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
	private BasicProfile basicProfile;
	private Bitmap otherThumbnail;
	private Date lastMessageTimestamp;
	private List<ChatMessage> chatMessagesList;

	public ChatConversationImpl(String conversationId, BasicProfile basicProfile) {
		this.conversationId = conversationId;
		this.chatMessagesList = new ArrayList<ChatMessage>();
		this.lastMessageTimestamp = new Date();
		setBasicProfile(basicProfile);
	}

	@Override
	public String getId() {
		return conversationId;
	}

	@Override
	public String getNickname() {
		return basicProfile.getNickname();
	}

	@Override
	public Date getLastMessageTimestamp() {
		return lastMessageTimestamp;
	}

	@Override
	public Bitmap getImageBitmap() {
		return otherThumbnail;
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
	public ChatConversation addMessage(ChatMessage message) {
		chatMessagesList.add(message);
		lastMessageTimestamp = message.getTimestamp();
		return this;
	}

	@Override
	public boolean isEmpty() {
		return chatMessagesList.isEmpty();
	}

	@Override
	public int getAge() {
		return basicProfile.getAge();
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

	@Override
	public void setBasicProfile(BasicProfile basicProfile) {
		this.basicProfile = basicProfile;
		this.otherThumbnail = ImageUtil.bitmapForCustomThumbnail(basicProfile.getMainProfileImage().getImageBitmap(), Services.currentState.getContext().getResources()
				.getDimensionPixelSize(R.dimen.chat_conversations_list_thumbnail_size));

	}

}