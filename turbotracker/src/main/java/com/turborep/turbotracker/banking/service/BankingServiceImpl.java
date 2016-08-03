/**
 * Copyright (c) 2014 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.banking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.MoAccount;
import com.turborep.turbotracker.banking.dao.Molinkage;
import com.turborep.turbotracker.banking.dao.Molinkagedetail;
import com.turborep.turbotracker.banking.dao.Momultipleaccount;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.dao.Movendorchecktemp;
import com.turborep.turbotracker.banking.dao.PrintCheckBean;
import com.turborep.turbotracker.banking.dao.ReconcileFLStatus;
import com.turborep.turbotracker.banking.dao.VeBillPaymentHistory;
import com.turborep.turbotracker.banking.dao.VendorPayBean;
import com.turborep.turbotracker.banking.dao.WritecheckDetails;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebillpay;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Service("bankingService")
public class BankingServiceImpl implements BankingService {

	Logger itsLogger = Logger.getLogger(BankingServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name="vendorService")
	private VendorServiceInterface vendorService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	private Integer[] rxAddressID =null;
	private Integer[] billId =null;
	private Date checkDate;
	
	@Override
	public ArrayList<MoAccount> getBankAccountDetails() throws BankingException {
		Session aSession = null;
		ArrayList<MoAccount> aMoAccountList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM  MoAccount where InActive <> 1");
			aMoAccountList = (ArrayList<MoAccount>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aMoAccountList;
	}

	@Override
	public int getRecordChartsCount(ArrayList<?> theTransactionDetails,Integer moAccount) throws BankingException {
		StringBuilder aStringBuilder = new StringBuilder("");
		if(theTransactionDetails != null){
			if(theTransactionDetails.get(0) !="" && theTransactionDetails.get(0) != null){
				aStringBuilder.append("WHERE moAccountID ="+theTransactionDetails.get(0)+"");
			}
			if(theTransactionDetails.get(1) != "" && theTransactionDetails.get(1) != null && theTransactionDetails.get(2) != "" && theTransactionDetails.get(2) != null){
				String aTranDateString = theTransactionDetails.get(1).toString();
				String aTranToDateString = theTransactionDetails.get(2).toString();
				String[] aTranSactionDate = aTranDateString.split("%2F");
				String[] aTranSactionToDate = aTranToDateString.split("%2F");
				String aFromTranDate = aTranSactionDate[2]+"-"+aTranSactionDate[0]+"-"+aTranSactionDate[1];
				String aToTranDate = aTranSactionToDate[2]+"-"+aTranSactionToDate[0]+"-"+aTranSactionToDate[1];
				aStringBuilder.append(" AND date(TransactionDate) >= '"+aFromTranDate+"' AND date(TransactionDate) <= '"+aToTranDate+"'");
			}
			if(theTransactionDetails.get(3) != "" && theTransactionDetails.get(3) != null){
				aStringBuilder.append(" AND moTransactionTypeID =").append(theTransactionDetails.get(3));
			}
			if(theTransactionDetails.get(4) != "" && theTransactionDetails.get(4) != null){
				aStringBuilder.append(" AND moTransactionTypeID =").append(theTransactionDetails.get(4));
			}
			if(theTransactionDetails.get(5) != "" && theTransactionDetails.get(5) != null){
				aStringBuilder.append(" AND moTransactionTypeID =").append(theTransactionDetails.get(5));
			}
			if(theTransactionDetails.get(6) != "" && theTransactionDetails.get(6) != null){
				aStringBuilder.append(" AND moTransactionTypeID =").append(theTransactionDetails.get(6));
			}
			if(theTransactionDetails.get(7) != "" && theTransactionDetails.get(7) != null){
				aStringBuilder.append(" AND moTransactionTypeID =").append(theTransactionDetails.get(7));
			}
		}
		else
		{
			aStringBuilder.append("WHERE moAccountID ="+moAccount+"");
		}
		
		String aTransactionCountStr = "SELECT COUNT(moTransactionID) FROM moTransaction "+aStringBuilder;
		Session aSession = null;
		BigInteger aTotalCount = null;
		itsLogger.info("aTransactionCountStr=="+aTransactionCountStr);
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aTransactionCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aTransactionCountStr=null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public List<MoAccount> getBankAccountList(int theFrom, int theTo) throws BankingException {
		Session aSession = null;
		List<MoAccount> aQueryList=null;
		String aCustomerQry = "SELECT * FROM moAccount "; /* LIMIT " + theFrom + ", " + theTo; */
		MoAccount aMoaccount = null;
		boolean inActiveVal = false;
		try{
			aQueryList = new ArrayList<MoAccount>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aMoaccount = new MoAccount();
				Object[] aObj = (Object[])aIterator.next();
				aMoaccount.setMoAccountId((Integer) aObj[0]);
				
				 if( aObj[1] instanceof Boolean) {
					inActiveVal = (Boolean)aObj[1];
					if(inActiveVal)
						aMoaccount.setInActive(new Byte("1"));
						else
						aMoaccount.setInActive(new Byte("0"));
				  }
				  else if( aObj[1] instanceof Byte) {
					  aMoaccount.setInActive((Byte)aObj[1]);
				  }
				
			
				aMoaccount.setAccountType((Short) aObj[2]);
				aMoaccount.setDescription((String) aObj[3]);
				aMoaccount.setCoAccountIDAsset((Integer) aObj[4]);
				aMoaccount.setCoAccountIDDeposits((Integer) aObj[5]);
				aMoaccount.setCoAccountIDInterest((Integer) aObj[6]);
				aMoaccount.setCoAccountIDFees((Integer) aObj[7]);
				aMoaccount.setOpenBalance((BigDecimal) aObj[8]);
				aMoaccount.setAdditions((BigDecimal) aObj[9]);
				aMoaccount.setSubtractions((BigDecimal) aObj[10]);
				aMoaccount.setUndepositedReceipts((BigDecimal) aObj[11]);
				aMoaccount.setUnprintedPayables((BigDecimal) aObj[12]);
				aMoaccount.setUnprintedPayroll((BigDecimal) aObj[13]);
				aMoaccount.setNextCheckNumber((Integer) aObj[14]);
				aMoaccount.setEndingBalance((BigDecimal) aObj[15]);
				aMoaccount.setCompany1((String) aObj[16]);
				aMoaccount.setCompany2((String) aObj[17]);
				aMoaccount.setCompany3((String) aObj[18]);
				aMoaccount.setCompany4((String) aObj[19]);
				aMoaccount.setCompany5((String) aObj[20]);
				aMoaccount.setBank1((String) aObj[21]);
				aMoaccount.setBank2((String) aObj[22]);
				aMoaccount.setBank3((String) aObj[23]);
				aMoaccount.setBank4((String) aObj[24]);
				aMoaccount.setBank5((String) aObj[25]);
				aMoaccount.setCheckCode((String) aObj[26]);
				aMoaccount.setRoutingNumber((String) aObj[27]);
				aMoaccount.setAccountNumber((String) aObj[28]);
				
				if(aObj[29]!=null && (Boolean)aObj[29]==true){
					aMoaccount.setLogoYN(true);
				}else{
					aMoaccount.setLogoYN(false);
				}
				if(aObj[30]!=null){
					aMoaccount.setLineNo(JobUtil.ConvertintoInteger(aObj[30].toString()));
				}else{
					aMoaccount.setLineNo(0);
				}
				
				aQueryList.add(aMoaccount);
				itsLogger.info("moaccount ID"+aMoaccount.getMoAccountId());
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;	
	}

	@Override
	public MoAccount getSingleBankingDetails(Integer theMoAccountID) throws BankingException {
		Session aSession = null;
		MoAccount aMoaccount = null;
		try {
			aSession = itsSessionFactory.openSession();
			aMoaccount = (MoAccount) aSession.get(MoAccount.class, theMoAccountID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aMoaccount;
	}

	@Override
	public Boolean deleteBankingAccount(Integer theMoAccountID) throws BankingException {
		Session aBankingdetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		MoAccount aMoaccount = new MoAccount();
		List<MoAccount> aQueryList=null;
		String aCustomerQry = "SELECT * FROM moTransaction where moAccountID = "+theMoAccountID;
		boolean returnValue = true;
		
		try {
			aQueryList = aBankingdetailSession.createSQLQuery(aCustomerQry).list();
			if(aQueryList.size()==0)
			{
			aMoaccount.setMoAccountId(theMoAccountID);
			aMoaccount.setNextCheckNumber(0);
			aTransaction = aBankingdetailSession.beginTransaction();
			aTransaction.begin();
			aBankingdetailSession.delete(aMoaccount);
			aTransaction.commit();
			returnValue = false;
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aBankingdetailSession.flush();
			aBankingdetailSession.close();
		}
		return returnValue;
	}

	@Override
	public boolean addBankingDetailsDetails(MoAccount theMoaccount) throws BankingException {
		Session aMoAccountSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aMoAccountSession.beginTransaction();
			aTransaction.begin();
			aMoAccountSession.save(theMoaccount);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aMoAccountSession.flush();
			aMoAccountSession.close();
		}
		return true;
	}

	@Override
	public boolean updateBankingDetailsDetails(MoAccount theMoaccount) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		MoAccount aMoaccount = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aMoaccount = (MoAccount) aSession.get(MoAccount.class, theMoaccount.getMoAccountId());
			aMoaccount.setAccountType(theMoaccount.getAccountType());
			aMoaccount.setDescription(theMoaccount.getDescription());
			aMoaccount.setCoAccountIDAsset(theMoaccount.getCoAccountIDAsset());
			aMoaccount.setCoAccountIDDeposits(theMoaccount.getCoAccountIDDeposits());
			aMoaccount.setCoAccountIDInterest(theMoaccount.getCoAccountIDInterest());
			aMoaccount.setCoAccountIDFees(theMoaccount.getCoAccountIDFees());
			aMoaccount.setCompany1(theMoaccount.getCompany1());
			aMoaccount.setCompany2(theMoaccount.getCompany2());
			aMoaccount.setCompany3(theMoaccount.getCompany3());
			aMoaccount.setCompany4(theMoaccount.getCompany4());
			aMoaccount.setCompany5(theMoaccount.getCompany5());
			aMoaccount.setBank1(theMoaccount.getBank1());
			aMoaccount.setBank2(theMoaccount.getBank2());
			aMoaccount.setBank3(theMoaccount.getBank3());
			aMoaccount.setBank4(theMoaccount.getBank4());
			aMoaccount.setBank5(theMoaccount.getBank5());
			aMoaccount.setCheckCode(theMoaccount.getCheckCode());
			aMoaccount.setRoutingNumber(theMoaccount.getRoutingNumber());
			aMoaccount.setAccountNumber(theMoaccount.getAccountNumber());
			aMoaccount.setInActive(theMoaccount.getInActive());
			aMoaccount.setNextCheckNumber(theMoaccount.getNextCheckNumber());
			aMoaccount.setMoAccountId(theMoaccount.getMoAccountId());
			aMoaccount.setLogoYN(theMoaccount.getLogoYN());
			aMoaccount.setLineNo(theMoaccount.getLineNo());
			aSession.update(aMoaccount);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	/*Updated By:Velmurugan
	 *Updated On:28-08-2014
	 *Description:added the current amount in  previous balance value and set for each record 
	 * */
	@Override
	public List<Motransaction> getTransactionRegisterList(int theFrom, int theTo, ArrayList<?> theTransactionDetails,int moaccountid) throws BankingException {
		Session aSession = null;
		StringBuilder aStringBuilder = new StringBuilder("");
		if(moaccountid !=0 ){
			aStringBuilder.append("WHERE moAccountID ="+moaccountid+"");
		}
		if(theTransactionDetails != null){
			/*if(theTransactionDetails.get(0) !="" && theTransactionDetails.get(0) != null){
				aStringBuilder.append("WHERE moAccountID ="+theTransactionDetails.get(0)+"");
			}*/
			if(theTransactionDetails.get(1) != "" && theTransactionDetails.get(1) != null && theTransactionDetails.get(2) != "" && theTransactionDetails.get(2) != null){
				String aTranDateString = theTransactionDetails.get(1).toString();
				String aTranToDateString = theTransactionDetails.get(2).toString();
				String[] aTranSactionDate = aTranDateString.split("%2F");
				String[] aTranSactionToDate = aTranToDateString.split("%2F");
				String aFromTranDate = aTranSactionDate[2]+"-"+aTranSactionDate[0]+"-"+aTranSactionDate[1];
				String aToTranDate = aTranSactionToDate[2]+"-"+aTranSactionToDate[0]+"-"+aTranSactionToDate[1];
				aStringBuilder.append(" AND date(TransactionDate) >= '"+aFromTranDate+"' AND date(TransactionDate) <= '"+aToTranDate+"'");
			}
			if(theTransactionDetails.get(3) != "" && theTransactionDetails.get(3) != null){
				aStringBuilder.append(" AND moTransactionTypeID ="+theTransactionDetails.get(3)+"");
			}
			if(theTransactionDetails.get(4) != "" && theTransactionDetails.get(4) != null){
				aStringBuilder.append(" AND moTransactionTypeID ="+theTransactionDetails.get(4)+"");
			}
			if(theTransactionDetails.get(5) != "" && theTransactionDetails.get(5) != null){
				aStringBuilder.append(" AND moTransactionTypeID ="+theTransactionDetails.get(5)+"");
			}
			if(theTransactionDetails.get(6) != "" && theTransactionDetails.get(6) != null){
				aStringBuilder.append(" AND moTransactionTypeID ="+theTransactionDetails.get(6)+"");
			}
			if(theTransactionDetails.get(7) != "" && theTransactionDetails.get(7) != null){
				aStringBuilder.append(" AND moTransactionTypeID ="+theTransactionDetails.get(7)+"");
			}
			if(theTransactionDetails.get(8) != "" && theTransactionDetails.get(8) != null){
				aStringBuilder.append(" AND Reconciled ="+theTransactionDetails.get(8)+"");
			}
			if(theTransactionDetails.get(9) != "" && theTransactionDetails.get(9) != null){
				aStringBuilder.append(" AND Void ="+theTransactionDetails.get(9)+"");
			}
		}
		List<Motransaction> aQueryList = new ArrayList<Motransaction>();
		String aCustomerQry = "SELECT *,CASE WHEN moTransaction.rxMasterID IS NULL THEN description ELSE IF((moTransaction.Void = 1 OR pageflagVoid = 1) ,description,(SELECT NAME FROM rxMaster WHERE rxMasterID=moTransaction.rxMasterID )) END 'namedescription' FROM moTransaction "+aStringBuilder+" ORDER BY moTransactionID DESC LIMIT " + theFrom + ", " + theTo;
		Motransaction aMotransaction = null;
		BigDecimal balance=new BigDecimal("0.00");
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aMotransaction = new Motransaction();
				Object[] aObj = (Object[])aIterator.next();
				aMotransaction.setMoTransactionId((Integer) aObj[0]);
				aMotransaction.setRxMasterId((Integer) aObj[1]);
				aMotransaction.setRxAddressId((Integer) aObj[2]);
				aMotransaction.setCoAccountId((Integer) aObj[3]);
				aMotransaction.setMoAccountId((Integer) aObj[4]);
				if(aObj[5]!=null)
				aMotransaction.setTransDate((String) DateFormatUtils.format((Date)aObj[5], "MM/dd/yyyy"));
				aMotransaction.setMoTransactionTypeId((Short) aObj[6]);
				aMotransaction.setMoTypeId((Short) aObj[6]);
				aMotransaction.setCheckType((Short) aObj[7]);
				aMotransaction.setReference((String) aObj[8]);
				aMotransaction.setDescription((String) aObj[9]);
				aMotransaction.setVoid_((Byte) aObj[10]);
				aMotransaction.setReconciled((Byte) aObj[11]);
				aMotransaction.setTempRec((Byte) aObj[12]);
				aMotransaction.setPrinted((Byte) aObj[13]);
				aMotransaction.setAmount((BigDecimal) aObj[14]);
				aMotransaction.setWithDrawel((BigDecimal) aObj[14]);
				aMotransaction.setDirectDeposit((Byte) aObj[15]);
				/*if(aMotransaction.getMoTransactionTypeId()!=null){
				if(aMotransaction.getMoTransactionTypeId()==0){
					System.out.println("DepositAmount=="+aMotransaction.getAmount());
					balance=balance.add(aMotransaction.getAmount());
				}else{
					System.out.println("WithDrawel=="+aMotransaction.getAmount());
					balance=balance.add(aMotransaction.getAmount());
				}
				}*/
				//balance=balance.multiply(new BigDecimal(-1));
				//aMotransaction.setBalance(balance);
				
				aMotransaction.setBalance((BigDecimal) aObj[16]);
				aMotransaction.setStatus((Boolean)aObj[17]);
				aMotransaction.setMemo((String) aObj[18]);
				aMotransaction.setDisplaydiscription((String) aObj[21]);
				
				
				Date myDate = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateAsString_format = simpleDateFormat.format(myDate);
				String futureornot="Current Transaction";
				//System.out.println("Date"+(Date)aObj[5]);
				if((Date)aObj[5]!=null){
				if(myDate.compareTo((Date)aObj[5])<0 || myDate.compareTo((Date)aObj[5])==0){
					futureornot="Future Transaction";
				}else{
					futureornot="Current Transaction";
				}
				}
				aMotransaction.setFutureorcurrent(futureornot);
				aQueryList.add(aMotransaction);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aStringBuilder=null;
		}
		return aQueryList;	
	}

	@Override
	public List<Motransaction> getChecksLists(int moaccountId,String reference,short checkType) throws BankingException {
		Session aSession = null;
		StringBuilder aStringBuilder = new StringBuilder("");
		itsLogger.info("MoAccountId: "+moaccountId);
		itsLogger.info("Reference: "+reference);
		itsLogger.info("CheckType: "+checkType);
			if(moaccountId!=0){
				aStringBuilder.append(" and moAccountID ="+moaccountId+"");
			}
			if(checkType!=0){
				aStringBuilder.append(" AND CheckType = "+checkType);
			}
			if(reference!=null&&!reference.equals("")){
				aStringBuilder.append(" AND reference >= "+reference);
			}
		//aStringBuilder.append(" ORDER BY TRIM(Reference)");
		List<Motransaction> aQueryList = new ArrayList<Motransaction>();
		String aCustomerQry = "SELECT * FROM moTransaction WHERE Reconciled = 0 "+aStringBuilder;
		Motransaction aMotransaction = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aMotransaction = new Motransaction();
				Object[] aObj = (Object[])aIterator.next();
				aMotransaction.setMoTransactionId((Integer) aObj[0]);
				aMotransaction.setRxMasterId((Integer) aObj[1]);
				aMotransaction.setRxAddressId((Integer) aObj[2]);
				aMotransaction.setCoAccountId((Integer) aObj[3]);
				aMotransaction.setMoAccountId((Integer) aObj[4]);
				if(aObj[5]!=null)
				aMotransaction.setTransDate((String) DateFormatUtils.format((Date)aObj[5], "MM/dd/yyyy"));
				aMotransaction.setMoTransactionTypeId((Short) aObj[6]);
				aMotransaction.setMoTypeId((Short) aObj[6]);
				aMotransaction.setCheckType((Short) aObj[7]);
				aMotransaction.setReference((String) aObj[8]);
				aMotransaction.setDescription((String) aObj[9]);
				aMotransaction.setVoid_((Byte) aObj[10]);
				aMotransaction.setReconciled((Byte) aObj[11]);
				aMotransaction.setTempRec((Byte) aObj[12]);
				aMotransaction.setPrinted((Byte) aObj[13]);
				aMotransaction.setAmount((BigDecimal) aObj[14]);
				aMotransaction.setWithDrawel((BigDecimal) aObj[14]);
				aMotransaction.setDirectDeposit((Byte) aObj[15]);
				aQueryList.add(aMotransaction);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aStringBuilder=null;
		}
		return aQueryList;	
	}
	
	@Override
	public List<Prwarehouse> getWhseList(Integer whseID) throws BankingException {
		Session aSession = null;
		itsLogger.info("whseID: "+whseID);
		List<Prwarehouse> aQueryList = new ArrayList<Prwarehouse>();
		String aCustomerQry = "SELECT * FROM prWarehouse WHERE prWarehouseID <> "+whseID;
		Prwarehouse aPrwarehouse = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPrwarehouse = new Prwarehouse();
				Object[] aObj = (Object[])aIterator.next();
				aPrwarehouse.setPrWarehouseId((Integer) aObj[0]);
				aPrwarehouse.setSearchName((String) aObj[1]);
				aPrwarehouse.setDescription((String) aObj[2]);
				aPrwarehouse.setAddress1((String) aObj[4]);
				aPrwarehouse.setAddress2((String) aObj[5]);
				aPrwarehouse.setCity((String)aObj[6]);
				aPrwarehouse.setState((String) aObj[7]);
				aPrwarehouse.setZip((String) aObj[8]);
				aQueryList.add(aPrwarehouse);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;	
	}
	
	@Override
	public BigInteger rePrintChecksInsert(Motransaction motrans,BigInteger startId,BigInteger endId, BigInteger chekcNo,Integer yearID,Integer periodID,String username) throws BankingException {
		itsLogger.debug("moTransaction Update");
		
		Session aSession = null;
		
		/*
		 * Description: MoAccountID is Added to check#. 
		 * Table:glLinkage
		 * Reason: fetching Purpose
		 */
		BigInteger startId1=new BigInteger(motrans.getMoAccountId()+""+startId);
		BigInteger endId1=new BigInteger(motrans.getMoAccountId()+""+endId);
		
		GlTransaction aGlTransaction = null;
		GlTransaction bGlTransaction = null;
		GlTransaction theGlTransaction = null;
		String updateQuery = null;
		//int newCheckValue = chekcNo.intValue();
		BigInteger referenceValue =chekcNo;
		int k=0;
		try{
			Coledgersource aColedgersource = new Coledgersource();
			
			Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
			Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);			
			aColedgersource = gltransactionService.getColedgersourceDetail("WC");
			
			updateQuery = "SELECT distinct(glTransactionId),vebillID FROM glLinkage WHERE STATUS=0 AND coLedgerSourceId = "+aColedgersource.getCoLedgerSourceId()+" and vebillID  BETWEEN  "+startId1+" AND "+endId1;
			itsLogger.info("Query : "+updateQuery);
			
			itsLogger.info("MoAccountID:---->"+motrans.getMoAccountId());
			
			aSession = itsSessionFactory.openSession();
			Query insQuery = aSession.createSQLQuery(updateQuery);
			Iterator<?> theIterator = insQuery.list().iterator();
			Transaction aSearchInsert = aSession.beginTransaction();
			ArrayList<GlTransaction> glTransactionList = new ArrayList<GlTransaction>();
			while (theIterator.hasNext()) {
				k++;
				aGlTransaction = new GlTransaction();
				theGlTransaction = new GlTransaction();
				Object[] aObj = (Object[])theIterator.next();
				aGlTransaction = (GlTransaction)aSession.get(GlTransaction.class,((Integer) aObj[0]));
				
				bGlTransaction = new GlTransaction();
				bGlTransaction.setBankClosingBalance(aGlTransaction.getBankClosingBalance());
				bGlTransaction.setBankOpeningBalance(aGlTransaction.getBankOpeningBalance());
				bGlTransaction.setCoAccountDesc(aGlTransaction.getCoAccountDesc());
				bGlTransaction.setCoAccountId(aGlTransaction.getCoAccountId());
				bGlTransaction.setCoAccountNumber(aGlTransaction.getCoAccountNumber());
				
				bGlTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
				bGlTransaction.setpStartDate(aCofiscalperiod.getStartDate());
				bGlTransaction.setpEndDate(aCofiscalperiod.getEndDate());
				bGlTransaction.setPeriod(aCofiscalperiod.getPeriod());
				
				bGlTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
				bGlTransaction.setyStartDate(aCofiscalyear.getStartDate());
				bGlTransaction.setyStartDate(aCofiscalyear.getEndDate());
				bGlTransaction.setFyear(aCofiscalyear.getFiscalYear());
				
				bGlTransaction.setEntrydate(new Date());
				bGlTransaction.setEnteredBy(username);
				bGlTransaction.setTransactionDate(motrans.getTransactionDate());
				
				bGlTransaction.setHiddenstatus(aGlTransaction.getHiddenstatus());
				bGlTransaction.setJournalDesc(aGlTransaction.getJournalDesc());
				bGlTransaction.setJournalId(aGlTransaction.getJournalId());
				bGlTransaction.setOldcoAccountId(aGlTransaction.getOldcoAccountId());
				bGlTransaction.setOldcoAccountNumber(aGlTransaction.getOldcoAccountNumber());
				bGlTransaction.setStatus(aGlTransaction.getStatus());
				bGlTransaction.setTransactionDesc(aGlTransaction.getTransactionDesc());
				bGlTransaction.setPoNumber(referenceValue+"");
				bGlTransaction.setDebit(aGlTransaction.getDebit());
				bGlTransaction.setCredit(aGlTransaction.getCredit());
				glTransactionList.add(bGlTransaction);
				
				if(k%2==0){
				referenceValue =referenceValue.add(BigInteger.ONE);
				
				GlRollback glRollback = new GlRollback();
				glRollback.setVeBillID((Integer) aObj[1]);
				glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
				glRollback.setPeriodID(periodID);
				glRollback.setYearID(yearID);
				glRollback.setTransactionDate(motrans.getTransactionDate());
				
				gltransactionService.rollBackGlTransaction(glRollback);
				}
				itsLogger.info("Reference Val: "+referenceValue);
			}
			for(int a=0;a<glTransactionList.size();a++){
				GlTransaction cGlTransaction = new GlTransaction();
				cGlTransaction = glTransactionList.get(a);
				Integer glID=0; 
				glID =(Integer) aSession.save(cGlTransaction);
				glLinkageInsert(aColedgersource,glID, Integer.parseInt(cGlTransaction.getPoNumber()),motrans.getMoAccountId());
				
				
			}
					
			aSearchInsert.commit();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);	
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		}finally {
			aSession.flush();
			aSession.close();
			updateQuery=null;
		}
		
		
		
		String aCustomerQry = "SELECT * FROM moTransaction WHERE  CAST(reference AS UNSIGNED) BETWEEN "+startId+" AND "+endId +" AND moAccountID="+motrans.getMoAccountId();
		itsLogger.info("Query : "+aCustomerQry);
		Motransaction aMotransaction = null;
		Motransaction aMotransactionUpdate = null;
		MoAccount aMoAccount=null;
		Query theQuery =null;
		BigInteger newCheckValue =chekcNo;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			Transaction aSearchInsert = aSession.beginTransaction();
			while (aIterator.hasNext()) {
				aMotransaction = new Motransaction();
				Object[] aObj = (Object[])aIterator.next();
				aMotransaction.setMoTransactionId((Integer) aObj[0]);
				aMotransactionUpdate = (Motransaction)aSession.get(Motransaction.class, aMotransaction.getMoTransactionId());
				theQuery = aSession.createQuery("From VeBillPaymentHistory where checkNo='"+aMotransactionUpdate.getReference()+"'");
				List aList = theQuery.list();
				for(int n=0;n<aList.size();n++){
					VeBillPaymentHistory objph=(VeBillPaymentHistory)aList.get(n);
					VeBillPaymentHistory objvbillph=(VeBillPaymentHistory)aSession.get(VeBillPaymentHistory.class, objph.getPaymentHistoryID());
					objvbillph.setCheckNo(newCheckValue+"");
					aSession.save(objvbillph);
				}
				aMotransactionUpdate.setReference(newCheckValue+"");
				aSession.update(aMotransactionUpdate);
				newCheckValue = newCheckValue.add(BigInteger.ONE);
				itsLogger.info("New Check Val: "+newCheckValue);
				aMoAccount = new MoAccount();
				aMoAccount = (MoAccount)aSession.get(MoAccount.class, motrans.getMoAccountId());
				aMoAccount.setNextCheckNumber(newCheckValue.intValue());
				aSession.update(aMotransactionUpdate);
				
			}
			aSearchInsert.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		}
		
	
		
		/*Session bSession = null;
		String InsertQry = "INSERT INTO motransaction " +
				"(rxMasterID,rxAddressID,coAccountID,moAccountId,TransactionDate," +
				"moTransactionTypeID,CheckType,reference,Description,void,reconciled," +
				"temprec,printed,amount,directdeposit) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Motransaction aSearchList = new Motransaction();
		try {
			
			bSession = itsSessionFactory.openSession();
			Transaction aSearchInsert = bSession.beginTransaction();
			Query aQuery = bSession.createSQLQuery(InsertQry);
			for (int i = 0; i < aQueryList.size(); i++) {
				aSearchList = (Motransaction) aQueryList.get(i);
				aQuery.setInteger(1, aSearchList.getRxMasterId());
				aQuery.setInteger(2, aSearchList.getRxAddressId());
				aQuery.setInteger(3, aSearchList.getCoAccountId());
				aQuery.setInteger(4, aSearchList.getMoAccountId());
				aQuery.setDate(5, aSearchList.getTransactionDate());
				aQuery.setInteger(6, aSearchList.getMoTransactionTypeId());
				aQuery.setInteger(7, aSearchList.getCheckType());
				aQuery.setString(8, newCheckValue+"");
				aQuery.setString(9, aSearchList.getDescription());
				aQuery.setInteger(10, aSearchList.getVoid_());
				aQuery.setInteger(11, aSearchList.getReconciled());
				aQuery.setInteger(12, aSearchList.getTempRec());
				aQuery.setInteger(13, aSearchList.getPrinted());
				aQuery.setBigDecimal(14, aSearchList.getAmount());
				aQuery.setInteger(15, aSearchList.getDirectDeposit());
				
				bSession.save(aQuery);
				if (i % 100 == 0) {
					aSession.flush();
					aSession.clear();
					Session aTransdetailSession = itsSessionFactory.openSession();
					Transaction aTransaction;
					try {
						aTransaction = aTransdetailSession.beginTransaction();
						aTransaction.begin();
						aTransdetailSession.delete(aSearchList);
						aTransaction.commit();
					} catch (Exception excep) {
						itsLogger.error(excep.getMessage(), excep);
						BankingException aBankingException = new BankingException(excep.getMessage(), excep);
						throw aBankingException;
					} finally {
						aTransdetailSession.flush();
						aTransdetailSession.close();
					}
				}
				
			}
			aSearchInsert.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		}*/
		 finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return newCheckValue;
	}
	@Override
	public BigInteger getMaxCheckList(Integer moAccountId) throws BankingException {
	//	String aVendorSelectQry = "SELECT  MAX(CAST(IF(LENGTH(reference)>7,SUBSTRING(reference, -5),reference) AS UNSIGNED))+1 AS newreference FROM moTransaction WHERE moAccountID="+moAccountId;
		String aVendorSelectQry = "SELECT NextCheckNumber FROM moAccount WHERE moAccountID="+moAccountId;
		Session aSession = itsSessionFactory.openSession();
		Integer newReferenceNo = null;
		try{
			List<?> aList = aSession.createSQLQuery(aVendorSelectQry).list();
			newReferenceNo = (Integer)aList.get(0);
		} catch (Exception e){
			itsLogger.error(e.getMessage(), e);
			throw new BankingException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return BigInteger.valueOf(newReferenceNo.intValue());
	}
	
	/*updated by:Velmurugan
	  *updated on:31-08-2014
	  *Description: when adding a new transaction one row has been inserted in motransaction table
	  *while edit the transaction two rows has been inserter one is for nullify the old record and 
	  *another one is for new edit amount has been inserted in table.
	  *while delete the transaction one row has been inserted for nullify the amount .
	  *deposit -withdrawel insert
	  *withdrawel - deposit insert
	  * */
	
	@Override
	public int addTransactionDetails(Motransaction theMotransaction, Session aTransactionSession,String insertStatus) throws BankingException {
		
		Transaction aTransaction;
		int motransid=0;
		MoAccount aMoaccount=new MoAccount();
		Motransaction amot= new Motransaction();
		try {
			
			if(insertStatus.equals("rollback"))
			{
				
				amot = (Motransaction)aTransactionSession.get(Motransaction.class,theMotransaction.getMoTransactionId());
				theMotransaction.setRxMasterId(amot.getRxMasterId());
				theMotransaction.setDescription(amot.getDescription());
				theMotransaction.setReference(amot.getReference());
				motransid = (Integer) aTransactionSession.save(theMotransaction);
				
			}
			else if(insertStatus.equals("rollbackfromvoid"))
			{
				amot = (Motransaction)aTransactionSession.get(Motransaction.class,theMotransaction.getMoTransactionId());
				theMotransaction.setRxMasterId(amot.getRxMasterId());
				theMotransaction.setReference(amot.getReference());
				motransid = (Integer) aTransactionSession.save(theMotransaction);
			}
			else
			{
				if(theMotransaction.getMoTransactionTypeId()==2)
				{
						aMoaccount = (MoAccount)aTransactionSession.get(MoAccount.class,theMotransaction.getMoAccountId());
					
						/*if(aMoaccount.getNextCheckNumber()!=Integer.parseInt(theMotransaction.getReference()))	
						{
						aMoaccount.setNextCheckNumber(Integer.parseInt(theMotransaction.getReference())+1);	
						theMotransaction.setReference(""+Integer.parseInt(theMotransaction.getReference()));
						}
						else
						{
						aMoaccount.setNextCheckNumber(Integer.parseInt(theMotransaction.getReference())+1);
						theMotransaction.setReference(""+Integer.parseInt(theMotransaction.getReference()));
						}*/
					
						aMoaccount.setNextCheckNumber(Integer.parseInt(theMotransaction.getReference())+1);	
						theMotransaction.setReference(""+Integer.parseInt(theMotransaction.getReference()));
						
						motransid = (Integer) aTransactionSession.save(theMotransaction);					
					
						BigDecimal substractions=  aMoaccount.getSubtractions();
						substractions = substractions.add(theMotransaction.getAmount()); 
						aMoaccount.setSubtractions(substractions);
						aTransactionSession.update(aMoaccount);
				}
				else
				{
					motransid = (Integer) aTransactionSession.save(theMotransaction);
				}
			}
			
		
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} 
		finally
		{   
			aTransactionSession.flush();
			aTransactionSession.clear();
		}
		return motransid;
	}
	
	
	@Override
	public boolean updateTransactionDetails(Motransaction theMotransaction) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		Motransaction aMotransaction = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aMotransaction = (Motransaction) aSession.get(Motransaction.class, theMotransaction.getMoTransactionId());
			//Old 
			/*aMotransaction.setTransactionDate(theMotransaction.getTransactionDate());
			aMotransaction.setMoTransactionTypeId(theMotransaction.getMoTransactionTypeId());
			aMotransaction.setReference(theMotransaction.getReference());
			aMotransaction.setDescription(theMotransaction.getDescription());
			aMotransaction.setCoAccountId(theMotransaction.getCoAccountId());
			aMotransaction.setReconciled(theMotransaction.getReconciled());
			System.out.println("updateTransactionDetails==>"+theMotransaction.getAmount());
			aMotransaction.setAmount(theMotransaction.getAmount());
			aMotransaction.setMoAccountId(theMotransaction.getMoAccountId());
			aMotransaction.setMoTransactionId(theMotransaction.getMoTransactionId());
			aMotransaction.setBalance(theMotransaction.getBalance());*/
			
			//new
			aMotransaction.setStatus(theMotransaction.getStatus());
			aSession.update(aMotransaction);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateTransactionDetails1(Motransaction theMotransaction,Session aSession) throws BankingException {
		Motransaction aMotransaction = null;
		try {
			aMotransaction = (Motransaction) aSession.get(Motransaction.class, theMotransaction.getMoTransactionId());
			aMotransaction.setStatus(theMotransaction.getStatus());
			aSession.update(aMotransaction);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.clear();
		}
		return true;
	}

	@Override
	public boolean deleteTransactionDetails(Motransaction theMotransaction) throws BankingException {
		Session aTransdetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aTransdetailSession.beginTransaction();
			aTransaction.begin();
			aTransdetailSession.delete(theMotransaction);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aTransdetailSession.flush();
			aTransdetailSession.close();
		}
		return false;
	}

	@Override
	public boolean voidTransactionDetails(Motransaction theMotransaction) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		Motransaction aMotransaction = null;
		Transaction aTransaction;
		Integer veBillId = 0;
		Vebill aVeBill =null;
		boolean returnStatus = true;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			aMotransaction = (Motransaction) aSession.get(Motransaction.class, theMotransaction.getMoTransactionId());
			aMotransaction.setDescription(theMotransaction.getDescription()+"[VOID]");
			aMotransaction.setVoid_(theMotransaction.getVoid_());
			aMotransaction.setMoAccountId(theMotransaction.getMoAccountId());
			aMotransaction.setStatus(theMotransaction.getStatus());
			aSession.update(aMotransaction);
			/**
			 * Created by: Leo Date: Apr 22,2015.
			 * ID: 294 
			 * Description: update veBill and veBillPaymentHistory
			 * */
		
			veBillId =(Integer) aSession.createSQLQuery("SELECT veBillID FROM moLinkageDetail WHERE moTransactionID ="+theMotransaction.getMoTransactionId()).uniqueResult();
			if(veBillId!=null && veBillId !=0 )
			{
			aVeBill = (Vebill) aSession.get(Vebill.class, veBillId);
			aVeBill.setAppliedAmount(aVeBill.getAppliedAmount().subtract(theMotransaction.getAmount()));
			aVeBill.setTransactionStatus(1);
			aSession.update(aVeBill);
			aSession.createSQLQuery("Delete FROM `veBillPaymentHistory` WHERE veBillID ="+veBillId).executeUpdate();
			returnStatus = true;
			}
			else
			{
			returnStatus = false;	
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return returnStatus;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean voidTransactionDetails1(Motransaction theMotransaction,Session aSession) throws BankingException {
		Motransaction aMotransaction = null;
		List<Integer> veBillId = null;
		Vebill aVeBill =null;
		VeBillPaymentHistory veBillHistory = null;
		List<Molinkagedetail> theMolinkagedetail = null;
		List<Motransaction> themotranlist = null;
		String checkNo = "";
		
		boolean returnStatus = false;
		try {
			aMotransaction = (Motransaction) aSession.get(Motransaction.class, theMotransaction.getMoTransactionId());
			aMotransaction.setDescription(theMotransaction.getDescription()+"[VOID]");
			aMotransaction.setVoid_(theMotransaction.getVoid_());
			aMotransaction.setMoAccountId(theMotransaction.getMoAccountId());
			aMotransaction.setStatus(theMotransaction.getStatus());
			checkNo = theMotransaction.getReference();
			aSession.update(aMotransaction);
			/**
			 * Created by: Leo Date: Apr 22,2015.
			 * ID: 294 
			 * Description: update veBill and veBillPaymentHistory
			 * */
		
			//veBillId =(List<Integer>) aSession.createSQLQuery("SELECT veBillID FROM moLinkageDetail WHERE moTransactionID ="+theMotransaction.getMoTransactionId()).list();
			
			themotranlist=(List<Motransaction>) aSession.createSQLQuery("SELECT moTransactionID FROM moTransaction WHERE uniquechkRef ="+checkNo).list();
			
			Iterator<?> amotIterator = themotranlist.iterator();
			while(amotIterator.hasNext())
			{
				Integer amotObj = (Integer) amotIterator.next();	
				// update all related transaction as void
				aSession.createSQLQuery("update moTransaction set Void = 1 where moTransactionID = "+amotObj).executeUpdate();
					
				theMolinkagedetail = (List<Molinkagedetail>) aSession.createSQLQuery("Select veBillID,Amount,Discount from moLinkageDetail where moTransactionID = "+amotObj).list();
				if(theMolinkagedetail.size() > 0 )
				{
					Iterator<?> aIterator = theMolinkagedetail.iterator();
					while(aIterator.hasNext())
					{
					Object[] aObj = (Object[])aIterator.next();
					aVeBill = (Vebill) aSession.get(Vebill.class, (Integer) aObj[0]);
					aVeBill.setAppliedAmount(aVeBill.getAppliedAmount().subtract(((BigDecimal) aObj[1]).add((BigDecimal) aObj[2])));
					aVeBill.setTransactionStatus(1);
					aSession.update(aVeBill);
					aSession.createSQLQuery("Delete FROM `veBillPaymentHistory` WHERE veBillID ="+(Integer) aObj[0]).executeUpdate();
					returnStatus = true;
					}
				}
				else
				{
				returnStatus = false;	
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.clear();
		}
		return returnStatus;
	}


	
	
	@SuppressWarnings("unchecked")
	@Override
	public PrintCheckBean printCheck(Motransaction theMotransaction,Integer checkNumber,Integer yearID,Integer periodID,String userName,Integer userID) throws BankingException {
		PrintCheckBean aPrintBean = new PrintCheckBean();
		Session aSession  = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		String deletetemCheck=null,billArrayQuery=null, rxAddressIDQuery=null, vendorPayBeanQuery=null;
		StringBuffer totalAmountQuery=null;
		Molinkage aMolinkage = new Molinkage();
		Integer moLinkageId=null;
		Integer moTransactionId = null;
		Integer moLinkageDetailID = null;
		Integer[] rxMasterID =null;
		ArrayList<Integer> rxMasterID1 =new ArrayList<Integer>();
		ArrayList<BigDecimal> creditAmtUsed =new ArrayList<BigDecimal>();
		BigDecimal[] totalAmount=new BigDecimal[0];
		BigDecimal[] totalAmount1=null;
		BigDecimal balAmount=new BigDecimal(0);
		BigDecimal balcalculationAmount=new BigDecimal(0);
		MoAccount aMoaccount = new MoAccount();
		checkDate = theMotransaction.getTransactionDate();
		int i =0;
		int j =0;
		try{
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			deletetemCheck = "DELETE FROM moVendorCheckTemp";
			aSession.createSQLQuery(deletetemCheck).executeUpdate();
			
			billArrayQuery = "select veBillID from veBillPay where moAccountId="+theMotransaction.getMoAccountId()+" and userID ="+userID;
			Query aQuery = aSession.createSQLQuery(billArrayQuery);
			List<?> billIds= aQuery.list();
			billId = new Integer[ aQuery.list().size()];
			for(i=0;i<billIds.size();i++)
				billId[i] = (Integer)billIds.get(i);
			String rxMasterList = "SELECT BillPayUniqueIDs.rxMasterID FROM (SELECT rxMasterID FROM (SELECT veBill.rxMasterID FROM veBill LEFT JOIN veBillPay ON veBill.veBillID = veBillPay.veBillID WHERE veBillPay.ApplyingAmount<>0 and veBillPay.moAccountId="+theMotransaction.getMoAccountId()+" and veBillPay.userID = "+userID+") AS BillPayIDs GROUP BY rxMasterID) AS BillPayUniqueIDs "
									+ "INNER JOIN rxMaster ON BillPayUniqueIDs.rxMasterID = rxMaster.rxMasterID ORDER BY Name;";
			Query aQuery1 = aSession.createSQLQuery(rxMasterList);
			itsLogger.info(aQuery1.toString());
			List<?> rxMasterLists = aQuery1.list();
			rxMasterID = new Integer[ aQuery1.list().size()];
			for(i=0;i<rxMasterLists.size();i++){
				rxMasterID[i]=(Integer)rxMasterLists.get(i);
			}
			
			int counting = 0;
			totalAmount1 = new BigDecimal[rxMasterID.length];
			for(i=0;i<rxMasterID.length;i++){
				
				totalAmountQuery = new StringBuffer("SELECT SUM(P.ApplyingAmount) AS CheckAmount FROM veBillPay AS P INNER JOIN veBill AS B ON P.veBillID = B.veBillID Where P.moAccountId="+theMotransaction.getMoAccountId()+" and P.userID = "+userID+" and (B.rxMasterID =").append(rxMasterID[i]).append(")");
				Query aQuery2 = aSession.createSQLQuery(totalAmountQuery.toString());
				List<?> totalList= aQuery2.list();
					for( j =0; j<totalList.size();j++){
						if((BigDecimal)totalList.get(j)!=null)
						{
							BigDecimal amt=(BigDecimal)totalList.get(j);
							
							if(amt.signum()<0)
							{
								rxMasterID1.add(rxMasterID[i]);
								creditAmtUsed.add((BigDecimal)totalList.get(j));
							}
							else
							{ 
								totalAmount1[counting] = (BigDecimal)totalList.get(j);
								counting++;
							}
						}
					}
				}
			
			for (int t=0;t<rxMasterID1.size();t++){
				rxMasterID = (Integer[]) ArrayUtils.removeElement(rxMasterID, rxMasterID1.get(t));
				itsLogger.info("rxMasterID::"+rxMasterID1.get(t));
			}
			 
			if(rxMasterID.length>0)
			{
				//Get rxAddress Information from normal payment
			rxAddressID = new Integer[rxMasterID.length];
			for(i=0;i<rxMasterID.length;i++){
				rxAddressIDQuery = "SELECT rxAddressID FROM rxAddress WHERE rxMasterID= " +rxMasterID[i]+ " and IsRemitTo = 1";
				Query aQuery3 = aSession.createSQLQuery(rxAddressIDQuery);
				List<?> rxAddressList = aQuery3.list();
				
				 if(rxAddressList.size()>0)
				 {
					 rxAddressID[i] = (Integer)rxAddressList.get(0);
				 }
				 else
				 {
					rxAddressIDQuery = "SELECT rxAddressID FROM rxAddress WHERE rxMasterID= " +rxMasterID[i]+ " limit 1";
					aQuery3 = aSession.createSQLQuery(rxAddressIDQuery);
					rxAddressList = aQuery3.list();
					rxAddressID[i] = (Integer)rxAddressList.get(0);
				 }
				 
				}
			}
			else
			{
				//Get rxAddress Information from credit payment
				rxAddressID = new Integer[rxMasterID1.size()];
				for(i=0;i<rxMasterID1.size();i++){
					rxAddressIDQuery = "SELECT rxAddressID FROM rxAddress WHERE rxMasterID= " +rxMasterID1.get(i)+ " limit 1";
					Query aQuery3 = aSession.createSQLQuery(rxAddressIDQuery);
					itsLogger.info("Query2 "+aQuery3.toString());
					 List<?> rxAddressList = aQuery3.list();
						for( j =0; j<rxAddressList.size();j++){
							if((Integer)rxAddressList.get(j)!=null)
							rxAddressID[i] = (Integer)rxAddressList.get(j);
						}
					}
			}
			
			aMolinkage.setDummyVal(true);
			moLinkageId = (Integer)aSession.save(aMolinkage);
			int chkno=0;
			chkno=checkNumber;
			if(counting!=0) // Normal Payment 
			{
				totalAmount = new BigDecimal[counting];
				for(int t=0;t<counting;t++)
				{
					totalAmount[t] = totalAmount1[t];
				}
				for(i=0;i<rxMasterID.length;i++){
					
				List<VendorPayBean> payBeanlist = new ArrayList<VendorPayBean>();
				
				vendorPayBeanQuery = "SELECT veBillPay.veBillPayID, veBillPay.veBillID, veBillPay.ApplyingAmount, veBillPay.DiscountAmount,"
						+ "veBill.BillDate,veBill.InvoiceNumber,veBill.BillAmount,veBill.AppliedAmount,vePO.PONumber FROM veBill "
						+ "LEFT JOIN veBillPay ON veBill.veBillID = veBillPay.veBillID  LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID "
						+ "WHERE veBillPay.ApplyingAmount<>0 AND veBillPay.moAccountId="+theMotransaction.getMoAccountId()+" AND veBillPay.userID = "+userID+" AND  veBill.rxMasterID= "+ rxMasterID[i] +" ORDER BY veBill.InvoiceNumber, veBill.BillDate";
						
				Query aQuery4 = aSession.createSQLQuery(vendorPayBeanQuery);
				payBeanlist = aQuery4.list();
				
				if(payBeanlist.size()<=25) // pick invoice count less than 25 for particular vendor
				{
				balAmount = new BigDecimal(0);
				ArrayList<VendorPayBean> payBean = new ArrayList<VendorPayBean>();
				balAmount = balAmount.add(totalAmount[i].negate());
				Motransaction aMotransaction = new Motransaction();
				aMotransaction.setRxMasterId(rxMasterID[i]);
				aMotransaction.setRxAddressId(rxAddressID[i]);				
				aMotransaction.setCheckType(theMotransaction.getCheckType());
				aMotransaction.setTransactionDate(theMotransaction.getTransactionDate());
				aMotransaction.setMoAccountId(theMotransaction.getMoAccountId());
				aMotransaction.setPrinted(theMotransaction.getPrinted());
				aMotransaction.setAmount(totalAmount[i].negate());
				balcalculationAmount = balcalculationAmount.add(balAmount); // for each Amount
				aMotransaction.setBalance(theMotransaction.getBalance().add(balcalculationAmount)); 
				aMotransaction.setCoAccountId(theMotransaction.getCoAccountId());
				aMotransaction.setReference(""+chkno);
				aMotransaction.setUniquechkRef(""+chkno);
				aMotransaction.setMoTransactionTypeId(theMotransaction.getMoTransactionTypeId());
				
				aMotransaction.setDescription("Vendor Write Checks");
				aMotransaction.setVoid_((byte)0);
				aMotransaction.setReconciled((byte)0);
				aMotransaction.setDirectDeposit((byte)0);
				aMotransaction.setTempRec((byte)0);
				aMotransaction.setPageflagVoid(false);
				moTransactionId =(Integer)aSession.save(aMotransaction);
				
				
				Iterator<?> aIterator  = aQuery4.list().iterator();
				while(aIterator.hasNext()){
				Object[] aObj = (Object[])aIterator.next();
					VendorPayBean aPayBean = new VendorPayBean();
					aPayBean.setVebillpayId((Integer)aObj[0]);
					aPayBean.setVebillID((Integer)aObj[1]);
					aPayBean.setApplyingAmount((BigDecimal)aObj[2]);
					aPayBean.setDiscountAmount((BigDecimal)aObj[3]);
					payBean.add(aPayBean);
					
					WritecheckDetails wcObj = new WritecheckDetails();
					wcObj.setWcDetailsID(1);
					wcObj.setMoAccountID(theMotransaction.getMoAccountId());
					wcObj.setMoTransactionId(moTransactionId);
					wcObj.setMoTransactionDate(theMotransaction.getTransactionDate());
					wcObj.setMoTransAmount(totalAmount[i].negate());
					wcObj.setMoLinkappliedAmount((BigDecimal)aObj[2]);
					wcObj.setMoLinkdiscount((BigDecimal)aObj[3]);
					wcObj.setCheckno(""+chkno);
					wcObj.setUserID(userID);
					wcObj.setRxMasterID(rxMasterID[i]);
					wcObj.setRxAddressID(rxAddressID[i]);
					wcObj.setVeBillID((Integer)aObj[1]);
					wcObj.setVeBillDate((Date)aObj[4]);
					wcObj.setInvoiceNumber((String)aObj[5]);
					wcObj.setVeBillAmount((BigDecimal)aObj[6]);
					wcObj.setVeBillAppliedAmt((BigDecimal)aObj[7]);
					wcObj.setPoNumber((String)aObj[8]);
					wcObj.setVoidStatus(0);
					wcObj.setVeBillBalance( ((BigDecimal)aObj[6]!=null?(BigDecimal)aObj[6]:BigDecimal.ZERO).subtract(((BigDecimal)aObj[2]!=null?(BigDecimal)aObj[2]:BigDecimal.ZERO).add((BigDecimal)aObj[3]!=null?(BigDecimal)aObj[3]:BigDecimal.ZERO)));
					aSession.save(wcObj);
					
				}
				/*String billtoRxmasterQuery = "SELECT vb.veBillID FROM veBillPay as vp INNER JOIN veBill as vb on vp.veBillID = vb.veBillID WHERE  vb.rxMasterID ="+rxMasterID[i]+";";
				List<?> billList = aSession.createSQLQuery(billtoRxmasterQuery).list();*/
				for(j=0;j<payBean.size();j++){
					
					Molinkagedetail aMolinkagedetail = new Molinkagedetail();
					aMolinkagedetail.setVeBillId(payBean.get(j).getVebillID());
					aMolinkagedetail.setAmount(payBean.get(j).getApplyingAmount());
					aMolinkagedetail.setMoLinkageId(moLinkageId);
					aMolinkagedetail.setDiscount(payBean.get(j).getDiscountAmount());
					aMolinkagedetail.setMoTransactionId(moTransactionId);
					moLinkageDetailID = (Integer)aSession.save(aMolinkagedetail);
					
					Vebill aVebill = new Vebill();
					aVebill = (Vebill)aSession.get(Vebill.class,payBean.get(j).getVebillID());
					BigDecimal appliedAmount = aVebill.getAppliedAmount();
					BigDecimal billAmount = aVebill.getBillAmount();
					appliedAmount = appliedAmount.add(payBean.get(j).getApplyingAmount()).add(payBean.get(j).getDiscountAmount());
					aVebill.setAppliedAmount(appliedAmount);
					aVebill.setCreditUsed("0");
					
					if(appliedAmount.compareTo(billAmount)==0)
						aVebill.setTransactionStatus(2);
					else
						aVebill.setTransactionStatus(1);
					
					aSession.update(aVebill);
					
					/**Created by: Leo  ID:110 
					 * Date:04/07/2015
					 * Purpose: To store payment history */
					
					if(payBean.get(j).getApplyingAmount().signum()>0)
					{
					VeBillPaymentHistory aVeBillPaymentHistory = new VeBillPaymentHistory();
					aVeBillPaymentHistory.setVeBillID(payBean.get(j).getVebillID());
					aVeBillPaymentHistory.setCheckNo(""+chkno);
					aVeBillPaymentHistory.setDatePaid(theMotransaction.getTransactionDate());
					aVeBillPaymentHistory.setAmountVal(payBean.get(j).getApplyingAmount());
					aSession.save(aVeBillPaymentHistory);
					}
					
					//added by prasant 
					if(payBean.get(j).getApplyingAmount().signum()<0)
					{
						
						

						System.out.println("=======================================================================================================");
		                System.out.println("Normal  Payment To store payment history when amount is credit chk:"+chkno+"Amount"+payBean.get(j).getApplyingAmount());
						System.out.println("=======================================================================================================");
						
					VeBillPaymentHistory aVeBillPaymentHistory = new VeBillPaymentHistory();
					aVeBillPaymentHistory.setVeBillID(payBean.get(j).getVebillID());
					aVeBillPaymentHistory.setCheckNo(""+chkno);
					aVeBillPaymentHistory.setDatePaid(theMotransaction.getTransactionDate());
					aVeBillPaymentHistory.setAmountVal(payBean.get(j).getApplyingAmount());
					aSession.save(aVeBillPaymentHistory);
					}
					
					//end
					
					
					
				}
				Movendorchecktemp amoVendorCheckTemp = new Movendorchecktemp();
				amoVendorCheckTemp.setMoTransactionId(moTransactionId);
				aSession.save(amoVendorCheckTemp);
				chkno++;
				
				}
				else
				{
					int veBilllistCount = payBeanlist.size()/25; // "segment spliting" for pdf and generate new check no for each page
					
					if((payBeanlist.size()%25)>0) // add 1 segment if remainder comes
						veBilllistCount += 1;
					
					int valSublist = 0;
					for(int cb=1;cb <= veBilllistCount ;cb++)
					{
					valSublist = (cb-1)*25; 	// calculation part for picking data from arralist.
					balAmount = new BigDecimal(0);
					ArrayList<VendorPayBean> payBean = new ArrayList<VendorPayBean>();
					balAmount = balAmount.add(totalAmount[i].negate());
					Motransaction aMotransaction = new Motransaction();
					aMotransaction.setRxMasterId(rxMasterID[i]);
					aMotransaction.setRxAddressId(rxAddressID[i]);				
					aMotransaction.setTransactionDate(theMotransaction.getTransactionDate());
					aMotransaction.setMoAccountId(theMotransaction.getMoAccountId());
					aMotransaction.setPrinted(theMotransaction.getPrinted());
					aMotransaction.setCoAccountId(theMotransaction.getCoAccountId());
					aMotransaction.setReference(""+chkno);
					aMotransaction.setMoTransactionTypeId(theMotransaction.getMoTransactionTypeId());
					
					if(cb==1)
					{
					aMotransaction.setUniquechkRef(""+chkno);
					aMotransaction.setDescription("Vendor Write Checks");
					aMotransaction.setCheckType(theMotransaction.getCheckType());
					aMotransaction.setAmount(totalAmount[i].negate());
					balcalculationAmount = balcalculationAmount.add(balAmount); // for each Amount
					aMotransaction.setBalance(theMotransaction.getBalance().add(balcalculationAmount)); 
					aMotransaction.setVoid_((byte)0);
					aMotransaction.setPageflagVoid(false);
					}
					else
					{	
					aMotransaction.setUniquechkRef(""+(chkno-(cb-1)));
					aMotransaction.setDescription("***VOID***");
					aMotransaction.setStatus(true);
					aMotransaction.setAmount(BigDecimal.ZERO);
					aMotransaction.setVoid_((byte)0);
					aMotransaction.setPageflagVoid(true);
					balcalculationAmount = balcalculationAmount.add(BigDecimal.ZERO); // for each Amount
					aMotransaction.setBalance(theMotransaction.getBalance().add(balcalculationAmount)); 
					}
					
					
					aMotransaction.setReconciled((byte)0);
					aMotransaction.setDirectDeposit((byte)0);
					aMotransaction.setTempRec((byte)0);
					moTransactionId =(Integer)aSession.save(aMotransaction);
					
					Iterator<?> aIterator  = null;
					
					if(cb*25 < payBeanlist.size()) // pick data from list using index
					aIterator  = aQuery4.list().subList(valSublist, cb*25).iterator();
					else
					aIterator  = aQuery4.list().subList(valSublist, payBeanlist.size()).iterator();	
					
					while(aIterator.hasNext()){
					Object[] aObj = (Object[])aIterator.next();
						VendorPayBean aPayBean = new VendorPayBean();
						aPayBean.setVebillpayId((Integer)aObj[0]);
						aPayBean.setVebillID((Integer)aObj[1]);
						aPayBean.setApplyingAmount((BigDecimal)aObj[2]);
						aPayBean.setDiscountAmount((BigDecimal)aObj[3]);
						payBean.add(aPayBean);
						
						WritecheckDetails wcObj = new WritecheckDetails();
						wcObj.setWcDetailsID(1);
						wcObj.setMoAccountID(theMotransaction.getMoAccountId());
						wcObj.setMoTransactionId(moTransactionId);
						wcObj.setMoTransactionDate(theMotransaction.getTransactionDate());
						wcObj.setMoTransAmount(totalAmount[i].negate());
						wcObj.setMoLinkappliedAmount((BigDecimal)aObj[2]);
						wcObj.setMoLinkdiscount((BigDecimal)aObj[3]);
						wcObj.setCheckno(""+chkno);
						wcObj.setUserID(userID);
						wcObj.setRxMasterID(rxMasterID[i]);
						wcObj.setRxAddressID(rxAddressID[i]);
						wcObj.setVeBillID((Integer)aObj[1]);
						wcObj.setVeBillDate((Date)aObj[4]);
						wcObj.setInvoiceNumber((String)aObj[5]);
						wcObj.setVeBillAmount((BigDecimal)aObj[6]);
						wcObj.setVeBillAppliedAmt((BigDecimal)aObj[7]);
						wcObj.setPoNumber((String)aObj[8]);
						wcObj.setVeBillBalance( ((BigDecimal)aObj[6]!=null?(BigDecimal)aObj[6]:BigDecimal.ZERO).subtract(((BigDecimal)aObj[2]!=null?(BigDecimal)aObj[2]:BigDecimal.ZERO).add((BigDecimal)aObj[3]!=null?(BigDecimal)aObj[3]:BigDecimal.ZERO)));
						if(valSublist>0)
							wcObj.setVoidStatus(1);
						else
							wcObj.setVoidStatus(0);
						aSession.save(wcObj);
						
					}
					/*String billtoRxmasterQuery = "SELECT vb.veBillID FROM veBillPay as vp INNER JOIN veBill as vb on vp.veBillID = vb.veBillID WHERE  vb.rxMasterID ="+rxMasterID[i]+";";
					List<?> billList = aSession.createSQLQuery(billtoRxmasterQuery).list();*/
					for(j=0;j<payBean.size();j++){
						
						Molinkagedetail aMolinkagedetail = new Molinkagedetail();
						aMolinkagedetail.setVeBillId(payBean.get(j).getVebillID());
						aMolinkagedetail.setAmount(payBean.get(j).getApplyingAmount());
						aMolinkagedetail.setMoLinkageId(moLinkageId);
						aMolinkagedetail.setDiscount(payBean.get(j).getDiscountAmount());
						aMolinkagedetail.setMoTransactionId(moTransactionId);
						moLinkageDetailID = (Integer)aSession.save(aMolinkagedetail);
						
						Vebill aVebill = new Vebill();
						aVebill = (Vebill)aSession.get(Vebill.class,payBean.get(j).getVebillID());
						BigDecimal appliedAmount = aVebill.getAppliedAmount();
						BigDecimal billAmount = aVebill.getBillAmount();
						appliedAmount = appliedAmount.add(payBean.get(j).getApplyingAmount()).add(payBean.get(j).getDiscountAmount());
						aVebill.setAppliedAmount(appliedAmount);
						aVebill.setCreditUsed("0");
						
						if(appliedAmount.compareTo(billAmount)==0)
							aVebill.setTransactionStatus(2);
						else
							aVebill.setTransactionStatus(1);
						
						aSession.update(aVebill);
						
						/**Created by: Leo  ID:110 
						 * Date:04/07/2015
						 * Purpose: To store payment history */
						
						if(payBean.get(j).getApplyingAmount().signum()>0)
						{
						VeBillPaymentHistory aVeBillPaymentHistory = new VeBillPaymentHistory();
						aVeBillPaymentHistory.setVeBillID(payBean.get(j).getVebillID());
						aVeBillPaymentHistory.setCheckNo(""+chkno);
						aVeBillPaymentHistory.setDatePaid(theMotransaction.getTransactionDate());
						aVeBillPaymentHistory.setAmountVal(payBean.get(j).getApplyingAmount());
						aSession.save(aVeBillPaymentHistory);
						}
					}
					Movendorchecktemp amoVendorCheckTemp = new Movendorchecktemp();
					amoVendorCheckTemp.setMoTransactionId(moTransactionId);
					aSession.save(amoVendorCheckTemp);
					chkno++;
					}
				
				}
			}
				/**Inserting GlTransaction*/
				payingVendorBillDiscount1(theMotransaction.getMoAccountId(), checkNumber,yearID,periodID,userName,theMotransaction.getTransactionDate(),userID);
				 
				/** Delete VeBillPay **/
				String rxMasterIds = StringUtils.join(rxMasterID, ',');
				if(rxMasterID.length>0)
				{
				vendorPayBeanQuery = "DELETE FROM veBillPay WHERE veBillPayID IN (SELECT * FROM (SELECT vP.veBillPayID FROM veBillPay vP,veBill v WHERE vP.moAccountId="+theMotransaction.getMoAccountId()+" AND vP.userID = "+userID+"  AND vP.veBillID = v.veBillID AND v.rxMasterID IN ("+rxMasterIds+")) AS subQuery)" ;
				aSession.createSQLQuery(vendorPayBeanQuery).executeUpdate();
				}
				BigDecimal totalamount = new BigDecimal(0);
					for(i=0;i<totalAmount.length;i++)
					{
						if(totalAmount[i]!=null)
						totalamount = totalamount.add(totalAmount[i]);
					}

				aMoaccount = (MoAccount)aSession.get(MoAccount.class,theMotransaction.getMoAccountId());
				aMoaccount.setNextCheckNumber(chkno);
							
				//BigDecimal openBalance = aMoaccount.getOpenBalance();
				/*openBalance = openBalance.subtract(totalamount);
				aMoaccount.setOpenBalance(openBalance);*/
				BigDecimal substractions=  aMoaccount.getSubtractions();
				substractions = substractions.add(totalamount);
				aMoaccount.setSubtractions(substractions);
				aSession.update(aMoaccount);
				aTransaction.commit();
			
			}
			else // Credit Payment 
			{
				/**Created by: Leo  ID:110 
				 * Date:04/06/2015
				 * Purpose: for credit payments */
				
				String creditVeBillID = "";
				
				for(i=0;i<rxMasterID1.size();i++){ // iterate credit rxMasterIDs
					balAmount = new BigDecimal(0);
					ArrayList<VendorPayBean> payBean = new ArrayList<VendorPayBean>();
					BigDecimal pickNegativeAmt =new BigDecimal(0);
					
					vendorPayBeanQuery = "SELECT veBillPay.veBillPayID, veBillPay.veBillID, veBillPay.ApplyingAmount, veBillPay.DiscountAmount "+
							"FROM veBill LEFT JOIN veBillPay ON veBill.veBillID = veBillPay.veBillID "+
							"WHERE veBillPay.ApplyingAmount<>0 AND veBillPay.moAccountId="+theMotransaction.getMoAccountId()+" and veBillPay.userID = "+userID+" And  veBill.rxMasterID= "+ rxMasterID1.get(i) + 
											" ORDER BY veBill.InvoiceNumber, veBill.BillDate;";
					Query aQuery4 = aSession.createSQLQuery(vendorPayBeanQuery);  // get All veBillPay Details
					Iterator<?> aIterator  = aQuery4.list().iterator();
					while(aIterator.hasNext()){
					Object[] aObj = (Object[])aIterator.next();
						VendorPayBean aPayBean = new VendorPayBean();
						aPayBean.setVebillpayId((Integer)aObj[0]);
						aPayBean.setVebillID((Integer)aObj[1]);
						aPayBean.setApplyingAmount((BigDecimal)aObj[2]);
						aPayBean.setDiscountAmount((BigDecimal)aObj[3]);
						if(	aPayBean.getApplyingAmount().signum()<0)
						{
							pickNegativeAmt = pickNegativeAmt.add(aPayBean.getApplyingAmount()); //sum of negative Amount only
							if(creditVeBillID.equals(""))  // Get All veBill IDs
							creditVeBillID = ""+(Integer)aObj[1]; 
							else
							creditVeBillID = creditVeBillID+","+(Integer)aObj[1];  
						}
						payBean.add(aPayBean);
					}
				
					balAmount = balAmount.add(pickNegativeAmt.negate().add(creditAmtUsed.get(i))); // sum of all applying amt without credit Amount
					
					for(j=0;j<payBean.size();j++){ // Iterate veBill IDs
					
						Vebill aVebill = new Vebill();
						aVebill = (Vebill)aSession.get(Vebill.class,payBean.get(j).getVebillID());
						BigDecimal appliedAmount = aVebill.getAppliedAmount();
						BigDecimal billAmount = aVebill.getBillAmount();
						itsLogger.info(payBean.get(j).getApplyingAmount());
						/**
						 * Note: Credit bill don't update check no. credit used bills only need check no.
						 */
						
						if(payBean.get(j).getApplyingAmount().signum()<0) // check whether Applying Amt is in negative value
						{
							
							if(payBean.get(j).getApplyingAmount().negate().compareTo(balAmount)==1)	// check credit amount is greater or not
							{
								appliedAmount = appliedAmount.add(balAmount.negate()).add(payBean.get(j).getDiscountAmount());
								aVebill.setAppliedAmount(appliedAmount);
							}
							else
							{
								appliedAmount = appliedAmount.add(payBean.get(j).getApplyingAmount()).add(payBean.get(j).getDiscountAmount());
								aVebill.setAppliedAmount(appliedAmount);
								balAmount = balAmount.add(payBean.get(j).getApplyingAmount());
							}
							aVebill.setCreditUsed("0");
							itsLogger.info(balAmount);
						}
						else
						{
						/**Created by: Leo  ID:110 
						 * Date:04/07/2015
						 * Purpose: To store payment history */
							
							appliedAmount = appliedAmount.add(payBean.get(j).getApplyingAmount()).add(payBean.get(j).getDiscountAmount());
							
							VeBillPaymentHistory aVeBillPaymentHistory = new VeBillPaymentHistory();
							aVeBillPaymentHistory.setVeBillID(payBean.get(j).getVebillID());
							aVeBillPaymentHistory.setCheckNo(""+chkno);
							aVeBillPaymentHistory.setDatePaid(theMotransaction.getTransactionDate());
							aVeBillPaymentHistory.setAmountVal(appliedAmount);
							aSession.save(aVeBillPaymentHistory);	
								
							
							aVebill.setAppliedAmount(appliedAmount);
							if(aVebill.getCreditUsed()!=null && !aVebill.getCreditUsed().equals("0"))						
							aVebill.setCreditUsed(aVebill.getCreditUsed()+","+creditVeBillID);
							else
							aVebill.setCreditUsed(""+creditVeBillID);
							
							itsLogger.info(appliedAmount);
						}
							
							if(appliedAmount.compareTo(billAmount)==0)
								aVebill.setTransactionStatus(2);
							else
								aVebill.setTransactionStatus(1);
							aSession.update(aVebill);
					}
					Movendorchecktemp amoVendorCheckTemp = new Movendorchecktemp();
					amoVendorCheckTemp.setMoTransactionId(moTransactionId);
					aSession.save(amoVendorCheckTemp);
					chkno++;
				}
				
				aMoaccount = (MoAccount)aSession.get(MoAccount.class,theMotransaction.getMoAccountId());
				aMoaccount.setNextCheckNumber(chkno);
				aSession.update(aMoaccount);
				
				aTransaction.commit();
			}
			
			if(rxMasterID.length>0)
			{
				aPrintBean.setReference("Success");
			}
			else
			{
				aPrintBean.setReference("Error");
			}
			
		}	
		catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			
			aTransaction.rollback();
			
			if(rxMasterID.length>0)
			{
				aPrintBean.setReference("Success");
			}
			else
			{
				aPrintBean.setReference("Error");
			}
		
			
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			deletetemCheck=null;billArrayQuery=null; rxAddressIDQuery=null; vendorPayBeanQuery=null;
			totalAmountQuery=null;
		}	
		return aPrintBean;
	}
	
	
	
	
	
	public void payingVendorBillDiscount1(Integer MoAccountID,Integer checkNumber,Integer yearID,Integer periodID,String username,Date transactionDate,Integer userID) throws BankingException,VendorException
	{
		
		Integer[] rxMasterID =null;
		Session aSession1 = null;
		StringBuffer totalAmountQuery =null;
		String vendorPayBeanQuery=null;
		String rxMasterList = null;
		try{
			 aSession1  = itsSessionFactory.openSession();
			 
		rxMasterList = "SELECT BillPayUniqueIDs.rxMasterID FROM (SELECT rxMasterID FROM (SELECT veBill.rxMasterID FROM veBill LEFT JOIN veBillPay ON veBill.veBillID = veBillPay.veBillID WHERE veBillPay.moAccountId = "+MoAccountID+" And veBillPay.userID = "+userID+" And veBillPay.ApplyingAmount<>0) AS BillPayIDs GROUP BY rxMasterID) AS BillPayUniqueIDs "
				+ "INNER JOIN rxMaster ON BillPayUniqueIDs.rxMasterID = rxMaster.rxMasterID ORDER BY Name";
		Query aQuery1 = aSession1.createSQLQuery(rxMasterList);
		itsLogger.info(aQuery1.toString());
		List<?> rxMasterLists = aQuery1.list();
		rxMasterID = new Integer[ aQuery1.list().size()];
		
		for(int i=0;i<rxMasterLists.size();i++){
			rxMasterID[i]=(Integer)rxMasterLists.get(i);
			
		}
		
		itsLogger.info("Checking======>>>"+rxMasterID.length);
		
		for(int i=0;i<rxMasterID.length;i++){
			totalAmountQuery = new StringBuffer("SELECT SUM(P.ApplyingAmount) AS CheckAmount FROM veBillPay AS P INNER JOIN veBill AS B ON P.veBillID = B.veBillID Where P.moAccountId="+MoAccountID+" And P.userID = "+userID+" And (B.rxMasterID =").append(rxMasterID[i]).append(")");
			Query aQuery2 = aSession1.createSQLQuery(totalAmountQuery.toString());
			List<?> totalList= aQuery2.list();
				for(int j =0; j<totalList.size();j++){
					if((BigDecimal)totalList.get(j)!=null)
					{
						BigDecimal amt=(BigDecimal)totalList.get(j);
						
						if(amt.signum()<0)
						{
							rxMasterID = (Integer[]) ArrayUtils.remove(rxMasterID, i);
						}
					}
					
				}	
			}
		
		itsLogger.info("Checking======>>>"+rxMasterID.length);
		
		int chkno=0;
		chkno=checkNumber;
		for(int i=0;i<rxMasterID.length;i++){
			
			BigDecimal TotalAmt=new BigDecimal(0);
			BigDecimal ApplyingAmt=new BigDecimal(0);
			BigDecimal DiscountAmt=new BigDecimal(0);
			
			Motransaction aMotransaction = new Motransaction();
			
			aMotransaction.setMoAccountId(MoAccountID);
			
			SysAccountLinkage aSysAccountLinkage = gltransactionService.getsysAccountLinkageDetail();
			
			//Get Discount ID
			
			Coaccount CoAccountIdDiscountdetails = gltransactionService
					.getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage
							.getCoAccountIddiscountsTaken());		
			
			//Get coAccount Details

			Coaccount coaccount = gltransactionService
					.getcoAccountDetails(aMotransaction);
			 
			Coaccount CoAccountIdapdetails =null;
			 
				 CoAccountIdapdetails = gltransactionService.getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIdap());
				 Rxmaster liRxmasters = gltransactionService.getTransactionDescriptionfromrxMasterID(rxMasterID[i]);
				 
					Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
					Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
					
					Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("WC");
					
					GlTransaction glTransaction = new GlTransaction();
	
					// period
					glTransaction
							.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
					glTransaction.setPeriod(aCofiscalperiod.getPeriod());
					glTransaction.setpStartDate(aCofiscalperiod.getStartDate());
					glTransaction.setpEndDate(aCofiscalperiod.getEndDate());
	
					// year
					glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
					
					if(aCofiscalyear.getFiscalYear()!=null)
					glTransaction.setFyear(aCofiscalyear.getFiscalYear());
					
					glTransaction.setyStartDate(aCofiscalyear.getStartDate());
					glTransaction.setyEndDate(aCofiscalyear.getEndDate());
	
					// journal
					glTransaction.setJournalId(aColedgersource.getJournalID());
					glTransaction.setJournalDesc(aColedgersource.getDescription());
					glTransaction.setEntrydate(new Date());
					glTransaction.setEnteredBy(username);
					glTransaction.setTransactionDate(transactionDate);
	
					// Ponumber and trDesc
					
					
					glTransaction.setPoNumber(""+chkno);
					
					if(liRxmasters.getName()!=null)
					glTransaction.setTransactionDesc(liRxmasters.getName());
			
			
		ArrayList<VendorPayBean> payBean = new ArrayList<VendorPayBean>();
		List<VendorPayBean> payBeanList = new ArrayList<VendorPayBean>();
		
			
		 vendorPayBeanQuery = "SELECT veBillPay.veBillPayID, veBillPay.veBillID, veBillPay.ApplyingAmount, veBillPay.DiscountAmount "+
				"FROM veBill LEFT JOIN veBillPay ON veBill.veBillID = veBillPay.veBillID "+
				"WHERE veBillPay.ApplyingAmount<>0 AND veBillPay.moAccountId="+MoAccountID+" And veBillPay.userID = "+userID+" AND veBill.rxMasterID= "+ rxMasterID[i] + 
								" ORDER BY veBill.InvoiceNumber, veBill.BillDate;";
		
		Query aQuery4 = aSession1.createSQLQuery(vendorPayBeanQuery);
		payBeanList =  aQuery4.list();
		Iterator<?> aIterator  = payBeanList.iterator();
		while(aIterator.hasNext()){
		Object[] aObj = (Object[])aIterator.next();
			VendorPayBean aPayBean = new VendorPayBean();
			aPayBean.setVebillpayId((Integer)aObj[0]);
			aPayBean.setVebillID((Integer)aObj[1]);
			aPayBean.setApplyingAmount((BigDecimal)aObj[2]);
			aPayBean.setDiscountAmount((BigDecimal)aObj[3]);
			payBean.add(aPayBean);
		}
		
		for(int j=0;j<payBean.size();j++){
		
		/*Get Account Payable and Discount IDs*/
		
			TotalAmt = payBean.get(j).getApplyingAmount().add(payBean.get(j).getDiscountAmount()).add(TotalAmt);
			ApplyingAmt =  payBean.get(j).getApplyingAmount().add(ApplyingAmt);
			DiscountAmt  = payBean.get(j).getDiscountAmount().add(DiscountAmt);
			 
		}
		
		
		int gltranID=0;	
		int gltranID1=0;
		int gltranID2=0;
		 //insert GlTransacion Table using Banking Details
		 
	 	gltranID = insertBankDettoglTranfromwritechecks(glTransaction, coaccount , aColedgersource, ApplyingAmt);
		
	 	for(int j=0;j<payBean.size();j++){
	 		if(gltranID!=0)	
			//glLinkageInsert(aColedgersource,gltranID,payBean.get(j).getVebillID	());
	 			glLinkageInsert(aColedgersource,gltranID,chkno,MoAccountID);
	 			
		}

		//insert GlTransacion Table using Account Payable Details

	 	gltranID1 = insertaccApDettoglTranfromwritechecks(glTransaction, CoAccountIdapdetails , aColedgersource,TotalAmt );
			
			for(int j=0;j<payBean.size();j++){
				if(gltranID1!=0)	
					glLinkageInsert(aColedgersource,gltranID1,chkno,MoAccountID);
			}

		//insert GlTransacion Table using Discount Details

		gltranID2 = insertDiscDettoglTranfromwritechecks(glTransaction, CoAccountIdDiscountdetails , aColedgersource, DiscountAmt);
		
		for(int j=0;j<payBean.size();j++){
			
			if(gltranID2!=0)
				glLinkageInsert(aColedgersource,gltranID2,chkno,MoAccountID);
		}
	
			if(payBeanList.size()>25) // checkno increment
			{
				int incCheckno = payBeanList.size()/25;
				if(payBeanList.size()%25 > 0)
					incCheckno += 1;
				
				chkno = chkno+incCheckno;
			}
			else
			{
				chkno++;
			}
		}
		}
		catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			
			aSession1.flush();
			aSession1.close();
			totalAmountQuery=null; vendorPayBeanQuery=null;rxMasterList = null;
		}
	}
	
	
	
	
	
	public int insertBankDettoglTranfromwritechecks(GlTransaction glTransaction,Coaccount CoAccountIdaps,Coledgersource aColedgersource, BigDecimal TotalAmt)
	throws BankingException
	{
		int glTransationid = 0;
		glTransaction.setCoAccountId(CoAccountIdaps.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdaps.getNumber());		
		
		glTransaction.setDebit(new BigDecimal(0));
		glTransaction.setCredit(TotalAmt);
		
		
		if (TotalAmt.compareTo(BigDecimal.ZERO) > 0) {
			glTransationid=gltransactionService.saveGltransactionTable(glTransaction);
		
		}
	
		return glTransationid;
		
	}
	
	
	
	
	
	public int insertaccApDettoglTranfromwritechecks(GlTransaction glTransaction,Coaccount CoAccountIdaps,Coledgersource aColedgersource, BigDecimal TotalAmt)
			throws BankingException
			{
				int glTransationid = 0;
				glTransaction.setCoAccountId(CoAccountIdaps.getCoAccountId());
				glTransaction.setCoAccountNumber(CoAccountIdaps.getNumber());		
				
				glTransaction.setDebit(TotalAmt);
				glTransaction.setCredit(new BigDecimal(0));

				if (TotalAmt.compareTo(BigDecimal.ZERO) > 0) {
					glTransationid=gltransactionService.saveGltransactionTable(glTransaction);
				
				}
			
				return glTransationid;
				
			}
	
	public int insertDiscDettoglTranfromwritechecks(GlTransaction glTransaction,Coaccount CoAccountIdaps,Coledgersource aColedgersource, BigDecimal TotalAmt)
			throws BankingException
			{
				int glTransationid = 0;
				glTransaction.setCoAccountId(CoAccountIdaps.getCoAccountId());
				glTransaction.setCoAccountNumber(CoAccountIdaps.getNumber());		
				
				glTransaction.setDebit(new BigDecimal(0));
				glTransaction.setCredit(TotalAmt);
				
				if (TotalAmt.compareTo(BigDecimal.ZERO) > 0) {
					glTransationid=gltransactionService.saveGltransactionTable(glTransaction);
				}
				return glTransationid;
			}
	
	
	public void glLinkageInsert(Coledgersource aColedgersource,Integer gltransactionID,Integer vebillID,Integer moAccountID) throws BankingException
	{
		GlLinkage glLinkage = new GlLinkage();
		glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
		glLinkage.setGlTransactionId(gltransactionID);
		glLinkage.setEntryDate(new Date());
		
		/*
		 * Description: MoAccountID is Added to check#. 
		 * Table:glLinkage
		 * Reason: fetching Purpose
		 */
		glLinkage.setVeBillID(Integer.parseInt(moAccountID+""+vebillID));
		
		glLinkage.setStatus(0);
		gltransactionService.saveGlLinkageTable(glLinkage);
		
	}
	
	
	public Vebill getveBillDetailsList(Integer theVeBillId)
			throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		Vebill aVebill =null;
		String addressQuery =null;
		try {
			
				 addressQuery = "SELECT veBillID,rxMasterID,vePOID,APAccountID FROM veBill WHERE veBillID="
						+ theVeBillId;
				Query aQuery = aSession.createSQLQuery(addressQuery);
			
				Iterator<?> aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					aVebill= new Vebill();
					Object[] aObj = (Object[]) aIterator.next();
					aVebill.setVeBillId((Integer) aObj[0]);
					aVebill.setRxMasterId((Integer) aObj[1]);
					aVebill.setVePoid((Integer) aObj[2]);
					aVebill.setApaccountId((Integer) aObj[3]);
					
				}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery =null;
		}

		return aVebill;
	}
	
	

	
	public Vebillpay getBillPayAccountDetails(Integer thevebilID)
			throws BankingException {
		
		Session aSession = null;
		
		String aVendorBillPayQryStr = "SELECT veBillPayID, veBillID, ApplyingAmount, DiscountAmount FROM veBillPay WHERE veBillPayID = '"+thevebilID+"'";
		
		Vebillpay aVendorBillPayBean = null;
		try {
			
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aVendorBillPayQryStr);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillPayBean = new Vebillpay();
				Object[] aObj = (Object[])aIterator.next();
				aVendorBillPayBean.setVeBillPayId((Integer) aObj[0]);
				aVendorBillPayBean.setVeBillId((Integer) aObj[1]);
				if((BigDecimal) aObj[2]==null)
					aVendorBillPayBean.setApplyingAmount(new BigDecimal(0));
				else
					aVendorBillPayBean.setApplyingAmount((BigDecimal) aObj[2]);
				if((BigDecimal) aObj[3]==null)
					aVendorBillPayBean.setDiscountAmount(new BigDecimal(0));
				else
					aVendorBillPayBean.setDiscountAmount((BigDecimal) aObj[3]);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillPayQryStr=null;
		}
		return aVendorBillPayBean;
	}
	
	@Override
	public PrintCheckBean rePrintCheck(Motransaction theMotransaction,String checkNumber,String endNo) throws BankingException {
		PrintCheckBean aPrintBean = new PrintCheckBean();
		Session aSession  = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Integer[] rxMasterID =null;
		String billArrayQuery=null, deletetemCheck=null, rxMasterList=null,  rxAddressIDQuery=null;
		StringBuffer totalAmountQuery=null;
		BigDecimal[] totalAmount=null;
		checkDate = theMotransaction.getTransactionDate();
		int i =0;
		int j =0;
		try{
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			deletetemCheck = "DELETE FROM moVendorCheckTemp";
			aSession.createSQLQuery(deletetemCheck).executeUpdate();
			aTransaction.commit();
			billArrayQuery = "SELECT veBillID FROM moLinkageDetail WHERE moTransactionID IN (SELECT moTransactionID FROM moTransaction WHERE CAST(reference AS UNSIGNED) BETWEEN "+checkNumber+" AND "+endNo+" )";
			Query aQuery = aSession.createSQLQuery(billArrayQuery);
			List<?> billIds= aQuery.list();
			billId = new Integer[ billIds.size()];
			itsLogger.info("Bill ID: "+billId.toString());
			for(i=0;i<billIds.size();i++)
				billId[i] = (Integer)billIds.get(i);
			rxMasterList = "SELECT BillPayUniqueIDs.rxMasterID FROM (SELECT rxMasterID FROM (SELECT veBill.rxMasterID FROM veBill WHERE veBill.veBillID ="+(Integer)billIds.get(0)+" ) AS BillPayIDs GROUP BY rxMasterID) AS BillPayUniqueIDs INNER JOIN rxMaster ON BillPayUniqueIDs.rxMasterID = rxMaster.rxMasterID ORDER BY NAME";
			Query aQuery1 = aSession.createSQLQuery(rxMasterList);
			itsLogger.info(aQuery1.toString());
			List<?> rxMasterLists = aQuery1.list();
			rxMasterID = new Integer[ rxMasterLists.size()];
			for(i=0;i<rxMasterLists.size();i++){
				rxMasterID[i]=(Integer)rxMasterLists.get(i);
				
			}
			totalAmount = new BigDecimal[rxMasterID.length];
			for(i=0;i<rxMasterID.length;i++){
				//StringBuffer totalAmountQuery = new StringBuffer("SELECT SUM(P.ApplyingAmount) AS CheckAmount FROM veBillPay AS P INNER JOIN veBill AS B ON P.veBillID = B.veBillID Where (B.rxMasterID =").append(rxMasterID[i]).append(")");
				
				totalAmountQuery = new StringBuffer("SELECT Amount FROM moTransaction WHERE reference='"+checkNumber+"'");
				
						

				Query aQuery2 = aSession.createSQLQuery(totalAmountQuery.toString());
				List<?> totalList= aQuery2.list();
					for( j =0; j<totalList.size();j++){
						if((BigDecimal)totalList.get(j)!=null)
						totalAmount[i] = (BigDecimal)totalList.get(j);
						
					}	
				}
			rxAddressID = new Integer[rxMasterID.length];
			for(i=0;i<rxMasterID.length;i++){
				rxAddressIDQuery = "SELECT rxAddressID FROM rxAddress WHERE IsMailing = 1 AND rxMasterID= " +rxMasterID[i]+ " limit 1";
				Query aQuery3 = aSession.createSQLQuery(rxAddressIDQuery);
				itsLogger.info("Query2 "+aQuery3.toString());
				 List<?> rxAddressList = aQuery3.list();
					for( j =0; j<rxAddressList.size();j++){
						if((Integer)rxAddressList.get(j)!=null)
						rxAddressID[i] = (Integer)rxAddressList.get(j);
					}
				}
		}	
		catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			aTransaction.rollback();
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			 billArrayQuery=null; deletetemCheck=null; rxMasterList=null;  rxAddressIDQuery=null;
			 totalAmountQuery=null;
		}	
		return aPrintBean;
	}
	
	@Override
	public void printCheckDetails(HttpServletResponse theResponse,HttpServletRequest theRequest) throws BankingException {
		Session printBeanSession =itsSessionFactory.openSession();
	    SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) printBeanSession.getSessionFactory();
	    ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		Map<String, Object> params = new HashMap<String, Object>();
		//Rxaddress address = null;
		Connection connection = null;InputStream imageStream =null;
		try{ 
			Integer addressID = rxAddressID[0];
			//address = (Rxaddress)printBeanSession.get(Rxaddress.class,addressID);
			params.put("Name", "");
			params.put("Address2", "");
			params.put("Address3","");
			itsLogger.info(billId.length);
			String count = billId.length+"";
			params.put("BillCount",count);
			params.put("CheckDate",checkDate);
			
			
			TsUserSetting objtsusersettings = (TsUserSetting) printBeanSession.get(TsUserSetting.class, 1);
			 Blob blob =  objtsusersettings.getCompanyLogo();
			 imageStream =blob.getBinaryStream();
			//params.put("companyLogo", imageStream);
			
			itsLogger.info("Check Date: "+checkDate);
			ServletOutputStream out = theResponse.getOutputStream();
			String fileName = theRequest.getParameter("fileName");
			theResponse.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			theResponse.setContentType("application/pdf");
			connection = connectionProvider.getConnection();
			String path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/PrintChecksfinal.jrxml");
			//String path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CheckMICR.jrxml");
			JasperReport report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params, connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
		} finally {
			try {
				if(imageStream!=null){
					imageStream.close();
				}
				if(connectionProvider!=null){
					connectionProvider.closeConnection(connection);
					connectionProvider=null;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printBeanSession.flush();
			printBeanSession.close();
			
		}
	}
	
	
	public Boolean billExistsToPay() throws BankingException {
			Session aSession = null;
			Boolean billsExists = true;
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createQuery("FROM  Vebillpay");
					 billsExists =  aQuery.list().isEmpty() ? false : true ;
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				BankingException aBankingException = new BankingException(e.getMessage(), e);
				throw aBankingException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return billsExists;
	}
	
	
	public Rxaddress getAddressFromTransactionId(Integer moTransactionID) throws BankingException{
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT * FROM rxAddress , moTransaction WHERE rxAddress.rxMasterID = moTransaction.rxMasterID AND moTransaction.moTransactionID = "+moTransactionID +";";
		Query aQuery = aSession.createSQLQuery(addressQuery);
		Rxaddress aRxaddress = new Rxaddress();
		try{
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()){
				Object[] aObj = (Object[])aIterator.next();
				aRxaddress.setName((String) aObj[3]);
				aRxaddress.setAddress1((String) aObj[4]);
				aRxaddress.setAddress2((String) aObj[5]);
				aRxaddress.setCity((String) aObj[6]);
				aRxaddress.setState((String)aObj[7]);
				aRxaddress.setZip((String) aObj[8]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aRxaddress;
	}
	@Override
	public List<Motransaction> getTransactionRegisterListForReconcile(int moAccountId, int TransactionType,String sortType, String sortBy) throws BankingException {
		Session aSession = null;		
		
		itsLogger.info("TransactionType::["+TransactionType+"]");
		
		List<Motransaction> aQueryList = new ArrayList<Motransaction>();
		String aCustomerQry =null;
		if(sortBy.equals("transDate")){
			sortBy = "TransactionDate";
		}
		if(sortBy.equals("description")){
			sortBy = "reference";
		}
		/*if(TransactionType==0){
			aCustomerQry = "select mo.Description,mo.Amount,mo.TransactionDate,mo.Void,mo.CheckType,mo.Reference,mo.TempRec,mo.moTransactionTypeID,"
					+ "mo.coAccountID,mo.moAccountID,mo.moTransactionId from moTransaction mo where mo.moAccountID="+moAccountId+" and mo.Void=0 "
					+ "and mo.Reconciled=0 and mo.moTransactionTypeID IN ('0','4') ORDER BY "+sortBy+" "+sortType;
		}else{
		aCustomerQry = "select rx.Name,mo.Amount,mo.TransactionDate,mo.Void,mo.CheckType,mo.Reference,mo.TempRec,mo.moTransactionTypeID,mo.coAccountID,"
					+ "mo.moAccountID,mo.moTransactionId,rx.rxMasterID from moTransaction mo join rxMaster rx on mo.rxMasterID=rx.rxMasterID "
					+ " where mo.moAccountID="+moAccountId+" and mo.Void=0 and mo.Reconciled=0 and mo.moTransactionTypeID IN ('1','2','3') "
					+ "ORDER BY "+sortBy+" "+sortType;
		}*/
		
		if(TransactionType==0){
		aCustomerQry = "SELECT moTransactionID,TempRec,moTransactionTypeID,Amount,TransactionDate,Reference,Description,Amount AS TheAmount,coAccountID "
				+ "FROM moTransaction WHERE Void = 0 AND Reconciled = 0 and status = 0 AND moAccountID="+moAccountId
				+ " AND  NOT (moTransactionTypeID IN (1,2,3)) ORDER BY "+sortBy+" "+sortType;
		}else{
		aCustomerQry = "SELECT moTransactionID,TempRec,moTransactionTypeID,Amount,TransactionDate,Reference,  "
					+ "(CASE WHEN Description IS NULL THEN rxMaster.Name ELSE Description END)  AS Description, "
					+ "-Amount AS TheAmount,coAccountID FROM moTransaction LEFT JOIN rxMaster ON moTransaction.rxMasterID = rxMaster.rxMasterID "
					+ "WHERE Void = 0 AND Reconciled = 0 and status=0 AND moAccountID="+moAccountId+" AND  moTransactionTypeID IN (1,2,3) "
					+ "ORDER BY "+sortBy+" "+sortType;
		}
		
		Motransaction aMotransaction = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				
				aMotransaction = new Motransaction();
				Object[] aObj = (Object[])aIterator.next();
				if(aObj[4]!=null)
				{
					aMotransaction.setMoTransactionId((Integer) aObj[0]);
					aMotransaction.setTempRec((Byte) aObj[1]);
					aMotransaction.setMoTransactionTypeId((Short) aObj[2]);
					aMotransaction.setAmount((BigDecimal) aObj[3]);
					aMotransaction.setTransDate((String) DateFormatUtils.format((Date)aObj[4], "MM/dd/yyyy"));
					aMotransaction.setReference((String) aObj[5]);
					aMotransaction.setDescription((String) aObj[6]);
					aMotransaction.setTheAmount((BigDecimal) aObj[7]);
					aMotransaction.setCoAccountId((Integer) aObj[8]);
					aMotransaction.setMoAccountId(moAccountId);
					
					if(aMotransaction.getMoTransactionTypeId() == 0){
						aMotransaction.setDescription("Deposit");
					}
					if(aMotransaction.getMoTransactionTypeId() == 4){
						aMotransaction.setDescription("Interest");
						aMotransaction.setAmount(aMotransaction.getAmount());
						}
				
				/*aMotransaction.setTransactionDate((Date) aObj[2]);
				aMotransaction.setVoid_((Byte) aObj[3]);
				aMotransaction.setCheckType((Short) aObj[4]);
				aMotransaction.setCoAccountId((Integer) aObj[8]);
						
				if(TransactionType==2){
					aMotransaction.setRxMasterId((Integer) aObj[11]);
				}*/
					
				aQueryList.add(aMotransaction);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;	
	}
	
	@Override
	public boolean tempRecTransactionDetails(Integer moAccountID) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		Motransaction aMotransaction = null;
		Transaction aTransaction;
		String query = null;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();

			query = "Select moTransactionID,tempRecStatus from reconcileFLStatus where moAccountID ="+moAccountID;
			Query aQuery = aSession.createSQLQuery(query);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				if(aObj[0]!=null)
				{
					aMotransaction = (Motransaction) aSession.get(Motransaction.class, (Integer) aObj[0]);
					aMotransaction.setTempRec(((Integer)aObj[1]).byteValue());
					aSession.update(aMotransaction);
				}
			}
			query = "Delete from reconcileFLStatus where moAccountID ="+moAccountID;
			aSession.createSQLQuery(query).executeUpdate();
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateReconcileDetails(MoAccount theMoAccount) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		MoAccount aMoAccount = null;
		Transaction aTransaction;
		String updateMoTransaction =null;
		String getMoTransaction = null;
		Motransaction aMotransaction= null;
		BigDecimal sumDep=BigDecimal.ZERO;
		BigDecimal sumWidh=BigDecimal.ZERO;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			
			
			
			getMoTransaction = "Select moTransactionTypeID,Amount from moTransaction where moAccountID = "+theMoAccount.getMoAccountId()+" AND tempRec = 1 AND Void<>1";
			Query aQuery = aSession.createSQLQuery(getMoTransaction);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				
				if(aObj[0]!=null)
				{
					if((Short)aObj[0]==0)
					{
						sumDep = sumDep.add((BigDecimal)aObj[1]);
					}
					else
					{
						sumWidh = sumWidh.add((BigDecimal)aObj[1]);
					}
				}
				
			}
			
			aMoAccount = (MoAccount) aSession.get(MoAccount.class, theMoAccount.getMoAccountId());
			aMoAccount.setOpenBalance(theMoAccount.getOpenBalance());
			aMoAccount.setEndingBalance(theMoAccount.getEndingBalance());
			aMoAccount.setAdditions(aMoAccount.getAdditions().subtract(sumDep));
			aMoAccount.setSubtractions(aMoAccount.getSubtractions().add(sumWidh));
			aSession.update(aMoAccount);
			
			updateMoTransaction = "UPDATE Motransaction set reconciled = 1,tempRec=0 WHERE tempRec = 1";
			Query query = aSession.createQuery(updateMoTransaction);
			int result = query.executeUpdate();
			itsLogger.info("Rows affected: " + result);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			updateMoTransaction=null;
		}
		return true;
	}
	
	@Override
	public List<Vemaster> getAllVendors() throws VendorException {
		itsLogger.debug("Retrieving all persons");
		StringBuilder aVendorSelectQry = new StringBuilder("SELECT veMasterId,manufacturer FROM veMaster");
		Session aSession = null;
		List<Vemaster> aQueryList = new ArrayList<Vemaster>();
		Vemaster aRolodexBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aVendorSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new Vemaster();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setVeMasterId((Integer) aObj[0]);  /**	rxMaster.rxMaserId	*/
				aRolodexBean.setManufacturer((String) aObj[1]);			/** rxMaster.name	*/
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorSelectQry=null;
		}
		return  aQueryList;
	}
	@Override
	public boolean updateMoTransactionReconcile(Motransaction theMotransaction) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		Motransaction aMotransaction = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			aMotransaction = (Motransaction) aSession.get(Motransaction.class, theMotransaction.getMoTransactionId());
			aMotransaction.setTempRec(theMotransaction.getTempRec());
			aMotransaction.setMoTransactionId(theMotransaction.getMoTransactionId());
			aMotransaction.setAmount(theMotransaction.getAmount());
			if(theMotransaction.getRxMasterId()!=null){
				aMotransaction.setRxMasterId(theMotransaction.getRxMasterId());	
			}
			
			aMotransaction.setTransDate(theMotransaction.getTransDate());
			aMotransaction.setReference(theMotransaction.getReference());
			aSession.update(aMotransaction);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public boolean createMoTransactionReconcile(Motransaction theMotransaction) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
