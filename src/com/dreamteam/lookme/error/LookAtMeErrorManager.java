/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.error;

public class LookAtMeErrorManager {

	/*
	 * Add here all app custom error code
	 */
	public static final int ERROR_NO_INTERFACE_AVAILABLE = 1001;

	public void checkError(int result) throws LookAtMeException {
		String message = null;
		switch (result) {
		case ERROR_NO_INTERFACE_AVAILABLE:
			message = "prendere errore da un resource";// TODO
			break;
		default:
			break;
		}
		if (message != null) {
			throw new LookAtMeException(message);
		}
	}
}
