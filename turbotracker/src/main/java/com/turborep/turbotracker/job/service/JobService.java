package com.turborep.turbotracker.job.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.customer.dao.Cusodetailtemplate;
import com.turborep.turbotracker.customer.dao.Cusotemplate;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.employee.dao.Ecsplitcode;
import com.turborep.turbotracker.employee.dao.Ecsplitjob;
import com.turborep.turbotracker.employee.dao.EmMasterBean;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.JoCustPO;
import com.turborep.turbotracker.job.dao.JoInvoiceCost;
import com.turborep.turbotracker.job.dao.JoQuoteCategory;
import com.turborep.turbotracker.job.dao.JoQuoteDetail;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.JoQuoteProperties;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateDetail;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateProperties;
import com.turborep.turbotracker.job.dao.JoQuotetemplateHeader;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.dao.JobCustomerBean;
import com.turborep.turbotracker.job.dao.JobFinancialBean;
import com.turborep.turbotracker.job.dao.JobHistory;
import com.turborep.turbotracker.job.dao.JobPurchaseOrderBean;
import com.turborep.turbotracker.job.dao.JobQuoteDetailBean;
import com.turborep.turbotracker.job.dao.JobQuotesBidListBean;
import com.turborep.turbotracker.job.dao.JobReleaseBean;
import com.turborep.turbotracker.job.dao.JobSalesOrderBean;
import com.turborep.turbotracker.job.dao.JobShippingBean;
import com.turborep.turbotracker.job.dao.JobSubmittalBean;
import com.turborep.turbotracker.job.dao.Jobidder;
import com.turborep.turbotracker.job.dao.Jobidstatus;
import com.turborep.turbotracker.job.dao.Jochange;
import com.turborep.turbotracker.job.dao.Jojournal;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.Joquotehistory;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.job.dao.Joscheduledetail;
import com.turborep.turbotracker.job.dao.Joschedulemodel;
import com.turborep.turbotracker.job.dao.Josubmittaldetail;
import com.turborep.turbotracker.job.dao.Josubmittalheader;
import com.turborep.turbotracker.job.dao.joQLineItemTemplateProp;
import com.turborep.turbotracker.job.dao.joQLineItemsProp;
import com.turborep.turbotracker.job.dao.joQuoteDetailMstr;
import com.turborep.turbotracker.job.dao.joQuoteDetailPosition;
import com.turborep.turbotracker.job.dao.joQuoteTempDetailMstr;
import com.turborep.turbotracker.job.dao.jocategory;
import com.turborep.turbotracker.job.dao.testforquotes;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.user.dao.JoWizardAppletData;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebilldetail;
import com.turborep.turbotracker.vendor.dao.Vecommdetail;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;
import com.turborep.turbotracker.vendor.exception.VendorException;

public interface JobService {

	/**
	 * Retrieves all Jobs
	 * 
	 * @return list of Jobs
	 */
	public List<Jomaster> getAll() throws JobException;

	/**
	 * Add a new Job
	 * 
	 * @param employee the new Job
	 * @return true if successful
	 */
	public Integer add(Jomaster theJob) throws JobException;

	/**
	 * Edit an existing employee
	 * 
	 * @param employee the existing employee
	 * @return true if successful
	 */
	public Boolean addDesignTeam(Rxmaster theRxmaster, Rxaddress theRxaddress) throws JobException;
	
	/**
	 * Upda) throws JobException; JoMasterDetails
	 * @param joMaster
	 * @return {@link Integer}
	 */
	public Jomaster edit(Jomaster theJoMaster) throws JobException;
	
	/**
	 * Get Custom job list
	 * @param from
	 * @param theRows
	 * @return Joblist in {@link ArrayList}
	 */
	public ArrayList<?> getCustomJobs(int theFrom, int theRows) throws JobException;
	
	/**
	 * Method For Get Job Search List
	 * @return job list in {@link List}
	 */
	public List<?> getJobSearchList() throws JobException;
	
	/**
	 * get Job Count
	 * @return {@link BigInteger}
	 */
	public BigInteger getJobsCount() throws JobException;
	
	/**
	 * Method for Single Job Details
	 * @param jobNumber
	 * @return {@link Jomaster}
	 */
	public Jomaster getSingleJobDetailsfromoutside(Integer theJobNumber) throws JobException;
	
	/**
	 * Method for Quote Bid List
	 * @param jobNumber
	 * @return {@link ArrayList}
	 */
	public List<JobQuotesBidListBean> getQuotesBidlist(Integer jomasterID) throws JobException;

	/**
	 * Method for Submittal List
	 * @param jobNumber
	 * @return {@link ArrayList}
	 */
	public List<JobSubmittalBean> getSubmittalList(String theJobNumber) throws JobException;
	
	/**
	 * Get Quotes List
	 * @param jobNumber
	 * @return
	 */
	public List<JobQuotesBidListBean> getQuotesList(Integer joMasterID) throws JobException;
	
	/**
	 * To get the list of journal entries by jobNumber
	 * @param theJobNumber {@link String}
	 * @return {@link List} of {@link Jojournal}
	 */
	public List<Jojournal> getAllJobJournals(Integer joMasterID) throws JobException;
	
	/**
	 * 
	 */
	public String getJobJournalsStatus(String joMasterID) throws JobException;
	
	
	/**
	 * Method For New Journal Entry
	 * @param theJobJournal
	 * @return {@link Integer}
	 */
	public Integer addJobJournalEntry(Jojournal theJobJournal) throws JobException;
	
	/**
	 * Method For delete Journal Entry
	 * @param theJobJournal
	 * @return {@link Integer}
	 */
	public Integer deleteJobJournalEntry(Jojournal theJobJournal) throws JobException;
	
	/**
	 * Method For Get Release list
	 * @param jobNumber
	 * @return {@link List}
	 */
	public List<JobReleaseBean> getReleaseList(String jobNumber,Integer joMasterID) throws JobException;
	
	/**
	 * Method For Get Change Order List
	 * @param thejoMasterID
	 * @return {@link List}
	 */
	public List<Jochange> getChangeorderList(Integer thejoMasterID) throws JobException;

	/**
	 * Method For Get Shipping list
	 * @param jobNumber
	 * @param theJoDetailsID
	 * @return {@link List}
	 */
	public List<JobShippingBean> getShippingList(String jobNumber, Integer theJoDetailsID, String releaseType) throws JobException;

	/**
	 * Method For Get Job Search results
	 * @param jobNumber
	 * @param jobName
	 * @return {@link List}
	 */
	public List<?> getjobSearchResultsJob(String jobNumber, String jobName) throws JobException; 
	
	/**
	 * Method For Getting Financial List
	 * @param jobNumber
	 * @return {@link List}
	 */
	public List<JobFinancialBean> getFinancialList(Integer joMasterID) throws JobException;
	
	/**
	 * Method For CuInvoice List Details
	 * @param jobNumber
	 * @return {@link List}
	 */
	public List<Cuinvoice> getCuInvoiceDetails(String jobNumber) throws JobException;

