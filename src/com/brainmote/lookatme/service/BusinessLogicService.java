package com.brainmote.lookatme.service;

import java.util.Set;

import android.content.Context;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.util.FakeUser;

public interface BusinessLogicService {

	void start(Context context);

	void stop(Context context);

	boolean isRunning();

	void sendLike(String nodeId);

	void requestFullProfile(String nodeId);

	boolean startChat(String nodeId);

	void refreshSocialList();

	void notifyMyProfileIsUpdated();

	Set<Interest> getFullInterestList();

	void storeConversation(ChatConversation conversation);

	FakeUser getFakeUser();

	boolean isFakeUserNode(String nodeId);

	boolean isMyProfileComplete();

	boolean isNodeAlive(String nodeId);

	/**
	 * Invia un messaggio di testo all'interno del contesto della conversazione
	 * passata
	 * 
	 * @param conversation
	 *            La conversazione di riferimento a cui mandare il messaggio
	 * @param messageText
	 *            Il testo del messaggio da inviare
	 * @return Restituisce true se il messaggio è stato correttamente inviato,
	 *         false in caso contrario
	 */
	boolean sendChatMessage(ChatConversation conversation, String messageText);

}
