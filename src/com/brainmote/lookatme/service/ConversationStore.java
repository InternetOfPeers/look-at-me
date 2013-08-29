package com.brainmote.lookatme.service;

import java.util.Map;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.chord.Node;

public interface ConversationStore extends Map<String, ChatConversation> {

	void updateConversationsByNode(Node node);

}
