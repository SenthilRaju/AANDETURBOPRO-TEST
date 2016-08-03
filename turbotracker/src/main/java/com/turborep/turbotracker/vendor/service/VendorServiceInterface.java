package com.turborep.turbotracker.vendor.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.banking.dao.VeBillPaymentHistory;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prmaster;
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

/**
 * Service interface for Vendor related operations.
 * 
 * @author Vish Pepala
 *
 */
public interface VendorServiceInterface {

	public List<RolodexBean> getAll(int from, int to) throws VendorException;
	
	public int getVendorsCount() throws VendorException;

	public ArrayList<Rxcontact> getVendorContactDetails(Rxcontact aRxcontact) throws VendorException;

	public ArrayList<Rxaddress> getVendorAddressDetails(Rxaddress aRxaddress) throws VendorException;

	public ArrayList<VeFactory> getManufacturers(String rxMasterId) throws VendorException;
	
	public ArrayList<Vemaster> getVendorMaster(String rxMasterId) throws VendorException;

	public Rxmaster addNewVendor(Rxmaster theCustomer, Rxaddress theRxaddress) throws VendorException;

	public boolean addnewFactory(Rxmaster aNewVendor) throws VendorException;

	public VeFactory updateManufactures(VeFactory aVeFactory) throws VendorException;

	public String getDescription(Short thefactoryID) throws VendorException;
	
	public Vepo getVePo(Integer theVePOID) throws VendorException;

	public Rxcontact getContactDetails(Integer theContactID) throws VendorException;

	public Veshipvia getVeShipVia(Integer theShipViaID) throws VendorException;
	
	public ArrayList<VendorBillPayBean> getVendorBillsList(Integer rxMasterID, int thePage, int theRows, String theSidx, String theSord,String DisplayType) throws VendorException;
	
	public ArrayList<VendorBillPayBean> getVendorBillsQuickPayList(String rxMasterID,  Date dueDate) throws VendorException;

	public Vebillpay changeVendorBillList(Vebillpay aVendorBillsList, boolean isExisting,Integer userID) throws VendorException;

	public ArrayList<Vebillpay> getBillPayDetails(Integer thevebilID) throws VendorException;
	
	public Vemaster gettermsDiscountsfromveMaster(Integer theveMasterID) throws VendorException;

	public VeFactory getSingleVeFactoryDetails(Integer theManufacturerID) throws VendorException;

	public List<Vebilldetail> getVeBillLineItems(Integer veBillId) throws VendorException;

	public Vebill addVendorInvoiceDetails(Vebill theVebill, JoReleaseDetail theJoReleaseDetail,String isUpdate) throws VendorException;

	public Vebill updateVendorInvoiceDetails(Vebill theVebill, JoReleaseDetail aJoReleaseDetail,Vebilldetail veBillDetail,String isUpdate) throws VendorException;

	/**
	 * This method will call the stored procedure <b>veBilldetail_Insert</b>, which will get the records from vePODetails DB object and insert into veBillDetail object.
	 * @param vePOId
	 * @param veBillId
	 * @return {@link Boolean} value whether inserted or not.
	 * @throws VendorException
	 */
	public int insertLineItemsFromPODetailtoBillDetail(Integer vePOId,	Integer veBillId) throws VendorException;

	/**
	 * This method is used to save the vendor bill information.
	 * @param aVeBill
	 * @throws VendorException
	 */
	public void saveVendorBillDetail(Vebilldetail aVeBillDetail) throws VendorException;

	public void updateVendorBillDetail(Vebilldetail aVeBillDetail) throws VendorException;

	public void deleteVendorBillDetail(Vebilldetail aVeBillDetail)	throws VendorException;

	public List<Vebillpay> getVeBillPayList(Integer moAccountID,Integer userID) throws VendorException;

	public List<VendorBillsBean> getVeBillList(int from, int to,String searchData,String fromDate,String toDate,String theSidx,String theSord) throws VendorException;
	
	public BigInteger getBillsCount() throws VendorException;
	
	//Added to get Tax Rate and Tax Territory
	
	public CoTaxTerritory getTaxRateTerritory(Integer theCoTaxTerritory) throws VendorException;
	public Cuinvoice getCuInvoiceObj(Integer thecuInvoiceID) throws VendorException;
	public Rxcontact getContactIdFromMasterID(String theMasterId) throws VendorException ;
	public String checkInvoiceNumber(String theSONumber) throws VendorException;
	public Integer vepoDetailReceiveInventory(Vepodetail vepodetail,BigDecimal quantityUpdate,Integer veReceiveID,Date receivedDate) throws VendorException;

