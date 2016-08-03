/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.logger;

import org.apache.log4j.Logger;

public class Log4jLog
{
	/**
	 * Method is used to get the logger.
	 * @param theClass	Class object
	 * @return	Logger object
	 */
	public static Logger getLogger( Class<?> theClass )
	{
		Logger aLogger = Logger.getLogger( theClass );
		return aLogger;
	}
	
}
