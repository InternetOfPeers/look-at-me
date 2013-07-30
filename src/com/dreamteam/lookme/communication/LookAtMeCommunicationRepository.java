package com.dreamteam.lookme.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.dreamteam.lookme.bean.MessageItem;

public class LookAtMeCommunicationRepository {

	// private static ILookAtMeCommunicationListener listener = null;

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

	// public static ILookAtMeCommunicationListener getInstance()
	// {
	// if(listener == null)
	// {
	// listener = new ILookAtMeCommunicationListener() {
	//
	// @Override
	// public void onSocialNodeUpdated(LookAtMeNode node) {
	// Log.d();
	// // TODO Auto-generated method stub
	// socialNodeMap.remove(node.getId());
	// socialNodeMap.put(node.getId(),node);
	//
	// }
	//
	// @Override
	// public void onSocialNodeProfileReceived(LookAtMeNode node) {
	// Log.d();
	// profileViewed=node;
	// onSocialNodeProfileProduced(node);
	// }
	//
	// @Produce
	// public SocialNodeProfileReceivedEvent
	// onSocialNodeProfileProduced(LookAtMeNode node) {
	// Log.d();
	// return new SocialNodeProfileReceivedEvent(node);
	// }
	//
	// @Override
	// public void onSocialNodeLeft(String nodeName) {
	// Log.d();
	// // remove node from socialNodeMap
	// socialNodeMap.remove(nodeName);
	//
	// }
	//
	// @Override
	// public void onSocialNodeJoined(LookAtMeNode node) {
	// Log.d();
	// socialNodeMap.put(node.getId(),node);
	//
	// }
	//
	// @Override
	// public void onLikeReceived(String nodeFrom) {
	// Log.d();
	// liked.add(nodeFrom);
	// }
	//
	// @Override
	// public void onCommunicationStopped() {
	// Log.d();
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCommunicationStarted() {
	// Log.d();
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onChatMessageReceived(LookAtMeNode nodeFrom, String message)
	// {
	// Log.d();
	// String
	// channelName=CommonUtils.generateChannelName(nodeFrom.getProfile().getId(),
	// CommonActivity.myProfile.getId());
	// MessageItem messageItem= new MessageItem(nodeFrom, message, false);
	// messagesHistoryMap.get(channelName).add(messageItem);
	// }
	//
	// @Override
	// public void onChatMessageReceived(String nodeFrom, String message) {
	// Log.d();
	// String channelName=CommonUtils.generateChannelName(nodeFrom,
	// CommonActivity.myProfile.getId());
	// MessageItem messageItem= new MessageItem(socialNodeMap.get(nodeFrom),
	// message, false);
	// messagesHistoryMap.get(channelName).add(messageItem);
	// }
	// };
	// }
	// return listener;
	// }

}
