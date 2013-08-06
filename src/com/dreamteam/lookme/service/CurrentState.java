package com.dreamteam.lookme.service;

import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ChatConversation;
import com.dreamteam.lookme.bean.ChatMessage;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Interest;
import com.dreamteam.lookme.chord.Node;

public interface CurrentState {

	FullProfile getMyFullProfile();

	BasicProfile getMyBasicProfile();

	Map<String, ChatConversation> getConversationsStore();

	void storeChatMessage(String id, ChatMessage chatMessage);

	Map<String, Node> getSocialNodeMap();

	void putSocialNodeInMap(Node node);

	void removeSocialNodeFromMap(String nodeName);

	Set<String> getILikeSet();

	void addILikeToSet(String nodeName);

	void removeILikeFromSet(String nodeName);

	Set<String> getLikedSet();

	void addLikedToSet(String nodeName);

	void removeLikedFromSet(String nodeName);

	Node getProfileViewed();

	void setProfileViewed(Node node);

	void setContext(Context context);

	Context getContext();

	String getNickname(String nodeId);

	boolean checkLikeMatch(String nodeId);

	Set<Interest> getInterestList();

	void setInterestList(Set<Interest> interestList);

}