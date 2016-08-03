package com.turborep.turbotracker.settings.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.vendor.dao.Veshipvia;

@Service("shipViaService")
@Transactional
public class ShipViaServiceImpl implements ShipViaService {

	Logger logger = Logger.getLogger(ShipViaServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public List<Veshipvia> getVeShipVia() throws UserException {
		Session aSession = null;
		List<Veshipvia> aQueryList = null;
		Query aQuery = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	aQuery = aSession.createQuery("FROM  Veshipvia WHERE Description <> '' ORDER BY Description ASC");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			UserException aUserException = new UserException(excep.getCause().getMessage(), excep);
			throw aUserException;		
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  aQueryList;
	}


	@Override
	public Veshipvia updateShipViaDetails(Veshipvia theVeshipvia) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		Veshipvia aVeshipvia = null;
		try {
			aVeshipvia = (Veshipvia) aSession.get(Veshipvia.class, theVeshipvia.getVeShipViaId());
			aVeshipvia.setDescription(theVeshipvia.getDescription());
			aVeshipvia.setInActive(aVeshipvia.getInActive());
			aVeshipvia.setTrackUrl(theVeshipvia.getTrackUrl());
			aVeshipvia.setTrackPrefix(theVeshipvia.getTrackPrefix());
			aVeshipvia.setTrackSuffix(theVeshipvia.getTrackSuffix());
			aSession.update(aVeshipvia);
			aVeshipvia.setOper("edit");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			UserException auserexception= new UserException(e.getMessage(), e);
			throw auserexception;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVeshipvia;
	}
	
	@Override
	public Boolean deleteveshipdetail(Integer aVeShipViaId) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Veshipvia aVeshipvia = new Veshipvia();
		try {
			aVeshipvia.setVeShipViaId(aVeShipViaId);
			aVeshipvia.setInActive(false);
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.delete(aVeshipvia);
			aTransaction.commit();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			UserException auserexception= new UserException(excep.getMessage(), excep);
			throw auserexception;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	 }


	@Override
	public Veshipvia addShipViaDetails(Veshipvia theVeshipvia) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Veshipvia aVeshipvia = null;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.save(theVeshipvia);
			aTransaction.commit();
			aVeshipvia = (Veshipvia) aSession.get(Veshipvia.class, theVeshipvia.getVeShipViaId());
			aVeshipvia.setOper("add");
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			UserException auserexception= new UserException(excep.getMessage(), excep);
			throw auserexception;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVeshipvia;
	}

}
