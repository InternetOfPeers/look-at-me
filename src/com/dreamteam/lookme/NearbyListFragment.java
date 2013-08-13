package com.dreamteam.lookme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;
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
		Log.d();

		final Node node = (Node) nearbyListAdapter.getItem((int) clickedItemID);

		Bundle parameters = new Bundle();
		parameters.putString(Nav.PROFILE_ID_KEY, node.getId());
		Nav.startActivityWithParameters(getActivity(), ProfileActivity.class, parameters);

		// final Node node = (Node) nearbyListAdapter.getItem((int)
		// clickedItemID);
		// final Dialog dialog = new Dialog(this.getActivity());
		// arg1.setAlpha(1);
		// // tell the Dialog to use the dialog.xml as it's layout description
		// dialog.setContentView(R.layout.chosed_profile_dialog);
		// dialog.setTitle("What do u wanna do?");
		//
		// TextView txt = (TextView) dialog.findViewById(R.id.nickname_txt);
		// txt.setText(node.getProfile().getNickname());
		// txt = (TextView) dialog.findViewById(R.id.gender_txt);
		// txt.setText(node.getProfile().getGender());
		// txt = (TextView) dialog.findViewById(R.id.matching_score_txt);
		// txt.setText("90%");
		//
		// Button dialogButton = (Button)
		// dialog.findViewById(R.id.startChat_btn);
		// dialogButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// Services.businessLogic.startChat(node.getId());
		// Bundle parameters = new Bundle();
		// parameters.putString(NotificationService.CONVERSATION_KEY_ID,
		// CommonUtils.getConversationId(Services.currentState.getMyBasicProfile().getId(),
		// node.getProfile().getId()));
		// Nav.startActivityWithParameters(getActivity(),
		// ChatMessagesActivity.class, parameters);
		// }
		//
		// });
		//
		// Button viewFullProfileButton = (Button)
		// dialog.findViewById(R.id.viewFullProfile_btn);
		// viewFullProfileButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// Services.businessLogic.requestFullProfile(node.getId());
		// // entro in attesa
		// loadingDialog = new
		// ProgressDialog(NearbyListFragment.this.getActivity());
		// loadingDialog.setTitle("Loading profile");
		// loadingDialog.show();
		// }
		//
		// });
		// dialog.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		Services.event.register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		Services.event.unregister(this);
	}

	@Subscribe
	public void onEventReceived(Event event) {
		switch (event.getEventType()) {
		case NODE_JOINED:
		case NODE_LEFT:
			refreshFragment();
			break;
		default:
			break;
		}
	}

	private void refreshFragment() {
		nearbyListAdapter.notifyDataSetChanged();
	}

}