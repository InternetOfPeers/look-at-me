/**
 * Interface for managing device communication.
 * This is the main access point for Activities
 * to start communication between devices.
 */
package com.dreamteam.lookme.chord;

public interface CommunicationManager {

	void startCommunication() throws CustomException;

	void stopCommunication();

	boolean sendBasicProfileRequestAll(); /*
										 * Maybe not necessary (to testing)
										 */

	boolean sendFullProfileRequest(String nodeTo);

	boolean sendMyNewBasicProfileAll(); /*
										 * to communicate all nodes my profile
										 * is updated
										 */

	boolean sendChatMessage(String nodeTo, String message);

	boolean sendStartChatMessage(String nodeTo);

	boolean sendLike(String nodeTo);

	boolean sendFullProfileResponse(String nodeTo);

}
