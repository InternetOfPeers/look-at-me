package com.dreamteam.lookme.event;

import com.dreamteam.lookme.bean.MessageItem;

public class MessageReceivedEvent {
	
	MessageItem message;
	
	public MessageReceivedEvent(MessageItem message) {
		this.message=message;
	}

}