	/**
	 * Method For Get job Change Amount
	 * @param jobNumber
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getJobChangeAmount (String jobNumber) throws JobException;

	/**
	 * Method For CuPO Number
	 * @param jobNumber
	 * @return {@link String}
	 */
	public String getCustPONo(String jobNumber) throws JobException;

	/**
	 * Method For Update Job Sattus
	 * @param dateType
	 * @param date
	 * @param jobNumber
	 * @param jobstatus
	 * @return {@link Integer}
	 */
	public int UpdateJobStatus(String dateType, Date date, Integer joMasterID, Integer jobstatus) throws JobException;

	/**
	 * Method For Get Job Status ID
	 * @param status
	 * @return {@link Integer}
	 */
	public int getjobStatusId(String status) throws JobException;

	/**
	 * Method For JoMaster By Job Number
	 * @param jobNumber
	 * @return {@link Integer}
	 */
	
	public Jomaster getJoMasterByJobNumber(String theJobNumber) throws JobException;
	
	/**
	 * Method For Add Last opened job
	 * @param aJobHistory
	 * @return {@link Integer}
	 */
	public int addLastOpened(JobHistory aJobHistory) throws JobException;
	
	/**
	 * get Job History
	 * @return {@link ArrayList}
	 */
	public ArrayList<?> getJobsHistory() throws JobException;

	/**
	 * Get Bidder List
	 * @param bidder
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getBidderList(String bidder) throws JobException;

	/**
	 * Get Contact List Base Bidder List
	 * @param theRxMasterId
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getContactListBaseBidderList(Integer theRxMasterId) throws JobException;

	/**
	 * Method for Add Quotes Bidder
	 * @param aJobidder
	 * @return
	 */
	public boolean addQuoteBidder(Jobidder aJobidder) throws JobException;

	/**
	 * Update Quote Bidder
	 * @param aJobidder
	 * @return {@link Boolean}
	 */
	public boolean updateQuoteBidder(Jobidder aJobidder) throws JobException;
	
	/**
	 * Delete Quote Bidder
	 * @param aJobidder
	 * @return {@link Boolean}
	 */
	public boolean deleteQuoteBidder(Jobidder aJobidder) throws JobException;

	/**
	 * Get Quote Type By Type Id
	 * @param theQuoteType
	 * @return {@link Integer}
	 */
	public Integer getQuoteTypeByTypeId(String theQuoteType) throws JobException;

	/**
	 * Get Bidder Contact List
	 */
	public void getBidderContactList() throws JobException;

	/**
	 * Method For get Submittal Details
	 * @param jobNumber
	 * @return {@link Josubmittalheader}
	 */
	public Josubmittalheader getSingleSubmittalDetails(String jobNumber,Integer joMasterID) throws JobException;

	/**
	 * Get Submittal name
	 * @param submittalById
	 * @return {@link String}
	 */
	public String getSubmittalName(Integer submittalById) throws JobException;

	/**
	 * Get Signed Name
	 * @param signedById
	 * @return {@link String}
	 */
	public String getSignedName(Integer signedById) throws JobException;

	/**
	 * Get Submittal Products List
	 * @param theSubmittalID
	 * @return {@link List}
	 */
	public List<?> getSubmittalProductList(String theSubmittalID) throws JobException;

	/**
	 * Get Submittal Sheduled List
	 * @param theSubmittalID
	 * @return {@link List}
	 */
	public List<?> getSubmittalSheduledList(Integer theSubmittalID,String theParameters) throws JobException;

	/**
	 * Get Scheduled List
	 * @param theScheduleText
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getScheduleList(String theScheduleText) throws JobException;

	/**
	 * Get Product List
	 * @param theProductText
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getProductList(String theProductText) throws JobException;

	/**
	 * Get Manufacturer List
	 * @param theManufactureText
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getManufactureList(String theManufactureText) throws JobException;

	/**
	 * Get product Name
	 * @param theProductName
	 * @return {@link Joschedtempheader}
	 */
	public Joschedtempheader getProductName(Integer theProductName) throws JobException;

	/**
	 * Get manufacturer Name
	 * @param aManufactureId
	 * @return {@link String}
	 */ 
	public String getManufactureName(Integer aManufactureId) throws JobException;

	/**
	 * Get Manufactuer ID
	 * @param theManufactureName
	 * @return {@link Integer}
	 */
	public Integer getManufactureId(String theManufactureName) throws JobException;

	/**
	 * Add Submittal Product
	 * @param joSubmittalDetails
	 * @return {@link Boolean}
	 */
	public boolean addSubmittal_Product(Josubmittaldetail joSubmittalDetails) throws JobException;

	/**
	 * Delete Submittal Product
	 * @param aJosubmittaldetail
	 * @return {@link Boolean}
	 */
	public boolean deleteSubmittal_Product(Josubmittaldetail aJosubmittaldetail) throws JobException;

	/**
	 * GEt model Number list
	 * @param theModelNo
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getModelNoList(String theModelNo) throws JobException;

	/**
	 * Get Scheduled Model
	 * @param theScheduledModelID
	 * @return {@link Joschedulemodel}
	 */
	public Joschedulemodel geScheduledModel(Integer theScheduledModelID) throws JobException;

	/**
	 * Get Scheduled Tag 
	 * @param theScheduledModelID
	 * @return {@link Joscheduledetail}
	 */
	public Joscheduledetail geScheduledTag(Integer theScheduledModelID) throws JobException;

	/**
	 * Add Submittal Schedule
	 * @param aJoscheduledetail
	 * @return {@link Boolean}
	 */
	public boolean addSubmittalSchedule(Joscheduledetail aJoscheduledetail) throws JobException;

	/**
	 * Delete Submittal Schedule
	 * @param aJosubmittal
	 * @return {@link Boolean}
	 */
	public boolean deleteSubmittalSchedule(Integer theJoScheduleDetailID) throws JobException;

	/**
	 * Add New Submittal
	 * @param aJosubmittalheader
	 * @return {@link Boolean}
	 */
	public Boolean addSubmittal(Josubmittalheader aJosubmittalheader) throws JobException;

	/**
	 * Get Submittal HeaderId By JoMASter
	 * @param joMasterID
	 * @return {@link Integer}
	 */
	public int getSubmittalHeaderIDByJoMaster(int joMasterID) throws JobException;

	/**
	 * Update Submittal
	 * @param aJosubmittalheader
	 * @return {@link Integer}
	 */
	public int updateSubmittal(Josubmittalheader aJosubmittalheader) throws JobException;

