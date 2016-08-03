package com.turborep.turbotracker.finance.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.finance.exception.FinancePostingException;

@Service("financePostingService")
@Transactional
public class FinancePostingServiceImpl implements FinancePostingService  {
	public static Logger itsLogger = Logger.getLogger("service");
	@Resource(name="sessionFactory")
	public SessionFactory itsSessionFactory;

	@Override
	public Integer postVeBill(Integer veBillid, String tableColumn,String acType) throws FinancePostingException
	{
		Session aSession = null;
		Transaction aTransaction = null;StringBuffer insertVeBillPost = null;
		Integer glTransactionID=0;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			if(acType.equals("debit")){
			insertVeBillPost = new StringBuffer(
			"INSERT INTO glTransaction(")
			.append("coFiscalPeriodID,")
			.append("coLedgerSourceID,")
			.append("SourceNo,")
			.append("TransactionReferenceID,")
			.append("PostDate,")
			.append("Description,")
			.append("Reference,")
			.append("Posted,")
			.append("coAccountID,")
			.append("Amount)SELECT(SELECT CurrentPeriodID FROM sysInfo ORDER BY 1 DESC LIMIT 1) AS fiscalPeriod,")
			.append("(SELECT coLedgerSourceID FROM coLedgerSource WHERE JournalID='VB') AS sourceID,")
			.append("(SELECT coLedgerSourceID DIV 1000 AS SourceNO FROM coLedgerSource WHERE JournalID='VB') AS sourceno,")
			.append("veBillID,")
			.append("PostDate,")
			.append("'' AS description,")
			.append("(SELECT PONumber FROM vePO WHERE vePOID=veBill.vePOID) AS poNum,")
			.append("TransactionStatus AS posted ,")
			.append("(SELECT ").append(tableColumn).append(" FROM sysAccountLinkage) AS accountid ,")
			.append("CONCAT('-',BillAmount) ")
			.append("FROM veBill WHERE veBillID = ").append(veBillid).append(";");
			}
			else{

				insertVeBillPost = new StringBuffer(
				"INSERT INTO glTransaction(")
				.append("coFiscalPeriodID,")
				.append("coLedgerSourceID,")
				.append("SourceNo,")
				.append("TransactionReferenceID,")
				.append("PostDate,")
				.append("Description,")
				.append("Reference,")
				.append("Posted,")
				.append("coAccountID,")
				.append("Amount)SELECT(SELECT CurrentPeriodID FROM sysInfo ORDER BY 1 DESC LIMIT 1) AS fiscalPeriod,")
				.append("(SELECT coLedgerSourceID FROM coLedgerSource WHERE JournalID='VB') AS sourceID,")
				.append("(SELECT coLedgerSourceID DIV 1000 AS SourceNO FROM coLedgerSource WHERE JournalID='VB') AS sourceno,")
				.append("veBillID,")
				.append("PostDate,")
				.append("'' AS description,")
				.append("(SELECT PONumber FROM vePO WHERE vePOID=veBill.vePOID) AS poNum,")
				.append("TransactionStatus AS posted ,")
				.append("(SELECT ").append(tableColumn).append(" FROM sysAccountLinkage) AS accountid ,")
				.append("BillAmount  ")
				.append("FROM veBill WHERE veBillID = ").append(veBillid).append(";");
			}
			String aInsertglTransactionQuery = insertVeBillPost.toString();
			glTransactionID =(Integer)aSession.createSQLQuery(aInsertglTransactionQuery).executeUpdate();
			aTransaction.commit();
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			FinancePostingException aFinancePostingException = new FinancePostingException(excep.getMessage(),excep);
			throw aFinancePostingException;
		} finally {
			aSession.flush();
			aSession.close();
			insertVeBillPost = null;
		}
		return glTransactionID;
		
	}

}