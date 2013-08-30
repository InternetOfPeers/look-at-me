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
import com.brainmote.lookatme.service.NotificationType;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.Log;
import com.brainmote.lookatme.util.Nav;

public class NotificationServiceImpl implements NotificationService {

	private SparseIntArray counters = new SparseIntArray();

	@Override
	public void chatMessage(Context context, String fromName, String fromNodeId, String message, String conversationId) {
		String title = "New messagge from " + fromName;
		notifyMessage(context, ChatMessagesActivity.class, NotificationType.CHAT_MESSAGE_RECEIVED, title, message, fromNodeId, conversationId);
	}

	@Override
	public void profileView(Context context, String fromName) {
		String title = "Your profile has been visited";
		String message = "Your profile has been visited by " + fromName;
		notifyMessage(context, ProfileActivity.class, NotificationType.YOUR_PROFILE_WAS_VISITED, title, message);
	}

	@Override
	public void like(Context context, String fromName, String fromNode) {
		String title = fromName + " liked your profile!";
		String message = fromName + " liked your profile!";
		notifyMessage(context, ProfileActivity.class, NotificationType.SOMEONE_LIKED_YOU, title, message, fromNode, null);
	}

	@Override
	public void perfectMatch(Context context, String fromName) {
		String title = "Perfect match!";
		String message = "You and " + fromName + " creates a perfect match!";
		notifyMessage(context, ProfileActivity.class, NotificationType.PERFECT_MATCH, title, message);
	}

	@Override
	public int getChatMessagePendingNotifications() {
		return getPendingNotifications(NotificationType.CHAT_MESSAGE_RECEIVED);
	}

	@Override
	public int getProfileViewPendingNotifications() {
		return getPendingNotifications(NotificationType.YOUR_PROFILE_WAS_VISITED);
	}

	@Override
	public int getLikePendingNotifications() {
		return getPendingNotifications(NotificationType.SOMEONE_LIKED_YOU);
	}

	@Override
	public int getPerfectMatchPendingNotifications() {
		return getPendingNotifications(NotificationType.PERFECT_MATCH);
	}

	@Override
	public void clearExternalSystemNotifications(Activity activity) {
		if (activity != null && activity.getIntent() != null && activity.getIntent().getExtras() != null
				&& activity.getIntent().getExtras().containsKey(Nav.NOTIFICATION_KEY_ID)) {
			counters.put(activity.getIntent().getExtras().getInt(Nav.NOTIFICATION_KEY_ID), 0);
		}
	}

	@Override
	public void clearLocalNotifications(NotificationType notificationType) {
		Log.d("clear " + notificationType);
		counters.put(notificationType.getInt(), 0);
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
	private void notifyMessage(Context context, Class<? extends Activity> destinationActivity, NotificationType notificationID, String title, String message) {
		notifyMessage(context, destinationActivity, notificationID, title, message, null, null);
	}

	private void notifyMessage(Context context, Class<? extends Activity> destinationActivity, NotificationType notificationID, String title, String message,
			String fromNodeId, String conversationId) {
		// Verifica lo stato dell'applicazione, standby o attualmente
		// utilizzata, monitor on o off, e si comporta di conseguenza
		if (CommonUtils.isApplicationInForeground(context) && CommonUtils.isScreenOn(context)) {
			// Modifico il messaggio se la notifica è di chat
			if (notificationID == NotificationType.CHAT_MESSAGE_RECEIVED)
				message = title;
			// Verifica il tipo di notifica: se è di tipo chat e l'utente è
			// nella chat activity, non mostra il toast
			if (notificationID == NotificationType.CHAT_MESSAGE_RECEIVED
					&& CommonUtils.getForegroundActivityClassName(context).equals(ChatMessagesActivity.class.getCanonicalName())) {
				// TODO Eseguo un suono discreto di default, non quello di
				// notifica normale
			} else {
				// Mostro un toast
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				// Emetto un suono di default per la notifica
				planNotificationSound(context);
				// Aumento il counter della chat
				counters.put(notificationID.getInt(), counters.get(notificationID.getInt()) + 1);
			}
		} else {
			// Creo una notifica di sistema
			// Aumenta il counter per il tipo di notifica selezionato
			counters.put(notificationID.getInt(), counters.get(notificationID.getInt()) + 1);
			// Crea la notifica da inviare
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(message)
					.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true).setNumber(counters.get(notificationID.getInt()));
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
			mNotificationManager.notify(notificationID.getInt(), mBuilder.build());
			// Emetto un suono di default per la notifica
			planNotificationSound(context);
		}
	}

	private void planNotificationSound(Context context) {
		MediaPlayer mp = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
		if (mp != null) {
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

	private int getPendingNotifications(NotificationType notificationID) {
		return counters.get(notificationID.getInt());
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
