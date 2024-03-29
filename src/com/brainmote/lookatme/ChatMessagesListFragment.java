package com.brainmote.lookatme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.Services;
import com.squareup.otto.Subscribe;

public class ChatMessagesListFragment extends Fragment {

	private ChatMessagesListAdapter chatMessagesListAdapter;
	private ListView messageListView;
	private EditText mInputEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_messages_list, null);
		final ChatMessagesActivity activity = (ChatMessagesActivity) getActivity();
		// Recupero la conversazione passata
		final ChatConversation conversation = activity.getConversation();
		if (conversation == null) {
			// Se non è stata passata alcuna conversazione ritorno una vista
			// vuota
			// TODO Migliorare la messaggistica
			activity.showDialog(getActivity().getString(R.string.chat_messages_dialog_error_title), getActivity().getString(R.string.message_no_conversation_with_that_id));
			return view;
		}
		// Mi assicuro che l'utente si connetta al canale privato della
		// conversazione
		Services.businessLogic.joinConversation(conversation);
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
		ImageButton sendButton = (ImageButton) view.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mInputEditText.getText().toString();
				if (text != null && !text.isEmpty()) {
					if (Services.businessLogic.sendChatMessage(conversation, text)) {
						mInputEditText.getText().clear();
						ChatMessagesListFragment.this.refreshFragment();
						scrollMyListViewToBottom();
						// Se è un fake user, risponde al messaggio
						Services.businessLogic.setResponseIfConversationIsFake(conversation);
					} else {
						// L'utente non è più online e non posso mandargli
						// messaggi
						activity.showDialog(getString(R.string.message_warning), getString(R.string.unable_to_send_chat_message));
					}
				}
			}
		});
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
			refreshFragment();
			// TODO Immagino che questo evento scatti anche quando ricevo un
			// messaggio in una conversazione diversa da quella che sto
			// visualizzando, quindi è da controllare perché altrimenti è errata
			// senso l'operazione di scroll
			scrollMyListViewToBottom();
			break;
		case BASIC_PROFILE_RECEIVED:
			refreshFragment();
			ChatMessagesActivity activity = (ChatMessagesActivity) getActivity();
			ChatConversation conversation = activity.getConversation();
			String age = conversation.getAge() > 0 ? ", " + String.valueOf(conversation.getAge()) : "";
			activity.setTitle(conversation.getNickname() + age);
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
