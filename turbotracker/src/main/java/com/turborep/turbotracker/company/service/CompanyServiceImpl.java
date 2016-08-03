package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.dao.SimpleCompanyBean;
import com.turborep.turbotracker.company.dao.versionMaintainance;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.exception.VendorException;

@Transactional
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	Logger itsLogger = Logger.getLogger(CompanyServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	
	@Override
	public List<SimpleCompanyBean> getRXArchitectList() throws CompanyException {
		itsLogger.debug("Retrieving Architect List");
		Query aQuery =null;
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory1 = 1 AND Name IS NOT null ORDER BY Name ASC";
		Session aSession = null;
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
			aQuery =null;aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<SimpleCompanyBean> getRXEngineerList() throws CompanyException {
		itsLogger.debug("Retrieving Engineering Company List");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory2 = 1 AND Name IS NOT null ORDER BY Name ASC";
		Session aSession=null;Query query =null;
		List<SimpleCompanyBean> aQueryList = new ArrayList<SimpleCompanyBean>();
		try{
			SimpleCompanyBean aCompanyBean = null;
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = query.list().iterator();
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
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		}finally{
			aSession.flush();
			aSession.close();aSalesselectQry=null;query =null;
		}
		return aQueryList;
	}

	@Override
	public List<SimpleCompanyBean> getRXGCList() throws CompanyException {
		itsLogger.debug("Retrieving General Contractors List");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory3 = 1 AND Name IS NOT null ORDER BY Name ASC";
		Session aSession=null;Query aQuery =null;
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
			aSession.close();aQuery =null;aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<Codivision> getCompanyDivisions() throws CompanyException {
		itsLogger.debug("Retrieving Company Division List");
		Session aSession = null;Query aQuery =null;
		List<Codivision> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession.createQuery("FROM  Codivision");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();aQuery =null;
		}
		return  aQueryList;
	}

	@Override
	public List<CoTaxTerritory> getCompanyTaxTerritory() throws CompanyException {
		itsLogger.debug("Retrieving CoTax List");
		Session aSession = null;
		List<CoTaxTerritory> aQueryList = null;Query aQuery =null;
		try {
		aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  Cotaxterritory ORDER BY county ASC");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return  aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getEngineerRxList(String theSalesRep) throws CompanyException {
		itsLogger.debug("Retrieving Engineering Company names for AutoComplete");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory2 = 1 AND Name IS NOT null  AND Name like "+"'%"+theSalesRep+"%'"+" ORDER BY Name ASC";
		Session aSession=null;Query query = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aUserbean = null;
		try{
			aSession=itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])iterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				aUserbean.setValue((String)aObj[1]);				/** UserName	*/
				aUserbean.setLabel((String)aObj[1]);	
				aQueryList.add(aUserbean);
			}
			if(aQueryList.isEmpty()){
				aUserbean.setValue(" ");	
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;query = null;
		}	
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getArchitectRxList(String theSalesRep) throws CompanyException {
		itsLogger.debug("Retrieving  Architect Company names for AutoComplete");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory1 = 1 AND Name IS NOT null  AND Name like "+"'%"+theSalesRep+"%'"+" ORDER BY Name ASC";
		AutoCompleteBean aUserbean = null;
		Session aSession=null;Query aQuery = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			aSession=itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				aUserbean.setValue((String)aObj[1]);				/** UserName	*/
				aUserbean.setLabel((String)aObj[1]);	
				aQueryList.add(aUserbean);
			}
			if(aQueryList.isEmpty()){
				aUserbean = new  AutoCompleteBean();
				aUserbean.setValue(" ");	
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getGCRXList(String theSalesRep) throws CompanyException {
		itsLogger.debug("Retrieving GC names for Auto Complete");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsCategory3 = 1 AND Name IS NOT null  AND Name like "+"'%"+theSalesRep+"%'"+" ORDER BY Name ASC";
		Session aSession=null;Query aQuery = null;
		AutoCompleteBean aUserbean = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			aSession=itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				aUserbean.setValue((String)aObj[1]);				/** UserName	*/
				aUserbean.setLabel((String)aObj[1]);	
				aQueryList.add(aUserbean);
			}
			if(aQueryList.isEmpty()){
				aUserbean = new  AutoCompleteBean();
				aUserbean.setValue(" ");	
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCompanyTaxList(String theSalesRep) throws CompanyException {
		itsLogger.debug("Retrieving Company Tax List");
		String aSalesselectQry = "SELECT coTaxTerritoryID, County, TaxRate, State,taxfreight  FROM coTaxTerritory WHERE County like "+"'%"+theSalesRep+"%'"+" ORDER BY County ASC";
		Session aSession=null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			AutoCompleteBean aUserbean = null;
			aSession=itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				aUserbean.setValue((String)aObj[1]);				/** UserName	*/
				String taxCountryState = (String)aObj[1] + "," + (String)aObj[3];
				aUserbean.setLabel(taxCountryState);	
				aUserbean.setTaxValue((BigDecimal)aObj[2]);	
				aUserbean.setTaxfreight((Byte) aObj[4]);
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public String getRolodexName(Integer theRxCategory, String theTablename) throws CompanyException {
		itsLogger.debug("Retrieving Rolodex Name");
		String aSalesselectQry = "SELECT Name FROM "+theTablename+ " WHERE rxMasterID = '"+theRxCategory+"' ORDER BY Name ASC";
		Session aSession=null;
		String aArchitect = "";
		Query query =null;
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aSalesselectQry);
			List<?> aList = query.list();
			aArchitect = (String) aList.get(0);
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;query =null;
		}
		return aArchitect;
	}
	
	@Override
	public String getCustomerName(Integer theRxCustomerId) throws CompanyException {
		itsLogger.debug("Retrieving Customer names");
		String aSalesselectQry = "SELECT Name FROM rxMaster WHERE IsCustomer=1 AND rxMasterId= '"+theRxCustomerId+"' ;";
		Session aSession=null;Query query = null;
		String aCustomerName = "";
		try{
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aSalesselectQry);
			List<?> aList = query.list();
			if (aList.size() <= 0) {
				return null;
			}
			aCustomerName = (String) aList.get(0);
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;query = null;
		}
		return aCustomerName;
	}

	@Override
	public List<CoTaxTerritory> getCompanyTaxTerritory(Integer theCoTaxTerritoryId, String theTableNameRX) throws CompanyException {
		itsLogger.debug("Retrieving CompanyTaxTerritory List");
		String aSalesselectQry ="SELECT County, TaxRate FROM coTaxTerritory WHERE coTaxTerritoryID IS NOT null AND coTaxTerritoryID = '"+theCoTaxTerritoryId+"' ;";
		Session aSession=null;
		Query aQuery =null;
		ArrayList<CoTaxTerritory> aQueryList = new ArrayList<CoTaxTerritory>();
		try{
			CoTaxTerritory aTax = null;
			aSession=itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aTax = new  CoTaxTerritory();
				Object[] aObj = (Object[])aIterator.next();
				aTax.setCounty((String)aObj[0]);				
				aTax.setTaxRate((BigDecimal)aObj[1]);		
				aQueryList.add(aTax);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;aQuery =null;
			
		}
		return aQueryList;
	}
	
	public Rxmaster getEmployeeDetails(String theRolodexNumber) throws CompanyException {
		itsLogger.debug("Getting Employee Details");
		Session aSession = null;
		Rxmaster aRxMaster = null;
		Integer aRxMasterID = Integer.parseInt(theRolodexNumber);
		try {
			aSession = itsSessionFactory.openSession();
			aRxMaster = (Rxmaster) aSession.get(Rxmaster.class, aRxMasterID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aRxMaster;
	}

	public Rxaddress getEmployeeAddressDetails(String theRolodexNumber) throws CompanyException {
		itsLogger.debug("Getting Employee Address Details");
		Session aSession = null;
		Rxaddress aRxAddress = null;
		Integer aRxMasterID = Integer.parseInt(theRolodexNumber);
		Integer aRxAddressID = getRxAddressID(aRxMasterID);
		try {
			aSession = itsSessionFactory.openSession();
			aRxAddress = (Rxaddress) aSession.get(Rxaddress.class, aRxAddressID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aRxAddress;
	}
	
	public Integer getRxAddressID(Integer theRxmasterId) throws CompanyException {
		itsLogger.debug("Getting RxAddressID");
		String aRxAddressQry = "SELECT rxAddressId FROM rxAddress WHERE rxMasterId="+theRxmasterId;
		Session aSession=null;Query aQuery = null;
		Integer aRxaddress = 0;
		try{
			aSession=itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aRxAddressQry);
			List<?> aList = aQuery.list();
			if(aList.size()>0){
			aRxaddress = (Integer) aList.get(0);
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aRxAddressQry=null;aQuery = null;
		}
		return aRxaddress;
	}

	@Override
	public Integer getRxMasterID(String theLocationName) throws CompanyException {
		String aReplaceLocationName = theLocationName.replaceAll("'", "''");
		itsLogger.debug("Getting RxMasterID");
		String aRxMasterQry = "SELECT rxMasterID FROM rxMaster WHERE Name like '%"+aReplaceLocationName+"%'";
		Session aSession=null;
		Integer aRxMasterID = null;Query query = null;
		try{
			aSession=itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aRxMasterQry);
			List<?> aList = query.list();
			if(!aList.isEmpty())
			aRxMasterID = (Integer) aList.get(0);	
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aRxMasterQry=null;query = null;
		}
		return aRxMasterID;
	}

	@Override
	public String getEmailID(Integer theCustomerID) throws CompanyException {
		itsLogger.debug("Getting EmailID");
		if(theCustomerID == null){
			theCustomerID = 0;
		}
		String aRxContactQry = "SELECT EMail FROM rxContact WHERE rxContactID = "+theCustomerID;
		Session aSession=null;
		String aEmailId = null;Query query =null;
		try{
			aSession=itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aRxContactQry);
			List<?> aList = query.list();
			if(!aList.isEmpty())
			aEmailId = (String) aList.get(0);	
			if(aEmailId == null){
				aEmailId = "";
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aRxContactQry =null;query =null;
		}
		return aEmailId;
	}

	@Override
	public Rxcontact updateEmailAddr(Rxcontact theRxcontact) throws CompanyException {
		itsLogger.debug("Update Email Addr in rxContact.");
		Session aSession = itsSessionFactory.openSession();
		Rxcontact aRxcontact = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aRxcontact = (Rxcontact) aSession.get(Rxcontact.class, theRxcontact.getRxContactId());
			aRxcontact.setEmail(theRxcontact.getEmail());
			aRxcontact.setRxContactId(theRxcontact.getRxContactId());
			aSession.update(aRxcontact);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxcontact;
	}

	@Override
	public List<CuTerms> getCuTermsList() throws CompanyException {
		Session aSession = null;
		List<CuTerms> aQueryList = null;
		Query aQuery =null;
		try {
		aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createQuery("FROM  CuTerms ORDER");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			 aQuery =null;
		}
		return  aQueryList;
	}
	@Override
	public List<?> getFreightCharges() throws CompanyException {
		Session aSession = null;
		List<Vefreightcharges> aQueryList = new ArrayList<Vefreightcharges>();
		String aCustomerQry = "SELECT * FROM veFreightCharges ORDER BY Description ASC "; /**LIMIT " + theFrom + ", " + theTo;*/
		Vefreightcharges aVeFreight = null;Query aQuery =null;
		try{
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				
				aVeFreight = new Vefreightcharges();
				Object[] aObj = (Object[])aIterator.next();
				aVeFreight.setVeFreightChargesId((Integer)aObj[0]);
				aVeFreight.setDescription((String)aObj[1]);
				Byte inActive= ((Byte) aObj[2]);
				aVeFreight.setInActive(false);
				if(inActive == 1){
					aVeFreight.setInActive(true);
				}
				Byte askForNote= ((Byte) aObj[3]);
				aVeFreight.setAskForNote(false);
				if(askForNote == 1){
					aVeFreight.setAskForNote(true);
				}
				//boolean inactive = (Boolean) aObj[2];
				//aVeFreight.setInActive((byte) aObj[2]);
				aQueryList.add(aVeFreight);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;aQuery =null;
		}
		return aQueryList;	
	}
	
	@Override
	public Vefreightcharges updateFreightChargesService(Vefreightcharges theRxcontact,Boolean isAdd) throws CompanyException {
		itsLogger.debug("Update Email Addr in Vefreightcharges.");
		Session aSession = itsSessionFactory.openSession();
		Vefreightcharges aRxcontact = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if(!isAdd){
				aRxcontact = (Vefreightcharges) aSession.get(Vefreightcharges.class, theRxcontact.getVeFreightChargesId());
				aRxcontact.setInActive(theRxcontact.getInActive());
				aRxcontact.setDescription(theRxcontact.getDescription());
				aRxcontact.setAskForNote(theRxcontact.getAskForNote());
				aRxcontact.setVeFreightChargesId(theRxcontact.getVeFreightChargesId());
				aSession.update(aRxcontact);
			}else{
			aRxcontact = new Vefreightcharges();
			aRxcontact.setInActive(theRxcontact.getInActive());
			aRxcontact.setDescription(theRxcontact.getDescription());
			aRxcontact.setAskForNote(theRxcontact.getAskForNote());
			aSession.save(aRxcontact);
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxcontact;
	}
	public void deleteFreightChargesService(Integer theFreightId) throws CompanyException{
		itsLogger.debug("Update Email Addr in Vefreightcharges.");
		Session aSession = itsSessionFactory.openSession();
		Vefreightcharges aRxcontact = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aRxcontact = (Vefreightcharges) aSession.get(Vefreightcharges.class, theFreightId);
			aSession.delete(aRxcontact);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public List<AutoCompleteBean> getcotaxterritoryList() throws CompanyException{
		String aSearchSelectQry = "select coTaxTerritoryID,County,State,TaxRate from coTaxTerritory order by County";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			if(aQuery.list()!=null){
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				AutoCompleteBean theSearchBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				//theSearchBean.setValue((String)aObj[3]);
				theSearchBean.setLabel(aObj[1]+ " , " +aObj[2] );
				theSearchBean.setId((Integer) aObj[0]);

				aQueryList.add(theSearchBean);
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getAddressShippingList(int rxmasterid) throws CompanyException{
		String aSearchSelectQry = "SELECT ra.coTaxTerritoryID,ra.Address1,ra.Address2,ra.City,ra.State,ra.Zip,COT.TaxRate,ra.rxAddressID,COT.county FROM rxAddress ra LEFT JOIN coTaxTerritory COT ON(ra.coTaxTerritoryID=COT.coTaxTerritoryID) WHERE rxMasterID="+rxmasterid+" AND IsShipTo=1";
		itsLogger.info("aSearchSelectQry=="+aSearchSelectQry);
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSearchSelectQry);
			if(aQuery.list()!=null){
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				AutoCompleteBean theSearchBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				if(aObj[1]==null){
					aObj[1]=" ";
				}
				if(aObj[2]==null){
					aObj[2]=" ";
				}
				if(aObj[3]==null){
					aObj[3]=" ";
				}
				if(aObj[4]==null){
					aObj[4]=" ";
				}
				if(aObj[5]==null){
					aObj[5]=" ";
				}
				
				//theSearchBean.setValue((String)aObj[3]);
				theSearchBean.setLabel(aObj[1]+ "  " +aObj[2] +" "+aObj[3]+" ,"+aObj[4]+" "+aObj[5]);
				theSearchBean.setEntity((String)aObj[8]);
				
				int cotaxterritoryid=-1;
				if(aObj[0]!=null){
					cotaxterritoryid=(Integer) aObj[0];
				}
				theSearchBean.setId(cotaxterritoryid);
			
				if(aObj[6]==null){
					aObj[6]="";
				}else{
					aObj[6]=new DecimalFormat("##.00").format(aObj[6]);
					aObj[6]=aObj[6]+"%";
					if(aObj[6].equals(".00%")){
						aObj[6]="0.00%";
					}
				}
				
				theSearchBean.setValue((String) aObj[6]);
				theSearchBean.setRxAddressID((Integer) aObj[7]);
				
				aQueryList.add(theSearchBean);
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;aQuery =null;
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getcuDefaultTerritoryList(int rxmasterid) throws CompanyException{
		String aSearchSelectQry = "SELECT ra.coTaxTerritoryID,ra.Address1,ra.Address2,ra.City,ra.State,ra.Zip,COT.TaxRate,ra.rxAddressID,COT.county FROM rxAddress ra LEFT JOIN coTaxTerritory COT ON(ra.coTaxTerritoryID=COT.coTaxTerritoryID) WHERE rxMasterID="+rxmasterid+" AND IsDefault=1";
		itsLogger.info("aSearchSelectQry=="+aSearchSelectQry);
		Session aSession = null;Query aQuery = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSearchSelectQry);
			if(aQuery.list()!=null){
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				AutoCompleteBean theSearchBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				if(aObj[1]==null){
					aObj[1]=" ";
				}
				if(aObj[2]==null){
					aObj[2]=" ";
				}
				if(aObj[3]==null){
					aObj[3]=" ";
				}
				if(aObj[4]==null){
					aObj[4]=" ";
				}
				if(aObj[5]==null){
					aObj[5]=" ";
				}
				
				//theSearchBean.setValue((String)aObj[3]);
				theSearchBean.setLabel(aObj[1]+ "  " +aObj[2] +" "+aObj[3]+" ,"+aObj[4]+" "+aObj[5]);
				theSearchBean.setEntity((String)aObj[8]);
				
				int cotaxterritoryid=-1;
				if(aObj[0]!=null){
					cotaxterritoryid=(Integer) aObj[0];
				}
				theSearchBean.setId(cotaxterritoryid);
			
				if(aObj[6]==null){
					aObj[6]="";
				}else{
					aObj[6]=new DecimalFormat("##.00").format(aObj[6]);
					aObj[6]=aObj[6]+"%";
					if(aObj[6].equals(".00%")){
						aObj[6]="0.00%";
					}
				}
				
				theSearchBean.setValue((String) aObj[6]);
				theSearchBean.setRxAddressID((Integer) aObj[7]);
				
				aQueryList.add(theSearchBean);
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;aQuery = null;
		}
		return aQueryList;
	}
	
	
	
	@Override
	public List<String> getsalesDetails(int rxmasterid) throws CompanyException{
		//
		String aSearchSelectQry = "SELECT SUM(SubTotal) AS TheAmount FROM cuInvoice WHERE TransactionStatus>0 AND rxCustomerID=495 AND YEAR(InvoiceDate)=2014";
		Session aSession = null;Query aQuery =null;
		ArrayList<String> aQueryList = new ArrayList<String>();
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSearchSelectQry);
			if(aQuery.list()!=null){
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
			
				
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;aQuery =null;
		}
		
		
		 aSearchSelectQry = "SELECT SUM(SubTotal) AS TheAmount FROM cuInvoice WHERE TransactionStatus>0 AND rxCustomerID=495 AND YEAR(InvoiceDate)=2013";
		 aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSearchSelectQry);
			if(aQuery.list()!=null){
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
			
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;aQuery =null;
		}
		
		String invoiceDate="";
		String LatestInv_Amt="0.00";
		aSearchSelectQry = "SELECT  InvoiceDate, SubTotal From cuInvoice Where SubTotal>0 AND rxCustomerID=495 AND (TransactionStatus > 0) ORDER BY InvoiceDate DESC limit 1";
		 aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSearchSelectQry);
			if(aQuery.list()!=null){
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				invoiceDate=(String)aObj[0];
				LatestInv_Amt=(String)aObj[1];
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;aQuery =null;
		}
		
		return aQueryList;
	}
	
	@Override
	public ArrayList<Rxaddress> getAllAddress(Integer rxMasterID)
			throws CompanyException {
		// TODO Auto-generated method stube
		
		Session aSession = null;Query aQuery =null;
		ArrayList<Rxaddress> rxAddress= new ArrayList<Rxaddress>();
		Rxaddress rxaddrobj = null;
		
		try {
			aSession = itsSessionFactory.openSession();
			//aQuery = aSession.createQuery("FROM Rxaddress as rx Left join fetch rx.Rxmaster as rm where rm.rxMasterId="+rxMasterID+" and rx.isShipTo=1");
			aQuery = aSession.createSQLQuery("Select {rx.*},{rm.*} FROM rxAddress rx Left join rxMaster rm on rx.rxMasterID = rm.rxMasterID where rx.rxMasterID="+rxMasterID+" and rx.isShipTo=1 order by IsDefault Desc")
					.addEntity("rx",Rxaddress.class)
					.addEntity("rm",Rxmaster.class);
			
			java.util.Iterator pairs = aQuery.list().iterator();
			while (pairs.hasNext()) {
				rxaddrobj = new Rxaddress();
				Object[] pair = (Object[]) pairs.next();
				Rxaddress rxadd = (Rxaddress) pair[0];
				Rxmaster rxmas = (Rxmaster) pair[1];
				rxaddrobj.setRxAddressId(rxadd.getRxAddressId());
				rxaddrobj.setRxMasterId(rxadd.getRxMasterId());
				rxaddrobj.setInActive(rxadd.getInActive());
				rxaddrobj.setName(rxmas.getName());
				rxaddrobj.setAddress1(rxadd.getAddress1());
				rxaddrobj.setAddress2(rxadd.getAddress2());
				rxaddrobj.setCity(rxadd.getCity());
				rxaddrobj.setState(rxadd.getState());
				rxaddrobj.setZip(rxadd.getZip());
				rxaddrobj.setIsStreet(rxadd.getIsStreet());
				rxaddrobj.setIsMailing(rxadd.getIsMailing());
				rxaddrobj.setIsBillTo(rxadd.getIsBillTo());
				rxaddrobj.setIsShipTo(rxadd.getIsShipTo());
				rxaddrobj.setCoTaxTerritoryId(rxadd.getCoTaxTerritoryId());
				rxaddrobj.setOtherBillTo(rxadd.getOtherBillTo());
				rxaddrobj.setOtherShipTo(rxadd.getOtherShipTo());
				rxaddrobj.setPhone1(rxadd.getPhone1());
				rxaddrobj.setPhone2(rxadd.getPhone2());
				rxaddrobj.setFax(rxadd.getFax());
				rxaddrobj.setIsDefault(rxadd.getIsDefault());
				
				rxAddress.add(rxaddrobj);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		
		
		
		return rxAddress;
	}
	
	
	@Override
	public Boolean getdbVersion(String appversion,String dbversion) {
		String aSalesselectQry = "select * from versionMaintainance limit 1";
		Session aSession=null;Query aQuery =null;
		boolean returnvalue=true;
		try{
			SimpleCompanyBean aCompanyBean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				String dbver=(String)aObj[2];
				Integer id=(Integer)aObj[0];
				if(dbver!=null&&dbver.equals(dbversion)){
					versionMaintainance vm=new versionMaintainance();
					vm.setAppversion(appversion);
					vm.setDbversion(dbver);
					vm.setId(id);
					updateAppVersion(vm);
					returnvalue=false;
				}
			}
		}catch(Exception e){
		}finally{
			aSession.flush();
			aSession.close();aQuery =null;aSalesselectQry=null;
		}
		return returnvalue;
	}

	public boolean updateAppVersion(versionMaintainance theversionMaintainance) throws CompanyException {
		itsLogger.debug("Update Email Addr in rxContact.");
		Session aSession = itsSessionFactory.openSession();
		versionMaintainance aversionMaintainance = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aversionMaintainance = (versionMaintainance) aSession.get(versionMaintainance.class, theversionMaintainance.getId());
			aversionMaintainance.setAppversion(theversionMaintainance.getAppversion());
			aversionMaintainance.setDbversion(theversionMaintainance.getDbversion());
			aSession.update(aversionMaintainance);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			CompanyException aCompanyException = new CompanyException(e.getCause().getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public int getCount(String query)  {
		Session aSession = null;
		BigInteger aTotalCount = null;
		Query aQuery = null;
		List<?> aList = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(query);
			aList = aQuery.list();
			if(aList!=null && aList.size()>0){
			    aTotalCount = new BigInteger(String.valueOf(aList.size())) ;
			}else{
				aTotalCount=new BigInteger("0");
			}
			
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
			aList = null;
		}
		return aTotalCount.intValue();
	}
	
	@Override
	public int getNewQueryCount(String query)  {
		Session aSession = null;
		int aTotalCount = 0;
		Query aQuery = null;
		List<?> aList = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(query);
			aList = aQuery.list();
			if(aList.get(0)!=null){
				aTotalCount = ((BigInteger) aList.get(0)).intValue();
			}
			
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
			aList = null;
		}
		return aTotalCount;
	}
}