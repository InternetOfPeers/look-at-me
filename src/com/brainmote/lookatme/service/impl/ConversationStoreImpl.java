package com.brainmote.lookatme.service.impl;

import java.util.HashMap;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.service.ConversationStore;

public class ConversationStoreImpl extends HashMap<String, ChatConversation> implements ConversationStore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7391424099425083992L;

	@Override
	public void updateConversationsByNode(Node node) {
		// Verifico se il profileId presente nel nodo passato è presente nello
		// store delle conversazioni e se sì aggiorno quella conversazione con i
		// dati presenti nell'oggetto nodo
		for (String conversationId : keySet()) {
			if (conversationId.contains(node.getProfile().getId())) {
				get(conversationId).setBasicProfile((BasicProfile) node.getProfile());
			}
		}
	}

}
