package com.dreamteam.lookme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.NotificationService;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ChatConversationsListFragment extends Fragment implements OnItemClickListener {

	private ChatConversationsListAdapter conversationsListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_conversations_list, container, false);
		conversationsListAdapter = new ChatConversationsListAdapter(this.getActivity());
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
	public void onItemClick(AdapterView<?> parent, View view, int clickedItemPosition, long clickedItemID) {
		Bundle parameters = new Bundle();
		parameters.putString(NotificationService.CONVERSATION_KEY_ID, conversationsListAdapter.getItem(clickedItemPosition).getId());
		Nav.startActivityWithParameters(getActivity(), ChatMessagesActivity.class, parameters);
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

	private void refreshFragment() {
		conversationsListAdapter.notifyDataSetChanged();
	}

}
