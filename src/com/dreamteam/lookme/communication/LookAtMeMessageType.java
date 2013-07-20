/**
 * Author: Carlo Tassi
 * 
 * Enumeration of all message types that can be 
 * sent over chord by Look@me App
 */
package com.dreamteam.lookme.communication;

public enum LookAtMeMessageType {

	PREVIEW, /* Minimal profile data */
	PROFILE, /* Complete profile data */
	LIKE, /* Love game */
	CHAT_MESSAGE, PROFILE_UPDATE, /*
								 * used to send all nodes new profile if it is
								 * updated
								 */
	PREVIEW_REQUEST, /* request minimal profile data */
	PROFILE_REQUEST; /* request complete profile data */

	@Override
	public String toString() {
		return name();
	}

}
