package com.turborep.turbotracker.projectsView.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.projectsView.ProjectBean;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.vendor.dao.Vepo;

public interface ProjectService {

	public ArrayList<JobsBean> getBookedSubmit(SalesRepBean theSalesRep);

	public ArrayList<JobsBean> getBookedPendingOrder(SalesRepBean theSalesRep);
	
	public ArrayList<JobsBean> getBookedPendingCredit(SalesRepBean theSalesRep);
	
	public ArrayList<JobsBean> getOrderTrackingDetails(SalesRepBean theSalesRep);
	
	public ArrayList<JobsBean> getDormantProjects(SalesRepBean theSalesRep);

	public ArrayList<BigDecimal> getCustomerARDetails(Integer tsUserLoginID);
	
	public ArrayList<BigDecimal> getCustomerUnappliedARDetails(Integer tsUserLoginID);
	
	public List<?> getOpenedJobs(Integer therxMasterID, Integer tsUserLoginID,int theFrom, int theTo, String sidx, String sordby,String fromDate, String todate);

	public int getOpenedJobs(Integer rxCustomerID, Integer tsUserLoginID,String fromdateDate, String todateDate);

	public List<?> getProfitMargin(Integer rxCustomerID, Integer tsUserLoginID,int aFrom, int aTo, String theSidx, String theSord,	String fromdateDate, String todateDate);
	

	public int getPurchaseSalesOrder(Integer therxMasterID,Integer tsUserLoginID,String fromDate,String todate);
	
	public List<Vepo> getPurchaseSalesOrder(Integer therxMasterID,Integer tsUserLoginID,int theFrom, int theTo,String sidx,String sordby,String fromDate,String todate) ;

	public BigDecimal getProjectPriorCalculation(Integer tsUserLoginID);

	public ArrayList<BigDecimal> getProjectCalculation(Integer tsUserLoginID);

	public int getProfitMarginTotal(Integer therxMasterID,Integer tsUserLoginID,int theFrom, int theTo,String sidx,String sordby,String fromDate,String todate);

	public ArrayList<ProjectBean> getCustomerAccountRecieveDetails(Integer tsUserLoginDetail,Integer customerID,String asofardate);
	
	//public int getCustomerAccountRecieveDetails(Integer tsUserLoginDetail);
	
	public ArrayList<ProjectBean> getARDetailsBasedonCustomer(Integer tsUserLoginDetail,Integer customerid,String inputdate,Integer customerName);
	
	public String getJoMasterData(Integer rxMasterId);
}
