/**
 * This Inventory Service Implementation is Carried out of Inventory Page Details. 
 * @author Madhan Kumar
 *
 */

package com.turborep.turbotracker.Inventory.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLog;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLogMaster;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.PrCategory;
import com.turborep.turbotracker.product.dao.PrDepartment;
import com.turborep.turbotracker.product.dao.PrDepartmentAccounts;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.SalesOrderBean;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.exception.VendorException;


@Transactional
@Service("inventoryService")
public class InventoryServiceImpl implements InventoryService {

	protected static Logger itsLogger = Logger.getLogger("services");

	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;

	@Resource(name = "gltransactionService")
	private GltransactionService callgltransaction;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Override
	public int getRecordCount() throws InventoryException {
		String aJobCountStr = "SELECT COUNT(prMasterID) AS count FROM prMaster";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr =null;
		}

		return aTotalCount.intValue();
	}

	@Override
	public List<?> getInventory(int theFrom, int theTo,Integer warehouseId, String theSearchString, Integer inactive,String column,String sortBy) throws InventoryException {
		Session aSession = null;
		List<Prmaster> aQueryList = new ArrayList<Prmaster>();
		
		/*String aStr = "SELECT " +
				"prMaster.prMasterID, " +
				"prMaster.ItemCode, " +
				"prMaster.Description, " +
				"prMaster.InventoryOnHand, " +
				"prMaster.InventoryAllocated, " +
				"prMaster.InventoryOnOrder, " +
				"prMaster.Submitted  ";*/
		String itemcodeCondn="";
		/*if(itemCode != null)
			itemcodeCondn=" OR pr.ItemCode LIKE '%"+itemCode+"%'";*/
		/*itemcodeCondn = "AND (pr.Description LIKE '%"+theSearchString+"%' OR" +
				" pr.ItemCode LIKE '%"+theSearchString+"%' OR" +
				" pc.Description LIKE '%"+theSearchString+"%' OR" +
				" rx.Name LIKE '%"+theSearchString+"%' OR"
				+" pw.InventoryOnHand LIKE'%"+theSearchString+"%') ";*/
	//	if(itemcodeCondn.length()>0){
			itemcodeCondn=" AND ( pr.Description LIKE '%"+theSearchString+"%' OR" +
					" pr.ItemCode LIKE '%"+theSearchString+"%' OR" +
					" pc.Description LIKE '%"+theSearchString+"%' OR" +
					" pd.Description LIKE '%"+theSearchString+"%' OR" +
					" rx.Name LIKE '%"+theSearchString+"%') ";
	//	}
		String inactiveCondn = "",warehouseCondn ="",sortByOption ="";
		if(inactive==null || inactive==0){
			inactiveCondn ="pr.InActive=0 ";
		}else{
			inactiveCondn ="pr.InActive=1 ";
		}
		if(warehouseId==null || warehouseId==-1){
			warehouseCondn = "";
		}else{
			warehouseCondn = " pw.prWarehouseID = "+warehouseId+" AND ";
		}
		
		if(theSearchString == null)
			theSearchString = "";
		//	aStr = aStr+" FROM prMaster WHERE Description NOT LIKE '%DO NOT %' AND ItemCode LIKE '%"+itemCode+"%' ORDER BY prMaster.ItemCode LIMIT " + theFrom + ", " + theTo;
		//else
			//aStr = aStr+" FROM prMaster WHERE Description NOT LIKE '%DO NOT %' ORDER BY prMaster.ItemCode LIMIT " + theFrom + ", " + theTo;
		
		if(column.equalsIgnoreCase("description")){
			sortByOption=" pr.Description ";
		}else if(column.equalsIgnoreCase("prDepartment")){
			sortByOption=" pd.Description ";
		}else if(column.equalsIgnoreCase("prCategory")){
			sortByOption=" pc.Description ";
		}else if(column.equalsIgnoreCase("vendorName")){
			sortByOption=" rx.Name ";
		}else if(column.equalsIgnoreCase("itemCode")){
			sortByOption=" pr.ItemCode ";
		}else if(column.equalsIgnoreCase("inventoryOnHand")){
			sortByOption=" pw.InventoryOnHand ";
		}else if(column.equalsIgnoreCase("lastCost")){
			sortByOption=" pr.LastCost ";
		}else if(column.equalsIgnoreCase("totalCost")){
			sortByOption=" TotalCost ";
		}else if(column.equalsIgnoreCase("averageCost")){
			sortByOption=" pr.AverageCost ";
		}else if(column.equalsIgnoreCase("inventoryOnHand")){
			sortByOption=" pw.InventoryOnHand ";
		}else if(column.equalsIgnoreCase("inventoryAllocated")){
			sortByOption=" pw.InventoryAllocated ";
		}else if(column.equalsIgnoreCase("inventoryAvailable")){
			sortByOption=" TotalCost ";
		}else if(column.equalsIgnoreCase("inventoryOnOrder")){
			sortByOption=" pw.InventoryOnOrder ";
		}else if(column.equalsIgnoreCase("submitted")){
			sortByOption=" pr.Submitted ";
		}	
		
		//removed condition pr.Description NOT LIKE '%DO NOT %' AND
		String aStr = "";
		
		if(warehouseId==-1)
			aStr = "SELECT pr.prMasterID,pr.ItemCode,REPLACE (pr.Description,'``','\"') as prDescription, SUM(pw.InventoryOnHand) AS InventoryOnHand, " +
					"SUM( pw.InventoryAllocated) AS InventoryAllocated, " +
					"SUM(pw.InventoryOnOrder) AS InventoryOnOrder, " +
					"SUM(pr.Submitted) AS Submitted, " +
					"pd.Description as Department,pc.Description as Category," +
					"rx.Name, pr.LastCost,IFNULL(pr.AverageCost*pw.InventoryOnHand ,0) AS TotalCost, pr.AverageCost" +
					" FROM prMaster pr" +
					" LEFT JOIN prWarehouseInventory pw ON pr.prMasterID = pw.prMasterID " +
					" LEFT JOIN prDepartment pd ON pd.prDepartmentID=pr.prDepartmentID" +
					" LEFT JOIN prCategory pc ON pc.prCategoryID = pr.prCategoryID" +
					" LEFT JOIN rxMaster rx ON rx.rxMasterID = pr.rxMasterIDPrimaryVendor WHERE " +
					warehouseCondn+" "+inactiveCondn +itemcodeCondn+" GROUP BY prMasterID ";
		else
			aStr = "SELECT pr.prMasterID,pr.ItemCode,REPLACE (pr.Description,'``','\"') as prDescription,pw.InventoryOnHand,pw.InventoryAllocated," +
					"pw.InventoryOnOrder,pr.Submitted,pd.Description as Department,pc.Description as Category," +
					"rx.Name, pr.LastCost,IFNULL(pr.AverageCost*pw.InventoryOnHand ,0) AS TotalCost, pr.AverageCost" +
					" FROM prMaster pr" +
					" LEFT JOIN prWarehouseInventory pw ON pr.prMasterID = pw.prMasterID " +
					" LEFT JOIN prDepartment pd ON pd.prDepartmentID=pr.prDepartmentID" +
					" LEFT JOIN prCategory pc ON pc.prCategoryID = pr.prCategoryID" +
					" LEFT JOIN rxMaster rx ON rx.rxMasterID = pr.rxMasterIDPrimaryVendor WHERE " +
					warehouseCondn+" "+inactiveCondn +itemcodeCondn;
		aStr += "ORDER BY "+sortByOption+sortBy.toUpperCase()+" LIMIT " + theFrom + ", " + theTo;
		System.out.println("aStr "+aStr);
				
		Prmaster aPrmaster = null;
		BigDecimal aTotal = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aStr);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				aPrmaster.setPrMasterId((Integer) aObj[0]);
				aPrmaster.setItemCode((String) aObj[1]);
				aPrmaster.setDescription((String) aObj[2]);
				aPrmaster.setInventoryOnHand((BigDecimal)aObj[3]);
				aPrmaster.setInventoryAllocated((BigDecimal)aObj[4]);
				aPrmaster.setInventoryOnOrder((BigDecimal)aObj[5]);
				aPrmaster.setSubmitted((BigDecimal)aObj[6]);
				aPrmaster.setPrDepartment((String)aObj[7]);
				aPrmaster.setPrCategory((String)aObj[8]);
				aPrmaster.setVendorName((String)aObj[9]);
				BigDecimal aUnitCost = (BigDecimal)aObj[3];
				BigDecimal aAllocated = (BigDecimal)aObj[4];
				BigDecimal aLastCost = (BigDecimal)aObj[10];
				//System.out.println("String"+(BigDecimal)aObj[11]+"Value");
				/*BigDecimal test = (BigDecimal)(aObj[11]);
				
				String str = (String)aObj[11];*/
				BigDecimal aTotalCost = (BigDecimal)(aObj[11]);
				BigDecimal aAverageCost = (BigDecimal)aObj[12];
				BigDecimal onHand = (BigDecimal)aObj[3];
				MathContext mc = new MathContext(4);
				
				//System.out.println(aPrmaster.getDescription()+"  ::::: "+aPrmaster.getInventoryOnOrder()+ " ::: "+aPrmaster.getInventoryOnHand());
				if(aUnitCost != null){
					Integer aUnit = Integer.valueOf(aUnitCost.intValue());
					if(aUnit != 0){
						aTotal = aUnitCost==null?new BigDecimal("0.0000").subtract(aAllocated==null?new BigDecimal("0.0000"):aAllocated):aUnitCost.subtract(aAllocated==null?new BigDecimal("0.0000"):aAllocated);
						aPrmaster.setInventoryOrderPoint(aTotal);
					}else{
						aPrmaster.setInventoryOrderPoint(aUnitCost);
					}
				}
				aPrmaster.setLastCost(aLastCost);
				aPrmaster.setTotalCost(aTotalCost);
				aPrmaster.setAverageCost(aAverageCost);
				aQueryList.add(aPrmaster);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			itemcodeCondn =null;
			aStr = null;
		}
		return aQueryList;	
	}

	@Override
	public List<PrDepartment> getPrDepartment() throws InventoryException{
		Session aSession = null;
		List<PrDepartment> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query query = aSession.createQuery("FROM PrDepartment");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException("Exception occurred while getting the Fried Charges in Purchase: " + excep.getMessage(), excep);
			throw aInventoryException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}

	@Override
	public List<PrCategory> getPrCategory() throws InventoryException{
		Session aSession = null;
		List<PrCategory> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query query = aSession.createQuery("FROM PrCategory ORDER BY Description ASC");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException("Exception occurred while getting the Fried Charges in Purchase: " + excep.getMessage(), excep);
			throw aInventoryException;		
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}

	@Override
	public Prmaster getSingleProductDetails(Integer theprMasterID) throws InventoryException{
		Session aSession = null;
		Prmaster aPrmaster = new Prmaster();
		try {
			aSession = itsSessionFactory.openSession();
			aPrmaster = new Prmaster();
			itsLogger.info("theprMasterID"+theprMasterID);
			aPrmaster = (Prmaster) aSession.get(Prmaster.class, theprMasterID);
			if(aPrmaster.getRxMasterIdprimaryVendor() != null){
				String aVendorName = null;
				aVendorName = getVendorNameUsingRxId(aPrmaster.getRxMasterIdprimaryVendor());
				aPrmaster.setVendorName(aVendorName);
			}
			String aProductDesc = aPrmaster.getDescription();
			if(null != aProductDesc)
				aPrmaster.setDescription(aProductDesc.replace("\"", "``"));
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aPrmaster;
	}

	private String getVendorNameUsingRxId(Integer rxMasterIdprimaryVendor) throws InventoryException {
		String aJobCountStr = "SELECT name FROM rxMaster where rxMasterId ="+rxMasterIdprimaryVendor;
		Session aSession = null;
		String aVendorName = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			if(aList.size()>0){
			aVendorName = (String) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr =null;
		}
		return aVendorName;
	}

	@Override
	public ArrayList<AutoCompleteBean> getVendorList(String theSearchString) throws InventoryException {
		itsLogger.debug("Retrieving Vendor Company names for AutoComplete");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster where IsVendor = 1 AND Name IS NOT null  AND Name like '%"+theSearchString+"%' ORDER BY Name ASC";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aUserbean = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])iterator.next();
				aUserbean.setId((Integer)aObj[0]);				
				aUserbean.setValue((String)aObj[1]);				
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
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry = null;
		}	
		return aQueryList;
	}

	@Override
	public Boolean deleteInventoryDetails(Integer theInventoryID) throws InventoryException {
		Session aInventorySession = itsSessionFactory.openSession();
		Transaction aTransaction; 
		String aCoBalanceQry;
		try {
			aTransaction = aInventorySession.beginTransaction();
			aCoBalanceQry = "DELETE FROM prMaster WHERE prMasterID =" + theInventoryID;
			aInventorySession.createSQLQuery(aCoBalanceQry).executeUpdate();
			aTransaction.commit();
		} catch (HibernateException e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e.getMessage(), e);
			throw aInventoryException;
		} finally {
			aInventorySession.flush();
			aInventorySession.close();
			aCoBalanceQry= null;
		}
		return true;
	}

	@Override
	public Boolean updateInventoryDetails(Prmaster thePrMaster, Prwarehouseinventory thePrwarehouseinventory) throws InventoryException {
		Session aSession = itsSessionFactory.openSession();
		Prmaster aPrmaster = null;
		Prwarehouseinventory aPrwarehouseinventory = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aPrmaster = (Prmaster) aSession.get(Prmaster.class, thePrMaster.getPrMasterId());
			aPrmaster.setItemCode(thePrMaster.getItemCode());
			aPrmaster.setDescription(thePrMaster.getDescription());
			aPrmaster.setPrDepartmentId(thePrMaster.getPrDepartmentId());
			aPrmaster.setPrCategoryId(thePrMaster.getPrCategoryId());
			aPrmaster.setBoxQty(thePrMaster.getBoxQty());
			aPrmaster.setWeight(thePrMaster.getWeight());
			aPrmaster.setPOMult(thePrMaster.getPOMult());
			aPrmaster.setSalesPrice00(thePrMaster.getSalesPrice00());
			aPrmaster.setLastCost(thePrMaster.getLastCost());
			aPrmaster.setSOPopup(thePrMaster.getSOPopup());
			aPrmaster.setPOPopup(thePrMaster.getPOPopup());
			aPrmaster.setCUPopup(thePrMaster.getCUPopup());
			aPrmaster.setQuantityBreak0(thePrMaster.getQuantityBreak0());
			aPrmaster.setQuantityBreak1(thePrMaster.getQuantityBreak1());
			aPrmaster.setSalesPrice01(thePrMaster.getSalesPrice01());
			aPrmaster.setSalesPrice10(thePrMaster.getSalesPrice10());
			aPrmaster.setSalesPrice20(thePrMaster.getSalesPrice20());
			aPrmaster.setSalesPrice30(thePrMaster.getSalesPrice30());
			aPrmaster.setSalesPrice40(thePrMaster.getSalesPrice40());
			aPrmaster.setSalesPrice50(thePrMaster.getSalesPrice50());
			aPrmaster.setInActive(thePrMaster.getInActive());
			aPrmaster.setIsInventory(thePrMaster.getIsInventory());
			aPrmaster.setIsTaxable(thePrMaster.getIsTaxable());
			aPrmaster.setConsignment(thePrMaster.getConsignment());
			aPrmaster.setPrintOnPos(thePrMaster.getPrintOnPos());
			aPrmaster.setPrintOnSos(thePrMaster.getPrintOnSos());
			aPrmaster.setSerialized(thePrMaster.getSerialized());
			aPrmaster.setAutoAssemble(thePrMaster.getAutoAssemble());
			aPrmaster.setWebSite(thePrMaster.getWebSite());
			aPrmaster.setLabor(thePrMaster.getLabor());
			aPrmaster.setSingleItemTax(thePrMaster.getSingleItemTax());
			aPrmaster.setPrintOnPTs(thePrMaster.getPrintOnPTs());
			aPrmaster.setPrintOnCUs(thePrMaster.getPrintOnCUs());
			aPrmaster.setRxMasterIdprimaryVendor(thePrMaster.getRxMasterIdprimaryVendor());
			aPrmaster.setVendorItemNumber(thePrMaster.getVendorItemNumber());
			aPrmaster.setPrMasterId(thePrMaster.getPrMasterId());
			aSession.update(aPrmaster);
			aTransaction.commit();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aPrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, thePrwarehouseinventory.getPrWarehouseInventoryId());
			aPrwarehouseinventory.setPrMasterId(thePrwarehouseinventory.getPrMasterId());
			if(thePrwarehouseinventory.getPrWarehouseId()!=-1)
			aPrwarehouseinventory.setPrWarehouseId(thePrwarehouseinventory.getPrWarehouseId());
			aPrwarehouseinventory.setBin(thePrwarehouseinventory.getBin());
			aSession.update(aPrwarehouseinventory);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public Integer getPrWareID(Prwarehouseinventory thePrwarehouseinventory) throws InventoryException {
		String aWarehouseStr = "";
	/*	if(thePrwarehouseinventory.getPrWarehouseId() != null){
			aWarehouseStr = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+thePrwarehouseinventory.getPrMasterId()+" and prWarehouseID = "+thePrwarehouseinventory.getPrWarehouseId()+"";
		}else{
			aWarehouseStr = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+thePrwarehouseinventory.getPrMasterId()+"";
		}*/
		aWarehouseStr = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+thePrwarehouseinventory.getPrMasterId()+"";
		Session aSession = null;
		Integer aPrWareID = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aWarehouseStr);
			List<?> aList = aQuery.list();
			if(!aList.isEmpty()){
				aPrWareID = (Integer) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aWarehouseStr = null;
		}
		return aPrWareID;
	}

	@Override
	public Prwarehouseinventory getSingleWareDetails(Integer theprMasterID) throws InventoryException {
		Session aSession = null;
		Prwarehouseinventory aPrwarehouseinventory = null;
		Integer aPrWareID = null;
		try {
			Prwarehouseinventory aPrwarehouseinventory2 = new Prwarehouseinventory();
			aPrwarehouseinventory2.setPrMasterId(theprMasterID);
			aPrWareID = this.getPrWareID(aPrwarehouseinventory2);
			aSession = itsSessionFactory.openSession();
			aPrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, aPrWareID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aPrwarehouseinventory;
	}
	
	@Override
	public Prwarehouseinventory getPrwarehouseinventoryDetails(Integer PrwarehouseinventoryID) throws InventoryException {
		Session aSession = null;
		Prwarehouseinventory aPrwarehouseinventory = null;
		Integer aPrWareID = null;
		try {
			aSession = itsSessionFactory.openSession();
			aPrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, PrwarehouseinventoryID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aPrwarehouseinventory;
	}

	@Override
	public Integer addNewInventory(Prmaster thePrMaster, Prwarehouseinventory thePrwarehouseinventory) throws InventoryException {
		Session aPrWareHouseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String Qry = "";
		Integer id=0;
		Integer prWarehouseID = 0;
		prWarehouseID = thePrwarehouseinventory.getPrWarehouseId();
		Prwarehouseinventory aPrwarehouseinventory = null; 
		itsLogger.info("prWarehouseID: "+prWarehouseID);
		try {
			
			aTransaction = aPrWareHouseSession.beginTransaction();
			aTransaction.begin();
			Integer aPrMasterID = (Integer) aPrWareHouseSession.save(thePrMaster);
			aTransaction.commit();
			thePrwarehouseinventory.setPrMasterId(aPrMasterID);
			
			 Qry = "FROM Prwarehouse WHERE InActive=0 ";
			List<Prwarehouse> warehouses = new ArrayList<Prwarehouse>();
			warehouses = (List<Prwarehouse>)aPrWareHouseSession.createQuery(Qry).list();
			if(warehouses.size()>0){
				for(int j=0;j<warehouses.size();j++){
					
					
					aTransaction = aPrWareHouseSession.beginTransaction();
					aTransaction.begin();
					aPrwarehouseinventory = new Prwarehouseinventory();
					aPrwarehouseinventory.setPrMasterId(thePrwarehouseinventory.getPrMasterId());
					aPrwarehouseinventory.setBin("");
					byte aHasInitialCost = 0;
					aPrwarehouseinventory.setHasInitialCost(thePrwarehouseinventory.getHasInitialCost());
					aPrwarehouseinventory.setInventoryOnHand(thePrwarehouseinventory.getInventoryOnHand());
					aPrwarehouseinventory.setInventoryOnOrder(thePrwarehouseinventory.getInventoryOnOrder());
					aPrwarehouseinventory.setInventoryAllocated(thePrwarehouseinventory.getInventoryAllocated());
					aPrwarehouseinventory.setPrWarehouseId(warehouses.get(j).getPrWarehouseId());
					id = (Integer) aPrWareHouseSession.save(aPrwarehouseinventory);
					aTransaction.commit();
				}
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aPrWareHouseSession.flush();
			aPrWareHouseSession.close();
			Qry= null;
		}
		return id;
	}

	/*
	 * @Override
	public Integer updateAverageCost(Prmaster thePrMaster) throws InventoryException {

		String aWarehouseStr = "";
		aWarehouseStr = "SELECT TranDate, IsPurchase, QTY, Cost, FreightCost FROM ( SELECT P.OrderDate AS TranDate, 1 AS IsPurchase, "
				+ "D.QuantityOrdered AS QTY, D.QuantityOrdered * D.UnitCost * (CASE WHEN (D.PriceMultiplier= 0) THEN 1 ELSE D.PriceMultiplier END) "
				+ "AS Cost, 0.0000 AS FreightCost FROM vePODetail AS D LEFT JOIN vePO AS P ON D.vePOID = P.vePOID "
				+ "WHERE D.prMasterID ="+thePrMaster.getPrMasterId()+" AND P.prWarehouseID IS NOT NULL AND P.TransactionStatus > 0 UNION ALL SELECT H.InvoiceDate AS TranDate, "
				+ "0 AS IsPurchase, D.QuantityBilled*-1 AS QTY, NULL AS Cost, NULL AS FreightCost FROM cuInvoice H RIGHT "
				+ "OUTER JOIN cuInvoiceDetail D ON H.cuInvoiceID = D.cuInvoiceID WHERE (D.prMasterID = "+thePrMaster.getPrMasterId()+") AND (H.TransactionStatus > 0) "
				+ "UNION ALL SELECT prTransfer.TransferDate AS TranDate, CASE WHEN prTransfer.Assemble_prMasterID = prTransferDetail.prMasterID "
				+ "THEN 1 ELSE 0 END AS IsPurchase, prTransferDetail.QuantityTransfered*-1 AS QTY, CASE WHEN "
				+ "prTransfer.Assemble_prMasterID = prTransferDetail.prMasterID THEN -1*QuantityTransfered*InventoryCost ELSE NULL END AS Cost, "
				+ "NULL AS FreightCost FROM prTransfer INNER JOIN prTransferDetail ON prTransfer.prTransferID = prTransferDetail.prTransferID "
				+ "WHERE (prTransfer.prToWarehouseID IS NULL) AND (prTransferDetail.prMasterID ="+thePrMaster.getPrMasterId()+")) qry ORDER BY TranDate, IsPurchase DESC";
		Session aSession = null;
		Integer isPurchase = 0;
		BigDecimal qty = new BigDecimal("0.0000");
		BigDecimal cost = new BigDecimal("0.0000");
		BigDecimal freightCost = new BigDecimal("0.0000");
		try {
			If Logical(prMaster("HasInitialCost")) Then
            FirstCost = False
            TotalQty = TotalQty + Dollar(prMaster("InitialOnHand"))
            averageCost = Dollar(prMaster("InitialCost"))
        End If
        if(thePrMaster.isHasInitialCost()==1){
        	
        }
			
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aWarehouseStr);
			Iterator<?> iterator = aQuery.list().iterator();
			while(iterator.hasNext()) {
				Object[] aObj = (Object[])iterator.next();
				isPurchase = (Integer)aObj[1];
				qty =  (BigDecimal)aObj[2];
				cost =  (BigDecimal)aObj[3];
				freightCost =  (BigDecimal)aObj[4];
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPrWareID;
	}
	*/
	
	@Override
	public Boolean updateInventory(Prmaster thePrMaster) throws InventoryException {
		Session aPrMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrMasterSession.beginTransaction();
			aTransaction.begin();
			aPrMasterSession.update(thePrMaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aPrMasterSession.flush();
			aPrMasterSession.close();
		}
		return true;
	}

	@Override
	public List<PrCategory> getInventoryCategories(String columnname,String ascordesc) throws InventoryException {
		Session aSession = null;
		List<PrCategory> aTaxTerritoryList = new ArrayList<PrCategory>();
		String Query="FROM  PrCategory";
		if(!columnname.equals(null)){
			if(columnname.equals("description")){
				Query=Query+" order by description "+ascordesc;	
			}
		}
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery =aSession.createQuery(Query);
			aTaxTerritoryList = (List<PrCategory>)aQuery.list();;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new InventoryException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			Query = null;
		}
		return aTaxTerritoryList;	
	}

	@Override
	public void addCategory(PrCategory aCategory) throws InventoryException{
		Session aPrCategorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrCategorySession.beginTransaction();
			aTransaction.begin();
			aPrCategorySession.save(aCategory);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrCategorySession.flush();
			aPrCategorySession.close();
		}
	}

	@Override
	public void editCategory(PrCategory aCategory)throws InventoryException{
		Session aPrCategorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrCategorySession.beginTransaction();
			aTransaction.begin();
			aPrCategorySession.update(aCategory);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrCategorySession.flush();
			aPrCategorySession.close();
		}
	}

	public void deleteCategory(PrCategory aCategory) throws InventoryException{
		Session aPrCategorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrCategorySession.beginTransaction();
			aTransaction.begin();
			aPrCategorySession.delete(aCategory);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrCategorySession.flush();
			aPrCategorySession.close();
		}
	}

	/*@Override
	public List<PrDepartment> getInventoryDepartments() throws InventoryException {

		return null;
	}*/

	@Override
	public List<PrDepartment> getInventoryDepartments() throws InventoryException {
		Session aSession = null;
		List<PrDepartment> aDepartments = new ArrayList<PrDepartment>();
		try{
			aSession = itsSessionFactory.openSession();
			aDepartments = (List<PrDepartment>)aSession.createCriteria(PrDepartment.class).list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new InventoryException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aDepartments;
	}

	@Override
	public List<PrDepartmentAccounts> getDepartmentAccountNo() throws InventoryException {
		String aDeptAccSelectQuery="select a.prDepartmentID,a.Description,a.InActive,a.coAccountIDSales,a.coAccountIDCOGS,b.Number as revenue,c.Number as cgs from prDepartment as a "+
				"LEFT JOIN coAccount as b ON a.coAccountIDSales=b.coAccountID "+ 
				"LEFT JOIN coAccount as c ON a.coAccountIDCOGS=c.coAccountID";
		Session aSession=null;
		List<PrDepartmentAccounts> aDepartmentAccounts=new ArrayList<PrDepartmentAccounts>();
		PrDepartmentAccounts itsPrDepartmentAccounts=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aDeptAccSelectQuery);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				itsPrDepartmentAccounts = new  PrDepartmentAccounts();
				Object[] aObj = (Object[])iterator.next();
				itsPrDepartmentAccounts.setPrDepartmentId((Integer)aObj[0]);
				itsPrDepartmentAccounts.setDescription((String)aObj[1]);
				Byte itsInActivevalue=(Byte)aObj[2];
				if(itsInActivevalue.byteValue()==0)
					itsPrDepartmentAccounts.setInActive(false);
				else if(itsInActivevalue.byteValue()==1)
					itsPrDepartmentAccounts.setInActive(true);
				itsPrDepartmentAccounts.setCoAccountIDSales((Integer)aObj[3]);
				itsPrDepartmentAccounts.setCoAccountIDCOGS((Integer)aObj[4]);
				itsPrDepartmentAccounts.setRevenueAccount((String)aObj[5]);
				itsPrDepartmentAccounts.setCgsAccount((String)aObj[6]);
				aDepartmentAccounts.add(itsPrDepartmentAccounts);
			}

		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aDeptAccSelectQuery= null;
		}	
		return aDepartmentAccounts;

	}

	@Override
	public void addDepartment(PrDepartment aDepartment)	throws InventoryException {
		Session aPrDepartmentSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrDepartmentSession.beginTransaction();
			aTransaction.begin();
			aPrDepartmentSession.save(aDepartment);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrDepartmentSession.flush();
			aPrDepartmentSession.close();
		}
	}

	@Override
	public void editDepartment(PrDepartment aDepartment) throws InventoryException {
		Session aPrDepartmentSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrDepartmentSession.beginTransaction();
			aTransaction.begin();
			aPrDepartmentSession.update(aDepartment);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrDepartmentSession.flush();
			aPrDepartmentSession.close();
		}
	}

	@Override
	public void deleteDepartment(PrDepartment aDepartment) throws InventoryException {
		Session aPrDepartmentSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPrDepartmentSession.beginTransaction();
			aTransaction.begin();
			aPrDepartmentSession.delete(aDepartment);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrDepartmentSession.flush();
			aPrDepartmentSession.close();
		}
	}
	
	public ArrayList<AutoCompleteBean> getAccountList(String theSearchString,AutoCompleteBean theUserBean) throws InventoryException {
		String aAccountsselectQry = "SELECT coAccountID,Number,Description FROM coAccount where (Description IS NOT NULL  AND Description LIKE '%"+theSearchString+"%') OR  (Number IS NOT NULL  AND Number LIKE '%"+theSearchString+"%')";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aUserbean = theUserBean;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aAccountsselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aUserbean = new  AutoCompleteBean();
				Object[] aObj = (Object[])iterator.next();
				aUserbean.setId((Integer)aObj[0]);				
				aUserbean.setValue((String)aObj[1]);				
				aUserbean.setLabel((String)aObj[2]+" ["+(String)aObj[1]+ "] ");	
				aQueryList.add(aUserbean);
			}
			if(aQueryList.isEmpty()){
				aUserbean.setValue(" ");	
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aAccountsselectQry =null;
		}	
		return aQueryList;
	}
	
	@Override
	public ArrayList<SalesOrderBean> getsalesOrderList(Integer theprMasterID) throws InventoryException {
		//String aCusoselectQry = "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		/*String aCusoselectQry = "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,(SUM(sodetail.QuantityOrdered) -  SUM(sodetail.QuantityBilled)) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSO so, cuSODetail sodetail,rxMaster rxmaster,prWarehouse warehouse WHERE"+
" so.cuSOID = sodetail.cuSOID AND so.TransactionStatus = 1 AND rxmaster.rxMasterID = so.rxCustomerID AND warehouse.prWarehouseID = so.prFromWarehouseID AND"+
" sodetail.prMasterID= "+theprMasterID+" AND sodetail.QuantityBilled IS NOT NULL GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";*/
//	String aCusoselectQry="SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,(SUM(sodetail.QuantityOrdered) - SUM(sodetail.QuantityBilled)) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSO so, cuSODetail sodetail,rxMaster rxmaster,prWarehouse warehouse "+
//						  "WHERE so.cuSOID = sodetail.cuSOID AND so.TransactionStatus = 1 AND rxmaster.rxMasterID = so.rxCustomerID AND warehouse.prWarehouseID = so.prFromWarehouseID AND "+
//						  "sodetail.prMasterID="+theprMasterID+" AND sodetail.QuantityBilled IS NOT NULL GROUP BY sodetail.cuSOID"+
//						  " UNION "+
//						  "SELECT '-1' AS cuSOID,'-1' AS cuSODetailID, H.TransferDate AS OrderDate, NULL AS DatePromised, 'Warehouse Transfer Out' AS NAME,'' AS joReleaseId,D.QuantityTransfered AS QuantityOrdered,prWarehouse.City,'0' AS rxCustomerID FROM prWarehouse INNER JOIN prWHtransfer H ON "+
//						  "prWarehouse.prWarehouseID = H.prFromWareHouseID RIGHT OUTER JOIN prWHtransferDetail D ON H.prTransferID = D.prTransferID WHERE (H.ReceivedDate IS NULL) AND (D.prMasterID = "+theprMasterID+")";

String aCusoselectQry="SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.SONumber,(SUM(sodetail.QuantityOrdered) - SUM(sodetail.QuantityBilled)) AS quantityordered,warehouse.SearchName,so.rxCustomerID FROM cuSO so LEFT JOIN cuSODetail sodetail ON (so.cuSOID = sodetail.cuSOID) "+
					  "LEFT JOIN rxMaster rxmaster ON(rxmaster.rxMasterID = so.rxCustomerID ) LEFT JOIN prWarehouse warehouse ON(warehouse.prWarehouseID = so.prFromWarehouseID) "+ 
		              "LEFT JOIN prMaster pr ON(sodetail.prMasterID=pr.prMasterID) "+
					  "WHERE  so.TransactionStatus = 1 AND "+	
					  "sodetail.prMasterID="+theprMasterID+" AND sodetail.QuantityBilled IS NOT NULL AND pr.IsInventory=1  GROUP BY sodetail.cuSOID"+
					  " UNION ALL "+
					  "SELECT -1 AS cuSOID,-1 AS cuSODetailID, H.TransferDate AS OrderDate, NULL AS DatePromised, 'Warehouse Transfer Out' AS NAME,'' AS SONumber,D.QuantityTransfered AS QuantityOrdered,prWarehouse.SearchName,0 AS rxCustomerID FROM prWarehouse INNER JOIN prWHtransfer H ON "+
					  "prWarehouse.prWarehouseID = H.prFromWareHouseID RIGHT OUTER JOIN prWHtransferDetail D ON H.prTransferID = D.prTransferID LEFT JOIN prMaster pr ON(D.prMasterID=pr.prMasterID)  WHERE (H.ReceivedDate IS NULL) AND  `prToWareHouseID` IS NOT NULL AND pr.IsInventory=1 AND (D.prMasterID = "+theprMasterID+")";

		Session aSession=null;
		ArrayList<SalesOrderBean> aQueryList = new ArrayList<SalesOrderBean>();
		SalesOrderBean aSalesOrderBean = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aCusoselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aSalesOrderBean = new  SalesOrderBean();
				Object[] aObj = (Object[])iterator.next();
				if(aObj[0] !=null){
					
					BigInteger cusoIDconvert = (BigInteger)aObj[0];
					
					aSalesOrderBean.setCuSoid(cusoIDconvert.intValue());	
				}
				
				if(aObj[2] != null)
				{
					Timestamp stamp = (Timestamp)aObj[2];
					Date date = new Date(stamp.getTime());
					aSalesOrderBean.setCreatedOn(date); //(Date) aObj[1]
				}
				
				aSalesOrderBean.setDatePromised((String)aObj[3]);	
				aSalesOrderBean.setName((String)aObj[4]);
				if(aObj[5]!=null){
					aSalesOrderBean.setJobName(aObj[5].toString());	
				}else{
					aSalesOrderBean.setJobName("");	
				}
				
				aSalesOrderBean.setQuantityOrdered((BigDecimal)aObj[6]);
				aSalesOrderBean.setCity((String)aObj[7]);
				if(aObj[8] !=null){
					
					BigInteger rxCustomerID = (BigInteger)aObj[8]; 
					aSalesOrderBean.setRxCustomerId(rxCustomerID.intValue());	
				}
				if(aSalesOrderBean.getQuantityOrdered()!=null && aSalesOrderBean.getQuantityOrdered().compareTo(BigDecimal.ZERO)==0){
					
				}else{
					aQueryList.add(aSalesOrderBean);
				}
				
			}
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}	
		return aQueryList;
	}
	
	@Override
	public ArrayList<PurchaseOrdersBean> getOnOrderList(Integer theprMasterID) throws InventoryException {
		//String aCusoselectQry = "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		/*String aCusoselectQry = "SELECT vepo.vePOID,vepo.PONumber,vepo.OrderDate,vedetail.EstimatedShipDate,rxmaster.Name,vepo.rxVendorID,vepo.prWarehouseID,SUM(vedetail.QuantityOrdered) AS QuantityOrdered,warehouse.City FROM vePO vepo,vePODetail vedetail,rxMaster rxmaster,prWarehouse warehouse WHERE"+
" vepo.vePOID = vedetail.vePOID AND rxmaster.rxMasterID = vepo.rxVendorID AND warehouse.prWarehouseID = vepo.prWarehouseID AND"+
" vedetail.prMasterID= "+theprMasterID+" AND vedetail.QuantityOrdered IS NOT NULL AND vepo.receiveddate IS NULL AND vepo.vePOID NOT IN (SELECT vebill.vePOID FROM veBill vebill) "
		+ "GROUP BY vepo.vePOID  ORDER BY vedetail.vePOID DESC";*/
		/*String aCusoselectQry = "SELECT vepo.vePOID,vepo.PONumber,vepo.OrderDate,vedetail.EstimatedShipDate,rxmaster.Name,vepo.rxVendorID,"
				+ "vepo.prWarehouseID,SUM(vedetail.QuantityOrdered-vedetail.QuantityReceived) AS QuantityOrdered,warehouse.City FROM vePO vepo "
				+ "LEFT JOIN vePODetail vedetail ON vepo.vePOID = vedetail.vePOID "
				+ "LEFT JOIN rxMaster rxmaster ON rxmaster.rxMasterID = vepo.rxVendorID "
				+ "LEFT JOIN prWarehouse warehouse ON warehouse.prWarehouseID = vepo.prWarehouseID "
				+ "LEFT JOIN veBill vebill ON vepo.vePOID = vebill.vePOID  WHERE  vedetail.prMasterID= "+theprMasterID
				+ " AND  vedetail.QuantityOrdered IS NOT NULL  AND "
				//+ "(vepo.receiveddate IS NULL OR vepo.vePOID NOT IN (vebill.vePOID)) "
				+ "vepo.receiveddate IS NULL  "
				+ "GROUP BY vepo.vePOID  ORDER BY vedetail.vePOID DESC";*/
		String aCusoselectQry = "SELECT vepo.vePOID,CAST(vepo.PONumber  AS CHAR) AS ponumber,vepo.OrderDate,vedetail.EstimatedShipDate,rxmaster.Name,vepo.rxVendorID,"
				+ "vepo.prWarehouseID,"
				+"CASE WHEN vepo.`joReleaseID` IS NULL THEN SUM(IFNULL(vedetail.QuantityOrdered,0)-IFNULL(vedetail.QuantityReceived ,0) ) ELSE "
				+"0 END AS QuantityOrdered,"
				//+"SUM(IFNULL(vedetail.QuantityOrdered,0)-IFNULL(vedetail.QuantityBilled ,0) ) END AS QuantityOrdered,"
				+"warehouse.City FROM vePO vepo "
				+ "LEFT JOIN vePODetail vedetail ON vepo.vePOID = vedetail.vePOID "
				+ "LEFT JOIN rxMaster rxmaster ON rxmaster.rxMasterID = vepo.rxVendorID "
				+ "LEFT JOIN prWarehouse warehouse ON warehouse.prWarehouseID = vepo.prWarehouseID "
				+ "LEFT JOIN joRelease ON vepo.joReleaseID=joRelease.joReleaseID "
				+ "LEFT JOIN prMaster pr ON(vedetail.prMasterID=pr.prMasterID) "
				+ " WHERE  vedetail.prMasterID= "+theprMasterID
				+ " AND  vedetail.QuantityOrdered IS NOT NULL AND pr.IsInventory=1 "
				+ " And (joRelease.releaseType IS NULL || joRelease.releaseType<>4) "
				//+ "AND (vepo.receiveddate IS NULL OR (vedetail.QuantityOrdered-vedetail.QuantityReceived) >0) "
				+ "GROUP BY vepo.vePOID HAVING QuantityOrdered>0 "
				+ "UNION All "
				+ "SELECT D.prTransferDetailID AS vePOID,CAST(H.TransNumber  AS CHAR) AS ponumber, H.TransferDate AS OrderDate,H.EstreceiveDate AS EstimatedShipDate,'Warehouse Transfer In' AS NAME,0 AS rxVendorID,"
				+ "prWarehouse.prWarehouseID,D.QuantityTransfered AS QuantityOrdered,prWarehouse.SearchName "
				+ "FROM prWarehouse INNER JOIN prWHtransfer H ON prWarehouse.prWarehouseID = H.prToWareHouseID "
				+ " JOIN prWHtransferDetail D ON H.prTransferID = D.prTransferID "
				+ "LEFT JOIN prMaster pr ON(D.prMasterID=pr.prMasterID) "
				+ "WHERE (H.ReceivedDate IS NULL) AND pr.IsInventory=1 AND (D.prMasterID = "+theprMasterID+") "
				+ " ORDER BY vePOID DESC";
		Session aSession=null;
		ArrayList<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aCusoselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aPurchaseOrdersBean = new  PurchaseOrdersBean();
				Object[] aObj = (Object[])iterator.next();
				aPurchaseOrdersBean.setVePOID((Integer)aObj[0]);
				aPurchaseOrdersBean.setPONumber((String) aObj[1]);	

				if(aObj[2] != null)
				{
					Timestamp stamp = (Timestamp)aObj[2];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}
				else
					aPurchaseOrdersBean.setCreatedOn((Date)aObj[2] );
				if(null != aObj[3])
				{
					Timestamp stamp = (Timestamp)aObj[3];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setEstimatedShipDate(date.toString());	
				}
				else
					aPurchaseOrdersBean.setEstimatedShipDate("");	
				aPurchaseOrdersBean.setVendorName((String)aObj[4]);	
				//aPurchaseOrdersBean.setJobName((String)aObj[5]);	
				aPurchaseOrdersBean.setQuantityOrdered((BigDecimal)aObj[7]);
				aPurchaseOrdersBean.setCity((String)aObj[8]);
				//aPurchaseOrdersBean.setRxCustomerId((Integer)aObj[8]);
				aQueryList.add(aPurchaseOrdersBean);
			}
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}	
		return aQueryList;
	}
	
	@Override
	public ArrayList<PurchaseOrdersBean> getOnHandList(Integer theprMasterID) throws InventoryException {
		//String aCusoselectQry = "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		String aCusoselectQry = "SELECT inventory.InventoryOnHand,inventory.InventoryAllocated,inventory.InventoryOnOrder,warehouse.SearchName FROM prWarehouseInventory inventory,prWarehouse warehouse,prMaster pr  WHERE  inventory.prWarehouseID = warehouse.prWarehouseID AND inventory.prMasterID=pr.prMasterID AND inventory.prMasterID ="+theprMasterID+" AND pr.IsInventory=1";
		Session aSession=null;
		ArrayList<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aCusoselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aPurchaseOrdersBean = new  PurchaseOrdersBean();
				Object[] aObj = (Object[])iterator.next();
				aPurchaseOrdersBean.setInventoryOnHand((BigDecimal)aObj[0]);
				aPurchaseOrdersBean.setInventoryAllocated((BigDecimal)aObj[1]);	
				aPurchaseOrdersBean.setInventoryOnOrder((BigDecimal)aObj[2]);
				BigDecimal inventoryonhand=aPurchaseOrdersBean.getInventoryOnHand()==null?new BigDecimal("0.0000"):aPurchaseOrdersBean.getInventoryOnHand();
				BigDecimal inventoryAllocated=aPurchaseOrdersBean.getInventoryAllocated()==null?new BigDecimal("0.0000"):aPurchaseOrdersBean.getInventoryAllocated();
				/*if(aPurchaseOrdersBean.getInventoryAllocated().compareTo(new BigDecimal("0.0000"))==1){
					aPurchaseOrdersBean.setInventoryAvailable(aPurchaseOrdersBean.getInventoryOnHand()==null?new BigDecimal("0.0000").subtract(aPurchaseOrdersBean.getInventoryAllocated()==null?new BigDecimal("0.0000"):aPurchaseOrdersBean.getInventoryAllocated()):aPurchaseOrdersBean.getInventoryOnHand().subtract(aPurchaseOrdersBean.getInventoryAllocated()==null?new BigDecimal("0.0000"):aPurchaseOrdersBean.getInventoryAllocated()));
				}else{
					aPurchaseOrdersBean.setInventoryAvailable(aPurchaseOrdersBean.getInventoryOnHand());
				}*/
				aPurchaseOrdersBean.setInventoryAvailable(inventoryonhand.subtract(inventoryAllocated));
				aPurchaseOrdersBean.setCity((String)aObj[3]);
				System.out.println("OnHand"+aPurchaseOrdersBean.getInventoryOnHand()+"\n Allocated"+aPurchaseOrdersBean.getInventoryAllocated()
						+"\n OnOrder"+aPurchaseOrdersBean.getInventoryOnOrder()
						+"\n Available"+aPurchaseOrdersBean.getInventoryAvailable());
				aQueryList.add(aPurchaseOrdersBean);
			}
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}	
		return aQueryList;
	}
	
	public ArrayList<PurchaseOrdersBean> getOnHandListForInventoryAdjust(Integer theprMasterID,Integer theWarehouseID) throws InventoryException { 
		//String aCusoselectQry = "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		String aCusoselectQry = "SELECT inventory.InventoryOnHand,inventory.InventoryAllocated,inventory.InventoryOnOrder,warehouse.SearchName FROM prWarehouseInventory inventory,prWarehouse warehouse  WHERE prMasterID =  "+theprMasterID+" AND inventory.prWarehouseID = warehouse.prWarehouseID AND inventory.prWarehouseID != "+theWarehouseID+"  ";
		Session aSession=null;
		ArrayList<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aCusoselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aPurchaseOrdersBean = new  PurchaseOrdersBean();
				Object[] aObj = (Object[])iterator.next();
				aPurchaseOrdersBean.setInventoryOnHand((BigDecimal)aObj[0]);
				aPurchaseOrdersBean.setInventoryAllocated((BigDecimal)aObj[1]);	
				aPurchaseOrdersBean.setInventoryOnOrder((BigDecimal)aObj[2]);
				aPurchaseOrdersBean.setInventoryAvailable(aPurchaseOrdersBean.getInventoryOnHand()==null?new BigDecimal("0.0000").subtract(aPurchaseOrdersBean.getInventoryAllocated()==null?new BigDecimal("0.0000"):aPurchaseOrdersBean.getInventoryAllocated()):aPurchaseOrdersBean.getInventoryOnHand().subtract(aPurchaseOrdersBean.getInventoryAllocated()==null?new BigDecimal("0.0000"):aPurchaseOrdersBean.getInventoryAllocated()));
				aPurchaseOrdersBean.setCity((String)aObj[3]);
				aQueryList.add(aPurchaseOrdersBean);
			}
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}	
		return aQueryList;
	}
	
	@Override
	public ArrayList<Prwhtransferdetail> getWarehouseTransferList(Integer theprTransferId) throws InventoryException {
		//String aCusoselectQry = "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		String aCusoselectQry = "select prTransferDetailID,prTransferID,prMasterID,Description,QuantityTransfered,QuantityAvailable,InventoryCost,ItemCode from prWHtransferDetail where prTransferID="+theprTransferId;
		Session aSession=null;
		ArrayList<Prwhtransferdetail> aQueryList = new ArrayList<Prwhtransferdetail>();
		Prwhtransferdetail aPrwhtransferdetail = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aCusoselectQry);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext()) {
				aPrwhtransferdetail = new  Prwhtransferdetail();
				Object[] aObj = (Object[])iterator.next();
				aPrwhtransferdetail.setPrTransferDetailId((Integer)aObj[0]);
				aPrwhtransferdetail.setPrTransferId((Integer)aObj[1]);
				aPrwhtransferdetail.setPrMasterId((Integer)aObj[2]);
				aPrwhtransferdetail.setDescription((String)aObj[3]);
				aPrwhtransferdetail.setQuantityTransfered((BigDecimal)aObj[4]);
				aPrwhtransferdetail.setQuantityAvailable((BigDecimal)aObj[5]);
				aPrwhtransferdetail.setInventoryCost((BigDecimal)aObj[6]);
				aPrwhtransferdetail.setItemCode((String)aObj[7]);
				aQueryList.add(aPrwhtransferdetail);
			}
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}	
		return aQueryList;
	}
	
	@Override
	public List<Prmaster> getTransferLineItems(Integer prMasterID,Integer fromwarehouse) throws InventoryException {
		/*Prmaster*/
		//SELECT prMasterID,ItemCode,Description,IsTaxable,PurchasingUnitMultiplier,NewPrice FROM prMaster
		
		//String aQry = "SELECT prMasterId,itemCode,Description,isTaxable,POMult,SalesPrice00,InventoryOnHand FROM prMaster WHERE prMasterId = "+prMasterID;
		String aQry="SELECT pm.prMasterId,pm.itemCode,pm.Description,pm.isTaxable,pm.POMult,pm.SalesPrice00,pw.InventoryOnHand,pw.InventoryAllocated FROM prMaster pm JOIN prWarehouseInventory pw ON(pm.prMasterID=pw.prMasterID)  WHERE pm.prMasterId = "+prMasterID+" AND pw.`prWarehouseID`="+fromwarehouse;
		Session aSession=null;
		List<Prmaster> alPrmaster = new ArrayList<Prmaster>();
		Prmaster objPrmaster = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext())
			{
				objPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				objPrmaster.setPrMasterId(Integer.valueOf(((Integer)aObj[0])));	
				objPrmaster.setItemCode((String) aObj[1]);	
				objPrmaster.setDescription((String) aObj[2]);	
				objPrmaster.setIsTaxable( new Byte((aObj[3]).toString()));					
				objPrmaster.setPOMult(new BigDecimal(1));
				if(null == aObj[5])
				{
					String sLastPrice = (String)aObj[5];
					if(null == sLastPrice || "null".equalsIgnoreCase(sLastPrice) || "".equalsIgnoreCase(sLastPrice))
					{
						objPrmaster.setLastCost(new BigDecimal(0));
					}
					else
						objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
				else
				{
					objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
				objPrmaster.setInventoryOnHand((BigDecimal)aObj[6]);
				if(aObj[7]!=null){
					objPrmaster.setInventoryAllocated(JobUtil.ConvertintoBigDecimal(aObj[7].toString()));
				}else{
					objPrmaster.setInventoryAllocated(BigDecimal.ZERO);
				}
				
				alPrmaster.add(objPrmaster);
			}
			//if(aQuery.list().size() > 0)
			return alPrmaster;
			/*else
				return null;*/
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aQry =null;
		}	
		
	}
	
	@Override
	public PrwarehouseTransfer saveTransferDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		PrwarehouseTransfer objPrwarehouseTransfer = new PrwarehouseTransfer();
		Transaction aTransaction;
		try {
			aTransaction = aPrWarehouseSession.beginTransaction();
			aTransaction.begin();
			Integer id = (Integer) aPrWarehouseSession.save(aPrwarehouseTransfer);			
			objPrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, id);
			aTransaction.commit();
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return objPrwarehouseTransfer;
	}
	
	
	@Override
	public PrwarehouseTransfer updateTransferDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		PrwarehouseTransfer objPrwarehouseTransfer;
		Transaction aTransaction;
		try {
			aTransaction = aPrWarehouseSession.beginTransaction();
			aTransaction.begin();
			objPrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, aPrwarehouseTransfer.getPrTransferId());
			if(aPrwarehouseTransfer.getTransferDate() != null)
				objPrwarehouseTransfer.setTransferDate(aPrwarehouseTransfer.getTransferDate());
			if(aPrwarehouseTransfer.getPrFromWarehouseId() != null)
				objPrwarehouseTransfer.setPrFromWarehouseId(aPrwarehouseTransfer.getPrFromWarehouseId());
			if(aPrwarehouseTransfer.getReceivedDate() != null)
				objPrwarehouseTransfer.setReceivedDate(aPrwarehouseTransfer.getReceivedDate());
			if(aPrwarehouseTransfer.getPrToWarehouseId() != null)
				objPrwarehouseTransfer.setPrToWarehouseId(aPrwarehouseTransfer.getPrToWarehouseId());
			if(aPrwarehouseTransfer.getTransactionNumber() != null)
				objPrwarehouseTransfer.setTransactionNumber(aPrwarehouseTransfer.getTransactionNumber());
			if(aPrwarehouseTransfer.getDesc() != null)
				objPrwarehouseTransfer.setDesc(aPrwarehouseTransfer.getDesc());
			objPrwarehouseTransfer.setCreatedOn(aPrwarehouseTransfer.getCreatedOn());
			objPrwarehouseTransfer.setCreatedByID(aPrwarehouseTransfer.getCreatedByID());
			objPrwarehouseTransfer.setScreenno(objPrwarehouseTransfer.getScreenno());
			aPrWarehouseSession.update(objPrwarehouseTransfer);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return objPrwarehouseTransfer;
	}
	
	public boolean saveTransferLineItems(PrwarehouseTransfer aPrwarehouseTransfer,Prwhtransferdetail aPrwhtransferdetail, String oper) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		Prwhtransferdetail objPrwhtransferdetail = new Prwhtransferdetail();
		Transaction aTransaction;
		BigDecimal newQuantityOrdered = new BigDecimal(0);
		BigDecimal oldQuantityTransfered = new BigDecimal(0);
		try {
			aTransaction = aPrWarehouseSession.beginTransaction();
			aTransaction.begin();
			if("add".equalsIgnoreCase(oper)){
//				updatePrMasterForWTransfer(aPrwhtransferdetail, new BigDecimal(0),
//						aPrwhtransferdetail.getQuantityTransfered(), "add",null);
				/*updatePrWarehouseInventoryForWTransfer(aPrwhtransferdetail, new BigDecimal(0),
						aPrwhtransferdetail.getQuantityTransfered(), "add",null);*/
				Integer id = (Integer) aPrWarehouseSession.save(aPrwhtransferdetail);
				aTransaction.commit();
				aPrwhtransferdetail.setPrTransferDetailId(id);
				updatePrMasterAndPrwhInventory(aPrwarehouseTransfer,aPrwhtransferdetail,"add");
				
				if(id>0){
					PrwarehouseTransfer thePrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, aPrwhtransferdetail.getPrTransferId());
					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
					aTpInventoryLog.setPrMasterID(aPrwhtransferdetail.getPrMasterId());
					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("IT");
					aTpInventoryLog.setTransDecription("IT Created");
					aTpInventoryLog.setTransID(aPrwhtransferdetail.getPrTransferId());
					aTpInventoryLog.setTransDetailID(aPrwhtransferdetail.getPrTransferDetailId());
					aTpInventoryLog.setProductOut(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductIn(new BigDecimal("0.0000"));
					aTpInventoryLog.setUserID(thePrwarehouseTransfer.getCreatedByID());
					aTpInventoryLog.setCreatedOn(thePrwarehouseTransfer.getCreatedOn());
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrToWarehouseId());
					aTpInventoryLog.setProductIn(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductOut(new BigDecimal("0.0000"));
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * FromWarehouseID AllocatedAffected
					 * */
					Session alogSession = itsSessionFactory.openSession();
					BigDecimal qo=aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000");
					Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							qo,BigDecimal.ZERO,"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
							"IT Created",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * ToWarehouseID OrderAffected
					 * */
					theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrToWarehouseId());
					theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrToWarehouseId(), aPrmaster.getPrMasterId());
					prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrToWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							BigDecimal.ZERO,qo,"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
							"IT Created",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					}
			}
			else if("del".equalsIgnoreCase(oper)){
				objPrwhtransferdetail = (Prwhtransferdetail) aPrWarehouseSession.get(Prwhtransferdetail.class, aPrwhtransferdetail.getPrTransferDetailId());
//				updatePrMasterForWTransfer(objPrwhtransferdetail, new BigDecimal(0), new BigDecimal(
//						0), "del",null);
				/*updatePrWarehouseInventoryForWTransfer(aPrwhtransferdetail, new BigDecimal(0), new BigDecimal(
						0), "del",null);*/
				updatePrMasterAndPrwhInventory(aPrwarehouseTransfer,aPrwhtransferdetail,"delete");
				
				if(aPrwhtransferdetail.getPrTransferDetailId()>0){
					PrwarehouseTransfer thePrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, aPrwhtransferdetail.getPrTransferId());
					Prwhtransferdetail thePrwhtransferdetail = (Prwhtransferdetail) aPrWarehouseSession.get(Prwhtransferdetail.class, aPrwhtransferdetail.getPrTransferDetailId());
					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
					aTpInventoryLog.setPrMasterID(thePrwhtransferdetail.getPrMasterId());
					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+thePrwhtransferdetail.getPrMasterId());
					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("IT");
					aTpInventoryLog.setTransDecription("IT Deleted");
					aTpInventoryLog.setTransID(thePrwhtransferdetail.getPrTransferId());
					aTpInventoryLog.setTransDetailID(thePrwhtransferdetail.getPrTransferDetailId());
					aTpInventoryLog.setProductIn(thePrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?thePrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductOut(new BigDecimal("0.0000"));
					aTpInventoryLog.setUserID(thePrwarehouseTransfer.getCreatedByID());
					aTpInventoryLog.setCreatedOn(thePrwarehouseTransfer.getCreatedOn());
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrToWarehouseId());
					aTpInventoryLog.setProductOut(thePrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?thePrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductIn(new BigDecimal("0.0000"));
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * FromWarehouseID AllocatedAffected
					 * Rollback
					 * */
					Session alogSession = itsSessionFactory.openSession();
					BigDecimal qo=thePrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?thePrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000");
					Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							qo.multiply(new BigDecimal(-1)),BigDecimal.ZERO,"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
							"IT Deleted",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * ToWarehouseID OrderAffected
					 * Rollback
					 * */
					theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrToWarehouseId());
					theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrToWarehouseId(), aPrmaster.getPrMasterId());
					prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrToWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							BigDecimal.ZERO,qo.multiply(new BigDecimal(-1)),"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
							"IT Deleted",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					}
				aPrWarehouseSession.delete(objPrwhtransferdetail);
				aTransaction.commit();
			}
			else if("edit".equalsIgnoreCase(oper))
			{
				
				objPrwhtransferdetail = (Prwhtransferdetail) aPrWarehouseSession.get(Prwhtransferdetail.class, aPrwhtransferdetail.getPrTransferDetailId());
				oldQuantityTransfered = objPrwhtransferdetail.getQuantityTransfered();
				newQuantityOrdered = aPrwhtransferdetail.getQuantityTransfered();
//				updatePrMasterForWTransfer(aPrwhtransferdetail, oldQuantityOrdered,
//						newQuantityOrdered, "edit",objPrwhtransferdetail);
				/*updatePrWarehouseInventoryForWTransfer(aPrwhtransferdetail, oldQuantityOrdered,
						newQuantityOrdered, "edit",objPrwhtransferdetail);*/
				objPrwhtransferdetail.setPrMasterId(aPrwhtransferdetail.getPrMasterId());
				objPrwhtransferdetail.setPrTransferId(aPrwhtransferdetail.getPrTransferId());
				objPrwhtransferdetail.setDescription(aPrwhtransferdetail.getDescription());
				objPrwhtransferdetail.setInventoryCost(new BigDecimal(0));
				objPrwhtransferdetail.setQuantityAvailable(aPrwhtransferdetail.getQuantityAvailable());
				objPrwhtransferdetail.setQuantityTransfered(aPrwhtransferdetail.getQuantityTransfered());
				aPrWarehouseSession.update(objPrwhtransferdetail);
				
				if(objPrwhtransferdetail.getPrTransferDetailId()>0 && oldQuantityTransfered.compareTo(newQuantityOrdered)!=0){
					PrwarehouseTransfer thePrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, aPrwhtransferdetail.getPrTransferId());
					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
					aTpInventoryLog.setPrMasterID(aPrwhtransferdetail.getPrMasterId());
					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("IT");
					aTpInventoryLog.setTransDecription("IT Rollback");
					aTpInventoryLog.setTransID(aPrwhtransferdetail.getPrTransferId());
					aTpInventoryLog.setTransDetailID(aPrwhtransferdetail.getPrTransferDetailId());
					aTpInventoryLog.setProductIn(oldQuantityTransfered.compareTo(new BigDecimal("0.0000"))==1?oldQuantityTransfered:new BigDecimal("0.0000"));
					aTpInventoryLog.setProductOut(new BigDecimal("0.0000"));
					aTpInventoryLog.setUserID(thePrwarehouseTransfer.getCreatedByID());
					aTpInventoryLog.setCreatedOn(thePrwarehouseTransfer.getCreatedOn());
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrToWarehouseId());
					aTpInventoryLog.setProductOut(oldQuantityTransfered.compareTo(new BigDecimal("0.0000"))==1?oldQuantityTransfered:new BigDecimal("0.0000"));
					aTpInventoryLog.setProductIn(new BigDecimal("0.0000"));
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * FromWarehouseID AllocatedAffected
					 * Rollback
					 * */
					Session alogSession = itsSessionFactory.openSession();
					BigDecimal qo=oldQuantityTransfered.compareTo(new BigDecimal("0.0000"))==1?oldQuantityTransfered:new BigDecimal("0.0000");
					Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							qo.multiply(new BigDecimal(-1)),BigDecimal.ZERO,"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
							"IT Edited",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * ToWarehouseID OrderAffected
					 * Rollback
					 * */
					theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrToWarehouseId());
					theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrToWarehouseId(), aPrmaster.getPrMasterId());
					prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrToWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							BigDecimal.ZERO,qo.multiply(new BigDecimal(-1)),"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
							"IT Edited",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					updatePrMasterAndPrwhInventory(aPrwarehouseTransfer,aPrwhtransferdetail,"edit");
					
					/*Rollback and InsertNew*/
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("IT");
					aTpInventoryLog.setTransDecription("IT Edited");
					aTpInventoryLog.setProductOut(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductIn(new BigDecimal("0.0000"));
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrToWarehouseId());
					aTpInventoryLog.setProductIn(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductOut(new BigDecimal("0.0000"));
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * FromWarehouseID AllocatedAffected
					 * Insertnew
					 * */
					aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
					BigDecimal qi=aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000");
					theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
					theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							qi,BigDecimal.ZERO,"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qi.compareTo(BigDecimal.ZERO)>0)?qi:BigDecimal.ZERO,
			/*Product in*/(qi.compareTo(BigDecimal.ZERO)<0)?qi.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
							"IT Edited",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * ToWarehouseID OrderAffected
					 * Insertnew
					 * */
					theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrToWarehouseId());
					theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrToWarehouseId(), aPrmaster.getPrMasterId());
					prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrToWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							BigDecimal.ZERO,qi,"IT",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getTransactionNumber().toString(),null ,
			/*Product out*/(qi.compareTo(BigDecimal.ZERO)<0)?qi.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
			/*Product in*/(qi.compareTo(BigDecimal.ZERO)>0)?qi:BigDecimal.ZERO ,
							"IT Edited",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					}
				
				aTransaction.commit();
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return true;
	}
	
	public String updatePrMasterForWTransfer(Prwhtransferdetail objCusodetail,
			BigDecimal oldQuantityOrdered, BigDecimal newQuantityOrdered,
			String operation, Prwhtransferdetail oldobjCusodetail) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			if ("edit".equalsIgnoreCase(operation)) {
				if (objCusodetail.getPrMasterId().equals(
						oldobjCusodetail.getPrMasterId())) {
					Prmaster objPrmaster = (Prmaster) aVePOSession.get(
							Prmaster.class,
							oldobjCusodetail.getPrMasterId());
					
					
					BigDecimal oldAllocated = objPrmaster.getInventoryAllocated();
					BigDecimal newValue = oldAllocated.subtract(oldQuantityOrdered);
					objPrmaster.setInventoryAllocated(newValue.add(newQuantityOrdered));					
					aVePOSession.update(objPrmaster);
					aTransaction.commit();
					
					
					
				} else {
					Prmaster objPrmaster = (Prmaster) aVePOSession.get(
							Prmaster.class,
							oldobjCusodetail.getPrMasterId());
					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated.subtract(oldobjCusodetail.getQuantityTransfered()));	
					aVePOSession.update(objPrmaster);
					aTransaction.commit();
					aTransaction.begin();
					objPrmaster = (Prmaster) aVePOSession.get(
							Prmaster.class,
							objCusodetail.getPrMasterId());
					allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated.add(newQuantityOrdered));
					aVePOSession.update(objPrmaster);
					aTransaction.commit();
				}				
			}			
			else if ("add".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, objCusodetail.getPrMasterId());
				BigDecimal allocated = objPrmaster.getInventoryAllocated();				
				objPrmaster.setInventoryAllocated(allocated.add(newQuantityOrdered));
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
			} else if ("del".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, objCusodetail.getPrMasterId());
				if(objPrmaster.getInventoryAllocated()!=null &&objCusodetail.getQuantityTransfered()!=null ){
					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated.subtract(objCusodetail.getQuantityTransfered()));	
				}
							
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
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
	/*public String updatePrWarehouseInventoryForWTransfer(Prwhtransferdetail objCusodetail,BigDecimal oldQuantityOrdered, BigDecimal newQuantityOrdered, String oper, Prwhtransferdetail oldobjCusodetail)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Cuso aCuso = (Cuso) aVePOSession.get(Cuso.class,
					objCusodetail.getCuSoid());
			Integer sPrWarehouseID = 0;
			BigDecimal inventoryAllocated = new BigDecimal(0);
			Integer prWarehouseInventoryID = 0;
			String sQuery = "SELECT prFromWarehouseID FROM cuSO cuso WHERE cuSOID = "+aCuso.getCuSoid();
			Query query  = aVePOSession.createSQLQuery(sQuery);
			if(query.list().size() > 0)
				sPrWarehouseID = (Integer)query.list().get(0);
			
			String sQuery1 = "SELECT inventory.InventoryAllocated,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "+objCusodetail.getPrMasterId()+" AND prWarehouseID = "+sPrWarehouseID;
			query  = aVePOSession.createSQLQuery(sQuery1);
			Iterator<?> iterator = query.list().iterator();
			while(iterator.hasNext())
			{
				Object[] object = (Object[]) iterator.next();
				onOrder.add((BigDecimal)object[0]);
			}
			if(query.list().size() > 0){
				Object[] object = (Object[])query.list().get(0);
				inventoryAllocated = (BigDecimal)object[0];
				prWarehouseInventoryID = (Integer)object[1];
			}
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class,
					prWarehouseInventoryID);
			BigDecimal allocated = objPrwarehouseinventory.getInventoryAllocated();
			if("del".equalsIgnoreCase(oper)){
				objPrwarehouseinventory.setInventoryAllocated(allocated.subtract(objCusodetail
					.getQuantityOrdered()));
			aVePOSession.update(objPrwarehouseinventory);
			aTransaction.commit();
			}
			else if("add".equalsIgnoreCase(oper)){
				objPrwarehouseinventory.setInventoryAllocated(allocated.add(objCusodetail
						.getQuantityOrdered()));
			aVePOSession.update(objPrwarehouseinventory);
			aTransaction.commit();
			}			
			else if ("edit".equalsIgnoreCase(oper)) {
				if (oldobjCusodetail.getPrMasterId().equals(
						objCusodetail.getPrMasterId())) {
					
					BigDecimal newAllocated = allocated.subtract(oldobjCusodetail.getQuantityOrdered());
					BigDecimal newOnAllocated = newAllocated.add(objCusodetail.getQuantityOrdered());
					objPrwarehouseinventory.setInventoryAllocated(newOnAllocated);					
					aVePOSession.update(objPrwarehouseinventory);
					aTransaction.commit();
					
				} else {
					
					BigDecimal newAllocated = allocated.subtract(oldobjCusodetail.getQuantityOrdered());
					objPrwarehouseinventory.setInventoryAllocated(newAllocated);	
					aVePOSession.update(objPrwarehouseinventory);
					aTransaction.commit();
								
					
					aTransaction.begin();
					String sQuery2 = "SELECT inventory.InventoryAllocated,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "+objCusodetail.getPrMasterId()+" AND prWarehouseID = "+sPrWarehouseID;
					Query query1  = aVePOSession.createSQLQuery(sQuery2);
					
					if(query1.list().size() > 0){
						Object[] object = (Object[])query1.list().get(0);
						inventoryAllocated = (BigDecimal)object[0];
						prWarehouseInventoryID = (Integer)object[1];
					}
					Prwarehouseinventory objPrwarehouseinventory1 = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class,
							prWarehouseInventoryID);
					allocated = objPrwarehouseinventory1.getInventoryAllocated();
					
					objPrwarehouseinventory1.setInventoryAllocated(allocated
							.add(objCusodetail.getQuantityOrdered()));
					aVePOSession.update(objPrwarehouseinventory1);
					aTransaction.commit();
				}
				
			}
			
			
			
			
			else if ("edit".equalsIgnoreCase(oper)) {				
				BigDecimal order = objPrwarehouseinventory.getInventoryAllocated();
				BigDecimal newValue = order.subtract(oldQuantityOrdered);
				BigDecimal allocatedNew = newValue.add(newQuantityOrdered);						
				objPrwarehouseinventory.setInventoryAllocated(allocatedNew);				
				
			}
			aVePOSession.update(objPrwarehouseinventory);
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
	
	/*
	 * Strung receiveOrUndo = del means Subtract PrMaster else Add PrMaster
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String updatePrMasterForReceiveItemsWTransfer(Integer prTransferId, String receiveOrUndo,int UserID,String username) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		PrwarehouseTransfer objPrwarehouseTransfer = null;
		PrwarehouseTransfer aPrwarehouseTransfer = null;
		BigDecimal totalAmount = new BigDecimal(0);
		
		try {
			if("del".equalsIgnoreCase(receiveOrUndo)){
			aTransaction = aVePOSession.beginTransaction();
			aPrwarehouseTransfer = (PrwarehouseTransfer)aVePOSession.get(PrwarehouseTransfer.class,prTransferId);
			java.util.Date now = new java.util.Date();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String createdDate = df.format(now);
			if ((createdDate != null && createdDate != "")) {
				aPrwarehouseTransfer.setReceivedDate(DateUtils.parseDate(createdDate,
						new String[] { "MM/dd/yyyy" }));
			}
			
			aVePOSession.update(aPrwarehouseTransfer);
			
			aTransaction.commit();
			}
			else if("add".equalsIgnoreCase(receiveOrUndo))
			{
				aTransaction = aVePOSession.beginTransaction();
				aPrwarehouseTransfer = (PrwarehouseTransfer)aVePOSession.get(PrwarehouseTransfer.class,prTransferId);				
				aPrwarehouseTransfer.setReceivedDate(null);				
				aVePOSession.update(aPrwarehouseTransfer);
				aTransaction.commit();
			}
		
			
			List<Prwhtransferdetail> alPrwhtransferdetail = (List<Prwhtransferdetail>)aVePOSession.createCriteria(Prwhtransferdetail.class).add(Restrictions.eq("prTransferId", prTransferId)).list();
			if(alPrwhtransferdetail.size() > 0)
			{
				for(Prwhtransferdetail objPrwhtransferdetail : alPrwhtransferdetail)
				{
					//updatePrMasterForReceiveItemsWTransfer(objPrwhtransferdetail.getPrMasterId(), objPrwhtransferdetail.getQuantityTransfered(),receiveOrUndo);
					updatePrWarehouseInventoryForWTransfer(aPrwarehouseTransfer.getPrFromWarehouseId(), aPrwarehouseTransfer.getPrToWarehouseId(),objPrwhtransferdetail.getPrMasterId(), objPrwhtransferdetail.getQuantityTransfered(),receiveOrUndo);
					updatePrMasterForWTransfer(objPrwhtransferdetail.getPrMasterId(),objPrwhtransferdetail.getQuantityTransfered(),receiveOrUndo);
					totalAmount=totalAmount.add(amountCalculation(objPrwhtransferdetail));
					
					
					
					
								if("del".equalsIgnoreCase(receiveOrUndo)){
									
									
									/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 * FromWarehouseID AllocatedAffected
									 * Rollback
									 * */
									Session alogSession = itsSessionFactory.openSession();
									Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+objPrwhtransferdetail.getPrMasterId());
									BigDecimal qo=objPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?objPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000");
									Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,aPrwarehouseTransfer.getPrFromWarehouseId());
									Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aPrwarehouseTransfer.getPrFromWarehouseId(), objPrwhtransferdetail.getPrMasterId());
									TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
											objPrwhtransferdetail.getPrMasterId(),aPrmaster.getItemCode(),aPrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
											qo.multiply(new BigDecimal(-1)),BigDecimal.ZERO,"IT",aPrwarehouseTransfer.getPrTransferId(),objPrwhtransferdetail.getPrTransferDetailId(),aPrwarehouseTransfer.getTransactionNumber().toString(),null ,
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
											"IT ReceiveItems",UserID,username,
											new java.util.Date());
									itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
									/*Code Ends*/
									
									/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 * ToWarehouseID OrderAffected
									 * Rollback
									 * */
									theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,aPrwarehouseTransfer.getPrToWarehouseId());
									theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aPrwarehouseTransfer.getPrToWarehouseId(), aPrmaster.getPrMasterId());
									prmatpInventoryLogMstr=new  TpInventoryLogMaster(
											aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aPrwarehouseTransfer.getPrToWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
											BigDecimal.ZERO,qo.multiply(new BigDecimal(-1)),"IT",aPrwarehouseTransfer.getPrTransferId(),objPrwhtransferdetail.getPrTransferDetailId(),aPrwarehouseTransfer.getTransactionNumber().toString(),null ,
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
											"IT ReceiveItems",UserID,username,
											new java.util.Date());
									itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
									/*Code Ends*/
									
								}else{
									/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 * FromWarehouseID AllocatedAffected
									 * Rollback
									 * */
									Session alogSession = itsSessionFactory.openSession();
									Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+objPrwhtransferdetail.getPrMasterId());
									BigDecimal qo=objPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?objPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000");
									Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,aPrwarehouseTransfer.getPrFromWarehouseId());
									Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aPrwarehouseTransfer.getPrFromWarehouseId(), objPrwhtransferdetail.getPrMasterId());
									TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
											objPrwhtransferdetail.getPrMasterId(),aPrmaster.getItemCode(),aPrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
											qo,BigDecimal.ZERO,"IT",aPrwarehouseTransfer.getPrTransferId(),objPrwhtransferdetail.getPrTransferDetailId(),aPrwarehouseTransfer.getTransactionNumber().toString(),null ,
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
											"IT UNDO",UserID,username,
											new java.util.Date());
									itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
									/*Code Ends*/
									
									/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 * ToWarehouseID OrderAffected
									 * Rollback
									 * */
									theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,aPrwarehouseTransfer.getPrToWarehouseId());
									theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aPrwarehouseTransfer.getPrToWarehouseId(), aPrmaster.getPrMasterId());
									prmatpInventoryLogMstr=new  TpInventoryLogMaster(
											aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aPrwarehouseTransfer.getPrToWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
											BigDecimal.ZERO,qo,"IT",aPrwarehouseTransfer.getPrTransferId(),objPrwhtransferdetail.getPrTransferDetailId(),aPrwarehouseTransfer.getTransactionNumber().toString(),null ,
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
											"IT UNDO",UserID,username,
											new java.util.Date());
									itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
									/*Code Ends*/
								}
				}
				if (totalAmount.compareTo(BigDecimal.ZERO) > 0)
				AddGeneralLedgerProcess(aPrwarehouseTransfer,totalAmount,username);
			}
			objPrwarehouseTransfer = (PrwarehouseTransfer)aVePOSession.get(PrwarehouseTransfer.class,prTransferId);
			
		}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		String formatedDate = "";
		if(objPrwarehouseTransfer.getReceivedDate() != null){
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		java.util.Date date = null;
		try {
			date = (java.util.Date)formatter.parse(objPrwarehouseTransfer.getReceivedDate().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		formatedDate = + (cal.get(Calendar.MONTH) + 1)+ "/"+cal.get(Calendar.DATE)  + "/" + cal.get(Calendar.YEAR);
		}
		return formatedDate;
	}
	
	public BigDecimal amountCalculation(Prwhtransferdetail objPrwhtransferdetail)
	{
		Session aopenSession = itsSessionFactory.openSession();
		BigDecimal TotalCost = new BigDecimal(0.0000);
		try{
		Prmaster objPrmaster = (Prmaster) aopenSession.get(Prmaster.class, objPrwhtransferdetail.getPrMasterId());
		BigDecimal prcost=objPrmaster.getAverageCost()==null?new BigDecimal(0.0000):objPrmaster.getAverageCost();
		System.out.println("prcost "+prcost);
		BigDecimal prmul=objPrmaster.getPOMult();
		BigDecimal prQty=objPrwhtransferdetail.getQuantityTransfered();
		System.out.println("prQty "+prQty);
		TotalCost=prcost.multiply(prQty);
		if(prmul!=null && prmul.compareTo(BigDecimal.ZERO)!=0){
			TotalCost=TotalCost.multiply(prmul);
		}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		finally {
			aopenSession.flush();
			aopenSession.close();
		}
		return TotalCost;
	}
	
	public void AddGeneralLedgerProcess(PrwarehouseTransfer aPrwarehouseTransfer,BigDecimal totAmount,String username) throws BankingException
	 {
		Session aopenSession = null;
		try {
			aopenSession = itsSessionFactory.openSession();
			Prwarehouse fromPrwarehouse=null;
			Prwarehouse toPrwarehouse=null;
			fromPrwarehouse = (Prwarehouse)aopenSession.get(Prwarehouse.class,aPrwarehouseTransfer.getPrFromWarehouseId());
			toPrwarehouse = (Prwarehouse)aopenSession.get(Prwarehouse.class,aPrwarehouseTransfer.getPrToWarehouseId());
			
			
			Coaccount fromCoAccountIdapdetails = callgltransaction.getCoaccountDetailsBasedoncoAccountid(fromPrwarehouse.getCoAccountIdasset());
			Coaccount toCoAccountIdapdetails =callgltransaction.getCoaccountDetailsBasedoncoAccountid(toPrwarehouse.getCoAccountIdasset());
			

			Cofiscalperiod aCofiscalperiod = callgltransaction.getCofiscalPeriodDetail();
			Cofiscalyear aCofiscalyear = callgltransaction.getCofiscalYearDetail();
			Coledgersource aColedgersource = callgltransaction.getColedgersourceDetail("IT");

			GlTransaction glTransaction = new GlTransaction();

			// period
			glTransaction
					.setCoFiscalPeriodId(aCofiscalperiod.getCoFiscalPeriodId());
			glTransaction.setPeriod(aCofiscalperiod.getPeriod());
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
			java.util.Date now = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(now.getTime());
			glTransaction.setEntrydate(sqlDate);
			
			glTransaction.setEnteredBy(username);
			
			if(aPrwarehouseTransfer.getReceivedDate()!=null)
			glTransaction.setTransactionDate(aPrwarehouseTransfer.getReceivedDate());
			else
			glTransaction.setTransactionDate(sqlDate);
			
		/*	aTransaction = aopenSession.beginTransaction();
			Prmaster objPrmaster = (Prmaster) aopenSession.get(Prmaster.class, objPrwhtransferdetail.getPrMasterId());
			BigDecimal prcost=objPrmaster.getLastCost();
			BigDecimal prmul=objPrmaster.getPOMult();
			BigDecimal prQty=objPrwhtransferdetail.getQuantityTransfered();
			BigDecimal TotalCost=prcost.multiply(prQty);
			if(prmul!=null && prmul.compareTo(BigDecimal.ZERO)!=0){
				TotalCost=TotalCost.multiply(prmul);
			}
			*/
			
			
			/**First insert for from warehousep debit**/
			glTransaction.setTransactionDesc("xfer: "+aPrwarehouseTransfer.getPrTransferId());
			glTransaction.setPoNumber(aPrwarehouseTransfer.getDesc());
			BigDecimal dBorCrAmount= new BigDecimal(0);
			glTransaction.setCoAccountId(fromPrwarehouse.getCoAccountIdasset());
			glTransaction.setCoAccountNumber(fromCoAccountIdapdetails.getNumber());
			glTransaction.setDebit( totAmount);
			glTransaction.setCredit(new BigDecimal(0));
			Integer gltransactionid=callgltransaction.saveGltransactionTable(glTransaction);
			
			GlLinkage glLinkage = new GlLinkage();
			glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(gltransactionid) 	 	;
			glLinkage.setEntryDate(sqlDate);
			glLinkage.setVeBillID(aPrwarehouseTransfer.getPrTransferId());
			glLinkage.setStatus(0);
			callgltransaction.saveGlLinkageTable(glLinkage);
			
			
			/**First insert for to warehouse credit**/
			glTransaction.setTransactionDesc("xfer: "+aPrwarehouseTransfer.getPrTransferId());
			glTransaction.setPoNumber(aPrwarehouseTransfer.getDesc());
			glTransaction.setCoAccountId(toPrwarehouse.getCoAccountIdasset());
			glTransaction.setCoAccountNumber(toCoAccountIdapdetails.getNumber());
			glTransaction.setCredit( totAmount);
			glTransaction.setDebit( new BigDecimal(0));
			gltransactionid=callgltransaction.saveGltransactionTable(glTransaction);
			
			glLinkage = new GlLinkage();
			glLinkage.setCoLedgerSourceId(aColedgersource.getCoLedgerSourceId());
			glLinkage.setGlTransactionId(gltransactionid) 	 	;
			glLinkage.setEntryDate(sqlDate);
			glLinkage.setVeBillID(aPrwarehouseTransfer.getPrTransferId());
			glLinkage.setStatus(0);
			callgltransaction.saveGlLinkageTable(glLinkage);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			aopenSession.flush();
			aopenSession.close();
		}
		
	}
	
	public String updatePrMasterForWTransfer(Integer prMasterID,BigDecimal qtyTransfered, String operation) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			if ("add".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, prMasterID);
				BigDecimal allocated = objPrmaster.getInventoryAllocated()!=null?objPrmaster.getInventoryAllocated():BigDecimal.ZERO;	
				objPrmaster.setInventoryAllocated(allocated.add(qtyTransfered));
				BigDecimal onorder=objPrmaster.getInventoryOnOrder()!=null?objPrmaster.getInventoryOnOrder():BigDecimal.ZERO;
				objPrmaster.setInventoryOnOrder(onorder.add(qtyTransfered));
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
			} else if ("del".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, prMasterID);
				BigDecimal allocated = objPrmaster.getInventoryAllocated()!=null?objPrmaster.getInventoryAllocated():BigDecimal.ZERO;
				BigDecimal onorder=objPrmaster.getInventoryOnOrder()!=null?objPrmaster.getInventoryOnOrder():BigDecimal.ZERO;
				if (allocated.compareTo(qtyTransfered) == 1 || allocated.compareTo(qtyTransfered) == 0)
				objPrmaster.setInventoryAllocated(allocated.subtract(qtyTransfered));
				if (onorder.compareTo(qtyTransfered) == 1 || onorder.compareTo(qtyTransfered) == 0)
					objPrmaster.setInventoryOnOrder(onorder.subtract(qtyTransfered));
				//objPrmaster.setInventoryOnHand(objPrmaster.getInventoryOnHand().add(allocated));
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
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
	
	
	
	public String updatePrMasterForReceiveItemsWTransfer(Integer prMasterID,BigDecimal qtyTransfered, String operation) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();						
			if ("add".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, prMasterID);
				BigDecimal allocated = objPrmaster.getInventoryAllocated();				
				//objPrmaster.setInventoryAllocated(allocated.add(qtyTransfered));
				//objPrmaster.setInventoryAllocated(qtyTransfered);
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
			} else if ("del".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, prMasterID);
				BigDecimal allocated = objPrmaster.getInventoryAllocated();
				//objPrmaster.setInventoryAllocated(allocated.subtract(qtyTransfered));
				//objPrmaster.setInventoryOnHand(objPrmaster.getInventoryOnHand().add(allocated));
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
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
	
	public String updatePrWarehouseInventoryForWTransfer(Integer fromWarehouse,Integer toWarehouse, Integer prMasterID, BigDecimal qty, String oper)
	throws JobException {
			Session aVePOSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			String sQuery ="";
			try {
				aTransaction = aVePOSession.beginTransaction();
				aTransaction.begin();
				
				Integer sPrWarehouseID = 0;
				if("add".equalsIgnoreCase(oper))
				{
					 sQuery = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+prMasterID+" AND prWarehouseID = "+fromWarehouse;
					Query query  = aVePOSession.createSQLQuery(sQuery);
					if(query.list().size() > 0)
						sPrWarehouseID = (Integer)query.list().get(0);
					
					Prwarehouseinventory aaPrwarehouseinventory = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class, sPrWarehouseID);
					aaPrwarehouseinventory.setInventoryAllocated(qty.add(aaPrwarehouseinventory.getInventoryAllocated()));
					aaPrwarehouseinventory.setInventoryOnHand(qty.add(aaPrwarehouseinventory.getInventoryOnHand()));
					aTransaction.commit();
					aTransaction.begin();
					sQuery = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+prMasterID+" AND prWarehouseID = "+toWarehouse;
					query  = aVePOSession.createSQLQuery(sQuery);
					if(query.list().size() > 0)
						sPrWarehouseID = (Integer)query.list().get(0);
					
					aaPrwarehouseinventory = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class, sPrWarehouseID);
					BigDecimal onHand = aaPrwarehouseinventory.getInventoryOnHand();
					aaPrwarehouseinventory.setInventoryOnHand(onHand.subtract(qty));
					aaPrwarehouseinventory.setInventoryOnOrder(qty.add(aaPrwarehouseinventory.getInventoryOnOrder()));
					aTransaction.commit();
				}
				else if("del".equalsIgnoreCase(oper))
				{
					 sQuery = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+prMasterID+" AND prWarehouseID = "+fromWarehouse;
					Query query  = aVePOSession.createSQLQuery(sQuery);
					if(query.list().size() > 0)
						sPrWarehouseID = (Integer)query.list().get(0);
					
					Prwarehouseinventory aaPrwarehouseinventory = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class, sPrWarehouseID);
					BigDecimal onHand = aaPrwarehouseinventory.getInventoryOnHand();
					aaPrwarehouseinventory.setInventoryOnHand(onHand.subtract(qty));
					aaPrwarehouseinventory.setInventoryAllocated(aaPrwarehouseinventory.getInventoryAllocated().subtract(qty));
					aTransaction.commit();
					aTransaction.begin();
					sQuery = "SELECT prWarehouseInventoryID FROM prWarehouseInventory WHERE prMasterID = "+prMasterID+" AND prWarehouseID = "+toWarehouse;
					query  = aVePOSession.createSQLQuery(sQuery);
					if(query.list().size() > 0)
						sPrWarehouseID = (Integer)query.list().get(0);
					
					aaPrwarehouseinventory = (Prwarehouseinventory) aVePOSession.get(Prwarehouseinventory.class, sPrWarehouseID);
					aaPrwarehouseinventory.setInventoryOnHand(qty.add(aaPrwarehouseinventory.getInventoryOnHand()));
					aaPrwarehouseinventory.setInventoryOnOrder(aaPrwarehouseinventory.getInventoryOnOrder().subtract(qty));
					aTransaction.commit();
				}
								
				
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getCause()
						.getMessage(), e);
				throw aJobException;
			} finally {
				aVePOSession.flush();
				aVePOSession.close();
				sQuery=null;
			}
			return "Success";
}
	@Override
	public PrwarehouseTransfer copyTransferDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		PrwarehouseTransfer objPrwarehouseTransfer = new PrwarehouseTransfer();
		Transaction aTransaction;
		try {
			aTransaction = aPrWarehouseSession.beginTransaction();
			aTransaction.begin();
			Integer id = (Integer) aPrWarehouseSession.save(aPrwarehouseTransfer);			
			objPrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, id);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return objPrwarehouseTransfer;
	}
	
	/*@Override
	public PrwarehouseTransfer copyTransferLineDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		PrwarehouseTransfer objPrwarehouseTransfer = new PrwarehouseTransfer();
		Transaction aTransaction;
		try {
			aTransaction = aPrWarehouseSession.beginTransaction();
			aTransaction.begin();
			Integer id = (Integer) aPrWarehouseSession.save(aPrwarehouseTransfer);			
			System.out.println("saveTransferDetails---->"+id);
			objPrwarehouseTransfer = (PrwarehouseTransfer) aPrWarehouseSession.get(PrwarehouseTransfer.class, id);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return objPrwarehouseTransfer;
	}*/
	@Override
	public String copyTransferLineDetails(Integer prTransferID, Integer newPrTransferID)
			throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		String aInsertInvoiceQuery ="";
		PrwarehouseTransfer aPrwarehouseTransfer = null;
		Prwhtransferdetail thePrwhtransferdetail = null;
		Integer newPrTransferDetailID = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();			
			
			//aPrwhtransferdetail = (Prwhtransferdetail) aSession.get(Prwhtransferdetail.class, prTransferID);
			List<Prwhtransferdetail> alPrwhtransferdetail = (List<Prwhtransferdetail>)aSession.createCriteria(Prwhtransferdetail.class).add(Restrictions.eq("prTransferId", prTransferID)).list();
			if(alPrwhtransferdetail.size() > 0)
			{
				for(Prwhtransferdetail aPrwhtransferdetail : alPrwhtransferdetail)
				{
					thePrwhtransferdetail = new Prwhtransferdetail();	
					thePrwhtransferdetail.setPrTransferId(newPrTransferID);
					thePrwhtransferdetail.setPrMasterId(aPrwhtransferdetail.getPrMasterId());
					thePrwhtransferdetail.setDescription(aPrwhtransferdetail.getDescription());
					thePrwhtransferdetail.setQuantityTransfered(aPrwhtransferdetail.getQuantityTransfered());
					thePrwhtransferdetail.setInventoryCost(aPrwhtransferdetail.getInventoryCost());
					thePrwhtransferdetail.setQuantityAvailable(aPrwhtransferdetail.getQuantityAvailable());
					thePrwhtransferdetail.setDifference(aPrwhtransferdetail.getDifference());
					thePrwhtransferdetail.setItemCode(aPrwhtransferdetail.getItemCode());
					thePrwhtransferdetail.setUserId(aPrwhtransferdetail.getUserId());
					thePrwhtransferdetail.setUserName(aPrwhtransferdetail.getUserName());
					newPrTransferDetailID = (Integer) aSession.save(thePrwhtransferdetail);
			/*StringBuffer insertInvoiceQuery = new StringBuffer( // Stringbuffer
					// creates less
					// no of
					// objects.
					"INSERT prWHtransferDetail")
					.append("(prTransferID,prMasterID, Description,")
					.append("QuantityTransfered, ").append("InventoryCost, ")
					.append("QuantityAvailable, ").append(" ItemCode ").append(") ").append("SELECT  ")
					.append(newPrTransferID).append(",prMasterID")
					.append(" ,Description ").append(" ,QuantityTransfered ")
					.append(",InventoryCost ").append(",QuantityAvailable ")
					.append(",ItemCode ").append("FROM prWHtransferDetail ")
					.append("WHERE prTransferID = ").append(prTransferID).append(";");
			 aInsertInvoiceQuery = insertInvoiceQuery.toString();
			aSession.createSQLQuery(aInsertInvoiceQuery).executeUpdate();*/
			aTransaction.commit();
			aPrwarehouseTransfer = (PrwarehouseTransfer) aSession.get(PrwarehouseTransfer.class, newPrTransferID);
			Prwhtransferdetail bPrwhtransferdetail = (Prwhtransferdetail) aSession.get(Prwhtransferdetail.class, newPrTransferDetailID);
			updatePrMasterAndPrwhInventory(aPrwarehouseTransfer,bPrwhtransferdetail,"add");
				}
			}
			/*updatePrMasterFirst(cuInvoiceID);
			updatePrWarehouseInventoryForCuInvoiceFirst(cuInvoiceID);*/
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aInsertInvoiceQuery = null;
		}
		return "success";
	}
	
	public int getInventoryRecordCount(String itemCode) throws InventoryException {
		Session aSession = null;
		BigInteger aTotalCount = null;
		String aStr = "SELECT count(*)"; 
		if(itemCode != null)
			aStr = aStr+" FROM prMaster WHERE Description NOT LIKE '%DO NOT %' AND ItemCode LIKE '%"+itemCode+"%' ORDER BY prMaster.ItemCode ";
		else
			aStr = aStr+" FROM prMaster WHERE Description NOT LIKE '%DO NOT %' ORDER BY prMaster.ItemCode ";
		
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aStr = null;
		}
		return aTotalCount.intValue();	
	}
	
	public int getInventoryRecordCount(Integer warehouseId, String theSearchString, Integer inactive,String column,String sortBy) throws InventoryException {
		Session aSession = null;
		BigInteger aTotalCount = null;
		String itemcodeCondn="";
	/*	if(itemCode != null)
			itemcodeCondn=" OR pr.ItemCode LIKE '%"+itemCode+"%'";*/
		if(itemcodeCondn.length()>0){
			itemcodeCondn=" AND ( pr.Description LIKE '%"+theSearchString+"%' OR" +
					" pr.ItemCode LIKE '%"+theSearchString+"%' OR" +
					" pc.Description LIKE '%"+theSearchString+"%' OR" +
					" rx.Name LIKE '%"+theSearchString+"%') ";
		}
		
		String inactiveCondn = "",sortByOption ="",warehouseCondn="";
		if(inactive==null || inactive==0){
			inactiveCondn ="pr.Inactive=0 ";
		}else{
			inactiveCondn ="pr.Inactive=1 ";
		}
		//	aStr = aStr+" FROM prMaster WHERE Description NOT LIKE '%DO NOT %' AND ItemCode LIKE '%"+itemCode+"%' ORDER BY prMaster.ItemCode LIMIT " + theFrom + ", " + theTo;
		//else
			//aStr = aStr+" FROM prMaster WHERE Description NOT LIKE '%DO NOT %' ORDER BY prMaster.ItemCode LIMIT " + theFrom + ", " + theTo;
		if(warehouseId==-1){
			//warehouseCondn = " 1 ";
			warehouseCondn = "";
		}else{
			warehouseCondn = " pw.prWarehouseID = "+warehouseId+" AND ";
		}
		/*if(column.equalsIgnoreCase("description")){
			sortByOption=" pr.Description ";
		}else if(column.equalsIgnoreCase("prDepartment")){
			sortByOption=" pd.Description ";
		}else if(column.equalsIgnoreCase("prCategory")){
			sortByOption=" pc.Description ";
		}else if(column.equalsIgnoreCase("vendorName")){
			sortByOption=" rx.Name ";
		}else if(column.equalsIgnoreCase("itemCode")){
			sortByOption=" pr.ItemCode ";
		}else if(column.equalsIgnoreCase("inventoryOnHand")){
			sortByOption=" pw.InventoryOnHand ";
		}else if(column.equalsIgnoreCase("lastCost")){
			sortByOption=" pr.LastCost ";
		}else if(column.equalsIgnoreCase("totalCost")){
			sortByOption=" TotalCost ";
		}*/
		  
		//removed condition pr.Description NOT LIKE '%DO NOT %' AND
		String aStr="SELECT COUNT(DISTINCT(pr.prMasterId)) FROM prMaster pr LEFT JOIN prWarehouseInventory pw ON pr.prMasterId = pw.prMasterID LEFT JOIN prDepartment pd ON pd.prDepartmentID=pr.prDepartmentID LEFT JOIN prCategory pc ON pc.prCategoryID = pr.prCategoryID LEFT JOIN rxMaster rx ON rx.rxMasterID = pr.rxMasterIDPrimaryVendor WHERE "+
				warehouseCondn+" "+inactiveCondn +itemcodeCondn/*+" ORDER BY "+sortByOption+sortBy.toUpperCase()+" "*/;
		System.out.println(aStr);
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			itemcodeCondn = null;
			aStr = null;
		}
		return aTotalCount.intValue();	
	}
