package com.dreamteam.lookme.event;

import com.dreamteam.lookme.communication.LookAtMeNode;

public class SocialNodeProfileReceivedEvent {
	
	LookAtMeNode node;
	
	public SocialNodeProfileReceivedEvent(LookAtMeNode node) {
		this.node=node;
	}

}
