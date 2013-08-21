package com.brainmote.lookatme.service;

import android.app.Activity;
import android.content.Context;

public interface NotificationService {

	final int CHAT_ID = 0;
	final int PROFILE_ID = 1;
	final int LIKED_ID = 2;
	final int PERFECT_MATCH_ID = 3;

	void clearActivityNotifications(Activity context);

	void chatMessage(Context context, String fromName, String fromNodeId, String message, String conversationId);

	int getChatMessagePendingNotifications();

	void profileView(Context context, String fromName);

	int getProfileViewPendingNotifications();

	void like(Context context, String fromName, String fromNode);

	int getLikePendingNotifications();

	void perfectMatch(Context context, String fromName);

	int getPerfectMatchPendingNotifications();
}
