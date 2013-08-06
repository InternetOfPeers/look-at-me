package com.dreamteam.lookme.bean;

import java.util.Date;

public class ChatMessage {

	private String from;
	private String to;
	private String text;
	private Date timestamp;

	public ChatMessage(String from, String to, String text) {
		this.from = from;
		this.to = to;
		this.text = text;
		this.timestamp = new Date(System.currentTimeMillis());
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getText() {
		return text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

}