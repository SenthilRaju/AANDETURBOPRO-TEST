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
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.persistence.metamodel.SetAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.type.SetType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoAccountBean;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.search.service.SearchServiceInterface;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.dao.Veshipvia;

@Controller
@RequestMapping("/company")
public class ChartOfAccountsController {

	protected static Logger logger = Logger.getLogger(ChartOfAccountsController.class);
	
	@Resource(name="chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "SearchServices")
	private SearchServiceInterface itsSearchServices;
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
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
	@RequestMapping(value = "/getListOfAccounts", method = RequestMethod.GET)
	public @ResponseBody CustomResponse getloginPage(@RequestParam(value="page", required=false) Integer page,
														@RequestParam(value="rows", required=false) Integer rows,
														@RequestParam(value="sidx", required=false) String sidx,
														@RequestParam(value="sord", required=false) String sord,
														HttpServletRequest request,HttpServletResponse theResponse,
														HttpSession session) throws IOException, MessagingException {
		int from, to;
		CustomResponse response = new CustomResponse();
	
			BigInteger aTotalCount = null;
			try {
				aTotalCount = chartOfAccountsService.getListOfAccountsCount();
				to = (rows * page);
				from = to - rows;
				if (to > aTotalCount.intValue()) to = aTotalCount.intValue();
				List<Coaccount> accounts = chartOfAccountsService.getListOfAccounts(from, rows);
				response.setRows(accounts);
				response.setRecords( String.valueOf(accounts.size()) );
				response.setPage( page );
				response.setTotal((int) Math.ceil((double)aTotalCount.intValue() / (double) rows));
			} catch (CompanyException e) {
				logger.error(e.getMessage(), e);
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
				sendTransactionException("<b>TrackingID:</b> chartOfAccountsService","ChartOfAccounts",e,session,request);
			}
		return response;
	}
	
	@RequestMapping(value = "/chartsAccountsList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getChartsAccounts(
			@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			HttpSession session, HttpServletRequest request,HttpServletResponse response) throws IOException, CompanyException, UserException, MessagingException {
		String aChartsAccount = "chartsAccount";
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		int aTotalCount = chartOfAccountsService.getRecordChartsCount();
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			aUserLogin = new TsUserLogin();
			aUserLogin.setChartsperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aChartsAccount);
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<?> aChartsAccountsList = chartOfAccountsService.getChartsAccountsList(aFrom, aTo);
			aResponse = new CustomResponse();
			aResponse.setRows(aChartsAccountsList);
			aResponse.setRecords(String.valueOf(aChartsAccountsList.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>TrackingID:</b> chartsAccount","ChartOfAccounts",e,session,request);
		} 
		return aResponse;
	}
	
	@RequestMapping(value="/getChartofAccounts", method = RequestMethod.GET)
	public @ResponseBody Coaccount getChartofAccounts(@RequestParam(value="coAccountID", required = false) Integer theCoAccountID,
						HttpSession session,HttpServletRequest request, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Coaccount aCoaccount = null;
		try{
			aCoaccount = new Coaccount();
			if(theCoAccountID != null){
					aCoaccount = chartOfAccountsService.getChartOfAccount(theCoAccountID);
			}
		}catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coAccountID:</b> "+theCoAccountID,"ChartOfAccounts",e,session,request);
		}
		return aCoaccount;
	}
	
