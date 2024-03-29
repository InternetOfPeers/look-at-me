package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.FormatUtils;

public class ChatConversationsListAdapter extends BaseAdapter {

	private Activity activity;

	public ChatConversationsListAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return Services.currentState.getConversationsStore().size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public ChatConversation getItem(int position) {
		List<String> conversationsKeyList = new ArrayList<String>();
		conversationsKeyList.addAll(Services.currentState.getConversationsStore().keySet());
		// TODO ordinare la lista di conversazioni in base all'ultimo messaggio
		// ricevuto (la conversazione con i messaggi più recenti va in testa,
		// come per gli SMS di android)
		String conversationKey = conversationsKeyList.get(position);
		return Services.currentState.getConversationsStore().get(conversationKey);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// LayoutInflater class is used to instantiate layout XML file into
			// its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.fragment_chat_conversations_list_item, null);
		}
		// Recupero la conversazione selezionata
		ChatConversation conversation = getItem(position);
		// Mostro la persona con la quale sto chattando
		TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
		nickNameText.setText(conversation.getNickname());
		// Mostro l'ultimo messaggio inviato
		TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessageText);
		lastMessage.setText(conversation.getLastMessage());
		// Mostro il tempo trascorso dall'ultimo messaggio ricevuto nella
		// conversazione
		TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);
		lastMessageDate.setText(FormatUtils.formatConversationTimestamp(conversation));
		// Imposto l'immagine del profilo
		ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
		photoImage.setImageBitmap(conversation.getImageBitmap());
		return convertView;
	}

}
