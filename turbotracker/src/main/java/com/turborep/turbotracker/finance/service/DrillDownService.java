/**
 * 
 */
package com.turborep.turbotracker.finance.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.banking.dao.GlTransactionv1;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.finance.dao.GlReporting;
import com.turborep.turbotracker.finance.dao.GlReportingAccounts;
import com.turborep.turbotracker.finance.dao.GlTransactionTest;
import com.turborep.turbotracker.finance.dao.TrialBalance;
import com.turborep.turbotracker.finance.exception.DrillDownException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.user.dao.UserBean;


/**
 * 
 * Handles CRUD services for Finance DrillDown
 * 
 * @author Jenith
 */

public interface DrillDownService {

	/**
	 * Created by : Leo    Date: 12-09-2014
	 * 
	 * Description: GLTransaction Report Generate.
	 * 
	 * */
	
	public BigInteger getGlTransactionCount()throws DrillDownException;
	
	public List<GlTransactionTest> getAccountDetails1(String accountNumber,String periodFrom,String PeriodTo, String yearId,String mostrecentPeriodId,Integer from ,Integer to) throws DrillDownException;
	
	public List<GlReportingAccounts> getAccountDetails(String accountNumber,String accDescription,String periodFromID,String periodTo,String yearID,Integer accID)throws DrillDownException ;
	
	public List<GlReportingAccounts> getJournalBalance(String accountID,String Period,String accDescription, String yearId,Integer coAccountsID )throws DrillDownException;
	
	public List<GlReportingAccounts> getJournalDetails (String accountID,String accountDesc,String period, String yearID,String journalId,Integer coAccountsID)	throws DrillDownException;
	
	public List<GlReportingAccounts> getTransactionDetails(String transactionReferenceID,String description,String Sourceid)throws DrillDownException;
	
	

	public GlReportingAccounts getJournalDetailsSum(String accountID,String period,String yearID,String journalId,Integer coAccountsID)	throws DrillDownException;
	
	public BigInteger getJournalDetailsCount(String accountNumber,String periodFromID,String periodTo,String yearID,String mostrecentPeriod,String pStrdate,String journalId)	throws DrillDownException;
		
	public List<Cofiscalyear> getYearList() throws DrillDownException;
	
	public List<GlReportingAccounts> getJournalBalance(String accountID,String Period,String accDescription, String yearId,Integer fromID,Integer toId,String sIdx,String sOrd,Integer coAccountID )throws DrillDownException;
	
	public List<GlReportingAccounts> getJournalDetails (String accountID,String accountDesc,String period, String yearID,String journalId,Integer fromID,Integer toId,String sIdx,String sOrd,Integer coAccountsID )	throws DrillDownException;
	
	public List<TrialBalance> getAllTrialBalances(Integer periodID,Integer yearID,Integer fromcoAccountID,Integer tocoAccountID) throws DrillDownException;
	
	public Cofiscalperiod getAllPeriodsbasedonDate(Date dateto) throws DrillDownException ;
	
	public List getProfitandlossdetails(String query);

	public BigDecimal getNetProfitAmount(Integer cofiscalPeriodID) throws DrillDownException;
	
}