	/**
	 * Get Owner Name List
	 * @param theOwnerName
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getOwnerNameList(String theOwnerName) throws JobException;

	/**
	 * GEt Bond Agent List
	 * @param theBondAgent
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getBondAgentList(String theBondAgent) throws JobException;

	/**
	 * GEt Filter Owner List
	 * @param theFilterName
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getFilterOwnerList(String theFilterName) throws JobException;

	/**
	 * Get rxAddress List
	 * @param theFilterName
	 * @return {@link ArrayList}
	 */
	public ArrayList<Rxaddress> getRxAddress(String theFilterName) throws JobException;
	
	
	public Rxaddress getRxAddressByRxAddressID(Integer rxAddressID) throws JobException;

	/**
	 * This Method for Getting the Purchase Order Details
	 * @param theVePOID
	 * @return {@link Vepo}
	 */
	public JobPurchaseOrderBean getPurchaseDetails(Integer theVePOID,String theJobNumber) throws JobException;

	/**
	 * This Method for Getting the Sales Order Details
	 * @param theVePOID
	 * @return {@link Vepo}
	 */
	public JobSalesOrderBean getSalesOrderDetails(Integer joReleaseId) throws JobException;
	/**
	 * Get User Name
	 * @param theUserName
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getUserName(String theUserName) throws JobException;

	/**
	 * Get Fright Changes List
	 * @param theFrieghtChange
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getFrieghtChange(String theFrieghtChange) throws JobException;

	/**
	 * Get Shipvia List
	 * @param theShipVia
	 * @return {@link ArrayList}
	 */
	public ArrayList<AutoCompleteBean> getShipVia(String theShipVia) throws JobException;

	/**
	 *	Get Product Single 
	 * @param theProductID
	 * @return {@link Prmaster}
	 */
	public Prmaster getProductSingleNameList(Integer theProductID) throws JobException;

	public ArrayList<AutoCompleteBean> getProductWithNameList(String theProductNameWithCode) throws JobException;

	public Integer getProductIDBaseName(String theProductNameWithCode) throws JobException;

	public Rxmaster getSingleRxMasterDetails(Integer rxMasterId) throws JobException;

	public Cuso getSingleCuSalesOrderDetails(Cuso aCuso1) throws JobException;

	public Integer getScheduledIDBaseSubmittalID(Integer theSchelduleModelID) throws JobException;

	public ArrayList<Joschedtempcolumn> getScheduledColumnName(Integer aSchedModelID) throws JobException;

	public List<?> getSheduledColumnModel(String theSubmitID, Integer columnLength) throws JobException;

	public List<JobQuoteDetailBean> getQuotesDetailGridList(Integer joQuoteHeaderID) throws JobException;

	public ArrayList<AutoCompleteBean> getProductQuoteList(String theProductText) throws JobException;

	public ArrayList<JoQuoteDetail> getQuoteDetails(Integer theQuoteDeatilID, Integer theRxMasterID) throws JobException;

	public  boolean deleteProductQuote(JoQuoteDetail aJoQuoteDetail) throws JobException;

	public Integer addProductQuote(JoQuoteDetail aJoQuoteDetail) throws JobException;

	public boolean updateProductQuote(JoQuoteDetail aJoQuoteDetail) throws JobException;
	
	public boolean updateProductQuoteFooter(JoQuoteDetail aJoQuoteDetail) throws JobException;

	public ArrayList<AutoCompleteBean> getManufaturerList(String theManufaturerText) throws JobException;
	
	public Integer saveQuoteDetails(JoQuoteHeader joQuoteHeader, String token,Integer bidderId) throws JobException;

	public boolean updateLineInfo(JoQuoteDetail aJoQuoteDetail) throws JobException;

	public Integer getJobStaus(Integer joMasterID) throws JobException;

	public String getjobStatusName(Integer theStatusID) throws JobException;

	public boolean updateSubmittal_Product(Josubmittaldetail aJosubmittaldetail) throws JobException;

	public ArrayList<AutoCompleteBean> getCustomerContactList(String theFilterName) throws JobException;

	public String getCustomerName(String theCustomerID) throws JobException;

	public ArrayList<AutoCompleteBean> getCityAndState(String theCityAndState) throws JobException;

	public boolean deleteQuickQuote(JoQuoteHeader aJoQuoteHeader) throws JobException;

	public ArrayList<JoQuoteDetail> getJoQuoteDetailID(Integer theJoDetailID) throws JobException;

	public boolean saveCreditInfoDetails(Jomaster aJomaster) throws JobException;

	public ArrayList<AutoCompleteBean> getVendorsList(String theVendorText) throws JobException;

	public int getFactoryID(Integer theManufaturer,String thename) throws JobException;

	public boolean updateCuMaster(Cumaster aCumaster) throws JobException;

	public boolean updateCustomerBidList(Jomaster aJoMaster) throws JobException;

	public boolean updateCustomerRxMaster(Rxmaster aRxmaster) throws JobException;

	public boolean addNewContact(Rxcontact aRxcontact) throws JobException;

	public boolean UpdateStatus(Jomaster aJomaster) throws JobException;

	public Jomaster updatePlanAndSpec(Jomaster aJomaster) throws JobException;

	public Jomaster updateAddendums(Jomaster aJomaster) throws JobException;
	
	public Integer getJoMasterId(String thejobNumber) throws JobException;
	
	public Integer getrxMasterId(Integer thejoBidder) throws JobException;
	
	public String getCustomerPONoFromJomaster(Integer ajoMasterID) throws JobException;
	
	public List<?> getJobpage(Integer JomasterId,String term) throws JobException;
	
	public Integer getFirstLastJomastID(String term) throws JobException;
	
	public Integer copyQuoteDetails(JoQuoteHeader joQuoteHeader, String token) throws JobException;
	
	public List<JobQuoteDetailBean> getQuotesDetailList(Integer joQuoteHeaderID) throws JobException;
	
	public Jomaster getJoMasterIdJobstatus(String jobNumber) throws JobException;

	public List<JoQuoteHeader> getQuotesHeaderDetails(Integer theJoQuoteHeaderID) throws JobException;

	public boolean updateQuoteDetails(JoQuoteHeader aJoQuoteHeader) throws JobException;

	public Jomaster updateAmount(Jomaster aJomaster) throws JobException;

	public JoQuoteDetail getSingleQuoteDetails(JoQuoteDetail aJoQuoteDetail) throws JobException;

	public JoQuoteDetail updateItemPosition(JoQuoteDetail aJoQuoteDetail) throws JobException;

	public JoQuoteDetail updateInlineItemPosition(JoQuoteDetail aJoQuoteDetail,String operation, Integer Difference,Integer endQuoteDetailID) throws JobException;

	public boolean updateLineItemUpDownPosition(joQuoteDetailPosition aJoQuoteDetailPosition) throws JobException;

	public boolean updateLineItemUpPosition(joQuoteDetailPosition aJoQuoteDetailPosition) throws JobException;

	public ArrayList<JoQuoteDetail> getJoQuoteDetails(Integer thePreviousJoQuoteHeader) throws JobException;

	public int getRecordCount(String theJobsTotal) throws JobException;

	public ArrayList<JobCustomerBean> getAdvancedSearchResults(int aFrom, int aTo, String aEmployeeAssign, String aSortBy,String sort) throws JobException;

