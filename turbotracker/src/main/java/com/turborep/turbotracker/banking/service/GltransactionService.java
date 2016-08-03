package com.turborep.turbotracker.banking.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebilldistribution;
import com.turborep.turbotracker.vendor.dao.Vepo;
 
public interface GltransactionService {

	public Coaccount getcoAccountDetails(Motransaction aMotransaction) throws BankingException;

	public SysAccountLinkage getsysAccountLinkageDetail() throws BankingException;	
	
	public ArrayList<Vebill> getveBillDetailsList(Motransaction aMotransaction) throws BankingException;

	public Cofiscalyear getCofiscalYearDetail()throws BankingException;

	public Cofiscalperiod getCofiscalPeriodDetail()throws BankingException;

	public Coledgersource getColedgersourceDetail(String JournalID) throws BankingException;

	public boolean saveGlTransactionTable(Motransaction aMotransaction,Coaccount coaccount, SysAccountLinkage aSysAccountLinkage,ArrayList<Vebill> vebilllist, Cofiscalperiod aCofiscalperiod,Cofiscalyear aCofiscalyear, Coledgersource aColedgersource) throws BankingException;

	public Coaccount getCoaccountDetailsBasedoncoAccountid(int coAccountId) throws BankingException;
	
	public int saveGltransactionTable(GlTransaction glTransactionobj) throws BankingException;
	
	public int saveGltransactionTable1(GlTransaction glTransactionobj,Session aSession) throws BankingException;
	
	public boolean saveGlLinkageTable(GlLinkage glGlLinkageobj) throws BankingException;
	
	public boolean saveGlLinkageTable1(GlLinkage glGlLinkageobj,Session aSession) throws BankingException;
	
	/*
	 *Modified Signature by Simon
	 *Reason for Modifying ID#573 for differentiating purchases 
	 */
	public void recieveVendorBill(List<Prmaster> prMaster,Vebill veBillObj, Integer veBillDetailObj,Integer yearID,Integer periodID,String username)  throws BankingException, CompanyException;
	
	public Vepo getPOnumberfromvePOID(Integer vePOId)throws BankingException ;
	
	public Rxmaster getTransactionDescriptionfromrxMasterID(Integer veRxMasterID) throws BankingException ;
	
	public String getRecieptType(Integer typeid) throws BankingException;
	
	public ArrayList<Vebill> getAllVeBillDetails(Integer thevebillID) throws BankingException;
	
	public ArrayList<Object> getAllVeBillDetailsandDistripution(Integer thevebillID) throws BankingException;
	
	public ArrayList<GlLinkage> getAllGlLinkageDetails(Integer thevebillID,Integer coledger) throws BankingException;
	
	public ArrayList<GlTransaction> getAllglTransactionDetails(Integer thevebillID) throws BankingException;
	
	public String getAllVeBillDetailswithvePoid(Integer thevebillID) throws BankingException;
	
	
	public ArrayList<Vebilldistribution> getAllVeBillDistripution(Integer theVeBillID) throws BankingException;
	
	//public void bankingDeposits(Motransaction motransaction, String DepositType,Integer yearID, Integer periodID,String userName) throws BankingException, CompanyException;
	
	public void bankingDeposits(Motransaction motransaction, String DepositType,Integer yearID, Integer periodID,String userName,Session aSession) throws BankingException, CompanyException;
	
	//public void insertDebitDetailsofBankTransaction(GlTransaction glTransaction, Coaccount CoAccountdetails,Coledgersource  aColedgersource,Motransaction aMotransaction, BigDecimal dBorCrAmount)throws BankingException ;
	
	public void insertDebitDetailsofBankTransaction(GlTransaction glTransaction, Coaccount CoAccountdetails,Coledgersource  aColedgersource,Motransaction aMotransaction, BigDecimal dBorCrAmount,Session aSession)throws BankingException ;
	
	//public void insertCreditDetailsofBankTransaction(GlTransaction glTransaction, Coaccount CoAccountdetails,Coledgersource  aColedgersource,Motransaction aMotransaction, BigDecimal dBorCrAmount)throws BankingException ;
	
	public void insertCreditDetailsofBankTransaction(GlTransaction glTransaction, Coaccount CoAccountdetails,Coledgersource  aColedgersource,Motransaction aMotransaction, BigDecimal dBorCrAmount,Session aSession)throws BankingException ;
	
	//public void rollBackGlTransaction(Integer veBillID,Coledgersource coledger) throws BankingException;
	public void rollBackGlTransaction(GlRollback aglRollback) throws BankingException,CompanyException;
	
	//public void rollBackGlTransaction1(Integer veBillID,Coledgersource coledger,Session aSession) throws BankingException;
	public void rollBackGlTransaction1(GlRollback aglRollback,Session aSession) throws BankingException,CompanyException;
	
	public void insertglAprollBackInsert(GlTransaction glTransObj,Integer coledgersourceid,Integer vebillid)throws BankingException;
	
	public void insertglrollBackInsert(GlTransaction glTransObj,Integer coledgersourceid,Integer vebillid)throws BankingException;
	
	public ArrayList<GlTransaction> getAllglTransactionDetailswithcoAccountID(Integer coAccountid) throws BankingException;
	
	
	
	public int  postCusInvoiceDetails(Cuinvoice cuInvoice,Cureceipt cuReceipt,Integer yearID,Integer periodID,UserBean aUserBean,Session aSession)	throws BankingException, CompanyException;
	/**Created by: Leo    Description: Customer Invoice Receivable  */
	
	public void receiveCustomerInvoiceBill(Cuinvoice cuInvoice,Integer yearID,Integer periodID,String userName)throws BankingException, CompanyException;
	
	public ArrayList<SysAccountLinkage> getAllsysaccountLinkage(Cuinvoice cuInvoice)throws BankingException;
	
	public void receiveCreditDebitMemo(Cuinvoice cuInvoice,Integer yearID,Integer periodID,String username)throws BankingException ,JobException,CompanyException;
	
	
	/** Transaction start and rollback **/
	
	public void startTransaction()throws BankingException;
	
	public void startRollBack()throws BankingException;
	
	public void startCommit()throws BankingException;
	
	public String getReferencenofromglTransaction(Integer vebillid,Integer coledgerid)throws BankingException;
	
	
	/** Get Period Details based on given date*/
	
	public Cofiscalperiod getAllcoFiscalPeriodbasedongivenDate(java.util.Date periodDate,Integer yearID);
	
	public Coaccount getcoAccountfromAccountNumber(String accNumber) throws BankingException;
	
	public String getsubAccountfromDivision(Integer cuInvoicID) throws BankingException;
	
	public String insertMissingEntries(int fromId,int toId)throws BankingException;
	
	/**  Customer Payments ID #395 **/
	public boolean postCustomerPaymentDetails(Cureceipt cuReceipt,BigDecimal discountAmt,Integer yearID,Integer periodID,String username,Session aSession)throws BankingException, CompanyException;
	
	/** Customer payment ID #520 **/
	public void rollBackGlforCuPayment(GlRollback aGlRollback, Session aSession) throws BankingException;
	
	public void insertInventoryAdjustmentCount(Integer prWarehouseid,Integer prInventoryid, BigDecimal Amount,String username,String prStatus)throws InventoryException,BankingException;

	public void updateTaxAdjustment(Cuinvoice cuInvoice,Integer year,Integer period,String username) throws BankingException; 
	
	/** 13th period closed ID #212**/
	
	public void insertglTransactionwith13thperiod(Integer yearID,Integer periodID,String Username)  throws CompanyException; 
	
}
