package com.dreamteam.lookme.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.MessageItem;

public class LookAtMeCommunicationRepository {

	private static FullProfile myFullProfile;

	private static BasicProfile myBasicProfile;

	private static LookAtMeCommunicationRepository instance;

	private static Map<String, List<MessageItem>> messagesHistoryMap = new HashMap<String, List<MessageItem>>();

	private static Map<String, LookAtMeNode> socialNodeMap = new HashMap<String, LookAtMeNode>();

	private static Set<String> iLikeSet = new TreeSet<String>();

	private static LookAtMeNode profileViewed;

	private static Set<String> likedSet = new TreeSet<String>();

	private LookAtMeCommunicationRepository() {
	}

	public static LookAtMeCommunicationRepository getInstance() {
		if (instance == null) {
			instance = new LookAtMeCommunicationRepository();
		}
		return instance;
	}

	public FullProfile getMyFullProfile() {
		return myFullProfile;
	}

	public void setMyFullProfile(FullProfile profile) {
		this.myFullProfile = profile;
	}

	public BasicProfile getMyBasicProfile() {
		return myBasicProfile;
	}

	public void setMyBasicProfile(BasicProfile profile) {
		this.myBasicProfile = profile;
	}

	public Map<String, List<MessageItem>> getMessagesHistoryMap() {
		return messagesHistoryMap;
	}

	public void putMessageInHistoryMap(String id, MessageItem messageItem) {
		if (messagesHistoryMap.containsKey(id)) {
			messagesHistoryMap.get(id).add(messageItem);
		} else {
			List<MessageItem> listMessageItem = new ArrayList<MessageItem>();
			listMessageItem.add(messageItem);
			messagesHistoryMap.put(id, listMessageItem);
		}
	}

	public Map<String, LookAtMeNode> getSocialNodeMap() {
		return socialNodeMap;
	}

	public void putSocialNodeInMap(LookAtMeNode node) {
		socialNodeMap.put(node.getId(), node);
	}

	public void removeSocialNodeFromMap(String nodeName) {
		socialNodeMap.remove(nodeName);
	}

	public Set<String> getILikeSet() {
		return iLikeSet;
	}

	public void addILikeToSet(String nodeName) {
		iLikeSet.add(nodeName);
	}

	public void removeILikeFromSet(String nodeName) {
		iLikeSet.remove(nodeName);
	}

	public Set<String> getLikedSet() {
		return likedSet;
	}

	public void addLikedToSet(String nodeName) {
		likedSet.add(nodeName);
	}

	public void removeLikedFromSet(String nodeName) {
		likedSet.remove(nodeName);
	}

	public LookAtMeNode getProfileViewed() {
		return profileViewed;
	}

	public void setProfileViewed(LookAtMeNode node) {
		profileViewed = node;
	}

}
