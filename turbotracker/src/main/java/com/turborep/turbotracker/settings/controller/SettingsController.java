package com.turborep.turbotracker.settings.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
public class SettingsController {

Logger logger = Logger.getLogger(ShipViaController.class);
@Resource(name = "userLoginService")
private UserService userService;
	
	
@RequestMapping(value = "/companyLogo", method = RequestMethod.GET)
public  @ResponseBody void  getShipDetails (HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
	TsUserSetting aUserSetting = null;
	try {
	aUserSetting = userService.getSingleUserSettingsDetails(1);
	Blob blob = aUserSetting.getCompanyLogo();
	byte[] image = blob.getBytes(1, (int) blob.length());
	Image aImage = Image.getInstance(image);
	OutputStream oImage;
	theResponse.setContentType("image/gif");   
	oImage=theResponse.getOutputStream();    
	oImage.write(image);     
	oImage.flush();         
	oImage.close();
	} catch (UserException e) {
		logger.error(e.getMessage(), e);
		theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		sendTransactionException("<b>getShipDetails:</b>","SettingsController",e,theSession,therequest);
	} catch (SQLException e) {
		logger.error(e.getMessage(), e);
		theResponse.sendError(e.getErrorCode(), e.getMessage());
		sendTransactionException("<b>getShipDetails:</b>","SettingsController",e,theSession,therequest);
	} catch (BadElementException e) {
		logger.error(e.getMessage(), e);
		theResponse.sendError(501, e.getMessage());
		sendTransactionException("<b>getShipDetails:</b>","SettingsController",e,theSession,therequest);
	}
	 
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
