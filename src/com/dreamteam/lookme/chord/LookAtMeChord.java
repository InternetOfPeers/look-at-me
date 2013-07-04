/**
 * Author: Carlo Tassi
 * 
 * In Look@me App exists 1 public channel and 2 private
 * channels.
 * Public channel is not used excepted for starting communication
 * between devices.
 * First private channel is used to social local functions,
 * second private channel is used to love game functions.
 * Users can decide to join one or both private channels.
 * "Devices running Chord-based applications are added to 
 * the public channel automatically" (docs).
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
	
	public static final String SOCIAL_CHANNEL_NAME = "LOOKATME_SOCIAL_CHANNEL";
	public static final String GAME_CHANNEL_NAME = "LOOKATME_GAME_CHANNEL";

	private final ChordManager chordManager;
	
	private final IChordManagerListener chordManagerListener;
	private final IChordChannelListener chordPublicChannelListener;
	private final IChordChannelListener chordSocialChannelListener;
	private final IChordChannelListener chordGameChannelListener;
	
	private IChordChannel publicChannel;
	private IChordChannel socialChannel;
	private IChordChannel gameChannel;
	
	public LookAtMeChord(Context context) {
		chordManager = ChordManager.getInstance(context);
		chordPublicChannelListener = new LookAtMePublicChannelListener();
		chordSocialChannelListener = new LookAtMeChordSocialChannelListener();
		chordGameChannelListener = new LookAtMeChordGameChannelListener();
		chordManagerListener = new LookAtMeChordManagerListener(chordManager, chordPublicChannelListener);
		
		int result = chordManager.start(ChordManager.INTERFACE_TYPE_WIFIP2P, chordManagerListener);
		if (result != ChordManager.ERROR_NONE) {
			//onChordStartFailed(result);
		}
		publicChannel = chordManager.getJoinedChannel(ChordManager.PUBLIC_CHANNEL);
	}
	
	public void stopChord() {
		if (publicChannel != null) {
			chordManager.leaveChannel(publicChannel.getName());
		}
		if (socialChannel != null) {
			chordManager.leaveChannel(socialChannel.getName());
		}
		if (gameChannel != null) {
			chordManager.leaveChannel(gameChannel.getName());
		}
		chordManager.stop();
	}

	/**
	 * Joins to the public channel (ChordManager.PUBLIC_CHANNEL).
	 * "Devices running Chord-based applications 
	 * are added to the public channel automatically" (docs).
	 * It's redoundant???? publicChannel is retrieved with
	 * chordManager.getJoinedChannel(ChordManager.PUBLIC_CHANNEL);
	 */
	void joinPublicChannel() {
		publicChannel = new LookAtMeChannel(
				chordManager.joinChannel(ChordManager.PUBLIC_CHANNEL,chordPublicChannelListener), false);
	}
	
	/**
	 * Joins to the social channel
	 */
	public void joinSocialChannel() {
		socialChannel = new LookAtMeChannel(
				chordManager.joinChannel(SOCIAL_CHANNEL_NAME, chordSocialChannelListener), true);
		//onJoinedToPrivateChannel(mSocialPrivateChannel);
	}
	
	/**
	 * Joins to the game channel
	 */
	public void joinGameChannel() {
		gameChannel = new LookAtMeChannel(
				chordManager.joinChannel(GAME_CHANNEL_NAME, chordGameChannelListener), true);
		//onJoinedToPrivateChannel(mPrivateChannel);
	}
	
	public String getNodeName() {
		return chordManager.getName();
	}
	
	/**
	 * Sends message over public channel.
	 */
	public void sendPublicMessage(ChordMessage message) {
		publicChannel.sendDataToAll(message.getType().name(), new byte[][] { message.getBytes() });
	}

	/**
	 * Sends message over social private channel.
	 */
	public void sendSocialMessage(ChordMessage message, String toNode) {
		socialChannel.sendData(toNode, message.getType().name(), new byte[][] { message.getBytes() });
	}

	/**
	 * Sends message over game private channel.
	 */
	public void sendGameMessage(ChordMessage message, String toNode) {
		gameChannel.sendData(toNode, message.getType().name(), new byte[][] { message.getBytes() });
	}
	/*
	public void handlePublicMessage(ChordMessage message) {
		//TODO
	}

	public void handlePrivateMessage(ChordMessage message) {
		//TODO
	}
	*/
}
