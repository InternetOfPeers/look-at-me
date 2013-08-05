package com.dreamteam.lookme.service;

import java.util.Set;

import android.content.Context;

import com.dreamteam.lookme.bean.Interest;

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

	Set<Interest> getFullInterestList();

}