/**
 * Created by : Praveenkumar
 * Date : 2014-09-19
 * Purpose: Calculate warehouse cost replicated by turbo rep app
 * 
 */
	@Override
	public BigDecimal getWarehouseCost(Integer prMasterID)
			throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		BigDecimal warehouseCost = new BigDecimal(0);
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
		Prmaster aPrmaster = (Prmaster) aSession.get(Prmaster.class, prMasterID);
		
		Sysvariable aSysvariable= (Sysvariable) aSession.get(Sysvariable.class, 1020000);
		BigDecimal inventoryMarkup = aSysvariable.getValueCurrency();
		//BigDecimal avgcost = aPrmaster.getLastCost().setScale(2, BigDecimal.ROUND_DOWN).multiply(aPrmaster.getPOMult().setScale(2, BigDecimal.ROUND_DOWN));
		BigDecimal avgcost = aPrmaster.getAverageCost()==null?new BigDecimal("0.0000").setScale(2, BigDecimal.ROUND_FLOOR):aPrmaster.getAverageCost().setScale(2, BigDecimal.ROUND_FLOOR);//.multiply(aPrmaster.getPOMult().setScale(2, BigDecimal.ROUND_DOWN));
		if(aPrmaster .getPrCategoryId() != null && aPrmaster .getPrCategoryId() != -1){

			PrCategory aPrCategory=getprCategories(aPrmaster .getPrCategoryId());
		if(aPrCategory!=null){
			if(aPrCategory.getMarkupAmount().compareTo(new BigDecimal(0))==1){
				inventoryMarkup =aPrCategory.getMarkupAmount().setScale(2, BigDecimal.ROUND_FLOOR); 
			}
		}
		}
		
		warehouseCost = avgcost.multiply(inventoryMarkup).setScale(2, BigDecimal.ROUND_FLOOR);
