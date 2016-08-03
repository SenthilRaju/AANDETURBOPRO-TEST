package com.turborep.turbotracker.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionConstants {

	public static final String USER = "user";
	
	public static final String JOB_GRID_OBJ = "jobGridObj";

	public static final String CUST_GRID_OBJ = "custGridObj";
	
	public static final String JOB_TOKEN = "jobToken";
	
	public static final String ROLODEX_ID = "rxMasterId";
	
	public static final String SUBSTITUTION_HEADER = "joSubstitutionHeader";
	
	public static final String SALES_USERID = "salesUserId";
	
	public static final String PROJECTS_USERID = "projectsUserId";
	
	public static final String INVENTORY_GRID_OBJ = "inventoryGridObj";
	
	public static final String INVENTORY_TOKEN = "inventoryToken";
	
	public static final String BANKING_GRID_OBJ = "bankingGridObj";
	
	public static final String BANKING_TOKEN = "bankingToken";
	
	public static final String BANKING_DETAILS = "bankingDetails";
	
	public static final String SYSUSERDEFAULT="sysUserDefault";
	
	public static final String TSUSERSETTINGS="tsUserSetting";
	
	public static boolean checkSessionExist(HttpServletRequest theRequest) {
		HttpSession aSession = theRequest.getSession();
		if(aSession.getAttribute(SessionConstants.USER) == null) {
			System.out.println("No user.........");
			return false;
		} else {
			System.out.println("user is there......");
			return true;
		}
	}
}
