/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord.channel;

import java.util.List;

import android.util.Log;

import com.dreamteam.lookme.chord.message.ChordMessage;
import com.samsung.chord.IChordChannel;

public class LookAtMeChannel implements IChordChannel {

	private final IChordChannel chordChannel;
	private final boolean isPrivate;
	
	public LookAtMeChannel(IChordChannel channel, boolean isPrivate) {
		this.chordChannel = channel;
		this.isPrivate = isPrivate;
	}
	
	@Override
	public String getName() {
		return chordChannel.getName();
	}

	@Override
	public boolean isName(String channelName) {
		return chordChannel.isName(channelName);
	}

	@Override
	public List<String> getJoinedNodeList() {
		return chordChannel.getJoinedNodeList();
	}

	@Override
	public boolean sendData(String toNode, String payloadType, byte[][] payload) {
		Log.d("LookAtMe", "sendData[" + getType() + getName() + "][" + toNode + "] "
				+ getMessageDescription(payload));
		return chordChannel.sendData(toNode, payloadType, payload);
	}

	@Override
	public boolean sendDataToAll(String payloadType, byte[][] payload) {
		Log.d("LookAtMe", "sendDataToAll[" + getType() + getName() + "][ALL] " + getMessageDescription(payload));
		return chordChannel.sendDataToAll(payloadType, payload);
	}

	@Override
	public String sendFile(String toNode, String fileType, String filePath, long timeoutMsc) {
		Log.d("LookAtMe", "sendFile[" + getName() + "][" + toNode + "]");
		return chordChannel.sendFile(toNode, fileType, filePath, timeoutMsc);
	}

	@Override
	public boolean acceptFile(String exchangeId, long chunkTimeoutMsc, int chunkRetries, long chunkSize) {
		return chordChannel.acceptFile(exchangeId, chunkTimeoutMsc, chunkRetries, chunkSize);
	}

	@Override
	public boolean rejectFile(String exchangeId) {
		return chordChannel.rejectFile(exchangeId);
	}

	@Override
	public boolean cancelFile(String exchangeId) {
		return chordChannel.cancelFile(exchangeId);
	}

	@Override
	public String getNodeIpAddress(String nodeName) {
		return chordChannel.getNodeIpAddress(nodeName);
	}

	private String getMessageDescription(byte[][] payload) {
		return ChordMessage.obtainChordMessage(payload[0], null).toString();
	}

	private String getType() {
		return isPrivate ? "(PRIVATE)" : "(PUBLIC)";
	}
}
