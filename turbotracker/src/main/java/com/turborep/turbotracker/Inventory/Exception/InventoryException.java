package com.turborep.turbotracker.Inventory.Exception;

public class InventoryException extends Exception {

	private static final long serialVersionUID = 1L;

	private int itsErrorStatusCode;

	public InventoryException(String theMessage) {
		super(theMessage);
		setItsErrorStatusCode(500);
	}

	public InventoryException(String theMessage, Throwable theThrowable) {
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
