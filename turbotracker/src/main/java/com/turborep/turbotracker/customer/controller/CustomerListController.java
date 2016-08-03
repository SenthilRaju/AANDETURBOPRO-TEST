package com.turborep.turbotracker.customer.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.RxJournal;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.StatementsBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.service.EmployeeService;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.Jobidder;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.EmailParameters;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.OperatingSystemInfo;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.PurchaseService;

@Controller
@RequestMapping("/customerList")
public class CustomerListController {
	
	protected static Logger itsLogger = Logger.getLogger(CustomerListController.class);
	private List<StatementsBean> rxMasterIDArray = null;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="customerService")
	private CustomerService itsCustomerService;
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name = "sysService")
	private SysService itsSysService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;
	
	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;
	
	/*Updated By: Velmurugan
	 *Updated On: 10-9-2014
	 *Description:Fetching the customer from rxmaster table and get the address if it is default 1 
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody CustomResponse getAll(@RequestParam(value="page", required=false) Integer thePage,
												@RequestParam(value="rows", required=false) Integer theRows,
												@RequestParam(value="sidx", required=false) String theSidx,
												@RequestParam(value="sord", required=false) String theSord,
												HttpSession session,HttpServletRequest therequest,
												HttpServletResponse response) throws IOException, MessagingException, VendorException {
		String aCustomerID = "customer";
		String aRolodexWhere = "WHERE isCustomer = 1";
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		//int aTotalCount = itsRolodexService.getRecordCount(aRolodexWhere);
		int aTotalCount =companyService.getCount(getCustomersCount());
		getCustomersCount();
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			
			System.out.println("CustomerListController.getAll():::"+aUserBean.getUserId());
			
			aUserLogin = new TsUserLogin();
			aUserLogin.setCustomerperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aCustomerID);
			int aFrom, aTo;
			if(theRows==null){
				theRows=50;
			}
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			itsLogger.debug("Retriving all the data of customer");
			List<?> customer = itsRolodexService.getCustomers(aFrom, aTo);
			aResponse = new CustomResponse();
			aResponse.setRows(customer);
			aResponse.setRecords(String.valueOf(customer.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
		} catch (UserException e) {
			sendTransactionException("<b>MethodName:</b>getAll","Customer",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>MethodName:</b>getAll","Customer",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	
	@RequestMapping(value="/customeraddress",method = RequestMethod.GET)
	public @ResponseBody CustomResponse customerAddress(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("Sales rep id: " + theRolodexId);
		CustomResponse aResponse = new CustomResponse();
		try{
		Rxaddress aRxaddress = new Rxaddress();
		aRxaddress.setRxMasterId(theRolodexId);
		ArrayList<Rxaddress> aRxaddressList = (ArrayList<Rxaddress>) itsRolodexService.getCustomerAddressDetails(aRxaddress);
		aResponse.setRows(aRxaddressList);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"Customer",e,session,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value="/customerjounal",method = RequestMethod.GET)
	public @ResponseBody CustomResponse customerJurnal(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession session ,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("Sales rep id: " + theRolodexId);
		CustomResponse aResponse = new CustomResponse();
		try{
		RxJournal aRxJounal = new RxJournal();
		aRxJounal.setRxMasterId(theRolodexId);
		ArrayList<RxJournal> aRxjournalsList = (ArrayList<RxJournal>) itsRolodexService.getRxJournalDetails(aRxJounal);
		aResponse.setRows(aRxjournalsList);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"Customer",e,session,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value="/customerJobs",method = RequestMethod.GET)
	public @ResponseBody CustomResponse customerJobs(@RequestParam(value = "rolodexNumber", required = false) String theRolodexId,
			HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("Sales rep id: " + theRolodexId);
		CustomResponse aResponse = new CustomResponse();
		try{
			ArrayList<JobsBean> aRxjournalsList = (ArrayList<JobsBean>) itsRolodexService.getCustomerJobsDetails(theRolodexId);
			aResponse.setRows(aRxjournalsList);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"Customer",e,session,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value="/customerNameList", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerNameList(@RequestParam("term") String theCustomerName,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		ArrayList<AutoCompleteBean> aCustomerName =new ArrayList<AutoCompleteBean>();
		try{
		aCustomerName = (ArrayList<AutoCompleteBean>) itsRolodexService.getCustomerNameList(theCustomerName);
		}catch(Exception e){
			sendTransactionException("<b>theCustomerName:</b>"+theCustomerName,"Customer",e,session,therequest);
		}
		return aCustomerName;
	}
	
	@RequestMapping(value="/customerLostQuotes",method = RequestMethod.GET)
	public @ResponseBody CustomResponse customerLostQuotes(@RequestParam(value = "rolodexNumber", required = false) String theRolodexId,
						@RequestParam(value = "rxContactId", required = false) Integer theContactId,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try{
			int aJobStatus = 2;
			ArrayList<JobsBean> aRxjournalsList = (ArrayList<JobsBean>) itsRolodexService.getCustomerLostQuotes(theRolodexId, aJobStatus, theContactId);
			aResponse.setRows(aRxjournalsList);
			System.out.println("inside controller calling lost quotes"+aRxjournalsList.size()+" : "+theRolodexId+" : "+theContactId);
			
		}catch(Exception e){
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"Customer",e,session,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value="/customerQuotes",method = RequestMethod.GET)
	public @ResponseBody CustomResponse customerQuotes(@RequestParam(value = "rolodexNumber", required = false) String theRolodexId,
	@RequestParam(value = "rxContactId", required = false) Integer theContactId,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try{
			int aJobStatus = 4;
			ArrayList<JobsBean> aRxjournalsList = (ArrayList<JobsBean>) itsRolodexService.getCustomerQuotes(theRolodexId, aJobStatus, theContactId);
			aResponse.setRows(aRxjournalsList);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"Customer",e,session,therequest);
		}
		
		return aResponse;
	}
	
	@RequestMapping(value="/contactIdbasedOpenQuotes",method = RequestMethod.GET)
	public @ResponseBody CustomResponse contactIdbasedOpenQuotes(@RequestParam(value = "rolodexNumber", required = false) String theRolodexId,
		@RequestParam(value = "rxContactId", required = false) Integer theContactId,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try{
			int aJobStatus = 4;
			ArrayList<JobsBean> aRxjournalsList = (ArrayList<JobsBean>) itsRolodexService.getContactIdbasedOpenQuotes(theRolodexId, aJobStatus, theContactId);
			aResponse.setRows(aRxjournalsList);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexId,theContactId:</b>"+theRolodexId+","+theContactId,"Customer",e,session,therequest);
		}
		
		return aResponse;
	}
	
	@RequestMapping(value="/addNewCustomer",method = RequestMethod.GET)
	public @ResponseBody Rxmaster addCustomer(@RequestParam(value = "name", required = false) String theName,
																						 @RequestParam(value = "address1", required = false) String theAddress1,
																						 @RequestParam(value = "address2", required = false) String theAddress2, 
																						 @RequestParam(value = "state", required = false) String theState,
																						 @RequestParam(value = "zip", required = false) String thePinCode, 
																						 @RequestParam(value = "contact1", required = false) String thePhone1,
																						 @RequestParam(value = "contact2", required = false) String thePhone2, 
																						 @RequestParam(value = "fax", required = false) String theFax, 
																						 @RequestParam(value = "cityNameListName", required = false) String theCity, 
																						 @RequestParam(value = "customerId", required = false) boolean theCustomerId, HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		Rxmaster aCustomer = new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		UserBean aUserBean;
		Rxmaster aNewCustomer = null;
		try{
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			aCustomer.setName(theName);
			String afirstName = "";
			boolean inActive = false;
			boolean IsStreet = false;
			boolean IsMailing = false;
			boolean IsBillTo = true;
			boolean IsShipTo = true;
			theCustomerId = true;
			aCustomer.setFirstName(afirstName);
			String asearchName = theName.replace(" ", "");
			if(asearchName.length() > 10){
				String asearchText = asearchName.substring(0, 10);
				aCustomer.setSearchName(asearchText.toUpperCase());
			}else{
				aCustomer.setSearchName(asearchName.toUpperCase());
			}
			aCustomer.setInActive(inActive);
			aCustomer.setPhone1(thePhone1);
			aCustomer.setPhone2(thePhone2);
			aCustomer.setFax(theFax);
			aCustomer.setIsCustomer(theCustomerId);
			aCustomer.setCreatedById(aUserBean.getUserId());
			aRxaddress.setName(theName);
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
			aRxaddress.setPhone1(thePhone1);
			aRxaddress.setPhone2(thePhone2);
			aRxaddress.setFax(theFax);
			aRxaddress.setIsDefault(true);
			itsLogger.debug("Add new customer");
			aNewCustomer = itsRolodexService.addNewCustomer(aCustomer, aRxaddress, aUserBean.getUserId());
		}catch (Exception e) {
			sendTransactionException("<b>addNewCustomer:</b>addNewCustomer","Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aNewCustomer;
	}
	
	@RequestMapping(value="/updateCustomerName",method = RequestMethod.GET)
	public @ResponseBody int updateCustomerName(@RequestParam(value = "customer", required = false) String theCustomerName,
		@RequestParam(value = "customerId", required = false)int theCustomerId,HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		itsLogger.debug("update Customer");
		int aCustomerName = 0;
		try{
			aCustomerName = itsRolodexService.updateCustomerName(theCustomerName, theCustomerId);
		}catch (Exception e) {
			sendTransactionException("<b>theCustomerId:</b>"+theCustomerId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aCustomerName;
	}

	@RequestMapping(value="/addRolodexCustomerDetails",method = RequestMethod.POST)
	public @ResponseBody Rxmaster addRolodexCustomer( @RequestParam(value = "rxMasterId", required = false) Integer theRxMasterId,
			@RequestParam(value ="cuStomerName",required= false) String theName,
			@RequestParam(value = "custAccountNumber", required = false) String theCustomerAccountNumber,
			@RequestParam(value = "custFinanceCharge", required = false) Byte theFinanceChange,
			@RequestParam(value = "custFinStatement", required = false) Byte theStatement,
			@RequestParam(value = "paymentTerms", required = false) String thePaymentTerms,
			@RequestParam(value = "cuPaymentTermsId", required = false) Integer theCuPaymentTermsId,
			@RequestParam(value = "customerType", required = false) Integer theCustomerTypeId,
			@RequestParam(value = "prWarehouse", required = false) Integer thePrWarehouse, 
			@RequestParam(value = "taxExemptNo", required = false) String theTaxExemptNo,
			@RequestParam(value = "isPORequired", required = false) Integer theIsPOrequired,
			@RequestParam(value = "assignedSalesRepId", required = false) Integer theEmpSalesMan, 
			@RequestParam(value = "assignedCSRId", required = false) Integer theEmpCSR, 
			@RequestParam(value = "assignedSalesMgrId", required = false) Integer theEmpSalesMgr, 
			@RequestParam(value = "assignedEngineerId", required = false) Integer theEmpEngineer, 
			@RequestParam(value = "assignedPrjMgrId", required = false) Integer theEmpPrjMgr,
			/**@throws MessagingException 
			 * @throws IOException 
			 * @RequestParam(value = "isCreditApp", required = false) Integer isCreditApp,*/
			@RequestParam(value = "creditAppDate", required = false) String theCreditAppDate,
			@RequestParam(value = "creditLimit", required = false) BigDecimal theCreditLimit,
			@RequestParam(value = "isCreditHold", required = false) Integer theIsCredithold,
			@RequestParam(value = "creditHoldOverride", required = false) Integer theCreditHoldOverrideToday,
			@RequestParam(value = "quoteMethod", required = false) Integer theQuoteMethod,
			@RequestParam(value = "qm_text", required = false) String theQmText,
			@RequestParam(value = "invoiceMethod", required = false) Integer theInvoiceMethod,
			@RequestParam(value = "im_text", required = false) String theImText,
			@RequestParam(value = "statementMethod", required = false) Integer theStatementMethod,
			@RequestParam(value = "sm_text", required = false) String theSmText,
			@RequestParam(value="rxaddressID", required=false) ArrayList<?> rxaddressIDlist,
			@RequestParam(value="custFinancetaxterr", required=false) ArrayList<?> taxterritorylist,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("Add Rolodex Customer Details");
		Cumaster aCumaster = new Cumaster();
		try {
			if(theRxMasterId==null){
				theRxMasterId=0;
			}
			aCumaster.setCuMasterId(theRxMasterId);
			aCumaster.setCuAssignmentId0(theEmpSalesMan);
			aCumaster.setCuAssignmentId1(theEmpCSR);
			aCumaster.setCuAssignmentId2(theEmpSalesMgr);
			aCumaster.setCuAssignmentId3(theEmpEngineer);
			aCumaster.setCuAssignmentId4(theEmpPrjMgr);
			aCumaster.setAccountNumber(theCustomerAccountNumber);
			if (theFinanceChange != null) {	aCumaster.setFinCharge(theFinanceChange);}
			if (theStatement != null) { aCumaster.setStatements(theStatement);}
			aCumaster.setCuTermsId(theCuPaymentTermsId);
			if(theCustomerTypeId.equals(-1)){
				aCumaster.setCuMasterTypeId(null);
			}else{
				aCumaster.setCuMasterTypeId(theCustomerTypeId);
			}
			if (thePrWarehouse != null) {aCumaster.setPrWarehouseId(thePrWarehouse);}
			if (theTaxExemptNo != null){aCumaster.setTaxExemptNumber(theTaxExemptNo);}
			if (theIsPOrequired != null && theIsPOrequired == 1) {aCumaster.setPorequired(true);}
			else {aCumaster.setPorequired(false);}
			aCumaster.setCreditLimit(theCreditLimit);
			if (!theCreditAppDate.equals("") && theCreditAppDate != null) {
				aCumaster.setCreditAppDate(DateUtils.parseDate(theCreditAppDate, new String[]{"MM/dd/yyyy"}));
			}
			if(theIsCredithold != null && theIsCredithold == 1) {aCumaster.setCreditHold(true);} 
			else {aCumaster.setCreditHold(false);}
			if(theCreditHoldOverrideToday != null && theCreditHoldOverrideToday == 1) {aCumaster.setCreditHoldOverride(new Date());}
			
			System.out.println(aCumaster.getCreditHoldOverride()+"::::"+aCumaster.getCuMasterId());
			
			aCumaster.setQuoteMethod(theQuoteMethod);
			if (theQuoteMethod == 1 || theQuoteMethod == 2){aCumaster.setQmText(theQmText);}
			aCumaster.setInvoiceMethod(theInvoiceMethod);
			if (theInvoiceMethod == 1 || theInvoiceMethod == 2){aCumaster.setImText(theImText);}
			aCumaster.setStatementMethod(theStatementMethod);
			if (theStatementMethod == 1 || theStatementMethod == 2){aCumaster.setSmText(theSmText);}
			if(rxaddressIDlist!=null){
			for(int i=0;i<rxaddressIDlist.size();i++){
				Rxaddress rxaddress=new Rxaddress();
				int id=Integer.parseInt((String) rxaddressIDlist.get(i));
				int tax=Integer.parseInt((String) taxterritorylist.get(i));
				rxaddress.setRxAddressId(id);
				rxaddress.setCoTaxTerritoryId(tax);
				itsRolodexService.updateRxaddressfromCustomerFinancial(rxaddress);
			}
			}	
			itsRolodexService.updateCuMasterDetailsRecord(aCumaster);
			itsRolodexService.updateCustomerName(theName,theRxMasterId);
			
		} catch (ParseException e) {
			sendTransactionException("<b>MethodName:addRolodexCustomerDetails</b>","Customer",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, "Parse exception occurred on updating the customer details");
		} catch (Exception e) {
			sendTransactionException("<b>MethodName:addRolodexCustomerDetails</b>","Customer",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, "Exception occurred on updating the customer details:" + e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value="/addQuickQuote",method = RequestMethod.POST)
	public @ResponseBody Rxmaster addQuickQuote( @RequestParam(value = "customerName", required = false) String theCustomerName,
																							@RequestParam(value = "customerHiddenName", required = false) Integer theCustomerHiddenName,
																							@RequestParam(value = "pharagraphName", required = false) String thePharagraphName,
																							@RequestParam(value = "productListName", required = false) String theProductListName,
																							/*@RequestParam(value = "productHiddenName", required = false) Integer theProductListHiddenName,*/
																							@RequestParam(value = "rxMasterHiddenName", required = false) Integer theRxMasterHiddenName,
																							@RequestParam(value = "customerContactHiddenName", required = false) String theCustomerContactHiddenName,
																							@RequestParam(value = "costName", required = false) BigDecimal theCost,
																							@RequestParam(value = "manufacturerName", required = false) String theManufactureName,
																							@RequestParam(value = "manufacterHiddenName", required = false) Integer theManufactureHiddenName, 
																							@RequestParam(value = "sellPriceName", required = false) BigDecimal theSellPriceName,
																							@RequestParam(value = "jobStatusName", required = false) Integer theJobStatus, 
																							@RequestParam(value = "revision", required = false) String theRevision, HttpServletResponse theResponse, HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("Add Quick Quote");
		Jomaster aJomaster = new Jomaster();
		Jobidder aJobidder = new Jobidder();
		Cuinvoice aCuinvoice = new Cuinvoice();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter=  new SimpleDateFormat("MM/dd/yyyy");
		String theDateNow = formatter.format(currentDate.getTime());
		int jomaterID;
		try {
			aJomaster.setRxCustomerId(theRxMasterHiddenName);
			aJomaster.setLocationName(thePharagraphName);
			aJomaster.setDescription(theProductListName);
			aJomaster.setEstimatedCost(theCost);
			aJomaster.setContractAmount(theSellPriceName);
			aJomaster.setJobStatus(theJobStatus);
			aJomaster.setCreatedById(aUserBean.getUserId());
			aJomaster.setBidDate(DateUtils.parseDate(theDateNow, new String[]{"MM/dd/yyyy"}));
			aJomaster.setJobNumber(itsSysService.getSysSequenceNumber("joMaster").toString());
			aCuinvoice.setRxCustomerId(theRxMasterHiddenName);
			if(theJobStatus == 3){
				aCuinvoice.setTransactionStatus(1);
			}else if(theJobStatus == 2){
				aCuinvoice.setTransactionStatus(2);
			}else if(theJobStatus == 1){
				aCuinvoice.setTransactionStatus(1);
			}
			aCuinvoice.setCreatedById(aUserBean.getUserId());
			aCuinvoice.setCreatedOn(DateUtils.parseDate(theDateNow, new String[]{"MM/dd/yyyy"}));
			aCuinvoice.setInvoiceDate(DateUtils.parseDate(theDateNow, new String[]{"MM/dd/yyyy"}));
			aCuinvoice.setApplied(false);
			BigDecimal aAmount = new BigDecimal(0);
			aCuinvoice.setAppliedAmount(aAmount);
			aCuinvoice.setInvoiceAmount(aAmount);
			aCuinvoice.setTaxAmount(aAmount);
			aJobidder.setQuoteDate(DateUtils.parseDate(theDateNow, new String[]{"MM/dd/yyyy"}));
			aJobidder.setRxMasterId(theRxMasterHiddenName);
			aJobidder.setRxContactId(theCustomerHiddenName);
			aJobidder.setQuoteStatus(aJomaster.getJobStatus().byteValue());
			aJobidder.setUserLoginID(aUserBean.getUserId());
			int aJobStatusID;
			if(theJobStatus == -10){
				aJobStatusID = 0;
			}else{
				aJobStatusID = theJobStatus;
			}
			byte aJobStatus =(byte) aJobStatusID;
			aJobidder.setQuoteStatus(aJobStatus);
			aJobidder.setQuoteRev(theRevision);
			aJobidder.setCuMasterTypeId(1);
			jomaterID = (Integer) itsRolodexService.addQuickQuote(aJomaster);
			aJobidder.setJoMasterId(jomaterID);
			itsRolodexService.addQuickQuoteBidder(aJobidder);
			itsRolodexService.addQuickQuoteCuMaster(aCuinvoice);
		} catch (Exception e) {
			sendTransactionException("<b>MethodName:theManufactureName</b>","Customer",e,session,therequest);
			try {
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, "Exception occurred on updating the customer details:" + e.getMessage());
			} catch (IOException ex) {
				itsLogger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	@RequestMapping(value="/updateQuickQuote",method = RequestMethod.POST)
	public @ResponseBody Rxmaster addOpenQuotes( @RequestParam(value = "editQuoteJobNumberName", required = false) String theJobNumber,
																								@RequestParam(value = "editQuoteJoBidderIdName", required = false) Integer theJoBidderID,
																								@RequestParam(value = "editQuoteJobMasterName", required = false) Integer theJoMasterID,
																								@RequestParam(value = "editQuoteRxContactName", required = false) Integer theRxContactID,
																								@RequestParam(value = "editQuoteRxMasterName", required = false) Integer theRxMasterID,
																								@RequestParam(value = "editQuoteProductListName", required = false) String theJobName,
																								@RequestParam(value = "editQuoteDateName", required = false) String theDate,
																								@RequestParam(value = "editQuoteCustomerName", required = false) String theContactName,
																								@RequestParam(value = "editQuoteCustomerHiddenName", required = false) String theContactNameHidden,
																								@RequestParam(value = "editQuoteRxMasterHiddenName", required = false) Integer theRxMasterIDHidden, 
																								@RequestParam(value = "editQuoteCustomerContactHiddenName", required = false) String theCustomerContactName,
																								@RequestParam(value = "editQuoteCostName", required = false) BigDecimal theCost,
																								@RequestParam(value = "editQuoteSellPriceName", required = false) BigDecimal theSellPriceName,
																								@RequestParam(value = "editQuoteRevisionName", required = false) String theRevision,
																								@RequestParam(value = "editQuoteJobStatusName", required = false) Integer theJobStatus,  
																								HttpServletResponse theResponse, HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("Update Quick Quote");
		Jomaster aJomaster = new Jomaster();
		Jobidder aJobidder = new Jobidder();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			aJomaster.setRxCustomerId(theRxMasterID);
			aJomaster.setDescription(theJobName);
			aJomaster.setEstimatedCost(theCost);
			aJomaster.setContractAmount(theSellPriceName);
			aJomaster.setJobStatus(theJobStatus);
			aJomaster.setCreatedById(aUserBean.getUserId());
			aJomaster.setBidDate(DateUtils.parseDate(theDate, new String[]{"MM/dd/yyyy"}));
			aJomaster.setJobNumber(theJobNumber);
			aJobidder.setRxMasterId(theRxMasterID);
			aJobidder.setRxContactId(theRxContactID);
			aJobidder.setQuoteStatus(theJobStatus.byteValue());
			aJobidder.setUserLoginID(aUserBean.getUserId());
			int aJobStatusID;
			if(theJobStatus == -10){
				aJobStatusID = 0;
			}else{
				aJobStatusID = theJobStatus;
			}
			byte aJobStatus =(byte) aJobStatusID;
			aJobidder.setQuoteStatus(aJobStatus);
			aJobidder.setQuoteRev(theRevision);
			aJobidder.setCuMasterTypeId(1);
			aJomaster.setJoMasterId(theJoMasterID);
			aJobidder.setJoBidderId(theJoBidderID);
			aJobidder.setJoMasterId(theJoMasterID);
			aJobidder.setRxMasterId(theRxMasterID);
			itsRolodexService.editQuickQuote(aJomaster);
			itsRolodexService.editQuickQuoteBidder(aJobidder);
		} catch (Exception e) {
			sendTransactionException("<b>theJoMasterID,theJoBidderID:</b>"+theJoMasterID+","+theJoBidderID,"Customer",e,session,therequest);
			try {
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, "Exception occurred on updating the customer details:" + e.getMessage());
			} catch (IOException ex) {
				itsLogger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	@RequestMapping(value = "newCustomerAddress", method = RequestMethod.POST)
	public @ResponseBody Rxmaster addCustomerAddress( @RequestParam(value = "locationAddress1", required = false) String theAddress1,
													 @RequestParam(value = "locationAddress2", required = false) String theAddress2, 
													 @RequestParam(value = "locationState", required = false) String theState,
													 @RequestParam(value = "locationZip", required = false) String thePinCode, 
													 @RequestParam(value = "USPhoneNumber", required = false) String thePhone1,
													 @RequestParam(value = "USPhone_Number", required = false) String thePhone2, 
													 @RequestParam(value = "fax", required = false) String theFax,
													 @RequestParam(value = "CountyId", required = false) Integer theCountyId,
													 @RequestParam(value="isMailing",required=false)boolean IsMailing,
													 @RequestParam(value="isShipping",required=false)boolean IsShipTo,
													 @RequestParam(value="isDefault",required=false)boolean IsDefault,
													 @RequestParam(value = "locationCity", required = false) String theCity,
													 @RequestParam(value="rolodexNumber", required = false) Integer theRxmasterId,
													 @RequestParam(value="existingAddress1",required=false)String theAddress,
													 @RequestParam(value="EmployeeLastName",required=false)String thename,
													 @RequestParam(value="EmployeeId",required=false)boolean theEmployeeID, 
													 @RequestParam(value="CustomerId",required=false)boolean theCustomerId,
													 @RequestParam(value="VendorId",required=false)boolean theVendorId, 
													 @RequestParam(value="ArchitectId",required=false)boolean theArchitectId,
													 @RequestParam(value="EngineerId",required=false)boolean theEngineerId,
													 @RequestParam(value="EmployeeName",required=false)String thefirstName,
													 @RequestParam(value="searchName",required=false)String thesearchName,
													 @RequestParam(value="officeExtension",required=false)String theOfficeExt,
													 @RequestParam(value="aEmployeeActive",required=false)boolean theActive,
													 @RequestParam(value="rxAddressId", required = false) Integer rxAddressId,
													 @RequestParam(value="OverrideOper", required = false) boolean OverrideOper,
													 HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		itsLogger.debug("Add New Customer");
			Rxmaster aCustomer = new Rxmaster();
			Rxaddress aRxaddress = new Rxaddress();
			Rxmaster aNewCustomer = null;
			try{
				String afirstName = "";
				String asearchName = "";
				boolean inActive = false;
				boolean IsStreet = false;
				boolean IsBillTo = true;
				aCustomer.setRxMasterId(theRxmasterId);
				aCustomer.setName(thename);
				if(theEmployeeID != true){
					aCustomer.setFirstName(afirstName);
				}else{
					aCustomer.setFirstName(thefirstName);
				}
				if(theEmployeeID != true){
					aCustomer.setSearchName(asearchName);
				}else{
					aCustomer.setSearchName(thesearchName);
				}
				aCustomer.setInActive(theActive);
				aCustomer.setPhone1(thePhone1);
				aCustomer.setPhone2(thePhone2);
				aCustomer.setFax(theFax);
				aCustomer.setPhone3(theOfficeExt);
				aCustomer.setIsCustomer(theCustomerId);
				aCustomer.setIsEmployee(theEmployeeID);
				aCustomer.setIsVendor(theVendorId);
				aCustomer.setIsCategory1(theArchitectId);
				aCustomer.setIsCategory2(theEngineerId);
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
				System.out.println("IsShipTo"+IsShipTo+"IsDefault"+IsDefault);
				aRxaddress.setIsShipTo(IsShipTo);
				
				aRxaddress.setIsStreet(IsStreet);
				aRxaddress.setIsBillTo(IsBillTo);
				aRxaddress.setOtherBillTo(2);
				aRxaddress.setCoTaxTerritoryId(theCountyId);
				aRxaddress.setOtherShipTo(3);
				aRxaddress.setPhone1(thePhone1);
				aRxaddress.setPhone2(thePhone2);
				aRxaddress.setFax(theFax);
				//itsRolodexService.updateCustomerInActive(theActive,theRxmasterId);
				/*Integer aRxAddressID = itsRolodexService.getRxAddressID(theRxmasterId,theAddress);*/
				/*if(aRxAddressID!=null){
					aRxaddress.setRxAddressId(aRxAddressID);
				}*/
				/*if(rxAddressId>0){
					aRxaddress.setRxAddressId(rxAddressId);
					aNewCustomer=null;
					System.out.println("insid the update");
					boolean updaterxaddress = itsCustomerService.updateRxaddress(aRxaddress);
				}else{*/
				aRxaddress.setRxAddressId(rxAddressId);
				itsLogger.info("Overrideoper"+OverrideOper);
				if(OverrideOper){
				aRxaddress.setIsDefault(true);
				} 
				else
				{
				aRxaddress.setIsDefault(IsDefault);
				}
				
				if(rxAddressId == null)
					aNewCustomer = itsRolodexService.addNewCustomerAddress(aCustomer, aRxaddress);
				else
					 itsRolodexService.updatedefaultstatusbasedoncustomer(aRxaddress);
					
					
				if(IsDefault && theCustomerId){
					Rxmaster arxm=new Rxmaster();
					
					if(aNewCustomer!=null)
					arxm.setRxMasterId(aNewCustomer.getRxMasterId());
					else
					arxm.setRxMasterId(theRxmasterId);
					arxm.setPhone1(thePhone1);
					arxm.setPhone2(thePhone2);
					arxm.setFax(theFax);
					arxm.setPhone3(theOfficeExt);
					boolean updatecuaddr=itsRolodexService.updateRxMasterDetails(arxm);
					
				}	
				//}
				itsLogger.debug("Query has to update customer Address"+aCustomer.getInActive());
				
			}catch (Exception e) {
				sendTransactionException("<b>rxAddressId:</b>"+rxAddressId,"Customer",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, e.getMessage());
			}
			return aNewCustomer;
	}
	
	@RequestMapping(value="/customerFirstNameList", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerFirstContactList(@RequestParam("term") String theFirstName,
			HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aCustomerFirstName = new ArrayList<AutoCompleteBean>();
		try{
			aCustomerFirstName=(ArrayList<AutoCompleteBean>) itsRolodexService.getCustomerFirstContactList(theFirstName);
		}catch(Exception e){
			sendTransactionException("<b>theFirstName:</b>"+theFirstName,"Customer",e,session,therequest);
		}
		return aCustomerFirstName;
	}
	
	@RequestMapping(value="/customerLastNameList", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerLastContactList(@RequestParam("term") String theLastName,
			HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		try{
			
		}catch(Exception e){
			sendTransactionException("<b>theLastName:</b>"+theLastName,"Customer",e,session,therequest);
		}
		ArrayList<AutoCompleteBean> aCustomerLastName = (ArrayList<AutoCompleteBean>) itsRolodexService.getCustomerLastContactList(theLastName);
		return aCustomerLastName;
	}
	
	@RequestMapping(value="/quickQuoteRevision", method = RequestMethod.GET)
	public @ResponseBody String QuickQuoteRevision( @RequestParam(value = "rxMasterID", required = false) Integer theRxMasterID,
																								@RequestParam(value = "rxContactID", required = false) Integer theRxContactID,
																								HttpSession session,HttpServletRequest therequest 	) throws IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		String aQuoteRev = "";
		try{
			aQuoteRev = itsRolodexService.getQuoteRev(theRxMasterID, theRxContactID);
			if(aQuoteRev == null){
				aQuoteRev = "";
			}
		}catch (Exception e) {
			sendTransactionException("<b>theRxMasterID,theRxContactID:</b>"+theRxMasterID+","+theRxContactID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aQuoteRev;
	}
	
	@RequestMapping(value="/QuoteHeaderID", method = RequestMethod.GET)
	public @ResponseBody JoQuoteHeader QuoteHeaderID( @RequestParam(value = "joMasterId", required = false) Integer thejoMasterId,
																										@RequestParam(value = "quoteRev", required = false) String theQuoteRev,
																										@RequestParam(value = "quoteTypeId", required = false) Integer theQuoteType,
																										HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		JoQuoteHeader theQuoteHeaderID = new JoQuoteHeader();
		JoQuoteHeader aQuoteHeaderID = new JoQuoteHeader();
		try{
			theQuoteHeaderID.setJoMasterID(thejoMasterId);
			theQuoteHeaderID.setCuMasterTypeID(theQuoteType);
			theQuoteHeaderID.setQuoteRev(theQuoteRev);
			aQuoteHeaderID = itsRolodexService.getQuoteHeadID(theQuoteHeaderID);
		}catch (Exception e) {
			sendTransactionException("<b>thejoMasterId:</b>"+thejoMasterId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aQuoteHeaderID;
	}
	
	@RequestMapping(value="/typeID", method = RequestMethod.GET)
	public @ResponseBody String typeID(@RequestParam(value = "quoteTypeId", required = false) Integer theQuoteType,HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		String aQuoteType = null;
		try{
			aQuoteType = itsRolodexService.getTypeID(theQuoteType);
		}catch (Exception e) {
			sendTransactionException("<b>theQuoteType:</b>"+theQuoteType,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aQuoteType;
	}
	
	@RequestMapping(value="/addQuoteHeaderID", method = RequestMethod.GET)
	public @ResponseBody JoQuoteHeader addQuoteHeaderID( @RequestParam(value = "joMasterId", required = false) Integer thejoMasterId,
														@RequestParam(value = "quoteRev", required = false) String theQuoteRev,
														@RequestParam(value = "quoteTypeId", required = false) Integer theQuoteType,
														HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		JoQuoteHeader theQuoteHeaderID = new JoQuoteHeader();
		JoQuoteHeader aQuoteHeaderID = new JoQuoteHeader();
		try{
			theQuoteHeaderID.setJoMasterID(thejoMasterId);
			theQuoteHeaderID.setCuMasterTypeID(theQuoteType);
			theQuoteHeaderID.setQuoteRev(theQuoteRev);
			aQuoteHeaderID = itsRolodexService.addQuoteHeaderId(theQuoteHeaderID);
		}catch (Exception e) {
			sendTransactionException("<b>thejoMasterId:</b>"+thejoMasterId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aQuoteHeaderID;
	}
	@RequestMapping(value="/printStatementConfirmation", method = RequestMethod.GET)
	public @ResponseBody List<StatementsBean> printStatement(@RequestParam(value = "stCustomerID", required = false) Integer stCustomerID,
			@RequestParam(value = "endCustomerID", required = false) Integer	 endCustomerID,
			@RequestParam(value = "startingCustomerName", required = false) String startCustomerName,
			@RequestParam(value = "endingCustomerName", required = false) String endcustomerName,
			@RequestParam(value = "exclusionDate", required = false) Date exclusionDate,
			@RequestParam(value = "statementDate", required = false) Date statementDate,
			@RequestParam(value = "warehouseInactive", required = false) Boolean showCredit,
			HttpSession session,HttpServletRequest therequest ) throws IOException, MessagingException{
		try{
			rxMasterIDArray = itsCustomerService.printSatatementsConfirmation(stCustomerID, endCustomerID, startCustomerName, endcustomerName, exclusionDate, showCredit);
		}catch (Exception e) {
			sendTransactionException("<b>stCustomerID:</b>"+stCustomerID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return rxMasterIDArray;
	}
	@RequestMapping(value="/printStatement", method = RequestMethod.GET)
	public @ResponseBody void typeID( @RequestParam(value = "filename", required = false) String fileName,
													@RequestParam(value = "rxMasterID", required = false) Integer rxMasterID,
													@RequestParam(value = "statementDate", required = false) Date statementDate,
													HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest ) throws SQLException, IOException, MessagingException{
		ServletOutputStream out=null;
		Connection connection =null;
	    ConnectionProvider connectionProvider = itspdfService.connectionForJasper();
		try {
				out = theResponse.getOutputStream();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("rxCustomerID",rxMasterID);
				params.put("statementDate", statementDate);
				theResponse.setHeader("Content-Disposition", "inline; filename="+"Statements.pdf");
				theResponse.setContentType("application/pdf");
				connection= connectionProvider.getConnection();
				String path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/sampleCustomerrepot.jrxml");
				JasperReport report = JasperCompileManager.compileReport(path_JRXML);
				JasperPrint print = JasperFillManager.fillReport(report, params, connection);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(print, baos);
				out.write(baos.toByteArray());
				
				
		} catch (IOException e) {
			sendTransactionException("<b>rxMasterID:</b>"+rxMasterID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (JRException e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			if(connectionProvider!=null){
				connectionProvider.closeConnection(connection);
				connectionProvider=null;
				}
			out.flush();
			out.close();
			
			
		}
	}
	@RequestMapping(value="/emailStatement", method = RequestMethod.GET)
	public @ResponseBody void sendMail( @RequestParam(value = "filename", required = false) String fileName,
													@RequestParam(value = "rxMasterID", required = false) Integer rxMasterID,
													@RequestParam(value="toEmailaddress", required= false) String theToAddr, 
													@RequestParam(value="StatementDate", required= false) Date statementDate, 
													HttpServletResponse theResponse,HttpServletRequest therequest, HttpSession session)  throws SQLException, IOException, MessagingException{
		File file = File.createTempFile("Statements", ".pdf");
	    ConnectionProvider connectionProvider = itspdfService.connectionForJasper();
	    Connection connection =null;
		try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("rxCustomerID",rxMasterID);
				params.put("statementDate", statementDate);
				connection = connectionProvider.getConnection();
				String path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/sampleCustomerrepot.jrxml");
				JasperReport report = JasperCompileManager.compileReport(path_JRXML);
				JasperPrint print = JasperFillManager.fillReport(report, params, connection);
				JasperExportManager.exportReportToPdfFile(print, file.getAbsolutePath());
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				TsUserLogin aTsUserLogin = itsUserService.getSingleUserDetails(aUserBean.getUserId());
				String aFromAddress 	=  aTsUserLogin.getEmailAddr();
				String aPassword 		=  aTsUserLogin.getLogOnPswd();
				String aPort =  aTsUserLogin.getSmtpport().toString();
				String ahost =  aTsUserLogin.getSmtpsvr();
				String aUserID 				=  aTsUserLogin.getLogOnName();
				SendQuoteMail.sendStatement(aFromAddress, theToAddr, aUserID, aPassword, aPort, ahost,file.getAbsolutePath());
		} catch (JRException e) {
			sendTransactionException("<b>rxMasterID:</b>"+rxMasterID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (UserException e) {
			sendTransactionException("<b>rxMasterID:</b>"+rxMasterID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (MessagingException e) {
			sendTransactionException("<b>rxMasterID:</b>"+rxMasterID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	    finally
	    {
	    	if(connectionProvider!=null){
	    		connectionProvider.closeConnection(connection);
	    		connectionProvider=null;
				}
	    }
	}
	@RequestMapping(value="/getCustomerTypes",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getCustomerTypes (HttpServletResponse theResponse,HttpServletRequest therequest, HttpSession session) throws IOException, MessagingException 
	{
		CustomResponse response=new CustomResponse();
		
		itsLogger.debug("Received request to get Customer Types");
		try{
			List<?> itsCuMasterType =itsCustomerService.getcuMasterType();
			response.setRows(itsCuMasterType);
		}catch (Exception e) {
			sendTransactionException("<b>MethodName:</b>getCustomerTypes","Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return response;	
	}
	
	@RequestMapping(value = "/saveCustomerTypes", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse editTerms(
			@RequestParam(value = "cuMasterTypeId", required = true) Integer cuMasterTypeId,
			@RequestParam(value = "typeCode", required = false) String typeCode,
			@RequestParam(value = "typeDescription", required = false) String description,
			@RequestParam(value = "typeActive", required = false) boolean inActive,
			HttpServletRequest therequest, HttpSession session,
			HttpServletResponse theResponse) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		itsLogger.info("MasterId: "+cuMasterTypeId);
		itsLogger.info("Type: "+typeCode);
		itsLogger.info("desc: "+description);
		itsLogger.info("Active: "+inActive);		
		try {
			itsCustomerService.updateCustomerTypes(cuMasterTypeId,typeCode,description,inActive);
		} catch (Exception e) {
			sendTransactionException("<b>cuMasterTypeId:</b>"+cuMasterTypeId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/deleteCustomerTypes", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse deleteTerms(
			@RequestParam(value = "cuMasterTypeId", required = true) Integer cuTypeId,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.info("/deleteCustomerTypes"+cuTypeId);
		CustomResponse response = new CustomResponse();
		try {
			itsCustomerService.deleteCustomerType(cuTypeId);
		} catch (Exception e) {
			sendTransactionException("<b>cuTypeId:</b>"+cuTypeId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return response;
	}
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Fetching the data for grid  from  rxaddress table in customer screen
	 */
	@RequestMapping(value="/getCustomerAddrList",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getCustomerAddrList (@RequestParam(value = "rolodexNumber", required = true) Integer theRolodexNumber,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException 
	{
		CustomResponse response=new CustomResponse();
		System.out.println("inside getCustomerAddrList"); 
		try{
			List<?> cuaddrlist =itsCustomerService.getCustomerAddressBasedonrxAddress(theRolodexNumber);
			response.setRows(cuaddrlist);
		}catch (Exception e) {
			sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return response;	
	}
	
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Fetching the data from rxaddress for edit popup from customer screen
	 */
	@RequestMapping(value="/getRxaddressDetails",method = RequestMethod.GET)
	public @ResponseBody Rxaddress getRxaddressDetails (@RequestParam(value = "rxAddressId", required = true) Integer rxAddressId,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException 
	{
		Rxaddress rxAddress=null;
		itsLogger.info("inside getRxaddressDetails"); 
		try{
			 rxAddress=itsCustomerService.getRxaddressDetails(rxAddressId);
		}catch (Exception e) {
			sendTransactionException("<b>rxAddressId:</b>"+rxAddressId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return rxAddress;	
	}
	
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Fetching the taxrate depends upon cotaxterritoryid from coTaxTerritory table
	 */
	@RequestMapping(value="/gettaxratedependsoncotax",method = RequestMethod.POST)
	public @ResponseBody CoTaxTerritory gettaxratedependsoncotax (@RequestParam(value = "coTaxTerritoryID", required = false) Integer coTaxTerritoryID,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException 
	{
		CoTaxTerritory aCoTaxTerritory=null;
		try{
			aCoTaxTerritory=itsCustomerService.gettaxratedependsoncotax(coTaxTerritoryID);
		}catch (Exception e) {
			sendTransactionException("<b>coTaxTerritoryID:</b>"+coTaxTerritoryID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return aCoTaxTerritory;	
	}
	
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Delete the customer address from rxaddress table
	 */
	@RequestMapping(value = "/deleteCustomerAddress", method = RequestMethod.GET)
	public @ResponseBody boolean deleteCustomerAddress(
			@RequestParam(value = "rxAddressId", required = true) Integer rxAddressId,
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse) throws IOException, MessagingException {
		itsLogger.info("/deleteCustomerAddress"+rxAddressId);
		try {
			itsCustomerService.deleteCustomerAddress(rxAddressId);
		} catch (Exception e) {
			sendTransactionException("<b>rxAddressId:</b>"+rxAddressId,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return true;
	}
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Default checkbox status in rxaddress table
	 */
	@RequestMapping(value="/Checkdefaultaddressstatus",method = RequestMethod.GET)
	public @ResponseBody boolean Checkdefaultaddressstatus (@RequestParam(value = "rolodexNumber", required = false) Integer theRolodexNumber,
			@RequestParam(value = "rxAddressID", required = false) String rxAddressid,HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException 
	
	{
		boolean checkdefaultstatus =false;
		CustomResponse response=new CustomResponse();
		try{
			int rxAddressID=0;
			/*if(rxAddressid!=null && rxAddressid!="false"){
				rxAddressID=Integer.parseInt(rxAddressid);
			}*/
			checkdefaultstatus =itsCustomerService.Checkdefaultaddressstatus(theRolodexNumber,rxAddressID);
		}catch (Exception e) {
			sendTransactionException("<b>theRolodexNumber,rxAddressID:</b>"+theRolodexNumber+","+rxAddressid,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		return checkdefaultstatus;	
	}
	@RequestMapping(value="/customerNameListEmployee", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerNameListFromEmployee(@RequestParam("term") String theCustomerName,
			@RequestParam(value = "tsUserLoginID", required = false)Integer tsUserLoginID,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		System.out.println("tsUserLoginID :: "+tsUserLoginID);
		ArrayList<AutoCompleteBean> aCustomerName = new ArrayList<AutoCompleteBean>();
		try{
			 aCustomerName = (ArrayList<AutoCompleteBean>) itsRolodexService.getCustomerNameListFromEmployee(theCustomerName,tsUserLoginID);
		}catch(Exception e){
			sendTransactionException("<b>theCustomerName</b>"+theCustomerName,"Customer",e,session,therequest);
		}
		
		return aCustomerName;
	}
	
	@RequestMapping(value="/getAllCustomerFromLogin", method = RequestMethod.GET)
	public @ResponseBody CustomResponse getAllFromLogin(@RequestParam(value="page", required=false) Integer thePage,
												@RequestParam(value="rows", required=false) Integer theRows,
												@RequestParam(value="sidx", required=false) String theSidx,
												@RequestParam(value="sord", required=false) String theSord,
												@RequestParam(value="tsUserLoginID", required=false) Integer tsUserLoginId,
												HttpSession session,HttpServletRequest therequest,
												HttpServletResponse response) throws IOException, MessagingException {
		
		
		int aTotalCount = itsRolodexService.getRecordCountFromEmployee(tsUserLoginId);
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			aUserLogin = new TsUserLogin();
			aUserLogin.setCustomerperpage(theRows);
			//aUserLogin.setUserLoginId(aUserBean.getUserId());
			
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			
			List<?> customer = itsRolodexService.getCustomersFromLogin(aFrom, aTo,tsUserLoginId);
			aResponse = new CustomResponse();
			aResponse.setRows(customer);
			aResponse.setRecords(String.valueOf(customer.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
		} catch (Exception e) {
			sendTransactionException("<b>tsUserLoginId</b>"+tsUserLoginId,"Customer",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}finally{
			aUserLogin = null;
		}
		return aResponse;
	}
	/*
	@RequestMapping(value="/printCustomerStatementfromajax", method = RequestMethod.POST)
	public @ResponseBody void printCustomerStatementfromajax(@RequestParam(value = "stCustomerID", required = false) Integer stCustomerID,
			@RequestParam(value = "rxMasterIDs[]", required = false) String[] rxMasterIdss,
			@RequestParam(value = "exclusiondate", required = false) String exclusiondate,
			@RequestParam(value = "isCredit", required = false) Integer isCredit,
			HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException
		{		
		String rxMasterIds = StringUtils.join(rxMasterIdss, ',');
		
		System.out.println("-->><<<2222>>><<<--("+rxMasterIds+")");
		
	String url ="/customerList/printCustomerStatement?rxMasterIDs=10600&exclusiondate="+ exclusiondate+"&isCredit="+isCredit;
		
		theResponse.sendRedirect(url);
		}*/
	
	
	/*@RequestMapping(value="/printCustomerStatement", method = RequestMethod.GET)
	public @ResponseBody void printCustomerStatement(@RequestParam(value = "stCustomerID", required = false) Integer stCustomerID,
			@RequestParam(value = "stCustomer", required = false) String stCustomer,
			@RequestParam(value = "endCustomer", required = false) String endCustomer,
			@RequestParam(value = "exclusiondate", required = false) String exclusiondate,
			@RequestParam(value = "isCredit", required = false) Integer isCredit,
			HttpServletResponse theResponse, HttpServletRequest theRequest,OutputStream out
		){
		try{
			
			System.out.println(endCustomer.trim());
			
		//	String rxMasterIds = StringUtils.join(rxMasterIdss, ',');
			
		//	System.out.println("-->><<<>>><<<--("+rxMasterIds+")");
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatement.jrxml");
			filename="ReissueCheck.pdf";
			
			ConnectionProvider con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("fromrxCustomerID", "");
			params.put("torxCustomerID", "");
			params.put("BeforeConversion", exclusiondate);
			Date date = new SimpleDateFormat("MM/dd/yyyy").parse(exclusiondate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			params.put("EXCLUDE_AFTER", sdf.format(date));
			Connection connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			String query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.QuickJobName ELSE J.Description END)  AS JobName"
					+ ",rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount,0) AS balance  ,(SELECT  Address1 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address1,(SELECT Address2 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address2,"
					+ " (SELECT City FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS City,(SELECT State FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1 ) AS State ,(SELECT Zip FROM rxAddress WHERE rxMasterID=I.rxCustomerID LIMIT 1 ) AS Zip"
					+ " FROM (joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID) LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
					+ " WHERE (I.InvoiceAmount - I.AppliedAmount > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1) AND (I.TransactionStatus > 0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ())  ORDER BY I.rxCustomerID";
			if(isCredit!=0){ 
				query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.QuickJobName ELSE J.Description END)  AS JobName"
						+ ",rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount,0) AS balance  ,(SELECT  Address1 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address1,(SELECT Address2 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address2,"
						+ " (SELECT City FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS City,(SELECT State FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1 ) AS State ,(SELECT Zip FROM rxAddress WHERE rxMasterID=I.rxCustomerID LIMIT 1 ) AS Zip"
						+ " FROM (joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID) LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ " WHERE  IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1) AND (I.TransactionStatus > 0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (rxm.Name>='"+stCustomer.trim()+"' and rxm.Name<='"+endCustomer.trim()+"')  ORDER BY I.rxCustomerID";
			}
			
			System.out.println("query::"+query);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			
			
			
			
			//ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
//			rxMasterIDArray = itsCustomerService.printSatatementsConfirmation(stCustomerID, endCustomerID, startCustomerName, endcustomerName, exclusionDate, showCredit);
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		
	}*/
	
	@RequestMapping(value="/printCustomerStatement1", method = RequestMethod.POST)
	public @ResponseBody void printCustomerStatement1(@RequestParam(value = "rxMasterIds", required = false) String rxMasterIds,
			@RequestParam(value = "exclusionDate", required = false) String exclusiondate,
			@RequestParam(value = "warehouseInactive", required = false) Integer isCredit,
			HttpServletResponse theResponse, HttpServletRequest theRequest
		){
		
		System.out.println("Entered!!!");
		System.out.println("rxMasterIds"+rxMasterIds);
		System.out.println("exclusiondate"+exclusiondate);
		System.out.println("isCredit"+isCredit);
		
		
	}
	
	/*@RequestMapping(value="/printCustomerStatement", method = RequestMethod.GET)
	public @ResponseBody void printCustomerStatement(@RequestParam(value = "stCustomerID", required = false) Integer stCustomerID,
			@RequestParam(value = "startingId", required = false) Integer	 startCustomerID,
			@RequestParam(value = "endingid", required = false) Integer endCustomerID,
			@RequestParam(value = "exclusiondate", required = false) String exclusiondate,
			@RequestParam(value = "isCredit", required = false) Integer isCredit,
			HttpServletResponse theResponse, HttpServletRequest theRequest
		){*/
	
	@RequestMapping(value="/printCustomerStatement", method = RequestMethod.POST)
	public @ResponseBody void printCustomerStatement(@RequestParam(value = "rxMasterIds", required = false) String rxMasterIds,
			@RequestParam(value = "exclusionDate", required = false) String exclusiondate,
			@RequestParam(value = "warehouseInactive", required = false) Integer isCredit,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session,
			HttpServletRequest theRequest
		) throws SQLException, IOException, MessagingException{
		
		Connection connection  =null;
		ConnectionProvider con =null;
		try{
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String subReport_JRXML=null;
			String filename=null;
			//path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatement.jrxml");
			filename="ReissueCheck.pdf";
			
			Sysvariable stmtgroupbyjob= itsEmployeeService.getSysVariableDetails("StatementsshallbegroupedbyJob");
			if(stmtgroupbyjob!=null){
				if(stmtgroupbyjob.getValueLong()>0)
					path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatementGroupJob.jrxml");
				else
					path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatement.jrxml");
			}
			
			File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
			String absolutePath = file.getAbsolutePath();
			absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
			String SubReportPath="";
			if (OperatingSystemInfo.isWindows()) {
				itsLogger.info("This is Windows");
				SubReportPath=absolutePath+"\\\\customerStatementSubReport.jasper";
			} else if (OperatingSystemInfo.isMac()) {
				itsLogger.info("This is Mac");
			} else if (OperatingSystemInfo.isUnix()) {
				itsLogger.info("This is Unix or Linux");
				SubReportPath=absolutePath+"/customerStatementSubReport.jasper";
			} else if (OperatingSystemInfo.isSolaris()) {
				itsLogger.info("This is Solaris");
			} else {
				itsLogger.info("Your OS is not support!!");
			}
				System.out.println("my value is "+stmtgroupbyjob.getValueLong());
			
				String reportdate=null;
				if(exclusiondate!=null && !exclusiondate.trim().equals("")){
					SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
				    Date convertedCurrentDate = sdf1.parse(exclusiondate);
				    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
				    reportdate=sdff.format(convertedCurrentDate );
				    System.out.println("Formated End Date:"+reportdate);
				}
				
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("fromrxCustomerID", "");
			params.put("torxCustomerID", "");
			params.put("BeforeConversion", exclusiondate);
			Date date = new SimpleDateFormat("MM/dd/yyyy").parse(exclusiondate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			params.put("EXCLUDE_AFTER", sdf.format(date));
			params.put("reportdate", reportdate);
			params.put("SubReportPath", SubReportPath);
			System.out.println(sdf.format(date)+" :: "+""+" "+"" );
			
			
			connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			String query ="";
			System.out.println("RxMaster ID " + rxMasterIds);
			String[] count =rxMasterIds.split(",");
			if(count.length==1){
				System.out.println("Single ID");
				params.put("pagenoDisp", true);
			}else{
				params.put("pagenoDisp", false);
			}
			
			//JRDesignQuery subQuery = new JRDesignQuery();
			
			String subQueryString="";
		
			if(rxMasterIds.equals("-1")){
				/*
				 * Commented By: Naveed
				 * query =  "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',"
						+ " I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.QuickJobName ELSE J.Description END)  AS JobName"
						+ " ,rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount+I.DiscountAmt,0) AS balance  ,"
						+ " rxA.Address1 AS Address1,rxA.Address2 AS Address2,rxA.City  AS City,rxA.State AS State ,"
						+ "  rxA.Zip AS Zip,tst.companyLogo,tst.HeaderText"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID)"
						+ "  LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  LEFT JOIN rxAddress rxA ON rxA.rxMasterID=I.rxCustomerID "
						+ "  WHERE (ABS(I.InvoiceAmount) - ABS(I.AppliedAmount+I.DiscountAmt) > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1  AND IsCredit<>1) AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') GROUP BY I.cuInvoiceID ORDER BY CustomerName,JobNumber";*/
				
		
				
				
				query =  "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',"
						+ " I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.jobnodescription ELSE J.Description END)  AS JobName"
						+ " ,rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount+(IFNULL(I.DiscountAmt,0)),0) AS balance  ,"
						+ " rxA.Address1 AS Address1,rxA.Address2 AS Address2,rxA.City  AS City,rxA.State AS State ,"
						+ "  rxA.Zip AS Zip,tst.companyLogo,tst.HeaderText,tst.remitToAddress1 as remitAddress1,tst.remitToAddress2 as remitAddress2,tst.remitToCity as remitCity,tst.remitToState as remitState,tst.remitToZip as remitZip,tst.remitToDescription as remitDescription"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID)"
						+ "  LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  LEFT JOIN rxAddress rxA ON rxA.rxMasterID=I.rxCustomerID "
						+ "  WHERE (ABS(I.InvoiceAmount) - ABS(I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1  AND IsCredit<>1) AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND rxm.`InActive`=0 GROUP BY I.cuInvoiceID ORDER BY CustomerName,JobNumber";
				
				//subQueryString="SELECT ifnull(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, ifnull(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, ifnull(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60, ifnull(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+DiscountAmt) AS Balance, DATEDIFF('"+sdf.format(date)+"',InvoiceDate) AS Days FROM cuInvoice WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1  AND IsCredit<>1) AND TransactionStatus>0 AND (ABS(InvoiceAmount-(AppliedAmount+DiscountAmt)) > .01) AND (rxCustomerID=$P{rxCustomerID} ) AND (InvoiceDate <= '"+sdf.format(date)+"') ORDER BY rxCustomerID) AS subquery";
				subQueryString="SELECT ifnull(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, ifnull(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, ifnull(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60, ifnull(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0))) AS Balance, DATEDIFF('"+sdf.format(date)+"',InvoiceDate) AS Days FROM cuInvoice WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1  AND IsCredit<>1) AND TransactionStatus>0 AND (ABS(InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0)))) > .01) AND (rxCustomerID=$P{rxCustomerID} ) AND (InvoiceDate <= '"+sdf.format(date)+"') ORDER BY rxCustomerID) AS subquery";
			}
			else{
				query =  "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',"
						+ " I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.jobnodescription ELSE J.Description END)  AS JobName"
						+ " ,rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL((I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),0) AS balance  ,"
						+ " rxA.Address1 AS Address1,rxA.Address2 AS Address2,rxA.City  AS City,rxA.State AS State ,"
						+ "  rxA.Zip AS Zip,tst.companyLogo,tst.HeaderText,tst.remitToAddress1 as remitAddress1,tst.remitToAddress2 as remitAddress2,tst.remitToCity as remitCity,tst.remitToState as remitState,tst.remitToZip as remitZip,tst.remitToDescription as remitDescription"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID)"
						+ "  LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  LEFT JOIN rxAddress rxA ON rxA.rxMasterID=I.rxCustomerID "
						+ "  WHERE (ABS(I.InvoiceAmount) - ABS((I.AppliedAmount+(IFNULL(I.DiscountAmt,0)))) > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1 AND IsCredit<>1) AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxMasterIds+")) GROUP BY I.cuInvoiceID ORDER BY CustomerName,JobNumber";
				
				subQueryString="SELECT ifnull(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, ifnull(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, ifnull(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60, ifnull(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0))) AS Balance, DATEDIFF('"+sdf.format(date)+"',InvoiceDate) AS Days FROM cuInvoice WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1  AND IsCredit<>1) AND TransactionStatus>0 AND (ABS(InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0)))) > .01) AND (rxCustomerID=$P{rxCustomerID} ) AND (InvoiceDate <= '"+sdf.format(date)+"') ORDER BY rxCustomerID) AS subquery";
			}
				if(isCredit!=null && rxMasterIds.equals("-1")){ 
				query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',"
						+ " I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.jobnodescription ELSE J.Description END)  AS JobName"
						+ " ,rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL((I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),0) AS balance  ,"
						+ " rxA.Address1 AS Address1,rxA.Address2 AS Address2,rxA.City  AS City,rxA.State AS State ,"
						+ "  rxA.Zip AS Zip,tst.companyLogo,tst.HeaderText,tst.remitToAddress1 as remitAddress1,tst.remitToAddress2 as remitAddress2,tst.remitToCity as remitCity,tst.remitToState as remitState,tst.remitToZip as remitZip,tst.remitToDescription as remitDescription"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID)"
						+ "  LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  LEFT JOIN rxAddress rxA ON rxA.rxMasterID=I.rxCustomerID "
						+ "  WHERE IF(I.CreditMemo = 1,I.memoStatus=1 AND I.InvoiceAmount <> (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) > 0.01 OR (I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) < -0.01))  AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND rxm.`InActive`=0  GROUP BY I.cuInvoiceID ORDER BY CustomerName,JobNumber";
						
				subQueryString="SELECT ifnull(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, ifnull(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, ifnull(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60 , ifnull(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0))) AS Balance, DATEDIFF('"+sdf.format(date)+"',InvoiceDate) Days FROM cuInvoice WHERE IF(CreditMemo = 1,memoStatus=1 AND InvoiceAmount <> (AppliedAmount+(IFNULL(DiscountAmt,0))),ABS(InvoiceAmount - (AppliedAmount+(IFNULL(DiscountAmt,0))))  > 0.01  OR (InvoiceAmount - (AppliedAmount+(IFNULL(DiscountAmt,0))) < -0.01) ) AND TransactionStatus>0  AND (rxCustomerID=$P{rxCustomerID} ) AND (InvoiceDate <= '"+sdf.format(date)+"') ORDER BY rxCustomerID) AS subquery";
			}else if(isCredit!=null){
				query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',"
						+ " I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.jobnodescription ELSE J.Description END)  AS JobName"
						+ " ,rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL((I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),0) AS balance  ,"
						+ " rxA.Address1 AS Address1,rxA.Address2 AS Address2,rxA.City  AS City,rxA.State AS State ,"
						+ "  rxA.Zip AS Zip,tst.companyLogo,tst.HeaderText,tst.remitToAddress1 as remitAddress1,tst.remitToAddress2 as remitAddress2,tst.remitToCity as remitCity,tst.remitToState as remitState,tst.remitToZip as remitZip,tst.remitToDescription as remitDescription"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID)"
						+ "  LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  LEFT JOIN rxAddress rxA ON rxA.rxMasterID=I.rxCustomerID "
						+ "  WHERE IF(I.CreditMemo = 1,I.memoStatus=1 AND I.InvoiceAmount <> (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),(I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) > 0.01) OR (I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) < -0.01))  AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxMasterIds+")) GROUP BY I.cuInvoiceID ORDER BY CustomerName,JobNumber";
						
				subQueryString="SELECT ifnull(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, ifnull(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, ifnull(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60 , ifnull(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0))) AS Balance, DATEDIFF('"+sdf.format(date)+"',InvoiceDate) Days FROM cuInvoice WHERE IF(CreditMemo = 1,memoStatus=1 AND InvoiceAmount <> (AppliedAmount+(IFNULL(DiscountAmt,0))),ABS(InvoiceAmount - (AppliedAmount+(IFNULL(DiscountAmt,0))))  > 0.01  OR (InvoiceAmount - (AppliedAmount+(IFNULL(DiscountAmt,0))) < -0.01) ) AND TransactionStatus>0  AND (rxCustomerID=$P{rxCustomerID} ) AND (InvoiceDate <= '"+sdf.format(date)+"') ORDER BY rxCustomerID) AS subquery";
		
			}
				
			/*if(rxMasterIds.equals("-1")){
				query =   "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.QuickJobName ELSE J.Description END)  AS JobName"
						+ ",rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount,0) AS balance  ,(SELECT  Address1 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address1,(SELECT Address2 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address2,"
						+ " (SELECT City FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS City,(SELECT State FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1 ) AS State ,(SELECT Zip FROM rxAddress WHERE rxMasterID=I.rxCustomerID LIMIT 1 ) AS Zip,tst.companyLogo,tst.HeaderText"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID) LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ " WHERE (I.InvoiceAmount - I.AppliedAmount > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1) AND (I.TransactionStatus > 0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') ORDER BY CustomerName,JobNumber";
			}
			else{
				query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.QuickJobName ELSE J.Description END)  AS JobName"
					+ ",rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount,0) AS balance  ,(SELECT  Address1 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address1,(SELECT Address2 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address2,"
					+ " (SELECT City FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS City,(SELECT State FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1 ) AS State ,(SELECT Zip FROM rxAddress WHERE rxMasterID=I.rxCustomerID LIMIT 1 ) AS Zip,tst.companyLogo,tst.HeaderText"
					+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID) LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
					+ " WHERE (I.InvoiceAmount - I.AppliedAmount > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1) AND (I.TransactionStatus > 0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxMasterIds+")) ORDER BY CustomerName,JobNumber";
			}
				if(isCredit!=null){ 
				query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.QuickJobName ELSE J.Description END)  AS JobName"
						+ ",rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL(I.AppliedAmount,0) AS balance  ,(SELECT  Address1 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address1,(SELECT Address2 FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS Address2,"
						+ " (SELECT City FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1) AS City,(SELECT State FROM rxAddress WHERE rxMasterID=I.rxCustomerID  LIMIT 1 ) AS State ,(SELECT Zip FROM rxAddress WHERE rxMasterID=I.rxCustomerID LIMIT 1 ) AS Zip,tst.companyLogo,tst.HeaderText"
						+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID) LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ " WHERE  IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1) AND (I.TransactionStatus > 0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxMasterIds+")) ORDER BY CustomerName,JobNumber";
			}*/
			
			System.out.println("query::"+query);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			
			System.out.println("subQueryString"+subQueryString);
			//subQuery.setText(subQueryString);
			
			subReport_JRXML=therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/customerStatementSubReport.jrxml");
			JasperDesign jdsb  = JRXmlLoader.load(subReport_JRXML);
			JRDesignQuery subReportQuery = new JRDesignQuery();
			subReportQuery.setText(subQueryString);
			jdsb.setQuery(subReportQuery);
			JasperCompileManager.compileReportToFile(jdsb, SubReportPath);
			/*Map<String, JRDataset> datasetMap = jd.getDatasetMap();
			JRDesignDataset subDataset = (JRDesignDataset) datasetMap.get("Dataset query");
			subDataset.setQuery(subQuery);	*/
			
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			
			
			
			//ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
//			rxMasterIDArray = itsCustomerService.printSatatementsConfirmation(stCustomerID, endCustomerID, startCustomerName, endcustomerName, exclusionDate, showCredit);
		}catch (Exception e) {
			sendTransactionException("<b>rxMasterIds</b>"+rxMasterIds,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				} 
		}
		
	}
	
	
	
	@RequestMapping(value="/getStatementPDF", method = RequestMethod.POST)
	public @ResponseBody void getStatementPDF(
			@RequestParam(value = "rxMasterIds", required = false) String rxMasterIds,
			@RequestParam(value = "exclusionDate", required = false) String exclusiondate,
			@RequestParam(value = "warehouseInactive", required = false) Integer isCredit,
			@RequestParam(value = "cuInvoiceIdArray[]",required =false) ArrayList<String> cuInvoiceList,
			@RequestParam(value = "rxCustomerIdArray[]",required =false) ArrayList<String> rxCustomerIdArray,
			@RequestParam(value = "emailListArray[]",required =false) ArrayList<String> emailListArray,
			HttpServletResponse theResponse, HttpServletRequest theRequest, HttpSession session
		) throws IOException, MessagingException{
		Connection connection =null;TsUserSetting	objtsusersettings=null;
		InputStream imageStream =null;
		ConnectionProvider con = null;
		Boolean emailStatus=false;
		
		System.out.println("rxMasterIds"+rxMasterIds+"exclusionDate"+exclusiondate+"warehouseInactive"+isCredit);
		System.out.println("cuInvoiceList"+cuInvoiceList);
		System.out.println("rxCustomerIdArray"+rxCustomerIdArray);
		System.out.println("emailListArray"+emailListArray);
		int k=0;
		try{
			
			UserBean aUserBean = (UserBean)session.getAttribute("user");
		    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
		    String aFromAddress = aTsUserLogin.getEmailAddr();
		    
		   // String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
		    String aUserID = aTsUserLogin.getLogOnName();
		    String aPassword = aTsUserLogin.getLogOnPswd();
		    String aPort = aTsUserLogin.getSmtpport().toString();
		    String ahost = aTsUserLogin.getSmtpsvr();
            
		    
		    
		    
		    
		    EmailParameters eParam=new EmailParameters();
		    

			eParam.seteUserID(aUserID);
			eParam.setePassword(aPassword);
			eParam.setEhost(ahost);
			eParam.setePort(aPort);
			eParam.seteSubject("Turbo Pro - Customer Statement Report");
			eParam.seteContent("Turbo Pro - Customer Statement Report");
			eParam.setFromAddress(aFromAddress);
			 HashMap<Integer, ArrayList<Cuinvoice>> hashmap = 
				        new HashMap<Integer, ArrayList<Cuinvoice>>();
			 int i=0;
			for(String rxcustomerID:rxCustomerIdArray){
				if(hashmap.get(Integer.parseInt(rxcustomerID))!=null){
					ArrayList<Cuinvoice> invlst= hashmap.get(Integer.parseInt(rxcustomerID));
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(Integer.parseInt(rxcustomerID));
					newobj.setCuInvoiceId(Integer.parseInt(cuInvoiceList.get(i)));
					newobj.setEmailList(emailListArray.get(i));
					invlst.add(newobj);
					hashmap.put(Integer.parseInt(rxcustomerID), invlst);
				}else{
					ArrayList<Cuinvoice> invlst= new ArrayList<Cuinvoice>();
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(Integer.parseInt(rxcustomerID));
					newobj.setCuInvoiceId(Integer.parseInt(cuInvoiceList.get(i)));
					newobj.setEmailList(emailListArray.get(i));
					invlst.add(newobj);
					hashmap.put(Integer.parseInt(rxcustomerID), invlst);
				}
				i=i+1;
			}
			
			for (Entry<Integer, ArrayList<Cuinvoice>> entry : hashmap.entrySet()) {
			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			    HashMap<String, ArrayList<Cuinvoice>> custbasedemailmap = 
				        new HashMap<String, ArrayList<Cuinvoice>>();
			    for(Cuinvoice thecuinv:entry.getValue()){
				if(custbasedemailmap.get(thecuinv.getEmailList())!=null){
					ArrayList<Cuinvoice> invlst= custbasedemailmap.get(thecuinv.getEmailList());
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(thecuinv.getRxCustomerId());
					newobj.setCuInvoiceId(thecuinv.getCuInvoiceId());
					newobj.setEmailList(thecuinv.getEmailList());
					invlst.add(newobj);
					custbasedemailmap.put(thecuinv.getEmailList(), invlst);
				}else{
					ArrayList<Cuinvoice> invlst= new ArrayList<Cuinvoice>();
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(thecuinv.getRxCustomerId());
					newobj.setCuInvoiceId(thecuinv.getCuInvoiceId());
					newobj.setEmailList(thecuinv.getEmailList());
					invlst.add(newobj);
					custbasedemailmap.put(thecuinv.getEmailList(), invlst);
				}
			    } 
			for (Entry<String, ArrayList<Cuinvoice>> entryobj : custbasedemailmap.entrySet()) { 
				String cuinvoiceID=null;
				Integer rxcustomerID=0;
				String emailID=entryobj.getKey();
				for(Cuinvoice thecuinv:entryobj.getValue()){
					rxcustomerID=thecuinv.getRxCustomerId();
					if(cuinvoiceID==null){
						cuinvoiceID=thecuinv.getCuInvoiceId()+"";
					}else{
						cuinvoiceID=cuinvoiceID+","+thecuinv.getCuInvoiceId();
					}
				}
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				String path_JRXML =null;
				String subReport_JRXML=null;
				String filename=null;
				//path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatement.jrxml");
				filename="CustomerStatement_"+aUserBean.getUserId()+"_"+k+".pdf";
				
				Sysvariable stmtgroupbyjob= itsEmployeeService.getSysVariableDetails("StatementsshallbegroupedbyJob");
				if(stmtgroupbyjob!=null){
					if(stmtgroupbyjob.getValueLong()>0)
						path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatementGroupJob.jrxml");
					else
						path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CustomerStatement.jrxml");
				}
				
				File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
				String absolutePath = file.getAbsolutePath();
				absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
				String SubReportPath="";
				if (OperatingSystemInfo.isWindows()) {
					itsLogger.info("This is Windows");
					SubReportPath=absolutePath+"\\\\customerStatementSubReport.jasper";
				} else if (OperatingSystemInfo.isMac()) {
					itsLogger.info("This is Mac");
				} else if (OperatingSystemInfo.isUnix()) {
					itsLogger.info("This is Unix or Linux");
					SubReportPath=absolutePath+"/customerStatementSubReport.jasper";
				} else if (OperatingSystemInfo.isSolaris()) {
					itsLogger.info("This is Solaris");
				} else {
					itsLogger.info("Your OS is not support!!");
				}
					System.out.println("my value is "+stmtgroupbyjob.getValueLong());
				
					String reportdate=null;
					if(exclusiondate!=null && !exclusiondate.trim().equals("")){
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
					    Date convertedCurrentDate = sdf1.parse(exclusiondate);
					    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
					    reportdate=sdff.format(convertedCurrentDate );
					    System.out.println("Formated End Date:"+reportdate);
					}
					
				con = itspdfService.connectionForJasper();
				//Have to set Params
				params.put("fromrxCustomerID", "");
				params.put("torxCustomerID", "");
				params.put("BeforeConversion", exclusiondate);
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(exclusiondate);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				params.put("EXCLUDE_AFTER", sdf.format(date));
				params.put("reportdate", reportdate);
				params.put("SubReportPath", SubReportPath);
				System.out.println(sdf.format(date)+" :: "+""+" "+"" );
				
				
				connection = con.getConnection();
				JasperDesign jd  = JRXmlLoader.load(path_JRXML);
				String query ="";
				System.out.println("RxMaster ID " + rxMasterIds);
				String[] count =rxMasterIds.split(",");
				if(count.length==1){
					System.out.println("Single ID");
					params.put("pagenoDisp", true);
				}else{
					params.put("pagenoDisp", false);
				}
				
				//JRDesignQuery subQuery = new JRDesignQuery();
				
				String subQueryString="";
			

					query =  "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber,DATEDIFF('"+sdf.format(date)+"',"
							+ " I.InvoiceDate) AS Aging, J.joMasterID, (CASE WHEN J.Description IS NULL THEN I.jobnodescription ELSE J.Description END)  AS JobName"
							+ " ,rxm.Name AS CustomerName,IFNULL(I.InvoiceAmount,0)-IFNULL((I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),0) AS balance  ,"
							+ " rxA.Address1 AS Address1,rxA.Address2 AS Address2,rxA.City  AS City,rxA.State AS State ,"
							+ "  rxA.Zip AS Zip,tst.companyLogo,tst.HeaderText,tst.remitToAddress1 as remitAddress1,tst.remitToAddress2 as remitAddress2,tst.remitToCity as remitCity,tst.remitToState as remitState,tst.remitToZip as remitZip,tst.remitToDescription as remitDescription"
							+ " FROM tsUserSetting tst,(joReleaseDetail AS R RIGHT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID)"
							+ "  LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
							+ "  LEFT JOIN rxAddress rxA ON rxA.rxMasterID=I.rxCustomerID "
							+ "  WHERE (ABS(I.InvoiceAmount) - ABS((I.AppliedAmount+(IFNULL(I.DiscountAmt,0)))) > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1 AND IsCredit<>1) AND " 
							+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxcustomerID+")) GROUP BY I.cuInvoiceID ORDER BY CustomerName,JobNumber";
					
					subQueryString="SELECT ifnull(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, ifnull(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, ifnull(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60, ifnull(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0))) AS Balance, DATEDIFF('"+sdf.format(date)+"',InvoiceDate) AS Days FROM cuInvoice WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1  AND IsCredit<>1) AND TransactionStatus>0 AND (ABS(InvoiceAmount-(AppliedAmount+(IFNULL(DiscountAmt,0)))) > .01) AND (rxCustomerID=$P{rxCustomerID} ) AND (InvoiceDate <= '"+sdf.format(date)+"') ORDER BY rxCustomerID) AS subquery";
		
			System.out.println("query::"+query);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			//ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			ReportService.dynamicWriteReportCall(theRequest,theResponse,params,"pdf",jd,filename,connection);

			eParam.setToAddress(emailID);
			eParam.setWriteFileName(filename);
			eParam.setFileName("CustomerStatement.pdf");
			SendQuoteMail sendBulkMail=new SendQuoteMail();
			emailStatus=sendBulkMail.sendBulkMailAttachment(eParam,theRequest);
		     
			if(emailStatus==true){
				for(Cuinvoice updateEmailDate:entryobj.getValue()){
				     itsPurchaseService.updateEmailInvoiceDate(updateEmailDate.getCuInvoiceId(),1,emailStatus);
				}
			}
			
			k=k+1;
			}
			}
		}catch (Exception e) {
			//logger.error(e.getMessage(), e);
		//	sendTransactionException("<b>joReleaseId:</b>"+batchInvoiceCuID,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				objtsusersettings=null;
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@RequestMapping(value="/customerdeleteConfirmation",method = RequestMethod.POST)
	public @ResponseBody boolean customerdeleteConfirmation(@RequestParam(value = "rxCustomerID", required = false) int rxCustomerID) {

		boolean availabilityStatus=false;
		
		System.out.println(rxCustomerID);
		
		availabilityStatus =  itsRolodexService.checkCustomeravailability(rxCustomerID);
		
		if(!availabilityStatus)
		{
			itsRolodexService.deleteCustomer(rxCustomerID);
		}
				
		return availabilityStatus;
	}
	
	@RequestMapping(value="/customerInactive",method = RequestMethod.POST)
	public @ResponseBody boolean customerInactive(@RequestParam(value = "rxCustomerID", required = false) int rxCustomerID) {

		boolean inactiveStatus=true;
		
		itsRolodexService.updateCustomerInActive(true, rxCustomerID);
				
		return inactiveStatus;
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
	
	public String getCustomersCount(){
		Session aSession = null;
		List<RolodexBean> aQueryList = new ArrayList<RolodexBean>();
		String aCustomerQry = "SELECT " +
												"rxMaster.rxMasterID " +
												
												/*"(SELECT city FROM rxAddress WHERE rxMasterID=rxMaster.rxMasterID ORDER BY rxAddressID  LIMIT 1) AS city,"+
												" (SELECT state FROM rxAddress WHERE rxMasterID=rxMaster.rxMasterID ORDER BY rxAddressID LIMIT 1) AS state,"+
												" (SELECT Address1 FROM rxAddress WHERE rxMasterID=rxMaster.rxMasterID ORDER BY rxAddressID LIMIT 1) AS Address1 "+*/
												"FROM rxMaster " +
												"LEFT JOIN rxAddress on (rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.IsDefault=1) " +
												"WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT null AND rxMaster.name <> '(missing)' AND rxMaster.name <> ''" +
												"Group by rxMaster.rxMasterID ORDER BY rxMaster.name" ;
		
		return aCustomerQry;	
	}
	
	@RequestMapping(value="/updateotherrxaddress",method = RequestMethod.POST)
	public @ResponseBody boolean updateotherrxaddress(@RequestParam(value = "rxCustomerID", required = false) int rxCustomerID) {

		
		itsRolodexService.updateotherrxaddress(rxCustomerID);
				
		return true;
	}
	
	@RequestMapping(value = "newEmployeeAddress", method = RequestMethod.POST)
	public @ResponseBody Rxmaster addEmployeeAddress( @RequestParam(value = "locationAddress1", required = false) String theAddress1,
													 @RequestParam(value = "locationAddress2", required = false) String theAddress2, 
													 @RequestParam(value = "locationState", required = false) String theState,
													 @RequestParam(value = "locationZip", required = false) String thePinCode, 
													 @RequestParam(value = "USPhoneNumber", required = false) String thePhone1,
													 @RequestParam(value = "USPhone_Number", required = false) String thePhone2, 
													 @RequestParam(value = "fax", required = false) String theFax,
													 @RequestParam(value = "CountyId", required = false) Integer theCountyId,
													 @RequestParam(value="isMailing",required=false)boolean IsMailing,
													 @RequestParam(value="isShipping",required=false)boolean IsShipTo,
													 @RequestParam(value="isDefault",required=false)boolean IsDefault,
													 @RequestParam(value = "locationCity", required = false) String theCity,
													 @RequestParam(value="rolodexNumber", required = false) Integer theRxmasterId,
													 @RequestParam(value="existingAddress1",required=false)String theAddress,
													 @RequestParam(value="EmployeeLastName",required=false)String thename,
													 @RequestParam(value="EmployeeId",required=false)boolean theEmployeeID, 
													 @RequestParam(value="CustomerId",required=false)boolean theCustomerId,
													 @RequestParam(value="VendorId",required=false)boolean theVendorId, 
													 @RequestParam(value="ArchitectId",required=false)boolean theArchitectId,
													 @RequestParam(value="EngineerId",required=false)boolean theEngineerId,
													 @RequestParam(value="EmployeeName",required=false)String thefirstName,
													 @RequestParam(value="searchName",required=false)String thesearchName,
													 @RequestParam(value="officeExtension",required=false)String theOfficeExt,
													 @RequestParam(value="aEmployeeActive",required=false)boolean theActive,
													 @RequestParam(value="rxAddressId", required = false) Integer rxAddressId,
													 @RequestParam(value="OverrideOper", required = false) boolean OverrideOper,
													 HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		itsLogger.debug("Add New Customer");
			Rxmaster aCustomer = new Rxmaster();
			Rxaddress aRxaddress = new Rxaddress();
			Rxmaster aNewCustomer = null;
			try{
					Rxmaster arxm=new Rxmaster();
					
					arxm.setRxMasterId(theRxmasterId);
					arxm.setName(thename);
					arxm.setFirstName(thefirstName);
					arxm.setPhone1(thePhone1);
					arxm.setPhone2(thePhone2);
					arxm.setFax(theFax);
					arxm.setPhone3(theOfficeExt);
					arxm.setInActive(theActive);
					boolean updatecuaddr=itsRolodexService.updateRxMasterfromempDetails(arxm);
					if(rxAddressId!=null){
						Rxaddress aRxAddress=new Rxaddress();
						aRxAddress.setAddress1(theAddress1);
						aRxAddress.setAddress2(theAddress2);
						aRxAddress.setCity(theCity);
						aRxAddress.setState(theState);
						aRxAddress.setZip(thePinCode);
						aRxAddress.setRxAddressId(rxAddressId);
						
						boolean address=itsRolodexService.updateRxaddress(aRxAddress);
					}
					
					
					
				itsLogger.debug("Query has to update customer Address"+aCustomer.getInActive());
				
			}catch (Exception e) {
				sendTransactionException("<b>rxAddressId:</b>"+rxAddressId,"Customer",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, e.getMessage());
			}
			return aNewCustomer;
	}
	
	@RequestMapping(value = "/getAllCustomerEmail", method = RequestMethod.GET)
	public @ResponseBody
	String getAllCustomerEmail(
			@RequestParam(value = "rxCustomerId", required = false) String rxCustomerId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		System.out.println("RxCustomerId"+rxCustomerId);
	    String customerEmailList=null;
		int rCuID=0;
		try {
			if (rxCustomerId != null) {
				rCuID=Integer.parseInt(rxCustomerId);
				
				customerEmailList=	itsCustomerService.getAllCustomerEmail(rCuID,0);
			//	aCuso = new Cuso();
			//	aCuso = jobService.getCUSOobjFromSONumber(theSONumber);
			}
		} catch (Exception e) {
		//	logger.error(e.getMessage(), e);
			//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			//sendTransactionException("<b>theSONumber:</b>"+theSONumber,"SalesOrderController",e,session,theRequest);
		}
		return customerEmailList ;
	}
}