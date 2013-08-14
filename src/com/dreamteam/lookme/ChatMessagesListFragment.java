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
import com.squareup.otto.Subscribe;

public class ChatMessagesListFragment extends Fragment {

	private ChatMessagesListAdapter chatMessagesListAdapter;
	private ListView messageListView;
	private EditText mInputEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.fragment_chat_messages_list, null);
		ChatMessagesActivity activity = (ChatMessagesActivity) getActivity();
		// Recupero la conversazione passata
		final ChatConversation conversation = activity.getConversation();
		if (conversation == null) {
			// Se non è stata passata alcuna conversazione ritorno una vista
			// vuota
			// TODO Migliorare la messaggistica
			activity.showDialog("Error", "Conversazione non trovata", false);
			return view;
		}
		// Imposto il title dell'activity con il nome e l'età della persona con
		// cui sto chattando
		String age = conversation.getAge() > 0 ? ", " + String.valueOf(conversation.getAge()) : "";
		activity.setTitle(conversation.getNickname() + age);
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
