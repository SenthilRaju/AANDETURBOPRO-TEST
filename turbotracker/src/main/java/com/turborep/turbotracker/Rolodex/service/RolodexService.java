package com.turborep.turbotracker.Rolodex.service;

import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
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

/**
 * 
 * @author vish_pepala
 */
public interface RolodexService  {
	
	public List<RolodexBean> getAll(int from, int rows,String category);
	
	/**
	 * This method is used to get the all customers with in the limit of 'from' and 'to'.
	 * @param from lower limit
	 * @param to Upper limit
	 * @return
	 */
	public List<RolodexBean> getCustomers(int from, int to);
	
	public List<RolodexBean> getEmployees(int from, int to,int aActive);
	
	public int getRecordCount(String theWhereClause);
	
	public int getRecordCountEmployee(String theWhereClause, int aInActive);
	
	public String getRxCategoty3(int rxCategory3ID);
	
	public Rxaddress getAddress(int rxMasterID);
	
	public List<Rxcontact> getContacts(int rxMasterId);

	public ArrayList<Rxcontact> getCustomerContactDetails(Rxcontact theRxcontact);

	public ArrayList<Rxaddress> getCustomerAddressDetails(Rxaddress theRxmaster);
	
	public ArrayList<Rxcontact> getEmployeeContactDetails(Rxcontact theRxcontact);

	public ArrayList<Rxaddress> getEmployeeAddressDetails(Rxaddress theRxaddress);

	public ArrayList<RxJournal> getRxJournalDetails(RxJournal aRxJounal);

	/*public ArrayList<RxJournal> getCustomerJournalDetails(RxJournal aRxJounal);

	public ArrayList<RxJournal> getEmployeeJournalDetails(RxJournal aRxJounal);*/

	public ArrayList<JobsBean> getCustomerJobsDetails(String aJobsBean);

	public ArrayList<AutoCompleteBean> getCustomerNameList(String theCustomerName);

	public ArrayList<Rxcontact> getRolodexContactDetails(Rxcontact theRxcontact);

	/**
	 * This method is used to add a new contact for a rolodex.
	 * @param rolodexContact {@link Rxcontact}
	 * @return {@link Boolean}
	 */
	public boolean addRolodexContact(Rxcontact rolodexContact);
	
	/**
	 * This method is used to edit a contact.
	 * @param rolodexContact {@link Rxcontact}
	 * @return {@link Boolean}
	 */
	public boolean editRolodexContact(Rxcontact rolodexContact);
	
	public Rxmaster addNewCustomer(Rxmaster aCustomer, Rxaddress aRxaddress,Integer uerid);

	public int getRxMasterId(String name);

	public int updateCustomerName(String customerName, int customerId);

	public boolean addRolodexJournal(RxJournal aRxjournal);

	public boolean updateRolodexJournal(RxJournal aRxjournal);

	public ArrayList<JobsBean> getContactIdbasedOpenQuotes(String theRolodexId, int jobStatus, Integer theContactId);

	public ArrayList<JobsBean> getCustomerLostQuotes(String theRolodexId, int jobStatus, Integer theContactId);
	
	public boolean updateCuMasterDetailsRecord(Cumaster theCuMaster);

	public Integer addQuickQuote(Jomaster aJomaster);

	public boolean addQuickQuoteBidder(Jobidder aJobidder);

	public boolean addQuickQuoteCuMaster(Cuinvoice aCuinvoice);

	public ArrayList<AutoCompleteBean> getCustomerFirstContactList(String theFilterName);

	public ArrayList<AutoCompleteBean> getCustomerLastContactList(String theFilterName);

	public Rxmaster addNewRolodex(Rxmaster aRolodex, Rxaddress aRxaddress,Integer userid);

	public Rxmaster getPhone(int rxMasterID);
	
	public Rxmaster addNewCustomerAddress(Rxmaster aCustomer,Rxaddress aRxaddress) throws CustomerException;

	public Integer getRxAddressID(Integer theRxmasterId, String address1);

	public Cumaster addNewAssignee(Cumaster acuMaster);

	public Cumaster updateAssignee(Cumaster acuMaster);

