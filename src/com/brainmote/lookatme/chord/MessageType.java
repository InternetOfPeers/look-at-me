/**
 * Author: Carlo Tassi
 * 
 * Enumeration of all message types that can be 
 * sent over chord by Look@me App
 */
package com.brainmote.lookatme.chord;

public enum MessageType {

	BASIC_PROFILE, /* Minimal profile data */
	FULL_PROFILE, /* Complete profile data */
	LIKE, /* Love game */
	CHAT_MESSAGE, /* Chat message over private channel */
	PROFILE_UPDATE, /* TODO: used to send all nodes new profile if it is updated */
	BASIC_PROFILE_REQUEST, /* request minimal profile data */
	FULL_PROFILE_REQUEST, /* request complete profile data */
	START_CHAT_MESSAGE; /* request a chat session */

	@Override
	public String toString() {
		return name();
	}

}
