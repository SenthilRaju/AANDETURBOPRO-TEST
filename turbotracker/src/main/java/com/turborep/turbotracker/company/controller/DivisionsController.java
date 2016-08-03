/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.company.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Codivisionposting;
import com.turborep.turbotracker.company.service.DivisionsService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.search.service.SearchServiceInterface;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.exception.VendorException;

@Controller
@RequestMapping("/company")
public class DivisionsController {

	protected static Logger logger = Logger
			.getLogger(DivisionsController.class);

	@Resource(name = "DivisionsService")
	private DivisionsService divisionsService;

	@Resource(name = "userLoginService")
	private UserService itsUserService;

	@Resource(name = "SearchServices")
	private SearchServiceInterface itsSearchServices;

	/**
	 * Method to get the list of "Chart of Accounts" into the data grid.
	 * 
	 * @param page
	 * @param rows
	 * @param sidx
	 * @param sord
	 * @param theResponse
	 * @return {@link CustomResponse}
	 * @throws IOException
	 * @throws MessagingException 
	 */
	@RequestMapping(value = "/getListOfDivisions", method = RequestMethod.GET)
	public @ResponseBody CustomResponse getloginPage(@RequestParam(value = "page", required = false) Integer page,
														@RequestParam(value = "rows", required = false) Integer rows,
														@RequestParam(value = "sidx", required = false) String sidx,
														@RequestParam(value = "sord", required = false) String sord,
														HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		int from, to;
		CustomResponse response = new CustomResponse();

		BigInteger aTotalCount = null;
		try {
			aTotalCount = divisionsService.getListOfDivisionsCount();
			to = (rows * page);
			from = to - rows;
			if (to > aTotalCount.intValue())
				to = aTotalCount.intValue();
			List<Codivision> accounts = divisionsService.getListOfDivisions(
					from, rows);
			response.setRows(accounts);
			response.setRecords(String.valueOf(accounts.size()));
			response.setPage(page);
			response.setTotal((int) Math.ceil((double) aTotalCount.intValue()
					/ (double) rows));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> getListOfDivisions","Division",e,session,request);
		}
		return response;
	}

	@RequestMapping(value = "/divisionList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getDivisions(@RequestParam(value = "rows", required = false) Integer theRows,
														@RequestParam(value = "sidx", required = false) String theSidx,
														@RequestParam(value = "sord", required = false) String theSord,
														 HttpServletResponse response,HttpServletRequest request,HttpSession session)throws IOException, CompanyException, UserException, MessagingException {

		CustomResponse aResponse = null;
		try {
			List<Codivision> aChartsAccountsList = divisionsService
					.getDivisionList();
			aResponse = new CustomResponse();
			aResponse.setRows(aChartsAccountsList);
			aResponse.setRecords(String.valueOf(aChartsAccountsList.size()));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> getListOfDivisions","Division",e,session,request);
		}
		return aResponse;
	}

	@RequestMapping(value = "/getDivisions", method = RequestMethod.GET)
	public @ResponseBody Codivision getDivisions(@RequestParam(value = "CoDivisionID", required = false) Integer theCoAccountID,
			HttpServletRequest request,HttpSession session, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException {
		Codivision aCodivision = null;
		try {
			aCodivision = new Codivision();
			if (theCoAccountID != null) {
				aCodivision = divisionsService.getDivisions(theCoAccountID);
			}
		} catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> getListOfDivisions","Division",e,session,request);
		}
		return aCodivision;
	}

