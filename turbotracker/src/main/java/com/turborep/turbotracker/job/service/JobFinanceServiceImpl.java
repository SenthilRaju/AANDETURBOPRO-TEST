package com.turborep.turbotracker.job.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.job.dao.JoFinancialReportTemp;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.util.JobUtil;

/**
 * This class consists of a mehod that returns the fields to be loaded in finance
 * tab UI.
 *
 * <p>The methods of this class all throw a <tt>JobException</tt>
 *
 * <p>This class is a member of the
 * @author  Kannan Subbu
 * @see     HashMap
 * @since   1.9.7
 */

@Service("jobfinanceservice")
@Transactional
public class JobFinanceServiceImpl implements JobFinanceService{

	protected static Logger itsLogger = Logger.getLogger(JobServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	/**
	 * It calculates the entire Finace tab field in job finance.
	 * @param: {@link JomasterID}
	 * @see: {@link HashMap}
	 * @exception: {@link JobException} */
	@Override
	public HashMap<String, Object> getJobFinanceDetails(Integer JoMasterID)
			throws JobException {
		Session joReleaseDetailSession = null;
		BigDecimal invoiced = null;							/*Invoiced to date in UI*/
		BigDecimal invoicedwofretax=BigDecimal.ZERO;
		BigDecimal frieght = null;
		BigDecimal itid = null;								/*Tax*/
		BigDecimal invoicedWithTax = null;
		BigDecimal appliedAmount = null;
		BigDecimal appliedAmount_new = new BigDecimal(0);
		BigDecimal actualCost = new BigDecimal(0);
		BigDecimal paidToDate = null;
		BigDecimal currentCommissionProfit = null;
		BigDecimal cpo = null;								/*commission Profit*/
		BigDecimal contractAmount = null;
		BigDecimal revisedContract = null;
		BigDecimal accountRecievable = null;
		BigDecimal billingReminder = null;
		BigDecimal estimatedTax = null;
		BigDecimal closeOut = null;
		BigDecimal estimatedCost = null;
		BigDecimal estimatedProfit = null;
		BigDecimal actualProfit = null;
		BigDecimal changeOrder = null;
		BigDecimal trtyDays = new BigDecimal(0);
		BigDecimal sixtyDays = new BigDecimal(0);
		BigDecimal nintyDays = new BigDecimal(0);
		BigDecimal amtCur = new BigDecimal(0);
		HashMap<String,Object> financeTabFields= new HashMap<String,Object>();
		
		String QuoteNumber=null;
		String queryToMakePaymentDateNull=null;
		String queryToGetJobAmount=null;
		String queryToCalcWageCost=null;
		String queryToCalculateSalesOrderAmount=null;
		String queryTocalcPOTotal=null;
		String queryTocalcommissions=null;
		String queryToGetbillCost=null;
		String queryToGetjoChangeTotal=null;
		String getTaxRate=null;
		String actProfitQuery =null;
		String actProfitQuery2=null;
		String invoiceDate=null;
		String customerOverallQuery=null;
		
		try {
			joReleaseDetailSession = itsSessionFactory.openSession();
			
			/* initialize all payment date columns in joReleaseDetail associated with the jomaster as null*/
			queryToMakePaymentDateNull = "UPDATE joReleaseDetail Set PaymentDate = NULL "
												+ " WHERE (joReleaseDetailID IN "  
												+" (SELECT R.joReleaseDetailID FROM joReleaseDetail AS R INNER JOIN cuInvoice AS I" 
												+" ON R.joReleaseDetailID = I.joReleaseDetailID" 
												+" WHERE (NOT (R.PaymentDate IS NULL)) AND (ABS(I.InvoiceAmount - (I.AppliedAmount+I.DiscountAmt) > .01))) "
												+" AND joMasterID = :JoMasterID" ;
			joReleaseDetailSession.createSQLQuery(queryToMakePaymentDateNull).setParameter("JoMasterID", JoMasterID); 
			
			/* find the sum of freight, sub total, tax amount and applied amount of the job */
			queryToGetJobAmount = "SELECT  Sum(IFNULL(I.CostTotal,0.0000)) AS SumOfCostTotal, "
											+" Sum(I.Freight) AS SumOfFreight, "
											+" Sum(I.Subtotal) AS SumOfSubtotal,"
					 						+" Sum(I.TaxAmount) AS SumOfTaxAmount,"
					 						+" Sum(IFNULL(I.AppliedAmount,0)+IFNULL(I.DiscountAmt,0)) AS SumOfAppliedAmount"
					 						+" FROM joRelease AS R " 
					 						+" RIGHT JOIN (joReleaseDetail AS D RIGHT JOIN cuInvoice AS I ON D.joReleaseDetailID = I.joReleaseDetailID)" 
					 						+" ON R.joReleaseID = D.joReleaseID WHERE (R.joMasterID="+JoMasterID+") AND (I.TransactionStatus<>-1) AND (R.ReleaseType = 1" 
					 						+" OR R.ReleaseType = 4 )";
					 						//+" OR R.ReleaseType = 4 OR ((R.ReleaseType = 3 OR R.ReleaseType =5 OR R.ReleaseType = 2) AND (I.cuSOID Is Null OR I.cuSOID < 1)))";
			//System.out.println("queryToGetJobAmount :: "+queryToGetJobAmount);
			Query aQuery = (Query) joReleaseDetailSession.createSQLQuery(queryToGetJobAmount);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {						/* Always returns one row.*/
				
				Object[] aObj = (Object[])aIterator.next();
				
				if(aObj[1] != null)
					frieght = (BigDecimal) aObj[1];
				if(aObj[3] != null)
					itid = (BigDecimal) aObj[3];
				if(aObj[4] != null)
					appliedAmount = (BigDecimal) aObj[4];
				if(aObj[2] != null && frieght != null){
					invoiced = ((BigDecimal) aObj[2]).add(frieght);
					invoicedwofretax=invoicedwofretax.add((BigDecimal) aObj[2]);
				}
				if(aObj[0] != null)
					actualCost = (BigDecimal) aObj[0]; 
				if(invoiced != null && itid != null)
					invoicedWithTax = invoiced.add(itid);
				if(aObj[4] != null)
					paidToDate = (BigDecimal) aObj[4];
				
			}
			/* Calculate wage cost */
			/*Commented By:Velmurugan jenith
			 * Commented on:16-6-2015 4.02PM
			 * queryToCalcWageCost = " SELECT SUM(TJ.Quantity * (TJ.Rate+TJ.Overhead)) AS Amount FROM moTransaction AS BT"
										   +" RIGHT OUTER JOIN  epTransaction AS PT ON BT.moTransactionID = PT.moTransactionID" 
										   +" RIGHT OUTER JOIN  epTranJob AS TJ ON PT.epTransactionID = TJ.epTransactionID"
										   +" Where (TJ.joMasterID = " + JoMasterID +") And (BT.Void = 0)";
			Query aQuery2 = joReleaseDetailSession.createSQLQuery(queryToCalcWageCost);
						*/
			/*if (!aQuery2.list().isEmpty() && aQuery2.uniqueResult() != null && actualCost!= null) {
				actualCost = actualCost.add((BigDecimal)aQuery2.list().get(0));
			}*/
			/*Commented By:Velmurugan jenith
			 * Commented on:16-6-2015 4.02PM
			 * Object objActualCost = aQuery2.uniqueResult();
			if (objActualCost != null && actualCost!= null) {
				actualCost = actualCost.add((BigDecimal)objActualCost);
			}*/
			
			/* calculate amount from sales order and invoice */
			/*String queryToCalculateSalesOrderAmount = "SELECT Sum(AppliedAmount) AS AppliedAmount,"
					+ " sum(inTax) AS TaxTotal, sum(Freight) AS FreightTotal"
														+" , sum(SubTotal) AS Subtotal, "
														+ "Sum( (CASE WHEN (SOCost>0 AND (SOCost)>(INCost) AND (TransactionStatus=1)) THEN (SOCost)"
														+" ELSE (INCost) END) ) AS Cost FROM "
														+" (SELECT R.ReleaseType, B.INCost, B.SubTotal, B.inTax, B.Freight, B.AppliedAmount, P.TransactionStatus"
													    +" , P.CostTotal AS SOCost,"
													    + " P.TaxTotal AS soTax "
													    + "FROM joRelease AS R RIGHT JOIN((SELECT cuSOID, Sum(INCost) AS INCost"
													    +" , sum(SubTotal) AS SubTotal, sum(inTax) as inTax, sum(Freight) as Freight, sum(AppliedAmount) AS AppliedAmount "
													    +" FROM (SELECT P.cuSOID, B.CostTotal as INCost, B.SubTotal AS SubTotal, B.TaxAmount AS inTax, B.Freight as Freight"
													    +" , B.AppliedAmount as AppliedAmount FROM (cuSO AS P RIGHT JOIN cuInvoice AS B ON P.cuSOID = B.cuSOID) "
													    +" LEFT JOIN joRelease AS R ON P.joReleaseID = R.joReleaseID WHERE B.TransactionStatus>0 And R.ReleaseType = 2" 
													    +" And R.joMasterID = " + JoMasterID +" UNION ALL SELECT P.cuSOID, 0 as CostTotal, 0 as SubTotal, 0 as TaxAmount, 0 as Freight"
													    +" ,0 as AppliedAmount"
													    +" FROM cuSO as P LEFT JOIN joRelease AS R ON P.joReleaseID = R.joReleaseID" 
													    +" WHERE P.TransactionStatus>0 And R.ReleaseType = 2 And R.joMasterID = " + JoMasterID +" ) AS BILLZ" 
													    +" GROUP BY cuSOID) AS B LEFT JOIN cuSO AS P ON B.cuSOID = P.cuSOID) ON R.joReleaseID = P.joReleaseID" 
													    +" Where R.joMasterID =" + JoMasterID +") AS Temp";*/
			queryToCalculateSalesOrderAmount = "SELECT Sum(IFNULL(AppliedAmount,0)) AS AppliedAmount,"
														+" sum(inTax) AS TaxTotal, sum(Freight) AS FreightTotal"
														+" , sum(SubTotal) AS Subtotal, sum(IFNULL(whseCostTotal,0.0000)) AS wareHouseCostTotal, "
														+"Sum( (CASE WHEN (SOCost>0 AND (SOCost)>(INCost) AND (TransactionStatus=1)) THEN (SOCost)"
														+" ELSE (INCost) END) ) AS Cost FROM "
														+" (SELECT R.ReleaseType, B.INCost, B.SubTotal, B.inTax, B.Freight, B.AppliedAmount, P.TransactionStatus"
													    +" , P.CostTotal AS SOCost,"
													    + " P.TaxTotal AS soTax ,P.CostTotal as whseCostTotal "
													    + "FROM joRelease AS R RIGHT JOIN((SELECT cuSOID, Sum(INCost) AS INCost"
													    +" , sum(SubTotal) AS SubTotal, sum(inTax) as inTax, sum(Freight) as Freight, sum(AppliedAmount) AS AppliedAmount "
													    +" FROM (SELECT P.cuSOID, B.CostTotal as INCost, B.SubTotal AS SubTotal, B.TaxAmount AS inTax, B.Freight as Freight"
													    +" , (B.AppliedAmount+B.DiscountAmt) as AppliedAmount FROM (cuSO AS P RIGHT JOIN cuInvoice AS B ON P.cuSOID = B.cuSOID) "
													    +" LEFT JOIN joRelease AS R ON P.joReleaseID = R.joReleaseID WHERE B.TransactionStatus>0 "
													    +" AND (R.ReleaseType=2 OR R.ReleaseType=3 OR R.ReleaseType=5)" 
													    +" And R.joMasterID = " + JoMasterID +") AS BILLZ" 
													    +" GROUP BY cuSOID) AS B LEFT JOIN cuSO AS P ON B.cuSOID = P.cuSOID) ON R.joReleaseID = P.joReleaseID" 
													    +" Where R.joMasterID =" + JoMasterID +" AND (R.ReleaseType=2 OR R.ReleaseType=3 OR R.ReleaseType=5)) AS Temp";
			Query aQuery3  = joReleaseDetailSession.createSQLQuery(queryToCalculateSalesOrderAmount);
			Iterator<?> asalesIterator = aQuery3.list().iterator();
			if(appliedAmount== null){
				appliedAmount=BigDecimal.ZERO;
			}
			if(invoiced== null){
				invoiced=BigDecimal.ZERO;
			}
			if(frieght== null){
				frieght=BigDecimal.ZERO;
			}
			if(itid== null){
				itid=BigDecimal.ZERO;
			}
			if(actualCost== null){
				actualCost=BigDecimal.ZERO;
			}
			if(invoicedWithTax== null){
				invoicedWithTax=BigDecimal.ZERO;
			}
			if(paidToDate== null){
				paidToDate=BigDecimal.ZERO;
			}
			while(asalesIterator.hasNext()){
				Object[] aObj = (Object[])asalesIterator.next();
				if(aObj[3] != null){
					invoiced = invoiced.add((BigDecimal)aObj[3]);
					invoicedwofretax=invoicedwofretax.add((BigDecimal)aObj[3]);
				}
				if(aObj[2] != null )
					frieght = frieght.add((BigDecimal)aObj[2]);
				if(aObj[1] != null )
					itid = itid.add((BigDecimal)aObj[1]);
				if(aObj[0] != null )
					appliedAmount = appliedAmount.add((BigDecimal)aObj[0]);
				if(aObj[4] != null && actualCost!=null){
					actualCost = actualCost.add((BigDecimal)aObj[4]);
				}
				if(invoicedWithTax != null && invoiced != null && itid != null){
					if(aObj[3]==null){
						aObj[3]=0;
					}
					if(aObj[2]==null){
						aObj[2]=0;
					}
					if(aObj[1]==null){
						aObj[1]=0;
					}
					invoicedWithTax = invoicedWithTax.add(JobUtil.ConvertintoBigDecimal(aObj[3].toString())).add(JobUtil.ConvertintoBigDecimal(aObj[2].toString())).add(JobUtil.ConvertintoBigDecimal(aObj[1].toString()));
				}
				if(paidToDate != null && appliedAmount != null )
					paidToDate = paidToDate.add(appliedAmount);
			}
			
			/* To calculate the puchase order and vendor invoice total*/
			/*String queryTocalcPOTotal = "SELECT Sum( (CASE WHEN POAmount>0 AND POAmount>BillAmount AND (TransactionStatus=1 OR ReleaseType=4)"
											+" THEN POAmount ELSE BillAmount END) ) AS Cost FROM (SELECT R.ReleaseType"
											+" , B.BillAmount, ifnull(P.Subtotal,0)+ifnull(P.TaxTotal,0)+ifnull(P.Freight,0) AS POAmount, P.TransactionStatus"
											+"  FROM joRelease AS R RIGHT JOIN ((SELECT vePOID, Sum(BillAmount) AS BillAmount FROM "
											+" (SELECT P.vePOID, B.BillAmount as BillAmount FROM (vePO AS P RIGHT JOIN veBill AS B ON P.vePOID = B.vePOID) "
											+" LEFT JOIN joRelease AS R ON P.joReleaseID = R.joReleaseID WHERE B.TransactionStatus>0 And R.joMasterID = " + JoMasterID +" "
											+" UNION ALL SELECT P.vePOID, 0 as BillAmount FROM vePO as P LEFT JOIN joRelease AS R ON P.joReleaseID = R.joReleaseID" 
											+" WHERE P.TransactionStatus>0 And R.joMasterID = " + JoMasterID +" ) AS BILLZ GROUP BY vePOID) AS B LEFT JOIN vePO AS P" 
											+" ON B.vePOID = P.vePOID) ON R.joReleaseID = P.joReleaseID WHERE R.joMasterID=" + JoMasterID +") AS Temp";*/
			queryTocalcPOTotal = "SELECT Sum((CASE WHEN POAmount>0 AND POAmount>BillAmount AND (TransactionStatus=1 OR ReleaseType=1 OR ReleaseType=4)"
					+" THEN POAmount ELSE BillAmount END) ) AS Cost FROM (SELECT R.ReleaseType"
					+" , B.BillAmount, ifnull(P.Subtotal,0)+ifnull(P.TaxTotal,0)+ifnull(P.Freight,0) AS POAmount, P.TransactionStatus"
					+"  FROM joRelease AS R RIGHT JOIN ((SELECT vePOID, Sum(BillAmount) AS BillAmount FROM "
					+" (SELECT P.vePOID, (IFNULL(B.BillAmount,0)+IFNULL(B.AdditionalFreight,0))  as BillAmount FROM (vePO AS P RIGHT JOIN veBill AS B ON P.vePOID = B.vePOID) "
					+" LEFT JOIN joRelease AS R ON P.joReleaseID = R.joReleaseID WHERE B.TransactionStatus>0 And R.joMasterID = " + JoMasterID +" "
					+" ) AS BILLZ GROUP BY vePOID) AS B LEFT JOIN vePO AS P" 
					+" ON B.vePOID = P.vePOID) ON R.joReleaseID = P.joReleaseID WHERE R.joMasterID=" + JoMasterID +") AS Temp";
			Query aQuery4= joReleaseDetailSession.createSQLQuery(queryTocalcPOTotal);
			Object lstActualCost4 = aQuery4.uniqueResult();
			/*if (!aQuery4.list().isEmpty() && aQuery4.uniqueResult() != null && actualCost != null) {
				actualCost = actualCost.add((BigDecimal)aQuery4.list().get(0));
			}*/
			if (lstActualCost4 != null && actualCost != null) {
				actualCost = actualCost.add((BigDecimal)lstActualCost4);
			}
			
			
			/* To calculate current commission profits */
			queryTocalcommissions = "SELECT SUM( (CASE WHEN CommissionReceived=1 THEN 0 ELSE EstimatedBilling END) ) AS cpo"
											+" , SUM( (CASE WHEN CommissionReceived=1 THEN EstimatedBilling ELSE 0 END) ) AS TEstimatedBilling"
											+" , SUM( (CASE WHEN CommissionReceived=1 THEN CommissionAmount ELSE 0 END) ) AS TCommissionAmount "
											+" FROM joRelease WHERE ReleaseType=4 AND joMasterID=" + JoMasterID +"";
			Query aQuery5 = joReleaseDetailSession.createSQLQuery(queryTocalcommissions);
			Iterator<?> aCommissionIterator = aQuery5.list().iterator();
			while(aCommissionIterator.hasNext()){
				Object[] aObject = (Object[]) aCommissionIterator.next();
				if(aObject[2] != null)
					currentCommissionProfit = (BigDecimal)aObject[2];
				if(aObject[1] != null && invoiced != null)
					invoiced = invoiced.add((BigDecimal)aObject[1]);
				if(aObject[0] != null)
					cpo = (BigDecimal)aObject[0];
			}
			Jomaster aJomaster = (Jomaster)joReleaseDetailSession.get(Jomaster.class,JoMasterID);
			if(aJomaster!=null){
				if(aJomaster.getPriorRevenue() != null && invoiced != null)
					invoiced = invoiced.add(aJomaster.getPriorRevenue());
				if(aJomaster.getPriorCost() != null && actualCost != null)
					actualCost = actualCost.add(aJomaster.getPriorCost());
				if(aJomaster.getContractAmount() != null)
					contractAmount = aJomaster.getContractAmount()==null?new BigDecimal("0.0000"):aJomaster.getContractAmount();
			}
			/*get bill total amount*/
			queryToGetbillCost ="SELECT sum(ExpenseAmount) FROM veBillDistribution WHERE joMasterID = " + JoMasterID +" GROUP BY joMasterID ";
			Query aQuery6 = joReleaseDetailSession.createSQLQuery(queryToGetbillCost);
			/*if(!aQuery6.list().isEmpty() && aQuery6.uniqueResult() != null && actualCost!= null){
				actualCost = actualCost.add((BigDecimal)aQuery6.uniqueResult()); Always return unique value.
			}*/
			Object lstActualCost6 = aQuery6.uniqueResult();
			if(lstActualCost6 != null && actualCost!= null){
				actualCost = actualCost.add((BigDecimal)lstActualCost6);  //Always return unique value.
			}
			
			/* Get billing reminder from change order */
			if(invoicedWithTax != null && appliedAmount != null){
				appliedAmount_new = appliedAmount;
				accountRecievable = (invoiced).subtract(appliedAmount);
			}
			queryToGetjoChangeTotal = "select sum(IFNULL(ChangeAmount,0.0000)) from joChange where joMasterID =" + JoMasterID;
			Query aQuery7 = joReleaseDetailSession.createSQLQuery(queryToGetjoChangeTotal);
			/*if (!aQuery7.list().isEmpty() && aQuery7.uniqueResult() != null) {
				changeOrder = (BigDecimal)aQuery7.list().get(0);
				if(contractAmount != null)
					revisedContract = contractAmount.add((BigDecimal)aQuery7.list().get(0));
				if(revisedContract != null && invoiced != null && frieght != null)
					billingReminder = (revisedContract.subtract(invoiced)).add(frieght);
			}*/
			Object objChangeAmt = aQuery7.uniqueResult();
			if (objChangeAmt != null) {
				changeOrder = (BigDecimal)objChangeAmt;
				if(contractAmount != null)
					revisedContract = contractAmount.add((BigDecimal)objChangeAmt);
				if(revisedContract != null && invoiced != null && frieght != null)
					billingReminder = (revisedContract.subtract(invoiced)).add(frieght);
			} else {
				revisedContract = contractAmount;
				if(invoiced != null && revisedContract != null && frieght != null)
					billingReminder = (revisedContract.subtract(invoiced)).add(frieght);
				else if(invoiced != null && revisedContract != null )
					billingReminder = revisedContract.subtract(invoiced);
			}
			getTaxRate = "select CAST(TaxRate AS CHAR) as TaxRate from coTaxTerritory where coTaxTerritoryID = (select coTaxTerritoryID from joMaster where joMasterID = " + JoMasterID +");";
			
			String taxR = (String)joReleaseDetailSession.createSQLQuery(getTaxRate).uniqueResult();
			BigDecimal taxRate = new BigDecimal(taxR==null?"0":taxR);
			if (taxRate != null ) {
				taxRate = (taxRate.multiply(new BigDecimal(0.01)));
			}
			/*closeOut = accountRecievable;
			if(billingReminder != null && estimatedTax != null && accountRecievable != null)
				closeOut = accountRecievable.add(revisedContract.subtract(invoicedwofretax)).add(estimatedTax);*/
			if(currentCommissionProfit != null  && actualCost != null) {
				actualCost = actualCost.subtract(currentCommissionProfit);
			}
/*	Commented on 11/06/2015
 * by velmurugan,jenith		
 * actualProfit = invoiced;
			if(actualCost != null && invoiced != null) {
				actualProfit = invoiced.subtract(actualCost);
			}*/
			
			/*actProfitQuery = "SELECT SUM(IFNULL((C.InvoiceAmount-C.TaxAmount),0.0000)-IFNULL(((IFNULL(V.BillAmount,0)+IFNULL(V.AdditionalFreight,0))-V.TaxAmount),0.0000)) AS cuin "
					+ "FROM joReleaseDetail D RIGHT JOIN joRelease AS R ON R.joReleaseID = D.joReleaseID  LEFT JOIN veBill AS V"
					+ " ON V.joReleaseDetailID = D.joReleaseDetailID LEFT JOIN cuInvoice AS C ON"
					+ " C.joReleaseDetailID = D.joReleaseDetailID WHERE R.ReleaseType=1 AND D.joMasterID="+JoMasterID;*/
			
			actProfitQuery = "SELECT SUM((IFNULL(C.InvoiceAmount,0)-IFNULL(C.TaxAmount,0)))-SUM((IFNULL(V.BillAmount,0))-IFNULL(V.TaxAmount,0)) AS cuin  "
					+ "FROM joReleaseDetail D RIGHT JOIN joRelease AS R ON R.joReleaseID = D.joReleaseID  LEFT JOIN veBill AS V"
					+ " ON V.joReleaseDetailID = D.joReleaseDetailID LEFT JOIN cuInvoice AS C ON"
					+ " C.joReleaseDetailID = D.joReleaseDetailID WHERE R.ReleaseType=1 AND D.joMasterID="+JoMasterID;
			
			Query aQueryActPro= joReleaseDetailSession.createSQLQuery(actProfitQuery);
			/*if (!aQueryActPro.list().isEmpty() && aQueryActPro.uniqueResult() != null) {
				actualProfit = (BigDecimal)aQueryActPro.list().get(0);
			}*/
			System.out.println("ActualProfitQuery===>"+actProfitQuery);
			Object objActPro = aQueryActPro.uniqueResult();
			if (objActPro != null) {
				actualProfit = (BigDecimal)objActPro;
			}

			/*actProfitQuery2 = "SELECT (SUM(IFNULL((I.InvoiceAmount-I.TaxAmount),0.0000)) -SUM(IFNULL((I.whseCostTotal),0.0000)+IFNULL(I.FreightCost,0.0000))) AS SumOfSubtotal "
					+ "FROM joReleaseDetail D RIGHT JOIN joRelease AS R ON R.joReleaseID = D.joReleaseID LEFT JOIN cuInvoice I "
					+ "ON I.joReleaseDetailID = D.joReleaseDetailID WHERE R.joMasterID= "+JoMasterID
					+ " AND (R.ReleaseType = 2 OR R.ReleaseType = 3 OR R.ReleaseType =5)";*/
			actProfitQuery2 = "SELECT (SUM(IFNULL((I.InvoiceAmount-I.TaxAmount),0.0000)) -SUM(IFNULL((cuso.CostTotal),0.0000))) AS SumOfSubtotal "
					+ "FROM joReleaseDetail D RIGHT JOIN joRelease AS R ON R.joReleaseID = D.joReleaseID LEFT JOIN cuInvoice I "
					+ "ON I.joReleaseDetailID = D.joReleaseDetailID LEFT JOIN cuSO cuso ON I.cuSOID= cuso.cuSOID WHERE R.joMasterID= "+JoMasterID
					+ " AND (R.ReleaseType = 2 OR R.ReleaseType = 3 OR R.ReleaseType =5)";
			actualProfit = actualProfit==null?new BigDecimal("0.0000"):actualProfit;
			Query aQueryActPro2= joReleaseDetailSession.createSQLQuery(actProfitQuery2);
			/*if (!aQueryActPro2.list().isEmpty() && aQueryActPro2.uniqueResult() != null) {
				actualProfit = actualProfit.add((BigDecimal)aQueryActPro2.list().get(0)==null?new BigDecimal("0.0000"):(BigDecimal)aQueryActPro2.list().get(0));
			}*/
			
			Object objActPro2 = aQueryActPro2.uniqueResult();
			if (objActPro2 != null) {
				actualProfit = actualProfit.add((BigDecimal)objActPro2==null?new BigDecimal("0.0000"):(BigDecimal)objActPro2);
			}
			
			if(aJomaster!=null){
			estimatedCost = aJomaster.getEstimatedCost();
			estimatedProfit = aJomaster.getEstimatedProfit();
			}
			int rxmasterid=0;
			if(aJomaster!=null){
				rxmasterid=aJomaster.getRxCustomerId()==null?0:aJomaster.getRxCustomerId();
			}
			/* Query to get the customers overall due in the next 30, 60 and 90 and a sum of all.*/
//			DateFormat inputDF  = new SimpleDateFormat("yyyy/MM/dd");
//			Date date =new Date();
//			invoiceDate = inputDF.format(date);
			 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			    Calendar cal = Calendar.getInstance();
			    invoiceDate=dateFormat.format(cal.getTime());
		/*	customerOverallQuery = "SELECT SUM( (CASE WHEN Days<=30 THEN Balance ELSE 0 END) ) AS AmtCur"
									      +" , SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ) AS Amt30 "
									      +" , SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ) AS Amt60"
									       +" , SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM" 
									      +" (SELECT InvoiceAmount-(AppliedAmount+DiscountAmt) AS Balance, DATEDIFF('"+invoiceDate+"',InvoiceDate) AS Days" 
									      +" FROM cuInvoice WHERE (TransactionStatus>0) AND (abs(InvoiceAmount-(AppliedAmount+DiscountAmt)) > .02) "
									      +" AND (rxCustomerID="+rxmasterid+")) AS SubQuery";*/
			  		    
			    
			customerOverallQuery = "SELECT SUM( (CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END) ) AS AmtCur,"
					+ " SUM( (CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END) ) AS Amt30, "
					+ " SUM( (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END) ) AS Amt60, "
					+ " SUM( (CASE WHEN Days>90 THEN Balance ELSE 0 END) ) AS Amt90 FROM "
					+ " (SELECT InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance, DATEDIFF('"+invoiceDate+"',InvoiceDate) AS Days "
					+ " FROM cuInvoice cu"
					+ " LEFT JOIN cuMaster ON cuMaster.cuMasterID = cu.rxCustomerID "
					+ " WHERE cu.rxCustomerID = "+rxmasterid+" AND IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) "
					+ " AND (ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .01)"
					+ " UNION ALL"
					+ " (SELECT IFNULL(balance,0) - SUM(Amount) AS Balance ,DATEDIFF('"+invoiceDate+"',ReceiptDate) AS Days"
					+ " FROM cuReceipt cr LEFT JOIN (SELECT cuReceiptID,SUM(IFNULL(PaymentApplied,0)) AS balance FROM cuLinkageDetail"
					+ " WHERE rxCustomerID ="+rxmasterid+" GROUP BY cuReceiptID ORDER BY cuReceiptID) cdl ON cdl.cuReceiptID = cr.cuReceiptID"
					+ " WHERE cr.rxCustomerID  = "+rxmasterid+" GROUP BY cr.cuReceiptID HAVING Balance<0 ORDER BY cr.cuReceiptID,ReceiptDate)"
					+ ")  AS SubQuery"; 
			   
			   			    
			Query aQuery8 = joReleaseDetailSession.createSQLQuery(customerOverallQuery);
			Iterator<?> customeriterator = aQuery8.list().iterator();
			while (customeriterator.hasNext()) {
				Object[] aobj = (Object[]) customeriterator.next();
				if (aobj[0] != null) {
					amtCur = (BigDecimal) aobj[0];
				}
				if (aobj[1] != null) {
					trtyDays = (BigDecimal) aobj[1];
				}
				if (aobj[2] != null) {
					sixtyDays = (BigDecimal) aobj[2];
				}
				if (aobj[3] != null) {
					nintyDays = (BigDecimal) aobj[3];
				}
			}
			if (frieght == null) {
				frieght = new BigDecimal(0);
			}
			if (itid == null) {
				itid = new BigDecimal(0);
			}
			if (invoiced == null) {
				invoiced = new BigDecimal(0);
			}
			
			
			if(revisedContract!=null && invoicedwofretax!=null){
				billingReminder=revisedContract.subtract(invoicedwofretax);
			}
			
			if(billingReminder != null) {
				if (cpo != null) {
					estimatedTax = (billingReminder.subtract(cpo)).multiply(JobUtil.floorFigureoverall(taxRate,2));
				} else {
					estimatedTax = (billingReminder).multiply(JobUtil.floorFigureoverall(taxRate,2));
				}
			}
			//invoicedWithTax=invoiced;
			accountRecievable = invoicedWithTax.subtract(appliedAmount_new);
			closeOut = accountRecievable;
			if(billingReminder != null && estimatedTax != null && accountRecievable != null)
				closeOut = accountRecievable.add(revisedContract.subtract(invoicedwofretax)).add(estimatedTax);
			
			
			
			financeTabFields.put("actualCost", actualCost);
			financeTabFields.put("actualProfit", actualProfit);
			financeTabFields.put("estimatedCost", estimatedCost);
			financeTabFields.put("estimatedProfit", estimatedProfit);
			financeTabFields.put("closeOut", closeOut);
			financeTabFields.put("contractAmount", contractAmount);
			financeTabFields.put("revisedContract", revisedContract);
			financeTabFields.put("invoiced", invoicedwofretax);
			financeTabFields.put("billingReminder", billingReminder);
			financeTabFields.put("estimatedTax", estimatedTax);
			financeTabFields.put("invoicedWithTax", invoicedWithTax);
			financeTabFields.put("invoicedwofretax", invoicedwofretax);
			financeTabFields.put("paidToDate", appliedAmount_new);
			financeTabFields.put("AR", accountRecievable);
			financeTabFields.put("changeOrder", changeOrder);
			financeTabFields.put("trtyDays", trtyDays);
			financeTabFields.put("sixtyDays", sixtyDays);
			financeTabFields.put("nintyDays", nintyDays);
			financeTabFields.put("amtCur", amtCur);
			
			
			if(aJomaster!=null){
				QuoteNumber=aJomaster.getQuoteNumber();
			}
			financeTabFields.put("quoteNo", QuoteNumber);
			/*Iterator it = financeTabFields.entrySet().iterator();
			System.out.println(itid+"freight :: "+frieght);
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
		    }*/
		} catch (Exception je) {										
			itsLogger.error(je.getMessage(), je);
			JobException aJobException = new JobException(je.getMessage(), je);
			throw aJobException;
		} finally {
			joReleaseDetailSession.flush();
			joReleaseDetailSession.close();   
			
			queryToMakePaymentDateNull= null; 
			queryToGetJobAmount= null;
			queryToCalcWageCost= null;
			queryToCalculateSalesOrderAmount= null;
			queryTocalcPOTotal= null;
			queryTocalcommissions= null;
			queryToGetbillCost= null;
			queryToGetjoChangeTotal= null;
			getTaxRate= null;
			actProfitQuery= null;
			actProfitQuery2= null;
			invoiceDate= null;
			customerOverallQuery= null;
			QuoteNumber = null;
		}
		return financeTabFields;
	}
	
	@Override
	public ArrayList<JoFinancialReportTemp> getFinanceReportTemp(Integer JoMasterID) throws JobException {
		Session releaseInvoiceSession  = null;
		String invoiceQuery = null;
		Transaction aTransaction = null;
		JoFinancialReportTemp aJoFinancialReportTemp = null; 
		ArrayList<JoFinancialReportTemp> JoFinancialReportTempList = new ArrayList<JoFinancialReportTemp>();
		try {
			releaseInvoiceSession = itsSessionFactory.openSession();
			invoiceQuery = "SELECT joRelease.joReleaseID,joRelease.ReleaseDate,(CASE ReleaseType WHEN  1 THEN "
					+ "(SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = vePO.rxVendorID) "
					+ "WHEN  2 THEN IFNULL((SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = cuSO.rxCustomerID), "
					+ "(SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = cuInvoice.rxCustomerID)) "
					+ "WHEN  3 THEN IFNULL((SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = cuSO.rxCustomerID), "
					+ "(SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = cuInvoice.rxCustomerID)) WHEN  4 THEN "
					+ "(SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = vePO.rxVendorID) "
					+ "WHEN  5 THEN IFNULL((SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = cuSO.rxCustomerID), "
					+ "(SELECT rxMaster.Name FROM rxMaster WHERE rxMaster.rxMasterID = cuInvoice.rxCustomerID)) END ) AS NAME, "
					+ "(CASE ReleaseType WHEN  1 THEN 'Drop Ship' WHEN  2 THEN 'Stock Order' WHEN  3 THEN 'Bill Only' "
					+ "WHEN  4 THEN 'Commission' WHEN  5 THEN 'Service' WHEN ReleaseType IS NULL THEN 'Sales' END ) AS releaseType ,"
					+ "joRelease.ReleaseNote,(CASE ReleaseType "
					+ "WHEN  1 THEN TRUNCATE(IFNULL(vePO.Subtotal,0)+IFNULL(vePO.Freight,0)-IFNULL(joRelease.CommissionAmount,0.0000),2) "
					+ "WHEN  2 THEN TRUNCATE(IFNULL(cuSO.CostTotal,0.0000),2) "
					+ "WHEN  3 THEN TRUNCATE(IFNULL(cuSO.CostTotal,0.0000),2) "
					+ "WHEN  4 THEN TRUNCATE(IFNULL(vePO.Subtotal,0)+IFNULL(vePO.Freight,0)-IFNULL(joRelease.CommissionAmount,0.0000),2) "
					+ "WHEN  5 THEN TRUNCATE(IFNULL(cuSO.CostTotal,0.0000),2) END )AS POcost ,vePO.AcknowledgementDate,"
					+ "vePO.EstimatedShipDate ,joReleaseDetail.ShipDate ,vePO.VendorOrderNumber ,veBill.InvoiceNumber AS VendorInvoiceNumber,"
					+ "TRUNCATE(IFNULL(veBill.BillAmount,0),2) AS InvoicedCost,TRUNCATE(IFNULL((cuInvoice.Freight+cuInvoice.FreightCost),0.0000)+(IFNULL(veBill.AdditionalFreight,0.0000)),2) AS "
					+ "otherfreight ,cuInvoice.InvoiceNumber AS CuInvoiceNumber ,TRUNCATE((IFNULL(cuInvoice.Subtotal+cuInvoice.Freight,0)),2) AS CustomeraAmount,"
					+ "TRUNCATE((IFNULL(IFNULL(cuInvoice.InvoiceAmount,0)-(IFNULL(cuInvoice.AppliedAmount,0)+IFNULL(cuInvoice.DiscountAmt,0)),0)),2) AS customerBalanece,"
					+ "((SELECT R.Name AS Customer FROM rxMaster AS R RIGHT JOIN joMaster AS J ON R.rxMasterID = J.rxCustomerID "
					+ "WHERE J.joMasterID="+JoMasterID+")) AS CustName,((SELECT J.JobNumber FROM rxMaster AS R "
					+ "RIGHT JOIN joMaster AS J ON R.rxMasterID = J.rxCustomerID WHERE J.joMasterID="+JoMasterID+")) AS JobNumber,"
					+ "((SELECT J.Description FROM rxMaster AS R RIGHT JOIN joMaster AS J ON R.rxMasterID = J.rxCustomerID "
					+ "WHERE J.joMasterID="+JoMasterID+")) AS Descriptio ,((SELECT J.CustomerPONumber AS Customer FROM rxMaster AS R "
					+ "RIGHT JOIN joMaster AS J ON R.rxMasterID = J.rxCustomerID WHERE J.joMasterID="+JoMasterID+")) AS PO, "
					+ "joRelease.seq_Number,joRelease.joMasterID FROM joRelease LEFT JOIN vePO "
					+ "ON joRelease.joReleaseID = vePO.joReleaseID LEFT JOIN joReleaseDetail ON joRelease.joReleaseID = joReleaseDetail.joReleaseID "
					+ "LEFT JOIN cuInvoice ON cuInvoice.joReleaseDetailID=joReleaseDetail.joReleaseDetailID LEFT JOIN veBill ON veBill.joReleaseDetailID = joReleaseDetail.joReleaseDetailID "
					+ "LEFT JOIN cuSO ON joRelease.joReleaseID = cuSO.joReleaseID WHERE joRelease.joMasterID = "+JoMasterID+" ORDER BY joRelease.joReleaseID";
			Query aQuery = releaseInvoiceSession.createSQLQuery(invoiceQuery);
			Iterator<?> asalesIterator = aQuery.list().iterator();
			while(asalesIterator.hasNext()){
				Object[] aObj = (Object[])asalesIterator.next();
				aJoFinancialReportTemp = new JoFinancialReportTemp();
				aJoFinancialReportTemp.setJoReleaseID((Integer) aObj[0]);
				aJoFinancialReportTemp.setFrtReleaseDate((Date) aObj[1]);
				aJoFinancialReportTemp.setName((String) aObj[2]);
				aJoFinancialReportTemp.setFrtReleaseType((String) aObj[3]);
				aJoFinancialReportTemp.setFrtReleaseNote((String) aObj[4]);
				aJoFinancialReportTemp.setFrtpoCost((BigDecimal) aObj[5]);
				aJoFinancialReportTemp.setFrtAcknowledgementDate((Date) aObj[6]);
				aJoFinancialReportTemp.setFrtEstimatedShipDate((Date) aObj[7]);
				aJoFinancialReportTemp.setFrtShipDate((Date) aObj[8]);
				aJoFinancialReportTemp.setFrtVendorOrderNumber((String) aObj[9]);
				aJoFinancialReportTemp.setFrtVendorInvoiceNumber((String) aObj[10]);
				aJoFinancialReportTemp.setFrtInvoicedCost((BigDecimal) aObj[11]);
				aJoFinancialReportTemp.setFrtOtherFreight((BigDecimal) aObj[12]);
				aJoFinancialReportTemp.setFrtCuInvoiceNumber((String) aObj[13]);
				aJoFinancialReportTemp.setFrtCustomerAmount((BigDecimal) aObj[14]);
				aJoFinancialReportTemp.setFrtCustomerBalance((BigDecimal) aObj[15]);
				aJoFinancialReportTemp.setFrtCustomerName((String) aObj[16]);
				aJoFinancialReportTemp.setFrtjobNumber((String) aObj[17]);
				aJoFinancialReportTemp.setFrtDescription((String) aObj[18]);
				aJoFinancialReportTemp.setFrtPO((String) aObj[19]);
				aJoFinancialReportTemp.setFrtReleaseSeq(JobUtil.IntToLetter((Integer) aObj[20]).toUpperCase());
				aJoFinancialReportTemp.setFrtseq_Number((Integer) aObj[20]);
				aJoFinancialReportTemp.setJoMasterID((Integer) aObj[21]);
				JoFinancialReportTempList.add(aJoFinancialReportTemp);
			}
			aTransaction = releaseInvoiceSession.beginTransaction();
			String deletePreviousDatas = "DELETE FROM joFinancialReportTemp WHERE joMasterID = "+JoMasterID;

			aTransaction.begin();
			releaseInvoiceSession.createSQLQuery(deletePreviousDatas).executeUpdate();
			aTransaction.commit();
			
			if(JoFinancialReportTempList.size()>0){
				for(int i=0;i<JoFinancialReportTempList.size();i++){
					
					JoFinancialReportTemp theJoFinancialReportTemp = new JoFinancialReportTemp();
						theJoFinancialReportTemp= JoFinancialReportTempList.get(i);
						if(i>0){
							System.out.println(JoFinancialReportTempList.get(i-1).getJoReleaseID() +"===="+JoFinancialReportTempList.get(i).getJoReleaseID());
							if(JoFinancialReportTempList.get(i-1).getJoReleaseID().equals(JoFinancialReportTempList.get(i).getJoReleaseID())
									&& JoFinancialReportTempList.get(i-1).getFrtReleaseType().equals(JoFinancialReportTempList.get(i).getFrtReleaseType())){
								theJoFinancialReportTemp.setFrtpoCost(new BigDecimal("0.0000"));
							}
						}
						if(theJoFinancialReportTemp.getFrtCuInvoiceNumber()!=null){
						String substring = theJoFinancialReportTemp.getFrtCuInvoiceNumber().substring(Math.max(theJoFinancialReportTemp.getFrtCuInvoiceNumber().length() - 2, 0));
						theJoFinancialReportTemp.setFrtCuInvoiceNumber(substring);
						}
						aTransaction.begin();
						theJoFinancialReportTemp.setJoFinancialReportTempID(null);
						releaseInvoiceSession.save(theJoFinancialReportTemp);
						aTransaction.commit();
				}
			}
			
		} catch (Exception je) {										
			itsLogger.error(je.getMessage(), je);
			JobException aJobException = new JobException(je.getMessage(), je);
			throw aJobException;
		} finally {
			releaseInvoiceSession.flush();
			releaseInvoiceSession.close();  
		}
		return JoFinancialReportTempList;
	}
	
	@Override
	public Integer getInvoiceCounts(Integer JoMasterID) throws JobException {
		Session releaseInvoiceSession = null;
		String invoiceQuery = null;
		BigInteger invoiceCount = new BigInteger("0");
		try {
			releaseInvoiceSession = itsSessionFactory.openSession();
			invoiceQuery = "SELECT COUNT(*) FROM joRelease WHERE joReleaseID IN (SELECT joReleaseID FROM joRelease WHERE joMasterID =" + JoMasterID+")";
			Query aQuery = releaseInvoiceSession.createSQLQuery(invoiceQuery);
			invoiceCount = (BigInteger) aQuery.uniqueResult();
		} catch (Exception je) {										
			itsLogger.error(je.getMessage(), je);
			JobException aJobException = new JobException(je.getMessage(), je);
			throw aJobException;
		} finally {
			releaseInvoiceSession.flush();
			releaseInvoiceSession.close();  
		}
		return invoiceCount.intValue();
	}
	
}
