package com.brainmote.lookatme.bean;

import java.util.Date;

public class ChatMessage {

	private String fromNickname;
	private String toNickname;
	private String text;
	private Date timestamp;
	private boolean isMine;

	public ChatMessage(String fromNickname, String toNickname, String text, boolean isMine) {
		this.fromNickname = fromNickname;
		this.toNickname = toNickname;
		this.text = text;
		this.timestamp = new Date(System.currentTimeMillis());
		this.isMine = isMine;
	}

	public String getFromNickname() {
		return fromNickname;
	}

	public String getToNickname() {
		return toNickname;
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