/**
 * Author: Carlo Tassi
 * 
 * This class extends generic LookAtMeErrorManager
 * to decode error codes that are chord specific
 */
package com.dreamteam.lookme.chord;

import android.util.Log;

import com.dreamteam.lookme.error.LookAtMeErrorManager;
import com.dreamteam.lookme.error.LookAtMeException;
import com.samsung.chord.ChordManager;

public class LookAtMeChordErrorManager extends LookAtMeErrorManager {
	
	/**
	 * specify chord error code in switch case
	 */
	@Override
	public void checkError(int result) throws LookAtMeException {
		super.checkError(result);
		Log.d("[LOOKATME_ERRORMANAGER]", "[LookAtMeChordErrorManager] : checkError " + result);
		String message = null;
		switch (result) {
		case ChordManager.ERROR_FAILED: 
			message = "prendere errore da un resource";// TODO 1
			break;
		case ChordManager.ERROR_INVALID_INTERFACE: 
			message = "prendere errore da un resource";// TODO 4
			break;
		case ChordManager.ERROR_INVALID_PARAM: 
			message = "prendere errore da un resource";// TODO 2
			break;
		case ChordManager.ERROR_INVALID_STATE: 
			message = "prendere errore da un resource";// TODO 3
			break;
		default: break;
		}
		if (message != null) {
			throw new LookAtMeException(message);
		}
	}

}
