package com.turborep.turbotracker.banking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlTransactionv1;
import com.turborep.turbotracker.banking.dao.MoAccount;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.BankingService;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebill;

@Controller
@RequestMapping("/gltransaction")
public class GltransactionController {
	protected static Logger logger = Logger
			.getLogger(GltransactionController.class);

	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;

	@RequestMapping(value = "/addgltransactionrecord", method = RequestMethod.POST)
	public @ResponseBody
	Motransaction printChecks(
			@RequestParam(value = "bankAccounts", required = false) Integer bankAccountsID,
			@RequestParam(value = "checksDate", required = false) Date checkDate,
			@RequestParam(value = "checkType", required = false) String checkType,
			@RequestParam(value = "checkNumber", required = false) Integer checkNumber,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		Motransaction aMotransaction = new Motransaction();
		UserBean aUserBean = null;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (checkType.equalsIgnoreCase("Vendorchecks"))
				aMotransaction.setCheckType((short) 1);
			else
				aMotransaction.setCheckType((short) 2);
			aMotransaction.setTransactionDate(checkDate);
			aMotransaction.setMoAccountId(bankAccountsID);
			aMotransaction.setPrinted((byte) 1);
			aMotransaction.setMoTransactionTypeId((short) 2);
			logger.info("MoAccountId==" + aMotransaction.getMoAccountId());

			Coaccount coaccount = gltransactionService.getcoAccountDetails(aMotransaction);
			logger.info("Account number=" + coaccount.getNumber()
					+ "==coaccountid==" + coaccount.getCoAccountId());

			SysAccountLinkage aSysAccountLinkage = gltransactionService
					.getsysAccountLinkageDetail();
			Coaccount CoAccountIdapdetails = gltransactionService
					.getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage
							.getCoAccountIdap());
			Coaccount CoAccountIdfreightdetails = gltransactionService
					.getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage
							.getCoAccountIdfreight());
			Coaccount CoAccountIdsalesTaxPaiddetails = gltransactionService
					.getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage
							.getCoAccountIdsalesTaxPaid());

			logger.info("CoAccountIdap="
					+ aSysAccountLinkage.getCoAccountIdap()
					+ "CoAccountIdfreight="
					+ aSysAccountLinkage.getCoAccountIdfreight()
					+ "CoAccountIdsalesTaxPaid="
					+ aSysAccountLinkage.getCoAccountIdsalesTaxPaid()
					+ "CoAccountIddiscountsTaken"
					+ aSysAccountLinkage.getCoAccountIddiscountsTaken());
			logger.info("CoAccountIdapnumber=="
					+ CoAccountIdapdetails.getNumber()
					+ "CoAccountIdfreightnumber"
					+ CoAccountIdfreightdetails.getNumber()
					+ "CoAccountIdsalesTaxPaidnumber"
					+ CoAccountIdsalesTaxPaiddetails.getNumber());

			logger.info("==CoFiscalPeriodId== ==Period== ==StartDate==  ==EndDate==");
			Cofiscalperiod aCofiscalperiod = gltransactionService
					.getCofiscalPeriodDetail();
			logger.info("==" + aCofiscalperiod.getCoFiscalPeriodId() + "=="
					+ aCofiscalperiod.getPeriod() + "=="
					+ aCofiscalperiod.getStartDate() + "=="
					+ aCofiscalperiod.getEndDate() + "==");

			logger.info("==CoFiscalYearId== ==StartDate== ==EndDate==  ==FiscalYear==");
			Cofiscalyear aCofiscalyear = gltransactionService
					.getCofiscalYearDetail();
			logger.info("==" + aCofiscalyear.getCoFiscalYearId() + "=="
					+ aCofiscalyear.getStartDate() + "=="
					+ aCofiscalyear.getEndDate() + "=="
					+ aCofiscalyear.getFiscalYear() + "==");

			logger.info("==CoLedgerSourceId== ==Description== ==JournalID== ");
			Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("VB");
			logger.info("==" + aColedgersource.getCoLedgerSourceId() + "=="
					+ aColedgersource.getDescription() + "=="
					+ aColedgersource.getJournalID() + "==");

			// boolean
			// saveornot=gltransactionService.saveGlTransactionTable(aMotransaction,coaccount,aSysAccountLinkage,vebilllist,aCofiscalperiod,aCofiscalyear,aColedgersource);

			ArrayList<Vebill> vebilllist = gltransactionService
					.getveBillDetailsList(aMotransaction);
			logger.info("==vebillid== ==BillAmount== ==TaxAmount== ==FrieghtAmount== AppliedAmount");

			int glTransationid = 0;

			for (Vebill aVebill : vebilllist) {
				logger.info("==" + aVebill.getVeBillId() + "== =="
						+ aVebill.getBillAmount() + "== =="
						+ aVebill.getTaxAmount() + "== =="
						+ aVebill.getFreightAmount() + "=="
						+ aVebill.getAppliedAmount());

				/*********************** GL Transaction ends here *******************************/

				GlTransactionv1 glTransaction = new GlTransactionv1();
				GlLinkage glLinkage = new GlLinkage();

				// period
				glTransaction.setCoFiscalPeriodId(aCofiscalperiod
						.getCoFiscalPeriodId());
				glTransaction.setPeriod(aCofiscalperiod.getPeriod());
				glTransaction.setpStartDate(aCofiscalperiod.getStartDate());
				glTransaction.setpEndDate(aCofiscalperiod.getEndDate());

				// year
				glTransaction.setCoFiscalYearId(aCofiscalyear
						.getCoFiscalYearId());
				glTransaction.setFyear(Integer.parseInt(aCofiscalyear
						.getFiscalYear()));
				glTransaction.setyStartDate(aCofiscalyear.getStartDate());
				glTransaction.setyEndDate(aCofiscalyear.getEndDate());

				// journal
				glTransaction.setJournalId(aColedgersource.getJournalID());
				glTransaction.setJournalDesc(aColedgersource.getDescription());
				glTransaction.setEntrydate(new Date());

				// Subtotal

				// coAccount
				glTransaction.setCoAccountId(CoAccountIdapdetails
						.getCoAccountId());
				glTransaction.setCoAccountNumber(CoAccountIdapdetails
						.getNumber());

				// Amount

				float subTotalAmount = aVebill.getBillAmount().floatValue()
						- (aVebill.getTaxAmount().floatValue() + aVebill
								.getFreightAmount().floatValue());

				glTransaction.setAmount(new BigDecimal(subTotalAmount));

				if (aVebill.getBillAmount().compareTo(BigDecimal.ZERO) != 0) {
					// glTransationid=gltransactionService.saveGltransactionTable(glTransaction);

					logger.info("coLedgerSourceId == "
							+ aColedgersource.getCoLedgerSourceId()
							+ "GlTransactionid == " + glTransationid
							+ "VeBillid == " + aVebill.getVeBillId());

					glLinkage.setCoLedgerSourceId(aColedgersource
							.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(aVebill.getVeBillId());

					gltransactionService.saveGlLinkageTable(glLinkage);

				}

				// Freight

				// coAccount
				glTransaction.setCoAccountId(CoAccountIdfreightdetails
						.getCoAccountId());
				glTransaction.setCoAccountNumber(CoAccountIdfreightdetails
						.getNumber());

				// Amount

				glTransaction.setAmount(aVebill.getFreightAmount());

				if (aVebill.getFreightAmount().compareTo(BigDecimal.ZERO) != 0) {
					// glTransationid=gltransactionService.saveGltransactionTable(glTransaction);

					logger.info("coLedgerSourceId=="
							+ aColedgersource.getCoLedgerSourceId()
							+ "GlTransactionid==" + glTransationid
							+ "VeBillid==" + aVebill.getVeBillId());

					glLinkage.setCoLedgerSourceId(aColedgersource
							.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(aVebill.getVeBillId());

					gltransactionService.saveGlLinkageTable(glLinkage);

				}

				// Tax

				// coAccount
				glTransaction.setCoAccountId(CoAccountIdsalesTaxPaiddetails
						.getCoAccountId());
				glTransaction.setCoAccountNumber(CoAccountIdsalesTaxPaiddetails
						.getNumber());

				// Amount
				glTransaction.setAmount(aVebill.getTaxAmount());

				if (aVebill.getTaxAmount().compareTo(BigDecimal.ZERO) != 0) {
					// glTransationid=gltransactionService.saveGltransactionTable(glTransaction);

					logger.info("coLedgerSourceId=="
							+ aColedgersource.getCoLedgerSourceId()
							+ "GlTransactionid==" + glTransationid
							+ "VeBillid==" + aVebill.getVeBillId());

					glLinkage.setCoLedgerSourceId(aColedgersource
							.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(aVebill.getVeBillId());

					gltransactionService.saveGlLinkageTable(glLinkage);

				}

				// Main bank account debit

				float tot_amt = subTotalAmount
						+ aVebill.getTaxAmount().floatValue()
						+ aVebill.getFreightAmount().floatValue();
				glTransaction.setCoAccountId(coaccount.getCoAccountId());
				glTransaction.setCoAccountNumber(coaccount.getNumber());

				// Amount
				glTransaction.setAmount(new BigDecimal(-tot_amt));

				if (tot_amt != 0.0) {
					// glTransationid=gltransactionService.saveGltransactionTable(glTransaction);

					logger.info("coLedgerSourceId=="
							+ aColedgersource.getCoLedgerSourceId()
							+ "GlTransactionid==" + glTransationid
							+ "VeBillid==" + aVebill.getVeBillId());

					glLinkage.setCoLedgerSourceId(aColedgersource
							.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(aVebill.getVeBillId());

					gltransactionService.saveGlLinkageTable(glLinkage);

				}

				/*********************** GL Transaction ends here *******************************/

			}
			logger.info("==END==");

		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>MoTransactionID:</b>"+aMotransaction.getMoTransactionId()
					 +"<br><b>RxMasterID:</b>"+aMotransaction.getRxMasterId(),"Banking.",e,session,request);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aMotransaction;
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
