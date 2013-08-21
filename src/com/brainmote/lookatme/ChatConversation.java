package com.brainmote.lookatme;

import java.util.Date;

import android.graphics.Bitmap;

import com.brainmote.lookatme.bean.ChatMessage;

public interface ChatConversation {

	String getId();

	String getNickname();

	int getAge();

	Date getLastMessageTimestamp();

	Bitmap getImageBitmap();

	String getNodeId();

	ChatMessage getMessage(int position);

	int size();

	void addMessage(ChatMessage message);

	boolean isEmpty();

	CharSequence getLastMessage();

}
