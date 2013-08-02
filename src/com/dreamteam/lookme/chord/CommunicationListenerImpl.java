package com.dreamteam.lookme.chord;

import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.EventType;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;

public class CommunicationListenerImpl implements CommunicationListener {

	@Override
	public void onCommunicationStarted() {
		// TODO Auto-generated method stub
		Log.d();

	}

	@Override
	public void onCommunicationStopped() {
		// TODO Auto-generated method stub
		Log.d();

	}

	@Override
	public void onBasicProfileNodeReceived(Node node) {
		Log.d();
		Services.currentState.putSocialNodeInMap(node);
		try {
			Services.eventBus.post(new Event(EventType.NODE_JOINED, node.getId()));
		} catch (Exception e) {
			Log.e(e.getMessage());
		}
	}

	@Override
	public void onFullProfileNodeReceived(Node node) {
		Log.d();
		Services.currentState.setProfileViewed(node);
		try {
			Services.eventBus.post(new Event(EventType.PROFILE_RECEIVED, node.getId()));
		} catch (Exception e) {
			Log.e(e.getMessage());
		}
	}

	@Override
	public void onNodeLeft(String nodeName) {
		Log.d();
		Services.currentState.removeSocialNodeFromMap(nodeName);
		try {
			Services.eventBus.post(new Event(EventType.NODE_LEFT, nodeName));
		} catch (Exception e) {
			Log.e(e.getMessage());
		}
	}

	@Override
	public void onProfileNodeUpdated(Node node) {
		// TODO Auto-generated method stub
		Log.d();

	}

	@Override
	public void onLikeReceived(String fromNode) {
		Log.d();
		Services.currentState.addLikedToSet(fromNode);
		try {
			Services.eventBus.post(new Event(EventType.LIKE_RECEIVED, fromNode));
		} catch (Exception e) {
			Log.e(e.getMessage());
		}
		Services.notify.like(Services.currentState.getContext(), Services.currentState.getNickname(fromNode), fromNode);
	}

	@Override
	public void onStartChatMessageReceived(String nodeFrom, String channelName) {
		// TODO Auto-generated method stub
		Log.d("Silently join to private channel");
	}

	@Override
	public void onChatMessageReceived(String nodeFrom, String message) {
		Log.d("node " + nodeFrom + " says: " + message);
		// creo il MessageItem
		// metto il messaggio nella map
		try {
			Services.eventBus.post(new Event(EventType.CHAT_MESSAGE_RECEIVED, nodeFrom));
		} catch (Exception e) {
			Log.e(e.getMessage());
		}
		String nickName = Services.currentState.getSocialNodeMap().get(nodeFrom).getProfile().getNickname();
		Services.notify.chatMessage(Services.currentState.getContext(), nickName, message);
	}
}
