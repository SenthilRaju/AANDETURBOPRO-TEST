package com.turborep.turbotracker.settings.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.settings.service.ShipViaService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Veshipvia;

@Controller
@RequestMapping(value ="/shipviaController")
public class ShipViaController {

	Logger logger = Logger.getLogger(ShipViaController.class);
	
	@Resource(name="shipViaService")
	private ShipViaService itsShipViaService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@RequestMapping(value = "getShipDetails", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getShipDetails (HttpSession theSession, HttpServletResponse theResponce,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		try{
			List<?> aVeShipVia = itsShipViaService.getVeShipVia();
			aResponse = new CustomResponse();
			aResponse.setRows(aVeShipVia);
		}catch (UserException e) {
			logger.error(e.getMessage(), e);
			theResponce.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>getShipDetails:</b>","ShipViaController",e,theSession,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value="/saveShipViaDetails", method = RequestMethod.POST)
	public @ResponseBody Veshipvia updateShipViaDetails(@RequestParam(value = "shipVia_ID", required = false) Integer theVeShipID,
												  @RequestParam(value = "Description_ID", required = false) String theDescription,
												  @RequestParam(value = "trackingUrl", required = false) String theTrackUrl,
												  @RequestParam(value = "trackingPrefix", required = false) String theTrackPrefix, 
												  @RequestParam(value = "trackingSuffix", required = false) String theTrackSuffix, 
												  HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest) throws UserException, IOException, MessagingException{
		
		Veshipvia aVeshipvia = new Veshipvia();
		Veshipvia aVeshipviaReturn = null;
		try{
			aVeshipvia.setDescription(theDescription);
			aVeshipvia.setTrackUrl(theTrackUrl);
			aVeshipvia.setTrackPrefix(theTrackPrefix);
			aVeshipvia.setTrackSuffix(theTrackSuffix);
			if(theVeShipID != null){
				aVeshipvia.setVeShipViaId(theVeShipID);
				aVeshipviaReturn = itsShipViaService.updateShipViaDetails(aVeshipvia);
			}else{
				aVeshipvia.setInActive(false);
				aVeshipviaReturn = itsShipViaService.addShipViaDetails(aVeshipvia);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theVeShipID:</b>"+theVeShipID ,"ShipViaController",e,theSession,therequest);
		}
		return aVeshipviaReturn;
	}
	
	@RequestMapping(value = "/deleteveshipdetail", method = RequestMethod.POST)
	public @ResponseBody Boolean getloginPage(@RequestParam(value = "veShipViaId", required = false) Integer aveShipViaId,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, UserException, MessagingException {
		Boolean isDeleted;
		try {
			isDeleted = itsShipViaService.deleteveshipdetail(aveShipViaId);
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>aveShipViaId:</b>"+aveShipViaId ,"ShipViaController",e,theSession,therequest);
			return false;
		}
		return isDeleted;
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
