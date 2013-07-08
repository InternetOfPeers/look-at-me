/**
 * Author: Carlo Tassi
 * 
 * Interface for managing device communication.
 * This is the main access point for Activities
 * to start communication between devices.
 */
package com.dreamteam.lookme.communication;

public interface ILookAtMeCommunicationManager {

	public void startCommunication();

	public void stopCommunication();

	public void sendToAll(ILookAtMeMessage message);

}
