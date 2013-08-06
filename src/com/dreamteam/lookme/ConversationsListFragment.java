package com.dreamteam.lookme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dreamteam.lookme.bean.ChatMessage;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ConversationsListFragment extends Fragment implements OnItemClickListener {

	private ConversationsListAdapter conversationsListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_list, container, false);
		conversationsListAdapter = new ConversationsListAdapter(this.getActivity());
		ListView messageListView = (ListView) view.findViewById(R.id.messageListView);
		messageListView.setAdapter(conversationsListAdapter);
		messageListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refreshFragment();
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
		case CHAT_MESSAGE_RECEIVED:
		case PROFILE_RECEIVED:
			refreshFragment();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int clickedItemPosition, long clickedItemID) {
		// NELLA LISTA DEI MESSAGGI DEVE ESSERCI SEMPRE UN MESSAGGIO CON IL
		// NODEID DEL NODO CON CUI STO CONVERSANDO
		
		
//		ChatMessage message = (ChatMessage) conversationsListAdapter.getItem((int) clickedItemID);
//		String profileId = Services.currentState.getSocialNodeMap().get(message.getFromNodeId()).getProfile().getId();
//		Nav.startActivityWithString(getActivity(), ChatActivity.class, CommonUtils.generateChannelName(Services.currentState.getMyBasicProfile().getId(), profileId));
	}

	private void refreshFragment() {
		conversationsListAdapter.notifyDataSetChanged();
	}

}
