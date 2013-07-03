/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord.channel;

import java.util.List;

import android.util.Log;

import com.dreamteam.lookme.chord.message.ChordMessage;
import com.samsung.chord.IChordChannel;

public class LookAtMeChannel implements IChordChannel {

	private final IChordChannel mChordChannel;
	private final boolean mIsPrivate;
	
	public LookAtMeChannel(IChordChannel channel, boolean isPrivate) {
		mChordChannel = channel;
		mIsPrivate = isPrivate;
	}
	
	@Override
	public String getName() {
		return mChordChannel.getName();
	}

	@Override
	public boolean isName(String channelName) {
		return mChordChannel.isName(channelName);
	}

	@Override
	public List<String> getJoinedNodeList() {
		return mChordChannel.getJoinedNodeList();
	}

	@Override
	public boolean sendData(String toNode, String payloadType, byte[][] payload) {
		Log.d("LookAtMe", "sendData[" + getType() + getName() + "][" + toNode + "] "
				+ getMessageDescription(payload));
		return mChordChannel.sendData(toNode, payloadType, payload);
	}

	@Override
	public boolean sendDataToAll(String payloadType, byte[][] payload) {
		Log.d("LookAtMe", "sendDataToAll[" + getType() + getName() + "][ALL] " + getMessageDescription(payload));
		return mChordChannel.sendDataToAll(payloadType, payload);
	}

	@Override
	public String sendFile(String toNode, String fileType, String filePath, long timeoutMsc) {
		Log.d("LookAtMe", "sendFile[" + getName() + "][" + toNode + "]");
		return mChordChannel.sendFile(toNode, fileType, filePath, timeoutMsc);
	}

	@Override
	public boolean acceptFile(String exchangeId, long chunkTimeoutMsc, int chunkRetries, long chunkSize) {
		return mChordChannel.acceptFile(exchangeId, chunkTimeoutMsc, chunkRetries, chunkSize);
	}

	@Override
	public boolean rejectFile(String exchangeId) {
		return mChordChannel.rejectFile(exchangeId);
	}

	@Override
	public boolean cancelFile(String exchangeId) {
		return mChordChannel.cancelFile(exchangeId);
	}

	@Override
	public String getNodeIpAddress(String nodeName) {
		return mChordChannel.getNodeIpAddress(nodeName);
	}

	private String getMessageDescription(byte[][] payload) {
		return ChordMessage.obtainChordMessage(payload[0], null).toString();
	}

	private String getType() {
		return mIsPrivate ? "(PRIVATE)" : "(PUBLIC)";
	}
}
