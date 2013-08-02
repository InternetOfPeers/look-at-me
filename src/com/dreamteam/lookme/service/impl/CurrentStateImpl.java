package com.dreamteam.lookme.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.CurrentState;

public class CurrentStateImpl implements CurrentState {

	private FullProfile myFullProfile;

	private BasicProfile myBasicProfile;

	private Map<String, List<MessageItem>> messagesHistoryMap = new HashMap<String, List<MessageItem>>();

	private Map<String, Node> socialNodeMap = new HashMap<String, Node>();

	private Set<String> iLikeSet = new TreeSet<String>();

	private Node profileViewed;

	private Set<String> likedSet = new TreeSet<String>();

	private Context currentContext;

	@Override
	public FullProfile getMyFullProfile() {
		return myFullProfile;
	}

	@Override
	public void setMyFullProfile(FullProfile profile) {
		this.myFullProfile = profile;
	}

	@Override
	public BasicProfile getMyBasicProfile() {
		return myBasicProfile;
	}

	@Override
	public void setMyBasicProfile(BasicProfile profile) {
		this.myBasicProfile = profile;
	}

	@Override
	public Map<String, List<MessageItem>> getMessagesHistoryMap() {
		return messagesHistoryMap;
	}

	@Override
	public void putMessageInHistoryMap(String id, MessageItem messageItem) {
		if (messagesHistoryMap.containsKey(id)) {
			messagesHistoryMap.get(id).add(messageItem);
		} else {
			List<MessageItem> listMessageItem = new ArrayList<MessageItem>();
			listMessageItem.add(messageItem);
			messagesHistoryMap.put(id, listMessageItem);
		}
	}

	@Override
	public Map<String, Node> getSocialNodeMap() {
		return socialNodeMap;
	}

	@Override
	public void putSocialNodeInMap(Node node) {
		socialNodeMap.put(node.getId(), node);
	}

	@Override
	public void removeSocialNodeFromMap(String nodeName) {
		socialNodeMap.remove(nodeName);
	}

	@Override
	public Set<String> getILikeSet() {
		return iLikeSet;
	}

	@Override
	public void addILikeToSet(String nodeName) {
		iLikeSet.add(nodeName);
	}

	@Override
	public void removeILikeFromSet(String nodeName) {
		iLikeSet.remove(nodeName);
	}

	@Override
	public Set<String> getLikedSet() {
		return likedSet;
	}

	@Override
	public void addLikedToSet(String nodeName) {
		likedSet.add(nodeName);
	}

	@Override
	public void removeLikedFromSet(String nodeName) {
		likedSet.remove(nodeName);
	}

	@Override
	public Node getProfileViewed() {
		return profileViewed;
	}

	@Override
	public void setProfileViewed(Node node) {
		profileViewed = node;
	}

	private CurrentStateImpl() {

	}

	public static class Factory {
		private static CurrentState instance;

		public static CurrentState getCurrentState() {
			return instance == null ? instance = new CurrentStateImpl() : instance;
		}
	}

	@Override
	public void setContext(Context context) {
		currentContext = context;
	}

	@Override
	public Context getContext() {
		return currentContext;
	}

	@Override
	public String getNickname(String nodeId) {
		return getSocialNodeMap().get(nodeId).getProfile().getNickname();
	}
}
