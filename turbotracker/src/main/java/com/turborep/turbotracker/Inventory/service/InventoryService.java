/**
 * This Inventory Service is Carried out of Inventory Details. 
 * @author Madhan Kumar
 *
 */

package com.turborep.turbotracker.Inventory.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLog;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLogMaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.PrCategory;
import com.turborep.turbotracker.product.dao.PrDepartment;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.SalesOrderBean;
import com.turborep.turbotracker.vendor.exception.VendorException;

public interface InventoryService {
	
	/**
	 * This Mathod to Get the Total Number of Inventory Records
	 * @return {@link Integer}
	 * throws {@link InventoryException}
	 */
	public int getRecordCount() throws InventoryException;
	
	/**
	 * This Method to get the List of Inventory Details
	 * @param theInactive 
	 * @param thewarehouse 
	 * @param theSord 
	 * @param theSidx 
	 * @param aFrom
	 * @param aTo
	 * @return {@link List}
	 * throws {@link InventoryException}
	 */
	public List<?> getInventory(int theFrom, int theTo, Integer thewarehouse, String searchData, Integer theInactive, String theSidx, String theSord) throws InventoryException;

	/**
	 * This method to Get the List of Product Department
	 * @return {@link List}
	 * @throws InventoryException
	 */
	public List<PrDepartment> getPrDepartment() throws InventoryException;

	/**
	 * This method to Get the List of Product Category
	 * @return {@link List}
	 * @throws InventoryException
	 */
	public List<PrCategory> getPrCategory() throws InventoryException;

	/**
	 * This method to Get the Single Product Details Using Product ID
	 * @param theprMasterID
	 * @return {@link Prmaster}
	 * @throws InventoryException
	 */
	public Prmaster getSingleProductDetails(Integer theprMasterID) throws InventoryException;
	
	
	/**
	 * Auto Suggestion for Vendor Name
	 * @param theSearchString
	 * @return
	 * @throws InventoryException
	 */

	public ArrayList<AutoCompleteBean> getVendorList(String theSearchString) throws InventoryException;

	/**
	 * Delete Inventory Record
	 * @param theInventoryID
	 * @return {@link Boolean}
	 * @throws InventoryException
	 */
	public Boolean deleteInventoryDetails(Integer theInventoryID) throws InventoryException;

	/**
	 * Update Inventory Details
	 * @param aPrMaster
	 * @param aPrwarehouseinventory 
	 * @throws InventoryException
	 */
	public Boolean updateInventoryDetails(Prmaster thePrMaster, Prwarehouseinventory thePrwarehouseinventory) throws InventoryException;

	/**
	 * getPrWareID
	 * @param thePrwarehouseinventory
	 * @return 
	 * @throws InventoryException
	 */
	public Integer getPrWareID(Prwarehouseinventory thePrwarehouseinventory) throws InventoryException;

	/**
	 * get Single Ware Details
	 * @param theprMasterID
	 * @return {@link Prwarehouseinventory}
	 * @throws InventoryException
	 */
	public Prwarehouseinventory getSingleWareDetails(Integer theprMasterID) throws InventoryException;
	
	public Prwarehouseinventory getPrwarehouseinventoryDetails(Integer PrwarehouseinventoryID) throws InventoryException ;

	/**
	 * Add Inventory Details
	 * @param aPrMaster
	 * @param aPrwarehouseinventory
	 * @return {@link Boolean}
	 * @throws InventoryException
	 */
	public Integer addNewInventory(Prmaster aPrMaster, Prwarehouseinventory aPrwarehouseinventory) throws InventoryException;

	public Boolean updateInventory(Prmaster thePrMaster) throws InventoryException;

	/**
	 * 
	 * @return List of Categories
	 * @throws InventoryException
	 */
	public List<PrCategory> getInventoryCategories(String columnname,String ascordesc) throws InventoryException;
	public List getDepartmentAccountNo() throws InventoryException;

