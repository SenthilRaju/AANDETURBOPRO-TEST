package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoAccountBean;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;

@Transactional
@Service("chartOfAccountsService")
public class ChartOfAccountsServiceImpl implements ChartOfAccountsService {

	Logger itsLogger = Logger.getLogger(ChartOfAccountsServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public List<Coaccount> getListOfAccounts(int theFrom, int theRows) throws CompanyException {
		String aSelectAccountsQry = "SELECT * FROM coAccount"; /*** LIMIT " + theFrom + ", " + theRows;*/
		ArrayList<Coaccount> aListOfAccounts = null;
		Session aSession = null;Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			aListOfAccounts = (ArrayList<Coaccount>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry=null;aQuery = null;
		}
		return aListOfAccounts;
	}

	@Override
	public BigInteger getListOfAccountsCount() throws CompanyException {
		BigInteger count;
		Session aSession = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  Coaccount");
			count = BigInteger.valueOf(aQuery.list().size());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return count;
	}

	@Override
	public Coaccount updateChartAccount(Coaccount theCoaccount) throws CompanyException {
		Session aSession = itsSessionFactory.openSession();
		Coaccount aCoaccount = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aCoaccount = (Coaccount) aSession.get(Coaccount.class, theCoaccount.getCoAccountId());
			aCoaccount.setNumber(theCoaccount.getNumber());
			aCoaccount.setDescription(theCoaccount.getDescription());
			aCoaccount.setInActive(theCoaccount.isInActive());
			aCoaccount.setTax1099(theCoaccount.isTax1099());
			aCoaccount.setFontBold(theCoaccount.isFontBold());
			aCoaccount.setFontItalic(theCoaccount.isFontItalic());
			aCoaccount.setFontLarge(theCoaccount.isFontLarge());
			aCoaccount.setFontUnderline(theCoaccount.isFontUnderline());
			aCoaccount.setIncludeWhenZero(theCoaccount.isIncludeWhenZero());
			aCoaccount.setDollarSign(theCoaccount.isDollarSign());
			aCoaccount.setVerticalSpacing(theCoaccount.getVerticalSpacing());
			aCoaccount.setHorizontalSpacing(theCoaccount.getHorizontalSpacing());
			aCoaccount.setBalanceSheetColumn(theCoaccount.getBalanceSheetColumn());
			aCoaccount.setLineAboveAmount(theCoaccount.getLineAboveAmount());
			aCoaccount.setLineBelowAmount(theCoaccount.getLineBelowAmount());
			aCoaccount.setTotalingLevel(theCoaccount.getTotalingLevel());
			aCoaccount.setCoAccountId(theCoaccount.getCoAccountId());
			aCoaccount.setDebitBalance(theCoaccount.isDebitBalance());
			aCoaccount.setTypeAccount(theCoaccount.getTypeAccount());
			aCoaccount.setFinancialStatus(theCoaccount.getFinancialStatus());
			aSession.update(aCoaccount);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCoaccount;
	}

	@Override
	public boolean addNewChartAccount(Coaccount theCoaccount) throws CompanyException {
		Session aChartAccountSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aChartAccountSession.beginTransaction();
			aTransaction.begin();
			aChartAccountSession.save(theCoaccount);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CompanyException aCompanyException = new CompanyException(excep.getMessage(), excep);
			throw aCompanyException;
		} finally {
			aChartAccountSession.flush();
			aChartAccountSession.close();
		}
		return true;
	}

