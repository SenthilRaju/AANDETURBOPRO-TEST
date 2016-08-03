package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.MoAccount;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoAccountBean;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;

public interface ChartOfAccountsService {

	public List<Coaccount> getListOfAccounts(int theFrom, int theRows) throws CompanyException;

	BigInteger getListOfAccountsCount() throws CompanyException;

	public Coaccount updateChartAccount(Coaccount aCoaccount) throws CompanyException;

	public boolean addNewChartAccount(Coaccount theCoaccount) throws CompanyException;
	
	public List<CoAccountBean> getAccountsList(Integer theAccounts) throws CompanyException;

	public Boolean deleteChartOfAccount(Integer coAccountID) throws  CompanyException;
	
	public SysAccountLinkage getAccountAdditionalInfo() throws CompanyException;
	
	public int getRecordChartsCount() throws CompanyException;

	public List<?> getChartsAccountsList(int aFrom, int aTo) throws CompanyException;

	public Coaccount getChartOfAccount(Integer theCoAccountID) throws CompanyException;

	public List<AutoCompleteBean> getAccountNumbersSearch(	String theAccountSearch) throws CompanyException;

	public Boolean updateAdditionalInfo(SysAccountLinkage aSysAccountLinkage) throws CompanyException;

	public List<Coaccount> getAccountNumber() throws CompanyException;

	public List<?> getChartsAccountsListforTax(int theFrom, int theTo)	throws CompanyException;

	public List<Coaccount> getListOfAccountsFordropdown()throws CompanyException;
	
	public List<GlTransaction> getLedgerDetails(Integer theAccounts,Integer periodID,Integer yearID) throws CompanyException;
	
	public List<GlTransaction> getLedgerDetailsPeriod(Integer theAccountID,Integer periodId,Integer yearId) throws CompanyException ;
	
	public List<GlTransaction> getLedgerDetailscurentPeriod(Integer theAccountID,Integer periodId,Integer yearId) throws CompanyException ;
	
	public List<Coaccount> getAllAccountsfromAccountNumber(String AccountNumber) throws CompanyException;

	public List<CoAccountBean> getChartsAccountSegment() throws CompanyException; 

	public BigDecimal getPayableApproved() throws CompanyException;

	public Coaccount getAandD(String rxMasterId);

}
