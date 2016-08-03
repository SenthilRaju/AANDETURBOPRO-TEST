/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.banking.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.banking.dao.MoAccount;
import com.turborep.turbotracker.banking.dao.Momultipleaccount;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.dao.PrintCheckBean;
import com.turborep.turbotracker.banking.dao.ReconcileFLStatus;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.vendor.dao.Vebillpay;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.exception.VendorException;

public interface BankingService {

	public ArrayList<MoAccount> getBankAccountDetails() throws BankingException;

	public int getRecordChartsCount(ArrayList<?> theTransactionDetails,Integer moAccount) throws BankingException;

	public List<MoAccount> getBankAccountList(int theFrom, int theTo) throws BankingException;

	public MoAccount getSingleBankingDetails(Integer theMoAccountID) throws BankingException;

	public Boolean deleteBankingAccount(Integer theMoAccountID) throws BankingException;

	public boolean addBankingDetailsDetails(MoAccount aMoaccount) throws BankingException;

	public boolean updateBankingDetailsDetails(MoAccount aMoaccount) throws BankingException;

	public List<Motransaction> getTransactionRegisterList(int theFrom, int theTo, ArrayList<?> theTransactionDetails,int moaccountid) throws BankingException;

	//public int addTransactionDetails(Motransaction aMotransaction) throws BankingException;

	public int addTransactionDetails(Motransaction aMotransaction,Session aSession,String insertStatus) throws BankingException;
	
	public boolean updateTransactionDetails(Motransaction aMotransaction) throws BankingException;
	
	public boolean updateTransactionDetails1(Motransaction aMotransaction,Session aSession) throws BankingException;

	public boolean deleteTransactionDetails(Motransaction aMotransaction) throws BankingException;

	public boolean voidTransactionDetails(Motransaction theMotransaction) throws BankingException;
	
	public boolean voidTransactionDetails1(Motransaction theMotransaction,Session aSession) throws BankingException;
	
	public PrintCheckBean printCheck(Motransaction theMotransaction,Integer checkNumber,Integer yearID,Integer periodID,String Username,Integer userID) throws BankingException, CompanyException;

	
	public Boolean billExistsToPay() throws BankingException ;
	
	public void printCheckDetails(HttpServletResponse theResponse,HttpServletRequest therequest) throws BankingException ;
	
	public void creditCheckDetails(HttpServletResponse theResponse,HttpServletRequest therequest) throws BankingException ;
	
	public Rxaddress getAddressFromTransactionId(Integer moTransactionID) throws BankingException;
	
	public List<Motransaction> getChecksLists(int moaccountId,String reference,short checkType) throws BankingException;

	List<Motransaction> getTransactionRegisterListForReconcile(int moAccountId,	int TransactionType,String sortType, String sortBy) throws BankingException;

	boolean tempRecTransactionDetails(Integer moAccountID)throws BankingException;

	boolean insertReconcileFLStatus(ReconcileFLStatus theReconcileFLStatus)throws BankingException;
	
	public List<Vemaster> getAllVendors() throws VendorException;

	BigInteger getMaxCheckList(Integer moAccountID) throws BankingException;
	
	public BigInteger rePrintChecksInsert(Motransaction motrans,BigInteger startId,BigInteger endId,BigInteger checkNo,Integer yearID,Integer periodID,String username)throws BankingException;

	boolean updateMoTransactionReconcile(Motransaction theMotransaction)throws BankingException;

	boolean createMoTransactionReconcile(Motransaction theMotransaction)throws BankingException;

	boolean updateAllMoTransaction(Integer theMoAccountID, Integer theTempRec,Integer theTransactionType,Integer userID) throws BankingException;

	//public boolean updateDepositAndWithDraw(MoAccount theMoaccount,Transaction aTransaction)throws BankingException;
	
	//public boolean updateDepositAndWithDraw(MoAccount theMoaccount)throws BankingException;
	
	public boolean updateDepositAndWithDraw(MoAccount theMoaccount,Session aSession)throws BankingException;

	public Motransaction getMOtransactionDetails(Integer theMoTransactionID)
			throws BankingException;

	public Motransaction getResultingBalance(Integer theMoAccountID)throws BankingException;

	public BigDecimal getpageinitialamount(int from, int to, int moaccountid)throws BankingException;
	
	public ArrayList<AutoCompleteBean> getAccountList(String accno)throws BankingException;

	public boolean saveGlmultipleAccount(Momultipleaccount momulaccount)throws BankingException;
	
	public boolean saveGlmultipleAccount1(Momultipleaccount momulaccount,Session session)throws BankingException;

	public boolean updateGlmultipleAccount(Momultipleaccount momulacc)throws BankingException;
	
	public boolean updateGlmultipleAccount1(Momultipleaccount momulacc,Session session)throws BankingException;

	public int gettotalrecordCount(String query) throws BankingException;

	public List<Momultipleaccount> getGlAccountTransactionRegisterList(int theFrom,int theTo, int motransid) throws BankingException;

	public PrintCheckBean rePrintCheck(Motransaction theMotransaction,String checkNumber, String endNumber) throws BankingException;
	
	
	public boolean updateReconcileDetails(MoAccount theMoAccount) throws BankingException;
	
	
	/**Created by : Leo     Description: NewCheck PDF*/
	
	public void newCheckDetails(HttpServletResponse theResponse,HttpServletRequest therequest) throws BankingException ;
	
	
	public List<Motransaction> findbytransactionRegisterList( int theFrom,int theTo,ArrayList<?> thefilterDetails,int moaccountid) throws BankingException;
	
	public Boolean checkisExists(String referenceNo,String moAccountID) throws BankingException;
	
	public ArrayList<BigDecimal> getNegativeAmountValidation() throws BankingException;
	
	public ArrayList<Vebillpay> getCheckBillAvailability(Integer moAccountID,Integer userID) throws BankingException;

	public Integer bankingTrasactionsRollback(MoAccount moAccount,Motransaction moTransaction,BigDecimal balance,String type,boolean accountStatus,Integer yearID,Integer periodID)throws BankingException , CompanyException;
	
	public boolean deleteVeBillPayDetails(Integer MoAccountId)throws BankingException;
	
	public Integer getRxAddressIDfmRxmasterID(Integer theRxmasterId) throws CompanyException;
	
	/** Created by :Leo  Purpose: Transaction Rollback */
	
	public Integer createtransactionBanknewCheck(MoAccount moAccount,Motransaction M1,String DepositType,Integer yearID,Integer periodID,String userName,
			BigDecimal balance,boolean accountStatus, int motransiid,String oper,String gridData,boolean futureornot,BigDecimal currentbalance,
			BigDecimal oldamount,String NewMoTypeId,BigDecimal amt,String aMemo,String aRxMasterID)throws BankingException,CompanyException;
	
	public Integer getcountfindbytransactionRegisterList(ArrayList<?> theFilterDetails,int moaccountid) throws BankingException;
	
	public String getfindbytransactionQueryString(ArrayList<?> theFilterDetails,int moaccountid) throws BankingException;
	
	public List<Prwarehouse> getWhseList(Integer whseID) throws BankingException;

}
