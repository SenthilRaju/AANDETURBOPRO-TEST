/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish
 */
package com.turborep.turbotracker.company.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.company.service.PaymentsTermsService;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
public class PaymentTermsController {

	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	protected static Logger logger = Logger
			.getLogger(PaymentTermsController.class);

	@Autowired
	@Resource(name="paymentTermsService")
	PaymentsTermsService paymentsTermsService;

	@RequestMapping(value = "/paymentterms", method = RequestMethod.GET)
	public String companyjournals(ModelMap modal, HttpServletRequest request) {
		String aPage = "paymentterms";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}
		
	@RequestMapping(value = "/getcuTerms", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getcuTerms(HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			List<?> getcuTerms = paymentsTermsService.getcuTerms();
			response.setRows(getcuTerms);
		} catch (CustomerException e) {
			sendTransactionException("<b>MethodName:</b>getcuTerms","Company",e,session,therequest);
			logger.error(e.getMessage(), e);
		}
		return response;
	}

	@RequestMapping(value = "/editTerms", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse editTerms(
			@RequestParam(value = "cuTermsId", required = true) Integer cuTermsId,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "active", required = false) boolean inActive,
			@RequestParam(value = "dueDay", required = false) Integer dueDays,
			@RequestParam(value = "discountDay", required = false) Integer discountDays,
			@RequestParam(value = "dueDayoption", required = false) boolean dueOnDay,
			@RequestParam(value = "discountDayoption", required = false) boolean discOnDay,
			@RequestParam(value = "global", required = false) boolean isGlobal,
			@RequestParam(value = "discountPercent", required = false) BigDecimal discountPercent,
			@RequestParam(value = "pickTicket1", required = false) String pickTicketNote1,
			@RequestParam(value = "orderNote", required = false) String orderNote,
			@RequestParam(value = "pickTicket2", required = false) String pickTicketNote2,
			@RequestParam(value = "pickTicket3", required = false) String pickTicketNote3,
			@RequestParam(value = "pickTicket4", required = false) String pickTicketNote4,
			@RequestParam(value = "pickTicket5", required = false) String pickTicketNote5,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			if(dueDays == null)
				dueDays = 0;
			if(discountDays == null)
				discountDays = 0;
			paymentsTermsService.updateTerms(cuTermsId,description,inActive,dueDays,
					discountDays,discountPercent,dueOnDay,
					discOnDay,orderNote,pickTicketNote1, 
					pickTicketNote2,pickTicketNote3,pickTicketNote4,
					pickTicketNote5, isGlobal);
		} catch (CustomerException e) {
			sendTransactionException("<b>cuTermsId:</b>"+cuTermsId,"Company",e,session,therequest);
			logger.error(e.getMessage(), e);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/deleteTerms", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse deleteTerms(
			@RequestParam(value = "cuTermsId", required = true) Integer cuTermsId,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			paymentsTermsService.deleteTerms(cuTermsId);
		} catch (CustomerException e) {
			sendTransactionException("<b>cuTermsId:</b>"+cuTermsId,"Company",e,session,therequest);
			logger.error(e.getMessage(), e);
		}
		return response;
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