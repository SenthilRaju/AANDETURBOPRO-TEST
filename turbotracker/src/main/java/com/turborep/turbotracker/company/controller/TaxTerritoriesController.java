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
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.service.TaxTerritoriesService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.search.service.SearchServiceInterface;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/company")
public class TaxTerritoriesController {

	protected static Logger logger = Logger.getLogger(TaxTerritoriesController.class);
	
	@Resource(name="taxTerritoriesService")
	private TaxTerritoriesService taxTerritoriesService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "SearchServices")
	private SearchServiceInterface itsSearchServices;
	
	/**
	 * Method to get the list of "Chart of Accounts" into the data grid.
	 * @param page
	 * @param rows
	 * @param sidx
	 * @param sord
	 * @param theResponse
	 * @return {@link CustomResponse}
	 * @throws IOException
	 * @throws MessagingException 
	 */
	@RequestMapping(value = "/taxTerritoriesList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getChartsAccounts(@RequestParam(value="page", required=false) Integer thePage,
															@RequestParam(value="rows", required=false) Integer theRows,
															@RequestParam(value="sidx", required=false) String theSidx,
															@RequestParam(value="sord", required=false) String theSord,
															HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, CompanyException, UserException, MessagingException {

		CustomResponse aResponse = null;
		try {
			List<CoTaxTerritory> aTaxTerritory = taxTerritoriesService.getTaxTerritoryList();
			aResponse = new CustomResponse();
			aResponse.setRows(aTaxTerritory);
			aResponse.setRecords(String.valueOf(aTaxTerritory.size()));
			aResponse.setPage(thePage);
		} catch (CompanyException e) {
			sendTransactionException("<b>MethodName:</b>taxTerritoriesList","Company",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} 
		return aResponse;
	}
	
	@RequestMapping(value = "/addNewTerritory", method = RequestMethod.POST)
	public @ResponseBody Boolean addNewTerritory(@RequestParam(value = "coTaxTerritoryName", required = false) Integer coTaxTerritoryID,
													@RequestParam(value = "stateName", required = false) String stateName,
													@RequestParam(value = "stateCodeName", required = false) String stateCodeName,
													@RequestParam(value = "decriptionName", required = false) String decriptionName,
													HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		Boolean isAdded = false;
		/** coTaxTerritoryName=&stateName=GA&stateCideName=01425&decriptionName=Test+Test+Test */
		CoTaxTerritory aNewTerritory = new CoTaxTerritory();
		aNewTerritory.setCounty(decriptionName);
		aNewTerritory.setCountyCode(stateCodeName);
		aNewTerritory.setState(stateName);
		try {
			isAdded = taxTerritoriesService.addTerritory(aNewTerritory);
		} catch (CompanyException e) {
			sendTransactionException("<b>addNewTerritory:</b>addNewTerritory","Company",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return false;
		}
		return isAdded;
	}
	
	@RequestMapping(value = "/deleteTaxTerritory", method = RequestMethod.POST)
	public @ResponseBody Boolean getloginPage(@RequestParam(value = "cotaxTerritoryId", required = false) Integer coTaxTerritoryID,
												HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		Boolean isDeleted = false;
		try {
			isDeleted = taxTerritoriesService.deleteTaxTerritory(coTaxTerritoryID);
		} catch (CompanyException e) {
			sendTransactionException("<b>coTaxTerritoryID:</b>"+coTaxTerritoryID,"Company",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return false;
		}
		return isDeleted;
	}
	@RequestMapping(value = "/editTaxterritory", method = RequestMethod.POST)
	public @ResponseBody Boolean editTaxTerritory(@RequestParam(value = "coTaxTerritoryName", required = false) Integer coTaxTerritoryID,
													@RequestParam(value = "stateName", required = false) String stateName,
													@RequestParam(value = "cotaxterritorryId", required = false) Integer cotaxterritorryId,
													@RequestParam(value = "decriptionName", required = false) String decriptionName,
													@RequestParam(value = "CountyCodeName", required = false) String CountyCodeName,
													@RequestParam(value = "CountyDecription", required = false) String CountyDecription,
													@RequestParam(value = "taxRate", required = false) BigDecimal taxRate,
													@RequestParam(value = "DistDesc1", required = false) String marta,
													@RequestParam(value = "DistDesc2", required = false) String local,
													@RequestParam(value = "DistDesc3", required = false) String towns,
													@RequestParam(value = "DistDesc4", required = false) String specials,
													@RequestParam(value = "DistDesc5", required = false) String education,
													@RequestParam(value = "DistDesc6", required = false) String homeStead,
													@RequestParam(value = "DistDesc7", required = false) String other1,
													@RequestParam(value = "DistDesc8", required = false) String other2,
													
													@RequestParam(value = "Distribution1", required = false) BigDecimal martaDist,
													@RequestParam(value = "Distribution2", required = false) BigDecimal localDist,
													@RequestParam(value = "Distribution3", required = false) BigDecimal townsDist,
													@RequestParam(value = "Distribution4", required = false) BigDecimal specialsDist,
													@RequestParam(value = "Distribution5", required = false) BigDecimal educationDist,
													@RequestParam(value = "Distribution6", required = false) BigDecimal homeSteadDist,
													@RequestParam(value = "Distribution7", required = false) BigDecimal other1Dist,
													@RequestParam(value = "Distribution8", required = false) BigDecimal other2Dist,
													
													@RequestParam(value = "GRDistribution1", required = false) BigDecimal martaGRDist,
													@RequestParam(value = "GRDistribution2", required = false) BigDecimal localGRDist,
													@RequestParam(value = "GRDistribution3", required = false) BigDecimal townsGRDist,
													@RequestParam(value = "GRDistribution4", required = false) BigDecimal specialsGRDist,
													@RequestParam(value = "GRDistribution5", required = false) BigDecimal educationGRDist,
													@RequestParam(value = "GRDistribution6", required = false) BigDecimal homeSteadGRDist,
													@RequestParam(value = "GRDistribution7", required = false) BigDecimal other1GRDist,
													@RequestParam(value = "GRDistribution8", required = false) BigDecimal other2GRDist,
													
													@RequestParam(value = "rateId2", required = false) BigDecimal rateId2 ,
													@RequestParam(value = "rateId3", required = false) BigDecimal rateId3 ,
													@RequestParam(value = "rateId4", required = false) BigDecimal rateId4 ,
													@RequestParam(value = "rateId5", required = false) BigDecimal rateId5 ,
													@RequestParam(value = "rateId6", required = false) BigDecimal rateId6 ,
													
													@RequestParam(value = "from2", required = false) BigDecimal from2 ,
													@RequestParam(value = "from3", required = false) BigDecimal from3 ,
													@RequestParam(value = "from4", required = false) BigDecimal from4 ,
													@RequestParam(value = "from5", required = false) BigDecimal from5 ,
													@RequestParam(value = "from6", required = false) BigDecimal from6 ,
													
													@RequestParam(value = "capName", required = false) BigDecimal cap,
													@RequestParam(value = "rateName", required = false) BigDecimal rate,
													@RequestParam(value = "GRTaxRate", required = false) BigDecimal GRTaxRate,
													@RequestParam(value = "Inactive", required = false) boolean inActiveChkbx,
													@RequestParam(value = "chk_taxfreightval", required = false) boolean chk_taxfreightval,
													HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		Boolean isUpdated = false;
		/** coTaxTerritoryName=&stateName=GA&stateCideName=01425&decriptionName=Test+Test+Test */
		CoTaxTerritory aNewTerritory = new CoTaxTerritory();
		aNewTerritory.setCoTaxTerritoryId(cotaxterritorryId);
		aNewTerritory.setState(stateName);
		aNewTerritory.setCounty(CountyDecription);
		aNewTerritory.setCountyCode(CountyCodeName);
		aNewTerritory.setTaxRate(taxRate);
		aNewTerritory.setDistDesc1(marta);
		aNewTerritory.setDistribution1(martaDist);
		aNewTerritory.setgRDistribution1(martaGRDist);
		aNewTerritory.setDistDesc2(local);
		aNewTerritory.setDistribution2(localDist);
		aNewTerritory.setgRDistribution2(localGRDist);
		aNewTerritory.setDistDesc3(towns);
		aNewTerritory.setDistribution3(townsDist);
		aNewTerritory.setgRDistribution3(townsGRDist);
		aNewTerritory.setDistDesc4(specials);
		aNewTerritory.setDistribution4(specialsDist);
		aNewTerritory.setgRDistribution4(specialsGRDist);
		aNewTerritory.setDistDesc5(education);
		aNewTerritory.setDistribution5(educationDist);
		aNewTerritory.setgRDistribution5(educationGRDist);
		aNewTerritory.setDistDesc6(homeStead);
		aNewTerritory.setgRDistribution6(homeSteadGRDist);
		aNewTerritory.setDistribution6(homeSteadDist);
		aNewTerritory.setDistDesc7(other1);
		aNewTerritory.setDistribution7(other1Dist);
		aNewTerritory.setgRDistribution7(other1GRDist);
		aNewTerritory.setDistDesc8(other2);
		aNewTerritory.setDistribution8(other2Dist);
		aNewTerritory.setgRDistribution8(other2GRDist);
		
		aNewTerritory.setRate2(rateId2);
		aNewTerritory.setRate3(rateId3);
		aNewTerritory.setRate4(rateId4);
		aNewTerritory.setRate5(rateId5);
		aNewTerritory.setRate6(rateId6);
		
		aNewTerritory.setFrom2(from2);
		aNewTerritory.setFrom3(from3);
		aNewTerritory.setFrom4(from4);
		aNewTerritory.setFrom5(from5);
		aNewTerritory.setFrom6(from6);
		
		aNewTerritory.setSurtaxCap(cap);
		aNewTerritory.setSurtaxRate(rate);
		aNewTerritory.setgRTaxRate(GRTaxRate);
		byte aInActive = (byte) (inActiveChkbx?1:0);
		aNewTerritory.setInactive(aInActive);
		byte Istaxfreight = (byte) (chk_taxfreightval?1:0);
		aNewTerritory.setTaxfreight(Istaxfreight);
		try {
			isUpdated = taxTerritoriesService.editTaxTerritory(aNewTerritory);
		} catch (CompanyException e) {
			sendTransactionException("<b>cotaxterritorryId:</b>"+cotaxterritorryId,"Company",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return false;
		}
		return isUpdated;
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