package com.dreamteam.lookme.event;

public enum LookAtMeEventType {

	NODE_JOINED, NODE_LEFT, PROFILE_RECEIVED, CHAT_MESSAGE_RECEIVED, LIKE_RECEIVED;

	@Override
	public String toString() {
		return name();
	}

}
