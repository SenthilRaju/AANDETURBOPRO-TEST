package com.turborep.turbotracker.job.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoQuoteDetail;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.JoQuoteProperties;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateDetail;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateProperties;
import com.turborep.turbotracker.job.dao.JoQuotetemplateHeader;
import com.turborep.turbotracker.job.dao.JobCustomerBean;
import com.turborep.turbotracker.job.dao.JobQuoteDetailBean;
import com.turborep.turbotracker.job.dao.JobQuotesBidListBean;
import com.turborep.turbotracker.job.dao.Jobidder;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.Joquotecolumn;
import com.turborep.turbotracker.job.dao.Joquotehistory;
import com.turborep.turbotracker.job.dao.joQLineItemTemplateProp;
import com.turborep.turbotracker.job.dao.joQLineItemsProp;
import com.turborep.turbotracker.job.dao.joQuoteDetailMstr;
import com.turborep.turbotracker.job.dao.joQuoteDetailPosition;
import com.turborep.turbotracker.job.dao.joQuoteTempDetailMstr;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.exception.QuoteTemplateException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.job.service.QuoteTemplateService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
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
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
@RequestMapping("/jobtabs2")
public class JobQuoteFormController {

	Logger logger = Logger.getLogger(JobQuoteFormController.class);

	private String jobNumber;
	private String cutomerRxName;
	private Jomaster jomasterDetails;
	private Cumaster customerRecord;
	private List<CuMasterType> itsCuMasterType;
	private TsUserLogin itsUserDetails;
	private String tableNameRxMaster = "rxMaster";

	@Resource(name = "userLoginService")
	private UserService itsUserService;

	@Resource(name = "jobService")
	private JobService jobService;

	@Resource(name = "customerService")
	private CustomerService itsCuMasterService;

	@Resource(name = "rolodexService")
	private RolodexService customerService;

	@Resource(name = "companyService")
	private CompanyService companyService;

	@Resource(name = "vendorService")
	private VendorServiceInterface itsVendorService;

	@Resource(name = "pdfService")
	private PDFService pdfService;

	@Resource(name = "quoteTemplateService")
	private QuoteTemplateService quoteTemplateService;
	
	@Resource(name="sysService")
	private SysService sysservice;
	
