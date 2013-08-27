package com.brainmote.lookatme.util;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
