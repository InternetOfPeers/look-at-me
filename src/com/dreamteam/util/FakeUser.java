package com.dreamteam.util;

import com.dreamteam.lookme.ChatConversation;
import com.dreamteam.lookme.chord.Node;

public interface FakeUser {

	Node getNode();

	ChatConversation getConversation(String myProfileId);

}