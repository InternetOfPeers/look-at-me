package com.dreamteam.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.dreamteam.lookme.MessagesActivity;
import com.dreamteam.lookme.ProfileActivity;
import com.dreamteam.lookme.R;

public class NotifyImpl implements Notify {

	private final int CHAT_ID = 0;
	private final int PROFILE_ID = 1;
	private final int LIKED_ID = 2;
	private final int PERFECT_MATCH_ID = 3;
	private Map<Integer, Integer> counters = new HashMap<Integer, Integer>();

	@Override
	public void chatMessage(Activity currentActivity, String fromName, String message) {
		String title = "Messagge from " + fromName;
		notifyMessage(currentActivity, MessagesActivity.class, CHAT_ID, title, message);
	}

	@Override
	public void profileView(Activity currentActivity, String fromName) {
		String title = "Your profile has been visited";
		String message = "Your profile has been visited by " + fromName;
		// TOIMPROVE Andare al profilo della persona che ti ha visitato
		notifyMessage(currentActivity, ProfileActivity.class, PROFILE_ID, title, message);
	}

	@Override
	public void like(Activity currentActivity, String fromName) {
		String title = fromName + " liked your profile!";
		String message = fromName + " liked your profile!";
		// TOIMPROVE Andare al profilo della persona che ti ha messo like
		notifyMessage(currentActivity, ProfileActivity.class, LIKED_ID, title, message);
	}

	@Override
	public void perfectMatch(Activity currentActivity, String fromName) {
		String title = "Perfect match!";
		String message = "You and " + fromName + " creates a perfect match!";
		// TOIMPROVE Andare al profilo della persona che ti ha messo like
		notifyMessage(currentActivity, ProfileActivity.class, PERFECT_MATCH_ID, title, message);
	}

	/**
	 * Notifica l'utente con un messaggio
	 * 
	 * @param currentActivity
	 * @param destinationActivity
	 * @param notificationID
	 * @param title
	 * @param message
	 */
	private void notifyMessage(Activity currentActivity, Class<? extends Activity> destinationActivity, int notificationID, String title, String message) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(currentActivity).setContentTitle(title).setContentText(message)
				.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true).setNumber(5);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(currentActivity, destinationActivity);
		// The stack builder object will contain an artificial back stack for
		// the started Activity. This ensures that navigating backward from the
		// Activity leads out of your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(currentActivity);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(destinationActivity);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) currentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(notificationID, mBuilder.build());
	}

	private NotifyImpl() {

	}

	public static class Factory {
		private static NotifyImpl instance;

		public static NotifyImpl getNotify() {
			return instance == null ? instance = new NotifyImpl() : instance;
		}
	}

	@Override
	public int getChatMessagePendingNotifications() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getProfileViewPendingNotifications() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLikePendingNotifications() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPerfectMatchPendingNotifications() {
		throw new UnsupportedOperationException();
	}

}
