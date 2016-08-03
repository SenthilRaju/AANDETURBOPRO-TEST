package com.turborep.turbotracker.finance.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.finance.dao.GlReportingAccounts;
import com.turborep.turbotracker.finance.dao.GlTransactionTest;
import com.turborep.turbotracker.finance.dao.TrialBalance;
import com.turborep.turbotracker.finance.exception.DrillDownException;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.util.JobUtil;


@Service("drillDownService")
@Transactional
public class DrillDownServiceImpl implements DrillDownService  {
	
	public static Logger itsLogger = Logger.getLogger("service");
	
	@Resource(name="sessionFactory")
	public SessionFactory itsSessionFactory;
	
	@Resource(name="chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;

	/**
	 * Created by : Leo    Date: 12-09-2014
	 * Description: Account Tab Report Generate.
	 * Tables Used : glTransaction
	 * 
	 * */
	
	@Override
	public List<GlReportingAccounts> getAccountDetails(String accountNumber,String accDescription,String periodFromID,String periodTo,String yearID,Integer coAccountsID)	throws DrillDownException {
		
		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>(); 
		ArrayList aQuery= new ArrayList();
		String fisclYear ="";
		Integer fiscalYearInt = 0;
		Integer fiscalPeriod =null;
		Integer searchPeriodFrom =Integer.parseInt(periodFromID);
		Integer searchPeriodTo =Integer.parseInt(periodTo);
		//Integer searchYear =Integer.parseInt(yearID);
		fiscalYearInt = getCurrentFiscalYear("SELECT coy.coFiscalYearId FROM coFiscalYear coy where coy.fiscalYear = "+yearID);
		fiscalPeriod = getCurrentFiscalPeriod("SELECT cop.coFiscalPeriodId FROM coFiscalPeriod cop where cop.period = "+periodTo+" and cop.coFiscalYearId = "+fiscalYearInt);
		
		
		itsLogger.info("fiscalYearInt"+fiscalYearInt+"\n"+"searchYear"+yearID+"\n"+"searchPeriodFrom"+searchPeriodFrom+"\n"+"searchPeriodTo"+searchPeriodTo+"\n"+"fiscalPeriod"+fiscalPeriod+"\n");
		String accDetailsQueryString ="";
		
		accDetailsQueryString="SELECT period, SUM(credit),SUM(debit),coFiscalPeriodId,coFiscalYearId FROM glTransaction WHERE period BETWEEN '"+periodFromID+"' AND '"+searchPeriodTo+"' AND fyear='"+yearID+"' AND coAccountId = '"+coAccountsID+"' GROUP BY period";
		GlReportingAccounts glRepAccObj=null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery =  (ArrayList) aSession.createSQLQuery(accDetailsQueryString).list();	
			
			
			/*Query query = aSession.createSQLQuery(
					"CALL GetStocks(:stockCode)")
					.addEntity(GlReportingAccounts.class)
					.setParameter("stockCode", "7277");
							
				List result = query.list();
				for(int i=0; i<result.size(); i++){
					GlReportingAccounts glRepAccObj1 = (GlReportingAccounts)result.get(i);
				//	System.out.println(stock.getStockCode());
				}
			
			
			*/
			
			BigDecimal credit = null;
			BigDecimal debit = null;
			
			
		
			if(aQuery.size()>0)
			{
			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj=new GlReportingAccounts();
				Object[] aObj = (Object[])aIterator.next();		
				
				credit = new BigDecimal(0);
				debit = new BigDecimal(0);
				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber(accountNumber);
				glRepAccObj.setCoAccountDesc(accDescription);
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod((Integer)aObj[0]);
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));
				
				glRepAccObj.setJournalID("");
				glRepAccObj.setJournalDesc("");
				glRepAccObj.setJournalBalance(new BigDecimal(0));			
				
				glRepAccObj.setSourceid(0);
				glRepAccObj.setDescription("");
				glRepAccObj.setEntrydate("");
				glRepAccObj.setGltransactionid(0);
				
				credit = (BigDecimal)aObj[1];
				debit = (BigDecimal)aObj[2];
								
				glRepAccObj.setCredit(credit);
				glRepAccObj.setDebit(debit);
				
				//------------Edited By Leo ID#497
				
					//List<GlTransaction> lstLedgerDetailPeriod=chartOfAccountsService.getLedgerDetails(coAccountsID,(Integer)aObj[3], (Integer)aObj[4]);
					//glRepAccObj.setPeriodbalance(lstLedgerDetailPeriod.get(0).getBankClosingBalance());
					
				   //calculating  closing balance for a particular period
					List<GlTransaction> lstLedgerDetailPeriod =chartOfAccountsService.getLedgerDetailscurentPeriod(coAccountsID, (Integer)aObj[3], (Integer)aObj[4]);
					
					if(lstLedgerDetailPeriod.size()>0)
					{
					if((lstLedgerDetailPeriod.get(0).getBankClosingBalance()).compareTo(BigDecimal.ZERO)<0)
					glRepAccObj.setpBalance(lstLedgerDetailPeriod.get(0).getBankClosingBalance().negate()+" CR");
					else
					glRepAccObj.setpBalance(lstLedgerDetailPeriod.get(0).getBankClosingBalance()+" DB");
					}
					else
					{
						glRepAccObj.setpBalance(BigDecimal.ZERO+" CR");
					}
						    
					//List<GlTransaction> lstLedgerDetail =chartOfAccountsService.getLedgerDetailsPeriod(coAccountsID, (Integer)aObj[3], (Integer)aObj[4]);
					//glRepAccObj.setYtdBalance(lstLedgerDetail.get(0).getBankClosingBalance());
					
					//calculating  closing balance for all period
					List<GlTransaction> lstLedgerDetail =chartOfAccountsService.getLedgerDetailsPeriod(coAccountsID, (Integer)aObj[3], (Integer)aObj[4]);
					if(lstLedgerDetail.size()>0)
					{
					if((lstLedgerDetail.get(0).getBankClosingBalance()).compareTo(BigDecimal.ZERO)<0)
					glRepAccObj.setyBalance(lstLedgerDetail.get(0).getBankClosingBalance().negate()+" CR");
					else
					glRepAccObj.setyBalance(lstLedgerDetail.get(0).getBankClosingBalance()+" DB");
					}
					else
					{
						glRepAccObj.setpBalance(BigDecimal.ZERO+" CR");
					}
				
				//-----------
				
				aQueryList.add(glRepAccObj);
			}
			}
			else
			{
				glRepAccObj=new GlReportingAccounts();
				credit = new BigDecimal(0);
				debit = new BigDecimal(0);
				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber(accountNumber);
				glRepAccObj.setCoAccountDesc(accDescription);
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod(Integer.parseInt(periodTo));
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));
				glRepAccObj.setJournalID("");
				glRepAccObj.setJournalDesc("");
				glRepAccObj.setJournalBalance(new BigDecimal(0));			
				glRepAccObj.setSourceid(0);
				glRepAccObj.setDescription("");
				glRepAccObj.setEntrydate("");
				glRepAccObj.setGltransactionid(0);
				glRepAccObj.setCredit(credit);
				glRepAccObj.setDebit(debit);
				glRepAccObj.setpBalance(BigDecimal.ZERO+" CR");
				
					    
				List<GlTransaction> lstLedgerDetail =chartOfAccountsService.getLedgerDetailsPeriod(coAccountsID, fiscalPeriod,fiscalYearInt);
				if(lstLedgerDetail.size()>0)
				{
				if((lstLedgerDetail.get(0).getBankClosingBalance()).compareTo(BigDecimal.ZERO)<0)
				glRepAccObj.setyBalance(lstLedgerDetail.get(0).getBankClosingBalance().negate()+" CR");
				else
				glRepAccObj.setyBalance(lstLedgerDetail.get(0).getBankClosingBalance()+" DB");
				}
				else
				{
				glRepAccObj.setyBalance(BigDecimal.ZERO+" CR");
				}
				
				aQueryList.add(glRepAccObj);
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			accDetailsQueryString=null;aQuery=null;
		}
		return aQueryList;
	}
	
	
	
	@Override
	public List<GlTransactionTest> getAccountDetails1(String accountNumber,String periodFromID,String periodTo,String yearID,String mostrecentPeriod,Integer from,Integer to)	throws DrillDownException {
		
		//ArrayList<GlTransactionTest> aQueryList = null; 
		String accDetailsQueryString="SELECT gl.glTransactionId,gl.coAccountNumber,ca.Description,gl.transactionDesc,gl.reference,gl.debit,gl.credit "
				+ "FROM glTransaction gl LEFT JOIN coAccount ca ON  ca.coAccountID = gl.coAccountID ORDER BY 1 DESC limit "+from+", "+to+";";
		
		List<GlTransactionTest> aQueryList = new ArrayList<GlTransactionTest>(); 
		ArrayList<GlTransactionTest> aQuery= new ArrayList<GlTransactionTest>();
		//System.out.println(accDetailsQueryString);
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList<GlTransactionTest>) aSession.createSQLQuery(accDetailsQueryString).list();		
			
			GlTransactionTest glRepAccObj=null;
			
			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj=new GlTransactionTest();
				Object[] aObj = (Object[])aIterator.next();				
			
				glRepAccObj.setGlTransactionId((Integer)aObj[0]);
				glRepAccObj.setCoAccountNumber((String)aObj[1]);
				glRepAccObj.setCoAccountDesc((String)aObj[2]);
				glRepAccObj.setTransactionDesc((String)aObj[3]);
				glRepAccObj.setReference((String)aObj[4]);
				glRepAccObj.setDebit(JobUtil.ConvertintoBigDecimal(""+aObj[5]));
				glRepAccObj.setCredit(JobUtil.ConvertintoBigDecimal(""+aObj[6]));
								
				aQueryList.add(glRepAccObj);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			accDetailsQueryString=null;aQuery=null;
		}
		return aQueryList;
	}
	
	
	/**
	 * Created by : Leo    Date: 15-09-2014
	 * Description: JournalBalance Tab Report Generate.
	 * Tables Used : glTransaction
	 * 
	 * */
	@Override
	public List<GlReportingAccounts> getJournalBalance(String accountNumber,String period,String accDesc,String yearID,Integer coAccountsID)	throws DrillDownException {
		
		String accDetailsQueryString = "SELECT journalid,journalDesc, SUM(credit),SUM(debit) "
				+ "FROM glTransaction where fyear='"+yearID+"' "
					//	+ "AND coAccountNumber = '"+accountNumber+"' AND EXTRACT(MONTH FROM entrydate) <="+period+" GROUP BY journalid";
						+ "AND coAccountId = '"+coAccountsID+"' AND period ="+period+" GROUP BY journalid";
		
		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>(); 
		ArrayList aQuery= new ArrayList();
				
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery =  (ArrayList) aSession.createSQLQuery(accDetailsQueryString).list();		
			
			GlReportingAccounts glRepAccObj=null;
		
			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj=new GlReportingAccounts();
				Object[] aObj = (Object[])aIterator.next();			
			
				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber(accountNumber);
				glRepAccObj.setCoAccountDesc(accDesc);
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod(Integer.parseInt(period));
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));
				
				glRepAccObj.setJournalID((String)aObj[0]);
				glRepAccObj.setJournalDesc((String)aObj[1]);
				glRepAccObj.setJournalBalance(new BigDecimal(0));	
				
				glRepAccObj.setSourceid(0);
				glRepAccObj.setDescription("");
				glRepAccObj.setEntrydate("");
				glRepAccObj.setGltransactionid(0);
				glRepAccObj.setCredit((BigDecimal)aObj[2]);
				glRepAccObj.setDebit((BigDecimal)aObj[3]);
				
				aQueryList.add(glRepAccObj);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery=null;accDetailsQueryString =null;
		}
			
		return aQueryList;
	}
	
	
	/**
	 * Created by : Leo    Date: 16-09-2014
	 * Description: JournalDetails Tab Report Generate.
	 * Tables Used : glTransaction,glLinkage,veBill,rxMaster
	 * 
	 * */
	@Override
	public List<GlReportingAccounts> getJournalDetails(String accountID,
			String accountDesc, String period, String yearID, String journalId,
			Integer coAccountsID) throws DrillDownException {

		String aJobSelectQry = "";

		if (journalId.equalsIgnoreCase("VB")) {
			aJobSelectQry = "SELECT Distinct gl.coLedgerSourceId,rx.Name ,gl.entrydate,glt.glTransactionId , glt.credit , glt.debit,glt.transactionDate FROM glTransaction glt, glLinkage gl, veBill ve,"
					+ "rxMaster rx  WHERE period = '"
					+ period
					+ "' AND fyear = '"
					+ yearID
					+ "' AND coAccountId = '"
					+ coAccountsID
					+ "' AND "
					+
					// "glt.glTransactionId = gl.glTransactionId AND gl.veBillID = ve.veBillID AND ve.rxMasterID = rx.rxMasterID AND journalid = '"+journalId+"' AND EXTRACT(MONTH FROM gl.entrydate) <="+period+" ORDER BY entrydate DESC";
					"glt.glTransactionId = gl.glTransactionId AND gl.veBillID = ve.veBillID AND ve.rxMasterID = rx.rxMasterID AND journalid = '"
					+ journalId
					+ "' AND period="
					+ period
					+ " ORDER BY entrydate DESC";
		} else // if(journalId.equalsIgnoreCase("IT"))
		{
			aJobSelectQry = "SELECT Distinct gl.coLedgerSourceId,glt.transactionDesc ,gl.entrydate,glt.glTransactionId , glt.credit , glt.debit,glt.transactionDate FROM glTransaction glt, glLinkage gl"
					+ "  WHERE period = '"
					+ period
					+ "' AND fyear = '"
					+ yearID
					+ "' AND coAccountId = '"
					+ coAccountsID
					+ "' AND "
					+
					// "glt.glTransactionId = gl.glTransactionId AND glt.journalid = '"+journalId+"' AND EXTRACT(MONTH FROM gl.entrydate) <="+period+" ORDER BY entrydate DESC";
					"glt.glTransactionId = gl.glTransactionId AND glt.journalid = '"
					+ journalId
					+ "' AND period="
					+ period
					+ " ORDER BY entrydate DESC";
		}

		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>();
		ArrayList aQuery = new ArrayList();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList) aSession.createSQLQuery(aJobSelectQry).list();

			GlReportingAccounts glRepAccObj = null;

			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj = new GlReportingAccounts();
				Object[] aObj = (Object[]) aIterator.next();

				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber(accountID);
				glRepAccObj.setCoAccountDesc(accountDesc);
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod(Integer.parseInt(period));
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));

				glRepAccObj.setJournalID(journalId);
				glRepAccObj.setJournalDesc("");
				glRepAccObj.setJournalBalance(new BigDecimal(0));

				glRepAccObj.setSourceid((Integer) aObj[0]);
				glRepAccObj.setDescription((String) aObj[1]);

				Timestamp stamp = (Timestamp) aObj[2];
				Date date = stamp;
				String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(date);
				glRepAccObj.setEntrydate((dateString));

				glRepAccObj.setGltransactionid((Integer) aObj[3]);
				// glRepAccObj.setAmount(Float.parseFloat(""+aObj[4]));

				glRepAccObj.setCredit((BigDecimal) aObj[4]);
				glRepAccObj.setDebit((BigDecimal) aObj[5]);

				Timestamp transStamps = (Timestamp) aObj[6];
				Date transdates = transStamps;
				String trandateStrings = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(transdates);
				glRepAccObj.setTransactionDate((trandateStrings));

				aQueryList.add(glRepAccObj);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry = null;
			aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public List<GlReportingAccounts> getJournalDetails(String accountID,
			String accountDesc, String period, String yearID, String journalId,
			Integer aFrom, Integer aTo, String sIdx, String sOrd,
			Integer coAccountsID) throws DrillDownException {

		String aJobSelectQry = "";
		if (sIdx == null) {
			sIdx = "transactionDate,entrydate";
		} else if (sIdx.equals("")) {
			sIdx = "transactionDate,entrydate";
		}

		if (journalId.equalsIgnoreCase("VB")) {
			aJobSelectQry = "SELECT Distinct gl.coLedgerSourceId,rx.Name ,gl.entrydate,glt.glTransactionId , glt.credit , glt.debit,glt.transactionDate FROM glTransaction glt, glLinkage gl, veBill ve,"
					+ "rxMaster rx  WHERE period = '"
					+ period
					+ "' AND fyear = '"
					+ yearID
					+ "' AND coAccountId = '"
					+ coAccountsID
					+ "' AND "
					+
					// "glt.glTransactionId = gl.glTransactionId AND gl.veBillID = ve.veBillID AND ve.rxMasterID = rx.rxMasterID AND journalid = '"+journalId+"' AND EXTRACT(MONTH FROM gl.entrydate) <="+period+" ORDER BY "+sIdx+" "+sOrd.toUpperCase()+"  LIMIT "+aFrom+
					// " , "+aTo;
					"glt.glTransactionId = gl.glTransactionId AND gl.veBillID = ve.veBillID AND ve.rxMasterID = rx.rxMasterID AND journalid = '"
					+ journalId
					+ "' AND period="
					+ period
					+ " GROUP BY glt.glTransactionId ORDER BY "
					+ sIdx
					+ " "
					+ sOrd.toUpperCase()
					+ "  LIMIT "
					+ aFrom
					+ " , " + aTo;
		} else // if(journalId.equalsIgnoreCase("IT"))
		{
			aJobSelectQry = "SELECT Distinct gl.coLedgerSourceId,glt.transactionDesc ,gl.entrydate,glt.glTransactionId , glt.credit , glt.debit,glt.transactionDate FROM glTransaction glt, glLinkage gl"
					+ "  WHERE period = '"
					+ period
					+ "' AND fyear = '"
					+ yearID
					+ "' AND coAccountId = '"
					+ coAccountsID
					+ "' AND "
					+
					// "glt.glTransactionId = gl.glTransactionId AND glt.journalid = '"+journalId+"' AND EXTRACT(MONTH FROM gl.entrydate) <="+period+" ORDER BY "+sIdx+" "+sOrd.toUpperCase()+"  LIMIT "+aFrom+
					// " , "+aTo;
					"glt.glTransactionId = gl.glTransactionId AND glt.journalid = '"
					+ journalId
					+ "' AND period ="
					+ period
					+ " GROUP BY glt.glTransactionId ORDER BY "
					+ sIdx
					+ " "
					+ sOrd.toUpperCase()
					+ "  LIMIT "
					+ aFrom
					+ " , " + aTo;
		}

		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>();
		ArrayList aQuery = new ArrayList();
		System.out.println("Journal Detail Query :: "+aJobSelectQry);
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList) aSession.createSQLQuery(aJobSelectQry).list();

			GlReportingAccounts glRepAccObj = null;

			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj = new GlReportingAccounts();
				Object[] aObj = (Object[]) aIterator.next();

				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber(accountID);
				glRepAccObj.setCoAccountDesc(accountDesc);
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod(Integer.parseInt(period));
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));

				glRepAccObj.setJournalID(journalId);
				glRepAccObj.setJournalDesc("");
				glRepAccObj.setJournalBalance(new BigDecimal(0));

				glRepAccObj.setSourceid((Integer) aObj[0]);
				glRepAccObj.setDescription((String) aObj[1]);

				Timestamp stamp = (Timestamp) aObj[2];
				Date date = stamp;
				String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(date);
				glRepAccObj.setEntrydate((dateString));

				glRepAccObj.setGltransactionid((Integer) aObj[3]);
				// glRepAccObj.setAmount(Float.parseFloat(""+aObj[4]));

				glRepAccObj.setCredit((BigDecimal) aObj[4]);
				glRepAccObj.setDebit((BigDecimal) aObj[5]);

				Timestamp transStamps = (Timestamp) aObj[6];
				Date transdates = transStamps;
				String trandateStrings = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(transdates);
				glRepAccObj.setTransactionDate((trandateStrings));

				aQueryList.add(glRepAccObj);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry = null;
			aQuery = null;
		}

		return aQueryList;
	}

	@Override
	public GlReportingAccounts getJournalDetailsSum(String accountID,
			String period, String yearID, String journalId, Integer coAccountsID)
			throws DrillDownException {

		String accDetailsQueryString = "SELECT  SUM(credit),SUM(debit) FROM glTransaction where  period = '"
				+ period
				+ "' AND fyear='"
				+ yearID
				+ "'"
				+ " AND coAccountId = '"
				+ coAccountsID
				+ "' AND  journalid = '" + journalId + "'";

		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>();
		ArrayList aQuery = new ArrayList();
		GlReportingAccounts glRepAccObj = null;

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList) aSession.createSQLQuery(accDetailsQueryString)
					.list();

			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj = new GlReportingAccounts();
				Object[] aObj = (Object[]) aIterator.next();

				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber("");
				glRepAccObj.setCoAccountDesc("");
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod(Integer.parseInt(period));
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));

				glRepAccObj.setJournalID("");
				glRepAccObj.setJournalDesc("");
				glRepAccObj.setJournalBalance(new BigDecimal(0));

				glRepAccObj.setSourceid(0);
				glRepAccObj.setDescription("");
				glRepAccObj.setEntrydate("");
				glRepAccObj.setGltransactionid(0);
				glRepAccObj.setCredit((BigDecimal) aObj[0]);
				glRepAccObj.setDebit((BigDecimal) aObj[1]);

				// aQueryList.add(glRepAccObj);

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			accDetailsQueryString = null;
			aQuery = null;
		}
		return glRepAccObj;
	}
			
	@Override
	public BigInteger getJournalDetailsCount(String accountNumber,
			String periodFromID, String periodTo, String yearID,
			String mostrecentPeriod, String pStrdate, String journalId)
			throws DrillDownException {
		String aJobSelectQry = "SELECT COUNT(*) FROM glReporting";
		if (accountNumber != null && !accountNumber.equals("")) {
			if (aJobSelectQry.endsWith("glReporting")) {
				aJobSelectQry = aJobSelectQry + " WHERE coAccountID="
						+ accountNumber;
			} else {
				aJobSelectQry = aJobSelectQry + " AND coAccountID="
						+ accountNumber;
			}
		}

		if (periodFromID != null && !periodFromID.equals("")
				&& periodTo != null && !periodTo.equals("")) {
			if (aJobSelectQry.endsWith("glReporting")) {
				aJobSelectQry = aJobSelectQry + " WHERE period BETWEEN "
						+ periodFromID + " AND " + periodTo;
			} else {
				aJobSelectQry = aJobSelectQry + " AND period BETWEEN "
						+ periodFromID + " AND " + periodTo;
			}
		}
		if (yearID != null && !yearID.equals("")) {
			if (aJobSelectQry.endsWith("glReporting")) {
				aJobSelectQry = aJobSelectQry + " WHERE fiscalYear =" + yearID;
			} else {
				aJobSelectQry = aJobSelectQry + " AND fiscalYear =" + yearID;
			}
		}
		if (mostrecentPeriod != null && !mostrecentPeriod.equals("")) {
			if (aJobSelectQry.endsWith("glReporting")) {
				aJobSelectQry = aJobSelectQry + " WHERE period ="
						+ mostrecentPeriod;
			} else {
				aJobSelectQry = aJobSelectQry + " AND period ="
						+ mostrecentPeriod;
			}
		}

		if (pStrdate != null && !pStrdate.equals("")) {
			if (aJobSelectQry.endsWith("glReporting")) {
				aJobSelectQry = aJobSelectQry + " WHERE pStartDate >='"
						+ pStrdate.trim() + "'";
			} else {
				aJobSelectQry = aJobSelectQry + " AND pStartDate >='"
						+ pStrdate.trim() + "'";
			}
		}
		if (journalId.trim() != null && !journalId.trim().equals("")) {
			if (aJobSelectQry.endsWith("glReporting")) {
				aJobSelectQry = aJobSelectQry + " WHERE JournalID = '"
						+ journalId.trim() + "'";
			} else {
				aJobSelectQry = aJobSelectQry + " AND JournalID ='"
						+ journalId.trim() + "'";
			}
		}

		Query aQuery = null;
		// aJobSelectQry =aJobSelectQry+" GROUP BY coLedgerSourceID";
		
		
		Session aSession = null;
		BigInteger count = new BigInteger("0");
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aJobSelectQry);
			count = (BigInteger) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry = null;
			aQuery = null;
		}
		return count;
	}
			
	/**
	 * Created by : Leo Date: 16-09-2014
	 * Description: TransactionDetails Tab Report Generate.
	 * Tables Used : glTransaction,glLinkage,coAccount
	 * 
	 * */

	@Override
	public List<GlReportingAccounts> getTransactionDetails(
			String transactionReferenceID, String description, String Sourceid)
			throws DrillDownException {

		String aJobSelectQry = "SELECT glt.coAccountNumber,ca.Description, glt.journalId, glt.journalDesc,glt.credit,glt.debit,glt.entrydate,glt.transactionDesc,glt.transactionDate FROM glTransaction glt, glLinkage gl, coAccount ca "
				+ "WHERE glt.glTransactionId = gl.glTransactionId AND ca.coAccountID = glt.coAccountID AND "
				+ "gl.veBillID = (SELECT Distinct veBillID FROM glLinkage WHERE glTransactionId = '"
				+ transactionReferenceID
				+ "') AND gl.coLedgerSourceId="
				+ Sourceid;

		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>();
		ArrayList aQuery = new ArrayList();

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList) aSession.createSQLQuery(aJobSelectQry).list();

			GlReportingAccounts glRepAccObj = null;

			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj = new GlReportingAccounts();
				Object[] aObj = (Object[]) aIterator.next();

				glRepAccObj.setCoAccountNumber((String) aObj[0]);
				glRepAccObj.setCoAccountDesc((String) aObj[1]);
				glRepAccObj.setYear("");
				glRepAccObj.setPeriod(0);
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));

				glRepAccObj.setJournalID((String) aObj[2]);
				glRepAccObj.setJournalDesc((String) aObj[3]);
				glRepAccObj.setJournalBalance(new BigDecimal(0));

				glRepAccObj.setSourceid(Integer.parseInt(Sourceid));
				glRepAccObj.setDescription((String) aObj[7]);

				Timestamp stamp = (Timestamp) aObj[6];
				Date date = stamp;
				String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(date);
				glRepAccObj.setEntrydate((dateString));

				Timestamp transStamps = (Timestamp) aObj[8];
				Date transdates = transStamps;
				String trandateStrings = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(transdates);
				glRepAccObj.setTransactionDate((trandateStrings));

				glRepAccObj.setGltransactionid(Integer
						.parseInt(transactionReferenceID));
				// glRepAccObj.setAmount(Float.parseFloat(""+aObj[4]));

				glRepAccObj.setCredit((BigDecimal) aObj[4]);
				glRepAccObj.setDebit((BigDecimal) aObj[5]);

				aQueryList.add(glRepAccObj);

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry = null;
			aQuery = null;
		}

		return aQueryList;

	}

	@Override
	public List<Cofiscalyear> getYearList() throws DrillDownException {
		Session aSession = itsSessionFactory.openSession();
		String Qry = "FROM Cofiscalyear";
		List<Cofiscalyear> yearList = null;
		try {
			yearList = (List<Cofiscalyear>) aSession.createQuery(Qry).list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new DrillDownException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			Qry = null;
		}
		return yearList;

	}
			
	public Integer getCurrentFiscalPeriod(String theCreated) {
		itsLogger.info("getCurrentFiscalDetail");
		String aSalesselectQry = theCreated;
		Session aSession = null;
		Integer fiscalData = null;
		Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			List<?> aList = aQuery.list();
			fiscalData = (Integer) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
			aSalesselectQry = null;
		}
		return fiscalData;
	}

	public Integer getCurrentFiscalYear(String theCreated) {
		itsLogger.info("getCurrentFiscalDetail");
		String aSalesselectQry = theCreated;
		Session aSession = null;
		Integer fiscalData = null;
		Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			List<?> aList = aQuery.list();
			fiscalData = (Integer) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
			aSalesselectQry = null;
		}
		return fiscalData;
	}

	@Override
	public List<GlReportingAccounts> getJournalBalance(String accountID,
			String period, String accDescription, String yearID,
			Integer fromID, Integer toId, String sIdx, String sOrd,
			Integer coAccountsID) throws DrillDownException {

		String accDetailsQueryString = "SELECT journalid,journalDesc, SUM(credit),SUM(debit) "
				+ "FROM glTransaction where "
				+ " fyear='"
				+ yearID
				+ "' "
				// +
				// "AND coAccountNumber = '"+accountID+"' AND EXTRACT(MONTH FROM entrydate) <="+period+" GROUP BY journalid LIMIT "+fromID
				// +" , "+toId;
				+ "AND coAccountId = '"
				+ coAccountsID
				+ "' AND period="
				+ period + " GROUP BY journalid LIMIT " + fromID + " , " + toId;

		List<GlReportingAccounts> aQueryList = new ArrayList<GlReportingAccounts>();
		ArrayList aQuery = new ArrayList();

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList) aSession.createSQLQuery(accDetailsQueryString)
					.list();

			GlReportingAccounts glRepAccObj = null;

			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				glRepAccObj = new GlReportingAccounts();
				Object[] aObj = (Object[]) aIterator.next();

				glRepAccObj.setCoAccountID(coAccountsID);
				glRepAccObj.setCoAccountNumber(accountID);
				glRepAccObj.setCoAccountDesc(accDescription);
				glRepAccObj.setYear(yearID);
				glRepAccObj.setPeriod(Integer.parseInt(period));
				glRepAccObj.setPeriodbalance(new BigDecimal(0));
				glRepAccObj.setCumulativebalance(new BigDecimal(0));

				glRepAccObj.setJournalID((String) aObj[0]);
				glRepAccObj.setJournalDesc((String) aObj[1]);
				//glRepAccObj.setJournalBalance(new BigDecimal(0));

				glRepAccObj.setSourceid(0);
				glRepAccObj.setDescription("");
				glRepAccObj.setEntrydate("");
				glRepAccObj.setGltransactionid(0);
				glRepAccObj.setCredit((BigDecimal) aObj[2]);
				glRepAccObj.setDebit((BigDecimal) aObj[3]);

				// EditedBy: Naveed  Date: 19 Aug 2015
				// Desc : Adding Balance column in GL
				BigDecimal debit =(BigDecimal) aObj[3];
				BigDecimal credit = (BigDecimal) aObj[2];
				
				DecimalFormat df = new DecimalFormat("#0.00");
				String balance =  df.format(debit.subtract(credit));
				int cond = debit.compareTo(credit);
							 
				String balanceDesc="";
				if(cond <0){
					BigDecimal pbalance =  new BigDecimal(balance).multiply(new BigDecimal(-1));
					balanceDesc = pbalance+" CR";
				}else
					balanceDesc = balance+" DB";
				glRepAccObj.setBalanceDesc(balanceDesc);
				
				aQueryList.add(glRepAccObj);

			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			accDetailsQueryString = null;
			aQuery = null;
		}

		return aQueryList;

	}

	@Override
	public List<TrialBalance> getAllTrialBalances(Integer periodID,
			Integer yearID, Integer fromcoAccountID, Integer tocoAccountID)
			throws DrillDownException {

		String accDetailsQueryString = "";
		String appendString = "";
		if (fromcoAccountID != -1 && tocoAccountID != -1) {
			appendString = "AND cA.coAccountID >=" + fromcoAccountID
					+ " AND cA.coAccountID <=" + tocoAccountID;
		} else if (fromcoAccountID != -1 && tocoAccountID == -1) {
			appendString = "AND cA.coAccountID =" + fromcoAccountID;
		} else if (fromcoAccountID == -1 && tocoAccountID != -1) {
			appendString = "AND cA.coAccountID =" + tocoAccountID;
		} else {
			appendString = "";
		}

		
		accDetailsQueryString = "SELECT  cA.coAccountID,cA.Number,cA.Description, IF(pdebits-pcredits>0,pdebits-pcredits,0) AS pdebits,IF(pcredits-pdebits>0,pcredits-pdebits,0) AS pcredits,IF(ydebits-ycredits>0,ydebits-ycredits,0) AS ydebits,IF(ycredits-ydebits>0,ycredits-ydebits,0) AS ycredits "
				+ " FROM coAccount cA"
				+ " LEFT JOIN"
				+ " (SELECT  gt.coAccountID, TRUNCATE(SUM(TRUNCATE(gt.debit,2)),2) pdebits,TRUNCATE(SUM(TRUNCATE(gt.credit,2)),2)pcredits FROM glTransaction gt WHERE gt.coFiscalPeriodId = "
				+ periodID
				+ " and  gt.coFiscalYearId = "
				+ yearID
				+ " GROUP BY gt.coAccountID) gl ON cA.coAccountID = gl.coAccountID "
				+ appendString
				+ " LEFT JOIN"
				+ " (SELECT  gtt.coAccountID, TRUNCATE(SUM(TRUNCATE(gtt.debit,2)),2) ydebits,TRUNCATE(SUM(TRUNCATE(gtt.credit,2)),2) ycredits FROM glTransaction gtt WHERE "
				//+ "gtt.coFiscalPeriodId <= "+ periodID 
				//+" (gtt.period <= (SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+") AND gtt.coFiscalYearId ="+yearID+") "
				+ " (gtt.fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearID+")  OR (gtt.period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodID+") AND gtt.coFiscalYearId = "+yearID+")) "
				/*+" and gtt.coFiscalYearId = "         // Commented by leo ID# 497
				+ yearID*/
				+ " GROUP BY gtt.coAccountID) gll ON cA.coAccountID = gll.coAccountID "
				+ appendString
				+ " WHERE cA.InActive <> 1 AND pdebits IS NOT NULL OR pcredits IS NOT NULL OR ydebits IS NOT NULL OR ycredits IS NOT NULL HAVING ycredits>0 OR ydebits>0 order by cA.Number";

		List<TrialBalance> aQueryList = new ArrayList<TrialBalance>();
		ArrayList<?> aQuery = new ArrayList<Object>();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = (ArrayList) aSession.createSQLQuery(accDetailsQueryString)
					.list();

			TrialBalance trialBalObj = null;

			Iterator<?> aIterator = aQuery.iterator();
			while (aIterator.hasNext()) {
				trialBalObj = new TrialBalance();
				Object[] aObj = (Object[]) aIterator.next();

				trialBalObj.setCoAccountID((Integer) aObj[0]);
				trialBalObj.setCoAccountNumber((String) aObj[1]);
				trialBalObj.setCoAccountDesc((String) aObj[2]);

				// new BigDecimal(aObj[4].toString()).setScale(2,
				// RoundingMode.CEILING)
				if (aObj[3] != null)
					trialBalObj.setPdebits(((BigDecimal) aObj[3]));

				if (aObj[4] != null)
					trialBalObj.setPcredits(((BigDecimal) aObj[4]));

				if (aObj[5] != null)
					trialBalObj.setYdebits(((BigDecimal) aObj[5]));

				if (aObj[6] != null)
					trialBalObj.setYcredits(((BigDecimal) aObj[6]));
				aQueryList.add(trialBalObj);

			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			DrillDownException aDrillDownException = new DrillDownException(
					e.getMessage(), e);
			throw aDrillDownException;
		} finally {
			aSession.flush();
			aSession.close();
			accDetailsQueryString = null;
			aQuery = null;
		}

		return aQueryList;
	}

	@Override
	public BigInteger getGlTransactionCount() throws DrillDownException {

		String aSalesselectQry = "Select Count(glTransactionID) from glTransaction";
		Session aSession = null;
		Query aQuery = null;
		BigInteger TotalCount = new BigInteger("0");
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			TotalCount = (BigInteger) aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
			aQuery = null;
		}
		return TotalCount;
	}

	@Override
	public Cofiscalperiod getAllPeriodsbasedonDate(Date dateto)
			throws DrillDownException {

		Session aSession = null;
		Query query = null;
		Cofiscalperiod aCofiscalperiod = null;
		try {

			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalperiod c where c.startDate <= :Date and c.endDate >= :Date").setMaxResults(1);
			query.setParameter("Date", dateto);
			aCofiscalperiod = (Cofiscalperiod) query.uniqueResult();
			
			itsLogger.info("Get Period::["+aCofiscalperiod.getPeriod()+"]");

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCofiscalperiod;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getProfitandlossdetails(String query) {
		
		Session aSession = null;
		List<?> aList = null;
		List aList1 = new ArrayList();
		TsUserSetting aTsUserSetting =null;
		BigDecimal pincometotal = BigDecimal.ZERO;
		BigDecimal pexpensetotal = BigDecimal.ZERO;
		BigDecimal yincometotal = BigDecimal.ZERO;
		BigDecimal yexpensetotal = BigDecimal.ZERO;
		try {

			aSession = itsSessionFactory.openSession();
			aList = aSession.createSQLQuery(query).list();
			
			for(int i=0;i<aList.size();i++)
			{
				Object[] row = (Object[]) aList.get(i);
		         //   System.out.println(row[4]);
				if(row[4].equals(new Byte((byte) 1)) || row[4].equals(new Byte((byte) 2)))
				{
					
					pincometotal = pincometotal.add((BigDecimal)row[5]== null ? new BigDecimal(0.00) : (BigDecimal)row[5]);
					yincometotal = yincometotal.add((BigDecimal)row[6]== null ? new BigDecimal(0.00) : (BigDecimal)row[6]);
				}
				
			}
			
			aList1.add((BigDecimal)pincometotal);
			aList1.add((BigDecimal)yincometotal);
			aTsUserSetting = (TsUserSetting)aSession.createQuery("From TsUserSetting").uniqueResult();
			aList1.add(aTsUserSetting.getHeaderText());
			aList1.add(aTsUserSetting.getCompanyLogo());
			

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aList1;
		
	}

	@Override
	public BigDecimal getNetProfitAmount(Integer cofiscalPeriodID) throws DrillDownException {

		String aSalesselectQry = "SELECT SUM(diff) AS netprofiloss FROM ("
 +"SELECT accountType,SUM(credit)-SUM(debit) AS diff FROM coAccount JOIN glTransaction USING (coAccountID) LEFT JOIN coFiscalPeriod USING(coFiscalPeriodId)  "
 +"WHERE accountType IN('Income','Expense') "
 +"and  (glTransaction.fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM `coFiscalPeriod` JOIN coFiscalYear USING(coFiscalYearID) WHERE coFiscalPeriodId="+cofiscalPeriodID+") "
 +" or (glTransaction.period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+cofiscalPeriodID+")"
 +" and glTransaction.coFiscalYearId = (SELECT coFiscalYearID FROM coFiscalPeriod WHERE coFiscalPeriodId="+cofiscalPeriodID+")))"
  //+" AND glTransaction.period <= (SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+cofiscalPeriodID+")"
 //+ "AND glTransaction.period<>13 "
 + " AND InActive <> 1 "
 //+" AND glTransaction.fyear <=(SELECT `fiscalYear` FROM `coFiscalPeriod` JOIN coFiscalYear USING(coFiscalYearID) WHERE coFiscalPeriodId="+cofiscalPeriodID+")"
 +"GROUP BY accountType) AS test";
		Session aSession = null;
		Query aQuery = null;
		BigDecimal TotalCount=BigDecimal.ZERO ;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			TotalCount = (BigDecimal) aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
			aQuery = null;
		}
		return TotalCount;
	}
}