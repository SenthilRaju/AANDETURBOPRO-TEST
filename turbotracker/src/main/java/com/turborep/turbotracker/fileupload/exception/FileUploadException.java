package com.turborep.turbotracker.fileupload.exception;

public class FileUploadException extends Exception {


	private static final long serialVersionUID = 1L;

	private int itsErrorStatusCode;
	
	public FileUploadException(String theMessage){
		super(theMessage);
		setErrorStatusCode(500);
	}
	
	public FileUploadException(String theMessage, Throwable theThrowable){
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
