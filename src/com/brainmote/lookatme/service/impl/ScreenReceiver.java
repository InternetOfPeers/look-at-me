package com.brainmote.lookatme.service.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.brainmote.lookatme.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			Log.d("Screen OFF");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d("Screen ON");
		}
	}

}
