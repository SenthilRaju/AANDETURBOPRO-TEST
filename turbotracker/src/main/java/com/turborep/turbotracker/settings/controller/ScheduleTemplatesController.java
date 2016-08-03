package com.turborep.turbotracker.settings.controller;

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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Joschedaccessory;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempcolumntype;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomGenericResponse;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.settings.exception.SettingsException;
import com.turborep.turbotracker.settings.service.ShedTempService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping(value ="/scheduleTempController")
public class ScheduleTemplatesController {

	Logger logger = Logger.getLogger(ScheduleTemplatesController.class);
	
	@Resource(name="schedTempService")
	private ShedTempService itsSchedTempService;
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@RequestMapping(value = "/joscheduleDiscription", method = RequestMethod.GET)
	public  @ResponseBody CustomResponse  getScheduleDiscription (HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		try {
			List<Joschedtempheader>	aJoschedtempheader = itsSchedTempService.getSchedDescription();
			aResponse = new CustomResponse();
			aResponse.setRows(aJoschedtempheader);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>joscheduleDiscription:</b>","ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aResponse;
	}
	
	@RequestMapping(value = "/joSchedManufacturer", method = RequestMethod.GET)
	public  @ResponseBody List<?>  getManufacturer (@RequestParam("term") String aKeyword,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aManufacturer = null;
		try {
				aManufacturer = (ArrayList<AutoCompleteBean>) itsSchedTempService.getManufacturer(aKeyword);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>aKeyword:</b>"+aKeyword,"ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aManufacturer;
	}
	
	@RequestMapping(value = "/getMasterName", method = RequestMethod.POST)
	public  @ResponseBody String getMasterName (@RequestParam("aRxMasterId") String aRxMasterId,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		String aName = null;
		try {
				aName = itsRolodexService.getPhone(Integer.parseInt(aRxMasterId)).getName();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>aRxMasterId:</b>"+aRxMasterId,"ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aName;
	}
	
	@RequestMapping(value = "/getScheduleDetails", method = RequestMethod.POST)
	public  @ResponseBody CustomResponse getScheduleDetails (@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="schedTempHeaderID", required=false) String theschedTempHeaderID,HttpSession theSession,
			HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		
		try {
			int aTotalCount = itsSchedTempService.getScheduleDetails(Integer.parseInt(theschedTempHeaderID)).size();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<Joschedtempcolumn>	aJoschedtempheader = itsSchedTempService.getScheduleDetails(aFrom, aTo,Integer.parseInt(theschedTempHeaderID));
			if(aJoschedtempheader != null)
			{
				aResponse = new CustomResponse();
				aResponse.setRows(aJoschedtempheader);
				aResponse.setRecords(String.valueOf(aJoschedtempheader.size()));
				aResponse.setPage(thePage);
				aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
			}
			
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theschedTempHeaderID:</b>"+theschedTempHeaderID,"ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aResponse;
	}
	
	@RequestMapping(value = "/getScheduleType", method = RequestMethod.GET)
	public  @ResponseBody List<Joschedtempcolumntype>  getScheduleType (HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		List<Joschedtempcolumntype>	aJoschedtempcolumntype = null;
		try {
				aJoschedtempcolumntype = itsSchedTempService.getScheduleType();
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>getScheduleType:</b>","ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aJoschedtempcolumntype;
	}
	
	@RequestMapping(value = "/manpulaterScheduleTemplate", method = RequestMethod.POST)
	public  @ResponseBody void  manpulaterProductQuotes (@RequestParam(value="oper", required=false) String theOperation,
			@RequestParam(value="joSchedTempColumnId", required=false) Integer theJoSchedTempColumnId,
			@RequestParam(value="displayText", required=false) String theDisplayText,
			@RequestParam(value="displayWidth", required=false) Integer theDisplayWidth,
			@RequestParam(value="printText", required=false) String thePrintText,
			@RequestParam(value="printWidth", required=false) Integer thePrintWidth,
			@RequestParam(value="inactive", required=false) Boolean theInactive,
			@RequestParam(value="copyDefaults", required=false) Boolean theCopyDefaults,
			@RequestParam(value="joSchedTempHeaderId", required=false) Integer theHeaderId,
			@RequestParam(value="scheduleDescription", required=false) Integer theTypeId,
			@RequestParam(value="importToOrder", required=false) Integer theImportToOrder,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		try {
			if(theOperation.equals("del")){
		itsSchedTempService.deleteJoSchedTempColumn(theJoSchedTempColumnId);
			}
			else if(theOperation.equals("edit")){
				itsSchedTempService.editJoSchedTempColumn(theJoSchedTempColumnId,theDisplayText,theDisplayWidth,thePrintText,thePrintWidth,theInactive
						,theCopyDefaults,theImportToOrder,theTypeId);
			}
			else if(theOperation.equals("add")){
				Joschedtempcolumn theJoschedtempcolumn = new Joschedtempcolumn();
				Integer aCount = itsSchedTempService.getScheduleCount(theHeaderId)+1;
				System.out.println(aCount);
				theJoschedtempcolumn.setColumnNumber(aCount);
				theJoschedtempcolumn.setDisplayText(theDisplayText);
				theJoschedtempcolumn.setDisplayWidth(theDisplayWidth);
				theJoschedtempcolumn.setPrintText(thePrintText);
				theJoschedtempcolumn.setPrintWidth(thePrintWidth);
				theJoschedtempcolumn.setImportToOrder(theImportToOrder);
				theJoschedtempcolumn.setInactive(theInactive);
				theJoschedtempcolumn.setCopyDefaults(theCopyDefaults);
				theJoschedtempcolumn.setJoSchedTempHeaderId(theHeaderId);
				theJoschedtempcolumn.setJoSchedTempColumnTypeId(theTypeId);
				itsSchedTempService.addJoSchedTempColumn(theJoschedtempcolumn);
			}
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>theJoSchedTempColumnId:</b>"+theJoSchedTempColumnId,"ScheduleTemplatesController",e,theSession,therequest);
		} 
	}
	
	@RequestMapping(value = "/addTemplateDescription", method = RequestMethod.POST)
	public  @ResponseBody CustomGenericResponse  addTemplateDescription (
			@RequestParam(value="aCode", required=false) String aCode,
			@RequestParam(value="aDescription", required=false) String aDescription,
			@RequestParam(value="aManufacturer", required=false) Integer aManufacturer,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		CustomGenericResponse	aCustomGenericResponse = new CustomGenericResponse();
		try {
			createTempHeader(aCode, aDescription, aManufacturer,
					aCustomGenericResponse);
		
			
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>aCode:</b>"+aCode,"ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aCustomGenericResponse;
	}

	private void createTempHeader(String aCode, String aDescription,
			Integer aManufacturer, CustomGenericResponse aCustomGenericResponse)
			throws SettingsException {
		if(!itsSchedTempService.checkProduct(aCode))
		{
			Joschedtempheader theJoschedtempheader = new Joschedtempheader();
			theJoschedtempheader.setDescription(aCode);
			theJoschedtempheader.setProduct(aDescription);
			theJoschedtempheader.setRxManufacturerId(aManufacturer);
			theJoschedtempheader.setPrintLandScape(false);
				itsSchedTempService.addTemplateDescription(theJoschedtempheader);
				aCustomGenericResponse.setSuccess(true);
		}
		else
		{
			aCustomGenericResponse.setMessage("Product Already Exist");
			aCustomGenericResponse.setSuccess(false);
		}
	}
	
	@RequestMapping(value = "/copyTemplate", method = RequestMethod.POST)
	public  @ResponseBody CustomGenericResponse  copyTemplate (
			@RequestParam(value="rxMasterId", required=false) String aRxMasterId ,
			@RequestParam(value="vendorName", required=false) String aVendorName,
			@RequestParam(value="code", required=false) String aCode,
			@RequestParam(value="aDescription", required=false) String aDescription,
			@RequestParam(value="joSchedTempHeaderId", required=false) Integer aJoSchedTempHeaderId,
			HttpSession theSession, HttpServletResponse theResponse ,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomGenericResponse	aCustomGenericResponse = new CustomGenericResponse();
		try {
			createTempHeader(aCode, aDescription,Integer.parseInt(aRxMasterId),
					aCustomGenericResponse);
			Integer aHeaderId = itsSchedTempService.getHeaderId(aCode);
			if(aHeaderId != null)
			{
				List<Joschedtempcolumn>	aJoschedtempcolumn = 	itsSchedTempService.getScheduleDetails(aJoSchedTempHeaderId);
				for(Joschedtempcolumn aJoschedtemp : aJoschedtempcolumn)
				{
					aJoschedtemp.setJoSchedTempHeaderId(aHeaderId);
					itsSchedTempService.saveJoschedtempcolumn(aJoschedtemp);
				}
			}
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>aRxMasterId:</b>"+aRxMasterId,"ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aCustomGenericResponse;
	}
	
	@RequestMapping(value = "/getAccessories", method = RequestMethod.POST)
	public  @ResponseBody CustomResponse  getAccessories (@RequestParam(value="joSchedTempColumnId", required=false) Integer aJoSchedTempColumnId ,HttpSession theSession, HttpServletResponse theResponse ,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		try {
			List<Joschedaccessory>	aJoschedaccessory = itsSchedTempService.getAccessories(aJoSchedTempColumnId);
			aResponse = new CustomResponse();
			aResponse.setRows(aJoschedaccessory);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>aJoSchedTempColumnId:</b>"+aJoSchedTempColumnId,"ScheduleTemplatesController",e,theSession,therequest);
		} 
		return aResponse;
	}
	
	@RequestMapping(value = "/manpulaterAccessories", method = RequestMethod.POST)
	public  @ResponseBody void  manpulaterAccessories (@RequestParam(value="oper", required=false) String theOperation,
			@RequestParam(value="joSchedAccessoryId", required=false) Integer theJoSchedAccessoryId,
			@RequestParam(value="code", required=false) String theCode,
			@RequestParam(value="joSchedTempColumnId", required=false) Integer theJoSchedTempColumnId,
			@RequestParam(value="description", required=false) String theDescription,
			HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		try {
			if(theOperation.equals("del")){
		itsSchedTempService.deleteAccessories(theJoSchedAccessoryId);
			}
			else if(theOperation.equals("edit")){
				itsSchedTempService.editAccessories(theJoSchedAccessoryId,theCode,theDescription);
			}
			else if(theOperation.equals("add")){
				Joschedaccessory theJoschedaccessory = new Joschedaccessory();
				theJoschedaccessory.setCode(theCode);
				theJoschedaccessory.setDescription(theDescription);
				theJoschedaccessory.setJoSchedTempColumnId(theJoSchedTempColumnId);
				itsSchedTempService.saveAccessories(theJoschedaccessory);
				
			}
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theCode:</b>"+theCode ,"ScheduleTemplatesController",e,theSession,therequest);
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
