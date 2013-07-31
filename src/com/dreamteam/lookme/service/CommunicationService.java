/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.dreamteam.lookme.chord.LookAtMeChordCommunicationManager;
import com.dreamteam.lookme.communication.EventBusProvider;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationManager;
import com.dreamteam.lookme.communication.LookAtMeCommunicationRepository;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.error.LookAtMeException;
import com.dreamteam.lookme.event.LookAtMeEvent;
import com.dreamteam.lookme.event.LookAtMeEventType;
import com.dreamteam.util.Log;

public class CommunicationService extends Service {

	public static final int SERVICE_RUNNING = 1;
	public static final int SERVICE_READY_TO_RUN = 0;

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.CommunicationService.";
	public static final String SERVICE_BIND = SERVICE_PREFIX + "SERVICE_BIND";
	public static final String SERVICE_START = SERVICE_PREFIX + "SERVICE_START";
	public static final String SERVICE_STOP = SERVICE_PREFIX + "SERVICE_STOP";

	private ILookAtMeCommunicationManager communicationManager;

	public class CommunicationServiceBinder extends Binder {
		public CommunicationService getService() {
			Log.d();
			return CommunicationService.this;
		}
	}

	private final IBinder binder = new CommunicationServiceBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d();
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d();
		return true;// super.onUnbind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d();
		return super.onStartCommand(intent, START_NOT_STICKY, startId);
	}

	public int initialize(Context context) {
		Log.d();
		if (communicationManager == null) {
			Log.d("service is not running");
			communicationManager = new LookAtMeChordCommunicationManager(context, getMainLooper(), new ILookAtMeCommunicationListener() {

				@Override
				public void onCommunicationStarted() {
					// TODO Auto-generated method stub
					Log.d();

				}

				@Override
				public void onCommunicationStopped() {
					// TODO Auto-generated method stub
					Log.d();

				}

				@Override
				public void onFullProfileNodeReceived(LookAtMeNode node) {
					Log.d();
					LookAtMeCommunicationRepository.getInstance().setProfileViewed(node);
					EventBusProvider.getIntance().post(new LookAtMeEvent(LookAtMeEventType.PROFILE_RECEIVED, node.getId()));
				}

				@Override
				public void onBasicProfileNodeReceived(LookAtMeNode node) {
					Log.d();
					LookAtMeCommunicationRepository.getInstance().putSocialNodeInMap(node);
					EventBusProvider.getIntance().post(new LookAtMeEvent(LookAtMeEventType.NODE_JOINED, node.getId()));
				}

				@Override
				public void onNodeLeft(String nodeName) {
					Log.d();
					LookAtMeCommunicationRepository.getInstance().removeSocialNodeFromMap(nodeName);
					EventBusProvider.getIntance().post(new LookAtMeEvent(LookAtMeEventType.NODE_LEFT, nodeName));
				}

				@Override
				public void onProfileNodeUpdated(LookAtMeNode node) {
					// TODO Auto-generated method stub
					Log.d();

				}

				@Override
				public void onLikeReceived(String nodeFrom) {
					Log.d();
					LookAtMeCommunicationRepository.getInstance().addLikedToSet(nodeFrom);
					EventBusProvider.getIntance().post(new LookAtMeEvent(LookAtMeEventType.LIKE_RECEIVED, nodeFrom));
				}

				@Override
				public void onStartChatMessageReceived(String nodeFrom, String channelName) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onChatMessageReceived(String nodeFrom, String message) {
					Log.d();
					// creo il MessageItem
					// metto il messaggio nella map
					EventBusProvider.getIntance().post(new LookAtMeEvent(LookAtMeEventType.CHAT_MESSAGE_RECEIVED, nodeFrom));
				}
			});
			return SERVICE_READY_TO_RUN;
		} else {
			Log.d("service was already running");
			return SERVICE_RUNNING;
		}
	}

	public void start() throws LookAtMeException {
		Log.d();
		communicationManager.startCommunication();
	}

	public void stop() {
		Log.d();
		communicationManager.stopCommunication();
	}

	public void refreshSocialList() {
		Log.d();
		communicationManager.sendBasicProfileRequestAll();
	}

	public void sendFullProfileRequest(String nodeTo) {
		Log.d();
		communicationManager.sendFullProfileRequest(nodeTo);
	}

	public void sendLike(String nodeTo) {
		Log.d();
		communicationManager.sendLike(nodeTo);
	}

	public boolean sendStartChatMessage(String nodeTo) {
		Log.d();
		return communicationManager.sendStartChatMessage(nodeTo);
	}

	public boolean sendChatMessage(String nodeTo, String message, String channel) {
		Log.d();
		return communicationManager.sendChatMessage(nodeTo, message, channel);
	}

}
