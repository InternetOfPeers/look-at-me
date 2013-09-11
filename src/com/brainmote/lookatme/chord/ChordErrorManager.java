/**
 * 
 * This class extends generic LookAtMeErrorManager
 * to decode error codes that are chord specific
 */
package com.brainmote.lookatme.chord;

import android.util.Log;

public class ChordErrorManager extends ErrorManager {

	/**
	 * specify chord error code in switch case
	 */
	@Override
	public void checkError(int result) throws CustomException {
		super.checkError(result);
		Log.d("[LOOKATME_ERRORMANAGER]", "[LookAtMeChordErrorManager] : checkError " + result);
		String message = null;
		// TODO Capire le costanti di ritorno che dovrebbero essere dentro
		// SchordManager.StatusListener.*
		/*
		 * switch (result) { case ChordManager.ERROR_FAILED: message =
		 * "FAILED";// TODO 1 break; case ChordManager.ERROR_INVALID_INTERFACE:
		 * message = "INVALID INTERFACE";// TODO 4 break; case
		 * ChordManager.ERROR_INVALID_PARAM: message = "INVALID PARAM";// TODO 2
		 * break; case ChordManager.ERROR_INVALID_STATE: message =
		 * "INVALID STATE";// TODO 3 break; default: break; }
		 */
		if (message != null) {
			throw new CustomException(message);
		}
	}

}
