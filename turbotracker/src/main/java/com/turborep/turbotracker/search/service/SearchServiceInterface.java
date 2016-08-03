package com.turborep.turbotracker.search.service;

import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.search.dao.SearchBean;
import com.turborep.turbotracker.search.exception.SearchException;

public interface SearchServiceInterface {
	
	public  ArrayList<?> getRolodex();

	public List<?> getjobSearchResultsWithJob(String job);

	public void insert(List<?> jobSearch);

	public List<?> getJobSearch(SearchBean theSearchJob);

	public List<?> getRolodexSearchList(AutoCompleteBean theSearchRolodex, String theRolodex);

	public List<?> getrolodexSearchResultsWithRolodex(String rolodex, String searchText, String search);

	public List<?> getrolodexSearchResultsWithRolodexDetails(String rxNumber);

	public List<?> getJobSearchList(AutoCompleteBean aSearchBean, String theJob);

	public List<?> getRolodexSearchCustomerList(AutoCompleteBean theSearchJob, String job);

	public List<?> getRolodexSearchEmployeeList(AutoCompleteBean theSearchJob, String job);

	public List<?> getRolodexSearchVendorList(AutoCompleteBean theSearchJob, String job);

	public List<SearchBean> getLoginIDForSales(Integer rolodex);

	public List<?> getRolodexSearchCustomerListForUpComingBids(AutoCompleteBean theSearchJob, String job);

	public List<?> getRolodexSearchArchitectList(AutoCompleteBean theSearchJob, String theJob);

	public List<?> getRolodexSearchEngineerList(AutoCompleteBean theSearchJob, String theJob);

	public List<?> getJobSearchListHome(AutoCompleteBean theSearchJob, String job);

	public List<?> getSearchUserList(AutoCompleteBean theAutoCompleteBean, String theUserSearch);

	public List<?> getVendorBillPayList(AutoCompleteBean theSearchJob, String theJob);

	public List<?> getSearchAccountsList(AutoCompleteBean theAutoCompleteBean, String theAccountSearch);
	
	public List<AutoCompleteBean> getInventorySearch(String theSearchString) throws SearchException;

	public List<?> getSearchShipViaList(AutoCompleteBean theAutoCompleteBean, String theShipViaSearch) throws SearchException;

	public ArrayList<AutoCompleteBean> getUserInitialsList(String theUserLogin) throws SearchException;
	
	/**
	 * Service method to search POs and get results.
	 * @return
	 * @throws SearchException
	 */
	public List<AutoCompleteBean> searchPOs(String theSearchString) throws SearchException;
	
	//Added by CGS
	public List<AutoCompleteBean> getVendorSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getCustomerSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getPOSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getSOSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getCuInvoiceSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getCuNameSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getCuInvoiceIDSearch(String theSearchString) throws SearchException;
	public List<AutoCompleteBean> getWarehouseTransferSearch(String theSearchString) throws SearchException;
	public List<?> getJobSearchListContact(AutoCompleteBean theAutoCompleteBean,String theUserSearch);
	public List<AutoCompleteBean> getVendorInvoiceList(String theSearchString) throws SearchException;

	public List<AutoCompleteBean> getSearchCustomer(String theSearchString)
			throws SearchException;

	public List<AutoCompleteBean> getInventoryproductSearch(String theSearchString)
			throws SearchException;

}