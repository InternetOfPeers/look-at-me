/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord;

public class CustomException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomException() {
		super();
	}

	public CustomException(Throwable e) {
		super(e);
	}

	public CustomException(String m) {
		super(m);
	}

	public CustomException(String m, Throwable e) {
		super(m, e);
	}
}
