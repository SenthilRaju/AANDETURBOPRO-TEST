package com.turborep.turbotracker.projectsView.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.projectsView.ProjectBean;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.Vepo;

@Service("projectServices")	
@Transactional
public class ProjectServiceImpl implements ProjectService {

	protected static Logger itsLogger = Logger.getLogger(ProjectServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public ArrayList<JobsBean> getBookedSubmit(SalesRepBean theSalesRep){
		int aJobStatus = 3;
		int aSubmittalSent = 0;
		String aBookedSubmitQry = "SELECT " +
									"joMaster.BookedDate as date, " +
									"joMaster.JobNumber as number, " +
									"joMaster.Description as name, " +
									"joMaster.ContractAmount as amount, " +
									"rxMaster.Name as fname, " +
									"rxMaster.Phone1 as phone, " +
									"tsUserLogin.FullName as name1, " +
									"rxMaster.FirstName as name2 FROM joMaster " +
									"INNER JOIN joSubmittalHeader on joSubmittalHeader.joMasterID = joMaster.joMasterID  "+
									"LEFT JOIN rxMaster ON rxMaster.rxMasterId = joMaster.rxCategory2 = joMaster.cuAssignmentID2 " +
									"INNER JOIN tsUserLogin ON tsUserLogin.UserLoginID = joMaster.cuAssignmentID0 " +
									"WHERE joMaster.SubmittalSent ='" + aSubmittalSent + "' " +
									"AND joMaster.JobStatus = '" + aJobStatus + "' " +
									"AND joMaster.cuAssignmentID0 = '" + theSalesRep.getSalesRepId() + "' AND tsUserLogin.LoginName != 'admin' " +
									"ORDER BY joMaster.BookedDate asc";
		Session aSession = null;
		ArrayList<JobsBean> aBookedQry = new ArrayList<JobsBean>();
		try{
			JobsBean aBookedSubmittal = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aBookedSubmitQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aBookedSubmittal = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				if (aObj[0] != null) {
					aBookedSubmittal.setBookedDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aBookedSubmittal.setJobNo((String)aObj[1]);
				aBookedSubmittal.setJobName((String)aObj[2]);
				if(aObj[3] != null) {
					aBookedSubmittal.setContractAmount((BigDecimal)aObj[3]);
				}
				if(aObj[4] != null) {
					aBookedSubmittal.setCustomer((String)aObj[4].toString());
				}
				if(aObj[5] != null) {
					aBookedSubmittal.setCustomerContact((String)aObj[5].toString());
				}
				if(aObj[6] != null) {
					aBookedSubmittal.setAssignedSalesman((String)aObj[6].toString());
				}
				if(aObj[7] != null) {
					aBookedSubmittal.setEngineer((String)aObj[7].toString());
				}
				aBookedQry.add(aBookedSubmittal);
			}
		} catch (Exception e) {
				itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aBookedSubmitQry = null;
		}
		return aBookedQry;
	}

	@Override
	public ArrayList<JobsBean> getBookedPendingCredit(SalesRepBean theSalesRep) {
		int aJobStatus = 3;
		int aCreditStatus = 2;
		int aCreditHold = 1;
		String aPendingCreditQry = "SELECT joMaster.BookedDate as date, " +
															"joMaster.JobNumber as number, " +
															"joMaster.Description as name, " +
															"joMaster.ContractAmount as amount, " +
															"c.Name as fname,c.Phone1 as phone, " +
															"tsUserLogin.FullName as name1, " +
															"a.FirstName as name2 " +
															"FROM joMaster " +
															"INNER JOIN rxMaster  c ON c.rxMasterId = joMaster.rxCategory2 " +
															"INNER JOIN tsUserLogin ON tsUserLogin.UserLoginID = joMaster.cuAssignmentID0 " +
															"INNER JOIN rxMaster a ON a.rxMasterId = joMaster.cuAssignmentID2 " +
															"INNER JOIN cuMaster ON cuMaster.cuMasterID = joMaster.rxCustomerID " +
															"WHERE (joMaster.cuAssignmentID0 = '" + theSalesRep.getSalesRepId() + "' " +
															"AND joMaster.JobStatus = '" + aJobStatus +"') " +
															"AND (joMaster.CreditStatus = ' " + aCreditStatus + " ' OR cuMaster.CreditHold=' " + aCreditHold + "') AND tsUserLogin.LoginName != 'admin' " +
															"ORDER BY joMaster.BookedDate asc ";
		Session aSession = null;
		ArrayList<JobsBean> aBookedpendingQry = new ArrayList<JobsBean>();
		try{
			JobsBean aBookedSubmittal = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPendingCreditQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aBookedSubmittal = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				if (aObj[0] != null) {
					aBookedSubmittal.setBookedDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aBookedSubmittal.setJobNo((String)aObj[1]);
				aBookedSubmittal.setJobName((String)aObj[2]);
				if(aObj[3] != null) {
					aBookedSubmittal.setContractAmount((BigDecimal)aObj[3]);
				}
				if(aObj[4] != null) {
					aBookedSubmittal.setCustomer((String)aObj[4].toString());
				}
				if(aObj[5] != null) {
					aBookedSubmittal.setCustomerContact((String)aObj[5].toString());
				}
				if(aObj[6] != null) {
					aBookedSubmittal.setAssignedSalesman((String)aObj[6].toString());
				}
				if(aObj[7] != null) {
					aBookedSubmittal.setEngineer((String)aObj[7].toString());
				}
				aBookedpendingQry.add(aBookedSubmittal);
			}
		} catch (Exception e) {
				itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aPendingCreditQry = null;
		}
		return aBookedpendingQry;
	}

	
	@Override
	public ArrayList<JobsBean> getBookedPendingOrder(SalesRepBean theSalesRep) {
		itsLogger.info("BookedPendingOrder");
		int aJobStatus =3;
		int aSubmittalSent = 1;
		String aPendingOrderQry = "SELECT DISTINCT " +
															" joMaster.BookedDate as date, " +
															" joMaster.JobNumber as number, " +
															" joMaster.Description as name, " +
															" joMaster.ContractAmount as amount, " +
															" c.Name as fname, " +
															" c.Phone1 as phone, " +
															" tsUserLogin.FullName as name1, " +
															" a.FirstName as name2 " +
															" FROM joMaster " +
															" INNER JOIN rxMaster  c ON c.rxMasterId = joMaster.rxCategory2 " +
															" INNER JOIN tsUserLogin ON tsUserLogin.UserLoginID = joMaster.cuAssignmentID0 " +
															" INNER JOIN rxMaster a ON a.rxMasterId = joMaster.cuAssignmentID2 " +
															" LEFT JOIN joRelease ON joRelease.joMasterID = joMaster.joMasterID " +
															" WHERE joMaster.cuAssignmentID0 = " + theSalesRep.getSalesRepId() + " " +
															" AND joMaster.submittalSent = " + aSubmittalSent + " " +
															" AND joRelease.POID IS NULL " +
															" AND joMaster.JobStatus = " + aJobStatus + " AND tsUserLogin.LoginName != 'admin' " +
															" ORDER BY joMaster.BookedDate asc";
		Session aSession = null;
		ArrayList<JobsBean> aBookedPendingOrderQry = new ArrayList<JobsBean>();
		try{
			JobsBean aBookedSubmittal = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPendingOrderQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aBookedSubmittal = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				if (aObj[0] != null) {
					aBookedSubmittal.setBookedDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aBookedSubmittal.setJobNo((String)aObj[1]);
				aBookedSubmittal.setJobName((String)aObj[2]);
				if(aObj[3] != null){
					aBookedSubmittal.setContractAmount((BigDecimal)aObj[3]);
				}
				if(aObj[4] != null) {
					aBookedSubmittal.setCustomer((String)aObj[4].toString());
				}
				if(aObj[5] != null) {
					aBookedSubmittal.setCustomerContact((String)aObj[5].toString());
				}
				if(aObj[6] != null) {
					aBookedSubmittal.setAssignedSalesman((String)aObj[6].toString());
				}
				if(aObj[7] != null) {
					aBookedSubmittal.setEngineer((String)aObj[7].toString());
				}
				aBookedPendingOrderQry.add(aBookedSubmittal);
			}
		} catch (Exception e) {
				itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aPendingOrderQry = null;
		}
		return aBookedPendingOrderQry;
	}

	@Override
	public ArrayList<JobsBean> getOrderTrackingDetails(SalesRepBean theSalesRep) {
		itsLogger.info("getOrderTrackingDetails");
		String orderTrackingQry = "SELECT DISTINCT " +
														" vp.PONumber, " +
														" vp.OrderDate, " +
														" vp.rxVendorId, " +
														" rm.name, " +
														" vp.VendorOrderNumber, " +
														" vp.EstimatedShipDate, " +
														" j.Description, " +
														" vp.CustomerPONumber " +
														" FROM vePO vp " +
														" LEFT JOIN rxMaster rm ON vp.rxVendorId = rm.rxMasterID " +
														" LEFT JOIN joMaster j ON vp.CustomerPONumber = j.CustomerPONumber " +
														" WHERE rm.isVendor = 1 " +
														" AND vp.TransactionStatus = 1 LIMIT 0, 100";
		Session aSession = null;
		ArrayList<JobsBean> aBookedQry = new ArrayList<JobsBean>();
		try{
			JobsBean aOrderTracking = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(orderTrackingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aOrderTracking = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				aOrderTracking.setPurchaseOrderNo((String) aObj[0]);
				if (aObj[1] != null) {
					aOrderTracking.setOrderDate((String)DateFormatUtils.format((Date)aObj[1], "MM/dd/yyyy"));
				}
				aOrderTracking.setVendor((String) aObj[3]);
				aOrderTracking.setVendorOrderNo((String) aObj[4]);
				if (aObj[5] != null) {
					aOrderTracking.setShipDate((String)DateFormatUtils.format((Date)aObj[5], "MM/dd/yyyy"));
				}
				aBookedQry.add(aOrderTracking);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			orderTrackingQry = null;
		}
		return aBookedQry;
	}

	@Override
	public ArrayList<JobsBean> getDormantProjects(SalesRepBean theSalesRep) {
		itsLogger.info("getDormantProjects");
		String aDormantQry =" SELECT DISTINCT " +
												" joMaster.JobNumber, " +
												" joMaster.Description, " +
												" joMaster.rxCustomerID, " +
												" rxMaster.Name, " +
												" rxMaster.Phone1 AS CustContact, " +
												" tsUserLogin.FullName AS SalesMan, " +
												" joMaster.ChangedOn as LastActivityDate " +
												" FROM joMaster " +
												" JOIN rxMaster ON joMaster.rxCustomerID = rxMaster.rxMasterID " +
												" LEFT JOIN tsUserLogin ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID " +
												" WHERE joMaster.cuAssignmentId0 = " + theSalesRep.getSalesRepId() + " " +
												" AND joMaster.SubmittalSent = 1 " +
												" AND joMaster.JobStatus = 3 AND tsUserLogin.LoginName != 'admin' ";
		Session aSession = null;
		ArrayList<JobsBean> aDormantProjectsList = new ArrayList<JobsBean>();
		try{
			JobsBean aDormantProject = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aDormantQry);
			Iterator<?> iterator = aQuery.list().iterator();
			while (iterator.hasNext()) {
				aDormantProject = new JobsBean();
				Object[] aObj = (Object[])iterator.next();
				aDormantProject.setJobNo((String) aObj[0]);
				aDormantProject.setJobName((String) aObj[1]);
				aDormantProject.setCustomer((String) aObj[3]);
				aDormantProject.setCustomerContact((String) aObj[4]);
				aDormantProject.setAssignedSalesman((String) aObj[5]);
				aDormantProject.setLastActivityDate((String)DateFormatUtils.format((Date)aObj[6], "MM/dd/yyyy"));
				aDormantProjectsList.add(aDormantProject);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aDormantQry = null;
		}
		return aDormantProjectsList;
	}
	@Override
	public ArrayList<BigDecimal> getCustomerARDetails(Integer tsUserLoginDetail){
		Session aSession = null;
		ArrayList<BigDecimal> aCustomerARDetails= new ArrayList<BigDecimal>();
		
		//String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		
	java.util.Date javaDate=new java.util.Date();
    java.sql.Date todayDate = new java.sql.Date(javaDate.getTime());
		

		//String aSelectQuery="SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'2014/09/25') AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)) AS SubQuery";
		String employeeCondn =" ";
		if(tsUserLoginDetail != 0){
		employeeCondn= " AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)";
		}
/*		String aSelectQuery="SELECT SUM( (CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END) ) AS AmtCur,"
				+ " SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ) AS Amt30,"
				+ " SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ) AS Amt60, "
				+ "SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90"
				+ " FROM  (SELECT InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance, DATEDIFF('"+todayDate+"',InvoiceDate) AS Days"
						+ " FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE "
						+ " IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND "
						+ "(ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .02) "+employeeCondn+") AS SubQuery";*/
		
		
		
		//String aSelectQuery="select AmtCur,Amt30,Amt60.Amt90 from (sp_arreport1('UIGrid',0,"+todayDate+"))";
		
		/*String aSelectQuery	CALL sp_arreport1('UIGrid',0,"+todayDate+");*/
		
		//aSelectQuery ="call sp_arreport1('UI','0',NULL)";
		
		
		//String aSelectQuery= "CALL sp_arreport1('UIGrid',0,'"+todayDate+"')";
		
		

		
		
	/*	String aSelectQuery= " SELECT SUM(AmtCur) as Amt ,SUM(Amt30)as Amt30,SUM(Amt60) as Amt60,SUM(Amt90) as Amt90 FROM (SELECT rxCustomerID,NAME,"
				+"SUM((CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END)) AS AmtCur,"
				+"SUM((CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END)) AS Amt30,"
				+"SUM((CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END)) AS Amt60,"
				+"SUM((CASE WHEN Days>90 THEN Balance ELSE 0 END)) AS Amt90 ,cuInvoiceID "
				+"FROM"
				+"(SELECT rxCustomerID,NAME ,cuInvoiceID,SUM(Balance) AS Balance,DATEDIFF('"+todayDate +"',InvoiceDate) AS Days FROM "
				+"(SELECT cuInvoice.cuInvoiceID,cuInvoice.rxCustomerID ,rxMaster.Name,(ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0))) AS Balance,"
				+"InvoiceDate,ar.arhistoryID "
				+"FROM `arhistory` ar "
				+"LEFT JOIN cuInvoice ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID)"
				+"LEFT JOIN rxMaster ON(cuInvoice.`rxCustomerID`=rxMaster.rxMasterID)"
				+"WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND"
				+"(TransactionStatus>0) AND (ABS(ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0))) > .02)"
				+"AND ar.createdOn<='"+todayDate +"') AS sub "
				+"GROUP BY cuInvoiceID HAVING MAX(arhistoryID)"
				+") AS a GROUP BY rxCustomerID ORDER BY NAME) AS sub";
	*/
		
		
		
		
	String	 aSelectQuery= " SELECT SUM(AmtCur) as Amt ,SUM(Amt30)as Amt30,SUM(Amt60) as Amt60,SUM(Amt90) as Amt90 FROM (SELECT a.rxCustomerID,NAME,Days,cuInvoiceID,"
	+" (CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END) AS AmtCur,"
				+" (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) AS Amt30,"
				+" (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) AS Amt60,"
				+" (CASE WHEN Days>90 THEN Balance ELSE 0 END) AS Amt90 , contName AS contact,InvoiceNumber,"
				+" Phone,Fax,CustomerPONumber,InvoiceDate,InvoiceAmount  "
				+" FROM"
				+" (SELECT cuInvoice.rxCustomerID ,cuInvoice.InvoiceNumber,CustomerPONumber,InvoiceDate,rxMaster.Name,cuInvoice.cuInvoiceID,ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0)) AS Balance,DATEDIFF('"+todayDate +"',InvoiceDate) AS Days"  
				+" ,ar.InvoiceAmount "
				+" FROM "
				+" (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory WHERE  DATE(createdOn)<='"+todayDate +"'AND `inv_rec`=0  GROUP BY cuInvoiceID) sub "
				+" JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)"
				+" LEFT JOIN cuInvoice ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID)"
				+"	LEFT JOIN rxMaster ON(cuInvoice.`rxCustomerID`=rxMaster.rxMasterID)"
				+"	WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND `inv_rec`=0 AND"
				+"	(TransactionStatus>0) AND (ABS(ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0))) > .02)"
				+" AND DATE(ar.createdOn)<='"+todayDate +"'"
						+"GROUP BY rxCustomerID,cuInvoiceID "		
                 +" UNION ALL"		
	            +" SELECT rxCustomerID,'Unapplied' AS InvoiceNumber,"
	        	+" (CASE cuReceiptTypeID WHEN 1 THEN "
	        	+" CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,'')) " 
				+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"			
				+" ReceiptDate AS InvoiceDate,"
				+"	rxMaster.Name,0 AS cuInvoiceID,"
				+"	(SUM(IFNULL(Appliedamount,0))-receiptamount) AS Balance , "
				+" DATEDIFF('"+todayDate +"',ReceiptDate) AS Days,(SUM(IFNULL(Appliedamount,0))-receiptamount) AS InvoiceAmount FROM ("
				+"	SELECT ar.arhistoryID,Reference,cuReceiptTypeID,cuReceipt.rxCustomerID,cuReceipt.cuReceiptID,IF(revornot=1,0,Amount) AS receiptamount,DiscountAmt,Appliedamount,"
				+" revornot,`createdOn`,`ReceiptDate` FROM "
				+"	(SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory  WHERE  DATE(createdOn)<='"+todayDate +"' AND `inv_rec`=1  GROUP BY cuReceiptID,cuInvoiceID) sub"
			+" JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)  "
				+" LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID)"
				+" WHERE `inv_rec`=1 AND  revornot=0)AS sub LEFT JOIN rxMaster ON(sub.rxCustomerID=rxMaster.rxMasterID) GROUP BY  rxCustomerID,cuReceiptID"
				
         +" Union All "

		+"  SELECT rxCustomerID,'Unapplied' AS InvoiceNumber,"
		+" (CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
		+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
		+"ar.createdOn AS InvoiceDate,rxMaster.Name,0 AS cuInvoiceID,(ar.Appliedamount*-1) AS Balance,DATEDIFF('"+todayDate+"' ,ar.`createdOn`) AS Days,ar.Appliedamount AS InvoiceAmount "
		+" FROM  (	"
		+" SELECT MAX(arhistoryID) AS arhistoryID,inv_rec FROM arhistory WHERE (inv_rec=1 OR inv_rec=2) AND  DATE(createdOn)<='"+todayDate+"' "
		+" GROUP BY cuReceiptID "
		+" ) sub JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID) "  
		+ " LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID) "
		+"  LEFT JOIN rxMaster ON(cuReceipt.rxCustomerID=rxMaster.rxMasterID) "
		+"  LEFT JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID "
		+" WHERE ar.inv_rec=2 AND  ar.revornot=0 "
		
				+") AS a LEFT JOIN (SELECT rxMasterID, `Name` AS contName,`Phone1` AS Phone,Fax FROM rxMaster) rxC ON a.rxCustomerID=rxC.rxMasterID"
				+"	WHERE ABS(Balance)>0 "
				+"	ORDER BY NAME,CASE cuInvoiceID WHEN 0 THEN 1  ELSE -1 END ASC, cuInvoiceID ASC)AS SU	";
	
		
		
		 if(tsUserLoginDetail != 0){
		
		 aSelectQuery="SELECT SUM(AmtCur) as Amt ,SUM(Amt30)as Amt30,SUM(Amt60) as Amt60,SUM(Amt90) as Amt90 FROM (SELECT a.rxCustomerID,NAME,Days,cuInvoiceID,"
				+"(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END) AS AmtCur,"
				+"(CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) AS Amt30,"
				+"(CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) AS Amt60,"
				+"(CASE WHEN Days>90 THEN Balance ELSE 0 END) AS Amt90 , contName AS contact,InvoiceNumber,"
				+"Phone,Fax,CustomerPONumber,InvoiceDate,InvoiceAmount  "
			+"	FROM"
			+"	(SELECT cuInvoice.rxCustomerID ,cuInvoice.InvoiceNumber,CustomerPONumber,InvoiceDate,rxMaster.Name,cuInvoice.cuInvoiceID,ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0)) AS Balance,DATEDIFF('"+todayDate +"',InvoiceDate) AS Days"  
				+" ,ar.InvoiceAmount "
				+" FROM "
				+" (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory WHERE  DATE(createdOn)<='"+todayDate +"' AND `inv_rec`=0  GROUP BY cuInvoiceID) sub "
				+" JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)"
			+"	LEFT JOIN cuInvoice ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID)"
			+"	LEFT JOIN rxMaster ON(cuInvoice.`rxCustomerID`=rxMaster.rxMasterID)"
			+"	LEFT JOIN cuMaster ON (cuInvoice.rxCustomerID =cuMaster.cuMasterID) "
			+"	WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND `inv_rec`=0 AND"
			+"	(TransactionStatus>0) AND (ABS(ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0))) > .02)"
			+"	AND DATE(ar.createdOn)<='"+todayDate +"' "
						+"AND "+tsUserLoginDetail+" IN (cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) "
			+"	GROUP BY rxCustomerID,cuInvoiceID "
				
			+"	UNION ALL "
			+"	SELECT rxCustomerID,'Unapplied' AS InvoiceNumber,"
			+"	(CASE cuReceiptTypeID WHEN 1 THEN "
			+"	CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))  "
			+"	WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,	"		
			+"	ReceiptDate AS InvoiceDate,"
			+"	rxMaster.Name,0 AS cuInvoiceID,"
			+"	(SUM(IFNULL(Appliedamount,0))-receiptamount) AS Balance , "
			+"	DATEDIFF('"+todayDate +"',ReceiptDate) AS Days,(SUM(IFNULL(Appliedamount,0))-receiptamount) AS InvoiceAmount FROM ("
						+"	SELECT ar.arhistoryID,Reference,cuReceiptTypeID,cuReceipt.rxCustomerID,cuReceipt.cuReceiptID,IF(revornot=1,0,Amount) AS receiptamount,DiscountAmt,Appliedamount,revornot,`createdOn`,`ReceiptDate` FROM "
			+"	(SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory  WHERE  DATE(createdOn)<='"+todayDate +"' AND `inv_rec`=1  GROUP BY cuReceiptID,cuInvoiceID) sub"
			+"	JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)  "
			+"	LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID)"
				+"	WHERE `inv_rec`=1 )AS sub LEFT JOIN rxMaster ON(sub.rxCustomerID=rxMaster.rxMasterID) "
			+"	LEFT JOIN cuMaster ON (rxMaster.rxMasterID =cuMaster.cuMasterID) "
			+"	WHERE "+tsUserLoginDetail+" IN (cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) "
			+"	GROUP BY  rxCustomerID,cuReceiptID"
				
			+"	UNION ALL"
				
			+"	SELECT rxCustomerID,'Unapplied' AS InvoiceNumber,"
			+"	(CASE cuReceiptTypeID WHEN 1 THEN "
			+"	CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))  "
			+"	WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
			+"	ar.createdOn AS InvoiceDate, rxMaster.Name,0 AS cuInvoiceID,"
			+"	ar.Appliedamount*-1 AS Balance,DATEDIFF('"+todayDate +"' ,ar.`createdOn`) AS Days,ar.Appliedamount AS InvoiceAmount  " 
			+"	FROM  (  SELECT MAX(arhistoryID) AS arhistoryID,inv_rec FROM arhistory WHERE (inv_rec=1 OR inv_rec=2) AND " 
			+"	DATE(createdOn)<='"+todayDate +"'  GROUP BY cuReceiptID  ) sub JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)  "
          +"	LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID)   LEFT JOIN rxMaster ON(cuReceipt.rxCustomerID=rxMaster.rxMasterID)  " 
			+"	LEFT JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID  WHERE "
			+"	"+tsUserLoginDetail+" IN (cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)  AND ar.inv_rec=2 AND  ar.revornot=0  AND DATE(ar.createdOn)<='"+todayDate +"'"
				
				
			+"	) AS a LEFT JOIN (SELECT rxMasterID, `Name` AS contName,`Phone1` AS Phone,Fax FROM rxMaster) rxC ON a.rxCustomerID=rxC.rxMasterID"
			+"	WHERE ABS(Balance)>0 "
			+"	ORDER BY NAME,CASE cuInvoiceID WHEN 0 THEN 1  ELSE -1 END ASC, cuInvoiceID ASC)AS SU	";
		
		
		 }
		
		
		
		
		
			
		
try{
	
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> iterator = aQuery.list().iterator();
			if(aQuery.list().isEmpty()){
				aCustomerARDetails.add(new BigDecimal(0));
				aCustomerARDetails.add(new BigDecimal(0));
				aCustomerARDetails.add(new BigDecimal(0));
				aCustomerARDetails.add(new BigDecimal(0));
				return aCustomerARDetails;
			}
			BigDecimal b2=BigDecimal.ZERO,
					b3=BigDecimal.ZERO,
					b4=BigDecimal.ZERO,
					b5=BigDecimal.ZERO;
			while (iterator.hasNext()) {
				
				Object[] aObj = (Object[])iterator.next();
				if(aObj[0] != null){
					//b2=b2.add((BigDecimal) aObj[2]);
					aCustomerARDetails.add((BigDecimal)aObj[0]);
						
				}
				else{
					aCustomerARDetails.add(new BigDecimal(0));
				}
				if(aObj[1] != null){
					//b3=b3.add((BigDecimal)aObj[3]);
				aCustomerARDetails.add((BigDecimal)aObj[1]);
				}
				else{
					aCustomerARDetails.add(new BigDecimal(0));
					
				}
				if(aObj[2] != null){
					//b4=b4.add((BigDecimal)aObj[4]);	
				aCustomerARDetails.add((BigDecimal)aObj[2]);
				}else{
					aCustomerARDetails.add(new BigDecimal(0));
					
				}
				if(aObj[3] != null){
					//b5=b5.add((BigDecimal) aObj[5]);
				aCustomerARDetails.add((BigDecimal)aObj[3]);
				}else{
					aCustomerARDetails.add(new BigDecimal(0));
				}
				
			}
			//aCustomerARDetails.add((BigDecimal)b2);
			//aCustomerARDetails.add((BigDecimal)b3);
			//aCustomerARDetails.add((BigDecimal)b4);
			//aCustomerARDetails.add((BigDecimal)b5);
			
			
			
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQuery = null;
			employeeCondn = null;
			todayDate=null;
		}		
		return aCustomerARDetails;
	}
	
	
	@Override
	public ArrayList<BigDecimal> getCustomerUnappliedARDetails(Integer tsUserLoginDetail){
		Session aSession = null;
		ArrayList<BigDecimal> aCustomerARDetails= new ArrayList<BigDecimal>();
		String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		//String aSelectQuery="SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'2014/09/25') AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)) AS SubQuery";
		String aSelectQuery= "";
		if(tsUserLoginDetail != 0){
			aSelectQuery ="call sp_arreport1('UI','"+tsUserLoginDetail+"',NULL)";		
		}
		else
		{
			aSelectQuery ="call sp_arreport1('UI','0',NULL)";		
		}
		
try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> iterator = aQuery.list().iterator();
			if(aQuery.list().isEmpty()){
				aCustomerARDetails.add(new BigDecimal(0));
				aCustomerARDetails.add(new BigDecimal(0));
				aCustomerARDetails.add(new BigDecimal(0));
				aCustomerARDetails.add(new BigDecimal(0));
				return aCustomerARDetails;
			}
			while (iterator.hasNext()) {
				
				Object[] aObj = (Object[])iterator.next();
				if(aObj[0] != null){
					aCustomerARDetails.add((BigDecimal)aObj[0]);	
				}else{
					aCustomerARDetails.add(new BigDecimal(0));
				}
				if(aObj[1] != null){
					aCustomerARDetails.add((BigDecimal)aObj[1]);	
				}else{
					aCustomerARDetails.add(new BigDecimal(0));
				}
				if(aObj[2] != null){
					aCustomerARDetails.add((BigDecimal)aObj[2]);	
				}else{
					aCustomerARDetails.add(new BigDecimal(0));
				}
				if(aObj[3] != null){
					aCustomerARDetails.add((BigDecimal)aObj[3]);	
				}else{
					aCustomerARDetails.add(new BigDecimal(0));
				}
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQuery = null;
		}		
		return aCustomerARDetails;
	}
	
	@Override
	public List<?> getOpenedJobs(Integer therxMasterID,Integer tsUserLoginID,int theFrom, int theTo,String sidx,String sordby,String fromDate,String todate) {

		
		Session aSession = null;
		String appendCondition=" WHERE jo.JobStatus=3 ";
		if(tsUserLoginID != null && tsUserLoginID!= 0){
			appendCondition += " AND "+tsUserLoginID+" IN (jo.cuAssignmentID0,jo.cuAssignmentID1,jo.cuAssignmentID2,jo.cuAssignmentID3,jo.cuAssignmentID4,jo.cuAssignmentID5,jo.cuAssignmentID6)";
			
			if(therxMasterID != null && therxMasterID!= -1){
				appendCondition += "AND jo.rxCustomerID = "+therxMasterID+" AND jo.JobStatus IN (0,-4,6,1,5,3)";
			}
		}else{
			if(therxMasterID != null && therxMasterID!= -1){
				appendCondition += "AND jo.rxCustomerID = "+therxMasterID+" AND jo.JobStatus IN (0,-4,6,1,5,3)";
			}
		}
		if(sidx.equalsIgnoreCase("joMasterId")){
			sidx = "jo.joMasterID";
		}else if(sidx.equalsIgnoreCase("customerName")){
			sidx = "rx.Name";
		}else if(sidx.equalsIgnoreCase("jobName")){
			sidx = "jo.Description";
		}else if(sidx.equalsIgnoreCase("jobNumber")){
			sidx = "jo.JobNumber";
		}else if(sidx.equalsIgnoreCase("contractAmount")){
			sidx = "(jo.ContractAmount+IFNULL(jc.ChangeAmount,0))";
		}
			if(!fromDate.equals("") && (!todate.equals(""))){
				appendCondition+=" AND jo.CreatedOn >= '"+fromDate+"' <= '"+todate+"' ";
			}else if(!fromDate.equals("") ){
				appendCondition+=" AND jo.CreatedOn >= '"+fromDate+"' ";
			}else if(!todate.equals("") ){
				appendCondition+=" AND jo.CreatedOn <= '"+todate+"' ";
			}
		//contractAmount
		String aSelectQry="SELECT IFNULL(rx.Name,'') AS customer,jo.JobNumber,jo.Description,(jo.ContractAmount+IFNULL(jc.ChangeAmount,0)) AS RevisedContract,IFNULL(SUM(jr.EstimatedBilling),0) AS Allocated , IFNULL(SUM(jd.CustomerInvoiceAmount),0) AS Invoiced,jo.joMasterID FROM joMaster jo LEFT JOIN rxMaster rx ON rx.rxMasterID=jo.rxCustomerID LEFT JOIN joChange jc ON jc.joMasterID = jo.joMasterID LEFT JOIN joRelease jr ON jo.joMasterID=jr.joMasterID LEFT JOIN joReleaseDetail jd ON jd.joReleaseID = jr.joReleaseID "+appendCondition+" group by jo.joMasterID ORDER BY "+sidx+" "+sordby.toUpperCase()+" LIMIT " + theFrom + ", " + theTo;
		//String aSelectQry="SELECT IFNULL(rx.Name,'') AS customer,jo.JobNumber,jo.Description,(jo.ContractAmount+IFNULL(jc.ChangeAmount,0)) AS RevisedContract,IFNULL(SUM(jr.EstimatedBilling),0) AS Allocated ,Project_Tab_invoicedAmount(jo.joMasterID)  AS Invoiced FROM joMaster jo LEFT JOIN rxMaster rx ON rx.rxMasterID=jo.rxCustomerID LEFT JOIN joChange jc ON jc.joMasterID = jo.joMasterID LEFT JOIN joRelease jr ON jo.joMasterID=jr.joMasterID  "+appendCondition+" group by jo.joMasterID ORDER BY "+sidx+" "+sordby.toUpperCase()+" LIMIT " + theFrom + ", " + theTo;
		ArrayList<ProjectBean> aQueryList = new ArrayList<ProjectBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				ProjectBean projectBean = new ProjectBean();
				BigDecimal contractAmount = (BigDecimal)aObj[3];
				BigDecimal allocated = (BigDecimal)aObj[4];
				BigDecimal invoiced= (BigDecimal)aObj[5];
				//BigDecimal invoiced= JobUtil.ConvertintoBigDecimal((String)aObj[5]);
				if(contractAmount  == null){
					contractAmount  = new BigDecimal(0);
				}
				if(allocated  == null){
					allocated  = new BigDecimal(0);
				}
				if(invoiced  == null){
					invoiced  = new BigDecimal(0);
				}
				
				projectBean.setCustomerName((String)aObj[0]);
				projectBean.setJobNumber((String)aObj[1]);
				projectBean.setJobName((String)aObj[2]);
				projectBean.setContractAmount(contractAmount);
				projectBean.setAllocated(allocated.subtract(invoiced));
				projectBean.setInvoiced(invoiced);
				projectBean.setJoMasterId((Integer)aObj[6]);
				projectBean.setUnreleased(contractAmount.subtract(allocated));
				aQueryList.add(projectBean);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			appendCondition = null;
			aSelectQry = null;
		}
		return aQueryList;
	}
	

	@Override
	public List<Vepo> getPurchaseSalesOrder(Integer therxMasterID,Integer tsUserLoginID,int theFrom, int theTo,String sidx,String sordby,String fromDate,String todate) {

		
		Session aSession = null;
		String appendCondition="";
		String appendCondition2="";	
		String appendCondition3="";		
		String orderCondition = "";
		String limitCondition = "";
	
		if(therxMasterID!= null && therxMasterID!= -1){
				
				appendCondition+= " AND rxCustomerID = "+therxMasterID+" ";
	
		}
		if(sidx.equalsIgnoreCase("ponumber")){
			sidx = "PONumber";
		}else if(sidx.equalsIgnoreCase("dateWanted")){
			sidx = "CreatedOn";
		}else if(sidx.equalsIgnoreCase("vendorName")){
			sidx = "vendor";
		}else if(sidx.equalsIgnoreCase("vendorOrderNumber")){
			sidx = "vendorNo";
		}else if(sidx.equalsIgnoreCase("shipDate")){
			sidx = "shipdate";
		}else{
			sidx = "CreatedOn";
		}
		if(!fromDate.equals("") && (!todate.equals(""))){
			appendCondition2+=" vePO.CreatedOn BETWEEN '"+fromDate+"' AND '"+todate+"' AND ";
			appendCondition3+=" cuso.CreatedOn BETWEEN '"+fromDate+"' AND '"+todate+"' AND ";
		}
		else if(!fromDate.equals("") ){
				appendCondition2+=" vePO.CreatedOn >= '"+fromDate+"' AND ";
				appendCondition3+=" cuso.CreatedOn >= '"+fromDate+"' AND ";
			}
		else if(!todate.equals("") ){
				appendCondition2+=" vePO.CreatedOn <= '"+todate+"' AND ";
				appendCondition3+=" cuso.CreatedOn <= '"+todate+"' AND ";
			}
		
		
			orderCondition = " ORDER BY "+sidx+" "+sordby.toUpperCase();
			
		if(theFrom<theTo){
			limitCondition = "  LIMIT " + theFrom + ", " + theTo;
		}
		
		
		String joMasterCondition = "(SELECT jr.joReleaseID FROM joMaster jo LEFT JOIN joRelease jr ON jo.joMasterID = jr.joMasterID WHERE (jo.cuAssignmentID0 ="+tsUserLoginID+" OR jo.cuAssignmentID1 ="+tsUserLoginID+" OR jo.cuAssignmentID2 ="+tsUserLoginID+" OR jo.cuAssignmentID3 ="+tsUserLoginID+" OR jo.cuAssignmentID4="+tsUserLoginID+"  OR jo.cuAssignmentID5="+tsUserLoginID+") "+appendCondition+" )";
		if(tsUserLoginID==0){
			joMasterCondition = "(SELECT jr.joReleaseID FROM joMaster jo LEFT JOIN joRelease jr ON jo.joMasterID = jr.joMasterID "+appendCondition+" )";
		}
		joMasterCondition= joMasterCondition.replace("rxCustomerID", "jo.rxCustomerID");
		
		String aSelectQry = "SELECT vePO.vePOID,vePO.CreatedOn,vePO.PONumber, rxm.rXMasterID, rxm.NAME AS vendor,vePO.vendorOrderNumber AS vendorNo,"
				+ "vePO.EstimatedShipDate, vePO.joReleaseID FROM vePO LEFT JOIN rxMaster rxm ON(rxm.rxMasterID=vePO.rxVendorId) WHERE "
				+appendCondition2+" joReleaseID IN "+joMasterCondition+"  AND (TransactionStatus in (0,1)) UNION SELECT cuso.cuSOID,"
				+ "cuso.CreatedOn,cuso.SONumber, rxm.rXMasterID AS rXMasterID, CONCAT('Warehouse:',prw.SearchName ), '' AS vendorNo, " 
				+ "STR_TO_DATE(cuso.DatePromised, '%m/%d/%Y') AS dateprom, cuso.joReleaseID FROM cuSO cuso LEFT JOIN rxMaster rxm "
				+ "ON(rxm.rxMasterID=cuso.rxCustomerID)	LEFT JOIN prWarehouse prw ON(prw.prWarehouseID = cuso.prFromWarehouseID) "
				+ "	WHERE "+ appendCondition3+" ((joReleaseID IN "+joMasterCondition+" ) OR (cuAssignmentID0 ="+tsUserLoginID+"  OR "
				+ "cuAssignmentID1 ="+tsUserLoginID+"  OR cuAssignmentID2 ="+tsUserLoginID+"  OR cuAssignmentID3 ="+tsUserLoginID+"  "
				+ "OR cuAssignmentID4="+tsUserLoginID+" )) AND (TransactionStatus in (0,1)) "+appendCondition+" "+orderCondition+" "+limitCondition;
		
		if(tsUserLoginID==0){
			
			aSelectQry = "SELECT vePO.vePOID,vePO.CreatedOn,vePO.PONumber, rxm.rXMasterID,rxm.NAME AS vendor, "
					+ " vePO.vendorOrderNumber AS vendorNo,vePO.EstimatedShipDate  as Estmidate, vePO.joReleaseID "
					+ " FROM vePO LEFT JOIN rxMaster rxm ON(rxm.rxMasterID=vePO.rxVendorId) WHERE "+appendCondition2+" joReleaseID IN "+joMasterCondition+"  AND (TransactionStatus in (0,1))"
					+ " UNION SELECT cuso.cuSOID,cuso.CreatedOn,cuso.SONumber,rxm.rXMasterID AS rXMasterID, " 
					+ " CONCAT('Warehouse:',prw.SearchName), '' AS vendorNo, cuso.shipdate, cuso.joReleaseID FROM cuSO cuso "
					+ " LEFT JOIN rxMaster rxm ON(rxm.rxMasterID=cuso.rxCustomerID)"
					+ "	LEFT JOIN prWarehouse prw ON(prw.prWarehouseID = cuso.prFromWarehouseID) "
					+ "WHERE "+ appendCondition3+" ((joReleaseID IN "+joMasterCondition+" ) ) AND "
					+ "(TransactionStatus in (0,1)) "+appendCondition+" "+orderCondition+" "+limitCondition;
			}
		ArrayList<Vepo> aQueryList = new ArrayList<Vepo>();
		String shipDate="";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				Vepo aVepo = new Vepo();
				
				aVepo.setVePoid((Integer) aObj[0]);
				String dateWanted = "";
				if(aObj[1]!=null){
					dateWanted=(String)DateFormatUtils.format((Date)aObj[1], "MM/dd/yyyy");
				}else{
					dateWanted="";
				}
				
				aVepo.setDateWanted(dateWanted);
			//	itsLogger.info("Date for Testing:"+dateWanted);
				aVepo.setPonumber((String) aObj[2]);
				aVepo.setRxVendorId((Integer) aObj[3]);
				aVepo.setVendorName((String) aObj[4]);
				aVepo.setVendorOrderNumber((String) aObj[5]);
				
				//itsLogger.info("Testing-->"+aObj[5].toString());
				
			//	if(!((String)aObj[5]).equals("00/00/0000")){
				if(!(aObj[6]==null)){
					  //Timestamp stamp = (Timestamp)(aObj[5]);
					  //Date date = new Date(stamp.getTime());
					  //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					  shipDate = (String)DateFormatUtils.format((Date)aObj[6], "MM/dd/yyyy");
					  aVepo.setShipDate(shipDate);
				}
				//aVepo.setEstimatedShipDate((Date)aObj[5]);
				aVepo.setJoReleaseId((Integer) aObj[7]);
				aQueryList.add(aVepo);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			 appendCondition=null;
			 appendCondition2=null;		
			 orderCondition =null;
			 limitCondition =null;
			aSelectQry=null;
			joMasterCondition=null;
			shipDate=null;
		}
		return aQueryList;
	}
	