	public boolean updateLastQuoteAndRev(Jobidder aJobidder) throws JobException;

	public JoQuoteHeader getSingleQuoteHeaderDetails(Integer theJoQuoteHeaderID) throws Exception;
	
	public boolean deleteMainJob(Jomaster aJomaster) throws JobException;

	public JoQuoteHeader updateQuoteProperties( JoQuoteProperties theJoQuoteProperty, JoQuoteTemplateProperties thejoQuoteTempProperties,Boolean isQuoteTempProperty) throws JobException;

	public Jomaster updateORIJobNumber(Jomaster aJomaster) throws JobException;

	public boolean deleteRecentlyOpendJob(Jomaster aJomaster3) throws JobException;

	public boolean editRelease(JoRelease theJoRelease, JoReleaseDetail theJoReleaseDetail, Vepo theVepo) throws JobException;
	
	public boolean deleteRelease(JoRelease theJoRelease, JoReleaseDetail theJoReleaseDetail, Vepo theVepo, Cuso thecuSO) throws JobException;

	public List<Vepodetail> getPOReleaseLineItem(Integer theVepoID) throws JobException;

	public List<Vepodetail> getPOReleaseAck(Integer theVepoID) throws JobException;

	/**
	 * Method for Adding Line Items in Purchase Order
	 * @param aVepo 
	 * @param aVepo
	 * @return {@link Boolean}
	 * @throws JobException
	 */
	public boolean addPOReleaseLineItem(Vepodetail theVepodetail, Vepo theVepo) throws JobException;
	
	/**
	 * Method for Editing Line Items in Purchase Order
	 * @param theVepo 
	 * @param aVepo
	 * @return {@link Boolean}
	 * @throws JobException
	 */
	/*public boolean addSOReleaseLineItem(Cusodetail theCuSODetail, Cuso theCuso) throws JobException;*/
	
	public boolean editSOReleaseLineItem(Cusodetail theCuSODetail, Cuso theCuso,Cuso editcuCuso ) throws JobException;
	/**
	 *Method for adding the SOReleaseline item
	 */

	
	public boolean editPOReleaseLineItem(Vepodetail theVepodetail, Vepo theVepo) throws JobException;

	public Vepo updatePOGeneral(Vepo aVepo, Rxaddress theRxaddress, Rxaddress theRxaddressShipTo, JoRelease theJoRelease) throws JobException;
	
	
	public Cuso updateSOGeneral(Cuso theCuso, Rxaddress theShipToOtherAddress) throws JobException;

	/**
	 * Method for Deleting Line Items in Purchase Order
	 * @param aVepodetail
	 * @return {@link Boolean}
	 * @throws JobException
	 */
	public boolean  deletePOReleaseLineItem(Vepodetail theVepodetail, Vepo theVepo) throws JobException;

	public boolean  deleteSOReleaseLineItem(Cusodetail theCusodetail,Cuso theCuso) throws JobException;

	public Integer updateJobRelease(Jomaster theJomaster) throws JobException;
	
	public Integer updateBillNote(JoRelease ajoRelease) throws JobException;
	
	public Cumaster getSingleCuMasterDetails(Integer rxCustomerId) throws JobException;

	public String getNewJobNumber(String reportDate) throws JobException;
	
	public Integer updateJobSubmittal(Jomaster theJomaster) throws JobException;

	public Integer editJobJournalEntry(Jojournal aJojournal) throws JobException;

	public List<?> getSOReleaseLineItem(Integer theCuSOID) throws JobException;
	
	public List<?> getcuInvoiceReleaseLineItem(Integer theCuInvoiceID) throws JobException;

	/**
	 * Get VefactoryID
	 * @param theManuId
	 * @return {@link Integer}
	 */
	public Integer getVeFactoryId(Integer theManuId) throws JobException;
	
	/**
	 * This method is used to get the Vendor "Freight Charges" list.
	 * @return List of {@link Vefreightcharges}
	 * @throws JobException
	 */
	public List<Vefreightcharges> getVefreightcharges() throws JobException;

	/**
	 * This method is used to get the Vendor "Shipping Via" list data.
	 * @return List of {@link Veshipvia}
	 * @throws JobException
	 */
	public List<Veshipvia> getVeShipVia() throws JobException;
	
	/**
	 * Getting the Quote Pdf Submtted by Name using SubmittedId
	 * @return {@link String}
	 * @throws JobException
	 */
	public String getSubmittedBy(Integer aSubmittedById) throws JobException;

	/**
	 * Method for to get the Purchase order Bill to Address
	 * @param therxMasterId
	 * @return
	 * @throws JobException
	 */

	public Rxaddress getRxMasterBillAddress(Integer therxMasterId,String theOper) throws JobException;

	/**
	 * Method for to get the Default Warehouse Address Details
	 * @return
	 * @throws JobException
	 */
	public List<Prwarehouse> getWareHouse() throws JobException;
	
	public List<Object> getWareHousewithtaxtertory() throws JobException;
	
	/**
	 * This Method for Getting the Vendor Purchase order ID using Job Release Tab ID
	 * @param thejoReleaseId
	 * @return
	 * @throws JobException
	 */
	public Integer getVepoId(Integer thejoReleaseId) throws JobException;

	/**
	 * This Method for creating the new Purchase Order Number
	 * @param theJobNumber 
	 * @param thevePoId
	 * @return {@link String}
	 * @throws JobException
	 */
	public String getNewPONumber(String theJobNumber, Integer thevePoId) throws JobException;
	
	/**
	 * This Method Used for moving the purchase order line items towards UP.
	 * @param aJoQuoteDetailPosition
	 * @return {@link Boolean}
	 * @throws JobException
	 */
	
	public boolean updatePOLineItemUpDownPosition(joQuoteDetailPosition aJoQuoteDetailPosition) throws JobException;
	
	/**
	 * This Method Used for moving the purchase order line items towards Down.
	 * @param aJoQuoteDetailPosition
	 * @return {@link Boolean}
	 * @throws JobException
	 */

	public boolean updatePOLineItemUpPosition(joQuoteDetailPosition aJoQuoteDetailPosition) throws JobException;

	/**
	 * This Method Used for get VeBillID.
	 * @param theReleaseDetailID
	 * @param theVePOId
	 * @return {@link Integer}
	 * @throws JobException
	 */
	public Integer getVeBillID(Integer theReleaseDetailID, Integer theVePOId) throws JobException;

	/**
	 * This Method Used for get VeBill Details.
	 * @param aVeBillID
	 * @return {@link Vebill}
	 * @throws JobException
	 */
	public Vebill getVeBillDetails(Integer aVeBillID) throws JobException;

	/**
	 * This Method Used for Update VeBill Details.
	 * @param aVebill
	 * @return {@link Vebill}
	 */
	/*public Vebill updateVendorInvoiceDetails(Vebill aVebill) throws JobException;*/
	
