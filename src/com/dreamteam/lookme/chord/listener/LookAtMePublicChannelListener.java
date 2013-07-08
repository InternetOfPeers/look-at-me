/**
 * Author: Carlo Tassi
 * 
 * Public channel listener will ignore all
 * received files because "it is best for users 
 * to share data, conversations, and files over 
 * a private channel, which is made up of only 
 * nodes running the same application" (docs).
 */
package com.dreamteam.lookme.chord.listener;

import com.samsung.chord.IChordChannelListener;

public class LookAtMePublicChannelListener implements IChordChannelListener {

	@Override
	public void onNodeJoined(String arg0, String arg1) {
		// NOP

	}

	@Override
	public void onNodeLeft(String arg0, String arg1) {
		// NOP

	}

	@Override
	public void onDataReceived(String arg0, String arg1, String arg2,
			byte[][] arg3) {
		// NOP

	}

	@Override
	public void onFileChunkReceived(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6, long arg7) {
		// NOP

	}

	@Override
	public void onFileChunkSent(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6, long arg7,
			long arg8) {
		// NOP

	}

	@Override
	public void onFileFailed(String arg0, String arg1, String arg2,
			String arg3, String arg4, int arg5) {
		// NOP

	}

	@Override
	public void onFileReceived(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6, String arg7) {
		// NOP

	}

	@Override
	public void onFileSent(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5) {
		// NOP

	}

	@Override
	public void onFileWillReceive(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6) {
		// NOP

	}

}
