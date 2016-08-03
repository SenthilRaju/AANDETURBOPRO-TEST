package com.turborep.turbotracker.quickbooks.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.quickbooks.dao.QbConfig;
import com.turborep.turbotracker.quickbooks.service.QuickBooksService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
public class QBSettingsController {

	protected static Logger logger = Logger.getLogger("controller");
	
	@Resource(name = "qbService")
	private QuickBooksService itsQuickBooksService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@RequestMapping(value="/qbsettings",method = RequestMethod.GET)
	public String qbSettings(HttpSession theSession,Model model) {
		UserBean theUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);		
		if(theUserBean != null)
		{
			QbConfig theQbpo = itsQuickBooksService.getQbConfigBean(theUserBean.getUserName());	
			if(theQbpo != null)
				model.addAttribute("theQbpo", theQbpo);
			return "qbsettings";
		}
		return "login";
	}

	@RequestMapping(value="/addQBSettings",method = RequestMethod.GET)
	public @ResponseBody String getJobStatus (@RequestParam(value = "qbuserName", required = false) String aQbuserName,
											@RequestParam(value = "qbpassword", required = false) String aQbpassword,
											@RequestParam(value = "key", required = false) String aKey,
											@RequestParam(value = "host", required = false) String aHost, 
											@RequestParam(value = "enable", required = false) boolean aEnabled, 
											HttpServletResponse theResponse,HttpSession theSession,HttpServletRequest therequest) throws ParseException, IOException, MessagingException {
		UserBean theUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);		
		String message = "error";
		String itsEnabled = "0";
		if(theUserBean != null)
		{
			try {
				if(aEnabled)
					itsEnabled = "1";
				QbConfig theQbpo = new QbConfig(aQbuserName,aQbpassword,aKey,aHost,theUserBean.getUserName(),itsEnabled);
				Integer aQbID  =  itsQuickBooksService.getQbConfig(theUserBean.getUserName());
				if(aQbID == null)
				itsQuickBooksService.saveQB(theQbpo);
				else
				{
					theQbpo.setQbID(aQbID);
					itsQuickBooksService.updateQB(theQbpo);		
				}
				message = "success";
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				sendTransactionException("<b>aQbuserName:</b>"+aQbuserName ,"QBSettingsController",excep,theSession,therequest);
				return message;
			}
		}
		return message;
	}
	
	@RequestMapping(value="/qbsetting",method = RequestMethod.GET)
	public @ResponseBody QbConfig getQBsettings(HttpSession theSession) {
		UserBean theUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);		
		QbConfig theQbpo =null;
		if(theUserBean != null)
		{
			theQbpo = itsQuickBooksService.getQbConfigBean(theUserBean.getUserName());	
		}
		return theQbpo;
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
			userService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	
}