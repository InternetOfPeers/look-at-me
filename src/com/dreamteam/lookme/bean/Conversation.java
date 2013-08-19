package com.dreamteam.lookme.bean;

import java.util.Date;
import java.util.List;

public class Conversation {

	private long id;

	private Date conversationDate;
	private BasicProfile interlocutor;
	private List<ChatMessage> chatList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getConversationDate() {
		return conversationDate;
	}

	public void setConversationDate(Date conversationDate) {
		this.conversationDate = conversationDate;
	}

	public BasicProfile getInterlocutor() {
		return interlocutor;
	}

	public void setInterlocutor(BasicProfile interlocutor) {
		this.interlocutor = interlocutor;
	}

	public List<ChatMessage> getChatList() {
		return chatList;
	}

	public void setChatList(List<ChatMessage> chatList) {
		this.chatList = chatList;
	}

}
