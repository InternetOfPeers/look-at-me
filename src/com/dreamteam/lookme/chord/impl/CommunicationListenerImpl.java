package com.dreamteam.lookme.chord.impl;

import com.dreamteam.lookme.ChatConversation;
import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ChatConversationImpl;
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
	}

	@Override
	public void onCommunicationStopped() {
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
	public void onChatMessageReceived(String fromNode, String message) {
		Log.d("node " + fromNode + " says: " + message);
		try {
			Services.event.post(new Event(EventType.CHAT_MESSAGE_RECEIVED, fromNode));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Node node = Services.currentState.getSocialNodeMap().get(fromNode);
		String nodeId = node.getId();
		BasicProfile otherProfile = (BasicProfile) node.getProfile();
		String otherNickName = otherProfile.getNickname();
		String otherProfileId = otherProfile.getId();
		String conversationId = CommonUtils.getConversationId(Services.currentState.getMyBasicProfile().getId(), otherProfileId);
		ChatConversation conversation = Services.currentState.getConversationsStore().get(conversationId);
		if (conversation == null || conversation.isEmpty())
			conversation = new ChatConversationImpl(conversationId, otherNickName, nodeId, otherProfile.getMainProfileImage().getImageBitmap());
		ChatMessage chatMessage = new ChatMessage(nodeId, otherProfileId, message, false);
		conversation.addMessage(chatMessage);
		Services.businessLogic.storeConversation(conversation);
		Services.notification.chatMessage(Services.currentState.getContext(), otherNickName, message);
	}
}
