/**
 * Interface for managing device communication.
 * This is the main access point for Activities
 * to start communication between devices.
 */
package com.dreamteam.lookme.chord;

public interface CommunicationManager {

	void startCommunication() throws CustomException;

	void stopCommunication();

	boolean requestAllProfiles();

	boolean requestFullProfile(String fromNodeId);

	boolean notifyMyProfileIsUpdated();

	boolean sendChatMessage(String toNodeId, String message);

	boolean sendStartChatMessage(String toNodeId);

	boolean sendLike(String toNodeId);

	boolean sendFullProfileResponse(String toNodeId);

}