	@RequestMapping(value = "/addNewDivisions", method = RequestMethod.POST)
	public @ResponseBody boolean addNewDivision(@RequestParam(value = "coDivisionName", required = false) String theDescription,
			@RequestParam(value = "coDivisionCode", required = false) String theCode,
			
			@RequestParam(value = "inActiveName", required = false) boolean theInActive,
			@RequestParam(value = "useInvSeqNo", required = false) boolean theUseInvSeqNo,
			@RequestParam(value = "inSeqNo", required = false) Integer theInvSeqNo,
			@RequestParam(value = "subAccountName", required = false) String theSubAccount,
			@RequestParam(value = "accountDisPercent", required = false) BigDecimal theAccountDisPercent,
			@RequestParam(value = "addressName1", required = false) String theAddress1,
			@RequestParam(value = "addressName2", required = false) String theAddress2,
			@RequestParam(value = "addressName3", required = false) String theAddress3,
			@RequestParam(value = "addressName4", required = false) String theAddress4,
			@RequestParam(value = "addressQuoteName", required = false) boolean theAddressQuote,
			@RequestParam(value = "additionalName1", required = false) String theAdditional1,
			@RequestParam(value = "additionalName2", required = false) String theAdditional2,
			@RequestParam(value = "additionalName3", required = false) String theAdditional3,
			HttpServletRequest request,HttpSession session, HttpServletResponse theResponse)throws ParseException, JobException, IOException, MessagingException {
		Codivision aCodivision = null;
		try {
			aCodivision = new Codivision();
			aCodivision.setCode(theCode);
			aCodivision.setDescription(theDescription);
			aCodivision.setInActive(theInActive);
			aCodivision.setUseInvoiceSeqNo(theUseInvSeqNo);
			aCodivision.setInvoiceSeqNo(theInvSeqNo);
			aCodivision.setSubAccount(theSubAccount);
			aCodivision.setAccountDistribution(theAccountDisPercent);
			aCodivision.setAddress1(theAddress1);
			aCodivision.setAddress2(theAddress2);
			aCodivision.setAddress3(theAddress3);
			aCodivision.setAddress4(theAddress4);
			aCodivision.setAddressQuote(theAddressQuote);
			aCodivision.setAdditional1(theAdditional1);
			aCodivision.setAdditional2(theAdditional2);
			aCodivision.setAdditional3(theAdditional3);
			divisionsService.addNewDivision(aCodivision);
		} catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>CoDevisionCode:</b> "+theCode,"Division",e,session,request);
		}
		return true;
	}

	@RequestMapping(value = "/deleteDivision", method = RequestMethod.POST)
	public @ResponseBody Boolean getloginPage(@RequestParam(value = "coDivisionID", required = false) Integer coDivisionID,
			HttpServletRequest request,HttpSession session,HttpServletResponse theResponse) throws IOException, MessagingException {
		Boolean isDeleted;
		try {
			isDeleted = divisionsService.deleteDivision(coDivisionID);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coDivisionID:</b> "+coDivisionID,"Division",e,session,request);
			return false;
		}
		return isDeleted;
	}

	@RequestMapping(value = "/updateDivision", method = RequestMethod.POST)
	public @ResponseBody boolean updateDivision(@RequestParam(value = "coDivisionName", required = false) String theDescription,
													@RequestParam(value = "coDivisionCode", required = false) String theCode,
													@RequestParam(value = "coDivisionIdName", required = false) int theCoDivisionId,
													@RequestParam(value = "inActiveName", required = false) boolean theInActive,
													@RequestParam(value = "useInvSeqNo", required = false) boolean theUseInvSeqNo,
													@RequestParam(value = "inSeqNo", required = false) Integer theInvSeqNo,
													@RequestParam(value = "subAccountName", required = false) String theSubAccount,
													@RequestParam(value = "accountDisPercent", required = false) BigDecimal theAccountDisPercent,
													@RequestParam(value = "addressName1", required = false) String theAddress1,
													@RequestParam(value = "addressName2", required = false) String theAddress2,
													@RequestParam(value = "addressName3", required = false) String theAddress3,
													@RequestParam(value = "addressName4", required = false) String theAddress4,
													@RequestParam(value = "addressQuoteName", required = false) boolean theAddressQuote,
													@RequestParam(value = "additionalName1", required = false) String theAdditional1,
													@RequestParam(value = "additionalName2", required = false) String theAdditional2,
													@RequestParam(value = "additionalName3", required = false) String theAdditional3,
													HttpServletRequest request,HttpSession session, HttpServletResponse theResponse) throws ParseException, JobException, IOException, MessagingException {
		Codivision aCodivision = null;
		try {
			logger.info("Division ID: "+theCoDivisionId +" Code : "+theCode);
			aCodivision = new Codivision();
			aCodivision.setCoDivisionId(theCoDivisionId);
			aCodivision.setCode(theCode);
			aCodivision.setDescription(theDescription);
			aCodivision.setInActive(theInActive);
			aCodivision.setUseInvoiceSeqNo(theUseInvSeqNo);
			aCodivision.setInvoiceSeqNo(theInvSeqNo);
			aCodivision.setSubAccount(theSubAccount);
			aCodivision.setAccountDistribution(theAccountDisPercent);
			aCodivision.setAddress1(theAddress1);
			aCodivision.setAddress2(theAddress2);
			aCodivision.setAddress3(theAddress3);
			aCodivision.setAddress4(theAddress4);
			aCodivision.setAddressQuote(theAddressQuote);
			aCodivision.setAdditional1(theAdditional1);
			aCodivision.setAdditional2(theAdditional2);
			aCodivision.setAdditional3(theAdditional3);
			divisionsService.updateDivision(aCodivision);
		} catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coDivisionID:</b> "+theCoDivisionId,"Division",e,session,request);
		}
		return true;
	}

	@RequestMapping(value = "/divisionAlternateList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAlternateDivisions(
			@RequestParam(value = "coDivisionId", required = false) Integer coDivisionID,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpServletRequest request,HttpSession session, HttpServletResponse response) throws IOException, CompanyException, UserException, MessagingException {
		CustomResponse aResponse = null;
		try {
			List<Codivisionposting> aChartsAccountsList = divisionsService
					.getDivisionAlternateAccount(coDivisionID);
			aResponse = new CustomResponse();
			aResponse.setRows(aChartsAccountsList);
			aResponse.setRecords(String.valueOf(aChartsAccountsList.size()));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coDivisionID:</b> "+coDivisionID,"Division",e,session,request);
		}
		return aResponse;
	}

	@RequestMapping(value = "/manpulaterDivisionAccount", method = RequestMethod.POST)
	public @ResponseBody boolean manipulateDivisionAccount(@RequestParam(value = "coDivisionId", required = false) Integer coDivisionID,
															@RequestParam(value = "alternateID", required = false) Integer alternateID,
															@RequestParam(value = "coAccountPostId", required = false) Integer coAccountPostId,
															@RequestParam(value = "coDivisionPostingId", required = false) Integer coDivisionPostingId,
															@RequestParam(value = "originalID", required = false) Integer originalID,
															@RequestParam(value = "oper", required = false) String theOperation,
															HttpServletRequest request,HttpSession session, HttpServletResponse theResponse) throws IOException, VendorException, ParseException, CompanyException {

		Codivisionposting aCodivisionposting = new Codivisionposting();
		aCodivisionposting.setCoDivisionId(coDivisionID);
		aCodivisionposting.setCoAccountAlternateId(alternateID);
		aCodivisionposting.setCoAccountPostId(originalID);
		if (theOperation.equalsIgnoreCase("add")) {
			divisionsService.addCoPostingAccount(aCodivisionposting);
		}
		if (theOperation.equalsIgnoreCase("edit")) {
			aCodivisionposting.setCoDivisionPostingId(coDivisionPostingId);
			divisionsService.editCoPostingAccount(aCodivisionposting);
		}
		if (theOperation.equalsIgnoreCase("del")) {
			divisionsService.deleteCoDivisionPosting(coDivisionID);
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/coAccountDivisionList", method = RequestMethod.GET)
	public @ResponseBody List<?> getCoAccountList(@RequestParam("term") String theProductNameWithCode,
			HttpServletRequest request,HttpSession session,HttpServletResponse theResponse) throws CompanyException, IOException, MessagingException {
		logger.debug("Received request to get search Accounts Lists");
		ArrayList<AutoCompleteBean> aCoAccountList = null;
		try {
			aCoAccountList = (ArrayList<AutoCompleteBean>) divisionsService
					.getCoAccountList(theProductNameWithCode);
		} catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coDivisionCode:</b> "+theProductNameWithCode,"Division",e,session,request);
		}
		return aCoAccountList;
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