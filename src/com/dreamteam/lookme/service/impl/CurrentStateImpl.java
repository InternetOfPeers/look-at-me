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

	private static CurrentState instance;

	private Map<String, List<MessageItem>> messagesHistoryMap = new HashMap<String, List<MessageItem>>();

	private Map<String, Node> socialNodeMap = new HashMap<String, Node>();

	private Set<String> iLikeSet = new TreeSet<String>();

	private Node profileViewed;

	private Set<String> likedSet = new TreeSet<String>();

	private Context currentContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getMyFullProfile()
	 */
	@Override
	public FullProfile getMyFullProfile() {
		return myFullProfile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#setMyFullProfile(com.dreamteam
	 * .lookme.bean.FullProfile)
	 */
	@Override
	public void setMyFullProfile(FullProfile profile) {
		this.myFullProfile = profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getMyBasicProfile()
	 */
	@Override
	public BasicProfile getMyBasicProfile() {
		return myBasicProfile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#setMyBasicProfile(com.dreamteam
	 * .lookme.bean.BasicProfile)
	 */
	@Override
	public void setMyBasicProfile(BasicProfile profile) {
		this.myBasicProfile = profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getMessagesHistoryMap()
	 */
	@Override
	public Map<String, List<MessageItem>> getMessagesHistoryMap() {
		return messagesHistoryMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#putMessageInHistoryMap(java
	 * .lang.String, com.dreamteam.lookme.bean.MessageItem)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getSocialNodeMap()
	 */
	@Override
	public Map<String, Node> getSocialNodeMap() {
		return socialNodeMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#putSocialNodeInMap(com.dreamteam
	 * .lookme.chord.Node)
	 */
	@Override
	public void putSocialNodeInMap(Node node) {
		socialNodeMap.put(node.getId(), node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#removeSocialNodeFromMap(java
	 * .lang.String)
	 */
	@Override
	public void removeSocialNodeFromMap(String nodeName) {
		socialNodeMap.remove(nodeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getILikeSet()
	 */
	@Override
	public Set<String> getILikeSet() {
		return iLikeSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#addILikeToSet(java.lang.String)
	 */
	@Override
	public void addILikeToSet(String nodeName) {
		iLikeSet.add(nodeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#removeILikeFromSet(java.lang
	 * .String)
	 */
	@Override
	public void removeILikeFromSet(String nodeName) {
		iLikeSet.remove(nodeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getLikedSet()
	 */
	@Override
	public Set<String> getLikedSet() {
		return likedSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#addLikedToSet(java.lang.String)
	 */
	@Override
	public void addLikedToSet(String nodeName) {
		likedSet.add(nodeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#removeLikedFromSet(java.lang
	 * .String)
	 */
	@Override
	public void removeLikedFromSet(String nodeName) {
		likedSet.remove(nodeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.service.CurrentState#getProfileViewed()
	 */
	@Override
	public Node getProfileViewed() {
		return profileViewed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.service.CurrentState#setProfileViewed(com.dreamteam
	 * .lookme.chord.Node)
	 */
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
}
