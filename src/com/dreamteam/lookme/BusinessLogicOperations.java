package com.dreamteam.lookme;

import com.dreamteam.lookme.communication.LookAtMeNode;

import android.content.ContextWrapper;

public interface BusinessLogicOperations {

	public void start(ContextWrapper context);

	public void stop(ContextWrapper context);

	public boolean isRunning();
	
	public boolean joinChannel(String channelName);
	
	public boolean sendChatMessage(LookAtMeNode toNode);
	
	//public boolean sendFile(LookAtMeNode toNode);
}
