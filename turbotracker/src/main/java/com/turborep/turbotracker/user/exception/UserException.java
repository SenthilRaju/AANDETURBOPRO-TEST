package com.turborep.turbotracker.user.exception;

public class UserException extends Exception {

	private static final long serialVersionUID = 1L;

	private int itsErrorStatusCode;

	public UserException(String theMessage) {
		super(theMessage);
		setItsErrorStatusCode(500);
	}

	public UserException(String theMessage, Throwable theThrowable) {
		super(theMessage, theThrowable);
		setItsErrorStatusCode(500);
	}
	
	public int getItsErrorStatusCode() {
		return itsErrorStatusCode;
	}

	public void setItsErrorStatusCode(int itsErrorStatusCode) {
		this.itsErrorStatusCode = itsErrorStatusCode;
	}

}
