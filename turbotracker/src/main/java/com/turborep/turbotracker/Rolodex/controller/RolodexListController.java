package com.turborep.turbotracker.Rolodex.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.employee.dao.RxMasterCategoryView;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.dao.Rxmastercategory1;
import com.turborep.turbotracker.employee.dao.Rxmastercategory2;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.exception.VendorException;

@Controller
@RequestMapping("/rolodex")
public class RolodexListController {
	
	protected static Logger itsLogger = Logger.getLogger(RolodexListController.class);
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="sysService")
	private SysService sysservice;
	
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	/*
	 * @author:Praveenkumar
	 * LastModified: 08/18/2014
	 * Reason: Enable category search option
	 * Jsp file: rolodexlist.jsp 
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody CustomResponse getAll( @RequestParam(value="page", required=false) Integer thePage,
																						@RequestParam(value="rows", required=false) Integer theRows,
																						@RequestParam(value="sidx", required=false) String theSidx,
																						@RequestParam(value="sord", required=false) String theSord,
																						@RequestParam(value="category", required=false) String theCategory,HttpServletRequest theRequest,
																						HttpServletResponse response, HttpSession session) throws IOException, MessagingException, VendorException {
		itsLogger.debug("Received request to get all Rolodex details");
		String aRolodexID = "rolodex";
		String aRolodexWhere = "";
		String query=getrolodexListQuery(aRolodexWhere);
		//int aTotalCount = itsRolodexService.getRecordCount(aRolodexWhere);
		int aTotalCount =companyService.getCount(query);
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			aUserLogin = new TsUserLogin();
			aUserLogin.setRolodexperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aRolodexID);
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			// Retrieve all users from the service
			List<?> aRolodexList = itsRolodexService.getAll(aFrom, theRows,theCategory);
			// Initialize our custom user response wrapper
			aResponse = new CustomResponse();
			// Assign the result from the service to this response
			aResponse.setRows(aRolodexList);
			// Assign the total number of records found. This is used for paging
			aResponse.setRecords( String.valueOf(aRolodexList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
		} catch (UserException e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getCause().getMessage());
			sendTransactionException("<b>theCategory:</b>"+theCategory,"RolodexListController",e,session,theRequest);
		} catch (Exception e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			sendTransactionException("<b>theCategory:</b>"+theCategory,"RolodexListController",e,session,theRequest);
		}

		return aResponse;
	}
	
	
	@RequestMapping(value = "/getRolodexCategory", method = RequestMethod.GET)
	public @ResponseBody Sysinfo getRolodexCategory(@RequestParam(value = "userInfoID", required = false) Integer userInfoID,
			HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException, MessagingException {
		Sysinfo aSysinfo = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			aSysinfo=sysservice.getRoldexCategories(1);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>userInfoID:</b>"+userInfoID,"RolodexListController",e,session,request);
		}
		return aSysinfo;
		
	}
	
	
	@RequestMapping(value="/addNewRolodexList",method = RequestMethod.GET)
	public @ResponseBody Rxmaster addRolodex(@RequestParam(value = "rolodexName", required = false) String theRolodexName, 
																						@RequestParam(value = "rolodexAddress1", required = false) String theAddress1,
																						 @RequestParam(value = "rolodexAddress2", required = false) String theAddress2, 
																						 @RequestParam(value = "rolodexState", required = false) String theState,
																						 @RequestParam(value = "rolodexCity", required = false) String theCity,
																						 @RequestParam(value = "rolodexZip", required = false) String thePinCode, 
																						 @RequestParam(value = "USPhoneNumber1", required = false) String thePhone1,
																						 @RequestParam(value = "USPhoneNumber2", required = false) String thePhone2, 
																						 @RequestParam(value = "fax", required = false) String theFax, 
																						 @RequestParam(value = "customerCheckName", required = false) boolean theIsCustomer,
																						 @RequestParam(value = "vendorCheckName", required = false) boolean theIsVendor,
																						 @RequestParam(value = "employeeCheckName", required = false) boolean theIsEmployee,
																						 @RequestParam(value = "customerValue", required = false) boolean IsCustomer,
																						 @RequestParam(value = "vendorValue", required = false) boolean IsVendor,
																						 @RequestParam(value = "employeeValue", required = false) boolean IsEmployee,
																						 @RequestParam(value = "architectValue", required = false) boolean IsArchitect,
																						 @RequestParam(value = "engineerValue", required = false) boolean IsEngineer,
																						 @RequestParam(value = "gcValue", required = false) boolean IsGC,
																						 @RequestParam(value = "category4CheckID", required = false) boolean category4CheckID,
																						 @RequestParam(value = "category5CheckID", required = false) boolean category5CheckID,
																						 @RequestParam(value = "category6CheckID", required = false) boolean category6CheckID,
																						 @RequestParam(value = "category7CheckID", required = false) boolean category7CheckID,
																						 @RequestParam(value = "category8CheckID", required = false) boolean category8CheckID,
																						 @RequestParam(value = "mailAddress", required = false) boolean themailID,
																						 @RequestParam(value = "shipAddress", required = false) boolean theshipID,
																						 @RequestParam(value = "defaultAddress", required = false) boolean thedefaultAddress,
																					 	 @RequestParam(value = "remitCheckbox", required = false) boolean remitto,
																						 HttpSession session,HttpServletRequest request) throws IOException, MessagingException {
		Rxmaster aRolodex = new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		Rxmaster aNewRolodex = null;
		UserBean aUserBean;			
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try{
				aRolodex.setName(theRolodexName);
				String afirstName = "";
				boolean inActive = false;
				boolean IsStreet = false;
				theIsVendor = true;
				aRolodex.setFirstName(afirstName);
				String asearchName = theRolodexName.replace(" ", "");
				if(asearchName.length() > 10){
					String asearchText = asearchName.substring(0, 10);
					aRolodex.setSearchName(asearchText.toUpperCase());
				}else{
					aRolodex.setSearchName(asearchName.toUpperCase());
				}
				aRolodex.setInActive(inActive);
				aRolodex.setPhone1(thePhone1);
				aRolodex.setPhone2(thePhone2);
				aRolodex.setFax(theFax);
				aRolodex.setIsCustomer(IsCustomer);
				aRolodex.setIsVendor(IsVendor);
				aRolodex.setIsEmployee(IsEmployee);
				aRolodex.setIsCategory1(IsArchitect);
				aRolodex.setIsCategory2(IsEngineer);
				aRolodex.setIsCategory3(IsGC);
				aRolodex.setIsCategory4(category4CheckID);
				aRolodex.setIsCategory5(category5CheckID);
				aRolodex.setIsCategory6(category6CheckID);
				aRolodex.setIsCategory7(category7CheckID);
				aRolodex.setIsCategory8(category8CheckID);
				
				aRxaddress.setName(theRolodexName);
				if(theAddress1 != null && !theAddress1.equalsIgnoreCase("")){
					String aAddress1 = theAddress1.trim();
					aRxaddress.setAddress1(aAddress1);
				}
				if(theAddress2 != null && !theAddress2.equalsIgnoreCase("")){
					String aAddress2 = theAddress2.trim();
					aRxaddress.setAddress2(aAddress2);
				}
				if(theCity != null && !theCity.equalsIgnoreCase("")){
					String aCity = theCity.trim();
					aRxaddress.setCity(aCity);
				}
				if(theState != null && !theState.equalsIgnoreCase("")){
					String aState = theState.trim();
					aRxaddress.setState(aState);
				}
				
				// Is Mailing Address
				aRxaddress.setIsMailing(themailID);
				aRxaddress.setIsBillTo(themailID);
				// Is Shipto Address
				aRxaddress.setIsShipTo(theshipID);
				// Is Default Address
				aRxaddress.setIsDefault(thedefaultAddress);
				aRxaddress.setIsRemitTo(remitto);
				aRxaddress.setZip(thePinCode);
				aRxaddress.setInActive(inActive);
				aRxaddress.setIsStreet(IsStreet);
				aRxaddress.setOtherBillTo(2);
				aRxaddress.setOtherShipTo(3);
				aRxaddress.setPhone1(thePhone1);
				aRxaddress.setPhone2(thePhone2);
				aRxaddress.setFax(theFax);
					
				aNewRolodex = itsRolodexService.addNewRolodex(aRolodex, aRxaddress,aUserBean.getUserId());
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexName:</b>"+theRolodexName,"RolodexListController",e,session,request);
		}
		return aNewRolodex;
	}
	//This is old code 
	/*@RequestMapping(value = "/NewEngineerAssignee", method = RequestMethod.GET)
	public @ResponseBody Cumaster createNewEngineerAsignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel) {
		Cumaster acuMaster = new Cumaster();
		try {
			acuMaster.setPorequired(false);
			acuMaster.setCreditHold(false);
			acuMaster.setCuAssignmentId0(theCuAssignmentID0);
			acuMaster.setCuAssignmentId1(theCuAssignmentID1);
			acuMaster.setCuAssignmentId2(theCuAssignmentID2);
			acuMaster.setCuAssignmentId3(theCuAssignmentID3);
			acuMaster.setCuAssignmentId4(theCuAssignmentID4);
			acuMaster.setCuMasterId(theRolodexId);
			itsRolodexService.addNewAssignee(acuMaster);
			itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return acuMaster;
	}
	*/
	/*
	 * updated by:Velmurugan
	 * updated on:12-9-2014
	 * Description: As per turborep application data have to save in rxmastercategory2 table*/
	@RequestMapping(value = "/NewEngineerAssignee", method = RequestMethod.GET)
	public @ResponseBody Rxmastercategory2 createNewEngineerAsignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel,HttpServletRequest request) throws IOException, MessagingException {
		Rxmastercategory2 aRxmastercategory2 = new Rxmastercategory2();
		UserBean aUserBean;			
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aRxmastercategory2.setCuAssignmentId0(theCuAssignmentID0);
			aRxmastercategory2.setCuAssignmentId1(theCuAssignmentID1);
			aRxmastercategory2.setCuAssignmentId2(theCuAssignmentID2);
			aRxmastercategory2.setCuAssignmentId3(theCuAssignmentID3);
			aRxmastercategory2.setCuAssignmentId4(theCuAssignmentID4);
			aRxmastercategory2.setRxMasterCategory2id(theRolodexId);
			itsRolodexService.addEngineerTitles(aRxmastercategory2);
			//itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexListController",e,theSession,request);
		}
		return aRxmastercategory2;
	}
	
	/*@RequestMapping(value = "/updateEngineerAssignee", method = RequestMethod.GET)
	public @ResponseBody Cumaster updateEngineerAsignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel) {
		Cumaster acuMaster = new Cumaster();
		try {
			acuMaster.setPorequired(false);
			acuMaster.setCreditHold(false);
			acuMaster.setCuAssignmentId0(theCuAssignmentID0);
			acuMaster.setCuAssignmentId1(theCuAssignmentID1);
			acuMaster.setCuAssignmentId2(theCuAssignmentID2);
			acuMaster.setCuAssignmentId3(theCuAssignmentID3);
			acuMaster.setCuAssignmentId4(theCuAssignmentID4);
			acuMaster.setCuMasterId(theRolodexId);
			itsRolodexService.updateAssignee(acuMaster);
			itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return acuMaster;
	}*/
	
	@RequestMapping(value = "/updateEngineerAssignee", method = RequestMethod.GET)
	public @ResponseBody Rxmastercategory2 updateEngineerAsignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel,HttpServletRequest request) throws IOException, MessagingException {
		Rxmastercategory2 aRxmastercategory2 = new Rxmastercategory2();
		UserBean aUserBean;			
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aRxmastercategory2.setCuAssignmentId0(theCuAssignmentID0);
			aRxmastercategory2.setCuAssignmentId1(theCuAssignmentID1);
			aRxmastercategory2.setCuAssignmentId2(theCuAssignmentID2);
			aRxmastercategory2.setCuAssignmentId3(theCuAssignmentID3);
			aRxmastercategory2.setCuAssignmentId4(theCuAssignmentID4);
			aRxmastercategory2.setRxMasterCategory2id(theRolodexId);
			itsLogger.info(theCuAssignmentID0+" "+theCuAssignmentID1+" "+theCuAssignmentID2+" "+theCuAssignmentID3+" "+theCuAssignmentID4);
			itsRolodexService.updateEngineerTitles(aRxmastercategory2);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexListController",e,theSession,request);
		}
		return aRxmastercategory2;
	}
	
	/*
	 * created by:Velmurugan
	 * created on:12-9-2014
	 * Description: As per turborep application data have to save in rxmastercategory1 table*/
	@RequestMapping(value = "/NewArchitectAssignee", method = RequestMethod.GET)
	public @ResponseBody Rxmastercategory1 createNewArchitectAssignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel,HttpServletRequest request) throws IOException, MessagingException {
		Rxmastercategory1 aRxmastercategory1 = new Rxmastercategory1();
		UserBean aUserBean= (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aRxmastercategory1.setCuAssignmentId0(theCuAssignmentID0);
			aRxmastercategory1.setCuAssignmentId1(theCuAssignmentID1);
			aRxmastercategory1.setCuAssignmentId2(theCuAssignmentID2);
			aRxmastercategory1.setCuAssignmentId3(theCuAssignmentID3);
			aRxmastercategory1.setCuAssignmentId4(theCuAssignmentID4);
			aRxmastercategory1.setRxMasterCategory1id(theRolodexId);
			itsRolodexService.addArchitectTitles(aRxmastercategory1);
			//itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexListController",e,theSession,request);
		}
		return aRxmastercategory1;
	}
	
	/*
	 * created by:Velmurugan
	 * created on:12-9-2014
	 * Description: As per turborep application data have to update in rxmastercategory1 table*/
	@RequestMapping(value = "/updateArchitectAssignee", method = RequestMethod.GET)
	public @ResponseBody Rxmastercategory1 updateArchitectAssignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel,HttpServletRequest request) throws IOException, MessagingException {
		Rxmastercategory1 aRxmastercategory1 = new Rxmastercategory1();
		UserBean aUserBean= (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aRxmastercategory1.setCuAssignmentId0(theCuAssignmentID0);
			aRxmastercategory1.setCuAssignmentId1(theCuAssignmentID1);
			aRxmastercategory1.setCuAssignmentId2(theCuAssignmentID2);
			aRxmastercategory1.setCuAssignmentId3(theCuAssignmentID3);
			aRxmastercategory1.setCuAssignmentId4(theCuAssignmentID4);
			aRxmastercategory1.setRxMasterCategory1id(theRolodexId);
			itsRolodexService.updateArchitectTitles(aRxmastercategory1);
			//itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexListController",e,theSession,request);
		}
		return aRxmastercategory1;
	}
	
	/*
	 * created by:Velmurugan
	 * created on:12-9-2014
	 * Description:  data have to save in rxmastercategoryview table*/
	@RequestMapping(value = "/NewViewAssignee", method = RequestMethod.GET)
	public @ResponseBody RxMasterCategoryView createNewViewAssignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel,HttpServletRequest request) throws IOException, MessagingException {
		RxMasterCategoryView aRxmastercategoryview = new RxMasterCategoryView();
		UserBean aUserBean= (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aRxmastercategoryview.setCuAssignmentId0(theCuAssignmentID0);
			aRxmastercategoryview.setCuAssignmentId1(theCuAssignmentID1);
			aRxmastercategoryview.setCuAssignmentId2(theCuAssignmentID2);
			aRxmastercategoryview.setCuAssignmentId3(theCuAssignmentID3);
			aRxmastercategoryview.setCuAssignmentId4(theCuAssignmentID4);
			aRxmastercategoryview.setRxMasterCategoryViewid(theRolodexId);
			itsRolodexService.addViewsTitles(aRxmastercategoryview);
			//itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexListController",e,theSession,request);
		}
		return aRxmastercategoryview;
	}
	
	/*
	 * created by:Velmurugan
	 * created on:12-9-2014
	 * Description:data have to update in RxMasterCategoryView table*/
	@RequestMapping(value = "/updateViewAssignee", method = RequestMethod.GET)
	public @ResponseBody RxMasterCategoryView updateViewAssignee(@RequestParam(value="rolodexId", required=false) Integer theRolodexId,
								@RequestParam(value ="custName",required= false) String theName,
								@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
								@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
								HttpSession theSession, ModelMap theModel,HttpServletRequest request) throws IOException, MessagingException {
		RxMasterCategoryView aRxmastercategoryview = new RxMasterCategoryView();
		UserBean aUserBean= (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			aRxmastercategoryview.setCuAssignmentId0(theCuAssignmentID0);
			aRxmastercategoryview.setCuAssignmentId1(theCuAssignmentID1);
			aRxmastercategoryview.setCuAssignmentId2(theCuAssignmentID2);
			aRxmastercategoryview.setCuAssignmentId3(theCuAssignmentID3);
			aRxmastercategoryview.setCuAssignmentId4(theCuAssignmentID4);
			aRxmastercategoryview.setRxMasterCategoryViewid(theRolodexId);
			itsRolodexService.updateViewTitles(aRxmastercategoryview);
			//itsRolodexService.updateCustomerName(theName,theRolodexId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexListController",e,theSession,request);
		}
		return aRxmastercategoryview;
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
			itsUserService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	
	public String getrolodexListQuery(String category){
		String condition="";
		if(category!=null){
			if(category.equalsIgnoreCase("9")){
				condition="AND IsCustomer=1";
			}else if(category.equalsIgnoreCase("10")){
				condition="AND IsVendor=1";
			}else if(category.equalsIgnoreCase("11")){
				condition="AND IsEmployee=1";
			}else if(category.equalsIgnoreCase("1")){
				condition="AND IsCategory1=1";
			}else if(category.equalsIgnoreCase("2")){
				condition="AND IsCategory2=1";
			}else if(category.equalsIgnoreCase("3")){
				condition="AND IsCategory3=1";
			}else if(category.equalsIgnoreCase("4")){
				condition="AND IsCategory4=1";
			}else if(category.equalsIgnoreCase("5")){
				condition="AND IsCategory5=1";
			}else if(category.equalsIgnoreCase("6")){
				condition="AND IsCategory6=1";
			}else if(category.equalsIgnoreCase("7")){
				condition="AND IsCategory7=1";
			}else if(category.equalsIgnoreCase("8")){
				condition="AND IsCategory8=1";
			}
		}
		String aStr = "SELECT rxMaster.rxMasterID " +
								"FROM rxMaster " +
								"JOIN rxAddress on rxMaster.rxMasterID = rxAddress.rxMasterID " +
								"WHERE rxMaster.name IS NOT NULL AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' "+condition+
								" Group By rxMaster.rxMasterID ORDER BY rxMaster.name ";
			
		return aStr;	
		
	}
}