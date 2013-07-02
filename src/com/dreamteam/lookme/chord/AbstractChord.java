/*
 * Author: Carlo Tassi
 * 
 * In Look@me App exists 1 public channel and 2 private
 * channels.
 * First private channel is used to social local functions,
 * second private channel is used to love game functions.
 * Users can decide to join one or both private channels
 */
package com.dreamteam.lookme.chord;

import android.content.Context;

import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public abstract class AbstractChord {

	private final ChordManager mChordManager;
	private IChordChannel mPublicChannel;
	private IChordChannel mPrivateChannel;
	private final IChordManagerListener mChordManagerListener = null;
	private final IChordChannelListener mChordPublicChannelListener = null;
	private final IChordChannelListener mChordPrivateChannelListener = null;
	
	AbstractChord(Context context, String gameName) {
		mChordManager = ChordManager.getInstance(context);
		
		int result = mChordManager.start(ChordManager.INTERFACE_TYPE_WIFI, mChordManagerListener);

		if (result != ChordManager.ERROR_NONE) {
			//onChordStartFailed(result);
		}
	}
}
