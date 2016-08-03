package com.turborep.turbotracker.settings.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.job.dao.Joschedaccessory;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempcolumntype;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.settings.exception.SettingsException;

@Service("schedTempService")
@Transactional
public class SchedTempServiceImpl implements ShedTempService {

	Logger logger = Logger.getLogger(SchedTempServiceImpl.class);
	
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public List<Joschedtempheader> getSchedDescription() throws SettingsException {
		Session aSession = null;
		List<Joschedtempheader> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Criteria aCriteria = aSession.createCriteria(Joschedtempheader.class);
			aCriteria.addOrder(Order.asc("description"));
			// Retrieve all
			aQueryList = aCriteria.list();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}
	
	
	
	@Override
	public List<AutoCompleteBean> getManufacturer(String aName) throws SettingsException {
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aRxmasterBean = null;
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query aQuery = aSession.createSQLQuery("SELECT r.rxMasterId,r.name FROM rxMaster r WHERE r.name Like :aName")
					.setParameter("aName", "%"+aName+"%");
			// Retrieve all
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aRxmasterBean = new AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aRxmasterBean.setId((Integer)aObj[0]);				
				aRxmasterBean.setValue((String)aObj[1]);					
				aRxmasterBean.setLabel((String)aObj[1]);
				aQueryList.add(aRxmasterBean);
			}
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}



	@Override
	public List<Joschedtempcolumn> getScheduleDetails(int theschedTempHeaderID)
			throws SettingsException {
		Session aSession = null;
		List<Joschedtempcolumn> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Criteria aCriteria = aSession.createCriteria(Joschedtempcolumn.class);
			aCriteria.add(Restrictions.eq("joSchedTempHeaderId", theschedTempHeaderID));
			aCriteria.addOrder(Order.asc("columnNumber"));
			// Retrieve all
			aQueryList = aCriteria.list();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}



	@Override
	public List<Joschedtempcolumn> getScheduleDetails(int aFrom, int aTo,Integer theschedTempHeaderID) throws SettingsException {
		Session aSession = null;
		List<Joschedtempcolumn> aQueryList = new ArrayList<Joschedtempcolumn>();
		Query aQuery = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession.createSQLQuery("SELECT jsc.joSchedTempColumnID,jsc.DisplayText,jsc.DisplayWidth,"
					+ "jsc.PrintText,jsc.PrintWidth,jsc.InActive,jsc.CopyDefaults,jsc.importToOrder,jsct.Description,jsc.ColumnNumber from joSchedTempColumn "
					+ "jsc join joSchedTempColumnType as jsct on jsct.joSchedTempColumnTypeID "
					+ "= jsc.joSchedTempColumnTypeID where jsc.joSchedTempHeaderId = :joSchedTempHeaderId order by jsc.ColumnNumber asc");
			aQuery.setParameter("joSchedTempHeaderId", theschedTempHeaderID);
			aQuery.setFirstResult(aFrom);
			aQuery.setMaxResults(aTo);
			List aList = aQuery.list();
			if(aList.size() > 0)
			{
				Joschedtempcolumn aJoschedtempcolumn = null;
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					aJoschedtempcolumn = new Joschedtempcolumn();
					Object[] aObj = (Object[])aIterator.next();
					aJoschedtempcolumn.setJoSchedTempColumnId((Integer) aObj[0]);
					aJoschedtempcolumn.setDisplayText((String) aObj[1]);
					aJoschedtempcolumn.setDisplayWidth((Integer) aObj[2]);
					aJoschedtempcolumn.setPrintText((String) aObj[3]);
					aJoschedtempcolumn.setPrintWidth((Integer) aObj[4]);
					aJoschedtempcolumn.setInactive(getBoolean((Byte) aObj[5]));
					aJoschedtempcolumn.setCopyDefaults(getBoolean((Byte) aObj[6]));
					aJoschedtempcolumn.setImportToOrder((Integer) aObj[7]);
					aJoschedtempcolumn.setScheduleDescription((String) aObj[8]);
					aJoschedtempcolumn.setColumnNumber((Integer) aObj[9]);
					
					aQueryList.add(aJoschedtempcolumn);
				}
			}
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  aQueryList;
	}

	private Boolean getBoolean(Byte aValue)
	{
		return (aValue == 1 ? true : false);
	}


	@Override
	public List<Joschedtempcolumntype> getScheduleType() throws SettingsException {
		Session aSession = null;
		List<Joschedtempcolumntype> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Criteria aCriteria = aSession.createCriteria(Joschedtempcolumntype.class);
			// Retrieve all
			aQueryList = aCriteria.list();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}



	@Override
	@Transactional
	public void deleteJoSchedTempColumn(Integer theJoSchedTempColumnId)
			throws SettingsException {Session aSession = null;
			try {
				// Retrieve session from Hibernate
				aSession = itsSessionFactory.openSession();
				Joschedtempcolumn theJoschedtempcolumn = 
		                   (Joschedtempcolumn)aSession.get(Joschedtempcolumn.class, theJoSchedTempColumnId); 
				aSession.delete(theJoschedtempcolumn); 
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
				throw aSettingsException;		
			} finally {
				aSession.flush();
				aSession.close();
			}
			}



	@Override
	@Transactional
	public void editJoSchedTempColumn(Integer theJoSchedTempColumnId,
			String theDisplayText, Integer theDisplayWidth,
			String thePrintText, Integer thePrintWidth, Boolean theInactive,
			Boolean theCopyDefaults, Integer theImportToOrder,Integer theTypeId)
			throws SettingsException {Session aSession = null;
			try {
				// Retrieve session from Hibernate
				aSession = itsSessionFactory.openSession();
				Joschedtempcolumn theJoschedtempcolumn = 
		                   (Joschedtempcolumn)aSession.get(Joschedtempcolumn.class, theJoSchedTempColumnId); 
				if(theJoschedtempcolumn != null)
				{
					theJoschedtempcolumn.setCopyDefaults(theCopyDefaults);
					theJoschedtempcolumn.setDisplayText(theDisplayText);
					theJoschedtempcolumn.setDisplayWidth(theDisplayWidth);
					theJoschedtempcolumn.setPrintText(thePrintText);
					theJoschedtempcolumn.setPrintWidth(thePrintWidth);
					theJoschedtempcolumn.setImportToOrder(theImportToOrder);
					theJoschedtempcolumn.setInactive(theInactive);
					theJoschedtempcolumn.setJoSchedTempColumnTypeId(theTypeId);
					aSession.update(theJoschedtempcolumn);
				}
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
				throw aSettingsException;		
			} finally {
				aSession.flush();
				aSession.close();
			}
			}



	@Override
	@Transactional
	public void addJoSchedTempColumn(Joschedtempcolumn theJoschedtempcolumn)
			throws SettingsException {Session aSession = null;
			try {
				// Retrieve session from Hibernate
				aSession = itsSessionFactory.openSession();
				aSession.save(theJoschedtempcolumn);
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
				throw aSettingsException;		
			} finally {
				aSession.flush();
				aSession.close();
			}
			}



	@Override
	public int getScheduleCount(Integer theHeaderId) throws SettingsException {
		Session aSession = null;
		Integer aCount = new Integer(0);
		String maxCountQuery = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			maxCountQuery = "SELECT MAX(jsc.ColumnNumber) FROM joSchedTempColumn jsc WHERE jsc.joSchedTempHeaderId = "+theHeaderId;
			Query aQuery = aSession.createSQLQuery(maxCountQuery);
			 aCount =  ((Integer) aQuery.uniqueResult());
			 if(aCount == null){				// if no records are there, this is the first one.
				 aCount = 0; 
			 }
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
			maxCountQuery = null;
		}
		return  aCount;
	}



	@Override
	public void addTemplateDescription(Joschedtempheader theJoschedtempheader) throws SettingsException {
		Session aSession = null;
			try {
				aSession = itsSessionFactory.openSession();
				aSession.save(theJoschedtempheader);
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
				throw aSettingsException;		
			} finally {
				aSession.flush();
				aSession.close();
			}
			}



	@Override
	public boolean checkProduct(String aCode) throws SettingsException {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Criteria aCriteria =  aSession.createCriteria(Joschedtempheader.class);
			aCriteria.add(Restrictions.eq("description", aCode));
			if(aCriteria.list().size() > 0)
				return true;
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
		}



	@Override
	public Integer getHeaderId(String aCode) throws SettingsException {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Criteria aCriteria =  aSession.createCriteria(Joschedtempheader.class);
			aCriteria.add(Restrictions.eq("description", aCode));
			if(aCriteria.list().size() > 0)
				return ((Joschedtempheader)aCriteria.uniqueResult()).getJoSchedTempHeaderId();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
		}



	@Transactional
	@Override
	public void saveJoschedtempcolumn(Joschedtempcolumn aJoschedtemp) throws SettingsException {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(aJoschedtemp);
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		}



	@Override
	public List<Joschedaccessory> getAccessories(Integer aJoSchedTempColumnId)
			throws SettingsException {
		Session aSession = null;
		List<Joschedaccessory> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Criteria aCriteria = aSession.createCriteria(Joschedaccessory.class);
			aCriteria.add(Restrictions.eq("joSchedTempColumnId", aJoSchedTempColumnId));
			aCriteria.addOrder(Order.asc("code"));
			// Retrieve all
			aQueryList = aCriteria.list();
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}



	@Override
	@Transactional
	public void deleteAccessories(Integer theJoSchedAccessoryId)
			throws SettingsException {
		
		Session aSession = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Joschedaccessory theJoschedaccessory = 
	                   (Joschedaccessory)aSession.get(Joschedaccessory.class, theJoSchedAccessoryId); 
			aSession.delete(theJoschedaccessory); 
		} catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
			throw aSettingsException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
			}



	@Override
	public void editAccessories(Integer theJoSchedAccessoryId, String theCode,
			String theDescription) throws SettingsException {
		Session aSession = null;
			try {
				// Retrieve session from Hibernate
				aSession = itsSessionFactory.openSession();
				Joschedaccessory theJoschedaccessory = 
		                   (Joschedaccessory)aSession.get(Joschedaccessory.class, theJoSchedAccessoryId); 
				if(theJoschedaccessory != null)
				{
					theJoschedaccessory.setCode(theCode);
					theJoschedaccessory.setDescription(theDescription);
					aSession.update(theJoschedaccessory);
				}
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
				throw aSettingsException;		
			} finally {
				aSession.flush();
				aSession.close();
			}
			}



	@Override
	public void saveAccessories(Joschedaccessory theJoschedaccessory)
			throws SettingsException {
		Session aSession = null;
			try {
				// Retrieve session from Hibernate
				aSession = itsSessionFactory.openSession();
				aSession.save(theJoschedaccessory);
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				SettingsException aSettingsException = new SettingsException(excep.getCause().getMessage(), excep);
				throw aSettingsException;		
			} finally {
				aSession.flush();
				aSession.close();
			}
			}



}
