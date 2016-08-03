package com.turborep.turbotracker.employee.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.employee.dao.CommissionStatementBean;
import com.turborep.turbotracker.employee.dao.Ecinvoiceperiod;
import com.turborep.turbotracker.employee.dao.Ecinvoicerepsplit;
import com.turborep.turbotracker.employee.dao.Ecinvoices;
import com.turborep.turbotracker.employee.dao.Ecjobs;
import com.turborep.turbotracker.employee.dao.Ecperiod;
import com.turborep.turbotracker.employee.dao.Ecsalesmgroverride;
import com.turborep.turbotracker.employee.dao.Ecsplitjob;
import com.turborep.turbotracker.employee.dao.Ecstatement;
import com.turborep.turbotracker.employee.dao.EmMasterBean;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.vendor.dao.Vebill;

public interface EmployeeServiceInterface {

	/**
	 * Retrieves all employee
	 * 
	 * @return list of employee
	 */
	public List<Rxmaster> getAll();

	/**
	 * Retrieves a single employee based on id
	 * 
	 * @param id the id of the employee
	 * @return the employee
	 */
	public Rxmaster get(String id);

	/**
	 * Add a new employee
	 * 
	 * @param employee the new employee
	 * 
	 * @return true if successful
	 */
	public Boolean add(Rxmaster employee);

	/**
	 * Delete an existing employee
	 * 
	 * @param employee the existing employee
	 * @return true if successful
	 */
	public Boolean delete(Rxmaster employee);
	
	/**
	 * Delete an existing employee
	 * 
	 * @param id employee id
	 * @return true if successful
	 */
	public Boolean delete(int id);

	/**
	 * Edit an existing employee
	 * 
	 * @param employee the existing employee
	 * @return true if successful
	 */
	public Boolean edit(Rxmaster employee);
	
	public ArrayList<?> getCSRs();
	
	public ArrayList<?> getSalesMGRs();
	
	public ArrayList<UserBean> getProjManagers();
	
	public ArrayList<UserBean> getEngineers();
	
	public ArrayList<UserBean> getQuotesBy();

	public List<CuMasterType> getCustomerType();

	public List<Prwarehouse> getWherehouseType();

	public List<CuTerms> getPaymentTerms();

	public ArrayList<AutoCompleteBean> getCSRList(String salesRep);

	public ArrayList<AutoCompleteBean> getSalesMGRList(String salesRep);

	public ArrayList<AutoCompleteBean> getProjectManagerList(String salesRep);

	public ArrayList<AutoCompleteBean> getEngineerList(String salesRep);

	public ArrayList<AutoCompleteBean> getQuotesList(String salesRep);

	public ArrayList<AutoCompleteBean> getPaymentTermType(String salesRep);

	public ArrayList<AutoCompleteBean> getCustomerType(String salesRep);

	public ArrayList<AutoCompleteBean> getWarehouseType(String salesRep);

	public Rxmaster addNewEmployee(Rxmaster aEmployee, Rxaddress aRxaddress);

	public boolean deleteEmployeeDetails(Rxmaster aEmployee) throws CompanyException;

	public Emmaster getEmployeeCommDetails(String theRolodexNumber);

	public List<Rxmaster> getrxMasterObject() throws EmployeeException;

	public Emmaster addemMaster(Emmaster emmaster);

	ArrayList<AutoCompleteBean> getTermsDueDate(Integer cuTermsID);
	/*--For Commission Calculation */
	public Ecperiod addNewCommissionPeriod(String periodDate);
	
	public Integer saveNewPeriod(Integer ecPeriodID);
	
	public int updateEcStatement(Integer newPeriodID,Integer ecStatementID) throws CompanyException;
	
	public ArrayList<Ecperiod> getPeriodEnding();
	
	public Sysinfo getSysinfoForCommissions() throws EmployeeException;
	
	public List<Ecperiod> getEcPeriodStartEnd(String condition) throws EmployeeException;
	