	@Override
	public List<CoAccountBean> getAccountsList(Integer theAccountID) throws CompanyException {
		Session aSession = null;
		List<CoAccountBean> aQueryList = new ArrayList<CoAccountBean>();
		Query aQuery =null;
		String aCustomerQry = "SELECT * FROM coAccount WHERE coAccountID = "+theAccountID;
		CoAccountBean aCoAcc = null;
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCoAcc = new CoAccountBean();
				Object[] aObj = (Object[])aIterator.next();
				aCoAcc.setCoAccountId((Integer) aObj[0]);
				if((Byte) aObj[1] == 1){
					aCoAcc.setInActive(true);
				}else{
					aCoAcc.setInActive(false);
				}
				aCoAcc.setNumber((String) aObj[2]);
				aCoAcc.setDescription((String) aObj[3]);
				aCoAcc.setBalanceSheetColumn((Short) aObj[4]);
				if((Byte) aObj[5] == 1){
					aCoAcc.setIncludeWhenZero(true);
				}else{
					aCoAcc.setIncludeWhenZero(false);
				}
				if((Byte) aObj[6] == 1){
					aCoAcc.setDebitBalance(true);
				}else{
					aCoAcc.setDebitBalance(false);
				}
				if((Byte) aObj[7] == 1){
					aCoAcc.setContraAccount(true);
				}else{
					aCoAcc.setContraAccount(false);
				}
				aCoAcc.setLineAboveAmount((Short) aObj[8]);
				aCoAcc.setLineBelowAmount((Short) aObj[9]);
				aCoAcc.setTotalingLevel((Short) aObj[10]);
				aCoAcc.setVerticalSpacing((Short) aObj[11]);
				aCoAcc.setHorizontalSpacing((Short) aObj[12]);
				if((Byte) aObj[13] == 1){
					aCoAcc.setFontLarge(true);
				}else{
					aCoAcc.setFontLarge(false);
				}
				if((Byte) aObj[14] == 1){
					aCoAcc.setFontBold(true);
				}else{
					aCoAcc.setFontBold(false);
				}
				if((Byte) aObj[15] == 1){
					aCoAcc.setFontItalic(true);
				}else{
					aCoAcc.setFontItalic(false);
				}
				if((Byte) aObj[16] == 1){
					aCoAcc.setFontUnderline(true);
				}else{
					aCoAcc.setFontUnderline(false);
				}
				if((Byte) aObj[17] == 1){
					aCoAcc.setTax1099(true);
				}else{
					aCoAcc.setTax1099(false);
				}
				aCoAcc.setSubAccount((String) aObj[18]);
				if((Byte) aObj[19] == 1){
					aCoAcc.setIsSubAccount(true);
				}else{
					aCoAcc.setIsSubAccount(false);
				}
				if((Byte) aObj[20] == 1){
					aCoAcc.setIsMasterAccount(true);
				}else{
					aCoAcc.setIsMasterAccount(false);
				}
				if((Byte) aObj[21] == 1){
					aCoAcc.setDollarSign(true);
				}else{
					aCoAcc.setDollarSign(false);
				}
				aCoAcc.setTypeAccount((String)aObj[22]);
				aQueryList.add(aCoAcc);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;aQuery =null;
		}
		return aQueryList;	
	}
	
	@Override
	public List<GlTransaction> getLedgerDetails(Integer theAccountID,Integer periodID,Integer yearID) throws CompanyException {
		Session aSession = null;Query aQuery = null;
		List<GlTransaction> aQueryList = new ArrayList<GlTransaction>();
	/*	String aCustomerQry = "SELECT glTransactionId,SUM(credit),SUM(debit),coAccount.openingBalance,(coAccount.openingBalance+SUM(debit))-SUM(credit) as closingBal "
				+ "FROM glTransaction  LEFT JOIN moAccount ON coAccountID = coAccountIDAsset LEFT JOIN coAccount ON(coAccount.coAccountID=glTransaction.coAccountID) WHERE coAccount.coAccountID = "+theAccountID+" and fyear = "+fiscalYear;
		*/
		
		String aCustomerQry ="SELECT SUM(credit),SUM(debit),SUM(debit)-SUM(credit) AS closingBal FROM glTransaction WHERE coAccountID = "+theAccountID+" AND coFiscalYearID <="+yearID;
		
		GlTransaction aCoAcc = null;
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			aCoAcc = new GlTransaction();
			if( aQuery.list().size()>0){
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				
			//	aCoAcc.setGlTransactionId((Integer) aObj[0]);
				aCoAcc.setCredit((BigDecimal) aObj[0]);
				aCoAcc.setDebit((BigDecimal) aObj[1]);
			//	aCoAcc.setBankOpeningBalance((BigDecimal) aObj[3]);
				aCoAcc.setBankClosingBalance((BigDecimal) aObj[2]);
			}
			}
			aQueryList.add(aCoAcc);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;aQuery = null;
		}
		return aQueryList;	
	}
	
	@Override
	public List<GlTransaction> getLedgerDetailscurentPeriod(Integer theAccountID,Integer periodID,Integer yearID) throws CompanyException {
		Session aSession = null;Query aQuery = null;
		List<GlTransaction> aQueryList = new ArrayList<GlTransaction>();
		
		String aCustomerQry ="SELECT SUM(credit),SUM(debit),SUM(debit)-SUM(credit) AS closingBal FROM glTransaction WHERE coAccountID = "+theAccountID+" AND coFiscalYearID ="+yearID+" AND coFiscalPeriodID = "+periodID;
		
		GlTransaction aCoAcc = null;
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			aCoAcc = new GlTransaction();
			if( aQuery.list().size()>0){
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				
				aCoAcc.setCredit((BigDecimal) aObj[0]);
				aCoAcc.setDebit((BigDecimal) aObj[1]);
				aCoAcc.setBankClosingBalance((BigDecimal) aObj[2]);
			}
			}
			aQueryList.add(aCoAcc);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;aQuery = null;
		}
		return aQueryList;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GlTransaction> getLedgerDetailsPeriod(Integer theAccountID,Integer periodId,Integer yearId) throws CompanyException {
		Session aSession = null;
		List<GlTransaction> aQueryList = new ArrayList<GlTransaction>();
		List<Coaccount> coaccList = null;
		List<?> aList = null;
		String aCustomerQry ="";
		Query aQuery =null;
		GlTransaction aCoAcc = null;
		
		
		try{
			aSession = itsSessionFactory.openSession();
			coaccList = aSession.createSQLQuery("Select * from coAccount where accountType in ('Income','Expense') and coAccountID ="+theAccountID).list();
			
			if(coaccList.size()>0)
			{
			aCustomerQry="SELECT 0 AS glTransactionID,SUM(credit),SUM(debit),0 AS prevbalance, (SUM(debit)-SUM(credit)) AS closingBalance FROM  glTransaction WHERE coAccountID ="+theAccountID+" AND "
					+ "coFiscalPeriodId ="+periodId+" AND coFiscalYearId="+yearId;
			}
			else
			{
			/* aCustomerQry = "SELECT glTransactionID,SUM(credit),SUM(debit),ifnull(prevBalance,0),ifnull(prevBalance,0)+SUM(debit)-SUM(credit) AS closingBal "
					+ "FROM glTransaction "
					+ "LEFT JOIN (SELECT cb.coAccountID,(cb.PeriodOpening+cb.PeriodDebits-cb.PeriodCredits) AS prevBalance FROM coBalance cb, coAccount ca "
					+ "WHERE cb.coFiscalPeriodID = (SELECT coFiscalPeriodID FROM coFiscalPeriod WHERE coFiscalPeriodID <= "+periodId+"  "
					+ "ORDER BY coFiscalPeriodID DESC LIMIT 1 OFFSET 1) AND ca.coAccountID = cb.coAccountID AND ca.accountType NOT IN ('Income','Expense')) cb ON cb.coAccountID = glTransaction.coAccountID "
					+ "WHERE glTransaction.coAccountID ="+theAccountID+" AND glTransaction.coFiscalPeriodId ="+periodId+" AND glTransaction.coFiscalYearId="+yearId;*/
				
			aCustomerQry="SELECT 0 AS glTransactionID,SUM(credit),SUM(debit),(SELECT SUM(debit)-SUM(credit) FROM glTransaction WHERE coAccountID ="+theAccountID+" "
					//+ "AND coFiscalPeriodId <= (SELECT coFiscalPeriodID FROM coFiscalPeriod WHERE coFiscalPeriodID <= "+periodId+" ORDER BY coFiscalPeriodID DESC LIMIT 1 OFFSET 1)"
					+ " AND (fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearId+") OR (period<(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodId+") AND coFiscalYearId = "+yearId+"))"
					+ ") AS prevbalance,"
					+ "(SELECT SUM(debit)-SUM(credit) FROM glTransaction "
					+ "WHERE coAccountID ="+theAccountID+" "
					//+ "AND coFiscalPeriodId <= "+periodId+""
					+ " AND (fyear<(SELECT SUBSTRING_INDEX(`fiscalYear`,'-',1) FROM coFiscalYear WHERE coFiscalYearId="+yearId+") OR (period<=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodId+") AND coFiscalYearId = "+yearId+"))"
					+ ")AS closingBalance "
					+ "FROM  glTransaction WHERE coAccountID ="+theAccountID+" AND coFiscalPeriodId = "+periodId+" AND glTransaction.coFiscalYearId="+yearId;
			}
		
		//openingBalance
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = null;
			aList = aQuery.list();
			aIterator = aList.iterator();
			aCoAcc = new GlTransaction();
			
			if( aList.size()>0){
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				
				
				if(aObj[0]!=null)
				{
					if(aObj[0] instanceof BigInteger)
					aCoAcc.setGlTransactionId((Integer)0);
					else
					aCoAcc.setGlTransactionId((Integer)aObj[0]);
					
					aCoAcc.setCredit((BigDecimal) aObj[1]);
					aCoAcc.setDebit((BigDecimal) aObj[2]);
					if(aObj[3] instanceof BigInteger)
						aCoAcc.setBankOpeningBalance(BigDecimal.ZERO);
						else
						aCoAcc.setBankOpeningBalance((BigDecimal)aObj[3]);
					aCoAcc.setBankClosingBalance((BigDecimal) aObj[4]);  
				}
				else
				{
					aCustomerQry = "SELECT 0 AS glTransaction , 0 AS credit, 0 AS debit,cb.PeriodOpening,(cb.PeriodOpening+cb.PeriodDebits-cb.PeriodCredits) AS prevBalance  "
							+ "FROM coBalance cb,coAccount ca WHERE "
							+"(period=(SELECT period FROM coFiscalPeriod WHERE coFiscalPeriodId="+periodId+")-1 AND coFiscalYearId = "+yearId+") "
							//+ "cb.coFiscalPeriodID = (SELECT coFiscalPeriodID FROM coFiscalPeriod WHERE coFiscalPeriodID <= "+periodId+"  ORDER BY coFiscalPeriodID DESC LIMIT 1 OFFSET 1) "
							+ "AND ca.coAccountID = cb.coAccountID AND ca.accountType NOT IN ('Income','Expense') AND cb.coAccountID ="+theAccountID;
					aQuery = aSession.createSQLQuery(aCustomerQry);
					aList = aQuery.list();
					aIterator = aList.iterator();
					
					if( aList.size()>0){
					while (aIterator.hasNext()) {
						Object[] bObj = (Object[])aIterator.next();
					aCoAcc.setGlTransactionId((Integer) 0);
					aCoAcc.setCredit(BigDecimal.ZERO);
					aCoAcc.setDebit(BigDecimal.ZERO);
					aCoAcc.setBankOpeningBalance((BigDecimal) bObj[4]);
					aCoAcc.setBankClosingBalance((BigDecimal) bObj[4]);  
					}}
				}
				
				
				
			}
			}
			aQueryList.add(aCoAcc);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;aCustomerQry=null;
		}
		return aQueryList;	
	}
	
	
	@Override
	public Boolean deleteChartOfAccount(Integer coAccountID) throws CompanyException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction; 
		String aCoBalanceQry=null, aCoLedgerDetailQry=null, aCoAccountQry=null;
		try {
			aTransaction = aSession.beginTransaction();
			aCoBalanceQry = "DELETE FROM coBalance WHERE coAccountID =" + coAccountID;
			aSession.createSQLQuery(aCoBalanceQry).executeUpdate();
			aCoLedgerDetailQry = "DELETE FROM coLedgerDetail WHERE coAccountID = " + coAccountID;
			aSession.createSQLQuery(aCoLedgerDetailQry).executeUpdate();
			aCoAccountQry = "DELETE FROM coAccount WHERE coAccountID = " + coAccountID;
			aSession.createSQLQuery(aCoAccountQry).executeUpdate();
			aTransaction.commit();
		} catch (HibernateException e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.flush();
			aCoBalanceQry=null; aCoLedgerDetailQry=null; aCoAccountQry=null;
		}
		return true;
	}
	
	public SysAccountLinkage getAccountAdditionalInfo() throws CompanyException {
		Session aSession = null;
		SysAccountLinkage aSysAccountlinkage = null;Query query =null;
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("FROM  SysAccountLinkage");
			if(!query.list().isEmpty())
				aSysAccountlinkage = (SysAccountLinkage) query.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close(); query =null;
		}
		return  aSysAccountlinkage;
	}

	@Override
	public int getRecordChartsCount() throws CompanyException {
		String aChartsCountStr = "SELECT COUNT(coAccountID) AS count FROM coAccount";
		Session aSession = null;
		BigInteger aTotalCount = null;Query aQuery = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aChartsCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();aChartsCountStr =null;aQuery = null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public List<?> getChartsAccountsList(int theFrom, int theTo) throws CompanyException {
		Session aSession = null;
		List<CoAccountBean> aQueryList = new ArrayList<CoAccountBean>();
		String aCustomerQry = "SELECT * FROM coAccount ORDER BY Number ASC"; /**LIMIT " + theFrom + ", " + theTo;*/
		CoAccountBean aCoAcc = null;Query aQuery =null;
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCoAcc = new CoAccountBean();
				Object[] aObj = (Object[])aIterator.next();
				aCoAcc.setCoAccountId((Integer) aObj[0]);
				if((Byte) aObj[1] == 1){
					aCoAcc.setInActive(true);
				}else{
					aCoAcc.setInActive(false);
				}
				aCoAcc.setNumber((String) aObj[2]);
				aCoAcc.setDescription((String) aObj[3]);
				aCoAcc.setBalanceSheetColumn((Short) aObj[4]);
				if((Byte) aObj[5] == 1){
					aCoAcc.setIncludeWhenZero(true);
				}else{
					aCoAcc.setIncludeWhenZero(false);
				}
				if((Byte) aObj[6] == 1){
					aCoAcc.setDebitBalance(true);
				}else{
					aCoAcc.setDebitBalance(false);
				}
				if((Byte) aObj[7] == 1){
					aCoAcc.setContraAccount(true);
				}else{
					aCoAcc.setContraAccount(false);
				}
				aCoAcc.setLineAboveAmount((Short) aObj[8]);
				aCoAcc.setLineBelowAmount((Short) aObj[9]);
				aCoAcc.setTotalingLevel((Short) aObj[10]);
				aCoAcc.setVerticalSpacing((Short) aObj[11]);
				aCoAcc.setHorizontalSpacing((Short) aObj[12]);
				if((Byte) aObj[13] == 1){
					aCoAcc.setFontLarge(true);
				}else{
					aCoAcc.setFontLarge(false);
				}
				if((Byte) aObj[14] == 1){
					aCoAcc.setFontBold(true);
				}else{
					aCoAcc.setFontBold(false);
				}
				if((Byte) aObj[15] == 1){
					aCoAcc.setFontItalic(true);
				}else{
					aCoAcc.setFontItalic(false);
				}
				if((Byte) aObj[16] == 1){
					aCoAcc.setFontUnderline(true);
				}else{
					aCoAcc.setFontUnderline(false);
				}
				if((Byte) aObj[17] == 1){
					aCoAcc.setTax1099(true);
				}else{
					aCoAcc.setTax1099(false);
				}
				aCoAcc.setSubAccount((String) aObj[18]);
				if((Byte) aObj[19] == 1){
					aCoAcc.setIsSubAccount(true);
				}else{
					aCoAcc.setIsSubAccount(false);
				}
				if((Byte) aObj[20] == 1){
					aCoAcc.setIsMasterAccount(true);
				}else{
					aCoAcc.setIsMasterAccount(false);
				}
				if((Byte) aObj[21] == 1){
					aCoAcc.setDollarSign(true);
				}else{
					aCoAcc.setDollarSign(false);
				}
				
				aCoAcc.setTypeAccount((String)aObj[22]);
				aCoAcc.setFinancialStatus((Byte)aObj[24]);
				
				aQueryList.add(aCoAcc);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();aCustomerQry=null;aQuery =null;
		}
		return aQueryList;	
	}
	@Override
	public List<?> getChartsAccountsListforTax(int theFrom, int theTo) throws CompanyException {
		Session aSession = null;
		List<CoAccountBean> aQueryList = new ArrayList<CoAccountBean>();
		CoAccountBean aCoAcc = null;String aCustomerQry =null;Query aQuery =null;
		aCustomerQry = "SELECT * FROM coAccount"; /**LIMIT " + theFrom + ", " + theTo;*/
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCoAcc = new CoAccountBean();
				Object[] aObj = (Object[])aIterator.next();
				aCoAcc.setCoAccountId((Integer) aObj[0]);
				if((Byte) aObj[1] == 1){
					aCoAcc.setInActive(true);
				}else{
					aCoAcc.setInActive(false);
				}
				aCoAcc.setNumber((String) aObj[2]);
				aCoAcc.setDescription("["+(aObj[2]+" - "+ aObj[3])+"]");
				aCoAcc.setBalanceSheetColumn((Short) aObj[4]);
				if((Byte) aObj[5] == 1){
					aCoAcc.setIncludeWhenZero(true);
				}else{
					aCoAcc.setIncludeWhenZero(false);
				}
				if((Byte) aObj[6] == 1){
					aCoAcc.setDebitBalance(true);
				}else{
					aCoAcc.setDebitBalance(false);
				}
				if((Byte) aObj[7] == 1){
					aCoAcc.setContraAccount(true);
				}else{
					aCoAcc.setContraAccount(false);
				}
				aCoAcc.setLineAboveAmount((Short) aObj[8]);
				aCoAcc.setLineBelowAmount((Short) aObj[9]);
				aCoAcc.setTotalingLevel((Short) aObj[10]);
				aCoAcc.setVerticalSpacing((Short) aObj[11]);
				aCoAcc.setHorizontalSpacing((Short) aObj[12]);
				if((Byte) aObj[13] == 1){
					aCoAcc.setFontLarge(true);
				}else{
					aCoAcc.setFontLarge(false);
				}
				if((Byte) aObj[14] == 1){
					aCoAcc.setFontBold(true);
				}else{
					aCoAcc.setFontBold(false);
				}
				if((Byte) aObj[15] == 1){
					aCoAcc.setFontItalic(true);
				}else{
					aCoAcc.setFontItalic(false);
				}
				if((Byte) aObj[16] == 1){
					aCoAcc.setFontUnderline(true);
				}else{
					aCoAcc.setFontUnderline(false);
				}
				if((Byte) aObj[17] == 1){
					aCoAcc.setTax1099(true);
				}else{
					aCoAcc.setTax1099(false);
				}
				aCoAcc.setSubAccount((String) aObj[18]);
				if((Byte) aObj[19] == 1){
					aCoAcc.setIsSubAccount(true);
				}else{
					aCoAcc.setIsSubAccount(false);
				}
				if((Byte) aObj[20] == 1){
					aCoAcc.setIsMasterAccount(true);
				}else{
					aCoAcc.setIsMasterAccount(false);
				}
				if((Byte) aObj[21] == 1){
					aCoAcc.setDollarSign(true);
				}else{
					aCoAcc.setDollarSign(false);
				}
				aQueryList.add(aCoAcc);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry =null;aQuery =null;
		}
		return aQueryList;	
	}
	@Override
	public Boolean updateAdditionalInfo(SysAccountLinkage aSysAccountLinkage) throws CompanyException {
		Session aSession = null;
		SysAccountLinkage theSysAccountLinkage=null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction =  aSession.beginTransaction();
			aTransaction.begin();
			theSysAccountLinkage = (SysAccountLinkage) aSession.get(SysAccountLinkage.class, aSysAccountLinkage.getSysAccountLinkageId());
			theSysAccountLinkage.setSysAccountLinkageId(aSysAccountLinkage.getSysAccountLinkageId());
			theSysAccountLinkage.setCoRangeAsset1(aSysAccountLinkage.getCoRangeAsset1());
			theSysAccountLinkage.setCoRangeAsset2(aSysAccountLinkage.getCoRangeAsset2());
			theSysAccountLinkage.setCoRangeLiability1(aSysAccountLinkage.getCoRangeLiability1());
			theSysAccountLinkage.setCoRangeLiability2(aSysAccountLinkage.getCoRangeLiability2());
			theSysAccountLinkage.setCoRangeEquity1(aSysAccountLinkage.getCoRangeEquity1());
			theSysAccountLinkage.setCoRangeEquity2(aSysAccountLinkage.getCoRangeEquity2());
			theSysAccountLinkage.setCoRangeIncome1(aSysAccountLinkage.getCoRangeIncome1());
			theSysAccountLinkage.setCoRangeIncome2(aSysAccountLinkage.getCoRangeIncome2());
			theSysAccountLinkage.setCoRangeExpense1(aSysAccountLinkage.getCoRangeExpense1());
			theSysAccountLinkage.setCoRangeExpense2(aSysAccountLinkage.getCoRangeExpense2());
			theSysAccountLinkage.setCoAccountIdap(aSysAccountLinkage.getCoAccountIdap());
			theSysAccountLinkage.setCoAccountIdfreight(aSysAccountLinkage.getCoAccountIdfreight());
			theSysAccountLinkage.setCoAccountIdsalesTaxPaid(aSysAccountLinkage.getCoAccountIdsalesTaxPaid());
			theSysAccountLinkage.setCoAccountIddiscountsTaken(aSysAccountLinkage.getCoAccountIddiscountsTaken());
			theSysAccountLinkage.setCoAccountIdmisc(aSysAccountLinkage.getCoAccountIdmisc());
			theSysAccountLinkage.setCoAccountIdinventoryAdjustment(aSysAccountLinkage.getCoAccountIdinventoryAdjustment());
			theSysAccountLinkage.setCoAccountIdnetSales(aSysAccountLinkage.getCoAccountIdnetSales());
			theSysAccountLinkage.setCoAccountIdpl(aSysAccountLinkage.getCoAccountIdpl());
			theSysAccountLinkage.setCoAccountIdcurEarnings(aSysAccountLinkage.getCoAccountIdcurEarnings());
			theSysAccountLinkage.setCoAccountIdretainedEarnings(aSysAccountLinkage.getCoAccountIdretainedEarnings());
			theSysAccountLinkage.setCoAccountIdar(aSysAccountLinkage.getCoAccountIdar());
			theSysAccountLinkage.setCoAccountIddiscounts(aSysAccountLinkage.getCoAccountIddiscounts());
			theSysAccountLinkage.setCoAccountIdsalesTaxInv(aSysAccountLinkage.getCoAccountIdsalesTaxInv());
			theSysAccountLinkage.setCoAccountIdshipping(aSysAccountLinkage.getCoAccountIdshipping());
			theSysAccountLinkage.setCoAccountIdshipInventory(aSysAccountLinkage.getCoAccountIdshipInventory());
			theSysAccountLinkage.setCoAccountIdotherCharges(aSysAccountLinkage.getCoAccountIdotherCharges());
			theSysAccountLinkage.setCoAccountIdpayments(aSysAccountLinkage.getCoAccountIdpayments());
			aSession.update(theSysAccountLinkage);
			aTransaction.commit();
			return true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public Coaccount getChartOfAccount(Integer theCoAccountID) throws CompanyException {
		Session aSession = null;
		Coaccount aCoaccount = null;
		try {
			aSession = itsSessionFactory.openSession();
			aCoaccount = (Coaccount) aSession.get(Coaccount.class, theCoAccountID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aCoaccount;
	}
	public List<AutoCompleteBean> getAccountNumbersSearch(	String theAccountNumberKeyword) throws CompanyException {
		itsLogger.debug("Query getSearchUserList");
		String aSearchSelectQry = "SELECT coAccountID, Number, Description FROM coAccount WHERE Description LIKE '%"+theAccountNumberKeyword+"%' " +
															"UNION " +
															"SELECT coAccountID, Number, Description FROM coAccount WHERE Number LIKE '%"+theAccountNumberKeyword+"%'  ORDER BY Number ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;aSearchSelectQry=null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aAutoCompleteBean.setId((Integer) aObj[0]);
				aAutoCompleteBean.setLabel((String)aObj[1]);
				aAutoCompleteBean.setValue((String)aObj[2]);
				aQueryList.add(aAutoCompleteBean);
			}
			if(aQueryList.isEmpty()){
				aAutoCompleteBean.setValue(" ");	
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;aQuery =null;
		}
		return  aQueryList;
	}

	@Override
	public List<Coaccount> getAccountNumber() throws CompanyException {
		Session aSession = null;
		List<Coaccount> aQueryList = null;
		Query query =null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	query = aSession.createQuery("FROM  Coaccount ORDER BY description asc");
			// Retrieve all
		// 	if(!query.list().isEmpty()){
		 		aQueryList = query.list();
		// 	}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CompanyException aCompanyException = new CompanyException(excep.getMessage(), excep);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return  aQueryList;
	}
	
	
	@Override
	public List<Coaccount> getListOfAccountsFordropdown() throws CompanyException {
		/*
		Turborep query for fetching glclient
		 * SELECT coAccount.Number, coAccount.Description, coAccount.coAccountID FROM coAccount
		 *WHERE (((coAccount.InActive)=0 Or (coAccount.InActive) Is Null) AND ((coAccount.TotalingLevel)=0))
		 *ORDER BY coAccount.Number
		 * 
		 */
		String aSelectAccountsQry = "SELECT coAccountID,Number,Description FROM coAccount WHERE ((coAccount.InActive)=0 Or (coAccount.InActive) Is Null) ORDER BY coAccount.Number"; 
		ArrayList<Coaccount> aListOfAccounts = new ArrayList<Coaccount>();
		Session aSession = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Coaccount coaccount = new Coaccount();
				Object[] aObj = (Object[])aIterator.next();
				coaccount.setCoAccountId((Integer)aObj[0]);
				coaccount.setNumber((String)aObj[1]);
				coaccount.setDescription((String)aObj[2]);
				aListOfAccounts.add(coaccount);
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry=null;aQuery =null;
		}
		return aListOfAccounts;
	}
	
	@Override
	public List<Coaccount> getAllAccountsfromAccountNumber(String AccountNumber) throws CompanyException {
		String aSelectAccountsQry = "SELECT * FROM coAccount where Number='"+AccountNumber+"'"; /*** LIMIT " + theFrom + ", " + theRows;*/
		ArrayList<Coaccount> aListOfAccounts = null;Query aQuery =null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			aListOfAccounts = (ArrayList<Coaccount>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			 aQuery =null;
		}
		return aListOfAccounts;
	}
	

@Override
public List<CoAccountBean> getChartsAccountSegment() throws CompanyException {
	Session aSession = null;
	List<CoAccountBean> aQueryList = new ArrayList<CoAccountBean>();
	List<String> listary = new ArrayList<String>();
	String aCustomerQry = "SELECT * FROM coAccount "; /**LIMIT " + theFrom + ", " + theTo;*/
	Query aQuery = null;
	try{
		aSession = itsSessionFactory.openSession();
		aQuery = aSession.createSQLQuery(aCustomerQry);
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			CoAccountBean aCoAcc = new CoAccountBean();
			Object[] aObj = (Object[])aIterator.next();
			
			if(aObj[2]!=null)
			{
				String Number = (String) aObj[2];
			
				if(Number.contains("-")){
				String[] arry = Number.split("-");
				
				if((arry.length>1) && (arry[1]!=null) && (!arry[1].trim().equals("")) && (!listary.contains(arry[1]))){
					
					
					aCoAcc.setNumber((String)arry[1]);
					aCoAcc.setCoAccountId((Integer)aObj[0]);
					listary.add(arry[1]);
					aQueryList.add(aCoAcc);
				}
				}
			}
			
		}
	} catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
	} finally {
		aSession.flush();
		aSession.close();
		aCustomerQry=null;aQuery = null;
	}
	
	return aQueryList;	
}