	@RequestMapping(value = "jobwizardquotes", method = RequestMethod.GET)
	public String getJobsQuotesPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession theSession, ModelMap theModel,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, JobException, UserException, MessagingException {
		String architect = null;
		String engineersRX = null;
		Integer aCustomerID = null;
		String aEmailID = null;
		setItsCuMasterType((List<CuMasterType>) itsCuMasterService
				.getcuMasterType());
		setJobNumber(theJobNumber);
		setJomasterDetails(jobNumber,joMasterID);
		Jomaster aJomaster = getJomasterDetails();
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		Integer aUserId = aUserBean.getUserId();
		Joquotecolumn Joquotecolumnlst=sysservice.getjoQuoteColumn();
		try {
			setItsUserDetails((TsUserLogin) itsUserService
					.getUserDetails(aUserId));
			if (aJomaster.getRxCustomerId() != null) {
				setCutomerRxName((String) companyService
						.getCustomerName(aJomaster.getRxCustomerId()));
			}
			theModel.addAttribute("joMasterDetails", aJomaster);
			if (aJomaster.getDescription() != null) {
				String aString = aJomaster.getDescription();
				String string = aString.replaceAll("\"", "");
				theModel.addAttribute("joMasterDescription", string);
				String aPDFProjectName = string.replaceAll("#", "_");
				theModel.addAttribute("joMasterDescriptionForPDF",
						aPDFProjectName);
			}
			if (aJomaster.getRxCategory1() != null) {
				architect = (String) companyService.getRolodexName(
						aJomaster.getRxCategory1(), tableNameRxMaster);
			}
			if (aJomaster.getRxCategory2() != null) {
				engineersRX = (String) companyService.getRolodexName(
						aJomaster.getRxCategory2(), tableNameRxMaster);
			}
			if (aJomaster.getLocationName() != null
					&& aJomaster.getLocationName() != "") {
				aCustomerID = (Integer) companyService.getRxMasterID(aJomaster
						.getLocationName());
				aEmailID = (String) companyService.getEmailID(aCustomerID);
				theModel.addAttribute("emailID", aEmailID);
			}
			theModel.addAttribute("AssignedArchitects", architect);
			theModel.addAttribute("AssignedEngineering", engineersRX);
			if (aJomaster.getRxCustomerId() != null) {
				theModel.addAttribute("CustomerName", getCutomerRxName());
			}
			theModel.addAttribute("Joquotecolumn", Joquotecolumnlst);
			List<String> addlist=new ArrayList<String>();
			//Job Settings
			addlist.add("PlanSpecLabel1");
			addlist.add("PlanSpecLabel2");
			addlist.add("SourceCheckBox1");
			addlist.add("SourceCheckBox2");
			addlist.add("SourceCheckBox3");
			addlist.add("SourceCheckBox4");
			addlist.add("SourceLabel1");
			addlist.add("SourceLabel2");
			ArrayList<Sysvariable> sysvariablelist= itsUserService.getInventorySettingsDetails(addlist);
			int i=0;
			for(Sysvariable theSysvariable:sysvariablelist){
			if(i==0){
			theModel.addAttribute("txt_PlanSpecLabel1", theSysvariable.getValueString());
			}else if(i==1){
			theModel.addAttribute("txt_PlanSpecLabel2", theSysvariable.getValueString());
			}else if(i==2){
			theModel.addAttribute("txt_SourceCheckBox1", theSysvariable.getValueString());
			}else if(i==3){
			theModel.addAttribute("txt_SourceCheckBox2", theSysvariable.getValueString());
			}else if(i==4){
			theModel.addAttribute("txt_SourceCheckBox3", theSysvariable.getValueString());
			}else if(i==5){
			theModel.addAttribute("txt_SourceCheckBox4", theSysvariable.getValueString());
			}else if(i==6){
			theModel.addAttribute("txt_SourceLabel1", theSysvariable.getValueString());
			}else if(i==7){
			theModel.addAttribute("txt_SourceLabel2", theSysvariable.getValueString());
			}
			i=i+1;
			}
		} catch (CompanyException e) {
			sendTransactionException("<b>theJobNumber,theJobCustomer:</b>"+theJobNumber+","+theJobCustomer,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		theModel.addAttribute("userDetails", getItsUserDetails());
		theModel.addAttribute("customerType", getItsCuMasterType());
		return "job/jobwizardquotes";
	}

	public String getCutomerRxName() {
		return cutomerRxName;
	}

	public void setCutomerRxName(String cutomerRxName) {
		this.cutomerRxName = cutomerRxName;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Jomaster getJomasterDetails() {
		return jomasterDetails;
	}

	public void setJomasterDetails(String jobNumber,Integer joMasterId) throws JobException {
		this.jomasterDetails = jobService.getSingleJobDetails(jobNumber,joMasterId);
	}

	@RequestMapping(value = "jobquotesbidlist", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getQuotesBidList(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponce)
			throws IOException, MessagingException {
		CustomResponse aResponse = null;
		try {
			List<?> aBids = jobService.getQuotesBidlist(joMasterID);
			aResponse = new CustomResponse();
			aResponse.setRows(aBids);
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponce.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "jobquoteslist", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getQuotesList(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws JobException, IOException, MessagingException {
		CustomResponse response = null;
		try {
			List<?> quotes = jobService.getQuotesList(joMasterID);
			response = new CustomResponse();
			response.setRows(quotes);
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "jobQuoteHistory", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getQuoteHistory(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws JobException, IOException, MessagingException {
		CustomResponse response = null;
		try {
			List<JoQuoteHeader> quotes = jobService
					.getQuotesHistory(theJobNumber);
			response = new CustomResponse();
			response.setRows(quotes);
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/bidderList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getBidderList(@RequestParam("term") String bidder,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		ArrayList<AutoCompleteBean> aBidderList = null;
		try {
			aBidderList = (ArrayList<AutoCompleteBean>) jobService
					.getBidderList(bidder);
		} catch (JobException e) {
			sendTransactionException("<b>bidder:</b>"+bidder,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} 
		return aBidderList;
	}

	@RequestMapping(value = "/filterContactListBaseBidderList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getContactList(@RequestParam("term") Integer theRxMasterId,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		ArrayList<AutoCompleteBean> aContactList = null;
		try {
			aContactList = (ArrayList<AutoCompleteBean>) jobService
					.getContactListBaseBidderList(theRxMasterId);
		} catch (JobException e) {
			sendTransactionException("<b>theRxMasterId:</b>"+theRxMasterId,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aContactList;
	}

	@RequestMapping(value = "/filterBidderList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getBidderContactList(
			@RequestParam("rxMasterId") Integer theRxMasterId,
			HttpServletRequest therequest,HttpSession session,
			HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		ArrayList<AutoCompleteBean> aContactList = null;
		try {
			aContactList = (ArrayList<AutoCompleteBean>) jobService
					.getContactListBaseBidderList(theRxMasterId);
		} catch (JobException e) {
			sendTransactionException("<b>theRxMasterId:</b>"+theRxMasterId,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aContactList;
	}

	@RequestMapping(value = "/manpulateQuotebidList", method = RequestMethod.POST)
	public @ResponseBody
	Integer insertQuoteBidlist(
			@RequestParam(value = "bidder", required = false) String theBidder,
			@RequestParam(value = "low", required = false) Boolean theLow,
			@RequestParam(value = "contact", required = false) String theContact,
			@RequestParam(value = "quoteType", required = false) Integer theQuoteTypeId,
			@RequestParam(value = "lastQuote", required = false) String theLastQuote,
			@RequestParam(value = "rev", required = false) String theRev,
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "rep", required = false) String theRep,
			@RequestParam(value = "selectBidderId", required = false) Integer theBidderId,
			@RequestParam(value = "bidderId", required = false) Integer theBidID,
			@RequestParam(value = "rxMasterId", required = false) Integer theRxMasterId,
			@RequestParam(value = "rxContactId", required = false) Integer theRxContactId,
			@RequestParam(value = "joMasterId", required = false) Integer theJoMasterId,
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "from", required = false) String fromPage,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		Jobidder aJobidder = new Jobidder();
		Cumaster aCumaster = new Cumaster();
		Cuinvoice aCuinvoice = new Cuinvoice();
		try {
			if (operation.equals("del")) {
				aJobidder.setJoBidderId(theBidderId);
				jobService.deleteQuoteBidder(aJobidder);
			} else {
				Jomaster joMasterId = jobService
						.getJoMasterIdJobstatus(theJobNumber);
				if (theLow != null) {
					aJobidder.setLowBid(theLow);
				}
				aJobidder.setCuMasterTypeId(theQuoteTypeId);
				aCumaster.setCuMasterTypeId(theQuoteTypeId);
				aCumaster.setCuMasterId(theRxMasterId);
				if (theLastQuote != null && theLastQuote != "") {
					aJobidder.setQuoteDate(DateUtils.parseDate(theLastQuote,
							new String[] { "MM/dd/yyyy hh:mm a" }));
				}
				aJobidder.setQuoteRev(theRev);
				aJobidder.setUserLoginID(aUserBean.getUserId());
				aJobidder.setRxMasterId(theRxMasterId);
				if (theRxContactId == null) {
					theRxContactId = null;
					aJobidder.setRxContactId(theRxContactId);
				} else {
					if (theRxContactId != -1) {
						aJobidder.setRxContactId(theRxContactId);
					} else {
						aJobidder.setRxContactId(null);
					}
				}
				Calendar currentDate = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				String theDateNow = formatter.format(currentDate.getTime());
				aCuinvoice.setRxCustomerId(theRxMasterId);
				if (joMasterId.getJoStatus() == 3) {
					aCuinvoice.setTransactionStatus(1);
				} else if (joMasterId.getJoStatus() == 2) {
					aCuinvoice.setTransactionStatus(2);
				} else if (joMasterId.getJoStatus() == 1) {
					aCuinvoice.setTransactionStatus(1);
				}
				aCuinvoice.setCreatedById(aUserBean.getUserId());
				aCuinvoice.setCreatedOn(DateUtils.parseDate(theDateNow,
						new String[] { "MM/dd/yyyy" }));
				aCuinvoice.setInvoiceDate(DateUtils.parseDate(theDateNow,
						new String[] { "MM/dd/yyyy" }));
				aCuinvoice.setApplied(false);
				BigDecimal aAmount = new BigDecimal(0);
				aCuinvoice.setAppliedAmount(aAmount);
				aCuinvoice.setInvoiceAmount(aAmount);
				aCuinvoice.setTaxAmount(aAmount);
				if (operation.equals("add")) {
					aJobidder.setJoMasterId(joMasterId.getJoMasterId());
					jobService.addQuoteBidder(aJobidder);
					// customerService.addQuickQuoteCuMaster(aCuinvoice);
					/* While adding bidlist invoice should not create */
					if (fromPage != null) {
						if (fromPage.equals("bidlistadd")) {
							logger.info("if:while added the bidlist invoice should not created");
						} else {
							logger.info("else:while added the bidlist invoice should not created");
							customerService.addQuickQuoteCuMaster(aCuinvoice);
						}
					} else {
						logger.info("else:while added the bidlist invoice should not created");
						customerService.addQuickQuoteCuMaster(aCuinvoice);
					}

				} else if (operation.equals("edit")) {
					aJobidder.setCuMasterTypeId(theQuoteTypeId);
					aJobidder.setJoMasterId(theJoMasterId);
					aJobidder.setJoBidderId(theBidID);
					jobService.updateQuoteBidder(aJobidder);
				}
			}
		} catch (JobException e) {
			sendTransactionException("<b>theBidID:</b>"+theBidID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "/deleteQuotebidList", method = RequestMethod.GET)
	public @ResponseBody
	Integer deleteBidlist(
			@RequestParam(value = "selectBidderId", required = false) Integer theBidderId,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		Jobidder aJobidder = new Jobidder();
		try {
			aJobidder.setJoBidderId(theBidderId);
			jobService.deleteQuoteBidder(aJobidder);
		} catch (JobException e) {
			sendTransactionException("<b>theBidderId:</b>"+theBidderId,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "/getQuoteListDetails", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getQuoteListDetailsGridData(
			@RequestParam("joQuoteHeaderID") Integer theJoQuoteHeaderID,
			HttpServletResponse theResponse,HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {

		CustomResponse aResponse = new CustomResponse();
		try {
			if (theJoQuoteHeaderID == 0) {
				return aResponse;
			} else {
				List<?> aQuotes = jobService
						.getQuotesDetailGridList(theJoQuoteHeaderID);
				aResponse.setRows(aQuotes);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuoteHeaderID:</b>"+theJoQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/getLineItemValues", method = RequestMethod.GET)
	public @ResponseBody
	List<JobQuoteDetailBean> getLineItemValues(
			@RequestParam("joQuoteHeaderID") Integer theJoQuoteHeaderID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		List<JobQuoteDetailBean> aQuotes = null;
		try {
			if (theJoQuoteHeaderID == 0) {
				return aQuotes;
			} else {
				aQuotes = jobService
						.getQuotesDetailGridList(theJoQuoteHeaderID);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuoteHeaderID:</b>"+theJoQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aQuotes;
	}

	@RequestMapping(value = "/SaveQuoteDetailInfo", method = RequestMethod.POST)
	public @ResponseBody
	Integer insertQuote(
			@RequestParam(value = "quoteTypeDetailName", required = false) Integer quoteTypeDetailID,
			@RequestParam(value = "joHeaderID", required = false) Integer joQuoteHeaderID,
			@RequestParam(value = "joHeaderQuoteID", required = false) Integer joHeaderID,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "jobQuoteRevision", required = false) String jobQuoteRevision,
			@RequestParam(value = "jobQuoteSubmittedBYFullName", required = false) String jobQuoteSubmittedBYFullName,
			@RequestParam(value = "jobQuoteSubmittedBYInitials", required = false) String createdByName,
			@RequestParam(value = "jobQuoteSubmittedBYID", required = false) Integer jobQuoteSubmittedBYID,
			@RequestParam(value = "quoteRemarks", required = false) String quoteRemarks,
			@RequestParam(value = "jobQuoteInternalNote", required = false) String jobQuoteInternalNote,
			@RequestParam(value = "quoteTotalPrice", required = false) BigDecimal quoteTotalPrice,
			@RequestParam(value = "quoteDiscountedPrice", required = false) BigDecimal quoteDiscountedPrice,
			@RequestParam(value = "totalcost", required = false) BigDecimal totalcost,
			@RequestParam(value = "totalPrice", required = false) BigDecimal totalPrice,
			@RequestParam(value = "rxContactID", required = false) Integer rxContactID,
			@RequestParam(value = "bidderId", required = false) Integer bidderId,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "token", required = false) String token,
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse response) throws IOException, MessagingException {
		try {
			// commented by Niaz 2014-09-02 since add not working
			// if(quoteTypeDetailID == null){
			// return null;
			// }
			if (jobQuoteSubmittedBYID == null) {
				response.sendError(500, "'Submitted By' Empty");
				return null;
			}
			JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
			if (joQuoteHeaderID != null) {
				aJoQuoteHeader.setJoQuoteHeaderId(joQuoteHeaderID);
			} else if (joHeaderID != null) {
				aJoQuoteHeader.setJoQuoteHeaderId(joHeaderID);
			}
			
			if (jobQuoteRevision != null && jobQuoteRevision.contains(",")) {
				String[] jobQuoteRevisions = jobQuoteRevision.split(",");
				if (jobQuoteRevisions.length > 0) {
					jobQuoteRevision = jobQuoteRevisions[0];
				} else {
					jobQuoteRevision = "";
				}

			}
			aJoQuoteHeader.setQuoteRev(jobQuoteRevision);
			System.out.println("niaz test ..jobQuoteRevision="
					+ jobQuoteRevision);
			aJoQuoteHeader.setCreatedById(jobQuoteSubmittedBYID);
			aJoQuoteHeader.setCreatedByName(createdByName);
			aJoQuoteHeader.setDateCreated(new Date());
			aJoQuoteHeader.setDiscountedPrice(quoteDiscountedPrice);
			aJoQuoteHeader.setQuoteAmount(totalPrice);
			aJoQuoteHeader.setCostAmount(totalcost);
			aJoQuoteHeader.setInternalNote(jobQuoteInternalNote);
			aJoQuoteHeader.setRemarks(quoteRemarks);

			aJoQuoteHeader.setCuMasterTypeID(quoteTypeDetailID);
			aJoQuoteHeader.setJoMasterID(joMasterID);
			aJoQuoteHeader.setRxContactID(rxContactID);
			Integer joQuoteheaderID;
			joQuoteheaderID = jobService.saveQuoteDetails(aJoQuoteHeader,
					token, bidderId);

			return joQuoteheaderID;
		} catch (JobException e) {
			sendTransactionException("<b>joQuoteHeaderID quoteTypeDetailID jobNumber:</b>"+joQuoteHeaderID+","+ quoteTypeDetailID+","+ jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/SaveQuoteInfo", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateQuote(
			@RequestParam(value = "quoteTypeDetailName", required = false) Integer quoteTypeDetailID,
			@RequestParam(value = "joHeaderID", required = false) Integer joQuoteHeaderID,
			@RequestParam(value = "joHeaderQuoteID", required = false) Integer joHeaderID,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "jobQuoteRevision", required = false) String jobQuoteRevision,
			@RequestParam(value = "jobQuoteSubmittedBYFullName", required = false) String jobQuoteSubmittedBYFullName,
			@RequestParam(value = "jobQuoteSubmittedBYInitials", required = false) String createdByName,
			@RequestParam(value = "jobQuoteSubmittedBYID", required = false) Integer jobQuoteSubmittedBYID,
			@RequestParam(value = "quoteRemarks", required = false) String quoteRemarks,
			@RequestParam(value = "jobQuoteInternalNote", required = false) String jobQuoteInternalNote,
			@RequestParam(value = "quoteTotalPrice", required = false) BigDecimal quoteTotalPrice,
			@RequestParam(value = "quoteDiscountedPrice", required = false) BigDecimal quoteDiscountedPrice,
			@RequestParam(value = "totalcost", required = false) BigDecimal totalcost,
			@RequestParam(value = "totalPrice", required = false) BigDecimal totalPrice,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "token", required = false) String token,
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse response) throws IOException, MessagingException {
		try {
			JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
			aJoQuoteHeader.setCreatedById(jobQuoteSubmittedBYID);
			aJoQuoteHeader.setCreatedByName(createdByName);
			aJoQuoteHeader.setDateCreated(new Date());
			aJoQuoteHeader.setDiscountedPrice(quoteDiscountedPrice);
			aJoQuoteHeader.setQuoteAmount(totalPrice);
			aJoQuoteHeader.setCostAmount(totalcost);
			aJoQuoteHeader.setInternalNote(jobQuoteInternalNote);
			aJoQuoteHeader.setRemarks(quoteRemarks);
			aJoQuoteHeader.setQuoteRev(jobQuoteRevision);
			aJoQuoteHeader.setCuMasterTypeID(quoteTypeDetailID);
			if (joQuoteHeaderID != null) {
				aJoQuoteHeader.setJoQuoteHeaderId(joQuoteHeaderID);
			} else if (joHeaderID != null) {
				aJoQuoteHeader.setJoQuoteHeaderId(joHeaderID);
			}
			aJoQuoteHeader.setJoMasterID(joMasterID);
			Integer joQuoteheaderID;
			joQuoteheaderID = jobService
					.copyQuoteDetails(aJoQuoteHeader, token);
			return joQuoteheaderID;
		} catch (JobException e) {
			sendTransactionException("<b>joQuoteHeaderID quoteTypeDetailID jobNumber:</b>"+joQuoteHeaderID+","+ quoteTypeDetailID+","+ jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return null;
		}
	}
	
	@RequestMapping(value = "/templateProductList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> templateProductList(@RequestParam("term") String theProductText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aProductList = null;
		try {
			aProductList = (ArrayList<AutoCompleteBean>) jobService.getTemplateProductQuoteList(theProductText);
		} catch (JobException e) {
			sendTransactionException("<b>theProductText:</b>"+theProductText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductList;
	}

	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getProductList(@RequestParam("term") String theProductText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aProductList = null;
		try {
			aProductList = (ArrayList<AutoCompleteBean>) jobService
					.getProductQuoteList(theProductText);
		} catch (JobException e) {
			sendTransactionException("<b>theProductText:</b>"+theProductText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductList;
	}

	@RequestMapping(value = "/vendorsList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getVendorsList(@RequestParam("term") String theVendorText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aVendorList = null;
		try {
			aVendorList = (ArrayList<AutoCompleteBean>) jobService
					.getVendorsList(theVendorText);
		} catch (JobException e) {
			sendTransactionException("<b>theVendorText:</b>"+theVendorText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aVendorList;
	}

	@RequestMapping(value = "/manufaturerList", method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<AutoCompleteBean> getManufaturerList(
			@RequestParam("term") String theManufaturerText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aManufacturerList = null;
		try {
			aManufacturerList = (ArrayList<AutoCompleteBean>) jobService
					.getManufaturerList(theManufaturerText);
		} catch (JobException e) {
			sendTransactionException("<b>theManufaturerText:</b>"+theManufaturerText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aManufacturerList;
	}

	@RequestMapping(value = "/getFactoryID", method = RequestMethod.GET)
	public @ResponseBody
	Integer getFactoryID(
			@RequestParam(value = "rxMasterID") Integer theManufaturer,
			@RequestParam(value = "descripition") String theName,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		int aFactoryID = 0;
		try {
			aFactoryID = jobService.getFactoryID(theManufaturer, theName);
		} catch (JobException e) {
			sendTransactionException("<b>theManufaturer:</b>"+theManufaturer,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aFactoryID;
	}

	@RequestMapping(value = "/getManufacID", method = RequestMethod.GET)
	public @ResponseBody
	Integer getManufacID(
			@RequestParam(value = "rxMasterID") Integer theManufaturer,
			@RequestParam(value = "descripition") String theName,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		int aFactoryID = 0;
		try {
			aFactoryID = jobService.getFactoryID(theManufaturer, theName);
		} catch (JobException e) {
			sendTransactionException("<b>theManufaturer:</b>"+theManufaturer,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aFactoryID;
	}

	@RequestMapping(value = "/quoteDetails", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getQuoteDetails(
			@RequestParam(value = "quoteDetailID", required = false) Integer theQuoteDeatilID,
			@RequestParam(value = "rxMasterID", required = false) Integer theRxMasterID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<JoQuoteDetail> aJoQuoteDetail = null;
		try {
			aJoQuoteDetail = (ArrayList<JoQuoteDetail>) jobService
					.getQuoteDetails(theQuoteDeatilID, theRxMasterID);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDeatilID,theRxMasterID:</b>"+theQuoteDeatilID+","+theRxMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}
	@RequestMapping(value = "/quoteTemplateDetails", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getQuoteTemplateDetails(
			@RequestParam(value = "quoteDetailID", required = false) Integer theQuoteDeatilID,
			@RequestParam(value = "rxMasterID", required = false) Integer theRxMasterID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<JoQuoteTemplateDetail> aJoQuoteDetail = null;
		try {
			aJoQuoteDetail = (ArrayList<JoQuoteTemplateDetail>) jobService.getQuoteTemplateDetails(theQuoteDeatilID, theRxMasterID);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDeatilID,theRxMasterID:</b>"+theQuoteDeatilID+","+theRxMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}

	// footnotes & inline items
	@RequestMapping(value = "/manpulaterProductQuotes", method = RequestMethod.POST)
	public @ResponseBody
	Integer manpulaterProductQuotes(
			@RequestParam(value = "joQuoteDetailID", required = false) Integer theQuoteDetailID,
			@RequestParam(value = "product", required = false) String theProduct,
			@RequestParam(value = "paragraph", required = false) String theParagraph,
			@RequestParam(value = "rxManufacturerID", required = false) Integer theManufacturer,
			@RequestParam(value = "manufacturer", required = false) String theManufacturerName,
			@RequestParam(value = "itemQuantity", required = false) String theItemQuantity,
			@RequestParam(value = "cost", required = false) BigDecimal theCost,
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theHeaderID,
			@RequestParam(value = "quoteheaderid", required = false) Integer joQuoteID,
			@RequestParam(value = "veFactoryId", required = false) Short thefactoryID,
			@RequestParam(value = "inlineNote", required = false) String theInlineNote,
			@RequestParam(value = "footnote", required = false) String theFootNote,
			@RequestParam(value = "productNote", required = false) String theProductNote,
			@RequestParam(value = "percentage", required = false) BigDecimal thePercentage,
			@RequestParam(value = "price", required = false) BigDecimal thePrice,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, JobException, IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		VeFactory aVeFactory = new VeFactory();
		logger.info("percentage :: " + thePercentage);
		Integer aJoQuoteDetailId = null;
		if (operation.equals("del")) {
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			jobService.deleteProductQuote(aJoQuoteDetail);
		} else {
			if (theQuoteDetailID != null && theQuoteDetailID != 0) {
				aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			}
			aJoQuoteDetail.setProduct(theProduct);
			aJoQuoteDetail.setParagraph(theParagraph);
			aJoQuoteDetail.setRxManufacturerID(theManufacturer);
			aJoQuoteDetail.setItemQuantity(theItemQuantity);
			aJoQuoteDetail.setCost(theCost);
			aJoQuoteDetail.setPrice(thePrice);
			aJoQuoteDetail.setInlineNote(theInlineNote);
			aJoQuoteDetail.setProductNote(theFootNote);
			aJoQuoteDetail.setPercentage(thePercentage);
			if (theProductNote != "" && theProductNote != null) {
				aJoQuoteDetail.setProductNote(theProductNote);
			}
			if (theHeaderID != null && theHeaderID != 0) {
				aJoQuoteDetail.setJoQuoteHeaderID(theHeaderID);
			}
			if (joQuoteID != null && joQuoteID != 0) {
				aJoQuoteDetail.setJoQuoteHeaderID(joQuoteID);
			}
			aJoQuoteDetail.setVeFactoryId(thefactoryID);
			String aDescription = null;
			try {
				aDescription = itsVendorService.getDescription(thefactoryID);
				if (aDescription == "") {
					aDescription = null;
				}
				if (theManufacturerName != "" && theManufacturerName != null) {
					if (aDescription != null) {
						if (theManufacturerName.length() > 20) {
							String asearchText = theManufacturerName.substring(
									0, 19);
							aVeFactory.setDescription(asearchText);
						} else {
							aVeFactory.setDescription(theManufacturerName);
						}
					} else {
						if (theManufacturerName.length() > 20) {
							String asearchText = theManufacturerName.substring(
									0, 19);
							aVeFactory.setDescription(asearchText);
						} else {
							aVeFactory.setDescription(theManufacturerName);
						}
						aVeFactory.setVeFactoryID(thefactoryID);
						itsVendorService.updateManufactures(aVeFactory);
					}
				}
			} catch (VendorException e) {
				sendTransactionException("<b>theQuoteDetailID:</b>"+theQuoteDetailID,"JOB",e,session,therequest);
				logger.error(e.getMessage());
				theResponse
						.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}

		}
		if (operation.equals("add")) {
			aJoQuoteDetailId = jobService.addProductQuote(aJoQuoteDetail);
		} else if (operation.equals("edit")) {
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			jobService.updateProductQuoteFooter(aJoQuoteDetail);
		}
		return aJoQuoteDetailId;
	}

	// edit & Save
	@RequestMapping(value = "/updateProductQuotes", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateProductQuotes(
			@RequestParam(value = "joQuoteDetailID", required = false) Integer theQuoteDetailID,

			@RequestParam(value = "product", required = false) String theProduct,
			@RequestParam(value = "paragraph", required = false) String theParagraph,
			@RequestParam(value = "rxManufacturerID", required = false) Integer theManufacturer,
			@RequestParam(value = "manufacturer", required = false) String theManufacturerName,
			@RequestParam(value = "itemQuantity", required = false) String theItemQuantity,
			@RequestParam(value = "cost", required = false) String theCost,
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theHeaderID,
			@RequestParam(value = "quoteheaderid", required = false) Integer joQuoteID,
			@RequestParam(value = "veFactoryId", required = false) Short thefactoryID,
			@RequestParam(value = "inlineNote", required = false) String theInlineNote,
			@RequestParam(value = "footnote", required = false) String theFootNote,
			@RequestParam(value = "productNote", required = false) String theProductNote,
			@RequestParam(value = "percentage", required = false) String thePercentage,
			@RequestParam(value = "spec", required = false) String spec,
			@RequestParam(value = "position", required = false) String position,
			@RequestParam(value = "mult", required = false) BigDecimal mult ,
			@RequestParam(value = "price", required = false) String thePrice,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, JobException, IOException, MessagingException {

		logger.info("Quote Inline Operations Called!");
		logger.info("Operation: " + operation);
		logger.info("theQuoteDetailID: " + theQuoteDetailID);
		logger.info("theProduct: " + theProduct);
		logger.info("theParagraph: " + theParagraph);
		logger.info("theManufacturer: " + theManufacturer);
		logger.info("theItemQuantity: " + theItemQuantity);
		logger.info("theCost,Price " + theCost + thePrice);
		logger.info("joQuoteID: " + joQuoteID);
		logger.info("theHeaderID: " + theHeaderID);
		logger.info("theInlineNote: " + theInlineNote);
		logger.info("thePercentage: " + thePercentage);
		logger.info("spec: " + spec);
		logger.info("mult: " + mult);
		
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		VeFactory aVeFactory = new VeFactory();
		Integer aJoQuoteDetailId = null;
		BigDecimal price = new BigDecimal(0);
		aJoQuoteDetail.setMult(mult);
		if (thePrice != null && (!thePrice.equals(""))) {
			price = new BigDecimal(
					thePrice.trim().contains("$") ? thePrice.replaceAll("[$,]",
							"") : thePrice);
		}
		BigDecimal cost = new BigDecimal(0);
		if (theCost != null && (!theCost.equals(""))) {
			cost = new BigDecimal(
					theCost.trim().contains("$") ? theCost.replaceAll("[$,]",
							"") : theCost);
		}

		if (thePercentage.trim() == null || thePercentage.trim().equals("")) {
			thePercentage = "0";
		}
		BigDecimal precent = new BigDecimal(thePercentage.trim());
		if (spec.trim() == null || spec.trim().equals("")) {
			spec = "0";
		}

		if (precent.signum() > 0) {
			double perc = 0.0;
			perc = precent.floatValue() / 100;
			perc = 100 - precent.floatValue();
			perc = perc / 100;
			price = new BigDecimal(cost.floatValue() / perc);
			/*
			 * BigDecimal prec= new BigDecimal(100); //sellPri = productCost / [
			 * (100 - pecent) / 100 ]; prec=(prec.subtract(precent));
			 * System.out.println("PRI1: "+prec); prec=prec.divide(new
			 * BigDecimal(100)); System.out.println("PRI2: "+prec);
			 * System.out.println("PRI3: "+prec); if(prec.intValue()>0){ //price
			 * = cost.divide(new BigDecimal(Integer.valueOf(prec.intValue())));
			 * } price = cost.divide(prec);
			 */
		}
		short specs = Short.parseShort(spec.trim());
		if (operation.equals("del")) {
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			jobService.deleteProductQuote(aJoQuoteDetail);
		} else {
			BigDecimal price_new = new BigDecimal(0);
			if (thePrice != null && (!thePrice.equals(""))) {
				price_new = new BigDecimal(
						thePrice.trim().contains("$") ? thePrice.replaceAll(
								"[$,]", "") : thePrice);
			}

			aJoQuoteDetail.setProduct(theProduct);
			aJoQuoteDetail.setParagraph(theParagraph);
			aJoQuoteDetail.setRxManufacturerID(theManufacturer);
			aJoQuoteDetail.setItemQuantity(theItemQuantity);
			aJoQuoteDetail.setCost(cost);
			aJoQuoteDetail.setPrice(price_new);
			aJoQuoteDetail.setInlineNote(theInlineNote);
			aJoQuoteDetail.setProductNote(theFootNote);
			if (precent.compareTo(new BigDecimal(100)) == -1) {
				aJoQuoteDetail.setPercentage(precent);
			}

			aJoQuoteDetail.setSpec(specs);
			if (theProductNote != "" && theProductNote != null) {
				aJoQuoteDetail.setProductNote(theProductNote);
			}
			if (theHeaderID != null && theHeaderID != 0) {
				aJoQuoteDetail.setJoQuoteHeaderID(theHeaderID);
			}
			if (joQuoteID != null && joQuoteID != 0) {
				aJoQuoteDetail.setJoQuoteHeaderID(joQuoteID);
			}
			aJoQuoteDetail.setVeFactoryId(thefactoryID);
			String aDescription = null;
			try {
				aDescription = itsVendorService.getDescription(thefactoryID);
				if (aDescription == "") {
					aDescription = null;
				}
				if (theManufacturerName != "" && theManufacturerName != null) {
					if (aDescription != null) {
						if (theManufacturerName.length() > 20) {
							String asearchText = theManufacturerName.substring(
									0, 19);
							aVeFactory.setDescription(asearchText);
						} else {
							aVeFactory.setDescription(theManufacturerName);
						}
					} else {
						if (theManufacturerName.length() > 20) {
							String asearchText = theManufacturerName.substring(
									0, 19);
							aVeFactory.setDescription(asearchText);
						} else {
							aVeFactory.setDescription(theManufacturerName);
						}
						aVeFactory.setVeFactoryID(thefactoryID);
						itsVendorService.updateManufactures(aVeFactory);
					}
				}
			} catch (VendorException e) {
				sendTransactionException("<b>theQuoteDetailID:</b>"+theQuoteDetailID,"JOB",e,session,therequest);
				logger.error(e.getMessage());
				theResponse
						.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}

		}
		if (operation.equals("add")) {
			logger.info("Quote Add Inline Called");
			aJoQuoteDetailId = jobService.addProductQuote(aJoQuoteDetail);
			// position

			// updateItemPosition(aJoQuoteDetailId,aJoQuoteDetail.getJoQuoteHeaderID());
		} else if (operation.equals("edit")) {
			logger.info("Quote Edit Inline Called");
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			jobService.updateProductQuote(aJoQuoteDetail);
		}
		return 1;
	}

	@RequestMapping(value = "/manpulaProductQuotes", method = RequestMethod.POST)
	public @ResponseBody
	Integer manpulaProductQuotes(
			@RequestParam(value = "joQuoteDetailID", required = false) Integer theQuoteDetailID,
			@RequestParam(value = "product", required = false) String theProduct,
			@RequestParam(value = "paragraph", required = false) String theParagraph,
			@RequestParam(value = "rxManufacturerID", required = false) Integer theManufacturer,
			@RequestParam(value = "itemQuantity", required = false) String theItemQuantity,
			@RequestParam(value = "cost", required = false) BigDecimal theCost,
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theHeaderID,
			@RequestParam(value = "quoteheaderid", required = false) Integer joQuoteID,
			@RequestParam(value = "veFactoryId", required = false) Short thefactoryID,
			@RequestParam(value = "inlineNote", required = false) String theInlineNote,
			@RequestParam(value = "percentage", required = false) BigDecimal thePercentage,
			@RequestParam(value = "footnote", required = false) String theFootNote,
			@RequestParam(value = "productNote", required = false) String theProductNote,
			@RequestParam(value = "price", required = false) BigDecimal thePrice,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, JobException, IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			aJoQuoteDetail.setProduct(theProduct);
			aJoQuoteDetail.setParagraph(theParagraph);
			aJoQuoteDetail.setRxManufacturerID(theManufacturer);
			aJoQuoteDetail.setItemQuantity(theItemQuantity);
			aJoQuoteDetail.setCost(theCost);
			aJoQuoteDetail.setPrice(thePrice);
			aJoQuoteDetail.setInlineNote(theInlineNote);
			aJoQuoteDetail.setProductNote(theFootNote);
			aJoQuoteDetail.setPercentage(thePercentage);
			if (theHeaderID != null && theHeaderID != 0) {
				aJoQuoteDetail.setJoQuoteHeaderID(theHeaderID);
			}
			if (theProductNote != "" && theProductNote != null) {
				aJoQuoteDetail.setProductNote(theProductNote);
			}
			if (joQuoteID != null && joQuoteID != 0) {
				aJoQuoteDetail.setJoQuoteHeaderID(joQuoteID);
			}
			aJoQuoteDetail.setVeFactoryId(thefactoryID);
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			jobService.updateProductQuote(aJoQuoteDetail);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDetailID:</b>"+theQuoteDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		aJoQuoteDetail.setVeFactoryId(thefactoryID);
		aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
		jobService.updateProductQuote(aJoQuoteDetail);
		return 0;
	}

	@RequestMapping(value = "/SavelinetextInfo", method = RequestMethod.POST)
	public @ResponseBody
	Integer insertLineInfo(
			/*
			 * @RequestParam(value="lineItemName", required= false) String
			 * theLineItem,
			 * 
			 * @RequestParam(value="footerLine", required= false) String
			 * theFooterLine,
			 * 
			 * @RequestParam(value="quoteHeaderID", required= false) String
			 * theJoQuoteHeaderID,
			 * 
			 * @RequestParam(value="inlineTextEditor", required= false) String
			 * theLineItemTextEditor,
			 * 
			 * @RequestParam(value="quoteDetailID", required= false) Integer
			 * theJoQuoteDetailID
			 */@RequestParam(value = "lineItem[]", required = false) ArrayList<?> theJoQuoteDetail,
			HttpSession session, HttpServletRequest therequest,HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		/*
		 * neItem.push(inlineText); aLineItem.push(joQuoteHeaderID);
		 * aLineItem.push(quoteDetailID);
		 */
		try {
			JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
			if (theJoQuoteDetail.get(0) != ""
					&& theJoQuoteDetail.get(0) != null) {
				
				String aInLine = theJoQuoteDetail.get(0).toString();
				//String aInLineReplace = aInLine.replaceAll("'", "&And");
				aJoQuoteDetail.setInlineNote(aInLine);
				logger.info("Data from:"+aInLine);
			}
			if (theJoQuoteDetail.get(1) != ""
					&& theJoQuoteDetail.get(1) != null) {
				String aQuoteHeaderID = theJoQuoteDetail.get(1).toString();
				aJoQuoteDetail.setJoQuoteHeaderID(Integer
						.valueOf(aQuoteHeaderID));
			}
			if (theJoQuoteDetail.get(2) != ""
					&& theJoQuoteDetail.get(2) != null) {
				String aQuoteDetailID = theJoQuoteDetail.get(2).toString();
				aJoQuoteDetail.setJoQuoteDetailId(Integer
						.valueOf(aQuoteDetailID));
			}
			jobService.updateLineInfo(aJoQuoteDetail);
		} catch (JobException e) {
			sendTransactionException("<b>SavelinetextInfo:</b>SavelinetextInfo","JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "/saveQuoteTempInlineNote", method = RequestMethod.POST)
	public @ResponseBody
	void insertLineInfo(
			@RequestParam(value = "joQuoteTempDetailId", required = false) Integer theJoQuoteDetailID,
			@RequestParam(value = "inlineNote", required = false) String inlineNote,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException {
		try {
			JoQuoteTemplateDetail aDetail = new JoQuoteTemplateDetail();
			aDetail.setJoQuoteTemplateDetailId(theJoQuoteDetailID);
			aDetail.setInlineNote(inlineNote);
			quoteTemplateService.updatelineItemInlineNote(aDetail);
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>theJoQuoteDetailID:</b>"+theJoQuoteDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}

	}

	@RequestMapping(value = "/deleteQuoteDetailID", method = RequestMethod.GET)
	public @ResponseBody
	List<JoQuoteDetail> getJoQuoteDetailID(
			@RequestParam(value = "joHeaderQuoteID", required = false) Integer theJoDetailID,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		ArrayList<JoQuoteDetail> aJoQuoteDetailList = null;
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			aJoQuoteDetailList = (ArrayList<JoQuoteDetail>) jobService
					.getJoQuoteDetailID(theJoDetailID);
			for (int index = 0; index < aJoQuoteDetailList.size(); index++) {
				int ajoQuoteDetailID = aJoQuoteDetailList.get(index)
						.getJoQuoteDetailId();
				aJoQuoteDetail.setJoQuoteDetailId(ajoQuoteDetailID);
				jobService.deleteProductQuote(aJoQuoteDetail);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theJoDetailID:</b>"+theJoDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetailList;
	}

	@RequestMapping(value = "/deleteQuickQuoteDetail", method = RequestMethod.GET)
	public @ResponseBody
	Integer deleteProductQuote(
			@RequestParam(value = "joHeaderQuoteDetailID", required = false) Integer theJoQuoteDetailID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			aJoQuoteDetail.setJoQuoteDetailId(theJoQuoteDetailID);
			jobService.deleteProductQuote(aJoQuoteDetail);
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuoteDetailID:</b>"+theJoQuoteDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "/filterQuoteTypeID", method = RequestMethod.GET)
	public @ResponseBody
	CuMasterType filterQuoteTypeID(
			@RequestParam(value = "rxMasterId", required = false) Integer theRxMasterID,
			HttpSession theSession) throws ParseException {
		CuMasterType aCumastertype = null;
		setCustomerRecord(theRxMasterID);
		if (customerRecord.getCuMasterTypeId() != null) {
			aCumastertype = itsCuMasterService.getCustomerType(customerRecord
					.getCuMasterTypeId());
		}
		return aCumastertype;
	}

	@RequestMapping(value = "/getEmailAddress", method = RequestMethod.GET)
	public @ResponseBody
	String getEmailAddress(
			@RequestParam(value = "contactID", required = false) Integer theContactID,
			HttpSession session, HttpServletResponse response)
			throws ParseException, IOException {
		String aEmailAddress = null;
		try {
			aEmailAddress = companyService.getEmailID(theContactID);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		if (aEmailAddress == null) {
			aEmailAddress = "";
		}
		return aEmailAddress;
	}

	public Cumaster getCustomerRecord() {
		return customerRecord;
	}

	public void setCustomerRecord(int rxMasterId) {
		this.customerRecord = itsCuMasterService.getCustomerDetails(rxMasterId);
	}

	@RequestMapping(value = "/deleteQuickQuote", method = RequestMethod.GET)
	public @ResponseBody
	Integer deleteQuickQuote(
			@RequestParam(value = "joHeaderQuoteID", required = false) Integer theJoQuoteHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
		try {
			aJoQuoteHeader.setJoQuoteHeaderId(theJoQuoteHeaderID);
			jobService.deleteQuickQuote(aJoQuoteHeader);
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuoteHeaderID:</b>"+theJoQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "/addContact", method = RequestMethod.POST)
	public @ResponseBody
	Rxcontact addContact(
			@RequestParam(value = "rxMasterId", required = false) Integer therxMasterId,
			@RequestParam(value = "firstName", required = false) String thefirstName,
			@RequestParam(value = "jobPosition", required = false) String therole,
			@RequestParam(value = "email", required = false) String theEmail,
			@RequestParam(value = "directLine", required = false) String thedirectline,
			@RequestParam(value = "cell", required = false) String thecell,
			@RequestParam(value = "lastName", required = false) String thelastName,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {

		Rxcontact aRxcontact = new Rxcontact();
		try {
			aRxcontact.setRxMasterId(therxMasterId);
			aRxcontact.setFirstName(thefirstName);
			aRxcontact.setInActive(false);
			aRxcontact.setDirectLine(thedirectline);
			aRxcontact.setEmail(theEmail);
			aRxcontact.setCell(thecell);
			aRxcontact.setJobPosition(therole);
			aRxcontact.setLastName(thelastName);
			jobService.addNewContact(aRxcontact);
		} catch (JobException e) {
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxcontact;
	}

	@RequestMapping(value = "/planAndSpec", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster updatePlanAndSpec(
			@RequestParam(value = "bin_number_name", required = false) String theBinNumber,
			@RequestParam(value = "plan_date_name", required = false) String thePlanDate,
			@RequestParam(value = "plan_nuber_name", required = false) String thePlanNumber,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		Jomaster aJomasterReturn = null;
		try {
			aJomaster.setBinNumber(theBinNumber);
			aJomaster.setPlanDate(DateUtils.parseDate(thePlanDate,
					new String[] { "MM/dd/yyyy" }));
			aJomaster.setPlanNumbers(thePlanNumber);
			aJomaster.setJoMasterId(theJoMasterID);
			aJomasterReturn = jobService.updatePlanAndSpec(aJomaster);
		} catch (JobException e) {
			sendTransactionException("<b>theJoMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJomasterReturn;
	}
	
	@RequestMapping(value = "/updateJobSource", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster updateJobSource(
			@RequestParam(value = "sourceDodge", required = false) Integer theSourceDodge,
			@RequestParam(value = "sourceISqft", required = false) Integer sourceISqft,
			@RequestParam(value = "sourceLDI", required = false) Integer sourceLDI,
			@RequestParam(value = "sourceOther", required = false) Integer sourceOther,
			@RequestParam(value = "sourceReport", required = false) String sourceReport,
			@RequestParam(value = "otherSource", required = false) String otherSource,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		Jomaster aJomasterReturn = null;
		logger.info("Source-1:"+theSourceDodge+"\nSource-2:"+sourceISqft+"\nSource-3:"+sourceLDI+"\nSource-4:"+sourceOther);
		try {
			if(theSourceDodge==1){
				aJomaster.setSource1(true);
			}else{
				aJomaster.setSource1(false);
			}
			if(sourceISqft==1){
				aJomaster.setSource2(true);
			}else{
				aJomaster.setSource2(false);
			}
			if(sourceLDI==1){
				aJomaster.setSource3(true);
			}else{
				aJomaster.setSource3(false);
			}
			if(sourceOther==1){
				aJomaster.setSource4(true);
			}else{
				aJomaster.setSource4(false);
			}
			aJomaster.setSourceReport1(sourceReport);
			aJomaster.setOtherSource(otherSource);
			aJomaster.setJoMasterId(theJoMasterID);
			aJomasterReturn = jobService.updateJobSources(aJomaster);
		} catch (JobException e) {
			sendTransactionException("<b>theJoMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJomasterReturn;
	}
	
	@RequestMapping(value = "/updateAddendums", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster UpdateAddendums(
			@RequestParam(value = "received_name", required = false) String theReceivedNo,
			@RequestParam(value = "quoteThru_name", required = false) String theQuoteThru,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, JobException, IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		Jomaster aJomasterReturn = null;
		try {
			aJomaster.setAddendumReceived(theReceivedNo);
			aJomaster.setAddendumQuotedThru(theQuoteThru);
			aJomaster.setJoMasterId(theJoMasterID);
			aJomasterReturn = jobService.updateAddendums(aJomaster);
		} catch (JobException e) {
			sendTransactionException("<b>theJoMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJomasterReturn;
	}

	@RequestMapping(value = "/SaveQuoteCustomerDetailInfo", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteHeader insertQuoteInCustomer(
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theJoHeaderID,
			@RequestParam(value = "totalCost", required = false) BigDecimal theTotalcost,
			@RequestParam(value = "totalPrice", required = false) BigDecimal theTotalPrice,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
		try {
			aJoQuoteHeader.setQuoteAmount(theTotalPrice);
			aJoQuoteHeader.setCostAmount(theTotalcost);
			aJoQuoteHeader.setJoQuoteHeaderId(theJoHeaderID);
			jobService.updateQuoteDetails(aJoQuoteHeader);
		} catch (JobException e) {
			sendTransactionException("<b>theJoHeaderID:</b>"+theJoHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteHeader;
	}

	@RequestMapping(value = "/updateJobamount", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster updateJobamount(
			@RequestParam(value = "EstimateCost", required = false) BigDecimal theEstimateCost,
			@RequestParam(value = "ContractAmount", required = false) BigDecimal theContractAmt,
			@RequestParam(value = "EstimateProfit", required = false) BigDecimal theEstimateProfit,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, JobException, IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		Jomaster aJomasterReturn = null;
		try {
			aJomaster.setContractAmount(theContractAmt);
			aJomaster.setEstimatedCost(theEstimateCost);
			aJomaster.setEstimatedProfit(theEstimateProfit);
			aJomaster.setJoMasterId(theJoMasterID);
			aJomasterReturn = jobService.updateAmount(aJomaster);
		} catch (JobException e) {
			sendTransactionException("<b>theJoMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJomasterReturn;
	}

	@RequestMapping(value = "/getQuoteDetils", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteDetail getSingleQuoteDetails(
			@RequestParam(value = "joDetailsID", required = false) Integer theQuoteDetailID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			aJoQuoteDetail = jobService.getSingleQuoteDetails(aJoQuoteDetail);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDetailID:</b>"+theQuoteDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}

	@RequestMapping(value = "/updateLineItemPosition", method = RequestMethod.POST)
	public @ResponseBody
	joQuoteDetailPosition updateLineItemPosition(
			@RequestParam(value = "selectedRowID", required = false) Integer theSelectedRowID,
			@RequestParam(value = "selectedPositionDetailID", required = false) Integer theSelectedPositionDetailID,
			@RequestParam(value = "selectedQuoteDetailID", required = false) Integer theSelectedQuoteDetailID,
			@RequestParam(value = "selectedJoQuoteHeaderID", required = false) Integer theSelectedJoQuoteHeaderID,
			@RequestParam(value = "abovePositionRowID", required = false) Integer theAbovePositionRowID,
			@RequestParam(value = "abovePositionDetailID", required = false) Integer theAbovePositionDetailID,
			@RequestParam(value = "aboveQuoteDetailID", required = false) Integer theAboveQuoteDetailID,
			@RequestParam(value = "oper", required = false) String theOperation,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		joQuoteDetailPosition aJoQuoteDetailPosition = new joQuoteDetailPosition();
		logger.info("theSelectedRowID: " + theSelectedRowID);
		logger.info("theSelectedPositionDetailID: "
				+ theSelectedPositionDetailID);
		logger.info("theSelectedQuoteDetailID: " + theSelectedQuoteDetailID);
		logger.info("theSelectedJoQuoteHeaderID: " + theSelectedJoQuoteHeaderID);
		logger.info("theAbovePositionRowID: " + theAbovePositionRowID);
		logger.info("theAbovePositionDetailID: " + theAbovePositionDetailID);
		logger.info("theAboveQuoteDetailID: " + theAboveQuoteDetailID);
		logger.info("oper: " + theOperation);
		try {
			aJoQuoteDetailPosition.setSelectedRowID(theSelectedRowID);
			aJoQuoteDetailPosition
					.setSelectedPositionDetailID(theSelectedPositionDetailID);
			aJoQuoteDetailPosition
					.setSelectedQuoteDetailID(theSelectedQuoteDetailID);
			aJoQuoteDetailPosition
					.setSelectedJoQuoteHeaderID(theSelectedJoQuoteHeaderID);
			aJoQuoteDetailPosition.setAbovePositionRowID(theAbovePositionRowID);
			aJoQuoteDetailPosition
					.setAbovePositionDetailID(theAbovePositionDetailID);
			aJoQuoteDetailPosition.setAboveQuoteDetailID(theAboveQuoteDetailID);
			jobService.updateLineItemUpDownPosition(aJoQuoteDetailPosition);
			jobService.updateLineItemUpPosition(aJoQuoteDetailPosition);
		} catch (JobException e) {
			sendTransactionException("<b>theSelectedQuoteDetailID,theSelectedJoQuoteHeaderID:</b>"+theSelectedQuoteDetailID+","+theSelectedJoQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetailPosition;
	}

	@RequestMapping(value = "/updateItemPosition", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteDetail updateItemPosition(
			@RequestParam(value = "joQuoteDetailID", required = false) Integer theQuoteDetailID,
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theQuoteHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			Integer quotePosition = jobService
					.getQuotePositionMaxValue(theQuoteHeaderID);
			quotePosition = quotePosition + 1;
			logger.info("New Quote Position: " + quotePosition);
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			aJoQuoteDetail.setPosition(quotePosition);
			aJoQuoteDetail = jobService.updateItemPosition(aJoQuoteDetail);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDetailID,theQuoteHeaderID:</b>"+theQuoteDetailID+","+theQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}

	@RequestMapping(value = "/updateQuoteTempItemPosition", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteTemplateDetail updateQuoteTempItemPosition(
			@RequestParam(value = "joQuoteTempDetailID", required = false) Integer theQuoteDetailID,
			@RequestParam(value = "joQuoteTempHeaderID", required = false) Integer theQuoteHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JoQuoteTemplateDetail aJoQuoteTemplateDetail = new JoQuoteTemplateDetail();
		try {
			Integer quotePosition = jobService
					.getQuoteTempPositionMaxValue(theQuoteHeaderID);
			quotePosition = quotePosition + 1;
			logger.info("New Quote Position: " + quotePosition);
			aJoQuoteTemplateDetail.setJoQuoteTemplateDetailId(theQuoteDetailID);
			aJoQuoteTemplateDetail.setPosition(quotePosition);
			aJoQuoteTemplateDetail = jobService.updateQuoteTempItemPosition(aJoQuoteTemplateDetail);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDetailID,theQuoteHeaderID:</b>"+theQuoteDetailID+","+theQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteTemplateDetail;
	}
	
	@RequestMapping(value = "/updateQuoteDetailsPosition", method = RequestMethod.POST)
	public @ResponseBody
	JoQuoteDetail updateInlineItemPosition(
			@RequestParam(value = "selectedQuoteDetailID", required = false) Integer theQuoteDetailId,
			@RequestParam(value = "selectedJoQuoteHeaderID", required = false) Integer theJoQuoteHeaderId,
			@RequestParam(value = "operate", required = false) String operation,
			@RequestParam(value = "difference", required = false) Integer difference,
			@RequestParam(value = "endQuoteDetailID", required = false) Integer endQuoteDetailID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailId);
			aJoQuoteDetail.setJoQuoteHeaderID(theJoQuoteHeaderId);
			aJoQuoteDetail = jobService.updateInlineItemPosition(
					aJoQuoteDetail, operation, difference, endQuoteDetailID);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDetailId,theJoQuoteHeaderId:</b>"+theQuoteDetailId+","+theJoQuoteHeaderId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}

	@RequestMapping(value = "/getLineItemsForCopyQuote", method = RequestMethod.POST)
	public @ResponseBody
	JoQuoteDetail getLineItemsForCopyQuote(
			@RequestParam(value = "previousJoQuoteHeader", required = false) Integer thePreviousJoQuoteHeader,
			@RequestParam(value = "currentJoQuoteheaderID", required = false) Integer theCurrentJoQuoteheaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		ArrayList<JoQuoteDetail> aJoQuoteDetailList = null;
		try {
			String aManufacturerName = "";
			String aOperation = "add";
			aJoQuoteDetailList = (ArrayList<JoQuoteDetail>) jobService
					.getJoQuoteDetails(thePreviousJoQuoteHeader);
			for (int index = 0; index < aJoQuoteDetailList.size(); index++) {
				manpulaterProductQuotes(aJoQuoteDetailList.get(index)
						.getJoQuoteDetailId(), aJoQuoteDetailList.get(index)
						.getProduct(), aJoQuoteDetailList.get(index)
						.getParagraph(), aJoQuoteDetailList.get(index)
						.getRxManufacturerID(), aManufacturerName,
						aJoQuoteDetailList.get(index).getItemQuantity(),
						aJoQuoteDetailList.get(index).getCost(), aOperation,
						theCurrentJoQuoteheaderID, theCurrentJoQuoteheaderID,
						aJoQuoteDetailList.get(index).getVeFactoryId(),
						aJoQuoteDetailList.get(index).getInlineNote(),
						aJoQuoteDetailList.get(index).getProductNote(),
						aJoQuoteDetailList.get(index).getProductNote(),
						aJoQuoteDetailList.get(index).getPercentage(),
						aJoQuoteDetailList.get(index).getPrice(), session,therequest,
						theResponse);
			}
		} catch (JobException e) {
			sendTransactionException("<b>thePreviousJoQuoteHeader,theCurrentJoQuoteheaderID:</b>"+thePreviousJoQuoteHeader+","+theCurrentJoQuoteheaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}

	@RequestMapping(value = "/updateEmailAddress", method = RequestMethod.GET)
	public @ResponseBody
	Rxcontact updateEmailAddress(
			@RequestParam(value = "email", required = false) String theEmailAddr,
			@RequestParam(value = "contactID", required = false) Integer theContactID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws CompanyException, IOException, MessagingException {
		Rxcontact aRxcontact = new Rxcontact();
		try {
			aRxcontact.setEmail(theEmailAddr);
			aRxcontact.setRxContactId(theContactID);
			aRxcontact = companyService.updateEmailAddr(aRxcontact);
		} catch (CompanyException e) {
			sendTransactionException("<b>theEmailAddr,theContactID:</b>"+theEmailAddr+","+theContactID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxcontact;
	}

	@RequestMapping(value = "/getAdvacedSearchJobList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAdvancedSearchList(
			@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value = "jobNumber_name", required = false) String theJobNumber,
			@RequestParam(value = "city_name", required = false) String theCity,
			@RequestParam(value = "project_code", required = false) String theProjectCode,
			@RequestParam(value = "bidDateName", required = false) boolean theRangerchk,
			@RequestParam(value = "rangepickerName", required = false) String theRangerDate,
			@RequestParam(value = "thruPickerName", required = false) String theThruPicker,
			@RequestParam(value = "budget_name", required = false) boolean thebudgetChk,
			@RequestParam(value = "bid_name", required = false) boolean theBidChk,
			@RequestParam(value = "quote_name", required = false) boolean theQuoteChk,
			@RequestParam(value = "booked_name", required = false) boolean theBookedChk,
			@RequestParam(value = "closed_name", required = false) boolean theClosedChk,
			@RequestParam(value = "submitted_name", required = false) boolean theSubmittedChk,
			@RequestParam(value = "planning_name", required = false) boolean theplanningChk,
			@RequestParam(value = "lost_name", required = false) boolean theLostChk,
			@RequestParam(value = "abondoned_name", required = false) boolean theAboundonedChk,
			@RequestParam(value = "reject_name", required = false) boolean theRejeckChk,
			@RequestParam(value = "overBudget_name", required = false) boolean theOverBudChk,
			@RequestParam(value = "customer_name", required = false) String theCustomerName,
			@RequestParam(value = "architect_name", required = false) String theArchitectName,
			@RequestParam(value = "engineer_name", required = false) String theEngineerName,
			@RequestParam(value = "gc_name", required = false) String theGCName,
			@RequestParam(value = "team_status_name", required = false) Integer theTeamStatusOpt,
			@RequestParam(value = "salesrep_name", required = false) Integer theSalesRep,
			@RequestParam(value = "csr_name", required = false) Integer theCSR,
			@RequestParam(value = "salesMgr_name", required = false) Integer theSalesMgr,
			@RequestParam(value = "engineerEmp_name", required = false) Integer theEngineerEmp,
			@RequestParam(value = "prjMgr_name", required = false) Integer theProjectMgr,
			@RequestParam(value = "takeOff_name", required = false) Integer theTakeOff,
			@RequestParam(value = "quoteBy_name", required = false) Integer theQuoteBy,
			@RequestParam(value = "employee_assignee_name", required = false) Integer theEmpAssOpt,
			@RequestParam(value = "customer_po_name", required = false) String theCustPONum,
			@RequestParam(value = "report_name", required = false) String theReportName,
			@RequestParam(value = "division_name", required = false) Integer theDivisionID,
			@RequestParam(value = "sort_by_name", required = false) Integer theSortByName,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws ParseException, IOException, MessagingException {
		String rangeDate = "";
		String thruDate = "";
		Jomaster aJomaster = new Jomaster();
		CustomResponse aResponse = new CustomResponse();
		try {
			aJomaster.setJobNumber(theJobNumber);
			aJomaster.setLocationCity(theCity);
			aJomaster.setDescription(theProjectCode);
			String aSortBy = "";
			StringBuilder aJobSearchTotal = new StringBuilder(" WHERE ");
			if (theDivisionID == -1) {
				aJobSearchTotal.append("");
			} else {
				aJobSearchTotal.append("joMaster.coDivisionID = "
						+ theDivisionID + " AND");
			}
			if (theRangerchk == true) {
				String[] aRangeSearch = theRangerDate.split("/");
				String aRag0 = aRangeSearch[0];
				String aRag1 = aRangeSearch[1];
				//aRag1 = String.valueOf(Integer.valueOf(aRag1) - 1);
				String aRag2 = aRangeSearch[2];
				rangeDate = aRag2 + "-" + aRag0 + "-" + aRag1;
				String[] aThruSearch = theThruPicker.split("/");
				String aThru0 = aThruSearch[0];
				String aThru1 = aThruSearch[1];
				//aThru1 = String.valueOf(Integer.valueOf(aThru1) + 1);
				String aThru2 = aThruSearch[2];
				thruDate = aThru2 + "-" + aThru0 + "-" + aThru1;
				aJobSearchTotal.append(" BidDate BETWEEN '" + rangeDate
						+ "' AND  DATE_ADD('"+thruDate+"', INTERVAL 1 DAY) AND");
			}
			if (!theJobNumber.trim().isEmpty() && theJobNumber != null)
				aJobSearchTotal.append(" joMaster.JobNumber like '%"
						+ theJobNumber + "%' AND");
			if (!theCity.trim().isEmpty() && theCity != null)
				aJobSearchTotal.append(" joMaster.LocationCity like '%"
						+ JobUtil.removeSpecialcharacterswithslash(theCity) + "%' AND");
			if (!theProjectCode.trim().isEmpty() && theProjectCode != null)
				aJobSearchTotal.append(" joMaster.Description like '%"
						+ JobUtil.removeSpecialcharacterswithslash(theProjectCode) + "%' AND");
			if (!theCustPONum.trim().isEmpty() && theCustPONum != null)
				aJobSearchTotal.append(" joMaster.CustomerPONumber like '%"
						+ JobUtil.removeSpecialcharacterswithslash(theCustPONum) + "%' AND");
			if (theEmpAssOpt == 1 && theSalesRep != null)
				aJobSearchTotal.append(" joMaster.cuAssignmentID0 = "
						+ theSalesRep + " AND");
			if (theEmpAssOpt == 2)
				aJobSearchTotal.append(" joMaster.cuAssignmentID1 = " + theCSR
						+ " AND");
			if (theEmpAssOpt == 3)
				aJobSearchTotal.append(" joMaster.cuAssignmentID2 = "
						+ theSalesMgr + " AND");
			if (theEmpAssOpt == 4)
				aJobSearchTotal.append(" joMaster.cuAssignmentID3 = "
						+ theEngineerEmp + " AND");
			if (theEmpAssOpt == 5)
				aJobSearchTotal.append(" joMaster.cuAssignmentID4 = "
						+ theProjectMgr + " AND");
			if (theEmpAssOpt == 6)
				aJobSearchTotal.append(" joMaster.cuAssignmentID5 = "
						+ theTakeOff + " AND");
			if (theEmpAssOpt == 7)
				aJobSearchTotal.append(" joMaster.cuAssignmentID6 = "
						+ theQuoteBy + " AND");
			if (theTeamStatusOpt == 1 && theCustomerName != "")
				aJobSearchTotal.append(" rxm.Name like '%" + JobUtil.removeSpecialcharacterswithslash(theCustomerName)
						+ "%' AND");
			if (theTeamStatusOpt == 2)
				aJobSearchTotal.append(" joMaster.rXCategory1 = "
						+ theArchitectName + " AND");
			if (theTeamStatusOpt == 3)
				aJobSearchTotal.append(" joMaster.rXCategory2 = "
						+ theEngineerName + " AND");
			if (theTeamStatusOpt == 4)
				aJobSearchTotal.append(" joMaster.rXCategory3 = " + theGCName
						+ " AND");
			if (thebudgetChk || theBidChk || theQuoteChk || theBookedChk
					|| theClosedChk || theSubmittedChk || theplanningChk
					|| theLostChk || theAboundonedChk || theRejeckChk
					|| theOverBudChk) {
				aJobSearchTotal.append(" joMaster.JobStatus IN (");
				if (theBidChk)
					aJobSearchTotal.append(0).append(",");
				if (theQuoteChk)
					aJobSearchTotal.append(1).append(",");
				if (theLostChk)
					aJobSearchTotal.append(2).append(",");
				if (theBookedChk)
					aJobSearchTotal.append(3).append(",");
				if (theClosedChk)
					aJobSearchTotal.append(4).append(",");
				if (theSubmittedChk)
					aJobSearchTotal.append(5).append(",");
				if (thebudgetChk)
					aJobSearchTotal.append(6).append(",");
				if (theAboundonedChk)
					aJobSearchTotal.append(-1).append(",");
				if (theRejeckChk)
					aJobSearchTotal.append(-2).append(",");
				if (theOverBudChk)
					aJobSearchTotal.append(-3).append(",");
				if (theplanningChk)
					aJobSearchTotal.append(-4).append(",");

				String aSubString = aJobSearchTotal.toString().substring(0,
						(aJobSearchTotal.toString().length() - 1));
				aJobSearchTotal = new StringBuilder(aSubString).append(")");
			}
			if(theSidx.equals(null)||theSidx.equals(""))
			{
				if (theSortByName == 1) {
					aSortBy = "BidDate";
				} else if (theSortByName == 2) {
					aSortBy = "JobNumber";
				} else if (theSortByName == 3) {
					aSortBy = "Description";
				} else if (theSortByName == 4) {
					aSortBy = "LocationCity";
				} else if (theSortByName == 5) {
					aSortBy = "cuAssignmentID0";
				} else if (theSortByName == 6) {
					aSortBy = "JobStatus";
				} else if (theSortByName == 7) {
					aSortBy = "Name";
				} else if (theSortByName == 8) {
					aSortBy = "CustomerPONumber";
				}
			}
			else
			{
				aSortBy = theSidx;
			}
			int aFrom, aTo;
			thePage = 1;
			theRows = 50;
			/*
			 * int aTotalCount =
			 * jobService.getRecordCount(aJobSearchTotal.toString()); aTo =
			 * (theRows * thePage); aFrom = aTo - theRows; if (aTo >
			 * aTotalCount) aTo = aTotalCount;
			 */
			aFrom = 1;
			aTo = 50;
			String sort ="";
			
			if(theSord !=null)
				sort =theSord;
			
			List<JobCustomerBean> aAdvancedresults = jobService
					.getAdvancedSearchResults(aFrom, aTo,
							aJobSearchTotal.toString(), aSortBy,sort);
			aResponse.setRows(aAdvancedresults);
			aResponse.setRecords(String.valueOf(aAdvancedresults.size()));
			aResponse.setPage(thePage);
			// aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double)
			// theRows));
		} catch (JobException e) {
			sendTransactionException("<b>getAdvacedSearchJobList:</b>getAdvacedSearchJobList","JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/updateLastQuoteAndRev", method = RequestMethod.GET)
	public @ResponseBody
	Jobidder updateLastQuoteAndRev(
			@RequestParam(value = "revision", required = false) String theRevision,
			@RequestParam(value = "joBidderID", required = false) Integer theJoBidderID,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			@RequestParam(value = "quoteTypeID", required = false) Integer theQuoteTypeID,
			@RequestParam(value = "quoteDate", required = false) String theDateNow,
			@RequestParam(value = "status", required = false) String status,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws Exception {
		Jobidder aJobidder = new Jobidder();
		try {
			String aJoMasterID = Integer.toString(theJoMasterID);
			String aQuoteTypeID = Integer.toString(theQuoteTypeID);
			theRevision = "sendMail";
			String aQuoteRev = "value";
			Integer aJoQuoteHeaderID = pdfService.getQuoteHeaderID(
					aQuoteTypeID, theRevision, aJoMasterID, aQuoteRev);
			JoQuoteHeader aJoQuoteHeader = jobService
					.getSingleQuoteHeaderDetails(aJoQuoteHeaderID);
			
			logger.info("Date from Client: "+DateUtils.parseDate(theDateNow,new String[] { "MM/dd/yyyy hh:mm a" }));
			aJobidder.setQuoteDate(DateUtils.parseDate(theDateNow,
					new String[] { "MM/dd/yyyy hh:mm a" }));
			aJobidder.setQuoteRev(aJoQuoteHeader.getQuoteRev());
			aJobidder.setJoBidderId(theJoBidderID);
			if(status.equals("sent")){
				aJobidder.setQuoteemailstatus(1);
			}else if(status.equals("Printed")){
				aJobidder.setQuoteemailstatus(2);
			}else{
				aJobidder.setQuoteemailstatus(0);
			}
			jobService.updateLastQuoteAndRev(aJobidder);
		} catch (JobException e) {
			sendTransactionException("<b>theJoBidderID,theJoMasterID:</b>"+theJoBidderID+","+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJobidder;
	}

	@RequestMapping(value = "/checkQuoteType", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteHeader checkQuoteType(
			@RequestParam(value = "revision", required = false) String theRevision,
			@RequestParam(value = "joBidderID", required = false) Integer theJoBidderID,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			@RequestParam(value = "quoteTypeID", required = false) Integer theQuoteTypeID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws Exception {
		JoQuoteHeader aJoQuoteHeaderID = new JoQuoteHeader();
		Integer aJoQuoteHeaderId = 0;
		try {
			aJoQuoteHeaderId = pdfService.getHeaderID(theQuoteTypeID,
					theJoMasterID);
			if (aJoQuoteHeaderId != 0) {
				aJoQuoteHeaderID = jobService
						.getSingleQuoteHeaderDetails(aJoQuoteHeaderId);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theJoBidderID,theJoMasterID:</b>"+theJoBidderID+","+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteHeaderID;
	}

	@RequestMapping(value = "/checkQuoteTypeAndRev", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkQuoteTypeAndRev(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "quoteType", required = false) String theQuoteType,
			@RequestParam(value = "quoteRev", required = false) String theQuoteRev,
			@RequestParam(value = "joHeaderId", required = false) Integer thejoHeaderId,
			@RequestParam(value = "operation", required = false) String theOperation,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		List<JobQuotesBidListBean> aQuotes = null;
		boolean aResultQuoteType = false;
		String aQuoteType = "";
		String aQuoteRev = "";
		try {
			aQuotes = jobService.getQuotesList(joMasterID);
			for (int index = 0; index < aQuotes.size(); index++) {
				aQuoteType = aQuotes.get(index).getQuoteTypeID() + "";
				aQuoteRev = aQuotes.get(index).getRev();
				Integer aQuoteheaderid = aQuotes.get(index)
						.getJoQuoteHeaderID();
				if (theOperation.equals("add")) {
					String aQuoteHeaderIdStr = aQuoteheaderid + "";
					String theQuoteHeaderIdStr = thejoHeaderId + "";
					if (!aQuoteHeaderIdStr.equals(theQuoteHeaderIdStr)) {
						if (aResultQuoteType = aQuoteType
								.equalsIgnoreCase(theQuoteType)) {
							// if (theQuoteRev.equalsIgnoreCase(aQuoteRev)
							// || theQuoteRev.equals("")) {
							if (theQuoteRev.equalsIgnoreCase(aQuoteRev)) {
								return true;

							} else {
								aResultQuoteType = false;
							}
						}
					}

				} else if (!theOperation.equals("add")) {
					String aQuoteHeaderIdStr = aQuoteheaderid + "";
					String theQuoteHeaderIdStr = thejoHeaderId + "";
					if (!aQuoteHeaderIdStr.equals(theQuoteHeaderIdStr)) {
						if (aResultQuoteType = aQuoteType
								.equalsIgnoreCase(theQuoteType)) {
							// if (theQuoteRev.equalsIgnoreCase(aQuoteRev)
							// || theQuoteRev.equals("")) {
							if (theQuoteRev.equalsIgnoreCase(aQuoteRev)) {
								return true;

							} else {
								aResultQuoteType = false;
							}
						}
					}
				}
			}
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber ,thejoHeaderId:</b>"+theJobNumber+","+thejoHeaderId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResultQuoteType;
	}

	@RequestMapping(value = "/getAdvacedSearchChkJobList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAdvacedSearchChkJobList(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "jobNumber_name", required = false) String theJobNumber,
			@RequestParam(value = "city_name", required = false) String theCity,
			@RequestParam(value = "project_code", required = false) String theProjectCode,
			@RequestParam(value = "bidDateName", required = false) boolean theRangerchk,
			@RequestParam(value = "rangepickerName", required = false) String theRangerDate,
			@RequestParam(value = "thruPickerName", required = false) String theThruPicker,
			@RequestParam(value = "budget_name", required = false) boolean thebudgetChk,
			@RequestParam(value = "bid_name", required = false) boolean theBidChk,
			@RequestParam(value = "quote_name", required = false) boolean theQuoteChk,
			@RequestParam(value = "booked_name", required = false) boolean theBookedChk,
			@RequestParam(value = "closed_name", required = false) boolean theClosedChk,
			@RequestParam(value = "submitted_name", required = false) boolean theSubmittedChk,
			@RequestParam(value = "planning_name", required = false) boolean theplanningChk,
			@RequestParam(value = "lost_name", required = false) boolean theLostChk,
			@RequestParam(value = "abondoned_name", required = false) boolean theAboundonedChk,
			@RequestParam(value = "reject_name", required = false) boolean theRejeckChk,
			@RequestParam(value = "overBudget_name", required = false) boolean theOverBudChk,
			@RequestParam(value = "customer_name", required = false) String theCustomerName,
			@RequestParam(value = "architect_name", required = false) String theArchitectName,
			@RequestParam(value = "engineer_name", required = false) String theEngineerName,
			@RequestParam(value = "gc_name", required = false) String theGCName,
			@RequestParam(value = "team_status_name", required = false) Integer theTeamStatusOpt,
			@RequestParam(value = "salesrep_name", required = false) Integer theSalesRep,
			@RequestParam(value = "csr_name", required = false) Integer theCSR,
			@RequestParam(value = "salesMgr_name", required = false) Integer theSalesMgr,
			@RequestParam(value = "engineerEmp_name", required = false) Integer theEngineerEmp,
			@RequestParam(value = "prjMgr_name", required = false) Integer theProjectMgr,
			@RequestParam(value = "takeOff_name", required = false) Integer theTakeOff,
			@RequestParam(value = "quoteBy_name", required = false) Integer theQuoteBy,
			@RequestParam(value = "employee_assignee_name", required = false) Integer theEmpAssOpt,
			@RequestParam(value = "customer_po_name", required = false) String theCustPONum,
			@RequestParam(value = "report_name", required = false) String theReportName,
			@RequestParam(value = "division_name", required = false) Integer theDivisionID,
			@RequestParam(value = "sort_by_name", required = false) Integer theSortByName,
			HttpServletResponse theResponse,HttpSession session, HttpServletRequest therequest) throws ParseException,
			JobException, IOException, MessagingException {
		String rangeDate = "";
		String thruDate = "";
		Jomaster aJomaster = new Jomaster();
		CustomResponse aResponse = null;
		List<JobCustomerBean> aAdvancedresults = null;
		try {
			aJomaster.setJobNumber(theJobNumber);
			aJomaster.setLocationCity(theCity);
			aJomaster.setDescription(theProjectCode);
			String aSortBy = "";
			StringBuilder aJobSearchTotal = new StringBuilder(" WHERE ");
			if (theDivisionID == -1) {
				aJobSearchTotal.append("");
			} else {
				aJobSearchTotal.append("joMaster.coDivisionID = "
						+ theDivisionID + " AND");
			}
			if (theRangerchk == true) {
				String[] aRangeSearch = theRangerDate.split("/");
				String aRag0 = aRangeSearch[0];
				String aRag1 = aRangeSearch[1];
				//aRag1 = String.valueOf(Integer.valueOf(aRag1) - 1);
				String aRag2 = aRangeSearch[2];
				rangeDate = aRag2 + "-" + aRag0 + "-" + aRag1;
				String[] aThruSearch = theThruPicker.split("/");
				String aThru0 = aThruSearch[0];
				String aThru1 = aThruSearch[1];
				//aThru1 = String.valueOf(Integer.valueOf(aThru1) + 1);
				String aThru2 = aThruSearch[2];
				thruDate = aThru2 + "-" + aThru0 + "-" + aThru1;
				System.out.println(rangeDate+"=========="+thruDate);
				aJobSearchTotal.append(" BidDate BETWEEN '" + rangeDate
						+ "' AND  DATE_ADD('"+thruDate+"', INTERVAL 1 DAY) AND");
			}
			if (!theJobNumber.trim().isEmpty() && theJobNumber != null)
				aJobSearchTotal.append(" joMaster.JobNumber like '%"
						+ theJobNumber + "%' AND");
			if (!theCity.trim().isEmpty() && theCity != null)
				aJobSearchTotal.append(" joMaster.LocationCity like '%"
						+ JobUtil.removeSpecialcharacterswithslash(theCity) + "%' AND");
			if (!theProjectCode.trim().isEmpty() && theProjectCode != null)
				aJobSearchTotal.append(" joMaster.Description like '%"
						+ JobUtil.removeSpecialcharacterswithslash(theProjectCode) + "%' AND");
			if (!theCustPONum.trim().isEmpty() && theCustPONum != null)
				aJobSearchTotal.append(" joMaster.CustomerPONumber like '%"
						+ JobUtil.removeSpecialcharacterswithslash(theCustPONum) + "%' AND");
			if (theEmpAssOpt == 1 && theSalesRep != null)
				aJobSearchTotal.append(" joMaster.cuAssignmentID0 = "
						+ theSalesRep + " AND");
			if (theEmpAssOpt == 2)
				aJobSearchTotal.append(" joMaster.cuAssignmentID1 = " + theCSR
						+ " AND");
			if (theEmpAssOpt == 3)
				aJobSearchTotal.append(" joMaster.cuAssignmentID2 = "
						+ theSalesMgr + " AND");
			if (theEmpAssOpt == 4)
				aJobSearchTotal.append(" joMaster.cuAssignmentID3 = "
						+ theEngineerEmp + " AND");
			if (theEmpAssOpt == 5)
				aJobSearchTotal.append(" joMaster.cuAssignmentID4 = "
						+ theProjectMgr + " AND");
			if (theEmpAssOpt == 6)
				aJobSearchTotal.append(" joMaster.cuAssignmentID5 = "
						+ theTakeOff + " AND");
			if (theEmpAssOpt == 7)
				aJobSearchTotal.append(" joMaster.cuAssignmentID6 = "
						+ theQuoteBy + " AND");
			if (theTeamStatusOpt == 1 && theCustomerName != "")
				aJobSearchTotal.append(" rxm.Name like '%" + JobUtil.removeSpecialcharacterswithslash(theCustomerName)
						+ "%' AND");
			if (theTeamStatusOpt == 2)
				aJobSearchTotal.append(" joMaster.rXCategory1 = "
						+ theArchitectName + " AND");
			if (theTeamStatusOpt == 3)
				aJobSearchTotal.append(" joMaster.rXCategory2 = "
						+ theEngineerName + " AND");
			if (theTeamStatusOpt == 4)
				aJobSearchTotal.append(" joMaster.rXCategory3 = " + theGCName
						+ " AND");
			if (thebudgetChk || theBidChk || theQuoteChk || theBookedChk
					|| theClosedChk || theSubmittedChk || theplanningChk
					|| theLostChk || theAboundonedChk || theRejeckChk
					|| theOverBudChk) {
				aJobSearchTotal.append(" joMaster.JobStatus IN (");
				if (theBidChk)
					aJobSearchTotal.append(0).append(",");
				if (theQuoteChk)
					aJobSearchTotal.append(1).append(",");
				if (theLostChk)
					aJobSearchTotal.append(2).append(",");
				if (theBookedChk)
					aJobSearchTotal.append(3).append(",");
				if (theClosedChk)
					aJobSearchTotal.append(4).append(",");
				if (theSubmittedChk)
					aJobSearchTotal.append(5).append(",");
				if (thebudgetChk)
					aJobSearchTotal.append(6).append(",");
				if (theAboundonedChk)
					aJobSearchTotal.append(-1).append(",");
				if (theRejeckChk)
					aJobSearchTotal.append(-2).append(",");
				if (theOverBudChk)
					aJobSearchTotal.append(-3).append(",");
				if (theplanningChk)
					aJobSearchTotal.append(-4).append(",");

				String aSubString = aJobSearchTotal.toString().substring(0,(aJobSearchTotal.toString().length() - 1));
				aJobSearchTotal = new StringBuilder(aSubString).append(")");
			}

			if (theSortByName == 1) {
				aSortBy = "BidDate";
			} else if (theSortByName == 2) {
				aSortBy = "JobNumber";
			} else if (theSortByName == 3) {
				aSortBy = "Description";
			} else if (theSortByName == 4) {
				aSortBy = "LocationCity";
			} else if (theSortByName == 5) {
				aSortBy = "cuAssignmentID0";
			} else if (theSortByName == 6) {
				aSortBy = "JobStatus";
			} else if (theSortByName == 7) {
				aSortBy = "Name";
			} else if (theSortByName == 8) {
				aSortBy = "CustomerPONumber";
			}
			int aFrom = 0, aTo = 0;
			
			String sort ="";
			
			if(theSord !=null)
				sort =theSord;
			aAdvancedresults = jobService.getAdvancedSearchResults(aFrom, aTo,
					aJobSearchTotal.toString(), aSortBy,sort);
			
			aResponse = new CustomResponse();
			aResponse.setRows(aAdvancedresults);
			aResponse.setRecords( String.valueOf(aAdvancedresults.size()) );
			aResponse.setPage( thePage );
			//aResponse.setTotal((int) Math.ceil((double)aTotalCount / (double) theRows));
			
		} catch (JobException e) {
			sendTransactionException("<b>MethodName:</b>getAdvacedSearchChkJobList","JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/updateQuoteProperties", method = RequestMethod.POST)
	public @ResponseBody
	JoQuoteHeader updateQuoteProperties(
			@RequestParam(value = "quantityDisplayName", required = false) boolean theQuantityDisplayName,
			@RequestParam(value = "quantityPrintName", required = false) boolean theQuantityPrintName,
			@RequestParam(value = "quantityUnderlineName", required = false) boolean theQuantityUnderlineName,
			@RequestParam(value = "quantityBoldName", required = false) boolean theQuantityBoldName,
			@RequestParam(value = "quantityDisplayParaName", required = false) boolean theDisplayParaName,
			@RequestParam(value = "quantityprintParaName", required = false) boolean thePrintParaName,
			@RequestParam(value = "quantityUnderlineParaName", required = false) boolean theUnderlineParaName,
			@RequestParam(value = "quantityBoldParaName", required = false) boolean theBoldParaName,
			@RequestParam(value = "quantityDisplayManufName", required = false) boolean theDisplayManufName,
			@RequestParam(value = "quantityPrintManufName", required = false) boolean thePrintManufName,
			@RequestParam(value = "quantityUnderlineManufName", required = false) boolean theUnderlineManufName,
			@RequestParam(value = "quantityBoldManufName", required = false) boolean theBoldManufName,
			@RequestParam(value = "quantityDisplaySpecName", required = false) boolean theDisplaySpecName,
			@RequestParam(value = "quantityprintSpecName", required = false) boolean thePrintSpecName,
			@RequestParam(value = "quantityUnderlineSpecName", required = false) boolean theUnderlineSpecName,
			@RequestParam(value = "quantityBoldSpecName", required = false) boolean theBoldSpecName,
			@RequestParam(value = "quantityDisplayCostName", required = false) boolean theDisplayCostName,
			@RequestParam(value = "quantityUnderlineCostName", required = false) boolean theUnderlineCostName,
			@RequestParam(value = "quantityBoldCostName", required = false) boolean theBoldCostName,
			@RequestParam(value = "quantityDisplayMultiName", required = false) boolean theDisplayMultiName,
			@RequestParam(value = "quantityUnderlineMultiName", required = false) boolean theUnderlineMultiName,
			@RequestParam(value = "quantityBoldMultiName", required = false) boolean theBoldMultiName,
			@RequestParam(value = "quantityDisplayPriceName", required = false) boolean theDisplayPriceName,
			@RequestParam(value = "quantityUnderlinePriceName", required = false) boolean theUnderlinePriceName,
			@RequestParam(value = "quantityBoldPriceName", required = false) boolean theBoldPriceName,
			@RequestParam(value = "quantityprintPriceName", required = false) boolean thePrintPriceName,
			@RequestParam(value = "quantityprintCostName", required = false) boolean thePrintCostname,
			@RequestParam(value = "quantityprintMultiName", required = false) boolean quantityprintMultiName,
			@RequestParam(value = "inlineNoteFullPageName", required = false) boolean theInlineNoteFullPage,
			@RequestParam(value = "PrintLineNumName", required = false) boolean thePrintLineNumber,
			@RequestParam(value = "printSummationName", required = false) boolean thePrintSummation,
			@RequestParam(value = "hidePriceName", required = false) boolean theHidePriceName,
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theJoQuoteHeaderID,
			@RequestParam(value = "isQuoteTempProp", required = false) Boolean isQuoteTemplateProperty,
			@RequestParam(value = "quantityDisplayItemName", required = false) boolean theDisplayItemName,
			@RequestParam(value = "quantityPrintItemName", required = false) boolean thePrintItemName,
			@RequestParam(value = "quantityUnderlineItemName", required = false) boolean theUnderlineItemName,
			@RequestParam(value = "quantityBoldItemName", required = false) boolean theBoldItemName,
			@RequestParam(value = "quantityDisplayHeaderName", required = false) boolean theDisplayHeaderName,
			@RequestParam(value = "quantityPrintHeaderName", required = false) boolean thePrintHeaderName,
			@RequestParam(value = "quantityUnderlineHeaderName", required = false) boolean theUnderlineHeaderName,
			@RequestParam(value = "quantityBoldHeaderName", required = false) boolean theBoldHeaderName,
			@ModelAttribute JoQuoteHeader theQuoteHeader,
			HttpServletResponse theResponse, HttpSession session,HttpServletRequest therequest)
			throws IOException, MessagingException {
		JoQuoteProperties aJoQuoteProperty = new JoQuoteProperties();
		JoQuoteTemplateProperties ajoQuoteTempProperties = new JoQuoteTemplateProperties();
		UserBean aUserBean;
		JoQuoteHeader aJoQuote = null;
		byte aQuantityDisplayName = (byte) (theQuantityDisplayName ? 1 : 0);
		byte aQuantityPrintName = (byte) (theQuantityPrintName ? 1 : 0);

		byte aDisplayParaName = (byte) (theDisplayParaName ? 1 : 0);
		byte aPrintParaName = (byte) (thePrintParaName ? 1 : 0);
		byte aDisplayManufName = (byte) (theDisplayManufName ? 1 : 0);
		byte aPrintManufName = (byte) (thePrintManufName ? 1 : 0);
		byte aDisplaySpecName = (byte) (theDisplaySpecName ? 1 : 0);
		byte aPrintSpecName = (byte) (thePrintSpecName ? 1 : 0);
		byte aDisplayCostName = (byte) (theDisplayCostName ? 1 : 0);
		byte aDisplayMultiName = (byte) (theDisplayMultiName ? 1 : 0);
		byte aDisplayPriceName = (byte) (theDisplayPriceName ? 1 : 0);
		byte aPrintPriceName = (byte) (thePrintPriceName ? 1 : 0);
		byte aInlineNoteFullPage = (byte) (theInlineNoteFullPage ? 1 : 0);
		byte aPrintLineNumber = (byte) (thePrintLineNumber ? 1 : 0);
		byte aPrintSummation = (byte) (thePrintSummation ? 1 : 0);
		byte aPrintcost = (byte) (thePrintCostname ? 1 : 0);
		byte aPrintmult = (byte) (quantityprintMultiName ? 1 : 0);
		byte aHidePrice = (byte) (theHidePriceName ? 1 : 0);
		byte aQuantityUnderlineName = (byte) (theQuantityUnderlineName ? 1 : 0);
		byte aQuantityBoldName = (byte) (theQuantityBoldName ? 1 : 0);
		byte aUnderlineParaName = (byte) (theUnderlineParaName ? 1 : 0);
		byte aBoldParaName = (byte) (theBoldParaName ? 1 : 0);
		byte aUnderlineManufName = (byte) (theUnderlineManufName ? 1 : 0);
		byte aBoldManufName = (byte) (theBoldManufName ? 1 : 0);
		byte aUnderlineSpecName = (byte) (theUnderlineSpecName ? 1 : 0);
		byte aBoldSpecName = (byte) (theBoldSpecName ? 1 : 0);
		byte aUnderlineCostName = (byte) (theUnderlineCostName ? 1 : 0);
		byte aUnderlineMultiName = (byte) (theUnderlineMultiName ? 1 : 0);
		byte aBoldCostName = (byte) (theBoldCostName ? 1 : 0);
		byte aBoldMultiName = (byte) (theBoldMultiName ? 1 : 0);
		byte aUnderlinePriceName = (byte) (theUnderlinePriceName ? 1 : 0);
		byte aBoldPriceName = (byte) (theBoldPriceName ? 1 : 0);

		byte aDisplayItemName = (byte) (theDisplayItemName ? 1 : 0);
		byte aPrintItemName = (byte) (thePrintItemName ? 1 : 0);
		byte aUnderlineItemName = (byte) (theUnderlineItemName ? 1 : 0);
		byte aBoldItemName = (byte) (theBoldItemName ? 1 : 0);

		byte aDisplayHeaderName = (byte) (theDisplayHeaderName ? 1 : 0);
		byte aPrintHeaderName = (byte) (thePrintHeaderName ? 1 : 0);
		byte aUnderlineHeaderName = (byte) (theUnderlineHeaderName ? 1 : 0);
		byte aBoldHeaderName = (byte) (theBoldHeaderName ? 1 : 0);

		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		Integer aUserId = aUserBean.getUserId();
		try {
			if (!isQuoteTemplateProperty) {
				aJoQuoteProperty.setDisplayQuantity(aQuantityDisplayName);
				aJoQuoteProperty.setPrintQuantity(aQuantityPrintName);
				aJoQuoteProperty.setDisplayParagraph(aDisplayParaName);
				aJoQuoteProperty.setPrintParagraph(aPrintParaName);
				aJoQuoteProperty.setDisplayManufacturer(aDisplayManufName);
				aJoQuoteProperty.setPrintManufacturer(aPrintManufName);
				aJoQuoteProperty.setDisplaySpec(aDisplaySpecName);
				aJoQuoteProperty.setPrintSpec(aPrintSpecName);
				aJoQuoteProperty.setPrintCost(aPrintcost);
				aJoQuoteProperty.setPrintMult(aPrintmult);
				aJoQuoteProperty.setDisplayCost(aDisplayCostName);
				aJoQuoteProperty.setDisplayMult(aDisplayMultiName);
				aJoQuoteProperty.setDisplayPrice(aDisplayPriceName);
				aJoQuoteProperty.setPrintPrice(aPrintPriceName);
				aJoQuoteProperty.setUserLoginID(aUserId);
				aJoQuoteProperty.setNotesFullWidth(aInlineNoteFullPage);
				aJoQuoteProperty.setLineNumbers(aPrintLineNumber);
				aJoQuoteProperty.setPrintTotal(aPrintSummation);
				aJoQuoteProperty.setHidePrice(aHidePrice);

				aJoQuoteProperty.setUnderlineQuantity(aQuantityUnderlineName);
				aJoQuoteProperty.setBoldQuantity(aQuantityBoldName);
				aJoQuoteProperty.setUnderlineParagraph(aUnderlineParaName);
				aJoQuoteProperty.setBoldParagraph(aBoldParaName);
				aJoQuoteProperty.setUnderlineManufacturer(aUnderlineManufName);
				aJoQuoteProperty.setBoldManufacturer(aBoldManufName);
				aJoQuoteProperty.setUnderlineSpec(aUnderlineSpecName);
				aJoQuoteProperty.setBoldSpec(aBoldSpecName);
				aJoQuoteProperty.setBoldCost(aBoldCostName);
				aJoQuoteProperty.setBoldMult(aBoldMultiName);
				aJoQuoteProperty.setUnderlineCost(aUnderlineCostName);
				aJoQuoteProperty.setUnderlineMult(aUnderlineMultiName);
				aJoQuoteProperty.setUnderlinePrice(aUnderlinePriceName);
				aJoQuoteProperty.setBoldPrice(aBoldPriceName);

				aJoQuoteProperty.setDisplayItem(aDisplayItemName);
				aJoQuoteProperty.setPrintItem(aPrintItemName);
				aJoQuoteProperty.setUnderlineItem(aUnderlineItemName);
				aJoQuoteProperty.setBoldItem(aBoldItemName);

				aJoQuoteProperty.setDisplayHeader(aDisplayHeaderName);
				aJoQuoteProperty.setPrintHeader(aPrintHeaderName);
				aJoQuoteProperty.setUnderlineHeader(aUnderlineHeaderName);
				aJoQuoteProperty.setBoldHeader(aBoldHeaderName);

			} else {
				ajoQuoteTempProperties.setDisplayQuantity(aQuantityDisplayName);
				ajoQuoteTempProperties.setPrintQuantity(aQuantityPrintName);
				ajoQuoteTempProperties.setDisplayParagraph(aDisplayParaName);
				ajoQuoteTempProperties.setPrintParagraph(aPrintParaName);
				ajoQuoteTempProperties
						.setDisplayManufacturer(aDisplayManufName);
				ajoQuoteTempProperties.setPrintManufacturer(aPrintManufName);
				ajoQuoteTempProperties.setDisplaySpec(aDisplaySpecName);
				ajoQuoteTempProperties.setPrintSpec(aPrintSpecName);
				ajoQuoteTempProperties.setPrintCost(aPrintcost);
				ajoQuoteTempProperties.setPrintMult(aPrintmult);
				ajoQuoteTempProperties.setDisplayCost(aDisplayCostName);
				ajoQuoteTempProperties.setDisplayMult(aDisplayMultiName);
				ajoQuoteTempProperties.setDisplayPrice(aDisplayPriceName);
				ajoQuoteTempProperties.setPrintPrice(aPrintPriceName);
				ajoQuoteTempProperties.setUserLoginID(aUserId);
				ajoQuoteTempProperties.setNotesFullWidth(aInlineNoteFullPage);
				ajoQuoteTempProperties.setLineNumbers(aPrintLineNumber);
				ajoQuoteTempProperties.setPrintTotal(aPrintSummation);
				ajoQuoteTempProperties.setHidePrice(aHidePrice);

				ajoQuoteTempProperties
						.setUnderlineQuantity(aQuantityUnderlineName);
				ajoQuoteTempProperties.setBoldQuantity(aQuantityBoldName);
				ajoQuoteTempProperties
						.setUnderlineParagraph(aUnderlineParaName);
				ajoQuoteTempProperties.setBoldParagraph(aBoldParaName);
				ajoQuoteTempProperties
						.setUnderlineManufacturer(aUnderlineManufName);
				ajoQuoteTempProperties.setBoldManufacturer(aBoldManufName);
				ajoQuoteTempProperties.setUnderlineSpec(aUnderlineSpecName);
				ajoQuoteTempProperties.setBoldSpec(aBoldSpecName);
				ajoQuoteTempProperties.setBoldCost(aBoldCostName);
				ajoQuoteTempProperties.setBoldMult(aBoldMultiName);
				ajoQuoteTempProperties.setUnderlineCost(aUnderlineCostName);
				ajoQuoteTempProperties.setUnderlineMult(aUnderlineMultiName);
				ajoQuoteTempProperties.setUnderlinePrice(aUnderlinePriceName);
				ajoQuoteTempProperties.setBoldPrice(aBoldPriceName);

				ajoQuoteTempProperties.setDisplayItem(aDisplayItemName);
				ajoQuoteTempProperties.setPrintItem(aPrintItemName);
				ajoQuoteTempProperties.setUnderlineItem(aUnderlineItemName);
				ajoQuoteTempProperties.setBoldItem(aBoldItemName);

				ajoQuoteTempProperties.setDisplayHeader(aDisplayHeaderName);
				ajoQuoteTempProperties.setPrintHeader(aPrintHeaderName);
				ajoQuoteTempProperties.setUnderlineHeader(aUnderlineHeaderName);
				ajoQuoteTempProperties.setBoldHeader(aBoldHeaderName);
			}
			aJoQuote = jobService.updateQuoteProperties(aJoQuoteProperty,
					ajoQuoteTempProperties, isQuoteTemplateProperty);
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuoteHeaderID:</b>"+theJoQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuote;
	}

	@RequestMapping(value = "/loadQuoteTemplates", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse loadQuoteTemplates(HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse) throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			List<JoQuotetemplateHeader> aQuoteTemplateList = quoteTemplateService
					.loadQuoteTemplate();
			aResponse.setRows(aQuoteTemplateList);
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>MethodName:</b>loadQuoteTemplates","JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/getQuoteTemplateLineItems", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getQuoteTemplateLineItems(
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "joQuoteTemplateHeaderId", required = true) Integer joQuoteTemplateHeaderId)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			List<JoQuoteTemplateDetail> aQuoteTemplateLineItemsList = quoteTemplateService
					.getQuoteTemplateLineItems(joQuoteTemplateHeaderId);
			aResponse.setRows(aQuoteTemplateLineItemsList);
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>MethodName:</b>loadQuoteTemplates","JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/updateTemplateLineItem", method = RequestMethod.POST)
	public @ResponseBody
	Integer loadQuoteTemplates(
			@RequestParam(value = "cost", required = false) String costTemplate,
			@RequestParam(value = "productNote", required = false) String footnoteTemplate,
			@RequestParam(value = "inlineNote", required = false) String inlineNoteTemplate,
			@RequestParam(value = "itemQuantity", required = false) String itemQuantityTemplate,
			@RequestParam(value = "joQuoteTemplateDetailId", required = false) Integer joQuoteTemplateDetailID,
			@RequestParam(value = "joQuoteTemplateHeaderId", required = false) Integer joQuoteTemplateHeaderID,
			@RequestParam(value = "manufacturerName", required = false) String manufacturerTemplate,
			@RequestParam(value = "oper", required = false) String operLineItem,
			@RequestParam(value = "spec", required = false) Short specTemplate,
			@RequestParam(value = "paragraph", required = false) String paragraphTemplate,
			@RequestParam(value = "mult", required = false) BigDecimal mult,
			@RequestParam(value = "price", required = false) String priceTemplate,
			@RequestParam(value = "product", required = false) String productTemplate,
			@RequestParam(value = "rxManufacturerId", required = false) Integer rxManufacturerIDTemplate,
			@RequestParam(value = "veFactoryId", required = false) Integer veFactoryIdTemplate,
			@RequestParam(value = "quoteTemplateTypeDetailName", required = false) Integer cuMasterTypeId,
			@RequestParam(value = "quoteTemplateTotalPrice", required = false) BigDecimal quoteAmount,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		
		logger.info("joQuoteTemplateHeaderID: "+joQuoteTemplateHeaderID+"\n costTemplate: "+costTemplate+"\nfootnoteTemplate:"+footnoteTemplate+"\ninlineNoteTemplate:"+
				inlineNoteTemplate+"\nitemQuantityTemplate:"+itemQuantityTemplate+"\nmanufacturerTemplate:"+manufacturerTemplate+"\noperLineItem:"+operLineItem+
				"\nparagraphTemplate:"+paragraphTemplate+"\npercentageTemplate:"+mult+"\npriceTemplate:"+priceTemplate);
		Integer joquoteHeaderId = -1;
		JoQuoteTemplateDetail aJoQuoteTemplateDetail = new JoQuoteTemplateDetail();
		JoQuotetemplateHeader aJoQuotetemplateHeader = new JoQuotetemplateHeader();
		JoQuotetemplateHeader theJoQuotetemplateHeader = new JoQuotetemplateHeader();
		BigDecimal cost = new BigDecimal(0);
		if (costTemplate != null && (!costTemplate.equals(""))) {
			cost = new BigDecimal(
					costTemplate.trim().contains("$") ? costTemplate.replaceAll("[$,]","") : costTemplate);
		}
		BigDecimal price = new BigDecimal(0);
		if (priceTemplate != null && (!priceTemplate.equals(""))) {
			price = new BigDecimal(
					priceTemplate.trim().contains("$") ? priceTemplate.replaceAll("[$,]","") : priceTemplate);
		}

		aJoQuoteTemplateDetail.setCost(cost);
		aJoQuoteTemplateDetail.setInlineNote(inlineNoteTemplate);
		aJoQuoteTemplateDetail.setItemQuantity(itemQuantityTemplate);
		aJoQuoteTemplateDetail
				.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderID);
		aJoQuoteTemplateDetail.setRxManufacturerId(rxManufacturerIDTemplate);
		aJoQuoteTemplateDetail.setParagraph(paragraphTemplate);
		aJoQuoteTemplateDetail.setProduct(productTemplate);
		aJoQuoteTemplateDetail.setProductNote(footnoteTemplate);
		aJoQuoteTemplateDetail.setPrice(price);
		logger.info("Spec Tem: "+specTemplate);
		aJoQuoteTemplateDetail.setSpec(specTemplate);
		aJoQuoteTemplateDetail.setMult(mult);
		try {
			if (operLineItem.equals("add")) {
				JoQuotetemplateHeader ajoHeader = new JoQuotetemplateHeader();
				//ajoHeader.setTemplateName(theTemplateName);
				ajoHeader.setCuMasterTypeId(cuMasterTypeId);
				//ajoHeader.setRemarks(remarks);
				//ajoHeader.setQuoteAmount(quoteAmount);
				ajoHeader.setGlobalTemplate(true);
				if (joQuoteTemplateHeaderID == null) {
					//joquoteHeaderId = quoteTemplateService.addQuoteTemplate(ajoHeader);
					aJoQuoteTemplateDetail.setJoQuoteTemplateHeaderId(joquoteHeaderId);
				} else {
					joquoteHeaderId = joQuoteTemplateHeaderID;
				}
				quoteTemplateService.addTemplateLineItem(aJoQuoteTemplateDetail);
			}
			
			if (operLineItem.equals("edit")) {
				joquoteHeaderId = joQuoteTemplateHeaderID;
				aJoQuoteTemplateDetail.setJoQuoteTemplateDetailId(joQuoteTemplateDetailID);
				quoteTemplateService.editTemplateLineItem(aJoQuoteTemplateDetail);
			}
			if (operLineItem.equals("delete")) {
				joquoteHeaderId = joQuoteTemplateHeaderID;
				aJoQuoteTemplateDetail
						.setJoQuoteTemplateDetailId(joQuoteTemplateDetailID);
				quoteTemplateService
						.deleteTemplateLineItem(aJoQuoteTemplateDetail);
				
			}
			try {
				theJoQuotetemplateHeader = quoteTemplateService.getTemplateDetailsAmounts(joQuoteTemplateHeaderID);
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Integer status  = 0;			
			
			aJoQuotetemplateHeader.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderID);
			aJoQuotetemplateHeader.setCostAmount(theJoQuotetemplateHeader.getCostAmount());
			aJoQuotetemplateHeader.setQuoteAmount(theJoQuotetemplateHeader.getQuoteAmount());
			logger.info("CostAmount: "+theJoQuotetemplateHeader.getCostAmount()+"\nQuoteAmount: "+theJoQuotetemplateHeader.getQuoteAmount());
			status = quoteTemplateService.editQuoteTemplateAmounts(aJoQuotetemplateHeader);
			logger.info("After Update Template Amount: "+status);
			
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateHeaderID,joQuoteTemplateDetailID:</b>"+joQuoteTemplateHeaderID+","+joQuoteTemplateDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return joquoteHeaderId;
	}

	
	@RequestMapping(value = "/updateTemplateProductNote", method = RequestMethod.POST)
	public @ResponseBody
	Integer loadQuoteTemplates(
			
			@RequestParam(value = "productNote", required = false) String footnoteTemplate,
			@RequestParam(value = "joQuoteTemplateDetailId", required = false) Integer joQuoteTemplateDetailID,
			@RequestParam(value = "joQuoteTemplateHeaderId", required = false) Integer joQuoteTemplateHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		logger.info("joQuoteTemplateHeaderID: "+joQuoteTemplateHeaderID);
		Integer joquoteHeaderId = -1;
		JoQuoteTemplateDetail aJoQuoteTemplateDetail = new JoQuoteTemplateDetail();
		
		
		try {
				aJoQuoteTemplateDetail.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderID);
				aJoQuoteTemplateDetail.setProductNote(footnoteTemplate);
				aJoQuoteTemplateDetail.setJoQuoteTemplateDetailId(joQuoteTemplateDetailID);
				quoteTemplateService.updatelineItemProductNote(aJoQuoteTemplateDetail);
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateDetailID,joQuoteTemplateDetailID:</b>"+joQuoteTemplateHeaderID+","+joQuoteTemplateDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return joquoteHeaderId;
	}
	
	@RequestMapping(value = "/deleteTemplateLineItem", method = RequestMethod.GET)
	public @ResponseBody
	Integer deleteQuoteTemplates(
			@RequestParam(value = "joQuoteTemplateDetailId", required = false) Integer joQuoteTemplateDetailID,
			@RequestParam(value = "joQuoteTemplateHeaderId", required = false) Integer joQuoteTemplateHeaderID,
			@RequestParam(value = "oper", required = false) String operTemplate,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		logger.info("joQuoteTemplateDetailsId: "+joQuoteTemplateDetailID);
		Integer joquoteHeaderId = -1;
		JoQuoteTemplateDetail aJoQuoteTemplateDetail = new JoQuoteTemplateDetail();
		JoQuotetemplateHeader theJoQuotetemplateHeader = new JoQuotetemplateHeader();
		JoQuotetemplateHeader aJoQuotetemplateHeader = new JoQuotetemplateHeader();
		
		int delStatus = 0;
		try {
				aJoQuoteTemplateDetail.setJoQuoteTemplateDetailId(joQuoteTemplateDetailID);
				delStatus = quoteTemplateService.deleteTemplateLineItem(aJoQuoteTemplateDetail);
				Integer status  = 0;
				try {
					theJoQuotetemplateHeader = quoteTemplateService.getTemplateDetailsAmounts(joQuoteTemplateHeaderID);
				} catch (JobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(delStatus>0){
				aJoQuotetemplateHeader.setCostAmount(theJoQuotetemplateHeader.getCostAmount());
				aJoQuotetemplateHeader.setQuoteAmount(theJoQuotetemplateHeader.getQuoteAmount());
				aJoQuotetemplateHeader.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderID);
				logger.info("CostAmount: "+theJoQuotetemplateHeader.getCostAmount()+"\nQuoteAmount: "+theJoQuotetemplateHeader.getQuoteAmount());
				status = quoteTemplateService.editQuoteTemplateAmounts(aJoQuotetemplateHeader);
				logger.info("After Update Template Amount: "+status);
				}
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateDetailID,joQuoteTemplateDetailID:</b>"+joQuoteTemplateHeaderID+","+joQuoteTemplateDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return joquoteHeaderId;
	}

	@RequestMapping(value = "/updateTemplateInlineNote", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateTemplateInlineNote(
			
			@RequestParam(value = "inlineNote", required = false) String inlineNoteTemplate,
			@RequestParam(value = "joQuoteTemplateDetailId", required = false) Integer joQuoteTemplateDetailID,
			@RequestParam(value = "joQuoteTemplateHeaderId", required = false) Integer joQuoteTemplateHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		logger.info("joQuoteTemplateHeaderID: "+joQuoteTemplateHeaderID);
		logger.info("inlineNote: "+inlineNoteTemplate);
		logger.info("joQuoteTemplateDetailId: "+joQuoteTemplateDetailID);
		Integer joquoteHeaderId = -1;
		JoQuoteTemplateDetail aJoQuoteTemplateDetail = new JoQuoteTemplateDetail();
		try {
			aJoQuoteTemplateDetail.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderID);
			aJoQuoteTemplateDetail.setInlineNote(inlineNoteTemplate);
				aJoQuoteTemplateDetail.setJoQuoteTemplateDetailId(joQuoteTemplateDetailID);
				quoteTemplateService.updatelineItemInlineNote(aJoQuoteTemplateDetail);
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateDetailID,joQuoteTemplateDetailID:</b>"+joQuoteTemplateHeaderID+","+joQuoteTemplateDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return joquoteHeaderId;
	}
	
	@RequestMapping(value = "/saveEditTemplate", method = RequestMethod.POST)
	public @ResponseBody
	Integer saveEditTemplates(
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "quoteTemplateHeaderId", required = false) Integer joQuoteTemplateHeaderId,
			@RequestParam(value = "quoteTemplateTypeDetailName", required = false) Integer cuMasterTypeId,
			@RequestParam(value = "quoteTemplateTotalPrice", required = false) BigDecimal oldonequoteAmount,
			@RequestParam(value = "quoteTotalPrice_template", required = false) BigDecimal quoteAmount,
			@RequestParam(value = "costTotalPrice", required = false) BigDecimal costTotalPrice,
			@RequestParam(value = "templateDescription", required = false) String theTemplateName,
			@RequestParam(value = "quoteTemplateRemarksName", required = false) String remarks,
			@RequestParam(value = "quotechkTotalPrice_template", required = false) Boolean quotechkTotalPrice_template,
			@RequestParam(value = "oper", required = false) String oper)
			throws IOException, MessagingException {
		Boolean saved = true;
		Integer joquoteHeaderId = -1;
		logger.info("QuoteTempHeaderID: "+joQuoteTemplateHeaderId+"\n Oper: "+oper+"\nRemarks: "+remarks);
		
		try {
			JoQuotetemplateHeader ajoHeader = new JoQuotetemplateHeader();
			byte printtotal=0;
			if(quotechkTotalPrice_template){
				printtotal=1;
			}
			ajoHeader.setTemplateName(theTemplateName);
			ajoHeader.setCuMasterTypeId(cuMasterTypeId);
			ajoHeader.setRemarks(remarks);
			ajoHeader.setQuoteAmount(quoteAmount);
			ajoHeader.setCostAmount(costTotalPrice);
			System.out.println("costTotalPrice=="+costTotalPrice);
			ajoHeader.setPrintTotal(printtotal);
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			Integer aUserId = aUserBean.getUserId();
			if (oper.equalsIgnoreCase("add")) {
				ajoHeader.setGlobalTemplate(true);
				joquoteHeaderId = quoteTemplateService
						.addQuoteTemplate(ajoHeader);
				saved = true;
			}
			if (oper.equalsIgnoreCase("edit")) {
				ajoHeader.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderId);
				System.out.println("joQuoteTemplateHeaderId"+joQuoteTemplateHeaderId);
				saved = quoteTemplateService.editQuoteTemplate(ajoHeader);
				if(saved){
					joquoteHeaderId =joQuoteTemplateHeaderId;
				}

			}
			if (oper.equalsIgnoreCase("delete")) {
				ajoHeader.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderId);
				saved = quoteTemplateService.deleteQuoteTemplate(ajoHeader);
				if(saved){
					joquoteHeaderId =joQuoteTemplateHeaderId;
				}
			}
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateHeaderId:</b>"+joQuoteTemplateHeaderId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		
		return joquoteHeaderId;
	}

	@RequestMapping(value = "/insertQuotefromTemplate", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteHeader insertQuoteFromTemplate(
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "joQuoteTemplateHeaderId", required = true) Integer joQuoteTemplateHeaderId,
			@RequestParam(value = "jobNumber", required = true) String jobNumber,
			@RequestParam(value = "joMasterID", required = true) Integer thejoMaster)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		JoQuoteHeader aJoQuotetemplateHeader = new JoQuoteHeader();
		Boolean saved = false;
		try {
			aJoQuotetemplateHeader = quoteTemplateService.insertQuoteTemplates(thejoMaster,joQuoteTemplateHeaderId);
			saved = true;
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateHeaderId,jobNumber:</b>"+joQuoteTemplateHeaderId+","+jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aJoQuotetemplateHeader;
	}

	/** The method to get the users Quote template properties 
	 * @throws MessagingException */
	@RequestMapping(value = "/getQuotePropertyIdOfUser", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteProperties getUserQuotePropertiesID(
			@RequestParam(value = "joQuotePropertyID", required = false) Integer theJoQuotePropertyID,
			HttpServletResponse theResponse, HttpSession session,HttpServletRequest therequest)
			throws IOException, MessagingException {
		JoQuoteProperties aPropertyID = null;
		try {
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			Integer aUserId = aUserBean.getUserId();
			aPropertyID = (JoQuoteProperties) jobService
					.getuserquoteProperty(aUserId);
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuotePropertyID:</b>"+theJoQuotePropertyID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aPropertyID;
	}

	/** Method to Get the User Quote Template Properties 
	 * @throws MessagingException */
	@RequestMapping(value = "/getquoteTempproperties", method = RequestMethod.GET)
	public @ResponseBody
	JoQuoteTemplateProperties getQuoteTempProperties(
			HttpServletRequest therequest,
			HttpServletResponse theResponse, HttpSession session)
			throws IOException, MessagingException {
		JoQuoteTemplateProperties aProperty = null;
		try {
			UserBean aUserBean;
			aUserBean = (UserBean) session
					.getAttribute(SessionConstants.USER);
			Integer aUserId = aUserBean.getUserId();
			aProperty = jobService.getuserquoteTemplateProperty(aUserId);
		} catch (JobException e) {
			sendTransactionException("<b>MethodName:</b>getquoteTempproperties","JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aProperty;
	}

	public List<CuMasterType> getItsCuMasterType() {
		return itsCuMasterType;
	}

	public void setItsCuMasterType(List<CuMasterType> itsCuMasterType) {
		this.itsCuMasterType = itsCuMasterType;
	}

	public TsUserLogin getItsUserDetails() {
		return itsUserDetails;
	}

	public void setItsUserDetails(TsUserLogin itsUserDetails) {
		this.itsUserDetails = itsUserDetails;
	}

	/*
	 * 
	 * Created by: Praveen kumar Created at:08/27/2014 Purpose:update position
	 * for new line items
	 */
	public JoQuoteDetail updateItemPosition(Integer theQuoteDetailID,
			Integer theQuoteHeaderID) throws IOException {
		JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
		try {
			Integer quotePosition = jobService
					.getQuotePositionMaxValue(theQuoteHeaderID);
			quotePosition = quotePosition + 1;
			logger.info("New Quote Position: " + quotePosition);
			aJoQuoteDetail.setJoQuoteDetailId(theQuoteDetailID);
			aJoQuoteDetail.setPosition(quotePosition);
			aJoQuoteDetail = jobService.updateItemPosition(aJoQuoteDetail);
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			// theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetail;
	}
	
	@RequestMapping(value = "/getjoquotecolumndetails", method = RequestMethod.GET)
	public @ResponseBody
	Joquotecolumn getjoquotecolumndetails(HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, UserException, MessagingException {
		Joquotecolumn joquotecolumn=new Joquotecolumn();
		try {
			 joquotecolumn=sysservice.getjoQuoteColumn();
		} catch (UserException e) {
			sendTransactionException("<b>MethodName:</b>getjoquotecolumndetails","JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return joquotecolumn;
	}
	
	@RequestMapping(value = "/LineItemProperties", method = RequestMethod.GET)
	public @ResponseBody
	Integer getFactoryID(
			@RequestParam(value = "JoQLineItemsPropId") Integer JoQLineItemsPropId,
			@RequestParam(value = "LineItemProperties") Integer LineItemProperties,
			@RequestParam(value = "Italic_ItemID") Boolean Italic_ItemID,
			@RequestParam(value = "UnderLine_ItemID") Boolean UnderLine_ItemID,
			@RequestParam(value = "Bold_ItemID") Boolean Bold_ItemID,
			@RequestParam(value = "Italic_QuantityID") Boolean Italic_QuantityID,
			@RequestParam(value = "UnderLine_QuantityID") Boolean UnderLine_QuantityID,
			@RequestParam(value = "Bold_QuantityID") Boolean Bold_QuantityID,
			@RequestParam(value = "Italic_ParagraphID") Boolean Italic_ParagraphID,
			@RequestParam(value = "UnderLine_ParagraphID") Boolean UnderLine_ParagraphID,
			@RequestParam(value = "Bold_ParagraphID") Boolean Bold_ParagraphID,
			@RequestParam(value = "Italic_ManufacturerID") Boolean Italic_ManufacturerID,
			@RequestParam(value = "UnderLine_ManufacturerID") Boolean UnderLine_ManufacturerID,
			@RequestParam(value = "Bold_ManufacturerID") Boolean Bold_ManufacturerID,
			@RequestParam(value = "Italic_SpecID") Boolean Italic_SpecID,
			@RequestParam(value = "UnderLine_SpecID") Boolean UnderLine_SpecID,
			@RequestParam(value = "Bold_SpecID") Boolean Bold_SpecID,
			@RequestParam(value = "Italic_MultID") Boolean Italic_MultID,
			@RequestParam(value = "UnderLine_MultID") Boolean UnderLine_MultID,
			@RequestParam(value = "Bold_MultID") Boolean Bold_MultID,
			@RequestParam(value = "Italic_PriceID") Boolean Italic_PriceID,
			@RequestParam(value = "UnderLine_PriceID") Boolean UnderLine_PriceID,
			@RequestParam(value = "Bold_PriceID") Boolean Bold_PriceID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
//		JoQLineItemsPropId=0&LineItemProperties=2&Italic_ItemID=true&Bold_ItemID=false&Italic_QuantityID=false&
//		UnderLine_QuantityID=false&Bold_QuantityID=false&Italic_ParagraphID=false&UnderLine_ParagraphID=false&
//		Bold_ParagraphID=false&Italic_ManufacturerID=false&UnderLine_ManufacturerID=false&Bold_ManufacturerID=false
//		&Italic_SpecID=false&UnderLine_SpecID=false&Bold_SpecID=false&Italic_MultID=false&UnderLine_MultID=false&
//		Bold_MultID=false&Italic_PriceID=false&UnderLine_PriceID=false&Bold_PriceID=false
		
		Integer joQLineItemsPropId = 0;
		try {
			System.out.println("insi the java method");
			joQLineItemsProp ajoQLineItemsProp=new joQLineItemsProp();
			
			ajoQLineItemsProp.setJoQLineItemsPropId(JoQLineItemsPropId);
			ajoQLineItemsProp.setJoQuoteDetailId(LineItemProperties);
			
			ajoQLineItemsProp.setItalicItem(Italic_ItemID);
			ajoQLineItemsProp.setItalicManufacturer(Italic_ManufacturerID);
			ajoQLineItemsProp.setItalicMult(Italic_MultID);
			ajoQLineItemsProp.setItalicParagraph(Italic_ParagraphID);
			ajoQLineItemsProp.setItalicPrice(Italic_PriceID);
			ajoQLineItemsProp.setItalicQuantity(Italic_QuantityID);
			ajoQLineItemsProp.setItalicSpec(Italic_SpecID);
			
			ajoQLineItemsProp.setUnderlineItem(UnderLine_ItemID);
			ajoQLineItemsProp.setUnderlineManufactur(UnderLine_ManufacturerID);
			ajoQLineItemsProp.setUnderlineMult(UnderLine_MultID);
			ajoQLineItemsProp.setUnderlineParagraph(UnderLine_ParagraphID);
			ajoQLineItemsProp.setUnderlinePrice(UnderLine_PriceID);
			ajoQLineItemsProp.setUnderlineQuantity(UnderLine_QuantityID);
			ajoQLineItemsProp.setUnderlineSpec(UnderLine_SpecID);
			
			/* Italic_ItemID UnderLine_ItemID  Bold_ItemID Italic_QuantityID UnderLine_QuantityID Bold_QuantityID
			Italic_ParagraphID UnderLine_ParagraphID Bold_ParagraphID
			
			Italic_ManufacturerID UnderLine_ManufacturerID Bold_ManufacturerID
			Italic_SpecID UnderLine_SpecID Bold_SpecID
			Italic_MultID UnderLine_MultID Bold_MultID 
			Italic_PriceID UnderLine_PriceID Bold_PriceID */
			ajoQLineItemsProp.setBoldItem(Bold_ItemID);
			ajoQLineItemsProp.setBoldManufacturer(Bold_ManufacturerID);
			ajoQLineItemsProp.setBoldMult(Bold_MultID);
			ajoQLineItemsProp.setBoldParagraph(Bold_ParagraphID);
			ajoQLineItemsProp.setBoldPrice(Bold_PriceID);
			ajoQLineItemsProp.setBoldQuantity(Bold_QuantityID);
			ajoQLineItemsProp.setBoldSpec(Bold_SpecID);
			 joQLineItemsPropId = jobService.createJoQuoteLineItemsProp(ajoQLineItemsProp);
		} catch (Exception e) {
			sendTransactionException("<b>JoQLineItemsPropId:</b>"+JoQLineItemsPropId,"JOB",e,session,therequest);
			logger.error(e.getMessage());
		}
		return joQLineItemsPropId;
	}
	@RequestMapping(value = "/getjoQLineItemsProp", method = RequestMethod.GET)
	public @ResponseBody
	joQLineItemsProp getjoQLineItemsProp(
			@RequestParam(value = "joQuoteDetailID") Integer joQuoteDetailID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, UserException, MessagingException {
		joQLineItemsProp ajoQLineItemsProp=new joQLineItemsProp();
		try {
			ajoQLineItemsProp=jobService.getjoQLineItemsProp(joQuoteDetailID);
		} catch (Exception e) {
			sendTransactionException("<b>joQuoteDetailID:</b>"+joQuoteDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
		}
		return ajoQLineItemsProp;
	}
	
	@RequestMapping(value = "/LineItemTemplateProperties", method = RequestMethod.GET)
	public @ResponseBody Integer LineItemTemplateProperties(
			@RequestParam(value = "JoQLineItemsPropId") Integer JoQLineItemsPropId,
			@RequestParam(value = "LineItemProperties") Integer LineItemProperties,
			@RequestParam(value = "Italic_ItemID") Boolean Italic_ItemID,
			@RequestParam(value = "UnderLine_ItemID") Boolean UnderLine_ItemID,
			@RequestParam(value = "Bold_ItemID") Boolean Bold_ItemID,
			@RequestParam(value = "Italic_QuantityID") Boolean Italic_QuantityID,
			@RequestParam(value = "UnderLine_QuantityID") Boolean UnderLine_QuantityID,
			@RequestParam(value = "Bold_QuantityID") Boolean Bold_QuantityID,
			@RequestParam(value = "Italic_ParagraphID") Boolean Italic_ParagraphID,
			@RequestParam(value = "UnderLine_ParagraphID") Boolean UnderLine_ParagraphID,
			@RequestParam(value = "Bold_ParagraphID") Boolean Bold_ParagraphID,
			@RequestParam(value = "Italic_ManufacturerID") Boolean Italic_ManufacturerID,
			@RequestParam(value = "UnderLine_ManufacturerID") Boolean UnderLine_ManufacturerID,
			@RequestParam(value = "Bold_ManufacturerID") Boolean Bold_ManufacturerID,
			@RequestParam(value = "Italic_SpecID") Boolean Italic_SpecID,
			@RequestParam(value = "UnderLine_SpecID") Boolean UnderLine_SpecID,
			@RequestParam(value = "Bold_SpecID") Boolean Bold_SpecID,
			@RequestParam(value = "Italic_MultID") Boolean Italic_MultID,
			@RequestParam(value = "UnderLine_MultID") Boolean UnderLine_MultID,
			@RequestParam(value = "Bold_MultID") Boolean Bold_MultID,
			@RequestParam(value = "Italic_PriceID") Boolean Italic_PriceID,
			@RequestParam(value = "UnderLine_PriceID") Boolean UnderLine_PriceID,
			@RequestParam(value = "Bold_PriceID") Boolean Bold_PriceID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
//		JoQLineItemsPropId=0&LineItemProperties=2&Italic_ItemID=true&Bold_ItemID=false&Italic_QuantityID=false&
//		UnderLine_QuantityID=false&Bold_QuantityID=false&Italic_ParagraphID=false&UnderLine_ParagraphID=false&
//		Bold_ParagraphID=false&Italic_ManufacturerID=false&UnderLine_ManufacturerID=false&Bold_ManufacturerID=false
//		&Italic_SpecID=false&UnderLine_SpecID=false&Bold_SpecID=false&Italic_MultID=false&UnderLine_MultID=false&
//		Bold_MultID=false&Italic_PriceID=false&UnderLine_PriceID=false&Bold_PriceID=false
		
		Integer joQLineItemsPropId = 0;
		try {
			joQLineItemTemplateProp ajoQLineItemsProp=new joQLineItemTemplateProp();
			
			ajoQLineItemsProp.setJoQLineItemsTempPropId(JoQLineItemsPropId);
			ajoQLineItemsProp.setJoQuoteTemplateDetailId(LineItemProperties);
			
			ajoQLineItemsProp.setItalicItem(Italic_ItemID);
			ajoQLineItemsProp.setItalicManufacturer(Italic_ManufacturerID);
			ajoQLineItemsProp.setItalicMult(Italic_MultID);
			ajoQLineItemsProp.setItalicParagraph(Italic_ParagraphID);
			ajoQLineItemsProp.setItalicPrice(Italic_PriceID);
			ajoQLineItemsProp.setItalicQuantity(Italic_QuantityID);
			ajoQLineItemsProp.setItalicSpec(Italic_SpecID);
			
			ajoQLineItemsProp.setUnderlineItem(UnderLine_ItemID);
			ajoQLineItemsProp.setUnderlineManufactur(UnderLine_ManufacturerID);
			ajoQLineItemsProp.setUnderlineMult(UnderLine_MultID);
			ajoQLineItemsProp.setUnderlineParagraph(UnderLine_ParagraphID);
			ajoQLineItemsProp.setUnderlinePrice(UnderLine_PriceID);
			ajoQLineItemsProp.setUnderlineQuantity(UnderLine_QuantityID);
			ajoQLineItemsProp.setUnderlineSpec(UnderLine_SpecID);
			
			/* Italic_ItemID UnderLine_ItemID  Bold_ItemID Italic_QuantityID UnderLine_QuantityID Bold_QuantityID
			Italic_ParagraphID UnderLine_ParagraphID Bold_ParagraphID
			
			Italic_ManufacturerID UnderLine_ManufacturerID Bold_ManufacturerID
			Italic_SpecID UnderLine_SpecID Bold_SpecID
			Italic_MultID UnderLine_MultID Bold_MultID 
			Italic_PriceID UnderLine_PriceID Bold_PriceID */
			ajoQLineItemsProp.setBoldItem(Bold_ItemID);
			ajoQLineItemsProp.setBoldManufacturer(Bold_ManufacturerID);
			ajoQLineItemsProp.setBoldMult(Bold_MultID);
			ajoQLineItemsProp.setBoldParagraph(Bold_ParagraphID);
			ajoQLineItemsProp.setBoldPrice(Bold_PriceID);
			ajoQLineItemsProp.setBoldQuantity(Bold_QuantityID);
			ajoQLineItemsProp.setBoldSpec(Bold_SpecID);
			 joQLineItemsPropId = jobService.createJoQuoteLineItemsTempProp(ajoQLineItemsProp);
		} catch (Exception e) {
			sendTransactionException("<b>JoQLineItemsPropId:</b>"+JoQLineItemsPropId,"JOB",e,session,therequest);
			logger.error(e.getMessage());
		}
		return joQLineItemsPropId;
	}
	
	@RequestMapping(value = "/getjoQLineItemsTempProp", method = RequestMethod.GET)
	public @ResponseBody
	joQLineItemTemplateProp getjoQLineItemsTempProp(
			@RequestParam(value = "joQuoteDetailID") Integer joQuoteDetailID,
			HttpServletResponse theResponse,HttpServletRequest therequest,HttpSession session) throws IOException, UserException, MessagingException {
		joQLineItemTemplateProp ajoQLineItemsProp=new joQLineItemTemplateProp();
		try {
			ajoQLineItemsProp=jobService.getjoQLineItemsTempProp(joQuoteDetailID);
		} catch (Exception e) {
			sendTransactionException("<b>joQuoteDetailID:</b>"+joQuoteDetailID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
		}
		return ajoQLineItemsProp;
	}
	
	@RequestMapping(value="/GettoemailaddContactDetails",method = RequestMethod.GET)
	public @ResponseBody Rxcontact getContactDetails(@RequestParam(value = "ContactId", required = false) Integer theContactID,
			HttpServletRequest therequest,HttpSession session,HttpServletResponse response) throws IOException, VendorException, MessagingException {
		Rxcontact aRxcontact = new Rxcontact();
		try{
			aRxcontact = jobService.getContactDetails(theContactID);
		}catch (JobException e) {
			sendTransactionException("<b>theContactID:</b>"+theContactID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxcontact;
	}
	
	
//	@RequestMapping(value = "/addquoteLineItems", method = RequestMethod.POST)
//	public @ResponseBody Integer addquoteLineItems(
//			@RequestParam(value = "lineitemtypeid") Integer lineitemtypeid,
//			@RequestParam(value = "Item2_TextBox") String Item2_TextBox,
//			@RequestParam(value = "Price_TextBox1") String Price_TextBox1,
//			@RequestParam(value = "Price_TextBox2") String Price_TextBox2,
//			@RequestParam(value = "Item3_TextBox1") String Item3_TextBox1,
//			@RequestParam(value = "Item3_TextBox2") String Item3_TextBox2,
//			@RequestParam(value = "Cost_TextBox") String Cost_TextBox,
//			@RequestParam(value = "manufacturertextboxid") String manufacturertextboxid,
//			@RequestParam(value = "joQuoteDetailMstrID") Integer joQuoteDetailMstrID,
//			@RequestParam(value = "texteditor") String texteditorfromjsp,
//			@RequestParam(value = "position") Integer position,
//			@RequestParam(value = "joQuoteHeaderID") Integer joQuoteHeaderID,
//			@RequestParam(value = "quoteCategorySelect") Integer quoteCategorySelect,
//			
//			@RequestParam(value = "quoteTypeDetail") String quoteTypeDetail,
//			@RequestParam(value = "jobQuoteSubmittedBYID") String jobQuoteSubmittedBYID,
//			@RequestParam(value = "jobQuoteInternalNote") String jobQuoteInternalNote,
//			@RequestParam(value = "quoteTotalPrice") String quoteTotalPrice,
//			@RequestParam(value = "joMasterID") Integer joMasterID,
//			@RequestParam(value = "jobQuoteSubmittedBYInitials") String jobQuoteSubmittedBYInitials,
//			@RequestParam(value = "jobQuoteRevision") String jobQuoteRevision,
//			@RequestParam(value = "quotecosttotalamount") String quotecosttotalamount,
//			@RequestParam(value = "quotechkTotalPrice") Boolean quotechkTotalPrice,
//			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
//		try {
//			Byte TotalCheck=0;
//			if(quotechkTotalPrice==true){
//				TotalCheck=1;
//			}
//			JoQuoteHeader joqh=new JoQuoteHeader();
//			Integer cumastertypeid=ConvertintoInteger(quoteTypeDetail);
//			joqh.setCuMasterTypeID(cumastertypeid);
//			joqh.setJoMasterID(joMasterID);
//			joqh.setQuoteRev(jobQuoteRevision);
//			joqh.setCreatedById(ConvertintoInteger(jobQuoteSubmittedBYID));
//			joqh.setQuoteAmount(ConvertintoBigDecimal(quoteTotalPrice));
//			joqh.setInternalNote(jobQuoteInternalNote);
//			joqh.setCreatedByName(jobQuoteSubmittedBYInitials);
//			joqh.setCostAmount(ConvertintoBigDecimal(quotecosttotalamount));
//			joqh.setPrintTotal(TotalCheck);
//			System.out.println("Print total=="+TotalCheck);
//			if(joQuoteHeaderID==0){
//				joQuoteHeaderID=jobService.addjoquoteheader(joqh);
//			}else{
//				joqh.setJoQuoteHeaderId(joQuoteHeaderID);
//				jobService.updatejoquoteheader(joqh);
//			}
//			
//			joQuoteDetailMstr ajoQuoteDetailMstr=new joQuoteDetailMstr();
//			String texteditor=texteditorfromjsp.replaceAll("size: xx-small;", "size: 7pt");
//			texteditor=texteditor.replaceAll("size: x-small","size: 7.5pt");
//			texteditor=texteditor.replaceAll("size: xx-large","size: 7.5pt");
//			texteditor=texteditor.replaceAll("size: x-large","size: 7.5pt");
//			texteditor=texteditor.replaceAll("size: smaller","size: 10pt");
//			texteditor=texteditor.replaceAll("size: small", "size: 10pt");
//			texteditor=texteditor.replaceAll("size: medium", "size: 12pt");
//			texteditor=texteditor.replaceAll("size: larger", "size: 14pt");
//			texteditor=texteditor.replaceAll("size: large", "size: 13.5pt");
//			System.out.println("texteditor=="+texteditor);
//			if(lineitemtypeid==1){
//				ajoQuoteDetailMstr.setType(lineitemtypeid);
//				ajoQuoteDetailMstr.setTexteditor(texteditor);
//			}else if(lineitemtypeid==2){
//				ajoQuoteDetailMstr.setType(lineitemtypeid);
//				ajoQuoteDetailMstr.setQuantity(Item2_TextBox);
//				ajoQuoteDetailMstr.setCost(ConvertintoBigDecimal(Cost_TextBox));
//				ajoQuoteDetailMstr.setManufacturer(ConvertintoInteger(manufacturertextboxid));
//				ajoQuoteDetailMstr.setCategory(quoteCategorySelect);
//				ajoQuoteDetailMstr.setTexteditor(texteditor);
//			}else if(lineitemtypeid==3){
//				ajoQuoteDetailMstr.setType(lineitemtypeid);
//				ajoQuoteDetailMstr.setQuantity(Item3_TextBox1);
//				ajoQuoteDetailMstr.setCost(ConvertintoBigDecimal(Cost_TextBox));
//				ajoQuoteDetailMstr.setManufacturer(ConvertintoInteger(manufacturertextboxid));
//				ajoQuoteDetailMstr.setCategory(quoteCategorySelect);
//				ajoQuoteDetailMstr.setSellprice(ConvertintoBigDecimal(Item3_TextBox2));
//				ajoQuoteDetailMstr.setTexteditor(texteditor);
//			}else if(lineitemtypeid==4){
//				ajoQuoteDetailMstr.setType(lineitemtypeid);
//				ajoQuoteDetailMstr.setTextbox(Price_TextBox1);
//				ajoQuoteDetailMstr.setSellprice(ConvertintoBigDecimal(Price_TextBox2));
//			}
//			ajoQuoteDetailMstr.setJoQuoteHeaderID(joQuoteHeaderID);
//			if(joQuoteDetailMstrID!=0){
//				//Update
//				ajoQuoteDetailMstr.setJoQuoteDetailMstrID(joQuoteDetailMstrID);
//				boolean returnvalue=jobService.UpdatenewQuotes(ajoQuoteDetailMstr);
//			}else{
//				boolean returnvalue=jobService.addjoQuoteDetailMstr(ajoQuoteDetailMstr);
//			}
//		} catch (Exception e) {
//			sendTransactionException("<b>joQuoteDetailMstrID,joMasterID:</b>"+joQuoteDetailMstrID+","+joMasterID,"JOB",e,session,therequest);
//			logger.error(e.getMessage());
//		}
//		return joQuoteHeaderID;
//	}
	
	@RequestMapping(value = "/loadquoteLineItems", method = RequestMethod.POST)
	public @ResponseBody CustomResponse loadquoteLineItems(
			/*@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,*/
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "joquoteheaderid", required = true) Integer joquoteheaderid)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			/*Integer aFrom, aTo = null;
			theRows=20;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			
			if(aFrom<0){
				aFrom=0;	
			}
			if(aTo<0){
				aTo=20;	
			}*/
			
			
			
			System.out.println("insid the loadquotelineitems");
			List<joQuoteDetailMstr> ajoQuoteDetailMstr =jobService.getjoQuoteDetailMstr(joquoteheaderid);
			
			/*If need pagination should uncomment this line*If need pagination should comment this line
			 * Commented By:Velmurugan
			 * BID 491 
			 * 
			 * if(aTo>ajoQuoteDetailMstr.size()){
				aTo=ajoQuoteDetailMstr.size();
			}
			if(aTo==0){
				aTo=0;
				aFrom=0;
				thePage=0;
			}
			List<joQuoteDetailMstr> lmtdlist=new ArrayList<joQuoteDetailMstr>();
			
			if((int) Math.ceil((double)ajoQuoteDetailMstr.size()/ (double) theRows)<thePage){
				thePage=(int) Math.ceil((double)ajoQuoteDetailMstr.size()/ (double) theRows);
				aTo = (theRows * thePage);
				aFrom = aTo - theRows;
				lmtdlist=ajoQuoteDetailMstr.subList(aFrom, aTo);
			}else{
				lmtdlist=ajoQuoteDetailMstr.subList(aFrom, aTo);
			}*/
			aResponse.setRows(ajoQuoteDetailMstr);
			/*If need pagination should uncomment this line*If need pagination should comment this line
			 * Commented By:Velmurugan
			 * BID 491 
			 * 
			  aResponse.setRecords(String.valueOf(ajoQuoteDetailMstr.size()));
			  aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)ajoQuoteDetailMstr.size()/ (double) theRows));*/
		} catch (JobException e) {
			
			sendTransactionException("<b>MethodName:</b>loadquoteLineItems","JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aResponse;
	}
	
	
	public Integer ConvertintoInteger(String Stringvalue){
		Integer returnvalue=0;
		try {
			returnvalue=Integer.parseInt(Stringvalue);
		} catch (NumberFormatException e) {
			return returnvalue;
		}
		
		return returnvalue;
	}
	public BigDecimal ConvertintoBigDecimal(String Stringvalue){
		BigDecimal returnvalue=new BigDecimal(0);
		try {
			logger.info("Stringvalue"+Stringvalue);
			System.out.println("Stringvalue"+Stringvalue);
			returnvalue=new BigDecimal(Stringvalue);
		} catch (Exception e) {
			return returnvalue;
		}
		
		return returnvalue;
	}
	
	@RequestMapping(value = "/updatenewQuoteDetailsPosition", method = RequestMethod.POST)
	public @ResponseBody
	joQuoteDetailMstr updatenewQuoteDetailsPosition(
			@RequestParam(value = "selectedQuoteDetailID", required = false) Integer theQuoteDetailId,
			@RequestParam(value = "selectedJoQuoteHeaderID", required = false) Integer theJoQuoteHeaderId,
			@RequestParam(value = "operate", required = false) String operation,
			@RequestParam(value = "difference", required = false) Integer difference,
			@RequestParam(value = "endQuoteDetailID", required = false) Integer endQuoteDetailID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		joQuoteDetailMstr ajoQuoteDetailMstr = new joQuoteDetailMstr();
		try {
			ajoQuoteDetailMstr.setJoQuoteDetailMstrID(theQuoteDetailId);
			ajoQuoteDetailMstr.setJoQuoteHeaderID(theJoQuoteHeaderId);
			ajoQuoteDetailMstr = jobService.updatenewInlineItemPosition(ajoQuoteDetailMstr, operation, difference, endQuoteDetailID);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteDetailId,theJoQuoteHeaderId:</b>"+theQuoteDetailId+","+theJoQuoteHeaderId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return ajoQuoteDetailMstr;
	}
	
	@RequestMapping(value = "/addquoteHeader", method = RequestMethod.GET)
	public @ResponseBody Boolean addquoteHeader(
			@RequestParam(value = "joQuoteHeaderID") Integer joQuoteHeaderID,
			@RequestParam(value = "quoteTypeDetail") String quoteTypeDetail,
			@RequestParam(value = "jobQuoteSubmittedBYID") String jobQuoteSubmittedBYID,
			@RequestParam(value = "jobQuoteInternalNote") String jobQuoteInternalNote,
			@RequestParam(value = "quoteTotalPrice") String quoteTotalPrice,
			@RequestParam(value = "joMasterID") Integer joMasterID,
			@RequestParam(value = "jobQuoteSubmittedBYInitials") String jobQuoteSubmittedBYInitials,
			@RequestParam(value = "jobQuoteRevision") String jobQuoteRevision,
			@RequestParam(value = "quotecostamount") String quotecostamount,
			@RequestParam(value = "quotechkTotalPrice") Boolean quotechkTotalPrice,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		try {
				JoQuoteHeader joqh=new JoQuoteHeader();
				Integer cumastertypeid=ConvertintoInteger(quoteTypeDetail);
				joqh.setCuMasterTypeID(cumastertypeid);
				joqh.setJoMasterID(joMasterID);
				joqh.setQuoteRev(jobQuoteRevision);
				joqh.setCreatedById(ConvertintoInteger(jobQuoteSubmittedBYID));
				joqh.setQuoteAmount(ConvertintoBigDecimal(quoteTotalPrice));
				joqh.setInternalNote(jobQuoteInternalNote);
				joqh.setCreatedByName(jobQuoteSubmittedBYInitials);
				joqh.setJoQuoteHeaderId(joQuoteHeaderID);
				joqh.setCostAmount(ConvertintoBigDecimal(quotecostamount));
				byte totalprint=0;
				if(quotechkTotalPrice){
					totalprint=1;
				}
				System.out.println("totalprint===="+totalprint);
				joqh.setPrintTotal(totalprint);
				jobService.updatejoquoteheader(joqh);
			
			
		} catch (Exception e) {
			sendTransactionException("<b>joQuoteHeaderID:</b>"+joQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
		}
		return true;
	}
	
	@RequestMapping(value = "/SavenewQuoteDetailInfo", method = RequestMethod.POST)
	public @ResponseBody Integer SavenewQuoteDetailInfo(
			@RequestParam(value = "quoteTypeDetailName", required = false) Integer quoteTypeDetailID,
			@RequestParam(value = "joHeaderID", required = false) Integer joQuoteHeaderID,
			@RequestParam(value = "joHeaderQuoteID", required = false) Integer joHeaderID,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "jobQuoteRevision", required = false) String jobQuoteRevision,
			@RequestParam(value = "jobQuoteSubmittedBYFullName", required = false) String jobQuoteSubmittedBYFullName,
			@RequestParam(value = "jobQuoteSubmittedBYInitials", required = false) String createdByName,
			@RequestParam(value = "jobQuoteSubmittedBYID", required = false) Integer jobQuoteSubmittedBYID,
			@RequestParam(value = "jobQuoteInternalNote", required = false) String jobQuoteInternalNote,
			@RequestParam(value = "totalcost", required = false) BigDecimal totalcost,
			@RequestParam(value = "totalPrice", required = false) BigDecimal totalPrice,
			@RequestParam(value = "rxContactID", required = false) Integer rxContactID,
			@RequestParam(value = "bidderId", required = false) Integer bidderId,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "token", required = false) String token,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		try {
			if (jobQuoteSubmittedBYID == null) {
				response.sendError(500, "'Submitted By' Empty");
				return null;
			}
			JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
			if (joQuoteHeaderID != null) {
				aJoQuoteHeader.setJoQuoteHeaderId(joQuoteHeaderID);
			} else if (joHeaderID != null) {
				aJoQuoteHeader.setJoQuoteHeaderId(joHeaderID);
			}
			if (jobQuoteRevision != null && jobQuoteRevision.contains(",")) {
				String[] jobQuoteRevisions = jobQuoteRevision.split(",");
				if (jobQuoteRevisions.length > 0) {
					jobQuoteRevision = jobQuoteRevisions[0];
				} else {
					jobQuoteRevision = "";
				}

			}
			aJoQuoteHeader.setQuoteRev(jobQuoteRevision);
			aJoQuoteHeader.setCreatedById(jobQuoteSubmittedBYID);
			aJoQuoteHeader.setCreatedByName(createdByName);
			aJoQuoteHeader.setDateCreated(new Date());
			aJoQuoteHeader.setQuoteAmount(totalPrice);
			aJoQuoteHeader.setCostAmount(totalcost);
			aJoQuoteHeader.setInternalNote(jobQuoteInternalNote);

			aJoQuoteHeader.setCuMasterTypeID(quoteTypeDetailID);
			aJoQuoteHeader.setJoMasterID(joMasterID);
			aJoQuoteHeader.setRxContactID(rxContactID);
			Integer joQuoteheaderID;
			joQuoteheaderID = jobService.addjoquoteheader(aJoQuoteHeader);

			return joQuoteheaderID;
		} catch (JobException e) {
			sendTransactionException("<b>joQuoteHeaderID:</b>"+joQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return null;
		}
	}
//	@RequestMapping(value = "/getnewLineItemsForCopyQuote", method = RequestMethod.POST)
//	public @ResponseBody boolean getnewLineItemsForCopyQuote(
//			@RequestParam(value = "previousJoQuoteHeader", required = false) Integer thePreviousJoQuoteHeader,
//			@RequestParam(value = "currentJoQuoteheaderID", required = false) Integer theCurrentJoQuoteheaderID,
//			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
//			throws ParseException, IOException, MessagingException {
//		ArrayList<joQuoteDetailMstr> ajoQuoteDetailMstrList = null;
//		try {
//			ajoQuoteDetailMstrList = (ArrayList<joQuoteDetailMstr>) jobService.getjoQuoteDetailMstr(thePreviousJoQuoteHeader);
//			for (joQuoteDetailMstr thejoQuoteDetailMstrList:ajoQuoteDetailMstrList) {
//				thejoQuoteDetailMstrList.setJoQuoteHeaderID(theCurrentJoQuoteheaderID);
//				jobService.addjoQuoteDetailMstr(thejoQuoteDetailMstrList);
//			}
//		} catch (JobException e) {
//			sendTransactionException("<b>thePreviousJoQuoteHeader,theCurrentJoQuoteheaderID:</b>"+thePreviousJoQuoteHeader+","+theCurrentJoQuoteheaderID,"JOB",e,session,therequest);
//			logger.error(e.getMessage(), e);
//			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
//		}
//		return true;
//	}
	
	@RequestMapping(value = "/getNewQuoteTemplateHeader", method = RequestMethod.GET)
	public @ResponseBody ArrayList<JoQuotetemplateHeader> getNewQuoteTemplateHeader(
			HttpServletRequest therequest,HttpSession session, HttpServletResponse theResponse)
			throws ParseException, IOException, QuoteTemplateException, MessagingException {
		ArrayList<JoQuotetemplateHeader> aJoQuotetemplateHeader = new ArrayList<JoQuotetemplateHeader>();
		try {
			aJoQuotetemplateHeader=(ArrayList<JoQuotetemplateHeader>) quoteTemplateService.loadQuoteTemplate();
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>MethodName:</b>getNewQuoteTemplateHeader","JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuotetemplateHeader;
	}
	
	@RequestMapping(value = "/SaveNewQuotetemplate", method = RequestMethod.GET)
	public @ResponseBody Boolean SaveNewQuotetemplate(
		@RequestParam(value = "quoteHeaderId", required = false) Integer ajoQuoteHeaderID,
			@RequestParam(value = "template_name", required = false) String Template_Name,
			HttpSession theSession, HttpServletResponse theResponse)
			throws ParseException, IOException,JobException {
		Boolean value= jobService.CopyFromQuoteToQuoteTemplate(ajoQuoteHeaderID, Template_Name);
		return value;
	}
	
	@RequestMapping(value = "/copyQuoteTemplateToQuote", method = RequestMethod.GET)
	public @ResponseBody Integer copyQuoteTemplateToQuote(
		@RequestParam(value = "quoteHeaderId", required = false) Integer ajoQuoteHeaderID,
			@RequestParam(value = "quotetemplateheaderId", required = false) Integer quotetemplateheaderId,
			
			@RequestParam(value = "quoteTypeDetail") String quoteTypeDetail,
			@RequestParam(value = "jobQuoteSubmittedBYID") String jobQuoteSubmittedBYID,
			@RequestParam(value = "jobQuoteInternalNote") String jobQuoteInternalNote,
			@RequestParam(value = "quoteTotalPrice") String quoteTotalPrice,
			@RequestParam(value = "joMasterID") Integer joMasterID,
			@RequestParam(value = "jobQuoteSubmittedBYInitials") String jobQuoteSubmittedBYInitials,
			@RequestParam(value = "jobQuoteRevision") String jobQuoteRevision,
			@RequestParam(value = "jobQuoteRevision") String quotecosttotalamount,
			HttpSession theSession, HttpServletResponse theResponse)
			throws ParseException, IOException,JobException {
		
		if(ajoQuoteHeaderID==0){
			JoQuoteHeader joqh=new JoQuoteHeader();
			Integer cumastertypeid=ConvertintoInteger(quoteTypeDetail);
			joqh.setCuMasterTypeID(cumastertypeid);
			joqh.setJoMasterID(joMasterID);
			joqh.setQuoteRev(jobQuoteRevision);
			joqh.setCreatedById(ConvertintoInteger(jobQuoteSubmittedBYID));
			joqh.setQuoteAmount(ConvertintoBigDecimal(quoteTotalPrice));
			joqh.setInternalNote(jobQuoteInternalNote);
			joqh.setCreatedByName(jobQuoteSubmittedBYInitials);
			joqh.setCostAmount(ConvertintoBigDecimal(quotecosttotalamount));
			ajoQuoteHeaderID=jobService.addjoquoteheader(joqh);
		}
		jobService.copyQuoteTemplateLIToQuoteLI(ajoQuoteHeaderID, quotetemplateheaderId);
		return ajoQuoteHeaderID;
	}
	
//	@RequestMapping(value = "/DeleteQuoteLineItems", method = RequestMethod.GET)
//	public @ResponseBody Boolean DeleteQuoteLineItems(
//			HttpSession session,HttpServletRequest therequest,
//			HttpServletResponse theResponse,
//			@RequestParam(value = "joquoteDetailid", required = true) Integer joquoteDetailid,
//			@RequestParam(value = "joquoteheaderid", required = true) Integer joquoteheaderid,
//			@RequestParam(value = "position", required = true) Integer position
//			)
//			throws IOException, MessagingException {
//		Boolean returnValue=false;
//		try{
//			System.out.println("insid the loadquotelineitems");
//			returnValue=jobService.deleteJoNewQuoteDetail(joquoteDetailid,joquoteheaderid,position);
//		} catch (JobException e) {
//			sendTransactionException("<b>joquoteDetailid:</b>"+joquoteDetailid,"JOB",e,session,therequest);
//			logger.error(e.getMessage(), e);
//			theResponse.sendError(e.getItsErrorStatusCode(),
//					e.getMessage());
//		}
//		return returnValue;
//	}
	
	
	
	
	
	
	
	
	///Template Methods
	@RequestMapping(value = "/addquoteLineItems_template", method = RequestMethod.POST)
	public @ResponseBody Integer addquoteLineItems_template(
			@RequestParam(value = "lineitemtypeid_template") Integer lineitemtypeid_template,
			@RequestParam(value = "Item2_TextBox_template") String Item2_TextBox_template,
			@RequestParam(value = "Price_TextBox1_template") String Price_TextBox1_template,
			@RequestParam(value = "Price_TextBox2_template") String Price_TextBox2_template,
			@RequestParam(value = "Item3_TextBox1_template") String Item3_TextBox1_template,
			@RequestParam(value = "Item3_TextBox2_template") String Item3_TextBox2_template,
			@RequestParam(value = "Cost_TextBox_template") String Cost_TextBox_template,
			@RequestParam(value = "manufacturertextboxid_template") String manufacturertextboxid_template,
			@RequestParam(value = "joQuoteDetailMstrID_template") Integer joQuoteDetailMstrID_template,
			@RequestParam(value = "texteditor_template") String texteditorfromjsp_template,
			@RequestParam(value = "position_template") Integer position_template,
			@RequestParam(value = "quoteTemplateHeaderID") Integer quoteTemplateHeaderID,
			@RequestParam(value = "quoteCategorySelect") Integer quoteCategorySelect,
			
			@RequestParam(value = "quoteTemplateTypeDetail") String quoteTemplateTypeDetail,
			@RequestParam(value = "templateDescription") String templateDescription,
			@RequestParam(value = "quoteTotalPrice") String quoteTotalPrice,
			@RequestParam(value = "joMasterID") Integer joMasterID,
			@RequestParam(value = "quotechkTotalPrice_template") Boolean quotechkTotalPrice,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException {
		try {
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			byte totalprint=0;
			if(quotechkTotalPrice){
				totalprint=1;
			}
			JoQuotetemplateHeader joqth=new JoQuotetemplateHeader();
			Integer cumastertypeid=ConvertintoInteger(quoteTemplateTypeDetail);
			joqth.setCuMasterTypeId(cumastertypeid);
			joqth.setCreatedById(aUserBean.getUserId());
			joqth.setQuoteAmount(ConvertintoBigDecimal(quoteTotalPrice));
			joqth.setTemplateName(templateDescription);
			joqth.setPrintTotal(totalprint);
			if(quoteTemplateHeaderID==0){
				quoteTemplateHeaderID=jobService.addJoQuotetemplateHeader(joqth);
			}else{
				joqth.setJoQuoteTemplateHeaderId(quoteTemplateHeaderID);
				quoteTemplateService.editQuoteTemplate(joqth);
				//jobService.addJoQuotetemplateHeader(joqth);
			}
			
			joQuoteTempDetailMstr ajoQuoteTempDetailMstr=new joQuoteTempDetailMstr();
			String texteditor_template=texteditorfromjsp_template.replaceAll("size: xx-small;", "size: 7pt");
			texteditor_template=texteditor_template.replaceAll("size: x-small","size: 7.5pt");
			texteditor_template=texteditor_template.replaceAll("size: xx-large","size: 7.5pt");
			texteditor_template=texteditor_template.replaceAll("size: x-large","size: 7.5pt");
			texteditor_template=texteditor_template.replaceAll("size: smaller","size: 10pt");
			texteditor_template=texteditor_template.replaceAll("size: small", "size: 10pt");
			texteditor_template=texteditor_template.replaceAll("size: medium", "size: 12pt");
			texteditor_template=texteditor_template.replaceAll("size: larger", "size: 14pt");
			texteditor_template=texteditor_template.replaceAll("size: large", "size: 13.5pt");
			System.out.println("texteditor_template=="+texteditor_template);
			//http://stackoverflow.com/questions/5912528/font-size-translating-to-actual-point-size
			
			//Andale Mono=andale mono,times;Arial=Arial,sans-serif;Courier New=Courier New;Helvetica=helvetica;Symbol=Symbol;
			//Tahoma=Tahoma,sans-serif;
			//Times New Roman=Times New Roman,serif;Verdana=Verdana,sans-serif;
			if(texteditor_template!=null){
				texteditor_template=texteditor_template.replaceAll("'Times New Roman', serif;", "Times New Roman, serif;");
				texteditor_template=texteditor_template.replaceAll("'andale mono', times;", "andale mono, times;");
				texteditor_template=texteditor_template.replaceAll("'Arial', sans-serif;", "Arial,sans-serif;");
				texteditor_template=texteditor_template.replaceAll("'Courier New';", "Courier New;");
				texteditor_template=texteditor_template.replaceAll("'helvetica';", "helvetica;");
				texteditor_template=texteditor_template.replaceAll("'Symbol';", "Symbol;");
				texteditor_template=texteditor_template.replaceAll("'Tahoma', sans-serif;", "Tahoma, sans-serif;");
				texteditor_template=texteditor_template.replaceAll("'Verdana', sans-serif;", "Verdana, sans-serif;");
				texteditor_template=texteditor_template.replaceAll("<br>", "<br />");
				texteditor_template=texteditor_template.replaceAll("<div ","<p ");
				texteditor_template=texteditor_template.replaceAll("</div","</p");
			}
			if(lineitemtypeid_template==1){
				ajoQuoteTempDetailMstr.setType(lineitemtypeid_template);
				ajoQuoteTempDetailMstr.setTexteditor(texteditor_template);
			}else if(lineitemtypeid_template==2){
				ajoQuoteTempDetailMstr.setType(lineitemtypeid_template);
				ajoQuoteTempDetailMstr.setQuantity(Item2_TextBox_template);
				ajoQuoteTempDetailMstr.setCost(ConvertintoBigDecimal(Cost_TextBox_template));
				ajoQuoteTempDetailMstr.setManufacturer(ConvertintoInteger(manufacturertextboxid_template));
				ajoQuoteTempDetailMstr.setCategory(quoteCategorySelect);
				ajoQuoteTempDetailMstr.setTexteditor(texteditor_template);
			}else if(lineitemtypeid_template==3){
				ajoQuoteTempDetailMstr.setType(lineitemtypeid_template);
				ajoQuoteTempDetailMstr.setQuantity(Item3_TextBox1_template);
				ajoQuoteTempDetailMstr.setCost(ConvertintoBigDecimal(Cost_TextBox_template));
				ajoQuoteTempDetailMstr.setManufacturer(ConvertintoInteger(manufacturertextboxid_template));
				ajoQuoteTempDetailMstr.setTexteditor(texteditor_template);
				ajoQuoteTempDetailMstr.setCategory(quoteCategorySelect);
				ajoQuoteTempDetailMstr.setSellprice(ConvertintoBigDecimal(Item3_TextBox2_template));
			}else if(lineitemtypeid_template==4){
				ajoQuoteTempDetailMstr.setType(lineitemtypeid_template);
				ajoQuoteTempDetailMstr.setTextbox(Price_TextBox1_template);
				ajoQuoteTempDetailMstr.setSellprice(ConvertintoBigDecimal(Price_TextBox2_template));
			}
			ajoQuoteTempDetailMstr.setJoQuoteTemplateHeaderID(quoteTemplateHeaderID);
			if(joQuoteDetailMstrID_template!=0){
				//Update
				ajoQuoteTempDetailMstr.setJoQuoteTempDetailMstrID(joQuoteDetailMstrID_template);
				boolean returnvalue=jobService.UpdatenewQuotestemplate(ajoQuoteTempDetailMstr);
			}else{
				boolean returnvalue=jobService.addjoQuoteTempDetailMstr(ajoQuoteTempDetailMstr);
			}
		} catch (Exception e) {
			sendTransactionException("<b>quoteTemplateHeaderID,joQuoteDetailMstrID_template:</b>"+quoteTemplateHeaderID+","+joQuoteDetailMstrID_template,"JOB",e,session,therequest);
			logger.error(e.getMessage());
		}
		return quoteTemplateHeaderID;
	}
	@RequestMapping(value = "/loadquoteLineItems_template", method = RequestMethod.POST)
	public @ResponseBody CustomResponse loadquoteLineItems_template(
			/*@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,*/
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "quoteTemplateHeaderID", required = true) Integer quoteTemplateHeaderID)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			/*If need pagination should uncomment this line*If need pagination should comment this line
			*Commented By Velmurugan
			*As per Eric requirement
			*BID 491
			*
			Integer aFrom, aTo;
			theRows=20;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if(aFrom==null){
				aFrom=0;	
			}
			if(aTo==null){
				aTo=20;	
			}
			System.out.println("insid the loadquotelineitems_template");*/
			List<joQuoteTempDetailMstr> ajoQuoteTempDetailMstr =jobService.getjoQuoteTempDetailMstr(quoteTemplateHeaderID);
			
			/*If need pagination should uncomment this line*If need pagination should comment this line
			*Commented By Velmurugan
			*As per Eric requirement
			*BID 491
			*
			if(aTo>ajoQuoteTempDetailMstr.size()){
				aTo=ajoQuoteTempDetailMstr.size();
			}
			if(aTo==0){
				aTo=0;
				aFrom=0;
				thePage=0;
			}
			List<joQuoteTempDetailMstr> lmtdlist=new ArrayList<joQuoteTempDetailMstr>();
			
			if((int) Math.ceil((double)ajoQuoteTempDetailMstr.size()/ (double) theRows)<thePage){
				thePage=(int) Math.ceil((double)ajoQuoteTempDetailMstr.size()/ (double) theRows);
				aTo = (theRows * thePage);
				aFrom = aTo - theRows;
				lmtdlist=ajoQuoteTempDetailMstr.subList(aFrom, aTo);
			}else{
				lmtdlist=ajoQuoteTempDetailMstr.subList(aFrom, aTo);
			}
			aResponse.setRows(lmtdlist);
			aResponse.setRecords(String.valueOf(ajoQuoteTempDetailMstr.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)ajoQuoteTempDetailMstr.size()/ (double) theRows));*/
			aResponse.setRows(ajoQuoteTempDetailMstr);
		} catch (JobException e) {
			sendTransactionException("<b>quoteTemplateHeaderID:</b>"+quoteTemplateHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return aResponse;
	}
	@RequestMapping(value = "/DeleteQuoteLineItems_template", method = RequestMethod.GET)
	public @ResponseBody Boolean DeleteQuoteLineItems_template(
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "joquotetempDetailid", required = true) Integer joquoteDetailid,
			@RequestParam(value = "quoteTemplateHeaderID", required = true) Integer quoteTemplateHeaderID,
			@RequestParam(value = "position", required = true) Integer position
			)
			throws IOException, MessagingException {
		Boolean returnValue=false;
		try{
			System.out.println("insid the DeleteQuoteLineItems_template");
			returnValue=jobService.deleteJoNewQuoteDetail_template(joquoteDetailid,quoteTemplateHeaderID,position);
		} catch (JobException e) {
			sendTransactionException("<b>quoteTemplateHeaderID,joquoteDetailid:</b>"+quoteTemplateHeaderID+","+joquoteDetailid,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return returnValue;
	}
	@RequestMapping(value = "/updatenewQuoteDetailsPosition_template", method = RequestMethod.POST)
	public @ResponseBody
	joQuoteTempDetailMstr updatenewQuoteDetailsPosition_template(
			@RequestParam(value = "selectedQuoteDetailID", required = false) Integer theQuotetempDetailId,
			@RequestParam(value = "selectedJoQuoteHeaderID", required = false) Integer theJoQuotetempHeaderId,
			@RequestParam(value = "operate", required = false) String operation,
			@RequestParam(value = "difference", required = false) Integer difference,
			@RequestParam(value = "endQuoteDetailID", required = false) Integer endQuotetempDetailID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		joQuoteTempDetailMstr ajoQuoteTempDetailMstr = new joQuoteTempDetailMstr();
		try {
			ajoQuoteTempDetailMstr.setJoQuoteTempDetailMstrID(theQuotetempDetailId);
			ajoQuoteTempDetailMstr.setJoQuoteTemplateHeaderID(theJoQuotetempHeaderId);
			ajoQuoteTempDetailMstr = jobService.updatenewInlineItemPosition_template(ajoQuoteTempDetailMstr, operation, difference, endQuotetempDetailID);
		} catch (JobException e) {
			sendTransactionException("<b>theJoQuotetempHeaderId,theQuotetempDetailId:</b>"+theJoQuotetempHeaderId+","+theQuotetempDetailId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return ajoQuoteTempDetailMstr;
	}
	@RequestMapping(value = "/DeleteEditTemplate", method = RequestMethod.POST)
	public @ResponseBody boolean DeleteEditTemplate(
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "quoteTemplateHeaderId", required = false) Integer joQuoteTemplateHeaderId
			)
			throws IOException, MessagingException {
		Boolean saved = true;
		Integer joquoteHeaderId = -1;
		
		try {
			JoQuotetemplateHeader ajoHeader = new JoQuotetemplateHeader();
				ajoHeader.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderId);
				saved = quoteTemplateService.deleteQuoteTemplate(ajoHeader);
		} catch (QuoteTemplateException e) {
			sendTransactionException("<b>joQuoteTemplateHeaderId:</b>"+joQuoteTemplateHeaderId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		
		return true;
	}
	
	@RequestMapping(value = "/UpdateEmailQuoteHistory", method = RequestMethod.GET)
	public @ResponseBody Integer updateLastQuoteAndRev(
			@RequestParam(value = "revision", required = false) String theRevision,
			@RequestParam(value = "joBidderID", required = false) Integer theJoBidderID,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			@RequestParam(value = "quoteTypeID", required = false) Integer theQuoteTypeID,
			@RequestParam(value = "quoteDate", required = false) String theDateNow,
			@RequestParam(value = "rxContactId", required = false) String theContactId,
			@RequestParam(value = "rxMasterId", required = false) String therxMasterId,
			@RequestParam(value = "emailstatus", required = false) String emailstatus,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws Exception {
			Integer returnvalue=0;
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			String aJoMasterID = Integer.toString(theJoMasterID);
			String aQuoteTypeID = Integer.toString(theQuoteTypeID);
			theRevision = "sendMail";
			String aQuoteRev = "value";
			Integer aJoQuoteHeaderID = pdfService.getQuoteHeaderID(aQuoteTypeID, theRevision, aJoMasterID, aQuoteRev);
			JoQuoteHeader aJoQuoteHeader = jobService.getSingleQuoteHeaderDetails(aJoQuoteHeaderID);
			
			logger.info("Date from Client: "+DateUtils.parseDate(theDateNow,new String[] { "MM/dd/yyyy hh:mm a" }));
			
			

			Joquotehistory thejoquotehistory=new Joquotehistory();
			thejoquotehistory.setJoQuoteHeaderId(aJoQuoteHeaderID);
			thejoquotehistory.setJoMasterId(ConvertintoInteger(aJoMasterID));
			thejoquotehistory.setRxMasterId(ConvertintoInteger(therxMasterId));
			thejoquotehistory.setRxContactId(ConvertintoInteger(theContactId));
			thejoquotehistory.setQuoteRev(aJoQuoteHeader.getQuoteRev());
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			theDateNow = formatter.format(currentDate.getTime());
			System.out.println("theDateNow"+theDateNow);
			thejoquotehistory.setQuoteDate(DateUtils.parseDate(theDateNow,new String[] { "MM/dd/yyyy hh:mm a" }));
			if(emailstatus.equals("sent"))
			{
				thejoquotehistory.setQuoteStatus((byte) 1);
			}else if(emailstatus.equals("Printed"))
			{
				thejoquotehistory.setQuoteStatus((byte) 2);
			}else{
				thejoquotehistory.setQuoteStatus((byte) 0);
			}
			
			thejoquotehistory.setJoBidderId(theJoBidderID);
			thejoquotehistory.setQuoteTypeID(theQuoteTypeID);
			System.out.println("==============="+aUserBean.getUserId());
			thejoquotehistory.setEmployeeId(aUserBean.getUserId());
			thejoquotehistory.setQuoteAmount(aJoQuoteHeader.getQuoteAmount());
			thejoquotehistory.setQuoteDate(new Date());
			returnvalue=jobService.saveJoquoteHistory(thejoquotehistory);
			
		} catch (JobException e) {
			sendTransactionException("<b>theJoMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return returnvalue;
	}
	@RequestMapping(value = "/settotalAndCostAmountForQuote", method = RequestMethod.GET)
	public @ResponseBody JoQuoteHeader settotalAndCostAmountForQuote(
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer theQuoteHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
		try {
			aJoQuoteHeader = quoteTemplateService.getDetailsMSTRAmounts(theQuoteHeaderID);
		} catch (JobException e) {
			sendTransactionException("<b>theQuoteHeaderID:</b>"+theQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteHeader;
	}
	@RequestMapping(value = "/settotalAndCostAmountForQuoteTemplate", method = RequestMethod.GET)
	public @ResponseBody JoQuotetemplateHeader settotalAndCostAmountForQuoteTemplate(
			@RequestParam(value = "joQuoteTemplateHeaderID", required = false) Integer joQuoteTemplateHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JoQuotetemplateHeader aJoQuotetemplateHeader = new JoQuotetemplateHeader();
		try {
			aJoQuotetemplateHeader = quoteTemplateService.getTemplateDetailsMSTRAmounts(joQuoteTemplateHeaderID);
		} catch (JobException e) {
			sendTransactionException("<b>joQuoteTemplateHeaderID:</b>"+joQuoteTemplateHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuotetemplateHeader;
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
	
//	@RequestMapping(value = "/DeleteQuoteMultipleLineItems", method = RequestMethod.POST)
//	public @ResponseBody Boolean DeleteQuoteMultipleLineItems(
//			HttpSession session,HttpServletRequest therequest,
//			HttpServletResponse theResponse,
//			@RequestParam(value = "delData[]", required = true)ArrayList<String>  delData,
//			@RequestParam(value = "delpositionData[]", required = true)ArrayList<String>  delpositionData,
//			@RequestParam(value = "joQuoteHeaderID", required = true)Integer  joQuoteHeaderID
//			)
//			throws IOException, MessagingException, JobException {
//		Boolean returnValue=false;
//		System.out.println("insid the loadquotelineitems");
//		if(delData!=null){
//			int i=0;
//			for(String detailID:delData){
//				Integer positionID=JobUtil.ConvertintoInteger(delpositionData.get(i));
//				Integer joQuoteDetailMstrID=JobUtil.ConvertintoInteger(detailID);
//				System.out.println("PositionID=="+positionID+"===joQuoteDetailMstrID==="+joQuoteDetailMstrID);
//				jobService.WhiledeleteUpdatePosition(joQuoteHeaderID, positionID);
//				jobService.deleteJoNewQuoteDetail(joQuoteDetailMstrID, null, null);
//				i=i+1;
//			}
//		}
//		return returnValue;
//	}
	
	@RequestMapping(value = "/DeleteQuoteTemplateMultipleLineItems", method = RequestMethod.GET)
	public @ResponseBody Boolean DeleteQuoteTemplateMultipleLineItems(
			HttpSession session,HttpServletRequest therequest,
			HttpServletResponse theResponse,
			@RequestParam(value = "delData[]", required = true)ArrayList<String>  delData,
			@RequestParam(value = "delpositionData[]", required = true)ArrayList<String>  delpositionData,
			@RequestParam(value = "joQuoteTempHeaderID", required = true)Integer  joQuoteTempHeaderID
			)
			throws IOException, MessagingException {
		Boolean returnValue=false;
		try{
			System.out.println("insid the DeleteQuoteLineItems_template");
			
			if(delData!=null){
				int i=0;
				for(String detailID:delData){
					Integer positionID=JobUtil.ConvertintoInteger(delpositionData.get(i));
					Integer joQuoteTempDetailMstrID=JobUtil.ConvertintoInteger(detailID);
					System.out.println("PositionID=="+positionID+"===joQuoteTempDetailMstrID==="+joQuoteTempDetailMstrID);
					jobService.WhiledeleteUpdatePosition_template(joQuoteTempHeaderID, positionID);
					jobService.deleteJoNewQuoteDetail_template(joQuoteTempDetailMstrID, null, null);
					i=i+1;
				}
			}
			
			//returnValue=jobService.deleteJoNewQuoteDetail_template(joquoteDetailid,quoteTemplateHeaderID,position);
		} catch (JobException e) {
			sendTransactionException("<b>quoteTemplateHeaderID,joquoteDetailid:</b>"+joQuoteTempHeaderID+",delData=="+delData,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(),
					e.getMessage());
		}
		return returnValue;
	}
	@RequestMapping(value = "/SaveQuoteLineItems", method = RequestMethod.POST)
	public @ResponseBody Integer SaveQuoteLineItems(
			@RequestParam(value = "joQuoteHeaderID") Integer joQuoteHeaderID,
			@RequestParam(value = "quoteTypeDetail") String quoteTypeDetail,
			@RequestParam(value = "jobQuoteSubmittedBYID") String jobQuoteSubmittedBYID,
			@RequestParam(value = "jobQuoteInternalNote") String jobQuoteInternalNote,
			@RequestParam(value = "quoteTotalPrice") String quoteTotalPrice,
			@RequestParam(value = "joMasterID") Integer joMasterID,
			@RequestParam(value = "jobQuoteSubmittedBYInitials") String jobQuoteSubmittedBYInitials,
			@RequestParam(value = "jobQuoteRevision") String jobQuoteRevision,
			@RequestParam(value = "quotecosttotalamount") String quotecosttotalamount,
			@RequestParam(value = "quotechkTotalPrice") Boolean quotechkTotalPrice,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "DelQUOData[]",required = false) ArrayList<String>  delData,
			@RequestParam(value = "editorcopy") String editorcopy,
			@RequestParam(value = "chk_includeLSD") Short chk_includeLSD,
			@RequestParam(value = "chk_donotQtyitem2and3") Short chk_donotQtyitem2and3,
			@RequestParam(value = "chk_showTotPriceonly") Short chk_showTotPriceonly,
			@RequestParam(value = "txt_LSDValue") BigDecimal txt_LSDValue,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException, ParseException {
		
		
		Byte TotalCheck=0;
		if(quotechkTotalPrice==true){
			TotalCheck=1;
		}
		JoQuoteHeader joqh=new JoQuoteHeader();
		Integer cumastertypeid=ConvertintoInteger(quoteTypeDetail);
		joqh.setCuMasterTypeID(cumastertypeid);
		joqh.setJoMasterID(joMasterID);
		joqh.setQuoteRev(jobQuoteRevision);
		joqh.setCreatedById(ConvertintoInteger(jobQuoteSubmittedBYID));
		joqh.setQuoteAmount(ConvertintoBigDecimal(quoteTotalPrice));
		joqh.setInternalNote(jobQuoteInternalNote);
		joqh.setCreatedByName(jobQuoteSubmittedBYInitials);
		joqh.setCostAmount(ConvertintoBigDecimal(quotecosttotalamount));
		joqh.setPrintTotal(TotalCheck);
		joqh.setIncludeLSD(chk_includeLSD);
		joqh.setDonotQtyitem2and3(chk_donotQtyitem2and3);
		joqh.setShowTotPriceonly(chk_showTotPriceonly);
		if(txt_LSDValue==null){
			txt_LSDValue=BigDecimal.ZERO;
		}
		joqh.setLSDValue(txt_LSDValue);
		System.out.println("Print total=="+TotalCheck);
		if(editorcopy.equals("copy")|| joQuoteHeaderID==0){
			joQuoteHeaderID=jobService.addjoquoteheader(joqh);
		}else{
			joqh.setJoQuoteHeaderId(joQuoteHeaderID);
			jobService.updatejoquoteheader(joqh);
		}
		
		
		JsonParser parser = new JsonParser();
		if (delData!=null && delData.size()>0 && !editorcopy.equals("copy")) {
			for(String joQuoteDetailID:delData){
				Integer joQuoteDetailMstrIdd=JobUtil.ConvertintoInteger(joQuoteDetailID);
				joQuoteDetailMstr ajoQuoteDetailMstr=new  joQuoteDetailMstr();
				ajoQuoteDetailMstr.setJoQuoteDetailMstrID(joQuoteDetailMstrIdd);
				jobService.UpdatenewQuotes(ajoQuoteDetailMstr, "del");
			}
		}
		if ( gridData!=null) {

			System.out.println("gridData"+gridData);
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			int i=1;
			for (JsonElement ele1 : array) {
				JsonObject obj = ele1.getAsJsonObject();
				joQuoteDetailMstr ajoQuoteDetailMstr=new joQuoteDetailMstr();
				Integer joQuoteDetailMstrID=null;
				if(obj.get("joQuoteDetailMstrID")!=null && obj.get("joQuoteDetailMstrID").getAsString()!=""&& obj.get("joQuoteDetailMstrID").getAsString().length()>0){
					joQuoteDetailMstrID=obj.get("joQuoteDetailMstrID").getAsInt();
				}
				Integer type=obj.get("type").getAsInt();
				String texteditor=obj.get("texteditor").getAsString();
				if(texteditor!=null){
				texteditor=texteditor.replaceAll("size: xx-small;", "size: 7pt");
				texteditor=texteditor.replaceAll("size: x-small","size: 7.5pt");
				texteditor=texteditor.replaceAll("size: xx-large","size: 7.5pt");
				texteditor=texteditor.replaceAll("size: x-large","size: 7.5pt");
				texteditor=texteditor.replaceAll("size: smaller","size: 10pt");
				texteditor=texteditor.replaceAll("size: small", "size: 10pt");
				texteditor=texteditor.replaceAll("size: medium", "size: 12pt");
				texteditor=texteditor.replaceAll("size: larger", "size: 14pt");
				texteditor=texteditor.replaceAll("size: large", "size: 13.5pt");
				}
				//Andale Mono=andale mono,times;Arial=Arial,sans-serif;Courier New=Courier New;Helvetica=helvetica;Symbol=Symbol;
				//Tahoma=Tahoma,sans-serif;
				//Times New Roman=Times New Roman,serif;Verdana=Verdana,sans-serif;
				if(texteditor!=null){
					texteditor=texteditor.replaceAll("'Times New Roman', serif;", "Times New Roman, serif;");
					texteditor=texteditor.replaceAll("'andale mono', times;", "andale mono, times;");
					texteditor=texteditor.replaceAll("'Arial', sans-serif;", "Arial,sans-serif;");
					texteditor=texteditor.replaceAll("'Courier New';", "Courier New;");
					texteditor=texteditor.replaceAll("'helvetica';", "helvetica;");
					texteditor=texteditor.replaceAll("'Symbol';", "Symbol;");
					texteditor=texteditor.replaceAll("'Tahoma', sans-serif;", "Tahoma, sans-serif;");
					texteditor=texteditor.replaceAll("'Verdana', sans-serif;", "Verdana, sans-serif;");
					texteditor=texteditor.replaceAll("<br>", "<br />");
					texteditor=texteditor.replaceAll("<div ","<p ");
					texteditor=texteditor.replaceAll("</div","</p");
				}
				BigDecimal sellprice=BigDecimal.ZERO;
				String sellprice_String=obj.get("sellprice").getAsString();
				if(sellprice_String!=null && sellprice_String!="" && sellprice_String.length()>0){
				sellprice_String=sellprice_String.replaceAll("\\$", "");
				sellprice_String=sellprice_String.replaceAll(",", "");
				sellprice=JobUtil.ConvertintoBigDecimal(sellprice_String);
				}
				String manufacturer_String=obj.get("manufacturer").getAsString();
				Integer manufacturer=0;
				if(manufacturer_String!=null && manufacturer_String!="" && manufacturer_String.length()>0){
					manufacturer=JobUtil.ConvertintoInteger(manufacturer_String);
				}
				String Quantity=null;
				String quantity_string=obj.get("quantity").getAsString();
				if(quantity_string!=null && quantity_string!="" && quantity_string.length()>0){
					Quantity=quantity_string;
				}
				
				BigDecimal cost=BigDecimal.ZERO;
				String cost_String=obj.get("cost").getAsString();
				if(cost_String!=null && cost_String!="" && cost_String.length()>0){
					cost_String=cost_String.replaceAll("\\$", "");
					cost_String=cost_String.replaceAll(",", "");
					cost=JobUtil.ConvertintoBigDecimal(cost_String);
				}
				String category_String=obj.get("category").getAsString();
				Integer category=JobUtil.ConvertintoInteger(category_String);
				
				String textbox=null;
				String textbox_string=obj.get("textbox").getAsString();
				if(textbox_string!=null && textbox_string!="" && textbox_string.length()>0){
					textbox=textbox_string;
				}
				boolean linebreak=false;
				String linebreak_String=obj.get("linebreak").getAsString();
				if(linebreak_String!=null && linebreak_String.equals("Yes")){
					linebreak=true;
				}
				if(type==1){
					ajoQuoteDetailMstr.setType(type);
					ajoQuoteDetailMstr.setTexteditor(texteditor);
					ajoQuoteDetailMstr.setLinebreak(linebreak);
				}else if(type==2){
					ajoQuoteDetailMstr.setType(type);
					ajoQuoteDetailMstr.setQuantity(Quantity);
					ajoQuoteDetailMstr.setCost(cost);
					ajoQuoteDetailMstr.setManufacturer(manufacturer);
					ajoQuoteDetailMstr.setTexteditor(texteditor);
					ajoQuoteDetailMstr.setCategory(category);
					ajoQuoteDetailMstr.setLinebreak(linebreak);
				}else if(type==3){
					ajoQuoteDetailMstr.setType(type);
					ajoQuoteDetailMstr.setQuantity(Quantity);
					ajoQuoteDetailMstr.setCost(cost);
					ajoQuoteDetailMstr.setManufacturer(manufacturer);
					ajoQuoteDetailMstr.setTexteditor(texteditor);
					ajoQuoteDetailMstr.setSellprice(sellprice);
					ajoQuoteDetailMstr.setCategory(category);
					ajoQuoteDetailMstr.setLinebreak(linebreak);
				}else if(type==4){
					ajoQuoteDetailMstr.setType(type);
					ajoQuoteDetailMstr.setTextbox(textbox);
					ajoQuoteDetailMstr.setSellprice(sellprice);
					ajoQuoteDetailMstr.setLinebreak(linebreak);
				}
				String Oper="add";
				if(joQuoteDetailMstrID!=null && joQuoteDetailMstrID>0 && !editorcopy.equals("copy")){
					Oper="edit";
					ajoQuoteDetailMstr.setJoQuoteDetailMstrID(joQuoteDetailMstrID);
				}
				ajoQuoteDetailMstr.setJoQuoteHeaderID(joQuoteHeaderID);
				ajoQuoteDetailMstr.setPosition(i);
				jobService.UpdatenewQuotes(ajoQuoteDetailMstr, Oper);
				i=i+1;
			}
		}
	
		
		
		return joQuoteHeaderID;
	}
	
	@RequestMapping(value = "/copyQuoteTemplate", method = RequestMethod.POST)
	public @ResponseBody CustomResponse copyQuoteTemplate(
			@RequestParam(value = "joquoteheaderid", required = false) Integer joquoteheaderid,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "QuoteTemplateID", required = false) Integer QuoteTemplateID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			ArrayList<joQuoteDetailMstr> overAllquoteDetailLst=new ArrayList<joQuoteDetailMstr>();
			JsonParser parser = new JsonParser();
			if ( gridData!=null) {

					System.out.println("gridData"+gridData);
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					int i=1;
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						joQuoteDetailMstr ajoQuoteDetailMstr=new joQuoteDetailMstr();
						Integer joQuoteDetailMstrID=null;
						if(obj.get("joQuoteDetailMstrID")!=null && obj.get("joQuoteDetailMstrID").getAsString()!=""&& obj.get("joQuoteDetailMstrID").getAsString().length()>0){
							joQuoteDetailMstrID=obj.get("joQuoteDetailMstrID").getAsInt();
						}
						Integer type=obj.get("type").getAsInt();
						String texteditor=obj.get("texteditor").getAsString();
						if(texteditor!=null){
						texteditor=texteditor.replaceAll("size: xx-small;", "size: 7pt");
						texteditor=texteditor.replaceAll("size: x-small","size: 7.5pt");
						texteditor=texteditor.replaceAll("size: xx-large","size: 7.5pt");
						texteditor=texteditor.replaceAll("size: x-large","size: 7.5pt");
						texteditor=texteditor.replaceAll("size: smaller","size: 10pt");
						texteditor=texteditor.replaceAll("size: small", "size: 10pt");
						texteditor=texteditor.replaceAll("size: medium", "size: 12pt");
						texteditor=texteditor.replaceAll("size: larger", "size: 14pt");
						texteditor=texteditor.replaceAll("size: large", "size: 13.5pt");
						}
						BigDecimal sellprice=BigDecimal.ZERO;
						String sellprice_String=obj.get("sellprice").getAsString();
						if(sellprice_String!=null && sellprice_String!="" && sellprice_String.length()>0){
						sellprice_String=sellprice_String.replaceAll("\\$", "");
						sellprice_String=sellprice_String.replaceAll(",", "");
						sellprice=JobUtil.ConvertintoBigDecimal(sellprice_String);
						}
						String manufacturer_String=obj.get("manufacturer").getAsString();
						Integer manufacturer=0;
						if(manufacturer_String!=null && manufacturer_String!="" && manufacturer_String.length()>0){
							manufacturer=JobUtil.ConvertintoInteger(manufacturer_String);
						}
						String Quantity=null;
						String quantity_string=obj.get("quantity").getAsString();
						if(quantity_string!=null && quantity_string!="" && quantity_string.length()>0){
							Quantity=quantity_string;
						}
						
						BigDecimal cost=BigDecimal.ZERO;
						String cost_String=obj.get("cost").getAsString();
						if(cost_String!=null && cost_String!="" && cost_String.length()>0){
							cost_String=cost_String.replaceAll("\\$", "");
							cost_String=cost_String.replaceAll(",", "");
							cost=JobUtil.ConvertintoBigDecimal(cost_String);
						}
						String category_String=obj.get("category").getAsString();
						Integer category=JobUtil.ConvertintoInteger(category_String);
						
						String textbox=null;
						String textbox_string=obj.get("textbox").getAsString();
						if(textbox_string!=null && textbox_string!="" && textbox_string.length()>0){
							textbox=textbox_string;
						}
						String typename=obj.get("typename").getAsString();
						String description=obj.get("description").getAsString();
						String vendorname=obj.get("vendorname").getAsString();
						boolean linebreak=false;
						String linebreak_String=obj.get("linebreak").getAsString();
						if(linebreak_String!=null && linebreak_String.equals("Yes")){
							linebreak=true;
						}
						if(type==1){
							ajoQuoteDetailMstr.setType(type);
							ajoQuoteDetailMstr.setTypename(typename);
							ajoQuoteDetailMstr.setTexteditor(texteditor);
							ajoQuoteDetailMstr.setDescription(description);
						}else if(type==2){
							ajoQuoteDetailMstr.setType(type);
							ajoQuoteDetailMstr.setTypename(typename);
							ajoQuoteDetailMstr.setQuantity(Quantity);
							ajoQuoteDetailMstr.setCost(cost);
							ajoQuoteDetailMstr.setManufacturer(manufacturer);
							ajoQuoteDetailMstr.setVendorname(vendorname);
							ajoQuoteDetailMstr.setTexteditor(texteditor);
							ajoQuoteDetailMstr.setDescription(description);
							ajoQuoteDetailMstr.setCategory(category);
						}else if(type==3){
							ajoQuoteDetailMstr.setType(type);
							ajoQuoteDetailMstr.setTypename(typename);
							ajoQuoteDetailMstr.setQuantity(Quantity);
							ajoQuoteDetailMstr.setCost(cost);
							ajoQuoteDetailMstr.setManufacturer(manufacturer);
							ajoQuoteDetailMstr.setVendorname(vendorname);
							ajoQuoteDetailMstr.setTexteditor(texteditor);
							ajoQuoteDetailMstr.setDescription(description);
							ajoQuoteDetailMstr.setSellprice(sellprice);
							ajoQuoteDetailMstr.setCategory(category);
						}else if(type==4){
							ajoQuoteDetailMstr.setType(type);
							ajoQuoteDetailMstr.setTypename(typename);
							ajoQuoteDetailMstr.setTextbox(textbox);
							ajoQuoteDetailMstr.setSellprice(sellprice);
						}
						ajoQuoteDetailMstr.setLinebreak(linebreak);
						if(joQuoteDetailMstrID!=null && joQuoteDetailMstrID>0){
							ajoQuoteDetailMstr.setJoQuoteDetailMstrID(joQuoteDetailMstrID);
						}
						ajoQuoteDetailMstr.setJoQuoteHeaderID(joquoteheaderid);
						i=i+1;
						overAllquoteDetailLst.add(ajoQuoteDetailMstr);
					}
			}
			ArrayList<joQuoteDetailMstr> joQuoteDetailMstrlst= jobService.getjoQuoteDetailMstrFromTemplate(QuoteTemplateID);
			for(joQuoteDetailMstr thejoQuoteDetailMstr:joQuoteDetailMstrlst){
				overAllquoteDetailLst.add(thejoQuoteDetailMstr);
			}
			aResponse.setRows(overAllquoteDetailLst);
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>Quote Copy From Template</b>","MethodName:copyQuoteTemplate",e,session,theRequest);
		}

		return aResponse;
	}
	@RequestMapping(value = "getjoQuoteDetailForPDF", method = RequestMethod.POST)
	public @ResponseBody
	JobQuotesBidListBean getjoQuoteDetailForPDF(
			@RequestParam(value = "joQuoteHeaderID", required = false) Integer joQuoteHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws JobException, IOException, MessagingException {
		JobQuotesBidListBean aJobQuotesBidListBean = null;
		try {
			aJobQuotesBidListBean = jobService.getjoQuoteDetailForPDF(joQuoteHeaderID);
		} catch (JobException e) {
			sendTransactionException("<b>joQuoteHeaderID:</b>"+joQuoteHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJobQuotesBidListBean;
	}
	@RequestMapping(value = "/SaveQuoteTempLineItems", method = RequestMethod.POST)
	public @ResponseBody Integer SaveQuoteTempLineItems(
			@RequestParam(value = "quoteTemplateHeaderId") Integer joQuoteTemplateHeaderID,
			@RequestParam(value = "quoteTotalPrice_template") String quoteTotalPrice,
			@RequestParam(value = "costTotalPrice") String quotecosttotalamount,
			@RequestParam(value = "quotechkTotalPrice_template") Boolean quotechkTotalPrice,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "DelQUOData[]",required = false) ArrayList<String>  delData,
			@RequestParam(value = "oper") String editorcopy,
			@RequestParam(value = "templatename") String templatename,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException, ParseException, QuoteTemplateException {
		boolean saved=false;
		Integer joQuotetempHeaderID=0;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		Integer aUserId = aUserBean.getUserId();
		Byte TotalCheck=0;
		if(quotechkTotalPrice==true){
			TotalCheck=1;
		}
		JoQuotetemplateHeader joqh=new JoQuotetemplateHeader();
		joqh.setCuMasterTypeId(-1);
		joqh.setCreatedById(aUserId);
		joqh.setQuoteAmount(ConvertintoBigDecimal(quoteTotalPrice));
		joqh.setCostAmount(ConvertintoBigDecimal(quotecosttotalamount));
		joqh.setPrintTotal(TotalCheck);
		joqh.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderID);
		joqh.setTemplateName(templatename);
		if(editorcopy.equalsIgnoreCase("add") || editorcopy.equalsIgnoreCase("edit"))
		{
			if(editorcopy.equalsIgnoreCase("add")){
			joqh.setGlobalTemplate(true);
			Integer joquotetempHeaderId = quoteTemplateService.addQuoteTemplate(joqh);
			joqh.setJoQuoteTemplateHeaderId(joquotetempHeaderId);
			}
			
			JsonParser parser = new JsonParser();
			if (delData!=null && delData.size()>0 && !editorcopy.equals("copy")) {
				for(String joQuoteDetailID:delData){
					Integer joQuoteDetailMstrIdd=JobUtil.ConvertintoInteger(joQuoteDetailID);
					joQuoteDetailMstr ajoQuoteDetailMstr=new  joQuoteDetailMstr();
					ajoQuoteDetailMstr.setJoQuoteDetailMstrID(joQuoteDetailMstrIdd);
					jobService.UpdatenewQuotes(ajoQuoteDetailMstr, "del");
				}
			}
			if ( gridData!=null) {

				System.out.println("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				System.out.println("array length==>"+array.size());
				int i=1;
				for (JsonElement ele1 : array) {
					JsonObject obj = ele1.getAsJsonObject();
					joQuoteTempDetailMstr ajoQuotetempDetailMstr=new joQuoteTempDetailMstr();
					Integer joQuoteTempDetailMstrID=null;
					if(obj.get("joQuoteTempDetailMstrID")!=null && obj.get("joQuoteTempDetailMstrID").getAsString()!=""&& obj.get("joQuoteTempDetailMstrID").getAsString().length()>0){
						joQuoteTempDetailMstrID=obj.get("joQuoteTempDetailMstrID").getAsInt();
					}
					Integer type=obj.get("type").getAsInt();
					String texteditor=obj.get("texteditor").getAsString();
					if(texteditor!=null){
					texteditor=texteditor.replaceAll("size: xx-small;", "size: 7pt");
					texteditor=texteditor.replaceAll("size: x-small","size: 7.5pt");
					texteditor=texteditor.replaceAll("size: xx-large","size: 7.5pt");
					texteditor=texteditor.replaceAll("size: x-large","size: 7.5pt");
					texteditor=texteditor.replaceAll("size: smaller","size: 10pt");
					texteditor=texteditor.replaceAll("size: small", "size: 10pt");
					texteditor=texteditor.replaceAll("size: medium", "size: 12pt");
					texteditor=texteditor.replaceAll("size: larger", "size: 14pt");
					texteditor=texteditor.replaceAll("size: large", "size: 13.5pt");
					}
					//Andale Mono=andale mono,times;Arial=Arial,sans-serif;Courier New=Courier New;Helvetica=helvetica;Symbol=Symbol;
					//Tahoma=Tahoma,sans-serif;
					//Times New Roman=Times New Roman,serif;Verdana=Verdana,sans-serif;
					if(texteditor!=null){
						texteditor=texteditor.replaceAll("'Times New Roman', serif;", "Times New Roman, serif;");
						texteditor=texteditor.replaceAll("'andale mono', times;", "andale mono, times;");
						texteditor=texteditor.replaceAll("'Arial', sans-serif;", "Arial,sans-serif;");
						texteditor=texteditor.replaceAll("'Courier New';", "Courier New;");
						texteditor=texteditor.replaceAll("'helvetica';", "helvetica;");
						texteditor=texteditor.replaceAll("'Symbol';", "Symbol;");
						texteditor=texteditor.replaceAll("'Tahoma', sans-serif;", "Tahoma, sans-serif;");
						texteditor=texteditor.replaceAll("'Verdana', sans-serif;", "Verdana, sans-serif;");
						texteditor=texteditor.replaceAll("<br>", "<br />");
						texteditor=texteditor.replaceAll("<div ","<p ");
						texteditor=texteditor.replaceAll("</div","</p");
					}
					BigDecimal sellprice=BigDecimal.ZERO;
					String sellprice_String=obj.get("sellprice").getAsString();
					if(sellprice_String!=null && sellprice_String!="" && sellprice_String.length()>0){
					sellprice_String=sellprice_String.replaceAll("\\$", "");
					sellprice_String=sellprice_String.replaceAll(",", "");
					sellprice=JobUtil.ConvertintoBigDecimal(sellprice_String);
					}
					String manufacturer_String=obj.get("manufacturer").getAsString();
					Integer manufacturer=0;
					if(manufacturer_String!=null && manufacturer_String!="" && manufacturer_String.length()>0){
						manufacturer=JobUtil.ConvertintoInteger(manufacturer_String);
					}
					String Quantity=null;
					String quantity_string=obj.get("quantity").getAsString();
					if(quantity_string!=null && quantity_string!="" && quantity_string.length()>0){
						Quantity=quantity_string;
					}
					
					BigDecimal cost=BigDecimal.ZERO;
					String cost_String=obj.get("cost").getAsString();
					if(cost_String!=null && cost_String!="" && cost_String.length()>0){
						cost_String=cost_String.replaceAll("\\$", "");
						cost_String=cost_String.replaceAll(",", "");
						cost=JobUtil.ConvertintoBigDecimal(cost_String);
					}
					String category_String=obj.get("category").getAsString();
					Integer category=JobUtil.ConvertintoInteger(category_String);
					
					String textbox=null;
					String textbox_string=obj.get("textbox").getAsString();
					if(textbox_string!=null && textbox_string!="" && textbox_string.length()>0){
						textbox=textbox_string;
					}
					boolean linebreak=false;
					String linebreak_String=obj.get("linebreak").getAsString();
					if(linebreak_String!=null && linebreak_String.equals("Yes")){
						linebreak=true;
					}
					if(type==1){
						ajoQuotetempDetailMstr.setType(type);
						ajoQuotetempDetailMstr.setTexteditor(texteditor);
						ajoQuotetempDetailMstr.setLinebreak(linebreak);
					}else if(type==2){
						ajoQuotetempDetailMstr.setType(type);
						ajoQuotetempDetailMstr.setQuantity(Quantity);
						ajoQuotetempDetailMstr.setCost(cost);
						ajoQuotetempDetailMstr.setManufacturer(manufacturer);
						ajoQuotetempDetailMstr.setTexteditor(texteditor);
						ajoQuotetempDetailMstr.setCategory(category);
						ajoQuotetempDetailMstr.setLinebreak(linebreak);
					}else if(type==3){
						ajoQuotetempDetailMstr.setType(type);
						ajoQuotetempDetailMstr.setQuantity(Quantity);
						ajoQuotetempDetailMstr.setCost(cost);
						ajoQuotetempDetailMstr.setManufacturer(manufacturer);
						ajoQuotetempDetailMstr.setTexteditor(texteditor);
						ajoQuotetempDetailMstr.setSellprice(sellprice);
						ajoQuotetempDetailMstr.setCategory(category);
						ajoQuotetempDetailMstr.setLinebreak(linebreak);
					}else if(type==4){
						ajoQuotetempDetailMstr.setType(type);
						ajoQuotetempDetailMstr.setTextbox(textbox);
						ajoQuotetempDetailMstr.setSellprice(sellprice);
						ajoQuotetempDetailMstr.setLinebreak(linebreak);
					}
					String Oper="add";
					if(joQuoteTempDetailMstrID!=null && joQuoteTempDetailMstrID>0){
						Oper="edit";
						ajoQuotetempDetailMstr.setJoQuoteTempDetailMstrID(joQuoteTempDetailMstrID);
					}
					ajoQuotetempDetailMstr.setJoQuoteTemplateHeaderID(joqh.getJoQuoteTemplateHeaderId());
					ajoQuotetempDetailMstr.setPosition(i);
					jobService.UpdatenewQuotes_template(ajoQuotetempDetailMstr, Oper);
					i=i+1;
				}
			}
			saved=true;
			joQuotetempHeaderID=joqh.getJoQuoteTemplateHeaderId();
		}else if(editorcopy.equalsIgnoreCase("delete")){
			saved = quoteTemplateService.deleteQuoteTemplate(joqh);
		}
		
		
		return joQuotetempHeaderID;
	}
	@RequestMapping(value = "checktemplatenamethereornot", method = RequestMethod.GET)
	public @ResponseBody
	boolean checktemplatenamethereornot(
			@RequestParam(value = "templateDescription", required = false) String templateDescription,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws JobException, IOException, MessagingException {
		boolean returnvalue=false;
		try {
			returnvalue = jobService.gettemplatethereornot(templateDescription);
		} catch (Exception e) {
			sendTransactionException("<b>templateDescription validate:</b>"+templateDescription,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			return returnvalue;
		}
		return returnvalue;
	}
}