/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord;

import android.content.Context;

import com.dreamteam.lookme.communication.ILookAtMeCommunicationManager;

public class LookAtMeChordCommunication implements ILookAtMeCommunicationManager {
	
	private LookAtMeChord chord;
	
	public LookAtMeChordCommunication(Context context) {
		chord = new LookAtMeChord(context);
	}
	
	/**
	 * This method calls LookAtMeChord constructor that joins 
	 * to public channel and to private social channel. 
	 * If user set game enabled then it joins to private game channel.
	 * From this moment every events are triggered by chord manager
	 * and chord listeners.
	 */
	@Override
	public void startCommunication() {
		chord.joinSocialChannel();
		if (true) { // TODO: mettere la condizione di check game abilitato
			chord.joinGameChannel();
		}
		
	}

	@Override
	public void stopCommunication() {
		chord.stopChord();
	}

}
