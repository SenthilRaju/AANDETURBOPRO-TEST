/**
 * Created by :Leo  Date:10/11/2014
 * 
 * Description: Credit/Debit Memo
 * 
 * */

package com.turborep.turbotracker.customer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/creditdebitmemo")
public class CreditDebitMemoContorller {
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="customerService")
	private CustomerService itsCuMasterService;
	
	@Resource(name = "gltransactionService")
	private GltransactionService itsGltransactionService;
	
	@Resource(name="accountingCyclesService")
	 AccountingCyclesService accountingCyclesService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	
	protected static Logger itsLogger = Logger.getLogger(CustomerListController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody CustomResponse getAll(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord, HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws CustomerException, IOException, MessagingException {
		int from, to;
		BigInteger aTotalCount;
		CustomResponse response = new CustomResponse();
		try{ 
			//aTotalCount = jobService.getJobsCount();
			to = (rows * page);
			from = to - rows;
			ArrayList<CustomerPaymentBean> aCustomerPaymentsList = itsCuMasterService.getcreditordebitmemoList(from, rows,sidx,sord);
			response.setRows(aCustomerPaymentsList);
			
			itsLogger.info("I am Here");
			aTotalCount = itsCuMasterService.getcreditdebitmemoCount();
			response.setPage(page);
			response.setTotal((int) Math.ceil((double)aTotalCount.intValue() / (double) rows));
		}catch (CustomerException e) {
			sendTransactionException("<b>getAll:</b>getAll","Customer",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			throw e;
		}
		return response;
	}
	
	@RequestMapping(value="/updateTaxAdjustment", method = RequestMethod.POST)
	public @ResponseBody void updateTaxAdjustment(@RequestParam(value="invoiceDate", required= false) Date invoiceDate,
			@RequestParam(value="memoName", required= false) String invoiceNo,
			@RequestParam(value="customerIDName", required= false) String rxcustomerID,
			@RequestParam(value="memotypeName", required=false) String memotype,
			@RequestParam(value="DescriptionName", required= false) String Description, 
			@RequestParam(value="notestextareaName", required= false) String note,
			@RequestParam(value = "amountName",required = false) BigDecimal subamount,
			@RequestParam(value = "frieghtName",required = false) BigDecimal frieghtamt,
			@RequestParam(value = "grandtotalName",required = false) BigDecimal grandtotalAmt,
			@RequestParam(value = "taxAmountName",required = false) BigDecimal taxAmount,
			@RequestParam(value = "salesmanName",required = false) Integer salesmanName,
			@RequestParam(value = "glAccountsName",required = false) Integer glAccountsId,
			@RequestParam(value = "divisonName",required = false) Integer divisonID,
			@RequestParam(value = "taxabe",required = false) String taxabe,
			@RequestParam(value = "taxterritoryName",required = false) Integer taxterritoryID,
			@RequestParam(value = "customerpoName",required = false) String customerpoNo,
			@RequestParam(value = "tax_RateName",required = false) BigDecimal taxRate,
			@RequestParam(value = "Options",required = false) String aOptions,
			@RequestParam(value = "invoiceID",required = false) Integer invoiceID,
			@RequestParam(value = "cuinvoiceInvoiceID",required = false) Integer cuinvoiceInvoiceID,
			@RequestParam(value = "coFiscalPeriodId",required = false) Integer periodId,
			@RequestParam(value = "coFiscalYearId",required = false) Integer yearId,
			@RequestParam(value = "taxsetting",required = false) Byte taxsetting,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, ParseException, BankingException, CustomerException, CompanyException, IOException, MessagingException
	{
		
		try{
			System.out.println("invoiceNo" + cuinvoiceInvoiceID);
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			//itsGltransactionService.receiveCustomerInvoiceBill(cuInvoice, yearID, periodID, userName);	
			   Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
			   Cuinvoice cuInvoice = new Cuinvoice();
			   cuInvoice.setTaxAmount(taxAmount);
			   cuInvoice.setInvoiceAmount(grandtotalAmt);
			   cuInvoice.setTaxRate(taxRate);
			   cuInvoice.setCoTaxTerritoryId(taxterritoryID);
			   cuInvoice.setCuInvoiceId(cuinvoiceInvoiceID);
			   cuInvoice.setNote(note);
			   cuInvoice.setInvoiceDate(invoiceDate);
			   cuInvoice.setChangedById(aUserBean.getUserId());
			   cuInvoice.setChangedOn(new Date());
			   cuInvoice.setTaxfreight(taxsetting);
			   itsGltransactionService.updateTaxAdjustment(cuInvoice,aSysinfo.getCurrentFiscalYearId(),aSysinfo.getCurrentPeriodId(),aUserBean.getUserName());
			
		}catch(Exception e){
			System.out.println("Exception : CreditDebitMemoContorller" +e);
		}
		
	}
	
	@RequestMapping(value="/createCustomerInvoicefromMemo", method = RequestMethod.POST)
	public @ResponseBody void createCustomerInvoicefromMemo(@RequestParam(value="invoiceDate", required= false) String invoiceDate,
																@RequestParam(value="memoName", required= false) String invoiceNo,
																@RequestParam(value="customerIDName", required= false) String rxcustomerID,
																@RequestParam(value="memotypeName", required=false) String memotype,
																@RequestParam(value="DescriptionName", required= false) String Description, 
																@RequestParam(value="notestextareaName", required= false) String note,
																@RequestParam(value = "amountName",required = false) String subamt,
																@RequestParam(value = "frieghtName",required = false) String frtamt,
																@RequestParam(value = "grandtotalName",required = false) String gtotalAmt,
																@RequestParam(value = "taxAmountName",required = false) String txAmt,
																@RequestParam(value = "salesmanName",required = false) Integer salesmanName,
																@RequestParam(value = "glAccountsName",required = false) Integer glAccountsId,
																@RequestParam(value = "divisonName",required = false) Integer divisonID,
																@RequestParam(value = "taxabe",required = false) String taxabe,
																@RequestParam(value = "taxterritoryName",required = false) Integer taxterritoryID,
																@RequestParam(value = "customerpoName",required = false) String customerpoNo,
																@RequestParam(value = "tax_RateName",required = false) BigDecimal taxRate,
																@RequestParam(value = "Options",required = false) String aOptions,
																@RequestParam(value = "invoiceID",required = false) Integer invoiceID,
																@RequestParam(value = "cuinvoiceInvoiceID",required = false) Integer cuinvoiceInvoiceID,
																@RequestParam(value = "coFiscalPeriodId",required = false) Integer periodId,
																@RequestParam(value = "coFiscalYearId",required = false) Integer yearId,
																@RequestParam(value = "taxfreight",required = false) Byte taxfreight,
																HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, ParseException, BankingException, CustomerException, CompanyException, IOException, MessagingException
	{
		try{
		Cuinvoice aCuinvoice = new Cuinvoice();
		Cuinvoicedetail aCuinvoiceDetails = new Cuinvoicedetail();
		int cuInvoiceId=0;
		String invNo="";
		
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		BigDecimal subamount = new BigDecimal(subamt.replaceAll("\\$", ""));
		BigDecimal frieghtamt = new BigDecimal(frtamt.replaceAll("\\$", ""));
		BigDecimal grandtotalAmt = new BigDecimal(gtotalAmt.replaceAll("\\$", ""));
		BigDecimal taxAmount = new BigDecimal(txAmt.replaceAll("\\$", ""));
		
		if(aOptions.equals("add"))
		{
		if(invoiceDate != null && invoiceDate != ""){
			aCuinvoice.setInvoiceDate(DateUtils.parseDate(invoiceDate, new String[]{"MM/dd/yyyy"}));
		}
		
		invNo = itsCuMasterService.getInvoiceNofromcuInvoice(Integer.parseInt(memotype));	
		aCuinvoice.setTaxfreight(taxfreight);
		if(Integer.parseInt(memotype)==1)
		{		
			if(invNo==null || invNo.equals(""))	
			{
				aCuinvoice.setInvoiceNumber("CR10000");
			}
			else
			{			
			aCuinvoice.setInvoiceNumber("CR"+(Integer.parseInt(invNo)+1));
			}
		aCuinvoice.setInvoiceAmount(grandtotalAmt.negate());
		//aCuinvoice.setCostTotal(grandtotalAmt.negate());
		aCuinvoice.setFreight(frieghtamt.negate());
		aCuinvoice.setTaxAmount(taxAmount.negate());
		aCuinvoice.setTaxTotal(taxAmount.negate());
		aCuinvoice.setSubtotal(subamount.negate());
		}
		else
		{
			
			if(invNo==null || invNo.equals(""))	
			{
				aCuinvoice.setInvoiceNumber("DB10000");
			}
			else
			{			
				aCuinvoice.setInvoiceNumber("DB"+(Integer.parseInt(invNo)+1));
			}	
		
		aCuinvoice.setInvoiceAmount(grandtotalAmt);
		//aCuinvoice.setCostTotal(grandtotalAmt);
		aCuinvoice.setFreight(frieghtamt);
		aCuinvoice.setTaxAmount(taxAmount);
		aCuinvoice.setTaxTotal(taxAmount);
		aCuinvoice.setSubtotal(subamount);
		}
			
		aCuinvoice.setCustomerPonumber(customerpoNo);
		aCuinvoice.setRxCustomerId(Integer.parseInt(rxcustomerID));
		aCuinvoice.setCreditmemo(1);
		aCuinvoice.setIscredit(Integer.parseInt(memotype));
		aCuinvoice.setDescription(Description);
		aCuinvoice.setNote(note);
		aCuinvoice.setAppliedAmount(new BigDecimal(0));
		aCuinvoice.setApplied(false);
		
		aCuinvoice.setTransactionStatus(1);
		aCuinvoice.setDiscountAmt(new BigDecimal(0));
		
		
		aCuinvoice.setCuAssignmentId0(salesmanName);
		aCuinvoice.setCoDivisionId(divisonID);
		aCuinvoice.setCoTaxTerritoryId(taxterritoryID);
		aCuinvoice.setTaxRate(taxRate);
		aCuinvoice.setMemoStatus(1);
		aCuinvoice.setCreatedById(aUserBean.getUserId());
		aCuinvoice.setCreatedOn(new Date());
		
		System.out.println("---------->"+taxRate);
		
		cuInvoiceId = itsCuMasterService.addCreditDebitDetails(aCuinvoice);
		aCuinvoice.setCuInvoiceId(cuInvoiceId);
		
		aCuinvoiceDetails.setCuInvoiceId(cuInvoiceId);
		aCuinvoiceDetails.setCoAccountID(glAccountsId);
		aCuinvoiceDetails.setQuantityBilled(new BigDecimal(0));
		aCuinvoiceDetails.setUnitCost(new BigDecimal(0));
		aCuinvoiceDetails.setUnitPrice(new BigDecimal(0));
		aCuinvoiceDetails.setPriceMultiplier(new BigDecimal(0));
		aCuinvoiceDetails.setQuantityBilled(new BigDecimal(0));
		
		if (taxabe != null) {
			if (taxabe.equalsIgnoreCase("on") || taxabe.equalsIgnoreCase("yes")) {
				aCuinvoiceDetails.setTaxable((byte) 1);
			} else {
				aCuinvoiceDetails.setTaxable((byte) 0);
			}
		}
		
		if(cuinvoiceInvoiceID!=null)
		itsCuMasterService.updateCustomerInvoice(cuinvoiceInvoiceID,"taxAdj",aUserBean);
		
		
		itsJobService.updateTaxableandNonTaxableforCuInvoice(aCuinvoice);
		if(aCuinvoice.getInvoiceAmount().compareTo(new BigDecimal("0.0000"))!=0){
			itsJobService.saveCustomerInvoiceLog(new Cuinvoice(),aCuinvoice,"CI-Created",1,periodId,yearId);
			}
		itsCuMasterService.addCreditDebitDetailstoInvDetails(aCuinvoiceDetails);
		
		aCuinvoice.setInvoiceAmount(grandtotalAmt);
		//aCuinvoice.setCostTotal(grandtotalAmt);
		aCuinvoice.setFreight(frieghtamt);
		aCuinvoice.setTaxAmount(taxAmount);
		aCuinvoice.setTaxTotal(taxAmount);
		aCuinvoice.setSubtotal(subamount);
		
		
		itsGltransactionService.receiveCreditDebitMemo(aCuinvoice,yearId,periodId,aUserBean.getFullName());	
		
	}
	
	else
	{

		System.out.println(invoiceID+"------------------->");
		
		itsCuMasterService.updateCustomerInvoice(invoiceID,"CrDBMemo",aUserBean);
		
		
	if(invoiceDate != null && invoiceDate != ""){
			aCuinvoice.setInvoiceDate(DateUtils.parseDate(invoiceDate, new String[]{"MM/dd/yyyy"}));
		}
		
		Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CI");
		GlRollback glRollback = new GlRollback();
		glRollback.setVeBillID(invoiceID);
		glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
		glRollback.setPeriodID(periodId);
		glRollback.setYearID(yearId);
		glRollback.setTransactionDate(aCuinvoice.getInvoiceDate());
		itsGltransactionService.rollBackGlTransaction(glRollback);	
		
		
		invNo = itsCuMasterService.getInvoiceNofromcuInvoice(Integer.parseInt(memotype));	
		aCuinvoice.setTaxfreight(taxfreight);
		if(Integer.parseInt(memotype)==1)
		{		
		aCuinvoice.setInvoiceNumber("CR"+(Integer.parseInt(invNo)+1));
		aCuinvoice.setInvoiceAmount(grandtotalAmt.negate());
		//aCuinvoice.setCostTotal(grandtotalAmt.negate());
		aCuinvoice.setFreight(frieghtamt.negate());
		aCuinvoice.setTaxAmount(taxAmount.negate());
		aCuinvoice.setTaxTotal(taxAmount.negate());
		aCuinvoice.setSubtotal(subamount.negate());
		}
		else
		{
		aCuinvoice.setInvoiceNumber("DB"+(Integer.parseInt(invNo)+1));
		aCuinvoice.setInvoiceAmount(grandtotalAmt);
		//aCuinvoice.setCostTotal(grandtotalAmt);
		aCuinvoice.setFreight(frieghtamt);
		aCuinvoice.setTaxAmount(taxAmount);
		aCuinvoice.setTaxTotal(taxAmount);
		aCuinvoice.setSubtotal(subamount);
		}
	
			
		aCuinvoice.setCustomerPonumber(customerpoNo);
		aCuinvoice.setRxCustomerId(Integer.parseInt(rxcustomerID));
		aCuinvoice.setCreditmemo(1);
		aCuinvoice.setIscredit(Integer.parseInt(memotype));
		aCuinvoice.setDescription(Description);
		aCuinvoice.setNote(note);
		aCuinvoice.setAppliedAmount(new BigDecimal(0));
		aCuinvoice.setApplied(false);
		
		aCuinvoice.setTransactionStatus(1);
		aCuinvoice.setDiscountAmt(new BigDecimal(0));
		
		aCuinvoice.setCuAssignmentId0(salesmanName);
		aCuinvoice.setCoDivisionId(divisonID);
		aCuinvoice.setCoTaxTerritoryId(taxterritoryID);
		aCuinvoice.setTaxRate(taxRate);
		aCuinvoice.setMemoStatus(1);
		aCuinvoice.setCreatedById(aUserBean.getUserId());
		aCuinvoice.setCreatedOn(new Date());
		aCuinvoice.setChangedById(aUserBean.getUserId());
		aCuinvoice.setChangedOn(new Date());
		
		System.out.println("---------->"+invoiceID);
		
		itsJobService.invoicelogRollbackentry(invoiceID,aCuinvoice);
		
		cuInvoiceId = itsCuMasterService.addCreditDebitDetails(aCuinvoice);
		aCuinvoice.setCuInvoiceId(cuInvoiceId);
		
		//aCuinvoiceDetails.setCuInvoiceDetailId(cuinvoiceInvoiceID);
		aCuinvoiceDetails.setCuInvoiceId(cuInvoiceId);
		aCuinvoiceDetails.setCoAccountID(glAccountsId);
		aCuinvoiceDetails.setQuantityBilled(new BigDecimal(0));
		aCuinvoiceDetails.setUnitCost(new BigDecimal(0));
		aCuinvoiceDetails.setUnitPrice(new BigDecimal(0));
		aCuinvoiceDetails.setPriceMultiplier(new BigDecimal(0));
		aCuinvoiceDetails.setQuantityBilled(new BigDecimal(0));
		
		if (taxabe != null) {
			if (taxabe.equalsIgnoreCase("on") || taxabe.equalsIgnoreCase("yes")) {
				aCuinvoiceDetails.setTaxable((byte) 1);
			} else {
				aCuinvoiceDetails.setTaxable((byte) 0);
			}
		}
		
		if(aCuinvoice.getInvoiceAmount().compareTo(new BigDecimal("0.0000"))!=0){
			itsJobService.saveCustomerInvoiceLog(new Cuinvoice(),aCuinvoice,"CI-Created",1,periodId,yearId);
			}
		itsCuMasterService.addCreditDebitDetailstoInvDetails(aCuinvoiceDetails);
	
		aCuinvoice.setInvoiceAmount(grandtotalAmt);
		//aCuinvoice.setCostTotal(grandtotalAmt);
		aCuinvoice.setFreight(frieghtamt);
		aCuinvoice.setTaxAmount(taxAmount);
		aCuinvoice.setTaxTotal(taxAmount);
		aCuinvoice.setSubtotal(subamount);
		
		itsGltransactionService.receiveCreditDebitMemo(aCuinvoice,yearId,periodId,aUserBean.getFullName());	
		
		
	
	  }
	}catch(Exception e){
		sendTransactionException("<b>cuinvoiceInvoiceID:</b>"+cuinvoiceInvoiceID,"Customer",e,session,therequest);
	}
	}	
	
	@RequestMapping(value = "/statusChecking", method = RequestMethod.POST)
	public @ResponseBody Boolean copyTemplateLineItems(
			@RequestParam(value = "cuInvoiceID", required = false) Integer cuInvoiceID,			
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException,CustomerException, MessagingException {
		Boolean isSaved = false;
		Motransaction aMotransaction = new Motransaction();
		ArrayList<GlLinkage> gllinkList=new ArrayList<GlLinkage>();
		try {
			
			Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CI");
			
			gllinkList = itsCuMasterService.getglLinkagewithinvoiceId(cuInvoiceID,aColedgersource);
			
			if(gllinkList.size()>0)
			{
				isSaved=true;
			}
			
		} catch (Exception e) {
			sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage());
		}

		return isSaved;
	}
	
	@RequestMapping(value = "/memoPdfPrint", method = RequestMethod.GET)
	public @ResponseBody void memoPdfPrint(HttpServletResponse theResponse,HttpServletRequest therequest,HttpSession session) throws IOException, CustomerException, SQLException, MessagingException {
		try{
			itsCuMasterService.memoPdfPrint(theResponse,therequest);
		}catch (CustomerException e) {
			sendTransactionException("<b>MethodName:</b>memoPdfPrint","Customer",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			throw e;
		}
	}
	
	
	
	
	@RequestMapping(value="/getCuInvoicefminvoiceno", method = RequestMethod.GET)
	public @ResponseBody Cuinvoice getCuInvoicefminvoiceno(@RequestParam(value="invoiceNumber", required= false) String invoiceNumber,
																 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) 
																 throws JobException, IOException, MessagingException{
		Cuinvoice alist=new Cuinvoice();
		try {	
			alist=itsCuMasterService.getCuInvoicefminvoiceno(invoiceNumber);	
			System.out.println("===>>>"+alist.getCuInvoiceId());
		} catch (Exception e) {
			sendTransactionException("<b>invoiceNumber:</b>"+invoiceNumber,"Customer",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
		}
		
		return alist;		
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
