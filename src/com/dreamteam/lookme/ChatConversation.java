package com.dreamteam.lookme;

import java.util.Date;

import android.graphics.Bitmap;

import com.dreamteam.lookme.bean.ChatMessage;

public interface ChatConversation {

	String getId();

	String getNickname();

	Date getLastMessageTimestamp();

	Bitmap getImageBitmap();

	String getNodeId();

	ChatMessage getMessage(int position);

	int size();

	void addMessage(ChatMessage message);

	boolean isEmpty();

}
