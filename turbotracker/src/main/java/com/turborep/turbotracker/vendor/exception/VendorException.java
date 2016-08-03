/**
 * Copyright (c) 2014 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.vendor.exception;

public class VendorException extends Exception {

	private static final long serialVersionUID = 1L;
	private int itsErrorStatusCode;
	
	public VendorException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public VendorException(String theMessage, Throwable theThrowable){
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
