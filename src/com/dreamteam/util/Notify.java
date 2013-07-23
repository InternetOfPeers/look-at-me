package com.dreamteam.util;

import android.app.Activity;

public interface Notify {

	void chatMessage(Activity activity, String fromName, String message);

	int getChatMessagePendingNotifications();

	void profileView(Activity activity, String fromName);

	int getProfileViewPendingNotifications();

	void like(Activity activity, String fromName);

	int getLikePendingNotifications();

	void perfectMatch(Activity activity, String fromName);

	int getPerfectMatchPendingNotifications();
}
