package com.turborep.turbotracker.product.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prinventorycount;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prorderpoint;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.product.exception.ProductException;

public interface ProductService {

	public Prmaster getProducts(String whereClause) throws ProductException;

	 public List<Prwarehouse> getWarehouses() throws ProductException;
	 
	 public void updateWarehouseDetails(Prwarehouse thePrwarehouse) throws ProductException;

	 public void addWarehouse(Prwarehouse thePrwarehouse) throws ProductException;

	 public void deleteWarehouse(Integer prWarehouseId) throws ProductException;

	 public Prwarehouse getWareHouseAddress(Integer PrWareHouseId) throws JobException;
	 
	 public List<Prmaster> getOrderPoints(Integer prWarehouseId, String searchData, int aFrom, int aTo, int thePage, int theRows, String theSidx, String theSord) throws ProductException;
	 
	 public List<Prmaster> getSuggestedOrderPoints(Integer prWarehouseId,String searchData,int aFrom, int aTo) throws ProductException;
	 
	 public Integer getOrderPointsCount(Integer prWarehouseID, String searchData, int thePage, int theRows, String theSidx, String theSord) throws ProductException;
	 
	 public void updateOrderPoints(Prorderpoint aprorderpoint);
	 
	 public List<Prmaster> getcountInventory(Integer prWarehouseID, int aFrom,int aTo,Integer sortOrder);
	 
	 public Integer getcountInventoryCount(Integer prWarehouseId) throws ProductException;
	 
	 //public void insertCountInventory(Prinventorycount aprinventorycount, Integer prWarehouseID);
	 
	 public List<PrwarehouseTransfer> getprTransferData(Integer prTransferID) throws ProductException;
	 
	 public List<Prwhtransferdetail> getprTransferDetails(Integer prTransferId) throws ProductException;	 
	 
	 public ArrayList<AutoCompleteBean> getprTransferNos(Integer prTransferId, String theProductText, Integer wareHouseId) throws ProductException;
	 
	 public String getAdjustmentPrInfoDetails(Integer productNo, Integer prTransferId, Integer wareHouseId) throws ProductException;

	 public Integer getSuggestedOrderPointsCount(Integer prWarehouseID, String searchData, int thePage, int theRows, String theSidx, String theSord) throws ProductException;

	 public PrwarehouseTransfer createInventoryAdjustment(PrwarehouseTransfer aprwhtf);
	 
	 public void updateInventoryAdjustment(PrwarehouseTransfer aprwhtf);
	 
	 public PrwarehouseTransfer getPrwarehouseTransfer(int id);
	 
	 boolean addPrwhtransferdetail(Prwhtransferdetail aPrwhtransferdetail,Integer warehouseListID) throws InventoryException;
	 
	 boolean saveGLTransaction(PrwarehouseTransfer aprwhtf,BigDecimal cdtotal,boolean result,String username) throws ParseException, BankingException, CompanyException;
	 
	 public Prwarehouse getWarehouseDetail(Integer prWarehouseID) throws ProductException;

	void deletePrwhtransferdetail(Prwhtransferdetail aPrwhtransferdetail,
			int warehouseListID) throws ProductException;

	public void insertCountInventory(List<Prinventorycount> aNewPrinventorycount,Integer prWarehouseID,Integer createdByID,String userName);
}
