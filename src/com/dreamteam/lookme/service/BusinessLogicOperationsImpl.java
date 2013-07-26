package com.dreamteam.lookme.service;

import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.IBinder;

import com.dreamteam.lookme.BusinessLogicOperations;
import com.dreamteam.util.Log;

public class BusinessLogicOperationsImpl extends Service implements BusinessLogicOperations {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.BusinessLogicOperationsImpl.";
	private static final String SERVICE_START = SERVICE_PREFIX + "SERVICE_START";
	private static final String SERVICE_STOP = SERVICE_PREFIX + "SERVICE_STOP";

	private boolean isRunning;

	/**
	 * 
	 * @param context
	 */
	@Override
	public void start(ContextWrapper context) {
		context.startService(new Intent(SERVICE_START));
	}

	/**
	 * 
	 * @param context
	 */
	@Override
	public void stop(ContextWrapper context) {
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

	public BusinessLogicOperationsImpl() {
	}

	public static class Factory {
		private static BusinessLogicOperationsImpl instance;

		public static BusinessLogicOperationsImpl getBusinessLogicOperations() {
			if (instance == null) {
				instance = new BusinessLogicOperationsImpl();
				instance.onCreate();
			}
			return instance;
		}
	}

}
