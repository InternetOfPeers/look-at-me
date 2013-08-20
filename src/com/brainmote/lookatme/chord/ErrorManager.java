/**
 * Author: Carlo Tassi
 */
package com.brainmote.lookatme.chord;

public class ErrorManager {

	/*
	 * Add here all app custom error code
	 */
	public static final int ERROR_NO_INTERFACE_AVAILABLE = 1001;

	public void checkError(int result) throws CustomException {
		String message = null;
		switch (result) {
		case ERROR_NO_INTERFACE_AVAILABLE:
			message = "NO INTERFACE AVAILABLE";// TODO
			break;
		default:
			break;
		}
		if (message != null) {
			throw new CustomException(message);
		}
	}
}
