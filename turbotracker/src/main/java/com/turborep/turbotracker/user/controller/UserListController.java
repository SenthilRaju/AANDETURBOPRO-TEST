/**
 
* Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.company.dao.ChSegments;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Joquotecolumn;
import com.turborep.turbotracker.job.dao.jocategory;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.system.dao.Sysassignment;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.exception.SysException;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.AccessProcedureConstant;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserGroup;
import com.turborep.turbotracker.user.dao.TsUserGroupLink;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping(value ="/userlistcontroller")
public class UserListController {

	protected static Logger itsLogger = Logger.getLogger(UserListController.class);
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="sysService")
	private SysService sysservice;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeServiceInter;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@Resource(name = "jobService")
	private JobService jobService;
	
	@RequestMapping(value = "getuserautofilldata", method = RequestMethod.GET)
	public @ResponseBody List<?> logout(@RequestParam("term") String theKeyword, HttpServletResponse response,HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aUserList = null;
		try {
			aUserList = (ArrayList<AutoCompleteBean>)itsUserService.getUserAutoSuggestList(theKeyword);
		} catch (UserException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theKeyword:</b>"+theKeyword ,"UserListController",e,theSession,theRequest);
		}
		return aUserList;
	}
	
	@RequestMapping(value = "/userlist", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAll(@RequestParam(value="page", required=false) Integer thePage,
													@RequestParam(value="rows", required=false) Integer theRows,
													@RequestParam(value="sidx", required=false) String theSidx,
													@RequestParam(value="sord", required=false) String theSord,
													@RequestParam(value="userActiveID", required=false) boolean theUserActive,
													HttpServletResponse response,
													HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		try{
			String aUserID = "user";
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			byte aInActive = (byte) (theUserActive?1:0);
			int aTotalCount = itsUserService.getRecordCount(aInActive);
			TsUserLogin aUserLogin = new TsUserLogin();
			aUserLogin.setUserperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aUserID);
			int aFrom, aTo;
			System.out.println(theRows+"::"+thePage);
			
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<?> aUserList = itsUserService.getUserList(aFrom, aTo, aInActive);
			aResponse = new CustomResponse();
			aResponse.setRows(aUserList);
			aResponse.setRecords(String.valueOf(aUserList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			response.sendError(501, excep.getMessage());
			sendTransactionException("<b>userlist:</b>","UserListController",excep,session,theRequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/CheckUserName", method = RequestMethod.GET)
	public @ResponseBody String getCheckUserName(@RequestParam(value = "checkUserName") String theUserName, HttpServletResponse response,HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException {
		itsLogger.debug("Getting the auto suggested user Name");
		String aUserName = null;
		try {
			aUserName = itsUserService.getCheckUserName(theUserName);
		} catch (UserException excep) {
			itsLogger.error(excep.getMessage(), excep);
			response.sendError(501, excep.getMessage());
			sendTransactionException("<b>theUserName:</b>"+theUserName ,"UserListController",excep,theSession,theRequest);
		}
		return aUserName;
	}
	
	@RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
	public @ResponseBody TsUserLogin addNewUser (@RequestParam(value = "loginName", required = false) String theLoginName,
													@RequestParam(value = "firstName", required = false) String theFirstName,
													@RequestParam(value = "middleName", required = false) String theMiddleName,
													@RequestParam(value = "lastName", required = false) String theLastName,
													@RequestParam(value = "initialsName", required = false) String theInitials, 
													@RequestParam(value = "sysAdminName", required = false) boolean theSysAdmin,
													@RequestParam(value = "passwordName", required = false) String thePassword, 
													@RequestParam(value = "inactiveboxName", required = false) boolean theInactive,
													@RequestParam(value = "salesRepName", required = false) boolean theSalesRep, 
													@RequestParam(value = "CSRBoxName", required = false) boolean theCSR, 
													@RequestParam(value = "salesMgrName", required = false) boolean theSalesMgr, 
													@RequestParam(value = "EnginnerName", required = false) boolean theEngineer,
													@RequestParam(value = "projectMgrName", required = false) boolean theProjectMgr,
													 @RequestParam(value = "isEmployee", required = false) boolean isEmployee,
													HttpSession theSession, HttpServletResponse response,HttpServletRequest theRequest) throws IOException, MessagingException {
		TsUserLogin aUserLogin = new TsUserLogin();
		TsUserLogin aTsUserLogin = null;
		itsLogger.info("Is Employee: "+isEmployee);
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aUserLogin.setLoginName(theLoginName);
			aUserLogin.setLoginPassword(thePassword);
			String aFullName ="";
			
			if(theMiddleName.trim().equals(""))
				aFullName = theFirstName+" "+theLastName;
			else
				aFullName = theFirstName+" "+theMiddleName+" "+theLastName;
			
			aUserLogin.setFullName(aFullName);
			aUserLogin.setInitials(theInitials.toUpperCase());
			byte aSysAdmin = (byte) (theSysAdmin?1:0);
			byte aInActive = (byte) (theInactive?1:0);
			byte aSalesRep = (byte) (theSalesRep?1:0);
			byte aSalesMgr = (byte) (theSalesMgr?1:0);
			byte aCSR = (byte) (theCSR?1:0);
			byte aEnginner = (byte) (theEngineer?1:0);
			byte aProjectMgr = (byte) (theProjectMgr?1:0);
			aUserLogin.setSystemAdministrator(aSysAdmin);
			aUserLogin.setUserperpage(25);
			aUserLogin.setCustomerperpage(50);
			aUserLogin.setVendorperpage(100);
			aUserLogin.setEmployeeperpage(50);
			aUserLogin.setBankingperpage(25);
			aUserLogin.setRolodexperpage(50);
			aUserLogin.setInactive(aInActive);
			aUserLogin.setEmployee0(aSalesRep);
			aUserLogin.setEmployee1(aCSR);
			aUserLogin.setEmployee2(aSalesMgr);
			aUserLogin.setEmployee3(aEnginner);
			aUserLogin.setEmployee4(aProjectMgr);
			aUserLogin.setChartsperpage(25);
			aTsUserLogin = itsUserService.addUser(aUserLogin);
			if(isEmployee && aTsUserLogin.getUserLoginId() >0){
				Rxmaster aEmployee = new Rxmaster();
				Rxaddress aRxaddress = new Rxaddress();
				Rxmaster aNewEmployee = null;
				aEmployee.setName(aFullName);
				boolean inActive = false;
				boolean IsStreet = false;
				boolean IsMailing = false;
				boolean IsBillTo = true;
				boolean IsShipTo = true;
				aEmployee.setFirstName(theFirstName);
				aEmployee.setCreatedById(aUserBean.getUserId());
				String asearchName = theFirstName.replace(" ", "");
				if(asearchName.length() > 10){
					String asearchText = asearchName.substring(0, 10);
					aEmployee.setSearchName(asearchText.toUpperCase());
				}else{
					aEmployee.setSearchName(asearchName.toUpperCase());
				}
				if(aInActive>0){
				aEmployee.setInActive(true);
				}
				else{
					aEmployee.setInActive(false);
				}
				aEmployee.setPhone1("-");
				aEmployee.setPhone2("-");
				aEmployee.setPhone3("-");
				aEmployee.setFax("-");
				aEmployee.setIsEmployee(true);
				aRxaddress.setName(theFirstName);
				aRxaddress.setZip("-");
				aRxaddress.setInActive(inActive);
				aRxaddress.setIsMailing(IsMailing);
				aRxaddress.setIsShipTo(IsShipTo);
				aRxaddress.setIsStreet(IsStreet);
				aRxaddress.setIsBillTo(IsBillTo);
				aRxaddress.setOtherBillTo(2);
				aRxaddress.setOtherShipTo(3);
				aRxaddress.setIsDefault(true);
				aNewEmployee = itsEmployeeServiceInter.addNewEmployee(aEmployee, aRxaddress);
				if(aNewEmployee!=null && aNewEmployee.getRxMasterId()!=null)
				{
					Emmaster aemmaster=new Emmaster();
					aemmaster.setEmMasterId(aNewEmployee.getRxMasterId());
					aemmaster.setUserLoginId(aTsUserLogin.getUserLoginId());
					itsEmployeeServiceInter.addemMaster(aemmaster);
					
				}
			}
			
		} catch (UserException excep) {
			itsLogger.error(excep.getMessage(), excep);
			response.sendError(501, excep.getMessage());
			sendTransactionException("<b>theLoginName:</b>"+theLoginName ,"UserListController",excep,theSession,theRequest);
		}
		return aTsUserLogin;
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody TsUserLogin updateUser (@RequestParam(value = "loginName", required = false) String theLoginName,
													 @RequestParam(value = "firstName", required = false) String theFirstName,
													 @RequestParam(value = "middleName", required = false) String theMiddleName,
													 @RequestParam(value = "lastName", required = false) String theLastName,
													 @RequestParam(value = "initialsName", required = false) String theInitials, 
													 @RequestParam(value = "updateSYSAdmin", required = false) boolean theUpdateSysAdmin,
													 @RequestParam(value = "updateActive", required = false) boolean theUpdateActive,
													 @RequestParam(value = "updateSalesRep", required = false) boolean theUpdateSalesRep,
													 @RequestParam(value = "updateCSR", required = false) boolean theUpdateCSR,
													 @RequestParam(value = "updateSalesMgr", required = false) boolean theUpdateSalesMgr,
													 @RequestParam(value = "updateEngineer", required = false) boolean theUpdateEngineer,
													 @RequestParam(value = "updateProjectMgr", required = false) boolean theUpdateProjectMgr,
													 @RequestParam(value = "userLoginID", required = false) Integer theUserLoginID,
													 @RequestParam(value = "sysAdminNameBox", required = false) boolean theSysAdmin,
													 @RequestParam(value = "passwordName", required = false) String thePassword, 
													 @RequestParam(value = "inactiveNameBox", required = false) boolean theInactive,
													 @RequestParam(value = "salesRepName", required = false) boolean theSalesRep, 
													 @RequestParam(value = "CSRBoxName", required = false) boolean theCSR, 
													 @RequestParam(value = "salesMgrName", required = false) boolean theSalesMgr, 
													 @RequestParam(value = "EnginnerName", required = false) boolean theEngineer,
													 @RequestParam(value = "projectMgrName", required = false) boolean theProjectMgr,
													 @RequestParam(value = "zipcode", required = false) String theZipCode,
													 HttpSession theSession, HttpServletResponse response,HttpServletRequest theRequest) throws IOException, MessagingException {
		TsUserLogin aUserLogin = new TsUserLogin();
		TsUserLogin aTsUserLogin = null;
		try {
			aUserLogin.setLoginName(theLoginName);
			aUserLogin.setLoginPassword(thePassword);
			String aFullName;
			if(theMiddleName.trim().equals(""))
				aFullName = theFirstName+" "+theLastName;
			else
				aFullName = theFirstName+" "+theMiddleName+" "+theLastName;
			//String aFullName = theFirstName+" "+theMiddleName+" "+theLastName;
			aUserLogin.setFullName(aFullName);
			aUserLogin.setInitials(theInitials.toUpperCase());
			byte aSysAdmin = (byte) (theUpdateSysAdmin?1:0);
			byte aInActive = (byte) (theUpdateActive?1:0);
			byte aSalesRep = (byte) (theUpdateSalesRep?1:0);
			byte aSalesMgr = (byte) (theUpdateSalesMgr?1:0);
			byte aCSR = (byte) (theUpdateCSR?1:0);
			byte aEnginner = (byte) (theUpdateEngineer?1:0);
			byte aProjectMgr = (byte) (theUpdateProjectMgr?1:0);
			aUserLogin.setSystemAdministrator(aSysAdmin);
			aUserLogin.setUserperpage(25);
			aUserLogin.setCustomerperpage(50);
			aUserLogin.setVendorperpage(100);
			aUserLogin.setEmployeeperpage(50);
			aUserLogin.setRolodexperpage(50);
			aUserLogin.setInactive(aInActive);
			aUserLogin.setEmployee0(aSalesRep);
			aUserLogin.setEmployee1(aCSR);
			aUserLogin.setEmployee2(aSalesMgr);
			aUserLogin.setEmployee3(aEnginner);
			aUserLogin.setEmployee4(aProjectMgr);
			aUserLogin.setUserLoginId(theUserLoginID);
			aUserLogin.setUserzipcode(theZipCode);
			aTsUserLogin = itsUserService.updateUser(aUserLogin);
			
		} catch (UserException excep) {
			itsLogger.error(excep.getMessage(), excep);
			response.sendError(501, excep.getMessage());
			sendTransactionException("<b>theLoginName:</b>"+theLoginName ,"UserListController",excep,theSession,theRequest);
		}
		return aTsUserLogin;
	}
	
	@RequestMapping(value = "/addUserDefaults", method = RequestMethod.POST)
	public @ResponseBody Integer addUserDefaults (@RequestParam(value = "warehouseName", required = false) Integer wareHouseID,
													@RequestParam(value = "divisionName", required = false) Integer coDivisionID,
													@RequestParam(value = "loginuserID", required = false) Integer LoginUserId,
													HttpServletResponse response,HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		SysUserDefault aSysUserDefault = new SysUserDefault();
		Integer status=0;
		
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			aSysUserDefault.setCoDivisionID(coDivisionID);
			aSysUserDefault.setWarehouseID(wareHouseID);
			aSysUserDefault.setUserLoginID(aUserBean.getUserId());
			itsLogger.info("Data From JSP:"+wareHouseID+" : "+coDivisionID+" : "+aSysUserDefault.getUserLoginID());
			try {
				status = itsUserService.addUserDefaults(aSysUserDefault);
			} catch (SysException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTransactionException("<b>wareHouseID:</b>"+wareHouseID ,"UserListController",e,session,theRequest);
			}
		return status;
	}
	
	@RequestMapping(value = "/updateGroupDetails", method = RequestMethod.POST)
	public @ResponseBody TsUserGroupLink updateGroupDetails(@RequestParam(value = "salesRepsName", required = false) String theLoginName,
																@RequestParam(value = "CSRsBoxName", required = false) String theFirstName,
																@RequestParam(value = "employeesName", required = false) String theMiddleName,
																@RequestParam(value = "administrativeName", required = false) String theLastName,
																@RequestParam(value = "groupLinkID", required = false) String theGroupLinkID,
																@RequestParam(value = "userLoginID", required = false) Integer theUserLoginID,
																@RequestParam(value = "wareHouseName", required = false) String theInitials, HttpSession theSession){
		TsUserGroupLink aUserGroupLink = new TsUserGroupLink();
		return aUserGroupLink;
	}
	
	@RequestMapping(value = "/updateGroupLink", method = RequestMethod.POST)
	public @ResponseBody TsUserGroupLink updateGroupLink(@RequestParam(value = "salesRepsName", required = false) String theLoginName,
																@RequestParam(value = "CSRsBoxName", required = false) String theFirstName,
																@RequestParam(value = "employeesName", required = false) String theMiddleName,
																@RequestParam(value = "administrativeName", required = false) String theLastName,
																@RequestParam(value = "groupID", required = false) Integer theGroupID,
																@RequestParam(value = "userLoginID", required = false) Integer theUserLoginID,
																@RequestParam(value = "wareHouseName", required = false) String theInitials,
																@RequestParam(value = "deleteBool", required = false) Boolean deleteBool,
																@RequestParam(value = "userGroupLinkId", required = false) Integer userGroupLinkId,																
																HttpSession theSession) throws UserException{
		
		itsLogger.info(" theGroupID = "+theGroupID+" || theUserLoginID = "+theUserLoginID+" || deleteBool = "+deleteBool+" || userGroupLinkId = "+userGroupLinkId);
		TsUserGroupLink aUserGroupLink = new TsUserGroupLink();
		aUserGroupLink.setUserGroupLinkId(userGroupLinkId);
		aUserGroupLink.setUserGroupID(theGroupID);
		aUserGroupLink.setUserLoginID(theUserLoginID);
		itsUserService.updateTsUserGroupLink( deleteBool, aUserGroupLink);
		return aUserGroupLink;
	}
	
	//CreatedBy: Naveed Date: 9 June 2015
	@RequestMapping(value = "/savequotesFooterNote", method = RequestMethod.POST)
	public @ResponseBody TsUserSetting savequotesFooterNote(@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "footernote", required = false) String footernote,
			HttpServletRequest therequest, HttpSession session,
			HttpServletResponse theResponse) throws IOException, MessagingException {
		TsUserSetting aUserSetting = new TsUserSetting();
		TsUserSetting aTsUserSetting = null;
		aUserSetting.setCompanyId(id);
		
		//footernote=JobUtil.convertintoProperFormat(footernote);
		aUserSetting.setQuotesFooter(footernote);
		
		itsLogger.info("Company ID: "+id);	
		try {
			
			aTsUserSetting = itsUserService.saveQuoteFooternote(aUserSetting);
		} catch (Exception e) {
			sendTransactionException("CompanyID"+id,"Quotes FooterNote",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aTsUserSetting;
	}
	
	//CreatedBy: Leo Date: 24 Feb 2016 - ID#508
		@RequestMapping(value = "/savesoFooterText", method = RequestMethod.POST)
		public @ResponseBody TsUserSetting savesoFooterText(@RequestParam(value = "id", required = true) int id,
				@RequestParam(value = "sofootertext", required = false) String sofootertext,
				HttpServletRequest therequest, HttpSession session,
				HttpServletResponse theResponse) throws IOException, MessagingException {
			TsUserSetting aUserSetting = new TsUserSetting();
			TsUserSetting aTsUserSetting = null;
			aUserSetting.setCompanyId(id);
			
			//sofootertext=JobUtil.convertintoProperFormat(sofootertext);
			aUserSetting.setSoFooterText(sofootertext);
			
			itsLogger.info("Company ID: "+id);	
			try {
				
				aTsUserSetting = itsUserService.saveSoFooterText(aUserSetting);
			} catch (Exception e) {
				sendTransactionException("CompanyID"+id,"Quotes FooterNote",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
			}
			return aTsUserSetting;
		}
		
	
	
	@RequestMapping(value = "/companySettings", method = RequestMethod.POST)
	public @ResponseBody String/*LinkedList<TsUserSetting>*/ saveCompanySetting (@RequestParam(value = "headerText", required = false) String theHeaderText,
																@RequestParam(value = "terms", required = false) String theTerms,
																@RequestParam(value = "quote", required = false) boolean thequote,
																@RequestParam(value = "quickquote", required = false) boolean theQuickquote, 
																@RequestParam(value = "invoices", required = false) boolean theInvoices,
																@RequestParam(value = "puchaseorder", required = false) boolean thePuchaseorder, 
																@RequestParam(value = "quote1", required = false) boolean theQuote1,
																@RequestParam(value = "quickquote1", required = false) boolean theQuickquote1, 
																@RequestParam(value = "invoices1", required = false) boolean theInvoices1,
																@RequestParam(value = "puchaseorder1", required = false) boolean thePuchaseorder1, 
																@RequestParam(value = "quote2", required = false) boolean theQuote2,
																@RequestParam(value = "quickquote2", required = false) boolean theQuickquote2, 
																@RequestParam(value = "invoices2", required = false) boolean theInvoices2,
																@RequestParam(value = "puchaseorder2", required = false) boolean thePuchaseorder2, 
																@RequestParam(value = "billToAddressName", required = false) String theAddressName,
																@RequestParam(value = "billToAddress1Name", required = false) String theAddress1Name,
																@RequestParam(value = "billToAddress2Name", required = false) String theAddress2Name,
																@RequestParam(value = "billToCityName", required = false) String theCityName,
																@RequestParam(value = "billToStateName", required = false) String theStateName,
																@RequestParam(value = "billToZipName", required = false) String theZipName,
																@RequestParam(value = "remitToAddressName", required = false) String theRemitAddressName,
																@RequestParam(value = "remitToAddress1Name", required = false) String theRemitAddress1Name,
																@RequestParam(value = "remitToAddress2Name", required = false) String theRemitAddress2Name,
																@RequestParam(value = "remitToCityName", required = false) String theRemitCityName,
																@RequestParam(value = "remitToStateName", required = false) String theRemitStateName,
																@RequestParam(value = "remitToZipName", required = false) String theRemitZipName,
																@RequestParam(value = "NorcrossbillToAddressName", required = false) String theNorcorssAddressName,
																@RequestParam(value = "NorcrossbillToAddress1Name", required = false) String theNorcorssAddress1Name,
																@RequestParam(value = "NorcrossbillToAddress2Name", required = false) String theNorcorssAddress2Name,
																@RequestParam(value = "NorcrossbillToCityName", required = false) String theNorcorssCityName,
																@RequestParam(value = "NorcrossbillToStateName", required = false) String theNorcorssStateName,
																@RequestParam(value = "NorcrossbillToZipName", required = false) String theNorcorssZipName,
																@RequestParam(value = "BirminghambillToAddressName", required = false) String theBirminghamAddressName,
																@RequestParam(value = "BirminghambillToAddress1Name", required = false) String theBirminghamAddress1Name,
																@RequestParam(value = "BirminghambillToAddress2Name", required = false) String theBirminghamAddress2Name,
																@RequestParam(value = "BirminghambillToCityName", required = false) String theBirminghamCityName,
																@RequestParam(value = "BirminghambillToStateName", required = false) String theBirminghamStateName,
																@RequestParam(value = "BirminghambillToZipName", required = false) String theBirminghamZipName,
																@RequestParam(value = "Category1Desc", required = false) String Category1Desc,
																@RequestParam(value = "Category2Desc", required = false) String Category2Desc,
																@RequestParam(value = "Category3Desc", required = false) String Category3Desc,
																@RequestParam(value = "Category4Desc", required = false) String Category4Desc,
																@RequestParam(value = "Category5Desc", required = false) String Category5Desc,
																@RequestParam(value = "Category6Desc", required = false) String Category6Desc,
																@RequestParam(value = "Category7Desc", required = false) String Category7Desc,
																@RequestParam(value = "Category8Desc", required = false) String Category8Desc,
																
																@RequestParam(value = "segment1", required = false) String segment1,
																@RequestParam(value = "segment2", required = false) String segment2,
																@RequestParam(value = "segment3", required = false) String segment3,
																@RequestParam(value = "segment4", required = false) String segment4,
																@RequestParam(value = "digits1", required = false) Integer digits1,
																@RequestParam(value = "digits2", required = false) Integer digits2,
																@RequestParam(value = "digits3", required = false) Integer digits3,
																@RequestParam(value = "digits4", required = false) Integer digits4,
																@RequestParam(value = "required1", required = false) String required1,
																@RequestParam(value = "required2", required = false) String required2,
																@RequestParam(value = "required3", required = false) String required3,
																
																@RequestParam(value = "GroupDefaults_1", required = false) String GroupDefaults_1,
																@RequestParam(value = "GroupDefaults_1id", required = false) Integer GroupDefaults_1id,
																
																@RequestParam(value = "GroupDefaults_2", required = false) String GroupDefaults_2,
																@RequestParam(value = "GroupDefaults_2id", required = false) Integer GroupDefaults_2id,
																
																@RequestParam(value = "GroupDefaults_3", required = false) String GroupDefaults_3,
																@RequestParam(value = "GroupDefaults_3id", required = false) Integer GroupDefaults_3id,
																
																@RequestParam(value = "GroupDefaults_4", required = false) String GroupDefaults_4,
																@RequestParam(value = "GroupDefaults_4id", required = false) Integer GroupDefaults_4id,
																
																@RequestParam(value = "GroupDefaults_5", required = false) String GroupDefaults_5,
																@RequestParam(value = "GroupDefaults_5id", required = false) Integer GroupDefaults_5id,
																
																@RequestParam(value = "GroupDefaults_6", required = false) String GroupDefaults_6,
																@RequestParam(value = "GroupDefaults_6id", required = false) Integer GroupDefaults_6id,
																
																@RequestParam(value = "GroupDefaults_7", required = false) String GroupDefaults_7,
																@RequestParam(value = "GroupDefaults_7id", required = false) Integer GroupDefaults_7id,
																
																@RequestParam(value = "GroupDefaults_8", required = false) String GroupDefaults_8,
																@RequestParam(value = "GroupDefaults_8id", required = false) Integer GroupDefaults_8id,
																
																HttpSession theSession, HttpServletResponse response,HttpServletRequest theRequest) throws IOException, MessagingException {
		TsUserSetting aUserSetting = new TsUserSetting();
		TsUserSetting aTsUserSetting = null;
		Sysinfo sysinfo=new Sysinfo();
		Prwarehouse aPrwareNorCross = new Prwarehouse();
		Prwarehouse aPrwareBirmingham = new Prwarehouse();
		ChSegments aChSegments = new ChSegments();
		int segmentCount=0; 
		int dbChSegmentCount=0;
		
		LinkedList<TsUserSetting> aUserSettingsList = new LinkedList<TsUserSetting>();
		try {
			
			aUserSetting.setHeaderText(theHeaderText);
			aUserSetting.setTerms(theTerms);
			byte aQuote = (byte) (thequote?1:0);
			byte aQuickquote = (byte) (theQuickquote?1:0);
			byte aInvoices = (byte) (theInvoices?1:0);
			byte aPuchaseorder = (byte) (thePuchaseorder?1:0);
			byte aQuote1 = (byte) (theQuote1?1:0);
			byte aQuickquote1 = (byte) (theQuickquote1?1:0);
			byte aInvoices1 = (byte) (theInvoices1?1:0);
			byte aPuchaseorder1 = (byte) (thePuchaseorder1?1:0);
			byte aQuote2 = (byte) (theQuote2?1:0);
			byte aQuickquote2 = (byte) (theQuickquote2?1:0);
			byte aInvoices2 = (byte) (theInvoices2?1:0);
			byte aPuchaseorder2 = (byte) (thePuchaseorder2?1:0);
			aUserSetting.setQuote(aQuote);
			aUserSetting.setQuickQuote(aQuickquote);
			aUserSetting.setInvoices(aInvoices);
			aUserSetting.setPurchaseOrders(aPuchaseorder);
			aUserSetting.setHeaderQuote(aQuote1);
			aUserSetting.setHeaderQuickQuote(aQuickquote1);
			aUserSetting.setHeaderInvoices(aInvoices1);
			aUserSetting.setHeaderPurchaseOrders(aPuchaseorder1);
			aUserSetting.setTermsQuote(aQuote2);
			aUserSetting.setTermsQuickQuote(aQuickquote2);
			aUserSetting.setTermsInvoices(aInvoices2);
			aUserSetting.setTermsPurchaseOrders(aPuchaseorder2);
			aUserSetting.setBillToDescription(theAddressName);
			aUserSetting.setBillToAddress1(theAddress1Name);
			aUserSetting.setBillToAddress2(theAddress2Name);
			aUserSetting.setBillToCity(theCityName);
			aUserSetting.setBillToState(theStateName);
			aUserSetting.setBillToZip(theZipName);
			aUserSetting.setRemitToDescription(theRemitAddressName);
			aUserSetting.setRemitToAddress1(theRemitAddress1Name);
			aUserSetting.setRemitToAddress2(theRemitAddress2Name);
			aUserSetting.setRemitToCity(theRemitCityName);
			aUserSetting.setRemitToState(theRemitStateName);
			aUserSetting.setRemitToZip(theRemitZipName);
			aUserSetting.setCompanyId(1);
			aPrwareNorCross.setDescription(theNorcorssAddressName);
			aPrwareNorCross.setAddress1(theNorcorssAddress1Name);
			aPrwareNorCross.setAddress2(theNorcorssAddress2Name);
			aPrwareNorCross.setCity(theNorcorssCityName);
			aPrwareNorCross.setState(theNorcorssStateName);
			aPrwareNorCross.setZip(theNorcorssZipName);
			aPrwareNorCross.setPrWarehouseId(1);
			aPrwareBirmingham.setDescription(theBirminghamAddressName);
			aPrwareBirmingham.setAddress1(theBirminghamAddress1Name);
			aPrwareBirmingham.setAddress2(theBirminghamAddress2Name);
			aPrwareBirmingham.setCity(theBirminghamCityName);
			aPrwareBirmingham.setState(theBirminghamStateName);
			aPrwareBirmingham.setZip(theBirminghamZipName);
			aPrwareBirmingham.setPrWarehouseId(2);
			aTsUserSetting = itsUserService.companySetting(aUserSetting);
		//	itsUserService.updateShipToAddress(aPrwareNorCross, aPrwareBirmingham);
			aUserSettingsList.add(aTsUserSetting);
			
			if(digits1==null){
				digits1=0;
			}
			if(digits2==null){
				digits2=0;
			}
			if(digits3==null){
				digits3=0;		
						}
			if(digits4==null){
				digits4=0;
			}

			
			if(!segment1.equals(""))
			{
				segmentCount++;
			}
			if(segment2!=null && !segment2.equals(""))
			{
				segmentCount++;
			}
			if(segment3!=null && !segment3.equals(""))
			{
				segmentCount++;
			}
			if(segment4!=null && !segment4.equals(""))
			{
				segmentCount++;
			}
			
			dbChSegmentCount = itsUserService.getchSegments1().size();
			
			itsLogger.info("dbSegmenttable Count:"+dbChSegmentCount);
			
			if(segmentCount > dbChSegmentCount)
			{
			for(int i=1;i<=segmentCount;i++)
			{
				
				switch(i)
				{
				case 1:
					if(i<=dbChSegmentCount)
					{
						aChSegments.setSegmentid(1);
						aChSegments.setSegmentsName(segment1);
						aChSegments.setDigitsallowed(digits1);
						aChSegments.setRequiredstatus(1);
						itsUserService.updatechSegments(aChSegments);
					}
					else
					{
						aChSegments.setSegmentsName(segment1);
						aChSegments.setDigitsallowed(digits1);
						aChSegments.setRequiredstatus(1);
						itsUserService.insertchSegments(aChSegments);
					}
					break;
				case 2:
					
					if(i<=dbChSegmentCount)
					{
						aChSegments.setSegmentid(2);
						aChSegments.setSegmentsName(segment2);
						aChSegments.setDigitsallowed(digits2);					
						
						if(required1.equals("Yes"))
						aChSegments.setRequiredstatus(1);		
						else
						aChSegments.setRequiredstatus(0);	
						
						itsUserService.updatechSegments(aChSegments);
					}
					else
					{
						aChSegments.setSegmentsName(segment2);
						aChSegments.setDigitsallowed(digits2);					
						
						if(required1.equals("Yes"))
						aChSegments.setRequiredstatus(1);		
						else
						aChSegments.setRequiredstatus(0);	
						
						itsUserService.insertchSegments(aChSegments);
					}
					
					break;					  
				case 3:		
					if(i<=dbChSegmentCount)
					{
						aChSegments.setSegmentid(3);
						aChSegments.setSegmentsName(segment3);
						aChSegments.setDigitsallowed(digits3);					
						
						if(required2.equals("Yes"))
						aChSegments.setRequiredstatus(1);		
						else
						aChSegments.setRequiredstatus(0);	
						itsUserService.updatechSegments(aChSegments);
					}
					else
					{
						aChSegments.setSegmentsName(segment3);
						aChSegments.setDigitsallowed(digits3);					
						
						if(required2.equals("Yes"))
						aChSegments.setRequiredstatus(1);		
						else
						aChSegments.setRequiredstatus(0);
						itsUserService.insertchSegments(aChSegments);
					}
					
					break;	
					
				case 4:
					
					if(i<=dbChSegmentCount)
					{
						aChSegments.setSegmentid(4);
						aChSegments.setSegmentsName(segment4);
						aChSegments.setDigitsallowed(digits4);					
						
						if(required3.equals("Yes"))
						aChSegments.setRequiredstatus(1);		
						else
						aChSegments.setRequiredstatus(0);	
						itsUserService.updatechSegments(aChSegments);
					}
					else
					{
						aChSegments.setSegmentsName(segment4);
						aChSegments.setDigitsallowed(digits4);					
						
						if(required3.equals("Yes"))
						aChSegments.setRequiredstatus(1);		
						else
						aChSegments.setRequiredstatus(0);	
						itsUserService.insertchSegments(aChSegments);
					}
					
					break;
			
			}
				//itsUserService.insertchSegments(aChSegments);
			}	
			}
			else
			{

				for(int i=1;i<=dbChSegmentCount;i++)
				{
					
					switch(i)
					{
					case 1:
						if(i<=segmentCount)
						{
							aChSegments.setSegmentid(1);
							aChSegments.setSegmentsName(segment1);
							aChSegments.setDigitsallowed(digits1);
							aChSegments.setRequiredstatus(1);
							itsUserService.updatechSegments(aChSegments);
						}
						else
						{
							aChSegments.setSegmentid(1);
							aChSegments.setSegmentsName(segment1);
							aChSegments.setDigitsallowed(digits1);
							aChSegments.setRequiredstatus(0);
							itsUserService.updatechSegments(aChSegments);
						}
						break;
					case 2:
						
						if(i<=segmentCount)
						{
							aChSegments.setSegmentid(2);
							aChSegments.setSegmentsName(segment2);
							aChSegments.setDigitsallowed(digits2);					
							
							if(required1.equals("Yes"))
							aChSegments.setRequiredstatus(1);		
							else
							aChSegments.setRequiredstatus(0);	
							
							itsUserService.updatechSegments(aChSegments);
						}
						else
						{
							aChSegments.setSegmentid(2);
							aChSegments.setSegmentsName(segment1);
							aChSegments.setDigitsallowed(digits1);
							aChSegments.setRequiredstatus(0);
							itsUserService.updatechSegments(aChSegments);
						}
						
						break;					  
					case 3:		
						if(i<=segmentCount)
						{
							aChSegments.setSegmentid(3);
							aChSegments.setSegmentsName(segment3);
							aChSegments.setDigitsallowed(digits3);					
							
							if(required2.equals("Yes"))
							aChSegments.setRequiredstatus(1);		
							else
							aChSegments.setRequiredstatus(0);	
							itsUserService.updatechSegments(aChSegments);
						}
						else
						{
							aChSegments.setSegmentid(3);
							aChSegments.setSegmentsName(segment1);
							aChSegments.setDigitsallowed(digits1);
							aChSegments.setRequiredstatus(0);
							itsUserService.updatechSegments(aChSegments);
						}
						
						break;	
						
					case 4:
						
						if(i<=segmentCount)
						{
							aChSegments.setSegmentid(4);
							aChSegments.setSegmentsName(segment4);
							aChSegments.setDigitsallowed(digits4);					
							
							if(required3.equals("Yes"))
							aChSegments.setRequiredstatus(1);		
							else
							aChSegments.setRequiredstatus(0);	
							itsUserService.updatechSegments(aChSegments);
						}
						else
						{
							aChSegments.setSegmentid(4);
							aChSegments.setSegmentsName(segment1);
							aChSegments.setDigitsallowed(digits1);
							aChSegments.setRequiredstatus(0);
							itsUserService.updatechSegments(aChSegments);
						}
						
						break;
				
				}
					//itsUserService.insertchSegments(aChSegments);
				}	
				
				
			}
			
			sysinfo.setRxMasterCategory1desc(Category1Desc);
			System.out.println("sysinfo.getRxMasterCategory1desc()"+sysinfo.getRxMasterCategory1desc());
			sysinfo.setRxMasterCategory2desc(Category2Desc);
			sysinfo.setRxMasterCategory3desc(Category3Desc);
			sysinfo.setRxMasterCategory4desc(Category4Desc);
			sysinfo.setRxMasterCategory5desc(Category5Desc);
			sysinfo.setRxMasterCategory6desc(Category6Desc);
			sysinfo.setRxMasterCategory7desc(Category7Desc);
			sysinfo.setRxMasterCategory8desc(Category8Desc);
			sysinfo.setSysInfoId(1);
			/*update Rolodex categories while changing the categories name in sysinfo table*/
			boolean sysinfoupdate=sysservice.updateRolodexCategories(sysinfo);
			itsLogger.info("while changing rolodex categories reflected in sysInfo table");
			
			
			TsUserGroup agroupDefaults=null;
			//GroupDefaults_1 GroupDefaults_1id
			//1 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_1);
			if(GroupDefaults_1==null){
				agroupDefaults.setGroupName("");
			}
			if(GroupDefaults_1id==null){
				agroupDefaults.setUserGroupId(1);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_1id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//2 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_2);
			if(GroupDefaults_2==null){
				agroupDefaults.setGroupName("");
			}
			if(GroupDefaults_2id==null){
				agroupDefaults.setUserGroupId(2);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_2id);
			}
			itsLogger.info("JTest::#"+GroupDefaults_2id);
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//3 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_3);
			if(GroupDefaults_3==null){
				agroupDefaults.setGroupName("");
			}
			if(GroupDefaults_3id==null){
				agroupDefaults.setUserGroupId(3);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_3id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//4 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_4);
			if(GroupDefaults_4==null){
				agroupDefaults.setGroupName("");
			}if(GroupDefaults_4id==null){
				agroupDefaults.setUserGroupId(4);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_4id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//5 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_5);
			if(GroupDefaults_5==null){
				agroupDefaults.setGroupName("");
			}if(GroupDefaults_5id==null){
				agroupDefaults.setUserGroupId(5);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_5id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//6 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_6);
			if(GroupDefaults_6==null){
				agroupDefaults.setGroupName("");
			}if(GroupDefaults_6id==null){
				agroupDefaults.setUserGroupId(6);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_6id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//7 setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_7);
			if(GroupDefaults_7==null){
				agroupDefaults.setGroupName("");
			}if(GroupDefaults_7id==null){
				agroupDefaults.setUserGroupId(7);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_7id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			//8  setGroupName
			agroupDefaults=new TsUserGroup();
			agroupDefaults.setGroupName(GroupDefaults_8);
			if(GroupDefaults_8==null){
				agroupDefaults.setGroupName("");
			}if(GroupDefaults_8id==null){
				agroupDefaults.setUserGroupId(8);
			}else{
				agroupDefaults.setUserGroupId(GroupDefaults_8id);
			}
			sysinfoupdate=sysservice.updateGroupDefaults(agroupDefaults);
			
			
			
		} catch (UserException excep) {
			itsLogger.error(excep.getMessage(), excep);
			response.sendError(501, excep.getMessage());
			sendTransactionException("<b>theHeaderText:</b>"+theHeaderText ,"UserListController",excep,theSession,theRequest);
		}
		//return aUserSettingsList;
		return "success";
	}
	
	@RequestMapping(value = "/saveAndTestMail", method = RequestMethod.POST)
	public @ResponseBody TsUserLogin updateEmailProperties (@RequestParam(value = "activeAdvancedEmailName", required = false) boolean theActiveEmailchk,
																@RequestParam(value = "emailName", required = false) String theEmailName,
																@RequestParam(value = "emailAddressName", required = false) String theEmailAddr,
																@RequestParam(value = "logOnBox", required = false) boolean theAuthChk,
																@RequestParam(value = "userName", required = false) String theUserName, 
																@RequestParam(value = "passwordName", required = false) String thePassword,
																@RequestParam(value = "CCEAAddress1", required = false) String theCCEAAddress1,
																@RequestParam(value = "CCEAAddress2", required = false) String theCCEAAddress2,
																@RequestParam(value = "CCEAAddress3", required = false) String theCCEAAddress3,
																@RequestParam(value = "CCEAAddress4", required = false) String theCCEAAddress4,
																@RequestParam(value = "CCName1", required = false) String theCCName1,
																@RequestParam(value = "CCName2", required = false) String theCCName2,
																@RequestParam(value = "CCName3", required = false) String theCCName3,
																@RequestParam(value = "CCName4", required = false) String theCCName4,
																@RequestParam(value = "BCCName", required = false) String theBCCAddr, 
																@RequestParam(value = "smtpServerName", required = false) String theSMTPServer,
																@RequestParam(value = "userLoginID", required = false) Integer theUserLoginID,
																@RequestParam(value = "smtpPortNumber", required = false) Integer theSMTPPort, 
																HttpSession theSession, HttpServletResponse response,HttpServletRequest theRequest) throws IOException, MessagingException {
		TsUserLogin aUserLogin = new TsUserLogin();
		TsUserLogin aTsUserLogin = null;
		try {
			byte aActiveMailChk = (byte) (theActiveEmailchk?1:0);
			byte aAuthChk = (byte) (theAuthChk?1:0);
			aUserLogin.setSmtpemailActive(aActiveMailChk);
			aUserLogin.setEmailName(theEmailName);
			aUserLogin.setEmailAddr(theEmailAddr);
			aUserLogin.setUseAuthentication(aAuthChk);
			aUserLogin.setLogOnName(theUserName);
			aUserLogin.setLogOnPswd(thePassword);
			aUserLogin.setCcaddr1(theCCEAAddress1);
			aUserLogin.setCcaddr2(theCCEAAddress2);
			aUserLogin.setCcaddr3(theCCEAAddress3);
			aUserLogin.setCcaddr4(theCCEAAddress4);
			aUserLogin.setCcname1(theCCName1);
			aUserLogin.setCcname2(theCCName2);
			aUserLogin.setCcname3(theCCName3);
			aUserLogin.setCcname4(theCCName4);
			aUserLogin.setBccaddr(theBCCAddr);
			aUserLogin.setSmtpsvr(theSMTPServer);
			aUserLogin.setSmtpport(theSMTPPort);
			aUserLogin.setUserLoginId(theUserLoginID);
			aTsUserLogin = itsUserService.updateEmailProperties(aUserLogin);
		} catch (UserException excep) {
			itsLogger.error(excep.getMessage(), excep);
			response.sendError(501, excep.getMessage());
			sendTransactionException("<b>theEmailName:</b>"+theEmailName ,"UserListController",excep,theSession,theRequest);
		}
		return aTsUserLogin;
	}
	
	@RequestMapping(value = "/userAdvancedOptions", method = RequestMethod.GET)
	public String getUserGroupDetails(@RequestParam(value="userLogin", required=false) Integer theUserID, 
																HttpSession session, ModelMap model, HttpServletRequest request) {
		String aPage = "user/user_advanced_options";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "login";
		}
		return aPage;
	}
	
	@RequestMapping(value = "/updateUserList", method = RequestMethod.POST)
	public @ResponseBody boolean userListUpdate(@RequestParam(value = "activeUserList", required = false) Integer theActiveUser,
												@RequestParam(value = "activeEmployeeList", required = false) Integer theEmployeeUser,
												HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException{
		itsLogger.debug("Updating the user list");
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		TsUserLogin aUserLogin = new TsUserLogin();
		try{
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			aUserLogin.setActiveUserList(theActiveUser);
			aUserLogin.setActiveEmployeeList(theEmployeeUser);
			itsUserService.updateUserDetails(aUserLogin);
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theActiveUser:</b>"+theActiveUser ,"UserListController",e,session,theRequest);
		}
		return true;
	}
	
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.POST)
	public @ResponseBody TsUserLogin updateUserLoginDetails(@RequestParam(value = "userLoginID", required = false) Integer theLoginID, HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		TsUserLogin aTsUserLogin = new TsUserLogin();
		try {
			aTsUserLogin =  itsUserService.getUserDetails(theLoginID);
		} catch (UserException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			sendTransactionException("<b>theLoginID:</b>"+theLoginID ,"UserListController",e,theSession,theRequest);
		}
		return aTsUserLogin;
	}
	

	@RequestMapping(value = "/updateUserPermissions", method = RequestMethod.POST)
	public @ResponseBody boolean updateUserPermissions(@RequestParam(value = "homeName", required = false) String theHomeName, @RequestParam(value = "salesName", required = false) String theSalesName,
																										@RequestParam(value = "projectName", required = false) String theProjectName, @RequestParam(value = "inventoryName", required = false) String theInventoryName,
																										@RequestParam(value = "jobQuotesName", required = false) String theJobQuotesName, @RequestParam(value = "jobSubmittalName", required = false) String theJobSubmittalName,
																										@RequestParam(value = "jobCreditName", required = false) String theJobCreditName, @RequestParam(value = "jobReleaseName", required = false) String theJobReleaseName,
																										@RequestParam(value = "jobFinancialName", required = false) String theJobFinancialName, @RequestParam(value = "jobJournalName", required = false) String theJobJournalName,
																										@RequestParam(value = "CompanyCustomerName", required = false) String theCompanyCustomerName, @RequestParam(value = "CompanyPaymentsName", required = false) String theCompanyPaymentsName,
																										@RequestParam(value = "CompanyVendorName", required = false) String theCompanyVendorName, @RequestParam(value = "CompanyPayBillName", required = false) String theCompanyPayBillName,
																										@RequestParam(value = "CompanyEmployeeName", required = false) String theCompanyEmployeeName, @RequestParam(value = "CompanyCommisionsName", required = false) String theCompanyCommisionsName,
																										@RequestParam(value = "CompanyRolodexName", required = false) String theCompanyRolodexName, @RequestParam(value = "CompanyUserName", required = false) String theCompanyUserName,
																										@RequestParam(value = "CompanySettingName", required = false) String theCompanySettingName, @RequestParam(value = "CompanyReplortName", required = false) String theCompanyReportName,
																										@RequestParam(value = "CompanyAccountName", required = false) String theCompanyAccountName, @RequestParam(value = "homeOperDelete", required = false) String theHomeOper,
																										@RequestParam(value = "salesOperDelete", required = false) String theSalesOper, @RequestParam(value = "projectOperDelete", required = false) String theProjectOper,
																										@RequestParam(value = "inventoryOperDelete", required = false) String theInventoryOper, @RequestParam(value = "quoteOperDelete", required = false) String theQuoteOper,
																										@RequestParam(value = "submittalOperDelete", required = false) String theSubmittalOper, @RequestParam(value = "creditOperDelete", required = false) String theCreditOper,
																										@RequestParam(value = "releaseOperDelete", required = false) String theReleaseOper, @RequestParam(value = "financialOperDelete", required = false) String theFinancialOper,
																										@RequestParam(value = "jouralOperDelete", required = false) String theJournalOper, @RequestParam(value = "customerOperDelete", required = false) String theCustomerOper,
																										@RequestParam(value = "paymentsOperDelete", required = false) String thePaymentOper, @RequestParam(value = "vendorOperDelete", required = false) String theVendorOper,
																										@RequestParam(value = "paybillOperDelete", required = false) String thePayBillOper, @RequestParam(value = "employeeOperDelete", required = false) String theEmployeeOper,
																										@RequestParam(value = "operation", required = false) String theOper, @RequestParam(value = "commissionOperDelete", required = false) String theCommissionOper,
																										@RequestParam(value = "rolodexOperDelete", required = false) String theRolodexOper, @RequestParam(value = "userOperDelete", required = false) String theUserOper,
																										@RequestParam(value = "settingOperDelete", required = false) String theSettingOper, @RequestParam(value = "reportOperDelete", required = false) String theReportOper,
																										@RequestParam(value = "chartAccOperDelete", required = false) String theChartOper, @RequestParam(value = "homeOperAdd", required = false) String theHomeAddOper,
																										@RequestParam(value = "salesOperAdd", required = false) String theSalesAddOper, @RequestParam(value = "projectOperAdd", required = false) String theProjectAddOper,
																										@RequestParam(value = "inventoryOperAdd", required = false) String theInventoryAddOper, @RequestParam(value = "quoteOperAdd", required = false) String theQuoteAddOper,
																										@RequestParam(value = "submittalOperAdd", required = false) String theSubmittalAddOper, @RequestParam(value = "creditOperAdd", required = false) String theCreditAddOper,
																										@RequestParam(value = "releaseOperAdd", required = false) String theReleaseAddOper, @RequestParam(value = "financialOperAdd", required = false) String theFinancialAddOper,
																										@RequestParam(value = "jouralOperAdd", required = false) String theJournalAddOper, @RequestParam(value = "customerOperAdd", required = false) String theCustomerAddOper,
																										@RequestParam(value = "paymentsOperAdd", required = false) String thePaymentAddOper, @RequestParam(value = "vendorOperAdd", required = false) String theVendorAddOper,
																										@RequestParam(value = "paybillOperAdd", required = false) String thePayBillAddOper, @RequestParam(value = "employeeOperAdd", required = false) String theEmployeeAddOper,
																										@RequestParam(value = "commissionOperAdd", required = false) String theCommissionAddOper, @RequestParam(value = "userLoginID", required = false) Integer theUserLoginID,
																										@RequestParam(value = "rolodexOperAdd", required = false) String theRolodexAddOper, @RequestParam(value = "userOperAdd", required = false) String theUserAddOper,
																										@RequestParam(value = "settingOperAdd", required = false) String theSettingAddOper, @RequestParam(value = "reportOperAdd", required = false) String theReportAddOper,
																										@RequestParam(value = "chartAccOperAdd", required = false) String theChartAddOper, HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		Sysprivilege aSysprivilege = new Sysprivilege();
		Integer aSysPrivilageID = null;
		try{
			aSysprivilege.setUserLoginId(theUserLoginID);
			aSysprivilege.setUserGroupId(aUserBean.getUserId());
			if(theHomeAddOper.equalsIgnoreCase("homeAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theHomeName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theHomeName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theSalesAddOper.equalsIgnoreCase("saleAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theSalesName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theSalesName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theProjectAddOper.equalsIgnoreCase("projectAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theProjectName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theProjectName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theInventoryAddOper.equalsIgnoreCase("inventoryAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theInventoryName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theInventoryName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theQuoteAddOper.equalsIgnoreCase("jobQuoteAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theJobQuotesName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobQuotesName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theSubmittalAddOper.equalsIgnoreCase("jobSubmittalAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theJobSubmittalName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobSubmittalName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theCreditAddOper.equalsIgnoreCase("jobCreditAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theJobCreditName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobCreditName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theReleaseAddOper.equalsIgnoreCase("jobReleaseAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theJobReleaseName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobReleaseName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theFinancialAddOper.equalsIgnoreCase("jobFinancialAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theJobFinancialName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobFinancialName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theJournalAddOper.equalsIgnoreCase("jobJournalAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theJobJournalName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobJournalName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theCustomerAddOper.equalsIgnoreCase("customerAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyCustomerName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyCustomerName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(thePaymentAddOper.equalsIgnoreCase("paymentsAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyPaymentsName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyPaymentsName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theVendorAddOper.equalsIgnoreCase("vendorAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyVendorName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyVendorName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(thePayBillAddOper.equalsIgnoreCase("payBillAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyPayBillName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyPayBillName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theEmployeeAddOper.equalsIgnoreCase("employeeAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyEmployeeName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyEmployeeName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theCommissionAddOper.equalsIgnoreCase("commissionsAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyCommisionsName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyCommisionsName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theRolodexAddOper.equalsIgnoreCase("rolodexAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyRolodexName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyRolodexName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theUserAddOper.equalsIgnoreCase("userAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyUserName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyUserName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theSettingAddOper.equalsIgnoreCase("settingAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanySettingName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanySettingName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theReportAddOper.equalsIgnoreCase("reportAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyReportName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyReportName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theChartAddOper.equalsIgnoreCase("accountAdd")){
				aSysprivilege.setAccessProcedureId(Integer.valueOf(theCompanyAccountName));
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyAccountName);
				if(aSysPrivilageID == null){
					itsUserService.updateUserPermission(aSysprivilege);
				}
			}if(theHomeOper.equalsIgnoreCase("homeDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theHomeName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theSalesOper.equalsIgnoreCase("salesDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theSalesName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theProjectOper.equalsIgnoreCase("projectDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theProjectName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theInventoryOper.equalsIgnoreCase("inventoryDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theInventoryName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theQuoteOper.equalsIgnoreCase("jobQuoteDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobQuotesName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theSubmittalOper.equalsIgnoreCase("jobSubmittalDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobSubmittalName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theCreditOper.equalsIgnoreCase("jobCreditDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobCreditName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theReleaseOper.equalsIgnoreCase("jobReleaseDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobReleaseName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theFinancialOper.equalsIgnoreCase("jobFinancialDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobFinancialName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theJournalOper.equalsIgnoreCase("jobJournalDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theJobJournalName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theCustomerOper.equalsIgnoreCase("customerDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyCustomerName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(thePaymentOper.equalsIgnoreCase("paymentsDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyPaymentsName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theVendorOper.equalsIgnoreCase("vendorDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyVendorName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(thePayBillAddOper.equalsIgnoreCase("payBillDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyPayBillName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theEmployeeOper.equalsIgnoreCase("employeeDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyEmployeeName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theCommissionOper.equalsIgnoreCase("commissionsDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyCommisionsName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theRolodexOper.equalsIgnoreCase("rolodexDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyRolodexName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theUserOper.equalsIgnoreCase("userDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyUserName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theSettingOper.equalsIgnoreCase("settingsDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanySettingName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theReportOper.equalsIgnoreCase("reportDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyReportName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}if(theChartOper.equalsIgnoreCase("accountDelete")){
				aSysPrivilageID = itsUserService.getSysPrivilageID(aUserBean.getUserId(), aUserBean.getUsergroupId(), theCompanyAccountName);
				aSysprivilege.setSysPrivilegeId(aSysPrivilageID);
				itsUserService.deleteSystemPrivilage(aSysprivilege);
			}
		}catch (UserException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theSalesName:</b>"+theSalesName ,"UserListController",e,session,therequest);
		}
		return true;
	}
	
	@RequestMapping(value="/getSysPrivilagePermissions", method = RequestMethod.GET)
	public @ResponseBody List<Sysprivilege> getSysPrivilagePermissions(HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		List<Sysprivilege> aSysPriList = null;
		try{
			itsLogger.info("aUserBean===="+aUserBean);
			itsLogger.info("aUserBean.getUserId()===="+aUserBean.getUserId());
			aSysPriList = itsUserService.getSysPrivilageDetails(aUserBean.getUserId());
		}catch (UserException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>getSysPrivilagePermissions:</b>","UserListController",e,theSession,theRequest);
		}
		finally{
			aUserBean = null;
		}
		return aSysPriList;
	}
	
	@RequestMapping(value="/getSysPrivilageChk", method = RequestMethod.GET)
	public @ResponseBody List<Sysprivilege> getSysPrivilageChk(@RequestParam(value = "userLoginID", required = false) String theUserName, HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		List<Sysprivilege> aSysPriList = null;
		try{
			aSysPriList = itsUserService.getSysPrivilageInfo(theUserName, aUserBean.getUserId());
		}catch (UserException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theUserName:</b>"+theUserName ,"UserListController",e,theSession,theRequest);
		}
		return aSysPriList;
	}	
	
	/**
	 * @RequestMapping(value="/upload_avatar", method = RequestMethod.POST)
	 * @param theSettingID
	 * @param theFilePathName
	 * @param theSession
	 * @return
	 */
	/*public @ResponseBody TsUserSetting uploadImage(@RequestParam(value = "userSettingID", required = false) Integer theSettingID,
																											@RequestParam(value = "imagePath", required = false) String theFilePathName,
																											HttpSession theSession){
		TsUserSetting aUserSetting = new TsUserSetting();
		theFilePathName = "/home/likewise-open/SYSVINE/thulasi_ram/Pictures/015079ss4.jpg";
		File file = new File(theFilePathName);
		byte[] bFile = new byte[(int) file.length()];
		try{
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
   	     	fileInputStream.close();
   	     	aUserSetting.setCompanyLogo(bFile);
   	     	aUserSetting.setCompanyId(theSettingID);
   	     	itsUserService.uploadImage(aUserSetting);
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return aUserSetting;
	}*/
	
	/*Customer setting category update process*/
	@RequestMapping(value = "/customercategorySettings", method = RequestMethod.POST)
	public @ResponseBody String customercategorySettings (@RequestParam(value = "headerText", required = false) String theHeaderText,
																@RequestParam(value = "customerCategory1Desc", required = false) String customerCategory1Desc,
																@RequestParam(value = "customerCategory2Desc", required = false) String customerCategory2Desc,
																@RequestParam(value = "customerCategory3Desc", required = false) String customerCategory3Desc,
																@RequestParam(value = "customerCategory4Desc", required = false) String customerCategory4Desc,
																@RequestParam(value = "customerCategory5Desc", required = false) String customerCategory5Desc,
																@RequestParam(value = "customerCategory1Id", required = false) byte customerCategory1Id,
																@RequestParam(value = "customerCategory2Id", required = false) byte customerCategory2Id,
																@RequestParam(value = "customerCategory3Id", required = false) byte customerCategory3Id,
																@RequestParam(value = "customerCategory4Id", required = false) byte customerCategory4Id,
																@RequestParam(value = "customerCategory5Id", required = false) byte customerCategory5Id,
																HttpSession theSession, HttpServletResponse response) throws IOException {
		
		System.out.println("customerCategory1Desc"+customerCategory1Desc+"customerCategory2Desc"+customerCategory2Desc+"customerCategory3Desc"+customerCategory3Desc+"customerCategory4Desc"+customerCategory4Desc+"customerCategory5Desc"+customerCategory5Desc);
		System.out.println("customerCategory1Desc"+customerCategory1Id+"customerCategory2Desc"+customerCategory2Id+"customerCategory3Desc"+customerCategory3Id+"customerCategory4Desc"+customerCategory4Id+"customerCategory5Desc"+customerCategory5Id);
		Sysassignment sysassign=new Sysassignment();
		/*update Customer categories while changing the categories name in sysAssignment table*/
		sysassign.setCustomerCategory1(customerCategory1Desc);
		sysassign.setCustomerCategory2(customerCategory2Desc);
		sysassign.setCustomerCategory3(customerCategory3Desc);
		sysassign.setCustomerCategory4(customerCategory4Desc);
		sysassign.setCustomerCategory5(customerCategory5Desc);
		sysassign.setCustomerCategoryId1(customerCategory1Id);
		sysassign.setCustomerCategoryId2(customerCategory2Id);
		sysassign.setCustomerCategoryId3(customerCategory3Id);
		sysassign.setCustomerCategoryId4(customerCategory4Id);
		sysassign.setCustomerCategoryId5(customerCategory5Id);
		System.out.println("sysassign"+sysassign);
		boolean sysinfoupdate=sysservice.updateCustomerCategories(sysassign);
		System.out.println("sysinfoupdate"+sysinfoupdate);
		itsLogger.info("while changing Customer categories reflected in sysAssignment table");
		//return aUserSettingsList;
		return "success";
	}
	
	
	/*
	 * Created by:Velmurugan
	 * Created on: 15-9-2014
	 * Description:This job categories will update in jocategory table .*/
	@RequestMapping(value = "/jobcategorySettings", method = RequestMethod.POST)
	public @ResponseBody String jobcategorySettings (
																@RequestParam(value = "joCategory1Desc", required = false) String joCategory1Desc,
																@RequestParam(value = "joCategory2Desc", required = false) String joCategory2Desc,
																@RequestParam(value = "joCategory3Desc", required = false) String joCategory3Desc,
																@RequestParam(value = "joCategory4Desc", required = false) String joCategory4Desc,
																@RequestParam(value = "joCategory5Desc", required = false) String joCategory5Desc,
																@RequestParam(value = "joCategory6Desc", required = false) String joCategory6Desc,
																@RequestParam(value = "joCategory7Desc", required = false) String joCategory7Desc,
																@RequestParam(value = "joCategory1Id", required = false) int joCategory1Id,
																@RequestParam(value = "joCategory2Id", required = false) int joCategory2Id,
																@RequestParam(value = "joCategory3Id", required = false) int joCategory3Id,
																@RequestParam(value = "joCategory4Id", required = false) int joCategory4Id,
																@RequestParam(value = "joCategory5Id", required = false) int joCategory5Id,
																@RequestParam(value = "joCategory6Id", required = false) int joCategory6Id,
																@RequestParam(value = "joCategory7Id", required = false) int joCategory7Id,
																HttpSession theSession, HttpServletResponse response) throws IOException {
		
		
		/*update Job categories while changing the categories name in jocategory table*/
		 jocategory ajocategory=new jocategory();
		 ajocategory.setJoCategory1ID(joCategory1Id);
		 ajocategory.setJoCategory2ID(joCategory2Id);
		 ajocategory.setJoCategory3ID(joCategory3Id);
		 ajocategory.setJoCategory4ID(joCategory4Id);
		 ajocategory.setJoCategory5ID(joCategory5Id);
		 ajocategory.setJoCategory6ID(joCategory6Id);
		 ajocategory.setJoCategory7ID(joCategory7Id);
		 ajocategory.setCategory1desc(joCategory1Desc);
		 ajocategory.setCategory2desc(joCategory2Desc);
		 ajocategory.setCategory3desc(joCategory3Desc);
		 ajocategory.setCategory4desc(joCategory4Desc);
		 ajocategory.setCategory5desc(joCategory5Desc);
		 ajocategory.setCategory6desc(joCategory6Desc);
		 ajocategory.setCategory7desc(joCategory7Desc);
		boolean sysinfoupdate=sysservice.updateJobCategories(ajocategory);
		
		itsLogger.info("while changing job categories reflected in jocategory table");
		//return aUserSettingsList;
		return "success";
	}
	
	/*
	 * Created by:Velmurugan
	 * Created on: 17-9-2014
	 * Description:This job column quote defaults  will update in joquotecolumn table .*/
	@RequestMapping(value = "/jobColumnQuoteDefaults", method = RequestMethod.POST)
	public @ResponseBody String jobColumnQuoteDefaults (		@RequestParam(value = "pHead1ID", required = false) String pHead1ID,
																@RequestParam(value = "pHead2ID", required = false) String pHead2ID,
																@RequestParam(value = "pHead3ID", required = false) String pHead3ID,
																@RequestParam(value = "pHead4ID", required = false) String pHead4ID,
																@RequestParam(value = "pHead5ID", required = false) String pHead5ID,
																@RequestParam(value = "pHead6ID", required = false) String pHead6ID,
																@RequestParam(value = "pHead7ID", required = false) String pHead7ID,
																@RequestParam(value = "pHead8ID", required = false) String pHead8ID,
																@RequestParam(value = "joquotecolumn1Id", required = false) int joquotecolumn1Id,
																@RequestParam(value = "joquotecolumn2Id", required = false) int joquotecolumn2Id,
																@RequestParam(value = "joquotecolumn3Id", required = false) int joquotecolumn3Id,
																@RequestParam(value = "joquotecolumn4Id", required = false) int joquotecolumn4Id,
																@RequestParam(value = "joquotecolumn5Id", required = false) int joquotecolumn5Id,
																@RequestParam(value = "joquotecolumn6Id", required = false) int joquotecolumn6Id,
																@RequestParam(value = "joquotecolumn7Id", required = false) int joquotecolumn7Id,
																@RequestParam(value = "joquotecolumn8Id", required = false) int joquotecolumn8Id,
																@RequestParam(value = "pOrder1ID", required = false) int pOrder1ID,
																@RequestParam(value = "pOrder2ID", required = false) int pOrder2ID,
																@RequestParam(value = "pOrder3ID", required = false) int pOrder3ID,
																@RequestParam(value = "pOrder4ID", required = false) int pOrder4ID,
																@RequestParam(value = "pOrder5ID", required = false) int pOrder5ID,
																@RequestParam(value = "pOrder6ID", required = false) int pOrder6ID,
																@RequestParam(value = "pOrder7ID", required = false) int pOrder7ID,
																@RequestParam(value = "pOrder8ID", required = false) int pOrder8ID,
																HttpSession theSession, HttpServletResponse response) throws IOException {
		
		
		/*update Job categories while changing the categories name in jocategory table*/
		 Joquotecolumn theJoquotecolumn=new Joquotecolumn();
		 
		 theJoquotecolumn.setJoquotecolumn1Id(joquotecolumn1Id);
		 theJoquotecolumn.setColumn1label(pHead1ID);
		 theJoquotecolumn.setCoOrder1Id(pOrder1ID);
		 
		 theJoquotecolumn.setJoquotecolumn2Id(joquotecolumn2Id);
		 theJoquotecolumn.setColumn2label(pHead2ID);
		 theJoquotecolumn.setCoOrder2Id(pOrder2ID);
		 
		 theJoquotecolumn.setJoquotecolumn3Id(joquotecolumn3Id);
		 theJoquotecolumn.setColumn3label(pHead3ID);
		 theJoquotecolumn.setCoOrder3Id(pOrder3ID);
		 
		 theJoquotecolumn.setJoquotecolumn4Id(joquotecolumn4Id);
		 theJoquotecolumn.setColumn4label(pHead4ID);
		 theJoquotecolumn.setCoOrder4Id(pOrder4ID);
		 
		 theJoquotecolumn.setJoquotecolumn5Id(joquotecolumn5Id);
		 theJoquotecolumn.setColumn5label(pHead5ID);
		 theJoquotecolumn.setCoOrder5Id(pOrder5ID);
		 
		 theJoquotecolumn.setJoquotecolumn6Id(joquotecolumn6Id);
		 theJoquotecolumn.setColumn6label(pHead6ID);
		 theJoquotecolumn.setCoOrder6Id(pOrder6ID);
		 
		 theJoquotecolumn.setJoquotecolumn7Id(joquotecolumn7Id);
		 theJoquotecolumn.setColumn7label(pHead7ID);
		 theJoquotecolumn.setCoOrder7Id(pOrder7ID);
		 
		 theJoquotecolumn.setJoquotecolumn8Id(joquotecolumn8Id);
		 theJoquotecolumn.setColumn8label(pHead8ID);
		 theJoquotecolumn.setCoOrder8Id(pOrder8ID);
		 
		boolean sysinfoupdate=sysservice.updatejobColumnQuoteDefaults(theJoquotecolumn);
		
		itsLogger.info("while changing job categories reflected in jocategory table");
		//return aUserSettingsList;
		return "success";
	}
	//savePriceTier
	@RequestMapping(value = "/savePriceTier", method = RequestMethod.POST)
	public @ResponseBody String savePriceTier (@RequestParam(value = "prPriceLevel0", required = false) String prPriceLevel0,
																@RequestParam(value = "prPriceLevel1", required = false) String prPriceLevel1,
																@RequestParam(value = "prPriceLevel2", required = false) String prPriceLevel2,
																@RequestParam(value = "prPriceLevel3", required = false) String prPriceLevel3,
																@RequestParam(value = "prPriceLevel4", required = false) String prPriceLevel4,
																@RequestParam(value = "prPriceLevel5", required = false) String prPriceLevel5,
																HttpSession theSession, HttpServletResponse response) throws IOException {
		
		
		
		Sysinfo sysassign=new Sysinfo();
		/*update Customer categories while changing the categories name in sysAssignment table*/
		sysassign.setPrPriceLevel0(prPriceLevel0);
		sysassign.setPrPriceLevel1(prPriceLevel1);
		sysassign.setPrPriceLevel2(prPriceLevel2);
		sysassign.setPrPriceLevel3(prPriceLevel3);
		sysassign.setPrPriceLevel4(prPriceLevel4);
		sysassign.setPrPriceLevel5(prPriceLevel5);
		sysservice.updateTierPriceLevel(sysassign);
		return "success";
	}
	
	@RequestMapping(value = "/updatesysprivilage", method = RequestMethod.POST)
	public @ResponseBody String updatesysprivilage (@RequestParam(value = "checkedornot", required = false) Boolean checkedornot,
			@RequestParam(value = "accessProcedurename", required = false) String accessProcedurename,
			@RequestParam(value = "userGroupID", required = false) Integer userGroupID,
			@RequestParam(value = "UserLoginID", required = false) Integer UserLoginID,
			HttpSession theSession, HttpServletResponse response) throws IOException {
		
		Sysprivilege aSysprivilege=new Sysprivilege();
		Integer privilagevalue=-1;
		if(checkedornot){
			privilagevalue=1;
		}
		aSysprivilege.setAccessProcedureId(AccessProcedureConstant.getConstantSysvariableId(accessProcedurename));
		aSysprivilege.setPrivilegeValue(privilagevalue);
		if(userGroupID!=null){
		aSysprivilege.setUserGroupId(userGroupID);
		}
		if(UserLoginID!=null){
			aSysprivilege.setUserLoginId(UserLoginID);
		}
		sysservice.updatesysprivilage(aSysprivilege);
		return "success";
	}
	@RequestMapping(value="/GetGroupDefaultPermission", method = RequestMethod.GET)
	public @ResponseBody List<Sysprivilege> GetGroupDefaultPermission(
			@RequestParam(value = "userGroupID", required = false) Integer userGroupID, 
			@RequestParam(value = "UserLoginID", required = false) Integer UserLoginID,
			HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		List<Sysprivilege> aSysPriList = null;
		try{
			Sysprivilege aSysprivilege=new Sysprivilege();
			if(UserLoginID!=null){
				aSysprivilege.setUserLoginId(UserLoginID);
			}
			if(userGroupID!=null){
				aSysprivilege.setUserGroupId(userGroupID);
			}
			
			
			aSysPriList = sysservice.getGroupdefaultSysPrivileageLst(aSysprivilege);
			
		}catch (UserException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>userGroupID:</b>"+userGroupID ,"UserListController",e,theSession,theRequest);
		}
		return aSysPriList;
	}	
	
	@RequestMapping(value = "/getSysvariableStatusBasedOnVariableName", method = RequestMethod.POST)
	public @ResponseBody  ArrayList<Sysvariable> getSysvariableStatusBasedOnVariableName (@RequestParam(value = "VariableName", required = false) String VariableName,
													HttpServletResponse response,HttpSession session) throws IOException, UserException {
		List<String> addlist=new ArrayList<String>();
		addlist.add(VariableName);
		ArrayList<Sysvariable> sysvariablelist= itsUserService.getInventorySettingsDetails(addlist);
		return sysvariablelist;
	}
	
	
	@RequestMapping(value = "/CreateTpUsage", method = RequestMethod.POST)
	public @ResponseBody boolean CreateTpUsage( 
			@RequestParam(value = "screen", required = false) String screen, 
			@RequestParam(value = "action", required = false) String action, 
			@RequestParam(value = "logLevel", required = false) String logLevel, 
			@RequestParam(value = "description", required = false) String description,HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException {
		TpUsage aTpusage=null;UserBean aUserBean=null;
		TsUserSetting objtsusersettings=null;
		try{
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
			if(aUserBean!=null){
			aTpusage=new TpUsage();
			aTpusage.setScreen(screen);
			aTpusage.setAction(action);
			aTpusage.setLogLevel(logLevel);
			aTpusage.setDescription(description);
			aTpusage.setUserID(aUserBean.getUserId());
			aTpusage.setDatetime(new Date());
			if(objtsusersettings.getItslogYN()==1){
		    itsUserService.createTpUsage(aTpusage);
			}
			}
		}catch(Exception e){
			e.printStackTrace();
			sendTransactionException("<b>screen:</b>"+screen ,"UserListController",e,session,therequest);
		}finally{
			 aTpusage=null;
			 aUserBean=null;
			 objtsusersettings = null;
		}
		return true;
	}
	
	public void sendTransactionException(String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		UserBean aUserBean=null;
		TsUserSetting objtsusersettings=null;
		try{
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
		 StringWriter errors = new StringWriter();
		 e.printStackTrace(new PrintWriter(errors));
		if(objtsusersettings.getItsmailYN()==1){
		Transactionmonitor transObj =new Transactionmonitor();
		 SendQuoteMail sendMail = new SendQuoteMail();
		 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
		 transObj.setTrackingId(trackingID);
		 transObj.setTimetotriggerd(new Date());
		 transObj.setJobStatus(jobstatus);
		 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
		 transObj.setDescription("Message :: " + errors.toString());
		 sendMail.sendTransactionInfo(transObj,therequest);
		}
		
		if(objtsusersettings.getItslogYN()==1){
			TpUsage aTpusage=new TpUsage(new Date(), jobstatus,trackingID,"Error",aUserBean.getUserId(),"Message :: " + errors.toString());
			userService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	
	@RequestMapping(value = "/updatechangepassword", method = RequestMethod.GET)
	public @ResponseBody boolean updatechangepassword(@RequestParam(value = "userloginid", required = false) Integer theLoginID, 
			@RequestParam(value = "newPswd", required = false) String Newpswd,
			HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		boolean returnvalue =  itsUserService.updatechangepassword(theLoginID,Newpswd);
		return true;
	}
	
	@RequestMapping(value = "/updateVendorProductDetails", method = RequestMethod.POST)
	public @ResponseBody Integer updateVendorProductDetails(@RequestParam(value = "userloginid", required = false) Integer theLoginID, 
			@RequestParam(value = "vendorProductID", required = false) Integer vendorProductID,
			@RequestParam(value = "vendorProductDeptID", required = false) Integer vendorProductDeptID,
			@RequestParam(value = "companyID", required = false) Integer companyID,
			
			HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		TsUserSetting aTsUserSetting = new TsUserSetting();
		aTsUserSetting.setCompanyId(companyID);
		aTsUserSetting.setVendorProductID(vendorProductID);
		aTsUserSetting.setVendorProductDeptID(vendorProductDeptID);		
		Integer returnvalue =  itsUserService.saveVendorProductDetails(aTsUserSetting);
		return returnvalue;
	}
	

	@RequestMapping(value="/getVendorProductDetails", method = RequestMethod.POST)
	public @ResponseBody Integer getVendorProductDetails(HttpSession theSession,HttpServletRequest theRequest) throws IOException, MessagingException{
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		TsUserSetting aUserLoginSetting = null;
		Integer ProductID = 0;
		try{
			if (aUserBean != null) {
				aUserLoginSetting = itsUserService.getSingleUserSettingsDetails(1);
				ProductID = aUserLoginSetting.getVendorProductID();
				itsLogger.info("Product ID::"+ProductID);
			}
		}catch (UserException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theUserName:</b>"+"","UserListController",e,theSession,theRequest);
		}
		return ProductID;
	}
	
	@RequestMapping(value = "/getMaxInvoiceNumber", method = RequestMethod.GET)
	public  @ResponseBody String getMaxInvoiceNumber(@RequestParam(value = "userID", required = false) Integer userID, HttpServletRequest request,HttpSession session, HttpServletResponse response)
			throws IOException, JobException {
		String MaxInvoiceNumber = "";
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (aUserBean != null) {
				MaxInvoiceNumber =  jobService.getMaxInvoiceNumber();
			}
		} catch (JobException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return MaxInvoiceNumber;
	}
}
