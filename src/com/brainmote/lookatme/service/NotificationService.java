package com.brainmote.lookatme.service;

import android.app.Activity;
import android.content.Context;

public interface NotificationService {

	void clearExternalSystemNotifications(Activity context);

	void chatMessage(Context context, String fromName, String fromNodeId, String message, String conversationId);

	int getChatMessagePendingNotifications();

	void profileView(Context context, String fromName);

	int getProfileViewPendingNotifications();

	void like(Context context, String fromName, String fromNode);

	int getLikePendingNotifications();

	void perfectMatch(Context context, String fromName);

	int getPerfectMatchPendingNotifications();

	void clearLocalNotifications(NotificationType notificationType);
}
