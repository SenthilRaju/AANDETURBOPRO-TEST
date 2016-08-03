package com.turborep.turbotracker.company.Exception;

public class CompanyException extends Exception {

	private static final long serialVersionUID = 1L;

	private int itsErrorStatusCode;

	public CompanyException(String theMessage) {		
		super(theMessage);
		setItsErrorStatusCode(500);
	}

	public CompanyException(String theMessage, Throwable theThrowable) {
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