	/**
	 * This Method used for Getting Quote Amount
	 * @param thejoQuoteHeaderId
	 * @return {@link JoQuoteHeader}
	 * @throws JobException
	 */
	
	public JoQuoteHeader getjoQuoteAmount(Integer thejoQuoteHeaderId) throws JobException;

	/**
	 * This Method used for Getting CuInvoiceID
	 * @param theReleaseDetailID
	 * @return {@link Integer}
	 * @throws JobException
	 */
	public Integer getCustomerInoiveID(Integer theReleaseDetailID) throws JobException;

	/**
	 * This Method used for Getting CuInvoice Details
	 * @param aInvoiceID
	 * @return {@link Cuinvoice}
	 * @throws JobException
	 */
	public Cuinvoice getCustomerInvoiceDetails(Integer aInvoiceID) throws JobException;

	/**
	 * This Method used for Updating CuInvoice Details
	 * @param aCuinvoice
	 * @return {@link Cuinvoice}
	 * @throws JobException
	 */
	public Cuinvoice updateCusotmerInvoiceDetails(Cuinvoice aCuinvoice) throws JobException;

	/**
	 * This Method used for Delete Ship Via Details
	 * @param aVeshipvia
	 * @param aJoReleaseDetail
	 * @param aVebill
	 * @param aCuinvoice 
	 * @return {@link Integer}
	 */
	public Integer deleteShipViaDetails(Veshipvia aVeshipvia, JoReleaseDetail aJoReleaseDetail, Vebill aVebill, Cuinvoice aCuinvoice) throws JobException;

	/**
	 * This Method used for Add New VeBill Details
	 * @param theVebill
	 * @param aJoReleaseDetail 
	 * @return {@link Vebill} {@link JoReleaseDetail}
	 * @throws JobException
	 */
	/*public Vebill addVendorInvoiceDetails(Vebill theVebill, JoReleaseDetail theJoReleaseDetail) throws JobException;*/

	/**
	 * This Method used for Get CoAccount Details
	 * @return {@link Coaccount}
	 * @throws JobException
	 */
	public List<Coaccount> getCoAccountDetails() throws JobException;

	/**
	 * This Method used for Add New CuInvoice Details
	 * @param aCuinvoice
	 * @param aJoReleaseDetail
	 * @return 
	 * @throws JobException
	 */
	public Cuinvoice addCusotmerInvoiceDetails(Cuinvoice aCuinvoice, JoReleaseDetail aJoReleaseDetail,String from) throws JobException;

	/**
	 * Inserts Line items of a sales order into Customer invoice Details.
	 * @param aVepo
	 * @return
	 */
	public void insertCustomerInvoiceLines(Integer cuSOID, Integer cuInvoiceID) throws JobException;

	/**
	 * Update Bill To And Ship To Address Value
	 * @param Cuso
	 * @return
	 */
	public Vepo updateBillToAndShipToSetting(Vepo aVepo) throws JobException;

	/**
	 * Save Customer PO Number function
	 * @param theCustomerPONumber
	 * @param theReleaseDetail 
	 * @param theJoMasterID 
	 * @return {@link Boolean}
	 * @throws JobException
	 */
	public Boolean saveCustomerPONumber(String theCustomerPONumber, Integer theJoMasterID, Integer theReleaseDetail) throws JobException;

	/**
	 * Save Customer Invoice function 
	 * @param aCuinvoice
	 * @return {@link Boolean}
	 * @throws JobException
	 */
	public Boolean addCustomerInvoice(Cuinvoice aCuinvoice) throws JobException;

	/**
	 * Update EmailStamp Function
	 * @param theVepo
	 * @throws JobException
	 */
	public Vepo updateEmailStampValue(Vepo theVepo) throws JobException;
	
	public Boolean savePOLineitemsSubtotal(Integer vePOID, BigDecimal thePOLISubtotal) throws JobException;

	void updateVepoDetailPosistion(Vepodetail theVepodetail) throws JobException;

	public ArrayList<?> getLineItemProductList() throws JobException;

	public Rxaddress getRxAddress(Integer theRxAddressID) throws JobException;

	public boolean updatePOLineInfo(Vepodetail aVepodetail) throws JobException;

	public Jochange saveChangeOrderDetails(Jochange aJochange) throws JobException;

	public JoCustPO saveCustomerPONumner(JoCustPO theJoCustPO, Jomaster theJomaster) throws JobException;

	public JoCustPO getSingleCusotomerPODetails(Integer theJoMasterId) throws JobException;

	public Integer getJoCustPOID(Integer theJoMater) throws JobException;

	public List<JoCustPO> getJoCustPODetail(Integer theJoMasterID) throws JobException;

	public Vepodetail getVePODetails(Integer theVepoDetailId) throws JobException;

	public Vepo addRelease(JoRelease theJoRelease, JoReleaseDetail theJoReleaseDetail,Vepo theVepo, String theJobNumber, Cuso theCuso, Integer releaseType)throws JobException;
	
	public Integer getCuSOID(String theJobNumber) throws JobException;
	
	public Cusodetail getCusoDetailObj(int CusoID) throws JobException;

	public Cuso getCusoObj(int CusoID) throws JobException;
	
	public Cusodetail getSingleCusoDetailObj(int CusoDetailID) throws JobException;
	
	public Boolean saveSoLines(Cuso theCuso) throws JobException;
	
	public Boolean checkInitialSaveCuInvoice(Integer cuSOID, Integer joReleaseDetailid) throws JobException;

	public Cuinvoicedetail getSingleCuInvoiceDetailObj(int cuInvoiceDetailId)throws JobException;
	
	public Cuinvoice getSingleCuInvoiceObj(int cuInvoiceID) throws JobException;

	public boolean addcuinvoiceReleaseLineItem(Cuinvoicedetail theCuSODetail,Cuinvoice theCuso) throws JobException;

	public boolean deleteCUInvReleaseLineItem(Cuinvoicedetail theCusodetail,Cuinvoice theCuso) throws JobException;

	public Cuinvoicedetail getCuInvoiceDetailObj(int cuInvoiceID) throws JobException;

	public String getCuTerms(Integer cuTermsId) throws JobException;

	public boolean editCuInvoiceReleaseLineItem(Cuinvoicedetail theCuInvDetail,Cuinvoice theCuInv, Cuinvoice editcuInv) throws JobException;

	public JoQuoteProperties getuserquoteProperty(Integer userID)throws JobException;

	public void editSubmittalSchedule(Joscheduledetail aJoscheduledetail)throws JobException;
	
	public Boolean checkReportPresent(String query) throws JobException;

	public Vepodetail getVepoDetailOBJ(int vePoid) throws JobException;
	/**
	 * Inserts Line items of a sales order into Customer invoice Details from PO.
	 * @param aVepo
	 * @return
	 */
	public void insertCustomerInvoiceLinesFromPO(Integer vePOID, Integer cuInvoiceID,Integer releasetype,BigDecimal aReleaseAllocatedAmt) throws JobException;

