package com.dreamteam.lookme.error;

public class LookAtMeException extends Exception {

	private static final long serialVersionUID = 1L;

	public LookAtMeException() {
		super();
	}
	
	public LookAtMeException(Throwable e) {
		super(e);
	}
	
	public LookAtMeException(String m) {
		super(m);
	}
	
	public LookAtMeException(String m, Throwable e) {
		super(m, e);
	}
}
