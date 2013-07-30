package com.dreamteam.lookme;

import android.content.ContextWrapper;

import com.dreamteam.lookme.communication.LookAtMeNode;

public interface BusinessLogicOperations {

	public void start(ContextWrapper context);

	public void stop(ContextWrapper context);

	public boolean isRunning();

	public boolean joinChannel(String channelName);

	public boolean sendChatMessage(LookAtMeNode toNode);

	// public boolean sendFile(LookAtMeNode toNode);
}
