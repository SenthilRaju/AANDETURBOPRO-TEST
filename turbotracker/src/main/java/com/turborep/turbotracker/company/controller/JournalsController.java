package com.turborep.turbotracker.company.controller;

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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.dao.journalentryhistory;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgerdetail;
import com.turborep.turbotracker.company.dao.Coledgerheader;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.finance.dao.GlTransactionTest;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/journalscontroller")
public class JournalsController {

	protected static Logger logger = Logger.getLogger(CompanyController.class);

	@Resource(name = "journalsService")
	private com.turborep.turbotracker.company.service.JournalsService JournalsService;
	
	@Resource(name = "sysService")
	private SysService itsSysService;
	
	@Resource(name = "jobService")
	private JobService jobService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@RequestMapping(value = "/getListOfJournals", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getJournalsGrid(@RequestParam(value="page", required=false) Integer page,
														@RequestParam(value="rows", required=false) Integer rows,
														@RequestParam(value="sidx", required=false) String sidx,
														@RequestParam(value="sord", required=false) String sord,
														HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		CustomResponse aResponse = null;
	
			try {
				aResponse = new CustomResponse();
				Integer aTotalCount = JournalsService.getJournalTotalCount();
				System.out.println("TotalCount::"+aTotalCount);
				int aFrom, aTo;
				aTo = (rows * page);
				aFrom = aTo - rows;
				if (aTo > aTotalCount)
					aTo = aTotalCount;
				List<?> accounts = JournalsService.getTotalJournals(aFrom,aTo);
				logger.debug("Retriving all the data of customer is start");
				aResponse.setRows(accounts);
				aResponse.setRecords(String.valueOf(accounts.size()));
				aResponse.setPage(page);
				logger.debug("Retriving all the data of customer is end");
				aResponse.setTotal((int) Math.ceil((double) aTotalCount / (double) rows));
			} catch (CompanyException e) {
				logger.error(e.getMessage(), e);
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
				sendTransactionException("<b>Tracking ID:</b> getListOfJournals","Journals",e,session,request);
			}
		return aResponse;
	}
	
	@RequestMapping(value = "/getListOfJournalDetails", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getJournalDetailsgrid(@RequestParam(value="page", required=false) Integer page,
														@RequestParam(value="rows", required=false) Integer rows,
														@RequestParam(value="sidx", required=false) String sidx,
														@RequestParam(value="sord", required=false) String sord,
														@RequestParam(value="coLedgerHeaderId", required=false) String coLedgerHeaderId,
														HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
	
			try {
				List<GlTransaction> accounts = null;
				
				if(!coLedgerHeaderId.equals("-1"))
				accounts = JournalsService.getTotalJournalDetails(coLedgerHeaderId);
				response.setRows(accounts);

			} catch (CompanyException e) {
				logger.error(e.getMessage(), e);
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
				sendTransactionException("<b>Tracking ID:</b> getListOfJournalDetails","Banking",e,session,request);
			}
		return response;
	}
	@RequestMapping(value = "/updateData", method = RequestMethod.POST)
	public @ResponseBody String updateJournalsData(@RequestParam(value = "headerid", required = false) Integer headerId,
															@RequestParam(value = "fisicalperiodid", required = false) Integer fisicalperiodid,
															@RequestParam(value = "coLedgerSourceId", required = false) Integer coLedgerSourceId,
															@RequestParam(value = "DateOfJournal", required = false) String DateOfJournal,
															@RequestParam(value = "decriptionNameID", required = false) String decriptionNameID,
															@RequestParam(value = "References", required = false) String References,
															@RequestParam(value = "coAccountNumber", required = false) String number,
															@RequestParam(value = "credit", required = false) String creditAmount,
															@RequestParam(value = "debit", required = false) String debitAmount,
															@RequestParam(value = "accountId", required = false) String coAccountId,
															@RequestParam(value = "gridData", required = false) String gridData,
															@RequestParam(value = "operation", required = false) String operation,
															@RequestParam(value = "formchange", required = false) String formchange,
															@RequestParam(value = "gridchange", required = false) String gridchange,
															@RequestParam(value = "reason", required = false) String reason,
															@RequestParam(value = "coFiscalPeriodId", required = false) Integer coFiscalPeriodId,
															@RequestParam(value = "coFiscalYearId", required = false) Integer coFiscalYearId,
															HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws Exception {
		
		logger.info("Journal Date: "+DateOfJournal);
		logger.info("headerid: "+headerId +"\n"+"number:"+number+"\n"+"creditAmount:"+creditAmount+"\n"+"debitAmount:"+debitAmount+"\nAccountID: "+coAccountId);
		Integer newInsertStatus = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		String jeReference=null;
		Date date=new Date();
		int deleteState=0;
		String debit = "";
		String credit = "";
		if(DateOfJournal!=null){
		try {
			date = formatter.parse(DateOfJournal);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		}
		logger.info("Description: "+decriptionNameID);;
		Coledgersource aColedgersource =new Coledgersource();
		Cofiscalperiod aCofiscalperiod = new Cofiscalperiod();
		Cofiscalyear aCofiscalyear = new Cofiscalyear();
		try {
			aColedgersource = gltransactionService.getColedgersourceDetail( "JE");
			aCofiscalperiod = accountingCyclesService.getCurrentPeriod(coFiscalPeriodId);//gltransactionService.getCofiscalPeriodDetail(amotransaction);
			aCofiscalyear = accountingCyclesService.getCurrentYear(coFiscalYearId);
		} catch (BankingException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("operation==="+operation);
		
		
				/*try {
					deleteState = JournalsService.deleteGlTransactionRecord(References);
				} catch (CompanyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				
				if(operation.equals("edit")||operation.equals("delete")){
					Coledgersource coledger=new Coledgersource();
					coledger.setCoLedgerSourceId(1300);
					//RollBack Operation
					
					GlRollback glRollback = new GlRollback();
					glRollback.setVeBillID(JobUtil.ConvertintoInteger(References));
					glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
					glRollback.setPeriodID(coFiscalPeriodId);
					glRollback.setYearID(coFiscalYearId);
					glRollback.setTransactionDate(date);					
					
					ArrayList<GlTransaction> glList =JournalsService.rollBackGlTransaction(glRollback);
				}
				
				if(operation.equals("edit")||operation.equals("delete")){
					journalentryhistory ajournalentryhistory=new journalentryhistory();
					ajournalentryhistory.setCreateddate(new Date());
					ajournalentryhistory.setEditordelete(operation);
					ajournalentryhistory.setReasondesc(reason);
					ajournalentryhistory.setReference(References);
					
					try {
						JournalsService.CreateJournalEntriesHistory(ajournalentryhistory);
					} catch (CompanyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(!operation.equals("delete")){
				if(deleteState>=0 || deleteState==-1){
					
				JsonParser parser = new JsonParser();
				if ( gridData!=null) {
					JsonElement ele = parser.parse(gridData.replaceAll("#", "&"));
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						String hstatus=obj.get("hiddenstatus").getAsString();
						int hiddenstatus=0;
						System.out.println("=="+hstatus+"=="+hstatus.trim()+"==");
						if(hstatus!=null && !hstatus.equals("") && hstatus.trim()!=""){
							hiddenstatus=obj.get("hiddenstatus").getAsInt();
						}
						String glTransactionIdString=obj.get("glTransactionId").getAsString();
						Integer glTransactionId=0;
						if(glTransactionIdString!=null && !glTransactionIdString.equals("") && glTransactionIdString.trim()!=""){
							glTransactionId=obj.get("glTransactionId").getAsInt();
						}
						
						System.out.println("hiddenstatus=====>"+hiddenstatus);
						GlTransaction aGlTransaction = new GlTransaction();
						
						
						//aGlTransaction.setGlTransactionId(glTransactionId);
						aGlTransaction.setTransactionDesc(decriptionNameID==null?" ":decriptionNameID);
						aGlTransaction.setPoNumber(References);
						aGlTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
						aGlTransaction.setPeriod(aCofiscalperiod.getPeriod());
						aGlTransaction.setpStartDate(aCofiscalperiod.getStartDate());
						aGlTransaction.setpEndDate(aCofiscalperiod.getEndDate());
						aGlTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
						aGlTransaction.setFyear(aCofiscalyear.getFiscalYear()==null?"0":aCofiscalyear.getFiscalYear());
						aGlTransaction.setyStartDate(aCofiscalyear.getStartDate());
						aGlTransaction.setyEndDate(aCofiscalyear.getEndDate());
						aGlTransaction.setJournalId(aColedgersource.getJournalID());
						aGlTransaction.setJournalDesc(aColedgersource.getDescription());
						aGlTransaction.setEntrydate(date);
						aGlTransaction.setTransactionDate(date);
						aGlTransaction.setEnteredBy(aUserBean.getFullName());
						aGlTransaction.setCoAccountId(obj.get("coAccountId").getAsInt());
						String coAccountNo = obj.get("coAccountNumber").getAsString();
						String[] acNoArray = coAccountNo.split("#");
						logger.info("Ac NO:"+coAccountNo +"  ----  "+acNoArray[0]);
						aGlTransaction.setCoAccountNumber(acNoArray[0]);
						credit = obj.get("credit").getAsString().replaceAll(",","");
						debit = obj.get("debit").getAsString().replaceAll(",","");
						aGlTransaction.setCredit( new BigDecimal(credit));
						aGlTransaction.setDebit(new BigDecimal(debit));
						if(hiddenstatus==0){
							aGlTransaction.setJereference(jeReference);
							Integer returnvalue=JournalsService.AddnewJournalEntries(aGlTransaction,operation);
							jeReference=String.valueOf(returnvalue);
							References=String.valueOf(returnvalue);
						}
						
						
						/**
						 * While edit the journal entries all the old data should swap 
						 * and insert the new record 
						 * Started here for edit
						 * */
						
						/*if(operation.equals("edit")){
							String olAccountId=obj.get("oldcoAccountId").getAsString();
							if(obj.get("oldcoAccountId")!=null && !obj.get("oldcoAccountId").getAsString().equals("") && obj.get("oldcoAccountId").getAsString().length()!=0){
							aGlTransaction.setCoAccountId(obj.get("oldcoAccountId").getAsInt());
							String oldcoAccountNo = obj.get("oldcoAccountNumber").getAsString();
							aGlTransaction.setCoAccountNumber(oldcoAccountNo);
							String oldcredit = obj.get("oldcredit").getAsString().replaceAll(",","");
							String olddebit = obj.get("olddebit").getAsString().replaceAll(",","");
							aGlTransaction.setCredit( new BigDecimal(olddebit));
							aGlTransaction.setDebit(new BigDecimal(oldcredit));
							try {
								if(hiddenstatus==0){
									GlTransaction thegltrans = JournalsService.updateJournalEntries(aGlTransaction,1,operation);
									newInsertStatus=thegltrans.getGlTransactionId();
								}
							} catch (CompanyException e) {
								sendTransactionException("<b>headerId:</b> "+headerId,"Banking",e,session,request);
								e.printStackTrace();
							}
							}
						}*/
						
						/*End edit functionality*/
						
						
						
						
						/*String swap="";
						int status=0;
						if(operation.equals("delete")){
							swap=credit;
							credit=debit;
							debit=swap;
							status=1;
						}*/
						
					
						/*try {
							if(hiddenstatus==0){
							if(operation.equals("add")){
								aGlTransaction.setJereference(jereference);
								aGlTransaction.setPoNumber(jereference);
								GlTransaction thegltrans = JournalsService.updateJournalEntries(aGlTransaction,status,operation);
								newInsertStatus=thegltrans.getGlTransactionId();
								jereference=thegltrans.getPoNumber();
							}else{
								GlTransaction thegltrans = JournalsService.updateJournalEntries(aGlTransaction,status,operation);
								newInsertStatus=thegltrans.getGlTransactionId();
							}	
							
							
							}
						} catch (CompanyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							sendTransactionException("<b>headerId:</b> "+headerId,"Banking",e,session,request);
						}*/
						
					}
					
					
					
					
					
					}
				}
				}
				
		return References;
	}
	
	
	@RequestMapping(value = "/getAccountDetails", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getProductList(@RequestParam("term") String theProductText,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> aProductList = null;
		try {
			aProductList = (ArrayList<AutoCompleteBean>) jobService
					.getAccountDetails(theProductText);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> "+theProductText,"Banking",e,session,request);
		}
		return aProductList;
	}

	
	
	@RequestMapping(value = "/getRreferenceNo", method = RequestMethod.POST)
	public @ResponseBody Integer updateJournalsData(HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		
		Integer seqNumber = 0;
		
		try {
				seqNumber = itsSysService.getSequenceNumber("JournalEntry");
				
				
			} catch (Exception e) 
			{
			
				e.printStackTrace();
				sendTransactionException("<b>JournalEnry Sequence:</b> "+seqNumber,"Banking",e,session,request);
			}
			
		return seqNumber;
	}
	
	@RequestMapping(value = "/updateJournalDetails", method = RequestMethod.POST)
	public @ResponseBody CustomResponse updateJournalsDetails(@RequestParam(value = "accountid", required = false) Integer accountid,
																		@RequestParam(value = "headerid", required = false) Integer headerid,
																		@RequestParam(value = "description", required = false) String description,
																		@RequestParam(value = "number", required = false) String number,
																		@RequestParam(value = "amount", required = false) Integer amount,
																		HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		
		
		
		CustomResponse response = new CustomResponse();
		try {
		Coaccount aCoaccount = new Coaccount();
		aCoaccount.setDescription(description);
		aCoaccount.setNumber(number);
		aCoaccount.setCoAccountId(accountid);

		Coledgerdetail aColedgerdetail = new Coledgerdetail();
		aColedgerdetail.setAmount(new BigDecimal(amount));
		aColedgerdetail.setCoAccountId(accountid);
		aColedgerdetail.setCoLedgerHeaderId(headerid);
		
		JournalsService.updateJournalDetails(aCoaccount,aColedgerdetail);
		} catch (CompanyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>Account ID:</b> "+accountid,"Banking",e,session,request);
		}
			
		return response;
	}
	@RequestMapping(value = "/deleteRecord", method = RequestMethod.POST)
	public @ResponseBody CustomResponse deleteJournal(@RequestParam(value = "referenceID", required = false) String headerId,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		
		
		logger.info("Header ID:"+headerId);
		
		CustomResponse response = new CustomResponse();
		try {
			JournalsService.deleterecord(headerId);
		} catch (CompanyException e) {
			sendTransactionException("<b>headerId:</b> "+headerId,"Banking",e,session,request);
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping(value = "/getListOfJournalshistory", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getListOfJournalshistory(@RequestParam(value="page", required=false) Integer page,
														@RequestParam(value="rows", required=false) Integer rows,
														@RequestParam(value="sidx", required=false) String sidx,
														@RequestParam(value="sord", required=false) String sord,
														@RequestParam(value = "reference", required = false) String reference,
														HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
	
			try {
				logger.info("Test:: I am here");
				List<?> accounts = JournalsService.getListOfJournalshistory(reference);
				response.setRows(accounts);
			} catch (CompanyException e) {
				sendTransactionException("<b>reference:</b> "+reference,"Banking",e,session,request);
				logger.error(e.getMessage(), e);
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
		return response;
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
