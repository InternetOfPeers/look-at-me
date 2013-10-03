package com.brainmote.lookatme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.bean.ChatMessage;
import com.brainmote.lookatme.util.FormatUtils;

public class ChatMessagesListAdapter extends BaseAdapter {
	private ChatConversation conversation;
	private Activity activity;

	public ChatMessagesListAdapter(Activity activity, ChatConversation conversation) {
		this.activity = activity;
		this.conversation = conversation;
	}

	@Override
	public int getCount() {
		return conversation.size();
	}

	@Override
	public ChatMessage getItem(int position) {
		return conversation.getMessage(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.fragment_chat_messages_list_item, null);
		}
		ChatMessage message = getItem(position);
		// TODO Se necessario mostra il timestamp del messaggio
		TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);
		lastMessageDate.setText(FormatUtils.formatMessageTimestamp(message));
		// Effettuo operazioni differenti a seconda che sia un messaggio
		// dell'utente o di un'altro
		TextView lastMessageText;
		if (message.isMine()) {
			// Seleziono il layout corretto da visualizzare
			convertView.findViewById(R.id.me).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.other).setVisibility(View.GONE);
			lastMessageText = (TextView) convertView.findViewById(R.id.myLastMessageText);
		} else {
			convertView.findViewById(R.id.me).setVisibility(View.GONE);
			convertView.findViewById(R.id.other).setVisibility(View.VISIBLE);
			ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
			photoImage.setImageBitmap(conversation.getImageBitmap());
			lastMessageText = (TextView) convertView.findViewById(R.id.otherLastMessageText);
		}
		lastMessageText.setText(message.getText());
		return convertView;
	}
}
