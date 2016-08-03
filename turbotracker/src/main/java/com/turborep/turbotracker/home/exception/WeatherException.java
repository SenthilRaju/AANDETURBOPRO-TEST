package com.turborep.turbotracker.home.exception;

public class WeatherException extends Exception {

	private static final long serialVersionUID = 1L;
	private int itsErrorStatusCode;
	
	public WeatherException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public WeatherException(String theMessage, Throwable theThrowable){
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
