package com.brainmote.lookatme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.NotificationService;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.Nav;
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
		verifyNoConversation();
	}

	private void verifyNoConversation() {
		// Verifica se è necessario mostrare un messaggio all'utente
		ListView messageListView = (ListView) getView().findViewById(R.id.messageListView);
		if (messageListView.getAdapter().getCount() > 0) {
			getView().findViewById(R.id.chat_conversations_message_no_conversation_yet).setVisibility(View.INVISIBLE);
		} else {
			getView().findViewById(R.id.chat_conversations_message_no_conversation_yet).setVisibility(View.VISIBLE);
		}

	}
}
