/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.job.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
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

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Jojournal;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.JoWizardAppletData;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

/**
 * This Class is a Controller for Job Journals to handle the Job journal requests. 
 * @author vish_pepala
 */
@Controller
@RequestMapping("/jobtabs7")
public class JobJournalFormController {

	Logger itsLogger = Logger.getLogger(JobJournalFormController.class);
	
	private String jobNumber;
	private String cutomerRxName;
	
	private Jomaster joMasterDetails;
	
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	/**
	 *  This method is a controller to handle the job journal page request.
	 * @return {@link String} Job Journal page.
	 * @throws IOException 
	 * @throws JobException 
	 */
	@RequestMapping(value = "jobwizard_journal", method = RequestMethod.GET)
	public String getJobwizardJournalPage(@RequestParam(value="jobNumber", required=false) String theJobNumber, 
			@RequestParam(value="jobName", required=false) String theJobName,
			@RequestParam(value="jobCustomer", required=false) String theJobCustomer,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session, ModelMap model, HttpServletResponse response) throws IOException, JobException {
		itsLogger.debug("Received request to show jobwizard_journal page");
		setJobNumber(theJobNumber);
		setJomasterDetails(jobNumber,joMasterID);
		Jomaster aJomaster = getJomasterDetails();
		if (aJomaster.getRxCustomerId() != null) {
			try {
				setCutomerRxName((String)companyService.getCustomerName(aJomaster.getRxCustomerId()));
			} catch (CompanyException e) {
				itsLogger.error(e.getMessage(), e);
				response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
		}
		model.addAttribute("joMasterDetails", aJomaster);
		if(aJomaster.getDescription() != null){
			String aString = aJomaster.getDescription();
			String string = aString.replaceAll("\"", "");
			model.addAttribute("joMasterDescription", string);
		}
		if (aJomaster.getRxCustomerId() != null) {
			model.addAttribute("CustomerName", getCutomerRxName());
		}
		return "job/jobwizard_journal";
	}

	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		
		this.jobNumber = jobNumber;
	}
	public Jomaster getJomasterDetails() {
			
			return joMasterDetails;
	}
	public void setJomasterDetails(String jobNumber,Integer joMasterID) throws JobException {
		this.joMasterDetails = itsJobService.getSingleJobDetails(jobNumber,joMasterID);
	}
	
	public String getCutomerRxName() {
		return cutomerRxName;
	}

	public void setCutomerRxName(String cutomerRxName) {
		this.cutomerRxName = cutomerRxName;
	}
	
	/**
	 * The method is controller for job journal grid data. This method is used to handle the grid data requests.
	 * @param theJobNumber {@link String}
	 * @param theType {@link String}
	 * @param theOperation {@link String}
	 * @param theJournalNote {@link String}
	 * @param theStatus {@link Short}
	 * @param theJoJournalId  {@link Integer}
	 * @param theSession {@link HttpSession}
	 * @return {@link CustomResponse} for response body.
	 * @throws JobException 
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	@RequestMapping(value = "journal_grid", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAllJournals(
						@RequestParam(value="jobNumber", required=false) String theJobNumber,
						@RequestParam(value = "type", required = true) String theType,
						@RequestParam(value = "oper", required = false) String theOperation,
						@RequestParam(value = "journalNote", required = false) String theJournalNote,
						@RequestParam(value = "journalStatus", required = false) Short theStatus,
						@RequestParam(value = "joJournalId", required = false) Integer theJoJournalId,
						@RequestParam(value="joMasterID", required=false) Integer joMasterID,
						HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		try{
			itsLogger.info("Status:"+theStatus);
			if(theType.equals("fetch")) {
				itsLogger.debug("Received request to get all Employee");
				List<Jojournal> aJobJournal = itsJobService.getAllJobJournals(joMasterID);
				CustomResponse response = new CustomResponse();
				response.setRows(aJobJournal);
				response.setRecords( String.valueOf(aJobJournal.size()) );
				return response; 
			} else if (theType.equals("manipulate") && theOperation.equals("add")) {
				UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				Jojournal aJojournal = new Jojournal();
				aJojournal.setJoMasterId(joMasterID);
				aJojournal.setJournalById(aUserBean.getUserId());
				aJojournal.setJournalNote(theJournalNote);
				aJojournal.setJournalStatus(theStatus);
				Integer aResult = itsJobService.addJobJournalEntry(aJojournal);
			} else if (theType.equals("manipulate") && theOperation.equals("del")) {
				Jojournal aJojournal = new Jojournal();
				aJojournal.setJournalStatus(theStatus);
				aJojournal.setJoJournalId(theJoJournalId);
				Integer aResult = itsJobService.deleteJobJournalEntry(aJojournal);
			} else if (theType.equals("manipulate") && theOperation.equals("edit")) {
				UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				Jojournal aJojournal = new Jojournal();
				aJojournal.setJoMasterId(joMasterID);
				aJojournal.setJournalById(aUserBean.getUserId());
				aJojournal.setJournalNote(theJournalNote);
				aJojournal.setJournalStatus(theStatus);
				aJojournal.setJoJournalId(theJoJournalId);
				Integer aResult = itsJobService.editJobJournalEntry(aJojournal);
			}
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber,theJoJournalId:</b>"+theJobNumber+","+theJoJournalId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "journalHeaderIcon", method = RequestMethod.POST)
	public @ResponseBody String getJournalHeaderIcon(@RequestParam(value="joMasterID", required=false) String theJobNumber,
						HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		String returnResponse = "";
		try{
				itsLogger.debug("Received request to get all Employee");
				returnResponse = itsJobService.getJobJournalsStatus(theJobNumber);
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return returnResponse;
	}
	
	@RequestMapping(value = "/saveAppletLocalFileURL", method = RequestMethod.POST)
	public @ResponseBody Boolean updateQuotesCategory(
			@RequestParam(value = "urlValue", required = true) String urlValue,
			@RequestParam(value = "jobNumber", required = true) String jobNumber,
			HttpServletResponse theResponse, HttpSession theSession) throws JobException, UserException {
		itsLogger.info(" urlValue = "+urlValue+" || jobNumber = "+jobNumber);
		int userId = (Integer) theSession.getAttribute(SessionConstants.SALES_USERID);
//		TsUserLogin aLogin = itsUserService.getSingleUserDetails(userId);
//		aLogin.setAppletLocalUrl(urlValue);
//		itsLogger.info(" urlValue = "+urlValue);
//		TsUserLogin aUser = itsUserService.updateUser(aLogin);
		itsUserService.updateJoWizardAppletData(jobNumber, userId, urlValue);
		itsLogger.info(" urlValue = "+urlValue+ " || "+jobNumber);
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
