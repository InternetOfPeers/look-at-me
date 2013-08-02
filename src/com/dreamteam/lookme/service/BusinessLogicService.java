package com.dreamteam.lookme.service;

import android.content.Context;

public interface BusinessLogicService {

	void start(Context context);

	void stop(Context context);

	boolean isRunning();

	void sendLike(String nodeId);

	void sendFullProfileRequest(String nodeId);

	boolean sendStartChatMessage(String nodeId);

	void refreshSocialList();

	boolean sendChatMessage(String nodeTo, String message);
	
	void sendMyNewBasicProfileAll();

}
