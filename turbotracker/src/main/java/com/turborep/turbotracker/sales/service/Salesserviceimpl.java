/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.sales.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.dao.TpInventoryLog;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLogMaster;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.customer.dao.Cusotemplate;
import com.turborep.turbotracker.employee.dao.Ecstatement;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.UserBean;

/**
 * This Class is service implementation for SalesService.java.
 * 
 * @author vish_pepala
 */
@Service("salesServices")
@Transactional
public class Salesserviceimpl implements SalesService {

	protected static Logger itsLogger = Logger
			.getLogger(Salesserviceimpl.class);

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;

	@Resource(name = "productService")
	private ProductService productService;
	
	@Resource(name = "jobService")
	private JobService itsjobService;
	
	@Override
	public ArrayList<?> getSalesRep() {
		itsLogger.debug("Retrieving getSalesRep");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ " FROM tsUserLogin WHERE "
				+ " (Employee0 = 1 OR Employee1 = 1 OR Employee2 = 1 OR Employee3 = 1 OR Employee4 = 1 ) "
				+ " AND  inactive = 0  AND LoginName != 'admin' "
				+ " ORDER BY FullName ASC";
		Session aSession = null;
		ArrayList<UserBean> aQueryList = new ArrayList<UserBean>();
		try {
			UserBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new UserBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setUserId((Integer) aObj[0]);
				aUserbean.setUserName((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getsalesrepList(String theSalesRep) {
		itsLogger.debug("Retrieving getsalesrepList");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE Employee0 = 1 AND inactive = 0  AND LoginName != 'admin' "
				+ "AND FullName like " + "'%" + theSalesRep + "%'"
				+ " ORDER BY FullName ASC";
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
			if (aQueryList.isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setValue(" ");
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}

		return aQueryList;
	}

	@Override
	public ArrayList<?> getUpcoming(SalesRepBean theSalesRep) {
		itsLogger.debug("Retrieving getUpcoming");
		Integer aSalesRepId = theSalesRep.getSalesRepId();
		if (aSalesRepId == 0) {
			aSalesRepId = null;
		}
		Integer customerID=theSalesRep.getRxMasterID();
		if(customerID==0){
			customerID=null;
		}
		String aName = "";
		String aname2 = "";
		if (aSalesRepId!=null && aSalesRepId != -1) {
			aName = "WHERE (" + " joMaster.cuAssignmentID0 = " + aSalesRepId
					+ " OR " + " joMaster.cuAssignmentID1 = " + aSalesRepId
					+ " OR " + " joMaster.cuAssignmentID2 = " + aSalesRepId
					+ " OR " + " joMaster.cuAssignmentID3 = " + aSalesRepId
					+ " OR " + " joMaster.cuAssignmentID4 = " + aSalesRepId
					+ " OR " + " joMaster.cuAssignmentID5 = " + aSalesRepId
					+ " OR " + " joMaster.cuAssignmentID6 = " + aSalesRepId
					+ " ) AND joMaster.jobStatus in (0,-4,1,6) ";
			aname2 = "WHERE " + "(cu.cuAssignmentID0 = " + aSalesRepId + " OR "
					+ "cu.cuAssignmentID1 = " + aSalesRepId + " OR "
					+ "cu.cuAssignmentID2 = " + aSalesRepId + " OR "
					+ "cu.cuAssignmentID3 = " + aSalesRepId + " OR "
					+ "cu.cuAssignmentID4 = " + aSalesRepId
					+ ")) AND joMaster.jobStatus in (0,-4,1,6)";
		} else {
			aName = "WHERE joMaster.jobStatus in (0,-4,1,6) ";
			aname2 = "WHERE joMaster.jobStatus in (0,-4,1,6) )";
		}
		String aUpcomingQry = "SELECT DISTINCT BidDate, Description, JobNumber, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID0  AND LoginName != 'admin' ) AS asignedSales, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID1  AND LoginName != 'admin' ) AS asignedCust, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCustomerID ) AS allCust, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCategory1  ) AS architect, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCategory2 ) AS engineer, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCategory3 ) AS generalContract,joMaster.joMasterID  "
				+ " FROM joMaster "
				+ aName
				+ " AND BidDate >= CURDATE() ";
		if(customerID!=null){
			aUpcomingQry=aUpcomingQry+" AND joMaster.rxCustomerID="	+customerID;
		}
			
		aUpcomingQry=aUpcomingQry+ " UNION "
				+ "SELECT DISTINCT BidDate, Description, JobNumber, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID0  AND LoginName != 'admin' ) AS asignedSales, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID1  AND LoginName != 'admin' ) AS asignedCust, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCustomerID ) AS allCust, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCategory1  ) AS architect, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCategory2 ) AS engineer, "
				+ " (SELECT NAME FROM rxMaster WHERE rxMasterID = joMaster.rxCategory3 ) AS generalContract,joMaster.joMasterID  "
				+ " FROM joMaster  WHERE joMaster.joMasterId IN (SELECT jb.joMasterId FROM joBidder jb JOIN cuMaster cu ON cu.cuMasterId = jb.rxMasterId "
				+ " "
				+ aname2
				+ ""
				+ " AND BidDate >= CURDATE() ";
				
		if(customerID!=null){
			aUpcomingQry=aUpcomingQry+" AND joMaster.rxCustomerID="	+customerID;
		}
		
				if(theSalesRep.getSortcolumn()!=null && theSalesRep.getSortby()!=null){
					aUpcomingQry=aUpcomingQry+" ORDER BY "+theSalesRep.getSortcolumn()+" "+theSalesRep.getSortby();
				}else{
					aUpcomingQry=aUpcomingQry+" ORDER BY BidDate DESC ";
				}
				
				
		Session aSession = null;
		ArrayList<JobsBean> aComingQry = new ArrayList<JobsBean>();
		try {
			JobsBean aUpcomingJobs = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUpcomingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUpcomingJobs = new JobsBean();
				Object[] aObj = (Object[]) aIterator.next();
				if (aObj[0] != null) {
					aUpcomingJobs.setBidDate((String) DateFormatUtils.format(
							(Date) aObj[0], "MM/dd/yyyy"));
				}
				aUpcomingJobs.setJobName((String) aObj[1]);
				aUpcomingJobs.setJobNo((String) aObj[2]);
				if (aObj[3] != null) {
					aUpcomingJobs.setAssignedSalesman((String) aObj[3]
							.toString());
				}
				if (aObj[4] != null) {
					aUpcomingJobs.setAssignedCustomers((String) aObj[4]
							.toString());
				}
				if (aObj[5] != null) {
					aUpcomingJobs.setAllCustomer((String) aObj[5].toString());
				}
				if (aObj[6] != null) {
					aUpcomingJobs.setArchitect((String) aObj[6].toString());
				}
				if (aObj[7] != null) {
					aUpcomingJobs.setEngineer((String) aObj[7].toString());
				}
				if (aObj[8] != null) {
					aUpcomingJobs.setGeneralContractor((String) aObj[8]
							.toString());
				}
				aUpcomingJobs.setJoMasterId((Integer) aObj[9]);
				aComingQry.add(aUpcomingJobs);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aUpcomingQry = null;
		}
		return aComingQry;
	}

	public ArrayList<AutoCompleteBean> getCustomerFindProject(
			SalesRepBean theSalesRep) {
		itsLogger.debug("Retrieving getCustomerFindProject");
		Integer aSalesRepId = theSalesRep.getSalesRepId();
		if (aSalesRepId == 0) {
			aSalesRepId = null;
		}
		String aUpcomingQry = "SELECT DISTINCT BidDate, Description, JobNumber, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID0  AND LoginName != 'admin' ) AS asignedSales, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID1  AND LoginName != 'admin' ) AS asignedCust, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID2  AND LoginName != 'admin' ) AS allCust, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID3  AND LoginName != 'admin' ) AS architect, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID4  AND LoginName != 'admin' ) AS engineer, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID5  AND LoginName != 'admin' ) AS generalContract "
				+ " FROM joMaster WHERE "
				+ "(joMaster.cuAssignmentID0 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID1 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID2 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID3 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID4 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID5 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID6 = "
				+ aSalesRepId
				+ " ) AND joMaster.jobStatus = 0 "
				+ " AND BidDate >= CURDATE() ORDER BY joMaster.biddate DESC ";
		Session aSession = null;
		ArrayList<AutoCompleteBean> comingQry = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUpcomingJobs = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUpcomingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUpcomingJobs = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUpcomingJobs.setValue((String) aObj[1]);
				aUpcomingJobs.setLabel((String) aObj[1]);
				comingQry.add(aUpcomingJobs);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aUpcomingQry = null;
		}
		return comingQry;
	}

	
	
	@Override
	public ArrayList<JobsBean> getPending(SalesRepBean theSalesRep,Integer rxCustomerID) {
	itsLogger.debug("Retrieving getPending");
	Integer aSalesRepId = theSalesRep.getSalesRepId();
	if (aSalesRepId == 0) {
	aSalesRepId = null;
	}
	String aName = "";
	String aname2 = "";
	if(rxCustomerID!= null && rxCustomerID!=0){
		aname2 = " AND joBidder.rxMasterID = "+rxCustomerID;
	}
	if (aSalesRepId != -1) {
	aName = " WHERE jM.jobStatus = 1 AND BidDate < CURDATE() AND joQuoteHistory.joMasterID IS NULL "
			+ "AND (" + "jM.cuAssignmentID0 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID1 = " + theSalesRep.getSalesRepId()
	+ " OR " + "jM.cuAssignmentID2 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID3 = " + theSalesRep.getSalesRepId()
	+ " OR " + "jM.cuAssignmentID4 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID5 = " + theSalesRep.getSalesRepId()
	+ " OR " + "jM.cuAssignmentID6 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID6 = " + theSalesRep.getSalesRepId()
	+ ") "+aname2+" GROUP BY BidDate ORDER BY BidDate DESC";
	
	
	/*aname2 = " WHERE " + "(cu.cuAssignmentID0 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "cu.cuAssignmentID1 = " + theSalesRep.getSalesRepId()
	+ " OR " + "cu.cuAssignmentID2 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "cu.cuAssignmentID3 = " + theSalesRep.getSalesRepId()
	+ " OR " + "cu.cuAssignmentID4 = "
	+ theSalesRep.getSalesRepId() + ")) "
	+ "GROUP BY biddate ORDER BY biddate DESC";*/
	} else {
	aName = " WHERE jM.jobStatus = 1 AND BidDate < CURDATE() AND joQuoteHistory.joMasterID IS NULL "
			+aname2+ " GROUP BY BidDate ORDER BY BidDate DESC";
	/*aname2 = ") GROUP BY jM.biddate ORDER BY BidDate DESC";*/
	}
	String aPendingQry = "SELECT DISTINCT jM.biddate AS BidDate, "
			+ "jM.jobnumber, "
			+ "jM.description, "
			+ "rxMaster.name, "
			+ "SUM(joQuoteHeader.quoteamount),jM.joMasterID "
			+ "FROM joMaster jM "
			+ "JOIN joQuoteHeader ON jM.joMasterId=joQuoteHeader.joMasterId "
			+ "LEFT JOIN joQuoteHistory ON joQuoteHistory.joMasterID=jM.joMasterID "
			+ "LEFT JOIN joBidder ON joBidder.joMasterID=jM.joMasterID 	"
			+ "LEFT JOIN rxMaster ON rxMaster.rxMasterID=joBidder.rxMasterID "
			+ aName;
	
	Session aSession = null;
	ArrayList<JobsBean> aPendingQuotes = new ArrayList<JobsBean>();
	try {
	JobsBean aSalesPendingBean = null;
	aSession = itsSessionFactory.openSession();
	Query aQuery = aSession.createSQLQuery(aPendingQry);
	Iterator<?> aIterator = aQuery.list().iterator();
	while (aIterator.hasNext()) {
	aSalesPendingBean = new JobsBean();
	Object[] aObj = (Object[]) aIterator.next();
	if (aObj[0] != null) {
	aSalesPendingBean.setBidDate((String) DateFormatUtils
	.format((Date) aObj[0], "MM/dd/yyyy"));
	}
	aSalesPendingBean.setJobNo((String) aObj[1]);
	aSalesPendingBean.setJobName((String) aObj[2]);
	aSalesPendingBean.setAssignedCustomers((String) aObj[3]);
	aSalesPendingBean.setQuoteAmount((BigDecimal) aObj[4]);
	aSalesPendingBean.setJoMasterId((Integer) aObj[5]);
	aPendingQuotes.add(aSalesPendingBean);
	}
	} catch (Exception e) {
	itsLogger.error(e.getMessage(), e);
	} finally {
	aSession.flush();
	aSession.close();
	aPendingQry = null;
	}
	return aPendingQuotes;
	}
	
	@Override
	public ArrayList<JobsBean> getQuotedJobs(SalesRepBean theSalesRep, String sortBy , String sortOrder) {
	itsLogger.debug("Retrieving Quoted Jobs");
	Integer aSalesRepId = theSalesRep.getSalesRepId();
	if (aSalesRepId == 0) {
	aSalesRepId = null;
	}
	
	Integer customerID=theSalesRep.getRxMasterID();
	if(customerID==0){
		customerID=null;
	}
	
	String sortByData="BidDate";
	if(sortBy.equals("bidDate")){
		sortByData="BidDate";
	}
	if(sortBy.equals("jobName")){
		sortByData="BidDate";
	}
	if(sortBy.equals("jobNo")){
		sortByData="BidDate";
	}
	if(sortBy.equals("assignedCustomers")){
		sortByData="BidDate";
	}
	if(sortBy.equals("quoteAmount")){
		sortByData="BidDate";
	}
	   
	
	String aName = "";
	String aname2 = "";
	if (aSalesRepId != -1) {
	aName = " WHERE jM.jobStatus = 1 AND joBidder.LowBid <>1 AND BidDate < CURDATE() AND "
			+ "joBidder.QuoteDate = (SELECT MAX(job.QuoteDate) FROM joBidder job WHERE job.joMasterID=jM.joMasterID) "
			+ "AND (" + "jM.cuAssignmentID0 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID1 = " + theSalesRep.getSalesRepId()
	+ " OR " + "jM.cuAssignmentID2 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID3 = " + theSalesRep.getSalesRepId()
	+ " OR " + "jM.cuAssignmentID4 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID5 = " + theSalesRep.getSalesRepId()
	+ " OR " + "jM.cuAssignmentID6 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "jM.cuAssignmentID6 = " + theSalesRep.getSalesRepId()
	+ ")";
	if(customerID!=null){
		aName=aName+" AND jM.rxCustomerID="+customerID+" ";
	}
	aName=aName+ " GROUP BY biddate ORDER BY "+sortByData+" "+sortOrder.toUpperCase();
	aname2 = " WHERE " + "(cu.cuAssignmentID0 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "cu.cuAssignmentID1 = " + theSalesRep.getSalesRepId()
	+ " OR " + "cu.cuAssignmentID2 = "
	+ theSalesRep.getSalesRepId() + " OR "
	+ "cu.cuAssignmentID3 = " + theSalesRep.getSalesRepId()
	+ " OR " + "cu.cuAssignmentID4 = "
	+ theSalesRep.getSalesRepId() + ")) "
	+ "GROUP BY biddate ORDER BY "+sortByData+" "+sortOrder.toUpperCase();
	} else {
	aName = "WHERE jM.jobStatus = 1 AND joBidder.LowBid <>1 AND BidDate < CURDATE() AND joBidder.QuoteDate = (SELECT MAX(job.QuoteDate) FROM joBidder job WHERE job.joMasterID=jM.joMasterID) ";
			if(customerID!=null){
				aName=aName+" AND jM.rxCustomerID="+customerID+" ";
			}
			
			aName=aName+ "GROUP BY BidDate ORDER BY "+sortByData+" "+sortOrder.toUpperCase();
	aname2 = ") GROUP BY jM.biddate ORDER BY "+sortByData+" "+sortOrder.toUpperCase();
	}
	String aPendingQry = "SELECT DISTINCT " +
			"jM.biddate AS BidDate, " +
			"jM.jobnumber, " +
			"jM.description, " +
			"rxMaster.name, " +
			"SUM(joQuoteHeader.quoteamount),jM.joMasterID " +
			"FROM joMaster jM JOIN joQuoteHeader ON jM.joMasterId=joQuoteHeader.joMasterId " +
			"LEFT JOIN joBidder ON joBidder.joMasterID=jM.joMasterID " +
			"LEFT JOIN rxMaster ON rxMaster.rxMasterID=joBidder.rxMasterID " 	+ aName;
	
	/*String aPendingQry = "SELECT DISTINCT "
			+ "jM.biddate AS BidDate, "
			+ "jM.jobnumber, "
			+ "jM.description, "
			+ "IF(CONCAT(r.FirstName, ' ', r.LastName) IS NULL,(SELECT tsUserLogin.FullName FROM tsUserLogin WHERE joQuoteHeader.createdbyid=tsUserLogin.userloginid),CONCAT(r.FirstName, ' ', r.LastName)) AS FullNAme, "
			+ "SUM(joQuoteHeader.quoteamount),MAX(jb.joBidderID) FROM joMaster jM "
			+ "JOIN joQuoteHeader ON jM.joMasterId=joQuoteHeader.joMasterId "
			+ "LEFT JOIN joBidder jb ON jb.joMasterID = jM.joMasterId "
			+ "LEFT JOIN rxContact r ON jb.rxContactID = r.rxContactID "
			+ aName;
			//+ "  UNION "
			+ "SELECT DISTINCT "
			+ "jM.biddate AS BidDate, "
			+ "jM.jobnumber, "
			+ "jM.description, "
			+ "tsUserLogin.FullName, "
			+ "SUM(joQuoteHeader.quoteamount) FROM joMaster jM "
			+ "JOIN joQuoteHeader ON jM.joMasterId=joQuoteHeader.joMasterId JOIN tsUserLogin ON "
			+ "joQuoteHeader.createdbyid=tsUserLogin.userloginid WHERE jM.jobStatus = 1  AND tsUserLogin.LoginName != 'admin'  AND "
			+ "jM.joMasterId IN (SELECT jb.joMasterId FROM cuMaster cu JOIN joBidder jb ON "
			+ "cu.cuMasterId = jb.rxMasterId" + aname2 + "";*/
	
	Session aSession = null;
	ArrayList<JobsBean> aPendingQuotes = new ArrayList<JobsBean>();
	try {
	JobsBean aSalesPendingBean = null;
	aSession = itsSessionFactory.openSession();
	Query aQuery = aSession.createSQLQuery(aPendingQry);
	Iterator<?> aIterator = aQuery.list().iterator();
	while (aIterator.hasNext()) {
	aSalesPendingBean = new JobsBean();
	Object[] aObj = (Object[]) aIterator.next();
	if (aObj[0] != null) {
	aSalesPendingBean.setBidDate((String) DateFormatUtils
	.format((Date) aObj[0], "MM/dd/yyyy"));
	}
	aSalesPendingBean.setJobNo((String) aObj[1]);
	aSalesPendingBean.setJobName((String) aObj[2]);
	aSalesPendingBean.setAssignedCustomers((String) aObj[3]);
	aSalesPendingBean.setQuoteAmount((BigDecimal) aObj[4]);
	aSalesPendingBean.setJoMasterId((Integer) aObj[5]);
	aPendingQuotes.add(aSalesPendingBean);
	}
	} catch (Exception e) {
	itsLogger.error(e.getMessage(), e);
	} finally {
	aSession.flush();
	aSession.close();
	aPendingQry = null;
	}
	return aPendingQuotes;
	}

	@Override
	public ArrayList<?> getAwarded(SalesRepBean theSalesRep) {
		itsLogger.debug("Retrieving getAwarded");
		Integer aSalesRepId = theSalesRep.getSalesRepId();
		if (aSalesRepId == 0) {
			aSalesRepId = null;
		}
		Integer customerID=theSalesRep.getRxMasterID();
		if(customerID==0){
			customerID=null;
		}
		String aName = "";
		String aname2 = "";
		String awarded  = null;
		if (aSalesRepId != -1) {
			aName = " WHERE joBidder.LowBid = 1 AND "
					+ " joMaster.JobStatus = 1 AND "
					+ " (joMaster.cuAssignmentID0 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " joMaster.cuAssignmentID1 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " joMaster.cuAssignmentID2 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " joMaster.cuAssignmentID3 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " joMaster.cuAssignmentID4 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " joMaster.cuAssignmentID5 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " joMaster.cuAssignmentID6 = "
					+ theSalesRep.getSalesRepId() + ")";
			aname2 = " WHERE (cu.cuAssignmentID0 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " cu.cuAssignmentID1 = " + theSalesRep.getSalesRepId()
					+ " OR " + " cu.cuAssignmentID2 = "
					+ theSalesRep.getSalesRepId() + " OR "
					+ " cu.cuAssignmentID3 = " + theSalesRep.getSalesRepId()
					+ " OR " + " cu.cuAssignmentID4 = "
					+ theSalesRep.getSalesRepId()
					+ ")) ORDER BY JobNumber DESC";
		} else {
			aName = " WHERE joBidder.LowBid = 1 AND "
					+ " joMaster.JobStatus = 1  ";
			aname2 = ") ORDER BY JobNumber DESC";
		}
		awarded = "SELECT DISTINCT "
				+ " joMaster.JobNumber, "
				+ " joMaster.Description, "
				+ " joBidder.LowBid, "
				+ " joBidder.rxMasterId, "
				+ " rxMaster.Name AS Name, "
				+ " (SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterId = joMaster.rxCategory3) AS GC, "
				+ " (SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterId = joMaster.rxCategory2) AS Engineer, "
				+ " (SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterId = joMaster.rxCategory1) AS Architect,joMaster.joMasterID "
				+ " FROM joMaster JOIN joBidder ON joMaster.joMasterID = joBidder.joMasterId"
				+ " LEFT JOIN rxMaster ON joBidder.rxMasterId = rxMaster.rxMasterId "
				+ aName;
				if(customerID!=null){
					awarded=awarded+" AND joMaster.rxCustomerID="+customerID+" ";	
				}
				awarded=awarded+ " UNION SELECT DISTINCT "
				+ " joMaster.JobNumber, "
				+ " joMaster.Description, "
				+ " joBidder.LowBid, "
				+ " joBidder.rxMasterId, "
				+ " rxMaster.Name AS Name, "
				+ " (SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterId = joMaster.rxCategory3) AS GC, "
				+ " (SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterId = joMaster.rxCategory2) AS Engineer, "
				+ " (SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterId = joMaster.rxCategory1) AS Architect,joMaster.joMasterID "
				+ " FROM joMaster JOIN joBidder ON joMaster.joMasterID = joBidder.joMasterId"
				+ " LEFT JOIN rxMaster ON joBidder.rxMasterId = rxMaster.rxMasterId "
				+ " WHERE joBidder.LowBid = 1 AND "
				+ " joMaster.JobStatus = 1 AND ";
				if(customerID!=null){
					awarded=awarded+" joMaster.rxCustomerID="+customerID+" AND ";	
				}
				awarded=awarded+ " joMaster.joMasterId IN (SELECT jb.joMasterId FROM cuMaster cu JOIN joBidder jb ON "
				+ " cu.cuMasterId = jb.rxMasterId " + aname2;
				
		Session aSession = null;
		ArrayList<JobsBean> aAwardedQuotes = new ArrayList<JobsBean>();
		try {
			JobsBean aSalesBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(awarded);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aSalesBean = new JobsBean();
				Object[] aObj = (Object[]) aIterator.next();
				aSalesBean.setJobNo((String) aObj[0]);
				aSalesBean.setJobName((String) aObj[1]);
				aSalesBean.setLowbidder((String) aObj[4]);
				aSalesBean.setGeneralContractor((String) aObj[5]);
				aSalesBean.setEngineer((String) aObj[6]);
				aSalesBean.setArchitect((String) aObj[7]);
				aSalesBean.setJoMasterId((Integer) aObj[8]);
				aAwardedQuotes.add(aSalesBean);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aName = null;
			aname2 = null;
			awarded  = null;
		}
		return aAwardedQuotes;
	}

	@Override
	public String getAssignedEmployeeName(Integer theSalesRepId,
			String theTableName) {

		String salesselectQry = "SELECT FullName " + "FROM " + theTableName
				+ " WHERE " + "UserLoginId = '" + theSalesRepId
				+ "' AND LoginName != 'admin' AND Inactive='0' ORDER BY FullName ASC";
		Session aSession = null;
		String aSalesRep = "";
		Query aQuery = null;
		List<?> aList = null;
		itsLogger.info("theTablename==>" + salesselectQry);
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(salesselectQry);
			aList = aQuery.list();
			if (aList.size() > 0) {
				aSalesRep = (String) aList.get(0);
			}
			return aSalesRep;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
			aSalesRep = "";
			aQuery = null;
			aList = null;
		}
		return aSalesRep;
	}

	@Override
	public Integer getsingleproject(String theCreated) {
		itsLogger.debug("Retrieving getsingleproject");
		String aSalesselectQry = "SELECT joMasterID FROM joMaster WHERE Description like "
				+ "'%" + theCreated + "%'" + "";
		Session aSession = null;
		Integer aSalesRep = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			List<?> aList = aQuery.list();
			aSalesRep = (Integer) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}
		return aSalesRep;
	}

	@Override
	public ArrayList<JobsBean> upcomingForSingleCustomer(SalesRepBean thSalesRep) {
		itsLogger.debug("Retrieving upcomingForSingleCustomer");
		Integer aSalesRepId = thSalesRep.getSalesRepId();
		if (aSalesRepId == 0) {
			aSalesRepId = null;
		}
		String aUpcomingQry = "SELECT DISTINCT BidDate, Description, JobNumber, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID0 AND LoginName != 'admin') AS asignedSales, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID1 AND LoginName != 'admin') AS asignedCust, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID2 AND LoginName != 'admin') AS allCust, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID3 AND LoginName != 'admin') AS architect, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID4 AND LoginName != 'admin') AS engineer, "
				+ " (SELECT FullName FROM tsUserLogin Where userLoginId = joMaster.cuAssignmentID5 AND LoginName != 'admin') AS generalContract "
				+ " FROM joMaster WHERE "
				+ "(joMaster.cuAssignmentID0 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID1 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID2 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID3 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID4 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID5 = "
				+ aSalesRepId
				+ " OR "
				+ " joMaster.cuAssignmentID6 = "
				+ aSalesRepId
				+ " ) AND joMaster.jobStatus = 0 "
				+ " AND BidDate >= CURDATE() ORDER BY joMaster.biddate DESC ";
		Session aSession = null;
		ArrayList<JobsBean> aComingQry = new ArrayList<JobsBean>();
		try {
			JobsBean aUpcomingJobs = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUpcomingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUpcomingJobs = new JobsBean();
				Object[] aObj = (Object[]) aIterator.next();
				if (aObj[0] != null) {
					aUpcomingJobs.setBidDate((String) DateFormatUtils.format(
							(Date) aObj[0], "MM/dd/yyyy"));
				}
				aUpcomingJobs.setJobName((String) aObj[1]);
				aUpcomingJobs.setJobNo((String) aObj[2]);
				if (aObj[3] != null) {
					aUpcomingJobs.setAssignedSalesman((String) aObj[3]
							.toString());
				}
				if (aObj[4] != null) {
					aUpcomingJobs.setAssignedCustomers((String) aObj[4]
							.toString());
				}
				if (aObj[5] != null) {
					aUpcomingJobs.setAllCustomer((String) aObj[5].toString());
				}
				if (aObj[6] != null) {
					aUpcomingJobs.setArchitect((String) aObj[6].toString());
				}
				if (aObj[7] != null) {
					aUpcomingJobs.setEngineer((String) aObj[7].toString());
				}
				if (aObj[8] != null) {
					aUpcomingJobs.setGeneralContractor((String) aObj[8]
							.toString());
				}
				aComingQry.add(aUpcomingJobs);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aUpcomingQry = null;
		}
		return aComingQry;
	}

	@Override
	public String getAssignedEmployee(Integer theSalesRepId) {
		String salesselectQry = "SELECT FullName " + "FROM  tsUserLogin WHERE "
				+ "UserLoginId = '" + theSalesRepId
				+ "' AND LoginName != 'admin'";
		Session aSession = null;
		String aSalesRep = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			List<?> aList = aQuery.list();
			if (theSalesRepId != null && theSalesRepId != -1
					&& theSalesRepId != 0 && aList.size()>0)
				aSalesRep = (String) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
		}
		return aSalesRep;
	}

