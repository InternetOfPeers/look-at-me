package com.dreamteam.lookme.service;

import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.IBinder;

import com.dreamteam.util.Log;

public class SimpleCommunicationService extends Service {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.SimpleCommunicationService.";
	private static final String SERVICE_START = SERVICE_PREFIX + "SERVICE_START";
	private static final String SERVICE_STOP = SERVICE_PREFIX + "SERVICE_STOP";
	private static boolean isRunning;

	/**
	 * 
	 * @param context
	 */
	public static void start(ContextWrapper context) {
		context.startService(new Intent(SERVICE_START));
	}

	/**
	 * 
	 * @param context
	 */
	public static void stop(ContextWrapper context) {
		context.stopService(new Intent(SERVICE_STOP));
		isRunning = false;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isRunning() {
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

}
