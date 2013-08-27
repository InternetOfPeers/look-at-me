package com.brainmote.lookatme.service;

import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.chord.Node;

public interface CurrentState {

	FullProfile getMyFullProfile();

	BasicProfile getMyBasicProfile();

	Map<String, ChatConversation> getConversationsStore();

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

	void reset();

}