//		aPrmaster.get
		aTransaction.commit();
		itsLogger.info("WareHouseCost::"+warehouseCost+" MarkupAmount::"+inventoryMarkup+" AverageCost::"+avgcost);
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			return warehouseCost;
			//throw aJobException;
			
		} finally {
			aSession.flush();
			aSession.close();
		}
		return warehouseCost;
	}
	
	@Override
	public PrCategory getprcategorymarkup(Integer theprCategoryID) throws InventoryException {
		Session aSession = null;
		PrCategory prCategory = null;
		try {
			aSession = itsSessionFactory.openSession();
			prCategory = (PrCategory) aSession.get(PrCategory.class, theprCategoryID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  prCategory;
	}
	
	public boolean saveInventorysettings(ArrayList<Sysvariable> sysvariablelist) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Transaction aTransaction1;
		try {
			
			for(Sysvariable listSysvariable:sysvariablelist){
				Sysvariable aSysvariable=null;
				aTransaction = aPrWarehouseSession.beginTransaction();
				aTransaction.begin();
				aSysvariable = (Sysvariable) aPrWarehouseSession.get(Sysvariable.class, listSysvariable.getSysVariableId());
				itsLogger.info(aSysvariable);
				if(aSysvariable!=null){
					aSysvariable.setValueLong(listSysvariable.getValueLong());
					aSysvariable.setValueString(listSysvariable.getValueString());
					aPrWarehouseSession.update(aSysvariable);
					aTransaction.commit();
				}else{
					aTransaction1 = aPrWarehouseSession.beginTransaction();
					aTransaction1.begin();
					aPrWarehouseSession.save(listSysvariable);
					aTransaction1.commit();
				}
			
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return true;
	}
	
	@Override
	public String updateSysVariableSequence(Integer sysVariableID) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String newInvoiceNumber = "";
		try {
				Sysvariable aSysvariable=null;
				aTransaction = aPrWarehouseSession.beginTransaction();
				aTransaction.begin();
				aSysvariable = (Sysvariable) aPrWarehouseSession.get(Sysvariable.class, sysVariableID);
				itsLogger.info(aSysvariable);
				if(aSysvariable!=null){
					Integer newIncValue = Integer.parseInt(aSysvariable.getValueString());
					//newIncValue = newIncValue+1;
					newInvoiceNumber = newIncValue+"";
					aSysvariable.setValueString((newIncValue+1)+"");
					aPrWarehouseSession.update(aSysvariable);
					aTransaction.commit();
				}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return newInvoiceNumber;
	}
	
	@Override
	public PrCategory getprCategories(Integer prcategoryId) throws InventoryException {
		String aDeptAccSelectQuery="SELECT prCategoryID,Description,InActive,MarkupCost,MarkupAmount FROM prCategory WHERE prCategoryID="+prcategoryId +" And MarkupCost=1";
		Session aSession=null;
		
		PrCategory aPrCategory=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aDeptAccSelectQuery);
			Iterator<?> iterator = query.list().iterator();
			if(iterator.hasNext()) {
				aPrCategory = new  PrCategory();
				Object[] aObj = (Object[])iterator.next();
				aPrCategory.setPrCategoryId((Integer) aObj[0]);
				aPrCategory.setDescription((String) aObj[1]);
				if(aObj[2]=="1"){
					aPrCategory.setInActive(true);
				}
				if(aObj[3]=="1"){
					aPrCategory.setMarkupCost(true);
				}
				aPrCategory.setMarkupAmount((BigDecimal) aObj[4]);
			}

		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			InventoryException aInventoryException = new InventoryException(e.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aDeptAccSelectQuery=null;
		}	
		return aPrCategory;

	}
	public String updatePrMasterAndPrwhInventory(PrwarehouseTransfer aPrwarehouseTransfer,Prwhtransferdetail aPrwhtransferdetail,String oper) throws InventoryException {
	Session aPrWarehouseSession=null;
	PrwarehouseTransfer objPrwarehouseTransfer = null;
	Transaction aTransaction;
	List<Prwarehouseinventory> aQueryList=null;
	Integer PrwarehouseinventoryfromId=null;
	Integer PrwarehouseinventorytoId=null;
	try {
		aPrWarehouseSession = itsSessionFactory.openSession();
		aTransaction = aPrWarehouseSession.beginTransaction();
		aTransaction.begin();
		
		Prwhtransferdetail oldPrwhtransferdetail=(Prwhtransferdetail)aPrWarehouseSession.get(Prwhtransferdetail.class,aPrwhtransferdetail.getPrTransferDetailId());
		
		
		Query aQuery = aPrWarehouseSession.createQuery("FROM  Prwarehouseinventory where prWarehouseId="+aPrwarehouseTransfer.getPrFromWarehouseId()+" and prMasterId="+aPrwhtransferdetail.getPrMasterId());
		aQueryList = aQuery.list();
		if(aQueryList!=null){
			PrwarehouseinventoryfromId=aQueryList.get(0).getPrWarehouseInventoryId();
		}
		aQueryList=null;
		aQuery = aPrWarehouseSession.createQuery("FROM  Prwarehouseinventory where prWarehouseId="+aPrwarehouseTransfer.getPrToWarehouseId()+" and prMasterId="+aPrwhtransferdetail.getPrMasterId());
		aQueryList = aQuery.list();
		if(aQueryList!=null){
			PrwarehouseinventorytoId=aQueryList.get(0).getPrWarehouseInventoryId();
		}
		Prwarehouseinventory objfrmprwarehouseinventory=(Prwarehouseinventory)aPrWarehouseSession.get(Prwarehouseinventory.class,PrwarehouseinventoryfromId);
		Prwarehouseinventory objtoprwarehouseinventory=(Prwarehouseinventory)aPrWarehouseSession.get(Prwarehouseinventory.class,PrwarehouseinventorytoId);
		if(oper.equals("add")){
				/*objfrmprwarehouseinventory.setInventoryOnHand(
					objfrmprwarehouseinventory.getInventoryOnHand()==null?(BigDecimal.ZERO).subtract(aPrwhtransferdetail.getQuantityTransfered()):objfrmprwarehouseinventory.getInventoryOnHand().subtract(aPrwhtransferdetail.getQuantityTransfered()));
				objtoprwarehouseinventory.setInventoryOnHand(
						objtoprwarehouseinventory.getInventoryOnHand()==null?(BigDecimal.ZERO).add(aPrwhtransferdetail.getQuantityTransfered()):objtoprwarehouseinventory.getInventoryOnHand().add(aPrwhtransferdetail.getQuantityTransfered()));*/
			objfrmprwarehouseinventory.setInventoryAllocated(
					objfrmprwarehouseinventory.getInventoryAllocated()==null?(BigDecimal.ZERO).add(aPrwhtransferdetail.getQuantityTransfered()):objfrmprwarehouseinventory.getInventoryAllocated().add(aPrwhtransferdetail.getQuantityTransfered()));
				objtoprwarehouseinventory.setInventoryOnOrder(
						objtoprwarehouseinventory.getInventoryOnOrder()==null?(BigDecimal.ZERO).add(aPrwhtransferdetail.getQuantityTransfered()):objtoprwarehouseinventory.getInventoryOnOrder().add(aPrwhtransferdetail.getQuantityTransfered()));
			aPrWarehouseSession.update(objfrmprwarehouseinventory);
				aPrWarehouseSession.update(objtoprwarehouseinventory);
				
			updatePrMasterForWTransfer(oldPrwhtransferdetail.getPrMasterId(),oldPrwhtransferdetail.getQuantityTransfered(),oper);
			//	updatePrMasterForReceiveItemsWTransfer(objPrwhtransferdetail.getPrMasterId(), objPrwhtransferdetail.getQuantityTransfered(),receiveOrUndo)
				
		}else if(oper.equals("edit")){
			/*objfrmprwarehouseinventory.setInventoryOnHand(
					objfrmprwarehouseinventory.getInventoryOnHand()==null?(BigDecimal.ZERO).add(oldPrwhtransferdetail.getQuantityTransfered()).subtract(aPrwhtransferdetail.getQuantityTransfered()):
						objfrmprwarehouseinventory.getInventoryOnHand().add(oldPrwhtransferdetail.getQuantityTransfered()).subtract(aPrwhtransferdetail.getQuantityTransfered()));
			objtoprwarehouseinventory.setInventoryOnHand(
					objtoprwarehouseinventory.getInventoryOnHand()==null?(BigDecimal.ZERO).subtract(oldPrwhtransferdetail.getQuantityTransfered()).add(aPrwhtransferdetail.getQuantityTransfered()):
						objtoprwarehouseinventory.getInventoryOnHand().subtract(oldPrwhtransferdetail.getQuantityTransfered()).add(aPrwhtransferdetail.getQuantityTransfered()));*/
			
			objfrmprwarehouseinventory.setInventoryAllocated(
					objfrmprwarehouseinventory.getInventoryAllocated()==null?(BigDecimal.ZERO).subtract(oldPrwhtransferdetail.getQuantityTransfered()).add(aPrwhtransferdetail.getQuantityTransfered()):
						objfrmprwarehouseinventory.getInventoryAllocated().subtract(oldPrwhtransferdetail.getQuantityTransfered()).add(aPrwhtransferdetail.getQuantityTransfered()));
			objtoprwarehouseinventory.setInventoryOnOrder(
					objtoprwarehouseinventory.getInventoryOnOrder()==null?(BigDecimal.ZERO).subtract(oldPrwhtransferdetail.getQuantityTransfered()).add(aPrwhtransferdetail.getQuantityTransfered()):
						objtoprwarehouseinventory.getInventoryOnOrder().subtract(oldPrwhtransferdetail.getQuantityTransfered()).add(aPrwhtransferdetail.getQuantityTransfered()));
			aPrWarehouseSession.update(objfrmprwarehouseinventory);
			aPrWarehouseSession.update(objtoprwarehouseinventory);
			updatePrMasterForWTransfer(oldPrwhtransferdetail.getPrMasterId(),oldPrwhtransferdetail.getQuantityTransfered(),"del");
			updatePrMasterForWTransfer(oldPrwhtransferdetail.getPrMasterId(),aPrwhtransferdetail.getQuantityTransfered(),"add");
			//updatePrMasterForWTransfer(objfrmprwarehouseinventory.getPrMasterId(),oldPrwhtransferdetail.getQuantityTransfered(),"del");
			//updatePrMasterForWTransfer(objfrmprwarehouseinventory.getPrMasterId(),aPrwhtransferdetail.getQuantityTransfered(),"add");
		}else if(oper.equals("delete")){
			/*objfrmprwarehouseinventory.setInventoryOnHand(
					objfrmprwarehouseinventory.getInventoryOnHand()==null?(BigDecimal.ZERO).add(oldPrwhtransferdetail.getQuantityTransfered()):objfrmprwarehouseinventory.getInventoryOnHand().add(oldPrwhtransferdetail.getQuantityTransfered()));
			objtoprwarehouseinventory.setInventoryOnHand(
					objtoprwarehouseinventory.getInventoryOnHand()==null?(BigDecimal.ZERO).subtract(oldPrwhtransferdetail.getQuantityTransfered()):objtoprwarehouseinventory.getInventoryOnHand().subtract(oldPrwhtransferdetail.getQuantityTransfered()));*/
	
			objfrmprwarehouseinventory.setInventoryAllocated(
					objfrmprwarehouseinventory.getInventoryAllocated()==null?(BigDecimal.ZERO).subtract(oldPrwhtransferdetail.getQuantityTransfered()):objfrmprwarehouseinventory.getInventoryAllocated().subtract(oldPrwhtransferdetail.getQuantityTransfered()));
			objtoprwarehouseinventory.setInventoryOnOrder(
					objtoprwarehouseinventory.getInventoryOnOrder()==null?(BigDecimal.ZERO).subtract(oldPrwhtransferdetail.getQuantityTransfered()):objtoprwarehouseinventory.getInventoryOnOrder().subtract(oldPrwhtransferdetail.getQuantityTransfered()));
			
			aPrWarehouseSession.update(objfrmprwarehouseinventory);
			aPrWarehouseSession.update(objtoprwarehouseinventory);
			updatePrMasterForWTransfer(objfrmprwarehouseinventory.getPrMasterId(),oldPrwhtransferdetail.getQuantityTransfered(),"del");
		}
		
		aTransaction.commit();
		
	} catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		throw new InventoryException(excep.getMessage(), excep);
	} finally {
		aPrWarehouseSession.flush();
		aPrWarehouseSession.close();
	}
	return "success";
}
	
	public Integer getRecordsCountforReceiveInvenory(int warehouseListID,String fromDateID,String toDateID,int prMasterID){
		//String aJobCountStr = "SELECT COUNT(vePOID) AS count FROM vePO";
		
	//	String aJobCountStr = "SELECT COUNT(vp.vePOID) AS COUNT FROM vePO vp WHERE receiveddate <>''";
		
		String aJobCountStr=	"SELECT  COUNT(vePO.vePOID) AS COUNT FROM vePO "
		+" LEFT JOIN vePODetail ON vePO.vePOID=vePODetail.vePOID WHERE vePODetail.QuantityReceived IS NOT NULL AND vePODetail.prMasterID='"+prMasterID+"' AND vePO.CreatedOn BETWEEN '"+fromDateID +" 00:00:00' AND '"+toDateID+" 23:59:59'  ";
		
		if(warehouseListID>0)
			aJobCountStr=aJobCountStr+" AND vePO.prWarehouseID='"+warehouseListID+"'";
		
		
		//System.out.println("PurchaseServiceImpl Integer getRecordsCount() query string :: "+aJobCountStr);
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr= null;
		}
		return aTotalCount.intValue();
	}
	
	@Override
	public List<PurchaseOrdersBean> filterInventoryTransactionListSize(String startDate,String endDate,int warehouseListID,int prMasterID) throws VendorException, ParseException {
		System.out.println("Retrieving Inventory Transactions");
		System.out.println("StartDate::"+startDate);
		System.out.println("EndDate::"+endDate);
		System.out.println("PrMasterID"+prMasterID);
		
		StringBuilder aPOSelectQry = new StringBuilder("SELECT H.prTransferID AS Id, H.TransferDate AS TranDate,"
				+ "CAST(H.Description AS CHAR(50)) AS TransNumber, 'Inventory Transfer' AS TranDesc,"
				+ " H.prFromWarehouseID AS fwID, H.prToWarehouseID AS twID, D.QuantityTransfered AS Qty, NULL AS UnitCost, NULL AS PriceMultiplier  "
				+ "FROM prTransfer AS H RIGHT OUTER JOIN prTransferDetail AS D ON H.prTransferID = D.prTransferID WHERE D.prMasterID = "+prMasterID+" ");
				
				if(!startDate.equals("") || !endDate.equals("")){
					aPOSelectQry.append(" AND H.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
				}
				if(warehouseListID>0){
					aPOSelectQry.append(" AND (H.prFromWarehouseID = '"+warehouseListID+"' OR H.prToWarehouseID = '"+warehouseListID+"')");
				}
				aPOSelectQry.append(" UNION ALL ");
				aPOSelectQry.append(" SELECT P.vePOID AS Id ,R.ReceiveDate AS TranDate, CAST(P.PONumber AS CHAR(50)) AS TransNumber,"
						+ "'Recieved PO' AS TranDesc, 0 AS fwID, "
						+ "P.prWarehouseID AS twID, D.QuantityReceived AS Qty, PD.UnitCost, PD.PriceMultiplier FROM veReceive AS R "
						+ "RIGHT OUTER JOIN vePODetail AS PD RIGHT OUTER JOIN veReceiveDetail AS D ON PD.vePODetailID = D.vePODetailID "
						+ "ON R.veReceiveID = D.veReceiveID LEFT OUTER JOIN vePO AS P ON R.vePOID = P.vePOID WHERE D.prMasterID = "+prMasterID+" ");
				if(!startDate.equals("") || !endDate.equals("")){
					aPOSelectQry.append(" AND P.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
				}
				if(warehouseListID>0){
					aPOSelectQry.append(" AND P.prWarehouseID='"+warehouseListID+"'");
				}
				aPOSelectQry.append(" UNION ALL ");
				aPOSelectQry.append(" SELECT I.cuInvoiceID as Id,I.InvoiceDate AS TranDate, CAST(I.InvoiceNumber AS CHAR(50)) AS TransNumber,"
						+ " 'Customer Invoice' AS TranDesc, "
						+ "I.prFromWarehouseID AS fwID, 0 AS twID, D.QuantityBilled AS Qty, D.UnitCost, NULL AS PriceMultiplier "
						+ "FROM cuInvoice AS I RIGHT OUTER JOIN cuInvoiceDetail AS D ON I.cuInvoiceID = D.cuInvoiceID WHERE "
						+ "(I.TransactionStatus > 0) AND (I.prFromWarehouseID IS NOT NULL) AND D.prMasterID = "+prMasterID+" ");
				
				if(!startDate.equals("") || !endDate.equals("")){
					aPOSelectQry.append(" AND I.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
				}
				if(warehouseListID>0){
					aPOSelectQry.append(" AND I.prFromWarehouseID='"+warehouseListID+"'");
				}
			aPOSelectQry.append(" ORDER BY TranDate ASC");
		
		//aPOSelectQry.append(" LIMIT " + from + ", " + to);
		Session aSession = null;

		List<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		
		BigDecimal pricemul = new BigDecimal(0.00);
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			//System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);			
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}	
				aPurchaseOrdersBean.setPONumber((String) aObj[2]);
				aPurchaseOrdersBean.setJobName((String) aObj[3]);
				aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
				if((BigDecimal) aObj[8]!=null )
			       pricemul =(BigDecimal) aObj[8];
				if(pricemul.compareTo(BigDecimal.ZERO) > 0 && (String)aObj[3]=="Recieved PO")
					aPurchaseOrdersBean.setSubtotal(((BigDecimal) aObj[7]).multiply(pricemul));
				else
				aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[7]);
				pricemul=new BigDecimal(0.00);
				aPurchaseOrdersBean.setInventoryOnHand((BigDecimal) aObj[6]);
				aQueryList.add(aPurchaseOrdersBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}
	
	@Override
	public List<PurchaseOrdersBean> filterInventoryTransactionList(String startDate,String endDate,int warehouseListID,int prMasterID,String sortIndex,String sortOrder,int from,int to) throws VendorException, ParseException {
		System.out.println("Retrieving Inventory Transactions");
		System.out.println("StartDate::"+startDate);
		System.out.println("EndDate::"+endDate);
		System.out.println("PrMasterID"+prMasterID);
		System.out.println("SortOrder::"+sortOrder);
		System.out.println("SortIndex::"+sortIndex);
		System.out.println("From::"+from);
		System.out.println("To::"+to);
		StringBuilder aPOSelectQry = new StringBuilder();
		/*StringBuilder aPOSelectQry = new StringBuilder(
				"SELECT  vePO.vePOID AS id,vePO.CreatedOn AS DATE,CAST(vePO.PONumber AS CHAR(50)) AS TransNumber,'Recieved PO' "
				+ "AS Trans,vePODetail.QuantityReceived AS income,vePODetail.UnitCost,"
				+ "vePODetail.PriceMultiplier,prMaster.InventoryOnHand FROM vePO  LEFT JOIN vePODetail ON "
				+ "vePO.vePOID=vePODetail.vePOID LEFT JOIN prMaster ON vePODetail.prMasterID = prMaster.prMasterID  "
				+ "WHERE vePODetail.QuantityReceived IS NOT NULL AND vePODetail.prMasterID='"+prMasterID+"'");*/
		/*StringBuilder aPOSelectQry = new StringBuilder("SELECT H.prTransferID AS Id, H.TransferDate AS TranDate,"
		+ "CAST(H.Description AS CHAR(50)) AS TransNumber, 'Inventory Transfer' AS TranDesc,"
		+ " H.prFromWarehouseID AS fwID, IFNULL(H.prToWarehouseID,0) AS twID, D.QuantityTransfered AS Qty, NULL AS UnitCost, NULL AS PriceMultiplier,0 as screenno  "
		+ "FROM prTransfer AS H RIGHT OUTER JOIN prTransferDetail AS D ON H.prTransferID = D.prTransferID WHERE D.prMasterID = "+prMasterID+" ");
		
		if(warehouseListID>0){
			aPOSelectQry.append(" AND (H.prFromWarehouseID = '"+warehouseListID+"' OR H.prToWarehouseID = '"+warehouseListID+"')");
		}
		
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND H.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		
		aPOSelectQry.append(" UNION ALL ");*/
		aPOSelectQry.append(" SELECT P.vePOID AS Id ,R.ReceiveDate AS TranDate, CAST(P.PONumber AS CHAR(50)) AS TransNumber,"
				+ "'Recieved PO' AS TranDesc, 0 AS fwID, "
				+ "P.prWarehouseID AS twID, D.QuantityReceived AS Qty, PD.UnitCost, PD.PriceMultiplier,4 as screenno,'' as ReceivedDate FROM veReceive AS R "
				+ "RIGHT OUTER JOIN vePODetail AS PD RIGHT OUTER JOIN veReceiveDetail AS D ON PD.vePODetailID = D.vePODetailID "
				+ "ON R.veReceiveID = D.veReceiveID LEFT OUTER JOIN vePO AS P ON R.vePOID = P.vePOID WHERE D.prMasterID = "+prMasterID+" ");
		if(warehouseListID>0){
			aPOSelectQry.append(" AND P.prWarehouseID='"+warehouseListID+"'");
		}
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND P.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		
		aPOSelectQry.append(" UNION ALL ");
		aPOSelectQry.append(" SELECT I.cuInvoiceID as Id,I.InvoiceDate AS TranDate, CAST(I.InvoiceNumber AS CHAR(50)) AS TransNumber,"
				+ " 'Customer Invoice' AS TranDesc, "
				+ "I.prFromWarehouseID AS fwID, 0 AS twID, D.QuantityBilled AS Qty, D.UnitPrice, NULL AS PriceMultiplier,5 as screenno,'' as ReceivedDate "
				+ "FROM cuInvoice AS I RIGHT OUTER JOIN cuInvoiceDetail AS D ON I.cuInvoiceID = D.cuInvoiceID WHERE "
				+ " I.TransactionStatus > 0 AND I.prFromWarehouseID IS NOT NULL AND D.prMasterID = "+prMasterID);
		
		if(warehouseListID>0){
			aPOSelectQry.append(" AND I.prFromWarehouseID='"+warehouseListID+"'");
		}
		
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND I.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		
		
		aPOSelectQry.append(" UNION ALL");
		aPOSelectQry.append(" SELECT  prWHtransfer.prTransferID AS Id,prWHtransfer.TransferDate AS TranDate,CASE prWHtransfer.screenno WHEN 2 THEN prWHtransfer.Description ELSE CAST(prWHtransfer.TransNumber AS CHAR(50)) END AS TransNumber,'Adjustments' AS Trans, "
				+ "IFNULL(prWHtransfer.prFromWareHouseID,0) AS fwID,IFNULL(prWHtransfer.prToWareHouseID,0)  AS twID,CASE prWHtransfer.screenno WHEN -2 THEN prWHtransferDetail.QuantityTransfered WHEN 1 THEN prWHtransferDetail.QuantityTransfered WHEN 2 THEN prWHtransferDetail.difference WHEN 3 THEN  prWHtransferDetail.`QuantityTransfered` END  AS Qty, prWHtransferDetail.InventoryCost AS TotalCost ,"
				+ "prWHtransferDetail.InventoryCost,prWHtransfer.screenno,prWHtransfer.ReceivedDate  FROM prWHtransfer  LEFT JOIN prWHtransferDetail ON prWHtransfer.prTransferID=prWHtransferDetail.prTransferID"
				+ " LEFT JOIN prMaster ON prWHtransferDetail.prMasterID = prMaster.prMasterID WHERE prWHtransferDetail.prMasterID="+prMasterID+" ");
		if(warehouseListID>0){
			aPOSelectQry.append(" AND (prWHtransfer.prFromWarehouseID='"+warehouseListID+"'");
			aPOSelectQry.append(" OR prWHtransfer.prToWarehouseID='"+warehouseListID+"')");
		}
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND prWHtransfer.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		aPOSelectQry.append(" AND screenno IS NOT NULL ");
		
		
		/*aPOSelectQry.append(" UNION ALL ");
		aPOSelectQry.append(" SELECT  prWHtransfer.prTransferID AS Id,prWHtransfer.TransferDate AS TranDate,"
				+ "CAST(prWHtransfer.TransNumber AS CHAR(50)) AS TransNumber,'Adjustments' AS Trans, "
				+ "IFNULL(prWHtransfer.prFromWareHouseID,0) AS fwID,IFNULL(prWHtransfer.prToWareHouseID,0)  AS twID,"
				+ "(prWHtransferDetail.QuantityTransfered*-1) AS Qty, prWHtransferDetail.InventoryCost AS TotalCost ,"
				+ "prWHtransferDetail.InventoryCost FROM prWHtransfer  LEFT JOIN prWHtransferDetail ON prWHtransfer.prTransferID="
				+ "prWHtransferDetail.prTransferID LEFT JOIN prMaster ON prWHtransferDetail.prMasterID = prMaster.prMasterID WHERE "
				+ "prWHtransferDetail.prMasterID="+prMasterID+" AND prWHtransfer.prFromWarehouseID IS NOT NULL AND prWHtransfer.prToWarehouseID IS NOT NULL");
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND prWHtransfer.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		if(warehouseListID>0){
			aPOSelectQry.append(" AND (prWHtransfer.prFromWarehouseID='"+warehouseListID+"')");
		}
		aPOSelectQry.append(" UNION ALL");
		aPOSelectQry.append(" SELECT  prWHtransfer.prTransferID AS Id,prWHtransfer.TransferDate AS TranDate,CAST(prWHtransfer.TransNumber AS CHAR(50)) AS TransNumber,'Adjustments' AS Trans, "
				+ "IFNULL(prWHtransfer.prFromWareHouseID,0) AS fwID,IFNULL(prWHtransfer.prToWareHouseID,0)  AS twID,prWHtransferDetail.QuantityTransfered AS Qty, prWHtransferDetail.InventoryCost AS TotalCost ,"
				+ "prWHtransferDetail.InventoryCost FROM prWHtransfer  LEFT JOIN prWHtransferDetail ON prWHtransfer.prTransferID=prWHtransferDetail.prTransferID"
				+ " LEFT JOIN prMaster ON prWHtransferDetail.prMasterID = prMaster.prMasterID WHERE prWHtransferDetail.prMasterID="+prMasterID+" "
				+ "AND prWHtransfer.prToWarehouseID IS NOT NULL AND prWHtransfer.prFromWarehouseID IS NOT NULL");
		
		if(warehouseListID>0){
			aPOSelectQry.append(" AND (prWHtransfer.prToWarehouseID = '"+warehouseListID+"')");
		}
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND prWHtransfer.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		aPOSelectQry.append(" UNION ALL");
		aPOSelectQry.append(" SELECT  prWHtransfer.prTransferID AS Id,prWHtransfer.TransferDate AS TranDate,CAST(prWHtransfer.TransNumber AS CHAR(50)) AS TransNumber,'Adjustments' AS Trans, "
				+ "IFNULL(prWHtransfer.prFromWareHouseID,0) AS fwID,IFNULL(prWHtransfer.prToWareHouseID,0)  AS twID,(prWHtransferDetail.difference*-1) AS Qty, prWHtransferDetail.InventoryCost AS TotalCost ,"
				+ "prWHtransferDetail.InventoryCost FROM prWHtransfer  LEFT JOIN prWHtransferDetail ON prWHtransfer.prTransferID=prWHtransferDetail.prTransferID"
				+ " LEFT JOIN prMaster ON prWHtransferDetail.prMasterID = prMaster.prMasterID WHERE prWHtransferDetail.prMasterID="+prMasterID+" "
				+ "AND prWHtransfer.prToWarehouseID IS NULL AND prWHtransfer.prFromWarehouseID IS NOT NULL AND prWHtransfer.reasonCode IS NOT NULL ");
		if(warehouseListID>0){
			aPOSelectQry.append(" AND prWHtransfer.prFromWarehouseID='"+warehouseListID+"'");
		}
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND prWHtransfer.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		
		
		aPOSelectQry.append(" UNION ALL");
		aPOSelectQry.append(" SELECT  prWHtransfer.prTransferID AS Id, prWHtransfer.TransferDate AS TranDate,'Count' AS TransNumber,"
							+"'Count' AS Trans,IFNULL(prWHtransfer.prFromWareHouseID,0) AS fwID,IFNULL(prWHtransfer.prToWareHouseID,0)  AS twID,"
							+"prWHtransferDetail.`QuantityTransfered` AS Qty,prWHtransferDetail.InventoryCost AS TotalCost,prWHtransferDetail.InventoryCost "
				+ " FROM prWHtransfer  LEFT JOIN prWHtransferDetail ON prWHtransfer.prTransferID=prWHtransferDetail.prTransferID"
				+ " LEFT JOIN prMaster ON prWHtransferDetail.prMasterID = prMaster.prMasterID WHERE prWHtransferDetail.prMasterID="+prMasterID+" "
				+ "AND prWHtransfer.prToWarehouseID IS NULL AND prWHtransfer.prFromWarehouseID IS NOT NULL AND reasonCode IS NULL");
		
		if(warehouseListID>0){
			aPOSelectQry.append(" AND prWHtransfer.prFromWarehouseID='"+warehouseListID+"'");
		}
		if(!startDate.equals("") || !endDate.equals("")){
			aPOSelectQry.append(" AND prWHtransfer.TransferDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}*/
		
		/*End*/
		
		
		
		if(sortIndex.equals("ponumber")){
			sortIndex = "TransNumber";
		}else if(sortIndex.equals("jobName")){
			sortIndex = "Trans";
		}else if(sortIndex.equals("quantityOrdered")){
			sortIndex ="income";
		}
		/*if(!sortIndex.equals("") && !sortOrder.equals("")){
			aPOSelectQry.append(" ORDER BY "+sortIndex+" "+sortOrder);
			
		}*/
		aPOSelectQry.append(" ORDER BY TranDate ASC ");
		if(from !=-1 && to!=-1){
			aPOSelectQry.append(" LIMIT "+from+" , "+to);
		}
		
		//aPOSelectQry.append(" LIMIT " + from + ", " + to);
		Session aSession = null;

		List<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		BigDecimal runningBalance = new BigDecimal(0);
		BigDecimal pricemul = new BigDecimal(0);
		BigInteger comparedVal = new BigInteger("0");
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			//System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			List alist = aQuery.list();
			ListIterator<?> aIterator = alist.listIterator();
			while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				Integer screenno=((BigInteger) aObj[9]).intValue();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);			
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}	
				aPurchaseOrdersBean.setPONumber((String) aObj[2]);
				
				
				if((BigDecimal) aObj[8]!=null )
			       pricemul =(BigDecimal) aObj[8];
				if(pricemul.compareTo(BigDecimal.ZERO) > 0 && (String)aObj[3]=="Recieved PO"){
					aPurchaseOrdersBean.setSubtotal(((BigDecimal) aObj[7]).multiply(pricemul));
				}
				else{
					aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[7]);
				}
				
				
				/*if(screenno==0){
					//Old Transfer Data
					if(((String)aObj[3]).equals("Inventory Transfer")){
						if(((BigInteger)aObj[5]).compareTo(new BigInteger("0"))!=0){
							
							if(comparedVal.compareTo(new BigInteger("0"))!=0 && comparedVal.compareTo((BigInteger)aObj[5])!=0){
							aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
							aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
							runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
							}else{
								aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
								aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
								runningBalance = runningBalance.add((BigDecimal) aObj[6]);
								comparedVal = new BigInteger((warehouseListID)+"");
								if(comparedVal.compareTo((BigInteger)aObj[5])!=0){
									aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
									aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
									runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
								}
								
							}
						}else{
						if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==-1){
							aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
							aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
							runningBalance = runningBalance.add(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
						}else{
							aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
							runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
							aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
						}
						}
					}else if(((String)aObj[3]).equals("Adjustments")){
						if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==1){
							aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
							aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
							runningBalance = runningBalance.add((BigDecimal) aObj[6]);
						}else{
							aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
							runningBalance = runningBalance.subtract(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
							aPurchaseOrdersBean.setDifference(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
						}
					}else if(((String)aObj[3]).equals("Count")){
						if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==1){
							aPurchaseOrdersBean.setJobName("Adjustment");
							aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
							runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
						}else{
							aPurchaseOrdersBean.setJobName((String)aObj[2]);
							aPurchaseOrdersBean.setQuantityOrdered(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
							runningBalance = runningBalance.add(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
							
						}
					}
					
					aPurchaseOrdersBean.setInventoryOnHand(runningBalance);
					aQueryList.add(aPurchaseOrdersBean);
				}*/
				if(screenno==-2)
				{
					
					//TurboRep Adjustment
					Integer frID=((BigInteger)aObj[4]).intValue();
					Integer toID=((BigInteger)aObj[5]).intValue();
					//WareHouse Transfer IN
					if(!(warehouseListID>0) || (frID!=0 && warehouseListID==frID)){
						PurchaseOrdersBean fromPurchaseOrdersBean =new  PurchaseOrdersBean();
						fromPurchaseOrdersBean.setVePOID(aPurchaseOrdersBean.getVePOID());
						fromPurchaseOrdersBean.setCreatedOn(aPurchaseOrdersBean.getCreatedOn());
						fromPurchaseOrdersBean.setPONumber(aPurchaseOrdersBean.getPONumber());
						fromPurchaseOrdersBean.setSubtotal(aPurchaseOrdersBean.getSubtotal());
						
							if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))>0){
								fromPurchaseOrdersBean.setJobName("Adjustment:"+(String)aObj[2]);
								fromPurchaseOrdersBean.setDifference(((BigDecimal) aObj[6]));
								fromPurchaseOrdersBean.setQuantityOrdered(null);
								runningBalance = runningBalance.subtract(fromPurchaseOrdersBean.getDifference());
							}else{
								fromPurchaseOrdersBean.setJobName("Adjustment:"+(String)aObj[2]);
								fromPurchaseOrdersBean.setQuantityOrdered(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
								fromPurchaseOrdersBean.setDifference(null);
								runningBalance = runningBalance.add(fromPurchaseOrdersBean.getQuantityOrdered());
							}
							fromPurchaseOrdersBean.setInventoryOnHand(runningBalance);
							aQueryList.add(fromPurchaseOrdersBean);
					}
				}else{
					if(screenno==1 && aObj[10]!=null){
						Integer frID=((BigInteger)aObj[4]).intValue();
						Integer toID=((BigInteger)aObj[5]).intValue();
						//WareHouse Transfer IN
						if(!(warehouseListID>0) || (frID!=0 && warehouseListID==frID)){
							PurchaseOrdersBean fromPurchaseOrdersBean =new  PurchaseOrdersBean();
							fromPurchaseOrdersBean.setVePOID(aPurchaseOrdersBean.getVePOID());
							fromPurchaseOrdersBean.setCreatedOn(aPurchaseOrdersBean.getCreatedOn());
							fromPurchaseOrdersBean.setPONumber(aPurchaseOrdersBean.getPONumber());
							fromPurchaseOrdersBean.setSubtotal(aPurchaseOrdersBean.getSubtotal());
							
								if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))>0){
									fromPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
									fromPurchaseOrdersBean.setDifference(((BigDecimal) aObj[6]));
									fromPurchaseOrdersBean.setQuantityOrdered(null);
									runningBalance = runningBalance.subtract(fromPurchaseOrdersBean.getDifference());
								}else{
									fromPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
									fromPurchaseOrdersBean.setQuantityOrdered(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
									fromPurchaseOrdersBean.setDifference(null);
									runningBalance = runningBalance.add(fromPurchaseOrdersBean.getQuantityOrdered());
								}
								fromPurchaseOrdersBean.setInventoryOnHand(runningBalance);
								aQueryList.add(fromPurchaseOrdersBean);
						}
						//WareHouse Transfer Out
						if(!(warehouseListID>0) || (toID!=0 && warehouseListID==toID)){
							PurchaseOrdersBean toPurchaseOrdersBean = new PurchaseOrdersBean();
							toPurchaseOrdersBean.setVePOID(aPurchaseOrdersBean.getVePOID());
							toPurchaseOrdersBean.setCreatedOn(aPurchaseOrdersBean.getCreatedOn());
							toPurchaseOrdersBean.setPONumber(aPurchaseOrdersBean.getPONumber());
							toPurchaseOrdersBean.setSubtotal(aPurchaseOrdersBean.getSubtotal());
							
							if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))>0){
								toPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
								toPurchaseOrdersBean.setQuantityOrdered(((BigDecimal) aObj[6]));
								toPurchaseOrdersBean.setDifference(null);
								runningBalance = runningBalance.add(toPurchaseOrdersBean.getQuantityOrdered());
							}else{
								toPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
								toPurchaseOrdersBean.setDifference(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
								toPurchaseOrdersBean.setQuantityOrdered(null);
								runningBalance = runningBalance.subtract(toPurchaseOrdersBean.getDifference());
							}
							toPurchaseOrdersBean.setInventoryOnHand(runningBalance);
							aQueryList.add(toPurchaseOrdersBean);
					}
						
						
					}else if(screenno==2){
						//Adjustment In
						if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))>0){
							aPurchaseOrdersBean.setJobName("Adjustment");
							aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
							runningBalance = runningBalance.add(aPurchaseOrdersBean.getQuantityOrdered());
						}else{
							aPurchaseOrdersBean.setJobName("Adjustment");
							aPurchaseOrdersBean.setDifference(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
							runningBalance = runningBalance.subtract(aPurchaseOrdersBean.getDifference());
						}
						aPurchaseOrdersBean.setInventoryOnHand(runningBalance);
						aQueryList.add(aPurchaseOrdersBean);
					}else if(screenno==3){
						//Inventory Count
						if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))>0){
							aPurchaseOrdersBean.setJobName("Count");
							aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
							runningBalance = runningBalance.subtract(aPurchaseOrdersBean.getDifference());
						}else{
							aPurchaseOrdersBean.setJobName("Count");
							aPurchaseOrdersBean.setQuantityOrdered(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
							runningBalance = runningBalance.add((aPurchaseOrdersBean.getQuantityOrdered()));
						}
						aPurchaseOrdersBean.setPONumber("System Generated Inventory Count");
						aPurchaseOrdersBean.setInventoryOnHand(runningBalance);
						aQueryList.add(aPurchaseOrdersBean);
					}else if(screenno==4){
						//PO Recieved In
						if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==-1){
							aPurchaseOrdersBean.setJobName("Stock PO Returned");
							aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
							runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
						}else{
							aPurchaseOrdersBean.setJobName("Stock PO Received");
							aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
							runningBalance = runningBalance.add((BigDecimal) aObj[6]);
						}
						aPurchaseOrdersBean.setInventoryOnHand(runningBalance);
						aQueryList.add(aPurchaseOrdersBean);
					}else if(screenno==5){
						//Custome Invoice Out
						if((((BigDecimal) aObj[6])==null?new BigDecimal(0):(BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==-1){
							aPurchaseOrdersBean.setJobName("Invoices In");
							aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
							runningBalance = runningBalance.add(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
						}else{
							aPurchaseOrdersBean.setJobName("Invoices Out");
							aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
							runningBalance = runningBalance.subtract((BigDecimal) aObj[6]==null?new BigDecimal(0):(BigDecimal) aObj[6]);
						}
						aPurchaseOrdersBean.setInventoryOnHand(runningBalance);
						aQueryList.add(aPurchaseOrdersBean);
					}
				}
				
				
			}//While loop ends
			
			/*while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);			
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}	
				aPurchaseOrdersBean.setPONumber((String) aObj[2]);
				
				
				if((BigDecimal) aObj[8]!=null )
			       pricemul =(BigDecimal) aObj[8];
				if(pricemul.compareTo(BigDecimal.ZERO) > 0 && (String)aObj[3]=="Recieved PO"){
					aPurchaseOrdersBean.setSubtotal(((BigDecimal) aObj[7]).multiply(pricemul));
				}
				else{
					aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[7]);
				}
				if(aObj[6]!=null){
				if(((String)aObj[3]).equals("Customer Invoice")){
					if((((BigDecimal) aObj[6])==null?new BigDecimal(0):(BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==-1){
						aPurchaseOrdersBean.setJobName("Invoices In");
						aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
						runningBalance = runningBalance.add(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
					}else{
						aPurchaseOrdersBean.setJobName("Invoices Out");
						aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
						runningBalance = runningBalance.subtract((BigDecimal) aObj[6]==null?new BigDecimal(0):(BigDecimal) aObj[6]);
					}
					
				}else if(((String)aObj[3]).equals("Inventory Transfer")){
					if(((BigInteger)aObj[5]).compareTo(new BigInteger("0"))!=0){
						
						if(comparedVal.compareTo(new BigInteger("0"))!=0 && comparedVal.compareTo((BigInteger)aObj[5])!=0){
						aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
						aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
						runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
						}else{
							aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
							aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
							runningBalance = runningBalance.add((BigDecimal) aObj[6]);
							comparedVal = new BigInteger((warehouseListID)+"");
							if(comparedVal.compareTo((BigInteger)aObj[5])!=0){
								aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
								aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
								runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
							}
							
						}
					}else{
					if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==-1){
						aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
						aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
						runningBalance = runningBalance.add(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
					}else{
						aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
						runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
						aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
					}
					}
				}else if(((String)aObj[3]).equals("Adjustments")){
					if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==1){
						aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
						aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
						runningBalance = runningBalance.add((BigDecimal) aObj[6]);
					}else{
						aPurchaseOrdersBean.setJobName("Inventory Transfer:"+(String)aObj[2]);
						runningBalance = runningBalance.subtract(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
						aPurchaseOrdersBean.setDifference(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
					}
				}else if(((String)aObj[3]).equals("Count")){
					if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==1){
						aPurchaseOrdersBean.setJobName("Adjustment");
						aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
						runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
					}else{
						aPurchaseOrdersBean.setJobName((String)aObj[2]);
						aPurchaseOrdersBean.setQuantityOrdered(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
						runningBalance = runningBalance.add(((BigDecimal) aObj[6]).multiply(new BigDecimal(-1.0)));
						
					}
				}
				else{
					if(((BigDecimal) aObj[6]).compareTo(new BigDecimal(0))==-1){
						aPurchaseOrdersBean.setJobName("Stock PO Returned");
						aPurchaseOrdersBean.setDifference((BigDecimal) aObj[6]);
						runningBalance = runningBalance.subtract((BigDecimal) aObj[6]);
					}else{
						aPurchaseOrdersBean.setJobName("Stock PO Received");
						aPurchaseOrdersBean.setQuantityOrdered((BigDecimal) aObj[6]);
						runningBalance = runningBalance.add((BigDecimal) aObj[6]);
					}
				}
			}
				pricemul=new BigDecimal(0.00);
				aPurchaseOrdersBean.setInventoryOnHand(runningBalance);
				aQueryList.add(aPurchaseOrdersBean);
			}
			*/
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}



	@Override
	public Prwarehouse getPrwarehouse(Integer PrwarehouseID) throws InventoryException {
		Session aSession = null;
		Prwarehouse aPrwarehouse = null;
		Integer aPrWareID = null;
		try {
			aSession = itsSessionFactory.openSession();
			aPrwarehouse = (Prwarehouse)aSession.get(Prwarehouse.class,PrwarehouseID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aPrwarehouse;
	}
	
	@Override
	public Integer saveInventoryTransactions(TpInventoryLog aTpInventoryLog) throws InventoryException {
		Session aPrWarehouseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer savePrimaryKey=0;
		try{
			aTransaction = aPrWarehouseSession.beginTransaction();
			aTransaction.begin();
			savePrimaryKey = (Integer) aPrWarehouseSession.save(aTpInventoryLog);
			aTransaction.commit();
		}
		catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			aPrWarehouseSession.flush();
			aPrWarehouseSession.close();
		}
		return savePrimaryKey;
	}

	@Override
	public Integer addTpInventoryLogMaster(TpInventoryLogMaster atpInventoryLogMaster) throws InventoryException{
		Session atpInventoryLogMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer returnvalue;
		try {
			aTransaction = atpInventoryLogMasterSession.beginTransaction();
			aTransaction.begin();
			returnvalue=(Integer) atpInventoryLogMasterSession.save(atpInventoryLogMaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new InventoryException(excep.getMessage(), excep);
		} finally {
			atpInventoryLogMasterSession.flush();
			atpInventoryLogMasterSession.close();
		}
		return returnvalue;
	}
	
	
	@Override
	public Prwarehouseinventory getPrwarehouseInventory(Integer PrwarehouseID,Integer prmasterID) throws InventoryException {
		Session aSession = null;
		Query aQuery =null;
		List<Prwarehouseinventory> aQueryList=null;
		Prwarehouseinventory aPrwarehouseinventory=null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery("FROM  Prwarehouseinventory where prWarehouseId="+PrwarehouseID+" and prMasterId="+prmasterID);
			aQueryList = aQuery.list();
			aPrwarehouseinventory=aQueryList.get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			InventoryException aInventoryException = new InventoryException(excep.getMessage(), excep);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aPrwarehouseinventory;
	}
}