	/* To get Customer Name */
	@Override
	public ArrayList<AutoCompleteBean> getCustomerNameList(
			String theCustomerName) {
		itsLogger.debug("Retrieving getCustomerNameList");
		String aSalesselectQry = "SELECT rxmaster.rxMasterID,rxmaster.Name FROM cuMaster cumaster, rxMaster rxmaster "
				+ "WHERE cumaster.cuMasterID = rxmaster.rxMasterID AND rxmaster.Name IS NOT NULL AND rxmaster.Name LIKE "
				+ "'%" + theCustomerName + "%' AND rxmaster.IsCustomer=1 and rxmaster.InActive<>1 ORDER BY rxmaster.Name ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		Iterator<?> aIterator = null;
		Query aQuery = null;
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			aIterator = aQuery.list().iterator();
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
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
			aIterator = null;
			aQuery = null;
		}

		return aQueryList;
	}

	@Override
	public Integer updateSalesOrderStatus(Integer cuSOId, Integer status,Integer userID,String userName) {
		Session aSession = itsSessionFactory.openSession();
		Cuso cuso = null;
		Transaction aTransaction;
		Integer joMasterID=0;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			cuso = (Cuso) aSession.get(Cuso.class, cuSOId);
			
			Integer prevStatus = cuso.getTransactionStatus();
		/*	if((prevStatus==1 && status==-2)||(prevStatus==-2 && status==1)
					
					
					){
				
			}else{
				checkSalesOrderInvoice(cuSOId, status, prevStatus);
			}*/
			
			if((prevStatus==1 || prevStatus==-2||prevStatus==-1||prevStatus==0 ||prevStatus==2) && status!=3){
				RollBackSalesOrderLineItems(cuso,status,userID,userName);
			}
			
			if((prevStatus==1 || prevStatus==-2) && status==3){
				if(prevStatus==-2){
					RollBackSalesOrderLineItems(cuso,1,userID,userName);
				}
				JoRelease joReleases=InsertintoaNewJobasSalesOrder(cuso,status,userID,userName);
				if(joReleases.getJoReleaseId()!=null){
					cuso.setSonumber(cuso.getSonumber()+"A");
					cuso.setJoReleaseId(joReleases.getJoReleaseId());
					status=1;
					joMasterID=joReleases.getJoMasterId();
				}
			}
			cuso.setTransactionStatus(status);
			aSession.update(cuso);
			aTransaction.commit();
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return joMasterID;
	}

	public void checkSalesOrderInvoice(Integer cuSOId, Integer status,
			Integer prevStatus) {
		itsLogger.debug("checkSalesOrderInvoice");
		String aSalesselectQry = "SELECT * FROM cuInvoice WHERE cuSOID="
				+ cuSOId;
		Session aSession = null;

		try {

			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			// Iterator<?> aIterator = aQuery.list().iterator();
			if (aQuery.list().size() > 0) {
				itsLogger.debug("checkSalesOrder Invoiced");
			} else {

				updateInventoryOnSOStatus(cuSOId, status, prevStatus);
				itsLogger.debug("checkSalesOrder not invoice");
			}

		} catch (Exception e) {

			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}
	}

	public void updateInventoryOnSOStatus(Integer cuSOId, Integer status,
			Integer prevStatus) {
		itsLogger.debug("updateInventoryOnSOStatus");
		String aSalesselectQry = "SELECT prMasterID,QuantityOrdered FROM cuSODetail where cuSOID="
				+ cuSOId;
		Session aSession = null;
		Transaction aTransaction;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aTransaction.begin();
				Prmaster prMaster = (Prmaster) aSession.get(Prmaster.class,
						(Integer) aObj[0]);
				BigDecimal allocated = (BigDecimal) aObj[1];
				if (status == 1) {
					prMaster.setInventoryAllocated(prMaster
							.getInventoryAllocated().add(allocated));
				}
				if (status != 1 && prevStatus == 1) {
					prMaster.setInventoryAllocated(prMaster
							.getInventoryAllocated().subtract(allocated));
				}

				aSession.update(prMaster);
				aTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}
	}

	@Override
	public ArrayList<AutoCompleteBean> getSalesOrderList(String theSalesorder) {
		itsLogger.debug("Retrieving getsalesrepList");
		String aSalesselectQry = "SELECT so.cuSOID, CONCAT (so.SONumber,'  ', rm.Name) AS salesorder  FROM cuSO so LEFT JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID WHERE rm.Name LIKE '%"
				+ theSalesorder
				+ "%' OR so.SONumber LIKE '%"
				+ theSalesorder
				+ "%' ORDER BY so.cuSOID DESC ";
		Session aSession = null;
		System.out.println(aSalesselectQry);
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
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}

		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getDivisionlist(String divisionname) {
		itsLogger.debug("Retrieving getsalesrepList");
		String aSalesselectQry = "select coDivisionID,Description from coDivision where Description like '%"
				+ divisionname + "%'";
		Session aSession = null;
		System.out.println(aSalesselectQry);
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
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}

		return aQueryList;
	}

	@Override
	public Cuso updateEmailStampValue(Cuso theCuso) {
		Session aVepoSession = itsSessionFactory.openSession();
		Cuso aCuso = new Cuso();
		Transaction aTransaction;
		try {
			aTransaction = aVepoSession.beginTransaction();
			aCuso = (Cuso) aVepoSession.get(Cuso.class, theCuso.getCuSoid());
			aCuso.setEmailTimeStamp(theCuso.getEmailTimeStamp());
			aCuso.setCuSoid(theCuso.getCuSoid());
			aVepoSession.update(aCuso);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aVepoSession.flush();
			aVepoSession.close();
		}
		return aCuso;
	}

	@Override
	public List<Cusotemplate> getCusoTemplateList() {
		Session aSession = null;
		List<Cusotemplate> aQueryList = new ArrayList<Cusotemplate>();
		String aCustomerQry = " Select TemplateDescription,cuSOID from cuSOTemplate ORDER BY TemplateDescription";

		Cusotemplate aRolodexBean = null;
		try {

			aSession = itsSessionFactory.openSession();

			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new Cusotemplate();
				Object[] aObj = (Object[]) aIterator.next();
				aRolodexBean.setTemplateDescription((String)aObj[0]);
				aRolodexBean.setCuSoid((Integer)aObj[1]);
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry = null;
		}
		return aQueryList;
	}
	
	/*
	 * 
	 * (non-Javadoc)
	 * @see com.turborep.turbotracker.sales.service.SalesService#getSOTemplatePriceDetails(int, int)
	 * Created by : Praveenkumar
	 * date : 2014-09-16
	 * Purpose : Calculate cost for SO line item details
	 */
	@Override
	public Map<String,BigDecimal> getTemplatePriceDetails(int CusoID,int cuSODetailID,int prMasterID){
		Session aSession = null;
		BigDecimal margin = new BigDecimal(0);
		
		BigDecimal cost = new BigDecimal(0);
		Map<String,BigDecimal> aPrice = new HashMap<String, BigDecimal>();
		String Query = null;
		try {
			itsLogger.info("cuSODetailID::"+cuSODetailID+"::CusoID::"+CusoID);
			Query = "SELECT prMaster.prMasterID,prMaster.SalesPrice00,prMaster.LastCost,template.QuantityOrdered,template.cuSODetailID,IFNULL(template.whseCost,0.0000) FROM cuSODetail AS template,prMaster WHERE template.cuSOID= :cuSoid AND prMaster.prMasterID = template.prMasterID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(Query);
			aQuery.setParameter("cuSoid", CusoID);
			List aList = aQuery.list();
			if (!aList.isEmpty()) {
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					//BigDecimal salesPrice00 = (BigDecimal)aObj[1] == null ? new BigDecimal(0.00) : (BigDecimal)aObj[1];
					//BigDecimal lastCost = (BigDecimal)aObj[2]== null ? new BigDecimal(0.00) : (BigDecimal)aObj[2];
					BigDecimal qty = (BigDecimal)aObj[3]== null ? new BigDecimal(0.00) : (BigDecimal)aObj[3];
					//margin = margin.add(salesPrice00.multiply(qty));
					BigDecimal whseCost = BigDecimal.ZERO;
					if(cuSODetailID == (Integer)aObj[4]){
						aPrice.put("product", itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));
						aPrice.put("productnqty", itsInventoryService.getWarehouseCost((Integer)aObj[0]));
					}
					cost = cost.add(itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));
					//itsLogger.info("Quantityz::"+qty);
					
					/*Commented By Velmurugan
					 * Date:17-7-2015
					 * Issue:BID #622
					 * 
					 * if(cuSODetailID == (Integer)aObj[4]){
						if(whseCost.compareTo(new BigDecimal("0.0000"))==0){
						aPrice.put("product", itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));
						whseCost = new BigDecimal("0.0000");
						}
						else{
						aPrice.put("product", whseCost);
						}
					}
					
					if(whseCost.compareTo(new BigDecimal("0.0000"))==0){
					cost = cost.add(itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));
					}else{
						cost = cost.add(whseCost);
					}*/
					
					//cost = cost.add(itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));	
				}				
				//BigDecimal percentage = margin.subtract(cost).divide(margin).multiply(new BigDecimal(100));
				
				
				aPrice.put("cost", cost);
				aPrice.put("percentage",  new BigDecimal(0.00));
			}
			else
			{				
				aPrice.put("productnqty", new BigDecimal(0.00));
				aPrice.put("product", new BigDecimal(0.00));
				aPrice.put("margin", new BigDecimal(0.00));
				aPrice.put("cost", new BigDecimal(0.00));
				aPrice.put("percentage", new BigDecimal(0.00));
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			
		} finally {
			aSession.flush();
			aSession.close();
			Query = null;
		}
		return aPrice;
	}
	
	public ArrayList<String > getsalesordershiptoAddressDetails(Cuso cusolist) {
		itsLogger.info("Address Details");
		String name=" ";
		String address1=" ";
		String address2=" ";
		String City =" ";
		String State=" ";
		String Zip=" ";
		String aUpcomingQry = "";
		if(cusolist.getShipToMode()==0){
			itsLogger.info("Pick Up - WareHouse Address");
			aUpcomingQry="SELECT prw.description,prw.address1,prw.address2,prw.city,prw.state,prw.zip FROM prWarehouse prw WHERE prWarehouseID="+cusolist.getPrToWarehouseId();
		}else if(cusolist.getShipToMode()==1){
			itsLogger.info("Customer - rxMaster Address");
			aUpcomingQry="SELECT rxmaster.Name,Address1,Address2,City,State,Zip FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = "+cusolist.getRxCustomerId()+" and address.rxAddressID = "+cusolist.getRxShipToId()+" ORDER BY rxAddressID asc";
		}else if(cusolist.getShipToMode()==3){
			itsLogger.info("Other - rxAddress Address");
			aUpcomingQry="SELECT NAME,address1,address2,city,state,zip FROM rxAddress WHERE rxAddressID="+cusolist.getRxShipToAddressId();
		}else if(cusolist.getShipToMode()==2){
			itsLogger.info("Job Site - joMaster Address");
			if(cusolist.getJoReleaseId()!=null)
			aUpcomingQry="SELECT rxMaster.Name,LocationAddress1,LocationAddress2,LocationCity,LocationState,LocationZip FROM joMaster JOIN joRelease ON(joRelease.joMasterID=joMaster.joMasterID) JOIN rxMaster ON(joMaster.rxCustomerID=rxMaster.rxMasterID) WHERE joReleaseID="+cusolist.getJoReleaseId();
			else
				aUpcomingQry="SELECT rxmaster.Name,'','','','','' FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = "+cusolist.getRxCustomerId()+" and address.rxAddressID = "+cusolist.getRxShipToId()+" ORDER BY rxAddressID asc";
		}
		Session aSession = null;
		ArrayList<String> AddressQry = new ArrayList<String>();
		try {
			System.out.println("insid==="+aUpcomingQry);
			AutoCompleteBean aUpcomingJobs = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUpcomingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				
				if(aObj[0]!=null){
					name=(String) aObj[0];
				}
				if(aObj[1]!=null){
					address1=(String) aObj[1];
				}
				if(aObj[2]!=null){
					address2=(String) aObj[2];
				}
				if(aObj[3]!=null){
					City=(String) aObj[3];
				}
				if(aObj[4]!=null){
					State=(String) aObj[4];
				}
				if(aObj[5]!=null){
					Zip=(String) aObj[5];
				}
				AddressQry.add(name);
				AddressQry.add(address1);
				AddressQry.add(address2);
				AddressQry.add(City);
				AddressQry.add(State);
				AddressQry.add(Zip);
			}else{
				
				AddressQry.add(name);
				AddressQry.add(address1);
				AddressQry.add(address2);
				AddressQry.add(City);
				AddressQry.add(State);
				AddressQry.add(Zip);
			}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aUpcomingQry = null;
		}
		return AddressQry;
	}
	
	@Override
	public String getCommissionPaidDetails(Integer cuInvoiceID){
		String esSQuery = "SELECT (InvoiceAmount-AppliedAmount) FROM cuInvoice WHERE cuInvoiceID ="+cuInvoiceID;
		Session aSession = null;
		String message = "";
		Integer ecInvoicePeriodID =0;
		String culQuery ,cipQuery,cirQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(esSQuery);
			List<?> aList = aQuery.list();
			if(((BigDecimal) aList.get(0)).compareTo(new BigDecimal("0.0000"))>0){
				message = "YY";
			}
			else{
				culQuery =" SELECT R.ReceiptDate FROM cuLinkageDetail AS L INNER JOIN cuReceipt AS R ON "
						+ "L.cuReceiptID = R.cuReceiptID WHERE L.cuInvoiceID = "+cuInvoiceID+" ORDER BY R.ReceiptDate DESC LIMIT 1";
				Query bQuery = aSession.createSQLQuery(culQuery);
				List<?> bList = bQuery.list();
				if(bList.size()>0){
					message = "Invoice is paid as of : "+(Date) bList.get(0)+".";
				}
				else{
					message = "NN";
				}
			}
				
				String cuIQuery =" SELECT * FROM ecInvoicePeriod WHERE cuInvoiceID = "+cuInvoiceID+"";
				Query cQuery = aSession.createSQLQuery(cuIQuery);
				if(cQuery.list().size()>0){
						cipQuery ="select ecInvoicePeriod.ecInvoicePeriodID, ecPeriod.PeriodEndingDate FROM "
								+ "ecInvoicePeriod LEFT OUTER JOIN ecPeriod ON ecInvoicePeriod.ecPeriodID = ecPeriod.ecPeriodID "
								+ "Where cuInvoiceID = "+cuInvoiceID+" ORDER BY ecPeriod.PeriodEndingDate DESC LIMIT 1";
						Query dQuery = aSession.createSQLQuery(cipQuery);
						Iterator<?> dIterator = dQuery.list().iterator();
						if (dIterator.hasNext()) {
							Object[] aObj = (Object[]) dIterator.next();
							itsLogger.info("Commission-1ecInvoicePeriodID::"+(Integer) aObj[0]);
							ecInvoicePeriodID = (Integer) aObj[0];
							message = message+"# CP@"+(Date) aObj[1];
						}else{
							ecInvoicePeriodID = 0;
							message = message+"# CP ";
						}
						
					
						cirQuery ="SELECT U.FullName FROM ecInvoiceRepSplit AS RS LEFT OUTER JOIN ecStatement "
								+ "AS S ON RS.ecStatementID = S.ecStatementID LEFT OUTER JOIN tsUserLogin AS U "
								+ "ON S.RepLoginID = U.UserLoginID LEFT OUTER JOIN ecInvoicePeriod AS P ON "
								+ "RS.ecInvoicePeriodID = P.ecInvoicePeriodID Where P.ecInvoicePeriodID=" +ecInvoicePeriodID;
						Query eQuery = aSession.createSQLQuery(cirQuery);
						List<?> eList = eQuery.list();
						if(eList.size()>0){
							message = message+"@"+(String) eList.get(0)+".";
						}
				}
				else{
					message = message+"# CPN";
				}
			itsLogger.info("Commission-1Message::"+message);
			
		}
		catch(Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			cirQuery = null;
			culQuery = null;
			cipQuery = null;
		}
		
		return message;
	}
	@Override
	public List<Ecstatement> getJobCommissionSplits(Integer joReleaseDetailID) {
		List<Ecstatement> aCustomerName = new ArrayList<Ecstatement>();
		String esSQuery = "SELECT ecs.ecStatementID,ecs.ecPeriodID,ecs.RepLoginID,ecs.CalculatedDate,rxm.FullName FROM ecStatement ecs "
				+ "JOIN tsUserLogin rxm ON ecs.RepLoginID = rxm.UserLoginID "
				+ " LEFT JOIN ecJobs ej ON ej.ecStatementID = ecs.ecStatementID "
				+ " WHERE ej.joMasterID = (SELECT jrd.joMasterID FROM joReleaseDetail jrd WHERE jrd.joReleaseDetailID ="+joReleaseDetailID+" ) GROUP BY rxm.FullName ";
		Session aSession = null;
		Ecstatement aEcstatement = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(esSQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEcstatement = new Ecstatement();
				Object[] aObj = (Object[]) aIterator.next();
				aEcstatement.setEcStatementId((Integer) aObj[0]);
				aEcstatement.setEcPeriodId((Integer) aObj[1]);
				aEcstatement.setRepLoginId((Integer) aObj[2]);
				java.sql.Timestamp aDate = null;
				aDate = (java.sql.Timestamp) aObj[3];
				String dateString = new SimpleDateFormat("MM/dd/yyyy").format(aDate);
				aEcstatement.setCalculatedDate(dateString);
				aEcstatement.setRepName((String) aObj[4]);
				aCustomerName.add(aEcstatement);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			esSQuery = null;
		}
		return aCustomerName;
	}
	
	@Override
	public JoReleaseDetail getJoReleaseDetails(Integer joReleaseDetailID) {
		Session aVepoSession = itsSessionFactory.openSession();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		try {
			aJoReleaseDetail = (JoReleaseDetail) aVepoSession.get(JoReleaseDetail.class, joReleaseDetailID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aVepoSession.flush();
			aVepoSession.close();
		}
		return aJoReleaseDetail;
	}
	
	
	@Override
	public Date getInvoicePaymentDetails(Integer cuInvoiceID) {
		String salesselectQry = "SELECT ReceiptDate FROM cuReceipt WHERE cuReceiptID in "
				+ "(SELECT Distinct cuReceiptID FROM cuLinkageDetail WHERE cuInvoiceID = "+cuInvoiceID+")";
		Session aSession = null;
		Date paymentDate = null;
		itsLogger.info("Query: " + salesselectQry);
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				paymentDate = (Date) aList.get(0);
			}
			return paymentDate;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
		}
		return paymentDate;
	
	}
	
	@Override
	public String getPaidCheckRefs(Integer cuInvoiceID) {

		String salesselectQry = "SELECT IF(reference IS NULL,'$$',reference) as refs FROM cuReceipt WHERE cuReceiptID in "
				+ "(SELECT Distinct cuReceiptID FROM cuLinkageDetail WHERE cuInvoiceID = "+cuInvoiceID+") ORDER BY cuReceiptID DESC";
		Session aSession = null;
		String aSalesRep = "";
		itsLogger.info("Query: " + salesselectQry);
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				aSalesRep = (String) aList.get(0);
			}
			return aSalesRep;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
		}
		return aSalesRep;
	}
	
	public ArrayList<String > getsalesorder_soldtoAddressDetails(Cuso cusolist) {
		itsLogger.debug("Retrieving getCustomerFindProject");
		String name=" ";
		String address1=" ";
		String address2=" ";
		String City =" ";
		String State=" ";
		String Zip=" ";
		String aUpcomingQry="SELECT rxmaster.Name,Address1,Address2,City,State,Zip FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID ="+cusolist.getRxBillToId();
		Session aSession = null;
		ArrayList<String> AddressQry = new ArrayList<String>();
		if(aUpcomingQry!=null && aUpcomingQry!=""){
		try {
			AutoCompleteBean aUpcomingJobs = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUpcomingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				
				if(aObj[0]!=null){
					name=(String) aObj[0];
				}
				if(aObj[1]!=null){
					address1=(String) aObj[1];
				}
				if(aObj[2]!=null){
					address2=(String) aObj[2];
				}
				if(aObj[3]!=null){
					City=(String) aObj[3];
				}
				if(aObj[4]!=null){
					State=(String) aObj[4];
				}
				if(aObj[5]!=null){
					Zip=(String) aObj[5];
				}
				AddressQry.add(name);
				AddressQry.add(address1);
				AddressQry.add(address2);
				AddressQry.add(City);
				AddressQry.add(State);
				AddressQry.add(Zip);
				
				itsLogger.info("Name:"+name);
			}
			else {
				aUpcomingQry="SELECT name,Address1,Address2,City,State,Zip FROM rxAddress WHERE rxMasterID="+cusolist.getRxBillToId();
				aSession = itsSessionFactory.openSession();
				 aQuery = aSession.createSQLQuery(aUpcomingQry);
				 aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					
					if(aObj[0]!=null){
						name=(String) aObj[0];
					}
					if(aObj[1]!=null){
						address1=(String) aObj[1];
					}
					if(aObj[2]!=null){
						address2=(String) aObj[2];
					}
					if(aObj[3]!=null){
						City=(String) aObj[3];
					}
					if(aObj[4]!=null){
						State=(String) aObj[4];
					}
					if(aObj[5]!=null){
						Zip=(String) aObj[5];
					}
					AddressQry.add(name);
					AddressQry.add(address1);
					AddressQry.add(address2);
					AddressQry.add(City);
					AddressQry.add(State);
					AddressQry.add(Zip);
					
					
				}else{
					
					AddressQry.add(name);
					AddressQry.add(address1);
					AddressQry.add(address2);
					AddressQry.add(City);
					AddressQry.add(State);
					AddressQry.add(Zip);
				}
			}

			
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aUpcomingQry = null;
		}
		}	
		return AddressQry;
	}

	public SysUserDefault getSysUserDefault(int userid)  {
		String	aSysPriSelectQry = "SELECT SysUserDefaultID,UserLoginID,WarehouseID,coDivisionID FROM SysUserDefault WHERE UserLoginID="+userid;
		Session aSession=null;
		SysUserDefault aSysUserDefault=aSysUserDefault = new SysUserDefault();;
		int i=0;
		try{
			Sysprivilege aSysprivilege = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			
			if(aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				aSysUserDefault.setSysUserDefaultID((Integer) aObj[0]);
				aSysUserDefault.setUserLoginID((Integer) aObj[1]);
				aSysUserDefault.setWarehouseID((Integer) aObj[2]);
				aSysUserDefault.setCoDivisionID((Integer) aObj[3]);
				
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
		}
		return aSysUserDefault;
	}

	@Override
	public Prmaster getInventoryAllocatedDetailsservice(int cuSOid,int prmasterid) {
		Session aSession=null;
		Integer sPrWarehouseID = 0;
		BigDecimal inventoryAllocated =new BigDecimal(0);
		BigDecimal inventory1 =new BigDecimal(0);
		BigDecimal inventory2 =new BigDecimal(0);
		String sQuery , sQuery1 =null;
		Prmaster objprmaster=new Prmaster();
		try{
			aSession=itsSessionFactory.openSession();
			sQuery = "SELECT prFromWarehouseID FROM cuSO cuso WHERE cuSOID = "
					+ cuSOid;
			Query query = aSession.createSQLQuery(sQuery);
			if (query.list().size() > 0)
				sPrWarehouseID = (Integer) query.list().get(0);

			sQuery1 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID,prMaster.IsInventory FROM prWarehouseInventory inventory left join prMaster on(prMaster.prMasterID=inventory.prMasterID) WHERE inventory.prMasterID = "
					+ prmasterid
					+ " AND prWarehouseID = "
					+ sPrWarehouseID;
			query = aSession.createSQLQuery(sQuery1);
			if (query.list().size() > 0) {
				Object[] object = (Object[]) query.list().get(0);
				inventory1 = (BigDecimal) object[0];
				
				inventory2 =  (BigDecimal) object[1];
				
				inventoryAllocated =(inventory2==null?new BigDecimal("0.0000").subtract(inventory1==null?new BigDecimal("0.0000"):inventory1):inventory2.subtract(inventory1==null?new BigDecimal("0.0000"):inventory1));
				objprmaster.setInventoryAllocated(inventoryAllocated);
				objprmaster.setIsInventory((Byte) object[3]);
				System.out.println("-=================================================>"+inventoryAllocated);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			sQuery1 =null;
			sQuery = null;
		}
		return objprmaster;
	}	
	
	@Override
	public boolean updatesetBillOnlyStatus(Integer releaseID, Integer status) {
		Session aSession = itsSessionFactory.openSession();
		JoRelease ajorel= null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			ajorel = (JoRelease) aSession.get(JoRelease.class, releaseID);
			ajorel.setRelease_status(status);
			aSession.update(ajorel);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public Integer getQuotedPricePrMasterID() {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String query1 = null;
		Integer prMasterIDforQP=null;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			query1  =  "SELECT prMasterID FROM prMaster WHERE ItemCode='QP'";
			prMasterIDforQP = (Integer) aSession.createSQLQuery(query1).uniqueResult();
			
			/*query2  =  "SELECT joReleaseID FROM cuSO WHERE cuSOID="+cuSOID;
			Integer joReleaseID = (Integer) aSession.createSQLQuery(query2).uniqueResult();
			JoRelease joRelease = (JoRelease) aSession.get(JoRelease.class,joReleaseID);
			
			query3 = "update cuSODetail set UnitCost = 0.0000 where cuSOID="+cuSOID;
			aSession.createSQLQuery(query3).executeUpdate();
			
			if(joRelease.getEstimatedBilling()!=null)
				unitCostValue = joRelease.getEstimatedBilling();
			
			boolean checkQPthereornotinsalesorder=getcusoqpthereornotinsalesorder(cuSOID,prMasterIDforQP);
			if(checkQPthereornotinsalesorder){
				query4 = "update cuSODetail set UnitCost ="+unitCostValue+",QuantityOrdered=1,PriceMultiplier=1,Description='Quoted Price' where cuSOID="+cuSOID+" and prMasterID="+prMasterIDforQP;
				aSession.createSQLQuery(query4).executeUpdate();
				//no need to insert one more quoted price if already there na
			}else{
				query4 = "INSERT INTO cuSODetail(cuSOID,prMasterID, Description,QuantityOrdered,UnitCost,PriceMultiplier,Taxable,joSchedDetailID,HasSingleItemTaxAmount,UnitPrice,QuantityBilled,QuantityReceived ) VALUES("
										+ cuSOID+ ","+ prMasterIDforQP+",'Quoted Price',1,"+ unitCostValue+ ","+ "1,0,0,0,0,0,0);";
			aSession.createSQLQuery(query4).executeUpdate();
			}*/
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
			query1 = null;
			
		}
		return prMasterIDforQP;
	}
	
	@Override
	public boolean updateQuotedPriceIncuSoDetails(Integer cuSOID) {
		Session aSession = itsSessionFactory.openSession();
		JoRelease ajorel= null;
		Transaction aTransaction;
		String query1,query2,query3,query4,query5 = null;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			query1  =  "SELECT prMasterID FROM prMaster WHERE ItemCode='QP'";
			Integer prMasterIDforQP = (Integer) aSession.createSQLQuery(query1).uniqueResult();
			
			query2  =  "SELECT joReleaseID FROM cuSO WHERE cuSOID="+cuSOID;
			Integer joReleaseID = (Integer) aSession.createSQLQuery(query2).uniqueResult();
			JoRelease joRelease = (JoRelease) aSession.get(JoRelease.class,joReleaseID);
			
			query5  =  "SELECT SUM(TRUNCATE(TRUNCATE(`QuantityOrdered`,2)*TRUNCATE(`UnitCost`,2)*IF(IFNULL(`PriceMultiplier`, 0)=0,1,`PriceMultiplier`),2)) FROM `cuSODetail` WHERE cuSOID="+cuSOID;
			BigDecimal quotedAmount = (BigDecimal) aSession.createSQLQuery(query5).uniqueResult();
			
			query3 = "update cuSODetail set UnitCost = 0.0000 where cuSOID="+cuSOID;
			aSession.createSQLQuery(query3).executeUpdate();
			boolean checkQPthereornotinsalesorder=getcusoqpthereornotinsalesorder(cuSOID,prMasterIDforQP);
			if(quotedAmount==null) {
				quotedAmount=BigDecimal.ZERO;
			}
			
			if(checkQPthereornotinsalesorder){
				query4 = "update cuSODetail set UnitCost = "+quotedAmount+",QuantityOrdered=1,PriceMultiplier=1,Description='Quoted Price' where cuSOID="+cuSOID+" and prMasterID="+prMasterIDforQP;
				aSession.createSQLQuery(query4).executeUpdate();
				//no need to insert one more quoted price if already there na
			}else{
				query4 = "INSERT INTO cuSODetail(cuSOID,prMasterID, Description,QuantityOrdered,UnitCost,PriceMultiplier,Taxable,joSchedDetailID,HasSingleItemTaxAmount,UnitPrice,QuantityBilled,QuantityReceived ) VALUES("
						+ cuSOID+ ","+ prMasterIDforQP+",'Quoted Price',1,"+quotedAmount+",1,0,0,0,0,0,0);";
				aSession.createSQLQuery(query4).executeUpdate();
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
			query1 = null;
			query2 = null;
			query3 = null;
			query4 = null;
			query5=null;
		}
		return true;
	}
	
	public boolean getcusoqpthereornotinsalesorder(Integer cuSOId, Integer quotepriceprmasterid) {
		itsLogger.debug("updateInventoryOnSOStatus");
		String aSalesselectQry = "SELECT * FROM cuSODetail where cuSOID="+ cuSOId+" and prMasterID="+quotepriceprmasterid;
		Session aSession = null;
		boolean returnvalue=false;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				returnvalue=true;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return returnvalue;
	}
	public void RollBackSalesOrderLineItems(Cuso cuso,Integer newstatus,Integer userID,String userName){
		Session aSession = null;
		boolean returnvalue=false;
		try {
			aSession = itsSessionFactory.openSession();
		String prwarehouseQuery="FROM Cusodetail WHERE cuSOID ="+cuso.getCuSoid();
		Query aQuery = aSession.createQuery(prwarehouseQuery);
			if(cuso.getTransactionStatus()==1 && (newstatus==-2 || newstatus==-1 ||newstatus==0 || newstatus==2)){
				String transstatus="";
				if(newstatus==-2){
					transstatus="Quote";
				}else if(newstatus==-1){
					transstatus="Void";
				}else if(newstatus==0){
					transstatus="onHold";
				}else if(newstatus==2){
					transstatus="Closed";
				}
				ArrayList<Cusodetail> cusodtllist = (ArrayList<Cusodetail>) aQuery.list();
				for(Cusodetail aCusodetail:cusodtllist){
					aCusodetail.setUserID(userID);
					aCusodetail.setUserName(userName);
					itsjobService.RollBackPrMasterPrWareHouseInventory(aCusodetail.getCuSoid(),aCusodetail.getCuSodetailId());
					if(aCusodetail.getCuSodetailId()>0){
												Cuso aCuso=(Cuso) aSession.get(Cuso.class, aCusodetail.getCuSoid());
												TpInventoryLog aTpInventoryLog = new TpInventoryLog();
												
												Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aCusodetail.getPrMasterId());
												aTpInventoryLog.setPrMasterID(aCusodetail.getPrMasterId());
												aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
												aTpInventoryLog.setWareHouseID(aCuso.getPrFromWarehouseId());
												aTpInventoryLog.setTransType("SO");
												aTpInventoryLog.setTransDecription("SO Status Open to "+transstatus);
												aTpInventoryLog.setTransID(aCusodetail.getCuSoid());
												aTpInventoryLog.setTransDetailID(aCusodetail.getCuSodetailId());
												aTpInventoryLog.setProductOut(aCusodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?aCusodetail.getQuantityOrdered():new BigDecimal("0.0000"));
												aTpInventoryLog.setProductIn(aCusodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?aCusodetail.getQuantityOrdered():new BigDecimal("0.0000"));
												aTpInventoryLog.setUserID(aCuso.getCreatedById());
												aTpInventoryLog.setCreatedOn(new Date());
												itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
												/*TpInventoryLogMaster
												 * Created on 04-12-2015
												 * Code Starts
												 * RollBack
												 * */
												BigDecimal oqo=aCusodetail.getQuantityOrdered().subtract(aCusodetail.getQuantityBilled());
												Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuso.getPrFromWarehouseId());
												Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuso.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
												TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
														aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aCuso.getPrFromWarehouseId() ,otheprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),otheprwarehsinventory.getInventoryOnHand(),
														oqo.multiply(new BigDecimal(-1)),BigDecimal.ZERO,"SO",aCuso.getCuSoid(),aCusodetail.getCuSodetailId(),aCuso.getSonumber(),null ,
										/*Product out*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
										/*Product in*/(oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO ,
														"SO Status Open to "+transstatus,aCusodetail.getUserID(),aCusodetail.getUserName(),
														new Date());
												itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
												/*Code Ends*/
											}
				}
			}else if((cuso.getTransactionStatus()==-2 || cuso.getTransactionStatus()==-1 ||cuso.getTransactionStatus()==0 || cuso.getTransactionStatus()==2)&& newstatus==1){
				String transstatus="";
				if(cuso.getTransactionStatus()==-2){
					transstatus="Quote";
				}else if(cuso.getTransactionStatus()==-1){
					transstatus="Void";
				}else if(cuso.getTransactionStatus()==0){
					transstatus="onHold";
				}else if(newstatus==2){
					transstatus="Closed";
				}
				ArrayList<Cusodetail> cusodtllist = (ArrayList<Cusodetail>) aQuery.list();
				for(Cusodetail aCusodetail:cusodtllist){
					aCusodetail.setUserID(userID);
					aCusodetail.setUserName(userName);
					itsjobService.insertPrMasterPrWareHouseInventory(aCusodetail.getCuSoid(),aCusodetail.getCuSodetailId());
					if(aCusodetail.getCuSodetailId()>0){
						Cuso aCuso=(Cuso) aSession.get(Cuso.class, aCusodetail.getCuSoid());
						TpInventoryLog aTpInventoryLog = new TpInventoryLog();
						aTpInventoryLog.setPrMasterID(aCusodetail.getPrMasterId());
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aCusodetail.getPrMasterId());
						aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						aTpInventoryLog.setWareHouseID(aCuso.getPrFromWarehouseId());
						aTpInventoryLog.setTransType("SO");
						aTpInventoryLog.setTransDecription("SO Status "+transstatus+" to OPen");
						aTpInventoryLog.setTransID(aCusodetail.getCuSoid());
						aTpInventoryLog.setTransDetailID(aCusodetail.getCuSodetailId());
						aTpInventoryLog.setProductOut(aCusodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?aCusodetail.getQuantityOrdered():new BigDecimal("0.0000"));
						aTpInventoryLog.setProductIn(aCusodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?aCusodetail.getQuantityOrdered():new BigDecimal("0.0000"));
						aTpInventoryLog.setUserID(aCuso.getCreatedById());
						aTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * */
						BigDecimal qo=aCusodetail.getQuantityOrdered().subtract(aCusodetail.getQuantityBilled());
						Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuso.getPrFromWarehouseId());
						Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuso.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
						TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
								aPrmaster.getPrMasterId(),
								aPrmaster.getItemCode(),
								aCuso.getPrFromWarehouseId() ,
								theprwarehouse.getSearchName(),
								aPrmaster.getInventoryOnHand(),
								theprwarehsinventory.getInventoryOnHand(),
								aCusodetail.getQuantityOrdered(),BigDecimal.ZERO,"SO",aCuso.getCuSoid(),
								aCusodetail.getCuSodetailId(),aCuso.getSonumber(),null ,
				/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
				/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
								"SO Status "+transstatus+" to OPen",aCusodetail.getUserID(),aCusodetail.getUserName(),new Date());
						itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
						/*Code Ends*/
					}
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	public JoRelease InsertintoaNewJobasSalesOrder(Cuso cuso,Integer newstatus,Integer userID,String userName){
		Session aSession = null;
		Transaction aTransaction;
		Integer joReleaseID=null;
		JoRelease ajorelease=new JoRelease();
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			String tag=cuso.getTag();
			String custponumber=cuso.getCustomerPonumber();
			if(tag==null||tag==""){
				if(cuso.getRxCustomerId()!=null){
				tag=itsjobService.getCustomerName(cuso.getRxCustomerId().toString());
				}
			}
			if(custponumber==null||custponumber==""){
				if(cuso.getRxCustomerId()!=null){
					custponumber=itsjobService.getCustomerName(cuso.getRxCustomerId().toString());
				}
			}
			Jomaster aJoMaster = new Jomaster();
			aJoMaster.setDescription(tag);
			aJoMaster.setJobStatus(3);
			aJoMaster.setCreditStatus((byte)1);
			aJoMaster.setCreditContact0(0);
			aJoMaster.setCreditContact1(0);
			aJoMaster.setCreditContact2(0);
			aJoMaster.setClaimFiled((byte)0);
			aJoMaster.setCreatedById(userID);
			aJoMaster.setBookedDate(new Date());
			aJoMaster.setBidDate(new Date());
			aJoMaster.setCuAssignmentId0(cuso.getCuAssignmentId0());
			aJoMaster.setCuAssignmentId1(cuso.getCuAssignmentId1());
			aJoMaster.setCuAssignmentId2(cuso.getCuAssignmentId2());
			aJoMaster.setCuAssignmentId3(cuso.getCuAssignmentId3());
			aJoMaster.setCuAssignmentId4(cuso.getCuAssignmentId4());
			aJoMaster.setRxCustomerId(cuso.getRxCustomerId());
			aJoMaster.setCoDivisionId(cuso.getCoDivisionID());
			aJoMaster.setCoTaxTerritoryId(cuso.getCoTaxTerritoryId());
			aJoMaster.setCreatedOn(new Date());
			aJoMaster.setChangedOn(new Date());
			aJoMaster.setCustomerPonumber(custponumber);
			aJoMaster.setContractAmount(BigDecimal.ZERO);
			aJoMaster.setJobNumber(cuso.getSonumber());
			aJoMaster.setQuoteNumber(null);
			aJoMaster.setCreditStatusDate(new Date());
			aJoMaster.setWho0(userID);
			aSession.save(aJoMaster);
			aTransaction.commit();
			
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			
			ajorelease.setJoMasterId(aJoMaster.getJoMasterId());
			ajorelease.setReleaseType(2);
			ajorelease.setReleaseDate(new Date());
			ajorelease.setEstimatedBilling(BigDecimal.ZERO);
			ajorelease.setSeq_Number(1);
			joReleaseID=(Integer) aSession.save(ajorelease);
			aTransaction.commit();
			
			return ajorelease;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ajorelease;
		}
		
		
		
	}
	
	@Override
	public Boolean checkSalesOrderInvoicedornot(Integer cuSOId) {
		itsLogger.debug("checkSalesOrderInvoice");
		String aSalesselectQry = "SELECT * FROM cuInvoice WHERE cuSOID="
				+ cuSOId;
		Session aSession = null;
		Boolean returnvalue=false;
		try {

			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			// Iterator<?> aIterator = aQuery.list().iterator();
			if (aQuery.list().size() > 0) {
				itsLogger.debug("checkSalesOrder Invoiced");
				returnvalue=true;
			} else {
				returnvalue=false;
				itsLogger.debug("checkSalesOrder not invoice");
			}

		} catch (Exception e) {

			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}
		return returnvalue;
	}

	@Override
	public List<String> getListOfCCMailId(Integer currentUserID) {
		Session aSession = null;
		List<String>ListOfCCmail=new ArrayList<String>();
		try {

			aSession = itsSessionFactory.openSession();
		TsUserLogin user=(TsUserLogin)aSession.get(TsUserLogin.class, currentUserID);
			// Iterator<?> aIterator = aQuery.list().iterator();
			System.out.println(user);
			if(user!=null)
			{
				ListOfCCmail.add(user.getCcaddr1());
				ListOfCCmail.add(user.getCcaddr2());
				ListOfCCmail.add(user.getCcaddr3());
				ListOfCCmail.add(user.getCcaddr4());
				ListOfCCmail.add(user.getBccaddr());
		
			}

		} catch (Exception e) {

			itsLogger.error(e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			
		}
		return ListOfCCmail;
	}
}