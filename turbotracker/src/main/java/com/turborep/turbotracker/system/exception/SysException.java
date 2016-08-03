package com.turborep.turbotracker.system.exception;

public class SysException extends Exception {

private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public SysException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public SysException(String theMessage, Throwable theThrowable){
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