	public Integer vepoReceiveInventoryDate(Vepo vepodetail,Integer veReceiveID,Integer UserID,String UserName) throws VendorException;
	
	public List<Vereceive> getveReceiveDetails(Integer vepoID) throws VendorException ;

	public List<Vemaster> getAllVendors() throws VendorException;
	public Integer addVendorInvoice(Vebill aVebill,Vebilldistribution aVeDist,Integer yearID,Integer periodID,String username) throws VendorException;
	public ArrayList<AutoCompleteBean> getCoAccountDetails(String theProductName) throws JobException;
	public ArrayList<AutoCompleteBean> getJobList(String theProductName) throws JobException;
	public Vepo getPONumber(String thePONumber) throws JobException;
	public List<Vepodetail> getPOLineDetails(Integer theVepoID) throws JobException;
	public List<Rxaddress> getCustomerAddresses(Integer rxMasterID) throws VendorException;
	public List<Rxaddress> getVendorName(Integer rxMasterID) throws VendorException;
	public Integer insertVebill(Vebill aVebill) throws VendorException;
	public Integer insertVebillDetail(Integer veBillid, Integer vepoID,Integer userID) throws VendorException;
	public String saveVebillDetail(Vebilldetail aVebilldetail, String operation) throws VendorException;
	public List<Vebilldetail> getVendorInvoicelineitem(Integer theVeBillID)	throws JobException;
	public Vebill getVeBillObj(Integer veBillId) throws JobException;
	public Vepo getVepo(Integer vepoID) throws JobException;

	
	public List<Vebilldetail> getVeBillDetails(Integer whereClause) throws JobException;

	public String saveVebillDistribution(Vebilldistribution aVebilldistribution, String operation) throws VendorException;
	public List<Vebilldistribution> getVendorInvoiceList(Integer theVeBillID) throws JobException;
	
	public int updateVepoStatus(Integer vepoID,Integer status) throws VendorException;
	
	
	
	public Vebill getVeBill(Integer vePoID) throws VendorException;
	
	public void removeVendorBillPaylist(Integer theVeBillID)throws VendorException;
	
	public void clearAllCheckedBills(Integer theMoAccountid)throws VendorException;

	public boolean saveVeBillDetail(Vebilldetail avebilldetail) throws VendorException;

	public void updateJoReleaseDetail(JoReleaseDetail aJoReleaseDetail);

	public BigDecimal getVendorBillDetails(String duration,String aRxMasterId) throws VendorException;
	public Integer addJoReleaseDetail(JoReleaseDetail theJoReleaseDetail);

	public List<Vepodetail> getreorderclicklineitems(Integer vendorID,Integer wareHouseId, Integer vePOID);

	public Boolean UpdateVendorFinancialTab(Vemaster aVemaster);

	public Integer createManufactures(VeFactory aVeFactory)throws VendorException;

	public Boolean checkInvoiceNumberExist(String InvNo,Integer vePOID)throws VendorException ;
	
	public List<VeBillPaymentHistory> getveBillPaymentHistory(Integer veBillid)throws VendorException ;
	
	public Vebill getVebill(Integer theVePOID) throws VendorException;
	
	public List<VendorBillsBean> getUninvoiceList(int from, int to,String searchData,String fromDate,String toDate,String theSidx,String theSord) throws VendorException;

	public VendorBillsBean getVeBillListOutSide(Integer vebillId)throws VendorException;

	public Boolean getPoTotalequalsvendorinvoice(Integer VepoID) throws JobException;
	
	public String rollbackupdatePrMaster(Integer veBillId) throws JobException;

	public int getCount(String query) throws VendorException;
	
	public void updateveBillHistory(Integer veBillID) throws VendorException;

	public Boolean getCheckinvoiceNumberavlforvendor(Integer VendorID,String invnumber);
	
	public Boolean getChecklineitemisthereforrelease(Integer type,Integer primaryID)throws VendorException;
	
	public List<VendorBillsBean> getAccountsPayableList(int theFrom, int theTo,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException;
	public Integer getAccountsPayableCount(String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException;

	public Integer insertVebillHistory(Integer veBillid, Integer vepoID, Integer userID)
			throws VendorException;

	public void receivingMiscellaneousBill(Vebill aVebill, Vebilldistribution veDist,
			Integer yearID, Integer periodID, String username,String reason)
			throws BankingException, VendorException, CompanyException,
			JobException;
	
	public List<Vepodetail> getreorderclicklineitems(Integer vendorID,Integer wareHouseId);

	public Integer changeTransactionStatus(Integer veBillId);

}