	@Override
	public int getOpenedJobs(Integer therxMasterID,Integer tsUserLoginID,String fromDate,String todate) {

		
		Session aSession = null;
		String appendCondition=" WHERE jo.JobStatus=3 ";
		String and = "";
		if(tsUserLoginID!=null && tsUserLoginID!= 0){
			appendCondition += " AND "+tsUserLoginID+" IN (jo.cuAssignmentID0,jo.cuAssignmentID1,jo.cuAssignmentID2,jo.cuAssignmentID3,jo.cuAssignmentID4,jo.cuAssignmentID5,jo.cuAssignmentID6)";
			
			if(therxMasterID!=null && therxMasterID!= -1){
				appendCondition += "AND jo.rxCustomerID = "+therxMasterID+" AND jo.JobStatus IN (0,-4,6,1,5,3)";
			}
		}else{
			if(therxMasterID!=null && therxMasterID!= -1){
				appendCondition += "AND jo.rxCustomerID = "+therxMasterID+" AND jo.JobStatus IN (0,-4,6,1,5,3)";
			}
		}
		
		if(fromDate!=null &&!fromDate.equals("") && todate!=null  && (!todate.equals(""))){
			appendCondition+=" AND jo.CreatedOn >= '"+fromDate+"' <= '"+todate+"' ";
		}else if(fromDate!=null && !fromDate.equals("") ){
			appendCondition+=" AND jo.CreatedOn >= '"+fromDate+"' ";
		}else if(todate!=null &&!todate.equals("") ){
			appendCondition+=" AND  jo.CreatedOn <= '"+todate+"' ";
		}
		String aSelectQry="SELECT COUNT(DISTINCT(jo.joMasterID)) FROM joMaster jo LEFT JOIN rxMaster rx ON rx.rxMasterID=jo.rxCustomerID LEFT JOIN joChange jc ON jc.joMasterID = jo.joMasterID LEFT JOIN joRelease jr ON jo.joMasterID=jr.joMasterID LEFT JOIN joReleaseDetail jd ON jd.joReleaseID = jr.joReleaseID "+appendCondition+" ";
		
		BigInteger aTotalCount = null;
		 
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			//Iterator<?> aIterator = aQuery.list().iterator();
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			appendCondition = null;
			aSelectQry =null;
		}
		return aTotalCount.intValue();
	}
	
	@Override
	public ArrayList<ProjectBean> getCustomerAccountRecieveDetails(Integer tsUserLoginDetail, Integer customerID,String asofardate) {
		Session aSession = null;
		//String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	
		//String aSelectQuery="SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'2014/09/25') AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)) AS SubQuery";
		//String employeeCondn =" ";
		//String customerCondn="";
		//String groupBy="";
		/*if(customerID>0){
			customerCondn= " AND rxCustomerID="+customerID;
		}else{
			groupBy = " GROUP BY rxCustomerID";
		}
		if(tsUserLoginDetail!= null && tsUserLoginDetail != 0){
		employeeCondn= " AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)";
		
		}
		String aSelectQuery="SELECT rxCustomerID,Name, SUM( (CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT cuInvoice.rxCustomerID,rxMaster.Name ,InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance, DATEDIFF('"+todayDate+"',InvoiceDate) AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE  IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND (ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .02) "
		+employeeCondn+" "+customerCondn
		+") AS SubQuery "+groupBy;*/
		
		ArrayList<ProjectBean> aCustomerARDetails= new ArrayList<ProjectBean>();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		java.sql.Date asofdt =null;
		 java.util.Date utilDate = null;
		try
		{
		if(asofardate!=null && asofardate.length()>0 && asofardate!=""){
			
			utilDate = formatter.parse(asofardate);
			asofdt = new java.sql.Date(utilDate.getTime());
			
			System.out.println("----------------------------------------------------------------------------------------------------___>"+asofdt+"****");
		}
		}
		catch(ParseException e)

		{e.printStackTrace();}
		
		
		String aSelectQuery="";
		if(tsUserLoginDetail!= null && tsUserLoginDetail != 0)
			aSelectQuery= "CALL sp_arreport1('UIGrid',"+tsUserLoginDetail+",'"+asofdt+"')";
		else
			aSelectQuery= "CALL sp_arreport1('UIGrid',0,'"+asofdt+"')";
		
		
		try{
	
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> iterator = aQuery.list().iterator();
			
			while (iterator.hasNext()) {
				ProjectBean pbean=new ProjectBean();
				Object[] aObj = (Object[])iterator.next();
				pbean.setRxMasterId((Integer) aObj[0]);
				pbean.setCustomerName((String) aObj[1]);
				BigDecimal defaultzero=new BigDecimal(0);
				if(aObj[2]!=null){
					pbean.setCurrentAmt((BigDecimal)aObj[2]);
				}else{
					pbean.setCurrentAmt(defaultzero);
				}
				if(aObj[3]!=null){
					pbean.setThirtyDays((BigDecimal)aObj[3]);
				}else{
					pbean.setThirtyDays(defaultzero);
				}
				if(aObj[4]!=null){
					pbean.setSixtyDays((BigDecimal)aObj[4]);
				}else{
					pbean.setSixtyDays(defaultzero);
				}
				if(aObj[5]!=null){
					pbean.setNinetyDays((BigDecimal)aObj[5]);
				}else{
					pbean.setNinetyDays(defaultzero);
				}
				
				
				BigDecimal TotalDaysAmt=new BigDecimal(0);
				TotalDaysAmt=TotalDaysAmt.add(pbean.getCurrentAmt()).add(pbean.getThirtyDays()).add(pbean.getSixtyDays()).add(pbean.getNinetyDays());
				pbean.setTotalDaysAmt(TotalDaysAmt);
				aCustomerARDetails.add(pbean);
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}		
		return aCustomerARDetails;
	}
	
	/*@Override
	public int getCustomerAccountRecieveDetails(Integer tsUserLoginDetail){
		Session aSession = null;
		String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		ArrayList<ProjectBean> aCustomerARDetails= new ArrayList<ProjectBean>();
		//String aSelectQuery="SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'2014/09/25') AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)) AS SubQuery";
		String employeeCondn =" ";
		if(tsUserLoginDetail!= null && tsUserLoginDetail != 0){
		employeeCondn= " AND "+tsUserLoginDetail+" IN (cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)";
		}
		String aSelectQuery="SELECT COUNT(DISTINCT(rxCustomerID)) FROM  (SELECT cuInvoice.rxCustomerID,rxMaster.Name ,InvoiceAmount-AppliedAmount AS Balance, DATEDIFF('"+todayDate+"',InvoiceDate) AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE  IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) "
		+employeeCondn
		+") AS SubQuery GROUP BY rxCustomerID";
		
		BigInteger aTotalCount = null;
		
		try{
			
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			//Iterator<?> aIterator = aQuery.list().iterator();
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
	
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}		
		return aTotalCount.intValue();
	}*/
	
	@Override
	public int getPurchaseSalesOrder(Integer therxMasterID,Integer tsUserLoginID,String fromDate,String todate) {

		Session aSession = null;
		String appendCondition="";
		String appendCondition2="";		
		String appendCondition3="";
		String orderCondition = "";
		String limitCondition = "";
		if(therxMasterID!= null && therxMasterID!= -1){
			
			appendCondition+= " AND rxCustomerID = "+therxMasterID+" ";

	}
//	if(sidx.equalsIgnoreCase("ponumber")){
//		sidx = "PONumber";
//	}else if(sidx.equalsIgnoreCase("dateWanted")){
//		sidx = "CreatedOn";
//	}else if(sidx.equalsIgnoreCase("vendorName")){
//		sidx = "vendor";
//	}else if(sidx.equalsIgnoreCase("vendorOrderNumber")){
//		sidx = "vendorNo";
//	}else if(sidx.equalsIgnoreCase("shipDate")){
//		sidx = "shipdate";
//	}else{
//		sidx = "CreatedOn";
//	}
	if(!fromDate.equals("") && (!todate.equals(""))){
			appendCondition2+=" vePO.CreatedOn BETWEEN '"+fromDate+"' AND '"+todate+"' AND ";
			appendCondition3+=" cuso.CreatedOn BETWEEN '"+fromDate+"' AND '"+todate+"' AND ";
		}
	else if(!fromDate.equals("") ){
			appendCondition2+=" vePO.CreatedOn >= '"+fromDate+"' AND ";
			appendCondition3+=" cuso.CreatedOn >= '"+fromDate+"' AND ";
		}
	else if(!todate.equals("") ){
			appendCondition2+=" vePO.CreatedOn <= '"+todate+"' AND ";
			appendCondition3+=" cuso.CreatedOn <= '"+todate+"' AND ";
		}
	
	
//		orderCondition = " ORDER BY "+sidx+" "+sordby.toUpperCase();
		
//	if(theFrom<theTo){
//		limitCondition = "  LIMIT " + theFrom + ", " + theTo;
//	}
	
		
		
		
		String joMasterCondition = "(SELECT jr.joReleaseID FROM joMaster jo LEFT JOIN joRelease jr ON jo.joMasterID = jr.joMasterID WHERE (jo.cuAssignmentID0 ="+tsUserLoginID+" OR jo.cuAssignmentID1 ="+tsUserLoginID+" OR jo.cuAssignmentID2 ="+tsUserLoginID+" OR jo.cuAssignmentID3 ="+tsUserLoginID+" OR jo.cuAssignmentID4="+tsUserLoginID+"  OR jo.cuAssignmentID5="+tsUserLoginID+") "+appendCondition+" )";
		if(tsUserLoginID==0){
			joMasterCondition = "(SELECT jr.joReleaseID FROM joMaster jo LEFT JOIN joRelease jr ON jo.joMasterID = jr.joMasterID "+appendCondition+" )";
		}
		joMasterCondition= joMasterCondition.replace("rxCustomerID", "jo.rxCustomerID");
		
		String aSelectQry = "SELECT vePO.vePOID,vePO.CreatedOn,vePO.PONumber, rxm.rXMasterID, rxm.NAME AS vendor,vePO.vendorOrderNumber AS vendorNo,"
				+ "vePO.EstimatedShipDate, vePO.joReleaseID FROM vePO LEFT JOIN rxMaster rxm ON(rxm.rxMasterID=vePO.rxVendorId) WHERE "
				+appendCondition2+" joReleaseID IN "+joMasterCondition+"  AND (TransactionStatus in (0,1)) UNION SELECT cuso.cuSOID,"
				+ "cuso.CreatedOn,cuso.SONumber, rxm.rXMasterID AS rXMasterID, CONCAT('Warehouse:',prw.SearchName ), '' AS vendorNo, " 
				+ "STR_TO_DATE(cuso.DatePromised, '%m/%d/%Y') AS dateprom, cuso.joReleaseID FROM cuSO cuso LEFT JOIN rxMaster rxm "
				+ "ON(rxm.rxMasterID=cuso.rxCustomerID)	LEFT JOIN prWarehouse prw ON(prw.prWarehouseID = cuso.prFromWarehouseID) "
				+ "	WHERE "+ appendCondition3+" ((joReleaseID IN "+joMasterCondition+" ) OR (cuAssignmentID0 ="+tsUserLoginID+"  OR "
				+ "cuAssignmentID1 ="+tsUserLoginID+"  OR cuAssignmentID2 ="+tsUserLoginID+"  OR cuAssignmentID3 ="+tsUserLoginID+"  "
				+ "OR cuAssignmentID4="+tsUserLoginID+" )) AND (TransactionStatus in (0,1)) "+appendCondition+" "+orderCondition;
		
		if(tsUserLoginID==0){
			
			aSelectQry = "SELECT vePO.vePOID,vePO.CreatedOn,vePO.PONumber, rxm.rXMasterID,rxm.NAME AS vendor, "
					+ " vePO.vendorOrderNumber AS vendorNo,vePO.EstimatedShipDate  as Estmidate, vePO.joReleaseID "
					+ " FROM vePO LEFT JOIN rxMaster rxm ON(rxm.rxMasterID=vePO.rxVendorId) WHERE "+appendCondition2+" joReleaseID IN "+joMasterCondition+"  AND (TransactionStatus in (0,1))"
					+ " UNION SELECT cuso.cuSOID,cuso.CreatedOn,cuso.SONumber,rxm.rXMasterID AS rXMasterID, " 
					+ " CONCAT('Warehouse:',prw.SearchName), '' AS vendorNo, cuso.shipdate, cuso.joReleaseID FROM cuSO cuso "
					+ " LEFT JOIN rxMaster rxm ON(rxm.rxMasterID=cuso.rxCustomerID)"
					+ "	LEFT JOIN prWarehouse prw ON(prw.prWarehouseID = cuso.prFromWarehouseID) "
					+ "WHERE "+ appendCondition3+" ((joReleaseID IN "+joMasterCondition+" ) ) AND "
					+ "(TransactionStatus in (0,1)) "+appendCondition+" "+orderCondition;
			}
		
	
		
		BigInteger aTotalCount = null;
		 
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			List<?> aList = aQuery.list();
			if(aList.size()>0){
			aTotalCount=new BigInteger(String.valueOf(aList.size()));
			itsLogger.info("TOT COUNT:"+aTotalCount);
			}else{
				aTotalCount = new BigInteger("0");
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			 appendCondition=null;
			 orderCondition = null;
			 limitCondition = null;
			 aSelectQry = null;
			 appendCondition2=null;		
			 appendCondition3=null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public List<?> getProfitMargin(Integer therxMasterID,Integer tsUserLoginID,int theFrom, int theTo,String sidx,String sordby,String fromDate,String todate) {
		
		
		Session aDateSession = null;
		aDateSession = itsSessionFactory.openSession();
		Sysinfo aSysInfo = (Sysinfo) aDateSession.get(Sysinfo.class,1);
		
		Cofiscalyear aCoFiscalYear = (Cofiscalyear) aDateSession.get(Cofiscalyear.class,aSysInfo.getCurrentFiscalYearId());
		String startDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getStartDate());
		String endDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getEndDate());
		//String aSelectQry="SELECT rx.Name,SUM(jr.CustomerInvoiceAmount),SUM(jr.VendorInvoiceAmount),SUM((jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount)) FROM joMaster jo JOIN cuMaster cu ON cu.cuMasterId = jo.rxCustomerID LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  JOIN rxMaster rx ON rx.rxMasterID = jo.rxCustomerID WHERE "+tsUserLoginID+" IN (cu.cuAssignmentID0,cu.cuAssignmentID1,cu.cuAssignmentID2,cu.cuAssignmentID3,cu.cuAssignmentID4) AND ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) ) GROUP BY jo.rxCustomerID LIMIT " + theFrom + ", " + theTo;
		String condition ="";
		if(tsUserLoginID !=0){
			condition = tsUserLoginID+" IN (ci.cuAssignmentID0,ci.cuAssignmentID1,ci.cuAssignmentID2,ci.cuAssignmentID3,ci.cuAssignmentID4) AND ";
		}
		/*String aSelectQry="SELECT rx.Name,SUM(jr.CustomerInvoiceAmount),SUM(jr.VendorInvoiceAmount),SUM((jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount)) FROM joMaster jo LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  JOIN rxMaster rx ON rx.rxMasterID = jo.rxCustomerID WHERE "+condition+" ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) ) GROUP BY jo.rxCustomerID LIMIT " + theFrom + ", " + theTo;*/
		/*String aSelectQry ="SELECT rx.Name,SUM(IF(ci.joReleaseDetailID > 0,jr.CustomerInvoiceAmount,(ci.Subtotal+ci.Freight))) AS sales,"*/
		String aSelectQry ="SELECT rx.Name,SUM(IF(ci.Subtotal IS NULL,0.0000,ci.Subtotal)) AS sales,"
		/*String aSelectQry ="SELECT rx.Name,SUM(ci.Subtotal+ci.Freight) AS sales,"*/
				+ " SUM(IF(jr.VendorInvoiceAmount IS NULL,0.0000,jr.VendorInvoiceAmount)), "
				+ "IF(SUM(IF(jr.VendorInvoiceAmount>0,(jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount),((ci.Subtotal+ci.Freight)-IFNULL(ci.whseCostTotal,0.0000)))) IS NULL,0.0000,"
				+ "SUM(IF(jr.VendorInvoiceAmount>0,(jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount),((ci.Subtotal+ci.Freight)-IFNULL(ci.whseCostTotal,0.0000))))) "
				+ "AS prof FROM cuInvoice ci LEFT JOIN joReleaseDetail jr ON jr.joReleaseDetailID = ci.joReleaseDetailID "
				+ "LEFT JOIN rxMaster rx ON rx.rxMasterID = ci.rxCustomerID "
				+ "WHERE "+condition+" (((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR "
				+ "(jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' )) OR "
				+ "(ci.InvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"') ) "
				+ "GROUP BY ci.rxCustomerID ORDER BY Name LIMIT "+theFrom+","+theTo;
		
		
		ArrayList<ProjectBean> aQueryList = new ArrayList<ProjectBean>();
		try {
			Query aQuery = aDateSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				ProjectBean projectBean = new ProjectBean();
				projectBean.setCustomerName((String)aObj[0]);
				BigDecimal customerInvoice = new BigDecimal(0);
				BigDecimal vendorInvoice = new BigDecimal(0);
				BigDecimal profit =  new BigDecimal(0);//(BigDecimal)aObj[3];
				BigDecimal margin = new BigDecimal(0);
				if(aObj[1] != null){
					customerInvoice = (BigDecimal)aObj[1];
				}
				if(aObj[2] != null){
					vendorInvoice = (BigDecimal)aObj[2];
				}
				//profit =  customerInvoice.subtract(vendorInvoice);
				if(aObj[3]!= null){
					profit = (BigDecimal)aObj[3];
				}
				
				//BigDecimal profit =  (BigDecimal)aObj[3];
					if(customerInvoice.compareTo(new BigDecimal(0))!=0){
						margin=  new BigDecimal((profit.doubleValue()/customerInvoice.doubleValue()));
						//margin = margin.setScale(2,BigDecimal.ROUND_CEILING );
						margin = margin.multiply(new BigDecimal(100));
						
					}
				projectBean.setMargin(JobUtil.floorFigureoverall(margin,2));
				projectBean.setProfit(JobUtil.floorFigureoverall(profit,2));
				projectBean.setSales(customerInvoice);
				
					
				aQueryList.add(projectBean);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aDateSession.flush();
			aDateSession.close();
			condition = null;
			aSelectQry = null;
			startDate = null;
			endDate = null;
		}
		return aQueryList;
	}
	
	@Override
	public ArrayList<BigDecimal> getProjectCalculation(Integer tsUserLoginID) {
		
		
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Sysinfo aSysInfo = (Sysinfo) aSession.get(Sysinfo.class,1);
		
		Cofiscalyear aCoFiscalYear = (Cofiscalyear) aSession.get(Cofiscalyear.class,aSysInfo.getCurrentFiscalYearId());
		String startDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getStartDate());
		String endDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getEndDate());
		String condition = "";

		if(tsUserLoginID!= 0){
		condition = " "+tsUserLoginID+" IN (cu.cuAssignmentID0,cu.cuAssignmentID1,cu.cuAssignmentID2,cu.cuAssignmentID3,cu.cuAssignmentID4)  AND";
		}
