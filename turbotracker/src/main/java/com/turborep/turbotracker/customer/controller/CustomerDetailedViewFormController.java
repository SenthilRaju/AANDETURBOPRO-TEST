package com.turborep.turbotracker.customer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.RxCustomerTabViewBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Rxmastercategory1;
import com.turborep.turbotracker.employee.dao.Rxmastercategory2;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JobCustomerBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.service.PurchaseService;

@Controller
@RequestMapping("/rxdetailedviewtabs")
public class CustomerDetailedViewFormController {
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	Logger logger = Logger.getLogger(CustomerDetailedViewFormController.class); 

	private Cumaster itsCustomerRecord;
	
	private List<Prwarehouse> itsDefaultWherehouse;
	private List<CuTerms> itsPaymentTerms;
	private List<CuMasterType> itsCuMasterType;
	private List<Codivision> itsDivision;
	
	private String itsTableNameUserLogin="tsUserLogin";
	
	/**
	 * Getting the services
	 */
	@Resource(name="salesServices")
	private SalesService itsEmpAssignedService;
	
	@Resource(name = "customerService")
	private CustomerService itsCuMasterService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	//Added to get Customer Address for SO
	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;
	/*
	 * Adding Customer due and balance from customer Invoice
	 * 
	 */
	@RequestMapping(value = "rolodexCustomer", method = RequestMethod.GET)
	public String getCustomerPage (@RequestParam(value="rolodexID", required=false) Integer theRolodexNumber, HttpSession session,HttpServletRequest therequest, ModelMap theModel) throws CompanyException, IOException, MessagingException {
		logger.debug("Received request to show generalCustomers page");
		String aAssignedSalesRep = null;
		String aAssignedCSR = null;
		String aAssignedSalesMGR = null;
		String aAssignedEngineers = null;
		String aAssignedProjMgr = null;
		setItsCuMasterType((List<CuMasterType>)itsCuMasterService.getcuMasterType());
		if(theRolodexNumber != null){
		Integer aRxMasterId = theRolodexNumber;
			setCustomerRecord(aRxMasterId);
			System.out.println("itsCustomerRecord====>"+itsCustomerRecord);
			if(itsCustomerRecord != null){
				RxCustomerTabViewBean aRxCustomerTabViewBean = new RxCustomerTabViewBean();
				if(itsCustomerRecord.getCuAssignmentId0() != null){
					aAssignedSalesRep = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId0(), itsTableNameUserLogin);
					aRxCustomerTabViewBean.setAssignedSalesRep(aAssignedSalesRep);
				}
				if(itsCustomerRecord.getCuAssignmentId1() != null){
					aAssignedCSR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId1(), itsTableNameUserLogin);
					aRxCustomerTabViewBean.setAssignedCSRs(aAssignedCSR);
				}
				if(itsCustomerRecord.getCuAssignmentId2() != null){
					aAssignedSalesMGR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId2(), itsTableNameUserLogin);
					aRxCustomerTabViewBean.setAssignedSalesMGRs(aAssignedSalesMGR);
				}
				if(itsCustomerRecord.getCuAssignmentId3() != null){
					aAssignedEngineers = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId3(), itsTableNameUserLogin);
					aRxCustomerTabViewBean.setAssignedEngineers(aAssignedEngineers);
				}
				if(itsCustomerRecord.getCuAssignmentId4() != null){
					aAssignedProjMgr = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId4(), itsTableNameUserLogin);
					aRxCustomerTabViewBean.setAssignedProjManagers(aAssignedProjMgr);
				}
				if (itsCustomerRecord.getCuTermsId() != null) {
					CuTerms aCuterms = itsCuMasterService.getCuTerms(itsCustomerRecord.getCuTermsId());
					if(aCuterms!=null){
						aRxCustomerTabViewBean.setCustomerTerms(aCuterms.getDescription());
					}else{
						aRxCustomerTabViewBean.setCustomerTerms("");
					}
				}
				if (itsCustomerRecord.getCuMasterTypeId() != null) {
					CuMasterType aCumastertype = itsCuMasterService.getCustomerType(itsCustomerRecord.getCuMasterTypeId());
					if(aCumastertype!=null)
					  aRxCustomerTabViewBean.setCustomerType(aCumastertype.getCode());
				}
				ArrayList<BigDecimal> dueDetails = itsCuMasterService.getCustomerAR(theRolodexNumber);
			//setDivision((List<Codivision>)itsCompanyService.getCompanyDivisions());
			//theModel.addAttribute("divisionsSearch", getDivision());
				
				BigDecimal yearToDate = null;
				BigDecimal lastYearSale = null;
				Cuinvoice aCuinvoice = new Cuinvoice();
				try {
					yearToDate = itsCuMasterService.getYearTodateSale(aRxMasterId);
					lastYearSale = itsCuMasterService.getLastYearSale(aRxMasterId);
					aCuinvoice =  itsCuMasterService.getLastSaleAmount(aRxMasterId);
				} catch (CustomerException e1) {
					sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"Customer",e1,session,therequest);
					e1.printStackTrace();
				}
				
			try {
				setDefaultWherehouse((List<Prwarehouse>) itsJobService.getWareHouse());
			} catch (JobException e) {
				sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"Customer",e,session,therequest);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			theModel.addAttribute("divisionsSearch", getDefaultWherehouse());
			theModel.addAttribute("currentDue", dueDetails.get(0));
			theModel.addAttribute("thirtyDays", dueDetails.get(1));
			theModel.addAttribute("sixtyDays", dueDetails.get(2));
			theModel.addAttribute("nintyDays", dueDetails.get(3));
			
			theModel.addAttribute("yearToDate", yearToDate);
			theModel.addAttribute("lastYearSale", lastYearSale);
			theModel.addAttribute("aCuinvoice", aCuinvoice);
			
			
			theModel.addAttribute("customerMasterObj", getCustomerRecord());
			theModel.addAttribute("customerTabFormDataBean", aRxCustomerTabViewBean);
			//fetching the shipping address theRolodexNumber
			//select Address1,Address2,City,State,Zip,coTaxTerritoryID from rxAddress where rxMasterID=8607

			List<?> shippinglist=itsCompanyService.getAddressShippingList(theRolodexNumber);
			theModel.addAttribute("ShippingList",shippinglist);
			//TaxteritoryList 
			List<?> cotaxterritorylist=itsCompanyService.getcotaxterritoryList();
			System.out.println("=====>"+cotaxterritorylist.size());
			theModel.addAttribute("AutoCompleteBean",cotaxterritorylist);
			}
		}
		theModel.addAttribute("customerType", getItsCuMasterType());
		//List<String> customerSales=itsCompanyService.getsalesDetails(theRolodexNumber);
		
		
		return "rolodexCustomer";
	}

	@RequestMapping(value = "engineer", method = RequestMethod.GET)
	public String getEngineerPage (@RequestParam(value="rolodexID", required=false) Integer theRolodexNumber, HttpSession session,HttpServletRequest therequest, ModelMap theModel) throws CustomerException, IOException, MessagingException {	
		logger.debug("Received request to show EngineerPage");
		try{
		
		JobCustomerBean aJobCustomerBean = new JobCustomerBean();
		Cumaster aCumaster=null;
		String aAssignedSalesRep = null;
		String aAssignedCSR = null;
		String aAssignedSalesMGR = null;
		String aAssignedEngineers = null;
		String aAssignedProjMgr = null;
		if(theRolodexNumber != null){
		Integer aRxMasterId = theRolodexNumber;
		setCustomerRecord(aRxMasterId);
		theModel.clear();
		theModel.addAttribute(SessionConstants.JOB_GRID_OBJ, aJobCustomerBean);
		aCumaster = getCustomerRecord();
		Rxmastercategory2 arxmstrctg2=itsCuMasterService.getrxMasterCategory2(aRxMasterId);
		
		//theModel.addAttribute("customerRecord", aCumaster);
		theModel.addAttribute("customerRecord", arxmstrctg2);
		
		if(itsCustomerRecord != null){
			if(itsCustomerRecord.getCuAssignmentId0() != null){
				aAssignedSalesRep = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId0(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId1() != null){
				aAssignedCSR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId1(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId2() != null){
				aAssignedSalesMGR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId2(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId3() != null){
				aAssignedEngineers = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId3(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId4() != null){
				aAssignedProjMgr = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId4(), itsTableNameUserLogin);
			}
			theModel.addAttribute("AssignedSalesRep", aAssignedSalesRep);
			theModel.addAttribute("AssignedCSRs", aAssignedCSR);
			theModel.addAttribute("AssignedSalesMGRs", aAssignedSalesMGR);
			theModel.addAttribute("AssignedProjManagers", aAssignedProjMgr);
			theModel.addAttribute("AssignedEngineers", aAssignedEngineers);
			theModel.addAttribute("customerMasterObj", getCustomerRecord());
			}
		}
		JobCustomerBean aCustomerBean = (JobCustomerBean) session.getAttribute(SessionConstants.CUST_GRID_OBJ);
		theModel.addAttribute(SessionConstants.CUST_GRID_OBJ, aCustomerBean);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"Customer",e,session,therequest);
		}
		return "engineer";
	}
	
	@RequestMapping(value = "architect", method = RequestMethod.GET)
	public String getArchitectPage(@RequestParam(value="rolodexID", required=false) Integer theRolodexNumber, HttpSession session,HttpServletRequest therequest, ModelMap theModel) throws CustomerException, IOException, MessagingException {	
		logger.debug("Received request to show ArchitectPage");
		try{
		JobCustomerBean aJobCustomerBean = new JobCustomerBean();
		Cumaster aCumaster=null;
		String aAssignedSalesRep = null;
		String aAssignedCSR = null;
		String aAssignedSalesMGR = null;
		String aAssignedEngineers = null;
		String aAssignedProjMgr = null;
		if(theRolodexNumber != null){
		Integer aRxMasterId = theRolodexNumber;
		setCustomerRecord(aRxMasterId);
		theModel.clear();
		theModel.addAttribute(SessionConstants.JOB_GRID_OBJ, aJobCustomerBean);
		aCumaster = getCustomerRecord();
		Rxmastercategory1 arxmstrctg1=itsCuMasterService.getrxMasterCategory1(aRxMasterId);
		theModel.addAttribute("customerRecord", arxmstrctg1);
		if(itsCustomerRecord != null){
			if(itsCustomerRecord.getCuAssignmentId0() != null){
				aAssignedSalesRep = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId0(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId1() != null){
				aAssignedCSR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId1(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId2() != null){
				aAssignedSalesMGR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId2(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId3() != null){
				aAssignedEngineers = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId3(), itsTableNameUserLogin);
			}
			if(itsCustomerRecord.getCuAssignmentId4() != null){
				aAssignedProjMgr = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId4(), itsTableNameUserLogin);
			}
			theModel.addAttribute("AssignedSalesRep", aAssignedSalesRep);
			theModel.addAttribute("AssignedCSRs", aAssignedCSR);
			theModel.addAttribute("AssignedSalesMGRs", aAssignedSalesMGR);
			theModel.addAttribute("AssignedProjManagers", aAssignedProjMgr);
			theModel.addAttribute("AssignedEngineers", aAssignedEngineers);
			theModel.addAttribute("customerMasterObj", getCustomerRecord());
			}
		}
		JobCustomerBean aCustomerBean = (JobCustomerBean) session.getAttribute(SessionConstants.CUST_GRID_OBJ);
		theModel.addAttribute(SessionConstants.CUST_GRID_OBJ, aCustomerBean);
		}catch(Exception e){
			sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"Customer",e,session,therequest);
		}
		return "architect";
	}
	
	@RequestMapping(value="/getJobDetailsList",method = RequestMethod.GET)
	public @ResponseBody  Jomaster getJobDetailsList (@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID
			,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException 
	{
		logger.debug("Received request to get search Jobs");
		Jomaster aJomaster = null;
		try{
			aJomaster = itsJobService.getSingleJobDetails(theJobNumber,joMasterID);
		}catch (Exception e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"Customer",e,session,therequest);
			logger.error(e.getMessage(), e);
		}
		return aJomaster;
	}
	
	@RequestMapping(value="/countyAndstate", method = RequestMethod.GET)
	public @ResponseBody List<?> getCityAndState(@RequestParam("term") String theCityAndState)
	{
		ArrayList<AutoCompleteBean> aCityAndState = (ArrayList<AutoCompleteBean>) itsCuMasterService.getcountyAndstate(theCityAndState);
		return aCityAndState;
	}
	
	
	public List<Prwarehouse> getDefaultWherehouse() {
		return itsDefaultWherehouse;
	}

	public void setDefaultWherehouse(List<Prwarehouse> defaultWherehouse) {
		this.itsDefaultWherehouse = defaultWherehouse;
	}

	public List<CuTerms> getPaymentTerms() {
		return itsPaymentTerms;
	}

	public void setPaymentTerms(List<CuTerms> paymentTerms) {
		this.itsPaymentTerms = paymentTerms;
	}

	public Cumaster getCustomerRecord() {
		return itsCustomerRecord;
	}
	public void setCustomerRecord(int rxMasterId) {
		this.itsCustomerRecord = itsCuMasterService.getCustomerDetails(rxMasterId);
	}

	public List<CuMasterType> getItsCuMasterType() {
		return itsCuMasterType;
	}

	public void setItsCuMasterType(List<CuMasterType> itsCuMasterType) {
		this.itsCuMasterType = itsCuMasterType;
	}
	
	public List<Codivision> getDivision() {
		return itsDivision;
	}
	public void setDivision(List<Codivision> division) {
		this.itsDivision = division;
	}
	
	@RequestMapping(value = "/SOAddressDetails", method = RequestMethod.POST)
	public  @ResponseBody Map<String, ArrayList<?>> getCustomerAddress(@RequestParam(value="rxMasterId") Integer rxMasterId, 
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			logger.info("SOAddressDetails rxMasterID:"+rxMasterId);
			ArrayList<Rxaddress> alRxaddress = (ArrayList<Rxaddress>)itsPurchaseService.getCustomerAddress(rxMasterId);
			ArrayList<String> alTaxRate = new ArrayList<String>();
			ArrayList<String> alTaxTerritory = new ArrayList<String>();
			ArrayList<String> alTaxTerritoryID = new ArrayList<String>();
			ArrayList<Rxcontact> alRxcontact = (ArrayList<Rxcontact>)itsPurchaseService.getEmailList(rxMasterId);
			map.put("customerAddress", alRxaddress);
			logger.info("SOAddressDetails"+rxMasterId);
			CoTaxTerritory acotTerritory  = null;
			for(Rxaddress al : alRxaddress){
				logger.info("SOAddressDetails isBilllto:"+al.getIsBillTo());
				acotTerritory = new CoTaxTerritory();
				try {
					if(al.getCoTaxTerritoryId() != null)
						acotTerritory = itsPurchaseService.getSingleTaxTerritory(al.getCoTaxTerritoryId());
					else{
						acotTerritory.setTaxRate(new BigDecimal(0.00));
						acotTerritory.setCounty("");
						acotTerritory.setCoTaxTerritoryId(0);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				if(null == acotTerritory.getTaxRate())
				{
					acotTerritory.setTaxRate(new BigDecimal(0.00));
				}
				if(null == acotTerritory.getCounty())
				{
					acotTerritory.setCounty("");
				}
				alTaxRate.add(acotTerritory.getTaxRate().toString());
				alTaxTerritory.add(acotTerritory.getCounty().toString());
				alTaxTerritoryID.add(acotTerritory.getCoTaxTerritoryId().toString());
				map.put("taxRateforCity", alTaxRate);
				map.put("taxTerritory", alTaxTerritory);
				map.put("taxTerritoryID", alTaxTerritoryID);
				String aAssignedSalesRep = null;
				String aAssignedCSR = null;
				String aAssignedSalesMGR = null;
				String aAssignedEngineers = null;
				String aAssignedProjMgr = null;
				//setItsCuMasterType((List<CuMasterType>)itsCuMasterService.getcuMasterType());
				if(rxMasterId != null){
				Integer aRxMasterId = rxMasterId;
					setCustomerRecord(aRxMasterId);
					if(itsCustomerRecord != null){
						RxCustomerTabViewBean aRxCustomerTabViewBean = new RxCustomerTabViewBean();
						if(itsCustomerRecord.getCuAssignmentId0() != null){
							aAssignedSalesRep = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId0(), itsTableNameUserLogin);
							aRxCustomerTabViewBean.setAssignedSalesRep(aAssignedSalesRep);
						}
						if(itsCustomerRecord.getCuAssignmentId1() != null){
							aAssignedCSR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId1(), itsTableNameUserLogin);
							aRxCustomerTabViewBean.setAssignedCSRs(aAssignedCSR);
						}
						if(itsCustomerRecord.getCuAssignmentId2() != null){
							aAssignedSalesMGR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId2(), itsTableNameUserLogin);
							aRxCustomerTabViewBean.setAssignedSalesMGRs(aAssignedSalesMGR);
						}
						if(itsCustomerRecord.getCuAssignmentId3() != null){
							aAssignedEngineers = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId3(), itsTableNameUserLogin);
							aRxCustomerTabViewBean.setAssignedEngineers(aAssignedEngineers);
						}
						if(itsCustomerRecord.getCuAssignmentId4() != null){
							aAssignedProjMgr = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId4(), itsTableNameUserLogin);
							aRxCustomerTabViewBean.setAssignedProjManagers(aAssignedProjMgr);
						}
						if (itsCustomerRecord.getCuTermsId() != null) {
							CuTerms aCuterms = itsCuMasterService.getCuTerms(itsCustomerRecord.getCuTermsId());
							aRxCustomerTabViewBean.setCustomerTerms(aCuterms.getDescription());
						}
						if (itsCustomerRecord.getCuMasterTypeId() != null) {
							CuMasterType aCumastertype = itsCuMasterService.getCustomerType(itsCustomerRecord.getCuMasterTypeId());
							aRxCustomerTabViewBean.setCustomerType(aCumastertype.getCode());
						}
						setDivision((List<Codivision>)itsCompanyService.getCompanyDivisions());
						ArrayList<Cumaster> alCumaster = new ArrayList<Cumaster>();
						ArrayList<RxCustomerTabViewBean> alRxCustomerTabViewBean = new ArrayList<RxCustomerTabViewBean>();
						Cumaster objCumaster = getCustomerRecord();
						alCumaster.add(objCumaster);
						alRxCustomerTabViewBean.add(aRxCustomerTabViewBean);
						map.put("divisionsSearch", (ArrayList<Codivision>)getDivision());
						map.put("customerMasterObj", alCumaster);
						map.put("customerTabFormDataBean", alRxCustomerTabViewBean);
						map.put("wareHouse", (ArrayList<?>) itsJobService.getWareHouse());
						if(alRxcontact != null && alRxcontact.size() > 0)
							map.put("emailList", alRxcontact);
						else
							map.put("emailList", new ArrayList<Rxcontact>());
						
					}
				}
				//theModel.addAttribute("customerType", getItsCuMasterType());
				
			}
		} catch (Exception e) {
			sendTransactionException("<b>rxMasterId:</b>"+rxMasterId,"Customer",e,session,therequest);
			logger.error(e.getMessage(), e);
		}
		return map;		
	
	}
	
	@RequestMapping(value = "/getEmailList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, ArrayList<?>> getEmailList(
			@RequestParam(value = "rxMasterID", required = false) Integer rxMasterId,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, JobException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Rxcontact> alRxcontact = (ArrayList<Rxcontact>)itsPurchaseService.getEmailList(rxMasterId);
			if(alRxcontact != null && alRxcontact.size() > 0)
				map.put("emailList", alRxcontact);
			else
				map.put("emailList", new ArrayList<Rxcontact>());
		} catch (Exception e) {
			sendTransactionException("<b>rxMasterId:</b>"+rxMasterId,"Customer",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/getJobCUInvoiceEmailList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, ArrayList<?>> getJobCUInvoiceEmailList(
			@RequestParam(value = "rxMasterID", required = false) Integer rxMasterId,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest)
			throws IOException, JobException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Cumaster> alCumaster = (ArrayList<Cumaster>)itsPurchaseService.getJobCUInvoiceEmailList(rxMasterId);
			if(alCumaster != null && alCumaster.size() > 0)
				map.put("emailList", alCumaster);
			else
				map.put("emailList", new ArrayList<Cumaster>());
		} catch (Exception e) {
			sendTransactionException("<b>rxMasterId:</b>"+rxMasterId,"Customer",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
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
}
