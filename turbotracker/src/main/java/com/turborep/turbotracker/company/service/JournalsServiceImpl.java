package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.journalentryhistory;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgerdetail;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.SimpleCompanyBean;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.util.JobUtil;

@Transactional
@Service("journalsService")
public class JournalsServiceImpl implements JournalsService {
Logger itsLogger = Logger.getLogger(CompanyServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;	
	
	@Resource(name = "sysService")
	private SysService itsSysService;
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	
	
	@Override
	public List<?> getJournalsDetails() throws CompanyException {
		itsLogger.debug("Retrieving Journal details List");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory1 = 1 AND Name IS NOT null ORDER BY Name ASC";
		Session aSession = null;Query aQuery = null;
		List<SimpleCompanyBean> aQueryList = new ArrayList<SimpleCompanyBean>();
		try{
			SimpleCompanyBean aCompanyBean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aCompanyBean = new SimpleCompanyBean();
				Object[] aObj = (Object[])aIterator.next();
				if (aObj[0] != null) {
					aCompanyBean.setPkId((Integer)aObj[0]);					/** PKey ID */
					aCompanyBean.setCompanyName((String)aObj[1]);			/** RolodexName	*/
				}
				aQueryList.add(aCompanyBean);
			}
		}catch(Exception e){
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		}finally{
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;aQuery=null;
		}
		return aQueryList;
	}

