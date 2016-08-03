package com.turborep.turbotracker.job.exception;

public class JobException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public JobException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public JobException(String theMessage, Throwable theThrowable){
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
