package com.turborep.turbotracker.company.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cobalance;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.system.dao.Sysinfo;

@Service("accountingCyclesService")
public class AccountingCyclesServiceImpl implements AccountingCyclesService {

Logger itsLogger = Logger.getLogger(AccountingCyclesServiceImpl.class);

SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;

	@Override
	public Sysinfo getSysinfo() throws CompanyException {
		Session aSession = null;
		Sysinfo asysinfo = new Sysinfo();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Sysinfo");
			if(query.list().size() > 0)
			asysinfo = (Sysinfo) query.uniqueResult();
			else return null;
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query=null;
		}
		return asysinfo;
	}

	@Override
	public Cofiscalyear getCurrentYear(Integer currentFiscalYearId)
			throws CompanyException {
		Session aSession = null;
		Cofiscalyear aCofiscalyear = new Cofiscalyear();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalyear c where c.coFiscalYearId = :coFiscalYearId");
			query.setParameter("coFiscalYearId", currentFiscalYearId);
		//	if(query.list().size() > 0)
				aCofiscalyear = (Cofiscalyear) query.uniqueResult();
		//	else return null;
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query=null;
		}
		return aCofiscalyear;
	}

	@Override
	public Cofiscalperiod getCurrentPeriod(Integer currentPeriodId)
			throws CompanyException {
		Session aSession = null;
		Cofiscalperiod aCofiscalperiod = new Cofiscalperiod();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalperiod c where c.coFiscalPeriodId = :coFiscalPeriodId");
			query.setParameter("coFiscalPeriodId", currentPeriodId);
		//	if(query.list().size() > 0)
				aCofiscalperiod = (Cofiscalperiod) query.uniqueResult();
		//	else return null;
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query=null;
		}
		return aCofiscalperiod;
	}

	@Override
	public List<Cofiscalperiod> getCurrentYearList(Integer coFiscalYearId)
			throws CompanyException {
		Session aSession = null;
		List<Cofiscalperiod> aCofiscalperiod = new ArrayList<Cofiscalperiod>();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalperiod c where c.coFiscalYearId = :coFiscalYearId");
			//query = aSession.createQuery("from Cofiscalperiod ");
			query.setParameter("coFiscalYearId", coFiscalYearId);
				aCofiscalperiod =  query.list();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCofiscalperiod;
	}
	
	@Override
	public List<Cofiscalyear> getfiscalYearList()
			throws CompanyException {
		Session aSession = null;
		List<Cofiscalyear> Cofiscalyear = new ArrayList<Cofiscalyear>();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			//query = aSession.createQuery("from Cofiscalperiod c where c.coFiscalYearId = :coFiscalYearId");
			query = aSession.createQuery("from Cofiscalyear ");
			//query.setParameter("coFiscalYearId", coFiscalYearId);
			Cofiscalyear =  query.list();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return Cofiscalyear;
	}

	@Override
	public void updateSiteInfo(Integer sysInfoId,
			Integer reversePeriodId) throws CompanyException {
		Session aSession = null;
		Transaction aTransaction = null; 
		try{
		aSession = itsSessionFactory.openSession();
		aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		if(sysInfoId != null)
		{
			Cofiscalperiod aCofiscalperiod = (Cofiscalperiod) aSession.get(Cofiscalperiod.class, reversePeriodId);
			Sysinfo aSysinfo = (Sysinfo) aSession.get(Sysinfo.class, sysInfoId);
			if(aSysinfo != null)
			{
				aSysinfo.setCurrentFiscalYearId(aCofiscalperiod.getCoFiscalYearId());
				aSysinfo.setCurrentPeriodId(reversePeriodId);
				aSession.update(aSysinfo);	
			}
		}
	} catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
	} 
		 finally {
			 aTransaction.commit();
				aSession.flush();
				aSession.close();
			}
	}

	@Override
	public Integer getYearId(String coFiscalYear) throws CompanyException {
		Session aSession = null;
		Integer yearId = 0;
		Query query = null;
		Cofiscalyear aCofiscalyear = new Cofiscalyear();
		try{
			
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalyear c where fiscalYear=:fiscalYear");
			query.setParameter("fiscalYear", coFiscalYear);
			aCofiscalyear =  (Cofiscalyear) query.uniqueResult();
			
			if(aCofiscalyear != null)
				yearId = aCofiscalyear.getCoFiscalYearId();
			
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return yearId;
	}

	@Override
	public Integer getPeriodId(Date nextDate, Integer yearId)
			throws CompanyException {
		Integer periodId = 0;
		Session aSession = null;
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalperiod c where c.startDate = :startDate and c.coFiscalYearId =:yearId");
			query.setParameter("startDate", nextDate);
			query.setParameter("yearId", yearId);
			Cofiscalperiod aCofiscalperiod =  (Cofiscalperiod) query.uniqueResult();
			if(aCofiscalperiod != null)
			periodId = aCofiscalperiod.getCoFiscalPeriodId();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return periodId;
	}

	@Override
	public Integer createYear(Date nextYearFrom, Date nextYearTo,String year,String description)
			throws CompanyException {
		Session aSession = null;
		/*Session aSession1 = null;
		Session aSession2 = null;*/
		Transaction aTransaction = null; 
		/*Integer periodId = 0;
		Integer yearId=0; */
		Integer yearId=0;
		try{
		aSession = itsSessionFactory.openSession();
		aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		
			Cofiscalyear aCofiscalyear = new Cofiscalyear();
			aCofiscalyear.setEndDate(nextYearTo);
			aCofiscalyear.setStartDate(nextYearFrom);
			aCofiscalyear.setFiscalYear(year);
			aCofiscalyear.setDescription(description);
			aCofiscalyear.setClosedStatus(true);
			yearId = (Integer) aSession.save(aCofiscalyear);
			
	} catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
	} 
		 finally {
			 aTransaction.commit();
				aSession.flush();
				aSession.close();
			}
		return yearId;
	}

	@Override
	public void createPeriodId(Date nextPeriodFrom, Date nextPeriodTo,
			Integer yearId,boolean flag,Integer period) throws CompanyException {
		Session aSession = null;
		Transaction aTransaction = null; 
		try{
		aSession = itsSessionFactory.openSession();
		aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		if(yearId != null)
		{
			Cofiscalperiod aCofiscalperiod = new Cofiscalperiod();
			aCofiscalperiod.setCoFiscalYearId(yearId);
			aCofiscalperiod.setEndDate(nextPeriodTo);
			aCofiscalperiod.setStartDate(nextPeriodFrom);
			aCofiscalperiod.setStartNewQuarter(flag);
			aCofiscalperiod.setPeriod(period);
			aSession.save(aCofiscalperiod);
		}
	
	} catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
	} 
		 finally {
			 aTransaction.commit();
				aSession.flush();
				aSession.close();
			}
	}
	
	@Override
	public void updatePeriodId(Date nextPeriodFrom, Date nextPeriodTo,
			Integer yearId,boolean flag,Integer period) throws CompanyException {
		Session aSession = null;
		Transaction aTransaction = null; String strQuery =null;
		try{
		aSession = itsSessionFactory.openSession();
		aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		
		if(yearId != null)
		{
			strQuery = "update coFiscalPeriod set StartDate='"+new java.sql.Date(nextPeriodFrom.getTime())+"', EndDate='"+new java.sql.Date(nextPeriodTo.getTime())+"' where coFiscalYearID = "+yearId+" and period="+period;
			aSession.createSQLQuery(strQuery).executeUpdate();
		}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {

			aSession.flush();
			aSession.close();
			strQuery = null;
		}
	}

	@Override
	public void updateSiteInfo(Integer sysInfoId, Integer periodId,
			Integer yearId) throws CompanyException {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			// aTransaction = aSession.beginTransaction();
			// Date today = null;

			if (sysInfoId != null) {
				// aTransaction.begin();
				Sysinfo aSysinfo = (Sysinfo) aSession.get(Sysinfo.class,
						sysInfoId);
				if (aSysinfo != null) {
					aSysinfo.setCurrentPeriodId(periodId);
					aSysinfo.setCurrentFiscalYearId(yearId);
					aSession.update(aSysinfo);
				}
				// aTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void savecoFiscalYear(String year) throws CompanyException {

		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();

			Cofiscalyear aCofiscalyear = new Cofiscalyear();
			aCofiscalyear.setFiscalYear(year);
			aSession.save(aCofiscalyear);
			aTransaction.commit();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {

			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Integer openorclosePeriodIdbasedonperiod(Integer periods,
			Integer yearid, Boolean opStatus) throws CompanyException {

		Integer period = 0;
		Session aSession = null;
		Query query = null;
		Cofiscalperiod aCofiscalperiod = null;
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createSQLQuery("update coFiscalPeriod set openstatus=:opStatus where period = :period and coFiscalYearId =:yearId");
			query.setParameter("yearId", yearid);
			query.setParameter("period", periods);
			query.setParameter("opStatus", opStatus);
			query.executeUpdate();
			
			period = getperiodAvailable(periods,yearid);
			aCofiscalperiod = getCurrentPeriod(period);
			
			

			if(!opStatus)
			{
				if(!getCoBalanceValues(period))
				{
				aSession.createSQLQuery(" call `sp_openingBalance`("+yearid+","+period+",'"+aCofiscalperiod.getStartDate()+"')").executeUpdate();
				}
				else
				{
				Sysinfo aSysinfo = getSysinfo();
				List<Cofiscalperiod> alist = getcofiperiodvalues(period,aSysinfo.getCurrentPeriodId());
				
				Iterator<?> aIterator = alist.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aSession.createSQLQuery(" call `sp_updateopBalance`("+(Integer)aObj[1]+","+(Integer)aObj[0]+",'"+(Date)aObj[2]+"')").executeUpdate();
				}
				}
			}
			
			/*
			 * Cofiscalperiod aCofiscalperiod = (Cofiscalperiod)
			 * query.uniqueResult(); if(aCofiscalperiod != null) periodId =
			 * aCofiscalperiod.getCoFiscalPeriodId();	
			 */
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return period;
	}
	
	@Override
	public boolean getCoBalanceValues(Integer coFiscalPeriod)
			throws CompanyException {
		Session aSession = null;
		List<Cobalance> alist =null;
		Query query = null;
		boolean aStatus = false;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cobalance c where c.coFiscalPeriodId = :coFiscalPeriodId");
			query.setParameter("coFiscalPeriodId", coFiscalPeriod);
			alist =  query.list();
			
			if(alist.size()>0)
			aStatus = true;
			
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query=null;
		}
		return aStatus;
	}
	
	@Override
	public List<Cofiscalperiod> getcofiperiodvalues(Integer closeperiodid,Integer currentperiodid)throws CompanyException {
		
		Session aSession = null;
		List<Cofiscalperiod> Cofiscalperiod = new ArrayList<Cofiscalperiod>();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery("SELECT coFiscalPeriodID ,coFiscalYearID,StartDate FROM coFiscalPeriod WHERE coFiscalPeriodID >= "+closeperiodid+" AND coFiscalPeriodID < "+currentperiodid+" AND period <>13");
			Cofiscalperiod = query.list();
			
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return Cofiscalperiod;
	}

		

	@Override
	public List<Cofiscalperiod> getActivitycount(Integer year)
			throws CompanyException {
		
		Session aSession = null;
		List<Cofiscalperiod> Cofiscalperiod = new ArrayList<Cofiscalperiod>();
		Cofiscalperiod CofiscalperiodObj = new Cofiscalperiod();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery("SELECT COUNT(*) AS Activitycount,period FROM glTransaction WHERE coFiscalYearId="+year+" GROUP BY period");
			Iterator<?> aIterator = query.list().iterator();
			while (aIterator.hasNext()) {
				CofiscalperiodObj = new Cofiscalperiod();
				Object[] aObj = (Object[]) aIterator.next();
				if ((BigInteger) aObj[0] != null) {
					CofiscalperiodObj.setActivityCount((BigInteger) aObj[0]);
				}
				if((Integer) aObj[1] != null) {
					CofiscalperiodObj.setPeriod((Integer) aObj[1]);
				}
				
				Cofiscalperiod.add(CofiscalperiodObj);
			}
			
			
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return Cofiscalperiod;
	}

	@Override
	public void updatecoFiscalYearDetails(Integer year, String desc)
			throws CompanyException {

		Session aSession = null;
		Transaction aTransaction = null;
		String strQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			strQuery = "update coFiscalYear set Description='" + desc
					+ "' where coFiscalYearID = " + year;
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

	@Override
	public void updatesysInfoDetailsinitially() throws CompanyException {
		Session aSession = null;
		Date today = null;
		Query qry = null;
		Query query = null;
		String strQuery = null;
		Sysinfo sysinfo = new Sysinfo();
		try {
			sysinfo = getSysinfo();
			aSession = itsSessionFactory.openSession();
			strQuery = "Select NOW() as currentDate from dual";
			today = (Date) aSession.createSQLQuery(strQuery).uniqueResult();
			//today = new Date();
			
			//For 13th period closed
			qry = aSession.createQuery("from Cofiscalyear cy where cy.startDate <= :Date and cy.endDate >= :Date");
			qry.setParameter("Date", today);
			Cofiscalyear aCofiscalyear = (Cofiscalyear)qry.uniqueResult();

			
			query = aSession
					.createQuery("from Cofiscalperiod c where c.startDate <= :Date and c.endDate >= :Date");
			query.setParameter("Date", today);
			Cofiscalperiod aCofiscalperiod = (Cofiscalperiod) query.uniqueResult();

			if (aCofiscalperiod != null) {
				
				
				// check 13th period closed
				if(aCofiscalyear.getClosedStatus())
				{
				aCofiscalperiod.setOpenStatus(true);
				aSession.update(aCofiscalperiod);
				}
				else
				{
				aCofiscalperiod.setOpenStatus(false);
				aSession.update(aCofiscalperiod);	
				}
				
				Sysinfo aSysinfo = getSysinfo();
				
				if(!aSysinfo.getCurrentPeriodId().equals(aCofiscalperiod.getCoFiscalPeriodId()))
				updateallaccountopeningbalance(aSysinfo.getCurrentPeriodId(),aSysinfo.getCurrentFiscalYearId(),aCofiscalperiod.getStartDate());
				
				updateSiteInfo(sysinfo.getSysInfoId(),
						aCofiscalperiod.getCoFiscalPeriodId(),
						aCofiscalperiod.getCoFiscalYearId());
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			strQuery = null;
		}
	}
	
	
	public boolean updateallaccountopeningbalance( Integer periodid,Integer yearid,Date startDate)
			throws CompanyException {

		Session aSession = null;
		Query query = null;
		try {

			aSession = itsSessionFactory.openSession();
			aSession.createSQLQuery(" call `sp_openingBalance`("+yearid+","+periodid+",'"+startDate+"')").executeUpdate();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return true;
	}
	

	@Override
	public Cofiscalperiod getAllOpenPeriods(Date dateto)
			throws CompanyException {

		Session aSession = null;
		Query query = null;
		Cofiscalperiod aCofiscalperiod = null;
		try {

			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("from Cofiscalperiod c where c.startDate <= :Date and c.endDate >= :Date and c.openStatus = TRUE");
			query.setParameter("Date", dateto);
			aCofiscalperiod = (Cofiscalperiod) query.uniqueResult();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCofiscalperiod;
	}
	
	@Override
	public Cofiscalperiod getCurrentOpenPeriods(Date dateto, Integer yearID,
			Integer periodID) throws CompanyException {

		Session aSession = null;
		Query query = null;
		Cofiscalperiod aCofiscalperiod = null;
		try {

			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("from Cofiscalperiod c where c.startDate <= :Date and c.endDate >= :Date and coFiscalYearID =:yearID and coFiscalPeriodID =:periodID and  c.openStatus = TRUE");
			query.setParameter("Date", dateto);
			query.setParameter("yearID", yearID);
			query.setParameter("periodID", periodID);
			aCofiscalperiod = (Cofiscalperiod) query.uniqueResult();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCofiscalperiod;
	}
	
	@Override
	public Date getOpenCofiscalperiod() throws CompanyException {

		Session aSession = null;
		Query query = null;
		Date minDate = null;

		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createSQLQuery("SELECT MIN(StartDate) FROM coFiscalPeriod WHERE openStatus = TRUE");
			minDate = (Date) query.uniqueResult();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return minDate;
	}

	@Override
	public boolean getperiodAvailableStatus(Integer period, Integer yearID)
			throws CompanyException {
		boolean statusCheck = false;
		Session aSession = null;
		Query query = null;
		Integer periodId = null;

		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createSQLQuery("SELECT coFiscalPeriodID from coFiscalPeriod where period = "
							+ period + " and coFiscalYearID = " + yearID);
			periodId = (Integer) query.uniqueResult();

			if (periodId != null)
				statusCheck = true;

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}

		return statusCheck;
	}
	
	
	@Override
	public Integer getperiodAvailable(Integer period, Integer yearID)
			throws CompanyException {
		boolean statusCheck = false;
		Session aSession = null;
		Query query = null;
		Integer periodId = null;

		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createSQLQuery("SELECT coFiscalPeriodID from coFiscalPeriod where period = "
							+ period + " and coFiscalYearID = " + yearID);
			periodId = (Integer) query.uniqueResult();


		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}

		return 			periodId;
	}

	@Override
	public List<Object> getMostRecentPeriods() throws CompanyException {
		
		Session aSession = null;
		List<Object> Cofiscalperiod = new ArrayList<Object>();
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery("SELECT fiscalYear,period FROM sysInfo si,coFiscalYear cf,coFiscalPeriod cp WHERE cp.coFiscalPeriodID = si.CurrentPeriodID AND cf.coFiscalYearID = si.CurrentFiscalYearID");
			Cofiscalperiod = query.list();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return Cofiscalperiod;
	}
	
	@Override
	public Date getopenPeriods() throws CompanyException {
		
		Session aSession = null;
		Query query = null;
		Date minDate = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery("SELECT cp.StartDate FROM sysInfo si,coFiscalPeriod cp WHERE cp.coFiscalPeriodID = si.CurrentPeriodID ");
			minDate = (Date) query.uniqueResult();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return minDate;
	}
	
	@Override
	public Date getCurrentServerDate() {
		
		Session aSession = null;
		Date getCurrentServerDate = null;
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery("SELECT NOW() AS currentDate FROM DUAL");
			getCurrentServerDate = (Date) query.uniqueResult();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return getCurrentServerDate;
	}
	
	@Override
	public List<?> getAllfiscalPeriod()
			throws CompanyException {
		Session aSession = null;
		List<Cofiscalperiod> aQueryList = new ArrayList<Cofiscalperiod>();
		Sysinfo aSysinfo =null;
		Query query = null;
		try{
			aSession = itsSessionFactory.openSession();
			aSysinfo = getSysinfo();
			query = aSession
					.createQuery(" FROM Cofiscalperiod cp  where cp.period <> 13  GROUP BY cp.endDate order by cp.coFiscalPeriodId asc");
			
			
			/*Criteria criteria = aSession.createCriteria(Cofiscalperiod.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.ne("period",13));
			criteria.addOrder(Order.asc("coFiscalPeriodId"));
			 aQueryList = criteria.list();*/
			
			aQueryList = query.list();
			
			for (Cofiscalperiod cfp : aQueryList) {
				cfp.setStrdate((new SimpleDateFormat("MM/dd/yyyy")
						.format((Date) cfp.getEndDate())));
				
				cfp.setCurrentPeriodid(aSysinfo.getCurrentPeriodId());
				cfp.setCurrentYearid(aSysinfo.getCurrentFiscalYearId());
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aQueryList;
	}

	@Override
	public void updateAllperiosstatusclosed(Integer yearId) throws CompanyException {
		Session aSession = null;
		Query query = null;
		Transaction aTransaction = null; 
		try{
		aSession = itsSessionFactory.openSession();
		aTransaction = aSession.beginTransaction();
			query = aSession.createSQLQuery("update coFiscalPeriod set openStatus = 0 where coFiscalYearID = "+yearId);
			query.executeUpdate();
		aTransaction.commit();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
	}

	@Override
	public Cofiscalperiod getPeriodDetailswithyearid(Integer yearId)throws CompanyException
	{
		Session aSession = null;
		Cofiscalperiod aCofiscalperiod = null;
		Query query = null;
		try{
			
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("from Cofiscalperiod c where c.coFiscalYearId = :coFiscalYearId and c.period = 13");
			query.setParameter("coFiscalYearId", yearId);
			aCofiscalperiod =  (Cofiscalperiod) query.uniqueResult();
			
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCofiscalperiod;
	}


	@Override
	public boolean updateCurrentYeartoopen(Integer currentFiscalYearId)
			throws CompanyException {
	
			Session aSession = null;
			Query query = null;
			Transaction aTransaction = null; 
			try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
				query = aSession.createSQLQuery("update coFiscalYear set closedStatus = 1 where coFiscalYearID = "+currentFiscalYearId);
				query.executeUpdate();
			aTransaction.commit();
			
			}catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
				query=null;
			}
			return true;
		
	}
	
	
}
