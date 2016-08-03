package com.turborep.turbotracker.customer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.web.commands.CommandException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.company.service.TaxTerritoriesService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.customer.dao.Cureceipttype;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Ecsplitjob;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/custpaymentslistcontroller")
public class CustomerPaymentsController {

	Logger logger = Logger.getLogger(CustomerPaymentsController.class);
	
	@Resource(name = "customerService")
	private CustomerService itsCuMasterService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name = "chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@Resource(name="taxTerritoriesService")
	private TaxTerritoriesService taxTerritoriesService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	
	@Resource(name="gltransactionService")
	private GltransactionService itsGltransactionService;
	
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
			logger.info("Called Customer Payment & SO \nPage:"+page+"\nRows:"+rows+"\nSort:"+sidx+"\nSorder:"+sord);
			
			ArrayList<CustomerPaymentBean> aCustomerPaymentsList = itsCuMasterService.getCustomerPaymentsList(from, rows,sidx,sord);
			response.setRows(aCustomerPaymentsList);
			aTotalCount = itsCuMasterService.getPaymentsCount();
			response.setPage(page);
			response.setTotal((int) Math.ceil((double)aTotalCount.intValue() / (double) rows));
		}catch (CustomerException e) {
			sendTransactionException("<b>MethodName:</b>getAll","Customer",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			throw e;
		}
		return response;
	}
	
	@RequestMapping(value="/searchCustomerPayments",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getSearchList(
			//
			@RequestParam(value="searchText", required=false) String searchText,
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="fromdate", required=false) String fromDatestr,
			@RequestParam(value="todate", required=false) String toDatestr,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord, HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws CustomerException, IOException, ParseException, MessagingException {
		int from, to;
		Integer aTotalCount;
		CustomResponse response = new CustomResponse();
		try{
			Date fromDate =null,toDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(!fromDatestr.equals("")){
				fromDate = formatter.parse(fromDatestr);
			}
			if(!toDatestr.equals("")){
				toDate = formatter.parse(toDatestr);	
			}
			to = (rows * page);
			from = to - rows;
			ArrayList<CustomerPaymentBean> aCustomerPaymentsList = itsCuMasterService.getCustomerPaymentsListSearch(from, rows,sidx,sord,searchText,fromDate,toDate,status);
			response.setRows(aCustomerPaymentsList);
			aTotalCount = itsCuMasterService.getcountpaymentsListSearch(from,rows,searchText,fromDate,toDate,status);
			response.setPage(page);
			response.setTotal((int) Math.ceil(aTotalCount.intValue() / rows));
		}catch (CustomerException e) {
			sendTransactionException("<b>searchText:</b>"+searchText,"Customer",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			throw e;
		}
		return response;
	}
	
	
	@RequestMapping(value="/searchCustomerunappliedPayments",method = RequestMethod.GET)
	public @ResponseBody CustomResponse searchCustomerunappliedPayments(
			//
			@RequestParam(value="searchText", required=false) String searchText,
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="fromdate", required=false) String fromDatestr,
			@RequestParam(value="todate", required=false) String toDatestr,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord, HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws CustomerException, IOException, ParseException, MessagingException {
		int from, to;
		Integer aTotalCount;
		CustomResponse response = new CustomResponse();
		try{
			Date fromDate =null,toDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(!fromDatestr.equals("")){
				fromDate = formatter.parse(fromDatestr);
			}
			if(!toDatestr.equals("")){
				toDate = formatter.parse(toDatestr);	
			}
			to = (rows * page);
			from = to - rows;
			ArrayList<CustomerPaymentBean> aCustomerPaymentsList = itsCuMasterService.getCustomerUnappliedPaymentsListSearch(from, rows,sidx,sord,searchText,fromDate,toDate,status);
			response.setRows(aCustomerPaymentsList);
			aTotalCount = itsCuMasterService.getcountUnappliedPaymentsListSearch(from,rows,searchText,fromDate,toDate,status);
			
			response.setPage(page);
			response.setTotal((int) Math.ceil(aTotalCount.intValue() / rows));
		}catch (CustomerException e) {
			sendTransactionException("<b>searchText:</b>"+searchText,"Customer",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			throw e;
		}
		return response;
	}
	

	
	@RequestMapping(value="/showUnappliedAmount",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getSearchList(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord, HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws CustomerException, IOException, ParseException, MessagingException {
		int from, to;
		Integer aTotalCount;
		CustomResponse response = new CustomResponse();
		try{
			/*Date fromDate =null,toDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(!fromDatestr.equals("")){
				fromDate = formatter.parse(fromDatestr);
			}
			if(!toDatestr.equals("")){
				toDate = formatter.parse(toDatestr);	
			}*/
			
			//aTotalCount = itsJobService.getJobsCount();
			
			aTotalCount = itsCuMasterService.getUnappliedPaymentCount();
			to = (rows * page);
			from = to - rows;
			if (to > aTotalCount) to = aTotalCount;
			ArrayList<CustomerPaymentBean> aCustomerPaymentsList = itsCuMasterService.getCustomerPaymentUnapplied(from, rows,sidx,sord,null,null,null);
			response.setRows(aCustomerPaymentsList);
			response.setPage(page);
			response.setTotal((int) Math.ceil(aTotalCount.intValue() / rows));
		}catch (CustomerException exception) {
			sendTransactionException("<b>showUnappliedAmount:</b>showUnappliedAmount","Customer",exception,session,therequest);
			logger.error(exception.getMessage());
			theResponse.sendError(exception.getItsErrorStatusCode(), exception.getMessage());
			throw exception;
		}
		return response;
	}
	@RequestMapping(value="/cuInvoices", method = RequestMethod.GET)
	public @ResponseBody  CustomResponse getCustInvoices(@RequestParam(value="customerId", required=false) Integer customerID,
										@RequestParam(value="cuRecieptID", required=false) Integer cuRecieptID,
										@RequestParam(value="Oper", required=false) String Oper,
										@RequestParam(value="page", required=false) Integer page,
										@RequestParam(value="rows", required=false) Integer rows,
										@RequestParam(value="sidx", required=false) String sidx,
										@RequestParam(value="sord", required=false) String sord,HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		
		int aFrom, aTo;
		int aTotalCount;
		CustomResponse customresponse = new CustomResponse();
		List<CustomerPaymentBean> aCuInvoices = null;
		
		try {
			
			if(customerID!=null && cuRecieptID!=null)
			{
				aCuInvoices = (List<CustomerPaymentBean>) itsCuMasterService.getCustomerInvoices(customerID,cuRecieptID, Oper ,-1,-1,sidx,sord);
				aTotalCount = aCuInvoices.size();
				aTo = (rows * page);
				aFrom = aTo - rows;
				if (aTo > aTotalCount) aTo = aTotalCount;
				customresponse.setRows((List<CustomerPaymentBean>) itsCuMasterService.getCustomerInvoices(customerID,cuRecieptID, Oper ,aFrom,aTo,sidx,sord));
				customresponse.setPage(page);
				customresponse.setTotal((int) Math.ceil((double)aTotalCount / (double) rows));
				
			}
			
		} catch (CustomerException excep) {
			sendTransactionException("<b>customerID,cuRecieptID:</b>"+customerID+","+cuRecieptID,"Customer",excep,session,therequest);
			logger.error(excep.getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
		}
		return customresponse;
	}
	@RequestMapping(value="/getPreLoadData", method = RequestMethod.POST)
	public @ResponseBody  CuLinkageDetail getPreloadData(@RequestParam(value="customerID", required=false) Integer customerID,
														@RequestParam(value="recieptID", required=false) Integer recieptID, 
														@RequestParam(value="reversePaymentStatusID", required=false) Boolean reversePaymentStatusID, 
														HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CuLinkageDetail aCulinkagedetail = new CuLinkageDetail();
			try {
				aCulinkagedetail =itsCuMasterService.getCuRecieptData(customerID,recieptID,reversePaymentStatusID);
			} catch (CustomerException excep) { 
				sendTransactionException("<b>customerID,recieptID:</b>"+customerID+","+recieptID,"Customer",excep,session,therequest);
				logger.error(excep.getMessage(), excep);
				response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			}
			return aCulinkagedetail;
		}
	@RequestMapping(value="/jobDetails", method = RequestMethod.POST)
	public @ResponseBody  String[] getJobDettails(@RequestParam(value="acuInvoiceNumber", required=false) String invoiceNumber, HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		String[] jobDetails = new String[2];
			try {
				jobDetails =itsCuMasterService.getJobDetails(invoiceNumber);
			} catch (CustomerException excep) { 
				sendTransactionException("<b>invoiceNumber:</b>"+invoiceNumber,"Customer",excep,session,therequest);
				logger.error(excep.getMessage(), excep);
				response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			}
			return jobDetails;
		}
	@RequestMapping(value="/invoiceDetails", method = RequestMethod.POST)
	public @ResponseBody  Cuinvoice getInvoiceDetails(@RequestParam(value="acuInvoiceNumber", required=false) Integer invoiceNumber, HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, JobException, MessagingException {
		Cuinvoice aCuinvoice = new Cuinvoice();
			try {
				if(invoiceNumber !=null)
					aCuinvoice =itsJobService.getCustomerInvoiceDetails(invoiceNumber);
			} catch (JobException excep) { 
				sendTransactionException("<b>invoiceNumber:</b>"+invoiceNumber,"Customer",excep,session,therequest);
				logger.error(excep.getMessage(), excep);
				response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
			}
			return aCuinvoice;
		}
	
	
	@RequestMapping(value="/updateCompletePayment", method = RequestMethod.POST)
	public @ResponseBody  CustomResponse updateCompletePayment(@RequestParam(value="cuInvoiceId", required=false) Integer cuInvoiceId,
																		@RequestParam(value="customerID", required=false) Integer cusomterID,
																		@RequestParam(value="recieptID", required=false) Integer recieptID,
																		@RequestParam(value="appliedAmtName", required=false) BigDecimal aAppliedAmount,
																		@RequestParam(value="discountName", required=false) BigDecimal Discount,
																		@RequestParam(value="cuLinkageDetailID", required=false) Integer cuLinkageDetailID,
																		HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		
		CuLinkageDetail aCulinkagedetail = new CuLinkageDetail();
		aCulinkagedetail.setCuInvoiceId(cuInvoiceId);
		aCulinkagedetail.setCuLinkageDetailId(cuLinkageDetailID);
		aCulinkagedetail.setPaymentApplied(aAppliedAmount);
		aCulinkagedetail.setDiscountUsed(Discount);
		aCulinkagedetail.setRxCustomerId(cusomterID);
		aCulinkagedetail.setCuReceiptId(recieptID);
		logger.info("Applied Amount: "+aAppliedAmount);
		try {
			itsCuMasterService.updateCompleteInvoice(aCulinkagedetail);
		} catch (CustomerException excep) {
			sendTransactionException("<b>cuInvoiceId:</b>"+cuInvoiceId,"Customer",excep,session,therequest);
			logger.error(excep.getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
		}
		return null;
	}

	@RequestMapping(value="/payMultipleInvoice", method = RequestMethod.POST)
	public @ResponseBody  CustomResponse payMultipleInvoice(@RequestParam(value="rxcustomerID", required=false) Integer rxCustomerID,
															@RequestParam(value="recieptID", required=false) Integer receiptID,
															@RequestParam(value="recieptAmt", required=false) BigDecimal recieptAmt,
															@RequestParam(value="memo", required=false) String memo,
															@RequestParam(value="receiptDate", required=false) Date receiptDate,
															@RequestParam(value="reference", required=false) String reference,
															@RequestParam(value="cuReceiptTypeId", required=false) Byte cuReceiptTypeId,
															@RequestParam(value="discountAmt", required=false) BigDecimal discountAmt,
															@RequestParam(value="paidInvoiceDetails", required=false) String paidInvoiceDetails,
															HttpSession session,HttpServletResponse response,HttpServletRequest therequest) throws CommandException,BankingException, MessagingException, IOException, CompanyException {
		
		Cureceipt cuReceipt =new Cureceipt();
		
	
	UserBean aUserBean;
	aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);

	
		logger.info("Applied Amount: payMultipleInvoice");
		try {
			Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
			cuReceipt.setRxCustomerId(rxCustomerID);
			cuReceipt.setAmount(recieptAmt);
			cuReceipt.setMemo(memo);
			cuReceipt.setReceiptDate(receiptDate);
			cuReceipt.setReference(reference);
			cuReceipt.setCuReceiptTypeId(cuReceiptTypeId);
			cuReceipt.setCuReceiptId(receiptID);

			itsCuMasterService.savePayment(cuReceipt,aSysinfo.getCurrentFiscalYearId(),aSysinfo.getCurrentPeriodId(),aUserBean,paidInvoiceDetails,discountAmt);
			
			
		} catch (Exception excep) {
			sendTransactionException("<b>recieptID:</b>"+rxCustomerID,"Customer",excep,session,therequest);
			logger.error(excep.getMessage(), excep);
			//response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
		}
		return null;
	}
	
	
	
	@RequestMapping(value="/updateInvoice", method = RequestMethod.POST)
	public @ResponseBody  Integer updateInvoice(@RequestParam(value="cuInvoiceId", required=false) Integer cuInvoiceId,
													    @RequestParam(value="customerID", required=false) Integer cusomterID,
														@RequestParam(value="recieptID", required=false) Integer recieptID,
														@RequestParam(value="appliedAmtName", required=false) BigDecimal aAppliedAmount,
														@RequestParam(value="discountName", required=false) BigDecimal Discount,
														@RequestParam(value="cuLinkageDetailID", required=false) Integer cuLinkageDetailID,
														@RequestParam(value="opInvoiceID", required=false) Integer opInvoiceID,
													    @RequestParam(value="opInvoiceNo", required=false) String opInvoiceNo,
														@RequestParam(value="opAmount", required=false) BigDecimal opAmount,
														@RequestParam(value="opLinkageId", required=false) Integer opLinkageId,
														@RequestParam(value="amtApplied", required=false) BigDecimal amtApplied,
														@RequestParam(value="ogapAmt", required=false) BigDecimal ogapAmt,
														@RequestParam(value="statusCheck", required=false) Integer statusCheck,
														@RequestParam(value="oper", required=false) String oper,
														HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		
		Integer cuLinkId=0;
		
		CuLinkageDetail aCulinkagedetail = new CuLinkageDetail();
		aCulinkagedetail.setCuInvoiceId(cuInvoiceId);
		aCulinkagedetail.setCuLinkageDetailId(cuLinkageDetailID);
		aCulinkagedetail.setPaymentApplied(aAppliedAmount);
		aCulinkagedetail.setDiscountUsed(Discount);
		aCulinkagedetail.setRxCustomerId(cusomterID);
		aCulinkagedetail.setCuReceiptId(recieptID);
		aCulinkagedetail.setAmtApplied(ogapAmt);
		aCulinkagedetail.setStatusCheck(statusCheck);
		oper = "dueradio";
		
		logger.info("cuInvoiceId:"+cuInvoiceId+"\ncusomterID:"+cusomterID+"\n recieptID:"+recieptID+"\n aAppliedAmount:"+aAppliedAmount+"\n Discount:"+Discount+
				"\n opInvoiceID:"+opInvoiceID+"\n opInvoiceNo:"+opInvoiceNo+"\n opAmount:"+opAmount+"\n opLinkageId:"+opLinkageId+"\n ogapAmt:"+ogapAmt);
		try {
			
			cuLinkId = itsCuMasterService.updateInvoice(aCulinkagedetail,opLinkageId,opInvoiceID,opAmount,amtApplied,oper);
		} catch (CustomerException excep) {
			sendTransactionException("<b>cuInvoiceId:</b>"+cuInvoiceId,"Customer",excep,session,therequest);
			logger.error(excep.getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
		}
 		return cuLinkId;
	}
	
	@RequestMapping(value="/saveCustomerPayments", method = RequestMethod.POST)
	public @ResponseBody  Cureceipt getRecieptType(@RequestParam(value="customerIDName", required=false) Integer customerIDName,
																	@RequestParam(value="payAmountName", required=false) String amount,
																	@RequestParam(value="paymentDateName", required=false) Date paymentDate,
																	@RequestParam(value="paymentReciptTypeName", required=false) Byte paymentType,
																	@RequestParam(value="payCheckName", required=false) String checkNumber,
																	@RequestParam(value="payMemoName", required=false) String memo,
																	@RequestParam(value="yearid", required=false) Integer yearid,
																	@RequestParam(value="periodid", required=false) Integer periodid,
																	HttpSession session,HttpServletRequest therequest,HttpServletResponse response) throws IOException, MessagingException {
		Cureceipt aCureceipt = new Cureceipt();
		Integer aCureceiptID=0;
		try {
			

			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			aCureceipt.setReceiptDate(paymentDate);
			
			BigDecimal amt = new BigDecimal(amount.replaceAll("[$,]", ""));
			System.out.println("Amount:::::"+amt);
			aCureceipt.setAmount(amt);
			aCureceipt.setCuReceiptTypeId(paymentType);
			aCureceipt.setMemo(memo);
			aCureceipt.setRxCustomerId(customerIDName);
			aCureceipt.setReference(checkNumber);
		//	aCureceiptID = itsCuMasterService.savePayment(aCureceipt,yearid,periodid,aUserBean.getFullName());
			aCureceipt.setCuReceiptId(aCureceiptID);
			
			
			
		} catch (Exception excep) {
			sendTransactionException("<b>customerIDName:</b>"+customerIDName,"Customer",excep,session,therequest);
			logger.error(excep.getMessage(), excep);
			//response.sendError(excep.getItsErrorStatusCode(), excep.getMessage());
		}
		return aCureceipt;
	}
	
	@RequestMapping(value="/deleteCustomerPayments", method = RequestMethod.POST)
	public @ResponseBody  void deleteCusotmerPayments(@RequestParam(value="cuReceiptID", required=false) Integer cuReceiptID,
																@RequestParam(value="rxMasterID", required=false) Integer cusomterID,
																HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		Cureceipt aReciept = new Cureceipt();
		try {
		aReciept.setCuReceiptId(cuReceiptID);
		aReciept.setRxCustomerId(cusomterID);
			itsCuMasterService.deletePayment(aReciept);
		} catch (CustomerException e) {
			sendTransactionException("<b>cuReceiptID:</b>"+cuReceiptID,"Customer",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			e.printStackTrace();
		}
	}
	@RequestMapping(value="/searchCustomerPayment", method = RequestMethod.GET)
	public @ResponseBody List<?> getJobSearch(@RequestParam(value="term", required=false) String searchText,
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="fromdate", required=false) String fromDatestr,
			@RequestParam(value="todate", required=false) String toDatestr) throws ParseException{
		Date fromDate =null,toDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(fromDatestr!=null && !fromDatestr.equals("")){
				fromDate = formatter.parse(fromDatestr);
			}
			if(toDatestr!=null && !toDatestr.equals("")){
				toDate = formatter.parse(toDatestr);	
			}
		AutoCompleteBean aSearchJob = new AutoCompleteBean();
		List<?> aJobSearch = itsCuMasterService.getCustomerPaymentSearchList(aSearchJob, searchText,fromDate,toDate,status);
		return aJobSearch;
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
	
	
	@RequestMapping(value = "/reversePaymentvalidation", method = RequestMethod.POST)
	public @ResponseBody  boolean reversePaymentvalidation(@RequestParam(value="recieptID", required=false) Integer receiptID, 
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, JobException, MessagingException {
		
		boolean reversePayment = false;
		try{
			reversePayment=itsCuMasterService.getreversePaymentvalidation(receiptID);
		}catch(Exception ex){
			ex.printStackTrace();}
		
		
		return reversePayment;
	}
	
	
	@RequestMapping(value = "/applyReversePayment", method = RequestMethod.POST)
	public @ResponseBody  boolean applyReversePayment(@RequestParam(value="recieptID", required=false) Integer receiptID, 
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, JobException, MessagingException {
		
		boolean reversePayment = false;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		try{
			
			Sysinfo aSysinfo = accountingCyclesService.getSysinfo();
			itsCuMasterService.processReversePayments(receiptID,aUserBean,aSysinfo.getCurrentFiscalYearId(),aSysinfo.getCurrentPeriodId());
			
		}catch(Exception ex){
			ex.printStackTrace();}
		
		return reversePayment;
	}
	
	
}

