package com.dreamteam.lookme.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ChatMessage;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Interest;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.service.CurrentState;
import com.dreamteam.util.Log;

public class CurrentStateImpl implements CurrentState {

	private Map<String, List<ChatMessage>> messagesHistoryMap = new HashMap<String, List<ChatMessage>>();

	private Map<String, Node> socialNodeMap = new HashMap<String, Node>();

	private Set<String> iLikeSet = new TreeSet<String>();

	private Node profileViewed;

	private Set<String> likedSet = new TreeSet<String>();

	private Context currentContext;

	private Set<Interest> interestList = new TreeSet<Interest>();

	@Override
	public FullProfile getMyFullProfile() {
		return DBOpenHelperImpl.getInstance(getContext()).getMyFullProfile();
	}

	@Override
	public BasicProfile getMyBasicProfile() {
		return DBOpenHelperImpl.getInstance(getContext()).getMyBasicProfile();
	}

	@Override
	public Map<String, List<ChatMessage>> getConversationsStore() {
		return messagesHistoryMap;
	}

	@Override
	public void storeChatMessage(String id, ChatMessage messageItem) {
		if (messagesHistoryMap.containsKey(id)) {
			messagesHistoryMap.get(id).add(messageItem);
		} else {
			List<ChatMessage> listMessageItem = new ArrayList<ChatMessage>();
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

	@Override
	public boolean checkLikeMatch(String nodeId) {
		return getILikeSet().contains(nodeId) && getLikedSet().contains(nodeId);
	}

	public Set<Interest> getInterestList() {
		return interestList;
	}

	public void setInterestList(Set<Interest> interestList) {
		this.interestList = interestList;
	}

}