	/**
	 * This method is used to get the Job details based on joReleaseId.
	 * @return {@link Jomaster} object
	 * @throws JobException
	 */
	public Jomaster getJoMasterByJoReleaseID(int joReleaseId) throws JobException;

	public JoQuoteTemplateProperties getuserquoteTemplateProperty(Integer userID) throws JobException;

	public Vepo getVepo(Integer vePOID);
	
	/*Newly added*/
	public Vepo addPOGeneral(Vepo aVepo, Rxaddress theRxaddress, Rxaddress theRxaddressShipTo, JoRelease theJoRelease) throws JobException;
	public String addPOLineDetails(List<Vepodetail> alVepodetails,BigDecimal taxAmt) throws JobException;
	public List<Vepodetail> getPOLineDetails(Integer  theVepoID) throws JobException;
	public JoQuoteTemplateProperties getuserTempquoteProperty(Integer userID) throws JobException;
	public Integer populatecusoID() throws JobException;
	public Cuso addSOGeneral(Cuso theCuso, Rxaddress theShipToOtherAddress) throws JobException;
	public Cuso getSingleCUSODetails(Integer theCUSOID) throws JobException;
	public Rxaddress getRxAddressFromAddressID(Integer theRxMasterID) throws JobException;
	public Rxaddress getShipToAddress(Integer theRxAddressID) throws JobException;
	public Rxaddress getShipToAddressforSO(Integer theRxAddressID) throws JobException;
	public List<Rxaddress> getShipToAddressforToggle(Integer theRxAddressID) throws JobException;
	public Cuso getCUSOobjFromSONumber(String theSONumber) throws JobException;
	public Integer getShipToAddressID(Integer theRxMasterID)throws JobException;
	public List<?> getcustomerInvoicelineitem(Integer theCusoId) throws JobException;
	public Integer insertRxAddress(Rxaddress theRxaddress, String oper) throws JobException;
	boolean deletePOLineItem(Vepodetail theVepodetail, Vepo theVepo)throws JobException;
	public ArrayList<AutoCompleteBean> getPayeeNameList(String theProductName)	throws JobException;
	public Integer getWareHouseTransactionNo() throws JobException;
	public PrwarehouseTransfer getWareHouseTransfer(Integer prTransferId) throws JobException;
    public Integer getRecordsCount() throws JobException;
    public List<AutoCompleteBean> getEmployeewithNameList(String theActive) throws JobException;
    public boolean addSplitCommission(Ecsplitjob ecsplitjob) throws JobException;
    public List<?> getjobCommissionListGrid(Integer JoMasterId) throws JobException;
    public boolean deleteSplitCommission(Integer ecSplitJobId) throws JobException;
    public boolean getsplitcommission_chkbox_status(Integer theJoMasterId) throws JobException;
	public List<?> getBidStatus();
	public List<?> getBidStatusList() throws JobException;
	public Jobidstatus updateJoBidStatusService(Jobidstatus theRxcontact, Boolean isAdd)
			throws JobException;

	public void deleteJoBidStatusService(Integer theJoBidstatusId);
	public Ecsplitcode updateEmpCommissionsplitypeService(Ecsplitcode ecsplitcode, Boolean isAdd)throws JobException;
	public List<?> getempSplitTypeListGrid() throws JobException;
	public void deleteEmpCommissionsplitypeService(Integer ecSplitCodeId);
	public List<AutoCompleteBean> getSplitTypewithNameList(String theActive) throws JobException;

	public List<JoQuoteHeader> getQuotesHistory(String jobNumber)throws JobException;
	
	public Ecsplitcode getpercentagebasedonsplittype(Integer id) throws JobException;
	
	public EmMasterBean getEmployeeJobProfitCommission(Integer id) throws JobException;
	
	public List<?> getjobCommissionListGrid(Integer JoMasterId, Integer JoReleaseId)
			throws JobException;

	public Integer addSOTemplateLineItem(Cusodetailtemplate theCusodetailtemplate, Cusotemplate theCusotemplate)
			throws JobException;
	public ArrayList<Cusodetailtemplate> getSOTemplateList(Integer thecuSOID) throws InventoryException;
	public Integer saveSOTemplate(Cusotemplate theCusotemplate) throws InventoryException;
	public Integer deleteSOTemplate(Integer cuSoid) throws InventoryException;
	public ArrayList<Cusotemplate> getSOTemplate() throws InventoryException;
	public Cusotemplate loadSOTemplate(Integer cuSoid) throws InventoryException;


	
	public Jobidder savBidData(Jobidder aJobidder, boolean a) throws JobException;
	public boolean updateSplitCommission(Ecsplitjob ecsplitjob) throws JobException;
	
	//For SO Template
	public Integer addSOTemplateLineItem1(Cusodetailtemplate theCuSODetail, Cusotemplate theCuso) throws JobException;
	public boolean editSOTemplateLineItem(Cusodetailtemplate theCuSODetail,Cusotemplate theCuso, Cusotemplate editCuso) throws JobException;
	public boolean deleteSOTemplateLineItem(Cusodetailtemplate theCusodetail,Cusotemplate theCuso) throws JobException;
	public Cusodetailtemplate getSingleCusoTemplateDetailObj(int CusoDetailID)throws JobException;
	public Cusodetailtemplate getCusoTemplateDetailObj(int CusoID) throws JobException;
	public List<CuMasterType> getCuMasterTypeForBidList();
	public Map<String,BigDecimal> getSOTemplatePriceDetails(int CusoID) throws JobException;
	public Integer addQuickJob(Jomaster aJoMaster) throws JobException;
	Integer addJoRelease(JoRelease theJob) throws JobException;
	Integer addJoReleaseDetail(JoReleaseDetail theJob) throws JobException;
	public int getQuotePositionMaxValue(Integer theJoQuoteHeader) throws JobException;
	//Release tab vendor invoice
	public List<Vepodetail> getPOReleaseLineItemForVendorInvoice(Integer theVepoID)	throws JobException;
	public ArrayList<?> getJobBidList(int theFrom, int theRows, int therxMasterID,String sord, String sidx) throws JobException;
	public Date getQuoteDate(Integer bidderId) throws JobException;
	public Emmaster getSalesManDetails(Integer emMasterID) throws JobException;
	
	public Integer getLatestRevisionNumberCopy(Integer joMasterId,Integer joQuoteHeaderID) throws JobException;
	public Integer addCuReceiptFromInvoice(Cureceipt theCuReceipt);
	public Integer addCuLinkageDetailFromInvoice(CuLinkageDetail aCuLinkageDetail);
	public void copyPOLineItemService(Integer vePODetailId);
	public Boolean saveSalesOrderNote(Integer cuSoDetailID, String note);
	/*public Boolean copySOTemplateLineItems(Integer cuSOID, Integer quantity,Integer templateID);*/
	public Boolean calculateSOCost(Integer cuSOID);
	public jocategory getJobCategories() throws JobException;
	public List<?> getContactFromRxMaster(Integer therxMasterID) throws JobException;

