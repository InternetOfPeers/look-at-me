package com.brainmote.lookatme;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.bean.ChatMessage;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.ImageUtil;

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
		int backgroundResId;
		Bitmap profileImageBitmap;
		ChatMessage message = getItem(position);
		// Imposto il background
		if (message.isMine()) {
			profileImageBitmap = ImageUtil.bitmapForCustomThumbnail(Services.currentState.getMyBasicProfile().getMainProfileImage().getImageBitmap(), 50);
			backgroundResId = R.drawable.right_message_bg;
		} else {
			backgroundResId = R.drawable.left_message_bg;
			profileImageBitmap = conversation.getImageBitmap();
		}
		ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
		photoImage.setImageBitmap(profileImageBitmap);
		convertView.setBackgroundResource(backgroundResId);
		TextView lastMessageText = (TextView) convertView.findViewById(R.id.lastMessageText);
		lastMessageText.setText(message.getText());
		TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);
		String timeElapsed = CommonUtils.timeElapsed(message.getTimestamp(), new Date(System.currentTimeMillis()));
		lastMessageDate.setText(timeElapsed);
		return convertView;
	}
}
