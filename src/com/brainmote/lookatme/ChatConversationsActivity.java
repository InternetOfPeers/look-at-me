package com.brainmote.lookatme;

import android.os.Bundle;
import android.view.Menu;

import com.brainmote.lookatme.service.NotificationType;
import com.brainmote.lookatme.service.Services;

public class ChatConversationsActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_conversations);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		checkIfProfileIsCompleted();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Cancello le notifiche di chat
		Services.notification.clearLocalNotifications(NotificationType.CHAT_MESSAGE_RECEIVED);
		return super.onPrepareOptionsMenu(menu);
	}
}
