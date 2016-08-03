package com.turborep.turbotracker.settings.exception;

public class SettingsException extends Exception {

	private static final long serialVersionUID = 1L;

	private int itsErrorStatusCode;
	
	public SettingsException(String theMessage){
		super(theMessage);
		setErrorStatusCode(500);
	}
	
	public SettingsException(String theMessage, Throwable theThrowable){
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
