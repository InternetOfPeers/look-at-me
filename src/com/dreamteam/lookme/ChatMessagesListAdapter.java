package com.dreamteam.lookme;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamteam.lookme.bean.ChatMessage;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;

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
			convertView = layoutInflater.inflate(R.layout.fragment_chat_messages_list_row, null);
		}
		int backgroundResId;
		Bitmap profileImageBitmap;
		// boolean leftSide = true;
		ChatMessage message = getItem(position);
		// Imposto il background
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		if (message.isMine()) {
			profileImageBitmap = Services.currentState.getMyBasicProfile().getMainProfileImage().getImageBitmap();
			backgroundResId = R.drawable.left_message_bg;
			params.gravity = Gravity.RIGHT;
		} else {
			backgroundResId = R.drawable.right_message_bg;
			profileImageBitmap = conversation.getImageBitmap();
		}
		ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
		photoImage.setImageBitmap(profileImageBitmap);
		convertView.setBackgroundResource(backgroundResId);
		TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
		nickNameText.setText(message.getFrom());
		TextView lastMessageText = (TextView) convertView.findViewById(R.id.lastMessageText);
		lastMessageText.setText(message.getText());
		TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);
		String timeElapsed = CommonUtils.timeElapsed(message.getTimestamp(), new Date(System.currentTimeMillis()));
		lastMessageDate.setText(timeElapsed);
		return convertView;
	}
}