	@Override
	public List<GlTransaction> getTotalJournals(Integer from ,Integer to) throws CompanyException {
		/*String aSelectAccountsQry = "SELECT * FROM glTransaction WHERE journalId='JE' GROUP BY reference ORDER BY entrydate DESC";*/
		//String aSelectAccountsQry ="SELECT * FROM glTransaction gl WHERE journalId='JE' AND glTransactionId IN(SELECT MAX(glTransactionId) FROM glTransaction WHERE reference=gl.reference) ORDER BY glTransactionId DESC";
		String aSelectAccountsQry ="SELECT gt.* FROM glTransaction gt WHERE gt.journalId='JE' and (gt.debit-gt.credit<>0) GROUP BY gt.reference HAVING COUNT(gt.reference)>=1 ORDER BY glTransactionId DESC limit "+from+","+to+";";
		ArrayList<GlTransaction> aListofJournals = new ArrayList<GlTransaction>();
		Session aSession = null;Query aQuery =null;
		
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				GlTransaction aGlTransaction = new GlTransaction();
					Object[] aObj = (Object[])aIterator.next();
					aGlTransaction.setGlTransactionId((Integer) aObj[0]);
					aGlTransaction.setCoFiscalPeriodId((Integer) aObj[1]);
					aGlTransaction.setPeriod((Integer) aObj[2]);
					aGlTransaction.setpStartDate((Date) aObj[3]);
					aGlTransaction.setpEndDate((Date) aObj[4]);
					aGlTransaction.setCoFiscalYearId((Integer) aObj[5]);
					aGlTransaction.setFyear((String) aObj[6]);
					aGlTransaction.setyStartDate((Date) aObj[7]);
					aGlTransaction.setyEndDate((Date) aObj[8]);
					aGlTransaction.setJournalId((String) aObj[9]);
					aGlTransaction.setJournalDesc((String) aObj[10]);
					Timestamp stamp = null;
					java.sql.Date date = null;
					if(aObj[11] != null)
					{
					stamp = (Timestamp)aObj[11];
					date = new java.sql.Date(stamp.getTime());
					aGlTransaction.setEntrydate(date);
					}
					else
					{
					aGlTransaction.setEntrydate(date);
					}

					aGlTransaction.setEnteredBy((String) aObj[12]);
					aGlTransaction.setCoAccountId((Integer) aObj[13]);
					aGlTransaction.setCoAccountNumber((String) aObj[14]);
					aGlTransaction.setPoNumber((String) aObj[15]);
					aGlTransaction.setTransactionDesc((String) aObj[16]);
					
					Timestamp stamp1 = null;
					java.sql.Date date1 = null;
					if(aObj[17] != null)
					{
					stamp1 = (Timestamp)aObj[17];
					date1 = new java.sql.Date(stamp1.getTime());
					aGlTransaction.setTransactionDate(date1);
					}
					else{
					aGlTransaction.setTransactionDate(date1);
					}
					aGlTransaction.setDebit((BigDecimal) aObj[18]);
					aGlTransaction.setCredit((BigDecimal) aObj[19]);
					aListofJournals.add(aGlTransaction);
				}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry =null;aQuery =null;
		}
			
		return aListofJournals;
		
	}
	public Boolean checkReferenceNumberInGlT(String ponumber){
		boolean returnboolean=false;
		String aSalesselectQry = "SELECT reference,glTransactionId from glTransaction where reference='"+ponumber+"'";
		Session aSession = null;Query aQuery = null;
		List<SimpleCompanyBean> aQueryList = new ArrayList<SimpleCompanyBean>();
		try{
			SimpleCompanyBean aCompanyBean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			if(aQuery.list()!=null){
				returnboolean=true;
			}
			
		}catch(Exception e){
			itsLogger.error(e.getMessage(),e);
		}finally{
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;aQuery=null;
		}
		return returnboolean;
	}

	@Override
	public GlTransaction updateJournalEntries(GlTransaction aGlTransaction,int gllinkagestatus,String oper) throws CompanyException {
		Session aSession = null;
		Integer headerID = 0;
		try{
			aSession = itsSessionFactory.openSession();
			if(aGlTransaction.getJereference()==null&&!(oper.equals("edit")) && !(oper.equals("delete"))){
				boolean checkrefnumber=checkReferenceNumberInGlT(aGlTransaction.getPoNumber());
				if(checkrefnumber){
					Integer jeseqnumber=itsSysService.getSequenceNumber("JournalEntry");
					aGlTransaction.setPoNumber(String.valueOf(jeseqnumber));
				}
			}
			
			//if(Obj.getGlTransactionId() != null) {
				
				/*
				GlTransaction aGlTransaction = (GlTransaction) aSession.get(GlTransaction.class, Obj.getGlTransactionId());
				aGlTransaction.setTransactionDesc(Obj.getTransactionDesc());
				aGlTransaction.setPoNumber(Obj.getPoNumber());
				aGlTransaction.setCoFiscalPeriodId(Obj.getCoFiscalPeriodId());
				aGlTransaction.setPeriod(Obj.getPeriod());
				aGlTransaction.setpStartDate(Obj.getpStartDate());
				aGlTransaction.setpEndDate(Obj.getpEndDate());
				aGlTransaction.setCoFiscalYearId(Obj.getCoFiscalYearId());
				aGlTransaction.setFyear(Obj.getFyear());
				aGlTransaction.setyStartDate(Obj.getyStartDate());
				aGlTransaction.setyEndDate(Obj.getyEndDate());
				aGlTransaction.setJournalId(Obj.getJournalId());
				aGlTransaction.setJournalDesc(Obj.getJournalDesc());
				aGlTransaction.setEntrydate(Obj.getEntrydate());
				aGlTransaction.setCoAccountId(Obj.getCoAccountId());
				aGlTransaction.setCoAccountNumber(Obj.getCoAccountNumber());
				aGlTransaction.setCredit(Obj.getCredit());
				aGlTransaction.setDebit(Obj.getDebit());
				aSession.update(aGlTransaction);
				*/
				Transaction aTransaction;
		
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				headerID = (Integer)aSession.save(aGlTransaction);
				aTransaction.commit();
				
				try {
					if(aGlTransaction.getJereference()==null&&!(oper.equals("edit")) && !(oper.equals("delete"))){
					itsSysService.updateSequenceNumber("JournalEntry");
					}
				} catch (Exception e) {
				}
				
				GlLinkage glLinkage = new GlLinkage();
				glLinkage.setEntryDate(new Date());
				glLinkage.setStatus(gllinkagestatus);
				glLinkage.setCoLedgerSourceId(1300);
				//glLinkage.setCoLedgerSourceId(aGlTransaction.getJournalId());
				glLinkage.setGlTransactionId(headerID);
				if(aGlTransaction.getPoNumber()!=null && !aGlTransaction.getPoNumber().equals("")){
					glLinkage.setVeBillID(Integer.parseInt(aGlTransaction.getPoNumber()));
				}
				aSession.save(glLinkage);
				aGlTransaction.setGlTransactionId(headerID);
				itsLogger.info("Header ID: "+headerID);
				
			//}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aGlTransaction;
	}

	@Override
	public int deleterecord(String headerId) throws CompanyException {
		Session aSession = null;
		int res = 0;Query qry =null;
		try{
			aSession = itsSessionFactory.openSession();
			 qry = aSession.createQuery("delete from GlTransaction glt where glt.poNumber="+headerId);
	           // qry.setParameter("refers",headerId);
	            Transaction aTransaction =  aSession.beginTransaction();
				aTransaction.begin();
	            res = qry.executeUpdate();	
			   aTransaction.commit();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			qry =null;
		}
		return res;
	}
	
	@Override
	public int deleteGlTransactionRecord(String headerId) throws CompanyException {
		Session aSession = null;
		 int res = -1;
		 BigInteger aTotalCount = null;String aJobCountStr =null;
		try{
			aSession = itsSessionFactory.openSession();     
		 
			aJobCountStr = "SELECT COUNT(glTransactionId) AS count FROM glTransaction glt where glt.reference="+headerId;
		
				// Retrieve aSession from Hibernate
				Query aQuery = aSession.createSQLQuery(aJobCountStr);
				List<?> aList = aQuery.list();
				aTotalCount = (BigInteger) aList.get(0);
			
			if(!aTotalCount.equals(0)){
		     Query qry = aSession.createQuery("delete from GlTransaction glt where glt.poNumber="+headerId);
		           // qry.setParameter("refers",headerId);
		            Transaction aTransaction =  aSession.beginTransaction();
					aTransaction.begin();
		            res = qry.executeUpdate();	
				aTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr =null;
		}
		itsLogger.info("Delete Data: "+res);
		return res;
	}

	@Override
	public List<GlTransaction> getTotalJournalDetails(String referenceID) throws CompanyException {
		
		Coledgersource aColedgersource = new Coledgersource();
		try {
			aColedgersource = gltransactionService.getColedgersourceDetail( "JE");
		} catch (BankingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String aSelectAccountsQry ="";

		 aSelectAccountsQry = "SELECT glTransaction.*,coAccount.Description,glLinkage.status FROM glTransaction "
				+ " JOIN coAccount ON glTransaction.coAccountId = coAccount.coAccountId "
				+ " JOIN glLinkage ON(glLinkage.glTransactionId=glTransaction.glTransactionId) "
				+ " WHERE glLinkage.coLedgerSourceId="+aColedgersource.getCoLedgerSourceId()+" AND "
				+ " reference='"+referenceID+"'  AND STATUS=0 ORDER BY glTransactionID ASC ";
				
		
		ArrayList<GlTransaction> aListofJournals = new ArrayList<GlTransaction>();
		Session aSession = null;
		Query aQuery =null;
		try{
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				GlTransaction aGlTransaction = new GlTransaction();
					Object[] aObj = (Object[])aIterator.next();
					aGlTransaction.setGlTransactionId((Integer) aObj[0]);
					aGlTransaction.setCoFiscalPeriodId((Integer) aObj[1]);
					aGlTransaction.setPeriod((Integer) aObj[2]);
					aGlTransaction.setpStartDate((Date) aObj[3]);
					aGlTransaction.setpEndDate((Date) aObj[4]);
					aGlTransaction.setCoFiscalYearId((Integer) aObj[5]);
					aGlTransaction.setFyear((String) aObj[6]);
					aGlTransaction.setyStartDate((Date) aObj[7]);
					aGlTransaction.setyEndDate((Date) aObj[8]);
					aGlTransaction.setJournalId((String) aObj[9]);
					aGlTransaction.setJournalDesc((String) aObj[10]);
					aGlTransaction.setStatus((Integer)aObj[21]);
					aGlTransaction.setHiddenstatus((Integer)aObj[21]);
					Timestamp stamp = null;
					java.sql.Date date = null;
					if(aObj[11] != null)
						{
						stamp = (Timestamp)aObj[11];
						date = new java.sql.Date(stamp.getTime());
						aGlTransaction.setEntrydate(date);
						}
					else{
						aGlTransaction.setEntrydate(date);
						}

					aGlTransaction.setEnteredBy((String) aObj[12]);
					aGlTransaction.setCoAccountId((Integer) aObj[13]);
					aGlTransaction.setCoAccountNumber((String) aObj[14]);
					aGlTransaction.setPoNumber((String) aObj[15]);
					aGlTransaction.setTransactionDesc((String) aObj[16]);
					
					Timestamp stamp1 = null;
					java.sql.Date date1 = null;
					if(aObj[17] != null)
						{
						stamp1 = (Timestamp)aObj[17];
						date1 = new java.sql.Date(stamp1.getTime());
						aGlTransaction.setTransactionDate(date1);
						}
					else{
						aGlTransaction.setTransactionDate(date1);
						}
					
					aGlTransaction.setDebit(JobUtil.floorFigureoverall(((BigDecimal) aObj[18]),2));
					aGlTransaction.setCredit(JobUtil.floorFigureoverall(((BigDecimal) aObj[19]),2));
					aGlTransaction.setCoAccountDesc((String) aObj[20]);
					
					aGlTransaction.setOldcoAccountId((Integer) aObj[13]);
					aGlTransaction.setOldcredit(JobUtil.floorFigureoverall(((BigDecimal) aObj[19]),2));
					aGlTransaction.setOlddebit(JobUtil.floorFigureoverall(((BigDecimal) aObj[18]),2));
					aGlTransaction.setOldcoAccountNumber((String) aObj[14]);
					
					aListofJournals.add(aGlTransaction);
				}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry =null;aQuery =null;
		}
		
		aSelectAccountsQry = "SELECT glTransaction.*,coAccount.Description,glLinkage.status FROM glTransaction "
				+ " JOIN coAccount ON glTransaction.coAccountId = coAccount.coAccountId "
				+ " JOIN glLinkage ON(glLinkage.glTransactionId=glTransaction.glTransactionId) "
				+ " WHERE glLinkage.coLedgerSourceId="+aColedgersource.getCoLedgerSourceId()+" AND "
				+ " reference='"+referenceID+"'  AND STATUS=1 ORDER BY glTransactionID DESC ";
		
		try{
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				GlTransaction aGlTransaction = new GlTransaction();
					Object[] aObj = (Object[])aIterator.next();
					aGlTransaction.setGlTransactionId((Integer) aObj[0]);
					aGlTransaction.setCoFiscalPeriodId((Integer) aObj[1]);
					aGlTransaction.setPeriod((Integer) aObj[2]);
					aGlTransaction.setpStartDate((Date) aObj[3]);
					aGlTransaction.setpEndDate((Date) aObj[4]);
					aGlTransaction.setCoFiscalYearId((Integer) aObj[5]);
					aGlTransaction.setFyear((String) aObj[6]);
					aGlTransaction.setyStartDate((Date) aObj[7]);
					aGlTransaction.setyEndDate((Date) aObj[8]);
					aGlTransaction.setJournalId((String) aObj[9]);
					aGlTransaction.setJournalDesc((String) aObj[10]);
					aGlTransaction.setStatus((Integer)aObj[21]);
					aGlTransaction.setHiddenstatus((Integer)aObj[21]);
					Timestamp stamp = null;
					java.sql.Date date = null;
					if(aObj[11] != null)
						{
						stamp = (Timestamp)aObj[11];
						date = new java.sql.Date(stamp.getTime());
						aGlTransaction.setEntrydate(date);
						}
					else{
						aGlTransaction.setEntrydate(date);
						}

					aGlTransaction.setEnteredBy((String) aObj[12]);
					aGlTransaction.setCoAccountId((Integer) aObj[13]);
					aGlTransaction.setCoAccountNumber((String) aObj[14]);
					aGlTransaction.setPoNumber((String) aObj[15]);
					aGlTransaction.setTransactionDesc((String) aObj[16]);
					
					Timestamp stamp1 = null;
					java.sql.Date date1 = null;
					if(aObj[17] != null)
						{
						stamp1 = (Timestamp)aObj[17];
						date1 = new java.sql.Date(stamp1.getTime());
						aGlTransaction.setTransactionDate(date1);
						}
					else{
						aGlTransaction.setTransactionDate(date1);
						}
					
					aGlTransaction.setDebit((BigDecimal) aObj[18]);
					aGlTransaction.setCredit((BigDecimal) aObj[19]);
					aGlTransaction.setCoAccountDesc((String) aObj[20]);
					
					aGlTransaction.setOldcoAccountId((Integer) aObj[13]);
					aGlTransaction.setOldcredit((BigDecimal) aObj[19]);
					aGlTransaction.setOlddebit((BigDecimal) aObj[18]);
					aGlTransaction.setOldcoAccountNumber((String) aObj[14]);
					
					aListofJournals.add(aGlTransaction);
				}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry =null;aQuery =null;
		}
			
		return aListofJournals;
	}

	@Override
	public void updateJournalDetails(Coaccount Obj, Coledgerdetail coledgerObj) throws CompanyException {
		Session aSession = null;
		try{
			aSession = itsSessionFactory.openSession();
			if(Obj.getCoAccountId() != null) {
				Coaccount aCoaccount = (Coaccount) aSession.get(Coaccount.class, Obj.getCoAccountId());
				if(aCoaccount != null) {
					aCoaccount.setDescription(Obj.getDescription());
					aCoaccount.setNumber(Obj.getNumber());
					aSession.update(aCoaccount);	
				}
			} else {
				/*bj.set);(true);
				Obj.setCoFiscalPeriodId(104);
				Obj.setCoLedgerSourceId(1300);
				aSession.save(Obj);*/
			}
			if(coledgerObj.getCoAccountId()!=null) {
				Coledgerdetail aCoLedgerDetail =(Coledgerdetail) aSession.get(Coledgerdetail.class, coledgerObj.getCoAccountId());
				if(aCoLedgerDetail != null) {
					aCoLedgerDetail.setCoAccountId(coledgerObj.getCoAccountId());
					aCoLedgerDetail.setCoLedgerHeaderId(coledgerObj.getCoLedgerHeaderId());
					aCoLedgerDetail.setAmount(coledgerObj.getAmount());
					aSession.update(aCoLedgerDetail);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public Integer CreateJournalEntriesHistory(journalentryhistory ajournalentryhistory) throws CompanyException {
		Session aSession = null;
		Integer rowID = 0;
		try{
			aSession = itsSessionFactory.openSession();
				Transaction aTransaction;
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				rowID = (Integer)aSession.save(ajournalentryhistory);
				aTransaction.commit();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return rowID;
	}
	
	@Override
	public List<journalentryhistory> getListOfJournalshistory(String reference) throws CompanyException {
		String aSelectAccountsQry = "SELECT * FROM journalentryhistory WHERE reference="+reference;
		ArrayList<journalentryhistory> aListofjournalentryhistory = new ArrayList<journalentryhistory>();
		Session aSession = null;Query aQuery =null;
		
		try{
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSelectAccountsQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				journalentryhistory ajournalentryhistory = new journalentryhistory();
					Object[] aObj = (Object[])aIterator.next();
					ajournalentryhistory.setReference((String) aObj[1]);
					ajournalentryhistory.setReasondesc((String) aObj[2]);
					Timestamp stamp = null;
					java.sql.Date date = null;
					if(aObj[3] != null)
					{
					stamp = (Timestamp)aObj[3];
					date = new java.sql.Date(stamp.getTime());
					ajournalentryhistory.setCreateddate(date);
					}else{
						ajournalentryhistory.setCreateddate((Date) aObj[3]);
					}
					
					ajournalentryhistory.setEditordelete((String) aObj[4]);
					aListofjournalentryhistory.add(ajournalentryhistory);
				}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry =null;aQuery =null;
		}
			
		return aListofjournalentryhistory;
		
	}
	
	@Override
	public int updateGlLinkage(String refId,int status) throws CompanyException {
		Session aSession = null;
		int res = 0; Query qry =null;
		try{
			aSession = itsSessionFactory.openSession();
			 qry = aSession.createSQLQuery("UPDATE glLinkage SET status='"+status+"' WHERE glTransactionId IN(SELECT glTransactionId FROM glTransaction WHERE reference='"+refId+"')");
	           // qry.setParameter("refers",headerId);
	            /*Transaction aTransaction =  aSession.beginTransaction();
				aTransaction.begin();*/
			 	qry.executeUpdate();
			  // aTransaction.commit();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			qry =null;
		}
		return 0;
	}

	@Override
	public Integer getJournalTotalCount() throws CompanyException {
		Session aSession = null;
		Integer rowCount = 0;
		 String aSelectAccountsQry ="SELECT gt.* FROM glTransaction gt WHERE gt.journalId='JE' and (gt.debit-gt.credit<>0) GROUP BY gt.reference HAVING COUNT(gt.reference)>=1 ORDER BY glTransactionId DESC";
		 Query qry =null;
		try{
			aSession = itsSessionFactory.openSession();
			 qry = aSession.createSQLQuery(aSelectAccountsQry);
			rowCount =qry.list().size();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSelectAccountsQry =null;qry =null;
		}
		return rowCount;
	}
	
	
	
	
	public ArrayList<GlTransaction> rollBackGlTransaction(GlRollback aGlRollback) throws BankingException, CompanyException
	{
		ArrayList<GlTransaction> newListGl=new ArrayList<GlTransaction>();
		itsLogger.info("Roll Back Executed In Journal Entry=="+aGlRollback.getVeBillID()+" "+aGlRollback.getCoLedgerSourceID());
		
		Session aSession1 = null;
		
		
		// For Getting New Period	
		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(aGlRollback.getPeriodID());
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(aGlRollback.getYearID());

		ArrayList<GlLinkage> arrayglLinkagelist= gltransactionService.getAllGlLinkageDetails(aGlRollback.getVeBillID(), aGlRollback.getCoLedgerSourceID());
		ArrayList<GlTransaction> arrayglTransList=new ArrayList<GlTransaction>();
	
		for(GlLinkage glLinkageObj:arrayglLinkagelist)
		{
			aSession1 = itsSessionFactory.openSession();
			
			arrayglTransList=gltransactionService.getAllglTransactionDetails(glLinkageObj.getGlTransactionId());
			
			for(GlTransaction glTransObj:arrayglTransList)
			{
				newListGl.add(glTransObj);
				
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
					   gltransactionService.insertglAprollBackInsert(glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID());
				   }
				   else
				   {
					   gltransactionService.insertglrollBackInsert( glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID());
				   }
		
			}
			
			
			String aQuery =null;
			try
			{
		//  aTransaction = aSession1.beginTransaction();
		//	aTransaction.begin();
			aQuery = "update glLinkage set status=1 where glTransactionId ="+glLinkageObj.getGlTransactionId();
			aSession1.createSQLQuery(aQuery).executeUpdate();
		//	aTransaction.commit();
			}
			catch(Exception excep){itsLogger.error(excep.getMessage(), excep);}
			finally
			{
				aSession1.flush();
				aSession1.close();
				aQuery =null;
			}
		
		}
		return newListGl;
	}
	public Integer AddnewJournalEntries(GlTransaction aGlTransaction,String operation) throws Exception{
	 Integer ReferenceNumber=JobUtil.ConvertintoInteger(aGlTransaction.getPoNumber());	
		if(aGlTransaction.getJereference()==null &&operation.equals("add")){
				boolean checkrefnumber=checkReferenceNumberInGlT(aGlTransaction.getPoNumber());
				if(checkrefnumber){
					ReferenceNumber=itsSysService.getSequenceNumber("JournalEntry");
					aGlTransaction.setPoNumber(String.valueOf(ReferenceNumber));
					itsSysService.updateSequenceNumber("JournalEntry");
				}
		}
		Integer glTransactionID=gltransactionService.saveGltransactionTable(aGlTransaction);
		GlLinkage gllink=new GlLinkage();
		gllink.setGlTransactionId(glTransactionID);
		gllink.setCoLedgerSourceId(1300);
		gllink.setEntryDate(new Date());
		gllink.setStatus(0);
		gllink.setVeBillID(ReferenceNumber);
		gltransactionService.saveGlLinkageTable(gllink);
		
		return ReferenceNumber;
	}
	/*public saveGlLinkageTable(GlLinkage glLinkageobj){
		
	}*/
	
}