	public String getJobNumber(String sJobNumber) throws JobException;
	
	public List<Prwarehouse> getWareHouseForDefaults() throws JobException;
	
	public Jomaster getSingleJobDetails(Integer joMasterID)	throws JobException;

	public List<Vebilldetail> getVeBillLineItemsFromRelease(Integer vepoid)throws JobException;
	
	public ArrayList<AutoCompleteBean> getAccountDetails(String theProductText) throws JobException;
	public List<?> getBidStatusList(int inActive) throws JobException;
	
	public Cuso updateSOEmailTimeStamp(Cuso theCuso) throws JobException;
	
	public ArrayList<AutoCompleteBean> getTemplateProductQuoteList(String theProductText) throws JobException;
	
	public ArrayList<JoQuoteTemplateDetail> getQuoteTemplateDetails(Integer theQuoteDeatilID,Integer theRxMasterID) throws JobException;

	Integer updateCuInvoiceJoReleaseDetail(JoReleaseDetail theJob)
			throws JobException;

	Integer updateReleaseAllocated(Integer theJoReleaseID) throws JobException;
	
	public List<JobQuoteDetailBean> getQuotesTemplateDetailList(Integer theJoQuoteHeaderID) throws JobException;
	
	public List<JoQuotetemplateHeader> getQuotesTemplateHeaderDetails(Integer theJoQuoteHeaderID) throws JobException;

	public List<Vepodetail> getPOReleaseLineItemforvendorinvoice(Integer theVepoID)
			throws JobException;

	

	public void updateCuInvoiceSubTotal(Integer cuInvoiceId, BigDecimal amount) throws JobException;

	public void checAllocatedAmount(Integer joReleaseID) throws JobException;

	String getLastestInvoiceNumber(String invoiceNumber) throws JobException;

	public JobReleaseBean getBilledUnbilled(Integer joReleaseID) throws JobException;

	public String getvendorInvoiceNumber(Integer thejoReleaseDetailID)	throws JobException;

	public Emmaster getEmployeeDetailLoginID(int UserLoginID) throws JobException;

	public Integer createJoQuoteLineItemsProp(joQLineItemsProp ajoQLineItemsProp);

	public joQLineItemsProp getjoQLineItemsProp(Integer joQuoteDetailID);
	
	public Integer createJoQuoteLineItemsTempProp(joQLineItemTemplateProp ajoQLineItemsProp);

	public joQLineItemTemplateProp getjoQLineItemsTempProp(Integer joquotedetailid);

	public int getProductNoteCount(Integer joquoteheaderid, Integer type);
	
	public Integer getPrToWarehouseID(Integer cuSOId) throws JobException;
	
	public JoQuoteTemplateDetail updateQuoteTempItemPosition(JoQuoteTemplateDetail theJoQuoteTemplateDetail)throws JobException;
	
	public int getQuoteTempPositionMaxValue(Integer theJoQuoteHeader) throws JobException;
	
	public Rxcontact getContactDetails(Integer theContactID) throws JobException;
	
	public List<Vefreightcharges> getFreights() throws JobException;
	
	public CustomerPaymentBean getCuRecieptDetials(Integer theRecieptID) throws CustomerException;

	public List<testforquotes> getTestQuotefromtable() throws JobException;
	
	public Integer addjoquoteheader(JoQuoteHeader theJoQuoteHeader) throws JobException;
	
	public List<joQuoteDetailMstr> getjoQuoteDetailMstr(Integer joquoteheaderid) throws JobException;

	public boolean UpdatenewQuotes(joQuoteDetailMstr thejoQuoteDetailMstr,String Oper) throws JobException;

	public joQuoteDetailMstr updatenewInlineItemPosition(joQuoteDetailMstr theJoQuoteDetail,
			String operation, Integer difference, Integer endQuoteDetailID)
			throws JobException;

	public boolean updatejoquoteheader(JoQuoteHeader theJoQuoteHeader)throws JobException;

	public boolean CopyFromQuoteToQuoteTemplate(Integer ajoQuoteHeaderID,String Template_Name);

	public boolean copyQuoteTemplateLIToQuoteLI(Integer ajoQuoteHeaderID,Integer joquoteTemplateHeaderID);

//	public boolean deleteJoNewQuoteDetail(Integer thejoQuoteDetailMstrID,Integer joquoteheaderid,Integer position)throws JobException;

	
	public List<?> getjoQuotesCategoryList() throws JobException;

	public JoQuoteCategory updateQuotesCategory(JoQuoteCategory theRxcontact, Boolean isAdd) throws JobException;

	public void deleteQuotesCategory(Integer theBidstatusId);

	
	//Template Methods
	public Integer addJoQuotetemplateHeader(JoQuotetemplateHeader theJoQuotetemplateHeader) throws JobException;

	public boolean UpdatenewQuotestemplate(joQuoteTempDetailMstr thejoQuoteTempDetailMstr) throws JobException;

	public boolean addjoQuoteTempDetailMstr(joQuoteTempDetailMstr thejoQuoteTempDetailMstr) throws JobException;

	public List<joQuoteTempDetailMstr> getjoQuoteTempDetailMstr(Integer quoteTemplateHeaderID) throws JobException;

	public boolean deleteJoNewQuoteDetail_template(Integer thejoQuoteDetailMstrID,Integer ajoQuoteHeaderID, Integer aposition) throws JobException;

	public joQuoteTempDetailMstr updatenewInlineItemPosition_template(joQuoteTempDetailMstr theJoQuoteDetail, String operation,Integer difference, Integer endQuoteDetailID) throws JobException;

	public JoQuotetemplateHeader getJoQuotetemplateHeader(Integer joQuoteTemplateHeaderID) throws JobException;

	public Integer getJobNumberFromReleaseDetail(Integer joReleaseDetailID) throws JobException;
	
	public Jomaster getJobNumberFromRelease(Integer joReleaseID) throws JobException;

	public Rxmaster getCustomerDetails(Integer rxMasterID) throws JobException;

	public Integer saveJoquoteHistory(Joquotehistory ajoquotehistory)throws JobException;

	public Jomaster getJoMasterDetails(Integer joreleaseID) throws JobException;

	public Rxcontact ContactsBasedonID(Integer ContactID) throws JobException;
	
	public Jomaster updateJobSources(Jomaster theJomaster) throws JobException;
	
	public JoRelease getJoRelease(Integer aJoReleaseID) throws JobException;

	public void updatejorealseseqnumber() throws JobException;

	public boolean UpDateCreditInfoDetails(Jomaster theJomaster) throws JobException;

	public boolean updateVepoDetailquantity(Vepodetail theVepodetail) throws JobException;

	public Integer createvepodetailfromvendorinvoice(Vepodetail vepod) throws JobException;

	public Cuinvoice getCustomerinvoicejobDetail(Cuinvoice aCuinvoice) throws JobException;
	
