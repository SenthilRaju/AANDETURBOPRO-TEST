package com.turborep.turbotracker.company.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.journalentryhistory;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoAccountBean;
import com.turborep.turbotracker.company.dao.CoHeaderBean;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Coledgerdetail;
import com.turborep.turbotracker.company.dao.Coledgerheader;
import com.turborep.turbotracker.company.dao.Coledgersource;

public interface JournalsService {
	public List<?> getJournalsDetails() throws CompanyException;
	
	public Integer getJournalTotalCount()throws CompanyException;
	public List<GlTransaction> getTotalJournals(Integer from, Integer to) throws CompanyException;
	public List<GlTransaction> getTotalJournalDetails(String coLedgerHeaderId) throws CompanyException;
	public GlTransaction updateJournalEntries(GlTransaction aGlTransaction,int gllinkagestatus,String oper) throws CompanyException;
	public void updateJournalDetails(Coaccount Obj,Coledgerdetail coledgerObj) throws CompanyException;
	public int deleterecord(String headerId) throws CompanyException;
	public int deleteGlTransactionRecord(String headerId) throws CompanyException;
	public Integer CreateJournalEntriesHistory(journalentryhistory ajournalentryhistory)throws CompanyException;
	public List<journalentryhistory> getListOfJournalshistory(String reference)throws CompanyException;
	public int updateGlLinkage(String refId, int status) throws CompanyException;

	//public ArrayList<GlTransaction> rollBackGlTransaction(Integer convertintoInteger, Coledgersource coledger) throws BankingException;
	public ArrayList<GlTransaction> rollBackGlTransaction(GlRollback aGlRollback) throws BankingException,CompanyException;

	public Integer AddnewJournalEntries(GlTransaction aGlTransaction,String operation) throws Exception;
}
