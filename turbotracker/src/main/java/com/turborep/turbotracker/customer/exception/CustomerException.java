package com.turborep.turbotracker.customer.exception;

public class CustomerException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public CustomerException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public CustomerException(String theMessage, Throwable theThrowable){
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
