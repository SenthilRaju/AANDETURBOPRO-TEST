package com.turborep.turbotracker.banking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.banking.dao.GlAccountAmount;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.GlTransactionv1;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cobalance;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebilldetail;
import com.turborep.turbotracker.vendor.dao.Vebilldistribution;
import com.turborep.turbotracker.vendor.dao.Vebillpay;
import com.turborep.turbotracker.vendor.dao.Vepo;

@Service("gltransactionService")
@Transactional(rollbackFor={Exception.class})
public class GltransactionServiceImpl implements GltransactionService {
	Logger itsLogger = Logger.getLogger(BankingServiceImpl.class);

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name="inventoryService")
	InventoryService aInventoryService;

	@Override
	public Coaccount getcoAccountDetails(Motransaction amotransaction)
			throws BankingException {
		Coaccount aCoaccount = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT coAccount.coAccountID,coAccount.Number FROM coAccount JOIN moAccount ON(coAccount.coAccountId=moAccount.coAccountIDAsset) WHERE moAccount.moAccountId="
				+ amotransaction.getMoAccountId();
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aCoaccount = new Coaccount();
				aCoaccount.setCoAccountId((Integer) aObj[0]);
				aCoaccount.setNumber((String) aObj[1]);
			}
			// aCoaccount = (Coaccount) aSession.get(Coaccount.class,
			// amotransaction.getMoAccountId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aCoaccount;
	}

	@Override
	public SysAccountLinkage getsysAccountLinkageDetail() throws BankingException {
		SysAccountLinkage aSysAccountLinkage = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT coAccountIDAP,coAccountIDFreight,coAccountIDSalesTaxPaid,coAccountIDDiscountsTaken,coAccountIDAR,coAccountIDPayments,coAccountIDDiscounts,coAccountIDRetainedEarnings FROM sysAccountLinkage ORDER BY sysAccountLinkageID DESC LIMIT 1";
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {// coAccountIDAP coAccountIDFreight coAccountIDSalesTaxPaid
				// coAccountIDDiscountsTaken
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aSysAccountLinkage = new SysAccountLinkage();
				aSysAccountLinkage.setCoAccountIdap((Integer) aObj[0]);
				aSysAccountLinkage.setCoAccountIdfreight((Integer) aObj[1]);
				aSysAccountLinkage.setCoAccountIdsalesTaxPaid((Integer) aObj[2]);
				aSysAccountLinkage.setCoAccountIddiscountsTaken((Integer) aObj[3]);
				aSysAccountLinkage.setCoAccountIdar((Integer) aObj[4]);
				aSysAccountLinkage.setCoAccountIdpayments((Integer) aObj[5]);
				aSysAccountLinkage.setCoAccountIddiscounts((Integer) aObj[6]);
				aSysAccountLinkage.setCoAccountIdretainedEarnings((Integer) aObj[7]);
			}
			// aCoaccount = (Coaccount) aSession.get(Coaccount.class,
			// amotransaction.getMoAccountId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aSysAccountLinkage;
	}

	@Override
	public ArrayList<Vebill> getveBillDetailsList(Motransaction amotransaction)
			throws BankingException {
		String addressQuery =null;
		Session aSession = itsSessionFactory.openSession();
		ArrayList<Vebill> Vebilllist = new ArrayList<Vebill>();
		ArrayList<Vebillpay> vebillpaylist = getveBillpaydetails(amotransaction);
		try {
			for (Vebillpay avebillpay : vebillpaylist) {
				addressQuery = "SELECT veBillID,BillAmount,TaxAmount,FreightAmount,AppliedAmount FROM veBill WHERE veBillID="
						+ avebillpay.getVeBillId();
				Query aQuery = aSession.createSQLQuery(addressQuery);
				// coAccountIDAP coAccountIDFreight coAccountIDSalesTaxPaid
				// coAccountIDDiscountsTaken
				Iterator<?> aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					Vebill aVebill = new Vebill();
					Object[] aObj = (Object[]) aIterator.next();
					aVebill.setVeBillId((Integer) aObj[0]);
					aVebill.setBillAmount((BigDecimal) aObj[1]);
					aVebill.setTaxAmount((BigDecimal) aObj[2]);
					aVebill.setFreightAmount((BigDecimal) aObj[3]);
					aVebill.setAppliedAmount(avebillpay.getApplyingAmount());
					// aVebill.setAppliedAmount((BigDecimal) aObj[4]);
					Vebilllist.add(aVebill);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}

		return Vebilllist;
	}

	public ArrayList<Vebillpay> getveBillpaydetails(Motransaction amotransaction)
			throws BankingException {
		Vebillpay aVebillpay = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "select veBillID,veBillPayID,ApplyingAmount from veBillPay";
		ArrayList<Vebillpay> vebilllist = new ArrayList<Vebillpay>();
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {// coAccountIDAP coAccountIDFreight coAccountIDSalesTaxPaid
				// coAccountIDDiscountsTaken
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aVebillpay = new Vebillpay();
				aVebillpay.setVeBillId((Integer) aObj[0]);
				aVebillpay.setApplyingAmount((BigDecimal) aObj[2]);
				vebilllist.add(aVebillpay);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return vebilllist;
	}

	@Override
	public Cofiscalperiod getCofiscalPeriodDetail()
			throws BankingException {
		Cofiscalperiod aCofiscalperiod = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT cf.coFiscalPeriodID,cf.period,cf.StartDate,cf.EndDate FROM coFiscalPeriod cf JOIN sysInfo si ON cf.coFiscalPeriodID = si.CurrentPeriodID ORDER BY cf.coFiscalPeriodID DESC";
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {// coAccountIDAP coAccountIDFreight coAccountIDSalesTaxPaid
				// coAccountIDDiscountsTaken
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aCofiscalperiod = new Cofiscalperiod();
				aCofiscalperiod.setCoFiscalPeriodId((Integer) aObj[0]);
				aCofiscalperiod.setPeriod((Integer) aObj[1]);
				aCofiscalperiod.setStartDate((Date) aObj[2]);
				aCofiscalperiod.setEndDate((Date) aObj[3]);
			}
			// aCoaccount = (Coaccount) aSession.get(Coaccount.class,
			// amotransaction.getMoAccountId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aCofiscalperiod;
	}

	@Override
	public Cofiscalyear getCofiscalYearDetail()
			throws BankingException {
		Cofiscalyear aCofiscalyear = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT cy.coFiscalYearID,cy.StartDate,cy.EndDate,cy.fiscalYear FROM coFiscalYear cy JOIN sysInfo si ON cy.coFiscalYearID = si.CurrentFiscalYearID  ORDER BY cy.coFiscalYearID DESC";
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {// coAccountIDAP coAccountIDFreight coAccountIDSalesTaxPaid
				// coAccountIDDiscountsTaken
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aCofiscalyear = new Cofiscalyear();
				aCofiscalyear.setCoFiscalYearId((Integer) aObj[0]);
				
				
				aCofiscalyear.setStartDate((Date) aObj[1]);
				aCofiscalyear.setEndDate((Date) aObj[2]);
				aCofiscalyear.setFiscalYear((String) aObj[3]);
			}
			// aCoaccount = (Coaccount) aSession.get(Coaccount.class,
			// amotransaction.getMoAccountId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aCofiscalyear;
	}

	@Override
	public Coledgersource getColedgersourceDetail(String JournalID) throws BankingException {
		Coledgersource aColedgersource = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT coLedgerSourceID,Description,JournalID FROM coLedgerSource WHERE  JournalID='"
				+ JournalID + "'";
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {// coAccountIDAP coAccountIDFreight coAccountIDSalesTaxPaid
				// coAccountIDDiscountsTaken
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aColedgersource = new Coledgersource();
				aColedgersource.setCoLedgerSourceId((Integer) aObj[0]);
				aColedgersource.setDescription((String) aObj[1]);
				aColedgersource.setJournalID((String) aObj[2]);
			}
			// aCoaccount = (Coaccount) aSession.get(Coaccount.class,
			// amotransaction.getMoAccountId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aColedgersource;
	}

	/*
	 * Created By:VelmuruganCreated On:10-09-2014Description:Add data in
	 * gltransaction table
	 */
	@Override
	public boolean saveGlTransactionTable(Motransaction aMotransaction,
			Coaccount coaccount, SysAccountLinkage aSysAccountLinkage,
			ArrayList<Vebill> vebilllist, Cofiscalperiod aCofiscalperiod,
			Cofiscalyear aCofiscalyear, Coledgersource aColedgersource)
			throws BankingException {
		Session aMomulAccountSession = itsSessionFactory.openSession();
		GlTransactionv1 gltransaction = new GlTransactionv1();
		Transaction aTransaction;
		try {
			aTransaction = aMomulAccountSession.beginTransaction();
			aTransaction.begin();
			aMomulAccountSession.save(gltransaction);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(
					excep.getMessage(), excep);
			throw aBankingException;
		} finally {
			aMomulAccountSession.flush();
			aMomulAccountSession.close();
		}
		return true;
	}

	@Override
	public Coaccount getCoaccountDetailsBasedoncoAccountid(int coAccountId)
			throws BankingException {
		Coaccount aCoaccount = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT coAccountID,Number,Description FROM coAccount  WHERE coAccountID="
				+ coAccountId;
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aCoaccount = new Coaccount();
				aCoaccount.setCoAccountId((Integer) aObj[0]);
				aCoaccount.setNumber((String) aObj[1]);
				aCoaccount.setDescription((String) aObj[2]);
			}
			// aCoaccount = (Coaccount) aSession.get(Coaccount.class,
			// amotransaction.getMoAccountId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aCoaccount;
	}

	
	@SuppressWarnings("finally")
	@Override
	public int saveGltransactionTable(GlTransaction glTransactionobj)
			throws BankingException {
		Session aMomulAccountSession = itsSessionFactory.openSession();
		Transaction aTransaction =null; 
		int getTransactionId = -1;

		try {
			aTransaction = aMomulAccountSession.beginTransaction();
			aTransaction.begin();
			aMomulAccountSession.save(glTransactionobj);
			aTransaction.commit(); 

			getTransactionId = glTransactionobj.getGlTransactionId();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(
					excep.getMessage(), excep);
			throw aBankingException;

		} finally {
			aMomulAccountSession.flush();
			aMomulAccountSession.close();
			return getTransactionId;
		}

	}

	/*
	 * Created By:LeoCreated On:11-09-2014Description:Save glLinkage data in
	 * glLinkage table based upon momultipleaccountid
	 */
	@Override
	public boolean saveGlLinkageTable(GlLinkage glLinkageobj)
			throws BankingException {
		Session aMomulAccountSession = itsSessionFactory.openSession();
		Transaction aTransaction =null; 
		try {
			aTransaction = aMomulAccountSession.beginTransaction();
			aTransaction.begin();
			aMomulAccountSession.save(glLinkageobj);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(
					excep.getMessage(), excep);
			throw aBankingException;

		} finally {
			aMomulAccountSession.flush();
			aMomulAccountSession.close();
		}
		return true;
	}
	
	@SuppressWarnings("finally")
	@Override
	public int saveGltransactionTable1(GlTransaction glTransactionobj,Session aMomulAccountSession)
			throws BankingException {

		int getTransactionId = -1;

		try {
			aMomulAccountSession.save(glTransactionobj);
			getTransactionId = glTransactionobj.getGlTransactionId();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(
					excep.getMessage(), excep);
			throw aBankingException;

		} 
		finally
		{
			aMomulAccountSession.flush();
			aMomulAccountSession.clear();
		}
				
		return getTransactionId;
	}


	/*
	 * Created By:LeoCreated On:11-09-2014Description:Save glLinkage data in
	 * glLinkage table based upon momultipleaccountid
	 */
	

	@Override
	public boolean saveGlLinkageTable1(GlLinkage glLinkageobj,Session aMomulAccountSession)
			throws BankingException {
		//Session aMomulAccountSession = itsSessionFactory.openSession();

		try {
		//	aTransaction = aMomulAccountSession.beginTransaction();
		//	aTransaction.begin();
			aMomulAccountSession.save(glLinkageobj);
		//	aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aBankingException = new BankingException(
					excep.getMessage(), excep);
			throw aBankingException;

		} 
		finally
		{
			aMomulAccountSession.flush();
			aMomulAccountSession.clear();
		}
		return true;
	}
	
	
	/*
	 * Created By:LeoCreated On:23-09-2014Description:GlTransaction Table insert
	 */

	@Override
	public Vepo getPOnumberfromvePOID(Integer vePOId) throws BankingException {

		Vepo aVepo = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT vePOID,PONumber FROM vePO  WHERE vePOID="
				+ vePOId;
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aVepo = new Vepo();
				aVepo.setVePoid((Integer) aObj[0]);
				aVepo.setPonumber((String) aObj[1]);

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
		return aVepo;
	}
	
	
	@Override
	public String getRecieptType(Integer type) throws  BankingException {
		Session aSession = null;
		String recieptTypeList = null;
		Query query =null;
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery("select Description from cuReceiptType where cuReceiptTypeId="+type);
			recieptTypeList =(String) query.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			query=null;
		}
		return recieptTypeList;
	}

	@Override
	public Rxmaster getTransactionDescriptionfromrxMasterID(Integer rxMasterID)
			throws BankingException {
		Rxmaster arxMaster = null;
		Session aSession = itsSessionFactory.openSession();
		String addressQuery = "SELECT rxMasterID,Name FROM rxMaster WHERE rxMasterID="
				+ rxMasterID;
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				arxMaster = new Rxmaster();
				
				arxMaster.setRxMasterId((Integer)aObj[0]);
				arxMaster.setName((String) aObj[1]);

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
			aQuery =null;
		}
		return arxMaster;
	}
	
	

	public GlAccountAmount getpurchasesAccoucntNo(Integer vebillDetID)
			throws BankingException {
		GlAccountAmount coAccountObj = null;
		Session aSession = itsSessionFactory.openSession();	
		
	/*	String addressQuery = "SELECT co.coaccountID,co.number FROM coAccount co,prDepartment prd,veBill ve,veBillDetail ved,prMaster prm"
				+ " WHERE co.coAccountID=prd.coAccountIDSales AND prd.prDepartmentID = prm.prDepartmentID"
				+ " AND ved.veBillDetailID = '"+vebillDetID+"' LIMIT 1";*/
		
		
		String addressQuery ="SELECT  SUM(ved.`QuantityBilled`*ved.`UnitCost`*ved.`PriceMultiplier`),ca.Number,ca.coAccountID FROM veBill veb "
				+ " JOIN veBillDetail ved ON veb.veBillID = ved.veBillID JOIN prMaster pr ON ved.prMasterID= pr.prMasterID JOIN prDepartment prd ON pr.prDepartmentID = prd.prDepartmentID "
				+ " JOIN coAccount ca ON ca.`coAccountID` = prd.`coAccountIDSales` WHERE veb.veBillID= "+vebillDetID+" GROUP BY prd.prDepartmentID,ca.Number,ca.coAccountID";
	
		Query aQuery = aSession.createSQLQuery(addressQuery);
		try {
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				coAccountObj = new GlAccountAmount();
				
				
				coAccountObj.setQuantityBilled((BigDecimal) aObj[0]);
				coAccountObj.setCoAccountNumber((String) aObj[3]);
				coAccountObj.setCoAccountid((Integer) aObj[4]);

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
			aQuery=null;
		}
		return coAccountObj;
	}
	
	@Override
	public boolean postCustomerPaymentDetails(Cureceipt aCureceipt,BigDecimal DiscountAmt,Integer yearID,Integer periodID,String userName,Session aSession)throws BankingException, CompanyException {
		SysAccountLinkage aSysAccountLinkage = getsysAccountLinkageDetail();
		itsLogger.info("CoAccountIdar = "+ aSysAccountLinkage.getCoAccountIdar()+ "\nCoAccountIdPayments = "+ aSysAccountLinkage.getCoAccountIdpayments());

		Coaccount CoAccountIdardetails        = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIdar());
		Coaccount CoAccountIdPaymentdetails   = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIdpayments());
		Coaccount CoAccountIdDiscountsdetails = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIddiscounts());


		//Cofiscalperiod aCofiscalperiod = getCofiscalPeriodDetail(aMotransaction);
		//Cofiscalyear aCofiscalyear = getCofiscalYearDetail(aMotransaction);
		//** Get Periods Details based on payment dates  
	
		itsLogger.info("GltransactionServiceImpl.postCusInvoiceDetails()::"+aCureceipt.getReceiptDate());
		//Cofiscalperiod aCofPeriod = getAllcoFiscalPeriodbasedongivenDate(aCureceipt.getReceiptDate(),aCofiscalyear.getCoFiscalYearId());
		
		GlTransaction glTransaction = new GlTransaction();
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Cofiscalperiod aCofPeriod = accountingCyclesService.getCurrentPeriod(periodID);
		Coledgersource aColedgersource = getColedgersourceDetail("CP");
		
		glTransaction.setCoFiscalPeriodId(aCofPeriod.getCoFiscalPeriodId());
		glTransaction.setPeriod(aCofPeriod.getPeriod());
		glTransaction.setpStartDate(aCofPeriod.getStartDate());
		glTransaction.setpEndDate(aCofPeriod.getEndDate());
		
		glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
		if(aCofiscalyear.getFiscalYear()!=null)
		glTransaction.setFyear(aCofiscalyear.getFiscalYear());
		glTransaction.setyStartDate(aCofiscalyear.getStartDate());
		glTransaction.setyEndDate(aCofiscalyear.getEndDate());
		glTransaction.setJournalId(aColedgersource.getJournalID());
		glTransaction.setJournalDesc(aColedgersource.getDescription());
		glTransaction.setEntrydate(new Date());
		glTransaction.setEnteredBy(userName);
		glTransaction.setTransactionDate(new Date());
		
		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(aCureceipt
				.getRxCustomerId());
		
		String type = getRecieptType(aCureceipt.getCuReceiptTypeId().intValue());
		
		if(!aCureceipt.getReference().equals(""))
		{
		glTransaction.setPoNumber(type==null?"":type+":"+aCureceipt.getReference());
		}
		else
		{
		glTransaction.setPoNumber(type==null?"":type+":"+aCureceipt.getMemo());	
		}
		
		if(liRxmasters!=null)
		glTransaction.setTransactionDesc(liRxmasters.getName());
		
		BigDecimal dBorCrAmount= new BigDecimal(0);
		
		int glTransationid = 0;
		
		//Credit Side
		itsLogger.info("AccountPayable Accountid="+CoAccountIdardetails.getCoAccountId()+" AccountNumber="+CoAccountIdardetails.getNumber());
		glTransaction.setCoAccountId(CoAccountIdardetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdardetails.getNumber());
		glTransaction.setDebit( dBorCrAmount);
		if(DiscountAmt.compareTo(BigDecimal.ZERO)!=0)
		glTransaction.setCredit(aCureceipt.getAmount().add(DiscountAmt));
		else
		glTransaction.setCredit(aCureceipt.getAmount());	
		
		if (aCureceipt.getAmount() != null && aCureceipt.getAmount().compareTo(BigDecimal.ZERO)!=0) {
			glTransationid = insertPayments(glTransaction,aColedgersource,aCureceipt.getCuReceiptId(),aSession);
			itsLogger.info("Insertion Success: " +glTransationid);
		}
	
		//Debit Side
		itsLogger.info("AccountPayable Accountid="+CoAccountIdPaymentdetails.getCoAccountId()+" AccountNumber="+CoAccountIdPaymentdetails.getNumber());
		glTransaction.setCoAccountId(CoAccountIdPaymentdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdPaymentdetails.getNumber());
		glTransaction.setDebit(aCureceipt.getAmount());
