package com.turborep.turbotracker.Rolodex.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.RxJournal;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.employee.dao.EmMasterBean;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.RxMasterCategoryView;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.dao.Rxmastercategory1;
import com.turborep.turbotracker.employee.dao.Rxmastercategory2;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.Jobidder;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.Vemaster;

@Service("rolodexService")
@Transactional
public class RolodexServiceImpl implements RolodexService {
	
	protected static Logger itsLogger = Logger.getLogger("services");
	
	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public List<RolodexBean> getAll(int theFrom, int theRows,String category) {
		itsLogger.info("Retrieving all ROLODEX List");
		Session aSession = null;
		List<RolodexBean> aQueryList = new ArrayList<RolodexBean>();
		String condition="";
		/*if(category!=null){
			if(category.equalsIgnoreCase("customer")){
				condition="AND IsCustomer=1";
			}else if(category.equalsIgnoreCase("vendor")){
				condition="AND IsVendor=1";
			}else if(category.equalsIgnoreCase("employee")){
				condition="AND IsEmployee=1";
			}else if(category.equalsIgnoreCase("engineer")){
				condition="AND IsCategory2=1";
			}else if(category.equalsIgnoreCase("architect")){
				condition="AND IsCategory1=1";
			}else if(category.equalsIgnoreCase("gc")){
				condition="AND IsCategory3=1";
			}else if(category.equalsIgnoreCase("owner")){
				condition="AND IsCategory4=1";
			}else if(category.equalsIgnoreCase("bond")){
				condition="AND IsCategory5=1";
			}
		}*/
		if(category!=null){
			if(category.equalsIgnoreCase("9")){
				condition="AND IsCustomer=1";
			}else if(category.equalsIgnoreCase("10")){
				condition="AND IsVendor=1";
			}else if(category.equalsIgnoreCase("11")){
				condition="AND IsEmployee=1";
			}else if(category.equalsIgnoreCase("1")){
				condition="AND IsCategory1=1";
			}else if(category.equalsIgnoreCase("2")){
				condition="AND IsCategory2=1";
			}else if(category.equalsIgnoreCase("3")){
				condition="AND IsCategory3=1";
			}else if(category.equalsIgnoreCase("4")){
				condition="AND IsCategory4=1";
			}else if(category.equalsIgnoreCase("5")){
				condition="AND IsCategory5=1";
			}else if(category.equalsIgnoreCase("6")){
				condition="AND IsCategory6=1";
			}else if(category.equalsIgnoreCase("7")){
				condition="AND IsCategory7=1";
			}else if(category.equalsIgnoreCase("8")){
				condition="AND IsCategory8=1";
			}
		}
		String aStr = "SELECT " +
								"rxMaster.rxMasterID," +
								"rxMaster.name, " +
								"rxMaster.phone1, " +
								"rxAddress.city, " +
								"rxAddress.state, " +
								"rxAddress.Address1 " +
								"FROM rxMaster " +
								"JOIN rxAddress on rxMaster.rxMasterID = rxAddress.rxMasterID " +
								"WHERE rxMaster.name IS NOT NULL AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' "+condition+
								" Group By rxMaster.rxMasterID ORDER BY rxMaster.name LIMIT " + theFrom + ", " + theRows;
		RolodexBean aRolodexBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aStr);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new RolodexBean();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setRxMasterId((Integer) aObj[0]);	/** rxMaster.rxMaserId	*/
				aRolodexBean.setName((String) aObj[1]);			/**	rxMaster.name		*/
				aRolodexBean.setPhone1((String) aObj[2]);		/**	rxAddress.phone1	*/
				aRolodexBean.setCity((String)aObj[3]);			/**	rxAddress.city		*/
				aRolodexBean.setState((String) aObj[4]);			/**	rxAddress.state		*/
				aRolodexBean.setAddress1((String) aObj[5]);		/**	rxAddress.Address1	*/
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aStr=null;
			condition=null;
		}
		return aQueryList;	
	}
