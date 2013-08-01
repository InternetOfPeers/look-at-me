package com.dreamteam.lookme.service.impl;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseIntArray;

import com.dreamteam.lookme.MessagesActivity;
import com.dreamteam.lookme.ProfileActivity;
import com.dreamteam.lookme.R;
import com.dreamteam.lookme.service.Notify;

public class NotifyImpl implements Notify {

	private final String NOTIFICATION_KEY_ID = "notification_key_id";
	private final int CHAT_ID = 0;
	private final int PROFILE_ID = 1;
	private final int LIKED_ID = 2;
	private final int PERFECT_MATCH_ID = 3;
	private SparseIntArray counters = new SparseIntArray();

	@Override
	public void chatMessage(Context context, String fromName, String message) {
		String title = "Messagge from " + fromName;
		notifyMessage(context, MessagesActivity.class, CHAT_ID, title, message);
	}

	@Override
	public void profileView(Context context, String fromName) {
		String title = "Your profile has been visited";
		String message = "Your profile has been visited by " + fromName;
		// TOIMPROVE Andare al profilo della persona che ti ha visitato
		notifyMessage(context, ProfileActivity.class, PROFILE_ID, title, message);
	}

	@Override
	public void like(Context context, String fromName) {
		String title = fromName + " liked your profile!";
		String message = fromName + " liked your profile!";
		// TOIMPROVE Andare al profilo della persona che ti ha messo like
		notifyMessage(context, ProfileActivity.class, LIKED_ID, title, message);
	}

	@Override
	public void perfectMatch(Context context, String fromName) {
		String title = "Perfect match!";
		String message = "You and " + fromName + " creates a perfect match!";
		// TOIMPROVE Andare al profilo della persona che ti ha messo like
		notifyMessage(context, ProfileActivity.class, PERFECT_MATCH_ID, title, message);
	}

	@Override
	public int getChatMessagePendingNotifications() {
		return getPendingNotifications(CHAT_ID);
	}

	@Override
	public int getProfileViewPendingNotifications() {
		return getPendingNotifications(PROFILE_ID);
	}

	@Override
	public int getLikePendingNotifications() {
		return getPendingNotifications(LIKED_ID);
	}

	@Override
	public int getPerfectMatchPendingNotifications() {
		return getPendingNotifications(PERFECT_MATCH_ID);
	}

	@Override
	public void clearActivityNotifications(Activity activity) {
		if (activity != null && activity.getIntent() != null && activity.getIntent().getExtras() != null
				&& activity.getIntent().getExtras().containsKey(NOTIFICATION_KEY_ID)) {
			counters.put(activity.getIntent().getExtras().getInt(NOTIFICATION_KEY_ID), 0);
		}

	}

	/**
	 * Notifica l'utente con un messaggio
	 * 
	 * @param context
	 * @param destinationActivity
	 * @param notificationID
	 * @param title
	 * @param message
	 */
	private void notifyMessage(Context context, Class<? extends Activity> destinationActivity, int notificationID, String title, String message) {
		// Aumenta il counter per il tipo di notifica selezionato
		counters.put(notificationID, counters.get(notificationID) + 1);
		long[] vibrationPattern = new long[1];
		vibrationPattern[0] = 5;
		// Crea la notifica da inviare
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true).setNumber(counters.get(notificationID));
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, destinationActivity);
		resultIntent.putExtra(NOTIFICATION_KEY_ID, notificationID);
		// The stack builder object will contain an artificial back stack for
		// the started Activity. This ensures that navigating backward from the
		// Activity leads out of your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(destinationActivity);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(notificationID, mBuilder.build());
	}

	private int getPendingNotifications(int notificationID) {
		return counters.get(notificationID);
	}

	private NotifyImpl() {

	}

	public static class Factory {
		private static NotifyImpl instance;

		public static NotifyImpl getNotify() {
			return instance == null ? instance = new NotifyImpl() : instance;
		}
	}

}
