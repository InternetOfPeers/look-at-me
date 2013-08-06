package com.dreamteam.lookme.service;

import java.util.Set;

import android.content.Context;

import com.dreamteam.lookme.bean.Interest;

public interface BusinessLogicService {

	void start(Context context);

	void stop(Context context);

	boolean isRunning();

	void sendLike(String nodeId);

	void requestFullProfile(String nodeId);

	boolean startChat(String nodeId);

	void refreshSocialList();

	boolean sendChatMessage(String nodeTo, String message);

	void notifyMyProfileIsUpdated();

	Set<Interest> getFullInterestList();

}
