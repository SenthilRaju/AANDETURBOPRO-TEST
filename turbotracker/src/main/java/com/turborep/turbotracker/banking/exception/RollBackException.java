package com.turborep.turbotracker.banking.exception;

public class RollBackException extends Exception {
	

	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public RollBackException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public RollBackException(String theMessage, Throwable theThrowable){
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
