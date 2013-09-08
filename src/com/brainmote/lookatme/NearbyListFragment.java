package com.brainmote.lookatme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.Log;
import com.brainmote.lookatme.util.Nav;
import com.squareup.otto.Subscribe;

public class NearbyListFragment extends Fragment implements OnItemClickListener {

	private NearbyListAdapter nearbyListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.fragment_nearby_list, null);
		nearbyListAdapter = new NearbyListAdapter(this.getActivity());
		GridView socialListView = (GridView) view.findViewById(R.id.nearbyListView);
		socialListView.setAdapter(nearbyListAdapter);
		socialListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refreshFragment();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int clickedItemPosition, long clickedItemID) {
		Node node = (Node) nearbyListAdapter.getItem((int) clickedItemID);
		Bundle parameters = new Bundle();
		parameters.putString(Nav.NODE_KEY_ID, node.getId());
		Nav.startActivityWithParameters(getActivity(), ProfileActivity.class, parameters);
	}

	@Override
	public void onStart() {
		super.onStart();
		Services.event.register(this);
		refreshFragment();
	}

	@Override
	public void onStop() {
		super.onStop();
		Services.event.unregister(this);
	}

	@Subscribe
	public void onEventReceived(Event event) {
		switch (event.getEventType()) {
		case BASIC_PROFILE_RECEIVED:
		case NODE_LEFT:
		case LIKE_RECEIVED:
			refreshFragment();
			break;
		default:
			break;
		}
	}

	private void refreshFragment() {
		nearbyListAdapter.notifyDataSetChanged();
		verifyNearbyState();
	}

	/**
	 * Verifica se è necessario mostrare un messaggio all'utente
	 */
	private void verifyNearbyState() {
		GridView messageListView = (GridView) getView().findViewById(R.id.nearbyListView);
		TextView messageText = (TextView) getView().findViewById(R.id.nearby_text_message);
		// Verifico lo stato della connessione alla rete WiFi
		if (!CommonUtils.isWifiConnected(this.getActivity()) && !CommonUtils.isEmulator()) {
			messageText.setText(getString(R.string.message_wifi_required));
			return;
		}
		// Verifica se il servizio di background è attivo
		if (!Services.businessLogic.isRunning()) {
			messageText.setText(getString(R.string.nearby_message_offline_mode));
			return;
		}
		// Verifica se ci sono utenti visibili
		messageText.setText((messageListView.getAdapter().getCount() > 0 ? "" : getString(R.string.nearby_message_no_users_yet)));
	}

}