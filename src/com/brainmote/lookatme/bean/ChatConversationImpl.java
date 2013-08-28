package com.brainmote.lookatme.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.R;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;

public class ChatConversationImpl implements ChatConversation {

	private String conversationId;
	private String otherNickname;
	private int otherAge;
	private Bitmap otherImageBitamp;
	private Date lastMessageTimestamp;
	private List<ChatMessage> chatMessagesList;

	public ChatConversationImpl(String conversationId, String otherNickname, int otherAge, Bitmap otherImageBitamp) {
		this.conversationId = conversationId;
		this.otherNickname = otherNickname;
		this.otherAge = otherAge;
		this.otherImageBitamp = ImageUtil.bitmapForCustomThumbnail(otherImageBitamp,
				Services.currentState.getContext().getResources().getDimensionPixelSize(R.dimen.chat_conversations_list_thumbnail_size));
		this.chatMessagesList = new ArrayList<ChatMessage>();
		this.lastMessageTimestamp = new Date();
	}

	@Override
	public String getId() {
		return conversationId;
	}

	@Override
	public String getNickname() {
		return otherNickname;
	}

	@Override
	public Date getLastMessageTimestamp() {
		return lastMessageTimestamp;
	}

	@Override
	public Bitmap getImageBitmap() {
		return otherImageBitamp;
	}

	@Override
	public ChatMessage getMessage(int position) {
		return chatMessagesList.get(position);
	}

	@Override
	public int size() {
		return chatMessagesList.size();
	}

	@Override
	public void addMessage(ChatMessage message) {
		chatMessagesList.add(message);
		lastMessageTimestamp = message.getTimestamp();
	}

	@Override
	public boolean isEmpty() {
		return chatMessagesList.isEmpty();
	}

	@Override
	public int getAge() {
		return otherAge;
	}

	@Override
	public CharSequence getLastMessage() {
		if (chatMessagesList.size() < 1)
			return "";
		ChatMessage message = chatMessagesList.get(chatMessagesList.size() - 1);
		if (message == null)
			return "";
		return message.getText();
	}

}