//		String aSelectQry="SELECT SUM(jr.CustomerInvoiceAmount),SUM(jr.VendorInvoiceAmount),SUM((jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount)) FROM joMaster jo JOIN cuMaster cu ON cu.cuMasterId = jo.rxCustomerID LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  WHERE "+tsUserLoginID+" IN (cu.cuAssignmentID0,cu.cuAssignmentID1,cu.cuAssignmentID2,cu.cuAssignmentID3,cu.cuAssignmentID4)  AND ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) )";
		String aSelectQry="SELECT SUM(jr.CustomerInvoiceAmount),SUM(jr.VendorInvoiceAmount),SUM((jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount)) FROM joMaster jo JOIN cuMaster cu ON cu.cuMasterId = jo.rxCustomerID LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  WHERE "+condition+" ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) )";

		
		
		ArrayList<BigDecimal> aQueryList = new ArrayList<BigDecimal>();
		try {
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				
				
				BigDecimal customerInvoice = new BigDecimal(0);
				BigDecimal vendorInvoice = new BigDecimal(0);
				BigDecimal profit =  new BigDecimal(0);//(BigDecimal)aObj[3];
				BigDecimal margin = new BigDecimal(0);
				if(aObj[0] != null){
					customerInvoice = (BigDecimal)aObj[0];
				}
				if(aObj[1] != null){
					vendorInvoice  = (BigDecimal)aObj[1];
				}
				
					profit =  customerInvoice.subtract(vendorInvoice);
					if(customerInvoice.compareTo(new BigDecimal(0))!=0){
						margin=  new BigDecimal((profit.doubleValue()/customerInvoice.doubleValue()));
						margin = margin.setScale(2,BigDecimal.ROUND_CEILING );
					}
				
				aQueryList.add(margin);
				aQueryList.add(customerInvoice);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			condition = null;
			aSelectQry = null;
			startDate = null;
			endDate = null;
		}
		return aQueryList;
	}
	
	
	@Override
	public BigDecimal getProjectPriorCalculation(Integer tsUserLoginID) {
		
		
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Sysinfo aSysInfo = (Sysinfo) aSession.get(Sysinfo.class,1);
		itsLogger.info("Jai Test");
		Cofiscalyear aCoFiscalYear = (Cofiscalyear) aSession.get(Cofiscalyear.class,aSysInfo.getCurrentFiscalYearId()-1);
		BigDecimal retValue = new BigDecimal(0);
		if(aCoFiscalYear!=null){
			String startDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getStartDate());
			String endDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getEndDate());
			String condition = "";

			if(tsUserLoginID != null && tsUserLoginID!= 0){
					condition = tsUserLoginID+" IN (cu.cuAssignmentID0,cu.cuAssignmentID1,cu.cuAssignmentID2,cu.cuAssignmentID3,cu.cuAssignmentID4)  AND "; 
			}
			
			String aSelectQry="SELECT SUM(ifnull(jr.CustomerInvoiceAmount,0)),SUM(jr.CustomerInvoiceAmount) FROM joMaster jo JOIN cuMaster cu ON cu.cuMasterId = jo.rxCustomerID LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  WHERE  "+condition+"((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) ) GROUP BY jo.rxCustomerID";
			//		String aSelectQry="SELECT SUM(ifnull(jr.CustomerInvoiceAmount,0)),SUM(jr.CustomerInvoiceAmount) FROM joMaster jo JOIN cuMaster cu ON cu.cuMasterId = jo.rxCustomerID LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  WHERE "+tsUserLoginID+" IN (cu.cuAssignmentID0,cu.cuAssignmentID1,cu.cuAssignmentID2,cu.cuAssignmentID3,cu.cuAssignmentID4)  AND ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) ) GROUP BY jo.rxCustomerID";
		
			try {
				Query aQuery = aSession.createSQLQuery(aSelectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
						if(aObj[0]!= null){
							retValue = (BigDecimal)aObj[0];
					}
				}
			
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry = null;
			condition = null;
			startDate = null;
			endDate = null;
		}
		}
		return retValue;
	}
	
	@Override
	public int getProfitMarginTotal(Integer therxMasterID,Integer tsUserLoginID,int theFrom, int theTo,String sidx,String sordby,String fromDate,String todate) {
		
		
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Sysinfo aSysInfo = (Sysinfo) aSession.get(Sysinfo.class,1);
		
		Cofiscalyear aCoFiscalYear = (Cofiscalyear) aSession.get(Cofiscalyear.class,aSysInfo.getCurrentFiscalYearId());
		String startDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getStartDate());
		String endDate = new SimpleDateFormat("yyyy/MM/dd").format(aCoFiscalYear.getEndDate());
		//String aSelectQry="SELECT rx.Name,SUM(jr.CustomerInvoiceAmount),SUM(jr.VendorInvoiceAmount),SUM((jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount)) FROM joMaster jo JOIN cuMaster cu ON cu.cuMasterId = jo.rxCustomerID LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  JOIN rxMaster rx ON rx.rxMasterID = jo.rxCustomerID WHERE "+tsUserLoginID+" IN (cu.cuAssignmentID0,cu.cuAssignmentID1,cu.cuAssignmentID2,cu.cuAssignmentID3,cu.cuAssignmentID4) AND ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) ) GROUP BY jo.rxCustomerID LIMIT " + theFrom + ", " + theTo;
		String condition ="";
		if(tsUserLoginID !=0){
			condition = tsUserLoginID+" IN (ci.cuAssignmentID0,ci.cuAssignmentID1,ci.cuAssignmentID2,ci.cuAssignmentID3,ci.cuAssignmentID4) AND ";
		}
		/*String aSelectQry="SELECT rx.Name,SUM(jr.CustomerInvoiceAmount),SUM(jr.VendorInvoiceAmount),SUM((jr.CustomerInvoiceAmount)-(jr.VendorInvoiceAmount)) FROM joMaster jo LEFT JOIN joReleaseDetail jr ON jo.joMasterId = jr.joMasterID  JOIN rxMaster rx ON rx.rxMasterID = jo.rxCustomerID WHERE "+condition+" ((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR (jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) ) GROUP BY jo.rxCustomerID LIMIT " + theFrom + ", " + theTo;*/
		String aSelectQry ="SELECT COUNT(rx.Name) "
				+ "FROM cuInvoice ci LEFT JOIN joReleaseDetail jr ON jr.joReleaseDetailID = ci.joReleaseDetailID "
				+ "LEFT JOIN rxMaster rx ON rx.rxMasterID = ci.rxCustomerID "
				+ "WHERE "+condition+" (((jr.CustomerInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' ) OR "
				+ "(jr.VendorInvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"' )) OR "
				+ "(ci.InvoiceDate BETWEEN '"+startDate+"' AND '"+endDate+"') ) "
				+ "GROUP BY ci.rxCustomerID";
				
		int totalValue =0;
		try {
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			totalValue = (int) aList.size();
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			
			aSession = null;
			aSysInfo = null;
			aCoFiscalYear = null;
			startDate =null;
			endDate =null;
			condition =null;
		}
		return totalValue;
	}
	
	/*@Override
	public ArrayList<ProjectBean> getCustomerAccountRecieveDetails(Integer tsUserLoginDetail,int theFrom, int theTo,String sidx,String sordby){
		Session aSession = null;
		String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		ArrayList<ProjectBean> aCustomerARDetails= new ArrayList<ProjectBean>();
		//String aSelectQuery="SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'2014/09/25') AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)) AS SubQuery";
		String employeeCondn =" ";
		if(tsUserLoginDetail!= null && tsUserLoginDetail != 0){
		employeeCondn= " AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)";
		}
		String aSelectQuery="SELECT rxCustomerID,Name, SUM( (CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT cuInvoice.rxCustomerID,rxMaster.Name ,InvoiceAmount-AppliedAmount AS Balance, DATEDIFF('"+todayDate+"',InvoiceDate) AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE  IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) "
		+employeeCondn 
		+") AS SubQuery GROUP BY rxCustomerID LIMIT " + theFrom + ", " + theTo;
		try{
	
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> iterator = aQuery.list().iterator();
			
			while (iterator.hasNext()) {
				ProjectBean pbean=new ProjectBean();
				Object[] aObj = (Object[])iterator.next();
				pbean.setRxMasterId((Integer) aObj[0]);
				pbean.setCustomerName((String) aObj[1]);
				BigDecimal defaultzero=new BigDecimal(0);
				if(aObj[2]!=null){
					pbean.setCurrentAmt((BigDecimal)aObj[2]);
				}else{
					pbean.setCurrentAmt(defaultzero);
				}
				if(aObj[3]!=null){
					pbean.setThirtyDays((BigDecimal)aObj[3]);
				}else{
					pbean.setThirtyDays(defaultzero);
				}
				if(aObj[4]!=null){
					pbean.setSixtyDays((BigDecimal)aObj[4]);
				}else{
					pbean.setSixtyDays(defaultzero);
				}
				if(aObj[5]!=null){
					pbean.setNinetyDays((BigDecimal)aObj[5]);
				}else{
					pbean.setNinetyDays(defaultzero);
				}
				
				
				BigDecimal TotalDaysAmt=new BigDecimal(0);
				TotalDaysAmt=TotalDaysAmt.add(pbean.getCurrentAmt()).add(pbean.getThirtyDays()).add(pbean.getSixtyDays()).add(pbean.getNinetyDays());
				pbean.setTotalDaysAmt(TotalDaysAmt);
				aCustomerARDetails.add(pbean);
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}		
		return aCustomerARDetails;
	}*/
	
	
	
	
	@Override
	public ArrayList<ProjectBean> getARDetailsBasedonCustomer(Integer tsUserLoginDetail,Integer customerid,String inputdate,Integer customerName){
		Session aSession = null;
	//	String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		String searchDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		ArrayList<ProjectBean> aCustomerARDetails= new ArrayList<ProjectBean>();
		String subquerycondition="";
		if(inputdate!=null && inputdate.length()>0 && inputdate!=""){
			 SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat cpyFormat = new SimpleDateFormat("yyyy-MM-dd");
			    Date date = null;
				try {
					date = mdyFormat.parse(inputdate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    searchDate=cpyFormat.format(date);
				//subquerycondition=" Where InvoiceDate<DATE_FORMAT(STR_TO_DATE('"+inputdate+"', '%c/%e/%Y'), '%Y-%m-%d %H:%m:%s')";
		}
		if(customerName!=null && customerName!=0 ){
			customerid=customerName;
		}
		
		
		//String aSelectQuery="SELECT SUM( (CASE WHEN 1>0 THEN Balance ELSE 0 END) ) AS AmtCur, SUM( (CASE WHEN Days>30 THEN Balance ELSE 0 END) ) AS Amt30, SUM( (CASE WHEN Days>60 THEN Balance ELSE 0 END) ) AS Amt60, SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM  (SELECT InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,'2014/09/25') AS Days FROM cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID WHERE (TransactionStatus>0) AND (ABS(InvoiceAmount-AppliedAmount) > .02) AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)) AS SubQuery";
		String employeeCondn =" ";
		String empCond="";
		if(tsUserLoginDetail!=null && tsUserLoginDetail != 0){
		employeeCondn= " AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) AND cuInvoice.rxCustomerID='"+customerid+"' AND DATE(ar.createdOn)<='"+searchDate+"'";
		
		empCond= " AND "+tsUserLoginDetail+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) AND rxCustomerID='"+customerid+"' AND DATE(ar.createdOn)<='"+searchDate+"'";
		}
		else
		{
		employeeCondn ="AND cuInvoice.rxCustomerID='"+customerid+"' AND DATE(ar.createdOn)<='"+searchDate+"'";
		empCond ="AND rxCustomerID='"+customerid+"' AND DATE(ar.createdOn)<='"+searchDate+"'";
		}
		
		
		/*String aSelectQuery="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60, CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID  FROM  "
				+ "(SELECT * FROM (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,ar.InvoiceAmount AS totalamount,DATEDIFF('"+searchDate+"',InvoiceDate) AS Days,ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0)) AS Balance "
		+ "FROM arhistory ar JOIN cuInvoice  ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID) LEFT JOIN cuLinkageDetail cdp on cdp.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0)  "+
		employeeCondn
		+" ORDER BY `arhistoryID` DESC) AS query1 "
		+" GROUP BY cuInvoiceID  HAVING ABS(balance)>0"
		+ " UNION ALL "
		+ " (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+searchDate+"',InvoiceDate) AS Days,"
		+ " sum(cdp.PaymentApplied+cdp.DiscountUsed) AS Balance  FROM  cuLinkageDetail cdp ,cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID "
		+ " LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) "
		+ " AND cdp.cuInvoiceID = cuInvoice.cuInvoiceID AND cuInvoice.rxCustomerID='"+customerid+"' AND DATE(cdp.datePaid) > '"+searchDate+"' AND DATE(InvoiceDate)<= '"+searchDate+"' Group by cuInvoice.cuInvoiceID) "
		+ " UNION ALL "
		+ " (SELECT 0 AS cuInvoiceID,ReceiptDate AS InvoiceDate,'Unapplied' AS InvoiceNumber,(CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
		+ " WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,( SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount ) AS totalamount,DATEDIFF('"+searchDate+"',ReceiptDate) AS Days,(SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount) AS Balance"
		+ " FROM cuReceipt cr LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID AND DATE(cdp.datePaid) <= '"+searchDate+"')"
		+ " WHERE cr.rxCustomerID IN ('"+customerid+"') AND  DATE(ReceiptDate)<= '"+searchDate+"' AND cr.reversePaymentStatus <>1  GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID  HAVING Balance<0)"
		+ ")AS SubQuery WHERE Days>=0";*/
		
		String aSelectQuery="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, "
				+" CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,"
				+" CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID "
				+" FROM ("
				+" SELECT cuInvoice.cuInvoiceID,InvoiceDate,"
				+" InvoiceNumber,CustomerPONumber,ar.InvoiceAmount AS totalamount,DATEDIFF('"+searchDate+"',InvoiceDate) AS Days,"
				+" ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0)) AS Balance "
				+ "From (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory WHERE DATE(createdOn)<='"+searchDate+"' AND `inv_rec`=0 GROUP BY cuInvoiceID) sub  JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)"
				+" JOIN cuInvoice ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID) "
				+" LEFT JOIN cuMaster  ON cuMaster.cuMasterID = cuInvoice.rxCustomerID"
				+" WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND `inv_rec`=0 "
				+ employeeCondn
				+" GROUP BY cuInvoiceID "
				
				+" Union All "
				
				+" SELECT 0 AS cuInvoiceID, ReceiptDate  AS InvoiceDate,'Unapplied' AS InvoiceNumber, "
				+" (CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
				+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
				+" (SUM(IFNULL(Appliedamount,0))-receiptamount) AS totalamount , "
				+" DATEDIFF('"+searchDate+"',ReceiptDate) AS Days, (SUM(IFNULL(Appliedamount,0))-receiptamount) AS Balance FROM ( "
				+" SELECT ar.arhistoryID,Reference,cuReceiptTypeID,cuReceipt.rxCustomerID,cuReceipt.cuReceiptID,IF(revornot=1,0,Amount) AS receiptamount,DiscountAmt,Appliedamount,revornot,`createdOn`,`ReceiptDate` FROM  "
				+" (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory  WHERE  DATE(createdOn)<='"+searchDate+"' AND `inv_rec`=1  GROUP BY cuReceiptID,cuInvoiceID) sub "
				+" JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)   "
				+" LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID) "
				+" WHERE `inv_rec`=1 )AS sub LEFT JOIN rxMaster ON(sub.rxCustomerID=rxMaster.rxMasterID) WHERE rxCustomerID='"+customerid+"' GROUP BY cuReceiptID "
				
				+" Union All "
		
		+"  SELECT 0 AS cuInvoiceID,ar.createdOn AS InvoiceDate,'Unapplied' AS InvoiceNumber,"
		+" (CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
		+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
		+"ar.Appliedamount AS totalamount,DATEDIFF('"+searchDate+"' ,ar.`createdOn`) AS Days,(ar.Appliedamount*-1) AS Balance "
		+" FROM  (	"
		+" SELECT MAX(arhistoryID) AS arhistoryID,inv_rec FROM arhistory WHERE (inv_rec=1 OR inv_rec=2) AND  DATE(createdOn)<='"+searchDate+"' "
		+" GROUP BY cuReceiptID "
		+" ) sub JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID) "  
		+ " LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID) "
		+"  LEFT JOIN rxMaster ON(cuReceipt.rxCustomerID=rxMaster.rxMasterID) "
		+"  LEFT JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID "
		+" WHERE ar.inv_rec=2 "+empCond
				+" ) AS SubQuery WHERE Days>=0 and ABS(Balance)>0";
		
		
		
		
		//+"IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) ";
		

		
		System.out.println("ARDetailQuery::"+aSelectQuery);
		try{
	
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> iterator = aQuery.list().iterator();
			
			while (iterator.hasNext()) {
				ProjectBean pbean=new ProjectBean();
				Object[] aObj = (Object[])iterator.next();
				pbean.setInvoiceDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				pbean.setInvoiceNumber((String) aObj[1]);
				pbean.setPoNumber((String) aObj[2]);
				pbean.setPoAmount((BigDecimal) aObj[3]);
				
				if(aObj[4] instanceof Integer)
				pbean.setDays((Integer) aObj[4]);
				else
				pbean.setDays(((BigInteger) aObj[4]).intValue());
				
				BigDecimal defaultzero=new BigDecimal(0); 
				if(aObj[5]!=null){
					pbean.setCurrentAmt((BigDecimal)aObj[5]);
				}else{
					pbean.setCurrentAmt(defaultzero);
				}
				if(aObj[6]!=null){
					pbean.setThirtyDays((BigDecimal)aObj[6]);
				}else{
					pbean.setThirtyDays(defaultzero);
				}
				if(aObj[7]!=null){
					pbean.setSixtyDays((BigDecimal)aObj[7]);
				}else{
					pbean.setSixtyDays(defaultzero);
				}
				if(aObj[8]!=null){
					pbean.setNinetyDays((BigDecimal)aObj[8]);
				}else{
					pbean.setNinetyDays(defaultzero);
				}
				
				if(aObj[9] instanceof Integer)
					pbean.setInvoiceID((Integer) aObj[9]);
					else
					pbean.setInvoiceID(((BigInteger) aObj[9]).intValue());
				
				BigDecimal TotalDaysAmt=new BigDecimal(0);
				TotalDaysAmt=TotalDaysAmt.add(pbean.getCurrentAmt()).add(pbean.getThirtyDays()).add(pbean.getSixtyDays()).add(pbean.getNinetyDays());
				pbean.setTotalDaysAmt(TotalDaysAmt);
				aCustomerARDetails.add(pbean);
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}		
		return aCustomerARDetails;
	}
	
	public String getJoMasterData(Integer joReleaseId) {
		Session aSession = itsSessionFactory.openSession();
		int rxCustomerID = 0, joMasterID = 0;
		String JobNumber = "", Description = "", returnString = "";
		String aSelectQry="SELECT JobNumber, rxCustomerID, joMasterID, Description FROM joMaster WHERE joMasterID=( SELECT joMasterID FROM joRelease WHERE joReleaseID ="+joReleaseId+")";
		itsLogger.info(" aSelectQry  :: " +aSelectQry);
		try {
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> iterator = aQuery.list().iterator();
			
			while (iterator.hasNext()) {
				Object[] aObj = (Object[])iterator.next();
				JobNumber = (String) aObj[0];
				rxCustomerID = (Integer) aObj[1];
				joMasterID = (Integer) aObj[2];
				Description = (String) aObj[3];
				returnString = JobNumber+"@"+rxCustomerID+"@"+joMasterID+"@"+Description;
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
		}
		return returnString;
	}
}
