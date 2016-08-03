/**
 * 
 */
package com.turborep.turbotracker.finance.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.banking.dao.GlTransactionv1;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.RxJournal;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.employee.dao.EmMasterBean;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.GlReportingAccounts;
import com.turborep.turbotracker.finance.dao.GlTransactionTest;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.finance.exception.DrillDownException;
import com.turborep.turbotracker.finance.service.DrillDownService;
import com.turborep.turbotracker.finance.service.FinancePostingService;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomGenericResponse;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.search.service.SearchServiceInterface;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;


@Controller
@RequestMapping("/drillDown")
public class FinanceController {
	protected static Logger itsLogger = Logger.getLogger(FinanceController.class);
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="drillDownService")
	private DrillDownService itsDrillDownService;
	
	@Resource(name="financePostingService")
	private FinancePostingService itsFinancePostingService;
	
	@Resource(name = "SearchServices")
	private SearchServiceInterface itsSearchServices;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	
	/**
	 * Created by : Leo    Date: 15-09-2014
	 * 
	 * Description: GlTransaction Report Generate.
	 * @throws MessagingException 
	 * 
	 * */
	
	@RequestMapping(value = "accountDtails1", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAccountsDetails1(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "accountNumber", required = false)String accountNumber,
			@RequestParam(value = "periodsId", required = false)String periodFromID,
			@RequestParam(value = "periodsToID", required = false)String periodToID,
			@RequestParam(value = "yearID", required = false)String yearID,
			@RequestParam(value = "mostRecentPeriodID", required = false)String mostRecentPeriod,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = null;
		
		try {
			
			aResponse = new CustomResponse();
			BigInteger aTotalCount = itsDrillDownService.getGlTransactionCount();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount.intValue())
				aTo = aTotalCount.intValue();
			
			List<GlTransactionTest> accountList = itsDrillDownService.getAccountDetails1(accountNumber,periodFromID,periodToID,yearID,mostRecentPeriod,aFrom, aTo);
		
			itsLogger.debug("Retriving all the data of customer is start");
			aResponse.setRows(accountList);
			aResponse.setRecords(String.valueOf(accountList.size()));
			aResponse.setPage(thePage);
			
			itsLogger.debug("Retriving all the data of customer is end");
			aResponse.setTotal((int) Math.ceil((double) aTotalCount.intValue() / (double) theRows));
		} catch (DrillDownException e) {
			sendTransactionException("<b>accountNumber:</b>"+accountNumber,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>accountNumber:</b>"+accountNumber,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}

		
	
	
	/**
	 * Created by : Leo    Date: 12-09-2014
	 * 
	 * Description: Account Tab Report Generate.
	 * @throws MessagingException 
	 * 
	 * */
	
	@RequestMapping(value = "accountDtails", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAccountsDetails(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "accountNumber", required = false)String accountNumber,
			@RequestParam(value = "periodsId", required = false)String periodFromID,
			@RequestParam(value = "periodsToID", required = false)String periodToID,
			@RequestParam(value = "yearID", required = false)String yearID,
			@RequestParam(value = "accDesc", required = false)String accDesc,
			@RequestParam(value = "coAccountsID", required = false)Integer coAccountsID,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
		
		CustomResponse aResponse = null;
		try {
			aResponse = new CustomResponse();
			
			BigInteger aTotalCount = itsDrillDownService.getGlTransactionCount();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount.intValue())
				aTo = aTotalCount.intValue();
			
			List<GlReportingAccounts> accountList = itsDrillDownService.getAccountDetails(accountNumber,accDesc,periodFromID,periodToID,yearID,coAccountsID);
			
			itsLogger.debug("Retriving all the data of customer is start");
		
			aResponse.setRows(accountList);
			aResponse.setRecords(String.valueOf(accountList.size()));
			aResponse.setPage(thePage);
			
			itsLogger.debug("Retriving all the data of customer is end");
			aResponse.setTotal((int) Math.ceil((double) aTotalCount.intValue() / (double) theRows));
		} catch (DrillDownException e) {
			sendTransactionException("<b>accountNumber:</b>"+accountNumber,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>accountNumber:</b>"+accountNumber,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	
	/**
	 * Created by : Leo    Date: 15-09-2014
	 * 
	 * Description: JournalBalance Tab Report Generate.
	 * @throws MessagingException 
	 * 
	 * */
	
	@RequestMapping(value = "journalBalance", method = RequestMethod.POST)
	public @ResponseBody CustomResponse journalBalance(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "accountID", required = false)String accountID,			
			@RequestParam(value = "periodsId", required = false)String periodsid,
			@RequestParam(value = "accDesc", required = false)String accDesc,
			@RequestParam(value = "yearID", required = false)String yearID,
			@RequestParam(value = "coAccountsID", required = false)Integer coAccountsID,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
		
		CustomResponse aResponse = null;
		try {
			aResponse = new CustomResponse();
			List<?> accountCountList = itsDrillDownService.getJournalBalance(accountID,periodsid,accDesc,yearID,coAccountsID);
			int aFrom, aTo;
			int aTotalCount = accountCountList.size();
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			
			List<?> accountList = itsDrillDownService.getJournalBalance(accountID,periodsid,accDesc,yearID,aFrom,aTo,theSidx,theSord,coAccountsID);
			
			itsLogger.debug("Retriving all the data of customer");
			
			System.out.println(accountID+"-"+periodsid+"-"+yearID);
			
			
			aResponse.setRows(accountList);
			aResponse.setRecords(String.valueOf(accountList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount / (double) theRows));
		} catch (DrillDownException e) {
			sendTransactionException("<b>coAccountsID:</b>"+coAccountsID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>coAccountsID:</b>"+coAccountsID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	

	/**
	 * Created by : Leo    Date: 15-09-2014
	 * 
	 * Description: Journal Details Tab Report Generate.
	 * @throws MessagingException 
	 * 
	 * */
	
	
	@RequestMapping(value = "journalDetails", method = RequestMethod.GET)
	public @ResponseBody CustomResponse journalDetails(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "accountID", required = false)String accountID,
			@RequestParam(value = "accountDesc", required = false)String accountDesc,
			@RequestParam(value = "period", required = false)String period,
			@RequestParam(value = "yearID", required = false)String yearID,
			@RequestParam(value = "journalID", required = false)String journalID,
			@RequestParam(value = "coAccountsID", required = false)Integer coAccountsID,
			HttpSession session,ModelMap model, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
			itsLogger.info("journalDetails JournalID: "+journalID);
			itsLogger.info("Responses From Grid:\n page:"+thePage+"\nrows:"+theRows+"\nsidx:"+theSidx+"\nsord:"+theSord);
		CustomResponse aResponse = null;
		try {
			
			aResponse = new CustomResponse();		
			
			itsLogger.debug("Retriving all the data of customer");			
			
			List<?> accountCountList = itsDrillDownService.getJournalDetails(accountID,accountDesc,period,yearID,journalID,coAccountsID);
			int aFrom, aTo;
			int aTotalCount = accountCountList.size();
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			List<?> accountList = itsDrillDownService.getJournalDetails(accountID,accountDesc,period,yearID,journalID,aFrom,aTo,theSidx,theSord,coAccountsID);
			
			aResponse = new CustomResponse();		
			aResponse.setRows(accountList);
			aResponse.setRecords(String.valueOf(accountList.size()));
			aResponse.setPage(thePage);			
			aResponse.setTotal((int) Math.ceil((double)aTotalCount / (double) theRows));
	
			
		} catch (DrillDownException e) {
			sendTransactionException("<b>coAccountsID:</b>"+coAccountsID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>coAccountsID:</b>"+coAccountsID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	
	
	@RequestMapping(value = "journalDetailsSum", method = RequestMethod.GET)
	public @ResponseBody GlReportingAccounts journalDetailsSum(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "accountID", required = false)String accountID,
			@RequestParam(value = "accountDesc", required = false)String accountDesc,
			@RequestParam(value = "period", required = false)String period,
			@RequestParam(value = "yearID", required = false)String yearID,
			@RequestParam(value = "journalID", required = false)String journalID,
			@RequestParam(value = "coAccountsID", required = false)Integer coAccountsID,
			HttpSession session,ModelMap model, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
		GlReportingAccounts aResponse = null;
		try {
			aResponse = new GlReportingAccounts();	
			aResponse = itsDrillDownService.getJournalDetailsSum(accountID,period,yearID,journalID,coAccountsID);
		
			
		} catch (DrillDownException e) {
			sendTransactionException("<b>coAccountsID:</b>"+coAccountsID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	
	/**
	 * Created by : Leo    Date: 15-09-2014
	 * 
	 * Description: Transaction Details Tab Report Generate.
	 * @throws MessagingException 
	 * 
	 * */
	
	
	@RequestMapping(value = "transactionDetails", method = RequestMethod.GET)
	public @ResponseBody CustomResponse transactionDetails(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "transactionReferenceID", required = false)String transactionReferenceID,
			@RequestParam(value = "description", required = false)String description,
			@RequestParam(value = "sourceid", required = false)String sourceid,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
			itsLogger.info("transactionReferenceID: "+transactionReferenceID);
		CustomResponse aResponse = null;
		try {
			aResponse = new CustomResponse();
			
			List<?> accountList = itsDrillDownService.getTransactionDetails(transactionReferenceID,description,sourceid);
			
			int aTotalCount = accountList.size();
			int theFrom, theTo;
			theTo = (theRows * thePage);
			theFrom = theTo - theRows;
			if (theTo > aTotalCount) theTo = aTotalCount;
			itsLogger.debug("Post Date: "+description);
			
			aResponse.setRows(accountList);
			aResponse.setRecords(String.valueOf(accountList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount / (double) theRows));
		} catch (DrillDownException e) {
			sendTransactionException("<b>transactionReferenceID:</b>"+transactionReferenceID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>transactionReferenceID:</b>"+transactionReferenceID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/getYearList", method = RequestMethod.POST)
	public @ResponseBody Map<String, ArrayList<?>> getCheckList(
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest)
			throws IOException, JobException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Cofiscalyear> yearList = (ArrayList<Cofiscalyear>)itsDrillDownService.getYearList();
			itsLogger.info("Year List Size: "+yearList.size());
			if(yearList != null && yearList.size() > 0)
				map.put("yearList", yearList);
			else
				map.put("yearList", new ArrayList<Cofiscalyear>());
		} catch (Exception e) {
			sendTransactionException("<b>getCheckList:</b>getCheckList","Finance",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	
	
	
	@RequestMapping(value = "/getAccountNoList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getCoAccountDetails(
			@RequestParam("term") String accNo,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		itsLogger.debug("Received request to get search Account No Lists");

		List<AutoCompleteBean> aAccountsSearchList = null;
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aAccountsSearchList = (List<AutoCompleteBean>) itsSearchServices.getSearchAccountsList(aAutoCompleteBean, accNo);
		} catch (Exception e) {
			sendTransactionException("<b>accNo:</b>"+accNo,"Finance",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(501, e.getMessage());
		}
		return aAccountsSearchList;
		
	}
	
	
	/**
	 * Created by : Leo    Date: 15-09-2014
	 * 
	 * Description: Trial Balance Report Generate.
	 * @throws MessagingException 
	 * 
	 * */
	
	@RequestMapping(value = "trialBalanceGridView", method = RequestMethod.GET)
	public @ResponseBody CustomResponse trialBalanceGridView(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord, 
			@RequestParam(value = "periodEnding", required = false)Date periodEndingDate,
			@RequestParam(value = "fromCoAccountid", required = false)Integer startcoAccountID,
			@RequestParam(value = "toCoAccountid", required = false)Integer endcoAccountID,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, MessagingException {
			itsLogger.info("periodEndingDate::["+periodEndingDate+"] || startcoAccountID::["+startcoAccountID+"] || endcoAccountID::["+endcoAccountID+"]");
		CustomResponse aResponse = null;
		try {
			aResponse = new CustomResponse();
			Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
			
			Cofiscalperiod cofiscalperiod = itsDrillDownService.getAllPeriodsbasedonDate(periodEndingDate);
						
			List<?> accountList = itsDrillDownService.getAllTrialBalances(cofiscalperiod.getCoFiscalPeriodId(),cofiscalperiod.getCoFiscalYearId(),startcoAccountID,endcoAccountID);
			
/*			int aTotalCount = accountList.size();
			int theFrom, theTo;
			theTo = (theRows * thePage);
			theFrom = theTo - theRows;
			if (theTo > aTotalCount) theTo = aTotalCount;
			itsLogger.debug("Post Date: "+description);*/
			
			aResponse.setRows(accountList);
			aResponse.setRecords(String.valueOf(accountList.size()));
			aResponse.setPage(thePage);
		//	aResponse.setTotal((int) Math.ceil((double) aTotalCount / (double) theRows));
		} catch (DrillDownException e) {
			sendTransactionException("<b>startcoAccountID,endcoAccountID:</b>"+startcoAccountID+","+endcoAccountID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			sendTransactionException("<b>startcoAccountID,endcoAccountID:</b>"+startcoAccountID+","+endcoAccountID,"Finance",e,session,therequest);
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/printtrialBalance", method = RequestMethod.GET)
	public @ResponseBody
	void printInventoryCSV(
			@RequestParam(value = "periodEnding", required = false)Date periodEndingDate,
			@RequestParam(value = "fromCoAccountid", required = false)Integer startcoAccountID,
			@RequestParam(value = "toCoAccountid", required = false)Integer endcoAccountID,
			@RequestParam(value = "showCurrentPeriod", required = false)boolean showCurrentPeriod,
			HttpSession session, HttpServletResponse theResponse, HttpServletRequest therequest)
			throws IOException, SQLException, MessagingException {
		
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			
			itsLogger.info("periodEndingDate***::["+periodEndingDate+"] || startcoAccountID***::["+startcoAccountID+"] || endcoAccountID***::["+endcoAccountID+"]"+"showCurrentPeriod***::["+showCurrentPeriod+"]");
			
			Cofiscalperiod cofiscalperiod = itsDrillDownService.getAllPeriodsbasedonDate(periodEndingDate);
			
			String path_JRXML =null;
			String filename=null;
			String accDetailsQueryString = "";
			String appendString = "";
			HashMap<String, Object> params = new HashMap<String, Object>();
			Integer periodID = cofiscalperiod.getCoFiscalPeriodId();
			Integer yearID = cofiscalperiod.getCoFiscalYearId();
			
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String reportDate = df.format(periodEndingDate);
			
			
			if(startcoAccountID!=-1 && endcoAccountID !=-1)
			{
				appendString = "AND cA.coAccountID >="+startcoAccountID+" AND cA.coAccountID <="+endcoAccountID;
			}
			else if(startcoAccountID!=-1 && endcoAccountID == -1)
			{
				appendString = "AND cA.coAccountID ="+startcoAccountID;
			}
			else if(startcoAccountID == -1 && endcoAccountID !=-1)
			{
				appendString = "AND cA.coAccountID ="+endcoAccountID;
			}
			else
			{
				appendString ="";
			}
			
			 accDetailsQueryString = "SELECT  cA.coAccountID,cA.Number,cA.Description, IF(pdebits-pcredits>0,pdebits-pcredits,0) AS pdebits,IF(pcredits-pdebits>0,pcredits-pdebits,0) AS pcredits,IF(ydebits-ycredits>0,ydebits-ycredits,0) AS ydebits,IF(ycredits-ydebits>0,ycredits-ydebits,0) AS ycredits,tsSet.HeaderText,tsSet.companyLogo FROM tsUserSetting tsSet,"
						+ " coAccount cA"
						+ " LEFT JOIN"
						+ " (SELECT  gt.coAccountID, TRUNCATE(SUM(TRUNCATE(gt.debit,2)),2) pdebits,TRUNCATE(SUM(TRUNCATE(gt.credit,2)),2)pcredits FROM glTransaction gt WHERE gt.coFiscalPeriodId = "+periodID+" and  gt.coFiscalYearId = "+yearID+" GROUP BY gt.coAccountID) gl ON cA.coAccountID = gl.coAccountID "+appendString
						+ " LEFT JOIN"
						+ " (SELECT  gtt.coAccountID, TRUNCATE(SUM(TRUNCATE(gtt.debit,2)),2) ydebits,TRUNCATE(SUM(TRUNCATE(gtt.credit,2)),2) ycredits FROM glTransaction gtt WHERE "
						+ " (gtt.fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearID+")  OR (gtt.period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+") AND gtt.coFiscalYearId = "+yearID+")) "
						//+ "(gtt.period <= (SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+") AND gtt.coFiscalYearId = "+yearID+")  "
						+ " GROUP BY gtt.coAccountID) gll ON cA.coAccountID = gll.coAccountID "+appendString
						+ " WHERE cA.InActive <> 1 AND pdebits IS NOT NULL OR pcredits IS NOT NULL OR ydebits IS NOT NULL OR ycredits IS NOT NULL HAVING ycredits>0 OR ydebits>0 Order by cA.Number";
			
				path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/TrailBalance.jrxml");
				filename="TrialBalance.pdf";
				
			itsLogger.info("TrialBalanceQuery=="+accDetailsQueryString);
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(accDetailsQueryString);
			jd.setQuery(jdq);
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("periodEnding", reportDate);
			params.put("showcurrentPeriod", showCurrentPeriod);
			 connection = con.getConnection();
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);

		} catch (Exception e) {
			sendTransactionException("<b>startcoAccountID,endcoAccountID:</b>"+startcoAccountID+","+endcoAccountID,"Finance",e,session,therequest);
			theResponse.sendError(500, e.getMessage());
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value = "/printIncomeStatement", method = RequestMethod.GET)
    public @ResponseBody
    void printIncomeStatement(
            @RequestParam(value = "periodEnding", required = false)Date periodEndingDate,
            @RequestParam(value = "showAccounts", required = false)String AccountStatus,
            @RequestParam(value = "division", required = false)Integer division,
            @RequestParam(value = "viewflag", required = false)String viewflag,
            HttpSession session, HttpServletResponse theResponse, HttpServletRequest therequest)
            throws IOException, SQLException, MessagingException {
        InputStream imageStream =null;
        Connection connection =null;
        ConnectionProvider con = null;
        try {
           
            itsLogger.info("periodEndingDate***::["+periodEndingDate+"] || AccountStatus***::["+AccountStatus+"] || division***::["+division+"]");
           
            Cofiscalperiod cofiscalperiod = itsDrillDownService.getAllPeriodsbasedonDate(periodEndingDate);
           
            String path_JRXML =null;
            String path_JRXML_CSV =null;
            String filename=null;
            String accDetailsQueryString = "";
            String appendString = "";
            HashMap<String, Object> params = new HashMap<String, Object>();
            Integer periodID = cofiscalperiod.getCoFiscalPeriodId();
            Integer yearID = cofiscalperiod.getCoFiscalYearId();
           
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String StartDate = df.format(cofiscalperiod.getStartDate());
            String EndDate = df.format(cofiscalperiod.getEndDate());
            boolean accStatus = true;
           
            if(AccountStatus.equals("Yes"))
                accStatus =true;
            else
                accStatus = false;
           
             accDetailsQueryString = "SELECT coAccountID,Number,Description,accountType,FinancialStatus,"
             		+ "(pdebits+pcredits) AS PeriodAmount,(ydebits+ycredits) AS YearAmount "
            		// +"IF(FinancialStatus=1,IF((pdebits+pcredits)>0,(pdebits+pcredits)*-1,(pdebits+pcredits)),IF((pdebits+pcredits)>0,(pdebits+pcredits),(pdebits+pcredits)*-1)) AS PeriodAmount,"
             		// +"IF(FinancialStatus=1,IF((ydebits+ycredits)>0,(ydebits+ycredits)*-1,(ydebits+ycredits)),IF((ydebits+ycredits)>0,(ydebits+ycredits),(ydebits+ycredits)*-1)) AS YearAmount"
            		 + "FROM ("
                     + " SELECT  cA.coAccountID,cA.Number,cA.Description,cA.accountType,cA.FinancialStatus, IF(pdebits-pcredits>0,pdebits-pcredits,0) AS pdebits,IF(pcredits-pdebits>0,pcredits-pdebits,0) AS pcredits,"
                     + " IF(ydebits-ycredits>0,ydebits-ycredits,0) AS ydebits, IF(ycredits-ydebits>0,ycredits-ydebits,0) AS ycredits FROM coAccount cA LEFT JOIN "
                     + " (SELECT  gt.coAccountID, TRUNCATE(SUM(TRUNCATE(gt.debit,4)),2) pdebits,TRUNCATE(SUM(TRUNCATE(gt.credit,4)),2)pcredits FROM glTransaction gt "
                     + " WHERE gt.coFiscalPeriodId = "+periodID+" AND  gt.coFiscalYearId = "+yearID+" and gt.period<>13 GROUP BY gt.coAccountID) gl ON cA.coAccountID = gl.coAccountID AND cA.accountType IN ('Income','Expense') "
                     + " LEFT JOIN (SELECT  gtt.coAccountID, TRUNCATE(SUM(TRUNCATE(gtt.debit,4)),2) ydebits, TRUNCATE(SUM(TRUNCATE(gtt.credit,4)),2) ycredits FROM glTransaction gtt WHERE (gtt.period <= (SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+") AND gtt.coFiscalYearId ="+yearID+/*" and gtt.coFiscalYearId = "+yearID+ --- commented by leo ID# 497*/") and gtt.period<>13 GROUP BY gtt.coAccountID) "
                     + " gll ON cA.coAccountID = gll.coAccountID AND cA.accountType IN ('Income','Expense')  WHERE cA.InActive <> 1 AND pdebits IS NOT NULL OR pcredits IS NOT NULL OR ydebits IS NOT NULL OR ycredits IS NOT NULL "
                     + " HAVING ycredits>0 OR ydebits>0 ORDER BY cA.Number) subquery  ORDER BY FinancialStatus ";
             
             
            path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/ProfitLossNew.jrxml");
            filename="Profit and Loss.pdf";
            JasperDesign jd  = JRXmlLoader.load(path_JRXML);
            
            path_JRXML_CSV = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/ProfitLossNewCSV.jrxml");
            filename="Profit and Loss.pdf";
            JasperDesign jd_csv  = JRXmlLoader.load(path_JRXML_CSV);
            
            JRDesignQuery jdq=new JRDesignQuery();
           
            con = itspdfService.connectionForJasper();
            //Have to set Params
            params.put("StartDate", StartDate);
            params.put("EndDate", EndDate);
           
            List<?> accountList = itsDrillDownService.getProfitandlossdetails(accDetailsQueryString);
           
            params.put("HeaderText", accountList.get(2));
            Blob blob =(Blob)accountList.get(3);
            imageStream =blob.getBinaryStream();
            params.put("companyLogo",imageStream); //  accountList.get(3)
            params.put("AccountStatus", accStatus);
            
            accDetailsQueryString = "SELECT coAccountID,Number,Description,accountType,FinancialStatus,((pdebits+pcredits)*100)/"+accountList.get(0)+" as PeriodRatio,"
	            		 //+ "(pdebits+pcredits) AS PeriodAmount,"
	            		 +" IF(FinancialStatus=1,IF((pdebits-pcredits)>0,(pdebits+pcredits)*-1,(pdebits+pcredits)),IF((pdebits+pcredits)>0,(pdebits+pcredits),(pdebits+pcredits)*-1)) AS PeriodAmount,"
	            		 + "((ydebits+ycredits)*100)/"+accountList.get(1)+" AS YearRatio,"
            			// + "(ydebits+ycredits) AS YearAmount "
            			+" IF(FinancialStatus=1,IF((ydebits-ycredits)>0,(ydebits+ycredits)*-1,(ydebits+ycredits)),IF((ydebits+ycredits)>0,(ydebits+ycredits),(ydebits+ycredits)*-1)) AS YearAmount"
             			 + " FROM ("
                         + " SELECT  cA.coAccountID,cA.Number,cA.Description,cA.accountType,cA.FinancialStatus, IF(pdebits-pcredits>0,pdebits-pcredits,0) AS pdebits,IF(pcredits-pdebits>0,pcredits-pdebits,0) AS pcredits,"
                         + " IF(ydebits-ycredits>0,ydebits-ycredits,0) AS ydebits, IF(ycredits-ydebits>0,ycredits-ydebits,0) AS ycredits FROM coAccount cA LEFT JOIN "
                         + " (SELECT  gt.coAccountID, TRUNCATE(SUM(TRUNCATE(gt.debit,4)),2) pdebits,TRUNCATE(SUM(TRUNCATE(gt.credit,4)),2)pcredits FROM glTransaction gt "
                         + " WHERE gt.coFiscalPeriodId = "+periodID+" AND  gt.coFiscalYearId = "+yearID+" and gt.period<>13 GROUP BY gt.coAccountID) gl ON cA.coAccountID = gl.coAccountID AND cA.accountType IN ('Income','Expense') "
                         + " LEFT JOIN (SELECT  gtt.coAccountID, TRUNCATE(SUM(TRUNCATE(gtt.debit,4)),2) ydebits, TRUNCATE(SUM(TRUNCATE(gtt.credit,4)),2) ycredits FROM glTransaction gtt WHERE gtt.period <= (SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+") AND gtt.coFiscalYearId ="+yearID+/*" and gtt.coFiscalYearId = "+yearID+ --- commented by leo ID# 497*/" and gtt.period<>13 GROUP BY gtt.coAccountID) "
                         + " gll ON cA.coAccountID = gll.coAccountID AND cA.accountType IN ('Income','Expense') WHERE cA.InActive <> 1 AND pdebits IS NOT NULL OR pcredits IS NOT NULL OR ydebits IS NOT NULL OR ycredits IS NOT NULL "
                         + " HAVING ycredits>0 OR ydebits>0 ORDER BY cA.Number) subquery  ORDER BY FinancialStatus asc ,Number asc,Description asc ";
        
             System.out.println("accDetailsQueryString=="+accDetailsQueryString);
            jdq.setText(accDetailsQueryString);
            jd.setQuery(jdq);
            jd_csv.setQuery(jdq);
            connection = con.getConnection();
            if(viewflag.equals("pdf"))
            {
            filename="Profit and Loss.pdf";
            ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
            }
            else
            {
            filename="Profit and Loss.csv";
            ReportService.dynamicReportCall(theResponse,params,"xls",jd_csv,filename,connection);
            }

        } catch (SQLException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>asofdate:</b>"+periodEndingDate,"IncomeStatement",e,session,therequest);
		}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>asofdate:</b>"+periodEndingDate,"IncomeStatement",e,session,therequest);
		}
        finally
        {
        	if(imageStream!=null){
				imageStream.close();
			}
			if(con!=null){
			con.closeConnection(connection);
			con=null;
			}
        }
    }
	
	
	@RequestMapping(value = "/printBalanceSheet", method = RequestMethod.GET)
	public @ResponseBody
	void printBalanceSheet(
			@RequestParam(value = "showaccount", required = false) boolean showaccount,
			@RequestParam(value = "cofiscalperiodid", required = false) Integer cofiscalperiodid,
			@RequestParam(value = "asofdate", required = false) Date asofdate,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, SQLException, MessagingException, JRException, DrillDownException {
		Connection connection =null;
		InputStream imageStream =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
		

			Cofiscalperiod cofiscalperiod = null;
	
			cofiscalperiod = itsDrillDownService.getAllPeriodsbasedonDate(asofdate);
           
            String accDetailsQueryString = "";
            Integer periodID = cofiscalperiod.getCoFiscalPeriodId();
            Integer yearID = cofiscalperiod.getCoFiscalYearId();
           
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String StartDate = df.format(cofiscalperiod.getStartDate());
            String EndDate = df.format(cofiscalperiod.getEndDate());
            String AsOfDate = df.format(asofdate);
            boolean accStatus = true;
           
            if(showaccount)
                accStatus =true;
            else
                accStatus = false;
           
             accDetailsQueryString = "SELECT coAccountID,Number,Description,accountType,FinancialStatus,(pdebits+pcredits) AS PeriodAmount,(ydebits+ycredits) AS YearAmount FROM ("
                     + " SELECT  cA.coAccountID,cA.Number,cA.Description,cA.accountType,cA.FinancialStatus, IF(pdebits-pcredits>0,pdebits-pcredits,0) AS pdebits,IF(pcredits-pdebits>0,pcredits-pdebits,0) AS pcredits,"
                     + " IF(ydebits-ycredits>0,ydebits-ycredits,0) AS ydebits, IF(ycredits-ydebits>0,ycredits-ydebits,0) AS ycredits FROM coAccount cA LEFT JOIN "
                     + " (SELECT  gt.coAccountID, TRUNCATE(SUM(TRUNCATE(gt.debit,4)),2) pdebits,TRUNCATE(SUM(TRUNCATE(gt.credit,4)),2)pcredits FROM glTransaction gt "
                     + " WHERE "
                     +" (gt.fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearID+") "
                     +" or (gt.period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+")"
                      +" and gt.coFiscalYearId = "+yearID+"))"
                    // + "gt.coFiscalPeriodId <= "+periodID+/*" AND  gt.coFiscalYearId = "+yearID+*/" "
                    //+ "and gt.period<>13 "
                    + " GROUP BY gt.coAccountID) gl ON cA.coAccountID = gl.coAccountID AND cA.accountType IN ('Asset','Liability','Equity') "
                     + " LEFT JOIN (SELECT  gtt.coAccountID, TRUNCATE(SUM(TRUNCATE(gtt.debit,4)),2) ydebits, TRUNCATE(SUM(TRUNCATE(gtt.credit,4)),2) ycredits FROM glTransaction gtt WHERE "
                     //+ "gtt.coFiscalPeriodId <= "+periodID
                     +" (gtt.fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearID+") "
                     +" or (gtt.period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+")"
                     +" and gtt.coFiscalYearId = "+yearID+"))"
                     /*" and gtt.coFiscalYearId = "+yearID+ --- commented by leo ID# 497*/
                     //+" and gtt.period<>13 "
                     + " GROUP BY gtt.coAccountID) "
                     + " gll ON cA.coAccountID = gll.coAccountID AND cA.accountType IN ('Asset','Liability','Equity')  WHERE cA.InActive <> 1 AND pdebits IS NOT NULL OR pcredits IS NOT NULL OR ydebits IS NOT NULL OR ycredits IS NOT NULL "
                     + " HAVING ycredits>0 OR ydebits>0 ORDER BY cA.Number) subquery  ORDER BY FinancialStatus,Number ";
             
             
            path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/BalanceSheetNew.jrxml");
            filename="BalanceSheet.pdf";
            JasperDesign jd  = JRXmlLoader.load(path_JRXML);
            JRDesignQuery jdq=new JRDesignQuery();
           
            con = itspdfService.connectionForJasper();
            //Have to set Params
            params.put("accountnochkbx", showaccount);
			params.put("cofiscalperiodID", cofiscalperiodid);
			params.put("as_of", AsOfDate);
			
            params.put("StartDate", StartDate);
            params.put("EndDate", EndDate);
           
            List<?> accountList = itsDrillDownService.getProfitandlossdetails(accDetailsQueryString);
           
            params.put("HeaderText", accountList.get(2));
            Blob blob =(Blob)accountList.get(3);
            imageStream =blob.getBinaryStream();
            params.put("companyLogo",imageStream); //  accountList.get(3)
            params.put("AccountStatus", accStatus);
            BigDecimal netProfitLoss=itsDrillDownService.getNetProfitAmount(periodID);
            
            params.put("netProfitLoss", netProfitLoss);
            /* accDetailsQueryString = "SELECT coAccountID,Number,Description,accountType,FinancialStatus,((pdebits+pcredits)*100)/"+accountList.get(0)+" as PeriodRatio,(pdebits+pcredits) AS PeriodAmount,((ydebits+ycredits)*100)/"+accountList.get(1)+" AS YearRatio,(ydebits+ycredits) AS YearAmount "
             			 + " FROM ("
                         + " SELECT  cA.coAccountID,cA.Number,cA.Description,cA.accountType,cA.FinancialStatus, IF(pdebits-pcredits>0,pdebits-pcredits,0) AS pdebits,IF(pcredits-pdebits>0,pcredits-pdebits,0) AS pcredits,"
                         + " IF(ydebits-ycredits>0,ydebits-ycredits,0) AS ydebits, IF(ycredits-ydebits>0,ycredits-ydebits,0) AS ycredits FROM coAccount cA LEFT JOIN "
                         + " (SELECT  gt.coAccountID, TRUNCATE(SUM(TRUNCATE(gt.debit,4)),2) pdebits,TRUNCATE(SUM(TRUNCATE(gt.credit,4)),2)pcredits FROM glTransaction gt "
                         + " WHERE gt.coFiscalPeriodId <= "+periodID+" AND  gt.coFiscalYearId = "+yearID+" and gt.period<>13 GROUP BY gt.coAccountID) gl ON cA.coAccountID = gl.coAccountID AND cA.accountType IN ('Asset','Liability','Equity') "
                         + " LEFT JOIN (SELECT  gtt.coAccountID, TRUNCATE(SUM(TRUNCATE(gtt.debit,4)),2) ydebits, TRUNCATE(SUM(TRUNCATE(gtt.credit,4)),2) ycredits FROM  glTransaction gtt WHERE gtt.coFiscalPeriodId <= "+periodID+" and gtt.coFiscalYearId = "+yearID+ --- commented by leo ID# 497" and gtt.period<>13 GROUP BY gtt.coAccountID) "
                         + " gll ON cA.coAccountID = gll.coAccountID AND cA.accountType IN ('Asset','Liability','Equity') WHERE cA.InActive <> 1 AND pdebits IS NOT NULL OR pcredits IS NOT NULL OR ydebits IS NOT NULL OR ycredits IS NOT NULL "
                         + " HAVING ycredits>0 OR ydebits>0 ORDER BY cA.Number) subquery  ORDER BY FinancialStatus,Number ";
           */
            accDetailsQueryString="SELECT test.*,IF((FinancialStatus=4||FinancialStatus=5||FinancialStatus=6),1,2) AS overallaccounttype  FROM (SELECT coAccount.coAccountID,Number,Description,accountType,FinancialStatus,pdebits,pcredit,"
            		//+ "pdebits-pcredit AS PeriodAmount "
            		+"IF((FinancialStatus=4||FinancialStatus=5||FinancialStatus=6),(pdebits-pcredit),IF((FinancialStatus=7||FinancialStatus=8||FinancialStatus=9),((pdebits-pcredit)*-1),0)) AS PeriodAmount "
								+"FROM coAccount JOIN  "
								+"( "
								+"SELECT coAccountID,TRUNCATE(SUM(TRUNCATE(debit,4)),2) pdebits,TRUNCATE(SUM(TRUNCATE(credit,4)),2) pcredit " 
								+"FROM coAccount JOIN glTransaction USING(coAccountID)   "
								//+"WHERE coFiscalPeriodId <= "+periodID+" AND period<>13 AND accountType IN ('Asset','Liability','Equity') "
								+"WHERE "
								+" (fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearID+") "
			                    +" or (period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+")"
			                    +" and coFiscalYearId = "+yearID+"))"
								//+" AND period<>13 "
								+" AND InActive <> 1  AND accountType IN ('Asset','Liability','Equity') "
								
								+"GROUP BY coAccountID "
								+")AS periodwisetable USING(coAccountID) where accountType IN ('Asset','Liability','Equity') "
								+" HAVING PeriodAmount<>0 ORDER BY FinancialStatus,Number ) as test";
            
            accDetailsQueryString=accDetailsQueryString+" union  SELECT 0 AS coAccountID,NULL AS Number,'Net Profit (Loss)' AS Description,'Equity' AS accountType,9 AS FinancialStatus,"+netProfitLoss+" AS pdebits,0 AS pcredit ,"+netProfitLoss+" AS PeriodAmount,2 AS overallaccounttype FROM DUAL";
            System.out.println("accDetailsQueryString=="+accDetailsQueryString);
            jdq.setText(accDetailsQueryString);
            jd.setQuery(jdq);
            connection = con.getConnection();
            ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
		} catch (SQLException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>asofdate:</b>"+asofdate,"BalanceSheet",e,session,theRequest);
		}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>asofdate:</b>"+asofdate,"BalanceSheet",e,session,theRequest);
		}
		finally
		{
			if(imageStream!=null){
				imageStream.close();
			}
			if(con!=null){
			con.closeConnection(connection);
			con=null;
			}
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