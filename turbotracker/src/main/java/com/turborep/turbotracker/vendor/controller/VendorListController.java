package com.turborep.turbotracker.vendor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.banking.dao.Momultipleaccount;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebillpay;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.dao.VendorBillPayBean;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
@RequestMapping("/vendorscontroller")
public class VendorListController {
	
	Logger itsLogger = Logger.getLogger(VendorListController.class);
	
	@Resource(name="vendorService")
	private VendorServiceInterface itsVendorService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="inventoryService")
	private InventoryService itsinventoryService;
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAll(@RequestParam(value="page", required=false) Integer thePage,
													@RequestParam(value="rows", required=false) Integer theRows,
													@RequestParam(value="sidx", required=false) String theSidx,
													@RequestParam(value="sord", required=false) String theSord,
													HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
		itsLogger.debug("Received request to get all Vendor List");
		String aVendorID = "vendor";
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		StringBuilder aVendorSelectQry = null;
		try {
			aVendorSelectQry =new StringBuilder("SELECT rxMaster.rxMasterID, rxMaster.name, rxMaster.phone1, rxAddress.city, rxAddress.state, rxAddress.address1 ")
			.append("FROM rxMaster JOIN rxAddress on rxMaster.rxMasterID = rxAddress.rxMasterID ")
			.append("WHERE rxMaster.isVendor = 1 GROUP BY rxMaster.rxMasterID ORDER BY rxMaster.name ");
			int aTotalCount = itsVendorService.getCount(aVendorSelectQry.toString());
			aUserLogin = new TsUserLogin();
			aUserLogin.setVendorperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aVendorID);
			int aFrom, aTo;
			if(theRows==null){
				theRows=50;
			}
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			// Retrieve all users from the service
			List<?> aJobs = itsVendorService.getAll(aFrom, aTo);
			// Initialize our custom user response wrapper
			aResponse = new CustomResponse();
			// Assign the result from the service to this response
			aResponse.setRows(aJobs);
			// Assign the total number of records found. This is used for paging
			aResponse.setRecords( String.valueOf(aJobs.size()) );
			aResponse.setPage( thePage );
			//response.setTotal(aBigInt.intValue());
			aResponse.setTotal((int) Math.ceil((double)aTotalCount / (double) theRows));
		} catch (UserException e) {
			itsLogger.error(e.getCause().getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getCause().getMessage());
			sendTransactionException( "","VendorListController",e,theSession,theRequest);
		} catch (VendorException e) {
			itsLogger.error(e.getCause().getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
			sendTransactionException( "","VendorListController",e,theSession,theRequest);
		} catch (Exception e) {
			itsLogger.error(e.getCause().getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
			sendTransactionException( "","VendorListController",e,theSession,theRequest);
		}finally{
			aVendorSelectQry=null;
		} 
		
		// Return the response
		// Spring will automatically convert our CustomResponse as JSON object. 
		// This is triggered by the @ResponseBody annotation. 
		// It knows this because the JqGrid has set the headers to accept JSON format when it made a request
		// Spring by default uses Jackson to convert the object to JSON
		
		return aResponse;
	}
	
	@RequestMapping(value="/vendorcontact",method = RequestMethod.GET)
	public @ResponseBody CustomResponse employeeContact(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
		itsLogger.debug("employeeContact");
		Rxcontact aRxcontact = new Rxcontact();
		aRxcontact.setRxMasterId(theRolodexId);
		ArrayList<Rxcontact> aRxcontactsList = null;
		try {
			aRxcontactsList = (ArrayList<Rxcontact>) itsVendorService.getVendorContactDetails(aRxcontact);
		} catch (VendorException e) {
			itsLogger.error(e.getCause().getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
			sendTransactionException( theRolodexId+"","VendorListController",e,theSession,theRequest);
		}
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aRxcontactsList);
		return aResponse;
	}

	@RequestMapping(value="/vendoraddress",method = RequestMethod.GET)
	public @ResponseBody CustomResponse customerAddress(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
		itsLogger.debug("customerAddress");
		Rxaddress aRxaddress = new Rxaddress();
		aRxaddress.setRxMasterId(theRolodexId);
		ArrayList<Rxaddress> aRxaddressList = null;
		try {
			aRxaddressList = (ArrayList<Rxaddress>) itsVendorService.getVendorAddressDetails(aRxaddress);
		} catch (VendorException e) {
			itsLogger.error(e.getCause().getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
			sendTransactionException( theRolodexId+"","VendorListController",e,theSession,theRequest);
		}
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aRxaddressList);
		return aResponse;
	}
	
	@RequestMapping(value="/addNewVendorList",method = RequestMethod.GET)
	public @ResponseBody Rxmaster addCustomer(@RequestParam(value = "VendorName", required = false) String theVendorName, 
																						@RequestParam(value = "address1Name", required = false) String theAddress1,
																						@RequestParam(value = "address2Name", required = false) String theAddress2, 
																						@RequestParam(value = "stateCodeName", required = false) String theState,
																						@RequestParam(value = "cityNameListName", required = false) String theCity,
																						@RequestParam(value = "pinCodeName", required = false) String thePinCode, 
																						@RequestParam(value = "USPhoneNumber", required = false) String thePhone1,
																						@RequestParam(value = "USPhone_Number", required = false) String thePhone2, 
																						@RequestParam(value = "fax", required = false) String theFax, 
																						@RequestParam(value = "vendorCheckName", required = false) boolean theIsVendor,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
		Rxmaster aVendor = new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		Rxmaster aNewVendor = null;
		try{
			aVendor.setName(theVendorName);
			String afirstName = "";
			boolean inActive = false;
			boolean IsStreet = false;
			boolean IsMailing = false;
			boolean IsBillTo = true;
			boolean IsShipTo = true;
			theIsVendor = true;
			aVendor.setFirstName(afirstName);
			String asearchName = theVendorName.replace(" ", "");
			if(asearchName.length() > 10){
				String asearchText = asearchName.substring(0, 10);
				aVendor.setSearchName(asearchText.toUpperCase());
			}else{
				aVendor.setSearchName(asearchName.toUpperCase());
			}
			aVendor.setInActive(inActive);
			aVendor.setPhone1(thePhone1);
			aVendor.setPhone2(thePhone2);
			aVendor.setFax(theFax);
			aVendor.setIsVendor(theIsVendor);
			aRxaddress.setName(theVendorName);
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
			aRxaddress.setZip(thePinCode);
			aRxaddress.setInActive(inActive);
			aRxaddress.setIsMailing(IsMailing);
			aRxaddress.setIsShipTo(IsShipTo);
			aRxaddress.setIsStreet(IsStreet);
			aRxaddress.setIsBillTo(IsBillTo);
			aRxaddress.setOtherBillTo(2);
			aRxaddress.setOtherShipTo(3);
			aRxaddress.setIsDefault(false);
			aNewVendor = itsVendorService.addNewVendor(aVendor, aRxaddress);
			if(theVendorName.length() > 20){
				String asearchText = theVendorName.substring(0, 19);
				aNewVendor.setName(asearchText);
			}else{
				aNewVendor.setName(theVendorName);
			}
			itsVendorService.addnewFactory(aNewVendor);
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException( theVendorName+"","VendorListController",e,theSession,theRequest);
		}
		return aNewVendor;
	}
	
	@RequestMapping(value="/vendorbills_data",method = RequestMethod.GET)
	public @ResponseBody CustomResponse vendorBills(@RequestParam(value = "manufacturerID", required = false) String theRxMasterID, 
			@RequestParam(value = "displayType", required = false) String displayType, 
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException, IOException, MessagingException {
		
		String[] strArray=displayType.split(":");
		strArray[1].trim();
		
		System.out.println("---->>>>"+theSidx);
		
		System.out.println(" theRxMasterID = "+theRxMasterID+" || page = "+thePage+" || rows = "+theRows+" || sidx = "+theSidx+" || sord = "+theSord+" || DisplayType="+strArray[1].trim());
		
		
		Integer aRxMasterID = null;
		if(theRxMasterID != ""){
			aRxMasterID = Integer.valueOf(theRxMasterID);
		}
		ArrayList<VendorBillPayBean> aVendorBillsList = null;
		try {
			aVendorBillsList = (ArrayList<VendorBillPayBean>) itsVendorService.getVendorBillsList(aRxMasterID, thePage, theRows, theSidx, theSord,strArray[1].trim());
		} catch (VendorException excep) {
			itsLogger.error(excep.getMessage(), excep);
			theResponse.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			sendTransactionException( theRxMasterID+"","VendorListController",excep,theSession,theRequest);
			throw excep;
		}
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aVendorBillsList);
		return aResponse;
	}
	
	
	@RequestMapping(value="/vendorbills_data_quickpay",method = RequestMethod.POST)
	public @ResponseBody CustomResponse vendorBillsQuickPay(@RequestParam(value = "rxMasterID[]", required = false) String[] theRxMasterID, 
															@RequestParam(value = "fromDate", required = false) Date date1,
															@RequestParam(value = "moAccountID", required = false) Integer moAccountID,
															@RequestParam(value = "vendorQuickPayAmount", required = false) BigDecimal Amounts,
															@RequestParam(value = "termsCheckedorNot", required = false) String termsCheckedorNot,
															HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException, IOException, ParseException, MessagingException {
		
	     Vebillpay aVendorBills = new Vebillpay();
		 java.sql.Date dueDate = new java.sql.Date(date1.getTime());
		 System.out.println(" dueDate :: "+dueDate+" || vendorQuickPayAmount :: "+Amounts+"|| termsCheckedorNot::"+termsCheckedorNot);
		 UserBean aUserBean;
			aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			
		  	SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );   
		    Calendar billCal = Calendar.getInstance();    
		    Calendar curentDate = Calendar.getInstance();  			
			
			for(int i=0;i<theRxMasterID.length;i++)
			{	
			ArrayList<VendorBillPayBean> aVendorBillsList = null;	
			aVendorBillsList = (ArrayList<VendorBillPayBean>) itsVendorService.getVendorBillsQuickPayList(theRxMasterID[i],dueDate); // Select each vendor
			itsLogger.info("VendorBillPayBean Length ::"+aVendorBillsList.size());
			int count=0;
			BigDecimal Amount = Amounts;
			for(VendorBillPayBean vBObj:aVendorBillsList)
			{
				if(Amount==null)
					Amount = new BigDecimal(0);
				count++;
				
				if(vBObj.isDueOnDay()==1)
				{
					billCal.setTime( dateFormat.parse(vBObj.getBillDate()));    
				    billCal.add( billCal.MONTH, vBObj.getDiscountDays() );  
					
				}
				else
				{
					billCal.setTime( dateFormat.parse(vBObj.getBillDate()));    
				    billCal.add( billCal.DATE, vBObj.getDiscountDays() );  
				}
				
				if(termsCheckedorNot.equals("checked"))
				{
					Date d1=billCal.getTime();
					Date d2=curentDate.getTime();
				itsLogger.debug(d1+"===="+d2);	
				if(billCal.getTime().compareTo(curentDate.getTime())>=0)
				{
					//float value = ((float)vBObj.getDiscountPercent()/(float)100);
					//BigDecimal discValue=new BigDecimal(""+value);
					BigDecimal discValue=vBObj.getDiscountPercent().divide(new BigDecimal(100));
					aVendorBills.setDiscountAmount((vBObj.getBillAmount().multiply(discValue)));
				}
				else
				{
					aVendorBills.setDiscountAmount(new BigDecimal(0));	
				}
				}
				else
				{
					aVendorBills.setDiscountAmount(new BigDecimal(0));	
				}
				
				
				if(Amount.compareTo(BigDecimal.ZERO)!=0) // check Amount not zero
				{
						if(vBObj.getBillAmount().compareTo(Amount)==-1) // check Amount is greater than bill amount
						{
								if(count==aVendorBillsList.size()) // iterating aVendorBillsList and  Paying full Amount
								{
									aVendorBills.setApplyingAmount(vBObj.getBillAmount().subtract(aVendorBills.getDiscountAmount()));
									aVendorBills.setVeBillId(vBObj.getVeBillID());
									aVendorBills.setMoAccountId(moAccountID);
									aVendorBills.setUserID(aUserBean.getUserId());
									itsVendorService.changeVendorBillList(aVendorBills, false,aUserBean.getUserId());		
									Amount =new BigDecimal(0);
								}
								else
								{
									aVendorBills.setApplyingAmount(vBObj.getBillAmount().subtract(aVendorBills.getDiscountAmount()));
									aVendorBills.setVeBillId(vBObj.getVeBillID());
									aVendorBills.setMoAccountId(moAccountID);
									aVendorBills.setUserID(aUserBean.getUserId());
									itsVendorService.changeVendorBillList(aVendorBills, false,aUserBean.getUserId());	
									Amount =Amount.subtract(vBObj.getBillAmount()).add(aVendorBills.getDiscountAmount()); // Add Discount Amount to Amount
								}
							
						}
						else if(vBObj.getBillAmount().compareTo(Amount)==0)
						{
							aVendorBills.setApplyingAmount(vBObj.getBillAmount());
							aVendorBills.setVeBillId(vBObj.getVeBillID());
							aVendorBills.setMoAccountId(moAccountID);
							aVendorBills.setUserID(aUserBean.getUserId());
							itsVendorService.changeVendorBillList(aVendorBills, false,aUserBean.getUserId());
							Amount = Amount.subtract(vBObj.getBillAmount());
						}
						else
						{
							aVendorBills.setApplyingAmount(Amount);
							aVendorBills.setVeBillId(vBObj.getVeBillID());
							aVendorBills.setMoAccountId(moAccountID);
							aVendorBills.setUserID(aUserBean.getUserId());
							itsVendorService.changeVendorBillList(aVendorBills, false,aUserBean.getUserId());
							Amount =new BigDecimal(0);
						}
				}
			}
			}
		} catch (VendorException excep) {
			itsLogger.error(excep.getMessage(), excep);
			theResponse.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			sendTransactionException( theRxMasterID+"","VendorListController",excep,theSession,theRequest);
			throw excep;
		}
		CustomResponse aResponse = new CustomResponse();
		return aResponse;
	}
	
	@RequestMapping(value="/GetContactDetails",method = RequestMethod.GET)
	public @ResponseBody Rxcontact getContactDetails(@RequestParam(value = "rxContactID", required = false) Integer theContactID,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		Rxcontact aRxcontact = new Rxcontact();
		try{
			aRxcontact = itsVendorService.getContactDetails(theContactID);
		}catch (VendorException e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( theContactID+"","VendorListController",e,theSession,theRequest);
			throw e;
		}
		return aRxcontact;
	}
	

	
	
	@RequestMapping(value="/addBillPayDetails",method = RequestMethod.POST)
	public @ResponseBody Vebillpay addBillPayDetails(@RequestParam(value = "veBillID", required = false) Integer theVeBillID, 
														@RequestParam(value = "disCountPrice", required = false) BigDecimal theDisCountPrice,
														@RequestParam(value = "payingAmount", required = false) BigDecimal thePayingAmount,
														@RequestParam(value = "isAlreadyExist", required = false) Boolean isAlreadyExist,
														@RequestParam(value = "moAccountId", required = false) Integer moAccountId, 
														HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException, IOException, MessagingException {
		Vebillpay aVebillpay = null;
		Vebillpay aVendorBillsList = new Vebillpay();
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			if(theDisCountPrice==null)
				theDisCountPrice = new BigDecimal(0);
			aVendorBillsList.setDiscountAmount(theDisCountPrice);
			if(thePayingAmount==null)
				thePayingAmount = new BigDecimal(0);
			aVendorBillsList.setApplyingAmount(thePayingAmount);
			aVendorBillsList.setVeBillId(theVeBillID);
			aVendorBillsList.setMoAccountId(moAccountId);
			aVendorBillsList.setUserID(aUserBean.getUserId());
			aVebillpay = itsVendorService.changeVendorBillList(aVendorBillsList, isAlreadyExist,aUserBean.getUserId());				
		} catch (VendorException excep) {
			itsLogger.error(excep.getMessage(), excep);
			theResponse.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			sendTransactionException( theVeBillID+"","VendorListController",excep,theSession,theRequest);
			throw excep;
		}
		return aVebillpay;
	}
	
	
	@RequestMapping(value="/removeBillPayDetails",method = RequestMethod.POST)
	public @ResponseBody String removeBillPayDetails(@RequestParam(value = "veBillID", required = false) Integer theVeBillID, 
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException, IOException, MessagingException {
		String status="";
		
	    try {
			
			itsVendorService.removeVendorBillPaylist(theVeBillID);	
			status="Success";
			
		} catch (VendorException excep) {
			itsLogger.error(excep.getMessage(), excep);
			theResponse.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			sendTransactionException( theVeBillID+"","VendorListController",excep,theSession,theRequest);
			throw excep;
		
		}
		return status;
	}
	
	@RequestMapping(value="/clearAllCheckedBills",method = RequestMethod.POST)
	public @ResponseBody String clearAllCheckedBills(@RequestParam(value = "moAccountID", required = false) Integer moAccountID, 
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException, IOException, MessagingException {
		String status="";
		
	    try {
			
			itsVendorService.clearAllCheckedBills(moAccountID);	
			status="Success";
			
		} catch (VendorException excep) {
			itsLogger.error(excep.getMessage(), excep);
			theResponse.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			sendTransactionException( moAccountID+"","VendorListController",excep,theSession,theRequest);
			throw excep;
		
		}
		return status;
	}
	
	
	@RequestMapping(value="/getBillPayDetails",method = RequestMethod.GET)
	public @ResponseBody ArrayList<Vebillpay> getBillPayDetails(@RequestParam(value = "vebilID", required = false) Integer thevebilID, HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		ArrayList<Vebillpay> aVebillpays = new ArrayList<Vebillpay>();
		try{
			aVebillpays = itsVendorService.getBillPayDetails(thevebilID);
			
			System.out.println("aVebillpays.length::"+aVebillpays.size());
			
		}catch (VendorException e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( thevebilID+"","VendorListController",e,theSession,theRequest);
			throw e;
		}
		return aVebillpays;
	}
	
	/**
	 * @throws MessagingException 
	 * 
	 * 
	 * */
	@RequestMapping(value="/gettermsDiscountsfromveMaster",method = RequestMethod.GET)
	public @ResponseBody Vemaster gettermsDiscountsfromveMaster(@RequestParam(value = "vendorID", required = false) Integer thevendorID,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		Vemaster aVebillpays = new Vemaster();
		try{
			
			itsLogger.debug(thevendorID);
			aVebillpays = itsVendorService.gettermsDiscountsfromveMaster(thevendorID);
			
		}catch (VendorException e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( thevendorID+"","VendorListController",e,theSession,theRequest);
			throw e;
		}
		return aVebillpays;
	}
	
	@RequestMapping(value="/GetContactDetailsFromCuso",method = RequestMethod.GET)
	public @ResponseBody Rxcontact getContactDetailsFromCuso(@RequestParam(value = "rxMasterID", required = false) String rxMasterID,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		Rxcontact rxmaster = new Rxcontact();
		try{
			
			rxmaster = itsVendorService.getContactIdFromMasterID(rxMasterID);
		}catch (VendorException e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( rxMasterID+"","VendorListController",e,theSession,theRequest);
			throw e;
		}
		return rxmaster;
	}
	
	@RequestMapping(value = "/PurchaseOrderlineitemsbasedonreorder", method = RequestMethod.POST)
	public @ResponseBody CustomResponse PurchaseOrderlineitemsbasedonreorder(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "VendorID", required = false) Integer VendorID,
			@RequestParam(value = "WareHouseId", required = false) Integer WareHouseId,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			@RequestParam(value = "lineitemlist", required = false) String gridData,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException {
		CustomResponse aResponse = null;
			System.out.println("fromurl"+gridData+vePOID+"inside the method VendorID="+VendorID+"WareHouseId="+WareHouseId);
			List<Vepodetail> gridlist = new ArrayList<Vepodetail>();
			
			//{vePodetailId:"575",note:"DSDDOOR2424",inLineNoteImage:"test for lineitem",description:"ACCESS DOOR 24X24",notes:"test for lineitem",notesdesc:"test for lineitem",quantityOrdered:"2",inLineNote:"test for lineitem",unitCost:"$100.00",priceMultiplier:"2.0000",taxable:"No",quantityBilled:"$400.00",ackDate:" ",shipDate:" ",vendorOrderNumber:"",prMasterId:"1003"}
			JsonParser parser = new JsonParser();
			if (null != gridData && gridData.length() > 6) {
				
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				Gson gson = new Gson();
				for (JsonElement ele1 : array) {
					JsonObject jobj = ele1.getAsJsonObject();
					System.out.println(ele1);
					Vepodetail obj=new Vepodetail();
					obj.setNote(jobj.get("note").getAsString());
					obj.setInLineNoteImage(jobj.get("inLineNoteImage").getAsString());
					obj.setDescription(jobj.get("description").getAsString());
					BigDecimal quantityOrdered=BigDecimal.ZERO;
					if(jobj.get("quantityOrdered")!=null){
						quantityOrdered=CheckBigDecimalisNull(jobj.get("quantityOrdered").getAsString());
					}
					obj.setQuantityOrdered(quantityOrdered);
					BigDecimal quantityReceived=BigDecimal.ZERO;
					if(jobj.get("quantityReceived")!=null){
						quantityReceived=CheckBigDecimalisNull(jobj.get("quantityReceived").getAsString());
					}
					obj.setQuantityReceived(quantityReceived);
					BigDecimal invoicedAmount=BigDecimal.ZERO;
					if(jobj.get("invoicedAmount")!=null){
						String strIA=jobj.get("invoicedAmount").getAsString().replace("$", "");
						invoicedAmount=CheckBigDecimalisNull(strIA);
					}
					obj.setInvoicedAmount(invoicedAmount);
					
					BigDecimal unitCost=BigDecimal.ZERO;
					if(jobj.get("unitCost")!=null){
						String strUC=jobj.get("unitCost").getAsString().replace("$", "");
						unitCost=CheckBigDecimalisNull(strUC);
					}
					obj.setUnitCost(unitCost);
					
					BigDecimal priceMultiplier=BigDecimal.ZERO;
					if(jobj.get("priceMultiplier")!=null){
						String strPM=jobj.get("priceMultiplier").getAsString().replace("$", "");
						priceMultiplier=CheckBigDecimalisNull(strPM);
					}
					
					obj.setPriceMultiplier(priceMultiplier);
					boolean taxable=false;
					if(jobj.get("taxable")!=null){
						if(jobj.get("taxable").getAsString().equals("Yes")){
							taxable=true;
						}
					}
					obj.setTaxable(taxable);
					
					BigDecimal netCast=BigDecimal.ZERO;
					if(jobj.get("netCast")!=null){
						String strNC=jobj.get("netCast").getAsString().replace("$", "");
						netCast=CheckBigDecimalisNull(strNC);
					}
					obj.setNetCast(netCast);
					BigDecimal quantityBilled=BigDecimal.ZERO;
					if(jobj.get("quantityBilled")!=null){
						String strQB=jobj.get("quantityBilled").getAsString().replace("$", "");
						quantityBilled=CheckBigDecimalisNull(strQB);
					}
					obj.setQuantityBilled(quantityBilled);
					int vepoid=CheckIntisNull(jobj.get("vePoid").getAsString());
					obj.setVePoid(vepoid);
					int prMasterId=CheckIntisNull(jobj.get("prMasterId").getAsString());
					obj.setPrMasterId(prMasterId);
					int vePodetailId=CheckIntisNull(jobj.get("vePodetailId").getAsString());
					obj.setVePodetailId(vePodetailId);
					
					BigDecimal taxTotal=BigDecimal.ZERO;
					if(jobj.get("taxTotal")!=null){
						String strTT=jobj.get("taxTotal").getAsString().replace("$", "");
						taxTotal=CheckBigDecimalisNull(strTT);
					}
					obj.setTaxTotal(taxTotal);
					String ackdate="";
					if(jobj.get("ackDate")!=null&& !jobj.get("ackDate").getAsString().contains("Â")){
						ackdate=jobj.get("ackDate").getAsString();
					}
					obj.setAckDate(ackdate);
					String shipDate="";
					if(jobj.get("shipDate")!=null&& !jobj.get("shipDate").getAsString().contains("Â")){
						ackdate=jobj.get("shipDate").getAsString();
					}
					obj.setShipDate(shipDate);
					obj.setVendorOrderNumber(jobj.get("vendorOrderNumber").getAsString());
					obj.setInLineNote(jobj.get("inLineNote").getAsString());
							//gson.fromJson(ele1, Vepodetail.class);
					gridlist.add(obj);
				}
			}
			
			
			List<Vepodetail> productlist =itsVendorService.getreorderclicklineitems(VendorID,WareHouseId,vePOID);
			for(Vepodetail newlines:productlist){
				gridlist.add(newlines);
			}
			aResponse = new CustomResponse();
			aResponse.setRows(gridlist);

		return aResponse;
	}	
	public int CheckIntisNull(String value){
		try {
			Integer chkint=Integer.parseInt(value);
			return chkint.intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
	public BigDecimal CheckBigDecimalisNull(String value){
		try {
			BigDecimal chkDecimal=new BigDecimal(value);
			return chkDecimal;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
	}
	@RequestMapping(value="/GetFromAddressContactDetails",method = RequestMethod.GET)
	public @ResponseBody TsUserLogin getContactDetails(HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		TsUserLogin aTsUserLogin = new TsUserLogin();
		try{
			UserBean aUserBean;
			aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
			 aTsUserLogin = itsUserService.getSingleUserDetails(aUserBean.getUserId());
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException( "","VendorListController",e,theSession,theRequest);
		}
		return aTsUserLogin;
	}
	
	/*@RequestMapping(value = "/ReorderInsert", method = RequestMethod.GET)
	public @ResponseBody Boolean ReorderInsert(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "VendorID", required = false) Integer VendorID,
			@RequestParam(value = "WareHouseId", required = false) Integer WareHouseId,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException {
			
			List<Vepodetail> productlist =itsVendorService.getreorderclicklineitems(VendorID,WareHouseId,vePOID);
			Vepo thevepo=new Vepo();
			thevepo.setVePoid(vePOID);
			for(Vepodetail newlines:productlist){
				itsJobService.addPOReleaseLineItem(newlines, thevepo);
			}
		return true;
	}	*/
	
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
	
	@RequestMapping(value="/GetTOEmailWarehouseAdd",method = RequestMethod.GET)
	public @ResponseBody Prwarehouse GetTOEmailWarehouseAdd(
			@RequestParam(value = "WareHouseID", required = false) Integer WareHouseID,
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		Prwarehouse aprwareHouse =null;
		try{
			Cuso acuso=null;
			if(WareHouseID==-1){
				acuso=itsJobService.getCusoObj(cuSOID);
				WareHouseID=acuso.getPrFromWarehouseId();
			}
			
			 aprwareHouse = itsinventoryService.getPrwarehouse(WareHouseID);
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException( "","VendorListController",e,theSession,theRequest);
		}
		return aprwareHouse;
	}
	
	@RequestMapping(value = "/ReorderInsertNew", method = RequestMethod.POST)
	public @ResponseBody CustomResponse ReorderInsert(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "VendorID", required = false) Integer VendorID,
			@RequestParam(value = "WareHouseId", required = false) Integer WareHouseId,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException {
		CustomResponse aResponse = new CustomResponse();
		ArrayList<Vepodetail> overAllvePODetailLst=new ArrayList<Vepodetail>();
		JsonParser parser = new JsonParser();
		if ( gridData!=null) {

			System.out.println("gridData"+gridData);
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			BigDecimal whcostTotalAmount=BigDecimal.ZERO;
			for (JsonElement ele1 : array) {
				boolean saved = false;
				Vepodetail aVepodetail=new Vepodetail();
				JsonObject obj = ele1.getAsJsonObject();
				
				String itemCode=obj.get("note").getAsString();
				String desc=obj.get("description").getAsString();
				BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
				BigDecimal quantityReceived=BigDecimal.ZERO;
				if(obj.get("quantityReceived")!=null && obj.get("quantityReceived").getAsString()!=""&& obj.get("quantityReceived").getAsString().length()>0 )
				{
					quantityReceived=obj.get("quantityReceived").getAsBigDecimal();
				}
				String invoicedAmount_String=obj.get("invoicedAmount").getAsString().replaceAll("\\$", "");
				invoicedAmount_String=invoicedAmount_String.replaceAll(",", "");
				BigDecimal invoicedAmount=JobUtil.ConvertintoBigDecimal(invoicedAmount_String);;
				String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
				unitCost_String=unitCost_String.replaceAll(",", "");
				BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
				BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
				String Taxable=obj.get("taxable").getAsString();
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("yes")) {
						aVepodetail.setTaxable(true);
					} else {
						aVepodetail.setTaxable(false);
					}
				}
				
				Integer vePoId=null;
				if(obj.get("vePoid")!=null && obj.get("vePoid").getAsString()!=""&& obj.get("vePoid").getAsString().length()>0){
					vePoId=obj.get("vePoid").getAsInt();
				}
				Integer prMasterId=obj.get("prMasterId").getAsInt();
				Integer vePodetailId=null;
				if(obj.get("vePodetailId")!=null && obj.get("vePodetailId").getAsString()!=""&& obj.get("vePodetailId").getAsString().length()>0){
					vePodetailId=obj.get("vePodetailId").getAsInt();
				}
				String ackDate_String=obj.get("ackDate").getAsString();
				String shipDate_String=obj.get("shipDate").getAsString();
				String vendorOrderNumber=obj.get("vendorOrderNumber").getAsString();
				String inLineNote=obj.get("inLineNote").getAsString();
				
				

				aVepodetail.setNote(itemCode);
				aVepodetail.setDescription(desc);
				aVepodetail.setQuantityOrdered(quantityOrder);
				aVepodetail.setQuantityReceived(quantityReceived);
				aVepodetail.setInvoicedAmount(invoicedAmount);
				aVepodetail.setUnitCost(unitCost);
				aVepodetail.setPriceMultiplier(priceMultiplier);
				aVepodetail.setVePoid(vePoId); 
				aVepodetail.setPrMasterId(prMasterId);
				aVepodetail.setVePodetailId(vePodetailId);
				aVepodetail.setAckDate(ackDate_String);
				aVepodetail.setShipDate(shipDate_String);
				aVepodetail.setVendorOrderNumber(vendorOrderNumber);
				aVepodetail.setInLineNote(inLineNote);
				
				overAllvePODetailLst.add(aVepodetail);
			}
		}
			List<Vepodetail> productlist =itsVendorService.getreorderclicklineitems(VendorID,WareHouseId);
			for(Vepodetail aVepodetail:productlist){
				aVepodetail.setVePoid(vePOID);
				overAllvePODetailLst.add(aVepodetail);
			}
			aResponse.setRows(overAllvePODetailLst);
			return aResponse;
	}
	
	
	@RequestMapping(value = "/XMLUploadData", method = RequestMethod.POST)
	public @ResponseBody CustomResponse XMLUploadData(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "XmlData",required = false) String XmlData,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException {
		CustomResponse aResponse = new CustomResponse();
		ArrayList<Vepodetail> overAllvePODetailLst=new ArrayList<Vepodetail>();
		JsonParser parser = new JsonParser();
		if ( gridData!=null) {

			System.out.println("gridData"+gridData);
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			BigDecimal whcostTotalAmount=BigDecimal.ZERO;
			for (JsonElement ele1 : array) {
				boolean saved = false;
				Vepodetail aVepodetail=new Vepodetail();
				JsonObject obj = ele1.getAsJsonObject();
				
				String itemCode=obj.get("note").getAsString();
				String desc=obj.get("description").getAsString();
				BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
				BigDecimal quantityReceived=BigDecimal.ZERO;
				if(obj.get("quantityReceived")!=null && obj.get("quantityReceived").getAsString()!=""&& obj.get("quantityReceived").getAsString().length()>0 )
				{
					quantityReceived=obj.get("quantityReceived").getAsBigDecimal();
				}
				String invoicedAmount_String=obj.get("invoicedAmount").getAsString().replaceAll("\\$", "");
				invoicedAmount_String=invoicedAmount_String.replaceAll(",", "");
				BigDecimal invoicedAmount=JobUtil.ConvertintoBigDecimal(invoicedAmount_String);;
				String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
				unitCost_String=unitCost_String.replaceAll(",", "");
				BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
				BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
				String Taxable=obj.get("taxable").getAsString();
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("yes")) {
						aVepodetail.setTaxable(true);
					} else {
						aVepodetail.setTaxable(false);
					}
				}
				
				Integer vePoId=null;
				if(obj.get("vePoid")!=null && obj.get("vePoid").getAsString()!=""&& obj.get("vePoid").getAsString().length()>0){
					vePoId=obj.get("vePoid").getAsInt();
				}
				Integer prMasterId=obj.get("prMasterId").getAsInt();
				Integer vePodetailId=null;
				if(obj.get("vePodetailId")!=null && obj.get("vePodetailId").getAsString()!=""&& obj.get("vePodetailId").getAsString().length()>0){
					vePodetailId=obj.get("vePodetailId").getAsInt();
				}
				String ackDate_String=obj.get("ackDate").getAsString();
				String shipDate_String=obj.get("shipDate").getAsString();
				String vendorOrderNumber=obj.get("vendorOrderNumber").getAsString();
				String inLineNote=obj.get("inLineNote").getAsString();
				
				

				aVepodetail.setNote(itemCode);
				aVepodetail.setDescription(desc);
				aVepodetail.setQuantityOrdered(quantityOrder);
				aVepodetail.setQuantityReceived(quantityReceived);
				aVepodetail.setInvoicedAmount(invoicedAmount);
				aVepodetail.setUnitCost(unitCost);
				aVepodetail.setPriceMultiplier(priceMultiplier);
				aVepodetail.setVePoid(vePoId); 
				aVepodetail.setPrMasterId(prMasterId);
				aVepodetail.setVePodetailId(vePodetailId);
				aVepodetail.setAckDate(ackDate_String);
				aVepodetail.setShipDate(shipDate_String);
				aVepodetail.setVendorOrderNumber(vendorOrderNumber);
				if(inLineNote!=null && inLineNote!=""){
					aVepodetail.setInLineNote(inLineNote);
					aVepodetail.setInLineNoteImage(inLineNote);
				}
				
				
				overAllvePODetailLst.add(aVepodetail);
			}
			
		}
			
		if(XmlData!=null){
			JsonElement ele = parser.parse(XmlData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			BigDecimal whcostTotalAmount=BigDecimal.ZERO;
			for (JsonElement ele1 : array) {
				boolean saved = false;
				Vepodetail aVepodetail=new Vepodetail();
				JsonObject obj = ele1.getAsJsonObject();
				
				String itemCode=obj.get("itemCode").getAsString();
				String desc=obj.get("description").getAsString();
				BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
				
				BigDecimal quantityReceived=BigDecimal.ZERO;
				String invoicedAmount_String=obj.get("invoicedAmount").getAsString().replaceAll("\\$", "");
				invoicedAmount_String=invoicedAmount_String.replaceAll(",", "");
				BigDecimal invoicedAmount=JobUtil.ConvertintoBigDecimal(invoicedAmount_String);;
				String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
				unitCost_String=unitCost_String.replaceAll(",", "");
				BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
				BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
				String Taxable=obj.get("taxable").getAsString();
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("true")) {
						aVepodetail.setTaxable(true);
					} else {
						aVepodetail.setTaxable(false);
					}
				}
				
				Integer vePoId=null;
				if(obj.get("vePoid")!=null && obj.get("vePoid").getAsString()!=""&& obj.get("vePoid").getAsString().length()>0){
					vePoId=obj.get("vePoid").getAsInt();
				}
				Integer prMasterId=obj.get("prMasterId").getAsInt();
				Integer vePodetailId=null;
				//String ackDate_String=obj.get("ackDate").getAsString();
				//String shipDate_String=obj.get("shipDate").getAsString();
				//String vendorOrderNumber=obj.get("vendorOrderNumber").getAsString();
				System.out.println("obj.get('inLineNote')"+obj.get("inLineNote"));
				if(obj.get("inLineNote")!=null && !(obj.get("inLineNote").equals("null"))){
					String inLineNote=obj.get("inLineNote").getAsString();
					if(inLineNote!=null && inLineNote!=""){
						aVepodetail.setInLineNote(inLineNote);
						aVepodetail.setInLineNoteImage(inLineNote);
					}
				}
				
				

				aVepodetail.setNote(itemCode);
				aVepodetail.setDescription(desc);
				aVepodetail.setQuantityOrdered(quantityOrder);
				aVepodetail.setQuantityReceived(quantityReceived);
				aVepodetail.setInvoicedAmount(invoicedAmount);
				aVepodetail.setUnitCost(unitCost);
				aVepodetail.setPriceMultiplier(priceMultiplier);
				aVepodetail.setVePoid(vePoId); 
				aVepodetail.setPrMasterId(prMasterId);
				aVepodetail.setVePodetailId(vePodetailId);
				//aVepodetail.setAckDate(ackDate_String);
				//aVepodetail.setShipDate(shipDate_String);
				//aVepodetail.setVendorOrderNumber(vendorOrderNumber);
				
				overAllvePODetailLst.add(aVepodetail);
			}
		}
			aResponse.setRows(overAllvePODetailLst);
			return aResponse;
	}
	
	
}
