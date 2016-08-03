package com.turborep.turbotracker.customer.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
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

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.Inventory.service.InventoryConstant;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuPaymentGlpost;
import com.turborep.turbotracker.customer.dao.CuReversePaymentsInv;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.customer.dao.Cureceipttype;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.dao.StatementsBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.employee.dao.RxMasterCategoryView;
import com.turborep.turbotracker.employee.dao.Rxmastercategory1;
import com.turborep.turbotracker.employee.dao.Rxmastercategory2;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.util.JobUtil;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Resource(name = "pdfService")
	private PDFService itspdfService;

	//private List<StatementsBean> arxMasterList;

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "gltransactionService")
	private GltransactionService itsGltransactionService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	//private Integer[] rxMasterIDArray = null;

	Logger itsLogger = Logger.getLogger(CustomerServiceImpl.class);

	@Override
	public Cumaster getCustomerDetails(int theCuMasterId) {
		itsLogger.debug("Retrieving Customer Details List");
		Session aSession = null;
		Cumaster aCustomer = null;
		try {
			aCustomer = new Cumaster();
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aCustomer = (Cumaster) aSession.get(Cumaster.class, theCuMasterId);
			
			if(aCustomer==null)
				aCustomer = new Cumaster();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCustomer;
	}

	public CuTerms getCuTerms(int theCuMasterTermsId) {
		itsLogger.debug("Retrieving CuTerms Details : " + theCuMasterTermsId);
		Session aSession = null;
		CuTerms aCuTerm = null;
		try {
			aCuTerm = new CuTerms();
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aCuTerm = (CuTerms) aSession.get(CuTerms.class, theCuMasterTermsId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuTerm;
	}

	public CuMasterType getCustomerType(int theCustomerTypeId) {
		Session aSession = null;
		CuMasterType aCuMasterType = null;
		try {
			aCuMasterType = new CuMasterType();
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aCuMasterType = (CuMasterType) aSession.get(CuMasterType.class,
					theCustomerTypeId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuMasterType;
	}

	@Override
	public ArrayList<AutoCompleteBean> getcountyAndstate(
			String theCountyAndState) {
		String salesselectQry = "SELECT DISTINCT(County),State,coTaxTerritoryID FROM coTaxTerritory WHERE County like '%"
				+ theCountyAndState + "%'";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				String aCity = (String) aObj[0];
				String aState = (String) aObj[1];
				Integer Id = (Integer) aObj[2];
				String aCityState = aCity + " " + "(" + aState + ")";
				aUserbean.setValue(aCityState);
				aUserbean.setLabel(aCityState);
				aUserbean.setId(Id);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry =null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public List<CuMasterType> getcuMasterType() {
		itsLogger.debug("Retrieving Customer Type List");
		Session aSession = null;
		List<CuMasterType> aQueryList = null;Query aQuery =null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession
					.createQuery("FROM CuMasterType ORDER BY Code ASC ");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			 aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public BigInteger getPaymentsCount() throws CustomerException {
		String aPaymentsCountStr = "SELECT COUNT(cuReceiptID) AS count FROM cuReceipt";
		Session aSession = null;Query aQuery =null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsCountStr);
			List<?> aList = aQuery.list();
			return (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsCountStr =null;aQuery =null;
		}
	}
	

	@Override
	public Integer getUnappliedPaymentCount() throws CustomerException {
		/*
		String aPaymentsCountStr = "SELECT (cr.Amount - SUM(cdp.PaymentApplied)) AS unApplied"
				+ " FROM rxMaster rm RIGHT JOIN cuReceipt cr ON (rm.rxMasterID = cr.rxCustomerID)"
				+ " LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID)"
				+ " GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID HAVING unApplied>0 ORDER BY  unApplied DESC";
		*/
		String aPaymentsCountStr = "SELECT cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, CONCAT(rm.Name, ' ', rm.FirstName) AS Customer,cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,SUM(cdp.PaymentApplied) AS PaymentRecieved,(cr.Amount - SUM(IFNULL(cdp.PaymentApplied,0))) AS unApplied"
				+ " FROM rxMaster rm RIGHT JOIN cuReceipt cr ON (rm.rxMasterID = cr.rxCustomerID) LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID) where reversePaymentStatus <> 1 "
				+ "GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID HAVING unApplied>0 ";
		
		
		Session aSession = null;Query aQuery =null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsCountStr);
			List<?> aList = aQuery.list();
			return aList.size();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsCountStr =null;aQuery =null;
		}
	}
	

	@Override
	public ArrayList<CustomerPaymentBean> getCustomerPaymentsList(int theFrom,
			int theRows,String sidx,String sord) throws CustomerException {
		
		if(sidx.equalsIgnoreCase("receiptDate")){
			sidx = "cr.ReceiptDate";
		}else if(sidx.equalsIgnoreCase("customer")){
			sidx = "Customer";
		}else if(sidx.equalsIgnoreCase("reference")){
			sidx = "cr.Reference";
		}else if(sidx.equalsIgnoreCase("memo")){
			sidx = "cr.Memo";
		}else if(sidx.equalsIgnoreCase("amount")){
			sidx = "cr.Amount";
		}else{
			sidx = "cr.ReceiptDate";
		}
		
		String aPaymentsselectQry = "SELECT "
				+ "cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, "
				+ "CONCAT(rm.Name, ' ', rm.FirstName) AS Customer, "
				+ "cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,cr.reversePaymentStatus FROM rxMaster rm "
				+ "RIGHT JOIN cuReceipt cr ON rm.rxMasterID = cr.rxCustomerID ORDER BY "+sidx +" "+sord.toUpperCase()+" LIMIT "
				+ theFrom + ", " + theRows + ";";
		Session aSession = null;Query aQuery =null;
		ArrayList<CustomerPaymentBean> aCuPaymentsList = new ArrayList<CustomerPaymentBean>();
		try {
			CustomerPaymentBean aCuPayment = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuReceiptID((Integer) aObj[0]);
				aCuPayment.setRxMasterID((Integer) aObj[1]);
				if (aObj[2] != null) {
					aCuPayment.setReceiptDate((String) DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy"));
				}
				aCuPayment.setCustomer((String) aObj[3]);
				aCuPayment.setReference((String) aObj[4]);
				aCuPayment.setMemo((String) (aObj[5]==null?"":aObj[5]));
				aCuPayment.setAmount((BigDecimal) aObj[6]);
				aCuPayment.setCuReceiptTypeID((Short) aObj[7]);
				aCuPayment.setReversePaymentStatus((Boolean) aObj[8]);
				aCuPaymentsList.add(aCuPayment);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry =null;aQuery =null;
		}
		return aCuPaymentsList;
	}
	
	
	@Override
	public BigInteger getcreditdebitmemoCount() throws CustomerException {
		String aPaymentsCountStr = " SELECT DISTINCT count(cuInvoiceID) FROM cuInvoice WHERE CreditMemo=1 ORDER BY InvoiceNumber";
		Query aQuery =null;
		Session aSession = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsCountStr);
			List<?> aList = aQuery.list();
			return (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsCountStr =null;aQuery =null;
		}
	}
	
	
	@Override
	public ArrayList<CustomerPaymentBean> getcreditordebitmemoList(int theFrom,
			int theRows,String column,String sortBy) throws CustomerException {
		String sortByOption="InvoiceNumber";
		
		if(column.equalsIgnoreCase("cuInvoiceID")){
				sortByOption=" cuInvoiceID ";
		}else if(column.equalsIgnoreCase("reference")){
			sortByOption=" InvoiceNumber ";
		}else if(column.equalsIgnoreCase("customer")){
			sortByOption=" Customer ";
		}else if(column.equalsIgnoreCase("receiptDate")){
			sortByOption=" InvoiceDate ";
		}else if(column.equalsIgnoreCase("amount")){
			sortByOption=" InvoiceAmount ";
		}
		
		
		String aPaymentsselectQry = " SELECT DISTINCT cuInvoiceID, InvoiceNumber,InvoiceDate,InvoiceAmount,CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Customer,cuInvoice.rxCustomerID,memoStatus  "
				+ " FROM rxMaster , cuInvoice where rxMaster.rxMasterID = cuInvoice.rxCustomerID "
				+ "and CreditMemo=1 ORDER BY "+sortByOption+sortBy.toUpperCase()+" Limit "
				+ theFrom + ", " + theRows + ";";
		Session aSession = null;
		BigDecimal amt=null;Query aQuery =null;
		ArrayList<CustomerPaymentBean> aCuPaymentsList = new ArrayList<CustomerPaymentBean>();
		try {
			CustomerPaymentBean aCuPayment = null;	
			aSession = itsSessionFactory.openSession();	
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuInvoiceID((Integer) aObj[0]);
				aCuPayment.setReference((String) aObj[1]);

				if (aObj[2] != null) {
					aCuPayment.setReceiptDate((String) DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy"));
				}
				amt=(BigDecimal)aObj[3];
				if(amt.signum()==-1)
				aCuPayment.setAmount(amt.negate());	
				else
				aCuPayment.setAmount(amt);
				aCuPayment.setCustomer((String) aObj[4]);				
				aCuPayment.setRxMasterID((Integer) aObj[5]);
				aCuPayment.setMemoStatus((Byte) aObj[6]);
				aCuPaymentsList.add(aCuPayment);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry =null;aQuery =null;
		}
		return aCuPaymentsList;
	}
	
	public Integer getcountpaymentsListSearch(int theFrom,int theRows, String searchText, Date fromdate, Date todate, String status) throws CustomerException
	{
		String condition = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if (status.equalsIgnoreCase("from")) {
			condition = "AND cr.ReceiptDate>='" + sdf.format(fromdate) + "'";
		}
		if (status.equalsIgnoreCase("to")) {
			condition = "AND cr.ReceiptDate<='" + sdf.format(todate) + "'";
		}
		if (status.equalsIgnoreCase("both")) {
			condition = "AND (cr.ReceiptDate BETWEEN '" + sdf.format(fromdate)
					+ "' AND '" + sdf.format(todate) + "' )";
		}
		String aPaymentsselectQry = "SELECT Count(cr.cuReceiptID) FROM rxMaster rm RIGHT JOIN cuReceipt cr ON rm.rxMasterID = cr.rxCustomerID WHERE (rm.Name LIKE '%"
				+ searchText
				+ "%' OR rm.FirstName LIKE '%"
				+ searchText
				+ "%' OR cr.Memo LIKE '%"
				+ searchText
				+ "%' OR cr.Amount LIKE '%"
				+ searchText
				+ "%' OR cr.Reference LIKE '%"
				+ searchText
				+ "%') "
				+ condition
				+ " ORDER BY cr.ReceiptDate Desc; ";
		
		Query aQuery =null;
		Session aSession = null;
		Integer totalCount = 0;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			totalCount = ((BigInteger)aQuery.list().get(0)).intValue();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry =null;aQuery =null;
		}
		return totalCount;
	
	}
	
	@Override
	public ArrayList<CustomerPaymentBean> getCustomerPaymentsListSearch(
			int theFrom, int theRows,String sidx,String sord, String searchText, Date fromdate,
			Date todate, String status) throws CustomerException {

		String condition = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// System.out.println("fromdate ::"+fromdate+" todate ::"+todate);
		// System.out.println("fromdate ::"+sdf.format(fromdate)+" todate ::"+sdf.format(todate));
		
		if(sidx.equalsIgnoreCase("receiptDate")){
			sidx = "cr.ReceiptDate";
		}else if(sidx.equalsIgnoreCase("customer")){
			sidx = "Customer";
		}else if(sidx.equalsIgnoreCase("reference")){
			sidx = "cr.Reference";
		}else if(sidx.equalsIgnoreCase("memo")){
			sidx = "cr.Memo";
		}else if(sidx.equalsIgnoreCase("amount")){
			sidx = "cr.Amount";
		}else{
			sidx = "cr.ReceiptDate";
		}
		

		if (status.equalsIgnoreCase("from")) {
			condition = "AND cr.ReceiptDate>='" + sdf.format(fromdate) + "'";
		}

		if (status.equalsIgnoreCase("to")) {
			condition = "AND cr.ReceiptDate<='" + sdf.format(todate) + "'";
		}

		if (status.equalsIgnoreCase("both")) {

			condition = "AND (cr.ReceiptDate BETWEEN '" + sdf.format(fromdate)
					+ "' AND '" + sdf.format(todate) + "' )";
			// condition =
			// "AND cr.ReceiptDate>="+sdf.format(fromdate)+" AND cr.ReceiptDate<="+sdf.format(todate)
			// +" ";
		}
		String aPaymentsselectQry = "SELECT cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate,CONCAT(rm.Name, ' ', rm.FirstName) AS Customer, cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,cr.reversePaymentStatus FROM rxMaster rm RIGHT JOIN cuReceipt cr ON rm.rxMasterID = cr.rxCustomerID WHERE (rm.Name LIKE '%"
				+ searchText
				+ "%' OR rm.FirstName LIKE '%"
				+ searchText
				+ "%' OR cr.Memo LIKE '%"
				+ searchText
				+ "%' OR cr.Amount LIKE '%"
				+ searchText
				+ "%' OR cr.Reference LIKE '%"
				+ searchText
				+ "%') "
				+ condition
				+ " ORDER BY "+sidx +" "+sord.toUpperCase()+" LIMIT "
				+ theFrom
				+ ", " + theRows + ";";
		Query aQuery =null;
		/*
		 * String aPaymentsselectQry = "SELECT " +
		 * "cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, " +
		 * "CONCAT(rm.Name, ' ', rm.FirstName) AS Customer, " +
		 * "cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID FROM rxMaster rm "
		 * +
		 * "RIGHT JOIN cuReceipt cr ON rm.rxMasterID = cr.rxCustomerID ORDER BY cr.ReceiptDate DESC LIMIT 5"
		 * ;
		 */Session aSession = null;
		ArrayList<CustomerPaymentBean> aCuPaymentsList = new ArrayList<CustomerPaymentBean>();
		try {
			CustomerPaymentBean aCuPayment = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuReceiptID((Integer) aObj[0]);
				aCuPayment.setRxMasterID((Integer) aObj[1]);
				if (aObj[2] != null) {
					aCuPayment.setReceiptDate((String) DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy"));
				}
				aCuPayment.setCustomer((String) aObj[3]);
				aCuPayment.setReference((String) aObj[4]);
				aCuPayment.setMemo((String) (aObj[5]==null?"":aObj[5]));
				aCuPayment.setAmount((BigDecimal) aObj[6]);
				aCuPayment.setCuReceiptTypeID((Short) aObj[7]);
				aCuPayment.setReversePaymentStatus((Boolean) aObj[8]);
				aCuPaymentsList.add(aCuPayment);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry =null;aQuery =null;
		}
		return aCuPaymentsList;
	}

	
	public Integer getcountUnappliedPaymentsListSearch(int theFrom,int theRows, String searchText, Date fromdate, Date todate, String status) throws CustomerException
	{
		String condition = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		if (status.equalsIgnoreCase("from")) {
			condition = "AND cr.ReceiptDate>='" + sdf.format(fromdate) + "'";
		}

		if (status.equalsIgnoreCase("to")) {
			condition = "AND cr.ReceiptDate<='" + sdf.format(todate) + "'";
		}

		if (status.equalsIgnoreCase("both")) {

			condition = "AND (cr.ReceiptDate BETWEEN '" + sdf.format(fromdate)
					+ "' AND '" + sdf.format(todate) + "' )";
		}

		String aPaymentsselectQry = "SELECT cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, CONCAT(rm.Name, ' ', rm.FirstName) AS Customer,cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,SUM(cdp.PaymentApplied) AS PaymentRecieved,(cr.Amount - SUM(IFNULL(cdp.PaymentApplied,0))) AS unApplied"
				+ " FROM rxMaster rm RIGHT JOIN cuReceipt cr ON (rm.rxMasterID = cr.rxCustomerID) LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID)  WHERE reversePaymentStatus <> 1 and (rm.Name LIKE '%"
				+ searchText
				+ "%' OR rm.FirstName LIKE '%"
				+ searchText
				+ "%' OR cr.Memo LIKE '%"
				+ searchText
				+ "%' OR cr.Amount LIKE '%"
				+ searchText
				+ "%' OR cr.Reference LIKE '%"
				+ searchText
				+ "%') "
				+ condition
				+ "GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID HAVING unApplied>0;";
		
		Query aQuery =null;
		Session aSession = null;
		Integer totalCount = 0;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			totalCount = aQuery.list().size();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry =null;aQuery =null;
		}
		return totalCount;
	
	}
	
	@Override
	public ArrayList<CustomerPaymentBean> getCustomerUnappliedPaymentsListSearch(
			int theFrom, int theRows,String sidx,String sord, String searchText, Date fromdate,
			Date todate, String status) throws CustomerException {

		String condition = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		if(sidx.equalsIgnoreCase("receiptDate")){
			sidx = "cr.ReceiptDate";
		}else if(sidx.equalsIgnoreCase("customer")){
			sidx = "Customer";
		}else if(sidx.equalsIgnoreCase("reference")){
			sidx = "cr.Reference";
		}else if(sidx.equalsIgnoreCase("memo")){
			sidx = "cr.Memo";
		}else if(sidx.equalsIgnoreCase("amount")){
			sidx = "cr.Amount";
		}else{
			sidx = "cr.ReceiptDate";
		}
		

		if (status.equalsIgnoreCase("from")) {
			condition = "AND cr.ReceiptDate>='" + sdf.format(fromdate) + "'";
		}

		if (status.equalsIgnoreCase("to")) {
			condition = "AND cr.ReceiptDate<='" + sdf.format(todate) + "'";
		}

		if (status.equalsIgnoreCase("both")) {

			condition = "AND (cr.ReceiptDate BETWEEN '" + sdf.format(fromdate)
					+ "' AND '" + sdf.format(todate) + "' )";
		}

		String aPaymentsselectQry = "SELECT cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, CONCAT(rm.Name, ' ', rm.FirstName) AS Customer,cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,SUM(cdp.PaymentApplied) AS PaymentRecieved,(cr.Amount - SUM(IFNULL(cdp.PaymentApplied,0))) AS unApplied"
				+ " FROM rxMaster rm RIGHT JOIN cuReceipt cr ON (rm.rxMasterID = cr.rxCustomerID) LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID)  WHERE reversePaymentStatus <> 1 and (rm.Name LIKE '%"
				+ searchText
				+ "%' OR rm.FirstName LIKE '%"
				+ searchText
				+ "%' OR cr.Memo LIKE '%"
				+ searchText
				+ "%' OR cr.Amount LIKE '%"
				+ searchText
				+ "%' OR cr.Reference LIKE '%"
				+ searchText
				+ "%') "
				+ condition
				+ "GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID HAVING unApplied>0 ORDER BY "+sidx +" "+sord.toUpperCase()+"  LIMIT "
				+ theFrom + ", " + theRows + ";";
		Session aSession = null;Query aQuery =null;
		
		System.out.println("showUnappliedAmount::"+aPaymentsselectQry);
		ArrayList<CustomerPaymentBean> aCuPaymentsList = new ArrayList<CustomerPaymentBean>();
		try {
			CustomerPaymentBean aCuPayment = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuReceiptID((Integer) aObj[0]);
				aCuPayment.setRxMasterID((Integer) aObj[1]);
				if (aObj[2] != null) {
					aCuPayment.setReceiptDate((String) DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy"));
				}
				aCuPayment.setCustomer((String) aObj[3]);
				aCuPayment.setReference((String) aObj[4]);
				aCuPayment.setMemo((String) (aObj[5]==null?"":aObj[5]));
				aCuPayment.setAmount((BigDecimal) aObj[6]);
				aCuPayment.setCuReceiptTypeID((Short) aObj[7]);
				aCuPayment.setPaymentApplied((BigDecimal) aObj[8]);
				BigDecimal amount = new BigDecimal(0);
				if (aObj[6] != null) {
					amount = (BigDecimal) aObj[6];
				}
				BigDecimal paymentApplied = new BigDecimal(0);
				if (aObj[9] != null) {
					paymentApplied = (BigDecimal) aObj[9];
				}
				aCuPayment.setPaymentApplied(paymentApplied);
				aCuPaymentsList.add(aCuPayment);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry=null;aQuery =null;
		}
		return aCuPaymentsList;
	}
	
	
	@Override
	public ArrayList<CustomerPaymentBean> getCustomerPaymentUnapplied(
			int theFrom, int theRows,String sidx,String sord, Date fromdate, Date todate, String status)
			throws CustomerException {

		// String aPaymentsselectQry =
		// "SELECT cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate,CONCAT(rm.Name, ' ', rm.FirstName) AS Customer, cr.Reference, cr.Memo,  cr.Amount,cr.cuReceiptTypeID,( SUM(cl.paymentApplied)-cr.Amount ) AS PaymentRecieved FROM cuReceipt cr RIGHT JOIN cuLinkageDetail cl ON cr.cuReceiptID = cl.cuReceiptID JOIN  rxMaster rm ON rm.rxMasterID = cr.rxCustomerID GROUP BY cl.rxCustomerID ORDER BY cr.ReceiptDate DESC LIMIT "
		// + theFrom + ", " + theRows +";";
		
		if(sidx.equalsIgnoreCase("receiptDate")){
			sidx = "cr.ReceiptDate";
		}else if(sidx.equalsIgnoreCase("customer")){
			sidx = "Customer";
		}else if(sidx.equalsIgnoreCase("reference")){
			sidx = "cr.Reference";
		}else if(sidx.equalsIgnoreCase("memo")){
			sidx = "cr.Memo";
		}else if(sidx.equalsIgnoreCase("paymentApplied")){
			sidx = "unApplied";
		}else{
			sidx = "cr.ReceiptDate";
		}
		

		String condition = "where reversePaymentStatus <> 1 ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		String aPaymentsselectQry = "SELECT cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, CONCAT(rm.Name, ' ', rm.FirstName) AS Customer,cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,SUM(cdp.PaymentApplied) AS PaymentRecieved,(cr.Amount - SUM(IFNULL(cdp.PaymentApplied,0))) AS unApplied"
				+ " FROM rxMaster rm RIGHT JOIN cuReceipt cr ON (rm.rxMasterID = cr.rxCustomerID) LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID) "
				+ condition
				+ "GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID HAVING unApplied>0 ORDER BY "+sidx +" "+sord.toUpperCase()+"  LIMIT "
			//	+ "GROUP BY cr.rxCustomerID ORDER BY cr.ReceiptDate DESC LIMIT "
				+ theFrom + ", " + theRows + ";";
		Session aSession = null;Query aQuery =null;
		
		System.out.println("showUnappliedAmount::"+aPaymentsselectQry);
		ArrayList<CustomerPaymentBean> aCuPaymentsList = new ArrayList<CustomerPaymentBean>();
		try {
			CustomerPaymentBean aCuPayment = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuReceiptID((Integer) aObj[0]);
				aCuPayment.setRxMasterID((Integer) aObj[1]);
				if (aObj[2] != null) {
					aCuPayment.setReceiptDate((String) DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy"));
				}
				aCuPayment.setCustomer((String) aObj[3]);
				aCuPayment.setReference((String) aObj[4]);
				aCuPayment.setMemo((String) (aObj[5]==null?"":aObj[5]));
				aCuPayment.setAmount((BigDecimal) aObj[6]);
				aCuPayment.setCuReceiptTypeID((Short) aObj[7]);
				aCuPayment.setPaymentApplied((BigDecimal) aObj[8]);
				BigDecimal amount = new BigDecimal(0);
				if (aObj[6] != null) {
					amount = (BigDecimal) aObj[6];
				}
				BigDecimal paymentApplied = new BigDecimal(0);
				if (aObj[9] != null) {
					paymentApplied = (BigDecimal) aObj[9];
				}
				aCuPayment.setPaymentApplied(paymentApplied);
				aCuPaymentsList.add(aCuPayment);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry=null;aQuery =null;
		}
		return aCuPaymentsList;
	}

	@Override
	public List<CustomerPaymentBean> getCustomerInvoices(Integer cuMasterID,
			Integer cuRecieptID, String oper,int from ,int to,String column,String sorder) throws CustomerException {
		StringBuffer ap = new StringBuffer(
				"SELECT I.cuInvoiceID, I.InvoiceNumber, I.CustomerPONumber,");
		ap.append("I.InvoiceDate,");
		ap.append("CASE WHEN L.PaymentApplied IS NULL THEN");
		ap.append(" InvoiceAmount - I.AppliedAmount");
		ap.append(" ELSE");
		//ap.append(" InvoiceAmount - (I.AppliedAmount + L.PaymentApplied) END AS InvoiceBalance, ");
		ap.append(" (InvoiceAmount+L.PaymentApplied) - L.PaymentApplied END AS InvoiceBalance, ");
		ap.append(" (InvoiceAmount*(term.DiscountPercent/100)) AS DiscountUsed ");
		ap.append(", L.PaymentApplied  as PaymentApplied, InvoiceAmount-(AppliedAmount+ L.DiscountUsed) AS Remaining ");
		ap.append(", I.InvoiceAmount,  L.DiscountUsed  AS PrevDiscountUsed ");
		ap.append(", L.PaymentApplied  AS PrevPaymentApplied ");
		ap.append(", L.cuLinkageDetailID ");
		ap.append("FROM cuInvoice AS I ");
		if (oper.equalsIgnoreCase("due")) {
			ap.append("LEFT JOIN cuLinkageDetail AS L ");
		/*	ap.append("ON I.cuInvoiceID = L.cuInvoiceID ");
			ap.append("AND L.cuReceiptID=" + cuRecieptID);
			ap.append(" JOIN cuTerms AS term ON I.cuTermsID = term.cuTermsId"); // edited
			ap.append(" WHERE (((I.InvoiceAmount)<>AppliedAmount) ");
			ap.append("AND ((I.TransactionStatus)>=0) AND ((I.rxCustomerID)="
					+ cuMasterID + ")) ");
			ap.append("OR ((Not (L.cuInvoiceID) Is Null) and I.rxCustomerID ="
					+ cuMasterID + " and (L.DiscountUsed+L.PaymentApplied)IS NULL OR (L.DiscountUsed+L.PaymentApplied)=0) Order By I.InvoiceNumber;");*/
		} else {
			ap.append("JOIN cuLinkageDetail AS L ");
			
		}
		
		ap.append("ON I.cuInvoiceID = L.cuInvoiceID ");
		ap.append("AND L.cuReceiptID=" + cuRecieptID);
		ap.append(" JOIN cuTerms AS term ON I.cuTermsID = term.cuTermsId"); // edited
		ap.append(" WHERE (((I.InvoiceAmount)<>AppliedAmount) ");
		ap.append("AND ((I.TransactionStatus)>=0) AND ((I.rxCustomerID)="
				+ cuMasterID + ")) ");
		ap.append("OR ((Not (L.cuInvoiceID) Is Null) and I.rxCustomerID ="
				+ cuMasterID + ") Order By I.InvoiceNumber;");// edited

	//	String aPaymentGridQuery = ap.toString();
		
		String aPaymentGridQuery ="";
		String sortByOption = "";
		
		if(column.equalsIgnoreCase("invoiceNumber")){
			sortByOption=" tmp1.InvoiceNumber ";
		}else if(column.equalsIgnoreCase("customerPoNumber")){
			sortByOption=" tmp1.CustomerPONumber ";
		}else if(column.equalsIgnoreCase("receiptDate")){
			sortByOption=" tmp1.InvoiceDate ";
		}else if(column.equalsIgnoreCase("invoiceBalance")){
			sortByOption=" tmp1.InvoiceBalance ";
		}else if(column.equalsIgnoreCase("preDiscountUsed")){
			sortByOption=" tmp1.DiscountUsed ";
		}else if(column.equalsIgnoreCase("paymentApplied")){
			sortByOption=" tmp1.PaymentApplied ";
		}else if(column.equalsIgnoreCase("amount")){
			sortByOption=" tmp1.amtApplied ";
		}else if(column.equalsIgnoreCase("remaining")){
			sortByOption=" tmp1.Remaining ";
		}
		
	//	sortByOption += " " +sorder;
		
		if (oper.equalsIgnoreCase("due")) {
			 aPaymentGridQuery = "CALL sp_cuPaymentList1('due','"+cuRecieptID+"','"+cuMasterID+"','"+from+"','"+to+"','"+sortByOption+"','"+sorder+"')";
		}
		else if(oper.equalsIgnoreCase("paying")) 
		{
			 aPaymentGridQuery = "CALL sp_cuPaymentList1('paying','"+cuRecieptID+"','"+cuMasterID+"','"+from+"','"+to+"','"+sortByOption+"','"+sorder+"')";
		}
		else
		{
			aPaymentGridQuery = "CALL sp_cuPaymentList1('reverse','"+cuRecieptID+"','"+cuMasterID+"','"+from+"','"+to+"','"+sortByOption+"','"+sorder+"')";
		}
		
		/*
		 * "SELECT I.cuInvoiceID, I.InvoiceNumber, I.CustomerPONumber, I.InvoiceDate, InvoiceAmount-AppliedAmount AS InvoiceBalance, L.DiscountUsed  AS DiscountUsed "
		 * +
		 * ", L.PaymentApplied  as PaymentApplied, InvoiceAmount-AppliedAmount AS Remaining "
		 * + ", I.InvoiceAmount,  L.DiscountUsed  AS PrevDiscountUsed "+
		 * ", L.PaymentApplied  AS PrevPaymentApplied "+
		 * ", L.cuLinkageDetailID "+ "FROM cuInvoice AS I " +
		 * "LEFT JOIN cuLinkageDetail AS L "+
		 * "ON I.cuInvoiceID = L.cuInvoiceID "+
		 * "AND L.cuReceiptID="+cuRecieptID+
		 * " WHERE (((I.InvoiceAmount)<>AppliedAmount) "+
		 * "AND ((I.TransactionStatus)>=0) AND ((I.rxCustomerID)="
		 * +cuMasterID+")) "+
		 * "OR ((Not (L.cuInvoiceID) Is Null) and I.rxCustomerID ="
		 * +cuMasterID+");";
		 */
		Session aSession = null;Query aQuery = null;
		ArrayList<CustomerPaymentBean> aQueryList = new ArrayList<CustomerPaymentBean>();
		try {
			itsLogger.info("Customer Payment Grid Data ---->"
					+ aPaymentGridQuery);
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentGridQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				CustomerPaymentBean aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuInvoiceID((Integer) aObj[0]);
				aCuPayment.setInvoiceNumber((String) aObj[1]);
				aCuPayment.setCustomerPoNumber((String) aObj[2]);
				aCuPayment.setReceiptDate((String) DateFormatUtils.format(
						(Date) aObj[3], "MM/dd/yyyy"));
				
				
				aCuPayment.setInvoiceBalance((BigDecimal) aObj[4]);
				aCuPayment.setDiscountUsed((BigDecimal) aObj[5]);
				aCuPayment.setPaymentApplied((BigDecimal) aObj[6]);
				aCuPayment.setRemaining((BigDecimal) aObj[7]);
				aCuPayment.setAmount((BigDecimal) aObj[8]);
				aCuPayment.setPreDiscountUsed((BigDecimal) aObj[9]);
				aCuPayment.setPrePaymentApplied((BigDecimal) aObj[10]);
				if(aObj[12]!=null)
				aCuPayment.setDatePaid((String) DateFormatUtils.format((Date) aObj[12], "MM/dd/yyyy"));
				
				if(aObj[11]!=null)
				aCuPayment.setCuLinkagedetailId((Integer) aObj[11]);
				aQueryList.add(aCuPayment);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			ap =null;aQuery = null;
		}
		return aQueryList;
	}


	@Override
	public CuLinkageDetail getCuRecieptData(Integer cuMasterID,
			Integer recieptID,Boolean reverseStatus) throws CustomerException {
		
		String preLoadDataQuery ="";
		
		if(reverseStatus==null)
			reverseStatus=false;
		
		if(!reverseStatus)
		{
		preLoadDataQuery = "SELECT DiscountUsed,creditusage, InvoiceApplied, InvoiceAmount FROM (SELECT sum(DiscountUsed) AS DiscountUsed,rxCustomerID FROM cuLinkageDetail  WHERE cuReceiptID = "+recieptID+" and deletedStatus<>1 GROUP BY rxCustomerID)cd "
				+ "LEFT JOIN (SELECT sum(paymentApplied) AS creditusage,rxCustomerID FROM cuLinkageDetail WHERE PaymentApplied<0 and deletedStatus<>1 AND cuReceiptID = "
				+ recieptID
				+ " GROUP BY rxCustomerID ) cdn ON cd.rxCustomerID = cdn.rxCustomerID "
				+ "LEFT JOIN (SELECT sum(paymentApplied) AS InvoiceApplied,rxCustomerID FROM cuLinkageDetail WHERE PaymentApplied>0 AND deletedStatus<>1 and cuReceiptID = "
				+ recieptID
				+ "  GROUP BY rxCustomerID ) cdp ON cd.rxCustomerID = cdp.rxCustomerID "
				+ " LEFT JOIN (SELECT SUM(ci.InvoiceAmount)AS InvoiceAmount,ci.rxCustomerID FROM cuInvoice ci,cuLinkageDetail cl WHERE ci.cuInvoiceID = cl.cuInvoiceID  AND cl.cuReceiptID =  "+recieptID+" ) AS inv "
				+ " ON cd.rxCustomerID = inv.rxCustomerID "
				+ "WHERE cd.rxCustomerID =" + cuMasterID + ";";
		}
		else
		{
		preLoadDataQuery="SELECT SUM(DiscountUsed) AS DiscountUsed,SUM(CASE WHEN paymentApplied<0 THEN paymentApplied ELSE 0 END) AS creditusage,"
				+ "SUM(CASE WHEN paymentApplied>=0 THEN paymentApplied ELSE 0 END) AS InvoiceApplied,SUM(ci.InvoiceAmount)AS InvoiceAmount "
				+ "FROM cuReversePaymentsInv cl, cuInvoice ci  WHERE  ci.cuInvoiceID = cl.cuInvoiceID  AND cl.cuReceiptID = "+recieptID;
		}
		
		itsLogger.info("PreLoad Data: "+preLoadDataQuery);
		Session aSession = null;Query aQuery =null;
		CuLinkageDetail aCulinkagedetail = new CuLinkageDetail();
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(preLoadDataQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aCulinkagedetail.setDiscountUsed((BigDecimal) aObj[0]);
				aCulinkagedetail.setCreditUsage((BigDecimal) aObj[1]);
				aCulinkagedetail.setInvoiceApplied((BigDecimal) aObj[2]);
				aCulinkagedetail.setInvoiceAmount((BigDecimal) aObj[3]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			preLoadDataQuery = null;aQuery =null;
		}
		return aCulinkagedetail;
	}

	@Override
	public String[] getJobDetails(String invoiceNumber)
			throws CustomerException {
		Session aSession = null;
		String jobDeailsQuery = "SELECT jm.JobNumber, jm.Description,jm.joMasterID FROM joMaster jm, joReleaseDetail  jr,cuInvoice cinv"
				+ " WHERE jm.joMasterID = jr.joMasterID AND jr.joReleaseDetailID = cinv.joReleaseDetailID AND cinv.InvoiceNumber= '"
				+ invoiceNumber + "';";
		String[] jobDetails = new String[2];Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(jobDeailsQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				jobDetails[0] = (String) aObj[0];
				jobDetails[1] = (String) aObj[1];
				Integer joMasterID=0;
				if(aObj[2]==null){
					joMasterID=(Integer) aObj[2];
				}
				jobDetails[2] = String.valueOf(joMasterID);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			jobDeailsQuery =null;aQuery =null;
		}
		return jobDetails;

	}

	/*
	 * @Override public List<?> getCustomerInvoices(Integer cuMasterID) throws
	 * CustomerException { SELECT jm.JobNumber, jm.Description FROM joMaster jm,
	 * joReleaseDetail jr,cuInvoice cinv WHERE jm.joMasterID = jr.joMasterID AND
	 * jr.joReleaseDetailID = cinv.joReleaseDetailID AND
	 * cinv.joReleaseDetailID=288; String aPaymentsselectQry =
	 * "SELECT cuReceipt.cuReceiptID, "+ "cuReceipt.ReceiptDate, "+
	 * "cuReceipt.rxCustomerID, "+ "cuReceipt.Reference, "+ "cuReceipt.Memo, "+
	 * "cuReceipt.Amount, "+ "cuReceipt.rxCustomerID, "+
	 * "rxContact.FirstName, "+ "cuInvoice.CustomerPONumber "+
	 * "from cuReceipt join rxContact on rxContact.rxContactID = cuReceipt.rxCustomerID "
	 * + "join cuInvoice on cuInvoice.cuInvoiceID = cuReceipt.cuReceiptID "+
	 * "where cuReceipt.rxCustomerID ="+ cuMasterID+";";
	 * 
	 * Session aSession = null; ArrayList<CustomerPaymentBean> aQueryList = new
	 * ArrayList<CustomerPaymentBean>(); try { aSession =
	 * itsSessionFactory.openSession(); Query aQuery =
	 * aSession.createSQLQuery(aPaymentsselectQry); Iterator<?> aIterator =
	 * aQuery.list().iterator(); while (aIterator.hasNext()) {
	 * CustomerPaymentBean aCuPayment = new CustomerPaymentBean(); Object[] aObj
	 * = (Object[]) aIterator.next();
	 * aCuPayment.setCuReceiptID((Integer)aObj[0]);
	 * aCuPayment.setReceiptDate((String)DateFormatUtils.format((Date)aObj[1],
	 * "MM/dd/yyyy")); aCuPayment.setRxMasterID((Integer)aObj[3]);
	 * aCuPayment.setMemo((String)aObj[4]); aCuPayment.setAmount((BigDecimal)
	 * aObj[5]); aCuPayment.setCustomer((String)aObj[7]);
	 * aCuPayment.setCustomerPoNumber((String)aObj[8]);
	 * aQueryList.add(aCuPayment); } } catch(Exception e) {
	 * itsLogger.error(e.getMessage(),e); CustomerException aCustomerException =
	 * new CustomerException(e.getMessage(), e); throw aCustomerException; }
	 * finally { aSession.flush(); aSession.close(); } return aQueryList; }
	 */
	@Override
	public Integer insertDivisions(Codivision theCodivision)
			throws CustomerException {
		Session aSession = null;
		Transaction aTransaction = null;
		Codivision aCodivision = new Codivision();
		int divisionId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			divisionId = (Integer) aSession.save(aCodivision);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return divisionId;
	}
	

	@Override
	public void updateCompleteInvoice(CuLinkageDetail theCulinkagedetail)
			throws CustomerException {
		itsLogger.info("CompleteInvoicePosting");
		Session aCuLinkageSession = null;
		Transaction aTransaction = null;
		Session aPaymentGlPostSession= null;
		CuLinkageDetail aLinkagedetail = null;
		Cuinvoice aCuinvoice = null;
		BigDecimal prevAppliedAmount = new BigDecimal(0);
		try {
			aPaymentGlPostSession = itsSessionFactory.openSession();
			aCuLinkageSession = itsSessionFactory.openSession();
			aTransaction = aCuLinkageSession.beginTransaction();
			aTransaction.begin();
			if (theCulinkagedetail.getCuLinkageDetailId() == null) {
				itsLogger.info("cuLinkageDetailEmpty");
				aCuLinkageSession.save(theCulinkagedetail);
			} else {
				itsLogger.info("Having - cuLinkageDetail");
					aLinkagedetail = (CuLinkageDetail) aCuLinkageSession.get(CuLinkageDetail.class,theCulinkagedetail.getCuLinkageDetailId());
					prevAppliedAmount = aLinkagedetail.getPaymentApplied();
				if (theCulinkagedetail.getPaymentApplied().equals(BigDecimal.ZERO)) {
					itsLogger.info("Having - Applied Amount 0");
					aCuLinkageSession.delete(aLinkagedetail);
				} else {
					itsLogger.info("Having - Applied Else");
					aLinkagedetail.setDiscountUsed(theCulinkagedetail.getDiscountUsed());
					aLinkagedetail.setPaymentApplied(theCulinkagedetail.getPaymentApplied());
					aCuLinkageSession.update(aLinkagedetail);
					
				}
			}
			aTransaction.commit();
			itsLogger.info("Having - getCuInvoiceId"+theCulinkagedetail.getCuInvoiceId());
			aTransaction = aCuLinkageSession.beginTransaction();
			aCuinvoice = (Cuinvoice) aCuLinkageSession.get(Cuinvoice.class,theCulinkagedetail.getCuInvoiceId());
			BigDecimal appliedNew = (aCuinvoice.getAppliedAmount().subtract(prevAppliedAmount)).add(theCulinkagedetail.getPaymentApplied());
			itsLogger.info("Applied Amount: "+theCulinkagedetail.getPaymentApplied());
			aCuinvoice.setAppliedAmount(theCulinkagedetail.getPaymentApplied());
			aCuLinkageSession.update(aCuinvoice);
			aTransaction.commit();
			if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()!=0 ){
				
				
				JoReleaseDetail joReleaseDetail = (JoReleaseDetail ) aCuLinkageSession.get(JoReleaseDetail.class,
						aCuinvoice.getJoReleaseDetailId());
				joReleaseDetail.setPaymentDate(new Date());
				
				/* Added by : Praveenkumar
				 * Date : 2014-09-04
				 * Adding column for updating balance due in joRelease table
				 * 
				 */
				
				joReleaseDetail.setBalanceDue(appliedNew.subtract(aCuinvoice.getInvoiceAmount()));
				
				aTransaction = aCuLinkageSession.beginTransaction();
				aTransaction.begin();
				aCuLinkageSession.update(joReleaseDetail);
				
			}
			
			aTransaction.commit();
			itsLogger.info("Posting in glTransaction !");
			//itsGltransactionService.postCusInvoiceDetails(aCuinvoice);
			aTransaction = aPaymentGlPostSession.beginTransaction();
			CuPaymentGlpost aCuPaymentGlpost = new CuPaymentGlpost();
			aCuPaymentGlpost.setCuLinkageDetailID(theCulinkagedetail.getCuLinkageDetailId());
			aCuPaymentGlpost.setGroupID(1);
			itsLogger.info("cuLinkageDetailEmpty");
			aPaymentGlPostSession.save(aCuPaymentGlpost);
			aTransaction.commit();	
					
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aCuLinkageSession.flush();
			aCuLinkageSession.close();
			
			aPaymentGlPostSession.flush();
			aPaymentGlPostSession.close();
		}
	}

	
	@Override
	public Integer updateInvoice(CuLinkageDetail theCulinkagedetail,Integer linkageID, Integer invoiceID, BigDecimal opAmount,BigDecimal amtApplied,String oper)
			throws CustomerException {
		Session aSession= null;
		Transaction aTransaction = null;
		CuLinkageDetail aLinkagedetail = null;
		BigDecimal prevAppliedAmount = new BigDecimal(0);
		boolean  statusValue = false;
		Integer cuLinkID=0;
			try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			
			if (theCulinkagedetail.getCuLinkageDetailId() == null || theCulinkagedetail.getCuLinkageDetailId()==0) {
				itsLogger.info("cuLinkageDetailEmpty");
				aTransaction.begin();
				theCulinkagedetail.setPaymentApplied(theCulinkagedetail.getPaymentApplied().add(opAmount));
				theCulinkagedetail.setRefInvoiceID(invoiceID);
				theCulinkagedetail.setCreditAmount(opAmount);
				theCulinkagedetail.setAmtApplied(theCulinkagedetail.getAmtApplied());
				theCulinkagedetail.setDatePaid(new Date());
				cuLinkID= (Integer)aSession.save(theCulinkagedetail);
				aTransaction.commit();	
				
				aTransaction.begin();
				CuPaymentGlpost aCuPaymentGlpost = new CuPaymentGlpost();
				aCuPaymentGlpost.setCuLinkageDetailID(theCulinkagedetail.getCuLinkageDetailId());
				aCuPaymentGlpost.setGroupID(1);
				aCuPaymentGlpost.setRxCustomerID(theCulinkagedetail.getRxCustomerId());
				aCuPaymentGlpost.setCuInvoiceID(theCulinkagedetail.getCuInvoiceId());
				itsLogger.info("cuLinkageDetailEmpty");
				aSession.save(aCuPaymentGlpost);
				aTransaction.commit();	
				
				//updateCuInvoicefromPayments( theCulinkagedetail, amtApplied, invoiceID, opAmount,false); 
				
				}
			else 
			{
					itsLogger.info("Having - cuLinkageDetail" +theCulinkagedetail.getCuLinkageDetailId());
					aLinkagedetail = (CuLinkageDetail) aSession.get(CuLinkageDetail.class,theCulinkagedetail.getCuLinkageDetailId());
					prevAppliedAmount = aLinkagedetail.getPaymentApplied();
					if (theCulinkagedetail.getPaymentApplied().equals(BigDecimal.ZERO)) {
						itsLogger.info("Having - Applied Amount 0");
						aTransaction.begin();
						if(oper.equals("dueradio"))
						{
						aSession.createSQLQuery("delete from cuLinkageDetail where cuLinkageDetailID = "+theCulinkagedetail.getCuLinkageDetailId()).executeUpdate();
						aSession.createSQLQuery("delete from cuPaymentGlpost where cuLinkageDetailID = "+theCulinkagedetail.getCuLinkageDetailId()).executeUpdate();
						}
						else
						{
						aSession.createSQLQuery("update cuLinkageDetail set deletedStatus = 1 where cuLinkageDetailID = "+theCulinkagedetail.getCuLinkageDetailId()).executeUpdate();
						}
						aTransaction.commit();
						statusValue = true;
				    } else {
						itsLogger.info("Discount Applied");
						aTransaction.begin();
						aLinkagedetail.setDiscountUsed(theCulinkagedetail.getDiscountUsed());
						aLinkagedetail.setAmtApplied(theCulinkagedetail.getAmtApplied());
						aLinkagedetail.setPaymentApplied(theCulinkagedetail.getPaymentApplied());
						aLinkagedetail.setStatusCheck(theCulinkagedetail.getStatusCheck());
						aLinkagedetail.setDatePaid(new Date());
						aSession.update(aLinkagedetail);
						aTransaction.commit();
						statusValue = true;
				    }
					if(linkageID>0){
						aTransaction.begin();
						aLinkagedetail = (CuLinkageDetail) aSession.get(CuLinkageDetail.class,linkageID);
						aLinkagedetail.setRefInvoiceID(invoiceID);
						aLinkagedetail.setCreditAmount(opAmount);
						aSession.update(aLinkagedetail);
						aTransaction.commit();
					}
					cuLinkID = linkageID;
					
				/*	if(statusValue)
					updateCuInvoicefromPayments( theCulinkagedetail, amtApplied, invoiceID,  opAmount,statusValue); */
					
				}
				
					
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close(); 
			
		}
		return cuLinkID;
	}
		
	
	public Boolean updateCuInvoicefromPayments(CuLinkageDetail theCulinkagedetail,BigDecimal amtApplied,Integer invoiceID, BigDecimal opAmount,boolean statusValue) throws CustomerException
	{
		
		Session aSession= null;
		Transaction aTransaction = null;
		
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
						
					itsLogger.info("Having - getCuInvoiceId"+theCulinkagedetail.getCuInvoiceId());
					Cuinvoice aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
							theCulinkagedetail.getCuInvoiceId());
					BigDecimal appliedNew = new BigDecimal(0.0);
					
					
					if(theCulinkagedetail.getPaymentApplied().compareTo(new BigDecimal(0.00))==1)
					{
						appliedNew = (aCuinvoice.getAppliedAmount().add(amtApplied));
					}
					else if(theCulinkagedetail.getPaymentApplied().compareTo(new BigDecimal(0.00))==-1)
					{
						appliedNew = (aCuinvoice.getAppliedAmount().add(amtApplied));
					}
					else
					{
						appliedNew = (aCuinvoice.getAppliedAmount().subtract(amtApplied.subtract(theCulinkagedetail.getDiscountUsed())));
					}
					
					if(statusValue)
					{
						aCuinvoice.setDiscountApplied(theCulinkagedetail.getDiscountUsed().subtract(theCulinkagedetail.getDiscountUsed()));
						aCuinvoice.setDiscountAmt((aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).subtract(theCulinkagedetail.getDiscountUsed()));
					}
					else if(theCulinkagedetail.getDiscountUsed().compareTo(new BigDecimal(0.00))==1)
					{
						aCuinvoice.setDiscountApplied((aCuinvoice.getDiscountApplied()==null?BigDecimal.ZERO:aCuinvoice.getDiscountApplied()).add(theCulinkagedetail.getDiscountUsed()));
						aCuinvoice.setDiscountAmt((aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).add(theCulinkagedetail.getDiscountUsed()));
					}
					else if(theCulinkagedetail.getDiscountUsed().compareTo(new BigDecimal(0.00))==-1)
					{
						aCuinvoice.setDiscountApplied((aCuinvoice.getDiscountApplied()==null?BigDecimal.ZERO:aCuinvoice.getDiscountApplied()).add(theCulinkagedetail.getDiscountUsed()));
						aCuinvoice.setDiscountAmt((aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).add(theCulinkagedetail.getDiscountUsed()));
					}
				
					
					aCuinvoice.setAppliedAmount(appliedNew);
					
					if(aCuinvoice.getCuInvoiceId()==invoiceID)
					{
						aCuinvoice.setAppliedAmount(aCuinvoice.getAppliedAmount().subtract(opAmount));
					}
					aTransaction.begin();
					aSession.update(aCuinvoice);
					aTransaction.commit();
					if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()!=0 ){
					
					JoReleaseDetail joReleaseDetail = (JoReleaseDetail ) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
					Date curDate = new Date();
					itsLogger.info("Null Pointer Error Date: "+curDate);
					joReleaseDetail.setPaymentDate(curDate);
					
					
					/* Added by : Praveenkumar
					 * Date : 2014-09-04
					 * Adding column for updating balance due in joRelease table
					 */
					aTransaction.begin();
					joReleaseDetail.setBalanceDue(appliedNew.subtract(aCuinvoice.getInvoiceAmount()));
					aSession.update(joReleaseDetail);
					aTransaction.commit();
					}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close(); 
			
		}
		
		return true;
	}
	
	
	
	
	
	/*public void postGlst(Cureceipt cuReceiptObj,Integer yearID,Integer periodID,String userName) throws CustomerException {
		
		Session aCuInvoiceSession = null;
		Cureceipt cuReceipt = null;
		int status =0;
		int count = 0;String aPaymentsselectQry =null;String hql =null;String aPaymentsCountStr = null;
		try {
			aCuInvoiceSession = itsSessionFactory.openSession();
			cuReceipt = new Cureceipt();				
			cuReceipt = (Cureceipt) aCuInvoiceSession.get(Cureceipt.class,cuReceiptObj.getCuReceiptId());
			
			if(cuReceipt.getAmount().compareTo(cuReceiptObj.getAmount())!=0)
				count++;
			if(cuReceipt.getReference()!=null && !cuReceipt.getReference().equals(cuReceiptObj.getReference()))
				count++;

			if(count>0)
			{
			cuReceiptObj.setRxCustomerId(cuReceipt.getRxCustomerId());
			Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CP");
			itsGltransactionService.rollBackGlTransaction(cuReceiptObj.getCuReceiptId(),aColedgersource);	
			//itsGltransactionService.postCusInvoiceDetails(cuReceiptObj,yearID,periodID,userName);
			}
			
			
			cuReceipt.setAmount(cuReceiptObj.getAmount());
			cuReceipt.setMemo(cuReceiptObj.getMemo());
			cuReceipt.setReceiptDate(cuReceiptObj.getReceiptDate());
			cuReceipt.setReference(cuReceiptObj.getReference());
			cuReceipt.setCuReceiptTypeId(cuReceiptObj.getCuReceiptTypeId());
			aCuInvoiceSession.update(cuReceipt);	
		
			aPaymentsselectQry = "SELECT cuInvoice.InvoiceDate, "
					+ "cuInvoice.cuInvoiceID, "
					+ "cuInvoice.InvoiceNumber, "
					+ "cuInvoice.rxCustomerID, "
					+ "SUM(cuInvoice.InvoiceAmount) AS cuInvoiceAmt,"
					+ "SUM(cuInvoice.AppliedAmount) AS invoiceAppliedAmt, "
					+ "SUM(cuLinkageDetail.PaymentApplied-cuLinkageDetail.creditAmount) AS linkApplied, "
					+ "cuLinkageDetail.DiscountUsed "
					+ "FROM cuInvoice ,cuLinkageDetail WHERE cuInvoice.cuinvoiceID = cuLinkageDetail.cuInvoiceID AND "
					+ "cuLinkageDetailID IN (SELECT cuLinkageDetailID FROM cuPaymentGlpost WHERE postStatus=0)";
			ArrayList<Cuinvoice> aCuPaymentsList = new ArrayList<Cuinvoice>();
				Cuinvoice aCuPayment = null;
				
				Query aQuery = aCuInvoiceSession.createSQLQuery(aPaymentsselectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				if(aIterator!=null){
				while (aIterator.hasNext()) {
					aCuPayment = new Cuinvoice();
					Object[] aObj = (Object[]) aIterator.next();
					
					if (aObj[0] != null) {
						aCuPayment.setInvoiceDate((Date) aObj[0]);
					}
					aCuPayment.setCuInvoiceId((Integer) aObj[1]);
					aCuPayment.setInvoiceNumber((String) aObj[2]);
					aCuPayment.setRxCustomerId((Integer) aObj[3]);
					aCuPayment.setInvoiceAmount((BigDecimal) aObj[5]);
					aCuPayment.setAppliedAmount((BigDecimal) aObj[6]);
					aCuPayment.setDiscountApplied((BigDecimal) aObj[7]);
					aCuPaymentsList.add(aCuPayment);
				}
				if(aCuPaymentsList.size()>0){
						Cuinvoice theCuinvoice = new Cuinvoice();
						theCuinvoice = aCuPaymentsList.get(0);
						status = itsGltransactionService.postCusInvoiceDetails(theCuinvoice,cuReceipt,yearID,periodID,userName);
				}
				if(status>0){
					
						aPaymentsCountStr = "SELECT MAX(groupID)+1 AS count FROM cuPaymentGlpost";
						BigInteger maxval = new BigInteger("0");
						try {
							// Retrieve aSession from Hibernate
							Query aGroupQuery = aCuInvoiceSession.createSQLQuery(aPaymentsCountStr);
							List<?> aList = aGroupQuery.list();
							maxval =  (BigInteger) aList.get(0);
						} catch (Exception e) {
							e.printStackTrace();
					     }
					hql = "UPDATE CuPaymentGlpost set postStatus = 1, groupID = "+maxval+" WHERE postStatus = 0";
					Query query = aCuInvoiceSession.createQuery(hql);
					int result = query.executeUpdate();
				}
				}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {

			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
			aPaymentsselectQry =null;
			hql =null;aPaymentsCountStr = null;
		}
		
	}*/

	
	@SuppressWarnings("unchecked")
	@Override
	public void postGl(Cureceipt cuReceiptObj,Integer yearID,Integer periodID,UserBean aUserBean,String paidStatus) throws CustomerException,BankingException {
		
		Session aCuInvoiceSession = null;
		Transaction aTransaction = null;
		Cureceipt cuReceipt = null;
		int status =0;
		int count = 0;String aPaymentsselectQry =null;String hql =null;String aPaymentsCountStr = null;
		try {
			aCuInvoiceSession = itsSessionFactory.openSession();
			aTransaction = aCuInvoiceSession.beginTransaction();
			aTransaction.begin();
			
			cuReceipt = new Cureceipt();		
			
			//Update cuReceipt Details
			cuReceipt = (Cureceipt) aCuInvoiceSession.get(Cureceipt.class,cuReceiptObj.getCuReceiptId());
			cuReceipt.setAmount(cuReceiptObj.getAmount());
			cuReceipt.setMemo(cuReceiptObj.getMemo());
			cuReceipt.setReceiptDate(cuReceiptObj.getReceiptDate());
			cuReceipt.setReference(cuReceiptObj.getReference());
			cuReceipt.setCuReceiptTypeId(cuReceiptObj.getCuReceiptTypeId());
			aCuInvoiceSession.update(cuReceipt);	
			
			
			if(paidStatus.equals("paid"))
			{
				// get coLedgerSourceID
				Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CP");
				
				// get groupIds for uncheck paid invoices
				List<Integer> groupIDs = (List<Integer>)aCuInvoiceSession.createSQLQuery("SELECT groupID FROM cuPaymentGlpost WHERE cuInvoiceID IN (SELECT cuInvoiceID FROM cuLinkageDetail WHERE deletedStatus=1 and cuReceiptID = "+cuReceiptObj.getCuReceiptId()+") GROUP BY groupID").list();
				
				// Update cuInvoice for deleted records.
				updateCuInvoiceforunsuccessfulpayment(cuReceiptObj.getCuReceiptId(),aCuInvoiceSession,aUserBean,yearID,periodID);	
				
				// roll back particular groups of discounted invoices.
				/*for(Integer gpid : groupIDs)
				itsGltransactionService.rollBackGlforCuPayment(cuReceiptObj.getCuReceiptId(),gpid,aColedgersource,aCuInvoiceSession);*/
			}
			
		
			updateCuInvoiceforsuccessfulpayment(cuReceiptObj.getCuReceiptId(),aCuInvoiceSession,aUserBean, yearID, periodID);	
			// Delete UnChecked invoices.
			aCuInvoiceSession.createSQLQuery("DELETE cul,cgl FROM cuLinkageDetail cul,cuPaymentGlpost cgl WHERE cul.cuLinkageDetailID = cgl.cuLinkageDetailID AND cul.deletedStatus = 1 AND cul.cuReceiptID = "+cuReceiptObj.getCuReceiptId()).executeUpdate();
		
			// Select sum of payment applied amounts
			aPaymentsselectQry = "SELECT cuL.cuReceiptID,cuL.rxCustomerID,SUM(cuL.amtApplied) AS InvoiceAmount,SUM(cuL.PaymentApplied - cuL.creditAmount) AS linkApplied,"
					+ "SUM(cuL.DiscountUsed) AS Discount FROM cuLinkageDetail cuL ,cuPaymentGlpost cgl WHERE cgl.postStatus = 0 AND cgl.cuLinkageDetailID = cuL.cuLinkageDetailID AND cuL.cuReceiptID ="+cuReceiptObj.getCuReceiptId();
			 
				Cuinvoice aCuPayment = null;
				Query aQuery = aCuInvoiceSession.createSQLQuery(aPaymentsselectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				BigDecimal unappliedAmt = BigDecimal.ZERO;
			
				if(aIterator!=null){
				while (aIterator.hasNext()) {
					aCuPayment = new Cuinvoice();
					Object[] aObj = (Object[]) aIterator.next();
					
					aCuPayment.setCuInvoiceId((Integer) aObj[0]);
					aCuPayment.setRxCustomerId((Integer) aObj[1]);
					aCuPayment.setInvoiceAmount((BigDecimal) aObj[2]);
					aCuPayment.setAppliedAmount((BigDecimal) aObj[3]);
					aCuPayment.setDiscountApplied((BigDecimal) aObj[4]);
				}
				
				
			
				// Insert into glTransaction
				
				//if(aCuPayment.getCuInvoiceId()!=null && unappliedAmt.compareTo(BigDecimal.ZERO)==0 ){ // apply gl when payment fully applied (i.e)no unappliedamt
				/*
				if(aCuPayment.getCuInvoiceId()!=null ){ // apply gl for each payment
					status = itsGltransactionService.postCusInvoiceDetails(aCuPayment,cuReceipt,yearID,periodID,userName,aCuInvoiceSession);
				}

				if(status>0){*/
					
				itsGltransactionService.postCusInvoiceDetails(aCuPayment,cuReceipt,yearID,periodID,aUserBean,aCuInvoiceSession);
				
						aPaymentsCountStr = "SELECT MAX(groupID)+1 AS count FROM cuPaymentGlpost";
						Session aSession = null;
						BigInteger maxval = new BigInteger("0");
						try {
							// Retrieve aSession from Hibernate
							aSession = itsSessionFactory.openSession();
							Query aGroupQuery = aSession.createSQLQuery(aPaymentsCountStr);
							List<?> aList = aGroupQuery.list();
							maxval =  (BigInteger) aList.get(0);
							} catch (Exception e) {
							e.printStackTrace();
							}
					
						hql = "UPDATE CuPaymentGlpost set postStatus = 1, groupID = "+maxval+" WHERE postStatus = 0";
						Query query = aCuInvoiceSession.createQuery(hql);
						int result = query.executeUpdate();
					//}	
				}
				
				aTransaction.commit();
				
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			aTransaction.rollback();
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {

			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
			aPaymentsselectQry =null;
			hql =null;aPaymentsCountStr = null;
		}
	}
	
	
	/*
	 * @Override public Boolean printSatatements(Integer stRxMasterID, Integer
	 * EndRxMasterID, String startCustName, String endCustName, Date
	 * excludeAfterDate, Boolean showCredit) throws CustomerException{ Boolean
	 * justpreview = false; Boolean actuallyFax = false; Session aSession =
	 * null; Session aSession1 = itsSessionFactory.openSession(); Session
	 * aSession2 = itsSessionFactory.openSession();; List<StatementsBean>
	 * arxMasterList = new ArrayList<StatementsBean>(); String query =null;
	 * Integer balance =0; Boolean printStatement = false; Boolean selectCust =
	 * false; Boolean tempBoolean = false; int i = 0 ; try{
	 * if(stRxMasterID!=null && EndRxMasterID!=null){
	 * query="SELECT Name AS Name,  rxMasterID FROM rxMaster WHERE (IsCustomer<>0)"
	 * + " AND (Name >= '"+ startCustName+"') AND (Name <= '"+ endCustName
	 * +"') " + "ORDER BY Name, FirstName;"; }else{ query =
	 * "SELECT Name AS Name,  rxMasterID FROM rxMaster " +
	 * "WHERE (IsCustomer<>0) ORDER BY Name, FirstName;"; } aSession =
	 * itsSessionFactory.openSession(); Query aQuery =
	 * aSession.createSQLQuery(query); if(aQuery.list().isEmpty()){ return
	 * false; }else{ Iterator<?> aIterator = aQuery.list().iterator();
	 * while(aIterator.hasNext()){ StatementsBean aBean = new StatementsBean();
	 * Object[] aObj = (Object[])aIterator.next();
	 * aBean.setName((String)aObj[0]); aBean.setRxMasterID((Integer)aObj[1]);
	 * arxMasterList.add(aBean); } for(StatementsBean aBean: arxMasterList){
	 * if(aBean.getRxMasterID()==stRxMasterID &&
	 * aBean.getRxMasterID()==EndRxMasterID){ selectCust = true; tempBoolean =
	 * true; }else{ selectCust = false; } if(selectCust){ query =
	 * " SELECT SUM(InvoiceAmount - AppliedAmount) AS Balance " +
	 * "FROM cuInvoice WHERE (rxCustomerID="+aBean.getRxMasterID() +
	 * "AND (TransactionStatus > 0) " +
	 * "AND (InvoiceDate <="+excludeAfterDate+"));"; } else{ query =
	 * "SELECT COUNT(Statements) FROM cuMaster WHERE cuMasterId ="
	 * +aBean.getRxMasterID(); Query balanceQuery =
	 * aSession1.createSQLQuery(query);
	 * if(Integer.parseInt(balanceQuery.uniqueResult().toString())==0){
	 * continue; }else{ query =
	 * "SELECT SUM(cuInvoice.InvoiceAmount - cuInvoice.AppliedAmount) AS Balance "
	 * +
	 * "FROM cuInvoice INNER JOIN cuMaster ON cuInvoice.rxCustomerID = cuMaster.cuMasterID "
	 * + "WHERE (cuInvoice.rxCustomerID= "+aBean.getRxMasterID()+
	 * ") AND (cuInvoice.InvoiceAmount - cuInvoice.AppliedAmount) <> 0 " +
	 * "AND (cuInvoice.InvoiceDate <= '"
	 * +excludeAfterDate+"' AND (Statements = 1));"; } } Query balanceQuery =
	 * aSession2.createSQLQuery(query); Integer QueryBalance = (Integer)
	 * balanceQuery.uniqueResult(); if(QueryBalance==null){ continue; }else{
	 * balance = QueryBalance; } if(!selectCust){ if(balance<0.0){ continue; } }
	 * else{ printStatement = true; } if(showCredit){ if(justpreview){
	 * if(actuallyFax){ emailStatements(aBean.getRxMasterID()); } }
	 * 
	 * }else{ printStatements(aBean.getRxMasterID()); } } } }catch(Exception e)
	 * { itsLogger.error(e.getMessage(),e); CustomerException aCustomerException
	 * = new CustomerException(e.getMessage(), e); throw aCustomerException; }
	 * finally { aSession.flush(); aSession.close(); aSession1.flush();
	 * aSession1.close(); aSession2.flush(); aSession2.close(); } return
	 * justpreview; }
	 */
	@Override
	public List<StatementsBean> printSatatementsConfirmation(
			Integer stRxMasterID, Integer EndRxMasterID, String startCustName,
			String endCustName, Date excludeAfterDate, Boolean showCredit)
			throws CustomerException {
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		List<StatementsBean> arxMasterList = new ArrayList<StatementsBean>();
		List<StatementsBean> arxMasterwithStatements = new ArrayList<StatementsBean>();
		String query = null;Query aQuery =null;
		try {
			if (stRxMasterID != null && EndRxMasterID != null) {
				query = "SELECT Name AS Name,  rm.rxMasterID, rc.IM_Text FROM rxMaster as rm "
						+ "join cuMaster as rc on rm.rxMasterID  = rc.cuMasterID"
						+ " WHERE (rm.IsCustomer<>0)"
						+ " AND (Name >= '"
						+ startCustName
						+ "' ) AND (Name <= '"
						+ endCustName
						+ "' )" + " ORDER BY rm.Name, rm.FirstName;";
			} else {
				query = "SELECT rm.Name AS Name,  rm.rxMasterID, rc.IM_Text"
						+ " FROM rxMaster as rm "
						+ "join cuMaster as rc on rm.rxMasterID  = rc.cuMasterID"
						+ " WHERE (rm.IsCustomer<>0) ORDER BY rm.Name, rm.FirstName ";
			}
			aQuery = aSession.createSQLQuery(query);
			List<?> lst =aQuery.list(); 
			if (lst !=null && lst.size()>0) {
				Iterator<?> aIterator = lst.iterator();
				while (aIterator.hasNext()) {
					StatementsBean aBean = new StatementsBean();
					Object[] aObj = (Object[]) aIterator.next();
					aBean.setName((String) aObj[0]);
					aBean.setRxMasterID((Integer) aObj[1]);
					aBean.setEmail((String) aObj[2]);
					arxMasterList.add(aBean);
				}
				for (StatementsBean aBean : arxMasterList) {
					query = "SELECT I.*,(CASE WHEN J.Description IS NULL THEN I.InvoiceNumber ELSE J.JobNumber END)  AS JobNumber"
							+ ", J.joMasterID, (CASE WHEN J.Description IS NULL THEN"
							+ " I.QuickJobName ELSE J.Description END)  AS JobName"
							+ " FROM joReleaseDetail AS R"
							+ " LEFT JOIN cuInvoice AS I ON R.joReleaseDetailID = I.joReleaseDetailID"
							+ " LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID"
							+ " join rxMaster as rm on rm.rxMasterID = I.rxCustomerID"
							+ " join rxAddress as ra on ra.rxMasterID = I.rxCustomerID"
							+ " WHERE I.rxCustomerID ="
							+ aBean.getRxMasterID()
							/*
							 * +"AND (I.InvoiceAmount - I.AppliedAmount > 0.01)"
							 * +"AND (I.TransactionStatus > 0)"
							 */
							+ " AND (I.InvoiceDate <= '"
							+ excludeAfterDate
							+ " ') ORDER BY I.InvoiceDate ";
					Query statementCountQuery = aSession.createSQLQuery(query);
					if (!statementCountQuery.list().isEmpty())
						arxMasterwithStatements.add(aBean);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			query=null;aQuery =null;
		}
		return arxMasterwithStatements;
	}

	@Override
	public List<Cureceipttype> getRecieptType() throws CustomerException {
		Session aSession = null;
		List<Cureceipttype> recieptTypeList = null;Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			 query = aSession.createQuery("from Cureceipttype");
			recieptTypeList = query.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return recieptTypeList;
	}

	@Override
	public Integer savePayment(Cureceipt theCureceipt,Integer yearID,Integer periodID,UserBean aUserBean,String paidinvoiceDetails,BigDecimal DiscountAmt) throws CustomerException {
		Session aSession = null;
		Integer cuRecieptID=0;
		aSession = itsSessionFactory.openSession();
		Transaction aTransaction = aSession.beginTransaction();
		
		try {
			aTransaction.begin();
			
			if(theCureceipt.getCuReceiptId()!=null &&theCureceipt.getCuReceiptId()!=0)
			{
				postcuLinkageDetail( theCureceipt,yearID,periodID,aUserBean,paidinvoiceDetails,aSession);
			}
			else
			{
				cuRecieptID =(Integer) aSession.save(theCureceipt);
				theCureceipt.setCuReceiptId(cuRecieptID);
				itsGltransactionService.postCustomerPaymentDetails(theCureceipt,DiscountAmt,yearID,periodID,aUserBean.getUserName(),aSession);
				postcuLinkageDetail( theCureceipt,yearID,periodID,aUserBean,paidinvoiceDetails,aSession);
			}
			aTransaction.commit();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			aTransaction.rollback();
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		}
		finally
		{
			aSession.flush();
			aSession.close();
			
		}
		return cuRecieptID;
	}

	/**
	 * Method Name : deletePayment (Used to delete a payment)
	 * 
	 * @param theCureceipt
	 * @throws CustomerException
	 */
	@Override
	@Transactional
	public void deletePayment(Cureceipt theCureceipt) throws CustomerException {
		Session aSession = null;
		List<CuLinkageDetail> linkageDetailList = null;Query aQuery = null;String query =null;
		try {
			aSession = itsSessionFactory.openSession();
			query = "FROM CuLinkageDetail CL WHERE CL.cuReceiptId = :recieptID";
			aQuery = aSession.createQuery(query);
			aQuery.setParameter("recieptID", theCureceipt.getCuReceiptId());
			linkageDetailList = aQuery.list();
			for (CuLinkageDetail aDetail : linkageDetailList) {
				updateInvoice(aDetail.getCuInvoiceId(),
						aDetail.getPaymentApplied());
				deleteCULinkageDetailObj(aDetail.getCuLinkageDetailId());
			}
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			Cureceipt aCureceipt = (Cureceipt) aSession.get(Cureceipt.class,
					theCureceipt.getCuReceiptId());
			aSession.delete(aCureceipt);
			aTransaction.commit();
		} catch (CustomerException e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;aQuery = null;
		}
	}

	/**
	 * Method name : updateInvoice (Used to update the cuinvoice table when a
	 * payment is deleted)
	 * 
	 * @param invoiceId
	 * @param appliedAmount
	 * @throws CustomerException
	 */
	@Transactional
	private void updateInvoice(Integer invoiceId, BigDecimal appliedAmount)
			throws CustomerException {

		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			Cuinvoice aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
					invoiceId);
			aCuinvoice.setAppliedAmount(aCuinvoice.getAppliedAmount().subtract(
					appliedAmount));
			aSession.update(aCuinvoice);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	/**
	 * Method Name : deleteCULinkageDetailObj (Used to delete the cuLinkage
	 * entries associated to a cureciept (payment))
	 * 
	 * @param cuLinkageDetailID
	 * @throws CustomerException
	 */
	@Transactional
	private void deleteCULinkageDetailObj(Integer cuLinkageDetailID)
			throws CustomerException {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			CuLinkageDetail aCuLinkageDetail = (CuLinkageDetail) aSession.get(
					CuLinkageDetail.class, cuLinkageDetailID);
			aSession.delete(aCuLinkageDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void updateCustomerTypes(Integer cuMasterTypeId, String code,
			String description, boolean inActive) {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			CuMasterType aCuMasterType = new CuMasterType(code, description,
					inActive);
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (cuMasterTypeId != null) {
				aCuMasterType = (CuMasterType) aSession.get(CuMasterType.class,
						cuMasterTypeId);
				if (aCuMasterType != null) {
					aCuMasterType.setCuMasterTypeId(cuMasterTypeId);
					aCuMasterType.setDescription(description);
					aCuMasterType.setCode(code);
					aCuMasterType.setInActive(inActive);
					aSession.update(aCuMasterType);
				}
			} else {
				aSession.save(aCuMasterType);
			}
			aTransaction.commit();
		} catch (Exception e) {
			aTransaction.rollback();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void deleteCustomerType(Integer cuMasterTypeId) {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (cuMasterTypeId != null) {
				CuMasterType aCuMasterType = (CuMasterType) aSession.get(
						CuMasterType.class, cuMasterTypeId);
				if (aCuMasterType != null) {
					aSession.delete(aCuMasterType);
				}
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Cumaster getUserbasedonCustomer(int customerid) {
		itsLogger.debug("Retrieving getsalesrepList");
		String aSalesselectQry = "SELECT "
				+ "cuAssignmentID0 AS salesrep ,(SELECT rxm.FullName FROM cuMaster cu JOIN tsUserLogin rxm ON(cu.cuAssignmentID0=rxm.UserLoginId) WHERE cu.cuMasterID='"
				+ customerid
				+ "' AND rxm.inactive='0') AS SalesNAME ,"
				+ "cuAssignmentID1 AS csr,(SELECT rxm.FullName FROM cuMaster cu JOIN tsUserLogin rxm ON(cu.cuAssignmentID1=rxm.UserLoginId) WHERE cu.cuMasterID='"
				+ customerid
				+ "' AND rxm.inactive='0') AS csrNAME,"
				+ "cuAssignmentID2 AS salesmgr,(SELECT rxm.FullName FROM cuMaster cu JOIN tsUserLogin rxm ON(cu.cuAssignmentID2=rxm.UserLoginId) WHERE cu.cuMasterID='"
				+ customerid
				+ "' AND rxm.inactive='0') AS salesmgrNAME ,"
				+ "cuAssignmentID3 AS Engr,(SELECT rxm.FullName FROM cuMaster cu JOIN tsUserLogin rxm ON(cu.cuAssignmentID3=rxm.UserLoginId) WHERE cu.cuMasterID='"
				+ customerid
				+ "' AND rxm.inactive='0') AS EngrName ,"
				+ "cuAssignmentID4 AS promgr,(SELECT rxm.FullName FROM cuMaster cu JOIN tsUserLogin rxm ON(cu.cuAssignmentID4=rxm.UserLoginId) WHERE cu.cuMasterID='"
				+ customerid + "' AND rxm.inactive='0') AS promgrname "
				+ ", CreditHold FROM cuMaster WHERE cumasterID=" + customerid;
		itsLogger.info(" aSalesselectQry = "+aSalesselectQry);
		Session aSession = null;
		Cumaster cu = null;Query aQuery =null;
//		String str = null;
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				cu = new Cumaster();
				cu.setCuAssignmentId0((Integer) aObj[0]);
				cu.setSalesrep((String) aObj[1]);
				cu.setCuAssignmentId1((Integer) aObj[2]);
				cu.setCsr((String) aObj[3]);
				cu.setCuAssignmentId2((Integer) aObj[4]);
				cu.setSalesmgr((String) aObj[5]);
				cu.setCuAssignmentId3((Integer) aObj[6]);
				cu.setEngineer((String) aObj[7]);
				cu.setCuAssignmentId4((Integer) aObj[8]);
				cu.setProjectmgr((String) aObj[9]);
				
				Byte b = (Byte) aObj[10];
				itsLogger.info("byte = "+b+"  || (String) aObj[10] ->   CreditHold = "+(Byte) aObj[10]);
				if(b==0)
					cu.setCreditHold(false);
				else
					cu.setCreditHold(true);
			}else{
				cu = new Cumaster();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null; aQuery =null;
		}

		return cu;
	}

	@Override
	public List<?> getCustomerPaymentSearchList(AutoCompleteBean theSearchBean,
			String searchText, Date fromdate, Date todate, String status) {
		itsLogger.debug("Query to Select getJobSearch");

		//String condition = "";
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// System.out.println("fromdate ::"+fromdate+" todate ::"+todate);
		// System.out.println("fromdate ::"+sdf.format(fromdate)+" todate ::"+sdf.format(todate));
		/*
		 * if(status.equalsIgnoreCase("from")) { condition =
		 * "AND cr.ReceiptDate>='"+sdf.format(fromdate)+"'"; }
		 * 
		 * if(status.equalsIgnoreCase("to")){ condition =
		 * "AND cr.ReceiptDate<='"+sdf.format(todate)+"'"; }
		 * 
		 * if(status.equalsIgnoreCase("both")){
		 * 
		 * condition
		 * ="AND (cr.ReceiptDate BETWEEN '"+sdf.format(fromdate)+"' AND '"
		 * +sdf.format(todate)+"' )"; //condition =
		 * "AND cr.ReceiptDate>="+sdf.format
		 * (fromdate)+" AND cr.ReceiptDate<="+sdf.format(todate) +" "; }
		 */
		String aSearchSelectQry = "SELECT cr.cuReceiptID,cr.ReceiptDate,CONCAT(rm.Name, ' ', rm.FirstName) AS Customer, IFNULL(cr.Reference,'') AS Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,cr.rxCustomerID,cr.ReceiptDate FROM rxMaster rm RIGHT JOIN cuReceipt cr ON rm.rxMasterID = cr.rxCustomerID WHERE (rm.Name LIKE '%"
				+ searchText
				+ "%' OR rm.FirstName LIKE '%"
				+ searchText
				+ "%' OR cr.Memo LIKE '%"
				+ searchText
				+ "%' OR cr.Amount LIKE '%"
				+ searchText
				+ "%' OR cr.Reference LIKE '%"
				+ searchText
				+ "%') ORDER BY cr.ReceiptDate DESC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchBean.setValue(aObj[2] + " | " + aObj[3] + " | "
						+ aObj[5]);
				theSearchBean.setLabel(aObj[2] + " | " + aObj[3] + " | "
						+ aObj[5]);
				theSearchBean.setId((Integer) aObj[0]);

				theSearchBean.setManufactureID((Integer) aObj[7]);
				theSearchBean.setRolodexEntity((String) aObj[2]);
				theSearchBean.setBidDate((Date) aObj[8]);
				theSearchBean.setTaxValue((BigDecimal) aObj[5]);
				theSearchBean.setMemo((String) aObj[4]);
				theSearchBean.setTypeId((Short) aObj[6]);
				theSearchBean.setCheckNo((String) aObj[3]);
				aQueryList.add(theSearchBean);
			}
			if (aQueryList.isEmpty()) {
				theSearchBean.setValue(" ");
				theSearchBean.setLabel(" ");
				aQueryList.add(theSearchBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;aSearchSelectQry =null;
		}
		return aQueryList;
	}

	/*
	 * Created by:Praveenkumar Date : 2014-09-04 Purpose: showing customer due
	 * details in customer rolodex page
	 */
	@Override
	public ArrayList<BigDecimal> getCustomerAR(Integer rxCustomerID) {
		Session session = null;
		DateFormat inputDF = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		ArrayList<BigDecimal> dueDetails = new ArrayList<BigDecimal>();
		String invoiceDate = inputDF.format(date);
		/*String customerOverallQuery = "SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur"
				+ " , SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30"
				+ " , SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60"
				+ " , SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM"
				+ " (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'"
				+ invoiceDate
				+ "') AS Days"
				+ " FROM cuInvoice WHERE (TransactionStatus>0) AND (abs(InvoiceAmount-AppliedAmount) > .02) "
				+ " AND (rxCustomerID=" + rxCustomerID + ")) AS SubQuery";*/
		String customerOverallQuery="SELECT IFNULL(SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ),0) AS AmtCur, IFNULL(SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ),0) AS Amt30, IFNULL(SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ),0) AS Amt60, IFNULL(SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ),0) AS Amt90 FROM (SELECT  InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance, DATEDIFF('"+invoiceDate+"',InvoiceDate) AS Days FROM cuInvoice WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND TransactionStatus>0 AND (ABS(InvoiceAmount-AppliedAmount) > .01) AND (rxCustomerID="+rxCustomerID+" ) AND (InvoiceDate <= '"+invoiceDate+"') "
				+ " UNION ALL"
				+ " (SELECT IFNULL(balance,0) - SUM(Amount) AS Balance ,DATEDIFF('"+invoiceDate+"',ReceiptDate) AS Days"
				+ " FROM cuReceipt cr LEFT JOIN (SELECT cuReceiptID,SUM(IFNULL(PaymentApplied,0)) AS balance FROM cuLinkageDetail"
				+ " WHERE rxCustomerID ="+rxCustomerID+" GROUP BY cuReceiptID ORDER BY cuReceiptID) cdl ON cdl.cuReceiptID = cr.cuReceiptID"
				+ " WHERE cr.rxCustomerID  = "+rxCustomerID+" GROUP BY cr.cuReceiptID HAVING Balance<0 ORDER BY cr.cuReceiptID,ReceiptDate)) AS subquery";
		
		
		
		Query aQuery8 = null;
		try {
			session = itsSessionFactory.openSession();
			aQuery8 = session.createSQLQuery(customerOverallQuery);
			Iterator<?> customeriterator = aQuery8.list().iterator();
			
			while (customeriterator.hasNext()) {
				Object[] aobj = (Object[]) customeriterator.next();
				if (aobj[0] != null) {
			
					dueDetails.add((BigDecimal) aobj[0]);

				} else {
					dueDetails.add(new BigDecimal(0));
				}
				if (aobj[1] != null) {
					dueDetails.add((BigDecimal) aobj[1]);
			
				} else {
					dueDetails.add(new BigDecimal(0));
				}
				if (aobj[2] != null) {
					dueDetails.add((BigDecimal) aobj[2]);
			
				} else {
					dueDetails.add(new BigDecimal(0));
					
				}
				if (aobj[3] != null) {
					dueDetails.add((BigDecimal) aobj[3]);
			
				} else {
					dueDetails.add(new BigDecimal(0));
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			session.flush();
			session.close();
			customerOverallQuery = null;aQuery8 = null;
		}
		return dueDetails;
	}

	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Fetching the data for grid  from  rxaddress table in customer screen
	 */
	@Override
	public ArrayList<Rxaddress> getCustomerAddressBasedonrxAddress(int rxMasterID) throws CustomerException {
		String vendorQry = "SELECT ra.Address1, ra.City, ra.State, ra.Zip, ra.Phone1, ra.Fax,ra.Address2,ra.Phone2,ra.IsMailing,ra.IsShipTo ,ra.rxAddressID,ra.IsDefault FROM rxAddress ra  WHERE ra.rxMasterID = '" + rxMasterID + "' " ;
		Session aSession = null;Query aQuery =null;
		ArrayList<Rxaddress> vendorAddressQry = new ArrayList<Rxaddress>();
		try{
			Rxaddress aVendorAddress = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(vendorQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorAddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				aVendorAddress.setAddress1((String) aObj[0]);
				aVendorAddress.setCity((String) aObj[1]);
				aVendorAddress.setState((String) aObj[2]);
				aVendorAddress.setZip((String) aObj[3]);
				aVendorAddress.setPhone1((String) aObj[4]);
				aVendorAddress.setFax((String) aObj[5]);
				aVendorAddress.setAddress2((String) aObj[6]);
				aVendorAddress.setPhone2((String) aObj[7]);
				Byte ismail= ((Byte) aObj[8]);
				if(ismail == 1){
					aVendorAddress.setIsMailing(true);
				}
				Byte isship= ((Byte) aObj[9]);
				if(isship == 1){
					aVendorAddress.setIsShipTo(true);
				}
				aVendorAddress.setRxAddressId((Integer)aObj[10]);
				Byte isDefault= ((Byte) aObj[11]);
				if(isDefault == 1){
					aVendorAddress.setIsDefault(true);
				}
				vendorAddressQry.add(aVendorAddress);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CustomerException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry =null;aQuery =null;
		}
		return vendorAddressQry;
	}
	
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Fetching the data from rxaddress for edit popup from customer screen
	 */
	@Override
	public Rxaddress getRxaddressDetails(int rxAddressID) throws CustomerException {
		String vendorQry = "SELECT ra.Address1, ra.City, ra.State, ra.Zip, ra.Phone1, ra.Fax,ra.Address2,ra.Phone2,ra.IsMailing,ra.IsShipTo ,ra.rxAddressID,ra.IsDefault "
				+ "FROM rxAddress ra WHERE ra.rxAddressID = '" + rxAddressID + "' " ;
		Session aSession = null;
		Rxaddress aVendorAddress = null;Query aQuery = null;
		try{
			
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(vendorQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				aVendorAddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				aVendorAddress.setAddress1((String) aObj[0]);
				aVendorAddress.setCity((String) aObj[1]);
				aVendorAddress.setState((String) aObj[2]);
				aVendorAddress.setZip((String) aObj[3]);
				aVendorAddress.setPhone1((String) aObj[4]);
				aVendorAddress.setFax((String) aObj[5]);
				aVendorAddress.setAddress2((String) aObj[6]);
				aVendorAddress.setPhone2((String) aObj[7]);
				Byte ismail= ((Byte) aObj[8]);
				if(ismail == 1){
					aVendorAddress.setIsMailing(true);
				}
				Byte isship= ((Byte) aObj[9]);
				if(isship == 1){
					aVendorAddress.setIsShipTo(true);
				}
				aVendorAddress.setRxAddressId((Integer)aObj[10]);
				Byte isdefault= ((Byte) aObj[11]);
				if(isdefault == 1){
					aVendorAddress.setIsDefault(true);
				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CustomerException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry =null;aQuery = null;
		}
		return aVendorAddress;
	}
	@Override
	public boolean updateRxaddress(Rxaddress therxaddress) throws CustomerException {
		Session aCuMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuMasterSession.beginTransaction();
			aTransaction.begin();
			Rxaddress aRxaddress = (Rxaddress) aCuMasterSession.get(	Rxaddress.class, therxaddress.getRxAddressId());
			aRxaddress.setName(therxaddress.getName());
			aRxaddress.setAddress1(therxaddress.getAddress1());
			aRxaddress.setAddress2(therxaddress.getAddress2());
			aRxaddress.setCity(therxaddress.getCity());
			aRxaddress.setState(therxaddress.getState());
			aRxaddress.setZip(therxaddress.getZip());
			aRxaddress.setInActive(therxaddress.getInActive());
			aRxaddress.setIsMailing(therxaddress.getIsMailing());
			aRxaddress.setIsShipTo(therxaddress.getIsShipTo());
			aRxaddress.setIsStreet(therxaddress.getIsStreet());
			aRxaddress.setIsBillTo(therxaddress.getIsBillTo());
			aRxaddress.setOtherBillTo(2);
			//aRxaddress.setCoTaxTerritoryId(therxaddress.getCoTaxTerritoryId());
			aRxaddress.setOtherShipTo(3);
			aRxaddress.setRxAddressId(therxaddress.getRxAddressId());
			
			aCuMasterSession.update(aRxaddress);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CustomerException aCustomerException = new CustomerException(excep.getMessage(),
					excep);
			throw aCustomerException;
		} finally {
			aCuMasterSession.flush();
			aCuMasterSession.close();
		}
		return false;
	}

	
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Fetching the taxrate depends upon cotaxterritoryid from coTaxTerritory table
	 */
	@Override
	public CoTaxTerritory gettaxratedependsoncotax(int CoTaxTerritoryID) throws CustomerException {
		itsLogger.debug("Retrieving aCoTaxTerritory");
		Session aSession = null;
		CoTaxTerritory aCoTaxTerritory = null;
		try {
			aCoTaxTerritory = new CoTaxTerritory();
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aCoTaxTerritory = (CoTaxTerritory) aSession.get(CoTaxTerritory.class, CoTaxTerritoryID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCoTaxTerritory;
	}
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Delete the customer address from rxaddress table
	 */
	@Override
	public void deleteCustomerAddress(Integer rxAddressId) {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (rxAddressId != null) {
				Rxaddress aRxaddress = (Rxaddress) aSession.get(
						Rxaddress.class, rxAddressId);
				if (aRxaddress != null) {
					aSession.delete(aRxaddress);
				}
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	/*
	 * Created By:Velmurugan
	 * Created on: 10-9-2013
	 * Description:Default checkbox status in rxaddress table
	 */
	@Override
	public boolean Checkdefaultaddressstatus(int rxMasterID,int rxaddressId) throws CustomerException {
		
		String vendorQry = "SELECT ra.Address1, ra.IsDefault FROM rxAddress ra  WHERE ra.rxMasterID = '"+rxMasterID+"' AND ra.isDefault=1  " ;
		
		Session aSession = null;Query aQuery =null;
		boolean returnvalue=false;
		try{
			//Rxaddress aVendorAddress = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(vendorQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			/*if(aQuery.list()!=null){
				returnvalue =aQuery.list().contains(1);
				
				itsLogger.info("returnvaluereturnvaluereturnvalue"+returnvalue);
			}*/
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				
				Byte isDefault= ((Byte) aObj[1]);
				if(isDefault == 1){
					returnvalue=true;
				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CustomerException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry =null;aQuery =null;
		}
		return returnvalue;
	}
	
	/*@Override
	public Rxmastercategory2 getrxMasterCategory2(int therxmasterId) {
		itsLogger.debug("Retrieving Customer Details List"+therxmasterId);
		String vendorQry = "" ;
		Session aSession = null;
		Rxmastercategory2 aRxmastercategory2 = null;
		try {
			 aRxmastercategory2 = new Rxmastercategory2();
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aRxmastercategory2 = (Rxmastercategory2) aSession.get(Rxmastercategory2.class, therxmasterId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxmastercategory2;
	}*/
	
	@Override
	public Rxmastercategory2 getrxMasterCategory2(int rxAddressID) throws CustomerException {
		String vendorQry = "SELECT cuAssignmentID0,cuAssignmentID1,cuAssignmentID2,cuAssignmentID3,cuAssignmentID4,ts0.FullName AS name0,ts1.FullName AS name1,ts2.FullName AS name2,ts3.FullName AS name3,ts4.FullName AS name4 FROM rxMasterCategory2 "+
				"LEFT JOIN tsUserLogin ts0 ON(ts0.UserLoginId=rxMasterCategory2.cuAssignmentID0) "+
				"LEFT JOIN tsUserLogin ts1 ON(ts1.UserLoginId=rxMasterCategory2.cuAssignmentID1) "+
				"LEFT JOIN tsUserLogin ts2 ON(ts2.UserLoginId=rxMasterCategory2.cuAssignmentID2) "+
				"LEFT JOIN tsUserLogin ts3 ON(ts3.UserLoginId=rxMasterCategory2.cuAssignmentID3) "+
				"LEFT JOIN tsUserLogin ts4 ON(ts4.UserLoginId=rxMasterCategory2.cuAssignmentID4) "+
				"WHERE rxMasterCategory2ID=" + rxAddressID  ;
		Session aSession = null;Query aQuery =null;
		Rxmastercategory2 aRxmastercategory2 = null;
		try{
			
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(vendorQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				aRxmastercategory2 = new Rxmastercategory2();
				Object[] aObj = (Object[])aIterator.next();
				if(aObj[0]!=null){
				aRxmastercategory2.setCuAssignmentId0((Integer) aObj[0]);
				}
				if(aObj[1]!=null){
				aRxmastercategory2.setCuAssignmentId1((Integer) aObj[1]);
				}
				if(aObj[2]!=null){
				aRxmastercategory2.setCuAssignmentId2((Integer) aObj[2]);
				}
				if(aObj[3]!=null){
				aRxmastercategory2.setCuAssignmentId3((Integer) aObj[3]);
				}
				if(aObj[4]!=null){
				aRxmastercategory2.setCuAssignmentId4((Integer) aObj[4]);
				}
				if(aObj[5]!=null){
				aRxmastercategory2.setCuAssignmentName0((String)aObj[5]);
				}
				if(aObj[6]!=null){
					aRxmastercategory2.setCuAssignmentName1((String)aObj[6]);			
								}
				if(aObj[7]!=null){
					aRxmastercategory2.setCuAssignmentName2((String)aObj[7]);			
								}
				if(aObj[8]!=null){
					aRxmastercategory2.setCuAssignmentName3((String)aObj[8]);
				}
				if(aObj[9]!=null){
					aRxmastercategory2.setCuAssignmentName4((String)aObj[9]);
				}
				
				aRxmastercategory2.setRxMasterCategory2id(rxAddressID);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CustomerException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry =null;aQuery =null;
		}
		return aRxmastercategory2;
	}
	
	
	@Override
	public Rxmastercategory1 getrxMasterCategory1(int rxAddressID) throws CustomerException {
		String vendorQry = "SELECT cuAssignmentID0,cuAssignmentID1,cuAssignmentID2,cuAssignmentID3,cuAssignmentID4,ts0.FullName AS name0,ts1.FullName AS name1,ts2.FullName AS name2,ts3.FullName AS name3,ts4.FullName AS name4 FROM rxMasterCategory1 "+
				"LEFT JOIN tsUserLogin ts0 ON(ts0.UserLoginId=rxMasterCategory1.cuAssignmentID0) "+
				"LEFT JOIN tsUserLogin ts1 ON(ts1.UserLoginId=rxMasterCategory1.cuAssignmentID1) "+
				"LEFT JOIN tsUserLogin ts2 ON(ts2.UserLoginId=rxMasterCategory1.cuAssignmentID2) "+
				"LEFT JOIN tsUserLogin ts3 ON(ts3.UserLoginId=rxMasterCategory1.cuAssignmentID3) "+
				"LEFT JOIN tsUserLogin ts4 ON(ts4.UserLoginId=rxMasterCategory1.cuAssignmentID4) "+
				"WHERE rxMasterCategory1ID=" + rxAddressID  ;
		Session aSession = null;Query aQuery =null;
		Rxmastercategory1 aRxmastercategory1 = null;
		try{
			
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(vendorQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				aRxmastercategory1 = new Rxmastercategory1();
				Object[] aObj = (Object[])aIterator.next();
				if(aObj[0]!=null){
					aRxmastercategory1.setCuAssignmentId0((Integer) aObj[0]);
				}
				if(aObj[1]!=null){
					aRxmastercategory1.setCuAssignmentId1((Integer) aObj[1]);
				}
				if(aObj[2]!=null){
					aRxmastercategory1.setCuAssignmentId2((Integer) aObj[2]);
				}
				if(aObj[3]!=null){
					aRxmastercategory1.setCuAssignmentId3((Integer) aObj[3]);
				}
				if(aObj[4]!=null){
					aRxmastercategory1.setCuAssignmentId4((Integer) aObj[4]);
				}
				if(aObj[5]!=null){
					aRxmastercategory1.setCuAssignmentName0((String)aObj[5]);
				}
				if(aObj[6]!=null){
					aRxmastercategory1.setCuAssignmentName1((String)aObj[6]);			
								}
				if(aObj[7]!=null){
					aRxmastercategory1.setCuAssignmentName2((String)aObj[7]);			
								}
				if(aObj[8]!=null){
					aRxmastercategory1.setCuAssignmentName3((String)aObj[8]);
				}
				if(aObj[9]!=null){
					aRxmastercategory1.setCuAssignmentName4((String)aObj[9]);
				}
				
				aRxmastercategory1.setRxMasterCategory1id(rxAddressID);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CustomerException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry =null;aQuery =null;
		}
		return aRxmastercategory1;
	}
	/*Created By:Velmurugan
	 *Created On:16-9-2014
	 *Description:Fetch views tab data from rxMasterCategoryView
	 * */
	@Override
	public RxMasterCategoryView getrxMasterCategoryView(int rxAddressID) throws CustomerException {
		String vendorQry = "SELECT cuAssignmentID0,cuAssignmentID1,cuAssignmentID2,cuAssignmentID3,cuAssignmentID4,ts0.FullName AS name0,ts1.FullName AS name1,ts2.FullName AS name2,ts3.FullName AS name3,ts4.FullName AS name4 FROM rxMasterCategoryView "+
				"LEFT JOIN tsUserLogin ts0 ON(ts0.UserLoginId=rxMasterCategoryView.cuAssignmentID0) "+
				"LEFT JOIN tsUserLogin ts1 ON(ts1.UserLoginId=rxMasterCategoryView.cuAssignmentID1) "+
				"LEFT JOIN tsUserLogin ts2 ON(ts2.UserLoginId=rxMasterCategoryView.cuAssignmentID2) "+
				"LEFT JOIN tsUserLogin ts3 ON(ts3.UserLoginId=rxMasterCategoryView.cuAssignmentID3) "+
				"LEFT JOIN tsUserLogin ts4 ON(ts4.UserLoginId=rxMasterCategoryView.cuAssignmentID4) "+
				"WHERE rxMasterCategoryViewID=" + rxAddressID  ;
		itsLogger.info("getrxMasterCategoryView=="+vendorQry);
		Session aSession = null;Query aQuery =null; 
		RxMasterCategoryView aRxmastercategoryView = null;
		try{
			
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(vendorQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				aRxmastercategoryView = new RxMasterCategoryView();
				Object[] aObj = (Object[])aIterator.next();
				if(aObj[0]!=null){
					aRxmastercategoryView.setCuAssignmentId0((Integer) aObj[0]);
				}
				if(aObj[1]!=null){
					aRxmastercategoryView.setCuAssignmentId1((Integer) aObj[1]);
				}
				if(aObj[2]!=null){
					aRxmastercategoryView.setCuAssignmentId2((Integer) aObj[2]);
				}
				if(aObj[3]!=null){
					aRxmastercategoryView.setCuAssignmentId3((Integer) aObj[3]);
				}
				if(aObj[4]!=null){
					aRxmastercategoryView.setCuAssignmentId4((Integer) aObj[4]);
				}
				if(aObj[5]!=null){
					aRxmastercategoryView.setCuAssignmentName0((String)aObj[5]);
				}
				if(aObj[6]!=null){
					aRxmastercategoryView.setCuAssignmentName1((String)aObj[6]);			
								}
				if(aObj[7]!=null){
					aRxmastercategoryView.setCuAssignmentName2((String)aObj[7]);			
								}
				if(aObj[8]!=null){
					aRxmastercategoryView.setCuAssignmentName3((String)aObj[8]);
				}
				if(aObj[9]!=null){
					aRxmastercategoryView.setCuAssignmentName4((String)aObj[9]);
				}
				
				aRxmastercategoryView.setRxMasterCategoryViewid(rxAddressID);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CustomerException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry =null;aQuery =null; 
		}
		return aRxmastercategoryView;
	}

	@Override
	public Integer addCreditDebitDetails(Cuinvoice acuInvoice)
			throws CustomerException {
		
		Session aSession = null;
		Transaction aTransaction = null;
		int divisionId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			divisionId = (Integer) aSession.save(acuInvoice);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return divisionId;
	}
	

	@Override
	public void updateCreditDebitDetails(Cuinvoice acuInvoice)
			throws CustomerException {
		
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aSession.update(acuInvoice);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
	}
	
	
	@Override
	public void addCreditDebitDetailstoInvDetails(Cuinvoicedetail acuInvoicedet)
			throws CustomerException {
		
		Session aSession = null;
		Transaction aTransaction = null;
		int divisionId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			divisionId = (Integer) aSession.save(acuInvoicedet);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
	}
	
	@Override
	public void updateCreditDebitDetailstoInvDetails(Cuinvoicedetail acuInvoicedet)
			throws CustomerException {
		
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aSession.update(acuInvoicedet);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
	}

	@Override
	public String getInvoiceNofromcuInvoice(int credittype)
			throws CustomerException {
		// TODO Auto-generated method stub
		
		Session aSession = null;
		String cdaQuery= null;
		if(credittype == 1)
		cdaQuery="SELECT SUBSTRING(MAX(InvoiceNumber),3) FROM cuInvoice WHERE SUBSTRING(InvoiceNumber,3) REGEXP '^[0-9]+' AND CreditMemo=1 AND IsCredit="+credittype+" AND InvoiceNumber LIKE 'CR%' ORDER BY 1 DESC";
		else
		cdaQuery="SELECT SUBSTRING(MAX(InvoiceNumber),3) FROM cuInvoice WHERE SUBSTRING(InvoiceNumber,3) REGEXP '^[0-9]+' AND CreditMemo=1 AND IsCredit="+credittype+" AND InvoiceNumber LIKE 'DB%' ORDER BY 1 DESC";
		
		Query aQuery =null;
		String InvoiceNo = "";
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(cdaQuery);
			InvoiceNo = (String) aQuery.uniqueResult();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			cdaQuery=null;
			aQuery =null;
		}
		
		return InvoiceNo;
	}
	
	@Override
	public ArrayList<GlLinkage> getglLinkagewithinvoiceId(int cuInvoiceId, Coledgersource coledgersource) throws CustomerException {
		Session aSession = null;
		ArrayList<GlLinkage> recieptTypeList = null;Query query = null;
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from GlLinkage where veBillID="+cuInvoiceId+" and coLedgerSourceId="+coledgersource.getCoLedgerSourceId()+" and status<>1");
			recieptTypeList = (ArrayList<GlLinkage>)query.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return recieptTypeList;
	}

	@Override
	public void memoPdfPrint(HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws CustomerException, SQLException {
		
		Session printBeanSession =itsSessionFactory.openSession();
	    SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) printBeanSession.getSessionFactory();
	    ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		Map<String, Object> params = new HashMap<String, Object>();
		//Rxaddress address = null;
		Connection connection =null;
		try{ 
			String type="";
			if(theRequest.getParameter("memotype").equals("1"))
			{
				type="CREDIT MEMO";
			}
			else
			{
				type="DEBIT MEMO";
			}
			
			params.put("invoiceID", Integer.parseInt(theRequest.getParameter("invoiceID")));
			params.put("type", type);
			itsLogger.info("For PDF Inputs: "+theRequest.getParameter("invoiceID")+"\n"+theRequest.getParameter("type"));
			ServletOutputStream out = theResponse.getOutputStream();
			String fileName = theRequest.getParameter("filename");
			theResponse.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			theResponse.setContentType("application/pdf");
			connection = connectionProvider.getConnection();
			String path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/memo.jrxml");
			JasperReport report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params, connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
		//	throw aCustomerException;
		} finally {
			
			printBeanSession.flush();
			printBeanSession.close();
			if(connectionProvider!=null){
				connectionProvider.closeConnection(connection);
				connectionProvider=null;
				}
		}
	}

	@Override
	public void updateCustomerInvoice(int invoiceID,String flag,UserBean aUserBean)
			throws CustomerException {
		// TODO Auto-generated method stub
		Session aSession = null;
		Cuinvoice cuinvoice =null;
		Transaction aTransaction=null;
		try {
			cuinvoice=new Cuinvoice();
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			cuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class, invoiceID);
			
			cuinvoice.setChangedById(aUserBean.getUserId());
			cuinvoice.setChangedOn(new Date());
			
			if(flag.equals("taxAdj"))
			cuinvoice.setTaxAdjustmentStatus(1);
			else
			cuinvoice.setMemoStatus(0);
			
			aSession.update(cuinvoice);
			aTransaction.commit();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public BigDecimal getYearTodateSale(Integer rxMasterID) throws CustomerException {
		String aPaymentsCountStr = "SELECT SUM(SubTotal) AS TheAmount FROM cuInvoice WHERE TransactionStatus>0 AND "
				+ "rxCustomerID="+rxMasterID+" AND YEAR(InvoiceDate)=YEAR(CURDATE())";
		Session aSession = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsCountStr);
			List<?> aList = aQuery.list();
			return (BigDecimal) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsCountStr=null;aQuery =null;
		}
	}
	
	@Override
	public BigDecimal getLastYearSale(Integer rxMasterID) throws CustomerException {
		String aPaymentsCountStr = "SELECT SUM(SubTotal) AS TheAmount FROM cuInvoice WHERE TransactionStatus>0 "
				+ "AND rxCustomerID="+rxMasterID+" AND YEAR(InvoiceDate)=(YEAR(CURDATE()))-1 ";
		Session aSession = null;Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aPaymentsCountStr);
			List<?> aList = aQuery.list();
			return (BigDecimal) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsCountStr =null;aQuery = null;
		}
	}
			
		@Override
		public Cuinvoice getLastSaleAmount(Integer rxMasterID) throws CustomerException {
			String aPaymentsCountStr = "SELECT InvoiceDate, SubTotal FROM cuInvoice WHERE SubTotal>0 AND "
					+ "rxCustomerID="+rxMasterID+" AND (TransactionStatus > 0) ORDER BY InvoiceDate DESC LIMIT 1 ";
			Session aSession = null;
			Cuinvoice aCuinvoice = new Cuinvoice();
			Query aQuery = null;
			try {
				aSession = itsSessionFactory.openSession();
				aQuery = aSession.createSQLQuery(aPaymentsCountStr);
				Iterator<?> aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();			
					aCuinvoice.setInvoiceDate((Date) aObj[0]);
					aCuinvoice.setSubtotal((BigDecimal) aObj[1]);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.close();
				aPaymentsCountStr =null;aQuery = null;
			}
			return aCuinvoice;
		}

			@Override
			public Cuinvoice getCuInvoicefminvoiceno(String invoiceNumber) throws CustomerException {
				Session aSession = null;Query query =null;
				String queryString = "";
				Cuinvoice cuInvoice = new Cuinvoice();
				Coaccount coAccount = new Coaccount();
				Sysvariable aSysvariable = new Sysvariable();
				try {
					aSession = itsSessionFactory.openSession();
					
					queryString ="SELECT rx.Name,cu.InvoiceNumber,cu.Subtotal,cu.Freight,cu.TaxAmount,cu.InvoiceAmount,cu.coTaxterritoryID,cu.coDivisionID,cT.county,cu.cuInvoiceID,cT.TaxRate,cu.cuAssignmentID0,cu.rxCustomerID,cu.taxAdjustmentStatus,jm.JobStatus,cu.taxfreight FROM cuInvoice cu "
					+ " LEFT JOIN coTaxTerritory cT on cu.coTaxterritoryID = cT.coTaxterritoryID "
					+ " LEFT JOIN rxMaster rx on cu.rxCustomerID = rx.rxMasterID "
					+ " LEFT JOIN joReleaseDetail jrd ON jrd.joReleaseDetailID = cu.joReleaseDetailID "
					+ " LEFT JOIN joMaster jm ON jm.joMasterID = jrd.joMasterID"
					+ " WHERE cu.InvoiceNumber = '"+invoiceNumber+"'";
					
					query = aSession.createSQLQuery(queryString);
					Iterator<?> aIterator = query.list().iterator();
					if (aIterator.hasNext()) {
						
						Object[] aObj = (Object[]) aIterator.next();
						cuInvoice.setCustomerName((String)aObj[0]);
						cuInvoice.setInvoiceNumber((String)aObj[1]);
						cuInvoice.setSubtotal((BigDecimal)aObj[2]);
						cuInvoice.setFreight((BigDecimal)aObj[3]);
						cuInvoice.setTaxAmount((BigDecimal)aObj[4]);
						cuInvoice.setInvoiceAmount((BigDecimal)aObj[5]);
						cuInvoice.setCoTaxTerritoryId((Integer)aObj[6]);
						cuInvoice.setCoDivisionId((Integer)aObj[7]);
						cuInvoice.setCotaxdescription((String)aObj[8]);
						cuInvoice.setCuInvoiceId((Integer)aObj[9]);
						coAccount = getDivisioncoAccountID((Integer)aObj[9]);
						cuInvoice.setCoAccountID(coAccount.getCoAccountId());
						cuInvoice.setTaxRate((BigDecimal)aObj[10]);
						cuInvoice.setCuAssignmentId0((Integer)aObj[11]);
						cuInvoice.setRxCustomerId((Integer)aObj[12]);
						cuInvoice.setTaxAdjustmentStatus((Integer)aObj[13]);
						if(aObj[14]!=null)
						cuInvoice.setTransactionStatus(((Short)aObj[14]).intValue());
						else
						cuInvoice.setTransactionStatus(3);
						
						cuInvoice.setTaxfreight((Byte) aObj[15]);
						
						Cuinvoicedetail obj=itsJobService.getCuInvoiceDetailObj(cuInvoice.getCuInvoiceId());
						cuInvoice.setTaxSum(obj.getTaxSum());
					}
					
					/*aSysvariable = (Sysvariable) aSession.get(Sysvariable.class,InventoryConstant.getConstantSysvariableId("RequireFreightwhencalculatingTaxonCustomerInvoices"));
					System.out.println("valueLong="+aSysvariable.getValueLong());
					cuInvoice.setMemoStatus(aSysvariable.getValueLong());*/
					
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
					CustomerException aCustomerException = new CustomerException(
							e.getMessage(), e);
					throw aCustomerException;
				} finally {
					aSession.flush();
					aSession.close();
					queryString=null;query =null;
				}
				return cuInvoice;
			}
			
		/**
		 * Created by: Leo  Date: Apr 23,2015
		 * 
		 * Description: Get Division Account ID 
		 * */	
		public Coaccount getDivisioncoAccountID(Integer invoiceID)throws CustomerException, BankingException
		{
			ArrayList<SysAccountLinkage> aSysAccountLinkage = itsGltransactionService.getAllsysaccountLinkage(null);
			
			Coaccount CoAccountIdShippingInventoryfromcoAccountdesc =new Coaccount();
			Coaccount CoAccountIdShippingInventoryfromsysAccLink =new Coaccount();
			String[] accNoArray;
			String customAccNo;
			String subAcccountValue=null;
			
			for(SysAccountLinkage sysLinObj:aSysAccountLinkage)
			{
			 CoAccountIdShippingInventoryfromsysAccLink = itsGltransactionService.getCoaccountDetailsBasedoncoAccountid(sysLinObj.getCoAccountIdshipInventory()); 
			 itsLogger.info(""+CoAccountIdShippingInventoryfromsysAccLink.getNumber());
			 accNoArray = (CoAccountIdShippingInventoryfromsysAccLink.getNumber()).split("-");
			 subAcccountValue = itsGltransactionService.getsubAccountfromDivision(invoiceID);
			 if(subAcccountValue!=null)
			 {
			 customAccNo = accNoArray[0]+"-"+subAcccountValue;
			 CoAccountIdShippingInventoryfromcoAccountdesc = itsGltransactionService.getcoAccountfromAccountNumber(customAccNo);
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
			
			return CoAccountIdShippingInventoryfromcoAccountdesc;
			
		}
		

		public Boolean updateCuInvoiceforsuccessfulpayment(Integer cuReceiptID,Session aSession,UserBean aUserBean,Integer yearID,Integer periodID) throws CustomerException
		{
			
			try {
				
				String aPaymentsCountStr = "SELECT * from cuLinkageDetail where cuReceiptID = "+cuReceiptID+" and statusCheck = 1 and deletedStatus<>1";
				Cuinvoice aCuinvoice = new Cuinvoice();
				Cuinvoice theCuinvoice = null;
				Query aQuery = null;
			
					aQuery = aSession.createSQLQuery(aPaymentsCountStr);
					List<?> aQueryList =  aQuery.list();
					Iterator<?> aIterator =aQueryList.iterator();
					while(aIterator.hasNext()) {
						
						Object[] aObj = (Object[]) aIterator.next();
						Integer cuInvoiceID = 	(Integer) aObj[1];
						BigDecimal paymentApplied = (BigDecimal) aObj[4];
						BigDecimal DiscountApplied = (BigDecimal) aObj[5];
						theCuinvoice = new Cuinvoice();
						theCuinvoice = itsJobService.getCustomerInvoiceDetails(cuInvoiceID);
						
						aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,cuInvoiceID);
						aCuinvoice.setAppliedAmount(aCuinvoice.getAppliedAmount().add(paymentApplied));
						aCuinvoice.setDiscountApplied((aCuinvoice.getDiscountApplied()==null?BigDecimal.ZERO:aCuinvoice.getDiscountApplied()).add(DiscountApplied));
						aCuinvoice.setDiscountAmt((aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).add(DiscountApplied));
						aCuinvoice.setTransactionStatus(2);
						aCuinvoice.setChangedById(aUserBean.getUserId());
						
						aSession.update(aCuinvoice);
						if(aCuinvoice.getDiscountAmt().compareTo(BigDecimal.ZERO)!=0)
							itsJobService.saveCustomerInvoiceLog(theCuinvoice,aCuinvoice,"CI-Edited-Payment",1,periodID,yearID);
						else
							itsJobService.saveCustomerInvoiceLog(theCuinvoice,aCuinvoice,"CI-Edited-Payment",0,periodID,yearID);
						
						if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()!=0 ){
						JoReleaseDetail joReleaseDetail = (JoReleaseDetail ) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
						Date curDate = new Date();
						itsLogger.info("Null Pointer Error Date: "+curDate);
						joReleaseDetail.setPaymentDate(curDate);
					//	joReleaseDetail.setBalanceDue(appliedNew.subtract(aCuinvoice.getInvoiceAmount()));
						aSession.update(joReleaseDetail);
						}
					}
					if(aQueryList.size()>0)
					aSession.createSQLQuery("update cuLinkageDetail set statusCheck = 0 where cuReceiptID = "+cuReceiptID).executeUpdate();	
						
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.clear(); 
				
			}
			return true;
		}
		
		public Boolean updateCuInvoiceforunsuccessfulpayment(Integer cuReceiptID,Session aSession,UserBean aUserBean,Integer yearid, Integer periodid) throws CustomerException
		{
			
			try {
				
				String aPaymentsCountStr = "SELECT * from cuLinkageDetail where cuReceiptID = "+cuReceiptID;
				Cuinvoice aCuinvoice = new Cuinvoice();
				Cuinvoice theCuinvoice = new Cuinvoice();
				Query aQuery = null;
			
					aQuery = aSession.createSQLQuery(aPaymentsCountStr);
					Iterator<?> aIterator = aQuery.list().iterator();
					while(aIterator.hasNext()) {
						
						Object[] aObj = (Object[]) aIterator.next();
						Integer cuInvoiceID = 	(Integer) aObj[1];
						BigDecimal paymentApplied = (BigDecimal) aObj[4];
						BigDecimal DiscountApplied = (BigDecimal) aObj[5];
						
						theCuinvoice = itsJobService.getCustomerInvoiceDetails(cuInvoiceID);
						aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,cuInvoiceID);
						
						
						if(aCuinvoice.getAppliedAmount().compareTo(BigDecimal.ZERO)==1)
						aCuinvoice.setAppliedAmount(aCuinvoice.getAppliedAmount().subtract(paymentApplied));
						else
						aCuinvoice.setAppliedAmount(BigDecimal.ZERO);
						
						if((aCuinvoice.getDiscountApplied()==null?BigDecimal.ZERO:aCuinvoice.getDiscountApplied()).compareTo(BigDecimal.ZERO)==1)
						aCuinvoice.setDiscountApplied((aCuinvoice.getDiscountApplied()==null?BigDecimal.ZERO:aCuinvoice.getDiscountApplied()).subtract(DiscountApplied));
						else
						aCuinvoice.setDiscountApplied(BigDecimal.ZERO);	
						
						if((aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).compareTo(BigDecimal.ZERO)==1)
						aCuinvoice.setDiscountAmt((aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).subtract(DiscountApplied));
						else
						aCuinvoice.setDiscountAmt(BigDecimal.ZERO);	
						
						aCuinvoice.setTransactionStatus(1);
						aCuinvoice.setChangedById(aUserBean.getUserId());
						aSession.update(aCuinvoice);
						
						if(DiscountApplied.compareTo(BigDecimal.ZERO)!=0)
						itsJobService.saveCustomerInvoiceLog(theCuinvoice,aCuinvoice,"CI-Edited-Payment",1,periodid,yearid);
						else
						itsJobService.saveCustomerInvoiceLog(theCuinvoice,aCuinvoice,"CI-Edited-Payment",0,periodid,yearid);
						
						if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()!=0 ){
						JoReleaseDetail joReleaseDetail = (JoReleaseDetail ) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
						Date curDate = new Date();
						itsLogger.info("Null Pointer Error Date: "+curDate);
						joReleaseDetail.setPaymentDate(null);
					//	joReleaseDetail.setBalanceDue(appliedNew.subtract(aCuinvoice.getInvoiceAmount()));
						aSession.update(joReleaseDetail);
						}
					}
					//aSession.createSQLQuery("update cuLinkageDetail set statusCheck = 0 where cuRecieptID = "+cuReceiptID).executeUpdate();
					
						
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.clear(); 
				
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean getreversePaymentvalidation(Integer receiptID) throws CustomerException{
			
			Session aSession = null;
			List<Object> aEcCommision = null;
			boolean commissionStatus = false;
			try {
				aSession = itsSessionFactory.openSession();
				
			/*	aEcCommision = (Integer) aSession.createSQLQuery("SELECT ecInvoicePeriodID FROM ecInvoicePeriod "
						+ "WHERE cuInvoiceID IN (SELECT cuInvoiceID FROM cuLinkageDetail "
						+ "WHERE cuReceiptID = "+receiptID+" GROUP BY cuInvoiceID) LIMIT 1 ").uniqueResult(); */
				
				aEcCommision =  (List<Object>) aSession.createSQLQuery("SELECT ec.ecInvoicePeriodID,ci.cuInvoiceID,ci.InvoiceAmount,(ci.AppliedAmount+ci.DiscountAmt) AS AppAmt,(cl.paymentApplied+cl.DiscountUsed) AS appforinv FROM ecInvoicePeriod ec"
						+ " LEFT JOIN cuInvoice ci ON ci.cuInvoiceID = ec.cuInvoiceID"
						+ " LEFT JOIN cuLinkageDetail cl ON cl.cuInvoiceID = ci.cuInvoiceId"
						+ " WHERE cl.cuInvoiceID IS NOT NULL AND cl.cuReceiptID = "+receiptID+" GROUP BY cl.cuInvoiceID"
						+ " HAVING IF(appforinv < 0,ci.InvoiceAmount < 0,ci.InvoiceAmount > 0) LIMIT 1").list();
				
				if(aEcCommision!=null && !aEcCommision.isEmpty())
					commissionStatus = true;
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return commissionStatus;
		}

		@Override
		public void postcuLinkageDetail(Cureceipt cuReceiptObj, Integer yearID,
				Integer periodID, UserBean aUserBean, String paidInvoiceDetails,Session aSession)
				throws CustomerException, BankingException {
			
			try {
			JsonParser parser = new JsonParser();
			boolean stausbit = false;
			if ( paidInvoiceDetails !=null) {
				System.out.println("gridData"+paidInvoiceDetails);
				JsonElement ele = parser.parse(paidInvoiceDetails);
				JsonArray array = ele.getAsJsonArray();
				System.out.println("array length==>"+array.size());
				for (JsonElement ele1 : array) {
					JsonObject obj = ele1.getAsJsonObject();
					
					String paymentApplied=obj.get("paymentApplied").getAsString().replaceAll("[$,]", "")==""?"0":obj.get("paymentApplied").getAsString().replaceAll("[$,]", "");
					String discountAmount = obj.get("preDiscountUsed").getAsString().replaceAll("[$,]", "")==""?"0":obj.get("preDiscountUsed").getAsString().replaceAll("[$,]", "");
					String amtApplied = obj.get("invoiceBalance").getAsString().replaceAll("[$,]", "")==""?"0":obj.get("invoiceBalance").getAsString().replaceAll("[$,]", "");
					Integer cuLinkID =0;
					
					CuLinkageDetail theCulinkagedetail = new CuLinkageDetail();
					
					theCulinkagedetail.setCuReceiptId(cuReceiptObj.getCuReceiptId());
					theCulinkagedetail.setCuInvoiceId(obj.get("cuInvoiceID").getAsInt());
					theCulinkagedetail.setRxCustomerId(cuReceiptObj.getRxCustomerId());
					theCulinkagedetail.setPaymentApplied(new BigDecimal(paymentApplied));
					theCulinkagedetail.setDiscountUsed(new BigDecimal(discountAmount));
					theCulinkagedetail.setRefInvoiceID(obj.get("cuInvoiceID").getAsInt());
					theCulinkagedetail.setAmtApplied(new BigDecimal(amtApplied));
					theCulinkagedetail.setDatePaid(new Date());
					theCulinkagedetail.setStatusCheck(1);
					cuLinkID= (Integer)aSession.save(theCulinkagedetail);
					
					CuPaymentGlpost aCuPaymentGlpost = new CuPaymentGlpost();
					aCuPaymentGlpost.setCuLinkageDetailID(theCulinkagedetail.getCuLinkageDetailId());
					aCuPaymentGlpost.setGroupID(1);
					aCuPaymentGlpost.setRxCustomerID(theCulinkagedetail.getRxCustomerId());
					aCuPaymentGlpost.setCuInvoiceID(theCulinkagedetail.getCuInvoiceId());
					aSession.save(aCuPaymentGlpost);
					
					stausbit = true;
					
					}	
				}
			
			if(stausbit)
			{
			String aPaymentsCountStr = "SELECT MAX(groupID)+1 AS count FROM cuPaymentGlpost";
			BigInteger maxval = new BigInteger("0");
		
			Query aGroupQuery = aSession.createSQLQuery(aPaymentsCountStr);
			List<?> aList = aGroupQuery.list();
			maxval =  (BigInteger) aList.get(0);
		
			String hql = "UPDATE CuPaymentGlpost set postStatus = 1, groupID = "+maxval+" WHERE postStatus = 0";
			Query query = aSession.createQuery(hql);
			query.executeUpdate();
			updateCuInvoiceforsuccessfulpayment(cuReceiptObj.getCuReceiptId(), aSession, aUserBean, yearID, periodID);
			}
			
			} 
			catch (Exception e) {
				e.printStackTrace();
				}
			
		}

		@Override
		public boolean processReversePayments(Integer receiptID,UserBean aUserBean,Integer yearid,Integer periodid)throws CustomerException {

			Session aSession = null;
			Transaction aTransaction = null;

			try {
				aSession = itsSessionFactory.openSession();
				aTransaction= aSession.beginTransaction();
				Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CP");
				
				GlRollback glRollback = new GlRollback();
				glRollback.setVeBillID(receiptID);
				glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
				glRollback.setPeriodID(periodid);
				glRollback.setYearID(yearid);
				glRollback.setTransactionDate(new Date());	
				
				itsGltransactionService.rollBackGlforCuPayment(glRollback,aSession);
				copyAllinvoicesfromclg(receiptID,aSession);
				updaterevstatusinreceipt(receiptID,aSession);
				updateCuInvoiceforunsuccessfulpayment(receiptID, aSession,aUserBean,yearid,periodid);
				deleteAllInvoicesfromclg(receiptID,aSession);				
				aTransaction.commit();
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				aTransaction.rollback();
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			
			
			return true;
		}
		
		
		public void updaterevstatusinreceipt(Integer receiptID,Session aSession)throws CustomerException
		{
			try
			{
				
			aSession.createSQLQuery("update cuReceipt set reversePaymentStatus =1 , Memo = 'PAYMENT REVERSED' where cuReceiptID ="+receiptID).executeUpdate();
			
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.clear();
			}
		}
		
		
		public void copyAllinvoicesfromclg (Integer receiptID,Session aSession)throws CustomerException
		{
			try
			{
			List<CuLinkageDetail> theculgdtl=getcuLinkageDetailLst(receiptID);	
			for(CuLinkageDetail thecld:theculgdtl){
				CuReversePaymentsInv therevpaymentinv=new CuReversePaymentsInv();
				therevpaymentinv.setCuLinkageDetailId(thecld.getCuLinkageDetailId());
				therevpaymentinv.setCuInvoiceId(thecld.getCuInvoiceId());
				therevpaymentinv.setCuReceiptId(thecld.getCuReceiptId());
				therevpaymentinv.setRxCustomerId(thecld.getRxCustomerId());
				therevpaymentinv.setPaymentApplied(thecld.getPaymentApplied());
				therevpaymentinv.setDiscountUsed(thecld.getDiscountUsed());
				therevpaymentinv.setCreditAmount(thecld.getCreditAmount());
				therevpaymentinv.setRefInvoiceID(thecld.getRefInvoiceID());
				therevpaymentinv.setAmtApplied(thecld.getAmtApplied());
				therevpaymentinv.setStatusCheck(thecld.getStatusCheck());
				therevpaymentinv.setDatePaid(thecld.getDatePaid());
				addcuReversePayment(therevpaymentinv,aSession);
			}
			//aSession.createSQLQuery("INSERT INTO cuReversePaymentsInv (SELECT cuLinkageDetail.* FROM cuLinkageDetail WHERE cuReceiptID ="+receiptID+")").executeUpdate();
			
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.clear();
			}
		}
		
		public void deleteAllInvoicesfromclg(Integer receiptID,Session aSession)throws CustomerException
		{
			try
			{
				aSession.createSQLQuery("DELETE cgl FROM cuLinkageDetail cul,cuPaymentGlpost cgl WHERE cul.cuLinkageDetailID = cgl.cuLinkageDetailID AND cul.cuReceiptID = "+receiptID).executeUpdate();
			
				List<CuLinkageDetail> theculgdtl=getcuLinkageDetailLst(receiptID);	
				for(CuLinkageDetail thecld:theculgdtl){
					aSession.delete(thecld);
				}
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				CustomerException aCustomerException = new CustomerException(
						e.getMessage(), e);
				throw aCustomerException;
			} finally {
				aSession.flush();
				aSession.clear();
			}
		}

		@Override
		public String getAllCustomerEmail(int rCuID,int invORstmt) {
			
			Session aSession = null;
			Query getEmailList = null;
			StringBuilder selectBox = new StringBuilder();
			String query=null;
			try {
				aSession = itsSessionFactory.openSession();		
				if(invORstmt==0){
					query="SELECT  cuMaster.IM_Text, 1 AS DefaultEmail   FROM cuMaster WHERE cuMaster.cuMasterID='"
						+ rCuID
						+ "'AND  cuMaster.IM_Text IS NOT NULL AND cuMaster.IM_Text != ' 'UNION ALL SELECT  rxContact.EMail, 0 AS DefaultEmail  FROM rxContact WHERE  rxContact.rxMasterID ='"
						+ rCuID
						+ "'AND rxContact.EMail IS NOT NULL AND rxContact.EMail != ' '";
				}
				else if (invORstmt==1){
					query="SELECT  cuMaster.SM_Text, 1 AS DefaultEmail   FROM cuMaster WHERE cuMaster.cuMasterID='"
							+ rCuID
							+ "'AND  cuMaster.SM_Text IS NOT NULL AND cuMaster.SM_Text != ' 'UNION ALL SELECT  rxContact.EMail, 0 AS DefaultEmail  FROM rxContact WHERE  rxContact.rxMasterID ='"
							+ rCuID
							+ "'AND rxContact.EMail IS NOT NULL AND rxContact.EMail != ' '";
				}
				
				getEmailList= (Query) aSession.createSQLQuery(query);
				Iterator<?> emailListiterator = getEmailList.list().iterator();
				while (emailListiterator.hasNext()) {
					Object[] aobj = (Object[]) emailListiterator.next();
					if(aobj[1].toString().equalsIgnoreCase("1"))
					{  
						selectBox.append("<option value='"+aobj[0]+"'"+" selected >"+aobj[0]+"</option>" );
					}
					else
					{
					selectBox.append("<option value='"+aobj[0]+"'>"+aobj[0]+"</option>" );
					}

				}
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);

			} finally {
				aSession.flush();
				aSession.close();
			}
			
			return selectBox.toString();
			
		}
		
		public List<CuLinkageDetail> getcuLinkageDetailLst(Integer cuReciptID) throws JobException {
			Session aSession = null;
			List<CuLinkageDetail> aQueryList = null;
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createQuery("FROM  CuLinkageDetail where cuReceiptID="+cuReciptID);
				aQueryList = aQuery.list();
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aQueryList;
		}			
		public Boolean addcuReversePayment(CuReversePaymentsInv therevpayment,Session aSession) throws JobException {
			try {
				aSession.save(therevpayment);

			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} 
			return true;
		}
}
