/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.employee.controller.EmployeeController;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.home.exception.WeatherException;
import com.turborep.turbotracker.json.WeatherForecastBean;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/homecontroller")
public class HomeController {

	protected static Logger itsLogger = Logger.getLogger(EmployeeController.class);
	
	@Resource(name="userLoginService")
	private UserService itsUserService;

	@RequestMapping(value = "/weather", method = RequestMethod.POST)
	public @ResponseBody List<WeatherForecastBean> getHomePage(ModelMap modal, HttpServletRequest therequest, HttpSession session, HttpServletResponse response) throws WeatherException, IOException, MessagingException {
		WeatherSer aWeatherSer = new WeatherSer();
		List<WeatherForecastBean> aWeatherList = null;
		TsUserLogin aTsUserLogin = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		aTsUserLogin = new TsUserLogin();
		try {
			aTsUserLogin = itsUserService.getUserZipCode(aUserBean.getUserId());
			if(aTsUserLogin.getUserzipcode() != null) {
				aWeatherSer.setZipCode(Long.parseLong(aTsUserLogin.getUserzipcode().toString()));
			}
			aWeatherList = aWeatherSer.getWeathrtForecast();
		} catch (WeatherException e) {
			sendTransactionException("<b>MethodName:</b>weather","Home",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} catch (ParseException e) {
			sendTransactionException("<b>MethodName:</b>weather","Home",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			response.sendError(501, e.getMessage());
		} catch (UserException e) {
			sendTransactionException("<b>MethodName:</b>weather","Home",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			response.sendError(501, e.getMessage());
		}
		return aWeatherList;
	}
	
	public void sendTransactionException(String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		UserBean aUserBean=null;
		TsUserSetting objtsusersettings=null;
		try{
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
		 StringWriter errors = new StringWriter();
		if(objtsusersettings.getItsmailYN()==1){
		Transactionmonitor transObj =new Transactionmonitor();
		 SendQuoteMail sendMail = new SendQuoteMail();
		 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
		 transObj.setTrackingId(trackingID);
		 transObj.setTimetotriggerd(new Date());
		 transObj.setJobStatus(jobstatus);
		 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
		 e.printStackTrace(new PrintWriter(errors));
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