	public Rxmaster addNewVendorAddress(Rxaddress aRxaddress);
	
	public String getQuoteRev(Integer theRxMasterID, Integer theRxContactID);

	public ArrayList<JobsBean> getCustomerQuotes(String theRolodexId, int jobStatus, Integer theContactId);
	
	public boolean editQuickQuote(Jomaster aJomaster);

	public boolean editQuickQuoteBidder(Jobidder aJobidder);

	public boolean editQuickQuoteCuMaster(Cuinvoice aCuinvoice);

	public boolean updateRolodexDetails(Rxmaster aRxmaster,Integer userid);
	
	public void getBidderIDList(Rxcontact arxContactId);

	public boolean updateQuickQuoteCuMaster(Cuinvoice aCuinvoice);

	public JoQuoteHeader getQuoteHeadID(JoQuoteHeader theQuoteHeaderID);

	public String getTypeID(Integer theQuoteType);

	public JoQuoteHeader addQuoteHeaderId(JoQuoteHeader theQuoteHeaderID);
	public CoTaxTerritory getCotaxterritory(Integer theCountyID);
	public boolean deleteRolodexJournal(RxJournal aRxjournal);
	public ArrayList<EmMasterBean> getEmployeeCommissions(Integer periodID);
	public Emmaster updateCommissions(Emmaster theEmmaster);
	public int updateCustomerInActive(Boolean theCustomerStatus, int theCustomerId);
	public List<Rxcontact> getContacts(String rxMasterId);

	public boolean updateRxaddressfromCustomerFinancial(Rxaddress therxaddress)throws CustomerException;

	public boolean updatedefaultstatusbasedoncustomer(Rxaddress aCustomer)throws CustomerException;

	public boolean addEngineerTitles(Rxmastercategory2 aRxmastercategory2);

	public boolean  updateEngineerTitles(Rxmastercategory2 aRxmastercategory2);

	public boolean addArchitectTitles(Rxmastercategory1 aRxmastercategory1);

	public boolean updateArchitectTitles(Rxmastercategory1 aRxmastercategory1);

	public boolean addViewsTitles(RxMasterCategoryView aRxmastercategoryview);

	public boolean updateViewTitles(RxMasterCategoryView aRxmastercategoryview);

	public ArrayList<AutoCompleteBean> getCustomerNameListFromEmployee(String theCustomerName, Integer tsUserLoginID);

	public List<RolodexBean> getCustomersFromLogin(int theFrom, int theTo,	Integer tsUserLoginID);

	public int getRecordCountFromEmployee(Integer tsUserLoginID);
	
	public boolean updateRxMasterDetails(Rxmaster aRxmaster);
	
	public boolean checkCustomeravailability(int theRxmaster);
	
	public boolean deleteCustomer(int theRxmaster);
	
	//public boolean updateCustomerasInactive(int theRxmaster);

	public Rxmaster deleteVendorAddress(Integer rxAddressId);
	
	public boolean deleteAddressBasedonCustomer(Rxaddress aRxaddressparameter) throws CustomerException;

	public Boolean TemporaryMethodforInsert(Rxmaster theRxMaster,int userloginid);
	
	public EmMasterBean getEmployeeCommissionStatement(Integer UserLoginID,Integer ecPeriodID);

	public boolean updateChildTableBaseonChkbx(Integer rxMasterID, Integer userID);

	Boolean updateotherrxaddress(Integer rxcustomerID);

	public boolean updateRxMasterfromempDetails(Rxmaster theRxmaster);

	public boolean updateRxaddress(Rxaddress therxaddress) throws CustomerException;
	
	public boolean updateRemittorxaddress(Integer rxMasterId)throws CustomerException;
	
	public boolean updateDefaultrxaddress(Integer rxMasterId)throws CustomerException;
	
	//Creadted by :Leo  Reason: Add/Edit/Delete Rolodex Address. 
	public boolean addoreditRolodexAddress(Rxaddress therxaddress,String Oper)throws CustomerException;
	
	public Emmaster updateCommissionsGeneral(Emmaster theEmmaster);
}
