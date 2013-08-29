package com.brainmote.lookatme.service;

public enum EventType {

	NODE_JOINED, NODE_LEFT, BASIC_PROFILE_RECEIVED, FULL_PROFILE_RECEIVED, CHAT_MESSAGE_RECEIVED, LIKE_RECEIVED, LIKE_RECEIVED_AND_MATCH;

	@Override
	public String toString() {
		return name();
	}

}
