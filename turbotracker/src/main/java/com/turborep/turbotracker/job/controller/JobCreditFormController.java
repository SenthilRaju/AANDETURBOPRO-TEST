package com.turborep.turbotracker.job.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.employee.service.EmployeeService;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/jobtabs4")
public class JobCreditFormController {

	Logger itsLogger = Logger.getLogger(JobCreditFormController.class);
	
	private Jomaster itsJomasterDetails = null;
	private String itsCutomerRxName;
	
	@Resource(name = "jobService")
	private JobService itsJobService;

	@Resource(name = "rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name = "userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;
	
	@RequestMapping(value = "jobwizard_credit", method = RequestMethod.GET)
	public String getJobsCreditPage (@RequestParam(value="jobNumber", required=false) String theJobNumber,
									@RequestParam(value="jobName", required=false) String theJobName,
									@RequestParam(value="jobCustomer", required=false) String theJobCustomer,
									@RequestParam(value="joMasterID", required=false) Integer joMasterID,
									HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws IOException, CompanyException, EmployeeException, MessagingException {
		Rxaddress bondAgentAddress = null;
		Rxaddress ownerAddress = null;
		List<Rxcontact> gcContacts = null;
		List<Rxcontact> bondAgentContacts = null;
		List<Rxcontact> ownerContacts = null;
		List<TsUserLogin> creditByUserList = null;
		Sysvariable aSysvariable = null;
		List<Sysvariable> aSysvariableList =null;
			try{
				setJomasterDetails(theJobNumber,joMasterID);
				String generalContractorName = getGeneralContractorName();
				if(itsJomasterDetails.getRxCategory3() != null) {
					gcContacts = getGcContacts(itsJomasterDetails.getRxCategory3());
				}
				if(itsJomasterDetails.getRxCategory4() != null) {
					ownerAddress = getOwnerAddress(itsJomasterDetails.getRxCategory4());
				}
				if(itsJomasterDetails.getRxCategory5() != null) {
					bondAgentAddress = getBondAgentAddress(itsJomasterDetails.getRxCategory5());
					bondAgentContacts = getBondAgentContacts(itsJomasterDetails.getRxCategory5());
				}
				creditByUserList = getCreditByUserList();
				Jomaster aJomaster = getJomasterDetails();
				if (aJomaster.getRxCustomerId() != null) {
					setCutomerRxName((String)itsCompanyService.getCustomerName(aJomaster.getRxCustomerId()));
				}
				theModel.addAttribute("joMasterDetails", aJomaster);
				if(aJomaster.getDescription() != null){
					String aString = aJomaster.getDescription();
					String string = aString.replaceAll("\"", "");
					theModel.addAttribute("joMasterDescription", string);
				}
				if (aJomaster.getRxCustomerId() != null) {
					theModel.addAttribute("CustomerName", getCutomerRxName());
				}
				theModel.addAttribute("generalContractor", generalContractorName);
				theModel.addAttribute("bondAgentAddress", bondAgentAddress);
				theModel.addAttribute("gcContacts", gcContacts);
				theModel.addAttribute("ownerAddress", ownerAddress);
				theModel.addAttribute("bondAgentContacts", bondAgentContacts);
				theModel.addAttribute("ownerContacts", ownerContacts);
				theModel.addAttribute("creditByUserList", creditByUserList);
				
				for(int i =1;i<=3;i++)
				{
				aSysvariable = itsEmployeeService.getSysVariableDetails("JobNoticelabel"+i+"Txt");
				itsLogger.info("ValueString::["+aSysvariable.getValueString()+"]");				
				theModel.addAttribute("JobNoticelabel"+i, aSysvariable);
				}
				
			} catch (JobException e) {
				sendTransactionException("<b>theJobNumber,theJobCustomer:</b>"+theJobNumber+","+theJobCustomer,"JOB",e,session,therequest);
				itsLogger.error("Error in credit form controller: " + e.getMessage(), e);
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
				
			}
			return "job/jobwizard_credit";
		}
	
		public Jomaster getJomasterDetails() {
			return itsJomasterDetails;
		}

		public void setJomasterDetails(String jobNumber,Integer joMasterID) throws JobException {
			this.itsJomasterDetails = itsJobService.getSingleJobDetails(jobNumber,joMasterID);
		}
	
		public String getGeneralContractorName() {
			Jomaster aJomaster = itsJomasterDetails;
			Integer aGCID = null;
			if (aJomaster.getRxCategory3() != null) {
				aGCID = aJomaster.getRxCategory3();
				return itsRolodexService.getRxCategoty3(aGCID);
			} else return null;
		}
		
		public Rxaddress getBondAgentAddress(Integer bondAgentID) {
			if (bondAgentID != null) {
				return itsRolodexService.getAddress(bondAgentID);
			} else return null;
		}
	
		public List<Rxcontact> getBondAgentContacts(Integer bondAgentID) {
			return itsRolodexService.getContacts(bondAgentID);
		}
	
		public List<Rxcontact> getGcContacts(Integer aGcId) {
			if (aGcId != null) {
				return itsRolodexService.getContacts(aGcId);
			} else return null;
		}
	
		public Rxaddress getOwnerAddress(Integer aOwnerId) {
			if (aOwnerId != null) {
				return itsRolodexService.getAddress(aOwnerId);
			} else return null;
		}
	
		public List<Rxcontact> getOwnerContacts(Integer aOwnerId) {
			if(aOwnerId != null){
				return itsRolodexService.getContacts(aOwnerId);
			} else return null;
		}
	
		public List<TsUserLogin> getCreditByUserList() {
			try {
				return itsUserService.getAllUserList();
			} catch (UserException e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				return null;
			}
		}
		
		@RequestMapping(value="/ownerNameList", method = RequestMethod.GET)
		public @ResponseBody List<?> getOwnerNameList(@RequestParam("term") String theOwnerName, HttpServletResponse theResponse,
				HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
			itsLogger.debug("Received request to get search Jobs Lists");
			ArrayList<AutoCompleteBean> aOwnerNameList = null;
			try{
				aOwnerNameList = (ArrayList<AutoCompleteBean>) itsJobService.getOwnerNameList(theOwnerName);
			}catch (JobException e) {
				sendTransactionException("<b>theOwnerName:</b>"+theOwnerName,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aOwnerNameList;
		}
		
		@RequestMapping(value="/bondNameList", method = RequestMethod.GET)
		public @ResponseBody List<?> getBondNameList(@RequestParam("term") String theBondAgent, HttpServletResponse theResponse,
				HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
			itsLogger.debug("Received request to get search Jobs Lists");
			ArrayList<AutoCompleteBean> aBondNameList = null;
			try{
				aBondNameList = (ArrayList<AutoCompleteBean>) itsJobService.getBondAgentList(theBondAgent);
			}catch (JobException e) {
				sendTransactionException("<b>theBondAgent:</b>"+theBondAgent,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aBondNameList;
		}
		
		@RequestMapping(value="/filterOwnerList", method = RequestMethod.GET)
		public @ResponseBody List<?> getFilterOwnerList(@RequestParam(value="ownerId", required = false) String theFilterName, 
				HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
			itsLogger.debug("Received request to get search Jobs Lists");
			ArrayList<AutoCompleteBean> aFilterOwnerNameList = null;
			try{
				aFilterOwnerNameList = (ArrayList<AutoCompleteBean>) itsJobService.getFilterOwnerList(theFilterName);
			}catch (JobException e) {
				sendTransactionException("<b>theFilterName:</b>"+theFilterName,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aFilterOwnerNameList;
		}
		
		@RequestMapping(value="/customerContactList", method = RequestMethod.GET)
		public @ResponseBody List<?> getCustomerContactList(@RequestParam("term") String theContactName, HttpServletResponse theResponse
				,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
			itsLogger.debug("Received request to get search Jobs Lists");
			ArrayList<AutoCompleteBean> aFilterOwnerNameList = null;
			try{
				aFilterOwnerNameList = (ArrayList<AutoCompleteBean>) itsJobService.getCustomerContactList(theContactName);
			}catch (JobException e) {
				sendTransactionException("<b>theContactName:</b>"+theContactName,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aFilterOwnerNameList;
		}
		
		@RequestMapping(value="/rxAddress", method = RequestMethod.GET)
		public @ResponseBody List<?> getRxAddress(@RequestParam(value="rxAddressId", required = false) String theFilterName, HttpServletResponse theResponse
				,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
			itsLogger.debug("Received request to get search Jobs Lists");
			ArrayList<Rxaddress> aRxAddressName = null;
			try{
				aRxAddressName = (ArrayList<Rxaddress>) itsJobService.getRxAddress(theFilterName);
			}catch (JobException e) {
				sendTransactionException("<b>theFilterName:</b>"+theFilterName,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aRxAddressName;
		}
		
		@RequestMapping(value="/SaveCreditDetailInfo", method = RequestMethod.POST)
		public @ResponseBody Jomaster insertQuote(@RequestParam(value="creditStatus", required= false) Byte theCreditStatus,
									@RequestParam(value="creditStatusDate", required= false) String theCreditStatusDate,
									@RequestParam(value="creditStatusChangedBy", required= false) Integer theCreditStatusChangedBy,
									@RequestParam(value="creditType", required= false) Byte theCreditType,
									@RequestParam(value="creditTypeDate", required= false) String theCreditTypeDate,
									@RequestParam(value="creditTypeChangedBy", required= false) Integer theCreditTypeChangedBy,
									@RequestParam(value="ownerName", required= false) String theOwnerName,
									@RequestParam(value="ownerhiddenName", required= false) Integer theOwnerHiddenName,
									@RequestParam(value="ownerAddress", required= false) String theOwnerAddress,
									@RequestParam(value="ownerContact", required= false) Integer theOwnerContact,
									@RequestParam(value="ownerSelectName", required= false) Integer theOwnerSelectName,
									@RequestParam(value="isRequestedNOC", required= false) Boolean theIsRequestedNOC,
									@RequestParam(value="requestedNOCDate", required= false) String theRequestNOCDate,
									@RequestParam(value="requestedNOCBy", required= false) Integer theRequestedNOCBy,
									@RequestParam(value="isReceivedNOC", required= false) Boolean theIsReceivedNOC,
									@RequestParam(value="receivedNOCDate", required= false) String theReceivedNOCDate,
									@RequestParam(value="receivedNOCBy", required= false) Integer theReceivedNOCBy,
									@RequestParam(value="isNTCSentGC", required= false) Boolean theIsNTCSentGC,
									@RequestParam(value="NTCSentGCDate", required= false) String theNTCSentGCDate, 
									@RequestParam(value="NTCSentGCBy", required= false) Integer theNTCSentGCBy,
									@RequestParam(value="creditReferenceNumber", required= false) String theCreditReferenceNumber,
									@RequestParam(value="GeneralContractorContact", required= false) Integer theGeneralContractorContact,
									@RequestParam(value="isBondedJob", required= false) Boolean theIsBondedJob,
									@RequestParam(value="bondAgentName", required= false) String theBondAgentName,
									@RequestParam(value="bondhiddenName", required= false) Integer theBondhiddenName,
									@RequestParam(value="bondAgentAddress", required= false) String theBondAgentAddress,
									@RequestParam(value="bondAgentContact", required= false) Integer theBondAgentContact,
									@RequestParam(value="isLienWaverSigned", required= false) Boolean theIsLienWaverSigned,
									@RequestParam(value="lienWaverSignedDate", required= false) String theLienWaverSignedDate,
									@RequestParam(value="lienWaverSignedBy", required= false) Integer theLienWaverSignedBy,
									@RequestParam(value="isLienWaverThrough", required= false) Boolean theIsLienWaverThrough,
									@RequestParam(value="lienWaverThroughDate", required= false) String theLienWaverThroughDate,
									@RequestParam(value="lienWaverThroughBy", required= false) Integer theLienWaverThroughBy,
									@RequestParam(value="lienWaverThroughAmountName", required= false) BigDecimal theLienWaverThroughAmountName,
									@RequestParam(value="isFinalWaiver", required= false) Boolean theIsFinalWaiver,
									@RequestParam(value="claimFiled", required= false) Byte theClaimFiled,
									@RequestParam(value="claimFiledDate", required= false) String theClaimFiledDate,
									@RequestParam(value="claimFiledBu", required= false) Integer theClaimFiledBu,
									@RequestParam(value="creditNotes", required= false) String theCreditNotes,
									@RequestParam(value="joMasterID", required= false) Integer joMasterID,
									@RequestParam(value="jobNumber", required= false) String theJobNumber, HttpServletResponse theResponse
									,HttpSession session,HttpServletRequest therequest) throws IOException, ParseException, MessagingException{
			Jomaster aJomaster = new Jomaster();
			try {
				aJomaster.setCreditStatus(theCreditStatus);
				if(theCreditStatusDate != null  && theCreditStatusDate != "") {
					aJomaster.setCreditStatusDate(DateUtils.parseDate(theCreditStatusDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho0(theCreditStatusChangedBy);
				aJomaster.setCreditType(theCreditType);
				if(theCreditTypeDate != null  && theCreditTypeDate != "") {
					aJomaster.setCreditTypeDate(DateUtils.parseDate(theCreditTypeDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho1(theCreditTypeChangedBy);
				if(theIsRequestedNOC != null){
					aJomaster.setRequestedNoc(theIsRequestedNOC);
				}
				if(theRequestNOCDate != null  && theRequestNOCDate != "") {
					aJomaster.setRequestedNocdate(DateUtils.parseDate(theRequestNOCDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho2(theRequestedNOCBy);
				if(theIsReceivedNOC != null){
					aJomaster.setReceivedNoc(theIsReceivedNOC);
				}
				if(theReceivedNOCDate != null  && theReceivedNOCDate != "") {
					aJomaster.setReceivedNocdate(DateUtils.parseDate(theReceivedNOCDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho3(theReceivedNOCBy);
				if(theIsNTCSentGC != null){
					aJomaster.setSentNtc(theIsNTCSentGC);
				}
				if(theNTCSentGCDate != null  && theNTCSentGCDate != "") {
					aJomaster.setSentNtcdate(DateUtils.parseDate(theNTCSentGCDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho4(theNTCSentGCBy);
				aJomaster.setRxCategory4(theOwnerHiddenName);
				aJomaster.setRxCategory5(theBondhiddenName);
				if(theIsBondedJob != null){
					aJomaster.setJobBonded(theIsBondedJob);
				}
				aJomaster.setCreditReferenceNumber(theCreditReferenceNumber);
				aJomaster.setCreditContact0(theGeneralContractorContact);
				aJomaster.setCreditContact1(theOwnerContact);
				aJomaster.setCreditContact2(theBondAgentContact);
				if(theIsLienWaverSigned != null){
					aJomaster.setLienWaverSigned(theIsLienWaverSigned);
				}
				if(theLienWaverSignedDate != null  && theLienWaverSignedDate != "") {
					aJomaster.setLienWaverSignedDate(DateUtils.parseDate(theLienWaverSignedDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho5(theLienWaverSignedBy);
				if(theIsLienWaverThrough != null){
					aJomaster.setLienWaverThrough(theIsLienWaverThrough);
				}
				if(theLienWaverThroughDate != null  && theLienWaverThroughDate != "") {
					aJomaster.setLienWaverThroughDate(DateUtils.parseDate(theLienWaverThroughDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho6(theLienWaverThroughBy);
				aJomaster.setLienWaverThroughAmount(theLienWaverThroughAmountName);
				if(theIsFinalWaiver != null){
					aJomaster.setLienWaverThroughFinal(theIsFinalWaiver);
				}
				aJomaster.setClaimFiled(theClaimFiled);
				if(theClaimFiledDate != null  && theClaimFiledDate != "") {
					aJomaster.setClaimFiledDate(DateUtils.parseDate(theClaimFiledDate, new String[]{"MM/dd/yyyy"}));
				}
				aJomaster.setWho7(theClaimFiledBu);
				aJomaster.setCreditNotes(theCreditNotes);
				aJomaster.setJobNumber(theJobNumber);
				aJomaster.setJoMasterId(joMasterID);
				itsJobService.saveCreditInfoDetails(aJomaster);
			} catch (JobException e) {
				sendTransactionException("<b>theCreditReferenceNumber:</b>"+theCreditReferenceNumber,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
				return null;
			}
			return aJomaster;
		}

		public String getCutomerRxName() {
			return itsCutomerRxName;
		}

		public void setCutomerRxName(String cutomerRxName) {
			this.itsCutomerRxName = cutomerRxName;
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