//		aSession.save(theJob);
		try {
			aSession.save(theMotransaction);
//			aTransaction = aSession.beginTransaction();	
//			aTransaction.begin();
//			aMotransaction = (Motransaction) aSession.get(Motransaction.class, theMotransaction.getMoTransactionId());
//			aMotransaction.setTempRec(theMotransaction.getTempRec());
//			aMotransaction.setMoTransactionId(theMotransaction.getMoTransactionId());
//			aMotransaction.setAmount(theMotransaction.getAmount());
//			if(theMotransaction.getRxMasterId()!=null){
//				aMotransaction.setRxMasterId(theMotransaction.getRxMasterId());	
//			}
//			aMotransaction.setDescription(theMotransaction.getDescription());
//			aMotransaction.setTransDate(theMotransaction.getTransDate());
//			aMotransaction.setReference(theMotransaction.getReference());
//			aSession.save(aMotransaction);
//			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	@Override
	public boolean updateAllMoTransaction(Integer theMoAccountID,Integer theTempRec,Integer theTransactionType,Integer userID) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		String hql = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
		//	hql = "UPDATE moTransaction set TempRec ="+theTempRec+" WHERE moAccountID = "+theMoAccountID+" AND Void=0 AND Reconciled=0 AND moTransactionTypeID="+theTransactionType;
	
//		query.setParameter("TempRec", theTempRec);
//		query.setParameter("moAccountID", theMoAccountID);
//		query.setParameter("void", 0);
//		query.setParameter("void", 0);
//		query.setParameter("moTransactionTypeID", theTransactionType);
		
		    if(theTransactionType == 0)
		    {
		    	if(theTempRec == 1)
		    	{
		    		hql = "INSERT INTO reconcileFLStatus (moAccountID,moTransactionID,tempRecstatus,userID,entryDate) "
				    		+ " (SELECT moAccountID,moTransactionID,"+theTempRec+","+userID+", NOW() FROM moTransaction  WHERE moAccountID = "+theMoAccountID+" AND Void=0 AND "
				    		+ " Reconciled=0 AND moTransactionTypeID IN (0,4))";
		    	}
		    	else{
		    		hql = "Delete from reconcileFLStatus where moTransactionID in (select moTransactionID from moTransaction where moAccountID = "+theMoAccountID+" AND Void=0 AND Reconciled=0 AND moTransactionTypeID IN (0,4))";
		    	}
		    }
		    else
		    {
		    	if(theTempRec == 1)
		    	{
		    		hql = "INSERT INTO reconcileFLStatus (moAccountID,moTransactionID,tempRecstatus,userID,entryDate) "
				    		+ " (SELECT moAccountID,moTransactionID,"+theTempRec+","+userID+", NOW() FROM moTransaction  WHERE moAccountID = "+theMoAccountID+" AND Void=0 AND "
				    		+ " Reconciled=0 AND moTransactionTypeID Not IN (0,4))";
		    	}
		    	else{
		    		hql = "Delete from reconcileFLStatus where moTransactionID in (select moTransactionID from moTransaction where moAccountID = "+theMoAccountID+" AND Void=0 AND Reconciled=0 AND moTransactionTypeID not IN (0,4))";
		    	}
		    	
		    }
		Query query = aSession.createSQLQuery(hql);
		int result = query.executeUpdate();
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			hql=null;
		}
		return true;
	}	
	
	@Override
	public boolean updateDepositAndWithDraw(MoAccount theMoaccount,Session aSession) throws BankingException {
		//Session aSession = itsSessionFactory.openSession();
		MoAccount aMoaccount = null;
		try {
			aMoaccount = (MoAccount) aSession.get(MoAccount.class, theMoaccount.getMoAccountId());
			if(theMoaccount.getOper().equals("delete")||theMoaccount.getOper().equals("void")){
				if(theMoaccount.getMoTransactionTypeId()==0){
					aMoaccount.setSubtractions(aMoaccount.getSubtractions().add(theMoaccount.getSubtractions().multiply(new BigDecimal(-1))));
				}else{
					aMoaccount.setAdditions(aMoaccount.getAdditions().add(theMoaccount.getAdditions()));
				}
			}else{
				if(theMoaccount.getMoTransactionTypeId()==0){
					aMoaccount.setAdditions(aMoaccount.getAdditions().add(theMoaccount.getAdditions()));
				}else{
					aMoaccount.setSubtractions(aMoaccount.getSubtractions().add(theMoaccount.getSubtractions().multiply(new BigDecimal(-1))));
				}
			}
			aSession.update(aMoaccount);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		}
		finally{
			aSession.flush();
			aSession.clear();
		}
		return true;
	}

	@Override
	public Motransaction getMOtransactionDetails(Integer theMoTransactionID) throws BankingException {
		Session aSession = null;
		Motransaction aMotransac = null;
		try {
			aSession = itsSessionFactory.openSession();
			aMotransac = (Motransaction) aSession.get(Motransaction.class, theMoTransactionID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aMotransac;
	}
	
	/*Created By : Velmurugan
	 *Created On :28-08-2014
	 *Description:Resulting Balance=currentbalance+(futuredeposit-futurewithdrawel) 
	 *In mysql curdate() will work and sqlserver getdate() will work
	 * */
	@Override
	public Motransaction getResultingBalance(Integer theMoAccountID) throws BankingException {
		Date dNow = new Date( );
	    SimpleDateFormat ft =new SimpleDateFormat ("yyyy-MM-dd");
	    String date=ft.format(dNow);
		//System.out.println(ft.format(dNow));
	    String resultingbalSelectQry=null;
	    String aJobCountStr = null;
	    String currentbalSelectQry = "select distinct (select SUM(Amount) from moTransaction where  moTransactionTypeID=0 and moAccountID="+theMoAccountID+" AND Void=0  AND status=0) 'depositamount',(select SUM(Amount) from moTransaction where  moTransactionTypeID>0 and moAccountID="+theMoAccountID+" AND Void=0 AND STATUS=0) 'withdrawel amount' from moTransaction";
	    itsLogger.info("Currentbalanceqry="+currentbalSelectQry);
	    Session aSession = itsSessionFactory.openSession();
		BigDecimal currentbalance = new BigDecimal(0.00);
		Motransaction moacc=new Motransaction();
		try{
			Query aQuery1 = aSession.createSQLQuery(currentbalSelectQry.toString());
			Iterator<?> aIterator1 = aQuery1.list().iterator();
			if (aIterator1.hasNext()) {
				
				Object[] aObj1 = (Object[])aIterator1.next();
				BigDecimal currentdepositAmount=new BigDecimal(0.00);
				BigDecimal currentwithdrawelAmount=new BigDecimal(0.00);
				if(aObj1[0]!=null){
					currentdepositAmount=(BigDecimal)aObj1[0];
				}
				if(aObj1[1]!=null){
					currentwithdrawelAmount=(BigDecimal)aObj1[1];
				}
				currentbalance=currentdepositAmount.add(currentwithdrawelAmount) ; 
				
			}
			moacc.setCurrentbalance(currentbalance);
			
			resultingbalSelectQry = "select distinct (select SUM(Amount) from moTransaction where  moTransactionTypeID=0 and moAccountID="+theMoAccountID+" AND Void=0 AND STATUS=0) 'depositamount',(select SUM(Amount) from moTransaction where  moTransactionTypeID>0 and moAccountID="+theMoAccountID+" AND Void=0 AND STATUS=0) 'withdrawel amount' from moTransaction";
			itsLogger.info("resultingbalQry="+resultingbalSelectQry);
			BigDecimal resultingbalance = new BigDecimal(0.00);
			Query aQuery = aSession.createSQLQuery(resultingbalSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				
				Object[] aObj = (Object[])aIterator.next();
				BigDecimal depositAmount=new BigDecimal(0.00);
				BigDecimal withdrawelAmount=new BigDecimal(0.00);
				if(aObj[0]!=null){
					depositAmount=(BigDecimal)aObj[0];
				}
				if(aObj[1]!=null){
					withdrawelAmount=(BigDecimal)aObj[1];
				}
				
				resultingbalance=depositAmount.add(withdrawelAmount) ; 
				
			}
			
			//    aSession.close();
			    aJobCountStr = "SELECT SUM(ApplyingAmount) FROM veBillPay WHERE moAccountId="+theMoAccountID;
			    BigDecimal approveVal = new BigDecimal(0.00);
			//	aSession = itsSessionFactory.openSession();
				Query appQuery = aSession.createSQLQuery(aJobCountStr);
				List<?> aList = appQuery.list();
				approveVal =  (BigDecimal) aList.get(0);
				moacc.setWithDrawel(approveVal);
				
			    moacc.setResultingbalance(resultingbalance);
		} catch (Exception e){
			itsLogger.error(e.getMessage(), e);
			throw new BankingException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			resultingbalSelectQry=null;
		    aJobCountStr = null;currentbalSelectQry =null;
		}
		
	
		return moacc;
	}
	@Override
	public BigDecimal getpageinitialamount(int from,int to,int moaccountid) throws BankingException {
	
		String aTransactionCountStr = "SELECT (SELECT IFNULL(SUM(amount) ,0.00) FROM (SELECT amount FROM moTransaction WHERE moTransactionTypeID=0 AND moAccountID="+moaccountid+" ORDER BY moTransactionID DESC LIMIT "+from+","+to+") price) AS depositamount,"
				+"(SELECT IFNULL(SUM(amount),0.00) FROM (SELECT amount FROM moTransaction WHERE moTransactionTypeID>0 AND moAccountID=4 ORDER BY moTransactionID DESC LIMIT "+from+","+to+") price) AS withdrawelamount FROM DUAL";
		Session aSession = null;
		BigDecimal depositamount = new BigDecimal(0);
		BigDecimal withdrawelamount = new BigDecimal(0);
		BigDecimal returnvalue = new BigDecimal(0);
		itsLogger.info("getpageinitialamount=="+aTransactionCountStr);
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aTransactionCountStr);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
			depositamount = (BigDecimal)aObj[0];
			withdrawelamount=(BigDecimal)aObj[1];
			}
			returnvalue=depositamount.add(withdrawelamount);
		} catch (Exception e) {
			e.printStackTrace();
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aTransactionCountStr=null;
		}
		return returnvalue;
	}
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:Save glaccountgrid data in momultipleaccount table based upon momultipleaccountid
	 * */
	@Override
	public ArrayList<AutoCompleteBean> getAccountList(String accno)
			throws BankingException {
		String aSalesselectQry = "SELECT coAccountID,concat(Number,'-',Description) FROM coAccount WHERE (((coAccount.InActive)=0 Or (coAccount.InActive) Is Null) AND ((coAccount.TotalingLevel)=0)) "
				+ "AND Number like "
				+ "'%"
				+ accno
				+ "%'"
				+ " ORDER BY coAccount.Number ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException bankingException = new BankingException(e.getMessage(), e);
			throw bankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}
	
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:Save glaccountgrid data in momultipleaccount table based upon momultipleaccountid
	 * */
	@Override
	public boolean saveGlmultipleAccount(Momultipleaccount momulaccount) throws BankingException {
		Session aMomulAccountSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aMomulAccountSession.beginTransaction();
			aTransaction.begin();
			aMomulAccountSession.save(momulaccount);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aMomulAccountSession.flush();
			aMomulAccountSession.close();
		}
		return true;
	}
	
	@Override
	public boolean saveGlmultipleAccount1(Momultipleaccount momulaccount,Session aMomulAccountSession) throws BankingException {
		try {
			aMomulAccountSession.save(momulaccount);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aMomulAccountSession.flush();
			aMomulAccountSession.clear();
		}
		return true;
	}
	
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:update glaccountgrid data in momultipleaccount table based upon momultipleaccountid
	 * */
	@Override
	public boolean updateGlmultipleAccount(Momultipleaccount momulacc) throws BankingException {
		Session aSession = itsSessionFactory.openSession();
		Momultipleaccount aMomulaccount = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			itsLogger.info("Momultiaccountid=="+momulacc.getMoMultipleAccountsId());
			aMomulaccount = (Momultipleaccount) aSession.get(Momultipleaccount.class, momulacc.getMoMultipleAccountsId());
			aMomulaccount.setMoTransactionId(momulacc.getMoTransactionId());
			aMomulaccount.setAmount(momulacc.getAmount());
			aMomulaccount.setCoAccountId(momulacc.getCoAccountId());
			aSession.update(aMomulaccount);
			aTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateGlmultipleAccount1(Momultipleaccount momulacc,Session aSession) throws BankingException {
		Momultipleaccount aMomulaccount = null;
		try {
			itsLogger.info("Momultiaccountid=="+momulacc.getMoMultipleAccountsId());
			aMomulaccount = (Momultipleaccount) aSession.get(Momultipleaccount.class, momulacc.getMoMultipleAccountsId());
			aMomulaccount.setMoTransactionId(momulacc.getMoTransactionId());
			aMomulaccount.setAmount(momulacc.getAmount());
			aMomulaccount.setCoAccountId(momulacc.getCoAccountId());
			aSession.update(aMomulaccount);
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.clear();
		}
		return true;
	}
	
	
	/*Created By:Velmurugan
	 *Created On:05-09-2014
	 *Description:fetch all record from momultipleaccount table based upon motransaction id
	 * */
	@Override
	public List<Momultipleaccount> getGlAccountTransactionRegisterList(int theFrom, int theTo,int motransid) throws BankingException {
		Session aSession = null;
		List<Momultipleaccount> aQueryList = new ArrayList<Momultipleaccount>();
		String aCustomerQry = "SELECT mom.moMultipleAccountsID,mom.coAccountID,mom.Amount,CONCAT(ca.Number,'_',ca.Description) AS accountdesc FROM moMultipleAccount mom JOIN coAccount ca ON(mom.coAccountID=ca.coAccountID) WHERE moTransactionID="+motransid;
		Momultipleaccount Momultiacc = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Momultiacc = new Momultipleaccount();
				Object[] aObj = (Object[])aIterator.next();
				Momultiacc.setMoMultipleAccountsId((Integer) aObj[0]);
				Momultiacc.setCoAccountId((Integer) aObj[1]);
				Momultiacc.setAmount((BigDecimal)aObj[2]);
				Momultiacc.setDescription((String) aObj[3]);
				aQueryList.add(Momultiacc);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;	
	}
	
	
	@Override
	public int gettotalrecordCount(String query) throws BankingException {
		
		Session aSession = null;
		int aTotalCount = 0;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(query);
			List<?> aList = aQuery.list();
			if(aList.get(0)!=null){
				aTotalCount = ((BigInteger) aList.get(0)).intValue();;
			}
			
		} catch (Exception e) {
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aTotalCount;
	}

	@Override
	public void newCheckDetails(HttpServletResponse theResponse,HttpServletRequest theRequest) throws BankingException {
		Session printBeanSession =itsSessionFactory.openSession();
	    SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) printBeanSession.getSessionFactory();
	    ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		Map<String, Object> params = new HashMap<String, Object>();
		Connection connection =null;
		try{ 
			itsLogger.info("Testing:--->"+theRequest.getParameter("moTransactionID"));
			
			params.put("traxID",Integer.parseInt(theRequest.getParameter("moTransactionID")));
			ServletOutputStream out = theResponse.getOutputStream();
			String fileName = theRequest.getParameter("fileName");
			theResponse.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			theResponse.setContentType("application/pdf");
			connection = connectionProvider.getConnection();
			String path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/newcheck.jrxml");
			JasperReport report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params, connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
		} finally {
			try {
				
				if(connectionProvider!=null){
					connectionProvider.closeConnection(connection);
					connectionProvider=null;
					}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printBeanSession.flush();
			printBeanSession.close();
		}
	}
	
	
	@Override
	public void creditCheckDetails(HttpServletResponse theResponse,HttpServletRequest theRequest) throws BankingException {
		Session printBeanSession =itsSessionFactory.openSession();
	    SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) printBeanSession.getSessionFactory();
	    ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		Map<String, Object> params = new HashMap<String, Object>();
		Connection connection = null;
		Rxaddress address = null;
		try{ 
			itsLogger.info("Testing:--->"+theRequest.getParameter("moTransactionID"));
			
			ServletOutputStream out = theResponse.getOutputStream();
			String fileName = theRequest.getParameter("fileName");
			theResponse.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			theResponse.setContentType("application/pdf");
			connection = connectionProvider.getConnection();
			String path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/CreditPayment.jrxml");
			JasperReport report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params, connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
		//	throw aBankingException;
		} finally {
			try {
				if(connectionProvider!=null){
					connectionProvider.closeConnection(connection);
					connectionProvider=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printBeanSession.flush();
			printBeanSession.close();
		}
	}

	@Override
	public Integer getcountfindbytransactionRegisterList(ArrayList<?> theFilterDetails,int moaccountid) throws BankingException {
		Session aSession = null;
		String param1 = null,param2 = null,value = null,param3="0";
		Integer aCount = 0;
		
		if(theFilterDetails != null){
			for(int i=0;i<theFilterDetails.size();i++){
				if(i==0){
					param1=theFilterDetails.get(i).toString();
				}else if(i==1){
					param2=theFilterDetails.get(i).toString();
				}else if(i==2){
					value=theFilterDetails.get(i).toString();
				}else if(i==3){
					param3=theFilterDetails.get(i).toString();
				}
			}
		}
		List<Motransaction> aQueryList = new ArrayList<Motransaction>();
		String aCustomerQry = "SELECT *,CASE WHEN moTransaction.rxMasterID IS NULL THEN description ELSE (SELECT NAME FROM rxMaster WHERE "
				+ " rxMasterID=moTransaction.rxMasterID) END 'namedescription' FROM moTransaction WHERE moAccountID ="+moaccountid;
		Motransaction aMotransaction = null;
			
		if(param1 !=null && param1.equalsIgnoreCase("Amount")){
			if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  ="+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Greater"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  >"+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Greater or equal"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  >="+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Less"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount) <"+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Less or equal"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  <="+value;
			
		}else if(param1 !=null && param1.equalsIgnoreCase("Cleared Status")){
			if(param2 !=null && param2.equalsIgnoreCase("Reconcile"))
				aCustomerQry += " and reconciled=1";
			else if(param2 !=null && param2.equalsIgnoreCase("Unreconciled"))
				aCustomerQry += " and reconciled=0 and status=0";
			
		

			
		}else if(param1 !=null && param1.equalsIgnoreCase("Date")){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat parseDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date fromDate = null,toDate =null;
			try {
				if(param2 !=null)
					fromDate = parseDateFormat.parse(param2);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(param2 !=null &&  value.equals("0"))
				aCustomerQry += " and transactionDate >='" +simpleDateFormat.format(fromDate)+"'";
			else if(param2 ==null && value !=null)
				aCustomerQry += " and transactionDate <='" +simpleDateFormat.format(toDate)+"'";
			else if(param2 !=null && value !=null && !value.equals("0")){
				if(value !=null && !value.equals("0"))
					try {
						toDate = parseDateFormat.parse(value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				aCustomerQry += " and transactionDate between '" +simpleDateFormat.format(fromDate) +"' and '"+simpleDateFormat.format(toDate)+"'";
			}
		}else if(param1 !=null && param1.equalsIgnoreCase("Reference")){
			if(param2 !=null && param2.equalsIgnoreCase("Contains"))
				aCustomerQry += " and reference like '%"+value+"%'";
			else if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				aCustomerQry += " and reference ='"+value+"'";
		}else if(param1 !=null && param1.equalsIgnoreCase("G/L Account #")){
			aCustomerQry += " and coAccountID ="+param2+"";
		}else if(param1 !=null && param1.equalsIgnoreCase("Type")){
			if(param2 !=null && param2.equalsIgnoreCase("Deposit"))
				aCustomerQry += " and amount > 0 ";
			else
				aCustomerQry += " and amount < 0 ";
		}
	
		//aCustomerQry +=" ORDER BY moTransactionID DESC";
		String sort =(String) theFilterDetails.get(theFilterDetails.size()-1);
		
		String  descriptionFilter="";
		
		if(param1 !=null && param1.equalsIgnoreCase("Description")){
			
			if(sort.equals("0"))
				aCustomerQry +=" ORDER BY transactionDate ASC";
			else
				aCustomerQry +=" ORDER BY transactionDate DESC";
			
			
			if(param2 !=null && param2.equalsIgnoreCase("Contains"))
				descriptionFilter += "  description like '%"+value+"%' or namedescription like '%"+value+"%'";
			else if(param2 !=null && param2.equalsIgnoreCase("Ends with"))
				descriptionFilter += "  description like '%"+value+"' or namedescription like '%"+value+"'";
			else if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				descriptionFilter += "  description ='"+value+"' or namedescription = '"+value+"'";
			else if(param2 !=null && param2.equalsIgnoreCase("Starts with"))
				descriptionFilter += "  description like '"+value+"%' or namedescription like '"+value+"%'";
			
			
			aCustomerQry  = "Select * from ("+aCustomerQry+")as subquery where "+descriptionFilter;
				
		}
		else
		{
			if(sort.equals("0"))
				aCustomerQry +=" ORDER BY transactionDate ASC ";
			else
				aCustomerQry +=" ORDER BY transactionDate DESC ";
			
		}
	
	try
	{
		
		aSession = itsSessionFactory.openSession();
		Query aQuery = aSession.createSQLQuery(aCustomerQry);
		aCount = aQuery.list().size();
		
	} catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
		BankingException aBankingException = new BankingException(e.getMessage(), e);
		throw aBankingException;
	} finally {
		aSession.flush();
		aSession.close();
		aCustomerQry=null;
	}
	return aCount;	
}
	

	@Override
	public String getfindbytransactionQueryString(ArrayList<?> theFilterDetails,int moaccountid) throws BankingException {
		Session aSession = null;
		String param1 = null,param2 = null,value = null,param3="0";
		Integer aCount = 0;
		
		if(theFilterDetails != null){
			for(int i=0;i<theFilterDetails.size();i++){
				if(i==0){
					param1=theFilterDetails.get(i).toString();
				}else if(i==1){
					param2=theFilterDetails.get(i).toString();
				}else if(i==2){
					value=theFilterDetails.get(i).toString();
				}else if(i==3){
					param3=theFilterDetails.get(i).toString();
				}
			}
		}
		List<Motransaction> aQueryList = new ArrayList<Motransaction>();
		String aCustomerQry = "SELECT *,CASE WHEN moTransaction.rxMasterID IS NULL THEN description ELSE (SELECT NAME FROM rxMaster WHERE "
				+ " rxMasterID=moTransaction.rxMasterID) END 'namedescription' FROM moTransaction WHERE moAccountID ="+moaccountid;
		Motransaction aMotransaction = null;
			
		if(param1 !=null && param1.equalsIgnoreCase("Amount")){
			if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  ="+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Greater"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  >"+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Greater or equal"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  >="+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Less"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount) <"+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Less or equal"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  <="+value;
			
		}else if(param1 !=null && param1.equalsIgnoreCase("Cleared Status")){
			if(param2 !=null && param2.equalsIgnoreCase("Reconcile"))
				aCustomerQry += " and reconciled=1";
			else if(param2 !=null && param2.equalsIgnoreCase("Unreconciled"))
				aCustomerQry += " and reconciled=0 and status=0";
			
			
			
			
			
		}else if(param1 !=null && param1.equalsIgnoreCase("Date")){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat parseDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date fromDate = null,toDate =null;
			try {
				if(param2 !=null)
					fromDate = parseDateFormat.parse(param2);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(param2 !=null &&  value.equals("0"))
				aCustomerQry += " and transactionDate >='" +simpleDateFormat.format(fromDate)+"'";
			else if(param2 ==null && value !=null)
				aCustomerQry += " and transactionDate <='" +simpleDateFormat.format(toDate)+"'";
			else if(param2 !=null && value !=null && !value.equals("0")){
				if(value !=null && !value.equals("0"))
					try {
						toDate = parseDateFormat.parse(value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				aCustomerQry += " and transactionDate between '" +simpleDateFormat.format(fromDate) +"' and '"+simpleDateFormat.format(toDate)+"'";
			}
		}else if(param1 !=null && param1.equalsIgnoreCase("Reference")){
			if(param2 !=null && param2.equalsIgnoreCase("Contains"))
				aCustomerQry += " and reference like '%"+value+"%'";
			else if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				aCustomerQry += " and reference ='"+value+"'";
		}else if(param1 !=null && param1.equalsIgnoreCase("G/L Account #")){
			aCustomerQry += " and coAccountID ="+param2+"";
		}else if(param1 !=null && param1.equalsIgnoreCase("Type")){
			if(param2 !=null && param2.equalsIgnoreCase("Deposit"))
				aCustomerQry += " and amount > 0 ";
			else
				aCustomerQry += " and amount < 0 ";
		}
	
		//aCustomerQry +=" ORDER BY moTransactionID DESC";
		String sort =(String) theFilterDetails.get(theFilterDetails.size()-1);
		
		String  descriptionFilter="";
		
		if(param1 !=null && param1.equalsIgnoreCase("Description")){
			
			if(sort.equals("0"))
				aCustomerQry +=" ORDER BY transactionDate ASC";
			else
				aCustomerQry +=" ORDER BY transactionDate DESC";
			
			
			if(param2 !=null && param2.equalsIgnoreCase("Contains"))
				descriptionFilter += "  description like '%"+value+"%' or namedescription like '%"+value+"%'";
			else if(param2 !=null && param2.equalsIgnoreCase("Ends with"))
				descriptionFilter += "  description like '%"+value+"' or namedescription like '%"+value+"'";
			else if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				descriptionFilter += "  description ='"+value+"' or namedescription = '"+value+"'";
			else if(param2 !=null && param2.equalsIgnoreCase("Starts with"))
				descriptionFilter += "  description like '"+value+"%' or namedescription like '"+value+"%'";
			
			
			aCustomerQry  = "Select * from ("+aCustomerQry+")as subquery where "+descriptionFilter;
				
		}
		else
		{
			if(sort.equals("0"))
				aCustomerQry +=" ORDER BY transactionDate ASC ";
			else
				aCustomerQry +=" ORDER BY transactionDate DESC ";
			
		}
	
	return aCustomerQry;	
}
	
	
	@Override
	public List<Motransaction> findbytransactionRegisterList(int theFrom,int theTo, ArrayList<?> theFilterDetails,int moaccountid) throws BankingException {
		Session aSession = null;
		String param1 = null,param2 = null,value = null,param3="0";
		
		if(theFilterDetails != null){
			for(int i=0;i<theFilterDetails.size();i++){
				if(i==0){
					param1=theFilterDetails.get(i).toString();
				}else if(i==1){
					param2=theFilterDetails.get(i).toString();
				}else if(i==2){
					value=theFilterDetails.get(i).toString();
				}else if(i==3){
					param3=theFilterDetails.get(i).toString();
				}
			}
		}
		List<Motransaction> aQueryList = new ArrayList<Motransaction>();
		String aCustomerQry = "SELECT *,CASE WHEN moTransaction.rxMasterID IS NULL THEN description ELSE (SELECT NAME FROM rxMaster WHERE "
				+ " rxMasterID=moTransaction.rxMasterID) END 'namedescription' FROM moTransaction WHERE moAccountID ="+moaccountid;
		Motransaction aMotransaction = null;
			
		if(param1 !=null && param1.equalsIgnoreCase("Amount")){
			if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  ="+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Greater"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  >"+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Greater or equal"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  >="+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Less"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount) <"+value;
			else if(param2 !=null && param2.equalsIgnoreCase("Less or equal"))
				aCustomerQry += " and IF(amount < 0, amount*-1, amount)  <="+value;
			
		}else if(param1 !=null && param1.equalsIgnoreCase("Cleared Status")){
			if(param2 !=null && param2.equalsIgnoreCase("Reconcile"))
				aCustomerQry += " and reconciled=1 ";
			else if(param2 !=null && param2.equalsIgnoreCase("Unreconciled"))
				aCustomerQry += " and reconciled=0 and status=0";
			
			/*if (param3.equals("0"))
				aCustomerQry += " limit " + theFrom + " ," + theTo;
			if (param3.equals("1"))
				aCustomerQry += " limit " + theFrom + " ," + theTo;*/
			
			
			System.out.println("==============================================>"+aCustomerQry);
			
		}else if(param1 !=null && param1.equalsIgnoreCase("Date")){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat parseDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date fromDate = null,toDate =null;
			try {
				if(param2 !=null)
					fromDate = parseDateFormat.parse(param2);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(param2 !=null &&  value.equals("0"))
				aCustomerQry += " and transactionDate >='" +simpleDateFormat.format(fromDate)+"'";
			else if(param2 ==null && value !=null)
				aCustomerQry += " and transactionDate <='" +simpleDateFormat.format(toDate)+"'";
			else if(param2 !=null && value !=null && !value.equals("0")){
				if(value !=null && !value.equals("0"))
					try {
						toDate = parseDateFormat.parse(value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				aCustomerQry += " and transactionDate between '" +simpleDateFormat.format(fromDate) +"' and '"+simpleDateFormat.format(toDate)+"'";
			}
		}else if(param1 !=null && param1.equalsIgnoreCase("Reference")){
			if(param2 !=null && param2.equalsIgnoreCase("Contains"))
				aCustomerQry += " and reference like '%"+value+"%'";
			else if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				aCustomerQry += " and reference ='"+value+"'";
		}else if(param1 !=null && param1.equalsIgnoreCase("G/L Account #")){
			aCustomerQry += " and coAccountID ="+param2+"";
		}else if(param1 !=null && param1.equalsIgnoreCase("Type")){
			if(param2 !=null && param2.equalsIgnoreCase("Deposit"))
				aCustomerQry += " and amount > 0 ";
			else
				aCustomerQry += " and amount < 0 ";
		}
	
		//aCustomerQry +=" ORDER BY moTransactionID DESC";
		String sort =(String) theFilterDetails.get(theFilterDetails.size()-1);
		
		String  descriptionFilter="";
		
		if(param1 !=null && param1.equalsIgnoreCase("Description")){
			
			if(sort.equals("0"))
				aCustomerQry +=" ORDER BY transactionDate ASC";
			else
				aCustomerQry +=" ORDER BY transactionDate DESC";
			
			
			if(param2 !=null && param2.equalsIgnoreCase("Contains"))
				descriptionFilter += "  description like '%"+value+"%' or namedescription like '%"+value+"%'";
			else if(param2 !=null && param2.equalsIgnoreCase("Ends with"))
				descriptionFilter += "  description like '%"+value+"' or namedescription like '%"+value+"'";
			else if(param2 !=null && param2.equalsIgnoreCase("Exact"))
				descriptionFilter += "  description ='"+value+"' or namedescription = '"+value+"'";
			else if(param2 !=null && param2.equalsIgnoreCase("Starts with"))
				descriptionFilter += "  description like '"+value+"%' or namedescription like '"+value+"%'";
			
			
			aCustomerQry  = "Select * from ("+aCustomerQry+")as subquery where "+descriptionFilter;
			
			
			if(sort.equals("0"))
				aCustomerQry +=" limit "+theFrom+" ,"+theTo;
			else
				aCustomerQry +=" limit "+theFrom+" ,"+theTo;
				
		}
		else
		{
			if(sort.equals("0"))
				aCustomerQry +=" ORDER BY transactionDate ASC limit "+theFrom+" ,"+theTo;
			else
				aCustomerQry +=" ORDER BY transactionDate DESC limit "+theFrom+" ,"+theTo;
			
		}
		
		System.out.println("aCustomerQry==="+aCustomerQry);
		
		
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aMotransaction = new Motransaction();
				Object[] aObj = (Object[])aIterator.next();
				aMotransaction.setMoTransactionId((Integer) aObj[0]);
				aMotransaction.setRxMasterId((Integer) aObj[1]);
				aMotransaction.setRxAddressId((Integer) aObj[2]);
				aMotransaction.setCoAccountId((Integer) aObj[3]);
				aMotransaction.setMoAccountId((Integer) aObj[4]);
				if (aObj[5] != null && aObj[5] != "")
				aMotransaction.setTransDate((String) DateFormatUtils.format((Date)aObj[5], "MM/dd/yyyy"));
				aMotransaction.setMoTransactionTypeId((Short) aObj[6]);
				aMotransaction.setMoTypeId((Short) aObj[6]);
				aMotransaction.setCheckType((Short) aObj[7]);
				aMotransaction.setReference((String) aObj[8]);
				aMotransaction.setDescription((String) aObj[9]);
				aMotransaction.setVoid_((Byte) aObj[10]);
				aMotransaction.setReconciled((Byte) aObj[11]);
				aMotransaction.setTempRec((Byte) aObj[12]);
				aMotransaction.setPrinted((Byte) aObj[13]);
				aMotransaction.setAmount((BigDecimal) aObj[14]);
				aMotransaction.setWithDrawel((BigDecimal) aObj[14]);
				aMotransaction.setDirectDeposit((Byte) aObj[15]);
				
				
				aMotransaction.setBalance((BigDecimal) aObj[16]);
				aMotransaction.setStatus((Boolean)aObj[17]);
				aMotransaction.setMemo((String) aObj[18]);
				aMotransaction.setDisplaydiscription((String) aObj[21]);
				
				
				Date myDate = new Date();
				String futureornot="Current Transaction";
				if((Date)aObj[5]!=null){
				if(myDate.compareTo((Date)aObj[5])<0 || myDate.compareTo((Date)aObj[5])==0){
					futureornot="Future Transaction";
				}else{
					futureornot="Current Transaction";
				}
				}
				aMotransaction.setFutureorcurrent(futureornot);
				aQueryList.add(aMotransaction);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;	
	}
	
	@Override
	public Boolean checkisExists(String referenceNo,String moAccountID) throws BankingException {
		Session aSession = null;
		Boolean checkisExists = true;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM Motransaction where moAccountId="+moAccountID+" and reference="+referenceNo);
			checkisExists =  aQuery.list().isEmpty() ? true : false ;
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return checkisExists;
}

	@Override
	public ArrayList<BigDecimal> getNegativeAmountValidation() throws BankingException {

		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		int i=0,j=0;
		Integer[] rxMasterID =null;
		BigDecimal[] totalAmount=null;
		String rxMasterList =null,billArrayQuery =null;
		StringBuffer totalAmountQuery =null;
		ArrayList<BigDecimal> Amount = new ArrayList<BigDecimal>();
		try{
		billArrayQuery = "select veBillID from veBillPay";
		Query aQuery = aSession.createSQLQuery(billArrayQuery);
		List<?> billIds= aQuery.list();
		billId = new Integer[ aQuery.list().size()];
		for(i=0;i<billIds.size();i++)
			billId[i] = (Integer)billIds.get(i);
		rxMasterList = "SELECT BillPayUniqueIDs.rxMasterID FROM (SELECT rxMasterID FROM (SELECT veBill.rxMasterID FROM veBill LEFT JOIN veBillPay ON veBill.veBillID = veBillPay.veBillID WHERE veBillPay.ApplyingAmount<>0) AS BillPayIDs GROUP BY rxMasterID) AS BillPayUniqueIDs "
								+ "INNER JOIN rxMaster ON BillPayUniqueIDs.rxMasterID = rxMaster.rxMasterID ORDER BY Name+FirstName;";
		Query aQuery1 = aSession.createSQLQuery(rxMasterList);
		itsLogger.info(aQuery1.toString());
		List<?> rxMasterLists = aQuery1.list();
		rxMasterID = new Integer[ aQuery1.list().size()];
		for(i=0;i<rxMasterLists.size();i++){
			rxMasterID[i]=(Integer)rxMasterLists.get(i);
			
		}
		totalAmount = new BigDecimal[rxMasterID.length];
		for(i=0;i<rxMasterID.length;i++){
			totalAmountQuery = new StringBuffer("SELECT SUM(P.ApplyingAmount) AS CheckAmount FROM veBillPay AS P INNER JOIN veBill AS B ON P.veBillID = B.veBillID Where (B.rxMasterID =").append(rxMasterID[i]).append(")");
			Query aQuery2 = aSession.createSQLQuery(totalAmountQuery.toString());
			List<?> totalList= aQuery2.list();
				for( j =0; j<totalList.size();j++){
					if((BigDecimal)totalList.get(j)!=null)
					totalAmount[i] = (BigDecimal)totalList.get(j);
					
					Amount.add((BigDecimal)totalList.get(j));
					
				}	
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			aSession.flush();
			aSession.close();
			totalAmountQuery =null;rxMasterList =null;billArrayQuery =null;
		}
		
		return Amount;
	}

	@Override
	public ArrayList<Vebillpay> getCheckBillAvailability(Integer moAccountID,Integer userID)
			throws BankingException {
		// TODO Auto-generated method stub
		
		Session aSession = null;
		ArrayList<Vebillpay> billAvailability= new ArrayList<Vebillpay>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM Vebillpay where moAccountId="+moAccountID+" and userID ="+userID);
			billAvailability =(ArrayList<Vebillpay>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
		return billAvailability;
	}

	@Override
	public Integer bankingTrasactionsRollback(MoAccount moAccount,Motransaction aMotransaction,BigDecimal balance,String type,boolean accountStatus,Integer yearID,Integer periodID )throws BankingException, CompanyException {
		
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		aTransaction = aSession.beginTransaction();
		Integer motransiid=0;
		try {
			
			aTransaction.begin();
			
			updateDepositAndWithDraw(moAccount,aSession);
			aMotransaction.setBalance(balance);
			motransiid=addTransactionDetails(aMotransaction,aSession,"");
			aMotransaction.setMoTransactionId(motransiid);
			if(accountStatus)
			gltransactionService.bankingDeposits(aMotransaction,type,yearID,periodID,"",aSession);  
			
			aTransaction.commit();
			
		} catch (Exception e) {
			aSession.flush();
			aTransaction.rollback();
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
		return motransiid;
	}

	@Override
	public boolean deleteVeBillPayDetails(Integer MoAccountId)
			throws BankingException {
		Session aBankingdetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		MoAccount aMoaccount = new MoAccount();
		try {
			aTransaction = aBankingdetailSession.beginTransaction();
			aTransaction.begin();
			aBankingdetailSession.createSQLQuery("delete from veBillPay where moAccountID="+MoAccountId).executeUpdate();
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aBankingdetailSession.flush();
			aBankingdetailSession.close();
		}
		return false;
	}
	
	@Override
	public Integer getRxAddressIDfmRxmasterID(Integer theRxmasterId) throws CompanyException {
		itsLogger.debug("Getting RxAddressID");
		String aRxAddressQry = "SELECT rxAddressId FROM rxAddress WHERE rxMasterId="+theRxmasterId+ " and IsDefault = 1";
		Session aSession=null;
		Integer aRxaddress = 0;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRxAddressQry);
			List<?> aList = aQuery.list();
			if(aList.size()>0){
			aRxaddress = (Integer) aList.get(0);
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aRxAddressQry=null;
		}
		return aRxaddress;
	}

	@Override
	public Integer createtransactionBanknewCheck(MoAccount moAccount,
			Motransaction M1, String DepositType, Integer yearID,
			Integer periodID, String userName,BigDecimal balance,boolean accountStatus, int motransiid,String oper,String gridData,boolean futureornot,
			BigDecimal currentbalance,BigDecimal oldamount,String NewMoTypeId,BigDecimal amt,String aMemo,String aRxMasterID) throws BankingException, CompanyException {
		Transaction aTransaction;
		Session aSession=null;
		aSession=itsSessionFactory.openSession();	
		aTransaction = aSession.beginTransaction();
		try{
	
		aTransaction.begin();
		
		if(oper.equals("new"))
		{
			updateDepositAndWithDraw(moAccount,aSession);
			M1.setBalance(balance);
			motransiid=addTransactionDetails(M1,aSession,"new");
			M1.setMoTransactionId(motransiid);
			if(accountStatus)
			gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession); 
		}
		else if(oper.equals("add"))
		{
			Momultipleaccount momulaccount=null;
			BigDecimal TotalAmt=new BigDecimal(0);
			
			updateDepositAndWithDraw(moAccount,aSession);
			M1.setBalance(balance);
			motransiid=addTransactionDetails(M1,aSession,"new");
			M1.setMoTransactionId(motransiid);
			if(accountStatus)
			{
			gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession); 
			}
			else
			{
			
					JsonParser parser = new JsonParser();
					if (null != gridData && gridData.length() > 6) {
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (JsonElement ele1 : array) {
						
						momulaccount = new Momultipleaccount();
						JsonObject obj = ele1.getAsJsonObject();
						
						momulaccount.setCoAccountId(obj.get("coAccountId").getAsInt());				
						M1.setCoAccountId(obj.get("coAccountId").getAsInt());				
						
						momulaccount.setAmount(obj.get("amount").getAsBigDecimal());				
						M1.setAmount(obj.get("amount").getAsBigDecimal());
						
						momulaccount.setMoTransactionId(motransiid);
						momulaccount.setMoMultipleAccountsId(obj.get("moMultipleAccountsId").getAsInt());
						System.out.println("checkupdates"+momulaccount.getCoAccountId()+"=="+momulaccount.getAmount()+"=="+obj.get("moMultipleAccountsId").getAsInt());
						if(obj.get("moMultipleAccountsId").getAsInt()==0){
							saveGlmultipleAccount1(momulaccount,aSession);
						}else{
							updateGlmultipleAccount1(momulaccount,aSession);
						}
						DepositType = "Multiple with subaccounts";
						gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession); 
						
						TotalAmt = TotalAmt.add(obj.get("amount").getAsBigDecimal());
						}
						DepositType = "Multiple with mainaccounts";
						M1.setAmount(TotalAmt);
						gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession); 
					
					}
				
			}
		
		}
		else if(oper.equals("edit"))
		{
			Momultipleaccount momulaccount=null;
			BigDecimal TotalAmt=new BigDecimal(0);
		
			updateTransactionDetails1(M1,aSession);	
			Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("BT");
			
			GlRollback glRollback = new GlRollback();
			glRollback.setVeBillID(M1.getMoTransactionId());
			glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
			glRollback.setPeriodID(periodID);
			glRollback.setYearID(yearID);
			glRollback.setTransactionDate(M1.getTransactionDate());
			gltransactionService.rollBackGlTransaction1(glRollback, aSession);
			
			String aDescription = M1.getDescription(); 
			String aReference 	= M1.getReference();
			if(M1.getMoTransactionTypeId()==0){
				/*if(futureornot){
				balance=currentbalance.subtract(oldamount);
				}else{
				balance=currentbalance;
				}*/
				balance=currentbalance.subtract(oldamount);
				oldamount=oldamount.multiply(new BigDecimal(-1));
				short change=1;
				M1.setMoTransactionTypeId(change);
				moAccount.setSubtractions(oldamount); // 220
			}else{
			
				/*if(futureornot){
				balance=currentbalance.add(oldamount);
				}else{
					balance=currentbalance;
				}*/
				balance=currentbalance.add(oldamount);
				short change=0;
				M1.setMoTransactionTypeId(change);
				moAccount.setAdditions(oldamount);
			}
			moAccount.setMoTransactionTypeId(M1.getMoTransactionTypeId());
			updateDepositAndWithDraw(moAccount,aSession);
			
			M1.setAmount(oldamount);
			M1.setBalance(balance);
			
			addTransactionDetails(M1,aSession,"rollback");
			
			BigDecimal newbalance=new BigDecimal("0.00");
			short editmotypeid=Short.parseShort(NewMoTypeId);
			M1.setMoTransactionTypeId(editmotypeid);
			M1.setAmount(amt);
			M1.setStatus(false);
			if(M1.getMoTransactionTypeId()==0){
				if(M1.getAmount().doubleValue()<0){
					M1.setAmount(M1.getAmount().multiply(new BigDecimal(-1)));
				}
				/*if(futureornot){
					newbalance=balance.add(M1.getAmount());
					}else{
					newbalance=currentbalance;
					}*/
				newbalance=balance.add(M1.getAmount());
				moAccount.setAdditions(M1.getAmount());
				
			}else{
				if(M1.getAmount().doubleValue()>0){
					M1.setAmount(M1.getAmount().multiply(new BigDecimal(-1)));
				}
				/*if(futureornot){
					newbalance=balance.add(M1.getAmount());
					}else{
					newbalance=currentbalance;
					}*/
				newbalance=balance.add(M1.getAmount());
				moAccount.setSubtractions(M1.getAmount());
			}
			M1.setBalance(newbalance);
			moAccount.setMoTransactionTypeId(M1.getMoTransactionTypeId());
			updateDepositAndWithDraw(moAccount,aSession);
			
			if(editmotypeid==2)
			{
				M1.setMemo(aMemo);
				M1.setRxMasterId(Integer.parseInt(aRxMasterID));
			}
			
			M1.setDescription(aDescription);
			M1.setReference(aReference);
			motransiid=addTransactionDetails(M1,aSession,"new");				
			M1.setMoTransactionId(motransiid);
			if(accountStatus==true)
			{
			gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession);  
			}
			else
			{
				JsonParser parser = new JsonParser();
				if (null != gridData && gridData.length() > 6) {
					
				 if((oper.equals("edit") || oper.equals("delete")))
				 {
						
						glRollback.setVeBillID(M1.getMoTransactionId());
						glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
						glRollback.setPeriodID(periodID);
						glRollback.setYearID(yearID);
						glRollback.setTransactionDate(M1.getTransactionDate());
						
					 gltransactionService.rollBackGlTransaction1(glRollback, aSession);
				 }
				
					
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				System.out.println("array length==>"+array.size());
				for (JsonElement ele1 : array) {
					
					momulaccount = new Momultipleaccount();
					JsonObject obj = ele1.getAsJsonObject();
					
					momulaccount.setCoAccountId(obj.get("coAccountId").getAsInt());				
					M1.setCoAccountId(obj.get("coAccountId").getAsInt());				
					
					momulaccount.setAmount(obj.get("amount").getAsBigDecimal());				
					M1.setAmount(obj.get("amount").getAsBigDecimal());
					
					momulaccount.setMoTransactionId(motransiid);
					momulaccount.setMoMultipleAccountsId(obj.get("moMultipleAccountsId").getAsInt());
					System.out.println("checkupdates"+momulaccount.getCoAccountId()+"=="+momulaccount.getAmount()+"=="+obj.get("moMultipleAccountsId").getAsInt());
					if(obj.get("moMultipleAccountsId").getAsInt()==0){
						saveGlmultipleAccount1(momulaccount,aSession);
					}else{
						updateGlmultipleAccount1(momulaccount,aSession);
					}
					DepositType = "Multiple with subaccounts";
					gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession); 
					
					TotalAmt = TotalAmt.add(obj.get("amount").getAsBigDecimal());
					}
					DepositType = "Multiple with mainaccounts";
					M1.setAmount(TotalAmt);
					gltransactionService.bankingDeposits(M1,DepositType,yearID,periodID,userName,aSession); 
				
				}
			}
	
		}
		else if(oper.equals("delete"))
		{
			updateTransactionDetails1(M1,aSession);
			Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("BT");
			
			GlRollback glRollback = new GlRollback();
			glRollback.setVeBillID(M1.getMoTransactionId());
			glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
			glRollback.setPeriodID(periodID);
			glRollback.setYearID(yearID);
			glRollback.setTransactionDate(new Date());
			
			gltransactionService.rollBackGlTransaction1(glRollback, aSession);
			balance=new BigDecimal("0.00");
			if(M1.getMoTransactionTypeId()==0){
				if(M1.getAmount().doubleValue()<0){
					M1.setAmount(M1.getAmount().multiply(new BigDecimal(-1)));
				}
				moAccount.setSubtractions(M1.getAmount());
				/*if(futureornot){
					balance=currentbalance.add(M1.getAmount());
					}else{
					balance=currentbalance;
					}*/
				balance=currentbalance.add(M1.getAmount());
			}else{
				if(M1.getAmount().doubleValue()>0){
					M1.setAmount(M1.getAmount().multiply(new BigDecimal(-1)));
				}
				moAccount.setAdditions(M1.getAmount());
				/*if(futureornot){
					balance=currentbalance.add(M1.getAmount());
					}else{
					balance=currentbalance;
					}*/
				balance=currentbalance.add(M1.getAmount());
			}
			moAccount.setOper(oper);
			updateDepositAndWithDraw(moAccount,aSession);
			M1.setStatus(true);
			M1.setBalance(balance);
			addTransactionDetails(M1,aSession,"rollback");
		}
		else if(oper.equals("void"))
		{
			boolean statusChk = true;
			statusChk = voidTransactionDetails1(M1,aSession);
			
			GlRollback glRollback = new GlRollback();
			
			if(statusChk)
			{
			Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("WC");
			glRollback.setVeBillID(Integer.parseInt(M1.getMoAccountId()+""+M1.getReference()));
			glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
			glRollback.setPeriodID(periodID);
			glRollback.setYearID(yearID);
			glRollback.setTransactionDate(new Date());
			
			gltransactionService.rollBackGlTransaction1(glRollback, aSession);
			}
			else
			{
			Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("BT");
			
			glRollback.setVeBillID(M1.getMoTransactionId());
			glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
			glRollback.setPeriodID(periodID);
			glRollback.setYearID(yearID);
			glRollback.setTransactionDate(new Date());
			
			gltransactionService.rollBackGlTransaction1(glRollback, aSession);	
			}			
			
			balance=new BigDecimal("0.00");
			if(M1.getMoTransactionTypeId()==0){
				if(M1.getAmount().doubleValue()<0){
					M1.setAmount(M1.getAmount().multiply(new BigDecimal(-1)));
				}
				moAccount.setSubtractions(M1.getAmount());
				/*if(futureornot){
					balance=currentbalance.add(M1.getAmount());
					}else{
					balance=currentbalance;
					}*/
				balance=currentbalance.add(M1.getAmount());
				
			}else{
				if(M1.getAmount().doubleValue()<0){
					M1.setAmount(M1.getAmount().multiply(new BigDecimal(-1)));
				}
				moAccount.setAdditions(M1.getAmount());
				/*if(futureornot){
					balance=currentbalance.add(M1.getAmount());
					}else{
					balance=currentbalance;
					}*/
				balance=currentbalance.add(M1.getAmount());
			}
			moAccount.setOper(oper);
			updateDepositAndWithDraw(moAccount,aSession);
			M1.setStatus(false);
			M1.setBalance(balance);
			M1.setDescription("[VOID]"+M1.getDescription());
			M1.setUniquechkRef(M1.getReference());
			addTransactionDetails(M1,aSession,"rollbackfromvoid");
			
		}
		aTransaction.commit();

		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			aTransaction.rollback();
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
			
		} finally {
			aSession.flush();
			aSession.close();
		}
		return motransiid;
		
	}

	@Override
	public boolean insertReconcileFLStatus(
			ReconcileFLStatus theReconcileFLStatus) throws BankingException {
		
		Session aSession = itsSessionFactory.openSession();
		Motransaction aMotransaction = null;
		Transaction aTransaction;
		String query = null;
		List<ReconcileFLStatus> reFLList =null;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			query = "Select finishLaterID from reconcileFLStatus where moTransactionID="+theReconcileFLStatus.getMoTransactionID()+" and moAccountID ="+theReconcileFLStatus.getMoAccountID();
			Query aQuery = aSession.createSQLQuery(query);
			reFLList = aQuery.list();
			
			if(reFLList.size()>0)
			{
				aSession.createSQLQuery("update reconcileFLStatus set tempRecStatus ="+theReconcileFLStatus.getTempRecstatus()+" where moTransactionID="+theReconcileFLStatus.getMoTransactionID()+" and moAccountID ="+theReconcileFLStatus.getMoAccountID()).executeUpdate();
			}
			else
			{
				aSession.save(theReconcileFLStatus);
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
		
	}
