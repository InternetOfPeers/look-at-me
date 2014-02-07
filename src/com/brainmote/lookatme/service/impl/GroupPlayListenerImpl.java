package com.brainmote.lookatme.service.impl;

import com.brainmote.lookatme.util.Log;
import com.samsung.android.sdk.groupplay.SgpGroupPlay;
import com.samsung.android.sdk.groupplay.SgpGroupPlay.SgpConnectionStatusListener;

public class GroupPlayListenerImpl implements SgpConnectionStatusListener {

	@Override
	public void onConnected(SgpGroupPlay sdk) {
		Log.d();
		if (sdk.hasSession()) {
			Log.d("hasSession");
			sdk.setParticipantInfo(true);
		}

	}

	@Override
	public void onDisconnected() {
		Log.d("ci fa piacere...");
	}

}
