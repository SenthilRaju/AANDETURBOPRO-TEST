package com.turborep.turbotracker.finance.exception;

public class DrillDownException  extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public DrillDownException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public DrillDownException(String theMessage, Throwable theThrowable){
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
