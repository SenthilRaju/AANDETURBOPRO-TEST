package com.turborep.turbotracker.company.service;

import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.dao.SimpleCompanyBean;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.exception.VendorException;

public interface CompanyService {

	public List<SimpleCompanyBean> getRXArchitectList() throws CompanyException;
	
	public List<SimpleCompanyBean> getRXEngineerList() throws CompanyException;
	
	public List<SimpleCompanyBean> getRXGCList() throws CompanyException;
	
	public List<Codivision>	getCompanyDivisions() throws CompanyException;
	
	public List<CoTaxTerritory>	getCompanyTaxTerritory() throws CompanyException;

	public ArrayList<AutoCompleteBean> getEngineerRxList(String salesRep) throws CompanyException;

	public ArrayList<AutoCompleteBean> getArchitectRxList(String salesRep) throws CompanyException;

	public ArrayList<AutoCompleteBean> getGCRXList(String salesRep) throws CompanyException;

	public ArrayList<AutoCompleteBean> getCompanyTaxList(String salesRep) throws CompanyException;

	public String getRolodexName(Integer rxCategory1, String tablename) throws CompanyException;

	public String getCustomerName(Integer rxCustomerId) throws CompanyException;

	public List<CoTaxTerritory> getCompanyTaxTerritory(Integer coTaxTerritoryId, String tableNameRX) throws CompanyException;

	public Rxmaster getEmployeeDetails(String theRolodexNumber) throws CompanyException;

	public Rxaddress getEmployeeAddressDetails(String theRolodexNumber) throws CompanyException;

	public Integer getRxMasterID(String locationName) throws CompanyException;

	public String getEmailID(Integer aCustomerID) throws CompanyException;

	public Rxcontact updateEmailAddr(Rxcontact aRxcontact) throws CompanyException;
	
	public List<CuTerms> getCuTermsList() throws CompanyException;

	public List<?> getFreightCharges() throws CompanyException;

	public Vefreightcharges updateFreightChargesService(Vefreightcharges theRxcontact,Boolean isAdd) throws CompanyException;

	public void deleteFreightChargesService(Integer theFreightId)throws CompanyException;
	
	public List<?> getcotaxterritoryList() throws CompanyException;
	
	public List<?> getAddressShippingList(int theRolodexNumber) throws CompanyException;
	
	public List<?> getcuDefaultTerritoryList(int theRolodexNumber) throws CompanyException;
	
	public List<String> getsalesDetails(int rxmasterid) throws CompanyException;
	
	public ArrayList<Rxaddress> getAllAddress(Integer rxMasterID)
			throws CompanyException;

	public Boolean getdbVersion(String appversion,String dbversion);

	public int getCount(String query) throws VendorException;

	public int getNewQueryCount(String query);
	
}
