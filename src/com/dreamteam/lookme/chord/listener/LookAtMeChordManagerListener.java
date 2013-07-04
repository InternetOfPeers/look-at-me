/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord.listener;

import android.util.Log;

import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class LookAtMeChordManagerListener implements IChordManagerListener {
	
	private ChordManager chordManager;
	private IChordChannelListener publicChannelListener;
	
	public LookAtMeChordManagerListener(ChordManager chordManager, IChordChannelListener publicChannelListener) {
		this.chordManager = chordManager;
		this.publicChannelListener = publicChannelListener;
	}

	@Override
	public void onError(int arg0) {
		Log.d("", "onError chord");
	}

	@Override
	public void onNetworkDisconnected() {
		Log.d("", "onNetworkDisconnected chord");
	}

	@Override
	public void onStarted(String arg0, int arg1) {
		Log.d("", "onStarted chord");
		
		// (optional) listen for public channel
        IChordChannel publicChannel = chordManager.joinChannel(ChordManager.PUBLIC_CHANNEL,
                publicChannelListener);
		if (publicChannel == null) {
            Log.e("", "fail to join public");
        }
		
	}
	

}
