/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.banking.exception;
/**
 * Extending <b>Exception</b> to customize banking exceptions.
 */
public class BankingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int itsErrorStatusCode;
	
	public BankingException(String theMessage){
		super(theMessage);
		setItsErrorStatusCode(500);
	}
	
	public BankingException(String theMessage, Throwable theThrowable){
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
