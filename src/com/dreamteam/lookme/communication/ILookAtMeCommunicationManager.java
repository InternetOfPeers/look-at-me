/**
 * Author: Carlo Tassi
 * 
 * Interface for managing device communication.
 * This is the main access point for Activities
 * to start communication between devices.
 */
package com.dreamteam.lookme.communication;

import com.dreamteam.lookme.error.LookAtMeException;

public interface ILookAtMeCommunicationManager {

	public void startCommunication() throws LookAtMeException;

	public void stopCommunication();

	public boolean sendBasicProfileRequestAll(); /*
													 * Maybe not necessary (to
													 * testing)
													 */

	public boolean sendFullProfileRequest(String nodeTo);

	public boolean sendBasicProfileAll(); /*
											 * to communicate all nodes my
											 * profile is updated
											 */

	public boolean sendChatMessage(String nodeTo, String message, String channelName);

	public boolean sendStartChatMessage(String nodeTo);

	public boolean sendLike(String nodeTo);

}
