package com.dreamteam.lookme.service.impl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.dreamteam.lookme.chord.CommunicationListenerImpl;
import com.dreamteam.lookme.chord.CommunicationManager;
import com.dreamteam.lookme.chord.CommunicationManagerImpl;
import com.dreamteam.lookme.chord.CustomException;
import com.dreamteam.lookme.service.BusinessLogicService;
import com.dreamteam.util.Log;

public class BusinessLogicServiceImpl extends Service implements BusinessLogicService {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.BusinessLogicOperationsImpl.";
	private static final String SERVICE_START = SERVICE_PREFIX + "SERVICE_START";
	private static final String SERVICE_STOP = SERVICE_PREFIX + "SERVICE_STOP";

	private boolean isRunning;
	private CommunicationManager communicationManager;

	/**
	 * 
	 * @param context
	 */
	@Override
	public void start(Context context) {
		context.startService(new Intent(SERVICE_START));
		isRunning = true;
		try {
			if (communicationManager == null) {
				communicationManager = new CommunicationManagerImpl(context, new CommunicationListenerImpl());
			}
			communicationManager.startCommunication();
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param context
	 */
	@Override
	public void stop(Context context) {
		communicationManager.stopCommunication();
		context.stopService(new Intent(SERVICE_STOP));
		isRunning = false;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void onCreate() {
		Log.d();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d();
		isRunning = true;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d();
		return null;
	}

	@Override
	public void onDestroy() {
		Log.d();
		isRunning = false;
		super.onDestroy();
	}

	/**
	 * Poiché il costruttore viene referenziato da Android (anche se non avviato
	 * direttamente) non è possibile impostare il costruttore come privato. Ad
	 * ogni modo, per accedere correttamente all'istanza dall'applicazione
	 * bisogna utilizzare la factory apposita.
	 */
	public BusinessLogicServiceImpl() {
	}

	public static class Factory {
		private static BusinessLogicServiceImpl instance;

		public static BusinessLogicServiceImpl getBusinessLogicService() {
			if (instance == null) {
				instance = new BusinessLogicServiceImpl();
				instance.onCreate();
			}
			return instance;
		}
	}

	@Override
	public void sendLike(String nodeId) {
		Log.d();
		communicationManager.sendLike(nodeId);
	}

	@Override
	public void sendFullProfileRequest(String toNodeId) {
		Log.d();
		communicationManager.sendFullProfileRequest(toNodeId);
	}

	@Override
	public boolean sendStartChatMessage(String nodeTo) {
		Log.d();
		return communicationManager.sendStartChatMessage(nodeTo);
	}

	@Override
	public void refreshSocialList() {
		Log.d();
		communicationManager.sendBasicProfileRequestAll();
	}

	@Override
	public boolean sendChatMessage(String nodeTo, String message) {
		Log.d();
		return communicationManager.sendChatMessage(nodeTo, message);
	}

}
