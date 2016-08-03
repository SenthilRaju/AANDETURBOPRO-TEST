package com.turborep.turbotracker.product.exception;

public class ProductException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public ProductException(String theMessage){
		super(theMessage);
		setErrorStatusCode(500);
	}
	
	public ProductException(String theMessage, Throwable theThrowable){
		super(theMessage, theThrowable);
		setErrorStatusCode(500);
	}

	public int getErrorStatusCode() {
		return itsErrorStatusCode;
	}

	public void setErrorStatusCode(int itsErrorStatusCode) {
		this.itsErrorStatusCode = itsErrorStatusCode;
	}
}
