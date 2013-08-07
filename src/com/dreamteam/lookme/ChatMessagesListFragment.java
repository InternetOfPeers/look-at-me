package com.dreamteam.lookme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ChatMessagesListFragment extends Fragment {

	private ChatConversation conversation;
	private ChatMessagesListAdapter chatMessagesListAdapter;
	private ListView messageListView;
	private EditText mInputEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.fragment_chat_messages_list, null);
		String conversationId = Nav.getStringParameter(ChatMessagesListFragment.this.getActivity());
		if (conversationId.isEmpty())
			return view;
		// Recupero la conversazione passata
		conversation = Services.currentState.getConversationsStore().get(conversationId);
		if (conversation == null)
			return view;
		// Imposto il resto delle variabili di gestione dell'elenco dei
		// messaggi
		chatMessagesListAdapter = new ChatMessagesListAdapter(getActivity(), conversation);
		messageListView = (ListView) view.findViewById(R.id.chatMessagesListView);
		messageListView.setAdapter(chatMessagesListAdapter);
		mInputEditText = (EditText) view.findViewById(R.id.messageEdit);
		// Imposto l'invio del messaggio di testo al click del pulsante send
		Button sendButton = (Button) view.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mInputEditText.getText().toString();
				if (text != null && !text.isEmpty()) {
					// Verifico che il nodo a cui mandare il messaggio sia
					// ancora attivo
					String nodeId = conversation.getNodeId();
					if (nodeIsValid(nodeId)) {
						// Invio il messaggio al nodo
						Services.businessLogic.sendChatMessage(nodeId, text);
						mInputEditText.getText().clear();
						ChatMessagesListFragment.this.refreshFragment();
						scrollMyListViewToBottom();
					} else {
						// TODO Migliorare la messaggistica
						Toast toast = Toast.makeText(ChatMessagesListFragment.this.getActivity(), R.string.unable_to_send_chat_message, Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}

			private boolean nodeIsValid(String node) {
				// TODO Verificare che il nodo a cui mandare il messaggio sia
				// ancora attivo
				return true;
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d();
		super.onActivityCreated(savedInstanceState);
		// TODO questo refresh può crashare perché chatMessagesListAdapter
		// potrebbe essere ancora null. Non è detto che alla ricreazione
		// dell'activity sia corrisposta già la ricreazione della view
		// (onViewCreted) e quindi chatMessagesListAdapter potrebbe essere null.
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
			refreshFragment();
			scrollMyListViewToBottom();
			break;
		default:
			break;
		}
	}

	private void refreshFragment() {
		if (chatMessagesListAdapter != null)
			chatMessagesListAdapter.notifyDataSetChanged();
	}

	private void scrollMyListViewToBottom() {
		if (messageListView != null)
			messageListView.setSelection(messageListView.getCount() - 1);
	}

}
