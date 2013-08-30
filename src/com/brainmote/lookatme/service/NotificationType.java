package com.brainmote.lookatme.service;

public enum NotificationType {
	CHAT_MESSAGE_RECEIVED(0), YOUR_PROFILE_WAS_VISITED(1), SOMEONE_LIKED_YOU(2), PERFECT_MATCH(3);
	int value;

	NotificationType(int value) {
		this.value = value;
	}

	public int getInt() {
		return value;
	}

}