package com.brainmote.lookatme.chord;

import java.util.List;

import com.brainmote.lookatme.ChatConversation;

public interface CommunicationManager {

	void startCommunication() throws CustomException;

	void stopCommunication();

	void requestAllProfiles();

	void requestFullProfile(String fromNodeId);

	void notifyMyProfileIsUpdated();

	void sendChatMessage(ChatConversation conversation, String message);

	void startChat(String toNodeId);

	void sendLike(String toNodeId);

	/**
	 * Restituisce l'elenco di tutti gli utenti che sono connessi al canale
	 * principale
	 * 
	 */
	List<String> getActiveNodeList();

	/**
	 * Restituisce l'elenco dei nodi attualmente connessi al canale specificato
	 * 
	 * @param channelId
	 * @return Restituisce l'elenco dei nodi attualmente connessi al canale
	 *         specificato
	 */
	List<String> getActiveNodeListInChannel(String channelId);

	/**
	 * Verifica lo stato della conversazione e se necessario richiede il join da
	 * parte dei nodi cionvolti e attualmente esistenti
	 * 
	 * @param conversation
	 *            La conversazione da analizzare
	 * @return Restituisce true se la verifica è andata correttamente e non sono
	 *         state effettuate modifiche al sistema, false se la procedura è
	 *         stata costretta a coreggere la conversazione e ad inviatare i
	 *         nodi esistenti a riconnettersi nuovamente
	 */
	boolean checkAndJoinChatConversation(ChatConversation conversation);

	/**
	 * Recupera il nodeId dell'interlocutore considerando la conversazione
	 * passata
	 * 
	 * @param conversation
	 *            la conversazione da considerare
	 * @return il nodeId dell'interlocutore considerando la conversazione
	 *         passata
	 */
	String getNodeIdFromConversation(ChatConversation conversation);

	/**
	 * Verifica se un nodo è presente nel canale principale dell'applicazione
	 * 
	 * @param nodeId
	 *            Il nodeId da verificare
	 * @return true se il nodo è presente, false in caso contrario
	 */
	boolean isNodeAlive(String nodeId);

}
