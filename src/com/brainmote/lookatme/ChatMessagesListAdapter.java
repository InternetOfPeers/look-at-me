package com.brainmote.lookatme;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.bean.ChatMessage;

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
		// Per rendere esteticamente migliore la chat, e dare movimento al
		// layout, aggiungo un padding casuale a quello presente di base
		// NOTA: Poiché la getView viene chiamata anche l'item deve essere
		// ridisegnato, quando l'utente effettua lo scroll ed il baloon esce
		// dalla view e rientra, viene chiamata nuovamente questo metodo e gli
		// viene assegnato un nuovo padding. Questo crea un effetto di
		// "trasformazione" del padding anche all'apertura della tastiera. Non è
		// fastidioso, ma lo metterei tra le cose da correggere comunque
		int randomRightPadding = 20 + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, new Random().nextInt(31), activity.getResources().getDisplayMetrics());
		int randomLeftPadding = 20 + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, new Random().nextInt(21), activity.getResources().getDisplayMetrics());
		// Effettuo operazioni differenti a seconda che sia un messaggio
		// dell'utente o di un'altro
		TextView lastMessageText;
		TextView lastMessageTime;
		View meView = convertView.findViewById(R.id.me);
		View otherView = convertView.findViewById(R.id.other);
		if (message.isMine()) {
			// Seleziono il layout corretto da visualizzare
			meView.setVisibility(View.VISIBLE);
			otherView.setVisibility(View.GONE);
			meView.setPadding(randomLeftPadding, meView.getPaddingTop(), meView.getPaddingRight(), meView.getPaddingBottom());
			// Aggancio il campo per il messaggio
			lastMessageText = (TextView) convertView.findViewById(R.id.myLastMessageText);
			// Aggancio il campo per il timestamp
			lastMessageTime = (TextView) convertView.findViewById(R.id.myLastMessageTime);
		} else {
			meView.setVisibility(View.GONE);
			otherView.setVisibility(View.VISIBLE);
			otherView.setPadding(otherView.getPaddingLeft(), otherView.getPaddingTop(), randomRightPadding, otherView.getPaddingBottom());
			// Imposto la foto
			ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
			photoImage.setImageBitmap(conversation.getImageBitmap());
			// Aggancio il campo per il messaggio
			lastMessageText = (TextView) convertView.findViewById(R.id.otherLastMessageText);
			// Aggancio il campo per il timestamp
			lastMessageTime = (TextView) convertView.findViewById(R.id.otherLastMessageTime);
		}
		// Imposto il messaggio
		lastMessageText.setText(message.getText());
		// TODO Se necessario (se è cambiato giorno rispetto all'ultimo
		// messaggio) imposto la data

		// TODO Se necessario (se è cambiato minuto rispetto all'ultimo
		// messaggio) imposto il time
		if (true) {
			lastMessageTime.setText(""); // lastMessageTime.setText(FormatUtils.formatMessageShowTime(message));
		}
		return convertView;
	}
}
