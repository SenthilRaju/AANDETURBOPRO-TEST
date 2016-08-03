package com.turborep.turbotracker.vendor.service;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.JobPurchaseOrderBean;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.WarehouseTransferBean;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.exception.VendorException;

public interface PurchaseService {

	public List<PurchaseOrdersBean> getAllPOsList(int from, int to, String searchData, String fromdate, String todate, String sortIndex, String sortOrder) throws VendorException;
	
	public Integer getRecordsCount()throws VendorException;
	public Integer getRecordsCountforReceiveInvenory() throws VendorException;
	
	public String getManufactureName(Integer theRxMasterId) throws VendorException;

	public Integer getRxMasterID(Integer aVePOId) throws VendorException;

	public List<Rxcontact> getContactList(Integer theRxMasterId) throws VendorException;
	public List<Rxcontact> getContactList() throws VendorException;

	public JobPurchaseOrderBean getPurchaseDetails(Integer theVepoID) throws VendorException;
	
	public Rxaddress getRxaddress(Integer theRxMasterId) throws VendorException;
	
	//Added by CGS
	public List<VeFactory> getManufactureNames(Integer rxMasterID) throws VendorException;
	public List<Rxmaster> getOrderedByName() throws VendorException;
	public List<Rxcontact> getATTNName(Integer rxMasterID) throws VendorException;
	public TsUserSetting getBilltoAddress() throws VendorException;
	public List<Rxaddress> getCustomerAddress(Integer rxMasterID) throws VendorException;
	public List<Rxaddress> getVendorName(Integer rxMasterID) throws VendorException;
	public List<Prmaster> getLineItems(Integer prMasterID) throws VendorException;
	public String getVendor(Integer theRxMasterId) throws VendorException;
	public String getManufacture(Integer theRxMasterId) throws VendorException;
	public CoTaxTerritory getSingleTaxTerritory(String country)	throws CompanyException;
	public CoTaxTerritory getSingleTaxTerritory(Integer TaxTerritoryID) throws CompanyException;
	public List<PurchaseOrdersBean> getAllSOsList(int from, int to,String searchData,String startDate,String endDate,String theSidx,String theSord) throws VendorException;
	public List<Rxaddress> getCustomerAddresses(Integer rxMasterID) throws VendorException;
	public Rxaddress getCustomerAddressauto(Integer rxMasterID) throws VendorException;
	public Integer getSORecordsCount()throws VendorException;
	public Integer getVepoIDFRomPoNumber(String PoNumber) throws VendorException;
	public Vepo getVePo(Integer theVePOID) throws VendorException;
	public Integer getCuInvoiceRecordsCount();
	public Integer getCuInvoiceRecordsCountWithDate(String fromdate,String todate,String searchData);
	public List<Cuinvoice> getAllCuInvoicesList(int from, int to,String sortIndex, String sortOrder,String searchData,String startDate,String endDate) throws VendorException;
	public List<Prmaster> getLineItemsSO(Integer prMasterID) throws VendorException;
	public List<Rxcontact> getEmailList(Integer rxMasterID) throws VendorException;
	public List<PurchaseOrdersBean> filterPOsList(int from, int to,String searchDate,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException, ParseException;
	public List<Rxmaster> getRxMasterName(Integer prMasterID) throws VendorException;
	public List<WarehouseTransferBean> getAllWarehouseTransfer(int from, int to) throws VendorException;
	public Integer getRecordsCountForWhTransfer()throws VendorException;
	public List<Prmaster> getLineItemsForSOTemplate(Integer prMasterID) throws VendorException;

	Rxaddress getCustomerAddressdetails(Integer rxMasterID)
			throws VendorException;
	
	public List<Cumaster> getJobCUInvoiceEmailList(Integer rxMasterID) throws VendorException;
	
	public Integer updatePOStatus(Integer cuSoId, Integer status) throws VendorException;
	
	public Integer getReceiveID(Integer vePOID)throws VendorException;
	
	public Vepodetail getPOinvoiceStatus(Integer vePoId) throws VendorException;
	
	public Integer getRxCustomerID(Integer joReleaseID) throws VendorException;
	
	public List<TsUserLogin> getOrderedByLoginName() throws VendorException;
	
	public Integer getVendorRxMasterID(Integer aVePOId) throws VendorException;
	
	public String getJobNumberFromVePO(Integer aVePOId) throws VendorException;
	
	//ShipToAddress
	
	public Rxaddress getSelectedShiptoaddress(String type,Integer addressid)throws VendorException;

	public List<Cuinvoice> getAllcuInvoiceEmailList(String query);

	public void updateEmailInvoiceDate(Integer cuInvoiceId,Integer invORstmt, Boolean emailStatus);

	public List<Cuinvoice> getAllcuStatementEmailList(String query);
	
	
	
}
