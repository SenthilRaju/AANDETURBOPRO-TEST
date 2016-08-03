package com.turborep.turbotracker.system.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.Joquotecolumn;
import com.turborep.turbotracker.job.dao.JoquotecolumnId;
import com.turborep.turbotracker.job.dao.jocategory;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.Sysassignment;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.system.dao.Syssequence;
import com.turborep.turbotracker.user.dao.TsAccessModule;
import com.turborep.turbotracker.user.dao.TsUserGroup;
import com.turborep.turbotracker.user.dao.TsUserGroupLink;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.exception.UserException;

@Service("sysService")
@Transactional
public class SysServiceImpl implements SysService {

	Logger itsLogger = Logger.getLogger(SysServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public Integer getSysSequenceNumber(String theTableName) throws Exception {
		Integer aSysSequenceId;
		Integer aSequenceNumber = null;
		Session aSession = null;
		Transaction aTransaction = null;
		try{
			aSysSequenceId = getSysSeqenceDynamic(theTableName);
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			// Retrieve existing person via id
			Syssequence aExistingSysSeq = (Syssequence) aSession.get(Syssequence.class, aSysSequenceId);
			// Assign updated values to this person
			aSequenceNumber = aExistingSysSeq.getSequence();
			aSequenceNumber = aSequenceNumber + 1;
			aExistingSysSeq.setSequence(aSequenceNumber);
			//existingPerson.setMoney(person.getMoney());
			// Save updates
			aSession.saveOrUpdate(aExistingSysSeq);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aSequenceNumber;
	}
	
	@Override
	public Integer getSysSequenceNumberwithSO(String theTableName) throws Exception {
		Integer aSysSequenceId;
		Integer aSequenceNumber = null;
		Session aSession = null;
		Transaction aTransaction = null;
		try{
			aSysSequenceId = getSysSeqenceDynamic(theTableName);
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			// Retrieve existing person via id
			Syssequence aExistingSysSeq = (Syssequence) aSession.get(Syssequence.class, aSysSequenceId);
			// Assign updated values to this person
			aSequenceNumber = aExistingSysSeq.getSequence();
			aSequenceNumber = aSequenceNumber + 1;
		//	aExistingSysSeq.setSequence(aSequenceNumber);
			//existingPerson.setMoney(person.getMoney());
			// Save updates
		//	aSession.saveOrUpdate(aExistingSysSeq);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aSequenceNumber;
	}


	@Override
	public Integer getSequenceNumber(String theTableName) throws Exception {
		Integer aSysSequenceId;
		Integer aSequenceNumber = null;
		Session aSession = null;
		Transaction aTransaction = null;
		try{
			aSysSequenceId = getSysSeqenceDynamic(theTableName);
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			// Retrieve existing person via id
			Syssequence aExistingSysSeq = (Syssequence) aSession.get(Syssequence.class, aSysSequenceId);
			// Assign updated values to this person
			aSequenceNumber = aExistingSysSeq.getSequence();
			aSequenceNumber = aSequenceNumber + 1;
			//aExistingSysSeq.setSequence(aSequenceNumber);
			//existingPerson.setMoney(person.getMoney());
			// Save updates
			//aSession.saveOrUpdate(aExistingSysSeq);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aSequenceNumber;
	}
	
	
	
	@Override
	public Integer updateSequenceNumber(String theTableName) throws Exception {
		Integer aSysSequenceId;
		Integer aSequenceNumber = null;
		Session aSession = null;
		Transaction aTransaction = null;
		try{
			aSysSequenceId = getSysSeqenceDynamic(theTableName);
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			// Retrieve existing person via id
			Syssequence aExistingSysSeq = (Syssequence) aSession.get(Syssequence.class, aSysSequenceId);
			// Assign updated values to this person
			aSequenceNumber = aExistingSysSeq.getSequence();
			aSequenceNumber = aSequenceNumber + 1;
			aExistingSysSeq.setSequence(aSequenceNumber);
			//existingPerson.setMoney(person.getMoney());
			// Save updates
			aSession.saveOrUpdate(aExistingSysSeq);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aSequenceNumber;
	}

	
	private int getSysSeqenceDynamic(String theTableName) throws Exception{
		Session aSession = null;
		List<Syssequence> aQueryList = null;
		int aSequenceID = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM Syssequence WHERE TableName = '"+theTableName+"' ORDER BY Sequence DESC LIMIT 1;");
			aQueryList = aQuery.list();
			Syssequence aSyssequence = aQueryList.get(0);
			aSequenceID = aSyssequence.getSysSequenceId();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw e;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aSequenceID;
	}
	
	@Override
	public boolean updateRolodexCategories(Sysinfo sysinfo) throws UserException {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = SysinfoSession.beginTransaction();
			transaction.begin();
			Sysinfo sysinfoupdate = (Sysinfo) SysinfoSession.get(Sysinfo.class, sysinfo.getSysInfoId());
			if(sysinfoupdate != null){
				if(sysinfo.getRxMasterCategory1desc()!=null||sysinfo.getRxMasterCategory1desc().length()>0){
				sysinfoupdate.setRxMasterCategory1desc(sysinfo.getRxMasterCategory1desc());
				}else{
					sysinfoupdate.setRxMasterCategory1desc("");	
				}
				if(sysinfo.getRxMasterCategory2desc()!=null && sysinfo.getRxMasterCategory2desc().length()>0){
				sysinfoupdate.setRxMasterCategory2desc(sysinfo.getRxMasterCategory2desc());
				}
				else{
					sysinfoupdate.setRxMasterCategory2desc("");	
				}
				if(sysinfo.getRxMasterCategory3desc()!=null||sysinfo.getRxMasterCategory3desc().length()>0){
				sysinfoupdate.setRxMasterCategory3desc(sysinfo.getRxMasterCategory3desc());
				}
				else{
					sysinfoupdate.setRxMasterCategory3desc("");	
				}
				if(sysinfo.getRxMasterCategory4desc()!=null||sysinfo.getRxMasterCategory4desc().length()>0){
				sysinfoupdate.setRxMasterCategory4desc(sysinfo.getRxMasterCategory4desc());
				}
				else{
					sysinfoupdate.setRxMasterCategory4desc("");	
				}
				if(sysinfo.getRxMasterCategory5desc()!=null||sysinfo.getRxMasterCategory5desc().length()>0){
				sysinfoupdate.setRxMasterCategory5desc(sysinfo.getRxMasterCategory5desc());
				}else{
					sysinfoupdate.setRxMasterCategory5desc("");	
				}
				if(sysinfo.getRxMasterCategory6desc()!=null||sysinfo.getRxMasterCategory6desc().length()>0){
				sysinfoupdate.setRxMasterCategory6desc(sysinfo.getRxMasterCategory6desc());
				}else{
					sysinfoupdate.setRxMasterCategory6desc("");	
				}
				if(sysinfo.getRxMasterCategory7desc()!=null||sysinfo.getRxMasterCategory7desc().length()>0){
				sysinfoupdate.setRxMasterCategory7desc(sysinfo.getRxMasterCategory7desc());
				}else{
					sysinfoupdate.setRxMasterCategory7desc("");	
				}
				if(sysinfo.getRxMasterCategory8desc()!=null||sysinfo.getRxMasterCategory8desc().length()>0){
				sysinfoupdate.setRxMasterCategory8desc(sysinfo.getRxMasterCategory8desc());
				}else{
					sysinfoupdate.setRxMasterCategory8desc("");	
				}
				SysinfoSession.update(sysinfoupdate);
				transaction.commit();
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	
	@Override
	public Sysinfo getRoldexCategories(int theUserinfoId) throws UserException {
		String	aSysPriSelectQry = "SELECT rxMasterCategory1Desc,rxMasterCategory2Desc,rxMasterCategory3Desc,rxMasterCategory4Desc,rxMasterCategory5Desc,rxMasterCategory6Desc,rxMasterCategory7Desc,rxMasterCategory8Desc,prPriceLevel0,prPriceLevel1,prPriceLevel2,prPriceLevel3,prPriceLevel4,prPriceLevel5,prPriceLevel6 FROM sysInfo where sysInfoID="+theUserinfoId;
		Session aSession=null;
		Sysinfo sysinfo=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				sysinfo = new Sysinfo();
				Object[] aObj = (Object[])aIterator.next();
				sysinfo.setRxMasterCategory1desc((String)aObj[0]);
				sysinfo.setRxMasterCategory2desc((String)aObj[1]);
				sysinfo.setRxMasterCategory3desc((String)aObj[2]);
				sysinfo.setRxMasterCategory4desc((String)aObj[3]);
				sysinfo.setRxMasterCategory5desc((String)aObj[4]);
				sysinfo.setRxMasterCategory6desc((String)aObj[5]);
				sysinfo.setRxMasterCategory7desc((String)aObj[6]);
				sysinfo.setRxMasterCategory8desc((String)aObj[7]);
				
				sysinfo.setPrPriceLevel0((String)aObj[8]);
				sysinfo.setPrPriceLevel1((String)aObj[9]);
				sysinfo.setPrPriceLevel2((String)aObj[10]);
				sysinfo.setPrPriceLevel3((String)aObj[11]);
				sysinfo.setPrPriceLevel4((String)aObj[12]);
				sysinfo.setPrPriceLevel5((String)aObj[13]);
				
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry=null;
		}
		return sysinfo;
	}
/*Fetching customer categories ie employees assigned*/
	@Override
	public Sysassignment getCustomerCategories() throws UserException {
		String	aSysPriSelectQry = "select sysAssignmentID,Title from sysAssignment";
		Session aSession=null;
		Sysassignment sysassign=null;
		int i=0;
		Query aQuery = null;
		Iterator<?> aIterator = null; 
		try{
			aSession=itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			aIterator = aQuery.list().iterator();
			sysassign = new Sysassignment();
			sysassign.setCustomerCategoryId1((byte)-1);
			sysassign.setCustomerCategoryId2((byte)-1);
			sysassign.setCustomerCategoryId3((byte)-1);
			sysassign.setCustomerCategoryId4((byte)-1);
			sysassign.setCustomerCategoryId5((byte)-1);
			while(aIterator.hasNext()) {
				
				Object[] aObj = (Object[])aIterator.next();
				short categoryid = (Short)aObj[0];
				if(i==0){
					sysassign.setCustomerCategoryId1((byte)categoryid);
					sysassign.setCustomerCategory1((String)aObj[1]);
				}else if(i==1){
					
					sysassign.setCustomerCategoryId2((byte)categoryid);
					sysassign.setCustomerCategory2((String)aObj[1]);
				}else if(i==2){
					sysassign.setCustomerCategoryId3((byte)categoryid);
					sysassign.setCustomerCategory3((String)aObj[1]);
				}else if(i==3){
					sysassign.setCustomerCategoryId4((byte)categoryid);
					sysassign.setCustomerCategory4((String)aObj[1]);
				}else if(i==4){
					sysassign.setCustomerCategoryId5((byte)categoryid);
					sysassign.setCustomerCategory5((String)aObj[1]);
				}
				i++;
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
			aQuery = null;
			aIterator = null;
		}
		return sysassign;
	}	
	/*Employees Assigned categories update in sysAssignment table*/
	@Override
	public boolean updateCustomerCategories(Sysassignment sysassign)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			
			for(int i=0;i<5;i++){
				transaction = SysinfoSession.beginTransaction();
				transaction.begin();
				byte id=0;
				String desc="";
				if(i==0){
					id=sysassign.getCustomerCategoryId1()==-1?0:sysassign.getCustomerCategoryId1();
					desc=sysassign.getCustomerCategory1();
				}else if(i==1){
					id=sysassign.getCustomerCategoryId2()==-1?1:sysassign.getCustomerCategoryId2();
					desc=sysassign.getCustomerCategory2();
				}else if(i==2){
					id=sysassign.getCustomerCategoryId3()==-1?2:sysassign.getCustomerCategoryId3();
					desc=sysassign.getCustomerCategory3();
				}else if(i==3){
					id=sysassign.getCustomerCategoryId4()==-1?3:sysassign.getCustomerCategoryId4();
					desc=sysassign.getCustomerCategory4();
				}else if(i==4){
					id=sysassign.getCustomerCategoryId5()==-1?4:sysassign.getCustomerCategoryId5();
					desc=sysassign.getCustomerCategory5();
				}
				
			Sysassignment sysassignment = (Sysassignment) SysinfoSession.get(Sysassignment.class, id);
			if(sysassignment != null){
				sysassignment.setTitle(desc);
				SysinfoSession.update(sysassignment);
				transaction.commit();
			}else{
				Sysassignment inssysassignment =new Sysassignment();
				inssysassignment.setSysAssignmentId(id);
				inssysassignment.setTitle(desc);
				inssysassignment.setAllowCustomerChange(false);
				inssysassignment.setAllowTransactionChange(false);
				inssysassignment.setTransDefaultLogin(false);
				SysinfoSession.save(inssysassignment);
				transaction.commit();
			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	@Override
	public jocategory getJobCategories() throws UserException {
		String	aSysPriSelectQry = "SELECT joCategoryID,Category FROM jocategory";
		Session aSession=null;
		jocategory ajocategory=null;
		int i=0;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			ajocategory = new jocategory();
			while(aIterator.hasNext()) {
				
				Object[] aObj = (Object[])aIterator.next();
				if(i==0){
					ajocategory.setJoCategory1ID((Integer) aObj[0]);
					ajocategory.setCategory1desc((String) aObj[1]);
				}else if(i==1){
					ajocategory.setJoCategory2ID((Integer) aObj[0]);
					ajocategory.setCategory2desc((String) aObj[1]);
					
				}else if(i==2){
					ajocategory.setJoCategory3ID((Integer) aObj[0]);
					ajocategory.setCategory3desc((String) aObj[1]);
				}else if(i==3){
					ajocategory.setJoCategory4ID((Integer) aObj[0]);
					ajocategory.setCategory4desc((String) aObj[1]);
				}else if(i==4){
					ajocategory.setJoCategory5ID((Integer) aObj[0]);
					ajocategory.setCategory5desc((String) aObj[1]);
				}
				else if(i==5){
					ajocategory.setJoCategory6ID((Integer) aObj[0]);
					ajocategory.setCategory6desc((String) aObj[1]);
				}
				else if(i==6){
					ajocategory.setJoCategory7ID((Integer) aObj[0]);
					ajocategory.setCategory7desc((String) aObj[1]);
				}
				
				i++;
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry=null;
		}
		return ajocategory;
	}	
	/*Employees Assigned categories update in sysAssignment table*/
	@Override
	public boolean updateJobCategories(jocategory thejocategory)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			
			itsLogger.info("inside updateJobCategories");
			for(int i=0;i<7;i++){
				transaction = SysinfoSession.beginTransaction();
				transaction.begin();
				int id=0;
				String desc="";
				if(i==0){
					id=thejocategory.getJoCategory1ID();
					desc=thejocategory.getCategory1desc();
				}else if(i==1){
					id=thejocategory.getJoCategory2ID();
					desc=thejocategory.getCategory2desc();
				}else if(i==2){
					id=thejocategory.getJoCategory3ID();
					desc=thejocategory.getCategory3desc();
				}else if(i==3){
					id=thejocategory.getJoCategory4ID();
					desc=thejocategory.getCategory4desc();
				}else if(i==4){
					id=thejocategory.getJoCategory5ID();
					desc=thejocategory.getCategory5desc();
				}else if(i==5){
					id=thejocategory.getJoCategory6ID();
					desc=thejocategory.getCategory6desc();
				}else if(i==6){
					id=thejocategory.getJoCategory7ID();
					desc=thejocategory.getCategory7desc();
				}
				
				jocategory ajocategory = (jocategory) SysinfoSession.get(jocategory.class, id);
				System.out.println("ajocategory=="+ajocategory);
			if(ajocategory != null){
				ajocategory.setCategory(desc);
				SysinfoSession.update(ajocategory);
				transaction.commit();
			}else{
				jocategory insjocategory=new jocategory();
				insjocategory.setCategory(desc);
				insjocategory.setJoCategoryID(id);
				SysinfoSession.save(insjocategory);
				transaction.commit();
			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	
	/*JoQuotecolumn details fetch*/
	@Override
	public Joquotecolumn getjoQuoteColumn() throws UserException {
		String	aSysPriSelectQry = "SELECT joQuoteColumnID,coLabel,coOrder FROM joQuoteColumn LIMIT 8";
		//String aSysPriSelectQry = "SELECT joQuoteColumnID,coLabel,coOrder FROM joQuoteColumn WHERE coOrder IS NOT NULL ORDER BY coOrder ";
		Session aSession=null;
		Joquotecolumn ajoquotecolumn=null;
		JoquotecolumnId aJoquotecolumnId=null;
		ArrayList<Joquotecolumn> Joquotecolumnlst=new ArrayList<Joquotecolumn>();
		int i=0;
		try{
			Sysprivilege aSysprivilege = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			ajoquotecolumn = new Joquotecolumn();
			ajoquotecolumn.setJoquotecolumn1Id(-1);
			ajoquotecolumn.setJoquotecolumn2Id(-1);
			ajoquotecolumn.setJoquotecolumn3Id(-1);
			ajoquotecolumn.setJoquotecolumn4Id(-1);
			ajoquotecolumn.setJoquotecolumn5Id(-1);
			ajoquotecolumn.setJoquotecolumn6Id(-1);
			ajoquotecolumn.setJoquotecolumn7Id(-1);
			ajoquotecolumn.setJoquotecolumn8Id(-1);
			while(aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				if(i==0){
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn1Name("column1");
				}
				else if(i==1){
					ajoquotecolumn.setJoquotecolumn2Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn2label((String) aObj[1]);
					ajoquotecolumn.setCoOrder2Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn2Name("column2");
				}else if(i==2){
					ajoquotecolumn.setJoquotecolumn3Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn3label((String) aObj[1]);
					ajoquotecolumn.setCoOrder3Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn3Name("column3");
				}
				else if(i==3){
					ajoquotecolumn.setJoquotecolumn4Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn4label((String) aObj[1]);
					ajoquotecolumn.setCoOrder4Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn4Name("column4");
				}
				else if(i==4){
					ajoquotecolumn.setJoquotecolumn5Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn5label((String) aObj[1]);
					ajoquotecolumn.setCoOrder5Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn5Name("column5");
					}
				else if(i==5){
					ajoquotecolumn.setJoquotecolumn6Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn6label((String) aObj[1]);
					ajoquotecolumn.setCoOrder6Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn6Name("column6");
					}
				else if(i==6){
					ajoquotecolumn.setJoquotecolumn7Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn7label((String) aObj[1]);
					ajoquotecolumn.setCoOrder7Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn7Name("column7");
					}
				else if(i==7){
					ajoquotecolumn.setJoquotecolumn8Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn8label((String) aObj[1]);
					ajoquotecolumn.setCoOrder8Id((Integer) aObj[2]);
					ajoquotecolumn.setColumn8Name("column8");
					}
				i=i+1;
				
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
		}
		return ajoquotecolumn;
	}	
	
	/*JoQuotecolumn details fetch*/
	@Override
	public ArrayList<Joquotecolumn> getjobQuotesColumn() throws UserException {
		String	aSysPriSelectQry = "SELECT joQuoteColumnID,coLabel,coOrder FROM joQuoteColumn LIMIT 8";
		//String aSysPriSelectQry = "SELECT joQuoteColumnID,coLabel,coOrder FROM joQuoteColumn WHERE coOrder IS NOT NULL ORDER BY coOrder ";
		Session aSession=null;
		Joquotecolumn ajoquotecolumn=null;
		ArrayList<Joquotecolumn> Joquotecolumnlst=new ArrayList<Joquotecolumn>();
		int i=0;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			ajoquotecolumn = new Joquotecolumn();
			HashMap<Integer, Joquotecolumn> maplist=new HashMap<Integer, Joquotecolumn>();
			
			while(aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				ajoquotecolumn = new Joquotecolumn();
				if(i==0){
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column1");
					
				}
				else if(i==1){
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column2");
				}else if(i==2){
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column3");
				}
				else if(i==3){
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column4");
				}
				else if(i==4){
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column5");
					}
				else if(i==5){
					ajoquotecolumn.setJoquotecolumn6Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column6");
					}
				else if(i==6){
					ajoquotecolumn.setJoquotecolumn7Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column7");
					}
				else if(i==7){
					ajoquotecolumn.setJoquotecolumn8Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					ajoquotecolumn.setColumnName("column8");
					}
				maplist.put(ajoquotecolumn.getCoOrder1Id(), ajoquotecolumn);
				i=i+1;
				
			}
			Map<Integer, Joquotecolumn> retlist=new TreeMap<Integer, Joquotecolumn>(maplist);
			System.out.println(retlist);
			 Set set = retlist.entrySet();
	         Iterator iterator = set.iterator();
	        
	         while(iterator.hasNext()) {
	               Map.Entry me = (Map.Entry)iterator.next();
	               Joquotecolumn lJoquotecolumn=(Joquotecolumn) me.getValue();
	               System.out.println(lJoquotecolumn.getColumn1label()+"==="+lJoquotecolumn.getColumnName()+"===");
	               Joquotecolumnlst.add(lJoquotecolumn);
	         }
			
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
		}
		return Joquotecolumnlst;
	}	
	
	public static Map<Integer, Joquotecolumn> sortByKeys(Map<Integer, Joquotecolumn> map){
        List<Integer> keys = new LinkedList<Integer>(map.keySet());
        Collections.sort(keys);
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<Integer, Joquotecolumn> sortedMap = new LinkedHashMap<Integer, Joquotecolumn>();
        for(Integer key: keys){
            sortedMap.put(key, map.get(key));
        }
      
        return sortedMap;
    }

	/*@Override
	public ArrayList<Joquotecolumn> getjobQuotesColumn() throws UserException {
		//String	aSysPriSelectQry = "SELECT joQuoteColumnID,coLabel,coOrder FROM joQuoteColumn LIMIT 8";
		String aSysPriSelectQry = "SELECT joQuoteColumnID,coLabel,coOrder FROM joQuoteColumn WHERE coOrder IS NOT NULL ORDER BY coOrder ";
		Session aSession=null;
		Joquotecolumn ajoquotecolumn=null;
	
		ArrayList<Joquotecolumn> joquotecolumnlst=new ArrayList<Joquotecolumn>();

		try{
			Sysprivilege aSysprivilege = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			
			while(aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
					ajoquotecolumn = new Joquotecolumn();
					ajoquotecolumn.setJoquotecolumn1Id((Integer) aObj[0]);
					ajoquotecolumn.setColumn1label((String) aObj[1]);
					ajoquotecolumn.setCoOrder1Id((Integer) aObj[2]);
					//ajoquotecolumn.setColumnName("column1");
					joquotecolumnlst.add(ajoquotecolumn);
						
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return joquotecolumnlst;
	}	*/
	
	/*Employees Assigned categories update in sysAssignment table*/
	@Override
	public boolean updatejobColumnQuoteDefaults(Joquotecolumn theJoquotecolumn)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		JoquotecolumnId aJoquotecolumnid=null; 
		String inssql,hql= null;
		try {
			
			for(int i=0;i<8;i++){
				transaction = SysinfoSession.beginTransaction();
				transaction.begin();
				int id=0;
				String desc="";
				aJoquotecolumnid=new JoquotecolumnId();
				if(i==0){
					id=theJoquotecolumn.getJoquotecolumn1Id()==-1?0:theJoquotecolumn.getJoquotecolumn1Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn1label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder1Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
					aJoquotecolumnid.setColDisplay((byte) 1);
					aJoquotecolumnid.setColPrint((byte) 1);
					aJoquotecolumnid.setWidthAdjust(0);
					
				}else if(i==1){
					id=theJoquotecolumn.getJoquotecolumn2Id()==-1?1:theJoquotecolumn.getJoquotecolumn2Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn2label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder2Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
				}else if(i==2){
					id=theJoquotecolumn.getJoquotecolumn3Id()==-1?2:theJoquotecolumn.getJoquotecolumn3Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn3label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder3Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
				}else if(i==3){
					id=theJoquotecolumn.getJoquotecolumn4Id()==-1?3:theJoquotecolumn.getJoquotecolumn4Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn4label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder4Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
				}else if(i==4){
					id=theJoquotecolumn.getJoquotecolumn5Id()==-1?4:theJoquotecolumn.getJoquotecolumn5Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn5label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder5Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
				}else if(i==5){
					id=theJoquotecolumn.getJoquotecolumn6Id()==-1?5:theJoquotecolumn.getJoquotecolumn6Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn6label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder6Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
				}else if(i==6){
					id=theJoquotecolumn.getJoquotecolumn7Id()==-1?6:theJoquotecolumn.getJoquotecolumn7Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn7label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder7Id());
					aJoquotecolumnid.setJoQuoteColumnId(theJoquotecolumn.getJoquotecolumn7Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
				}else if(i==7){
					id=theJoquotecolumn.getJoquotecolumn8Id()==-1?7:theJoquotecolumn.getJoquotecolumn8Id();
					aJoquotecolumnid.setCoLabel(theJoquotecolumn.getColumn8label());
					aJoquotecolumnid.setCoOrder(theJoquotecolumn.getCoOrder8Id());
					aJoquotecolumnid.setJoQuoteColumnId(id);
					
				}
				
				hql = "UPDATE joQuoteColumn SET coLabel='"+aJoquotecolumnid.getCoLabel()+"' ,coOrder='"+aJoquotecolumnid.getCoOrder()+"' WHERE joQuoteColumnID="+aJoquotecolumnid.getJoQuoteColumnId();
				Query query = SysinfoSession.createSQLQuery(hql);
				int result = query.executeUpdate();
				itsLogger.info("Rows affected: " + result);	
				if(result==0){
					inssql="INSERT INTO joQuoteColumn( joQuoteColumnID,colDisplay,colPrint,coLabel,coOrder,WidthAdjust )  VALUES( "
							+aJoquotecolumnid.getJoQuoteColumnId()+",0,0,'"
							+aJoquotecolumnid.getCoLabel()+"',"
							+aJoquotecolumnid.getCoOrder()+",0"
							+")";
					Query aquery = SysinfoSession.createSQLQuery(inssql);
					int insresult=aquery.executeUpdate();
					itsLogger.info("Rows affected: " + insresult);	
				}
				
				
				
				transaction.commit();
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
			inssql = null;
			hql = null;
		}
		return true;
	}

	@Override
	public boolean updateTierPriceLevel(Sysinfo sysassign)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		
		try {
			transaction = SysinfoSession.beginTransaction();
			transaction.begin();
			Sysinfo aSysinfo = (Sysinfo) SysinfoSession.get(Sysinfo.class, 1);			
			aSysinfo.setPrPriceLevel0(sysassign.getPrPriceLevel0());
			aSysinfo.setPrPriceLevel1(sysassign.getPrPriceLevel1());
			aSysinfo.setPrPriceLevel2(sysassign.getPrPriceLevel2());
			aSysinfo.setPrPriceLevel3(sysassign.getPrPriceLevel3());
			aSysinfo.setPrPriceLevel4(sysassign.getPrPriceLevel4());
			aSysinfo.setPrPriceLevel5(sysassign.getPrPriceLevel5());
			
			SysinfoSession.update(aSysinfo);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateGroupDefaults(TsUserGroup thegroupDefaults)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			TsUserGroup agroupDefaults = (TsUserGroup) SysinfoSession.get(TsUserGroup.class, thegroupDefaults.getUserGroupId());
			if(agroupDefaults != null){
				transaction = SysinfoSession.beginTransaction();
				agroupDefaults.setUserGroupId(thegroupDefaults.getUserGroupId());
				agroupDefaults.setGroupName(thegroupDefaults.getGroupName());
				SysinfoSession.update(agroupDefaults);
				transaction.commit();
			}else{
				transaction = SysinfoSession.beginTransaction();
				SysinfoSession.save(thegroupDefaults);
				transaction.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	

	@Override
	public TsUserGroup getgroupDefaults(Integer groupDefaultsID) throws JobException {
		Session aSession = null;
		TsUserGroup agroupDefaults = null;
		try {
			aSession = itsSessionFactory.openSession();
			agroupDefaults = (TsUserGroup) aSession.get(TsUserGroup.class,groupDefaultsID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return agroupDefaults;
	}
	
	@Override
	public boolean updatesysprivilage(Sysprivilege theSysprivilege)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			String thecondition=" WHERE  UserGroupID="+theSysprivilege.getUserGroupId()+" And AccessProcedureID="+theSysprivilege.getAccessProcedureId();
			if(theSysprivilege.getUserLoginId()!=null){
				thecondition=" WHERE   AccessProcedureID="+theSysprivilege.getAccessProcedureId()+" And UserLoginID="+theSysprivilege.getUserLoginId();
			}
			
			
			Sysprivilege obj=getSysPrivileageLst(theSysprivilege,thecondition);
			if(obj!=null && obj.getSysPrivilegeId()!=null){
			Sysprivilege agrouppermission =(Sysprivilege) SysinfoSession.get(Sysprivilege.class,obj.getSysPrivilegeId());
			if(agrouppermission != null){
				transaction = SysinfoSession.beginTransaction();
				agrouppermission.setPrivilegeValue(theSysprivilege.getPrivilegeValue());
				if(theSysprivilege.getUserGroupId()!=null){
				agrouppermission.setUserGroupId(theSysprivilege.getUserGroupId());
				}
				if(theSysprivilege.getUserLoginId()!=null){
				agrouppermission.setUserLoginId(theSysprivilege.getUserLoginId());
				}
				SysinfoSession.update(agrouppermission);
				transaction.commit();
			}else{
				transaction = SysinfoSession.beginTransaction();
				SysinfoSession.save(theSysprivilege);
				transaction.commit();
			}
			}else{
				System.out.println("else");
				transaction = SysinfoSession.beginTransaction();
				SysinfoSession.save(theSysprivilege);
				transaction.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	
	
	
	public Sysprivilege getSysPrivileageLst(Sysprivilege theSysprivilege,String Condition) throws UserException {
		String	aSysPriSelectQry = "SELECT sysPrivilegeID,AccessProcedureID,UserLoginID,UserGroupID,PrivilegeValue FROM sysPrivilege "+Condition;
		itsLogger.info(" aSysPriSelectQry = "+aSysPriSelectQry);
		Session aSession=null;
		Sysprivilege sysprivilege=null;
		Iterator<?> aIterator = null;
		Query aQuery = null;
		try{
			aSession=itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				sysprivilege = new Sysprivilege();
				Object[] aObj = (Object[])aIterator.next();
				sysprivilege.setSysPrivilegeId((Integer)aObj[0]);
				sysprivilege.setPrivilegeValue((Integer)aObj[4]);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry= null;
			aIterator = null;
			aQuery = null;
		}
		return sysprivilege;
	}
	
	
	
	@Override
	public ArrayList<Sysprivilege> getGroupdefaultSysPrivileageLst(Sysprivilege theSysprivilege) throws UserException {
		String theCondition=" ";
		if(theSysprivilege.getUserGroupId()!=null){
			theCondition=" WHERE sp.UserGroupID="+theSysprivilege.getUserGroupId();
		}
		if(theSysprivilege.getUserLoginId()!=null){
			theCondition=" WHERE sp.UserLoginID="+theSysprivilege.getUserLoginId();
		}
		String	aSysPriSelectQry = "SELECT sp.sysPrivilegeID,sp.AccessProcedureID,sp.UserLoginID,sp.UserGroupID,sp.PrivilegeValue,tsap.ProcedureName FROM sysPrivilege sp LEFT JOIN tsAccessProcedure tsap ON(sp.AccessProcedureID=tsap.AccessProcedureID) "+theCondition;
		Session aSession=null;
		Sysprivilege sysprivilege=null;
		ArrayList<Sysprivilege> theSysprivilegeLst=new ArrayList<Sysprivilege>();
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				sysprivilege = new Sysprivilege();
				Object[] aObj = (Object[])aIterator.next();
				sysprivilege.setSysPrivilegeId((Integer)aObj[0]);
				sysprivilege.setAccessProcedureId((Integer)aObj[1]);
				if(aObj[2]!=null){
					sysprivilege.setUserLoginId((Integer)aObj[2]);
				}
				
				sysprivilege.setUserGroupId((Integer)aObj[3]);
				sysprivilege.setPrivilegeValue((Integer)aObj[4]);
				sysprivilege.setProcedureName((String)aObj[5]);
				theSysprivilegeLst.add(sysprivilege);
				
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry=null;
		}
		return theSysprivilegeLst;
	}
	
	
	@Override
	public TsUserGroupLink getUserGroupLink(Integer userLoginId, Integer groupID) throws JobException {
		String query = "SELECT * FROM tsUserGroupLink where UserLoginID="+userLoginId+" AND UserGroupID="+groupID;
		itsLogger.info(" query :: "+query);
		Session aSession = null;
		TsUserGroupLink agroupDefaults = new TsUserGroupLink();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(query);
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				agroupDefaults = new TsUserGroupLink();
				Object[] aObj = (Object[])aIterator.next();
				itsLogger.info(" aObj[0] = "+aObj[0]+" || aObj[1] = "+aObj[1]+" || aObj[2] "+aObj[2]);
				agroupDefaults.setUserGroupLinkId((Integer) aObj[0]);
				agroupDefaults.setUserGroupID((Integer) aObj[1]);
				agroupDefaults.setUserLoginID((Integer) aObj[2]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return agroupDefaults;
	}
	
	public ArrayList<TsUserLogin> getallUserID() {
		String	aSysPriSelectQry = "SELECT UserLoginId FROM tsUserLogin WHERE Employee0 = 1 AND UserLoginId NOT IN(	  SELECT UserLoginId FROM emMaster WHERE UserLoginId  IN (SELECT UserLoginId FROM tsUserLogin WHERE Employee0 = 1))";
		itsLogger.info(" aSysPriSelectQry = "+aSysPriSelectQry);
		Session aSession=null;
		ArrayList<TsUserLogin> atsuserlogin=new ArrayList<TsUserLogin>();
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				TsUserLogin atsuser = new TsUserLogin();
				Integer loginid=(Integer) aIterator.next();
				atsuser.setUserLoginId(loginid);
				atsuserlogin.add(atsuser);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
		}
		return atsuserlogin;
	}
}
