package com.turborep.turbotracker.settings.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.job.dao.Joschedulemodel;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomGenericResponse;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.settings.exception.SettingsException;
import com.turborep.turbotracker.settings.service.ModelMaintenanceService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
public class ModelMaintinenceController {

	Logger logger = Logger.getLogger(ModelMaintinenceController.class);
	
	@Resource(name="modelMaintenanceService")
	private ModelMaintenanceService itsModelMaintenanceService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@RequestMapping(value = "/modelDetails", method = RequestMethod.GET)
	public String getModelDetails(ModelMap modal, HttpServletRequest request,@RequestParam("joSchedTempHeaderId") Integer joSchedTempHeaderId) {
		String aPage = "model_details";
		modal.addAttribute("joSchedTempHeaderId",joSchedTempHeaderId);
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message", "User Name Or Password are Invalid.");
		}
		return aPage;
	}
	
	
	@RequestMapping(value="/modelMaintenanceController/getColumnName", method = RequestMethod.GET)
	public @ResponseBody List<?> getScheduledColumnName(@RequestParam(value="tempHeaderID", required=false) Integer theSchelduleModelID, HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<Joschedtempcolumn> aJoschedtempcolumns = null;
		try{
			aJoschedtempcolumns =  (ArrayList<Joschedtempcolumn>) itsModelMaintenanceService.getColumnName(theSchelduleModelID);
		}catch (SettingsException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>theSchelduleModelID:</b>"+theSchelduleModelID,"ModelMaintinenceController",e,session,therequest);
		}
		return aJoschedtempcolumns;
	}
	
	@RequestMapping(value = "/modelMaintenanceController/joscheduleDiscription", method = RequestMethod.GET)
	public  @ResponseBody CustomResponse  getScheduleDiscription (HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		try {

			List<Map<String,Object>> aSchedule = new ArrayList<Map<String,Object>>();
			List<?> aJoschedtempheader = itsModelMaintenanceService.getSchedDescription();
			Iterator<?> aIterator = aJoschedtempheader.iterator();
			while(aIterator.hasNext()) {
				Map<String,Object> aObject = new HashMap<String, Object>();
				Object[] aObj = (Object[])aIterator.next();
					aObject.put("joSchedTempHeaderId", aObj[0]); 
					aObject.put("description", aObj[1]); 
					aObject.put("product", aObj[2]); 
					aObject.put("name", aObj[3]); 
				aSchedule.add(aObject);
			}
			aResponse = new CustomResponse();
			aResponse.setRows(aSchedule);
			
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>joscheduleDiscription:</b>","ModelMaintinenceController",e,theSession,therequest);
		} 
		return aResponse;
	}
	
	@RequestMapping(value = "/modelMaintenanceController/getModelMaintenaceList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getModelMaintenaceList(@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="searchString", required=false) String theSearch,
			@RequestParam(value="rows", required=false) Integer theRows,@RequestParam(value="joSchedTempHeaderID", required=false) Integer theJoSchedTempHeaderID,
			@RequestParam(value="theParameters", required=false) String theParameters,
			@RequestParam(value="theKeys[]", required=false) String[] theKeys,HttpSession theSession,
			HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException, MessagingException{
		CustomResponse aResponse = new CustomResponse();
		try{
			
			int aTotalCount = itsModelMaintenanceService.getModelMaintenaceList(theJoSchedTempHeaderID,theParameters,theSearch).size();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<Map<String,Object>> aSchedule = new ArrayList<Map<String,Object>>();
			List<?> getModelMaintenace = itsModelMaintenanceService.getModelMaintenaceList(theJoSchedTempHeaderID,theParameters,aFrom,aTo,theSearch);
			Iterator<?> aIterator = getModelMaintenace.iterator();
			while(aIterator.hasNext()) {
				Map<String,Object> aObject = new HashMap<String, Object>();
				Object[] aObj = (Object[])aIterator.next();
				for(int index = 0;index < theKeys.length; index++)
				{
					aObject.put(theKeys[index], aObj[index]); 
					
				}
				aSchedule.add(aObject);
			}
			aResponse.setRows(aSchedule);
			aResponse.setRecords(String.valueOf(getModelMaintenace.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
			
		}catch (SettingsException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>theJoSchedTempHeaderID:</b>"+theJoSchedTempHeaderID,"ModelMaintinenceController",e,theSession,therequest);
		}
		return aResponse;
	}
	
	
	@RequestMapping(value = "/modelMaintenanceController/getProductCategory",method = RequestMethod.GET)
	public @ResponseBody ArrayList<AutoCompleteBean> getProductCategory(@RequestParam("term") String aKeyword,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws JobException, IOException, MessagingException{

		ArrayList<AutoCompleteBean> aManufacturer = null;
		try {
				aManufacturer = (ArrayList<AutoCompleteBean>) itsModelMaintenanceService.getProductCategory(aKeyword);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>aKeyword:</b>"+aKeyword,"ModelMaintinenceController",e,theSession,therequest);
		} 
		return aManufacturer;
	}
	
	@RequestMapping(value = "/modelMaintenanceController/getItemCode",method = RequestMethod.GET)
	public @ResponseBody ArrayList<AutoCompleteBean> getItemCode(@RequestParam("term") String aKeyword,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws JobException, IOException, MessagingException{

		ArrayList<AutoCompleteBean> aManufacturer = null;
		try {
				aManufacturer = (ArrayList<AutoCompleteBean>) itsModelMaintenanceService.getItemCode(aKeyword);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>aKeyword:</b>"+aKeyword,"ModelMaintinenceController",e,theSession,therequest);
		} 
		return aManufacturer;
	}
	
	@RequestMapping(value="/modelMaintenanceController/manpulaterModel", method = RequestMethod.POST)
	public @ResponseBody Integer insertSubmittalSchedule(@RequestParam(value="joSchedTempHeaderID", required= false) Integer theSchedTempHeaderID,
			@RequestParam(value="joScheduleModelId", required= false) Integer theJoScheduleModelId,
															@RequestParam(value="col01", required = false) String col01,
															@RequestParam(value="col02", required = false) String col02,
															@RequestParam(value="col03", required = false) String col03,
															@RequestParam(value="col04", required = false) String col04,
															@RequestParam(value="col05", required = false) String col05,
															@RequestParam(value="col06", required = false) String col06,
															@RequestParam(value="col07", required = false) String col07,
															@RequestParam(value="col08", required = false) String col08,
															@RequestParam(value="col09", required = false) String col09,
															@RequestParam(value="col10", required = false) String col10,
															@RequestParam(value="col11", required = false) String col11,
															@RequestParam(value="col12", required = false) String col12,
															@RequestParam(value="col13", required = false) String col13,
															@RequestParam(value="col14", required = false) String col14,
															@RequestParam(value="col15", required = false) String col15,
															@RequestParam(value="col16", required = false) String col16,
															@RequestParam(value="col17", required = false) String col17,
															@RequestParam(value="col18", required = false) String col18,
															@RequestParam(value="col19", required = false) String col19,
															@RequestParam(value="col20", required = false) String col20,
															@RequestParam(value="oper", required = false) String operation,
															@RequestParam(value="model", required=false) String theModel,
															@RequestParam(value="webAddress", required=false) String theWebAddress,
															@RequestParam(value="prMasterId", required = false) Integer thePrMasterId,
															HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest) throws ParseException, IOException, MessagingException {
		Joschedulemodel aJoschedulemodel = new Joschedulemodel();
		try{
			aJoschedulemodel.setCol01(col01);
			aJoschedulemodel.setCol02(col02);
			aJoschedulemodel.setCol03(col03);
			aJoschedulemodel.setCol04(col04);
			aJoschedulemodel.setCol05(col05);
			aJoschedulemodel.setCol06(col06);
			aJoschedulemodel.setCol07(col07);
			aJoschedulemodel.setCol08(col08);
			aJoschedulemodel.setCol09(col09);
			aJoschedulemodel.setCol10(col10);
			aJoschedulemodel.setCol11(col11);
			aJoschedulemodel.setCol12(col12);
			aJoschedulemodel.setCol13(col13);
			aJoschedulemodel.setCol14(col14);
			aJoschedulemodel.setCol15(col15);
			aJoschedulemodel.setCol16(col16);
			aJoschedulemodel.setCol17(col17);
			aJoschedulemodel.setCol18(col18);
			aJoschedulemodel.setCol19(col19);
			aJoschedulemodel.setCol20(col20);
			UserBean aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
			aJoschedulemodel.setCreatedByID(aUserBean.getUserId());
			if(thePrMasterId != -1)
			aJoschedulemodel.setPrMasterId(thePrMasterId);
			aJoschedulemodel.setWebAddress(theWebAddress);
			aJoschedulemodel.setModelNo(theModel);
			aJoschedulemodel.setJoSchedTempHeaderId(theSchedTempHeaderID);
			if(operation.equals("add")){
				aJoschedulemodel.setCreatedOn(new Date());
				itsModelMaintenanceService.addScheduleModel(aJoschedulemodel);
			}else if(operation.equals("edit")){
				aJoschedulemodel.setJoScheduleModelId(theJoScheduleModelId);
				itsModelMaintenanceService.editScheduleModel(aJoschedulemodel);
			}
			else if(operation.equals("del")){
				itsModelMaintenanceService.deleteScheduleModel(theJoScheduleModelId);
			}
		}catch (SettingsException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>theSchedTempHeaderID:</b>"+theSchedTempHeaderID,"ModelMaintinenceController",e,theSession,therequest);
		}
		return 0;
	}
	
	@RequestMapping(value = "/modelMaintenanceController/saveproductCategory", method = RequestMethod.GET)
	public  @ResponseBody CustomGenericResponse  saveproductCategory (
			@RequestParam(value="joScheduleModelID", required=false) Integer aScheduleModelID,
			@RequestParam(value="prMasterId", required=false) Integer aPrMasterId,HttpSession theSession, HttpServletResponse theResponse ,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomGenericResponse	aCustomGenericResponse = new CustomGenericResponse();
		try {
			itsModelMaintenanceService.updateproductCategory(aScheduleModelID,aPrMasterId);
					aCustomGenericResponse.setSuccess(true);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>aScheduleModelID:</b>"+aScheduleModelID,"ModelMaintinenceController",e,theSession,therequest);
		} 
		return aCustomGenericResponse;
	}
	
	@RequestMapping(value = "/modelMaintenanceController/getProductCategoryName", method = RequestMethod.GET)
	public  @ResponseBody String  getproductCategory (
			@RequestParam(value="joScheduleModelID", required=false) Integer joScheduleModelID
			,HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest ) throws IOException, MessagingException {
		String	aName = null;
		try {
			aName = itsModelMaintenanceService.getProductCategory(joScheduleModelID);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>joScheduleModelID:</b>"+joScheduleModelID,"ModelMaintinenceController",e,theSession,therequest);
		} 
		return aName;
	}
	
	@RequestMapping(value = "/modelMaintenanceController/getManufacurersLink", method = RequestMethod.GET)
	public  @ResponseBody String  getManufacurersLink (
			@RequestParam(value="joSchedTempHeaderId", required=false) Integer joSchedTempHeaderId
			,HttpSession theSession, HttpServletResponse theResponse ,HttpServletRequest therequest ) throws IOException, MessagingException {
		String	aWebSite = null;
		try {
			aWebSite = itsModelMaintenanceService.getManufacurersLink(joSchedTempHeaderId);
		} catch (SettingsException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>joSchedTempHeaderId:</b>"+joSchedTempHeaderId,"ModelMaintinenceController",e,theSession,therequest);
		} 
		return aWebSite;
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
