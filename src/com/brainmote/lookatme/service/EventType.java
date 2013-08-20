package com.brainmote.lookatme.service;

public enum EventType {

	NODE_JOINED, NODE_LEFT, PROFILE_RECEIVED, CHAT_MESSAGE_RECEIVED, LIKE_RECEIVED, LIKE_MATCH;

	@Override
	public String toString() {
		return name();
	}

}
