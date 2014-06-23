package com.brainmote.lookatme.service.impl;

import android.content.Context;

import com.brainmote.lookatme.service.GroupPlayManager;
import com.brainmote.lookatme.util.Log;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.groupplay.Sgp;
import com.samsung.android.sdk.groupplay.SgpGroupPlay;
import com.samsung.android.sdk.groupplay.SgpGroupPlay.SgpConnectionStatusListener;

public class GroupPlayManagerImpl implements GroupPlayManager, SgpConnectionStatusListener {
	public static class Factory {
		private static GroupPlayManagerImpl instance;

		public static GroupPlayManager getGroupPlayManager() {
			return instance == null ? instance = new GroupPlayManagerImpl() : instance;
		}
	}

	private SgpGroupPlay groupPlay;

	@Override
	public void init(Context context) throws SsdkUnsupportedException {
		Sgp sgp = new Sgp();
		// Inizializzo la group play sdk
		sgp.initialize(context);
		// Tento di agganciare l'SDK a Group Play (l'applicazione)
		groupPlay = new SgpGroupPlay(this);
		groupPlay.start();
	}

	@Override
	public void onConnected(SgpGroupPlay sdk) {
		Log.d();
		if (sdk.hasSession()) {
			Log.d("hasSession implica mando lo stato di joined all'app GP");
			sdk.setParticipantInfo(true);
		}
	}

	@Override
	public void onDisconnected() {
		Log.d("L'SDK si è disconnessa da Group Play.");
	}

	@Override
	public boolean isReady() {
		return groupPlay != null
				&& (groupPlay.getGroupPlayStatus() == SgpGroupPlay.STATUS_HAS_SESSION_AS_CLIENT || groupPlay.getGroupPlayStatus() == SgpGroupPlay.STATUS_HAS_SESSION_AS_HOST);
	}

	@Override
	public void onSessionClosed() {
		// TODO Auto-generated method stub
		
	}
}
