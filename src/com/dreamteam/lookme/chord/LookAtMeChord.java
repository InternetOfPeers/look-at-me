/**
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

import com.dreamteam.lookme.chord.channel.LookAtMeChannel;
import com.dreamteam.lookme.chord.listener.LookAtMeChordGameChannelListener;
import com.dreamteam.lookme.chord.listener.LookAtMeChordManagerListener;
import com.dreamteam.lookme.chord.listener.LookAtMeChordSocialChannelListener;
import com.dreamteam.lookme.chord.listener.LookAtMePublicChannelListener;
import com.dreamteam.lookme.chord.message.ChordMessage;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class LookAtMeChord {

	private final ChordManager mChordManager;
	
	private IChordChannel mPublicChannel;
	private IChordChannel mSocialPrivateChannel;
	private IChordChannel mGamePrivateChannel;
	
	private static final String SOCIAL_CHANNEL_NAME = "LOOKATME_SOCIAL_CHANNEL";
	private static final String GAME_CHANNEL_NAME = "LOOKATME_GAME_CHANNEL";
	
	private final IChordManagerListener mChordManagerListener = new LookAtMeChordManagerListener();
	private final IChordChannelListener mChordPublicChannelListener = new LookAtMePublicChannelListener();
	private final IChordChannelListener mChordSocialChannelListener = new LookAtMeChordSocialChannelListener();
	private final IChordChannelListener mChordGameChannelListener = new LookAtMeChordGameChannelListener();
	
	LookAtMeChord(Context context, String gameName) {
		mChordManager = ChordManager.getInstance(context);
		int result = mChordManager.start(ChordManager.INTERFACE_TYPE_WIFI, mChordManagerListener);
		if (result != ChordManager.ERROR_NONE) {
			//onChordStartFailed(result);
		}
	}
	
	public void stopChord() {
		if (mPublicChannel != null) {
			mChordManager.leaveChannel(mPublicChannel.getName());
		}
		if (mSocialPrivateChannel != null) {
			mChordManager.leaveChannel(mSocialPrivateChannel.getName());
		}
		if (mGamePrivateChannel != null) {
			mChordManager.leaveChannel(mGamePrivateChannel.getName());
		}
		mChordManager.stop();
	}

	/**
	 * Joins to the public channel (ChordManager.PUBLIC_CHANNEL).
	 */
	void joinPublicChannel() {
		mPublicChannel = new LookAtMeChannel(
				mChordManager.joinChannel(ChordManager.PUBLIC_CHANNEL,mChordPublicChannelListener), false);
		//onJoinedToPublicChannel(mPublicChannel);
	}
	
	/**
	 * Joins to the social channel
	 */
	public void joinSocialPrivateChannel() {
		mSocialPrivateChannel = new LookAtMeChannel(
				mChordManager.joinChannel(SOCIAL_CHANNEL_NAME, mChordSocialChannelListener), true);
		//onJoinedToPrivateChannel(mSocialPrivateChannel);
	}
	
	/**
	 * Joins to the game channel
	 */
	public void joinGamePrivateChannel() {
		mGamePrivateChannel = new LookAtMeChannel(
				mChordManager.joinChannel(GAME_CHANNEL_NAME, mChordGameChannelListener), true);
		//onJoinedToPrivateChannel(mPrivateChannel);
	}
	
	public String getNodeName() {
		return mChordManager.getName();
	}
	
	/**
	 * Sends message over public channel.
	 */
	public void sendPublicMessage(ChordMessage message) {
		mPublicChannel.sendDataToAll(message.getType().name(), new byte[][] { message.getBytes() });
	}

	/**
	 * Sends message over social private channel.
	 */
	public void sendSocialPrivateMessage(ChordMessage message, String toNode) {
		mSocialPrivateChannel.sendData(toNode, message.getType().name(), new byte[][] { message.getBytes() });
	}

	/**
	 * Sends message over game private channel.
	 */
	public void sendGamePrivateMessage(ChordMessage message, String toNode) {
		mGamePrivateChannel.sendData(toNode, message.getType().name(), new byte[][] { message.getBytes() });
	}

	public void handlePublicMessage(ChordMessage message) {
		//throw new UnsupportedOperationException(message.getType().name());
	}

	public void handlePrivateMessage(ChordMessage message) {
		//throw new UnsupportedOperationException(message.getType().name());
	}
}