	@RequestMapping(value="/updateChartAccount", method = RequestMethod.POST)
	public @ResponseBody Coaccount updateChartAccount(@RequestParam(value = "numberName1", required = false) String theNumber1,
																				@RequestParam(value = "numberName2", required = false) String theNumber2,
																				@RequestParam(value = "numberName3", required = false) String theNumber3,
																				@RequestParam(value = "numberName4", required = false) String theNumber4,
																				@RequestParam(value = "inActiveName", required = false) boolean theActive,
																				@RequestParam(value = "accountTypeName", required = false) Integer accountType,
												 								@RequestParam(value = "decriptionName", required = false) String theDescription,
																				/*Commented by Jenith @RequestParam(value = "expenseNameBox", required = false) boolean theExpense,*/ 
																				@RequestParam(value = "largeName", required = false) boolean theLargeName,
																				@RequestParam(value = "boldName", required = false) boolean theBoldName,
																				@RequestParam(value = "italicName", required = false) boolean theItalicName,
																				@RequestParam(value = "dollourName", required = false) boolean theDollourName,
																				@RequestParam(value = "alwaysName", required = false) boolean theAlwaysName,
																				@RequestParam(value = "underlineName", required = false) boolean theUnderlineName,
																				@RequestParam(value = "verticalSpacingName", required = false) short theVerticalSpace,
																				@RequestParam(value = "tabIndentationName", required = false) short theTabIndentation,
																				@RequestParam(value = "balanceSheetColumnName", required = false) short theBalanceSheet,
																				@RequestParam(value = "lineAboveAmountName", required = false) short theLineAboveAmount,
																				@RequestParam(value = "lineBelowAmountName", required = false) short theLineBelowAmount,
																				/*Commented by Jenith  @RequestParam(value = "postingLevelName", required = false) short thePostingName,*/
																				@RequestParam(value = "coAccountName", required = false) Integer theCoAccount,
																				@RequestParam(value = "typeAccount", required = false) String typeAccount,
																				@RequestParam(value = "financialtype", required = false) Byte financialtype,
																				HttpServletRequest request, HttpSession session, HttpServletResponse theResponse) throws ParseException, JobException, IOException, MessagingException{
		Coaccount aCoaccount = null;
		Coaccount aCoaccountReturn = null;
		String accNumber="";
		try{
			aCoaccount = new Coaccount();
			byte aInActive = (byte) (theActive?1:0);
			byte aExpense = (byte) (0);
			byte aLarge = (byte) (theLargeName?1:0);
			byte aBoldName = (byte) (theBoldName?1:0);
			byte aItalicName = (byte) (theItalicName?1:0);
			byte aDollour = (byte) (theDollourName?1:0);
			byte aAlways = (byte) (theAlwaysName?1:0);
			byte aUnderLine = (byte) (theUnderlineName?1:0);
			byte debitType = (byte)((accountType==0) ? 0 : 1);
			
			System.out.println(theNumber1 +"---"+theNumber2+"---"+theNumber3+"---"+theNumber4);
			
			if(!theNumber1.trim().equals(""))
				accNumber += theNumber1;
			if(!theNumber2.trim().equals(""))
				accNumber += "-"+theNumber2;
			if(!theNumber3.trim().equals(""))
				accNumber += "-"+theNumber3;	
			if(!theNumber4.trim().equals(""))
				accNumber += "-"+theNumber4;
			
			aCoaccount.setNumber(accNumber);
			aCoaccount.setDescription(theDescription);
			aCoaccount.setInActive(aInActive);
			aCoaccount.setTax1099(aExpense);
			aCoaccount.setFontBold(aBoldName);
			aCoaccount.setFontItalic(aItalicName);
			aCoaccount.setFontLarge(aLarge);
			aCoaccount.setFontUnderline(aUnderLine);
			aCoaccount.setIncludeWhenZero(aAlways);
			aCoaccount.setDollarSign(aDollour);
			aCoaccount.setVerticalSpacing(theVerticalSpace);
			aCoaccount.setHorizontalSpacing(theTabIndentation);
			aCoaccount.setBalanceSheetColumn(theBalanceSheet);
			aCoaccount.setLineAboveAmount(theLineAboveAmount);
			aCoaccount.setLineBelowAmount(theLineBelowAmount);
			aCoaccount.setTotalingLevel((short)0);
			aCoaccount.setCoAccountId(theCoAccount);
			aCoaccount.setDebitBalance(debitType);
			aCoaccount.setTypeAccount(typeAccount);
			aCoaccount.setFinancialStatus(financialtype);
			aCoaccountReturn = chartOfAccountsService.updateChartAccount(aCoaccount);
		}catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>AccountNumber:</b> "+accNumber,"ChartOfAccounts",e,session,request);
		}
		return aCoaccountReturn;
	}
	
	@RequestMapping(value="/addNewChartAccount", method = RequestMethod.POST)
	public @ResponseBody boolean addNewChartAccount(@RequestParam(value = "numberName1", required = false) String theNumber1,
																							@RequestParam(value = "numberName2", required = false) String theNumber2,
																							@RequestParam(value = "numberName3", required = false) String theNumber3,
																							@RequestParam(value = "numberName4", required = false) String theNumber4,
																							@RequestParam(value = "segment1length", required = false) String segment1length,
																							@RequestParam(value = "segment2length", required = false) String segment2length,
																							@RequestParam(value = "segment3length", required = false) String segment3length,
																							@RequestParam(value = "segment4length", required = false) String segment4length,
																							@RequestParam(value = "inActiveName", required = false) boolean theActive,
																							@RequestParam(value = "accountTypeName", required = false) Integer accountType,
																							@RequestParam(value = "decriptionName", required = false) String theDescription,
																							@RequestParam(value = "expenseNameBox", required = false) boolean theExpense, 
																							@RequestParam(value = "largeName", required = false) boolean theLargeName,
																							@RequestParam(value = "boldName", required = false) boolean theBoldName,
																							@RequestParam(value = "italicName", required = false) boolean theItalicName,
																							@RequestParam(value = "dollourName", required = false) boolean theDollourName,
																							@RequestParam(value = "alwaysName", required = false) boolean theAlwaysName,
																							@RequestParam(value = "underlineName", required = false) boolean theUnderlineName,
																							@RequestParam(value = "verticalSpacingName", required = false) short theVerticalSpace,
																							@RequestParam(value = "tabIndentationName", required = false) short theTabIndentation,
																							@RequestParam(value = "balanceSheetColumnName", required = false) short theBalanceSheet,
																							@RequestParam(value = "lineAboveAmountName", required = false) short theLineAboveAmount,
																							@RequestParam(value = "lineBelowAmountName", required = false) short theLineBelowAmount,
																							//@RequestParam(value = "postingLevelName", required = false) short thePostingName,
																							@RequestParam(value = "coAccountName", required = false) Integer theCoAccount,
																							@RequestParam(value = "typeAccount", required = false) String typeAccount,
																							@RequestParam(value = "financialtype", required = false) Byte financialtype,
																							HttpSession session, HttpServletRequest request, HttpServletResponse theResponse) throws ParseException, JobException, IOException, MessagingException{
		Coaccount aCoaccount = null;
		String accNumber="";
		try{
			aCoaccount = new Coaccount();
			byte aInActive = (byte) (theActive?1:0);
			byte aExpense = (byte) (theExpense?1:0);
			byte aLarge = (byte) (theLargeName?1:0);
			byte aBoldName = (byte) (theBoldName?1:0);
			byte aItalicName = (byte) (theItalicName?1:0);
			byte aDollour = (byte) (theDollourName?1:0);
			byte aAlways = (byte) (theAlwaysName?1:0);
			byte aUnderLine = (byte) (theUnderlineName?1:0);
			byte aCantraAcc = 0;
			byte aIsMasterAccount =0;
			byte aIsSubAccount = 0;
			byte debitType = (byte)((accountType==0) ? 0 : 1);
			
			if(!theNumber1.trim().equals(""))
				accNumber += theNumber1;
			if(!theNumber2.trim().equals(""))
				accNumber += "-"+theNumber2;
			if(!theNumber3.trim().equals(""))
				accNumber += "-"+theNumber3;	
			if(!theNumber4.trim().equals(""))
				accNumber += "-"+theNumber4;
			
			System.out.println(segment1length+"-"+segment2length+"-"+segment3length+"-"+segment4length);
			
			if(theNumber1.trim()=="" && segment1length.trim()!="")
			{
				System.out.println("enter");
				accNumber = segmentzerocalculation(accNumber,segment1length);
			}
			if(theNumber2.trim()=="" && segment2length.trim()!="")
			{
				System.out.println("enter1");
				accNumber = segmentzerocalculation(accNumber,segment2length);
			}
			if(theNumber3.trim()=="" && segment3length.trim()!="")
			{
				System.out.println("enter2");
				accNumber = segmentzerocalculation(accNumber,segment3length);
			}
			if(theNumber3.trim()=="" && segment4length.trim()!="")
			{
				System.out.println("enter3");
				accNumber = segmentzerocalculation(accNumber,segment4length);
			}
						
			aCoaccount.setNumber(accNumber);
			aCoaccount.setDescription(theDescription);
			aCoaccount.setInActive(aInActive);
			aCoaccount.setTax1099(aExpense);
			aCoaccount.setFontBold(aBoldName);
			aCoaccount.setFontItalic(aItalicName);
			aCoaccount.setFontLarge(aLarge);
			aCoaccount.setFontUnderline(aUnderLine);
			aCoaccount.setIncludeWhenZero(aAlways);
			aCoaccount.setDollarSign(aDollour);
			aCoaccount.setVerticalSpacing((short)0);
			aCoaccount.setContraAccount(aCantraAcc);
			aCoaccount.setBalanceSheetColumn((short)0);
			aCoaccount.setIsMasterAccount(aIsMasterAccount);
			aCoaccount.setIsSubAccount(aIsSubAccount);
			aCoaccount.setHorizontalSpacing((short)0);
			aCoaccount.setLineAboveAmount((short)0);
			aCoaccount.setLineBelowAmount((short)0);
			aCoaccount.setTotalingLevel((short)0);
			aCoaccount.setDebitBalance((debitType));
			aCoaccount.setTypeAccount(typeAccount);
			aCoaccount.setFinancialStatus(financialtype);
			
			List<Coaccount> licoAccount=chartOfAccountsService.getAllAccountsfromAccountNumber(accNumber);
			
			if(licoAccount.size()==0)
			{
			chartOfAccountsService.addNewChartAccount(aCoaccount);
			return true;
			}
			else
			{
			return false;
			}
		}catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>AccountNumber:</b> "+accNumber,"ChartOfAccounts",e,session,request);
			return false;
		}
		
	}
	
	public String segmentzerocalculation(String accountno,String length)
	{
			if(length.equals("1"))
				accountno += "-0";
			else if(length.equals("2"))
				accountno += "-00";
			else if(length.equals("3"))
				accountno += "-000";
			else if(length.equals("4"))
				accountno += "-0000";
			else if(length.equals("5"))
				accountno += "-00000";
			
			System.out.println(accountno);
		
		return accountno;
	}
	
	@RequestMapping(value="/getAccountsDetails", method = RequestMethod.GET)
	public @ResponseBody List<CoAccountBean> getAccountsList(@RequestParam(value = "accountID") Integer theAccounts, HttpServletResponse theResponse) throws CompanyException, IOException{
		ArrayList<CoAccountBean> aAccountsList = null;
		try{
			aAccountsList = (ArrayList<CoAccountBean>) chartOfAccountsService.getAccountsList(theAccounts);
		}catch (CompanyException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aAccountsList;
	}
	
	@RequestMapping(value="/getLedgerDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String, ArrayList<?>> getLedgerDetail(@RequestParam(value = "accountID") Integer theAccounts,
			HttpServletResponse theResponse,HttpServletRequest request, HttpSession session) throws CompanyException, IOException, MessagingException{
		
			Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
			//Coledgersource aColedgersource =new Coledgersource();
			Cofiscalperiod aCofiscalperiod = new Cofiscalperiod();
			Cofiscalyear aCofiscalyear = new Cofiscalyear();
			try {
				//aColedgersource = gltransactionService.getColedgersourceDetail("JE");
				aCofiscalperiod = gltransactionService.getCofiscalPeriodDetail();
				aCofiscalyear = gltransactionService.getCofiscalYearDetail();
			} catch (BankingException e1) {
				e1.printStackTrace();
			}
			ArrayList<GlTransaction> aAccountsList = null;
			ArrayList<GlTransaction> aAccountsListwithPeriod = null;
			try {
				
				logger.info("Account ID: "+theAccounts);

				//aAccountsList = (ArrayList<GlTransaction>) chartOfAccountsService.getLedgerDetails(theAccounts,aCofiscalyear.getFiscalYear());
				aAccountsList = (ArrayList<GlTransaction>) chartOfAccountsService.getLedgerDetails(theAccounts,aCofiscalperiod.getCoFiscalPeriodId(),aCofiscalyear.getCoFiscalYearId());

				map.put("yearToDate", aAccountsList);
				aAccountsListwithPeriod = (ArrayList<GlTransaction>) chartOfAccountsService.getLedgerDetailsPeriod(theAccounts,aCofiscalperiod.getCoFiscalPeriodId(),aCofiscalyear.getCoFiscalYearId());
				map.put("periodData", aAccountsListwithPeriod);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>AccountID:</b> "+theAccounts,"ChartOfAccounts",e,session,request);
			}
			return map;

		}

	
	@RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
	public @ResponseBody Boolean getloginPage(@RequestParam(value = "coAccountID", required = false) Integer coAccountID, 
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, BankingException, MessagingException {
		Boolean isDeleted = false;
		try {
			
			ArrayList<GlTransaction> arrayListofGlTransaction = new ArrayList<GlTransaction>();
			
			arrayListofGlTransaction = gltransactionService.getAllglTransactionDetailswithcoAccountID(coAccountID);
			
			if(arrayListofGlTransaction.size()==0)
			isDeleted = chartOfAccountsService.deleteChartOfAccount(coAccountID);
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coAccountID:</b> "+coAccountID,"ChartOfAccounts",e,session,request);
			return false;
		}
		return isDeleted;
	}
	
	@RequestMapping(value = "/getAdditional", method = RequestMethod.GET)
	public @ResponseBody SysAccountLinkage getAccountAdditionalInformation(@RequestParam(value = "coAccountID", required = false) Integer coAccountID,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		SysAccountLinkage aSysAccountlinkage = null;
		try {
			aSysAccountlinkage = chartOfAccountsService.getAccountAdditionalInfo();
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>coAccountID:</b> "+coAccountID,"ChartOfAccounts",e,session,request);
		}
		return aSysAccountlinkage;
	}

	@RequestMapping(value = "/updateAdditional", method = RequestMethod.POST)
	public @ResponseBody Boolean updateAdditionalInformation(@RequestParam(value = "accountRangeAssetStart", required = false) String accountRangeAssetStart,
																																							@RequestParam(value = "accountRangeAssetEnd", required = false) String accountRangeAssetEnd,
																																							@RequestParam(value = "accountRangeLiabilityStart", required = false) String accountRangeLiabilityStart,
																																							@RequestParam(value = "accountRangeLiabilityEnd", required = false) String accountRangeLiabilityEnd,
																																							@RequestParam(value = "accountRangeEquityStart", required = false) String accountRangeEquityStart,
																																							@RequestParam(value = "accountRangeEquityEnd", required = false) String accountRangeEquityEnd,
																																							@RequestParam(value = "accountRangeIncomeStart", required = false) String accountRangeIncomeStart,
																																							@RequestParam(value = "accountRangeIncomeEnd", required = false) String accountRangeIncomeEnd,
																																							@RequestParam(value = "accountRangeExpenseStart", required = false) String accountRangeExpenseStart,
																																							@RequestParam(value = "accountRangeExpenseEnd", required = false) String accountRangeExpenseEnd,
																																							@RequestParam(value = "vendorAccountsPayableAccID", required = false) Integer vendorAccountsPayableAccID,
																																							@RequestParam(value = "vendorFreightAccID", required = false) Integer vendorFreightAccID,
																																							@RequestParam(value = "vendorSalesTaxPaidAccID", required = false) Integer vendorSalesTaxPaidAccID,
																																							@RequestParam(value = "vendorDiscountsTakenAccID", required = false) Integer vendorDiscountsTakenAccID,
																																							@RequestParam(value = "vendorExpenseAccID", required = false) Integer vendorExpenseAccID,
																																							@RequestParam(value = "miscAccountAccID", required = false) Integer miscAccountAccID,
																																							@RequestParam(value = "miscInventoryAdjustmentAccID", required = false) Short miscInventoryAdjustmentAccID,
																																							@RequestParam(value = "generalLedgerNetSalesAccID", required = false) Integer generalLedgerNetSalesAccID,
																																							@RequestParam(value = "generalLedgerProfitLossAccID", required = false) Integer generalLedgerProfitLossAccID,
																																							@RequestParam(value = "generalLedgerCurEarningsAccID", required = false) Integer generalLedgerCurEarningsAccID,
																																							@RequestParam(value = "generalLedgerRetainedEarningsAccID", required = false) Integer generalLedgerRetainedEarningsAccID,
																																							@RequestParam(value = "customerAccountsReceivableAccID", required = false) Integer customerAccountsReceivableAccID,
																																							@RequestParam(value = "customerDisc_AdjstAccID", required = false) Integer customerDisc_AdjstAccID,
																																							@RequestParam(value = "customerSalesTaxInvoicedAccID", required = false) Integer customerSalesTaxInvoicedAccID,
																																							@RequestParam(value = "customerShippingDropShipAccID", required = false) Integer customerShippingDropShipAccID,
																																							@RequestParam(value = "customerShpngInvntryAccID", required = false) Short customerShpngInvntryAccID,
																																							@RequestParam(value = "customerOthrChrgsAccID", required = false) Integer customerOthrChrgsAccID,
																																							@RequestParam(value = "customerPaymntsRcvdAccID", required = false) Integer customerPaymntsRcvdAccID,
																																							HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		Boolean isUpdated = false;
		SysAccountLinkage aSysAccountlinkage = new SysAccountLinkage();
		aSysAccountlinkage.setSysAccountLinkageId(1);
		aSysAccountlinkage.setCoRangeAsset1(accountRangeAssetStart);
		aSysAccountlinkage.setCoRangeAsset2(accountRangeAssetEnd);
		aSysAccountlinkage.setCoRangeLiability1(accountRangeLiabilityStart);
		aSysAccountlinkage.setCoRangeLiability2(accountRangeLiabilityEnd);
		aSysAccountlinkage.setCoRangeEquity1(accountRangeEquityStart);
		aSysAccountlinkage.setCoRangeEquity2(accountRangeEquityEnd);
		aSysAccountlinkage.setCoRangeIncome1(accountRangeIncomeStart);
		aSysAccountlinkage.setCoRangeIncome2(accountRangeIncomeEnd);
		aSysAccountlinkage.setCoRangeExpense1(accountRangeExpenseStart);
		aSysAccountlinkage.setCoRangeExpense2(accountRangeExpenseEnd);
		aSysAccountlinkage.setCoAccountIdap(vendorAccountsPayableAccID);
		aSysAccountlinkage.setCoAccountIdfreight(vendorFreightAccID);
		aSysAccountlinkage.setCoAccountIdsalesTaxPaid(vendorSalesTaxPaidAccID);
		aSysAccountlinkage.setCoAccountIddiscountsTaken(vendorDiscountsTakenAccID);
		aSysAccountlinkage.setCoAccountIdmisc(miscAccountAccID);
		aSysAccountlinkage.setCoAccountIdinventoryAdjustment(miscInventoryAdjustmentAccID);
		aSysAccountlinkage.setCoAccountIdnetSales(generalLedgerNetSalesAccID);
		aSysAccountlinkage.setCoAccountIdpl(generalLedgerProfitLossAccID);
		aSysAccountlinkage.setCoAccountIdcurEarnings(generalLedgerCurEarningsAccID);
		aSysAccountlinkage.setCoAccountIdretainedEarnings(generalLedgerRetainedEarningsAccID);
		aSysAccountlinkage.setCoAccountIdar(customerAccountsReceivableAccID);
		aSysAccountlinkage.setCoAccountIddiscounts(customerDisc_AdjstAccID);
		aSysAccountlinkage.setCoAccountIdsalesTaxInv(customerSalesTaxInvoicedAccID);
		aSysAccountlinkage.setCoAccountIdshipping(customerShippingDropShipAccID);
		aSysAccountlinkage.setCoAccountIdshipInventory(customerShpngInvntryAccID);
		aSysAccountlinkage.setCoAccountIdotherCharges(customerOthrChrgsAccID);
		aSysAccountlinkage.setCoAccountIdpayments(customerPaymntsRcvdAccID);
		try {
			isUpdated = chartOfAccountsService.updateAdditionalInfo(aSysAccountlinkage);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>SysAccountLinkageID:</b> "+1,"ChartOfAccounts",e,session,request);
		}
		return isUpdated;
	}
	
	@RequestMapping(value = "/getAccountNumberAutoSuggest", method = RequestMethod.GET)
	public @ResponseBody List<AutoCompleteBean> getAccountNumberAutoSuggest(@RequestParam(value = "term", required = false) String theAccountSearch, 
			HttpServletRequest request,HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException {
		List<AutoCompleteBean> aAccountsSearchList = null;
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aAccountsSearchList = (List<AutoCompleteBean>) itsSearchServices.getSearchAccountsList(aAutoCompleteBean, theAccountSearch);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(501, e.getMessage());
			sendTransactionException("<b>term:</b> "+theAccountSearch,"ChartOfAccounts",e,session,request);
		}
		return aAccountsSearchList;
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