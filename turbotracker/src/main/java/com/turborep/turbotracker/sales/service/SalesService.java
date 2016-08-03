/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.sales.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusotemplate;
import com.turborep.turbotracker.employee.dao.Ecstatement;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.system.dao.SysUserDefault;

/**
 * Service object for salesSrevice. This object will be used get sales related information. 
 * @author thulasi_ram
 */
public interface SalesService {
	
	/**
	 * Method for get the list of  sales reps . 
	 * @return {@link ArrayList} of SalesRepBean
	 */
	public ArrayList<?> getSalesRep();
	
	/**
	 * Method will be used to get the data of upcoming bids.
	 * @param theSalesRep {@link SalesRepBean}
	 * @return  {@link ArrayList} of JobsBean
	 */
	public ArrayList<?> getUpcoming(SalesRepBean theSalesRep);
	
	/**
	 * Method will be used to get the data of Pending quotes.
	 * @param theSalesRep {@link SalesRepBean}
	 * @return {@link ArrayList}  of JobsBean
	 */
	public ArrayList<?> getPending(SalesRepBean theSalesRep,Integer rxCustomerID);
	
	/**
	 * Method will be used to get the data of Awarded Contractors.
	 * @param theSalesRep {@link SalesRepBean}
	 * @return {@link ArrayList}  of JobsBean
	 */
	
	public ArrayList<?> getQuotedJobs(SalesRepBean theSalesRep,String sortBy,String sortOrder);
	
	/**
	 * Method will be used to get the data of Awarded Contractors.
	 * @param theSalesRep {@link SalesRepBean}
	 * @return {@link ArrayList}  of JobsBean
	 */
	public ArrayList<?> getAwarded(SalesRepBean theSalesRep);

	/**
	 * This Method is used to get the salesrep list for auto suggest search in all over the application.
	 * @param salesRep {@link String}
	 * @return {@link ArrayList} of salesreps for Search
	 */
	public ArrayList<AutoCompleteBean> getsalesrepList(String salesRep);

	public String getAssignedEmployeeName(Integer salesRepId, String tableName);

	public ArrayList<AutoCompleteBean> getCustomerFindProject(SalesRepBean aSalesRep);

	public Integer getsingleproject(String theCreated);

	public ArrayList<JobsBean> upcomingForSingleCustomer(SalesRepBean aSalesRep);

	public String getAssignedEmployee(Integer theSalesRepId);
	
	/*
	 * To get Customer Name List
	 */
	
	public ArrayList<AutoCompleteBean> getCustomerNameList(String theSalesRep);

	public Integer updateSalesOrderStatus(Integer cuSOId, Integer status,Integer userID,String userName);
	
	public ArrayList<AutoCompleteBean> getSalesOrderList(String salesOrder);
	
	public ArrayList<AutoCompleteBean> getDivisionlist(String divisionname);
	
	public Cuso updateEmailStampValue(Cuso theCuso);
	public List<Cusotemplate> getCusoTemplateList();
	public Map<String, BigDecimal> getTemplatePriceDetails(int CusoID,int cuSODetailID, int prMasterID);
	
	public Prmaster getInventoryAllocatedDetailsservice(int cuSOid,int prmasterid);
	
	public ArrayList<String> getsalesordershiptoAddressDetails(Cuso cusolist);
	
	public List<Ecstatement> getJobCommissionSplits(Integer joMasterID) throws JobException;
	public ArrayList<String> getsalesorder_soldtoAddressDetails(Cuso cusolist);

	public SysUserDefault getSysUserDefault(int userId);
	
	public JoReleaseDetail getJoReleaseDetails(Integer joReleaseDetailID);
	
	public String getPaidCheckRefs(Integer cuInvoiceID);

	public boolean updatesetBillOnlyStatus(Integer joReleaseID, Integer status);
	
	public Date getInvoicePaymentDetails(Integer cuInvoiceID);
	
	public Integer getQuotedPricePrMasterID();

	public boolean updateQuotedPriceIncuSoDetails(Integer cuSOID);
	
	public String getCommissionPaidDetails(Integer cuInvoiceID);

	public Boolean checkSalesOrderInvoicedornot(Integer cuSOId)throws JobException;

	public List<String> getListOfCCMailId(Integer userLoginId);;
}
	