@Override
public BigDecimal getPayableApproved() throws CompanyException {
	String aJobCountStr = "SELECT SUM(ApplyingAmount) FROM veBillPay";
	Session aSession = null;Query aQuery =null;
	BigDecimal approveVal = new BigDecimal(0.00);
	try {
		// Retrieve aSession from Hibernate
		aSession = itsSessionFactory.openSession();
		 aQuery = aSession.createSQLQuery(aJobCountStr);
		List<?> aList = aQuery.list();
		approveVal =  (BigDecimal) aList.get(0);
	}  catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
	}  finally {
		aSession.flush();
		aSession.close();
		aJobCountStr=null;aQuery =null;
	}
	return approveVal;
}

@Override
public Coaccount getAandD(String rxMasterId) {
Session aSession = null;Query aQuery =null;
Coaccount coAccount=null;
try{
 aSession = itsSessionFactory.openSession();
  aQuery = aSession.createQuery("FROM com.turborep.turbotracker.company.dao.Coaccount WHERE coAccountID=(SELECT coExpenseAccountId FROM com.turborep.turbotracker.vendor.dao.Vemaster WHERE veMasterId =?)");
  aQuery.setString(0, rxMasterId);
  List<Coaccount> coAccounts=aQuery.list();
  if(coAccounts.size()!=0 || coAccounts!=null){    
   coAccount=coAccounts.get(0);
  }
}catch(Exception e){
 itsLogger.error(e.getMessage(), e);
}finally {
 aSession.flush();
 aSession.close();
 aQuery =null;
}
return coAccount;
}
}