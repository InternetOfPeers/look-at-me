package com.brainmote.lookatme.service.impl;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.brainmote.lookatme.ChatMessagesActivity;
import com.brainmote.lookatme.ProfileActivity;
import com.brainmote.lookatme.R;
import com.brainmote.lookatme.service.NotificationService;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.Log;
import com.brainmote.lookatme.util.Nav;

public class NotificationServiceImpl implements NotificationService {

	private SparseIntArray counters = new SparseIntArray();

	@Override
	public void chatMessage(Context context, String fromName, String fromNodeId, String message, String conversationId) {
		String title = "Messagge from " + fromName;
		notifyMessage(context, ChatMessagesActivity.class, CHAT_ID, title, message, fromNodeId, conversationId);
	}

	@Override
	public void profileView(Context context, String fromName) {
		String title = "Your profile has been visited";
		String message = "Your profile has been visited by " + fromName;
		notifyMessage(context, ProfileActivity.class, PROFILE_ID, title, message);
	}

	@Override
	public void like(Context context, String fromName, String fromNode) {
		String title = fromName + " liked your profile!";
		String message = fromName + " liked your profile!";
		notifyMessage(context, ProfileActivity.class, LIKED_ID, title, message, fromNode, null);
	}

	@Override
	public void perfectMatch(Context context, String fromName) {
		String title = "Perfect match!";
		String message = "You and " + fromName + " creates a perfect match!";
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
				&& activity.getIntent().getExtras().containsKey(Nav.NOTIFICATION_KEY_ID)) {
			counters.put(activity.getIntent().getExtras().getInt(Nav.NOTIFICATION_KEY_ID), 0);
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
		notifyMessage(context, destinationActivity, notificationID, title, message, null, null);
	}

	private void notifyMessage(Context context, Class<? extends Activity> destinationActivity, int notificationID, String title, String message, String fromNodeId,
			String conversationId) {
		// Verifica lo stato dell'applicazione (standby o attualmente
		// utilizzata) e si comporta di conseguenza
		// TODO Considerare anche che se il monitor è spento andrebbe comunque
		// inviata la notifica come se l'app fosse in background, perché
		// effettivamente l'utente non la può vedere
		if (CommonUtils.isMyActivityInForeground(context)) {
			// Mostro un toast che notifica l'evento
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} else {
			// Creo una notifica di sistema
			// Aumenta il counter per il tipo di notifica selezionato
			counters.put(notificationID, counters.get(notificationID) + 1);
			// Crea la notifica da inviare
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(message)
					.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true).setNumber(counters.get(notificationID));
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, destinationActivity);
			resultIntent.putExtra(Nav.NOTIFICATION_KEY_ID, notificationID);
			resultIntent.putExtra(Nav.NODE_KEY_ID, fromNodeId);
			resultIntent.putExtra(Nav.CONVERSATION_KEY_ID, conversationId);
			// The stack builder object will contain an artificial back stack
			// for the started Activity. This ensures that navigating backward
			// from the Activity leads out of your application to the Home
			// screen.
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
			// Emetto un suono di default per la notifica
			MediaPlayer mp = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.i();
					mp.release();
				}
			});
			mp.start();
		}
	}

	private int getPendingNotifications(int notificationID) {
		return counters.get(notificationID);
	}

	private NotificationServiceImpl() {

	}

	public static class Factory {
		private static NotificationServiceImpl instance;

		public static NotificationServiceImpl getNotificationService() {
			return instance == null ? instance = new NotificationServiceImpl() : instance;
		}
	}

}
