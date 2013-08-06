package com.dreamteam.lookme.chord.impl;

import java.util.ArrayList;

import com.dreamteam.lookme.bean.ChatConversation;
import com.dreamteam.lookme.bean.ChatMessage;
import com.dreamteam.lookme.chord.CommunicationListener;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.EventType;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;

public class CommunicationListenerImpl implements CommunicationListener {

	@Override
	public void onCommunicationStarted() {
		Log.d();
	}

	@Override
	public void onCommunicationStopped() {
		Log.d();
	}

	@Override
	public void onBasicProfileNodeReceived(Node node) {
		Log.d();
		Services.currentState.putSocialNodeInMap(node);
		try {
			Services.event.post(new Event(EventType.NODE_JOINED, node.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFullProfileNodeReceived(Node node) {
		Log.d();
		Services.currentState.setProfileViewed(node);
		try {
			Services.event.post(new Event(EventType.PROFILE_RECEIVED, node.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onNodeLeft(String nodeName) {
		Log.d();
		Services.currentState.removeSocialNodeFromMap(nodeName);
		try {
			Services.event.post(new Event(EventType.NODE_LEFT, nodeName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProfileNodeUpdated(Node node) {
		Log.d();
	}

	@Override
	public void onLikeReceived(String fromNode) {
		Log.d();
		Services.currentState.addLikedToSet(fromNode);
		try {
			Services.event.post(new Event(EventType.LIKE_RECEIVED, fromNode));
			if (Services.currentState.checkLikeMatch(fromNode)) {
				Services.event.post(new Event(EventType.LIKE_MATCH, fromNode));
				Services.notification.perfectMatch(Services.currentState.getContext(), Services.currentState.getNickname(fromNode));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Services.notification.like(Services.currentState.getContext(), Services.currentState.getNickname(fromNode), fromNode);
	}

	@Override
	public void onChatMessageReceived(String nodeFrom, String message) {
		Log.d("node " + nodeFrom + " says: " + message);
		try {
			Services.event.post(new Event(EventType.CHAT_MESSAGE_RECEIVED, nodeFrom));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String nickName = Services.currentState.getSocialNodeMap().get(nodeFrom).getProfile().getNickname();
		String nodeId = Services.currentState.getSocialNodeMap().get(nodeFrom).getId();
		String deviceId = Services.currentState.getSocialNodeMap().get(nodeFrom).getProfile().getId();
		// TODO il channelid è generato in modo diverso rispetto all'on click
		// del nearby
		String channelName = CommonUtils.generateChannelId(deviceId, Services.currentState.getMyBasicProfile().getId());
		ChatConversation messagesList = Services.currentState.getConversationsStore().get(channelName);
		if (messagesList == null || messagesList.isEmpty())
			messagesList = (ChatConversation) new ArrayList<ChatMessage>();
		ChatMessage messageItem = new ChatMessage(nodeId, deviceId, message);
		messagesList.add(messageItem);
		Services.currentState.getConversationsStore().put(channelName, messagesList);
		Services.notification.chatMessage(Services.currentState.getContext(), nickName, message);
	}
}