	public void addCategory(PrCategory aCategory) throws InventoryException;

	public void editCategory(PrCategory aCategory) throws InventoryException;

	public void deleteCategory(PrCategory aCategory) throws InventoryException;
	public List<PrDepartment> getInventoryDepartments() throws InventoryException;

	public void addDepartment(PrDepartment aDepartment)throws InventoryException;

	public void editDepartment(PrDepartment aDepartment)throws InventoryException;

	public void deleteDepartment(PrDepartment aDepartment)throws InventoryException;
	
	public ArrayList<AutoCompleteBean> getAccountList(String theSearchString,AutoCompleteBean theUserBean) throws InventoryException;
	
	public ArrayList<SalesOrderBean> getsalesOrderList(Integer theprMasterID) throws InventoryException;
	public ArrayList<PurchaseOrdersBean> getOnOrderList(Integer theprMasterID) throws InventoryException;
	public ArrayList<PurchaseOrdersBean> getOnHandList(Integer theprMasterID) throws InventoryException;
	public ArrayList<Prwhtransferdetail> getWarehouseTransferList(Integer theprTransferId) throws InventoryException;
	public List<Prmaster> getTransferLineItems(Integer prMasterID,Integer fromWareHouseId) throws InventoryException;
	public PrwarehouseTransfer saveTransferDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException;
	public PrwarehouseTransfer updateTransferDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException;
	public boolean saveTransferLineItems(PrwarehouseTransfer objPrwarehouseTransfer,Prwhtransferdetail aPrwhtransferdetail, String oper) throws InventoryException;
	public String updatePrMasterForReceiveItemsWTransfer(Integer prTransferId, String receiveOrUndo,int i, String username) throws JobException;
	public String copyTransferLineDetails(Integer prTransferID, Integer newPrTransferID) throws JobException;
	public PrwarehouseTransfer copyTransferDetails(PrwarehouseTransfer aPrwarehouseTransfer) throws InventoryException;
	public int getInventoryRecordCount(String itemCode) throws InventoryException;
	public int getInventoryRecordCount(Integer thewarehouse, String searchData, Integer theInactive, String theSidx,String theSord) throws InventoryException;
	public BigDecimal getWarehouseCost(Integer prMasterID) throws JobException;
	public PrCategory getprcategorymarkup(Integer theprCategoryID)	throws InventoryException;
	
	public boolean saveInventorysettings(ArrayList<Sysvariable> sysvariablelist) throws InventoryException;

	public PrCategory getprCategories(Integer prcategoryId) throws InventoryException;

	public Integer getRecordsCountforReceiveInvenory(int warehouseListID,
			String fromDateID, String toDateID, int prMasterID);

	List<PurchaseOrdersBean> filterInventoryTransactionList(
			String startDate, String endDate, int warehouseListID,
			int prMasterID, String sortIndex, String sortOrder,int from,int to )
			throws VendorException, ParseException;

	ArrayList<PurchaseOrdersBean> getOnHandListForInventoryAdjust(
			Integer theprMasterID, Integer theWarehouseID)
			throws InventoryException;

	public Prwarehouse getPrwarehouse(Integer PrwarehouseID) throws InventoryException;
	
	List<PurchaseOrdersBean> filterInventoryTransactionListSize(
			String startDate, String endDate, int warehouseListID,
			int prMasterID)
			throws VendorException, ParseException;
	
	public Integer saveInventoryTransactions(TpInventoryLog aTpInventoryLog) throws InventoryException;

	public Integer addTpInventoryLogMaster(TpInventoryLogMaster atpInventoryLogMaster)
			throws InventoryException;

	public Prwarehouseinventory getPrwarehouseInventory(Integer PrwarehouseID,
			Integer prmasterID) throws InventoryException;
	
	public String updateSysVariableSequence(Integer sysVariableID) throws InventoryException;
}