	public Boolean deleteCommissionsDetails(Integer ecPeriodID) throws EmployeeException;
	
	public Sysvariable getSysVariableDetails(String sysVariableName) throws EmployeeException;
	
	public List<Jomaster> getJobDetails(String startPeriodDate, String endPeriodDate) throws EmployeeException;
	
	public Integer calCulateJobCommissions(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException ;
	
	public Ecjobs getEcJobsDetails(Integer joMasterID) throws EmployeeException;
	
	public Ecstatement getEcStatement(Integer aEcperiod,Integer rxmasterid) throws EmployeeException;

	public Emmaster getEmmasterDetails(Integer LoginUserID) throws EmployeeException;
	
	public Integer calculatePaidInvoices(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public List<Ecinvoices> getEcInvoiceDetails() throws EmployeeException;
	
	public List<Ecinvoicerepsplit> getEcInvoiceRepSplitDetails(String Condition) throws EmployeeException;
	
	public List<Ecinvoiceperiod> getEcInvoicePeriodDetails() throws EmployeeException;
	
	public ArrayList<CuLinkageDetail> getCuLinkageDetails(Cuinvoice aCuinvoice,String endPeriodDate,String startPeriodDate) throws EmployeeException;
	
	public ArrayList<Vebill> getVeBillDetails(Cuinvoice aCuinvoice) throws EmployeeException;
	
	public List<Ecsplitjob> getEcSplitJobDetails(Integer joMasterID) throws EmployeeException;
	
	public Ecstatement getSaveEcStatement(Integer ecPeriodID,Integer loginRepID);
	
	public Integer calculateWarehouseInvoices(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public List<Ecstatement> getEcStatementPeriodList(Integer aEcperiod) throws EmployeeException;
	
	public BigDecimal getBigDecimalValue(String strQuery) throws JobException;
	
	public Integer calculateCommissionTotals(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList,String calculatePayment) throws EmployeeException;
	
	public Integer getLastEcPeriod() throws EmployeeException;
	
	public Integer checkPeriodExists(String periodDate);
	
	public List<Ecsplitjob> getEcSplitJobEmMasterDetails(Integer joMasterID,Integer cuInvoiceID,String Type) throws EmployeeException;
	
	public Integer calculateFactoryCommissions(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public List<Ecsalesmgroverride> getEcSalesMgrOverrideList() throws EmployeeException;
	
	public Ecjobs getEcJobsStatementID(Integer ecStatementID) throws EmployeeException;
	
	public Integer calCulateOverrides(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public Integer removeInactiveEntries(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public List<Cofiscalperiod> getCoFiscalPeriodList() throws EmployeeException;
	
	public Integer calculateMiscJobs(String startPeriodDate, String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public Integer reverseCommissionPeriod(String startPeriodDate,String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public Integer calCulateJobReleaseCommissions(String startPeriodDate,String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public List<Rxmaster> getrxMasterTableValues() throws EmployeeException;
	
	public ArrayList<Ecstatement> getEmployeeCommissionsStatement(Integer ecStatementID);
	
	public List<Ecperiod> isPeriodsExists(Integer ecPeriodID) throws EmployeeException;

	public Integer CheckEmployeeCommissionCount(Integer ecstatementid)throws EmployeeException;
	
	public ArrayList<CommissionStatementBean> getCommissionStatements(Integer ecStatementID);
	
	public Map<String,BigDecimal> getCommissionsStatementTotals(Integer ecStatementID);
	
	public Integer calculateAddedCostCommission(String periodStartDate, String periodEndDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public Integer calculateUnpaidInvoiceAddedCost(String periodStartDate,String periodEndDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public Integer calculatePartialPaidInvoiceAddedCost(String periodStartDate,String periodEndDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
	
	public Integer savePaidInvoicesFromSQLView(String startPeriodDate,String endPeriodDate,List<Ecperiod> ecPeriodList) throws EmployeeException;
}