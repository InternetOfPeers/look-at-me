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
	public void onBasicProfileNodeReceived(Node node) {
		Services.currentState.putSocialNodeInMap(node);
		Services.currentState.getConversationsStore().updateConversationsByNode(node);
		Services.event.post(new Event(EventType.BASIC_PROFILE_RECEIVED, node.getId()));
		if (Services.currentState.checkInterestMatch((BasicProfile) node.getProfile())) {
			Services.notification.perfectMatch(Services.currentState.getContext(), Services.currentState.getNickname(node.getId()));
			// questo non serve a nulla e di conseguenza neanche
			// l'EventType.INTEREST_MATCH
			// Services.event.post(new Event(EventType.INTEREST_MATCH,
			// node.getId()));
		}
	}

	@Override
	public void onFullProfileNodeReceived(Node node) {
		Services.currentState.setProfileViewed(node);
		Services.event.post(new Event(EventType.FULL_PROFILE_RECEIVED, node.getId()));
	}

	@Override
	public void onNodeJoined(String nodeId) {
		// Mando a tutti una richiesta di aggiornamento del profile
		Services.businessLogic.refreshSocialList();
		Services.event.post(new Event(EventType.NODE_JOINED, nodeId));
	}

	@Override
	public void onNodeLeft(String nodeId) {
		Services.currentState.removeSocialNodeFromMap(nodeId);
		Services.event.post(new Event(EventType.NODE_LEFT, nodeId));
	}

	@Override
	public void onLikeReceived(String fromNodeId) {
		String profileId = Services.currentState.getSocialNodeMap().getProfileIdByNodeId(fromNodeId);
		Services.currentState.addLikedToSet(profileId);
		if (Services.currentState.checkLikeMatch(profileId)) {
			Services.notification.perfectMatch(Services.currentState.getContext(), Services.currentState.getNickname(fromNodeId));
			Services.event.post(new Event(EventType.LIKE_RECEIVED_AND_MATCH, fromNodeId));
		} else {
			Services.notification.like(Services.currentState.getContext(), Services.currentState.getNickname(fromNodeId), fromNodeId);
			Services.event.post(new Event(EventType.LIKE_RECEIVED, fromNodeId));
		}
	}

	@Override
	public void onChatMessageReceived(String fromNodeId, String message) {
		Node node = Services.currentState.getSocialNodeMap().findNodeByNodeId(fromNodeId);
		BasicProfile otherProfile = (BasicProfile) node.getProfile();
		String otherNickName = otherProfile.getNickname();
		String otherProfileId = otherProfile.getId();
		BasicProfile myProfile = Services.currentState.getMyBasicProfile();
		String conversationId = CommonUtils.getConversationId(myProfile.getId(), otherProfileId);
		ChatConversation conversation = Services.currentState.getConversationsStore().get(conversationId);
		if (conversation == null || conversation.isEmpty())
			conversation = new ChatConversationImpl(conversationId, otherProfile);
		ChatMessage chatMessage = new ChatMessage(message, false);
		conversation.addMessage(chatMessage);
		Services.businessLogic.storeConversation(conversation);
		Services.notification.chatMessage(Services.currentState.getContext(), otherNickName, fromNodeId, message, conversationId);
		Services.event.post(new Event(EventType.CHAT_MESSAGE_RECEIVED, fromNodeId));
	}

	@Override
	public void onFullProfileRequestReceived(String nodeId) {
		Services.currentState.addVisitSet(Services.currentState.getSocialNodeMap().getProfileIdByNodeId(nodeId));
		Services.event.post(new Event(EventType.VISIT_RECEIVED, nodeId));
	}

}
