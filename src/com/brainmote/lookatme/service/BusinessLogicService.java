package com.brainmote.lookatme.service;

import java.util.Set;

import android.content.Context;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.fake.FakeUser;

public interface BusinessLogicService {

	void start(Context context);

	void stop(Context context);

	boolean isRunning();

	void sendLike(String nodeId);

	void requestFullProfile(String nodeId);

	void startChat(String nodeId);

	void refreshSocialList();

	void notifyMyProfileIsUpdated();

	Set<Interest> getFullInterestList();

	void storeConversation(ChatConversation conversation);

	FakeUser getFakeUser(String nodeId);

	boolean isFakeUserNode(String nodeId);

	boolean isMyProfileComplete();

	boolean isNodeAlive(String nodeId);

	String getProfileIdFromConversationId(String conversationId);

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

	/**
	 * Effettua tutte le operazioni del caso per far entrare l'utente nella
	 * conversazione corrispondente
	 * 
	 * @param conversation
	 *            La conversazione alla quale l'utente vuole partecipare
	 */
	void joinConversation(ChatConversation conversation);

	/**
	 * Verifica se l'utente ha abilitato l'opzione di credits in app (invoke
	 * developers)
	 * 
	 * @param context
	 * @return
	 */
	boolean isCreditsInAppEnabled(Context context);

	/**
	 * Inizializza le conversazioni degli utenti fake
	 */
	void initFakeUsersConversations();

	/**
	 * Se la conversazione passata è con un fake user, viene generata una
	 * risposta casuale tra quelle disponibili per l'utente selezionato
	 * 
	 * @param conversation
	 */
	void setResponseIfConversationIsFake(ChatConversation conversation);

	/**
	 * Aggiunge i developers all'elenco degli utenti visibili
	 * 
	 * @param context
	 */
	void addDevelopers(Context context);

	/**
	 * Rimuove i developers dall'elenco degli utenti visibili
	 * 
	 */
	void removeDevelopers();

}
