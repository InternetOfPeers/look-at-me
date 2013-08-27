package com.brainmote.lookatme.service;

import java.util.Set;

import android.content.Context;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.util.FakeUser;

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

	void storeConversation(ChatConversation conversation);

	FakeUser getFakeUser();

	boolean isFakeUserNode(String nodeId);

	boolean isMyProfileComplete();

	boolean isNodeConnected(String nodeId);

	boolean isConversationAlive(ChatConversation conversation);

	boolean isAppInForeground();

}
