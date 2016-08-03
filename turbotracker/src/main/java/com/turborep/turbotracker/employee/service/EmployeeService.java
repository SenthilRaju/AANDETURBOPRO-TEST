/**
 * 
 */
package com.turborep.turbotracker.employee.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.service.InventoryConstant;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.employee.dao.CommissionCalculateBean;
import com.turborep.turbotracker.employee.dao.CommissionStatementBean;
import com.turborep.turbotracker.employee.dao.Ecinvoiceperiod;
import com.turborep.turbotracker.employee.dao.Ecinvoicerepsplit;
import com.turborep.turbotracker.employee.dao.Ecinvoices;
import com.turborep.turbotracker.employee.dao.Ecjobs;
import com.turborep.turbotracker.employee.dao.Ecperiod;
import com.turborep.turbotracker.employee.dao.Ecsalesmgroverride;
import com.turborep.turbotracker.employee.dao.Ecsplitjob;
import com.turborep.turbotracker.employee.dao.Ecstatement;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.job.dao.JoInvoiceCost;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.Vebill;

/**
 * 
 * Handles CRUD services for users
 * 
 * @author vish_pepala
 */
@Service("employeeService")
@Transactional
public class EmployeeService implements EmployeeServiceInterface {

	protected static Logger itsLogger = Logger.getLogger("service");

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;

	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;

	@Resource(name = "jobService")
	private JobService jobService;

	/**
	 * Retrieves all persons
	 * 
	 * @return a list of persons
	 */
	public List<Rxmaster> getAll() {
		itsLogger.debug("Retrieving all persons");
		Session aSession = null;
		List<Rxmaster> aQueryList = null;
		Query query = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			query = aSession
					.createQuery("FROM  Rxmaster WHERE isEmployee = 1");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aQueryList;
	}

