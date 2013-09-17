package com.brainmote.lookatme.service;

import java.util.Set;

import android.content.Context;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.Statistics;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.util.SocialNodeMap;

public interface CurrentState {

	FullProfile getMyFullProfile();

	BasicProfile getMyBasicProfile();

	ConversationStore getConversationsStore();

	SocialNodeMap getSocialNodeMap();

	void putSocialNodeInMap(Node node);

	void removeSocialNodeFromMap(String nodeName);

	Set<String> getILikeSet();

	void addILikeToSet(String profileId);

	void removeILikeFromSet(String profileId);

	Set<String> getLikedSet();

	void addLikedToSet(String profileId);

	void removeLikedFromSet(String profileId);

	Node getProfileViewed();

	void setProfileViewed(Node node);

	void setContext(Context context);

	Context getContext();

	String getNickname(String nodeId);

	boolean checkLikeMatch(String profileId);

	Set<Integer> getInterestSet();

	void setInterestSet(Set<Integer> interestSet);

	void addInterestToSet(int interest);

	void removeInterestFromSet(int interest);

	void reset();

	Statistics getStatistics();

	void addVisitSet(String profileId);

}