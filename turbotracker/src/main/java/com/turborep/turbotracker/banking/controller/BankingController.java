
		
	/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.banking.controller;

import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.print.PrintService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.MoAccount;
import com.turborep.turbotracker.banking.dao.Momultipleaccount;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.dao.PrintCheckBean;
import com.turborep.turbotracker.banking.dao.ReconcileFLStatus;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.BankingService;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.finance.exception.DrillDownException;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebillpay;

@Controller
@RequestMapping("/banking")
public class BankingController {

	protected static Logger logger = Logger.getLogger(BankingController.class);
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name="bankingService")
	private BankingService bankingAccountsService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name = "chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@RequestMapping(value = "/bankingAccountsList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getBankingAccounts(@RequestParam(value="page", required=false) Integer thePage,
															@RequestParam(value="rows", required=false) Integer theRows,
															@RequestParam(value="sidx", required=false) String theSidx,
															@RequestParam(value="sord", required=false) String theSord,
															HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, CompanyException, UserException, BankingException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		CustomResponse aResponse = null;
		try {
			int aFrom = 0, aTo = 0;
			List<?> aBankingAccountsList = bankingAccountsService.getBankAccountList(aFrom, aTo);
			aResponse = new CustomResponse();
			aResponse.setRows(aBankingAccountsList);
			aResponse.setPage(thePage);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>TrackingID:</b>"+"bankingAccountsList","Banking.",e,session,request);
		} 
		return aResponse;
	}
	
	@RequestMapping(value = "/deleteBankingDetails", method = RequestMethod.POST)
	public @ResponseBody Boolean deleteBankingAccount(
			@RequestParam(value = "moAccountID", required = false) Integer moAccountID, 
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest request) throws IOException, MessagingException {
		Boolean isDeleted;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			isDeleted = bankingAccountsService.deleteBankingAccount(moAccountID);
		} catch (BankingException e) {
			sendTransactionException("<b>MoAccountID:</b>"+moAccountID,"Banking.",e,session,request);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return false;
		}
		return isDeleted;
	}
	
	@RequestMapping(value = "banking_bankdetails", method = RequestMethod.GET)
	public String getbankdetailsPage (ModelMap theModel,HttpSession session, 
			HttpServletResponse response,HttpServletRequest request) throws IOException, BankingException, MessagingException {
		
		
		TsUserLogin aUserLogin = null;
		UserBean aUserBean = null;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = itsUserService.getSingleUserDetails(aUserBean.getUserId());
				theModel.addAttribute("userDetails", aUserLogin);
				
					theModel.addAttribute("userSysAdmin",aUserLogin.getSystemAdministrator());
					theModel.addAttribute("coAccounts", (List<Coaccount>) chartOfAccountsService.getAccountNumber());
					BigDecimal payableApprove = chartOfAccountsService.getPayableApproved();
					theModel.addAttribute("payableApproved",payableApprove);
					theModel.addAttribute("coAccountsDetails",chartOfAccountsService.getListOfAccountsFordropdown());
					session.setAttribute(SessionConstants.BANKING_DETAILS,(List<MoAccount>) bankingAccountsService.getBankAccountDetails());
				}
			}
			catch (Exception e) {
				sendTransactionException("<b>Tracking ID:</b>"+"CoAccountList","Banking.",e,session,request);
			}
		
		return "banking/banking_bankdetails";
	}
	
	@RequestMapping(value = "banking_transaction", method = RequestMethod.GET)
	public String gettransactionPage (ModelMap theModel,HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, BankingException, MessagingException {
		
		
		TsUserLogin aUserLogin = null;
		UserBean aUserBean=null;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = itsUserService.getSingleUserDetails(aUserBean.getUserId());
				theModel.addAttribute("userDetails", aUserLogin);
			
					theModel.addAttribute("userSysAdmin",aUserLogin.getSystemAdministrator());
					theModel.addAttribute("coAccounts", (List<Coaccount>) chartOfAccountsService.getAccountNumber());
					BigDecimal payableApprove = chartOfAccountsService.getPayableApproved();
					theModel.addAttribute("payableApproved",payableApprove);
					theModel.addAttribute("coAccountsDetails",chartOfAccountsService.getListOfAccountsFordropdown());
					session.setAttribute(SessionConstants.BANKING_DETAILS,(List<MoAccount>) bankingAccountsService.getBankAccountDetails());
				}
			}
			catch (Exception e) {
				sendTransactionException("<b>MoTransactionID:</b>"+"banking_transaction","Banking.",e,session,request);
				 }
		return "banking/banking_transaction";
	}
	
	
	@RequestMapping(value = "/reconcileSelectedShow", method = RequestMethod.GET)
	public @ResponseBody ModelAndView reconcileSelectedShow(
			@RequestParam(value = "moAccountID", required = true) Integer moAccountID, HttpSession session,
			HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		MoAccount aMoAccount;
		UserBean aUserBean=null;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		ModelAndView theView = new ModelAndView("banking/reconcileAccountDetails");
		try {
			aMoAccount = bankingAccountsService.getSingleBankingDetails(moAccountID);
			
			theView.addObject("moaccount",aMoAccount);
					
			theView.addObject("openbalance",aMoAccount.getOpenBalance());
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoAccountID:</b>"+moAccountID,"Banking.",e,session,request);
			
			return null;
		}
		return theView;
	}
	
	/*
	 * undefined
	 */
	
	@RequestMapping(value = "/saveBankingDetailsDetails", method = RequestMethod.POST)
	public @ResponseBody Boolean saveBankingDetailsDetails(@RequestParam(value = "descriptionName", required = false) String theDescription,
															@RequestParam(value = "assetAccountName", required = false) Integer theAssetAccountName,
															@RequestParam(value = "depositDefultAccountName", required = false) Integer theDepositDefultAccountName,
															@RequestParam(value = "interstDefultAccountName", required = false) Integer theInterstAccountName,
															@RequestParam(value = "feesDefultAccountName", required = false) Integer theFeesAccName,
															@RequestParam(value = "accountHolder1Name", required = false) String theAccountHolder1Name,
															@RequestParam(value = "accountHolder2Name", required = false) String theAccountHolder2Name,
															@RequestParam(value = "accountHolder3Name", required = false) String theAccountHolder3Name,
															@RequestParam(value = "accountHolder4Name", required = false) String theAccountHolder4Name,
															@RequestParam(value = "accountHolder5Name", required = false) String theAccountHolder5Name,
															@RequestParam(value = "bankName1Name", required = false) String theBankName1,
															@RequestParam(value = "bankName2Name", required = false) String theBankName2,
															@RequestParam(value = "bankName3Name", required = false) String theBankName3,
															@RequestParam(value = "bankName4Name", required = false) String theBankName4,
															@RequestParam(value = "bankName5Name", required = false) String theBankName5,
															@RequestParam(value = "ckCodeName", required = false) String theCKName,
															@RequestParam(value = "typeName", required = false) Short theTypeID,
															@RequestParam(value = "ABARoutingName", required = false) String theABAAccountName,
															@RequestParam(value = "accountNumberName", required = false) String theAccNumber,
															@RequestParam(value = "inActiveBox", required = false) boolean theInActive,
															@RequestParam(value = "moAccountID", required = false) Integer theMoAccID,
															@RequestParam(value = "token", required = false) String theToken,
															@RequestParam(value = "nextCheckNumber", required = false) Integer theNextCheckNumber,
															@RequestParam(value = "LogoYN", required = false) Boolean LogoYN,
															@RequestParam(value = "LineNo", required = false) Integer LineNo,
															HttpServletResponse theResponse,HttpServletRequest request,
															HttpSession session) throws IOException, MessagingException {
		MoAccount aMoaccount = new MoAccount();
		UserBean aUserBean=null;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try{
			
			logger.info("Testing-->"+theTypeID+"||ActiveStatus::"+theInActive);
			
			byte aInActive = (byte) (theInActive?1:0);
			aMoaccount.setDescription(theDescription);
			aMoaccount.setCoAccountIDAsset(theAssetAccountName);
			aMoaccount.setCoAccountIDDeposits(theDepositDefultAccountName);
			aMoaccount.setCoAccountIDInterest(theInterstAccountName);
			aMoaccount.setCoAccountIDFees(theFeesAccName);
			aMoaccount.setCompany1(theAccountHolder1Name);
			aMoaccount.setCompany2(theAccountHolder2Name);
			aMoaccount.setCompany3(theAccountHolder3Name);
			aMoaccount.setCompany4(theAccountHolder4Name);
			aMoaccount.setCompany5(theAccountHolder5Name);
			aMoaccount.setBank1(theBankName1);
			aMoaccount.setBank2(theBankName2);
			aMoaccount.setBank3(theBankName3);
			aMoaccount.setBank4(theBankName4);
			aMoaccount.setBank5(theBankName5);
			aMoaccount.setCheckCode(theCKName);
			aMoaccount.setAccountType(theTypeID);
			aMoaccount.setRoutingNumber(theABAAccountName);
			aMoaccount.setAccountNumber(theAccNumber);
			aMoaccount.setInActive(aInActive);
			System.out.println("LogoYN" + LogoYN + "\t LineNO" + LineNo);
			aMoaccount.setLogoYN(LogoYN !=null ? LogoYN : false);
			aMoaccount.setLineNo(LineNo);
			
			if(theNextCheckNumber == null){
				theNextCheckNumber = 1;
			}
			aMoaccount.setNextCheckNumber(theNextCheckNumber);
			if(theToken.equalsIgnoreCase("new")){
				bankingAccountsService.addBankingDetailsDetails(aMoaccount);
			}else{
				aMoaccount.setMoAccountId(theMoAccID);
				bankingAccountsService.updateBankingDetailsDetails(aMoaccount);
			}
		}catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>AccNumber:</b>"+theAccNumber
					 +"<br><b>AccountID:</b>"+theMoAccID,"Banking.",e,session,request);
			return false;
		}
		return true;
	}
	/*Updated By:Velmurugan
	 *Updated On:28-08-2014
	 *Description:added the current amount in  previous balance value and set for each record 
	 * */
	@RequestMapping(value = "/transactionRegisterList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getTransactionRegisterList(@RequestParam(value="page", required=false) Integer thePage,
																	@RequestParam(value="rows", required=false) Integer theRows,
																	@RequestParam(value="sidx", required=false) String theSidx,
																	@RequestParam(value="sord", required=false) String theSord,
																	@RequestParam(value="transactionDetails[]", required=false) ArrayList<?> theTransactionDetails,
																	@RequestParam(value="moAccountID", required=false) Integer moAccountID,
																	HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, CompanyException, UserException, BankingException, MessagingException {
		String aBankingAccount = "bankingAccount";
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		int aTotalCount = bankingAccountsService.getRecordChartsCount(theTransactionDetails,moAccountID);
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			aUserLogin = new TsUserLogin();
			aUserLogin.setBankingperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aBankingAccount);
			int aFrom, aTo;
			
			System.out.println(thePage +"==="+theRows );
		//	theRows = 50;
			
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<Motransaction> aTransactionRegisterList = bankingAccountsService.getTransactionRegisterList(aFrom, aTo, theTransactionDetails,moAccountID);
			
			//List<Motransaction> lst = new ArrayList<Motransaction>();
			//BigDecimal bal=bankingAccountsService.getpageinitialamount(aTo+1,aTotalCount,moAccountID);
			
			/*BigDecimal bal= new BigDecimal(0.0);
			for( int i= aTransactionRegisterList.size();i >0;i--){
				Motransaction dto = aTransactionRegisterList.get(i-1);
				bal = bal.add(dto.getAmount());
				dto.setBalance(bal);
				lst.add(dto);
			}*/
			aResponse = new CustomResponse();
			//aResponse.setRows(aTransactionRegisterList);
			//Collections.reverse(lst);
			aResponse.setRows(aTransactionRegisterList);
			aResponse.setRecords(String.valueOf(aTransactionRegisterList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>MoAccountID:</b>"+moAccountID,"Banking.",e,session,request);
			
		} 
		return aResponse;
	}
	
	
	@RequestMapping(value = "/findbytransactionRegisterList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse findbytransactionRegisterList(@RequestParam(value="page", required=false) Integer thePage,
																	  @RequestParam(value="rows", required=false) Integer theRows,
																	@RequestParam(value="sidx", required=false) String theSidx,
																	@RequestParam(value="sord", required=false) String theSord,
																	@RequestParam(value="filterDetails[]", required=false) ArrayList<?> thefilterDetails,
																	@RequestParam(value="moAccountID", required=false) Integer moAccountID,
																	HttpSession session, HttpServletRequest request,HttpServletResponse response,HttpSession aSession) throws IOException, CompanyException, UserException, BankingException, MessagingException {
		CustomResponse aResponse = null;
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		int aTotalCount =0;
		int aFrom, aTo;
		
		try {
			aTotalCount = bankingAccountsService.getcountfindbytransactionRegisterList(thefilterDetails,moAccountID);
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<Motransaction> aTransactionRegisterList =new ArrayList<Motransaction>();
			System.out.println("thefilterDetails==============="+thefilterDetails);
			if(thefilterDetails!=null){
				aTransactionRegisterList =new ArrayList<Motransaction>(bankingAccountsService.findbytransactionRegisterList(aFrom, aTo,thefilterDetails,moAccountID));
			}
			aResponse = new CustomResponse();
			aResponse.setRows(aTransactionRegisterList);
			aResponse.setRecords(String.valueOf(aTransactionRegisterList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>MoAccountID:</b>"+moAccountID,"Banking.",e,session,request);
		} 
		return aResponse;
	}
	
	
	@RequestMapping(value = "/printBankingFilter", method = RequestMethod.GET)
	public @ResponseBody
	void printBankingFilter(
			@RequestParam(value="filterArray[]", required=false) ArrayList<?> thefilterDetails,
			@RequestParam(value="moAccountID", required=false) Integer moAccountID,
			@RequestParam(value="viewType", required=false) String viewType,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, CompanyException, UserException, BankingException, MessagingException, JRException, SQLException {
		Connection connection =null;
		ConnectionProvider con=null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;

			System.out.println(thefilterDetails);
            path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/BankingFilter.jrxml");
          
            JasperDesign jd  = JRXmlLoader.load(path_JRXML);
            JRDesignQuery jdq=new JRDesignQuery();
           
           con = itspdfService.connectionForJasper();
             params.put("delete", theRequest.getSession().getServletContext().getRealPath("/resources/images/delete.png"));
             params.put("wright", theRequest.getSession().getServletContext().getRealPath("/resources/images/greenTick.png"));
            String querString = "";
            
        	if(thefilterDetails!=null){
        		querString =bankingAccountsService.getfindbytransactionQueryString(thefilterDetails,moAccountID);
			}
           
            System.out.println("FilterQueryString=="+querString);
            jdq.setText(querString);
            jd.setQuery(jdq);
            connection = con.getConnection();
            if(viewType.equals("pdf"))
            {
            filename="BankingFilter.pdf";
            ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
            }
            else
            {
            filename = "BankingFilter.csv";
            ReportService.dynamicReportCall(theResponse,params,"xls",jd,filename,connection);
            }
            
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("Filter Csv/PDF ","BankingController",e,session,theRequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
			
		}
	}
	
	
	
	
	@RequestMapping(value = "/singleAcccountDetails", method = RequestMethod.POST)
	public @ResponseBody MoAccount singleAcccountDetails(@RequestParam(value = "moAccountID", required = false) Integer moAccountID, 
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		MoAccount aMoaccount = new MoAccount();
		try {
			aMoaccount = bankingAccountsService.getSingleBankingDetails(moAccountID);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoAccountID:</b>"+moAccountID,"Banking.",e,session,request);
			
		}
		return aMoaccount;
	}
	
	
	@RequestMapping(value = "/getcoFiscalPeriod", method = RequestMethod.GET)
	public @ResponseBody Cofiscalperiod getcoFiscalPeriod(HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws IOException, CompanyException, MessagingException {
		Cofiscalperiod cofiscalperiod  = new Cofiscalperiod();
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try {
			
			cofiscalperiod.setStartDate(accountingCyclesService.getOpenCofiscalperiod());
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> getcoFiscalPeriod","Banking.",e,aSession,request);
			
			 Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Transaction Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>Tracking ID:</b> getcoFiscalPeriod");
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Banking.");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,request);
			
		}
		return cofiscalperiod;
	}
	
	@RequestMapping(value = "/getopenPeriods", method = RequestMethod.GET)
	public @ResponseBody Cofiscalperiod getopenPeriods(HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws IOException, CompanyException, MessagingException {
		Cofiscalperiod cofiscalperiod  = new Cofiscalperiod();
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try {
			
			cofiscalperiod.setStartDate(accountingCyclesService.getopenPeriods());
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> getcoFiscalPeriod","Banking.",e,aSession,request);
			
			 Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Transaction Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>Tracking ID:</b> getcoFiscalPeriod");
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Banking.");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,request);
			
		}
		return cofiscalperiod;
	}
	
	
	
	
	/*updated by:Velmurugan
	  *updated on:31-08-2014
	  *Description: when adding a new transaction one row has been inserted in motransaction table
	  *while edit the transaction two rows has been inserter one is for nullify the old record and 
	  *another one is for new edit amount has been inserted in table.
	  *while delete the transaction one row has been inserted for nullify the amount .
	  *deposit -withdrawel insert
	  *withdrawel - deposit insert
	  * */
	@RequestMapping(value = "/saveTransactionDetails", method = RequestMethod.POST)
	public @ResponseBody Integer saveTransactionDetails(@RequestParam(value = "transactionDetails[]", required = false) ArrayList<?> theTransactionDetails, 
			@RequestParam(value = "editamount", required = false) BigDecimal editamount,
			@RequestParam(value = "oldamount", required = false) BigDecimal oldamount,
			@RequestParam(value = "currentbalance", required = false) BigDecimal currentbalance,
			@RequestParam(value = "resultingbalance", required = false) BigDecimal resultingbalance,
			@RequestParam(value = "NewMoTypeId", required = false) String NewMoTypeId,
			@RequestParam(value = "coFiscalPeriodId", required = false) Integer periodID,
			@RequestParam(value = "coFiscalYearId", required = false) Integer yearID,
			@RequestParam(value = "gridData", required = false) String gridData,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest request) throws IOException, ParseException ,BankingException, CompanyException, MessagingException{
		Motransaction aMotransaction = new Motransaction();
		Byte aBigDecimal = 0;
		boolean accountStatus=false;
		Byte printed = 0;
		Byte void_ = 0;
		Byte tempRec =0;
		String aTypeID = "";
		Date transdate=null;
		Date currdate=null;
		boolean futureornot=true;
		int motransiid=0;
		String DepositType="Single";
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		
		try {
			if(theTransactionDetails.get(0) != "" && theTransactionDetails.get(0) != null){
				
				aMotransaction.setTransactionDate(DateUtils.parseDate( (String) theTransactionDetails.get(0), new String[]{"MM/dd/yyyy"}));
				//transdate=DateUtils.parseDate( (String) theTransactionDetails.get(0), new String[]{"yyyy-MM-dd"});
				
				Date myDate = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateAsString_format = simpleDateFormat.format(myDate);
				if(myDate.compareTo(aMotransaction.getTransactionDate())<0 || myDate.compareTo(aMotransaction.getTransactionDate())==0){
					//System.out.println("transdate date is greater than todate");
					futureornot=false;
				}else{
					futureornot=true;
					//System.out.println("transdate date is lesser than todate");
				}
			}
			
			if(theTransactionDetails.get(1) != "" && theTransactionDetails.get(1) != null){
				aTypeID = theTransactionDetails.get(1).toString(); 
				aMotransaction.setMoTransactionTypeId(Short.parseShort(aTypeID));
			}
			if(theTransactionDetails.get(2) != "" && theTransactionDetails.get(2) != null){
				aMotransaction.setReference((String) theTransactionDetails.get(2));
			}
			if(theTransactionDetails.get(3) != "" && theTransactionDetails.get(3) != null){
				String aDescription = theTransactionDetails.get(3).toString().replaceAll("\n", "").replaceAll("\t", "");
				aMotransaction.setDescription(aDescription);
			}
			if(theTransactionDetails.get(4) != "" && theTransactionDetails.get(4) != null){
				String aCoAccountID = theTransactionDetails.get(4).toString();
				aMotransaction.setCoAccountId(Integer.parseInt(aCoAccountID));
				System.out.println("===========>>="+aCoAccountID);
				if(!aCoAccountID.equals("-1"))
				accountStatus=true;
			}
			if(theTransactionDetails.get(5) != "" && theTransactionDetails.get(5) != null){
				String aReconcil = theTransactionDetails.get(5).toString();
				aMotransaction.setReconciled(Byte.valueOf(aReconcil));
			}
			if(theTransactionDetails.get(6) != "" && theTransactionDetails.get(6) != null){
				String aAmount = "";
				String theAmount="";
				if(theTransactionDetails.get(6).equals("0")){
					aAmount = "0";
				}else{
					if(aTypeID.equalsIgnoreCase("0")){
						aAmount = theTransactionDetails.get(6).toString();
					}else{
						aAmount ="-"+theTransactionDetails.get(6).toString();
					}
				}
				BigDecimal aPrice = new BigDecimal(aAmount);
				aMotransaction.setAmount(aPrice);
			}
			if(theTransactionDetails.get(9) != "" && theTransactionDetails.get(9) != null){
				String aMoAccountID = theTransactionDetails.get(9).toString();
				aMotransaction.setMoAccountId(Integer.valueOf(aMoAccountID));
			}
			String aOperation = theTransactionDetails.get(7).toString();
			Motransaction mts=	bankingAccountsService.getResultingBalance(aMotransaction.getMoAccountId());
			currentbalance = mts.getCurrentbalance();
			
			String aMemo=theTransactionDetails.get(11).toString();
			String aRxMasterID=theTransactionDetails.get(12).toString();
			String aRxAddressID=theTransactionDetails.get(13).toString();
			
			aMotransaction.setDirectDeposit(aBigDecimal);
			aMotransaction.setPrinted(printed);
			aMotransaction.setVoid_(void_);
			aMotransaction.setTempRec(tempRec);
			
			System.out.println("aOperation---"+aMotransaction.getMoAccountId()+"--->"+aOperation);
			//aMotransaction.setBalance(currentbalance);
			MoAccount moAccount=new MoAccount();
			moAccount.setMoAccountId(aMotransaction.getMoAccountId());
			moAccount.setOper(aOperation);
			
		/**
		 * Created By :Leo   Description:New Check Creation
		 * 
		 * Date:28-10-2014
		 * 
		 * */	
			
		/*---------------------------New Check----------------------*/	
			
			if(aOperation.equalsIgnoreCase("new")){
				
				moAccount.setMoTransactionTypeId(aMotransaction.getMoTransactionTypeId());
				BigDecimal balance=new BigDecimal("0.00");
				if(aMotransaction.getMoTransactionTypeId()==0){
					moAccount.setAdditions(aMotransaction.getAmount());
					
					//Commented by: Leo purpose:BID#1326
					/*if(futureornot){
						balance=currentbalance.add(aMotransaction.getAmount());
						}else{
						balance=currentbalance;
						}*/
					balance=currentbalance.add(aMotransaction.getAmount());
					
				}else{
					moAccount.setSubtractions(aMotransaction.getAmount());
					/*if(futureornot){
						balance=currentbalance.add(aMotransaction.getAmount());
						}else{
						balance=currentbalance;
						}*/
					balance=currentbalance.add(aMotransaction.getAmount());
					
				}
				aMotransaction.setMemo(aMemo);
				aMotransaction.setRxMasterId(Integer.parseInt(aRxMasterID));
				if(aRxAddressID != "" && aRxAddressID != null)
					aMotransaction.setRxAddressId(bankingAccountsService.getRxAddressIDfmRxmasterID(Integer.parseInt(aRxMasterID)));
				
				motransiid = bankingAccountsService.createtransactionBanknewCheck(moAccount, aMotransaction, DepositType, yearID, periodID, aUserBean.getFullName(), 
						balance,accountStatus,motransiid,aOperation,"",false,null,null,"",null, "", "");
				
			}
			
			/*---------------------------New Transaction----------------------*/
			
			 else if(aOperation.equalsIgnoreCase("add")){
				 
				moAccount.setMoTransactionTypeId(aMotransaction.getMoTransactionTypeId());
				BigDecimal balance=new BigDecimal("0.00");
				if(aMotransaction.getMoTransactionTypeId()==0){
					moAccount.setAdditions(aMotransaction.getAmount());
					/*if(futureornot){
						balance=currentbalance.add(aMotransaction.getAmount());
						}else{
						balance=currentbalance;
						}*/
					balance=currentbalance.add(aMotransaction.getAmount());
				}else{
					moAccount.setSubtractions(aMotransaction.getAmount());
					/*if(futureornot){
						balance=currentbalance.add(aMotransaction.getAmount());
						}else{
						balance=currentbalance;
						}*/
					
					balance=currentbalance.add(aMotransaction.getAmount());
					
				}
				motransiid = bankingAccountsService.createtransactionBanknewCheck(moAccount, aMotransaction, DepositType, yearID, periodID, aUserBean.getFullName(), 
						balance,accountStatus,motransiid,aOperation,gridData,false,null,null,"",null, "", "");
				
			}
			/*---------------------------Edit Transaction----------------------*/
			 else if(aOperation.equalsIgnoreCase("edit")){
				Motransaction M1 = new Motransaction();
				M1 = aMotransaction;
				boolean motransaceditStatus=false; 
				BigDecimal balance=new BigDecimal("0.00");
				short transtypid=M1.getMoTransactionTypeId();
				BigDecimal amt=M1.getAmount();
				moAccount.setMoTransactionTypeId(M1.getMoTransactionTypeId());
				if(theTransactionDetails.get(8) != "" && theTransactionDetails.get(8) != null){
					String aTransID = theTransactionDetails.get(8).toString();
					M1.setMoTransactionId(Integer.valueOf(aTransID));
				}
				M1.setStatus(true);
				
				Motransaction checkforTestMotrans=new Motransaction();
				
				checkforTestMotrans=bankingAccountsService.getMOtransactionDetails(aMotransaction.getMoTransactionId());
				
				if(checkforTestMotrans!=null)
				{
					if(!(M1.getMoTransactionTypeId().equals(checkforTestMotrans.getMoTransactionTypeId())))
					{
						motransaceditStatus=true;
					}
					else if(!M1.getTransactionDate().equals(checkforTestMotrans.getTransactionDate()))
					{
						motransaceditStatus=true;
					}
					else if(!(M1.getReference()==null?"":M1.getReference()).equals(checkforTestMotrans.getReference()))
					{
						motransaceditStatus=true;
					}
					else if(!(M1.getDescription()==null?"":M1.getDescription()).equals(checkforTestMotrans.getDescription()))
					{
						motransaceditStatus=true;
					}
					else if(!M1.getReconciled().equals(checkforTestMotrans.getReconciled()))
					{
						motransaceditStatus=true;
					}
					else if(!M1.getAmount().equals(checkforTestMotrans.getAmount()))
					{
						motransaceditStatus=true;						
					}
					else if(!M1.getCoAccountId().equals(checkforTestMotrans.getCoAccountId()))
					{
						motransaceditStatus=true;						
					}
					
				}
					
				motransiid = bankingAccountsService.createtransactionBanknewCheck(moAccount, M1, DepositType, yearID, periodID, aUserBean.getFullName(), 
						balance,accountStatus,motransiid,aOperation,gridData,futureornot,currentbalance,oldamount,NewMoTypeId,amt,aMemo,aRxMasterID);
			
			}
			
			/*---------------------------Delete Transaction----------------------*/
			else if(aOperation.equalsIgnoreCase("delete")){
				
				Motransaction dM1 = new Motransaction();
				dM1 = aMotransaction;
				
				if(theTransactionDetails.get(8) != "" && theTransactionDetails.get(8) != null){
					String aTransID = theTransactionDetails.get(8).toString();
					dM1.setMoTransactionId(Integer.valueOf(aTransID));
				}
				if(aRxMasterID != "" && aRxMasterID != null)
					dM1.setRxMasterId(Integer.parseInt(aRxMasterID));
				if(aRxAddressID != "" && aRxAddressID != null)
					dM1.setRxAddressId(Integer.parseInt(aRxAddressID));
				
				moAccount.setMoAccountId(dM1.getMoAccountId());
				moAccount.setMoTransactionTypeId(dM1.getMoTransactionTypeId());
				dM1.setStatus(true);
				
				bankingAccountsService.createtransactionBanknewCheck(moAccount, dM1,DepositType, yearID, periodID, aUserBean.getFullName(), BigDecimal.ZERO,
						accountStatus,motransiid,aOperation,"",futureornot,currentbalance,null,"",null, "", "");
			}
			else if(aOperation.equalsIgnoreCase("void")){
				
				Motransaction vM1 = new Motransaction();
				vM1 = aMotransaction;
				
				if(theTransactionDetails.get(8) != "" && theTransactionDetails.get(8) != null){
					String aTransID = theTransactionDetails.get(8).toString();
					vM1.setMoTransactionId(Integer.valueOf(aTransID));
				}
				if(theTransactionDetails.get(10) != "" && theTransactionDetails.get(10) != null){
					String aVoid = theTransactionDetails.get(10).toString();
					vM1.setVoid_(Byte.valueOf(aVoid));
				}
				
			    if(aRxMasterID != "" && aRxMasterID != null)
			    	vM1.setRxMasterId(Integer.parseInt(aRxMasterID));
				if(aRxAddressID != "" && aRxAddressID != null)
					vM1.setRxAddressId(Integer.parseInt(aRxAddressID));
				vM1.setStatus(true);
				
				moAccount.setMoAccountId(vM1.getMoAccountId());
				moAccount.setMoTransactionTypeId(vM1.getMoTransactionTypeId());
				
				bankingAccountsService.createtransactionBanknewCheck(moAccount, vM1, DepositType, yearID, periodID, aUserBean.getFullName(), BigDecimal.ZERO,
						accountStatus,motransiid,aOperation,"",futureornot,currentbalance,null,"",null, "", "");
			}
			
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoTransactionID:</b>"+aMotransaction.getMoTransactionId()+"<br><b>RxMasterID:</b>"+aMotransaction.getRxMasterId(),"Banking.",e,session,request);
		}
		return motransiid;
	}
	@RequestMapping(value = "/printCheckDetails", method = RequestMethod.GET)
	public @ResponseBody void printCheckDetails(HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try{
			bankingAccountsService.printCheckDetails(theResponse,request);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> printCheckDetails","Banking.",e,aSession,request);
			
		}
	}
	
	
	@RequestMapping(value = "/newCheckDetails", method = RequestMethod.GET)
	public @ResponseBody void newCheckDetails(HttpServletResponse theResponse,HttpServletRequest request,
			HttpSession aSession) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try{
			bankingAccountsService.newCheckDetails(theResponse,request);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID :</b> newCheckDetails","Banking.",e,aSession,request);
		}
	}
	
	@RequestMapping(value = "/creditCheckDetails", method = RequestMethod.GET)
	public @ResponseBody void creditCheckDetails(@RequestParam(value = "moAccountId", required = false) Integer moAccountId,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try{
			bankingAccountsService.creditCheckDetails(theResponse,request);
			bankingAccountsService.deleteVeBillPayDetails(moAccountId);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>MoAccountId:</b>"+moAccountId,"Banking.",e,aSession,request);
		}
	}
	
	@RequestMapping(value = "/clearPayBills", method = RequestMethod.POST)
	public @ResponseBody void clearPayBills(@RequestParam(value = "moAccountId", required = false) Integer moAccountId,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try{
			bankingAccountsService.deleteVeBillPayDetails(moAccountId);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoAccountId:</b>"+moAccountId,"Banking.",e,aSession,request);
		}
	}
	
	@RequestMapping(value = "/rePrintCheckDetails", method = RequestMethod.GET)
	public @ResponseBody void rePrintCheckDetails(
			@RequestParam(value = "moAccountId", required = false) Integer bankAccountsID,
			@RequestParam(value = "checksDate", required = false) Date checkDate,
			@RequestParam(value = "checktype", required = false) String checkType, 
			@RequestParam(value = "newCheckNo", required = false) String checkNumber,
			@RequestParam(value = "checkStartId", required = true)String checkStartId,
			@RequestParam(value = "checkEndId", required = true) String checkEndId,
			@RequestParam(value = "lastNo", required = true) String lastNo,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession aSession) throws IOException, MessagingException {
		Motransaction aMotransaction = new Motransaction();
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try{
			if(checkType.equalsIgnoreCase("Vendorchecks"))
				aMotransaction.setCheckType((short) 1);
			else
			aMotransaction.setCheckType((short) 2);
			aMotransaction.setTransactionDate(checkDate);
			aMotransaction.setMoAccountId(bankAccountsID);
			aMotransaction.setMoTransactionTypeId((short) 2);
			aMotransaction.setPrinted((byte) 1);
			bankingAccountsService.rePrintCheck(aMotransaction,checkNumber,lastNo);
			bankingAccountsService.printCheckDetails(theResponse,request);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoTransactionID:</b>"+aMotransaction.getMoTransactionId()
					 +"<br><b>RxMasterID:</b>"+aMotransaction.getRxMasterId(),"Banking.",e,aSession,request);
			
		}
	}
	
	
	@RequestMapping(value = "/rePrintCheck", method = RequestMethod.POST)
	public @ResponseBody Motransaction rePrintChecks(
			@RequestParam(value = "moAccountId", required = false) Integer bankAccountsID,
			@RequestParam(value = "checksDate", required = false) Date checkDate,
			@RequestParam(value = "checktype", required = false) String checkType, 
			@RequestParam(value = "newCheckNo", required = false) BigInteger checkNumber,
			@RequestParam(value = "checkStartId", required = true)BigInteger checkStartId,
			@RequestParam(value = "checkEndId", required = true) BigInteger checkEndId,
			@RequestParam(value = "coFiscalPeriodId", required = true) Integer periodID,
			@RequestParam(value = "coFiscalYearId", required = true) Integer yearID,
			HttpSession aSession,HttpServletRequest request,HttpServletResponse theResponse) throws IOException, MessagingException {
			Motransaction aMotransaction = new Motransaction();
			UserBean aUserBean;
			aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try {
			if(checkType.equalsIgnoreCase("Vendorchecks"))
				aMotransaction.setCheckType((short) 1);
			else
			aMotransaction.setCheckType((short) 2);
			aMotransaction.setTransactionDate(checkDate);
			aMotransaction.setMoAccountId(bankAccountsID);
			aMotransaction.setMoTransactionTypeId((short) 2);
			aMotransaction.setPrinted((byte) 1);
			BigInteger lastData =  bankingAccountsService.rePrintChecksInsert(aMotransaction,checkStartId,checkEndId,checkNumber,yearID,periodID,aUserBean.getFullName());
			aMotransaction.setLastCheckNo(lastData);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoTransactionID:</b>"+aMotransaction.getMoTransactionId()
					 +"<br><b>RxMasterID:</b>"+aMotransaction.getRxMasterId(),"Banking.",e,aSession,request);
			
		}
		return aMotransaction;
	}
	
	@RequestMapping(value = "/printCheck", method = RequestMethod.POST)
	//public @ResponseBody Motransaction printChecks(@RequestParam(value = "bankAccounts", required = false) Integer bankAccountsID,
	public @ResponseBody String printChecks(@RequestParam(value = "bankAccounts", required = false) Integer bankAccountsID,
												@RequestParam(value = "checksDate", required = false) Date checkDate,
												@RequestParam(value = "checkType", required = false) String checkType, 
												@RequestParam(value = "checkNumber", required = false) Integer checkNumber,
												@RequestParam(value = "coFiscalPeriodId", required = false) Integer periodid,
												@RequestParam(value = "coFiscalYearId", required = false) Integer yearid,
												HttpSession aSession,HttpServletRequest request,HttpServletResponse theResponse) throws IOException, CompanyException, MessagingException {
		Motransaction aMotransaction = new Motransaction();
		PrintCheckBean aPrintBean = new PrintCheckBean();
		String status ="";
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		System.out.println(bankAccountsID);
		
		try {
			
			if(checkType.equalsIgnoreCase("Vendorchecks"))
				aMotransaction.setCheckType((short) 1);
			else
				aMotransaction.setCheckType((short) 2);
			
			aMotransaction.setMoAccountId(bankAccountsID);
			
			// Get coAccountID
			Coaccount coaccount = gltransactionService.getcoAccountDetails(aMotransaction);
			
			// Get CurrentAccount Balance
			Motransaction motrans=	bankingAccountsService.getResultingBalance(bankAccountsID);
			
		
			aMotransaction.setTransactionDate(checkDate);		
			
			Date myDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateAsString_format = simpleDateFormat.format(myDate);
			
			if(myDate.compareTo(aMotransaction.getTransactionDate())<0 || myDate.compareTo(aMotransaction.getTransactionDate())==0){
				aMotransaction.setBalance(motrans.getResultingbalance());
			}else{
				aMotransaction.setBalance(motrans.getCurrentbalance());
			}
			
			
			aMotransaction.setCoAccountId(coaccount.getCoAccountId());
			aMotransaction.setPrinted((byte) 1);
			aMotransaction.setMoTransactionTypeId((short) 2);
			
			aPrintBean = bankingAccountsService.printCheck(aMotransaction,checkNumber,yearid,periodid,aUserBean.getFullName(),aUserBean.getUserId());
			
			status = aPrintBean.getReference();
			
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MoTransactionID:</b>"+aMotransaction.getMoTransactionId()
					 +"<br><b>RxMasterID:</b>"+aMotransaction.getRxMasterId(),"Banking.",e,aSession,request);
			
			
		}
		
		
		return status;
	}
	

	@RequestMapping(value = "/getMaxReference", method = RequestMethod.GET)
	public @ResponseBody String getMaxReference(
			@RequestParam(value = "accountID", required = false) Integer AccountID,
			ModelMap modal, HttpServletRequest request)
			throws BankingException {
		
		  	BigInteger newReferenc = bankingAccountsService.getMaxCheckList(AccountID);
			modal.addAttribute("newRefID",newReferenc);

			return newReferenc+"";
	}
	
	
	@RequestMapping(value = "/selectprinter", method = RequestMethod.POST)
	public @ResponseBody String selectPrinter(@RequestParam(value = "bankAccounts", required = false) Integer bankAccountsID,
												@RequestParam(value = "checksDate", required = false) Date checkDate,
												@RequestParam(value = "checkType", required = false) String checkType, 
												@RequestParam(value = "checkNumber", required = false) Integer checkNumber,HttpServletResponse theResponse) throws IOException {
		Motransaction aMotransaction = new Motransaction();
		
		 	PrintService[] printServices = PrinterJob.lookupPrintServices();
		 	StringBuilder sbr = new StringBuilder();
		 	String sep = "";
	        for (PrintService printService : printServices) {
	        	sbr.append(sep);
	        	sbr.append(printService.getName());
	        	sep=",";
	        }
		return sbr.toString();
	}
	
	@RequestMapping(value = "/getaddress", method = RequestMethod.GET)
	public @ResponseBody Rxaddress getAddressforCheck(@RequestParam(value = "rxMasterID", required = false) Integer arxMasterID,
			HttpSession aSession,HttpServletRequest request,HttpServletResponse theResponse) throws IOException, MessagingException {
		Rxaddress aRxaddress = new Rxaddress();
		UserBean aUserBean;
		aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
		try{
			aRxaddress = bankingAccountsService.getAddressFromTransactionId(arxMasterID);
			
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<br><b>RxMasterID:</b>"+arxMasterID,"Banking.",e,aSession,request);
		}
		return aRxaddress;
	}
	//tempRecTransactionDetails
	@RequestMapping(value = "getReconcileTransactions",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getReconcileTransactions(
			@RequestParam(value = "moAccountID", required = true) Integer moAccountID,
			@RequestParam(value = "transactionType", required = true) Integer transactionType,
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			HttpServletResponse theResponse) throws  IOException, BankingException{
		CustomResponse aResponse = new CustomResponse();
			logger.info("moAccountID"+moAccountID+"\n"+transactionType+"\nthePage "+thePage+"\n theRows "+theRows+"\ntheSidx "+theSidx+"\n theSord "+theSord+"\n sortBy "+sortBy);
			List<?> aTransactionRegisterList = bankingAccountsService.getTransactionRegisterListForReconcile(moAccountID,transactionType,theSord,theSidx);
			System.out.println("inside getting account details"+aTransactionRegisterList.size());
			
			aResponse.setRows(aTransactionRegisterList);
		return aResponse;
	}
	
	
	@RequestMapping(value = "/updateReconcileFL", method = RequestMethod.POST)
	public @ResponseBody Boolean updateReconcileFL(@RequestParam(value = "moTransactionId", required = false) Integer theMoTransactionID,
												@RequestParam(value = "tempRec", required = false) Byte theTempRec,
												@RequestParam(value = "moAccountID", required = false) Integer moAccountID,
												HttpServletResponse theResponse,HttpSession session) throws IOException, BankingException {
		logger.info("MoTrnsactionID: "+theMoTransactionID +"\n TempRec: "+theTempRec);
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		ReconcileFLStatus aReconcileFLStatus = new ReconcileFLStatus();
		aReconcileFLStatus.setMoTransactionID(theMoTransactionID);
		aReconcileFLStatus.setTempRecstatus(theTempRec);
		aReconcileFLStatus.setMoAccountID(moAccountID);
		aReconcileFLStatus.setUserID(aUserBean.getUserId());
		aReconcileFLStatus.setEntryDate(new Date());
		bankingAccountsService.insertReconcileFLStatus(aReconcileFLStatus);
		return true;
	}
	
	
	@RequestMapping(value = "/updateTempReconcile", method = RequestMethod.POST)
	public @ResponseBody Boolean updateTempReconcile(@RequestParam(value = "moAccountID", required = false) Integer moAccountID,
												HttpServletResponse theResponse) throws IOException, BankingException {
	
		bankingAccountsService.tempRecTransactionDetails(moAccountID);
		return true;
	}
	
	
	@RequestMapping(value = "/updateReconcile", method = RequestMethod.POST)
	public @ResponseBody Boolean updateReconcile(@RequestParam(value = "moAccountId", required = false) Integer moAccountId,
												@RequestParam(value = "closingBalance", required = false) BigDecimal closingBalance,
												@RequestParam(value = "openingBalance", required = false) BigDecimal openingBalance,
												HttpServletResponse theResponse) throws IOException, BankingException {
		logger.info("moAccountId: "+moAccountId +"\n closingBalance: "+closingBalance);
		
		bankingAccountsService.tempRecTransactionDetails(moAccountId);
		MoAccount aMoAccount = new MoAccount();
		aMoAccount.setMoAccountId(moAccountId);
		aMoAccount.setOpenBalance(closingBalance);
		aMoAccount.setEndingBalance(openingBalance);
		bankingAccountsService.updateReconcileDetails(aMoAccount);
		return true;
	}

	
	@RequestMapping(value = "/updateDebitsTransaction", method = RequestMethod.POST)
	public @ResponseBody Boolean updateTempReconcile(@RequestParam(value = "moTransactionId", required = false) Integer theMoTransactionID,
												@RequestParam(value = "tempRec", required = false) Byte theTempRec,
												@RequestParam(value = "transDate", required = false) String theTransDate,
	@RequestParam(value = "rxMasterId", required = false) Integer theRxMasterId,
	@RequestParam(value = "amount", required = false) BigDecimal theAmount,
	@RequestParam(value = "reference", required = false) String theReference,
												HttpServletResponse theResponse) throws IOException, BankingException {
		Motransaction aMotransaction = new Motransaction();
		aMotransaction.setMoTransactionId(theMoTransactionID);
		aMotransaction.setTempRec(theTempRec);
		aMotransaction.setTransDate(theTransDate);
		if(theRxMasterId!=null){
			aMotransaction.setRxMasterId(theRxMasterId);	
		}
		
		aMotransaction.setAmount(theAmount);
		aMotransaction.setReference(theReference);
		bankingAccountsService.updateMoTransactionReconcile(aMotransaction);
		return true;
	}
	
	@RequestMapping(value = "/createDebitsTransaction", method = RequestMethod.POST)
	public @ResponseBody Boolean createDebitsTransaction(@RequestParam(value = "moTransactionId", required = false) Integer theMoTransactionID,
												@RequestParam(value = "tempRec", required = false) Byte theTempRec,
												@RequestParam(value = "transDate", required = false) String theTransDate,
	@RequestParam(value = "rxMasterId", required = false) Integer theRxMasterId,
	@RequestParam(value = "amount", required = false) BigDecimal theAmount,
	@RequestParam(value = "reference", required = false) String theReference,
	@RequestParam(value = "description", required = false) String theDescription,
	@RequestParam(value = "transactionDate", required = false) Date theTransactionDate,
	@RequestParam(value = "moAccountId", required = false) Integer themoAccountId,
	//
												HttpServletResponse theResponse) throws IOException, BankingException {
		Motransaction aMotransaction = new Motransaction();
		aMotransaction.setMoTransactionId(theMoTransactionID);
		aMotransaction.setTempRec(theTempRec);
		aMotransaction.setTransDate(theTransDate);
		aMotransaction.setVoid_(new Byte((byte) 0));
		aMotransaction.setReconciled(new Byte((byte) 0));
		aMotransaction.setDirectDeposit(new Byte((byte) 0));
		aMotransaction.setPrinted(new Byte((byte) 0));
		if(theRxMasterId!=null){
			aMotransaction.setRxMasterId(theRxMasterId);	
		}
		//transactionDate
		aMotransaction.setMoAccountId(themoAccountId);
		aMotransaction.setTransactionDate(theTransactionDate);
		aMotransaction.setDescription(theDescription);
		aMotransaction.setAmount(theAmount);
		aMotransaction.setReference(theReference);
		aMotransaction.setMoTransactionTypeId((short) 2);
		bankingAccountsService.createMoTransactionReconcile(aMotransaction);
		return true;
	}
	
	@RequestMapping(value = "/updateDebitsTransactionAll", method = RequestMethod.POST)
	public @ResponseBody Boolean updateDebitsTransactionAll(@RequestParam(value = "moAccountID", required = false) Integer theMoAccountID,
												@RequestParam(value = "tempRec", required = false) Integer theTempRec,
								@RequestParam(value = "transactionType", required = false) Integer theTransactionType,
												HttpServletResponse theResponse,HttpSession session) throws IOException, BankingException {
		
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				bankingAccountsService.updateAllMoTransaction(theMoAccountID,theTempRec,theTransactionType,aUserBean.getUserId());
		return true;
	}
	/*Created By : Velmurugan
	 *Created On :28-08-2014
	 *Description:Resulting Balance=currentbalance+(futuredeposit-futurewithdrawel) 
	 * */
	@RequestMapping(value = "/getResultingBalance", method = RequestMethod.POST)
	public @ResponseBody Motransaction getResultingBalance(@RequestParam(value = "moAccountID", required = false) Integer theMoAccountID,
												HttpServletResponse theResponse) throws IOException, BankingException {
		
		Motransaction motrans=	bankingAccountsService.getResultingBalance(theMoAccountID);
		return motrans;
	}
	
	/**
	 * Created by :Leo
	 * Date:12/11/2014
	 * */
	@RequestMapping(value = "/getCheckNumberExists", method = RequestMethod.POST)
	public @ResponseBody boolean getCheckNumberExists(@RequestParam(value = "checkno", required = false) String checkno,
													  @RequestParam(value = "moAccountID", required = false) String moAccountID,
												HttpServletResponse theResponse) throws IOException, BankingException {
		boolean checkstatus = false;
		checkstatus = bankingAccountsService.checkisExists(checkno,moAccountID);
		System.out.println(checkstatus+"-------->");
		
		return checkstatus;
	}
	
	
	@RequestMapping(value = "/getNegativeAmountinVB", method = RequestMethod.GET)
	public @ResponseBody boolean getNegativeAmountinVB(	HttpServletResponse theResponse) throws IOException, BankingException {
		boolean amountstatus = true;

		ArrayList<BigDecimal> negAmt= new ArrayList<BigDecimal>();
		
		negAmt = bankingAccountsService.getNegativeAmountValidation();
	
		for(int i=0;i<negAmt.size();i++)
		{
			if(negAmt.get(i).signum()<0)
			{
				amountstatus = false;
			}
		}
		
		return amountstatus;
	}
	
	@RequestMapping(value = "/checkBillAvailability", method = RequestMethod.GET)
	public @ResponseBody boolean checkBillAvailability(@RequestParam(value = "moAccountID", required = false) Integer moAccountID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, BankingException, MessagingException {
		boolean availabilitystatus = false;
		try{
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		ArrayList<Vebillpay> checkBillAvailability= new ArrayList<Vebillpay>();
		
		System.out.println("Testing"+moAccountID);
		checkBillAvailability = bankingAccountsService.getCheckBillAvailability(moAccountID,aUserBean.getUserId());
		if(checkBillAvailability.size()>0){
			availabilitystatus = true;
		}
		}catch(Exception e){
			sendTransactionException("<b>Tracking ID:</b>"+"moAccountID="+moAccountID,"Banking.",e,session,therequest);
		}
		return availabilitystatus;
	}
	
	
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:Fetch all account number and description in moaccount table
	 * */
	@RequestMapping(value="/accountList", method = RequestMethod.GET)
	public @ResponseBody List<?> getOwnerNameList(@RequestParam("term") String theOwnerName,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException{
		ArrayList<AutoCompleteBean> accountList = null;
		try{
			accountList = (ArrayList<AutoCompleteBean>) bankingAccountsService.getAccountList(theOwnerName);
		}catch (BankingException e) {
			sendTransactionException("<b>Tracking ID:</b>"+"accountList","Banking.",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return accountList;
	}
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:Save glaccountgrid data in momultipleaccount table based upon momultipleaccountid
	 * */
	@RequestMapping(value = "/saveglaccountgrid", method = RequestMethod.POST)
	public @ResponseBody boolean saveglaccountgrid(
			@RequestParam(value = "motransactionid", required = false) Integer motransid,
			@RequestParam(value = "gridData", required = false) String gridData,
			@RequestParam(value = "transactionDetails[]", required = false) ArrayList<?> theTransactionDetails, 
			@RequestParam(value = "coFiscalPeriodId", required = false) Integer periodid,
			@RequestParam(value = "coFiscalYearId", required = false) Integer yearid,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, BankingException, CompanyException, MessagingException {
		//System.out.println(gridData);
		Momultipleaccount momulaccount=null;
		BigDecimal TotalAmt=new BigDecimal(0);
		try{
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		Motransaction aMotransaction =new Motransaction();
		String DepositType="";
		String aTypeID="";
		
		if(theTransactionDetails.get(1) != "" && theTransactionDetails.get(1) != null){
			aTypeID = theTransactionDetails.get(1).toString(); 
			aMotransaction.setMoTransactionTypeId(Short.parseShort(aTypeID));
		}
		if(theTransactionDetails.get(2) != "" && theTransactionDetails.get(2) != null){
			aMotransaction.setReference((String) theTransactionDetails.get(2));
		}
		if(theTransactionDetails.get(3) != "" && theTransactionDetails.get(3) != null){
			String aDescription = theTransactionDetails.get(3).toString().replaceAll("\n", "").replaceAll("\t", "");
			aMotransaction.setDescription(aDescription);
		}
	
		if(theTransactionDetails.get(9) != "" && theTransactionDetails.get(9) != null){
			String aMoAccountID = theTransactionDetails.get(9).toString();
			aMotransaction.setMoAccountId(Integer.valueOf(aMoAccountID));
		}
		
		aMotransaction.setMoTransactionId(motransid);
		
		String aOperation = theTransactionDetails.get(7).toString();
		
		Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("BT");
		//##------Commented By Zenith for disable Multiple Transaction on 2014-10-23 17-15
		
	
		System.out.println("gridData.length()"+gridData.length());
		JsonParser parser = new JsonParser();
		if (null != gridData && gridData.length() > 6) {
			
			if((aOperation.equals("edit") || aOperation.equals("delete")))
			{
				GlRollback glRollback = new GlRollback();
				glRollback.setVeBillID(aMotransaction.getMoTransactionId());
				glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
				glRollback.setPeriodID(periodid);
				glRollback.setYearID(yearid);
				glRollback.setTransactionDate(aMotransaction.getTransactionDate());
				 gltransactionService.rollBackGlTransaction(glRollback);
			}
				
			
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			for (JsonElement ele1 : array) {
				
				momulaccount = new Momultipleaccount();
				JsonObject obj = ele1.getAsJsonObject();
				
				momulaccount.setCoAccountId(obj.get("coAccountId").getAsInt());				
				aMotransaction.setCoAccountId(obj.get("coAccountId").getAsInt());				
				
				momulaccount.setAmount(obj.get("amount").getAsBigDecimal());				
				aMotransaction.setAmount(obj.get("amount").getAsBigDecimal());
				
				momulaccount.setMoTransactionId(motransid);
				momulaccount.setMoMultipleAccountsId(obj.get("moMultipleAccountsId").getAsInt());
				System.out.println("checkupdates"+momulaccount.getCoAccountId()+"=="+momulaccount.getAmount()+"=="+obj.get("moMultipleAccountsId").getAsInt());
				if(obj.get("moMultipleAccountsId").getAsInt()==0){
					bankingAccountsService.saveGlmultipleAccount(momulaccount);
				}else{
					bankingAccountsService.updateGlmultipleAccount(momulaccount);
				}
				DepositType = "Multiple with subaccounts";
				//##------Commented By Zenith for disable Multiple Transaction on 2014-10-23 17-15
			//	gltransactionService.bankingDeposits(aMotransaction,DepositType,yearid,periodid,aUserBean.getFullName());  
				
				TotalAmt = TotalAmt.add(obj.get("amount").getAsBigDecimal());
			}
			DepositType = "Multiple with mainaccounts";
			aMotransaction.setAmount(TotalAmt);
			//##------Commented By Zenith for disable Multiple Transaction on 2014-10-23 17-15
			//gltransactionService.bankingDeposits(aMotransaction,DepositType,yearid,periodid,aUserBean.getFullName());  
			
			}
		}catch(Exception e){
			sendTransactionException("<b>Tracking ID:</b>"+"saveglaccountgrid"+motransid,"Banking.",e,session,therequest);
		
		}
		
		
		return true;
	}
	
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:fetch all record from momultipleaccount table based upon motransaction id
	 * */
	@RequestMapping(value = "/GlAccountRegisterList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getGlAccountRegisterList(@RequestParam(value="page", required=false) Integer thePage,
																	@RequestParam(value="rows", required=false) Integer theRows,
																	@RequestParam(value="sidx", required=false) String theSidx,
																	@RequestParam(value="sord", required=false) String theSord,
																	@RequestParam(value="motransactionID", required=false) Integer motransactionID,
																	HttpSession session, HttpServletResponse response,HttpServletRequest therequest) throws IOException, CompanyException, UserException, BankingException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		String rowcountquery="SELECT COUNT(*) FROM moMultipleAccount mom JOIN coAccount ca ON(mom.coAccountID=ca.coAccountID) WHERE moTransactionID="+motransactionID;
		int aTotalCount = bankingAccountsService.gettotalrecordCount(rowcountquery);
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			aUserLogin = new TsUserLogin();
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<Momultipleaccount> momultipleaccountList = bankingAccountsService.getGlAccountTransactionRegisterList(aFrom, aTo,motransactionID);
			
			aResponse = new CustomResponse();
			aResponse.setRows(momultipleaccountList);
			aResponse.setRecords(String.valueOf(momultipleaccountList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b>"+"GlAccountRegisterList","Banking.",e,session,therequest);
		} 
		return aResponse;
	}
	@RequestMapping(value = "/getglaccountlist", method = RequestMethod.GET)
	public @ResponseBody List<Coaccount> getglaccountlist(HttpSession aSession,HttpServletResponse theResponse,
			HttpServletRequest therequest,HttpSession session) throws IOException, CompanyException, MessagingException {
	List<Coaccount> glaccountlist=new ArrayList<Coaccount>();
	UserBean aUserBean;
	aUserBean = (UserBean) aSession.getAttribute(SessionConstants.USER);
	try {

	glaccountlist= chartOfAccountsService.getListOfAccountsFordropdown();

	} catch (CompanyException e) {
	logger.error(e.getMessage(), e);
	theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
	
	sendTransactionException("<b>Tracking ID:</b> getglaccountlist","Banking.",e,session,therequest);
	}
	return glaccountlist;
	}
	@RequestMapping(value = "/reIssueCheckDetails", method = RequestMethod.GET)
	public @ResponseBody
	void printCSVCompanyContacts(
			@RequestParam(value = "firstcheckNumber", required = false) Integer firstcheckNumber,
			@RequestParam(value = "lastcheckNumber", required = false) Integer lastcheckNumber,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session)
			throws IOException, MessagingException, SQLException {
		Connection connection = null;
		ConnectionProvider con =null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/ReissueChecksfinal.jrxml");
			filename="ReissueCheck.pdf";
			
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("StartingCheckNumber", firstcheckNumber);
			params.put("EndingCheckNumber", lastcheckNumber);
			
			connection = con.getConnection();
			ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}finally{
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
		 transObj.setHeadermsg("Transaction Log << "+e.getMessage()+" >>");
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