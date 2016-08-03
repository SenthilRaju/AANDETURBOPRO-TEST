package com.turborep.turbotracker.employee.exception;

public class EmployeeException  extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public EmployeeException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public EmployeeException(String theMessage, Throwable theThrowable){
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
