/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dreamteam.lookme.chord.LookAtMeChordCommunicationManager;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationManager;
import com.dreamteam.lookme.error.LookAtMeException;

public class CommunicationService extends Service {

	private static final String TAG = "LOOKATME_SERVICE";
	private static final String TAGClass = "[CommunicationService]";

	private ILookAtMeCommunicationManager communicationManager;

	public class CommunicationServiceBinder extends Binder {
		public CommunicationService getService() {
			Log.d(TAG, TAGClass + "[CommunicationServiceBinder] : getService");
			return CommunicationService.this;
		}
	}

	private final IBinder binder = new CommunicationServiceBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, TAGClass + " : " + "onBind");
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, TAGClass + " : " + "onStartCommand");
		return super.onStartCommand(intent, START_NOT_STICKY, startId);
	}

	public void initialize(Context context,
			ILookAtMeCommunicationListener listener) {
		Log.d(TAG, TAGClass + " : " + "initialize");
		communicationManager = new LookAtMeChordCommunicationManager(context,
				getMainLooper(), listener);
	}

	public void start() throws LookAtMeException {
		Log.d(TAG, TAGClass + " : " + "start");
		communicationManager.startCommunication();
	}

	public void stop() {
		Log.d(TAG, TAGClass + " : " + "stop");
		communicationManager.stopCommunication();
	}

	public void refreshSocialList() {
		Log.d(TAG, TAGClass + " : " + "refreshSocialList");
		communicationManager.sendProfilePreviewRequestAll();
	}

}