/*Updated By: Velmurugan
 *Updated On: 10-9-2014
 *Description:Fetching the customer from rxmaster table and get the address if it is default 1 
 * */
	public List<RolodexBean> getCustomers(int theFrom, int theTo){
		Session aSession = null;
		List<RolodexBean> aQueryList = new ArrayList<RolodexBean>();
		String aCustomerQry = "SELECT " +
												"rxMaster.rxMasterID," +
												"rxMaster.name, " +
												"rxMaster.phone1, " +
												"rxAddress.city, " +
												"rxAddress.state, " +
												"rxAddress.Address1 " +
												/*"(SELECT city FROM rxAddress WHERE rxMasterID=rxMaster.rxMasterID ORDER BY rxAddressID  LIMIT 1) AS city,"+
												" (SELECT state FROM rxAddress WHERE rxMasterID=rxMaster.rxMasterID ORDER BY rxAddressID LIMIT 1) AS state,"+
												" (SELECT Address1 FROM rxAddress WHERE rxMasterID=rxMaster.rxMasterID ORDER BY rxAddressID LIMIT 1) AS Address1 "+*/
												"FROM rxMaster " +
												"LEFT JOIN rxAddress on (rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.IsDefault=1) " +
												"WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT null AND rxMaster.name <> '(missing)' AND rxMaster.name <> ''" +
												"Group by rxMaster.rxMasterID ORDER BY rxMaster.name LIMIT " + theFrom + ", " + theTo;
		RolodexBean aRolodexBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new RolodexBean();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setRxMasterId((Integer) aObj[0]);      /** rxMaster.rxMaserId	*/  
				aRolodexBean.setName((String) aObj[1]);             /**	rxMaster.name		*/  
				aRolodexBean.setPhone1((String) aObj[2]);           /**	rxAddress.phone1	*/  
				aRolodexBean.setCity((String)aObj[3]);              /**	rxAddress.city		*/  
				aRolodexBean.setState((String) aObj[4]);            /**	rxAddress.state		*/  
				aRolodexBean.setAddress1((String) aObj[5]);         /**	rxAddress.Address1	*/  
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry = null;
		}
		return aQueryList;	
	}

	@Override
	public List<RolodexBean> getEmployees(int theFrom, int theTo,int theActive) {
		Session aSession = null;
		List<RolodexBean> aQueryList = new ArrayList<RolodexBean>();
		String aCustomerQry=" ";
		if(theActive == 1){
			aCustomerQry = "SELECT " +
								"rxMaster.rxMasterID," +
								"rxMaster.name, " +
								"rxMaster.phone1, " +
								"rxAddress.city, " +
								"rxAddress.state, " +
								"rxAddress.Address1, " +
								"rxMaster.firstName, " +
								"rxMaster.phone2, " +
								"rxMaster.fax, " +
								"rxAddress.Address2," +
								"rxAddress.zip, " +
								"rxMaster.phone3, " +
								"rxMaster.InActive " +
								"FROM rxMaster " +
								"LEFT JOIN rxAddress on (rxMaster.rxMasterID = rxAddress.rxMasterID  AND rxAddress.IsDefault=1) " +
								"WHERE rxMaster.isEMployee = 1 " +
								"ORDER BY rxMaster.name LIMIT " + theFrom + ", " + theTo;
		}else{
			aCustomerQry = "SELECT " +
								"rxMaster.rxMasterID," +
								"rxMaster.name, " +
								"rxMaster.phone1, " +
								"rxAddress.city, " +
								"rxAddress.state, " +
								"rxAddress.Address1, " +
								"rxMaster.firstName, " +
								"rxMaster.phone2, " +
								"rxMaster.fax, " +
								"rxAddress.Address2," +
								"rxAddress.zip, " +
								"rxMaster.phone3, " +
								"rxMaster.InActive " +
								"FROM rxMaster " +
								"LEFT JOIN rxAddress on (rxMaster.rxMasterID = rxAddress.rxMasterID  AND rxAddress.IsDefault=1) " +
								"WHERE rxMaster.isEMployee = 1 AND rxMaster.InActive = 0 " +
								"ORDER BY rxMaster.name LIMIT " + theFrom + ", " + theTo;
			
			//removed inactive AND rxMaster.InActive = 0 
		}
		RolodexBean aRolodexBean = null;
		try{
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new RolodexBean();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setRxMasterId((Integer) aObj[0]);		/**	rxMaster.rxMaserId	*/
				aRolodexBean.setName((String) aObj[1]);				/**	rxMaster.name		*/
				aRolodexBean.setPhone1((String) aObj[2]);			/**	rxAddress.phone1	*/
				aRolodexBean.setCity((String)aObj[3]);				/**	rxAddress.city		*/
				aRolodexBean.setState((String) aObj[4]);			/**	rxAddress.state		*/
				aRolodexBean.setAddress1((String) aObj[5]); 		/**	rxAddress.Address1	*/
				aRolodexBean.setFirstName((String) aObj[6]); 
				aRolodexBean.setPhone2((String) aObj[7]); 
				aRolodexBean.setFax((String) aObj[8]); 
				aRolodexBean.setAddress2((String) aObj[9]); 
				aRolodexBean.setZip((String) aObj[10]); 
				aRolodexBean.setPhone((String) aObj[11]);
				aRolodexBean.setInactive((Byte) aObj[12]);
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aCustomerQry = null;
			aSession.flush();
			aSession.close();
		}
		return aQueryList;	
	}

	@Override
	public int getRecordCount(String theWhereClause) {
		String aJobCountStr = "SELECT COUNT(rxMasterID) AS count FROM rxMaster " + theWhereClause;
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr=null;
		}
		return aTotalCount.intValue();
	}
	
	@Override
	public int getRecordCountEmployee(String theWhereClause,int theInActive) {
		String aJobCountStr=" ";
		if(theInActive==1){
			aJobCountStr = "SELECT COUNT(rxMasterID) AS count FROM rxMaster " + theWhereClause ;
		}else{
			aJobCountStr = "SELECT COUNT(rxMasterID) AS count FROM rxMaster " + theWhereClause +" AND InActive = 0";
		}
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public String getRxCategoty3(int theRxCategory3ID) {
		String aGeneralContractorQry = "SELECT Name FROM rxMaster WHERE rxMasterID = " + theRxCategory3ID + " AND isCategory3 = 1";
		Session aSession = null;
		String aGCName = "";
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aGeneralContractorQry);
			List<?> aList = aQuery.list();
			aGCName = (String) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aGeneralContractorQry=null;
		}
		return aGCName;
	}
	
	@Override
	public Rxaddress getAddress(int theRxMasterID) {
		String aRxAddressQry = "SELECT DISTINCT " +
													" rx.Name, " +
													" ra.Address1, " +
													" ra.Address2, " +
													" ra.City, " +
													" ra.State, " +
													" ra.Zip " +
													" FROM rxAddress ra " +
													" JOIN rxMaster rx ON rx.rxMasterID = ra.rxMasterID " +
													" WHERE ra.rxMasterID = " + theRxMasterID;
		Session aSession = null;
		Rxaddress aRolodexBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRxAddressQry);
			List<?> aRxAddressList = aQuery.list();
			if(aRxAddressList.size()>0)
			{	
			aRolodexBean = new Rxaddress();
			Object[] aObj = (Object[]) aRxAddressList.get(0);
			aRolodexBean.setName((String) aObj[0]);			/**	rxMaster.name		*/
			aRolodexBean.setAddress1((String)aObj[1]);
			aRolodexBean.setAddress2((String)aObj[2]);
			aRolodexBean.setCity((String)aObj[3]);			/**	rxAddress.city		*/
			aRolodexBean.setState((String) aObj[4]);		/**	rxAddress.state		*/
			aRolodexBean.setZip((String) aObj[5]);			/**	rxAddress.Address1	*/
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aRxAddressQry = null;
		}
		return aRolodexBean;
	}

	@Override
	public Rxmaster getPhone(int theRxMasterID) {
		itsLogger.debug("Retrieving all getPhone");
		String aRxmasterQry = "SELECT DISTINCT " +
													" rx.Name, " +
													" rx.Phone1, " +
													" rx.Phone2, " +
													" rx.fax " +
													" FROM rxMaster rx " +
													" JOIN rxAddress ra ON ra.rxMasterID = rx.rxMasterID " +
													" WHERE rx.rxMasterID = " + theRxMasterID;
		Session aSession = null;
		Rxmaster aRolodexBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRxmasterQry);
			List<?> aRxMasterList = aQuery.list();
			aRolodexBean = new Rxmaster();
			if(aRxMasterList.get(0) != null){
				Object[] aObj = (Object[]) aRxMasterList.get(0);
				aRolodexBean.setName((String) aObj[0]);			/**	rxMaster.name		*/
				aRolodexBean.setPhone1((String)aObj[1]);
				aRolodexBean.setPhone2((String)aObj[2]);
				aRolodexBean.setFax((String)aObj[3]);			/**	rxAddress.city		*/
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aRxmasterQry = null;
		}
		return aRolodexBean;
	}
	
	@Override
	public List<Rxcontact> getContacts(int theRxMasterId) {
		itsLogger.debug("Retrieving all Contacts");
		Session aSession = null;
		List<Rxcontact> aQueryList = null;
		try {

			itsLogger.info("getContacts()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM  Rxcontact WHERE rxMasterID = " + theRxMasterId + " ORDER BY FirstName");
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getContacts()=[Connection Closed]");
		}
		return aQueryList;
	}

	@Override
	public ArrayList<Rxcontact> getCustomerContactDetails(Rxcontact theRxcontact) {
		itsLogger.debug("Retrieving all getCustomerContactDetails");
		String orderTrackingQry = "SELECT LastName, FirstName, JobPosition, EMail, Cell, Division, rxContactID FROM rxContact r WHERE rxMasterID ='"+theRxcontact.getRxMasterId()+"'" ;
		Session aSession = null;
		ArrayList<Rxcontact> aCustomerContactQry = new ArrayList<Rxcontact>();
		try{
			Rxcontact aCutomerContacts = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(orderTrackingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCutomerContacts = new Rxcontact();
				Object[] aObj = (Object[])aIterator.next();
				aCutomerContacts.setLastName((String) aObj[0]);
				aCutomerContacts.setFirstName((String) aObj[1]);
				aCutomerContacts.setJobPosition((String) aObj[2]);
				aCutomerContacts.setEmail((String) aObj[3]);
				aCutomerContacts.setCell((String) aObj[4]);
				aCutomerContacts.setPager((String) aObj[5]);
				aCutomerContacts.setRxContactId((Integer) aObj[6]);
				aCustomerContactQry.add(aCutomerContacts);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			orderTrackingQry = null;
		}
		return aCustomerContactQry;
	}

	@Override
	public ArrayList<Rxaddress> getCustomerAddressDetails(Rxaddress theRxaddress) {
		itsLogger.debug("Retrieving all getCustomerAddressDetails");
		String aOrderTrackingQry = "SELECT ra.Address1,ra.Address2, ra.City, ra.State, ra.Zip, rxm.Phone1, rxm.Fax FROM rxAddress ra JOIN rxMaster rxm ON rxm.rxMasterID = ra.rxMasterID WHERE ra.rxMasterID ='"+theRxaddress.getRxMasterId()+"'";
		Session aSession = null;
		ArrayList<Rxaddress> aCustomerAddressQry = new ArrayList<Rxaddress>();
		try{
			Rxaddress aCutomerAddress = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aOrderTrackingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCutomerAddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				aCutomerAddress.setAddress1((String) aObj[0]);
				aCutomerAddress.setCity((String) aObj[1]);
				aCutomerAddress.setState((String) aObj[2]);
				aCutomerAddress.setZip((String) aObj[3]);
				aCutomerAddress.setAddress2((String) aObj[4]);
				aCutomerAddress.setName((String) aObj[5]);
				aCustomerAddressQry.add(aCutomerAddress);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aOrderTrackingQry = null;
		}
		return aCustomerAddressQry;
	}

	@Override
	public ArrayList<Rxcontact> getEmployeeContactDetails(Rxcontact theRxcontact){
		itsLogger.debug("Retrieving all getEmployeeContactDetails");
		String theOrderTrackingQry = "SELECT LastName, FirstName, JobPosition, EMail, Cell, Division FROM rxContact r WHERE rxMasterID ='"+theRxcontact.getRxMasterId()+"'" ;
		Session aSession = null;
		ArrayList<Rxcontact> aEmployeeContactQry = new ArrayList<Rxcontact>();
		try{
			Rxcontact aEmployeeContacts = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(theOrderTrackingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEmployeeContacts = new Rxcontact();
				Object[] aObj = (Object[])aIterator.next();
				aEmployeeContacts.setLastName((String) aObj[0]);
				aEmployeeContacts.setFirstName((String) aObj[1]);
				aEmployeeContacts.setJobPosition((String) aObj[2]);
				aEmployeeContacts.setEmail((String) aObj[3]);
				aEmployeeContacts.setCell((String) aObj[4]);
				aEmployeeContacts.setPager((String) aObj[5]);
				aEmployeeContactQry.add(aEmployeeContacts);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			theOrderTrackingQry = null;
		}
		return aEmployeeContactQry;
	}
	
	@Override
	public ArrayList<Rxaddress> getEmployeeAddressDetails(Rxaddress theRxaddress) {
		itsLogger.debug("Retrieving all getEmployeeAddressDetails");
		String aOrderTrackingQry = "SELECT ra.Address1, ra.City, ra.State, ra.Zip, rxm.Phone1, rxm.Fax FROM rxAddress ra JOIN rxMaster rxm ON rxm.rxMasterID = ra.rxMasterID WHERE ra.rxMasterID ='"+theRxaddress.getRxMasterId()+"'" ;
		Session aSession = null;
		ArrayList<Rxaddress> aEmployeeAddressQry = new ArrayList<Rxaddress>();
		try{
			Rxaddress aEmployeeAddress = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aOrderTrackingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEmployeeAddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				aEmployeeAddress.setAddress1((String) aObj[0]);
				aEmployeeAddress.setCity((String) aObj[1]);
				aEmployeeAddress.setState((String) aObj[2]);
				aEmployeeAddress.setZip((String) aObj[3]);
				aEmployeeAddress.setAddress2((String) aObj[4]);
				aEmployeeAddress.setName((String) aObj[5]);
				aEmployeeAddressQry.add(aEmployeeAddress);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aOrderTrackingQry = null;
		}
		return aEmployeeAddressQry;
	}
	
	@Override
	public ArrayList<RxJournal> getRxJournalDetails(RxJournal theRxJounal) {
		itsLogger.debug("Retrieving all getRxJournalDetails");
		String rxJounalQry = "SELECT j.EntryDate, " +
												" j.EntryMemo, " +
												" k.FullName, " +
												" j.rxJournalID " +
												" FROM rxJournal j INNER JOIN tsUserLogin k ON k.UserLoginID = j.UserLoginID " +
												" WHERE j.rxMasterID = '" + theRxJounal.getRxMasterId() + "'";
		Session aSession = null;
		ArrayList<RxJournal> aJournalQry = new ArrayList<RxJournal>();
		try{
			RxJournal aJournalVendor = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(rxJounalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJournalVendor = new RxJournal();
				Object[] aObj = (Object[])aIterator.next();
				//aJournalVendor.setEntryDate((Date)aObj[0]);
				if (aObj[0] != null) {
					aJournalVendor.setEntryDateString((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aJournalVendor.setEntryMemo((String) aObj[1]);
				aJournalVendor.setName((String) aObj[2]);
				aJournalVendor.setRxJournalId((Integer) aObj[3]);
				aJournalQry.add(aJournalVendor);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			rxJounalQry =null;
		}
		return aJournalQry;
	}

	@Override
	public ArrayList<JobsBean> getCustomerJobsDetails(String theRxJounal) {
		itsLogger.debug("Retrieving all getCustomerJobsDetails");
		/*String rxJounalQry = "SELECT DISTINCT j.JobNumber, j.Description, j.EstimatedCost, j.ContractAmount FROM joMaster j " +
												"INNER JOIN cuInvoice i ON j.rxCustomerID = i.rxCustomerID where j.rxCustomerID ='" + theRxJounal + "' AND j.JobStatus = 3";*/
		String rxJounalQry = "SELECT DISTINCT j.JobNumber, j.Description, j.EstimatedCost, j.ContractAmount FROM joMaster j " +
				" where j.rxCustomerID ='" + theRxJounal + "' AND j.JobStatus = 3";
		Session aSession = null;
		ArrayList<JobsBean> aJournalQry = new ArrayList<JobsBean>();
		try{
			JobsBean aCustomerJobs = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(rxJounalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCustomerJobs = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				aCustomerJobs.setJobNo((String)aObj[0]);
				aCustomerJobs.setJobName((String) aObj[1]);
				aCustomerJobs.setQuoteAmount((BigDecimal) aObj[2]);
				aCustomerJobs.setContractAmount((BigDecimal) aObj[3]);
				aJournalQry.add(aCustomerJobs);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			rxJounalQry =null;
		}
		return aJournalQry;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCustomerNameList(String theCustomerName) {
		itsLogger.debug("Retrieving all getCustomerNameList");
		String aCustomerNameTypeSelectQry = "SELECT rxMasterID, Name FROM rxMaster WHERE IsCustomer=1 AND Name like "+"'%"+JobUtil.removeSpecialcharacterswithslash(theCustomerName)+"%'"+" ORDER BY Name ASC";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			AutoCompleteBean aUserbean = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerNameTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				aUserbean.setValue((String)aObj[1]);				/** UserName	*/
				aUserbean.setLabel((String)aObj[1]);	
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerNameTypeSelectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public ArrayList<Rxcontact> getRolodexContactDetails(Rxcontact theRxcontact) {
		itsLogger.debug("Retrieving all getRolodexContactDetails");
		String aRolodexContactsQry = "FROM Rxcontact WHERE rxMasterID = '" + theRxcontact.getRxMasterId() + "'" ;
		Session aSession = null;
		ArrayList<Rxcontact> aRolodexContactList = new ArrayList<Rxcontact>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aRolodexContactsQry);
			aRolodexContactList = (ArrayList<Rxcontact>) aQuery.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aRolodexContactsQry =null;
		}
		return aRolodexContactList;
	}
	
	@Override
	public boolean addRolodexContact(Rxcontact theRolodexContact){
		itsLogger.debug("Retrieving all addRolodexContact");
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.save(theRolodexContact);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}
	
	@Override
	public boolean editRolodexContact(Rxcontact theRolodexContact) {
		itsLogger.debug("EditRolodexContact");
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.update(theRolodexContact);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public Rxmaster addNewCustomer(Rxmaster theRxMaster, Rxaddress theRxaddress,Integer userid) {
		itsLogger.debug("addNewCustomer");
		Session aSession = itsSessionFactory.openSession();
		//Session aRxAddressSession = itsSessionFactory.openSession();
		Transaction aRxMasterTransaction;
		//Session aemMasterSession = itsSessionFactory.openSession();
		Rxmaster aRxMaster = null;
		try {
			aRxMasterTransaction = aSession.beginTransaction();
			aRxMasterTransaction.begin();
			aSession.save(theRxMaster);
			theRxaddress.setRxMasterId(theRxMaster.getRxMasterId());
			aSession.save(theRxaddress);
			aRxMasterTransaction.commit();
			updateChildTableBaseonChkbx(theRxMaster.getRxMasterId(),userid);
			aRxMaster = (Rxmaster) aSession.get(Rxmaster.class, theRxMaster.getRxMasterId());
//			emmaster = new Emmaster();
//			emmaster.setEmMasterId(theRxMaster.getRxMasterId());
//			aSession.save(emmaster);
			/*aRxMaster = (Rxmaster) aSession.get(Rxmaster.class, theRxMaster.getRxMasterId());
			
			List<Cumaster> cuListQuery=aSession.createQuery("from Cumaster where cuMasterId="+theRxMaster.getRxMasterId()).list();
			
			listQuery = (String) aSession.createSQLQuery("Select IF(WarehouseID IS NULL or WarehouseID = '', '0', WarehouseID) as whseID  from SysUserDefault where UserLoginID = "+userid).uniqueResult();
			
			if(theRxMaster.isIsCustomer()){
			Cumaster cum=new Cumaster();
			cum.setCuMasterId(theRxMaster.getRxMasterId());
			cum.setPorequired(false);
			cum.setCreditHold(false);
			cum.setPrWarehouseId(JobUtil.ConvertintoInteger(listQuery));
			if(cuListQuery.isEmpty())
				addNewAssignee(cum);
			else
				updateAssignee(cum);
			}*/
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxMaster;
	}

	@Override
	public Rxmaster addNewCustomerAddress(Rxmaster theCustomer , Rxaddress theRxaddress) throws CustomerException{
		itsLogger.info("addNewCustomerAddress >>>>> theRxaddress.getRxAddressId() = "+theRxaddress.getRxAddressId());
		Session aRxMasterSession = itsSessionFactory.openSession();
		Transaction aRxMasterTransaction;
		Rxmaster aRxMaster = null;
		try {
			aRxMasterTransaction = aRxMasterSession.beginTransaction();
			aRxMasterTransaction.begin();
			
			Rxmaster anExistingRxMaster = (Rxmaster) aRxMasterSession.get(Rxmaster.class, theCustomer.getRxMasterId());
			itsLogger.info("theCustomer.getRxMasterId() = "+theCustomer.getRxMasterId());
			itsLogger.info(" theCustomer.getRxAddressId() = "+theCustomer.getPhone1()+" | "+theCustomer.getPhone2());
			if(theCustomer.getRxMasterId()!=null && theRxaddress.getIsDefault()){
				anExistingRxMaster.setInActive(theCustomer.getInActive());
				anExistingRxMaster.setPhone1(theCustomer.getPhone1());
				anExistingRxMaster.setPhone2(theCustomer.getPhone2());
				anExistingRxMaster.setPhone3(theCustomer.getPhone3());
				anExistingRxMaster.setFax(theCustomer.getFax());
				
				aRxMasterSession.update(anExistingRxMaster);
			
			}
//			updateCustomerInActive(theCustomer.getInActive(),theCustomer.getRxMasterId());
			theRxaddress.setRxMasterId(theCustomer.getRxMasterId());
		
			
			if(theRxaddress.getRxAddressId() != null)
			{
				Rxaddress anExistingRxaddress= (Rxaddress) aRxMasterSession.get(Rxaddress.class, theRxaddress.getRxAddressId());
				anExistingRxaddress.setInActive(theCustomer.getInActive());
				anExistingRxaddress.setPhone1(theCustomer.getPhone1());
				anExistingRxaddress.setPhone2(theCustomer.getPhone2());
				anExistingRxaddress.setFax(theCustomer.getFax());
				itsLogger.info(" anExistingRxaddress.getRxAddressId() = "+anExistingRxaddress.getRxAddressId());
				if(anExistingRxaddress.getRxAddressId() != null){
					itsLogger.info(" anExistingRxaddress.getRxAddressId() = "+anExistingRxaddress.getPhone1()+" | "+anExistingRxaddress.getPhone2());
					theRxaddress.setRxAddressId(anExistingRxaddress.getRxAddressId());
					aRxMasterSession.update(anExistingRxaddress);
				}
			}
			else{
				itsLogger.info(" theCustomer.getRxAddressId() = "+theCustomer.getPhone1()+" | "+theCustomer.getPhone2());
				aRxMasterSession.save(theRxaddress);
			}
			aRxMasterTransaction.commit();
			aRxMaster = (Rxmaster) aRxMasterSession.get(Rxmaster.class, theCustomer.getRxMasterId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new CustomerException(e.getMessage(), e);
		} finally {
			aRxMasterSession.flush();
			aRxMasterSession.close();
		
		}
		return aRxMaster;
	}

	public Rxmaster addNewVendorAddress(Rxaddress theRxaddress){
		itsLogger.debug("addNewVendorAddress");
		Session aRxAddressSession = itsSessionFactory.openSession();
		Transaction aRxAddressTransaction;
		Rxmaster aRxMaster = null;
		try {
			aRxAddressTransaction = aRxAddressSession.beginTransaction();
			aRxAddressTransaction.begin();
			aRxAddressSession.save(theRxaddress);
			aRxAddressTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxAddressSession.flush();
			aRxAddressSession.close();
		}
		return aRxMaster;
	}
	
	public Rxmaster deleteVendorAddress(Integer rxAddressId){
		itsLogger.debug(">>>>>>>>>>>>>> delete VendorAddress");
		Session aRxAddressSession = itsSessionFactory.openSession();
		Transaction aRxAddressTransaction;
		Rxmaster aRxMaster = null;
		try {
			aRxAddressTransaction = aRxAddressSession.beginTransaction();
			aRxAddressTransaction.begin();
			if (rxAddressId != null) {
				Rxaddress aRxaddress = (Rxaddress) aRxAddressSession.get(
						Rxaddress.class, rxAddressId);
				if (aRxaddress != null) {
					aRxAddressSession.delete(aRxaddress);
				}
			}
			
			aRxAddressTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxAddressSession.flush();
			aRxAddressSession.close();
		}
		return aRxMaster;
	}
	
	@Override
	public int getRxMasterId(String theName) {
		itsLogger.debug("getRxMasterId");
		Integer aRxMasterId = null;
		String aSelectQry = "SELECT rxMasterId FROM rxMaster where Name like '"+"%"+theName+"%"+"'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aRxMasterId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
			aSelectQry = null;
		}
		return aRxMasterId;
	}

	@Override
	public int updateCustomerName(String theCustomerName, int theCustomerId) {
		itsLogger.debug("updateCustomerName");
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Rxmaster aRxmaster = (Rxmaster) aSession.get(Rxmaster.class, theCustomerId);
			aRxmaster.setName(theCustomerName);
			aRxmaster.setRxMasterId(theCustomerId);
			aSession.update(aRxmaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return 0;
	}
	@Override
	public int updateCustomerInActive(Boolean theCustomerStatus, int theCustomerId) {
		itsLogger.debug("updateCustomerName");
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Rxmaster aRxmaster = (Rxmaster) aSession.get(Rxmaster.class, theCustomerId);
			aRxmaster.setInActive(theCustomerStatus);
			aRxmaster.setRxMasterId(theCustomerId);
			aSession.update(aRxmaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return 0;
	}

	@Override
	public boolean addRolodexJournal(RxJournal theRxjournal) {
		itsLogger.debug("addRolodexJournal");
		Session aRxJournalSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aRxJournalSession.beginTransaction();
			aTransaction.begin();
			aRxJournalSession.save(theRxjournal);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aRxJournalSession.flush();
			aRxJournalSession.close();
		}
		return false;
	}

	@Override
	public boolean updateRolodexJournal(RxJournal theRxjournal) {
		itsLogger.debug("updateRolodexJournal");
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.update(theRxjournal);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}
	
	@Override
	public boolean deleteRolodexJournal(RxJournal theRxjournal) {
		itsLogger.debug("deleteRolodexJournal");
		Session aRxJournalSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aRxJournalSession.beginTransaction();
			aTransaction.begin();
			aRxJournalSession.delete(theRxjournal);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aRxJournalSession.flush();
			aRxJournalSession.close();
		}
		return false;
	}

	@Override
	public ArrayList<JobsBean> getContactIdbasedOpenQuotes(String theRolodexId, int theJobStatus, Integer theContactId) {
		itsLogger.debug("getContactIdbasedOpenQuotes");
		String rxJounalQry = "SELECT DISTINCT j.BidDate, j.JobNumber, j.Description, jb.rxContactID, CONCAT(rxc.FirstName,' ', rxc.LastName) AS Contact, j.EstimatedCost, j.ContractAmount,  jb.QuoteRev, jb.joBidderId, j.joMasterId, jb.rxMasterID, j.locationName,j.JobStatus FROM joMaster j " +
								"RIGHT JOIN joBidder jb ON jb.JoMasterID = j.JoMasterID " +
								"LEFT JOIN cuInvoice ci ON j.rxCustomerID = ci.rxCustomerID " +
								"RIGHT JOIN rxContact rxc ON rxc.rxContactID = jb.rxContactID " +
								"WHERE EXISTS " +
								"(SELECT jb.joMasterId FROM joBidder jb LEFT JOIN cuInvoice ci  ON jb.rxMasterID = ci.rxCustomerID LEFT JOIN rxContact rxc ON rxc.rxContactID = jb.rxContactID " +
								"WHERE jb.rxMasterID = "+theRolodexId+"  AND rxc.rxContactID = "+theContactId+"  AND ci.TransactionStatus = 1 ) " +
								"AND jb.rxMasterID = "+theRolodexId+" AND QuoteDate IS NOT NULL  AND rxc.rxContactID = "+theContactId+" AND j.JobStatus = 1";
		Session aSession = null;
		ArrayList<JobsBean> aOpenQuotes = new ArrayList<JobsBean>();
		try{
			JobsBean aCustomerOpenQuotes = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(rxJounalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCustomerOpenQuotes = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				if((Date)aObj[0] != null){
					aCustomerOpenQuotes.setQuoteDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aCustomerOpenQuotes.setJobNo((String)aObj[1]);
				aCustomerOpenQuotes.setJobName((String) aObj[2]);
				aCustomerOpenQuotes.setContactID((Integer) aObj[3]);
				aCustomerOpenQuotes.setCustomerContact((String)aObj[4]);
				aCustomerOpenQuotes.setQuoteAmount((BigDecimal) aObj[5]);
				aCustomerOpenQuotes.setContractAmount((BigDecimal) aObj[6]);
				aCustomerOpenQuotes.setQuoteRev((String) aObj[7]);
				aCustomerOpenQuotes.setJoBidderId((Integer) aObj[8]);
				aCustomerOpenQuotes.setJoMasterId((Integer) aObj[9]);
				aCustomerOpenQuotes.setRxMasterID((Integer) aObj[10]);
				aCustomerOpenQuotes.setDescription((String) aObj[11]);
				aCustomerOpenQuotes.setJobStatus(Integer.valueOf(aObj[12].toString()));
				aOpenQuotes.add(aCustomerOpenQuotes);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			rxJounalQry =null;
		}
		return aOpenQuotes;
	}
// AND ci.TransactionStatus = 2
	@Override
	public ArrayList<JobsBean> getCustomerLostQuotes(String theRolodexId, int theJobStatus, Integer theContactId) {
		itsLogger.debug("getCustomerLostQuotes");
		String rxJounalQry = "SELECT DISTINCT j.BidDate, j.JobNumber, j.Description, jb.rxContactID, CONCAT(rxc.FirstName,' ', rxc.LastName) AS Contact, j.EstimatedCost, j.ContractAmount,  jb.QuoteRev, jb.joBidderId, j.joMasterId, jb.rxMasterID, j.locationName,j.JobStatus FROM joMaster j " +
												"RIGHT JOIN joBidder jb ON jb.JoMasterID = j.JoMasterID " +
												"LEFT JOIN cuInvoice ci ON j.rxCustomerID = ci.rxCustomerID " +
												"RIGHT JOIN rxContact rxc ON rxc.rxContactID = jb.rxContactID " +
												"WHERE EXISTS" +
												" (SELECT jb.joMasterId FROM joBidder jb LEFT JOIN cuInvoice ci  ON jb.rxMasterID = ci.rxCustomerID LEFT JOIN rxContact rxc ON rxc.rxContactID = jb.rxContactID WHERE jb.rxMasterID = "+theRolodexId+"  AND rxc.rxContactID = "+theContactId+"  ) " +
												"AND jb.rxMasterID = "+theRolodexId+" AND QuoteDate IS NOT NULL  AND rxc.rxContactID = "+theContactId+" AND j.JobStatus = 2";
		Session aSession = null;
		ArrayList<JobsBean> aLostQuotes = new ArrayList<JobsBean>();
		try{
			JobsBean aCustomerLostQuotes = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(rxJounalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCustomerLostQuotes = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				if((Date)aObj[0] != null){
					aCustomerLostQuotes.setQuoteDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aCustomerLostQuotes.setJobNo((String)aObj[1]);
				aCustomerLostQuotes.setJobName((String) aObj[2]);
				aCustomerLostQuotes.setContactID((Integer) aObj[3]);
				aCustomerLostQuotes.setCustomerContact((String)aObj[4]);
				aCustomerLostQuotes.setQuoteAmount((BigDecimal) aObj[5]);
				aCustomerLostQuotes.setContractAmount((BigDecimal) aObj[6]);
				aCustomerLostQuotes.setQuoteRev((String) aObj[7]);
				aCustomerLostQuotes.setJoBidderId((Integer) aObj[8]);
				aCustomerLostQuotes.setJoMasterId((Integer) aObj[9]);
				aCustomerLostQuotes.setRxMasterID((Integer) aObj[10]);
				aCustomerLostQuotes.setDescription((String) aObj[11]);
				aCustomerLostQuotes.setJobStatus(Integer.valueOf(aObj[12].toString()));
				aLostQuotes.add(aCustomerLostQuotes);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			rxJounalQry=null;
		}
		return aLostQuotes;
	}
	
	@Override
	public ArrayList<JobsBean> getCustomerQuotes(String theRolodexId, int theJobStatus, Integer theContactId) {
		itsLogger.debug("getCustomerQuotes");
		String rxJounalQry = "SELECT DISTINCT j.BidDate, j.JobNumber, j.Description, jb.rxContactID, CONCAT(rxc.FirstName,' ', rxc.LastName) AS Contact, j.EstimatedCost, j.ContractAmount,  jb.QuoteRev, jb.joBidderId, j.joMasterId, jb.rxMasterID, j.locationName,j.JobStatus FROM joMaster j " +
												"RIGHT JOIN joBidder jb ON jb.JoMasterID = j.JoMasterID " +
												"LEFT JOIN cuInvoice ci ON j.rxCustomerID = ci.rxCustomerID " +
												"RIGHT JOIN rxContact rxc ON rxc.rxContactID = jb.rxContactID " +
												"WHERE EXISTS" +
												" (SELECT jb.joMasterId FROM joBidder jb LEFT JOIN cuInvoice ci  ON jb.rxMasterID = ci.rxCustomerID LEFT JOIN rxContact rxc ON rxc.rxContactID = jb.rxContactID WHERE jb.rxMasterID = "+theRolodexId+"  AND rxc.rxContactID = "+theContactId+"  AND ci.TransactionStatus = 1 ) " +
												"AND jb.rxMasterID = "+theRolodexId+" AND QuoteDate IS NOT NULL  AND rxc.rxContactID = "+theContactId+" AND j.JobStatus = 1";
		Session aSession = null;
		ArrayList<JobsBean> aQuotes = new ArrayList<JobsBean>();
		try{
			JobsBean aCustomerQuotes = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(rxJounalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCustomerQuotes = new JobsBean();
				Object[] aObj = (Object[])aIterator.next();
				if((Date)aObj[0] != null){
					aCustomerQuotes.setQuoteDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aCustomerQuotes.setJobNo((String)aObj[1]);
				aCustomerQuotes.setJobName((String) aObj[2]);
				aCustomerQuotes.setContactID((Integer) aObj[3]);
				aCustomerQuotes.setCustomerContact((String)aObj[4]);
				aCustomerQuotes.setQuoteAmount((BigDecimal) aObj[5]);
				aCustomerQuotes.setContractAmount((BigDecimal) aObj[6]);
				aCustomerQuotes.setQuoteRev((String) aObj[7]);
				aCustomerQuotes.setJoBidderId((Integer) aObj[8]);
				aCustomerQuotes.setJoMasterId((Integer) aObj[9]);
				aCustomerQuotes.setRxMasterID((Integer) aObj[10]);
				aCustomerQuotes.setDescription((String) aObj[11]);
				aCustomerQuotes.setJobStatus(Integer.valueOf(aObj[12].toString()));
				aQuotes.add(aCustomerQuotes);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			rxJounalQry =null;
		}
		return aQuotes;
	}
		
	public boolean updateCuMasterDetailsRecord(Cumaster theCuMaster) {
		itsLogger.debug("updateCuMasterDetailsRecord");
		Session aSession = null;
		Cumaster aExistingCuMaster = new Cumaster();
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (theCuMaster.getCuMasterId() > 1) {
			aExistingCuMaster = (Cumaster) aSession.get(Cumaster.class, theCuMaster.getCuMasterId());
			aExistingCuMaster.setCuAssignmentId0(theCuMaster.getCuAssignmentId0());
			aExistingCuMaster.setCuAssignmentId1(theCuMaster.getCuAssignmentId1());
			aExistingCuMaster.setCuAssignmentId2(theCuMaster.getCuAssignmentId2());
			aExistingCuMaster.setCuAssignmentId3(theCuMaster.getCuAssignmentId3());
			aExistingCuMaster.setCuAssignmentId4(theCuMaster.getCuAssignmentId4());
			aExistingCuMaster.setAccountNumber(theCuMaster.getAccountNumber());
			aExistingCuMaster.setFinCharge(theCuMaster.getFinCharge());
			aExistingCuMaster.setStatements(theCuMaster.getStatements());
			aExistingCuMaster.setCuTermsId(theCuMaster.getCuTermsId());
			aExistingCuMaster.setCuMasterTypeId(theCuMaster.getCuMasterTypeId());
			aExistingCuMaster.setPrWarehouseId(theCuMaster.getPrWarehouseId());
			aExistingCuMaster.setTaxExemptNumber(theCuMaster.getTaxExemptNumber());
			aExistingCuMaster.setPorequired(theCuMaster.getPorequired());
			aExistingCuMaster.setCreditLimit(theCuMaster.getCreditLimit());
			aExistingCuMaster.setCreditAppDate(theCuMaster.getCreditAppDate());
			aExistingCuMaster.setCreditHold(theCuMaster.getCreditHold());
			aExistingCuMaster.setCreditHoldOverride(theCuMaster.getCreditHoldOverride());
			aExistingCuMaster.setQuoteMethod(theCuMaster.getQuoteMethod());
			aExistingCuMaster.setQmText(theCuMaster.getQmText());
			aExistingCuMaster.setInvoiceMethod(theCuMaster.getInvoiceMethod());
			aExistingCuMaster.setImText(theCuMaster.getImText());
			aExistingCuMaster.setStatementMethod(theCuMaster.getStatementMethod());
			aExistingCuMaster.setSmText(theCuMaster.getSmText());
			aSession.update(aExistingCuMaster);
			}
			else {
				aExistingCuMaster.setCuAssignmentId0(theCuMaster.getCuAssignmentId0());
				aExistingCuMaster.setCuAssignmentId1(theCuMaster.getCuAssignmentId1());
				aExistingCuMaster.setCuAssignmentId2(theCuMaster.getCuAssignmentId2());
				aExistingCuMaster.setCuAssignmentId3(theCuMaster.getCuAssignmentId3());
				aExistingCuMaster.setCuAssignmentId4(theCuMaster.getCuAssignmentId4());
				aExistingCuMaster.setAccountNumber(theCuMaster.getAccountNumber());
				aExistingCuMaster.setFinCharge(theCuMaster.getFinCharge());
				aExistingCuMaster.setStatements(theCuMaster.getStatements());
				aExistingCuMaster.setCuTermsId(theCuMaster.getCuTermsId());
				aExistingCuMaster.setCuMasterTypeId(theCuMaster.getCuMasterTypeId());
				aExistingCuMaster.setPrWarehouseId(theCuMaster.getPrWarehouseId());
				aExistingCuMaster.setTaxExemptNumber(theCuMaster.getTaxExemptNumber());
				aExistingCuMaster.setPorequired(theCuMaster.getPorequired());
				aExistingCuMaster.setCreditLimit(theCuMaster.getCreditLimit());
				aExistingCuMaster.setCreditAppDate(theCuMaster.getCreditAppDate());
				aExistingCuMaster.setCreditHold(theCuMaster.getCreditHold());
				aExistingCuMaster.setCreditHoldOverride(theCuMaster.getCreditHoldOverride());
				aExistingCuMaster.setQuoteMethod(theCuMaster.getQuoteMethod());
				aExistingCuMaster.setQmText(theCuMaster.getQmText());
				aExistingCuMaster.setInvoiceMethod(theCuMaster.getInvoiceMethod());
				aExistingCuMaster.setImText(theCuMaster.getImText());
				aExistingCuMaster.setStatementMethod(theCuMaster.getStatementMethod());
				aExistingCuMaster.setSmText(theCuMaster.getSmText());
				aSession.save(theCuMaster);
			}
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		
		return false;
	}

	@Override
	public Integer addQuickQuote(Jomaster theJomaster){
		itsLogger.debug("addQuickQuote");
		Session aJoMasterSession = null;
		Transaction aTransaction;
		int aAutoId = 0;
		try {
			aJoMasterSession = itsSessionFactory.openSession();
			aTransaction = aJoMasterSession.beginTransaction();
			aTransaction.begin();
			aAutoId = (Integer) aJoMasterSession.save(theJomaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aJoMasterSession.flush();
			aJoMasterSession.close();
		}
		return aAutoId;
	}

	@Override
	public boolean addQuickQuoteBidder(Jobidder theJobidder) {
		itsLogger.debug("addQuickQuoteBidder");
		Session aJoBidderSession = null;
		Transaction aTransaction;
		try {
			aJoBidderSession = itsSessionFactory.openSession();
			aTransaction = aJoBidderSession.beginTransaction();
			aTransaction.begin();
			aJoBidderSession.save(theJobidder);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aJoBidderSession.flush();
			aJoBidderSession.close();
		}
		return false;
	}

	@Override
	public boolean addQuickQuoteCuMaster(Cuinvoice theCuinvoice) {
		itsLogger.debug("addQuickQuoteCuMaster");
		Session aCuinvoiceSession = null;
		Transaction aTransaction;
		try {
			aCuinvoiceSession = itsSessionFactory.openSession();
			aTransaction = aCuinvoiceSession.beginTransaction();
			aTransaction.begin();
			aCuinvoiceSession.save(theCuinvoice);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aCuinvoiceSession.flush();
			aCuinvoiceSession.close();
		}
		return false;
	}
	
	@Override
	public boolean editQuickQuote(Jomaster theJomaster){
		itsLogger.debug("editQuickQuote");
		Session aJoMasterSession = null;
		Transaction aTransaction;
		try {
			aJoMasterSession = itsSessionFactory.openSession();
			aTransaction = aJoMasterSession.beginTransaction();
			aTransaction.begin();
			Jomaster aExistingJomaster= (Jomaster)aJoMasterSession.get(Jomaster.class, theJomaster.getJoMasterId());
			aExistingJomaster.setRxCustomerId(theJomaster.getRxCustomerId());
			aExistingJomaster.setDescription(theJomaster.getDescription());
			aExistingJomaster.setEstimatedCost(theJomaster.getEstimatedCost());
			aExistingJomaster.setContractAmount(theJomaster.getContractAmount());
			aExistingJomaster.setJobStatus(theJomaster.getJobStatus());
			aExistingJomaster.setCreatedById(theJomaster.getCreatedById());
			aExistingJomaster.setBidDate(theJomaster.getBidDate());
			aExistingJomaster.setJobNumber(theJomaster.getJobNumber());
			aExistingJomaster.setJoMasterId(theJomaster.getJoMasterId());
			aJoMasterSession.update(aExistingJomaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aJoMasterSession.flush();
			aJoMasterSession.close();
		}
		return false;
	}

	@Override
	public boolean editQuickQuoteBidder(Jobidder theJobidder) {
		itsLogger.debug("editQuickQuoteBidder");
		Session aJoBidderSession = null;
		Transaction aTransaction;
		try {
			aJoBidderSession = itsSessionFactory.openSession();
			aTransaction = aJoBidderSession.beginTransaction();
			aTransaction.begin();
			Jobidder aExistingJobidder= (Jobidder)aJoBidderSession.get(Jobidder.class, theJobidder.getJoBidderId());
			aExistingJobidder.setRxMasterId(theJobidder.getRxMasterId());
			aExistingJobidder.setRxContactId(theJobidder.getRxContactId());
			aExistingJobidder.setQuoteStatus(theJobidder.getQuoteStatus());
			aExistingJobidder.setUserLoginID(theJobidder.getUserLoginID());
			aExistingJobidder.setQuoteRev(theJobidder.getQuoteRev());
			aExistingJobidder.setCuMasterTypeId(theJobidder.getCuMasterTypeId());
			aExistingJobidder.setJoMasterId(theJobidder.getJoMasterId());
			aExistingJobidder.setJoBidderId(theJobidder.getJoBidderId());
			aJoBidderSession.update(aExistingJobidder);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aJoBidderSession.flush();
			aJoBidderSession.close();
		}
		return false;
	}

	@Override
	public boolean editQuickQuoteCuMaster(Cuinvoice theCuinvoice) {
		itsLogger.debug("editQuickQuoteCuMaster");
		Session aCuinvoiceSession = null;
		Transaction aTransaction;
		try {
			aCuinvoiceSession = itsSessionFactory.openSession();
			aTransaction = aCuinvoiceSession.beginTransaction();
			aTransaction.begin();
			Cuinvoice aExistingCuinvoice= (Cuinvoice) aCuinvoiceSession.get(Cuinvoice.class, theCuinvoice.getCuInvoiceId());
			aExistingCuinvoice.setRxCustomerId(theCuinvoice.getRxCustomerId());
			aExistingCuinvoice.setTransactionStatus(theCuinvoice.getTransactionStatus());
			aExistingCuinvoice.setCreatedById(theCuinvoice.getCreatedById());
			aExistingCuinvoice.setCreatedOn(theCuinvoice.getCreatedOn());
			aExistingCuinvoice.setInvoiceDate(theCuinvoice.getInvoiceDate());
			aExistingCuinvoice.setApplied(theCuinvoice.getApplied());
			aExistingCuinvoice.setAppliedAmount(theCuinvoice.getAppliedAmount());
			aExistingCuinvoice.setInvoiceAmount(theCuinvoice.getInvoiceAmount());
			aExistingCuinvoice.setTaxAmount(theCuinvoice.getTaxAmount());
			aExistingCuinvoice.setCuInvoiceId(theCuinvoice.getCuInvoiceId());
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aCuinvoiceSession.flush();
			aCuinvoiceSession.close();
		}
		return false;
	}
	
	@Override
	public ArrayList<AutoCompleteBean> getCustomerFirstContactList(String theFilterName) {
		itsLogger.debug("getCustomerFirstContactList");
		String aJobSelectQry = "SELECT rxContactID, rxMasterID, FirstName FROM rxContact " +
													"WHERE FirstName like "+"'%"+theFilterName+"%'"+"";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			AutoCompleteBean aUserbean = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);
				aUserbean.setManufactureID((Integer)aObj[1]);
				aUserbean.setLabel((String)aObj[2]);
				aUserbean.setValue((String)aObj[2]);
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public ArrayList<AutoCompleteBean> getCustomerLastContactList(String theFilterName) {
		itsLogger.debug("getCustomerLastContactList");
		String aJobSelectQry = "SELECT rxContactID, rxMasterID, LastName FROM rxContact " +
													"WHERE LastName like "+"'%"+theFilterName+"%'"+"";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			AutoCompleteBean aUserbean = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);
				aUserbean.setManufactureID((Integer)aObj[1]);
				aUserbean.setLabel((String)aObj[2]);
				aUserbean.setValue((String)aObj[2]);
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry =null;
		}
		return aQueryList;
	}

	@Override
	public Rxmaster addNewRolodex(Rxmaster theRolodex, Rxaddress theRxaddress,Integer userid) {
		itsLogger.debug("addNewRolodex");
		Session aRxMasterSession = itsSessionFactory.openSession();
		Transaction aRxMasterTransaction;
		//SysUserDefault aSysUserDefault;
		String listQuery=null;
		Rxmaster aRxMaster = null;
		try {
			aRxMasterTransaction = aRxMasterSession.beginTransaction();
			aRxMasterTransaction.begin();
			aRxMasterSession.save(theRolodex);
			/*Vemaster aVemaster = (Vemaster) aRxMasterSession.get(Vemaster.class,theRolodex.getRxMasterId());
			if(aVemaster==null){
				aVemaster=new Vemaster();
				aVemaster.setVeMasterId(theRolodex.getRxMasterId());
				aRxMasterSession.save(aVemaster);
			}
			Cumaster acuMaster = (Cumaster) aRxMasterSession.get(Cumaster.class,theRolodex.getRxMasterId());
			if(acuMaster==null){
				acuMaster=new Cumaster();
				acuMaster.setCuMasterId(theRolodex.getRxMasterId());
				acuMaster.setCreditHold(false);
				acuMaster.setPorequired(false);
				acuMaster.setFinCharge((byte) 0);
				acuMaster.setStatements((byte) 0);
				acuMaster.setUseAvgDaysAsOf((byte) 0);
				aRxMasterSession.save(acuMaster);
			}*/
			
			theRxaddress.setRxMasterId(theRolodex.getRxMasterId());
			aRxMasterSession.save(theRxaddress);
			aRxMasterTransaction.commit();
			aRxMaster = (Rxmaster) aRxMasterSession.get(Rxmaster.class, theRolodex.getRxMasterId());
			
			//listQuery = (String) aRxMasterSession.createSQLQuery("Select IF(WarehouseID IS NULL or WarehouseID = '', '0', WarehouseID) as whseID  from SysUserDefault where UserLoginID = "+userid).uniqueResult();
			updateChildTableBaseonChkbx(aRxMaster.getRxMasterId(),userid);
			/*if(theRolodex.isIsCustomer()){
				Cumaster cum=new Cumaster();
				cum.setCuMasterId(theRolodex.getRxMasterId());
				cum.setPorequired(false);
				cum.setCreditHold(false);
				cum.setPrWarehouseId(Integer.parseInt(listQuery));
				addNewAssignee(cum);
			}*/
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxMasterSession.flush();
			aRxMasterSession.close();
			listQuery=null;
		}
		return aRxMaster;
	}

	@Override
	public Integer getRxAddressID(Integer theRxmasterId, String theAddress1){
		itsLogger.debug("getRxAddressID");
		String aRxAddressQry = "SELECT rxAddressId FROM rxAddress "+" WHERE rxMasterId="+theRxmasterId;
		if(theAddress1!=null && theAddress1!= ""){
			if(theAddress1.contains("'"))
				theAddress1 = theAddress1.replaceAll("'", "\\\\'");
		aRxAddressQry = aRxAddressQry +" AND address1 LIKE "+"'%"+theAddress1+"%'";
		}
		Session aSession=null;
		Integer aRxaddress = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRxAddressQry);
			if(!aQuery.list().isEmpty()){
				List<?> aList = aQuery.list();
				aRxaddress = (Integer) aList.get(0);	
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally{
			aSession.flush();
			aSession.close();
			aRxAddressQry =null;
		}
		return aRxaddress;
	}

	public Cumaster addNewAssignee(Cumaster theCuMaster){
		itsLogger.debug("addNewAssignee");
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			aCuMasterTransaction.begin();
			aRxCumasterSession.save(theCuMaster);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return theCuMaster;
	}

	public Cumaster updateAssignee(Cumaster theCuMaster){
		itsLogger.debug("updateAssignee");
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			aCuMasterTransaction.begin();
			aRxCumasterSession.update(theCuMaster);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return theCuMaster;
	}

	@Override
	public String getQuoteRev(Integer theRxMasterID, Integer theRxContactID) {
		itsLogger.debug("getQuoteRev");
		String aRxAddressQry = "Select QuoteRev from joBidder where rxMasterID = "+theRxMasterID+" and rxContactId = "+theRxContactID+" And QuoteRev IS NOT NULL ORDER BY QuoteRev DESC";
		Session aSession=null;
		String aQuoteRev = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRxAddressQry);
			List<?> aList = aQuery.list();
			if(!aList.isEmpty()){
				aQuoteRev = (String) aList.get(0);	
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally{
			aSession.flush();
			aSession.close();
			aRxAddressQry = null;
		}
		return aQuoteRev;
	}

	@Override
	public boolean updateRolodexDetails(Rxmaster theRxmaster,Integer userid){
		itsLogger.debug("updateRolodexDetails");
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String listQuery=null;
		try {
			aTransaction = aSession.beginTransaction();
			Rxmaster aExistingrxMaster= (Rxmaster)aSession.get(Rxmaster.class, theRxmaster.getRxMasterId());
			itsLogger.info("aExistingrxMaster"+aExistingrxMaster);
			aExistingrxMaster.setIsCustomer(theRxmaster.isIsCustomer());
			aExistingrxMaster.setIsVendor(theRxmaster.isIsVendor());
			aExistingrxMaster.setIsEmployee(theRxmaster.isIsEmployee());
			aExistingrxMaster.setIsCategory1(theRxmaster.isIsCategory1());
			aExistingrxMaster.setIsCategory2(theRxmaster.isIsCategory2());
			aExistingrxMaster.setIsCategory3(theRxmaster.isIsCategory3());
			aExistingrxMaster.setIsCategory4(theRxmaster.isIsCategory4());
			aExistingrxMaster.setIsCategory5(theRxmaster.isIsCategory5());
			aExistingrxMaster.setIsCategory6(theRxmaster.isIsCategory6());
			aExistingrxMaster.setIsCategory7(theRxmaster.isIsCategory7());
			aExistingrxMaster.setIsCategory8(theRxmaster.isIsCategory8());
			aSession.update(aExistingrxMaster);
			aTransaction.commit();
			updateChildTableBaseonChkbx(theRxmaster.getRxMasterId(),userid);
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
			listQuery=null;
		}
		return false;
	}
	
	
	
	@Override
	public boolean updateRxMasterDetails(Rxmaster theRxmaster){
		itsLogger.debug("updateRolodexDetails");
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			Rxmaster aExistingrxMaster= (Rxmaster)aSession.get(Rxmaster.class, theRxmaster.getRxMasterId());
			itsLogger.info("aExistingrxMaster"+aExistingrxMaster);
			aExistingrxMaster.setPhone1(theRxmaster.getPhone1());
			aExistingrxMaster.setPhone2(theRxmaster.getPhone2());
			aExistingrxMaster.setFax(theRxmaster.getFax());
			aExistingrxMaster.setPhone3(theRxmaster.getPhone3());
			aSession.update(aExistingrxMaster);
			aTransaction.commit();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}
	
	@Override
	public void getBidderIDList(Rxcontact theRolodexContact){
		itsLogger.debug("updateRolodexDetails");
	Integer aRxContactId= theRolodexContact.getRxContactId(); 
	String aRxAddressQry = "Select joBidderId,rxContactId,joMasterId from joBidder where rxContactId = "+aRxContactId+"";
	Session aSession=null;
	ArrayList<Jobidder> aQueryList = new ArrayList<Jobidder>();
	try{
		Jobidder aUserbean = null;
		aSession=itsSessionFactory.openSession();
		Query aQuery = aSession.createSQLQuery(aRxAddressQry);
		Iterator<?> aIterator = aQuery.list().iterator();
		while(aIterator.hasNext()) {
			aUserbean = new  Jobidder();
			Object[] aObj = (Object[])aIterator.next();
			aUserbean.setJoBidderId((Integer) aObj[0]);
			aUserbean.setRxContactId((Integer) aObj[1]);
			aUserbean.setJoMasterId((Integer) aObj[2]);
			aQueryList.add(aUserbean);
		}
		deletejoBidderDetails(aQueryList);
		deleteRolodexContact(theRolodexContact);
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aRxAddressQry =null;
				
		}
	}
	
	public void deleteRolodexContact(Rxcontact theRolodexContact) {
		itsLogger.debug("deleteRolodexContact");
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			aSession.delete(theRolodexContact);
			transaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	public void deletejoBidderDetails(ArrayList<Jobidder> theJobidders) {
		itsLogger.debug("deletejoBidderDetails");
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Jobidder aJobidder=null;
			for(int index=0; index < theJobidders.size(); index++){
				Transaction transaction = aSession.beginTransaction();
				transaction.begin();
				aJobidder=new Jobidder();
				aJobidder.setJoBidderId(theJobidders.get(index).getJoBidderId());
				aJobidder.setRxContactId(theJobidders.get(index).getRxContactId());
				aJobidder.setJoMasterId(theJobidders.get(index).getJoMasterId());
				aSession.delete(aJobidder);
				transaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public boolean updateQuickQuoteCuMaster(Cuinvoice theCuinvoice) {
		itsLogger.debug("updateQuickQuoteCuMaster");
		Session aCuinvoiceSession = itsSessionFactory.openSession();
		Transaction transaction;
		try {
			transaction = aCuinvoiceSession.beginTransaction();
			transaction.begin();
			Cuinvoice aExistingInvoice = (Cuinvoice) aCuinvoiceSession.get(Cuinvoice.class, theCuinvoice.getCuInvoiceId());
			aExistingInvoice.setRxCustomerId(theCuinvoice.getRxCustomerId());
			aExistingInvoice.setTransactionStatus(theCuinvoice.getTransactionStatus());
			aExistingInvoice.setCreatedById(theCuinvoice.getCreatedById());
			aExistingInvoice.setCreatedOn(theCuinvoice.getCreatedOn());
			aExistingInvoice.setInvoiceDate(theCuinvoice.getInvoiceDate());
			aExistingInvoice.setApplied(theCuinvoice.getApplied());
			aExistingInvoice.setAppliedAmount(theCuinvoice.getAppliedAmount());
			aExistingInvoice.setInvoiceAmount(theCuinvoice.getInvoiceAmount());
			aExistingInvoice.setTaxAmount(theCuinvoice.getTaxAmount());
			aCuinvoiceSession.update(aExistingInvoice);
			transaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aCuinvoiceSession.flush();
			aCuinvoiceSession.close();
		}
		return false;
	}
	
	@Override
	public JoQuoteHeader getQuoteHeadID(JoQuoteHeader theQuoteHeaderID) {
		itsLogger.debug("getQuoteHeadID");
		String aSelectAll = "SELECT joQuoteHeaderID,cuMasterTypeID,QuoteRev FROM joQuoteHeader where jomasterID = '"+theQuoteHeaderID.getJoMasterID()+"' and " +
											"quoterev = '"+theQuoteHeaderID.getQuoteRev()+"' and cumastertypeID = '"+theQuoteHeaderID.getCuMasterTypeID()+"'";
		String aSelectWithCuMaster = "SELECT joQuoteHeaderID,cuMasterTypeID,QuoteRev FROM joQuoteHeader where jomasterID = '"+theQuoteHeaderID.getJoMasterID()+"' and cumastertypeID ='"+theQuoteHeaderID.getCuMasterTypeID()+"'";
		String aSelectWithQuoteRev = "SELECT joQuoteHeaderID,cuMasterTypeID,QuoteRev FROM joQuoteHeader where jomasterID = '"+theQuoteHeaderID.getJoMasterID()+"' and " +
																"quoterev = '"+theQuoteHeaderID.getQuoteRev()+"'";
		String aSelectWithJomaster = "SELECT joQuoteHeaderID,cuMasterTypeID,QuoteRev FROM joQuoteHeader where jomasterID = '"+theQuoteHeaderID.getJoMasterID()+"'";
		Session aSession = null;
		JoQuoteHeader ajoQuoteHeader = null;
		try {
			aSession = itsSessionFactory.openSession();
			ajoQuoteHeader = new JoQuoteHeader();
			Query aQueryAll = aSession.createSQLQuery(aSelectAll);
			Iterator<?> aIterator = aQueryAll.list().iterator();
			Query aQueryCumas = aSession.createSQLQuery(aSelectWithCuMaster);
			Iterator<?> aIterator1 = aQueryCumas.list().iterator();
			Query aQueryQuote = aSession.createSQLQuery(aSelectWithQuoteRev);
			Iterator<?> aIterator2 = aQueryQuote.list().iterator();
			Query aQueryJoma = aSession.createSQLQuery(aSelectWithJomaster);
			Iterator<?> aIterator3 = aQueryJoma.list().iterator();
			if(!aQueryAll.list().isEmpty()){
				while(aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					ajoQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
					ajoQuoteHeader.setCuMasterTypeID((Integer) aObj[1]);
					ajoQuoteHeader.setQuoteRev((String) aObj[2]);
				}
			}else if(!aQueryCumas.list().isEmpty()){
				while(aIterator1.hasNext()) {
					Object[] aObj = (Object[])aIterator1.next();
					ajoQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
					ajoQuoteHeader.setCuMasterTypeID((Integer) aObj[1]);
					ajoQuoteHeader.setQuoteRev((String) aObj[2]);
				}
			}else if(!aQueryCumas.list().isEmpty()){
				while(aIterator2.hasNext()) {
					Object[] aObj = (Object[])aIterator2.next();
					ajoQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
					ajoQuoteHeader.setCuMasterTypeID((Integer) aObj[1]);
					ajoQuoteHeader.setQuoteRev((String) aObj[2]);
				}
			}else{
				while(aIterator3.hasNext()) {
					Object[] aObj = (Object[])aIterator3.next();
					ajoQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
					ajoQuoteHeader.setCuMasterTypeID((Integer) aObj[1]);
					ajoQuoteHeader.setQuoteRev((String) aObj[2]);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
			aSelectAll = null;
			aSelectWithCuMaster = null;
			aSelectWithQuoteRev = null;
			aSelectWithJomaster = null;
		}
		return ajoQuoteHeader;
	}
	
	@Override
	public String getTypeID(Integer theQuoteType) {
		itsLogger.debug("getTypeID");
		String aQuerySelect= "Select code From cuMasterType Where cuMasterTypeId = "+theQuoteType;
		String aTypeName = null;
		Session aSession = null;
		try {
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQuerySelect);
			List<?> aList = aQuery.list();
			if(!aList.isEmpty()){
				aTypeName = (String) aList.get(0);	
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
			aQuerySelect = null;
		}
		return aTypeName;
	}
	
	@Override
	public JoQuoteHeader addQuoteHeaderId(JoQuoteHeader theQuoteHeaderID) {
		Session aSession = null;
		Transaction aRxMasterTransaction;
		JoQuoteHeader aJoQuoteHeader = null;
		try {
			aSession= itsSessionFactory.openSession();
			aRxMasterTransaction = aSession.beginTransaction();
			aRxMasterTransaction.begin();
			aSession.save(theQuoteHeaderID);
			aRxMasterTransaction.commit();
			aJoQuoteHeader = (JoQuoteHeader) aSession.get(JoQuoteHeader.class, theQuoteHeaderID.getJoQuoteHeaderId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuoteHeader;
	}
	
	@Override
	public CoTaxTerritory getCotaxterritory(Integer theCountyID){
		itsLogger.debug("theCountyID");
		Session aSession = null;
		String aQuerySelect = "SELECT county,state From coTaxTerritory where coTaxTerritoryId= "+theCountyID;
		CoTaxTerritory aCotaxterritory = null;
		try{
			aSession = itsSessionFactory.openSession();
			aCotaxterritory = new CoTaxTerritory();
			Query aQueryAll = aSession.createSQLQuery(aQuerySelect);
			Iterator<?> aIterator = aQueryAll.list().iterator();
			while(aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				aCotaxterritory.setCounty((String) aObj[0]);
				aCotaxterritory.setState((String) aObj[1]);
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
			aQuerySelect = null;
		}
		return aCotaxterritory;
	}

	@Override
	public ArrayList<EmMasterBean> getEmployeeCommissions(Integer periodID) {
		Session aSession = null;
		ArrayList<EmMasterBean> aQueryList = new ArrayList<EmMasterBean>();
		String whereCon = "";
		itsLogger.info("esStatement Details.");
		if(periodID!=-1 && periodID>0){
			whereCon = "AND es.ecPeriodID="+periodID;
		}else
		{
			whereCon = "AND es.ecPeriodID = (SELECT ecPeriodID FROM ecPeriod ORDER BY ecPeriodID DESC LIMIT 1)";
		}
		String	aCommissionQry = "SELECT em.UserLoginID, CONCAT(rx.FirstName,' ',rx.Name) as empName,  es.JobCommissions, es.OtherCommissions, es.Adjustments,"
				+ " es.Payment, es.ecStatementID FROM emMaster em  "
				+ " JOIN ecStatement es ON em.UserLoginID = es.RepLoginID  "
				+ " JOIN rxMaster rx ON rx.rxMasterID = em.emMasterID "
				+ " JOIN tsUserLogin tsu ON tsu.UserLoginID = em.UserLoginID WHERE rx.InActive = 0 AND GetsCommission = 1 "
				+ "AND tsu.Inactive = 0 "+whereCon+"  ORDER BY empName ASC";
		
		EmMasterBean aEmMasterBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCommissionQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEmMasterBean = new EmMasterBean();
				Object[] aObj = (Object[])aIterator.next();
				aEmMasterBean.setUserLoginId((Integer) aObj[0]);
				aEmMasterBean.setWebName((String) aObj[1]);
				aEmMasterBean.setJobCommissions((BigDecimal) aObj[2]);
				aEmMasterBean.setOtherCommissions((BigDecimal) aObj[3]);
				aEmMasterBean.setAdjustments((BigDecimal) aObj[4]);
				aEmMasterBean.setPayment((BigDecimal) aObj[5]);
				aEmMasterBean.setEcStatementID((Integer) aObj[6]);
				aQueryList.add(aEmMasterBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			whereCon = null;
			aCommissionQry = null;
		}
		return aQueryList;
	}

	@Override
	public EmMasterBean getEmployeeCommissionStatement(Integer UserLoginID,Integer ecPeriodID) {
		Session aSession = null;
		ArrayList<EmMasterBean> aQueryList = new ArrayList<EmMasterBean>();
		String whereCon = "";
		itsLogger.info("esStatement Details.UserLoginID: "+UserLoginID+" - PeriodID "+ecPeriodID);
		if(UserLoginID!=-1 && UserLoginID>0){
			whereCon = " AND em.UserLoginID="+UserLoginID;
		}
		if(ecPeriodID!=null && ecPeriodID >0)
		{
			whereCon = whereCon+" AND es.ecPeriodID ="+ ecPeriodID;
		}else{
			whereCon = whereCon+" AND es.ecPeriodID = (SELECT ecPeriodID FROM ecPeriod ORDER BY ecPeriodID DESC LIMIT 1)";
		}
		String	aCommissionQry = "SELECT em.UserLoginID, CONCAT(rx.FirstName,' ',rx.Name) as empName,  es.JobCommissions, es.OtherCommissions, es.Adjustments,"
				+ " es.Payment, es.ecStatementID FROM emMaster em  "
				+ " JOIN ecStatement es ON em.UserLoginID = es.RepLoginID  "
				+ " JOIN rxMaster rx ON rx.rxMasterID = em.emMasterID "
				+ " JOIN tsUserLogin tsu ON tsu.UserLoginID = em.UserLoginID WHERE rx.InActive = 0 AND GetsCommission = 1 "
				/*+ " AND es.ecPeriodID = (SELECT ecPeriodID FROM ecPeriod ORDER BY ecPeriodID DESC LIMIT 1) "*/
				+ " AND tsu.Inactive = 0 "+whereCon+"  ORDER BY empName ASC";
		
		EmMasterBean aEmMasterBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCommissionQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEmMasterBean = new EmMasterBean();
				Object[] aObj = (Object[])aIterator.next();
				aEmMasterBean.setUserLoginId((Integer) aObj[0]);
				aEmMasterBean.setWebName((String) aObj[1]);
				aEmMasterBean.setJobCommissions((BigDecimal) aObj[2]);
				aEmMasterBean.setOtherCommissions((BigDecimal) aObj[3]);
				aEmMasterBean.setAdjustments((BigDecimal) aObj[4]);
				aEmMasterBean.setPayment((BigDecimal) aObj[5]);
				aEmMasterBean.setEcStatementID((Integer) aObj[6]);
				aQueryList.add(aEmMasterBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			whereCon = null;
			aCommissionQry = null;
		}
		return aEmMasterBean;
	}
	
	@Override
	public Emmaster updateCommissions(Emmaster theEmmaster) {
		Session aEmMasterSession = itsSessionFactory.openSession();
		Transaction transaction;
		Emmaster aGetDetails = null;
		try {
			transaction = aEmMasterSession.beginTransaction();
			transaction.begin();
			Emmaster aEmmaster = (Emmaster) aEmMasterSession.get(Emmaster.class, theEmmaster.getEmMasterId());
			aEmmaster.setCommissionJobProfit(theEmmaster.getCommissionJobProfit());
			aEmmaster.setCommissionBuySellProfit(theEmmaster.getCommissionBuySellProfit());
			aEmmaster.setCommissionFactory(theEmmaster.getCommissionFactory());
			aEmmaster.setCommissionInvoiceProfit(theEmmaster.getCommissionInvoiceProfit());
			aEmmaster.setQuota(theEmmaster.getQuota());
			aEmmaster.setProfitBonus(theEmmaster.getProfitBonus());
			aEmmaster.setEnginComm(theEmmaster.getEnginComm());
			aEmmaster.setJobNumberPrefix(theEmmaster.getJobNumberPrefix());
			aEmmaster.setJobNumberSequence(theEmmaster.getJobNumberSequence());
			aEmmaster.setJobNumberGenerate(theEmmaster.getJobNumberGenerate());
			aEmmaster.setPaymentPeriod(theEmmaster.getPaymentPeriod());
			aEmmaster.setGetsCommission(theEmmaster.getGetsCommission());
			aEmmaster.setRepDeduct1(theEmmaster.getRepDeduct1());
			aEmmaster.setRepDeduct2(theEmmaster.getRepDeduct2());
			aEmmaster.setRepDeduct3(theEmmaster.getRepDeduct3());
			aEmmaster.setPaymentPeriod(theEmmaster.getPaymentPeriod());
			aEmmaster.setBonusComm1(theEmmaster.getBonusComm1());
			aEmmaster.setBonusComm2(theEmmaster.getBonusComm2());
			aEmmaster.setBonusComm3(theEmmaster.getBonusComm3());
			aEmmaster.setBonusComm4(theEmmaster.getBonusComm4());
			aEmmaster.setBonusComm5(theEmmaster.getBonusComm5());
			aEmmaster.setBonusComm6(theEmmaster.getBonusComm6());
			aEmmaster.setBonusComm7(theEmmaster.getBonusComm7());
			aEmmaster.setBonusComm8(theEmmaster.getBonusComm8());
			aEmmaster.setBonusLevel1(theEmmaster.getBonusLevel1());
			aEmmaster.setBonusLevel2(theEmmaster.getBonusLevel2());
			aEmmaster.setBonusLevel3(theEmmaster.getBonusLevel3());
			aEmmaster.setBonusLevel4(theEmmaster.getBonusLevel4());
			aEmmaster.setBonusLevel5(theEmmaster.getBonusLevel5());
			aEmmaster.setBonusLevel6(theEmmaster.getBonusLevel6());
			aEmmaster.setBonusLevel7(theEmmaster.getBonusLevel7());
			aEmmaster.setBonusLevel8(theEmmaster.getBonusLevel8());
			aEmmaster.setEmMasterId(theEmmaster.getEmMasterId());
			aEmmaster.setDayAfter(theEmmaster.getDayAfter());
			aEmmaster.setCommAfterPayment(theEmmaster.getCommAfterPayment());
			aEmmaster.setCommPayOn(theEmmaster.getCommPayOn());
			aEmMasterSession.update(aEmmaster);
			transaction.commit();
			aGetDetails = (Emmaster) aEmMasterSession.get(Emmaster.class, aEmmaster.getEmMasterId());
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aEmMasterSession.flush();
			aEmMasterSession.close();
		}
		return aGetDetails;
	}

	@Override
	public Emmaster updateCommissionsGeneral(Emmaster theEmmaster) {
		Session aEmMasterSession = itsSessionFactory.openSession();
		Transaction transaction;
		Emmaster aGetDetails = null;
		try {
			transaction = aEmMasterSession.beginTransaction();
			transaction.begin();
			Emmaster aEmmaster = (Emmaster) aEmMasterSession.get(Emmaster.class, theEmmaster.getEmMasterId());
			aEmmaster.setCoDivisionId(theEmmaster.getCoDivisionId());
			aEmmaster.setEmploymentType(theEmmaster.getEmploymentType());
			aEmmaster.setMaritalStatus(theEmmaster.getMaritalStatus());
			aEmmaster.setSex(theEmmaster.getSex());
			aEmmaster.setBirthDate(theEmmaster.getBirthDate());
			aEmmaster.setHireDate(theEmmaster.getHireDate());
			aEmMasterSession.update(aEmmaster);
			transaction.commit();
			aGetDetails = (Emmaster) aEmMasterSession.get(Emmaster.class, aEmmaster.getEmMasterId());
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aEmMasterSession.flush();
			aEmMasterSession.close();
		}
		return aGetDetails;
	}
	
	@Override
	public List<Rxcontact> getContacts(String rxMasterId) {
		Session aSession = null;
		List<Rxcontact> aQueryList = new ArrayList<Rxcontact>();
		String aCustomerQry="SELECT * FROM rxContact WHERE rxMasterID='"+rxMasterId+"'";
		
		Rxcontact aRolodexBean = null;
		try{
			
			aSession = itsSessionFactory.openSession();
			
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new Rxcontact();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setRxContactId((Integer)aObj[0]);
				aRolodexBean.setFirstName(((String)aObj[4])+" "+((String)aObj[6]));
				
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry = null;
		}
		return aQueryList;	
	}
	@Override
	public boolean updateRxaddressfromCustomerFinancial(Rxaddress therxaddress) throws CustomerException {
		Session aCuMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuMasterSession.beginTransaction();
			aTransaction.begin();
			Rxaddress aRxaddress = (Rxaddress) aCuMasterSession.get(Rxaddress.class, therxaddress.getRxAddressId());
			aRxaddress.setCoTaxTerritoryId(therxaddress.getCoTaxTerritoryId());
			aCuMasterSession.update(aRxaddress);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CustomerException aCustomerException = new CustomerException(excep.getMessage(),
					excep);
			throw aCustomerException;
		} finally {
			aCuMasterSession.flush();
			aCuMasterSession.close();
		}
		return false;
	}
	
/*	public boolean updatedefaultstatusbasedoncustomer(Rxmaster aCustomer) throws CustomerException {
		Session aSession = itsSessionFactory.openSession();
		
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			String sql = "UPDATE rxAddress SET IsDefault=0 WHERE rxMasterID="+aCustomer.getRxMasterId();
		Query query = aSession.createSQLQuery(sql);
		int result = query.executeUpdate();
		itsLogger.info("Rows affected: " + result);			
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}*/	
	
	@Override
	public boolean deleteAddressBasedonCustomer(Rxaddress aRxaddressparameter) throws CustomerException {
		Session aSession = itsSessionFactory.openSession();
		
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			Rxaddress aRxaddress = (Rxaddress)aSession.get(Rxaddress.class, aRxaddressparameter.getRxAddressId());
			aSession.delete(aRxaddress);	
			
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updatedefaultstatusbasedoncustomer(Rxaddress aRxaddressparameter) throws CustomerException {
		Session aSession = itsSessionFactory.openSession();
		
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			Rxaddress aRxaddress = (Rxaddress)aSession.get(Rxaddress.class, aRxaddressparameter.getRxAddressId());
			
		
			aRxaddress.setAddress1(aRxaddressparameter.getAddress1());
			aRxaddress.setAddress2(aRxaddressparameter.getAddress2());
			aRxaddress.setCity(aRxaddressparameter.getCity());
			aRxaddress.setState(aRxaddressparameter.getState());
			aRxaddress.setZip(aRxaddressparameter.getZip());
			aRxaddress.setInActive(aRxaddressparameter.getInActive());
			aRxaddress.setIsMailing(aRxaddressparameter.getIsMailing());
			aRxaddress.setIsShipTo(aRxaddressparameter.getIsShipTo());
			aRxaddress.setIsDefault(aRxaddressparameter.getIsDefault());
			aRxaddress.setIsStreet(aRxaddressparameter.getIsStreet());
			aRxaddress.setIsBillTo(aRxaddressparameter.getIsBillTo());
			aRxaddress.setOtherBillTo(aRxaddressparameter.getOtherBillTo());
			//aRxaddress.setCoTaxTerritoryId(aRxaddressparameter.getCoTaxTerritoryId());
			aRxaddress.setOtherShipTo(aRxaddressparameter.getOtherShipTo());
			aRxaddress.setPhone1(aRxaddressparameter.getPhone1());
			aRxaddress.setPhone2(aRxaddressparameter.getPhone2());
			aRxaddress.setFax(aRxaddressparameter.getFax());
			System.out.println("myAddress");
			aRxaddress.setIsRemitTo(aRxaddressparameter.getIsRemitTo());
			aSession.update(aRxaddress);	
			
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	
	@Override
	public boolean addEngineerTitles(Rxmastercategory2 aRxmastercategory2){
		itsLogger.debug("addNewAssignee");
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			aCuMasterTransaction.begin();
			aRxCumasterSession.save(aRxmastercategory2);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return true;
	}
	@Override
	public boolean updateEngineerTitles(Rxmastercategory2 aRxmastercategory2){
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			//aCuMasterTransaction.begin();
			Rxmastercategory2 theRxmastercategory2= (Rxmastercategory2)aRxCumasterSession.get(Rxmastercategory2.class, aRxmastercategory2.getRxMasterCategory2id());
			theRxmastercategory2.setCuAssignmentId0(aRxmastercategory2.getCuAssignmentId0());
			theRxmastercategory2.setCuAssignmentId1(aRxmastercategory2.getCuAssignmentId1());
			theRxmastercategory2.setCuAssignmentId2(aRxmastercategory2.getCuAssignmentId2());
			theRxmastercategory2.setCuAssignmentId3(aRxmastercategory2.getCuAssignmentId3());
			theRxmastercategory2.setCuAssignmentId4(aRxmastercategory2.getCuAssignmentId4());
			aRxCumasterSession.update(theRxmastercategory2);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return true;
	}
	
	@Override
	public boolean addArchitectTitles(Rxmastercategory1 aRxmastercategory1){
		itsLogger.debug("addArchitectTitles");
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			aCuMasterTransaction.begin();
			aRxCumasterSession.save(aRxmastercategory1);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateArchitectTitles(Rxmastercategory1 aRxmastercategory1){
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			//aCuMasterTransaction.begin();
			Rxmastercategory1 theRxmastercategory1= (Rxmastercategory1)aRxCumasterSession.get(Rxmastercategory1.class, aRxmastercategory1.getRxMasterCategory1id());
			theRxmastercategory1.setCuAssignmentId0(aRxmastercategory1.getCuAssignmentId0());
			theRxmastercategory1.setCuAssignmentId1(aRxmastercategory1.getCuAssignmentId1());
			theRxmastercategory1.setCuAssignmentId2(aRxmastercategory1.getCuAssignmentId2());
			theRxmastercategory1.setCuAssignmentId3(aRxmastercategory1.getCuAssignmentId3());
			theRxmastercategory1.setCuAssignmentId4(aRxmastercategory1.getCuAssignmentId4());
			aRxCumasterSession.update(theRxmastercategory1);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return true;
	}
	
	@Override
	public boolean addViewsTitles(RxMasterCategoryView aRxmastercategoryview){
		itsLogger.debug("addViewsTitles");
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			aCuMasterTransaction.begin();
			aRxCumasterSession.save(aRxmastercategoryview);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return true;
	}
	@Override
	public boolean updateViewTitles(RxMasterCategoryView aRxmastercategoryview){
		Session aRxCumasterSession = itsSessionFactory.openSession();
		Transaction aCuMasterTransaction;
		try {
			aCuMasterTransaction = aRxCumasterSession.beginTransaction();
			//aCuMasterTransaction.begin();
			RxMasterCategoryView theRxmastercategoryview= (RxMasterCategoryView)aRxCumasterSession.get(RxMasterCategoryView.class, aRxmastercategoryview.getRxMasterCategoryViewid());
			theRxmastercategoryview.setCuAssignmentId0(aRxmastercategoryview.getCuAssignmentId0());
			theRxmastercategoryview.setCuAssignmentId1(aRxmastercategoryview.getCuAssignmentId1());
			theRxmastercategoryview.setCuAssignmentId2(aRxmastercategoryview.getCuAssignmentId2());
			theRxmastercategoryview.setCuAssignmentId3(aRxmastercategoryview.getCuAssignmentId3());
			theRxmastercategoryview.setCuAssignmentId4(aRxmastercategoryview.getCuAssignmentId4());
			aRxCumasterSession.update(theRxmastercategoryview);
			aCuMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxCumasterSession.flush();
			aRxCumasterSession.close();
		}
		return true;
	}
	
	@Override
	public ArrayList<AutoCompleteBean> getCustomerNameListFromEmployee(String theCustomerName,Integer tsUserLoginID) {
		
		String aCustomerNameTypeSelectQry = "SELECT rxMaster.rxMasterID,rxMaster.Name FROM rxMaster JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID  WHERE (IFNULL(cuMaster.cuAssignmentID0,0) ="+tsUserLoginID+" OR IFNULL(cuMaster.cuAssignmentID1,0) ="+tsUserLoginID+" OR IFNULL(cuMaster.cuAssignmentID2,0) ="+tsUserLoginID+" OR IFNULL(cuMaster.cuAssignmentID3,0) ="+tsUserLoginID+" OR IFNULL(cuMaster.cuAssignmentID4,0) ="+tsUserLoginID+" ) AND rxMaster.Name LIKE '%"+theCustomerName+"%' ORDER BY rxMaster.Name ASC";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			AutoCompleteBean aUserbean = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerNameTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				aUserbean.setValue((String)aObj[1]);				/** UserName	*/
				aUserbean.setLabel((String)aObj[1]);	
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerNameTypeSelectQry = null;
		}
		return aQueryList;
	}
	@Override
	public List<RolodexBean> getCustomersFromLogin(int theFrom, int theTo,Integer tsUserLoginID){
		Session aSession = null;
		List<RolodexBean> aQueryList = new ArrayList<RolodexBean>();
		String condition = "";

		if(tsUserLoginID!= 0){
		 condition =" AND (cuMaster.cuAssignmentID0 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID1 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID2 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID3 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID4 = "+tsUserLoginID+"    ) ";
		 }
		
		String aCustomerQry = "SELECT rxMaster.rxMasterID,rxMaster.name,rxMaster.phone1,rxAddress.city,rxAddress.state,rxAddress.Address1 FROM rxMaster LEFT JOIN rxAddress on (rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.IsDefault=1) JOIN cuMaster on cuMaster.cuMasterID = rxMaster.rxMasterID WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT null AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' "+condition+" ORDER BY rxMaster.Name ASC LIMIT " + theFrom + ", " + theTo;
		//String aCustomerQry = "SELECT rxMaster.rxMasterID,rxMaster.name,rxMaster.phone1,rxAddress.city,rxAddress.state,rxAddress.Address1 FROM rxMaster LEFT JOIN rxAddress on (rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.IsDefault=1) JOIN cuMaster on cuMaster.cuMasterID = rxMaster.rxMasterID WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT null AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' AND (cuMaster.cuAssignmentID0 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID1 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID2 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID3 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID4 = "+tsUserLoginID+"    ) ORDER BY rxMaster.Name ASC LIMIT " + theFrom + ", " + theTo;
		
		RolodexBean aRolodexBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new RolodexBean();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setRxMasterId((Integer) aObj[0]);      /** rxMaster.rxMaserId	*/  
				aRolodexBean.setName((String) aObj[1]);             /**	rxMaster.name		*/  
				aRolodexBean.setPhone1((String) aObj[2]);           /**	rxAddress.phone1	*/  
				aRolodexBean.setCity((String)aObj[3]);              /**	rxAddress.city		*/  
				aRolodexBean.setState((String) aObj[4]);            /**	rxAddress.state		*/  
				aRolodexBean.setAddress1((String) aObj[5]);         /**	rxAddress.Address1	*/  
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			condition = null;
			aCustomerQry = null;
			aRolodexBean = null;
		}
		return aQueryList;	
	}
	@Override
	public int getRecordCountFromEmployee(Integer tsUserLoginID) {
	 String condition = "";
		if(tsUserLoginID!= 0){
		condition =" AND (cuMaster.cuAssignmentID0 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID1 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID2 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID3 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID4 = "+tsUserLoginID+"  ) ";
		}
		
		String aJobCountStr = "SELECT COUNT(rxMasterID) AS count FROM rxMaster JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT NULL AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' "+condition ;
		 
		//String aJobCountStr = "SELECT COUNT(rxMasterID) AS count FROM rxMaster JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT NULL AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' AND (cuMaster.cuAssignmentID0 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID1 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID2 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID3 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID4 = "+tsUserLoginID+"  )" ;
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			condition = null;
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}
	
	/**
	 * Created by: Leo Date: Dec 10 2014
	 * 
	 * Description: Check whether customerid used or not
	 * */
	
	@Override
	public boolean checkCustomeravailability(int therxCustomerid) {
		
		boolean checkingStatus = false;
		
		String aQuerySelect= "SELECT rxCustomerID FROM cuSO WHERE rxCustomerID = "+therxCustomerid;
		Session aSession = null;
		try {
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQuerySelect);
			List<?> aList = aQuery.list();
			if(!aList.isEmpty()){
				checkingStatus = true;	
			}
			
			if(checkingStatus==false)
			{
				aQuerySelect= "SELECT rxCustomerID FROM cuInvoice WHERE rxCustomerID = "+therxCustomerid;
				aQuery = aSession.createSQLQuery(aQuerySelect);
				aList = aQuery.list();
				if(!aList.isEmpty()){
					checkingStatus = true;	
				}
			}
			
			if(checkingStatus==false)
			{
				aQuerySelect= "SELECT rxVendorID FROM vePO WHERE rxVendorID = "+therxCustomerid;
				aQuery = aSession.createSQLQuery(aQuerySelect);
				aList = aQuery.list();
				if(!aList.isEmpty()){
					checkingStatus = true;	
				}
			}
			
			if(checkingStatus==false)
			{
				aQuerySelect= "SELECT rxMasterID FROM veBill WHERE rxMasterID = "+therxCustomerid;
				aQuery = aSession.createSQLQuery(aQuerySelect);
				aList = aQuery.list();
				if(!aList.isEmpty()){
					checkingStatus = true;	
				}
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
			aQuerySelect = null;
		}
		
		return checkingStatus;
	}
	@Override
	public boolean deleteCustomer(int theRxmaster) {
		itsLogger.debug("Delete Query Executed");
		
		Session aSession = null;
		String deleteQuery1="Delete from rxMaster where rxMasterID="+theRxmaster;
		String deleteQuery2="Delete from rxAddress where rxMasterID="+theRxmaster;
		String deleteQuery3="Delete from emMaster where emMasterID="+theRxmaster;
		String deleteQuery4="Delete from search_index where pk_fields="+theRxmaster;
		String deleteQuery5="Delete from cuMaster where cuMasterID="+theRxmaster;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			aSession.createSQLQuery(deleteQuery1).executeUpdate();
			aSession.createSQLQuery(deleteQuery2).executeUpdate();
			aSession.createSQLQuery(deleteQuery3).executeUpdate();
			aSession.createSQLQuery(deleteQuery4).executeUpdate();
			aSession.createSQLQuery(deleteQuery5).executeUpdate();
			transaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			deleteQuery1 = null;
			deleteQuery2= null;
			deleteQuery3= null;
			deleteQuery4= null;
			deleteQuery5= null;
		}
		return false;
	}
	/*@Override
	public boolean updateCustomerasInactive(int theRxmaster) {
		itsLogger.debug("Update as Inactive");
		Rxmaster aRxMaster = new Rxmaster(); 
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			aRxMaster = (Rxmaster) aSession.get(Rxmaster.class, theRxmaster);
			aRxMaster.setInActive(true);
			aSession.update(aRxMaster);
			
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}*/
	
	@Override
	public Boolean TemporaryMethodforInsert(Rxmaster theRxMaster,int userloginid) {
		itsLogger.debug("addNewCustomer");
		Session aRxMasterSession = itsSessionFactory.openSession();
		Transaction aRxMasterTransaction;
		Transaction aemMasterTransaction;
		Emmaster emmaster = null;
		try {
			theRxMaster.setFirstName("");
			theRxMaster.setInActive(false);
			aRxMasterTransaction = aRxMasterSession.beginTransaction();
			aRxMasterTransaction.begin();
			Integer therxmasterid=(Integer) aRxMasterSession.save(theRxMaster);
			aRxMasterTransaction.commit();
			emmaster = new Emmaster();
			emmaster.setEmMasterId(therxmasterid);
			emmaster.setUserLoginId(userloginid);
			aemMasterTransaction = aRxMasterSession.beginTransaction();
			aemMasterTransaction.begin();
			aRxMasterSession.save(emmaster);
			aemMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aRxMasterSession.flush();
			aRxMasterSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateChildTableBaseonChkbx(Integer rxMasterID,Integer userID){
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			Rxmaster therxMaster = (Rxmaster) aSession.get(Rxmaster.class, rxMasterID);
			Cumaster acuMaster=(Cumaster)aSession.get(Cumaster.class, therxMaster.getRxMasterId());
			Vemaster aveMaster=(Vemaster)aSession.get(Vemaster.class, therxMaster.getRxMasterId());
			Emmaster aemMaster=(Emmaster)aSession.get(Emmaster.class, therxMaster.getRxMasterId());
			if(therxMaster.isIsCustomer()){
				if(acuMaster==null){
					Query aQuery =aSession.createSQLQuery("Select CAST(IF(WarehouseID IS NULL or WarehouseID = '', 0, WarehouseID) AS CHAR)   as whseID,SysUserDefaultID  from SysUserDefault where UserLoginID = "+userID);
					String whId="0";
					Iterator<?> aIterator = aQuery.list().iterator();
					while (aIterator.hasNext()) {
						Object[] aObj = (Object[])aIterator.next();
						whId=(String) aObj[0]; 
						}
					
						Cumaster cum=new Cumaster();
						cum.setCuMasterId(therxMaster.getRxMasterId());
						cum.setCreditHold(false);
						cum.setPorequired(false);
						cum.setFinCharge((byte) 0);
						cum.setStatements((byte) 0);
						cum.setUseAvgDaysAsOf((byte) 0);
						cum.setPrWarehouseId(Integer.parseInt(whId));
						aSession.save(cum);
				}
			}else{
				if(acuMaster!=null){
					aSession.delete(acuMaster);
				}
			}
			if(therxMaster.isIsVendor() ){
				if(aveMaster==null){
					Vemaster thevemaster=new Vemaster();
					thevemaster.setVeMasterId(therxMaster.getRxMasterId());
					thevemaster.setImportType(0);
					aSession.save(thevemaster);
				}
			}else{
				if(aveMaster!=null){
					aSession.delete(aveMaster);
								}
			}
			if(therxMaster.isIsEmployee() ){
				if(aemMaster==null){
					Emmaster theemmaster=new Emmaster();
					theemmaster.setEmMasterId(therxMaster.getRxMasterId());
					aSession.save(theemmaster);
				}
			}else{
				if(aemMaster!=null){
				aSession.delete(aemMaster);
				}
			}
			aTransaction.commit();
			
			
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}
	
	@Override
	public Boolean updateotherrxaddress(Integer rxcustomerID) {
		
		String aCustomerNameTypeSelectQry = "UPDATE rxAddress SET IsDefault=0 WHERE rxMasterID="+rxcustomerID;
		Session aSession=null;
		Transaction aTransaction = null;
		System.out.println("aCustomerNameTypeSelectQry" +aCustomerNameTypeSelectQry);
		try{
			aSession=itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			aSession.createSQLQuery(aCustomerNameTypeSelectQry).executeUpdate();
			aTransaction.commit();
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerNameTypeSelectQry = null;
		}
		return true;
	}
	
	@Override
	public boolean updateRxMasterfromempDetails(Rxmaster theRxmaster){
		itsLogger.debug("updateRolodexDetails");
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			Rxmaster aExistingrxMaster= (Rxmaster)aSession.get(Rxmaster.class, theRxmaster.getRxMasterId());
			itsLogger.info("aExistingrxMaster"+aExistingrxMaster);
			aExistingrxMaster.setName(theRxmaster.getName());
			aExistingrxMaster.setFirstName(theRxmaster.getFirstName());
			aExistingrxMaster.setPhone1(theRxmaster.getPhone1());
			aExistingrxMaster.setPhone2(theRxmaster.getPhone2());
			aExistingrxMaster.setFax(theRxmaster.getFax());
			aExistingrxMaster.setPhone3(theRxmaster.getPhone3());
			aExistingrxMaster.setInActive(theRxmaster.getInActive());
			aSession.update(aExistingrxMaster);
			aTransaction.commit();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}
	
	
	@Override
	public boolean updateRxaddress(Rxaddress therxaddress) throws CustomerException {
		Session aCuMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuMasterSession.beginTransaction();
			aTransaction.begin();
			Rxaddress aRxaddress = (Rxaddress) aCuMasterSession.get(Rxaddress.class, therxaddress.getRxAddressId());
			aRxaddress.setAddress1(therxaddress.getAddress1());
			aRxaddress.setAddress2(therxaddress.getAddress2());
			aRxaddress.setCity(therxaddress.getCity());
			aRxaddress.setState(therxaddress.getState());
			aRxaddress.setZip(therxaddress.getZip());
			aCuMasterSession.update(aRxaddress);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CustomerException aCustomerException = new CustomerException(excep.getMessage(),
					excep);
			throw aCustomerException;
		} finally {
			aCuMasterSession.flush();
			aCuMasterSession.close();
		}
		return false;
	}
	@Override
	public boolean updateRemittorxaddress(Integer rxMasterId)throws CustomerException {
		Session aCuMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuMasterSession.beginTransaction();
			aTransaction.begin();
			aCuMasterSession.createSQLQuery("UPDATE rxAddress SET IsRemitTo = FALSE WHERE rxMasterID = "+rxMasterId).executeUpdate();
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CustomerException aCustomerException = new CustomerException(excep.getMessage(),
					excep);
			throw aCustomerException;
		} finally {
			aCuMasterSession.flush();
			aCuMasterSession.close();
		}
		return false;
	}
	
	
	
	@Override
	public boolean addoreditRolodexAddress(Rxaddress therxaddress, String Oper)
			throws CustomerException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();	
			aTransaction.begin();
			
			if(Oper.equals("add"))
			{
				aSession.save(therxaddress);
				
				Rxmaster aRxmaster = (Rxmaster)aSession.get(Rxmaster.class, therxaddress.getRxMasterId());
				if(therxaddress.getIsDefault()){
					aRxmaster.setPhone1(therxaddress.getPhone1());
					aRxmaster.setPhone2(therxaddress.getPhone2());
					aRxmaster.setFax(therxaddress.getFax());
					aSession.update(aRxmaster);
				}
				
			}
			else if(Oper.equals("edit"))
			{
				Rxaddress aRxaddress = (Rxaddress)aSession.get(Rxaddress.class, therxaddress.getRxAddressId());
				aRxaddress.setAddress1(therxaddress.getAddress1());
				aRxaddress.setAddress2(therxaddress.getAddress2());
				aRxaddress.setCity(therxaddress.getCity());
				aRxaddress.setState(therxaddress.getState());
				aRxaddress.setZip(therxaddress.getZip());
				aRxaddress.setInActive(therxaddress.getInActive());
				aRxaddress.setIsMailing(therxaddress.getIsMailing());
				aRxaddress.setIsShipTo(therxaddress.getIsShipTo());
				aRxaddress.setIsDefault(therxaddress.getIsDefault());
				aRxaddress.setIsStreet(therxaddress.getIsStreet());
				aRxaddress.setIsBillTo(therxaddress.getIsBillTo());
				aRxaddress.setOtherBillTo(therxaddress.getOtherBillTo());
				aRxaddress.setOtherShipTo(therxaddress.getOtherShipTo());
				aRxaddress.setPhone1(therxaddress.getPhone1());
				aRxaddress.setPhone2(therxaddress.getPhone2());
				aRxaddress.setFax(therxaddress.getFax());
				aRxaddress.setIsRemitTo(therxaddress.getIsRemitTo());
				aRxaddress.setName(therxaddress.getName());
				aSession.update(aRxaddress);
				
				Rxmaster aRxmaster = (Rxmaster)aSession.get(Rxmaster.class, therxaddress.getRxMasterId());
				if(therxaddress.getIsDefault()){
					aRxmaster.setPhone1(therxaddress.getPhone1());
					aRxmaster.setPhone2(therxaddress.getPhone2());
					aRxmaster.setFax(therxaddress.getFax());
					aSession.update(aRxmaster);
				}
			}
			else if(Oper.equals("delete"))
			{
				Rxaddress aRxaddress = (Rxaddress)aSession.get(Rxaddress.class, therxaddress.getRxAddressId());
				aSession.delete(aRxaddress);
			}
			
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	@Override
	public boolean updateDefaultrxaddress(Integer rxMasterId)
			throws CustomerException {
		Session aCuMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuMasterSession.beginTransaction();
			aTransaction.begin();
			aCuMasterSession.createSQLQuery("UPDATE rxAddress SET IsDefault = FALSE WHERE rxMasterID = "+rxMasterId).executeUpdate();
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CustomerException aCustomerException = new CustomerException(excep.getMessage(),
					excep);
			throw aCustomerException;
		} finally {
			aCuMasterSession.flush();
			aCuMasterSession.close();
		}
		return false;
	}
}