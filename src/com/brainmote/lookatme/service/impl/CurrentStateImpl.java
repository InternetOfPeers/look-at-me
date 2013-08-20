package com.brainmote.lookatme.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.service.CurrentState;

public class CurrentStateImpl implements CurrentState {

	private Map<String, ChatConversation> conversationsStore = new HashMap<String, ChatConversation>();

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
		// TODO getcontext() potrebbe tornare null (dopo un lungo inutilizzo?),
		// gestire la situazione di conseguenza
		return DBOpenHelperImpl.getInstance(getContext()).getMyBasicProfile();
	}

	@Override
	public Map<String, ChatConversation> getConversationsStore() {
		return conversationsStore;
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
