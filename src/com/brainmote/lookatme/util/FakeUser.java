package com.brainmote.lookatme.util;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.chord.Node;

public interface FakeUser {

	Node getNode();

	ChatConversation getConversation(String myProfileId);

}