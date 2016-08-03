package com.turborep.turbotracker.vendor.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.dao.TpInventoryLog;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLogMaster;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.dao.VeBillPaymentHistory;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebilldetail;
import com.turborep.turbotracker.vendor.dao.Vebilldistribution;
import com.turborep.turbotracker.vendor.dao.Vebillpay;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.dao.VendorBillPayBean;
import com.turborep.turbotracker.vendor.dao.VendorBillsBean;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Vereceive;
import com.turborep.turbotracker.vendor.dao.Vereceivedetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;
import com.turborep.turbotracker.vendor.exception.VendorException;

@Service("vendorService")
@Transactional(rollbackFor = Exception.class)
public class VendorService implements VendorServiceInterface{

	
	Logger itsLogger = Logger.getLogger(VendorService.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name = "jobService")
	private JobService jobService;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Override
	public List<RolodexBean> getAll(int theFrom, int theTo) throws VendorException {
		itsLogger.debug("Retrieving all persons");
		StringBuilder aVendorSelectQry = new StringBuilder("SELECT rxMaster.rxMasterID, rxMaster.name, rxMaster.phone1, rxAddress.city, rxAddress.state, rxAddress.address1 ")
															.append("FROM rxMaster JOIN rxAddress on rxMaster.rxMasterID = rxAddress.rxMasterID ")
															.append("WHERE rxMaster.isVendor = 1 GROUP BY rxMaster.rxMasterID ORDER BY rxMaster.name LIMIT ").append(theFrom).append(", ").append(theTo);
		Session aSession = null;
		List<RolodexBean> aQueryList = new ArrayList<RolodexBean>();
		RolodexBean aRolodexBean = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aVendorSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new RolodexBean();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setRxMasterId((Integer) aObj[0]);  /**	rxMaster.rxMaserId	*/
				aRolodexBean.setName((String) aObj[1]);			/** rxMaster.name	*/
				aRolodexBean.setPhone1((String) aObj[2]);		/**	rxMaster.phone1	*/
				aRolodexBean.setCity((String)aObj[3]);			/**	rxAddress.city	*/
				aRolodexBean.setState((String) aObj[4]);
				aRolodexBean.setAddress1((String) aObj[5]);		/**	rxAddress.state	*/
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorSelectQry = null;
		}
		return  aQueryList;
	}
	
	@Override
	public List<Vemaster> getAllVendors() throws VendorException {
		itsLogger.debug("Retrieving all persons");
		StringBuilder aVendorSelectQry = new StringBuilder("select veMasterID,Manufacturer from  veMaster where Manufacturer <> '' ");
//															.append("FROM rxMaster JOIN rxAddress on rxMaster.rxMasterID = rxAddress.rxMasterID ")
//															.append("WHERE rxMaster.isVendor = 1");
		Session aSession = null;
		List<Vemaster> aQueryList = new ArrayList<Vemaster>();
		Vemaster aRolodexBean = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aVendorSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new Vemaster();
				Object[] aObj = (Object[])aIterator.next();
				aRolodexBean.setVeMasterId((Integer) aObj[0]); 
				aRolodexBean.setManufacturer((String) aObj[1]);
//				System.out.println(" aRolodexBean.getManufacturer() :"+aRolodexBean.getManufacturer());
//				System.out.println(" aRolodexBean.getVeMasterId() :"+aRolodexBean.getVeMasterId());
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorSelectQry = null;
		}
		return  aQueryList;
	}

	@Override
	public int getVendorsCount() throws VendorException {
		String aJobCountStr = "SELECT COUNT(rxMasterID) AS count FROM rxMaster WHERE isVendor = 1";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public ArrayList<Rxcontact> getVendorContactDetails(Rxcontact theRxcontact) throws VendorException {
		StringBuilder vendorQry = new StringBuilder("SELECT LastName, FirstName, JobPosition,Phone, EMail, Cell, Division,rxContactId, DirectLine,extension FROM rxContact r WHERE rxMasterID = '")
											.append(theRxcontact.getRxMasterId()).append("'");
		Session aSession = null;
		ArrayList<Rxcontact> aVendorContactQry = new ArrayList<Rxcontact>();
		try{
			Rxcontact avendorContacts = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(vendorQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				avendorContacts = new Rxcontact();
				Object[] aObj = (Object[])aIterator.next();
				avendorContacts.setLastName((String) aObj[0]);
				avendorContacts.setFirstName((String) aObj[1]);
				avendorContacts.setJobPosition((String) aObj[2]);
				avendorContacts.setPhone((String) aObj[3]);
				avendorContacts.setEmail((String) aObj[4]);
				avendorContacts.setCell((String) aObj[5]);
				avendorContacts.setDivision((String) aObj[6]);
				avendorContacts.setRxContactId((Integer) aObj[7]);
				avendorContacts.setDirectLine((String) aObj[8]);
				avendorContacts.setExtension((String) aObj[9]);
				aVendorContactQry.add(avendorContacts);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			vendorQry = null;
		}
		return aVendorContactQry;
	}

	@Override
	public ArrayList<Rxaddress> getVendorAddressDetails(Rxaddress theRxaddress) throws VendorException {
		String vendorQry = "SELECT ra.Address1, ra.City, ra.State, ra.Zip, ra.Phone1, ra.Fax,ra.Address2,ra.Phone2,ra.IsMailing,ra.IsShipTo, ra.rxAddressID,ra.IsRemitTo ,ra.IsDefault"
				+ " FROM rxAddress ra JOIN rxMaster rxm ON rxm.rxMasterID = ra.rxMasterID WHERE ra.rxMasterID = '" + theRxaddress.getRxMasterId() + "' " ;
	Session aSession = null;
	ArrayList<Rxaddress> vendorAddressQry = new ArrayList<Rxaddress>();
	try{
	Rxaddress aVendorAddress = null;
	aSession = itsSessionFactory.openSession();
	Query aQuery = aSession.createSQLQuery(vendorQry);
	Iterator<?> aIterator = aQuery.list().iterator();
	while (aIterator.hasNext()) {
	aVendorAddress = new Rxaddress();
	Object[] aObj = (Object[])aIterator.next();
	aVendorAddress.setAddress1((String) aObj[0]);
	aVendorAddress.setCity((String) aObj[1]);
	aVendorAddress.setState((String) aObj[2]);
	aVendorAddress.setZip((String) aObj[3]);
	aVendorAddress.setPhone1((String) aObj[4]);
	aVendorAddress.setFax((String) aObj[5]);
	aVendorAddress.setAddress2((String) aObj[6]);
	aVendorAddress.setPhone2((String) aObj[7]);
	Byte ismail= ((Byte) aObj[8]);
	if(ismail == 1){
	aVendorAddress.setIsMailing(true);
	}
	Byte isship= ((Byte) aObj[9]);
	if(isship == 1){
	aVendorAddress.setIsShipTo(true);
	}
	aVendorAddress.setRxAddressId((Integer) aObj[10]);
	
	Byte isremit= ((Byte) aObj[11]);
	if(isremit == 1){
	aVendorAddress.setIsRemitTo(true);
	}
	else
	{
	aVendorAddress.setIsRemitTo(false);
	}
	
	Byte isdefault= ((Byte) aObj[12]);
	if(isdefault == 1){
	aVendorAddress.setIsDefault(true);
	}
	else
	{
	aVendorAddress.setIsDefault(false);
	}
	vendorAddressQry.add(aVendorAddress);
	}
	} catch (Exception excep) {
	itsLogger.error(excep.getMessage(), excep);
	throw new VendorException(excep.getMessage(), excep);
	} finally {
	aSession.flush();
	aSession.close();
	vendorQry = null;
	}
	return vendorAddressQry;
	}



	
	public ArrayList<VeFactory> getManufacturers(String rxMasterId) throws VendorException {
		String veManufacturerQry = "FROM VeFactory WHERE rxMasterId = " + rxMasterId ;
		Session aSession = null;
		ArrayList<VeFactory> aVeManufacturers = new ArrayList<VeFactory>();
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(veManufacturerQry);
			aVeManufacturers = (ArrayList<VeFactory>) aQuery.list();
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			veManufacturerQry = null;
		}
		return aVeManufacturers;
	}

	public ArrayList<Vemaster> getVendorMaster(String rxMasterId) throws VendorException {
		StringBuilder veMasterQry = new StringBuilder("SELECT veMasterId, dueDays, disCountDays, discountPercent, dueOnDay, discOnDay, discountIncludesFreight, manufacturer, coExpenseAccountId, ssn, importType, accountNumber,coAccount.Description ")
											.append(" FROM veMaster LEFT JOIN coAccount ON(veMaster.coExpenseAccountId=coAccount.coAccountID)  WHERE veMasterID = ").append(rxMasterId);
		Session aSession = null;
		ArrayList<Vemaster> aVeMasterRecords = null;
		try {
			aVeMasterRecords = new ArrayList<Vemaster>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(veMasterQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			Vemaster aVeMaster = null;
			while (aIterator.hasNext()) {
				aVeMaster = new Vemaster();
				Object[] aObj = (Object[])aIterator.next();
				aVeMaster.setVeMasterId((Integer) aObj[0]);
				aVeMaster.setDueDays((Short) aObj[1]);
				aVeMaster.setDiscountDays((Short) aObj[2]);
				BigDecimal dicPercent = (BigDecimal) aObj[3];
				
				aVeMaster.setDiscountPercent(dicPercent.setScale(2));
				if((Byte)aObj[4] == 0){
					aVeMaster.setDueOnDay(false);
				} else {
					aVeMaster.setDueOnDay(true);
				}
				if((Byte)aObj[5] == 0){
					aVeMaster.setDiscOnDay(false);
				} else {
					aVeMaster.setDiscOnDay(true);
				}
				if((Byte)aObj[6] == 0){
					aVeMaster.setDiscountIncludesFreight(false);
				} else {
					aVeMaster.setDiscountIncludesFreight(true);
				}
				if(aObj[7] != null){
					aVeMaster.setManufacturer((String) aObj[7]);
				}
				if(aObj[8] != null){
					aVeMaster.setCoExpenseAccountId((Integer) aObj[8]);
				}
				if(aObj[9] != null){
					aVeMaster.setSsn((String) aObj[9]);
				}
				if(aObj[10] != null){
					aVeMaster.setImportType((Integer) aObj[10]);
				}
				if(aObj[11] != null){
					aVeMaster.setAccountNumber((String) aObj[11]);
				}
				aVeMaster.setCoaccountDescription((String) aObj[12]);
				aVeMasterRecords.add(aVeMaster);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			veMasterQry = null;
		}
		return aVeMasterRecords;
	}

	@Override
	public Rxmaster addNewVendor(Rxmaster theCustomer, Rxaddress theRxaddress) throws VendorException {
		itsLogger.debug("Query has to get jobNumber and date");
		Session rxMasterSession = itsSessionFactory.openSession();
		Transaction rxMasterTransaction;
		Rxmaster aRxMaster = null;
		try {
			rxMasterTransaction = rxMasterSession.beginTransaction();
			rxMasterTransaction.begin();
			int rxmasterid=(Integer) rxMasterSession.save(theCustomer);
			itsLogger.info("rxmasterid===>"+rxmasterid);
			Vemaster aVemaster = (Vemaster) rxMasterSession.get(Vemaster.class,rxmasterid);
			if(aVemaster==null){
				aVemaster=new Vemaster();
				aVemaster.setVeMasterId(rxmasterid);
				aVemaster.setImportType(0);
				 rxMasterSession.save(aVemaster);
			}
			theRxaddress.setRxMasterId(theCustomer.getRxMasterId());
			int Rxaddress=(Integer)rxMasterSession.save(theRxaddress);
			itsLogger.info("theRxaddress===>"+Rxaddress);
			rxMasterTransaction.commit();
			aRxMaster = (Rxmaster) rxMasterSession.get(Rxmaster.class, theCustomer.getRxMasterId());
		} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				throw new VendorException(excep.getMessage(), excep);
		} finally {
			rxMasterSession.flush();
			rxMasterSession.close();
		}
		return aRxMaster;
	}
	
	@Override
	public boolean addnewFactory(Rxmaster aNewVendor) throws VendorException{
		Session rxMasterSession = itsSessionFactory.openSession();
		Transaction rxMasterTransaction;
		VeFactory aVeFactory=new VeFactory();
		try
		{
			aVeFactory.setRxMasterId(aNewVendor.getRxMasterId());
			aVeFactory.setDescription(aNewVendor.getName());
			rxMasterTransaction = rxMasterSession.beginTransaction();
			rxMasterTransaction.begin();
			rxMasterSession.save(aVeFactory);
			rxMasterTransaction.commit();
		}catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			rxMasterSession.flush();
			rxMasterSession.close();
		}
		return true;
	}

	@Override
	public VeFactory updateManufactures(VeFactory theVeFactory) throws VendorException {
		Session aSession = itsSessionFactory.openSession();
		VeFactory aFactory = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			VeFactory aVeFactory = (VeFactory) aSession.get(VeFactory.class, theVeFactory.getVeFactoryID());
			if(aVeFactory!=null){
			aVeFactory.setDescription(theVeFactory.getDescription());
			aVeFactory.setInActive(theVeFactory.isInActive());
			aVeFactory.setVeFactoryID(theVeFactory.getVeFactoryID());
			aSession.update(aVeFactory);
			transaction.commit();
			aFactory = (VeFactory) aSession.get(VeFactory.class, theVeFactory.getVeFactoryID());
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aFactory;
	}

	@Override
	public String getDescription(Short thefactoryID) throws VendorException {
		String aJobCountStr = "SELECT Description FROM veFactory WHERE veFactoryID ='"+thefactoryID+"'";
		Session aSession = null;
		String aFactory = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			if(!aList.isEmpty()){
				aFactory = (String) aList.get(0);
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aFactory;
	}
	
	@Override
	public Vepo getVePo(Integer theVePOID) throws VendorException {
		Vepo aPO = new Vepo();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(null != theVePOID){
				aPO = (Vepo) aSession.get(Vepo.class, theVePOID);
				if(aPO != null){
					if(aPO.getTag() == null){
						aPO.setTag("");
					}if(aPO.getDateWanted() == null){
						aPO.setDateWanted("");
					}if(aPO.getCustomerPonumber() == null){
						aPO.setCustomerPonumber("");
					}if(aPO.getSpecialInstructions() == null){
						aPO.setSpecialInstructions("");
					}
				}
			}
			else
			{
				aPO.setTag("");
				aPO.setDateWanted("");
				aPO.setCustomerPonumber("");
				aPO.setSpecialInstructions("");
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPO;
	}
	
	@Override
	public Vebill getVebill(Integer veBillID) throws VendorException {
		Vebill aVebill = new Vebill();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(null != veBillID){
				aVebill = (Vebill) aSession.get(Vebill.class, veBillID);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVebill;
	}

	@Override
	public Rxcontact getContactDetails(Integer theContactID) throws VendorException {
		Rxcontact aRxcontact = new Rxcontact();
		Session aSession = null;
		try {
			if(theContactID==null)
			{
				theContactID =-1;
			}
			
			aSession = itsSessionFactory.openSession();
			aRxcontact = (Rxcontact) aSession.get(Rxcontact.class, theContactID);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxcontact;
	}
	
	public Veshipvia getVeShipVia(Integer theShipViaID) throws VendorException {
		Veshipvia aVeshipvia = new Veshipvia();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aVeshipvia = (Veshipvia) aSession.get(Veshipvia.class, theShipViaID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVeshipvia;
	}

	@Override
	public ArrayList<VendorBillPayBean> getVendorBillsList(Integer rxMasterID, int thePage, int theRows, String column,String sortBy,String DisplayType) throws VendorException {
		/*StringBuilder aVendorBillPayQryStr = null;
		if(rxMasterID != null){
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount, veBillPay.ApplyingAmount, veBillPay.ApplyingAmount,veBillPay.DiscountAmount")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" LEFT JOIN veBillPay veBillPay ON veBill.veBillID = veBillPay.veBillID")
				.append(" WHERE (veBill.TransactionStatus=1) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount) AND rxMaster.rxMasterID = ")
				.append( rxMasterID );
		}else{
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount, veBillPay.ApplyingAmount,veBillPay.DiscountAmount")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" LEFT JOIN veBillPay veBillPay ON veBill.veBillID = veBillPay.veBillID")
				.append(" WHERE (veBill.TransactionStatus=1) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount)");
		}
		
		if(rxMasterID != null){
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount, veBillPay.ApplyingAmount, veBillPay.ApplyingAmount,veBillPay.DiscountAmount,veBill.TransactionStatus,veBill.Reason")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" LEFT JOIN veBillPay veBillPay ON veBill.veBillID = veBillPay.veBillID")
				.append(" WHERE ( veBill.TransactionStatus=1 OR veBill.TransactionStatus=-2 ) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount) AND rxMaster.rxMasterID = ")
				.append( rxMasterID );
		}else{
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount, veBillPay.ApplyingAmount,veBillPay.DiscountAmount,veBill.TransactionStatus,veBill.Reason")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" LEFT JOIN veBillPay veBillPay ON veBill.veBillID = veBillPay.veBillID")
				.append(" WHERE (veBill.TransactionStatus=1  OR veBill.TransactionStatus=-2) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount)");
		}
		if(DisplayType.equals("Purchases"))
		{
			aVendorBillPayQryStr.append(" AND veBill.vePOID IS NOT NULL");
		}
		else if(DisplayType.equals("Expenses"))
		{
			aVendorBillPayQryStr.append(" AND veBill.vePOID IS NULL");
		}
		
		String sortByOption ="";
//		column = column.split(", ")[1];
		if(column.equalsIgnoreCase("vendor asc, dueDate")){
			sortByOption=" veBill.DueDate ";
		}else if(column.equalsIgnoreCase("vendor asc, ponumber")){
			sortByOption=" vePO.PONumber ";
		}else if(column.equalsIgnoreCase("vendor asc, invoiceNumber")){
			sortByOption=" veBill.InvoiceNumber ";
		}else if(column.equalsIgnoreCase("vendor asc, billDate")){
			sortByOption=" veBill.BillDate ";
		}
		else if(column.equalsIgnoreCase("vendor asc, balance")){
			sortByOption=" Balance ";
		}
		else if(column.equalsIgnoreCase("vendor asc, vendor")){
			sortByOption=" Vendor ASC,veBill.DueDate ";
		}
		if(column.equalsIgnoreCase("vendor asc, applyingAmount")){
			sortByOption=" veBill.AppliedAmount ";
		}
		else if(column.equalsIgnoreCase("dueDate")){
			sortByOption=" veBill.DueDate ";
		}
		else if(column.equalsIgnoreCase("ponumber")){
			sortByOption=" vePO.PONumber ";
		}else if(column.equalsIgnoreCase("invoiceNumber")){
			sortByOption=" veBill.InvoiceNumber ";
		}else if(column.equalsIgnoreCase("billDate")){
			sortByOption=" veBill.BillDate ";
		}
		else if(column.equalsIgnoreCase("balance")){
			sortByOption=" Balance ";
		}
		else if(column.equalsIgnoreCase("vendor")){
			sortByOption=" Vendor ";
		}
		
		if((sortByOption+sortBy.toUpperCase()).trim().length() > 3)
			aVendorBillPayQryStr.append(" ORDER BY "+sortByOption+sortBy.toUpperCase()+";");
		else
			aVendorBillPayQryStr.append(" ORDER BY Vendor, veBill.DueDate;");
		
		Session aSession = null;
		ArrayList<VendorBillPayBean> aBillPayList = null;
		VendorBillPayBean aVendorBillPayBean = null;
		try {
			aBillPayList = new ArrayList<VendorBillPayBean>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aVendorBillPayQryStr.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillPayBean = new VendorBillPayBean();
				Object[] aObj = (Object[])aIterator.next	();
				if(aObj[0] != null){
					aVendorBillPayBean.setDueDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aVendorBillPayBean.setVendor((String) aObj[1]);
				aVendorBillPayBean.setPONumber((String) aObj[2]);
				
				//System.out.println("PO Number:::"+(String) aObj[2]);
				
				aVendorBillPayBean.setInvoiceNumber((String) aObj[3]);
				if (aObj[4] != null) {
					aVendorBillPayBean.setBillDate((String)DateFormatUtils.format((Date)aObj[4], "MM/dd/yyyy"));
				}
				aVendorBillPayBean.setBalance((BigDecimal) aObj[5]);
				aVendorBillPayBean.setBillAmount((BigDecimal) aObj[6]);
				aVendorBillPayBean.setAppliedAmount((BigDecimal) aObj[7]);
				aVendorBillPayBean.setVeBillID((Integer) aObj[8]);
				aVendorBillPayBean.setVendorRxMasterID((Integer) aObj[9]);
				aVendorBillPayBean.setFreightAmount((BigDecimal) aObj[10]);
				aVendorBillPayBean.setApplyingAmount((BigDecimal) aObj[11]);
				aVendorBillPayBean.setDiscountAmount((BigDecimal) aObj[12]);
				aBillPayList.add(aVendorBillPayBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException excep = new VendorException(e.getMessage(), e);
			throw excep;
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillPayQryStr = null;
		}
		return aBillPayList;*/
		StringBuilder aVendorBillPayQryStr = null;
		if(rxMasterID != null){
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount, veBillPay.ApplyingAmount, veBillPay.ApplyingAmount,veBillPay.DiscountAmount,veBill.TransactionStatus,veBill.Reason")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" LEFT JOIN veBillPay veBillPay ON veBill.veBillID = veBillPay.veBillID")
				.append(" WHERE ( veBill.TransactionStatus=1 OR veBill.TransactionStatus=-2 ) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount) AND rxMaster.rxMasterID = ")
				.append( rxMasterID );
		}else{
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount, veBillPay.ApplyingAmount,veBillPay.DiscountAmount,veBill.TransactionStatus,veBill.Reason")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" LEFT JOIN veBillPay veBillPay ON veBill.veBillID = veBillPay.veBillID")
				.append(" WHERE (veBill.TransactionStatus=1  OR veBill.TransactionStatus=-2) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount)");
		}
		
		if(DisplayType.equals("Purchases"))
		{
			aVendorBillPayQryStr.append(" AND veBill.vePOID IS NOT NULL");
		}
		else if(DisplayType.equals("Expenses"))
		{
			aVendorBillPayQryStr.append(" AND veBill.vePOID IS NULL");
		}
		
		String sortByOption ="";
//		column = column.split(", ")[1];
		if(column.equalsIgnoreCase("vendor asc, dueDate")){
			sortByOption=" veBill.DueDate ";
		}else if(column.equalsIgnoreCase("vendor asc, ponumber")){
			sortByOption=" vePO.PONumber ";
		}else if(column.equalsIgnoreCase("vendor asc, invoiceNumber")){
			sortByOption=" veBill.InvoiceNumber ";
		}else if(column.equalsIgnoreCase("vendor asc, billDate")){
			sortByOption=" veBill.BillDate ";
		}
		else if(column.equalsIgnoreCase("vendor asc, balance")){
			sortByOption=" Balance ";
		}
		else if(column.equalsIgnoreCase("vendor asc, vendor")){
			sortByOption=" Vendor ASC,veBill.DueDate ";
		}
		if(column.equalsIgnoreCase("vendor asc, applyingAmount")){
			sortByOption=" veBill.AppliedAmount ";
		}
		else if(column.equalsIgnoreCase("dueDate")){
			sortByOption=" veBill.DueDate ";
		}
		else if(column.equalsIgnoreCase("ponumber")){
			sortByOption=" vePO.PONumber ";
		}else if(column.equalsIgnoreCase("invoiceNumber")){
			sortByOption=" veBill.InvoiceNumber ";
		}else if(column.equalsIgnoreCase("billDate")){
			sortByOption=" veBill.BillDate ";
		}
		else if(column.equalsIgnoreCase("balance")){
			sortByOption=" Balance ";
		}
		else if(column.equalsIgnoreCase("vendor")){
			sortByOption=" Vendor ";
		}
		
		if((sortByOption+sortBy.toUpperCase()).trim().length() > 3)
			aVendorBillPayQryStr.append(" ORDER BY "+sortByOption+sortBy.toUpperCase()+";");
		else
			aVendorBillPayQryStr.append(" ORDER BY Vendor, veBill.DueDate;");
		
		Session aSession = null;
		ArrayList<VendorBillPayBean> aBillPayList = null;
		VendorBillPayBean aVendorBillPayBean = null;
		try {
			aBillPayList = new ArrayList<VendorBillPayBean>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aVendorBillPayQryStr.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillPayBean = new VendorBillPayBean();
				Object[] aObj = (Object[])aIterator.next	();
				if(aObj[0] != null){
					aVendorBillPayBean.setDueDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aVendorBillPayBean.setVendor((String) aObj[1]);
				aVendorBillPayBean.setPONumber((String) aObj[2]);
				
				//System.out.println("PO Number:::"+(String) aObj[2]);
				
				aVendorBillPayBean.setInvoiceNumber((String) aObj[3]);
				if (aObj[4] != null) {
					aVendorBillPayBean.setBillDate((String)DateFormatUtils.format((Date)aObj[4], "MM/dd/yyyy"));
				}
				aVendorBillPayBean.setBalance((BigDecimal) aObj[5]);
				aVendorBillPayBean.setBillAmount((BigDecimal) aObj[6]);
				aVendorBillPayBean.setAppliedAmount((BigDecimal) aObj[7]);
				aVendorBillPayBean.setVeBillID((Integer) aObj[8]);
				aVendorBillPayBean.setVendorRxMasterID((Integer) aObj[9]);
				aVendorBillPayBean.setFreightAmount((BigDecimal) aObj[10]);
				aVendorBillPayBean.setApplyingAmount((BigDecimal) aObj[11]);
				aVendorBillPayBean.setDiscountAmount((BigDecimal) aObj[12]);
				aVendorBillPayBean.setTranStatus((short)aObj[13]);
				aVendorBillPayBean.setReason((String)aObj[14]);
				aBillPayList.add(aVendorBillPayBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException excep = new VendorException(e.getMessage(), e);
			throw excep;
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillPayQryStr = null;
		}
		return aBillPayList;
	}
	
	@Override
	public ArrayList<VendorBillPayBean> getVendorBillsQuickPayList(String rxMasterID,Date dueDate) throws VendorException {
		StringBuilder aVendorBillPayQryStr = null;
		/*if(rxMasterID != 0){*/
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount ,veM.DiscountDays,veM.DiscountPercent,veM.DiscOnDay ")
				.append(" FROM veMaster veM, (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" WHERE veM.veMasterID =  rxMaster.rxMasterID AND veBill.veBillID NOT IN (SELECT veBillID FROM veBillPay) and (veBill.TransactionStatus=1) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount) AND rxMaster.rxMasterID =")
				.append( rxMasterID )
				.append(" AND (veBill.BillAmount-veBill.AppliedAmount)>0 AND veBill.DueDate < '"+dueDate+"'" );
		/*}else{
			aVendorBillPayQryStr = new StringBuilder("SELECT veBill.DueDate, CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS Vendor, vePO.PONumber, veBill.InvoiceNumber,")
				.append(" veBill.BillDate, veBill.BillAmount-veBill.AppliedAmount AS Balance, veBill.BillAmount, veBill.AppliedAmount,")
				.append(" veBill.veBillID, veBill.rxMasterID, veBill.FreightAmount ")
				.append(" FROM (veBill LEFT JOIN vePO ON veBill.vePOID = vePO.vePOID) LEFT JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID")
				.append(" WHERE veBill.veBillID NOT IN (SELECT veBillID FROM veBillPay) and (veBill.TransactionStatus=1) AND (veBill.Applied=0) AND (veBill.BillAmount <> veBill.AppliedAmount)")
				.append(" AND (veBill.BillAmount-veBill.AppliedAmount)>0 AND veBill.DueDate < '"+dueDate+"'" );
		}*/
		
		aVendorBillPayQryStr.append(" ORDER BY veBill.DueDate,Vendor,veBill.BillAmount");
		
		Session aSession = null;
		ArrayList<VendorBillPayBean> aBillPayList = null;
		VendorBillPayBean aVendorBillPayBean = null;
		try {
			aBillPayList = new ArrayList<VendorBillPayBean>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aVendorBillPayQryStr.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillPayBean = new VendorBillPayBean();
				Object[] aObj = (Object[])aIterator.next	();
				if(aObj[0] != null){
					aVendorBillPayBean.setDueDate((String)DateFormatUtils.format((Date)aObj[0], "MM/dd/yyyy"));
				}
				aVendorBillPayBean.setVendor((String) aObj[1]);
				aVendorBillPayBean.setPONumber((String) aObj[2]);
				
				aVendorBillPayBean.setInvoiceNumber((String) aObj[3]);
				if (aObj[4] != null) {
					aVendorBillPayBean.setBillDate((String)DateFormatUtils.format((Date)aObj[4], "MM/dd/yyyy"));
				}
				aVendorBillPayBean.setBalance((BigDecimal) aObj[5]);
				aVendorBillPayBean.setBillAmount((BigDecimal) aObj[6]);
				aVendorBillPayBean.setAppliedAmount((BigDecimal) aObj[7]);
				aVendorBillPayBean.setVeBillID((Integer) aObj[8]);
				aVendorBillPayBean.setVendorRxMasterID((Integer) aObj[9]);
				aVendorBillPayBean.setFreightAmount((BigDecimal) aObj[10]);
				aVendorBillPayBean.setDiscountDays((Short)aObj[11]);
				aVendorBillPayBean.setDiscountPercent((BigDecimal)aObj[12]);
				aVendorBillPayBean.setDueOnDay((Byte)aObj[13]);
			//	aVendorBillPayBean.setApplyingAmount((BigDecimal) aObj[11]);
				aBillPayList.add(aVendorBillPayBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException excep = new VendorException(e.getMessage(), e);
			throw excep;
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillPayQryStr = null;
		}
		return aBillPayList;
	}

	@Override
	public Vebillpay changeVendorBillList(Vebillpay theVendorBillsList, boolean isExisting,Integer userID) throws VendorException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction = aSession.beginTransaction();
		String aHQL = null;
		try
		{
			
			System.out.println(""+theVendorBillsList.getVeBillId());
			aTransaction.begin();
			if(isExisting){
				aHQL = "FROM Vebillpay WHERE veBillID = " + theVendorBillsList.getVeBillId();
				List<?> aList = aSession.createQuery(aHQL).list();
				if(!aList.isEmpty()){
					Vebillpay aExistedVebillpay = (Vebillpay) aSession.createQuery(aHQL).list().get(0);
					aExistedVebillpay.setApplyingAmount(theVendorBillsList.getApplyingAmount());
					aExistedVebillpay.setDiscountAmount(theVendorBillsList.getDiscountAmount());
					aExistedVebillpay.setUserID(userID);
					aSession.update(aExistedVebillpay);
				}
			} else {
				Integer aBillPAyID = (Integer) aSession.save(theVendorBillsList);
				theVendorBillsList.setVeBillPayId(aBillPAyID);
			}			
			aTransaction.commit();
		} catch (Exception e) {
			aTransaction.rollback();
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e); 
		} finally {
			aSession.flush();
			aSession.close();
		}
		return theVendorBillsList;
	}

	@Override
	public ArrayList<Vebillpay> getBillPayDetails(Integer thevebilID) throws VendorException {
		String aVendorBillPayQryStr = "SELECT veBillPayID, veBillID, ApplyingAmount, DiscountAmount FROM veBillPay WHERE veBillID = '"+thevebilID+"'";
		Session aSession = null;
		ArrayList<Vebillpay> aBillPayList = null;
		Vebillpay aVendorBillPayBean = null;
		try {
			aBillPayList = new ArrayList<Vebillpay>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aVendorBillPayQryStr);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillPayBean = new Vebillpay();
				Object[] aObj = (Object[])aIterator.next();
				aVendorBillPayBean.setVeBillPayId((Integer) aObj[0]);
				aVendorBillPayBean.setVeBillId((Integer) aObj[1]);
				if((BigDecimal) aObj[2]==null)
					aVendorBillPayBean.setApplyingAmount(new BigDecimal(0));
				else
					aVendorBillPayBean.setApplyingAmount((BigDecimal) aObj[2]);
				if((BigDecimal) aObj[3]==null)
					aVendorBillPayBean.setDiscountAmount(new BigDecimal(0));
				else
					aVendorBillPayBean.setDiscountAmount((BigDecimal) aObj[3]);
				aBillPayList.add(aVendorBillPayBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillPayQryStr = null;
		}
		return aBillPayList;
	}
	
	@Override
	public Vemaster gettermsDiscountsfromveMaster(Integer theveMasterID) throws VendorException {
		
		Session aSession = null;
		Vemaster veMasterList = null;
		try {
			aSession = itsSessionFactory.openSession();
			veMasterList = (Vemaster) aSession.get(Vemaster.class, theveMasterID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		
		return veMasterList;
	}
	

	@Override
	public VeFactory getSingleVeFactoryDetails(Integer theManufacturerID) throws VendorException {
		Session aSession = null;
		VeFactory aVeFactory = null;
		try {
			aSession = itsSessionFactory.openSession();
			aVeFactory = (VeFactory) aSession.get(VeFactory.class, theManufacturerID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aVeFactory;
	}
	
	@Override
	public List<Vebilldetail> getVeBillLineItems(Integer veBillId) throws VendorException{
		Session aSession = null;
		List<Vebilldetail> aVebillLineitemsList = null;
		Vebilldetail aVebilldetail = null;
		String anSqlQry = "SELECT vbd.veBillDetailID, vbd.veBillID, IFNULL(vbd.vePODetailID,0), vbd.prMasterID, pr.ItemCode, vbd.Description, "
							+ "vbd.Note, vbd.QuantityBilled, IFNULL(vbd.UnitCost,0.0000), IFNULL(vbd.PriceMultiplier,0), IFNULL(vbd.FreightCost,0.00),(vbd.QuantityBilled+vpd.QuantityOrdered - (SELECT SUM(QuantityBilled) FROM veBillDetail WHERE vePODetailID = vbd.vePODetailID)) AS remainQuantity,vbd.Taxable "
							+ "FROM vePODetail vpd RIGHT JOIN veBillDetail vbd ON(vpd.vePODetailID = vbd.vePODetailID) RIGHT JOIN prMaster pr ON vbd.prMasterID = pr.prMasterID WHERE  vbd.veBillID = "+veBillId+";";
		try {
			aVebillLineitemsList = new ArrayList<Vebilldetail>();
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(anSqlQry);
			Iterator<?> iterator = query.list().iterator();
			while (iterator.hasNext()) {
				aVebilldetail = new Vebilldetail();
				Object[] aObj = (Object[])iterator.next();

				aVebilldetail.setVeBillDetailId((Integer)aObj[0]);
				aVebilldetail.setVeBillId((Integer)aObj[1]);
				aVebilldetail.setVePodetailId(JobUtil.ConvertintoInteger(aObj[2].toString()));
				aVebilldetail.setPrMasterId((Integer)aObj[3]);
				aVebilldetail.setPrItemCode((String)aObj[4]);
				aVebilldetail.setDescription((String)aObj[5]);
				aVebilldetail.setNote((String)aObj[6]);
				aVebilldetail.setInLineNoteImage((String)aObj[6]);
				BigDecimal qty = BigDecimal.ZERO;
				if(aObj[7]!=null){		
					qty =JobUtil.floorFigureoverall(((BigDecimal)aObj[7]),2);
				}
				aVebilldetail.setQuantityOrdered(qty);
				BigDecimal unitCost = new BigDecimal(0);
				BigDecimal multiplier = new BigDecimal(0);
				BigDecimal atotal = new BigDecimal(0);
				if(aObj[8]!=null){
					unitCost = JobUtil.floorFigureoverall(((BigDecimal)aObj[8]),2);
					
				}

				atotal = qty.multiply(unitCost);
				if(aObj[9] != null){
					multiplier = (BigDecimal)aObj[9];
				if(multiplier.compareTo(new BigDecimal(0))==0){
					atotal= atotal.multiply(new BigDecimal(1));
				}else{
					atotal= atotal.multiply(multiplier);
				}
//					int res = mul
				}
				aVebilldetail.setQuantityBilled(JobUtil.floorFigureoverall(atotal,2));
				aVebilldetail.setUnitCost(unitCost);
				aVebilldetail.setPriceMultiplier((BigDecimal)aObj[9]);
				aVebilldetail.setFreightCost((BigDecimal)aObj[10]);
				aVebilldetail.setRemainQuatity((BigDecimal)aObj[11]);
				
				if ((Byte) aObj[12] == 1) {
					aVebilldetail.setTaxable(true);
				} else {
					aVebilldetail.setTaxable(false);
				}
				aVebillLineitemsList.add(aVebilldetail);
			}
			/*Criteria aCriteria = aSession.createCriteria(Vebilldetail.class);
			aCriteria.add(Restrictions.eq("veBillId", veBillId));
			aVebillLineitemsList = aCriteria.list();*/
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVebillLineitemsList;
	}
	
	@Override
	public Vebill addVendorInvoiceDetails(Vebill theVebill, JoReleaseDetail theJoReleaseDetail,String isUpdate) throws VendorException {
		Session aVeBillSession = itsSessionFactory.openSession();
		//Session aJoReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Vebill aVebill = null;
		try {
			Integer joReleaseDetailID = 0;
			if(theJoReleaseDetail.getJoReleaseDetailId()!=null){
				joReleaseDetailID=theJoReleaseDetail.getJoReleaseDetailId();
			}else{
				joReleaseDetailID = addJoReleaseDetail(theJoReleaseDetail);
			}
			
			aTransaction = aVeBillSession.beginTransaction();
			aTransaction.begin();
			theVebill.setJoReleaseDetailId(joReleaseDetailID);
			Integer aVeBillID = (Integer) aVeBillSession.save(theVebill);
			aTransaction.commit();
			aVebill = (Vebill) aVeBillSession.get(Vebill.class, aVeBillID);
			//theJoReleaseDetail.setJoReleaseDetailId(theVebill.getJoReleaseDetailId());
			
			//status = insertLineItemsFromPODetailtoBillDetail(theVebill.getVePoid(), aVeBillID);
			//itsLogger.info("Status: "+status);
			if(isUpdate.equalsIgnoreCase("yes")){
				updateVepoStatus(theVebill.getVePoid(),2);
			}else{
				updateVepoStatus(theVebill.getVePoid(),1);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aVeBillSession.flush();
			aVeBillSession.close();
			//aJoReleaseSession.flush();
			//aJoReleaseSession.close();
		}
		return aVebill;
	}
	@Override
	public Integer addJoReleaseDetail(JoReleaseDetail theJoReleaseDetail){
		Session aJoReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		JoReleaseDetail aJoReleaseDetail = null;
		Integer joReleaseDetailID = null;
		try{
			aTransaction = aJoReleaseSession.beginTransaction();
			aTransaction.begin();
			aJoReleaseDetail = new JoReleaseDetail();
			//(JoReleaseDetail) aJoReleaseSession.get(JoReleaseDetail.class, theJoReleaseDetail.getJoReleaseDetailId());
			aJoReleaseDetail.setJoReleaseId(theJoReleaseDetail.getJoReleaseId());
			
			aJoReleaseDetail.setShipDate(theJoReleaseDetail.getShipDate());
			aJoReleaseDetail.setVendorInvoiceDate(theJoReleaseDetail.getVendorInvoiceDate());
			
			//aJoReleaseDetail.setVendorInvoiceAmount(aChangeAmount);
			aJoReleaseDetail.setVendorInvoiceAmount(theJoReleaseDetail.getVendorInvoiceAmount());
			//aJoReleaseSession.save(aJoReleaseDetail);
			joReleaseDetailID = (Integer) aJoReleaseSession.save(aJoReleaseDetail);
			//joReleaseDetailID = aJoReleaseDetail.getJoReleaseDetailId();
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aJoReleaseSession.flush();
			aJoReleaseSession.close();
		}
		return joReleaseDetailID;
	}
	@Override
	public void updateJoReleaseDetail(JoReleaseDetail theJoReleaseDetail){
		Transaction aTransaction;
		JoReleaseDetail aJoReleaseDetail = null;
		Session aSession = itsSessionFactory.openSession();
		try{
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoReleaseDetail = (JoReleaseDetail) aSession.get(JoReleaseDetail.class, theJoReleaseDetail.getJoReleaseDetailId());
			
			Query aQuery = aSession.createSQLQuery("SELECT SUM(billAmount) FROM veBill WHERE joReleaseDetailID="+aJoReleaseDetail.getJoReleaseDetailId());
			//SELECT SUM(billAmount) FROM veBill WHERE joReleaseDetailID=6
			List<?> aList = aQuery.list();
			BigDecimal aChangeAmount = (BigDecimal) aList.get(0);
			
			
			
			aJoReleaseDetail.setShipDate(theJoReleaseDetail.getShipDate());
			aJoReleaseDetail.setVendorInvoiceDate(theJoReleaseDetail.getVendorInvoiceDate());
			
			aJoReleaseDetail.setVendorInvoiceAmount(aChangeAmount);
			//aJoReleaseDetail.setVendorInvoiceAmount(theJoReleaseDetail.getVendorInvoiceAmount());
			aSession.update(aJoReleaseDetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	public List<Coaccount> getCoAccountDetails() throws JobException {
		Session aSession = null;
		List<Coaccount> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession
					.createQuery("FROM  Coaccount ORDER BY Description ASC");
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}
	
	/**
	 * Added by : Leo  Date: 02/19/2015
	 * Description : Parameter Added 
	 * Table :veBillDetail
	 * Bartos ID : 105
	 * */
	@Override
	public Vebill updateVendorInvoiceDetails(Vebill theVebill, JoReleaseDetail theJoReleaseDetail,Vebilldetail vebilldetail,String isUpdate) throws VendorException {
		Session aSession = itsSessionFactory.openSession();
		Vebill aVebill = null;
		Vebilldetail aveBillDetail=null;
		
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aVebill = (Vebill) aSession.get(Vebill.class, theVebill.getVeBillId());
			aVebill.setReceiveDate(theVebill.getReceiveDate());
			aVebill.setInvoiceNumber(theVebill.getInvoiceNumber());
			aVebill.setDueDate(theVebill.getDueDate());
			aVebill.setUsePostDate(theVebill.isUsePostDate());
			aVebill.setPostDate(theVebill.getPostDate());
			aVebill.setShipDate(theVebill.getShipDate());
			aVebill.setVeShipViaId(theVebill.getVeShipViaId());
			aVebill.setTrackingNumber(theVebill.getTrackingNumber());
			aVebill.setFreightAmount(theVebill.getFreightAmount());
			aVebill.setBillAmount(theVebill.getBillAmount());
			aVebill.setTaxAmount(theVebill.getTaxAmount());
			aVebill.setApaccountId(theVebill.getApaccountId());
			aVebill.setVeBillId(theVebill.getVeBillId());
			aVebill.setRxMasterId(theVebill.getRxMasterId());
			aVebill.setReason(theVebill.getReason());
			aVebill.setBillDate(theVebill.getBillDate());
			aVebill.setChangedById(theVebill.getChangedById());
			aVebill.setChangedOn(new Date());
			aSession.update(aVebill);
			
			/**
			 * Added by : Leo  Date: 02/19/2015
l.			 * Table :veBillDetail
			 * Bartos ID : 105
			 * */
			
			//aveBillDetail = (Vebilldetail) aSession.get(Vebilldetail.class, vebilldetail.getVeBillDetailId());
			//aveBillDetail.setQuantityBilled(vebilldetail.getQuantityBilled());
			//aSession.update(aveBillDetail);
			
			/**--------------------------------------*/
			
			aTransaction.commit();
			theJoReleaseDetail.setJoReleaseDetailId(aVebill.getJoReleaseDetailId());
			updateJoReleaseDetail(theJoReleaseDetail);
			
			if(isUpdate.equalsIgnoreCase("yes")){
				updateVepoStatus(theVebill.getVePoid(),2);
			}else{
				updateVepoStatus(theVebill.getVePoid(),1);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(),excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVebill;
	}
	
	@Override
	public int insertLineItemsFromPODetailtoBillDetail(Integer vePOId, Integer veBillId) throws VendorException {
		int aResult = 0;
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			itsLogger.info("Result after executing Stored procedure: before res");
			Query query = aSession.createSQLQuery("CALL veBilldetail_Insert("+vePOId+", "+veBillId+ ", @result)");
			aResult = query.executeUpdate();
			itsLogger.info("Result after executing Stored procedure: " + aResult);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aResult;
	}
	
	@Override
	public void saveVendorBillDetail(Vebilldetail aVeBillDetail) throws VendorException{
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.save(aVeBillDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public void updateVendorBillDetail(Vebilldetail theVebilldetail) throws VendorException{
		Session aSession = itsSessionFactory.openSession();
		Session acreateSession = itsSessionFactory.openSession();
		Vepodetail theVepodetail = new Vepodetail();
		BigDecimal oldQuantityBilled = new BigDecimal("0.0000");
		boolean tplog=false;
		Session tplogsession =null;
		try {
			Vepodetail aVepodetail = (Vepodetail) aSession.get(Vepodetail.class,
					theVebilldetail.getVePodetailId());
			Vebilldetail aVebilldetail = (Vebilldetail) aSession.get(Vebilldetail.class, theVebilldetail.getVeBillDetailId());
			oldQuantityBilled = aVebilldetail.getQuantityBilled();
			/*if(aVepodetail!=null){
				Prmaster thePrmaster =jobService.getPrMasterBasedOnId(aVepodetail.getPrMasterId());
				if(thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(aVepodetail,theVebilldetail,"edit");
				}
			}*/
			
			Transaction aTransaction = acreateSession.beginTransaction();
			aTransaction.begin();
			
			Vebilldetail updvebilldetail=new Vebilldetail();
			updvebilldetail.setVeBillDetailId(theVebilldetail.getVeBillDetailId());
			updvebilldetail.setVeBillId(theVebilldetail.getVeBillId());
			updvebilldetail.setVePodetailId(theVebilldetail.getVePodetailId());
			updvebilldetail.setPrMasterId(theVebilldetail.getPrMasterId());
			updvebilldetail.setDescription(theVebilldetail.getDescription());
			updvebilldetail.setNote(theVebilldetail.getNote());
			updvebilldetail.setQuantityBilled(theVebilldetail.getQuantityBilled());
			updvebilldetail.setQuantityOrdered(theVebilldetail.getQuantityOrdered());
			updvebilldetail.setPriceMultiplier(theVebilldetail.getPriceMultiplier());
			updvebilldetail.setUnitCost(theVebilldetail.getUnitCost());
			updvebilldetail.setTaxable(theVebilldetail.getTaxable());
			updvebilldetail.setFreightCost(theVebilldetail.getFreightCost());
			Vepo aVepo=null;
			Vebill aVebill=null;
			//Rollback
			if(theVebilldetail.getVePodetailId()>0){
				if(aVepodetail!=null){
					/*Eric Said No need to update inside job for onorder
					 * ID #471
					 * Velmurugan
					 * 11-12-2015
					 * */
					/*Prmaster thePrmaster =jobService.getPrMasterBasedOnId(aVepodetail.getPrMasterId());
					Vepodetail rbVepodetail = (Vepodetail) aSession.get(Vepodetail.class,theVebilldetail.getVePodetailId());
					Vebilldetail rbVebilldetail = (Vebilldetail) aSession.get(Vebilldetail.class,theVebilldetail.getVeBillDetailId());
					if(thePrmaster.getIsInventory()==1){
					System.out.println("theVebilldetail.getprmasterID==="+theVebilldetail.getPrMasterId());
					updatePrWarehouseInventoryOrdered(rbVepodetail,rbVebilldetail,"del");
					tplog=true;
					}*/
				}
				if(oldQuantityBilled.compareTo(theVebilldetail.getQuantityBilled()) !=0 && tplog){
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVebilldetail.getPrMasterId());	
				aVebill=(Vebill) aSession.get(Vebill.class, theVebilldetail.getVeBillId());
				aVepo=(Vepo) aSession.get(Vepo.class, aVebill.getVePoid());
				TpInventoryLog theTpInventoryLog = new TpInventoryLog();
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(aVebilldetail.getPrMasterId());
				aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
				aTpInventoryLog.setTransType("VI");
				aTpInventoryLog.setTransDecription("VI RollBack");
				aTpInventoryLog.setTransID(aVebilldetail.getVeBillId());
				aTpInventoryLog.setTransDetailID(aVebilldetail.getVeBillDetailId());
				aTpInventoryLog.setProductOut(oldQuantityBilled.compareTo(new BigDecimal("0.0000"))==-1?oldQuantityBilled.multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(oldQuantityBilled.compareTo(new BigDecimal("0.0000"))==1?oldQuantityBilled:new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(aVebill.getCreatedById());
				aTpInventoryLog.setCreatedOn(new Date());
				itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 * Rollback
				 * */
				tplogsession = itsSessionFactory.openSession();
				BigDecimal qb=oldQuantityBilled;
				Prwarehouse theprwarehouse=(Prwarehouse) tplogsession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
				Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
				TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
						aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
						BigDecimal.ZERO,qb,"VI",aVebilldetail.getVeBillId(),aVebilldetail.getVeBillDetailId(),aVebill.getInvoiceNumber(),aVepo.getPonumber() ,
		/*Product out*/(qb.compareTo(BigDecimal.ZERO)<0)?qb.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
		/*Product in*/(qb.compareTo(BigDecimal.ZERO)>0)?qb:BigDecimal.ZERO ,
						"VI Edited",aVebilldetail.getUserId(),aVebilldetail.getUserName(),
						new Date());
				itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
				
				/*Code Ends*/
				
				
				
				}
				
			}
			acreateSession.update(updvebilldetail);
			aTransaction.commit();
			if(theVebilldetail.getVePodetailId()!=null && theVebilldetail.getVePodetailId()>0){
				theVepodetail = new Vepodetail();
				theVepodetail.setVePodetailId(theVebilldetail.getVePodetailId());
				theVepodetail.setQuantityBilled(theVebilldetail.getQuantityBilled());
			updateBilledQuantity(theVepodetail);
			}
			//Insert
			if(theVebilldetail.getVePodetailId()>0){
				if(aVepodetail!=null){
					/*Eric Said No need to update inside job for onorder
					 * ID #471
					 * Velmurugan
					 * 11-12-2015
					 * */
					/*Prmaster thePrmaster =jobService.getPrMasterBasedOnId(aVepodetail.getPrMasterId());
					if(thePrmaster.getIsInventory()==1){
						Vepodetail inVepodetail = (Vepodetail) acreateSession.get(Vepodetail.class,theVebilldetail.getVePodetailId());
						Vebilldetail inrbVebilldetail = (Vebilldetail) acreateSession.get(Vebilldetail.class,theVebilldetail.getVeBillDetailId());
					System.out.println("theVebilldetail.getprmasterID==="+theVebilldetail.getPrMasterId());
					updatePrWarehouseInventoryOrdered(inVepodetail,inrbVebilldetail,"add");
					}*/
				}
				
				
				if(oldQuantityBilled.compareTo(theVebilldetail.getQuantityBilled()) !=0 && tplog){
					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVebilldetail.getPrMasterId());
					TpInventoryLog theTpInventoryLog = new TpInventoryLog();
					theTpInventoryLog.setPrMasterID(aVebilldetail.getPrMasterId());
					theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
					theTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
					theTpInventoryLog.setTransType("VI");
					theTpInventoryLog.setTransDecription("VI Edited");
					theTpInventoryLog.setTransID(aVebilldetail.getVeBillId());
					theTpInventoryLog.setTransDetailID(aVebilldetail.getVeBillDetailId());
					theTpInventoryLog.setProductOut(theVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?theVebilldetail.getQuantityBilled():new BigDecimal("0.0000"));
					theTpInventoryLog.setProductIn(theVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?theVebilldetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
					theTpInventoryLog.setUserID(aVebill.getCreatedById());
					theTpInventoryLog.setCreatedOn(new Date());
					itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * Insert
					 * */
					tplogsession = itsSessionFactory.openSession();
					BigDecimal qb=theVebilldetail.getQuantityBilled();
					Prwarehouse theprwarehouse=(Prwarehouse) tplogsession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							BigDecimal.ZERO,qb.multiply(new BigDecimal(-1)),"VI",aVebilldetail.getVeBillId(),aVebilldetail.getVeBillDetailId(),aVebill.getInvoiceNumber(),aVepo.getPonumber() ,
			/*Product out*/(qb.compareTo(BigDecimal.ZERO)>0)?qb:BigDecimal.ZERO,
			/*Product in*/(qb.compareTo(BigDecimal.ZERO)<0)?qb.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
							"VI Edited",aVebilldetail.getUserId(),aVebilldetail.getUserName(),
							new Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
				}
				
			}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			if(tplogsession!=null){
			tplogsession.flush();
			tplogsession.close();
			}
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public void deleteVendorBillDetail(Vebilldetail aVeBillDetail) throws VendorException{
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.delete(aVeBillDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public List<Vebillpay> getVeBillPayList(Integer moAccountID,Integer userID) throws VendorException{
		List<Vebillpay> aPaidBills = null;
		Session aSession = itsSessionFactory.openSession();
		try {
		//	aPaidBills = aSession.createCriteria(Vebillpay.class).list();
			
		    Criteria criteria = aSession.createCriteria(Vebillpay.class);
		    criteria.add(Restrictions.eq("moAccountId",moAccountID));
		    criteria.add(Restrictions.eq("userID",userID));
			aPaidBills = criteria.list();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPaidBills;
	}
	
	@Override
	public List<VendorBillsBean> getVeBillList(int theFrom, int theTo,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException{
		List<VendorBillsBean> aPaidBills = null;
		VendorBillsBean aVendorBillsBean = null;
		Session aSession = itsSessionFactory.openSession();
		/*String aVendorBillsListQry = "SELECT DISTINCT veBillID, BillDate, PONumber, InvoiceNumber, veBill.rxMasterID, "
				+ "CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID "
				+ "FROM  veBill "
				+ "LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
				+ "LEFT OUTER JOIN vePO ON veBill.vePOID = vePO.vePOID   WHERE veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL order by BillDate DESC "
				+ "LIMIT " + theFrom + ", " + theTo;*/

		String aVendorBillsListQry = "SELECT DISTINCT veBillID, BillDate, PONumber, InvoiceNumber, veBill.rxMasterID, "
			//	+ "CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID,veBill.DueDate,moTransaction.Reference,moTransaction.TransactionDate "
				+ "CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID,veBill.DueDate, "
				+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.checkNo AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS reference,"
				+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.datePaid AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS datePaid,"
				+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.amountVal AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS Amount,veBill.TransactionStatus,veBill.creditUsed"
				+ " FROM  veBill "
				+ "LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
				+ "LEFT OUTER JOIN vePO ON veBill.vePOID = vePO.vePOID  "
				//+ "LEFT OUTER JOIN moTransaction ON moTransactionID IN (SELECT DISTINCT moTransactionID FROM moLinkageDetail WHERE veBillID = veBill.veBillID Group by veBillID)"
				+ " WHERE (veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL) ";
		         
		if(searchData !=null && !searchData.equals("")){
			aVendorBillsListQry+= "And  (veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
					" OR veBill.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
					" OR AppliedAmount LIKE '%"+searchData+"%' OR veBill.vePOID LIKE '%"+searchData+"%' OR veBill.joReleaseDetailID LIKE '%"+searchData+"%')";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    startDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated Date:"+startDate);
		}
		if(endDate!=null && !endDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    endDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated End Date:"+endDate);
		}
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedfrom);
		
		cal.add(Calendar.MONTH, -6);
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedto);
		
		if(!startDate.equals("")&& !endDate.equals("")){
			
			aVendorBillsListQry+= " AND BillDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
		}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND BillDate >='"+startDate+" 00:00:00'";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND BillDate<='"+endDate+" 23:59:59'";
		}else{
			aVendorBillsListQry+= " AND BillDate BETWEEN '"+formattedto +" 00:00:00' AND '"+formattedfrom+" 23:59:59'";
		}
				
		String orderByIndex="";
		String orderBy="DESC";
		
		itsLogger.info("sortIndex::["+sortIndex+"]");
		     
		if(sortIndex.equals("billDate")){
			orderByIndex="BillDate";
		}else if(sortIndex.equals("ponumber")){
			orderByIndex="PONumber";
		}else if(sortIndex.equals("veInvoiceNumber")){
			orderByIndex="InvoiceNumber";
		}else if(sortIndex.equals("payableTo")){
			orderByIndex="PayableTo";
		}else if(sortIndex.equals("billAmount")){
			orderByIndex="BillAmount";
		}else if(sortIndex.equals("appliedAmount")){
			orderByIndex="AppliedAmount";
		}else if(sortIndex.equals("dueDate")){
			orderByIndex="veBill.DueDate";
		}else if(sortIndex.equals("chkNo")){
			orderByIndex="reference";
		}
		if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}
		aVendorBillsListQry+=" order by "+orderByIndex+"  "+orderBy+", veBillID desc LIMIT " + theFrom + ", " + theTo;
		
		try {
			aPaidBills = new ArrayList<VendorBillsBean>();
			Query aQuery = aSession.createSQLQuery(aVendorBillsListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillsBean = new VendorBillsBean();
				Object[] aObj = (Object[])aIterator.next();
				aVendorBillsBean.setVeBillId((Integer) aObj[0]);
				aVendorBillsBean.setBillDate((Date) aObj[1]);
				aVendorBillsBean.setPONumber((String) aObj[2]);
				aVendorBillsBean.setVeInvoiceNumber((String) aObj[3]);
				aVendorBillsBean.setRxMasterId((Integer) aObj[4]);
				aVendorBillsBean.setPayableTo((String) aObj[5]);
				aVendorBillsBean.setBillAmount((BigDecimal) aObj[6]);
				aVendorBillsBean.setAppliedAmount((BigDecimal) aObj[7]);
				aVendorBillsBean.setVePoid((Integer) aObj[8]);
				if(aObj[9]!=null){
					aVendorBillsBean.setJoreleasedetailid((Integer) aObj[9]);
				}else{
					aVendorBillsBean.setJoreleasedetailid(0);
				}
				aVendorBillsBean.setDueDate((Date) aObj[10]);
				
				if(aObj[11]!=null && aObj[12]!=null && aObj[13]!=null )
				
				if(aObj[11]!=null)
					aVendorBillsBean.setChkNo((String)aObj[11]);
				if(aObj[12]!=null)
					aVendorBillsBean.setDatePaid(aObj[12].toString());
				if(aObj[13]!=null)
					aVendorBillsBean.setAmt((String)aObj[13]);
				
			//	aVendorBillsBean.setDatePaid(new Date());
				aVendorBillsBean.setTransaction_status(aObj[14]!=null?JobUtil.ConvertintoInteger(aObj[14].toString()):new Integer(-1));
				aVendorBillsBean.setCreditUsed((String)aObj[15]);
				aPaidBills.add(aVendorBillsBean);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillsListQry = null;
		}
		return aPaidBills;
	}
	
	@Override
	public Integer getAccountsPayableCount(String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException{
		List<VendorBillsBean> aPaidBills = null;
		VendorBillsBean aVendorBillsBean = null;
		Session aSession = itsSessionFactory.openSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    startDate=sdff.format(convertedCurrentDate );
		}
		
		if(endDate!=null && !endDate.trim().equals("")){
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    endDate=sdff.format(convertedCurrentDate );
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedto);
		
		cal.add(Calendar.MONTH, -6);
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedfrom);
		
		String customDate = "";
		
		if(!startDate.equals("")&& !endDate.equals("")){
			customDate = endDate;		}
		else if(!startDate.equals("") && endDate.equals("")){
			customDate = startDate;
		}else if(!endDate.equals("") && startDate.equals("")){
			customDate = endDate;
		}else{
			customDate = formattedto;
		}

	/*	String aVendorBillsListQry = "SELECT vb.veBillID,vb.BillDate,vp.PONumber,vb.InvoiceNumber,vb.rxMasterID,CONCAT(rx.Name, ' ', rx.FirstName) AS PayableTo,"
				+ " vb.BillAmount,vb.AppliedAmount,vb.vePOID,vb.joReleaseDetailID,vb.DueDate,vh.checkNo,vh.datePaid,vh.amountVal,"
				+ " vb.TransactionStatus,vb.creditUsed,(DATEDIFF('"+customDate+"',vb.BillDate)) AS age ,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))<=30 THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS currentAmount,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>30 AND DATEDIFF('"+customDate+"',vb.BillDate)<=60) THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age30Amount ,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>60 AND DATEDIFF('"+customDate+"',vb.BillDate)<=90) THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age60Amount ,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))>90 THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age90Amount ,"
				+ " IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)AS balance"
				+ " FROM veBill vb LEFT JOIN veBillPaymentHistory vh ON vb.veBillID = vh.vebillID LEFT OUTER JOIN rxMaster rx ON vb.rxMasterID = rx.rxMasterID LEFT OUTER JOIN vePO vp ON vb.vePOID = vp.vePOID"
				+ " WHERE (vb.vePOID IS NULL OR vb.vePOID IS NOT NULL)";*/
		
		String aVendorBillsListQry = "SELECT vb.veBillID,vb.BillDate,vp.PONumber,vb.InvoiceNumber,vb.rxMasterID,CONCAT(rx.Name, ' ', rx.FirstName) AS PayableTo,"
		+ " vb.BillAmount,vb.AppliedAmount,vb.vePOID,vb.joReleaseDetailID,vb.DueDate,mo.Reference,mo.TransactionDate,(mLD.Amount+mLD.Discount) AS amountVal,"
		+ " vb.TransactionStatus,vb.creditUsed,(DATEDIFF('"+customDate+"',vb.BillDate)) AS age ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))<=30 THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS currentAmount,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>30 AND DATEDIFF('"+customDate+"',vb.BillDate)<=60) THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age30Amount ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>60 AND DATEDIFF('"+customDate+"',vb.BillDate)<=90) THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age60Amount ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))>90 THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age90Amount ,"
		+ " IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)AS balance"
		+ " FROM veBill vb LEFT JOIN moLinkageDetail mLD ON vb.veBillID = mLD.veBillID LEFT JOIN moTransaction mo ON mLD.moTransactionID = mo.moTransactionID AND mo.Void <>1"
		+ " LEFT OUTER JOIN rxMaster rx ON vb.rxMasterID = rx.rxMasterID LEFT OUTER JOIN vePO vp ON vb.vePOID = vp.vePOID"
		+ " WHERE (vb.vePOID IS NULL OR vb.vePOID IS NOT NULL) and vb.TransactionStatus >0 or vb.TransactionStatus=-2";
		         
		if(searchData !=null && !searchData.equals("")){
			aVendorBillsListQry+= "And  (vb.veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
					" OR vb.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rx.Name, ' ', rx.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
					" OR AppliedAmount LIKE '%"+searchData+"%' OR vb.vePOID LIKE '%"+searchData+"%' OR vb.joReleaseDetailID LIKE '%"+searchData+"%')";
		}
	
		/*if(!startDate.equals("")&& !endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >= '"+startDate +"' AND Date(BillDate) <= '"+endDate+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";	}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >='"+startDate+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) <='"+endDate+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";
		}else{
			aVendorBillsListQry+= " AND Date(BillDate) <= '"+formattedto+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";
		}*/
		
		if(!startDate.equals("")&& !endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >= '"+startDate +"' AND Date(BillDate) <= '"+endDate+"' GROUP BY vb.veBillID ";	}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >='"+startDate+"' GROUP BY vb.veBillID ";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) <='"+endDate+"' GROUP BY vb.veBillID ";
		}else{
			aVendorBillsListQry+= " AND Date(BillDate) <= '"+formattedto+"' GROUP BY vb.veBillID ";
		}
				
		String orderByIndex="";
		String orderBy="DESC";
		
		itsLogger.info("sortIndex::["+sortIndex+"]");
		     
		if(sortIndex.equals("billDate")){
			orderByIndex="BillDate";
		}else if(sortIndex.equals("ponumber")){
			orderByIndex="PONumber";
		}else if(sortIndex.equals("veInvoiceNumber")){
			orderByIndex="InvoiceNumber";
		}else if(sortIndex.equals("payableTo")){
			orderByIndex="PayableTo";
		}else if(sortIndex.equals("billAmount")){
			orderByIndex="BillAmount";
		}else if(sortIndex.equals("appliedAmount")){
			orderByIndex="AppliedAmount";
		}else if(sortIndex.equals("dueDate")){
			orderByIndex="vb.DueDate";
		}else if(sortIndex.equals("chkNo")){
			orderByIndex="reference";
		}else if(sortIndex.equals("age")){
			orderByIndex="age";
		}else if(sortIndex.equals("currentAmount")){
			orderByIndex="currentAmount";
		}else if(sortIndex.equals("age30Amount")){
			orderByIndex="age30Amount";
		}else if(sortIndex.equals("age60Amount")){
			orderByIndex="age60Amount";
		}else if(sortIndex.equals("age90Amount")){
			orderByIndex="age90Amount";
		}
		
		if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}
		aVendorBillsListQry+=" order by "+orderByIndex+"  "+orderBy;
		//+" LIMIT " + theFrom + ", " + theTo;
		String overallQuery="SELECT accountpayable.*,IF(ABS(balance)>0.01,TRUE,FALSE) AS checkstatus from ("+aVendorBillsListQry+") AS accountpayable HAVING checkstatus=1 ";
		itsLogger.info("Accountpayablequery::::"+overallQuery);
		
		try {
			aPaidBills = new ArrayList<VendorBillsBean>();
			Query aQuery = aSession.createSQLQuery(overallQuery);
			aPaidBills = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillsListQry = null;
		}
		return aPaidBills.size();
	}
		
	
	@Override
	public List<VendorBillsBean> getAccountsPayableList(int theFrom, int theTo,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException{
		List<VendorBillsBean> aPaidBills = null;
		VendorBillsBean aVendorBillsBean = null;
		Session aSession = itsSessionFactory.openSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    startDate=sdff.format(convertedCurrentDate );
		}
		
		if(endDate!=null && !endDate.trim().equals("")){
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    endDate=sdff.format(convertedCurrentDate );
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedto);
		
		cal.add(Calendar.MONTH, -6);
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedfrom);
		
		String customDate = "";
		
		if(!startDate.equals("")&& !endDate.equals("")){
			customDate = endDate;		}
		else if(!startDate.equals("") && endDate.equals("")){
			customDate = startDate;
		}else if(!endDate.equals("") && startDate.equals("")){
			customDate = endDate;
		}else{
			customDate = formattedto;
		}

	/*	String aVendorBillsListQry = "SELECT vb.veBillID,vb.BillDate,vp.PONumber,vb.InvoiceNumber,vb.rxMasterID,CONCAT(rx.Name, ' ', rx.FirstName) AS PayableTo,"
				+ " vb.BillAmount,vb.AppliedAmount,vb.vePOID,vb.joReleaseDetailID,vb.DueDate,vh.checkNo,vh.datePaid,vh.amountVal,"
				+ " vb.TransactionStatus,vb.creditUsed,(DATEDIFF('"+customDate+"',vb.BillDate)) AS age ,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))<=30 THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS currentAmount,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>30 AND DATEDIFF('"+customDate+"',vb.BillDate)<=60) THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age30Amount ,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>60 AND DATEDIFF('"+customDate+"',vb.BillDate)<=90) THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age60Amount ,"
				+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))>90 THEN (IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age90Amount ,"
				+ " IF(DATE(vh.datePaid)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(vh.amountVal)),vb.BillAmount-vb.AppliedAmount)AS balance"
				+ " FROM veBill vb LEFT JOIN veBillPaymentHistory vh ON vb.veBillID = vh.vebillID LEFT OUTER JOIN rxMaster rx ON vb.rxMasterID = rx.rxMasterID LEFT OUTER JOIN vePO vp ON vb.vePOID = vp.vePOID"
				+ " WHERE (vb.vePOID IS NULL OR vb.vePOID IS NOT NULL)";*/
		
		String aVendorBillsListQry = "SELECT vb.veBillID,vb.BillDate,vp.PONumber,vb.InvoiceNumber,vb.rxMasterID,CONCAT(rx.Name, ' ', rx.FirstName) AS PayableTo,"
		+ " vb.BillAmount,vb.AppliedAmount,vb.vePOID,vb.joReleaseDetailID,vb.DueDate,mo.Reference,mo.TransactionDate,(mLD.Amount+mLD.Discount) AS amountVal,"
		+ " vb.TransactionStatus,vb.creditUsed,(DATEDIFF('"+customDate+"',vb.BillDate)) AS age ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))<=30 THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS currentAmount,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>30 AND DATEDIFF('"+customDate+"',vb.BillDate)<=60) THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age30Amount ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>60 AND DATEDIFF('"+customDate+"',vb.BillDate)<=90) THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age60Amount ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))>90 THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age90Amount ,"
		+ " IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)AS balance"
		+ " FROM veBill vb LEFT JOIN moLinkageDetail mLD ON vb.veBillID = mLD.veBillID LEFT JOIN moTransaction mo ON mLD.moTransactionID = mo.moTransactionID AND mo.Void <>1"
		+ " LEFT OUTER JOIN rxMaster rx ON vb.rxMasterID = rx.rxMasterID LEFT OUTER JOIN vePO vp ON vb.vePOID = vp.vePOID"
		+ " WHERE (vb.vePOID IS NULL OR vb.vePOID IS NOT NULL) and vb.TransactionStatus >0 or vb.TransactionStatus=-2";
		         
		if(searchData !=null && !searchData.equals("")){
			aVendorBillsListQry+= "And  (vb.veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
					" OR vb.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rx.Name, ' ', rx.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
					" OR AppliedAmount LIKE '%"+searchData+"%' OR vb.vePOID LIKE '%"+searchData+"%' OR vb.joReleaseDetailID LIKE '%"+searchData+"%')";
		}
	
		/*if(!startDate.equals("")&& !endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >= '"+startDate +"' AND Date(BillDate) <= '"+endDate+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";	}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >='"+startDate+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) <='"+endDate+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";
		}else{
			aVendorBillsListQry+= " AND Date(BillDate) <= '"+formattedto+"' GROUP BY vb.veBillID HAVING (balance >0.01 OR balance < -0.01)";
		}*/
		
		if(!startDate.equals("")&& !endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >= '"+startDate +"' AND Date(BillDate) <= '"+endDate+"' GROUP BY vb.veBillID ";	}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >='"+startDate+"' GROUP BY vb.veBillID ";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) <='"+endDate+"' GROUP BY vb.veBillID ";
		}else{
			aVendorBillsListQry+= " AND Date(BillDate) <= '"+formattedto+"' GROUP BY vb.veBillID ";
		}
				
		String orderByIndex="";
		String orderBy="DESC";
		
		itsLogger.info("sortIndex::["+sortIndex+"]");
		     
		if(sortIndex.equals("billDate")){
			orderByIndex="BillDate";
		}else if(sortIndex.equals("ponumber")){
			orderByIndex="PONumber";
		}else if(sortIndex.equals("veInvoiceNumber")){
			orderByIndex="InvoiceNumber";
		}else if(sortIndex.equals("payableTo")){
			orderByIndex="PayableTo";
		}else if(sortIndex.equals("billAmount")){
			orderByIndex="BillAmount";
		}else if(sortIndex.equals("appliedAmount")){
			orderByIndex="AppliedAmount";
		}else if(sortIndex.equals("dueDate")){
			orderByIndex="vb.DueDate";
		}else if(sortIndex.equals("chkNo")){
			orderByIndex="reference";
		}else if(sortIndex.equals("age")){
			orderByIndex="age";
		}else if(sortIndex.equals("currentAmount")){
			orderByIndex="currentAmount";
		}else if(sortIndex.equals("age30Amount")){
			orderByIndex="age30Amount";
		}else if(sortIndex.equals("age60Amount")){
			orderByIndex="age60Amount";
		}else if(sortIndex.equals("age90Amount")){
			orderByIndex="age90Amount";
		}
		
		if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}
		aVendorBillsListQry+=" order by "+orderByIndex+"  "+orderBy;
		//+" LIMIT " + theFrom + ", " + theTo;
		String overallQuery="SELECT accountpayable.*,IF(ABS(balance)>0.01,TRUE,FALSE) AS checkstatus from ("+aVendorBillsListQry+") AS accountpayable HAVING checkstatus=1 LIMIT " + theFrom + ", " + theTo;
		itsLogger.info("Accountpayablequery::::"+overallQuery);
		
		try {
			aPaidBills = new ArrayList<VendorBillsBean>();
			Query aQuery = aSession.createSQLQuery(overallQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillsBean = new VendorBillsBean();
				Object[] aObj = (Object[])aIterator.next();
				aVendorBillsBean.setVeBillId((Integer) aObj[0]);
				aVendorBillsBean.setBillDate((Date) aObj[1]);
				aVendorBillsBean.setPONumber((String) aObj[2]);
				aVendorBillsBean.setVeInvoiceNumber((String) aObj[3]);
				aVendorBillsBean.setRxMasterId((Integer) aObj[4]);
				aVendorBillsBean.setPayableTo((String) aObj[5]);
				//aVendorBillsBean.setBillAmount((BigDecimal) aObj[6]); 
				aVendorBillsBean.setAppliedAmount((BigDecimal) aObj[7]);
				aVendorBillsBean.setVePoid((Integer) aObj[8]);
				if(aObj[9]!=null){
					aVendorBillsBean.setJoreleasedetailid((Integer) aObj[9]);
				}else{
					aVendorBillsBean.setJoreleasedetailid(0);
				}
				aVendorBillsBean.setDueDate((Date) aObj[10]);
				
				if(aObj[11]!=null && aObj[12]!=null && aObj[13]!=null )
				
				if(aObj[11]!=null)
					aVendorBillsBean.setChkNo((String)aObj[11]);
				if(aObj[12]!=null)
					aVendorBillsBean.setDatePaid(aObj[12].toString());
				if(aObj[13]!=null)
				{
					if(aObj[13] instanceof BigDecimal)
						aVendorBillsBean.setAmt(aObj[13].toString());
					else
						aVendorBillsBean.setAmt((String)aObj[13]);
				}
				
			//	aVendorBillsBean.setDatePaid(new Date());
				aVendorBillsBean.setTransaction_status(aObj[14]!=null?JobUtil.ConvertintoInteger(aObj[14].toString()):new Integer(-1));
				aVendorBillsBean.setCreditUsed((String)aObj[15]);
				
				if(aObj[16]!=null)
				{
					if(aObj[16] instanceof Integer)
						aVendorBillsBean.setAge((Integer)aObj[16]);
					else
						aVendorBillsBean.setAge(((BigInteger)aObj[16]).intValue());
				}
				
				//aVendorBillsBean.setAge((Integer)aObj[16]);
				aVendorBillsBean.setCurrentAmount((BigDecimal) aObj[17]);
				aVendorBillsBean.setAge30Amount((BigDecimal) aObj[18]);
				aVendorBillsBean.setAge60Amount((BigDecimal) aObj[19]);
				aVendorBillsBean.setAge90Amount((BigDecimal) aObj[20]);
				aVendorBillsBean.setBillAmount((BigDecimal) aObj[21]);
				
				aPaidBills.add(aVendorBillsBean);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillsListQry = null;
		}
		return aPaidBills;
	}
	
	@Override
	public List<VendorBillsBean> getUninvoiceList(int theFrom, int theTo,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException{
		List<VendorBillsBean> aPaidBills = null;
		VendorBillsBean aVendorBillsBean = null;
		Session aSession = itsSessionFactory.openSession();
		
		
		String aVendorBillsListQry = "SELECT DISTINCT veBillID, BillDate, PONumber, InvoiceNumber, veBill.rxMasterID," 
				+ " CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID,veBill.DueDate,"
				+ " (SELECT DISTINCT GROUP_CONCAT(CAST(vph.checkNo AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS reference,"
		+ " (SELECT DISTINCT GROUP_CONCAT(CAST(vph.datePaid AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS datePaid,"
		+ " (SELECT DISTINCT GROUP_CONCAT(CAST(vph.amountVal AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS Amount,"
		+ " veBill.TransactionStatus,veBill.creditUsed,veBill.ReceiveDate as ReceiveDate,joMaster.JobNumber as JobNumber,joMaster.Description as JobName"
		+ "   FROM  veBill LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
		+ " LEFT OUTER JOIN joReleaseDetail ON joReleaseDetail.joReleaseDetailID=veBill.joReleaseDetailID " 
		+ " LEFT OUTER JOIN joRelease ON joRelease.joReleaseID =joReleaseDetail.joReleaseID "
		+ " LEFT OUTER JOIN joMaster ON joMaster.joMasterID =joRelease.joMasterID "
		+ " LEFT OUTER JOIN vePO ON vePO.vePOID=veBill.vePOID "
		+ " LEFT OUTER JOIN moTransaction ON moTransactionID = (SELECT DISTINCT moTransactionID FROM moLinkageDetail WHERE veBillID = veBill.veBillID " 
		//+ " GROUP BY veBillID) WHERE (veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL) AND veBill.joReleaseDetailID IS NOT NULL AND "
		+ " GROUP BY veBillID) WHERE veBill.joReleaseDetailID IS NOT NULL AND "
		+ " joReleaseDetail.CustomerInvoiceDate IS NULL AND joReleaseDetail.CustomerInvoiceAmount IS NULL ";
		         
		if(searchData !=null && !searchData.equals("")){
			aVendorBillsListQry+= "And  (veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
					" OR veBill.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
					" OR AppliedAmount LIKE '%"+searchData+"%' OR veBill.vePOID LIKE '%"+searchData+"%' OR veBill.joReleaseDetailID LIKE '%"+searchData+"%')";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    startDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated Date:"+startDate);
		}
		if(endDate!=null && !endDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    endDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated End Date:"+endDate);
		}
		
		if(!startDate.equals("")&& !endDate.equals("")){
			
			aVendorBillsListQry+= " AND veBill.ReceiveDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
		}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND veBill.ReceiveDate >='"+startDate+" 00:00:00'";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND veBill.ReceiveDate<='"+endDate+" 23:59:59'";
		}
				
		String orderByIndex="";
		String orderBy="DESC";
		
		itsLogger.info("sortIndex::["+sortIndex+"]");
		     
		if(sortIndex.equals("billDate")){
			orderByIndex="BillDate";
		}else if(sortIndex.equals("ponumber")){
			orderByIndex="PONumber";
		}else if(sortIndex.equals("veInvoiceNumber")){
			orderByIndex="InvoiceNumber";
		}else if(sortIndex.equals("payableTo")){
			orderByIndex="PayableTo";
		}else if(sortIndex.equals("billAmount")){
			orderByIndex="BillAmount";
		}else if(sortIndex.equals("appliedAmount")){
			orderByIndex="AppliedAmount";
		}else if(sortIndex.equals("dueDate")){
			orderByIndex="veBill.DueDate";
		}else if(sortIndex.equals("chkNo")){
			orderByIndex="reference";
		}else if(sortIndex.equals("receiveDate")){
			orderByIndex="ReceiveDate";
		}else if(sortIndex.equals("jobNumber")){
			orderByIndex="JobNumber";
		}else if(sortIndex.equals("jobName")){
			orderByIndex="JobName";
		}
		if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}
		aVendorBillsListQry+=" order by "+orderByIndex+"  "+orderBy+" LIMIT " + theFrom + ", " + theTo;
		
		try {
			aPaidBills = new ArrayList<VendorBillsBean>();
			Query aQuery = aSession.createSQLQuery(aVendorBillsListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVendorBillsBean = new VendorBillsBean();
				Object[] aObj = (Object[])aIterator.next();
				aVendorBillsBean.setVeBillId((Integer) aObj[0]);
				aVendorBillsBean.setBillDate((Date) aObj[1]);
				aVendorBillsBean.setPONumber((String) aObj[2]);
				aVendorBillsBean.setVeInvoiceNumber((String) aObj[3]);
				aVendorBillsBean.setRxMasterId((Integer) aObj[4]);
				aVendorBillsBean.setPayableTo((String) aObj[5]);
				aVendorBillsBean.setBillAmount((BigDecimal) aObj[6]);
				aVendorBillsBean.setAppliedAmount((BigDecimal) aObj[7]);
				aVendorBillsBean.setVePoid((Integer) aObj[8]);
				if(aObj[9]!=null){
					aVendorBillsBean.setJoreleasedetailid((Integer) aObj[9]);
				}else{
					aVendorBillsBean.setJoreleasedetailid(0);
				}
				aVendorBillsBean.setDueDate((Date) aObj[10]);
				
				if(aObj[11]!=null && aObj[12]!=null && aObj[13]!=null )
				
				if(aObj[11]!=null)
					aVendorBillsBean.setChkNo((String)aObj[11]);
				if(aObj[12]!=null)
					aVendorBillsBean.setDatePaid(aObj[12].toString());
				if(aObj[13]!=null)
					aVendorBillsBean.setAmt((String)aObj[13]);
			//	aVendorBillsBean.setDatePaid(new Date());
				aVendorBillsBean.setTransaction_status(aObj[14]!=null?JobUtil.ConvertintoInteger(aObj[14].toString()):new Integer(-1));
				aVendorBillsBean.setCreditUsed((String)aObj[15]);
				if(aObj[16]!=null)
					aVendorBillsBean.setReceiveDate((Date)aObj[16]);
				if(aObj[17]!=null)
					aVendorBillsBean.setJobNumber((String)aObj[17]);
				if(aObj[18]!=null)
					aVendorBillsBean.setJobName((String)aObj[18]);
				aPaidBills.add(aVendorBillsBean);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillsListQry = null;
		}
		return aPaidBills;
	}
	
	@Override
	public Vebill getVeBill(Integer vePoID) throws VendorException {
		Session aSession = null;
		Vebill aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	Query query = aSession.createQuery("FROM  Vebill WHERE vePOID = "+vePoID);
			// Retrieve all
		 	if(!query.list().isEmpty()){
		 		aQueryList = (Vebill)query.list().get(0);
		 	}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		}finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList; 
		
	}
	
	@Override
	public BigInteger getBillsCount() throws VendorException {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedfrom);
		
		cal.add(Calendar.MONTH, -6);
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedto);
		String aVendorSelectQry = "SELECT COUNT(*) FROM  veBill "
									+ "LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
									+ "LEFT OUTER JOIN vePO ON veBill.vePOID = vePO.vePOID WHERE BillDate BETWEEN '"+formattedto +" 00:00:00' AND '"+formattedfrom+" 23:59:59'";
		Session aSession = itsSessionFactory.openSession();
		BigInteger count = null;
		try{
			List<?> aList = aSession.createSQLQuery(aVendorSelectQry).list();
			count = (BigInteger) aList.get(0);
		} catch (Exception e){
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorSelectQry = null;
		}
		return count;
	}
	
	@Override
	public CoTaxTerritory getTaxRateTerritory(Integer theCoTaxTerritory) throws VendorException {
		Session aSession = null;
		CoTaxTerritory aQueryList = null;
		Query query = null;
		List aList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	query = aSession.createQuery("FROM  CoTaxTerritory WHERE coTaxTerritoryId = "+theCoTaxTerritory);
			// Retrieve all
		 	aList = query.list();
		 	if(!aList.isEmpty()){
		 		aQueryList = (CoTaxTerritory)aList.get(0);
		 	}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		}finally {
			aSession.flush();
			aSession.close();
			query = null;
			aList = null;
		}
		return  aQueryList; 
		
	}
	
	@Override
	public Cuinvoice getCuInvoiceObj(Integer thecuInvoiceID) throws VendorException {
		itsLogger.debug("getCuInvoiceObj Method");
		Cuinvoice aCuInvoice = new Cuinvoice();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aCuInvoice = (Cuinvoice) aSession.get(Cuinvoice.class, thecuInvoiceID);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuInvoice;
	}
	@Override
	public Rxcontact getContactIdFromMasterID(String theMasterId) throws VendorException {
		Session aSession = null;
		Rxcontact aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createQuery("FROM  Rxcontact WHERE rxMasterId = "+theMasterId);
			List aList = query.list();
			if(!aList.isEmpty()){
		 		aQueryList = (Rxcontact)aList.get(0);
		 	}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}
	
	@Override
	public String checkInvoiceNumber(String theSONumber) throws VendorException {
		itsLogger.debug("getCuInvoiceObj Method");
		String aQuery = "SELECT InvoiceNumber FROM cuInvoice WHERE InvoiceNumber LIKE '"+ theSONumber + "%' ORDER BY cuInvoiceID DESC LIMIT 1";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aQuery);
			List aList = query.list();
			if(!aList.isEmpty()){
				int size = aList.size();
		 		return (String)aList.get(size-1);
		 	}
			else
				return "";
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
	}
	@Override
	public Integer vepoDetailReceiveInventory(Vepodetail vepodetail,BigDecimal inventoryUpdate,Integer veReceiveID,Date ReceivedDate) throws VendorException {
		Session aSession = itsSessionFactory.openSession();
		Vepodetail aFactory = null;
		Vepo aVepo = new Vepo();
		int receiveID = 0;
		try {
			Vepodetail aVeFactory = (Vepodetail) aSession.get(Vepodetail.class, vepodetail.getVePodetailId());
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			aVeFactory.setVePodetailId(vepodetail.getVePodetailId());
			aVeFactory.setPrMasterId(vepodetail.getPrMasterId());
			if(vepodetail.getQuantityReceived()!=null){
				aVeFactory.setQuantityReceived(vepodetail.getQuantityReceived());	
			}
			
			Prmaster thePrmaster =jobService.getPrMasterBasedOnId(aVeFactory.getPrMasterId());
			if(thePrmaster.getIsInventory()==1){
				updateInventoryOrderedForInventoryWarehouse(aVeFactory,inventoryUpdate);
			}
			aSession.update(aVeFactory);
			transaction.commit();
			
			aFactory = (Vepodetail) aSession.get(Vepodetail.class, vepodetail.getVePodetailId());
			List<Vepodetail> aQueryList = null;
			Query querys = aSession.createQuery("FROM  Vepodetail WHERE VepoID="+aFactory.getVePoid());
			aQueryList = querys.list();
			Integer dateNoNeed = 0;
			if(aQueryList.size()>0){
			for(int j=0;j<aQueryList.size();j++){
				if((aQueryList.get(j).getQuantityOrdered().subtract(aQueryList.get(j).getQuantityReceived())).compareTo(aQueryList.get(j).getQuantityOrdered())==0){
					dateNoNeed++;
				}
			}
			if(aFactory.getVePoid()>0){
				aVepo = (Vepo) aSession.get(Vepo.class, aFactory.getVePoid());
				if(dateNoNeed==aQueryList.size()){
					aVepo.setreceiveddate(aVepo.getreceiveddate());
				}else{
					aVepo.setreceiveddate(ReceivedDate);
				}
			//	receiveID = vepoReceiveInventoryDate(vepodetail,aVepo,veReceiveID);
			}
		}
			
//			aFactory.setQuantityReceived(inventoryUpdate);
			//updateInventoryOrdered(aFactory,inventoryUpdate);
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return receiveID;
	}
	public String updateInventoryOrdered(Vepodetail objVepodetail,BigDecimal updateDifference)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Prmaster objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
					objVepodetail.getPrMasterId());
			System.out.println("objPrmaster"+objPrmaster);
			
			if(objPrmaster.getInventoryOnOrder()!=null){
				BigDecimal order = objPrmaster.getInventoryOnOrder();
				objPrmaster.setInventoryOnOrder(order.subtract(updateDifference));
			}
			if(objPrmaster.getInventoryOnHand()!=null){
				BigDecimal onHand= objPrmaster.getInventoryOnHand();
				
				objPrmaster.setInventoryOnHand(onHand.add(updateDifference));
			}
			aVePOSession.update(objPrmaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}
	
	public String updateInventoryOrderedForInventoryWarehouse(Vepodetail objVepodetail,BigDecimal updateDifference)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Prmaster aPrmaster = null;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Vepodetail aVepodetail = (Vepodetail) aVePOSession.get(Vepodetail.class,
					objVepodetail.getVePodetailId());
			itsLogger.info("Vepoid-->"+aVepodetail.getVePoid());
			
			itsLogger.info("objVepodetail.getVePodetailId() ::"+objVepodetail.getVePodetailId());
			Vepo objVepo = (Vepo) aVePOSession.get(Vepo.class, aVepodetail.getVePoid());
			
			Criteria criteria = aVePOSession.createCriteria(Prwarehouseinventory.class)
					.add(Restrictions.eq("prMasterId", aVepodetail.getPrMasterId()))
					.add(Restrictions.eq("prWarehouseId", objVepo.getPrWarehouseId()));
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory)criteria.uniqueResult();
			
			
			Vepodetail getVepodetail = (Vepodetail) aVePOSession.get(Vepodetail.class,objVepodetail.getVePodetailId());
			BigDecimal quantityOrder = BigDecimal.ZERO;
			BigDecimal oldquantityReceived = BigDecimal.ZERO;
			BigDecimal newquantityReceived =  BigDecimal.ZERO;
			BigDecimal inventoryonhand =  BigDecimal.ZERO;
			BigDecimal order = BigDecimal.ZERO;
			quantityOrder=getVepodetail.getQuantityOrdered()==null?quantityOrder:getVepodetail.getQuantityOrdered();
			oldquantityReceived=getVepodetail.getQuantityReceived()==null?oldquantityReceived:getVepodetail.getQuantityReceived();
			newquantityReceived=objVepodetail.getQuantityReceived()==null?newquantityReceived:objVepodetail.getQuantityReceived();
			order = objPrwarehouseinventory.getInventoryOnOrder()==null?order:objPrwarehouseinventory.getInventoryOnOrder();
			order=order.add(oldquantityReceived).subtract(newquantityReceived);
			objPrwarehouseinventory.setInventoryOnOrder(order);
			inventoryonhand=objPrwarehouseinventory.getInventoryOnHand().subtract(oldquantityReceived).add(newquantityReceived);
			objPrwarehouseinventory.setInventoryOnHand(inventoryonhand);
			aVePOSession.update(objPrwarehouseinventory);
			
			List<Prwarehouseinventory> aQueryList = null;
			Query querys = aVePOSession
					.createQuery("FROM  Prwarehouseinventory WHERE prMasterID="+objVepodetail.getPrMasterId());
				aQueryList = querys.list();
		BigDecimal quantityOnHand = new BigDecimal("0.0000");
		BigDecimal quantityOnOrder = new BigDecimal("0.0000");
		if(aQueryList.size()>0){
			for(int j=0;j<aQueryList.size();j++){
				quantityOnHand = quantityOnHand.add(aQueryList.get(j).getInventoryOnHand()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnHand());
				quantityOnOrder = quantityOnOrder.add(aQueryList.get(j).getInventoryOnOrder()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnOrder());
			}
			 aPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
					 objVepodetail.getPrMasterId());
			aPrmaster.setInventoryOnHand(quantityOnHand);
			aPrmaster.setInventoryOnOrder(quantityOnOrder);
			aVePOSession.update(aPrmaster);
		}
			
			
			aTransaction.commit();			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}
	
	/*public String updateInventoryOrderedForInventoryWarehouse(Vepodetail objVepodetail,BigDecimal updateDifference)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Prmaster aPrmaster = null;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Vepodetail aVepodetail = (Vepodetail) aVePOSession.get(Vepodetail.class,
					objVepodetail.getVePodetailId());
			itsLogger.info("Vepoid-->"+aVepodetail.getVePoid());
			
			itsLogger.info("objVepodetail.getVePodetailId() ::"+objVepodetail.getVePodetailId());
			Vepo objVepo = (Vepo) aVePOSession.get(Vepo.class, aVepodetail.getVePoid());
			
			Criteria criteria = aVePOSession.createCriteria(Prwarehouseinventory.class)
					.add(Restrictions.eq("prMasterId", aVepodetail.getPrMasterId()))
					.add(Restrictions.eq("prWarehouseId", objVepo.getPrWarehouseId()));
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory)criteria.uniqueResult();
			
			List<Vepodetail> bQueryList = null;
			Query aQuerys = aVePOSession
					.createQuery("FROM  Vepodetail WHERE prMasterID="+objVepodetail.getPrMasterId());
			bQueryList = aQuerys.list();
		BigDecimal quantityOrder = new BigDecimal("0.0000");
		BigDecimal quantityReceived = new BigDecimal("0.0000");
		if(bQueryList.size()>0){
			for(int j=0;j<bQueryList.size();j++){
				itsLogger.info("Quantity Ordered::"+bQueryList.get(j).getQuantityOrdered());
				
				if(bQueryList.get(j).getQuantityOrdered() != null && bQueryList.get(j).getQuantityReceived()!=null){
					itsLogger.info("Compare::"+bQueryList.get(j).getQuantityOrdered().compareTo(bQueryList.get(j).getQuantityReceived()));
					if(bQueryList.get(j).getQuantityOrdered().compareTo(bQueryList.get(j).getQuantityReceived())!=0){
						quantityOrder = quantityOrder.add(bQueryList.get(j).getQuantityOrdered()==null?new BigDecimal("0.0000"):bQueryList.get(j).getQuantityOrdered());
				}else{
					quantityOrder = quantityOrder.add(bQueryList.get(j).getQuantityOrdered()==null?new BigDecimal("0.0000"):bQueryList.get(j).getQuantityOrdered());
				}
			}else{
					quantityOrder = quantityOrder.add(bQueryList.get(j).getQuantityOrdered()==null?new BigDecimal("0.0000"):bQueryList.get(j).getQuantityOrdered());
				}
				quantityReceived = quantityReceived.add(bQueryList.get(j).getQuantityReceived() ==null?new BigDecimal("0.0000"):bQueryList.get(j).getQuantityReceived());
			}
		}
			itsLogger.info("VepoDetail quantityOrder::"+quantityOrder);
			if(objPrwarehouseinventory.getInventoryOnOrder()!=null){
				BigDecimal order = objPrwarehouseinventory.getInventoryOnOrder();
				objPrwarehouseinventory.setInventoryOnOrder(quantityOrder.subtract(quantityReceived));
			}
			objPrwarehouseinventory.setInventoryOnHand(quantityReceived);
			aVePOSession.update(objPrwarehouseinventory);
			
			List<Prwarehouseinventory> aQueryList = null;
			Query querys = aVePOSession
					.createQuery("FROM  Prwarehouseinventory WHERE prMasterID="+objVepodetail.getPrMasterId());
				aQueryList = querys.list();
		BigDecimal quantityOnHand = new BigDecimal("0.0000");
		BigDecimal quantityOnOrder = new BigDecimal("0.0000");
		if(aQueryList.size()>0){
			for(int j=0;j<aQueryList.size();j++){
				quantityOnHand = quantityOnHand.add(aQueryList.get(j).getInventoryOnHand()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnHand());
				quantityOnOrder = quantityOnOrder.add(aQueryList.get(j).getInventoryOnOrder()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnOrder());
			}
			 aPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
					 objVepodetail.getPrMasterId());
			aPrmaster.setInventoryOnHand(quantityOnHand);
			aPrmaster.setInventoryOnOrder(quantityOnOrder);
			aVePOSession.update(aPrmaster);
		}
			
			
			aTransaction.commit();			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}*/
	
	
	
	@Override
	public Integer vepoReceiveInventoryDate(Vepo vepo,Integer veReceiveID,Integer UserID,String UserName) throws VendorException {
		Session aSession = itsSessionFactory.openSession();
		Vepo aFactory = null;
		Vereceivedetail aVereceivedetail = null;
		Vereceive aVereceive = null;
		int receiveID = 0;
		Integer aveReceiveIDforref = 0;
		TpInventoryLog theTpInventoryLog = null;
		BigDecimal OldReceivedQuantity = new BigDecimal(0);
		try {
			Vepo aVeFactory = (Vepo) aSession.get(Vepo.class, vepo.getVePoid());
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			aVeFactory.setreceiveddate(vepo.getreceiveddate());
			List<?> averecevedet = null;
			aSession.update(aVeFactory);
									
			if(veReceiveID!=null && veReceiveID!=0)
			{
				aVereceive = (Vereceive) aSession.get(Vereceive.class, veReceiveID);
				aVereceive.setRxMasterId(aVeFactory.getRxVendorId());
				aVereceive.setReceiveDate(vepo.getreceiveddate());
				aVereceive.setVePoid(vepo.getVePoid());
				aSession.update(aVereceive);
				averecevedet = aSession.createSQLQuery("SELECT vePODetailID,SUM(QuantityReceived) FROM veReceiveDetail vrd WHERE vrd.vePOID = "+vepo.getVePoid()+" AND vrd.veReceiveID <> "+veReceiveID+" GROUP BY vePODetailID").list();
				if(averecevedet.size()>0)
			    {
			     for(int i=0;i<averecevedet.size();i++)
			     {
			      Object row[] = (Object[]) averecevedet.get(i);
			      Query querys = aSession.createSQLQuery("SELECT veReceiveDetailID,veReceiveID,prMasterID,QuantityReceived,vePOID FROM veReceiveDetail  WHERE vePOID="+vepo.getVePoid()+" AND vrd.veReceiveID = "+veReceiveID+" AND vrd.vePODetailID = "+row[0]);
				  Iterator<?> aIterator = querys.list().iterator();
				  while (aIterator.hasNext()) {
					  	theTpInventoryLog = new TpInventoryLog();
						Object[] aObj = (Object[]) aIterator.next();
						Vepo theVepo = (Vepo) aSession.get(Vepo.class, (Integer)aObj[2]);
						theTpInventoryLog.setPrMasterID((Integer)aObj[2]);
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+(Integer)aObj[2]);
						theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						theTpInventoryLog.setWareHouseID(theVepo.getPrWarehouseId());
						theTpInventoryLog.setTransType("RI");
						theTpInventoryLog.setTransDecription("RI Rollback");
						theTpInventoryLog.setTransID((Integer)aObj[1]);
						theTpInventoryLog.setTransDetailID((Integer)aObj[0]);
						OldReceivedQuantity = ((BigDecimal)aObj[3]);
						theTpInventoryLog.setProductIn((OldReceivedQuantity).compareTo(new BigDecimal("0.0000"))==-1?(OldReceivedQuantity).multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						theTpInventoryLog.setProductOut((OldReceivedQuantity).compareTo(new BigDecimal("0.0000"))==1?(OldReceivedQuantity):new BigDecimal("0.0000"));
						theTpInventoryLog.setUserID(theVepo.getCreatedById());
						theTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
						//if(OldReceivedQuantity.compareTo(new BigDecimal(row[1].toString()))!=0){
							/*TpInventoryLogMaster
							 * Created on 04-12-2015
							 * Code Starts
							 * RollBack
							 * */
								BigDecimal qo=OldReceivedQuantity;
								Vepo thevepo=(Vepo) aSession.get(Vepo.class,(Integer)aObj[4]);
								Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class,theVepo.getPrWarehouseId());
								Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
								
								TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
										aPrmaster.getPrMasterId(),
										aPrmaster.getItemCode(),
										theVepo.getPrWarehouseId() ,
										theprwarehouse.getSearchName(),
										aPrmaster.getInventoryOnHand().add(qo.multiply(new BigDecimal(-1))),
										theprwarehsinventory.getInventoryOnHand().add(qo.multiply(new BigDecimal(-1))),
										BigDecimal.ZERO,qo,"RI",(Integer)aObj[1],
										(Integer)aObj[0],aObj[4].toString(),thevepo.getPonumber(),
						/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
						/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
										"RI Edited",UserID,UserName,new Date());
								itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
								/*Code Ends*/
						//	}
						
				  }
				  
				  aSession.createSQLQuery("UPDATE veReceiveDetail vrd,vePODetail vp SET vrd.QuantityReceived = (vp.QuantityReceived-"+row[1]+") WHERE vp.vePODetailID = vrd.vePODetailID AND vrd.vePOID = "+vepo.getVePoid()+" AND vrd.veReceiveID = "+veReceiveID+" AND vrd.vePODetailID = "+row[0]).executeUpdate();
				  
				  Query queryVeReceiveDetail = aSession.createSQLQuery("SELECT veReceiveDetailID,veReceiveID,prMasterID,QuantityReceived,vePOID FROM veReceiveDetail  WHERE vePOID="+vepo.getVePoid()+" AND veReceiveID = "+veReceiveID+" AND vePODetailID = "+row[0]);
				  Iterator<?> aVeReDetailIterator = queryVeReceiveDetail.list().iterator();
				  while (aVeReDetailIterator.hasNext()) {
						Object[] aVeReDeObj = (Object[]) aVeReDetailIterator.next();
						 if(theTpInventoryLog !=null){
							  theTpInventoryLog.setProductOut(((BigDecimal)aVeReDeObj[3]).compareTo(new BigDecimal("0.0000"))==-1?(((BigDecimal) aVeReDeObj[3]).multiply(new BigDecimal(-1))):new BigDecimal("0.0000"));
							  theTpInventoryLog.setProductIn(((BigDecimal)aVeReDeObj[3]).compareTo(new BigDecimal("0.0000"))==1?((BigDecimal)aVeReDeObj[3]):new BigDecimal("0.0000"));
							  theTpInventoryLog.setTransType("RI");
							  theTpInventoryLog.setTransDecription("RI Edited");
							  itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
							  
							  //if(OldReceivedQuantity.compareTo(new BigDecimal(row[1].toString()))!=0){
									/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 * RollBack Insert
									 * */
										BigDecimal qo=(BigDecimal)aVeReDeObj[3];
										Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+(Integer)aVeReDeObj[2]);
										Vepo thevepo=(Vepo) aSession.get(Vepo.class,(Integer)aVeReDeObj[4]);
										Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class,thevepo.getPrWarehouseId());
										Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thevepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
										TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
												aPrmaster.getPrMasterId(),
												aPrmaster.getItemCode(),
												thevepo.getPrWarehouseId() ,
												theprwarehouse.getSearchName(),
												aPrmaster.getInventoryOnHand(),
												theprwarehsinventory.getInventoryOnHand(),
												BigDecimal.ZERO,qo.multiply(new BigDecimal(-1)),"RI",(Integer)aVeReDeObj[1],
												(Integer)aVeReDeObj[0],aVeReDeObj[4].toString(),thevepo.getPonumber(),
								/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
								/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
												"RI Edited",UserID,UserName,new Date());
										itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
										/*Code Ends*/
								//	}
						  }
				  }
			     }
			    }
			    else
			    {
			     averecevedet = aSession.createSQLQuery("SELECT vePODetailID,QuantityReceived FROM vePODetail WHERE vePOID = "+vepo.getVePoid()).list(); 
			     for(int i=0;i<averecevedet.size();i++)
			     {
			      Object rows[] = (Object[]) averecevedet.get(i);
			      Query querys = aSession.createSQLQuery("SELECT veReceiveDetailID,veReceiveID,prMasterID,QuantityReceived,vePOID FROM veReceiveDetail  WHERE vePOID="+vepo.getVePoid()+" AND veReceiveID = "+veReceiveID+" AND vePODetailID = "+rows[0]);
				  Iterator<?> aIterator = querys.list().iterator();
				  while (aIterator.hasNext()) {
					  	theTpInventoryLog = new TpInventoryLog();
						Object[] aObj = (Object[]) aIterator.next();
						Vepo theVepo = (Vepo) aSession.get(Vepo.class, (Integer)aObj[4]);
						theTpInventoryLog.setPrMasterID((Integer)aObj[2]);
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+(Integer)aObj[2]);
						theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						theTpInventoryLog.setWareHouseID(theVepo.getPrWarehouseId());
						theTpInventoryLog.setTransType("RI");
						theTpInventoryLog.setTransDecription("RI Rollback");
						theTpInventoryLog.setTransID((Integer)aObj[1]);
						theTpInventoryLog.setTransDetailID((Integer)aObj[0]);
						OldReceivedQuantity = ((BigDecimal)aObj[3]);
						theTpInventoryLog.setProductIn((OldReceivedQuantity).compareTo(new BigDecimal("0.0000"))==-1?(OldReceivedQuantity).multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						theTpInventoryLog.setProductOut((OldReceivedQuantity).compareTo(new BigDecimal("0.0000"))==1?(OldReceivedQuantity):new BigDecimal("0.0000"));
						theTpInventoryLog.setUserID(theVepo.getCreatedById());
						theTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
						//if(OldReceivedQuantity.compareTo(new BigDecimal(row[1].toString()))!=0){
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * RollBack
						 * */
							BigDecimal qo=OldReceivedQuantity;
							Vepo thevepo=(Vepo) aSession.get(Vepo.class,(Integer)aObj[4]);
							Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class,theVepo.getPrWarehouseId());
							Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),
									aPrmaster.getItemCode(),
									theVepo.getPrWarehouseId() ,
									theprwarehouse.getSearchName(),
									aPrmaster.getInventoryOnHand().add(qo.multiply(new BigDecimal(-1))),
									theprwarehsinventory.getInventoryOnHand().add(qo.multiply(new BigDecimal(-1))),
									BigDecimal.ZERO,qo,"RI",(Integer)aObj[1],
									(Integer)aObj[0],aObj[4].toString(),thevepo.getPonumber(),
					/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
					/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
									"RI Edited",UserID,UserName,new Date());
							itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
							/*Code Ends*/
					//	}
				  }
				  aSession.createSQLQuery("UPDATE veReceiveDetail SET QuantityReceived = "+rows[1]+" WHERE vePOID = "+vepo.getVePoid()+"  AND veReceiveID = "+veReceiveID+" AND vePODetailID = "+rows[0]).executeUpdate();
				  
				  Query queryVeReceiveDetail = aSession.createSQLQuery("SELECT veReceiveDetailID,veReceiveID,prMasterID,QuantityReceived,vePOID FROM veReceiveDetail  WHERE vePOID="+vepo.getVePoid()+" AND veReceiveID = "+veReceiveID+" AND vePODetailID = "+rows[0]);
				  Iterator<?> aVeReDetailIterator = queryVeReceiveDetail.list().iterator();
				  while (aVeReDetailIterator.hasNext()) {
						Object[] aVeReDeObj = (Object[]) aVeReDetailIterator.next();
						 if(theTpInventoryLog !=null){
							  theTpInventoryLog.setProductOut(((BigDecimal)aVeReDeObj[3]).compareTo(new BigDecimal("0.0000"))==-1?(((BigDecimal) aVeReDeObj[3]).multiply(new BigDecimal(-1))):new BigDecimal("0.0000"));
							  theTpInventoryLog.setProductIn(((BigDecimal)aVeReDeObj[3]).compareTo(new BigDecimal("0.0000"))==1?((BigDecimal)aVeReDeObj[3]):new BigDecimal("0.0000"));
							  theTpInventoryLog.setTransType("RI");
							  theTpInventoryLog.setTransDecription("RI Edited");
							  itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
							  //if(OldReceivedQuantity.compareTo(new BigDecimal(row[1].toString()))!=0){
								/*TpInventoryLogMaster
								 * Created on 04-12-2015
								 * Code Starts
								 * RollBack Insert
								 * */
									BigDecimal qo=(BigDecimal)aVeReDeObj[3];
									Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+(Integer)aVeReDeObj[2]);
									Vepo thevepo=(Vepo) aSession.get(Vepo.class,(Integer)aVeReDeObj[4]);
									Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class,thevepo.getPrWarehouseId());
									Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thevepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
									TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
											aPrmaster.getPrMasterId(),
											aPrmaster.getItemCode(),
											thevepo.getPrWarehouseId() ,
											theprwarehouse.getSearchName(),
											aPrmaster.getInventoryOnHand(),
											theprwarehsinventory.getInventoryOnHand(),
											BigDecimal.ZERO,qo.multiply(new BigDecimal(-1)),"RI",(Integer)aVeReDeObj[1],
											(Integer)aVeReDeObj[0],aVeReDeObj[4].toString(),thevepo.getPonumber(),
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
											"RI Edited",UserID,UserName,new Date());
									itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
									/*Code Ends*/
							//	}
						  }
				  }
			     }     
			    }
				receiveID = veReceiveID;
			}
			else
			{
			
				aVereceive = new Vereceive();
				aVereceive.setRxMasterId(aVeFactory.getRxVendorId());
				aVereceive.setReceiveDate(vepo.getreceiveddate());
				aVereceive.setVePoid(vepo.getVePoid());
				receiveID = (Integer)aSession.save(aVereceive);
				Integer veReceiveDetailID = null;
				
				Vereceive theVereceive=(Vereceive) aSession.get(Vereceive.class, receiveID);
				
				Query querys = aSession.createSQLQuery("select vePODetailID,prMasterID,Description,Note,QuantityReceived FROM vePODetail WHERE vePOID="+vepo.getVePoid());
				Iterator<?> aIterator = querys.list().iterator();
				while (aIterator.hasNext()) {
					aVereceivedetail = new Vereceivedetail();
					Object[] aObj = (Object[]) aIterator.next();
					BigDecimal qtyrec = BigDecimal.ZERO;
					aVereceivedetail.setVeReceiveId(receiveID);
					aVereceivedetail.setVePodetailId((Integer)aObj[0]);
					aVereceivedetail.setPrMasterId((Integer)aObj[1]);
					aVereceivedetail.setDescription((String)aObj[2]);
					aVereceivedetail.setNote((String)aObj[3]);
					aVereceivedetail.setVePOID(vepo.getVePoid());
					
					qtyrec = (BigDecimal)aSession.createSQLQuery("SELECT SUM(QuantityReceived) FROM veReceiveDetail vrd WHERE vrd.vePOID  = "+vepo.getVePoid()+" AND vrd.vePODetailID = "+aVereceivedetail.getVePodetailId()).uniqueResult();
					
					if(qtyrec!=null)
					aVereceivedetail.setQuantityReceived(((BigDecimal)aObj[4]).subtract(qtyrec));	
					else
					aVereceivedetail.setQuantityReceived((BigDecimal)aObj[4]);
					
					veReceiveDetailID = (Integer) aSession.save(aVereceivedetail);
					if(veReceiveDetailID>0){
						Vepo aVepo = (Vepo) aSession.get(Vepo.class, theVereceive.getVePoid());
						TpInventoryLog aTpInventoryLog = new TpInventoryLog();
						aTpInventoryLog.setPrMasterID(aVereceivedetail.getPrMasterId());
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVereceivedetail.getPrMasterId());
						aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
						aTpInventoryLog.setTransType("RI");
						aTpInventoryLog.setTransDecription("RI Created");
						aTpInventoryLog.setTransID(aVereceivedetail.getVeReceiveId());
						aTpInventoryLog.setTransDetailID(aVereceivedetail.getVeReceiveDetailId());
						aTpInventoryLog.setProductOut(aVereceivedetail.getQuantityReceived().compareTo(new BigDecimal("0.0000"))==-1?aVereceivedetail.getQuantityReceived().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						aTpInventoryLog.setProductIn(aVereceivedetail.getQuantityReceived().compareTo(new BigDecimal("0.0000"))==1?aVereceivedetail.getQuantityReceived():new BigDecimal("0.0000"));
						aTpInventoryLog.setUserID(aVepo.getCreatedById());
						aTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
						
						//if(OldReceivedQuantity.compareTo(new BigDecimal(row[1].toString()))!=0){
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * Insert
						 * */
							BigDecimal qo=aVereceivedetail.getQuantityReceived();
							Vepo thevepo=(Vepo) aSession.get(Vepo.class,aVereceivedetail.getVePOID());
							Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class,thevepo.getPrWarehouseId());
							Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thevepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),
									aPrmaster.getItemCode(),
									thevepo.getPrWarehouseId() ,
									theprwarehouse.getSearchName(),
									aPrmaster.getInventoryOnHand(),
									theprwarehsinventory.getInventoryOnHand(),
									BigDecimal.ZERO,qo.multiply(new BigDecimal(-1)),"RI",aVereceivedetail.getVeReceiveId(),
									veReceiveDetailID,aVereceivedetail.getVePOID().toString(),thevepo.getPonumber(),
					/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
					/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
									"RI Created",UserID,UserName,new Date());
							itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
							/*Code Ends*/
					//	}
					}
					
				}
			
			
			}
			transaction.commit();
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return receiveID;
	}
	
	@Override
	public Integer addVendorInvoice(Vebill aVebill,Vebilldistribution veDist,Integer yearID, Integer periodID,String username) throws VendorException {
		Session aSession = null;
		Transaction aTransaction = null;
		Integer veBIllId = null;
		try {
			if(aVebill.getVeBillId() != null && aVebill.getVeBillId() != 0)
			{
				aSession = itsSessionFactory.openSession();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Vebill theVebill = (Vebill)aSession.get(Vebill.class, aVebill.getVeBillId());

				if (aVebill.getReceiveDate() != null) {
					theVebill.setReceiveDate(aVebill.getReceiveDate());
				}
				if (aVebill.getBillDate() != null) {
					theVebill.setBillDate(aVebill.getBillDate());
				}
				if (aVebill.getDueDate() != null) {
					theVebill.setDueDate(aVebill.getDueDate());
				}
				
				if (aVebill.getShipDate() != null) {
					theVebill.setShipDate(aVebill.getShipDate());
				}
				theVebill.setVeShipViaId(aVebill.getVeShipViaId());
				theVebill.setApaccountId(aVebill.getApaccountId());
				theVebill.setInvoiceNumber(aVebill.getInvoiceNumber());
				
				if(aVebill.getRxMasterId() != null)
					theVebill.setRxMasterId(aVebill.getRxMasterId());
				if(aVebill.isUsePostDate())
				{
					theVebill.setUsePostDate(true);
					if (aVebill.getPostDate() != null) {
						theVebill.setPostDate(aVebill.getPostDate());
					}
					
				}
				else
				{
					theVebill.setUsePostDate(false);
					if (aVebill.getPostDate() != null) {
						theVebill.setPostDate(aVebill.getPostDate());
					}
				}
				theVebill.setTrackingNumber(aVebill.getTrackingNumber());
				theVebill.setBillAmount(aVebill.getBillAmount());
				theVebill.setFreightAmount(aVebill.getFreightAmount());
				theVebill.setVePoid(aVebill.getVePoid());
				theVebill.setTransactionStatus(aVebill.getTransactionStatus());
				
				//theVebill.setAppliedAmount(aVebill.getAppliedAmount());
				theVebill.setTaxAmount(aVebill.getTaxAmount());
				theVebill.setReason(aVebill.getReason());
				
				theVebill.setApplied(aVebill.isApplied());
				theVebill.setVoid_(aVebill.isVoid_());
				theVebill.setVeBillId(aVebill.getVeBillId());
				

				veBIllId =aVebill.getVeBillId();
				/**GL Transaction Insert*/
				
				//receivingMiscellaneousBill(aVebill,veDist,yearID,periodID,username);
				
				
				
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.update(theVebill);				
				aTransaction.commit();
				
			
			}
			else{
				aSession = itsSessionFactory.openSession();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				veBIllId = (Integer)aSession.save(aVebill);
				aTransaction.commit();
			}
			
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return veBIllId;
	}
	
	@Override
	public void receivingMiscellaneousBill(Vebill aVebill,Vebilldistribution veDist,Integer yearID,Integer periodID,String username,String reason) throws BankingException, VendorException, CompanyException, JobException
	{
		Session aSession1 = null;		
		Transaction aTransaction = null;
		Integer veBIllId = null;
		String aQuery = null;
		
		Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("VB");
		
		ArrayList<GlLinkage> arrayglLinkagelist= gltransactionService.getAllGlLinkageDetails( aVebill.getVeBillId(), aColedgersource.getCoLedgerSourceId());
		
	try
	{
       if(arrayglLinkagelist.size()==0)
       {
    	   insertGlTransactionforMiscellaneous(aVebill,yearID,periodID,username);
       }
       else
       {
    	    ArrayList<Object> arrayVeBillID = gltransactionService.getAllVeBillDetailsandDistripution( aVebill.getVeBillId());
			int count=0;
			java.util.Iterator pairs = arrayVeBillID.iterator();
			while (pairs.hasNext()) {
				Object[] pair = (Object[]) pairs.next();
				Vebill veobj = (Vebill) pair[0];
				Vebilldistribution vedobj = (Vebilldistribution) pair[1];
				System.out.println(vedobj.getJoMasterId()+"---------------->>");
								
			}
			
			// For Getting New Period	
			Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
			Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
			
			if(reason!= null && !reason.trim().equals(""))
			{
				ArrayList<GlTransaction> arrayglTransList=new ArrayList<GlTransaction>();
			
				for(GlLinkage glLinkageObj:arrayglLinkagelist)
				{
					aSession1 = itsSessionFactory.openSession();
					
					arrayglTransList=gltransactionService.getAllglTransactionDetails(glLinkageObj.getGlTransactionId());
					
					for(GlTransaction glTransObj:arrayglTransList)
					{
						
						glTransObj.setTransactionDate(aVebill.getBillDate());
						
						// period
						glTransObj.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
						glTransObj.setPeriod(aCofiscalperiod.getPeriod());
						glTransObj.setpStartDate(aCofiscalperiod.getStartDate());
						glTransObj.setpEndDate(aCofiscalperiod.getEndDate());
		
						// year
						glTransObj.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
						glTransObj.setFyear(aCofiscalyear.getFiscalYear());
						glTransObj.setyStartDate(aCofiscalyear.getStartDate());
						glTransObj.setyEndDate(aCofiscalyear.getEndDate());
					
						   if (glTransObj.getCredit().compareTo(BigDecimal.ZERO) != 0)
						   {
							   gltransactionService.insertglAprollBackInsert(glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID());
						   }
						   else
						   {
							   gltransactionService.insertglrollBackInsert( glTransObj,glLinkageObj.getCoLedgerSourceId(),glLinkageObj.getVeBillID());
						   }
				
					}
					
					try
					{
					aTransaction = aSession1.beginTransaction();
					aTransaction.begin();
					aQuery = "update glLinkage set status=1 where glTransactionId ="+glLinkageObj.getGlTransactionId();
					aSession1.createSQLQuery(aQuery).executeUpdate();
					aTransaction.commit();
					}
					catch(Exception excep){itsLogger.error(excep.getMessage(), excep);}
					finally
					{
						aSession1.flush();
						aSession1.close();
						aQuery =null;
					}
				
				}
				veBIllId= aVebill.getVeBillId();
				//added by prasant #Void invoice only rollback entry show in glTransaction
				if(aVebill.getTransactionStatus()!=-1)
				 insertGlTransactionforMiscellaneous(aVebill,yearID,periodID,username);
			}
			
       }
       
	}
	 catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
		JobException aJobException = new JobException(e.getMessage(), e);
		throw aJobException;
	} 
	}
	
	
	public void insertGlTransactionforMiscellaneous(Vebill aVebill,Integer yearID,Integer periodID,String username) throws BankingException, CompanyException
	{

		
		Coaccount CoAccountIdapdetails = gltransactionService.getCoaccountDetailsBasedoncoAccountid(aVebill.getApaccountId());
		Rxmaster liRxmasters = gltransactionService.getTransactionDescriptionfromrxMasterID(aVebill.getRxMasterId());

		Cofiscalperiod aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
		Cofiscalyear aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
		Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("VB");
		
		GlTransaction glTransaction = new GlTransaction();

		// period
		glTransaction.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
		
		if(aVebill.getBillDate()!=null)
		{
			/*Calendar difcurdate = Calendar.getInstance();
			difcurdate.setTime(aVebill.getBillDate());
			int period = difcurdate.get(Calendar.MONTH);
			itsLogger.info("Period:"+period +"Add 1 :"+(period+1));
			glTransaction.setPeriod(period+1);*/
			Cofiscalperiod getperiod =  accountingCyclesService.getAllOpenPeriods(aVebill.getBillDate());
			glTransaction.setPeriod(getperiod.getPeriod());
		}
		else
		{
			glTransaction.setPeriod(aCofiscalperiod.getPeriod());
		}
		//glTransaction.setPeriod(aCofiscalperiod.getPeriod());
		glTransaction.setpStartDate(aCofiscalperiod.getStartDate());
		glTransaction.setpEndDate(aCofiscalperiod.getEndDate());

		// year
		glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
		if(aCofiscalyear.getFiscalYear()!=null)
		glTransaction.setFyear(aCofiscalyear.getFiscalYear());
		glTransaction.setyStartDate(aCofiscalyear.getStartDate());
		glTransaction.setyEndDate(aCofiscalyear.getEndDate());

		// journal
		glTransaction.setJournalId(aColedgersource.getJournalID());
		glTransaction.setJournalDesc(aColedgersource.getDescription());
		glTransaction.setEntrydate(new Date());
		
		glTransaction.setTransactionDate(aVebill.getBillDate());
		glTransaction.setEnteredBy(username);
		

		// Ponumber and trDesc
		
	
		glTransaction.setPoNumber(aVebill.getInvoiceNumber());
		glTransaction.setTransactionDesc(liRxmasters.getName());
		BigDecimal dBorCrAmount= new BigDecimal(0);
		BigDecimal totAmount=new BigDecimal(0);
		
		ArrayList<Vebilldistribution> arrayVeBillIDlist = gltransactionService.getAllVeBillDistripution( aVebill.getVeBillId());
		
		for(Vebilldistribution arrayVeBillID:arrayVeBillIDlist)
		{
			
			Coaccount lineItemAccNo = gltransactionService.getCoaccountDetailsBasedoncoAccountid(arrayVeBillID.getCoExpenseAccountId());
			
			insertLineItemswithoutPO(glTransaction, lineItemAccNo, aVebill,aColedgersource,arrayVeBillID.getExpenseAmount(),dBorCrAmount);
			
			totAmount= arrayVeBillID.getExpenseAmount().add(totAmount);
			
		}
		
		insertAccountPayablewithoutPO(glTransaction, CoAccountIdapdetails, aVebill,aColedgersource,totAmount,dBorCrAmount);
		
	}
	
	

	public void insertLineItemswithoutPO(GlTransaction glTransaction,
			Coaccount CoAccountIdapdetails, Vebill aVebill,
			Coledgersource aColedgersource,BigDecimal amount,BigDecimal dBorCrAmount) throws BankingException {
		
		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;
		
		Session sess=null;
		
		itsLogger.info("*************LineItems without PO Starts Here**************");

		// Line Item
		

		glTransaction.setCoAccountId(CoAccountIdapdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdapdetails.getNumber());

		// Line Item Amount

		if(amount.compareTo(BigDecimal.ZERO) < 0)
		{
		glTransaction.setDebit(dBorCrAmount);
		glTransaction.setCredit(amount.negate()); //  for negative amount
		}
		else
		{
		glTransaction.setDebit( amount);
		glTransaction.setCredit(dBorCrAmount);
		}
		
		if (amount.compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = gltransactionService.saveGltransactionTable(glTransaction);

			itsLogger.info("coLedgerSourceId == "
					+ aColedgersource.getCoLedgerSourceId()
					+ "GlTransactionid == " + glTransationid + "VeBillid == "
					+ aVebill.getVeBillId());

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aVebill.getVeBillId());
			glLinkage.setStatus(0);
			gltransactionService.saveGlLinkageTable(glLinkage);

		}
	
	itsLogger.info("*************LineItems without PO Ends Here**************");
	
	}
	
	
	
	public void insertAccountPayablewithoutPO(GlTransaction glTransaction,
			Coaccount CoAccountIdapdetails, Vebill aVebill,
			Coledgersource aColedgersource,BigDecimal amount,BigDecimal dBorCrAmount) throws BankingException {
		
		GlLinkage glLinkage = new GlLinkage();

		int glTransationid = 0;
		
		itsLogger.info("*************Acccount Payable without PO Starts Here**************");

		// AccountPayable
		
		System.out.println("AccountPayable Accountid="+CoAccountIdapdetails.getCoAccountId()+" AccountNumber="+CoAccountIdapdetails.getNumber());

		glTransaction.setCoAccountId(CoAccountIdapdetails.getCoAccountId());
		glTransaction.setCoAccountNumber(CoAccountIdapdetails.getNumber());

		// AccountPayable Amount

		
		
		if(amount.compareTo(BigDecimal.ZERO) < 0)
		{
			glTransaction.setDebit( amount.negate()); //  for negative amount
			glTransaction.setCredit(dBorCrAmount);
		}
		else
		{
			glTransaction.setDebit( dBorCrAmount);
			glTransaction.setCredit(amount);
		}
		

		if (amount.compareTo(BigDecimal.ZERO) != 0) {
			glTransationid = gltransactionService.saveGltransactionTable(glTransaction);

			itsLogger.info("coLedgerSourceId == "
					+ aColedgersource.getCoLedgerSourceId()
					+ "GlTransactionid == " + glTransationid + "VeBillid == "
					+ aVebill.getVeBillId());

			glLinkage
					.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(glTransationid);
			glLinkage.setEntryDate(new Date());
			glLinkage.setVeBillID(aVebill.getVeBillId());
			glLinkage.setStatus(0);
			gltransactionService.saveGlLinkageTable(glLinkage);

		}
	
	itsLogger.info("*************Acccount Payable without PO Ends Here**************");
	
	}
	
	
	
	
	
	@Override
	public ArrayList<AutoCompleteBean> getCoAccountDetails(String theProductName) throws JobException {
		String salesselectQry = "SELECT coAccountID, Number, Description FROM coAccount WHERE Number like '"
				+ theProductName + "%' ORDER BY Number ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				if (((String) aObj[2]) == null) {
					aUserbean.setLabel((String) aObj[1]);
				} else {
					String aProduct = (String) aObj[1] + " -["
							+ (String) aObj[2] + "]";
					aUserbean.setLabel(aProduct);
				}
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public ArrayList<AutoCompleteBean> getJobList(String theProductName) throws JobException {
		String salesselectQry = "SELECT joMasterID, JobNumber, Description From joMaster Where (JobStatus = 3) and JobNumber like '"+theProductName+"%' ORDER BY JobNumber";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				if (((String) aObj[2]) == null) {
					aUserbean.setLabel((String) aObj[1]);
				} else {
					String aProduct = (String) aObj[1] + " -["
							+ (String) aObj[2] + "]";
					aUserbean.setLabel(aProduct);
				}
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public Vepo getPONumber(String thePONumber) throws JobException {
		String salesselectQry = "SELECT vePOID,PONumber From vePO Where PONumber = '"+thePONumber+"'";
		Session aSession = null;
		Vepo aVepo = null;
		String jobName = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			List aList = aQuery.list();
			if(aList.size() > 0)
			{
				Iterator<?> iterator = aList.iterator();
				if(iterator.hasNext())
				{
					Object[] aObj = (Object[]) iterator.next();
					Integer vepoID = (Integer)aObj[0];
					if(vepoID != null){
						aVepo = (Vepo)aSession.get(Vepo.class, vepoID);
						if(aVepo.getJoReleaseId() == null || aVepo.getJoReleaseId() == 1)
						{
							aVepo.setJoReleaseId(aVepo.getJoReleaseId());
							aVepo.setUpdateKey(aVepo.getTag());
						}
						else
						{
							jobName = "SELECT mas.Description FROM joMaster AS mas LEFT JOIN joRelease jo ON jo.joMasterID = mas.joMasterID WHERE jo.joReleaseID ="+ aVepo.getJoReleaseId();
							//jobSession = itsSessionFactory.openSession();
							Query aQuery1 = aSession.createSQLQuery(jobName.toString());
							String sjobname = "";
							if(aQuery1.list().size() > 0)
								sjobname = (String)aQuery1.list().get(0);
							aVepo.setJoReleaseId(aVepo.getJoReleaseId());
							aVepo.setUpdateKey(sjobname);
							
						}
					}
				}
				//String poNumber = (String)aQuery.list().get(0);
				return aVepo;
			}
			else
			{
				return new Vepo();
			}			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
			jobName = null;
		}
	}
	@Override
	public List<Vepodetail> getPOLineDetails(Integer theVepoID)
			throws JobException {

		String aPOLineItemListQry = "";
		if (theVepoID != null) {
			
			Vepo thevepo=getVepo(theVepoID);
			if(thevepo.getJoReleaseId()!=null&&thevepo.getJoReleaseId()!=0){
				//aPOLineItemListQry
				
				aPOLineItemListQry="SELECT ve.vePODetailID, ve.vePOID, ve.prMasterID, ve.Description, (IF (ve.QuantityOrdered - IFNULL (a.quaninv,0)>=0,ve.QuantityOrdered - IFNULL (a.quaninv,0),0))AS QuantityOrdered," 
						+" ve.Taxable, ve.UnitCost, ve.PriceMultiplier, ve.posistion, pr.ItemCode,  vepo.TaxTotal,  ve.Note,  "
						+" ve.EstimatedShipDate,ve.AcknowledgementDate, ve.VendorOrderNumber,ve.QuantityReceived,(ve.QuantityOrdered-ve.QuantityReceived)  "
						+" FROM vePODetail ve  LEFT JOIN prMaster pr ON ve.prMasterID = pr.prMasterID LEFT JOIN "
						+" (SELECT vePODetailID,SUM(QuantityBilled) AS quaninv FROM veBill JOIN veBillDetail USING(veBillID) WHERE veBill.vepoID="+theVepoID+" GROUP BY vePODetailID ) a "
						+" ON a.vePODetailID = ve.vePODetailID "
						+" RIGHT JOIN vePO vepo ON vepo.vePOID = ve.vePOID" 
						+" WHERE ve.vePOID =" + theVepoID; 

			}else{
			
			aPOLineItemListQry = "SELECT ve.vePODetailID," + " ve.vePOID,"
					+ " ve.prMasterID," + " ve.Description,"
					+ " (IF (ve.QuantityOrdered - IFNULL (a.quaninv,0)>=0,ve.QuantityOrdered - IFNULL (a.quaninv,0),0))AS QuantityOrdered," + " ve.Taxable," + " ve.UnitCost,"
					+ " ve.PriceMultiplier," + " ve.posistion,"
					+ " pr.ItemCode, " + " vepo.TaxTotal, " + " ve.Note, "
					+ " ve.EstimatedShipDate," + "ve.AcknowledgementDate,"
					+ " ve.VendorOrderNumber," + "ve.QuantityReceived,"
					+ "(ve.QuantityOrdered-ve.QuantityReceived) "
					+ " FROM vePODetail ve "
					+ " Left Join prMaster pr on ve.prMasterID = pr.prMasterID"
					+ " LEFT JOIN (SELECT vePODetailID,SUM(quantityInvoiced) AS quaninv FROM veBillHistory WHERE vePOID="+theVepoID+" GROUP BY vePODetailID ) a ON a.vePODetailID = ve.vePODetailID"
					+ " Right Join vePO vepo on vepo.vePOID = ve.vePOID"
					+ " where ve.vePOID = " + theVepoID
					+ " ORDER BY ve.posistion";
			}
		}
		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		BigDecimal aTotal;
		BigDecimal aNetCast;
		try {
			Vepodetail avepoDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				avepoDetail = new Vepodetail();
				Object[] aObj = (Object[]) aIterator.next();
				avepoDetail.setVePodetailId((Integer) aObj[0]);
				avepoDetail.setVePoid((Integer) aObj[1]);
				avepoDetail.setPrMasterId((Integer) aObj[2]);
				avepoDetail.setDescription((String) aObj[3]);
				avepoDetail.setQuantityOrdered((BigDecimal) aObj[4]);
				avepoDetail.setQuantityReceived((BigDecimal) aObj[15]);
				itsLogger.info("Available Quantities:"+(BigDecimal) aObj[4] +" "+(BigDecimal) aObj[15]+" "+(BigDecimal) aObj[16]);
				if ((Byte) aObj[5] == 1) {
					avepoDetail.setTaxable(true);
				} else {
					avepoDetail.setTaxable(false);
				}
				avepoDetail.setUnitCost((BigDecimal) aObj[6]);
				avepoDetail.setPriceMultiplier((BigDecimal) aObj[7]);
				avepoDetail.setPosistion((Double) aObj[8]);
				String notes = (String) aObj[11];
				if (null == notes || "null".equalsIgnoreCase(notes) || "".equalsIgnoreCase(notes) || "undefined".equalsIgnoreCase(notes) || notes.length() <= 0) {
					notes = "";
				}
				avepoDetail.setPrItemCode(((String) aObj[9]));
				System.out.println(avepoDetail.getNote());
				avepoDetail.setNote(notes);
				BigDecimal aUnitCost = JobUtil.floorFigureoverall(((BigDecimal) aObj[6]),2);
				BigDecimal aPriceMult = ((BigDecimal) aObj[7]);
				BigDecimal aQuantity = JobUtil.floorFigureoverall(((BigDecimal) aObj[4]),2);
				if (aUnitCost != null && aPriceMult != null
						&& aQuantity != null) {
					if (Double.valueOf(aPriceMult.toString()) > Double
							.valueOf(0.0000)) {
						aTotal = aUnitCost.multiply(aPriceMult);
						aTotal = aTotal.multiply(aQuantity);
						avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
					} else {
						aTotal = aUnitCost.multiply(aQuantity);
						avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
					}
				} else if (aUnitCost != null && aQuantity != null) {
					aTotal = aUnitCost.multiply(aQuantity);
					avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
				} else if (aUnitCost != null && aPriceMult != null) {
					aTotal = aUnitCost.multiply(aQuantity);
					avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
				} else if (aUnitCost != null) {
					avepoDetail.setQuantityBilled(aUnitCost);
				}
				if (aUnitCost != null && aPriceMult != null) {
					if (Double.valueOf(aPriceMult.toString()) <= Double
							.valueOf(0.0000)) {
						avepoDetail.setNetCast(aUnitCost);
					} else {
						aNetCast = aUnitCost.multiply(aPriceMult);
						avepoDetail.setNetCast(aNetCast);
					}
				}
				avepoDetail.setTaxTotal((BigDecimal) aObj[10]);
				avepoDetail.setInLineNote((String) aObj[11]);

				if (aObj[12] != null) {
					avepoDetail.setShipDate((String) DateFormatUtils.format(
							(Date) aObj[12], "dd-MM-yyyy"));
				}
				if (aObj[13] != null) {
					avepoDetail.setAckDate((String) DateFormatUtils.format(
							(Date) aObj[13], "dd-MM-yyyy"));
				}
				String orderNumber = (String) aObj[14];
				if (null == orderNumber || "null".equalsIgnoreCase(orderNumber) || "".equalsIgnoreCase(orderNumber) || "undefined".equalsIgnoreCase(orderNumber) || orderNumber.length() <= 0) {
					orderNumber = "";
				}
				avepoDetail.setVendorOrderNumber(orderNumber);
				
				// avepoDetail.setNotesDesc((String) aObj[15]);
				/*
				 * avepoDetail.setPosistion((Double) aObj[8]);
				 * avepoDetail.setNote((String) aObj[9]);
				 * avepoDetail.setAcknowledgedDate((Date) aObj[12]);
				 * avepoDetail.setEstimatedShipDate((Date) aObj[13]);
				 */
				// avepoDetail.setoInLineNote((String) aObj[14]);
				aQueryList.add(avepoDetail);

			}

		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public List<Rxaddress> getCustomerAddresses(Integer rxMasterID) throws VendorException {
		
		String aQry = "SELECT rxMaster.Name,address.Address1,address.Address2,address.city,address.State,address.Zip,address.rxAddressId FROM rxAddress AS address JOIN rxMaster ON rxMaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = "+rxMasterID;
		Session aSession=null;
		Rxaddress objRxaddress = null;
		List<Rxaddress> alRxaddress = new ArrayList<Rxaddress>();
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			List aList = aQuery.list();
			Iterator<?> aIterator = aList.iterator();
			while(aIterator.hasNext())
			{
				objRxaddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				objRxaddress.setName((String) aObj[0]);
				objRxaddress.setAddress1((String) aObj[1]);
				objRxaddress.setAddress2((String) aObj[2]);
				objRxaddress.setCity((String) aObj[3]);
				objRxaddress.setState((String) aObj[4]);
				objRxaddress.setZip((String) aObj[5]);
				objRxaddress.setRxAddressId((Integer) aObj[6]);
				alRxaddress.add(objRxaddress);
			}
			if(aList.size() > 0)
			return alRxaddress;
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry=null;
		}	
		
	}
	
	@Override
	public List<Rxaddress> getVendorName(Integer rxMasterID) throws VendorException {
		
		String aQry = "SELECT * FROM rxMaster WHERE rxMasterID = "+rxMasterID;
		Session aSession=null;
		Rxaddress objRxaddress = null;
		List<Rxaddress> alRxaddress = new ArrayList<Rxaddress>();
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			List aList = aQuery.list();
			while(aIterator.hasNext())
			{
				objRxaddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				objRxaddress.setName((String) aObj[10]);				
				alRxaddress.add(objRxaddress);
			}
			if(aList.size() > 0)
			return alRxaddress;
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	public Integer insertVebill(Vebill aVebill) throws VendorException
	{
		Session aSession = null;
		Transaction aTransaction = null;
	
		Integer veBIllId = null;
		boolean frieghtStatus = false;
		try {
			aSession = itsSessionFactory.openSession();
			if(aVebill.getVeBillId() != null && aVebill.getVeBillId() != 0)
			{
				Vebill theVebill = (Vebill)aSession.get(Vebill.class, aVebill.getVeBillId());

				if (aVebill.getReceiveDate() != null) {
					theVebill.setReceiveDate(aVebill.getReceiveDate());
				}
				if (aVebill.getBillDate() != null) {
					theVebill.setBillDate(aVebill.getBillDate());
				}
				if (aVebill.getDueDate() != null) {
					theVebill.setDueDate(aVebill.getDueDate());
				}
				
				if (aVebill.getShipDate() != null) {
					theVebill.setShipDate(aVebill.getShipDate());
				}
				theVebill.setVeShipViaId(aVebill.getVeShipViaId());
				theVebill.setApaccountId(aVebill.getApaccountId());
				theVebill.setInvoiceNumber(aVebill.getInvoiceNumber());
				
				if(aVebill.getRxMasterId() != null)
					theVebill.setRxMasterId(aVebill.getRxMasterId());
				if(aVebill.isUsePostDate())
				{
					theVebill.setUsePostDate(true);
					if (aVebill.getPostDate() != null) {
						theVebill.setPostDate(aVebill.getPostDate());
					}
					
				}
				else
				{
					theVebill.setUsePostDate(false);
					if (aVebill.getPostDate() != null) {
						theVebill.setPostDate(aVebill.getPostDate());
					}
				}
				theVebill.setTrackingNumber(aVebill.getTrackingNumber());
				theVebill.setBillAmount(aVebill.getBillAmount());
				theVebill.setFreightAmount(aVebill.getFreightAmount());
				theVebill.setVePoid(aVebill.getVePoid());
				theVebill.setReason(aVebill.getReason());
				
				theVebill.setTransactionStatus(aVebill.getTransactionStatus());
				//theVebill.setAppliedAmount(aVebill.getAppliedAmount());
				theVebill.setTaxAmount(aVebill.getTaxAmount());
				//aVebill.setFreightAmount(new BigDecimal(0.00));
				//aVebill.setVePoid(1);
				theVebill.setApplied(aVebill.isApplied());
				theVebill.setVoid_(aVebill.isVoid_());
				theVebill.setVeBillId(aVebill.getVeBillId());
				theVebill.setChangedById(aVebill.getChangedById());
				theVebill.setChangedOn(aVebill.getChangedOn());
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.update(theVebill);				
				aTransaction.commit();
				
			
				//if(frieghtStatus)
				updatePrMaster(theVebill.getVeBillId());// update the new cost.
				
				if(aVebill.getJoReleaseDetailId()!=null)
				{
					JoReleaseDetail thejoReleaseDetail = (JoReleaseDetail)aSession.get(JoReleaseDetail.class,aVebill.getJoReleaseDetailId());
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					thejoReleaseDetail.setVendorInvoiceAmount(aVebill.getBillAmount());
					aSession.update(thejoReleaseDetail);				
					aTransaction.commit();
				}
				
				//To record veBillHistory
			}
			else{
				if(aVebill.getBillAmount()==null){
					aVebill.setBillAmount(BigDecimal.ZERO);
				}
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				veBIllId = (Integer)aSession.save(aVebill);
				aTransaction.commit();
				
			}
			
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
						
			aSession.flush();
			aSession.close();
		}
		return veBIllId;
	}
	
	
	@Override
	public void updateveBillHistory(Integer veBillID) throws VendorException {
		Session aSession = null;
		Transaction aTransaction = null;
		BigDecimal quantityinv = new BigDecimal("0");
		BigDecimal unitcost = new BigDecimal("0");
		BigDecimal pricemultiplier = new BigDecimal("0");
		Integer vePODetailID = 0;
		try {
			// for only edited line items
			aSession = itsSessionFactory.openSession();
			String aQuery = "Select * from veBillDetail where veBillID = "+veBillID;
			Query aQry = aSession.createSQLQuery(aQuery);
			Iterator<?> aIterator = aQry.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				
				if(aObj[6]==null){
					aObj[6]=BigDecimal.ZERO;
				}
				quantityinv = JobUtil.floorFigureoverall(((BigDecimal)aObj[6]),2);
				if(aObj[7]==null){
					aObj[7]=BigDecimal.ZERO;
				}
				unitcost =  JobUtil.floorFigureoverall(((BigDecimal)aObj[7]),2);
				if(aObj[8]==null){
					aObj[8]=BigDecimal.ZERO;
				}
				pricemultiplier = ((BigDecimal)aObj[8]);
				vePODetailID = (Integer) aObj[2];
				
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.createSQLQuery("update veBillHistory set quantityInvoiced = "+quantityinv+",invoiceAmount = "
					+ JobUtil.floorFigureoverall((quantityinv.multiply(unitcost).multiply(pricemultiplier)),2)
					+ " where veBillID = "+veBillID+" and  vePODetailID = "+vePODetailID).executeUpdate();
			aTransaction.commit();
			}
			// for only deleted line items
			String aQuery1 ="SELECT * FROM veBillHistory WHERE vePODetailID NOT IN (SELECT vePODetailID FROM veBillDetail WHERE veBillID = "+veBillID+") AND veBillID = "+veBillID;
			Query aQry1 = aSession.createSQLQuery(aQuery1);
			Iterator<?> aIterator1 = aQry1.list().iterator();
			while (aIterator1.hasNext()) {
				Object[] aObj1 = (Object[]) aIterator1.next();
				
				quantityinv = BigDecimal.ZERO;
				vePODetailID = (Integer) aObj1[3];
				
			aTransaction.begin();
			aSession.createSQLQuery("update veBillHistory set quantityInvoiced = "+quantityinv+",invoiceAmount =0 "
						+ " where veBillID = "+veBillID+" and  vePODetailID = "+vePODetailID).executeUpdate();
			aTransaction.commit();
			}
			
			
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	public Integer insertVebillDetail(Integer veBillid, Integer vepoID,Integer userID) throws VendorException
	{
		Session aSession = null;
		Transaction aTransaction = null;
		Integer veBillDetailID=0;
		String insertInvoiceQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			insertInvoiceQuery = "INSERT into veBillDetail (veBillID,vePODetailID,prMasterID, Description, Note,  QuantityBilled, UnitCost,PriceMultiplier)  "
					+ " SELECT "+veBillid+",veP.vePODetailID,veP.prMasterID ,veP.Description ,veP.Note ,(IF (veP.QuantityOrdered - IFNULL (a.quaninv,0)>=0,veP.QuantityOrdered - IFNULL (a.quaninv,0),0))AS QuantityOrdered,veP.UnitCost,veP.PriceMultiplier"
					+ " FROM vePODetail veP"
					+ " LEFT JOIN (SELECT vePODetailID,SUM(quantityInvoiced) AS quaninv FROM veBillHistory WHERE vePOID="+vepoID+" GROUP BY vePODetailID ) a ON a.vePODetailID = veP.vePODetailID"
					+ " WHERE veP.vePOID = "+vepoID+"";
			
			veBillDetailID = (Integer) aSession.createSQLQuery(insertInvoiceQuery).executeUpdate();
			
			aSession.createSQLQuery("insert into veBillHistory (vePOID,veBillID,vePODetailID,quantityInvoiced,invoiceAmount,entryDate,enteredBy)"
					+ " SELECT vePOID,"+veBillid+",vePODetailID,QuantityOrdered,QuantityBilled AS invoiceAmt, NOW() as entryDate,"+userID+"  FROM vePODetail  WHERE vePOID = '"+vepoID+"'").executeUpdate();
			
			
			aTransaction.commit();
			updatePrMaster(veBillid);
			
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
			insertInvoiceQuery = null;
		}
		return veBillDetailID;
		
	}

	public String updatePrMaster(Integer veBillId) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Transaction aTransaction1;
		Transaction aTransaction2;
		try {
			aTransaction2 = aVePOSession.beginTransaction();
			aTransaction2.begin();
			Vebill avebill=(Vebill) aVePOSession.get(Vebill.class,veBillId);
			//Vepo objVepo= (Vepo) aVePOSession.get(Vepo.class,objVepodetail.getVePoid());
			itsLogger.info("FreightAmount=="+avebill.getFreightAmount());
			int comp=avebill.getFreightAmount().compareTo(new BigDecimal(0));
			BigDecimal freightamount=new BigDecimal(0);
			BigDecimal eachprofreightamount=new BigDecimal(0);
			aTransaction1 = aVePOSession.beginTransaction();
			aTransaction1.begin();
			
			List<Vebilldetail> aVebilldetail= getVeBillDetails(veBillId);
			/*If comp==0 both are equal if comp==1 first value is greater if comp=-1 second value is greater*/
			if(avebill!=null && comp==1){
				int sizeVeBillDetail=aVebilldetail.size();
				System.out.println("------------"+avebill.getFreightAmount());
				BigDecimal fr_Amt=avebill.getFreightAmount();
				freightamount = new BigDecimal(fr_Amt.floatValue()/Float.parseFloat(""+sizeVeBillDetail));
				System.out.println("------------"+freightamount);
			}
			for(Vebilldetail loopVebilldetail:aVebilldetail){
				
				BigDecimal quanti_billed=loopVebilldetail.getQuantityBilled();
				
				if(quanti_billed!=null && quanti_billed.compareTo(BigDecimal.ZERO)!=0) // if quatity is zero nothing to updated as per eric request
				{
					if(freightamount.compareTo(new BigDecimal(0))>0){
						
						//if(quanti_billed!=null && quanti_billed.compareTo(BigDecimal.ZERO)!=0)
						eachprofreightamount=new BigDecimal(freightamount.floatValue()/quanti_billed.floatValue());
						/*else
						eachprofreightamount = BigDecimal.ZERO;*/
					}
					
					BigDecimal productcost=loopVebilldetail.getUnitCost().multiply((loopVebilldetail.getPriceMultiplier()==null||loopVebilldetail.getPriceMultiplier().compareTo(BigDecimal.ZERO)==0)?new BigDecimal(1):loopVebilldetail.getPriceMultiplier()).add(eachprofreightamount).setScale(2,BigDecimal.ROUND_FLOOR);
					aTransaction = aVePOSession.beginTransaction();
					aTransaction.begin();
					Prmaster objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,loopVebilldetail.getPrMasterId());
					/*Velmurugan
					 * 31-07-2015
					 * Regarding need to update only is inventory product
					 * */
					if(objPrmaster.getIsInventory()==1){
						if(objPrmaster.getAverageCost()!=null && objPrmaster.getAverageCost().compareTo(new BigDecimal(0))>0){
							productcost=productcost.add(objPrmaster.getAverageCost().setScale(2, BigDecimal.ROUND_FLOOR)).divide(new BigDecimal(2)).setScale(2, BigDecimal.ROUND_FLOOR);
							objPrmaster.setAverageCost(productcost);
						}else{
							objPrmaster.setAverageCost(productcost.setScale(2, BigDecimal.ROUND_FLOOR));
						}
					aVePOSession.update(objPrmaster);
					}
					aTransaction.commit();
				}
			}
			
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}
	
	@Override
	public String rollbackupdatePrMaster(Integer veBillId) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Transaction aTransaction1;
		Transaction aTransaction2;
		try {
			aTransaction2 = aVePOSession.beginTransaction();
			aTransaction2.begin();
			Vebill avebill=(Vebill) aVePOSession.get(Vebill.class,veBillId);
			//Vepo objVepo= (Vepo) aVePOSession.get(Vepo.class,objVepodetail.getVePoid());
			itsLogger.info("FreightAmount=="+avebill.getFreightAmount());
			int comp=avebill.getFreightAmount().compareTo(new BigDecimal(0));
			BigDecimal freightamount=new BigDecimal(0);
			BigDecimal eachprofreightamount=new BigDecimal(0);
			aTransaction1 = aVePOSession.beginTransaction();
			aTransaction1.begin();
			
			List<Vebilldetail> aVebilldetail= getVeBillDetails(veBillId);
			/*If comp==0 both are equal if comp==1 first value is greater if comp=-1 second value is greater*/
			if(avebill!=null && comp==1){
				int sizeVeBillDetail=aVebilldetail.size();
				System.out.println("------------"+avebill.getFreightAmount());
				BigDecimal fr_Amt=avebill.getFreightAmount();
				freightamount = new BigDecimal(fr_Amt.floatValue()/Float.parseFloat(""+sizeVeBillDetail));
				System.out.println("------------"+freightamount);
			}
			for(Vebilldetail loopVebilldetail:aVebilldetail){
				itsLogger.info("PriceMultiplier=="+loopVebilldetail.getPriceMultiplier());
				if(freightamount.compareTo(new BigDecimal(0))>0){
					BigDecimal quanti_billed=loopVebilldetail.getQuantityBilled();
					System.out.println("---"+quanti_billed.setScale(2, BigDecimal.ROUND_FLOOR)+"---");
					
					if(quanti_billed!=null && quanti_billed.compareTo(BigDecimal.ZERO)!=0)
					eachprofreightamount=new BigDecimal(freightamount.floatValue()/quanti_billed.floatValue());
					else
					eachprofreightamount = BigDecimal.ZERO;
				}
				
				BigDecimal productcost=loopVebilldetail.getUnitCost().multiply(loopVebilldetail.getPriceMultiplier()).add(eachprofreightamount).setScale(2,BigDecimal.ROUND_FLOOR);
				BigDecimal oldavgcost = BigDecimal.ZERO;
				aTransaction = aVePOSession.beginTransaction();
				aTransaction.begin();
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,loopVebilldetail.getPrMasterId());
				/*Velmurugan
				 * 31-07-2015
				 * Regarding need to update only is inventory product
				 * */
				if(objPrmaster.getIsInventory()==1){
				if(objPrmaster.getAverageCost()!=null && objPrmaster.getAverageCost().compareTo(new BigDecimal(0))>0){
					oldavgcost = (objPrmaster.getAverageCost().multiply(new BigDecimal(2)).setScale(2, BigDecimal.ROUND_FLOOR));
					System.out.println("====>>>"+oldavgcost.subtract(productcost));
					objPrmaster.setAverageCost(oldavgcost.subtract(productcost));
				}
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
				}
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}
	
	
	
			@Override
			public int updateVepoStatus(Integer vepoID,Integer vepostatus) throws VendorException {
//				String aJobCountStr = "SELECT COUNT(*) FROM veBillDetail WHERE veBillID IN (SELECT veBillID FROM veBill WHERE vePOID="+vepoID+")";
//				String vepoDetailcount = "SELECT COUNT(*) FROM vePODetail WHERE vePOID="+vepoID;
				Session aSession = null;
				BigInteger aTotalCount = null;
				BigInteger bTotalCount = null;
				itsLogger.info("Called updateVepoStatus");
				try {
					// Retrieve aSession from Hibernate
					aSession = itsSessionFactory.openSession();
					/*Commented By vel for partial invoice not get closed status
					 * Query aQuery = aSession.createSQLQuery(aJobCountStr);
					List<?> aList = aQuery.list();
					aTotalCount = (BigInteger) aList.get(0);
					Query bQuery = aSession.createSQLQuery(vepoDetailcount);
					List<?> bList = bQuery.list();
					bTotalCount = (BigInteger) bList.get(0);
					
					if(aTotalCount.compareTo(bTotalCount)<=0){*/
						Transaction transaction = aSession.beginTransaction();
						transaction.begin();
						Vepo aVepo = (Vepo) aSession.get(Vepo.class, vepoID);
						aVepo.setTransactionStatus(vepostatus);
						aSession.update(aVepo);
						transaction.commit();
					/*}
					*/
				} catch (Exception excep) {
					itsLogger.error(excep.getMessage(), excep);
					throw new VendorException(excep.getMessage(), excep);
				} finally {
					aSession.flush();
					aSession.close();
/*					aJobCountStr = null;
					vepoDetailcount = null;*/
				}
				return 1;
			}
	
	@Override
	public List<Vebilldetail> getVeBillDetails(Integer veBillId) throws JobException {
		String aQry = "FROM Vebilldetail WHERE veBillID=" + veBillId ;
		Session aSession = null;
		List<Vebilldetail> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query query = aSession.createQuery(aQry);
			// Retrieve all
			aQueryList = query.list();
			if(aQueryList.size() < 1){
				throw new JobException("No Product is available with given Item code and Description: \n");
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new JobException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return aQueryList;
	}
	
	public String saveVebillDetail(Vebilldetail aVebilldetail, String operation) throws VendorException
	{
		Session aSession = null;
		Transaction aTransaction = null;
		Vepodetail theVepodetail = null;
		Integer vePODetailID=null;
		Integer veBillDetailID=null;
		BigDecimal oldQuantityBilled = new BigDecimal("0.0000");
		Vebilldetail tpInvLogVebilldetail = new Vebilldetail();
		try {
			aSession = itsSessionFactory.openSession();
			if(aVebilldetail.getVeBillDetailId()!= null && aVebilldetail.getVeBillDetailId()>0){
				tpInvLogVebilldetail = (Vebilldetail) aSession.get(Vebilldetail.class, aVebilldetail.getVeBillDetailId());
			}
			if(aVebilldetail.getVeBillDetailId() != null && "edit".equalsIgnoreCase(operation))
			{
				oldQuantityBilled = tpInvLogVebilldetail.getQuantityBilled();
				vePODetailID=aVebilldetail.getVePodetailId();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Vebilldetail theVebilldetail = (Vebilldetail)aSession.get(Vebilldetail.class, aVebilldetail.getVeBillDetailId());
				if(aVebilldetail.getDescription() != null)
					theVebilldetail.setDescription(aVebilldetail.getDescription());
				if(aVebilldetail.getNote() != null)
					theVebilldetail.setNote(aVebilldetail.getNote());
				if(aVebilldetail.getQuantityBilled() != null)
					theVebilldetail.setQuantityBilled(aVebilldetail.getQuantityBilled());
				if(aVebilldetail.getPriceMultiplier() != null)
					theVebilldetail.setPriceMultiplier(aVebilldetail.getPriceMultiplier());
				if(aVebilldetail.getPrMasterId() != null)
					theVebilldetail.setPrMasterId(aVebilldetail.getPrMasterId());
				if(aVebilldetail.getVeBillId() != null)
					theVebilldetail.setVeBillId(aVebilldetail.getVeBillId());
				if(aVebilldetail.getUnitCost() != null)
					theVebilldetail.setUnitCost(aVebilldetail.getUnitCost());
				theVebilldetail.setTaxable(aVebilldetail.getTaxable());
				aSession.update(theVebilldetail);
			
//				if(oldQuantityBilled.compareTo(aVebilldetail.getQuantityBilled()) !=0){
//					Vebill aVebill=(Vebill) aSession.get(Vebill.class, aVebilldetail.getVeBillId());
//					Vepo aVepo=(Vepo) aSession.get(Vepo.class, aVebill.getVePoid());
//					TpInventoryLog theTpInventoryLog = new TpInventoryLog();
//					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
//					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVebilldetail.getPrMasterId());
//					aTpInventoryLog.setPrMasterID(aVebilldetail.getPrMasterId());
//					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
//					aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
//					aTpInventoryLog.setTransType("VI");
//					aTpInventoryLog.setTransDecription("VI RollBack");
//					aTpInventoryLog.setTransID(aVebilldetail.getVeBillId());
//					aTpInventoryLog.setTransDetailID(aVebilldetail.getVeBillDetailId());
//					aTpInventoryLog.setProductOut(oldQuantityBilled.compareTo(new BigDecimal("0.0000"))==-1?oldQuantityBilled.multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
//					aTpInventoryLog.setProductIn(oldQuantityBilled.compareTo(new BigDecimal("0.0000"))==1?oldQuantityBilled:new BigDecimal("0.0000"));
//					aTpInventoryLog.setUserID(aVebill.getCreatedById());
//					aTpInventoryLog.setCreatedOn(new Date());
//					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
//					theTpInventoryLog.setPrMasterID(aVebilldetail.getPrMasterId());
//					theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
//					theTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
//					theTpInventoryLog.setTransType("VI");
//					theTpInventoryLog.setTransDecription("VI Edited");
//					theTpInventoryLog.setTransID(aVebilldetail.getVeBillId());
//					theTpInventoryLog.setTransDetailID(aVebilldetail.getVeBillDetailId());
//					theTpInventoryLog.setProductOut(aVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?aVebilldetail.getQuantityBilled():new BigDecimal("0.0000"));
//					theTpInventoryLog.setProductIn(aVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?aVebilldetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
//					theTpInventoryLog.setUserID(aVebill.getCreatedById());
//					theTpInventoryLog.setCreatedOn(new Date());
//					itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
//				}
				aTransaction.commit();
				
				/*Eric Said No need to update inside job for onorder
				 * ID #471
				 * Velmurugan
				 * 11-12-2015
				 * */
				//updatePrMaster(aVebilldetail.getVeBillId());
			}
			else if(aVebilldetail.getVeBillDetailId() != null && "delete".equalsIgnoreCase(operation))
			{
				boolean tplog=false;
				Vepodetail rbVepodetail =null;
				Session agetSession = itsSessionFactory.openSession();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Vebilldetail rbVebilldetail = (Vebilldetail) aSession.get(Vebilldetail.class,aVebilldetail.getVeBillDetailId());
				Vebill rbVebill=(Vebill) agetSession.get(Vebill.class,aVebilldetail.getVeBillId());
				if(rbVebill.getJoReleaseDetailId()!=null && rbVebilldetail.getVePodetailId()!=null &&rbVebilldetail.getVePodetailId()>0){
					vePODetailID=rbVebilldetail.getVePodetailId();
					if(rbVebilldetail.getVePodetailId()!=null){
						/*Eric Said No need to update inside job for onorder
						 * ID #471
						 * Velmurugan
						 * 11-12-2015
						 * */
						
/*						Prmaster thePrmaster =jobService.getPrMasterBasedOnId(rbVebilldetail.getPrMasterId());
						rbVepodetail=(Vepodetail) aSession.get(Vepodetail.class,rbVebilldetail.getVePodetailId());
						if(thePrmaster.getIsInventory()==1){
						updatePrWarehouseInventoryOrdered(rbVepodetail,rbVebilldetail,"del");
						tplog=true;
						}*/
					}
					
				}
				
				
				aSession.delete(rbVebilldetail);
				aTransaction.commit();
					Vebill aVebill=(Vebill) aSession.get(Vebill.class, tpInvLogVebilldetail.getVeBillId());
					Vepo aVepo=(Vepo) aSession.get(Vepo.class, aVebill.getVePoid());
					
					
					
					if(tplog){
						TpInventoryLog aTpInventoryLog = new TpInventoryLog();
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+tpInvLogVebilldetail.getPrMasterId());
						aTpInventoryLog.setPrMasterID(tpInvLogVebilldetail.getPrMasterId());
						aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
						aTpInventoryLog.setTransType("VI");
						aTpInventoryLog.setTransDecription("VI Deleted");
						aTpInventoryLog.setTransID(tpInvLogVebilldetail.getVeBillId());
						aTpInventoryLog.setTransDetailID(tpInvLogVebilldetail.getVeBillDetailId());
						aTpInventoryLog.setProductOut(tpInvLogVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?tpInvLogVebilldetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						aTpInventoryLog.setProductIn(tpInvLogVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?tpInvLogVebilldetail.getQuantityBilled():new BigDecimal("0.0000"));
						aTpInventoryLog.setUserID(aVebill.getCreatedById());
						aTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
						
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * Rollback
						 * */
						Session tplogsession = itsSessionFactory.openSession();
						BigDecimal qb=rbVebilldetail.getQuantityBilled();
						Prwarehouse theprwarehouse=(Prwarehouse) tplogsession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
						Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
						TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
								aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
								BigDecimal.ZERO,qb,"VI",rbVebilldetail.getVeBillId(),rbVebilldetail.getVeBillDetailId(),aVebill.getInvoiceNumber(),aVepo.getPonumber() ,
				/*Product out*/(qb.compareTo(BigDecimal.ZERO)<0)?qb.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
				/*Product in*/(qb.compareTo(BigDecimal.ZERO)>0)?qb:BigDecimal.ZERO ,
								"VI Deleted",aVebilldetail.getUserId(),aVebilldetail.getUserName(),
								new Date());
						itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
						/*Code Ends*/
					}
					
			}
			else
			{
				vePODetailID=aVebilldetail.getVePodetailId();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				veBillDetailID = (Integer) aSession.save(aVebilldetail);
				aTransaction.commit();
				
				/*if(veBillDetailID>0){
				Vebill aVebill=(Vebill) aSession.get(Vebill.class, aVebilldetail.getVeBillId());
				Vepo aVepo=(Vepo) aSession.get(Vepo.class, aVebill.getVePoid());
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(aVebilldetail.getPrMasterId());
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVebilldetail.getPrMasterId());
				aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
				aTpInventoryLog.setTransType("VI");
				aTpInventoryLog.setTransDecription("VI Created");
				aTpInventoryLog.setTransID(aVebilldetail.getVeBillId());
				aTpInventoryLog.setTransDetailID(veBillDetailID);
				aTpInventoryLog.setProductOut(aVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?aVebilldetail.getQuantityBilled():new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(aVebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?aVebilldetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(aVebill.getCreatedById());
				aTpInventoryLog.setCreatedOn(new Date());
				itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
				}*/
			}
			if(vePODetailID!=null && vePODetailID>0){
				theVepodetail = new Vepodetail();
				theVepodetail.setVePodetailId(vePODetailID);
				theVepodetail.setQuantityBilled(aVebilldetail.getQuantityBilled());
				updateBilledQuantity(theVepodetail);
			}
			
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return "success";
	}
	
	@Override
	public List<Vebilldetail> getVendorInvoicelineitem(Integer theVeBillID)
			throws JobException {

		String aPOLineItemListQry = "";
		if (theVeBillID != null) {
			aPOLineItemListQry = "SELECT ve.veBillDetailID," + " ve.veBillID,ve.vePODetailID,"
					+ " ve.prMasterID," + " ve.Description,"
					+ " ve.QuantityBilled," + " ve.UnitCost,"
					+ " ve.PriceMultiplier,"
					+ " pr.ItemCode, " + " ve.Note,ve.Taxable"
					+ " FROM veBillDetail ve "
					+ " Left Join prMaster pr on ve.prMasterID = pr.prMasterID"
					+ " Right Join veBill vebill on vebill.veBillID = ve.veBillID"
					+ " where ve.veBillID = " + theVeBillID
					+ " ORDER BY ve.veBillID";
		}
		System.out.println("Vendor Invoice Line Item Query :: "+aPOLineItemListQry);
		Session aSession = null;
		ArrayList<Vebilldetail> aQueryList = new ArrayList<Vebilldetail>();
		BigDecimal aTotal;
		try {
			Vebilldetail aVebilldetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVebilldetail = new Vebilldetail();
				Object[] aObj = (Object[]) aIterator.next();
				aVebilldetail.setVeBillDetailId((Integer) aObj[0]);
				aVebilldetail.setVeBillId((Integer) aObj[1]);
				aVebilldetail.setVePodetailId((Integer) aObj[2]);
				aVebilldetail.setPrMasterId((Integer) aObj[3]);
				aVebilldetail.setDescription((String) aObj[4]);
				aVebilldetail.setQuantityOrdered((BigDecimal) aObj[5]);				
				aVebilldetail.setUnitCost((BigDecimal) aObj[6]);
				aVebilldetail.setPriceMultiplier((BigDecimal) aObj[7]);
				aVebilldetail.setPrItemCode((String) aObj[8]);
				aVebilldetail.setNote((String) aObj[9]);
				if ((Byte) aObj[10] == 1) {
					aVebilldetail.setTaxable(true);
				} else {
					aVebilldetail.setTaxable(false);
				}
				/*String notes = (String) aObj[11];
				if (null == notes || "null".equalsIgnoreCase(notes) || "".equalsIgnoreCase(notes) || "undefined".equalsIgnoreCase(notes) || notes.length() <= 0) {
					notes = "";
				}
				aVebilldetail.setNote(((String) aObj[9]));
				System.out.println(aVebilldetail.getNote());*/
				// avepoDetail.setInLineNote((String) aObj[11]);
				if(aObj[6]==null){
					aObj[6]=BigDecimal.ZERO;
				}
				BigDecimal aUnitCost = JobUtil.floorFigureoverall(((BigDecimal) aObj[6]),2);
				if(aObj[7]==null){
					aObj[7]=BigDecimal.ZERO;
				}
				BigDecimal aPriceMult = (BigDecimal) aObj[7];
				if(aObj[5]==null){
					aObj[5]=BigDecimal.ZERO;
				}
				BigDecimal aQuantity = JobUtil.floorFigureoverall(((BigDecimal) aObj[5]),2);
				if (aUnitCost != null && aPriceMult != null
						&& aQuantity != null) {
					if (Double.valueOf(aPriceMult.toString()) > Double
							.valueOf(0.0000)) {
						aTotal = aUnitCost.multiply(aPriceMult);
						aTotal = aTotal.multiply(aQuantity);
						aVebilldetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
					} else {
						aTotal = aUnitCost.multiply(aQuantity);
						aVebilldetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
					}
				} else if (aUnitCost != null && aQuantity != null) {
					aTotal = aUnitCost.multiply(aQuantity);
					aVebilldetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
				} else if (aUnitCost != null && aPriceMult != null) {
					aTotal = aUnitCost.multiply(aQuantity);
					aVebilldetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
				} else if (aUnitCost != null) {
					aVebilldetail.setQuantityBilled(aUnitCost);
				}
				/*if (aUnitCost != null && aPriceMult != null) {
					if (Double.valueOf(aPriceMult.toString()) <= Double
							.valueOf(0.0000)) {
						aVebilldetail.setNetCast(aUnitCost);
					} else {
						aNetCast = aUnitCost.multiply(aPriceMult);
						aVebilldetail.setNetCast(aNetCast);
					}
				}
				aVebilldetail.setTaxTotal((BigDecimal) aObj[10]);
				aVebilldetail.setInLineNote((String) aObj[11]);*/

				/*if (aObj[12] != null) {
					aVebilldetail.setShipDate((String) DateFormatUtils.format(
							(Date) aObj[12], "dd-MM-yyyy"));
				}
				if (aObj[13] != null) {
					aVebilldetail.setAckDate((String) DateFormatUtils.format(
							(Date) aObj[13], "dd-MM-yyyy"));
				}
				String orderNumber = (String) aObj[14];
				if (null == orderNumber || "null".equalsIgnoreCase(orderNumber) || "".equalsIgnoreCase(orderNumber) || "undefined".equalsIgnoreCase(orderNumber) || orderNumber.length() <= 0) {
					orderNumber = "";
				}
				aVebilldetail.setVendorOrderNumber(orderNumber);*/
				// avepoDetail.setNotesDesc((String) aObj[15]);
				/*
				 * avepoDetail.setPosistion((Double) aObj[8]);
				 * avepoDetail.setNote((String) aObj[9]);
				 * avepoDetail.setAcknowledgedDate((Date) aObj[12]);
				 * avepoDetail.setEstimatedShipDate((Date) aObj[13]);
				 */
				// avepoDetail.setoInLineNote((String) aObj[14]);
				aQueryList.add(aVebilldetail);

			}

		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public Vebill getVeBillObj(Integer veBillId) throws JobException {		
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Vebill aVebill = (Vebill)aSession.get(Vebill.class, veBillId);
			return aVebill;
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public Vepo getVepo(Integer vepoID) throws JobException {		
		Session aSession = null;
		Vepo aVepo = null;
		String jobName = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(vepoID != null){
				aVepo = (Vepo)aSession.get(Vepo.class, vepoID);
				System.out.println("Job Id = "+aVepo.getJoReleaseId());
				if(aVepo.getJoReleaseId() == null || aVepo.getJoReleaseId() == 1)
				{
					aVepo.setJoReleaseId(aVepo.getJoReleaseId());
					aVepo.setUpdateKey(aVepo.getTag());
				}
				else
				{
					jobName = "SELECT mas.Description FROM joMaster AS mas LEFT JOIN joRelease jo ON jo.joMasterID = mas.joMasterID WHERE jo.joReleaseID ="+ aVepo.getJoReleaseId();
					//jobSession = itsSessionFactory.openSession();
					Query aQuery1 = aSession.createSQLQuery(jobName.toString());
					String sjobname = "";
					List aList = aQuery1.list();
					if(aList.size() > 0)
						sjobname = (String)aList.get(0);
					aVepo.setJoReleaseId(aVepo.getJoReleaseId());
					aVepo.setUpdateKey(sjobname);
					
				}
			}
				
				//String poNumber = (String)aQuery.list().get(0);
				return aVepo;
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			jobName = null;
		}
	}
	@Override
	public String saveVebillDistribution(Vebilldistribution aVebilldistribution, String operation) throws VendorException
	{

		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(aVebilldistribution.getVeBillDistributionId() != null && "edit".equalsIgnoreCase(operation))
			{
				
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Vebilldistribution theVebilldistribution = (Vebilldistribution)aSession.get(Vebilldistribution.class, aVebilldistribution.getVeBillDistributionId());
				if(aVebilldistribution.getCoExpenseAccountId() != null)
					theVebilldistribution.setCoExpenseAccountId(aVebilldistribution.getCoExpenseAccountId());
				if(aVebilldistribution.getExpenseAmount() != null)
					theVebilldistribution.setExpenseAmount(aVebilldistribution.getExpenseAmount());
				if(aVebilldistribution.getJoMasterId() != null)
					theVebilldistribution.setJoMasterId(aVebilldistribution.getJoMasterId());
				if(aVebilldistribution.getVeBillDistributionId() != null)
					theVebilldistribution.setVeBillDistributionId(aVebilldistribution.getVeBillDistributionId());
				if(aVebilldistribution.getVeBillId() != null)
					theVebilldistribution.setVeBillId(aVebilldistribution.getVeBillId());
				aSession.update(theVebilldistribution);
				aTransaction.commit();
				
			
				
			}
			else if(aVebilldistribution.getVeBillDistributionId() != null && "delete".equalsIgnoreCase(operation))
			{
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.delete(aVebilldistribution);
				aTransaction.commit();
			}
			else
			{
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.save(aVebilldistribution);
				aTransaction.commit();
			}
			
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return "success";
	
	}
	@Override
	public List<Vebilldistribution> getVendorInvoiceList(Integer theVeBillID) throws JobException {

		String aPOLineItemListQry = "";
		if (theVeBillID != null) {			 
			aPOLineItemListQry = "SELECT DISTINCT ve.veBillDistributionID,  ve.veBillID,ve.coExpenseAccountID,"+
			 "ve.ExpenseAmount,  ve.joMasterID,"+
			 "co.Number,  co.Description,jo.JobNumber"+			 
			 " FROM veBillDistribution ve"+
			 " JOIN coAccount co ON co.coAccountID = ve.coExpenseAccountID"+
			 " LEFT JOIN joMaster jo ON jo.joMasterID = ve.joMasterID"+
			 " WHERE ve.veBillID ="+ theVeBillID+
			 " ORDER BY ve.veBillID";
		}
		Session aSession = null;
		ArrayList<Vebilldistribution> aQueryList = new ArrayList<Vebilldistribution>();
		if (theVeBillID != null) {
		try {
			Vebilldistribution aVebilldistribution = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVebilldistribution = new Vebilldistribution();
				Object[] aObj = (Object[]) aIterator.next();
				aVebilldistribution.setVeBillDistributionId((Integer) aObj[0]);
				aVebilldistribution.setVeBillId((Integer) aObj[1]);
				aVebilldistribution.setCoExpenseAccountId((Integer) aObj[2]);
				aVebilldistribution.setExpenseAmount((BigDecimal) aObj[3]);
				aVebilldistribution.setJoMasterId((Integer) aObj[4]);
				aVebilldistribution.setNumber((String) aObj[5]);				
				aVebilldistribution.setDesc((String) aObj[6]);
				aVebilldistribution.setJobNumber((String) aObj[7]);
				/*String notes = (String) aObj[11];
				if (null == notes || "null".equalsIgnoreCase(notes) || "".equalsIgnoreCase(notes) || "undefined".equalsIgnoreCase(notes) || notes.length() <= 0) {
					notes = "";
				}
				aVebilldetail.setNote(((String) aObj[9]));
				System.out.println(aVebilldetail.getNote());*/
				// avepoDetail.setInLineNote((String) aObj[11]);
				BigDecimal aExpenseAmt = (BigDecimal) aObj[3];
								
				aQueryList.add(aVebilldistribution);

			}

		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry= null;
		}
		}
		return aQueryList;
	}
	
	/**
	 * Created by :leo    Date:01-10-2014
	 * 
	 * Description: Delete veBillPay Details
	 * 
	 * */
	@Override
	public void removeVendorBillPaylist(Integer theVeBillID)
			throws VendorException {
		
		Session aSession = null;
		Transaction aTransaction = null;
		String aQuery= null;
		try {
			
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aQuery="delete from veBillPay where veBillID="+theVeBillID;
			aSession.createSQLQuery(aQuery).executeUpdate();
			aTransaction.commit();
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery= null;
		}
	}
	
	@Override
	public void clearAllCheckedBills(Integer theMoAccountid)
			throws VendorException {
		
		Session aSession = null;
		Transaction aTransaction = null;
		String aQuery = null;
		try {
			
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aQuery="delete from veBillPay where moAccountId="+theMoAccountid;
			aSession.createSQLQuery(aQuery).executeUpdate();
			aTransaction.commit();
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
	}
	
	
	@Override
	public boolean saveVeBillDetail(Vebilldetail avebilldetail) throws VendorException {
		Session avebilldetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		boolean returnvalue=false;
		boolean tplog=false;
		Integer veBilldetailID=0;
		Vepodetail theVepodetail = null;
		try {
			aTransaction = avebilldetailSession.beginTransaction();
			aTransaction.begin();
			veBilldetailID = (Integer) avebilldetailSession.save(avebilldetail);
			aTransaction.commit();
			if(avebilldetail.getVePodetailId()!=null && avebilldetail.getVePodetailId()>0){
				theVepodetail = new Vepodetail();
				theVepodetail.setVePodetailId(avebilldetail.getVePodetailId());
				theVepodetail.setQuantityBilled(avebilldetail.getQuantityBilled());
			updateBilledQuantity(theVepodetail);
			}
			
			itsLogger.info("veBillDetailID::"+veBilldetailID);
			if(veBilldetailID>0 ){
				returnvalue = true;
				Vebilldetail theVebilldetail = (Vebilldetail) avebilldetailSession.get(Vebilldetail.class,
						veBilldetailID);
				Vepodetail aVepodetail = (Vepodetail) avebilldetailSession.get(Vepodetail.class,
						theVebilldetail.getVePodetailId());
				if(aVepodetail!=null){
					/*Eric Said No need to update inside job for onorder
					 * ID #471
					 * Velmurugan
					 * 11-12-2015
					 * */
					/*Prmaster thePrmaster =jobService.getPrMasterBasedOnId(aVepodetail.getPrMasterId());
					if(thePrmaster.getIsInventory()==1){
					System.out.println("theVebilldetail.getprmasterID==="+theVebilldetail.getPrMasterId());
					updatePrWarehouseInventoryOrdered(aVepodetail,theVebilldetail,"add");
					tplog=true;
					}*/
				}
				if(tplog){
				Vebill aVebill=(Vebill) avebilldetailSession.get(Vebill.class, avebilldetail.getVeBillId());
				Vepo aVepo=(Vepo) avebilldetailSession.get(Vepo.class, aVebill.getVePoid());
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(avebilldetail.getPrMasterId());
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+avebilldetail.getPrMasterId());
				aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
				aTpInventoryLog.setTransType("VI");
				aTpInventoryLog.setTransDecription("VI Created");
				aTpInventoryLog.setTransID(avebilldetail.getVeBillId());
				aTpInventoryLog.setTransDetailID(veBilldetailID);
				aTpInventoryLog.setProductOut(avebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?avebilldetail.getQuantityBilled():new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(avebilldetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?avebilldetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(aVebill.getCreatedById());
				aTpInventoryLog.setCreatedOn(new Date());
				itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 * */
				BigDecimal qb=avebilldetail.getQuantityBilled();
				Prwarehouse theprwarehouse=(Prwarehouse) avebilldetailSession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
				Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
				TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
						aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
						BigDecimal.ZERO,qb.multiply(new BigDecimal(-1)),"VI",avebilldetail.getVeBillId(),avebilldetail.getVeBillDetailId(),aVebill.getInvoiceNumber(),aVepo.getPonumber() ,
		/*Product out*/(qb.compareTo(BigDecimal.ZERO)>0)?qb:BigDecimal.ZERO,
		/*Product in*/(qb.compareTo(BigDecimal.ZERO)<0)?qb.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
						"VI Created",avebilldetail.getUserId(),avebilldetail.getUserName(),
						new Date());
				itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
				/*Code Ends*/
				}
				
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(), excep);
			throw aVendorException;
		} finally {
			avebilldetailSession.flush();
			avebilldetailSession.close();
		}
		return returnvalue;
	}
	
	
	public String updatePrWarehouseInventoryOrdered(Vepodetail theVepodetail,Vebilldetail aVebilldetail,
			String oper) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Prmaster aPrmaster = null;
		String sQuery,sQuery1 = null;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Prwarehouseinventory thePrwarehouseinventory = null;
			Integer sPrWarehouseID = 0;
			BigDecimal onOrder = new BigDecimal(0);
			Integer prWarehouseInventoryID = 0;
			sQuery = "SELECT prWarehouseID FROM vePO vepo WHERE vePOID = "
					+ theVepodetail.getVePoid();
			Query query = aVePOSession.createSQLQuery(sQuery);
			
			List listQuery1= query.list();
			if (listQuery1.size() > 0)
				sPrWarehouseID = (Integer) listQuery1.get(0);

			sQuery1 = "SELECT inventory.InventoryOnOrder,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
					+ theVepodetail.getPrMasterId()
					+ " AND prWarehouseID = "
					+ sPrWarehouseID;
			 query = aVePOSession.createSQLQuery(sQuery1);
			/*
			 * Iterator<?> iterator = query.list().iterator();
			 * while(iterator.hasNext()) { Object[] object = (Object[])
			 * iterator.next(); onOrder.add((BigDecimal)object[0]); }
			 */
			List listQuery2= query.list();
			if (listQuery2.size() > 0) {
				Object[] object = (Object[]) listQuery2.get(0);
				onOrder = (BigDecimal) object[0];
				prWarehouseInventoryID = (Integer) object[1];
			}else{
				thePrwarehouseinventory = new Prwarehouseinventory();
				thePrwarehouseinventory.setPrWarehouseId(sPrWarehouseID);
				thePrwarehouseinventory.setPrMasterId(theVepodetail.getPrMasterId());
				thePrwarehouseinventory.setHasInitialCost(new Byte("0"));
				thePrwarehouseinventory.setInventoryOnOrder(new BigDecimal("0.0000"));
				thePrwarehouseinventory.setInventoryOnHand(new BigDecimal("0.0000"));
				thePrwarehouseinventory.setInventoryAllocated(new BigDecimal("0.0000"));
				thePrwarehouseinventory.setBin("");
				prWarehouseInventoryID = (Integer)aVePOSession.save(thePrwarehouseinventory);
			}
			System.out.println();
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class, prWarehouseInventoryID);
			
			/*Vepodetail bVepodetail = (Vepodetail) aVePOSession
					.get(Vepodetail.class, theVepodetail.getVePodetailId());
			bVepodetail.setQuantityOrdered(bVepodetail.getQuantityOrdered().subtract(aVebilldetail.getQuantityBilled()));
			aVePOSession.update(bVepodetail);
			aTransaction.commit();*/
			/*Vepodetail objVepodetail = (Vepodetail) aVePOSession
					.get(Vepodetail.class, theVepodetail.getVePodetailId());
			
			List<Vepodetail> bQueryList = new ArrayList<Vepodetail>();
			Query queryz = aVePOSession
					.createSQLQuery("SELECT vePODetailID,vePOID,prMasterID,Description,Note,QuantityOrdered FROM  vePODetail WHERE vePOID IN (SELECT vePOID FROM vePO WHERE prWarehouseID = "+sPrWarehouseID+") AND prMasterID="+aVebilldetail.getPrMasterId());
			Vepodetail cVepodetail = null;
				BigDecimal qtyOrdered = new BigDecimal("0.0000");
				Iterator<?> aIterator = queryz.list().iterator();
				while (aIterator.hasNext()) {
					cVepodetail = new Vepodetail();
					Object[] aObj = (Object[]) aIterator.next();
					cVepodetail.setVePodetailId((Integer) aObj[0]);
					cVepodetail.setVePoid((Integer) aObj[1]);
					cVepodetail.setPrMasterId((Integer) aObj[2]);
					cVepodetail.setDescription((String) aObj[3]);
					cVepodetail.setNote((String) aObj[4]);
					cVepodetail.setQuantityOrdered((BigDecimal) aObj[5]);	
					qtyOrdered = qtyOrdered.add((BigDecimal) aObj[5]);
					bQueryList.add(cVepodetail);

				}*/
			/*	if(bQueryList.size()>0){
					for(int i=0;i<bQueryList.size();i++){
						qtyOrdered = qtyOrdered.add(bQueryList.get(i).getQuantityOrdered()==null?new BigDecimal("0.0000"):bQueryList.get(i).getQuantityOrdered());
					}
				}*/
			
			if (objPrwarehouseinventory != null) {
				aTransaction = aVePOSession.beginTransaction();
				aTransaction.begin();
				BigDecimal order = objPrwarehouseinventory
						.getInventoryOnOrder();
				if ("del".equalsIgnoreCase(oper)){
					//inventoryorder+newQuantity
					objPrwarehouseinventory.setInventoryOnOrder((objPrwarehouseinventory.getInventoryOnOrder()==null?new BigDecimal("0.0000"):objPrwarehouseinventory.getInventoryOnOrder()).add(
							aVebilldetail.getQuantityBilled()==null?new BigDecimal("0.0000"):aVebilldetail.getQuantityBilled()));
				}else if ("add".equalsIgnoreCase(oper)){
					//inventoryorder-newQuantity
					objPrwarehouseinventory.setInventoryOnOrder((objPrwarehouseinventory.getInventoryOnOrder()==null?new BigDecimal("0.0000"):objPrwarehouseinventory.getInventoryOnOrder()).subtract(
							aVebilldetail.getQuantityBilled()==null?new BigDecimal("0.0000"):aVebilldetail.getQuantityBilled()));
				}else if ("edit".equalsIgnoreCase(oper)){
					//inventoryorder+oldQuantity-newQuantity
					Vebilldetail theVebilldetail=(Vebilldetail)aVePOSession.get(Vebilldetail.class, aVebilldetail.getVeBillDetailId());
					objPrwarehouseinventory.setInventoryOnOrder((objPrwarehouseinventory.getInventoryOnOrder()==null?new BigDecimal("0.0000"):objPrwarehouseinventory.getInventoryOnOrder()).add(
							theVebilldetail.getQuantityBilled()==null?new BigDecimal("0.0000"):theVebilldetail.getQuantityBilled()).subtract(
							aVebilldetail.getQuantityBilled()==null?new BigDecimal("0.0000"):aVebilldetail.getQuantityBilled()));
				}
				aVePOSession.update(objPrwarehouseinventory);
				aTransaction.commit();
				List<Prwarehouseinventory> aQueryList = null;
					Query querys = aVePOSession
							.createQuery("FROM  Prwarehouseinventory WHERE prMasterID="+aVebilldetail.getPrMasterId());
						aQueryList = querys.list();
				BigDecimal quantityOrdered = new BigDecimal("0.0000");
				if(aQueryList.size()>0){
					for(int j=0;j<aQueryList.size();j++){
						quantityOrdered = quantityOrdered.add(aQueryList.get(j).getInventoryOnOrder()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnOrder());
					}
					aTransaction = aVePOSession.beginTransaction();
					aTransaction.begin();
					 aPrmaster = (Prmaster) aVePOSession
							.get(Prmaster.class, aVebilldetail.getPrMasterId());
					aPrmaster.setInventoryOnOrder(quantityOrdered);
					aVePOSession.update(aPrmaster);
					aTransaction.commit();
				}
				aTransaction = aVePOSession.beginTransaction();
				aTransaction.begin();
				Vepo aVepo = (Vepo) aVePOSession.get(Vepo.class,
						theVepodetail.getVePoid());
				aVepo.setreceiveddate(new Date());
				aVePOSession.update(aVepo);
				aTransaction.commit();
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
			sQuery = null;
			sQuery1 = null;
		}
		return "Success";
	}
	@Override
	public BigDecimal getVendorBillDetails(String duration,String rxMasterID) throws VendorException {
		 
		String aVendorSelectQry = "";
		if(duration=="current"){
			aVendorSelectQry="SELECT IF(SUM(BillAmount) IS NULL,0.00,SUM(BillAmount)) AS CurrentData FROM veBill WHERE rxMasterID = "+rxMasterID+"  AND DueDate >= DATE_SUB(CURDATE(),INTERVAL 30 DAY) AND NOW() AND TransactionStatus=1";
		}
		if(duration=="30"){
			aVendorSelectQry="SELECT IF(SUM(BillAmount) IS NULL,0.00,SUM(BillAmount)) AS 30DaysData FROM veBill WHERE rxMasterID = "+rxMasterID+"  AND DueDate BETWEEN  DATE_SUB(CURDATE(),INTERVAL 60 DAY) AND DATE_SUB(CURDATE(),INTERVAL 30 DAY)  AND TransactionStatus=1";
		}
		if(duration=="60"){
			aVendorSelectQry="SELECT IF(SUM(BillAmount) IS NULL,0.00,SUM(BillAmount)) AS 60DaysData FROM veBill WHERE rxMasterID = "+rxMasterID+"  AND DueDate BETWEEN  DATE_SUB(CURDATE(),INTERVAL 90 DAY) AND DATE_SUB(CURDATE(),INTERVAL 60 DAY)  AND TransactionStatus=1";
		}
		if(duration=="90"){
			aVendorSelectQry="SELECT IF(SUM(BillAmount) IS NULL,0.00,SUM(BillAmount)) AS 90DaysData FROM veBill WHERE rxMasterID = "+rxMasterID+"  AND DueDate < DATE_SUB(CURDATE(),INTERVAL 90 DAY)  AND TransactionStatus=1";
		}
		
		Session aSession = itsSessionFactory.openSession();
		BigDecimal count = null;
		try{
			List<?> aList = aSession.createSQLQuery(aVendorSelectQry).list();
			count = (BigDecimal) aList.get(0);
			itsLogger.info(count);
		} catch (Exception e){
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorSelectQry = null;
		}
		return count;
	}
	
	public List<Vepodetail> getreorderclicklineitems(Integer VendorID,Integer WareHouseId,Integer vePOID) {
		Session aSession = null;
		List<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		String query= null;
		try {
			aSession = itsSessionFactory.openSession();
			/*String query="SELECT a.prMasterID,a.ItemCode,a.Description,(b.InventoryOrderPoint+b.InventoryOrderQuantity) AS suggestedOrder,a.LastCost,a.POMult, rx.Name,rx.rxMasterID AS vName "
					    +" FROM prMaster a LEFT JOIN rxMaster rx ON rx.rxMasterID = a.rxMasterIDPrimaryVendor LEFT JOIN prOrderPoint b ON a.prMasterID =b.prMasterID LEFT JOIN prDepartment c "
					    +" ON a.prDepartmentID = c.prDepartmentID LEFT JOIN prCategory d ON a.prCategoryID=d.prCategoryID WHERE b.prWarehouseID="+WareHouseId+" AND rx.rxMasterID="+VendorID;
			*/
			query="SELECT P.prMasterID,P.ItemCode,P.Description,"
						//+ "IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF((O.InventoryOrderPoint+O.InventoryOrderQuantity)>60 &&(W.InventoryOnHand>60 || W.InventoryOnOrder>60),0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) )) AS suggestedOrder ,"
						//+"IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF(W.InventoryOnHand>O.InventoryOrderPoint || W.InventoryOnOrder>O.InventoryOrderPoint,0,(O.InventoryOrderPoint+O.InventoryOrderQuantity))) AS suggestedOrder, "
						+ "P.LastCost,P.POMult, rx.Name,rx.rxMasterID AS vendorid, "
						+"IF(W.InventoryOnOrder IS NULL,0,W.InventoryOnOrder) AS InventoryOnOrder,"
						+"IF(W.InventoryOnHand IS NULL,0,W.InventoryOnHand) AS InventoryOnHand,"
						+"IF(W.InventoryAllocated IS NULL,0,W.InventoryAllocated) AS InventoryAllocated,"
						+"IF(O.InventoryOrderPoint IS NULL,0,O.InventoryOrderPoint) AS InventoryOrderPoint "
						+"FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+WareHouseId+")  AS W ON P.prMasterID = W.prMasterID) LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+WareHouseId+")  AS O  ON P.prMasterID = O.prMasterID LEFT JOIN rxMaster rx ON rx.rxMasterID = P.rxMasterIDPrimaryVendor LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID  "
						+"LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID LEFT JOIN prWarehouse prw ON prw.prWarehouseID="+WareHouseId+" WHERE P.IsInventory=1 AND P.InActive=0 AND "
						+"IFNULL((W.InventoryOnHand-W.InventoryAllocated),0)<IFNULL(O.InventoryOrderPoint,0) AND IFNULL(W.InventoryOnOrder,0)<IFNULL(O.InventoryOrderPoint,0 ) AND "
						+ "(IFNULL(O.InventoryOrderPoint,0)-(IFNULL(W.InventoryOnOrder, 0)+(IFNULL(W.InventoryOnHand,0)-IFNULL(W.InventoryAllocated,0))))>0 "
						
						 
						 // + "IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF((O.InventoryOrderPoint+O.InventoryOrderQuantity)>60 &&(W.InventoryOnHand>60 || W.InventoryOnOrder>60),0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) ))>0 "
						//+"IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF(W.InventoryOnHand>O.InventoryOrderPoint || W.InventoryOnOrder>O.InventoryOrderPoint,0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) ))>0  "
						+ "AND rx.rxMasterID="+VendorID;
			Query aQuery = aSession.createSQLQuery(query);
			Iterator<?> aIterator = aQuery.list().iterator();
			
			
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				Vepodetail vepolist=new Vepodetail();
				//vepolist.setNote((String) aObj[1]);
				vepolist.setDescription((String) aObj[2]);
				BigDecimal unitcost=new BigDecimal(0);
				if(aObj[3]!=null){
					unitcost=(BigDecimal) aObj[3];
				}
				vepolist.setUnitCost(unitcost);
				BigDecimal PriceMultiplier=new BigDecimal(0);
				if(aObj[4]!=null){
					PriceMultiplier=(BigDecimal) aObj[4];
				}
				vepolist.setPriceMultiplier(PriceMultiplier);
				
				
				BigDecimal QuantityOrdered=new BigDecimal(0);
				BigDecimal inventoryonorder=BigDecimal.ZERO;
				BigDecimal inventoryonhand=BigDecimal.ZERO;
				BigDecimal inventoryorderpoint=BigDecimal.ZERO;
				BigDecimal inventoryallocated=BigDecimal.ZERO;
				inventoryonorder=(BigDecimal)aObj[7];
				inventoryonhand=(BigDecimal)aObj[8];
				inventoryallocated=(BigDecimal)aObj[9];
				inventoryorderpoint=(BigDecimal)aObj[10];
				BigDecimal InventoryProjected=inventoryonorder.add(inventoryonhand.subtract(inventoryallocated));
				//Suggested Order
				QuantityOrdered=inventoryorderpoint.subtract(InventoryProjected);

				vepolist.setQuantityOrdered(QuantityOrdered);
				vepolist.setQuantityBilled(unitcost.multiply(PriceMultiplier).multiply(QuantityOrdered));
				vepolist.setQuantityReceived(new BigDecimal(0));
				vepolist.setVePoid(vePOID);
				vepolist.setPrMasterId((Integer) aObj[0]);
				vepolist.setTaxable(false);
				aQueryList.add(vepolist);

			}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aQueryList;
	}
	
	public Boolean UpdateVendorFinancialTab(Vemaster theVemaster){
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			Vemaster aVemaster = (Vemaster) aSession.get(Vemaster.class, theVemaster.getVeMasterId());
			aVemaster.setDueDays(theVemaster.getDueDays());
			aVemaster.setDueOnDay(theVemaster.isDueOnDay());
			aVemaster.setDiscountPercent(theVemaster.getDiscountPercent());
			aVemaster.setDiscountDays(theVemaster.getDiscountDays());
			aVemaster.setDiscOnDay(theVemaster.isDiscOnDay());
			aVemaster.setDiscountIncludesFreight(theVemaster.isDiscountIncludesFreight());
			aVemaster.setManufacturer(theVemaster.getManufacturer());
			aVemaster.setCoExpenseAccountId(theVemaster.getCoExpenseAccountId());
			aVemaster.setImportType(theVemaster.getImportType());
			aVemaster.setAccountNumber(theVemaster.getAccountNumber());
			aSession.update(aVemaster);
			transaction.commit();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	@Override
	public Integer createManufactures(VeFactory theVeFactory) throws VendorException {
		itsLogger.debug("Query has to get jobNumber and date");
		Session theVeFactorySession = itsSessionFactory.openSession();
		Transaction theVeFactoryTransaction;
		Integer areturnValue = null;
		try {
			theVeFactoryTransaction = theVeFactorySession.beginTransaction();
			theVeFactoryTransaction.begin();
			areturnValue=(Integer) theVeFactorySession.save(theVeFactory);
			theVeFactoryTransaction.commit();
		} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				throw new VendorException(excep.getMessage(), excep);
		} finally {
			theVeFactorySession.flush();
			theVeFactorySession.close();
		}
		return areturnValue;
	}

	@Override
	public Boolean checkInvoiceNumberExist(String InvNo,Integer vePOID)throws VendorException  {
		
		Session theVeFactorySession = itsSessionFactory.openSession();
		String areturnValue = "";
		boolean statusReturn = false;
		List<?> lisQuery = null;
		String invNo = "Select InvoiceNumber from veBill where InvoiceNumber='"+InvNo+"' and vePOID="+vePOID;
		
		try {
			lisQuery = theVeFactorySession.createSQLQuery(invNo).list();
			if(lisQuery.size()>0)
			{
			areturnValue=(String)lisQuery.get(0);
			if(areturnValue!=null)
				statusReturn = true;
			}
			
		} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				throw new VendorException(excep.getMessage(), excep);
		} finally {
			theVeFactorySession.flush();
			theVeFactorySession.close();
			invNo = null;
		}
		return statusReturn;
	}
	@Override
	public List<VeBillPaymentHistory> getveBillPaymentHistory(Integer veBillid)
			throws VendorException {
		
		Session aSession = null;
		List<VeBillPaymentHistory> aQueryList = null;
		Query query = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	query = aSession.createQuery("FROM  VeBillPaymentHistory WHERE veBillID = "+veBillid);
			// Retrieve all
		 		aQueryList = (List<VeBillPaymentHistory>)query.list();
		 		
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		}finally {
			aSession.flush();
			aSession.close();
			query = null;
			
		}
		return  aQueryList; 
	}
	
	@Override
	public VendorBillsBean getVeBillListOutSide(Integer vebillId) throws VendorException{
		VendorBillsBean aVendorBillsBean = null;
		Session aSession = itsSessionFactory.openSession();
		String aVendorBillsListQry = "SELECT veBill.veBillID,veBill.vePOID,vePO.PONumber,jm.joMasterID,jm.Description,jm.JobNumber,rm.Name,veBill.joReleaseDetailID,veBill.TransactionStatus,jm.JobStatus,rm.rxMasterID " 
									 +"FROM veBill LEFT OUTER JOIN vePO ON veBill.vePOID = vePO.vePOID "
									 +"LEFT OUTER JOIN joReleaseDetail jd ON(jd.joReleaseDetailID=veBill.joReleaseDetailID) "
									 +"LEFT OUTER JOIN joRelease jr ON(jr.joReleaseID=jd.joReleaseID) "
									 +"LEFT OUTER JOIN joMaster jm ON(jm.joMasterID=jr.joMasterID) "
									 +"LEFT OUTER JOIN rxMaster rm ON veBill.rxMasterID = rm.rxMasterID "
									 +"WHERE veBill.veBillID="+vebillId;
		try {
			Query aQuery = aSession.createSQLQuery(aVendorBillsListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				aVendorBillsBean = new VendorBillsBean();
				Object[] aObj = (Object[])aIterator.next();
				aVendorBillsBean.setVeBillId((Integer) aObj[0]);
				aVendorBillsBean.setVePoid((Integer) aObj[1]);
				aVendorBillsBean.setPONumber((String) aObj[2]);
				aVendorBillsBean.setJobName((String) aObj[4]);
				aVendorBillsBean.setJobNumber((String) aObj[5]);
				aVendorBillsBean.setCustomerName((String) aObj[6]);
				if(aObj[7]!=null){
					aVendorBillsBean.setJoreleasedetailid((Integer) aObj[7]);
				}else{
					aVendorBillsBean.setJoreleasedetailid(0);
				}
				aVendorBillsBean.setTransaction_status(aObj[8]!=null?JobUtil.ConvertintoInteger(aObj[8].toString()):new Integer(-1));
				aVendorBillsBean.setJobStatus(aObj[9]!=null?JobUtil.ConvertintoInteger(aObj[9].toString()):0);
				aVendorBillsBean.setRxMasterId((Integer) aObj[10]);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aVendorBillsListQry = null;
		}
		return aVendorBillsBean;
	}

	@Override
	public Boolean getPoTotalequalsvendorinvoice(Integer VepoID) {
				String aSummaryQry = "SELECT SUM(BillAmount-TaxAmount-FreightAmount),vePO.subtotal FROM veBill LEFT JOIN vePO ON(veBill.vePOID=vePO.vePOID) WHERE vePO.vePOID=" + VepoID ;
				Session aSession = null;
				Boolean retunValue=false;
				try {
					aSession = itsSessionFactory.openSession();
					Query aQuery = aSession.createSQLQuery(aSummaryQry);
					Iterator<?> aIterator = aQuery.list().iterator();
					if (aIterator!=null && aIterator.hasNext()) {
						Object[] aObj = (Object[]) aIterator.next();
						BigDecimal vebillAmount=(BigDecimal)aObj[0];
						BigDecimal vepoAmount=(BigDecimal)aObj[1];
						vepoAmount = vepoAmount==null?new BigDecimal("0.0000"):vepoAmount;
						vebillAmount = vebillAmount==null?new BigDecimal("0.0000"):vebillAmount;
						if((vebillAmount.compareTo(vepoAmount)==0)||(vebillAmount.compareTo(vepoAmount)==-1)){
							retunValue=true;
						}
					}
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				} finally {
					aSession.flush();
					aSession.close();
					aSummaryQry = null;
				}
				return retunValue;
			}
	
	@Override
	public int getCount(String query) throws VendorException {
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(query);
			List<?> aList = aQuery.list();
			if(aList!=null && aList.size()>0){
			    aTotalCount = new BigInteger(String.valueOf(aList.size())) ;
			}else{
				aTotalCount=new BigInteger("0");
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aTotalCount.intValue();
	}
	
	
	@Override
	public Boolean getCheckinvoiceNumberavlforvendor(Integer VendorID,String invnumber) {
				String aSummaryQry = "SELECT veBillID FROM veBill WHERE rxMasterID="+VendorID+" and InvoiceNumber='"+invnumber+"'" ;
				Session aSession = null;
				Boolean retunValue=false;
				try {
					aSession = itsSessionFactory.openSession();
					Query aQuery = aSession.createSQLQuery(aSummaryQry);
					List lst=aQuery.list();
					if(lst!=null && lst.size()>0){
						retunValue=true;
					}
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				} finally {
					aSession.flush();
					aSession.close();
					aSummaryQry = null;
				}
				return retunValue;
			}
	
	@Override
	public Integer insertVebillHistory(Integer veBillid, Integer vepoID,Integer userID) throws VendorException
	{
		Session aSession = null;
		Transaction aTransaction = null;
		String insertInvoiceQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			aSession.createSQLQuery("insert into veBillHistory (vePOID,veBillID,vePODetailID,quantityInvoiced,invoiceAmount,entryDate,enteredBy)"
					+ " SELECT vePOID,"+veBillid+",vePODetailID,QuantityOrdered,QuantityBilled AS invoiceAmt, NOW() as entryDate,"+userID+"  FROM vePODetail  WHERE vePOID = '"+vepoID+"'").executeUpdate();
			aTransaction.commit();
			updatePrMaster(veBillid);
			
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
			insertInvoiceQuery = null;
		}
		return 0;
		
	}
	
	public boolean updateBilledQuantity(Vepodetail aVepodetail) throws VendorException{
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Vepodetail theVepodetail = null;
		BigDecimal billedQuantity = new BigDecimal(0);
		try
		{
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			String aPOLineItemListQry = "";
			aPOLineItemListQry = "SELECT SUM(ve.QuantityBilled)  FROM veBillDetail ve WHERE ve.vePODetailID ="+aVepodetail.getVePodetailId()+" GROUP BY ve.vePODetailID;";
			List<?> lisQuery = null;
			lisQuery = aSession.createSQLQuery(aPOLineItemListQry).list();
			if(lisQuery.size()>0)
			{
				billedQuantity=(BigDecimal)lisQuery.get(0);
			}
			
			itsLogger.info("Billed Quantity::"+billedQuantity);
			theVepodetail = (Vepodetail) aSession.get(Vepodetail.class, aVepodetail.getVePodetailId());
			theVepodetail.setQuantityBilled(billedQuantity);
			aSession.update(theVepodetail);
			aTransaction.commit();
		}catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new VendorException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	@Override
	public Boolean getChecklineitemisthereforrelease(Integer type,Integer primaryID) throws VendorException{
		
		Session aSession = null;
		Transaction aTransaction = null;
		String selectQuery = null;
		List<?> valueID = null;
		Boolean result = false;
		try {
			aSession = itsSessionFactory.openSession();
			if(type!=3)
			{
				if(type == 2 || type == 5)
				{
					selectQuery = "select cuSODetailID from cuSODetail where cuSOID="+primaryID;
				}
				else
				{
					selectQuery = "select vePODetailID from vePODetail where vePOID="+primaryID;
				}
				
				valueID = (List<?>) aSession.createSQLQuery(selectQuery).list();
				
				if(valueID.size()>0)
					result = true;
				else
					result = false;
			}
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
			selectQuery = null;
		}
		return result;
	}
	@Override
	public List<Vepodetail> getreorderclicklineitems(Integer VendorID,Integer WareHouseId) {
		Session aSession = null;
		List<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		String query= null;
		try {
			aSession = itsSessionFactory.openSession();
			/*String query="SELECT a.prMasterID,a.ItemCode,a.Description,(b.InventoryOrderPoint+b.InventoryOrderQuantity) AS suggestedOrder,a.LastCost,a.POMult, rx.Name,rx.rxMasterID AS vName "
					    +" FROM prMaster a LEFT JOIN rxMaster rx ON rx.rxMasterID = a.rxMasterIDPrimaryVendor LEFT JOIN prOrderPoint b ON a.prMasterID =b.prMasterID LEFT JOIN prDepartment c "
					    +" ON a.prDepartmentID = c.prDepartmentID LEFT JOIN prCategory d ON a.prCategoryID=d.prCategoryID WHERE b.prWarehouseID="+WareHouseId+" AND rx.rxMasterID="+VendorID;
			*/
			query="SELECT P.prMasterID,P.ItemCode,P.Description,"
						//+ "IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF((O.InventoryOrderPoint+O.InventoryOrderQuantity)>60 &&(W.InventoryOnHand>60 || W.InventoryOnOrder>60),0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) )) AS suggestedOrder ,"
						//+"IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF(W.InventoryOnHand>O.InventoryOrderPoint || W.InventoryOnOrder>O.InventoryOrderPoint,0,(O.InventoryOrderPoint+O.InventoryOrderQuantity))) AS suggestedOrder, "
						+ "P.LastCost,P.POMult, rx.Name,rx.rxMasterID AS vendorid, "
						+"IF(W.InventoryOnOrder IS NULL,0,W.InventoryOnOrder) AS InventoryOnOrder,"
						+"IF(W.InventoryOnHand IS NULL,0,W.InventoryOnHand) AS InventoryOnHand,"
						+"IF(W.InventoryAllocated IS NULL,0,W.InventoryAllocated) AS InventoryAllocated,"
						+"IF(O.InventoryOrderPoint IS NULL,0,O.InventoryOrderPoint) AS InventoryOrderPoint "
						+"FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+WareHouseId+")  AS W ON P.prMasterID = W.prMasterID) LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+WareHouseId+")  AS O  ON P.prMasterID = O.prMasterID LEFT JOIN rxMaster rx ON rx.rxMasterID = P.rxMasterIDPrimaryVendor LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID  "
						+"LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID LEFT JOIN prWarehouse prw ON prw.prWarehouseID="+WareHouseId+" WHERE P.IsInventory=1 AND P.InActive=0 AND "
						//+"IFNULL((W.InventoryOnHand-W.InventoryAllocated),0)<IFNULL(O.InventoryOrderPoint,0) AND IFNULL(W.InventoryOnOrder,0)<IFNULL(O.InventoryOrderPoint,0 ) AND "
						+ "(IFNULL(O.InventoryOrderPoint,0)-(IFNULL(W.InventoryOnOrder, 0)+(IFNULL(W.InventoryOnHand,0)-IFNULL(W.InventoryAllocated,0))))>0 "
						
						 
						 // + "IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF((O.InventoryOrderPoint+O.InventoryOrderQuantity)>60 &&(W.InventoryOnHand>60 || W.InventoryOnOrder>60),0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) ))>0 "
						//+"IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF(W.InventoryOnHand>O.InventoryOrderPoint || W.InventoryOnOrder>O.InventoryOrderPoint,0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) ))>0  "
						+ "AND rx.rxMasterID="+VendorID;
			Query aQuery = aSession.createSQLQuery(query);
			Iterator<?> aIterator = aQuery.list().iterator();
			
			
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				Vepodetail vepolist=new Vepodetail();
				vepolist.setNote((String) aObj[1]);
				vepolist.setDescription((String) aObj[2]);
				BigDecimal unitcost=new BigDecimal(0);
				if(aObj[3]!=null){
					unitcost=(BigDecimal) aObj[3];
				}
				vepolist.setUnitCost(unitcost);
				BigDecimal PriceMultiplier=new BigDecimal(0);
				if(aObj[4]!=null){
					PriceMultiplier=(BigDecimal) aObj[4];
				}
				vepolist.setPriceMultiplier(PriceMultiplier);
				
				
				BigDecimal QuantityOrdered=new BigDecimal(0);
				BigDecimal inventoryonorder=BigDecimal.ZERO;
				BigDecimal inventoryonhand=BigDecimal.ZERO;
				BigDecimal inventoryorderpoint=BigDecimal.ZERO;
				BigDecimal inventoryallocated=BigDecimal.ZERO;
				inventoryonorder=(BigDecimal)aObj[7];
				inventoryonhand=(BigDecimal)aObj[8];
				inventoryallocated=(BigDecimal)aObj[9];
				inventoryorderpoint=(BigDecimal)aObj[10];
				BigDecimal InventoryProjected=inventoryonorder.add(inventoryonhand.subtract(inventoryallocated));
				//Suggested Order
				QuantityOrdered=inventoryorderpoint.subtract(InventoryProjected);

				vepolist.setQuantityOrdered(QuantityOrdered);
				vepolist.setQuantityBilled(QuantityOrdered);
				vepolist.setQuantityReceived(new BigDecimal(0));
				//vepolist.setVePoid(vePOID);
				vepolist.setPrMasterId((Integer) aObj[0]);
				vepolist.setTaxable(false);
				aQueryList.add(vepolist);

			}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aQueryList;
	}

	@Override
	public  List<Vereceive> getveReceiveDetails(Integer vepoID) throws VendorException {
		
		String aQry = "FROM Vereceive WHERE vePOID="+vepoID+" order by receiveDate desc" ;
		Session aSession = null;
		List<Vereceive> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createQuery(aQry);
			aQueryList = query.list();
			if(aQueryList.size() < 1){
				throw new VendorException("No Product is available with given Item code and Description: \n");
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return aQueryList;
	}

	@Override
	public Integer changeTransactionStatus(Integer veBillId) {
		String aQry = "UPDATE veBill SET TransactionStatus=1 WHERE veBillID=?";
		Session aSession = null;
		Integer status=0;
	
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aQry);
			
			query.setInteger(0, veBillId);
			status=query.executeUpdate();
		
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return status;
	}
}
