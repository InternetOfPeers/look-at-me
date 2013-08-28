package com.brainmote.lookatme.bean;

import java.util.Date;

public class ChatMessage {

	private String text;
	private Date timestamp;
	private boolean isMine;

	public ChatMessage(String text, boolean isMine) {
		this.text = text;
		this.timestamp = new Date(System.currentTimeMillis());
		this.isMine = isMine;
	}

	public String getText() {
		return text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public boolean isMine() {
		return isMine;
	}

}