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
import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationManager;
import com.dreamteam.lookme.error.LookAtMeException;
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

	public int initialize(Context context, ILookAtMeCommunicationListener listener) {
		Log.d();
		if (communicationManager == null) {
			Log.d("service is not running");
			communicationManager = new LookAtMeChordCommunicationManager(context, getMainLooper(), listener);
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
		communicationManager.sendProfilePreviewRequestAll();
	}

	public void sendProfileRequest(String nodeTo) {
		Log.d();
		communicationManager.sendProfileRequest(nodeTo);
	}

	public void sendLike(String nodeTo) {
		Log.d();
		communicationManager.sendLike(nodeTo);
	}

}
