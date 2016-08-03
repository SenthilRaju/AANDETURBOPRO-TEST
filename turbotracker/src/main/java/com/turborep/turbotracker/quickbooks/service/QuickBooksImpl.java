package com.turborep.turbotracker.quickbooks.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.quickbooks.dao.QbConfig;
import com.turborep.turbotracker.vendor.dao.Vepodetail;

@Service("qbService")
@Transactional
public class QuickBooksImpl implements QuickBooksService {
	
	protected static Logger itsLogger = Logger.getLogger("services");
	
	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public void saveQB(QbConfig theQbpo ) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(theQbpo);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	@Transactional
	public void updateQB(QbConfig theQbpo) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.update(theQbpo);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Integer getQbConfig(String aQbuserName) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query theQuery = aSession.createQuery("FROM QbConfig q WHERE q.tpUserName = :tpUserName");
			theQuery.setParameter("tpUserName", aQbuserName);
			List<?> theList = theQuery.list();
			if(theList.size() > 0)
				return ((QbConfig)theList.get(0)).getQbID();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}

	@Override
	public QbConfig getQbConfigBean(String userName) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query theQuery = aSession.createQuery("FROM QbConfig q WHERE q.tpUserName = :tpUserName");
			theQuery.setParameter("tpUserName", userName);
			List<?> theList = theQuery.list();
			if(theList.size() > 0)
				return (QbConfig)theList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}

	@Override
	public List<Vepodetail> getVepodetailList(Integer theVePOId) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query theQuery = aSession.createQuery("FROM Vepodetail q WHERE q.vePoid = :theVePOId");
			theQuery.setParameter("theVePOId", theVePOId);
			List<Vepodetail> theVepodetailList = theQuery.list();
				return theVepodetailList;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}

	@Override
	public String getItemCode(Integer thePrMasterId) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query theQuery = aSession.createQuery("FROM Prmaster p WHERE p.prMasterId = :prMasterId");
			theQuery.setParameter("prMasterId", thePrMasterId);
			List<?> thePrmaster = theQuery.list();
			if(thePrmaster.size() > 0)
				return ((Prmaster) theQuery.uniqueResult()).getItemCode();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}



}

//QB service impl