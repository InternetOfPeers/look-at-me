package com.dreamteam.lookme;

import java.util.Date;
import java.util.List;

import com.dreamteam.lookme.bean.ChatMessage;

import android.graphics.Bitmap;

public interface ChatConversation extends List<ChatMessage> {

	String getId();

	String getNickname();

	Date getLastMessageTimestamp();

	Bitmap getImageBitmap();

	String getNode();

	ChatMessage getMessage(int position);

}
