package com.turborep.turbotracker.job.exception;

public class QuoteTemplateException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public QuoteTemplateException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public QuoteTemplateException(String theMessage, Throwable theThrowable){
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