	/**
	 * Retrieves a single person
	 */
	public Rxmaster get(String theId) {
		itsLogger.debug("Retrieving all persons");
		// Retrieve session from Hibernate
		Session aSession = itsSessionFactory.openSession();
		Rxmaster aPerson = null;
		try {
			// Retrieve existing person first
			aPerson = (Rxmaster) aSession.get(Rxmaster.class, theId);
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPerson;
	}

	/**
	 * Adds a new person
	 * 
	 * @return
	 */
	public Boolean add(Rxmaster person) {
		itsLogger.debug("Adding new person");
		Session aSession = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Save
			aSession.save(person);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	/**
	 * Deletes an existing person
	 * 
	 * @param theId
	 *            the id of the existing person
	 */
	public Boolean delete(int theId) {
		itsLogger.debug("Deleting existing person");
		Session aSession = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Retrieve existing person first
			Rxmaster person = (Rxmaster) aSession.get(Rxmaster.class, theId);
			// Delete
			aSession.delete(person);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	public Boolean delete(Rxmaster theUserId) {
		Session aSession = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			aSession.delete(theUserId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	/**
	 * Edits an existing person
	 * 
	 * @return
	 */
	public Boolean edit(Rxmaster aPerson) {
		itsLogger.debug("Editing existing person");
		Session aSession = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Retrieve existing person via id
			Rxmaster aExistingPerson = (Rxmaster) aSession.get(Rxmaster.class,
					aPerson.getRxMasterId());
			// Assign updated values to this person
			aExistingPerson.setFirstName(aPerson.getFirstName());
			aExistingPerson.setSearchName(aPerson.getSearchName());
			// existingPerson.setMoney(person.getMoney());
			// Save updates
			aSession.save(aExistingPerson);
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public ArrayList<?> getCSRs() {
		itsLogger.debug("Retrieving all CSRs");
		String salesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE employee1 = 1 AND inactive = 0  AND LoginName != 'admin'"
				+ "ORDER BY FullName ASC";
		Session aSession = null;Query query =null;
		ArrayList<UserBean> aQueryList = new ArrayList<UserBean>();
		
		try {
			UserBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			 query = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = query.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new UserBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setUserId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setUserName((String) aObj[1]);
				/** UserName */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry =null;query =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<?> getSalesMGRs() {
		itsLogger.debug("Retrieving all SalesMGRs");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE employee2 = 1 AND inactive = 0  AND LoginName != 'admin'"
				+ "ORDER BY FullName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<UserBean> aQueryList = new ArrayList<UserBean>();
		try {
			UserBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new UserBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setUserId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setUserName((String) aObj[1]);
				/** UserName */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<UserBean> getProjManagers() {
		itsLogger.debug("Retrieving all ProjManagers");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE Employee4 = 1 AND inactive = 0  AND LoginName != 'admin'"
				+ "ORDER BY FullName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<UserBean> aQueryList = new ArrayList<UserBean>();
		try {
			UserBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new UserBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setUserId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setUserName((String) aObj[1]);
				/** UserName */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<UserBean> getEngineers() {
		itsLogger.debug("Retrieving all Engineers");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE Employee3 = 1 AND inactive = 0  AND LoginName != 'admin'"
				+ "ORDER BY FullName ASC";
		Session aSession = null;Query aQuery = null;
		ArrayList<UserBean> aQueryList = new ArrayList<UserBean>();
		try {
			UserBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new UserBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setUserId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setUserName((String) aObj[1]);
				/** UserName */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<UserBean> getQuotesBy() {
		itsLogger.debug("Retrieving all QuotesBy");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE employee0 = 1 AND employee1 = 0 OR inactive = 0  AND LoginName != 'admin' ORDER BY FullName";
		Session aSession = null;Query aQuery =null;
		ArrayList<UserBean> aQueryList = new ArrayList<UserBean>();
		try {
			UserBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new UserBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setUserId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setUserName((String) aObj[1]);
				/** UserName */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public List<CuMasterType> getCustomerType() {
		itsLogger.debug("Retrieving all CustomerType");
		String aCustomerTypeSelectQry = "SELECT cuMasterTypeID, Code, Description FROM cuMasterType";
		Session aSession = null;Query aQuery = null;
		ArrayList<CuMasterType> aQueryList = new ArrayList<CuMasterType>();
		try {
			CuMasterType aCustomerType = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCustomerType = new CuMasterType();
				Object[] aObj = (Object[]) aIterator.next();
				aCustomerType.setCuMasterTypeId((Integer) aObj[0]);
				String aCode = (String) aObj[1];
				String aDescription = (String) aObj[2];
				aCustomerType.setCode(aCode + " | " + aDescription);
				aCustomerType.setDescription(aDescription);
				aQueryList.add(aCustomerType);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerTypeSelectQry =null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public List<Prwarehouse> getWherehouseType() {
		itsLogger.debug("Retrieving all WherehouseType");
		String aWerehouseTypeSelectQry = "SELECT prWarehouseID, SearchName FROM prWarehouse";
		Session aSession = null;Query aQuery =null;
		ArrayList<Prwarehouse> aQueryList = new ArrayList<Prwarehouse>();
		try {
			Prwarehouse aWerehouseype = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aWerehouseype = new Prwarehouse();
				Object[] aObj = (Object[]) aIterator.next();
				aWerehouseype.setPrWarehouseId((Integer) aObj[0]);
				aWerehouseype.setSearchName((String) aObj[1]);
				aQueryList.add(aWerehouseype);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aWerehouseTypeSelectQry =null;aQuery =null;
		}

		return aQueryList;
	}

	@Override
	public List<CuTerms> getPaymentTerms() {
		itsLogger.debug("Retrieving all PaymentTerms");
		String aWerehouseTypeSelectQry = "SELECT cuTermsID, Description FROM cuTerms";
		Session aSession = null;Query aQuery = null;
		ArrayList<CuTerms> aQueryList = new ArrayList<CuTerms>();
		try {
			CuTerms aTermsType = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aTermsType = new CuTerms();
				Object[] aObj = (Object[]) aIterator.next();
				aTermsType.setCuTermsId((Integer) aObj[0]);
				aTermsType.setDescription((String) aObj[1]);
				aQueryList.add(aTermsType);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aWerehouseTypeSelectQry = null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCSRList(String theSalesRep) {
		itsLogger.debug("Retrieving all CSRList");
		String aCSRselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin " + "WHERE employee1 = 1 AND inactive = 0 "
				+ "AND FullName like " + "'%" + theSalesRep + "%'"
				+ "  AND LoginName != 'admin' ORDER BY FullName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCSRselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}

			if (aQueryList.isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setValue(" ");
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCSRselectQry =null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getSalesMGRList(String salesRep) {
		itsLogger.debug("Retrieving all SalesMGRList");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin " + "WHERE employee2 = 1 AND inactive = 0 "
				+ "AND FullName like " + "'%" + salesRep + "%'"
				+ "  AND LoginName != 'admin' ORDER BY FullName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}

			if (aQueryList.isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setValue(" ");
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getProjectManagerList(String theSalesRep) {
		itsLogger.debug("Retrieving all ProjectManagerList");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin " + "WHERE Employee4 = 1 AND inactive = 0 "
				+ "AND FullName like " + "'%" + theSalesRep + "%'"
				+ "  AND LoginName != 'admin' ORDER BY FullName ASC";
		Session aSession = null;Query query =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			query = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = query.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
			if (aQueryList.isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setValue(" ");
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;query =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getEngineerList(String theSalesRep) {
		itsLogger.debug("Retrieving EngineerList");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin " + "WHERE Employee3 = 1 AND inactive = 0 "
				+ "AND FullName like " + "'%" + theSalesRep + "%'"
				+ "  AND LoginName != 'admin' ORDER BY FullName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
			if (aQueryList.isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setValue(" ");
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null;
			aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getQuotesList(String theSalesRep) {
		itsLogger.debug("Retrieving QuotesList");
		String aSalesselectQry = "SELECT UserLoginId, FullName "
				+ "FROM tsUserLogin "
				+ "WHERE (employee0 = 1 OR employee1 = 0) AND inactive = 0 "
				+ "AND FullName like " + "'%" + theSalesRep + "%'"
				+ "  AND LoginName != 'admin' ORDER BY FullName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry =null;
			aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getTermsDueDate(Integer cuTermsID) {

		String aWerehouseTypeSelectQry = "SELECT cuTermsID, Description,DueDays,DueOnDay FROM cuTerms WHERE cuTermsID  ="
				+ cuTermsID + " ";
		Session aSession = null;Query aQuery = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aQuery.list().isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setDueDays((short) 0);
				aUserbean.setDueOnDay((byte) 0);
				aQueryList.add(aUserbean);
			}
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setDueDays(((Short) aObj[2]));
				aUserbean.setDueOnDay((Byte) aObj[3]);
				aQueryList.add(aUserbean);

			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aWerehouseTypeSelectQry =null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getPaymentTermType(String theSalesRep) {
		itsLogger.debug("Retrieving PaymentTermTypes");
		String aWerehouseTypeSelectQry = "SELECT cuTermsID, Description,DueDays,DueOnDay FROM cuTerms WHERE Description like "
				+ "'%" + theSalesRep + "%'" + " ORDER BY Description ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aUserbean.setDueDays(((Short) aObj[2]));
				Calendar c = Calendar.getInstance();
				aUserbean.setDueOnDay((Byte) aObj[3]);
				if (aUserbean.getDueOnDay() == 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

					c.setTime(new Date()); // Now use today date.
					c.add(Calendar.DATE, (Short) aObj[2]);
					aUserbean.setMemo(sdf.format(c.getTime()));
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/"
							+ (Short) aObj[2] + "/yyyy");
					c.setTime(new Date()); // Now use today date.
					c.add(Calendar.DATE, 32);
					aUserbean.setMemo(sdf.format(c.getTime()));
				}

				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aWerehouseTypeSelectQry =null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCustomerType(String theSalesRep) {
		itsLogger.debug("Retrieving all Customer Type");
		String aCustomerTypeSelectQry = "SELECT cuMasterTypeID, Code, Description FROM cuMasterType WHERE  Code LIKE "
				+ "'%"
				+ theSalesRep
				+ "%'"
				+ "  OR Description like "
				+ "'%"
				+ theSalesRep + "%'" + " ORDER BY Description ASC";
		Session aSession = null;Query aQuery = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				String aCode = (String) aObj[1];
				String aDescription = (String) aObj[2];
				aUserbean.setValue(aCode);
				/** UserName */
				aUserbean.setLabel(aCode + " (" + aDescription + ")");
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerTypeSelectQry =null; aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getWarehouseType(String theSalesRep) {
		itsLogger.debug("Query has to WarehouseType");
		String aWerehouseTypeSelectQry = "SELECT prWarehouseID, SearchName FROM prWarehouse WHERE SearchName like "
				+ "'%" + theSalesRep + "%'" + " ORDER BY SearchName ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aWerehouseTypeSelectQry=null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public Rxmaster addNewEmployee(Rxmaster theEmployee, Rxaddress theRxaddress) {
		itsLogger.debug("Query has to add NewEmployee");
		Session aRxMasterSession = itsSessionFactory.openSession();
		Transaction aRxMasterTransaction;
		Rxmaster aRxMaster = null;
		try {
			aRxMasterTransaction = aRxMasterSession.beginTransaction();
			aRxMasterTransaction.begin();
			aRxMasterSession.save(theEmployee);
			theRxaddress.setRxMasterId(theEmployee.getRxMasterId());
			aRxMasterSession.save(theRxaddress);

			aRxMasterTransaction.commit();
			aRxMaster = (Rxmaster) aRxMasterSession.get(Rxmaster.class,
					theEmployee.getRxMasterId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aRxMasterSession.flush();
			aRxMasterSession.close();
		}
		return aRxMaster;
	}

	@Override
	public Integer checkPeriodExists(String periodDate) {
		itsLogger.debug("Query has to Check NewPeriod");
		Session periodSession = itsSessionFactory.openSession();
		Ecperiod aEcperiod = null;
		Date date1 = null;
		String newDate = null;
		Integer status = 0;
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(periodDate);
			newDate = new SimpleDateFormat("yyyy-MM-dd").format(date1);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		itsLogger.info("Date: " + newDate);
		Query aQuery =null;
		String aSelectEcPeriod = "SELECT * FROM ecPeriod WHERE PeriodEndingDate>='"
				+ newDate + "'";
		List<Ecperiod> aQueryList = new ArrayList<Ecperiod>();
		try {
			periodSession = itsSessionFactory.openSession();
			aQuery = periodSession.createSQLQuery(aSelectEcPeriod);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEcperiod = new Ecperiod();
				Object[] aObj = (Object[]) aIterator.next();
				aEcperiod.setEcPeriodId((Integer) aObj[0]);
				aEcperiod.setPeriodEndingDate((Date) aObj[1]);
				aQueryList.add(aEcperiod);
			}
			if (aQueryList.size() > 0) {
				status = 1;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			periodSession.flush();
			periodSession.close();
			aSelectEcPeriod=null;aQuery =null;
		}
		return status;
	}

	@Override
	public Ecperiod addNewCommissionPeriod(String periodDate) {
		itsLogger.debug("Query has to add NewPeriod");
		Session periodSession = itsSessionFactory.openSession();
		Ecperiod aEcperiod = null;
		Date date1 = null;
		String newDate = null;
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(periodDate);
			newDate = new SimpleDateFormat("yyyy-MM-dd").format(date1);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		itsLogger.info("Date: " + newDate);
		String aSelectEcPeriod = "SELECT * FROM ecPeriod  ORDER BY PeriodEndingDate DESC LIMIT 1";
		Query aQuery =null;
		List<Ecperiod> aQueryList = null;
		try {
			aEcperiod = new Ecperiod();
			periodSession = itsSessionFactory.openSession();
			aQueryList = new ArrayList<Ecperiod>();
			aQuery = periodSession.createSQLQuery(aSelectEcPeriod);
			Iterator<?> aIterator = aQuery.list().iterator();

			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aEcperiod.setEcPeriodId((Integer) aObj[0]);
				aEcperiod.setPeriodEndingDate((Date) aObj[1]);

				aQueryList.add(aEcperiod);
			}
			if(aQueryList.size()>0){
			if (date1.compareTo(aEcperiod.getPeriodEndingDate()) > 0) {
				itsLogger.info("New Date After Last Period");
				Transaction periodInsertTransaction;
				Ecperiod theEcperiod = new Ecperiod();
				theEcperiod.setPeriodEndingDate(date1);
				periodInsertTransaction = periodSession
						.beginTransaction();
				periodInsertTransaction.begin();
				periodSession.save(theEcperiod);
				periodInsertTransaction.commit();
				aEcperiod = (Ecperiod) periodSession.get(Ecperiod.class,
						theEcperiod.getEcPeriodId());
			} 
			}
			else{
				itsLogger.info("New Date");
				Transaction periodInsertTransaction;
				Ecperiod theEcperiod = new Ecperiod();
				theEcperiod.setPeriodEndingDate(date1);
				periodInsertTransaction = periodSession.beginTransaction();
				periodInsertTransaction.begin();
				periodSession.save(theEcperiod);
				periodInsertTransaction.commit();
				aEcperiod = (Ecperiod) periodSession.get(Ecperiod.class,
						theEcperiod.getEcPeriodId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			periodSession.flush();
			periodSession.close();
			aSelectEcPeriod=null;aQuery =null;
		}
		return aEcperiod;
	}

	@Override
	public ArrayList<Ecperiod> getPeriodEnding() {
		Session aSession = null;
		ArrayList<Ecperiod> aEcperiodList = new ArrayList<Ecperiod>();
		Ecperiod aEcperiod = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery("SELECT * FROM  ecPeriod");
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEcperiod = new Ecperiod();
				Object[] aObj = (Object[]) aIterator.next();
				aEcperiod.setEcPeriodId((Integer) aObj[0]);
				// Date date1 = new
				// SimpleDateFormat("yyyy/MM/dd").parse((String) aObj[1]);
				String newDate = new SimpleDateFormat("MM/dd/yyyy")
						.format((Date) aObj[1]);
				aEcperiod.setEndDate(newDate);
				aEcperiodList.add(aEcperiod);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
		return aEcperiodList;
	}

	@Override
	public Sysinfo getSysinfoForCommissions() throws EmployeeException {
		Session aSession = null;
		List<Sysinfo> aQueryList = null;
		Sysinfo aSysinfo = null;Query query =null;
		try {
			aSession = itsSessionFactory.openSession();
			 query = aSession
					.createQuery("FROM Sysinfo WHERE sysInfoId=1");
			aQueryList = query.list();
			if (aQueryList.size() > 0) {
				aSysinfo = aQueryList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return aSysinfo;
	}

	@Override
	public List<Cofiscalperiod> getCoFiscalPeriodList()
			throws EmployeeException {
		Session aSession = null;Query query =null;
		List<Cofiscalperiod> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("FROM Cofiscalperiod order by coFiscalPeriodId desc");
			aQueryList = query.list();
			for (Cofiscalperiod cfp : aQueryList) {
				cfp.setStrdate((new SimpleDateFormat("MM/dd/yyyy")
						.format((Date) cfp.getEndDate())));
			}

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting the Fried Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return aQueryList;
	}

	@Override
	public List<Ecperiod> getEcPeriodStartEnd(String condition) throws EmployeeException {
		Session aSession = null;Query query =null;
		List<Ecperiod> aQueryList = null;
		if(condition!=null && !condition.trim().equals("")){
			condition = "WHERE ecPeriodID <= "+condition; 
		}
		else
		{
			condition = "";
		}
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("FROM Ecperiod "+condition+" ORDER BY PeriodEndingDate DESC LIMIT 2");
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred: " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return aQueryList;
	}

	@Override
	public List<Ecperiod> isPeriodsExists(Integer ecPeriodID) throws EmployeeException{
		Session aSession = null;
		List<Ecperiod> aQueryList = new ArrayList<Ecperiod>();
		String whereCon = "";
		Ecperiod aEcperiod =null;Query aQuery =null;
		if(ecPeriodID!=-1 && ecPeriodID>0){
				whereCon = " WHERE ecPeriodID > "+ecPeriodID;
		}else
		{
				whereCon = " WHERE ecPeriodID > (SELECT ecp.ecPeriodID  FROM ecPeriod ecp ORDER BY ecp.ecPeriodID DESC LIMIT 1)";
		}
		try {
			aSession = itsSessionFactory.openSession();
		aQuery = aSession.createSQLQuery("SELECT * FROM  ecPeriod"+whereCon);
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			aEcperiod = new Ecperiod();
			Object[] aObj = (Object[]) aIterator.next();
			aEcperiod.setEcPeriodId((Integer) aObj[0]);
			// Date date1 = new
			// SimpleDateFormat("yyyy/MM/dd").parse((String) aObj[1]);
			String newDate = new SimpleDateFormat("MM/dd/yyyy")
					.format((Date) aObj[1]);
			aEcperiod.setEndDate(newDate);
			aQueryList.add(aEcperiod);
		}
		
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred: " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery=null;whereCon = null;
		}
		return aQueryList;
	
	}
	
	@Override
	public Integer getLastEcPeriod() throws EmployeeException {
		Session aSession = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession
					.createSQLQuery("SELECT ecPeriodID FROM ecPeriod ORDER BY 1 DESC LIMIT 1");
			List<?> aList = aQuery.list();
			if(aList.size()>0){
				return (Integer) aList.get(0);
			}
			else{
				return 0;
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred: " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;
		}
	}

	@Override
	public Boolean deleteCommissionsDetails(Integer ecPeriodID)
			throws EmployeeException {
		Session aInventorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String ecInvoiceRepSplit = "DELETE FROM ecInvoiceRepSplit WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String joInvoiceCostUpdate = "UPDATE joInvoiceCost SET ecInvoicePeriodID = NULL WHERE "
				+ "ecInvoicePeriodID IN (SELECT ecInvoicePeriodID FROM ecInvoicePeriod WHERE ecPeriodID = "
				+ ecPeriodID+")";
		String veCommDetailUpdate = "UPDATE veCommDetail SET ecInvoicePeriodID = NULL WHERE "
				+ "ecInvoicePeriodID IN (SELECT ecInvoicePeriodID FROM ecInvoicePeriod WHERE ecPeriodID = "
				+ ecPeriodID+")";
		String ecInvoicePeriod = "DELETE FROM ecInvoicePeriod WHERE ecPeriodID = "
				+ ecPeriodID;
		String ecJobs = "DELETE FROM ecJobs WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String ecInvoices = "DELETE FROM ecInvoices WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String ecSalesMgrOverride = "DELETE FROM ecSalesMgrOverride WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		
		try {
			aTransaction = aInventorySession.beginTransaction();
			aInventorySession.createSQLQuery(ecInvoiceRepSplit).executeUpdate();
			aInventorySession.createSQLQuery(joInvoiceCostUpdate).executeUpdate();
			aInventorySession.createSQLQuery(veCommDetailUpdate).executeUpdate();
			aInventorySession.createSQLQuery(ecInvoicePeriod).executeUpdate();
			aInventorySession.createSQLQuery(ecJobs).executeUpdate();
			aInventorySession.createSQLQuery(ecInvoices).executeUpdate();
			aInventorySession.createSQLQuery(ecSalesMgrOverride).executeUpdate();
			aTransaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			EmployeeException aEmployeeException = new EmployeeException(
					e.getMessage(), e);
			throw aEmployeeException;
		} finally {
			aInventorySession.flush();
			aInventorySession.close();
			ecInvoiceRepSplit =null;
			ecInvoicePeriod =null;
			ecJobs =null;
			ecInvoices =null;
			ecSalesMgrOverride=null;
		}
		return true;
	}

	@Override
	public Integer saveNewPeriod(Integer ecPeriodID) {
		itsLogger.debug("saveNewPeriod");
		String aWerehouseTypeSelectQry = "SELECT emMaster.*, rxMaster.Name, rxMaster.FirstName FROM emMaster LEFT JOIN rxMaster ON "
				+ "emMaster.emMasterID = rxMaster.rxMasterID WHERE emMaster.GetsCommission= 1 AND rxMaster.IsEmployee=1 AND rxMaster.InActive =0 AND UserLoginID IS NOT NULL ORDER BY "
				+ "rxMaster.Name, rxMaster.FirstName";
		Session aSession = null;Query aQuery =null;
		Integer aInStatus = 0;
		ArrayList<Ecstatement> aQueryList = new ArrayList<Ecstatement>();
		try {
			Ecstatement aEcstatement = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEcstatement = new Ecstatement();
				Object[] aObj = (Object[]) aIterator.next();
				aEcstatement.setEcPeriodId(ecPeriodID);
				aEcstatement.setRepLoginId((Integer) aObj[3]);
				aEcstatement.setOpeningBalance(new BigDecimal(0.0000));
				aEcstatement.setJobCommissions(new BigDecimal(0.0000));
				aEcstatement.setOtherCommissions(new BigDecimal(0.0000));
				aEcstatement.setOverrides(new BigDecimal(0.0000));
				if(aObj[6]==null){
					aObj[6]=BigDecimal.ZERO;
				}
				if(aObj[7]==null){
					aObj[7]=BigDecimal.ZERO;
				}
				if(aObj[8]==null){
					aObj[8]=BigDecimal.ZERO;
				}
				if(aObj[9]==null){
					aObj[9]=BigDecimal.ZERO;
				}
				aEcstatement.setAdjustments(((BigDecimal) aObj[6]).add((BigDecimal) aObj[7]).add((BigDecimal) aObj[8]).add((BigDecimal) aObj[9]));
				aEcstatement.setPayment(new BigDecimal(0.0000));
				aEcstatement.setRepDeduct1((BigDecimal) aObj[6]);
				aEcstatement.setRepDeduct2((BigDecimal) aObj[7]);
				aEcstatement.setRepDeduct3((BigDecimal) aObj[8]);
				aEcstatement.setRepDeduct4((BigDecimal) aObj[9]);
				// aEcstatement.setComment(comment);
				aEcstatement.setCustom1(new BigDecimal(0.0000));
				aEcstatement.setCustom2(new BigDecimal(0.0000));
				aEcstatement.setCustom3(new BigDecimal(0.0000));
				aEcstatement.setCustom4(new BigDecimal(0.0000));
				aEcstatement.setCustom5(new BigDecimal(0.0000));
				aEcstatement.setCustom6(new BigDecimal(0.0000));
				aEcstatement.setCustom7(new BigDecimal(0.0000));
				aEcstatement.setCustom8(new BigDecimal(0.0000));
				aEcstatement.setCustom9(new BigDecimal(0.0000));
				aEcstatement.setCustom10(new BigDecimal(0.0000));
				// aEcstatement.setFlags(flags);
				Transaction periodInsertTransaction;
				periodInsertTransaction = aSession
						.beginTransaction();
				periodInsertTransaction.begin();
				aInStatus = (Integer) aSession.save(aEcstatement);
				periodInsertTransaction.commit();
				aQueryList.add(aEcstatement);
				updateEcStatement(ecPeriodID,aInStatus);

			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;aWerehouseTypeSelectQry=null;
		}
		return aInStatus;
	}

	@Override
	public int updateEcStatement(Integer newPeriodID,Integer ecStatementID) throws CompanyException {
		Session aSession = null;String aVendorSelectQry = null;Query qry = null;
		try {
			aSession = itsSessionFactory.openSession();
			aVendorSelectQry = "SELECT ecPeriodID FROM ecPeriod ORDER BY PeriodEndingDate DESC LIMIT 1 OFFSET 1";
			Integer oldPeriodID = null;
			List<?> aList = aSession.createSQLQuery(aVendorSelectQry).list();
			if(aList.size()>0){
			oldPeriodID = (Integer) aList.get(0);
			}
			
			if(oldPeriodID!=null && oldPeriodID!=0){
			qry = aSession
					.createSQLQuery("UPDATE ecStatement AS es1 INNER JOIN ecStatement AS es2 ON es1.RepLoginID = es2.RepLoginID "
							+ "SET es1.OpeningBalance = es2.OpeningBalance+es2.Overrides+es2.JobCommissions+es2.OtherCommissions+es2.Adjustments-es2.Payment, "
							+ "es1.Adjustments = es2.RepDeduct1+es2.RepDeduct2+es2.RepDeduct3+es2.RepDeduct4 WHERE es1.ecPeriodID = "
							+ newPeriodID
							+ " AND es2.ecPeriodID="
							+ oldPeriodID +" AND es1.ecStatementID="+ecStatementID);
			qry.executeUpdate();
			}else{
				qry = aSession
						.createSQLQuery("UPDATE ecStatement SET Adjustments = RepDeduct1+RepDeduct2+RepDeduct3+RepDeduct4 WHERE ecStatementID="+ecStatementID);
				qry.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			throw new CompanyException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorSelectQry =null;qry = null;
		}
		return 1;
	}

	@Override
	public Ecstatement getEcStatement(Integer aEcperiod, Integer rxmasterid)
			throws EmployeeException {
		Session aSession = null;Query query = null;
		List<Ecstatement> aQueryList = new ArrayList<Ecstatement>();
		Ecstatement aEcstatement = new Ecstatement();
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("FROM Ecstatement WHERE ecPeriodID="
							+ aEcperiod + " and RepLoginID=" + rxmasterid);
			aQueryList = query.list();
			if (aQueryList.size() > 0) {
				aEcstatement = aQueryList.get(0);
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting the Fried Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aEcstatement;
	}

	@Override
	public Sysvariable getSysVariableDetails(String sysVariableName)
			throws EmployeeException {
		Session aSession = null;Query query =null;
		List<Sysvariable> aQueryList = new ArrayList<Sysvariable>();
		Sysvariable aSysvariable = new Sysvariable();
		try {
			Integer sysVariableId = InventoryConstant
					.getConstantSysvariableId(sysVariableName);
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("FROM Sysvariable WHERE sysVariableID="
							+ sysVariableId);
			aQueryList = query.list();
			if (aQueryList.size() > 0) {
				aSysvariable = aQueryList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			excep.printStackTrace();
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting sysVariable : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return aSysvariable;

	}

	@Override
	public List<Jomaster> getJobDetails(String startPeriodDate,
			String endPeriodDate) throws EmployeeException {
		Session aSession = null;
		List<Jomaster> aQueryList = null;
		Sysvariable aSysvariable = itsEmployeeService
				.getSysVariableDetails("CommissionSpecialCode");
		String aQueryString = null;Query aQuery = null;
		//Rep Functionality
		if (aSysvariable.getValueString().contains("/CJ")) {
			if (startPeriodDate != "" && endPeriodDate != "")
				aQueryString = "FROM  Jomaster WHERE JobStatus=4 AND ClosedDate>= "
						+ startPeriodDate + " AND ClosedDate<=" + endPeriodDate;
		} else {
			if (startPeriodDate != null) {
				aQueryString = "FROM  Jomaster WHERE  BookedDate <= "
						+ endPeriodDate
						+ " AND (JobStatus=3 OR JobStatus=4) AND ClosedDate>="
						+ startPeriodDate;
			} else {
				aQueryString = "FROM  Jomaster WHERE BookedDate <= "
						+ endPeriodDate + " AND (JobStatus=3 OR JobStatus=4)";
			}
		}

		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery(aQueryString);
			aQueryList = aQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			EmployeeException aEmployeeException = new EmployeeException(
					e.getMessage(), e);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString=null;aQuery = null;
		}
		return aQueryList;
	}

	@Override
	public Ecjobs getEcJobsDetails(Integer joMasterID) throws EmployeeException {
		Session aSession = null;
		Ecjobs aEcjobs = null;Query ecJobQuery =null;
		List<Ecjobs> aEcJobsList = new ArrayList<Ecjobs>();
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession
					.createQuery(" FROM Ecjobs WHERE joMasterID=" + joMasterID
							+ " ORDER BY ecJobsID DESC LIMIT 1");
			aEcJobsList = ecJobQuery.list();
			if (aEcJobsList.size() > 0) {
				aEcjobs = aEcJobsList.get(0);
			} else {
				aEcjobs = new Ecjobs();
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting sysVariable : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			ecJobQuery =null;
		}
		return aEcjobs;

	}
		
	public JoInvoiceCost getAddedCost(Integer joReleaseDetailID,String startPeriodDate,
			String endPeriodDate) throws EmployeeException {
		String aQueryString = null;
		JoInvoiceCost aJoInvoiceCost = new JoInvoiceCost();
		aQueryString = "SELECT CAST(SUM(IFNULL(cost,0.0000)) AS DECIMAL(19,4)) AS Cost,MAX(enteredDate)  FROM joInvoiceCost WHERE joReleaseDetailID="
		+ joReleaseDetailID+" AND enteredDate BETWEEN '"+startPeriodDate+"' AND '"+endPeriodDate+"' GROUP BY DATE_FORMAT(enteredDate, '%Y-%m')";
		Session aSession = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aJoInvoiceCost.setCost((BigDecimal) aObj[0]);
				aJoInvoiceCost.setEnteredDate((Date) aObj[1]);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;aQuery =null;
		}
		return aJoInvoiceCost;
	}

	public Integer updateAddedCostPaid(Integer joReleaseDetailID,String startPeriodDate,
			String endPeriodDate,Integer ecInvoicePeriodID) throws EmployeeException {
		String aQueryString = null;
		Integer updateStatus = 0;
		Transaction aTransaction;
		aQueryString = "UPDATE joInvoiceCost SET ecInvoicePeriodID ="+ecInvoicePeriodID+" WHERE joReleaseDetailID="
		+ joReleaseDetailID+" AND enteredDate <= '"+endPeriodDate+" 23:59:59' AND ecInvoicePeriodID IS NULL";
		Session aSession = null;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			aTransaction = aSession.beginTransaction();
			aSession.createSQLQuery(aQueryString).executeUpdate();
			aTransaction.commit();
			updateStatus=1;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;
			aQuery =null;
		}
		return updateStatus;
	}
	
	@Override
	public Emmaster getEmmasterDetails(Integer LoginUserID)
			throws EmployeeException {
		Session aSession = null;
		Emmaster aEmmaster = null;
		Query ecJobQuery = null;
		List<Emmaster> aEmmasterList = new ArrayList<Emmaster>();
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession
					.createQuery(" FROM Emmaster WHERE UserLoginID="
							+ LoginUserID + " AND GetsCommission=1");
			aEmmasterList = ecJobQuery.list();
			if (aEmmasterList.size() > 0) {
				aEmmaster = aEmmasterList.get(0);
			}else{
				aEmmaster = new Emmaster();
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting sysVariable : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			ecJobQuery = null;
		}
		return aEmmaster;

	}
	
	
	@Override
	public Integer calCulateJobReleaseCommissions(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {

		Sysvariable aSysvariable = itsEmployeeService.getSysVariableDetails("CommissionSpecialCode");
		String aQueryString = null;
		Integer ecJobiD = 0;
		Integer endPeriodID = ecPeriodList.get(0).getEcPeriodId();
		//CommissionCalculateBean comCalcBean = new CommissionCalculateBean();
		ArrayList<CommissionCalculateBean> comCalcListPayment = new ArrayList<CommissionCalculateBean>();
		ArrayList<CommissionCalculateBean> comCalcListInvoiced = new ArrayList<CommissionCalculateBean>();
		// List<Ecperiod> aEcperiodList = new ArrayList<Ecperiod>();
		Transaction aTransaction;
		Transaction bTransaction;
		Ecjobs theEcjobs = null;
		//Rep Functionality
		if (aSysvariable.getValueString() != null
				&& aSysvariable.getValueString().contains("/CJ")) {
			if (startPeriodDate != "" && endPeriodDate != "")
				aQueryString = "SELECT joMasterID,cuAssignmentID0 FROM  joMaster WHERE (JobStatus=4 AND ClosedDate>= '"
						+ startPeriodDate
						+ "' ) AND ClosedDate<='"
						+ endPeriodDate + "'";
		} else {
			if (endPeriodDate != null) {
				aQueryString = "SELECT  joMasterID,cuAssignmentID0 FROM  joMaster WHERE  BookedDate <= '"
						+ endPeriodDate
						+ "' AND BookedDate >= '"+ startPeriodDate +"' AND JobStatus=3 OR (JobStatus=4 AND ClosedDate>='"
						+ startPeriodDate + "')";
			} else {
				aQueryString = "SELECT  joMasterID,cuAssignmentID0 FROM  joMaster WHERE BookedDate <= '"
						+ endPeriodDate + "' AND JobStatus=3 OR JobStatus=4";
			}
		}
		Session aSession = null;
		ArrayList<Jomaster> aQueryList = new ArrayList<Jomaster>();
		BigDecimal profitPaidOnTodate = new BigDecimal(0.0000);
		Emmaster aEmmaster = null;
		Emmaster bEmmaster = null;
		Emmaster aEmmasterObj = null;
		Ecjobs aEcjobs = null;Query aQuery =null;
		Ecstatement aEcstatement = new Ecstatement();
		List<Ecsplitjob> aEcsplitjobList = new ArrayList<Ecsplitjob>();
		List<Ecsplitjob> aEcsplitjobList2 = new ArrayList<Ecsplitjob>();
		try {
			Jomaster aJomaster = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {

				aJomaster = new Jomaster();
				Object[] aObj = (Object[]) aIterator.next();
				aJomaster.setJoMasterId((Integer) aObj[0]);
				aJomaster.setCuAssignmentId0((Integer) aObj[1]);
				aQueryList.add(aJomaster);
			}
			for (int i = 0; i < aQueryList.size(); i++) {
				aEcjobs = new Ecjobs();
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				BigDecimal localComRate = new BigDecimal(0.00);
				BigDecimal localprofitToDate = new BigDecimal(0.00);
				aEcjobs = getEcJobsDetails(aQueryList.get(i).getJoMasterId());
				aEmmasterObj = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
				if( aEmmasterObj!=null && (aEmmasterObj.getCommAfterPayment()!=null && aEmmasterObj.getCommAfterPayment()==1)){
					comCalcListPayment = calculateJobCommission(aQueryList.get(i).getJoMasterId(), endPeriodDate,startPeriodDate,"payment");
				}
				else{
					comCalcListInvoiced = calculateJobCommission(aQueryList.get(i).getJoMasterId(), endPeriodDate,startPeriodDate,"invoiced");
				}
				if(comCalcListInvoiced.size()>0){

					for(int j=0;j<comCalcListInvoiced.size();j++){
						aEcsplitjobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterId());
						aEcsplitjobList2 = getEcSplitJobEmMasterDetails(comCalcListInvoiced.get(j).getJoReleaseID(),aQueryList.get(i).getJoMasterId(),"release");
						if(aEcsplitjobList2.size()>0){
							for(int k=0;k<aEcsplitjobList2.size();k++){
							theEcjobs = new Ecjobs();
							aEcstatement = getSaveEcStatement(endPeriodID, aEcsplitjobList2.get(k).getRxMasterId());
							if (aEcstatement != null) {
								aEmmaster = getEmmasterDetails(aEcsplitjobList2.get(k).getRxMasterId());
								if(aEmmaster!=null && (aEmmaster.getCommAfterPayment()!=null && aEmmaster.getCommAfterPayment()==1)){
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								theEcjobs.setJoMasterId(aQueryList.get(i).getJoMasterId());
								
								localprofitToDate = comCalcListInvoiced.get(j).getTotalSales().subtract(comCalcListInvoiced.get(j).getTotalCosts());
								
								theEcjobs.setProfitToDate(localprofitToDate);
								theEcjobs.setEcStatementId(aEcstatement.getEcStatementId());
								if (aEmmaster.getProfitBonus() != null
										&& aEmmaster.getProfitBonus() == 1) {
									theEcjobs.setCommissionRate(new BigDecimal(0));
									localComRate = new BigDecimal(0);
								} else {
									//theEcjobs.setCommissionRate(aEmmaster.getCommissionJobProfit());
									theEcjobs.setCommissionRate(aEcsplitjobList2.get(k).getAllocated());
									localComRate = aEmmaster.getCommissionJobProfit();
								}
								BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
								//ecProfitPaidonTodate =localprofitToDate;
								
								if(localComRate ==null){localComRate = new BigDecimal("0.0000");}
								if(localprofitToDate==null){localprofitToDate= new BigDecimal("0.0000");}
								
								theEcjobs.setCommissionToDate(localprofitToDate
										.multiply(localComRate).multiply(new BigDecimal(0.01).multiply((aEcsplitjobList2.get(k).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList2.get(k).getAllocated()).multiply(new BigDecimal(0.01)))));
								theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
								theEcjobs.setPaidToDate(new BigDecimal(0.00));
								BigDecimal dueAmount = localprofitToDate
										.subtract(ecProfitPaidonTodate)
										.multiply(localComRate)
										.multiply(new BigDecimal(0.01).multiply((aEcsplitjobList2.get(k).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList2.get(k).getAllocated()).multiply(new BigDecimal(0.01))));
								theEcjobs.setAmountDue(dueAmount);
								if (dueAmount.compareTo(new BigDecimal(0)) < 0
										&& (aSysvariable.getValueString() != null && aSysvariable
												.getValueString().contains("/JLF"))) {
									theEcjobs.setAmountDue(localprofitToDate
											.subtract(ecProfitPaidonTodate));
									theEcjobs.setCommissionRate(new BigDecimal(100));
								}

								ecJobiD = (Integer) aSession.save(theEcjobs);
								bTransaction.commit();
							}
							}
							}
							
						}
						else if(aEcsplitjobList.size()>0){

							for(int l=0;l<aEcsplitjobList.size();l++){
							theEcjobs = new Ecjobs();
							aEcstatement = getSaveEcStatement(endPeriodID, aEcsplitjobList.get(l).getRxMasterId());
							if (aEcstatement != null) {
								bEmmaster = getEmmasterDetails(aEcsplitjobList.get(l).getRxMasterId());
								if(bEmmaster !=null && (bEmmaster.getCommAfterPayment()!=null && bEmmaster.getCommAfterPayment()==1)){
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								theEcjobs.setJoMasterId(aQueryList.get(i).getJoMasterId());
								
								localprofitToDate = comCalcListInvoiced.get(j).getTotalSales().subtract(comCalcListInvoiced.get(j).getTotalCosts());
								
								theEcjobs.setProfitToDate(localprofitToDate);
								theEcjobs.setEcStatementId(aEcstatement.getEcStatementId());
								if (bEmmaster.getProfitBonus() != null
										&& bEmmaster.getProfitBonus() == 1) {
									theEcjobs.setCommissionRate(new BigDecimal(0));
									localComRate = new BigDecimal(0);
								} else {
									//theEcjobs.setCommissionRate(bEmmaster.getCommissionJobProfit());
									theEcjobs.setCommissionRate(aEcsplitjobList.get(l).getAllocated());
									localComRate = bEmmaster.getCommissionJobProfit();
								}
								BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
								//ecProfitPaidonTodate =localprofitToDate;

								if(localComRate==null){localComRate = new BigDecimal("0.0000");}
								if(localprofitToDate==null){
									localprofitToDate = new BigDecimal("0.0000");
								}
								theEcjobs.setCommissionToDate(localprofitToDate
										.multiply(localComRate).multiply(new BigDecimal(0.01).multiply((aEcsplitjobList.get(l).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList.get(l).getAllocated()).multiply(new BigDecimal(0.01)))));
								theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
								theEcjobs.setPaidToDate(new BigDecimal(0.00));
								BigDecimal dueAmount = localprofitToDate
										.subtract(ecProfitPaidonTodate)
										.multiply(localComRate)
										.multiply(new BigDecimal(0.01).multiply((aEcsplitjobList.get(l).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList.get(l).getAllocated()).multiply(new BigDecimal(0.01))));
								theEcjobs.setAmountDue(dueAmount);
								if (dueAmount.compareTo(new BigDecimal(0)) < 0
										&& (aSysvariable.getValueString() != null && aSysvariable
												.getValueString().contains("/JLF"))) {
									theEcjobs.setAmountDue(localprofitToDate
											.subtract(ecProfitPaidonTodate));
									theEcjobs.setCommissionRate(new BigDecimal(100));
								}

								ecJobiD = (Integer) aSession.save(theEcjobs);
								bTransaction.commit();
							}
							}
							}
						}
						else{
							theEcjobs = new Ecjobs();
							aEcstatement = getSaveEcStatement(endPeriodID, aQueryList.get(i).getCuAssignmentId0());
							if (aEcstatement != null) {
								aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
								if(aEmmaster.getCommAfterPayment()!=null && aEmmaster.getCommAfterPayment()==1){
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								theEcjobs.setJoMasterId(aQueryList.get(i).getJoMasterId());
								
								localprofitToDate = (comCalcListInvoiced.get(j).getTotalSales()==null? new BigDecimal("0.0000"):comCalcListInvoiced.get(j).getTotalSales()).subtract(comCalcListInvoiced.get(j).getTotalCosts()==null?new BigDecimal("0.0000"):comCalcListInvoiced.get(j).getTotalCosts());
								
								theEcjobs.setProfitToDate(localprofitToDate);
								theEcjobs.setEcStatementId(aEcstatement.getEcStatementId());
								if (aEmmaster.getProfitBonus() != null
										&& aEmmaster.getProfitBonus() == 1) {
									theEcjobs.setCommissionRate(new BigDecimal(0));
									localComRate = new BigDecimal(0);
								} else {
									theEcjobs.setCommissionRate(aEmmaster
											.getCommissionJobProfit());
									
									localComRate = aEmmaster.getCommissionJobProfit();
								}
								BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
								//ecProfitPaidonTodate =localprofitToDate;
								if(localComRate==null){localComRate = new BigDecimal("0.0000");}
								if(localprofitToDate==null){
									localprofitToDate = new BigDecimal("0.0000");
								}
								theEcjobs.setCommissionToDate(localprofitToDate
										.multiply(localComRate).multiply(new BigDecimal(0.01)));
								theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
								theEcjobs.setPaidToDate(new BigDecimal(0.00));
								BigDecimal dueAmount = localprofitToDate
										.subtract(ecProfitPaidonTodate)
										.multiply(localComRate)
										.multiply(new BigDecimal(0.01));

								if (dueAmount.compareTo(new BigDecimal(0)) < 0
										&& (aSysvariable.getValueString() != null && aSysvariable
												.getValueString().contains("/JLF"))) {
									theEcjobs.setAmountDue(localprofitToDate
											.subtract(ecProfitPaidonTodate));
									theEcjobs.setCommissionRate(new BigDecimal(100));
								}
								theEcjobs.setAmountDue(dueAmount);

								ecJobiD = (Integer) aSession.save(theEcjobs);
								bTransaction.commit();
							}
								}
							}
						}
					}
					
				}
				
				if(comCalcListPayment.size()>0){
				for(int j=0;j<comCalcListPayment.size();j++){
					aEcsplitjobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterId()==null?0:aQueryList.get(i).getJoMasterId());
					aEcsplitjobList2 = getEcSplitJobEmMasterDetails(comCalcListPayment.get(j).getJoReleaseID()==null?0:comCalcListPayment.get(j).getJoReleaseID(),aQueryList.get(i).getJoMasterId()==null?0:aQueryList.get(i).getJoMasterId(),"release");
					if(aEcsplitjobList2.size()>0){
						for(int k=0;k<aEcsplitjobList2.size();k++){
						theEcjobs = new Ecjobs();
						aEcstatement = getSaveEcStatement(endPeriodID, aEcsplitjobList2.get(k).getRxMasterId()==null?0:aEcsplitjobList2.get(k).getRxMasterId());
						if (aEcstatement != null) {
							aEmmaster = getEmmasterDetails(aEcsplitjobList2.get(k).getRxMasterId()==null?0:aEcsplitjobList2.get(k).getRxMasterId());
							if(aEmmaster!=null && (aEmmaster.getCommAfterPayment()!=null && aEmmaster.getCommAfterPayment()==1)){
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							theEcjobs.setJoMasterId(aQueryList.get(i).getJoMasterId());
							
							localprofitToDate = (comCalcListPayment.get(j).getTotalSales()==null?new BigDecimal("0.0000"):comCalcListPayment.get(j).getTotalSales()).subtract(comCalcListPayment.get(j).getTotalCosts()==null?new BigDecimal("0.0000"):comCalcListPayment.get(j).getTotalCosts());
							
							theEcjobs.setProfitToDate(localprofitToDate);
							theEcjobs.setEcStatementId(aEcstatement.getEcStatementId());
							if (aEmmaster.getProfitBonus() != null
									&& aEmmaster.getProfitBonus() == 1) {
								theEcjobs.setCommissionRate(new BigDecimal(0));
								localComRate = new BigDecimal(0);
							} else {
								theEcjobs.setCommissionRate(aEcsplitjobList2.get(k).getAllocated());
								localComRate = aEmmaster.getCommissionJobProfit();
							}
							BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
							//ecProfitPaidonTodate =localprofitToDate;

							if(localComRate==null){localComRate = new BigDecimal("0.0000");}
							if(localprofitToDate==null){
								localprofitToDate = new BigDecimal("0.0000");
							}
							
							theEcjobs.setCommissionToDate(localprofitToDate
									.multiply(localComRate).multiply(new BigDecimal(0.01).multiply((aEcsplitjobList2.get(k).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList2.get(k).getAllocated()).multiply(new BigDecimal(0.01)))));
							theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
							theEcjobs.setPaidToDate(new BigDecimal(0.00));
							BigDecimal dueAmount = localprofitToDate
									.subtract(ecProfitPaidonTodate)
									.multiply(localComRate)
									.multiply(new BigDecimal(0.01).multiply((aEcsplitjobList2.get(k).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList2.get(k).getAllocated()).multiply(new BigDecimal(0.01))));
							theEcjobs.setAmountDue(dueAmount);
							if (dueAmount.compareTo(new BigDecimal(0)) < 0
									&& (aSysvariable.getValueString() != null && aSysvariable
											.getValueString().contains("/JLF"))) {
								theEcjobs.setAmountDue(localprofitToDate
										.subtract(ecProfitPaidonTodate));
								theEcjobs.setCommissionRate(new BigDecimal(100));
							}

							ecJobiD = (Integer) aSession.save(theEcjobs);
							bTransaction.commit();
						}
						}
						}
						
					}
					else if(aEcsplitjobList.size()>0){

						for(int l=0;l<aEcsplitjobList.size();l++){
						theEcjobs = new Ecjobs();
						aEcstatement = getSaveEcStatement(endPeriodID, aEcsplitjobList.get(l).getRxMasterId()==null?0:aEcsplitjobList.get(l).getRxMasterId());
						if (aEcstatement != null) {
							bEmmaster = getEmmasterDetails(aEcsplitjobList.get(l).getRxMasterId()==null?0:aEcsplitjobList.get(l).getRxMasterId());
							if(bEmmaster != null && (bEmmaster.getCommAfterPayment()!=null && bEmmaster.getCommAfterPayment()==1)){
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							theEcjobs.setJoMasterId(aQueryList.get(i).getJoMasterId()==null?0:aQueryList.get(i).getJoMasterId());
							
							localprofitToDate = (comCalcListPayment.get(j).getTotalSales()==null?new BigDecimal("0.0000"):comCalcListPayment.get(j).getTotalSales()).subtract(comCalcListPayment.get(j).getTotalCosts()==null?new BigDecimal("0.0000"):comCalcListPayment.get(j).getTotalCosts());
							
							theEcjobs.setProfitToDate(localprofitToDate);
							theEcjobs.setEcStatementId(aEcstatement.getEcStatementId());
							if (bEmmaster.getProfitBonus() != null
									&& bEmmaster.getProfitBonus() == 1) {
								theEcjobs.setCommissionRate(new BigDecimal(0));
								localComRate = new BigDecimal(0);
							} else {
								theEcjobs.setCommissionRate(aEcsplitjobList.get(l).getAllocated());
								localComRate = bEmmaster.getCommissionJobProfit();
							}
							BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
							//ecProfitPaidonTodate =localprofitToDate;

							if(localComRate==null){localComRate = new BigDecimal("0.0000");}
							if(localprofitToDate==null){
								localprofitToDate = new BigDecimal("0.0000");
							}
							
							theEcjobs.setCommissionToDate(localprofitToDate
									.multiply(localComRate).multiply(new BigDecimal(0.01).multiply((aEcsplitjobList.get(l).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList.get(l).getAllocated()).multiply(new BigDecimal(0.01)))));
							theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
							theEcjobs.setPaidToDate(new BigDecimal(0.00));
							BigDecimal dueAmount = localprofitToDate
									.subtract(ecProfitPaidonTodate)
									.multiply(localComRate)
									.multiply(new BigDecimal(0.01).multiply((aEcsplitjobList.get(l).getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjobList.get(l).getAllocated()).multiply(new BigDecimal(0.01))));
							theEcjobs.setAmountDue(dueAmount);
							if (dueAmount.compareTo(new BigDecimal(0)) < 0
									&& (aSysvariable.getValueString() != null && aSysvariable
											.getValueString().contains("/JLF"))) {
								theEcjobs.setAmountDue(localprofitToDate
										.subtract(ecProfitPaidonTodate));
								theEcjobs.setCommissionRate(new BigDecimal(100));
							}

							ecJobiD = (Integer) aSession.save(theEcjobs);
							bTransaction.commit();
						}
						}
						}
					}
					else{
						theEcjobs = new Ecjobs();
						aEcstatement = getSaveEcStatement(endPeriodID, aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
						if (aEcstatement != null) {
							aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null && (aEmmaster.getCommAfterPayment()!=null && aEmmaster.getCommAfterPayment()==1)){
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							theEcjobs.setJoMasterId(aQueryList.get(i).getJoMasterId());
							
							localprofitToDate = (comCalcListPayment.get(j).getTotalSales()==null? new BigDecimal("0.0000"):comCalcListPayment.get(j).getTotalSales()).subtract(comCalcListPayment.get(j).getTotalCosts());
							
							theEcjobs.setProfitToDate(localprofitToDate);
							theEcjobs.setEcStatementId(aEcstatement.getEcStatementId());
							if (aEmmaster.getProfitBonus() != null
									&& aEmmaster.getProfitBonus() == 1) {
								theEcjobs.setCommissionRate(new BigDecimal(0));
								localComRate = new BigDecimal(0);
							} else {
								theEcjobs.setCommissionRate(aEmmaster
										.getCommissionJobProfit());
								localComRate = aEmmaster.getCommissionJobProfit();
							}
							BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
							//ecProfitPaidonTodate =localprofitToDate;

							if(localComRate==null){localComRate = new BigDecimal("0.0000");}
							if(localprofitToDate==null){
								localprofitToDate = new BigDecimal("0.0000");
							}
							
							theEcjobs.setCommissionToDate(localprofitToDate
									.multiply(localComRate).multiply(new BigDecimal(0.01)));
							theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
							theEcjobs.setPaidToDate(new BigDecimal(0.00));
							BigDecimal dueAmount = localprofitToDate
									.subtract(ecProfitPaidonTodate)
									.multiply(localComRate)
									.multiply(new BigDecimal(0.01));

							if (dueAmount.compareTo(new BigDecimal(0)) < 0
									&& (aSysvariable.getValueString() != null && aSysvariable
											.getValueString().contains("/JLF"))) {
								theEcjobs.setAmountDue(localprofitToDate
										.subtract(ecProfitPaidonTodate));
								theEcjobs.setCommissionRate(new BigDecimal(100));
							}
							theEcjobs.setAmountDue(dueAmount);

							ecJobiD = (Integer) aSession.save(theEcjobs);
							bTransaction.commit();
						}
						}
					}
				}
				}
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString=null;
			aQuery =null;
		}
		return ecJobiD;
	}

	@Override
	public Integer calCulateJobCommissions(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {

		Sysvariable aSysvariable = itsEmployeeService
				.getSysVariableDetails("CommissionSpecialCode");
		String aQueryString = null;
		Integer ecJobiD = 0;
		Integer endPeriodID = ecPeriodList.get(0).getEcPeriodId();
		CommissionCalculateBean comCalcBean = new CommissionCalculateBean();
		// List<Ecperiod> aEcperiodList = new ArrayList<Ecperiod>();
		Transaction aTransaction;
		Transaction bTransaction;
		Ecjobs theEcjobs = null;
		// Rep Functionality
		 if (aSysvariable.getValueString() != null
				&& aSysvariable.getValueString().contains("/CJ")) {
			if (startPeriodDate != "" && endPeriodDate != "")
				aQueryString = "SELECT joMasterID,cuAssignmentID0 FROM  joMaster WHERE JobStatus=4 AND ClosedDate>= '"
						+ startPeriodDate
						+ "'  AND ClosedDate<='"
						+ endPeriodDate + "'";
		} else {
			if (endPeriodDate != null) {
				aQueryString = "SELECT  joMasterID,cuAssignmentID0 FROM  joMaster WHERE  BookedDate <= '"
						+ endPeriodDate
						+ "' AND BookedDate >= '"+startPeriodDate +"' AND JobStatus=3 OR (JobStatus=4 AND ClosedDate>='"
						+ startPeriodDate + "')";
			} else {
				aQueryString = "SELECT  joMasterID,cuAssignmentID0 FROM  joMaster WHERE BookedDate <= '"
						+ endPeriodDate + "' AND (JobStatus=3 OR JobStatus=4)";
			}
		
		}
		Session aSession = null;
		ArrayList<Jomaster> aQueryList = new ArrayList<Jomaster>();
		BigDecimal profitPaidOnTodate = new BigDecimal(0.0000);
		Emmaster aEmmaster = null;
		Ecjobs aEcjobs = null;Query aQuery = null;
		Ecstatement aEcstatement = new Ecstatement();
		List<Ecsplitjob> aEcsplitjobList = new ArrayList<Ecsplitjob>();
		try {
			Jomaster aJomaster = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {

				aJomaster = new Jomaster();
				Object[] aObj = (Object[]) aIterator.next();
				aJomaster.setJoMasterId((Integer) aObj[0]);
				aJomaster.setCuAssignmentId0((Integer) aObj[1]);
				aQueryList.add(aJomaster);
			}
			for (int i = 0; i < aQueryList.size(); i++) {
				aEcjobs = new Ecjobs();
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				BigDecimal localComRate = new BigDecimal(0.00);
				BigDecimal localprofitToDate = new BigDecimal(0.00);
				aEcjobs = getEcJobsDetails(aQueryList.get(i).getJoMasterId()==null?0:aQueryList.get(i).getJoMasterId());
				comCalcBean = calculateJobFinancials(aQueryList.get(i).getJoMasterId(), endPeriodDate,startPeriodDate);
				itsLogger.info("CalcBean: "+comCalcBean.getTotalCosts()+ "-"+comCalcBean.getTotalSales());
				aEcsplitjobList = getEcSplitJobDetails(aQueryList.get(i)
						.getJoMasterId());
				profitPaidOnTodate = aEcjobs.getProfitToDate()==null?new BigDecimal(0.000):aEcjobs.getProfitToDate();
				if (aEcsplitjobList.size() > 0) {
					aEcstatement = getSaveEcStatement(endPeriodID,
							aEcsplitjobList.get(0).getRxMasterId()==null?0:aEcsplitjobList.get(0).getRxMasterId());

					if (aEcstatement != null) {
						aEmmaster = getEmmasterDetails(aEcsplitjobList.get(0).getRxMasterId()==null?0:aEcsplitjobList.get(0).getRxMasterId());
						theEcjobs = new Ecjobs();
						theEcjobs.setJoMasterId(aQueryList.get(i)
								.getJoMasterId());
						theEcjobs.setEcStatementId(aEcstatement
								.getEcStatementId());
						if (comCalcBean != null) {
							localprofitToDate = comCalcBean.getTotalSales()
									.subtract(comCalcBean.getTotalCosts());
						}

						theEcjobs.setProfitToDate(localprofitToDate);
						if(aEmmaster!=null){
						if (aEmmaster.getProfitBonus()!=null && aEmmaster.getProfitBonus() > 0) {
							theEcjobs.setCommissionRate(new BigDecimal(0.00));
							localComRate = new BigDecimal(0.00);
						} else {
							if (aEmmaster.getCommissionJobProfit() != null)
								localComRate = aEmmaster.getCommissionJobProfit();
								theEcjobs.setCommissionRate(aEmmaster.getCommissionJobProfit());
						}
						}
						if(localComRate==null){localComRate = new BigDecimal("0.0000");}
						if(localprofitToDate==null){
							localprofitToDate = new BigDecimal("0.0000");
						}
						theEcjobs.setCommissionToDate(localprofitToDate
								.multiply(localComRate).multiply(
										new BigDecimal(0.01)));
						theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
						theEcjobs.setPaidToDate(new BigDecimal(0.00));
						if (profitPaidOnTodate == null){
							profitPaidOnTodate = new BigDecimal(0.0000);
						}
						
						if (comCalcBean != null) {
							theEcjobs.setAmountDue((localprofitToDate
									.subtract(profitPaidOnTodate))
									.multiply(localComRate)
									.multiply(new BigDecimal(0.01))
									.multiply(new BigDecimal(0.01)
													.multiply(aEcsplitjobList
															.get(0)
															.getAllocated())));
							theEcjobs.setCommissionRate(aEcsplitjobList.get(0).getAllocated());
						} else {
							theEcjobs.setAmountDue(new BigDecimal(0.0000));
						}
						//Rep Functionality
						if (aSysvariable.getValueString() != null
								&& aSysvariable.getValueString().contains(
										"/JLF")) {
							theEcjobs.setAmountDue(localprofitToDate
									.subtract(profitPaidOnTodate));
							theEcjobs.setCommissionRate(new BigDecimal(100.00));
						}
					}
					ecJobiD = (Integer) aSession.save(theEcjobs);
					bTransaction.commit();

				} else {
					theEcjobs = new Ecjobs();
					aEcstatement = getSaveEcStatement(endPeriodID, aQueryList
							.get(i).getCuAssignmentId0());
					if (aEcstatement != null) {
						aEmmaster = getEmmasterDetails(aQueryList.get(i)
								.getCuAssignmentId0());
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						// comCalcBean =
						// calculateJobFinancials(aQueryList.get(i).getJoMasterId(),
						// endPeriodDate);
						theEcjobs.setJoMasterId(aQueryList.get(i)
								.getJoMasterId());
						if (comCalcBean != null) {
							localprofitToDate = (comCalcBean.getTotalSales()==null?new BigDecimal("0.0000"):comCalcBean.getTotalSales())
									.subtract(comCalcBean.getTotalCosts()==null?new BigDecimal("0.0000"):comCalcBean.getTotalCosts());
						}
						if(localprofitToDate==null){
							localprofitToDate = new BigDecimal("0.0000");
						}
						theEcjobs.setProfitToDate(localprofitToDate);
						theEcjobs.setEcStatementId(aEcstatement
								.getEcStatementId());
						if(aEmmaster!=null){
						if (aEmmaster.getProfitBonus() != null
								&& aEmmaster.getProfitBonus() == 1) {
							theEcjobs.setCommissionRate(new BigDecimal(0));
							localComRate = new BigDecimal(0);
						} else {
							theEcjobs.setCommissionRate(aEmmaster
									.getCommissionJobProfit());
							localComRate = aEmmaster.getCommissionJobProfit();
						}
						}
						if(localComRate==null){
							localComRate = new BigDecimal("0.0000");
						}
						BigDecimal ecProfitPaidonTodate = new BigDecimal(0.000);
						if (getEcJobsDetails(aQueryList.get(i).getJoMasterId()) != null) {
							ecProfitPaidonTodate = getEcJobsDetails(
									aQueryList.get(i).getJoMasterId())
									.getProfitToDate() == null ? new BigDecimal(
									0.0000) : getEcJobsDetails(
									aQueryList.get(i).getJoMasterId())
									.getProfitToDate();
						}
						
						if(ecProfitPaidonTodate==null){
							ecProfitPaidonTodate = new BigDecimal("0.0000");
						}
						
						theEcjobs.setCommissionToDate(localprofitToDate
								.multiply(localComRate).multiply(
										new BigDecimal(0.01)));
						theEcjobs.setProfitPaidOnToDate(profitPaidOnTodate);
						theEcjobs.setPaidToDate(new BigDecimal(0.00));
						BigDecimal dueAmount = localprofitToDate
								.subtract(ecProfitPaidonTodate)
								.multiply(localComRate)
								.multiply(new BigDecimal(0.01));

						if (dueAmount.compareTo(new BigDecimal(0)) < 0
								&& (aSysvariable.getValueString() != null && aSysvariable
										.getValueString().contains("/JLF"))) {
							theEcjobs.setAmountDue(localprofitToDate
									.subtract(ecProfitPaidonTodate));
							theEcjobs.setCommissionRate(new BigDecimal(100));
						}

						ecJobiD = (Integer) aSession.save(theEcjobs);
						bTransaction.commit();
					}
				}
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString=null;aQuery = null;
		}
		return ecJobiD;
	}

	@Override
	public Ecjobs getEcJobsStatementID(Integer ecStatementID)
			throws EmployeeException {
		Session aSession = null;
		Ecjobs aEcjobs = null;Query ecJobQuery =null;
		List<Ecjobs> aEcJobsList = new ArrayList<Ecjobs>();
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession
					.createQuery(" FROM Ecjobs WHERE ecStatementID="
							+ ecStatementID + " ORDER BY ecJobsID DESC LIMIT 1");
			aEcJobsList = ecJobQuery.list();
			if (aEcJobsList.size() > 0) {
				aEcjobs = aEcJobsList.get(0);
			} else {
				aEcjobs = new Ecjobs();
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			ecJobQuery =null;
		}
		return aEcjobs;

	}

	@Override
	public Integer calCulateOverrides(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
		Ecjobs theEcjobs = new Ecjobs();
		Sysinfo aSysinfo = getSysinfoForCommissions();
		Integer endPeriodID = ecPeriodList.get(0).getEcPeriodId();
		List<Ecstatement> aEcStatementList = null;
		List<Ecinvoicerepsplit> aEcinvoicerepsplits = new ArrayList<Ecinvoicerepsplit>();
		Ecsalesmgroverride aEcsalesmgroverride = null;
		Integer ecJobiD = 0;

		Transaction bTransaction;
		Session ecJobsInsSession = null;
		try {
			ecJobsInsSession = itsSessionFactory.openSession();
			aEcStatementList = getEcStatementPeriodList(endPeriodID);
			for (int j = 0; j < aEcStatementList.size(); j++) {
				if (aEcStatementList.get(j).getEcStatementId() == aSysinfo
						.getSalesMgrId()) {
					bTransaction = ecJobsInsSession.beginTransaction();
					bTransaction.begin();
					theEcjobs = getEcJobsStatementID(aEcStatementList.get(j).getEcStatementId());
					aEcinvoicerepsplits = getEcInvoiceRepSplitDetails(" WHERE ecStatementID="
							+ aEcStatementList.get(j).getEcStatementId());
					aEcsalesmgroverride = new Ecsalesmgroverride();
					BigDecimal repSplitProfit= new BigDecimal(0.0000);
					if(aEcinvoicerepsplits.size()>0){
						repSplitProfit =aEcinvoicerepsplits.get(0).getProfit();
					}
					aEcsalesmgroverride.setAmountDue(repSplitProfit);
					aEcsalesmgroverride.setCommissionRate(aSysinfo
							.getCommissionOverride());
					aEcsalesmgroverride.setEcStatementId(aEcStatementList
							.get(j).getEcStatementId());
					if(theEcjobs.getProfitToDate()!=null && theEcjobs.getProfitPaidOnToDate() !=null){
					aEcsalesmgroverride.setProfit(theEcjobs.getProfitToDate()
							.subtract(theEcjobs.getProfitPaidOnToDate())
							.add(repSplitProfit));
					aEcsalesmgroverride.setJobProfit(theEcjobs
							.getProfitToDate().subtract(
									theEcjobs.getProfitPaidOnToDate()));
					}
					aEcsalesmgroverride.setRepLoginId(aEcStatementList.get(j)
							.getRepLoginId());
					ecJobiD = (Integer) ecJobsInsSession.save(aEcsalesmgroverride);
					bTransaction.commit();
				}
			}
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			ecJobsInsSession.flush();
			ecJobsInsSession.close();
		}
		return ecJobiD;
	}

	public Integer removeInactiveEntries(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {

		Integer endPeriodID = ecPeriodList.get(0).getEcPeriodId();
		itsLogger.debug("Remove InactiveEntries:" + endPeriodID);
		List<Ecstatement> aEcstatementsList = new ArrayList<Ecstatement>();
		aEcstatementsList = getEcStatementPeriodList(endPeriodID);
		Emmaster aEmmaster = new Emmaster();
		boolean canDelete = false;
		Transaction aTransaction;
		Session ecStatementSession = null;
		ecStatementSession = itsSessionFactory.openSession();
		if (aEcstatementsList.size() > 0) {
			for (int j = 0; j < aEcstatementsList.size(); j++) {
				if (aEcstatementsList.get(j).getOpeningBalance() == null
						|| aEcstatementsList.get(j).getOpeningBalance()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getJobCommissions() == null
						|| aEcstatementsList.get(j).getJobCommissions()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getOtherCommissions() == null
						|| aEcstatementsList.get(j).getOtherCommissions()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getOverrides() == null
						|| aEcstatementsList.get(j).getOverrides()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getAdjustments() == null
						|| aEcstatementsList.get(j).getAdjustments()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getPayment() == null
						|| aEcstatementsList.get(j).getPayment()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getRepDeduct1() == null
						|| aEcstatementsList.get(j).getRepDeduct1()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getRepDeduct2() == null
						|| aEcstatementsList.get(j).getRepDeduct2()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getRepDeduct3() == null
						|| aEcstatementsList.get(j).getRepDeduct3()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}
				if (aEcstatementsList.get(j).getRepDeduct4() == null
						|| aEcstatementsList.get(j).getRepDeduct4()
								.compareTo(new BigDecimal(0.0000)) == 0) {
					canDelete = true;
				}

				if (canDelete) {
					if (aEmmaster.getEmploymentStatus() != null
							&& aEmmaster.getEmploymentStatus() > 0) {
						String ecSalesMgrOverride = "DELETE FROM ecStatement WHERE ecStatementID = "
								+ aEcstatementsList.get(j).getEcStatementId();
						try {
							aTransaction = ecStatementSession
									.beginTransaction();
							ecStatementSession.createSQLQuery(
									ecSalesMgrOverride).executeUpdate();
							aTransaction.commit();
						} catch (HibernateException e) {
							e.printStackTrace();
							itsLogger.error(e.getMessage(), e);
							EmployeeException aEmployeeException = new EmployeeException(
									e.getMessage(), e);
							throw aEmployeeException;
						} finally {
							ecStatementSession.flush();
							ecStatementSession.close();
						}

					}
				}
			}
		}
		return 1;
	}

	@Override
	public Ecstatement getSaveEcStatement(Integer ecPeriodID, Integer loginRepID) {
		itsLogger.debug("saveNewPeriod:" + ecPeriodID);
		String aWerehouseTypeSelectQry = "SELECT * From ecStatement WHERE ecPeriodID="
				+ ecPeriodID + " and RepLoginID=" + loginRepID;
		Session aSession = null;
		Integer aInStatus = 0;Query aQuery =null;
		ArrayList<Ecstatement> aQueryList = new ArrayList<Ecstatement>();
		Ecstatement bEcstatement = new Ecstatement();
		try {
			Ecstatement aEcstatement = null;
			Ecstatement cEcstatement = null;
			Ecstatement theEcstatement = null;
			Emmaster aEmmaster = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aWerehouseTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theEcstatement = new Ecstatement();
				cEcstatement = new Ecstatement();
				Object[] aObj = (Object[]) aIterator.next();
				theEcstatement.setEcPeriodId(ecPeriodID);
				theEcstatement.setEcStatementId((Integer) aObj[0]);
				theEcstatement.setEcPeriodId((Integer) aObj[1]);
				theEcstatement.setRepLoginId((Integer) aObj[2]);
				aQueryList.add(theEcstatement);
				
				cEcstatement = (Ecstatement) aSession.get(
						Ecstatement.class, (Integer) aObj[0]);
				aEmmaster = getEmmasterDetails(loginRepID);
				cEcstatement.setRepDeduct1(aEmmaster.getRepDeduct1());
				cEcstatement.setRepDeduct2(aEmmaster.getRepDeduct2());
				cEcstatement.setRepDeduct3(aEmmaster.getRepDeduct3());
				cEcstatement.setRepDeduct4(aEmmaster.getRepDeduct4());
				BigDecimal RepDeduct1 = aEmmaster.getRepDeduct1()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct1();
				BigDecimal RepDeduct2 = aEmmaster.getRepDeduct2()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct2();
				BigDecimal RepDeduct3 = aEmmaster.getRepDeduct3()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct3();
				BigDecimal RepDeduct4 = aEmmaster.getRepDeduct4()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct4();
				cEcstatement.setAdjustments(RepDeduct1.add(RepDeduct2).add(RepDeduct3).add(RepDeduct4));
				Transaction periodUpdateTransaction;
				periodUpdateTransaction = aSession.beginTransaction();
				periodUpdateTransaction.begin();
				aSession.update(cEcstatement);
				periodUpdateTransaction.commit();
			}
			if (aQueryList.size() > 0) {
				bEcstatement = aQueryList.get(0);
			} else {
				aEmmaster = getEmmasterDetails(loginRepID);
				if (aEmmaster != null) {
					aEcstatement = new Ecstatement();
					aEcstatement.setEcPeriodId(ecPeriodID);
					aEcstatement.setRepLoginId(loginRepID);
					aEcstatement.setOpeningBalance(new BigDecimal(0.0000));
					aEcstatement.setJobCommissions(new BigDecimal(0.0000));
					aEcstatement.setOtherCommissions(new BigDecimal(0.0000));
					aEcstatement.setOverrides(new BigDecimal(0.0000));
					aEcstatement.setPayment(new BigDecimal(0.0000));
					aEcstatement.setRepDeduct1(aEmmaster.getRepDeduct1());
					aEcstatement.setRepDeduct2(aEmmaster.getRepDeduct2());
					aEcstatement.setRepDeduct3(aEmmaster.getRepDeduct3());
					aEcstatement.setRepDeduct4(aEmmaster.getRepDeduct4());
					BigDecimal RepDeduct1 = aEmmaster.getRepDeduct1()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct1();
					BigDecimal RepDeduct2 = aEmmaster.getRepDeduct2()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct2();
					BigDecimal RepDeduct3 = aEmmaster.getRepDeduct3()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct3();
					BigDecimal RepDeduct4 = aEmmaster.getRepDeduct4()==null?new BigDecimal("0.0000"):aEmmaster.getRepDeduct4();
					aEcstatement.setAdjustments(RepDeduct1.add(RepDeduct2).add(RepDeduct3).add(RepDeduct4));
					// aEcstatement.setComment(comment);
					aEcstatement.setCustom1(new BigDecimal(0.0000));
					aEcstatement.setCustom2(new BigDecimal(0.0000));
					aEcstatement.setCustom3(new BigDecimal(0.0000));
					aEcstatement.setCustom4(new BigDecimal(0.0000));
					aEcstatement.setCustom5(new BigDecimal(0.0000));
					aEcstatement.setCustom6(new BigDecimal(0.0000));
					aEcstatement.setCustom7(new BigDecimal(0.0000));
					aEcstatement.setCustom8(new BigDecimal(0.0000));
					aEcstatement.setCustom9(new BigDecimal(0.0000));
					aEcstatement.setCustom10(new BigDecimal(0.0000));
					// aEcstatement.setFlags(flags);
					Transaction periodInsertTransaction;
					periodInsertTransaction = aSession
							.beginTransaction();
					periodInsertTransaction.begin();
					aInStatus = (Integer) aSession
							.save(aEcstatement);
					bEcstatement = (Ecstatement) aSession.get(
							Ecstatement.class, aInStatus);
					periodInsertTransaction.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;aWerehouseTypeSelectQry =null;
		}
		return bEcstatement;
	}

	public CommissionCalculateBean calculateJobFinancials(Integer joMasterID,
			String EndingDate , String startPeriodDate) throws EmployeeException {

		String customerInvoiceQry = null;
		String vendorBillsQry = null;
		String commissionSalesQry = null;
		String miscBillsQry = null;

		Jomaster aJomaster = new Jomaster();
		CommissionCalculateBean ccBean = new CommissionCalculateBean();

		BigDecimal jobSales = new BigDecimal(0.00);
		BigDecimal jobCosts = new BigDecimal(0.00);

		customerInvoiceQry = "SELECT  IF(SUM(I.CostTotal) IS NULL,0.0000,SUM(I.CostTotal)) AS CostTotal, "
				+ "IF(Sum(I.Subtotal) IS NULL,0.0000,Sum(I.Subtotal)) AS Subtotal "
				+ " FROM joRelease AS R RIGHT JOIN (joReleaseDetail AS D RIGHT JOIN cuInvoice "
				+ "AS I ON D.joReleaseDetailID = I.joReleaseDetailID) ON R.joReleaseID = D.joReleaseID "
				+ "WHERE I.InvoiceDate <='"
				+ EndingDate
				+ "'"
				/*+ " AND I.InvoiceDate >='"+startPeriodDate+"' "*/
				+ " AND  R.joMasterID = "+ joMasterID
				+ " And I.TransactionStatus>0";
		
		
		vendorBillsQry = "SELECT IF(Sum(B.BillAmount) IS NULL,0.0000,Sum(B.BillAmount)) AS BA, IF(Sum(B.FreightAmount) IS NULL,0.0000,"
				+ "Sum(B.FreightAmount)) AS FA, IF(Sum(B.TaxAmount) IS NULL,0.0000,Sum(B.TaxAmount)) AS TA "
				+ " FROM joRelease AS R RIGHT JOIN (joReleaseDetail AS D RIGHT JOIN veBill AS B ON D.joReleaseDetailID = B.joReleaseDetailID) "
				+ "ON R.joReleaseID = D.joReleaseID  WHERE B.PostDate <= '"
				+ EndingDate+"'"
				/*+ "' AND B.PostDate >='"+startPeriodDate+"'"*/
				+ " AND R.joMasterID="
				+ joMasterID
				+ " AND B.TransactionStatus>0";

		commissionSalesQry = "SELECT IF(SUM(EstimatedBilling) IS NULL,0.0000,SUM(EstimatedBilling)) AS EB, "
				+ "IF(SUM(CommissionAmount) IS NULL,0.0000,SUM(CommissionAmount)) AS CA   FROM joRelease "
				+ "WHERE CommissionDate <= '"
				+ EndingDate+"'"
				/*+ " AND CommissionDate >='"+startPeriodDate+"'"*/
				+ " AND CommissionReceived= 1 AND ReleaseType=4 AND joMasterID="
				+ joMasterID;

		miscBillsQry = "SELECT IF(SUM(ExpenseAmount) IS NULL,0.00,SUM(ExpenseAmount)) as EA FROM veBillDistribution WHERE joMasterID = "
				+ joMasterID;
		// JobCosts = JobCosts + SumCurrency("veBillDistribution",
		// "ExpenseAmount", "joMasterID = " & Str(joMasterID)) ' zq ignores date

		try {
			aJomaster = jobService.getSingleJobDetails(joMasterID);
		} catch (JobException e) {
			e.printStackTrace();
		}
		jobSales = aJomaster.getPriorRevenue();
		jobCosts = aJomaster.getPriorCost();
		try {
			jobSales = getBigDecimalValue("SELECT PriorRevenue FROM joMaster WHERE joMasterid="+joMasterID);
			jobCosts = getBigDecimalValue("SELECT PriorCost FROM joMaster WHERE joMasterid="+joMasterID);
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Session aSession = null;
		// Session vbSession=null;
		// Session csSession=null;
		// Session mbSession=null;
		Query aQuery =null;
		Query vbQuery =null;
		Query csQuery =null;
		Query mbQuery =null;
		if (jobSales == null)
			jobSales = new BigDecimal(0.00);
		if (jobCosts == null)
			jobCosts = new BigDecimal(0.00);
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(customerInvoiceQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator != null) {
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					BigDecimal cost = null;
					BigDecimal subTotal = null;

					cost = (BigDecimal) aObj[0];
					subTotal = (BigDecimal) aObj[1];
					jobSales = jobSales.add(cost);
					jobCosts = jobCosts.add(subTotal);
					itsLogger
							.info("Total Values: " + aObj[0] + " - " + aObj[1]);
				}
			}

			// vbSession = itsSessionFactory.openSession();
			if (jobSales == null)
				jobSales = new BigDecimal("0.0000");
			if (jobCosts == null)
				jobCosts = new BigDecimal("0.0000");
			vbQuery = aSession.createSQLQuery(vendorBillsQry);
			Iterator<?> vbIterator = vbQuery.list().iterator();
			if (vbIterator != null) {
				while (vbIterator.hasNext()) {
					Object[] vbObj = (Object[]) vbIterator.next();
					jobCosts = jobCosts.add((BigDecimal) vbObj[0]);
				}
			}
			
			if (jobCosts == null)
				jobCosts = new BigDecimal("0.0000");
			// csSession = itsSessionFactory.openSession();
			csQuery = aSession.createSQLQuery(commissionSalesQry);
			Iterator<?> csIterator = csQuery.list().iterator();
			if (csIterator != null) {
				while (csIterator.hasNext()) {
					BigDecimal eb = null;
					BigDecimal ca = null;
					Object[] csObj = (Object[]) csIterator.next();
					eb = (BigDecimal) csObj[0];
					ca = (BigDecimal) csObj[1];
					jobCosts = jobCosts.add(eb);
					jobCosts = jobCosts.subtract(ca);
					jobSales = jobSales.add((BigDecimal) csObj[0]);

				}
			}
			
			if (jobSales == null)
				jobSales = new BigDecimal("0.0000");
			if (jobCosts == null)
				jobCosts = new BigDecimal("0.0000");
			
			// mbSession = itsSessionFactory.openSession();
			mbQuery = aSession.createSQLQuery(miscBillsQry);
			List<?> mbIterator = mbQuery.list();
			BigDecimal jcost = null;
			jcost = (BigDecimal) mbIterator.get(0);
			if(jcost==null){
				jcost=new BigDecimal(0.0000);
			}
			jobCosts = jobCosts.add(jcost);

			ccBean.setTotalCosts(jobCosts);
			ccBean.setTotalSales(jobSales);

		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			customerInvoiceQry=null;vendorBillsQry=null;commissionSalesQry=null;miscBillsQry=null;
			aQuery =null;vbQuery =null;csQuery =null;mbQuery =null;
		}
		return ccBean;

	}
	
	public ArrayList<CommissionCalculateBean> calculateJobCommission(Integer joMasterID,
			String EndingDate,String startPeriodDate, String commPayment) throws EmployeeException {

		String customerInvoiceQry = null;
		CommissionCalculateBean ccBean = new CommissionCalculateBean();
		ArrayList<CommissionCalculateBean> ccBeanList = new ArrayList<CommissionCalculateBean>();
		String Condition = "";
		/*customerInvoiceQry = "SELECT D.joReleaseID, IF(vb.BillAmount IS NULL,0.0000, vb.BillAmount)  AS vendorAmount, "
				+ " ((IF(cui.CostTotal IS NULL,0.0000,cui.CostTotal)) - (IF(cui.TaxAmount IS NULL,0.0000,cui.TaxAmount))) AS Subtotal FROM joRelease AS R "
				+ " RIGHT JOIN joReleaseDetail AS D ON R.joReleaseID = D.joReleaseID "
				+ " LEFT JOIN cuInvoice AS cui ON D.joReleaseDetailID = cui.joReleaseDetailID"
				+ " LEFT JOIN veBill AS vb ON D.joReleaseDetailID = vb.joReleaseDetailID "
				+ "WHERE D.PaymentDate <='"+EndingDate+"' AND D.joMasterID="+joMasterID+" AND D.VendorInvoiceDate IS NOT NULL AND D.CustomerInvoiceDate IS NOT NULL";
		*/
		if(commPayment.equals("payment")){
			Condition = " AND D.PaymentDate <='"+EndingDate+"' "
					/*+ "AND D.PaymentDate >='"+startPeriodDate+"'"*/;
		}else if(commPayment.equals("invoiced")){
			Condition = " AND D.CustomerInvoiceDate <='"+EndingDate+"' "
					/*+ " AND D.CustomerInvoiceDate >='"+startPeriodDate+"'"*/
					+ " AND D.PaymentDate IS NULL ";
		}
		
		customerInvoiceQry = "SELECT D.joReleaseID, IF(D.VendorInvoiceAmount IS NULL,0.0000, D.VendorInvoiceAmount)  AS vendorAmount, "
				+ " IF(D.CustomerInvoiceAmount IS NULL,0.0000,D.CustomerInvoiceAmount) AS Subtotal FROM joRelease AS R  "
				+ " RIGHT JOIN joReleaseDetail AS D ON R.joReleaseID = D.joReleaseID "
				+ "WHERE D.joMasterID="+joMasterID+ Condition
				+ " AND D.CustomerInvoiceDate IS NOT NULL";
		
		Session aSession = null;Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(customerInvoiceQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator != null) {
				while (aIterator.hasNext()) {
					ccBean = new CommissionCalculateBean();
					Object[] aObj = (Object[]) aIterator.next();
					BigDecimal cost = null;
					BigDecimal subTotal = null;
					
					cost = (BigDecimal) aObj[1];
					subTotal = (BigDecimal) aObj[2];
					itsLogger.info("Total Values: " + aObj[0] + " - " + aObj[1]);	
					ccBean.setJoReleaseID((Integer) aObj[0]);
					ccBean.setTotalCosts(cost);
					ccBean.setTotalSales(subTotal);
					ccBeanList.add(ccBean);
				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			customerInvoiceQry=null;aQuery =null;
		}
		return ccBeanList;

	}

	@Override
	public Integer calculatePaidInvoices(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
		Sysvariable aSysvariable = null;
		aSysvariable = getSysVariableDetails("CommissionSpecialCode");
		String aQueryString = null;
		Integer ecInvoiceID = 0;
		Integer ecInvoiceRepSplitID = 0;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		Transaction aTransaction;
		Transaction bTransaction;
		Ecstatement aEcstatement = null;
		Ecsplitjob aEcsplitjob = null;
		Ecsplitjob theEcsplitjob = null;
		Integer payCommissionAfter = null;
		Emmaster aEmmaster = null;
		Emmaster bEmmaster = null;
		ArrayList<CuLinkageDetail> aCuLinkageDetaiList = null;
		Query invoiceDateQuery =null;
		aQueryString = "SELECT PaidInvoices.* FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber,"
				+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount,"
				+ "I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID,"
				+ " IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal,I.FreightCost FROM "
				+ "(cuInvoice AS I LEFT JOIN joReleaseDetail AS RD ON I.joReleaseDetailID = RD.joReleaseDetailID) "
				+ "LEFT JOIN joRelease AS R ON RD.joReleaseID = R.joReleaseID WHERE I.TransactionStatus>0 AND I.InvoiceDate<= '"
				+ endPeriodDate
				/*+ "' AND I.InvoiceDate>= '"
				+ startPeriodDate*/
				+ "' "
				+ " AND I.InvoiceAmount <> 0 AND (ABS(I.InvoiceAmount-(I.AppliedAmount+I.DiscountAmt)<0.005 OR InvoiceAmount<0.005))) AS PaidInvoices "
				+ "LEFT JOIN ecInvoicePeriod ON PaidInvoices.cuInvoiceID = ecInvoicePeriod.cuInvoiceID "
				+ "WHERE ecInvoicePeriod.cuInvoiceID IS NULL ";
		Session aSession = null;Query aQuery =null;
		ArrayList<Cuinvoice> aQueryList = new ArrayList<Cuinvoice>();
		try {
			Cuinvoice aCuinvoice = null;
			Ecinvoiceperiod aEcinvoiceperiod = null;
			Ecinvoiceperiod theEcinvoiceperiod = null;
			Ecinvoicerepsplit aEcinvoicerepsplit = null;
			Ecinvoices aEcinvoices = null;
			BigDecimal costTotal = null;
			BigDecimal addedCost = null;
			Date addedCostDate = null;
			JoInvoiceCost aJoInvoiceCost = null;
			Vebill aVebill = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuinvoice = new Cuinvoice();
				Object[] aObj = (Object[]) aIterator.next();
				aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
				aCuinvoice.setInvoiceDate((Date) aObj[1]);
				aCuinvoice.setInvoiceNumber((String) aObj[2]);
				aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
				aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
				aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
				aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
				aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
				aCuinvoice.setFreight((BigDecimal) aObj[8]);
				aCuinvoice.setCostTotal((BigDecimal) aObj[9]);
				aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
				aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
				aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
				aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
				aCuinvoice.setJoMasterID((Integer) aObj[14]);
				//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

				aCuinvoice
						.setJoReleaseType((Integer) aObj[15]);
				aCuinvoice.setRxCustomerId((Integer) aObj[16]);
				aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
						: aObj[17]));
				aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
						: aObj[18]));
				aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
						: aObj[19]));
				aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
				: aObj[20]));
				aQueryList.add(aCuinvoice);
			}
			itsLogger.info("CuInvoice List Size: " + aQueryList.size());
			if(aQueryList.size()>0){
			for (int i = 0; i < aQueryList.size(); i++) {
				aEcinvoiceperiod = new Ecinvoiceperiod();
				theEcinvoiceperiod = new Ecinvoiceperiod();
				aVebill = new Vebill();
				/*payCommissionAfter = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())==null?0:getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommAfterPayment();*/
				bEmmaster = new Emmaster();
				bEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
				if(bEmmaster!=null){
					payCommissionAfter = bEmmaster.getCommAfterPayment()==null?0:bEmmaster.getCommAfterPayment();
				}
				if(payCommissionAfter!=null && payCommissionAfter==1){
					aCuLinkageDetaiList = getCuLinkageDetails(aQueryList.get(i), endPeriodDate,startPeriodDate);
				if(aCuLinkageDetaiList!=null){
				/*BigDecimal Paid = getCuLinkageDetails(aQueryList.get(i),
						endPeriodDate,startPeriodDate)
						.get(0)
						.getPaymentApplied()
						.add(getCuLinkageDetails(aQueryList.get(i),
								endPeriodDate,startPeriodDate).get(0).getDiscountUsed());*/
				// if((aQueryList.get(i).getInvoiceAmount().subtract(Paid).compareTo(new
				// BigDecimal(0.005))>1) ||
				// (aQueryList.get(i).getInvoiceAmount().compareTo(new
				// BigDecimal(0.005))>1)){
				aVebill = getVeBillDetails(aQueryList.get(i)).get(0);
				/*BigDecimal vendorCost = getVeBillDetails(aQueryList.get(i))
						.get(0).getBillAmount() == null ? new BigDecimal(0.0000)
						: getVeBillDetails(aQueryList.get(i)).get(0)
								.getBillAmount();*/
				BigDecimal vendorCost = aVebill==null?new BigDecimal("0.0000"):aVebill.getBillAmount()==null?new BigDecimal("0.0000"):aVebill.getBillAmount();
				/*08/26/2015 for BID#825 vendorCost = vendorCost.add(aQueryList.get(i).getFreightCost());*/
				addedCost = new BigDecimal("0.0000");
				costTotal = new BigDecimal("0.0000");
				costTotal = aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal();
				/*if(aQueryList.get(i).getJoReleaseDetailId()!=null){
					addedCost = getAddedCost(aQueryList.get(i).getJoReleaseDetailId(),startPeriodDate,endPeriodDate);
					addedCost = addedCost==null?new BigDecimal("0.0000"):addedCost;
				}*/
				costTotal = costTotal.add(addedCost);
				//vendorCost = vendorCost.add(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal());
				vendorCost = vendorCost.add(costTotal);
				/*//BigDecimal vendorFreight = getVeBillDetails(aQueryList.get(i)).get(0).getFreightAmount();*/				
				BigDecimal vendorFreight = aVebill==null?new BigDecimal("0.0000"):aVebill.getFreightAmount()==null?new BigDecimal("0.0000"):aVebill.getFreightAmount();
				vendorFreight = vendorFreight==null?new BigDecimal("0.0000"):vendorFreight;
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(aQueryList.get(i)
						.getCuInvoiceId());
				if (aCuLinkageDetaiList.get(0).getReciptDate() != null
						&& aCuLinkageDetaiList.get(0).getReciptDate() != new Date()) {
					aEcinvoiceperiod.setDatePaid(aCuLinkageDetaiList.get(0)
							.getReciptDate());
				}
				
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				if(aQueryList.get(i).getJoReleaseType()==1){
				/*if (aQueryList.get(i).getCostTotal() != null) {*/
					aEcinvoiceperiod.setProfit(((aQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getSubtotal()).add(aQueryList.get(i).getFreight()==null?new BigDecimal(0.00):aQueryList.get(i).getFreight()))
							.subtract(vendorCost==null?new BigDecimal("0.0000"):vendorCost)); // subtotal from CustInvoice for without tax
				/*} else {
					aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
				}*/
				}
				else if(aQueryList.get(i).getJoReleaseType()==2){
					if (aQueryList.get(i).getCostTotal() != null) {
						aEcinvoiceperiod.setProfit(((aQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getSubtotal()).add(aQueryList.get(i).getFreight()==null?new BigDecimal(0.00):aQueryList.get(i).getFreight()))
								/*.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax*/						
								.subtract(costTotal));
								
					} else {
						aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
					}
					}
				else if(aQueryList.get(i).getJoReleaseType()==0){
					itsLogger.info("Release is SalesOrder");
					if (aQueryList.get(i).getCostTotal() != null) {
						aEcinvoiceperiod.setProfit(((aQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getSubtotal()).add(aQueryList.get(i).getFreight()==null?new BigDecimal(0.00):aQueryList.get(i).getFreight()))
								/*.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax*/
								.subtract(costTotal));
								
					} else {
						aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
					}
					}
				else{
					if (aQueryList.get(i).getCostTotal() != null) {
						aEcinvoiceperiod.setProfit((aQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getSubtotal()).subtract(vendorCost==null?new BigDecimal("0.0000"):vendorCost)); // subtotal from CustInvoice for without tax
					} else {
						aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
					}
				}
				aEcinvoiceperiod.setReference(aQueryList.get(i)
						.getInvoiceNumber() == null ? "" : aQueryList.get(i)
						.getInvoiceNumber());
				if (aQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) aQueryList.get(i)
							.getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (aQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(aQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;

				if (aQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = aQueryList.get(i).getSubtotal();
				}
				if (aQueryList.get(i).getFreight() != null) {
					freight = aQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				if(subTotal==null){subTotal= new BigDecimal("0.0000");}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("I");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();

				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(aQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,aQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (aQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(costTotal
							.subtract(vendorCost==null?new BigDecimal("0.0000"):vendorCost));
					profit = costTotal
							.subtract(vendorCost==null?new BigDecimal("0.0000"):vendorCost);
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
				}
			}
				}
				}
				
				BigDecimal acommissionProfit = new BigDecimal(0.0000);
				BigDecimal profits = new BigDecimal(0.000);
				List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
				List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
				aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(aQueryList.get(i).getJoMasterID(),aQueryList.get(i).getCuInvoiceId(),"invoice");
				aEcSplitJobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterID());
				itsLogger.info("SplitJob List Size::"+aEcSplitJobEmMasterList.size());
				if (aEcSplitJobEmMasterList.size() > 0) {
							for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
								theEcsplitjob= new Ecsplitjob();
								theEcsplitjob = aEcSplitJobEmMasterList.get(m);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							
							if(payCommissionAfter!=null && payCommissionAfter==1){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,theEcsplitjob.getRxMasterId());

							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
									acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal("0.0000"):aEmmaster.getCommissionJobProfit();
								}
								//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
								/*Commented on 10-04-2015
								 * if (aQueryList.get(i).getFreight() != null
										&& aQueryList.get(i).getFreight()
												.compareTo(new BigDecimal(0.00)) > 0) {*/
								BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
								BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
								theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
								theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
								if(theEcinvoiceperiodProfit==null){theEcinvoiceperiodProfit = new BigDecimal("0.0000");}
								if(acommissionProfit==null){acommissionProfit = new BigDecimal("0.0000");}
								if(theEcsplitjobAllocated==null){theEcsplitjobAllocated = new BigDecimal("0.0000");}
								profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
								/*} else {
									profits = theEcinvoiceperiod.getProfit();
								}*/
								
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
								
								aEcinvoicerepsplit.setAmountDue(profits);
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
						}
					}
				}
				else if (aEcSplitJobList.size() > 0) {
						for(int k=0;k<aEcSplitJobList.size();k++){
							aEcsplitjob= new Ecsplitjob();
							aEcsplitjob = aEcSplitJobList.get(k);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							
							if(payCommissionAfter!= null && payCommissionAfter ==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,aEcsplitjob.getRxMasterId());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							acommissionProfit = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommissionJobProfit();
							aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
							BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
							if(theEcinvoiceperiod.getProfit()==null){
								ecInvoicePeriodProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setAmountDue(ecInvoicePeriodProfit.multiply(acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply((aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjob.getAllocated()).multiply(new BigDecimal(0.01))));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
						}
					}
					else {
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
						/*payCommissionAfter = ecsEmmaster==null?0:ecsEmmaster.getCommAfterPayment();*/
						if(ecsEmmaster!=null){
							payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						if(payCommissionAfter!=0 && payCommissionAfter==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
								.get(i).getCuAssignmentId0());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
							aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
								if(aQueryList.get(i).getJoReleaseType()==0){
									acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
								}else{
									acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
								}
									
								}
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
							if(theEcinvoiceperiod.getProfit()==null){
								ecInvoicePeriodProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
							aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
									.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
											.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
									.multiply(new BigDecimal(0.01)));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
					}
				/*if (aQueryList.get(i).getCuAssignmentId1() > 0) {
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
							.get(i).getCuAssignmentId1());
					if (aEcstatement != null) {
						bTransaction = ecSplitCommSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement
								.getEcStatementId());
						acommissionProfit = getEmmasterDetails(
								aQueryList.get(i).getCuAssignmentId1())
								.getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
						if (aQueryList.get(i).getFreight() != null
								&& aQueryList.get(i).getFreight()
										.compareTo(new BigDecimal(0.00)) > 0) {
							profits = theEcinvoiceperiod.getSales().subtract(
									aQueryList.get(i).getFreight());
						} else {
							profits = theEcinvoiceperiod.getSales().subtract(
									vendorFreight);
						}
						aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
								.getProfit());
						aEcinvoicerepsplit.setAmountDue(profits.multiply(
								acommissionProfit));
						ecInvoiceRepSplitID = (Integer) ecJobsInsSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
				}
				if (aQueryList.get(i).getCuAssignmentId2() > 0) {
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
							.get(i).getCuAssignmentId2());
					if (aEcstatement != null) {
						bTransaction = ecSplitCommSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement
								.getEcStatementId());
						acommissionProfit = getEmmasterDetails(
								aQueryList.get(i).getCuAssignmentId2())
								.getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
						if (aQueryList.get(i).getFreight() != null
								&& aQueryList.get(i).getFreight()
										.compareTo(new BigDecimal(0.00)) > 0) {
							profits = theEcinvoiceperiod.getSales().subtract(
									aQueryList.get(i).getFreight());
						} else {
							profits = theEcinvoiceperiod.getSales().subtract(
									vendorFreight);
						}
						aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
								.getProfit());
						aEcinvoicerepsplit.setAmountDue(profits.multiply(
								acommissionProfit));
						ecInvoiceRepSplitID = (Integer) ecJobsInsSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
				}*/
			
				/* For Added Cost Calculation Starts Here */
				if(aQueryList.get(i).getJoReleaseDetailId()!=null){
					aJoInvoiceCost = getAddedCost(aQueryList.get(i).getJoReleaseDetailId(),startPeriodDate,endPeriodDate);
					addedCost = aJoInvoiceCost.getCost()==null?new BigDecimal("0.0000"):aJoInvoiceCost.getCost();
					addedCostDate = aJoInvoiceCost.getEnteredDate();
				}
				if(addedCost.compareTo(new BigDecimal("0.0000"))!=0){
				aEcinvoiceperiod = new Ecinvoiceperiod();
				theEcinvoiceperiod = new Ecinvoiceperiod();
				/*payCommissionAfter = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())==null?0:getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommAfterPayment();*/
				//2015-10-08 bEmmaster = new Emmaster();
				//2015-10-08 bEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
				if(bEmmaster!=null){
					payCommissionAfter = bEmmaster.getCommAfterPayment()==null?0:bEmmaster.getCommAfterPayment();
				}
				if(payCommissionAfter!=null && payCommissionAfter==1){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					aEcinvoiceperiod.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
					aEcinvoiceperiod.setDatePaid(addedCostDate);
					aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
					aEcinvoiceperiod.setModified(false);
					aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(addedCost)); // subtotal from CustInvoice for without tax
					aEcinvoiceperiod.setReference(aQueryList.get(i).getInvoiceNumber() == null ? "" : aQueryList.get(i).getInvoiceNumber());
					if (aQueryList.get(i).getJoReleaseType() != null) {
						aEcinvoiceperiod.setReleaseType((byte) aQueryList.get(i).getJoReleaseType().intValue());
					} else {
						aEcinvoiceperiod.setReleaseType((byte) 0);
					}
					if (aQueryList.get(i).getRxCustomerId() != null) {
						aEcinvoiceperiod.setRxCustomerId(aQueryList.get(i)
								.getRxCustomerId());
					}
					BigDecimal subTotal = null;
					BigDecimal freight = null;
		
					if (aQueryList.get(i).getSubtotal() == null) {
						subTotal = new BigDecimal(0.000);
					} else {
						subTotal = aQueryList.get(i).getSubtotal();
					}
					if (aQueryList.get(i).getFreight() != null) {
						freight = aQueryList.get(i).getFreight();
					} else {
						freight = new BigDecimal(0.000);
					}
					if(subTotal==null){subTotal= new BigDecimal("0.0000");}
					aEcinvoiceperiod.setSales(subTotal.add(freight));
					aEcinvoiceperiod.setEnteredDate(new Date());
					aEcinvoiceperiod.setEcType("A");
					ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
					aTransaction.commit();
					updateAddedCostPaid(aQueryList.get(i).getJoReleaseDetailId(),startPeriodDate,endPeriodDate,ecInvoiceID);
					theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
							Ecinvoiceperiod.class, ecInvoiceID);
					BigDecimal profit=new BigDecimal(0.000);
					BigDecimal comRate = new BigDecimal(0.00);
					//Rep Functionality
					if (aSysvariable.getValueString() != null
							&& aSysvariable.getValueString().contains("/CJ")) {
					aEcinvoices = new Ecinvoices();
					aEcstatement = new Ecstatement();
					
					if(aQueryList.get(i).getCuAssignmentId0()!=null){
						aEcstatement = getSaveEcStatement(ecPeriodID,aQueryList.get(i).getCuAssignmentId0());
						aEmmaster =  getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
						if(aEmmaster!=null){
						comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
						}
						else{
						comRate = new BigDecimal(0.00);
					}
					aEcinvoices.setCommissionRate(comRate);
					
					
					aEcinvoices.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
					aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
					if (aQueryList.get(i).getCostTotal() != null) {
						aEcinvoices.setProfit(new BigDecimal("0.0000") 
								.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal()));
						profit = new BigDecimal("0.0000") 
						.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal());
					} else {
						aEcinvoices.setProfit(new BigDecimal(0.000));
						profit = new BigDecimal(0.000);
					}
					if(comRate==null){
						comRate = new BigDecimal(0.00);
					}
					
					
					aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
					bTransaction = aSession.beginTransaction();
					bTransaction.begin();
					ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
					bTransaction.commit();
				}
			}
				}
				
				acommissionProfit = new BigDecimal(0.0000);
				profits = new BigDecimal(0.000);
				/*List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
				List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
				aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(aQueryList.get(i).getJoMasterID(),aQueryList.get(i).getCuInvoiceId(),"invoice");
				aEcSplitJobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterID());*/
				itsLogger.info("SplitJob List Size::"+aEcSplitJobEmMasterList.size());
				if (aEcSplitJobEmMasterList.size() > 0) {
							for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
								theEcsplitjob= new Ecsplitjob();
								theEcsplitjob = aEcSplitJobEmMasterList.get(m);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							
							if(payCommissionAfter!=null && payCommissionAfter==1){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,theEcsplitjob.getRxMasterId());

							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
									acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal("0.0000"):aEmmaster.getCommissionJobProfit();
								}
								//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
								/*Commented on 10-04-2015
								 * if (aQueryList.get(i).getFreight() != null
										&& aQueryList.get(i).getFreight()
												.compareTo(new BigDecimal(0.00)) > 0) {*/
								BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
								BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
								theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
								theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
								if(theEcinvoiceperiodProfit==null){theEcinvoiceperiodProfit = new BigDecimal("0.0000");}
								if(acommissionProfit==null){acommissionProfit = new BigDecimal("0.0000");}
								if(theEcsplitjobAllocated==null){theEcsplitjobAllocated = new BigDecimal("0.0000");}
								profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
								/*} else {
									profits = theEcinvoiceperiod.getProfit();
								}*/
								
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
								
								aEcinvoicerepsplit.setAmountDue(profits);
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
						}
					}
				}
				else if (aEcSplitJobList.size() > 0) {
						for(int k=0;k<aEcSplitJobList.size();k++){
							aEcsplitjob= new Ecsplitjob();
							aEcsplitjob = aEcSplitJobList.get(k);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							
							if(payCommissionAfter!= null && payCommissionAfter ==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,aEcsplitjob.getRxMasterId());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							acommissionProfit = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommissionJobProfit();
							aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
							BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
							if(theEcinvoiceperiod.getProfit()==null){
								ecInvoicePeriodProfit = new BigDecimal("0.0000");
							}else{
								ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
							}
							aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setAmountDue(ecInvoicePeriodProfit.multiply(acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply((aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjob.getAllocated()).multiply(new BigDecimal(0.01))));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
						}
					}
					else {
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
						/*payCommissionAfter = ecsEmmaster==null?0:ecsEmmaster.getCommAfterPayment();*/
						if(ecsEmmaster!=null){
							payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						if(payCommissionAfter!=0 && payCommissionAfter==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
								.get(i).getCuAssignmentId0());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
							aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
								if(aQueryList.get(i).getJoReleaseType()==0){
									acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
								}else{
									acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
								}
									
								}
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
							if(theEcinvoiceperiod.getProfit()==null){
								ecInvoicePeriodProfit = new BigDecimal("0.0000");
							}else{
								ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
							}
							aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
							aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
									.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
											.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
									.multiply(new BigDecimal(0.01)));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
					}
				}
			/*Added Cost Calculation Completed*/
				
			}
		}
			aQueryString = "SELECT PaidInvoices.* FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber,"
					+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount,"
					+ "I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID,"
					+ " IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal,I.FreightCost FROM "
					+ "(cuInvoice AS I LEFT JOIN joReleaseDetail AS RD ON I.joReleaseDetailID = RD.joReleaseDetailID) "
					+ "LEFT JOIN joRelease AS R ON RD.joReleaseID = R.joReleaseID WHERE I.TransactionStatus>0 AND I.InvoiceDate<= '"
					+ endPeriodDate
					/*+ "' AND I.InvoiceDate>= '"
					+ startPeriodDate*/
					+ "' "
					+ " ) AS PaidInvoices "
					+ "LEFT JOIN ecInvoicePeriod ON PaidInvoices.cuInvoiceID = ecInvoicePeriod.cuInvoiceID "
					+ "WHERE ecInvoicePeriod.cuInvoiceID IS NULL ";
			
			ArrayList<Cuinvoice> invoiceDateQueryList = new ArrayList<Cuinvoice>();
			try {
				aSession = itsSessionFactory.openSession();
				invoiceDateQuery = aSession.createSQLQuery(aQueryString);
				Iterator<?> invoiceDateIterator = invoiceDateQuery.list().iterator();
				while (invoiceDateIterator.hasNext()) {
					aCuinvoice = new Cuinvoice();
					Object[] invoiceDateObj = (Object[]) invoiceDateIterator.next();
					aCuinvoice.setCuInvoiceId((Integer) invoiceDateObj[0]);
					aCuinvoice.setInvoiceDate((Date) invoiceDateObj[1]);
					aCuinvoice.setInvoiceNumber((String) invoiceDateObj[2]);
					aCuinvoice.setPrToWarehouseId((Integer) invoiceDateObj[3]);
					aCuinvoice.setJoReleaseDetailId((Integer) invoiceDateObj[4]);
					aCuinvoice.setInvoiceAmount((BigDecimal) invoiceDateObj[5]);
					aCuinvoice.setAppliedAmount((BigDecimal) invoiceDateObj[6]);
					aCuinvoice.setTaxAmount((BigDecimal) invoiceDateObj[7]);
					aCuinvoice.setFreight((BigDecimal) invoiceDateObj[8]);
					aCuinvoice.setCostTotal((BigDecimal) invoiceDateObj[9]);
					aCuinvoice.setSubtotal((BigDecimal) invoiceDateObj[10]);
					aCuinvoice.setTaxTotal((BigDecimal) invoiceDateObj[11]);
					aCuinvoice.setTaxRate((BigDecimal) invoiceDateObj[12]);
					aCuinvoice.setCuAssignmentId0((Integer) invoiceDateObj[13]);
					aCuinvoice.setJoMasterID((Integer) invoiceDateObj[14]);
					//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

					aCuinvoice
							.setJoReleaseType((Integer) invoiceDateObj[15]);
					aCuinvoice.setRxCustomerId((Integer) invoiceDateObj[16]);
					aCuinvoice.setCuAssignmentId1((Integer) (invoiceDateObj[17] == null ? 0
							: invoiceDateObj[17]));
					aCuinvoice.setCuAssignmentId2((Integer) (invoiceDateObj[18] == null ? 0
							: invoiceDateObj[18]));
					aCuinvoice.setWhseCostTotal((BigDecimal) (invoiceDateObj[19] == null ? new BigDecimal("0.0000")
					: invoiceDateObj[19]));
					aCuinvoice.setFreightCost((BigDecimal) (invoiceDateObj[20] == null ? new BigDecimal("0.0000")
					: invoiceDateObj[20]));
					invoiceDateQueryList.add(aCuinvoice);
				}
				itsLogger.info("CuInvoice List Size: " + invoiceDateQueryList.size());
				if(invoiceDateQueryList.size()>0){
				for (int i = 0; i < invoiceDateQueryList.size(); i++) {
					
					aEcinvoiceperiod = new Ecinvoiceperiod();
					theEcinvoiceperiod = new Ecinvoiceperiod();
					BigDecimal acommissionProfit = new BigDecimal(0.0000);
					BigDecimal profits = new BigDecimal(0.000);
					List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
					List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
					Emmaster theEmmaster = new Emmaster();
					theEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					if(theEmmaster!=null)
						payCommissionAfter = theEmmaster.getCommAfterPayment()==null?0:theEmmaster.getCommAfterPayment();
					if(payCommissionAfter == null || payCommissionAfter==0){
						aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(invoiceDateQueryList.get(i).getJoMasterID(),invoiceDateQueryList.get(i).getCuInvoiceId(),"invoice");
						aEcSplitJobList = getEcSplitJobDetails(invoiceDateQueryList.get(i).getJoMasterID());
						aCuLinkageDetaiList = getCuLinkageDetails(invoiceDateQueryList.get(i), endPeriodDate,startPeriodDate);
						if(aCuLinkageDetaiList!=null){
					/* on 08/09/2015 BigDecimal Paid = getCuLinkageDetails(invoiceDateQueryList.get(i),
							endPeriodDate,startPeriodDate)
							.get(0)
							.getPaymentApplied()
							.add(getCuLinkageDetails(invoiceDateQueryList.get(i),
									endPeriodDate,startPeriodDate).get(0).getDiscountUsed());*/
					// if((aQueryList.get(i).getInvoiceAmount().subtract(Paid).compareTo(new
					// BigDecimal(0.005))>1) ||
					// (aQueryList.get(i).getInvoiceAmount().compareTo(new
					// BigDecimal(0.005))>1)){
					/*BigDecimal vendorCost = getVeBillDetails(invoiceDateQueryList.get(i))
							.get(0).getBillAmount() == null ? new BigDecimal(0.0000)
							: getVeBillDetails(invoiceDateQueryList.get(i)).get(0)
									.getBillAmount();*/
					aVebill = getVeBillDetails(invoiceDateQueryList.get(i)).get(0);
					BigDecimal vendorCost = aVebill==null?new BigDecimal("0.0000"):aVebill.getBillAmount()==null?new BigDecimal("0.0000"):aVebill.getBillAmount();
					/* On 8/26/2015 for BID#825 vendorCost = vendorCost.add(invoiceDateQueryList.get(i).getFreightCost());*/
					addedCost = new BigDecimal("0.0000");
					costTotal = new BigDecimal("0.0000");
					costTotal = invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal();
					/* Commented for New Calculation without added cost
					 * if(invoiceDateQueryList.get(i).getJoReleaseDetailId()!=null){
						addedCost = getAddedCost(invoiceDateQueryList.get(i).getJoReleaseDetailId(),startPeriodDate,endPeriodDate);
						addedCost = addedCost==null?new BigDecimal("0.0000"):addedCost;
					}*/
					costTotal = costTotal.add(addedCost);
					//vendorCost = vendorCost.add(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal());
					vendorCost = vendorCost.add(costTotal);
					/*BigDecimal vendorFreight = getVeBillDetails(invoiceDateQueryList.get(i))
							.get(0).getFreightAmount() == null ? new BigDecimal(
							0.0000) : getVeBillDetails(invoiceDateQueryList.get(i)).get(0)
							.getFreightAmount();*/
					BigDecimal vendorFreight = aVebill==null?new BigDecimal("0.0000"):aVebill.getFreightAmount()==null?new BigDecimal("0.0000"):aVebill.getFreightAmount();
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					aEcinvoiceperiod.setCuInvoiceId(invoiceDateQueryList.get(i)
							.getCuInvoiceId());
					if (aCuLinkageDetaiList.get(0).getReciptDate() != null
							&& aCuLinkageDetaiList.get(0).getReciptDate() != new Date()) {
						aEcinvoiceperiod.setDatePaid(aCuLinkageDetaiList.get(0)
								.getReciptDate());
					}
					
					aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
					aEcinvoiceperiod.setModified(false);
					if(invoiceDateQueryList.get(i).getJoReleaseType()==1){
					/*if (invoiceDateQueryList.get(i).getCostTotal() != null) {*/
						aEcinvoiceperiod.setProfit(((invoiceDateQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getSubtotal()).add(invoiceDateQueryList.get(i).getFreight()==null?new BigDecimal(0.00):invoiceDateQueryList.get(i).getFreight()))
								.subtract(vendorCost==null?new BigDecimal("0.0000"):vendorCost)); // subtotal from CustInvoice for without tax
					/*} else {
						aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
					}*/
					}
					else if(invoiceDateQueryList.get(i).getJoReleaseType()==2){
						if (invoiceDateQueryList.get(i).getCostTotal() != null) {
							aEcinvoiceperiod.setProfit(((invoiceDateQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getSubtotal()).add(invoiceDateQueryList.get(i).getFreight()==null?new BigDecimal(0.00):invoiceDateQueryList.get(i).getFreight()))
							/*.subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax*/
							.subtract(costTotal));
						} else {
							aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
						}
						}
					else if(invoiceDateQueryList.get(i).getJoReleaseType()==0){
						itsLogger.info("Release is SalesOrder");
						if (invoiceDateQueryList.get(i).getCostTotal() != null) {
							aEcinvoiceperiod.setProfit(((invoiceDateQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getSubtotal()).add(invoiceDateQueryList.get(i).getFreight()==null?new BigDecimal(0.00):invoiceDateQueryList.get(i).getFreight()))
							/*.subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax*/						
							.subtract(costTotal));	
						} else {
							aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
						}
						}
					else{
						if (invoiceDateQueryList.get(i).getCostTotal() != null) {
							aEcinvoiceperiod.setProfit((invoiceDateQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getSubtotal()).subtract(vendorCost==null? new BigDecimal("0.0000"):vendorCost)); // subtotal from CustInvoice for without tax
						} else {
							aEcinvoiceperiod.setProfit(new BigDecimal(0.000));
						}
					}
					aEcinvoiceperiod.setReference(invoiceDateQueryList.get(i)
							.getInvoiceNumber() == null ? "" : invoiceDateQueryList.get(i)
							.getInvoiceNumber());
					if (invoiceDateQueryList.get(i).getJoReleaseType() != null) {
						aEcinvoiceperiod.setReleaseType((byte) invoiceDateQueryList.get(i)
								.getJoReleaseType().intValue());
					} else {
						aEcinvoiceperiod.setReleaseType((byte) 0);
					}
					if (invoiceDateQueryList.get(i).getRxCustomerId() != null) {
						aEcinvoiceperiod.setRxCustomerId(invoiceDateQueryList.get(i)
								.getRxCustomerId());
					}
					BigDecimal subTotal = null;
					BigDecimal freight = null;

					if (invoiceDateQueryList.get(i).getSubtotal() == null) {
						subTotal = new BigDecimal(0.000);
					} else {
						subTotal = invoiceDateQueryList.get(i).getSubtotal();
					}
					if (invoiceDateQueryList.get(i).getFreight() != null) {
						freight = invoiceDateQueryList.get(i).getFreight();
					} else {
						freight = new BigDecimal(0.000);
					}
					aEcinvoiceperiod.setSales(subTotal.add(freight));
					aEcinvoiceperiod.setEnteredDate(new Date());
					aEcinvoiceperiod.setEcType("I");
					ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
					aTransaction.commit();

					theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
							Ecinvoiceperiod.class, ecInvoiceID);
					BigDecimal profit=new BigDecimal(0.000);
					BigDecimal comRate = new BigDecimal(0.00);
					//Rep Functionality
					if (aSysvariable.getValueString() != null
							&& aSysvariable.getValueString().contains("/CJ")) {
					aEcinvoices = new Ecinvoices();
					aEcstatement = new Ecstatement();
					
					if(invoiceDateQueryList.get(i).getCuAssignmentId0()!=null){
						aEcstatement = getSaveEcStatement(ecPeriodID,invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
						aEmmaster =  getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
						if(aEmmaster!=null){
						comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
						}
						else{
						comRate = new BigDecimal(0.00);
					}
					aEcinvoices.setCommissionRate(comRate);
					
					
					aEcinvoices.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
					aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
					if (invoiceDateQueryList.get(i).getCostTotal() != null) {
						aEcinvoices.setProfit(costTotal
								.subtract(vendorCost));
						profit = costTotal
								.subtract(vendorCost);
					} else {
						aEcinvoices.setProfit(new BigDecimal(0.000));
						profit = new BigDecimal(0.000);
					}
					if(comRate==null){
						comRate = new BigDecimal(0.00);
					}
					
					
					aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
					bTransaction = aSession.beginTransaction();
					bTransaction.begin();
					ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
					bTransaction.commit();
					}
				}
					}
					}
					if (aEcSplitJobEmMasterList.size() > 0) {
								for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
									theEcsplitjob= new Ecsplitjob();
									theEcsplitjob = aEcSplitJobEmMasterList.get(m);
								Emmaster ecsEmmaster = new Emmaster();
								ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
								if(ecsEmmaster!=null){
									payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
								}
								if(payCommissionAfter==null || payCommissionAfter==0){
								aEcstatement = new Ecstatement();
								aEcstatement = getSaveEcStatement(
										ecPeriodID,theEcsplitjob.getRxMasterId());

								if (aEcstatement != null) {
									bTransaction = aSession.beginTransaction();
									bTransaction.begin();
									aEcinvoicerepsplit = new Ecinvoicerepsplit();
									aEcinvoicerepsplit
											.setEcInvoicePeriodId(theEcinvoiceperiod
													.getEcInvoicePeriodId());
									aEcinvoicerepsplit.setEcStatementId(aEcstatement
											.getEcStatementId());
									aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
									if(aEmmaster!=null){
									acommissionProfit = aEmmaster.getCommissionJobProfit();
									}else{
										acommissionProfit = new BigDecimal("0.0000");
									}
									//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
									aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
									
									/*Commented on 10-04-2015
									 * if (invoiceDateQueryList.get(i).getFreight() != null
											&& invoiceDateQueryList.get(i).getFreight()
													.compareTo(new BigDecimal(0.00)) > 0) {*/
									BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
									BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
									theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
									theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
									if(acommissionProfit==null){
										acommissionProfit = new BigDecimal("0.0000");
									}
									profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
											.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
									/*} else {
										profits = theEcinvoiceperiod.getProfit();
									}*/
									
									aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
									
									aEcinvoicerepsplit.setAmountDue(profits);
									ecInvoiceRepSplitID = (Integer) aSession
											.save(aEcinvoicerepsplit);
									bTransaction.commit();
								}
							}
					}
						}
					else if (aEcSplitJobList.size() > 0) {
							for(int k=0;k<aEcSplitJobList.size();k++){
								aEcsplitjob= new Ecsplitjob();
								aEcsplitjob = aEcSplitJobList.get(k);
								Emmaster ecsEmmaster = new Emmaster();
								ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
								if(ecsEmmaster!=null){
									payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
								}
								if(payCommissionAfter==null || payCommissionAfter==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,aEcsplitjob.getRxMasterId());
							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								ecsEmmaster=null;
								ecsEmmaster = new Emmaster();
								ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
								if(ecsEmmaster!=null){
									acommissionProfit = ecsEmmaster.getCommissionJobProfit();
								}else{
									acommissionProfit = new BigDecimal("0.0000");
								}
								aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
										.getProfit());
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								BigDecimal theEcInvoicePeriodProfit = new BigDecimal("0.0000");
								BigDecimal aEcSplitJobAllocated = new BigDecimal("0.0000");
								aEcSplitJobAllocated = aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"): aEcsplitjob.getAllocated();
								theEcInvoicePeriodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal("0.0000"):theEcinvoiceperiod.getProfit();
								aEcinvoicerepsplit.setAmountDue(theEcInvoicePeriodProfit.multiply(acommissionProfit==null?new BigDecimal("0.0000"):acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply(aEcSplitJobAllocated.multiply(new BigDecimal(0.01))));
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
							}
							}
						}
						else {
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
									.get(i).getCuAssignmentId0());
							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
								aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
								if(invoiceDateQueryList.get(i).getJoReleaseType().equals("0")){
									acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
								}else{
									acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
								}
									
								}
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
										.getProfit());
								aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
										.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
												.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
										.multiply(new BigDecimal(0.01)));
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
							}
						}
					/*if (invoiceDateQueryList.get(i).getCuAssignmentId1() > 0) {
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
								.get(i).getCuAssignmentId1());
						if (aEcstatement != null) {
							bTransaction = ecSplitCommSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							acommissionProfit = getEmmasterDetails(
									invoiceDateQueryList.get(i).getCuAssignmentId1())
									.getCommissionJobProfit();
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							if (invoiceDateQueryList.get(i).getFreight() != null
									&& invoiceDateQueryList.get(i).getFreight()
											.compareTo(new BigDecimal(0.00)) > 0) {
								profits = theEcinvoiceperiod.getSales().subtract(
										invoiceDateQueryList.get(i).getFreight());
							} else {
								profits = theEcinvoiceperiod.getSales().subtract(
										vendorFreight);
							}
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							aEcinvoicerepsplit.setAmountDue(profits.multiply(
									acommissionProfit));
							ecInvoiceRepSplitID = (Integer) ecJobsInsSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
					}
					if (invoiceDateQueryList.get(i).getCuAssignmentId2() > 0) {
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
								.get(i).getCuAssignmentId2());
						if (aEcstatement != null) {
							bTransaction = ecSplitCommSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							acommissionProfit = getEmmasterDetails(
									invoiceDateQueryList.get(i).getCuAssignmentId2())
									.getCommissionJobProfit();
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							if (invoiceDateQueryList.get(i).getFreight() != null
									&& invoiceDateQueryList.get(i).getFreight()
											.compareTo(new BigDecimal(0.00)) > 0) {
								profits = theEcinvoiceperiod.getSales().subtract(
										invoiceDateQueryList.get(i).getFreight());
							} else {
								profits = theEcinvoiceperiod.getSales().subtract(
										vendorFreight);
							}
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							aEcinvoicerepsplit.setAmountDue(profits.multiply(
									acommissionProfit));
							ecInvoiceRepSplitID = (Integer) ecJobsInsSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
					}*/
				
					/* For Added Cost Calculation Starts Here */
					if(invoiceDateQueryList.get(i).getJoReleaseDetailId()!=null){
						aJoInvoiceCost = getAddedCost(invoiceDateQueryList.get(i).getJoReleaseDetailId(),startPeriodDate,endPeriodDate);
						addedCost = aJoInvoiceCost.getCost()==null?new BigDecimal("0.0000"):aJoInvoiceCost.getCost();
						addedCostDate = aJoInvoiceCost.getEnteredDate();
					}
					if(addedCost.compareTo(new BigDecimal("0.0000"))!=0){
					aEcinvoiceperiod = new Ecinvoiceperiod();
					theEcinvoiceperiod = new Ecinvoiceperiod();
					/*payCommissionAfter = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())==null?0:getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommAfterPayment();*/
					bEmmaster=null;
					bEmmaster = new Emmaster();
					bEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					if(bEmmaster!=null){
						payCommissionAfter = bEmmaster.getCommAfterPayment()==null?0:bEmmaster.getCommAfterPayment();
					}
					if(payCommissionAfter!=null && payCommissionAfter==0){
						aTransaction = aSession.beginTransaction();
						aTransaction.begin();
						aEcinvoiceperiod.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
						aEcinvoiceperiod.setDatePaid(addedCostDate);
						aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
						aEcinvoiceperiod.setModified(false);
						aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(addedCost)); // subtotal from CustInvoice for without tax
						aEcinvoiceperiod.setReference(invoiceDateQueryList.get(i).getInvoiceNumber() == null ? "" : invoiceDateQueryList.get(i).getInvoiceNumber());
						if (invoiceDateQueryList.get(i).getJoReleaseType() != null) {
							aEcinvoiceperiod.setReleaseType((byte) invoiceDateQueryList.get(i).getJoReleaseType().intValue());
						} else {
							aEcinvoiceperiod.setReleaseType((byte) 0);
						}
						if (invoiceDateQueryList.get(i).getRxCustomerId() != null) {
							aEcinvoiceperiod.setRxCustomerId(invoiceDateQueryList.get(i)
									.getRxCustomerId());
						}
						BigDecimal subTotal = null;
						BigDecimal freight = null;
			
						if (invoiceDateQueryList.get(i).getSubtotal() == null) {
							subTotal = new BigDecimal(0.000);
						} else {
							subTotal = invoiceDateQueryList.get(i).getSubtotal();
						}
						if (invoiceDateQueryList.get(i).getFreight() != null) {
							freight = invoiceDateQueryList.get(i).getFreight();
						} else {
							freight = new BigDecimal(0.000);
						}
						if(subTotal==null){subTotal= new BigDecimal("0.0000");}
						aEcinvoiceperiod.setSales(subTotal.add(freight));
						aEcinvoiceperiod.setEnteredDate(new Date());
						aEcinvoiceperiod.setEcType("A");
						ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
						aTransaction.commit();
						updateAddedCostPaid(invoiceDateQueryList.get(i).getJoReleaseDetailId(),startPeriodDate,endPeriodDate,ecInvoiceID);
						theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
								Ecinvoiceperiod.class, ecInvoiceID);
						BigDecimal profit=new BigDecimal(0.000);
						BigDecimal comRate = new BigDecimal(0.00);
						//Rep Functionality
						if (aSysvariable.getValueString() != null
								&& aSysvariable.getValueString().contains("/CJ")) {
						aEcinvoices = new Ecinvoices();
						aEcstatement = new Ecstatement();
						
						if(invoiceDateQueryList.get(i).getCuAssignmentId0()!=null){
							aEcstatement = getSaveEcStatement(ecPeriodID,invoiceDateQueryList.get(i).getCuAssignmentId0());
							aEmmaster =  getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
							comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}
							else{
							comRate = new BigDecimal(0.00);
						}
						aEcinvoices.setCommissionRate(comRate);
						
						
						aEcinvoices.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
						aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
						if (invoiceDateQueryList.get(i).getCostTotal() != null) {
							aEcinvoices.setProfit(new BigDecimal("0.0000") 
									.subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal()));
							profit = new BigDecimal("0.0000") 
							.subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal());
						} else {
							aEcinvoices.setProfit(new BigDecimal(0.000));
							profit = new BigDecimal(0.000);
						}
						if(comRate==null){
							comRate = new BigDecimal(0.00);
						}
						
						
						aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
						bTransaction.commit();
					}
				}
					}
					
					acommissionProfit = new BigDecimal(0.0000);
					profits = new BigDecimal(0.000);
					/*List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
					List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
					aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(invoiceDateQueryList.get(i).getJoMasterID(),invoiceDateQueryList.get(i).getCuInvoiceId(),"invoice");
					aEcSplitJobList = getEcSplitJobDetails(invoiceDateQueryList.get(i).getJoMasterID());*/
					itsLogger.info("SplitJob List Size::"+aEcSplitJobEmMasterList.size());
					if (aEcSplitJobEmMasterList.size() > 0) {
								for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
									theEcsplitjob= new Ecsplitjob();
									theEcsplitjob = aEcSplitJobEmMasterList.get(m);
								Emmaster ecsEmmaster = new Emmaster();
								ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
								if(ecsEmmaster!=null){
									payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
								}
								
								if(payCommissionAfter!=null && payCommissionAfter==0){
								aEcstatement = new Ecstatement();
								aEcstatement = getSaveEcStatement(
										ecPeriodID,theEcsplitjob.getRxMasterId());

								if (aEcstatement != null) {
									bTransaction = aSession.beginTransaction();
									bTransaction.begin();
									aEcinvoicerepsplit = new Ecinvoicerepsplit();
									aEcinvoicerepsplit
											.setEcInvoicePeriodId(theEcinvoiceperiod
													.getEcInvoicePeriodId());
									aEcinvoicerepsplit.setEcStatementId(aEcstatement
											.getEcStatementId());
									aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
									if(aEmmaster!=null){
										acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal("0.0000"):aEmmaster.getCommissionJobProfit();
									}
									//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
									aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
									/*Commented on 10-04-2015
									 * if (invoiceDateQueryList.get(i).getFreight() != null
											&& invoiceDateQueryList.get(i).getFreight()
													.compareTo(new BigDecimal(0.00)) > 0) {*/
									BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
									BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
									theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
									theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
									if(theEcinvoiceperiodProfit==null){theEcinvoiceperiodProfit = new BigDecimal("0.0000");}
									if(acommissionProfit==null){acommissionProfit = new BigDecimal("0.0000");}
									if(theEcsplitjobAllocated==null){theEcsplitjobAllocated = new BigDecimal("0.0000");}
									profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
											.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
									/*} else {
										profits = theEcinvoiceperiod.getProfit();
									}*/
									
									aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
									
									aEcinvoicerepsplit.setAmountDue(profits);
									ecInvoiceRepSplitID = (Integer) aSession
											.save(aEcinvoicerepsplit);
									bTransaction.commit();
								}
							}
						}
					}
					else if (aEcSplitJobList.size() > 0) {
							for(int k=0;k<aEcSplitJobList.size();k++){
								aEcsplitjob= new Ecsplitjob();
								aEcsplitjob = aEcSplitJobList.get(k);
								Emmaster ecsEmmaster = new Emmaster();
								ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
								if(ecsEmmaster!=null){
									payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
								}
								
								if(payCommissionAfter!= null && payCommissionAfter ==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,aEcsplitjob.getRxMasterId());
							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								acommissionProfit = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()).getCommissionJobProfit();
								aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
								BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
								if(theEcinvoiceperiod.getProfit()==null){
									ecInvoicePeriodProfit = new BigDecimal("0.0000");
								}else{
									ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
								}
								aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								aEcinvoicerepsplit.setAmountDue(ecInvoicePeriodProfit.multiply(acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply((aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjob.getAllocated()).multiply(new BigDecimal(0.01))));
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
							}
							}
						}
						else {
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
							/*payCommissionAfter = ecsEmmaster==null?0:ecsEmmaster.getCommAfterPayment();*/
							if(ecsEmmaster!=null){
								payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter!=0 && payCommissionAfter==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
									.get(i).getCuAssignmentId0());
							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
								aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
									if(invoiceDateQueryList.get(i).getJoReleaseType()==0){
										acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
									}else{
										acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
									}
										
									}
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
								if(theEcinvoiceperiod.getProfit()==null){
									ecInvoicePeriodProfit = new BigDecimal("0.0000");
								}else{
									ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
								}
								aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
								aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
										.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
												.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
										.multiply(new BigDecimal(0.01)));
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
							}
						}
					}
				/*Added Cost Calculation Completed*/
				}
			}
			}
			 catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				EmployeeException aEmployeeException = new EmployeeException(
						"Exception occurred : " + excep.getMessage(), excep);
				throw aEmployeeException;
			}
		}
		 catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			excep.printStackTrace();
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString=null;
			aQuery =null;
			invoiceDateQuery =null;
			aSysvariable = null;
			aQueryString = null;
			ecInvoiceID = null;
			ecInvoiceRepSplitID = null;
			ecPeriodID = null;
			aTransaction = null;
			bTransaction = null;
			aEcstatement = null;
			aEcsplitjob = null;
			theEcsplitjob = null;
			payCommissionAfter = null;
			aEmmaster = null;
			bEmmaster = null;
			invoiceDateQuery = null;
		}
		return ecInvoiceRepSplitID;
	}

	@Override
	public Integer savePaidInvoicesFromSQLView(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
		String aQueryString = null;
		Integer ecInvoiceRepSplitID = 0;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		Transaction bTransaction;
		Ecinvoiceperiod aEcinvoiceperiod = null;
		Ecinvoicerepsplit aEcinvoicerepsplit = null;
		aQueryString = "SELECT RecordType,CustomerStatus,IsCommPaid,info_cuInvoiceID,CommAvailableDate,InvoiceNumber,"
				+ "CAST(ReleaseTypes AS SIGNED),info_rxCustomerID,InvoiceAmount,CAST(CalcRawInvoiceProfit AS DECIMAL(19,4)),CommPercent,"
				+ "CAST(CalcSplitRev AS DECIMAL(19,4)) AS AmountDue,"
				+ "info_userLoginID, (SELECT CommAfterPayment FROM emMaster WHERE UserLoginID = info_userLoginID LIMIT 1) AS PayType,"
				+ "login.StatementID,Info_ReleasedetailID,CAST(CalcRevShare AS DECIMAL(19,4)),info_joReleaseID FROM SalesCommissionView "
				+ " scv LEFT JOIN (SELECT ecstm.repLoginID AS repid,ecstm.ecStatementID "
				+ "AS StatementID FROM  ecStatement ecstm WHERE ecstm.ecPeriodID = "+ecPeriodID+") AS login ON "
				+ "login.repid = scv.info_userLoginID WHERE isCommPaid='no' AND "
				+ "CommAvailableDate <= '"+endPeriodDate+" 23:59:59'";
		
		Session aSession = null;Query aQuery =null;
		try {
			Cuinvoice aCuinvoice = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			bTransaction = aSession.beginTransaction();
			bTransaction.begin();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date periodEndingDate = sdf.parse(endPeriodDate);
			itsLogger.info("Entering into Commission Calculation. .. ...");
			while (aIterator.hasNext()) { 
				String RecordType,CustomerStatus,IsCommPaid = "";
				BigDecimal CommissionPercent = new BigDecimal(0);
				BigDecimal AmountDue =  new BigDecimal(0);
				Integer ecStatementID = null;
				Integer ecInvoicePeriodID = null;
				aEcinvoiceperiod = new Ecinvoiceperiod();
				aCuinvoice = new Cuinvoice();
				Object[] aObj = (Object[]) aIterator.next();
				RecordType = (String) aObj[0];
				CustomerStatus = (String) aObj[1];
				IsCommPaid = (String) aObj[2];
				aCuinvoice.setCuInvoiceId(((BigInteger) aObj[3]).intValue());
				aCuinvoice.setInvoiceDate((Date) aObj[4]);
				aCuinvoice.setInvoiceNumber((String) aObj[5]);
				aCuinvoice.setJoReleaseType(((BigInteger) aObj[6]).intValue());
				aCuinvoice.setRxCustomerId(((BigInteger) aObj[7]).intValue());
				aCuinvoice.setInvoiceAmount((BigDecimal) aObj[8]);
				aCuinvoice.setAppliedAmount((BigDecimal) aObj[9]);
				CommissionPercent = (BigDecimal) aObj[10];
				AmountDue = (BigDecimal) aObj[11];
				aCuinvoice.setCuAssignmentId0(((BigInteger) aObj[12]).intValue());
				ecStatementID = (Integer) aObj[14];
				aCuinvoice.setJoReleaseDetailId((Integer) aObj[15]);
				
				aEcinvoiceperiod.setCuInvoiceId(aCuinvoice.getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(periodEndingDate);
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(true);
				aEcinvoiceperiod.setSales(aCuinvoice.getInvoiceAmount());
				aEcinvoiceperiod.setProfit(aCuinvoice.getAppliedAmount());
				aEcinvoiceperiod.setReference(aCuinvoice.getInvoiceNumber());
				aEcinvoiceperiod.setReleaseType((byte) aCuinvoice.getJoReleaseType().intValue());
				aEcinvoiceperiod.setRxCustomerId(aCuinvoice.getRxCustomerId());
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setJoReleaseID(JobUtil.ConvertintoInteger(String.valueOf(aObj[17])));
				if(RecordType.equals("Invoice")){
					aEcinvoiceperiod.setEcType("I");
				}else if(RecordType.equals("AddCost")){
					aEcinvoiceperiod.setEcType("A");
				}
				/*-- Code Added on 11th Nov 2015 ---- */
				else if(RecordType.equals("VendorComm"))
				{
					aEcinvoiceperiod.setEcType("V");
				//	aCuinvoice.setAppliedAmount((BigDecimal) aObj[16]);
				//	aEcinvoiceperiod.setProfit((BigDecimal) aObj[16]);
					itsLogger.info("The Profit Amount in RepSplit table is "+aObj[16]+aEcinvoiceperiod.getEcPeriodId()+aEcinvoiceperiod.getEcInvoicePeriodId());
				}
				/*--- Ends here --------------------*/
				itsLogger.info(aObj[17]+"JoReleaseID="+JobUtil.ConvertintoInteger(String.valueOf(aObj[17]))+"Issue CuInvoiceId="+aEcinvoiceperiod.getCuInvoiceId());
				ecInvoicePeriodID = (Integer) aSession.save(aEcinvoiceperiod);
				if(RecordType.equals("VendorComm")){
					updateVendorCommissionPaid(aCuinvoice.getJoReleaseDetailId(),startPeriodDate,endPeriodDate,ecInvoicePeriodID);
				}
				if(RecordType.equals("AddCost")){
					updateAddedCostPaid(aCuinvoice.getJoReleaseDetailId(),startPeriodDate,endPeriodDate,ecInvoicePeriodID);
				}
				
				aEcinvoicerepsplit = new Ecinvoicerepsplit();
				aEcinvoicerepsplit.setEcInvoicePeriodId(ecInvoicePeriodID);
				aEcinvoicerepsplit.setEcStatementId(ecStatementID);
				aEcinvoicerepsplit.setCommissionRate(CommissionPercent);
				aEcinvoicerepsplit.setProfit(aCuinvoice.getAppliedAmount());
				aEcinvoicerepsplit.setAmountDue(AmountDue);
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoicerepsplit);
			}
			bTransaction.commit();
			itsLogger.info("Commission Commit. .. ...");
			}
			 catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				EmployeeException aEmployeeException = new EmployeeException(
						"Exception occurred : " + excep.getMessage(), excep);
				throw aEmployeeException;
			 } finally {
			aSession.flush();
			aSession.close();
			aQueryString=null;
			aQuery =null;
			aQueryString = null;
			ecInvoiceRepSplitID = null;
			ecPeriodID = null;
			bTransaction = null;
		}
		return ecInvoiceRepSplitID;
	}
	
	
	/*--- Code Changes Made on 11th Nov 2015 ----- */
	public Integer updateVendorCommissionPaid(Integer joReleaseDetailID,String startPeriodDate,
			String endPeriodDate,Integer ecInvoicePeriodID) throws EmployeeException {
		String aQueryString = null;
		Integer updateStatus = 0;
		Transaction aTransaction;
		aQueryString = "UPDATE veCommDetail SET ecInvoicePeriodID ="+ecInvoicePeriodID+" WHERE joReleaseDetailID="
		+ joReleaseDetailID+" AND ShipDate <= '"+endPeriodDate+" 23:59:59' AND ecInvoicePeriodID IS NULL";
		Session aSession = null;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			aTransaction = aSession.beginTransaction();
			aSession.createSQLQuery(aQueryString).executeUpdate();
			aTransaction.commit();
			updateStatus=1;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;
			aQuery =null;
		}
		return updateStatus;
	}
	
	/*------ Ends here ------------------------------------*/
	
	@Override
	public Integer calculateAddedCostCommission(String periodStartDate,
			String periodEndDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
	Sysvariable aSysvariable = null;
	aSysvariable = getSysVariableDetails("CommissionSpecialCode");
	String aQueryString = null;
	Integer ecInvoiceID = 0;
	Integer ecInvoiceRepSplitID = 0;
	Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
	Transaction aTransaction;
	Transaction bTransaction;
	Ecstatement aEcstatement = null;
	Ecsplitjob aEcsplitjob = null;
	Ecsplitjob theEcsplitjob = null;
	Integer payCommissionAfter = null;
	Emmaster aEmmaster = null;
	Emmaster bEmmaster = null;
	Query invoiceDateQuery =null;
/*	aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,"
			+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID, "
			+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal,"
			+ "I.FreightCost,SUM(jic.cost) as Cost,jic.enteredDate FROM joInvoiceCost jic "
			+ "LEFT JOIN cuInvoice I ON jic.joReleaseDetailID = I.joReleaseDetailID "
			+ "INNER JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID "
			+ "LEFT JOIN joReleaseDetail jod ON I.joReleaseDetailID = jod.joReleaseDetailID "
			+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID "
			+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' AND eip.ecPeriodID<"+ecPeriodID
			+" GROUP BY jic.joReleaseDetailID , eip.DatePaid LIMIT 1";*/
	/*aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount, "
			+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID, "
			+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, "
			+ "I.FreightCost,SUM(cost),jic.enteredDate,jic.joInvoiceCostID FROM joInvoiceCost jic "
			+ "LEFT JOIN joReleaseDetail jod ON jic.joReleaseDetailID = jod.joReleaseDetailID "
			+ "LEFT JOIN cuInvoice I ON I.joReleaseDetailID = jod.joReleaseDetailID "
			+ "LEFT JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID "
			+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID "
			+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' AND eip.ecPeriodID<"+ecPeriodID
			+ "GROUP BY jic.joInvoiceCostID,jic.joReleaseDetailID";*/
	aQueryString = " SELECT temp.cuInvoiceID,temp.InvoiceDate,temp.InvoiceNumber, temp.prToWarehouseId, "
			+ " temp.joReleaseDetailId,temp.InvoiceAmount,temp.AppliedAmount,temp.TaxAmount, temp.Freight,temp.CostTotal,"
			+ "temp.SubTotal,temp.TaxTotal, temp.TaxRate,temp.cuAssignmentID0, temp.joMasterID, temp.ReleaseType,"
			+ "temp.rxCustomerID, temp.cuAssignmentID1,temp.cuAssignmentID2,temp.whseCostTotal, temp.FreightCost,SUM(temp.cost),"
			+ "MAX(temp.enteredDate),temp.joInvoiceCostID FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, "
			+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,"
			+ "I.SubTotal,I.TaxTotal, I.TaxRate,I.cuAssignmentID0, R.joMasterID, IF(R.ReleaseType IS NULL,0,R.ReleaseType) "
			+ "AS ReleaseType,I.rxCustomerID, I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, I.FreightCost,jic.cost,"
			+ "jic.enteredDate,jic.joInvoiceCostID FROM  cuInvoice I LEFT JOIN joInvoiceCost jic ON jic.joReleaseDetailID = "
			+ "I.joReleaseDetailID LEFT JOIN joReleaseDetail jod ON jod.joReleaseDetailID = I.joReleaseDetailID LEFT JOIN "
			+ "joRelease R ON R.joReleaseID = jod.joReleaseID LEFT JOIN (SELECT cuInvoiceID,ecPeriodID FROM ecInvoicePeriod "
			+ " GROUP BY cuInvoiceID  ) eip ON eip.cuInvoiceID = I.cuInvoiceID WHERE eip.ecPeriodID<"+ecPeriodID
			+ " AND jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' ) AS temp GROUP BY temp.joReleaseDetailID,"
			+ " DATE_FORMAT(temp.enteredDate, '%Y-%m')";
	
	Session aSession = null;Query aQuery =null;
	ArrayList<Cuinvoice> aQueryList = new ArrayList<Cuinvoice>();
	try {
		Cuinvoice aCuinvoice = null;
		Ecinvoiceperiod aEcinvoiceperiod = null;
		Ecinvoiceperiod theEcinvoiceperiod = null;
		Ecinvoicerepsplit aEcinvoicerepsplit = null;
		Ecinvoices aEcinvoices = null;
		aSession = itsSessionFactory.openSession();
		aQuery = aSession.createSQLQuery(aQueryString);
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			aCuinvoice = new Cuinvoice();
			Object[] aObj = (Object[]) aIterator.next();
			aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
			aCuinvoice.setInvoiceNumber((String) aObj[2]);
			aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
			aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
			aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
			aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
			aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
			aCuinvoice.setFreight((BigDecimal) aObj[8]);
			aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
			aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
			aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
			aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
			aCuinvoice.setJoMasterID((Integer) aObj[14]);
			//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

			aCuinvoice
					.setJoReleaseType((Integer) aObj[15]);
			aCuinvoice.setRxCustomerId((Integer) aObj[16]);
			aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
					: aObj[17]));
			aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
					: aObj[18]));
			aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
					: aObj[19]));
			aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
			: aObj[20]));
			aCuinvoice.setCostTotal((BigDecimal) aObj[21]);
			aCuinvoice.setInvoiceDate((Date) aObj[22]);
			aQueryList.add(aCuinvoice);
		}
		itsLogger.info("CuInvoice List Size: " + aQueryList.size());
		if(aQueryList.size()>0){
		for (int i = 0; i < aQueryList.size(); i++) {
			aEcinvoiceperiod = new Ecinvoiceperiod();
			theEcinvoiceperiod = new Ecinvoiceperiod();
			/*payCommissionAfter = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())==null?0:getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommAfterPayment();*/
			bEmmaster = new Emmaster();
			bEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
			if(bEmmaster!=null){
				payCommissionAfter = bEmmaster.getCommAfterPayment()==null?0:bEmmaster.getCommAfterPayment();
			}
			if(payCommissionAfter!=null && payCommissionAfter==1){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(aQueryList.get(i).getInvoiceDate());
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax
				aEcinvoiceperiod.setReference(aQueryList.get(i).getInvoiceNumber() == null ? "" : aQueryList.get(i).getInvoiceNumber());
				if (aQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) aQueryList.get(i).getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (aQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(aQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;
	
				if (aQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = aQueryList.get(i).getSubtotal();
				}
				if (aQueryList.get(i).getFreight() != null) {
					freight = aQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				if(subTotal==null){subTotal= new BigDecimal("0.0000");}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("A");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				updateAddedCostPaid(aQueryList.get(i).getJoReleaseDetailId(),periodStartDate,periodEndDate,ecInvoiceID);
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(aQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,aQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (aQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(new BigDecimal("0.0000") 
							.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal()));
					profit = new BigDecimal("0.0000") 
					.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal());
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
			}
		}
			}
			
			BigDecimal acommissionProfit = new BigDecimal(0.0000);
			BigDecimal profits = new BigDecimal(0.000);
			List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
			List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
			aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(aQueryList.get(i).getJoMasterID(),aQueryList.get(i).getCuInvoiceId(),"invoice");
			aEcSplitJobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterID());
			itsLogger.info("SplitJob List Size::"+aEcSplitJobEmMasterList.size());
			if (aEcSplitJobEmMasterList.size() > 0) {
						for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
							theEcsplitjob= new Ecsplitjob();
							theEcsplitjob = aEcSplitJobEmMasterList.get(m);
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
						if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						
						if(payCommissionAfter!=null && payCommissionAfter==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,theEcsplitjob.getRxMasterId());

						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal("0.0000"):aEmmaster.getCommissionJobProfit();
							}
							//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
							/*Commented on 10-04-2015
							 * if (aQueryList.get(i).getFreight() != null
									&& aQueryList.get(i).getFreight()
											.compareTo(new BigDecimal(0.00)) > 0) {*/
							BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
							BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
							theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
							theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
							if(theEcinvoiceperiodProfit==null){theEcinvoiceperiodProfit = new BigDecimal("0.0000");}
							if(acommissionProfit==null){acommissionProfit = new BigDecimal("0.0000");}
							if(theEcsplitjobAllocated==null){theEcsplitjobAllocated = new BigDecimal("0.0000");}
							profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
							/*} else {
								profits = theEcinvoiceperiod.getProfit();
							}*/
							
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
							
							aEcinvoicerepsplit.setAmountDue(profits);
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
					}
				}
			}
			else if (aEcSplitJobList.size() > 0) {
					for(int k=0;k<aEcSplitJobList.size();k++){
						aEcsplitjob= new Ecsplitjob();
						aEcsplitjob = aEcSplitJobList.get(k);
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
						if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						
						if(payCommissionAfter!= null && payCommissionAfter ==1){
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(
							ecPeriodID,aEcsplitjob.getRxMasterId());
					if (aEcstatement != null) {
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement
								.getEcStatementId());
						acommissionProfit = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
						BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
						if(theEcinvoiceperiod.getProfit()==null){
							ecInvoicePeriodProfit = new BigDecimal("0.0000");
						}else{
							ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
						}
						aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
						if(acommissionProfit==null){
							acommissionProfit = new BigDecimal("0.0000");
						}
						aEcinvoicerepsplit.setAmountDue(ecInvoicePeriodProfit.multiply(acommissionProfit)
								.multiply(new BigDecimal(0.01)).multiply((aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjob.getAllocated()).multiply(new BigDecimal(0.01))));
						ecInvoiceRepSplitID = (Integer) aSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
					}
					}
				}
				else {
					Emmaster ecsEmmaster = new Emmaster();
					ecsEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
					/*payCommissionAfter = ecsEmmaster==null?0:ecsEmmaster.getCommAfterPayment();*/
					if(ecsEmmaster!=null){
						payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
					}
					if(payCommissionAfter!=0 && payCommissionAfter==1){
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
							.get(i).getCuAssignmentId0());
					if (aEcstatement != null) {
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
						aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
						if(aEmmaster!=null){
							if(aQueryList.get(i).getJoReleaseType()==0){
								acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}else{
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
							}
								
							}
						if(acommissionProfit==null){
							acommissionProfit = new BigDecimal("0.0000");
						}
						aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
						BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
						if(theEcinvoiceperiod.getProfit()==null){
							ecInvoicePeriodProfit = new BigDecimal("0.0000");
						}else{
							ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
						}
						aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
						aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
								.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
										.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
								.multiply(new BigDecimal(0.01)));
						ecInvoiceRepSplitID = (Integer) aSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
					}
				}
		}
	}
		/*aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,"
				+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID, "
				+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal,"
				+ "I.FreightCost,SUM(jic.cost) as Cost,jic.enteredDate FROM joInvoiceCost jic "
				+ "LEFT JOIN cuInvoice I ON jic.joReleaseDetailID = I.joReleaseDetailID "
				+ "INNER JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID "
				+ "LEFT JOIN joReleaseDetail jod ON I.joReleaseDetailID = jod.joReleaseDetailID "
				+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID "
				+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' ""' AND eip.ecPeriodID<"+ecPeriodID+""
				+ " GROUP BY jic.joReleaseDetailID , eip.DatePaid LIMIT 1";*/
		/*aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount, "
				+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID, "
				+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, "
				+ "I.FreightCost,SUM(cost),jic.enteredDate,jic.joInvoiceCostID FROM joInvoiceCost jic "
				+ "LEFT JOIN joReleaseDetail jod ON jic.joReleaseDetailID = jod.joReleaseDetailID "
				+ "LEFT JOIN cuInvoice I ON I.joReleaseDetailID = jod.joReleaseDetailID "
				+ "LEFT JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID "
				+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID "
				+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' AND eip.ecPeriodID<"+ecPeriodID
				+ "GROUP BY jic.joInvoiceCostID,jic.joReleaseDetailID";*/
		aQueryString = " SELECT temp.cuInvoiceID,temp.InvoiceDate,temp.InvoiceNumber, temp.prToWarehouseId, "
				+ " temp.joReleaseDetailId,temp.InvoiceAmount,temp.AppliedAmount,temp.TaxAmount, temp.Freight,temp.CostTotal,"
				+ "temp.SubTotal,temp.TaxTotal, temp.TaxRate,temp.cuAssignmentID0, temp.joMasterID, temp.ReleaseType,"
				+ "temp.rxCustomerID, temp.cuAssignmentID1,temp.cuAssignmentID2,temp.whseCostTotal, temp.FreightCost,SUM(temp.cost),"
				+ "MAX(temp.enteredDate),temp.joInvoiceCostID FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, "
				+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,"
				+ "I.SubTotal,I.TaxTotal, I.TaxRate,I.cuAssignmentID0, R.joMasterID, IF(R.ReleaseType IS NULL,0,R.ReleaseType) "
				+ "AS ReleaseType,I.rxCustomerID, I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, I.FreightCost,jic.cost,"
				+ "jic.enteredDate,jic.joInvoiceCostID FROM  cuInvoice I LEFT JOIN joInvoiceCost jic ON jic.joReleaseDetailID = "
				+ "I.joReleaseDetailID LEFT JOIN joReleaseDetail jod ON jod.joReleaseDetailID = I.joReleaseDetailID LEFT JOIN "
				+ "joRelease R ON R.joReleaseID = jod.joReleaseID LEFT JOIN (SELECT cuInvoiceID,ecPeriodID FROM ecInvoicePeriod "
				+ "GROUP BY cuInvoiceID  ) eip ON eip.cuInvoiceID = I.cuInvoiceID WHERE eip.ecPeriodID<"+ecPeriodID
				+ " AND jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' ) AS temp GROUP BY temp.joReleaseDetailID,"
				+ "DATE_FORMAT(temp.enteredDate, '%Y-%m')";

				ArrayList<Cuinvoice> invoiceDateQueryList = new ArrayList<Cuinvoice>();
				try {
					aSession = itsSessionFactory.openSession();
					aQuery = aSession.createSQLQuery(aQueryString);
					aIterator = aQuery.list().iterator();
					while (aIterator.hasNext()) {
						aCuinvoice = new Cuinvoice();
						Object[] aObj = (Object[]) aIterator.next();
						aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
						aCuinvoice.setInvoiceNumber((String) aObj[2]);
						aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
						aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
						aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
						aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
						aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
						aCuinvoice.setFreight((BigDecimal) aObj[8]);
						aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
						aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
						aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
						aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
						aCuinvoice.setJoMasterID((Integer) aObj[14]);
						//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

						aCuinvoice
								.setJoReleaseType((Integer) aObj[15]);
						aCuinvoice.setRxCustomerId((Integer) aObj[16]);
						aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
								: aObj[17]));
						aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
								: aObj[18]));
						aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
								: aObj[19]));
						aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
						: aObj[20]));
						aCuinvoice.setCostTotal((BigDecimal) aObj[21]);
						aCuinvoice.setInvoiceDate((Date) aObj[22]);
				
				invoiceDateQueryList.add(aCuinvoice);
			}
			itsLogger.info("CuInvoice List Size: " + invoiceDateQueryList.size());
			if(invoiceDateQueryList.size()>0){
			for (int i = 0; i < invoiceDateQueryList.size(); i++) {
				
				aEcinvoiceperiod = new Ecinvoiceperiod();
				theEcinvoiceperiod = new Ecinvoiceperiod();
				BigDecimal acommissionProfit = new BigDecimal(0.0000);
				BigDecimal profits = new BigDecimal(0.000);
				List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
				List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
				aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(invoiceDateQueryList.get(i).getJoMasterID(),invoiceDateQueryList.get(i).getCuInvoiceId(),"invoice");
				aEcSplitJobList = getEcSplitJobDetails(invoiceDateQueryList.get(i).getJoMasterID());
				Emmaster theEmmaster = new Emmaster();
				theEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
				if(theEmmaster!=null)
					payCommissionAfter = theEmmaster.getCommAfterPayment()==null?0:theEmmaster.getCommAfterPayment();
				if(payCommissionAfter == null || payCommissionAfter==0){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(invoiceDateQueryList.get(i).getInvoiceDate());
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax
				aEcinvoiceperiod.setReference(invoiceDateQueryList.get(i)
						.getInvoiceNumber() == null ? "" : invoiceDateQueryList.get(i)
						.getInvoiceNumber());
				if (invoiceDateQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) invoiceDateQueryList.get(i)
							.getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (invoiceDateQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(invoiceDateQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;

				if (invoiceDateQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = invoiceDateQueryList.get(i).getSubtotal();
				}
				if (invoiceDateQueryList.get(i).getFreight() != null) {
					freight = invoiceDateQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("A");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				updateAddedCostPaid(invoiceDateQueryList.get(i).getJoReleaseDetailId(),periodStartDate,periodEndDate,ecInvoiceID);
				
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(invoiceDateQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (invoiceDateQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(new BigDecimal("0.0000")
							.subtract(invoiceDateQueryList.get(i).getCostTotal()));
					profit = new BigDecimal("0.0000")
							.subtract(invoiceDateQueryList.get(i).getCostTotal());
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
				}
			}
				
				}
				if (aEcSplitJobEmMasterList.size() > 0) {
							for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
								theEcsplitjob= new Ecsplitjob();
								theEcsplitjob = aEcSplitJobEmMasterList.get(m);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,theEcsplitjob.getRxMasterId());

							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
								acommissionProfit = aEmmaster.getCommissionJobProfit();
								}else{
									acommissionProfit = new BigDecimal("0.0000");
								}
								//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
								
								/*Commented on 10-04-2015
								 * if (invoiceDateQueryList.get(i).getFreight() != null
										&& invoiceDateQueryList.get(i).getFreight()
												.compareTo(new BigDecimal(0.00)) > 0) {*/
								BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
								BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
								theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
								theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
								
								aEcinvoicerepsplit.setAmountDue(profits);
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
						}
				}
					}
				else if (aEcSplitJobList.size() > 0) {
						for(int k=0;k<aEcSplitJobList.size();k++){
							aEcsplitjob= new Ecsplitjob();
							aEcsplitjob = aEcSplitJobList.get(k);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,aEcsplitjob.getRxMasterId());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(ecsEmmaster!=null){
								acommissionProfit = ecsEmmaster.getCommissionJobProfit();
							}else{
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							BigDecimal theEcInvoicePeriodProfit = new BigDecimal("0.0000");
							BigDecimal aEcSplitJobAllocated = new BigDecimal("0.0000");
							aEcSplitJobAllocated = aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"): aEcsplitjob.getAllocated();
							theEcInvoicePeriodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal("0.0000"):theEcinvoiceperiod.getProfit();
							aEcinvoicerepsplit.setAmountDue(theEcInvoicePeriodProfit.multiply(acommissionProfit==null?new BigDecimal("0.0000"):acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply(aEcSplitJobAllocated.multiply(new BigDecimal(0.01))));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
						}
					}
					else {
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
						if(ecsEmmaster!=null){
						payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						if(payCommissionAfter==null || payCommissionAfter==0){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
								.get(i).getCuAssignmentId0());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
							aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
							if(invoiceDateQueryList.get(i).getJoReleaseType().equals("0")){
								acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}else{
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
							}
								
							}
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}else{
								acommissionProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
							}
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
									.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
											.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
									.multiply(new BigDecimal(0.01)));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
					}
			}
		}
		}
		 catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		}
	}
	 catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		excep.printStackTrace();
		EmployeeException aEmployeeException = new EmployeeException(
				"Exception occurred : " + excep.getMessage(), excep);
		throw aEmployeeException;
	} finally {
		aSession.flush();
		aSession.close();
		aQueryString=null;
		aQuery =null;
		invoiceDateQuery =null;
		aSysvariable = null;
		aQueryString = null;
		ecInvoiceID = null;
		ecInvoiceRepSplitID = null;
		ecPeriodID = null;
		aTransaction = null;
		bTransaction = null;
		aEcstatement = null;
		aEcsplitjob = null;
		theEcsplitjob = null;
		payCommissionAfter = null;
		aEmmaster = null;
		bEmmaster = null;
		invoiceDateQuery = null;
	}
	return ecInvoiceRepSplitID;

	}
	
	@Override
	public Integer calculateUnpaidInvoiceAddedCost(String periodStartDate,
			String periodEndDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
	Sysvariable aSysvariable = null;
	aSysvariable = getSysVariableDetails("CommissionSpecialCode");
	String aQueryString = null;
	Integer ecInvoiceID = 0;
	Integer ecInvoiceRepSplitID = 0;
	Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
	Transaction aTransaction;
	Transaction bTransaction;
	Ecstatement aEcstatement = null;
	Ecsplitjob aEcsplitjob = null;
	Ecsplitjob theEcsplitjob = null;
	Integer payCommissionAfter = null;
	Emmaster aEmmaster = null;
	Emmaster bEmmaster = null;
	Query invoiceDateQuery =null;
	/*aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount, "
			+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID, "
			+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, "
			+ "I.FreightCost,SUM(jic.cost) AS Cost,jic.enteredDate FROM joInvoiceCost jic "
			+ "LEFT JOIN cuInvoice I ON jic.joReleaseDetailID = I.joReleaseDetailID "
			+ "LEFT JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID  "
			+ "LEFT JOIN joReleaseDetail jod ON I.joReleaseDetailID = jod.joReleaseDetailID "
			+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID  "
			+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
			+ "AND PaymentDate IS NULL AND jic.cost>0 AND eip.cuInvoiceID IS NULL  GROUP BY jic.joReleaseDetailID ";*/
	aQueryString = " SELECT temp.cuInvoiceID,temp.InvoiceDate,temp.InvoiceNumber, temp.prToWarehouseId, "
			+ " temp.joReleaseDetailId,temp.InvoiceAmount,temp.AppliedAmount,temp.TaxAmount, temp.Freight,temp.CostTotal,"
			+ "temp.SubTotal,temp.TaxTotal, temp.TaxRate,temp.cuAssignmentID0, temp.joMasterID, temp.ReleaseType,"
			+ "temp.rxCustomerID, temp.cuAssignmentID1,temp.cuAssignmentID2,temp.whseCostTotal, temp.FreightCost,SUM(temp.cost),"
			+ "MAX(temp.enteredDate),temp.joInvoiceCostID FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, "
			+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,"
			+ "I.SubTotal,I.TaxTotal, I.TaxRate,I.cuAssignmentID0, R.joMasterID, IF(R.ReleaseType IS NULL,0,R.ReleaseType) "
			+ "AS ReleaseType,I.rxCustomerID, I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, I.FreightCost,jic.cost,"
			+ "jic.enteredDate,jic.joInvoiceCostID FROM  cuInvoice I LEFT JOIN joInvoiceCost jic ON jic.joReleaseDetailID = "
			+ "I.joReleaseDetailID LEFT JOIN joReleaseDetail jod ON jod.joReleaseDetailID = I.joReleaseDetailID LEFT JOIN "
			+ "joRelease R ON R.joReleaseID = jod.joReleaseID LEFT JOIN cuLinkageDetail cud ON cud.cuInvoiceID = I.cuInvoiceID "
			+ " LEFT JOIN (SELECT cuInvoiceID,ecPeriodID FROM ecInvoicePeriod "
			+ "GROUP BY cuInvoiceID ) eip ON eip.cuInvoiceID = I.cuInvoiceID WHERE cud.DatePaid IS NULL AND "
			+ " jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
			+ " AND jic.cost>0 AND eip.cuInvoiceID IS NULL"
			+ ") AS temp GROUP BY temp.joReleaseDetailID,"
			+ "DATE_FORMAT(temp.enteredDate, '%Y-%m')";
	Session aSession = null;Query aQuery =null;
	ArrayList<Cuinvoice> aQueryList = new ArrayList<Cuinvoice>();
	try {
		Cuinvoice aCuinvoice = null;
		Ecinvoiceperiod aEcinvoiceperiod = null;
		Ecinvoiceperiod theEcinvoiceperiod = null;
		Ecinvoicerepsplit aEcinvoicerepsplit = null;
		Ecinvoices aEcinvoices = null;
		aSession = itsSessionFactory.openSession();
		aQuery = aSession.createSQLQuery(aQueryString);
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			aCuinvoice = new Cuinvoice();
			Object[] aObj = (Object[]) aIterator.next();
			aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
			aCuinvoice.setInvoiceNumber((String) aObj[2]);
			aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
			aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
			aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
			aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
			aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
			aCuinvoice.setFreight((BigDecimal) aObj[8]);
			aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
			aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
			aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
			aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
			aCuinvoice.setJoMasterID((Integer) aObj[14]);
			//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

			aCuinvoice
					.setJoReleaseType((Integer) aObj[15]);
			aCuinvoice.setRxCustomerId((Integer) aObj[16]);
			aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
					: aObj[17]));
			aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
					: aObj[18]));
			aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
					: aObj[19]));
			aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
			: aObj[20]));
			aCuinvoice.setCostTotal((BigDecimal) aObj[21]);
			aCuinvoice.setInvoiceDate((Date) aObj[22]);
			aQueryList.add(aCuinvoice);
		}
		itsLogger.info("CuInvoice List Size: " + aQueryList.size());
		if(aQueryList.size()>0){
		for (int i = 0; i < aQueryList.size(); i++) {
			aEcinvoiceperiod = new Ecinvoiceperiod();
			theEcinvoiceperiod = new Ecinvoiceperiod();
			/*payCommissionAfter = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())==null?0:getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommAfterPayment();*/
			bEmmaster = new Emmaster();
			bEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
			if(bEmmaster!=null){
				payCommissionAfter = bEmmaster.getCommAfterPayment()==null?0:bEmmaster.getCommAfterPayment();
			}
			if(payCommissionAfter!=null && payCommissionAfter==1){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(aQueryList.get(i).getInvoiceDate());
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax
				aEcinvoiceperiod.setReference(aQueryList.get(i).getInvoiceNumber() == null ? "" : aQueryList.get(i).getInvoiceNumber());
				if (aQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) aQueryList.get(i).getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (aQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(aQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;
	
				if (aQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = aQueryList.get(i).getSubtotal();
				}
				if (aQueryList.get(i).getFreight() != null) {
					freight = aQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				if(subTotal==null){subTotal= new BigDecimal("0.0000");}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("A");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				updateAddedCostPaid(aQueryList.get(i).getJoReleaseDetailId(),periodStartDate,periodEndDate,ecInvoiceID);
				
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(aQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,aQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (aQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(new BigDecimal("0.0000") 
							.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal()));
					profit = new BigDecimal("0.0000") 
					.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal());
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
			}
		}
			}
			
			BigDecimal acommissionProfit = new BigDecimal(0.0000);
			BigDecimal profits = new BigDecimal(0.000);
			List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
			List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
			aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(aQueryList.get(i).getJoMasterID(),aQueryList.get(i).getCuInvoiceId(),"invoice");
			aEcSplitJobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterID());
			itsLogger.info("SplitJob List Size::"+aEcSplitJobEmMasterList.size());
			if (aEcSplitJobEmMasterList.size() > 0) {
						for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
							theEcsplitjob= new Ecsplitjob();
							theEcsplitjob = aEcSplitJobEmMasterList.get(m);
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
						if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						
						if(payCommissionAfter!=null && payCommissionAfter==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,theEcsplitjob.getRxMasterId());

						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal("0.0000"):aEmmaster.getCommissionJobProfit();
							}
							//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
							/*Commented on 10-04-2015
							 * if (aQueryList.get(i).getFreight() != null
									&& aQueryList.get(i).getFreight()
											.compareTo(new BigDecimal(0.00)) > 0) {*/
							BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
							BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
							theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
							theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
							if(theEcinvoiceperiodProfit==null){theEcinvoiceperiodProfit = new BigDecimal("0.0000");}
							if(acommissionProfit==null){acommissionProfit = new BigDecimal("0.0000");}
							if(theEcsplitjobAllocated==null){theEcsplitjobAllocated = new BigDecimal("0.0000");}
							profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
							/*} else {
								profits = theEcinvoiceperiod.getProfit();
							}*/
							
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
							
							aEcinvoicerepsplit.setAmountDue(profits);
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
					}
				}
			}
			else if (aEcSplitJobList.size() > 0) {
					for(int k=0;k<aEcSplitJobList.size();k++){
						aEcsplitjob= new Ecsplitjob();
						aEcsplitjob = aEcSplitJobList.get(k);
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
						if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						
						if(payCommissionAfter!= null && payCommissionAfter ==1){
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(
							ecPeriodID,aEcsplitjob.getRxMasterId());
					if (aEcstatement != null) {
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement
								.getEcStatementId());
						acommissionProfit = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
						BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
						if(theEcinvoiceperiod.getProfit()==null){
							ecInvoicePeriodProfit = new BigDecimal("0.0000");
						}else{
							ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
						}
						aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
						if(acommissionProfit==null){
							acommissionProfit = new BigDecimal("0.0000");
						}
						aEcinvoicerepsplit.setAmountDue(ecInvoicePeriodProfit.multiply(acommissionProfit)
								.multiply(new BigDecimal(0.01)).multiply((aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjob.getAllocated()).multiply(new BigDecimal(0.01))));
						ecInvoiceRepSplitID = (Integer) aSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
					}
					}
				}
				else {
					Emmaster ecsEmmaster = new Emmaster();
					ecsEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
					/*payCommissionAfter = ecsEmmaster==null?0:ecsEmmaster.getCommAfterPayment();*/
					if(ecsEmmaster!=null){
						payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
					}
					if(payCommissionAfter!=0 && payCommissionAfter==1){
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
							.get(i).getCuAssignmentId0());
					if (aEcstatement != null) {
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
						aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
						if(aEmmaster!=null){
							if(aQueryList.get(i).getJoReleaseType()==0){
								acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}else{
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
							}
								
							}
						if(acommissionProfit==null){
							acommissionProfit = new BigDecimal("0.0000");
						}
						aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
						BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
						if(theEcinvoiceperiod.getProfit()==null){
							ecInvoicePeriodProfit = new BigDecimal("0.0000");
						}else{
							ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
						}
						aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
						aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
								.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
										.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
								.multiply(new BigDecimal(0.01)));
						ecInvoiceRepSplitID = (Integer) aSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
					}
				}
		}
	}
		/*aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount, "
				+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID, "
				+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, "
				+ "I.FreightCost,SUM(jic.cost) AS Cost,jic.enteredDate FROM joInvoiceCost jic "
				+ "LEFT JOIN cuInvoice I ON jic.joReleaseDetailID = I.joReleaseDetailID "
				+ "LEFT JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID  "
				+ "LEFT JOIN joReleaseDetail jod ON I.joReleaseDetailID = jod.joReleaseDetailID "
				+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID  "
				+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
				+ "AND PaymentDate IS NULL AND jic.cost>0 AND eip.cuInvoiceID IS NULL  GROUP BY jic.joReleaseDetailID ";*/
		aQueryString = " SELECT temp.cuInvoiceID,temp.InvoiceDate,temp.InvoiceNumber, temp.prToWarehouseId, "
				+ " temp.joReleaseDetailId,temp.InvoiceAmount,temp.AppliedAmount,temp.TaxAmount, temp.Freight,temp.CostTotal,"
				+ "temp.SubTotal,temp.TaxTotal, temp.TaxRate,temp.cuAssignmentID0, temp.joMasterID, temp.ReleaseType,"
				+ "temp.rxCustomerID, temp.cuAssignmentID1,temp.cuAssignmentID2,temp.whseCostTotal, temp.FreightCost,SUM(temp.cost),"
				+ "MAX(temp.enteredDate),temp.joInvoiceCostID FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, "
				+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,"
				+ "I.SubTotal,I.TaxTotal, I.TaxRate,I.cuAssignmentID0, R.joMasterID, IF(R.ReleaseType IS NULL,0,R.ReleaseType) "
				+ "AS ReleaseType,I.rxCustomerID, I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, I.FreightCost,jic.cost,"
				+ "jic.enteredDate,jic.joInvoiceCostID FROM  cuInvoice I LEFT JOIN joInvoiceCost jic ON jic.joReleaseDetailID = "
				+ "I.joReleaseDetailID LEFT JOIN joReleaseDetail jod ON jod.joReleaseDetailID = I.joReleaseDetailID LEFT JOIN "
				+ "joRelease R ON R.joReleaseID = jod.joReleaseID LEFT JOIN cuLinkageDetail cud ON cud.cuInvoiceID = I.cuInvoiceID"
				+ " LEFT JOIN (SELECT cuInvoiceID,ecPeriodID FROM ecInvoicePeriod "
				+ "GROUP BY cuInvoiceID  ) eip ON eip.cuInvoiceID = I.cuInvoiceID WHERE cud.DatePaid IS NULL AND "
				+ " jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
				+ " AND jic.cost>0 AND eip.cuInvoiceID IS NULL"
				+ ") AS temp GROUP BY temp.joReleaseDetailID,"
				+ "DATE_FORMAT(temp.enteredDate, '%Y-%m')";
				ArrayList<Cuinvoice> invoiceDateQueryList = new ArrayList<Cuinvoice>();
				try {
					aSession = itsSessionFactory.openSession();
					aQuery = aSession.createSQLQuery(aQueryString);
					aIterator = aQuery.list().iterator();
					while (aIterator.hasNext()) {
						aCuinvoice = new Cuinvoice();
						Object[] aObj = (Object[]) aIterator.next();
						aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
						aCuinvoice.setInvoiceNumber((String) aObj[2]);
						aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
						aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
						aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
						aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
						aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
						aCuinvoice.setFreight((BigDecimal) aObj[8]);
						aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
						aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
						aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
						aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
						aCuinvoice.setJoMasterID((Integer) aObj[14]);
						//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

						aCuinvoice
								.setJoReleaseType((Integer) aObj[15]);
						aCuinvoice.setRxCustomerId((Integer) aObj[16]);
						aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
								: aObj[17]));
						aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
								: aObj[18]));
						aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
								: aObj[19]));
						aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
						: aObj[20]));
						aCuinvoice.setCostTotal((BigDecimal) aObj[21]);
						aCuinvoice.setInvoiceDate((Date) aObj[22]);
				
				invoiceDateQueryList.add(aCuinvoice);
			}
			itsLogger.info("CuInvoice List Size: " + invoiceDateQueryList.size());
			if(invoiceDateQueryList.size()>0){
			for (int i = 0; i < invoiceDateQueryList.size(); i++) {
				
				aEcinvoiceperiod = new Ecinvoiceperiod();
				theEcinvoiceperiod = new Ecinvoiceperiod();
				BigDecimal acommissionProfit = new BigDecimal(0.0000);
				BigDecimal profits = new BigDecimal(0.000);
				List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
				List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
				aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(invoiceDateQueryList.get(i).getJoMasterID(),invoiceDateQueryList.get(i).getCuInvoiceId(),"invoice");
				aEcSplitJobList = getEcSplitJobDetails(invoiceDateQueryList.get(i).getJoMasterID());
				Emmaster theEmmaster = new Emmaster();
				theEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
				if(theEmmaster!=null)
					payCommissionAfter = theEmmaster.getCommAfterPayment()==null?0:theEmmaster.getCommAfterPayment();
				if(payCommissionAfter == null || payCommissionAfter==0){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(invoiceDateQueryList.get(i).getInvoiceDate());
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax
				aEcinvoiceperiod.setReference(invoiceDateQueryList.get(i)
						.getInvoiceNumber() == null ? "" : invoiceDateQueryList.get(i)
						.getInvoiceNumber());
				if (invoiceDateQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) invoiceDateQueryList.get(i)
							.getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (invoiceDateQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(invoiceDateQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;

				if (invoiceDateQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = invoiceDateQueryList.get(i).getSubtotal();
				}
				if (invoiceDateQueryList.get(i).getFreight() != null) {
					freight = invoiceDateQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("A");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				updateAddedCostPaid(invoiceDateQueryList.get(i).getJoReleaseDetailId(),periodStartDate,periodEndDate,ecInvoiceID);
				
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(invoiceDateQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (invoiceDateQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(new BigDecimal("0.0000")
							.subtract(invoiceDateQueryList.get(i).getCostTotal()));
					profit = new BigDecimal("0.0000")
							.subtract(invoiceDateQueryList.get(i).getCostTotal());
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
				}
			}
				
				}
				if (aEcSplitJobEmMasterList.size() > 0) {
							for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
								theEcsplitjob= new Ecsplitjob();
								theEcsplitjob = aEcSplitJobEmMasterList.get(m);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,theEcsplitjob.getRxMasterId());

							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
								acommissionProfit = aEmmaster.getCommissionJobProfit();
								}else{
									acommissionProfit = new BigDecimal("0.0000");
								}
								//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
								
								/*Commented on 10-04-2015
								 * if (invoiceDateQueryList.get(i).getFreight() != null
										&& invoiceDateQueryList.get(i).getFreight()
												.compareTo(new BigDecimal(0.00)) > 0) {*/
								BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
								BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
								theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
								theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
								
								aEcinvoicerepsplit.setAmountDue(profits);
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
						}
				}
					}
				else if (aEcSplitJobList.size() > 0) {
						for(int k=0;k<aEcSplitJobList.size();k++){
							aEcsplitjob= new Ecsplitjob();
							aEcsplitjob = aEcSplitJobList.get(k);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,aEcsplitjob.getRxMasterId());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(ecsEmmaster!=null){
								acommissionProfit = ecsEmmaster.getCommissionJobProfit();
							}else{
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							BigDecimal theEcInvoicePeriodProfit = new BigDecimal("0.0000");
							BigDecimal aEcSplitJobAllocated = new BigDecimal("0.0000");
							aEcSplitJobAllocated = aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"): aEcsplitjob.getAllocated();
							theEcInvoicePeriodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal("0.0000"):theEcinvoiceperiod.getProfit();
							aEcinvoicerepsplit.setAmountDue(theEcInvoicePeriodProfit.multiply(acommissionProfit==null?new BigDecimal("0.0000"):acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply(aEcSplitJobAllocated.multiply(new BigDecimal(0.01))));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
						}
					}
					else {
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
						if(ecsEmmaster!=null){
						payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						if(payCommissionAfter==null || payCommissionAfter==0){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
								.get(i).getCuAssignmentId0());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
							aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
							if(invoiceDateQueryList.get(i).getJoReleaseType().equals("0")){
								acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}else{
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
							}
								
							}
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}else{
								acommissionProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
							}
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
									.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
											.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
									.multiply(new BigDecimal(0.01)));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
					}
			}
		}
		}
		 catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		}
	}
	 catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		excep.printStackTrace();
		EmployeeException aEmployeeException = new EmployeeException(
				"Exception occurred : " + excep.getMessage(), excep);
		throw aEmployeeException;
	} finally {
		aSession.flush();
		aSession.close();
		aQueryString=null;
		aQuery =null;
		invoiceDateQuery =null;
		aSysvariable = null;
		aQueryString = null;
		ecInvoiceID = null;
		ecInvoiceRepSplitID = null;
		ecPeriodID = null;
		aTransaction = null;
		bTransaction = null;
		aEcstatement = null;
		aEcsplitjob = null;
		theEcsplitjob = null;
		payCommissionAfter = null;
		aEmmaster = null;
		bEmmaster = null;
		invoiceDateQuery = null;
	}
	return ecInvoiceRepSplitID;

	}
	
	@Override
	public Integer calculatePartialPaidInvoiceAddedCost(String periodStartDate,
			String periodEndDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
	Sysvariable aSysvariable = null;
	aSysvariable = getSysVariableDetails("CommissionSpecialCode");
	String aQueryString = null;
	Integer ecInvoiceID = 0;
	Integer ecInvoiceRepSplitID = 0;
	Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
	Transaction aTransaction;
	Transaction bTransaction;
	Ecstatement aEcstatement = null;
	Ecsplitjob aEcsplitjob = null;
	Ecsplitjob theEcsplitjob = null;
	Integer payCommissionAfter = null;
	Emmaster aEmmaster = null;
	Emmaster bEmmaster = null;
	Query invoiceDateQuery =null;
/*	aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount, "
			+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID,"
			+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal,"
			+ "I.FreightCost,SUM(jic.cost) AS Cost,jic.enteredDate FROM joInvoiceCost jic "
			+ "LEFT JOIN cuInvoice I ON jic.joReleaseDetailID = I.joReleaseDetailID "
			+ "LEFT JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID "
			+ "LEFT JOIN joReleaseDetail jod ON I.joReleaseDetailID = jod.joReleaseDetailID "
			+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID " 
			+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
			+ "AND PaymentDate IS NOT NULL AND jic.cost>0 AND eip.cuInvoiceID IS NULL  GROUP BY jic.joReleaseDetailID";*/
	aQueryString = " SELECT temp.cuInvoiceID,temp.InvoiceDate,temp.InvoiceNumber, temp.prToWarehouseId, "
			+ " temp.joReleaseDetailId,temp.InvoiceAmount,temp.AppliedAmount,temp.TaxAmount, temp.Freight,temp.CostTotal,"
			+ "temp.SubTotal,temp.TaxTotal, temp.TaxRate,temp.cuAssignmentID0, temp.joMasterID, temp.ReleaseType,"
			+ "temp.rxCustomerID, temp.cuAssignmentID1,temp.cuAssignmentID2,temp.whseCostTotal, temp.FreightCost,SUM(temp.cost),"
			+ "MAX(temp.enteredDate),temp.joInvoiceCostID FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, "
			+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,"
			+ "I.SubTotal,I.TaxTotal, I.TaxRate,I.cuAssignmentID0, R.joMasterID, IF(R.ReleaseType IS NULL,0,R.ReleaseType) "
			+ "AS ReleaseType,I.rxCustomerID, I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, I.FreightCost,jic.cost,"
			+ "jic.enteredDate,jic.joInvoiceCostID FROM  cuInvoice I LEFT JOIN joInvoiceCost jic ON jic.joReleaseDetailID = "
			+ "I.joReleaseDetailID LEFT JOIN joReleaseDetail jod ON jod.joReleaseDetailID = I.joReleaseDetailID LEFT JOIN "
			+ "joRelease R ON R.joReleaseID = jod.joReleaseID LEFT JOIN cuLinkageDetail cud ON cud.cuInvoiceID = I.cuInvoiceID "
			+ " LEFT JOIN (SELECT cuInvoiceID,ecPeriodID FROM ecInvoicePeriod "
			+ "GROUP BY cuInvoiceID  ) eip ON eip.cuInvoiceID = I.cuInvoiceID WHERE cud.DatePaid IS NOT NULL AND "
			+ " jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
			+ " AND jic.cost>0 AND eip.cuInvoiceID IS NULL"
			+ ") AS temp GROUP BY temp.joReleaseDetailID,"
			+ "DATE_FORMAT(temp.enteredDate, '%Y-%m')";
	Session aSession = null;Query aQuery =null;
	ArrayList<Cuinvoice> aQueryList = new ArrayList<Cuinvoice>();
	try {
		Cuinvoice aCuinvoice = null;
		Ecinvoiceperiod aEcinvoiceperiod = null;
		Ecinvoiceperiod theEcinvoiceperiod = null;
		Ecinvoicerepsplit aEcinvoicerepsplit = null;
		Ecinvoices aEcinvoices = null;
		aSession = itsSessionFactory.openSession();
		aQuery = aSession.createSQLQuery(aQueryString);
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			aCuinvoice = new Cuinvoice();
			Object[] aObj = (Object[]) aIterator.next();
			aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
			aCuinvoice.setInvoiceNumber((String) aObj[2]);
			aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
			aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
			aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
			aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
			aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
			aCuinvoice.setFreight((BigDecimal) aObj[8]);
			aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
			aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
			aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
			aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
			aCuinvoice.setJoMasterID((Integer) aObj[14]);
			//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

			aCuinvoice
					.setJoReleaseType((Integer) aObj[15]);
			aCuinvoice.setRxCustomerId((Integer) aObj[16]);
			aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
					: aObj[17]));
			aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
					: aObj[18]));
			aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
					: aObj[19]));
			aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
			: aObj[20]));
			aCuinvoice.setCostTotal((BigDecimal) aObj[21]);
			aCuinvoice.setInvoiceDate((Date) aObj[22]);
			aQueryList.add(aCuinvoice);
		}
		itsLogger.info("CuInvoice List Size: " + aQueryList.size());
		if(aQueryList.size()>0){
		for (int i = 0; i < aQueryList.size(); i++) {
			aEcinvoiceperiod = new Ecinvoiceperiod();
			theEcinvoiceperiod = new Ecinvoiceperiod();
			/*payCommissionAfter = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())==null?0:getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommAfterPayment();*/
			bEmmaster = new Emmaster();
			bEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
			if(bEmmaster!=null){
				payCommissionAfter = bEmmaster.getCommAfterPayment()==null?0:bEmmaster.getCommAfterPayment();
			}
			if(payCommissionAfter!=null && payCommissionAfter==1){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(aQueryList.get(i).getInvoiceDate());
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax
				aEcinvoiceperiod.setReference(aQueryList.get(i).getInvoiceNumber() == null ? "" : aQueryList.get(i).getInvoiceNumber());
				if (aQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) aQueryList.get(i).getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (aQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(aQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;
	
				if (aQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = aQueryList.get(i).getSubtotal();
				}
				if (aQueryList.get(i).getFreight() != null) {
					freight = aQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				if(subTotal==null){subTotal= new BigDecimal("0.0000");}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("A");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				updateAddedCostPaid(aQueryList.get(i).getJoReleaseDetailId(),periodStartDate,periodEndDate,ecInvoiceID);
				
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(aQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,aQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (aQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(new BigDecimal("0.0000") 
							.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal()));
					profit = new BigDecimal("0.0000") 
					.subtract(aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal());
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
			}
		}
			}
			
			BigDecimal acommissionProfit = new BigDecimal(0.0000);
			BigDecimal profits = new BigDecimal(0.000);
			List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
			List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
			aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(aQueryList.get(i).getJoMasterID(),aQueryList.get(i).getCuInvoiceId(),"invoice");
			aEcSplitJobList = getEcSplitJobDetails(aQueryList.get(i).getJoMasterID());
			itsLogger.info("SplitJob List Size::"+aEcSplitJobEmMasterList.size());
			if (aEcSplitJobEmMasterList.size() > 0) {
						for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
							theEcsplitjob= new Ecsplitjob();
							theEcsplitjob = aEcSplitJobEmMasterList.get(m);
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
						if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						
						if(payCommissionAfter!=null && payCommissionAfter==1){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,theEcsplitjob.getRxMasterId());

						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal("0.0000"):aEmmaster.getCommissionJobProfit();
							}
							//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
							/*Commented on 10-04-2015
							 * if (aQueryList.get(i).getFreight() != null
									&& aQueryList.get(i).getFreight()
											.compareTo(new BigDecimal(0.00)) > 0) {*/
							BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
							BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
							theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
							theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
							if(theEcinvoiceperiodProfit==null){theEcinvoiceperiodProfit = new BigDecimal("0.0000");}
							if(acommissionProfit==null){acommissionProfit = new BigDecimal("0.0000");}
							if(theEcsplitjobAllocated==null){theEcsplitjobAllocated = new BigDecimal("0.0000");}
							profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
							/*} else {
								profits = theEcinvoiceperiod.getProfit();
							}*/
							
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
							
							aEcinvoicerepsplit.setAmountDue(profits);
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
					}
				}
			}
			else if (aEcSplitJobList.size() > 0) {
					for(int k=0;k<aEcSplitJobList.size();k++){
						aEcsplitjob= new Ecsplitjob();
						aEcsplitjob = aEcSplitJobList.get(k);
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
						if(ecsEmmaster!=null){
							payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						
						if(payCommissionAfter!= null && payCommissionAfter ==1){
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(
							ecPeriodID,aEcsplitjob.getRxMasterId());
					if (aEcstatement != null) {
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement
								.getEcStatementId());
						acommissionProfit = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()).getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
						BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
						if(theEcinvoiceperiod.getProfit()==null){
							ecInvoicePeriodProfit = new BigDecimal("0.0000");
						}else{
							ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
						}
						aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
						if(acommissionProfit==null){
							acommissionProfit = new BigDecimal("0.0000");
						}
						aEcinvoicerepsplit.setAmountDue(ecInvoicePeriodProfit.multiply(acommissionProfit)
								.multiply(new BigDecimal(0.01)).multiply((aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"):aEcsplitjob.getAllocated()).multiply(new BigDecimal(0.01))));
						ecInvoiceRepSplitID = (Integer) aSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
					}
					}
				}
				else {
					Emmaster ecsEmmaster = new Emmaster();
					ecsEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0()==null?0:aQueryList.get(i).getCuAssignmentId0());
					/*payCommissionAfter = ecsEmmaster==null?0:ecsEmmaster.getCommAfterPayment();*/
					if(ecsEmmaster!=null){
						payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
					}
					if(payCommissionAfter!=0 && payCommissionAfter==1){
					aEcstatement = new Ecstatement();
					aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList
							.get(i).getCuAssignmentId0());
					if (aEcstatement != null) {
						bTransaction = aSession.beginTransaction();
						bTransaction.begin();
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit
								.setEcInvoicePeriodId(theEcinvoiceperiod
										.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
						aEmmaster = getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0());
						if(aEmmaster!=null){
							if(aQueryList.get(i).getJoReleaseType()==0){
								acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}else{
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
							}
								
							}
						if(acommissionProfit==null){
							acommissionProfit = new BigDecimal("0.0000");
						}
						aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
						BigDecimal ecInvoicePeriodProfit = new BigDecimal("0.0000");
						if(theEcinvoiceperiod.getProfit()==null){
							ecInvoicePeriodProfit = new BigDecimal("0.0000");
						}else{
							ecInvoicePeriodProfit = theEcinvoiceperiod.getProfit();
						}
						aEcinvoicerepsplit.setProfit(ecInvoicePeriodProfit);
						aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
								.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
										.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
								.multiply(new BigDecimal(0.01)));
						ecInvoiceRepSplitID = (Integer) aSession
								.save(aEcinvoicerepsplit);
						bTransaction.commit();
					}
					}
				}
		}
	}
		/*aQueryString = "SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount, "
				+ "I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,I.SubTotal,I.TaxTotal,I.TaxRate,I.cuAssignmentID0, R.joMasterID,"
				+ "IF(R.ReleaseType IS NULL,0,R.ReleaseType),I.rxCustomerID,I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal,"
				+ "I.FreightCost,SUM(jic.cost) AS Cost,jic.enteredDate FROM joInvoiceCost jic "
				+ "LEFT JOIN cuInvoice I ON jic.joReleaseDetailID = I.joReleaseDetailID "
				+ "LEFT JOIN ecInvoicePeriod eip ON eip.cuInvoiceID = I.cuInvoiceID "
				+ "LEFT JOIN joReleaseDetail jod ON I.joReleaseDetailID = jod.joReleaseDetailID "
				+ "LEFT JOIN joRelease R ON R.joReleaseID = jod.joReleaseID " 
				+ "WHERE jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
				+ "AND PaymentDate IS NOT NULL AND jic.cost>0 AND eip.cuInvoiceID IS NULL  GROUP BY jic.joReleaseDetailID";*/
		aQueryString = " SELECT temp.cuInvoiceID,temp.InvoiceDate,temp.InvoiceNumber, temp.prToWarehouseId, "
				+ " temp.joReleaseDetailId,temp.InvoiceAmount,temp.AppliedAmount,temp.TaxAmount, temp.Freight,temp.CostTotal,"
				+ "temp.SubTotal,temp.TaxTotal, temp.TaxRate,temp.cuAssignmentID0, temp.joMasterID, temp.ReleaseType,"
				+ "temp.rxCustomerID, temp.cuAssignmentID1,temp.cuAssignmentID2,temp.whseCostTotal, temp.FreightCost,SUM(temp.cost),"
				+ "MAX(temp.enteredDate),temp.joInvoiceCostID FROM (SELECT I.cuInvoiceID,I.InvoiceDate,I.InvoiceNumber, "
				+ "I.prToWarehouseId,I.joReleaseDetailId,I.InvoiceAmount,I.AppliedAmount,I.TaxAmount, I.Freight,I.CostTotal,"
				+ "I.SubTotal,I.TaxTotal, I.TaxRate,I.cuAssignmentID0, R.joMasterID, IF(R.ReleaseType IS NULL,0,R.ReleaseType) "
				+ "AS ReleaseType,I.rxCustomerID, I.cuAssignmentID1,I.cuAssignmentID2,I.whseCostTotal, I.FreightCost,jic.cost,"
				+ "jic.enteredDate,jic.joInvoiceCostID FROM  cuInvoice I LEFT JOIN joInvoiceCost jic ON jic.joReleaseDetailID = "
				+ "I.joReleaseDetailID LEFT JOIN joReleaseDetail jod ON jod.joReleaseDetailID = I.joReleaseDetailID LEFT JOIN "
				+ "joRelease R ON R.joReleaseID = jod.joReleaseID LEFT JOIN cuLinkageDetail cud ON cud.cuInvoiceID = I.cuInvoiceID"
				+ " LEFT JOIN (SELECT cuInvoiceID,ecPeriodID FROM ecInvoicePeriod "
				+ "GROUP BY cuInvoiceID  ) eip ON eip.cuInvoiceID = I.cuInvoiceID WHERE cud.DatePaid IS NOT NULL AND "
				+ " jic.enteredDate BETWEEN '"+periodStartDate+"' AND '"+periodEndDate+"' "
				+ " AND jic.cost>0 AND eip.cuInvoiceID IS NULL"
				+ ") AS temp GROUP BY temp.joReleaseDetailID,"
				+ "DATE_FORMAT(temp.enteredDate, '%Y-%m')";
				ArrayList<Cuinvoice> invoiceDateQueryList = new ArrayList<Cuinvoice>();
				try {
					aSession = itsSessionFactory.openSession();
					aQuery = aSession.createSQLQuery(aQueryString);
					aIterator = aQuery.list().iterator();
					while (aIterator.hasNext()) {
						aCuinvoice = new Cuinvoice();
						Object[] aObj = (Object[]) aIterator.next();
						aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
						aCuinvoice.setInvoiceNumber((String) aObj[2]);
						aCuinvoice.setPrToWarehouseId((Integer) aObj[3]);
						aCuinvoice.setJoReleaseDetailId((Integer) aObj[4]);
						aCuinvoice.setInvoiceAmount((BigDecimal) aObj[5]);
						aCuinvoice.setAppliedAmount((BigDecimal) aObj[6]);
						aCuinvoice.setTaxAmount((BigDecimal) aObj[7]);
						aCuinvoice.setFreight((BigDecimal) aObj[8]);
						aCuinvoice.setSubtotal((BigDecimal) aObj[10]);
						aCuinvoice.setTaxTotal((BigDecimal) aObj[11]);
						aCuinvoice.setTaxRate((BigDecimal) aObj[12]);
						aCuinvoice.setCuAssignmentId0((Integer) aObj[13]);
						aCuinvoice.setJoMasterID((Integer) aObj[14]);
						//short releaseType = (Short) (aObj[15]==null?0:aObj[15]);

						aCuinvoice
								.setJoReleaseType((Integer) aObj[15]);
						aCuinvoice.setRxCustomerId((Integer) aObj[16]);
						aCuinvoice.setCuAssignmentId1((Integer) (aObj[17] == null ? 0
								: aObj[17]));
						aCuinvoice.setCuAssignmentId2((Integer) (aObj[18] == null ? 0
								: aObj[18]));
						aCuinvoice.setWhseCostTotal((BigDecimal) (aObj[19] == null ? new BigDecimal("0.0000")
								: aObj[19]));
						aCuinvoice.setFreightCost((BigDecimal) (aObj[20] == null ? new BigDecimal("0.0000")
						: aObj[20]));
						aCuinvoice.setCostTotal((BigDecimal) aObj[21]);
						aCuinvoice.setInvoiceDate((Date) aObj[22]);
				
				invoiceDateQueryList.add(aCuinvoice);
			}
			itsLogger.info("CuInvoice List Size: " + invoiceDateQueryList.size());
			if(invoiceDateQueryList.size()>0){
			for (int i = 0; i < invoiceDateQueryList.size(); i++) {
				
				aEcinvoiceperiod = new Ecinvoiceperiod();
				theEcinvoiceperiod = new Ecinvoiceperiod();
				BigDecimal acommissionProfit = new BigDecimal(0.0000);
				BigDecimal profits = new BigDecimal(0.000);
				List<Ecsplitjob> aEcSplitJobList = new ArrayList<Ecsplitjob>();
				List<Ecsplitjob> aEcSplitJobEmMasterList = new ArrayList<Ecsplitjob>();
				aEcSplitJobEmMasterList = getEcSplitJobEmMasterDetails(invoiceDateQueryList.get(i).getJoMasterID(),invoiceDateQueryList.get(i).getCuInvoiceId(),"invoice");
				aEcSplitJobList = getEcSplitJobDetails(invoiceDateQueryList.get(i).getJoMasterID());
				Emmaster theEmmaster = new Emmaster();
				theEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
				if(theEmmaster!=null)
					payCommissionAfter = theEmmaster.getCommAfterPayment()==null?0:theEmmaster.getCommAfterPayment();
				if(payCommissionAfter == null || payCommissionAfter==0){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
				aEcinvoiceperiod.setDatePaid(invoiceDateQueryList.get(i).getInvoiceDate());
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setProfit(new BigDecimal("0.0000").subtract(invoiceDateQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):invoiceDateQueryList.get(i).getCostTotal())); // subtotal from CustInvoice for without tax
				aEcinvoiceperiod.setReference(invoiceDateQueryList.get(i)
						.getInvoiceNumber() == null ? "" : invoiceDateQueryList.get(i)
						.getInvoiceNumber());
				if (invoiceDateQueryList.get(i).getJoReleaseType() != null) {
					aEcinvoiceperiod.setReleaseType((byte) invoiceDateQueryList.get(i)
							.getJoReleaseType().intValue());
				} else {
					aEcinvoiceperiod.setReleaseType((byte) 0);
				}
				if (invoiceDateQueryList.get(i).getRxCustomerId() != null) {
					aEcinvoiceperiod.setRxCustomerId(invoiceDateQueryList.get(i)
							.getRxCustomerId());
				}
				BigDecimal subTotal = null;
				BigDecimal freight = null;

				if (invoiceDateQueryList.get(i).getSubtotal() == null) {
					subTotal = new BigDecimal(0.000);
				} else {
					subTotal = invoiceDateQueryList.get(i).getSubtotal();
				}
				if (invoiceDateQueryList.get(i).getFreight() != null) {
					freight = invoiceDateQueryList.get(i).getFreight();
				} else {
					freight = new BigDecimal(0.000);
				}
				aEcinvoiceperiod.setSales(subTotal.add(freight));
				aEcinvoiceperiod.setEnteredDate(new Date());
				aEcinvoiceperiod.setEcType("A");
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				updateAddedCostPaid(invoiceDateQueryList.get(i).getJoReleaseDetailId(),periodStartDate,periodEndDate,ecInvoiceID);
				
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				BigDecimal profit=new BigDecimal(0.000);
				BigDecimal comRate = new BigDecimal(0.00);
				//Rep Functionality
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) {
				aEcinvoices = new Ecinvoices();
				aEcstatement = new Ecstatement();
				
				if(invoiceDateQueryList.get(i).getCuAssignmentId0()!=null){
					aEcstatement = getSaveEcStatement(ecPeriodID,invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					aEmmaster =  getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
					if(aEmmaster!=null){
					comRate = aEmmaster.getCommissionInvoiceProfit()==null?new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
					}
					else{
					comRate = new BigDecimal(0.00);
				}
				aEcinvoices.setCommissionRate(comRate);
				
				
				aEcinvoices.setCuInvoiceId(invoiceDateQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				if (invoiceDateQueryList.get(i).getCostTotal() != null) {
					aEcinvoices.setProfit(new BigDecimal("0.0000")
							.subtract(invoiceDateQueryList.get(i).getCostTotal()));
					profit = new BigDecimal("0.0000")
							.subtract(invoiceDateQueryList.get(i).getCostTotal());
				} else {
					aEcinvoices.setProfit(new BigDecimal(0.000));
					profit = new BigDecimal(0.000);
				}
				if(comRate==null){
					comRate = new BigDecimal(0.00);
				}
				
				
				aEcinvoices.setAmountDue(profit.multiply(comRate).multiply(new BigDecimal(0.01)));
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				ecInvoiceRepSplitID = (Integer) aSession.save(aEcinvoices);
				bTransaction.commit();
				}
			}
				
				}
				if (aEcSplitJobEmMasterList.size() > 0) {
							for(int m=0;m<aEcSplitJobEmMasterList.size();m++){
								theEcsplitjob= new Ecsplitjob();
								theEcsplitjob = aEcSplitJobEmMasterList.get(m);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(theEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter = ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
							aEcstatement = new Ecstatement();
							aEcstatement = getSaveEcStatement(
									ecPeriodID,theEcsplitjob.getRxMasterId());

							if (aEcstatement != null) {
								bTransaction = aSession.beginTransaction();
								bTransaction.begin();
								aEcinvoicerepsplit = new Ecinvoicerepsplit();
								aEcinvoicerepsplit
										.setEcInvoicePeriodId(theEcinvoiceperiod
												.getEcInvoicePeriodId());
								aEcinvoicerepsplit.setEcStatementId(aEcstatement
										.getEcStatementId());
								aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
								if(aEmmaster!=null){
								acommissionProfit = aEmmaster.getCommissionJobProfit();
								}else{
									acommissionProfit = new BigDecimal("0.0000");
								}
								//aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
								aEcinvoicerepsplit.setCommissionRate(theEcsplitjob.getAllocated());
								
								/*Commented on 10-04-2015
								 * if (invoiceDateQueryList.get(i).getFreight() != null
										&& invoiceDateQueryList.get(i).getFreight()
												.compareTo(new BigDecimal(0.00)) > 0) {*/
								BigDecimal theEcsplitjobAllocated = new BigDecimal(0.00);
								BigDecimal theEcinvoiceperiodProfit = new BigDecimal(0.00);
								theEcsplitjobAllocated = theEcsplitjob.getAllocated() ==null?new BigDecimal(0.00):theEcsplitjob.getAllocated();
								theEcinvoiceperiodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
								if(acommissionProfit==null){
									acommissionProfit = new BigDecimal("0.0000");
								}
								profits = theEcinvoiceperiodProfit.multiply(acommissionProfit)
										.multiply(new BigDecimal(0.01)).multiply(theEcsplitjobAllocated.multiply(new BigDecimal(0.01)));
								aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
								
								aEcinvoicerepsplit.setAmountDue(profits);
								ecInvoiceRepSplitID = (Integer) aSession
										.save(aEcinvoicerepsplit);
								bTransaction.commit();
							}
						}
				}
					}
				else if (aEcSplitJobList.size() > 0) {
						for(int k=0;k<aEcSplitJobList.size();k++){
							aEcsplitjob= new Ecsplitjob();
							aEcsplitjob = aEcSplitJobList.get(k);
							Emmaster ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
							if(ecsEmmaster!=null){
								payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
							}
							if(payCommissionAfter==null || payCommissionAfter==0){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(
								ecPeriodID,aEcsplitjob.getRxMasterId());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement
									.getEcStatementId());
							ecsEmmaster = new Emmaster();
							ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(ecsEmmaster!=null){
								acommissionProfit = ecsEmmaster.getCommissionJobProfit();
							}else{
								acommissionProfit = new BigDecimal("0.0000");
							}
							aEcinvoicerepsplit.setCommissionRate(aEcsplitjob.getAllocated());
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}
							BigDecimal theEcInvoicePeriodProfit = new BigDecimal("0.0000");
							BigDecimal aEcSplitJobAllocated = new BigDecimal("0.0000");
							aEcSplitJobAllocated = aEcsplitjob.getAllocated()==null?new BigDecimal("0.0000"): aEcsplitjob.getAllocated();
							theEcInvoicePeriodProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal("0.0000"):theEcinvoiceperiod.getProfit();
							aEcinvoicerepsplit.setAmountDue(theEcInvoicePeriodProfit.multiply(acommissionProfit==null?new BigDecimal("0.0000"):acommissionProfit)
									.multiply(new BigDecimal(0.01)).multiply(aEcSplitJobAllocated.multiply(new BigDecimal(0.01))));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
						}
					}
					else {
						Emmaster ecsEmmaster = new Emmaster();
						ecsEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
						if(ecsEmmaster!=null){
						payCommissionAfter =ecsEmmaster.getCommAfterPayment()==null?0:ecsEmmaster.getCommAfterPayment();
						}
						if(payCommissionAfter==null || payCommissionAfter==0){
						aEcstatement = new Ecstatement();
						aEcstatement = getSaveEcStatement(ecPeriodID, invoiceDateQueryList
								.get(i).getCuAssignmentId0());
						if (aEcstatement != null) {
							bTransaction = aSession.beginTransaction();
							bTransaction.begin();
							aEcinvoicerepsplit = new Ecinvoicerepsplit();
							aEcinvoicerepsplit
									.setEcInvoicePeriodId(theEcinvoiceperiod
											.getEcInvoicePeriodId());
							aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
							aEmmaster = getEmmasterDetails(invoiceDateQueryList.get(i).getCuAssignmentId0()==null?0:invoiceDateQueryList.get(i).getCuAssignmentId0());
							if(aEmmaster!=null){
							if(invoiceDateQueryList.get(i).getJoReleaseType().equals("0")){
								acommissionProfit = aEmmaster.getCommissionInvoiceProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionInvoiceProfit();
							}else{
								acommissionProfit = aEmmaster.getCommissionJobProfit()==null? new BigDecimal(0.0000):aEmmaster.getCommissionJobProfit();
							}
								
							}
							if(acommissionProfit==null){
								acommissionProfit = new BigDecimal("0.0000");
							}else{
								acommissionProfit = theEcinvoiceperiod.getProfit()==null?new BigDecimal(0.00):theEcinvoiceperiod.getProfit();
							}
							aEcinvoicerepsplit.setCommissionRate(acommissionProfit);
							aEcinvoicerepsplit.setProfit(theEcinvoiceperiod
									.getProfit());
							aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod
									.getProfit()==null?new BigDecimal(0.00).multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit):theEcinvoiceperiod
											.getProfit().multiply(acommissionProfit==null?new BigDecimal(0.00):acommissionProfit)
									.multiply(new BigDecimal(0.01)));
							ecInvoiceRepSplitID = (Integer) aSession
									.save(aEcinvoicerepsplit);
							bTransaction.commit();
						}
						}
					}
			}
		}
		}
		 catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		}
	}
	 catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		excep.printStackTrace();
		EmployeeException aEmployeeException = new EmployeeException(
				"Exception occurred : " + excep.getMessage(), excep);
		throw aEmployeeException;
	} finally {
		aSession.flush();
		aSession.close();
		aQueryString=null;
		aQuery =null;
		invoiceDateQuery =null;
		aSysvariable = null;
		aQueryString = null;
		ecInvoiceID = null;
		ecInvoiceRepSplitID = null;
		ecPeriodID = null;
		aTransaction = null;
		bTransaction = null;
		aEcstatement = null;
		aEcsplitjob = null;
		theEcsplitjob = null;
		payCommissionAfter = null;
		aEmmaster = null;
		bEmmaster = null;
		invoiceDateQuery = null;
	}
	return ecInvoiceRepSplitID;

	}
	
	@Override
	public Integer reverseCommissionPeriod(String startPeriodDate,String endPeriodDate,List<Ecperiod> ecPeriodList) 
			throws EmployeeException {/*
		Integer ecInvoiceRepSplitID = 1;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		Session aInventorySession =null;
		Transaction aTransaction;
		String ecInvoiceRepSplit=null;String ecInvoices =null;String ecInvoicePeriod =null;String ecJobs = null;String ecPeriod = null;
		try {
			aInventorySession = itsSessionFactory.openSession();
			ecInvoiceRepSplit = "DELETE FROM ecJobs WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "+ ecPeriodID + ")";
			ecInvoices = "DELETE FROM ecInvoices WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "+ ecPeriodID + ")";
			ecInvoicePeriod = "DELETE FROM ecInvoicePeriod WHERE ecPeriodID = "+ ecPeriodID;
			ecJobs = "DELETE FROM ecStatement WHERE ecPeriodID = "+ ecPeriodID;
			ecPeriod = "DELETE FROM ecPeriod WHERE ecPeriodID = "+ ecPeriodID;

			aTransaction = aInventorySession.beginTransaction();
			aInventorySession.createSQLQuery(ecInvoiceRepSplit).executeUpdate();
			aInventorySession.createSQLQuery(ecInvoicePeriod).executeUpdate();
			aInventorySession.createSQLQuery(ecJobs).executeUpdate();
			aInventorySession.createSQLQuery(ecInvoices).executeUpdate();
			aInventorySession.createSQLQuery(ecPeriod).executeUpdate();
			aTransaction.commit();
		}
		 catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			excep.printStackTrace();
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aInventorySession.flush();
			aInventorySession.close();
			ecInvoiceRepSplit=null;ecInvoices =null;ecInvoicePeriod =null;ecJobs = null;ecPeriod = null;
		}
		return ecInvoiceRepSplitID;
	*/
		Integer ecInvoiceRepSplitID = 1;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		Session aInventorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String ecInvoiceRepSplit = "DELETE FROM ecInvoiceRepSplit WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String joInvoiceCostUpdate = "UPDATE joInvoiceCost SET ecInvoicePeriodID = NULL WHERE "
				+ "ecInvoicePeriodID IN (SELECT ecInvoicePeriodID FROM ecInvoicePeriod WHERE ecPeriodID = "
				+ ecPeriodID+")";
		String veCommDetailUpdate = "UPDATE veCommDetail SET ecInvoicePeriodID = NULL WHERE "
				+ "ecInvoicePeriodID IN (SELECT ecInvoicePeriodID FROM ecInvoicePeriod WHERE ecPeriodID = "
				+ ecPeriodID+")";
		String ecInvoicePeriod = "DELETE FROM ecInvoicePeriod WHERE ecPeriodID = "
				+ ecPeriodID;
		String ecJobs = "DELETE FROM ecJobs WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String ecInvoices = "DELETE FROM ecInvoices WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String ecSalesMgrOverride = "DELETE FROM ecSalesMgrOverride WHERE ecStatementID IN (SELECT ecStatementID FROM ecStatement WHERE ecPeriodID = "
				+ ecPeriodID + ")";
		String ecStatement = "DELETE FROM ecStatement WHERE ecPeriodID = "+ ecPeriodID;
		String ecPeriod = "DELETE FROM ecPeriod WHERE ecPeriodID = "+ ecPeriodID;
		
		try {
			aTransaction = aInventorySession.beginTransaction();
			aInventorySession.createSQLQuery(ecInvoiceRepSplit).executeUpdate();
			aInventorySession.createSQLQuery(joInvoiceCostUpdate).executeUpdate();
			aInventorySession.createSQLQuery(veCommDetailUpdate).executeUpdate();
			aInventorySession.createSQLQuery(ecInvoicePeriod).executeUpdate();
			aInventorySession.createSQLQuery(ecJobs).executeUpdate();
			aInventorySession.createSQLQuery(ecInvoices).executeUpdate();
			aInventorySession.createSQLQuery(ecSalesMgrOverride).executeUpdate();
			aInventorySession.createSQLQuery(ecStatement).executeUpdate();
			aInventorySession.createSQLQuery(ecPeriod).executeUpdate();
			aTransaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			EmployeeException aEmployeeException = new EmployeeException(
					e.getMessage(), e);
			throw aEmployeeException;
		} finally {
			aInventorySession.flush();
			aInventorySession.close();
			ecInvoiceRepSplit =null;
			ecInvoicePeriod =null;
			ecJobs =null;
			ecInvoices =null;
			ecSalesMgrOverride=null;
		}
		return ecInvoiceRepSplitID;
	
		}

	
	@Override
	public Integer calculateFactoryCommissions(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
		String aQueryString = null;
		Integer ecInvoiceID = 0;
		Integer ecInvoiceRepSplitID = 0;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		Transaction aTransaction;
		Transaction bTransaction;
		Ecstatement aEcstatement = null;
		Emmaster aEmmaster = null;
		aQueryString = "SELECT D.joReleaseID, D.CustomerInvoiceDate, D.CustomerInvoiceAmount, D.VendorInvoiceAmount, J.joMasterID, "
				+ "J.rxCustomerID, J.cuAssignmentID0, J.cuAssignmentID1, J.cuAssignmentID2, J.cuAssignmentID3, J.cuAssignmentID4, "
				+ "P.rxVendorID, P.Subtotal AS Salez, P.PONumber, R.ReleaseType, J.BookedDate  FROM joRelease AS R "
				+ "RIGHT JOIN joReleaseDetail AS D ON R.joReleaseID = D.joReleaseID "
				+ "LEFT JOIN joMaster AS J ON R.joMasterID = J.joMasterID "
				+ "LEFT JOIN vePO AS P ON R.joReleaseID = P.joReleaseID "
				+ "WHERE D.CustomerInvoiceDate >= '"
				+ startPeriodDate+"'"
				/*+ " AND D.CustomerInvoiceDate <= '"+ endPeriodDate+"'"*/
				+ " AND R.ReleaseType=4";

		Session aSession = null;
		Query aQuery = null;
		try {
			Ecinvoiceperiod aEcinvoiceperiod = null;
			Ecinvoiceperiod theEcinvoiceperiod = null;
			Ecinvoicerepsplit aEcinvoicerepsplit = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoiceperiod = new Ecinvoiceperiod();
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				aEcinvoiceperiod.setSales((BigDecimal) aObj[12]);
				aEcinvoiceperiod.setProfit((BigDecimal) aObj[2]);
				aEcinvoiceperiod.setReference((String) aObj[13]);
				aEcinvoiceperiod.setReleaseType((byte) 4);
				aEcinvoiceperiod.setRxCustomerId((Integer) aObj[5]);
				aEcinvoiceperiod.setDatePaid((Date) aObj[1]);
				aEcinvoiceperiod.setJoReleaseID((Integer) aObj[0]);
				ecInvoiceID = (Integer) aSession.save(aEcinvoiceperiod);
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, ecInvoiceID);
				aTransaction.commit();
				BigDecimal acommissionRate = null;
				bTransaction = aSession.beginTransaction();
				bTransaction.begin();
				if (getEcSplitJobDetails((Integer) aObj[4]).size() > 0) {
					aEcstatement = new Ecstatement();
					aEcinvoicerepsplit = new Ecinvoicerepsplit();
					aEmmaster = getEmmasterDetails(getEcSplitJobDetails(
							((Integer) aObj[4])).get(0).getRxMasterId());
					aEcstatement = getSaveEcStatement(ecPeriodID,
							getEcSplitJobDetails(((Integer) aObj[4])).get(0)
									.getRxMasterId());
					aEcinvoicerepsplit.setEcInvoicePeriodId(theEcinvoiceperiod
							.getEcInvoicePeriodId());
					aEcinvoicerepsplit.setEcStatementId(aEcstatement
							.getEcStatementId());
					if (aEmmaster.getProfitBonus() > 0) {
						acommissionRate = new BigDecimal(0.0000);
						aEcinvoicerepsplit.setCommissionRate(acommissionRate);

					} else {
						acommissionRate = aEmmaster.getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(acommissionRate);
					}
					aEcinvoicerepsplit
							.setProfit(theEcinvoiceperiod.getProfit());
					aEcinvoicerepsplit.setAmountDue((theEcinvoiceperiod.getProfit()==null?new BigDecimal("0.0000"):theEcinvoiceperiod.getProfit()).multiply(acommissionRate==null?new BigDecimal("0.0000"):acommissionRate)
							.multiply(new BigDecimal(0.01)));
				} else {
					aEcstatement = new Ecstatement();
					aEcinvoicerepsplit = new Ecinvoicerepsplit();
					aEcstatement = getSaveEcStatement(ecPeriodID,
							((Integer) aObj[6]));
					aEcinvoicerepsplit.setEcInvoicePeriodId(theEcinvoiceperiod
							.getEcInvoicePeriodId());
					aEcinvoicerepsplit.setEcStatementId(aEcstatement
							.getEcStatementId());
					aEmmaster = getEmmasterDetails(((Integer) aObj[6]));
					if (aEmmaster.getProfitBonus() > 0) {
						acommissionRate = new BigDecimal(0.0000);
						aEcinvoicerepsplit.setCommissionRate(acommissionRate);

					} else {
						acommissionRate = aEmmaster.getCommissionJobProfit();
						aEcinvoicerepsplit.setCommissionRate(acommissionRate);
					}
					aEcinvoicerepsplit
							.setProfit(theEcinvoiceperiod.getProfit());
					aEcinvoicerepsplit.setAmountDue((theEcinvoiceperiod.getProfit()==null?new BigDecimal("0.0000"):theEcinvoiceperiod.getProfit()).multiply(acommissionRate==null?new BigDecimal("0.0000"):acommissionRate)
							.multiply(new BigDecimal(0.01)));
				}

				ecInvoiceRepSplitID = (Integer) aSession
						.save(aEcinvoicerepsplit);
				bTransaction.commit();

			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString=null;aQuery = null;
		}
		return ecInvoiceRepSplitID;
	}

	@Override
	public ArrayList<CuLinkageDetail> getCuLinkageDetails(Cuinvoice aCuinvoice,
			String endPeriodDate,String startPeriodDate) throws EmployeeException {
		String aQueryString = null;
		/*aQueryString = "SELECT IF(SUM(L.PaymentApplied) IS NULL,0.0000,SUM(L.PaymentApplied)) AS Paid, IF(SUM(L.DiscountUsed) IS NULL,0.0000,SUM(L.DiscountUsed)) AS Disc,"
				+ " MAX(L.datePaid) AS DatePaid FROM cuReceipt AS R "
				+ "RIGHT JOIN cuLinkageDetail AS L ON R.cuReceiptID = L.cuReceiptID WHERE L.cuInvoiceID= "+ aCuinvoice.getCuInvoiceId()
				+ " AND R.ReceiptDate IS NOT NULL AND R.ReceiptDate<='"+ endPeriodDate + "' " +" AND R.ReceiptDate>='"+startPeriodDate+"'";*/
/*		aQueryString = "SELECT SUM(IFNULL(L.PaymentApplied,0.0000)) AS Paid, SUM(IFNULL(L.DiscountUsed,0.0000)) AS Disc,"
				+ " MAX(L.datePaid) AS DatePaid FROM cuLinkageDetail AS L WHERE L.cuInvoiceID= "+ aCuinvoice.getCuInvoiceId()
				+ " AND L.deletedStatus=0  AND L.datePaid IS NOT NULL AND L.datePaid<='"+endPeriodDate+"'" +" AND R.ReceiptDate>='"+startPeriodDate+"'";
		*/
		aQueryString = "SELECT dates.Paid,dates.Disc,dates.DatePaid FROM (SELECT SUM(IFNULL(L.PaymentApplied,0.0000)) AS Paid, "
				+ "SUM(IFNULL(L.DiscountUsed,0.0000)) AS Disc,MAX(L.datePaid) AS DatePaid FROM cuLinkageDetail AS L WHERE "
				+ "L.cuInvoiceID= "+ aCuinvoice.getCuInvoiceId()+" AND L.deletedStatus=0  AND L.datePaid IS NOT NULL) AS dates WHERE "
				+ "dates.datePaid<='"+endPeriodDate+" 23:59:59'";
		Session aSession = null;Query aQuery =null;
		ArrayList<CuLinkageDetail> aQueryList = new ArrayList<CuLinkageDetail>();
		try {
			CuLinkageDetail aCuLinkageDetail = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				
				aCuLinkageDetail = new CuLinkageDetail();
				Object[] aObj = (Object[]) aIterator.next();
				if(aObj[2] != null){
				aCuLinkageDetail.setPaymentApplied((BigDecimal) aObj[0]);
				aCuLinkageDetail.setDiscountUsed((BigDecimal) aObj[1]);
				aCuLinkageDetail
						.setReciptDate((Date) (aObj[2] == null ? new Date()
								: aObj[2]));
				aQueryList.add(aCuLinkageDetail);
				}
			}
			if(aQueryList.size()<1){
				aQueryList = null;
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<Vebill> getVeBillDetails(Cuinvoice aCuinvoice)
			throws EmployeeException {
		String aQueryString = null;
		aQueryString = "SELECT IF(Sum(BillAmount) IS NULL,0.0000,Sum(BillAmount)) AS Cost, IF(Sum(FreightAmount) IS NULL,0.0000,Sum(FreightAmount)) AS Freight FROM veBill WHERE TransactionStatus>0 AND "
				+ "(cuInvoiceID_ApplyCost= "
				+ aCuinvoice.getCuInvoiceId()
				+ " OR joReleaseDetailID="
				+ aCuinvoice.getJoReleaseDetailId()
				+ ")";
		Session aSession = null;Query aQuery =null;
		ArrayList<Vebill> aQueryList = new ArrayList<Vebill>();
		try {
			Vebill aVebill = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVebill = new Vebill();
				Object[] aObj = (Object[]) aIterator.next();
				aVebill.setBillAmount((BigDecimal) aObj[0]);
				aVebill.setFreightAmount((BigDecimal) aObj[1]);
				aQueryList.add(aVebill);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public List<Ecinvoices> getEcInvoiceDetails() throws EmployeeException {
		Session aSession = null;
		List<Ecinvoices> aEmmasterList = new ArrayList<Ecinvoices>();
		Query ecJobQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			 ecJobQuery = aSession.createQuery(" FROM Ecinvoices");
			aEmmasterList = ecJobQuery.list();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting Ecinvoices : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			ecJobQuery =null;
		}
		return aEmmasterList;

	}

	@Override
	public List<Ecinvoicerepsplit> getEcInvoiceRepSplitDetails(String Condition)
			throws EmployeeException {
		Session aSession = null;
		List<Ecinvoicerepsplit> aEmmasterList = new ArrayList<Ecinvoicerepsplit>();
		String whereCondition = "";Query ecJobQuery = null;
		if (Condition != null && Condition.trim().equals("")) {
			whereCondition = Condition;
		}
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession.createQuery(" FROM Ecinvoicerepsplit "
					+ whereCondition);
			aEmmasterList = ecJobQuery.list();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting Ecinvoicerepsplit : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			 ecJobQuery = null;
		}
		return aEmmasterList;

	}

	@Override
	public List<Ecsplitjob> getEcSplitJobDetails(Integer joMasterID)
			throws EmployeeException {
		Session aSession = null;
		Query ecJobQuery =null;
		List<Ecsplitjob> aEcsplitjobList = new ArrayList<Ecsplitjob>();
		Ecsplitjob aEcsplitjob = null;
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession
					.createSQLQuery("SELECT S.joMasterID, S.Allocated, M.UserLoginID FROM ecSplitJob AS S "
							+ "LEFT JOIN emMaster AS M ON S.rxMasterID = M.emMasterID WHERE S.joReleaseID IS NULL AND S.joMasterID="
							+ joMasterID);
			Iterator<?> aIterator = ecJobQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEcsplitjob = new Ecsplitjob();
				Object[] aObj = (Object[]) aIterator.next();
				aEcsplitjob.setJoMasterId((Integer) aObj[0]);
				aEcsplitjob.setAllocated((BigDecimal) aObj[1]);
				aEcsplitjob.setRxMasterId((Integer) aObj[2]);
				aEcsplitjobList.add(aEcsplitjob);
			}

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting Ecinvoicerepsplit : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			ecJobQuery =null;
		}
		return aEcsplitjobList;

	}

	@Override
	public List<Ecsplitjob> getEcSplitJobEmMasterDetails(Integer joMasterID,
			Integer cuInvoiceID,String Type) throws EmployeeException {
		Session aSession = null;
		List<Ecsplitjob> aEcsplitjobList = new ArrayList<Ecsplitjob>();
		Ecsplitjob aEcsplitjob = null;Query ecJobQuery = null;
		String strQuery ="SELECT ecSplitJob.ecSplitJobID,ecSplitJob.joMasterID,ecSplitJob.Allocated,"
				+ "ecSplitJob.ecSplitCodeID, ecSplitJob.joReleaseID,ecSplitJob.splitType, emMaster.UserLoginID FROM ecSplitJob "
				+ "INNER JOIN joRelease ON ecSplitJob.joReleaseID = joRelease.joReleaseID "
				+ "INNER JOIN emMaster ON ecSplitJob.rxMasterID = emMaster.emMasterID "
				+ "INNER JOIN joReleaseDetail ON joRelease.joReleaseID = joReleaseDetail.joReleaseID ";
		if(Type.equals("invoice")){
			strQuery += "INNER JOIN cuInvoice ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID "
					+ " WHERE joRelease.joMasterID = " + joMasterID
					+ " AND cuInvoice.cuInvoiceID = " + cuInvoiceID;
		}else{
			strQuery+= " WHERE joRelease.joReleaseID = "+ joMasterID +" AND joRelease.joMasterID= "+cuInvoiceID;
		}
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession
					.createSQLQuery(strQuery);
			Iterator<?> aIterator = ecJobQuery.list().iterator();
			while (aIterator.hasNext()) {
				aEcsplitjob = new Ecsplitjob();
				Object[] aObj = (Object[]) aIterator.next();
				aEcsplitjob.setEcSplitJobId((Integer) aObj[0]);
				aEcsplitjob.setJoMasterId((Integer) aObj[1]);
				aEcsplitjob.setAllocated((BigDecimal) aObj[2]);
				aEcsplitjob.setEcSplitCodeID((Integer) aObj[3]);
				aEcsplitjob.setJoReleaseID((Integer) aObj[4]);
				aEcsplitjob.setSplittype((String) aObj[5]);
				aEcsplitjob.setRxMasterId((Integer) aObj[6]);
				aEcsplitjobList.add(aEcsplitjob);
			}

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting Ecinvoicerepsplit : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			strQuery =null;ecJobQuery = null;
		}
		return aEcsplitjobList;

	}

	@Override
	public List<Ecinvoiceperiod> getEcInvoicePeriodDetails()
			throws EmployeeException {
		Session aSession = null;Query ecJobQuery =null;
		List<Ecinvoiceperiod> aEmmasterList = new ArrayList<Ecinvoiceperiod>();
		try {
			aSession = itsSessionFactory.openSession();
			ecJobQuery = aSession.createQuery(" FROM Ecinvoiceperiod");
			aEmmasterList = ecJobQuery.list();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred while getting Ecinvoiceperiod : "
							+ excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			 ecJobQuery =null;
		}
		return aEmmasterList;

	}

	@Override
	public Emmaster addemMaster(Emmaster emmaster) {
		itsLogger.debug("Query has to add NewEmployee");
		Session aemMasterSession = itsSessionFactory.openSession();

		Transaction aemMasterTransaction;
		try {
			aemMasterTransaction = aemMasterSession.beginTransaction();
			aemMasterTransaction.begin();
			aemMasterSession.save(emmaster);
			aemMasterTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aemMasterSession.flush();
			aemMasterSession.close();

		}
		return emmaster;
	}

	@Override
	public boolean deleteEmployeeDetails(Rxmaster theRxMaster)
			throws CompanyException {
		Session aRxMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aRxMasterSession.beginTransaction();
			aTransaction.begin();
			Rxmaster aExistingRxmaster = (Rxmaster) aRxMasterSession.get(
					Rxmaster.class, theRxMaster.getRxMasterId());
			aExistingRxmaster.setIsEmployee(false);
			aRxMasterSession.update(aExistingRxmaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CompanyException aCompanyException = new CompanyException(excep
					.getCause().getMessage(), excep);
			throw aCompanyException;
		} finally {
			aRxMasterSession.flush();
			aRxMasterSession.close();
		}
		return false;
	}

	@Override
	public Emmaster getEmployeeCommDetails(String theRolodexNumber) {
		Session aSession = null;
		Emmaster aEmmaster = null;
		try {
			itsLogger.info("the Rolodex NO: " + theRolodexNumber);
			int aRxMasterId = Integer.parseInt(theRolodexNumber);
			itsLogger.info("the Rx MasterID : " + aRxMasterId);
			aSession = itsSessionFactory.openSession();
			aEmmaster = (Emmaster) aSession.get(Emmaster.class, aRxMasterId);
			itsLogger.info("Em Master ID: " + aEmmaster.getEmMasterId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aEmmaster;
	}

	@Override
	public List<Rxmaster> getrxMasterObject() throws EmployeeException {
		Session aSession = null;String query =null;
		List<Rxmaster> rxMasterObjs = null;
		try {
			 query = "From Rxmaster WHERE name <> '' AND IsCustomer='1' AND InActive='1' ORDER BY rxMasterID ";
			aSession = itsSessionFactory.openSession();
			rxMasterObjs = (List<Rxmaster>) aSession.createQuery(query).list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return rxMasterObjs;
	}

	@Override
	public List<Rxmaster> getrxMasterTableValues() throws EmployeeException {
		Session aSession = null;
		List<Rxmaster> rxMasterObjs = null;String query = null;
		try {
			query = "From Rxmaster WHERE InActive<>1 and IsCustomer=1 and name <> '' ORDER BY name, firstName";
			aSession = itsSessionFactory.openSession();
			rxMasterObjs = (List<Rxmaster>) aSession.createQuery(query).list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return rxMasterObjs;
	}
	
	@Override
	public Integer calculateWarehouseInvoices(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
		Ecinvoices aEcinvoices = null;
		Ecstatement aEcstatement = new Ecstatement();
		Integer ecInvoiceID = 0;
		Integer ecInvoiceRepSplitID = 0;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		String aQueryString = null;
		Session aSession = null;
		Transaction aTransaction;

		aQueryString = "SELECT cuInvoiceID,InvoiceDate,InvoiceNumber,InvoiceAmount,AppliedAmount,TaxAmount,"
				+ "Freight,CostTotal,SubTotal,TaxTotal,TaxRate,cuAssignmentID0,rxCustomerID "
				+ "FROM cuInvoice WHERE InvoiceDate>= '"
				+ startPeriodDate +"'"
				/*+ " AND InvoiceDate<= '"+ endPeriodDate+"'"*/
				+ " AND TransactionStatus>0 AND "
				+ " (joReleaseDetailID IS NULL OR joReleaseDetailID = 0) ORDER BY cuAssignmentID0";
		Query aQuery =null;
		ArrayList<Cuinvoice> aQueryList = new ArrayList<Cuinvoice>();
		try {
			Cuinvoice aCuinvoice = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuinvoice = new Cuinvoice();
				Object[] aObj = (Object[]) aIterator.next();
				aCuinvoice.setCuInvoiceId((Integer) aObj[0]);
				aCuinvoice.setInvoiceDate((Date) aObj[1]);
				aCuinvoice.setInvoiceNumber((String) aObj[2]);
				aCuinvoice.setInvoiceAmount((BigDecimal) aObj[3]);
				aCuinvoice.setAppliedAmount((BigDecimal) aObj[4]);
				aCuinvoice.setTaxAmount((BigDecimal) aObj[5]);
				aCuinvoice.setFreight((BigDecimal) aObj[6]);
				aCuinvoice.setCostTotal((BigDecimal) aObj[7]);
				aCuinvoice.setSubtotal((BigDecimal) aObj[8]);
				aCuinvoice.setTaxTotal((BigDecimal) aObj[9]);
				aCuinvoice.setTaxRate((BigDecimal) aObj[10]);
				aCuinvoice.setCuAssignmentId0((Integer) aObj[11]);
				aCuinvoice.setRxCustomerId((Integer) aObj[12]);
				aQueryList.add(aCuinvoice);
			}
			itsLogger.info("CuInvoice List Size: " + aQueryList.size());
			if(aQueryList.size()>0){
			for (int i = 0; i < aQueryList.size(); i++) {
				aEcstatement = getSaveEcStatement(ecPeriodID, aQueryList.get(i)
						.getCuAssignmentId0());
				aEcinvoices = new Ecinvoices();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aEcinvoices.setCuInvoiceId(aQueryList.get(i).getCuInvoiceId());
				aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
				aEcinvoices.setAmountDue((aQueryList.get(i).getSubtotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getSubtotal())
						.subtract((aQueryList.get(i).getCostTotal()==null?new BigDecimal("0.0000"):aQueryList.get(i).getCostTotal())));
				if (getEmmasterDetails(aQueryList.get(i).getCuAssignmentId0())
						.getProfitBonus() != null) {
					aEcinvoices.setCommissionRate(new BigDecimal(0));
				} else {
					aEcinvoices.setCommissionRate(getEmmasterDetails(
							aQueryList.get(i).getCuAssignmentId0())
							.getCommissionInvoiceProfit());
				}

				aEcinvoices.setProfit(new BigDecimal(0));

				ecInvoiceID = (Integer) aSession.save(aEcinvoices);
				aTransaction.commit();
			}
		}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;aQuery =null;
		}
		return ecInvoiceRepSplitID;
	}

	@Override
	public Integer calculateCommissionTotals(String startPeriodDate,
			String endPeriodDate,List<Ecperiod> ecPeriodList,String calculatePayment) throws EmployeeException {
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		Ecstatement aEcstatement = new Ecstatement();
		BigDecimal profit = new BigDecimal(0.0000);
		Session ecStatementSession = itsSessionFactory.openSession();
		Transaction aTransaction;	String querytoGetAmountDue =null;
		Query aQuery6 =null;Query aQuery7 =null;
		Sysvariable jobSysvariable =new Sysvariable();
		jobSysvariable = getSysVariableDetails("InvoicesCombinedforJobs");
		List<Ecstatement> ecStatementList = new ArrayList<Ecstatement>();
		Integer ecStatementID = 0;
		try {
			ecStatementList = getEcStatementPeriodList(ecPeriodID);
			aTransaction = ecStatementSession.beginTransaction();
			aTransaction.begin();
			for (int j = 0; j < ecStatementList.size(); j++) {
				
				aEcstatement = (Ecstatement) ecStatementSession.get(
						Ecstatement.class, ecStatementList.get(j)
								.getEcStatementId());
				BigDecimal profitTodate = getBigDecimalValue("SELECT IF(SUM(ProfitToDate) IS NULL,0.0000,SUM(ProfitToDate)) AS ptd FROM ecJobs WHERE ecStatementID="
						+ ecStatementList.get(j).getEcStatementId());
				BigDecimal profitPaidOnTodate = getBigDecimalValue("SELECT IF(SUM(ProfitPaidOnToDate) IS NULL,0.0000,SUM(ProfitPaidOnToDate)) AS ppotd FROM ecJobs WHERE ecStatementID="
						+ ecStatementList.get(j).getEcStatementId());
				profit = profit.add(profitTodate.subtract(profitPaidOnTodate));
				profit = profit
						.add(getBigDecimalValue("SELECT IF(SUM(Profit) IS NULL,0.0000,SUM(Profit)) AS pro FROM ecInvoices WHERE ecStatementID="
								+ ecStatementList.get(j).getEcStatementId()));
				profit = profit
						.add(getBigDecimalValue("SELECT IF(SUM(Profit) IS NULL,0.0000,SUM(Profit)) AS pro FROM ecInvoiceRepSplit WHERE ecStatementID="
								+ ecStatementList.get(j).getEcStatementId()));
			
				aEcstatement.setProfit(profit);
				aEcstatement.setCalculatedDate(ecPeriodList.get(0).getPeriodEndingDate().toString());
				//aEcstatement.setCalculatedDate(new Date().toString());
				aEcstatement.setEcStatementEnteredDate(new Date());
				BigDecimal amountDue = new BigDecimal("0.0000");
				querytoGetAmountDue ="SELECT IF(SUM(AmountDue) IS NULL,0.0000,SUM(AmountDue)) AS ptd FROM ecJobs WHERE ecStatementID="
						+ ecStatementList.get(j).getEcStatementId();
				aQuery6 = ecStatementSession.createSQLQuery(querytoGetAmountDue);
				if(!aQuery6.list().isEmpty() && aQuery6.uniqueResult() != null){
					amountDue = amountDue.add((BigDecimal)aQuery6.uniqueResult()); /*Always return unique value.*/
				}
				
				querytoGetAmountDue ="SELECT IF(SUM(AmountDue) IS NULL,0.0000,SUM(AmountDue)) AS ptd FROM ecInvoiceRepSplit WHERE ecStatementID="
						+ ecStatementList.get(j).getEcStatementId();
				aQuery7 = ecStatementSession.createSQLQuery(querytoGetAmountDue);
				if(!aQuery7.list().isEmpty() && aQuery7.uniqueResult() != null){
					amountDue = amountDue.add((BigDecimal)aQuery7.uniqueResult()); /*Always return unique value.*/
				}
				
				aEcstatement.setJobCommissions(amountDue);
				if(jobSysvariable.getValueLong()!=null && jobSysvariable.getValueLong()>0){
				/*aEcstatement
						.setJobCommissions(getBigDecimalValue("SELECT IF(SUM(AmountDue) IS NULL,0.0000,SUM(AmountDue)) AS ptd FROM ecJobs WHERE ecStatementID="
								+ ecStatementList.get(j).getEcStatementId()));*/
			
				aEcstatement
						.setOtherCommissions(getBigDecimalValue("SELECT IF(SUM(AmountDue) IS NULL,0.0000,SUM(AmountDue)) AS ptd FROM ecInvoices WHERE ecStatementID="
								+ ecStatementList.get(j).getEcStatementId()));
				//aEcstatement.setPayment();
				//rs("Payment") = rs("OpeningBalance") + rs("Overrides") + rs("JobCommissions") + rs("OtherCommissions") + rs("Adjustments")
				if(calculatePayment.equalsIgnoreCase("no")){
					//aEcstatement.setOpeningBalance(new BigDecimal(0.0000));
					aEcstatement.setPayment(aEcstatement.getOpeningBalance().add(aEcstatement.getOverrides()).add(aEcstatement.getJobCommissions()).add(aEcstatement.getAdjustments()));
				}
				}
				else{
				/*aEcstatement
				.setJobCommissions(getBigDecimalValue("SELECT IF(SUM(AmountDue) IS NULL,0.0000,SUM(AmountDue)) AS ptd FROM ecInvoiceRepSplit WHERE ecStatementID="
						+ ecStatementList.get(j).getEcStatementId()));*/
				if(calculatePayment.equalsIgnoreCase("no")){
					//aEcstatement.setOpeningBalance(new BigDecimal(0.0000));
				aEcstatement.setPayment(aEcstatement.getOpeningBalance().add(aEcstatement.getOverrides()).add(aEcstatement.getJobCommissions()).add(aEcstatement.getAdjustments()));
				}
				}
				/*
				 * If InvoicesCombinedforJobs Then rs("JobCommissions") =
				 * Cents(SumCurrency("ecJobs", "AmountDue", "ecStatementID=" &
				 * Str(ecStatementID))) rs("OtherCommissions") =
				 * Cents(SumCurrency("ecInvoices", "AmountDue", "ecStatementID="
				 * & Str(ecStatementID))) Else rs("JobCommissions") =
				 * Cents(SumCurrency("ecInvoiceRepSplit", "AmountDue",
				 * "ecStatementID=" & Str(ecStatementID)))
				 */
				ecStatementSession.update(aEcstatement);
				ecStatementID = ecStatementList.get(j).getEcStatementId();
			}
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			ecStatementSession.flush();
			ecStatementSession.close();
			querytoGetAmountDue =null;
			aQuery6 =null;aQuery7 =null;
		}
		return ecStatementID;
	}

@Override
public Map<String,BigDecimal> getCommissionsStatementTotals(Integer ecStatementID) {
			Session aSession = null;String	aCommissionQry =null;Query aQuery =null;
			itsLogger.info("esStatement Details.");
			Map<String,BigDecimal> aTotals = new HashMap<String, BigDecimal>();
		if(ecStatementID>0){
		aCommissionQry = "SELECT SUM(ecInvoiceRepSplit.Profit), SUM(ecInvoiceRepSplit.AmountDue) "
				+ "FROM(joReleaseDetail RIGHT JOIN (((ecInvoiceRepSplit LEFT JOIN ecInvoicePeriod "
				+ "ON ecInvoiceRepSplit.ecInvoicePeriodID = ecInvoicePeriod.ecInvoicePeriodID) "
				+ "LEFT JOIN rxMaster  ON ecInvoicePeriod.rxCustomerID = rxMaster.rxMasterID) "
				+ "LEFT JOIN cuInvoice ON ecInvoicePeriod.cuInvoiceID = cuInvoice.cuInvoiceID) "
				+ "ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) LEFT JOIN joMaster "
				+ "ON joReleaseDetail.joMasterID = joMaster.joMasterID WHERE "
				+ "ecInvoiceRepSplit.ecStatementID = "+ecStatementID+" AND ecInvoiceRepSplit.AmountDue != 0.0000";
		
		itsLogger.info(" aCommissionQry :: "+aCommissionQry);
		BigDecimal profitTotal = new BigDecimal("0.0000");
		BigDecimal amounDueTotal = new BigDecimal("0.0000");
		try{
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aCommissionQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				profitTotal =(BigDecimal) aObj[0];
				amounDueTotal=(BigDecimal) aObj[1];
				aTotals.put("profitTotal", profitTotal);
				aTotals.put("amounDueTotal", amounDueTotal);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCommissionQry=null; aQuery =null;
		}
		}
		return aTotals;
	}
			
	@Override
	public ArrayList<Ecstatement> getEmployeeCommissionsStatement(Integer ecStatementID) {
				Session aSession = null;String	aCommissionQry = null;Query aQuery = null;
				ArrayList<Ecstatement> aQueryList = new ArrayList<Ecstatement>();
				itsLogger.info("esStatement Details.");
				if(ecStatementID>0){
				aCommissionQry = " SELECT ecStatement.RepLoginID, ecStatement.Flags, ecStatement.ecStatementID, tsUserLogin.FullName, ecStatement.Profit, "
						+ "ecStatement.OpeningBalance, ecStatement.JobCommissions, ecStatement.OtherCommissions, ecStatement.Overrides,"
						+ " ecStatement.RepDeduct1, "
						+ " ecStatement.RepDeduct2, ecStatement.RepDeduct3, ecStatement.RepDeduct4, ecStatement.Adjustments, ecStatement.Payment, "
						+ "ecStatement.Comment, ecPeriod.PeriodEndingDate FROM ecPeriod RIGHT JOIN (ecStatement LEFT JOIN tsUserLogin ON "
						+ "ecStatement.RepLoginID = tsUserLogin.UserLoginID)  ON ecPeriod.ecPeriodID = ecStatement.ecPeriodID WHERE "
						+ "ecStatement.ecStatementID="+ecStatementID;
				
				itsLogger.info(" aCommissionQry :: "+aCommissionQry);
				Ecstatement aEcstatement = null;
				try{
					aSession = itsSessionFactory.openSession();
					aQuery = aSession.createSQLQuery(aCommissionQry);
					Iterator<?> aIterator = aQuery.list().iterator();
					while (aIterator.hasNext()) {
						aEcstatement = new Ecstatement();
						Object[] aObj = (Object[])aIterator.next();
						aEcstatement.setRepLoginId((Integer) aObj[0]);
						aEcstatement.setFlags((String) aObj[1]);
						aEcstatement.setEcStatementId((Integer) aObj[2]);
						aEcstatement.setRepName((String) aObj[3]);
						aEcstatement.setProfit((BigDecimal) aObj[4]);
						aEcstatement.setOpeningBalance((BigDecimal) aObj[5]);
						aEcstatement.setJobCommissions((BigDecimal) aObj[6]);
						aEcstatement.setOtherCommissions((BigDecimal) aObj[7]);
						aEcstatement.setOverrides((BigDecimal) aObj[8]);
						aEcstatement.setRepDeduct1((BigDecimal) aObj[9]);
						aEcstatement.setRepDeduct2((BigDecimal) aObj[10]);
						aEcstatement.setRepDeduct3((BigDecimal) aObj[11]);
						aEcstatement.setRepDeduct4((BigDecimal) aObj[12]);
						aEcstatement.setAdjustments((BigDecimal) aObj[13]);
						aEcstatement.setPayment((BigDecimal) aObj[14]);
						aEcstatement.setComment((String) aObj[15]);
						aQueryList.add(aEcstatement);
					}
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				} finally {
					aSession.flush();
					aSession.close();
					aCommissionQry = null;aQuery = null;
				}
				}
				return aQueryList;
			}
			
	
	@Override
	public List<Ecstatement> getEcStatementPeriodList(Integer aEcperiod)
			throws EmployeeException {
		Session aSession = null;Query query =null;
		List<Ecstatement> aQueryList = new ArrayList<Ecstatement>();
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession
					.createQuery("FROM Ecstatement WHERE ecPeriodID="
							+ aEcperiod);
			aQueryList = query.list();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query =null;
		}
		return aQueryList;
	}

	@Override
	public BigDecimal getBigDecimalValue(String strQuery) throws JobException {
		String aJobCountStr = strQuery;
		Session aSession = null;Query aQuery = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			return (BigDecimal) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
	}

	@Override
	public List<Ecsalesmgroverride> getEcSalesMgrOverrideList()
			throws EmployeeException {
		Session aSession = null;Query query = null;
		List<Ecsalesmgroverride> aQueryList = new ArrayList<Ecsalesmgroverride>();
		try {
			aSession = itsSessionFactory.openSession();
			query = aSession.createQuery("FROM Ecsalesmgroverride ");
			aQueryList = query.list();

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aQueryList;
	}
	
	@Override
	public Integer calculateMiscJobs(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException {
		Session aSession = null;
		Sysvariable aSysvariable = itsEmployeeService.getSysVariableDetails("CommissionSpecialCode");
		String aQueryString = null;
		Integer ecPeriodID = ecPeriodList.get(0).getEcPeriodId();
		aQueryString = "SELECT J.JobNumber, J.Description, D.joMasterID, D.ExpenseAmount, B.rxMasterID, B.InvoiceNumber, B.PostDate, "
				+ "B.InvoiceNumber, J.cuAssignmentID0, J.cuAssignmentID1 FROM veBillDistribution D "
				+ "LEFT OUTER JOIN joMaster J ON D.joMasterID = J.joMasterID  "
				+ "LEFT OUTER JOIN veBill B ON D.veBillID = B.veBillID "
				+ "WHERE D.joMasterID IS NOT NULL AND B.TransactionStatus > 0  "
				+ "AND B.PostDate >= '"+startPeriodDate+"'"
				/*+" AND B.PostDate <= '"+endPeriodDate+"'"*/;
		Transaction aTransaction;
		Ecinvoices aEcinvoices = null;
		Ecstatement aEcstatement = new Ecstatement();
		Ecinvoiceperiod aEcinvoiceperiod = null;Query aQuery =null;
		Integer insertStatus = 0;
		Integer invoiceStatus =0;
		List<Ecsplitjob> aEcsplitjobList = new ArrayList<Ecsplitjob>();
		try{

			Emmaster aEmmaster = null;
			Ecinvoicerepsplit aEcinvoicerepsplit = null;
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aQueryString);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				Ecsplitjob aEcsplitjob = null; 
				aEcinvoiceperiod = new Ecinvoiceperiod();
				Ecinvoiceperiod theEcinvoiceperiod = new Ecinvoiceperiod();
				aEmmaster = new  Emmaster();
				aEcinvoices = new Ecinvoices();
				Object[] aObj = (Object[])aIterator.next();
			    BigDecimal ecInvoicePeriodProfit = new BigDecimal(0.00);
				aEcinvoiceperiod.setEcPeriodId(ecPeriodID);
				aEcinvoiceperiod.setModified(false);
				ecInvoicePeriodProfit = ((BigDecimal) aObj[3]).multiply(new BigDecimal(-1));
				aEcinvoiceperiod.setSales(new BigDecimal(0.0000));
				aEcinvoiceperiod.setProfit(ecInvoicePeriodProfit);
				aEcinvoiceperiod.setReference((String) aObj[5]);
				aEcinvoiceperiod.setReleaseType((byte) 1);
				aEcinvoiceperiod.setDatePaid((Date) aObj[6]);
				aEcinvoiceperiod.setExpJoMasterID((Integer) aObj[2]);
				aEcinvoiceperiod.setExpRxMasterID((Integer) aObj[4]);
				
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				insertStatus = (Integer) aSession.save(aEcinvoiceperiod);
				aTransaction.commit();
				theEcinvoiceperiod = (Ecinvoiceperiod) aSession.get(
						Ecinvoiceperiod.class, insertStatus);
				//Rep Functionality
				if(aSysvariable.getValueString().contains("/CJ")){
					aEcstatement = getSaveEcStatement(ecPeriodID, (Integer) aObj[8]);
					aEmmaster = getEmmasterDetails(((Integer) aObj[8]));
							if(aEcstatement!=null){
								aEcinvoices.setEcStatementId(aEcstatement.getEcStatementId());
								
								aEcinvoices.setProfit((BigDecimal) aObj[3]);
								if(aEmmaster!=null && aEmmaster.getCommissionJobProfit()!=null){
								aEcinvoices.setCommissionRate(aEmmaster.getCommissionJobProfit());
								}
								BigDecimal commissionJobProfit = aEmmaster.getCommissionJobProfit() == null? new BigDecimal("0.000"): aEmmaster.getCommissionJobProfit();
								aEcinvoices.setAmountDue(ecInvoicePeriodProfit.multiply(commissionJobProfit.multiply(new BigDecimal(0.01))));
								aTransaction = aSession.beginTransaction();
								aTransaction.begin();
								invoiceStatus = (Integer) aSession.save(aEcinvoices);
								aTransaction.commit();
								
							}
					
			
				}

				aEcsplitjobList=getEcSplitJobDetails((Integer) aObj[2]);
				if(aEcsplitjobList.size()>0){
					for(int j=0;j<aEcsplitjobList.size();j++){
					aEcsplitjob = aEcsplitjobList.get(j);
					aEcstatement = getSaveEcStatement(ecPeriodID,aEcsplitjob.getRxMasterId());
					aEmmaster = getEmmasterDetails(aEcsplitjob.getRxMasterId());
					if(aEcstatement!=null){
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit.setEcInvoicePeriodId(theEcinvoiceperiod.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
						aEcinvoicerepsplit.setCommissionRate(aEmmaster.getCommissionJobProfit());
						aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
						aEcinvoicerepsplit.setAmountDue((theEcinvoiceperiod.getProfit().multiply(aEmmaster.getCommissionJobProfit().multiply(new BigDecimal(0.01))).multiply(new BigDecimal(0.01).multiply(aEcsplitjob.getAllocated()))));
						     
		                //If SysVarGetBool(gsPercentageProfit) Then
		                //ecInvoiceRepSplit("Profit") = ecInvoicePeriod("Profit") * 0.01 * Dollar(rss("Allocated"))
		                //ecInvoiceRepSplit("AmountDue") = Cents(ecInvoiceRepSplit("Profit") * (ecInvoiceRepSplit("CommissionRate") * 0.01))
		                //Else
		                //ecInvoiceRepSplit("Profit") = ecInvoicePeriod("Profit")
		                //ecInvoiceRepSplit("AmountDue") = Cents(ecInvoiceRepSplit("Profit") * (ecInvoiceRepSplit("CommissionRate") * 0.01) * 0.01 * Dollar(rss("Allocated")))
		                //End If
						aTransaction = aSession.beginTransaction();
						aTransaction.begin();
						invoiceStatus = (Integer) aSession.save(aEcinvoicerepsplit);
						aTransaction.commit();          
					}
					}
				}
				else{
					aEcstatement = getSaveEcStatement(ecPeriodID, (Integer) aObj[8]);
					aEmmaster = getEmmasterDetails( (Integer) aObj[8]);
					if(aEcstatement!=null){
						aEcinvoicerepsplit = new Ecinvoicerepsplit();
						aEcinvoicerepsplit.setEcInvoicePeriodId(theEcinvoiceperiod.getEcInvoicePeriodId());
						aEcinvoicerepsplit.setEcStatementId(aEcstatement.getEcStatementId());
						aEcinvoicerepsplit.setCommissionRate(aEmmaster.getCommissionJobProfit());
						aEcinvoicerepsplit.setProfit(theEcinvoiceperiod.getProfit());
						aEcinvoicerepsplit.setAmountDue(theEcinvoiceperiod.getProfit().multiply(aEmmaster.getCommissionJobProfit().multiply(new BigDecimal(0.01))));
		
						aTransaction = aSession.beginTransaction();
						aTransaction.begin();
						invoiceStatus = (Integer) aSession.save(aEcinvoicerepsplit);
						aTransaction.commit(); 
						}
					}
				}
			}
			catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				EmployeeException aEmployeeException = new EmployeeException("Exception occurred : " + excep.getMessage(), excep);
				throw aEmployeeException;		
			} finally {
				aSession.flush();
				aSession.close();
				aQueryString =null;aQuery =null;
			}
	return invoiceStatus;
	}
	
	@Override
	public Integer CheckEmployeeCommissionCount(Integer ecstatementid) throws EmployeeException {
		String aQueryString = null;
		Transaction aTransaction;
		BigInteger returnvalue=null;Query aQuery =null;
		/*aQueryString = "SELECT COUNT(*) FROM rxMaster RIGHT JOIN (joMaster RIGHT JOIN ecJobs ON joMaster.joMasterID = ecJobs.joMasterID) ON rxMaster.rxMasterID = joMaster.rxCustomerID"
				+" WHERE ecJobs.ecStatementID="+ecstatementid+" ORDER BY rxMaster.Name, rxMaster.FirstName, joMaster.Description";
		 */
		aQueryString = " SELECT COUNT(*) FROM (joReleaseDetail RIGHT JOIN (((ecInvoiceRepSplit LEFT JOIN ecInvoicePeriod "
		+ "ON ecInvoiceRepSplit.ecInvoicePeriodID = ecInvoicePeriod.ecInvoicePeriodID) LEFT JOIN rxMaster ON  "
		+ "ecInvoicePeriod.rxCustomerID = rxMaster.rxMasterID) LEFT JOIN cuInvoice ON "
		+ "ecInvoicePeriod.cuInvoiceID = cuInvoice.cuInvoiceID) ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) "
		+ "LEFT JOIN joMaster ON joReleaseDetail.joMasterID = joMaster.joMasterID WHERE ecInvoiceRepSplit.ecStatementID = "+ecstatementid+""
		+ " ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQueryString);
			List<?> aList = aQuery.list();
			returnvalue =  (BigInteger) aList.get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			EmployeeException aEmployeeException = new EmployeeException(
					"Exception occurred : " + excep.getMessage(), excep);
			throw aEmployeeException;
		} finally {
			aSession.flush();
			aSession.close();
			aQueryString = null;aQuery =null;
		}
		return returnvalue.intValue();
	}

	@Override
	public ArrayList<CommissionStatementBean> getCommissionStatements(Integer ecStatementID) {
		Session aSession = null;Query aQuery =null;
		ArrayList<CommissionStatementBean> aQueryList = new ArrayList<CommissionStatementBean>();
		itsLogger.info("esStatement Details.");
		String whereCondition ="";
		if(ecStatementID>0){
			whereCondition = "AND ecInvoiceRepSplit.ecStatementID = "+ecStatementID;
		}
		String	aCommissionQry = "SELECT ecInvoicePeriod.ExpjoMasterID, ecInvoicePeriod.ExprxMasterID, ecInvoicePeriod.joReleaseID AS joReleaseID2,"
				+ "joReleaseDetail.joReleaseID, rxMaster.rxMasterID, rxMaster.Name, rxMaster.FirstName, IFNULL(joMaster.JobNumber,'-') AS JobNumber, "
				+ "IFNULL(joMaster.Description,rxMaster.Name) AS JobName, cuInvoice.QuickJobName, (CASE ecInvoicePeriod.ReleaseType "
				+ "WHEN  1 THEN 'Drop Ship' WHEN  2 THEN 'Stock Order' WHEN  3 THEN 'Bill Only' WHEN  4 THEN 'Commission' "
				+ "WHEN  5 THEN 'Service' WHEN ecInvoicePeriod.ReleaseType IS NULL THEN 'Sales' END ) as releaseType, ecInvoicePeriod.Sales, "
				+ "ecInvoicePeriod.Sales AS TotalSales, ecInvoicePeriod.Profit AS TotalProfit, ecInvoicePeriod.Reference, ecInvoicePeriod.DatePaid,"
				+ "ecInvoiceRepSplit.Profit, ecInvoiceRepSplit.CommissionRate, ecInvoiceRepSplit.AmountDue,cuInvoice.SubTotal + cuInvoice.Freight "
				+ "AS Gross,ecInvoiceRepSplit.ecStatementID "
				+ "FROM(joReleaseDetail RIGHT JOIN (((ecInvoiceRepSplit LEFT JOIN ecInvoicePeriod ON "
				+ "ecInvoiceRepSplit.ecInvoicePeriodID = ecInvoicePeriod.ecInvoicePeriodID) LEFT JOIN rxMaster ON "
				+ "ecInvoicePeriod.rxCustomerID = rxMaster.rxMasterID) LEFT JOIN cuInvoice ON ecInvoicePeriod.cuInvoiceID = cuInvoice.cuInvoiceID) "
				+ "ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) LEFT JOIN joMaster ON "
				+ "joReleaseDetail.joMasterID = joMaster.joMasterID WHERE ecInvoicePeriod.DatePaid IS NOT NULL AND ecInvoiceRepSplit.AmountDue <> 0.0000 "+whereCondition
				+ " ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";
		
		itsLogger.info(" aCommissionQry :: "+aCommissionQry);
		CommissionStatementBean aCommissionStatementBean = null;
		try{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCommissionQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCommissionStatementBean = new CommissionStatementBean();
				Object[] aObj = (Object[])aIterator.next();
				aCommissionStatementBean.setEcInperiodExpjoMasterID((Integer) aObj[0]);
				aCommissionStatementBean.setEcInperiodExprxMasterID((Integer) aObj[1]);
				aCommissionStatementBean.setEcInperiodjoReleaseID((Integer) aObj[2]);
				aCommissionStatementBean.setJoReleaseID((Integer) aObj[3]);
				aCommissionStatementBean.setRxMasterID((Integer) aObj[4]);
				aCommissionStatementBean.setRxName((String) aObj[5]);
				aCommissionStatementBean.setRxFirstName((String) aObj[6]);
				aCommissionStatementBean.setJobNumber((String) aObj[7]);
				aCommissionStatementBean.setJobName((String) aObj[8]);
				aCommissionStatementBean.setQuickJobName((String) aObj[9]);
				aCommissionStatementBean.setReleaseType((String) aObj[10]);
				aCommissionStatementBean.setEcInperiodsales((BigDecimal) aObj[11]);
				aCommissionStatementBean.setTotalSales((BigDecimal) aObj[12]);
				aCommissionStatementBean.setTotalProfit((BigDecimal) aObj[13]);
				aCommissionStatementBean.setReference((String) aObj[14]);
				if(aObj[15]!=null){
					aCommissionStatementBean.setDatePaid((String) aObj[15].toString());
				}
				else{
					aCommissionStatementBean.setDatePaid("0000-00-00 00-00-00");
				}
				aCommissionStatementBean.setEcRepSplitProfit((BigDecimal) aObj[16]);
				aCommissionStatementBean.setEcInrepsplitCommissionRate((BigDecimal) aObj[17]);
				aCommissionStatementBean.setEcInrepsplitAmountDue((BigDecimal) aObj[18]);
				aCommissionStatementBean.setGrossTotal((BigDecimal) aObj[19]);
				aCommissionStatementBean.setEcStatementID((Integer) aObj[20]);
				aQueryList.add(aCommissionStatementBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCommissionQry =null;aQuery =null;
		}
		return aQueryList;
	}
}
