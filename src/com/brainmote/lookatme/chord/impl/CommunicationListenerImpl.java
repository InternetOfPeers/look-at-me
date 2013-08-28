package com.brainmote.lookatme.chord.impl;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.ChatConversationImpl;
import com.brainmote.lookatme.bean.ChatMessage;
import com.brainmote.lookatme.chord.CommunicationListener;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.EventType;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;

public class CommunicationListenerImpl implements CommunicationListener {

	@Override
	public void onCommunicationStarted() {
	}

	@Override
	public void onCommunicationStopped() {
	}

	@Override
	public void onBasicProfileNodeReceived(Node node) {
		Services.currentState.putSocialNodeInMap(node);
		Services.event.post(new Event(EventType.NODE_JOINED, node.getId()));
	}

	@Override
	public void onFullProfileNodeReceived(Node node) {
		Services.currentState.setProfileViewed(node);
		Services.event.post(new Event(EventType.PROFILE_RECEIVED, node.getId()));
	}

	@Override
	public void onNodeLeft(String nodeId) {
		Services.currentState.removeSocialNodeFromMap(nodeId);
		Services.event.post(new Event(EventType.NODE_LEFT, nodeId));
	}

	@Override
	public void onLikeReceived(String fromNodeId) {
		Services.currentState.addLikedToSet(fromNodeId);
		Services.event.post(new Event(EventType.LIKE_RECEIVED, fromNodeId));
		if (Services.currentState.checkLikeMatch(fromNodeId)) {
			Services.event.post(new Event(EventType.LIKE_MATCH, Services.currentState.getSocialNodeMap().findNodeByNodeId(fromNodeId).getProfile().getNickname()));
			Services.notification.perfectMatch(Services.currentState.getContext(), Services.currentState.getNickname(fromNodeId));
		}
		Services.notification.like(Services.currentState.getContext(), Services.currentState.getNickname(fromNodeId), fromNodeId);
	}

	@Override
	public void onChatMessageReceived(String fromNodeId, String message) {
		Node node = Services.currentState.getSocialNodeMap().findNodeByNodeId(fromNodeId);
		BasicProfile otherProfile = (BasicProfile) node.getProfile();
		String otherNickName = otherProfile.getNickname();
		int otherAge = otherProfile.getAge();
		String otherProfileId = otherProfile.getId();
		BasicProfile myProfile = Services.currentState.getMyBasicProfile();
		String conversationId = CommonUtils.getConversationId(myProfile.getId(), otherProfileId);
		ChatConversation conversation = Services.currentState.getConversationsStore().get(conversationId);
		if (conversation == null || conversation.isEmpty())
			conversation = new ChatConversationImpl(conversationId, otherNickName, otherAge, otherProfile.getMainProfileImage().getImageBitmap());
		ChatMessage chatMessage = new ChatMessage(message, false);
		conversation.addMessage(chatMessage);
		Services.businessLogic.storeConversation(conversation);
		Services.event.post(new Event(EventType.CHAT_MESSAGE_RECEIVED, otherNickName));
		Services.notification.chatMessage(Services.currentState.getContext(), otherNickName, fromNodeId, message, conversationId);
	}
}