	public BigDecimal saveCommissionAmount(Integer joReleaseId, BigDecimal commissionAmount) throws JobException;
	
	public Integer saveCommissionInvoice(Vecommdetail aVecommdetail,Integer joMasterID, Integer joReleaseID) throws JobException;
	
	public List<JobShippingBean> getCommissionInvoiceList(String theJobNumber,Integer theJoDetailsID, String releaseType) throws JobException;
	
	public Vecommdetail getVeCommissionDetails(Integer veCommissionDetailID) throws JobException;
	
	public void updatecuMasterOverriteCreditHold(Integer customerid)throws JobException;
	
	public JoRelease getCommissionAmount(Integer joReleaseId) throws JobException;
	
	public String getCoTaxterritory(Integer coTaxterritoryID) throws JobException;
	
	public Integer getCoTaxterritoryIdfmrxmasterid(Integer rxMasterID)throws JobException;
	
	public Boolean saveCustomerInvoiceNote(Integer CuinvoicedetailID, String note);
	
	public Boolean saveCusoDetailWhseCost(Cusodetail theCusodetail) throws JobException;

	boolean getjoquotedetailrtftohtml(Integer betweenfrom,Integer betweento)
			throws JobException;

	public String getJobNumbersequence(String sJobNumber) throws JobException;

	public Integer getnumberofInvoiceNumber(String jobNumberwithPrefix)throws JobException;

	public Prmaster getPrMasterBasedOnId(Integer theprmasterid) throws JobException;
	
	public List<JoInvoiceCost> getInvoiceCostList(Integer joReleaseDetailID) throws JobException;
	
	public boolean deleteInvoiceCost(JoInvoiceCost aJoInvoiceCost) throws JobException;
	
	public boolean saveInvoiceCost(JoInvoiceCost aJoInvoiceCost) throws JobException;
	
	public boolean updateCustomerInvoiceCost(Integer joReleaseDetailID,Integer cuInvoiceID,Integer veBillID) throws JobException;
	
	public String getJobNumberSequenceDate(String sJobNumber) throws JobException;
	
    public Boolean saveXMLVepoDetail(Vepodetail theVepodetail);
	
	public Integer getStarProductIDforXMLUpload();
	
	public boolean updateEmailTimestamp(String type, Integer processID) throws JobException;
	
	public BigDecimal getReleaseAllocatedAmt(Integer theJoReleaseID)throws JobException;

	public Integer addvePOfromInsideJob(Vepo aVepo, JoRelease aJoRelease,String theJobNumber,Integer thecuSOID);

	public List<Vepodetail> jobReleaseLineItemForrecieveInventory(Integer theVepoID)
			throws JobException;

	public boolean getjoquotedetailupdateposition() throws JobException;

	public Integer addRXMasterAndRxAddress(Rxmaster theRxmaster, Rxaddress theRxaddress)throws JobException;

	public Boolean saveSalesOrderTempNote(Integer cuSoDetailTempID, String note);

//	public boolean WhiledeleteUpdatePosition(Integer ajoQuoteHeaderID,Integer aposition);

	public boolean WhiledeleteUpdatePosition_template(Integer ajoQuotetempHeaderID,
			Integer aposition);

	public boolean addSalesORderReleaseLineItem(Cusodetail theCuSODetail,String oper)
			throws JobException;

	public boolean updateCuso(Cuso theCuso) throws JobException;

	public ArrayList<Cusodetail> copySOTemplateLineItems(Integer cuSOID, Integer templateID);

	public boolean addPurchaseORderLineItem(Vepodetail theVepodetail, String oper)
			throws JobException;

	public boolean updatevePOfromlinesTab(Vepo theVepo) throws JobException;

	public boolean updatevePODetailfromAckTab(Vepodetail theVepodetail)
			throws JobException;

	public void updatejowizardAppletData(JoWizardAppletData ajowizAppletData);

	public Jomaster getjoMasterAddressDetails(Integer joReleaseDetailId);

	public Integer getCustomerTaxTerritoryIDWithShipTo(Integer rxMasterID);

	public CoTaxTerritory getgetoverride_taxterritoryTaxID(Integer customerID);

	public boolean getjoquoteTemplatedetailrtftohtml() throws JobException; 
	
	public Integer getCommPaidDetails(Integer joReleaseDetailID);
	
	public Integer getCommPaidReleaseDetails(Integer joReleaseID,Integer joMasterID);
	
	public Integer updateCommissionReceived(JoRelease aJoRelease) throws JobException ;

	public ArrayList<joQuoteDetailMstr> getjoQuoteDetailMstrFromTemplate(Integer quoteTemplateHeaderID) throws JobException;
	
	public String getMaxInvoiceNumber() throws JobException;

	public boolean RollBackPrMasterPrWareHouseInventory(Integer cuSoid,Integer cuSodetailId) throws JobException;

	public boolean insertPrMasterPrWareHouseInventory(Integer cuSoid,Integer cuSodetailId) throws JobException;
	
	public Integer saveCustomerInvoiceLog(Cuinvoice oldCuinvoice,Cuinvoice newCuinvoice, String transType,Integer transStatus,Integer periodID,Integer yearID) throws JobException;

	public Map<String, String> getjoMasterByJoreleaseDetailID(Integer joreleasedetailid);

	public String getalternatetextinhtml(String theJoQuoteHeaderID);

	public JobQuotesBidListBean getjoQuoteDetailForPDF(Integer joQuoteHeaderID)
			throws JobException;
	
	public Integer updateInvoiceLineItems(Integer cuInvoiceID,Integer Operation);


	public List<?> getjobSearchResultsJob(String theJobNumber, String theJobName,
			Integer joMasterID) throws JobException;

	public Jomaster getSingleJobDetails(String theJobNumber, Integer aJoMasterId)
			throws JobException;

	public JoQuoteHeader getjoQuoteHeader(Integer aJoQuoteHeaderID) throws JobException;

	public boolean invoicelogRollbackentry(Integer cuInvoiceid,Cuinvoice newCuinvoice) throws JobException;
	
	public Integer addcusotodsorcommordfromInsideJob(Integer thevePoId, Cuso aCuSO) throws JobException;

	public Boolean deletecuso(Integer thecusoid) throws JobException;

	public Boolean deleteVepo(Integer thevePoId) throws JobException;

	public boolean UpdatenewQuotes_template(
			joQuoteTempDetailMstr thejoQuotetempDetailMstr, String Oper)
			throws JobException;

	public boolean gettemplatethereornot(String templateDescription);

	public CoTaxTerritory getCoTaxTerritoryDetails(Integer coTaxTerritoryId);

	public Cuso updateTaxableandNonTaxableforCuSO(Cuso acuSO);
	
	public Cuinvoice updateTaxableandNonTaxableforCuInvoice(Cuinvoice aCuinvoice);
	public Integer copySoTemplate(Integer cuSoId);
	public Integer saveSODetailTemplate(Cusodetailtemplate theCusoDetailtemplate)
			throws InventoryException;
}