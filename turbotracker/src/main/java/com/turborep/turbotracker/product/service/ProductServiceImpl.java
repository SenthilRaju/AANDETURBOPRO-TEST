package com.turborep.turbotracker.product.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLog;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLogMaster;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.GlTransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prinventorycount;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prorderpoint;
import com.turborep.turbotracker.product.dao.Prtransfer;
import com.turborep.turbotracker.product.dao.Prtransferdetail;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vepo;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name = "gltransactionService")
	private GltransactionService gltransactionService;
	
	@Resource(name = "inventoryService")
	private InventoryService inventoryService;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Override
	public Prmaster getProducts(String whereClause) throws ProductException {
		String aQry = "FROM Prmaster " + whereClause ;
		Session aSession = null;
		List<Prmaster> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query query = aSession.createQuery(aQry);
			// Retrieve all
			aQueryList = query.list();
			if(aQueryList.size() < 1){
				logger.error("No Product is available with given Item code and Description: \n" + whereClause);
				throw new ProductException("No Product is available with given Item code and Description: \n" + whereClause.replace("WHERE ", ""));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return aQueryList.get(0);
	}
	
	@Override
	public List<Prmaster> getOrderPoints(Integer warehouseID, String theSearchString, int from ,int to, int thePage, int theRows, String column,String sortBy) throws ProductException {
		Session aSession = null;
		String sortByOption ="";
		
		sortByOption=" P.ItemCode ";
		if(column.equalsIgnoreCase("description")){
			sortByOption=" P.Description ";
		}else if(column.equalsIgnoreCase("prDepartment")){
			sortByOption=" c.Description ";
		}else if(column.equalsIgnoreCase("prCategory")){
			sortByOption=" P.Description ";
		}/*else if(column.equalsIgnoreCase("vendorName")){
			sortByOption=" rx.Name ";
		}*/else if(column.equalsIgnoreCase("itemCode")){
			sortByOption=" P.ItemCode ";
		}else if(column.equalsIgnoreCase("inventoryAvailable")){
			sortByOption=" Available ";
		}else if(column.equalsIgnoreCase("inventoryOnOrder")){
			sortByOption=" P.InventoryOnOrder ";
		}else if(column.equalsIgnoreCase("inventoryOrderPoint")){
			sortByOption=" b.InventoryOrderPoint ";
		}else if(column.equalsIgnoreCase("inventoryOrderQuantity")){
			sortByOption=" b.InventoryOrderQuantity ";
		}		
		
		
	
		//aStringBuilder.append(" ORDER BY TRIM(Reference)");
		List<Prmaster> aQueryList = new ArrayList<Prmaster>();
		/*String aCustomerQry = "SELECT a.prMasterID,a.ItemCode,a.Description,c.Description AS Department," +
				" d.Description as category,(a.InventoryOnHand-a.InventoryAllocated) AS Available,a.InventoryOnOrder," +
				" b.InventoryOrderPoint,b.InventoryOrderQuantity" +
				" FROM prMaster a " +
				" LEFT JOIN prOrderPoint b ON a.prMasterID =b.prMasterID" +
				" LEFT JOIN prDepartment c ON a.prDepartmentID = c.prDepartmentID" +
				" LEFT JOIN rxMaster rx ON rx.rxMasterID = a.rxMasterIDPrimaryVendor "+
				" LEFT JOIN prCategory d ON a.prCategoryID=d.prCategoryID "+aStringBuilder;*/
		
		String aCustomerQry="SELECT P.prMasterID, P.ItemCode, P.Description,c.Description AS Department, d.Description AS category," 
							+" IF(W.InventoryOnHand-W.InventoryAllocated IS NULL,0,W.InventoryOnHand-W.InventoryAllocated) AS Available, "
							+ "if(W.InventoryOnOrder IS NULL,0,W.InventoryOnOrder), IF(O.InventoryOrderPoint IS NULL,0,O.InventoryOrderPoint), "
							+ "IF(O.InventoryOrderQuantity IS NULL,0,O.InventoryOrderQuantity) "              
							+" FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+warehouseID+") "
							+" AS W ON P.prMasterID = W.prMasterID)  LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+warehouseID+")  AS O "
							+" ON P.prMasterID = O.prMasterID LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID  "
							+" LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID  WHERE P.IsInventory=1 AND P.InActive=0 ";
		
		if(theSearchString!=null && theSearchString!=""){
			aCustomerQry = aCustomerQry+" And (P.Description LIKE '%"+theSearchString+"%' OR" +
						" P.ItemCode LIKE '%"+theSearchString+"%' OR" +
						" d.Description LIKE '%"+theSearchString+"%') ";
			 
		}	
		aCustomerQry = aCustomerQry+" ORDER BY "+sortByOption+sortBy.toUpperCase()+" LIMIT " + from + ", " + to;
		
		logger.info("Customer Qry : "+aCustomerQry);
		Prmaster aPrmaster = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				aPrmaster.setPrMasterId((Integer) aObj[0]);
				aPrmaster.setItemCode((String) aObj[1]);
				aPrmaster.setDescription((String) aObj[2]);
				aPrmaster.setPrDepartment((String) aObj[3]);
				aPrmaster.setPrCategory((String) aObj[4]);
				aPrmaster.setInventoryAvailable((BigDecimal) aObj[5]);
				aPrmaster.setInventoryOnOrder((BigDecimal) aObj[6]);
				aPrmaster.setInventoryOrderPoint((BigDecimal) aObj[7]);
				aPrmaster.setInventoryOrderQuantity((BigDecimal) aObj[8]);
				aQueryList.add(aPrmaster);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ProductException aProductException = new ProductException(e.getMessage(), e);
			throw aProductException;
		} finally {
			aSession.flush();
			aSession.close();
			sortByOption = null;
			aCustomerQry = null;
		}
		return aQueryList;	
	}
	
	@Override
	public List<Prmaster> getSuggestedOrderPoints(Integer warehouseID,String theSearchString,int from ,int to) throws ProductException {
		Session aSession = null;
			
		List<Prmaster> aQueryList = new ArrayList<Prmaster>();
		//String aCustomerQry = "SELECT a.prMasterID,a.ItemCode,a.Description,a.BoxQty, a.InventoryOnHand, a.InventoryAllocated,(a.InventoryOnHand-a.InventoryAllocated) AS Available, a.InventoryOnOrder, b.InventoryOrderPoint, (b.InventoryOrderPoint+b.InventoryOrderQuantity) AS suggestedOrder, (a.InventoryOnHand-a.InventoryAllocated) AS Projected FROM prMaster a LEFT JOIN prOrderPoint b ON a.prMasterID =b.prMasterID LEFT JOIN prDepartment c ON a.prDepartmentID = c.prDepartmentID LEFT JOIN prCategory d ON a.prCategoryID=d.prCategoryID "+aStringBuilder;
		String aCustomerQry="SELECT P.prMasterID, P.ItemCode, P.Description,IF(P.BoxQty IS NULL,0,P.BoxQty) AS BoxQty,"
		                    + "IF(W.InventoryOnHand IS NULL,0,W.InventoryOnHand) AS InventoryOnHand,IF(W.InventoryAllocated IS NULL,0,W.InventoryAllocated) AS InventoryAllocated, "
		                    + "IF(W.InventoryOnHand-W.InventoryAllocated IS NULL,0,W.InventoryOnHand-W.InventoryAllocated) AS Available,"
		                    + "IF(W.InventoryOnOrder IS NULL,0,W.InventoryOnOrder) AS InventoryOnOrder, IF(O.InventoryOrderPoint IS NULL,0,O.InventoryOrderPoint) AS InventoryOrderPoint,"
		                    + "IF(O.InventoryOrderQuantity IS NULL,0,O.InventoryOrderQuantity) AS InventoryOrderQuantity ,"
		                    + "getYeartoDate(P.prMasterID)  AS YTDSales "
		                    + "FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+warehouseID+")  AS W ON P.prMasterID = W.prMasterID)  "
		                    + "LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+warehouseID+")  AS O  ON P.prMasterID = O.prMasterID "
		                    + "LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID   LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID  "
		                    + "WHERE P.IsInventory=1 AND P.InActive=0 "
		                   // +"AND IFNULL((W.InventoryOnHand-W.InventoryAllocated),0)<IFNULL(O.InventoryOrderPoint,0) AND IFNULL(W.InventoryOnOrder,0)<IFNULL(O.InventoryOrderPoint,0 ) " 
		                    + "AND (IFNULL(O.InventoryOrderPoint,0)-(IFNULL(W.InventoryOnOrder, 0)+(IFNULL(W.InventoryOnHand,0)-IFNULL(W.InventoryAllocated,0))))>0 ";
		                   
		/*String aCustomerQry="SELECT P.prMasterID, P.ItemCode, P.Description,IF(IS NULL P.BoxQty,0,P.BoxQty),W.InventoryOnHand,W.InventoryAllocated, " +
							"W.InventoryOnHand-W.InventoryAllocated AS Available,W.InventoryOnOrder, O.InventoryOrderPoint, O.InventoryOrderQuantity ,"+
							"(O.InventoryOrderPoint+O.InventoryOrderQuantity) AS suggestedOrder,(W.InventoryOnHand-W.InventoryAllocated)+W.InventoryOnOrder AS Projected "+
							" FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+warehouseID+")  AS W ON P.prMasterID = W.prMasterID)  "+
							"LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+warehouseID+")  AS O  ON P.prMasterID = O.prMasterID "+
							"LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID   "+
							"LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID  WHERE P.IsInventory=1 AND P.InActive=0 ";*/
		
		if(theSearchString!=null && theSearchString!=""){
			aCustomerQry = aCustomerQry+" And (P.Description LIKE '%"+theSearchString+"%' OR" +
						" P.ItemCode LIKE '%"+theSearchString+"%' OR" +
						" d.Description LIKE '%"+theSearchString+"%') ";
			 
		}	
		aCustomerQry = aCustomerQry+" ORDER BY  P.ItemCode ASC LIMIT " + from + ", " + to;
		logger.info("Customer Qry : "+aCustomerQry);
		Prmaster aPrmaster = null;
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				aPrmaster.setPrMasterId((Integer) aObj[0]);
				aPrmaster.setItemCode((String) aObj[1]);
				aPrmaster.setDescription((String) aObj[2]);
				aPrmaster.setBoxQty((BigDecimal) aObj[3]);
				aPrmaster.setInventoryOnHand((BigDecimal) aObj[4]);
				aPrmaster.setInventoryAllocated((BigDecimal) aObj[5]);
				aPrmaster.setInventoryAvailable((BigDecimal) aObj[6]);
				aPrmaster.setInventoryOnOrder((BigDecimal) aObj[7]);
				aPrmaster.setInventoryOrderPoint((BigDecimal) aObj[8]);
				aPrmaster.setInventoryProjected(aPrmaster.getInventoryOnOrder().add(aPrmaster.getInventoryOnHand().subtract(aPrmaster.getInventoryAllocated())));
				
				if(aPrmaster.getInventoryOrderPoint().subtract(aPrmaster.getInventoryProjected())==null){
					aPrmaster.setInventorySuggestedOrder(BigDecimal.ZERO);
				}else{
					/*if((aPrmaster.getInventoryAvailable().compareTo(aPrmaster.getInventoryOrderPoint())==1) ||
							aPrmaster.getInventoryOnOrder().compareTo(aPrmaster.getInventoryOrderPoint())==1
							){
						aPrmaster.setInventorySuggestedOrder(BigDecimal.ZERO);
					}else{
					aPrmaster.setInventorySuggestedOrder(aPrmaster.getInventoryOrderPoint().subtract(aPrmaster.getInventoryProjected()));
					}
					*/
					if((aPrmaster.getInventoryOrderPoint().subtract(aPrmaster.getInventoryProjected())).compareTo(BigDecimal.ZERO)>0){
						aPrmaster.setInventorySuggestedOrder(aPrmaster.getInventoryOrderPoint().subtract(aPrmaster.getInventoryProjected()));
					}else{
						aPrmaster.setInventorySuggestedOrder(BigDecimal.ZERO);
					}
				}
				aPrmaster.setYtd((Integer) aObj[10]);
				if(aPrmaster.getInventorySuggestedOrder()!=null){
				if(aPrmaster.getInventorySuggestedOrder().compareTo(BigDecimal.ZERO)>0){
					aQueryList.add(aPrmaster);
				}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ProductException aProductException = new ProductException(e.getMessage(), e);
			throw aProductException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry = null;
		}
		return aQueryList;	
	}
	
	@Override
	public Integer getOrderPointsCount(Integer warehouseID, String theSearchString, int thePage, int theRows, String theSidx, String theSord)throws ProductException{
		
		String aJobCountStr = "SELECT COUNT(P.prMasterID) AS COUNT FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = 2)  AS W ON P.prMasterID = W.prMasterID) " 
							  +"LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+warehouseID+")  AS O "
							  +"ON P.prMasterID = O.prMasterID LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID   WHERE P.IsInventory=1 AND P.InActive=0 ";
		if(theSearchString!=null && theSearchString!=""){
			aJobCountStr=aJobCountStr+ " and  (P.Description LIKE '%"+theSearchString+"%' OR" +
					" P.ItemCode LIKE '%"+theSearchString+"%' OR" +
					" d.Description LIKE '%"+theSearchString+"%') ";
		}
		
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}
	
	
	@Override
	public Integer getSuggestedOrderPointsCount(Integer warehouseID, String theSearchString, int thePage, int theRows, String theSidx, String theSord)throws ProductException{
		
		
		
		
		
		
		
		String aJobCountStr = "SELECT count(P.prMasterID) "
                + "FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+warehouseID+")  AS W ON P.prMasterID = W.prMasterID)  "
                + "LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+warehouseID+")  AS O  ON P.prMasterID = O.prMasterID "
                + "LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID   LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID  "
                + "WHERE P.IsInventory=1 AND P.InActive=0 "
               // +"AND IFNULL(W.InventoryOnHand-W.InventoryAllocated,0)<IFNULL(O.InventoryOrderPoint,0) AND IFNULL(W.InventoryOnOrder,0)<IFNULL(O.InventoryOrderPoint,0 ) " 
                + " AND (IFNULL(O.InventoryOrderPoint,0)-(IFNULL(W.InventoryOnOrder, 0)+(IFNULL(W.InventoryOnHand,0)-IFNULL(W.InventoryAllocated,0))))>0 ";

		if(theSearchString!=null && theSearchString!=""){
			aJobCountStr = aJobCountStr+" And (P.Description LIKE '%"+theSearchString+"%' OR" +
						" P.ItemCode LIKE '%"+theSearchString+"%' OR" +
						" d.Description LIKE '%"+theSearchString+"%') ";
			 
		}	
		
		
		Session aSession = null;
		int aTotalCount = 0;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			if(aList.get(0)!=null){
				aTotalCount = ((BigInteger) aList.get(0)).intValue();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount;
	}
	
	
	@Override
	public List<Prwarehouse> getWarehouses() throws ProductException {
		Session aSession =itsSessionFactory.openSession(); 
		String Qry = "FROM Prwarehouse";
		List<Prwarehouse> warehouses = null;
		try{
			warehouses = (List<Prwarehouse>)aSession.createQuery(Qry).list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			Qry = null;
		}
		return  warehouses;
		
	}
	
	@Override
	public Prwarehouse getWarehouseDetail(Integer prWarehouseID) throws ProductException {
		Session aSession =itsSessionFactory.openSession(); 
		Prwarehouse warehouses = new Prwarehouse();
		try{
			warehouses = (Prwarehouse)aSession.get(Prwarehouse.class,prWarehouseID);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  warehouses;
		
	}
	
	@Transactional
	@Override
	public void updateWarehouseDetails(Prwarehouse thePrwarehouse) throws ProductException {
		Session aSession = null;
		org.hibernate.Transaction aTransaction = null;
		Prwarehouse aPrwarehouse = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aPrwarehouse = (Prwarehouse)aSession.get(Prwarehouse.class,thePrwarehouse.getPrWarehouseId());
			aPrwarehouse.setAddress1(thePrwarehouse.getAddress1());
			aPrwarehouse.setAddress2(thePrwarehouse.getAddress2());
			aPrwarehouse.setCity(thePrwarehouse.getCity());
			aPrwarehouse.setCoAccountIdasset(thePrwarehouse.getCoAccountIdasset());
			aPrwarehouse.setAdjustCogAccountID(thePrwarehouse.getAdjustCogAccountID());
			aPrwarehouse.setCoTaxTerritoryId(thePrwarehouse.getCoTaxTerritoryId());
			aPrwarehouse.setDescription(thePrwarehouse.getDescription());
			aPrwarehouse.setEmail(thePrwarehouse.getEmail());
			aPrwarehouse.setInActive(thePrwarehouse.getInActive());
			aPrwarehouse.setSearchName(thePrwarehouse.getSearchName());
			aPrwarehouse.setState(thePrwarehouse.getState());
			aPrwarehouse.setZip(thePrwarehouse.getZip());
			aPrwarehouse.setPickTicketInfo(thePrwarehouse.getPickTicketInfo());
			aSession.update(aPrwarehouse);
			aTransaction.commit();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	@Transactional
	@Override
	public void addWarehouse(Prwarehouse thePrwarehouse) throws ProductException {
		Session aSession = null;
		org.hibernate.Transaction aTransaction = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.save(thePrwarehouse);
			aTransaction.commit();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	@Transactional
	@Override
	public void deleteWarehouse(Integer prWarehouseId) throws ProductException {
		Session aSession = null;
		org.hibernate.Transaction aTransaction = null;
		Prwarehouse aPrwarehouse = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aPrwarehouse = (Prwarehouse)aSession.get(Prwarehouse.class,prWarehouseId);
			aSession.delete(aPrwarehouse);
			aTransaction.commit();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Transactional
	@Override
	public Prwarehouse getWareHouseAddress(Integer PrWareHouseId)throws JobException {
		Session prWarehouseSession =null;
		Prwarehouse aPrwarehouse = new Prwarehouse();
		try{
			prWarehouseSession = itsSessionFactory.openSession();
			aPrwarehouse = (Prwarehouse)prWarehouseSession.get(Prwarehouse.class, PrWareHouseId);
			
		}catch (Exception excep) {
			logger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(), excep);
			throw aJobException;
		} finally {
			prWarehouseSession.flush();
			prWarehouseSession.close();
		}
		return aPrwarehouse;
	}
	
	public void updateOrderPoints (Prorderpoint aOrderPoint){
		Session aSession = null;
		Transaction aTransaction = null;
		
		aSession =itsSessionFactory.openSession(); 
		String Qry = "SELECT * FROM prOrderPoint WHERE prWarehouseID="+aOrderPoint.getPrWarehouseId()+" AND prMasterID="+aOrderPoint.getPrMasterId();
		Prorderpoint aProrderpoint = null;
		try{
			Query aQuery = aSession.createSQLQuery(Qry);
			Iterator<?> aIterator = aQuery.list().iterator();
			
			if (aIterator.hasNext()) {
				aProrderpoint = new Prorderpoint();
				Object[] aObj = (Object[])aIterator.next();
				aProrderpoint.setPrOrderPointId((Integer) aObj[0]);
				aProrderpoint.setPrMasterId((Integer) aObj[1]);
				aProrderpoint.setPrWarehouseId((Integer) aObj[2]);
				aProrderpoint.setInventoryOrderPoint((BigDecimal) aObj[3]);
				aProrderpoint.setInventoryOrderQuantity((BigDecimal) aObj[4]);
			}
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if(aProrderpoint != null)
			{
				logger.info("Data available in DB - Update");
				aProrderpoint = (Prorderpoint)aSession.get(Prorderpoint.class, aProrderpoint.getPrOrderPointId());
				if(aOrderPoint.getInventoryOrderPoint() != null)
					aProrderpoint.setInventoryOrderPoint(aOrderPoint.getInventoryOrderPoint());	
				if(aOrderPoint.getInventoryOrderQuantity() != null)
					aProrderpoint.setInventoryOrderQuantity(aOrderPoint.getInventoryOrderQuantity());
				logger.info("aProrderpoint :: "+aProrderpoint);
				aSession.update(aProrderpoint);	
			}
			else 
			{
				aProrderpoint = new Prorderpoint();
				logger.info("Data not available in DB - Insert");
				aProrderpoint.setPrWarehouseId(aOrderPoint.getPrWarehouseId());
				aProrderpoint.setPrMasterId(aOrderPoint.getPrMasterId());
				if(aOrderPoint.getInventoryOrderPoint() != null)
					aProrderpoint.setInventoryOrderPoint(aOrderPoint.getInventoryOrderPoint());	
				if(aOrderPoint.getInventoryOrderQuantity() != null)
					aProrderpoint.setInventoryOrderQuantity(aOrderPoint.getInventoryOrderQuantity());
				aSession.save(aProrderpoint);
			}
			aTransaction.commit();
		}
		catch (Exception e) {
			aTransaction.rollback();
			logger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			Qry = null;
		}
	}

	@Override
	public List<Prmaster> getcountInventory(Integer prWarehouseID, int aFrom,
			int aTo, Integer sortval) {
			Session aSession = null;
			List<Prmaster> aQueryList = new ArrayList<Prmaster>();
			StringBuilder aStringBuilder = new StringBuilder("");
			
			if(sortval!=0){
				if(sortval==1)aStringBuilder.append(" ORDER BY prMaster.ItemCode");
				if(sortval==2)aStringBuilder.append(" ORDER BY prMaster.Description");
				if(sortval==3)aStringBuilder.append(" ORDER BY Category");
				if(sortval==4)aStringBuilder.append(" ORDER BY PrimaryVendor");
			}
			if(aFrom>-1 && aTo>-1){
			aStringBuilder.append(" LIMIT " + aFrom + ", " + aTo);
			}
			
			String aStr = "SELECT prMaster.prMasterID,prMaster.ItemCode,prMaster.Description,prMaster.prDepartmentID,prMaster.prCategoryID, " +
					"(SELECT Name FROM rxMaster WHERE rxMaster.rxMasterID=prMaster.rxMasterIDPrimaryVendor) as PrimaryVendor, " +
					"(SELECT prDepartment.Description FROM prDepartment WHERE prDepartment.prDepartmentID=prMaster.prDepartmentID) as Department, "+
					"(SELECT Description FROM prCategory WHERE prCategory.prCategoryID=prMaster.prCategoryID)as Category," +
					"prWarehouseInventory.InventoryOnHand,prInventoryCount.CountedOnHand,prWarehouseInventory.prWarehouseInventoryID  "
					+ "FROM prMaster LEFT JOIN prWarehouseInventory ON prWarehouseInventory.prMasterID = prMaster.prMasterID "
					+ "LEFT JOIN prInventoryCount ON prInventoryCount.prMasterID = prWarehouseInventory.prMasterID  where "
					+ " prMaster.InActive=0 AND prMaster.IsInventory =1 AND prWarehouseInventory.prWarehouseID="+prWarehouseID+aStringBuilder;
			logger.info(aStr);
			Prmaster aPrmaster = null;
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
					aPrmaster.setPrDepartmentId((Integer) aObj[3]);
					aPrmaster.setPrCategoryId((Integer) aObj[4]);
					aPrmaster.setPrimaryVendorName((String) aObj[5]);
					aPrmaster.setPrDepartment((String) aObj[6]);
					aPrmaster.setPrCategory((String) aObj[7]);
					aPrmaster.setInventoryOnHand((BigDecimal)aObj[8]);
					aPrmaster.setInventoryCounted((BigDecimal) aObj[9]);
					aPrmaster.setPrWarehouseInventoryID((Integer) aObj[10]);
					aQueryList.add(aPrmaster);
				}
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
			} finally {
				aSession.flush();
				aSession.close();
				aStr = null;
			}
			return aQueryList;	
		}

	
	
	@Override
	public Integer getcountInventoryCount(Integer prWarehouseID)
			throws ProductException {
			StringBuilder aStringBuilder = new StringBuilder("");
			
			if(prWarehouseID!=0){
				aStringBuilder.append(" WHERE b.prWarehouseID="+prWarehouseID);
			}
			
			String aJobCountStr = "SELECT COUNT(prMaster.prMasterID) FROM prMaster LEFT JOIN prWarehouseInventory ON prWarehouseInventory.prMasterID = prMaster.prMasterID LEFT JOIN prInventoryCount ON prInventoryCount.prMasterID = prWarehouseInventory.prMasterID  where prWarehouseInventory.prWarehouseID="+prWarehouseID;
			logger.info(aJobCountStr);
			Session aSession = null;
			BigInteger aTotalCount = null;
			try {
				// Retrieve aSession from Hibernate
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aJobCountStr);
				List<?> aList = aQuery.list();
				aTotalCount = (BigInteger) aList.get(0);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
				aJobCountStr = null;
			}
			return aTotalCount.intValue();
		}

	/*@Override
	public void insertCountInventory(Prinventorycount aNewPrinventorycount,Integer prWarehouseID) {
		Session aSession = null;
		Transaction aTransaction = null;
		Prmaster aPrmaster = null;
		try{
			aPrmaster = new Prmaster();
			Prmaster newPrmaster = new Prmaster();
			aSession =itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aPrmaster= getProducts(" WHERE prMasterID="+aNewPrinventorycount.getPrMasterId());
			//if(aPrmaster.getInventoryOnHand().equals("")){
				//aPrmaster.setInventoryOnHand(new BigDecimal(0));
			//}
				if(aPrmaster!=null){
					logger.info("Data available in PRM: "+aPrmaster.getPrMasterId());
					newPrmaster = (Prmaster)aSession.get(Prmaster.class, aPrmaster.getPrMasterId());
					logger.info("New PRM counted Value: "+aNewPrinventorycount.getCountedOnHand());
					newPrmaster.setInventoryOnHand(aNewPrinventorycount.getCountedOnHand());
					aPrmaster.setInventoryOnHand(aNewPrinventorycount.getCountedOnHand());
					aSession.update(newPrmaster);	
				}
				aTransaction.commit();
		}
		catch(Exception e){
			aTransaction.rollback();
			logger.error(e.getMessage(), e);
		}finally {
			aSession.flush();
			aSession.close();
		}
		try{
			aSession =itsSessionFactory.openSession(); 
			String Qry = "SELECT * FROM prInventoryCount WHERE prMasterID="+aPrmaster.getPrMasterId();
			logger.info("Query-1"+Qry);
			Prinventorycount aPrinventorycount = null;
			try{
				aTransaction = aSession.beginTransaction();
				Query aQuery = aSession.createSQLQuery(Qry);
				List<Prinventorycount> prinventory = aQuery.list();
			if(prinventory.size()>0)
			{
				logger.info("prInventoryCount - Update");
				aPrinventorycount = new Prinventorycount();
				aPrinventorycount = (Prinventorycount)aSession.get(Prinventorycount.class, aPrmaster.getPrMasterId());
				aPrinventorycount.setCountedOnHand(aNewPrinventorycount.getCountedOnHand());
				aSession.update(aPrinventorycount);	
			}
			else 
			{
				
				aPrinventorycount = new Prinventorycount();
				logger.info("prInventoryCount - Insert");
				aPrinventorycount.setItemCode(aPrmaster.getItemCode());
				aPrinventorycount.setPrMasterId(aPrmaster.getPrMasterId());
				aPrinventorycount.setDescription(aPrmaster.getDescription());
				aPrinventorycount.setPrDepartmentId(aPrmaster.getPrDepartmentId());
				aPrinventorycount.setSystemOnHand(aPrmaster.getInventoryOnHand());
				aPrinventorycount.setCountedOnHand(aPrmaster.getInventoryCounted());
				aSession.save(aPrinventorycount);
			}
			aTransaction.commit();
		} catch (Exception e) {
			aTransaction.rollback();
			logger.error(e.getMessage(), e);
		} finally{
			aSession.flush();
			aSession.close();
			Qry = null;
		}
			
			aSession =itsSessionFactory.openSession(); 
			String query = "SELECT * FROM prWarehouseInventory WHERE prWarehouseID="+prWarehouseID+" AND prMasterID="+aPrmaster.getPrMasterId();
			logger.info("Query-2"+query);
			Prwarehouseinventory aprPrwarehouseinventory = null;
			try{
				aTransaction = aSession.beginTransaction();
				Query aQuery = aSession.createSQLQuery(query);
				List<Prwarehouseinventory> aprPrwarehouseinventoryList = aQuery.list();
			if(aprPrwarehouseinventoryList.size() >0)
			{
				Iterator<?> aIteratorNew = aQuery.list().iterator();
				while (aIteratorNew.hasNext()) {
					Object[] aObj = (Object[])aIteratorNew.next();
				logger.info("Data available in DB - Update");
				aprPrwarehouseinventory = new Prwarehouseinventory();
				aprPrwarehouseinventory.setPrWarehouseInventoryId((Integer) aObj[0]);
				logger.info("Data-1 "+aprPrwarehouseinventory.getPrWarehouseInventoryId());
				aprPrwarehouseinventory = (Prwarehouseinventory)aSession.get(Prwarehouseinventory.class, aprPrwarehouseinventory.getPrWarehouseInventoryId());
				logger.info("Data-2 "+aPrmaster.getInventoryOnHand());
				aprPrwarehouseinventory.setInventoryOnHand(aPrmaster.getInventoryOnHand());
				aSession.update(aprPrwarehouseinventory);	
				}
			}
			else 
			{
				
				aprPrwarehouseinventory = new Prwarehouseinventory();
				logger.info("Data not available in DB - Insert");
				aprPrwarehouseinventory.setPrMasterId(aPrmaster.getPrMasterId());
				aprPrwarehouseinventory.setPrWarehouseId(prWarehouseID);
				aprPrwarehouseinventory.setInitialOnHand(aPrmaster.getInventoryOnHand());
				aSession.save(aPrinventorycount);
			}
			aTransaction.commit();
		} catch (Exception e) {
			aTransaction.rollback();
			logger.error(e.getMessage(), e);
		}finally{
			aSession.flush();
			aSession.close();
			query = null;
		}
			
			aSession =itsSessionFactory.openSession(); 
			query = "SELECT * FROM prTransferDetail WHERE  prMasterID="+aPrmaster.getPrMasterId();
			logger.info("Query-3"+query);
			Prtransferdetail aPrtransferdetail = null;
			try{
				aTransaction = aSession.beginTransaction();
				Query aQuery = aSession.createSQLQuery(query);
				List<Prtransferdetail> aPrtransferdetailList = aQuery.list();
			if(aPrtransferdetailList != null)
			{
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					aPrtransferdetail = new Prtransferdetail();
					aPrtransferdetail.setPrTransferDetailId((Integer) aObj[0]);
					aPrtransferdetail = (Prtransferdetail)aSession.get(Prtransferdetail.class, aPrtransferdetail.getPrTransferDetailId());
					aPrtransferdetail.setQuantityTransfered(aPrmaster.getInventoryOnOrder().subtract(aNewPrinventorycount.getCountedOnHand()));
					aSession.update(aPrtransferdetail);	
				}
			}
			else 
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
				Date date = Calendar.getInstance().getTime();
				Prtransfer aprTransfer = new Prtransfer();
				aprTransfer.setTransferDate(date);
				aprTransfer.setPrFromWarehouseId(prWarehouseID);
				aprTransfer.setDescription("System Generated Inventory Count");
				Integer id = (Integer)aSession.save(aprTransfer);
				aPrtransferdetail = new Prtransferdetail();
				aPrtransferdetail.setPrMasterId(aPrmaster.getPrMasterId());
				aPrtransferdetail.setPrTransferId(id);
				aPrtransferdetail.setDescription(aPrmaster.getItemCode());
				aPrtransferdetail.setQuantityTransfered(aPrmaster.getInventoryOnOrder().subtract(aNewPrinventorycount.getCountedOnHand()));
				aSession.save(aPrtransferdetail);
			}
			aTransaction.commit();
		} catch (Exception e) {
			aTransaction.rollback();
			logger.error(e.getMessage(), e);
		}finally{
			aSession.flush();
			aSession.close();
			query=null;
		}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}finally {
			aSession.flush();
			aSession.close();
		}
	}*/

	@Override
	public List<PrwarehouseTransfer> getprTransferData(Integer prTransferID) throws ProductException {
			Session aSession =itsSessionFactory.openSession(); 
			StringBuilder aStringBuilder = new StringBuilder("");
			
				aStringBuilder.append(" WHERE prTransferID="+prTransferID);
			
			String Qry = "SELECT prTransferID,TransferDate,prFromWarehouseID,prToWarehouseID,Description,coAccountID,reasonCode,adjustCogAccountID  FROM prWHtransfer WHERE  screenno=2 or screenno=-2 ";
			if(prTransferID!=0){
			Qry=Qry+" and prTransferID="+prTransferID;
			}
			Qry=Qry+" order by TransferDate DESC";
			List<PrwarehouseTransfer> aprtransferList = null;
			PrwarehouseTransfer aPrtransfer = null;
			try{
				aprtransferList = new ArrayList<PrwarehouseTransfer>();
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(Qry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					aPrtransfer = new PrwarehouseTransfer();
					Object[] aObj = (Object[])aIterator.next();
					aPrtransfer.setPrTransferId((Integer) aObj[0]);			
					if(aObj[1] != null)
					{
						Timestamp stamp = (Timestamp)aObj[1];
						java.sql.Date date = new java.sql.Date(stamp.getTime());
						//System.out.println("getprTransferData() Date -----> "+date);
						aPrtransfer.setTransferDate(date);
					}
					else{
						aPrtransfer.setTransferDate((Date)aObj[1] );//(Date) aObj[1]
						}
					aPrtransfer.setPrFromWarehouseId((Integer) aObj[2]);
					aPrtransfer.setPrToWarehouseId((Integer) aObj[3]);
					aPrtransfer.setDesc((String) aObj[4]);
					aPrtransfer.setCoAccountID((Integer) aObj[5]);
					aPrtransfer.setReasonCode((String) aObj[6]);
					aPrtransfer.setAdjustCogAccountID((Integer) aObj[7]);
					aprtransferList.add(aPrtransfer);
				}
				
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ProductException(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
				Qry= null;
			}
			return  aprtransferList;
	}
	
			@Override
			public List<Prwhtransferdetail> getprTransferDetails(Integer prTransferId) throws ProductException {
				//String aQry = "SELECT p.prTransferDetailID, p.prTransferID, p.prMasterID, p.Description, pw.InventoryOnHand, p.QuantityTransfered,pm.ItemCode FROM prWHtransferDetail p LEFT JOIN prWarehouseInventory pw ON p.prMasterID = pw.prMasterID left JOIN prMaster pm ON pm.prMasterID=pw.prMasterID WHERE prTransferID= " + prTransferId;
				String aQry = "SELECT p.prTransferDetailID, p.prTransferID, p.prMasterID, p.Description, p.QuantityTransfered,p.inventoryCost,p.quantityAvailable,p.itemCode,p.difference FROM prWHtransferDetail p  WHERE p.prTransferID= " + prTransferId;
				Session aSession = null;

				List<Prwhtransferdetail> aQueryList = new ArrayList<Prwhtransferdetail>();
				
				try {
					// Retrieve session from Hibernate
					aSession = itsSessionFactory.openSession();
					// Create a Hibernate query (HQL)
					Query query = aSession.createSQLQuery(aQry);
					// Retrieve all
					Prwhtransferdetail prtransferdetail = new Prwhtransferdetail();
					Iterator<?> aIterator = query.list().iterator();
					while (aIterator.hasNext()) {
						prtransferdetail = new Prwhtransferdetail();
						Object[] aObj = (Object[])aIterator.next();
						prtransferdetail.setPrTransferDetailId((Integer) aObj[0]);
						prtransferdetail.setPrTransferId((Integer) aObj[1]);
						prtransferdetail.setPrMasterId((Integer) aObj[2]);
						prtransferdetail.setDescription((String) aObj[3]);
						prtransferdetail.setQuantityTransfered((BigDecimal) aObj[4]);
						prtransferdetail.setInventoryCost((BigDecimal) aObj[5]);
						prtransferdetail.setQuantityAvailable((BigDecimal) aObj[6]);
						prtransferdetail.setItemCode((String) aObj[7]);
						prtransferdetail.setDifference((BigDecimal) aObj[8]);
						aQueryList.add(prtransferdetail);
					}
					//aQueryList = query.list();
					if(aQueryList.size() < 1){
						logger.error("No Product is available with given Item code and Description: \n");
			
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ProductException(e.getMessage(), e);
				} finally {
					aSession.flush();
					aSession.close();
				}
				return aQueryList;
			}
			
	@Override
	public ArrayList<AutoCompleteBean> getprTransferNos(Integer prTransferId, String theProductText, Integer wareHouseId) throws ProductException
	{
		String aQry = "SELECT pm.prMasterID,pm.ItemCode,pm.Description,pw.InventoryOnHand,pm.AverageCost FROM prWarehouseInventory pw JOIN prMaster pm ON pm.prMasterID=pw.prMasterID WHERE pw.prWarehouseID="+wareHouseId+" AND pm.ItemCode LIKE '%"+theProductText+"%'";
		Session aSession = null;
		logger.info("Query : "+aQry);
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aUserbean = new AutoCompleteBean();
		try {
			aQueryList = new ArrayList<AutoCompleteBean>();
			aSession = itsSessionFactory.openSession();
			aUserbean = new AutoCompleteBean();
			Query query = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = query.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				aUserbean = new AutoCompleteBean();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setLabel((String) aObj[1]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setMemo((String) aObj[2]);
				aUserbean.setInventoryOnHand((BigDecimal) aObj[3]);
				aUserbean.setInventoryCost((BigDecimal) aObj[4]);
				aQueryList.add(aUserbean);
			}
			if(aQueryList.size() < 1){
				logger.error("No Product is available with given Item code and Description: \n");
	
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public String getAdjustmentPrInfoDetails(Integer productNo, Integer prTransferId, Integer wareHouseId) throws ProductException
	{
		String str = "";
		String aQry = "SELECT p.prTransferDetailID, p.prTransferID, p.prMasterID, p.Description, pw.InventoryOnHand, p.QuantityTransfered" +
				" FROM prTransferdetail p LEFT JOIN prWarehouseInventory pw ON p.prMasterID = pw.prMasterID" +
				" WHERE p.prTransferID= " + prTransferId+" AND p.prTransferDetailID = " + productNo+" AND pw.prWarehouseID="+wareHouseId;
		Session aSession = null;
		logger.info("Query : "+aQry);
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aQry);
			Prtransferdetail prtransferdetail = new Prtransferdetail();
			Iterator<?> aIterator = query.list().iterator();
			Object[] aObj = (Object[])aIterator.next();
			str = aObj[0]+"@"+aObj[1]+"@"+aObj[2]+"@"+aObj[3]+"@"+aObj[4];
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return str;
	}
	
	public List<Prtransfer> getinventoryTransferData(Integer prTransferID) throws ProductException {
		Session aSession =null;
		String Qry = "SELECT prTransferID,TransferDate,prFromWarehouseID,prToWarehouseID,Description FROM prTransfer order by TransferDate ASC";
		List<Prtransfer> aprtransferList = null;
		Prtransfer aPrtransfer = null;
		try{
			aprtransferList = new ArrayList<Prtransfer>();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(Qry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPrtransfer = new Prtransfer();
				Object[] aObj = (Object[])aIterator.next();
				aPrtransfer.setPrTransferId((Integer) aObj[0]);			
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					java.sql.Date date = new java.sql.Date(stamp.getTime());
					aPrtransfer.setTransferDate(date);
				}
				else{
					aPrtransfer.setTransferDate((Date)aObj[1] );//(Date) aObj[1]
					}
				aPrtransfer.setPrFromWarehouseId((Integer) aObj[2]);
				aPrtransfer.setPrToWarehouseId((Integer) aObj[3]);
				aPrtransfer.setDescription((String) aObj[4]);
				aprtransferList.add(aPrtransfer);
			}
			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			Qry = null;
		}
		return  aprtransferList;
}

	
	public PrwarehouseTransfer createInventoryAdjustment(PrwarehouseTransfer aPrwarehouseTransfer){
		Session itssession=itsSessionFactory.openSession();
		PrwarehouseTransfer aprwhtf=null;
		Transaction atransaction=null;
		int id=0;
		try {
			 atransaction=itssession.beginTransaction();
			atransaction.begin();
			id =  (Integer) itssession.save(aPrwarehouseTransfer);
			aprwhtf = (PrwarehouseTransfer) itssession.get(PrwarehouseTransfer.class, id); 
			atransaction.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			itssession.flush();
			itssession.close();
		}
		return aprwhtf;
	}
	
	public void updateInventoryAdjustment(PrwarehouseTransfer aPrwarehouseTransfer){
		Session itssession=itsSessionFactory.openSession();
		PrwarehouseTransfer aprwhtf=null;
		Transaction atransaction=null;
		int id=0;
		try {
			 atransaction=itssession.beginTransaction();
			atransaction.begin();
			itssession.update(aPrwarehouseTransfer);
			atransaction.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			itssession.flush();
			itssession.close();
		}
	}
	
	
	public PrwarehouseTransfer getPrwarehouseTransfer(int id){
		Session itssession=itsSessionFactory.openSession();
		PrwarehouseTransfer aprwhtf=null;
		Transaction atransaction=null;
		try {
			 atransaction=itssession.beginTransaction();
			atransaction.begin();
			aprwhtf = (PrwarehouseTransfer) itssession.get(PrwarehouseTransfer.class, id); 
			atransaction.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			itssession.flush();
			itssession.close();
		}
		return aprwhtf;
	}
	
	@Override
	public boolean addPrwhtransferdetail(Prwhtransferdetail aPrwhtransferdetail,Integer warehouseListID) throws InventoryException  {
		Session aPrwhtdetailSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		boolean returnvalue=false;
		Integer PrwhtransferdetailID = null;
		BigDecimal oldQuantityTransfered = null;
		try {
			aTransaction = aPrwhtdetailSession.beginTransaction();
			aTransaction.begin();
            Prwhtransferdetail prwhtdetail =new Prwhtransferdetail();
			
		            if(aPrwhtransferdetail.getPrTransferDetailId()>0 ){
		            	 prwhtdetail = (Prwhtransferdetail) aPrwhtdetailSession.get(Prwhtransferdetail.class, aPrwhtransferdetail.getPrTransferDetailId());
		            	 oldQuantityTransfered = prwhtdetail.getDifference();
				            	 if(oldQuantityTransfered.compareTo(aPrwhtransferdetail.getDifference())!=0){
				            	 prwhtdetail.setPrTransferId(aPrwhtransferdetail.getPrTransferId());
				                 prwhtdetail.setPrMasterId(aPrwhtransferdetail.getPrMasterId());
				                 prwhtdetail.setItemCode(aPrwhtransferdetail.getItemCode());
				                 prwhtdetail.setDescription(aPrwhtransferdetail.getDescription());
				                 prwhtdetail.setQuantityAvailable(aPrwhtransferdetail.getQuantityAvailable());
				                 prwhtdetail.setQuantityTransfered(aPrwhtransferdetail.getQuantityTransfered());
				                 prwhtdetail.setInventoryCost(aPrwhtransferdetail.getInventoryCost());
				                 prwhtdetail.setDifference(aPrwhtransferdetail.getDifference());
				                 aPrwhtdetailSession.update(prwhtdetail);
				                 returnvalue=true;
				                 if(aPrwhtransferdetail.getPrTransferDetailId()>0){
				                	PrwarehouseTransfer thePrwarehouseTransfer = (PrwarehouseTransfer) aPrwhtdetailSession.get(PrwarehouseTransfer.class, aPrwhtransferdetail.getPrTransferId());
				 					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				 					aTpInventoryLog.setPrMasterID(aPrwhtransferdetail.getPrMasterId());
				 					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
				 					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				 					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
				 					aTpInventoryLog.setTransType("IA");
				 					aTpInventoryLog.setTransDecription("IA Edited");
				 					aTpInventoryLog.setTransID(aPrwhtransferdetail.getPrTransferId());
				 					aTpInventoryLog.setTransDetailID(aPrwhtransferdetail.getPrTransferDetailId());
				 					aTpInventoryLog.setProductOut(oldQuantityTransfered.compareTo(new BigDecimal("0.0000"))==1?oldQuantityTransfered:new BigDecimal("0.0000"));
				 					aTpInventoryLog.setProductIn(new BigDecimal("0.0000"));
				 					aTpInventoryLog.setUserID(thePrwarehouseTransfer.getCreatedByID());
				 					aTpInventoryLog.setCreatedOn(thePrwarehouseTransfer.getCreatedOn());
				 					inventoryService.saveInventoryTransactions(aTpInventoryLog);
				 					
				 					/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 *  
									 * */
									Session alogSession = itsSessionFactory.openSession();
									aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
									BigDecimal qo=oldQuantityTransfered;
									Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
									Prwarehouseinventory theprwarehsinventory=inventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
									TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
											aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
											BigDecimal.ZERO,BigDecimal.ZERO,"IA",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getDesc(),null ,
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
											"IA Edited",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
											new java.util.Date());
									inventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
									/*Code Ends*/
				 					
				 					aTransaction.commit();
				 					updateprMasterAndprWarehouseInventory_Adjustment(aPrwhtransferdetail,warehouseListID);
				 					aTpInventoryLog.setTransType("IA");
				 					aTpInventoryLog.setTransDecription("IA Edited");
				 					aTpInventoryLog.setProductIn(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
				 					aTpInventoryLog.setProductOut(new BigDecimal("0.0000"));
				 					inventoryService.saveInventoryTransactions(aTpInventoryLog);
				 					
				 					/*TpInventoryLogMaster
									 * Created on 04-12-2015
									 * Code Starts
									 * */
									alogSession = itsSessionFactory.openSession();
									aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
									qo=aPrwhtransferdetail.getDifference();
									theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
									theprwarehsinventory=inventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
									TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
											aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
											BigDecimal.ZERO,BigDecimal.ZERO,"IA",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getDesc(),null ,
							/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
							/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
											"IA Edited",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
											new java.util.Date());
									inventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
									/*Code Ends*/
				 					
				                 }
				            	 }
		                 
		            }
			else{
				PrwhtransferdetailID = (Integer) aPrwhtdetailSession.save(aPrwhtransferdetail);
            	aTransaction.commit();
            	updateprMasterAndprWarehouseInventory_Adjustment(aPrwhtransferdetail,warehouseListID);
            	if(PrwhtransferdetailID>0){
					PrwarehouseTransfer thePrwarehouseTransfer = (PrwarehouseTransfer) aPrwhtdetailSession.get(PrwarehouseTransfer.class, aPrwhtransferdetail.getPrTransferId());
					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
					aTpInventoryLog.setPrMasterID(aPrwhtransferdetail.getPrMasterId());
					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
					aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("IA");
					aTpInventoryLog.setTransDecription("IA Created");
					aTpInventoryLog.setTransID(aPrwhtransferdetail.getPrTransferId());
					aTpInventoryLog.setTransDetailID(aPrwhtransferdetail.getPrTransferDetailId());
					aTpInventoryLog.setProductIn(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductOut(new BigDecimal("0.0000"));
					aTpInventoryLog.setUserID(thePrwarehouseTransfer.getCreatedByID());
					aTpInventoryLog.setCreatedOn(thePrwarehouseTransfer.getCreatedOn());
					inventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * */
					Session alogSession = itsSessionFactory.openSession();
					BigDecimal qo=aPrwhtransferdetail.getDifference();
					Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=inventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
							BigDecimal.ZERO,BigDecimal.ZERO,"IA",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getDesc(),null ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
							"IA Created",aPrwhtransferdetail.getUserId(),aPrwhtransferdetail.getUserName(),
							new java.util.Date());
					inventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
					
					
					}
			}
            
            
		} catch(Exception e){
			aTransaction.rollback();
			logger.error(e.getMessage(), e);
		} finally {
			aPrwhtdetailSession.flush();
			aPrwhtdetailSession.close();
		}
		
		
		return returnvalue;
	}
	
	public  void updateprMasterAndprWarehouseInventory_Adjustment(Prwhtransferdetail aPrwhtransferdetail,Integer warehouseListID){
		boolean isinventory=false;
		Session aPrwhtdetailSession = itsSessionFactory.openSession();
		BigDecimal total=new BigDecimal(0.00);
		Transaction aTransaction =null;
		try{
		aTransaction = aPrwhtdetailSession.beginTransaction();
		aTransaction.begin();
		 ArrayList<PurchaseOrdersBean> mylist=inventoryService.getOnHandListForInventoryAdjust(aPrwhtransferdetail.getPrMasterId(),warehouseListID);
		 for(PurchaseOrdersBean pob:mylist){
			 total=total.add(pob.getInventoryOnHand());
		 }
		
	
		Prmaster prmaster = (Prmaster) aPrwhtdetailSession.get(Prmaster.class, aPrwhtransferdetail.getPrMasterId());
		prmaster.setInventoryOnHand(total.add(aPrwhtransferdetail.getQuantityTransfered()));
				if(prmaster!=null && prmaster.getIsInventory()>0)
					isinventory=true;
				if(isinventory)
		aPrwhtdetailSession.update(prmaster);	
		aTransaction.commit();
			
		
		aPrwhtdetailSession =itsSessionFactory.openSession(); 
		String query = "SELECT * FROM prWarehouseInventory WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId()+ " AND prWarehouseID="+warehouseListID;
		Prwarehouseinventory aprPrwarehouseinventory = null;
			aTransaction = aPrwhtdetailSession.beginTransaction();
			Query aQuery = aPrwhtdetailSession.createSQLQuery(query);
			List<Prwarehouseinventory> aPrwhdetailList = aQuery.list();
		if(aPrwhdetailList != null)
		{
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				aprPrwarehouseinventory = new Prwarehouseinventory();
				aprPrwarehouseinventory.setPrWarehouseInventoryId((Integer) aObj[0]);
				aprPrwarehouseinventory = (Prwarehouseinventory) aPrwhtdetailSession.get(Prwarehouseinventory.class, aprPrwarehouseinventory.getPrWarehouseInventoryId());
				aprPrwarehouseinventory.setInventoryOnHand(aPrwhtransferdetail.getQuantityTransfered());
				if(isinventory)
				aPrwhtdetailSession.update(aprPrwarehouseinventory);	
			}
		}

		aTransaction.commit();
	} catch (Exception e) {
		aTransaction.rollback();
		logger.error(e.getMessage(), e);
	}finally {
		aPrwhtdetailSession.flush();
		aPrwhtdetailSession.close();
	}
	}
	
	@Transactional
	@Override
	public void deletePrwhtransferdetail(Prwhtransferdetail aPrwhtransferdetail,int warehouseListID) throws ProductException {
		Session aPrwhtdetailSession = null;
		org.hibernate.Transaction aTransaction = null;
		Prwhtransferdetail prwhtransferdetail = null;
		try{
			aPrwhtdetailSession = itsSessionFactory.openSession();
			aTransaction = aPrwhtdetailSession.beginTransaction();
			aTransaction.begin();
			prwhtransferdetail = (Prwhtransferdetail)aPrwhtdetailSession.get(Prwhtransferdetail.class,aPrwhtransferdetail.getPrTransferDetailId());
			prwhtransferdetail.setUserId(aPrwhtransferdetail.getUserId());
			prwhtransferdetail.setUserName(aPrwhtransferdetail.getUserName());
			aPrwhtransferdetail=prwhtransferdetail;
			aPrwhtdetailSession.delete(prwhtransferdetail);
			aTransaction.commit();
			
			aTransaction.begin();
			Prmaster thePrmaster = (Prmaster) aPrwhtdetailSession.get(Prmaster.class, aPrwhtransferdetail.getPrMasterId());
			thePrmaster.setInventoryOnHand(thePrmaster.getInventoryOnHand().add(aPrwhtransferdetail.getDifference().multiply(new BigDecimal(-1))));
			aPrwhtdetailSession.update(thePrmaster);
			Prwarehouseinventory thePrwarehouseinventory=inventoryService.getPrwarehouseInventory(warehouseListID, aPrwhtransferdetail.getPrMasterId());
			thePrwarehouseinventory.setInventoryOnHand(thePrwarehouseinventory.getInventoryOnHand().add(aPrwhtransferdetail.getDifference().multiply(new BigDecimal(-1))));
			aPrwhtdetailSession.update(thePrwarehouseinventory);
			aTransaction.commit();
			
			if(aPrwhtransferdetail.getPrTransferDetailId()>0){
				PrwarehouseTransfer thePrwarehouseTransfer = (PrwarehouseTransfer) aPrwhtdetailSession.get(PrwarehouseTransfer.class, aPrwhtransferdetail.getPrTransferId());
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(aPrwhtransferdetail.getPrMasterId());
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
				aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(thePrwarehouseTransfer.getPrFromWarehouseId());
				aTpInventoryLog.setTransType("IA");
				aTpInventoryLog.setTransDecription("IA Deleted");
				aTpInventoryLog.setTransID(aPrwhtransferdetail.getPrTransferId());
				aTpInventoryLog.setTransDetailID(aPrwhtransferdetail.getPrTransferDetailId());
				aTpInventoryLog.setProductOut(aPrwhtransferdetail.getQuantityTransfered().compareTo(new BigDecimal("0.0000"))==1?aPrwhtransferdetail.getQuantityTransfered():new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(thePrwarehouseTransfer.getCreatedByID());
				aTpInventoryLog.setCreatedOn(thePrwarehouseTransfer.getCreatedOn());
				inventoryService.saveInventoryTransactions(aTpInventoryLog);
				
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 *  
				 * */
				Session alogSession = itsSessionFactory.openSession();
				aPrmaster =  productService.getProducts(" WHERE prMasterID="+aPrwhtransferdetail.getPrMasterId());
				BigDecimal qo=aPrwhtransferdetail.getDifference();
				Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,thePrwarehouseTransfer.getPrFromWarehouseId());
				Prwarehouseinventory theprwarehsinventory=inventoryService.getPrwarehouseInventory(thePrwarehouseTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
				TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
						aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),thePrwarehouseTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
						BigDecimal.ZERO,BigDecimal.ZERO,"IA",thePrwarehouseTransfer.getPrTransferId(),aPrwhtransferdetail.getPrTransferDetailId(),thePrwarehouseTransfer.getDesc(),null ,
		/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
		/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
						"IA Deleted",prwhtransferdetail.getUserId(),prwhtransferdetail.getUserName(),
						new java.util.Date());
				inventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
				/*Code Ends*/
				}
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ProductException(e.getMessage(), e);
		} finally {
			aPrwhtdetailSession.flush();
			aPrwhtdetailSession.close();
		}
		
	}
	
	public boolean saveGLTransaction(PrwarehouseTransfer aprwhtf,BigDecimal cdtotal,boolean result,String username) throws ParseException, BankingException, CompanyException{
		
		Coledgersource aColedgersource = gltransactionService.getColedgersourceDetail("IA");
		
		

		
/*		String entrydate=null;
		Date transferDate=null;
		if(aprwhtf!=null)
			transferDate=aprwhtf.getTransferDate();
		
		System.out.println("transferDate "+transferDate);
		
		if(transferDate!=null && !transferDate.trim().equals("")){
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date convertedCurrentDate = sdf1.parse(transferDate);
		    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    entrydate=sdff.format(convertedCurrentDate );
		    System.out.println("Formated Date:"+entrydate);
		}*/
		
		Cofiscalperiod cofiscalperiod=null;
		Cofiscalyear aCofiscalyear =null;
		Coaccount coaccount1=null;
		Coaccount coaccount2=null;
		int id1 = 0,id2=0;
		
		cofiscalperiod=accountingCyclesService.getAllOpenPeriods(aprwhtf.getTransferDate());
		
		if(cofiscalperiod!=null)
			aCofiscalyear = accountingCyclesService.getCurrentYear(cofiscalperiod.getCoFiscalYearId());
		
			
		if(result)
		{
			GlRollback glRollback = new GlRollback();
			glRollback.setVeBillID(aprwhtf.getPrTransferId());
			glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
			glRollback.setPeriodID(cofiscalperiod.getCoFiscalPeriodId());
			glRollback.setYearID(cofiscalperiod.getCoFiscalYearId());
			glRollback.setTransactionDate(aprwhtf.getTransferDate());
			gltransactionService.rollBackGlTransaction(glRollback);
		}	
		
		if(aprwhtf!=null)
			coaccount1=gltransactionService.getCoaccountDetailsBasedoncoAccountid(aprwhtf.getCoAccountID());
		
		if(aprwhtf!=null)
			coaccount2=gltransactionService.getCoaccountDetailsBasedoncoAccountid(aprwhtf.getAdjustCogAccountID());
		
		GlTransaction glTransaction = new GlTransaction();
		GlLinkage glLinkage = new GlLinkage();

		
		glTransaction.setCoFiscalPeriodId(cofiscalperiod.getCoFiscalPeriodId());
		glTransaction.setPeriod(cofiscalperiod.getPeriod());
		glTransaction.setpStartDate(cofiscalperiod.getStartDate());
		glTransaction.setpEndDate(cofiscalperiod.getEndDate());
		glTransaction.setCoFiscalYearId(aCofiscalyear.getCoFiscalYearId());
		glTransaction.setFyear(aCofiscalyear.getFiscalYear());
		glTransaction.setyEndDate(aCofiscalyear.getEndDate());
		glTransaction.setyStartDate(aCofiscalyear.getStartDate());
		glTransaction.setJournalId("IA");
		glTransaction.setJournalDesc("Inventory Adjustment");
		glTransaction.setEntrydate(new Date());
		glTransaction.setTransactionDate(aprwhtf.getTransferDate());
		glTransaction.setEnteredBy(username);
		
		
		glTransaction.setPoNumber(aprwhtf.getPrTransferId().toString());
		glTransaction.setTransactionDesc("ADJ:"+aprwhtf.getDesc());
		
		glTransaction.setCoAccountId(coaccount1.getCoAccountId());
		glTransaction.setCoAccountNumber(coaccount1.getNumber());
		
		if(cdtotal.compareTo(BigDecimal.ZERO) < 0){
			glTransaction.setCredit(cdtotal.negate());
		    glTransaction.setDebit(new BigDecimal(0.00));}
		else{
			  glTransaction.setDebit(cdtotal);
			  glTransaction.setCredit(new BigDecimal(0.00));}
	
		if(cdtotal.negate().compareTo(BigDecimal.ZERO)!= 0)
		id1=gltransactionService.saveGltransactionTable(glTransaction);
		
		glLinkage.setCoLedgerSourceId(aColedgersource
				.getCoLedgerSourceId());
		glLinkage.setGlTransactionId(id1);
		glLinkage.setEntryDate(aprwhtf.getTransferDate());
		glLinkage.setVeBillID(aprwhtf.getPrTransferId());
		glLinkage.setStatus(0);

		if(cdtotal.negate().compareTo(BigDecimal.ZERO)!=0)
		gltransactionService.saveGlLinkageTable(glLinkage);
		
		
		glTransaction.setCoAccountId(coaccount2.getCoAccountId());
		glTransaction.setCoAccountNumber(coaccount2.getNumber());
		
		if(cdtotal.compareTo(BigDecimal.ZERO) < 0){
			glTransaction.setDebit(cdtotal.negate());
			 glTransaction.setCredit(new BigDecimal(0.00));
		}
		else{
			 glTransaction.setCredit(cdtotal);
			 glTransaction.setDebit(new BigDecimal(0.00));
		}
		
		if(cdtotal.negate().compareTo(BigDecimal.ZERO)!= 0)
		id2=gltransactionService.saveGltransactionTable(glTransaction);
		
		glLinkage.setGlTransactionId(id2);
		
		if(cdtotal.negate().compareTo(BigDecimal.ZERO)!= 0)
		gltransactionService.saveGlLinkageTable(glLinkage);
		
		
		return true;
		
	}

	@Override
	public void insertCountInventory(List<Prinventorycount> objPrinventorycount,Integer prWarehouseID,Integer createdByID,String createdByName) {
		Session aSession = null;
		Transaction aTransaction = null;
		Integer prTransferDetailID = null;
		try{
			aSession =itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
			Date date = Calendar.getInstance().getTime();
			PrwarehouseTransfer aprTransfer=new PrwarehouseTransfer();
			aprTransfer.setDesc("System Generated Inventory Count");
			aprTransfer.setTransferDate(date);
			aprTransfer.setCreatedByID(createdByID);
			aprTransfer.setCreatedOn(date);
			aprTransfer.setPrFromWarehouseId(prWarehouseID);
			aprTransfer.setScreenno(3);
			Integer prtransferid = (Integer)aSession.save(aprTransfer);
			aTransaction.commit();
			BigDecimal Amount=BigDecimal.ZERO;
			BigDecimal overallQuantity=BigDecimal.ZERO;
		for(Prinventorycount aNewPrinventorycount:objPrinventorycount){
			BigDecimal oldprWarehouseInventoryQty=BigDecimal.ZERO;
			
			Prwarehouseinventory aprwarehousinventory = (Prwarehouseinventory)aSession.get(Prwarehouseinventory.class, aNewPrinventorycount.getPrwarehouseInventoryID());
			if(aprwarehousinventory!=null){
				aTransaction.begin();
				oldprWarehouseInventoryQty=aprwarehousinventory.getInventoryOnHand();
				aprwarehousinventory.setInventoryOnHand(aNewPrinventorycount.getCountedOnHand());
				aSession.update(aprwarehousinventory);
				aTransaction.commit();
			}
			
			Query query = aSession.createSQLQuery("select InventoryOnHand,prMasterID FROM prWarehouseInventory where prMasterID=" +  aNewPrinventorycount.getPrMasterId());
			// Retrieve all
			Iterator<?> aIterator = query.list().iterator();
			BigDecimal prmasteronHand=BigDecimal.ZERO;
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				prmasteronHand=prmasteronHand.add(aObj[0]!=null?(BigDecimal)aObj[0] :BigDecimal.ZERO);
			}
			
			aTransaction.begin();
			Prmaster newPrmaster = (Prmaster)aSession.get(Prmaster.class,  aNewPrinventorycount.getPrMasterId());
				newPrmaster.setInventoryOnHand(prmasteronHand);
				aSession.update(newPrmaster);	
				aTransaction.commit();
			
			
			Prinventorycount aPrinventorycount = (Prinventorycount)aSession.get(Prinventorycount.class,  aNewPrinventorycount.getPrMasterId());
			if(aPrinventorycount!=null){
				aTransaction.begin();
				aPrinventorycount.setSystemOnHand(aNewPrinventorycount.getCountedOnHand());
				aSession.update(newPrmaster);	
				aTransaction.commit();
			}else{
				aTransaction.begin();
				Prinventorycount Prinventorycountobj = new Prinventorycount();
				Prinventorycountobj.setItemCode(newPrmaster.getItemCode());
				Prinventorycountobj.setPrMasterId(newPrmaster.getPrMasterId());
				Prinventorycountobj.setDescription(newPrmaster.getDescription());
				Prinventorycountobj.setPrDepartmentId(newPrmaster.getPrDepartmentId());
				Prinventorycountobj.setSystemOnHand(newPrmaster.getInventoryOnHand());
				aSession.save(Prinventorycountobj);
				aTransaction.commit();
			}
			aTransaction.begin();
			Prwhtransferdetail aPrtransferdetail = new Prwhtransferdetail();
			aPrtransferdetail.setPrMasterId( aNewPrinventorycount.getPrMasterId());
			aPrtransferdetail.setPrTransferId(prtransferid);
			aPrtransferdetail.setDescription(newPrmaster.getDescription());
			aPrtransferdetail.setItemCode(newPrmaster.getItemCode());
			aPrtransferdetail.setInventoryCost(newPrmaster.getAverageCost());
			aPrtransferdetail.setQuantityTransfered(oldprWarehouseInventoryQty.subtract(aNewPrinventorycount.getCountedOnHand()));
			prTransferDetailID = (Integer) aSession.save(aPrtransferdetail);
			aTransaction.commit();
			//BigDecimal currentQuantity=aPrtransferdetail.getQuantityTransfered().compareTo(BigDecimal.ZERO)>0?aPrtransferdetail.getQuantityTransfered():aPrtransferdetail.getQuantityTransfered().multiply(new BigDecimal(-1));
			Amount=Amount.add((newPrmaster.getAverageCost()==null?BigDecimal.ZERO:JobUtil.floorFigureoverall(((newPrmaster.getAverageCost()).multiply(aPrtransferdetail.getQuantityTransfered().multiply(new BigDecimal(-1)))
					),2)));
			
			if(prTransferDetailID>0){
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(aNewPrinventorycount.getPrMasterId());
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aNewPrinventorycount.getPrMasterId());
				aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(prWarehouseID);
				aTpInventoryLog.setTransType("IC");
				aTpInventoryLog.setTransDecription("IC Created");
				aTpInventoryLog.setTransID(prtransferid);
				aTpInventoryLog.setTransDetailID(prTransferDetailID);
				aTpInventoryLog.setProductOut(oldprWarehouseInventoryQty.subtract(aNewPrinventorycount.getCountedOnHand()).compareTo(new BigDecimal("0.0000"))==-1?oldprWarehouseInventoryQty.subtract(aNewPrinventorycount.getCountedOnHand()).multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(oldprWarehouseInventoryQty.subtract(aNewPrinventorycount.getCountedOnHand()).compareTo(new BigDecimal("0.0000"))==1?oldprWarehouseInventoryQty.subtract(aNewPrinventorycount.getCountedOnHand()):new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(createdByID);
				aTpInventoryLog.setCreatedOn(new Date());
				inventoryService.saveInventoryTransactions(aTpInventoryLog);
				
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 * */
				Session alogSession = itsSessionFactory.openSession();
				BigDecimal qo=oldprWarehouseInventoryQty.subtract(aNewPrinventorycount.getCountedOnHand());
				Prwarehouse theprwarehouse=(Prwarehouse) alogSession.get(Prwarehouse.class,aprTransfer.getPrFromWarehouseId());
				Prwarehouseinventory theprwarehsinventory=inventoryService.getPrwarehouseInventory(aprTransfer.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
				TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
						aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aprTransfer.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
						BigDecimal.ZERO,BigDecimal.ZERO,"IC",prtransferid,prTransferDetailID,"",null ,
		/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO,
		/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
						"IC Created",createdByID,createdByName,
						new java.util.Date());
				inventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
				/*Code Ends*/
			}
		}
		String incordec="decrease";
		if(Amount.compareTo(BigDecimal.ZERO)>0){
			incordec="Increase";
		}else{
			Amount=Amount.multiply(new BigDecimal(-1));
		}
		if(!(Amount.compareTo(BigDecimal.ZERO)==0)){
		 gltransactionService.insertInventoryAdjustmentCount(prWarehouseID,prtransferid,Amount,createdByName,incordec);
		}
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}finally {
			aSession.flush();
			aSession.close();
		}
		
	}

	
}
