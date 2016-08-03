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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
public class AccountingCyclesController {
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@InitBinder
	private void dateBinder(WebDataBinder binder) {
	            //The date format to parse or output your dates
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	            //Create a new CustomDateEditor
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	            //Register it as custom editor for the Date type
	    binder.registerCustomEditor(Date.class, editor);
	}

	protected static Logger logger = Logger
			.getLogger(AccountingCyclesController.class);

	@Autowired
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name="gltransactionService")
	GltransactionService gltransactionService;
	
	@RequestMapping(value = "/updateSystemInfoDetails", method = RequestMethod.GET)
	public @ResponseBody
	String updateSystemInfoDetails(HttpServletRequest theRequest,HttpServletResponse theResponse,HttpSession aSession) throws IOException, MessagingException {
		try {
			System.out.println("AccountingCyclesController.updateSystemInfoDetails()");
			accountingCyclesService.updatesysInfoDetailsinitially();
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>TrackingID:</b> updateSystemInfoDetails","Banking",e,aSession,theRequest);
		}
		return "Success";
	}
	
	
	@RequestMapping(value = "/accountingcycles", method = RequestMethod.GET)
	public String companyjournals(ModelMap modal, HttpServletRequest request,HttpSession aSession) throws IOException, MessagingException {
		String aPage = "accountingcyclesNew";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message", "User Name Or Password are Invalid.");
		}
		try {
			
			Calendar cal = Calendar.getInstance();
			Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
			modal.addAttribute("sysInfo",aSysinfo);
			
			Cofiscalperiod coFiscalPeriod=accountingCyclesService.getCurrentPeriod(aSysinfo.getCurrentPeriodId());
			Cofiscalyear coFiscalYear= accountingCyclesService.getCurrentYear(aSysinfo.getCurrentFiscalYearId());
			Cofiscalyear coFiscalYear1= accountingCyclesService.getCurrentYear(aSysinfo.getCurrentFiscalYearId());
			
			modal.addAttribute("currentPeriod", coFiscalPeriod);
			modal.addAttribute("currentYear", coFiscalYear);
			modal.addAttribute("currentYearforclosedyearDlg", coFiscalYear);   
			modal.addAttribute("currentPeriodList", accountingCyclesService.getCurrentYearList(aSysinfo.getCurrentFiscalYearId()));
			modal.addAttribute("fiscalYearList", accountingCyclesService.getfiscalYearList());
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>TrackingID:</b> updateSystemInfoDetails","Banking",e,aSession,request);
		}
		
		System.out.println("Accounting Cycle page:::"+aPage);
		return aPage;
	}
	
	@RequestMapping(value = "/cofiscalYeardropdownchanged", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> cofiscalYeardropdownchanged(@RequestParam(value = "yearId", required = true) Integer yearID,
			@RequestParam(value = "closesysInfoId", required = false) Integer sysInfoId,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws CompanyException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		ArrayList<Cofiscalyear> cofisYearList = new ArrayList<Cofiscalyear>();
		try {
			
			ArrayList<Cofiscalperiod> cofisPeriodActivityList = (ArrayList<Cofiscalperiod>)accountingCyclesService.getActivitycount(yearID);
			ArrayList<Cofiscalperiod> cofisPeriodList = (ArrayList<Cofiscalperiod>)accountingCyclesService.getCurrentYearList(yearID);
			cofisYearList.add(accountingCyclesService.getCurrentYear(yearID));
			
			map.put("dpchange_getYearList", cofisYearList);
			map.put("dpchange_getPeriodList", cofisPeriodList);
			map.put("dpchange_getActivityList", cofisPeriodActivityList);
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>SysInfoId:</b> "+sysInfoId,"Banking",e,aSession,request);
		}
		return map;
	}
	
	@RequestMapping(value = "/closecurrentPeriod", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse closecurrentPeriod(@RequestParam(value = "closeCurrentPeriodId", required = true) Integer period,
								@RequestParam(value = "closeCurrentYearId", required = true) Integer year,
								@RequestParam(value = "oper", required = true) String oper,
								HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws CompanyException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
		boolean opStatus =false;
		try {
			
			if(oper.equals("open"))
			{
			opStatus=true;
			accountingCyclesService.openorclosePeriodIdbasedonperiod(period,year,opStatus);
			}
			else
			{
			accountingCyclesService.openorclosePeriodIdbasedonperiod(period,year,opStatus);	
			}
			
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>periodID:</b> "+period,"Banking",e,aSession,request);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/updatePeriods", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse insertPeriod(
			@RequestParam(value = "jsonList", required = false) String gridData,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "periodID", required = false) Integer periodID,
			@RequestParam(value = "yearID", required = false) Integer yearID,
			@RequestParam(value = "closesysInfoId", required = false) Integer sysInfoId,
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession aSession) throws CompanyException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		boolean statusCheck = true;
		try {
			
			accountingCyclesService.updatecoFiscalYearDetails(yearID, description);
			JsonParser parser = new JsonParser();
			if (null != gridData && gridData.length() > 6) {
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				for (JsonElement ele1 : array) {
					JsonObject obj = ele1.getAsJsonObject();
					System.out.println("Period::"+obj.get("period").getAsInt());
					if(!obj.get("periodstartdate").getAsString().equals("") && !obj.get("periodenddate").getAsString().equals(""))
					{
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date stDart = sdf.parse(obj.get("periodstartdate").getAsString());
					Date endDart = sdf.parse(obj.get("periodenddate").getAsString());
					System.out.println("AccountingCyclesController.insertPeriod()::"+endDart);
					statusCheck = accountingCyclesService.getperiodAvailableStatus(obj.get("period").getAsInt(),yearID);
					if(statusCheck)
					accountingCyclesService.updatePeriodId(stDart,endDart,yearID,true,obj.get("period").getAsInt());
					else
					accountingCyclesService.createPeriodId(stDart,endDart,yearID,true,obj.get("period").getAsInt());
					}
				}
			}
		//	accountingCyclesService.updateSiteInfo(sysInfoId,reversePeriodId);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>sysInfoId:</b> "+sysInfoId,"Banking",e,aSession,request);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/savecoFiscalYear", method = RequestMethod.POST)
	public @ResponseBody
	String savecoFiscalYear(@RequestParam(value = "coFiscalYear", required = false) String coFiscalYear,
									@RequestParam(value = "curyearstartdate", required = false) Date curyearstartdate,
									@RequestParam(value = "curyearenddate", required = false) Date curyearenddate,
									@RequestParam(value = "closesysInfoId", required = false) String sysInfoId,
									@RequestParam(value = "jsonList", required = false) String gridData,
									@RequestParam(value = "description", required = false) String description,
									HttpServletRequest request,HttpServletResponse theResponse,HttpSession aSession) throws ParseException, IOException, MessagingException {
		String response = "";
		int yearId=0;
		int periodId =0;
		Integer checkYear;
		try {
			
			checkYear = accountingCyclesService.getYearId(coFiscalYear);
			
			System.out.println("Year::"+checkYear);
			
			if(checkYear==null || checkYear == 0)
			{
				yearId = accountingCyclesService.createYear(curyearstartdate,curyearenddate,coFiscalYear,description);
				JsonParser parser = new JsonParser();
				if (null != gridData && gridData.length() > 6) {
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						System.out.println("Period::"+obj.get("period").getAsInt());
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						Date stDart = sdf.parse(obj.get("periodstartdate").getAsString());
						Date endDart = sdf.parse(obj.get("periodenddate").getAsString());
						accountingCyclesService.createPeriodId(stDart,endDart,yearId,true,obj.get("period").getAsInt());
					}
				}
				periodId =accountingCyclesService.getPeriodId(curyearstartdate, yearId) ;
				accountingCyclesService.updateSiteInfo(Integer.parseInt(sysInfoId),periodId,yearId);	
			
			response="Success";
			}
			else
			{
			response="failed";	
			}
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>sysInfoId:</b> "+sysInfoId,"Banking",e,aSession,request);
		}
		return response;
	}
	
	@RequestMapping(value = "/getAllcoFiscalPeriodBasedonyear", method = RequestMethod.POST)
	public @ResponseBody Map<String, ArrayList<?>> getCheckList(@RequestParam(value = "closesysInfoId", required = false) Integer closesysInfoId,
																HttpSession session, HttpServletResponse theResponse,HttpServletRequest request) throws CompanyException, IOException, MessagingException{
		
		Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
		
			ArrayList<Cofiscalperiod> cofisPeriodList = (ArrayList<Cofiscalperiod>)accountingCyclesService.getCurrentYearList(aSysinfo.getCurrentFiscalYearId());
			
			if(cofisPeriodList != null && cofisPeriodList.size() > 0)
				map.put("currentPeriodList", cofisPeriodList);
			else
				map.put("currentPeriodList", new ArrayList<Motransaction>());
			
			
			ArrayList<Cofiscalperiod> cofisPeriodActivityList = (ArrayList<Cofiscalperiod>)accountingCyclesService.getActivitycount(aSysinfo.getCurrentFiscalYearId());
			
			map.put("ActivityList", cofisPeriodActivityList);
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>sysInfoId:</b> "+closesysInfoId,"Banking",e,session,request);
		}
		return map;
	}
	
	@RequestMapping(value = "/checkAccountingCyclePeriods", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkAccountingCyclePeriods(@RequestParam(value = "datetoCheck", required = false) Date datetoCheck,
											   @RequestParam(value = "UserStatus", required = false) boolean UserStatus,
											   HttpSession session,HttpServletResponse theResponse,HttpServletRequest request) throws IOException, MessagingException {
		
		System.out.println("===>>"+datetoCheck);
		Cofiscalperiod cofiscalperiod = new Cofiscalperiod();
		Map<String, Object> map = new HashMap<String, Object>();
		String AuthStatus = "";
		UserBean aUserBean;
		try {
			
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
			
			if(aUserBean.getSystemAdministrator() == 1 || !UserStatus)
			{
			 cofiscalperiod = accountingCyclesService.getAllOpenPeriods(datetoCheck);
			 if(cofiscalperiod==null)
			 AuthStatus="granted";
			}
			else
			{
			 cofiscalperiod = accountingCyclesService.getCurrentOpenPeriods(datetoCheck,aSysinfo.getCurrentFiscalYearId(),aSysinfo.getCurrentPeriodId());
			 if(cofiscalperiod==null)
			 AuthStatus="denied";
			}
			
			map.put("cofiscalperiod", cofiscalperiod);
			map.put("AuthStatus", AuthStatus);
			 
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>coFiscalPeriod:</b> "+cofiscalperiod,"Banking",e,session,request);
		}
		return map;
	}
	
	
	@RequestMapping(value = "/getMostRecentPeriod", method = RequestMethod.GET)
	public @ResponseBody String getMostRecentPeriod(HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, CompanyException, MessagingException {
		String period ="";

		try {
			
			List<Object> listArray = (List<Object>) accountingCyclesService.getMostRecentPeriods();
			for(int i=0;i<listArray.size();i++)
			{
			Object row[] = (Object[]) listArray.get(i);
			period = row[0]+"--"+row[1];
			System.out.println(period);	
			}
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coFiscalYear-coFiscalPeriod:</b> "+period,"Banking",e,session,request);
		}
		return period;
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

	@RequestMapping(value = "/getCurrentopeningperiods", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getCurrentopeningperiods(HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, CompanyException, MessagingException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			map.put("Sysinfo", accountingCyclesService.getSysinfo());
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value = "/closeFiscalyearValidation", method = RequestMethod.POST)
	public @ResponseBody Map<String,String> closeFiscalyearValidation(@RequestParam(value = "currentperiod", required = false) Integer currentperiod,
			@RequestParam(value = "currentyear", required = false) Integer currentyear,
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, CompanyException, MessagingException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			int a=0,b=0;
			ArrayList<Cofiscalperiod> cofisPeriodList = (ArrayList<Cofiscalperiod>)accountingCyclesService.getCurrentYearList(currentyear);
			for(Cofiscalperiod cObj:cofisPeriodList)
			{
				if(cObj.getOpenStatus()==null || cObj.getOpenStatus())
					a++;
				if(cObj.getPeriod() == 12)
					b=cObj.getCoFiscalPeriodId();
			}
			
			if(a==0)
				map.put("periodflag", "Enable");
			else
				map.put("periodflag", "Disable");
			
			if(b==currentperiod)
			{
				map.put("yearflag", "Enable");
			}
			else
			{
				if(a==0)
					map.put("yearflag", "Enable");
				else
					map.put("yearflag", "Disable");
			}
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value = "/closeAllopenperiods", method = RequestMethod.POST)
	public @ResponseBody boolean closeAllopenperiods(@RequestParam(value = "currentyear", required = false) Integer currentyear,
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, CompanyException, MessagingException {
		try {
			
			UserBean aUserBean=null;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			accountingCyclesService.updateAllperiosstatusclosed(currentyear);
			Cofiscalperiod aCofiscalperiod = accountingCyclesService.getPeriodDetailswithyearid(currentyear);
			gltransactionService.insertglTransactionwith13thperiod(aCofiscalperiod.getCoFiscalYearId(),aCofiscalperiod.getCoFiscalPeriodId(),aUserBean.getFullName());
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return true;
	}
	
	@RequestMapping(value = "/checkClosedyearstatus", method = RequestMethod.POST)
	public @ResponseBody boolean checkClosedyearstatus(@RequestParam(value = "currentyear", required = false) Integer currentyear,
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, CompanyException, MessagingException {
		boolean closedstatus = true;
		try {
		
			Cofiscalyear aCofiscalyear =  accountingCyclesService.getCurrentYear(currentyear);
			
			if(aCofiscalyear!=null)
			{
				closedstatus = aCofiscalyear.getClosedStatus();
			}
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return closedstatus;
	}
	
	@RequestMapping(value = "/updateyeartoopen", method = RequestMethod.POST)
	public @ResponseBody boolean updateyeartoopen(@RequestParam(value = "currentyear", required = false) Integer currentyear,
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, CompanyException, MessagingException {
		try {
			accountingCyclesService.updateCurrentYeartoopen(currentyear);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return true;
	}
	
}