package com.turborep.turbotracker.search.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
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

import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.search.dao.SearchBean;
import com.turborep.turbotracker.search.exception.SearchException;
import com.turborep.turbotracker.search.service.SearchServiceInterface;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/search")
public class SearchController {
	protected static Logger itslogger = Logger.getLogger(SearchController.class);
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name = "SearchServices")
	private SearchServiceInterface itsSearchServices;
	
	@Resource(name="salesServices")
	private SalesService itsSalesServices;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody  String searchrolodex (@RequestParam("rolodex") String theRolodex) {
		List<?> aHomesearch = itsSearchServices.getRolodex();
		return theRolodex;
	}

	@RequestMapping(value="/searchjoblist", method = RequestMethod.GET)
	public @ResponseBody List<?> getJobSearch(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aJobSearch = itsSearchServices.getJobSearchList(aSearchJob, theJob);
		return aJobSearch;
	}
	@RequestMapping(value="/searchjobincontacts", method = RequestMethod.GET)
	public @ResponseBody List<?> getJobSearchfromContacts(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aJobSearch = itsSearchServices.getJobSearchListContact(aSearchJob, theJob);
		return aJobSearch;
	}
	@RequestMapping(value="/searchjoblisthome", method = RequestMethod.GET)
	public @ResponseBody List<?> getJobSearchHome(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists Home");
		AutoCompleteBean theSearchJob = new AutoCompleteBean();
		List<?> aJobSearch = itsSearchServices.getJobSearchListHome(theSearchJob, theJob);
		return aJobSearch;
	}
	
	@RequestMapping(value="/searchjob",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchJob (@RequestParam(value = "jobsearch", required = false) String theJob) {
		itslogger.debug("Received request to get search Jobs");
		List<?> aJobSearch=null;
		try {
			String aJobNumber =StringUtils.substringBefore(theJob, "[").trim();
			aJobSearch = itsSearchServices.getjobSearchResultsWithJob(aJobNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aJobSearch;
	}

	@RequestMapping(value="/searchjobcustomer",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchJobCustomer (@RequestParam(value = "jobnumber", required = false) String theJobNumber,
																				 					@RequestParam(value="jobname", required=false) String theJobName,
								
																				 					@RequestParam(value = "joMasterID", required = false) Integer thejoMasterID, HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession theSession) throws JobException, IOException, MessagingException{
		System.out.println("===========>>>>>>>> searchjobcustomer");
		itslogger.debug("Received request to searchJobCustomers");
		String ajobName= theJobName.replace("`", " ");
		List<?> aJobSearch = null;
		try{
			aJobSearch = itsJobService.getjobSearchResultsJob(theJobNumber, ajobName,thejoMasterID);
		}catch (JobException e) {
			itslogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"SearchController",e,theSession,theRequest);
		}
		return aJobSearch;
	}
	
	@RequestMapping(value="/searchrolodexlist", method = RequestMethod.GET)
	public @ResponseBody List<?> getRolodexSearch(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean theSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchList(theSearchJob, theJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchrolodex",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchRolodex (@RequestParam(value = "rolodex", required = false) String theRolodex) {
		itslogger.debug("Received request to get search Rolodex");
		String[] aRoll = theRolodex.split(": ");
		String aEntity =aRoll[0];
		if(aRoll.length<=1){
			return new ArrayList();
		}
		if(aEntity.equalsIgnoreCase("ENGR")){
			aEntity = "engi";
		}
		if(aEntity.equalsIgnoreCase("G.C")){
			aEntity = "generalcontractors";
		}
		if(aEntity.equalsIgnoreCase("ARCH/ENGR")){
			aEntity = "architect";
		}
		String aRolodex1 =aRoll[1];
		String[] aDescription = aRolodex1.split(",  "); 
		String[] aName;
		String aSearchText = "";
		String aSearch = "";
		if (aDescription.length == 4) {
			aSearchText = aDescription[0];
			aName = aSearchText.split("'");
			if(aName.length == 2){
				aSearchText = aName[0];
			}
			aSearch = aDescription[2];
		} else if(aDescription.length == 3) {
			aSearchText = aDescription[1];
			aName = aSearchText.split("'");
			if(aName.length == 2){
				aSearchText = aName[0];
			}
			aSearch = aDescription[2];
		} else if(aDescription.length == 2) {
			aSearchText = aDescription[0];
			aName = aSearchText.split("'");
			if(aName.length == 2){
				aSearchText = aName[0];
			}
			aSearch = aDescription[1];
		} else if(aDescription.length == 1) {
			aSearchText = aDescription[0];
			aName = aSearchText.split("'");
			if(aName.length == 2){
				aSearchText = aName[0];
			}
		}
		List<?> aRolodexSearch = itsSearchServices.getrolodexSearchResultsWithRolodex(aEntity, aSearchText, aSearch);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchrolodexForSale",method = RequestMethod.GET)
	public @ResponseBody  SearchBean searchRolodexForSale (@RequestParam(value = "rolodex", required = false) Integer theRolodex) {
		itslogger.debug("Received request to get search Rolodex");
		List<SearchBean> aLoginID = itsSearchServices.getLoginIDForSales(theRolodex);
		SearchBean aSearchBean = aLoginID.get(0);
		aSearchBean.setPk_fields(theRolodex);
		return aSearchBean;
	}

	
	@RequestMapping(value = "salesupcomingForSale",method = RequestMethod.GET)
	public @ResponseBody CustomResponse upcomingForCustomer(@RequestParam(value = "createdLoginID") int theCreated,
																								  @RequestParam(value = "changedLoginID") int theChanged,
																								  @RequestParam(value = "rxMaster") int theRxMaster, HttpSession theSession) {
		SalesRepBean aSalesRep = new SalesRepBean();
		if(theCreated != 0){
			aSalesRep.setSalesRepId(theCreated);
		}else if(theChanged !=0){
			aSalesRep.setSalesRepId(theChanged);
		}else if(theCreated != 0 && theChanged != 0){
			aSalesRep.setSalesRepId(theCreated);
		}
		ArrayList<JobsBean> aUpcomingJobsList = (ArrayList<JobsBean>) itsSalesServices.getUpcoming(aSalesRep);
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aUpcomingJobsList);
		return aResponse;
	}
	
	@RequestMapping(value = "salessingleupcomingForSale",method = RequestMethod.GET)
	public @ResponseBody CustomResponse upcomingForSingleCustomer(@RequestParam(value = "createdLoginID") int theCreated,
																								  @RequestParam(value = "changedLoginID") int theChanged,
																								  @RequestParam(value = "joMasterID") int theRxMaster,HttpSession theSession) {
		SalesRepBean aSalesRep = new SalesRepBean();
		if(theCreated != 0){
			aSalesRep.setSalesRepId(theCreated);
		}else if(theChanged !=0){
			aSalesRep.setSalesRepId(theChanged);
		}else if(theCreated != 0 && theChanged != 0){
			aSalesRep.setSalesRepId(theCreated);
		}
		aSalesRep.setUserLoginID(theRxMaster);
		ArrayList<JobsBean> aUpcomingJobsList = (ArrayList<JobsBean>) itsSalesServices.upcomingForSingleCustomer(aSalesRep);
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aUpcomingJobsList);
		return aResponse;
	}
	
	@RequestMapping(value = "getsingleproject",method = RequestMethod.GET)
	public @ResponseBody Integer getsingleproject(@RequestParam(value = "description") String theCreated, HttpSession theSession) {
		int aJobMaterID = itsSalesServices.getsingleproject(theCreated);
		return aJobMaterID;
	}
	
	@RequestMapping(value = "getCustomerFindProject",method = RequestMethod.GET)
	public @ResponseBody List<?> upcomingForCustomerFindPorject(@RequestParam(value = "loginIDForCustomer") int theCreated, HttpSession theSession) {
		SalesRepBean aSalesRep = new SalesRepBean();
		if(theCreated != 0){
			aSalesRep.setSalesRepId(theCreated);
		}
		ArrayList<AutoCompleteBean> aUpcomingJobsList = (ArrayList<AutoCompleteBean>) itsSalesServices.getCustomerFindProject(aSalesRep);
		return aUpcomingJobsList;
	}
	
	@RequestMapping(value="/searchCustomerList", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerSearch(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Listz");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchCustomerList(aSearchJob, theJob);
		itslogger.info("CustomerListSize::"+aRolodexSearch.size());
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchArchitectList", method = RequestMethod.GET)
	public @ResponseBody List<?> getArchitectList(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchArchitectList(aSearchJob, theJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchEngineerList", method = RequestMethod.GET)
	public @ResponseBody List<?> getEngineerList(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchEngineerList(aSearchJob, theJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchCustomerListForUpComingBids", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerSearchForUpComingBids(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchCustomerListForUpComingBids(aSearchJob, theJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchEmployeeList", method = RequestMethod.GET)
	public @ResponseBody List<?> getEmployeeSearch(@RequestParam(value = "term", required = false) String theJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchEmployeeList(aSearchJob, theJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchVendorList", method = RequestMethod.GET)
	public @ResponseBody List<?> getVendorSearch(@RequestParam(value = "term", required = false) String aJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getRolodexSearchVendorList(aSearchJob, aJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/getVendorBillPayList", method = RequestMethod.GET)
	public @ResponseBody List<?> getVendorBillPayList(@RequestParam(value = "term", required = false) String aJob){
		itslogger.debug("Received request to get search Jobs Lists");
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aRolodexSearch = itsSearchServices.getVendorBillPayList(aSearchJob, aJob);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchrolodexdetails",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchRolodexDetails (@RequestParam(value = "rolodexNumber", required = false) String theRxNumber) {
		itslogger.debug("Received request to get search Rolodex");
		List<?> aRolodexSearch = itsSearchServices.getrolodexSearchResultsWithRolodexDetails(theRxNumber);
		return aRolodexSearch;
	}
	
	@RequestMapping(value="/searchUserList",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchUserList (@RequestParam(value = "term", required = false) String theUserSearch) 
	{
		itslogger.debug("Received request to get USER LIST");
		AutoCompleteBean aAutoCompleteBean = new AutoCompleteBean();
		List<?> aUserSearchList = itsSearchServices.getSearchUserList(aAutoCompleteBean, theUserSearch);
		return aUserSearchList;
	}
	
	@RequestMapping(value="/searchAccountsList",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchAccountsList (@RequestParam(value = "term", required = false) String theAccountSearch) 
	{
		itslogger.debug("Received request to get ACCOUNTS LIST");
		AutoCompleteBean aAutoCompleteBean = new AutoCompleteBean();
		List<?> aAccountsSearchList = itsSearchServices.getSearchAccountsList(aAutoCompleteBean, theAccountSearch);
		return aAccountsSearchList;
	}
	
	@RequestMapping(value="/searchinventory",method = RequestMethod.GET)
	public @ResponseBody  List<AutoCompleteBean> searchInventory (@RequestParam(value = "term", required = false) String theAccountSearch, HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
	{
		itslogger.debug("Received request to get Inventory Search");
		List<AutoCompleteBean> aAccountsSearchList = null;
		try {
			aAccountsSearchList = itsSearchServices.getInventorySearch(theAccountSearch);
		} catch (SearchException e) {
			itslogger.error(e.getMessage(), e);
			sendTransactionException("<b>theAccountSearch:</b>"+theAccountSearch,"SearchController",e,theSession,theRequest);
		}
		return aAccountsSearchList;
	}
	
	
	@RequestMapping(value="/searchShipViaList",method = RequestMethod.GET)
	public @ResponseBody  List<?> searchShipViaList(@RequestParam(value = "term", required = false) String theAccountSearch) throws SearchException 
	{
		itslogger.debug("Received request to get ACCOUNTS LIST");
		AutoCompleteBean aAutoCompleteBean = new AutoCompleteBean();
		List<?> aShipViaSearchList = itsSearchServices.getSearchShipViaList(aAutoCompleteBean, theAccountSearch);
		return aShipViaSearchList;
	}
	
	@RequestMapping(value="/userInitials", method = RequestMethod.GET)
	public @ResponseBody List<?> getUserInitialsList(@RequestParam("term") String theUserLogin, HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession theSession) throws IOException, SearchException, MessagingException{
		ArrayList<AutoCompleteBean> aUserLoginInitials = null;
		try{
			aUserLoginInitials = (ArrayList<AutoCompleteBean>) itsSearchServices.getUserInitialsList(theUserLogin);
		}catch (SearchException e) {
			itslogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theUserLogin:</b>"+theUserLogin,"SearchController",e,theSession,theRequest);
		}
		return aUserLoginInitials;
	}
	
	/**
	 * Controller method to search POs based on Vendor Name or Order number.
	 * @param theAccountSearch
	 * @return {@link List} of search results.
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	@RequestMapping(value="/search_po",method = RequestMethod.GET)
	public @ResponseBody  List<AutoCompleteBean> searchPurchaseOrders (@RequestParam(value = "term", required = false) String theAccountSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
	{
		itslogger.debug("Received request to get Inventory Search");
		List<AutoCompleteBean> aAccountsSearchList = null;
		try {
			aAccountsSearchList = itsSearchServices.searchPOs(theAccountSearch);
		} catch (SearchException e) {
			itslogger.error(e.getMessage(), e);
			sendTransactionException("<b>theAccountSearch:</b>"+theAccountSearch,"SearchController",e,theSession,theRequest);
		}
		return aAccountsSearchList;
	}
	
	//Added to get Vendor Names
		/**
		 * Returns the Vendor List from the parameter passed.
		 * @param theVendorSearch
		 * @return List of Vendor Names.
		 * @throws MessagingException 
		 * @throws IOException 
		 */
		@RequestMapping(value="/searchVendor",method = RequestMethod.GET)
		public @ResponseBody  List<AutoCompleteBean> searchVendor (@RequestParam(value = "term", required = false) String theVendorSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
		{
			itslogger.debug("Received request to get Vendor Search");
			List<AutoCompleteBean> aVendorSearchList = null;
			try {
				aVendorSearchList = itsSearchServices.getVendorSearch(theVendorSearch);
			} catch (SearchException e) {
				itslogger.error(e.getMessage(), e);
				sendTransactionException("<b>theVendorSearch:</b>"+theVendorSearch,"SearchController",e,theSession,theRequest);
			}
			return aVendorSearchList;
		}
		//Added to get Customer Names
				@RequestMapping(value="/customerAddress",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchVendor (@RequestParam(value = "term", required = false) String theAccountSearch,
						@RequestParam(value = "rxMasterId", required = false) Integer rxMasterId,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get Vendor Search");
					List<AutoCompleteBean> aCustomerSearchList = null;
					try {
						aCustomerSearchList = itsSearchServices.getCustomerSearch(theAccountSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theAccountSearch:</b>"+theAccountSearch,"SearchController",e,theSession,theRequest);
					}
					return aCustomerSearchList;
				}
				
				//Added to get Po Numbar
				@RequestMapping(value="/searchPONumber",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchPONumber (@RequestParam(value = "term", required = false) String thePOSearch,
						@RequestParam(value = "rxMasterId", required = false) Integer rxMasterId,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get Vendor Search");
					List<AutoCompleteBean> aPONumbarList = null;
					try {
						aPONumbarList = itsSearchServices.getPOSearch(thePOSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>thePOSearch:</b>"+thePOSearch,"SearchController",e,theSession,theRequest);
					}
					return aPONumbarList;
				}
				
				//Added to get SO Number
				@RequestMapping(value="/searchSONumber",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchSONumber (@RequestParam(value = "term", required = false) String theSOSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get SO Search");
					List<AutoCompleteBean> aPONumbarList = null;
					try {
						aPONumbarList = itsSearchServices.getSOSearch(theSOSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theSOSearch:</b>"+theSOSearch,"SearchController",e,theSession,theRequest);
					}
					return aPONumbarList;
				}
				
				@RequestMapping(value="/searchCuInvoiceNumber",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchCuInvoiceNumber (@RequestParam(value = "term", required = false) String theCuSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get Customer Invoice Search");
					List<AutoCompleteBean> aCuNumberList = null;
					try {
						aCuNumberList = itsSearchServices.getCuInvoiceSearch(theCuSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theCuSearch:</b>"+theCuSearch,"SearchController",e,theSession,theRequest);
					}
					return aCuNumberList;
				}
				
				@RequestMapping(value="/searchCuName",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchCuName (@RequestParam(value = "term", required = false) String theCuSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get CustomerName Invoice Search");
					List<AutoCompleteBean> aCuNumberList = null;
					try {
						aCuNumberList = itsSearchServices.getCuNameSearch(theCuSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theCuSearch:</b>"+theCuSearch,"SearchController",e,theSession,theRequest);
					}
					return aCuNumberList;
				}
				
				@RequestMapping(value="/searchBatchInvoice",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchBatchInvoice (@RequestParam(value = "term", required = false) String theCuSearch,
						@RequestParam(value = "batchInvoiceCu", required = false) String batchInvoiceCu,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException
				{
					itslogger.debug("Received request to get Customer Invoice ID");
					List<AutoCompleteBean> aCuNumberList = null;
					try {
						aCuNumberList = itsSearchServices.getCuInvoiceIDSearch(theCuSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theCuSearch:</b>"+theCuSearch,"SearchController",e,theSession,theRequest);
					}
					return aCuNumberList;
				}
				
				@RequestMapping(value="/getWarehouseTransferSearch",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> getWarehouseTransferSearch (@RequestParam(value = "term", required = false) String thePOSearch,
						@RequestParam(value = "rxMasterId", required = false) Integer rxMasterId,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get Vendor Search");
					List<AutoCompleteBean> aPONumbarList = null;
					try {
						aPONumbarList = itsSearchServices.getWarehouseTransferSearch(thePOSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>thePOSearch:</b>"+thePOSearch,"SearchController",e,theSession,theRequest);
					}
					return aPONumbarList;
				}
				
				@RequestMapping(value="/searchVendorInvoiceNumber",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchVendorInvoiceNumber (@RequestParam(value = "term", required = false) String thePOSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get Vendor Search");
					List<AutoCompleteBean> aPONumbarList = null;
					try {
						aPONumbarList = itsSearchServices.getVendorInvoiceList(thePOSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>thePOSearch:</b>"+thePOSearch,"SearchController",e,theSession,theRequest);
					}
					return aPONumbarList;
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
				
				@RequestMapping(value="/searchCustomer",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchCustomer (@RequestParam(value = "term", required = false) String theCuSearch,HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get CustomerName Invoice Search");
					List<AutoCompleteBean> aCuNumberList = null;
					try {
						aCuNumberList = itsSearchServices.getSearchCustomer(theCuSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theCuSearch:</b>"+theCuSearch,"SearchController",e,theSession,theRequest);
					}
					return aCuNumberList;
				}
				
				@RequestMapping(value="/searchinventoryProduct",method = RequestMethod.GET)
				public @ResponseBody  List<AutoCompleteBean> searchinventoryProduct(@RequestParam(value = "term", required = false) String theAccountSearch, HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException 
				{
					itslogger.debug("Received request to get Inventory Search");
					List<AutoCompleteBean> aAccountsSearchList = null;
					try {
						aAccountsSearchList = itsSearchServices.getInventoryproductSearch(theAccountSearch);
					} catch (SearchException e) {
						itslogger.error(e.getMessage(), e);
						sendTransactionException("<b>theAccountSearch:</b>"+theAccountSearch,"SearchController",e,theSession,theRequest);
					}
					return aAccountsSearchList;
				}
}