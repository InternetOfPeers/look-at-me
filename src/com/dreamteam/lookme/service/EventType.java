package com.dreamteam.lookme.service;

public enum EventType {

	NODE_JOINED, NODE_LEFT, PROFILE_RECEIVED, CHAT_MESSAGE_RECEIVED, LIKE_RECEIVED;

	@Override
	public String toString() {
		return name();
	}

}
