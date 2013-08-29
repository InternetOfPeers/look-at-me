package com.brainmote.lookatme;

import java.util.Date;

import android.graphics.Bitmap;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.ChatMessage;

public interface ChatConversation {

	String getId();

	String getNickname();

	int getAge();

	Date getLastMessageTimestamp();

	Bitmap getImageBitmap();

	ChatMessage getMessage(int position);

	int size();

	ChatConversation addMessage(ChatMessage message);

	boolean isEmpty();

	CharSequence getLastMessage();

	void setBasicProfile(BasicProfile basicProfile);

}
