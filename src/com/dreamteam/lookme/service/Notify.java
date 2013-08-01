package com.dreamteam.lookme.service;

import android.app.Activity;
import android.content.Context;

public interface Notify {

	void clearActivityNotifications(Activity context);

	void chatMessage(Context context, String fromName, String message);

	int getChatMessagePendingNotifications();

	void profileView(Context context, String fromName);

	int getProfileViewPendingNotifications();

	void like(Context context, String fromName);

	int getLikePendingNotifications();

	void perfectMatch(Context context, String fromName);

	int getPerfectMatchPendingNotifications();
}
