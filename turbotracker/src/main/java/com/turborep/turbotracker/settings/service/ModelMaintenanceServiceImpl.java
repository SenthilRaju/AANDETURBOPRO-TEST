package com.turborep.turbotracker.settings.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.job.dao.Joscheduledetail;
import com.turborep.turbotracker.job.dao.Joschedulemodel;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobServiceImpl;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.settings.exception.SettingsException;

@Service("modelMaintenanceService")
@Transactional
public class ModelMaintenanceServiceImpl implements ModelMaintenanceService {
	
	protected static Logger itsLogger = Logger.getLogger(JobServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;

	@Override
	public List<Joschedtempcolumn> getColumnName(Integer theSchedModelID)
			throws SettingsException {
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("From Joschedtempcolumn j where j.joSchedTempHeaderId = :joSchedTempHeaderId and j.copyDefaults = 1 ORDER BY j.columnNumber asc");
			aQuery.setParameter("joSchedTempHeaderId", theSchedModelID);
			return aQuery.list();
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public List<?> getModelMaintenaceList(Integer theJoSchedTempHeaderID,String theParameters,String theSearch) throws SettingsException {
		itsLogger.debug("Query to Select All ModelMaintenaceList");
		String aLike = "";
		if(theSearch != null && !theSearch.isEmpty())
		{
			aLike = " AND pm.ItemCode LIKE :theSearch";
		}
		String aSubmittalQry="SELECT "+theParameters+" FROM joScheduleModel jsm LEFT JOIN prMaster pm ON jsm.prMasterID = pm.prMasterID LEFT JOIN tsUserLogin tul ON jsm.CreatedByID = tul.UserLoginID WHERE jsm.joSchedTempHeaderID = :theJoSchedTempHeaderID"+aLike;
		Session aSession = null;
		List<?> aQryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			aQuery.setParameter("theJoSchedTempHeaderID", theJoSchedTempHeaderID);
			if(theSearch != null && !theSearch.isEmpty())
			{
				aQuery.setParameter("theSearch", theSearch+"%");
			}
			aQryList = aQuery.list();
					}catch(Exception e) {
			itsLogger.error("Exception while getting the Model Maintenace list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry = null;
		}
		return aQryList;
	}
	
	@Override
	public List<?> getModelMaintenaceList(Integer theJoSchedTempHeaderID,String theParameters,int aFrom,int aTo,String theSearch) throws SettingsException {
		itsLogger.debug("Query to Select All ModelMaintenaceList");
		String aLike = "";
		if(theSearch != null && !theSearch.isEmpty())
		{
			aLike = " AND pm.ItemCode LIKE :theSearch";
		}
		String aSubmittalQry="SELECT "+theParameters+" FROM joScheduleModel jsm LEFT JOIN prMaster pm ON jsm.prMasterID = pm.prMasterID LEFT JOIN tsUserLogin tul ON jsm.CreatedByID = tul.UserLoginID WHERE jsm.joSchedTempHeaderID = :theJoSchedTempHeaderID"+aLike;
		Session aSession = null;
		List<?> aQryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			aQuery.setParameter("theJoSchedTempHeaderID", theJoSchedTempHeaderID);
			if(theSearch != null && !theSearch.isEmpty())
			{
				aQuery.setParameter("theSearch", theSearch+"%");
			}
			aQuery.setFirstResult(aFrom);
			aQuery.setMaxResults(aTo);
			aQryList = aQuery.list();
					}catch(Exception e) {
			itsLogger.error("Exception while getting the Model Maintenace list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry = null;
		}
		return aQryList;
	}
	
	
	@Override
	public List<?> getSchedDescription() throws SettingsException {
		itsLogger.debug("Query to Select All joSchedTempHeader");
		String aSubmittalQry="SELECT jsh.joSchedTempHeaderID,jsh.Description,jsh.Product,rm.Name FROM joSchedTempHeader jsh,rxMaster rm WHERE jsh.rxManufacturerID = rm.rxMasterID order by jsh.Description asc";
		Session aSession = null;
		List<?> aQryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			aQryList = aQuery.list();
					}catch(Exception e) {
			itsLogger.error("Exception while getting the Model Maintenace list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry=null;
		}
		return aQryList;
	}

	@Override
	public List<?> getProductCategory(String aKeyword) throws SettingsException {
		itsLogger.debug("Query to Select All getProductCategory");
		Session aSession = null;
		String aSubmittalQry="SELECT pm.prMasterId,pm.itemCode,pm.description FROM Prmaster pm WHERE pm.isInventory=0 AND pm.description Like :description";
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aRxmasterBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aSubmittalQry).setParameter("description", "%"+aKeyword+"%");
			if(!aQuery.list().isEmpty())
			{
				Iterator<?> aIterator = aQuery.iterate();
				while(aIterator.hasNext()) {
					aRxmasterBean = new AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aRxmasterBean.setId((Integer)aObj[0]);				
				aRxmasterBean.setValue((String)aObj[1]);					
				aRxmasterBean.setLabel((String)aObj[2]+"("+(String)aObj[1]+")");
				aQueryList.add(aRxmasterBean);
			}
			}
					}catch(Exception e) {
			itsLogger.error("Exception while getting the ProductCategory list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry = null;
		}
		return aQueryList;
	}

	@Override
	public void addScheduleModel(Joschedulemodel aJoschedulemodel)
			throws SettingsException {
		Session aSession = itsSessionFactory.openSession();
		try {
			aSession.save(aJoschedulemodel);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getMessage(), excep);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void editScheduleModel(Joschedulemodel aJoschedulemodel)
			throws SettingsException {
		Session aSession = null;
		Joschedulemodel theJoschedulemodel = null;
		try {
			aSession = itsSessionFactory.openSession();
			 theJoschedulemodel = (Joschedulemodel) aSession.get(Joschedulemodel.class, aJoschedulemodel.getJoScheduleModelId());
			if(theJoschedulemodel != null){
				theJoschedulemodel.setCol01(aJoschedulemodel.getCol01());
				theJoschedulemodel.setCol02(aJoschedulemodel.getCol02());
				theJoschedulemodel.setCol03(aJoschedulemodel.getCol03());
				theJoschedulemodel.setCol04(aJoschedulemodel.getCol04());
				theJoschedulemodel.setCol05(aJoschedulemodel.getCol05());
				theJoschedulemodel.setCol06(aJoschedulemodel.getCol06());
				theJoschedulemodel.setCol07(aJoschedulemodel.getCol07());
				theJoschedulemodel.setCol08(aJoschedulemodel.getCol08());
				theJoschedulemodel.setCol09(aJoschedulemodel.getCol09());
				theJoschedulemodel.setCol10(aJoschedulemodel.getCol10());
				theJoschedulemodel.setCol11(aJoschedulemodel.getCol11());
				theJoschedulemodel.setCol12(aJoschedulemodel.getCol12());
				theJoschedulemodel.setCol13(aJoschedulemodel.getCol13());
				theJoschedulemodel.setCol14(aJoschedulemodel.getCol14());
				theJoschedulemodel.setCol15(aJoschedulemodel.getCol15());
				theJoschedulemodel.setCol16(aJoschedulemodel.getCol16());
				theJoschedulemodel.setCol17(aJoschedulemodel.getCol17());
				theJoschedulemodel.setCol18(aJoschedulemodel.getCol18());
				theJoschedulemodel.setCol19(aJoschedulemodel.getCol19());
				theJoschedulemodel.setCol20(aJoschedulemodel.getCol20());
				if(aJoschedulemodel.getPrMasterId() != null)
				theJoschedulemodel.setPrMasterId(aJoschedulemodel.getPrMasterId());
				theJoschedulemodel.setWebAddress(aJoschedulemodel.getWebAddress());
				theJoschedulemodel.setCreatedByID(aJoschedulemodel.getCreatedByID());
				theJoschedulemodel.setModelNo(aJoschedulemodel.getModelNo());
				aSession.update(theJoschedulemodel);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getMessage(), excep);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void deleteScheduleModel(Integer theJoScheduleModelId)
			throws SettingsException {
		Session aSession = itsSessionFactory.openSession();
		Joschedulemodel aJoschedulemodel = new Joschedulemodel();
		try {
			aJoschedulemodel = (Joschedulemodel) aSession.get(Joschedulemodel.class, theJoScheduleModelId);
			aSession.delete(aJoschedulemodel);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getMessage(), excep);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public ArrayList<AutoCompleteBean> getItemCode(String aKeyword) throws SettingsException{
		itsLogger.debug("Query to Select All getItemCode");
		Session aSession = null;
		String aSubmittalQry="SELECT pm.prMasterId,pm.itemCode,pm.description FROM Prmaster pm WHERE pm.description Like :description";
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aRxmasterBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aSubmittalQry).setParameter("description", "%"+aKeyword+"%");
			if(!aQuery.list().isEmpty())
			{
				Iterator<?> aIterator = aQuery.iterate();
				while(aIterator.hasNext()) {
					aRxmasterBean = new AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aRxmasterBean.setId((Integer)aObj[0]);				
				aRxmasterBean.setValue((String)aObj[1]);					
				aRxmasterBean.setLabel((String)aObj[2]+"("+(String)aObj[1]+")");
				aQueryList.add(aRxmasterBean);
			}
			}
					}catch(Exception e) {
			itsLogger.error("Exception while getting the ProductCategory list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry = null;
		}
		return aQueryList;
	}

	@Override
	public void updateproductCategory(Integer aScheduleModelID,
			Integer aPrMasterId) throws SettingsException {
		Session aSession = null;
		Joschedulemodel theJoschedulemodel = null;
		try {
			aSession = itsSessionFactory.openSession();
			 theJoschedulemodel = (Joschedulemodel) aSession.get(Joschedulemodel.class, aScheduleModelID);
			if(theJoschedulemodel != null){
				theJoschedulemodel.setCategoryID(aPrMasterId);
				aSession.update(theJoschedulemodel);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			SettingsException aSettingsException = new SettingsException(excep.getMessage(), excep);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public String getProductCategory(Integer joScheduleModelID)
			throws SettingsException {
		itsLogger.debug("Query to Select All getProductCategory");
		Session aSession = null;
		String aSubmittalQry="SELECT pm.ItemCode FROM joScheduleModel jsm LEFT JOIN prMaster pm ON jsm.CategoryID = pm.prMasterID WHERE jsm.joScheduleModelID = :joScheduleModelID";
		String theName = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			aQuery.setParameter("joScheduleModelID", joScheduleModelID);
			if(!aQuery.list().isEmpty())
			{
				theName = (String)aQuery.uniqueResult();
			}
					}catch(Exception e) {
			itsLogger.error("Exception while getting the ProductCategory list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry = null;
		}
		return theName;
	}

	@Override
	public String getManufacurersLink(Integer joSchedTempHeaderId)
			throws SettingsException {
		itsLogger.debug("Query to Select All getManufacurersLink");
		Session aSession = null;
		String theWebSite = null;
		try {
			
			aSession = itsSessionFactory.openSession();
			Joschedtempheader aJoschedtempheader = (Joschedtempheader) aSession.get(Joschedtempheader.class, joSchedTempHeaderId);
			if(aJoschedtempheader != null)
			{
				Integer aRxMasterId = aJoschedtempheader.getRxManufacturerId();
				Rxmaster aRxmaster = (Rxmaster) aSession.get(Rxmaster.class, aRxMasterId);
				if(aRxmaster != null)
				{
					theWebSite = aRxmaster.getWebsight();
				}
			}
					}catch(Exception e) {
			itsLogger.error("Exception while getting the ProductCategory list: " + e.getMessage(),e);
			SettingsException aSettingsException = new SettingsException(e.getMessage(), e);
			throw aSettingsException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return theWebSite;
	}

}