//		}
		glTransaction.setCredit(dBorCrAmount);
		if (aCureceipt.getAmount()!= null && aCureceipt.getAmount().compareTo(BigDecimal.ZERO)!=0) {
			glTransationid=insertPayments(glTransaction,aColedgersource,aCureceipt.getCuReceiptId(),aSession);
			itsLogger.info("Insertion Success: " +glTransationid);
		}
		
		//Discount Applied
		if(DiscountAmt.compareTo(BigDecimal.ZERO)!=0)
		{
			glTransaction.setCoAccountId(CoAccountIdDiscountsdetails.getCoAccountId());
			glTransaction.setCoAccountNumber(CoAccountIdDiscountsdetails.getNumber());
			glTransaction.setDebit( DiscountAmt);
			glTransaction.setCredit(dBorCrAmount);
			if (DiscountAmt != null && aCureceipt.getAmount().compareTo(BigDecimal.ZERO)!=0) {
				glTransationid=insertPayments(glTransaction,aColedgersource,aCureceipt.getCuReceiptId(),aSession);
				itsLogger.info("Insertion Success: " +glTransationid);
			}
		}
		
		return true;
		
	}
	
	
	@Override
	public int postCusInvoiceDetails(Cuinvoice aCuinvoice,Cureceipt aCureceipt,Integer yearID,Integer periodID,UserBean aUserBean,Session aSession)	throws BankingException, CompanyException {
		
		SysAccountLinkage aSysAccountLinkage = getsysAccountLinkageDetail();
		BigDecimal dBorCrAmount= new BigDecimal(0);
		int glTransationid = 0;
		itsLogger.info("CoAccountIdar = "+ aSysAccountLinkage.getCoAccountIdar()+ "\nCoAccountIdPayments = "+ aSysAccountLinkage.getCoAccountIdpayments());

		Coaccount CoAccountIdardetails        = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIdar());
		Coaccount CoAccountIdPaymentdetails   = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIdpayments());
		Coaccount CoAccountIdDiscountsdetails = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIddiscounts());

		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Cofiscalperiod aCofiscalperiod = accountingCyclesService.getCurrentPeriod(periodID);
		Coledgersource aColedgersource = getColedgersourceDetail("CP");
		
		GlTransaction glTransaction = new GlTransaction();
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
		glTransaction.setPeriod(aCofiscalperiod.getPeriod());
		glTransaction.setpStartDate(aCofiscalperiod.getStartDate());
		glTransaction.setpEndDate(aCofiscalperiod.getEndDate());
		glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
		if(aCofiscalyear.getFiscalYear()!=null)
		glTransaction.setFyear(aCofiscalyear.getFiscalYear());
		glTransaction.setyStartDate(aCofiscalyear.getStartDate());
		glTransaction.setyEndDate(aCofiscalyear.getEndDate());
		glTransaction.setJournalId(aColedgersource.getJournalID());
		glTransaction.setJournalDesc(aColedgersource.getDescription());
		glTransaction.setEntrydate(new Date());
		glTransaction.setTransactionDate(new Date());
		glTransaction.setEnteredBy(aUserBean.getFullName());
		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(aCuinvoice.getRxCustomerId());
		String type = getRecieptType(aCureceipt.getCuReceiptTypeId().intValue());
		if(!aCureceipt.getReference().equals(""))
		glTransaction.setPoNumber(type+":"+aCureceipt.getReference());
		else
		glTransaction.setPoNumber(type+":"+aCureceipt.getMemo());	
		
		if(liRxmasters!=null)
		glTransaction.setTransactionDesc(liRxmasters.getName());
	
				//Discount Insert
				if( aCuinvoice.getDiscountApplied() !=null && aCuinvoice.getDiscountApplied().compareTo(new BigDecimal(0.00))==1)
		        {
					//Credit Side Discount Insert
					glTransaction.setCoAccountId(CoAccountIdardetails.getCoAccountId());
					glTransaction.setCoAccountNumber(CoAccountIdardetails.getNumber());
					glTransaction.setDebit( dBorCrAmount);
			   		glTransaction.setCredit(aCuinvoice.getDiscountApplied());
					if (aCuinvoice.getDiscountApplied() != null) {
						glTransationid = insertPayments(glTransaction,aColedgersource,aCuinvoice.getCuInvoiceId(),aSession);
						itsLogger.info("Insertion Success: " +glTransationid);
					}
					
					//Debit Side Discount Insert
					System.out.println("Discount Accountid="+CoAccountIdDiscountsdetails.getCoAccountId()+" AccountNumber="+CoAccountIdDiscountsdetails.getNumber());
					glTransaction.setCoAccountId(CoAccountIdDiscountsdetails.getCoAccountId());
					glTransaction.setCoAccountNumber(CoAccountIdDiscountsdetails.getNumber());
					glTransaction.setDebit( aCuinvoice.getDiscountApplied());
					glTransaction.setCredit(dBorCrAmount);
					if (aCuinvoice.getDiscountApplied() != null) {
						glTransationid=insertPayments(glTransaction,aColedgersource,aCuinvoice.getCuInvoiceId(),aSession);
						itsLogger.info("Insertion Success: " +glTransationid);
					}
		        }
		return glTransationid;
	}
	
	
	
	
	
	public int insertPayments(GlTransaction aglTransaction,Coledgersource aColedgersource,int CuReceiptId,Session aSession) throws BankingException 
	{
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
				
		if(aglTransaction.getCredit().compareTo(BigDecimal.ZERO)<0)
		{
			aglTransaction.setDebit(aglTransaction.getCredit().negate());
			aglTransaction.setCredit(BigDecimal.ZERO);
		}
		else if(aglTransaction.getDebit().compareTo(BigDecimal.ZERO)<0)
		{
			aglTransaction.setCredit(aglTransaction.getDebit().negate());
			aglTransaction.setDebit(BigDecimal.ZERO);
		}
			
		
		glTransationid = saveGltransactionTable1(aglTransaction,aSession);
		itsLogger.info("insert glLinkagecoLedgerSourceId:"+ aColedgersource.getCoLedgerSourceId()+ "\nGlTransactionid:" + glTransationid + "\nVeBillid: "+aglTransaction.getPoNumber());

		glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
		glLinkage.setGlTransactionId(glTransationid);
		glLinkage.setEntryDate(new Date());
		glLinkage.setVeBillID(CuReceiptId);
		glLinkage.setStatus(0);
		saveGlLinkageTable1(glLinkage,aSession);		
	
		return glTransationid;
	}	
	
	
	@Override
	public void recieveVendorBill(List<Prmaster> prMaster,Vebill veBillObj, Integer veBillDetailObj,Integer yearID,Integer periodID,String username)
			throws BankingException, CompanyException{


		SysAccountLinkage aSysAccountLinkage = getsysAccountLinkageDetail();
		Coaccount CoAccountIdapdetails = getCoaccountDetailsBasedoncoAccountid(veBillObj.getApaccountId());
		Coaccount CoAccountIdfreightdetails = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage
				.getCoAccountIdfreight());
		Coaccount CoAccountIdsalesTaxPaiddetails = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage
				.getCoAccountIdsalesTaxPaid());
		
		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(veBillObj
				.getRxMasterId());

		//Vepo liVepo = getPOnumberfromvePOID(veBillObj.getVePoid());
		//itsLogger.info("PONumber== " + liVepo.getPonumber()
		//		+ "TransactionDescription == " + liRxmasters.getName());
		
		
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = getColedgersourceDetail("VB");

		GlTransaction glTransaction = new GlTransaction();

		// period
		
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
		
		
		if(veBillObj.getBillDate()!=null)
		{
			
			// Commented by leo :  Reason: Period must be get from coFiscalPeriod.
			
		/*	Calendar difcurdate = Calendar.getInstance();
			difcurdate.setTime(veBillObj.getBillDate());
			int period = difcurdate.get(Calendar.MONTH);
			itsLogger.info("Period:"+period +"Add 1 :"+(period+1));
			glTransaction.setPeriod(period+1); */
			Cofiscalperiod getperiod =  accountingCyclesService.getAllOpenPeriods(veBillObj.getBillDate());
			glTransaction.setPeriod(getperiod.getPeriod());
			
		}
		else
		{
			glTransaction.setPeriod(aCofiscalperiod.getPeriod());
		}
		
		
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
		
		glTransaction.setTransactionDate(veBillObj.getBillDate());
		glTransaction.setEnteredBy(username);

		// Ponumber and trDesc
		
		/*if(liVepo!=null){
			if(veBillObj.getInsorouts()!=null && veBillObj.getInsorouts().equals("INS_JOB")){
				glTransaction.setPoNumber(liVepo.getPonumber()+veBillObj.getShippingrownum());
			}
			else if (veBillObj.getInsorouts()!=null && veBillObj.getInsorouts().equals("INS_JOBEdit")){
				glTransaction.setPoNumber(getReferencenofromglTransaction(veBillObj.getVeBillId(),aColedgersource.getCoLedgerSourceId()));
			}
			else{
				glTransaction.setPoNumber(liVepo.getPonumber());
			}
		}*/
		
		glTransaction.setPoNumber(veBillObj.getInvoiceNumber());
		
		glTransaction.setTransactionDesc(liRxmasters.getName());
		
		BigDecimal dBorCrAmount= new BigDecimal(0);
		
		boolean frieghtStatus=false;
		

		/* Account Payable */
		
		insertAccountPayable(glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount);
		
		/* Purchases */

		
		if(veBillObj.getJoReleaseDetailId()!=null && veBillObj.getJoReleaseDetailId()!=0)
		frieghtStatus = insertPurchases(null,null,glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount,"InsideJob");
		else{
		/*
		 * Modified By Simon
		 * Reason for Modifying is ID#573 for differentiating purchases
		 */
		if(prMaster.size()!=0){
			/*for(Prmaster master:prMaster){			
				frieghtStatus = insertPurchases(master,glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount,"OutsideJob");
			}	*/
			Map<String,List<Prmaster>> partitionedInventories=partitionInventories(prMaster);
			if(partitionedInventories.get("Inventory").size()==0){
				Map<String,Object> purchaseValues=new HashMap<String, Object>();
				if(prMaster.get(0).getInitialCost().compareTo(BigDecimal.ZERO)!=0){
					purchaseValues.put("amount", prMaster.get(0).getInitialCost());
					frieghtStatus = insertPurchases("freightType",purchaseValues,glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount,"OutsideJob");
				}
			}
			for(Map.Entry<String, List<Prmaster>> entry:partitionedInventories.entrySet()){
				if(entry.getKey().equals("Inventory")==true){
					String inventoryType=entry.getKey();
					if(entry.getValue().size()!=0){
						Map<String,Object> purchaseValues=initPurchases(entry.getValue());
						double freightAmount=entry.getValue().get(0).getInitialCost().doubleValue();
						double amount=((BigDecimal)purchaseValues.get("amount")).doubleValue();
						amount+=freightAmount;
						purchaseValues.put("amount", BigDecimal.valueOf(amount));
						 if(amount!=0)
						frieghtStatus = insertPurchases(inventoryType,purchaseValues,glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount,"OutsideJob");
					}
				}
				if(entry.getKey().equals("nonInventory")==true){
					String inventoryType=entry.getKey();
					Map<Integer,List<Prmaster>> partitionedNonInventories=partitionNonInventories(entry.getValue());
					if(partitionedNonInventories.size()!=0){
						for(Map.Entry<Integer, List<Prmaster>> pnEntry:partitionedNonInventories.entrySet()){
							if(pnEntry.getValue().size()!=0){
								Map<String,Object> initPurchases=initPurchases(pnEntry.getValue());
						        double amount=((BigDecimal)initPurchases.get("amount")).doubleValue();
						        if(amount!=0)
								frieghtStatus = insertPurchases(inventoryType,initPurchases(pnEntry.getValue()),glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount,"OutsideJob");
							}
						}
					}
				}
			}
		}else{
			frieghtStatus = insertPurchases(null,null,glTransaction, CoAccountIdapdetails, veBillObj,aColedgersource,dBorCrAmount,"OutsideJob");			
		}
		}

		/* Sales Tax */
		
		
		insertSalesTax(glTransaction, CoAccountIdsalesTaxPaiddetails, veBillObj,aColedgersource,dBorCrAmount);		

		/* Freight */

		
		if(frieghtStatus==true)
		insertFrieght(glTransaction,CoAccountIdfreightdetails, veBillObj,aColedgersource,dBorCrAmount);
		
	}
	
	@Override
	public void startTransaction() throws BankingException
	{

		
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.createSQLQuery("start transaction").executeUpdate();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
			
	}
	@Override
	public void startRollBack() throws BankingException
	{

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.createSQLQuery("rollback").executeUpdate();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
			
	}
	
	@Override
	public void startCommit() throws BankingException
	{

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.createSQLQuery("commit").executeUpdate();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
			
	}
	


	public void insertAccountPayable(GlTransaction glTransaction,
			Coaccount CoAccountIdapdetails, Vebill aVebill,
			Coledgersource aColedgersource,BigDecimal dBorCrAmount) throws BankingException {
		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;
		
		
		itsLogger.info("*************Acccount Payable Starts Here**************");

		// AccountPayable
		
		itsLogger.info("AccountPayable Accountid="+CoAccountIdapdetails.getCoAccountId()+" AccountNumber="+CoAccountIdapdetails.getNumber());

		glTransaction.setCoAccountId(CoAccountIdapdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdapdetails.getNumber());

		// AccountPayable Amount

		
		BigDecimal amtValueforaccpayable = new BigDecimal(0);
		BigDecimal accPayableAmt=new BigDecimal(0);
		BigDecimal quantity=new BigDecimal(0);
		BigDecimal unitcost=new BigDecimal(0);
		BigDecimal pricemul=new BigDecimal(0);
		
		
		ArrayList<Vebilldetail> vedetList= getveBillDetailsIDs(aVebill.getVeBillId());
		
		for(Vebilldetail veDetObj:vedetList)
		{
			/*FloorFigureOverAll Change
			 * 29-10-2015
			 * */
			if(veDetObj.getQuantityBilled()!=null)
				quantity =JobUtil.floorFigureoverall(veDetObj.getQuantityBilled(),2);
			if(veDetObj.getUnitCost()!=null)
				unitcost =JobUtil.floorFigureoverall(veDetObj.getUnitCost(),2);
				
			if(veDetObj.getPriceMultiplier()!=null && veDetObj.getPriceMultiplier().compareTo( BigDecimal.ZERO) != 0)
				pricemul =veDetObj.getPriceMultiplier();
			else
				pricemul = new BigDecimal(1);
			
			accPayableAmt = accPayableAmt.add(JobUtil.floorFigureoverall((quantity.multiply(unitcost).multiply(pricemul)),2));
		}
		
		amtValueforaccpayable = accPayableAmt.add(aVebill.getFreightAmount()).add(aVebill.getTaxAmount());
		accPayableAmt = accPayableAmt.add(amtValueforaccpayable);
		if(amtValueforaccpayable.signum()>0)
		{ 
		glTransaction.setDebit( dBorCrAmount);
		glTransaction.setCredit(amtValueforaccpayable);
		}
		else
		{
		glTransaction.setDebit(amtValueforaccpayable.negate());
		glTransaction.setCredit(dBorCrAmount);
		}
		
		

	if (accPayableAmt.compareTo( BigDecimal.ZERO) != 0) {
			glTransationid = saveGltransactionTable(glTransaction);

			itsLogger.info("coLedgerSourceId == "
					+ aColedgersource.getCoLedgerSourceId()
					+ "GlTransactionid == " + glTransationid + "VeBillid == "
					+ aVebill.getVeBillId());

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aVebill.getVeBillId());
			glLinkage.setStatus(0);
			saveGlLinkageTable(glLinkage);
			
			

		}
	
	itsLogger.info("*************Acccount Payable Ends Here**************");

	}
	
	public boolean insertPurchases(String inventoryType,Map<String,Object> purchaseValues,GlTransaction glTransaction,
			Coaccount CoAccountIdapdetails, Vebill aVebill,
			Coledgersource aColedgersource,BigDecimal dBorCrAmount,String poStatus) throws BankingException {

		GlLinkage glLinkage = new GlLinkage();
		boolean frieghtStatus=false;
		int glTransationid = 0;
		
		itsLogger.info("*************Purchases Starts Here**************");

		// Purchases
		
					ArrayList<GlAccountAmount> vbilldetailslist=getveBillDetailsID(aVebill.getVeBillId(),poStatus);
					 
					for(GlAccountAmount vebilldetObj:vbilldetailslist)
					{
						/*
						 *Created By Simon
						 *Reason for creating : ID#573 (for verifying whether a product belongs to non-inventory or inventory)  
						 *14 lines added from following if condition.
						 */
						if(purchaseValues!=null && inventoryType.equals("nonInventory")){
							glTransaction.setCoAccountId((Integer)purchaseValues.get("coAccountId"));
							glTransaction.setCoAccountNumber((String)purchaseValues.get("coAccountNumber"));
						}else if(inventoryType!=null && inventoryType.equals("freightType")){
							Coaccount coAccount=getFreightAccount();
							if(coAccount!=null){
								glTransaction.setCoAccountId(coAccount.getCoAccountId());
								glTransaction.setCoAccountNumber(coAccount.getNumber());
							}else{
								glTransaction.setCoAccountId(vebilldetObj.getCoAccountid());
								glTransaction.setCoAccountNumber(vebilldetObj.getCoAccountNumber());
							}
						}else{
							glTransaction.setCoAccountId(vebilldetObj.getCoAccountid());
							glTransaction.setCoAccountNumber(vebilldetObj.getCoAccountNumber());	
						}
					// Purchases Amount		
					
						if(aVebill.getJoReleaseDetailId()!=null)
						{
							if(vebilldetObj.getQuantityBilled().signum()>0)
							{
								glTransaction.setDebit(vebilldetObj.getQuantityBilled());
								glTransaction.setCredit( dBorCrAmount);
							}
							else
							{
								glTransaction.setCredit(vebilldetObj.getQuantityBilled().negate());
								glTransaction.setDebit( dBorCrAmount);
							}
						}
					else
					{
						/*if(vbilldetailslist.size()>1)
						{
							if(vebilldetObj.getQuantityBilled().signum()>0)
							{
								glTransaction.setDebit(vebilldetObj.getQuantityBilled());
								glTransaction.setCredit( dBorCrAmount);
							}
							else
							{
								glTransaction.setCredit(vebilldetObj.getQuantityBilled().negate());
								glTransaction.setDebit( dBorCrAmount);
							}
						}
						else
						{*/
						if(inventoryType!=null && !inventoryType.equals("freightType")){
							if((vebilldetObj.getQuantityBilled().add(aVebill.getFreightAmount())).signum()>0)
							{
								//vebilldetObj.getQuantityBilled().add()
								if(purchaseValues!=null){
									String val=String.valueOf(purchaseValues.get("amount"));
									if(((BigDecimal)purchaseValues.get("amount")).signum()>0){
										glTransaction.setDebit(JobUtil.ConvertintoBigDecimal(val));
										glTransaction.setCredit( dBorCrAmount);	
									}else{
										glTransaction.setDebit(dBorCrAmount);
										glTransaction.setCredit(JobUtil.ConvertintoBigDecimal(val).negate());
									}
									
								}else{
									glTransaction.setDebit(vebilldetObj.getQuantityBilled().add(aVebill.getFreightAmount()));
									glTransaction.setCredit( dBorCrAmount);	
								}
							}
							else
							{
								if(purchaseValues!=null){
									String val=String.valueOf(((BigDecimal)purchaseValues.get("amount")));
									if(((BigDecimal)purchaseValues.get("amount")).signum()>0){
										glTransaction.setDebit(JobUtil.ConvertintoBigDecimal(val));
										glTransaction.setCredit(dBorCrAmount);
									}else{
										glTransaction.setDebit(dBorCrAmount);
										glTransaction.setCredit(JobUtil.ConvertintoBigDecimal(val).negate());
									}
										
								}else{
									glTransaction.setCredit(vebilldetObj.getQuantityBilled().add(aVebill.getFreightAmount()).negate());
									glTransaction.setDebit( dBorCrAmount);
								}
							}
						//}
					}else if(inventoryType!=null && inventoryType.equals("freightType")){
						if(aVebill.getFreightAmount().signum()>0)
						{
							//vebilldetObj.getQuantityBilled().add()
							if(purchaseValues!=null){
								String val=String.valueOf(purchaseValues.get("amount"));
								if(((BigDecimal)purchaseValues.get("amount")).signum()>0){
									glTransaction.setDebit(JobUtil.ConvertintoBigDecimal(val));
									glTransaction.setCredit( dBorCrAmount);	
								}else{
									glTransaction.setDebit(dBorCrAmount);
									glTransaction.setCredit(JobUtil.ConvertintoBigDecimal(val).negate());
								}
								
							}else{
								glTransaction.setDebit(vebilldetObj.getQuantityBilled().add(aVebill.getFreightAmount()));
								glTransaction.setCredit( dBorCrAmount);	
							}
						}
						else
						{
							if(purchaseValues!=null){
								String val=String.valueOf(((BigDecimal)purchaseValues.get("amount")));
								if(((BigDecimal)purchaseValues.get("amount")).signum()>0){
									glTransaction.setDebit(dBorCrAmount);
									glTransaction.setCredit(JobUtil.ConvertintoBigDecimal(val));	
								}else{
									glTransaction.setDebit(JobUtil.ConvertintoBigDecimal(val).negate());
									glTransaction.setCredit(dBorCrAmount);
								}
								
							}else{
								glTransaction.setCredit(vebilldetObj.getQuantityBilled().add(aVebill.getFreightAmount()).negate());
								glTransaction.setDebit( dBorCrAmount);
							}
						}
					}
					}
					if ((vebilldetObj.getQuantityBilled()).compareTo(BigDecimal.ZERO) != 0) {
						glTransationid = saveGltransactionTable(glTransaction);
			
						itsLogger.info("coLedgerSourceId == "
								+ aColedgersource.getCoLedgerSourceId()
								+ "GlTransactionid == " + glTransationid + "VeBillid == "
								+ aVebill.getVeBillId());
			
						glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
						glLinkage.setGlTransactionId(glTransationid);
						glLinkage.setEntryDate(new Date());
						glLinkage.setVeBillID(aVebill.getVeBillId());
						glLinkage.setStatus(0);
						saveGlLinkageTable(glLinkage);		
						
					
					}
					}
					if(aVebill.getJoReleaseDetailId()!=null)
						{frieghtStatus=true;}
					else
						{
						/*if(aVebill.getBillAmount().compareTo(aVebill.getFreightAmount())==0)
							frieghtStatus=true;
						else*/
							frieghtStatus=false;
						}
					
					itsLogger.info("*************Purchases Ends Here**************");
					return frieghtStatus;
		
	
	}


	public void insertSalesTax(GlTransaction glTransaction,
			Coaccount CoAccountIdsalesTaxPaiddetails, Vebill aVebill,
			Coledgersource aColedgersource,BigDecimal dBorCrAmount) throws BankingException {

		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;

		itsLogger.info("*************Sales Tax Starts Here**************");
		
		// Sales Tax

		glTransaction.setCoAccountId(CoAccountIdsalesTaxPaiddetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdsalesTaxPaiddetails.getNumber());

		// Sales Tax

		
				if(aVebill.getTaxAmount().signum()>0)
				{
				glTransaction.setDebit(aVebill.getTaxAmount());
				glTransaction.setCredit( dBorCrAmount);
				}
				else
				{
				glTransaction.setCredit(aVebill.getTaxAmount().negate());
				glTransaction.setDebit( dBorCrAmount);
				}
		
				if (aVebill.getTaxAmount().compareTo(BigDecimal.ZERO) != 0) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					itsLogger.info("coLedgerSourceId == "
							+ aColedgersource.getCoLedgerSourceId()
							+ "GlTransactionid == " + glTransationid + "VeBillid == "
							+ aVebill.getVeBillId());
		
					glLinkage
							.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(aVebill.getVeBillId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
		
					
			}
	
	itsLogger.info("*************Sales Tax Ends Here**************");

	}


	public void insertFrieght(GlTransaction glTransaction,
			Coaccount CoAccountIdfreightdetails, Vebill aVebill,
			Coledgersource aColedgersource,BigDecimal dBorCrAmount) throws BankingException {

		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;
		
		itsLogger.info("*************Frieght Ends Here**************");

		// Frieght

		glTransaction.setCoAccountId(CoAccountIdfreightdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdfreightdetails.getNumber());
		

		// Frieght Amount

					if(aVebill.getFreightAmount().signum()>0)
					{
					glTransaction.setDebit(aVebill.getFreightAmount());
					glTransaction.setCredit(dBorCrAmount);
					}
					else
					{
					glTransaction.setCredit(aVebill.getFreightAmount().negate());
					glTransaction.setDebit(dBorCrAmount);	
					}
			
					if (aVebill.getFreightAmount().compareTo(BigDecimal.ZERO) != 0) {
						glTransationid=saveGltransactionTable(glTransaction); 	 	
			
						itsLogger.info("coLedgerSourceId=="
								+ aColedgersource.getCoLedgerSourceId()
								+ "GlTransactionid==" + glTransationid + "VeBillid=="
								+ aVebill.getVeBillId());
			
						glLinkage
								.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
						glLinkage.setGlTransactionId(glTransationid) 	 	;
						glLinkage.setEntryDate(new Date());
						glLinkage.setVeBillID(aVebill.getVeBillId());
						glLinkage.setStatus(0);
						saveGlLinkageTable(glLinkage);
						
						
			
				}
		
		itsLogger.info("*************Frieght Ends Here**************");

	}
	
	public ArrayList<GlAccountAmount> getveBillDetailsID(Integer veBillID,String poStatus) throws BankingException
	{
		
		ArrayList<GlAccountAmount> vebill=new ArrayList<GlAccountAmount>();
		
		GlAccountAmount vebillDetail =new GlAccountAmount();
		
		Session aSession = itsSessionFactory.openSession();
		
		String addressQuery = "";

		if(poStatus.equals("InsideJob"))
		{
		addressQuery ="SELECT  SUM(TRUNCATE(TRUNCATE(`QuantityBilled`,2)*TRUNCATE(`UnitCost`,2)*IF(IFNULL(`PriceMultiplier`, 0)=0,1,`PriceMultiplier`),2)),ca.Number,ca.coAccountID FROM veBill veb "
				+ " JOIN veBillDetail ved ON veb.veBillID = ved.veBillID JOIN prMaster pr ON ved.prMasterID= pr.prMasterID JOIN prDepartment prd ON pr.prDepartmentID = prd.prDepartmentID "
				+ " JOIN coAccount ca ON ca.`coAccountID` = prd.`coAccountIDCOGS` WHERE veb.veBillID= "+veBillID+" GROUP BY ca.coAccountID";
		}
		else
		{
		addressQuery ="SELECT SUM(TRUNCATE(TRUNCATE(`QuantityBilled`,2)*TRUNCATE(`UnitCost`,2)*IF(IFNULL(`PriceMultiplier`, 0)=0,1,`PriceMultiplier`),2)) AS amt,ca.Number,ca.coAccountID FROM veBill veb "
				+ " JOIN veBillDetail ved ON veb.veBillID = ved.veBillID JOIN vePO vP ON veb.vePOID = vP.vePOID	JOIN prWarehouse pW ON vP.prWarehouseID = pW.prWarehouseID "
				+ " JOIN coAccount ca ON ca.`coAccountID` = pW.`coAccountIDAsset` WHERE veb.veBillID= "+veBillID+" GROUP BY ca.coAccountID";	
		}
		

		try {
			Query aQuery = aSession.createSQLQuery(addressQuery);
			
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					vebillDetail = new GlAccountAmount();
					
					
					vebillDetail.setQuantityBilled((BigDecimal) aObj[0]);
					vebillDetail.setCoAccountNumber((String) aObj[1]);
					vebillDetail.setCoAccountid((Integer) aObj[2]);
					
					vebill.add(vebillDetail);
				}
					
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
		}
	
		return vebill;
	}	
	
	public ArrayList<Vebilldetail> getveBillDetailsIDs(Integer veBillID) throws BankingException
	{
		
		ArrayList<Vebilldetail> vebill=null;
		
		
		Session aSession = null;
		String addressQuery = "FROM Vebilldetail WHERE veBillId = "
				+ veBillID;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery(addressQuery);			
			vebill = (ArrayList<Vebilldetail>) aQuery.list();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			addressQuery=null;
			aQuery =null;
		}
	
		return vebill;
	}
	
	
	@Override
	public ArrayList<Vebill> getAllVeBillDetails(Integer thevebillID) throws BankingException {
		Session aSession = null;
		ArrayList<Vebill> aMoAccountList = null;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  Vebill where veBillID="+thevebillID);
			aMoAccountList = (ArrayList<Vebill>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  aMoAccountList;
	}
	
	@Override
	public ArrayList<Object> getAllVeBillDetailsandDistripution(Integer thevebillID) throws BankingException {
		Session aSession = null;
		ArrayList<Object> aMoAccountList = null;
		Query aQuery =null;
		
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  Vebill vb,Vebilldistribution vd where vd.veBillId=vb.veBillId and vb.veBillId="+thevebillID);
			aMoAccountList = (ArrayList<Object>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  aMoAccountList;
	}
	
	@Override
	public String getAllVeBillDetailswithvePoid(Integer thevepoid) throws BankingException {
		Session aSession = null;
		ArrayList<?> aVeBillList = null;
		String invoiceStatus = null;
		String strQuery =null;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			strQuery = "SELECT (IF (ve.QuantityOrdered - IFNULL (a.quaninv,0)>=0,ve.QuantityOrdered - IFNULL (a.quaninv,0),0))AS QuantityOrdered  FROM vePODetail ve "
					+ " LEFT JOIN (SELECT vePODetailID,SUM(quantityInvoiced) AS quaninv FROM veBillHistory WHERE vePOID= "+thevepoid+" GROUP BY vePODetailID ) a ON a.vePODetailID = ve.vePODetailID"
					+ " WHERE ve.vePOID ="+thevepoid;
			aQuery = aSession.createSQLQuery(strQuery);
			aVeBillList = (ArrayList<BigDecimal>) aQuery.list();
			
			if(aVeBillList.size()>0)
			{
				for(int i=0;i<aVeBillList.size();i++)
				{
					if(((BigDecimal) aVeBillList.get(i)).compareTo(BigDecimal.ZERO) == 0)
					{
						invoiceStatus = "Invoiced";
					}
					else
					{
						invoiceStatus = "Not Invoiced";
						break;
					}
				}
			}
				
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  invoiceStatus;
	}
	
	
	@Override
	public ArrayList<GlLinkage> getAllGlLinkageDetails(Integer thevebillID,Integer ledgerid) throws BankingException {
		itsLogger.info("=="+thevebillID+"--"+ledgerid);
		ArrayList<GlLinkage> aMoAccountList = null;
		Query aQuery =null;
		Session glLinkageSession = itsSessionFactory.openSession();
		try {
			
			aQuery = glLinkageSession.createQuery("FROM  GlLinkage where veBillID="+thevebillID+" and status=0 and coLedgerSourceId="+ledgerid+" group by glTransactionId");
			aMoAccountList = (ArrayList<GlLinkage>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			glLinkageSession.flush();
			glLinkageSession.close();
			aQuery =null;
		}
		return  aMoAccountList;
	}
	
	@Override
	public ArrayList<GlTransaction> getAllglTransactionDetails(Integer glTransactionId) throws BankingException {
		Session aSession = null;
		ArrayList<GlTransaction> aMoAccountList = null;Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  GlTransaction where glTransactionId="+glTransactionId);
			aMoAccountList = (ArrayList<GlTransaction>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
		return  aMoAccountList;
	}
	
	@Override
	public ArrayList<GlTransaction> getAllglTransactionDetailswithcoAccountID(Integer coAccountid) throws BankingException {
		Session aSession = null;
		ArrayList<GlTransaction> aMoAccountList = null;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  GlTransaction where coAccountID="+coAccountid);
			aMoAccountList = (ArrayList<GlTransaction>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  aMoAccountList;
	}

	@Override
	public ArrayList<Vebilldistribution> getAllVeBillDistripution(
			Integer theVeBillID)  throws BankingException {
		Session aSession = null;
		ArrayList<Vebilldistribution> aMoAccountList = null;
		Vebilldistribution vebilldistribution =null;
		String addressQuery =null;
		try {
			aMoAccountList = new ArrayList<Vebilldistribution>();
			addressQuery ="Select coExpenseAccountID, SUM(ExpenseAmount),joMasterID from  veBillDistribution where veBillID="+theVeBillID+" Group by coExpenseAccountID";
		
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(addressQuery);
			
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					vebilldistribution = new Vebilldistribution();
					
					vebilldistribution.setCoExpenseAccountId((Integer) aObj[0]);					
					vebilldistribution.setExpenseAmount((BigDecimal) aObj[1]);
					if((Integer)aObj[2]!=null)
					vebilldistribution.setJoMasterId((Integer) aObj[2]);
					aMoAccountList.add(vebilldistribution);
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
		return  aMoAccountList;
	}

	
	
	@Override
	public void bankingDeposits(Motransaction aMotransaction,String DepositType,Integer yearID,Integer periodID,String userName,Session aSession) throws BankingException, CompanyException {
		try {
		Motransaction aMotran=(Motransaction) aSession.get(Motransaction.class, aMotransaction.getMoTransactionId());
		Coaccount coaccount = getcoAccountDetails(aMotransaction);
		Coaccount CoAccountdetails = getCoaccountDetailsBasedoncoAccountid(aMotransaction.getCoAccountId());
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = getColedgersourceDetail( "BT");

		GlTransaction glTransaction = new GlTransaction();

		// period
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
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
		glTransaction.setTransactionDate(aMotransaction.getTransactionDate());
		glTransaction.setEnteredBy(userName);

	
		glTransaction.setPoNumber(aMotransaction.getReference());
		
		glTransaction.setTransactionDesc("Bank Transaction: "+ aMotransaction.getDescription());
		
		BigDecimal dBorCrAmount= new BigDecimal(0);
	
		int checkAmtnegative = 0;
		checkAmtnegative = aMotransaction.getAmount().signum();
		
		
		
		if(DepositType.equals("Single"))
		{
			if(checkAmtnegative == 1)
			{
			insertCreditDetailsofBankTransaction(glTransaction, CoAccountdetails, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			insertDebitDetailsofBankTransaction(glTransaction, coaccount, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			}
			else
			{
			aMotransaction.setAmount(aMotransaction.getAmount().multiply(new BigDecimal(-1)));				
			insertDebitDetailsofBankTransaction(glTransaction, CoAccountdetails, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			insertCreditDetailsofBankTransaction(glTransaction, coaccount, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			}
		}
		else if(DepositType.equals("Multiple with subaccounts"))
		{
			if(aMotran.getMoTransactionTypeId() == 0)
			{
			insertCreditDetailsofBankTransaction(glTransaction, CoAccountdetails, aColedgersource, aMotransaction, dBorCrAmount,aSession);	
			}
			else
			{
		//	aMotransaction.setAmount(aMotransaction.getAmount().multiply(new BigDecimal(-1)));
			insertDebitDetailsofBankTransaction(glTransaction, CoAccountdetails, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			}
		}
		else if(DepositType.equals("Multiple with mainaccounts"))
		{
			
			if(aMotran.getMoTransactionTypeId() == 0)
			{
			insertDebitDetailsofBankTransaction(glTransaction, coaccount, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			}
			else
			{
	//		aMotransaction.setAmount(aMotransaction.getAmount().multiply(new BigDecimal(-1)));
			insertCreditDetailsofBankTransaction(glTransaction, coaccount, aColedgersource, aMotransaction, dBorCrAmount,aSession);
			}
		}
		}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		}
		
	}


	
	@Override
	public void insertDebitDetailsofBankTransaction(
			GlTransaction glTransaction, Coaccount CoAccountdetails,
			Coledgersource aColedgersource, Motransaction aMotransaction,
			BigDecimal dBorCrAmount,Session aSession) throws BankingException {
	
		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;
		
		//int a=10/0;

		glTransaction.setCoAccountId(CoAccountdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountdetails.getNumber());


		glTransaction.setDebit( aMotransaction.getAmount());
		glTransaction.setCredit(dBorCrAmount);

		if ( aMotransaction.getAmount().compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = saveGltransactionTable1(glTransaction,aSession);

			itsLogger.info("coLedgerSourceId == "
					+ aColedgersource.getCoLedgerSourceId()
					+ "GlTransactionid == " + glTransationid + "Id == "
					);

			glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aMotransaction.getMoTransactionId());
			glLinkage.setStatus(0);
			saveGlLinkageTable1(glLinkage,aSession);

		}
		
		
	}

	
	@Override
	public void insertCreditDetailsofBankTransaction(
			GlTransaction glTransaction, Coaccount CoAccountdetails,
			Coledgersource aColedgersource, Motransaction aMotransaction,
			BigDecimal dBorCrAmount, Session aSession) throws BankingException {
	
		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;
		
		glTransaction.setCoAccountId(CoAccountdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountdetails.getNumber());

		glTransaction.setDebit(dBorCrAmount);
		glTransaction.setCredit( aMotransaction.getAmount());

		
		if ( aMotransaction.getAmount().compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = saveGltransactionTable1(glTransaction,aSession);

			itsLogger.info("coLedgerSourceId == "
					+ aColedgersource.getCoLedgerSourceId()
					+ "GlTransactionid == " + glTransationid + "Id == "
					);

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aMotransaction.getMoTransactionId());
			glLinkage.setStatus(0);
			saveGlLinkageTable1(glLinkage,aSession);

		}
		
	}
	
	
	
	@Override
	public void rollBackGlTransaction(GlRollback aGlRollback) throws BankingException, CompanyException
	{
		itsLogger.info("Roll Back Executed=="+aGlRollback.getVeBillID()+" "+aGlRollback.getCoLedgerSourceID());
		Transaction aTransaction =null;
		
		Session aSession1 = null;
	
		// For Getting New Period	
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(aGlRollback.getPeriodID());
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(aGlRollback.getYearID());

		// Getting glLinkage data
		ArrayList<GlLinkage> arrayglLinkagelist= getAllGlLinkageDetails(aGlRollback.getVeBillID(), aGlRollback.getCoLedgerSourceID());
		ArrayList<GlTransaction> arrayglTransList=new ArrayList<GlTransaction>();
	
		
		
		for(GlLinkage glLinkageObj:arrayglLinkagelist)
		{
			aSession1 = itsSessionFactory.openSession();
			
			// Getting all transaction data
			arrayglTransList=getAllglTransactionDetails(glLinkageObj.getGlTransactionId());
			
			for(GlTransaction glTransObj:arrayglTransList)
			{
					glTransObj.setTransactionDate(aGlRollback.getTransactionDate());
				
					// period
					glTransObj.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
					glTransObj.setPeriod(aCofiscalperiod.getPeriod());
					glTransObj.setpStartDate(aCofiscalperiod.getStartDate());
					glTransObj.setpEndDate(aCofiscalperiod.getEndDate());
	
					// year
					glTransObj.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
					glTransObj.setFyear(aCofiscalyear.getFiscalYear());
					glTransObj.setyStartDate(aCofiscalyear.getStartDate());
					glTransObj.setyEndDate(aCofiscalyear.getEndDate());
				
			
				   if (glTransObj.getCredit().compareTo(BigDecimal.ZERO) != 0)
				   {
					   insertglAprollBackInsert(glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID());
				   }
				   else
				   {
					   insertglrollBackInsert( glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID());
				   }
			}
			
			String aQuery =null;
			try
			{
		    aTransaction = aSession1.beginTransaction();
			aTransaction.begin();
			aQuery = "update glLinkage set status=1 where glTransactionId ="+glLinkageObj.getGlTransactionId();
			aSession1.createSQLQuery(aQuery).executeUpdate();
			aTransaction.commit();
			}
			catch(Exception excep){itsLogger.error(excep.getMessage(), excep);}
			finally
			{
				aSession1.flush();
				aSession1.close();
				aQuery =null;
			}
		
		}
	}
	
	public void insertglAprollBackInsert(GlTransaction glTransObj,Integer coledgersourceid,Integer vebillid) throws BankingException
	{
		int glTransationid=0;
		GlLinkage glLinkage =new GlLinkage();
		
		try
		{				
			glTransObj.setDebit(glTransObj.getCredit());
			glTransObj.setCredit(new BigDecimal(0));				
			glTransationid = saveGltransactionTable(glTransObj);				
			glLinkage.setCoLedgerSourceId(coledgersourceid);
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(vebillid);
			glLinkage.setStatus(1);
			saveGlLinkageTable(glLinkage);		
		}
		catch(Exception excep){
			itsLogger.error(excep.getMessage(), excep);
			BankingException aVendorException = new BankingException(excep.getMessage(),
					excep);
			throw aVendorException;
		} 
	}
	
	public void insertglrollBackInsert(GlTransaction glTransObj,Integer coledgersourceid,Integer vebillid)throws BankingException
	{
		int glTransationid=0;
		GlLinkage glLinkage =new GlLinkage();
		
		try
		{				
			glTransObj.setCredit(glTransObj.getDebit());
			glTransObj.setDebit(new BigDecimal(0));
			glTransationid = saveGltransactionTable(glTransObj);				
			glLinkage.setCoLedgerSourceId(coledgersourceid);
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(vebillid);
			glLinkage.setStatus(1);
			saveGlLinkageTable(glLinkage);	
		} catch(Exception excep){
			itsLogger.error(excep.getMessage(), excep);
			BankingException aVendorException = new BankingException(excep.getMessage(),
					excep);
			throw aVendorException;
		}
	}

	@Override
	public void rollBackGlTransaction1(GlRollback aGlRollback, Session aSession) throws BankingException,CompanyException {
		
		itsLogger.info("Roll Back Executed==" + aGlRollback.getVeBillID() + " "+ aGlRollback.getCoLedgerSourceID());
		
		// For Getting New Period	
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(aGlRollback.getPeriodID());
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(aGlRollback.getYearID());

		
		ArrayList<GlLinkage> arrayglLinkagelist = getAllGlLinkageDetails(aGlRollback.getVeBillID(), aGlRollback.getCoLedgerSourceID());
		ArrayList<GlTransaction> arrayglTransList = new ArrayList<GlTransaction>();
		
		
		for (GlLinkage glLinkageObj : arrayglLinkagelist) {
			arrayglTransList = getAllglTransactionDetails(glLinkageObj.getGlTransactionId());
			for (GlTransaction glTransObj : arrayglTransList) {
				
				glTransObj.setTransactionDate(aGlRollback.getTransactionDate());
				
				// period
				glTransObj.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
				glTransObj.setPeriod(aCofiscalperiod.getPeriod());
				glTransObj.setpStartDate(aCofiscalperiod.getStartDate());
				glTransObj.setpEndDate(aCofiscalperiod.getEndDate());

				// year
				glTransObj.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
				glTransObj.setFyear(aCofiscalyear.getFiscalYear());
				glTransObj.setyStartDate(aCofiscalyear.getStartDate());
				glTransObj.setyEndDate(aCofiscalyear.getEndDate());
				
				if (glTransObj.getCredit().compareTo(BigDecimal.ZERO) != 0) {
					insertglAprollBackInsert1(glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID(), aSession);
				} else {
					insertglrollBackInsert1(glTransObj,	glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID(), aSession);
				}
			}
			String aQuery = null;
			try {
				aQuery = "update glLinkage set status=1 where glTransactionId ="
						+ glLinkageObj.getGlTransactionId();
				aSession.createSQLQuery(aQuery).executeUpdate();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
			} finally {
				aSession.flush();
				aSession.clear();
				aQuery = null;
			}
		}
	}
	
	public void insertglAprollBackInsert1(GlTransaction glTransObj,
			Integer coledgersourceid, Integer vebillid, Session aSession)
			throws BankingException {

		int glTransationid = 0;
		GlLinkage glLinkage = new GlLinkage();

		try {
			glTransObj.setDebit(glTransObj.getCredit());
			glTransObj.setCredit(new BigDecimal(0));
			glTransationid = saveGltransactionTable1(glTransObj, aSession);
			glLinkage.setCoLedgerSourceId(coledgersourceid);
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(vebillid);
			glLinkage.setStatus(1);
			saveGlLinkageTable1(glLinkage, aSession);

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aVendorException = new BankingException(
					excep.getMessage(), excep);
			throw aVendorException;
		}
	}
	
	public void insertglrollBackInsert1(GlTransaction glTransObj,
			Integer coledgersourceid, Integer vebillid, Session aSession)
			throws BankingException {

		int glTransationid = 0;
		GlLinkage glLinkage = new GlLinkage();

		try {
			glTransObj.setCredit(glTransObj.getDebit());
			glTransObj.setDebit(new BigDecimal(0));
			glTransationid = saveGltransactionTable1(glTransObj, aSession);
			glLinkage.setCoLedgerSourceId(coledgersourceid);
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(vebillid);
			glLinkage.setStatus(1);
			saveGlLinkageTable1(glLinkage, aSession);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			BankingException aVendorException = new BankingException(
					excep.getMessage(), excep);
			throw aVendorException;
		}
	}
		
	/**
	 * Created by:Leo   Date : 29/10/14
	 * 
	 * Description: Insert GlTransaction while Customer Invoicing
	 * @throws CompanyException 
	 * 
	 * */
		
	@Override
	public void receiveCustomerInvoiceBill(Cuinvoice thecuInvoice,
			Integer yearID, Integer periodID, String username)
			throws BankingException, CompanyException {

		Session aSession = itsSessionFactory.openSession();
		Cuinvoice cuInvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
				thecuInvoice.getCuInvoiceId());

		GlTransaction glTransaction = new GlTransaction();
		ArrayList<SysAccountLinkage> aSysAccountLinkage = getAllsysaccountLinkage(cuInvoice);

		Short ReleaseID = getReleaseTypeID(cuInvoice.getJoReleaseDetailId());

		if (cuInvoice.getCuSoid() != null) {
			if (ReleaseID != null) {
				if (ReleaseID == 2 || ReleaseID == 5 || ReleaseID == 3) {
					insideJobCustomerInvoice(aSysAccountLinkage, cuInvoice,
							glTransaction, yearID, periodID, username);
				}

				else if (ReleaseID == 1) {
					insideJobCustomerInvoicefromDropship(aSysAccountLinkage,
							cuInvoice, glTransaction, yearID, periodID,
							username);
				}
			} else {
				outsideJobCustomerInvoicewithSO(aSysAccountLinkage, cuInvoice,
						glTransaction, yearID, periodID, username);
			}
		} else {
			outsideJobCustomerInvoicewithoutSO(aSysAccountLinkage, cuInvoice,
					glTransaction, yearID, periodID, username);
		}
	}
	
	
	public void insideJobCustomerInvoice(ArrayList<SysAccountLinkage> aSysAccountLinkage,Cuinvoice cuInvoice,GlTransaction glTransaction,Integer yearID,Integer periodID,String username)throws BankingException, CompanyException
	{
		Coaccount CoAccountIdar =new Coaccount();
		Coaccount CoAccountIdsalesTaxInv =new Coaccount();
		Coaccount CoAccountIdShippingInventoryfromsysAccLink =new Coaccount();
		Coaccount coAccountfromInventoryAccountNumber =new Coaccount();

		Coaccount CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
		Coaccount CoAccountIdPurchases =new Coaccount();
		
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = getColedgersourceDetail("CI");
		
		String[] accNoArray;
		String customAccNo="";
		
		String[] accNoArray1;
		String customAccNo1;
		String subAcccountValue=null;

		
		/**------------Customer Invoice Stock Order Inside Job-------------------*/
			
		for(SysAccountLinkage sysLinObj:aSysAccountLinkage)
		{
			 //Account Receivable id
			 CoAccountIdar = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdar());  /*2a*/
			 //Sales Tax Invoice id
			 CoAccountIdsalesTaxInv = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdsalesTaxInv()); /*2c*/
			 
			 //Shipping inventory account from settings 2d
			 CoAccountIdShippingInventoryfromsysAccLink = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdshipInventory()); 
			 itsLogger.info(""+CoAccountIdShippingInventoryfromsysAccLink.getNumber());
			/* int lastindex = str.lastIndexOf("_");
			 String splitstr = str.substring(lastindex+1); */
			 accNoArray = (CoAccountIdShippingInventoryfromsysAccLink.getNumber()).split("-");
			 subAcccountValue = getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
			 
			 if(subAcccountValue!=null)
			 {
				/*String subacconly="";
				if(accNoArray.length>2)	 
				{
					for(int i=2;i<accNoArray.length;i++)
					{
						if(i==accNoArray.length-1)
						{
							subacconly += accNoArray[i];
						}
						else
						{
							subacconly +=accNoArray[i]+"-";
						}
					}
				}*/
				
			 customAccNo = accNoArray[0]+"-"+subAcccountValue;
			 CoAccountIdShippingInventoryfromcoAccountdesc = getcoAccountfromAccountNumber(customAccNo);
			 if(CoAccountIdShippingInventoryfromcoAccountdesc!=null) 
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription( CoAccountIdShippingInventoryfromcoAccountdesc.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(customAccNo);
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromcoAccountdesc.getCoAccountId());
			 }
			 else
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShippingInventoryfromsysAccLink.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShippingInventoryfromsysAccLink.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromsysAccLink.getCoAccountId());
			 }
			 }
			 else
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShippingInventoryfromsysAccLink.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShippingInventoryfromsysAccLink.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromsysAccLink.getCoAccountId());
			 }
		}
		
		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(cuInvoice.getRxCustomerId());
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
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
		glTransaction.setTransactionDate(cuInvoice.getInvoiceDate());
		glTransaction.setEnteredBy(username);

		// Ponumber and trDesc
		glTransaction.setPoNumber(cuInvoice.getInvoiceNumber());
		glTransaction.setTransactionDesc(liRxmasters.getName());
		
		BigDecimal dBorCrAmount= new BigDecimal(0);
		BigDecimal dBorDrAmount= new BigDecimal(0);
		
		/* Account Receivable */
		
		 insertAccountRecievable(glTransaction, CoAccountIdar, cuInvoice,aColedgersource,dBorCrAmount);
		 
		 /* Warehouse AccountNo */
		
		coAccountfromInventoryAccountNumber = getcoAccountfromInventoryAccountNumber(cuInvoice.getCuInvoiceId());
		
		List<?> InventoryAccount = getInventAccountDetails("Inside",cuInvoice.getCuInvoiceId());
		
		Iterator<?> aIterator =InventoryAccount.iterator() ;
		while (aIterator.hasNext()) {
			Object[] aObj = (Object[]) aIterator.next();
			
			dBorCrAmount = dBorCrAmount.add((BigDecimal)aObj[2]);
		}
		
		if(coAccountfromInventoryAccountNumber.getCoAccountId()!=null)
		insertWarehouseInv(glTransaction, coAccountfromInventoryAccountNumber, cuInvoice,aColedgersource,dBorCrAmount,dBorDrAmount);		
		
		 dBorCrAmount= new BigDecimal(0);
		 dBorDrAmount= new BigDecimal(0);
		/* Sales Tax */

		insertSalesTaxcuInv(glTransaction, CoAccountIdsalesTaxInv, cuInvoice,aColedgersource,dBorCrAmount);		

		/* Sales shipping */

		insertSales(glTransaction, CoAccountIdShippingInventoryfromcoAccountdesc, cuInvoice,aColedgersource,dBorCrAmount);
		
		 dBorCrAmount= new BigDecimal(0);
		 dBorDrAmount= new BigDecimal(0);
		
		 //Purchase account from division 2d
		List<?> InventoryAccount1 = getInventAccountDetails("Inside",cuInvoice.getCuInvoiceId());
		
		Iterator<?> aIterator1 =InventoryAccount1.iterator() ;
		while (aIterator1.hasNext()) {
			Object[] aObj = (Object[]) aIterator1.next();
			
			 accNoArray1 = ((String)aObj[1]).split("-");
			 customAccNo1 = accNoArray1[0]+"-"+getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
			 dBorDrAmount = (BigDecimal)aObj[2];
			 CoAccountIdPurchases = getcoAccountfromAccountNumber(customAccNo1);
			 if(CoAccountIdPurchases!=null) 
			 {
				 CoAccountIdPurchases.setDescription(CoAccountIdPurchases.getDescription());
				 CoAccountIdPurchases.setCoAccountId(CoAccountIdPurchases.getCoAccountId());
				 CoAccountIdPurchases.setNumber(customAccNo1);
			 }
			 else
			 {
				 CoAccountIdPurchases = getcoAccountfromAccountNumber((String)aObj[1]);	
				 CoAccountIdPurchases.setNumber((String)aObj[1]);
			 }
			 
			 if(coAccountfromInventoryAccountNumber.getCoAccountId()!=null)										 
			 insertPurchases(glTransaction, CoAccountIdPurchases, cuInvoice,aColedgersource,dBorCrAmount,dBorDrAmount);
			
		}
	
	}
	
	public void insideJobCustomerInvoicefromDropship(ArrayList<SysAccountLinkage> aSysAccountLinkage,Cuinvoice cuInvoice,GlTransaction glTransaction,Integer yearID,Integer periodID,String username)throws BankingException, CompanyException
	{
		
		Coaccount CoAccountIdar =new Coaccount();
		Coaccount CoAccountIdsalesTaxInv =new Coaccount();
		Coaccount CoAccountIdShipping =new Coaccount();
		Coaccount CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
		String[] accNoArray;
		String customAccNo="";
		String subAcccountValue=null;
		
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = getColedgersourceDetail( "CI");
		

		/**------------Customer Invoice Dropship Inside Job-------------------*/

		itsLogger.info("I am Entering Partial");		
		for(SysAccountLinkage sysLinObj:aSysAccountLinkage)
		{
			
			 CoAccountIdar = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdar());
			 CoAccountIdsalesTaxInv = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdsalesTaxInv());
			 CoAccountIdShipping = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdshipping());
			 
			 accNoArray = (CoAccountIdShipping.getNumber()).split("-");
			 subAcccountValue = getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
			 
			 if(subAcccountValue!=null)
			 {
			 customAccNo = accNoArray[0]+"-"+subAcccountValue;
			 CoAccountIdShippingInventoryfromcoAccountdesc = getcoAccountfromAccountNumber(customAccNo);
			 
			 if(CoAccountIdShippingInventoryfromcoAccountdesc!=null) 
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription( CoAccountIdShippingInventoryfromcoAccountdesc.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(customAccNo);
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromcoAccountdesc.getCoAccountId());
			 }
			 else
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShipping.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShipping.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShipping.getCoAccountId());
			 }
			 }
			 else
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShipping.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShipping.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShipping.getCoAccountId());
			 }
		}
					itsLogger.info("AP Receivable: "+CoAccountIdar.getNumber()+" Sales Tax Inv:"+CoAccountIdsalesTaxInv.getNumber()+" Shipping:"+CoAccountIdShipping.getNumber());
					Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(cuInvoice.getRxCustomerId());
				
					itsLogger.info("-------->"+aCofiscalperiod.getCoFiscalPeriodId());
					
					glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
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
					glTransaction.setTransactionDate(cuInvoice.getInvoiceDate());
					glTransaction.setEnteredBy(username);
				
					// Ponumber and trDesc
					glTransaction.setPoNumber(cuInvoice.getInvoiceNumber());
					glTransaction.setTransactionDesc(liRxmasters.getName());
					
					BigDecimal dBorCrAmount= new BigDecimal(0);
					
					/* Account Receivable */
				
					insertAccountRecievable(glTransaction, CoAccountIdar, cuInvoice,aColedgersource,dBorCrAmount);
					
					/* Sales Tax */
				
					insertSalesTaxcuInv(glTransaction, CoAccountIdsalesTaxInv, cuInvoice,aColedgersource,dBorCrAmount);		
				
					/* Sales shipping */
				
					insertSales(glTransaction, CoAccountIdShippingInventoryfromcoAccountdesc, cuInvoice,aColedgersource,dBorCrAmount);
	
	}
	
	public void outsideJobCustomerInvoicewithSO(ArrayList<SysAccountLinkage> aSysAccountLinkage,Cuinvoice cuInvoice,GlTransaction glTransaction,Integer yearID,Integer periodID,String username)throws BankingException, CompanyException
	{

		Coaccount CoAccountIdar =new Coaccount();
		Coaccount CoAccountIdsalesTaxInv =new Coaccount();
		Coaccount CoAccountIdShippingInventoryfromsysAccLink =new Coaccount();
		Coaccount coAccountfromInventoryAccountNumber =new Coaccount();
		
		Coaccount CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
		Coaccount CoAccountIdPurchases =new Coaccount();
		
		String[] accNoArray;
		String customAccNo="";
		
		String[] accNoArray1;
		String customAccNo1;
		String subAcccountValue=null;
		
		
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = getColedgersourceDetail("CI");
		
		/**------------Customer Invoice with Sales Order Outside Job-------------------*/

		for(SysAccountLinkage sysLinObj:aSysAccountLinkage)
		{
			 //Account Receivable id
			 CoAccountIdar = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdar());  /*2a*/
			 //Sales Tax Invoice id
			 CoAccountIdsalesTaxInv = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdsalesTaxInv()); /*2c*/
			 
			 //Shipping inventory account from settings 2d
			 CoAccountIdShippingInventoryfromsysAccLink = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdshipInventory()); 
			 
			 
			 accNoArray = (CoAccountIdShippingInventoryfromsysAccLink.getNumber()).split("-");
			 subAcccountValue = getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
			 
			 if(subAcccountValue!=null)
			 {
			 customAccNo = accNoArray[0]+"-"+subAcccountValue;
			 CoAccountIdShippingInventoryfromcoAccountdesc = getcoAccountfromAccountNumber(customAccNo);
			 
			 if(CoAccountIdShippingInventoryfromcoAccountdesc!=null) 
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription( CoAccountIdShippingInventoryfromcoAccountdesc.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(customAccNo);
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromcoAccountdesc.getCoAccountId());
			 }
			 else
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShippingInventoryfromsysAccLink.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShippingInventoryfromsysAccLink.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromsysAccLink.getCoAccountId());
			 }
			 
			 }
			 else
			 {
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShippingInventoryfromsysAccLink.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShippingInventoryfromsysAccLink.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromsysAccLink.getCoAccountId());
			 }
		
		}
		
		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(cuInvoice.getRxCustomerId());
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
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
		glTransaction.setTransactionDate(cuInvoice.getInvoiceDate());
		glTransaction.setEnteredBy(username);

		// Ponumber and trDesc
		glTransaction.setPoNumber(cuInvoice.getInvoiceNumber());
		glTransaction.setTransactionDesc(liRxmasters.getName());
		
		BigDecimal dBorCrAmount= new BigDecimal(0);
		BigDecimal dBorDrAmount= new BigDecimal(0);
		
		/* Account Receivable */
		
		insertAccountRecievable(glTransaction, CoAccountIdar, cuInvoice,aColedgersource,dBorCrAmount);
		 
		 /* Warehouse AccountNo */
		
		coAccountfromInventoryAccountNumber = getcoAccountfromInventoryAccountNumber(cuInvoice.getCuInvoiceId());
		
		List<?> InventoryAccount = getInventAccountDetails("Outside",cuInvoice.getCuInvoiceId());
		
		Iterator<?> aIterator =InventoryAccount.iterator() ;
		while (aIterator.hasNext()) {
			Object[] aObj = (Object[]) aIterator.next();
			dBorCrAmount = dBorCrAmount.add((BigDecimal)aObj[2]);
		}
		
		
		if(coAccountfromInventoryAccountNumber.getCoAccountId()!=null)
		insertWarehouseInv(glTransaction, coAccountfromInventoryAccountNumber, cuInvoice,aColedgersource,dBorCrAmount,dBorDrAmount);		
		
		 dBorCrAmount= new BigDecimal(0);
		 dBorDrAmount= new BigDecimal(0);
		/* Sales Tax */

		insertSalesTaxcuInv(glTransaction, CoAccountIdsalesTaxInv, cuInvoice,aColedgersource,dBorCrAmount);		

		/* Sales shipping */

		insertSales(glTransaction, CoAccountIdShippingInventoryfromcoAccountdesc, cuInvoice,aColedgersource,dBorCrAmount);
		
		 dBorCrAmount= new BigDecimal(0);
		 dBorDrAmount= new BigDecimal(0);
		
		 //Purchase account from division 2d
		List<?> InventoryAccount1 = getInventAccountDetails("Outside",cuInvoice.getCuInvoiceId());
		
		Iterator<?> aIterator1 =InventoryAccount1.iterator() ;
		while (aIterator1.hasNext()) {
		 Object[] aObj = (Object[]) aIterator1.next();
		 accNoArray1 = ((String)aObj[1]).split("-");
		 customAccNo1 = accNoArray1[0]+"-"+getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
		 dBorDrAmount = (BigDecimal)aObj[2];
		 CoAccountIdPurchases = getcoAccountfromAccountNumber(customAccNo1);
		 
		 if(CoAccountIdPurchases!=null) 
		 {
			 CoAccountIdPurchases.setDescription(CoAccountIdPurchases.getDescription());
			 CoAccountIdPurchases.setCoAccountId(CoAccountIdPurchases.getCoAccountId());
			 CoAccountIdPurchases.setNumber(customAccNo1);
			
		 }
		 else
		 {
			 CoAccountIdPurchases = getcoAccountfromAccountNumber((String)aObj[1]);	
			 CoAccountIdPurchases.setNumber((String)aObj[1]);
		 }
		 
		 if(coAccountfromInventoryAccountNumber.getCoAccountId()!=null)
		 insertPurchases(glTransaction, CoAccountIdPurchases, cuInvoice,aColedgersource,dBorCrAmount,dBorDrAmount);
		}
	}
	
	public void outsideJobCustomerInvoicewithoutSO(ArrayList<SysAccountLinkage> aSysAccountLinkage,Cuinvoice cuInvoice,GlTransaction glTransaction,Integer yearID,Integer periodID,String username)throws BankingException, CompanyException
	{

		Coaccount CoAccountIdar =new Coaccount();
		Coaccount CoAccountIdsalesTaxInv =new Coaccount();
		Coaccount CoAccountIdShippingInventoryfromsysAccLink =new Coaccount();
		Coaccount coAccountfromInventoryAccountNumber =new Coaccount();
		
		Coaccount CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
		Coaccount CoAccountIdPurchases =new Coaccount();
		String[] accNoArray;
		String customAccNo="";
		
		String[] accNoArray1;
		String customAccNo1;
		String subAcccountValue=null;
		
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = getColedgersourceDetail( "CI");
		
		/**------------Customer Invoice Without Sales Order Outside Job-------------------*/

		for(SysAccountLinkage sysLinObj:aSysAccountLinkage)
		{
			 //Account Receivable id
			 CoAccountIdar = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdar());  /*2a*/
			 //Sales Tax Invoice id
			 CoAccountIdsalesTaxInv = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdsalesTaxInv()); /*2c*/
			 //Shipping inventory account from settings 2d
			 CoAccountIdShippingInventoryfromsysAccLink = getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdshipInventory()); 
			 
			 itsLogger.info("Mainl Account-------------->"+CoAccountIdShippingInventoryfromsysAccLink.getNumber());
			 
			 accNoArray = (CoAccountIdShippingInventoryfromsysAccLink.getNumber()).split("-");
			 subAcccountValue = getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
			 
			 itsLogger.info("--->"+subAcccountValue);
			 
			 if(subAcccountValue!=null)
			 {
			 customAccNo = accNoArray[0]+"-"+subAcccountValue;
			 CoAccountIdShippingInventoryfromcoAccountdesc = getcoAccountfromAccountNumber(customAccNo);
				 
				 if(CoAccountIdShippingInventoryfromcoAccountdesc!=null) 
				 {
					 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription( CoAccountIdShippingInventoryfromcoAccountdesc.getDescription());
					 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(customAccNo);
					 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromcoAccountdesc.getCoAccountId());
				 }
				 else
				 {
					 itsLogger.info("SubAccount------------>"+CoAccountIdShippingInventoryfromsysAccLink.getDescription());
					 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
					 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShippingInventoryfromsysAccLink.getDescription());
					 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShippingInventoryfromsysAccLink.getNumber());
					 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromsysAccLink.getCoAccountId());
				 }
			 
			 }
			 else
			 {
				 itsLogger.info("SubAccount------------>"+CoAccountIdShippingInventoryfromsysAccLink.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
				 CoAccountIdShippingInventoryfromcoAccountdesc.setDescription(CoAccountIdShippingInventoryfromsysAccLink.getDescription());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setNumber(CoAccountIdShippingInventoryfromsysAccLink.getNumber());
				 CoAccountIdShippingInventoryfromcoAccountdesc.setCoAccountId(CoAccountIdShippingInventoryfromsysAccLink.getCoAccountId());
			 }
			 itsLogger.info("SubAccount------------>"+getsubAccountfromDivision(cuInvoice.getCuInvoiceId()));
		
		}
		
		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(cuInvoice.getRxCustomerId());
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
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
		glTransaction.setTransactionDate(cuInvoice.getInvoiceDate());
		glTransaction.setEnteredBy(username);

		// Ponumber and trDesc
		glTransaction.setPoNumber(cuInvoice.getInvoiceNumber());
		glTransaction.setTransactionDesc(liRxmasters.getName());
		
		BigDecimal dBorCrAmount= new BigDecimal(0);
		BigDecimal dBorDrAmount= new BigDecimal(0);
		
		/* Account Receivable */
		
		insertAccountRecievable(glTransaction, CoAccountIdar, cuInvoice,aColedgersource,dBorCrAmount);
		 
		 /* Warehouse AccountNo */
		
		coAccountfromInventoryAccountNumber = getcoAccountfromInventoryAccountNumber(cuInvoice.getCuInvoiceId());
		
		List<?> InventoryAccount = getInventAccountDetails("Outside",cuInvoice.getCuInvoiceId());
		
		Iterator<?> aIterator =InventoryAccount.iterator() ;
		while (aIterator.hasNext()) {
			Object[] aObj = (Object[]) aIterator.next();
			dBorCrAmount = dBorCrAmount.add((BigDecimal)aObj[2]);
		}
		
		itsLogger.info("coAccountfromInventoryAccountNumber "+coAccountfromInventoryAccountNumber.getCoAccountId());
		
		if(coAccountfromInventoryAccountNumber.getCoAccountId()!=null)
		insertWarehouseInv(glTransaction, coAccountfromInventoryAccountNumber, cuInvoice,aColedgersource,dBorCrAmount,dBorDrAmount);		
		
		 dBorCrAmount= new BigDecimal(0);
		 dBorDrAmount= new BigDecimal(0);
		/* Sales Tax */

		insertSalesTaxcuInv(glTransaction, CoAccountIdsalesTaxInv, cuInvoice,aColedgersource,dBorCrAmount);		

		/* Sales shipping */

		insertSales(glTransaction, CoAccountIdShippingInventoryfromcoAccountdesc, cuInvoice,aColedgersource,dBorCrAmount);
		
		 dBorCrAmount= new BigDecimal(0);
		 dBorDrAmount= new BigDecimal(0);
		
		 //Purchase account from division 2d
		List<?> InventoryAccount1 = getInventAccountDetails("Outside",cuInvoice.getCuInvoiceId());
		Iterator<?> aIterator1 =InventoryAccount1.iterator() ;
		
		while (aIterator1.hasNext()) {
		 Object[] aObj = (Object[]) aIterator1.next();
		 accNoArray1 = ((String)aObj[1]).split("-");
		 customAccNo1 = accNoArray1[0]+"-"+getsubAccountfromDivision(cuInvoice.getCuInvoiceId());
		 dBorDrAmount = (BigDecimal)aObj[2];
		 CoAccountIdPurchases = getcoAccountfromAccountNumber(customAccNo1);
		 if(CoAccountIdPurchases!=null) 
		 {
			 CoAccountIdPurchases.setDescription(CoAccountIdPurchases.getDescription());
			 CoAccountIdPurchases.setCoAccountId(CoAccountIdPurchases.getCoAccountId());
			 CoAccountIdPurchases.setNumber(customAccNo1);
		 }
		 else
		 {
			 CoAccountIdPurchases = getcoAccountfromAccountNumber((String)aObj[1]);	
			 CoAccountIdPurchases.setNumber((String)aObj[1]);
		 }
		 
		 if(coAccountfromInventoryAccountNumber.getCoAccountId()!=null)
		 insertPurchases(glTransaction, CoAccountIdPurchases, cuInvoice,aColedgersource,dBorCrAmount,dBorDrAmount);
		
		}
	
	
	}

	@Override
	public ArrayList<SysAccountLinkage> getAllsysaccountLinkage(Cuinvoice cuInvoice)throws BankingException {
		
		ArrayList<SysAccountLinkage> arrayListSysAcc = new ArrayList<SysAccountLinkage>();
		Query aQuery = null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  SysAccountLinkage");
			arrayListSysAcc = (ArrayList<SysAccountLinkage>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
		return  arrayListSysAcc;
		
	}
	
	
	public void insertAccountRecievable(GlTransaction glTransaction,Coaccount CoAccountIdar,Cuinvoice cuInvoice,Coledgersource aColedgersource,BigDecimal dBorCrAmount) throws BankingException
	{
		/*Account Receivable*/
		
		itsLogger.info("/* Account Receivable */");
		
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		
		glTransaction.setCoAccountId(CoAccountIdar.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdar.getNumber());
		
		BigDecimal costAmount = new BigDecimal(0);
		
		costAmount=cuInvoice.getFreight().add(cuInvoice.getSubtotal()).add(cuInvoice.getTaxAmount());
		//costAmount = cuInvoice.getInvoiceAmount();
		
		//itsLogger.info("FrieghtAmt:"+theFrieght+" TaxAmount:"+taxRate+" SubTotal:"+theSubTotal+" CostTotal:"+theTotal);
		
				if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setDebit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setCredit(costAmount);
				}
				else if(costAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setCredit(costAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setDebit( dBorCrAmount);
				}else{
					glTransaction.setCredit(dBorCrAmount);
					glTransaction.setDebit( costAmount);
				}
				
				
				if (costAmount.compareTo(BigDecimal.ZERO) != 0) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
			}
	}
	
	public void insertWarehouseInv(GlTransaction glTransaction,Coaccount CoAccountIdar,Cuinvoice cuInvoice,Coledgersource aColedgersource,BigDecimal dBorCrAmount,BigDecimal dBorDrAmount) throws BankingException
	{
		/*Account Receivable*/
		
		itsLogger.info("/* Warehouse Inv */");
		
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		
		glTransaction.setCoAccountId(CoAccountIdar.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdar.getNumber());

		
		//itsLogger.info("FrieghtAmt:"+theFrieght+" TaxAmount:"+taxRate+" SubTotal:"+theSubTotal+" CostTotal:"+theTotal);
		
				if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setDebit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setCredit(dBorDrAmount);
				}
				else if(dBorDrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setCredit(dBorDrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setDebit( dBorCrAmount);
				}else{
					glTransaction.setCredit(dBorCrAmount);
					glTransaction.setDebit( dBorDrAmount);
				}
				
				
				if (dBorCrAmount.compareTo(BigDecimal.ZERO) != 0) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
		
					
			}
		
	}
	
	public void insertSales(GlTransaction glTransaction,Coaccount CoAccountIdsalesid,Cuinvoice cuInvoice,Coledgersource aColedgersource,BigDecimal dBorCrAmount) throws BankingException
	{
		/*Shipping*/
		
		itsLogger.info("/* Shipping */");
		
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		
		glTransaction.setCoAccountId(CoAccountIdsalesid.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdsalesid.getNumber());

		
		//itsLogger.info("FrieghtAmt:"+theFrieght+" TaxAmount:"+taxRate+" SubTotal:"+theSubTotal+" CostTotal:"+theTotal);
		
		itsLogger.info("FrieghtAmt:"+cuInvoice.getFreight()+" TaxAmount:"+cuInvoice.getTaxAmount()+" SubTotal:"+cuInvoice.getSubtotal()+" CostTotal:"+cuInvoice.getCostTotal());
		
			/*	glTransaction.setCredit(cuInvoice.getSubtotal().add(cuInvoice.getFreight()));
				glTransaction.setDebit( dBorCrAmount);*/
				
				if(cuInvoice.getSubtotal().add(cuInvoice.getFreight()).compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setDebit(cuInvoice.getSubtotal().add(cuInvoice.getFreight()).multiply(new BigDecimal("-1.0000")));
					glTransaction.setCredit(dBorCrAmount);
				}
				else if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setCredit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setDebit( cuInvoice.getSubtotal().add(cuInvoice.getFreight()));
				}
				else{
					glTransaction.setCredit(cuInvoice.getSubtotal().add(cuInvoice.getFreight()));
					glTransaction.setDebit(dBorCrAmount);
				}
				
				
				if ((cuInvoice.getSubtotal().add(cuInvoice.getFreight()))!=null && (cuInvoice.getSubtotal().add(cuInvoice.getFreight())).compareTo(BigDecimal.ZERO) != 0 ) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
		
					
			}
		
	}
	
	
	public void insertSalesTaxcuInv(GlTransaction glTransaction,Coaccount CoAccountIdSalesTaxcuInv,Cuinvoice cuInvoice,Coledgersource aColedgersource,BigDecimal dBorCrAmount) throws BankingException
	{
		
		itsLogger.info("/* Sales Tax */");
		
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		// Sales Tax
		glTransaction.setCoAccountId(CoAccountIdSalesTaxcuInv.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdSalesTaxcuInv.getNumber());

		// Sales Tax
		
		itsLogger.info("FrieghtAmt:"+cuInvoice.getFreight()+" TaxAmount:"+cuInvoice.getTaxAmount()+" SubTotal:"+cuInvoice.getSubtotal()+" CostTotal:"+cuInvoice.getCostTotal());
		
				/*glTransaction.setCredit(cuInvoice.getTaxAmount());
				glTransaction.setDebit( dBorCrAmount);*/
				
				if(cuInvoice.getTaxAmount().compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setDebit(cuInvoice.getTaxAmount().multiply(new BigDecimal("-1.0000")));
					glTransaction.setCredit(dBorCrAmount);
				}
				else if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setCredit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setDebit( cuInvoice.getTaxAmount());
				}else{
					glTransaction.setCredit(cuInvoice.getTaxAmount());
					glTransaction.setDebit(dBorCrAmount);
				}
				
				
		
				if ((cuInvoice.getSubtotal().add(cuInvoice.getFreight()))!=null && cuInvoice.getTaxAmount().compareTo(BigDecimal.ZERO) != 0  ) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
		
					
			}
		
	}
	
	
	public void insertPurchases(GlTransaction glTransaction,Coaccount CoAccountIdSalesTaxcuInv,Cuinvoice cuInvoice,Coledgersource aColedgersource,BigDecimal dBorCrAmount,BigDecimal dBorDrAmount) throws BankingException
	{
		
		itsLogger.info("/* Purchases */");
		
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		// Sales Tax
		glTransaction.setCoAccountId(CoAccountIdSalesTaxcuInv.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdSalesTaxcuInv.getNumber());

		// Sales Tax
		
		/*glTransaction.setCredit(dBorCrAmount);
		glTransaction.setDebit(dBorDrAmount );*/
		
		if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
			glTransaction.setDebit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
			glTransaction.setCredit(dBorDrAmount);
		}
		else if(dBorDrAmount.compareTo(new BigDecimal("0.0000"))==-1){
			glTransaction.setCredit(dBorDrAmount.multiply(new BigDecimal("-1.0000")));
			glTransaction.setDebit( dBorCrAmount);
		}else{
			glTransaction.setCredit(dBorCrAmount);
			glTransaction.setDebit( dBorDrAmount);
		}
		
				if (dBorDrAmount.compareTo(BigDecimal.ZERO) != 0  ) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
			}
	}
		
	//Get Sub Account from Division
	@Override
	public String getsubAccountfromDivision(Integer cuInvoicID) throws BankingException
	{
		String subAccountID = "";		
		Query aQuery =null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery("SELECT coD.subAccount FROM coDivision coD,cuInvoice cuI WHERE coD.coDivisionID = cuI.coDivisionID AND cuI.cuInvoiceID="+cuInvoicID);
			subAccountID = (String) aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
			
		return subAccountID;
	}
	
	public Short getReleaseTypeID(Integer releaseIDs) throws BankingException
	{
		Short releaseID = -1;		
		Query aQuery =null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery("SELECT jo.ReleaseType FROM joReleaseDetail joD ,joRelease jo WHERE jo.joReleaseID = joD.joReleaseID AND joD.joReleaseDetailID = "+releaseIDs);
			releaseID = (Short) aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
			
		return releaseID;
	}
	
	@Override
	public Coaccount getcoAccountfromAccountNumber(String accNumber) throws BankingException
	{
		Coaccount coAccount = null;	
		ArrayList<Object> coAccountList=new ArrayList<Object>(); 
		Query aQuery =null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery("SELECT coAccountID,Description from coAccount where Number= '"+accNumber+"'");
			coAccountList = (ArrayList<Object>) aQuery.list();
			
			Iterator<?> itrList=coAccountList.iterator();
			while (itrList.hasNext()) {
				coAccount = new Coaccount();	
				Object[] aObj = (Object[]) itrList.next();
				coAccount.setCoAccountId((Integer)aObj[0]);
				coAccount.setDescription((String)aObj[1]);
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
			
		return coAccount;	
	}
		
	public List<Object> getInventAccountDetails(String jobType,Integer cuInvoiceID) throws BankingException
	{
		List<Object> deptAccountDetails = new ArrayList<Object>();		
		Query aQuery = null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			/*Query aQuery = aSession.createSQLQuery("SELECT prd.Description AS Department,co.Number AS AccountNo, "
					+ "SUM(IF(cuD.QuantityBilled IS NULL OR cuD.QuantityBilled = '', 0, cuD.QuantityBilled) * IF(pr.AverageCost IS NULL OR pr.AverageCost = '', 0, pr.AverageCost))  AS avgcost "
					+ "FROM cuInvoice cu JOIN cuInvoiceDetail cuD ON cu.cuInvoiceID = cuD.cuInvoiceID "
					+ "JOIN prMaster pr ON cuD.prMasterID= pr.prMasterID "
					+ "JOIN prDepartment prd ON pr.prDepartmentID = prd.prDepartmentID "
					+ "JOIN coAccount co ON prd.coAccountIDCOGS = co.coAccountID "
					+ "WHERE cu.cuInvoiceID="+cuInvoiceID+" GROUP BY prd.prDepartmentID ");*/
	
			if(jobType.equals("Inside"))
			{
			aQuery = aSession.createSQLQuery("  SELECT prd.Description AS Department,co.Number AS AccountNo, "
					/*+ "IF(jr.ReleaseType <> 3 ,cu.whseCostTotal, "
					+ "SUM(IF(cuD.QuantityBilled IS NULL OR cuD.QuantityBilled = '', 0, cuD.QuantityBilled) * IF(pr.AverageCost IS NULL OR pr.AverageCost = '', 0, pr.AverageCost)))  AS avgcosts "*/
					+ " SUM(IF(cuD.QuantityBilled IS NULL OR cuD.QuantityBilled = '', 0, cuD.QuantityBilled) * IF(pr.AverageCost IS NULL OR pr.AverageCost = '', 0, pr.AverageCost))  AS avgcosts "
					+ "FROM cuInvoice cu "
					+ "JOIN joReleaseDetail jd ON  cu.joReleaseDetailID =jd.joReleaseDetailID "
					+ "JOIN joRelease jr ON  jd.joReleaseID = jr.joReleaseID "
					+ "JOIN cuInvoiceDetail cuD ON cu.cuInvoiceID = cuD.cuInvoiceID "
					+ "JOIN prMaster pr ON cuD.prMasterID= pr.prMasterID "
					+ "JOIN prDepartment prd ON pr.prDepartmentID = prd.prDepartmentID "
					+ "JOIN coAccount co ON prd.coAccountIDCOGS = co.coAccountID "
					//+ "WHERE  cu.cuInvoiceID="+cuInvoiceID+" GROUP BY prd.prDepartmentID ");
					/*Added by velmurugan 
					 * Inventory cost should be for only inventoryItem
					 * Said by naveed
					 * */
					+ "WHERE  cu.cuInvoiceID="+cuInvoiceID+" and pr.IsInventory=1 GROUP BY prd.prDepartmentID ");
			}
			else
			{
				aQuery = aSession.createSQLQuery("SELECT prd.Description AS Department,co.Number AS AccountNo, "
						/*+ "IF(cu.whseCostTotal IS NOT NULL,cu.whseCostTotal, SUM(IF(cuD.QuantityBilled IS NULL OR cuD.QuantityBilled = '', 0, cuD.QuantityBilled) * IF(pr.AverageCost IS NULL OR pr.AverageCost = '', 0, pr.AverageCost)))  AS avgcost "*/
						+ " SUM(IF(cuD.QuantityBilled IS NULL OR cuD.QuantityBilled = '', 0, cuD.QuantityBilled) * IF(pr.AverageCost IS NULL OR pr.AverageCost = '', 0, pr.AverageCost))  AS avgcost "
						+ "FROM cuInvoice cu JOIN cuInvoiceDetail cuD ON cu.cuInvoiceID = cuD.cuInvoiceID "
						+ "JOIN prMaster pr ON cuD.prMasterID= pr.prMasterID "
						+ "JOIN prDepartment prd ON pr.prDepartmentID = prd.prDepartmentID "
						+ "JOIN coAccount co ON prd.coAccountIDCOGS = co.coAccountID "
						//+ "WHERE cu.cuInvoiceID="+cuInvoiceID+" GROUP BY prd.prDepartmentID ");
						/*Added by velmurugan 
						 * Inventory cost should be for only inventoryItem
						 * Said by naveed
						 * */
						+ "WHERE cu.cuInvoiceID="+cuInvoiceID+" and pr.IsInventory=1 GROUP BY prd.prDepartmentID ");
			}
			
			deptAccountDetails = (List<Object>) aQuery.list();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
		return deptAccountDetails;
		
	}
		
	public Coaccount getcoAccountfromInventoryAccountNumber(Integer cuInvoiceID) throws BankingException
	{
		Coaccount coAccount = new Coaccount();	
		ArrayList<Object> coAccountList=new ArrayList<Object>(); 
		Query aQuery =null;
		
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery("SELECT co.Number,co.Description,co.coAccountID FROM cuInvoice cu ,coAccount co,prWarehouse prw "
					+ "WHERE co.coAccountID = prw.coAccountIDAsset AND prw.prWarehouseID = cu.prFromWarehouseId AND cu.cuInvoiceID="+cuInvoiceID);
			coAccountList = (ArrayList<Object>) aQuery.list();
			
			
			Iterator<?> itrList=coAccountList.iterator();
			while (itrList.hasNext()) {
			
				Object[] aObj = (Object[]) itrList.next();
				coAccount.setNumber((String)aObj[0]);
				coAccount.setDescription((String)aObj[1]);
				coAccount.setCoAccountId((Integer)aObj[2]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
			
		return coAccount;		
	}

	@Override
	public void receiveCreditDebitMemo(Cuinvoice cuInvoice, Integer yearID,
			Integer periodID, String username) throws BankingException,
			JobException, CompanyException {

		Coaccount CoAccountIdar = new Coaccount();
		Coaccount CoAccountIdsalesTaxInv = new Coaccount();
		Coaccount CoAccountIdSales = new Coaccount();
		GlTransaction glTransaction = new GlTransaction();

		Cofiscalperiod aCofiscalperiod = accountingCyclesService
				.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService
				.getCurrentYear(yearID);

		Coledgersource aColedgersource = getColedgersourceDetail("CI");
		Cuinvoicedetail cuInvDetailsObj = itsJobService
				.getCuInvoiceDetailObj(cuInvoice.getCuInvoiceId());

		ArrayList<SysAccountLinkage> aSysAccountLinkage = getAllsysaccountLinkage(cuInvoice);

		for (SysAccountLinkage sysLinObj : aSysAccountLinkage) {
			// Account Recievable Invoice id
			CoAccountIdar = getCoaccountDetailsBasedoncoAccountid(sysLinObj
					.getCoAccountIdar());
			// Sales Tax Invoice id
			CoAccountIdsalesTaxInv = getCoaccountDetailsBasedoncoAccountid(sysLinObj
					.getCoAccountIdsalesTaxInv());

		}

		CoAccountIdSales = getCoaccountDetailsBasedoncoAccountid(cuInvDetailsObj
				.getCoAccountID());

		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(cuInvoice
				.getRxCustomerId());
		glTransaction
				.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
		glTransaction.setPeriod(aCofiscalperiod.getPeriod());

		glTransaction.setpStartDate(aCofiscalperiod.getStartDate());
		glTransaction.setpEndDate(aCofiscalperiod.getEndDate());

		// year
		glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
		if (aCofiscalyear.getFiscalYear() != null)
			glTransaction.setFyear(aCofiscalyear
					.getFiscalYear());
		glTransaction.setyStartDate(aCofiscalyear.getStartDate());
		glTransaction.setyEndDate(aCofiscalyear.getEndDate());

		// journal
		glTransaction.setJournalId(aColedgersource.getJournalID());
		glTransaction.setJournalDesc(aColedgersource.getDescription());
		glTransaction.setEntrydate(new Date());
		glTransaction.setTransactionDate(cuInvoice.getInvoiceDate());
		glTransaction.setEnteredBy(username);

		// Ponumber and trDesc
		glTransaction.setPoNumber(cuInvoice.getInvoiceNumber());
		glTransaction.setTransactionDesc(liRxmasters.getName());

		BigDecimal dBorCrAmount = new BigDecimal(0);
		BigDecimal dBorDrAmount = new BigDecimal(0);

		if (cuInvoice.getIscredit() == 0) {
			/* Account Receivable */
			insertAccountRecievable(glTransaction, CoAccountIdar, cuInvoice,
					aColedgersource, dBorCrAmount);

			if (cuInvDetailsObj.getTaxable() == 1) {
				/* Sales Tax */
				insertSalesTaxcuInv(glTransaction, CoAccountIdsalesTaxInv,
						cuInvoice, aColedgersource, dBorCrAmount);
			}
			/* Sales */
			insertSales(glTransaction, CoAccountIdSales, cuInvoice,
					aColedgersource, dBorCrAmount);

		} else {

			/* Account Receivable */
			insertAccountRecievable1(glTransaction, CoAccountIdar, cuInvoice,
					aColedgersource, dBorCrAmount);

			if (cuInvDetailsObj.getTaxable() == 1) {
				/* Sales Tax */
				insertSalesTaxcuInv1(glTransaction, CoAccountIdsalesTaxInv,
						cuInvoice, aColedgersource, dBorCrAmount);
			}
			/* Sales */
			insertSales1(glTransaction, CoAccountIdSales, cuInvoice,
					aColedgersource, dBorCrAmount);
		}
	}
	
	public void insertAccountRecievable1(GlTransaction glTransaction,
			Coaccount CoAccountIdar, Cuinvoice cuInvoice,
			Coledgersource aColedgersource, BigDecimal dBorCrAmount)
			throws BankingException {
		/* Account Receivable */

		itsLogger.info("/* Account Receivable */");

		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;

		glTransaction.setCoAccountId(CoAccountIdar.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdar.getNumber());

		BigDecimal costAmount = new BigDecimal(0);

		costAmount = cuInvoice.getFreight().add(cuInvoice.getSubtotal())
				.add(cuInvoice.getTaxAmount());

		// itsLogger.info("FrieghtAmt:"+theFrieght+" TaxAmount:"+taxRate+" SubTotal:"+theSubTotal+" CostTotal:"+theTotal);

		glTransaction.setDebit(dBorCrAmount);
		glTransaction.setCredit(costAmount);

		if (costAmount.compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = saveGltransactionTable(glTransaction);

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
			glLinkage.setStatus(0);
			saveGlLinkageTable(glLinkage);
		}
	}
	
	public void insertSalesTaxcuInv1(GlTransaction glTransaction,
			Coaccount CoAccountIdSalesTaxcuInv, Cuinvoice cuInvoice,
			Coledgersource aColedgersource, BigDecimal dBorCrAmount)
			throws BankingException {

		itsLogger.info("/* Sales Tax */");

		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		// Sales Tax
		glTransaction.setCoAccountId(CoAccountIdSalesTaxcuInv.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdSalesTaxcuInv.getNumber());

		// Sales Tax

		itsLogger.info("FrieghtAmt:" + cuInvoice.getFreight() + " TaxAmount:"
				+ cuInvoice.getTaxAmount() + " SubTotal:"
				+ cuInvoice.getSubtotal() + " CostTotal:"
				+ cuInvoice.getCostTotal());

		glTransaction.setDebit(cuInvoice.getTaxAmount());
		glTransaction.setCredit(dBorCrAmount);

		if ((cuInvoice.getSubtotal().add(cuInvoice.getFreight())) != null
				&& cuInvoice.getTaxAmount().compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = saveGltransactionTable(glTransaction);

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
			glLinkage.setStatus(0);
			saveGlLinkageTable(glLinkage);

		}
	}
		
	public void insertSales1(GlTransaction glTransaction,
			Coaccount CoAccountIdsalesid, Cuinvoice cuInvoice,
			Coledgersource aColedgersource, BigDecimal dBorCrAmount)
			throws BankingException {
		/* Shipping */

		itsLogger.info("/* Shipping */");

		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;

		glTransaction.setCoAccountId(CoAccountIdsalesid.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdsalesid.getNumber());

		// itsLogger.info("FrieghtAmt:"+theFrieght+" TaxAmount:"+taxRate+" SubTotal:"+theSubTotal+" CostTotal:"+theTotal);

		itsLogger.info("FrieghtAmt:" + cuInvoice.getFreight() + " TaxAmount:"
				+ cuInvoice.getTaxAmount() + " SubTotal:"
				+ cuInvoice.getSubtotal() + " CostTotal:"
				+ cuInvoice.getCostTotal());

		glTransaction.setDebit(cuInvoice.getSubtotal().add(
				cuInvoice.getFreight()));
		glTransaction.setCredit(dBorCrAmount);

		if ((cuInvoice.getSubtotal().add(cuInvoice.getFreight())) != null
				&& (cuInvoice.getSubtotal().add(cuInvoice.getFreight()))
						.compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = saveGltransactionTable(glTransaction);

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
			glLinkage.setStatus(0);
			saveGlLinkageTable(glLinkage);
		}
	}

	@Override
	public String getReferencenofromglTransaction(Integer vebillid,
			Integer coledgerid) throws BankingException {

		String referenceNo = "";

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			referenceNo = (String) aSession
					.createSQLQuery(
							"SELECT DISTINCT gt.reference FROM glTransaction gt,glLinkage gl WHERE gt.glTransactionId=gl.glTransactionId AND gl.veBillID ="
									+ vebillid
									+ " AND coLedgerSourceId ="
									+ coledgerid + " and status = 0")
					.uniqueResult();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			BankingException aBankingException = new BankingException(
					e.getMessage(), e);
			throw aBankingException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return referenceNo;
	}
	
	@Override
	public Cofiscalperiod getAllcoFiscalPeriodbasedongivenDate(Date periodDate,
			Integer yearid) {

		Session aSession = null;
		Cofiscalperiod aCofiscalPeriod = new Cofiscalperiod();
		Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("from Cofiscalperiod c where c.startDate <=:periodDate AND c.endDate>=:periodDate");
			query.setParameter("periodDate", periodDate);

			aCofiscalPeriod = (Cofiscalperiod) query.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCofiscalPeriod;
	}

	@Override
	public String insertMissingEntries(int fromId, int toId)
			throws BankingException {

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			for (int i = fromId; i <= toId; i++) {
				System.out.println(i);
				aSession.createSQLQuery("call sp_cpmissingentries(" + i + ")")
						.executeUpdate();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return "success";
	}
	
	// Customer Payment Rollback functionality
	@Override
	public void rollBackGlforCuPayment(GlRollback aGlRollback, Session aSession) throws BankingException {
		
		GlLinkage aGlLinkage = null;
		GlLinkage aGlLinkageObj = null;
		Cofiscalperiod aCofiscalperiod =  getCofiscalPeriodDetail();
		Cofiscalyear aCofiscalyear =  getCofiscalYearDetail();
		
		ArrayList<GlLinkage> arrayglLinkagelist = getAllGlLinkageDetails(aGlRollback.getVeBillID(), aGlRollback.getCoLedgerSourceID());
		ArrayList<GlTransaction> arrayglTransList=new ArrayList<GlTransaction>();
		try{
			
			for(GlLinkage glLinkageObj:arrayglLinkagelist)
			{
				arrayglTransList=getAllglTransactionDetails(glLinkageObj.getGlTransactionId());
				for(GlTransaction glTransObj:arrayglTransList)
				{
						// period
						glTransObj.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
						glTransObj.setPeriod(aCofiscalperiod.getPeriod());
						glTransObj.setpStartDate(aCofiscalperiod.getStartDate());
						glTransObj.setpEndDate(aCofiscalperiod.getEndDate());
	
						// year
						glTransObj.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
						if(aCofiscalyear.getFiscalYear()!=null)
						glTransObj.setFyear(aCofiscalyear.getFiscalYear());
						glTransObj.setyStartDate(aCofiscalyear.getStartDate());
						glTransObj.setyEndDate(aCofiscalyear.getEndDate());
						
						glTransObj.setTransactionDate(new Date());
						glTransObj.setEntrydate(new Date());
					
					   if (glTransObj.getCredit().compareTo(BigDecimal.ZERO) != 0)
					   {
						   // insert rollback creditside
						   aGlLinkageObj = (GlLinkage) aSession.get(GlLinkage.class,glLinkageObj.getGlLinkageId());				   
						   insertCPcreditRollback(glTransObj,aGlLinkageObj);
					   }
					   else
					   {
						   // Insert roll back debitside
						   aGlLinkageObj = (GlLinkage) aSession.get(GlLinkage.class,glLinkageObj.getGlLinkageId());		
						   insertCPdebitRollback( glTransObj,aGlLinkageObj);
					   }
				}
				
				// Update status as roll backed
				String asqlQuery = "update glLinkage set status=1 where glTransactionId ="+glLinkageObj.getGlTransactionId();
				aSession.createSQLQuery(asqlQuery).executeUpdate();
				
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.clear();
		}
	}

	public void insertCPcreditRollback(GlTransaction glTransObj,GlLinkage aGlLinkageObj) throws BankingException
	{		
		int glTransationid=0;
		GlLinkage glLinkage =new GlLinkage();
		try
		{
			
			glTransObj.setDebit(glTransObj.getCredit());
			glTransObj.setCredit(new BigDecimal(0));				
			glTransationid = saveGltransactionTable(glTransObj);				
			glLinkage.setCoLedgerSourceId(aGlLinkageObj.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aGlLinkageObj.getVeBillID());
			glLinkage.setStatus(1);
			saveGlLinkageTable(glLinkage);	
		} catch(Exception excep){
			itsLogger.error(excep.getMessage(), excep);
			BankingException aVendorException = new BankingException(excep.getMessage(),
					excep);
			throw aVendorException;
		} 
	}
	
	public void insertCPdebitRollback(GlTransaction glTransObj,GlLinkage aGlLinkageObj)throws BankingException
	{
		int glTransationid=0;
		GlLinkage glLinkage =new GlLinkage();
		try
		{				
			glTransObj.setCredit(glTransObj.getDebit());
			glTransObj.setDebit(new BigDecimal(0));
			glTransationid = saveGltransactionTable(glTransObj);
			glLinkage.setCoLedgerSourceId(aGlLinkageObj.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aGlLinkageObj.getVeBillID());
			glLinkage.setStatus(1);
			saveGlLinkageTable(glLinkage);	
		}
		catch(Exception excep){
			itsLogger.error(excep.getMessage(), excep);
			BankingException aVendorException = new BankingException(excep.getMessage(),
					excep);
			throw aVendorException;
		}
	}

	@Override
	public void insertInventoryAdjustmentCount(Integer prWarehouseid,
			Integer prInventoryid, BigDecimal Amount,String username,String prStatus)throws InventoryException,BankingException {
		
			Prwarehouse prwarehouseobj = aInventoryService.getPrwarehouse(prWarehouseid);
			
			Coaccount CoAccountforCOG = getCoaccountDetailsBasedoncoAccountid(prwarehouseobj.getAdjustCogAccountID());
			Coaccount CoAccountforInventory = getCoaccountDetailsBasedoncoAccountid(prwarehouseobj.getCoAccountIdasset());
	
			Cofiscalperiod aCofiscalperiod =  getCofiscalPeriodDetail();
			Cofiscalyear aCofiscalyear =  getCofiscalYearDetail();
			Coledgersource aColedgersource = getColedgersourceDetail("IC");
			
			GlTransaction glTransaction = new GlTransaction();

			// period
			glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
			
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
			
			glTransaction.setTransactionDate(new Date());
			glTransaction.setEnteredBy(username);
			
			// Ponumber and trDesc
			glTransaction.setPoNumber("");
			glTransaction.setTransactionDesc("Inventory Adjustments");
			BigDecimal dBorCrAmount=BigDecimal.ZERO;
			BigDecimal dBorDrAmount=BigDecimal.ZERO;
			
			if(prStatus.equals("Increase"))
			{
				//Inventory Debit
				 dBorCrAmount=BigDecimal.ZERO;
				 dBorDrAmount=Amount;
				 insertInventoryCount( glTransaction, CoAccountforInventory, prInventoryid, aColedgersource, dBorCrAmount, dBorDrAmount);
				
				//COG Credit
				 dBorCrAmount=Amount;
				 dBorDrAmount=BigDecimal.ZERO;
				insertInventoryCount( glTransaction,CoAccountforCOG, prInventoryid, aColedgersource, dBorCrAmount, dBorDrAmount);
			} else {
				//Inventory Credit
				 dBorCrAmount=Amount;
				 dBorDrAmount=BigDecimal.ZERO;
				 insertInventoryCount( glTransaction, CoAccountforInventory, prInventoryid, aColedgersource, dBorCrAmount, dBorDrAmount);
				
				//COG Debit
				 dBorCrAmount=BigDecimal.ZERO;
				 dBorDrAmount=Amount;
				 insertInventoryCount( glTransaction,CoAccountforCOG, prInventoryid, aColedgersource, dBorCrAmount, dBorDrAmount);
			}
	}
	
	public void insertInventoryCount(GlTransaction glTransaction,
			Coaccount CoAccountIdar, Integer prInventoryid,
			Coledgersource aColedgersource, BigDecimal dBorCrAmount,
			BigDecimal dBorDrAmount) throws BankingException {

		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;

		glTransaction.setCoAccountId(CoAccountIdar.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdar.getNumber());
		glTransaction.setCredit(dBorCrAmount);
		glTransaction.setDebit(dBorDrAmount);

		glTransationid = saveGltransactionTable(glTransaction);

		glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
		glLinkage.setGlTransactionId(glTransationid);
		glLinkage.setEntryDate(new Date());
		glLinkage.setVeBillID(prInventoryid);
		glLinkage.setStatus(0);
		saveGlLinkageTable(glLinkage);

	}
	
	@SuppressWarnings("unused")
    public void updateTaxAdjustment(Cuinvoice cuInvoice,Integer yearid,Integer periodid,String username)
            throws BankingException {

        Session aSession = null;
        Transaction aTransaction = null;
        Cuinvoice aOldCuinvoice = null;
        Integer taxAdjustmentStatus = 0;
        List<GlLinkage> aGllist= null;
        GlTransaction glTransaction = new GlTransaction(); 
       try {
        	aOldCuinvoice = new Cuinvoice();
            aSession = itsSessionFactory.openSession();
            aTransaction = aSession.beginTransaction();
            aOldCuinvoice = itsJobService.getCustomerInvoiceDetails(cuInvoice.getCuInvoiceId());
            aTransaction.begin();
            Cuinvoice cuInvoiceobj = (Cuinvoice) aSession.get(Cuinvoice.class,cuInvoice.getCuInvoiceId());
            
            cuInvoiceobj.setTaxAmount(cuInvoice.getTaxAmount());
            if(cuInvoiceobj.getTaxAdjustmentStatus()!=null)
            taxAdjustmentStatus = cuInvoiceobj.getTaxAdjustmentStatus();
            cuInvoiceobj.setInvoiceAmount(cuInvoice.getInvoiceAmount());
            cuInvoiceobj.setTaxRate(cuInvoice.getTaxRate());
            cuInvoiceobj.setTaxAdjustmentStatus(1);
            cuInvoiceobj.setNote(cuInvoice.getNote());
            cuInvoiceobj.setTaxfreight(cuInvoice.getTaxfreight());
            cuInvoiceobj.setCoTaxTerritoryId(cuInvoice.getCoTaxTerritoryId());
            aSession.update(cuInvoiceobj);
            aTransaction.commit();
           
			itsJobService.saveCustomerInvoiceLog(aOldCuinvoice,cuInvoiceobj,"CI-Edited-Tax Adjustments",1,periodid,yearid);
			
			ArrayList<SysAccountLinkage> aSysAccountLinkage = getAllsysaccountLinkage(cuInvoice);
            Coaccount CoAccountIdar = new Coaccount();
            Coaccount CoAccountIdsalesTaxInv = new Coaccount();
            
    		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodid);
    		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearid);
    		Coledgersource aColedgersource = getColedgersourceDetail( "CI");
    		Rxmaster liRxmasters = getTransactionDescriptionfromrxMasterID(cuInvoiceobj.getRxCustomerId());

            for (SysAccountLinkage sysobj : aSysAccountLinkage) {
                CoAccountIdar = getCoaccountDetailsBasedoncoAccountid(sysobj
                        .getCoAccountIdar());
                CoAccountIdsalesTaxInv = getCoaccountDetailsBasedoncoAccountid(sysobj
                        .getCoAccountIdsalesTaxInv());
            }
            
            
            glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
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
    		glTransaction.setTransactionDate(cuInvoice.getInvoiceDate());
    		glTransaction.setEnteredBy(username);

    		// Ponumber and trDesc
    		glTransaction.setPoNumber(cuInvoiceobj.getInvoiceNumber());
    		glTransaction.setTransactionDesc(liRxmasters.getName());
    		
    		BigDecimal dBorCrAmount= BigDecimal.ZERO;
    		BigDecimal dBorDrAmount= BigDecimal.ZERO;
            
           	
			if(cuInvoice.getTaxAmount().compareTo(BigDecimal.ZERO) == 0)
			{
				
				insertARcuInvTaxadj(glTransaction, CoAccountIdar, aOldCuinvoice,aColedgersource,dBorCrAmount,"AR","Rollback");
				insertARcuInvTaxadj(glTransaction, CoAccountIdsalesTaxInv, aOldCuinvoice,aColedgersource,dBorCrAmount,"Stax","Rollback");			
			}
			else
			{
				if(aOldCuinvoice.getTaxAmount().compareTo(BigDecimal.ZERO)!=0)
				{
				insertARcuInvTaxadj(glTransaction, CoAccountIdar, aOldCuinvoice,aColedgersource,dBorCrAmount,"AR","Rollback");
				insertARcuInvTaxadj(glTransaction, CoAccountIdsalesTaxInv, aOldCuinvoice,aColedgersource,dBorCrAmount,"Stax","Rollback");
				}
				
				insertARcuInvTaxadj(glTransaction, CoAccountIdar, cuInvoice,aColedgersource,dBorCrAmount,"AR","Normal");
				insertARcuInvTaxadj(glTransaction, CoAccountIdsalesTaxInv, cuInvoice,aColedgersource,dBorCrAmount,"Stax","Normal");
			}
            
          
        }catch (Exception excep) {
            itsLogger.error(excep.getMessage(), excep);
            BankingException aVendorException = new BankingException(
                    excep.getMessage(), excep);
            throw aVendorException;
        }finally{
            aSession.flush();
            aSession.close();
        }
    }
	
	public void insertARcuInvTaxadj(GlTransaction glTransaction,Coaccount CoAccountIdSalesTaxcuInv,Cuinvoice cuInvoice,Coledgersource aColedgersource,BigDecimal dBorCrAmount,String acctype,String statusbit) throws BankingException
	{
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;
		
		glTransaction.setCoAccountId(CoAccountIdSalesTaxcuInv.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdSalesTaxcuInv.getNumber());

		if(statusbit.equals("Normal"))
		{
			if(acctype.equals("AR"))		
			{
					if(cuInvoice.getTaxAmount().compareTo(new BigDecimal("0.0000"))==-1){
						glTransaction.setCredit(cuInvoice.getTaxAmount().multiply(new BigDecimal("-1.0000")));
						glTransaction.setDebit(dBorCrAmount);
					}
					else if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
						glTransaction.setDebit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
						glTransaction.setCredit( cuInvoice.getTaxAmount());
					}else{
						glTransaction.setDebit(cuInvoice.getTaxAmount());
						glTransaction.setCredit(dBorCrAmount);
					}
			}
			else
			{

				if(cuInvoice.getTaxAmount().compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setDebit(cuInvoice.getTaxAmount().multiply(new BigDecimal("-1.0000")));
					glTransaction.setCredit(dBorCrAmount);
				}
				else if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setCredit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setDebit( cuInvoice.getTaxAmount());
				}else{
					glTransaction.setCredit(cuInvoice.getTaxAmount());
					glTransaction.setDebit(dBorCrAmount);
				}
			}
		}
		else
		{
			if(acctype.equals("AR"))		
			{
					if(cuInvoice.getTaxAmount().compareTo(new BigDecimal("0.0000"))==-1){
						glTransaction.setDebit(cuInvoice.getTaxAmount().multiply(new BigDecimal("-1.0000")));
						glTransaction.setCredit(dBorCrAmount);
					}
					else if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
						glTransaction.setCredit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
						glTransaction.setDebit( cuInvoice.getTaxAmount());
					}else{
						glTransaction.setCredit(cuInvoice.getTaxAmount());
						glTransaction.setDebit(dBorCrAmount);
					}
			}
			else
			{
				if(cuInvoice.getTaxAmount().compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setCredit(cuInvoice.getTaxAmount().multiply(new BigDecimal("-1.0000")));
					glTransaction.setDebit(dBorCrAmount);
				}
				else if(dBorCrAmount.compareTo(new BigDecimal("0.0000"))==-1){
					glTransaction.setDebit(dBorCrAmount.multiply(new BigDecimal("-1.0000")));
					glTransaction.setCredit( cuInvoice.getTaxAmount());
				}else{
					glTransaction.setDebit(cuInvoice.getTaxAmount());
					glTransaction.setCredit(dBorCrAmount);
				}
			}
		}
			
				
				if (cuInvoice.getTaxAmount().compareTo(BigDecimal.ZERO) != 0) {
					glTransationid = saveGltransactionTable(glTransaction);
		
					glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
					glLinkage.setGlTransactionId(glTransationid);
					glLinkage.setEntryDate(new Date());
					glLinkage.setVeBillID(cuInvoice.getCuInvoiceId());
					glLinkage.setStatus(0);
					saveGlLinkageTable(glLinkage);
			}
	}
	
	
	
	public List<GlLinkage> getGLLinkageIDs(Date adjustmentDate,Integer InvoiceID)
            throws BankingException {
        Session aSession = null;
        List<GlLinkage> aList = null;
        try {
            Query query = null;
            aSession = itsSessionFactory.openSession();
            query = aSession
                    .createQuery("FROM GlLinkage gl where "
                            + " veBillID="
                            + InvoiceID
                            + " AND status =0 AND coLedgerSourceId=4220 AND entryDate >'"
                            + adjustmentDate+"'");
            aList=query.list();
        } catch (Exception excep) {
            itsLogger.error(excep.getMessage(), excep);
            BankingException aVendorException = new BankingException(
                    excep.getMessage(), excep);
            throw aVendorException;
        }
        return aList;
    }
	

    public Object[] getGLObjectData(Integer cuInvoiceID, Integer coAccountID)
            throws BankingException {
        Session aSession = null;
        Object[] aObj = null;
        try {
            Query query = null;
            aSession = itsSessionFactory.openSession();
            query = aSession
                    .createQuery("FROM GlLinkage gl,GlTransaction gt  WHERE gl.glTransactionId=gt.glTransactionId AND "
                            + " veBillID="
                            + cuInvoiceID
                            + " AND status =0 AND coLedgerSourceId=4220 AND coAccountID ="
                            + coAccountID);
            System.out.println("Query :" + query);
            Iterator<?> itrr = query.list().iterator();
            while (itrr.hasNext()) {
                aObj = (Object[]) itrr.next();
            }
        } catch (Exception excep) {
            itsLogger.error(excep.getMessage(), excep);
            BankingException aVendorException = new BankingException(
                    excep.getMessage(), excep);
            throw aVendorException;
        }
        return aObj;
    }
   
    public void insertglTaxAdjrollback(GlTransaction glTransObj,
            GlLinkage gllinkobj, String flag,String amountStatus) throws BankingException {

        int glTransationid = 0;
        GlLinkage glLinkage = new GlLinkage();

        try {
           
            if(amountStatus.equals("possitive"))
            {
                if (flag.equals("AR")) {
                    glTransObj.setCredit(glTransObj.getDebit());
                    glTransObj.setDebit(new BigDecimal(0));
                } else {
                    glTransObj.setDebit(glTransObj.getCredit());
                    glTransObj.setCredit(BigDecimal.ZERO);
                }
            }
            else
            {
                if (flag.equals("AR")) {
                    glTransObj.setDebit(glTransObj.getCredit());
                    glTransObj.setCredit(BigDecimal.ZERO);
                   
                } else {
                    glTransObj.setCredit(glTransObj.getDebit());
                    glTransObj.setDebit(BigDecimal.ZERO);
                }
            }
           
            glTransationid = saveGltransactionTable(glTransObj);
            glLinkage.setCoLedgerSourceId(gllinkobj.getCoLedgerSourceId());
            glLinkage.setGlTransactionId(glTransationid);
            glLinkage.setEntryDate(new Date());
            glLinkage.setVeBillID(gllinkobj.getVeBillID());
            glLinkage.setStatus(gllinkobj.getStatus());
            saveGlLinkageTable(glLinkage);

        } catch (Exception excep) {
            itsLogger.error(excep.getMessage(), excep);
            BankingException aVendorException = new BankingException(
                    excep.getMessage(), excep);
            throw aVendorException;
        }
    }
   
    public void insertglNewTaxAdj(GlTransaction glTransObj,
            GlLinkage gllinkobj, String flag) throws BankingException {

        int glTransationid = 0;
        GlLinkage glLinkage = new GlLinkage();

        try {
            if (flag.equals("AR")) {
                if (glTransObj.getDebit().compareTo(BigDecimal.ZERO) < 0) {
                    glTransObj.setCredit(glTransObj.getDebit().negate());
                    glTransObj.setDebit(BigDecimal.ZERO);
                } else {
                    glTransObj.setDebit(glTransObj.getDebit());
                    glTransObj.setCredit(BigDecimal.ZERO);
                }
            } else {
                if (glTransObj.getCredit().compareTo(BigDecimal.ZERO) < 0) {
                    glTransObj.setDebit(glTransObj.getCredit().negate());
                    glTransObj.setCredit(BigDecimal.ZERO);
                } else {
                    glTransObj.setCredit(glTransObj.getCredit());
                    glTransObj.setDebit(BigDecimal.ZERO);
                }
            }
            
            
            
            if(glTransObj.getDebit().compareTo(BigDecimal.ZERO)!=0 && glTransObj.getCredit().compareTo(BigDecimal.ZERO)!=0)
            {
            glTransationid = saveGltransactionTable(glTransObj);
            glLinkage.setCoLedgerSourceId(gllinkobj.getCoLedgerSourceId());
            glLinkage.setGlTransactionId(glTransationid);
            glLinkage.setEntryDate(new Date());
            glLinkage.setVeBillID(gllinkobj.getVeBillID());
            glLinkage.setStatus(0);
            saveGlLinkageTable(glLinkage);
            }

        } catch (Exception excep) {
            itsLogger.error(excep.getMessage(), excep);
            BankingException aVendorException = new BankingException(
                    excep.getMessage(), excep);
            throw aVendorException;
        }
    }
   
    public void updateGlLinkage(int glTranscationID) {
        Session aSession = null;
        Transaction aTransaction = null;
        String aQuery=null;
        try {
            aSession = itsSessionFactory.openSession();
            aTransaction = aSession.beginTransaction();
            aTransaction.begin();
            aQuery = "update glLinkage set status=1 where glTransactionId =" + glTranscationID;
            aSession.createSQLQuery(aQuery).executeUpdate();
            aTransaction.commit();
        } catch (Exception excep) {
            itsLogger.error(excep.getMessage(), excep);
        } finally {
            aSession.flush();
            aSession.close();
            aQuery = null;
        }
    }

    
    // Insert 13th period
	@Override
	public void insertglTransactionwith13thperiod(Integer yearID,Integer periodID,String userName) throws CompanyException {
		
		 Session aSession = null;
	     Transaction aTransaction = null;
	     String Query = "";
	     String Query1 = "";
	     List<?> aList =null;
	     List<?> aList1 =null;
		
		try
		{
	    aSession = itsSessionFactory.openSession();
        aTransaction = aSession.beginTransaction();
        aTransaction.begin();
        
        BigDecimal dBorCrAmount= new BigDecimal(0);
        BigDecimal dBorDrAmount= new BigDecimal(0);
        
        BigDecimal CrAmount= new BigDecimal(0);
        BigDecimal DrAmount= new BigDecimal(0);
		//int glTransationid = 0;
        
    	SysAccountLinkage aSysAccountLinkage = getsysAccountLinkageDetail();
    	Coaccount CoAccountRetainedEarnings  = getCoaccountDetailsBasedoncoAccountid(aSysAccountLinkage.getCoAccountIdretainedEarnings());
        
        Query = "SELECT ca.coAccountID,ca.Number,ca.Description,ca.accountType,SUM(gt.debit),SUM(gt.credit) FROM coAccount ca"
        		+ " LEFT JOIN glTransaction gt ON gt.coAccountID = ca.coAccountID"
        		+ " LEFT JOIN glLinkage gl ON gl.glTransactionID = gt.glTransactionID"
        		+ " WHERE ca.accountType IN ('Income','Expense') AND gt.coFiscalYearId ="+yearID+" AND gl.Status<>1 and gt.period<>13 GROUP BY gt.coAccountID ORDER BY ca.accountType DESC";
        
        aList = aSession.createSQLQuery(Query).list();

		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Cofiscalperiod aCofiscalperiod = accountingCyclesService.getCurrentPeriod(periodID);
		Coledgersource aColedgersource = getColedgersourceDetail("RE");
		
		GlTransaction glTransaction = new GlTransaction();
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
		glTransaction.setPeriod(aCofiscalperiod.getPeriod());
		glTransaction.setpStartDate(aCofiscalperiod.getStartDate());
		glTransaction.setpEndDate(aCofiscalperiod.getEndDate());
		glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
		if(aCofiscalyear.getFiscalYear()!=null)
		glTransaction.setFyear(aCofiscalyear.getFiscalYear());
		glTransaction.setyStartDate(aCofiscalyear.getStartDate());
		glTransaction.setyEndDate(aCofiscalyear.getEndDate());
		glTransaction.setJournalId(aColedgersource.getJournalID());
		glTransaction.setJournalDesc(aColedgersource.getDescription());
		glTransaction.setEntrydate(new Date());
		glTransaction.setTransactionDate(new Date());
		glTransaction.setEnteredBy(userName);
		glTransaction.setPoNumber("End of Year");	
		
		Iterator<?> aIterator = aList.iterator();
		
		// Insert all other accounts
		while(aIterator.hasNext())
		{
			Object[] aObj = (Object[]) aIterator.next();
			glTransaction.setTransactionDesc("EOY: "+(String)aObj[2]);
			glTransaction.setCoAccountId((Integer)aObj[0]);
			glTransaction.setCoAccountNumber((String)aObj[1]);
	   		
	   		DrAmount = (BigDecimal)aObj[4];
	   		CrAmount = (BigDecimal)aObj[5];
	   		
	   		if(DrAmount.compareTo(CrAmount)>0)
			{
	   			glTransaction.setCredit(DrAmount.subtract(CrAmount));
	   			glTransaction.setDebit(BigDecimal.ZERO);
	   			dBorDrAmount = dBorDrAmount.add(DrAmount.subtract(CrAmount));
			}
	   		else
	   		{
	   			glTransaction.setDebit(CrAmount.subtract(DrAmount));
	   			glTransaction.setCredit(BigDecimal.ZERO);
	   			dBorCrAmount = dBorCrAmount.add(CrAmount.subtract(DrAmount));
	   		}
	   		insert13period(glTransaction,aColedgersource,yearID,aSession);
	   		
		}
		// Insert Equity account
		if(dBorDrAmount.compareTo(dBorCrAmount)>0)
		{
			glTransaction.setTransactionDesc("EOY: "+CoAccountRetainedEarnings.getDescription());
			glTransaction.setCoAccountId(CoAccountRetainedEarnings.getCoAccountId());
			glTransaction.setCoAccountNumber(CoAccountRetainedEarnings.getNumber());
			glTransaction.setDebit(dBorDrAmount.subtract(dBorCrAmount));
	   		glTransaction.setCredit(BigDecimal.ZERO);
			insert13period(glTransaction,aColedgersource,yearID,aSession);
		}
		else
		{
			glTransaction.setTransactionDesc("EOY: "+CoAccountRetainedEarnings.getDescription());
			glTransaction.setCoAccountId(CoAccountRetainedEarnings.getCoAccountId());
			glTransaction.setCoAccountNumber(CoAccountRetainedEarnings.getNumber());
			glTransaction.setCredit(dBorCrAmount.subtract(dBorDrAmount));
	   		glTransaction.setDebit(BigDecimal.ZERO);
			insert13period(glTransaction,aColedgersource,yearID,aSession);
		}
		updatecoFiscalYearDetailsclosedStatus(yearID);
		
		
		aSession.createSQLQuery(" call `sp_openingBalance`("+yearID+","+aCofiscalperiod.getCoFiscalPeriodId()+",'"+aCofiscalperiod.getStartDate()+"')").executeUpdate();
		
		
		//Close fiscal period 
	/*	// Equity amount updation in coBalance
		Query1 = "SELECT SUM(debit) AS PeriodDebits,SUM(credit) AS PeriodCredits,prevBalance AS PeriodOpening FROM glTransaction "
				+ "LEFT JOIN (SELECT coAccountID,(PeriodOpening+PeriodDebits-PeriodCredits) AS prevBalance FROM coBalance WHERE coFiscalPeriodID = (SELECT coFiscalPeriodID FROM coFiscalPeriod WHERE coFiscalPeriodID <= "+aCofiscalperiod.getCoFiscalPeriodId()+" ORDER BY coFiscalPeriodID DESC LIMIT 1 OFFSET 1)) cb ON cb.coAccountID = glTransaction.coAccountID "
				+ "WHERE glTransaction.coAccountID ="+CoAccountRetainedEarnings.getCoAccountId()+" AND glTransaction.coFiscalPeriodId ="+aCofiscalperiod.getCoFiscalPeriodId()+" AND glTransaction.coFiscalYearId="+aCofiscalyear.getCoFiscalYearId(); 
		
		 aList1 = aSession.createSQLQuery(Query1).list();
		
		BigDecimal perioddebits=BigDecimal.ZERO;
		BigDecimal periodcredits=BigDecimal.ZERO;
		BigDecimal periodopening=BigDecimal.ZERO;
		 
		 Iterator<?> aIterator1 = aList1.iterator();
			while(aIterator1.hasNext())
			{
				Object[] aObj = (Object[]) aIterator1.next();
				
				perioddebits = (BigDecimal)aObj[0];
				periodcredits = (BigDecimal)aObj[1];
				periodopening = (BigDecimal)aObj[2];
				
			}
		
		aTransaction.commit();
		 aTransaction = aSession.beginTransaction();
	        aTransaction.begin();
		 aSession.createSQLQuery("update coBalance set PeriodDebits ="+perioddebits +", PeriodCredits = "+periodcredits+" ,PeriodOpening = "+ periodopening +" where coAccountID ="+CoAccountRetainedEarnings.getCoAccountId()+" and coFiscalPeriodId = (SELECT coFiscalPeriodID FROM coFiscalPeriod WHERE coFiscalPeriodID <= "+aCofiscalperiod.getCoFiscalPeriodId()+" ORDER BY coFiscalPeriodID DESC LIMIT 1 OFFSET 1)").executeUpdate();*/
			aTransaction.commit();
	} catch (Exception excep) {
        itsLogger.error(excep.getMessage(), excep);
        aTransaction.rollback();
    } finally {
        aSession.flush();
        aSession.close();
    }
		
	}
	
	 // Insert 13th period
	public int insert13period(GlTransaction aglTransaction,Coledgersource aColedgersource,int coFiscalYearID,Session aSession) throws BankingException 
	{
		GlLinkage glLinkage = new GlLinkage();
		int glTransationid = 0;

		glTransationid = saveGltransactionTable1(aglTransaction,aSession);
		itsLogger.info("insert glLinkagecoLedgerSourceId:"+ aColedgersource.getCoLedgerSourceId()+ "\nGlTransactionid:" + glTransationid + "\nVeBillid: "+aglTransaction.getPoNumber());

		glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
		glLinkage.setGlTransactionId(glTransationid);
		glLinkage.setEntryDate(new Date());
		glLinkage.setVeBillID(coFiscalYearID);
		glLinkage.setStatus(0);
		saveGlLinkageTable1(glLinkage,aSession);		
	
		return glTransationid;
	}	
	

	public void updatecoFiscalYearDetailsclosedStatus(Integer year)
			throws CompanyException {

		Session aSession = null;
		Transaction aTransaction = null;
		String strQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			strQuery = "update coFiscalYear set closedStatus= 0 where coFiscalYearID = " + year;
			aSession.createSQLQuery(strQuery).executeUpdate();
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			strQuery = null;
			aSession.flush();
			aSession.close();
		}
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for knowing whether a product is inventory or not.) 
	 *
	 */
	private boolean isNotInventory(Integer prMasterID){
		boolean flag=true;
		Session aSession = null;
		Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query=aSession.createSQLQuery("SELECT IsInventory FROM prMaster WHERE prMasterID=?");
			query.setParameter(0, prMasterID);
			Integer isInventory=new Integer((Byte) query.uniqueResult());
			if(isInventory!=0){
				flag=false;
			}else{
				flag=true;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return flag;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting an account number from the department if the product is non-inventory) 
	 *
	 */
	private String getDepartmentSettings(Integer prMasterID){
		String result=null;
		Session aSession = null;
		Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query=aSession.createSQLQuery("SELECT c.Number AS cgs FROM prDepartment AS a LEFT JOIN coAccount AS c ON a.coAccountIDCOGS=c.coAccountID WHERE prDepartmentID=(SELECT prDepartmentID FROM prMaster WHERE prMasterID=?)");
			query.setParameter(0, prMasterID);
			result=(String) query.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return result;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting coAccountId using its accountNumber when product falls in non-inventory) 
	 *
	 */
	private Integer getCoAccountID(String coAccountNumber){
		Integer coAccountID=0;
		Session aSession = null;
		Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query=aSession.createSQLQuery("SELECT coAccountID FROM coAccount WHERE Number LIKE ?");
			query.setParameter(0, coAccountNumber);
			coAccountID=(Integer) query.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return coAccountID;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting an account number from the department if the product is non-inventory) 
	 *
	 */
	private Integer getDepartmentId(Integer prMasterID){
		//SELECT prDepartmentID FROM prMaster WHERE prMasterID=?
		Integer departmentID=0;
		Session aSession = null;
		Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query=aSession.createSQLQuery("SELECT prDepartmentID FROM prMaster WHERE prMasterID=?");
			query.setParameter(0, prMasterID);
			departmentID=(Integer) query.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return departmentID;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting an account number from the department if the product is non-inventory) 
	 *
	 */
	private Map<String,List<Prmaster>> partitionInventories(List<Prmaster> prMaster){
		Map<String,List<Prmaster>> result=null;
		ArrayList<Prmaster> nonInventory=null;
		ArrayList<Prmaster> inventory=null;		
		if(prMaster.size()!=0){
			result=new LinkedHashMap<String,List<Prmaster>>();
			nonInventory=new ArrayList<Prmaster>();
			inventory=new ArrayList<Prmaster>();
			for(Prmaster master:prMaster){
				if(isNotInventory(master.getPrMasterId())==true){
					nonInventory.add(master);
				}else{
					inventory.add(master);
				}
			}
			result.put("Inventory", inventory);
			result.put("nonInventory", nonInventory);
		}
		return result;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting an account number from the department if the product is non-inventory) 
	 *
	 */
	private Map<Integer,List<Prmaster>> partitionNonInventories(List<Prmaster> prMaster){
		 Map<Integer,List<Prmaster>> nonInventoryCache=new LinkedHashMap<Integer, List<Prmaster>>();
		 if(prMaster.size()!=0){
			 for(Prmaster master:prMaster){
				 Integer departmentID=getDepartmentId(master.getPrMasterId());
				 if(departmentID!=0){					 
					 if(nonInventoryCache.containsKey(departmentID)==false){
						 ArrayList<Prmaster> prMasters=new ArrayList<Prmaster>();
						 prMasters.add(master);
						 prMasters.trimToSize();
						 nonInventoryCache.put(departmentID, prMasters);
					 }else{
						 nonInventoryCache.get(departmentID).add(master);
					 }
				 }
			 }
		 }
		 return nonInventoryCache;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting an account number from the department if the product is non-inventory) 
	 *
	 */
	private Map<String,Object> initPurchases(List<Prmaster> prMaster){
		Map<String,Object> purchaseValues=null;
		if(prMaster.size()!=0){
			purchaseValues=new LinkedHashMap<String, Object>();
			String coAcccountNumber=getDepartmentSettings(prMaster.get(0).getPrMasterId());
			purchaseValues.put("coAccountNumber", coAcccountNumber);
			purchaseValues.put("coAccountId", getCoAccountID(coAcccountNumber));
			double amount=0;
			for(Prmaster master:prMaster){
				amount+=master.getLastCost().doubleValue();
			}
			purchaseValues.put("amount",BigDecimal.valueOf(amount));
		}
		return purchaseValues;
	}
	/**
	 *Created By Simon
	 *Reason for creating : ID#573 (for getting an account number from the department if the product is non-inventory) 
	 *
	 */
	private Coaccount getFreightAccount(){
		Coaccount coAccount=null;
		Session aSession = null;
		Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query=aSession.createQuery("FROM Coaccount as coaccount WHERE coaccount.coAccountId IN (SELECT sysaccLinkage.coAccountIdfreight FROM SysAccountLinkage as sysaccLinkage)");
			List<Coaccount> coAccounts=query.list();
			if(!coAccounts.isEmpty())
				coAccount=coAccounts.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return coAccount;
	}
    }