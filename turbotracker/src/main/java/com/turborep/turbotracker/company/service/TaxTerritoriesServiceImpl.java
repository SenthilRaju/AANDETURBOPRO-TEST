package com.turborep.turbotracker.company.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;

@Transactional
@Service("taxTerritoriesService")
public class TaxTerritoriesServiceImpl implements TaxTerritoriesService {

	Logger itsLogger = Logger.getLogger(TaxTerritoriesServiceImpl.class);

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	/*public List<CoTaxTerritory> getTaxTerritoryList() throws CompanyException {
		Session aSession = null;
		List<CoTaxTerritory> aTaxTerritoryList = new ArrayList<CoTaxTerritory>();
		try{
			aSession = itsSessionFactory.openSession();
			aTaxTerritoryList = (List<CoTaxTerritory>)aSession.createCriteria(CoTaxTerritory.class).list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aTaxTerritoryList;
	}*/
	
	public List<CoTaxTerritory> getTaxTerritoryList() throws CompanyException {
		Session aSession = null;
		List<CoTaxTerritory> aTaxTerritoryList = null;Query query = null;
		try{
			aSession = itsSessionFactory.openSession(); 
			query = aSession.createQuery("FROM  CoTaxTerritory ORDER BY county asc");
			aTaxTerritoryList = query.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aTaxTerritoryList;
	}
	
	@Override
	public Boolean deleteTaxTerritory(Integer coTaxTerritoryID) throws CompanyException {
		Session aCoTaxTerritorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		CoTaxTerritory aCoTaxTerritory = new CoTaxTerritory();
		aCoTaxTerritory.setCoTaxTerritoryId(coTaxTerritoryID);
		try {
			aTransaction = aCoTaxTerritorySession.beginTransaction();
			aCoTaxTerritorySession.delete(aCoTaxTerritory);
			aTransaction.commit();
		} catch (HibernateException e) {
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aCoTaxTerritorySession.flush();
			aCoTaxTerritorySession.close();
		}
		return true;
	}

	@Override
	public Boolean addTerritory(CoTaxTerritory theNewTerritory) throws CompanyException {
		Session aCoTaxTerritorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCoTaxTerritorySession.beginTransaction();
			aCoTaxTerritorySession.save(theNewTerritory);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CompanyException(excep.getMessage(), excep);
		} finally {
			aCoTaxTerritorySession.flush();
			aCoTaxTerritorySession.close();
		}
		return true;
	}

	@Override
	public CoTaxTerritory getSingleTaxTerritory(Integer TaxTerritoryID)
			throws CompanyException {
		String query = "FROM CoTaxTerritory where coTaxTerritoryId="+TaxTerritoryID;
		Session aSession =null;
		CoTaxTerritory aCoTaxTerritory = new CoTaxTerritory();
		try{ 
			aSession = itsSessionFactory.openSession();
			if(aSession.createQuery(query).list().size()>0){
			aCoTaxTerritory = (CoTaxTerritory) aSession.createQuery(query).list().get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CompanyException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return aCoTaxTerritory;
	}
	@Override
	public Boolean editTaxTerritory(CoTaxTerritory theTaxTerritory) throws CompanyException {
		Session aCoTaxTerritorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		CoTaxTerritory aTaxTerritory = null;
		try {
			aTransaction = aCoTaxTerritorySession.beginTransaction();
			aTransaction.begin();
			aTaxTerritory = (CoTaxTerritory)aCoTaxTerritorySession.get(CoTaxTerritory.class,theTaxTerritory.getCoTaxTerritoryId());
			aTaxTerritory.setCounty(theTaxTerritory.getCounty());
			aTaxTerritory.setCountyCode(theTaxTerritory.getCountyCode());
			aTaxTerritory.setDistDesc1(theTaxTerritory.getDistDesc1());
			aTaxTerritory.setDistribution1(theTaxTerritory.getDistribution1());
			aTaxTerritory.setgRDistribution1(theTaxTerritory.getgRDistribution1());
			aTaxTerritory.setDistDesc2(theTaxTerritory.getDistDesc2());
			aTaxTerritory.setDistribution2(theTaxTerritory.getDistribution2());
			aTaxTerritory.setgRDistribution2(theTaxTerritory.getgRDistribution2());
			aTaxTerritory.setDistDesc3(theTaxTerritory.getDistDesc3());
			aTaxTerritory.setDistribution3(theTaxTerritory.getDistribution3());
			aTaxTerritory.setgRDistribution3(theTaxTerritory.getgRDistribution3());
			aTaxTerritory.setDistDesc4(theTaxTerritory.getDistDesc4());
			aTaxTerritory.setDistribution4(theTaxTerritory.getDistribution4());
			aTaxTerritory.setgRDistribution4(theTaxTerritory.getgRDistribution4());
			aTaxTerritory.setDistDesc5(theTaxTerritory.getDistDesc5());
			aTaxTerritory.setDistribution5(theTaxTerritory.getDistribution5());
			aTaxTerritory.setgRDistribution5(theTaxTerritory.getgRDistribution5());
			aTaxTerritory.setDistDesc6(theTaxTerritory.getDistDesc6());
			aTaxTerritory.setDistribution6(theTaxTerritory.getDistribution6());
			aTaxTerritory.setgRDistribution6(theTaxTerritory.getgRDistribution6());
			aTaxTerritory.setDistDesc7(theTaxTerritory.getDistDesc7());
			aTaxTerritory.setDistribution7(theTaxTerritory.getDistribution7());
			aTaxTerritory.setgRDistribution7(theTaxTerritory.getgRDistribution7());
			aTaxTerritory.setDistDesc8(theTaxTerritory.getDistDesc8());
			aTaxTerritory.setDistribution8(theTaxTerritory.getDistribution8());
			aTaxTerritory.setgRDistribution8(theTaxTerritory.getgRDistribution8());
			aTaxTerritory.setTaxRate(theTaxTerritory.getTaxRate());
			aTaxTerritory.setSurtaxCap(theTaxTerritory.getSurtaxCap());
			aTaxTerritory.setSurtaxRate(theTaxTerritory.getSurtaxRate());
			aTaxTerritory.setState(theTaxTerritory.getState());
			
			aTaxTerritory.setRate2(theTaxTerritory.getRate2());
			aTaxTerritory.setRate3(theTaxTerritory.getRate3());
			aTaxTerritory.setRate4(theTaxTerritory.getRate4());
			aTaxTerritory.setRate5(theTaxTerritory.getRate5());
			aTaxTerritory.setRate6(theTaxTerritory.getRate6());
			
			aTaxTerritory.setFrom2(theTaxTerritory.getFrom2());
			aTaxTerritory.setFrom3(theTaxTerritory.getFrom3());
			aTaxTerritory.setFrom4(theTaxTerritory.getFrom4());
			aTaxTerritory.setFrom5(theTaxTerritory.getFrom5());
			aTaxTerritory.setFrom6(theTaxTerritory.getFrom6());
			
			aTaxTerritory.setgRTaxRate(theTaxTerritory.getgRTaxRate());
			aTaxTerritory.setInactive(theTaxTerritory.getInactive());
			aTaxTerritory.setTaxfreight(theTaxTerritory.getTaxfreight());
			aCoTaxTerritorySession.update(aTaxTerritory);
			aTransaction.commit();
		}catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CompanyException(excep.getMessage(), excep);
		} finally {
			aCoTaxTerritorySession.flush();
			aCoTaxTerritorySession.close();
		}
		return true;
	}
}
