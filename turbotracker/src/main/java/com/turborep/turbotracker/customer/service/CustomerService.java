package com.turborep.turbotracker.customer.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.turborep.turbotracker.banking.dao.GlLinkage;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.customer.dao.Cureceipttype;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.dao.StatementsBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.employee.dao.RxMasterCategoryView;
import com.turborep.turbotracker.employee.dao.Rxmastercategory1;
import com.turborep.turbotracker.employee.dao.Rxmastercategory2;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.user.dao.UserBean;

public interface CustomerService {
	
	public Cumaster getCustomerDetails(int cuMasterId);
	
	public CuTerms getCuTerms(int cuMasterTermsId);
	
	public ArrayList<AutoCompleteBean> getcountyAndstate(String theCountyAndState);
	
	public List<CuMasterType> getcuMasterType();
	
	public BigInteger getPaymentsCount() throws CustomerException;
	
	public ArrayList<CustomerPaymentBean> getCustomerPaymentsList(int theFrom, int theRows,String sidx,String sort) throws CustomerException;
	
		
	public CuLinkageDetail getCuRecieptData(Integer cuMasterID, Integer recieptID,Boolean reversestatus) throws CustomerException;

	public String[] getJobDetails(String invoiceNumber) throws CustomerException;

	public Integer insertDivisions(Codivision theCodivision) throws CustomerException;

	public Integer updateInvoice(CuLinkageDetail theCulinkagedetail,Integer linkgeID,Integer invoiceID, BigDecimal opAmount,BigDecimal amtApplied,String oper) throws CustomerException;
	
	public List<StatementsBean> printSatatementsConfirmation(Integer stRxMasterID, Integer EndRxMasterID,String startCustName, String endCustName, Date excludeAfterDate,Boolean showCredit) throws CustomerException;

	public List<Cureceipttype> getRecieptType() throws CustomerException;

	public Integer savePayment(Cureceipt theCureceipt,Integer year,Integer period,UserBean username,String paidinvoiceDetails,BigDecimal discountamt) throws CustomerException;

	public void deletePayment(Cureceipt theCureceipt) throws CustomerException;

	public CuMasterType getCustomerType(int customerTypeId);
	
	public void updateCustomerTypes(Integer id, String code, String description, boolean active);
	
	public void deleteCustomerType(Integer cusTypeId);

	public Cumaster getUserbasedonCustomer(int cuMasterId);
	
	public List<?> getCustomerInvoices(Integer cuMasterID,Integer cuRecieptID, String oper,int theFrom, int theRows, String sidx, String sord) throws CustomerException;

	public ArrayList<CustomerPaymentBean> getCustomerPaymentsListSearch(int theFrom,int theRows,String sidx,String sord, String searchText, Date fromDate, Date toDate, String status) throws CustomerException;
	
	public ArrayList<CustomerPaymentBean> getCustomerUnappliedPaymentsListSearch(int theFrom,int theRows,String sidx,String sord, String searchText, Date fromDate, Date toDate, String status) throws CustomerException;
	
	public Integer getcountpaymentsListSearch(int theFrom,int theRows, String searchText, Date fromDate, Date toDate, String status) throws CustomerException;

	public Integer getcountUnappliedPaymentsListSearch(int theFrom,int theRows, String searchText, Date fromDate, Date toDate, String status) throws CustomerException;
		
	public ArrayList<CustomerPaymentBean> getCustomerPaymentUnapplied(int theFrom,int theRows,String sidx,String sord, Date fromDate, Date toDate, String status) throws CustomerException;
	
	public Integer getUnappliedPaymentCount() throws CustomerException;

	public List<?> getCustomerPaymentSearchList(AutoCompleteBean theSearchBean,String searchText, Date fromdate, Date todate, String status);

	public ArrayList getCustomerAR(Integer rxCustomerID);

	public List<?> getCustomerAddressBasedonrxAddress(int rxMasterID)throws CustomerException;
	
	public Rxaddress getRxaddressDetails(int rxAddressId)throws CustomerException;

	public boolean updateRxaddress(Rxaddress therxaddress) throws CustomerException;
	
	public CoTaxTerritory gettaxratedependsoncotax(int CoTaxTerritoryId)throws CustomerException;
	
	public void deleteCustomerAddress(Integer rxAddressId);

	public boolean Checkdefaultaddressstatus(int rxMasterID, int rxaddressId)throws CustomerException;

	public Rxmastercategory2 getrxMasterCategory2(int theCuMasterId) throws CustomerException;

	public Rxmastercategory1 getrxMasterCategory1(int rxAddressID)throws CustomerException;

	public RxMasterCategoryView getrxMasterCategoryView(int rxAddressID)throws CustomerException;
	
	public void updateCompleteInvoice(CuLinkageDetail theCulinkagedetail) throws CustomerException;
	
	public void postGl(Cureceipt cuReceiptObj,Integer yearID,Integer periodID,UserBean aUserBean,String paidStatus) throws CustomerException,BankingException;
	
	public void postcuLinkageDetail(Cureceipt cuReceiptObj,Integer yearID,Integer periodID,UserBean aUserBean,String paidInvoiceDetails,Session asession) throws CustomerException,BankingException;
	
	public BigInteger getcreditdebitmemoCount() throws CustomerException;
	
	public ArrayList<CustomerPaymentBean> getcreditordebitmemoList(int theFrom,
			int theRows,String column,String sortBy) throws CustomerException;
	
	
	public Integer addCreditDebitDetails(Cuinvoice acuInvoice) throws CustomerException;
	
	public void addCreditDebitDetailstoInvDetails(Cuinvoicedetail acuInvoicedet) throws CustomerException;
	
	public void updateCreditDebitDetails(Cuinvoice acuInvoice) throws CustomerException;
	
	public void updateCreditDebitDetailstoInvDetails(Cuinvoicedetail acuInvoicedet) throws CustomerException;
	
	public String getInvoiceNofromcuInvoice(int credittype) throws CustomerException;
	
	public ArrayList<GlLinkage> getglLinkagewithinvoiceId(int cuInvoiceId,Coledgersource coledgersource)throws CustomerException;
	
	public void memoPdfPrint(HttpServletResponse theResponse,HttpServletRequest theRequest)throws CustomerException,SQLException;
	
	public void updateCustomerInvoice(int invoiceID,String flag,UserBean aUserBean)throws CustomerException;

	public BigDecimal getYearTodateSale(Integer rxMasterID) throws CustomerException;
	
	public BigDecimal getLastYearSale(Integer rxMasterID) throws CustomerException;
	
	public Cuinvoice getLastSaleAmount(Integer rxMasterID) throws CustomerException;
	
	public Cuinvoice getCuInvoicefminvoiceno(String invoiceNumber)throws CustomerException;
	
	public boolean getreversePaymentvalidation(Integer receiptID)throws CustomerException;
	
	public boolean processReversePayments(Integer receiptID,UserBean aUserBean,Integer yearid,Integer periodid)throws CustomerException;

	public String getAllCustomerEmail(int rCuID,int invORstmt);
}
