package com.turborep.turbotracker.job.service;

// jobServiceImple
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLog;
import com.turborep.turbotracker.Inventory.dao.TpInventoryLogMaster;
import com.turborep.turbotracker.Inventory.service.InventoryConstant;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.customer.dao.CuLinkageDetail;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cureceipt;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.customer.dao.Cusodetailtemplate;
import com.turborep.turbotracker.customer.dao.Cusotemplate;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.dao.TpCuinvoiceLogMaster;
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
import com.turborep.turbotracker.job.dao.Josubmittal;
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
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.search.dao.search_index;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.JoWizardAppletData;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebilldetail;
import com.turborep.turbotracker.vendor.dao.Vecommdetail;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Vereceivedetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

/**
 * Handles services for Jobs
 * 
 */

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService {

	protected static Logger itsLogger = Logger.getLogger(JobServiceImpl.class);
	protected Phrase itsHeader;
	protected PdfPTable itsFooter;

	@Resource(name = "vendorService")
	private VendorServiceInterface itsVendorService;

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;

	@Resource(name = "sysService")
	private SysService itsSysService;

	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;
	
	@Resource(name = "salesServices")
	private SalesService salesServices;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Resource(name = "userLoginService")
	private UserService userService;	

	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	
	private String itsSelect = " ";

	@Override
	public List<Jomaster> getAll() throws JobException {
		Session aSession = null;
		List<Jomaster> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM  Jomaster");
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}

	@Override
	public Integer add(Jomaster theJob) throws JobException {
		Session aSession = null;
		Integer joMasterID=null;
		try {
			aSession = itsSessionFactory.openSession();
			joMasterID=(Integer)aSession.save(theJob);

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return joMasterID;
	}

	@Override
	public Integer addQuickJob(Jomaster theJob) throws JobException {
		Session aSession = null;

		try {
			aSession = itsSessionFactory.openSession();
			Integer joMasterID=(Integer) aSession.save(theJob);
			
			if (theJob.getJoMasterId() > 0) {
				JobHistory aJobHistory = new JobHistory();
				aJobHistory.setJobNumber(theJob.getJobNumber());
				aJobHistory.setJobName(theJob.getDescription());
				aJobHistory.setBidDate(theJob.getBidDate());
				aJobHistory.setJobOpenedDate(new java.util.Date());
				aJobHistory.setJoMasterID(joMasterID);
				addLastOpened(aJobHistory);
			}
			return theJob.getJoMasterId();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Boolean addDesignTeam(Rxmaster theRxmaster, Rxaddress theRxaddress)
			throws JobException {
		Session aRxMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aRxMasterSession.beginTransaction();
			aTransaction.begin();
			int aRxMasterId = (Integer) aRxMasterSession.save(theRxmaster);
			aTransaction.commit();
			theRxaddress.setRxMasterId(aRxMasterId);
			aTransaction = aRxMasterSession.beginTransaction();
			aTransaction.begin();
			aRxMasterSession.save(theRxaddress);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aRxMasterSession.flush();
			aRxMasterSession.close();
		}
		return true;
	}

	@Override
	public Jomaster edit(Jomaster theJoMaster) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Jomaster aJoMaster = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJoMaster.getJoMasterId());
			//Integer oldcustomerID=aJoMaster.getRxCustomerId();
			//Integer newcustomerID=theJoMaster.getRxCustomerId();
			aJoMaster.setDescription(theJoMaster.getDescription()==null?aJoMaster.getDescription():theJoMaster.getDescription());
			aJoMaster.setRxCustomerId(theJoMaster.getRxCustomerId()==null?aJoMaster.getRxCustomerId():theJoMaster.getRxCustomerId());
			aJoMaster.setLocationAddress1(theJoMaster.getLocationAddress1()==null?aJoMaster.getLocationAddress1():theJoMaster.getLocationAddress1());
			aJoMaster.setLocationAddress2(theJoMaster.getLocationAddress2()==null?aJoMaster.getLocationAddress2():theJoMaster.getLocationAddress2());
			aJoMaster.setLocationCity(theJoMaster.getLocationCity()==null?aJoMaster.getLocationCity():theJoMaster.getLocationCity());
			aJoMaster.setLocationState(theJoMaster.getLocationState()==null?aJoMaster.getLocationState():theJoMaster.getLocationState());
			aJoMaster.setLocationZip(theJoMaster.getLocationZip()==null?aJoMaster.getLocationZip():theJoMaster.getLocationZip());
			aJoMaster.setChangedById(theJoMaster.getChangedById()==null?aJoMaster.getChangedById():theJoMaster.getChangedById());
			//aJoMaster.setCreatedById(theJoMaster.getCreatedById());
			aJoMaster.setJoBidStatusId(theJoMaster.getJoBidStatusId());
			if (theJoMaster.getBidDate() != null) {
				aJoMaster.setBidDate(theJoMaster.getBidDate());
			}
			if (theJoMaster.getBookedDate() != null) {
				aJoMaster.setBookedDate(theJoMaster.getBookedDate());
			}
			if (theJoMaster.getClosedDate() != null) {
				aJoMaster.setClosedDate(theJoMaster.getClosedDate());
			}
			aJoMaster.setCuAssignmentId0(theJoMaster.getCuAssignmentId0()==null?aJoMaster.getCuAssignmentId0():theJoMaster.getCuAssignmentId0());
			aJoMaster.setCuAssignmentId1(theJoMaster.getCuAssignmentId1()==null?aJoMaster.getCuAssignmentId1():theJoMaster.getCuAssignmentId1());
			aJoMaster.setCuAssignmentId2(theJoMaster.getCuAssignmentId2()==null?aJoMaster.getCuAssignmentId2():theJoMaster.getCuAssignmentId2());
			aJoMaster.setCuAssignmentId3(theJoMaster.getCuAssignmentId3()==null?aJoMaster.getCuAssignmentId3():theJoMaster.getCuAssignmentId3());
			aJoMaster.setCuAssignmentId4(theJoMaster.getCuAssignmentId4()==null?aJoMaster.getCuAssignmentId4():theJoMaster.getCuAssignmentId4());
			aJoMaster.setCuAssignmentId5(theJoMaster.getCuAssignmentId5()==null?aJoMaster.getCuAssignmentId5():theJoMaster.getCuAssignmentId5());
			aJoMaster.setCuAssignmentId6(theJoMaster.getCuAssignmentId6()==null?aJoMaster.getCuAssignmentId6():theJoMaster.getCuAssignmentId6());
			aJoMaster.setRxCategory1(theJoMaster.getRxCategory1()==null?aJoMaster.getRxCategory1():theJoMaster.getRxCategory1());
			aJoMaster.setRxCategory2(theJoMaster.getRxCategory2()==null?aJoMaster.getRxCategory2():theJoMaster.getRxCategory2());
			aJoMaster.setRxCategory3(theJoMaster.getRxCategory3()==null?aJoMaster.getRxCategory3():theJoMaster.getRxCategory3());
			aJoMaster.setCoDivisionId(theJoMaster.getCoDivisionId()==null?aJoMaster.getCoDivisionId():theJoMaster.getCoDivisionId());
			aJoMaster.setCoTaxTerritoryId(theJoMaster.getCoTaxTerritoryId()==null?aJoMaster.getCoTaxTerritoryId():theJoMaster.getCoTaxTerritoryId());
			aJoMaster.setCustomerPonumber(theJoMaster.getCustomerPonumber()==null?aJoMaster.getCustomerPonumber():theJoMaster.getCustomerPonumber());
			aJoMaster.setJobStatus(theJoMaster.getJobStatus()==null?aJoMaster.getJobStatus():theJoMaster.getJobStatus());
			aJoMaster.setJoMasterId(theJoMaster.getJoMasterId()==null?aJoMaster.getJoMasterId():theJoMaster.getJoMasterId());
			if (theJoMaster.getOriginalBidDate() != null) {
				aJoMaster.setOriginalBidDate(theJoMaster.getOriginalBidDate());
			}
			aJoMaster.setLocationName(theJoMaster.getLocationName());
			aSession.update(aJoMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoMaster;
	}

	@Override
	public ArrayList<?> getCustomJobs(int theFrom, int theRows)
			throws JobException {
		String aJobSelectQry = "SELECT joMaster.joMasterId, joMaster.jobNumber,"
				+ " joMaster.Description, joMaster.locationName, joMaster.locationCity,"
				+ " tsUserLogin.Initials, joStatus.JobStatus, rxMaster.Name,"
				+ " joMaster.CustomerPONumber, coDivision.Code, joMaster.SourceReport1,"
				+ " joMaster.bookedDate, joMaster.contractAmount, joMaster.estimatedCost"
				+ " FROM rxMaster RIGHT OUTER JOIN joStatus RIGHT OUTER JOIN coDivision RIGHT OUTER JOIN joMaster"
				+ " ON coDivision.coDivisionID = joMaster.coDivisionID ON joStatus.joStatusID = joMaster.JobStatus"
				+ " ON rxMaster.rxMasterID = joMaster.rxCustomerID LEFT OUTER JOIN tsUserLogin"
				+ " ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID ORDER BY joMaster.jobNumber ASC LIMIT "
				+ theFrom + ", " + theRows;
		Session aSession = null;
		ArrayList<JobCustomerBean> aQueryList = null;
		try {
			aQueryList = new ArrayList<JobCustomerBean>();
			JobCustomerBean aJobCustomerBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			/**
			 * In general you are not supposed to get the column names.
			 * Actually, that's exactly what an O2R mapper is about - hiding the
			 * R details and showing only O specifics.
			 **/
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobCustomerBean = new JobCustomerBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobCustomerBean.setJoMasterId((Integer) aObj[0]);
				/** joMasterId */
				aJobCustomerBean.setJobNumber((String) aObj[1]);
				/** jobNumber */
				aJobCustomerBean.setDescription((String) aObj[2]);
				/** Description */
				aJobCustomerBean.setLocationName((String) aObj[3]);
				/** locationName */
				aJobCustomerBean.setLocationCity((String) aObj[4]);
				/** locationCity */
				aJobCustomerBean.setInitials((String) aObj[5]);
				/** Initials */
				aJobCustomerBean.setJobStatus((String) aObj[6]);
				/** JobStatus */
				aJobCustomerBean.setCustomerName((String) aObj[7]);
				/** Name */
				aJobCustomerBean.setCustomerPONumber((String) aObj[8]);
				/** CustomerPONumber */
				aJobCustomerBean.setCode((String) aObj[9]);
				/** Code */
				aJobCustomerBean.setSourceReport1((String) aObj[10]);
				/** SourceReport1 */
				if (aObj[11] != null) {
					aJobCustomerBean.setBookedDate((String) DateFormatUtils
							.format((Date) aObj[11], "MM/dd/yyyy hh:mm a"));
					/** bookedDate */
				}
				aQueryList.add(aJobCustomerBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	/*@Override
	public Jomaster getSingleJobDetails(String theJobNumber)
			throws JobException {
		Session aSession = null;
		Jomaster aJoMaster = new Jomaster();
		Integer aJoMasterId = 0;
		try {
			
			aJoMasterId = getJoMasterIdByJobNumber(theJobNumber);

			itsLogger.info("getSingleJobDetails()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			if (aJoMasterId != 0) {
				aJoMaster = (Jomaster) aSession
						.get(Jomaster.class, aJoMasterId);
				if (aJoMaster != null) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					
					if (aJoMaster.getRequestedNocdate() != null) {
						itsLogger.info("Requested NOC Date in DB::"+aJoMaster.getRequestedNocdate());
						aJoMaster.setRequestedNocdate_Str(formatter.format(aJoMaster.getRequestedNocdate()));
					}
					if (aJoMaster.getReceivedNocdate() != null) {
						itsLogger.info("REceived NOC Date in DB::"+aJoMaster.getReceivedNocdate());
						aJoMaster.setReceivedNocdate_Str(formatter.format(aJoMaster.getReceivedNocdate()));
					}
					if (aJoMaster.getSentNtcdate() != null) {
						itsLogger.info("Sent Ntc Date in DB::"+aJoMaster.getSentNtcdate());
						aJoMaster.setSentNtcdate_Str(formatter.format(aJoMaster.getSentNtcdate()));
					}
					if (aJoMaster.getLienWaverSignedDate() != null) {
						aJoMaster.setLienWaverSignedDate_Str(formatter.format(aJoMaster.getLienWaverSignedDate()));
					}
					if (aJoMaster.getLienWaverThroughDate() != null) {
						aJoMaster.setLienWaverThroughDate_Str(formatter.format(aJoMaster.getLienWaverThroughDate()));
					}
					
					if (aJoMaster.getCreditStatusDate() != null) {
						aJoMaster.setCreditstadate_Str(formatter.format(aJoMaster.getCreditStatusDate()));
					}
					if (aJoMaster.getLocationAddress1() == null) {
						aJoMaster.setLocationAddress1("");
					}
					if (aJoMaster.getLocationAddress2() == null) {
						aJoMaster.setLocationAddress2("");
					}
					if (aJoMaster.getLocationCity() == null) {
						aJoMaster.setLocationCity("");
					}
					if (aJoMaster.getLocationState() == null) {
						aJoMaster.setLocationState("");
					}
					if (aJoMaster.getLocationZip() == null) {
						aJoMaster.setLocationZip("");
					}
					if (aJoMaster.getReleaseNotes() == null) {
						aJoMaster.setReleaseNotes("");
					}

				}
			} else {
				aJoMaster = new Jomaster();
				aJoMaster.setLocationAddress1("");
				aJoMaster.setLocationAddress2("");
				aJoMaster.setLocationCity("");
				aJoMaster.setLocationState("");
				aJoMaster.setLocationZip("");
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			if(aSession!=null){
			aSession.flush();
			aSession.close();
			}
			aJoMasterId = null;
			itsLogger.info("getSingleJobDetails()=[Connection Closed]");
		}
		
		return aJoMaster;
	}*/
	
	@Override
	public Jomaster getSingleJobDetailsfromoutside(Integer cusoId)throws JobException {
		Session aSession = null;
		Jomaster aJoMaster = null;
		Cuso aCuso = null;
		try {
			aSession = itsSessionFactory.openSession();
			 aCuso  = (Cuso) aSession.get(Cuso.class, cusoId);
			
			if (aCuso.getJoReleaseId() != null) {
				Query aQuery = aSession.createQuery("from JoRelease jr, Jomaster jm where jm.joMasterId = jr.joMasterId and jr.joReleaseId="+aCuso.getJoReleaseId());
				
					
					
					List<Object> list = aQuery.list();
					java.util.Iterator pairs = list.iterator();
					while (pairs.hasNext()) {
						Object[] pair = (Object[]) pairs.next();
						JoRelease jrobj = (JoRelease) pair[0];
						 aJoMaster = (Jomaster) pair[1];
				
						
						if (aJoMaster != null) {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							
							if (aJoMaster.getRequestedNocdate() != null) {
								itsLogger.info("Requested NOC Date in DB::"+aJoMaster.getRequestedNocdate());
								aJoMaster.setRequestedNocdate_Str(formatter.format(aJoMaster.getRequestedNocdate()));
							}
							if (aJoMaster.getReceivedNocdate() != null) {
								itsLogger.info("REceived NOC Date in DB::"+aJoMaster.getReceivedNocdate());
								aJoMaster.setReceivedNocdate_Str(formatter.format(aJoMaster.getReceivedNocdate()));
							}
							if (aJoMaster.getSentNtcdate() != null) {
								itsLogger.info("Sent Ntc Date in DB::"+aJoMaster.getSentNtcdate());
								aJoMaster.setSentNtcdate_Str(formatter.format(aJoMaster.getSentNtcdate()));
							}
							if (aJoMaster.getLienWaverSignedDate() != null) {
								aJoMaster.setLienWaverSignedDate_Str(formatter.format(aJoMaster.getLienWaverSignedDate()));
							}
							if (aJoMaster.getLienWaverThroughDate() != null) {
								aJoMaster.setLienWaverThroughDate_Str(formatter.format(aJoMaster.getLienWaverThroughDate()));
							}
							
							if (aJoMaster.getCreditStatusDate() != null) {
								aJoMaster.setCreditstadate_Str(formatter.format(aJoMaster.getCreditStatusDate()));
							}
							if (aJoMaster.getLocationAddress1() == null) {
								aJoMaster.setLocationAddress1("");
							}
							if (aJoMaster.getLocationAddress2() == null) {
								aJoMaster.setLocationAddress2("");
							}
							if (aJoMaster.getLocationCity() == null) {
								aJoMaster.setLocationCity("");
							}
							if (aJoMaster.getLocationState() == null) {
								aJoMaster.setLocationState("");
							}
							if (aJoMaster.getLocationZip() == null) {
								aJoMaster.setLocationZip("");
							}
							if (aJoMaster.getReleaseNotes() == null) {
								aJoMaster.setReleaseNotes("");
							}
						}
					

				}
			} else {
				aJoMaster = new Jomaster();
				aJoMaster.setLocationAddress1("");
				aJoMaster.setLocationAddress2("");
				aJoMaster.setLocationCity("");
				aJoMaster.setLocationState("");
				aJoMaster.setLocationZip("");
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			if(aSession!=null){
			aSession.flush();
			aSession.close();
			}
			itsLogger.info("getSingleJobDetails()=[Connection Closed]");
		}
		
		return aJoMaster;
	}
	

	@Override
	public Jomaster getSingleJobDetails(Integer joMasterID) throws JobException {
		Session aSession = null;
		Jomaster aJoMaster = null;
		try {
			
			aSession = itsSessionFactory.openSession();
			if (joMasterID != 0) {
				aJoMaster = (Jomaster) aSession.get(Jomaster.class, joMasterID);
				if (aJoMaster != null) {
					if (aJoMaster.getLocationAddress1() == null) {
						aJoMaster.setLocationAddress1("");
					}
					if (aJoMaster.getLocationAddress2() == null) {
						aJoMaster.setLocationAddress2("");
					}
					if (aJoMaster.getLocationCity() == null) {
						aJoMaster.setLocationCity("");
					}
					if (aJoMaster.getLocationState() == null) {
						aJoMaster.setLocationState("");
					}
					if (aJoMaster.getLocationZip() == null) {
						aJoMaster.setLocationZip("");
					}
					if (aJoMaster.getReleaseNotes() == null) {
						aJoMaster.setReleaseNotes("");
					}

				}
			} else {
				aJoMaster = new Jomaster();
				aJoMaster.setLocationAddress1("");
				aJoMaster.setLocationAddress2("");
				aJoMaster.setLocationCity("");
				aJoMaster.setLocationState("");
				aJoMaster.setLocationZip("");
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
		return aJoMaster;
	}

	@Override
	public Rxmaster getSingleRxMasterDetails(Integer theRxMasterID)
			throws JobException {
		Session aSession = null;
		Rxmaster aRxmaster = null;
		try {
			aSession = itsSessionFactory.openSession();
		if(theRxMasterID!=null){
			aRxmaster = (Rxmaster) aSession.get(Rxmaster.class, theRxMasterID);
		}else{
			aRxmaster = new Rxmaster();
		}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxmaster;
	}

	@Override
	public Cuso getSingleCuSalesOrderDetails(Cuso theCuso) throws JobException {
		Session aSession = null;
		Cuso aCuso = null;
		try {
			int cuSoID = this.getCUSOID(theCuso);
			aSession = itsSessionFactory.openSession();
			if (cuSoID != 0) {
				aCuso = (Cuso) aSession.get(Cuso.class, cuSoID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuso;
	}

	private int getCUSOID(Cuso theCuso) throws JobException {
		Integer aCUSOID = 0;
		String aSONumber = theCuso.getSonumber();
		String aSONO = aSONumber.replace(" ", "");
		String rxCustomerID = "";
		if (theCuso.getRxCustomerId() != null) {
			rxCustomerID = theCuso.getRxCustomerId().toString();
		}

		String aSelectQry = "SELECT * FROM cuSO where rxCustomerID = '"
				+ rxCustomerID + "' and SONumber like '%" + JobUtil.removeSpecialcharacterswithslash(aSONO) + "%'";
		Session aSession = null;
		try {
			Cuso aCuso = new Cuso();
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				if ((Integer) aObj[0] != 0 && (Integer) aObj[0] != null) {
					aCuso.setCuSoid((Integer) aObj[0]);
				}
				aCUSOID = aCuso.getCuSoid();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSONumber=null;aSONO=null;rxCustomerID=null;aSelectQry=null;
		}
		return aCUSOID;
	}

	@Override
	public BigInteger getJobsCount() throws JobException {
		String aJobCountStr = "SELECT COUNT(joMasterID) AS count FROM joMaster";
		Session aSession = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			return (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();aJobCountStr=null;
		}
	}

	@Override
	public List<JobQuotesBidListBean> getQuotesBidlist(Integer jomasterID)
			throws JobException {
		String aJobBidListQry = "SELECT DISTINCT "
				+ " rx.Name, j.LowBid, "
				+ " CONCAT(r.FirstName, ' ', r.LastName) AS FullNAme, "
				+ " c.Code, j.QuoteDate, j.QuoteRev, ts.Initials, "
				+ " j.joMasterID, j.rxMasterID, j.rxContactID, j.joBidderId, j.cuMasterTypeID, r.Email,j.quoteemailstatus,jom.JobNumber  FROM joMaster jom "
				+ " LEFT JOIN joBidder j ON(jom.joMasterID=j.joMasterID) "
				+ " LEFT JOIN rxContact r ON j.rxContactID = r.rxContactID "
				+ " LEFT JOIN rxMaster rx ON j.rxMasterID = rx.rxMasterID "
				+ " LEFT JOIN tsUserLogin ts ON j.UserLoginID = ts.UserLoginID "
				+ " Right JOIN cuMasterType c ON j.cuMasterTypeID = c.cuMasterTypeID "
				+ " WHERE j.joMasterID = "+jomasterID;
		itsLogger.info("ur Query: " + aJobBidListQry);
		Session aSession = null;
		ArrayList<JobQuotesBidListBean> aQueryList = new ArrayList<JobQuotesBidListBean>();
		try {
			JobQuotesBidListBean aJobQuotesBidListBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobBidListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuotesBidListBean = new JobQuotesBidListBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuotesBidListBean.setBidder((String) aObj[0]);
				/** joMasterId */
				if ((Byte) aObj[1] == 1) {
					aJobQuotesBidListBean.setLow(true);
				} else {
					aJobQuotesBidListBean.setLow(false);
				}
				aJobQuotesBidListBean.setContact((String) aObj[2]);
				aJobQuotesBidListBean.setQuoteType((String) aObj[3]);
				if (aObj[4] != null) {
					aJobQuotesBidListBean.setLastQuote((String) DateFormatUtils
							.format((Date) aObj[4], "MM/dd/yy hh:mm a"));
				}
				aJobQuotesBidListBean.setRev((String) aObj[5]);
				if (aObj[6] != null) {
					aJobQuotesBidListBean.setRep((String) aObj[6]);
				}
				aJobQuotesBidListBean.setJoMasterId((Integer) aObj[7]);
				aJobQuotesBidListBean.setRxMasterId((Integer) aObj[8]);
				aJobQuotesBidListBean.setRxContactId((Integer) aObj[9]);
				aJobQuotesBidListBean.setBidderId((Integer) aObj[10]);
				aJobQuotesBidListBean.setQuoteTypeID((Integer) aObj[11]);
				aJobQuotesBidListBean.setEmail((String) aObj[12]);
				aJobQuotesBidListBean.setJobNumber((String) aObj[14]);
				aJobQuotesBidListBean.setQuoteemailstatus((Integer) aObj[13]);
				aQueryList.add(aJobQuotesBidListBean);
			}
		} catch (Exception e) {
			itsLogger.error(
					"Exception while getting the quote bid list: "
							+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobBidListQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<JobSubmittalBean> getSubmittalList(String theJobNumber)
			throws JobException {
		String aSubmittalQry = "SELECT DISTINCT "
				+ " jd.product, "
				+ " jd.quantity, "
				+ " jd.released, "
				+ " vm.Manufacturer, "
				+ " jd.estimatedcost, "
				+ " jd.cost, "
				+ " jd.status, "
				+ " jd.joSubmittalDetailID "
				+ " FROM joSubmittalDetail jd "
				+ " INNER JOIN joSubmittalHeader jh ON jd.joSubmittalHeaderID = jh.joSubmittalHeaderID "
				+ " INNER JOIN veMaster vm ON vm.veMasterID = jd.rxManufacturerID "
				+ " INNER JOIN joMaster j ON jh.joMasterID = j.joMasterID WHERE j.jobNumber = '"
				+ theJobNumber + "'";
		Session aSession = null;
		ArrayList<JobSubmittalBean> aQryList = new ArrayList<JobSubmittalBean>();
		try {
			JobSubmittalBean aJobSubmittalBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobSubmittalBean = new JobSubmittalBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobSubmittalBean.setProduct((String) aObj[0]);
				aJobSubmittalBean.setquantity((String) aObj[1]);
				aJobSubmittalBean.setReleased((String) aObj[2]);
				aJobSubmittalBean.setManufacture((String) aObj[3]);
				aJobSubmittalBean.setEstimatecost((BigDecimal) aObj[4]);
				aJobSubmittalBean.setCost((BigDecimal) aObj[5]);
				aJobSubmittalBean.setStatus((Integer) aObj[6]);
				aJobSubmittalBean.setManufactureID((Integer) aObj[7]);
				aQryList.add(aJobSubmittalBean);
			}
		} catch (Exception e) {
			itsLogger.error(
					"Exception while getting the submittal list: "
							+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry=null;
		}
		return aQryList;
	}

	@Override
	public String getSubmittedBy(Integer aSubmittedById) throws JobException {
		itsLogger.debug("Getting Submitted by Full Name in Quote Pdf");
		String aJobSelectQry = "SELECT FullName AS CreatedBYFullName FROM tsUserLogin WHERE UserLoginID = "
				+ aSubmittedById + "  AND LoginName != 'admin'";
		Session aSession = null;
		String aCreatedBy = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			aCreatedBy = (String) aQuery.list().get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Full Name of Submitted By in Quote: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;aCreatedBy=null;
		}
		return aCreatedBy;
	}

	@Override
	public List<JobQuotesBidListBean> getQuotesList(Integer joMasterID)
			throws JobException {
		String aJobSelectQry = "SELECT cuMasterType.Code, "
				+ " j.QuoteRev, "
				+ " j.CostAmount, "
				+ " j.QuoteAmount, "
				+ " j.CreatedByName, "
				+ " j.InternalNote, "
				+ " j.PrimaryQuote, "
				+ " j.joQuoteHeaderID, "
				+ " j.cuMasterTypeID, "
				+ " j.CreatedByID, (SELECT FullName AS CreatedBYFullName FROM tsUserLogin WHERE UserLoginID = j.CreatedByID  AND LoginName != 'admin'), j.Remarks,j.DiscountedPrice, "
				+ " j.DisplayQuantity , j.PrintQuantity,"
				+ " j.DisplayParagraph, j.PrintParagraph,"
				+ " j.DisplayManufacturer, j.PrintManufacturer,"
				+ " j.DisplaySpec, j.PrintSpec,"
				+ " j.DisplayCost, j.PrintCost,"
				+ " j.DisplayPrice, j.PrintPrice,"
				+ " j.DisplayMult, j.PrintMult,"
				+ " j.NotesFullWidth,"
				+ " j.LineNumbers,"
				+ " j.PrintTotal,"
				+ " j.HidePrice,"
				+ " j.LSDStatus,"
				+ " j.LSDValue,"
				+ " j.DonotQty,"
				+ " j.ShowTotOnly "
				+ " FROM joQuoteHeader j "
				+ " JOIN cuMasterType ON cuMasterType.cuMasterTypeID = j.cuMasterTypeID "
				+ " WHERE j.joMasterID = "+joMasterID+"  and j.neworoldquote='1' order by j.joQuoteHeaderID";
		itsLogger.info("Query : " + aJobSelectQry);
		Session aSession = null;
		ArrayList<JobQuotesBidListBean> aQueryList = new ArrayList<JobQuotesBidListBean>();
		try {
			JobQuotesBidListBean aJobQuotesBidListBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuotesBidListBean = new JobQuotesBidListBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuotesBidListBean.setJoQuoteHeaderID((Integer) aObj[7]);
				aJobQuotesBidListBean.setQuoteType((String) aObj[0]);
				aJobQuotesBidListBean.setRev((String) aObj[1]);
				aJobQuotesBidListBean.setCostAmount((BigDecimal) aObj[2]);
				aJobQuotesBidListBean.setQuoteAmount((BigDecimal) aObj[3]);
				aJobQuotesBidListBean.setCreatedByName((String) aObj[4]);
				aJobQuotesBidListBean.setInternalNote((String) aObj[5]);
				aJobQuotesBidListBean.setQuoteTypeID((Integer) aObj[8]);
				aJobQuotesBidListBean.setCreatedByID((Integer) aObj[9]);
				aJobQuotesBidListBean.setCreatedBYFullName((String) aObj[10]);
				aJobQuotesBidListBean.setQuoteRemarks((String) aObj[11]);
				aJobQuotesBidListBean.setDiscountAmount((BigDecimal) aObj[12]);
				aJobQuotesBidListBean.setDisplayQuantity((Byte) aObj[13]);
				aJobQuotesBidListBean.setPrintQuantity((Byte) aObj[14]);
				aJobQuotesBidListBean.setDisplayParagraph((Byte) aObj[15]);
				aJobQuotesBidListBean.setPrintParagraph((Byte) aObj[16]);
				aJobQuotesBidListBean.setDisplayManufacturer((Byte) aObj[17]);
				aJobQuotesBidListBean.setPrintManufacturer((Byte) aObj[18]);
				aJobQuotesBidListBean.setDisplaySpec((Byte) aObj[19]);
				aJobQuotesBidListBean.setPrintSpec((Byte) aObj[20]);
				aJobQuotesBidListBean.setDisplayCost((Byte) aObj[21]);
				aJobQuotesBidListBean.setPrintCost((Byte) aObj[22]);
				aJobQuotesBidListBean.setDisplayPrice((Byte) aObj[23]);
				aJobQuotesBidListBean.setPrintPrice((Byte) aObj[24]);
				aJobQuotesBidListBean.setDisplayMult((Byte) aObj[25]);
				aJobQuotesBidListBean.setPrintMult((Byte) aObj[26]);
				aJobQuotesBidListBean.setNotesFullWidth((Byte) aObj[27]);
				aJobQuotesBidListBean.setLineNumbers((Byte) aObj[28]);
				aJobQuotesBidListBean.setPrintTotal((Byte) aObj[29]);
				aJobQuotesBidListBean.setHidePrice((Byte) aObj[30]);
				
				aJobQuotesBidListBean.setIncludeLSD((Boolean) aObj[31]);
				aJobQuotesBidListBean.setLsdValue((BigDecimal)aObj[32]);
				System.out.println("===="+(BigDecimal)aObj[32]);
				aJobQuotesBidListBean.setDonotQtyitem2and3((Boolean) aObj[33]);
				aJobQuotesBidListBean.setShowTotPriceonly((Boolean) aObj[34]);
				
				if ((Byte) aObj[6] == 1) {
					aJobQuotesBidListBean.setPrimaryQuote(true);
				} else {
					aJobQuotesBidListBean.setPrimaryQuote(false);
				}
				aQueryList.add(aJobQuotesBidListBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<JoQuoteHeader> getQuotesHistory(String jobNumber)
			throws JobException {
		String aJobSelectQry = "SELECT joQuoteHistory.joQuoteHeaderID, joQuoteHistory.joMasterID, joQuoteHistory.QuoteDate, joQuoteHistory.QuoteStatus,"+
				"tsUserLogin.FullName AS CreatedBYFullName,"+
				"cuMasterType.code AS quotesType,joQuoteHistory.QuoteRev, "+
				"rxMaster.Name AS biddername, "+
				"CONCAT(rxContact.FirstName,' ',rxContact.LastName) AS contact, "+
				"joQuoteHeader.QuoteAmount ,joMaster.Description AS company "+
				"FROM joQuoteHistory LEFT JOIN joQuoteHeader ON(joQuoteHistory.joQuoteHeaderID = joQuoteHeader.joQuoteHeaderID ) " +
				//"LEFT JOIN tsUserLogin ON(UserLoginID = joQuoteHeader.CreatedByID  AND LoginName != 'admin') "+
				"LEFT JOIN tsUserLogin ON(UserLoginID = joQuoteHistory.employeeId  ) "+
				
				"LEFT JOIN cuMasterType ON(cuMasterType.cuMasterTypeID =joQuoteHeader.cuMasterTypeID) "+
				"LEFT JOIN joMaster ON(joMaster.joMasterID=joQuoteHistory.joMasterID ) "+
				"LEFT JOIN rxContact ON(rxContact.rxContactID = joQuoteHistory.rxContactID) "+
				"LEFT JOIN rxMaster ON(rxMaster.rxMasterId=joQuoteHistory.rxMasterID) "+
				"WHERE joMaster.JobNumber='"+jobNumber+"' AND joQuoteHistory.QuoteDate IS NOT NULL";

		itsLogger.info("Query : " + aJobSelectQry);
		Session aSession = null;
		List<JoQuoteHeader> aQueryList = new ArrayList<JoQuoteHeader>();
		try {
			JoQuoteHeader aJoQuoteHeader = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoQuoteHeader = new JoQuoteHeader();
				Object[] aObj = (Object[]) aIterator.next();

				aJoQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
				aJoQuoteHeader.setJoMasterID((Integer) aObj[1]);
				Timestamp stamp = (Timestamp) aObj[2];
				Date date = stamp;
				String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				String outputdate =JobUtil.convertinto12hourformat(dateString);
				
				Short state=(Short) aObj[3];
				aJoQuoteHeader.setQuoteHistoryDate(outputdate);
				if(state==1){
					aJoQuoteHeader.setQuoteStatus("Emailed");
				}else if(state==2){
					aJoQuoteHeader.setQuoteStatus("Printed");
				}else{
					aJoQuoteHeader.setQuoteStatus("Failed");
				}
				
				String byName = null;
				byName = (String) aObj[4];
				if (byName != null) {
					if (byName.length() > 20) {
						byName = byName.substring(0, 20);
						byName = byName + "..";
					}
				}
				aJoQuoteHeader.setCreatedByName(byName);
				aJoQuoteHeader.setQuoteType((String) aObj[5]);
				aJoQuoteHeader.setQuoteRev((String) aObj[6]);
				/*String companyName = null;
				companyName = (String) aObj[7];
				if (companyName != null) {
					if (companyName.length() > 20) {
						companyName = companyName.substring(0, 20);
						companyName = companyName + "..";
					}
				}*/
				aJoQuoteHeader.setCompany((String) aObj[7]);
				String contactName = null;
				contactName = (String) aObj[8];
				if (contactName != null) {
					if (contactName.length() > 20) {
						contactName = contactName.substring(0, 20);
						contactName = contactName + "..";
					}
				}
				aJoQuoteHeader.setContact(contactName);
				aJoQuoteHeader.setQuoteAmount((BigDecimal) aObj[9]);
				aQueryList.add(aJoQuoteHeader);
				dateString=null;outputdate=null;byName=null;contactName=null;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getJobSearchList() throws JobException {
		String aJobSearchSelectQry = "SELECT joMaster.joMasterID, joMaster.JobNumber as JobNumber, joMaster.Description as Description, "
				+ "tsUserLogin.FullName as FullName, CONCAT(JobNumber, '_', Description, '_', FullName) AS searchText "
				+ "FROM joMaster JOIN tsUserLogin ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID LIMIT 0, 20000";
		Session aSession = null;
		ArrayList<JobCustomerBean> aQueryList = new ArrayList<JobCustomerBean>();
		try {
			JobCustomerBean aJobCustomerBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSearchSelectQry);
			/**
			 * In general you are not supposed to get the column names.
			 * Actually, that's exactly what an O2R mapper is about - hiding the
			 * R details and showing only O specifics.
			 * */
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobCustomerBean = new JobCustomerBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobCustomerBean.setJoMasterId((Integer) aObj[0]);
				/** joMasterId */
				aJobCustomerBean.setJobNumber((String) aObj[1]);
				/** jobNumber */
				aJobCustomerBean.setDescription((String) aObj[2]);
				/** Description */
				aJobCustomerBean.setLocationName((String) aObj[3]);
				/** locationName */
				aJobCustomerBean.setCode((String) aObj[4]);
				/** searchText **/
				aQueryList.add(aJobCustomerBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSearchSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<Jojournal> getAllJobJournals(Integer joMasterID)
			throws JobException {
		Session aSession = null;
		ArrayList<Jojournal> aQueryList = new ArrayList<Jojournal>();
		try {
			aSession = itsSessionFactory.openSession();
			String aJoJournalStr = "SELECT " + " joJournalId, "
					+ " joMasterId, " + " JournalDate, " + " journalById, "
					+ " journalByName, " + " JournalNote, " + " JournalStatus "
					+ " FROM  joJournal " + " WHERE joMasterID = "+joMasterID;
			Query aQuery = aSession.createSQLQuery(aJoJournalStr);
			Iterator<?> aIterator = aQuery.list().iterator();
			Jojournal ajoJournal = null;
			while (aIterator.hasNext()) {
				ajoJournal = new Jojournal();
				Object[] aObj = (Object[]) aIterator.next();
				ajoJournal.setJoJournalId((Integer) aObj[0]);
				ajoJournal.setJoMasterId((Integer) aObj[1]);
				ajoJournal.setJournalDateStr((String) DateFormatUtils.format(
						(Date) aObj[2], "MM/dd/yyyy"));
				ajoJournal.setJournalById((Integer) aObj[3]);
				ajoJournal.setJournalByName((String) aObj[4]);
				ajoJournal.setJournalNote((String) aObj[5]);
				ajoJournal.setJournalStatus((Short) aObj[6]);
				aQueryList.add(ajoJournal);
			}
			aJoJournalStr=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}
	
	public String getJobJournalsStatus(String joMasterID) throws JobException {
		Session aSession = null;
		String str = "";
		try {
			aSession = itsSessionFactory.openSession();
			String aJoJournalStr = "SELECT JournalStatus FROM  joJournal WHERE joMasterID = "
					+ joMasterID;
			if(joMasterID!=null && !joMasterID.equals("")){
			Query aQuery = aSession.createSQLQuery(aJoJournalStr);
			List list = aQuery.list();
			if (list.size() > 0)
				for (int i = 0; i < list.size(); i++) {
					// Object[] aObj = (Object[]) list.get(i);
					Short s = (Short) (list.get(i)==null?new Short("0"):list.get(i));// aObj[0];
					if(s==0){
						str = "";
					}
					else if (s == 1){
						str = "open";
					}
					else if (s == 2){
						str = "resolved";
					}
					break;
				}
			else
				str = "nojournals";
			}else{
				str = "nojournals";
			}
			aJoJournalStr=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return str;
	}

	@Override
	public Integer getJobNumberFromReleaseDetail(Integer joReleaseDetailID) throws JobException {
		Session aSession = null;
		Integer str =0;
		try {
			aSession = itsSessionFactory.openSession();
			String aJoJournalStr = "SELECT joMasterID FROM joMaster WHERE joMaster.joMasterID IN	"
					+ "( SELECT joRelease.joMasterID FROM joRelease WHERE joRelease.joReleaseID IN "
					+ "(SELECT joReleaseID FROM joReleaseDetail WHERE 	joReleaseDetail.joReleaseDetailID="+joReleaseDetailID+"))";
			if(joReleaseDetailID!=null){
			Query aQuery = aSession.createSQLQuery(aJoJournalStr);
			List list = aQuery.list();
			if (list.size() > 0)
				for (int i = 0; i < list.size(); i++) {
					str= (Integer)list.get(i);
				}
		}
			aJoJournalStr=null;
			}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return str;
	}
	
	@Override
	public Jomaster getJobNumberFromRelease(Integer joReleaseID) throws JobException {
		Session aSession = null;
		Jomaster ajoMaster=new Jomaster();
		try {
			aSession = itsSessionFactory.openSession();
			String aJoJournalStr = "SELECT joMasterID,JobNumber FROM joMaster WHERE joMaster.joMasterID IN	"
					+ "( SELECT joRelease.joMasterID FROM joRelease WHERE joRelease.joReleaseID = "+joReleaseID+")";
			if(joReleaseID!=null){
			Query aQuery = aSession.createSQLQuery(aJoJournalStr);
			List<?> list = aQuery.list();
			Iterator<?> aIterator = list.iterator();
				if (list.size() > 0){
					Object[] aObj = (Object[]) aIterator.next();
					ajoMaster.setJoMasterId((Integer) aObj[0]);
					ajoMaster.setJobNumber((String) aObj[1]);
					ajoMaster=(Jomaster) aSession.get(Jomaster.class, ajoMaster.getJoMasterId());
				}
			}
			
			}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return ajoMaster;
	}
	
	@Override
	public List<JobReleaseBean> getReleaseList(String theJobNumber,Integer joMasterID)
			throws JobException {
		/*
		 * String aReleaseQry = "SELECT DISTINCT " + "joRelease.releasedate, " +
		 * "joRelease.releasetype, " + "veFactory.veFactoryID, " +
		 * "rxMaster.name, " + "joRelease.releasenote , " +
		 * "joRelease.estimatedbilling, " + "vePO.subtotal, " +
		 * "vePO.taxtotal, " + "vePO.freight, " +
		 * "joReleaseDetail.joReleaseDetailID, " +
		 * "joReleaseDetail.VendorInvoiceAmount, " +
		 * "joReleaseDetail.CustomerInvoiceAmount, " + "joRelease.joReleaseId, "
		 * + "vePO.vePoID, " + "vePO.poNumber, " + "joRelease.BillNote, " +
		 * "rxMaster.Websight, " + "rxMaster.rxMasterID, " +
		 * "vePO.rxVendorContactID, " + "vePO.ShipToMode, " +
		 * "vePO.emailTimeStamp, " + "vePO.rxBillToAddressID, " +
		 * "vePO.rxShipToAddressID, " + "vePO.rxVendorID, " +
		 * "vePO.CustomerPONumber, " + "joRelease.POID, " + "cuSO.cuSoid, " +
		 * "cuSO.subtotal, " + "cuSO.taxtotal, " + "cuSO.freight, " +
		 * "cuSO.CreatedByID, " + "cuSO.ShipToMode, " +
		 * "cuSO.rxBillToAddressID, " + "cuSO.rxShipToAddressID, " +
		 * "cuSO.rxCustomerID, " + "cuSO.sonumber, " +
		 * "joReleaseDetail.joReleaseDetailID, " +
		 * "joReleaseDetail.VendorInvoiceAmount, " +
		 * "joReleaseDetail.CustomerInvoiceAmount, " +
		 * "joRelease.joReleaseId, vePO.vePoID, vePO.poNumber, joRelease.BillNote, "
		 * +
		 * "rxMaster.Websight, rxMaster.rxMasterID, vePO.rxVendorContactID, vePO.ShipToMode, "
		 * +
		 * "vePO.emailTimeStamp, vePO.rxBillToAddressID, vePO.rxShipToAddressID, vePO.rxVendorID, "
		 * +
		 * "vePO.CustomerPONumber, joRelease.POID, cuSO.cuSoid, joRelease.joReleaseID "
		 * + "FROM joMaster LEFT JOIN joRelease " +
		 * "ON joMaster.joMasterID=joRelease.joMasterID " +
		 * "LEFT JOIN joReleaseDetail ON joRelease.joReleaseID = joReleaseDetail.joReleaseID "
		 * + "LEFT JOIN vePO ON joRelease.joReleaseID = vePO.joReleaseID " +
		 * "LEFT JOIN cuSO ON cuSO.joReleaseID = joReleaseDetail.joReleaseID " +
		 * "LEFT JOIN veFactory ON veFactory.rxMasterID = vePO.rxVendorID " +
		 * "LEFT JOIN rxMaster ON vePO.rxVendorID = rxMaster.rxMasterID OR cuSO.rxCustomerID = rxMaster.rxMasterID "
		 * + "WHERE jobnumber='" + theJobNumber.trim() + "';";
		 */

		String aReleaseQry = "SELECT DISTINCT "
				+ "joRelease.releasedate, "
				+ "joRelease.releasetype, "
				+ "veFactory.veFactoryID, "
				+ "rxMaster.name, "
				+ "joRelease.releasenote , "
				+ "joRelease.estimatedbilling, "
				+ "vePO.subtotal, "
				+ "vePO.taxtotal, "
				+ "vePO.freight, "
				+ "joReleaseDetail.joReleaseDetailID, "
				+ "(SELECT SUM(VendorInvoiceAmount) FROM joReleaseDetail vpd WHERE vpd.joReleaseID=joRelease.joReleaseID) as VendorInvoiceAmount, "
				+ "(SELECT SUM(CustomerInvoiceAmount) FROM joReleaseDetail vpd WHERE vpd.joReleaseID=joRelease.joReleaseID) as CustomerInvoiceAmount, "
				+ "joRelease.joReleaseId, "
				+ "vePO.vePoID, "
				+ "vePO.poNumber, "
				+ "joRelease.BillNote, "
				+ "rxMaster.Websight, "
				+ "rxMaster.rxMasterID, "
				+ "vePO.rxVendorContactID, "
				+ "vePO.ShipToMode, "
				+ "vePO.emailTimeStamp, "
				+ "vePO.rxBillToAddressID, "
				+ "vePO.rxShipToAddressID, "
				+ "vePO.rxVendorID, "
				+ "IFNULL(vePO.CustomerPONumber,cuSO.CustomerPONumber), "
				+ "joRelease.POID, "
				+ "cuSO.cuSoid, "
				/*+ "cuSO.subtotal, "
				+ "cuSO.taxtotal, "
				+ "cuSO.freight, "
				+ "cuSO.CreatedByID, "
				+ "cuSO.ShipToMode, "
				+ "cuSO.rxBillToAddressID, "
				+ "cuSO.rxShipToAddressID, "
				+ "cuSO.rxCustomerID, "*/
				+ "cuSO.sonumber, "
				+ "IFNULL(cuSO.TransactionStatus,3), "
				/*+ "joReleaseDetail.joReleaseDetailID, "
				+ "joReleaseDetail.VendorInvoiceAmount, "
				+ "joReleaseDetail.CustomerInvoiceAmount, "
				+ "joRelease.joReleaseId, vePO.vePoID, vePO.poNumber, joRelease.BillNote, "
				+ "rxMaster.Websight, rxMaster.rxMasterID, vePO.rxVendorContactID, vePO.ShipToMode, "
				+ "vePO.emailTimeStamp, vePO.rxBillToAddressID, vePO.rxShipToAddressID, vePO.rxVendorID, "
				+ "vePO.CustomerPONumber, joRelease.POID, cuSO.cuSoid, joRelease.joReleaseID, "*/
				+ "rxAddress.Address1,rxAddress.Address2,rxAddress.City,rxAddress.State,rxAddress.Zip, "
				// Added by velmurugan
				+ "(SELECT SUM(QuantityBilled) FROM veBill vb JOIN veBillDetail vbd ON(vb.veBillID=vbd.veBillID) WHERE joReleaseDetailID = joReleaseDetail.joReleaseDetailID) AS vebilltotalitemqty ,"
				+ "(SELECT SUM(QuantityOrdered) FROM vePODetail vpd WHERE vpd.vePOID=vePO.vePOID) as vepodetailtotalquantity, "
				+ "joRelease.seq_Number,vePO.TransactionStatus AS vepotrans,joRelease.release_status,cuSO.TransactionStatus "
				// up to this line
				+ "FROM joMaster LEFT JOIN joRelease "
				+ "ON joMaster.joMasterID=joRelease.joMasterID "
				+ "LEFT JOIN joReleaseDetail ON joRelease.joReleaseID = joReleaseDetail.joReleaseID "
				+ "LEFT JOIN vePO ON joRelease.joReleaseID = vePO.joReleaseID "
				+ "LEFT JOIN cuSO ON cuSO.joReleaseID = joRelease.joReleaseID "
				+ "LEFT JOIN veFactory ON veFactory.rxMasterID = vePO.rxVendorID "
				+ "LEFT JOIN rxMaster ON vePO.rxVendorID = rxMaster.rxMasterID OR cuSO.rxCustomerID = rxMaster.rxMasterID "
				+ "LEFT JOIN rxAddress ON rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.rxAddressID =( SELECT rxAdd.rxAddressID FROM rxAddress AS rxAdd WHERE rxAdd.rxMasterID = rxMaster.rxMasterID LIMIT 1,1) "
				+ "WHERE joMaster.joMasterID= "+ joMasterID
				+ " group by joRelease.joReleaseID;";

		itsLogger.info("aReleaseQry ==>" + aReleaseQry);
		Session aSession = null;
		List<?> aReleaseList =null;
		Query aQuery = null;
		List<JobReleaseBean> aQryList = new LinkedList<JobReleaseBean>();
		try {
			JobReleaseBean aJobReleaseBean = null;
			itsLogger.info("getReleaseList()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aReleaseQry);
			aReleaseList = aQuery.list();
			Iterator<?> aIterator = aReleaseList.iterator();
			while (aIterator.hasNext()) {
				String invoicestatus = null;
				Object[] aObj =null;
				BigDecimal obj60 = BigDecimal.ZERO;
				BigDecimal obj61 =BigDecimal.ZERO;
				try {
					aJobReleaseBean = new JobReleaseBean();
					aObj = (Object[]) aIterator.next();
					
					if (aObj[0] != null) {
						aJobReleaseBean.setReleased(DateFormatUtils.format((Date) aObj[0], "MM/dd/yyyy "));
					}
					if (aObj[1] == null) {
						return null;
					}
					else{
						aJobReleaseBean.setType((Short) aObj[1]);
					}
					aJobReleaseBean.setManufacturerId((Integer) aObj[2]);
					if ((Short) aObj[1] != 2 && (Short) aObj[1] != 5) {
						aJobReleaseBean.setManufacturer((String) aObj[3]);
					}
					aJobReleaseBean.setNote((String) aObj[4]);
					aJobReleaseBean.setEstimatedBilling((BigDecimal) aObj[5]);
					aJobReleaseBean.setAllocated((BigDecimal) aObj[6]);
					aJobReleaseBean.setJoReleaseDetailid((Integer) aObj[9]);
					// (BigDecimal) aObj[6];
					/*
					 * if(aObj[6]!=null){ POAmount =
					 * POAmount.add((BigDecimal)aObj[6]); } if(aObj[7]!=null){
					 * POAmount = POAmount.add((BigDecimal)aObj[7]); }
					 * if(aObj[8]!=null){ POAmount =
					 * POAmount.add((BigDecimal)aObj[8]); } Added By Zenith on
					 * 2014-09-19 if(aObj[10]!=null){ POAmount =
					 * POAmount.add((BigDecimal)aObj[10]); }
					 */
					BigDecimal InvoiceAmount = (BigDecimal) aObj[11];
					aJobReleaseBean.setJoReleaseId((Integer) aObj[12]);
//	System.out.println("Release Id :: "
						//	+ aJobReleaseBean.getJoReleaseId());
					aJobReleaseBean.setVePoId((Integer) aObj[13]);
					// if()
					/*if ((Short) aObj[1] == 1) {
						aJobReleaseBean.setPonumber((String) aObj[14]);
					} else {
						aJobReleaseBean.setPonumber((String) aObj[35]);
					}*/

					aJobReleaseBean.setBillNote((String) aObj[15]);
					aJobReleaseBean.setBillNoteImage((String) aObj[15]);
					aJobReleaseBean.setWebSight((String) aObj[16]);
					aJobReleaseBean.setRxMasterId((Integer) aObj[17]);
					/* aJobReleaseBean.setPO(POAmount); */
					aJobReleaseBean.setPO((BigDecimal) aObj[6]);
					// System.out.println("POAmount :: "+POAmount);
					aJobReleaseBean.setInvoiceAmount(InvoiceAmount);
					aJobReleaseBean.setRxVendorContactID((Integer) aObj[18]);
					aJobReleaseBean.setShipToMode((Short) aObj[19]);
					aJobReleaseBean.setBillToAddressID((Integer) aObj[21]);
					aJobReleaseBean.setShipToAddressID((Integer) aObj[22]);
					aJobReleaseBean.setRxAddressID((Integer) aObj[23]);
					aJobReleaseBean.setCustomerPONumber((String) aObj[24]);
					aJobReleaseBean.setPOID((String)aObj[25]);
					aJobReleaseBean.setCuSOID((Integer) aObj[26]);
					/*aJobReleaseBean.setAddress1((String) aObj[56]);
					aJobReleaseBean.setAddress2((String) aObj[57]);
					aJobReleaseBean.setCity((String) aObj[58]);
					aJobReleaseBean.setState((String) aObj[59]);
					aJobReleaseBean.setZip((String) aObj[60]);
					String invoicestatus = null;
					BigDecimal obj60 = (BigDecimal) aObj[61];
					BigDecimal obj61 = (BigDecimal) aObj[62];*/
					
					aJobReleaseBean.setAddress1((String) aObj[29]);
					aJobReleaseBean.setAddress2((String) aObj[30]);
					aJobReleaseBean.setCity((String) aObj[31]);
					aJobReleaseBean.setState((String) aObj[32]);
					aJobReleaseBean.setZip((String) aObj[33]);
					
					obj60 = (BigDecimal) aObj[34];
					obj61 = (BigDecimal) aObj[35];
					
//	System.out.println("aObj[60]" + aObj[61] + "aObj[61]"+ aObj[61]);
					/*aJobReleaseBean.setTransactionStatus((Integer) aObj[36]);*/
					aJobReleaseBean.setTransactionStatus((Integer) aObj[28]);
					//itsLogger.info("TransactionStatus: "+aJobReleaseBean.getTransactionStatus());
					/*Commented By Jenith on 2015-05-06
					 * if (obj60 != null && obj61 != null) {
						if(aObj[64] !=null){
							if((Short) aObj[64]==2){
								invoicestatus = "true";
							}else{
								invoicestatus = "false";
							}
						}*/
					if(aJobReleaseBean.getType()==1|| aJobReleaseBean.getType()==4){
						if(aObj[37]==null){
							aObj[37]=1;
						}
						aJobReleaseBean.setTransStatus(JobUtil.ConvertintoInteger(aObj[37].toString()));
					}else if(aJobReleaseBean.getType()==2 || aJobReleaseBean.getType()==5){
						if(aObj[39]==null){
							aObj[39]=1;
						}
						aJobReleaseBean.setTransStatus(JobUtil.ConvertintoInteger( aObj[39].toString()));
					}
					
					if (obj60 != null && obj61 != null) {
						if(aObj[37] !=null){
							
							if(aObj[37] instanceof Integer)
							{
								if((Integer) aObj[37]==2){
									invoicestatus = "true";
								}else{
									invoicestatus = "false";
								}
							}
							else
							{
								if((Short) aObj[37]==2){
									invoicestatus = "true";
								}else{
									invoicestatus = "false";
								}
							}
						}
						/*if (obj60.equals(obj61)) {
							invoicestatus = "true";
						} else if (obj60 != null
								&& obj60.compareTo(BigDecimal.ZERO) != 0) {
							invoicestatus = "false";
						}*/
					}else if(aJobReleaseBean.getType()==3){
						/*Commented By Jenith on 2015-05-06
						 * if((Integer)aObj[65]==2){
						invoicestatus = "true";
						}*/
						if((Integer)aObj[38]==2){
							invoicestatus = "true";
							}
					}else{
						if(aJobReleaseBean.getTransactionStatus()==2){
							invoicestatus="close";
						}
					}
					
					//System.out.println(invoicestatus);
					aJobReleaseBean.setCheckcloseoropen(invoicestatus);
					aJobReleaseBean.setCheckcloseoropenhidden(invoicestatus);
					boolean checkedornot = getSplittypethereornot(
							theJobNumber.trim(), aJobReleaseBean.getJoReleaseId());

					if (checkedornot) {
						aJobReleaseBean.setSplitchkbox(checkedornot);
					} else {
						aJobReleaseBean.setSplitchkbox(checkedornot);
					}
					/*if ((Short) aObj[1] == 1) {
						aJobReleaseBean.setPonumber((String) aObj[14]);
					} else {
						aJobReleaseBean.setPonumber((String) aObj[35]);
					}*/

/*	if (aJobReleaseBean.getPonumber() == null)
						aJobReleaseBean.setPonumber((String) aObj[35]);
					*/
					//Alphabet newly changed
					/*Commented By Jenith on 2015-05-06
					 * aJobReleaseBean.setPonumber(JobUtil.IntToLetter((Integer) aObj[63]).toUpperCase());*/
					aJobReleaseBean.setPonumber(JobUtil.IntToLetter((Integer) aObj[36]).toUpperCase());
					aQryList.add(aJobReleaseBean);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					invoicestatus=null;
					aJobReleaseBean=null;
					aObj =null;
					obj60=null;
					obj61=null;
				}
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getReleaseList()=[Connection Closed]");
			aReleaseQry=null;
			aReleaseList=null;
			aQuery=null;
		}
		return aQryList;

	} 

	@Override 
	public List<JobShippingBean> getShippingList(String theJobNumber,
			Integer theJoDetailsID, String releaseType) throws JobException {

		itsLogger.info("InvoiceDate Testing");
		Session aSession = null;
		ArrayList<JobShippingBean> aQryArrayList = new ArrayList<JobShippingBean>();
		try {
			String aShippingQry = "SELECT jrd.*,ve.veBillID,ve.InvoiceNumber,ve.veShipViaID,ship.Description,cu.cuInvoiceID,"
					+ "cu.InvoiceDate,cu.ShipToMode,ve.BillAmount - (ve.TaxAmount+ve.FreightAmount) AS veSubtotal,"
					+ "ve.TransactionStatus,"
					+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.checkNo AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = ve.veBillID) AS reference,"
					+ "Date((SELECT DISTINCT GROUP_CONCAT(CAST(vph.datePaid AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = ve.veBillID)) AS datePaid,"
					+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.amountVal AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = ve.veBillID) AS Amount,"
					+ "cu.cIopenStatus,ve.BillAmount , cu.InvoiceAmount, ve.BillDate,ship.trackUrl,ve.trackingNumber FROM joRelease jr "  //ve.AppliedAmount,
					+ "JOIN joReleaseDetail jrd ON jr.joReleaseId=jrd.joReleaseID "
					+ "LEFT JOIN veBill ve ON ve.joReleaseDetailID=jrd.joReleaseDetailID "
					//+ "LEFT JOIN moTransaction ON moTransactionID IN (SELECT DISTINCT moTransactionID FROM moLinkageDetail WHERE veBillID = ve.veBillID group by veBillID) "
					+ "LEFT JOIN cuInvoice cu ON cu.joReleaseDetailID=jrd.joReleaseDetailID "
					+ "LEFT JOIN veShipVia ship ON ship.veShipViaID = ve.veShipViaID WHERE jr.joReleaseID ='"
					+ theJoDetailsID + "'";

			itsLogger.info("getShippingList()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aShippingQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				JobShippingBean aJobShippingBean = new JobShippingBean();
				aJobShippingBean.setJoReleaseDetailID((Integer) aObj[0]);
				// aJobShippingBean.setJoReleaseID((Integer) aObj[1]);
				if (aObj[3] != null) {
					aJobShippingBean.setShipDate(DateFormatUtils.format(
							(Date) aObj[3], "MM/dd/yyyy "));
				}

				if (aObj[4] != null) {
					aJobShippingBean.setVendorDate(DateFormatUtils.format(
							(Date) aObj[4], "MM/dd/yyyy "));
				}

				// aJobShippingBean.setVendorInvoice((String) aObj[5]);
				if (aObj[5] != null) {
					aJobShippingBean.setVendorAmount((BigDecimal) aObj[5]);
				}
				if (aObj[6] != null) {
					aJobShippingBean.setCustomerDate(DateFormatUtils.format(
							(Date) aObj[6], "MM/dd/yyyy "));
				}
				if (aObj[7] != null) {
					aJobShippingBean.setCustomerAmount((BigDecimal) aObj[7]);
				}
				if (aObj[11] != null) {
					aJobShippingBean.setVeBillID((Integer) aObj[11]);
				}
				if (aObj[12] != null) {
					aJobShippingBean.setVendorInvoice((String) aObj[12]);
				}
				if (aObj[13] != null) {
					aJobShippingBean.setVeShipViaID((Integer) aObj[13]);
				}
				if (aObj[14] != null) {
					aJobShippingBean.setShippingLine((String) aObj[14]);
				}
				if (aObj[15] != null) {
					aJobShippingBean.setCuInvoiceID((Integer) aObj[15]);
				}
				
				if (aObj[16] != null) {
					aJobShippingBean.setInvoiceDate(DateFormatUtils.format(
							(Date) aObj[16], "MM/dd/yyyy "));
				}
				if (aObj[17] != null) {
					aJobShippingBean.setShiptoMode((Short) aObj[17]);
				}
				if (aObj[18] != null) {
					aJobShippingBean.setVendorsubtotalAmt((BigDecimal) aObj[18]);
				}

				if (aObj[19] != null) {
					aJobShippingBean.setTransactionStatus((Short) aObj[19]);
				}
				if (aObj[20] != null) {
					aJobShippingBean.setVechkNo((String) aObj[20]);
				}
				if (aObj[21] != null) {
					aJobShippingBean.setVedatePaid(DateFormatUtils.format((Date) aObj[21], "MM/dd/yyyy "));
					//aJobShippingBean.setVedatePaid( aObj[21].toString());
				}
				if (aObj[22] != null) {
					
					aJobShippingBean.setVendorAppliedAmt(aObj[22].toString());
					
				}
				if (aObj[23] != null) {
					aJobShippingBean.setcIopenStatus((Boolean) aObj[23]);
				}
				if (aObj[24] != null) {
					aJobShippingBean.setVendorAmount((BigDecimal) aObj[24]);
				}
				if (aObj[25] != null) {
					aJobShippingBean.setCustomerAmount((BigDecimal) aObj[25]);
				}
				if (aObj[26] != null) {
					aJobShippingBean.setVeBillDate(DateFormatUtils.format(
							(Date) aObj[26], "MM/dd/yyyy "));
				}
				if (aObj[27] != null) {
					aJobShippingBean.setWebSight((String)aObj[27]);
				}
				if (aObj[28] != null) {
					aJobShippingBean.setTrackingNumber((String)aObj[28]);
				}
				
				aQryArrayList.add(aJobShippingBean);
			}
			aShippingQry=null;
		} catch (Exception e) {

			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getShippingList()=[Connection Closed]");
		}
		return aQryArrayList;
	}

	
	@Override
	public List<JobShippingBean> getCommissionInvoiceList(String theJobNumber,
			Integer theJoDetailsID, String releaseType) throws JobException {

		itsLogger.info("InvoiceDate Testing");
		Session aSession = null;
		ArrayList<JobShippingBean> aQryArrayList = new ArrayList<JobShippingBean>();
		try {
			String aShippingQry = "SELECT jrd.*,ve.veCommDetailID,ve.InvoiceNumber,ve.veShipViaID,ship.Description FROM joRelease jr "
					+ "JOIN joReleaseDetail jrd ON jr.joReleaseId=jrd.joReleaseID "
					+ "LEFT JOIN veCommDetail ve ON ve.joReleaseDetailID=jrd.joReleaseDetailID "
					+ "LEFT JOIN veShipVia ship ON ship.veShipViaID = ve.veShipViaID WHERE jr.joReleaseID ='"
					+ theJoDetailsID + "'";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aShippingQry);
			Iterator<?> aIterator = aQuery.list().iterator();

			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				JobShippingBean aJobShippingBean = new JobShippingBean();
				aJobShippingBean.setJoReleaseDetailID((Integer) aObj[0]);
				// aJobShippingBean.setJoReleaseID((Integer) aObj[1]);
				if (aObj[3] != null) {
					aJobShippingBean.setShipDate(DateFormatUtils.format(
							(Date) aObj[3], "MM/dd/yyyy "));
				}

				if (aObj[4] != null) {
					aJobShippingBean.setVendorDate(DateFormatUtils.format(
							(Date) aObj[4], "MM/dd/yyyy "));
				}

				// aJobShippingBean.setVendorInvoice((String) aObj[5]);
				if (aObj[5] != null) {
					aJobShippingBean.setVendorAmount((BigDecimal) aObj[5]);
				}
				if (aObj[6] != null) {
					aJobShippingBean.setCustomerDate(DateFormatUtils.format(
							(Date) aObj[6], "MM/dd/yyyy "));
				}
				if (aObj[7] != null) {
					aJobShippingBean.setCustomerAmount((BigDecimal) aObj[7]);
				}
				if (aObj[11] != null) {
					aJobShippingBean.setVeCommDetailID((Integer) aObj[11]);
				}
				if (aObj[12] != null) {
					aJobShippingBean.setVendorInvoice((String) aObj[12]);
				}
				if (aObj[13] != null) {
					aJobShippingBean.setVeShipViaID((Integer) aObj[13]);
				}
				if (aObj[14] != null) {
					aJobShippingBean.setShippingLine((String) aObj[14]);
				}
				aQryArrayList.add(aJobShippingBean);
			}
			aShippingQry=null;
		} catch (Exception e) {

			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQryArrayList;
	}
	
	
	
	public List<JobShippingBean> oldgetShippingList(String theJobNumber,
			Integer theJoDetailsID, String releaseType) throws JobException {
		String aShippingQry = "SELECT DISTINCT joReleaseDetail.shipdate, veShipVia.description, joReleaseDetail.vendorinvoicedate, veBill.invoicenumber, veBill.BillAmount, "
				+ "joReleaseDetail.customerinvoicedate, joReleaseDetail.customerinvoiceamount, veShipVia.veShipViaID, joReleaseDetail.joReleaseDetailID, veBill.veBillID, "
				+ "veBill.APAccountID, cuInvoice.cuInvoiceID FROM joMaster "
				+ "JOIN joReleaseDetail ON joMaster.joMasterid=joReleaseDetail.joMasterid "
				+ "LEFT JOIN veBill ON joReleaseDetail.joReleaseDetailid= veBill.joReleaseDetailid "
				+ "LEFT JOIN cuInvoice ON joReleaseDetail.joReleaseDetailid= cuInvoice.joReleaseDetailid ";
		if (releaseType.equalsIgnoreCase("Drop Ship")) {
			aShippingQry = aShippingQry
					+ "LEFT JOIN veShipVia ON veBill.veShipViaid=veShipVia.veShipViaid ";
		} else {
			aShippingQry = aShippingQry
					+ "LEFT JOIN veShipVia ON cuInvoice.veShipViaid=veShipVia.veShipViaid ";
		}
		aShippingQry = aShippingQry + "WHERE jobnumber = '" + theJobNumber
				+ "' AND joReleaseDetail.joReleaseDetailID = '"
				+ theJoDetailsID + "'";
		if (!releaseType.equalsIgnoreCase("Drop Ship")) {
			aShippingQry = aShippingQry
					+ "AND joReleaseDetail.customerinvoicedate IS NOT NULL";
		} else {
			aShippingQry = aShippingQry
					+ "AND joReleaseDetail.VendorInvoiceDate IS NOT NULL";
		}
		String aShippCuInvoiceQry = "SELECT DISTINCT joReleaseDetail.shipdate, veShipVia.description, joReleaseDetail.vendorinvoicedate, joReleaseDetail.vendorinvoiceamount, "
				+ "joReleaseDetail.customerinvoicedate, joReleaseDetail.customerinvoiceamount, veShipVia.veShipViaID, joReleaseDetail.joReleaseDetailID, cuInvoice.cuInvoiceID "
				+ "FROM joMaster JOIN joReleaseDetail ON joMaster.joMasterid=joReleaseDetail.joMasterid "
				+ "JOIN cuInvoice ON joReleaseDetail.joReleaseDetailid= cuInvoice.joReleaseDetailid "
				+ "JOIN veShipVia ON cuInvoice.veShipViaid=veShipVia.veShipViaid WHERE jobnumber = '"
				+ theJobNumber
				+ "' AND joReleaseDetail.joReleaseDetailID = '"
				+ theJoDetailsID + "'";
		String aShippVeBillQry = "SELECT DISTINCT joReleaseDetail.shipdate, veShipVia.description, joReleaseDetail.vendorinvoicedate, veBill.invoicenumber, joReleaseDetail.vendorinvoiceamount, "
				+ "joReleaseDetail.customerinvoicedate, joReleaseDetail.customerinvoiceamount, veShipVia.veShipViaID, joReleaseDetail.joReleaseDetailID, veBill.veBillID, "
				+ "veBill.APAccountID FROM joMaster JOIN joReleaseDetail ON joMaster.joMasterid=joReleaseDetail.joMasterid "
				+ "JOIN veBill ON joReleaseDetail.joReleaseDetailid= veBill.joReleaseDetailid "
				+ "JOIN veShipVia ON veBill.veShipViaid=veShipVia.veShipViaid WHERE jobnumber = '"
				+ theJobNumber
				+ "' AND joReleaseDetail.joReleaseDetailID = '"
				+ theJoDetailsID + "'";
		Session aSession = null;
		ArrayList<JobShippingBean> aQryArrayList = new ArrayList<JobShippingBean>();
		try {
			JobShippingBean aJobShippingBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aShippingQry);
			Query aQueryCuInvoice = aSession.createSQLQuery(aShippCuInvoiceQry);
			Query aQueryVeBillQry = aSession.createSQLQuery(aShippVeBillQry);
			List aList = aQuery.list();
			Iterator<?> aIterator = aList.iterator();
			if (!aList.isEmpty()) {
				
				aIterator = aList.iterator();
			} else if (!aQueryCuInvoice.list().isEmpty()) {
				
				aIterator = aQueryCuInvoice.list().iterator();
			} else if (!aQueryVeBillQry.list().isEmpty()) {
				
				aIterator = aQueryVeBillQry.list().iterator();
			}
			while (aIterator.hasNext()) {
				aJobShippingBean = new JobShippingBean();
				Object[] aObj = (Object[]) aIterator.next();
				if (!aList.isEmpty()) {
					if (aObj[0] != null) {
						aJobShippingBean.setShipDate(DateFormatUtils.format(
								(Date) aObj[0], "MM/dd/yyyy "));
					}
					aJobShippingBean.setShippingLine((String) aObj[1]);
					if (aObj[2] != null) {
						aJobShippingBean.setVendorDate(DateFormatUtils.format(
								(Date) aObj[2], "MM/dd/yyyy "));
					}
					aJobShippingBean.setVendorInvoice((String) aObj[3]);
					aJobShippingBean.setVendorAmount((BigDecimal) aObj[4]);
					if (aObj[5] != null) {
						aJobShippingBean.setCustomerDate(DateFormatUtils
								.format((Date) aObj[5], "MM/dd/yyyy "));
					}
					aJobShippingBean.setCustomerAmount((BigDecimal) aObj[6]);
					aJobShippingBean.setVeShipViaID((Integer) aObj[7]);
					aJobShippingBean.setJoReleaseDetailID((Integer) aObj[8]);
					aJobShippingBean.setVeBillID((Integer) aObj[9]);
					aJobShippingBean.setCoAccountID((Integer) aObj[10]);
					aJobShippingBean.setCuInvoiceID((Integer) aObj[11]);
				} else if (!aQueryCuInvoice.list().isEmpty()) {
					if (aObj[0] != null) {
						aJobShippingBean.setShipDate(DateFormatUtils.format(
								(Date) aObj[0], "MM/dd/yyyy "));
					}
					aJobShippingBean.setShippingLine((String) aObj[1]);
					if (aObj[2] != null) {
						aJobShippingBean.setVendorDate(DateFormatUtils.format(
								(Date) aObj[2], "MM/dd/yyyy "));
					}
					aJobShippingBean.setVendorAmount((BigDecimal) aObj[3]);
					if (aObj[4] != null) {
						aJobShippingBean.setCustomerDate(DateFormatUtils
								.format((Date) aObj[4], "MM/dd/yyyy "));
					}
					aJobShippingBean.setCustomerAmount((BigDecimal) aObj[5]);
					aJobShippingBean.setVeShipViaID((Integer) aObj[6]);
					aJobShippingBean.setJoReleaseDetailID((Integer) aObj[7]);
					aJobShippingBean.setCuInvoiceID((Integer) aObj[8]);
				} else if (!aQueryVeBillQry.list().isEmpty()) {
					if (aObj[0] != null) {
						aJobShippingBean.setShipDate(DateFormatUtils.format(
								(Date) aObj[0], "MM/dd/yyyy "));
					}
					aJobShippingBean.setShippingLine((String) aObj[1]);
					if (aObj[2] != null) {
						aJobShippingBean.setVendorDate(DateFormatUtils.format(
								(Date) aObj[2], "MM/dd/yyyy "));
					}
					aJobShippingBean.setVendorInvoice((String) aObj[3]);
					aJobShippingBean.setVendorAmount((BigDecimal) aObj[4]);
					if (aObj[5] != null) {
						aJobShippingBean.setCustomerDate(DateFormatUtils
								.format((Date) aObj[5], "MM/dd/yyyy "));
					}
					aJobShippingBean.setCustomerAmount((BigDecimal) aObj[6]);
					aJobShippingBean.setVeShipViaID((Integer) aObj[7]);
					aJobShippingBean.setJoReleaseDetailID((Integer) aObj[8]);
					aJobShippingBean.setVeBillID((Integer) aObj[9]);
					aJobShippingBean.setCoAccountID((Integer) aObj[10]);
				}
				aQryArrayList.add(aJobShippingBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aShippingQry=null;aShippCuInvoiceQry=null;aShippVeBillQry=null;
		}
		return aQryArrayList;
	}

	@Override
	public Vecommdetail getVeCommissionDetails(Integer veCommissionDetailID) throws JobException {
		Session aSession = null;
		Vecommdetail aVecommdetail = null;
		try {
			
			aSession = itsSessionFactory.openSession();
			if (veCommissionDetailID > 0) {
				aVecommdetail = (Vecommdetail) aSession.get(Vecommdetail.class, veCommissionDetailID);
				
			} else {
				aVecommdetail = new Vecommdetail();
				
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVecommdetail;
	}
	
	@Override
	public List<Jochange> getChangeorderList(Integer thejoMasterID)
			throws JobException {
		String aReleaseQry = "SELECT DISTINCT " + " joChangeId, "
				+ " joMasterId, " + " ChangeDate, " + " CustomerPONumber, "
				+ " ChangeByID, " + " ChangeByName, " + " ChangeReason, "
				+ " ChangeAmount, " + " ChangeCost "
				+ " FROM joChange where joMasterId = " + thejoMasterID;
		Session aSession = null;
		ArrayList<Jochange> aQryList = new ArrayList<Jochange>();
		try {
			Jochange aJochange = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aReleaseQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJochange = new Jochange();
				Object[] aObj = (Object[]) aIterator.next();
				aJochange.setJoChangeId((Integer) aObj[0]);
				aJochange.setJoMasterId((Integer) aObj[1]);
				aJochange.setCustomerPonumber((String) aObj[3]);
				aJochange.setChangeById((Integer) aObj[4]);
				aJochange.setChangeByName((String) aObj[5]);
				aJochange.setChangeReason((String) aObj[6]);
				aJochange.setChangeAmount((BigDecimal) aObj[7]);
				aJochange.setChangeCost((BigDecimal) aObj[8]);
				if (aObj[2] != null) {
					aJochange.setChangdate(DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy "));
				}
				aQryList.add(aJochange);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aReleaseQry=null;
		}
		return aQryList;
	}
	
	@Override
	public List<JoInvoiceCost> getInvoiceCostList(Integer joReleaseDetailID) throws JobException {
		Session aSession = null;
			String aReleaseQry = "SELECT " + " joInvoiceCostID, "
					+ " joReleaseDetailID, " + " enteredBy, " + " enteredDate, "
					+ " reason, " + " cost ,"+" FullName,"
					+ " veBillID," + " cuInvoiceID" 
					+ " FROM joInvoiceCost LEFT JOIN tsUserLogin ON UserLoginID = enteredBy where joReleaseDetailID = " + joReleaseDetailID;
			ArrayList<JoInvoiceCost> aQryList = new ArrayList<JoInvoiceCost>();
			try {
				JoInvoiceCost aJoInvoiceCost = null;
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aReleaseQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					aJoInvoiceCost = new JoInvoiceCost();
					Object[] aObj = (Object[]) aIterator.next();
					aJoInvoiceCost.setJoInvoiceCostID((Integer) aObj[0]);
					aJoInvoiceCost.setJoReleaseDetailID((Integer) aObj[1]);
					aJoInvoiceCost.setEnteredBy((Integer) aObj[2]);
					
					if(aObj[3] != null)
					{
						Timestamp stamp = (Timestamp)aObj[3];
						java.sql.Date date = new java.sql.Date(stamp.getTime());
						aJoInvoiceCost.setEnteredDate(date);
					}
					aJoInvoiceCost.setReason((String) aObj[4]);
					aJoInvoiceCost.setCost((BigDecimal) aObj[5]);
					aJoInvoiceCost.setEnteredByName((String) aObj[6]);
					aJoInvoiceCost.setVeBillID((Integer) aObj[7]);
					aJoInvoiceCost.setCuInvoiceID((Integer) aObj[8]);
					aQryList.add(aJoInvoiceCost);
				}
			
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aReleaseQry=null;
		}
		return aQryList;
	}
	
	
	@Override
	public boolean deleteInvoiceCost(JoInvoiceCost aJoInvoiceCost) throws JobException {
	Session aSession = null;
	Transaction aTransaction = null;
	JoInvoiceCost theJoInvoiceCost = new JoInvoiceCost();
	try {
		aSession = itsSessionFactory.openSession();
		theJoInvoiceCost = (JoInvoiceCost) aSession.get(JoInvoiceCost.class,
		aJoInvoiceCost.getJoInvoiceCostID());

		aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		aSession.delete(theJoInvoiceCost);
		aTransaction.commit();
	} catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		JobException aJobException = new JobException(excep.getMessage(),
				excep);
		throw aJobException;
	} finally {
		aSession.flush();
		aSession.close();
	}
	return true;
	}
	
	
	@Override
	public boolean saveInvoiceCost(JoInvoiceCost aJoInvoiceCost) throws JobException {
	Session aSession = itsSessionFactory.openSession();
	Transaction aTransaction = null;
	try {
		aTransaction =aSession.beginTransaction();
		aTransaction.begin();
		if(aJoInvoiceCost.getJoInvoiceCostID()!=null && aJoInvoiceCost.getJoInvoiceCostID() >0){
			aSession.update(aJoInvoiceCost);
		} 
		else { 
			aSession.save(aJoInvoiceCost);
		}
		aTransaction.commit();
		
	} catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		JobException aJobException = new JobException(excep.getMessage(),
				excep);
		throw aJobException;
	} finally {
		aSession.flush();
		aSession.close();
	}
	return true;
	}
	
	@Override
	public boolean updateEmailTimestamp(String type, Integer processID) throws JobException {
	Session aSession = itsSessionFactory.openSession();
	Transaction aTransaction = null;
	Cuinvoice aCuinvoice = new Cuinvoice();
	try {
		aTransaction =aSession.beginTransaction();
		if(type.equals("cuInvoice")){
		aTransaction.begin();
		if(processID!=null && processID >0){
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,processID);
			aCuinvoice.setPrintDate(new Date());
			aSession.update(aCuinvoice);
		} 
		aTransaction.commit();
		}
	} catch (Exception excep) {
		itsLogger.error(excep.getMessage(), excep);
		JobException aJobException = new JobException(excep.getMessage(),
				excep);
		throw aJobException;
	} finally {
		aSession.flush();
		aSession.close();
	}
	return true;
	}
	
	@Override
	public boolean updateCustomerInvoiceCost(Integer joReleaseDetailID,Integer cuInvoiceID,Integer veBillID) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		Cuinvoice aCuinvoice = new Cuinvoice();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail(); 
			String aSummaryQry = "SELECT sum(IFNULL(cost,0.0000)) FROM joInvoiceCost where joReleaseDetailID = " + joReleaseDetailID;
			BigDecimal addedCostAmount = null;
			BigDecimal oldFreightCost = null;
			BigDecimal costTotal = null;
			BigDecimal newFreightCost = null;
			BigDecimal veBillFreightCost = null;
			Vebill aVebill = new Vebill();
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aSummaryQry);
				List<?> aList = aQuery.list();
				addedCostAmount = (BigDecimal) aList.get(0);
				addedCostAmount = addedCostAmount==null?new BigDecimal("0.0000"):addedCostAmount;
				if(cuInvoiceID!=null && cuInvoiceID>0){
					if(veBillID!=null && veBillID>0){
						aVebill = (Vebill) aSession.get(Vebill.class,veBillID);
						veBillFreightCost = aVebill.getAdditionalFreight();
						veBillFreightCost = addedCostAmount.subtract(veBillFreightCost==null?new BigDecimal("0.0000"):veBillFreightCost);
					}
					aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,cuInvoiceID);
					oldFreightCost = aCuinvoice.getFreightCost();
					//aCostAmount =  aCuinvoice.getCostTotal();
					oldFreightCost = oldFreightCost==null?new BigDecimal("0.0000"):oldFreightCost;
					newFreightCost = addedCostAmount==null?new BigDecimal("0.0000").subtract(oldFreightCost):addedCostAmount.subtract(oldFreightCost);
					//costTotal = aCuinvoice.getCostTotal()==null?new BigDecimal("0.0000"):aCuinvoice.getCostTotal().subtract(oldFreightCost);
					//costTotal = costTotal.add(newFreightCost==null?new BigDecimal("0.0000"):newFreightCost);
					aCuinvoice.setFreightCost(addedCostAmount);
					//aCuinvoice.setCostTotal(costTotal);
					aTransaction =aSession.beginTransaction();
					aTransaction.begin();
					aSession.update(aCuinvoice);
					aTransaction.commit();
				
					aTransaction =aSession.beginTransaction();
					aTransaction.begin();
					aJoReleaseDetail = (JoReleaseDetail) aSession.get(JoReleaseDetail.class,joReleaseDetailID);
					aJoReleaseDetail.setCustomerInvoiceCost(addedCostAmount);
					aSession.update(aJoReleaseDetail);
					aTransaction.commit();
				} else if(veBillID!=null && veBillID>0){
					aSummaryQry = "SELECT sum(IFNULL(cost,0.0000)) FROM joInvoiceCost where joReleaseDetailID = " + joReleaseDetailID +" AND veBillID="+veBillID;
					aSession = itsSessionFactory.openSession();
					aQuery = aSession.createSQLQuery(aSummaryQry);
					aList = aQuery.list();
					addedCostAmount = (BigDecimal) aList.get(0);
					aVebill = (Vebill) aSession.get(Vebill.class,veBillID);
					oldFreightCost = aVebill.getAdditionalFreight();
					oldFreightCost = oldFreightCost==null?new BigDecimal("0.0000"):oldFreightCost;
					//newFreightCost = aCostAmount==null?new BigDecimal("0.0000").subtract(oldFreightCost):aCostAmount.subtract(oldFreightCost);
					aVebill.setAdditionalFreight(addedCostAmount);
					aTransaction =aSession.beginTransaction();
					aTransaction.begin();
					aSession.update(aVebill);
					aTransaction.commit();
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aSummaryQry=null;
			}
	return true;
	}
	
	@Override
	public List<?> getjobSearchResultsJob(String theJobNumber, String theJobName)
			throws JobException {
		String aJobSelectQry = "SELECT joMaster.joMasterId, joMaster.jobNumber,"
				+ " joMaster.Description, joMaster.locationName, joMaster.locationCity,"
				+ " tsUserLogin.Initials, joStatus.JobStatus, rxMaster.Name,"
				+ " joMaster.CustomerPONumber, coDivision.Code, joMaster.SourceReport1,"
				+ " joMaster.bookedDate, joMaster.contractAmount, joMaster.estimatedCost"
				+ " FROM rxMaster RIGHT OUTER JOIN joStatus RIGHT OUTER JOIN coDivision RIGHT OUTER JOIN joMaster"
				+ " ON coDivision.coDivisionID = joMaster.coDivisionID ON joStatus.joStatusID = joMaster.JobStatus"
				+ " ON rxMaster.rxMasterID = joMaster.rxCustomerID LEFT OUTER JOIN tsUserLogin"
				+ " ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID WHERE joMaster.jobNumber = '"
				+ theJobNumber + "'";
		Session aSession = null;
		ArrayList<JobCustomerBean> aQueryList = new ArrayList<JobCustomerBean>();
		try {
			JobCustomerBean aJobCustomerBean = null;
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			/**
			 * In general you are not supposed to get the column names.
			 * Actually, that's exactly what an O2R mapper is about - hiding the
			 * R details and showing only O specifics.
			 **/
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobCustomerBean = new JobCustomerBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobCustomerBean.setJoMasterId((Integer) aObj[0]);
				aJobCustomerBean.setJobNumber((String) aObj[1]);
				aJobCustomerBean.setDescription((String) aObj[2]);
				aJobCustomerBean.setJobStatus((String) aObj[6]);
				/** JobStatus */
				aJobCustomerBean.setCustomerName((String) aObj[7]);
				/** Name */
				aQueryList.add(aJobCustomerBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<JobFinancialBean> getFinancialList(Integer joMasterID)
			throws JobException {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Calendar cal = Calendar.getInstance();
		    String currentdate=dateFormat.format(cal.getTime());
		
		    String aFinancialQry = "SELECT cuInvoice.invoicenumber ,"
					+ " cuInvoice.invoicedate , "
					+ " MAX(cul.datePaid),"
					+ " cuInvoice.InvoiceAmount , "
					+ " cuInvoice.donotmail , "
					+ " cuInvoice.surtaxtotal  , "
					+ " cuInvoice.surtaxrate , "
					+ " cuInvoice.surtaxamount,  "
					+ " cuInvoice.Subtotal,  "
					+ " IFNULL((IFNULL(cuInvoice.InvoiceAmount,0) - (SUM(IFNULL(cul.`PaymentApplied`,0))+IFNULL(cuInvoice.DiscountAmt,0))),0) as appliedAmt,  "
					+ " cuInvoice.Freight,cuInvoice.TaxAmount,DATEDIFF('"+currentdate+"',InvoiceDate) AS days, "
					+ " (SUM(IFNULL(cul.`PaymentApplied`,0))+IFNULL(cuInvoice.DiscountAmt,0)) AS paidamt "
					+ " FROM joMaster JOIN joReleaseDetail "
					+ " ON joMaster.joMasterid=joReleaseDetail.joMasterid "
					+ " JOIN cuInvoice ON joReleaseDetail.joReleaseDetailid=cuInvoice.joReleaseDetailid "
					+ " LEFT JOIN cuLinkageDetail cul ON(cuInvoice.cuInvoiceID=cul.cuInvoiceID)"
					+ " WHERE cuInvoice.TransactionStatus <> -1 and joMaster.joMasterID = '" + joMasterID + "' GROUP BY cuInvoice.cuInvoiceID ORDER BY joReleaseDetail.joReleaseID ,cuInvoice.invoicenumber  ASC";
 System.out.println("aFinancialQry===>"+aFinancialQry);
		/*String aFinancialQry = "SELECT cuInvoice.invoicenumber ,"
				+ " cuInvoice.invoicedate , "
				+ " joReleaseDetail.paymentdate,"
				+ " cuInvoice.InvoiceAmount , "
				+ " cuInvoice.donotmail , "
				+ " cuInvoice.surtaxtotal  , "
				+ " cuInvoice.surtaxrate , "
				+ " cuInvoice.surtaxamount,  "
				+ " cuInvoice.Subtotal,  "
				+ " (cuInvoice.InvoiceAmount - (cuInvoice.AppliedAmount+IFNULL(cuInvoice.DiscountAmt,0))) as appliedAmt,  "
				+ " cuInvoice.Freight,cuInvoice.TaxAmount,DATEDIFF('"+currentdate+"',InvoiceDate) AS days "
				+ " FROM joMaster JOIN joReleaseDetail "
				+ " ON joMaster.joMasterid=joReleaseDetail.joMasterid "
				+ " JOIN cuInvoice ON joReleaseDetail.joReleaseDetailid=cuInvoice.joReleaseDetailid "
				+ " WHERE jobnumber = '" + theJobNumber + "' ORDER BY joReleaseDetail.joReleaseID ,cuInvoice.invoicenumber  ASC";
*/
		/*
		 * The exact query from legacy, however both returns same value. Need to
		 * be replaced.
		 */
		/*
		 * SELECT SubInvoiceNumber, InvoiceDate, PaymentDate, InvoiceAmount ,
		 * (CASE WHEN Days<=30 THEN Balance ELSE null END) AS AmtCur , (CASE
		 * WHEN Days>30 AND Days<=60 THEN Balance ELSE null END) AS Amt30 ,
		 * (CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE null END) AS Amt60
		 * , (CASE WHEN Days>90 THEN Balance ELSE null END) AS Amt90 FROM
		 * (SELECT InvoiceNumber AS SubInvoiceNumber,
		 * InvoiceNumber,InvoiceDate,PaymentDate,InvoiceAmount
		 * ,InvoiceAmount-AppliedAmount AS Balance, DATEDIFF(InvoiceDate,
		 * '02-10-2014') AS Days FROM joReleaseDetail RIGHT JOIN cuInvoice ON
		 * joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID WHERE
		 * joReleaseDetail.joMasterID=26742 ORDER BY InvoiceNumber) AS SubQuery
		 */

		Session aSession = null;
		ArrayList<JobFinancialBean> aFinancialList = new ArrayList<JobFinancialBean>();
		try {
			JobFinancialBean aJobFinancialBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aFinancialQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobFinancialBean = new JobFinancialBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobFinancialBean.setInvoiceNumber((String) aObj[0]);
				if (aObj[1] != null) {
					aJobFinancialBean.setInvoiceDate(DateFormatUtils.format(
							(Date) aObj[1], "MM/dd/yyyy "));
				}
				if (aObj[2] != null) {
					aJobFinancialBean.setDatePaid(DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy "));
				}
				/*aJobFinancialBean.setCurrentDue1(new BigDecimal(0));
				if (aObj[3] != null && aObj[9] != null) {

					aJobFinancialBean.setCurrentDue1(((BigDecimal) aObj[3])
							.subtract((BigDecimal) aObj[9]));
				}*/
				
				BigDecimal subtotal=BigDecimal.ZERO;
				if(aObj[8]!=null){
					subtotal=JobUtil.floorFigureoverall(((BigDecimal) aObj[8]),2);
				}
				BigDecimal frieght=BigDecimal.ZERO;
				if(aObj[10]!=null){
					frieght=((BigDecimal) aObj[10]);
				}
				subtotal=subtotal.add(frieght);
				BigDecimal taxAmount=BigDecimal.ZERO;
				if(aObj[11]!=null){
					taxAmount=((BigDecimal) aObj[11]);
				}
				BigDecimal invoicetotal=subtotal.add(taxAmount);
				aJobFinancialBean.setInvoiceTotal(invoicetotal);
				
				aJobFinancialBean.setSubTotal(subtotal);
				aJobFinancialBean.setAppliedAmount((BigDecimal) aObj[9]);
				aJobFinancialBean.setFreight((BigDecimal) aObj[10]);
				aJobFinancialBean.setPaidAmt((BigDecimal) aObj[13]);
				
				Integer days=(Integer) aObj[12];
				//if(aJobFinancialBean.getDatePaid()==null){

				if(aJobFinancialBean.getAppliedAmount().compareTo(BigDecimal.ZERO) != 0){
				if(days<=30){
					if(aJobFinancialBean.getAppliedAmount().compareTo(BigDecimal.ZERO) > 0)
					aJobFinancialBean.setCurrentDue1((aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))).compareTo(BigDecimal.ZERO)>0?
							aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))
							:invoicetotal.subtract(aJobFinancialBean.getPaidAmt()));
					else
					aJobFinancialBean.setCurrentDue1(aJobFinancialBean.getAppliedAmount());
					
					aJobFinancialBean.setCurrentDue1withtax(aJobFinancialBean.getAppliedAmount());
				}else if(days>30 && days<=60){
					if(aJobFinancialBean.getAppliedAmount().compareTo(BigDecimal.ZERO) > 0)
					aJobFinancialBean.setThirtyDays((aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))).compareTo(BigDecimal.ZERO)>0?
							aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))
							:invoicetotal.subtract(aJobFinancialBean.getPaidAmt()));
					else
						aJobFinancialBean.setThirtyDays(aJobFinancialBean.getAppliedAmount());
					
					aJobFinancialBean.setThirtyDayswithtax(aJobFinancialBean.getAppliedAmount());
				}else if(days>60 && days<=90){
					
					if(aJobFinancialBean.getAppliedAmount().compareTo(BigDecimal.ZERO) > 0)
					aJobFinancialBean.setSixtyDays((aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))).compareTo(BigDecimal.ZERO)>0?
							aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))
							:invoicetotal.subtract(aJobFinancialBean.getPaidAmt()));
					else
						aJobFinancialBean.setSixtyDays(aJobFinancialBean.getAppliedAmount());
					
					aJobFinancialBean.setSixtyDayswithtax(aJobFinancialBean.getAppliedAmount());
				}else{
					
					System.out.println((aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))).compareTo(BigDecimal.ZERO)>0?
							aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))
							:invoicetotal.subtract(aJobFinancialBean.getPaidAmt()));
					
					if(aJobFinancialBean.getAppliedAmount().compareTo(BigDecimal.ZERO) > 0)
					aJobFinancialBean.setNinetyDays((aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))).compareTo(BigDecimal.ZERO)>0?
							aJobFinancialBean.getSubTotal().subtract(aJobFinancialBean.getPaidAmt().subtract(taxAmount))
							:invoicetotal.subtract(aJobFinancialBean.getPaidAmt()));
					else
						aJobFinancialBean.setNinetyDays(aJobFinancialBean.getAppliedAmount());
					
					aJobFinancialBean.setNinetyDayswithtax(aJobFinancialBean.getAppliedAmount());
				}
				
				}
				
				
				
				aFinancialList.add(aJobFinancialBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aFinancialQry=null;
		}
		return aFinancialList;
	}

	@Override
	public List<Cuinvoice> getCuInvoiceDetails(String theJobNumber)
			throws JobException {
		String aSummaryQry = "SELECT SUM(cuInvoice.taxtotal), "
				+ " SUM(cuInvoice.subtotal),"
				+ " SUM(cuInvoice.invoiceamount),"
				+ " SUM(cuInvoice.appliedamount) "
				+ " FROM joMaster JOIN joReleaseDetail ON joMaster.joMasterid = joReleaseDetail.joMasterid "
				+ " JOIN cuInvoice ON  joReleaseDetail.joReleaseDetailid = cuInvoice.joReleaseDetailid "
				+ " WHERE jobnumber = '" + theJobNumber + "'";
		Session aSession = null;
		ArrayList<Cuinvoice> aFinancialList = new ArrayList<Cuinvoice>();
		try {
			Cuinvoice aCuinvoice = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSummaryQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuinvoice = new Cuinvoice();
				Object[] aObj = (Object[]) aIterator.next();
				aCuinvoice.setTaxTotal((BigDecimal) aObj[0]);
				aCuinvoice.setSubtotal((BigDecimal) aObj[1]);
				aCuinvoice.setInvoiceAmount((BigDecimal) aObj[2]);
				aCuinvoice.setAppliedAmount((BigDecimal) aObj[3]);
				aFinancialList.add(aCuinvoice);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSummaryQry=null;
		}
		return aFinancialList;
	}

	@Override
	public BigDecimal getJobChangeAmount(String theJobNumber)
			throws JobException {
		String aSummaryQry = "SELECT sum(changeamount)"
				+ " FROM joChange join joMaster on joChange.joMasterid= joMaster.joMasterid "
				+ " where jobnumber= '" + theJobNumber + "'";
		Session aSession = null;
		BigDecimal aChangeAmount = null;
		try {
			itsLogger.info("getJobChangeAmount()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSummaryQry);
			List<?> aList = aQuery.list();
			aChangeAmount = (BigDecimal) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getJobChangeAmount()=[Connection Closed]");
			aSummaryQry=null;
		}
		return aChangeAmount;
	}
	
	@Override
	public BigDecimal saveCommissionAmount(Integer joReleaseId, BigDecimal commissionAmount) throws JobException {
		JoRelease aJoRelease = null;
		BigDecimal commissionAmounts = new BigDecimal("0.0000");
		if(commissionAmount != null)
		{}else
			commissionAmount = new BigDecimal(0.0);
		String aSummaryQry = "UPDATE joRelease SET CommissionAmount ="+commissionAmount+" WHERE joReleaseID = "+joReleaseId;
		itsLogger.info(" aSummaryQry = "+aSummaryQry);
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aSession.createSQLQuery(aSummaryQry).executeUpdate();
			aTransaction.commit();
			if(joReleaseId>0){
				aJoRelease = (JoRelease) aSession.get(JoRelease.class,joReleaseId);
			}
			if(aJoRelease!=null)
			{
				commissionAmounts = aJoRelease.getCommissionAmount();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSummaryQry=null;
		}
		return commissionAmounts;
	}
	@Override
	public JoRelease getCommissionAmount(Integer joReleaseId)throws JobException {
		JoRelease aJoRelease = null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(joReleaseId>0){
				aJoRelease = (JoRelease) aSession.get(JoRelease.class,joReleaseId);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoRelease;
	}

	@Override
	public Integer saveCommissionInvoice(Vecommdetail aVecommdetail,Integer joMasterID, Integer joReleaseID) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		JoReleaseDetail aJoreleaseDetail = new JoReleaseDetail();
		Vecommdetail theVecommdetail = null;
		Integer joReleaseDetailID = null;
		Integer veCommdetailID = null;
		aSession = itsSessionFactory.openSession();
		try {
		if(aVecommdetail !=null){
			
			if(aVecommdetail.getVeCommDetailId() != null && aVecommdetail.getVeCommDetailId()>0){
				theVecommdetail = (Vecommdetail) aSession.get(Vecommdetail.class,aVecommdetail.getVeCommDetailId());
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				theVecommdetail.setBillAmount(aVecommdetail.getBillAmount());
				theVecommdetail.setCommAmount(aVecommdetail.getCommAmount());
				theVecommdetail.setInvoiceNumber(aVecommdetail.getInvoiceNumber());
				theVecommdetail.setShipDate(aVecommdetail.getShipDate());
				theVecommdetail.setTrackingNumber(aVecommdetail.getTrackingNumber());
				theVecommdetail.setVeShipViaId(aVecommdetail.getVeShipViaId());
				theVecommdetail.setCommDate(aVecommdetail.getCommDate());
				aSession.update(theVecommdetail);
				aTransaction.commit();
				itsLogger.info("VeCommDetailId::"+aVecommdetail.getVeCommDetailId());		
			
			if(theVecommdetail.getJoReleaseDetailId()>0){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				JoReleaseDetail theJoReleaseDetail = (JoReleaseDetail) aSession.get(JoReleaseDetail.class,theVecommdetail.getJoReleaseDetailId());
				theJoReleaseDetail.setShipDate(aVecommdetail.getShipDate());
				theJoReleaseDetail.setVendorInvoiceAmount(aVecommdetail.getBillAmount());
				theJoReleaseDetail.setCustomerInvoiceDate(aVecommdetail.getCommDate());
				theJoReleaseDetail.setCustomerInvoiceAmount(aVecommdetail.getCommAmount());
				aSession.update(theJoReleaseDetail);
				aTransaction.commit();
			}
		}
			else{
					aJoreleaseDetail.setJoMasterId(joMasterID);
					aJoreleaseDetail.setJoReleaseId(joReleaseID);
					aSession = itsSessionFactory.openSession();
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					joReleaseDetailID = (Integer) aSession.save(aJoreleaseDetail);
					aTransaction.commit();
					itsLogger.info("JOReleaseDetailID::"+joReleaseDetailID);
					if(joReleaseDetailID > 0){
						aTransaction = aSession.beginTransaction();
						aTransaction.begin();
						aVecommdetail.setJoReleaseDetailId(joReleaseDetailID);
						veCommdetailID = (Integer) aSession.save(aVecommdetail);
						aTransaction.commit();
					
					if(veCommdetailID>0){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					JoReleaseDetail theJoReleaseDetail = (JoReleaseDetail) aSession.get(JoReleaseDetail.class,joReleaseDetailID);
					theJoReleaseDetail.setShipDate(aVecommdetail.getShipDate());
					theJoReleaseDetail.setVendorInvoiceAmount(aVecommdetail.getBillAmount());
					theJoReleaseDetail.setCustomerInvoiceDate(aVecommdetail.getCommDate());
					theJoReleaseDetail.setCustomerInvoiceAmount(aVecommdetail.getCommAmount());
					aSession.update(theJoReleaseDetail);
					aTransaction.commit();
			}
		}
		}
		}
		}
		catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return joReleaseDetailID;
	}
	
	@Override
	public String getCustPONo(String theJobNumber) throws JobException {
		String aCustomerPONo = "SELECT cuInvoice.CustomerPONumber"
				+ " FROM joMaster "
				+ " JOIN joReleaseDetail  "
				+ " ON joMaster.joMasterid=joReleaseDetail.joMasterid  "
				+ " JOIN cuInvoice ON joReleaseDetail.joReleaseDetailid=cuInvoice.joReleaseDetailid  "
				+ " WHERE jobnumber = '" + theJobNumber + "'";
		Session aSession = null;
		String aCustPONo = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerPONo);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty())
				aCustPONo = (String) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerPONo=null;
		}
		return aCustPONo;
	}

	@Override
	public int UpdateJobStatus(String theDateType, Date theDate,
			Integer aJoMasterId, Integer theJobstatus) throws JobException {
		itsLogger.debug("Query has to get jobNumber and date");
		Session aSession = itsSessionFactory.openSession();
		Date aDate = null;
		try {
			
			if(aJoMasterId!=0)
			{
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			
			Jomaster aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					aJoMasterId);
			if (theDateType.equals("BookedDate")) {
				aJoMaster.setBookedDate(theDate);
				aJoMaster.setClosedDate(aDate);
				aJoMaster.setChangedOn(new Date());
			} else if (theDateType.equals("ClosedDate")) {
				aJoMaster.setClosedDate(theDate);
			} else if (theDateType.equals("Submitted")) {
				aJoMaster.setClosedDate(aDate);
			} else if (theDateType.isEmpty()) {
				aJoMaster.setChangedOn(new Date());
				aJoMaster.setBookedDate(aDate);
				aJoMaster.setClosedDate(aDate);
			}
			aJoMaster.setJobStatus(theJobstatus);
			aSession.update(aJoMaster);
			aTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return 0;
	}

	@Override
	public int getjobStatusId(String theStatus) throws JobException {
		String aJobStatus = "SELECT joStatusID FROM joStatus where JobStatus='"
				+ theStatus + "'";
		Session aSession = null;
		int aStatus = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobStatus);
			List<?> aList = aQuery.list();
			aStatus = (Short) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aStatus;
	}

	@Override
	public Date getQuoteDate(Integer bidderId) throws JobException {
		String aJobStatus = "SELECT QuoteDate FROM joBidder WHERE joBidderID="
				+ bidderId;
		Session aSession = null;
		Date quoteDate = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobStatus);
			List<?> aList = aQuery.list();
			quoteDate = (Date) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobStatus=null;
		}
		return quoteDate;
	}

	@Override
	public int addLastOpened(JobHistory theJobHistory) throws JobException {
		Session aSession = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			// Save
			aSession.save(theJobHistory);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return 0;
	}

	@Override
	public ArrayList<JobHistory> getJobsHistory() throws JobException {
		String aSelectHistoryQry = "SELECT jobHis.jobNumber, jobHis.jobName, jobHis.jobOpenedDate , jobHis.BidDate,jobHis.joMasterID "
				+ "FROM jobHistory jobHis "
				+ "JOIN (SELECT MAX(jobHistoryID) as jobHisID FROM jobHistory GROUP BY jobNumber) jh "
				+ "ON jobHis.jobHistoryID = jh.jobHisID "
				+ "ORDER BY jobHis.jobHistoryID DESC LIMIT 0, 15;";
		Session aSession = null;
		List<JobHistory> aQueryList = null;
		try {
			JobHistory aJobHistory = null;
			aSession = itsSessionFactory.openSession();
			aQueryList = new ArrayList<JobHistory>();
			Query aQuery = aSession.createSQLQuery(aSelectHistoryQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobHistory = new JobHistory();
				Object[] aObj = (Object[]) aIterator.next();
				// aJobHistory.setJobHistoryID((Integer)aObj[0]);
				aJobHistory.setJobNumber((String) aObj[0]);
				aJobHistory.setJobName((String) aObj[1]);
				aJobHistory.setJobOpenedDateStr(DateFormatUtils.format(
						(Date) aObj[2], "MM/dd/yyyy "));
				if (aObj[3] != null && aObj[3].toString() != "") {
					aJobHistory.setJobBidDateStr(DateFormatUtils.format(
							(Date) aObj[3], "MM/dd/yyyy "));
				} else {
					aJobHistory.setJobBidDateStr("");
				}
				aJobHistory.setJoMasterID((Integer)aObj[4]);
				aQueryList.add(aJobHistory);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectHistoryQry=null;
		}
		return (ArrayList<JobHistory>) aQueryList;
	}

	

	@Override
	public Jomaster getJoMasterIdJobstatus(String theJobNumber)
			throws JobException {
		Jomaster aJoMaster = null;
		String aSelectQry = "SELECT joMasterID,jobStatus FROM joMaster WHERE jobNumber = '"
				+ theJobNumber + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aJoMaster = new Jomaster();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aJoMaster.setJoMasterId((Integer) aObj[0]);
				/** joMasterId */
				aJoMaster.setJoStatus((Short) aObj[1]);
				/** jobStatus */
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJoMaster;
	}

	@Override
	public Jomaster getJoMasterByJobNumber(String theJobNumber)
			throws JobException {
		String aSelectQry = new StringBuilder(
				"FROM Jomaster WHERE jobNumber = '").append(theJobNumber)
				.append("'").toString();
		Session aSession = null;
		Jomaster joMaster = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aSelectQry);
			List aList = aQuery.list();
			if (!aList.isEmpty())
				joMaster = (Jomaster) aList.get(0);

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return joMaster;
	}

	@Override
	public ArrayList<AutoCompleteBean> getBidderList(String theBidderSearchStr)
			throws JobException {
		String aJobSelectQry = "SELECT rxMasterID, Name FROM rxMaster"
				+ " WHERE (IsCustomer=1 or isCategory7=1) AND Name like " + "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theBidderSearchStr) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getContactListBaseBidderList(
			Integer theRxMasterId) throws JobException {
		itsLogger.debug("Retrieving all represent names");
		String aJobSelectQry = "SELECT rxContactID, rxMasterID, FirstName, LastName FROM rxContact WHERE rxMasterId ="
				+ theRxMasterId;
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				String aFirstName = (String) aObj[2];
				String aLastName = (String) aObj[3];
				aUserbean.setLabel(aFirstName + " " + aLastName);
				aUserbean.setValue(aFirstName + " " + aLastName);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean addQuoteBidder(Jobidder theJobidder) throws JobException {
		Session aJoBidderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoBidderSession.beginTransaction();
			aTransaction.begin();
			aJoBidderSession.save(theJobidder);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoBidderSession.flush();
			aJoBidderSession.close();
		}
		return false;
	}

	@Override
	public boolean updateQuoteBidder(Jobidder theJobidder) throws JobException {
		Session aJoBidderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoBidderSession.beginTransaction();
			aTransaction.begin();
			Jobidder aExistingBidder = (Jobidder) aJoBidderSession.get(
					Jobidder.class, theJobidder.getJoBidderId());
			aExistingBidder.setCuMasterTypeId(theJobidder.getCuMasterTypeId());
			aExistingBidder.setJoBidderId(theJobidder.getJoBidderId());
			aExistingBidder.setJoMasterId(theJobidder.getJoMasterId());
			aExistingBidder.setLowBid(theJobidder.isLowBid());
			// aExistingBidder.setQuoteDate(theJobidder.getQuoteDate());
			aExistingBidder.setQuoteRev(theJobidder.getQuoteRev());
			aExistingBidder.setRxMasterId(theJobidder.getRxMasterId());
			aExistingBidder.setRxContactId(theJobidder.getRxContactId());
			aExistingBidder.setUserLoginID(theJobidder.getUserLoginID());
			aJoBidderSession.update(aExistingBidder);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoBidderSession.flush();
			aJoBidderSession.close();
		}
		return false;
	}

	@Override
	public boolean deleteQuoteBidder(Jobidder theJobidder) throws JobException {
		Session aJoBidderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoBidderSession.beginTransaction();
			aTransaction.begin();
			aJoBidderSession.delete(theJobidder);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoBidderSession.flush();
			aJoBidderSession.close();
		}
		return false;
	}

	@Override
	public Integer getQuoteTypeByTypeId(String theQuoteType)
			throws JobException {
		Integer aQuoteTypeId = null;
		String aSelectQry = "SELECT cuMasterTypeID FROM cuMasterType WHERE  Code LIKE "
				+ "'%" + JobUtil.removeSpecialcharacterswithslash(theQuoteType) + "%'" + "";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aQuoteTypeId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aQuoteTypeId;
	}

	@Override
	public void getBidderContactList() throws JobException {
		itsLogger.debug("Retrieving all represent names");
		StringBuffer aJobSelectQry = new StringBuffer(
				"SELECT rxContactID, rxMasterID, FirstName, LastName FROM rxContact WHERE rxMasterId ='954'");
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			StringBuffer aStatus = new StringBuffer();
			aStatus = aStatus.append("<select>");
			aStatus = aStatus.append("<option value = '" + itsSelect
					+ "'>  </option>");
			while (aIterator.hasNext()) {
				Integer aId = null;
				String aFirstName = null;
				String aLastName = null;
				Object[] aObj = (Object[]) aIterator.next();
				if ((Integer) aObj[0] != null) {
					aId = (Integer) aObj[0];
				}
				if ((String) aObj[2] != null) {
					aFirstName = (String) aObj[2];
				}
				if ((String) aObj[3] != null) {
					aLastName = (String) aObj[3];
				}
				String aName = aFirstName + " " + aLastName;
				aStatus = aStatus.append("<option value = '" + aId + "'>"
						+ aName + "</option>");
				aStatus = aStatus.append("</select>");
				aFirstName=null;aLastName=null;aName=null;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
	}

	@Override
	public Josubmittalheader getSingleSubmittalDetails(String theJobNumber,Integer aJoMasterId)
			throws JobException {
		Session aSession = null;
		Josubmittalheader ajoJosubmittalheader = null;
		Josubmittalheader aJosubmittal = new Josubmittalheader();
		try {
			aSession = itsSessionFactory.openSession();
			aJosubmittal.setJoMasterId(aJoMasterId);
			int aJoSubmittalID = getJoMasterIdByJoSubmittal(aJoMasterId);
			if (aJoSubmittalID != 0)
				ajoJosubmittalheader = (Josubmittalheader) aSession.get(
						Josubmittalheader.class, aJoSubmittalID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return ajoJosubmittalheader;
	}

	private int getJoMasterIdByJoSubmittal(int theJoMasterId)
			throws JobException {
		Integer aJoSubmittalId = 0;
		String aSelectQry = "SELECT joSubmittalHeaderID FROM joSubmittalHeader WHERE joMasterID = '"
				+ theJoMasterId + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList =aQuery.list();
			if (!(aList.isEmpty())) {
				aJoSubmittalId = (Integer) aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoSubmittalId;
	}

	@Override
	public String getSubmittalName(Integer theSubmittalById)
			throws JobException {
		String aSubmittalName = "SELECT Initials " + "FROM tsUserLogin "
				+ "WHERE UserLoginId = " + theSubmittalById
				+ "  AND LoginName != 'admin'";
		Session aSession = null;
		String aSalesRep = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalName);
			List<?> aList = aQuery.list();
			aSalesRep = (String) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalName=null ;
		}
		return aSalesRep;
	}

	@Override
	public String getSignedName(Integer theSignedById) throws JobException {
		String aSignedName = "SELECT Initials " + "FROM tsUserLogin "
				+ "WHERE UserLoginId = " + theSignedById
				+ "  AND LoginName != 'admin'";
		Session aSession = null;
		String aSalesRep = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSignedName);
			List<?> aList = aQuery.list();
			aSalesRep = (String) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSignedName=null;
		}
		return aSalesRep;
	}

	@Override
	public List<?> getSubmittalProductList(String theSubmittalID)
			throws JobException {
		itsLogger.debug("Query to Select All SubmittalProductList");
		String aSubmittalQry = "SELECT DISTINCT "
				+ " jd.product, "
				+ " jd.quantity, "
				+ " jd.Paragraph, "
				+ " jd.rxManufacturerID, "
				+ " vm.Description, "
				+ " jd.estimatedcost, "
				+ " sth.Description, "
				+ " jd.joSubmittalDetailId, "
				+ " jd.status, "
				+ " jd.Cost, "
				+ " jd.joSchedTempHeaderID, "
				+ " jd.joSubmittalHeaderID "
				+ " FROM joSubmittalDetail jd "
				+ " LEFT JOIN veFactory vm ON vm.veFactoryID = jd.rxManufacturerID "
				+ " LEFT JOIN joSchedTempHeader sth ON sth.joSchedTempHeaderID = jd.joSchedTempHeaderID "
				+ " WHERE jd.joSubmittalHeaderID = '" + theSubmittalID + "'";
		Session aSession = null;
		ArrayList<JobSubmittalBean> aQryList = new ArrayList<JobSubmittalBean>();
		try {
			JobSubmittalBean aJobSubmittalBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobSubmittalBean = new JobSubmittalBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobSubmittalBean.setProduct((String) aObj[0]);
				aJobSubmittalBean.setquantity((String) aObj[1]);
				aJobSubmittalBean.setReleased((String) aObj[2]);
				aJobSubmittalBean.setManufactureID((Integer) aObj[3]);
				aJobSubmittalBean.setManufacture((String) aObj[4]);
				aJobSubmittalBean.setEstimatecost((BigDecimal) aObj[5]);
				aJobSubmittalBean.setSchedule((String) aObj[6]);
				aJobSubmittalBean.setJoSubmittalID((Integer) aObj[7]);
				aJobSubmittalBean.setStatus((Integer) aObj[8]);
				aJobSubmittalBean.setCost((BigDecimal) aObj[9]);
				aJobSubmittalBean.setJoSchdTempHeaderID((Integer) aObj[10]);
				aJobSubmittalBean.setJoSubmittalHeaderID((Integer) aObj[11]);
				aQryList.add(aJobSubmittalBean);
			}
		} catch (Exception e) {
			itsLogger.error(
					"Exception while getting the submittal list: "
							+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry=null;
		}
		return aQryList;
	}

	@Override
	public List<?> getSubmittalSheduledList(Integer theSubmittalID,
			String theParameters) throws JobException {
		itsLogger.debug("Query to Select All SubmittalSheduledList");
		String aSubmittalQry = "SELECT "
				+ theParameters
				+ " FROM joScheduleDetail jsd join  joScheduleModel jsm on jsd.joScheduleModelID = jsm.joScheduleModelID WHERE jsd.joSubmittalDetailID = :joSubmittalDetailId";
		Session aSession = null;
		List<Object> aQryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			aQuery.setParameter("joSubmittalDetailId", theSubmittalID);
			aQryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(
					"Exception while getting the submittal list: "
							+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry=null;
		}
		return aQryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getScheduleList(String theScheduleText)
			throws JobException {
		String salesselectQry = "SELECT joSchedTempHeaderID, Description FROM joSchedTempHeader "
				+ "WHERE Description like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theScheduleText)
				+ "%'"
				+ " ORDER BY Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getProductList(String theProductText)
			throws JobException {
		String salesselectQry = "SELECT joSchedTempHeaderID, Description FROM joSchedTempHeader "
				+ "WHERE Description like '%"
				+ JobUtil.removeSpecialcharacterswithslash(theProductText)
				+ "%' ORDER BY Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getManufactureList(
			String theManufactureText) throws JobException {
		String salesselectQry = "SELECT rxMasterID, Description FROM veFactory "
				+ "WHERE Description like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theManufactureText)
				+ "%'"
				+ " ORDER BY Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Joschedtempheader getProductName(Integer theProductName)
			throws JobException {
		Session aSession = null;
		Joschedtempheader aJoschedtempheader = null;
		Joschedtempheader aJoschedtempheader2 = new Joschedtempheader();
		try {
			aSession = itsSessionFactory.openSession();
			aJoschedtempheader2.setJoSchedTempHeaderId(theProductName);
			aJoschedtempheader = (Joschedtempheader) aSession.get(
					Joschedtempheader.class,
					aJoschedtempheader2.getJoSchedTempHeaderId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoschedtempheader;
	}

	@Override
	public String getManufactureName(Integer aManufactureId)
			throws JobException {
		String signedName = "SELECT Manufacturer FROM veMaster "
				+ "WHERE veMasterID = " + aManufactureId + "";
		Session aSession = null;
		String aSalesRep = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(signedName);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				aSalesRep = (String) aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			signedName=null;
		}
		return aSalesRep;
	}

	@Override
	public Integer getManufactureId(String theManufactureName)
			throws JobException {
		String signedName = "SELECT rxManufacturerID FROM joSchedTempHeader WHERE joSchedTempHeaderID = "
				+ theManufactureName + "";
		Session aSession = null;
		Integer aSalesRep = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(signedName);
			List<?> aList = aQuery.list();
			aSalesRep = (Integer) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			signedName=null;
		}
		return aSalesRep;
	}

	@Override
	public boolean addSubmittal_Product(Josubmittaldetail theJoSubmittalDetails)
			throws JobException {
		Session aJosubmittalheaderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJosubmittalheaderSession.beginTransaction();
			aTransaction.begin();
			aJosubmittalheaderSession.save(theJoSubmittalDetails);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJosubmittalheaderSession.flush();
			aJosubmittalheaderSession.close();
		}
		return false;
	}

	@Override
	public boolean updateSubmittal_Product(
			Josubmittaldetail theJosubmittaldetail) throws JobException {
		Session aJosubmittalheaderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJosubmittalheaderSession.beginTransaction();
			aTransaction.begin();
			Josubmittaldetail aJosubmittaldetail = (Josubmittaldetail) aJosubmittalheaderSession
					.get(Josubmittaldetail.class,
							theJosubmittaldetail.getJoSubmittalDetailId());
			aJosubmittaldetail.setQuantity(theJosubmittaldetail.getQuantity());
			aJosubmittaldetail
					.setParagraph(theJosubmittaldetail.getParagraph());
			aJosubmittaldetail.setRxManufacturerId(theJosubmittaldetail
					.getRxManufacturerId());
			aJosubmittaldetail.setEstimatedCost(theJosubmittaldetail
					.getEstimatedCost());
			aJosubmittaldetail.setCost(theJosubmittaldetail.getCost());
			aJosubmittaldetail.setProduct(theJosubmittaldetail.getProduct());
			aJosubmittaldetail.setJoSchedTempHeaderId(theJosubmittaldetail
					.getJoSchedTempHeaderId());
			aJosubmittaldetail.setJoSubmittalHeaderId(theJosubmittaldetail
					.getJoSubmittalHeaderId());
			aJosubmittaldetail.setStatus(theJosubmittaldetail.getStatus());
			aJosubmittaldetail.setJoSubmittalDetailId(theJosubmittaldetail
					.getJoSubmittalDetailId());
			aJosubmittalheaderSession.update(aJosubmittaldetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJosubmittalheaderSession.flush();
			aJosubmittalheaderSession.close();
		}
		return false;
	}

	@Override
	public boolean deleteSubmittal_Product(
			Josubmittaldetail theJosubmittaldetail) throws JobException {
		Session aJosubmittaldetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Josubmittaldetail aJosubmittaldetail = null;
		try {
			aTransaction = aJosubmittaldetailSession.beginTransaction();
			aTransaction.begin();
			aJosubmittaldetail = (Josubmittaldetail) aJosubmittaldetailSession
					.get(Josubmittaldetail.class,
							theJosubmittaldetail.getJoSubmittalDetailId());
			aJosubmittaldetailSession.delete(aJosubmittaldetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJosubmittaldetailSession.flush();
			aJosubmittaldetailSession.close();
		}
		return false;
	}

	@Override
	public ArrayList<AutoCompleteBean> getModelNoList(String theModelNo)
			throws JobException {
		String salesselectQry = "SELECT joScheduleModelID, ModelNo FROM joScheduleModel WHERE ModelNo like "
				+ "'%" + JobUtil.removeSpecialcharacterswithslash(theModelNo) + "%'" + " ORDER BY ModelNo ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Joschedulemodel geScheduledModel(Integer theScheduledModelID)
			throws JobException {
		Session aSession = null;
		Joschedulemodel aJoschedulemodel = null;
		Joschedulemodel aJoschedule = new Joschedulemodel();
		try {
			aSession = itsSessionFactory.openSession();
			aJoschedule.setJoScheduleModelId(theScheduledModelID);
			aJoschedulemodel = (Joschedulemodel) aSession.get(
					Joschedulemodel.class, aJoschedule.getJoScheduleModelId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoschedulemodel;
	}

	@Override
	public Joscheduledetail geScheduledTag(Integer theScheduledModelID)
			throws JobException {
		Session aSession = null;
		Joscheduledetail ajoJoscheduledetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			int aHeaderId = setJoScheduleModelIdInHeaderModelId(theScheduledModelID);
			ajoJoscheduledetail = (Joscheduledetail) aSession.get(
					Joscheduledetail.class, aHeaderId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return ajoJoscheduledetail;
	}

	private int setJoScheduleModelIdInHeaderModelId(Integer theScheduledModelID)
			throws JobException {
		Integer aHeaderId = null;
		String aSelectQry = "SELECT joScheduleDetailID FROM joScheduleDetail where joScheduleModelID = '"
				+ theScheduledModelID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aHeaderId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aHeaderId;
	}

	@Override
	public boolean addSubmittalSchedule(Joscheduledetail theJoscheduledetail)
			throws JobException {
		Session aJoscheduledetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoscheduledetailSession.beginTransaction();
			aTransaction.begin();
			aJoscheduledetailSession.save(theJoscheduledetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoscheduledetailSession.flush();
			aJoscheduledetailSession.close();
		}
		return false;
	}

	@Override
	public boolean deleteSubmittalSchedule(Integer theJoScheduleDetailID)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Joscheduledetail aJoscheduledetail = new Joscheduledetail();
		try {
			aJoscheduledetail = (Joscheduledetail) aSession.get(
					Joscheduledetail.class, theJoScheduleDetailID);
			aSession.delete(aJoscheduledetail);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public Boolean addSubmittal(Josubmittalheader aJosubmittalheader)
			throws JobException {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(aJosubmittalheader);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public int getSubmittalHeaderIDByJoMaster(int theJoMasterID)
			throws JobException {
		Integer aSubmittalHeaderId = null;
		String aSelectQry = "SELECT joSubmittalHeaderID FROM joSubmittalHeader WHERE joMasterID = '"
				+ theJoMasterID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aSubmittalHeaderId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aSubmittalHeaderId;
	}

	@Override
	public int updateSubmittal(Josubmittalheader theJosubmittalheader)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Josubmittalheader aJosubmittalheader = (Josubmittalheader) aSession
					.get(Josubmittalheader.class,
							theJosubmittalheader.getJoSubmittalHeaderId());
			aJosubmittalheader.setSubmittalDate(theJosubmittalheader
					.getSubmittalDate());
			aJosubmittalheader.setCopies(theJosubmittalheader.getCopies());
			aJosubmittalheader.setSubmittalById(theJosubmittalheader
					.getSubmittalById());
			aJosubmittalheader.setRevision(theJosubmittalheader.getRevision());
			aJosubmittalheader.setAddendum(theJosubmittalheader.getAddendum());
			aJosubmittalheader.setPlanDate(theJosubmittalheader.getPlanDate());
			aJosubmittalheader.setSignedById(theJosubmittalheader
					.getSignedById());
			aJosubmittalheader.setRemarkNote(theJosubmittalheader
					.getRemarkNote());
			aJosubmittalheader.setRevisionNote(theJosubmittalheader
					.getRevisionNote());
			aJosubmittalheader.setJoMasterId(theJosubmittalheader
					.getJoMasterId());
			aJosubmittalheader.setCreatedById(theJosubmittalheader
					.getCreatedById());
			aJosubmittalheader
					.setCreatedOn(theJosubmittalheader.getCreatedOn());
			aJosubmittalheader.setJoSubmittalHeaderId(theJosubmittalheader
					.getJoSubmittalHeaderId());
			aSession.update(aJosubmittalheader);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return 0;
	}

	@Override
	public Integer addJobJournalEntry(Jojournal theJobJournal)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(
					TsUserLogin.class, theJobJournal.getJournalById());
			theJobJournal.setJournalByName(aTsUserLogin.getInitials());
			theJobJournal.setJournalDate(new Date());
			aSession.save(theJobJournal);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}

	@Override
	public Integer editJobJournalEntry(Jojournal theJobJournal)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(
					TsUserLogin.class, theJobJournal.getJournalById());
			theJobJournal.setJournalByName(aTsUserLogin.getInitials());
			theJobJournal.setJournalDate(new Date());
			aSession.update(theJobJournal);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}

	@Override
	public Integer deleteJobJournalEntry(Jojournal theJobJournal)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.delete(theJobJournal);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return null;
	}

	@Override
	public ArrayList<AutoCompleteBean> getOwnerNameList(String theOwnerName)
			throws JobException {
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster WHERE IsCategory4 = 1 "
				+ "AND Name like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theOwnerName)
				+ "%'"
				+ " ORDER BY Name ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getBondAgentList(String theBondAgent)
			throws JobException {
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster WHERE IsCategory5 = 1 "
				+ "AND Name like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theBondAgent)
				+ "%'"
				+ " ORDER BY Name ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getFilterOwnerList(String theFilterName)
			throws JobException {
		String aJobSelectQry = "SELECT rxContactID, rxMasterID, FirstName, LastName FROM rxContact WHERE rxMasterId ="
				+ theFilterName;
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				String aFirstName = (String) aObj[2];
				String aLastName = (String) aObj[3];
				aUserbean.setLabel(aFirstName + " " + aLastName);
				aUserbean.setValue(aFirstName + " " + aLastName);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCustomerContactList(
			String theFilterName) throws JobException {
		itsLogger.debug("Retrieving getCustomerContactList");
		String aJobSelectQry = "SELECT rxContactID, rxMasterID, CONCAT(FirstName,' ',LastName) as contactName FROM rxContact "
				+ "WHERE CONCAT(FirstName,' ',LastName) like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theFilterName) + "%'" + "";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setManufactureID((Integer) aObj[1]);
				aUserbean.setLabel((String) aObj[2]);
				aUserbean.setValue((String) aObj[2]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<Rxaddress> getRxAddress(String theFilterName)
			throws JobException {
		String aJobSelectQry = "SELECT rxaddressId, rxMasterId, address1, address2, city, state, zip FROM rxAddress WHERE rxMasterId ="
				+ theFilterName;
		Session aSession = null;
		ArrayList<Rxaddress> aQueryList = new ArrayList<Rxaddress>();
		try {
			Rxaddress aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new Rxaddress();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setRxAddressId((Integer) aObj[0]);
				aUserbean.setRxMasterId((Integer) aObj[1]);
				aUserbean.setAddress1((String) aObj[2]);
				aUserbean.setAddress2((String) aObj[3]);
				aUserbean.setCity((String) aObj[4]);
				aUserbean.setState((String) aObj[5]);
				aUserbean.setZip((String) aObj[6]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Rxaddress getRxAddressByRxAddressID(Integer theRxAddressID)
			throws JobException {
		Session aSession = null;
		Rxaddress aRxAddress = null;
		try {
			if (theRxAddressID != null) {
				theRxAddressID = getRxAddressID(theRxAddressID);
				aSession = itsSessionFactory.openSession();
				aRxAddress = (Rxaddress) aSession.get(Rxaddress.class,
						theRxAddressID);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxAddress;
	}

	@Override
	public Rxaddress getRxAddress(Integer theRxAddressID) throws JobException {
		Session aSession = null;
		Rxaddress aRxAddress = new Rxaddress();
		try {
			if (theRxAddressID != null)
			theRxAddressID = getRxAddressID(theRxAddressID);
			
			if (theRxAddressID != null) {
				aSession = itsSessionFactory.openSession();
				aRxAddress = (Rxaddress) aSession.get(Rxaddress.class, theRxAddressID);
				
				if(aRxAddress!=null)
				{
						if (aRxAddress.getCity() != null
								&& !aRxAddress.getCity().equalsIgnoreCase("")) {
							String aCity = aRxAddress.getCity().trim();
							aRxAddress.setCity(aCity);
						} else {
							aRxAddress.setCity("");
						}
						if (aRxAddress.getAddress1() == null) {
							aRxAddress.setCity("");
						}
						if (aRxAddress.getAddress2() == null) {
							aRxAddress.setAddress2("");
						}
						if (aRxAddress.getZip() == null) {
							aRxAddress.setZip("");
						}
						if (aRxAddress.getState() == null) {
							aRxAddress.setState("");
						}
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			if(aSession!=null){
			aSession.flush();
			aSession.close();
			}
		}
		return aRxAddress;
	}
	
	public Integer getRxAddressID(Integer theRxAddressID) throws JobException {
		Integer aRxAddressID = null;
		String aSelectQry = "SELECT rxAddressID FROM rxAddress WHERE rxMasterID ="
				+ theRxAddressID;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (aList.size()>0) {
				aRxAddressID = (Integer) aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aRxAddressID;
	}

	@Override
	public JobPurchaseOrderBean getPurchaseDetails(Integer theVepoID,
			String theJobNumber) throws JobException {
		String aPurcheaseDeatils = "SELECT ve.vePOID," + " ve.CreatedByID,"
				+ " ve.joReleaseID," + " ve.veShipViaID,"
				+ " ve.veFreightChargesID," + " ve.OrderedByID,"
				+ " ve.PONumber," + " ve.CustomerPONumber," + " ve.OrderDate,"
				+ " ve.DateWanted," + " ve.Tag," + " ve.SpecialInstructions,"
				+ " ve.SubTotal," + " ve.TaxTotal," + " ve.Freight,"
				+ " ve.TaxRate," + " ve.rxVendorContactID,"
				+ " ve.veFactoryID, " + " ve.billToIndex," + " ve.shipToIndex,"
				+ " ve.emailTimeStamp," + " ve.wantedOnOrBefore,"
				+ " ve.ShipToMode, " + " ve.qbPO, " + " ve.TransactionStatus , ve.rxShipToAddressID,ve.rxShipToID,ve.prWarehouseId "
				+ " from vePO ve where vePOId =" + theVepoID + ";";
		Session aSession = null;
		JobPurchaseOrderBean aJobPurchaseOrderBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPurcheaseDeatils);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobPurchaseOrderBean = new JobPurchaseOrderBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobPurchaseOrderBean.setVePoid((Integer) aObj[0]);
				if ((Integer) aObj[1] != null) {
					aJobPurchaseOrderBean.setCreatedById((Integer) aObj[1]);
				}
				aJobPurchaseOrderBean.setJoReleaseId((Integer) aObj[2]);
				aJobPurchaseOrderBean.setVeShipViaId((Integer) aObj[3]);
				aJobPurchaseOrderBean.setVeFreightChargesId((Integer) aObj[4]);
				if ((Integer) aObj[5] != null) {
					aJobPurchaseOrderBean.setOrderedById((Integer) aObj[5]);
					aJobPurchaseOrderBean
							.setTsFullName(getUserfullName((Integer) aObj[5]));
				}
				if ((String) aObj[6] == null || (String) aObj[6] == " ") {
					String thePoNumber = getNewPONumber(theJobNumber,
							(Integer) aObj[0]);
					aJobPurchaseOrderBean.setPonumber(thePoNumber);
				} else {
					aJobPurchaseOrderBean.setPonumber((String) aObj[6]);
				}
				aJobPurchaseOrderBean.setCustomerPonumber((String) aObj[7]);
				aJobPurchaseOrderBean.setOrderDate((Date) aObj[8]);
				aJobPurchaseOrderBean.setDateWanted((String) aObj[9]);
				aJobPurchaseOrderBean.setTag((String) aObj[10]);
				aJobPurchaseOrderBean.setSpecialInstructions((String) aObj[11]);
				aJobPurchaseOrderBean.setSubtotal((BigDecimal) aObj[12]);
				aJobPurchaseOrderBean.setTaxTotal((BigDecimal) aObj[13]);
				aJobPurchaseOrderBean.setFreight((BigDecimal) aObj[14]);
				aJobPurchaseOrderBean.setTaxRate((BigDecimal) aObj[15]);
				aJobPurchaseOrderBean.setRxVendorContactId((Integer) aObj[16]);
				aJobPurchaseOrderBean.setVeFactoryId((Short) aObj[17]);
				aJobPurchaseOrderBean.setBillTo((Integer) aObj[18]);
				aJobPurchaseOrderBean.setShipTo((Integer) aObj[19]);
				aJobPurchaseOrderBean.setQbPO((String) aObj[23]);
				short transStatus = aObj[24] != null ? (Short) aObj[24] : 1;
				aJobPurchaseOrderBean.setTransactionStatus(Integer
						.valueOf(transStatus));
				SimpleDateFormat fromUser = new SimpleDateFormat(
						"MM/dd/yyyy hh:mm ");
				if (aObj[20] != null) {
					String aDateFormat = fromUser.format(aObj[20]);
					aJobPurchaseOrderBean.setEmailTimeStamp(aDateFormat);
				}
				aJobPurchaseOrderBean.setWantedOnOrBefore((Integer) aObj[21]);
				aJobPurchaseOrderBean.setShipToMode((Short) aObj[22]);
				aJobPurchaseOrderBean.setRxShipToAddressId((Integer) aObj[25]);
				aJobPurchaseOrderBean.setRxShipToId((Integer) aObj[26]);
				aJobPurchaseOrderBean.setPrWarehouseId((Integer) aObj[27]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPurcheaseDeatils=null;
		}
		return aJobPurchaseOrderBean;
	}

	@Override
	public JobSalesOrderBean getSalesOrderDetails(Integer theReleaseId)
			throws JobException {
		String Query = "SELECT * From cuSO WHERE joReleaseID =" + theReleaseId
				+ ";";
		Session aSession = null;
		JobSalesOrderBean jSOBean = new JobSalesOrderBean();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(Query);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				jSOBean.setCuSoid((Integer) aObj[0]);
				jSOBean.setCreatedById((Integer) aObj[1]);
				jSOBean.setCreatedOn((Date) aObj[2]);
				jSOBean.setChangedById((Integer) aObj[3]);
				jSOBean.setChangedOn((Date) aObj[4]);
				jSOBean.setJoReleaseId((Integer) aObj[6]);
				jSOBean.setRxCustomerId((Integer) aObj[7]);
				jSOBean.setRxBillToId((Integer) aObj[8]);
				jSOBean.setRxBillToAddressId((Integer) aObj[9]);
				jSOBean.setRxShipToId((Integer) aObj[10]);
				jSOBean.setRxShipToAddressId((Integer) aObj[11]);
				jSOBean.setVeShipViaId((Integer) aObj[12]);
				jSOBean.setSonumber((String) aObj[18]);
				jSOBean.setMailTimeStamp((String) aObj[53]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			Query=null;
		}
		return jSOBean;
	}

	private String getUserfullName(Integer theTsLoginID) {
		String salesselectQry = "SELECT FullName FROM tsUserLogin "
				+ "WHERE UserLoginID =" + theTsLoginID
				+ "  AND LoginName != 'admin'";
		Session aSession = null;
		String tsFullName = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			List<?> aList =aQuery.list();
			if (!aList.isEmpty()) {
				tsFullName = (String) aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return tsFullName;
	}

	@Override
	public ArrayList<AutoCompleteBean> getUserName(String theUserName)
			throws JobException {
		String aSalesselectQry = "SELECT UserLoginId, Initials, FullName "
				+ "FROM tsUserLogin " + "WHERE FullName like " + "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theUserName) + "%'"
				+ "  AND LoginName != 'admin' AND Inactive=0 ORDER BY FullName ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[2]);
				/** UserName */
				aUserbean.setLabel((String) aObj[2]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getFrieghtChange(String theFrieghtChange)
			throws JobException {
		String salesselectQry = "SELECT veFreightChargesID, Description FROM veFreightCharges "
				+ "WHERE Description like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theFrieghtChange)
				+ "%'"
				+ " ORDER BY Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<Vefreightcharges> getVefreightcharges() throws JobException {
		Session aSession = null;
		List<Vefreightcharges> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query query = aSession.createQuery("FROM  Vefreightcharges");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Fried Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getShipVia(String theShipVia)
			throws JobException {
		String salesselectQry = "SELECT veShipViaID, Description FROM veShipVia "
				+ "WHERE Description like "
				+ "'%"
				+ JobUtil.removeSpecialcharacterswithslash(theShipVia)
				+ "%'"
				+ " ORDER BY Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<Veshipvia> getVeShipVia() throws JobException {
		Session aSession = null;
		List<Veshipvia> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query aQuery = aSession
					.createQuery("FROM Veshipvia WHERE Description <> '' ORDER BY Description ASC");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Ship Via in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getProductWithNameList(
			String theProductName) throws JobException {
		String salesselectQry = "SELECT prMasterID, ItemCode, Description FROM prMaster WHERE ItemCode like '"
				+ JobUtil.removeSpecialcharacterswithslash(theProductName)
				+ "%' and InActive='0' ORDER BY ItemCode asc ,Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				if (((String) aObj[2]) == null) {
					aUserbean.setLabel((String) aObj[1]);
				} else {
					String aProduct = (String) aObj[1] + " -["
							+ (String) aObj[2] + "]";
					aUserbean.setLabel(aProduct);
				}
				aQueryList.add(aUserbean);
			}
			if (aQueryList.isEmpty()) {
				aUserbean = new AutoCompleteBean();
				aUserbean.setValue(" ");
				aUserbean.setLabel(" ");
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getPayeeNameList(String theProductName)
			throws JobException {
		String salesselectQry = "SELECT rxMasterID, Name FROM rxMaster WHERE Name like '"
				+ JobUtil.removeSpecialcharacterswithslash(theProductName) + "%' ORDER BY Name ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);

				String aProduct = (String) aObj[1];
				aUserbean.setLabel(aProduct);

				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<?> getLineItemProductList() throws JobException {
		String salesselectQry = "SELECT prMasterID, ItemCode, Description FROM prMaster ORDER BY Description ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				if (((String) aObj[2]) == null) {
					aUserbean.setLabel((String) aObj[1]);
				} else {
					String aProduct = (String) aObj[1] + " -["
							+ (String) aObj[2] + "]";
					aUserbean.setLabel(aProduct);
				}
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Prmaster getProductSingleNameList(Integer theProductID)
			throws JobException {
		Session aSession = null;
		Prmaster aPrmaster = null;
		Prmaster aPrmasterVal = new Prmaster();
		try {
			aSession = itsSessionFactory.openSession();
			aPrmasterVal.setPrMasterId(theProductID);
			aPrmaster = (Prmaster) aSession.get(Prmaster.class,
					aPrmasterVal.getPrMasterId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPrmaster;
	}

	@Override
	public Integer getProductIDBaseName(String theProductNameWithCode)
			throws JobException {
		Integer aPrMasterID = null;
		String aProduct = theProductNameWithCode.replace(" ", "");
		String aSelectQry = "SELECT prMasterID FROM prMaster WHERE ItemCode like '%"
				+ JobUtil.removeSpecialcharacterswithslash(aProduct) + "%'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aPrMasterID = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;aProduct=null;
		}
		return aPrMasterID;
	}
	
	@Override
	public Integer getStarProductIDforXMLUpload()
			{
		Integer aPrMasterID = null;
		String aSelectQry = "SELECT prMasterID FROM prMaster WHERE ItemCode='*'";
				
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aPrMasterID = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aPrMasterID;
	}

	@Override
	public Integer getScheduledIDBaseSubmittalID(Integer theSchelduleModelID)
			throws JobException {
		Integer aSubmittalHeaderID = null;
		String aSelectQry = "SELECT joSchedTempHeaderID FROM joScheduleModel where joscheduleModelId = '"
				+ theSchelduleModelID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aSubmittalHeaderID = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aSubmittalHeaderID;
	}

	@Override
	public ArrayList<Joschedtempcolumn> getScheduledColumnName(
			Integer theSchedModelID) throws JobException {
		String salesselectQry = "SELECT joSchedTempColumnID, joSchedTempHeaderID, ColumnNumber, joSchedTempColumnTypeID, DisplayText, "
				+ "DisplayWidth, PrintText, PrintWidth, importToOrder FROM joSchedTempColumn where "
				+ "joschedtempheaderid = '"
				+ theSchedModelID
				+ "' ORDER BY ColumnNumber ASC";
		Session aSession = null;
		ArrayList<Joschedtempcolumn> aQueryList = new ArrayList<Joschedtempcolumn>();
		try {
			Joschedtempcolumn aJoschedtempcolumn = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoschedtempcolumn = new Joschedtempcolumn();
				Object[] aObj = (Object[]) aIterator.next();
				aJoschedtempcolumn.setJoSchedTempColumnId((Integer) aObj[0]);
				aJoschedtempcolumn.setJoSchedTempHeaderId((Integer) aObj[1]);
				aJoschedtempcolumn.setColumnNumber((Integer) aObj[2]);
				aJoschedtempcolumn
						.setJoSchedTempColumnTypeId((Integer) aObj[3]);
				aJoschedtempcolumn.setDisplayText((String) aObj[4]);
				aJoschedtempcolumn.setDisplayWidth((Integer) aObj[5]);
				aJoschedtempcolumn.setPrintText((String) aObj[6]);
				aJoschedtempcolumn.setPrintWidth((Integer) aObj[7]);
				aJoschedtempcolumn.setImportToOrder((Integer) aObj[8]);
				aQueryList.add(aJoschedtempcolumn);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getSheduledColumnModel(String theSubmitID,
			Integer theColumnLength) throws JobException {
		itsLogger.debug("Query to Select All SheduledColumnModel");
		String aSubmittalQry = "SELECT sm.ModelNo, sm.Col01, sd.Quantity, sm.Col02, sm.Col03, sm.Col04, sm.Col05, sm.Col06, sm.Col07, sm.Col08, "
				+ "sm.Col09, sm.Col10, sm.Col11, sm.Col12, sm.Col13, sm.Col14, sm.Col15, sm.Col16, sm.Col17, sm.Col18, sm.Col19, sm.Col20, "
				+ "sm.joScheduleModelID, sm.joSchedTempHeaderID FROM joScheduleModel sm "
				+ " JOIN joScheduleDetail sd ON sd.joScheduleModelID=sm.joScheduleModelID where sd.joSubmittalDetailID= '"
				+ theSubmitID + "'";
		Session aSession = null;
		ArrayList<Joschedulemodel> aQryList = new ArrayList<Joschedulemodel>();
		try {
			Joschedulemodel aJoschedulemodel = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSubmittalQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoschedulemodel = new Joschedulemodel();
				Object[] aObj = (Object[]) aIterator.next();
				aJoschedulemodel.setModelNo((String) aObj[0]);
				aJoschedulemodel.setCol01((String) aObj[1]);
				aJoschedulemodel.setUnitCost((BigDecimal) aObj[2]);
				aJoschedulemodel.setCol02((String) aObj[3]);
				aJoschedulemodel.setCol03((String) aObj[4]);
				aJoschedulemodel.setCol04((String) aObj[5]);
				aJoschedulemodel.setCol05((String) aObj[6]);
				aJoschedulemodel.setCol06((String) aObj[7]);
				aJoschedulemodel.setCol07((String) aObj[8]);
				aJoschedulemodel.setCol08((String) aObj[9]);
				aJoschedulemodel.setCol09((String) aObj[10]);
				aJoschedulemodel.setCol10((String) aObj[11]);
				aJoschedulemodel.setCol11((String) aObj[12]);
				aJoschedulemodel.setCol12((String) aObj[13]);
				aJoschedulemodel.setCol13((String) aObj[14]);
				aJoschedulemodel.setCol14((String) aObj[15]);
				aJoschedulemodel.setCol15((String) aObj[16]);
				aJoschedulemodel.setCol16((String) aObj[17]);
				aJoschedulemodel.setCol17((String) aObj[18]);
				aJoschedulemodel.setCol18((String) aObj[19]);
				aJoschedulemodel.setCol19((String) aObj[20]);
				aJoschedulemodel.setCol20((String) aObj[21]);
				aJoschedulemodel.setJoScheduleModelId((Integer) aObj[22]);
				aJoschedulemodel.setJoSchedTempHeaderId((Integer) aObj[23]);
				aQryList.add(aJoschedulemodel);
			}
		} catch (Exception e) {
			itsLogger.error(
					"Exception while getting the submittal list: "
							+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSubmittalQry=null;
		}
		return aQryList;
	}

	@Override
	public List<JobQuoteDetailBean> getQuotesDetailGridList(
			Integer theJoQuoteHeaderID) throws JobException {
		String aQuotesDetailGridList = "SELECT "
				+ "jqd.joQuoteDetailID, "
				+ "jqd.joQuoteHeaderID, "
				+ "jqd.Product, "
				+ "jqd.ProductNote, "
				+ "jqd.ItemQuantity, "
				+ "jqd.Paragraph, "
				+ "jqd.Price, "
				+ "jqd.Cost, "
				+ "jqd.InlineNote, "
				+ "rm.Name AS Manufacturer, "
				+ "jqd.rxManufacturerID, "
				+ "jqd.veFactoryID, "
				+ "jqd.position, "
				+ "jqd.Percentage, "
				+ "jqd.Mult, "
				+ "jqd.Spec, "
				+ "jqprop.italicItem,jqprop.underlineItem,jqprop.boldItem,"
				+ "jqprop.italicQuantity,jqprop.underlineQuantity,jqprop.boldQuantity,"
				+ "jqprop.italicParagraph,jqprop.underlineParagraph,jqprop.boldParagraph,"
				+ "jqprop.italicManufacturer,jqprop.underlineManufacturer,jqprop.boldManufacturer,"
				+ "jqprop.italicSpec,jqprop.underlineSpec,jqprop.boldSpec,"
				+ "jqprop.italicMult,jqprop.underlineMult,jqprop.boldMult,"
				+ "jqprop.italicPrice,jqprop.underlinePrice,jqprop.boldPrice "

				// +"IF(jqprop.italicItem IS NULL,'0',jqprop.italicItem) AS italicItem,IF(ISNULL (jqprop.underlineItem),'0',jqprop.underlineItem) AS underlineItem,"
				// +"IF(ISNULL (jqprop.boldItem),'0',jqprop.boldItem) AS boldItem,IF(ISNULL (jqprop.italicQuantity),'0',jqprop.italicQuantity) AS italicQuantity,"
				// +" IF(ISNULL (jqprop.underlineQuantity),'0',jqprop.underlineQuantity) AS underlineQuantity,IF(ISNULL (jqprop.boldQuantity),'0',jqprop.boldQuantity) AS boldQuantity,"
				// +"IF(ISNULL (jqprop.italicParagraph),'0',jqprop.italicParagraph) AS italicParagraph,IF(ISNULL (jqprop.underlineParagraph),'0',jqprop.underlineParagraph) AS underlineParagraph,"
				// +" IF(ISNULL (jqprop.boldParagraph),'0',jqprop.boldParagraph) AS boldParagraph,IF(ISNULL (jqprop.italicManufacturer),'0',jqprop.italicManufacturer) AS italicManufacturer,"
				// +"IF(ISNULL (jqprop.underlineManufacturer),'0',jqprop.underlineManufacturer) AS underlineManufacturer,IF(ISNULL (jqprop.boldManufacturer),'0',jqprop.boldManufacturer) AS boldManufacturer,"
				// +"IF(ISNULL (jqprop.italicSpec),'0',jqprop.italicSpec) AS italicSpec,IF(ISNULL (jqprop.underlineSpec),'0',jqprop.underlineSpec) AS underlineSpec,"
				// +" IF(ISNULL (jqprop.boldSpec),'0',jqprop.boldSpec) AS boldSpec,IF(ISNULL (jqprop.italicMult),'0',jqprop.italicMult) AS italicMult,"
				// +" IF(ISNULL (jqprop.underlineMult),'0',jqprop.underlineMult) AS underlineMult,IF(ISNULL (jqprop.boldMult),'0',jqprop.boldMult) AS boldMult,"
				// +" IF(ISNULL (jqprop.italicPrice),'0',jqprop.italicPrice) AS italicPrice,IF(ISNULL (jqprop.underlinePrice),'0',jqprop.underlinePrice) AS underlinePrice,"
				// +" IF(ISNULL (jqprop.boldPrice),'0',jqprop.boldPrice) AS boldPrice "

				+ "FROM joQuoteDetail jqd "
				+ "LEFT JOIN rxMaster rm ON  rm.rxMasterID = jqd.rxManufacturerID  "
				+ "LEFT JOIN joQLineItemsProp jqprop ON(jqd.joQuoteDetailID=jqprop.joQuoteDetailID) "
				+ "WHERE jqd.joQuoteheaderID =  '" + theJoQuoteHeaderID
				+ "' ORDER BY jqd.position";
		itsLogger.info("Query Called: " + aQuotesDetailGridList);
		ArrayList<JobQuoteDetailBean> aQueryList = new ArrayList<JobQuoteDetailBean>();
		JobQuoteDetailBean aJobQuoteDetailBean = null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQuotesDetailGridList);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuoteDetailBean = new JobQuoteDetailBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuoteDetailBean.setJoQuoteDetailID((Integer) aObj[0]);
				aJobQuoteDetailBean.setJoQuoteHeaderID((Integer) aObj[1]);
				aJobQuoteDetailBean.setProduct((String) aObj[2]);
				aJobQuoteDetailBean.setProductNote((String) aObj[3]);
				aJobQuoteDetailBean.setItemQuantity((String) aObj[4]);
				aJobQuoteDetailBean.setParagraph((String) aObj[5]);
				aJobQuoteDetailBean.setPrice((BigDecimal) aObj[6]);
				aJobQuoteDetailBean.setCost((BigDecimal) aObj[7]);
				aJobQuoteDetailBean.setInlineNote((String) aObj[8]);
				aJobQuoteDetailBean.setManufacturer((String) aObj[9]);
				aJobQuoteDetailBean.setRxManufacturerID((Integer) aObj[10]);
				aJobQuoteDetailBean.setVeFactoryId((Short) aObj[11]);
				aJobQuoteDetailBean.setPosition((Double) aObj[12]);
				aJobQuoteDetailBean.setInLineNoteImage((String) aObj[8]);
				aJobQuoteDetailBean.setPercentage((BigDecimal) aObj[13]);
				aJobQuoteDetailBean.setMult((BigDecimal) aObj[14]);
				aJobQuoteDetailBean.setSpec((Short) aObj[15]);
				String itemclassname = " ";
				
				if (aObj[16] != null && ((Byte) aObj[16]) == 1) {
					itemclassname = itemclassname + " griditalic";
				}
				if (aObj[17] != null && ((Byte) aObj[17]) == 1) {
					itemclassname = itemclassname + " gridunderline";
				}
				if (aObj[18] != null && ((Byte) aObj[18]) == 1) {
					itemclassname = itemclassname + " gridbold";
				}
				aJobQuoteDetailBean.setItemClassName(itemclassname);

				String quantityclassname = " ";

				if (aObj[19] != null && ((Byte) aObj[19]) == 1) {
					quantityclassname = quantityclassname + " griditalic";
				}
				if (aObj[20] != null && ((Byte) aObj[20]) == 1) {
					quantityclassname = quantityclassname + " gridunderline";
				}
				if (aObj[21] != null && ((Byte) aObj[21]) == 1) {
					quantityclassname = quantityclassname + " gridbold";
				}
				aJobQuoteDetailBean.setQtyClassName(quantityclassname);

				String paragraphclassname = " ";

				if (aObj[22] != null && ((Byte) aObj[22]) == 1) {
					paragraphclassname = paragraphclassname + " griditalic";
				}
				if (aObj[23] != null && ((Byte) aObj[23]) == 1) {
					paragraphclassname = paragraphclassname + " gridunderline";
				}
				if (aObj[24] != null && ((Byte) aObj[24]) == 1) {
					paragraphclassname = paragraphclassname + " gridbold";
				}
				aJobQuoteDetailBean.setParaClassName(paragraphclassname);

				String Manclassname = " ";

				if (aObj[25] != null && ((Byte) aObj[25]) == 1) {
					Manclassname = Manclassname + " griditalic";
				}
				if (aObj[26] != null && ((Byte) aObj[26]) == 1) {
					Manclassname = Manclassname + " gridunderline";
				}
				if (aObj[27] != null && ((Byte) aObj[27]) == 1) {
					Manclassname = Manclassname + " gridbold";
				}
				aJobQuoteDetailBean.setManfcClassName(Manclassname);

				String Specclassname = " ";

				if (aObj[28] != null && ((Byte) aObj[28]) == 1) {
					Specclassname = Specclassname + " griditalic";
				}
				if (aObj[29] != null && ((Byte) aObj[29]) == 1) {
					Specclassname = Specclassname + " gridunderline";
				}
				if (aObj[30] != null && ((Byte) aObj[30]) == 1) {
					Specclassname = Specclassname + " gridbold";
				}
				aJobQuoteDetailBean.setSpecClassName(Specclassname);

				String Multclassname = " ";

				if (aObj[31] != null && ((Byte) aObj[31]) == 1) {
					Multclassname = Multclassname + " griditalic";
				}
				if (aObj[32] != null && ((Byte) aObj[32]) == 1) {
					Multclassname = Multclassname + " gridunderline";
				}
				if (aObj[33] != null && ((Byte) aObj[33]) == 1) {
					Multclassname = Multclassname + " gridbold";
				}
				aJobQuoteDetailBean.setMultClassName(Multclassname);

				String Priceclassname = " ";

				if (aObj[34] != null && ((Byte) aObj[34]) == 1) {
					Priceclassname = Priceclassname + " griditalic";
				}
				if (aObj[35] != null && ((Byte) aObj[35]) == 1) {
					Priceclassname = Priceclassname + " gridunderline";
				}
				if (aObj[36] != null && ((Byte) aObj[36]) == 1) {
					Priceclassname = Priceclassname + " gridbold";
				}
				aJobQuoteDetailBean.setPriceClassName(Priceclassname);

				aQueryList.add(aJobQuoteDetailBean);
				itemclassname=null;quantityclassname=null;paragraphclassname=null;
				Manclassname=null;Specclassname=null;Multclassname=null;Priceclassname=null;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuotesDetailGridList=null;
		}
		return aQueryList;
	}

	@Override
	public List<JobQuoteDetailBean> getQuotesDetailList(
			Integer theJoQuoteHeaderID) throws JobException {
		String aQuotesDetailGridList = "SELECT "
				+ "jqd.joQuoteDetailID, "
				+ "jqd.joQuoteHeaderID, "
				+ "jqd.Product, "
				+ "jqd.ProductNote, "
				+ "jqd.ItemQuantity, "
				+ "jqd.Paragraph, "
				+ "jqd.Price, "
				+ "jqd.Cost, "
				+ "jqd.InlineNote, "
				+ "rm.Name As Name, "
				+ "vf.Description AS Manufacturer, "
				+ "jqd.rxManufacturerID, "
				+ "jqd.veFactoryID, "
				+ "jqd.position, "
				+ "vf.InActive "
				+ "FROM joQuoteDetail jqd "
				+ "LEFT JOIN veFactory vf ON jqd.veFactoryID = vf.veFactoryID "
				+ "LEFT JOIN rxMaster rm ON  rm.rxMasterID = jqd.rxManufacturerID "
				+ "WHERE jqd.joQuoteheaderID =  '" + theJoQuoteHeaderID
				+ "' ORDER BY jqd.position";
		ArrayList<JobQuoteDetailBean> aQueryList = new ArrayList<JobQuoteDetailBean>();
		JobQuoteDetailBean aJobQuoteDetailBean = null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQuotesDetailGridList);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuoteDetailBean = new JobQuoteDetailBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuoteDetailBean.setJoQuoteDetailID((Integer) aObj[0]);
				aJobQuoteDetailBean.setJoQuoteHeaderID((Integer) aObj[1]);
				aJobQuoteDetailBean.setProduct((String) aObj[2]);
				aJobQuoteDetailBean.setProductNote((String) aObj[3]);
				aJobQuoteDetailBean.setItemQuantity((String) aObj[4]);
				aJobQuoteDetailBean.setParagraph((String) aObj[5]);
				aJobQuoteDetailBean.setPrice((BigDecimal) aObj[6]);
				aJobQuoteDetailBean.setCost((BigDecimal) aObj[7]);
				aJobQuoteDetailBean.setInlineNote((String) aObj[8]);
				aJobQuoteDetailBean.setInActive((Byte) aObj[14]);
				aJobQuoteDetailBean.setManufacturer((String) aObj[10]);
				aJobQuoteDetailBean.setRxManufacturerID((Integer) aObj[11]);
				aJobQuoteDetailBean.setVeFactoryId((Short) aObj[12]);
				aJobQuoteDetailBean.setPosition((Double) aObj[13]);
				aJobQuoteDetailBean.setInLineNoteImage((String) aObj[8]);
				aJobQuoteDetailBean.setManufacturer((String) aObj[9]);
				aQueryList.add(aJobQuoteDetailBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuotesDetailGridList=null;
		}
		return aQueryList;
	}

	@Override
	public List<JobQuoteDetailBean> getQuotesTemplateDetailList(
			Integer theJoQuoteHeaderID) throws JobException {
		itsLogger.info("Called: getQuotesTemplateDetailList");
		String aQuotesDetailGridList = "SELECT "
				+ "jqd.joQuoteTemplateDetailID, "
				+ "jqd.joQuoteTemplateHeaderID, "
				+ "jqd.Product, "
				+ "jqd.ProductNote, "
				+ "jqd.ItemQuantity, "
				+ "jqd.Paragraph, "
				+ "jqd.Price, "
				+ "jqd.Cost, "
				+ "jqd.InlineNote, "
				+ "rm.Name As Name, "
				+ "vf.Description AS Manufacturer, "
				+ "jqd.rxManufacturerID, "
				+ "jqd.veFactoryID, "
				+ "jqd.position, "
				+ "vf.InActive "
				+ "FROM joQuoteTemplateDetail jqd "
				+ "LEFT JOIN veFactory vf ON jqd.veFactoryID = vf.veFactoryID "
				+ "LEFT JOIN rxMaster rm ON  rm.rxMasterID = jqd.rxManufacturerID "
				+ "WHERE jqd.joQuoteTemplateHeaderID =  '" + theJoQuoteHeaderID
				+ "' ORDER BY jqd.position";
		ArrayList<JobQuoteDetailBean> aQueryList = new ArrayList<JobQuoteDetailBean>();
		JobQuoteDetailBean aJobQuoteDetailBean = null;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQuotesDetailGridList);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuoteDetailBean = new JobQuoteDetailBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuoteDetailBean.setJoQuoteDetailID((Integer) aObj[0]);
				aJobQuoteDetailBean.setJoQuoteHeaderID((Integer) aObj[1]);
				aJobQuoteDetailBean.setProduct((String) aObj[2]);
				aJobQuoteDetailBean.setProductNote((String) aObj[3]);
				aJobQuoteDetailBean.setItemQuantity((String) aObj[4]);
				aJobQuoteDetailBean.setParagraph((String) aObj[5]);
				aJobQuoteDetailBean.setPrice((BigDecimal) aObj[6]);
				aJobQuoteDetailBean.setCost((BigDecimal) aObj[7]);
				aJobQuoteDetailBean.setInlineNote((String) aObj[8]);
				aJobQuoteDetailBean.setInActive((Byte) aObj[14]);
				aJobQuoteDetailBean.setManufacturer((String) aObj[10]);
				aJobQuoteDetailBean.setRxManufacturerID((Integer) aObj[11]);
				aJobQuoteDetailBean.setVeFactoryId((Short) aObj[12]);
				aJobQuoteDetailBean.setPosition((Double) aObj[13]);
				aJobQuoteDetailBean.setInLineNoteImage((String) aObj[8]);
				aJobQuoteDetailBean.setManufacturer((String) aObj[9]);
				aQueryList.add(aJobQuoteDetailBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuotesDetailGridList=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getTemplateProductQuoteList(
			String theProductText) throws JobException {
		String aSalesselectQry = "SELECT joQuoteTemplateDetailID, Product, rxManufacturerID FROM joQuoteTemplateDetail WHERE Product like '"
				+ JobUtil.removeSpecialcharacterswithslash(theProductText) + "%'" + " LIMIT 0,100";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				if (aObj[2] != null) {
					aUserbean.setManufactureID((Integer) aObj[2]);
				}
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getProductQuoteList(String theProductText)
			throws JobException {
		String aSalesselectQry = "SELECT joQuoteDetailID, Product, rxManufacturerID FROM joQuoteDetail WHERE Product like '"
				+ JobUtil.removeSpecialcharacterswithslash(theProductText) + "%'" + " LIMIT 0,100";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				if (aObj[2] != null) {
					aUserbean.setManufactureID((Integer) aObj[2]);
				}
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getAccountDetails(String theProductText)
			throws JobException {
		String aSalesselectQry = "SELECT coAccountID,Number, concat(Number,' # ',Description), Description FROM coAccount WHERE Number like '"
				+ JobUtil.removeSpecialcharacterswithslash(theProductText) + "%'" + " AND InActive !=1 LIMIT 0,100";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[2]);
				if (aObj[3] != null) {
					String accDescription = (String) aObj[3];
					if (accDescription.contains("&")) {
						// accDescription = accDescription.replaceAll("&",
						// "&amp;");
					}
					aUserbean.setAccountDescription(accDescription);
				}
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<JoQuoteDetail> getQuoteDetails(Integer theQuoteDeatilID,
			Integer theRxMasterID) throws JobException {
		String aSalesselectQry = "";
		int aFactoryID = getFactoryId(theQuoteDeatilID);
		if (theRxMasterID == null) {
			theRxMasterID = 0;
		}
		if (theRxMasterID == 0) {
			aSalesselectQry = "SELECT jq.joQuoteHeaderID, jq.Product, jq.rxManufacturerID, jq.Paragraph, "
					+ "jq.Price, jq.Cost, jq.Subtotal, vf.vefactoryID FROM joQuoteDetail jq "
					+ "INNER JOIN veFactory vf ON vf.rxMasterID = '"
					+ aFactoryID
					+ "' "
					+ "WHERE jq.joQuoteDetailID= '"
					+ theQuoteDeatilID + "'";
		} else {
			aSalesselectQry = "SELECT jq.joQuoteHeaderID, jq.Product, jq.rxManufacturerID, jq.Paragraph, "
					+ "jq.Price, jq.Cost, jq.Subtotal, vf.vefactoryID, vx.Name FROM joQuoteDetail jq "
					+ "INNER JOIN rxMaster vx ON vx.rxMasterID = '"
					+ theRxMasterID
					+ "' "
					+ "INNER JOIN veFactory vf ON vf.rxMasterID = '"
					+ aFactoryID
					+ "' "
					+ "WHERE jq.joQuoteDetailID= '"
					+ theQuoteDeatilID + "'";
		}
		Session aSession = null;
		ArrayList<JoQuoteDetail> aQueryList = new ArrayList<JoQuoteDetail>();
		try {
			JoQuoteDetail aJoQuoteDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoQuoteDetail = new JoQuoteDetail();
				Object[] aObj = (Object[]) aIterator.next();
				aJoQuoteDetail.setJoQuoteHeaderID((Integer) aObj[0]);
				aJoQuoteDetail.setProduct((String) aObj[1]);
				aJoQuoteDetail.setRxManufacturerID((Integer) aObj[2]);
				aJoQuoteDetail.setParagraph((String) aObj[3]);
				aJoQuoteDetail.setPrice((BigDecimal) aObj[4]);
				aJoQuoteDetail.setCost((BigDecimal) aObj[5]);
				aJoQuoteDetail.setSubtotal((Byte) aObj[6]);
				aJoQuoteDetail.setDetailSequenceId((Integer) aObj[7]);
				if (theRxMasterID != 0) {
					aJoQuoteDetail.setItemQuantity((String) aObj[8]);
				}
				aQueryList.add(aJoQuoteDetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<JoQuoteTemplateDetail> getQuoteTemplateDetails(
			Integer theQuoteDeatilID, Integer theRxMasterID)
			throws JobException {
		String aSalesselectQry = "";
		int aFactoryID = getTemplateFactoryId(theQuoteDeatilID);
		if (theRxMasterID == null) {
			theRxMasterID = 0;
		}
		if (theRxMasterID == 0) {
			aSalesselectQry = "SELECT jq.joQuoteTemplateHeaderID, jq.Product, jq.rxManufacturerID, jq.Paragraph, "
					+ "jq.Price, jq.Cost, jq.Subtotal, vf.vefactoryID, vx.rxMasterID FROM joQuoteTemplateDetail jq "
					+ "INNER JOIN veFactory vf ON vf.rxMasterID = '"
					+ aFactoryID
					+ "' "
					+ "WHERE jq.joQuoteTemplateDetailID= '"
					+ theQuoteDeatilID + "'";
		} else {
			aSalesselectQry = "SELECT jq.joQuoteTemplateHeaderID, jq.Product, jq.rxManufacturerID, jq.Paragraph, "
					+ "jq.Price, jq.Cost, jq.Subtotal, vf.vefactoryID, vx.Name,vx.rxMasterID FROM joQuoteTemplateDetail jq "
					+ "INNER JOIN rxMaster vx ON vx.rxMasterID = '"
					+ theRxMasterID
					+ "' "
					+ "INNER JOIN veFactory vf ON vf.rxMasterID = '"
					+ aFactoryID
					+ "' "
					+ "WHERE jq.joQuoteTemplateDetailID= '"
					+ theQuoteDeatilID + "'";
		}
		Session aSession = null;
		ArrayList<JoQuoteTemplateDetail> aQueryList = new ArrayList<JoQuoteTemplateDetail>();
		try {
			JoQuoteTemplateDetail aJoQuoteDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoQuoteDetail = new JoQuoteTemplateDetail();
				Object[] aObj = (Object[]) aIterator.next();
				aJoQuoteDetail.setJoQuoteTemplateHeaderId((Integer) aObj[0]);
				aJoQuoteDetail.setProduct((String) aObj[1]);
				aJoQuoteDetail.setRxManufacturerId((Integer) aObj[2]);
				aJoQuoteDetail.setParagraph((String) aObj[3]);
				aJoQuoteDetail.setPrice((BigDecimal) aObj[4]);
				aJoQuoteDetail.setCost((BigDecimal) aObj[5]);
				aJoQuoteDetail.setSubTotal((Byte) aObj[6]);
				aJoQuoteDetail.setDetailSequenceId((Integer) aObj[7]);
				if (theRxMasterID != 0) {
					aJoQuoteDetail.setManufacturerName((String) aObj[8]);
					aJoQuoteDetail.setRxManufacturerId((Integer) aObj[9]);
				}
				aQueryList.add(aJoQuoteDetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	private int getTemplateFactoryId(Integer theQuoteDeatilID)
			throws JobException {
		Integer aManufacter = null;
		String aSelectQry = "SELECT rxmanufacturerID FROM joQuoteTemplateDetail where joQuoteTemplateDetailID= '"
				+ theQuoteDeatilID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aManufacter = (Integer) aQuery.list().get(0);
			if (aManufacter == null) {
				aManufacter = 44;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aManufacter;
	}

	private int getFactoryId(Integer theQuoteDeatilID) throws JobException {
		Integer aManufacter = null;
		String aSelectQry = "SELECT rxmanufacturerID FROM joQuoteDetail where joQuotedetailID= '"
				+ theQuoteDeatilID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aManufacter = (Integer) aQuery.list().get(0);
			if (aManufacter == null) {
				aManufacter = 44;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aManufacter;
	}

	@Override
	public boolean deleteProductQuote(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aJoProductSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoProductSession.beginTransaction();
			aTransaction.begin();
			aJoProductSession.delete(theJoQuoteDetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoProductSession.flush();
			aJoProductSession.close();
		}
		return false;
	}

	@Override
	public Integer addProductQuote(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aJoProductSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer theJoQuoteDetailId = null;
		try {
			aTransaction = aJoProductSession.beginTransaction();
			aTransaction.begin();
			aJoProductSession.save(theJoQuoteDetail);
			theJoQuoteDetailId = theJoQuoteDetail.getJoQuoteDetailId();
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoProductSession.flush();
			aJoProductSession.close();
		}
		return theJoQuoteDetailId;
	}

	@Override
	public boolean updateProductQuote(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aJoProductSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			itsLogger.info("Manufacture Id: "
					+ theJoQuoteDetail.getRxManufacturerID());
			Integer rxValue = theJoQuoteDetail.getRxManufacturerID();
			aTransaction = aJoProductSession.beginTransaction();
			aTransaction.begin();
			JoQuoteDetail aExistingProduct = (JoQuoteDetail) aJoProductSession
					.get(JoQuoteDetail.class,
							theJoQuoteDetail.getJoQuoteDetailId());
			itsLogger.info("Old Manufacture ID: "
					+ aExistingProduct.getRxManufacturerID());
			aExistingProduct.setProduct(theJoQuoteDetail.getProduct());
			aExistingProduct.setRxManufacturerID(rxValue);
			aExistingProduct.setParagraph(theJoQuoteDetail.getParagraph());
			aExistingProduct
					.setItemQuantity(theJoQuoteDetail.getItemQuantity());
			aExistingProduct.setCost(theJoQuoteDetail.getCost());
			aExistingProduct.setPrice(theJoQuoteDetail.getPrice());
			aExistingProduct.setVeFactoryId(theJoQuoteDetail.getVeFactoryId());
			// aExistingProduct.setProductNote(theJoQuoteDetail.getProductNote());
			// aExistingProduct.setInlineNote(theJoQuoteDetail.getInlineNote());
			aExistingProduct.setJoQuoteHeaderID(theJoQuoteDetail
					.getJoQuoteHeaderID());
			aExistingProduct.setJoQuoteDetailId(theJoQuoteDetail
					.getJoQuoteDetailId());
			aExistingProduct.setMult(theJoQuoteDetail.getMult());
			aExistingProduct.setPercentage(theJoQuoteDetail.getPercentage());
			aExistingProduct.setSpec(theJoQuoteDetail.getSpec());
			aJoProductSession.update(aExistingProduct);
			itsLogger.info("Updated Successfully");
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoProductSession.flush();
			aJoProductSession.close();
		}
		return false;
	}

	// added by niaz 20140901 for foot notes & inline item

	@Override
	public boolean updateProductQuoteFooter(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aJoProductSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			itsLogger.info("Manufacture Id: "
					+ theJoQuoteDetail.getRxManufacturerID());
			Integer rxValue = theJoQuoteDetail.getRxManufacturerID();
			aTransaction = aJoProductSession.beginTransaction();
			aTransaction.begin();
			JoQuoteDetail aExistingProduct = (JoQuoteDetail) aJoProductSession
					.get(JoQuoteDetail.class,
							theJoQuoteDetail.getJoQuoteDetailId());
			itsLogger.info("Old Manufacture ID: "
					+ aExistingProduct.getRxManufacturerID());
			// AEXISTINGPRODUCT.SETPRODUCT(THEJOQUOTEDETAIL.GETPRODUCT());
			// AEXISTINGPRODUCT.SETRXMANUFACTURERID(RXVALUE);
			// AEXISTINGPRODUCT.SETPARAGRAPH(THEJOQUOTEDETAIL.GETPARAGRAPH());
			// AEXISTINGPRODUCT.SETITEMQUANTITY(THEJOQUOTEDETAIL.GETITEMQUANTITY());
			// AEXISTINGPRODUCT.SETCOST(THEJOQUOTEDETAIL.GETCOST());
			// AEXISTINGPRODUCT.SETPRICE(THEJOQUOTEDETAIL.GETPRICE());
			// AEXISTINGPRODUCT.SETVEFACTORYID(THEJOQUOTEDETAIL.GETVEFACTORYId());
			aExistingProduct.setProductNote(theJoQuoteDetail.getProductNote());
			aExistingProduct.setInlineNote(theJoQuoteDetail.getInlineNote());
			// aExistingProduct.setJoQuoteHeaderID(theJoQuoteDetail.getJoQuoteHeaderID());
			// aExistingProduct.setJoQuoteDetailId(theJoQuoteDetail.getJoQuoteDetailId());
			// aExistingProduct.setPercentage(theJoQuoteDetail.getPercentage());
			// aExistingProduct.setSpec(theJoQuoteDetail.getSpec());
			aJoProductSession.update(aExistingProduct);
			itsLogger.info("Updated Successfully");
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoProductSession.flush();
			aJoProductSession.close();
		}
		return false;
	}

	@Override
	public ArrayList<AutoCompleteBean> getManufaturerList(
			String theManufaturerText) throws JobException {
		String aSalesselectQry = "SELECT veFactoryID, Description, rxMasterID FROM veFactory WHERE Description like '"
				+ JobUtil.removeSpecialcharacterswithslash(theManufaturerText) + "%'" + "";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aUserbean.setManufactureID((Integer) aObj[2]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	public Integer saveQuoteDetails(JoQuoteHeader theJoQuoteHeader,
			String theToken, Integer bidderId) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Joquotehistory aJoquotehistory = new Joquotehistory();
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (theToken.equals("add")) {
				aSession.save(theJoQuoteHeader);
			} else if (theToken.equals("edit")) {
				JoQuoteHeader aExistingQuoteHeader = (JoQuoteHeader) aSession
						.get(JoQuoteHeader.class,
								theJoQuoteHeader.getJoQuoteHeaderId());
				aExistingQuoteHeader.setCuMasterTypeID(theJoQuoteHeader
						.getCuMasterTypeID());
				aExistingQuoteHeader
						.setQuoteRev(theJoQuoteHeader.getQuoteRev());
				aExistingQuoteHeader.setCreatedById(theJoQuoteHeader
						.getCreatedById());
				aExistingQuoteHeader.setCreatedByName(theJoQuoteHeader
						.getCreatedByName());
				aExistingQuoteHeader.setRemarks(theJoQuoteHeader.getRemarks());
				aExistingQuoteHeader.setInternalNote(theJoQuoteHeader
						.getInternalNote());
				aExistingQuoteHeader.setQuoteAmount(theJoQuoteHeader
						.getQuoteAmount());
				aExistingQuoteHeader.setCostAmount(theJoQuoteHeader
						.getCostAmount());
				aExistingQuoteHeader.setDiscountedPrice(theJoQuoteHeader
						.getDiscountedPrice());
				aSession.update(aExistingQuoteHeader);
			} else if (theToken.equals("copy")) {
				aSession.save(theJoQuoteHeader);
			}
			aTransaction.commit();
			aJoquotehistory.setRxContactId(theJoQuoteHeader.getRxContactID());
			aJoquotehistory.setJoMasterId(theJoQuoteHeader.getJoMasterID());
			aJoquotehistory.setJoQuoteHeaderId(theJoQuoteHeader
					.getJoQuoteHeaderId());
			aJoquotehistory.setQuoteDate(theJoQuoteHeader.getDateCreated());
			// aJoquotehistory.setQuoteDate(getQuoteDate(bidderId));
			aJoquotehistory.setQuoteRev(theJoQuoteHeader.getQuoteRev());
			if (bidderId != null) {
				aJoquotehistory.setJoBidderId(bidderId);
			}

			updateJoQuoteHistory(aJoquotehistory, theToken);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return theJoQuoteHeader.getJoQuoteHeaderId();
	}

	@Override
	public Integer copyQuoteDetails(JoQuoteHeader theJoQuoteHeader,
			String theToken) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Joquotehistory aJoquotehistory = new Joquotehistory();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			JoQuoteHeader aExistingQuoteHeader = (JoQuoteHeader) aSession
					.get(JoQuoteHeader.class,
							theJoQuoteHeader.getJoQuoteHeaderId());
			aExistingQuoteHeader.setCuMasterTypeID(theJoQuoteHeader
					.getCuMasterTypeID());
			aExistingQuoteHeader.setQuoteRev(theJoQuoteHeader.getQuoteRev());
			aExistingQuoteHeader.setCreatedById(theJoQuoteHeader
					.getCreatedById());
			aExistingQuoteHeader.setCreatedByName(theJoQuoteHeader
					.getCreatedByName());
			aExistingQuoteHeader.setRemarks(theJoQuoteHeader.getRemarks());
			aExistingQuoteHeader.setInternalNote(theJoQuoteHeader
					.getInternalNote());
			aExistingQuoteHeader.setQuoteAmount(theJoQuoteHeader
					.getQuoteAmount());
			aExistingQuoteHeader
					.setCostAmount(theJoQuoteHeader.getCostAmount());
			aExistingQuoteHeader.setDiscountedPrice(theJoQuoteHeader
					.getDiscountedPrice());
			aSession.update(aExistingQuoteHeader);
			aTransaction.commit();
			aJoquotehistory.setJoMasterId(theJoQuoteHeader.getJoMasterID());
			aJoquotehistory.setJoQuoteHeaderId(theJoQuoteHeader
					.getJoQuoteHeaderId());
			aJoquotehistory.setQuoteDate(theJoQuoteHeader.getDateCreated());
			aJoquotehistory.setQuoteRev(theJoQuoteHeader.getQuoteRev());
			updateJoQuoteHistory(aJoquotehistory, "edit");
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return theJoQuoteHeader.getJoQuoteHeaderId();
	}

	public boolean updateLineInfo(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			JoQuoteDetail aLineIfo = (JoQuoteDetail) aSession.get(
					JoQuoteDetail.class, theJoQuoteDetail.getJoQuoteDetailId());
			aLineIfo.setInlineNote(theJoQuoteDetail.getInlineNote());
			aLineIfo.setJoQuoteHeaderID(theJoQuoteDetail.getJoQuoteHeaderID());
			aLineIfo.setJoQuoteDetailId(theJoQuoteDetail.getJoQuoteDetailId());
			aSession.update(aLineIfo);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public Integer getJobStaus(Integer joMasterID) throws JobException {
		int aJobStatus = 0;
		String aSelectQry = "SELECT JobStatus FROM joMaster WHERE joMasterID="+ joMasterID;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if(aList!=null && aList.size()>0)
			aJobStatus = (Short) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJobStatus;
	}

	@Override
	public String getjobStatusName(Integer theStatusID) throws JobException {
		String aStatus = null;
		String aJobStatus = "SELECT JobStatus FROM joStatus where joStatusID='"
				+ theStatusID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobStatus);
			List<?> aList = aQuery.list();
			aStatus = (String) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobStatus=null;
		}
		return aStatus;
	}

	@Override
	public String getCustomerName(String theCustomerID) throws JobException {
		String aCustomerName = null;
		String aCustomerID = "SELECT Name FROM rxMaster WHERE rxMasterID= '"
				+ theCustomerID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			if (theCustomerID != null && !theCustomerID.equalsIgnoreCase("")) {
				Query aQuery = aSession.createSQLQuery(aCustomerID);
				List<?> aList = aQuery.list();
				if(aList.size()>0){
				aCustomerName = (String) aList.get(0);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerID=null;
		}
		return aCustomerName;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCityAndState(String theCityAndState)
			throws JobException {
		String salesselectQry = "SELECT DISTINCT(city), state FROM rxAddress WHERE city like '%"
				+ JobUtil.removeSpecialcharacterswithslash(theCityAndState) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				String aCity = (String) aObj[0];
				String aState = (String) aObj[1];
				String aCityState = aCity + " " + "(" + aState + ")";
				aUserbean.setValue(aCity);
				aUserbean.setLabel(aCityState);
				aQueryList.add(aUserbean);
				aCity=null;aState=null;aCityState=null;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean deleteQuickQuote(JoQuoteHeader theJoQuoteHeader)
			throws JobException {
		Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			JoQuoteHeader aExistingQuoteHeader = (JoQuoteHeader) aJoQuoteHeaderSession.get(JoQuoteHeader.class,theJoQuoteHeader.getJoQuoteHeaderId());
			if(aExistingQuoteHeader!=null){
			aTransaction = aJoQuoteHeaderSession.beginTransaction();
			aTransaction.begin();
			aJoQuoteHeaderSession.delete(aExistingQuoteHeader);
			aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoQuoteHeaderSession.flush();
			aJoQuoteHeaderSession.close();
		}
		return false;
	}

	public ArrayList<JoQuoteDetail> getJoQuoteDetailID(
			Integer theJoQuoteHeaderId) throws JobException {
		String aSelectQry = "SELECT * FROM joQuoteDetail where joQuoteHeaderID= '"
				+ theJoQuoteHeaderId + "'";
		Session aSession = null;
		ArrayList<JoQuoteDetail> aQueryList = new ArrayList<JoQuoteDetail>();
		try {
			JoQuoteDetail aJoQuoteDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoQuoteDetail = new JoQuoteDetail();
				Object[] aObj = (Object[]) aIterator.next();
				aJoQuoteDetail.setJoQuoteDetailId((Integer) aObj[0]);
				aQueryList.add(aJoQuoteDetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aQueryList;
	}

	public ArrayList<JoQuoteDetail> getJoQuoteDetails(Integer theJoQuoteHeaderId)
			throws JobException {
		String aSelectQry = "SELECT * FROM joQuoteDetail where joQuoteHeaderID= '"
				+ theJoQuoteHeaderId + "' ORDER BY Position";
		Session aSession = null;
		ArrayList<JoQuoteDetail> aQueryList = new ArrayList<JoQuoteDetail>();
		try {
			JoQuoteDetail aJoQuoteDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJoQuoteDetail = new JoQuoteDetail();
				Object[] aObj = (Object[]) aIterator.next();
				aJoQuoteDetail.setJoQuoteDetailId((Integer) aObj[0]);
				aJoQuoteDetail.setJoQuoteHeaderID((Integer) aObj[1]);
				aJoQuoteDetail.setProduct((String) aObj[2]);
				aJoQuoteDetail.setProductNote((String) aObj[3]);
				aJoQuoteDetail.setItemQuantity((String) aObj[4]);
				aJoQuoteDetail.setParagraph((String) aObj[5]);
				aJoQuoteDetail.setRxManufacturerID((Integer) aObj[6]);
				aJoQuoteDetail.setPrice((BigDecimal) aObj[7]);
				aJoQuoteDetail.setInlineNote((String) aObj[8]);
				aJoQuoteDetail.setCost((BigDecimal) aObj[9]);
				aJoQuoteDetail.setVeFactoryId((Short) aObj[11]);
				aJoQuoteDetail.setPercentage((BigDecimal) aObj[21]);
				aQueryList.add(aJoQuoteDetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean saveCreditInfoDetails(Jomaster theJomaster)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Jomaster aJomaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			aJomaster.setCreditStatus(theJomaster.getCreditStatus());
			aJomaster.setCreditStatusDate(theJomaster.getCreditStatusDate());
			aJomaster.setWho0(theJomaster.getWho0());
			aJomaster.setCreditType(theJomaster.getCreditType());
			aJomaster.setCreditTypeDate(theJomaster.getCreditTypeDate());
			aJomaster.setWho1(theJomaster.getWho1());
			aJomaster.setRequestedNoc(theJomaster.isRequestedNoc());
			aJomaster.setRequestedNocdate(theJomaster.getRequestedNocdate());
			aJomaster.setWho2(theJomaster.getWho2());
			aJomaster.setReceivedNoc(theJomaster.isReceivedNoc());
			aJomaster.setReceivedNocdate(theJomaster.getReceivedNocdate());
			aJomaster.setWho3(theJomaster.getWho3());
			aJomaster.setSentNtc(theJomaster.isSentNtc());
			aJomaster.setSentNtcdate(theJomaster.getSentNtcdate());
			aJomaster.setWho4(theJomaster.getWho4());
			aJomaster.setCreditReferenceNumber(theJomaster
					.getCreditReferenceNumber());
			aJomaster.setLienWaverSigned(theJomaster.isLienWaverSigned());
			aJomaster.setLienWaverSignedDate(theJomaster
					.getLienWaverSignedDate());
			aJomaster.setWho5(theJomaster.getWho5());
			aJomaster.setLienWaverThrough(theJomaster.isLienWaverThrough());
			aJomaster.setLienWaverThroughDate(theJomaster
					.getLienWaverThroughDate());
			aJomaster.setWho6(theJomaster.getWho6());
			aJomaster.setLienWaverThroughAmount(theJomaster
					.getLienWaverThroughAmount());
			aJomaster.setLienWaverThroughFinal(theJomaster
					.isLienWaverThroughFinal());
			aJomaster.setClaimFiled(theJomaster.getClaimFiled());
			aJomaster.setClaimFiledDate(theJomaster.getClaimFiledDate());
			aJomaster.setWho7(theJomaster.getWho7());
			aJomaster.setCreditNotes(theJomaster.getCreditNotes());
			aJomaster.setJobBonded(theJomaster.isJobBonded());
			aJomaster.setCreditContact0(theJomaster.getCreditContact0());
			aJomaster.setCreditContact1(theJomaster.getCreditContact1());
			aJomaster.setCreditContact2(theJomaster.getCreditContact2());
			aJomaster.setRxCategory4(theJomaster.getRxCategory4());
			aJomaster.setRxCategory5(theJomaster.getRxCategory5());
			aJomaster.setJobNumber(theJomaster.getJobNumber());
			aJomaster.setJoMasterId(theJomaster.getJoMasterId());
			aSession.update(aJomaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turborep.turbotracker.job.service.JobService#getVendorsList(java.
	 * lang.String)
	 * 
	 * @Last Modified: 08/22/2014
	 * 
	 * @Purpose: Any part of Vendor search on Quotes detail
	 */
	@Override
	public ArrayList<AutoCompleteBean> getVendorsList(String theVendorText)
			throws JobException {
		itsLogger.debug("Retrieving all represent names");
		String aSalesselectQry = "SELECT rxMasterID, Name FROM rxMaster WHERE IsVendor = 1 AND Name like '%"
				+ JobUtil.removeSpecialcharacterswithslash(theVendorText) + "%'" + "";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSalesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSalesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public int getFactoryID(Integer theManufaturer, String thename)
			throws JobException {
		Integer aFactoryID = null;
		String aSelectQry = "SELECT veFactoryID FROM veFactory where rxMasterID= '"
				+ theManufaturer + "'";
		Transaction aTransaction;
		Session aGetFactoryaSession = null;
		VeFactory aVeFactory = null;
		try {
			aGetFactoryaSession = itsSessionFactory.openSession();
			Query aQuery = aGetFactoryaSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				aFactoryID = (Integer) aList.get(0);
			} else {
				aVeFactory = new VeFactory();
				aTransaction = aGetFactoryaSession.beginTransaction();
				aTransaction.begin();
				aVeFactory.setRxMasterId(theManufaturer);
				aVeFactory.setInActive(false);
				if (thename.length() > 20) {
					String asearchText = thename.substring(0, 19);
					aVeFactory.setDescription(asearchText);
				} else {
					aVeFactory.setDescription(thename);
				}
				aGetFactoryaSession.save(aVeFactory);
				aTransaction.commit();
				aFactoryID = getFactory(theManufaturer);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aGetFactoryaSession.flush();
			aGetFactoryaSession.close();
			aSelectQry=null;
		}
		return aFactoryID;
	}

	public Integer getFactory(Integer themanufacturer) throws JobException {
		Integer aFactoryID = null;
		String aSelectQry = "SELECT veFactoryID FROM veFactory where rxMasterID= '"
				+ themanufacturer + "'";
		Session aGetFactoryaSession = null;
		try {
			aGetFactoryaSession = itsSessionFactory.openSession();
			Query aQuery = aGetFactoryaSession.createSQLQuery(aSelectQry);
			aFactoryID = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aGetFactoryaSession.flush();
			aGetFactoryaSession.close();
			aSelectQry=null;
		}
		return aFactoryID;
	}

	@Override
	public boolean updateCuMaster(Cumaster theCumaster) throws JobException {
		Session aCuMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuMasterSession.beginTransaction();
			aTransaction.begin();
			Cumaster aExistingCumaster = (Cumaster) aCuMasterSession.get(
					Cumaster.class, theCumaster.getCuMasterId());
			aExistingCumaster
					.setCuMasterTypeId(theCumaster.getCuMasterTypeId());
			aExistingCumaster.setCuMasterId(theCumaster.getCuMasterId());
			aCuMasterSession.update(aExistingCumaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aCuMasterSession.flush();
			aCuMasterSession.close();
		}
		return false;
	}

	@Override
	public boolean updateCustomerBidList(Jomaster theJoMaster)
			throws JobException {
		itsLogger.debug("updateCustomerBidList");
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Jomaster aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJoMaster.getJoMasterId());
			if (theJoMaster.getBidDate() != null) {
				aJoMaster.setBidDate(theJoMaster.getBidDate());
			}
			aJoMaster.setCuAssignmentId0(theJoMaster.getCuAssignmentId0());
			aJoMaster.setJobStatus(theJoMaster.getJobStatus());
			aJoMaster.setJoMasterId(theJoMaster.getJoMasterId());
			aSession.update(aJoMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public boolean updateCustomerRxMaster(Rxmaster theRxmaster)
			throws JobException {
		itsLogger.debug("updateCustomerRxMaster");
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Rxmaster aRxmaster = (Rxmaster) aSession.get(Rxmaster.class,
					theRxmaster.getRxMasterId());
			aRxmaster.setChangedById(theRxmaster.getChangedById());
			aRxmaster.setCreatedById(theRxmaster.getCreatedById());
			aRxmaster.setRxMasterId(theRxmaster.getRxMasterId());
			aSession.update(aRxmaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public boolean addNewContact(Rxcontact theRxcontact) throws JobException {
		Session aRxcontactSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aRxcontactSession.beginTransaction();
			aTransaction.begin();
			aRxcontactSession.save(theRxcontact);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aRxcontactSession.flush();
			aRxcontactSession.close();
		}
		return true;
	}

	@Override
	public boolean UpdateStatus(Jomaster theJomaster) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Jomaster aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			aJoMaster.setJobStatus(theJomaster.getJobStatus());
			aSession.update(aJoMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public Jomaster updatePlanAndSpec(Jomaster theJomaster) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Jomaster aJoMaster = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			aJoMaster.setBinNumber(theJomaster.getBinNumber());
			aJoMaster.setPlanDate(theJomaster.getPlanDate());
			aJoMaster.setPlanNumbers(theJomaster.getPlanNumbers());
			aJoMaster.setJoMasterId(theJomaster.getJoMasterId());
			aSession.update(aJoMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoMaster;
	}
	
	@Override
	public Jomaster updateJobSources(Jomaster theJomaster) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Jomaster aJoMaster = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			aJoMaster.setSource1(theJomaster.isSource1());
			aJoMaster.setSource2(theJomaster.isSource2());
			aJoMaster.setSource3(theJomaster.isSource3());
			aJoMaster.setSource4(theJomaster.isSource4());
			aJoMaster.setSourceReport1(theJomaster.getSourceReport1());
			aJoMaster.setOtherSource(theJomaster.getOtherSource());
			aJoMaster.setJoMasterId(theJomaster.getJoMasterId());
			aSession.update(aJoMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoMaster;
	}

	@Override
	public Jomaster updateAddendums(Jomaster theJomaster) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Jomaster aJoMaster = null;
		try {
			if(theJomaster!=null && theJomaster.getJoMasterId()!=null){
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			if(aJoMaster!=null){
			aJoMaster.setAddendumReceived(theJomaster.getAddendumReceived());
			aJoMaster
					.setAddendumQuotedThru(theJomaster.getAddendumQuotedThru());
			aJoMaster.setJoMasterId(theJomaster.getJoMasterId());
			aSession.update(aJoMaster);
			aTransaction.commit();
			}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoMaster;
	}

	@Override
	public Integer getJoMasterId(String thejobNumber) throws JobException {
		Integer aJoMasterId = null;
		String aSelectQry = "SELECT joMasterId FROM joMaster where jobNumber= '"
				+ thejobNumber + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aJoMasterId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJoMasterId;
	}

	@Override
	public Integer getrxMasterId(Integer thejoBidder) throws JobException {
		Integer aJoMasterId = null;
		String aSelectQry = "SELECT rxMasterId FROM joBidder where joBidderId = '"
				+ thejoBidder + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aJoMasterId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJoMasterId;
	}

	@Override
	public List<?> getJobpage(Integer theJomasterId, String theTerm)
			throws JobException {
		String aSelectQry = "SELECT jobNumber,description FROM joMaster where joMasterId= '"
				+ theJomasterId + "'";
		Session aSession = null;
		String aJobnumber = null;
		String aJobName = null;
		List<?> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aJobnumber = (String) aObj[0];
				aJobName = (String) aObj[1];
			}
			aQueryList = getjobSearchResultsJob(aJobnumber, aJobName);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;aJobnumber=null;aJobName=null;
		}
		return aQueryList;
	}

	@Override
	public Integer getFirstLastJomastID(String aTerm) throws JobException {
		String aSelectQry = null;
		Session aSession = null;
		Integer aJoMasterID = null;
		try {
			aSession = itsSessionFactory.openSession();
			if (aTerm.equals("next")) {
				aSelectQry = "SELECT joMasterId from joMaster ORDER BY joMasterId DESC LIMIT 0,1";
			} else if (aTerm.equals("previous")) {
				aSelectQry = "SELECT joMasterId from joMaster ORDER BY joMasterId ASC LIMIT 0,1";
			}
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aJoMasterID = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJoMasterID;
	}

	@Override
	public List<JoQuoteHeader> getQuotesHeaderDetails(Integer theJoQuoteHeaderID)
			throws JobException {
		String aJobSelectQry = "SELECT Remarks, DiscountedPrice,PrintTotal,QuoteAmount FROM joQuoteHeader WHERE joQuoteHeaderID="
				+ theJoQuoteHeaderID;
		Session aSession = null;
		ArrayList<JoQuoteHeader> aQueryList = new ArrayList<JoQuoteHeader>();
		try {
			JoQuoteHeader aJobQuotesBidListBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuotesBidListBean = new JoQuoteHeader();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuotesBidListBean.setRemarks((String) aObj[0]);
				aJobQuotesBidListBean.setDiscountedPrice((BigDecimal) aObj[1]);
				aJobQuotesBidListBean.setPrintTotal((Byte) aObj[2]);
				if(aObj[3]==null){
					aObj[3]="";
				}
				aJobQuotesBidListBean.setQuoteAmount(ConvertintoBigDecimal(aObj[3].toString()).setScale(2));
				aQueryList.add(aJobQuotesBidListBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<JoQuotetemplateHeader> getQuotesTemplateHeaderDetails(
			Integer theJoQuoteHeaderID) throws JobException {
		String aJobSelectQry = "SELECT Remarks, DiscountedPrice FROM joQuoteTemplateHeader WHERE joQuoteTemplateHeaderID="
				+ theJoQuoteHeaderID;
		Session aSession = null;
		ArrayList<JoQuotetemplateHeader> aQueryList = new ArrayList<JoQuotetemplateHeader>();
		try {
			JoQuotetemplateHeader aJobQuotesBidListBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuotesBidListBean = new JoQuotetemplateHeader();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuotesBidListBean.setRemarks((String) aObj[0]);
				aJobQuotesBidListBean.setDiscountedPrice((BigDecimal) aObj[1]);
				aQueryList.add(aJobQuotesBidListBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean updateQuoteDetails(JoQuoteHeader theJoQuoteHeader)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		JoQuoteHeader aQuoteHeader = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aQuoteHeader = (JoQuoteHeader) aSession.get(JoQuoteHeader.class,
					theJoQuoteHeader.getJoQuoteHeaderId());
			aQuoteHeader.setQuoteAmount(theJoQuoteHeader.getQuoteAmount());
			aQuoteHeader.setCostAmount(theJoQuoteHeader.getCostAmount());
			aQuoteHeader.setJoQuoteHeaderId(theJoQuoteHeader
					.getJoQuoteHeaderId());
			aSession.update(aQuoteHeader);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public Jomaster updateAmount(Jomaster theJomaster) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Jomaster thejoMaster = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			thejoMaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			thejoMaster.setContractAmount(theJomaster.getContractAmount());
			thejoMaster.setEstimatedCost(theJomaster.getEstimatedCost());
			thejoMaster.setEstimatedProfit(theJomaster.getEstimatedProfit());
			thejoMaster.setJoMasterId(theJomaster.getJoMasterId());
			aSession.update(thejoMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return thejoMaster;
	}

	@Override
	public JoQuoteDetail getSingleQuoteDetails(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aSession = null;
		JoQuoteDetail aJoQuoteDetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			aJoQuoteDetail = (JoQuoteDetail) aSession.get(JoQuoteDetail.class,
					theJoQuoteDetail.getJoQuoteDetailId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuoteDetail;
	}

	/*
	 * @getQuotePositionMaxValue
	 */
	@Override
	public int getQuotePositionMaxValue(Integer theJoQuoteHeader)
			throws JobException {
		String joQuoteMaxPosition = "select MAX(position) from joQuoteDetail where joQuoteHeaderID= "
				+ theJoQuoteHeader;
		Session aSession = null;
		double joQuoteMaxPositionVal = 0.0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(joQuoteMaxPosition);
			List<?> aList = aQuery.list();
			joQuoteMaxPositionVal = (Double) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			joQuoteMaxPosition=null;
		}
		return (int) joQuoteMaxPositionVal;
	}

	@Override
	public int getQuoteTempPositionMaxValue(Integer theJoQuoteHeader)
			throws JobException {
		String joQuoteMaxPosition = "select MAX(POSITION) from joQuoteTemplateDetail where joQuoteTemplateHeaderID= "
				+ theJoQuoteHeader;
		Session aSession = null;
		double joQuoteMaxPositionVal = 0.0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(joQuoteMaxPosition);
			List<?> aList = aQuery.list();
			joQuoteMaxPositionVal = (Double) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			joQuoteMaxPosition=null;
		}
		return (int) joQuoteMaxPositionVal;
	}

	
	@Override
	public JoQuoteDetail updateItemPosition(JoQuoteDetail theJoQuoteDetail)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		JoQuoteDetail aJoQuoteDetail = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoQuoteDetail = (JoQuoteDetail) aSession.get(JoQuoteDetail.class,
					theJoQuoteDetail.getJoQuoteDetailId());
			aJoQuoteDetail.setPosition(theJoQuoteDetail.getPosition());
			aJoQuoteDetail.setJoQuoteDetailId(theJoQuoteDetail
					.getJoQuoteDetailId());
			aSession.update(aJoQuoteDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuoteDetail;
	}

	@Override
	public JoQuoteTemplateDetail updateQuoteTempItemPosition(JoQuoteTemplateDetail theJoQuoteTemplateDetail)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		JoQuoteTemplateDetail aJoQuoteTemplateDetail = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJoQuoteTemplateDetail = (JoQuoteTemplateDetail) aSession.get(JoQuoteTemplateDetail.class,
					theJoQuoteTemplateDetail.getJoQuoteTemplateDetailId());
			aJoQuoteTemplateDetail.setPosition(theJoQuoteTemplateDetail.getPosition());
			aJoQuoteTemplateDetail.setJoQuoteTemplateDetailId(theJoQuoteTemplateDetail
					.getJoQuoteTemplateDetailId());
			aSession.update(aJoQuoteTemplateDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuoteTemplateDetail;
	}
	
	@Override
	public JoQuoteDetail updateInlineItemPosition(
			JoQuoteDetail theJoQuoteDetail, String operation,
			Integer difference, Integer endQuoteDetailID) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		JoQuoteDetail aJoQuoteDetail = null;
		JoQuoteDetail theLastJoQuoteDetail = null;
		JoQuoteDetail theFirstJoQuoteDetail = null;
		String aSelectQry = "";
		if (operation.equalsIgnoreCase("upwards")) {
			itsLogger.info("Operation - Upwards");
			aSelectQry = "FROM JoQuoteDetail where joQuoteHeaderID ="
					+ theJoQuoteDetail.getJoQuoteHeaderID()
					+ " AND position BETWEEN " + endQuoteDetailID + " AND "
					+ theJoQuoteDetail.getJoQuoteDetailId()
					+ " ORDER BY position";
		}
		if (operation.equalsIgnoreCase("downwards")) {
			itsLogger.info("Operation - DownWards");
			aSelectQry = "FROM JoQuoteDetail where joQuoteHeaderID ="
					+ theJoQuoteDetail.getJoQuoteHeaderID()
					+ " AND position BETWEEN "
					+ theJoQuoteDetail.getJoQuoteDetailId() + " AND "
					+ endQuoteDetailID + " ORDER BY position";
		}
		try {
			Transaction aTransaction = aSession.beginTransaction();
			Query aQuery = aSession.createQuery(aSelectQry);

			// aQuery.setMaxResults(difference);

			List<JoQuoteDetail> aQuteDetails = aQuery.list();
			itsLogger.info("Size of the List : " + aQuteDetails.size());
			int sizeOfQuoteDetails = 0;
			int jval = 0;
			if (operation.equalsIgnoreCase("downwards")) {
				sizeOfQuoteDetails = aQuteDetails.size();
				jval = 1;
			}
			if (operation.equalsIgnoreCase("upwards")) {
				sizeOfQuoteDetails = aQuteDetails.size() - 1;
				jval = 0;
			}
			for (int j = jval; j < sizeOfQuoteDetails; j++) {
				if (operation.equalsIgnoreCase("downwards")) {
					aTransaction.begin();
					aJoQuoteDetail = (JoQuoteDetail) aSession.get(
							JoQuoteDetail.class, aQuteDetails.get(j)
									.getJoQuoteDetailId());
					itsLogger.info("Quote Downwards Detail ID: "
							+ aQuteDetails.get(j).getJoQuoteDetailId());
					itsLogger.info("Before Position: "
							+ aQuteDetails.get(j).getPosition());
					aJoQuoteDetail.setPosition((aQuteDetails.get(j)
							.getPosition()) - 1.0);
					itsLogger.info("After Position: "
							+ aJoQuoteDetail.getPosition());
					aSession.update(aJoQuoteDetail);
					aTransaction.commit();
				}
				if (operation.equalsIgnoreCase("upwards")) {
					aTransaction.begin();
					aJoQuoteDetail = (JoQuoteDetail) aSession.get(
							JoQuoteDetail.class, aQuteDetails.get(j)
									.getJoQuoteDetailId());
					itsLogger.info("Quote Upwards Detail ID: "
							+ aQuteDetails.get(j).getJoQuoteDetailId());
					itsLogger.info("Before Position: "
							+ aQuteDetails.get(j).getPosition());
					aJoQuoteDetail.setPosition((aQuteDetails.get(j)
							.getPosition()) + 1.0);
					itsLogger.info("After Position: "
							+ aJoQuoteDetail.getPosition());
					aSession.update(aJoQuoteDetail);
					aTransaction.commit();
				}
			}
			if (operation.equalsIgnoreCase("downwards")) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				theLastJoQuoteDetail = aQuteDetails
						.get(aQuteDetails.size() - 1);
				theFirstJoQuoteDetail = aQuteDetails.get(0);
				aJoQuoteDetail = (JoQuoteDetail) aSession.get(
						JoQuoteDetail.class,
						theFirstJoQuoteDetail.getJoQuoteDetailId());
				itsLogger.info("Quote Detail ID: "
						+ theFirstJoQuoteDetail.getJoQuoteDetailId());
				aJoQuoteDetail.setPosition(theFirstJoQuoteDetail.getPosition()
						+ difference);
				itsLogger.info("New Position of The Current Value : "
						+ aJoQuoteDetail.getPosition());
				aSession.update(aJoQuoteDetail);
				aTransaction.commit();
			}
			if (operation.equalsIgnoreCase("upwards")) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				theLastJoQuoteDetail = aQuteDetails
						.get(aQuteDetails.size() - 1);
				theFirstJoQuoteDetail = aQuteDetails.get(0);
				itsLogger.info("Quote Detail ID: "
						+ theLastJoQuoteDetail.getJoQuoteDetailId());
				aJoQuoteDetail = (JoQuoteDetail) aSession.get(
						JoQuoteDetail.class,
						theLastJoQuoteDetail.getJoQuoteDetailId());
				aJoQuoteDetail.setPosition((theLastJoQuoteDetail.getPosition())
						- difference);
				aSession.update(aJoQuoteDetail);
				aTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJoQuoteDetail;
	}

	@Override
	public boolean updateLineItemUpDownPosition(
			joQuoteDetailPosition theJoQuoteDetailPosition) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			JoQuoteDetail aJoQuoteDetail = (JoQuoteDetail) aSession.get(
					JoQuoteDetail.class,
					theJoQuoteDetailPosition.getSelectedQuoteDetailID());
			aJoQuoteDetail.setPosition(theJoQuoteDetailPosition
					.getAbovePositionDetailID());
			aSession.update(aJoQuoteDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public boolean updateLineItemUpPosition(
			joQuoteDetailPosition theoQuoteDetailPosition) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			JoQuoteDetail aJoQuoteDetail = (JoQuoteDetail) aSession.get(
					JoQuoteDetail.class,
					theoQuoteDetailPosition.getAboveQuoteDetailID());
			aJoQuoteDetail.setPosition(theoQuoteDetailPosition
					.getSelectedPositionDetailID());
			aSession.update(aJoQuoteDetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public int getRecordCount(String theJobsTotal) throws JobException {
		String aJobCountStr = "SELECT COUNT(joMasterID) AS count FROM joMaster "
				+ theJobsTotal;
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr=null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public ArrayList<JobCustomerBean> getAdvancedSearchResults(int theFrom,
			int theTo, String theJobsTotal, String theSortBy,String sort)
			throws JobException {
		if (theJobsTotal.equals(" WHERE ")) {
			theJobsTotal = "";
		}

String aJobSelectQry = "SELECT joMaster.joMasterId, joMaster.jobNumber, joMaster.Description, joMaster.locationName, joMaster.locationCity, ts.Initials as salesman, "
				+ "joStatus.JobStatus, rx.Name, joMaster.CustomerPONumber, coDivision.Code, joMaster.SourceReport1, joMaster.bookedDate, joMaster.bidDate, joMaster.contractAmount, "
				+ "joMaster.estimatedCost, coDivision.Description as division, ts5.Initials as takeoff, ts6.Initials as quoteBy, IFNULL(rxm.Name,'') as bidList, COUNT(joMaster.jobNumber), "
				+ " joMaster.LocationState FROM joMaster as joMaster "
				+ "LEFT JOIN joStatus as joStatus on joStatus.jostatusID = joMaster.jobstatus "
				+ "LEFT JOIN coDivision as coDivision on joMaster.coDivisionID = coDivision.coDivisionID "
				+ "LEFT JOIN joBidder as j  ON j.joMasterID= joMaster.joMasterID "
				+ "LEFT JOIN rxMaster as rx ON j.rxMasterID = rx.rxMasterID "
				+ "LEFT JOIN rxMaster AS rxm ON joMaster.rxCustomerID = rxm.rxMasterID "
				+ "LEFT JOIN tsUserLogin as ts ON joMaster.cuAssignmentID0 = ts.UserLoginID "
				+ "LEFT JOIN tsUserLogin as ts5 ON joMaster.cuAssignmentID5 = ts5.UserLoginID "
				+ "LEFT JOIN tsUserLogin as ts6 ON joMaster.cuAssignmentID6 = ts6.UserLoginID  "
				+ theJobsTotal
				+ " GROUP BY joMaster.jobNumber ORDER BY "
				+ theSortBy + " "+sort;
		if (aJobSelectQry.contains("AND ORDER BY")) {
			aJobSelectQry = aJobSelectQry.replace("AND ORDER BY", "ORDER BY");
		}
		if (aJobSelectQry.contains("AND GROUP BY")) {
			aJobSelectQry = aJobSelectQry.replace("AND GROUP BY", "GROUP BY");
		}
		Session aSession = null;
		ArrayList<JobCustomerBean> aQueryList = new ArrayList<JobCustomerBean>();
		try {
			JobCustomerBean aJobCustomerBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			/**
			 * In general you are not supposed to get the column names.
			 * Actually, that's exactly what an O2R mapper is about - hiding the
			 * R details and showing only O specifics.
			 **/
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobCustomerBean = new JobCustomerBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobCustomerBean.setJoMasterId((Integer) aObj[0]);
				/** joMasterId */
				aJobCustomerBean.setJobNumber((String) aObj[1]);
				/** jobNumber */
				aJobCustomerBean.setDescription((String) aObj[2]);
				/** Description */
				aJobCustomerBean.setLocationName((String) aObj[3]);
				
				/** locationName */
				// Edited By: Naveed 20 Oct 2015  ID #427
				String state ="";
				if(aObj[20] !=null){
					if( !aObj[20].equals(""))
						state=","+(String)aObj[20];
				}
				aJobCustomerBean.setLocationCity((String) aObj[4] + state);
				
				/** locationCity */
				aJobCustomerBean.setInitials((String) aObj[5]);
				/** Initials */
				aJobCustomerBean.setJobStatus((String) aObj[6]);
				/** JobStatus */
				aJobCustomerBean.setCustomerName((String) aObj[7]);
				/** Name */
				aJobCustomerBean.setCustomerPONumber((String) aObj[8]);
				/** CustomerPONumber */
				aJobCustomerBean.setCode((String) aObj[9]);
				/** Code */
				aJobCustomerBean.setSourceReport1((String) aObj[10]);
				/** SourceReport1 */
				if (aObj[11] != null) {
					aJobCustomerBean.setBookedDate((String) DateFormatUtils
							.format((Date) aObj[11], "MM/dd/yyyy hh:mm a"));
					/** bookedDate */
				}
				if (aObj[12] != null) {
					aJobCustomerBean.setBidDate((String) DateFormatUtils
							.format((Date) aObj[12], "MM/dd/yyyy"));
				}
				aJobCustomerBean.setDivision((String) aObj[15]);
				aJobCustomerBean.setTakeoff((String) aObj[16]);
				aJobCustomerBean.setQuoteBy((String) aObj[17]);
				aJobCustomerBean.setBidList((String) aObj[18]);
				aJobCustomerBean.setBidListCount((BigInteger) aObj[19]);
				aQueryList.add(aJobCustomerBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean updateLastQuoteAndRev(Jobidder theJobidder)
			throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Jobidder aJobidder = (Jobidder) aSession.get(Jobidder.class,
					theJobidder.getJoBidderId());
			aJobidder.setQuoteRev(theJobidder.getQuoteRev());
			// aJobidder.setQuoteDate(theJobidder.getQuoteDate());
			aJobidder.setQuoteDate(new Date());
			aJobidder.setJoBidderId(theJobidder.getJoBidderId());
			aJobidder.setQuoteemailstatus(theJobidder.getQuoteemailstatus());
			aSession.update(aJobidder);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public JoQuoteHeader getSingleQuoteHeaderDetails(Integer theJoQuoteHeaderID)
			throws JobException {
		Session aSession = null;
		JoQuoteHeader aJoQuoteHeader = null;
		try {
			aSession = itsSessionFactory.openSession();
			aJoQuoteHeader = (JoQuoteHeader) aSession.get(JoQuoteHeader.class,
					theJoQuoteHeaderID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuoteHeader;
	}

	@Override
	public boolean deleteMainJob(Jomaster theJomaster) throws JobException {
		Session aJoMasterSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			deleteJoBidderRecords(theJomaster);
			deleteJoQuoteRecords(theJomaster);
			deleteJoSubmittedRecords(theJomaster);
			deleteJoSubmittedHeaderRecords(theJomaster);
			deleteSearchIndex(theJomaster);
			deleteRecentlyOpendJob(theJomaster);
			aTransaction = aJoMasterSession.beginTransaction();
			aTransaction.begin();
			aJoMasterSession.delete(theJomaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoMasterSession.flush();
			aJoMasterSession.close();
		}
		return false;
	}

	private boolean deleteJoSubmittedHeaderRecords(Jomaster theJomaster)
			throws JobException {
		List<Josubmittalheader> aJosubmittals = getJoSubmittalHeaderDetails(theJomaster);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Josubmittalheader aJosubmittal = null;
		try {
			for (int index = 0; index < aJosubmittals.size(); index++) {
				aJosubmittal = (Josubmittalheader) aSearchSession.get(
						Josubmittalheader.class, aJosubmittals.get(index)
								.getJoSubmittalHeaderId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aJosubmittal);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<Josubmittalheader> getJoSubmittalHeaderDetails(
			Jomaster theJomaster) throws JobException {
		String aJobSelectQry = "SELECT joSubmittalHeaderID, SubmittalByID FROM joSubmittalHeader WHERE joMasterID = "
				+ theJomaster.getJoMasterId();
		Session aSession = null;
		ArrayList<Josubmittalheader> aQueryList = new ArrayList<Josubmittalheader>();
		try {
			Josubmittalheader aJosubmittal = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJosubmittal = new Josubmittalheader();
				Object[] aObj = (Object[]) aIterator.next();
				aJosubmittal.setJoSubmittalHeaderId((Integer) aObj[0]);
				aJosubmittal.setSubmittalById((Integer) aObj[1]);
				aQueryList.add(aJosubmittal);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	private boolean deleteJoSubmittedRecords(Jomaster theJomaster)
			throws JobException {
		List<Josubmittal> aJosubmittals = getJoSubmittalDetails(theJomaster);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Josubmittal aJosubmittal = null;
		try {
			for (int index = 0; index < aJosubmittals.size(); index++) {
				aJosubmittal = (Josubmittal) aSearchSession.get(
						Josubmittal.class, aJosubmittals.get(index)
								.getJoSubmittalId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aJosubmittal);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<Josubmittal> getJoSubmittalDetails(Jomaster theJomaster)
			throws JobException {
		String aJobSelectQry = "SELECT joSubmittalID, rxManufacturerID FROM joSubmittal WHERE joMasterID = "
				+ theJomaster.getJoMasterId();
		Session aSession = null;
		ArrayList<Josubmittal> aQueryList = new ArrayList<Josubmittal>();
		try {
			Josubmittal aJosubmittal = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJosubmittal = new Josubmittal();
				Object[] aObj = (Object[]) aIterator.next();
				aJosubmittal.setJoSubmittalId((Integer) aObj[0]);
				aJosubmittal.setRxManufacturerId((Integer) aObj[1]);
				aQueryList.add(aJosubmittal);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	private boolean deleteJoQuoteRecords(Jomaster theJomaster)
			throws JobException {
		List<JoQuoteHeader> aJoQuoteHeaders = getJoQuoteHeaderDetails(theJomaster);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		JoQuoteHeader aJoQuoteHeader = null;
		try {
			for (int aIndexDetail = 0; aIndexDetail < aJoQuoteHeaders.size(); aIndexDetail++) {
				ArrayList<JoQuoteDetail> aJoQuoteDetailList = getJoQuoteDetailID(aJoQuoteHeaders
						.get(aIndexDetail).getJoQuoteHeaderId());
				JoQuoteDetail aJoQuoteDetail = new JoQuoteDetail();
				for (int aIndex1 = 0; aIndex1 < aJoQuoteDetailList.size(); aIndex1++) {
					int ajoQuoteDetailID = aJoQuoteDetailList.get(aIndex1)
							.getJoQuoteDetailId();
					aJoQuoteDetail.setJoQuoteDetailId(ajoQuoteDetailID);
					deleteProductQuote(aJoQuoteDetail);
				}
			}
			for (int aIndex = 0; aIndex < aJoQuoteHeaders.size(); aIndex++) {
				aJoQuoteHeader = (JoQuoteHeader) aSearchSession.get(
						JoQuoteHeader.class, aJoQuoteHeaders.get(aIndex)
								.getJoQuoteHeaderId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aJoQuoteHeader);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<JoQuoteHeader> getJoQuoteHeaderDetails(Jomaster theJomaster)
			throws JobException {
		String aJobSelectQry = "SELECT joQuoteHeaderID, cuMasterTypeID FROM joQuoteHeader WHERE joMasterID = "
				+ theJomaster.getJoMasterId();
		Session aSession = null;
		ArrayList<JoQuoteHeader> aQueryList = new ArrayList<JoQuoteHeader>();
		try {
			JoQuoteHeader aQuoteHeader = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aQuoteHeader = new JoQuoteHeader();
				Object[] aObj = (Object[]) aIterator.next();
				aQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
				aQuoteHeader.setCuMasterTypeID((Integer) aObj[1]);
				aQueryList.add(aQuoteHeader);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	private boolean deleteJoBidderRecords(Jomaster theJomaster)
			throws JobException {
		List<Jobidder> aJobidders = getJoBidderDetails(theJomaster);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Jobidder aJobidder = null;
		try {
			for (int aIndex = 0; aIndex < aJobidders.size(); aIndex++) {
				aJobidder = (Jobidder) aSearchSession.get(Jobidder.class,
						aJobidders.get(aIndex).getJoBidderId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aJobidder);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<Jobidder> getJoBidderDetails(Jomaster theJomaster)
			throws JobException {
		String aJobSelectQry = "SELECT joBidderID, rxContactID FROM joBidder WHERE joMasterID = "
				+ theJomaster.getJoMasterId();
		Session aSession = null;
		ArrayList<Jobidder> aQueryList = new ArrayList<Jobidder>();
		try {
			Jobidder aJobidder = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobidder = new Jobidder();
				Object[] aObj = (Object[]) aIterator.next();
				aJobidder.setJoBidderId((Integer) aObj[0]);
				aJobidder.setRxContactId((Integer) aObj[1]);
				aQueryList.add(aJobidder);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	public boolean deleteSearchIndex(Jomaster theJoMaster) throws JobException {
		List<search_index> aSearchBeans = getJobDetails(theJoMaster);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		search_index aSearchBean = null;
		try {
			for (int index = 0; index < aSearchBeans.size(); index++) {
				aSearchBean = (search_index) aSearchSession.get(
						search_index.class, aSearchBeans.get(index)
								.getSearchId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aSearchBean);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	public List<search_index> getJobDetails(Jomaster theJoMaster)
			throws JobException {
		String aJobSelectQry = "SELECT searchId, searchText FROM search_index WHERE pk_fields = "
				+ theJoMaster.getJoMasterId()
				+ " AND resultedTableName like '%joMaster%' AND entity like '%Job%'";
		Session aSession = null;
		ArrayList<search_index> aQueryList = new ArrayList<search_index>();
		try {
			search_index aJobQuotesBidListBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobQuotesBidListBean = new search_index();
				Object[] aObj = (Object[]) aIterator.next();
				aJobQuotesBidListBean.setSearchId((Integer) aObj[0]);
				aJobQuotesBidListBean.setSearchText((String) aObj[1]);
				aQueryList.add(aJobQuotesBidListBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	public boolean deleteRecentlyOpendJob(Jomaster theJoMaster)
			throws JobException {
		List<JobHistory> aJobHistories = getJobHistoryDetails(theJoMaster);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		JobHistory aJobHistory = null;
		try {
			for (int aIndex = 0; aIndex < aJobHistories.size(); aIndex++) {
				aJobHistory = (JobHistory) aSearchSession.get(JobHistory.class,
						aJobHistories.get(aIndex).getJobHistoryID());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aJobHistory);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<JobHistory> getJobHistoryDetails(Jomaster theJoMaster)
			throws JobException {
		String aJobSelectQry = "SELECT jobHistoryID, jobNumber FROM jobHistory WHERE jobNumber like '%"
				+ JobUtil.removeSpecialcharacterswithslash(theJoMaster.getJobNumber()) + "%'";
		Session aSession = null;
		ArrayList<JobHistory> aQueryList = new ArrayList<JobHistory>();
		try {
			JobHistory aJobHistory = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobHistory = new JobHistory();
				Object[] aObj = (Object[]) aIterator.next();
				aJobHistory.setJobHistoryID((Integer) aObj[0]);
				aJobHistory.setJobNumber((String) aObj[1]);
				aQueryList.add(aJobHistory);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public JoQuoteHeader updateQuoteProperties(
			JoQuoteProperties theJoQuoteProperty,
			JoQuoteTemplateProperties thejoQuoteTempProperties,
			Boolean isQuoteTempProperty) throws JobException {
		itsLogger.debug("updateQuoteProperties.");
		Session aPropertySessioin = itsSessionFactory.openSession();
		JoQuoteHeader aQuoteHeader = null;
		JoQuoteProperties aProperty = null;
		JoQuoteTemplateProperties aTempProperty = null;
		Transaction aTransaction = null;
		try {
			aTransaction = aPropertySessioin.beginTransaction();
			aTransaction.begin();
			if (isQuoteTempProperty) {
				aTempProperty = getuserquoteTemplateProperty(thejoQuoteTempProperties
						.getUserLoginID());
				if (aTempProperty == null) {
					aPropertySessioin.save(thejoQuoteTempProperties);
				} else {
					aTempProperty.setDisplayQuantity(thejoQuoteTempProperties
							.getDisplayQuantity());
					aTempProperty.setPrintQuantity(thejoQuoteTempProperties
							.getPrintQuantity());
					aTempProperty.setDisplayParagraph(thejoQuoteTempProperties
							.getDisplayParagraph());
					aTempProperty.setPrintParagraph(thejoQuoteTempProperties
							.getPrintParagraph());
					aTempProperty
							.setDisplayManufacturer(thejoQuoteTempProperties
									.getDisplayManufacturer());
					aTempProperty.setPrintManufacturer(thejoQuoteTempProperties
							.getPrintManufacturer());
					aTempProperty.setDisplaySpec(thejoQuoteTempProperties
							.getDisplaySpec());
					aTempProperty.setPrintSpec(thejoQuoteTempProperties
							.getPrintSpec());
					aTempProperty.setDisplayCost(thejoQuoteTempProperties
							.getDisplayCost());
					aTempProperty.setDisplayMult(thejoQuoteTempProperties
							.getDisplayMult());
					aTempProperty.setDisplayPrice(thejoQuoteTempProperties
							.getDisplayPrice());
					aTempProperty.setPrintPrice(thejoQuoteTempProperties
							.getPrintPrice());
					aTempProperty.setPrintCost(thejoQuoteTempProperties
							.getPrintCost());
					aTempProperty.setPrintMult(thejoQuoteTempProperties
							.getPrintMult());
					aTempProperty.setNotesFullWidth(thejoQuoteTempProperties
							.getNotesFullWidth());
					aTempProperty.setLineNumbers(thejoQuoteTempProperties
							.getLineNumbers());
					aTempProperty.setPrintTotal(thejoQuoteTempProperties
							.getPrintTotal());
					aTempProperty.setHidePrice(thejoQuoteTempProperties
							.getHidePrice());
					aTempProperty.setUnderlineQuantity(thejoQuoteTempProperties
							.getUnderlineQuantity());
					aTempProperty.setBoldQuantity(thejoQuoteTempProperties
							.getBoldQuantity());
					aTempProperty
							.setUnderlineParagraph(thejoQuoteTempProperties
									.getUnderlineParagraph());
					aTempProperty.setBoldParagraph(thejoQuoteTempProperties
							.getBoldParagraph());
					aTempProperty
							.setUnderlineManufacturer(thejoQuoteTempProperties
									.getUnderlineManufacturer());
					aTempProperty.setBoldManufacturer(thejoQuoteTempProperties
							.getBoldManufacturer());
					aTempProperty.setUnderlineSpec(thejoQuoteTempProperties
							.getUnderlineSpec());
					aTempProperty.setBoldSpec(thejoQuoteTempProperties
							.getBoldSpec());
					aTempProperty.setBoldCost(thejoQuoteTempProperties
							.getBoldCost());
					aTempProperty.setBoldMult(thejoQuoteTempProperties
							.getBoldMult());
					aTempProperty.setUnderlineCost(thejoQuoteTempProperties
							.getUnderlineCost());
					aTempProperty.setUnderlineMult(thejoQuoteTempProperties
							.getUnderlineMult());
					aTempProperty.setUnderlinePrice(thejoQuoteTempProperties
							.getUnderlinePrice());
					aTempProperty.setBoldPrice(thejoQuoteTempProperties
							.getBoldPrice());

					aTempProperty.setDisplayItem(thejoQuoteTempProperties
							.getDisplayItem());
					aTempProperty.setPrintItem(thejoQuoteTempProperties
							.getPrintItem());
					aTempProperty.setUnderlineItem(thejoQuoteTempProperties
							.getUnderlineItem());
					aTempProperty.setBoldItem(thejoQuoteTempProperties
							.getBoldItem());

					aTempProperty.setDisplayHeader(thejoQuoteTempProperties
							.getDisplayHeader());
					aTempProperty.setPrintHeader(thejoQuoteTempProperties
							.getPrintHeader());
					aTempProperty.setUnderlineHeader(thejoQuoteTempProperties
							.getUnderlineHeader());
					aTempProperty.setBoldHeader(thejoQuoteTempProperties
							.getBoldHeader());
					aPropertySessioin.update(aTempProperty);
				}
			} else {
				aProperty = getuserquoteProperty(theJoQuoteProperty
						.getUserLoginID());
				if (aProperty == null) {
					aPropertySessioin.save(theJoQuoteProperty);
				} else {
					aProperty.setDisplayQuantity(theJoQuoteProperty
							.getDisplayQuantity());
					aProperty.setPrintQuantity(theJoQuoteProperty
							.getPrintQuantity());
					aProperty.setDisplayParagraph(theJoQuoteProperty
							.getDisplayParagraph());
					aProperty.setPrintParagraph(theJoQuoteProperty
							.getPrintParagraph());
					aProperty.setDisplayManufacturer(theJoQuoteProperty
							.getDisplayManufacturer());
					aProperty.setPrintManufacturer(theJoQuoteProperty
							.getPrintManufacturer());
					aProperty.setDisplaySpec(theJoQuoteProperty
							.getDisplaySpec());
					aProperty.setPrintSpec(theJoQuoteProperty.getPrintSpec());
					aProperty.setDisplayCost(theJoQuoteProperty
							.getDisplayCost());
					aProperty.setDisplayMult(theJoQuoteProperty
							.getDisplayMult());
					aProperty.setDisplayPrice(theJoQuoteProperty
							.getDisplayPrice());
					aProperty.setPrintPrice(theJoQuoteProperty.getPrintPrice());
					aProperty.setPrintCost(theJoQuoteProperty.getPrintCost());
					aProperty.setPrintMult(theJoQuoteProperty.getPrintMult());
					aProperty.setNotesFullWidth(theJoQuoteProperty
							.getNotesFullWidth());
					aProperty.setLineNumbers(theJoQuoteProperty
							.getLineNumbers());
					aProperty.setPrintTotal(theJoQuoteProperty.getPrintTotal());
					aProperty.setHidePrice(theJoQuoteProperty.getHidePrice());
					// aProperty.setJoQuoteHeaderId(theJoQuoteProperty.getJoQuoteHeaderId());

					aProperty.setUnderlineQuantity(theJoQuoteProperty
							.getUnderlineQuantity());
					aProperty.setBoldQuantity(theJoQuoteProperty
							.getBoldQuantity());
					aProperty.setUnderlineParagraph(theJoQuoteProperty
							.getUnderlineParagraph());
					aProperty.setBoldParagraph(theJoQuoteProperty
							.getBoldParagraph());
					aProperty.setUnderlineManufacturer(theJoQuoteProperty
							.getUnderlineManufacturer());
					aProperty.setBoldManufacturer(theJoQuoteProperty
							.getBoldManufacturer());
					aProperty.setUnderlineSpec(theJoQuoteProperty
							.getUnderlineSpec());
					aProperty.setBoldSpec(theJoQuoteProperty.getBoldSpec());
					aProperty.setBoldCost(theJoQuoteProperty.getBoldCost());
					aProperty.setBoldMult(theJoQuoteProperty.getBoldMult());
					aProperty.setUnderlineCost(theJoQuoteProperty
							.getUnderlineCost());
					aProperty.setUnderlineMult(theJoQuoteProperty
							.getUnderlineMult());
					aProperty.setUnderlinePrice(theJoQuoteProperty
							.getUnderlinePrice());
					aProperty.setBoldPrice(theJoQuoteProperty.getBoldPrice());

					aProperty.setDisplayItem(theJoQuoteProperty
							.getDisplayItem());
					aProperty.setPrintItem(theJoQuoteProperty.getPrintItem());
					aProperty.setUnderlineItem(theJoQuoteProperty
							.getUnderlineItem());
					aProperty.setBoldItem(theJoQuoteProperty.getBoldItem());

					aProperty.setDisplayHeader(theJoQuoteProperty
							.getDisplayHeader());
					aProperty.setPrintHeader(theJoQuoteProperty
							.getPrintHeader());
					aProperty.setUnderlineHeader(theJoQuoteProperty
							.getUnderlineHeader());
					aProperty.setBoldHeader(theJoQuoteProperty.getBoldHeader());
					aPropertySessioin.update(aProperty);
				}

			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aPropertySessioin.flush();
			aPropertySessioin.close();
		}
		return aQuoteHeader;
	}

	@Override
	public Jomaster updateORIJobNumber(Jomaster theJomaster)
			throws JobException {
		itsLogger.debug("Update ORI JobNumber in Main page.");
		Session aSession = itsSessionFactory.openSession();
		Jomaster aJomaster = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aJomaster = (Jomaster) aSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			if (aJomaster.getQuoteNumber() != null
					&& !aJomaster.getQuoteNumber().equals("")) {
				itsLogger.info("Already Quote Updated");
			} else {
				aJomaster.setQuoteNumber(theJomaster.getQuoteNumber());
			}
			aJomaster.setJobNumber(theJomaster.getJobNumber());
			aJomaster.setJoMasterId(theJomaster.getJoMasterId());
			aJomaster.setSeqnum(theJomaster.getSeqnum());
			aSession.update(aJomaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJomaster;
	}
	@Override
	public Integer addvePOfromInsideJob(Vepo theVepo, JoRelease aJoRelease,String theJobNumber,Integer thecuSOID){
		Session aJoReleaseSession = itsSessionFactory.openSession();
		Vepo aVepo = new Vepo();
		Transaction aTransaction;
		Integer VepoID=0;
		try {
			if(aJoRelease.getJoReleaseId()>0){
			String getInvoiceIDQuery = "SELECT vePOID FROM vePO WHERE joReleaseID = "
					+ aJoRelease.getJoReleaseId();
			Query aQuery = aJoReleaseSession.createSQLQuery(getInvoiceIDQuery);
			List<?> aList = aQuery.list();
			if(aList.size()>0){	
				VepoID = (Integer) aList.get(0);
			}
			if(VepoID>0){
				aTransaction = aJoReleaseSession.beginTransaction();
				aTransaction.begin();
				aVepo = (Vepo) aJoReleaseSession.get(Vepo.class, VepoID);
				aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
				aVepo.setOrderedById(theVepo.getOrderedById());
				aVepo.setRxVendorId(theVepo.getRxVendorId());
				//aVepo.setRxBillToId(theVepo.getRxBillToId());
				//aVepo.setRxBillToAddressId(theVepo.getRxBillToAddressId());
				//aVepo.setRxShipToId(theVepo.getRxShipToId());
				//aVepo.setRxShipToAddressId(theVepo.getRxShipToAddressId());
				aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
				//aVepo.setShipToMode(theVepo.getShipToMode());
				//aVepo.setShipTo(theVepo.getShipTo());
				//aVepo.setBillToIndex(theVepo.getBillToIndex());
				aVepo.setChangedById(theVepo.getOrderedById());
				aVepo.setChangedOn(new Date());
				aJoReleaseSession.update(aVepo);
				aTransaction.commit();
			}
			else{
				aTransaction = aJoReleaseSession.beginTransaction();
				aTransaction.begin();
				JoRelease aJoReleaseobj = (JoRelease) aJoReleaseSession.get(JoRelease.class, aJoRelease.getJoReleaseId());
				Integer alphabetseqnum=aJoReleaseobj.getSeq_Number();
				aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
				aVepo.setPonumber(theJobNumber+JobUtil.IntToLetter(alphabetseqnum).toUpperCase());
				aVepo.setOrderedById(theVepo.getOrderedById());
				aVepo.setRxShipToId(theVepo.getRxShipToId());
				aVepo.setRxBillToId(theVepo.getRxBillToId());
				aVepo.setRxVendorId(theVepo.getRxVendorId());
				aVepo.setRxVendorId(theVepo.getRxVendorId());
				aVepo.setRxBillToId(theVepo.getRxBillToId());
				aVepo.setRxBillToAddressId(theVepo.getRxBillToAddressId());
				aVepo.setRxShipToId(theVepo.getRxShipToId());
				aVepo.setRxShipToAddressId(theVepo.getRxShipToAddressId());
				aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
				aVepo.setConsignment(false);
				aVepo.setMultipleAcks(false);
				aVepo.setCreatedOn(theVepo.getCreatedOn());
				aVepo.setCreatedById(theVepo.getOrderedById());
				aVepo.setJoReleaseId(aJoRelease.getJoReleaseId());
				aVepo.setBillToIndex(0);
				aVepo.setShipTo(0);
				aVepo.setShipToMode(theVepo.getShipToMode()); 
				aVepo.setTransactionStatus(1);
				VepoID=(Integer) aJoReleaseSession.save(aVepo);
				if(thecuSOID!=null && thecuSOID>0)
				{
				Cuso aCuso= (Cuso) aJoReleaseSession.get(Cuso.class, thecuSOID);	
				if(aCuso!=null)
				aJoReleaseSession.delete(aCuso); 
				}
				aTransaction.commit();
				
			}
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
		} finally {
			aJoReleaseSession.flush();
			aJoReleaseSession.close();
		}
		return VepoID;
	}
	@Override
	public Vepo addRelease(JoRelease theJoRelease,
			JoReleaseDetail theJoReleaseDetail, Vepo theVepo,
			String theJobNumber, Cuso theCuso, Integer theReleaseType)
			throws JobException {
		Session aJoReleaseSession = itsSessionFactory.openSession();
		Integer aJoReleaseId = 0;
		Vepo aVepo = new Vepo();
		Transaction aTransaction;
		try {
			aTransaction = aJoReleaseSession.beginTransaction();
			aTransaction.begin();
			Integer alphabetseqnum=getJoReleaseCountBasedonjomaster(theJoRelease.getJoMasterId())+1;
			theJoRelease.setSeq_Number(alphabetseqnum);
			aJoReleaseId = (Integer) aJoReleaseSession.save(theJoRelease);
			aTransaction.commit();
			theJoReleaseDetail.setJoReleaseId(aJoReleaseId);
			aTransaction = aJoReleaseSession.beginTransaction();
			aTransaction.begin();
			// aJoReleaseDetailSession.save(theJoReleaseDetail);
			aTransaction.commit();
			theVepo.setJoReleaseId(aJoReleaseId);
			theVepo.setPonumber(theJobNumber+JobUtil.IntToLetter(alphabetseqnum).toUpperCase());
			if (theReleaseType == 1 || theReleaseType == 4) {
				aTransaction = aJoReleaseSession.beginTransaction();
				aTransaction.begin();
				int avePOId = (Integer) aJoReleaseSession.save(theVepo);
				aVepo = (Vepo) aJoReleaseSession.get(Vepo.class, avePOId);
				aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
				aVepo.setOrderedById(theVepo.getOrderedById());
				aVepo.setRxShipToId(theVepo.getRxShipToId());
				aVepo.setRxBillToId(theVepo.getRxBillToId());
				aVepo.setRxVendorId(theVepo.getRxVendorId());
				aVepo.setVePoid(theVepo.getVePoid());
				aJoReleaseSession.update(aVepo);
				aTransaction.commit();
			} else if (theReleaseType == 2 || theReleaseType == 5) {
				if(theCuso.getRxCustomerId()!=null){
						Cumaster prMaster = (Cumaster) aJoReleaseSession.get(
						Cumaster.class, theCuso.getRxCustomerId());
				if (prMaster != null && prMaster.getCuTermsId() != null) {
					theCuso.setCuTermsId(prMaster.getCuTermsId());
				}
				}
				theCuso.setSonumber(theJobNumber+JobUtil.IntToLetter(alphabetseqnum).toUpperCase());
				aTransaction = aJoReleaseSession.beginTransaction();
				aTransaction.begin();
				theCuso.setJoReleaseId(aJoReleaseId);
				int cuSOId = (Integer) aJoReleaseSession.save(theCuso);
				aTransaction.commit();
			}else{
				//Bill only no need to insert in to so or po
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoReleaseSession.flush();
			aJoReleaseSession.close();
			
		}
		return aVepo;
	}

	@Override
	public boolean editRelease(JoRelease theJoRelease,
			JoReleaseDetail theJoReleaseDetail, Vepo theVepo)
			throws JobException {
		Session aJoReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		JoRelease aJoRelease = new JoRelease();
		Vepo aVepo = new Vepo();
		try {
			aJoRelease = (JoRelease) aJoReleaseSession.get(JoRelease.class,theJoRelease.getJoReleaseId());
			/*Commented By Jenith for BID#754
			 * aJoRelease.setBillNote(theJoRelease.getBillNote());*/
			aJoRelease.setCancelled(theJoRelease.getCancelled());
			aJoRelease.setCommissionAmount(theJoRelease.getCommissionAmount());
			aJoRelease.setCommissionDate(theJoRelease.getCommissionDate());
			aJoRelease.setCommissionReceived(theJoRelease.getCommissionReceived());
			aJoRelease.setEstimatedBilling(theJoRelease.getEstimatedBilling());
			aJoRelease.setReleaseDate(theJoRelease.getReleaseDate());
			aJoRelease.setReleaseNote(theJoRelease.getReleaseNote());
			aJoRelease.setReleaseType(theJoRelease.getReleaseType());
			aJoRelease.setPoid(theJoRelease.getPoid());
			aTransaction = aJoReleaseSession.beginTransaction();
			aTransaction.begin();
			aJoReleaseSession.update(aJoRelease);
			aTransaction.commit();
			aTransaction = aJoReleaseSession.beginTransaction();
			/*
			 * aTransaction.begin();
			 * aJoReleaseDetailSession.update(theJoReleaseDetail);
			 * aTransaction.commit();
			 */
		if ((aJoRelease.getReleaseType()==1 ||aJoRelease.getReleaseType()==4)  &&null != theVepo.getVePoid()) {
				aVepo = (Vepo) aJoReleaseSession.get(Vepo.class,theVepo.getVePoid());
				aVepo.setRxVendorId(theVepo.getRxVendorId());
				aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
				aTransaction = aJoReleaseSession.beginTransaction();
				aTransaction.begin();
				aJoReleaseSession.update(aVepo);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoReleaseSession.flush();
			aJoReleaseSession.close();
		}
		return true;
	}

	@Override
	public boolean deleteRelease(JoRelease theJoRelease,JoReleaseDetail theJoReleaseDetail, Vepo theVepo, Cuso theCuso)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Vepo aVepo = null;
		Cuso aCuso = null;
		Integer i = 0;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (theVepo.getVePoid() != null) {
				
				String getInvoiceIDQuery = "SELECT veBillID  from veBill where vePOID = "+ theVepo.getVePoid();
				List<Integer> invoiceList = (List<Integer>) aSession.createSQLQuery(getInvoiceIDQuery).list();
				if (!invoiceList.isEmpty()) {
					while (i < invoiceList.size()) {
						Vebill vebill = (Vebill) aSession.get(Vebill.class,	invoiceList.get(i));
						aSession.delete(vebill);
						
						i++;
					}
				}
				aVepo = (Vepo) aSession.get(Vepo.class, theVepo.getVePoid());
				aSession.delete(aVepo);
				
			} else if (theCuso.getCuSoid() != null && theCuso.getCuSoid() != 0) {
				String getInvoiceIDQuery = "SELECT cuInvoiceID FROM cuInvoice WHERE cuSOID = "
						+ theCuso.getCuSoid();
				List<Integer> invoiceList = (List<Integer>) aSession.createSQLQuery(getInvoiceIDQuery).list();
				if (!invoiceList.isEmpty()) {
					while (i < invoiceList.size()) {
						Cuinvoice aCuinvoice = (Cuinvoice) aSession.get(
								Cuinvoice.class, invoiceList.get(i));
					
						aSession.delete(aCuinvoice);
						i++;
					}
				}
			
				aCuso = (Cuso) aSession
						.get(Cuso.class, theCuso.getCuSoid());
				aSession.delete(aCuso);
			}
			if (theJoReleaseDetail.getJoReleaseDetailId() != null) {
			
				JoReleaseDetail aDetail = (JoReleaseDetail) aSession
						.get(JoReleaseDetail.class,theJoReleaseDetail.getJoReleaseDetailId());
				
				if(aDetail!=null)
				aSession.delete(aDetail);
			}
			if (theJoRelease.getJoReleaseId() != null) {
				JoRelease joreleaseobj=getJoRelease(theJoRelease.getJoReleaseId());
				if(joreleaseobj!=null)
				aSession.delete(theJoRelease);
				
			}
			
			
		aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public List<Vepodetail> getPOReleaseLineItem(Integer theVepoID)
			throws JobException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String aPOLineItemListQry = "";
		
		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		BigDecimal aTotal;
		BigDecimal aNetCast;
		try {
			
			if (theVepoID != null) {
				aPOLineItemListQry = "SELECT ve.vePODetailID," + " ve.vePOID, "
						+ " ve.prMasterID, " + " ve.Description,"
						+ " ve.QuantityOrdered, " + " ve.Taxable, "
						+ " ve.UnitCost," + " ve.PriceMultiplier,"
						+ " ve.posistion," + " pr.ItemCode, " + " vepo.TaxTotal, "
						+ " ve.Note, " + " ve.QuantityReceived, "
						+ " ve.AcknowledgementDate, ve.EstimatedShipDate, ve.VendorOrderNumber "+
						/*
						 * " vbd.QuantityBilled, " +
						 * " IF(vbd.QuantityBilled IS NULL,0.0000,vbd.QuantityBilled) AS invoiced "
						 * +
						 */
						"FROM vePODetail ve LEFT JOIN prMaster pr ON ve.prMasterID = pr.prMasterID "
						+ "RIGHT JOIN vePO vepo ON vepo.vePOID = ve.vePOID " +
						/*
						 * "LEFT JOIN veBillDetail vbd ON ve.vePODetailID = vbd.vePOdetailID "
						 * +
						 */
						"WHERE ve.vePOID = " + theVepoID + "  ORDER BY ve.posistion";
			
			
			Vepodetail avepoDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			List<?> aList = aQuery.list();
			if (aPOLineItemListQry.length() > 0)
				if (!aList.isEmpty()) {
					Iterator<?> aIterator = aList.iterator();
					while (aIterator.hasNext()) {
						avepoDetail = new Vepodetail();
						Object[] aObj = (Object[]) aIterator.next();
						avepoDetail.setVePodetailId((Integer) aObj[0]);
						avepoDetail.setVePoid((Integer) aObj[1]);
						avepoDetail.setPrMasterId((Integer) aObj[2]);
						avepoDetail.setDescription((String) aObj[3]);
					
						
						avepoDetail.setQuantityOrdered(aObj[4]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[4]),2):new BigDecimal("0"));
						if ((Byte) aObj[5] == 1) {
							avepoDetail.setTaxable(true);
						} else {
							avepoDetail.setTaxable(false);
						}
						avepoDetail.setUnitCost(aObj[6]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[6]),2):new BigDecimal("0"));
						avepoDetail.setPriceMultiplier(aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0"));
						avepoDetail.setPosistion((Double) aObj[8]);
					
						
						
						String ackDate = "", shipDate = "";
						
						
//						if(aObj[13] != null && aObj[13] != "")
//							System.out.println(" aObj[13] : "+DateUtils.parseDate((String) aObj[13], new String[]{"MM/dd/yyyy"}));
//						if(aObj[14] != null && aObj[14] != "")
//							System.out.println(" aObj[14] : "+DateUtils.parseDate((String) aObj[14], new String[]{"MM/dd/yyyy"}));
						avepoDetail.setNote((String) aObj[9]);
						BigDecimal aUnitCost =aObj[6]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[6]),2):new BigDecimal("0");
						BigDecimal aPriceMult = aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0");
						BigDecimal aQuantity = aObj[4]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[4]),2):new BigDecimal("0");
						BigDecimal quantityReceived = aObj[12]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[12]),2):new BigDecimal("0");
						if (quantityReceived == null) {
							quantityReceived = new BigDecimal(0);
						}
						if(aPriceMult==null){
							aPriceMult=new BigDecimal(0);
						}
						if(aUnitCost==null){
							aUnitCost=new BigDecimal(0);
						}
						if (aUnitCost != null && aPriceMult != null
								&& aQuantity != null) {
							if (aPriceMult.compareTo(new BigDecimal("0.0000"))==0) {
								aTotal = aUnitCost.multiply(new BigDecimal("1.0000"));
								aTotal = aTotal.multiply(getTotal(aQuantity,
										quantityReceived));
								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
							} else {
								aTotal = aUnitCost.multiply(aPriceMult);
								aTotal = aTotal.multiply(getTotal(aQuantity,
										quantityReceived));
								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
							}
						} else if (aUnitCost != null && aQuantity != null) {
							aTotal = aUnitCost.multiply(getTotal(aQuantity,	quantityReceived));
							avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
						} else if (aUnitCost != null && aPriceMult != null) {
							if ((aQuantity.compareTo(BigDecimal.ZERO) != 0)) {
								aTotal = aUnitCost.multiply(getTotal(aQuantity,
										quantityReceived));
							} else {
								aTotal = aUnitCost.multiply(getTotal(aQuantity,
										quantityReceived));
							}

							avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
						} else if (aUnitCost != null) {
							avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aUnitCost,2));
						}
						if (aUnitCost != null && aPriceMult != null) {
							if (aPriceMult.compareTo(new BigDecimal("0.0000"))==0) {
								avepoDetail.setNetCast(JobUtil.floorFigureoverall((avepoDetail.getQuantityOrdered().multiply(
												aUnitCost).multiply(new BigDecimal("1.0000"))),2));
							} else {
								aNetCast = aUnitCost.multiply(aPriceMult);
								avepoDetail.setNetCast(JobUtil.floorFigureoverall((avepoDetail
										.getQuantityOrdered()
										.multiply(aNetCast)),2));
							}
						}
						if (quantityReceived != null) {

							avepoDetail.setQuantityReceived(quantityReceived);
						} else {
							avepoDetail.setQuantityReceived(new BigDecimal(0));
						}
			
						avepoDetail.setTaxTotal((BigDecimal) aObj[10]);
						avepoDetail.setInLineNote((String) aObj[11]);
						avepoDetail.setInLineNoteImage((String) aObj[11]);
						if(aObj[13] != null && aObj[13] != "")
						{
							ackDate = formatter.format((Date) aObj[13]);
							avepoDetail.setAckDate(formatter.format(DateUtils.parseDate(ackDate, new String[]{"MM/dd/yyyy"})));
						}
						else
							avepoDetail.setAckDate("");
						if(aObj[14] != null && aObj[14] != "")
						{
							shipDate = formatter.format((Date) aObj[14]);
							avepoDetail.setShipDate(formatter.format(DateUtils.parseDate(shipDate, new String[]{"MM/dd/yyyy"})));
						}
						else
							avepoDetail.setShipDate("");
						if(aObj[15] != null && aObj[15] != "")
							avepoDetail.setVendorOrderNumber((String) aObj[15]);
						else
							avepoDetail.setVendorOrderNumber("");
						
						
						
						// avepoDetail.setInvoicedAmount((BigDecimal) aObj[14]);
						aQueryList.add(avepoDetail);
						ackDate = null; shipDate = null;
					}
				}
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry=null;
		}
		return aQueryList;
	}

	@SuppressWarnings("unused")
	@Override
	public List<Vepodetail> getPOReleaseLineItemforvendorinvoice(
			Integer theVepoID) throws JobException {
		String aPOLineItemListQry = "";
		
		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		BigDecimal aTotal;
		BigDecimal aNetCast;
		try {
			if (theVepoID != null) {
				
				
				/*
				 * aPOLineItemListQry = "SELECT ve.vePODetailID," + " ve.vePOID, " +
				 * " ve.prMasterID, " + " ve.Description," + " ve.QuantityOrdered, "
				 * + " ve.Taxable, " + " ve.UnitCost," + " ve.PriceMultiplier," +
				 * " ve.posistion," + " pr.ItemCode, " + " vepo.TaxTotal, " +
				 * " ve.Note, "+ " ve.QuantityReceived " +
				 * "FROM vePODetail ve  LEFT JOIN prMaster pr ON ve.prMasterID = pr.prMasterID "
				 * + "RIGHT JOIN vePO vepo ON vepo.vePOID = ve.vePOID " +
				 * "WHERE ve.vePOID = "+theVepoID+" ORDER BY ve.posistion";
				 */

				aPOLineItemListQry = "SELECT ve.vePODetailID, ve.vePOID,  ve.prMasterID,  ve.Description, ve.QuantityOrdered, ve.Taxable,  ve.UnitCost, ve.PriceMultiplier, ve.posistion, pr.ItemCode,  vepo.TaxTotal,  REPLACE(ve.Note,':',''),  ve.QuantityReceived,ve.QuantityOrdered -IFNULL(SUM(vbd.QuantityBilled),0) AS vebillquantity,IFNULL(SUM(vbd.QuantityBilled),0) as vbillquantity FROM vePODetail ve  LEFT JOIN prMaster pr ON ve.prMasterID = pr.prMasterID RIGHT JOIN vePO vepo ON vepo.vePOID = ve.vePOID"
						+ " LEFT JOIN veBillDetail vbd ON(vbd.vePODetailID=ve.vePODetailID) WHERE ve.vePOID="
						+ theVepoID
						+ " GROUP BY ve.vePODetailID ORDER BY ve.posistion";
			
			Vepodetail avepoDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			List<?> aList = aQuery.list();
			if (aPOLineItemListQry.length() > 0)
				if (!aList.isEmpty()) {
					Iterator<?> aIterator = aList.iterator();
					while (aIterator.hasNext()) {
						avepoDetail = new Vepodetail();
						Object[] aObj = (Object[]) aIterator.next();
						avepoDetail.setVePodetailId((Integer) aObj[0]);
						avepoDetail.setVePoid((Integer) aObj[1]);
						avepoDetail.setPrMasterId((Integer) aObj[2]);
						avepoDetail.setDescription((String) aObj[3]);
						if(aObj[4]==null){
							aObj[4]=BigDecimal.ZERO;
						}
						if(aObj[14]==null){
							aObj[14]=BigDecimal.ZERO;
						}
						BigDecimal aQuantity = ((BigDecimal) aObj[4]).subtract((BigDecimal)aObj[14]);
						if(aQuantity.compareTo(BigDecimal.ZERO)!=0){
							avepoDetail.setQuantityOrdered(JobUtil.floorFigureoverall(aQuantity,2));
						}
						else{
							avepoDetail.setQuantityOrdered(BigDecimal.ZERO);
						}
						
						avepoDetail.setSubtractedquantity(JobUtil.floorFigureoverall(aQuantity,2));
						if ((Byte) aObj[5] == 1) {
							avepoDetail.setTaxable(true);
						} else {
							avepoDetail.setTaxable(false);
						}
						avepoDetail.setUnitCost(aObj[6]!=null?(JobUtil.floorFigureoverall((BigDecimal)aObj[6],2)):new BigDecimal("0"));
						avepoDetail.setPriceMultiplier(aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0"));
						avepoDetail.setPosistion((Double) aObj[8]);
						avepoDetail.setItemCode((String) aObj[9]);
						avepoDetail.setNote((String) aObj[11]);
						BigDecimal aUnitCost = aObj[6]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[6]),2):new BigDecimal("0");
						BigDecimal aPriceMult = aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0");
						// BigDecimal aQuantity = (BigDecimal) aObj[4];
						BigDecimal quantityReceived = aObj[12]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[12]),2):new BigDecimal("0");
						if (quantityReceived == null) {
							quantityReceived = new BigDecimal(0);
						}
						if (aUnitCost != null && aPriceMult != null
								&& avepoDetail.getQuantityOrdered() != null) {	
							if (Double.valueOf(aPriceMult.toString()) > Double
									.valueOf(0.0000)) {
								aTotal = aUnitCost.multiply(aPriceMult);
								aTotal = aTotal.multiply(getTotal(avepoDetail.getQuantityOrdered(),
										quantityReceived));
								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
							} else {
								aTotal = aUnitCost.multiply(getTotal(avepoDetail.getQuantityOrdered(),
										quantityReceived));
								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
							}
						} else if (aUnitCost != null && avepoDetail.getQuantityOrdered() != null) {
							aTotal = aUnitCost.multiply(getTotal(avepoDetail.getQuantityOrdered(),
									quantityReceived));
							avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
						} else if (aUnitCost != null && aPriceMult != null) {
							if ((avepoDetail.getQuantityOrdered().compareTo(BigDecimal.ZERO) != 0)) {
								aTotal = aUnitCost.multiply(getTotal(avepoDetail.getQuantityOrdered(),
										quantityReceived));
							} else {
								aTotal = aUnitCost.multiply(getTotal(avepoDetail.getQuantityOrdered(),
										quantityReceived));
							}
							
							avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
						} else if (aUnitCost != null) {
							avepoDetail.setQuantityBilled(aUnitCost);
						}
						if (aUnitCost != null && aPriceMult != null) {
							if (Double.valueOf(aPriceMult.toString()) <= Double
									.valueOf(0.0000)) {
								avepoDetail.setNetCast(JobUtil.floorFigureoverall((avepoDetail.getQuantityOrdered().multiply(aUnitCost)),2));
							} else {
								aNetCast = aUnitCost.multiply(aPriceMult);
								avepoDetail.setNetCast(JobUtil.floorFigureoverall((avepoDetail
										.getQuantityOrdered()
										.multiply(aNetCast)),2));
							}
						}
						if (quantityReceived != null) {
							avepoDetail.setQuantityReceived(JobUtil.floorFigureoverall(quantityReceived,2));
						} else {
							avepoDetail.setQuantityReceived(new BigDecimal(0));
						}
				
						avepoDetail.setTaxTotal((BigDecimal) aObj[10]);
						avepoDetail.setInLineNote((String) aObj[11]);
						avepoDetail.setInLineNoteImage((String) aObj[11]);
						avepoDetail.setValidatequantity(JobUtil.floorFigureoverall(((BigDecimal) aObj[4]),2));
						// avepoDetail.setInvoicedAmount((BigDecimal) aObj[14]);

						aQueryList.add(avepoDetail);

					}
				}
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			if (theVepoID != null) {
				aSession.flush();
				aSession.close();
			}
			aPOLineItemListQry=null;
		}
		return aQueryList;
	}

	/*
	 * @author: praveenkumar Reason : created to fix amount differentation Bug *
	 * (non-Javadoc)
	 * 
	 * @see com.turborep.turbotracker.job.service.JobService#
	 * getPOReleaseLineItemForVendorInvoice(java.lang.Integer)
	 */
	@Override
	public List<Vepodetail> getPOReleaseLineItemForVendorInvoice(
			Integer theVepoID) throws JobException {
		String aPOLineItemListQry = "";
		if (theVepoID != null) {
			aPOLineItemListQry = "SELECT ve.vePODetailID," + " ve.vePOID,"
					+ " ve.prMasterID," + " ve.Description,"
					+ " ve.QuantityOrdered," + " ve.Taxable," + " ve.UnitCost,"
					+ " ve.PriceMultiplier," + " ve.posistion,"
					+ " pr.ItemCode, " + " vepo.TaxTotal, "
					+ " ve.Note ,ve.QuantityReceived" + " FROM vePODetail ve "
					+ " Left Join prMaster pr on ve.prMasterID = pr.prMasterID"
					+ " Right Join vePO vepo on vepo.vePOID = ve.vePOID"
					+ " where ve.vePOID = " + theVepoID
					+ " ORDER BY ve.posistion";
		}
		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		BigDecimal aTotal;
		BigDecimal aNetCast;
		try {
			Vepodetail avepoDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				avepoDetail = new Vepodetail();
				Object[] aObj = (Object[]) aIterator.next();
				avepoDetail.setVePodetailId((Integer) aObj[0]);
				avepoDetail.setVePoid((Integer) aObj[1]);
				avepoDetail.setPrMasterId((Integer) aObj[2]);
				avepoDetail.setDescription((String) aObj[3]);
				avepoDetail.setQuantityOrdered(aObj[4]!=null?(BigDecimal)aObj[4]:new BigDecimal("0"));
				if ((Byte) aObj[5] == 1) {
					avepoDetail.setTaxable(true);
				} else {
					avepoDetail.setTaxable(false);
				}
				avepoDetail.setUnitCost(aObj[6]!=null?(BigDecimal)aObj[6]:new BigDecimal("0"));
				avepoDetail.setPriceMultiplier(aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0"));
				avepoDetail.setPosistion((Double) aObj[8]);
				avepoDetail.setNote((String) aObj[9]);
				BigDecimal aUnitCost = aObj[6]!=null?(BigDecimal)aObj[6]:new BigDecimal("0");
				BigDecimal aPriceMult = aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0");
				BigDecimal aQuantity = aObj[4]!=null?(BigDecimal)aObj[4]:new BigDecimal("0");
				BigDecimal quantityReceived = aObj[12]!=null?(BigDecimal)aObj[12]:new BigDecimal("0");
				if (aUnitCost != null && aPriceMult != null
						&& aQuantity != null) {
					if (Double.valueOf(aPriceMult.toString()) > Double
							.valueOf(0.0000)) {

						aTotal = aUnitCost.multiply(aPriceMult);
					
						aTotal = aTotal.multiply(getTotal(aQuantity,
								(BigDecimal) aObj[4]));
						
						avepoDetail.setQuantityBilled(aTotal);
					} else {
						aTotal = aUnitCost.multiply(getTotal(aQuantity,
								(BigDecimal) aObj[4]));
						avepoDetail.setQuantityBilled(aTotal);
					}
				} else if (aUnitCost != null && aQuantity != null) {
					aTotal = aUnitCost.multiply(getTotal(aQuantity,
							(BigDecimal) aObj[4]));
					avepoDetail.setQuantityBilled(aTotal);
				} else if (aUnitCost != null && aPriceMult != null) {
					if ((aQuantity.compareTo(BigDecimal.ZERO) != 0)) {
						aTotal = aUnitCost.multiply(getTotal(aQuantity,
								(BigDecimal) aObj[4]));
					} else {
						aTotal = aUnitCost.multiply(getTotal(aQuantity,
								(BigDecimal) aObj[4]));
					}

					avepoDetail.setQuantityBilled(aTotal);
				} else if (aUnitCost != null) {
					avepoDetail.setQuantityBilled(aUnitCost);
				}
				if (aUnitCost != null && aPriceMult != null) {
					if (Double.valueOf(aPriceMult.toString()) <= Double
							.valueOf(0.0000)) {
						avepoDetail.setNetCast(aUnitCost);
					} else {
						aNetCast = aUnitCost.multiply(aPriceMult);
						avepoDetail.setNetCast(aNetCast);
					}
				}
				if (quantityReceived != null) {

					avepoDetail.setQuantityReceived(quantityReceived);
				} else {
					avepoDetail.setQuantityReceived(new BigDecimal(0));
				}
				avepoDetail.setTaxTotal((BigDecimal) aObj[10]);
				avepoDetail.setInLineNote((String) aObj[11]);
				aQueryList.add(avepoDetail);

			}

		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getSOReleaseLineItem(Integer theCusoId) throws JobException {

		String aSOReleaseLineItemQry = "SELECT SO.cuSODetailID,"
				+ " SO.cuSOID," + " SO.prMasterID," + " SO.Description,"
				+ " SO.QuantityOrdered," + " SO.Taxable," + " SO.UnitCost,"
				+ " SO.PriceMultiplier," + " pr.ItemCode, "
				+ " cuso.TaxTotal, " + " SO.Note " + " FROM cuSODetail SO "
				+ " Left Join prMaster pr on SO.prMasterID = pr.prMasterID"
				+ " Right Join cuSO cuso on cuso.cuSOID = SO.cuSOID"
				+ " where SO.cuSOID = " + theCusoId + " order by SO.position;";
		Session aSession = null;
		ArrayList<Cusodetail> aQueryList = new ArrayList<Cusodetail>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Cusodetail aCuSOdetails = new Cusodetail();
				Object[] aObj = (Object[]) aIterator.next();
				aCuSOdetails.setCuSodetailId((Integer) aObj[0]);
				aCuSOdetails.setCuSoid((Integer) aObj[1]);
				aCuSOdetails.setPrMasterId((Integer) aObj[2]);
				aCuSOdetails.setDescription((String) aObj[3]);
				if (aObj[4] != null)
					aCuSOdetails.setQuantityOrdered(JobUtil.floorFigureoverall(((BigDecimal) aObj[4]),2));
				aCuSOdetails.setTaxable((Byte) aObj[5]);
				if(aObj[6]!=null){
					 aCuSOdetails.setUnitCost(JobUtil.floorFigureoverall(((BigDecimal) aObj[6]),2));
					
				}else{
					aCuSOdetails.setUnitCost(BigDecimal.ZERO);
				}
				aCuSOdetails.setPriceMultiplier((BigDecimal) aObj[7]);
				aCuSOdetails.setItemCode((String) aObj[8]);
				aCuSOdetails.setTaxTotal((BigDecimal) aObj[9]);
				aCuSOdetails.setNote((String) aObj[10]);
				aCuSOdetails.setNoteImage((String) aObj[10]);
				aCuSOdetails.setWhseCost( itsInventoryService.getWarehouseCost(aCuSOdetails.getPrMasterId()));
				aQueryList.add(aCuSOdetails);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getcuInvoiceReleaseLineItem(Integer thecuInvoiceId)
			throws JobException {

		if(thecuInvoiceId==null){
			thecuInvoiceId =0;
		}
		String aSOReleaseLineItemQry = "SELECT cuInvDet.cuInvoiceDetailID, "
				+ "cuInvDet.cuInvoiceID, "
				+ "cuInvDet.prMasterID, "
				+ "cuInvDet.Description, "
				+ "IFNULL(cuInvDet.QuantityBilled,0.0000), "
				+ "cuInvDet.Taxable, "
				+ "IFNULL(cuInvDet.UnitCost,0.0000), "
				+ "IFNULL(cuInvDet.PriceMultiplier,0.0000), "
				+ "pr.ItemCode, "
				+ "IFNULL(cuso.TaxTotal,0.0000), "
				+ "cuInvDet.Note "
				+ "FROM cuInvoiceDetail cuInvDet "
				+ "Left Join prMaster pr on cuInvDet.prMasterID = pr.prMasterID "
				+ "Right Join cuInvoice cuso on cuso.cuInvoiceID = cuInvDet.cuInvoiceID "
				+ "where cuInvDet.cuInvoiceID ='" + thecuInvoiceId + "';";
		Session aSession = null;
		ArrayList<Cuinvoicedetail> aQueryList = new ArrayList<Cuinvoicedetail>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Cuinvoicedetail aCuinvoicedetail = new Cuinvoicedetail();
				Object[] aObj = (Object[]) aIterator.next();
				aCuinvoicedetail.setCuInvoiceDetailId((Integer) aObj[0]);
				aCuinvoicedetail.setCuInvoiceId((Integer) aObj[1]);
				aCuinvoicedetail.setPrMasterId((Integer) aObj[2]);
				aCuinvoicedetail.setDescription((String) aObj[3]);
				if (aObj[4] != null)
					aCuinvoicedetail.setQuantityBilled(((BigDecimal) aObj[4]).setScale(2,RoundingMode.FLOOR));
				aCuinvoicedetail.setTaxable((Byte) aObj[5]);
				if(aObj[6]!= null)
				   aCuinvoicedetail.setUnitCost(((BigDecimal) aObj[6]).setScale(2,RoundingMode.FLOOR));
				aCuinvoicedetail.setPriceMultiplier((BigDecimal) aObj[7]);
				aCuinvoicedetail.setItemCode((String) aObj[8]);
				aCuinvoicedetail.setTaxTotal((BigDecimal) aObj[9]);
				aCuinvoicedetail.setNote((String) aObj[10]);
				aCuinvoicedetail.setNoteImage((String) aObj[10]);
				aQueryList.add(aCuinvoicedetail);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<Vepodetail> getPOReleaseAck(Integer theVepoID)
			throws JobException {
		String aPOLineItemListQry = "SELECT ve.vePODetailID,"
				+ " ve.vePOID,"
				+ " ve.prMasterID,"
				+ " ve.Description,"
				+ " ve.QuantityOrdered,"
				+ " ve.AcknowledgementDate,"
				+ " ve.VendorOrderNumber,"
				+ " ve.EstimatedShipDate,"
				+ " ve.posistion,"
				+ " pr.ItemCode"
				+ " FROM vePODetail ve Left Join prMaster pr on ve.prMasterID = pr.prMasterID"
				+ " where vePOID = " + theVepoID + " ORDER BY ve.posistion";
		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		try {
			Vepodetail avepoDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				avepoDetail = new Vepodetail();
				Object[] aObj = (Object[]) aIterator.next();
				avepoDetail.setVePodetailId((Integer) aObj[0]);
				avepoDetail.setVePoid((Integer) aObj[1]);
				avepoDetail.setPrMasterId((Integer) aObj[2]);
				avepoDetail.setDescription((String) aObj[3]);
				avepoDetail.setQuantityOrdered((BigDecimal) aObj[4]);
				if (aObj[7] != null) {
					avepoDetail.setShipDate((String) DateFormatUtils.format(
							(Date) aObj[7], "MM/dd/yyyy"));
				}
				if (aObj[5] != null) {
					avepoDetail.setAckDate((String) DateFormatUtils.format(
							(Date) aObj[5], "MM/dd/yyyy"));
				}
				avepoDetail.setVendorOrderNumber((String) aObj[6]);
				avepoDetail.setPosistion((Double) aObj[8]);
				avepoDetail.setNote((String) aObj[9]);
				aQueryList.add(avepoDetail);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO Acknowledge list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry=null;
		}
		return aQueryList;
	}
	
	@Override
	public Boolean saveXMLVepoDetail(Vepodetail theVepodetail) {
		Session aPOReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer aVePOdetailID = 0;
		try {
			aTransaction = aPOReleaseSession.beginTransaction();
			aTransaction.begin();
			aVePOdetailID = (Integer) aPOReleaseSession.save(theVepodetail);
			aTransaction.commit();
			Prmaster thePrmaster =getPrMasterBasedOnId(theVepodetail.getPrMasterId());
			if(aVePOdetailID>0 && thePrmaster!=null && thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(theVepodetail,"add");
			}
			theVepodetail.setPosistion(aVePOdetailID);
			updateVepoDetailPosistion(theVepodetail);

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
		
		} finally {
			aPOReleaseSession.flush();
			aPOReleaseSession.close();
		}
		return true;
	}

	@Override
	public boolean addPOReleaseLineItem(Vepodetail theVepodetail, Vepo theVepo)
			throws JobException {
		Session aPOReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer aVePOdetailID = 0;
		try {
			aTransaction = aPOReleaseSession.beginTransaction();
			aTransaction.begin();
			aVePOdetailID = (Integer) aPOReleaseSession.save(theVepodetail);
			aTransaction.commit();
			Vepo insideoroutsidejob=getVepo(theVepo.getVePoid());
			Prmaster thePrmaster =getPrMasterBasedOnId(theVepodetail.getPrMasterId());
			 if(insideoroutsidejob.getJoReleaseId()!=null){
			if( thePrmaster!=null ){
			if(aVePOdetailID>0 && thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(theVepodetail,"add");
			}
			}
			}else{
				if( thePrmaster!=null ){
				if(aVePOdetailID>0 && thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(theVepodetail,"add");
				}
				}
			}
			theVepodetail.setPosistion(aVePOdetailID);
			updateVepoDetailPosistion(theVepodetail);
			if (theVepodetail.getTaxable() != false) {
				aTransaction = aPOReleaseSession.beginTransaction();
				aTransaction.begin();
				Vepo aVepo = (Vepo) aPOReleaseSession.get(Vepo.class,
						theVepo.getVePoid());
				aVepo.setTaxTotal(theVepo.getTaxTotal());
				aVepo.setVePoid(theVepo.getVePoid());
				aPOReleaseSession.update(aVepo);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
			throw aJobException;
		} finally {
			aPOReleaseSession.flush();
			aPOReleaseSession.close();
		}
		return true;
	}

	@Override
	public void updateVepoDetailPosistion(Vepodetail theVepodetail)
			throws JobException {
		Session aPOReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aPOReleaseSession.beginTransaction();
			aTransaction.begin();
			aPOReleaseSession.update(theVepodetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
			throw aJobException;
		} finally {
			aPOReleaseSession.flush();
			aPOReleaseSession.close();
		}
	}

	@Override
	public boolean editPOReleaseLineItem(Vepodetail theVepodetail, Vepo theVepo)
			throws JobException {
		
		
		itsLogger.info(" theVepodetail.getEstimatedShipDate() = "+theVepodetail.getEstimatedShipDate());
		itsLogger.info(" theVepodetail.getAcknowledgedDate() = "+theVepodetail.getAcknowledgedDate());
		itsLogger.info(" theVepodetail.getVendorOrderNumber() = "+theVepodetail.getVendorOrderNumber());
		
		Session aPOReleaseEditSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Vepodetail aVepodetail = null;
		try {
			//Y i put here na. we should rollback before quantity update in vepodetail table
			
			
			if (theVepodetail.getDescription().equals("all")) {

				Vepo aVepo = null;

				if (theVepodetail.getAckDatePO() != null
						&& theVepodetail.getAcknowledgedDate() != null) {
					for (int index = 0; index < theVepodetail.getAckDatePO()
							.size(); index++) {
						String aVePOID = (String) theVepodetail.getAckDatePO()
								.get(index);
						aTransaction = aPOReleaseEditSession.beginTransaction();
						aVepodetail = (Vepodetail) aPOReleaseEditSession.get(
								Vepodetail.class, Integer.valueOf(aVePOID));
						aVepo = (Vepo) aPOReleaseEditSession.get(Vepo.class,
								aVepodetail.getVePoid());
						aTransaction.begin();
						aVepodetail.setAcknowledgedDate(theVepodetail
								.getAcknowledgedDate());
						aVepo.setAcknowledged(true);
						aVepo.setAcknowledgementDate(theVepodetail
								.getAcknowledgedDate());
						aVepodetail.setPrMasterId(aVepodetail.getPrMasterId());
						aVepodetail.setVePoid(aVepodetail.getVePoid());
						aVepodetail.setVePodetailId(Integer.valueOf(aVePOID));
						if(theVepodetail.getAcknowledgedDate() != null)
							aVepodetail.setAcknowledgedDate(theVepodetail.getAcknowledgedDate());
						if(theVepodetail.getEstimatedShipDate() != null)
							aVepodetail.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());
						if(theVepodetail.getVendorOrderNumber() != null)
							aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
						aPOReleaseEditSession.update(aVepodetail);
						aTransaction.commit();

					}
				}
				if (theVepodetail.getaShipDate() != null
						&& theVepodetail.getEstimatedShipDate() != null) {
					for (int index = 0; index < theVepodetail.getaShipDate()
							.size(); index++) {
						String aShipVePOID = (String) theVepodetail
								.getaShipDate().get(index);
						aTransaction = aPOReleaseEditSession.beginTransaction();
						aVepodetail = (Vepodetail) aPOReleaseEditSession.get(
								Vepodetail.class, Integer.valueOf(aShipVePOID));
						
						aVepo = (Vepo) aPOReleaseEditSession.get(Vepo.class,
								aVepodetail.getVePoid());
						aTransaction.begin();
						aVepodetail.setEstimatedShipDate(theVepodetail
								.getEstimatedShipDate());

						aVepo.setEstimatedShipDate(theVepodetail
								.getEstimatedShipDate());
						aVepodetail.setPrMasterId(aVepodetail.getPrMasterId());
						aVepodetail.setVePoid(aVepodetail.getVePoid());
						aVepodetail.setVePodetailId(Integer
								.valueOf(aShipVePOID));
						if(theVepodetail.getAcknowledgedDate() != null)
							aVepodetail.setAcknowledgedDate(theVepodetail.getAcknowledgedDate());
						if(theVepodetail.getEstimatedShipDate() != null)
							aVepodetail.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());
						if(theVepodetail.getVendorOrderNumber() != null)
							aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
						aPOReleaseEditSession.update(aVepodetail);
						aTransaction.commit();
					}
				}
				if (theVepodetail.getOrderNumber() != null
						&& !theVepodetail.getVendorOrderNumber()
								.equalsIgnoreCase("")) {
					for (int index = 0; index < theVepodetail.getOrderNumber()
							.size(); index++) {
						String aShipVePOID = (String) theVepodetail
								.getOrderNumber().get(index);
						aTransaction = aPOReleaseEditSession.beginTransaction();
						aVepodetail = (Vepodetail) aPOReleaseEditSession.get(
								Vepodetail.class, Integer.valueOf(aShipVePOID));
						aVepo = (Vepo) aPOReleaseEditSession.get(Vepo.class,
								aVepodetail.getVePoid());
						aTransaction.begin();

						aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
						aVepo.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
						aVepodetail.setPrMasterId(aVepodetail.getPrMasterId());
						aVepodetail.setVePoid(aVepodetail.getVePoid());
						aVepodetail.setVePodetailId(Integer
								.valueOf(aShipVePOID));
						if(theVepodetail.getAcknowledgedDate() != null)
							aVepodetail.setAcknowledgedDate(theVepodetail.getAcknowledgedDate());
						if(theVepodetail.getEstimatedShipDate() != null)
							aVepodetail.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());
						if(theVepodetail.getVendorOrderNumber() != null)
							aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
						aPOReleaseEditSession.update(aVepodetail);
						aTransaction.commit();
					}
				}
				if (aVepo != null) {
					aTransaction = aPOReleaseEditSession.beginTransaction();
					aTransaction.begin();
					aPOReleaseEditSession.update(aVepo);
					aTransaction.commit();
				}
			} else {
				aTransaction = aPOReleaseEditSession.beginTransaction();
				aVepodetail = (Vepodetail) aPOReleaseEditSession.get(
						Vepodetail.class, theVepodetail.getVePodetailId());
				aTransaction.begin();
				Vepo aVepoack = (Vepo) aPOReleaseEditSession.get(Vepo.class,
						theVepo.getVePoid());
				if (theVepodetail.getDescription().equals("acknowlegement")) {
					aVepodetail.setEstimatedShipDate(theVepodetail
							.getEstimatedShipDate());
					aVepodetail.setAcknowledgedDate(theVepodetail
							.getAcknowledgedDate());
					aVepodetail.setVendorOrderNumber(theVepodetail
							.getVendorOrderNumber());
					//Edited by: leo  Budid:289
					aVepoack.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
					if(theVepodetail.getEstimatedShipDate()!=null)
					aVepoack.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());

				} else {
					aVepodetail.setDescription(theVepodetail.getDescription());
					aVepodetail.setPriceMultiplier(theVepodetail
							.getPriceMultiplier());
					aVepodetail.setQuantityOrdered(theVepodetail
							.getQuantityOrdered());
					aVepodetail.setTaxable(theVepodetail.getTaxable());
					aVepodetail.setUnitCost(theVepodetail.getUnitCost());
					aVepodetail.setPosistion(theVepodetail.getPosistion());
				}
				
				
				
				if(theVepodetail.getAcknowledgedDate() != null)
					aVepodetail.setAcknowledgedDate(theVepodetail.getAcknowledgedDate());
				if(theVepodetail.getEstimatedShipDate() != null)
				{
					aVepodetail.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());
				
				}
				if(theVepodetail.getVendorOrderNumber() != null)
					aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
				
				aVepodetail.setPrMasterId(theVepodetail.getPrMasterId());
				aVepodetail.setVePoid(theVepodetail.getVePoid());
				aVepodetail.setVePodetailId(theVepodetail.getVePodetailId());
				if (!theVepodetail.getDescription().equals("acknowlegement")) {
					Vepo insideoroutsidejob=getVepo(theVepo.getVePoid());
					Prmaster thePrmaster =getPrMasterBasedOnId(aVepodetail.getPrMasterId());
					 if(insideoroutsidejob.getJoReleaseId()!=null){
							if(thePrmaster.getIsInventory()==1){
								updatePrWarehouseInventoryOrdered(aVepodetail,"edit");
							}
					}else{
						if(thePrmaster.getIsInventory()==1){
						updatePrWarehouseInventoryOrdered(aVepodetail,"edit");
						}
					}
				}
				
				aPOReleaseEditSession.update(aVepodetail);
				aTransaction.commit();
				if (!theVepodetail.getDescription().equals("acknowlegement")) {
					if (theVepodetail.getTaxable() != null) {
						aTransaction = aPOReleaseEditSession.beginTransaction();
						aTransaction.begin();
						Vepo aVepo = (Vepo) aPOReleaseEditSession.get(Vepo.class,
								theVepo.getVePoid());
						aVepo.setTaxTotal(theVepo.getTaxTotal());
						aVepo.setVePoid(theVepo.getVePoid());
						aPOReleaseEditSession.update(aVepo);
						aTransaction.commit();
					}
				} else {
					aTransaction = aPOReleaseEditSession.beginTransaction();
					aTransaction.begin();
					aPOReleaseEditSession.update(aVepoack);
					aTransaction.commit();
				}

			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
			throw aJobException;
		} finally {
			aPOReleaseEditSession.flush();
			aPOReleaseEditSession.close();
		}
		return true;
	}

	public String updateInventoryOrderedForInventoryWarehouse(Vepodetail objVepodetail)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Prmaster aPrmaster = null;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Vepodetail aVepodetail = (Vepodetail) aVePOSession.get(Vepodetail.class,
					objVepodetail.getVePodetailId());
			itsLogger.info("Vepoid-->"+aVepodetail.getVePoid());
			
			itsLogger.info("objVepodetail.getVePodetailId() ::"+objVepodetail.getVePodetailId());
			Vepo objVepo = (Vepo) aVePOSession.get(Vepo.class, aVepodetail.getVePoid());
			
			Criteria criteria = aVePOSession.createCriteria(Prwarehouseinventory.class)
					.add(Restrictions.eq("prMasterId", aVepodetail.getPrMasterId()))
					.add(Restrictions.eq("prWarehouseId", objVepo.getPrWarehouseId()));
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory)criteria.uniqueResult();
			/*if(objPrwarehouseinventory.getInventoryOnOrder()!=null){
				BigDecimal order = objPrwarehouseinventory.getInventoryOnOrder();
				//objPrwarehouseinventory.setInventoryOnOrder(order.subtract(updateDifference));
			}
			if(objPrwarehouseinventory.getInventoryOnHand()!=null){
				BigDecimal onHand= objPrwarehouseinventory.getInventoryOnHand();*/
				
				objPrwarehouseinventory.setInventoryOnOrder(aVepodetail.getQuantityOrdered());
			//}
			aVePOSession.update(objPrwarehouseinventory);
			
			List<Prwarehouseinventory> aQueryList = null;
			Query querys = aVePOSession
					.createQuery("FROM  Prwarehouseinventory WHERE prMasterID="+objVepodetail.getPrMasterId());
				aQueryList = querys.list();
		BigDecimal quantityOnOrder = new BigDecimal("0.0000");
		if(aQueryList.size()>0){
			for(int j=0;j<aQueryList.size();j++){
				quantityOnOrder = quantityOnOrder.add(aQueryList.get(j).getInventoryOnOrder()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnOrder());
			}
			 aPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
					 objVepodetail.getPrMasterId());
			aPrmaster.setInventoryOnOrder(quantityOnOrder);
			aVePOSession.update(aPrmaster);
		}
			aTransaction.commit();			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}

	@Override
	public Vepo updatePOGeneral(Vepo theVepo, Rxaddress theRxaddress,
			Rxaddress theRxaddressShipTo, JoRelease theJoRelease)
			throws JobException {
		itsLogger.debug("Update PO General in Release page.");
		Session aSession = itsSessionFactory.openSession();
		Vepo aVepo = null;
		Integer aRxAddressShipID = null;
		Integer aRxAddresID = null;
		Transaction aTransaction;
		Rxmaster aRxmaster = null;
		Integer rxMasterID = null;
		Integer aPrWarehouseID = null;
		Integer vePOID=null;
		Jomaster aJomaster = new Jomaster();
		JoRelease aJoRelease = new JoRelease();
		Vepo bVepo = null;
		itsLogger.info("rxShiptoAddress::"+theVepo.getRxShipToAddressId()+ " ::"+theVepo.getTransactionStatus());
		try {
			if (theVepo.getShipToMode() == 3 && theVepo.getRxShipToAddressId()==null) {
				//aRxmaster = new Rxmaster();
				//aRxmaster.setInActive(false);
				//aRxmaster.setName(theRxaddressShipTo.getName());
				
			
				//Length checking here
				
				/*if(theRxaddressShipTo.getName()!=null && !theRxaddressShipTo.getName().trim().equals("") && theRxaddressShipTo.getName().trim().length()>=5)
				aRxmaster.setSearchName(theRxaddressShipTo.getName().substring(0,4).toUpperCase().trim());
				else
				aRxmaster.setSearchName("");
				aRxmaster.setFirstName("");
				aRxmaster.setIsCustomer(false);
				rxMasterID = (Integer) aSession.save(aRxmaster);
				theRxaddressShipTo.setRxMasterId(rxMasterID);*/
					
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				theRxaddressShipTo.setIsShipTo(true);
				aRxAddressShipID = (Integer) aSession
						.save(theRxaddressShipTo);
				itsLogger.info("rxShiptoAddress::"+aRxAddressShipID);
				aTransaction.commit();
				aPrWarehouseID = theVepo.getPrWarehouseId();
			}
			else if (theVepo.getShipToMode() == 0)
			{
				aRxAddressShipID = theVepo.getRxShipToAddressId();
				aPrWarehouseID = theVepo.getPrWarehouseId();
			}
			else
			{
				aRxAddressShipID = theVepo.getRxShipToAddressId();
				aPrWarehouseID = theVepo.getPrWarehouseId();
			}
			
			
			if (theVepo.getBillToIndex()!=null && theVepo.getBillToIndex() == 2) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aRxAddresID = (Integer) aSession.save(theRxaddress);
				aTransaction.commit();
			}
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if(theVepo.getVePoid()!=null && theVepo.getVePoid()>0){
			aVepo = (Vepo) aSession.get(Vepo.class, theVepo.getVePoid());
			aVepo.setTag(theVepo.getTag());
			aVepo.setCreatedById(theVepo.getCreatedById());
			aVepo.setRxVendorId(theVepo.getRxVendorId());
			aVepo.setJoReleaseId(theVepo.getJoReleaseId());
			aVepo.setRxBillToId(theVepo.getRxBillToId());
			aVepo.setRxShipToId(theVepo.getRxShipToId());
			aVepo.setRxBillToAddressId(aRxAddresID);
			aVepo.setRxShipToAddressId(aRxAddressShipID);
			if(theVepo.getShipToMode()==3){
				aVepo.setRxShipToOtherAddressID(aRxAddressShipID);
			}
			//aVepo.setCreatedOn(theVepo.getCreatedOn());
			
			aVepo.setChangedById(aVepo.getOrderedById());
			aVepo.setChangedOn(new Date());
			aVepo.setOrderDate(theVepo.getOrderDate());
			aVepo.setVeFactoryId(theVepo.getVeFactoryId());
			aVepo.setOrderedById(theVepo.getOrderedById());
			if (!theVepo.getRxVendorContactId().equals(-1)) {
				aVepo.setRxVendorContactId(theVepo.getRxVendorContactId());
			}
			if (!theVepo.getVeFreightChargesId().equals(-1)) {
				aVepo.setVeFreightChargesId(theVepo.getVeFreightChargesId());
			}
			if (!theVepo.getVeShipViaId().equals(-1)) {
				aVepo.setVeShipViaId(theVepo.getVeShipViaId());
			}
			aVepo.setCustomerPonumber(theVepo.getCustomerPonumber());
			aVepo.setPonumber(theVepo.getPonumber());
			aVepo.setDateWanted(theVepo.getDateWanted());
			aVepo.setSpecialInstructions(theVepo.getSpecialInstructions());
			aVepo.setQbPO(theVepo.getQbPO());
			aVepo.setFreight(theVepo.getFreight());
			aVepo.setTaxRate(theVepo.getTaxRate());
			aVepo.setSubtotal(theVepo.getSubtotal());
			aVepo.setTaxTotal(theVepo.getTaxTotal());
			aVepo.setBillToIndex(theVepo.getBillToIndex());
			aVepo.setShipTo(theVepo.getShipTo());
			aVepo.setShipToMode(theVepo.getShipToMode());
			aVepo.setEmailTimeStamp(theVepo.getEmailTimeStamp());
			aVepo.setVePoid(theVepo.getVePoid());
			aVepo.setWantedOnOrBefore(theVepo.getWantedOnOrBefore());
			aVepo.setTransactionStatus(theVepo.getTransactionStatus());
			aVepo.setPrWarehouseId(aPrWarehouseID);
			
			String VepodetailQuery="FROM Vepodetail WHERE vePOID ="+theVepo.getVePoid();
			Query theQuery = aSession.createQuery(VepodetailQuery);
			ArrayList<Vepodetail> vepoDetailList = (ArrayList<Vepodetail>) theQuery.list();
			for(Vepodetail theVepodetail:vepoDetailList){
				RollBackPrMasterPrWareHouseInventoryForPO(theVepodetail.getVePoid(), theVepodetail.getVePodetailId());
			}
			aSession.update(aVepo);
			for(Vepodetail theVepodetail:vepoDetailList){
				insertPrMasterPrWareHouseInventoryForPO(theVepodetail.getVePoid(), theVepodetail.getVePodetailId());
			}
			}else{
				//Added by Zenith on 08/06/2015 for Empty PO Release
				itsLogger.info("Shipto##"+theVepo.getShipTo()
						+"\nShiptoID"+theVepo.getRxBillToId()+"\nBilltoID"+theVepo.getRxBillToId()
						+"\nprWarehouseIDID"+ theVepo.getPrWarehouseId());
				theVepo.setRxBillToId(theVepo.getJobCustomerID());
				theVepo.setRxBillToAddressId(theVepo.getJobCustomerID());
				
				theVepo.setRxShipToId(theVepo.getJobCustomerID());
				theVepo.setRxShipToAddressId(theVepo.getJobCustomerID());
				
				theVepo.setConsignment(false);
				theVepo.setMultipleAcks(false);
				Date now = new Date();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String createdOnDate = df.format(now);
				if((createdOnDate != null && createdOnDate != "")){
					theVepo.setCreatedOn(DateUtils.parseDate(createdOnDate, new String[]{"MM/dd/yyyy"}));
				}
				
				theVepo.setConsignment(true);
				theVepo.setMultipleAcks(true);
				theVepo.setPrWarehouseId(aPrWarehouseID);
				theVepo.setRxBillToAddressId(aRxAddresID);
				theVepo.setRxShipToAddressId(aRxAddressShipID);
				if(theVepo.getShipToMode()==3){
					theVepo.setRxShipToOtherAddressID(aRxAddressShipID);
				}
				vePOID = (Integer) aSession.save(theVepo);
			}
			aTransaction.commit();
			if(vePOID!=null && vePOID>0){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
			aJoRelease = (JoRelease) aSession.get(JoRelease.class, theJoRelease.getJoReleaseId());
			aJomaster = getSingleJobDetails(aJoRelease.getJoMasterId());
			
			//String aNewPONumber = getNewPONumber(aJomaster.getJobNumber()+getCharForNumber(aJoRelease.getSeq_Number()), vePOID);
			String aNewPONumber = aJomaster.getJobNumber()+getCharForNumber(aJoRelease.getSeq_Number());
			bVepo = (Vepo) aSession.get(Vepo.class, vePOID);
			bVepo.setPonumber(aNewPONumber);
			aSession.update(bVepo);
			aTransaction.commit();
			aVepo=bVepo;
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVepo;
	}

	public static String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
	@Override
	public boolean deletePOReleaseLineItem(Vepodetail theVepodetail,
			Vepo theVepo) throws JobException {
		Session aJoPOReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			deleteveBillDeatil(theVepodetail);
			deleteveReceiveDeatil(theVepodetail);
			Vepodetail avepodet=(Vepodetail) aJoPOReleaseSession.get(Vepodetail.class,theVepodetail.getVePodetailId());
			Vepo insideoroutsidejob=getVepo(theVepo.getVePoid());
			Prmaster thePrmaster =getPrMasterBasedOnId(avepodet.getPrMasterId());
			 if(insideoroutsidejob.getJoReleaseId()!=null){
				if(theVepodetail.getVePodetailId()>0 && thePrmaster!=null && thePrmaster.getIsInventory()==1){
				 updatePrWarehouseInventoryOrdered(avepodet, "del");
				}
			}else{
				if(theVepodetail.getVePodetailId()>0 && thePrmaster!=null && thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(avepodet, "del");
				}
			}
			aTransaction = aJoPOReleaseSession.beginTransaction();
			aTransaction.begin();
			aJoPOReleaseSession.delete(avepodet);
			aTransaction.commit();
			if (theVepodetail.getTaxable() == true) {
				aTransaction = aJoPOReleaseSession.beginTransaction();
				aTransaction.begin();
				Vepo aVepo = (Vepo) aJoPOReleaseSession.get(Vepo.class,
						theVepo.getVePoid());
				aVepo.setTaxTotal(theVepo.getTaxTotal());
				aVepo.setVePoid(theVepo.getVePoid());
				aJoPOReleaseSession.update(aVepo);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
			throw aJobException;
		} finally {
			aJoPOReleaseSession.flush();
			aJoPOReleaseSession.close();
		}
		return false;
	}

	@Override
	public boolean deletePOLineItem(Vepodetail theVepodetail, Vepo theVepo)
			throws JobException {
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			if (null != theVepo.getVePoid()) {
				List<Vepodetail> ve = getvePOList(theVepo.getVePoid());
				Transaction aTransaction123 = null;
				
				if (ve.size() > 0) {
					Vepodetail aVepodetail = null;
					try {
						for (int index = 0; index < ve.size(); index++) {

							aVepodetail = (Vepodetail) aSearchSession.get(
									Vepodetail.class, ve.get(index)
											.getVePodetailId());
							aTransaction123 = aSearchSession.beginTransaction();
							aTransaction123.begin();
							aSearchSession.delete(aVepodetail);
							aTransaction123.commit();
							updateInventoryOrdered(aVepodetail);
						}

					} catch (Exception excep) {
						itsLogger.error(excep.getMessage(), excep);
						JobException aJobException = new JobException(
								excep.getMessage(), excep);
						throw aJobException;
					} finally {
						aSearchSession.flush();
						aSearchSession.close();
					}
				}
				try {
					aTransaction123 = aSearchSession.beginTransaction();
					aTransaction123.begin();
					Vepo vePO = (Vepo) aSearchSession.get(Vepo.class,
							theVepo.getVePoid());
					aSearchSession.delete(vePO);
					aTransaction123.commit();
				} catch (Exception excep) {
					itsLogger.error(excep.getMessage(), excep);
					JobException aJobException = new JobException(
							excep.getMessage(), excep);
					throw aJobException;
				} finally {
					aSearchSession.flush();
					aSearchSession.close();
				}
			}
			// deleteveBillDeatil(theVepodetail);
			// deleteveReceiveDeatil(theVepodetail);
			// aTransaction = aJoPOReleaseSession.beginTransaction();
			// aTransaction.begin();
			// aJoPOReleaseSession.delete(theVepodetail);
			// aTransaction.commit();
			//
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
			throw aJobException;
		} finally {
		}
		return false;
	}

	private boolean deleteveBillDeatil(Vepodetail theVepodetail)
			throws JobException {
		List<Vebilldetail> aVebilldetail = getveBillDeatilID(theVepodetail);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Vebilldetail aVebilldetails = null;
		try {
			for (int index = 0; index < aVebilldetail.size(); index++) {
				aVebilldetails = (Vebilldetail) aSearchSession.get(
						Vebilldetail.class, aVebilldetail.get(index)
								.getVeBillDetailId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aVebilldetails);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<Vebilldetail> getveBillDeatilID(Vepodetail theVepodetail)
			throws JobException {
		String aJobSelectQry = "SELECT veBillDetailID, vePODetailID FROM veBillDetail WHERE vePODetailID = "
				+ theVepodetail.getVePodetailId();
		Session aSession = null;
		ArrayList<Vebilldetail> aQueryList = new ArrayList<Vebilldetail>();
		try {
			Vebilldetail aVebilldetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVebilldetail = new Vebilldetail();
				Object[] aObj = (Object[]) aIterator.next();
				aVebilldetail.setVeBillDetailId((Integer) aObj[0]);
				aVebilldetail.setVePodetailId((Integer) aObj[1]);
				aQueryList.add(aVebilldetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	private boolean deleteveReceiveDeatil(Vepodetail theVepodetail)
			throws JobException {
		List<Vereceivedetail> aVereceivedetails = getveReceiveDeatilID(theVepodetail);
		Session aSearchSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Vereceivedetail aVereceivedetail = null;
		try {
			for (int index = 0; index < aVereceivedetails.size(); index++) {
				aVereceivedetail = (Vereceivedetail) aSearchSession.get(
						Vereceivedetail.class, aVereceivedetails.get(index)
								.getVeReceiveDetailId());
				aTransaction = aSearchSession.beginTransaction();
				aTransaction.begin();
				aSearchSession.delete(aVereceivedetail);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSearchSession.flush();
			aSearchSession.close();
		}
		return false;
	}

	private List<Vereceivedetail> getveReceiveDeatilID(Vepodetail theVepodetail)
			throws JobException {
		String aJobSelectQry = "SELECT veReceiveDetailID, vePODetailID FROM veReceiveDetail WHERE vePODetailID = "
				+ theVepodetail.getVePodetailId();
		Session aSession = null;
		ArrayList<Vereceivedetail> aQueryList = new ArrayList<Vereceivedetail>();
		try {
			Vereceivedetail aVereceivedetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVereceivedetail = new Vereceivedetail();
				Object[] aObj = (Object[]) aIterator.next();
				aVereceivedetail.setVeReceiveDetailId((Integer) aObj[0]);
				aVereceivedetail.setVePodetailId((Integer) aObj[1]);
				aQueryList.add(aVereceivedetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Integer updateJobRelease(Jomaster theJomaster) throws JobException {
		Session aJobReleaseSession = itsSessionFactory.openSession();
		Jomaster aJomaster = new Jomaster();
		Transaction aTransaction;
		try {
			aTransaction = aJobReleaseSession.beginTransaction();
			aTransaction.begin();
			aJomaster = getSingleJobDetails(theJomaster.getJobNumber(),theJomaster.getJoMasterId());
			aJomaster.setReleaseNotes(theJomaster.getReleaseNotes());
			aJomaster.setNoticeId(theJomaster.getNoticeId());
			aJomaster.setContactId(theJomaster.getContactId());
			aJomaster.setNotice(theJomaster.getNotice());
			aJomaster.setContactName(theJomaster.getContactName());
			aJomaster.setOtherContact(theJomaster.getOtherContact());
			if(theJomaster.getCustomerPonumber()!=null)
			aJomaster.setCustomerPonumber(theJomaster.getCustomerPonumber());
			aJobReleaseSession.update(aJomaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJobReleaseSession.flush();
			aJobReleaseSession.close();
		}
		return 0;
	}

	@Override
	public Integer updateBillNote(JoRelease theJoRelease) throws JobException {
		Session aJobReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			JoRelease aJoRelease = (JoRelease) aJobReleaseSession.get(JoRelease.class,theJoRelease.getJoReleaseId());
			aJoRelease.setReleaseDate(theJoRelease.getReleaseDate());
			aJoRelease.setReleaseType(theJoRelease.getReleaseType());
			aJoRelease.setEstimatedBilling(theJoRelease.getEstimatedBilling());
			aJoRelease.setJoMasterId(theJoRelease.getJoMasterId());
			aJoRelease.setReleaseNote(theJoRelease.getReleaseNote());
			aJoRelease.setBillNote(theJoRelease.getBillNote());
			aJoRelease.setJoReleaseId(theJoRelease.getJoReleaseId());
			aTransaction = aJobReleaseSession.beginTransaction();
			aTransaction.begin();
			aJobReleaseSession.update(aJoRelease);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJobReleaseSession.flush();
			aJobReleaseSession.close();
		}
		return 0;
	}

	public Cumaster getSingleCuMasterDetails(Integer theMasterID)
			throws JobException {
		Session aSession = null;
		Cumaster aCumaster = null;
		try {
			aSession = itsSessionFactory.openSession();
			aCumaster = new Cumaster();
			
			if(theMasterID!=null && theMasterID!=0)
			aCumaster = (Cumaster) aSession.get(Cumaster.class, theMasterID);
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCumaster;
	}

	public boolean updateJoQuoteHistory(Joquotehistory theJoquotehistory,
			String theToken) throws JobException {
		Session aJobHistorySession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJobHistorySession.beginTransaction();
			Integer rxMasterId = getRxmasterID(theJoquotehistory
					.getJoMasterId());
			theJoquotehistory.setRxMasterId(rxMasterId);
			aTransaction.begin();
			if (theToken.equals("add")) {
				aJobHistorySession.save(theJoquotehistory);
			} else if (theToken.equals("edit")) {
				Joquotehistory aExistingQuoteHistory = new Joquotehistory();
				aExistingQuoteHistory
						.setJoQuoteHistoryId(getJoQuoteHistoryId(theJoquotehistory
								.getJoQuoteHeaderId()));
				if (aExistingQuoteHistory.getJoQuoteHistoryId() != 0) {
					aExistingQuoteHistory.setJoMasterId(theJoquotehistory
							.getJoMasterId());
					aExistingQuoteHistory.setJoQuoteHeaderId(theJoquotehistory
							.getJoQuoteHeaderId());
					aExistingQuoteHistory.setRxMasterId(theJoquotehistory
							.getRxMasterId());
					aExistingQuoteHistory.setQuoteDate(theJoquotehistory
							.getQuoteDate());
					aExistingQuoteHistory.setQuoteRev(theJoquotehistory
							.getQuoteRev());
					aJobHistorySession.update(aExistingQuoteHistory);
				} else {
					aJobHistorySession.save(theJoquotehistory);
				}
			} else if (theToken.equals("copy")) {
				aJobHistorySession.save(theJoquotehistory);
			}
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while updating the Quote history: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aJobHistorySession.flush();
			aJobHistorySession.close();
		}
		return true;
	}

	public Integer getRxmasterID(Integer thejoMasterID) throws JobException {
		String aJobSelectQry = "SELECT rxCustomerId FROM joMaster WHERE joMasterId ="
				+ thejoMasterID;
		Session aSession = null;
		Integer theRxMasterId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				theRxMasterId = 0;
			} else {
				theRxMasterId = (Integer) aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return theRxMasterId;

	}

	public String getCustomerPONoFromJomaster(Integer thejoMasterID)
			throws JobException {
		String aJobSelectQry = "SELECT CustomerPONumber FROM joMaster WHERE joMasterId ="
				+ thejoMasterID;
		Session aSession = null;
		String thecustPONo = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				thecustPONo = "";
			} else {
				if(aList.get(0)!=null)
				thecustPONo = (String) aList.get(0);
				else	
				thecustPONo = "";
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return thecustPONo;

	}

	public Integer getJoQuoteHistoryId(Integer theJoQuoteHeaderID)
			throws JobException {
		String aJobSelectQry = "SELECT joQuoteHistoryID FROM joQuoteHistory WHERE joQuoteHistoryID ="
				+ theJoQuoteHeaderID;
		Session aSession = null;
		Integer aJoQuoteHistoryId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				aJoQuoteHistoryId = 0;
			} else {
				aJoQuoteHistoryId = (Integer) aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aJoQuoteHistoryId;
	}

	@Override
	public String getNewJobNumber(String theSearchJobString)
			throws JobException {
		String aJobSelectQry = "SELECT jobNumber, RIGHT(jobNumber, 3) AS jobNumberCount FROM joMaster WHERE jobNumber LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchJobString)
				+ "%' ORDER BY jobNumberCount DESC limit 0,1;";
		Session aSession = null;
		String aNewBookedjobNumber = null;
		String aJobString = null;
		String[] aJobStrings = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				aNewBookedjobNumber = theSearchJobString + "001";
			} else {
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aJobString = (String) aObj[0];
				}
				aJobStrings = aJobString.trim().split("-");
				aJobStrings[1] = aJobStrings[1].replaceAll("[^0-9]", "");
				Integer lastNumber = Integer.parseInt(aJobStrings[1]);
				lastNumber = lastNumber + 1;
				String spaceHolder = "000";
				String intString = String.valueOf(lastNumber);
				String string = spaceHolder.substring(intString.length())
						.concat(intString);
				aNewBookedjobNumber = theSearchJobString + string;
				itsLogger.info("NewBooked JobNumber: " + aNewBookedjobNumber);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;aJobString=null;aJobStrings=null;
		}
		return aNewBookedjobNumber;
	}

	@Override
	public Integer updateJobSubmittal(Jomaster theJomaster) throws JobException {
		Session aJobReleaseSession = itsSessionFactory.openSession();
		Jomaster aJomaster = new Jomaster();
		Transaction aTransaction;
		try {
			aTransaction = aJobReleaseSession.beginTransaction();
			aTransaction.begin();
			aJomaster = (Jomaster) aJobReleaseSession.get(Jomaster.class,
					theJomaster.getJoMasterId());
			aJomaster.setSubmittalSent(theJomaster.isSubmittalSent());
			aJomaster.setSubmittalSentDate(theJomaster.getSubmittalSentDate());
			aJomaster.setSubmittalResent(theJomaster.isSubmittalResent());
			aJomaster.setSubmittalResentDate(theJomaster
					.getSubmittalResentDate());
			aJomaster.setSubmittalApproved(theJomaster.isSubmittalApproved());
			aJomaster.setSubmittalApprovedDate(theJomaster
					.getSubmittalApprovedDate());
			aJomaster.setManualQty(theJomaster.getManualQty());
			aJomaster.setManualSent(theJomaster.getManualSent());
			aJomaster.setManualDate(theJomaster.getManualDate());
			aJomaster.setRequestManual(theJomaster.getRequestManual());
			aJomaster.setRequestDate(theJomaster.getRequestDate());
			aJomaster.setReleaseNotes(theJomaster.getReleaseNotes());
			aJobReleaseSession.update(aJomaster);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJobReleaseSession.flush();
			aJobReleaseSession.close();
		}
		return 0;
	}

	@Override
	public Integer getVeFactoryId(Integer therxMasterId) throws JobException {
		Integer aVeFactoryId = null;
		String aSelectQry = "SELECT veFactoryId FROM veFactory where rxMasterId= "
				+ therxMasterId;
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			aVeFactoryId = (Integer) aQuery.list().get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aVeFactoryId;
	}

	@Override
	public List<Prwarehouse> getWareHouse() throws JobException {
		Session aSession = null;
		List<Prwarehouse> aQueryList = null;
		Query query = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			query = aSession
					.createQuery("FROM  Prwarehouse WHERE inActive=0 ");
			// Retrieve all
			
			
			//if (!query.list().isEmpty()) {
				aQueryList = query.list();
		//	}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			excep.printStackTrace();
			JobException aJobException = new JobException(
					"Exception occurred while getting the Freight Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aQueryList;
	}
	
	@Override
	public List<Object> getWareHousewithtaxtertory() throws JobException {
		Session aSession = null;
		List<Object> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createQuery("FROM  Prwarehouse pr,CoTaxTerritory co WHERE co.coTaxTerritoryId=pr.coTaxTerritoryId and pr.inActive=0 ORDER BY pr.prWarehouseId");
			aQueryList = query.list();
				
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Freight Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}
	
	@Override
	public List<Vefreightcharges> getFreights() throws JobException {
		Session aSession = null;
		List<Vefreightcharges> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession
					.createQuery("FROM  Vefreightcharges");
			// Retrieve all
			if (!query.list().isEmpty()) {
				aQueryList = query.list();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Freight Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}


	@Override
	public List<Prwarehouse> getWareHouseForDefaults() throws JobException {
		Session aSession = null;
		List<Prwarehouse> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query query = aSession
					.createQuery("FROM  Prwarehouse WHERE InActive=0");
			// Retrieve all
			if (!query.list().isEmpty()) {
				aQueryList = query.list();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Freight Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}

	@Override
	public Rxaddress getRxMasterBillAddress(Integer theMasterID, String theOper)
			throws JobException {
		
		itsLogger.info("opertor checking::"+theOper);
		
		
		String aSelectQry = "";
		if (theOper.equalsIgnoreCase("bill")) {
			aSelectQry = "SELECT rxAddress.address1,rxAddress.address2,rxAddress.city,rxAddress.state,rxAddress.zip,rxAddress.isBillto,"
			           + "rxAddress.rxAddressId, rxMaster.Name,rxAddress.coTaxTerritoryID FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID "
			           + " WHERE rxAddress.rxMasterId ="+ theMasterID + " AND IsMailing = 1 order by rxAddress.rxAddressId asc limit 1";
		} else if (theOper.equalsIgnoreCase("ship")) {
		//	aSelectQry = "Select address1,address2,city,state,zip,isBillto,rxAddressId, name from rxAddress where rxMasterId ="
			aSelectQry = "Select rxA.address1,rxA.address2,rxA.city,rxA.state,rxA.zip,rxA.isBillto,rxA.rxAddressId,rxA.name,rxM.Name as cuSName,rxA.coTaxTerritoryID from rxAddress rxA,rxMaster rxM where rxM.rxMasterId =rxA.rxMasterId and rxA.rxMasterId="
					+ theMasterID + " AND isShipTo = 1 order by rxA.rxAddressId asc limit 1";
			itsLogger.info("Ship to Address:: "+aSelectQry);
		} else if (theOper.equalsIgnoreCase("shipTo")) {
		//	aSelectQry = "Select address1,address2,city,state,zip,isBillto,rxAddressId, name from rxAddress where rxAddressId ="
			aSelectQry = "Select rxA.address1,rxA.address2,rxA.city,rxA.state,rxA.zip,rxA.isBillto,rxA.rxAddressId,rxA.name,rxM.Name as cuSName,rxA.coTaxTerritoryID from rxAddress rxA,rxMaster rxM where rxM.rxMasterId =rxA.rxMasterId and rxA.rxMasterId="
					+ theMasterID + " AND otherShipTo = 3";
			itsLogger.info("Ship to Address:: "+aSelectQry);
		}else if (theOper.equalsIgnoreCase("shipToOther")) {
			aSelectQry = "SELECT address1,address2,city,state,zip,isBillto,"
			           + "rxAddressId,rxAddress.name  FROM rxAddress LEFT JOIN rxMaster rxmaster ON rxAddress.rxMasterID = rxmaster.rxMasterID  "
			           + " WHERE rxAddressId ="+ theMasterID; // + " AND otherShipTo = 3";
			itsLogger.info("Ship to Address:: "+aSelectQry);
		}else if (theOper.equalsIgnoreCase("billTo")) {
			aSelectQry = "SELECT rxAddress.address1,rxAddress.address2,rxAddress.city,rxAddress.state,rxAddress.zip,rxAddress.isBillto,"
			           + "rxAddress.rxAddressId, rxMaster.Name FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID "
			           + " WHERE rxAddress.rxAddressId ="+ theMasterID +" AND IsMailing = 1 order by rxAddress.rxAddressId asc limit 1";// + " AND otherBillTo = 2";
		}
		else if(theOper.equalsIgnoreCase("shipToCus"))
		{
			aSelectQry = "SELECT rxAddress.address1,rxAddress.address2,rxAddress.city,rxAddress.state,rxAddress.zip,rxAddress.isBillto,"
			           + "rxAddress.rxAddressId, rxMaster.name,rxMaster.Name,rxAddress.coTaxTerritoryID FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID "
			           + " WHERE rxAddress.rxAddressId ="+ theMasterID + " AND rxAddress.isShipTo = 1";
			itsLogger.info("Ship to Address:: "+aSelectQry);
		}
		
	/*	String aSelectNameQuery = "Select Name from rxMaster where rxMasterId = "
				+ theMasterID;*/
		Session aSession = null;
		Rxaddress aRxaddress = null;
		List<?> aQueryList = null;
		
		aSession = itsSessionFactory.openSession();
		Query aQuery = aSession.createSQLQuery(aSelectQry);
		Iterator<?> aIterator = null;
		try {
			
			aQueryList = aQuery.list();
			
			if(aQueryList.isEmpty())
			{
				/*aSelectQry = "Select address1,address2,city,state,zip,isBillto,rxAddressId, name from rxAddress where rxMasterId ="
						+ theMasterID + " AND IsDefault = 1";*/
				aSelectQry = "Select rxA.address1,rxA.address2,rxA.city,rxA.state,rxA.zip,rxA.isBillto,rxA.rxAddressId,rxA.name,rxM.Name as cuSName,rxA.coTaxTerritoryID from rxAddress rxA,rxMaster rxM where rxM.rxMasterId =rxA.rxMasterId and rxA.rxMasterId="
						+ theMasterID + " AND IsDefault = 1";
				aQueryList = aSession.createSQLQuery(aSelectQry).list();
			}
			
			itsLogger.info("aSelectQry"+aSelectQry);
			
			aIterator = aQueryList.iterator();
			aRxaddress = new Rxaddress();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				if ((String) aObj[0] != "" && (String) aObj[0] != null) {
					aRxaddress.setAddress1((String) aObj[0]);
				} else {
					aRxaddress.setAddress1("");
				}
				if ((String) aObj[1] != "" && (String) aObj[1] != null) {
					aRxaddress.setAddress2((String) aObj[1]);
				} else {
					aRxaddress.setAddress2("");
				}
				if ((String) aObj[2] != "" && (String) aObj[2] != null) {
					aRxaddress.setCity((String) aObj[2]);
					String aCity = aRxaddress.getCity().trim();
					aRxaddress.setCity(aCity);
				} else {
					aRxaddress.setCity("");
				}
				if ((String) aObj[3] != "" && (String) aObj[3] != null) {
					aRxaddress.setState((String) aObj[3]);
				} else {
					aRxaddress.setState("");
				}
				if ((String) aObj[4] != "" && (String) aObj[4] != null) {
					aRxaddress.setZip((String) aObj[4]);
				} else {
					aRxaddress.setZip("");
				}
				/*aRxaddress.setRxAddressId((Integer) aObj[6]);
				Query aQuery1 = aSession.createSQLQuery(aSelectNameQuery);
				Iterator<?> aIterator1 = aQuery1.list().iterator();
				while (aIterator1.hasNext()) {
					String aObj1 = (String) aIterator1.next();
					aRxaddress.setName(aObj1);
					System.out.println("Name------->" + aRxaddress.getName());
				}*/
			/*if (theOper.equalsIgnoreCase("billTo")) {
					aRxaddress.setName((String) aObj[7]);
				}
				if (theOper.equalsIgnoreCase("shipTo")) {
					aRxaddress.setName((String) aObj[7]);
				}*/
				
				itsLogger.info("==>==>"+theOper);
				
				
				if(theOper.equalsIgnoreCase("shipToOther")||theOper.equalsIgnoreCase("bill")||theOper.equalsIgnoreCase("billTo"))
				{
					
					aRxaddress.setName((String) aObj[7]);
					
				}
				else if(theOper.equalsIgnoreCase("shipToCus"))
				{
					aRxaddress.setName((String) aObj[8]);
					if(aObj[9]!=null){
					aRxaddress.setCoTaxTerritoryId((Integer)aObj[9]);
					}
				}
				else
				{
					itsLogger.info((String) aObj[7]+"Sample Address::::"+(String) aObj[8]+"::"+(Integer)aObj[9]);
					aRxaddress.setName((String) aObj[8]);
					if(aObj[9]!=null){
					aRxaddress.setCoTaxTerritoryId((Integer)aObj[9]);
					}
				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			if (aQueryList.isEmpty()) {
				aRxaddress.setAddress1("");
				aRxaddress.setAddress2("");
				aRxaddress.setCity("");
				aRxaddress.setState("");
				aRxaddress.setZip("");
			}
			aSession.flush();
			aSession.close();
			aSelectQry=null;
			aQueryList = null;
			aIterator = null; 
		}
		return aRxaddress;
	}

	@Override
	public Integer getVepoId(Integer thejoReleaseId) throws JobException {
		String aJobSelectQry = "SELECT vePOId FROM vePO WHERE joReleaseID ="
				+ thejoReleaseId;
		Session aSession = null;
		Integer theVepoId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				theVepoId = 0;
			} else {
				theVepoId = (Integer) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return theVepoId;
	}

	@Override
	public String getNewPONumber(String theJobNumber, Integer theVePOId)
			throws JobException {
		String aJobSelectQry = "SELECT poNumber,vePOID FROM vePO WHERE poNumber like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJobNumber.trim()) + "%' ORDER BY vePOID DESC LIMIT 1";
		String aJobSotQry = "SELECT SONumber,cuSOID from cuSO where SONumber like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJobNumber.trim()) + "%' ORDER BY cuSOID DESC LIMIT 1";
		Session aSession = null;
		String thePoNumber = null;
		String aPoNumber = null;
		String aSoNumber = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Query soQuery = aSession.createSQLQuery(aJobSotQry);
			List<?> aList = aQuery.list();
			List<?> bList = soQuery.list();
			if (aList.isEmpty() && bList.isEmpty()) {
				thePoNumber = theJobNumber + "A";
			}
			/*
			 * else if(soQuery.list().isEmpty()) { thePoNumber = theJobNumber +
			 * "A"; }
			 */
			else {
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aPoNumber = (String) aObj[0];
				}
				Iterator<?> soIterator = bList.iterator();
				while (soIterator.hasNext()) {
					Object[] soObj = (Object[]) soIterator.next();
					aSoNumber = (String) soObj[0];
				}

				if (aPoNumber == null) {
					aPoNumber = "0";
				}
				if (aSoNumber == null) {
					aSoNumber = "0";
				}
				if (aPoNumber.matches(".*[a-zA-Z]+.*")
						&& aSoNumber.matches(".*[a-zA-Z]+.*")) {
					int poNumChar = 0;
					int soNumChar = 0;
					poNumChar = (int) aPoNumber.charAt(aPoNumber.length() - 1);
					soNumChar = (int) aSoNumber.charAt(aSoNumber.length() - 1);
					itsLogger.info("The PO SO Last Char: " + poNumChar + "   "
							+ soNumChar);
					if (poNumChar > soNumChar) {
						thePoNumber = aPoNumber;

					} else {
						thePoNumber = aSoNumber;

					}
				} else if (aPoNumber.matches(".*[a-zA-Z]+.*")
						&& !aSoNumber.matches(".*[a-zA-Z]+.*")) {
					thePoNumber = aPoNumber;
				} else if (!aPoNumber.matches(".*[a-zA-Z]+.*")
						&& aSoNumber.matches(".*[a-zA-Z]+.*")) {
					thePoNumber = aSoNumber;
				}
				char theLastChar = thePoNumber.charAt(thePoNumber.length() - 1);
				int theIndex = (int) theLastChar;
				if (theIndex >= 65 && theIndex < 90) {
					String theSecondLastChar = Character.toString(thePoNumber
							.charAt(thePoNumber.length() - 2));
					if (theSecondLastChar.matches("[0-9]")) {
						theIndex = theIndex + 1;
						theLastChar = (char) theIndex;
						thePoNumber = theJobNumber + theLastChar;
					} else {
						theIndex = theIndex + 1;
						theLastChar = (char) theIndex;
						thePoNumber = theJobNumber + theSecondLastChar
								+ theLastChar;
					}
				} else if (theLastChar == 'Z') {
					char theSecondLastChar = thePoNumber.charAt(thePoNumber
							.length() - 2);
					int thesecondIndex = (int) theSecondLastChar;
					if (thesecondIndex >= 65 && thesecondIndex < 90) {
						thesecondIndex = thesecondIndex + 1;
						theSecondLastChar = (char) thesecondIndex;
						thePoNumber = theJobNumber + theSecondLastChar + "A";
					} else {
						thePoNumber = theJobNumber + "A" + "A";
					}

				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;aJobSotQry=null;aPoNumber=null;aSoNumber=null;
		}
		return thePoNumber;
	}

	@Override
	public boolean updatePOLineItemUpDownPosition(
			joQuoteDetailPosition theJoQuoteDetailPosition) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Vepodetail aVepodetail = (Vepodetail) aSession.get(
					Vepodetail.class,
					theJoQuoteDetailPosition.getSelectedQuoteDetailID());
			aVepodetail.setPosistion(theJoQuoteDetailPosition
					.getAbovePositionDetailID());
			aSession.update(aVepodetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public boolean updatePOLineItemUpPosition(
			joQuoteDetailPosition theoQuoteDetailPosition) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Vepodetail aVepodetail = (Vepodetail) aSession.get(
					Vepodetail.class,
					theoQuoteDetailPosition.getAboveQuoteDetailID());
			aVepodetail.setPosistion(theoQuoteDetailPosition
					.getSelectedPositionDetailID());
			aSession.update(aVepodetail);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public Integer getVeBillID(Integer theReleaseDetailID, Integer theVePOId)
			throws JobException {
		String aSelectQry = "";
		if (theReleaseDetailID != null) {
			aSelectQry = "SELECT veBillID FROM veBill WHERE joReleaseDetailID = '"
					+ theReleaseDetailID + "'";
		}
		if (theVePOId != null) {
			aSelectQry = "SELECT veBillID FROM veBill WHERE vePOID = '"
					+ theVePOId + "'";
		}
		if (theReleaseDetailID != null && theVePOId != null) {
			aSelectQry = "SELECT veBillID FROM veBill WHERE vePOID = '"
					+ theVePOId + "' AND joReleaseDetailID = '"
					+ theReleaseDetailID + "'";
		}
		if (aSelectQry.isEmpty()) {
			itsLogger.error("releaseDetailId and vePOId both are NULL");
			JobException aJobException = new JobException(
					"releaseDetailId and vePOId both are NULL");
			throw aJobException;
		}
		Session aSession = null;
		Integer aBillId = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				aBillId = null;
			} else {
				aBillId = (Integer) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aBillId;
	}

	@Override
	public Vebill getVeBillDetails(Integer theVeBillID) throws JobException {
		Session aSession = null;
		Vebill aVebill = null;
		try {
			aSession = itsSessionFactory.openSession();
			aVebill = (Vebill) aSession.get(Vebill.class, theVeBillID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVebill;
	}

	@Override
	public JoQuoteHeader getjoQuoteAmount(Integer thejoQuoteHeaderId)
			throws JobException {
		String aSelectQry = "SELECT joQuoteHeaderID,quoteAmount,joMasterID,Remarks,QuoteRev,FullName FROM joQuoteHeader jh LEFT JOIN tsUserLogin tsu ON(tsu.UserLoginID=jh.CreatedByID) WHERE jh.joQuoteHeaderID = '"
				+ thejoQuoteHeaderId + "'";
		Session aSession = null;
		JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aJoQuoteHeader.setJoQuoteHeaderId((Integer) aObj[0]);
				aJoQuoteHeader.setQuoteAmount((BigDecimal) aObj[1]);
				aJoQuoteHeader.setJoMasterID((Integer) aObj[2]);
				aJoQuoteHeader.setRemarks((String) aObj[3]);
				aJoQuoteHeader.setQuoteRev((String) aObj[4]);
				if(aObj[5]!=null){
					aJoQuoteHeader.setCreatedByName((String) aObj[5]);
				}else{
					aJoQuoteHeader.setCreatedByName("  ");
				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aJoQuoteHeader;
	}

	@Override
	public Integer getCustomerInoiveID(Integer theReleaseDetailID)
			throws JobException {
		String aSelectQry = "SELECT cuInvoiceID FROM cuInvoice WHERE joReleaseDetailID = '"
				+ theReleaseDetailID + "'";
		Session aSession = null;
		Integer aCuInvoice = 0;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				aCuInvoice = null;
			} else {
				aCuInvoice = (Integer) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aCuInvoice;
	}

	@Override
	public Cuinvoice getCustomerInvoiceDetails(Integer theInvoiceID)
			throws JobException {
		Session aSession = null;
		Cuinvoice aCuInvoice = null;
		String cuTermsDesc="";
		String coTaxTerritoryDesc="";
		try {
			aSession = itsSessionFactory.openSession();
			aCuInvoice = (Cuinvoice) aSession
					.get(Cuinvoice.class, theInvoiceID);
			cuTermsDesc = getCuTermsName(aCuInvoice.getCuTermsId());
			coTaxTerritoryDesc = getCoTaxterritoryName(aCuInvoice.getCoTaxTerritoryId());
			aCuInvoice.setDescription(cuTermsDesc);
			aCuInvoice.setCotaxdescription(coTaxTerritoryDesc);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			cuTermsDesc=null;coTaxTerritoryDesc=null;
		}
		return aCuInvoice;
	}

	public String getCuTermsName(Integer termsID)
			throws JobException {
		String aSelectQry = "SELECT Description FROM cuTerms WHERE cuTermsID = "+ termsID;
		Session aSession = null;
		String termsDesc = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				termsDesc = null;
			} else {
				termsDesc = (String) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return termsDesc;
	}
	public String getCoTaxterritoryName(Integer coTaxTerritoryID)
			throws JobException {
		String aSelectQry = "SELECT County FROM coTaxTerritory WHERE coTaxTerritoryID = "+ coTaxTerritoryID;
		Session aSession = null;
		String coTaxTerritoryDesc = "";
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				coTaxTerritoryDesc = null;
			} else {
				coTaxTerritoryDesc = (String) aList.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return coTaxTerritoryDesc;
	}
	
	@Override
	public Cuinvoice updateCusotmerInvoiceDetails(Cuinvoice theCuinvoice)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Cuinvoice aCuinvoice = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
					theCuinvoice.getCuInvoiceId());
			aCuinvoice.setInvoiceDate(theCuinvoice.getInvoiceDate());
			// aCuinvoice.setInvoiceNumber(theCuinvoice.getInvoiceNumber());
			aCuinvoice.setDueDate(theCuinvoice.getDueDate());
			aCuinvoice.setShipDate(theCuinvoice.getShipDate());
			if (theCuinvoice.getInvoiceAmount() != null) {
				aCuinvoice.setInvoiceAmount(theCuinvoice.getInvoiceAmount());
			} else{
				aCuinvoice.setInvoiceAmount((theCuinvoice.getSubtotal()==null?new BigDecimal("0.000"):theCuinvoice.getSubtotal()).add((theCuinvoice.getTaxTotal()==null?new BigDecimal("0.0000"):theCuinvoice.getTaxTotal())));
			}
			aCuinvoice.setPrFromWarehouseId(theCuinvoice.getPrFromWarehouseId());
			aCuinvoice.setPrToWarehouseId(theCuinvoice.getPrToWarehouseId());
			aCuinvoice.setVeShipViaId(theCuinvoice.getVeShipViaId());
			aCuinvoice.setTrackingNumber(theCuinvoice.getTrackingNumber());
			aCuinvoice.setFreight(theCuinvoice.getFreight());
			aCuinvoice.setTaxRate(theCuinvoice.getTaxRate());
			aCuinvoice.setTaxAmount(theCuinvoice.getTaxAmount());
			//aCuinvoice.setCostTotal(theCuinvoice.getCostTotal());
			aCuinvoice.setSubtotal(theCuinvoice.getSubtotal());
			aCuinvoice.setCustomerPonumber(theCuinvoice.getCustomerPonumber());
			aCuinvoice.setCuAssignmentId0(theCuinvoice.getCuAssignmentId0());
			aCuinvoice.setCuAssignmentId1(theCuinvoice.getCuAssignmentId1());
			aCuinvoice.setCuAssignmentId2(theCuinvoice.getCuAssignmentId2());
			aCuinvoice.setCuAssignmentId3(theCuinvoice.getCuAssignmentId3());
			aCuinvoice.setCuAssignmentId4(theCuinvoice.getCuAssignmentId4());
			aCuinvoice.setCoTaxTerritoryId(theCuinvoice.getCoTaxTerritoryId());
			aCuinvoice.setCoDivisionId(theCuinvoice.getCoDivisionId());
			aCuinvoice.setDoNotMail(theCuinvoice.getDoNotMail());
			aCuinvoice.setCuInvoiceId(theCuinvoice.getCuInvoiceId());
			aCuinvoice.setTaxRate(theCuinvoice.getTaxRate());
			aCuinvoice.setCuTermsId(theCuinvoice.getCuTermsId());
			aCuinvoice.setRxContactId(theCuinvoice.getRxContactId());
			aCuinvoice.setDescription(theCuinvoice.getDescription());
			aCuinvoice.setIscredit(theCuinvoice.getIscredit());
			aCuinvoice.setCreditmemo(theCuinvoice.getCreditmemo());
			aCuinvoice.setMemoStatus(theCuinvoice.getMemoStatus());
			aCuinvoice.setChangedById(theCuinvoice.getChangedById());
			aCuinvoice.setChangedOn(theCuinvoice.getChangedOn());
			if(theCuinvoice.getcIopenStatus()==false)
			{
			aCuinvoice.setcIopenStatus(theCuinvoice.getcIopenStatus());
			aCuinvoice.setReason(theCuinvoice.getReason());
			}
			
			aCuinvoice.setJobnoDescription(theCuinvoice.getJobnoDescription());
			if (theCuinvoice.getRxShipToId() != null
					&& theCuinvoice.getRxShipToId() != 0)
				aCuinvoice.setRxShipToId(theCuinvoice.getRxShipToId());
			if (theCuinvoice.getRxBillToId() != null
					&& theCuinvoice.getRxBillToId() != 0)
				aCuinvoice.setRxBillToId(theCuinvoice.getRxBillToId());
			if (theCuinvoice.getRxShipToAddressId() != null
					&& theCuinvoice.getRxShipToAddressId() != 0)
				aCuinvoice.setRxShipToAddressId(theCuinvoice
						.getRxShipToAddressId());
			//if (theCuinvoice.getShipToMode() != 0)
				aCuinvoice.setShipToMode(theCuinvoice.getShipToMode());
			if (theCuinvoice.getRxCustomerId() != null
					&& theCuinvoice.getRxCustomerId() != 0)
				aCuinvoice.setRxCustomerId(theCuinvoice.getRxCustomerId());
			if (theCuinvoice.getRxShipToId() != null
					&& theCuinvoice.getRxShipToId() != 0)
				aCuinvoice.setRxShipToId(theCuinvoice.getRxShipToId());
			
			aCuinvoice.setPrToWarehouseId(theCuinvoice.getPrToWarehouseId());
			aCuinvoice.setTaxfreight(theCuinvoice.getTaxfreight());
			aSession.update(aCuinvoice);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuinvoice;
	}

	@Override
	public Integer saveCustomerInvoiceLog(Cuinvoice oldCuinvoice,Cuinvoice newCuinvoice,String transType,Integer transStatus,Integer periodID,Integer yearID) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Cuinvoice aCuinvoice = null;
		TpCuinvoiceLogMaster aTpCuinvoiceLogMaster = null;
		TpCuinvoiceLogMaster bTpCuinvoiceLogMaster = null;
		CoTaxTerritory aCoTaxTerritory = null;
		Integer savedStatus = 0;
		BigDecimal TaxableSales = new BigDecimal("0.0000");
		BigDecimal newTaxAmt = new BigDecimal("0.0000");
		BigDecimal newSubTotal = new BigDecimal("0.0000");
		BigDecimal newSubTotal2 = new BigDecimal("0.0000");
		BigDecimal newFreight = new BigDecimal("0.0000");
		BigDecimal newAppliedAmt = new BigDecimal("0.0000");
		BigDecimal newDiscountAmt = new BigDecimal("0.0000");
		BigDecimal newTaxableSales=new BigDecimal("0.0000");
		BigDecimal newNonTaxableSales = new BigDecimal("0.0000");
		BigDecimal NonTaxableSales = new BigDecimal("0.0000");
		BigDecimal TaxAmount = new BigDecimal("0.0000");
		BigDecimal StateTax = new BigDecimal("0.0000");
		BigDecimal CityTax = new BigDecimal("0.0000");
		BigDecimal CountyTax = new BigDecimal("0.0000");
		BigDecimal MTATax = new BigDecimal("0.0000");
		BigDecimal CCDTax = new BigDecimal("0.0000");
		BigDecimal OtherTax = new BigDecimal("0.0000");
		String createdByName="",changedByName ="";
		boolean rollbackstatus = false;
		try {
			aTpCuinvoiceLogMaster = new TpCuinvoiceLogMaster();
			Cofiscalperiod aCofiscalperiod =null;
			Cofiscalyear aCofiscalyear = null;
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if(transType.equals("CI-Edited-Payment")){
				aCuinvoice=newCuinvoice;
			}else{
				aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,newCuinvoice.getCuInvoiceId());		
			}
			
			if(oldCuinvoice==null){
				oldCuinvoice = aCuinvoice;
			}
			else
			{
				if(oldCuinvoice.getCuInvoiceId()!=null)
				{
					
					if(transType.equals("CI-Edited")||transType.equals("CI-Line Item(s) Deleted")||transType.equals("CI-Edited-Tax Adjustments")||transType.equals("CI-Edited-Payment"))
					{
						if( transStatus == 1 && (
								(aCuinvoice.getInvoiceAmount()==null?BigDecimal.ZERO:aCuinvoice.getInvoiceAmount()).compareTo(oldCuinvoice.getInvoiceAmount()==null?BigDecimal.ZERO:oldCuinvoice.getInvoiceAmount())!=0 ||
								(aCuinvoice.getSubtotal()==null?BigDecimal.ZERO:aCuinvoice.getSubtotal()).compareTo(oldCuinvoice.getSubtotal()==null?BigDecimal.ZERO:oldCuinvoice.getSubtotal())!=0 || 
								(aCuinvoice.getFreight()==null?BigDecimal.ZERO:aCuinvoice.getFreight()).compareTo(oldCuinvoice.getFreight()==null?BigDecimal.ZERO:oldCuinvoice.getFreight())!=0 ||
								aCuinvoice.getCoTaxTerritoryId()!=oldCuinvoice.getCoTaxTerritoryId()||(aCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:aCuinvoice.getDiscountAmt()).compareTo(oldCuinvoice.getDiscountAmt()==null?BigDecimal.ZERO:oldCuinvoice.getDiscountAmt())!=0))
						rollbackstatus = invoicelogRollbackentry(aCuinvoice.getCuInvoiceId(),aCuinvoice);
					}
				}
			}
			
			bTpCuinvoiceLogMaster =getDataforInvoiceLog(aCuinvoice.getCuInvoiceId());
			aCoTaxTerritory = getCoTaxTerritoryDetails(aCuinvoice.getCoTaxTerritoryId());
			
		/*	newTaxAmt = (aCuinvoice.getTaxAmount()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxAmount()).subtract(oldCuinvoice.getTaxAmount()==null?new BigDecimal("0.0000"):oldCuinvoice.getTaxAmount());
			newSubTotal = (aCuinvoice.getSubtotal()==null?new BigDecimal("0.0000"):aCuinvoice.getSubtotal()).subtract(oldCuinvoice.getSubtotal()==null?new BigDecimal("0.0000"):oldCuinvoice.getSubtotal());
			newFreight = (aCuinvoice.getFreight()==null?new BigDecimal("0.0000"):aCuinvoice.getFreight()).subtract(oldCuinvoice.getFreight()==null?new BigDecimal("0.0000"):oldCuinvoice.getFreight());
			newAppliedAmt = (aCuinvoice.getAppliedAmount()==null?new BigDecimal("0.0000"):aCuinvoice.getAppliedAmount()).subtract(oldCuinvoice.getAppliedAmount()==null? new BigDecimal("0.0000"):oldCuinvoice.getAppliedAmount());*/
			
//			List<String> addlist=new ArrayList<String>();
//			addlist.add("RequireFreightwhencalculatingTaxonCustomerInvoices");
//			ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
			int taxfreight=aCuinvoice.getTaxfreight();
			
			newTaxAmt = (aCuinvoice.getTaxAmount()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxAmount());
			newSubTotal = (aCuinvoice.getSubtotal()==null?new BigDecimal("0.0000"):aCuinvoice.getSubtotal());
			newFreight = (aCuinvoice.getFreight()==null?new BigDecimal("0.0000"):aCuinvoice.getFreight());
			newAppliedAmt = (aCuinvoice.getAppliedAmount()==null?new BigDecimal("0.0000"):aCuinvoice.getAppliedAmount());
			newDiscountAmt = (aCuinvoice.getDiscountAmt()==null?new BigDecimal("0.0000"):aCuinvoice.getDiscountAmt());
			newTaxableSales=(aCuinvoice.getTaxableSales()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxableSales());
			newNonTaxableSales=(aCuinvoice.getNonTaxableSales()==null?new BigDecimal("0.0000"):aCuinvoice.getNonTaxableSales());
			//TaxableSales  = (newTaxAmt.compareTo(new BigDecimal("0.0000"))!=0?newSubTotal.add(sysvariablelist.get(0).getValueLong()==1?newFreight:new BigDecimal("0.0000")).subtract(newDiscountAmt):new BigDecimal("0.0000"));
			TaxableSales  = newTaxableSales.subtract(newDiscountAmt);
			if(transType.equals("CI-PDF-Viewed")){
				NonTaxableSales = new BigDecimal("0.0000");
			}
			else{
//				NonTaxableSales = (newTaxAmt.compareTo(new BigDecimal("0.0000"))==0?newSubTotal.add(taxfreight==1?newFreight:new BigDecimal("0.0000")).subtract(newDiscountAmt):new BigDecimal("0.0000"));
				NonTaxableSales = newNonTaxableSales.subtract(newDiscountAmt);
			}
			//TaxAmount = JobUtil.floorFigureoverall((new BigDecimal("0.01")).multiply((newSubTotal).add(taxfreight==1?newFreight:new BigDecimal("0.0000")).subtract(newDiscountAmt)).multiply(aCuinvoice.getTaxRate()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxRate()),3);
			TaxAmount=aCuinvoice.getTaxAmount()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxAmount();
			
			CityTax = JobUtil.floorFigureoverall((new BigDecimal("0.01")).multiply(TaxableSales).multiply(aCoTaxTerritory.getDistribution1()==null?new BigDecimal("0.0000"):aCoTaxTerritory.getDistribution1()),2);
			CountyTax =JobUtil.floorFigureoverall((new BigDecimal("0.01")).multiply(TaxableSales).multiply(aCoTaxTerritory.getDistribution2()==null?new BigDecimal("0.0000"):aCoTaxTerritory.getDistribution2()),2);
			MTATax = JobUtil.floorFigureoverall((new BigDecimal("0.01")).multiply(TaxableSales).multiply(aCoTaxTerritory.getDistribution3()==null?new BigDecimal("0.0000"):aCoTaxTerritory.getDistribution3()),2);
			CCDTax = JobUtil.floorFigureoverall((new BigDecimal("0.01")).multiply(TaxableSales).multiply(aCoTaxTerritory.getDistribution4()==null?new BigDecimal("0.0000"):aCoTaxTerritory.getDistribution4()),2);
			OtherTax =JobUtil.floorFigureoverall((new BigDecimal("0.01")).multiply(TaxableSales).multiply(aCoTaxTerritory.getDistribution5()==null?new BigDecimal("0.0000"):aCoTaxTerritory.getDistribution5()),2);
			
			StateTax = JobUtil.floorFigureoverall(TaxAmount.subtract(CityTax).subtract(CountyTax).subtract(MTATax).subtract(CCDTax).subtract(OtherTax),2);
			
			//StateTax = JobUtil.floorFigureoverall(aCuinvoice.getTaxAmount().subtract(CityTax).subtract(CountyTax).subtract(MTATax).subtract(CCDTax).subtract(OtherTax),2);
			
			
			aTransaction.begin();
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,newCuinvoice.getCuInvoiceId());
			/*Customer Invoice Log*/
			if(aCuinvoice.getCreatedById()!=null && aCuinvoice.getCreatedById()!=-1){
				createdByName = ((TsUserLogin) aSession.get(TsUserLogin.class,aCuinvoice.getCreatedById()==null?0:aCuinvoice.getCreatedById())).getFullName();
			}
			if(aCuinvoice.getChangedById() != null && aCuinvoice.getCreatedById()!=-1){
				changedByName = ((TsUserLogin) aSession.get(TsUserLogin.class,aCuinvoice.getChangedById()==null?0:aCuinvoice.getChangedById())).getFullName();
			}
			aTpCuinvoiceLogMaster.setCuInvoiceId(aCuinvoice.getCuInvoiceId());
			aTpCuinvoiceLogMaster.setCreatedById(aCuinvoice.getCreatedById());
			aTpCuinvoiceLogMaster.setCreatedByName(createdByName);
			aTpCuinvoiceLogMaster.setCreatedOn(aCuinvoice.getCreatedOn());
			aTpCuinvoiceLogMaster.setChangedById(aCuinvoice.getChangedById());
			aTpCuinvoiceLogMaster.setChangedOn(aCuinvoice.getChangedOn());
			aTpCuinvoiceLogMaster.setChangedByName(changedByName);
			aTpCuinvoiceLogMaster.setJoReleaseDetailId(aCuinvoice.getJoReleaseDetailId());
			aTpCuinvoiceLogMaster.setCuSoid(aCuinvoice.getCuSoid());
			aTpCuinvoiceLogMaster.setRxCustomerId(aCuinvoice.getRxCustomerId());
			aTpCuinvoiceLogMaster.setTransType(transType);
			aTpCuinvoiceLogMaster.setPrFromWarehouseId(aCuinvoice.getPrFromWarehouseId());
			aTpCuinvoiceLogMaster.setPrToWarehouseId(aCuinvoice.getPrToWarehouseId());
			aTpCuinvoiceLogMaster.setRxShipToId(aCuinvoice.getRxShipToId());
			aTpCuinvoiceLogMaster.setRxShipToAddressId(aCuinvoice.getRxShipToAddressId());
			aTpCuinvoiceLogMaster.setShipToMode(aCuinvoice.getShipToMode());
			aTpCuinvoiceLogMaster.setCuTermsId(aCuinvoice.getCuTermsId());
			aTpCuinvoiceLogMaster.setCoTaxTerritoryId(aCuinvoice.getCoTaxTerritoryId());
			aTpCuinvoiceLogMaster.setInvoiceNumber(aCuinvoice.getInvoiceNumber());
			aTpCuinvoiceLogMaster.setCustomerPonumber(aCuinvoice.getCustomerPonumber());
			aTpCuinvoiceLogMaster.setInvoiceDate(aCuinvoice.getInvoiceDate());
			aTpCuinvoiceLogMaster.setShipDate(aCuinvoice.getShipDate());
			aTpCuinvoiceLogMaster.setDueDate(aCuinvoice.getDueDate());
			aTpCuinvoiceLogMaster.setPrintDate(aCuinvoice.getPrintDate());
			aTpCuinvoiceLogMaster.setCuAssignmentId0(aCuinvoice.getCuAssignmentId0());
			aTpCuinvoiceLogMaster.setCuAssignmentId1(aCuinvoice.getCuAssignmentId1());
			aTpCuinvoiceLogMaster.setCreditmemo(aCuinvoice.getCreditmemo());
			aTpCuinvoiceLogMaster.setMemoStatus(aCuinvoice.getMemoStatus());
			aTpCuinvoiceLogMaster.setIscredit(aCuinvoice.getIscredit());
			aTpCuinvoiceLogMaster.setReason(aCuinvoice.getReason());
			
			aTpCuinvoiceLogMaster.setSoNumber(bTpCuinvoiceLogMaster.getSoNumber());
			aTpCuinvoiceLogMaster.setPONumber(bTpCuinvoiceLogMaster.getPONumber());
			aTpCuinvoiceLogMaster.setJoReleaseID(bTpCuinvoiceLogMaster.getJoReleaseID());
			aTpCuinvoiceLogMaster.setJobName(bTpCuinvoiceLogMaster.getJobName());
			aTpCuinvoiceLogMaster.setJobNumber(bTpCuinvoiceLogMaster.getJobNumber());
			aTpCuinvoiceLogMaster.setCustomerName(bTpCuinvoiceLogMaster.getCustomerName());
			aTpCuinvoiceLogMaster.setSalesPerson(bTpCuinvoiceLogMaster.getSalesPerson());
			
			aTpCuinvoiceLogMaster.setInvoiceAmount(aCuinvoice.getInvoiceAmount()==null?aCuinvoice.getInvoiceAmount():aCuinvoice.getInvoiceAmount());
			aTpCuinvoiceLogMaster.setLastInvoiceAmount(oldCuinvoice.getInvoiceAmount()==null?new BigDecimal("0.0000"):oldCuinvoice.getInvoiceAmount());
			aTpCuinvoiceLogMaster.setAppliedAmount(aCuinvoice.getAppliedAmount()==null?aCuinvoice.getAppliedAmount():aCuinvoice.getAppliedAmount());
			aTpCuinvoiceLogMaster.setLastAppliedAmount(oldCuinvoice.getAppliedAmount()==null?new BigDecimal("0.0000"):oldCuinvoice.getAppliedAmount());
			aTpCuinvoiceLogMaster.setDiscountAmt(aCuinvoice.getDiscountAmt()==null?new BigDecimal("0.0000"):aCuinvoice.getDiscountAmt());
			aTpCuinvoiceLogMaster.setLastDiscountAmt(oldCuinvoice.getDiscountAmt()==null?new BigDecimal("0.0000"):oldCuinvoice.getDiscountAmt());
			
			if(newDiscountAmt.compareTo(BigDecimal.ZERO)>0)
			aTpCuinvoiceLogMaster.setTaxAmount(JobUtil.floorFigureoverall((TaxableSales.multiply(new BigDecimal("0.01"))).multiply(aCuinvoice.getTaxRate()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxRate()),2));
			else
			aTpCuinvoiceLogMaster.setTaxAmount(aCuinvoice.getTaxAmount()==null?aCuinvoice.getTaxAmount():aCuinvoice.getTaxAmount());
			
	        /*if(rollbackstatus)
			aTpCuinvoiceLogMaster.setLastTaxAmount(oldCuinvoice.getTaxAmount().negate());
			else*/
			aTpCuinvoiceLogMaster.setLastTaxAmount(oldCuinvoice.getTaxAmount()==null?new BigDecimal("0.0000"):oldCuinvoice.getTaxAmount());
			
			aTpCuinvoiceLogMaster.setFreight(aCuinvoice.getFreight()==null?new BigDecimal("0.0000"):aCuinvoice.getFreight());
			aTpCuinvoiceLogMaster.setLastFreight(oldCuinvoice.getFreight()==null?new BigDecimal("0.0000"):oldCuinvoice.getFreight());
			aTpCuinvoiceLogMaster.setCostTotal(aCuinvoice.getCostTotal()==null?new BigDecimal("0.0000"):aCuinvoice.getCostTotal());
			aTpCuinvoiceLogMaster.setLastCostTotal(oldCuinvoice.getCostTotal()==null?new BigDecimal("0.0000"):oldCuinvoice.getCostTotal());
			aTpCuinvoiceLogMaster.setSubtotal(aCuinvoice.getSubtotal()==null?new BigDecimal("0.0000"):aCuinvoice.getSubtotal());
			aTpCuinvoiceLogMaster.setLastSubtotal(oldCuinvoice.getSubtotal()==null?new BigDecimal("0.0000"):oldCuinvoice.getSubtotal());
			aTpCuinvoiceLogMaster.setTaxRate(aCuinvoice.getTaxRate()==null?new BigDecimal("0.0000"):aCuinvoice.getTaxRate());
			aTpCuinvoiceLogMaster.setLastTaxRate(oldCuinvoice.getTaxRate()==null?new BigDecimal("0.0000"):oldCuinvoice.getTaxRate());
			aTpCuinvoiceLogMaster.setAddCost(aCuinvoice.getFreightCost()==null?new BigDecimal("0.0000"):aCuinvoice.getFreightCost());
			aTpCuinvoiceLogMaster.setLastAddCost(oldCuinvoice.getFreightCost()==null?new BigDecimal("0.0000"):oldCuinvoice.getFreightCost());
			aTpCuinvoiceLogMaster.setSalesPersionId(aCuinvoice.getCuAssignmentId0());
			
			aTpCuinvoiceLogMaster.setState(aCoTaxTerritory.getState());
			aTpCuinvoiceLogMaster.setCounty(aCoTaxTerritory.getCounty());
			aTpCuinvoiceLogMaster.setCountyCode(aCoTaxTerritory.getCountyCode());
			aTpCuinvoiceLogMaster.setTaxDistribution1(aCoTaxTerritory.getDistribution1());
			aTpCuinvoiceLogMaster.setTaxDistribution2(aCoTaxTerritory.getDistribution2());
			aTpCuinvoiceLogMaster.setTaxDistribution3(aCoTaxTerritory.getDistribution3());
			aTpCuinvoiceLogMaster.setTaxDistribution4(aCoTaxTerritory.getDistribution4());
			aTpCuinvoiceLogMaster.setTaxDistribution5(aCoTaxTerritory.getDistribution5());
			aTpCuinvoiceLogMaster.setIsTaxShipping(aCuinvoice.getTaxfreight()==1?(byte)1:(byte)0);
			
			aTpCuinvoiceLogMaster.setTaxableSales(TaxableSales);
			aTpCuinvoiceLogMaster.setNonTaxableSales(NonTaxableSales);
			
			aTpCuinvoiceLogMaster.setStateTax(StateTax);
			aTpCuinvoiceLogMaster.setCityTax(CityTax);
			aTpCuinvoiceLogMaster.setCountyTax(CountyTax);
			aTpCuinvoiceLogMaster.setMTATax(MTATax);
			aTpCuinvoiceLogMaster.setCCDTax(CCDTax);
			aTpCuinvoiceLogMaster.setOtherTax(OtherTax);
			aTpCuinvoiceLogMaster.setTransStatus(transStatus);
			aTpCuinvoiceLogMaster.setLastcoTaxTerritoryId(oldCuinvoice.getCoTaxTerritoryId());
			
			// If period = -1 means, PDF VieWed, Email Sent
			if(periodID!=-1) 
			{
			// period and year store for time of invoice created or modify	
			 aCofiscalperiod =accountingCyclesService.getCurrentPeriod(periodID);
			 aCofiscalyear = accountingCyclesService.getCurrentYear(yearID);
			 aTpCuinvoiceLogMaster.setFperiod(aCofiscalperiod.getPeriod());
			 aTpCuinvoiceLogMaster.setFyear(aCofiscalyear.getFiscalYear());
			}
			else
			{
				// place the invoice created period
				Integer logidfor = (Integer) aSession.createSQLQuery("select max(tpCuInvoiceLogMasterID) from tpCuInvoiceLogMaster where transStatus<>0 and cuInvoiceId = "+newCuinvoice.getCuInvoiceId()).uniqueResult();
				TpCuinvoiceLogMaster aTpCuinvoiceLogMasterinve =null;
				if(logidfor!=null)
				{
				 aTpCuinvoiceLogMasterinve =(TpCuinvoiceLogMaster) aSession.get(TpCuinvoiceLogMaster.class,logidfor); 
				}	
				
			 aTpCuinvoiceLogMaster.setFperiod(aTpCuinvoiceLogMasterinve.getFperiod());
			 aTpCuinvoiceLogMaster.setFyear(aTpCuinvoiceLogMasterinve.getFyear());
			}
			
			savedStatus = (Integer) aSession.save(aTpCuinvoiceLogMaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return savedStatus;
	}
	
	@Override
	public boolean invoicelogRollbackentry(Integer cuInvoiceid,Cuinvoice newCuinvoice) throws JobException 
	{
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		TpCuinvoiceLogMaster aTpCuinvoiceLogMaster = new TpCuinvoiceLogMaster();
		TpCuinvoiceLogMaster aRollbacklog = new TpCuinvoiceLogMaster();
		boolean rollbackstatus = false;
		try {
			
			aTransaction = aSession.beginTransaction();	
			Integer logid = (Integer) aSession.createSQLQuery("select max(tpCuInvoiceLogMasterID) from tpCuInvoiceLogMaster where transStatus<>0 and cuInvoiceId = "+cuInvoiceid).uniqueResult();
		
			if(logid!=null)
			{
			 aTpCuinvoiceLogMaster =(TpCuinvoiceLogMaster) aSession.get(TpCuinvoiceLogMaster.class,logid); 
			 
			 
			if(aTpCuinvoiceLogMaster.getCoTaxTerritoryId()!=newCuinvoice.getCoTaxTerritoryId() || aTpCuinvoiceLogMaster.getInvoiceAmount().compareTo(newCuinvoice.getInvoiceAmount())!=0 || aTpCuinvoiceLogMaster.getTransType().equals("CI-Edited")||aTpCuinvoiceLogMaster.getTransType().equals("CI-Line Item(s) Deleted")||aTpCuinvoiceLogMaster.getTransType().equals("CI-Edited-Tax Adjustments"))
				rollbackstatus = true;
			else if(aTpCuinvoiceLogMaster.getTransType().equals("CI-Edited-Payment"))
				rollbackstatus = true;
			else
				rollbackstatus = false;
			
			if(rollbackstatus)
			{
			 String changedByName = "";
			 if(newCuinvoice.getChangedById() != null){
					changedByName = ((TsUserLogin) aSession.get(TsUserLogin.class,newCuinvoice.getChangedById()==null?0:newCuinvoice.getChangedById())).getFullName();
				}
			 
			 	aRollbacklog.setCuInvoiceId(aTpCuinvoiceLogMaster.getCuInvoiceId());
				aRollbacklog.setCreatedById(aTpCuinvoiceLogMaster.getCreatedById());
				aRollbacklog.setCreatedByName(aTpCuinvoiceLogMaster.getCreatedByName());
				aRollbacklog.setCreatedOn(aTpCuinvoiceLogMaster.getCreatedOn());
				aRollbacklog.setChangedById(newCuinvoice.getChangedById());
				aRollbacklog.setChangedOn(newCuinvoice.getChangedOn());
				aRollbacklog.setChangedByName(changedByName);
				aRollbacklog.setJoReleaseDetailId(aTpCuinvoiceLogMaster.getJoReleaseDetailId());
				aRollbacklog.setCuSoid(aTpCuinvoiceLogMaster.getCuSoid());
				aRollbacklog.setRxCustomerId(aTpCuinvoiceLogMaster.getRxCustomerId());
				aRollbacklog.setTransType(aTpCuinvoiceLogMaster.getTransType());
				aRollbacklog.setPrFromWarehouseId(aTpCuinvoiceLogMaster.getPrFromWarehouseId());
				aRollbacklog.setPrToWarehouseId(aTpCuinvoiceLogMaster.getPrToWarehouseId());
				aRollbacklog.setRxShipToId(aTpCuinvoiceLogMaster.getRxShipToId());
				aRollbacklog.setRxShipToAddressId(aTpCuinvoiceLogMaster.getRxShipToAddressId());
				aRollbacklog.setShipToMode(aTpCuinvoiceLogMaster.getShipToMode());
				aRollbacklog.setCuTermsId(aTpCuinvoiceLogMaster.getCuTermsId());
				aRollbacklog.setCoTaxTerritoryId(aTpCuinvoiceLogMaster.getCoTaxTerritoryId());
				aRollbacklog.setInvoiceNumber(aTpCuinvoiceLogMaster.getInvoiceNumber());
				aRollbacklog.setCustomerPonumber(aTpCuinvoiceLogMaster.getCustomerPonumber());
				aRollbacklog.setInvoiceDate(aTpCuinvoiceLogMaster.getInvoiceDate());
				aRollbacklog.setShipDate(aTpCuinvoiceLogMaster.getShipDate());
				aRollbacklog.setDueDate(aTpCuinvoiceLogMaster.getDueDate());
				aRollbacklog.setPrintDate(aTpCuinvoiceLogMaster.getPrintDate());
				aRollbacklog.setCuAssignmentId0(aTpCuinvoiceLogMaster.getCuAssignmentId0());
				aRollbacklog.setCuAssignmentId1(aTpCuinvoiceLogMaster.getCuAssignmentId1());
				aRollbacklog.setCreditmemo(aTpCuinvoiceLogMaster.getCreditmemo());
				aRollbacklog.setMemoStatus(aTpCuinvoiceLogMaster.getMemoStatus());
				aRollbacklog.setIscredit(aTpCuinvoiceLogMaster.getIscredit());
				aRollbacklog.setReason(aTpCuinvoiceLogMaster.getReason());
				
				aRollbacklog.setSoNumber(aTpCuinvoiceLogMaster.getSoNumber());
				aRollbacklog.setPONumber(aTpCuinvoiceLogMaster.getPONumber());
				aRollbacklog.setJoReleaseID(aTpCuinvoiceLogMaster.getJoReleaseID());
				aRollbacklog.setJobName(aTpCuinvoiceLogMaster.getJobName());
				aRollbacklog.setJobNumber(aTpCuinvoiceLogMaster.getJobNumber());
				aRollbacklog.setCustomerName(aTpCuinvoiceLogMaster.getCustomerName());
				aRollbacklog.setSalesPerson(aTpCuinvoiceLogMaster.getSalesPerson());
				
				aRollbacklog.setInvoiceAmount(aTpCuinvoiceLogMaster.getInvoiceAmount().negate());
				aRollbacklog.setLastInvoiceAmount(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastInvoiceAmount());
				aRollbacklog.setAppliedAmount(aTpCuinvoiceLogMaster.getAppliedAmount().negate());
				aRollbacklog.setLastAppliedAmount(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastAppliedAmount());
				aRollbacklog.setDiscountAmt(aTpCuinvoiceLogMaster.getDiscountAmt().negate());
				aRollbacklog.setLastDiscountAmt(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastDiscountAmt());
				aRollbacklog.setTaxAmount(aTpCuinvoiceLogMaster.getTaxAmount().negate());
				aRollbacklog.setLastTaxAmount(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastTaxAmount());
				aRollbacklog.setFreight(aTpCuinvoiceLogMaster.getFreight().negate());
				aRollbacklog.setLastFreight(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastFreight());
				aRollbacklog.setCostTotal((aTpCuinvoiceLogMaster.getCostTotal()==null?BigDecimal.ZERO:aTpCuinvoiceLogMaster.getCostTotal()).negate());
				aRollbacklog.setLastCostTotal(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastCostTotal());
				aRollbacklog.setSubtotal(aTpCuinvoiceLogMaster.getSubtotal().negate());
				aRollbacklog.setLastSubtotal(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastSubtotal());
				aRollbacklog.setTaxRate(aTpCuinvoiceLogMaster.getTaxRate());
				aRollbacklog.setLastTaxRate(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastTaxRate());
				aRollbacklog.setAddCost(aTpCuinvoiceLogMaster.getAddCost().negate());
				aRollbacklog.setLastAddCost(BigDecimal.ZERO);//aTpCuinvoiceLogMaster.getLastAddCost());
				aRollbacklog.setSalesPersionId(aTpCuinvoiceLogMaster.getCuAssignmentId0());
				
				aRollbacklog.setState(aTpCuinvoiceLogMaster.getState());
				aRollbacklog.setCounty(aTpCuinvoiceLogMaster.getCounty());
				aRollbacklog.setCountyCode(aTpCuinvoiceLogMaster.getCountyCode());
				aRollbacklog.setTaxDistribution1(aTpCuinvoiceLogMaster.getTaxDistribution1());
				aRollbacklog.setTaxDistribution2(aTpCuinvoiceLogMaster.getTaxDistribution2());
				aRollbacklog.setTaxDistribution3(aTpCuinvoiceLogMaster.getTaxDistribution3());
				aRollbacklog.setTaxDistribution4(aTpCuinvoiceLogMaster.getTaxDistribution4());
				aRollbacklog.setTaxDistribution5(aTpCuinvoiceLogMaster.getTaxDistribution5());
				aRollbacklog.setIsTaxShipping(aTpCuinvoiceLogMaster.getIsTaxShipping());
				
				aRollbacklog.setTaxableSales(aTpCuinvoiceLogMaster.getTaxableSales().negate());
				aRollbacklog.setNonTaxableSales(aTpCuinvoiceLogMaster.getNonTaxableSales().negate());
				
				aRollbacklog.setStateTax(aTpCuinvoiceLogMaster.getStateTax().negate());
				aRollbacklog.setCityTax(aTpCuinvoiceLogMaster.getCityTax().negate());
				aRollbacklog.setCountyTax(aTpCuinvoiceLogMaster.getCountyTax().negate());
				aRollbacklog.setMTATax(aTpCuinvoiceLogMaster.getMTATax().negate());
				aRollbacklog.setCCDTax(aTpCuinvoiceLogMaster.getCCDTax().negate());
				aRollbacklog.setOtherTax(aTpCuinvoiceLogMaster.getOtherTax().negate());
				aRollbacklog.setTransStatus(1);
				aRollbacklog.setLastcoTaxTerritoryId(aTpCuinvoiceLogMaster.getLastcoTaxTerritoryId());
				aRollbacklog.setFperiod(aTpCuinvoiceLogMaster.getFperiod());
				aRollbacklog.setFyear(aTpCuinvoiceLogMaster.getFyear());
				
				aSession.save(aRollbacklog);
				aTransaction.commit();
			}
			}
			
			System.out.println(aTpCuinvoiceLogMaster.getTaxAmount() +"===="+ aTpCuinvoiceLogMaster.getLastTaxAmount());
			
			return rollbackstatus;
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	
	@Override
	public Integer deleteShipViaDetails(Veshipvia theVeshipvia,
			JoReleaseDetail theJoReleaseDetail, Vebill theVebill,
			Cuinvoice theCuinvoice) throws JobException {
		Session aVeShipViaSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		JoReleaseDetail aJoReleaseDetail = null;
		List<Cuinvoicedetail> aCuinvoicedetails;
		Iterator<Cuinvoicedetail> iterator = null;
		int i = 0;
		try {
			aTransaction = aVeShipViaSession.beginTransaction();
			aTransaction.begin();
			aJoReleaseDetail = (JoReleaseDetail) aVeShipViaSession.get(
					JoReleaseDetail.class,
					theJoReleaseDetail.getJoReleaseDetailId());
			aJoReleaseDetail.setCustomerInvoiceDate(theJoReleaseDetail
					.getCustomerInvoiceDate());
			aJoReleaseDetail.setCustomerInvoiceAmount(theJoReleaseDetail
					.getCustomerInvoiceAmount());
			aJoReleaseDetail.setVendorInvoiceDate(theJoReleaseDetail
					.getVendorInvoiceDate());
			aJoReleaseDetail.setVendorInvoiceAmount(theJoReleaseDetail
					.getVendorInvoiceAmount());
			aJoReleaseDetail.setJoReleaseDetailId(theJoReleaseDetail
					.getJoReleaseDetailId());
			aVeShipViaSession.update(aJoReleaseDetail);
			aTransaction = aVeShipViaSession.beginTransaction();
			aTransaction.commit();
			aTransaction.begin();
			aVeShipViaSession.delete(theCuinvoice);
			aTransaction.commit();
			aTransaction = aVeShipViaSession.beginTransaction();
			aTransaction.begin();
			aVeShipViaSession.delete(theVebill);
			aTransaction.commit();
			deletefromvebilldetail(aJoReleaseDetail, theVebill);
			aTransaction = aVeShipViaSession.beginTransaction();
			aTransaction.begin();
			aVeShipViaSession.delete(theVeshipvia);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aVeShipViaSession.flush();
			aVeShipViaSession.close();
		}
		return 0;
	}

	public boolean deletefromvebilldetail(JoReleaseDetail aJoReleaseDetail,
			Vebill theVebill) throws JobException {
		Session aSession = null;
		List<Coaccount> aQueryList = null;
		Vebill avebill = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.createSQLQuery(
					"DELETE FROM veBillDetail WHERE veBillID="
							+ theVebill.getVeBillId()).executeUpdate();
			// itsVendorService.updateJoReleaseDetail(aJoReleaseDetail);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public List<Coaccount> getCoAccountDetails() throws JobException {
		Session aSession = null;
		List<Coaccount> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession
					.createQuery("FROM  Coaccount ORDER BY Description ASC");
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}

	@Override
	public Cuinvoice addCusotmerInvoiceDetails(Cuinvoice theCuinvoice,
			JoReleaseDetail theJoReleaseDetail, String from)
			throws JobException {
		Session aCuInvoiceSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Cuinvoice aCuinvoice = null;
		JoReleaseDetail aJoReleaseDetail = null;
		try {

			if (from != null) {
				aTransaction = aCuInvoiceSession.beginTransaction();
				aTransaction.begin();
				if (theCuinvoice.getJoReleaseDetailId() != null
						&& theCuinvoice.getJoReleaseDetailId() != 0) {
					aJoReleaseDetail = (JoReleaseDetail) aCuInvoiceSession.get(
							JoReleaseDetail.class,
							theCuinvoice.getJoReleaseDetailId());
				} else {
					aJoReleaseDetail = new JoReleaseDetail();
				}
				aJoReleaseDetail.setCustomerInvoiceDate(theCuinvoice
						.getDueDate());
				aJoReleaseDetail.setCustomerInvoiceAmount(theJoReleaseDetail
						.getCustomerInvoiceAmount());
				/*
				 * aJoReleaseDetail.setJoReleaseDetailId(aCuinvoice
				 * .getJoReleaseDetailId());
				 */
				aJoReleaseDetail.setShipDate(theCuinvoice.getShipDate());
				aJoReleaseDetail.setJoReleaseId(theJoReleaseDetail
						.getJoReleaseId());

				// JoRelease
				JoRelease aJoRelease = (JoRelease) aCuInvoiceSession.get(
						JoRelease.class, theJoReleaseDetail.getJoReleaseId());

				aJoReleaseDetail.setJoMasterId(aJoRelease.getJoMasterId());
				if (aJoReleaseDetail.getJoReleaseDetailId() != null) {
					theCuinvoice.setJoReleaseDetailId(aJoReleaseDetail
							.getJoReleaseDetailId());

					aCuInvoiceSession.update(aJoReleaseDetail);
					updateReleaseAllocated(aJoReleaseDetail
							.getJoReleaseDetailId());
				} else {
					Integer joReleaseDetailID = (Integer) aCuInvoiceSession
							.save(aJoReleaseDetail);
					theCuinvoice.setJoReleaseDetailId(joReleaseDetailID);
					updateReleaseAllocated(aJoReleaseDetail.getJoReleaseId());
				}

				aTransaction.commit();
			}
			aTransaction = aCuInvoiceSession.beginTransaction();
			aTransaction.begin();

			/*
			 * if (null == theCuinvoice.getInvoiceNumber() || "" ==
			 * theCuinvoice.getInvoiceNumber()) { String sQuery =
			 * "SELECT InvoiceNumber FROM cuInvoice WHERE InvoiceNumber REGEXP '^[0-9]+$' ORDER BY cuInvoiceID DESC LIMIT 1"
			 * ; sInvoiceNumber = (String) aInvoiceNumberSession.createSQLQuery(
			 * sQuery).uniqueResult();
			 * theCuinvoice.setInvoiceNumber(String.valueOf(Integer
			 * .parseInt(InvNo))); }
			 */

			if (null == theCuinvoice.getInvoiceNumber()
					|| "" == theCuinvoice.getInvoiceNumber()) {
					itsLogger.info("InvoiceNumber::"+theCuinvoice.getInvoiceNumber());
				/* Commented for ID#465
				 * String InvNo = itsSysService.getSysSequenceNumber("cuInvoice").toString();*/
				String	newInvoiceNo = itsInventoryService.updateSysVariableSequence(InventoryConstant.getConstantSysvariableId("RequireNewNumbersForCuInvoices"));
				theCuinvoice.setInvoiceNumber(newInvoiceNo);
			} else {
				if (theCuinvoice.getJoReleaseDetailId() == null
						|| theCuinvoice.getJoReleaseDetailId() == 0) {
					/* Commented for ID#465
					 * String InvNo = itsSysService.getSysSequenceNumber("cuInvoice").toString();*/
					String	newInvoiceNo = itsInventoryService.updateSysVariableSequence(InventoryConstant.getConstantSysvariableId("RequireNewNumbersForCuInvoices"));
					theCuinvoice.setInvoiceNumber(newInvoiceNo);
				}else{
					String newInvoiceNumber = "";
					Integer requireInvoiceNumberOrNot = 0;
					List<String> addlist=new ArrayList<String>();
					addlist.add("RequireNewNumbersForCuInvoices");
					ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
					requireInvoiceNumberOrNot = sysvariablelist.get(0).getValueLong()==null?0:sysvariablelist.get(0).getValueLong();
					if(requireInvoiceNumberOrNot>0){
						String	newInvoiceNo = itsInventoryService.updateSysVariableSequence(InventoryConstant.getConstantSysvariableId("RequireNewNumbersForCuInvoices"));
						itsLogger.info("New InvoiceNumber Updated #"+newInvoiceNo);
					}
				}

			}

			//theCuinvoice.setcIopenStatus(true);

			Integer aCuInVoiceID = (Integer) aCuInvoiceSession
					.save(theCuinvoice);
			aTransaction.commit();
			aCuinvoice = (Cuinvoice) aCuInvoiceSession.get(Cuinvoice.class,
					aCuInVoiceID);

			
			/*Commented by Jenith for update transaction status by asking the user to set the status by UI Level*/
			/*if (aCuinvoice.getCuSoid() != null && aCuinvoice.getCuSoid() != 0) {
				aTransaction = aCusoSession.beginTransaction();
				aTransaction.begin();
				aCuso = (Cuso) aCusoSession.get(Cuso.class,
						aCuinvoice.getCuSoid());
				if (aCuso != null) {
					aCuso.setTransactionStatus(2);
					aCusoSession.update(aCuso);
					aTransaction.commit();
				}
			}*/

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
		}
		return aCuinvoice;
	}

	@Override
	public Vepo updateBillToAndShipToSetting(Vepo theVepo)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aVePOTransaction;
		Vepo aVepo = null;
		try {
			
			if(theVepo.getVePoid()!=null)
			{
			aVePOTransaction = aVePOSession.beginTransaction();
			aVepo = (Vepo) aVePOSession.get(Vepo.class, theVepo.getVePoid());
			if (theVepo.getUpdateKey().equalsIgnoreCase("billTo")) {
				aVepo.setBillToIndex(theVepo.getBillToIndex());
			}
			if (theVepo.getUpdateKey().equalsIgnoreCase("shipTo")) {
				aVepo.setShipTo(theVepo.getShipTo());
				aVepo.setShipToMode(theVepo.getShipToMode());
				aVepo.setRxShipToId(theVepo.getRxShipToId());
				aVepo.setRxShipToAddressId(theVepo.getRxShipToAddressId());
			}
			if (theVepo.getUpdateKey().equalsIgnoreCase("shipToUSID")) {
				aVepo.setShipToMode(theVepo.getShipToMode());
				if(theVepo.getShipTo()==0)
				{
				aVepo.setShipTo(theVepo.getShipTo());	
				}
				else
				{
				aVepo.setShipTo(aVepo.getShipTo()+theVepo.getShipTo());
				}
			}
			
			if (theVepo.getUpdateKey().equalsIgnoreCase("shipToCustomer")) {
				aVepo.setShipToMode(theVepo.getShipToMode());
				
				if(theVepo.getShipTo()==0)
				{
				aVepo.setShipTo(theVepo.getShipTo());	
				}
				else
				{
				aVepo.setShipTo(aVepo.getShipTo()+theVepo.getShipTo());
				}
			}
			if (theVepo.getUpdateKey().equalsIgnoreCase("shipToOther")) {
				aVepo.setShipToMode(theVepo.getShipToMode());
				//aVepo.setShipTo(aVepo.getShipTo());
			}
			
			aVepo.setVePoid(theVepo.getVePoid());
			aVePOSession.update(aVepo);
			aVePOTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return aVepo;
	}

	@Override
	public Boolean saveCustomerPONumber(String theCustomerPONumber,
			Integer theJoMasterID, Integer theReleaseDetail)
			throws JobException {
		Session aCuInvoiceSession = itsSessionFactory.openSession();
		Transaction aCuInvoiceTransaction;
		Cuinvoice aCuinvoice = null;
		try {
			Integer aCuInvoiceID = getCustomerInoiveID(theReleaseDetail);
			aCuInvoiceTransaction = aCuInvoiceSession.beginTransaction();
			aCuinvoice = (Cuinvoice) aCuInvoiceSession.get(Cuinvoice.class,
					aCuInvoiceID);
			aCuinvoice.setCustomerPonumber(theCustomerPONumber);
			aCuInvoiceSession.update(aCuinvoice);
			aCuInvoiceTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
		}
		return false;
	}

	@Override
	public Boolean savePOLineitemsSubtotal(Integer vePOID,
			BigDecimal thePOLISubtotal) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aVePOTransaction;
		Vepo aVepo = null;
		try {
			aVePOTransaction = aVePOSession.beginTransaction();
			aVepo = (Vepo) aVePOSession.get(Vepo.class, vePOID);
			aVepo.setSubtotal(thePOLISubtotal);
			aVePOSession.update(aVepo);
			aVePOTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			throw new JobException(excep.getMessage(), excep);
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return true;
	}

	@Override
	public Boolean addCustomerInvoice(Cuinvoice theCuinvoice)
			throws JobException {
		Session aCuInvoiceSession = itsSessionFactory.openSession();
		Transaction aCuInvoiceTransaction;
		try {
			aCuInvoiceTransaction = aCuInvoiceSession.beginTransaction();
			aCuInvoiceTransaction.begin();
			aCuInvoiceSession.save(theCuinvoice);
			aCuInvoiceTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
		}
		return false;
	}

	@Override
	public Vepo updateEmailStampValue(Vepo theVepo) throws JobException {
		Session aVepoSession = itsSessionFactory.openSession();
		Vepo aVepo = new Vepo();
		Transaction aTransaction;
		try {
			
			aVepo = (Vepo) aVepoSession.get(Vepo.class, theVepo.getVePoid());
			long time = new Date().getTime();
		
			if(aVepo!=null)
			{
			aTransaction = aVepoSession.beginTransaction();
			aVepo = (Vepo) aVepoSession.get(Vepo.class, theVepo.getVePoid());
			if(theVepo.getEmailTimeStamp()!=null)
			aVepo.setEmailTimeStamp(theVepo.getEmailTimeStamp());
			else
			aVepo.setEmailTimeStamp(new Date());
			
			aVepo.setVePoid(theVepo.getVePoid());
			aVepoSession.update(aVepo);
			aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aVepoSession.flush();
			aVepoSession.close();
		}
		return aVepo;
	}

	@Override
	public Cuso updateSOEmailTimeStamp(Cuso theCuso) throws JobException {
		Session aVepoSession = itsSessionFactory.openSession();
		Cuso aCuso = new Cuso();
		Transaction aTransaction;
		try {
			aTransaction = aVepoSession.beginTransaction();
			aCuso = (Cuso) aVepoSession.get(Cuso.class, theCuso.getCuSoid());
			aCuso.setEmailTimeStamp(new Date());
			aCuso.setCuSoid(theCuso.getCuSoid());
			aVepoSession.update(aCuso);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aVepoSession.flush();
			aVepoSession.close();
		}
		return aCuso;
	}

	@Override
	public boolean updatePOLineInfo(Vepodetail theVepodetail)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Vepodetail aLineIfo = (Vepodetail) aSession.get(Vepodetail.class,
					theVepodetail.getVePodetailId());
			aLineIfo.setNote(theVepodetail.getNote());
			aLineIfo.setVePodetailId(theVepodetail.getVePodetailId());
			aSession.update(aLineIfo);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}

	@Override
	public Jochange saveChangeOrderDetails(Jochange theJochange)
			throws JobException {
		Session aJochangeSession = itsSessionFactory.openSession();
		Transaction aJoChangeTransaction;
		Jochange aJochange = null;
		try {
			if (theJochange.getOper().equalsIgnoreCase("add")) {
				aJoChangeTransaction = aJochangeSession.beginTransaction();
				aJoChangeTransaction.begin();
				aJochangeSession.save(theJochange);
				aJoChangeTransaction.commit();
				aJochange = (Jochange) aJochangeSession.get(Jochange.class,
						theJochange.getJoChangeId());
			} else if (theJochange.getOper().equalsIgnoreCase("edit")) {
				aJoChangeTransaction = aJochangeSession.beginTransaction();
				aJoChangeTransaction.begin();
				aJochange = (Jochange) aJochangeSession.get(Jochange.class,
						theJochange.getJoChangeId());
				aJochange.setChangeDate(theJochange.getChangeDate());
				aJochange
						.setCustomerPonumber(theJochange.getCustomerPonumber());
				aJochange.setChangeByName(theJochange.getChangeByName());
				aJochange.setChangeReason(theJochange.getChangeReason());
				aJochange.setChangeAmount(theJochange.getChangeAmount());
				aJochange.setChangeCost(theJochange.getChangeCost());
				aJochange.setJoMasterId(theJochange.getJoMasterId());
				aJochange.setChangeById(theJochange.getChangeById());
				aJochange.setJoChangeId(theJochange.getJoChangeId());
				aJochangeSession.update(aJochange);
				aJoChangeTransaction.commit();
			} else if (theJochange.getOper().equalsIgnoreCase("del")) {
				aJoChangeTransaction = aJochangeSession.beginTransaction();
				aJoChangeTransaction.begin();
				aJochangeSession.delete(theJochange);
				aJoChangeTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aJochangeSession.flush();
			aJochangeSession.close();
		}
		return aJochange;
	}

	@Override
	public JoCustPO saveCustomerPONumner(JoCustPO theJoCustPO,
			Jomaster theJomaster) throws JobException {
		Session aJoCustPOSession = itsSessionFactory.openSession();
		Transaction aJoCustPOTransaction;
		JoCustPO aJoCustPO = null;
		Jomaster aJomaster = null;
		try {
			if (theJoCustPO.getOper().equalsIgnoreCase("add")) {
				aJoCustPOTransaction = aJoCustPOSession.beginTransaction();
				aJoCustPOTransaction.begin();
				aJoCustPOSession.save(theJoCustPO);
				aJoCustPOTransaction.commit();
				aJoCustPO = (JoCustPO) aJoCustPOSession.get(JoCustPO.class,
						theJoCustPO.getJoCustPoid());
				aJoCustPO.setOper("add");
				aJoCustPOTransaction = aJoCustPOSession.beginTransaction();
				aJoCustPOTransaction.begin();
				aJomaster = (Jomaster) aJoCustPOSession.get(Jomaster.class,
						theJomaster.getJoMasterId());
				aJomaster
						.setCustomerPonumber(theJomaster.getCustomerPonumber());
				aJomaster.setJoMasterId(theJomaster.getJoMasterId());
				aJoCustPOSession.update(aJomaster);
				aJoCustPOTransaction.commit();
			} else if (theJoCustPO.getOper().equalsIgnoreCase("edit")) {
				aJoCustPOTransaction = aJoCustPOSession.beginTransaction();
				aJoCustPOTransaction.begin();
				aJoCustPO = (JoCustPO) aJoCustPOSession.get(JoCustPO.class,
						theJoCustPO.getJoCustPoid());
				aJoCustPO.setPodesc0(theJoCustPO.getPodesc0());
				aJoCustPO.setPodesc1(theJoCustPO.getPodesc1());
				aJoCustPO.setPodesc2(theJoCustPO.getPodesc2());
				aJoCustPO.setPodesc3(theJoCustPO.getPodesc3());
				aJoCustPO.setPodesc4(theJoCustPO.getPodesc4());
				aJoCustPO.setPodesc5(theJoCustPO.getPodesc5());
				aJoCustPO.setCustomerPonumber1(theJoCustPO
						.getCustomerPonumber1());
				aJoCustPO.setCustomerPonumber2(theJoCustPO
						.getCustomerPonumber2());
				aJoCustPO.setCustomerPonumber3(theJoCustPO
						.getCustomerPonumber3());
				aJoCustPO.setCustomerPonumber4(theJoCustPO
						.getCustomerPonumber4());
				aJoCustPO.setCustomerPonumber5(theJoCustPO
						.getCustomerPonumber5());
				aJoCustPO.setPoamount0(theJoCustPO.getPoamount0());
				aJoCustPO.setPoamount1(theJoCustPO.getPoamount1());
				aJoCustPO.setPoamount2(theJoCustPO.getPoamount2());
				aJoCustPO.setPoamount3(theJoCustPO.getPoamount3());
				aJoCustPO.setPoamount4(theJoCustPO.getPoamount4());
				aJoCustPO.setPoamount5(theJoCustPO.getPoamount5());
				aJoCustPO.setJoMasterId(theJoCustPO.getJoMasterId());
				aJoCustPO.setJoCustPoid(theJoCustPO.getJoCustPoid());
				aJoCustPOSession.update(aJoCustPO);
				aJoCustPOTransaction.commit();
				aJoCustPO.setOper("edit");
				aJoCustPOTransaction = aJoCustPOSession.beginTransaction();
				aJoCustPOTransaction.begin();
				aJomaster = (Jomaster) aJoCustPOSession.get(Jomaster.class,
						theJomaster.getJoMasterId());
				aJomaster
						.setCustomerPonumber(theJomaster.getCustomerPonumber());
				aJomaster.setJoMasterId(theJomaster.getJoMasterId());
				aJoCustPOSession.update(aJomaster);
				aJoCustPOTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aJoCustPOSession.flush();
			aJoCustPOSession.close();
		}
		return aJoCustPO;
	}

	@Override
	public JoCustPO getSingleCusotomerPODetails(Integer theJoMasterId)
			throws JobException {
		Session aSession = null;
		JoCustPO aCustPO = new JoCustPO();
		Integer aJoCustPOID = getJoCustPOID(theJoMasterId);
		try {
			aSession = itsSessionFactory.openSession();
			if (aJoCustPOID != null) {
				aCustPO = (JoCustPO) aSession.get(JoCustPO.class, aJoCustPOID);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCustPO;
	}

	@Override
	public Integer getJoCustPOID(Integer theJoMasterId) throws JobException {
		Integer aCustPOId = null;
		String aSelectQry = "SELECT joCustPOID FROM joCustPO WHERE joMasterID = '"
				+ theJoMasterId + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty())
				aCustPOId = (Integer) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQry=null;
		}
		return aCustPOId;
	}

	@Override
	public List<JoCustPO> getJoCustPODetail(Integer theJoMasterID)
			throws JobException {
		Session aSession = null;
		List<JoCustPO> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			itsLogger.info("getJoCustPODetail()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
			Query aQuery = aSession
					.createQuery("FROM  JoCustPO WHERE JoMasterID = "
							+ theJoMasterID + "");
			// Retrieve all
			aQueryList = aQuery.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Ship Via in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getJoCustPODetail()=[Connection Closed]");
		}
		return aQueryList;
	}

	@Override
	public Vepodetail getVePODetails(Integer thePOID) throws JobException {
		Session aSession = null;
		Vepodetail aVepodetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			aVepodetail = new Vepodetail();
			aVepodetail = (Vepodetail) aSession.get(Vepodetail.class, thePOID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVepodetail;
	}

	@Override
	public Integer getCuSOID(String theJoReleaseId) throws JobException {
		Session aSession = null;
		int CuSOID = 0;
		String acusoIdQuery = null;
		Query aQuery = null;
		List<?> aList = null;
		try {
			aSession = itsSessionFactory.openSession();
			acusoIdQuery = "SELECT cuSO.cuSOID FROM cuSO WHERE joReleaseID='"
					+ theJoReleaseId + "';";
			aQuery = aSession.createSQLQuery(acusoIdQuery);
			 aList = aQuery.list();
			if (!aList.isEmpty())
				CuSOID = (Integer) aList.get(0);
			acusoIdQuery=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			excep.printStackTrace();
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			acusoIdQuery = null;
			aQuery = null;
			aList = null;
		}
		return CuSOID;
	}
	
	@Override
	public Integer getPrToWarehouseID(Integer cuSOId) throws JobException {
		Session aSession = null;
		int CuSOID = 0;
		String acusoIdQuery = null;
		Query aQuery = null;
		List<?> aList = null;
		try {
			itsLogger.info("prToWarehouseID");
			aSession = itsSessionFactory.openSession();
			acusoIdQuery = "SELECT cuSO.prToWarehouseID FROM cuSO WHERE cuSOID="+ cuSOId;
			aQuery = aSession.createSQLQuery(acusoIdQuery);
			aList = aQuery.list();
			if (!aList.isEmpty())
				CuSOID = (Integer) (aList.get(0)==null?0:aList.get(0));
			acusoIdQuery=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			acusoIdQuery = null;
			aQuery = null;
			aList = null;
		}
		return CuSOID;
	}

	@Override
	public Cusodetail getCusoDetailObj(int CusoID) throws JobException {
		Session aSession = null;
		Cusodetail aCusodetail = null;
		Cusodetail iteratorObj = null;
		BigDecimal sum = new BigDecimal(0);
		BigDecimal taxsum = new BigDecimal(0);
		int i = 0;
		String Query = null;
		Query aQuery = null;
		List<?> aList = null;
		try {
			Query = "FROM Cusodetail WHERE cuSoid= :cuSoid";
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery(Query);
			aQuery.setParameter("cuSoid", CusoID);
			aList = aQuery.list();
			if (!aList.isEmpty()) {
				/*Cuso aCuso = new Cuso();
				aCuso = (Cuso) aSession.get(Cuso.class, CusoID);*/
				aCusodetail = (Cusodetail) aList.get(0);
				for (i = 0; i < aList.size(); i++) {
					iteratorObj = (Cusodetail) aList.get(i);
					
					iteratorObj.setQuantityBilled(iteratorObj.getQuantityBilled()==null?BigDecimal.ZERO:iteratorObj.getQuantityBilled());
					if((iteratorObj.getQuantityOrdered().subtract(iteratorObj.getQuantityBilled())).compareTo(BigDecimal.ZERO)>0){
					BigDecimal total = (iteratorObj.getQuantityOrdered()==null?new BigDecimal("0.0000"):iteratorObj.getQuantityOrdered().subtract(iteratorObj.getQuantityBilled()))
							.multiply((iteratorObj.getPriceMultiplier()==null || (iteratorObj.getPriceMultiplier().compareTo(new BigDecimal("0.0000"))==0) ?new BigDecimal("1.0000"):iteratorObj.getPriceMultiplier())
									.multiply(iteratorObj.getUnitCost()==null?new BigDecimal("0.0000"):iteratorObj.getUnitCost()));
					sum = sum.add(JobUtil.floorFigureoverall(total,2));
					
					taxsum=taxsum.add((iteratorObj.getTaxable()==1)?JobUtil.floorFigureoverall(total,2):BigDecimal.ZERO);
					}
				}
				
				
				/*if(aCuso.getTaxRate() != null && aCuso.getTaxRate().compareTo(new BigDecimal("0.0000"))==1){
					taxRate = aCuso.getTaxRate();
				}
				taxSum = (sum.multiply(taxRate)).divide(new BigDecimal("100.0000"));
				sum = sum.add(taxSum);*/
				aCusodetail.setTaxTotal(sum); // A transient field 'tax total'
				aCusodetail.setTaxableSum(taxsum);
				Query=null;							// to show in UI. Need to change
			}
			
			
			
			
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			iteratorObj = null;
			sum = null;
			Query = null;
			aQuery = null;
			aList = null;
		}
		return aCusodetail;
	}

	@Override
	public Vepodetail getVepoDetailOBJ(int vePoid) throws JobException {
		Session aSession = null;
		Vepodetail aVepodetail = null;
		Vepodetail iteratorObj = null;
		BigDecimal sum = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		
		BigDecimal quantBil = new BigDecimal(0);
		BigDecimal unit = new BigDecimal(0);
		BigDecimal price = new BigDecimal(0);
		BigDecimal quantOrd = new BigDecimal(0);
		String Query = null;
		Query aQuery = null;
		List<?> aList = null;
		int i = 0;
		try {
			Query = "FROM Vepodetail WHERE vePoid= :vePoid";
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery(Query);
			aQuery.setParameter("vePoid", vePoid);
			aList = aQuery.list();
			if (!aList.isEmpty()) {
				aVepodetail = (Vepodetail) aList.get(0);
				for (i = 0; i < aList.size(); i++) {
					iteratorObj = (Vepodetail) aList.get(i);
					
					quantBil = iteratorObj.getQuantityBilled()==null?new BigDecimal("0.0000"):iteratorObj.getQuantityBilled();
					 unit = iteratorObj.getPriceMultiplier()==null?new BigDecimal("0.0000"):iteratorObj.getPriceMultiplier();
					 price =iteratorObj.getUnitCost()==null?new BigDecimal("0.0000"):iteratorObj.getUnitCost();
					 quantOrd = iteratorObj.getQuantityOrdered()==null?new BigDecimal("0.0000"):iteratorObj.getQuantityOrdered();
					
					if (iteratorObj.getPriceMultiplier() != null)
						total  = quantBil.multiply(unit).multiply(price);
					else if (iteratorObj.getUnitCost() != null)
						total = quantOrd.multiply(unit);
					
					sum = sum.add(total);
				}
				aVepodetail.setTaxTotal(sum); // A transient field 'tax total'
												// to show in UI. Need to change
			}
			Query=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			iteratorObj = null;
			sum = null;
			total =  null;
			quantBil = null;
			unit = null;
			price = null;
			quantOrd = null;
			Query = null;
			aQuery = null;
			aList = null;
		}
		return aVepodetail;
	}

	@Override
	public Cuinvoicedetail getCuInvoiceDetailObj(int cuInvoiceID)
			throws JobException {
		Session aSession = null;
		Cuinvoicedetail aCuinvoicedetail = null;
		Cuinvoicedetail iteratorObj = null;
		BigDecimal sum = new BigDecimal(0);
		BigDecimal Multiplier = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		BigDecimal taxsum=BigDecimal.ZERO;
		int i = 0;
		try {
			String Query = "FROM Cuinvoicedetail WHERE cuInvoiceId= :cuInvoiceID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(Query);
			aQuery.setParameter("cuInvoiceID", cuInvoiceID);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				aCuinvoicedetail = (Cuinvoicedetail) aList.get(0);
				for (i = 0; i < aList.size(); i++) {
					iteratorObj = (Cuinvoicedetail) aList.get(i);
					
					Multiplier = iteratorObj.getPriceMultiplier();
					if (iteratorObj.getPriceMultiplier() != null
							&& iteratorObj.getQuantityBilled() != null
							&& iteratorObj.getUnitCost() != null) {
						if(iteratorObj.getPriceMultiplier().compareTo(new BigDecimal("0.0000"))==0){
							Multiplier=new BigDecimal("1.0000");
						}
						total = iteratorObj.getQuantityBilled().multiply(
								Multiplier.multiply(
										iteratorObj.getUnitCost()));
					} else if (iteratorObj.getUnitCost() != null) {
						total = iteratorObj.getQuantityBilled()==null?new BigDecimal("0.0000").multiply(iteratorObj.getUnitCost()):iteratorObj.getQuantityBilled().multiply(iteratorObj.getUnitCost());
					} else if (iteratorObj.getPriceMultiplier() != null) {
						if(iteratorObj.getPriceMultiplier().compareTo(new BigDecimal("0.0000"))==0){
							Multiplier=new BigDecimal("1.0000");
						}
						total = iteratorObj.getQuantityBilled()==null?new BigDecimal("0.0000").multiply(
								Multiplier):iteratorObj.getQuantityBilled().multiply(
										Multiplier);
					}
					
					taxsum=(iteratorObj.getTaxable()==1)?taxsum.add(total):taxsum.add(BigDecimal.ZERO);
					sum = sum.add(total);
				}
				aCuinvoicedetail.setTaxTotal(sum);
				aCuinvoicedetail.setTaxSum(taxsum);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuinvoicedetail;
	}

	public List<Cuinvoicedetail> getCuInvoiceDetailObjs(int cuInvoiceID)
			throws JobException {
		Session aSession = null;
		List<Cuinvoicedetail> cuinvoicedetails = null;
		try {
			String Query = "FROM Cuinvoicedetail WHERE cuInvoiceId= :cuInvoiceID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(Query);
			aQuery.setParameter("cuInvoiceID", cuInvoiceID);
			List aList = aQuery.list();
			if (!aList.isEmpty()) {
				cuinvoicedetails = aList;
			}
			Query=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return cuinvoicedetails;
	}

	@Override
	public Cuso getCusoObj(int CusoID) throws JobException {
		Session aSession = null;
		Cuso acuso = null;
		try {
			aSession = itsSessionFactory.openSession();
			acuso = (Cuso) aSession.get(Cuso.class, CusoID);
			
			itsLogger.info("TimeStamp: " + acuso.getEmailTimeStamp());
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return acuso;
	}

	@Override
	public Cuso updateSOGeneral(Cuso theCuso, Rxaddress theShipToOtherAddress)
			throws JobException {
		Session aCuSOSession = null;
		Cuso aCuso = null;
		Cuso afmdbCuso = null;
		Integer aRxAddressShipID = null;
		Integer iShipToID = null;
		Integer arxMasterID = null;
		String sQry = null;
		Query aQuery = null;
		Iterator<?> aIterator = null;
		Rxaddress theShipToAddress = null;
		Rxmaster aRxmaster =null;
		try {
			aCuSOSession = itsSessionFactory.openSession();
			Transaction cuSOTransaction = aCuSOSession.getTransaction();
			afmdbCuso =  (Cuso) aCuSOSession.get(Cuso.class,theCuso.getCuSoid());

			if (theCuso.getShipToMode() == 3) {
				aRxmaster = new Rxmaster();
				if(afmdbCuso.getRxShipToAddressId()!=null)
				{
					Rxaddress aradd=new Rxaddress();
					aradd = (Rxaddress) aCuSOSession.get(Rxaddress.class,afmdbCuso.getRxShipToAddressId());	
					aradd.setFax(theShipToOtherAddress.getFax());
					aradd.setAddress1(theShipToOtherAddress.getAddress1());
					aradd.setAddress2(theShipToOtherAddress.getAddress2());
					aradd.setCity(theShipToOtherAddress.getCity());
					aradd.setState(theShipToOtherAddress.getState());
					aradd.setIsBillTo(theShipToOtherAddress.getIsBillTo());
					aradd.setInActive(theShipToOtherAddress.getInActive());
					aradd.setIsMailing(theShipToOtherAddress.getIsMailing());
					aradd.setIsStreet(theShipToOtherAddress.getIsStreet());
					aradd.setIsShipTo(theShipToOtherAddress.getIsShipTo());
					aradd.setOtherShipTo(theShipToOtherAddress.getOtherShipTo());
					aradd.setZip(theShipToOtherAddress.getZip());
					aradd.setName(theShipToOtherAddress.getName());
					aCuSOSession.update(aradd);
					iShipToID=theCuso.getRxShipToAddressId();
					
				}
				else
				{
					if(theShipToOtherAddress.getName()!=null && !theShipToOtherAddress.getName().trim().equals("")){
						
					/*aRxmaster.setInActive(false);
					aRxmaster.setName(theShipToOtherAddress.getName());
					aRxmaster.setSearchName(theShipToOtherAddress.getName().substring(0,2).toUpperCase().trim());
					aRxmaster.setFirstName("");
					aRxmaster.setIsCustomer(true);
					arxMasterID = (Integer) aCuSOSession.save(aRxmaster);
					theShipToOtherAddress.setRxMasterId(arxMasterID);*/
					
					aRxAddressShipID = (Integer) aCuSOSession.save(theShipToOtherAddress);
					}
					iShipToID = aRxAddressShipID;
				}
			}
			else
			{
				iShipToID=afmdbCuso.getRxShipToAddressId();
			}
			
			if(iShipToID!=null)
			theCuso.setRxShipToAddressId(iShipToID);
			
			cuSOTransaction.begin();
			aCuso = (Cuso) aCuSOSession.get(Cuso.class, theCuso.getCuSoid());

			if (theCuso.getCuAssignmentId0() != null) {
				aCuso.setCuAssignmentId0(theCuso.getCuAssignmentId0());
			}
			if (theCuso.getCuAssignmentId1() != null) {
				aCuso.setCuAssignmentId1(theCuso.getCuAssignmentId1());
			}
			if (theCuso.getCuAssignmentId2() != null) {
				aCuso.setCuAssignmentId2(theCuso.getCuAssignmentId2());
			}
			if (theCuso.getCuAssignmentId3() != null) {
				aCuso.setCuAssignmentId3(theCuso.getCuAssignmentId3());
			}
			if (theCuso.getCuAssignmentId4() != null) {
				aCuso.setCuAssignmentId4(theCuso.getCuAssignmentId4());
			}
			if (theCuso.getPrFromWarehouseId() != null
					&& theCuso.getPrFromWarehouseId() != -1
					&& theCuso.getPrFromWarehouseId() != 0) {
				aCuso.setPrFromWarehouseId(theCuso.getPrFromWarehouseId());
			}
			
			if (theCuso.getPrToWarehouseId() != null
					&& theCuso.getPrToWarehouseId() != -1
					&& theCuso.getPrToWarehouseId() != 0) {
				aCuso.setPrToWarehouseId(theCuso.getPrToWarehouseId());
			}
			
			if (theCuso.getVeShipViaId() != null
					&& theCuso.getVeShipViaId() != -1
					&& theCuso.getVeShipViaId() != 0) {
				aCuso.setVeShipViaId(theCuso.getVeShipViaId());
			}
			if (theCuso.getCoTaxTerritoryId() != null
					&& theCuso.getCoTaxTerritoryId() != 0) {
				aCuso.setCoTaxTerritoryId(theCuso.getCoTaxTerritoryId());
			}
			aCuso.setCoDivisionID(theCuso.getCoDivisionID());
			aCuso.setDatePromised(theCuso.getDatePromised());
			aCuso.setCustomerPonumber(theCuso.getCustomerPonumber());
			aCuso.setTag(theCuso.getTag());
			aCuso.setFreight(theCuso.getFreight());
			aCuso.setCuTermsId(theCuso.getCuTermsId());
			if(theCuso.getChangedById()!=null)
			aCuso.setChangedById(theCuso.getChangedById());
			if(theCuso.getChangedOn()!=null)
			aCuso.setChangedOn(theCuso.getChangedOn());
			aCuso.setShipDate(theCuso.getShipDate());
			aCuso.setRxContactId(theCuso.getRxContactId());
			aCuso.setTrackingNumber(theCuso.getTrackingNumber());

			aCuso.setJoScheddDetailID(theCuso.getJoScheddDetailID());
			aCuso.setTaxRate(theCuso.getTaxRate());
			aCuso.setTaxTotal(theCuso.getTaxTotal());
			aCuso.setWithpriceStatus(theCuso.isWithpriceStatus()); //Added by leo ID#505
			if (theCuso.getRxCustomerId() != null)
				aCuso.setRxCustomerId(theCuso.getRxCustomerId());
			if (theCuso.getRxBillToId() != null)
				aCuso.setRxBillToId(theCuso.getRxBillToId());
			if (theCuso.getRxShipToId() != null)
				aCuso.setRxShipToId(theCuso.getRxShipToId());
			if (theCuso.getRxShipToAddressId() != null)
				aCuso.setRxShipToAddressId(theCuso.getRxShipToAddressId());
			if (theCuso.getShipToMode() >-1)
				aCuso.setShipToMode(theCuso.getShipToMode());
			aCuso.setShipToIndex(theCuso.getShipToIndex());
			
			/*
			 * if("Others".equalsIgnoreCase(theCustomerShipToOtherID)){
			 * theShipToOtherAddress.setFax(theCustomerShipToOtherID);
			 * theShipToOtherAddress.setAddress1(theShipToOtherAddress1);
			 * theShipToOtherAddress.setAddress2(theShipToOtherAddress2);
			 * theShipToOtherAddress.setCity(theShipToOtherCity);
			 * theShipToOtherAddress.setState(theShipToOtherState);
			 * theShipToOtherAddress.setIsBillTo(false);
			 * theShipToOtherAddress.setInActive(false);
			 * theShipToOtherAddress.setIsMailing(false);
			 * theShipToOtherAddress.setIsStreet(false);
			 * theShipToOtherAddress.setIsShipTo(false);
			 * theShipToOtherAddress.setOtherShipTo(3);
			 * theShipToOtherAddress.setZip(theShipToOtherZip);
			 * //aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
			 * theShipToOtherAddress.setName(theShipToOtherNameID);
			 * //acuSO.setRxShipToAddressId(theCustomerShipToAddressID);
			 * acuSO.setShipToMode((byte)2);
			 */

			String CusodetailQuery="FROM Cusodetail WHERE cuSOID ="+aCuso.getCuSoid();
			Query theQuery = aCuSOSession.createQuery(CusodetailQuery);
			ArrayList<Cusodetail> cusoDetailList = (ArrayList<Cusodetail>) theQuery.list();
			for(Cusodetail theCusodetail:cusoDetailList){
				RollBackPrMasterPrWareHouseInventory(aCuso.getCuSoid(),theCusodetail.getCuSodetailId());
			}
			aCuso.setCreatedOn(theCuso.getCreatedOn());
			aCuso.setTaxfreight(theCuso.getTaxfreight());
			aCuSOSession.update(aCuso);
			cuSOTransaction.commit();
			for(Cusodetail theCusodetail:cusoDetailList){
				insertPrMasterPrWareHouseInventory(aCuso.getCuSoid(),theCusodetail.getCuSodetailId());
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aCuSOSession.flush();
			aCuSOSession.close();
			sQry = null;
			aQuery = null;
			aIterator = null;
		}
		return aCuso;
	}

	/*@Override
	public boolean addSOReleaseLineItem(Cusodetail theCuSODetail, Cuso theCuso)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Cuso aCuso = new Cuso();
		BigDecimal checkTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		int compare = 0;
		Integer cusoDetailID = null;
		Map<String,BigDecimal> productCost =null;
		Cusodetail aCusodetail = new Cusodetail();
		try {
			aTransaction = aSession.beginTransaction();
			productCost = new HashMap<String,BigDecimal>();
			aTransaction.begin();
			cusoDetailID =(Integer) aSession.save(theCuSODetail);
			aTransaction.commit();
			aTransaction.begin();
			aCusodetail = (Cusodetail) aSession.get(Cusodetail.class, cusoDetailID);
			productCost = salesServices.getTemplatePriceDetails(aCusodetail.getCuSoid(), aCusodetail.getCuSodetailId(), aCusodetail.getCuSodetailId());
			aCusodetail.setWhseCost(productCost.get("product"));
			aSession.update(aCusodetail);
			aTransaction.commit();
			if(theCuso.getCuSoid()!=null){
			aTransaction.begin();
			aCuso = (Cuso) aSession.get(Cuso.class, theCuso.getCuSoid());
			if (aCuso.getTaxTotal() != null)
				Taxtotal = aCuso.getTaxTotal();
			compare = Taxtotal.compareTo(checkTax);
			if (Taxtotal != null && compare == 1) {  Add to existing values 
				aCuso.setTaxTotal(aCuso.getTaxTotal()==null?new BigDecimal("0.0000"):aCuso.getTaxTotal()
						.add(theCuso.getTaxTotal()==null?new BigDecimal("0.0000"):theCuso.getTaxTotal()));
			} else {  the new Entry to cuso 
				aCuso.setTaxTotal(theCuso.getTaxTotal());
			}
			aSession.update(aCuso);
			aTransaction.commit();
			}
			Prmaster thePrmaster =getPrMasterBasedOnId(theCuSODetail.getPrMasterId());
			 if(aCuso!=null&&aCuso.getJoReleaseId()!=null){
			if(thePrmaster.getIsInventory()==1){
				updatePrMasterForSO(theCuSODetail, new BigDecimal(0),
						theCuSODetail.getQuantityOrdered(), "add", null);
				updatePrWarehouseInventoryForSO(theCuSODetail, new BigDecimal(0),
						theCuSODetail.getQuantityOrdered(), "add", null);
			}
			}else{
				if(thePrmaster.getIsInventory()==1){
				updatePrMasterForSO(theCuSODetail, new BigDecimal(0),
						theCuSODetail.getQuantityOrdered(), "add", null);
				updatePrWarehouseInventoryForSO(theCuSODetail, new BigDecimal(0),
						theCuSODetail.getQuantityOrdered(), "add", null);
				}
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}*/

	@Override
	public boolean addcuinvoiceReleaseLineItem(Cuinvoicedetail theCuInvDetail,
			Cuinvoice thecuInv) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Cuinvoice aCuinvoice = new Cuinvoice();
		BigDecimal checkTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		Integer compare = 0;
		Integer cuInvoiceDetailID = 0;
		Boolean tplog=false;
		Boolean tpFromSo=false;
		try {
			 Integer userID=theCuInvDetail.getUserID();
			 String userName=theCuInvDetail.getUserName();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			cuInvoiceDetailID = (Integer) aSession.save(theCuInvDetail);
			aTransaction.commit();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
					thecuInv.getCuInvoiceId());
			if (aCuinvoice.getTaxTotal() != null)
				Taxtotal = aCuinvoice.getTaxTotal();
			compare = Taxtotal.compareTo(checkTax);
			if (compare == 1)
				Taxtotal = Taxtotal.add(thecuInv.getTaxTotal()==null?BigDecimal.ZERO:thecuInv.getTaxTotal());
			else
				Taxtotal = thecuInv.getTaxTotal();
			aCuinvoice.setTaxTotal(Taxtotal);
			
			aCuinvoice.setInvoiceAmount((aCuinvoice.getInvoiceAmount()==null?BigDecimal.ZERO:aCuinvoice.getInvoiceAmount()).add(thecuInv.getInvoiceAmount()==null?BigDecimal.ZERO:thecuInv.getInvoiceAmount()));
			aCuinvoice.setSubtotal((aCuinvoice.getSubtotal()==null?BigDecimal.ZERO:aCuinvoice.getSubtotal()).add(thecuInv.getSubtotal()==null?BigDecimal.ZERO:thecuInv.getSubtotal()));
			if(thecuInv.getReleaseType()!=null && (thecuInv.getReleaseType().equals("1") || thecuInv.getReleaseType().equals("4"))){
				aCuinvoice.setCostTotal(new BigDecimal("0.0000"));
				aCuinvoice.setWhseCostTotal(new BigDecimal("0.0000"));
			}else{
				aCuinvoice.setCostTotal(getTemplatePriceDetails(thecuInv.getCuInvoiceId()));
				aCuinvoice.setWhseCostTotal(getTemplatePriceDetails(thecuInv.getCuInvoiceId()));
			}
			aSession.update(aCuinvoice);
			aTransaction.commit();
			Integer cusoid=thecuInv.getCuSoid()==null?0:thecuInv.getCuSoid();
			if(aCuinvoice!=null){
				cusoid=aCuinvoice.getCuSoid();
			}
			Cuso aCuso= null;
			if(cusoid!=null){
					aCuso=(Cuso) aSession.get(Cuso.class,cusoid);
			}
			Prmaster thePrmaster =getPrMasterBasedOnId(theCuInvDetail.getPrMasterId());
			if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()>0){
			JoReleaseDetail jrd=(JoReleaseDetail) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
			JoRelease jor = null;
			if(jrd.getJoReleaseId() != null){
				jor=(JoRelease) aSession.get(JoRelease.class,jrd.getJoReleaseId());
			}
			if(aCuso!=null){
				 if(aCuso.getJoReleaseId()==null){
					if(thePrmaster.getIsInventory()==1){
					updatePrMasterCustomerinvoiceWithSo(thecuInv.getCuInvoiceId(),theCuInvDetail,"add");
					tplog=true;
					tpFromSo = true;
					}
				}else{
					//Inside job InventoryUpdate inside job
						if(thePrmaster.getIsInventory()==1){
							updatePrMasterCustomerinvoiceInsideJob(thecuInv.getCuInvoiceId(),theCuInvDetail,"add");
							tplog=true;
							tpFromSo = true;
						}
				}
				}else if(jor!=null && jor.getReleaseType()==3){
					if(thePrmaster.getIsInventory()==1){
						updatePrMasterCustomerinvoiceWithoutSo(thecuInv.getCuInvoiceId(),theCuInvDetail,"add");
						tplog=true;
						tpFromSo = false;
						}
				}
			}else{
				if(aCuso!=null){
					 if(aCuso.getJoReleaseId()==null){
						if(thePrmaster.getIsInventory()==1){
						updatePrMasterCustomerinvoiceWithSo(thecuInv.getCuInvoiceId(),theCuInvDetail,"add");
						tplog=true;
						tpFromSo = true;
						}
					}
				}else{
					itsLogger.info("CusoID#"+aCuso+" Without SO.");
					if(thePrmaster.getIsInventory()==1){
						updatePrMasterCustomerinvoiceWithoutSo(thecuInv.getCuInvoiceId(),theCuInvDetail,"add");
						tplog=true;
						tpFromSo = false;
						}
					}
			}
			
			if(cuInvoiceDetailID>0 && tplog){
				Cuinvoice theCuinvoice=(Cuinvoice) aSession.get(Cuinvoice.class, theCuInvDetail.getCuInvoiceId());
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(theCuInvDetail.getPrMasterId());
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+theCuInvDetail.getPrMasterId());
				aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(theCuinvoice.getPrFromWarehouseId());
				aTpInventoryLog.setTransType("CI");
				aTpInventoryLog.setTransDecription("CI Created");
				aTpInventoryLog.setTransID(theCuInvDetail.getCuInvoiceId());
				aTpInventoryLog.setTransDetailID(cuInvoiceDetailID);
				aTpInventoryLog.setProductOut(theCuInvDetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?theCuInvDetail.getQuantityBilled():new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(theCuInvDetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?theCuInvDetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(theCuinvoice.getCreatedById());
				aTpInventoryLog.setCreatedOn(new Date());
				itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 * */
					BigDecimal qo=(theCuInvDetail.getQuantityBilled());
					Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, theCuinvoice.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theCuinvoice.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),
							aPrmaster.getItemCode(),
							theCuinvoice.getPrFromWarehouseId() ,
							theprwarehouse.getSearchName(),
							aPrmaster.getInventoryOnHand(),
							theprwarehsinventory.getInventoryOnHand(),
							(tpFromSo==true &&theCuInvDetail.getCuSodetailId()!=null ?theCuInvDetail.getQuantityBilled().multiply(new BigDecimal(-1)):BigDecimal.ZERO),BigDecimal.ZERO,"CI",theCuinvoice.getCuInvoiceId(),
							cuInvoiceDetailID,theCuinvoice.getInvoiceNumber(),aCuso!=null?aCuso.getSonumber():"",
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
							"CI Created",userID,userName,new Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
			}
			
			//updatePrMasterForCuInvoice(null, theCuInvDetail, "add");
			//updatePrWarehouseInventoryForCuInvoice(null, theCuInvDetail, "add");
			if (aCuinvoice.getJoReleaseDetailId() != null
					&& aCuinvoice.getJoReleaseDetailId() != 0) {
				JoReleaseDetail aJoreleaseDetail = (JoReleaseDetail) aSession
						.get(JoReleaseDetail.class,
								aCuinvoice.getJoReleaseDetailId());
				// checAllocatedAmount(aJoreleaseDetail.getJoReleaseId());
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	public BigDecimal getTemplatePriceDetails(Integer cuInvoiceID){
		Session aSession = null;
		BigDecimal margin = new BigDecimal(0);
		
		BigDecimal cost = new BigDecimal(0);
		Map<String,BigDecimal> aPrice = new HashMap<String, BigDecimal>();
		String Query = null;
		try {
			Query = "SELECT prMaster.prMasterID,template.QuantityBilled FROM cuInvoiceDetail AS template,prMaster "
					+ "WHERE template.cuInvoiceID= :cuInvoiceID AND prMaster.prMasterID = template.prMasterID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(Query);
			aQuery.setParameter("cuInvoiceID", cuInvoiceID);
			List aList = aQuery.list();
			if (!aList.isEmpty()) {
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					BigDecimal qty = (BigDecimal)aObj[1]== null ? new BigDecimal(0.00) : (BigDecimal)aObj[1];
					cost = cost.add(itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));
					
				}				
			}
				
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			
		} finally {
			aSession.flush();
			aSession.close();
			Query = null;
		}
		return cost;
	}
	
	

	@Override
	public boolean editSOReleaseLineItem(Cusodetail theCuSODetail,
			Cuso theCuso, Cuso editCuso) throws JobException {
		Session cusoDetailSession = null;
		Transaction aTransaction = null;
		Cusodetail aCusodetail = new Cusodetail();
		Cuso aCuso = new Cuso();
		cusoDetailSession = itsSessionFactory.openSession();
		BigDecimal checkTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		BigDecimal oldQuantityOrdered = new BigDecimal(0);
		BigDecimal newQuantityOrdered = new BigDecimal(0);
		Map<String, BigDecimal> productCost = null;
		try {
			aCuso = (Cuso) cusoDetailSession.get(Cuso.class, theCuso.getCuSoid());
			aTransaction = cusoDetailSession.beginTransaction();
			aCusodetail = (Cusodetail) cusoDetailSession.get(Cusodetail.class,
					theCuSODetail.getCuSodetailId());
			aTransaction.begin();
			oldQuantityOrdered = aCusodetail.getQuantityOrdered();
			newQuantityOrdered = theCuSODetail.getQuantityOrdered();
			
			// Added to reduce On Order Value in Prmaster
			Prmaster thePrmaster =getPrMasterBasedOnId(theCuSODetail.getPrMasterId());
			 if(aCuso!=null && aCuso.getJoReleaseId()!=null){
			if(thePrmaster.getIsInventory()==1){
			updatePrMasterForSO(theCuSODetail, oldQuantityOrdered,
					newQuantityOrdered, "edit", aCusodetail);
			updatePrWarehouseInventoryForSO(theCuSODetail, oldQuantityOrdered,
					newQuantityOrdered, "edit", aCusodetail);
			}
			}else{
				if(thePrmaster.getIsInventory()==1){
				updatePrMasterForSO(theCuSODetail, oldQuantityOrdered,
						newQuantityOrdered, "edit", aCusodetail);
				updatePrWarehouseInventoryForSO(theCuSODetail, oldQuantityOrdered,
						newQuantityOrdered, "edit", aCusodetail);
				}
			}
			
			 if(oldQuantityOrdered.compareTo(newQuantityOrdered)!=0){
				 productCost = salesServices.getTemplatePriceDetails(aCusodetail.getCuSoid(), aCusodetail.getCuSodetailId(), aCusodetail.getCuSodetailId());
				 aCusodetail.setWhseCost(productCost.get("productnqty"));
			 }
			aCusodetail.setDescription(theCuSODetail.getDescription());
			aCusodetail.setPrMasterId(theCuSODetail.getPrMasterId());
			aCusodetail.setPriceMultiplier(theCuSODetail.getPriceMultiplier());
			aCusodetail.setUnitCost(theCuSODetail.getUnitCost());
			aCusodetail.setQuantityOrdered(theCuSODetail.getQuantityOrdered());
			aCusodetail.setTaxable(theCuSODetail.getTaxable());
			cusoDetailSession.update(aCusodetail);
			aTransaction.commit();
			aTransaction = cusoDetailSession.beginTransaction();
			aTransaction.begin();
			
			BigDecimal oldTax = aCuso.getTaxTotal();
			int compare = (oldTax==null?new BigDecimal("0"):oldTax).compareTo(checkTax);
			if (aCuso.getTaxTotal() != null && compare == 1)
				Taxtotal = (oldTax==null?new BigDecimal("0"):oldTax).subtract(editCuso.getTaxTotal());
			Taxtotal = (Taxtotal==null?new BigDecimal("0"):Taxtotal).add(theCuso.getTaxTotal());
			aCuso.setTaxTotal(Taxtotal);
			cusoDetailSession.update(aCuso);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			cusoDetailSession.flush();
			cusoDetailSession.close();
		}
		return true;
	}

	@Override
	public boolean editCuInvoiceReleaseLineItem(Cuinvoicedetail theCuInvDetail,
			Cuinvoice theCuInv, Cuinvoice editcuInv) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		Cuinvoicedetail aCuinvdetail = new Cuinvoicedetail();
		Cuinvoice aCuinvoice = new Cuinvoice();
		aSession= itsSessionFactory.openSession();
		BigDecimal checkTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		int compare = 0;
		BigDecimal OldQuantityBilled = new BigDecimal(0);
		Boolean tplog=false;
		Boolean tpFromSo=false;
		boolean returnvalue=false;
		try {
			Integer userID=theCuInvDetail.getUserID();
			 String userName=theCuInvDetail.getUserName();
			aTransaction = aSession.beginTransaction();
			aCuinvdetail = (Cuinvoicedetail) aSession.get(
					Cuinvoicedetail.class,
					theCuInvDetail.getCuInvoiceDetailId());
			 returnvalue=aCuinvdetail.comparetwoobjects(aCuinvdetail, theCuInvDetail);
			
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
					theCuInv.getCuInvoiceId());
			if(aCuinvoice.getCuSoid()==null){
				aCuinvoice.setCuSoid(0);
			}
			OldQuantityBilled = aCuinvdetail.getQuantityBilled();
			Cuso aCuso= null;
			if(aCuinvoice.getCuSoid()!=null){
			 aCuso= (Cuso) aSession.get(Cuso.class,aCuinvoice.getCuSoid());
			}
			Prmaster thePrmaster =getPrMasterBasedOnId(theCuInvDetail.getPrMasterId());
			if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()>0){
				JoReleaseDetail jrd=(JoReleaseDetail) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
				JoRelease jor= null;
				if(jrd.getJoReleaseId()!=null){
					jor=(JoRelease) aSession.get(JoRelease.class,jrd.getJoReleaseId());
				}
			if(aCuso!=null){
				if(aCuso.getJoReleaseId()==null){
					if(thePrmaster.getIsInventory()==1){
					tplog=true;
					tpFromSo=true;
					}
				}else{
					//Inside job InventoryUpdate inside job
						if(thePrmaster.getIsInventory()==1){
							tplog=true;
							tpFromSo=true;
						}
				}
				}else if(jor!=null && jor.getReleaseType()==3){
					if(thePrmaster.getIsInventory()==1){
						tplog=true;
						tpFromSo=false;
						}
				}
				}else{
					if(aCuso!=null){
						if(thePrmaster.getIsInventory()==1){
							tplog=true;
							tpFromSo=true;
						}
					}
					else{
						itsLogger.info("CuSOID#"+aCuso+ " Cuso is Null!");
						if(thePrmaster.getIsInventory()==1){
							tplog=true;
							tpFromSo=false;
						}
					}
				}
			
			
			
			//updatePrMasterForCuInvoice(aCuinvdetail, theCuInvDetail, "edit");
			//updatePrWarehouseInventoryForCuInvoice(aCuinvdetail,
			//		theCuInvDetail, "edit");
			aTransaction.begin();
			aCuinvdetail.setDescription(theCuInvDetail.getDescription());
			aCuinvdetail.setPrMasterId(theCuInvDetail.getPrMasterId());
			aCuinvdetail
					.setPriceMultiplier(theCuInvDetail.getPriceMultiplier());
			aCuinvdetail.setUnitCost(theCuInvDetail.getUnitCost());
			BigDecimal prevQuantityBilled = aCuinvdetail.getQuantityBilled();
			BigDecimal latestQuantityBilled = theCuInvDetail
					.getQuantityBilled();
			aCuinvdetail.setQuantityBilled(theCuInvDetail.getQuantityBilled());
			BigDecimal difference = prevQuantityBilled
					.subtract(latestQuantityBilled);
			aCuinvdetail.setTaxable(theCuInvDetail.getTaxable());
			aCuinvdetail.setNote(theCuInvDetail.getNote());
			Boolean rollbackexecute=false;
			if(theCuInvDetail.getCuInvoiceDetailId()>0){
				Session atplogSession= itsSessionFactory.openSession();
				Cuinvoicedetail oldCuinvdetail = (Cuinvoicedetail) atplogSession.get(Cuinvoicedetail.class,theCuInvDetail.getCuInvoiceDetailId());
				if(oldCuinvdetail.getQuantityBilled().compareTo(theCuInvDetail.getQuantityBilled()) !=0 && tplog){
					rollbackexecute=true;
					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
					Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+theCuInvDetail.getPrMasterId());
					aTpInventoryLog.setPrMasterID(theCuInvDetail.getPrMasterId());
					aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
					aTpInventoryLog.setWareHouseID(aCuinvoice.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("CI");
					aTpInventoryLog.setTransDecription("CI RollBack");
					aTpInventoryLog.setTransID(theCuInvDetail.getCuInvoiceId());
					aTpInventoryLog.setTransDetailID(theCuInvDetail.getCuInvoiceDetailId());
					aTpInventoryLog.setProductOut(OldQuantityBilled.compareTo(new BigDecimal("0.0000"))==-1?OldQuantityBilled.multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
					aTpInventoryLog.setProductIn(OldQuantityBilled.compareTo(new BigDecimal("0.0000"))==1?OldQuantityBilled:new BigDecimal("0.0000"));
					aTpInventoryLog.setUserID(aCuinvoice.getCreatedById());
					aTpInventoryLog.setCreatedOn(new Date());
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * RollBack
					 * */
					//Cusodetail aCusodetail = (Cusodetail) atplogSession.get(Cusodetail.class, aCuinvdetail.getCuSodetailId());
					BigDecimal oqo=oldCuinvdetail.getQuantityBilled();
					Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuinvoice.getPrFromWarehouseId());
					Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuinvoice.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aCuinvoice.getPrFromWarehouseId() ,otheprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),otheprwarehsinventory.getInventoryOnHand(),
							(tpFromSo==true&&oldCuinvdetail.getCuSodetailId()!=null?oqo:BigDecimal.ZERO),BigDecimal.ZERO,"CI",theCuInvDetail.getCuInvoiceId(),theCuInvDetail.getCuInvoiceDetailId(),aCuinvoice.getInvoiceNumber(),aCuso!=null?aCuso.getSonumber():"" ,
			/*Product out*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
			/*Product in*/ (oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO,
							"CI Edited",userID,userName,new Date());
					itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
					/*Code Ends*/
					
				}
			}
			
			if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()>0){
				JoReleaseDetail jrd=(JoReleaseDetail) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
				JoRelease jor= null;
				if(jrd.getJoReleaseId()!=null){
					jor=(JoRelease) aSession.get(JoRelease.class,jrd.getJoReleaseId());
				}
			if(aCuso!=null){
				if(aCuso.getJoReleaseId()==null){
					if(thePrmaster.getIsInventory()==1){
					updatePrMasterCustomerinvoiceWithSo(aCuinvoice.getCuInvoiceId(),theCuInvDetail,"edit");
					tplog=true;
					tpFromSo=true;
					}
				}else{
					//Inside job InventoryUpdate inside job
						if(thePrmaster.getIsInventory()==1){
							updatePrMasterCustomerinvoiceInsideJob(theCuInv.getCuInvoiceId(),theCuInvDetail,"edit");
							tplog=true;
							tpFromSo=true;
						}
				}
				}else if(jor!=null && jor.getReleaseType()==3){
					if(thePrmaster.getIsInventory()==1){
						updatePrMasterCustomerinvoiceWithoutSo(theCuInv.getCuInvoiceId(),theCuInvDetail,"edit");
						tplog=true;
						tpFromSo=false;
						}
				}
				}else{
					if(aCuso!=null){
						if(thePrmaster.getIsInventory()==1){
							updatePrMasterCustomerinvoiceWithSo(aCuinvoice.getCuInvoiceId(),theCuInvDetail,"edit");
							tplog=true;
							tpFromSo=true;
						}
					}
					else{
						itsLogger.info("CuSOID#"+aCuso+ " Cuso is Null!");
						if(thePrmaster.getIsInventory()==1){
							updatePrMasterCustomerinvoiceWithoutSo(aCuinvoice.getCuInvoiceId(),theCuInvDetail,"edit");
							tplog=true;
							tpFromSo=false;
						}
					}
				}
			
			aSession.update(aCuinvdetail);
			aTransaction.commit();
			
			
			
			if(theCuInvDetail.getCuInvoiceDetailId()>0){
				Cuinvoicedetail newCuinvdetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,theCuInvDetail.getCuInvoiceDetailId());
				if(rollbackexecute && tplog){
				TpInventoryLog theTpInventoryLog = new TpInventoryLog();
				Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+theCuInvDetail.getPrMasterId());
				theTpInventoryLog.setPrMasterID(theCuInvDetail.getPrMasterId());
				theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
				theTpInventoryLog.setWareHouseID(aCuinvoice.getPrFromWarehouseId());
				theTpInventoryLog.setTransType("CI");
				theTpInventoryLog.setTransDecription("CI Edited");
				theTpInventoryLog.setTransID(theCuInvDetail.getCuInvoiceId());
				theTpInventoryLog.setTransDetailID(theCuInvDetail.getCuInvoiceDetailId());
				theTpInventoryLog.setProductOut(theCuInvDetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?theCuInvDetail.getQuantityBilled():new BigDecimal("0.0000"));
				theTpInventoryLog.setProductIn(theCuInvDetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?theCuInvDetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				theTpInventoryLog.setUserID(aCuinvoice.getCreatedById());
				theTpInventoryLog.setCreatedOn(new Date());
				itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 * */
					BigDecimal qo=newCuinvdetail.getQuantityBilled();
					Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuinvoice.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuinvoice.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),
							aPrmaster.getItemCode(),
							aCuinvoice.getPrFromWarehouseId() ,
							theprwarehouse.getSearchName(),
							aPrmaster.getInventoryOnHand(),
							theprwarehsinventory.getInventoryOnHand(),
							(tpFromSo==true&&newCuinvdetail.getCuSodetailId()!=null?newCuinvdetail.getQuantityBilled().multiply(new BigDecimal(-1)):BigDecimal.ZERO),BigDecimal.ZERO,"CI",aCuinvoice.getCuInvoiceId(),
							theCuInvDetail.getCuInvoiceDetailId(),aCuinvoice.getInvoiceNumber(),aCuso!=null?aCuso.getSonumber():"" ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
							"CI Edited",userID,userName,new Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
				}
			}
			
			
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			BigDecimal oldTax = aCuinvoice.getTaxTotal();
			if (oldTax != null)
				oldTax.compareTo(checkTax);
			if (aCuinvoice.getTaxTotal() != null && compare == 1)
				Taxtotal = oldTax.subtract(editcuInv.getTaxTotal());
			Taxtotal = Taxtotal.add(theCuInv.getTaxTotal());
			aCuinvoice.setTaxTotal(Taxtotal);
			if(theCuInv!=null && theCuInv.getReleaseType() != null && !theCuInv.getReleaseType().equals("1") && !theCuInv.getReleaseType().equals("4")){
				aCuinvoice.setCostTotal(getTemplatePriceDetails(theCuInv.getCuInvoiceId()));
				aCuinvoice.setWhseCostTotal(getTemplatePriceDetails(theCuInv.getCuInvoiceId()));
			}else{
				aCuinvoice.setCostTotal(new BigDecimal("0.0000"));
				aCuinvoice.setWhseCostTotal(new BigDecimal("0.0000"));
			}
			aCuinvoice.setSubtotal(aCuinvoice.getSubtotal().add(
					theCuInv.getSubtotal()));
			aCuinvoice.setInvoiceAmount(aCuinvoice.getInvoiceAmount().add(
					theCuInv.getCostTotal()));
			
			aSession.update(aCuinvoice);
			aTransaction.commit();
			if (aCuinvdetail.getCuSodetailId() != null && theCuInv.getReleaseType()!=null && !theCuInv.getReleaseType().equals("Drop Ship")) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();

				Cusodetail acusodetail = (Cusodetail) aSession.get(
						Cusodetail.class, aCuinvdetail.getCuSodetailId());
				if(acusodetail != null && acusodetail.getQuantityBilled()!=null)
				acusodetail.setQuantityBilled(acusodetail.getQuantityBilled()
						.subtract(difference));
				aTransaction.commit();
				JoReleaseDetail aJoreleaseDetail = (JoReleaseDetail) aSession
						.get(JoReleaseDetail.class,
								aCuinvoice.getJoReleaseDetailId());
				// checAllocatedAmount(aJoreleaseDetail.getJoReleaseId());

			}else if(aCuinvdetail.getCuSodetailId() != null && theCuInv.getReleaseType()==null ){
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();

				Cusodetail acusodetail = (Cusodetail) aSession.get(
						Cusodetail.class, aCuinvdetail.getCuSodetailId());
				if(acusodetail != null && acusodetail.getQuantityBilled()!=null)
				acusodetail.setQuantityBilled(acusodetail.getQuantityBilled()
						.subtract(difference));
				aTransaction.commit();
			}

		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
			
		} finally {
			aSession.flush();
			aSession.close();
		}
		return returnvalue;
	}

	@Override
	public Cusodetail getSingleCusoDetailObj(int CusoDetailID)
			throws JobException {
		Session aSession = null;
		Cusodetail acusoDetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			acusoDetail = (Cusodetail) aSession.get(Cusodetail.class,
					CusoDetailID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return acusoDetail;
	}

	@Override
	public Cuinvoicedetail getSingleCuInvoiceDetailObj(int cuInvoiceDetailId)
			throws JobException {
		Session aSession = null;
		Cuinvoicedetail acusoDetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			acusoDetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,
					cuInvoiceDetailId);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return acusoDetail;
	}

	@Override
	public boolean deleteSOReleaseLineItem(Cusodetail theCusodetail,
			Cuso theCuso) throws JobException {
		Session acusoSession = null;
		Transaction aTransaction = null;
		Cusodetail aCusodetail = new Cusodetail();
		Cuso acuSO = new Cuso();
		BigDecimal newTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		BigDecimal oldQuantityOrdered = new BigDecimal(0);
		BigDecimal newQuantityOrdered = new BigDecimal(0);
		try {
			acusoSession = itsSessionFactory.openSession();
			if(theCusodetail!=null && theCusodetail.getCuSodetailId()!=null){
			aCusodetail = (Cusodetail) acusoSession.get(Cusodetail.class,
					theCusodetail.getCuSodetailId());
			}
			oldQuantityOrdered = aCusodetail.getQuantityOrdered();
			newQuantityOrdered = theCusodetail.getQuantityOrdered();
			
			acuSO = (Cuso) acusoSession.get(Cuso.class, theCuso.getCuSoid());
			int prmasterid=0;
			if(aCusodetail!=null && aCusodetail.getPrMasterId()!=null){
				prmasterid=aCusodetail.getPrMasterId();
			}
			Prmaster thePrmaster =getPrMasterBasedOnId(prmasterid);
			if(acuSO!=null && acuSO.getJoReleaseId()!=null){
			if(thePrmaster!=null && thePrmaster.getIsInventory()==1){
			updatePrMasterForSO(aCusodetail, new BigDecimal(0), new BigDecimal(
					0), "del", null);
			updatePrWarehouseInventoryForSO(aCusodetail, new BigDecimal(0),
					new BigDecimal(0), "del", null);
			}
			}else{
				if(thePrmaster.getIsInventory()==1){
				updatePrMasterForSO(aCusodetail, new BigDecimal(0), new BigDecimal(
						0), "del", null);
				updatePrWarehouseInventoryForSO(aCusodetail, new BigDecimal(0),
						new BigDecimal(0), "del", null);
				}
			}
			if(aCusodetail!=null && aCusodetail.getCuSodetailId()!=null){
			aTransaction = acusoSession.beginTransaction();
			aTransaction.begin();
			acusoSession.delete(aCusodetail);
			aTransaction.commit();
			}
			if(acuSO!=null&&acuSO.getCuSoid()!=null){
			aTransaction = acusoSession.beginTransaction();
			aTransaction.begin();
			if (acuSO.getTaxTotal() != null)
				Taxtotal = acuSO.getTaxTotal();
			int compare = Taxtotal.compareTo(newTax);
			if (compare == 1 && theCuso.getTaxTotal() != newTax)
				acuSO.setTaxTotal(acuSO.getTaxTotal().subtract(
						theCuso.getTaxTotal()));
			acusoSession.update(acuSO);
			aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acusoSession.flush();
			acusoSession.close();
		}
		return true;
	}

	@Override
	 public boolean deleteCUInvReleaseLineItem(Cuinvoicedetail thecuInvdetObj,
	   Cuinvoice theCuso) throws JobException {
	  Session acusodetSession = null;
	  Session aSession=null;
	  Transaction aTransaction = null;
	  Cuinvoicedetail aCuInvdetail = null;
	  Cuinvoice aCuinvoice = new Cuinvoice();
	  Byte isTaxable = 0;
	  Boolean tplog=false;
	  Boolean tpFromSo=false;
	  try {
		 Integer userID=thecuInvdetObj.getUserID();
		 String userName=thecuInvdetObj.getUserName();
	   acusodetSession = itsSessionFactory.openSession();
	   aSession=itsSessionFactory.openSession();
	   
	   aTransaction = acusodetSession.beginTransaction();
	   aTransaction.begin();
	   
	   aCuInvdetail = (Cuinvoicedetail) acusodetSession.get(
	     Cuinvoicedetail.class, thecuInvdetObj.getCuInvoiceDetailId());
	   aCuinvoice = (Cuinvoice) acusodetSession.get(Cuinvoice.class,
	     aCuInvdetail.getCuInvoiceId());
	   Cuso aCuso = null;
	   if(aCuinvoice.getCuSoid()!=null){
	    aCuso= (Cuso) aSession.get(Cuso.class,aCuinvoice.getCuSoid());
	   }
	   Prmaster thePrmaster =getPrMasterBasedOnId(aCuInvdetail.getPrMasterId());
	   if(aCuinvoice.getJoReleaseDetailId()!=null && aCuinvoice.getJoReleaseDetailId()>0){
	    JoReleaseDetail jrd=(JoReleaseDetail) aSession.get(JoReleaseDetail.class,aCuinvoice.getJoReleaseDetailId());
	    JoRelease jor= null;
	    if(jrd.getJoReleaseId() != null){
	     jor=(JoRelease) aSession.get(JoRelease.class,jrd.getJoReleaseId());
	    }
	   if(aCuso!=null){
	    
	     if(aCuso.getJoReleaseId()==null){
	     if(thePrmaster.getIsInventory()==1){
	     updatePrMasterCustomerinvoiceWithSo(aCuinvoice.getCuInvoiceId(),aCuInvdetail,"delete");
	     tplog=true;
		 tpFromSo=true;
	     }
	    }else{
	     //Inside job InventoryUpdate inside job
	      if(thePrmaster.getIsInventory()==1){
	       updatePrMasterCustomerinvoiceInsideJob(aCuinvoice.getCuInvoiceId(),aCuInvdetail,"delete");
	       tplog=true;
	       tpFromSo=true;
	      }
	    }
	    }else if(jor!=null && jor.getReleaseType()==3){
	     if(thePrmaster.getIsInventory()==1){
	      updatePrMasterCustomerinvoiceWithoutSo(aCuinvoice.getCuInvoiceId(),aCuInvdetail,"delete");
	      tplog=true;
		  tpFromSo=false;
	      }
	    }
	   }else{
	    if(aCuso!=null){
	     if(thePrmaster.getIsInventory()==1){
	      updatePrMasterCustomerinvoiceWithSo(aCuinvoice.getCuInvoiceId(),aCuInvdetail,"delete");
	      tplog=true;
	      tpFromSo=true;
	      }
	     }else{
	      itsLogger.info("CusoID#"+aCuso+" Without SO.");
	      if(thePrmaster.getIsInventory()==1){
	      updatePrMasterCustomerinvoiceWithoutSo(aCuinvoice.getCuInvoiceId(),aCuInvdetail,"delete");
	      tplog=true;
	      tpFromSo=false;
	      }
	     }
	    }
	   
	   //updatePrMasterForCuInvoice(null, aCuInvdetail, "del");
	   //updatePrWarehouseInventoryForCuInvoice(null, aCuInvdetail, "del");
	   isTaxable = aCuInvdetail.getTaxable();
	  
	   if(aCuInvdetail.getCuSodetailId()!=null&&aCuInvdetail.getCuSodetailId()>0){
		  Session acusoifdetSession = itsSessionFactory.openSession();
		  Transaction bTransaction = acusoifdetSession.beginTransaction();
		  bTransaction.begin();
		  Cusodetail acusodetail = (Cusodetail) acusoifdetSession.get(
	      Cusodetail.class, aCuInvdetail.getCuSodetailId());

	    if (acusodetail != null && acusodetail.getQuantityBilled() != null) {
	    	acusodetail.setQuantityBilled(acusodetail.getQuantityBilled()
	    			.subtract(aCuInvdetail.getQuantityBilled()));
	    	acusoifdetSession.update(acusodetail);
	    }
	    }
	   if(aCuInvdetail.getCuInvoiceDetailId()>0 && tplog){
		    Cuinvoice theCuinvoice=(Cuinvoice) aSession.get(Cuinvoice.class, aCuInvdetail.getCuInvoiceId());
		    TpInventoryLog aTpInventoryLog = new TpInventoryLog();
		    
		    Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aCuInvdetail.getPrMasterId());
		    aTpInventoryLog.setPrMasterID(aCuInvdetail.getPrMasterId());
		    aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
		    aTpInventoryLog.setWareHouseID(theCuinvoice.getPrFromWarehouseId());
		    aTpInventoryLog.setTransType("CI");
		    aTpInventoryLog.setTransDecription("CI Deleted");
		    aTpInventoryLog.setTransID(aCuInvdetail.getCuInvoiceId());
		    aTpInventoryLog.setTransDetailID(aCuInvdetail.getCuInvoiceDetailId());
		    aTpInventoryLog.setProductOut(aCuInvdetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?aCuInvdetail.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
		    aTpInventoryLog.setProductIn(aCuInvdetail.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?aCuInvdetail.getQuantityBilled():new BigDecimal("0.0000"));
		    aTpInventoryLog.setUserID(theCuinvoice.getCreatedById());
		    aTpInventoryLog.setCreatedOn(new Date());
		    itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
		    
		    /*TpInventoryLogMaster
			 * Created on 04-12-2015
			 * Code Starts
			 * RollBack
			 * */
			BigDecimal oqo=aCuInvdetail.getQuantityBilled();
			Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, theCuinvoice.getPrFromWarehouseId());
			Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theCuinvoice.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
			TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
					aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),theCuinvoice.getPrFromWarehouseId() ,otheprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),otheprwarehsinventory.getInventoryOnHand(),
					aCuInvdetail.getCuSodetailId()!=null?oqo:BigDecimal.ZERO,BigDecimal.ZERO,"CI",theCuinvoice.getCuInvoiceId(),aCuInvdetail.getCuInvoiceDetailId(),theCuinvoice.getInvoiceNumber(),null ,
	/*Product out*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
	/*Product in*/ (oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO,
					"CI Deleted",userID,userName,
					new Date());
			itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
			/*Code Ends*/
		}
	   Integer CuSodetailId=aCuInvdetail.getCuSodetailId();
	   BigDecimal quantity=aCuInvdetail.getQuantityBilled();
	   acusodetSession.delete(aCuInvdetail);
	   aTransaction.commit();
	   
	   
	   
	   
	   if (CuSodetailId != null && aCuinvoice.getReleaseType()!=null && !aCuinvoice.getReleaseType().equals("Drop Ship")) {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();

			Cusodetail acusodetail = (Cusodetail) aSession.get(Cusodetail.class, CuSodetailId);
			if(acusodetail != null && acusodetail.getQuantityBilled()!=null){
			acusodetail.setQuantityBilled(acusodetail.getQuantityBilled().subtract(quantity));
			}
			aTransaction.commit();

		}else if(CuSodetailId != null && aCuinvoice.getReleaseType()==null ){
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();

			Cusodetail acusodetail = (Cusodetail) aSession.get(
					Cusodetail.class, CuSodetailId);
			if(acusodetail != null && acusodetail.getQuantityBilled()!=null){
			acusodetail.setQuantityBilled(acusodetail.getQuantityBilled()
					.subtract(quantity));
			}
			aTransaction.commit();
		}
	   
	   
	   
	   
	   
	   
	   aTransaction = acusodetSession.beginTransaction();
	   aTransaction.begin();
	   
	   if (aCuinvoice.getTaxTotal() != null) {
	    if (aCuinvoice.getTaxTotal().compareTo(new BigDecimal(0)) == 1)
	     if (isTaxable == 1) {
	      aCuinvoice.setTaxTotal(aCuinvoice.getTaxTotal()
	        .subtract(theCuso.getTaxTotal()==null?BigDecimal.ZERO:theCuso.getTaxTotal()));
	     } else
	      aCuinvoice.setTaxTotal(new BigDecimal(0));
	   }
	   acusodetSession.update(aCuinvoice);
	   aTransaction.commit();
	   

	  } catch (Exception excep) {
	   itsLogger.error(excep.getMessage(), excep);
	   JobException aJobException = new JobException(excep.getMessage(),
	     excep);
	   throw aJobException;
	  } finally {
	   acusodetSession.flush();
	   acusodetSession.close();
	  }
	  return true;
	 }

	@Override
	public Boolean saveSoLines(Cuso theCuso) throws JobException {
		Session acuSOSession = null;
		Transaction aTransaction = null;
		Cuso aCuso = new Cuso();
		try {
			acuSOSession = itsSessionFactory.openSession();
			aCuso = (Cuso) acuSOSession.get(Cuso.class, theCuso.getCuSoid());
			aTransaction = acuSOSession.beginTransaction();
			aTransaction.begin();
			aCuso.setFreight(theCuso.getFreight());
			aCuso.setSubTotal(theCuso.getSubTotal());
			aCuso.setCostTotal(theCuso.getCostTotal());
			aCuso.setTaxTotal(theCuso.getTaxTotal());
			aCuso.setWhseCostTotal(theCuso.getWhseCostTotal());
			acuSOSession.update(aCuso);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acuSOSession.flush();
			acuSOSession.close();
		}
		return true;
	}

	@Override
	public Boolean saveCusoDetailWhseCost(Cusodetail theCusodetail) throws JobException {
		Session acuSOSession = null;
		Transaction aTransaction = null;
		Cusodetail aCusodetail = new Cusodetail();
		Map<String,BigDecimal> productCost = null;
		try {
			acuSOSession = itsSessionFactory.openSession();
			aCusodetail = (Cusodetail) acuSOSession.get(Cusodetail.class, theCusodetail.getCuSodetailId());
			aTransaction = acuSOSession.beginTransaction();
			productCost = new HashMap<String,BigDecimal>();
			productCost = salesServices.getTemplatePriceDetails(aCusodetail.getCuSoid(), aCusodetail.getCuSodetailId(), aCusodetail.getPrMasterId());
			aTransaction.begin();
			
			aCusodetail.setWhseCost(productCost.get("productnqty"));
			acuSOSession.update(aCusodetail);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acuSOSession.flush();
			acuSOSession.close();
		}
		return true;
	}
	
	@Override
	public Boolean checkInitialSaveCuInvoice(Integer cuSOID,
			Integer joReleaseDetailid) throws JobException {
		Session acuInvoiceSession = null;
		Boolean Exists = true;
		try {
			acuInvoiceSession = itsSessionFactory.openSession();
			String cuInvoiceQuery = "SELECT * FROM cuInvoice WHERE cuSOID='"
					+ cuSOID + "' and joReleaseDetailID =" + joReleaseDetailid;
			Query aQuery = acuInvoiceSession.createSQLQuery(cuInvoiceQuery);
			if (aQuery.list().isEmpty())
				Exists = false;
			cuInvoiceQuery=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acuInvoiceSession.flush();
			acuInvoiceSession.close();
		}
		return Exists;

	}

	@Override
	public void insertCustomerInvoiceLines(Integer cuSOID, Integer cuInvoiceID)
			throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		Cuso aCuso = new Cuso();
		Cuinvoice aCuinvoice = new Cuinvoice();
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();

			String aInsertInvoice = "INSERT cuInvoiceDetail(cuInvoiceID, cuSODetailID,prMasterID, Description, Note,  QuantityBilled, UnitCost, PriceMultiplier, Taxable,whseCost ) SELECT  "
					+ cuInvoiceID
					+ ",cuSODetailID ,prMasterID  ,Description ,Note ,(QuantityOrdered - QuantityBilled) ,UnitCost ,PriceMultiplier ,Taxable,whseCost FROM cuSODetail WHERE cuSOID="
					+ cuSOID
					+ " AND (IFNULL(QuantityOrdered,0) - IFNULL(QuantityBilled,0)) !=0";
			    /* Changed by Zenith on 2015-05-12 for (-) Quantities 
			     * + " AND (IFNULL(QuantityOrdered,0) - IFNULL(QuantityBilled,0))>0";*/
			aSession.createSQLQuery(aInsertInvoice).executeUpdate();
			aTransaction.commit();
			String updateQuery = "update cuSODetail set QuantityBilled=QuantityOrdered where cuSOID="
					+ cuSOID;
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.createSQLQuery(updateQuery).executeUpdate();
			aTransaction.commit();
			
			aCuso = (Cuso) aSession.get(Cuso.class, cuSOID);
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class, cuInvoiceID);
			aTransaction.begin();
			if(cuSOID!=0){
			aCuinvoice.setWhseCostTotal(aCuso.getWhseCostTotal());
			aCuinvoice.setCostTotal(aCuso.getCostTotal());
			aSession.update(aCuinvoice);
			aTransaction.commit();
			}
			TsUserLogin usrlogin=(TsUserLogin) aSession.get(TsUserLogin.class, aCuinvoice.getCreatedById());
			/***
			 * Updating subtotal of customer invoice on adding line items to
			 * customer invoice
			 */
			//updateCuInvoiceSubTotal(cuInvoiceID);
			if(aCuso!=null){
			if(aCuso.getJoReleaseId()==null){
			Query aQuery = aSession.createQuery("FROM  Cuinvoicedetail where cuInvoiceId="+cuInvoiceID);
			List<Cuinvoicedetail> aQueryList = aQuery.list();
			for(Cuinvoicedetail newcuinv:aQueryList){
				Prmaster thePrmaster =getPrMasterBasedOnId(newcuinv.getPrMasterId());
				if(thePrmaster.getIsInventory()==1){
				updatePrMasterCustomerinvoiceWithSo(cuInvoiceID,newcuinv,"add");

				Cuinvoice theCuinvoice=(Cuinvoice) aSession.get(Cuinvoice.class, cuInvoiceID);
				TpInventoryLog aTpInventoryLog = new TpInventoryLog();
				aTpInventoryLog.setPrMasterID(thePrmaster.getPrMasterId());
				aTpInventoryLog.setProductCode(thePrmaster.getItemCode());
				aTpInventoryLog.setWareHouseID(theCuinvoice.getPrFromWarehouseId());
				aTpInventoryLog.setTransType("CI");
				aTpInventoryLog.setTransDecription("CI Created");
				aTpInventoryLog.setTransID(newcuinv.getCuInvoiceId());
				aTpInventoryLog.setTransDetailID(newcuinv.getCuInvoiceId());
				aTpInventoryLog.setProductOut(newcuinv.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?newcuinv.getQuantityBilled():new BigDecimal("0.0000"));
				aTpInventoryLog.setProductIn(newcuinv.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?newcuinv.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
				aTpInventoryLog.setUserID(theCuinvoice.getCreatedById());
				aTpInventoryLog.setCreatedOn(new Date());
				itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
				
				/*TpInventoryLogMaster
				 * Created on 04-12-2015
				 * Code Starts
				 * */
					BigDecimal qo=(newcuinv.getQuantityBilled());
					Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, theCuinvoice.getPrFromWarehouseId());
					Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theCuinvoice.getPrFromWarehouseId(), thePrmaster.getPrMasterId());
					Prmaster aPrmaster =getPrMasterBasedOnId(newcuinv.getPrMasterId());
					TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
							aPrmaster.getPrMasterId(),
							aPrmaster.getItemCode(),
							theCuinvoice.getPrFromWarehouseId() ,
							theprwarehouse.getSearchName(),
							aPrmaster.getInventoryOnHand(),
							theprwarehsinventory.getInventoryOnHand(),
							(qo.multiply(new BigDecimal(-1))),BigDecimal.ZERO,"CI",theCuinvoice.getCuInvoiceId(),
							newcuinv.getCuInvoiceDetailId(),theCuinvoice.getInvoiceNumber(),aCuso.getSonumber() ,
			/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
			/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
							"CI Created",theCuinvoice.getCreatedById(),usrlogin.getLoginName(),new Date());
					itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
					/*Code Ends*/
			
				
				}
			}
			//updatePrWarehouseInventoryForCuInvoiceFirst(cuInvoiceID);
			}else{
				//Inside job InventoryUpdate inside job
				Query aQuery = aSession.createQuery("FROM  Cuinvoicedetail where cuInvoiceId="+cuInvoiceID);
				List<Cuinvoicedetail> aQueryList = aQuery.list();
				for(Cuinvoicedetail newcuinv:aQueryList){
					Prmaster thePrmaster =getPrMasterBasedOnId(newcuinv.getPrMasterId());
					if(thePrmaster.getIsInventory()==1){
						updatePrMasterCustomerinvoiceInsideJob(cuInvoiceID,newcuinv,"add");

						Cuinvoice theCuinvoice=(Cuinvoice) aSession.get(Cuinvoice.class, cuInvoiceID);
						TpInventoryLog aTpInventoryLog = new TpInventoryLog();
						aTpInventoryLog.setPrMasterID(thePrmaster.getPrMasterId());
						aTpInventoryLog.setProductCode(thePrmaster.getItemCode());
						aTpInventoryLog.setWareHouseID(theCuinvoice.getPrFromWarehouseId());
						aTpInventoryLog.setTransType("CI");
						aTpInventoryLog.setTransDecription("CI Created");
						aTpInventoryLog.setTransID(newcuinv.getCuInvoiceId());
						aTpInventoryLog.setTransDetailID(newcuinv.getCuInvoiceId());
						aTpInventoryLog.setProductOut(newcuinv.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?newcuinv.getQuantityBilled():new BigDecimal("0.0000"));
						aTpInventoryLog.setProductIn(newcuinv.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?newcuinv.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						aTpInventoryLog.setUserID(theCuinvoice.getCreatedById());
						aTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
						
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * */
							BigDecimal qo=(newcuinv.getQuantityBilled());
							Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, theCuinvoice.getPrFromWarehouseId());
							Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theCuinvoice.getPrFromWarehouseId(), thePrmaster.getPrMasterId());
							Prmaster aPrmaster =getPrMasterBasedOnId(newcuinv.getPrMasterId());
							TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),
									aPrmaster.getItemCode(),
									theCuinvoice.getPrFromWarehouseId() ,
									theprwarehouse.getSearchName(),
									aPrmaster.getInventoryOnHand(),
									theprwarehsinventory.getInventoryOnHand(),
									(qo.multiply(new BigDecimal(-1))),BigDecimal.ZERO,"CI",theCuinvoice.getCuInvoiceId(),
									newcuinv.getCuInvoiceDetailId(),theCuinvoice.getInvoiceNumber(),aCuso.getSonumber() ,
					/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
					/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
									"CI Created",theCuinvoice.getCreatedById(),usrlogin.getLoginName(),new Date());
							itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
							/*Code Ends*/
					}
				}
			}
			}else{

				Query aQuery = aSession.createQuery("FROM  Cuinvoicedetail where cuInvoiceId="+cuInvoiceID);
				List<Cuinvoicedetail> aQueryList = aQuery.list();
				for(Cuinvoicedetail newcuinv:aQueryList){
					Prmaster thePrmaster =getPrMasterBasedOnId(newcuinv.getPrMasterId());
					if(aCuinvoice.getJoReleaseDetailId()==null){
					if(thePrmaster.getIsInventory()==1){
					updatePrMasterCustomerinvoiceWithoutSo(cuInvoiceID,newcuinv,"add");
					
					Cuinvoice theCuinvoice=(Cuinvoice) aSession.get(Cuinvoice.class, newcuinv.getCuInvoiceId());
					TpInventoryLog aTpInventoryLog = new TpInventoryLog();
					aTpInventoryLog.setPrMasterID(thePrmaster.getPrMasterId());
					aTpInventoryLog.setProductCode(thePrmaster.getItemCode());
					aTpInventoryLog.setWareHouseID(theCuinvoice.getPrFromWarehouseId());
					aTpInventoryLog.setTransType("CI");
					aTpInventoryLog.setTransDecription("CI Created");
					aTpInventoryLog.setTransID(newcuinv.getCuInvoiceId());
					aTpInventoryLog.setTransDetailID(newcuinv.getCuInvoiceDetailId());
					aTpInventoryLog.setProductOut(newcuinv.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==1?newcuinv.getQuantityBilled():new BigDecimal("0.0000"));
					aTpInventoryLog.setProductIn(newcuinv.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1?newcuinv.getQuantityBilled().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
					aTpInventoryLog.setUserID(theCuinvoice.getCreatedById());
					aTpInventoryLog.setCreatedOn(new Date());
					itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
					
					/*TpInventoryLogMaster
					 * Created on 04-12-2015
					 * Code Starts
					 * */
						BigDecimal qo=(newcuinv.getQuantityBilled());
						Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, theCuinvoice.getPrFromWarehouseId());
						Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(theCuinvoice.getPrFromWarehouseId(), thePrmaster.getPrMasterId());
						Prmaster aPrmaster =getPrMasterBasedOnId(newcuinv.getPrMasterId());
						TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
								aPrmaster.getPrMasterId(),
								aPrmaster.getItemCode(),
								theCuinvoice.getPrFromWarehouseId() ,
								theprwarehouse.getSearchName(),
								aPrmaster.getInventoryOnHand(),
								theprwarehsinventory.getInventoryOnHand(),
								(BigDecimal.ZERO),BigDecimal.ZERO,"CI",newcuinv.getCuInvoiceId(),
								newcuinv.getCuInvoiceDetailId(),theCuinvoice.getInvoiceNumber(),aCuso.getSonumber() ,
				/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
				/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
								"CI Created",newcuinv.getUserID(),usrlogin.getLoginName(),new Date());
						itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
						/*Code Ends*/
				
					
					}
					}
				}
				//updatePrWarehouseInventoryForCuInvoiceFirst(cuInvoiceID);
				
			}
			
			
			
			/*Cuinvoice TheCuInvoice = (Cuinvoice) aSession.get(Cuinvoice.class,cuInvoiceID);
			aTransaction.begin();
			
			 StringBuffer insertTpInventoryLogQuery = new StringBuffer("INSERT INTO tpInventoryLog ")
			.append("(prMasterID, ").append("productCode, ").append("wareHouseID, ").append("transType, ")
			.append("transDecription, ").append("transID, ").append("transDetailID, ")
			.append("productOut, ").append("productIn, ").append("userId, ").append("createdOn ) ")
			.append("(SELECT ").append("cui.prMasterID,").append("prm.ItemCode,")
			.append(TheCuInvoice.getPrFromWarehouseId()).append(", 'CI'").append(", 'CI Created' ,")
			.append(TheCuInvoice.getCuInvoiceId()).append(",cui.cuInvoiceDetailID,")
			.append("IF(cui.QuantityBilled>0,cui.QuantityBilled,0),")
			.append("IF(cui.QuantityBilled<0,cui.QuantityBilled,0),")
			.append(TheCuInvoice.getCreatedById()).append(", now() ")
			.append("FROM cuInvoiceDetail AS cui JOIN prMaster AS prm ON cui.prMasterID = prm.prMasterID ")
			.append("WHERE cuInvoiceID = ").append(TheCuInvoice.getCuInvoiceId()).append(")");
			
			String  aInsertTpInventoryLogQuery = insertTpInventoryLogQuery.toString();
			aSession.createSQLQuery(aInsertTpInventoryLogQuery).executeUpdate();
			aTransaction.commit();*/
			
			
			aInsertInvoice=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	public void insert(Integer cuSOID, Integer cuInvoiceID) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			StringBuffer insertInvoiceQuery = new StringBuffer( // Stringbuffer
																// creates less
																// no of
																// objects.
					"INSERT cuInvoiceDetail")
					.append("(cuInvoiceID, cuSODetailID,")
					.append("prMasterID, ").append("Description, ")
					.append("Note, ").append(" QuantityBilled, ")
					.append("UnitCost ").append(") ").append("SELECT  ")
					.append(cuInvoiceID).append(",cuSODetailID")
					.append(" ,prMasterID ").append(" ,Description ")
					.append(",Note ").append(",QuantityOrdered ").append(",0 ")
					.append("FROM cuSODetail ").append("WHERE cuSOID = ")
					.append(cuSOID).append(";");

			String aInsertInvoiceQuery = insertInvoiceQuery.toString();
			// String as =
			// "INSERT cuInvoiceDetail(cuInvoiceID, cuSODetailID,prMasterID, Description, Note,  QuantityBilled, UnitCost, PriceMultiplier, Taxable ) SELECT "+cuInvoiceID+",cuSODetailID ,prMasterID  ,Description ,Note ,QuantityBilled ,UnitCost ,PriceMultiplier ,Taxable FROM (SELECT cuSODetail.cuSODetailID ,cuSODetail.prMasterID  ,cuSODetail.Description ,cuSODetail.Note ,(cuSODetail.QuantityOrdered-IFNULL(SUM(cuInvoiceDetail.QuantityBilled ),0)) AS QuantityBilled,cuSODetail.UnitCost ,cuSODetail.PriceMultiplier ,cuSODetail.Taxable FROM cuSODetail LEFT JOIN cuInvoiceDetail ON cuSODetail.cuSODetailID = cuInvoiceDetail.cuSODetailID WHERE cuSoDetail.cuSOID= "+cuSOID+"  GROUP BY cuInvoiceDetail.cuSODetailID) AS subQuery WHERE ROUND(QuantityBilled )>0";

			// String
			// aInsertInvoiceQuery="INSERT cuInvoiceDetail(cuInvoiceID, cuSODetailID,prMasterID, Description, Note,  QuantityBilled, UnitCost, PriceMultiplier, Taxable ) SELECT "+cuInvoiceID+",cuSODetailID ,prMasterID  ,Description ,Note ,QuantityBilled ,UnitCost ,PriceMultiplier ,Taxable FROM (SELECT cuSODetail.cuSODetailID ,cuSODetail.prMasterID  ,cuSODetail.Description ,cuSODetail.Note ,(cuSODetail.QuantityOrdered-IFNULL(SUM(cuInvoiceDetail.QuantityBilled ),0)) AS QuantityBilled,cuSODetail.UnitCost ,cuSODetail.PriceMultiplier ,cuSODetail.Taxable FROM cuSODetail LEFT JOIN cuInvoiceDetail ON cuSODetail.cuSODetailID = cuInvoiceDetail.cuSODetailID   WHERE cuSoDetail.cuSOID="+cuSOID+"  GROUP BY cuInvoiceDetail.cuSODetailID) AS subQuery WHERE ROUND(QuantityBilled )>0";

			aSession.createSQLQuery(aInsertInvoiceQuery).executeUpdate();
			aTransaction.commit();
			insertInvoiceQuery=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Cuinvoice getSingleCuInvoiceObj(int cuInvoiceID) throws JobException {
		Session aSession = null;
		Cuinvoice aCuinvoice = null;
		try {
			aSession = itsSessionFactory.openSession();
			aCuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class, cuInvoiceID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCuinvoice;
	}

	@Override
	public String getCuTerms(Integer cuTermsID) throws JobException {
		Session aSession = null;
		CuTerms aCuTerms = null;
		String cuTerms = "";
		if(cuTermsID==null || cuTermsID.equals("")){
			cuTermsID=0;
		}
		aSession = itsSessionFactory.openSession();
		try {
			if (cuTermsID>0) {
				aCuTerms = (CuTerms) aSession.get(CuTerms.class, cuTermsID);
				cuTerms = aCuTerms.getDescription();
			}else{
				aCuTerms = new CuTerms();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aCuTerms = null;
		}
		return cuTerms;
	}
	
	@Override
	public String getCoTaxterritory(Integer coTaxterritoryID) throws JobException {
		Session aSession = null;
		CoTaxTerritory aCoTaxTerritory = null;
		String coTaxTerritory = "";
		if(coTaxterritoryID==null || coTaxterritoryID.equals("")){
			coTaxterritoryID=0;
		}
		aSession = itsSessionFactory.openSession();
		try {
			if (coTaxterritoryID>0) {
				aCoTaxTerritory = (CoTaxTerritory) aSession.get(CoTaxTerritory.class, coTaxterritoryID);
				coTaxTerritory = aCoTaxTerritory.getCounty();
			}else{
				aCoTaxTerritory = new CoTaxTerritory();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return coTaxTerritory;
	}

	@Override
	public JoQuoteProperties getuserquoteProperty(Integer userID)
			throws JobException {
		String getPropertyIdQuery = "FROM JoQuoteProperties WHERE UserLoginID = "
				+ userID;
		Session aSession = null;
		JoQuoteProperties aProperty = null;
		List<JoQuoteProperties> ajoQuoteProperties = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(getPropertyIdQuery);
			ajoQuoteProperties = aQuery.list();
			if (!ajoQuoteProperties.isEmpty()) {
				aProperty = ajoQuoteProperties.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			getPropertyIdQuery=null;
		}
		return aProperty;
	}

	@Override
	public void editSubmittalSchedule(Joscheduledetail aJoscheduledetail)
			throws JobException {
		Session aSession = null;
		Joscheduledetail theJoscheduledetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			theJoscheduledetail = (Joscheduledetail) aSession.get(
					Joscheduledetail.class,
					aJoscheduledetail.getJoScheduleDetailId());
			if (theJoscheduledetail != null) {
				theJoscheduledetail.setCol01(aJoscheduledetail.getCol01());
				theJoscheduledetail.setCol02(aJoscheduledetail.getCol02());
				theJoscheduledetail.setCol03(aJoscheduledetail.getCol03());
				theJoscheduledetail.setCol04(aJoscheduledetail.getCol04());
				theJoscheduledetail.setCol05(aJoscheduledetail.getCol05());
				theJoscheduledetail.setCol06(aJoscheduledetail.getCol06());
				theJoscheduledetail.setCol07(aJoscheduledetail.getCol07());
				theJoscheduledetail.setCol08(aJoscheduledetail.getCol08());
				theJoscheduledetail.setCol09(aJoscheduledetail.getCol09());
				theJoscheduledetail.setCol10(aJoscheduledetail.getCol10());
				theJoscheduledetail.setCol11(aJoscheduledetail.getCol11());
				theJoscheduledetail.setCol12(aJoscheduledetail.getCol12());
				theJoscheduledetail.setCol13(aJoscheduledetail.getCol13());
				theJoscheduledetail.setCol14(aJoscheduledetail.getCol14());
				theJoscheduledetail.setCol15(aJoscheduledetail.getCol15());
				theJoscheduledetail.setCol16(aJoscheduledetail.getCol16());
				theJoscheduledetail.setCol17(aJoscheduledetail.getCol17());
				theJoscheduledetail.setCol18(aJoscheduledetail.getCol18());
				theJoscheduledetail.setCol19(aJoscheduledetail.getCol19());
				theJoscheduledetail.setCol20(aJoscheduledetail.getCol20());
				if (aJoscheduledetail.getJoScheduleModelId() != null)
					theJoscheduledetail.setJoScheduleModelId(aJoscheduledetail
							.getJoScheduleModelId());
				theJoscheduledetail.setJoSubmittalDetailId(aJoscheduledetail
						.getJoSubmittalDetailId());
				theJoscheduledetail
						.setQuantity(aJoscheduledetail.getQuantity());
				aSession.update(theJoscheduledetail);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Boolean checkReportPresent(String query) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Query aQuery = aSession.createSQLQuery(query);
			if (aQuery.list().isEmpty())
				return false;
			else
				return true;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void insertCustomerInvoiceLinesFromPO(Integer vePOID,
			Integer cuInvoiceID,Integer releaseType,BigDecimal aReleaseAllocAmt) throws JobException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Cuinvoice cuInvoice = (Cuinvoice) aSession.get(Cuinvoice.class,
					cuInvoiceID);
			JoReleaseDetail joReleaseDetail = (JoReleaseDetail) aSession.get(
					JoReleaseDetail.class, cuInvoice.getJoReleaseDetailId());
			JoRelease joRelease = (JoRelease) aSession.get(JoRelease.class,	joReleaseDetail.getJoReleaseId());
		//	JobReleaseBean joRleaseBean  = aReleaseAllocatedAmt;
			BigDecimal aReleaseAllocatedAmt = BigDecimal.ZERO;
			if(aReleaseAllocAmt==null){
				aReleaseAllocAmt=aReleaseAllocatedAmt;
			}
			if(joRelease.getEstimatedBilling()==null){
				joRelease.setEstimatedBilling(aReleaseAllocatedAmt);
			}
			if(joRelease!=null)
			aReleaseAllocatedAmt = joRelease.getEstimatedBilling().subtract(aReleaseAllocAmt);
			
			
			
			if (joReleaseDetail.getVendorInvoiceAmount() != null || releaseType==1) {
				String aSelectQry = "SELECT veBillID FROM veBillDetail WHERE veBillID in(select veBillID from veBill where joReleaseDetailID = "
						+ cuInvoice.getJoReleaseDetailId() + ") ";

				List<?> billedList = aSession.createSQLQuery(aSelectQry).list();
				if (!billedList.isEmpty()) {
					/*String query1 = "INSERT INTO cuInvoiceDetail(cuInvoiceID,  prMasterID, Description, Note,QuantityBilled,UnitCost,UnitPrice,PriceMultiplier,Taxable ) VALUES("
							+ cuInvoiceID
							+ ",318,'Center roof fans','',"
							+ billedList.get(0) + ",0,0,0,0);";*/
					
					
							   StringBuffer insertInvoiceQuery = new StringBuffer(
							   "INSERT INTO cuInvoiceDetail").append("(cuInvoiceID, ")
							  .append("cuSODetailID, ").append("prMasterID,")
							  .append(" Description,").append(" Note,")
							  .append("QuantityBilled,").append(" UnitCost,PriceMultiplier,Taxable ) ")
							  .append("(SELECT ").append(cuInvoiceID)
							  .append(",vePODetailID,prMasterID ,Description")
							  .append(",Note ,QuantityBilled ")
							//  .append(",UnitCost,PriceMultiplier,FreightCost FROM veBillDetail ").append("WHERE")
							    .append(",0,0,Taxable FROM veBillDetail ").append("WHERE")
							  .append(" veBillID =").append(billedList.get(0)).append(")");
					
					
					 String  aInsertInvoiceQuery = insertInvoiceQuery.toString();
							  
					 String query1  =  "SELECT prMasterID FROM prMaster WHERE ItemCode='QP'";
					 Integer prMasterIDforQP = (Integer) aSession.createSQLQuery(query1).uniqueResult();
					 Prmaster thePrmaster = (Prmaster) aSession.get(Prmaster.class,	prMasterIDforQP);
					 

					String query2 = "INSERT INTO cuInvoiceDetail(cuInvoiceID,  prMasterID, Description, Note,QuantityBilled,UnitCost,UnitPrice,PriceMultiplier,Taxable ) VALUES("
							+ cuInvoiceID
							+ ",'"
							+ prMasterIDforQP+"','Quoted Price','',1,"
							+ aReleaseAllocatedAmt
							+ ","
							+ aReleaseAllocatedAmt + ",1,"+thePrmaster.getIsTaxable()+");";
					//aSession.createSQLQuery(query1).executeUpdate();
					aSession.createSQLQuery(aInsertInvoiceQuery).executeUpdate();
					aSession.createSQLQuery(query2).executeUpdate();
					aTransaction.commit();
				}else{

					/*String query1 = "INSERT INTO cuInvoiceDetail(cuInvoiceID,  prMasterID, Description, Note,QuantityBilled,UnitCost,UnitPrice,PriceMultiplier,Taxable ) VALUES("
							+ cuInvoiceID
							+ ",318,'Center roof fans','',"
							+ billedList.get(0) + ",0,0,0,0);";*/
					
					
							   StringBuffer insertInvoiceQuery = new StringBuffer(
							   "INSERT INTO cuInvoiceDetail").append("(cuInvoiceID, ")
							  .append("cuSODetailID, ").append("prMasterID,")
							  .append(" Description,").append(" Note,")
							  .append("QuantityBilled,").append(" UnitCost,PriceMultiplier,Taxable ) ")
							  .append("(SELECT ").append(cuInvoiceID)
							  .append(",vePODetailID,prMasterID ,Description,Note ,0,0,0,Taxable ")
							    .append(" FROM vePODetail ").append("WHERE")
							  .append(" vePOID =").append(cuInvoice.getCuSoid()).append(")");
					
					
					 String  aInsertInvoiceQuery = insertInvoiceQuery.toString();
							  
					 String query1  =  "SELECT prMasterID FROM prMaster WHERE ItemCode='QP'";
					 Integer prMasterIDforQP = (Integer) aSession.createSQLQuery(query1).uniqueResult();
					 Prmaster thePrmaster = (Prmaster) aSession.get(Prmaster.class,	prMasterIDforQP);

					String query2 = "INSERT INTO cuInvoiceDetail(cuInvoiceID,  prMasterID, Description, Note,QuantityBilled,UnitCost,UnitPrice,PriceMultiplier,Taxable ) VALUES("
							+ cuInvoiceID
							+ ",'"
							+ prMasterIDforQP+"','Quoted Price','',1,"
							+ aReleaseAllocatedAmt
							+ ","
							+ aReleaseAllocatedAmt + ",1,"+thePrmaster.getIsTaxable()+");";
					//aSession.createSQLQuery(query1).executeUpdate();
					aSession.createSQLQuery(aInsertInvoiceQuery).executeUpdate();
					aSession.createSQLQuery(query2).executeUpdate();
					aTransaction.commit();
				}
			
			}
			else
			{
				/*String query1 = "INSERT INTO cuInvoiceDetail(cuInvoiceID,  prMasterID, Description, Note,QuantityBilled,UnitCost,UnitPrice,PriceMultiplier,Taxable ) VALUES("
				+ cuInvoiceID
				+ ",318,'Center roof fans','',"
				+ billedList.get(0) + ",0,0,0,0);";*/
		
		
				   StringBuffer insertInvoiceQuery = new StringBuffer(
				   "INSERT INTO cuInvoiceDetail").append("(cuInvoiceID, ")
				  .append("cuSODetailID, ").append("prMasterID,")
				  .append(" Description,").append(" Note,")
				  .append("QuantityBilled,").append(" UnitCost,PriceMultiplier,Taxable ) ")
				  .append("(SELECT ").append(cuInvoiceID)
				  .append(",vePODetailID,prMasterID ,Description")
				  .append(",Note ,QuantityOrdered ")
				  .append(",UnitCost,PriceMultiplier,Taxable FROM vePODetail ").append("WHERE")
				  .append(" vePOID =").append(vePOID).append(")");
		
		
		 String  aInsertInvoiceQuery = insertInvoiceQuery.toString();
				  
		 String query1  =  "SELECT prMasterID FROM prMaster WHERE ItemCode='QP'";
		 
		 Integer prMasterIDforQP = (Integer) aSession.createSQLQuery(query1).uniqueResult();

		String query2 = "INSERT INTO cuInvoiceDetail(cuInvoiceID,  prMasterID, Description, Note,QuantityBilled,UnitCost,UnitPrice,PriceMultiplier,Taxable ) VALUES("
				+ cuInvoiceID
				+ ",'"
				+ prMasterIDforQP+"','Quoted Price','',1,"
				+ aReleaseAllocatedAmt
				+ ","
				+ aReleaseAllocatedAmt + ",1,0);";
		//aSession.createSQLQuery(query1).executeUpdate();
		aSession.createSQLQuery(aInsertInvoiceQuery).executeUpdate();
		aSession.createSQLQuery(query2).executeUpdate();
		aTransaction.commit();
	
			
			}
			/*
			 * StringBuffer insertInvoiceQuery = new StringBuffer(
			 * "INSERT INTO cuInvoiceDetail").append("(cuInvoiceID, ")
			 * .append("cuSODetailID, ").append("prMasterID,")
			 * .append(" Description,").append(" Note,")
			 * .append("QuantityBilled,"
			 * ).append(" UnitCost,PriceMultiplier,Taxable ) ")
			 * .append("(SELECT ").append(cuInvoiceID)
			 * .append(",vePODetailID,prMasterID ,Description")
			 * .append(",Note ,QuantityOrdered ")
			 * .append(",UnitCost,PriceMultiplier,Taxable FROM vePODetail "
			 * ).append("WHERE")
			 * .append(" vePOID =").append(vePOID).append(")"); String
			 * aInsertInvoiceQuery = insertInvoiceQuery.toString();
			 * aSession.createSQLQuery(aInsertInvoiceQuery).executeUpdate();
			 * aTransaction.commit();
			 */

			// cuInvoiceID
			/*Cuinvoice TheCuInvoice = (Cuinvoice) aSession.get(Cuinvoice.class,cuInvoiceID);
			aTransaction.begin();
			
			 StringBuffer insertTpInventoryLogQuery = new StringBuffer("INSERT INTO tpInventoryLog ")
			.append("(prMasterID, ").append("productCode, ").append("wareHouseID, ").append("transType, ")
			.append("transDecription, ").append("transID, ").append("transDetailID, ")
			.append("productOut, ").append("productIn, ").append("userId, ").append("createdOn ) ")
			.append("(SELECT ").append("cui.prMasterID,").append("prm.ItemCode,")
			.append(TheCuInvoice.getPrFromWarehouseId()).append(", 'CI'").append(", 'CI Created' ,")
			.append(TheCuInvoice.getCuInvoiceId()).append(",cui.cuInvoiceDetailID,")
			.append("IF(cui.QuantityBilled>0,cui.QuantityBilled,0),")
			.append("IF(cui.QuantityBilled<0,cui.QuantityBilled,0),")
			.append(TheCuInvoice.getCreatedById()).append(", now() ")
			.append("FROM cuInvoiceDetail AS cui JOIN prMaster AS prm ON cui.prMasterID = prm.prMasterID ")
			.append("WHERE cuInvoiceID = ").append(TheCuInvoice.getCuInvoiceId()).append(")");
			
			String  aInsertTpInventoryLogQuery = insertTpInventoryLogQuery.toString();
			aSession.createSQLQuery(aInsertTpInventoryLogQuery).executeUpdate();
			aTransaction.commit();*/
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Jomaster getJoMasterByJoReleaseID(int joReleaseId)
			throws JobException {
		Session aSession = null;
		Jomaster aJoMaster = null;
		try {
			aSession = itsSessionFactory.openSession();
			JoRelease aJoReleaseObj = (JoRelease) aSession.get(JoRelease.class,
					joReleaseId);
			aJoMaster = (Jomaster) aSession.get(Jomaster.class,
					aJoReleaseObj.getJoMasterId());
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new JobException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoMaster;
	}

	@Override
	public JoQuoteTemplateProperties getuserquoteTemplateProperty(Integer userID)
			throws JobException {
		String getPropertyIdQuery = "FROM JoQuoteTemplateProperties WHERE UserLoginID = "
				+ userID;
		Session aSession = null;
		JoQuoteTemplateProperties aProperty = null;
		List<JoQuoteTemplateProperties> ajoQuoteTempProperties = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(getPropertyIdQuery);
			ajoQuoteTempProperties = aQuery.list();
			if (!ajoQuoteTempProperties.isEmpty()) {
				aProperty = ajoQuoteTempProperties.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			getPropertyIdQuery=null;
		}
		return aProperty;
	}

	@Override
	public Vepo getVepo(Integer vePOID) {
		Session aSession = null;
		Vepo aVepo = null;
		try {
			aSession = itsSessionFactory.openSession();
			aVepo = (Vepo) aSession.get(Vepo.class, vePOID);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVepo;
	}

	private List<Vepodetail> getvePOList(Integer theVepoID) throws JobException {
		String aJobSelectQry = "SELECT vePOID, vePODetailID FROM vePODetail WHERE vePOID = "
				+ theVepoID;
		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		try {
			Vepodetail aVepodetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aVepodetail = new Vepodetail();
				Object[] aObj = (Object[]) aIterator.next();
				aVepodetail.setVePoid((Integer) aObj[0]);
				aVepodetail.setVePodetailId((Integer) aObj[1]);
				aQueryList.add(aVepodetail);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aQueryList;
	}

	// Added by CGS
	@Override
	public Vepo addPOGeneral(Vepo theVepo, Rxaddress theRxaddress,
			Rxaddress theRxaddressShipTo, JoRelease theJoRelease)
			throws JobException {
		itsLogger.debug("add PO General.");
		Session aSession = itsSessionFactory.openSession();
		Vepo aVepo = null;
		Vepo aVepoforRef = null;
		Integer aRxAddressShipID = null;
		Integer aRxAddresID = null;
		Transaction aTransaction;
		
		
		try {
			
			aTransaction = aSession.beginTransaction();
			
			Rxmaster aRxmaster = null; 
			Integer iBillToID = null;
			Integer iShipToID = null;
			Integer arxMasterID = null;
			if ("Other".equalsIgnoreCase(theRxaddress.getFax())) {
				if(theRxaddress.getName()!=null && !theRxaddress.getName().trim().equals("")){
				aRxmaster = new Rxmaster();
				aRxmaster.setInActive(false);
				aRxmaster.setName(theRxaddress.getName());
				aRxmaster.setSearchName(theRxaddress.getName().substring(0,4).toUpperCase().trim());
				aRxmaster.setFirstName("");
				aRxmaster.setIsCustomer(true);
				
				arxMasterID = (Integer) aSession.save(aRxmaster);
				theRxaddress.setRxMasterId(arxMasterID);
				aRxAddresID = (Integer) aSession.save(theRxaddress);
				}
				String sQry = "SELECT rxAddressId FROM rxAddress ORDER BY rxAddressId DESC LIMIT 1";
				Query aQuery = aSession.createSQLQuery(sQry);
				Iterator<?> aIterator = aQuery.list().iterator();

				while (aIterator.hasNext()) {
					iBillToID = (Integer) aIterator.next();
				}
			}
			if (theVepo.getShipToMode() == 3) {
				aRxmaster = new Rxmaster();
				if(theVepo.getRxShipToAddressId()!=null)
				{
					Rxaddress aradd=new Rxaddress();
					aradd = (Rxaddress) aSession.get(Rxaddress.class,theVepo.getRxShipToAddressId());	
					//aradd.setFax(theRxaddressShipTo.getFax());
					aradd.setAddress1(theRxaddressShipTo.getAddress1());
					aradd.setAddress2(theRxaddressShipTo.getAddress2());
					aradd.setCity(theRxaddressShipTo.getCity());
					aradd.setState(theRxaddressShipTo.getState());
					aradd.setIsBillTo(theRxaddressShipTo.getIsBillTo());
					aradd.setInActive(theRxaddressShipTo.getInActive());
					aradd.setIsMailing(theRxaddressShipTo.getIsMailing());
					aradd.setIsStreet(theRxaddressShipTo.getIsStreet());
					aradd.setIsShipTo(theRxaddressShipTo.getIsShipTo());
					aradd.setOtherShipTo(theRxaddressShipTo.getOtherShipTo());
					aradd.setZip(theRxaddressShipTo.getZip());
					aradd.setName(theRxaddressShipTo.getName());
					aSession.update(aradd);
					
					
				}
				else
				{
					//if(theRxaddressShipTo.getName()!=null && !theRxaddressShipTo.getName().trim().equals("")){
					/*aRxmaster.setInActive(false);
					aRxmaster.setName(theRxaddressShipTo.getName());
					aRxmaster.setSearchName(theRxaddressShipTo.getName().substring(0,4).toUpperCase().trim());
					aRxmaster.setFirstName("");
					aRxmaster.setIsCustomer(true);
					
					arxMasterID = (Integer) aSession.save(aRxmaster);*/
					
					//theRxaddressShipTo.setRxMasterId(arxMasterID);
					aRxAddressShipID = (Integer) aSession.save(theRxaddressShipTo);
					//}
					String sQry = "SELECT rxAddressId FROM rxAddress ORDER BY rxAddressId DESC LIMIT 1";
					Query aQuery = aSession.createSQLQuery(sQry);
					Iterator<?> aIterator = aQuery.list().iterator();
	
					while (aIterator.hasNext()) {
						iShipToID = (Integer) aIterator.next();
					}
				}
				aTransaction.commit();
			}

			if (null != theVepo.getVePoid()) {

				String sPO = "";
				
				String sPONumberQry = "SELECT ponumber FROM vePO WHERE vePoid = :vePOID";
				Query aQuery2 = aSession.createSQLQuery(sPONumberQry)
						.setParameter("vePOID", theVepo.getVePoid());
				Iterator<?> aIterator2 = aQuery2.list().iterator();
				while (aIterator2.hasNext()) {
					sPO = (String) aIterator2.next();
					
				}
				theVepo.setPonumber(sPO);

			}
			if (null == theVepo.getVePoid()) {
				if (null == theVepo.getPonumber()|| "".equalsIgnoreCase(theVepo.getPonumber())| "null".equalsIgnoreCase(theVepo.getPonumber())) {
					theVepo.setPonumber(itsSysService.getSysSequenceNumber(
							"vePO").toString());
				}
				theVepo.setRxBillToAddressId(iBillToID);
				theVepo.setRxShipToAddressId(iShipToID);
				theVepo.setShipTo(theVepo.getPrWarehouseId());
				theVepo.setRxShipToAddressId(theVepo.getRxShipToAddressId());
				theVepo.setCreatedOn(new Date());
				aSession.save(theVepo);
				aTransaction.commit();

				String sQry = "SELECT vePOID FROM vePO ORDER BY vePOID DESC LIMIT 1";
				Query aQuery = aSession.createSQLQuery(sQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				Integer iVepoid = null;
				while (aIterator.hasNext()) {
					iVepoid = (Integer) aIterator.next();
				}

				aVepo = (Vepo) aSession.get(Vepo.class, iVepoid);
			} else {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aVepo = (Vepo) aSession.get(Vepo.class, theVepo.getVePoid());
				
					if (iBillToID != null) {
						theVepo.setRxBillToAddressId(iBillToID);
						theVepo.setBillToIndex(2);
					}
	
					if (iShipToID != null) {
						theVepo.setRxShipToAddressId(iShipToID);
						theVepo.setShipTo(3);
					}
				
				theVepo.setRxShipToAddressId(theVepo.getRxShipToAddressId());
				theVepo.setShipTo(theVepo.getPrWarehouseId());
				theVepo.setCreatedOn(aVepo.getCreatedOn());
				theVepo.setChangedOn(new Date());
				theVepo.setChangedById(theVepo.getOrderedById());
				theVepo.setreceiveddate(aVepo.getreceiveddate());
				
				
				//This One update when warehouseChanges
					aVepo = (Vepo) aSession.get(Vepo.class, theVepo.getVePoid());
					
					
					String VepodetailQuery="FROM Vepodetail WHERE vePOID ="+theVepo.getVePoid();
					Query theQuery = aSession.createQuery(VepodetailQuery);
					ArrayList<Vepodetail> vepoDetailList = (ArrayList<Vepodetail>) theQuery.list();
					for(Vepodetail theVepodetail:vepoDetailList){
						RollBackPrMasterPrWareHouseInventoryForPO(theVepodetail.getVePoid(), theVepodetail.getVePodetailId());
					}
					
				aSession.merge(theVepo);
				aTransaction.commit();
				for(Vepodetail theVepodetail:vepoDetailList){
					insertPrMasterPrWareHouseInventoryForPO(theVepodetail.getVePoid(), theVepodetail.getVePodetailId());
				}
				aVepo = (Vepo) aSession.get(Vepo.class, theVepo.getVePoid());
				
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aVepo;
	}

	public void updateVePOTaxAmt(Integer theVepoID, BigDecimal taxAmt)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aVePOTransaction;
		Vepo aVepo = null;
		try {
			aVePOTransaction = aVePOSession.beginTransaction();
			aVepo = (Vepo) aVePOSession.get(Vepo.class, theVepoID);
			aVepo.setTaxTotal(taxAmt);

			aVePOSession.update(aVepo);
			aVePOTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}

	}

	@Override
	public String addPOLineDetails(List<Vepodetail> alVepodetails,
			BigDecimal taxAmt) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer iVepoid = null;
		for (Vepodetail objVepodetail : alVepodetails) {
			iVepoid = objVepodetail.getVePoid();
		}
		if (null != iVepoid) {
			List<Vepodetail> ve = getvePOList(iVepoid);
			if (ve.size() > 0) {
				Session aSearchSession = itsSessionFactory.openSession();
				Transaction aTransaction123 = null;
				Vepodetail aVepodetail = null;
				Vepo aVepo = null;
				try {
 					for (int index = 0; index < ve.size(); index++) {

						aVepodetail = (Vepodetail) aSearchSession.get(
								Vepodetail.class, ve.get(index)
										.getVePodetailId());
						aTransaction123 = aSearchSession.beginTransaction();
						aTransaction123.begin();
						aSearchSession.delete(aVepodetail);
						aTransaction123.commit();
						updateInventoryOrdered(aVepodetail);
						updatePrWarehouseInventoryOrdered(aVepodetail, "del");
					}

				} catch (Exception excep) {
					itsLogger.error(excep.getMessage(), excep);
					JobException aJobException = new JobException(
							excep.getMessage(), excep);
					throw aJobException;
				} finally {
					aSearchSession.flush();
					aSearchSession.close();
				}
			}

			/*
			 * for(Vepodetail objVepodetail : alVepodetails) { aTransaction =
			 * aVePOSession.beginTransaction(); aTransaction.begin();
			 * aVePOSession.delete(objVepodetail); aTransaction.commit(); }
			 */

		}
		try {

			if (alVepodetails.size() > 0) {
				for (Vepodetail objVepodetail : alVepodetails) {
					aTransaction = aVePOSession.beginTransaction();
					aTransaction.begin();
					if (objVepodetail.getDescription() != null) {
						if (null == objVepodetail.getTaxable()) {
							objVepodetail.setTaxable(false);
						}

						aVePOSession.save(objVepodetail);
						aTransaction.commit();
						updatePrMaster(objVepodetail, alVepodetails.size());
						updatePrWarehouseInventoryOrdered(objVepodetail, "add");
						itsLogger.info("Orderdered Quantity: "+objVepodetail.getQuantityOrdered());
					}

				}

				if (taxAmt.compareTo(BigDecimal.ZERO) != 0) {
					updateVePOTaxAmt(iVepoid, taxAmt);
				}

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "success";
	}

	public String updatePrMaster(Vepodetail objVepodetail,
			Integer vepodetailsize) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		//Session aVePOSession1 = itsSessionFactory.openSession();
		//Transaction aTransaction1;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();

			/*
			 * Vepo objVepo= (Vepo)
			 * aVePOSession.get(Vepo.class,objVepodetail.getVePoid());
			 * itsLogger.info("FreightAmount=="+objVepo.getFreight()); int
			 * comp=objVepo.getFreight().compareTo(new BigDecimal(0));
			 * BigDecimal freightamount=new BigDecimal(0); If comp==0 both are
			 * equal if comp==1 first value is greater if comp=-1 second value
			 * is greater if(objVepo!=null && comp==1){
			 * freightamount=objVepo.getFreight().divide(new
			 * BigDecimal(vepodetailsize)); }
			 * itsLogger.info("PriceMultiplier=="+
			 * objVepodetail.getPriceMultiplier()); BigDecimal
			 * productcost=objVepodetail
			 * .getUnitCost().multiply(objVepodetail.getPriceMultiplier
			 * ()).add(freightamount).setScale(2,BigDecimal.ROUND_DOWN);
			 */
		//	aTransaction = aVePOSession.beginTransaction();
		//	aTransaction.begin();
			Prmaster objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
					objVepodetail.getPrMasterId());
			BigDecimal order = objPrmaster.getInventoryOnOrder();
			objPrmaster.setInventoryOnOrder(order==null?new BigDecimal("0.0000").add(objVepodetail
					.getQuantityOrdered()):order.add(objVepodetail
					.getQuantityOrdered()));
			// objPrmaster.setLastCost(productcost);
			aVePOSession.update(objPrmaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}

	public String updateInventoryOrdered(Vepodetail objVepodetail)
			throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Prmaster objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
					objVepodetail.getPrMasterId());
			BigDecimal order = new BigDecimal(0);
			if(objPrmaster.getInventoryOnOrder() != null)
				order = objPrmaster.getInventoryOnOrder();
			objPrmaster.setInventoryOnOrder(order.subtract(objVepodetail
					.getQuantityOrdered()));
			aVePOSession.update(objPrmaster);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}

	public String updatePrWarehouseInventoryOrdered(Vepodetail objVepodetail,
			String oper) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Prmaster aPrmaster = null;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Prwarehouseinventory thePrwarehouseinventory = null;
//			Cuso aCuso = (Cuso) aVePOSession.get(Cuso.class,
//					objVepodetail.getVePoid());
			Integer sPrWarehouseID = 0;
			BigDecimal onOrder = new BigDecimal(0);
			Integer prWarehouseInventoryID = 0;
			String sQuery = "SELECT prWarehouseID FROM vePO vepo WHERE vePOID = "
					+ objVepodetail.getVePoid();
			Query query = aVePOSession.createSQLQuery(sQuery);
			
			List<?> listQuery1= query.list();
			if (listQuery1.size() > 0)
				sPrWarehouseID = (Integer) listQuery1.get(0);

			String sQuery1 = "SELECT inventory.InventoryOnOrder,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
					+ objVepodetail.getPrMasterId()
					+ " AND prWarehouseID = "
					+ sPrWarehouseID;
			query = aVePOSession.createSQLQuery(sQuery1);
			/*
			 * Iterator<?> iterator = query.list().iterator();
			 * while(iterator.hasNext()) { Object[] object = (Object[])
			 * iterator.next(); onOrder.add((BigDecimal)object[0]); }
			 */
			List<?> listQuery2= query.list();
			if (listQuery2.size() > 0) {
				Object[] object = (Object[]) listQuery2.get(0);
				onOrder = (BigDecimal) object[0];
				prWarehouseInventoryID = (Integer) object[1];
			}else{
				thePrwarehouseinventory = new Prwarehouseinventory();
				thePrwarehouseinventory.setPrWarehouseId(sPrWarehouseID);
				thePrwarehouseinventory.setPrMasterId(objVepodetail.getPrMasterId());
				thePrwarehouseinventory.setHasInitialCost(new Byte("0"));
				thePrwarehouseinventory.setInventoryOnOrder(new BigDecimal("0.0000"));
				thePrwarehouseinventory.setInventoryOnHand(new BigDecimal("0.0000"));
				thePrwarehouseinventory.setInventoryAllocated(new BigDecimal("0.0000"));
				thePrwarehouseinventory.setBin("");
				prWarehouseInventoryID = (Integer)aVePOSession.save(thePrwarehouseinventory);
			}
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory) aVePOSession
					.get(Prwarehouseinventory.class, prWarehouseInventoryID);
			if (objPrwarehouseinventory != null) {
				BigDecimal order = objPrwarehouseinventory.getInventoryOnOrder();
				if ("del".equalsIgnoreCase(oper)){
					objPrwarehouseinventory.setInventoryOnOrder(order==null?new BigDecimal("0.0000").add(objVepodetail
							.getQuantityOrdered()):order
							.subtract(objVepodetail.getQuantityOrdered()));
				}else if ("add".equalsIgnoreCase(oper)){
					objPrwarehouseinventory.setInventoryOnOrder(order==null?new BigDecimal("0.0000").add(objVepodetail
							.getQuantityOrdered()):order
							.add(objVepodetail.getQuantityOrdered()));
				}else if ("edit".equalsIgnoreCase(oper)){
					Vepodetail theVepodetail=(Vepodetail) aVePOSession.get(Vepodetail.class, objVepodetail.getVePodetailId());
					objPrwarehouseinventory.setInventoryOnOrder((order==null?new BigDecimal("0.0000"):order).subtract(
							theVepodetail.getQuantityOrdered()==null?new BigDecimal("0.0000"):theVepodetail.getQuantityOrdered()).add(
							objVepodetail.getQuantityOrdered()==null?new BigDecimal("0.0000"):objVepodetail.getQuantityOrdered()));
				}
				aVePOSession.update(objPrwarehouseinventory);
				
				List<Prwarehouseinventory> aQueryList = null;
					Query querys = aVePOSession
							.createQuery("FROM  Prwarehouseinventory WHERE prMasterID="+objVepodetail.getPrMasterId());
						aQueryList = querys.list();
				BigDecimal quantityOrdered = new BigDecimal("0.0000");
				if(aQueryList.size()>0){
					for(int j=0;j<aQueryList.size();j++){
						quantityOrdered = quantityOrdered.add(aQueryList.get(j).getInventoryOnOrder()==null?new BigDecimal("0.0000"):aQueryList.get(j).getInventoryOnOrder());
					}
					 aPrmaster = (Prmaster) aVePOSession
							.get(Prmaster.class, objVepodetail.getPrMasterId());
					aPrmaster.setInventoryOnOrder(quantityOrdered);
					aVePOSession.update(aPrmaster);
				}
				
				aTransaction.commit();
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}

	public List<Vepodetail> getPOLineDetails(Integer theVepoID)
			throws JobException {

		String aPOLineItemListQry = "";
		String condition = "";
		if (theVepoID != null) {
			condition = " WHERE ve.vePOID = " + theVepoID;
		}
		/*
		 * aPOLineItemListQry = "SELECT ve.vePODetailID," + " ve.vePOID," +
		 * " ve.prMasterID," + " ve.Description," + " ve.QuantityOrdered," +
		 * " ve.Taxable," + " ve.UnitCost," + " ve.PriceMultiplier," +
		 * " ve.posistion," + " pr.ItemCode, " + " vepo.TaxTotal, " +
		 * " ve.Note, " + " ve.EstimatedShipDate," + "ve.AcknowledgementDate," +
		 * " ve.VendorOrderNumber, " +
		 * " ve.QuantityReceived,IF(vbd.QuantityBilled IS NULL,0.0000,vbd.QuantityBilled) AS invoiced "
		 * + " FROM vePODetail ve " +
		 * " Left Join prMaster pr on ve.prMasterID = pr.prMasterID" +
		 * " Right Join vePO vepo on vepo.vePOID = ve.vePOID" +
		 * " LEFT JOIN veBillDetail vbd ON ve.vePODetailID = vbd.vePOdetailID" +
		 * " where ve.vePOID = " + theVepoID + " ORDER BY ve.posistion";
		 */

		aPOLineItemListQry = "SELECT ve.vePODetailID,"
				+ " ve.vePOID,"
				+ " ve.prMasterID,"
				+ " ve.Description,"
				+ " ve.QuantityOrdered,"
				+ " ve.Taxable,"
				+ " ve.UnitCost,"
				+ " ve.PriceMultiplier,"
				+ " ve.posistion,"
				+ " pr.ItemCode, "
				+ " vepo.TaxTotal, "
				+ " ve.Note, "
				+ " ve.EstimatedShipDate,"
				+ "ve.AcknowledgementDate,"
				+ " ve.VendorOrderNumber, "
				+ " ve.QuantityReceived,IF(SUM(vbd.QuantityBilled) IS NULL,0.0000,SUM(vbd.QuantityBilled)) AS invoiced "
				+ " FROM vePODetail ve "
				+ " Left Join prMaster pr on ve.prMasterID = pr.prMasterID"
				+ " Right Join vePO vepo on vepo.vePOID = ve.vePOID"
				+ " LEFT JOIN veBillDetail vbd ON ve.vePODetailID = vbd.vePOdetailID"
				+ condition + " GROUP BY ve.vePODetailID"
				+ " ORDER BY ve.posistion";

		Session aSession = null;
		ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
		BigDecimal aTotal;
		BigDecimal aNetCast;
		String description = "";
		try {
			Vepodetail avepoDetail = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				avepoDetail = new Vepodetail();
				Object[] aObj = (Object[]) aIterator.next();
				avepoDetail.setVePodetailId((Integer) aObj[0]);
				avepoDetail.setVePoid((Integer) aObj[1]);
				avepoDetail.setPrMasterId((Integer) aObj[2]);
				
				
				
				
				/*if(description.contains("'")){
					description = description.replaceAll("'", "&#39;");
				}*/
				
				if(aObj[3]!=null)
				{
				description = (String)aObj[3];
				description = description.replaceAll("\\r\\n|\\r|\\n", " ");
				description = description.replaceAll("\"", "&#34;");
				description = description.replaceAll("\'", "&#39;");
				}
				
				avepoDetail.setDescription(description);
				
				avepoDetail.setQuantityOrdered(aObj[4]!=null?(BigDecimal)aObj[4]:BigDecimal.ZERO);
				if ((Byte) aObj[5] == 1) {
					avepoDetail.setTaxable(true);
				} else {
					avepoDetail.setTaxable(false);
				}
				avepoDetail.setUnitCost(aObj[6]!=null?(BigDecimal)aObj[6]:BigDecimal.ZERO);
				avepoDetail.setPriceMultiplier(aObj[7]!=null?(BigDecimal)aObj[7]:BigDecimal.ZERO);
				avepoDetail.setPosistion((Double) aObj[8]);
				String notes ="";
				
				if (aObj[11] != null) {
					
					notes = (String) aObj[11];
					notes = notes.replaceAll("\\r\\n|\\r|\\n", "\\n");
					notes = notes.replaceAll("\"", "&#34;");
					notes = notes.replaceAll("\'", "&#39;");
					System.out.println("["+notes+"]");
					
				}
				String notevalue="";
				if (aObj[9] != null) {
					
					notevalue = (String) aObj[9];
					notevalue = notevalue.replaceAll("\\r\\n|\\r|\\n", "\\n");
					notevalue = notevalue.replaceAll("\"", "&#34;");
					notevalue = notevalue.replaceAll("\'", "&#39;");
					System.out.println("["+notevalue+"]");
					
				}
				
				
				avepoDetail.setNote(notevalue + "&^& " + notes);
				avepoDetail.setInLineNote(notes);
				avepoDetail.setInLineNoteImage(notes);
				
				
				
				// avepoDetail.setInLineNote((String) aObj[11]);
				BigDecimal aUnitCost = aObj[6]!=null?(BigDecimal)aObj[6]:BigDecimal.ZERO;
				BigDecimal aPriceMult = aObj[7]!=null?(BigDecimal)aObj[7]:BigDecimal.ZERO;
				BigDecimal aQuantity = aObj[4]!=null?(BigDecimal)aObj[4]:BigDecimal.ZERO;
				if (aUnitCost != null && aPriceMult != null
						&& aQuantity != null) {
					if (Double.valueOf(aPriceMult.toString()) > Double
							.valueOf(0.0000)) {
						aTotal = aUnitCost.multiply(aPriceMult);
						aTotal = aTotal.multiply(aQuantity);
						avepoDetail.setQuantityBilled(aTotal);
					} else {
						aTotal = aUnitCost.multiply(aQuantity);
						avepoDetail.setQuantityBilled(aTotal);
					}
				} else if (aUnitCost != null && aQuantity != null) {
					aTotal = aUnitCost.multiply(aQuantity);
					avepoDetail.setQuantityBilled(aTotal);
				} else if (aUnitCost != null && aPriceMult != null) {
					aTotal = aUnitCost.multiply(aQuantity);
					avepoDetail.setQuantityBilled(aTotal);
				} else if (aUnitCost != null) {
					avepoDetail.setQuantityBilled(aUnitCost);
				}
				if (aUnitCost != null && aPriceMult != null) {
					if (Double.valueOf(aPriceMult.toString()) <= Double
							.valueOf(0.0000)) {
						avepoDetail.setNetCast(aUnitCost);
					} else {
						aNetCast = aUnitCost.multiply(aPriceMult);
						avepoDetail.setNetCast(aNetCast);
					}
				}
				avepoDetail.setTaxTotal((BigDecimal) aObj[10]);
				avepoDetail.setInLineNote((String) aObj[11]);

				if (aObj[12] != null) {
					avepoDetail.setShipDate((String) DateFormatUtils.format(
							(Date) aObj[12], "dd-MM-yyyy"));
				}
				if (aObj[13] != null) {
					avepoDetail.setAckDate((String) DateFormatUtils.format(
							(Date) aObj[13], "dd-MM-yyyy"));
				}
				String orderNumber = (String) aObj[14];
				if (null == orderNumber || "null".equalsIgnoreCase(orderNumber)
						|| "".equalsIgnoreCase(orderNumber)
						|| "undefined".equalsIgnoreCase(orderNumber)
						|| orderNumber.length() <= 0) {
					orderNumber = "";
				}
				avepoDetail.setVendorOrderNumber(orderNumber);
				avepoDetail.setQuantityReceived((BigDecimal) aObj[15]);
				avepoDetail.setInvoicedAmount((BigDecimal) aObj[16]);
				// avepoDetail.setNotesDesc((String) aObj[15]);
				/*
				 * avepoDetail.setPosistion((Double) aObj[8]);
				 * avepoDetail.setNote((String) aObj[9]);
				 * avepoDetail.setAcknowledgedDate((Date) aObj[12]);
				 * avepoDetail.setEstimatedShipDate((Date) aObj[13]);
				 */
				// avepoDetail.setoInLineNote((String) aObj[14]);
				aQueryList.add(avepoDetail);

			}

		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry=null;condition=null;
		}
		return aQueryList;

	}

	@Override
	public JoQuoteTemplateProperties getuserTempquoteProperty(Integer userID)
			throws JobException {
		String getPropertyIdQuery = "FROM JoQuoteTemplateProperties WHERE UserLoginID = "
				+ userID;
		Session aSession = null;
		JoQuoteTemplateProperties aProperty = null;
		List<JoQuoteTemplateProperties> ajoQuoteProperties = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(getPropertyIdQuery);
			ajoQuoteProperties = aQuery.list();
			if (!ajoQuoteProperties.isEmpty()) {
				aProperty = ajoQuoteProperties.get(0);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			getPropertyIdQuery=null;
		}
		return aProperty;
	}

	@Override
	public Integer populatecusoID() throws JobException {
		Session aSession = null;
		int CuSOID = 0;
		try {
			aSession = itsSessionFactory.openSession();
			String acusoIdQuery = "SELECT cuSO.cuSOID FROM cuSO ORDER BY cuSO.cuSOID DESC LIMIT 1";
			Query aQuery = aSession.createSQLQuery(acusoIdQuery);
			if (!aQuery.list().isEmpty())
				CuSOID = (Integer) aQuery.list().get(0);
			acusoIdQuery=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return CuSOID + 1;
	}

	@Override
	public Cuso addSOGeneral(Cuso theCuso, Rxaddress theShipToOtherAddress)
			throws JobException {
		Session aCuSOSession = null;
		Cuso aCuso = null;
		Integer aRxAddressShipID = null;
		Integer iShipToID = null;
		String sQry = null;
		String sQry2 = null;
		Query aQuery = null;
		Iterator<?> aIterator = null;
		Query aQuery2 = null;
		Iterator<?> aIterator2 = null;
		
		try {
			aCuSOSession = itsSessionFactory.openSession();
			Transaction cuSOTransaction = null;
			if ("Other".equalsIgnoreCase(theShipToOtherAddress.getFax())) {
				cuSOTransaction = aCuSOSession.beginTransaction();
				cuSOTransaction.begin();
				aRxAddressShipID = (Integer) aCuSOSession
						.save(theShipToOtherAddress);
				cuSOTransaction.commit();

				sQry = "SELECT rxAddressID FROM rxAddress ORDER BY rxAddressID DESC LIMIT 1";
				aQuery = aCuSOSession.createSQLQuery(sQry);
				aIterator = aQuery.list().iterator();

				while (aIterator.hasNext()) {
					iShipToID = (Integer) aIterator.next();
				}
				theCuso.setRxShipToAddressId(iShipToID);

			}
			theCuso.setCuSoid(null);
			if (null == theCuso.getCuSoid()) {
				if (null == theCuso.getSonumber()
						|| "".equalsIgnoreCase(theCuso.getSonumber())
						|| "null".equalsIgnoreCase(theCuso.getSonumber())) {
					//Commented for ID#494
					//sQry2 = "SELECT SONumber FROM cuSO WHERE SONumber REGEXP '^[0-9]+$' ORDER BY cuSOID DESC LIMIT 1";
					sQry2 = "SELECT sequence FROM sysSequence WHERE TableName='cuSO'";
					// String sQry2 =
					// "SELECT CONVERT(SUBSTRING_INDEX(SONumber,'-',-1),UNSIGNED INTEGER) AS num FROM cuSO WHERE IsNumeric(SONumber) = 1 ORDER BY 1 DESC LIMIT 1";
					aQuery2 = aCuSOSession.createSQLQuery(sQry2);
					aIterator2 = aQuery2.list().iterator();
					BigInteger iSO = new BigInteger("0");
					while (aIterator2.hasNext()) {

						Integer sSONumber = (Integer) aIterator2.next();
					}
					theCuso.setSonumber(String.valueOf((iSO.intValue() + 1)));
				}
				// theVepo.setRxBillToAddressId(iBillToID);
				// theVepo.setRxShipToAddressId(iShipToID);
				cuSOTransaction = aCuSOSession.beginTransaction();
				cuSOTransaction.begin();
				Integer icuSoid=(Integer) aCuSOSession.save(theCuso);
				cuSOTransaction.commit();

				/*sQry = "SELECT cuSOID FROM cuSO ORDER BY cuSOID DESC LIMIT 1";
				aQuery = aCuSOSession.createSQLQuery(sQry);
				aIterator = aQuery.list().iterator();
				Integer icuSoid = null;
				while (aIterator.hasNext()) {
					icuSoid = (Integer) aIterator.next();
				}*/
				/*For Increament the value*/
				aCuso = (Cuso) aCuSOSession.get(Cuso.class, icuSoid);
				if(icuSoid>0){
					cuSOTransaction = aCuSOSession.beginTransaction();
					cuSOTransaction.begin();
					String soNo=itsSysService.getSysSequenceNumber("cuSO").toString();
					aCuso.setSonumber(soNo);
					aCuSOSession.update(aCuso);
					cuSOTransaction.commit();
				}
				
				/*
				 * aCuso.setJoReleaseId(icuSoid); cuSOTransaction =
				 * aCuSOSession.beginTransaction(); cuSOTransaction.begin();
				 * aCuSOSession.update(aCuso); cuSOTransaction.commit();
				 */
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aCuSOSession.flush();
			aCuSOSession.close();
			sQry = null;
			sQry2 = null;
			aQuery = null;
			 aIterator = null;
			aQuery2 = null;
			aIterator2 = null;
		}
		return aCuso;
	}

	@Override
	public Cuso getSingleCUSODetails(Integer theCUSOID) throws JobException {
		Session aSession = null;
		Cuso objCuso = null;
		try {
			aSession = itsSessionFactory.openSession();
			objCuso = (Cuso) aSession.get(Cuso.class, theCUSOID);
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return objCuso;
	}

	@Override
	public Rxaddress getRxAddressFromAddressID(Integer theRxAddressrID)
			throws JobException {
		Session aSession = null;
		Rxaddress aRxAddress = null;
		try {
			if (theRxAddressrID != null) {
				aSession = itsSessionFactory.openSession();
				aRxAddress = (Rxaddress) aSession.get(Rxaddress.class,
						theRxAddressrID);
				if(aRxAddress!=null)
				{
					if (aRxAddress.getCity() != null
							&& !aRxAddress.getCity().equalsIgnoreCase("")) {
						String aCity = aRxAddress.getCity().trim();
						aRxAddress.setCity(aCity);
					} else {
						aRxAddress.setCity("");
					}
					if (aRxAddress.getAddress1() == null) {
						aRxAddress.setCity("");
					}
					if (aRxAddress.getAddress2() == null) {
						aRxAddress.setAddress2("");
					}
					if (aRxAddress.getZip() == null) {
						aRxAddress.setZip("");
					}
					if (aRxAddress.getState() == null) {
						aRxAddress.setState("");
					}
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxAddress;
	}

	@Override
	public Rxaddress getShipToAddress(Integer theRxMasterID)
			throws JobException {
		Session aSession = null;
		Rxaddress aRxAddress = null;
		String sQry = "SELECT rxmaster.Name,Address1,Address2,City,State,Zip,coTaxTerritoryID,address.rxAddressID FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = :theRxMasterID  and IsShipTo=1 ORDER BY address.rxAddressID";
		Query aQuery2 = null;
		Iterator<?> aIterator = null;
		try {
			if (theRxMasterID != null) {
				// theRxAddressID = getRxAddressID(theRxAddressID);
				aSession = itsSessionFactory.openSession();
				aQuery2 = aSession.createSQLQuery(sQry).setParameter(
						"theRxMasterID", theRxMasterID);
				aIterator = aQuery2.list().iterator();
				while (aIterator.hasNext()) {
					aRxAddress = new Rxaddress();
					Object[] aObj = (Object[]) aIterator.next();

					if (aObj[0] != null)
						aRxAddress.setName((String) aObj[0]);
					else
						aRxAddress.setName("");
					if (aObj[1] == null) {
						aRxAddress.setAddress1("");
					} else
						aRxAddress.setAddress1((String) aObj[1]);
					if (aObj[2] == null) {
						aRxAddress.setAddress2("");
					} else
						aRxAddress.setAddress2((String) aObj[2]);
					if (aObj[3] != null) {
						String aCity = (String) aObj[3];
						aRxAddress.setCity(aCity.trim());
					} else {
						aRxAddress.setCity("");
					}
					if (aObj[5] == null) {
						aRxAddress.setZip("");
					} else {
						aRxAddress.setZip((String) aObj[5]);
					}
					if (aObj[4] == null) {
						aRxAddress.setState("");
					} else {
						aRxAddress.setState((String) aObj[4]);
					}
					if (aObj[6] == null) {
						aRxAddress.setCoTaxTerritoryId(0);
					} else {
						aRxAddress.setCoTaxTerritoryId((Integer) aObj[6]);
					}
					if (aObj[7] == null) {
						aRxAddress.setRxAddressId(0);
					} else {
						aRxAddress.setRxAddressId((Integer) aObj[7]);
					}
				}

			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			sQry=null;
			aQuery2 = null;
			aIterator = null;
		}
		return aRxAddress;
	}
	

	@Override
	public Rxaddress getShipToAddressforSO(Integer theRxMasterID)
			throws JobException {
		Session aSession = null;
		Rxaddress aRxAddress = null;
		String sQry = "SELECT rxmaster.Name,Address1,Address2,City,State,Zip,coTaxTerritoryID,address.rxAddressID FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxAddressID = :theRxMasterID  and IsShipTo=1 ORDER BY address.rxAddressID";
		Query aQuery2 = null;
		Iterator<?> aIterator = null;
		try {
			if (theRxMasterID != null) {
				// theRxAddressID = getRxAddressID(theRxAddressID);
				aSession = itsSessionFactory.openSession();
				aQuery2 = aSession.createSQLQuery(sQry).setParameter(
						"theRxMasterID", theRxMasterID);
				List list = aQuery2.list();
				if(list.size()>0)
				{
				aIterator = list.iterator();
				while (aIterator.hasNext()) {
					aRxAddress = new Rxaddress();
					Object[] aObj = (Object[]) aIterator.next();

					if (aObj[0] != null)
						aRxAddress.setName((String) aObj[0]);
					else
						aRxAddress.setName("");
					if (aObj[1] == null) {
						aRxAddress.setAddress1("");
					} else
						aRxAddress.setAddress1((String) aObj[1]);
					if (aObj[2] == null) {
						aRxAddress.setAddress2("");
					} else
						aRxAddress.setAddress2((String) aObj[2]);
					if (aObj[3] != null) {
						String aCity = (String) aObj[3];
						aRxAddress.setCity(aCity.trim());
					} else {
						aRxAddress.setCity("");
					}
					if (aObj[5] == null) {
						aRxAddress.setZip("");
					} else {
						aRxAddress.setZip((String) aObj[5]);
					}
					if (aObj[4] == null) {
						aRxAddress.setState("");
					} else {
						aRxAddress.setState((String) aObj[4]);
					}
					if (aObj[6] == null) {
						aRxAddress.setCoTaxTerritoryId(0);
					} else {
						aRxAddress.setCoTaxTerritoryId((Integer) aObj[6]);
					}
					if (aObj[7] == null) {
						aRxAddress.setRxAddressId(0);
					} else {
						aRxAddress.setRxAddressId((Integer) aObj[7]);
					}
				}

			}
			else
			{
				sQry = "SELECT rxmaster.Name,Address1,Address2,City,State,Zip,coTaxTerritoryID,address.rxAddressID FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = :theRxMasterID  and IsShipTo=1 ORDER BY address.rxAddressID limit 1";
				// theRxAddressID = getRxAddressID(theRxAddressID);
				aSession = itsSessionFactory.openSession();
				aQuery2 = aSession.createSQLQuery(sQry).setParameter(
						"theRxMasterID", theRxMasterID);
				aIterator = aQuery2.list().iterator();
				while (aIterator.hasNext()) {
					aRxAddress = new Rxaddress();
					Object[] aObj = (Object[]) aIterator.next();

					if (aObj[0] != null)
						aRxAddress.setName((String) aObj[0]);
					else
						aRxAddress.setName("");
					if (aObj[1] == null) {
						aRxAddress.setAddress1("");
					} else
						aRxAddress.setAddress1((String) aObj[1]);
					if (aObj[2] == null) {
						aRxAddress.setAddress2("");
					} else
						aRxAddress.setAddress2((String) aObj[2]);
					if (aObj[3] != null) {
						String aCity = (String) aObj[3];
						aRxAddress.setCity(aCity.trim());
					} else {
						aRxAddress.setCity("");
					}
					if (aObj[5] == null) {
						aRxAddress.setZip("");
					} else {
						aRxAddress.setZip((String) aObj[5]);
					}
					if (aObj[4] == null) {
						aRxAddress.setState("");
					} else {
						aRxAddress.setState((String) aObj[4]);
					}
					if (aObj[6] == null) {
						aRxAddress.setCoTaxTerritoryId(0);
					} else {
						aRxAddress.setCoTaxTerritoryId((Integer) aObj[6]);
					}
					if (aObj[7] == null) {
						aRxAddress.setRxAddressId(0);
					} else {
						aRxAddress.setRxAddressId((Integer) aObj[7]);
					}
				}

			
			}
		}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			sQry=null;
			aQuery2 = null;
			aIterator = null;
		}
		return aRxAddress;
	}
	@Override
	public Integer getShipToAddressID(Integer theRxMasterID)throws JobException {
		Session aSession = null;
		Query aQuery2 = null;
		Integer rxID = 0;
		String sQry = "SELECT address.rxAddressID FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID ="+theRxMasterID+"  and IsShipTo=1 ORDER BY address.rxAddressID limit 1";
		try {
		aSession = itsSessionFactory.openSession();
		rxID = (Integer) aSession.createSQLQuery(sQry).uniqueResult();
		
	} catch (Exception e) {
		itsLogger.error(e.getMessage(), e);
		JobException aJobException = new JobException(e.getMessage(), e);
		throw aJobException;
	} finally {
		aSession.flush();
		aSession.close();
		sQry=null;
		aQuery2 = null;
	}
	return rxID;
		
	}
	
	
	@Override
	public List<Rxaddress> getShipToAddressforToggle(Integer theRxMasterID)
			throws JobException {
		Session aSession = null;
		List<Rxaddress> aRxAddressList = new ArrayList<Rxaddress>();
		Rxaddress aRxaddress =null;
		String sQry = "SELECT rxmaster.Name,Address1,Address2,City,State,Zip,coTaxTerritoryID,address.rxAddressID FROM rxAddress address JOIN rxMaster rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = :theRxMasterID  and IsShipTo=1 ORDER BY address.rxAddressID";
		
		Query aQuery2 = null;
		Iterator<?> aIterator = null;
		try {
			if (theRxMasterID != null) {
				// theRxAddressID = getRxAddressID(theRxAddressID);
				aSession = itsSessionFactory.openSession();
				aQuery2 = aSession.createSQLQuery(sQry).setParameter(
						"theRxMasterID", theRxMasterID);
				aIterator = aQuery2.list().iterator();
				while (aIterator.hasNext()) {
					aRxaddress = new Rxaddress();
					Object[] aObj = (Object[]) aIterator.next();

					if (aObj[0] != null)
						aRxaddress.setName((String) aObj[0]);
					else
						aRxaddress.setName("");
					if (aObj[1] == null) {
						aRxaddress.setAddress1("");
					} else
						aRxaddress.setAddress1((String) aObj[1]);
					if (aObj[2] == null) {
						aRxaddress.setAddress2("");
					} else
						aRxaddress.setAddress2((String) aObj[2]);
					if (aObj[3] != null) {
						String aCity = (String) aObj[3];
						aRxaddress.setCity(aCity.trim());
					} else {
						aRxaddress.setCity("");
					}
					if (aObj[5] == null) {
						aRxaddress.setZip("");
					} else {
						aRxaddress.setZip((String) aObj[5]);
					}
					if (aObj[4] == null) {
						aRxaddress.setState("");
					} else {
						aRxaddress.setState((String) aObj[4]);
					}
					if (aObj[6] == null) {
						aRxaddress.setCoTaxTerritoryId(0);
					} else {
						aRxaddress.setCoTaxTerritoryId((Integer) aObj[6]);
					}
					if (aObj[7] == null) {
						aRxaddress.setRxAddressId(0);
					} else {
						aRxaddress.setRxAddressId((Integer) aObj[7]);
					}
					
					aRxAddressList.add(aRxaddress);
				}

			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			sQry=null;
			aQuery2 = null;
			aIterator = null;
		}
		return aRxAddressList;
	}


	public String updatePrMasterForSO(Cusodetail objCusodetail,
			BigDecimal oldQuantityOrdered, BigDecimal newQuantityOrdered,
			String operation, Cusodetail oldobjCusodetail) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			if ("edit".equalsIgnoreCase(operation)) {
				//if (objCusodetail.getPrMasterId().equals(oldobjCusodetail.getPrMasterId())) {
					Prmaster objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class, objCusodetail.getPrMasterId());
					Cusodetail beforequantitycusodetail=(Cusodetail)aVePOSession.get(Cusodetail.class, objCusodetail.getCuSodetailId());
					
//					BigDecimal oldAllocated = objPrmaster.getInventoryAllocated();
//					BigDecimal newValue = oldAllocated.subtract(oldQuantityOrdered);
					
					objPrmaster.setInventoryAllocated((objPrmaster.getInventoryAllocated()==null?BigDecimal.ZERO:objPrmaster.getInventoryAllocated()).subtract(
							beforequantitycusodetail.getQuantityOrdered()==null?BigDecimal.ZERO:beforequantitycusodetail.getQuantityOrdered()).add(
							objCusodetail.getQuantityOrdered()==null?BigDecimal.ZERO:objCusodetail.getQuantityOrdered()	
									));
					aVePOSession.update(objPrmaster);
					aTransaction.commit();

				/*} else {
					Prmaster objPrmaster = (Prmaster) aVePOSession.get(
							Prmaster.class, oldobjCusodetail.getPrMasterId());
					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated
							.subtract(oldobjCusodetail.getQuantityOrdered()));
					aVePOSession.update(objPrmaster);
					aTransaction.commit();
					aTransaction.begin();
					objPrmaster = (Prmaster) aVePOSession.get(Prmaster.class,
							objCusodetail.getPrMasterId());
					allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated
							.add(newQuantityOrdered));
					aVePOSession.update(objPrmaster);
					aTransaction.commit();
				}*/
				/*
				 * Prmaster objPrmaster = (Prmaster) aCuInvoiceSession.get(
				 * Prmaster.class, theCuinvoicedetailNew.getPrMasterId());
				 * BigDecimal order = objPrmaster.getInventoryOnHand();
				 */
				/*
				 * BigDecimal newValue = order.subtract(oldQuantityOrdered);
				 * objPrmaster.setInventoryOnHand(newValue
				 * .add(newQuantityOrdered));
				 */
				/*
				 * aCuInvoiceSession.update(objPrmaster); aTransaction.commit();
				 */
			}

			/*
			 * if ("edit".equalsIgnoreCase(operation)) { Prmaster objPrmaster =
			 * (Prmaster) aVePOSession.get( Prmaster.class,
			 * objCusodetail.getPrMasterId()); BigDecimal order =
			 * objPrmaster.getInventoryOnOrder(); BigDecimal newValue =
			 * order.subtract(oldQuantityOrdered); BigDecimal allocated =
			 * (objPrmaster.getInventoryOnHand()
			 * .subtract(objPrmaster.getInventoryOnOrder()));
			 * objPrmaster.setInventoryAllocated(allocated);
			 * objPrmaster.setInventoryOnOrder(newValue
			 * .add(newQuant0000ityOrdered)); aVePOSession.update(objPrmaster);
			 * aTransaction.commit(); }
			 */else if ("add".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, objCusodetail.getPrMasterId());
				BigDecimal allocated = objPrmaster.getInventoryAllocated();
				objPrmaster.setInventoryAllocated(allocated==null?new BigDecimal("0.0000").add(newQuantityOrdered):allocated
						.add(newQuantityOrdered));
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
			} else if ("del".equalsIgnoreCase(operation)||"delete".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aVePOSession.get(
						Prmaster.class, objCusodetail.getPrMasterId());
				if(objPrmaster!=null){
				BigDecimal allocated = objPrmaster.getInventoryAllocated();
				objPrmaster.setInventoryAllocated(allocated
						.subtract(objCusodetail.getQuantityOrdered()));
				aVePOSession.update(objPrmaster);
				aTransaction.commit();
				}
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}

	public String updatePrWarehouseInventoryForSO(Cusodetail objCusodetail,
			BigDecimal oldQuantityOrdered, BigDecimal newQuantityOrdered,
			String oper, Cusodetail oldobjCusodetail) throws JobException {
		Session aVePOSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			Cuso aCuso = (Cuso) aVePOSession.get(Cuso.class,
					objCusodetail.getCuSoid());
			Integer sPrWarehouseID = 0;
			BigDecimal inventoryAllocated = new BigDecimal(0);
			Integer prWarehouseInventoryID = 0;
			String sQuery = "SELECT prFromWarehouseID FROM cuSO cuso WHERE cuSOID = "
					+ aCuso.getCuSoid();
			Query query = aVePOSession.createSQLQuery(sQuery);
			if (query.list().size() > 0)
				sPrWarehouseID = (Integer) query.list().get(0);

			String sQuery1 = "SELECT inventory.InventoryAllocated,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
					+ objCusodetail.getPrMasterId()
					+ " AND prWarehouseID = "
					+ sPrWarehouseID;
			query = aVePOSession.createSQLQuery(sQuery1);
			/*
			 * Iterator<?> iterator = query.list().iterator();
			 * while(iterator.hasNext()) { Object[] object = (Object[])
			 * iterator.next(); onOrder.add((BigDecimal)object[0]); }
			 */
			if (query.list().size() > 0) {
				Object[] object = (Object[]) query.list().get(0);
				inventoryAllocated = (BigDecimal) object[0];
				prWarehouseInventoryID = (Integer) object[1];
			}
			BigDecimal allocated = new BigDecimal(0.00);
			Prwarehouseinventory objPrwarehouseinventory = (Prwarehouseinventory) aVePOSession
					.get(Prwarehouseinventory.class, prWarehouseInventoryID);
			if (prWarehouseInventoryID != null && prWarehouseInventoryID != 0) {
				allocated = objPrwarehouseinventory.getInventoryAllocated();
			}

			if ("del".equalsIgnoreCase(oper)) {
				if (objPrwarehouseinventory != null) {
					objPrwarehouseinventory.setInventoryAllocated(allocated
							.subtract(objCusodetail.getQuantityOrdered()));
					aVePOSession.update(objPrwarehouseinventory);
					aTransaction.commit();
				}
			} else if ("add".equalsIgnoreCase(oper)) {
				if (objPrwarehouseinventory != null) {
					objPrwarehouseinventory.setInventoryAllocated(allocated==null?new BigDecimal("0.0000").add(objCusodetail.getQuantityOrdered()):allocated
							.add(objCusodetail.getQuantityOrdered()));

					aVePOSession.update(objPrwarehouseinventory);
					aTransaction.commit();
				}
			} else if ("edit".equalsIgnoreCase(oper)) {
				if (oldobjCusodetail.getPrMasterId().equals(
						objCusodetail.getPrMasterId())) {

					BigDecimal newAllocated = allocated
							.subtract(oldobjCusodetail.getQuantityOrdered());
					BigDecimal newOnAllocated = newAllocated.add(objCusodetail
							.getQuantityOrdered());
					if (objPrwarehouseinventory != null) {
						objPrwarehouseinventory
								.setInventoryAllocated(newOnAllocated);
						aVePOSession.update(objPrwarehouseinventory);
						aTransaction.commit();
					}

				} else {

					BigDecimal newAllocated = allocated
							.subtract(oldobjCusodetail.getQuantityOrdered());
					if (objPrwarehouseinventory != null) {
						objPrwarehouseinventory
								.setInventoryAllocated(newAllocated);
						aVePOSession.update(objPrwarehouseinventory);
						aTransaction.commit();

					}
					aTransaction.begin();
					String sQuery2 = "SELECT inventory.InventoryAllocated,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
							+ objCusodetail.getPrMasterId()
							+ " AND prWarehouseID = " + sPrWarehouseID;
					Query query1 = aVePOSession.createSQLQuery(sQuery2);

					if (query1.list().size() > 0) {
						Object[] object = (Object[]) query1.list().get(0);
						inventoryAllocated = (BigDecimal) object[0];
						prWarehouseInventoryID = (Integer) object[1];
					}
					Prwarehouseinventory objPrwarehouseinventory1 = (Prwarehouseinventory) aVePOSession
							.get(Prwarehouseinventory.class,
									prWarehouseInventoryID);
					allocated = objPrwarehouseinventory1
							.getInventoryAllocated();

					objPrwarehouseinventory1.setInventoryAllocated(allocated
							.add(objCusodetail.getQuantityOrdered()));
					aVePOSession.update(objPrwarehouseinventory1);
					aTransaction.commit();
				}

			}

			/*
			 * else if ("edit".equalsIgnoreCase(oper)) { BigDecimal order =
			 * objPrwarehouseinventory.getInventoryAllocated(); BigDecimal
			 * newValue = order.subtract(oldQuantityOrdered); BigDecimal
			 * allocatedNew = newValue.add(newQuantityOrdered);
			 * objPrwarehouseinventory.setInventoryAllocated(allocatedNew);
			 * 
			 * }
			 */
			/*
			 * aVePOSession.update(objPrwarehouseinventory);
			 * aTransaction.commit();
			 */
			sQuery=null;
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}

	@Override
	public Cuso getCUSOobjFromSONumber(String theSONumber) throws JobException {
		Session aSession = null;
		String sQry = "FROM Cuso where sonumber = :theSONumber";
		List<Cuso> aQueryList = new ArrayList<Cuso>();
		Cuso aCuso = null;
		try {
			if (theSONumber != null) {
				// theRxAddressID = getRxAddressID(theRxAddressID);
				aSession = itsSessionFactory.openSession();
				Query aQuery2 = aSession.createQuery(sQry).setParameter("theSONumber", theSONumber);
				aQueryList = (List<Cuso>) aQuery2.list();
				
				if (aQueryList.size() > 0)
				{
					Iterator it = aQueryList.iterator();
					while(it.hasNext())
					{
					   Object obj = it.next();
					   aCuso = (Cuso)obj;
					   
					}
					
					if(aCuso.getJoReleaseId()==null || aCuso.getJoReleaseId()==0)
					return (Cuso) aQueryList.get(0);
					else
					return new Cuso();
				}
				else
				{
					return new Cuso();
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			sQry=null;
		}
		return new Cuso();
	}

	@Override
	public List<?> getcustomerInvoicelineitem(Integer theCusoId)
			throws JobException {

		String aSOReleaseLineItemQry = "SELECT SO.cuSODetailID,"
				+ " SO.cuSOID," + " SO.prMasterID," + " SO.Description,"
				+ " SO.QuantityOrdered," + " SO.Taxable," + " SO.UnitCost,"
				+ " SO.PriceMultiplier," + " pr.ItemCode, "
				+ " cuso.TaxTotal, " + " SO.Note " + " FROM cuSODetail SO "
				+ " Left Join prMaster pr on SO.prMasterID = pr.prMasterID"
				+ " Right Join cuSO cuso on cuso.cuSOID = SO.cuSOID"
				+ " where SO.cuSOID = " + theCusoId + " ;";
		Session aSession = null;
		ArrayList<Cusodetail> aQueryList = new ArrayList<Cusodetail>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Cusodetail aCuSOdetails = new Cusodetail();
				Object[] aObj = (Object[]) aIterator.next();
				aCuSOdetails.setCuSodetailId((Integer) aObj[0]);
				aCuSOdetails.setCuSoid((Integer) aObj[1]);
				aCuSOdetails.setPrMasterId((Integer) aObj[2]);
				aCuSOdetails.setDescription((String) aObj[3]);
				if (aObj[4] != null)
					aCuSOdetails.setQuantityBilled((BigDecimal) aObj[4]);
				aCuSOdetails.setTaxable((Byte) aObj[5]);
				aCuSOdetails.setUnitCost((BigDecimal) aObj[6]);
				aCuSOdetails.setPriceMultiplier((BigDecimal) aObj[7]);
				aCuSOdetails.setItemCode((String) aObj[8]);
				aCuSOdetails.setTaxTotal((BigDecimal) aObj[9]);
				aCuSOdetails.setNote("Test grid");
				aQueryList.add(aCuSOdetails);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public Integer insertRxAddress(Rxaddress theRxaddress, String oper)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		if (theRxaddress.getRxAddressId() != null) {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Rxaddress objrxaddress = (Rxaddress) aSession.get(Rxaddress.class,theRxaddress.getRxAddressId());
			objrxaddress.setName(theRxaddress.getName());
			objrxaddress.setAddress1(theRxaddress.getAddress1());
			objrxaddress.setAddress2(theRxaddress.getAddress2());
			objrxaddress.setCity(theRxaddress.getCity());
			objrxaddress.setState(theRxaddress.getState());
			objrxaddress.setZip(theRxaddress.getZip());
			objrxaddress.setIsBillTo(false);
			objrxaddress.setInActive(false);
			objrxaddress.setIsMailing(false);
			objrxaddress.setIsStreet(false);
			objrxaddress.setIsShipTo(false);
			
			aSession.update(objrxaddress);
			aTransaction.commit();
			return theRxaddress.getRxAddressId();

		} else {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Integer rxOtherID = (Integer)aSession.save(theRxaddress);
			aTransaction.commit();
			return rxOtherID;
		}
	}

	public String updatePrMasterForCuInvoice(
			Cuinvoicedetail theCuinvoicedetailold,
			Cuinvoicedetail theCuinvoicedetailNew, String operation)
			throws JobException {
		Session aCuInvoiceSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuInvoiceSession.beginTransaction();
			aTransaction.begin();
			if ("edit".equalsIgnoreCase(operation)) {
				if (theCuinvoicedetailold.getPrMasterId().equals(
						theCuinvoicedetailNew.getPrMasterId())) {
					Prmaster objPrmaster = (Prmaster) aCuInvoiceSession.get(
							Prmaster.class,
							theCuinvoicedetailold.getPrMasterId());
					BigDecimal order = objPrmaster.getInventoryOnHand();
					BigDecimal newValue = order.add(theCuinvoicedetailold
							.getQuantityBilled());
					objPrmaster
							.setInventoryOnHand(newValue
									.subtract(theCuinvoicedetailNew
											.getQuantityBilled()));

					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					BigDecimal newallocatedValue = allocated
							.add(theCuinvoicedetailold.getQuantityBilled());
					objPrmaster
							.setInventoryAllocated(newallocatedValue
									.subtract(theCuinvoicedetailNew
											.getQuantityBilled()));
					aCuInvoiceSession.update(objPrmaster);
					aTransaction.commit();
				} else {
					Prmaster objPrmaster = (Prmaster) aCuInvoiceSession.get(
							Prmaster.class,
							theCuinvoicedetailold.getPrMasterId());
					BigDecimal order = objPrmaster.getInventoryOnHand();
					BigDecimal newValue = order.add(theCuinvoicedetailold
							.getQuantityBilled());
					objPrmaster.setInventoryOnHand(newValue);
					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					BigDecimal newallocatedValue = allocated
							.add(theCuinvoicedetailold.getQuantityBilled());
					objPrmaster.setInventoryAllocated(newallocatedValue);
					aCuInvoiceSession.update(objPrmaster);
					aTransaction.commit();
					aTransaction.begin();
					objPrmaster = (Prmaster) aCuInvoiceSession.get(
							Prmaster.class,
							theCuinvoicedetailNew.getPrMasterId());
					order = objPrmaster.getInventoryOnHand();
					newValue = order.subtract(theCuinvoicedetailNew
							.getQuantityBilled());
					objPrmaster.setInventoryOnHand(newValue);
					allocated = objPrmaster.getInventoryAllocated();
					newallocatedValue = allocated
							.subtract(theCuinvoicedetailNew.getQuantityBilled());
					objPrmaster.setInventoryAllocated(newallocatedValue);
					aCuInvoiceSession.update(objPrmaster);
					aTransaction.commit();
				}
				/*
				 * Prmaster objPrmaster = (Prmaster) aCuInvoiceSession.get(
				 * Prmaster.class, theCuinvoicedetailNew.getPrMasterId());
				 * BigDecimal order = objPrmaster.getInventoryOnHand();
				 */
				/*
				 * BigDecimal newValue = order.subtract(oldQuantityOrdered);
				 * objPrmaster.setInventoryOnHand(newValue
				 * .add(newQuantityOrdered));
				 */
				/*
				 * aCuInvoiceSession.update(objPrmaster); aTransaction.commit();
				 */
			} else if ("add".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aCuInvoiceSession.get(
						Prmaster.class, theCuinvoicedetailNew.getPrMasterId());
				BigDecimal order = objPrmaster.getInventoryOnHand();
				
				
				objPrmaster.setInventoryOnHand(order
						.subtract(theCuinvoicedetailNew.getQuantityBilled()));

				BigDecimal allocated = objPrmaster.getInventoryAllocated();
				objPrmaster.setInventoryAllocated(allocated
						.subtract(theCuinvoicedetailNew.getQuantityBilled()));
				aCuInvoiceSession.update(objPrmaster);
				aTransaction.commit();
			} else if ("del".equalsIgnoreCase(operation)||"delete".equalsIgnoreCase(operation)) {
				Prmaster objPrmaster = (Prmaster) aCuInvoiceSession.get(
						Prmaster.class, theCuinvoicedetailNew.getPrMasterId());
				BigDecimal order = objPrmaster.getInventoryOnHand();
				objPrmaster.setInventoryOnHand(order.add(theCuinvoicedetailNew
						.getQuantityBilled()));

				BigDecimal allocated = objPrmaster.getInventoryAllocated();
				objPrmaster.setInventoryAllocated(allocated
						.add(theCuinvoicedetailNew.getQuantityBilled()));
				aCuInvoiceSession.update(objPrmaster);
				aTransaction.commit();
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
		}
		return "Success";
	}

	public String updatePrWarehouseInventoryForCuInvoice(
			Cuinvoicedetail theCuinvoicedetailold,
			Cuinvoicedetail theCuinvoicedetailNew, String operation)
			throws JobException {
		Session aCuInvoiceSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuInvoiceSession.beginTransaction();
			aTransaction.begin();
			Cuinvoice aCuinvoice = (Cuinvoice) aCuInvoiceSession.get(
					Cuinvoice.class, theCuinvoicedetailNew.getCuInvoiceId());
			Integer sPrWarehouseID = 0;
			BigDecimal inventoryAllocated = new BigDecimal(0);
			BigDecimal inventoryOnHand = new BigDecimal(0);
			Integer prWarehouseInventoryID = 0;
			String sQuery = "SELECT prFromWarehouseID FROM cuInvoice cuinvoice WHERE cuInvoiceID = "
					+ aCuinvoice.getCuInvoiceId();
			Query query = aCuInvoiceSession.createSQLQuery(sQuery);
			if (query.list().size() > 0)
				sPrWarehouseID = (Integer) query.list().get(0);

			String sQuery1 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
					+ theCuinvoicedetailNew.getPrMasterId()
					+ " AND prWarehouseID = " + sPrWarehouseID;
			query = aCuInvoiceSession.createSQLQuery(sQuery1);

			if (query.list().size() > 0) {
				Object[] object = (Object[]) query.list().get(0);
				inventoryAllocated = (BigDecimal) object[0];
				inventoryOnHand = (BigDecimal) object[1];
				prWarehouseInventoryID = (Integer) object[2];
			}
			Prwarehouseinventory objPrwarehouseinventory = null;
			BigDecimal allocated = new BigDecimal(0.0);
			BigDecimal onHand = new BigDecimal(0.0);
			if (null != prWarehouseInventoryID) {
				objPrwarehouseinventory = (Prwarehouseinventory) aCuInvoiceSession
						.get(Prwarehouseinventory.class, prWarehouseInventoryID);
				if (null != objPrwarehouseinventory) {
					allocated = objPrwarehouseinventory.getInventoryAllocated();
					onHand = objPrwarehouseinventory.getInventoryOnHand();
					if ("edit".equalsIgnoreCase(operation)) {
						if (theCuinvoicedetailold.getPrMasterId().equals(
								theCuinvoicedetailNew.getPrMasterId())) {

							BigDecimal newAllocated = allocated
									.add(theCuinvoicedetailold
											.getQuantityBilled());
							BigDecimal newOnHand = onHand
									.add(theCuinvoicedetailold
											.getQuantityBilled());
							if(theCuinvoicedetailNew!=null && theCuinvoicedetailNew.getCuSodetailId()!=null){
							objPrwarehouseinventory
									.setInventoryAllocated(newAllocated
											.subtract(theCuinvoicedetailNew
													.getQuantityBilled()));
							}
							objPrwarehouseinventory
									.setInventoryOnHand(newOnHand
											.subtract(theCuinvoicedetailNew
													.getQuantityBilled()));
							aCuInvoiceSession.update(objPrwarehouseinventory);
							aTransaction.commit();

						} else {

							BigDecimal newAllocated = allocated
									.add(theCuinvoicedetailold
											.getQuantityBilled());
							BigDecimal newOnHand = onHand
									.add(theCuinvoicedetailold
											.getQuantityBilled());
							if(theCuinvoicedetailNew!=null && theCuinvoicedetailNew.getCuSodetailId()!=null){
							objPrwarehouseinventory
									.setInventoryAllocated(newAllocated);
							}
							objPrwarehouseinventory
									.setInventoryOnHand(newOnHand);
							aCuInvoiceSession.update(objPrwarehouseinventory);
							aTransaction.commit();

							aTransaction.begin();
							String sQuery2 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
									+ theCuinvoicedetailNew.getPrMasterId()
									+ " AND prWarehouseID = " + sPrWarehouseID;
							Query query1 = aCuInvoiceSession
									.createSQLQuery(sQuery2);

							if (query1.list().size() > 0) {
								Object[] object = (Object[]) query1.list().get(
										0);
								inventoryAllocated = (BigDecimal) object[0];
								inventoryOnHand = (BigDecimal) object[1];
								prWarehouseInventoryID = (Integer) object[2];
							}
							Prwarehouseinventory objPrwarehouseinventory1 = null;
							if (null != prWarehouseInventoryID) {
								objPrwarehouseinventory1 = (Prwarehouseinventory) aCuInvoiceSession
										.get(Prwarehouseinventory.class,
												prWarehouseInventoryID);
								allocated = objPrwarehouseinventory1
										.getInventoryAllocated();
								onHand = objPrwarehouseinventory1
										.getInventoryOnHand();
							} else {
								allocated = new BigDecimal(0.0);
								onHand = new BigDecimal(0.0);
							}

							objPrwarehouseinventory1.setInventoryOnHand(onHand
									.subtract(theCuinvoicedetailNew
											.getQuantityBilled()));
							if(theCuinvoicedetailNew!=null && theCuinvoicedetailNew.getCuSodetailId()!=null){
							objPrwarehouseinventory1
									.setInventoryAllocated(allocated
											.subtract(theCuinvoicedetailNew
													.getQuantityBilled()));
							}
							aCuInvoiceSession.update(objPrwarehouseinventory1);
							aTransaction.commit();
						}

					} else if ("add".equalsIgnoreCase(operation)) {
						onHand =onHand==null?new BigDecimal("0.0000"):onHand;
						allocated= allocated==null?new BigDecimal("0.0000"):allocated;
						objPrwarehouseinventory.setInventoryOnHand(onHand
								.subtract(theCuinvoicedetailNew
										.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
												.getQuantityBilled()));
						if(theCuinvoicedetailNew!=null && theCuinvoicedetailNew.getCuSodetailId()!=null){
						objPrwarehouseinventory.setInventoryAllocated(allocated
								.subtract(theCuinvoicedetailNew
										.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
												.getQuantityBilled()));
						}
						aCuInvoiceSession.update(objPrwarehouseinventory);
						aTransaction.commit();

					} else if ("del".equalsIgnoreCase(operation)||"delete".equalsIgnoreCase(operation)) {
						objPrwarehouseinventory
								.setInventoryOnHand(onHand
										.add(theCuinvoicedetailNew
												.getQuantityBilled()));
						if(theCuinvoicedetailNew!=null && theCuinvoicedetailNew.getCuSodetailId()!=null){
						objPrwarehouseinventory
								.setInventoryAllocated(allocated
										.add(theCuinvoicedetailNew
												.getQuantityBilled()));
						}
						aCuInvoiceSession.update(objPrwarehouseinventory);
						aTransaction.commit();
					}
				}

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
		}
		return "Success";
	}

	public String updatePrWarehouseInventoryForCuInvoiceWithoutSO(
			Cuinvoicedetail theCuinvoicedetailold,
			Cuinvoicedetail theCuinvoicedetailNew, String operation)
			throws JobException {
		Session aCuInvoiceSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCuInvoiceSession.beginTransaction();
			aTransaction.begin();
			Cuinvoice aCuinvoice = (Cuinvoice) aCuInvoiceSession.get(
					Cuinvoice.class, theCuinvoicedetailNew.getCuInvoiceId());
			Integer sPrWarehouseID = 0;
			BigDecimal inventoryOnHand = new BigDecimal(0);
			Integer prWarehouseInventoryID = 0;
			String sQuery = "SELECT prFromWarehouseID FROM cuInvoice cuinvoice WHERE cuInvoiceID = "
					+ aCuinvoice.getCuInvoiceId();
			Query query = aCuInvoiceSession.createSQLQuery(sQuery);
			if (query.list().size() > 0)
				sPrWarehouseID = (Integer) query.list().get(0);

			String sQuery1 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
					+ theCuinvoicedetailNew.getPrMasterId()
					+ " AND prWarehouseID = " + sPrWarehouseID;
			query = aCuInvoiceSession.createSQLQuery(sQuery1);

			if (query.list().size() > 0) {
				Object[] object = (Object[]) query.list().get(0);
				inventoryOnHand = (BigDecimal) object[1];
				prWarehouseInventoryID = (Integer) object[2];
			}
			Prwarehouseinventory objPrwarehouseinventory = null;
			BigDecimal onHand = new BigDecimal(0.0);
			if (null != prWarehouseInventoryID) {
				objPrwarehouseinventory = (Prwarehouseinventory) aCuInvoiceSession
						.get(Prwarehouseinventory.class, prWarehouseInventoryID);
				if (null != objPrwarehouseinventory) {
					onHand = objPrwarehouseinventory.getInventoryOnHand();
					if ("edit".equalsIgnoreCase(operation)) {
						if (theCuinvoicedetailold.getPrMasterId().equals(
								theCuinvoicedetailNew.getPrMasterId())) {

							BigDecimal newOnHand = onHand
									.add(theCuinvoicedetailold
											.getQuantityBilled());
							objPrwarehouseinventory
									.setInventoryOnHand(newOnHand
											.subtract(theCuinvoicedetailNew
													.getQuantityBilled()));
							aCuInvoiceSession.update(objPrwarehouseinventory);
							aTransaction.commit();

						} else {

							BigDecimal newOnHand = onHand
									.add(theCuinvoicedetailold
											.getQuantityBilled());
							objPrwarehouseinventory
									.setInventoryOnHand(newOnHand);
							aCuInvoiceSession.update(objPrwarehouseinventory);
							aTransaction.commit();

							aTransaction.begin();
							String sQuery2 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
									+ theCuinvoicedetailNew.getPrMasterId()
									+ " AND prWarehouseID = " + sPrWarehouseID;
							Query query1 = aCuInvoiceSession
									.createSQLQuery(sQuery2);

							if (query1.list().size() > 0) {
								Object[] object = (Object[]) query1.list().get(
										0);
								inventoryOnHand = (BigDecimal) object[1];
								prWarehouseInventoryID = (Integer) object[2];
							}
							Prwarehouseinventory objPrwarehouseinventory1 = null;
							if (null != prWarehouseInventoryID) {
								objPrwarehouseinventory1 = (Prwarehouseinventory) aCuInvoiceSession
										.get(Prwarehouseinventory.class,
												prWarehouseInventoryID);
								onHand = objPrwarehouseinventory1
										.getInventoryOnHand();
							} else {
								onHand = new BigDecimal(0.0);
							}

							objPrwarehouseinventory1.setInventoryOnHand(onHand
									.subtract(theCuinvoicedetailNew
											.getQuantityBilled()));
							aCuInvoiceSession.update(objPrwarehouseinventory1);
							aTransaction.commit();
						}

					} else if ("add".equalsIgnoreCase(operation)) {
						onHand =onHand==null?new BigDecimal("0.0000"):onHand;
						objPrwarehouseinventory.setInventoryOnHand(onHand
								.subtract(theCuinvoicedetailNew
										.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
												.getQuantityBilled()));
						aCuInvoiceSession.update(objPrwarehouseinventory);
						aTransaction.commit();

					} else if ("del".equalsIgnoreCase(operation)||"delete".equalsIgnoreCase(operation)) {
						objPrwarehouseinventory
								.setInventoryOnHand(onHand
										.add(theCuinvoicedetailNew
												.getQuantityBilled()));
						aCuInvoiceSession.update(objPrwarehouseinventory);
						aTransaction.commit();
					}
				}

			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getCause()
					.getMessage(), e);
			throw aJobException;
		} finally {
			aCuInvoiceSession.flush();
			aCuInvoiceSession.close();
		}
		return "Success";
	}
	
	public BigDecimal getTotal(BigDecimal aQuantity, BigDecimal aReceived) {
		if (aQuantity != null && (aQuantity.compareTo(BigDecimal.ZERO) != 0)) {
			return aQuantity;
		} else {
			return aReceived!=null?aReceived:new BigDecimal("0");
		}
	}
	
	
	public String updatePrMasterFirst(Integer cuInvoiceID,String oper) {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String sQry = "SELECT prMasterID,QuantityBilled FROM cuInvoiceDetail WHERE cuInvoiceID = "
				+ cuInvoiceID + " ORDER BY cuInvoiceDetailID";
		Query aQuery = aSession.createSQLQuery(sQry);
		Iterator<?> aIterator = aQuery.list().iterator();
		Integer prMasterID = null;
		BigDecimal QtyBilled = new BigDecimal(0);
		while (aIterator.hasNext()) {
			Object[] object = (Object[]) aIterator.next();

			aTransaction = aSession.beginTransaction();
			prMasterID = (Integer) object[0];
			if (prMasterID != null && prMasterID != 0) {
				QtyBilled = (BigDecimal) object[1];
				
				Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class,
						prMasterID);
				
				BigDecimal order = new BigDecimal(0);
				
				if(objPrmaster.getInventoryOnHand()!=null)
				order = objPrmaster.getInventoryOnHand();
				order = order==null?new BigDecimal("0.0000"):order;
				QtyBilled = QtyBilled==null?new BigDecimal("0.0000"):QtyBilled;
				objPrmaster.setInventoryOnHand(order.subtract(QtyBilled));

				BigDecimal allocated = new BigDecimal(0);
				if(objPrmaster.getInventoryAllocated()!=null)
				allocated = objPrmaster.getInventoryAllocated();
				
				objPrmaster.setInventoryAllocated(allocated.subtract(QtyBilled));
				aSession.update(objPrmaster);
				aTransaction.commit();
			}
		}
		sQry=null;
		return "sucess";
	}

	public String updatePrWarehouseInventoryForCuInvoiceFirst(
			Integer cuInvoiceID) {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction=null;
		String sQry = "SELECT cuInvoiceDetailID,cuInvoiceID,cuSODetailID FROM cuInvoiceDetail WHERE cuInvoiceID = "
				+ cuInvoiceID + " ORDER BY cuInvoiceDetailID";
		Query aQuery = aSession.createSQLQuery(sQry);
		Iterator<?> aIterator = aQuery.list().iterator();
		Integer cuInvoiceDetailID = null;
		BigDecimal QtyBilled = new BigDecimal(0);

		// updatePrWarehouseInventoryForCuInvoice(null, theCuInvDetail, "add");
		while (aIterator.hasNext()) {
			Object[] object = (Object[]) aIterator.next();

			aTransaction = aSession.beginTransaction();
			cuInvoiceDetailID = (Integer) object[0];
			Cuinvoicedetail objCuinvoicedetailr = (Cuinvoicedetail) aSession
					.get(Cuinvoicedetail.class, cuInvoiceDetailID);
			try {
				objCuinvoicedetailr.setCuInvoiceId(cuInvoiceID);
				updatePrWarehouseInventoryForCuInvoice(null,
						objCuinvoicedetailr, "add");
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * if(prMasterID != null && prMasterID != 0) { QtyBilled =
			 * (BigDecimal)object[1];
			 * System.out.println("PrMaster--->"+prMasterID
			 * +"--QtyBilled--->"+QtyBilled); Prmaster objPrmaster = (Prmaster)
			 * aSession.get( Prmaster.class, prMasterID); BigDecimal order =
			 * objPrmaster.getInventoryOnHand();
			 * objPrmaster.setInventoryOnHand(order .subtract(QtyBilled));
			 * 
			 * BigDecimal allocated = objPrmaster.getInventoryAllocated();
			 * objPrmaster.setInventoryAllocated(allocated
			 * .subtract(QtyBilled)); aSession.update(objPrmaster);
			 * aTransaction.commit(); }
			 */
		}
		sQry=null;
		return "sucess";
	}

	@Override
	public Integer getWareHouseTransactionNo() throws JobException {
		Session aSession = null;
		Integer maxTransferId = 0;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Criteria criteria = aSession.createCriteria(
					PrwarehouseTransfer.class).setProjection(
					Projections.max("transactionNumber"));
			maxTransferId = (Integer) criteria.uniqueResult();
			if (null == maxTransferId) {
				maxTransferId = 1;
			} else {
				maxTransferId += 1;
			}

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Freight Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return maxTransferId;
	}

	@Override
	public PrwarehouseTransfer getWareHouseTransfer(Integer prTransferId)
			throws JobException {
		Session aSession = null;
		PrwarehouseTransfer objPrwarehouseTransfer = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			objPrwarehouseTransfer = (PrwarehouseTransfer) aSession.get(
					PrwarehouseTransfer.class, prTransferId);

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting the Freight Charges in Purchase: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return objPrwarehouseTransfer;
	}

	public Integer getRecordsCount() throws JobException {
		String aJobCountStr = "SELECT COUNT(vePOID) AS count FROM vePO";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		aJobCountStr=null;
		return aTotalCount.intValue();
	}

	@Override
	public ArrayList<AutoCompleteBean> getEmployeewithNameList(
			String theProductName) throws JobException {
		// SELECT NAME FROM rxmaster WHERE InActive=1
		// String salesselectQry =
		// "SELECT rxMasterID,CONCAT(FirstName,' ',Name) AS NAME FROM rxMaster WHERE InActive=0 AND IsEmployee=1 AND (FirstName LIKE '%"+theProductName+"%' OR NAME LIKE '%"+theProductName+"%') ORDER BY rxMasterID ASC";
		String salesselectQry = "SELECT emMaster.emMasterID ,rxMaster.Name FROM emMaster ,rxMaster,tsUserLogin WHERE "
				+ "emMaster.GetsCommission=1 AND emMaster.emMasterID = rxMaster.rxMasterID AND emMaster.UserLoginID = tsUserLogin.UserLoginID "
				+ "AND rxMaster.InActive = 0 AND tsUserLogin.InActive =0 AND rxMaster.Name LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theProductName) + "%' ORDER BY emMasterID";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				/*
				 * if (((String) aObj[2]) == null) { aUserbean.setLabel((String)
				 * aObj[1]); } else { String aProduct = (String) aObj[1] + " -["
				 * + (String) aObj[2] + "]"; aUserbean.setLabel(aProduct); }
				 */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean addSplitCommission(Ecsplitjob ecsplitjob)
			throws JobException {
		itsLogger.info("split commission in jobservice");
		Session aJoscheduledetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoscheduledetailSession.beginTransaction();
			aTransaction.begin();
			aJoscheduledetailSession.save(ecsplitjob);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoscheduledetailSession.flush();
			aJoscheduledetailSession.close();
		}
		return false;
	}

	@Override
	public List<?> getjobCommissionListGrid(Integer JoMasterId)
			throws JobException {

		String aSOReleaseLineItemQry = "SELECT ecs.rxMasterID,rxm.Name,ecs.Allocated,ecs.ecSplitCodeID,"
				+ "IF(ecs.ecSplitCodeID IS NULL,ecs.splitType, "
				+ "(SELECT CodeName FROM ecSplitCode WHERE ecSplitCodeID =ecs.ecSplitCodeID )) AS Splittype, "
				+ "ecs.ecSplitJobID FROM ecSplitJob ecs JOIN emMaster emm ON emm.emMasterID =ecs.rxMasterID "
				+ "JOIN rxMaster rxm ON emm.emMasterID =rxm.rxMasterID  WHERE rxm.IsEmployee=1 AND "
				+ "ecs.joReleaseID IS NULL AND ecs.joMasterID="	+ JoMasterId;
		Session aSession = null;
		ArrayList<Ecsplitjob> aQueryList = new ArrayList<Ecsplitjob>();
		try {
			itsLogger.info("getjobCommissionListGrid()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Ecsplitjob ecsplitjob = new Ecsplitjob();

				Object[] aObj = (Object[]) aIterator.next();
				if (aObj[0] != null)
					ecsplitjob.setRxMasterId((Integer) aObj[0]);
				ecsplitjob.setRep((String) aObj[1]);
				if (aObj[2] != null)
					ecsplitjob.setAllocated((BigDecimal) aObj[2]);

				if (aObj[3] != null)
					ecsplitjob.setEcSplitCodeID((Integer) aObj[3]);
				ecsplitjob.setSplittype((String) aObj[4]);
				ecsplitjob.setEcSplitJobId((Integer) aObj[5]);

				aQueryList.add(ecsplitjob);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getjobCommissionListGrid()=[Connection Closed]");
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public boolean deleteSplitCommission(Integer ecSplitJobId)
			throws JobException {
		Session ecsplitjobSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Ecsplitjob ecsplitjob = null;
		try {
			if(ecSplitJobId!=null && ecSplitJobId>0){
				aTransaction = ecsplitjobSession.beginTransaction();
				aTransaction.begin();
				ecsplitjob = (Ecsplitjob) ecsplitjobSession.get(Ecsplitjob.class,
						ecSplitJobId);
				ecsplitjobSession.delete(ecsplitjob);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			ecsplitjobSession.flush();
			ecsplitjobSession.close();
		}
		return false;
	}

	@Override
	public boolean getsplitcommission_chkbox_status(Integer theJoMasterId)
			throws JobException {
		String aSOReleaseLineItemQry = "SELECT *  FROM ecSplitJob WHERE joReleaseID is NULL AND joMasterID= "
				+ theJoMasterId;
		Session aSession = null;
		boolean returnvalue = false;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				returnvalue = true;
			}
		} catch (Exception e) {
			itsLogger.error(
					"Exception while getting getSingleJobDetails(Integer theJoMasterId) : "
							+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSOReleaseLineItemQry=null;
		}

		return returnvalue;
	}

	@Override
	public List<?> getBidStatusList() throws JobException {
		Session aSession = null;
		List<Jobidstatus> aQueryList = new ArrayList<Jobidstatus>();
		String aCustomerQry = "SELECT * FROM joBidStatus order by Description ASC ";
		/** LIMIT " + theFrom + ", " + theTo; */
		Jobidstatus aJobBidStatus = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {

				aJobBidStatus = new Jobidstatus();
				Object[] aObj = (Object[]) aIterator.next();
				aJobBidStatus.setJoBidStatusId((Integer) aObj[0]);
				aJobBidStatus.setDescription((String) aObj[2]);
				Byte inActive = ((Byte) aObj[3]);
				aJobBidStatus.setInActive(false);
				if (inActive == 1) {
					aJobBidStatus.setInActive(true);
				}
				aJobBidStatus.setCode((String) aObj[1]);

				// boolean inactive = (Boolean) aObj[2];
				// aJobBidStatus.setInActive((byte) aObj[2]);
				aQueryList.add(aJobBidStatus);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;
	}
	
	

	@Override
	public Jobidstatus updateJoBidStatusService(Jobidstatus theRxcontact,
			Boolean isAdd) throws JobException {
		itsLogger.debug("Update Email Addr in Jobidstatus.");
		Session aSession = itsSessionFactory.openSession();
		Jobidstatus aRxcontact = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (!isAdd) {
				aRxcontact = (Jobidstatus) aSession.get(Jobidstatus.class,
						theRxcontact.getJoBidStatusId());
				aRxcontact.setInActive(theRxcontact.getInActive());
				aRxcontact.setDescription(theRxcontact.getDescription());
				aRxcontact.setCode(theRxcontact.getCode());
				aRxcontact.setJoBidStatusId(theRxcontact.getJoBidStatusId());
				aSession.update(aRxcontact);
			} else {
				aRxcontact = new Jobidstatus();
				aRxcontact.setInActive(theRxcontact.getInActive());
				aRxcontact.setDescription(theRxcontact.getDescription());
				aRxcontact.setCode(theRxcontact.getCode());
				aSession.save(aRxcontact);
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aCompanyException = new JobException(e.getCause()
					.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxcontact;
	}

	@Override
	public void deleteJoBidStatusService(Integer theBidstatusId) {
		itsLogger.debug("delete jo bid status ");
		Session aSession = itsSessionFactory.openSession();
		Jobidstatus aRxcontact = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aRxcontact = (Jobidstatus) aSession.get(Jobidstatus.class,
					theBidstatusId);
			aSession.delete(aRxcontact);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aCompanyException = new JobException(e.getCause()
					.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	

	@Override
	public List<Jobidstatus> getBidStatus() {
		Session aSession = null;
		List<Jobidstatus> aQueryList = new ArrayList<Jobidstatus>();
		String aCustomerQry = " ";

		aCustomerQry = "SELECT * FROM joBidStatus";

		Jobidstatus aRolodexBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new Jobidstatus();
				Object[] aObj = (Object[]) aIterator.next();
				aRolodexBean.setJoBidStatusId((Integer) aObj[0]);
				aRolodexBean.setDescription((String) aObj[1]);
				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;
	}

	public Ecsplitcode updateEmpCommissionsplitypeService(
			Ecsplitcode ecsplitcode, Boolean isAdd) throws JobException {
		itsLogger.debug("Update Email Addr in Jobidstatus.");
		Session aSession = itsSessionFactory.openSession();
		Ecsplitcode ecsplitcodebean = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (!isAdd) {
				ecsplitcodebean = (Ecsplitcode) aSession.get(Ecsplitcode.class,
						ecsplitcode.getEcSplitCodeID());
				ecsplitcodebean.setEcSplitCodeID(ecsplitcodebean
						.getEcSplitCodeID());
				ecsplitcodebean.setCodeName(ecsplitcode.getCodeName());
				ecsplitcodebean.setDefaultPct(ecsplitcode.getDefaultPct());
				aSession.update(ecsplitcodebean);
			} else {
				ecsplitcodebean = new Ecsplitcode();
				ecsplitcodebean.setCodeName(ecsplitcode.getCodeName());
				ecsplitcodebean.setDefaultPct(ecsplitcode.getDefaultPct());
				aSession.save(ecsplitcodebean);
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aCompanyException = new JobException(e.getCause()
					.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return ecsplitcodebean;
	}

	@Override
	public List<?> getempSplitTypeListGrid() throws JobException {

		String aSOReleaseLineItemQry = "SELECT ecSplitCodeID,CodeName,DefaultPct FROM ecSplitCode ORDER BY CodeName ASC";
		Session aSession = null;
		ArrayList<Ecsplitcode> aQueryList = new ArrayList<Ecsplitcode>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Ecsplitcode ecsplitcode = new Ecsplitcode();

				Object[] aObj = (Object[]) aIterator.next();
				
				if (aObj[0] != null)
					ecsplitcode.setEcSplitCodeID((Integer) aObj[0]);
				
				if (aObj[1] != null)
					ecsplitcode.setCodeName((String) aObj[1]);
				if (aObj[2] != null)
					ecsplitcode.setDefaultPct((BigDecimal) aObj[2]);

				aQueryList.add(ecsplitcode);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public void deleteEmpCommissionsplitypeService(Integer ecSplitCodeId) {
		itsLogger.debug("delete Employee Commission splitype ");
		Session aSession = itsSessionFactory.openSession();
		Ecsplitcode ecsplitcode = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			ecsplitcode = (Ecsplitcode) aSession.get(Ecsplitcode.class,
					ecSplitCodeId);
			aSession.delete(ecsplitcode);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aCompanyException = new JobException(e.getCause()
					.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public ArrayList<AutoCompleteBean> getSplitTypewithNameList(
			String theProductName) throws JobException {
		// SELECT NAME FROM rxmaster WHERE InActive=1
		String salesselectQry = "SELECT ecSplitCodeID,CodeName FROM ecSplitCode WHERE CodeName like '"
				+ JobUtil.removeSpecialcharacterswithslash(theProductName) + "%' ORDER BY ecSplitCodeID ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setValue((String) aObj[1]);
				aUserbean.setLabel((String) aObj[1]);
				/*
				 * if (((String) aObj[2]) == null) { aUserbean.setLabel((String)
				 * aObj[1]); } else { String aProduct = (String) aObj[1] + " -["
				 * + (String) aObj[2] + "]"; aUserbean.setLabel(aProduct); }
				 */
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Ecsplitcode getpercentagebasedonsplittype(Integer id)
			throws JobException {
		// SELECT NAME FROM rxmaster WHERE InActive=1
		String salesselectQry = "SELECT DefaultPct FROM ecSplitCode WHERE ecSplitCodeID="
				+ id;
		Session aSession = null;
		double d = 0.00;
		BigDecimal percentage = new BigDecimal(d);
		Ecsplitcode ecsplitcode = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				ecsplitcode = new Ecsplitcode();
				ecsplitcode.setDefaultPct((BigDecimal) aIterator.next());
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;
		}
		return ecsplitcode;
	}

	@Override
	public EmMasterBean getEmployeeJobProfitCommission(Integer id)
			throws JobException {
		// SELECT NAME FROM rxmaster WHERE InActive=1
		String salesselectQry = "SELECT CommissionJobProfit FROM emMaster WHERE GetsCommission=1 AND emMasterID = "
				+ id;
		Session aSession = null;
		double d = 0.00;
		EmMasterBean ecsplitcode = null;
		try {
			itsLogger.info("getEmployeeJobProfitCommission()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				ecsplitcode = new EmMasterBean();
				ecsplitcode.setJobCommissions((BigDecimal) aIterator.next());
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getEmployeeJobProfitCommission()=[Connection Closed]");
			salesselectQry=null;
		}
		return ecsplitcode;
	}

	public boolean getSplittypethereornot(String jobnumber, int joreleaseid)
			throws JobException {
		// SELECT NAME FROM rxmaster WHERE InActive=1
		// String splitthereornotquery
		// ="SELECT * FROM ecSplitJob ecs JOIN  joMaster jo ON(jo.joMasterID=ecs.joMasterID) WHERE jo.JobNumber='"+jobnumber+"' AND ecs.joReleaseID="+joreleaseid
		// ;
		String splitthereornotquery = "SELECT * FROM ecSplitJob ecs WHERE ecs.joReleaseID="
				+ joreleaseid;
		Session aSession = null;
		boolean returnvalue = false;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(splitthereornotquery);
			Iterator<?> aIterator = aQuery.list().iterator();
			if (aIterator.hasNext()) {
				returnvalue = true;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			splitthereornotquery=null;
		}
		return returnvalue;
	}

	@Override
	public ArrayList<?> getJobBidList(int theFrom, int theRows,
			int therxMasterID, String sidx, String sord) throws JobException {
		String sortBy = "";
		if (sord.equalsIgnoreCase("biddate")) {
			sortBy = "jo.BidDate";
		} else if (sord.equalsIgnoreCase("description")) {
			sortBy = "jo.Description";
		} else {
			sortBy = "r.FirstName";
		}

		String aJobSelectQry = "SELECT DISTINCT jo.Description,jo.JobStatus,CONCAT(r.FirstName, ' ', r.LastName) AS FullNAme, "
				+ "jo.BidDate,jo.JobNumber,jo.joMasterID  FROM joBidder j  LEFT JOIN rxContact r ON j.rxContactID = r.rxContactID "
				+ "LEFT JOIN rxMaster rx ON j.rxMasterID = rx.rxMasterID  LEFT JOIN tsUserLogin ts ON j.UserLoginID = ts.UserLoginID RIGHT JOIN cuMasterType c ON j.cuMasterTypeID = c.cuMasterTypeID RIGHT JOIN joMaster jo ON jo.joMasterID = j.joMasterID WHERE rx.rxMasterID = '"
				+ therxMasterID
				+ "' AND jo.JobStatus = 0 AND jo.BidDate >= CURDATE() ORDER BY "
				+ sortBy
				+ " "
				+ sidx.toUpperCase()
				+ " LIMIT "
				+ theFrom
				+ ", " + theRows;
		Session aSession = null;
		ArrayList<JobCustomerBean> aQueryList = null;
		try {
			aQueryList = new ArrayList<JobCustomerBean>();
			JobCustomerBean aJobCustomerBean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);

			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aJobCustomerBean = new JobCustomerBean();
				Object[] aObj = (Object[]) aIterator.next();
				aJobCustomerBean.setDescription((String) aObj[0]);
				aJobCustomerBean.setBidStatus((Short) aObj[1]);
				aJobCustomerBean.setJobNumber((String) aObj[4]);
				aJobCustomerBean.setQuoteBy((String) aObj[2]);
				if (aObj[3] != null) {
					Timestamp stamp = (Timestamp) aObj[3];
					Date date = new Date(stamp.getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
					aJobCustomerBean.setBidDate(sdf.format(date));
				}
				aJobCustomerBean.setJoMasterId((Integer)aObj[5]);
				// aJobCustomerBean.setJoMasterId((Integer)aObj[4]);

				aQueryList.add(aJobCustomerBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;sortBy=null;
		}
		return aQueryList;
	}

	@Override
	public Jobidder savBidData(Jobidder givenJobidder, boolean isUpdate)
			throws JobException {
		itsLogger.debug("Insert Bidder to Job");
		Session aSession = itsSessionFactory.openSession();
		Jobidder aJobidder = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (isUpdate) {
				aJobidder = (Jobidder) aSession.get(Jobidder.class,
						givenJobidder.getJoBidderId());

				aJobidder.setJoMasterId(givenJobidder.getJoMasterId());
				aJobidder.setRxContactId(givenJobidder.getRxContactId());
				aJobidder.setRxMasterId(givenJobidder.getRxMasterId());
				aJobidder.setQuoteDate(givenJobidder.getQuoteDate());
				aSession.update(aJobidder);
			} else {
				aSession.save(givenJobidder);
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aCompanyException = new JobException(e.getCause()
					.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return givenJobidder;
	}

	@Override
	public List<?> getjobCommissionListGrid(Integer JoMasterId,
			Integer JoReleaseId) throws JobException {

		// String aSOReleaseLineItemQry =
		// "SELECT ecs.rxMasterID,CONCAT(rxm.FirstName,' ',rxm.Name) AS rep,ecs.Allocated,ecs.ecSplitCodeID,ecc.CodeName AS splittype,ecs.ecSplitJobID  FROM ecSplitJob ecs JOIN rxMaster rxm ON rxm.rxMasterID=ecs.rxMasterID JOIN ecSplitCode ecc ON ecc.ecSplitCodeID=ecs.ecSplitCodeID  WHERE ecs.joMasterID="
		// + JoMasterId +" and ecs.joReleaseID="+JoReleaseId;
		String aSOReleaseLineItemQry = "SELECT ecs.rxMasterID,rxm.Name,ecs.Allocated,ecs.ecSplitCodeID,"
				+ " IF(ecs.splitType IS NULL,esj.CodeName,ecs.splitType), ecs.ecSplitJobID "
				+ " FROM ecSplitJob ecs LEFT JOIN ecSplitCode esj ON ecs.ecSplitCodeID = esj.ecSplitCodeID "
				+ " JOIN emMaster emm ON emm.emMasterID = ecs.rxMasterID "
				+ " JOIN rxMaster rxm ON emm.emMasterID=rxm.rxMasterID WHERE  ecs.joReleaseID="
				+ JoReleaseId;
		Session aSession = null;
		ArrayList<Ecsplitjob> aQueryList = new ArrayList<Ecsplitjob>();
		try {

			itsLogger.info("getjobCommissionListGrid()=[Connection Opened]");
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Ecsplitjob ecsplitjob = new Ecsplitjob();

				Object[] aObj = (Object[]) aIterator.next();
				if (aObj[0] != null)
					ecsplitjob.setRxMasterId((Integer) aObj[0]);
				ecsplitjob.setRep((String) aObj[1]);
				if (aObj[2] != null)
					ecsplitjob.setAllocated((BigDecimal) aObj[2]);

				if (aObj[3] != null)
					ecsplitjob.setEcSplitCodeID((Integer) aObj[3]);
				ecsplitjob.setSplittype((String) aObj[4]);
				ecsplitjob.setEcSplitJobId((Integer) aObj[5]);

				aQueryList.add(ecsplitjob);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			itsLogger.info("getjobCommissionListGrid()=[Connection Closed]");
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public Integer addSOTemplateLineItem(
			Cusodetailtemplate theCusodetailtemplate,
			Cusotemplate theCusotemplate) throws JobException {
		Session aPOReleaseSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer acuSOdetailID = 0;
		Integer aCuSOID = 0;
		try {
			aTransaction = aPOReleaseSession.beginTransaction();
			aTransaction.begin();
			if (theCusotemplate.getCuSoid() != null) {
				Cusotemplate objCusotemplate = (Cusotemplate) aPOReleaseSession
						.get(Cusotemplate.class, theCusotemplate.getCuSoid());
				if (theCusotemplate.getSubTotal() != null)
					objCusotemplate.setSubTotal(theCusotemplate.getSubTotal());
				if (theCusotemplate.getFreight() != null)
					objCusotemplate.setFreight(theCusotemplate.getFreight());
				if (theCusotemplate.getTaxTotal() != null)
					objCusotemplate.setTaxTotal(theCusotemplate.getTaxTotal());
				if (theCusotemplate.getCostTotal() != null)
					objCusotemplate
							.setCostTotal(theCusotemplate.getCostTotal());
				aPOReleaseSession.update(objCusotemplate);
				aCuSOID = theCusotemplate.getCuSoid();

			} else {
				aCuSOID = (Integer) aPOReleaseSession.save(theCusotemplate);
			}
			if (theCusodetailtemplate.getCuSoid() != null)
				acuSOdetailID = (Integer) aPOReleaseSession
						.save(theCusodetailtemplate);
			else {
				theCusodetailtemplate.setCuSoid(aCuSOID);
				acuSOdetailID = (Integer) aPOReleaseSession
						.save(theCusodetailtemplate);
			}
			aTransaction.commit();
			/*
			 * theVepodetail.setPosistion(aVePOdetailID);
			 * updateVepoDetailPosistion(theVepodetail); if
			 * (theVepodetail.getTaxable() != false) { aTransaction =
			 * aVePOSession.beginTransaction(); aTransaction.begin(); Vepo aVepo
			 * = (Vepo) aVePOSession.get(Vepo.class, theVepo.getVePoid());
			 * aVepo.setTaxTotal(theVepo.getTaxTotal());
			 * aVepo.setVePoid(theVepo.getVePoid()); aVePOSession.update(aVepo);
			 * aTransaction.commit(); }
			 */
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getCause()
					.getMessage(), excep);
			throw aJobException;
		} finally {
			aPOReleaseSession.flush();
			aPOReleaseSession.close();
		}
		return aCuSOID;
	}

	@Override
	public ArrayList<Cusodetailtemplate> getSOTemplateList(Integer thecuSOID)
			throws InventoryException {
		// String aCusoselectQry =
		// "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		String aCusoselectQry = "SELECT soTemplate.cuSODetailID,soTemplate.cuSOID,soTemplate.prMasterID,soTemplate.Description,soTemplate.QuantityOrdered,soTemplate.UnitCost,soTemplate.PriceMultiplier,soTemplate.Taxable, pr.ItemCode,soTemplate.Note FROM cuSODetailTemplate soTemplate LEFT JOIN prMaster pr ON(pr.prMasterID=soTemplate.prMasterID) WHERE soTemplate.cuSOID="+ thecuSOID+" ORDER BY position ASC";
		Session aSession = null;
		ArrayList<Cusodetailtemplate> aQueryList = new ArrayList<Cusodetailtemplate>();
		Cusodetailtemplate aCusodetailtemplate = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(aCusoselectQry);
			Iterator<?> iterator = query.list().iterator();
			while (iterator.hasNext()) {
				aCusodetailtemplate = new Cusodetailtemplate();
				Object[] aObj = (Object[]) iterator.next();
				aCusodetailtemplate.setCuSodetailId((Integer) aObj[0]);
				aCusodetailtemplate.setCuSoid((Integer) aObj[1]);
				aCusodetailtemplate.setPrMasterId((Integer) aObj[2]);
				aCusodetailtemplate.setDescription((String) aObj[3]);
				aCusodetailtemplate.setQuantityOrdered((BigDecimal) aObj[4]);
				aCusodetailtemplate.setUnitCost((BigDecimal) aObj[5]);
				aCusodetailtemplate.setPriceMultiplier((BigDecimal) aObj[6]);
				/*
				 * if(null != aObj[7]) { Integer tax = (Integer)aObj[7]; if(tax
				 * == 1) aCusodetailtemplate.setTaxable((byte)1); else
				 * aCusodetailtemplate.setTaxable((byte)0);
				 * 
				 * }
				 */
				aCusodetailtemplate.setTaxable((byte) 1);
				aCusodetailtemplate.setNote((String) aObj[8]);
				aCusodetailtemplate.setNoteImage((String) aObj[9]);
				aCusodetailtemplate.setInlineNote((String) aObj[9]);
				aCusodetailtemplate.setUnitPrice((aCusodetailtemplate.getQuantityOrdered()!=null?aCusodetailtemplate.getQuantityOrdered():new BigDecimal(0))
						.multiply(aCusodetailtemplate.getPriceMultiplier()!=null?aCusodetailtemplate.getPriceMultiplier():new BigDecimal(0))
						.multiply(aCusodetailtemplate.getUnitCost()!=null?aCusodetailtemplate.getUnitCost():new BigDecimal(0)));
				aQueryList.add(aCusodetailtemplate);
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e
					.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Integer saveSOTemplate(Cusotemplate theCusotemplate)
			throws InventoryException {
		Session aSession = null;
		Transaction aTransaction;
		Integer cusoID = theCusotemplate.getCuSoid();
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (theCusotemplate.getCuSoid() != null
					&& theCusotemplate.getCuSoid() > 0)
				aSession.update(theCusotemplate);
			else
				cusoID = (Integer) aSession.save(theCusotemplate);
			aTransaction.commit();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e
					.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return cusoID;

	}
	@Override
	public Integer saveSODetailTemplate(Cusodetailtemplate theCusoDetailtemplate)
			throws InventoryException {
		Session aSession = null;
		Transaction aTransaction;
		Integer cusoID = theCusoDetailtemplate.getCuSoid();
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (theCusoDetailtemplate.getCuSodetailId() != null
					&& theCusoDetailtemplate.getCuSodetailId() > 0)
			{
			/*Cusodetailtemplate tempCusoDetailtemplate=(Cusodetailtemplate) aSession.get(Cusodetailtemplate.class,theCusoDetailtemplate.getCuSodetailId());
			
			tempCusoDetailtemplate.setCuSodetailId(theCusoDetailtemplate.getCuSodetailId());
			tempCusoDetailtemplate.setCuSoid(theCusoDetailtemplate.getCuSoid());
			tempCusoDetailtemplate.setDescription(theCusoDetailtemplate.getDescription());
			tempCusoDetailtemplate.setInlineNote(theCusoDetailtemplate.getInlineNote());
			tempCusoDetailtemplate.setNote(theCusoDetailtemplate.getNote());
			tempCusoDetailtemplate.setNoteImage(theCusoDetailtemplate.getNoteImage());
			tempCusoDetailtemplate.setPosition(theCusoDetailtemplate.getPosition());*/
			
			
				aSession.update(theCusoDetailtemplate);
			}	
			else
				cusoID = (Integer) aSession.save(theCusoDetailtemplate);
			aTransaction.commit();
			
			
			

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e
					.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return cusoID;

	}
	

	@Override
	public Integer deleteSOTemplate(Integer cuSoid) throws InventoryException {
		Session aSession = null;
		Transaction aTransaction;
		try {
			aSession = itsSessionFactory.openSession();

			Criteria criteria = aSession.createCriteria(
					Cusodetailtemplate.class).add(
					Restrictions.eq("cuSoid", cuSoid));
			@SuppressWarnings("unchecked")
			List<Cusodetailtemplate> cusoDetailId = (List<Cusodetailtemplate>) criteria
					.list();
			for (Cusodetailtemplate id : cusoDetailId) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.delete(id);
				aTransaction.commit();
			}

			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			Cusotemplate aCusotemplate = (Cusotemplate) aSession.get(
					Cusotemplate.class, cuSoid);
			aSession.delete(aCusotemplate);
			aTransaction.commit();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e
					.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return cuSoid;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Cusotemplate> getSOTemplate() throws InventoryException {
		// String aCusoselectQry =
		// "SELECT so.cuSOID,sodetail.cuSODetailID,so.CreatedOn,so.DatePromised,rxmaster.Name,so.joReleaseId,SUM(sodetail.quantityordered) AS quantityordered,warehouse.City,so.rxCustomerID FROM cuSODetail sodetail JOIN cuSO so ON  so.cuSOID = sodetail.cuSOID JOIN rxMaster rxmaster ON rxmaster.rxMasterID = so.rxCustomerID JOIN prWarehouse warehouse ON warehouse.prWarehouseID = so.prFromWarehouseID WHERE sodetail.prMasterID ="+theprMasterID+" GROUP BY sodetail.cuSOID ORDER BY sodetail.cuSOID DESC";
		String aCusoselectQry = "from Cusotemplate  ORDER BY TemplateDescription ASC";
		Session aSession = null;
		ArrayList<Cusotemplate> aQueryList = new ArrayList<Cusotemplate>();
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createQuery(aCusoselectQry);
			aQueryList = (ArrayList<Cusotemplate>) query.list();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e
					.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
			aCusoselectQry=null;
		}
		return aQueryList;
	}

	@Override
	public Cusotemplate loadSOTemplate(Integer cuSoid)
			throws InventoryException {
		Session aSession = null;
		Cusotemplate aCusotemplate = null;
		try {
			aSession = itsSessionFactory.openSession();
			aCusotemplate = (Cusotemplate) aSession.get(Cusotemplate.class,
					cuSoid);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			InventoryException aInventoryException = new InventoryException(e
					.getCause().getMessage(), e);
			throw aInventoryException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCusotemplate;

	}

	@Override
	public boolean updateSplitCommission(Ecsplitjob ecsplitjobb)
			throws JobException {
		Session aJoscheduledetailSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoscheduledetailSession.beginTransaction();
			aTransaction.begin();
			itsLogger.info(ecsplitjobb.getRxMasterId() + "rx"
					+ ecsplitjobb.getEcSplitCodeID() + "ec"
					+ ecsplitjobb.getEcSplitJobId());
			Ecsplitjob ecsplitjob = (Ecsplitjob) aJoscheduledetailSession.get(
					Ecsplitjob.class, ecsplitjobb.getEcSplitJobId());
			if (ecsplitjobb.getRxMasterId() != null
					&& ecsplitjobb.getRxMasterId() > 0) {
				ecsplitjob.setRxMasterId(ecsplitjobb.getRxMasterId());
			}
			if (ecsplitjobb.getJoMasterId() != null
					&& ecsplitjobb.getJoMasterId() > 0) {
				ecsplitjob.setJoMasterId(ecsplitjobb.getJoMasterId());
			}
			
			if (ecsplitjobb.getEcSplitCodeID() != null
					&& ecsplitjobb.getEcSplitCodeID() > 0) {
				ecsplitjob.setEcSplitCodeID(ecsplitjobb.getEcSplitCodeID());
			}
			itsLogger.info(ecsplitjobb.getAllocated());
			ecsplitjob.setAllocated(ecsplitjobb.getAllocated());
			ecsplitjob.setSplittype(ecsplitjobb.getSplittype());
			ecsplitjob.setChangedByID(ecsplitjobb.getChangedByID());
			ecsplitjob.setChangedOn(ecsplitjobb.getChangedOn());
			aJoscheduledetailSession.update(ecsplitjob);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoscheduledetailSession.flush();
			aJoscheduledetailSession.close();
		}
		return false;
	}

	@Override
	public Integer addSOTemplateLineItem1(Cusodetailtemplate theCuSODetail,
			Cusotemplate theCuso) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		Cusotemplate aCuso = new Cusotemplate();
		BigDecimal checkTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		int compare = 0;
		Integer id = theCuso.getCuSoid();
		try {
			/*
			 * updatePrMasterForSO(theCuSODetail, new BigDecimal(0),
			 * theCuSODetail.getQuantityOrdered(), "add",null);
			 * updatePrWarehouseInventoryForSO(theCuSODetail, new BigDecimal(0),
			 * theCuSODetail.getQuantityOrdered(), "add",null);
			 */
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if (theCuso.getCuSoid() != null)
				aSession.update(theCuso);
			else
				id = (Integer) aSession.save(theCuso);

			theCuSODetail.setCuSoid(id);
			aSession.save(theCuSODetail);
			aCuso = (Cusotemplate) aSession.get(Cusotemplate.class, id);
			if (aCuso.getTaxTotal() != null)
				Taxtotal = aCuso.getTaxTotal();
			compare = Taxtotal.compareTo(checkTax);
			if (Taxtotal != null && compare == 1) { /* Add to existing values */
				if (theCuso.getTaxTotal() != null)
					aCuso.setTaxTotal(aCuso.getTaxTotal().add(
							theCuso.getTaxTotal()));
			} else { /* the new Entry to cuso */
				aCuso.setTaxTotal(theCuso.getTaxTotal());
			}

			aSession.update(aCuso);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return id;
	}

	@Override
	public boolean editSOTemplateLineItem(Cusodetailtemplate theCuSODetail,
			Cusotemplate theCuso, Cusotemplate editCuso) throws JobException {
		Session cusoDetailSession = null;
		Transaction aTransaction = null;
		Cusodetailtemplate aCusodetail = new Cusodetailtemplate();
		Cusotemplate aCuso = new Cusotemplate();
		cusoDetailSession = itsSessionFactory.openSession();
		BigDecimal checkTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		BigDecimal oldQuantityOrdered = new BigDecimal(0);
		BigDecimal newQuantityOrdered = new BigDecimal(0);
		try {
			aTransaction = cusoDetailSession.beginTransaction();
			aCusodetail = (Cusodetailtemplate) cusoDetailSession.get(
					Cusodetailtemplate.class, theCuSODetail.getCuSodetailId());
			aTransaction.begin();
			oldQuantityOrdered = aCusodetail.getQuantityOrdered();
			newQuantityOrdered = theCuSODetail.getQuantityOrdered();
			// Added to reduce On Order Value in Prmaster
			/*
			 * updatePrMasterForSO(theCuSODetail, oldQuantityOrdered,
			 * newQuantityOrdered, "edit",aCusodetail);
			 * updatePrWarehouseInventoryForSO(theCuSODetail,
			 * oldQuantityOrdered, newQuantityOrdered, "edit",aCusodetail);
			 */
			aCusodetail.setDescription(theCuSODetail.getDescription());
			aCusodetail.setPrMasterId(theCuSODetail.getPrMasterId());
			aCusodetail.setPriceMultiplier(theCuSODetail.getPriceMultiplier());
			aCusodetail.setUnitCost(theCuSODetail.getUnitCost());
			aCusodetail.setQuantityOrdered(theCuSODetail.getQuantityOrdered());
			aCusodetail.setTaxable(theCuSODetail.getTaxable());
			cusoDetailSession.update(aCusodetail);
			aTransaction.commit();
			aTransaction = cusoDetailSession.beginTransaction();
			aTransaction.begin();
			aCuso = (Cusotemplate) cusoDetailSession.get(Cusotemplate.class,
					theCuso.getCuSoid());
			BigDecimal oldTax = new BigDecimal(0.00);
			if (aCuso.getTaxTotal() != null) {
				oldTax = aCuso.getTaxTotal();
			}

			int compare = oldTax.compareTo(checkTax);
			if (aCuso.getTaxTotal() != null && compare == 1) {
				Taxtotal = oldTax.subtract(editCuso.getTaxTotal());
			}
			if (theCuso != null) {
				Taxtotal = Taxtotal.add(theCuso.getTaxTotal());
			}

			aCuso.setTaxTotal(Taxtotal);
			cusoDetailSession.update(aCuso);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			cusoDetailSession.flush();
			cusoDetailSession.close();
		}
		return true;
	}

	@Override
	public boolean deleteSOTemplateLineItem(Cusodetailtemplate theCusodetail,
			Cusotemplate theCuso) throws JobException {
		Session acusodetSession = null;
		Transaction aTransaction = null;
		Cusodetailtemplate aCusodetail = new Cusodetailtemplate();
		Cusotemplate acuSO = new Cusotemplate();
		BigDecimal newTax = new BigDecimal(0);
		BigDecimal Taxtotal = new BigDecimal(0);
		BigDecimal oldQuantityOrdered = new BigDecimal(0);
		BigDecimal newQuantityOrdered = new BigDecimal(0);
		try {
			acusodetSession = itsSessionFactory.openSession();
			aCusodetail = (Cusodetailtemplate) acusodetSession.get(
					Cusodetailtemplate.class, theCusodetail.getCuSodetailId());

			oldQuantityOrdered = aCusodetail.getQuantityOrdered();
			newQuantityOrdered = theCusodetail.getQuantityOrdered();
			/*
			 * updatePrMasterForSO(aCusodetail, new BigDecimal(0), new
			 * BigDecimal( 0), "del",null);
			 * updatePrWarehouseInventoryForSO(aCusodetail, new BigDecimal(0),
			 * new BigDecimal( 0), "del",null);
			 */
			aTransaction = acusodetSession.beginTransaction();
			aTransaction.begin();
			acusodetSession.delete(aCusodetail);
			aTransaction.commit();
			aTransaction = acusodetSession.beginTransaction();
			aTransaction.begin();
			acuSO = (Cusotemplate) acusodetSession.get(Cusotemplate.class,
					theCuso.getCuSoid());
			if (acuSO.getTaxTotal() != null)
				Taxtotal = acuSO.getTaxTotal();
			int compare = Taxtotal.compareTo(newTax);
			if (compare == 1 && theCuso.getTaxTotal() != newTax)
				acuSO.setTaxTotal(acuSO.getTaxTotal().subtract(
						theCuso.getTaxTotal()));
			acusodetSession.update(acuSO);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acusodetSession.flush();
			acusodetSession.close();
		}
		return true;
	}

	@Override
	public Cusodetailtemplate getSingleCusoTemplateDetailObj(int CusoDetailID)
			throws JobException {
		Session aSession = null;
		Cusodetailtemplate acusoDetail = null;
		try {
			aSession = itsSessionFactory.openSession();
			acusoDetail = (Cusodetailtemplate) aSession.get(
					Cusodetailtemplate.class, CusoDetailID);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return acusoDetail;
	}

	@Override
	public Cusodetailtemplate getCusoTemplateDetailObj(int CusoID)
			throws JobException {
		Session aSession = null;
		Cusodetailtemplate aCusodetail = null;
		Cusodetailtemplate iteratorObj = null;
		BigDecimal sum = new BigDecimal(0);
		int i = 0;
		try {
			String Query = "FROM Cusodetailtemplate WHERE cuSoid= :cuSoid";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(Query);
			aQuery.setParameter("cuSoid", CusoID);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				aCusodetail = (Cusodetailtemplate) aList.get(0);
				for (i = 0; i < aList.size(); i++) {
					iteratorObj = (Cusodetailtemplate) aList.get(i);
					BigDecimal total = (iteratorObj.getQuantityOrdered()!=null?iteratorObj.getQuantityOrdered():new BigDecimal(0))
							.multiply(iteratorObj.getPriceMultiplier()!=null?iteratorObj.getPriceMultiplier():new BigDecimal(0))
							.multiply(iteratorObj.getUnitCost()!=null?iteratorObj.getUnitCost():new BigDecimal(0));
					sum = sum.add(total);
				}
				aCusodetail.setUnitPrice(sum); // A transient field 'tax total'
												// to show in UI. Need to change
			}
			Query=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCusodetail;
	}

	@Override
	public Map<String, BigDecimal> getSOTemplatePriceDetails(int CusoID)
			throws JobException {
		Session aSession = null;
		BigDecimal margin = new BigDecimal(0);
		BigDecimal cost = new BigDecimal(0);
		Map<String, BigDecimal> aPrice = new HashMap<String, BigDecimal>();
		try {
			String Query = "SELECT prMaster.prMasterID,prMaster.SalesPrice00,prMaster.LastCost,template.QuantityOrdered FROM cuSODetailTemplate AS template,prMaster  WHERE template.cuSoid= :cuSoid AND prMaster.prMasterID = template.prMasterID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(Query);
			aQuery.setParameter("cuSoid", CusoID);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					BigDecimal salesPrice00 = (BigDecimal) aObj[1] == null ? new BigDecimal(
							0.00) : (BigDecimal) aObj[1];
					BigDecimal lastCost = (BigDecimal) aObj[2] == null ? new BigDecimal(
							0.00) : (BigDecimal) aObj[2];
					BigDecimal qty = (BigDecimal) aObj[3] == null ? new BigDecimal(
							0.00) : (BigDecimal) aObj[3];
					margin = margin.add(salesPrice00.multiply(qty));
					cost = cost.add(lastCost.multiply(qty));
				}
				// BigDecimal percentage =
				// margin.subtract(cost).divide(margin).multiply(new
				// BigDecimal(100));
				aPrice.put("product", new BigDecimal(0.00));
				aPrice.put("margin", margin);
				aPrice.put("cost", cost);
				aPrice.put("percentage", new BigDecimal(0.00));
			} else {
				aPrice.put("product", new BigDecimal(0.00));
				aPrice.put("margin", new BigDecimal(0.00));
				aPrice.put("cost", new BigDecimal(0.00));
				aPrice.put("percentage", new BigDecimal(0.00));
			}
			Query=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPrice;
	}

	/*
	 * Getting drop down data for CuMasterType in Customer Bid list
	 */
	@Override
	public List<CuMasterType> getCuMasterTypeForBidList() {
		Session aSession = null;
		List<CuMasterType> aQueryList = new ArrayList<CuMasterType>();
		String aCustomerQry = "SELECT * FROM cuMasterType ORDER BY Code ASC";

		CuMasterType aRolodexBean = null;
		try {

			aSession = itsSessionFactory.openSession();

			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new CuMasterType();
				Object[] aObj = (Object[]) aIterator.next();
				aRolodexBean.setCuMasterTypeId((Integer) aObj[0]);
				aRolodexBean.setCode((String) aObj[1]);

				aQueryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;
	}

	@Override
	public Integer addJoRelease(JoRelease theJob) throws JobException {
		Session aSession = null;

		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(theJob);
			itsLogger.info("theJob.getJoReleaseId()" + theJob.getJoReleaseId());
			return theJob.getJoReleaseId();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Integer addJoReleaseDetail(JoReleaseDetail theJob)
			throws JobException {
		Session aSession = null;

		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(theJob);
			itsLogger.info("theJob.getJoReleasedetailId()"
					+ theJob.getJoReleaseDetailId());
			return theJob.getJoReleaseDetailId();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	public Integer updateCuInvoiceJoReleaseDetail(JoReleaseDetail theJob)
			throws JobException {
		Session aSession = null;

		try {
			aSession = itsSessionFactory.openSession();
			aSession.update(theJob);
			itsLogger.info("theJob.getJoReleasedetailId()"
					+ theJob.getJoReleaseDetailId());
			return theJob.getJoReleaseDetailId();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Emmaster getSalesManDetails(Integer emMasterID) throws JobException {
		Session aSession = null;
		Emmaster aEmmaster = null;
		try {
			aSession = itsSessionFactory.openSession();
			aEmmaster = (Emmaster) aSession.get(Emmaster.class, emMasterID);
			if (aEmmaster == null) {
				aEmmaster = new Emmaster();
			}
			itsLogger.info("theJob.getJoReleasedetailId()"
					+ aEmmaster.getJobNumberGenerate());
			return aEmmaster;

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public Emmaster getEmployeeDetailLoginID(int UserLoginID)
			throws JobException {
		Session aSession = null;
		Emmaster aEmmaster = null;
		try {
			String Query = "FROM Emmaster WHERE UserLoginID= :UserLoginID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(Query);
			aQuery.setParameter("UserLoginID", UserLoginID);
			List<?> aList = aQuery.list();
			if (!aList.isEmpty()) {
				aEmmaster = (Emmaster) aList.get(0);
			}
			Query=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aEmmaster;
	}

	/*@Override
	public String getJobNumber(String sJobNumber) throws JobException {

		Session aSession = null;

		try {
			String theJobNumber = "SELECT JobNumber from joMaster where JobNumber LIKE '"
					+ sJobNumber + "%' ORDER BY joMasterID DESC LIMIT 1";
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(theJobNumber);
			if (query.list().size() > 0)
				return (String) query.list().get(0);
			else
				return new String("");

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}

	}*/
	
	@Override
	public String getJobNumber(String theSearchJobString)
			throws JobException {
		String aJobSelectQry = "SELECT jobNumber, RIGHT(jobNumber, 3) AS jobNumberCount FROM joMaster WHERE jobNumber LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchJobString)
				+ "%' ORDER BY jobNumberCount DESC limit 1;";
		Session aSession = null;
		String aNewBookedjobNumber = null;
		String aJobString = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			List<?> aList = aQuery.list();
			if (aList.isEmpty()) {
				aJobString ="";
			} else {
				Iterator<?> aIterator = aList.iterator();
				if (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aJobString = (String) aObj[0];
				}
				
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobSelectQry=null;
		}
		return aJobString;
	}

	/**
	 * created by : Praveenkumar Date :2014/08/31 Purpose: Adding a revision to
	 * copy quote
	 * 
	 */

	@Override
	public Integer getLatestRevisionNumberCopy(Integer joMasterId,
			Integer joQuoteHeaderID) throws JobException {
		Session aSession = null;

		try {
			String theJobNumber = "SELECT (QuoteRev) from joQuoteHeader where joMasterID = "
					+ joMasterId
					+ " AND cuMasterTypeID="
					+ joQuoteHeaderID
					+ " ORDER BY CAST(QuoteRev AS UNSIGNED) DESC LIMIT 1";
			
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(theJobNumber);
			Iterator<?> aIterator = query.list().iterator();
			while (aIterator.hasNext()) {

				Double dbvalue = (Double) aIterator.next();
				
				return dbvalue.intValue();

				// return Integer.parseInt(String.valueOf(dbvalue.intValue()));
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		}
		
		return 0;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turborep.turbotracker.job.service.JobService#addCuReceiptFromInvoice
	 * (com.turborep.turbotracker.customer.dao.Cureceipt) created by :
	 * praveenkumar Date: 2014-09-01 Purpose: Save Receipt detail from job
	 * invoice
	 */
	@Override
	public Integer addCuReceiptFromInvoice(Cureceipt theCuReceipt) {
		Session aSession = null;
		Integer cuReceiptID = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(theCuReceipt);
			cuReceiptID = theCuReceipt.getCuReceiptId();
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return cuReceiptID;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turborep.turbotracker.job.service.JobService#addCuReceiptFromInvoice
	 * (com.turborep.turbotracker.customer.dao.Cureceipt) created by :
	 * praveenkumar Date: 2014-09-01 Purpose: Save Linkage detail from job
	 * invoice
	 */
	@Override
	public Integer addCuLinkageDetailFromInvoice(
			CuLinkageDetail aCuLinkageDetail) {
		Session aSession = null;
		Integer cuReceiptID = null;
		try {
			aSession = itsSessionFactory.openSession();
			aSession.save(aCuLinkageDetail);
			cuReceiptID = aCuLinkageDetail.getCuLinkageDetailId();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return cuReceiptID;
	}

	public BigDecimal getContractAmount(String theJobNumber) {
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return (new BigDecimal(0));
	}

	@Override
	public void copyPOLineItemService(Integer vePODetailId) {
		Session aSession = null;
		Transaction aTransaction;
		List<?> aPOLineItemDetails = null;
		try {
			aSession = itsSessionFactory.openSession();
			Vepodetail vePODetail = (Vepodetail) aSession.get(Vepodetail.class,
					vePODetailId);
			Vepodetail avepoDetail = new Vepodetail();

			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			BeanUtils.copyProperties(vePODetail, avepoDetail);

			Integer newVepodetailid=(Integer)aSession.save(avepoDetail);

			avepoDetail.setPosistion(avepoDetail.getVePodetailId());
			aSession.update(avepoDetail);
			aTransaction.commit();
			
			
			
			Vepodetail newvePODetail = (Vepodetail) aSession.get(Vepodetail.class,newVepodetailid);
			Vepo insideoroutsidejob=getVepo(newvePODetail.getVePoid());
			if(insideoroutsidejob.getJoReleaseId()!=null){
			Prmaster thePrmaster =getPrMasterBasedOnId(newvePODetail.getPrMasterId());
			if(newVepodetailid>0 && thePrmaster!=null && thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(newvePODetail,"add");
			}
			}else{
				if(newVepodetailid>0 ){
				updatePrWarehouseInventoryOrdered(newvePODetail,"add");
				}
			}
			
			// aPOLineItemDetails = getPOLineDetails(vepoID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}

	}

	public static String doHttpUrlConnectionAction(String desiredUrl)
			throws Exception {
		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder;
		try {
			// create the HttpURLConnection
			url = new URL(desiredUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");

			// connection.setDoOutput(true);

			// give it 15 seconds to respond
			connection.setReadTimeout(15 * 1000);
			connection.connect();

			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			stringBuilder = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// close the reader; this can throw an exception too, so
			// wrap it in another try/catch block.
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turborep.turbotracker.job.service.JobService#saveSalesOrderNote(java
	 * .lang.Integer, java.lang.String) Created by: Praveenkumar Date :
	 * 2014-09-15 Purpose: To save sales order line item note
	 */
	@Override
	public Boolean saveSalesOrderNote(Integer cuSoDetailID, String note) {
		itsLogger.info("Notes::"+note);
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Transaction aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		try {

			Cusodetail cuSoDetail = (Cusodetail) aSession.get(Cusodetail.class,
					cuSoDetailID);
			cuSoDetail.setNote(note);
			aSession.update(cuSoDetail);
			aTransaction.commit();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;

		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}

	@Override
	public Boolean saveCustomerInvoiceNote(Integer CuinvoicedetailID, String note) {
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Transaction aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		try {

			Cuinvoicedetail aCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,
					CuinvoicedetailID);
			aCuinvoicedetail.setNote(note);
			aSession.update(aCuinvoicedetail);
			aTransaction.commit();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;

		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turborep.turbotracker.job.service.JobService#saveSalesOrderNote(java
	 * .lang.Integer, java.lang.String) Created by: Praveenkumar Date :
	 * 2014-09-15 Purpose: Copy Sales Order template line items to Sales Order
	 */
	@Override
	public ArrayList<Cusodetail> copySOTemplateLineItems(Integer cuSOID,Integer templateID) {

		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Transaction aTransaction = aSession.beginTransaction();
		String aSelectQuery = "select cuSODetailTemplate.prMasterID,cuSODetailTemplate.Description,cuSODetailTemplate.Note,cuSODetailTemplate.UnitCost,cuSODetailTemplate.UnitPrice,cuSODetailTemplate.PriceMultiplier,cuSODetailTemplate.Taxable,cuSODetailTemplate.joSchedDetailID,cuSODetailTemplate.QuantityOrdered,cuSODetailTemplate.Note,prMaster.ItemCode  from cuSODetailTemplate LEFT JOIN prMaster ON(prMaster.prMasterID=cuSODetailTemplate.prMasterID) where cuSOID = "
				+ templateID;
		ArrayList<Cusodetail> Cusodetaillst=new ArrayList<Cusodetail>();
		try {

			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				aTransaction.begin();
				Cusodetail cuSODetail = new Cusodetail();
				cuSODetail.setCuSoid(cuSOID);
				if (aObj[0] != null) {
					cuSODetail.setPrMasterId((Integer) aObj[0]);
				}
				if (aObj[1] != null) {
					cuSODetail.setDescription((String) aObj[1]);
				}
				if (aObj[2] != null) {
					cuSODetail.setNote("");
				}
				if (aObj[3] != null) {
					cuSODetail.setUnitCost((BigDecimal) aObj[3]);
				}
				if (aObj[4] != null) {
					cuSODetail.setUnitPrice((BigDecimal) aObj[4]);
				}
				if (aObj[5] != null) {
					cuSODetail.setPriceMultiplier((BigDecimal) aObj[5]);
				}else{
					cuSODetail.setPriceMultiplier(BigDecimal.ZERO);
				}
				if (aObj[6] != null) {
					cuSODetail.setTaxable((Byte) aObj[6]);
				}
				if (aObj[7] != null) {
					cuSODetail.setJoSchedDetailId((Integer) aObj[7]);
				}
				if(aObj[8] == null){
					aObj[8]=BigDecimal.ZERO;
				}
				cuSODetail.setQuantityOrdered((BigDecimal) (aObj[8]));
				cuSODetail.setQuantityBilled(BigDecimal.ZERO);
				cuSODetail.setCuSoid((Integer) (cuSOID));
				cuSODetail.setWhseCost(itsInventoryService.getWarehouseCost(cuSODetail.getPrMasterId()));
				cuSODetail.setNote((String) aObj[9]);
				if (aObj[10] != null) {
					cuSODetail.setItemCode((String) aObj[10]);
				}
				Cusodetaillst.add(cuSODetail);
				/*Integer cusodetailId=(Integer)aSession.save(cuSODetail);
				aTransaction.commit();
				cuSODetail.setCuSodetailId(cusodetailId);*/
				
				/*Cuso aCuso = (Cuso) aSession.get(Cuso.class, cuSODetail.getCuSoid());
				Prmaster thePrmaster =getPrMasterBasedOnId(cuSODetail.getPrMasterId());
				if(aCuso!=null&&aCuso.getJoReleaseId()!=null){
					if(thePrmaster.getIsInventory()==1){
						updatePrMasterForSO(cuSODetail, new BigDecimal(0),
								cuSODetail.getQuantityOrdered(), "add", null);
						updatePrWarehouseInventoryForSO(cuSODetail, new BigDecimal(0),
								cuSODetail.getQuantityOrdered(), "add", null);
					}
					}else{
						if(thePrmaster.getIsInventory()==1){
						updatePrMasterForSO(cuSODetail, new BigDecimal(0),
								cuSODetail.getQuantityOrdered(), "add", null);
						updatePrWarehouseInventoryForSO(cuSODetail, new BigDecimal(0),
								cuSODetail.getQuantityOrdered(), "add", null);
						}
					}*/
				
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Cusodetaillst;

		} finally {
			aSession.flush();
			aSession.close();
		}
		return Cusodetaillst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turborep.turbotracker.job.service.JobService#saveSalesOrderNote(java
	 * .lang.Integer, java.lang.String) Created by: Praveenkumar Date :
	 * 2014-09-16 Purpose: Show Sales Order cost &Margin - calculation
	 */
	@Override
	public Boolean calculateSOCost(Integer cuSOID) {
		Session aSession = null;
		aSession = itsSessionFactory.openSession();
		Transaction aTransaction = aSession.beginTransaction();
		aTransaction.begin();
		String aSelectQuery = "";
		try {

			Query aQuery = aSession.createSQLQuery(aSelectQuery);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			aSession.flush();
			aSession.close();
			aSelectQuery=null;
		}
		return true;
	}

	@Override
	public jocategory getJobCategories() throws JobException {
		String aSysPriSelectQry = "SELECT joCategoryID,Category FROM jocategory";
		Session aSession = null;
		jocategory ajocategory = null;
		int i = 0;
		try {
			Sysprivilege aSysprivilege = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			ajocategory = new jocategory();
			while (aIterator.hasNext()) {

				Object[] aObj = (Object[]) aIterator.next();
				if (i == 0) {
					ajocategory.setJoCategory1ID((Integer) aObj[0]);
					ajocategory.setCategory1desc((String) aObj[1]);
				} else if (i == 1) {
					ajocategory.setJoCategory2ID((Integer) aObj[0]);
					ajocategory.setCategory2desc((String) aObj[1]);

				} else if (i == 2) {
					ajocategory.setJoCategory3ID((Integer) aObj[0]);
					ajocategory.setCategory3desc((String) aObj[1]);
				} else if (i == 3) {
					ajocategory.setJoCategory4ID((Integer) aObj[0]);
					ajocategory.setCategory4desc((String) aObj[1]);
				} else if (i == 4) {
					ajocategory.setJoCategory5ID((Integer) aObj[0]);
					ajocategory.setCategory5desc((String) aObj[1]);
				} else if (i == 5) {
					ajocategory.setJoCategory6ID((Integer) aObj[0]);
					ajocategory.setCategory6desc((String) aObj[1]);
				} else if (i == 6) {
					ajocategory.setJoCategory7ID((Integer) aObj[0]);
					ajocategory.setCategory7desc((String) aObj[1]);
				}

				i++;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry=null;
		}
		return ajocategory;
	}

	@Override
	public List<?> getContactFromRxMaster(Integer therxMasterID)
			throws JobException {

		String aSOReleaseLineItemQry = "SELECT rxContactID,rxMasterID,FirstName,LastName,JobPosition,DirectLine,Cell,Email FROM rxContact WHERE rxMasterID="
				+ therxMasterID + ";";
		Session aSession = null;
		ArrayList<Rxcontact> aQueryList = new ArrayList<Rxcontact>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSOReleaseLineItemQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Rxcontact rxcontact = new Rxcontact();
				Object[] aObj = (Object[]) aIterator.next();

				rxcontact.setRxContactId((Integer) aObj[0]);
				rxcontact.setRxMasterId((Integer) aObj[1]);
				rxcontact.setFirstName((String) (aObj[2] + "" + aObj[3]));
				rxcontact.setJobPosition((String) aObj[4]);
				rxcontact.setDirectLine((String) aObj[5]);
				rxcontact.setCell((String) aObj[6]);
				rxcontact.setEmail((String) aObj[7]);

				aQueryList.add(rxcontact);
			}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the SO LineItem list: "
					+ e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
			aSOReleaseLineItemQry=null;
		}
		return aQueryList;
	}

	@Override
	public List<Vebilldetail> getVeBillLineItemsFromRelease(Integer vepoid)
			throws JobException {
		Session aSession = null;
		List<Vebilldetail> aVebillLineitemsList = null;
		Vebilldetail aVebilldetail = null;
		String anSqlQry = "SELECT vbd.veBillDetailID, vbd.veBillID, vbd.vePODetailID, vbd.prMasterID, pr.ItemCode, vbd.Description,vbd.Note, vbd.QuantityBilled, vbd.UnitCost, vbd.PriceMultiplier, vbd.FreightCost  "
				+ "FROM veBill vb JOIN veBillDetail vbd ON vb.veBillID=vbd.veBillID  RIGHT JOIN prMaster pr ON vbd.prMasterID = pr.prMasterID "
				+ "WHERE vb.vePOID =" + vepoid;
		try {
			aVebillLineitemsList = new ArrayList<Vebilldetail>();
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createSQLQuery(anSqlQry);
			Iterator<?> iterator = query.list().iterator();
			while (iterator.hasNext()) {
				aVebilldetail = new Vebilldetail();
				Object[] aObj = (Object[]) iterator.next();
				aVebilldetail.setVeBillDetailId((Integer) aObj[0]);
				aVebilldetail.setVeBillId((Integer) aObj[1]);
				aVebilldetail.setVePodetailId((Integer) aObj[2]);
				aVebilldetail.setPrMasterId((Integer) aObj[3]);
				aVebilldetail.setPrItemCode((String) aObj[4]);
				aVebilldetail.setDescription((String) aObj[5]);
				aVebilldetail.setNote((String) aObj[6]);
				aVebilldetail.setQuantityBilled((BigDecimal) aObj[7]);
				BigDecimal qty = (BigDecimal) aObj[7];
				BigDecimal unitCost = new BigDecimal(0);
				BigDecimal multiplier = new BigDecimal(0);
				BigDecimal atotal = new BigDecimal(0);
				if (aObj[8] != null) {
					unitCost = (BigDecimal) aObj[8];

				}

				atotal = qty.multiply(unitCost);
				if (aObj[9] != null) {
					multiplier = (BigDecimal) aObj[9];
					if (multiplier.compareTo(new BigDecimal(0)) == 1) {
						atotal = atotal.multiply(multiplier);
					}
					// int res = mul
				}
				aVebilldetail.setAmount(atotal);
				aVebilldetail.setUnitCost((BigDecimal) aObj[8]);
				aVebilldetail.setPriceMultiplier((BigDecimal) aObj[9]);
				aVebilldetail.setFreightCost((BigDecimal) aObj[10]);
				aVebillLineitemsList.add(aVebilldetail);
			}
			/*
			 * Criteria aCriteria = aSession.createCriteria(Vebilldetail.class);
			 * aCriteria.add(Restrictions.eq("veBillId", veBillId));
			 * aVebillLineitemsList = aCriteria.list();
			 */
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new JobException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			anSqlQry=null;
		}
		return aVebillLineitemsList;
	}

	@Override
	public List<?> getBidStatusList(int inActiveValue) throws JobException {
		Session aSession = null;
		List<Jobidstatus> aQueryList = new ArrayList<Jobidstatus>();
		String aCustomerQry = "SELECT * FROM joBidStatus  WHERE InActive="
				+ inActiveValue;
		Jobidstatus aJobBidStatus = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {

				aJobBidStatus = new Jobidstatus();
				Object[] aObj = (Object[]) aIterator.next();
				aJobBidStatus.setJoBidStatusId((Integer) aObj[0]);
				aJobBidStatus.setDescription((String) aObj[2]);
				Byte inActive = ((Byte) aObj[3]);
				aJobBidStatus.setInActive(false);
				if (inActive == 1) {
					aJobBidStatus.setInActive(true);
				}
				aJobBidStatus.setCode((String) aObj[1]);

				// boolean inactive = (Boolean) aObj[2];
				// aJobBidStatus.setInActive((byte) aObj[2]);
				aQueryList.add(aJobBidStatus);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;
		}
		return aQueryList;
	}

	/***
	 * Created by:Praveen kumar Purpose: Update allocated on creating invoice
	 * Date : 2014-10-30
	 * 
	 */
	@Override
	public Integer updateReleaseAllocated(Integer theJoReleaseID)
			throws JobException {
		Integer aCustPOId = null;
		Session aSession = null;
		List<?> lisQuery = null;
		
		Transaction aTransaction;
		try {
			aSession = itsSessionFactory.openSession();
			// aDetailSession.get(JoReleaseDetail.class, theJoReleaseID);
			String aSelectQry = "SELECT sum(CustomerInvoiceAmount) FROM joReleaseDetail WHERE joReleaseID = '"
					+ theJoReleaseID + "'";
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			lisQuery = aQuery.list();
			
			if (!lisQuery.isEmpty() && lisQuery.get(0) != null) {
				aTransaction = aSession.beginTransaction();
				// Integer billing = (Integer)aQuery.list().get(0).toString();
				JoRelease joRelease = (JoRelease) aSession.get(JoRelease.class,
						theJoReleaseID);
				joRelease.setEstimatedBilling(new BigDecimal(lisQuery.get(0).toString()));
				aTransaction.begin();
				aSession.update(joRelease);
				aTransaction.commit();
			}
			aSelectQry=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCustPOId;
	}
	
	@Override
	public BigDecimal getReleaseAllocatedAmt(Integer theJoReleaseID)
			throws JobException {
		BigDecimal ReleaseAllocatedAmt = BigDecimal.ZERO;
		Session aSession = null;
		List<?> lisQuery = null;
		
		Transaction aTransaction;
		try {
			aSession = itsSessionFactory.openSession();
			// aDetailSession.get(JoReleaseDetail.class, theJoReleaseID);
			String aSelectQry = "SELECT sum(CustomerInvoiceAmount) FROM joReleaseDetail WHERE joReleaseID = '"
					+ theJoReleaseID + "'";
			Query aQuery = aSession.createSQLQuery(aSelectQry);
			lisQuery = aQuery.list();
			
			if (!lisQuery.isEmpty() && lisQuery.get(0) != null) {
				ReleaseAllocatedAmt = (BigDecimal)lisQuery.get(0);
			}
			aSelectQry=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return ReleaseAllocatedAmt;
	}
	
	

	@Override
	public void updateCuInvoiceSubTotal(Integer cuInvoiceId, BigDecimal amount)
			throws JobException {
		Session acuInvoiceSession = null;
		Transaction aTransaction;

		try {
			acuInvoiceSession = itsSessionFactory.openSession();
			aTransaction = acuInvoiceSession.beginTransaction();
			aTransaction.begin();
			Cuinvoice cuInvoice = (Cuinvoice) acuInvoiceSession.get(
					Cuinvoice.class, cuInvoiceId);
			cuInvoice.setSubtotal(amount);

			acuInvoiceSession.update(cuInvoice);
			aTransaction.commit();
			if (cuInvoice.getJoReleaseDetailId() != null
					&& cuInvoice.getJoReleaseDetailId() != 0) {
				aTransaction = acuInvoiceSession.beginTransaction();
				aTransaction.begin();
				JoReleaseDetail joReleaseDetail = (JoReleaseDetail) acuInvoiceSession
						.get(JoReleaseDetail.class,
								cuInvoice.getJoReleaseDetailId());
				joReleaseDetail.setCustomerInvoiceAmount(amount);
				acuInvoiceSession.update(joReleaseDetail);
				aTransaction.commit();
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acuInvoiceSession.flush();
			acuInvoiceSession.close();
		}

	}

	@Override
	public void checAllocatedAmount(Integer joReleaseID) throws JobException {
		Session acuInvoiceSession = null;
		Transaction aTransaction;
		BigDecimal salesTotal = new BigDecimal(0);
		BigDecimal invoiceTotal = new BigDecimal(0);
		BigDecimal allocated = new BigDecimal(0);
		BigDecimal finalValueAllocated = new BigDecimal(0);
		acuInvoiceSession = itsSessionFactory.openSession();
		int res;
		try {
			if (joReleaseID != null && joReleaseID != 0) {
				aTransaction = acuInvoiceSession.beginTransaction();
				aTransaction.begin();
				String cuSOTotalQuery = "SELECT CostTotal FROM cuSO WHERE joReleaseID="
						+ joReleaseID + " ";
				Query acuSOQuery = acuInvoiceSession
						.createSQLQuery(cuSOTotalQuery);
				if (acuSOQuery.list().get(0) != null) {
					salesTotal = (BigDecimal) acuSOQuery.list().get(0);
				}

				JoRelease jorelease = (JoRelease) acuInvoiceSession.get(
						JoRelease.class, joReleaseID);
				allocated = jorelease.getEstimatedBilling();
				if (salesTotal.compareTo(allocated) == 1) {
					allocated = salesTotal;
				}
			
				String cuInvoiceQuery = "SELECT SUM(CustomerInvoiceAmount) FROM joReleaseDetail WHERE joReleaseID="
						+ joReleaseID + " group by joReleaseID";
				Query aQuery = acuInvoiceSession.createSQLQuery(cuInvoiceQuery);
				List<?> aList = aQuery.list();
				if (aList.get(0) != null) {
					invoiceTotal = (BigDecimal) aList.get(0);
				}
				
				if (invoiceTotal.compareTo(allocated) == 1) {
					allocated = invoiceTotal;
				}
				jorelease.setEstimatedBilling(allocated);
				aTransaction = acuInvoiceSession.beginTransaction();
				aTransaction.begin();
				acuInvoiceSession.update(jorelease);
				aTransaction.commit();

			}

		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acuInvoiceSession.flush();
			acuInvoiceSession.close();
		}

	}

	@Override
	public String getLastestInvoiceNumber(String jobNumberwithPrefix)
			throws JobException {
		Session acuInvoiceSession = null;
		Transaction aTransaction;
		String returninvoiceNumber = null;
		try {
			acuInvoiceSession = itsSessionFactory.openSession();
			aTransaction = acuInvoiceSession.beginTransaction();
			aTransaction.begin();
			String aSelectquery = "Select InvoiceNumber,cuInvoiceID from cuInvoice where InvoiceNumber LIKE '%"
					+ JobUtil.removeSpecialcharacterswithslash(jobNumberwithPrefix) + "%' ORDER  BY cuInvoiceID desc LIMIT 1";
			List<?> listdetails = acuInvoiceSession
					.createSQLQuery(aSelectquery).list();

			Iterator<?> aIterator = listdetails.iterator();
			int count = listdetails.size();
			if (listdetails.isEmpty()) {

				//jobNumberwithPrefix = jobNumberwithPrefix ;
				returninvoiceNumber = jobNumberwithPrefix+1;

			} else {
				Object[] aObj = (Object[]) aIterator.next();
				String invNumber = jobNumberwithPrefix;
				if (aObj[0] != null) {
					invNumber = (String) aObj[0];
				}
				char lastChar = invNumber.charAt(invNumber.length() - 1);
				int number = 0;
				if (Character.isDigit(lastChar)) {
					number = Integer.parseInt(lastChar + "");
					number++;
					invNumber = invNumber.substring(0, invNumber.length() - 1)
							+ "" + number;
				} else {
					number = count;
					number++;
					invNumber = invNumber + "" + number;
				}
				invNumber = invNumber.substring(0, invNumber.length() - 1) + ""
						+ number;
				returninvoiceNumber = invNumber;

			}
			aSelectquery=null;
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			acuInvoiceSession.flush();
			acuInvoiceSession.close();
		}

		return returninvoiceNumber;
	}

	@Override
	 public JobReleaseBean getBilledUnbilled(Integer theJoReleaseID)
	   throws JobException {
	  Session acuInvoiceSession = null;
	  Transaction aTransaction;
	  JobReleaseBean joReleasebean = new JobReleaseBean();
	  try {

	   acuInvoiceSession = itsSessionFactory.openSession();
	   aTransaction = acuInvoiceSession.beginTransaction();
	   if(theJoReleaseID!=null){
	   JoRelease joRelease = (JoRelease) acuInvoiceSession.get(
	     JoRelease.class, theJoReleaseID);
	   if(joRelease!=null)
	   {
	    aTransaction.begin();
	    String aSelectquery = "SELECT CAST(SUM(IFNULL(InvoiceAmount,0.0000)-IFNULL(cuInvoice.TaxAmount,0.0000)-IFNULL(Freight,0.0000)) AS DECIMAL (19,4)),"
	    		+ "CAST(SUM(IFNULL(Freight,0.0000)) AS DECIMAL (19,4)) FROM cuInvoice WHERE cuInvoice.joReleaseDetailID IN "
	      + "(SELECT joReleaseDetailID FROM joReleaseDetail where joReleaseID='"
	      + theJoReleaseID + "') ";
	    Query aQuery = acuInvoiceSession.createSQLQuery(aSelectquery);
	    BigDecimal billedAmount = new BigDecimal(0);
	    BigDecimal FreightAmt = new BigDecimal(0);
	    
	    Iterator<?> aIterator = aQuery.list().iterator();
	    while (aIterator.hasNext()) {
	     Object[] aObj = (Object[]) aIterator.next();
	     billedAmount = (BigDecimal) aObj[0];
	     FreightAmt = (BigDecimal) aObj[1];
	     billedAmount=billedAmount==null?new BigDecimal(0):billedAmount;
	     FreightAmt=FreightAmt==null?new BigDecimal(0):FreightAmt;
	    }
	 
	    if(joRelease.getEstimatedBilling()!=null)
	    joReleasebean.setEstimatedBilling(joRelease.getEstimatedBilling()
	      .subtract(billedAmount));
	    else
	    joReleasebean.setEstimatedBilling(new BigDecimal(0)
	       .subtract(billedAmount)); 
	    
	    joReleasebean.setInvoiceAmount(billedAmount);
	    joReleasebean.setFreight(FreightAmt);
	    aSelectquery=null;
	    }
	   }
	  } catch (Exception excep) {
	   itsLogger.error(excep.getMessage(), excep);
	   JobException aJobException = new JobException(excep.getMessage(),
	     excep);
	   throw aJobException;
	  } finally {
	   acuInvoiceSession.flush();
	   acuInvoiceSession.close();
	  }
	  return joReleasebean;
	 }

	@Override
	public String getvendorInvoiceNumber(Integer thejoReleaseDetailID)
			throws JobException {
		String aCustomerName = null;
		// Comments For:Show pro # should show from customer invoice inside the
		// job after created vendor invoice
		/*
		 * String aCustomerID =
		 * "SELECT InvoiceNumber FROM veBill WHERE joReleaseDetailID= '" +
		 * thejoReleaseDetailID + "'";
		 */
		String aCustomerID = "SELECT TrackingNumber FROM veBill WHERE joReleaseDetailID= '"
				+ thejoReleaseDetailID + "'";
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			if (thejoReleaseDetailID != null) {
				List<?> aList = aSession.createSQLQuery(aCustomerID).list();
				if(aList.size()>0){
				aCustomerName = (String) aList.get(0);
				}
				else{
					aCustomerName = "";
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerID=null;
		}
		return aCustomerName;
	}

	@Override
	public Integer createJoQuoteLineItemsProp(
			joQLineItemsProp thejoQLineItemsProp) {
		Session aSession = null;
		Integer joQuoteReciptID = null;
		try {
			aSession = itsSessionFactory.openSession();
			/*
			 * String Selectqry =
			 * "SELECT joQLineItemsPropId FROM joQLineItemsProp WHERE joQuoteDetailId= "
			 * + thejoQLineItemsProp.getJoQuoteDetailId(); try {
			 * 
			 * List<?> aList= aSession.createSQLQuery(Selectqry).list();
			 * if(aList!=null&&aList.size()!=0){ joQLineItemsPropId = (Integer)
			 * aList.get(0); }
			 * 
			 * } catch (Exception e) { itsLogger.error(e.getMessage(), e); }
			 */
			joQLineItemsProp objjoQLineItemsPropId = getjoQLineItemsProp(thejoQLineItemsProp
					.getJoQuoteDetailId());

			if (objjoQLineItemsPropId == null) {
				joQuoteReciptID = (Integer) aSession.save(thejoQLineItemsProp);
			} else {
				joQLineItemsProp ajoQLineItemsProp = (joQLineItemsProp) aSession
						.get(joQLineItemsProp.class,
								objjoQLineItemsPropId.getJoQLineItemsPropId());
				ajoQLineItemsProp.setItalicItem(thejoQLineItemsProp
						.isItalicItem());
				ajoQLineItemsProp.setItalicManufacturer(thejoQLineItemsProp
						.isItalicManufacturer());
				ajoQLineItemsProp.setItalicMult(thejoQLineItemsProp
						.isItalicMult());
				ajoQLineItemsProp.setItalicParagraph(thejoQLineItemsProp
						.isItalicParagraph());
				ajoQLineItemsProp.setItalicPrice(thejoQLineItemsProp
						.isItalicPrice());
				ajoQLineItemsProp.setItalicQuantity(thejoQLineItemsProp
						.isItalicQuantity());
				ajoQLineItemsProp.setItalicSpec(thejoQLineItemsProp
						.isItalicSpec());

				ajoQLineItemsProp.setUnderlineItem(thejoQLineItemsProp
						.isUnderlineItem());
				ajoQLineItemsProp.setUnderlineManufactur(thejoQLineItemsProp
						.isUnderlineManufactur());
				ajoQLineItemsProp.setUnderlineMult(thejoQLineItemsProp
						.isUnderlineMult());
				ajoQLineItemsProp.setUnderlineParagraph(thejoQLineItemsProp
						.isUnderlineParagraph());
				ajoQLineItemsProp.setUnderlinePrice(thejoQLineItemsProp
						.isUnderlinePrice());
				ajoQLineItemsProp.setUnderlineQuantity(thejoQLineItemsProp
						.isUnderlineQuantity());
				ajoQLineItemsProp.setUnderlineSpec(thejoQLineItemsProp
						.isUnderlineSpec());

				ajoQLineItemsProp.setBoldItem(thejoQLineItemsProp.isBoldItem());
				ajoQLineItemsProp.setBoldManufacturer(thejoQLineItemsProp
						.isBoldManufacturer());
				ajoQLineItemsProp.setBoldMult(thejoQLineItemsProp.isBoldMult());
				ajoQLineItemsProp.setBoldParagraph(thejoQLineItemsProp
						.isBoldParagraph());
				ajoQLineItemsProp.setBoldPrice(thejoQLineItemsProp
						.isBoldPrice());
				ajoQLineItemsProp.setBoldQuantity(thejoQLineItemsProp
						.isBoldQuantity());
				ajoQLineItemsProp.setBoldSpec(thejoQLineItemsProp.isBoldSpec());
				aSession.update(ajoQLineItemsProp);
				joQuoteReciptID = objjoQLineItemsPropId.getJoQLineItemsPropId();
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return joQuoteReciptID;
	}

	@Override
	public joQLineItemsProp getjoQLineItemsProp(Integer joquotedetailid) {
		Session aSession = null;
		Integer joQLineItemsPropId = 0;
		joQLineItemsProp ajoQLineItemsProp = null;
		try {
			aSession = itsSessionFactory.openSession();
			String Selectqry = "SELECT joQLineItemsPropId FROM joQLineItemsProp WHERE joQuoteDetailId= "
					+ joquotedetailid;
			try {

				List<?> aList = aSession.createSQLQuery(Selectqry).list();
				if (aList != null && aList.size() != 0) {
					joQLineItemsPropId = (Integer) aList.get(0);
				}

			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}
			ajoQLineItemsProp = (joQLineItemsProp) aSession.get(
					joQLineItemsProp.class, joQLineItemsPropId);

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return ajoQLineItemsProp;
	}

	@Override
	public Integer createJoQuoteLineItemsTempProp(
			joQLineItemTemplateProp thejoQLineItemsProp) {
		Session aSession = null;
		Integer joQuoteReciptID = null;
		try {
			aSession = itsSessionFactory.openSession();
			/*
			 * String Selectqry =
			 * "SELECT joQLineItemsPropId FROM joQLineItemsProp WHERE joQuoteDetailId= "
			 * + thejoQLineItemsProp.getJoQuoteDetailId(); try {
			 * 
			 * List<?> aList= aSession.createSQLQuery(Selectqry).list();
			 * if(aList!=null&&aList.size()!=0){ joQLineItemsPropId = (Integer)
			 * aList.get(0); }
			 * 
			 * } catch (Exception e) { itsLogger.error(e.getMessage(), e); }
			 */
			joQLineItemTemplateProp objjoQLineItemsPropId = getjoQLineItemsTempProp(thejoQLineItemsProp
					.getJoQuoteTemplateDetailId());

			if (objjoQLineItemsPropId == null) {
				joQuoteReciptID = (Integer) aSession.save(thejoQLineItemsProp);
			} else {
				joQLineItemTemplateProp ajoQLineItemsProp = (joQLineItemTemplateProp) aSession
						.get(joQLineItemTemplateProp.class,
								objjoQLineItemsPropId
										.getJoQLineItemsTempPropId());
				ajoQLineItemsProp.setItalicItem(thejoQLineItemsProp
						.isItalicItem());
				ajoQLineItemsProp.setItalicManufacturer(thejoQLineItemsProp
						.isItalicManufacturer());
				ajoQLineItemsProp.setItalicMult(thejoQLineItemsProp
						.isItalicMult());
				ajoQLineItemsProp.setItalicParagraph(thejoQLineItemsProp
						.isItalicParagraph());
				ajoQLineItemsProp.setItalicPrice(thejoQLineItemsProp
						.isItalicPrice());
				ajoQLineItemsProp.setItalicQuantity(thejoQLineItemsProp
						.isItalicQuantity());
				ajoQLineItemsProp.setItalicSpec(thejoQLineItemsProp
						.isItalicSpec());

				ajoQLineItemsProp.setUnderlineItem(thejoQLineItemsProp
						.isUnderlineItem());
				ajoQLineItemsProp.setUnderlineManufactur(thejoQLineItemsProp
						.isUnderlineManufactur());
				ajoQLineItemsProp.setUnderlineMult(thejoQLineItemsProp
						.isUnderlineMult());
				ajoQLineItemsProp.setUnderlineParagraph(thejoQLineItemsProp
						.isUnderlineParagraph());
				ajoQLineItemsProp.setUnderlinePrice(thejoQLineItemsProp
						.isUnderlinePrice());
				ajoQLineItemsProp.setUnderlineQuantity(thejoQLineItemsProp
						.isUnderlineQuantity());
				ajoQLineItemsProp.setUnderlineSpec(thejoQLineItemsProp
						.isUnderlineSpec());

				ajoQLineItemsProp.setBoldItem(thejoQLineItemsProp.isBoldItem());
				ajoQLineItemsProp.setBoldManufacturer(thejoQLineItemsProp
						.isBoldManufacturer());
				ajoQLineItemsProp.setBoldMult(thejoQLineItemsProp.isBoldMult());
				ajoQLineItemsProp.setBoldParagraph(thejoQLineItemsProp
						.isBoldParagraph());
				ajoQLineItemsProp.setBoldPrice(thejoQLineItemsProp
						.isBoldPrice());
				ajoQLineItemsProp.setBoldQuantity(thejoQLineItemsProp
						.isBoldQuantity());
				ajoQLineItemsProp.setBoldSpec(thejoQLineItemsProp.isBoldSpec());
				aSession.update(ajoQLineItemsProp);
				joQuoteReciptID = objjoQLineItemsPropId
						.getJoQLineItemsTempPropId();
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return joQuoteReciptID;
	}

	@Override
	public joQLineItemTemplateProp getjoQLineItemsTempProp(
			Integer joquotedetailid) {
		Session aSession = null;
		Integer joQLineItemsPropId = 0;
		joQLineItemTemplateProp ajoQLineItemsProp = null;
		try {
			aSession = itsSessionFactory.openSession();
			String Selectqry = "SELECT joQLineItemsTempPropId FROM joQLineItemsTempProp WHERE joQuoteTemplateDetailID= "
					+ joquotedetailid;
			try {

				List<?> aList = aSession.createSQLQuery(Selectqry).list();
				if (aList != null && aList.size() != 0) {
					joQLineItemsPropId = (Integer) aList.get(0);
				}

			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}
			ajoQLineItemsProp = (joQLineItemTemplateProp) aSession.get(
					joQLineItemTemplateProp.class, joQLineItemsPropId);
			Selectqry=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return ajoQLineItemsProp;
	}
	@Override
	public int getProductNoteCount(Integer joquoteheaderid,Integer type) {
		
		String query = "";
		if(type==0){
			query = "SELECT COUNT(ProductNote) FROM joQuoteDetail WHERE joQuoteHeaderID="+joquoteheaderid+" AND ProductNote!='' ";
		}else if(type==1){
			query = "SELECT COUNT(ProductNote) FROM joQuoteTemplateDetail WHERE joQuoteTemplateHeaderID="+joquoteheaderid+" AND ProductNote!='' ";
		}
		Session aSession = null;
		BigInteger aTotalCount = null;
	
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(query);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query=null;
		}
		return aTotalCount.intValue();
	}
	
	@Override
	public Rxcontact getContactDetails(Integer theContactID) {
		Rxcontact aRxcontact = new Rxcontact();
		Session aSession = null;
		try {
			if(theContactID==null)
			{
				theContactID =-1;
			}
			
			aSession = itsSessionFactory.openSession();
			aRxcontact = (Rxcontact) aSession.get(Rxcontact.class, theContactID);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxcontact;
	}
	
	@Override
	public CustomerPaymentBean getCuRecieptDetials(Integer theRecieptID) throws CustomerException {
		String aPaymentsselectQry = "SELECT "
				+ "cr.cuReceiptID, cr.rxCustomerID, cr.ReceiptDate, "
				+ "CONCAT(rm.Name, ' ', rm.FirstName) AS Customer, "
				+ "cr.Reference, cr.Memo, cr.Amount, cr.cuReceiptTypeID,cr.reversePaymentStatus FROM cuReceipt cr "
				+ " JOIN rxMaster rm ON rm.rxMasterID = cr.rxCustomerID where cr.cuReceiptID = "+theRecieptID;
		Session aSession = null;
		CustomerPaymentBean aCuPayment = null;
		try {			
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPaymentsselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuPayment = new CustomerPaymentBean();
				Object[] aObj = (Object[]) aIterator.next();
				aCuPayment.setCuReceiptID((Integer) aObj[0]);
				aCuPayment.setRxMasterID((Integer) aObj[1]);
				if (aObj[2] != null) {
					aCuPayment.setReceiptDate((String) DateFormatUtils.format(
							(Date) aObj[2], "MM/dd/yyyy"));
				}
				aCuPayment.setCustomer((String) aObj[3]);
				aCuPayment.setReference((String) aObj[4]);
				aCuPayment.setMemo((String) (aObj[5]==null?"":aObj[5]));
				aCuPayment.setAmount((BigDecimal) aObj[6]);
				aCuPayment.setCuReceiptTypeID((Short) aObj[7]);
				aCuPayment.setReversePaymentStatus((Boolean) aObj[8]);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CustomerException aCustomerException = new CustomerException(
					e.getMessage(), e);
			throw aCustomerException;
		} finally {
			aSession.flush();
			aSession.close();
			aPaymentsselectQry=null;
		}
		return aCuPayment;
	}
	
	public List<testforquotes> getTestQuotefromtable() throws JobException {
		Session aSession = null;
		List<testforquotes> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM  testforquotes");
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}
	
	@Override
	public Rxmaster getCustomerDetails(Integer rxMasterID) throws JobException {
		Session aSession = null;
		Rxmaster aRxmaster = null;
		itsLogger.info("getCustomerDetails: "+rxMasterID);
		try {
			aSession = itsSessionFactory.openSession();
			aRxmaster = (Rxmaster) aSession
					.get(Rxmaster.class, rxMasterID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aRxmaster;
	}
	@Override
	public Integer addjoquoteheader(JoQuoteHeader theJoQuoteHeader) throws JobException {
		Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Integer returnvalue=0;
		try {
			aTransaction = aJoQuoteHeaderSession.beginTransaction();
			aTransaction.begin();
			returnvalue=(Integer) aJoQuoteHeaderSession.save(theJoQuoteHeader);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoQuoteHeaderSession.flush();
			aJoQuoteHeaderSession.close();
		}
		return returnvalue;
	}
	
	@Override
	public boolean updatejoquoteheader(JoQuoteHeader theJoQuoteHeader) throws JobException {
		Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aJoQuoteHeaderSession.beginTransaction();
			aTransaction.begin();
			JoQuoteHeader joqh = (JoQuoteHeader) aJoQuoteHeaderSession.get(JoQuoteHeader.class,theJoQuoteHeader.getJoQuoteHeaderId());
			joqh.setCuMasterTypeID(theJoQuoteHeader.getCuMasterTypeID());
			joqh.setQuoteRev(theJoQuoteHeader.getQuoteRev());
			joqh.setCreatedById(theJoQuoteHeader.getCreatedById());
			joqh.setQuoteAmount(theJoQuoteHeader.getQuoteAmount());
			joqh.setInternalNote(theJoQuoteHeader.getInternalNote());
			joqh.setCreatedByName(theJoQuoteHeader.getCreatedByName());
			joqh.setCostAmount(theJoQuoteHeader.getCostAmount());
			joqh.setJoQuoteHeaderId(theJoQuoteHeader.getJoQuoteHeaderId());
			joqh.setPrintTotal(theJoQuoteHeader.getPrintTotal());
			joqh.setIncludeLSD(theJoQuoteHeader.getIncludeLSD());
			joqh.setDonotQtyitem2and3(theJoQuoteHeader.getDonotQtyitem2and3());
			joqh.setShowTotPriceonly(theJoQuoteHeader.getShowTotPriceonly());
			joqh.setLSDValue(theJoQuoteHeader.getLSDValue());
			aJoQuoteHeaderSession.update(joqh);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoQuoteHeaderSession.flush();
			aJoQuoteHeaderSession.close();
		}
		return true;
	}
	
	public int getNewQuotesCount(Integer joquoteheaderid) throws JobException {
		//String aJobCountStr = "SELECT IF(ISNULL (MAX(POSITION)),'0',MAX(POSITION)) as position  FROM joQuoteDetailMstr where joQuoteHeaderID="+ joquoteheaderid;
		String aJobCountStr = "SELECT MAX(POSITION) AS position FROM joQuoteDetailMstr where joQuoteHeaderID="+ joquoteheaderid;
		Session aSession = null;
		Integer aTotalCount = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = ConvertintoInteger(aList.get(0)==null?"0":aList.get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr=null;
		}
		return aTotalCount.intValue();
	}
	
	public int getNewQuotesCountForTemplate(Integer joQuoteTemplateHeaderID) throws JobException {
		//String aJobCountStr = "SELECT IF(ISNULL (MAX(POSITION)),'0',MAX(POSITION)) as position FROM joQuoteTempDetailMstr WHERE joQuoteTemplateHeaderID="+ joQuoteTemplateHeaderID;
		String aJobCountStr = "SELECT MAX(POSITION) as position FROM joQuoteTempDetailMstr WHERE joQuoteTemplateHeaderID="+ joQuoteTemplateHeaderID;
		Session aSession = null;
		Integer aTotalCount = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = ConvertintoInteger(aList.get(0)==null?"0":aList.get(0).toString());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr=null;
		}
		return aTotalCount.intValue();
	}
	
	/*@Override
	public boolean addjoQuoteDetailMstr(joQuoteDetailMstr thejoQuoteDetailMstr) throws JobException {
		Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		boolean returnvalue=true;
		int positionnumber=getNewQuotesCount(thejoQuoteDetailMstr.getJoQuoteHeaderID());
		thejoQuoteDetailMstr.setPosition(positionnumber+1);
		try {
			aTransaction = aJoQuoteHeaderSession.beginTransaction();
			aTransaction.begin();
			
			 aJoQuoteHeaderSession.save(thejoQuoteDetailMstr);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aJoQuoteHeaderSession.flush();
			aJoQuoteHeaderSession.close();
		}
		return returnvalue;
	}*/
	
	@Override
	public List<joQuoteDetailMstr> getjoQuoteDetailMstr(Integer joquoteheaderid) throws JobException {
		String aPOLineItemListQry = "";
		Session aSession = null;
		ArrayList<joQuoteDetailMstr> aQueryList = new ArrayList<joQuoteDetailMstr>();
		try {
			joQuoteDetailMstr ajoQuoteDetailMstr = null;
			aSession = itsSessionFactory.openSession();
			if (joquoteheaderid != null) {
				aPOLineItemListQry = "SELECT jmstr.joQuoteDetailMstrID,jmstr.joQuoteHeaderID,jmstr.type,jmstr.quantity,jmstr.textbox,jmstr.texteditor,jmstr.sellprice,jmstr.cost,jmstr.manufacturer,jmstr.position ,rxm.Name,jmstr.category,jmstr.linebreak "
									+"FROM joQuoteDetailMstr jmstr LEFT JOIN rxMaster rxm ON(jmstr.manufacturer=rxm.rxMasterID) WHERE  joQuoteHeaderID="+joquoteheaderid+" ORDER BY jmstr.position";
		
			Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
			List<?> aList = aQuery.list();
			if (aPOLineItemListQry.length() > 0)
				if (!aList.isEmpty()) {
					Iterator<?> aIterator = aList.iterator();
					while (aIterator.hasNext()) {
						ajoQuoteDetailMstr=new joQuoteDetailMstr();
						Object[] aObj = (Object[]) aIterator.next();
						Integer joquotedetailsmstrid=ConvertintoInteger(aObj[0].toString());
						Integer joquoteheaderID=ConvertintoInteger(aObj[1].toString());
						Integer type=ConvertintoInteger(aObj[2].toString());
						String Quantity="";
						if(aObj[3]!=null){
							Quantity=(String) aObj[3];
						}
						String textbox="";
						if(aObj[4]!=null){
							textbox=(String) aObj[4];
						}
						String texteditor="";
						if(aObj[5]!=null){
							texteditor=(String) aObj[5];
						}
						if(aObj[6]==null){
							aObj[6]="0";
						}
						BigDecimal sellprice=ConvertintoBigDecimal(aObj[6].toString());
						if(aObj[7]==null){
							aObj[7]="0";
						}
						if(aObj[8]==null){
							aObj[8]="0";
						}
						if(aObj[9]==null){
							aObj[9]="0";
						}
						BigDecimal cost=ConvertintoBigDecimal(aObj[7].toString());
						Integer manufacturer=ConvertintoInteger(aObj[8].toString());
						Integer position=ConvertintoInteger(aObj[9].toString());
						String vendorname="";
						if(aObj[10]!=null){
							vendorname=(String)aObj[10];
						}
						
						ajoQuoteDetailMstr.setJoQuoteDetailMstrID(joquotedetailsmstrid);
						ajoQuoteDetailMstr.setJoQuoteHeaderID(joquoteheaderID);
						ajoQuoteDetailMstr.setJoQuoteHeaderIDforimg(joquotedetailsmstrid);
						
						String typename="";
						if(type==1){
							typename="Title";
						}else if(type==2){
							typename="Item2";
						}else if(type==3){
							typename="Item3";
						}else if(type==4){
							typename="Price";
						}
						
						
						org.jsoup.nodes.Document doc = Jsoup.parse(texteditor);
						String text = doc.body().text();
						ajoQuoteDetailMstr.setTypename(typename);
						ajoQuoteDetailMstr.setType(type);
						ajoQuoteDetailMstr.setQuantity(Quantity);
						ajoQuoteDetailMstr.setTextbox(textbox);
						ajoQuoteDetailMstr.setTexteditor(texteditor);
						ajoQuoteDetailMstr.setDescription(text);
						ajoQuoteDetailMstr.setSellprice(sellprice);
						ajoQuoteDetailMstr.setCost(cost);
						ajoQuoteDetailMstr.setManufacturer(manufacturer);
						ajoQuoteDetailMstr.setPosition(position);
						ajoQuoteDetailMstr.setVendorname(vendorname);
						boolean linebreak=false;
						if(aObj[12]!=null && (Byte)aObj[12]==1){
							linebreak=true;
						}
						ajoQuoteDetailMstr.setLinebreak(linebreak);
						if(aObj[11]==null){
							aObj[11]="0";
						}
						ajoQuoteDetailMstr.setCategory(ConvertintoInteger(aObj[11].toString()));
						aQueryList.add(ajoQuoteDetailMstr);
					}
				}
		}
		} catch (Exception e) {
			itsLogger.error("Exception while getting the PO LineItem list: "
					+ e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
			aPOLineItemListQry=null;
		}
		return aQueryList;
	}
	public Integer ConvertintoInteger(String Stringvalue){
		Integer returnvalue=0;
		try {
			returnvalue=Integer.parseInt(Stringvalue);
		} catch (NumberFormatException e) {
			return returnvalue;
		}
		
		return returnvalue;
	}
	public BigDecimal ConvertintoBigDecimal(String Stringvalue){
		BigDecimal returnvalue=new BigDecimal(0);
		try {
			returnvalue=new BigDecimal(Stringvalue);
		} catch (Exception e) {
			return returnvalue;
		}
		
		return returnvalue;
	}
	@Override
	public boolean UpdatenewQuotes(joQuoteDetailMstr thejoQuoteDetailMstr,String Oper) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Date aDate = null;
		Integer aCustomer = null;
		try {
			if(Oper=="add"){
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.save(thejoQuoteDetailMstr);
				aTransaction.commit();
			}else if(Oper=="edit"){
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				joQuoteDetailMstr ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(joQuoteDetailMstr.class,thejoQuoteDetailMstr.getJoQuoteDetailMstrID());
				if(ajoQuoteDetailMstr!=null){
				if(thejoQuoteDetailMstr.getType()==1){
					ajoQuoteDetailMstr.setType(thejoQuoteDetailMstr.getType());
					ajoQuoteDetailMstr.setTexteditor(thejoQuoteDetailMstr.getTexteditor());
				}else if(thejoQuoteDetailMstr.getType()==2){
					ajoQuoteDetailMstr.setType(thejoQuoteDetailMstr.getType());
					ajoQuoteDetailMstr.setQuantity(thejoQuoteDetailMstr.getQuantity());
					ajoQuoteDetailMstr.setCost(thejoQuoteDetailMstr.getCost());
					ajoQuoteDetailMstr.setManufacturer(thejoQuoteDetailMstr.getManufacturer());
					ajoQuoteDetailMstr.setTexteditor(thejoQuoteDetailMstr.getTexteditor());
					ajoQuoteDetailMstr.setCategory(thejoQuoteDetailMstr.getCategory());
				}else if(thejoQuoteDetailMstr.getType()==3){
					ajoQuoteDetailMstr.setType(thejoQuoteDetailMstr.getType());
					ajoQuoteDetailMstr.setQuantity(thejoQuoteDetailMstr.getQuantity());
					ajoQuoteDetailMstr.setCost(thejoQuoteDetailMstr.getCost());
					ajoQuoteDetailMstr.setManufacturer(thejoQuoteDetailMstr.getManufacturer());
					ajoQuoteDetailMstr.setTexteditor(thejoQuoteDetailMstr.getTexteditor());
					ajoQuoteDetailMstr.setSellprice(thejoQuoteDetailMstr.getSellprice());
					ajoQuoteDetailMstr.setCategory(thejoQuoteDetailMstr.getCategory());
				}else if(thejoQuoteDetailMstr.getType()==4){
					ajoQuoteDetailMstr.setType(thejoQuoteDetailMstr.getType());
					ajoQuoteDetailMstr.setTextbox(thejoQuoteDetailMstr.getTextbox());
					ajoQuoteDetailMstr.setSellprice(thejoQuoteDetailMstr.getSellprice());
				}
				ajoQuoteDetailMstr.setJoQuoteHeaderID(ajoQuoteDetailMstr.getJoQuoteHeaderID());
				ajoQuoteDetailMstr.setPosition(thejoQuoteDetailMstr.getPosition());
				ajoQuoteDetailMstr.setLinebreak(thejoQuoteDetailMstr.isLinebreak());
				aSession.update(ajoQuoteDetailMstr);
				aTransaction.commit();
				}
			}else if(Oper=="del"){
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				joQuoteDetailMstr ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(joQuoteDetailMstr.class, thejoQuoteDetailMstr.getJoQuoteDetailMstrID());
				aSession.delete(ajoQuoteDetailMstr);
				aTransaction.commit();
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	
	@Override
	public joQuoteDetailMstr updatenewInlineItemPosition(
			joQuoteDetailMstr theJoQuoteDetail, String operation,
			Integer difference, Integer endQuoteDetailID) throws JobException {
		Session aSession = itsSessionFactory.openSession();
		joQuoteDetailMstr ajoQuoteDetailMstr = null;
		joQuoteDetailMstr theLastjoQuoteDetailMstr = null;
		joQuoteDetailMstr theFirstjoQuoteDetailMstr = null;
		String aSelectQry = "";
		if (operation.equalsIgnoreCase("upwards")) {
			itsLogger.info("Operation - Upwards");
			aSelectQry = "FROM joQuoteDetailMstr where joQuoteHeaderID ="
					+ theJoQuoteDetail.getJoQuoteHeaderID()
					+ " AND position BETWEEN " + endQuoteDetailID + " AND "
					+ theJoQuoteDetail.getJoQuoteDetailMstrID()
					+ " ORDER BY position";
		}
		if (operation.equalsIgnoreCase("downwards")) {
			itsLogger.info("Operation - DownWards");
			aSelectQry = "FROM joQuoteDetailMstr where joQuoteHeaderID ="
					+ theJoQuoteDetail.getJoQuoteHeaderID()
					+ " AND position BETWEEN "
					+ theJoQuoteDetail.getJoQuoteDetailMstrID() + " AND "
					+ endQuoteDetailID + " ORDER BY position";
		}
		try {
			Transaction aTransaction = aSession.beginTransaction();
			Query aQuery = aSession.createQuery(aSelectQry);

			// aQuery.setMaxResults(difference);

			List<joQuoteDetailMstr> aQuteDetails = aQuery.list();
			itsLogger.info("Size of the List : " + aQuteDetails.size());
			int sizeOfQuoteDetails = 0;
			int jval = 0;
			if (operation.equalsIgnoreCase("downwards")) {
				sizeOfQuoteDetails = aQuteDetails.size();
				jval = 1;
			}
			if (operation.equalsIgnoreCase("upwards")) {
				sizeOfQuoteDetails = aQuteDetails.size() - 1;
				jval = 0;
			}
			for (int j = jval; j < sizeOfQuoteDetails; j++) {
				if (operation.equalsIgnoreCase("downwards")) {
					aTransaction.begin();
					ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(
							joQuoteDetailMstr.class, aQuteDetails.get(j).getJoQuoteDetailMstrID());
					itsLogger.info("Quote Downwards Detail ID: "
							+ aQuteDetails.get(j).getJoQuoteDetailMstrID());
					itsLogger.info("Before Position: "
							+ aQuteDetails.get(j).getPosition());
					ajoQuoteDetailMstr.setPosition(aQuteDetails.get(j).getPosition()-1);
					itsLogger.info("After Position: "
							+ ajoQuoteDetailMstr.getPosition());
					aSession.update(ajoQuoteDetailMstr);
					aTransaction.commit();
				}
				if (operation.equalsIgnoreCase("upwards")) {
					aTransaction.begin();
					ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(
							joQuoteDetailMstr.class, aQuteDetails.get(j)
									.getJoQuoteDetailMstrID());
					itsLogger.info("Quote Upwards Detail ID: "
							+ aQuteDetails.get(j).getJoQuoteDetailMstrID());
					itsLogger.info("Before Position: "
							+ aQuteDetails.get(j).getPosition());
					ajoQuoteDetailMstr.setPosition((aQuteDetails.get(j)
							.getPosition()) + 1);
					itsLogger.info("After Position: "
							+ ajoQuoteDetailMstr.getPosition());
					aSession.update(ajoQuoteDetailMstr);
					aTransaction.commit();
				}
			}
			if (operation.equalsIgnoreCase("downwards")) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				theLastjoQuoteDetailMstr = aQuteDetails
						.get(aQuteDetails.size() - 1);
				theFirstjoQuoteDetailMstr = aQuteDetails.get(0);
				ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(
						joQuoteDetailMstr.class,
						theFirstjoQuoteDetailMstr.getJoQuoteDetailMstrID());
				itsLogger.info("Quote Detail ID: "
						+ theFirstjoQuoteDetailMstr.getJoQuoteDetailMstrID());
				ajoQuoteDetailMstr.setPosition(theFirstjoQuoteDetailMstr.getPosition()
						+ difference);
				itsLogger.info("New Position of The Current Value : "
						+ ajoQuoteDetailMstr.getPosition());
				aSession.update(ajoQuoteDetailMstr);
				aTransaction.commit();
			}
			if (operation.equalsIgnoreCase("upwards")) {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				theLastjoQuoteDetailMstr = aQuteDetails.get(aQuteDetails.size() - 1);
				theFirstjoQuoteDetailMstr = aQuteDetails.get(0);
				itsLogger.info("Quote Detail ID: "
						+ theLastjoQuoteDetailMstr.getJoQuoteDetailMstrID());
				ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(
						joQuoteDetailMstr.class,
						theLastjoQuoteDetailMstr.getJoQuoteDetailMstrID());
				ajoQuoteDetailMstr.setPosition((theLastjoQuoteDetailMstr.getPosition())
						- difference);
				aSession.update(ajoQuoteDetailMstr);
				aTransaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			JobException aJobException = new JobException(e.getMessage(), e);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return ajoQuoteDetailMstr;
	}
	
	
	@Override
	public boolean CopyFromQuoteToQuoteTemplate(Integer ajoQuoteHeaderID,String Template_Name) {
		Session aSession = null;
		joQLineItemTemplateProp ajoQLineItemsProp = null;
		try {
			//2014-11-26 17:22:37
			aSession = itsSessionFactory.openSession();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = new Date();
			String createddate= dateFormat.format(date);
			Integer joquoteTemplateHeaderID=JobUtil.getAutoIncrement(itsSessionFactory,"joQuoteTemplateHeader");
			String Selectqry = "INSERT INTO joQuoteTemplateHeader "+
					"(joQuoteTemplateHeaderID,TemplateName,cuMasterTypeID,QuoteRev,Remarks,DateCreated,CreatedByID,QuoteAmount,DiscountedPrice,QuoteYesNo1," +
					"DisplayQuantity,DisplayParagraph,DisplayManufacturer,DisplayCost,DisplayPrice,PrintQuantity,PrintParagraph,PrintManufacturer,"+
					"PrintCost,PrintPrice,NotesFullWidth,LineNumbers,GlobalTemplate,PrintTotal,CostAmount,DisplayMult,DisplaySpec,PrintMult,PrintSpec,"+
					"HidePrice,neworoldquote) "+
					"SELECT  "+joquoteTemplateHeaderID+" AS joquoteTemplateHeader ,'"+Template_Name+"' AS TemplateName,cuMasterTypeID,QuoteRev,Remarks,'"+createddate+"' as DateCreated,CreatedByID,QuoteAmount,DiscountedPrice,QuoteYesNo1,"+
					"DisplayQuantity,DisplayParagraph,DisplayManufacturer,DisplayCost,DisplayPrice,PrintQuantity,PrintParagraph,"+
					"PrintManufacturer,PrintCost,PrintPrice,NotesFullWidth,LineNumbers,'1' as globaltemplate,PrintTotal,CostAmount,DisplayMult,DisplaySpec,PrintMult,"+
					"PrintSpec,HidePrice,'1' as neworoldquote FROM joQuoteHeader WHERE joQuoteHeaderID="+ajoQuoteHeaderID;
			Query aQuery = aSession.createSQLQuery(Selectqry);
			
			
			CopyFromQuoteLIToQuoteTemplateLI(ajoQuoteHeaderID,joquoteTemplateHeaderID);
			try {
				aQuery.executeUpdate();
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}
			
			Selectqry=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
			
		}
		return true;
	}
	
	public boolean CopyFromQuoteLIToQuoteTemplateLI(Integer ajoQuoteHeaderID,Integer joquoteTemplateHeaderID)  {
		Statement st = null;	
		ConnectionProvider con=null;
		Connection conn=null;
			String Selectqry = "INSERT INTO joQuoteTempDetailMstr "+
								"(joQuoteTemplateHeaderID,type,quantity,textbox,texteditor,sellprice,cost,manufacturer,position,category,linebreak) "+
								"SELECT '"+joquoteTemplateHeaderID+"' as joquoteTemplateHeader,type,quantity,textbox,texteditor,sellprice,cost,manufacturer,@s:=@s+1 AS position,category,linebreak "+
								"FROM joQuoteDetailMstr,(SELECT @s:=(SELECT IF(ISNULL (MAX(POSITION)),'0',MAX(POSITION)) FROM joQuoteTempDetailMstr WHERE joQuoteTemplateHeaderID='"+joquoteTemplateHeaderID+"')) AS s WHERE joQuoteHeaderID="+ajoQuoteHeaderID +"  ORDER BY joQuoteDetailMstr.POSITION";
		
			try {
				con=itspdfService.connectionForJasper();
				conn=con.getConnection();
				st = conn.createStatement();
				st.executeUpdate(Selectqry);	
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}
			finally{
				try {
					st.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Selectqry=null;
			}
		return true;
	}
	
	
	
	@Override
	public boolean copyQuoteTemplateLIToQuoteLI(Integer ajoQuoteHeaderID,Integer joquoteTemplateHeaderID) {
		Statement st = null;	
		ConnectionProvider con=null;
		Connection conn=null;	
		String Selectqry = "INSERT INTO joQuoteDetailMstr "+
								"(joQuoteHeaderID,type,quantity,textbox,texteditor,sellprice,cost,manufacturer,position,category) "+
								"SELECT '"+ajoQuoteHeaderID+"' as joquoteTemplateHeader,type,quantity,textbox,texteditor,sellprice,cost,manufacturer,@s:=@s+1 AS position,category "+
								"FROM joQuoteTempDetailMstr,(SELECT @s:= (SELECT IF(ISNULL (MAX(POSITION)),'0',MAX(POSITION)) FROM joQuoteDetailMstr WHERE joQuoteHeaderID='"+ajoQuoteHeaderID+"')) AS s WHERE joQuoteTemplateHeaderID="+joquoteTemplateHeaderID+ " ORDER BY joQuoteTempDetailMstr.POSITION";
			
			try {
				con=itspdfService.connectionForJasper();
				conn=con.getConnection();
				st = conn.createStatement();
				st.executeUpdate(Selectqry);	
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}finally{
				try {
					st.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Selectqry=null;
			}
		return true;
	}
	/*@Override
	public boolean deleteJoNewQuoteDetail(Integer thejoQuoteDetailMstrID,Integer ajoQuoteHeaderID,Integer aposition)
			throws JobException {
		Session aSession = itsSessionFactory.openSession();
		Transaction aTransaction = null;
		joQuoteDetailMstr ajoQuoteDetailMstr = new joQuoteDetailMstr();
		try {
			if(thejoQuoteDetailMstrID!=null)
			{
				//boolean update_beforedel=WhiledeleteUpdatePosition(ajoQuoteHeaderID,aposition);
				ajoQuoteDetailMstr = (joQuoteDetailMstr) aSession.get(joQuoteDetailMstr.class, thejoQuoteDetailMstrID);
				
				if(ajoQuoteDetailMstr!=null)
				{
				itsLogger.info("["+ajoQuoteDetailMstr.getJoQuoteDetailMstrID()+"}");					
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aSession.delete(ajoQuoteDetailMstr);	
				aTransaction.commit();
				}
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(excep.getMessage(),
					excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return false;
	}*/
	/*@Override
	public boolean WhiledeleteUpdatePosition(Integer ajoQuoteHeaderID,Integer aposition) {
		Session aSession = null;
	
		try {
			//2014-11-26 17:22:37
			aSession = itsSessionFactory.openSession();
			String Selectqry = "UPDATE joQuoteDetailMstr SET position=position-1 WHERE joQuoteHeaderID="+ajoQuoteHeaderID+" AND position>"+aposition;
			Query aQuery = aSession.createSQLQuery(Selectqry);
			try {
				aQuery.executeUpdate();
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}
			
			Selectqry=null;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
			
		}
		return true;
	}*/
	
	
	//Annamalai code
	
		@Override
		public List<?> getjoQuotesCategoryList() throws JobException {
			Session aSession = null;
			List<JoQuoteCategory> aQueryList = new ArrayList<JoQuoteCategory>();
			String aCustomerQry = "SELECT * FROM joQuoteCategory order by description ASC ";
			JoQuoteCategory aJoQuoteCategory = null;
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aCustomerQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {

					aJoQuoteCategory = new JoQuoteCategory();
					Object[] aObj = (Object[]) aIterator.next();
					
					if(aObj != null && aObj[0] != null)
					{
						aJoQuoteCategory.setId((Integer) aObj[0]);
						aJoQuoteCategory.setDescription((String) aObj[1]);
					}
					// boolean inactive = (Boolean) aObj[2];
					// aJobBidStatus.setInActive((byte) aObj[2]);
					aQueryList.add(aJoQuoteCategory);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
				aCustomerQry=null;
			}
			return aQueryList;
		}
		
		@Override
		public JoQuoteCategory updateQuotesCategory(JoQuoteCategory theRxcontact, Boolean isAdd) throws JobException {
			itsLogger.debug("JoQuoteCategory >>>> "+isAdd);
			Session aSession = itsSessionFactory.openSession();
			JoQuoteCategory aJoQuoteCategory = null;
			try {
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				if (!isAdd) {
					aJoQuoteCategory = (JoQuoteCategory) aSession.get(JoQuoteCategory.class,
							theRxcontact.getId());
					aJoQuoteCategory.setDescription(theRxcontact.getDescription());
					aJoQuoteCategory.setId(theRxcontact.getId());
					aSession.update(aJoQuoteCategory);
				} else {
					aJoQuoteCategory = new JoQuoteCategory();
					aJoQuoteCategory.setDescription(theRxcontact.getDescription());
					aSession.save(aJoQuoteCategory);
				}
				aTransaction.commit();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aCompanyException = new JobException(e.getCause()
						.getMessage(), e);
				throw aCompanyException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aJoQuoteCategory;
		}

		@Override
		public void deleteQuotesCategory(Integer id) {
			itsLogger.debug("deleteQuotesCategory ");
			Session aSession = itsSessionFactory.openSession();
			JoQuoteCategory aJoQuoteCategory = null;
			try {
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aJoQuoteCategory = (JoQuoteCategory) aSession.get(JoQuoteCategory.class, id);
				aSession.delete(aJoQuoteCategory);
				aTransaction.commit();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aCompanyException = new JobException(e.getCause().getMessage(), e);

			} finally {
				aSession.flush();
				aSession.close();
			}
		}
		
		
		
		
		//Template Methods
		@Override
		public Integer addJoQuotetemplateHeader(JoQuotetemplateHeader theJoQuotetemplateHeader) throws JobException {
			Session aJoQuotetemplateHeaderSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			Integer returnvalue=0;
			try {
				aTransaction = aJoQuotetemplateHeaderSession.beginTransaction();
				aTransaction.begin();
				returnvalue=(Integer) aJoQuotetemplateHeaderSession.save(theJoQuotetemplateHeader);
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aJoQuotetemplateHeaderSession.flush();
				aJoQuotetemplateHeaderSession.close();
			}
			return returnvalue;
		}
		
		
		@Override
		public boolean UpdatenewQuotestemplate(joQuoteTempDetailMstr thejoQuoteTempDetailMstr) throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Date aDate = null;
			Integer aCustomer = null;
			try {
				
				joQuoteTempDetailMstr ajoQuoteTempDetailMstr = (joQuoteTempDetailMstr) aSession.get(joQuoteTempDetailMstr.class,thejoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
				
				if(ajoQuoteTempDetailMstr!=null)
				{
					Transaction aTransaction = aSession.beginTransaction();
					aTransaction.begin();

				if(thejoQuoteTempDetailMstr.getType()==1){
					ajoQuoteTempDetailMstr.setType(thejoQuoteTempDetailMstr.getType());
					ajoQuoteTempDetailMstr.setTexteditor(thejoQuoteTempDetailMstr.getTexteditor());
				}else if(thejoQuoteTempDetailMstr.getType()==2){
					ajoQuoteTempDetailMstr.setType(thejoQuoteTempDetailMstr.getType());
					ajoQuoteTempDetailMstr.setQuantity(thejoQuoteTempDetailMstr.getQuantity());
					ajoQuoteTempDetailMstr.setCost(thejoQuoteTempDetailMstr.getCost());
					ajoQuoteTempDetailMstr.setManufacturer(thejoQuoteTempDetailMstr.getManufacturer());
					ajoQuoteTempDetailMstr.setCategory(thejoQuoteTempDetailMstr.getCategory());
					ajoQuoteTempDetailMstr.setTexteditor(thejoQuoteTempDetailMstr.getTexteditor());
				}else if(thejoQuoteTempDetailMstr.getType()==3){
					ajoQuoteTempDetailMstr.setType(thejoQuoteTempDetailMstr.getType());
					ajoQuoteTempDetailMstr.setQuantity(thejoQuoteTempDetailMstr.getQuantity());
					ajoQuoteTempDetailMstr.setCost(thejoQuoteTempDetailMstr.getCost());
					ajoQuoteTempDetailMstr.setManufacturer(thejoQuoteTempDetailMstr.getManufacturer());
					ajoQuoteTempDetailMstr.setSellprice(thejoQuoteTempDetailMstr.getSellprice());
					ajoQuoteTempDetailMstr.setCategory(thejoQuoteTempDetailMstr.getCategory());
					ajoQuoteTempDetailMstr.setTexteditor(thejoQuoteTempDetailMstr.getTexteditor());
				}else if(thejoQuoteTempDetailMstr.getType()==4){
					ajoQuoteTempDetailMstr.setType(thejoQuoteTempDetailMstr.getType());
					ajoQuoteTempDetailMstr.setTextbox(thejoQuoteTempDetailMstr.getTextbox());
					ajoQuoteTempDetailMstr.setSellprice(thejoQuoteTempDetailMstr.getSellprice());
				}
				ajoQuoteTempDetailMstr.setJoQuoteTemplateHeaderID(ajoQuoteTempDetailMstr.getJoQuoteTemplateHeaderID());
				aSession.update(ajoQuoteTempDetailMstr);
				aTransaction.commit();
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		
		@Override
		public boolean addjoQuoteTempDetailMstr(joQuoteTempDetailMstr thejoQuoteTempDetailMstr) throws JobException {
			Session ajoQuoteTempDetailMstrSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			boolean returnvalue=true;
			int positionnumber=getNewQuotesCountForTemplate(thejoQuoteTempDetailMstr.getJoQuoteTemplateHeaderID());
			thejoQuoteTempDetailMstr.setPosition(positionnumber+1);
			try {
				aTransaction = ajoQuoteTempDetailMstrSession.beginTransaction();
				aTransaction.begin();
				
				ajoQuoteTempDetailMstrSession.save(thejoQuoteTempDetailMstr);
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				ajoQuoteTempDetailMstrSession.flush();
				ajoQuoteTempDetailMstrSession.close();
			}
			return returnvalue;
		}
		
		
		@Override
		public List<joQuoteTempDetailMstr> getjoQuoteTempDetailMstr(Integer quoteTemplateHeaderID) throws JobException {
			String aPOLineItemListQry = "";
			if (quoteTemplateHeaderID != null) {
				aPOLineItemListQry = "SELECT jmstr.joQuoteTempDetailMstrID,jmstr.joQuoteTemplateHeaderID,jmstr.type,jmstr.quantity,jmstr.textbox,jmstr.texteditor,jmstr.sellprice,jmstr.cost,jmstr.manufacturer,jmstr.position ,rxm.Name,jmstr.category,jmstr.linebreak "
									+"FROM joQuoteTempDetailMstr jmstr LEFT JOIN rxMaster rxm ON(jmstr.manufacturer=rxm.rxMasterID) WHERE  joQuoteTemplateHeaderID="+quoteTemplateHeaderID+" ORDER BY jmstr.position";
			}
			Session aSession = null;
			ArrayList<joQuoteTempDetailMstr> aQueryList = new ArrayList<joQuoteTempDetailMstr>();
			try {
				joQuoteTempDetailMstr ajoQuoteTempDetailMstr = null;
				aSession = itsSessionFactory.openSession();
				if (quoteTemplateHeaderID != null) {
				Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
				List<?> aList = aQuery.list();
				if (aPOLineItemListQry.length() > 0)
					if (!aList.isEmpty()) {
						Iterator<?> aIterator = aList.iterator();
						while (aIterator.hasNext()) {
							ajoQuoteTempDetailMstr=new joQuoteTempDetailMstr();
							Object[] aObj = (Object[]) aIterator.next();
							Integer joQuoteTempDetailMstrid=ConvertintoInteger(aObj[0].toString());
							Integer joQuoteTemplateHeaderID=ConvertintoInteger(aObj[1].toString());
							Integer type=ConvertintoInteger(aObj[2].toString());
							String Quantity="";
							if(aObj[3]!=null){
								Quantity=(String) aObj[3];
							}
							String textbox="";
							if(aObj[4]!=null){
								textbox=(String) aObj[4];
							}
							String texteditor="";
							if(aObj[5]!=null){
								texteditor=(String) aObj[5];
							}
							if(aObj[6]==null){
								aObj[6]="";
							}
							BigDecimal sellprice=ConvertintoBigDecimal(aObj[6].toString());
							if(aObj[7]==null){
								aObj[7]="";
							}
							BigDecimal cost=ConvertintoBigDecimal(aObj[7].toString());
							Integer manufacturer=ConvertintoInteger(aObj[8].toString());
							Integer position=ConvertintoInteger(aObj[9].toString());
							String vendorname="";
							if(aObj[10]!=null){
								vendorname=(String)aObj[10];
							}
							
							ajoQuoteTempDetailMstr.setJoQuoteTempDetailMstrID(joQuoteTempDetailMstrid);
							ajoQuoteTempDetailMstr.setJoQuoteTemplateHeaderID(joQuoteTemplateHeaderID);
							ajoQuoteTempDetailMstr.setJoQuoteTemplateHeaderIDforimg(joQuoteTempDetailMstrid);
							
							String typename="";
							if(type==1){
								typename="Title";
							}else if(type==2){
								typename="Item2";
							}else if(type==3){
								typename="Item3";
							}else if(type==4){
								typename="Price";
							}
							
							
							org.jsoup.nodes.Document doc = Jsoup.parse(texteditor);
							String text = doc.body().text();
							ajoQuoteTempDetailMstr.setTypename(typename);
							ajoQuoteTempDetailMstr.setType(type);
							ajoQuoteTempDetailMstr.setQuantity(Quantity);
							ajoQuoteTempDetailMstr.setTextbox(textbox);
							ajoQuoteTempDetailMstr.setTexteditor(texteditor);
							ajoQuoteTempDetailMstr.setDescription(text);
							ajoQuoteTempDetailMstr.setSellprice(sellprice);
							ajoQuoteTempDetailMstr.setCost(cost);
							ajoQuoteTempDetailMstr.setManufacturer(manufacturer);
							ajoQuoteTempDetailMstr.setPosition(position);
							ajoQuoteTempDetailMstr.setVendorname(vendorname);
							if(aObj[11]==null){
								aObj[11]=-1;
							}
							ajoQuoteTempDetailMstr.setCategory(ConvertintoInteger(aObj[11].toString()));
							boolean linebreak=false;
							if(aObj[12]!=null && (Byte)aObj[12]==1){
								linebreak=true;
							}
							ajoQuoteTempDetailMstr.setLinebreak(linebreak);
							aQueryList.add(ajoQuoteTempDetailMstr);
						}
					}
				}
			} catch (Exception e) {
				itsLogger.error("Exception while getting the PO LineItem list: "
						+ e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aPOLineItemListQry=null;
			}
			return aQueryList;
		}
		
		@Override
		public boolean deleteJoNewQuoteDetail_template(Integer thejoQuoteTempDetailMstrID,Integer ajoQuotetempHeaderID,Integer aposition)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			joQuoteTempDetailMstr ajoQuoteTempDetailMstr = new joQuoteTempDetailMstr();
			try {
				//boolean update_beforedel=WhiledeleteUpdatePosition_template(ajoQuotetempHeaderID,aposition);
				
				ajoQuoteTempDetailMstr = (joQuoteTempDetailMstr) aSession.get(
						joQuoteTempDetailMstr.class, thejoQuoteTempDetailMstrID);
				aSession.delete(ajoQuoteTempDetailMstr);
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return false;
		}
		
		@Override
		public boolean WhiledeleteUpdatePosition_template(Integer ajoQuotetempHeaderID,Integer aposition) {
			Session aSession = null;
			try {
				//2014-11-26 17:22:37
				aSession = itsSessionFactory.openSession();
				String Selectqry = "UPDATE joQuoteTempDetailMstr SET position=position-1 WHERE joQuoteTemplateHeaderID="+ajoQuotetempHeaderID+" AND position>"+aposition;
				Query aQuery = aSession.createSQLQuery(Selectqry);
				try {
					aQuery.executeUpdate();
					
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				}
				
				Selectqry=null;
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);

			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		@Override
        public joQuoteTempDetailMstr updatenewInlineItemPosition_template(
                joQuoteTempDetailMstr theJoQuoteDetail, String operation,
                Integer difference, Integer endQuoteDetailID) throws JobException {
            Session aSession = itsSessionFactory.openSession();
            joQuoteTempDetailMstr ajoQuoteTempDetailMstr = null;
            joQuoteTempDetailMstr theLastjoQuoteTempDetailMstr = null;
            joQuoteTempDetailMstr theFirstjoQuoteTempDetailMstr = null;
            String aSelectQry = "";
            if (operation.equalsIgnoreCase("upwards")) {
                itsLogger.info("Operation - Upwards");
                aSelectQry = "FROM joQuoteTempDetailMstr where joQuoteTemplateHeaderID ="
                        + theJoQuoteDetail.getJoQuoteTemplateHeaderID()
                        + " AND position BETWEEN " + endQuoteDetailID + " AND "
                        + theJoQuoteDetail.getJoQuoteTempDetailMstrID()
                        + " ORDER BY position";
            }
            if (operation.equalsIgnoreCase("downwards")) {
                itsLogger.info("Operation - DownWards");
                aSelectQry = "FROM joQuoteTempDetailMstr where joQuoteTemplateHeaderID ="
                        + theJoQuoteDetail.getJoQuoteTemplateHeaderID()
                        + " AND position BETWEEN "
                        + theJoQuoteDetail.getJoQuoteTempDetailMstrID() + " AND "
                        + endQuoteDetailID + " ORDER BY position";
            }
            try {
                Transaction aTransaction = aSession.beginTransaction();
                Query aQuery = aSession.createQuery(aSelectQry);

                // aQuery.setMaxResults(difference);

                List<joQuoteTempDetailMstr> aQuteDetails = aQuery.list();
                itsLogger.info("Size of the List : " + aQuteDetails.size());
                int sizeOfQuoteDetails = 0;
                int jval = 0;
                if (operation.equalsIgnoreCase("downwards")) {
                    sizeOfQuoteDetails = aQuteDetails.size();
                    jval = 1;
                }
                if (operation.equalsIgnoreCase("upwards")) {
                    sizeOfQuoteDetails = aQuteDetails.size() - 1;
                    jval = 0;
                }
                for (int j = jval; j < sizeOfQuoteDetails; j++) {
                    if (operation.equalsIgnoreCase("downwards")) {
                        aTransaction.begin();
                        ajoQuoteTempDetailMstr = (joQuoteTempDetailMstr) aSession.get(
                                joQuoteTempDetailMstr.class, aQuteDetails.get(j).getJoQuoteTempDetailMstrID());
                        itsLogger.info("Quote Downwards Detail ID: "
                                + aQuteDetails.get(j).getJoQuoteTempDetailMstrID());
                        itsLogger.info("Before Position: "
                                + aQuteDetails.get(j).getPosition());
                        ajoQuoteTempDetailMstr.setPosition(aQuteDetails.get(j).getPosition()-1);
                        itsLogger.info("After Position: "
                                + ajoQuoteTempDetailMstr.getPosition());
                        aSession.update(ajoQuoteTempDetailMstr);
                        aTransaction.commit();
                    }
                    if (operation.equalsIgnoreCase("upwards")) {
                        aTransaction.begin();
                        ajoQuoteTempDetailMstr = (joQuoteTempDetailMstr) aSession.get(
                                joQuoteTempDetailMstr.class, aQuteDetails.get(j)
                                        .getJoQuoteTempDetailMstrID());
                        itsLogger.info("Quote Upwards Detail ID: "
                                + aQuteDetails.get(j).getJoQuoteTempDetailMstrID());
                        itsLogger.info("Before Position: "
                                + aQuteDetails.get(j).getPosition());
                        ajoQuoteTempDetailMstr.setPosition((aQuteDetails.get(j)
                                .getPosition()) + 1);
                        itsLogger.info("After Position: "
                                + ajoQuoteTempDetailMstr.getPosition());
                        aSession.update(ajoQuoteTempDetailMstr);
                        aTransaction.commit();
                    }
                }
                if (operation.equalsIgnoreCase("downwards")) {
                    aTransaction = aSession.beginTransaction();
                    aTransaction.begin();
                    theLastjoQuoteTempDetailMstr = aQuteDetails.get(aQuteDetails.size() - 1);
                    theFirstjoQuoteTempDetailMstr = aQuteDetails.get(0);
                    ajoQuoteTempDetailMstr = (joQuoteTempDetailMstr) aSession.get(
                            joQuoteTempDetailMstr.class,
                            theFirstjoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
                    itsLogger.info("Quote Detail ID: "
                            + theFirstjoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
                    ajoQuoteTempDetailMstr.setPosition(theFirstjoQuoteTempDetailMstr.getPosition()
                            + difference);
                    itsLogger.info("New Position of The Current Value : "
                            + ajoQuoteTempDetailMstr.getPosition());
                    aSession.update(ajoQuoteTempDetailMstr);
                    aTransaction.commit();
                }
                if (operation.equalsIgnoreCase("upwards")) {
                    aTransaction = aSession.beginTransaction();
                    aTransaction.begin();
                    theLastjoQuoteTempDetailMstr = aQuteDetails.get(aQuteDetails.size() - 1);
                    theFirstjoQuoteTempDetailMstr = aQuteDetails.get(0);
                    itsLogger.info("Quote Detail ID: "
                            + theLastjoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
                    ajoQuoteTempDetailMstr = (joQuoteTempDetailMstr) aSession.get(
                            joQuoteTempDetailMstr.class,
                            theLastjoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
                    ajoQuoteTempDetailMstr.setPosition((theLastjoQuoteTempDetailMstr.getPosition())
                            - difference);
                    aSession.update(ajoQuoteTempDetailMstr);
                    aTransaction.commit();
                }
            } catch (Exception e) {
                itsLogger.error(e.getMessage(), e);
                JobException aJobException = new JobException(e.getMessage(), e);
                throw aJobException;
            } finally {
                aSession.flush();
                aSession.close();
                aSelectQry=null;
            }
            return ajoQuoteTempDetailMstr;
        }
		
		
		@Override
		public JoQuotetemplateHeader getJoQuotetemplateHeader(Integer joQuoteTemplateHeaderID)
				throws JobException {
			String aSelectQry = "SELECT jqth.joQuoteTemplateHeaderID ,jqth.TemplateName,jqth.cuMasterTypeID,jqth.CreatedByID,jqth.QuoteAmount,tsu.FullName,jqth.PrintTotal FROM joQuoteTemplateHeader jqth LEFT JOIN tsUserLogin tsu ON(tsu.UserLoginID=jqth.CreatedByID) WHERE joQuoteTemplateHeaderID="+joQuoteTemplateHeaderID;
				
			Session aSession = null;
			JoQuotetemplateHeader aJoQuotetemplateHeader= new JoQuotetemplateHeader();
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aSelectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					
					aJoQuotetemplateHeader.setJoQuoteTemplateHeaderId(ConvertintoInteger(aObj[0].toString()));
					aJoQuotetemplateHeader.setTemplateName((String)aObj[1]);
					aJoQuotetemplateHeader.setCuMasterTypeId(ConvertintoInteger(aObj[2].toString()));
					aJoQuotetemplateHeader.setCreatedById(ConvertintoInteger(aObj[3].toString()));
					//aJoQuotetemplateHeader.setQuoteAmount(ConvertintoBigDecimal(aObj[4].toString()));
					if(aObj[5]!=null){
						aJoQuotetemplateHeader.setCreatedByName((String) aObj[5]);
					}else{
						aJoQuotetemplateHeader.setCreatedByName("  ");
					}
					
					if(aObj[4]==null){
						aObj[4]="";
					}
					aJoQuotetemplateHeader.setQuoteAmount(ConvertintoBigDecimal(aObj[4].toString()).setScale(2));
					aJoQuotetemplateHeader.setPrintTotal((Byte) aObj[6]);
				}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aSelectQry=null;
			}
			return aJoQuotetemplateHeader;
		}
		
		
		@Override
		public Integer saveJoquoteHistory(Joquotehistory ajoquotehistory) throws JobException {
			Session ajoQuotehistorySession = itsSessionFactory.openSession();
			Transaction aTransaction;
			Integer returnvalue=0;
			try {
				aTransaction = ajoQuotehistorySession.beginTransaction();
				aTransaction.begin();
				returnvalue=(Integer) ajoQuotehistorySession.save(ajoquotehistory);
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getCause().getMessage(), excep);
				throw new JobException(excep.getMessage(), excep);
			} finally {
				ajoQuotehistorySession.flush();
				ajoQuotehistorySession.close();
			}
			return returnvalue;
		}
		@Override
		public Jomaster getJoMasterDetails(Integer joreleaseID)
				throws JobException {
			Session aSession = null;
			Jomaster aJoMaster = null;
			try {
				JoRelease aJorelease = getJoMasterIDFromJoreleaseID(joreleaseID);
				aSession = itsSessionFactory.openSession();
					aJoMaster = (Jomaster) aSession.get(Jomaster.class, aJorelease.getJoMasterId());
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aJoMaster;
		}
		public JoRelease getJoMasterIDFromJoreleaseID(Integer joreleaseID)
				throws JobException {
			Session aSession = null;
			JoRelease aJoRelease = null;
			try {
				aSession = itsSessionFactory.openSession();
					aJoRelease = (JoRelease) aSession
							.get(JoRelease.class, joreleaseID);
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aJoRelease;
		}
		@Override
		public Rxcontact ContactsBasedonID(Integer ContactID)
				throws JobException {
			Session aSession = null;
			Rxcontact aRxcontact = null;
			try {
				aSession = itsSessionFactory.openSession();
					aRxcontact = (Rxcontact) aSession
							.get(Rxcontact.class, ContactID);
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aRxcontact;
		}
		@Override
		public JoRelease getJoRelease(Integer aJoReleaseID)
				throws JobException {
			Session aSession = null;
			JoRelease aJoRelease = null;
			try {
				aSession = itsSessionFactory.openSession();
				
				if(aJoReleaseID !=null && !aJoReleaseID.equals("")){
					aJoRelease = (JoRelease) aSession
							.get(JoRelease.class, aJoReleaseID);
				}else{
					aJoRelease = new JoRelease();
					aJoRelease.setReleaseType(0);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aJoRelease;
		}

		
		public List<JoRelease> getjoreleasebyjomasterid(Integer jomasterid) throws JobException {
			Session aSession = null;
			List<JoRelease> aQueryList = null;
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createQuery("FROM  JoRelease where joMasterID="+jomasterid);
				aQueryList = aQuery.list();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aQueryList;
		}
		public void updatejorealseseqnumber() throws JobException{
			List<Jomaster> ajomaster=getAll();
			for(Jomaster thejom:ajomaster){
				List<JoRelease> ajorelease=getjoreleasebyjomasterid(thejom.getJoMasterId());
				int i=1;
				for(JoRelease thejorelease:ajorelease){
					JoRelease newjorelease=new JoRelease();
					newjorelease.setJoReleaseId(thejorelease.getJoReleaseId());
					newjorelease.setSeq_Number(i);
					edittheJoRelease(newjorelease);
					i=i+1;
				}
				
			}
		}
		
		
		public JoRelease edittheJoRelease(JoRelease theJoRelease)  {
			Session aSession = itsSessionFactory.openSession();
			JoRelease aJoRelease = null;
			try {
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				aJoRelease = (JoRelease) aSession.get(JoRelease.class,theJoRelease.getJoReleaseId());
				aJoRelease.setSeq_Number(theJoRelease.getSeq_Number());
				aSession.update(aJoRelease);
				aTransaction.commit();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aJoRelease;
		}
		
		public Integer getJoReleaseCountBasedonjomaster(Integer jomasterID) throws JobException {
			String aJobCountStr = "SELECT IFNULL(MAX(seq_Number),0) FROM joRelease WHERE joMasterID="+jomasterID;
			Session aSession = null;
			BigInteger aTotalCount = null;
			try {
				// Retrieve aSession from Hibernate
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aJobCountStr);
				List<?> aList = aQuery.list();
				aTotalCount = (BigInteger) aList.get(0);
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
				aJobCountStr=null;
			}
			return aTotalCount.intValue();
		}
		public boolean WhiledeleteUpdateseqnumber_jorelease(Integer jomasterid,Integer seqnumber) {
			Session aSession = null;
			try {
				//2014-11-26 17:22:37
				aSession = itsSessionFactory.openSession();
				String Selectqry = "UPDATE joRelease SET seq_Number=seq_Number-1 WHERE joMasterID="+jomasterid+" AND seq_Number>"+seqnumber;
				Query aQuery = aSession.createSQLQuery(Selectqry);
				try {
					aQuery.executeUpdate();
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				}
				
				Selectqry=null;
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);

			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		@Override
		public boolean UpDateCreditInfoDetails(Jomaster theJomaster)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			try {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Jomaster aJomaster = (Jomaster) aSession.get(Jomaster.class,theJomaster.getJoMasterId());
				aJomaster.setCreditStatus(theJomaster.getCreditStatus());
				aJomaster.setCreditStatusDate(theJomaster.getCreditStatusDate());
				aJomaster.setWho0(theJomaster.getWho0());
				aSession.update(aJomaster);
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return false;
		}
		
		@Override
		public boolean updateVepoDetailquantity(Vepodetail theVepodetail)
				throws JobException {
			Session aVepoSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			try {
				aTransaction = aVepoSession.beginTransaction();
				aTransaction.begin();
				Vepodetail aVepodet = (Vepodetail) aVepoSession.get(Vepodetail.class,
						theVepodetail.getVePodetailId());
				aVepodet.setQuantityOrdered(aVepodet.getQuantityOrdered().add(theVepodetail.getQuantityOrdered()));
				BigDecimal totalamount=aVepodet.getUnitCost().multiply(theVepodetail.getQuantityOrdered());
				if(aVepodet.getPriceMultiplier().compareTo(BigDecimal.ZERO)==1){
					totalamount=totalamount.multiply(aVepodet.getPriceMultiplier());
				}
				aVepoSession.update(aVepodet);
				updateVepototalAndSubtotal(theVepodetail,aVepodet,totalamount);
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getCause()
						.getMessage(), excep);
				throw aJobException;
			} finally {
				aVepoSession.flush();
				aVepoSession.close();
			}
			return true;
		}
		public boolean updateVepototalAndSubtotal(Vepodetail gridVepodetail,Vepodetail theVepodetail,BigDecimal totalamount)
				throws JobException {
			Session aVepoSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			try {
				aTransaction = aVepoSession.beginTransaction();
				aTransaction.begin();
				Vepo aVepo = (Vepo) aVepoSession.get(Vepo.class, gridVepodetail.getVePoid());
				BigDecimal taxrate=aVepo.getTaxTotal().multiply(new BigDecimal(100)).divide(aVepo.getSubtotal());
				aVepo.setSubtotal(aVepo.getSubtotal().add(totalamount));
				if(theVepodetail.getTaxable()){
				if(taxrate.compareTo(BigDecimal.ZERO)==1){
					aVepo.setTaxTotal(taxrate.multiply(aVepo.getSubtotal()).divide(new BigDecimal(100)));
				}
				}
				//aVepo.setTotalAmount(aVepo.getSubtotal().add(aVepo.getFreight().add(aVepo.getTaxTotal())));
				aVepoSession.update(aVepo);
				
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getCause()
						.getMessage(), excep);
				throw aJobException;
			} finally {
				aVepoSession.flush();
				aVepoSession.close();
			}
			return true;
		}
	
		
		@Override
		public Integer createvepodetailfromvendorinvoice(Vepodetail theVepodetail) throws JobException {
			Session aSession = null;
			Transaction aTransaction=null;
			Integer vepodetailid=0;
			try {
				aSession = itsSessionFactory.openSession();
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				
				vepodetailid=(Integer) aSession.save(theVepodetail);
				aTransaction.commit();
				updateVepototalAndSubtotal(theVepodetail,theVepodetail,theVepodetail.getQuantityBilled());
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return vepodetailid;
		}
		
		@Override
		public Cuinvoice getCustomerinvoicejobDetail(Cuinvoice aCuinvoice)
				throws JobException {
			String aJobSelectQry = "SELECT jo.cuAssignmentID0,jo.cuAssignmentID1,jo.cuAssignmentID2,jo.cuAssignmentID3,"
									+"jo.cuAssignmentID4,rxc.cuTermsID,cut.Description,jo.coTaxTerritoryID,ctax.County,jo.coDivisionID,"
									+"tsu0.FullName AS cuAssignmentName0,tsu1.FullName AS cuAssignmentName1, "
									+"tsu2.FullName AS cuAssignmentName2, tsu3.FullName AS cuAssignmentName3, "
									+"tsu4.FullName AS cuAssignmentName4   "
									+"FROM joMaster jo LEFT JOIN cuMaster rxc ON(jo.rxCustomerID=rxc.cuMasterID) " 
									+"LEFT JOIN tsUserLogin tsu0 ON(tsu0.UserLoginID=jo.cuAssignmentID0) "
									+"LEFT JOIN tsUserLogin tsu1 ON(tsu1.UserLoginID=jo.cuAssignmentID1) "
									+"LEFT JOIN tsUserLogin tsu2 ON(tsu2.UserLoginID=jo.cuAssignmentID2) "
									+"LEFT JOIN tsUserLogin tsu3 ON(tsu3.UserLoginID=jo.cuAssignmentID3) "
									+"LEFT JOIN tsUserLogin tsu4 ON(tsu4.UserLoginID=jo.cuAssignmentID4) "
									+"LEFT JOIN cuTerms cut ON(rxc.cuTermsID=cut.cuTermsID) "
									+"LEFT JOIN coTaxTerritory ctax ON(ctax.coTaxTerritoryID=jo.coTaxTerritoryID)"
									+"WHERE jo.joMasterID="+aCuinvoice.getJoMasterID();
			Session aSession = null;
			Cuinvoice aCuinvoice2=new Cuinvoice();
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aJobSelectQry);
				/**
				 * In general you are not supposed to get the column names.
				 * Actually, that's exactly what an O2R mapper is about - hiding the
				 * R details and showing only O specifics.
				 **/
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aCuinvoice2.setCuAssignmentId0(aObj[0]!=null?JobUtil.ConvertintoInteger(aObj[0].toString()):new Integer(0));
					aCuinvoice2.setCuAssignmentId1(aObj[1]!=null?JobUtil.ConvertintoInteger(aObj[1].toString()):new Integer(0));
					aCuinvoice2.setCuAssignmentId2(aObj[2]!=null?JobUtil.ConvertintoInteger(aObj[2].toString()):new Integer(0));
					aCuinvoice2.setCuAssignmentId3(aObj[3]!=null?JobUtil.ConvertintoInteger(aObj[3].toString()):new Integer(0));
					aCuinvoice2.setCuAssignmentId4(aObj[4]!=null?JobUtil.ConvertintoInteger(aObj[4].toString()):new Integer(0));
					aCuinvoice2.setCuTermsId(aObj[5]!=null ?JobUtil.ConvertintoInteger(aObj[5].toString()):new Integer(0));
					aCuinvoice2.setDescription(aObj[6]!=null?(String) aObj[6]:"");
					aCuinvoice2.setCoTaxTerritoryId(aObj[7]!=null?JobUtil.ConvertintoInteger(aObj[7].toString()):new Integer(0));
					aCuinvoice2.setCotaxdescription(aObj[8]!=null?(String) aObj[8]:"");
					aCuinvoice2.setCoDivisionId(JobUtil.ConvertintoInteger(aObj[9].toString()));
					aCuinvoice2.setCuAssignmentName0(aObj[10]!=null ?(String) aObj[10]:"");
					aCuinvoice2.setCuAssignmentName1(aObj[11]!=null ?(String) aObj[11]:"");
					aCuinvoice2.setCuAssignmentName2(aObj[12]!=null ?(String) aObj[12]:"");
					aCuinvoice2.setCuAssignmentName3(aObj[13]!=null ?(String) aObj[13]:"");
					aCuinvoice2.setCuAssignmentName4(aObj[14]!=null ?(String) aObj[14]:"");
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aJobSelectQry=null;
			}
			return aCuinvoice2;
		}

		@Override
		public void updatecuMasterOverriteCreditHold(Integer customerid)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Cumaster cuMaster = null;
			try {
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				cuMaster = (Cumaster) aSession.get(Cumaster.class,customerid);
				cuMaster.setCreditHoldOverride(null);
				aSession.update(cuMaster);
				aTransaction.commit();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			
		}
		@Override
		public boolean getjoquotedetailrtftohtml(Integer betweenfrom,Integer betweento) throws JobException {
			Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
			List<JoQuoteDetail> jdetlst=null;
			try {
				//SELECT * FROM joQuoteDetail  WHERE joQuoteDetailID BETWEEN 272090 AND 300000
				//Query aQuery = aJoQuoteHeaderSession.createQuery("select joQuoteDetailID FROM  JoQuoteDetail where joQuoteDetailID BETWEEN "+betweenfrom+" AND "+betweento);
				Query aQuery = aJoQuoteHeaderSession.createSQLQuery("select joQuoteDetailID,joQuoteHeaderID FROM  joQuoteDetail");
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
				//for(int i=272090;i<1605774;i++){
					System.out.println("JoQuoteDetailID=="+aObj[0].toString());
					JoQuoteDetail joqh = (JoQuoteDetail) aJoQuoteHeaderSession.get(JoQuoteDetail.class,JobUtil.ConvertintoInteger(aObj[0].toString()));
					if(joqh!=null){
						if(joqh.getInlineNote()!=null &&joqh.getInlineNote().trim()!=null){
						//System.out.println("Before Conversion=====>"+joqh.getInlineNote());
						String htmlText = rtfToHtml(new StringReader(joqh.getInlineNote()));
						htmlText=  htmlText.replaceAll("<html>","");
						htmlText=  htmlText.replaceAll("<head>","");
						htmlText=  htmlText.replaceAll("<style>","");
						htmlText=  htmlText.replaceAll("<!--","");
						htmlText=  htmlText.replaceAll("-->","");
						htmlText=  htmlText.replaceAll("</style>","");
						htmlText=  htmlText.replaceAll("</head>","");
						htmlText=  htmlText.replaceAll("<body>","");
						htmlText=  htmlText.replaceAll("</body>","");
						htmlText=  htmlText.replaceAll("</html>","");
						htmlText=htmlText.trim();
					
						if(htmlText.replaceAll("\\s","")!=""){
							Transaction aTransaction = aJoQuoteHeaderSession.beginTransaction();
							aTransaction.begin();
							joqh.setInlineNote(htmlText);
							aJoQuoteHeaderSession.update(joqh);
							aTransaction.commit();
						}
						
						}
						
					}
				}
				//}
				aQuery=null;
				} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aJoQuoteHeaderSession.flush();
				aJoQuoteHeaderSession.close();
			}
			return true;
		}
		
		@Override
		public boolean getjoquoteTemplatedetailrtftohtml() throws JobException {
			Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
			List<JoQuoteDetail> jdetlst=null;
			try {
				Query aQuery = aJoQuoteHeaderSession.createSQLQuery("select joQuoteTemplateDetailID,joQuoteTemplateHeaderID FROM  joQuoteTemplateDetail");
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
				//for(int i=272090;i<1605774;i++){
					System.out.println("JoQuoteDetailID=="+aObj[0].toString());
					JoQuoteTemplateDetail joqh = (JoQuoteTemplateDetail) aJoQuoteHeaderSession.get(JoQuoteTemplateDetail.class,JobUtil.ConvertintoInteger(aObj[0].toString()));
					if(joqh!=null){
						if(joqh.getInlineNote()!=null &&joqh.getInlineNote().trim()!=null){
						//System.out.println("Before Conversion=====>"+joqh.getInlineNote());
						String htmlText = rtfToHtml(new StringReader(joqh.getInlineNote()));
						htmlText=  htmlText.replaceAll("<html>","");
						htmlText=  htmlText.replaceAll("<head>","");
						htmlText=  htmlText.replaceAll("<style>","");
						htmlText=  htmlText.replaceAll("<!--","");
						htmlText=  htmlText.replaceAll("-->","");
						htmlText=  htmlText.replaceAll("</style>","");
						htmlText=  htmlText.replaceAll("</head>","");
						htmlText=  htmlText.replaceAll("<body>","");
						htmlText=  htmlText.replaceAll("</body>","");
						htmlText=  htmlText.replaceAll("</html>","");
						htmlText=htmlText.trim();
					
						if(htmlText.replaceAll("\\s","")!=""){
							Transaction aTransaction = aJoQuoteHeaderSession.beginTransaction();
							aTransaction.begin();
							joqh.setInlineNote(htmlText);
							aJoQuoteHeaderSession.update(joqh);
							aTransaction.commit();
						}
						
						}
						
					}
				}
				//}
				aQuery=null;
				} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aJoQuoteHeaderSession.flush();
				aJoQuoteHeaderSession.close();
			}
			return true;
		}
		@Override
		public boolean getjoquotedetailupdateposition() throws JobException {
			Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
			List<JoQuoteDetail> jdetlst=null;
			try {
				Query aQuery = aJoQuoteHeaderSession.createSQLQuery("select joQuoteHeaderID,joMasterID FROM  joQuoteHeader where joQuoteHeaderID>58499 and joQuoteHeaderID<60968");
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					Integer joQuoteHeaderID=(Integer)aObj[0];
					updatePosition(joQuoteHeaderID);
				}
				} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aJoQuoteHeaderSession.flush();
				aJoQuoteHeaderSession.close();
			}
			return true;
		}
		
		public boolean updatePosition(Integer joQuoteHeaderIDval) throws JobException {
			Session aJoQuoteHeaderSession = itsSessionFactory.openSession();
			List<JoQuoteDetail> jdetlst=null;
			try {
				Query aQuery = aJoQuoteHeaderSession.createSQLQuery("select joQuoteDetailMstrID,joQuoteHeaderID FROM  joQuoteDetailMstr where joQuoteHeaderID="+joQuoteHeaderIDval+" order by joQuoteDetailMstrID");
				Iterator<?> aIterator = aQuery.list().iterator();
				Integer position=0;
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					System.out.println("joQuoteHeaderIDval=="+joQuoteHeaderIDval+"joQuoteDetailMstrID=="+aObj[0]);
					Integer joQuoteDetailMstrID=(Integer)aObj[0];
					Integer joQuoteHeaderID=(Integer)aObj[1];
					joQuoteDetailMstr joqh = (joQuoteDetailMstr) aJoQuoteHeaderSession.get(joQuoteDetailMstr.class,joQuoteDetailMstrID);
					Transaction aTransaction = aJoQuoteHeaderSession.beginTransaction();
					aTransaction.begin();
					position=position+1;
					joqh.setPosition(position);
					aJoQuoteHeaderSession.update(joqh);
					aTransaction.commit();
					/*Query aQuery1 = aJoQuoteHeaderSession.createSQLQuery("call sp_updatePosition("+joQuoteHeaderID+","+joQuoteDetailMstrID+")");
					aQuery1.executeUpdate();*/
				}
				} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aJoQuoteHeaderSession.flush();
				aJoQuoteHeaderSession.close();
			}
			return true;
		}
		
		
		public static String rtfToHtml(Reader rtf) throws IOException {
			JEditorPane p = new JEditorPane();
			p.setContentType("text/rtf");
			EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
			try {
				kitRtf.read(rtf, p.getDocument(), 0);
				kitRtf = null;
				EditorKit kitHtml = p.getEditorKitForContentType("text/html");
				Writer writer = new StringWriter();
				kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
				return writer.toString();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		public String getJobNumbersequence(String sJobNumber) throws JobException {
			Session aSession = null;
			if(sJobNumber.contains("-0")){
				sJobNumber=sJobNumber.replaceAll("-0", "");
			}else if(sJobNumber.contains("-")){
				sJobNumber=sJobNumber.replaceAll("-", "");
			}else{
				
			}
			try {
				String theJobNumber = "SELECT MAX(CAST(seqnum AS UNSIGNED)) from joMaster where JobNumber LIKE '"
						+ JobUtil.removeSpecialcharacterswithslash(sJobNumber) + "%' AND seqnum IS NOT NULL ORDER BY seqnum DESC LIMIT 1";
				
				aSession = itsSessionFactory.openSession();
				Query query = aSession.createSQLQuery(theJobNumber);
				List querylist=query.list();
				if (querylist!=null && query.list().size() > 0){
					return String.valueOf(querylist.get(0));
				}
				else{
					return new String("0");
				}

			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}

		}
		
		
		@Override
		public String getJobNumberSequenceDate(String sJobNumber) throws JobException {
			Session aSession = null;
			if(sJobNumber.contains("-0")){
				sJobNumber=sJobNumber.replaceAll("-0", "");
			}else if(sJobNumber.contains("-")){
				sJobNumber=sJobNumber.replaceAll("-", "");
			}else{
				
			}
			try {
				String theJobNumber = " SELECT JobNumber FROM joMaster WHERE JobNumber LIKE '"
						+ JobUtil.removeSpecialcharacterswithslash(sJobNumber) + "%' ORDER BY JobNumber DESC LIMIT 1";
			
				aSession = itsSessionFactory.openSession();
				Query query = aSession.createSQLQuery(theJobNumber);
				if (query.list().size() > 0){
					return (String) query.list().get(0);
				}
				else{
					return new String("0");
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}

		}
		
		@Override
		public Integer getnumberofInvoiceNumber(String jobNumberwithPrefix)
				throws JobException {
			Session acuInvoiceSession = null;
			Transaction aTransaction;
			Integer returncountvalue=0;
			String aSelectquery = null;
			List<?> listdetails = null;
			try {
				acuInvoiceSession = itsSessionFactory.openSession();
				aTransaction = acuInvoiceSession.beginTransaction();
				aTransaction.begin();
				aSelectquery = "SELECT COUNT(*) FROM cuInvoice WHERE InvoiceNumber LIKE '%"+JobUtil.removeSpecialcharacterswithslash(jobNumberwithPrefix)+"%'";
				listdetails = acuInvoiceSession
						.createSQLQuery(aSelectquery).list();
				returncountvalue = ((BigInteger)listdetails.get(0)).intValue();
				aSelectquery=null;
			} finally {
				acuInvoiceSession.flush();
				acuInvoiceSession.close();
				aSelectquery = null;
				listdetails = null;
			}

			return returncountvalue;
		}
		@Override
		public Prmaster getPrMasterBasedOnId(Integer theprmasterid)
				throws JobException {
			Session aSession = null;
			Prmaster aPrmaster = new Prmaster();
			try {
				aSession = itsSessionFactory.openSession();
				aPrmaster = (Prmaster) aSession.get(Prmaster.class,theprmasterid);
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aPrmaster;
		}
		public String updatePrMasterCustomerinvoiceWithSo(Integer cuInvoiceID,Cuinvoicedetail newCuInvDetail,String oper) {
			Session aSession = null;
			Transaction aTransaction;
			try{
				aSession = itsSessionFactory.openSession();
					aTransaction = aSession.beginTransaction();
					BigDecimal order = BigDecimal.ZERO;
					BigDecimal QtyBilled =BigDecimal.ZERO;
					BigDecimal allocated = BigDecimal.ZERO;
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class,newCuInvDetail.getPrMasterId());
					if(objPrmaster.getInventoryOnHand()!=null){
						order = objPrmaster.getInventoryOnHand();
						allocated = objPrmaster.getInventoryAllocated();
						order = order==null?new BigDecimal("0.0000"):order;
						allocated=allocated==null?new BigDecimal("0.0000"):allocated;
						if(oper.equals("add")){
							QtyBilled = newCuInvDetail.getQuantityBilled()==null?new BigDecimal("0.0000"):newCuInvDetail.getQuantityBilled();
							objPrmaster.setInventoryOnHand(order.subtract(QtyBilled));
							if(newCuInvDetail!=null && newCuInvDetail.getCuSodetailId()!=null){
							objPrmaster.setInventoryAllocated(allocated.subtract(QtyBilled));
							}
							updatePrWarehouseInventoryForCuInvoice(null,newCuInvDetail, oper);
						}else if(oper.equals("edit")){
							Cuinvoicedetail oldCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,newCuInvDetail.getCuInvoiceDetailId());
							QtyBilled = oldCuinvoicedetail.getQuantityBilled()==null?new BigDecimal("0.0000"):oldCuinvoicedetail.getQuantityBilled();
							order=order.add(QtyBilled);
							BigDecimal newQtyBilled=newCuInvDetail.getQuantityBilled()==null?new BigDecimal("0.0000"):newCuInvDetail.getQuantityBilled();
							objPrmaster.setInventoryOnHand(order.subtract(newQtyBilled));
							allocated=allocated.add(QtyBilled);
							if(oldCuinvoicedetail!=null && oldCuinvoicedetail.getCuSodetailId()!=null){
							objPrmaster.setInventoryAllocated(allocated.subtract(newQtyBilled));
							}
							updatePrWarehouseInventoryForCuInvoice(oldCuinvoicedetail,newCuInvDetail, oper);
						}else if(oper.equals("delete")){
							Cuinvoicedetail oldCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,newCuInvDetail.getCuInvoiceDetailId());
							QtyBilled = oldCuinvoicedetail.getQuantityBilled()==null?new BigDecimal("0.0000"):oldCuinvoicedetail.getQuantityBilled();
							order=order.add(QtyBilled);
							objPrmaster.setInventoryOnHand(order);
							if(oldCuinvoicedetail!=null && oldCuinvoicedetail.getCuSodetailId()!=null){
							objPrmaster.setInventoryAllocated(allocated.add(QtyBilled));
							}
							updatePrWarehouseInventoryForCuInvoice(oldCuinvoicedetail,newCuInvDetail, oper);
						}
						
						
					}
					
					
					
					aSession.update(objPrmaster);
					aTransaction.commit();
					
					
				
				
			//Cuinvoicedetail acuInvoiceDetail=(Prmaster) aSession.get(Prmaster.class,theprmasterid);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				aSession.flush();
				aSession.close();
			}
			
			return "sucess";
		}
		
		public String updatePrMasterCustomerinvoiceWithoutSo(Integer cuInvoiceID,Cuinvoicedetail newCuInvDetail,String oper) {
			Session aSession = null;
			Transaction aTransaction;
			try{
				aSession = itsSessionFactory.openSession();
					aTransaction = aSession.beginTransaction();
					BigDecimal order = BigDecimal.ZERO;
					BigDecimal QtyBilled =BigDecimal.ZERO;
					BigDecimal allocated = BigDecimal.ZERO;
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class,newCuInvDetail.getPrMasterId());
					if(objPrmaster.getInventoryOnHand()!=null){
						order = objPrmaster.getInventoryOnHand();
						allocated = objPrmaster.getInventoryAllocated();
						order = order==null?new BigDecimal("0.0000"):order;
						allocated=allocated==null?new BigDecimal("0.0000"):allocated;
						if(oper.equals("add")){
							QtyBilled = newCuInvDetail.getQuantityBilled()==null?new BigDecimal("0.0000"):newCuInvDetail.getQuantityBilled();
							objPrmaster.setInventoryOnHand(order.subtract(QtyBilled));
							updatePrWarehouseInventoryForCuInvoiceWithoutSO(null,newCuInvDetail, oper);
						}else if(oper.equals("edit")){
							Cuinvoicedetail oldCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,newCuInvDetail.getCuInvoiceDetailId());
							QtyBilled = oldCuinvoicedetail.getQuantityBilled()==null?new BigDecimal("0.0000"):oldCuinvoicedetail.getQuantityBilled();
							order=order.add(QtyBilled);
							BigDecimal newQtyBilled=newCuInvDetail.getQuantityBilled()==null?new BigDecimal("0.0000"):newCuInvDetail.getQuantityBilled();
							objPrmaster.setInventoryOnHand(order.subtract(newQtyBilled));
							updatePrWarehouseInventoryForCuInvoiceWithoutSO(oldCuinvoicedetail,newCuInvDetail, oper);
						}else if(oper.equals("delete")){
							Cuinvoicedetail oldCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,newCuInvDetail.getCuInvoiceDetailId());
							QtyBilled = oldCuinvoicedetail.getQuantityBilled()==null?new BigDecimal("0.0000"):oldCuinvoicedetail.getQuantityBilled();
							order=order.add(QtyBilled);
							objPrmaster.setInventoryOnHand(order);
							//objPrmaster.setInventoryAllocated(allocated.add(QtyBilled));
							updatePrWarehouseInventoryForCuInvoiceWithoutSO(oldCuinvoicedetail,newCuInvDetail, oper);
						}
						
						
					}
					
					
					
					aSession.update(objPrmaster);
					aTransaction.commit();
					
					
				
				
			//Cuinvoicedetail acuInvoiceDetail=(Prmaster) aSession.get(Prmaster.class,theprmasterid);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				aSession.flush();
				aSession.close();
			}
			
			return "sucess";
		}
		
		public String updatePrMasterCustomerinvoiceInsideJob(Integer cuInvoiceID,Cuinvoicedetail newCuInvDetail,String oper) {
			Session aSession = null;
			Transaction aTransaction;
			try{
				aSession = itsSessionFactory.openSession();
					aTransaction = aSession.beginTransaction();
					BigDecimal order = BigDecimal.ZERO;
					BigDecimal QtyBilled =BigDecimal.ZERO;
					BigDecimal allocated = BigDecimal.ZERO;
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class,newCuInvDetail.getPrMasterId());
					if(objPrmaster.getInventoryOnHand()!=null){
						order = objPrmaster.getInventoryOnHand();
						allocated = objPrmaster.getInventoryAllocated();
						order = order==null?new BigDecimal("0.0000"):order;
						allocated=allocated==null?new BigDecimal("0.0000"):allocated;
						if(oper.equals("add")){
							QtyBilled = newCuInvDetail.getQuantityBilled()==null?new BigDecimal("0.0000"):newCuInvDetail.getQuantityBilled();
							
							if(newCuInvDetail!=null && newCuInvDetail.getCuSodetailId()!=null){
							objPrmaster.setInventoryAllocated(allocated.subtract(QtyBilled));
							}
							//2015-07-02 19:01 Jenith
							objPrmaster.setInventoryOnHand(order.subtract(QtyBilled));
							updatePWInventoryForCuInvoiceInsideJob(null,newCuInvDetail, oper);
						}else if(oper.equals("edit")){
							Cuinvoicedetail oldCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,newCuInvDetail.getCuInvoiceDetailId());
							QtyBilled = oldCuinvoicedetail.getQuantityBilled()==null?new BigDecimal("0.0000"):oldCuinvoicedetail.getQuantityBilled();
							order=order.add(QtyBilled);
							BigDecimal newQtyBilled=newCuInvDetail.getQuantityBilled()==null?new BigDecimal("0.0000"):newCuInvDetail.getQuantityBilled();
							objPrmaster.setInventoryOnHand(order.subtract(newQtyBilled));
							allocated=allocated.add(QtyBilled);
							if(oldCuinvoicedetail!=null && oldCuinvoicedetail.getCuSodetailId()!=null){
							objPrmaster.setInventoryAllocated(allocated.subtract(newQtyBilled));
							}
							updatePWInventoryForCuInvoiceInsideJob(oldCuinvoicedetail,newCuInvDetail, oper);
						}else if(oper.equals("delete")){
							Cuinvoicedetail oldCuinvoicedetail = (Cuinvoicedetail) aSession.get(Cuinvoicedetail.class,newCuInvDetail.getCuInvoiceDetailId());
							QtyBilled = oldCuinvoicedetail.getQuantityBilled()==null?new BigDecimal("0.0000"):oldCuinvoicedetail.getQuantityBilled();
							order=order.add(QtyBilled);
							objPrmaster.setInventoryOnHand(order);
							if(oldCuinvoicedetail!=null && oldCuinvoicedetail.getCuSodetailId()!=null){
							objPrmaster.setInventoryAllocated(allocated.add(QtyBilled));
							}
							updatePWInventoryForCuInvoiceInsideJob(oldCuinvoicedetail,newCuInvDetail, oper);
						}
						
						
					}
					
					
					
					aSession.update(objPrmaster);
					aTransaction.commit();
					
					
				
				
			//Cuinvoicedetail acuInvoiceDetail=(Prmaster) aSession.get(Prmaster.class,theprmasterid);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				aSession.flush();
				aSession.close();
			}
			
			return "sucess";
		}
		public String updatePWInventoryForCuInvoiceInsideJob(
				Cuinvoicedetail theCuinvoicedetailold,
				Cuinvoicedetail theCuinvoicedetailNew, String operation)
				throws JobException {
			Session aCuInvoiceSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			try {
				aTransaction = aCuInvoiceSession.beginTransaction();
				aTransaction.begin();
				Cuinvoice aCuinvoice = (Cuinvoice) aCuInvoiceSession.get(
						Cuinvoice.class, theCuinvoicedetailNew.getCuInvoiceId());
				Integer sPrWarehouseID = 0;
				BigDecimal inventoryAllocated = new BigDecimal(0);
				BigDecimal inventoryOnHand = new BigDecimal(0);
				Integer prWarehouseInventoryID = 0;
				String sQuery = "SELECT prFromWarehouseID FROM cuInvoice cuinvoice WHERE cuInvoiceID = "
						+ aCuinvoice.getCuInvoiceId();
				Query query = aCuInvoiceSession.createSQLQuery(sQuery);
				if (query.list().size() > 0)
					sPrWarehouseID = (Integer) query.list().get(0);

				String sQuery1 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
						+ theCuinvoicedetailNew.getPrMasterId()
						+ " AND prWarehouseID = " + sPrWarehouseID;
				query = aCuInvoiceSession.createSQLQuery(sQuery1);

				if (query.list().size() > 0) {
					Object[] object = (Object[]) query.list().get(0);
					inventoryAllocated = (BigDecimal) object[0];
					inventoryOnHand = (BigDecimal) object[1];
					prWarehouseInventoryID = (Integer) object[2];
				}
				Prwarehouseinventory objPrwarehouseinventory = null;
				BigDecimal allocated = new BigDecimal(0.0);
				BigDecimal onHand = new BigDecimal(0.0);
				if (null != prWarehouseInventoryID) {
					objPrwarehouseinventory = (Prwarehouseinventory) aCuInvoiceSession
							.get(Prwarehouseinventory.class, prWarehouseInventoryID);
					if (null != objPrwarehouseinventory) {
						allocated = objPrwarehouseinventory.getInventoryAllocated();
						onHand = objPrwarehouseinventory.getInventoryOnHand();
						if ("edit".equalsIgnoreCase(operation)) {
							if (theCuinvoicedetailold.getPrMasterId().equals(
									theCuinvoicedetailNew.getPrMasterId())) {

								BigDecimal newAllocated = allocated
										.add(theCuinvoicedetailold
												.getQuantityBilled());
								BigDecimal newOnHand = onHand
										.add(theCuinvoicedetailold
												.getQuantityBilled());
								
								if(theCuinvoicedetailold!=null && theCuinvoicedetailold.getCuSodetailId()!=null){
								objPrwarehouseinventory
										.setInventoryAllocated(newAllocated
												.subtract(theCuinvoicedetailNew
														.getQuantityBilled()));
								}
								//2015-07-02 19:01 Jenith
//								if(theCuinvoicedetailNew
//										.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1){
								objPrwarehouseinventory
										.setInventoryOnHand(newOnHand
												.subtract(theCuinvoicedetailNew
														.getQuantityBilled()));
//								}
								aCuInvoiceSession.update(objPrwarehouseinventory);
								aTransaction.commit();

							} else {

								BigDecimal newAllocated = allocated
										.add(theCuinvoicedetailold
												.getQuantityBilled());
								BigDecimal newOnHand = onHand
										.add(theCuinvoicedetailold
												.getQuantityBilled());
								objPrwarehouseinventory
										.setInventoryAllocated(newAllocated);
//								objPrwarehouseinventory
//										.setInventoryOnHand(newOnHand);
								aCuInvoiceSession.update(objPrwarehouseinventory);
								aTransaction.commit();

								aTransaction.begin();
								String sQuery2 = "SELECT inventory.InventoryAllocated,inventory.InventoryOnHand,inventory.prWarehouseInventoryID FROM prWarehouseInventory inventory WHERE inventory.prMasterID = "
										+ theCuinvoicedetailNew.getPrMasterId()
										+ " AND prWarehouseID = " + sPrWarehouseID;
								Query query1 = aCuInvoiceSession
										.createSQLQuery(sQuery2);

								if (query1.list().size() > 0) {
									Object[] object = (Object[]) query1.list().get(
											0);
									inventoryAllocated = (BigDecimal) object[0];
									inventoryOnHand = (BigDecimal) object[1];
									prWarehouseInventoryID = (Integer) object[2];
								}
								Prwarehouseinventory objPrwarehouseinventory1 = null;
								if (null != prWarehouseInventoryID) {
									objPrwarehouseinventory1 = (Prwarehouseinventory) aCuInvoiceSession
											.get(Prwarehouseinventory.class,
													prWarehouseInventoryID);
									allocated = objPrwarehouseinventory1
											.getInventoryAllocated();
									onHand = objPrwarehouseinventory1
											.getInventoryOnHand();
								} else {
									allocated = new BigDecimal(0.0);
									onHand = new BigDecimal(0.0);
								}
								//2015-07-02 19:01 Jenith
//								if(theCuinvoicedetailNew
//										.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1){
								objPrwarehouseinventory1.setInventoryOnHand(onHand
										.subtract(theCuinvoicedetailNew
												.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
														.getQuantityBilled()));
								/*}else{
									objPrwarehouseinventory.setInventoryOnHand(onHand
											.subtract(theCuinvoicedetailNew
													.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
															.getQuantityBilled()));
								}*/
								if(theCuinvoicedetailold!=null && theCuinvoicedetailold.getCuSodetailId()!=null){
								objPrwarehouseinventory1
										.setInventoryAllocated(allocated
												.subtract(theCuinvoicedetailNew
														.getQuantityBilled()));
								}
								aCuInvoiceSession.update(objPrwarehouseinventory1);
								aTransaction.commit();
							}

						} else if ("add".equalsIgnoreCase(operation)) {
							onHand =onHand==null?new BigDecimal("0.0000"):onHand;
							allocated= allocated==null?new BigDecimal("0.0000"):allocated;
							//2015-07-02 19:01 Jenith
//							if(theCuinvoicedetailNew
//									.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1){
							objPrwarehouseinventory.setInventoryOnHand(onHand
									.subtract(theCuinvoicedetailNew
											.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
													.getQuantityBilled()));
							/*}
							else{
								objPrwarehouseinventory.setInventoryOnHand(onHand
										.subtract(theCuinvoicedetailNew
												.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
														.getQuantityBilled()));
							}*/
							if(theCuinvoicedetailNew!=null && theCuinvoicedetailNew.getCuSodetailId()!=null){
							objPrwarehouseinventory.setInventoryAllocated(allocated
									.subtract(theCuinvoicedetailNew
											.getQuantityBilled()==null?new BigDecimal("0.0000"):theCuinvoicedetailNew
													.getQuantityBilled()));
							}
							
							aCuInvoiceSession.update(objPrwarehouseinventory);
							aTransaction.commit();

						} else if ("del".equalsIgnoreCase(operation)||"delete".equalsIgnoreCase(operation)) {
							//2015-07-02 19:01 Jenith
//							if(theCuinvoicedetailNew
//									.getQuantityBilled().compareTo(new BigDecimal("0.0000"))==-1){
							objPrwarehouseinventory
									.setInventoryOnHand(onHand
											.add(theCuinvoicedetailNew
													.getQuantityBilled()));
//							}
							if(theCuinvoicedetailold!=null && theCuinvoicedetailold.getCuSodetailId()!=null){
							objPrwarehouseinventory
									.setInventoryAllocated(allocated
											.add(theCuinvoicedetailNew
													.getQuantityBilled()));
							}
							aCuInvoiceSession.update(objPrwarehouseinventory);
							aTransaction.commit();
						}
					}

				}
				sQuery=null;sQuery1=null;
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getCause()
						.getMessage(), e);
				throw aJobException;
			} finally {
				aCuInvoiceSession.flush();
				aCuInvoiceSession.close();
			}
			return "Success";
		}
		
		@Override
		public List<Vepodetail> jobReleaseLineItemForrecieveInventory(Integer theVepoID)
				throws JobException {
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String aPOLineItemListQry = "";
			
			Session aSession = null;
			ArrayList<Vepodetail> aQueryList = new ArrayList<Vepodetail>();
			BigDecimal aTotal;
			BigDecimal aNetCast;
			try {
				
				if (theVepoID != null) {
					aPOLineItemListQry = "SELECT ve.vePODetailID," + " ve.vePOID, "
							+ " ve.prMasterID, " + " ve.Description,"
							+ " ve.QuantityOrdered, " + " ve.Taxable, "
							+ " ve.UnitCost," + " ve.PriceMultiplier,"
							+ " ve.posistion," + " pr.ItemCode, " + " vepo.TaxTotal, "
							+ " ve.Note, " + " ve.QuantityReceived, "
							+ " ve.AcknowledgementDate, ve.EstimatedShipDate, ve.VendorOrderNumber "+
							"FROM vePODetail ve LEFT JOIN prMaster pr ON ve.prMasterID = pr.prMasterID "
							+ "RIGHT JOIN vePO vepo ON vepo.vePOID = ve.vePOID " +
							"WHERE ve.vePOID = " + theVepoID + " AND pr.IsInventory=1 ORDER BY ve.posistion";
				
				
				Vepodetail avepoDetail = null;
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
				List<?> aList = aQuery.list();
				if (aPOLineItemListQry.length() > 0)
					if (!aList.isEmpty()) {
						Iterator<?> aIterator = aList.iterator();
						while (aIterator.hasNext()) {
							avepoDetail = new Vepodetail();
							Object[] aObj = (Object[]) aIterator.next();
							avepoDetail.setVePodetailId((Integer) aObj[0]);
							avepoDetail.setVePoid((Integer) aObj[1]);
							avepoDetail.setPrMasterId((Integer) aObj[2]);
							avepoDetail.setDescription((String) aObj[3]);
						
							
							avepoDetail.setQuantityOrdered(aObj[4]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[4]),2):new BigDecimal("0"));
							if ((Byte) aObj[5] == 1) {
								avepoDetail.setTaxable(true);
							} else {
								avepoDetail.setTaxable(false);
							}
							avepoDetail.setUnitCost(aObj[6]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[6]),2):new BigDecimal("0"));
							avepoDetail.setPriceMultiplier(aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0"));
							avepoDetail.setPosistion((Double) aObj[8]);
						
							
							
							String ackDate = "", shipDate = "";
							
							
//							if(aObj[13] != null && aObj[13] != "")
//								System.out.println(" aObj[13] : "+DateUtils.parseDate((String) aObj[13], new String[]{"MM/dd/yyyy"}));
//							if(aObj[14] != null && aObj[14] != "")
//								System.out.println(" aObj[14] : "+DateUtils.parseDate((String) aObj[14], new String[]{"MM/dd/yyyy"}));
							avepoDetail.setNote((String) aObj[9]);
							BigDecimal aUnitCost =aObj[6]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[6]),2):new BigDecimal("0");
							BigDecimal aPriceMult = aObj[7]!=null?(BigDecimal)aObj[7]:new BigDecimal("0");
							BigDecimal aQuantity = aObj[4]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[4]),2):new BigDecimal("0");
							BigDecimal quantityReceived = aObj[12]!=null?JobUtil.floorFigureoverall(((BigDecimal)aObj[12]),2):new BigDecimal("0");
							if (quantityReceived == null) {
								quantityReceived = new BigDecimal(0);
							}
							if(aPriceMult==null){
								aPriceMult=new BigDecimal(0);
							}
							if(aUnitCost==null){
								aUnitCost=new BigDecimal(0);
							}
							if (aUnitCost != null && aPriceMult != null
									&& aQuantity != null) {
								if (aPriceMult.compareTo(new BigDecimal("0.0000"))==0) {
									aTotal = aUnitCost.multiply(new BigDecimal("1.0000"));
									aTotal = aTotal.multiply(getTotal(aQuantity,
											quantityReceived));
									avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
								} else {
									aTotal = aUnitCost.multiply(aPriceMult);
									aTotal = aTotal.multiply(getTotal(aQuantity,
											quantityReceived));
									avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
								}
							} else if (aUnitCost != null && aQuantity != null) {
								aTotal = aUnitCost.multiply(getTotal(aQuantity,	quantityReceived));
								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
							} else if (aUnitCost != null && aPriceMult != null) {
								if ((aQuantity.compareTo(BigDecimal.ZERO) != 0)) {
									aTotal = aUnitCost.multiply(getTotal(aQuantity,
											quantityReceived));
								} else {
									aTotal = aUnitCost.multiply(getTotal(aQuantity,
											quantityReceived));
								}

								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aTotal,2));
							} else if (aUnitCost != null) {
								avepoDetail.setQuantityBilled(JobUtil.floorFigureoverall(aUnitCost,2));
							}
							if (aUnitCost != null && aPriceMult != null) {
								if (aPriceMult.compareTo(new BigDecimal("0.0000"))==0) {
									avepoDetail.setNetCast(JobUtil.floorFigureoverall((avepoDetail.getQuantityOrdered().multiply(
													aUnitCost).multiply(new BigDecimal("1.0000"))),2));
								} else {
									aNetCast = aUnitCost.multiply(aPriceMult);
									avepoDetail.setNetCast(JobUtil.floorFigureoverall((avepoDetail
											.getQuantityOrdered()
											.multiply(aNetCast)),2));
								}
							}
							if (quantityReceived != null) {

								avepoDetail.setQuantityReceived(quantityReceived);
							}else {
								avepoDetail.setQuantityReceived(BigDecimal.ZERO);
							}
				
							avepoDetail.setTaxTotal((BigDecimal) aObj[10]);
							avepoDetail.setInLineNote((String) aObj[11]);
							avepoDetail.setInLineNoteImage((String) aObj[11]);
							if(aObj[13] != null && aObj[13] != "")
							{
								ackDate = formatter.format((Date) aObj[13]);
								avepoDetail.setAckDate(formatter.format(DateUtils.parseDate(ackDate, new String[]{"MM/dd/yyyy"})));
							}
							else
								avepoDetail.setAckDate("");
							if(aObj[14] != null && aObj[14] != "")
							{
								shipDate = formatter.format((Date) aObj[14]);
								avepoDetail.setShipDate(formatter.format(DateUtils.parseDate(shipDate, new String[]{"MM/dd/yyyy"})));
							}
							else
								avepoDetail.setShipDate("");
							if(aObj[15] != null && aObj[15] != "")
								avepoDetail.setVendorOrderNumber((String) aObj[15]);
							else
								avepoDetail.setVendorOrderNumber("");
							
							//avepoDetail.setQuantityOrdered(avepoDetail.getQuantityOrdered().subtract(avepoDetail.getQuantityReceived()));
							avepoDetail.setDifference(avepoDetail.getQuantityOrdered().subtract(avepoDetail.getQuantityReceived()));
							
							// avepoDetail.setInvoicedAmount((BigDecimal) aObj[14]);
							aQueryList.add(avepoDetail);
							ackDate = null; shipDate = null;
						}
					}
				}
			} catch (Exception e) {
				itsLogger.error("Exception while getting the PO LineItem list: "
						+ e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aPOLineItemListQry=null;
			}
			return aQueryList;
		}
		
		@Override
		public Integer addRXMasterAndRxAddress(Rxmaster theRxmaster, Rxaddress theRxaddress)
				throws JobException {
			Session aRxMasterSession = itsSessionFactory.openSession();
			Integer rxAddressId=0;
			Transaction aTransaction;
			try {
				aTransaction = aRxMasterSession.beginTransaction();
				aTransaction.begin();
				int aRxMasterId = (Integer) aRxMasterSession.save(theRxmaster);
				aTransaction.commit();
				theRxaddress.setRxMasterId(aRxMasterId);
				aTransaction = aRxMasterSession.beginTransaction();
				aTransaction.begin();
				rxAddressId=(Integer)aRxMasterSession.save(theRxaddress);
				aTransaction.commit();
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aRxMasterSession.flush();
				aRxMasterSession.close();
			}
			return rxAddressId;
		}
		
		
		@Override
		public Boolean saveSalesOrderTempNote(Integer cuSoDetailTempID, String note) {
			itsLogger.info("Notes::"+note);
			Session aSession = null;
			aSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			try {

				Cusodetailtemplate cuSotempDetail = (Cusodetailtemplate) aSession.get(Cusodetailtemplate.class,
						cuSoDetailTempID);
				cuSotempDetail.setNote(note);
				aSession.update(cuSotempDetail);
				aTransaction.commit();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;

			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		public List<Vepodetail> getVepodetail(Integer vepoID) throws JobException {
			Session aSession = null;
			List<Vepodetail> aQueryList = null;
			String query="";
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createQuery("FROM  Vepodetail where vePoid="+vepoID);
				aQueryList=aQuery.list();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return  aQueryList;
		}
		
		public Boolean updateInventoryforpurchase(Vepo theVepo,String oper) throws JobException{
			List<Vepodetail> objVepodetaillst=getVepodetail(theVepo.getVePoid());
		    for(Vepodetail aVepodetail:objVepodetaillst){
			Vepo insideoroutsidejob=getVepo(theVepo.getVePoid());
			Prmaster thePrmaster =getPrMasterBasedOnId(aVepodetail.getPrMasterId());
			 if(insideoroutsidejob.getJoReleaseId()!=null){
					if(thePrmaster.getIsInventory()==1){
						updatePrWarehouseInventoryOrdered(aVepodetail,oper);
					}
			}else{
				if(thePrmaster.getIsInventory()==1){
				updatePrWarehouseInventoryOrdered(aVepodetail,oper);
				}
			}
		    }
		    
		    return true;
		}
		
		
		@Override
		public boolean addSalesORderReleaseLineItem(Cusodetail theCuSODetail,String oper)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			Integer cusoDetailID=null;
			BigDecimal oldQuantityOrdered = new BigDecimal("0.0000");
			Boolean tplog=false;
			try {
				
				if(oper.equals("add")){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					cusoDetailID =(Integer) aSession.save(theCuSODetail);
					tplog=true;
					aTransaction.commit();
					Cuso aCuso=(Cuso) aSession.get(Cuso.class, theCuSODetail.getCuSoid());
					if(aCuso.getTransactionStatus()==1){
					insertPrMasterPrWareHouseInventory(theCuSODetail.getCuSoid(),cusoDetailID);
					}
						if(cusoDetailID>0 && tplog==true && aCuso.getTransactionStatus()==1){
							
							TpInventoryLog aTpInventoryLog = new TpInventoryLog();
							aTpInventoryLog.setPrMasterID(theCuSODetail.getPrMasterId());
							Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+theCuSODetail.getPrMasterId());
							aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
							aTpInventoryLog.setWareHouseID(aCuso.getPrFromWarehouseId());
							aTpInventoryLog.setTransType("SO");
							aTpInventoryLog.setTransDecription("SO Created");
							aTpInventoryLog.setTransID(theCuSODetail.getCuSoid());
							aTpInventoryLog.setTransDetailID(theCuSODetail.getCuSodetailId());
							aTpInventoryLog.setProductOut(theCuSODetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?theCuSODetail.getQuantityOrdered():new BigDecimal("0.0000"));
							aTpInventoryLog.setProductIn(theCuSODetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?theCuSODetail.getQuantityOrdered():new BigDecimal("0.0000"));
							aTpInventoryLog.setUserID(aCuso.getCreatedById());
							aTpInventoryLog.setCreatedOn(new Date());
							itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
						
							/*TpInventoryLogMaster
							 * Created on 04-12-2015
							 * Code Starts
							 * */
							BigDecimal qo=theCuSODetail.getQuantityOrdered().subtract(theCuSODetail.getQuantityBilled());
							Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuso.getPrFromWarehouseId());
							Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuso.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),
									aPrmaster.getItemCode(),
									aCuso.getPrFromWarehouseId() ,
									theprwarehouse.getSearchName(),
									aPrmaster.getInventoryOnHand(),
									theprwarehsinventory.getInventoryOnHand(),
									theCuSODetail.getQuantityOrdered(),BigDecimal.ZERO,"SO",aCuso.getCuSoid(),
									cusoDetailID,aCuso.getSonumber(),null ,
					/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
					/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
									"SO Created",theCuSODetail.getUserID(),theCuSODetail.getUserName(),new Date());
							itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
							/*Code Ends*/
						}
					
				}else if(oper.equals("edit")){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Cusodetail aCusodetail = (Cusodetail) aSession.get(Cusodetail.class, theCuSODetail.getCuSodetailId());
					oldQuantityOrdered = aCusodetail.getQuantityOrdered();
					aCusodetail.setDescription(theCuSODetail.getDescription());
					aCusodetail.setPrMasterId(theCuSODetail.getPrMasterId());
					aCusodetail.setPriceMultiplier(theCuSODetail.getPriceMultiplier());
					aCusodetail.setUnitCost(theCuSODetail.getUnitCost());
					aCusodetail.setQuantityOrdered(theCuSODetail.getQuantityOrdered());
					aCusodetail.setTaxable(theCuSODetail.getTaxable());
					aCusodetail.setNote(theCuSODetail.getNote());
					aCusodetail.setWhseCost(theCuSODetail.getWhseCost());
					aCusodetail.setPosition(theCuSODetail.getPosition());
					Cuso aCuso=(Cuso) aSession.get(Cuso.class, aCusodetail.getCuSoid());
					if(aCuso.getTransactionStatus()==1){
					RollBackPrMasterPrWareHouseInventory(theCuSODetail.getCuSoid(),theCuSODetail.getCuSodetailId());
					}
					tplog=true;
					if(aCusodetail.getCuSodetailId()>0 && aCuso.getTransactionStatus()==1){
						if(oldQuantityOrdered.compareTo(theCuSODetail.getQuantityOrdered()) !=0 && tplog){
							TpInventoryLog aTpInventoryLog = new TpInventoryLog();
							Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aCusodetail.getPrMasterId());
							aTpInventoryLog.setPrMasterID(aCusodetail.getPrMasterId());
							aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
							aTpInventoryLog.setWareHouseID(aCuso.getPrFromWarehouseId());
							aTpInventoryLog.setTransType("SO");
							aTpInventoryLog.setTransDecription("SO RollBack");
							aTpInventoryLog.setTransID(aCusodetail.getCuSoid());
							aTpInventoryLog.setTransDetailID(aCusodetail.getCuSodetailId());
							aTpInventoryLog.setProductOut(oldQuantityOrdered.compareTo(new BigDecimal("0.0000"))==-1?oldQuantityOrdered:new BigDecimal("0.0000"));
							aTpInventoryLog.setProductIn(oldQuantityOrdered.compareTo(new BigDecimal("0.0000"))==1?oldQuantityOrdered:new BigDecimal("0.0000"));
							aTpInventoryLog.setUserID(aCuso.getCreatedById());
							aTpInventoryLog.setCreatedOn(new Date());
							itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
							
							/*TpInventoryLogMaster
							 * Created on 04-12-2015
							 * Code Starts
							 * RollBack
							 * */
							BigDecimal oqo=oldQuantityOrdered.subtract(aCusodetail.getQuantityBilled());
							Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuso.getPrFromWarehouseId());
							Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuso.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),
									aPrmaster.getItemCode(),
									aCuso.getPrFromWarehouseId() ,
									otheprwarehouse.getSearchName(),
									aPrmaster.getInventoryOnHand(),
									otheprwarehsinventory.getInventoryOnHand(),
									oqo.multiply(new BigDecimal(-1)),
									BigDecimal.ZERO,"SO",aCuso.getCuSoid(),
									aCusodetail.getCuSodetailId(),aCuso.getSonumber(),null ,
					/*Product out*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
					/*Product in*/(oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO,
									"SO Edited",theCuSODetail.getUserID(),theCuSODetail.getUserName(),
									new Date());
							itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
							/*Code Ends*/
							}
						}
					aSession.update(aCusodetail);
					aTransaction.commit();
					 aCuso=(Cuso) aSession.get(Cuso.class, aCusodetail.getCuSoid());
					if(aCuso.getTransactionStatus()==1){
					insertPrMasterPrWareHouseInventory(theCuSODetail.getCuSoid(),aCusodetail.getCuSodetailId());
					}
					if(aCusodetail.getCuSodetailId()>0 && aCuso.getTransactionStatus()==1){
						if(oldQuantityOrdered.compareTo(theCuSODetail.getQuantityOrdered()) !=0 && tplog){
						TpInventoryLog theTpInventoryLog = new TpInventoryLog();
						
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aCusodetail.getPrMasterId());
						theTpInventoryLog.setPrMasterID(theCuSODetail.getPrMasterId());
						theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						theTpInventoryLog.setWareHouseID(aCuso.getPrFromWarehouseId());
						theTpInventoryLog.setTransType("SO");
						theTpInventoryLog.setTransDecription("SO Edited");
						theTpInventoryLog.setTransID(theCuSODetail.getCuSoid());
						theTpInventoryLog.setTransDetailID(theCuSODetail.getCuSodetailId());
						theTpInventoryLog.setProductOut(theCuSODetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?theCuSODetail.getQuantityOrdered():new BigDecimal("0.0000"));
						theTpInventoryLog.setProductIn(theCuSODetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?theCuSODetail.getQuantityOrdered():new BigDecimal("0.0000"));
						theTpInventoryLog.setUserID(aCuso.getCreatedById());
						theTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * */
						BigDecimal qo=theCuSODetail.getQuantityOrdered().subtract(theCuSODetail.getQuantityBilled()==null?BigDecimal.ZERO:theCuSODetail.getQuantityBilled());
						Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuso.getPrFromWarehouseId());
						Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuso.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
						TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
								aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),
								aCuso.getPrFromWarehouseId() ,theprwarehouse.getSearchName(),
								aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
								theCuSODetail.getQuantityOrdered(),BigDecimal.ZERO,"SO",aCuso.getCuSoid(),theCuSODetail.getCuSodetailId(),aCuso.getSonumber(),null ,
				/*Product out*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
				/*Product in*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
								"SO Edited",theCuSODetail.getUserID(),theCuSODetail.getUserName(),
								new Date());
						itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
						/*Code Ends*/
						}
					}
					
				}else if(oper.equals("delete")){
						aTransaction = aSession.beginTransaction();
						aTransaction.begin();
						Cusodetail aCusodetail = (Cusodetail) aSession.get(Cusodetail.class, theCuSODetail.getCuSodetailId());
						Cuso aCuso=(Cuso) aSession.get(Cuso.class, aCusodetail.getCuSoid());
						if(aCuso.getTransactionStatus()==1){
						RollBackPrMasterPrWareHouseInventory(aCusodetail.getCuSoid(),aCusodetail.getCuSodetailId());
						}
						aSession.delete(aCusodetail);
						
						if(aCusodetail.getCuSodetailId()>0){
							TpInventoryLog aTpInventoryLog = new TpInventoryLog();
							
							Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aCusodetail.getPrMasterId());
							aTpInventoryLog.setPrMasterID(aCusodetail.getPrMasterId());
							aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
							aTpInventoryLog.setWareHouseID(aCuso.getPrFromWarehouseId());
							aTpInventoryLog.setTransType("SO");
							aTpInventoryLog.setTransDecription("SO Deleted");
							aTpInventoryLog.setTransID(aCusodetail.getCuSoid());
							aTpInventoryLog.setTransDetailID(aCusodetail.getCuSodetailId());
							aTpInventoryLog.setProductOut(aCusodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?aCusodetail.getQuantityOrdered():new BigDecimal("0.0000"));
							aTpInventoryLog.setProductIn(aCusodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?aCusodetail.getQuantityOrdered():new BigDecimal("0.0000"));
							aTpInventoryLog.setUserID(aCuso.getCreatedById());
							aTpInventoryLog.setCreatedOn(new Date());
							itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
							aTransaction.commit();
							
							/*TpInventoryLogMaster
							 * Created on 04-12-2015
							 * Code Starts
							 * RollBack
							 * */
							BigDecimal oqo=aCusodetail.getQuantityOrdered().subtract(aCusodetail.getQuantityBilled());
							Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aCuso.getPrFromWarehouseId());
							Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aCuso.getPrFromWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aCuso.getPrFromWarehouseId() ,otheprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),otheprwarehsinventory.getInventoryOnHand(),
									oqo.multiply(new BigDecimal(-1)),BigDecimal.ZERO,"SO",aCuso.getCuSoid(),aCusodetail.getCuSodetailId(),aCuso.getSonumber(),null ,
					/*Product out*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
					/*Product in*/(oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO ,
									"SO Deleted",theCuSODetail.getUserID(),theCuSODetail.getUserName(),
									new Date());
							itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
							/*Code Ends*/
						}
						
				}
				
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		@Override
		public boolean updateCuso(Cuso theCuso)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			Integer cusoDetailID=null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Cuso aCuso = (Cuso) aSession.get(Cuso.class, theCuso.getCuSoid());
					aCuso.setSubTotal(theCuso.getSubTotal());
					aCuso.setFreight(theCuso.getFreight());
					aCuso.setTaxTotal(theCuso.getTaxTotal());
					aCuso.setCostTotal(theCuso.getCostTotal());
				    aCuso.setWhseCostTotal(theCuso.getWhseCostTotal());
				    aCuso.setWithpriceStatus(theCuso.isWithpriceStatus());
					aSession.update(aCuso);
					aTransaction.commit();
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		public boolean insertPrMasterPrWareHouseInventory(Integer cuSOID,Integer cuSODetailID)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Cuso thecuso=(Cuso) aSession.get(Cuso.class, cuSOID);
					Cusodetail thecusodetail = (Cusodetail) aSession.get(Cusodetail.class, cuSODetailID);
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class, thecusodetail.getPrMasterId());
					if(objPrmaster.getIsInventory()==1){
					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated==null?new BigDecimal("0.0000").add(thecusodetail.getQuantityOrdered().subtract(
							thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
							)):
							allocated.add(thecusodetail.getQuantityOrdered().subtract(
									thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
									))
					);
					aSession.update(objPrmaster);
					
					String prwarehouseQuery="FROM Prwarehouseinventory WHERE prMasterID ="+thecusodetail.getPrMasterId()+" And prWarehouseID="+thecuso.getPrFromWarehouseId();
					Query aQuery = aSession.createQuery(prwarehouseQuery);
					ArrayList<Prwarehouseinventory> Prwarehouseinventorylist = (ArrayList<Prwarehouseinventory>) aQuery.list();
					if (!Prwarehouseinventorylist.isEmpty()) {
						Prwarehouseinventory aPrwarehouseinventory = Prwarehouseinventorylist.get(0);
						Prwarehouseinventory thePrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, aPrwarehouseinventory.getPrWarehouseInventoryId());
						BigDecimal prallocated = thePrwarehouseinventory.getInventoryAllocated();
						thePrwarehouseinventory.setInventoryAllocated(
								prallocated==null?new BigDecimal("0.0000").add(thecusodetail.getQuantityOrdered().subtract(
										thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
										)):
										prallocated.add(thecusodetail.getQuantityOrdered().subtract(
												thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
												))
								);
						aSession.update(thePrwarehouseinventory);
					}
					aTransaction.commit();
					}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		public boolean RollBackPrMasterPrWareHouseInventory(Integer cuSOID,Integer cuSODetailID)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Cuso thecuso=(Cuso) aSession.get(Cuso.class, cuSOID);
					Cusodetail thecusodetail = (Cusodetail) aSession.get(Cusodetail.class, cuSODetailID);
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class, thecusodetail.getPrMasterId());
					if(objPrmaster.getIsInventory()==1){
					BigDecimal allocated = objPrmaster.getInventoryAllocated();
					objPrmaster.setInventoryAllocated(allocated==null?new BigDecimal("0.0000").subtract(thecusodetail.getQuantityOrdered().subtract(
							thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
							)):
							allocated.subtract(thecusodetail.getQuantityOrdered().subtract(
									thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
									))
					);
					aSession.update(objPrmaster);
					
					String prwarehouseQuery="FROM Prwarehouseinventory WHERE prMasterID ="+thecusodetail.getPrMasterId()+" And prWarehouseID="+thecuso.getPrFromWarehouseId();
					Query aQuery = aSession.createQuery(prwarehouseQuery);
					ArrayList<Prwarehouseinventory> Prwarehouseinventorylist = (ArrayList<Prwarehouseinventory>) aQuery.list();
					if (!Prwarehouseinventorylist.isEmpty()) {
						Prwarehouseinventory aPrwarehouseinventory = Prwarehouseinventorylist.get(0);
						Prwarehouseinventory thePrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, aPrwarehouseinventory.getPrWarehouseInventoryId());
						BigDecimal prallocated = thePrwarehouseinventory.getInventoryAllocated();
						thePrwarehouseinventory.setInventoryAllocated(
								prallocated==null?new BigDecimal("0.0000").subtract(thecusodetail.getQuantityOrdered().subtract(
										thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
										)):
										prallocated.subtract(thecusodetail.getQuantityOrdered().subtract(
												thecusodetail.getQuantityBilled()==null?BigDecimal.ZERO:thecusodetail.getQuantityBilled()
												))
								);
						aSession.update(thePrwarehouseinventory);
					}
					aTransaction.commit();
					}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		@Override
		public boolean addPurchaseORderLineItem(Vepodetail theVepodetail,String oper)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			Integer VepodetailID=null;
			BigDecimal oldQuantityOrdered = new BigDecimal("0.0000");
			try {
				
				Vepo avepo=(Vepo) aSession.get(Vepo.class,theVepodetail.getVePoid());
				JoRelease jor = null;
				if(avepo.getJoReleaseId() !=null){
					jor=(JoRelease) aSession.get(JoRelease.class,avepo.getJoReleaseId());
				}
				if(oper.equals("add")){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					VepodetailID =(Integer) aSession.save(theVepodetail);
					aTransaction.commit();
					Boolean tplog=false;
					if(jor==null){
						//Outside Purchase Order
						insertPrMasterPrWareHouseInventoryForPO(theVepodetail.getVePoid(),VepodetailID);
						tplog=true;
					}else if(jor!=null && jor.getReleaseType()==1){
						//Update only drop ship
						/*Eric Said No need to update inside job for onorder
						 * ID #471
						 * Velmurugan
						 * 11-12-2015
						 * */
						/*insertPrMasterPrWareHouseInventoryForPO(theVepodetail.getVePoid(),VepodetailID);
						tplog=true;*/
					}

						if(VepodetailID>0 && tplog==true){
							Vepo aVepo=(Vepo) aSession.get(Vepo.class, theVepodetail.getVePoid());
							TpInventoryLog aTpInventoryLog = new TpInventoryLog();
							
							Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+theVepodetail.getPrMasterId());
							aTpInventoryLog.setPrMasterID(theVepodetail.getPrMasterId());
							aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
							aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
							aTpInventoryLog.setTransType("PO");
							aTpInventoryLog.setTransDecription("PO Created");
							aTpInventoryLog.setTransID(theVepodetail.getVePoid());
							aTpInventoryLog.setTransDetailID(VepodetailID);
							aTpInventoryLog.setProductOut(theVepodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?theVepodetail.getQuantityOrdered().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
							aTpInventoryLog.setProductIn(theVepodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?theVepodetail.getQuantityOrdered():new BigDecimal("0.0000"));
							aTpInventoryLog.setUserID(aVepo.getCreatedById());
							aTpInventoryLog.setCreatedOn(new Date());
							
							itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
							

							
							/*TpInventoryLogMaster
							 * Created on 04-12-2015
							 * Code Starts
							 * */
							BigDecimal qo=theVepodetail.getQuantityOrdered().subtract(theVepodetail.getQuantityBilled());
							Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
							Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
									BigDecimal.ZERO,qo,"PO",aVepo.getVePoid(),theVepodetail.getVePodetailId(),aVepo.getPonumber(),null ,
					/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
					/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
									"PO Created",theVepodetail.getUserId(),theVepodetail.getUserName(),
									new Date());
							itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
							/*Code Ends*/
						}

					
					
				}else if(oper.equals("edit")){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepodetail aVepodetail = (Vepodetail) aSession.get(Vepodetail.class, theVepodetail.getVePodetailId());
					oldQuantityOrdered = aVepodetail.getQuantityOrdered();
					aVepodetail.setDescription(theVepodetail.getDescription());
					aVepodetail.setQuantityOrdered(theVepodetail.getQuantityOrdered());
					aVepodetail.setUnitCost(theVepodetail.getUnitCost());
					aVepodetail.setPriceMultiplier(theVepodetail.getPriceMultiplier());
					aVepodetail.setNote(theVepodetail.getNote());
					aVepodetail.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());
					aVepodetail.setAcknowledgedDate(theVepodetail.getAcknowledgedDate());
					aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
					aVepodetail.setPrMasterId(theVepodetail.getPrMasterId());
					aVepodetail.setVePoid(theVepodetail.getVePoid()); 
					aVepodetail.setTaxable(theVepodetail.getTaxable());
					aVepodetail.setPosistion(theVepodetail.getPosistion());
					Boolean tplog=false;
					//RollBack
					if(jor==null){
						//Outside Purchase Order
						RollBackPrMasterPrWareHouseInventoryForPO(aVepodetail.getVePoid(),aVepodetail.getVePodetailId());
						tplog=true;
					}else if(jor!=null && jor.getReleaseType()==1){
						//Update only drop ship
						/*Eric Said No need to update inside job for onorder
						 * ID #471
						 * Velmurugan
						 * 11-12-2015
						 * */
						/*RollBackPrMasterPrWareHouseInventoryForPO(aVepodetail.getVePoid(),aVepodetail.getVePodetailId());
						tplog=true;*/
					}
					
					if(oldQuantityOrdered.compareTo(theVepodetail.getQuantityOrdered()) !=0 && tplog){
						Vepo aVepo=(Vepo) aSession.get(Vepo.class, aVepodetail.getVePoid());
						TpInventoryLog aTpInventoryLog = new TpInventoryLog();
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVepodetail.getPrMasterId());
						aTpInventoryLog.setPrMasterID(aVepodetail.getPrMasterId());
						aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
						aTpInventoryLog.setTransType("PO");
						aTpInventoryLog.setTransDecription("PO RollBack");
						aTpInventoryLog.setTransID(aVepodetail.getVePoid());
						aTpInventoryLog.setTransDetailID(aVepodetail.getVePodetailId());
						aTpInventoryLog.setProductOut(oldQuantityOrdered.compareTo(new BigDecimal("0.0000"))==1?oldQuantityOrdered:new BigDecimal("0.0000"));
						aTpInventoryLog.setProductIn(oldQuantityOrdered.compareTo(new BigDecimal("0.0000"))==-1?oldQuantityOrdered.multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						aTpInventoryLog.setUserID(aVepo.getCreatedById());
						aTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
						
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * RollBack
						 * */
						BigDecimal oqo=oldQuantityOrdered.subtract(aVepodetail.getQuantityBilled());
						Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
						Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
						TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
								aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,otheprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),otheprwarehsinventory.getInventoryOnHand(),
								BigDecimal.ZERO,oqo.multiply(new BigDecimal(-1)),"PO",aVepo.getVePoid(),aVepodetail.getVePodetailId(),aVepo.getPonumber(),null ,
				/*Product out*/(oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO,
				/*Product in*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
								"PO Edited",theVepodetail.getUserId(),theVepodetail.getUserName(),
								new Date());
						itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
						/*Code Ends*/
						}
					
					aSession.update(aVepodetail);
					aTransaction.commit();
					//add
					if(jor==null){
						//Outside Purchase Order
						insertPrMasterPrWareHouseInventoryForPO(aVepodetail.getVePoid(),aVepodetail.getVePodetailId());
						tplog=true;
					}else if(jor!=null && jor.getReleaseType()==1){
						//Update only drop ship
						/*Eric Said No need to update inside job for onorder
						 * ID #471
						 * Velmurugan
						 * 11-12-2015
						 * */
						/*insertPrMasterPrWareHouseInventoryForPO(aVepodetail.getVePoid(),aVepodetail.getVePodetailId());
						tplog=true;*/
					}
					
					if(oldQuantityOrdered.compareTo(theVepodetail.getQuantityOrdered()) !=0 && tplog){
						Vepo aVepo=(Vepo) aSession.get(Vepo.class, aVepodetail.getVePoid());
						TpInventoryLog theTpInventoryLog = new TpInventoryLog();
						Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVepodetail.getPrMasterId());
						theTpInventoryLog.setPrMasterID(theVepodetail.getPrMasterId());
						theTpInventoryLog.setProductCode(aPrmaster.getItemCode());
						theTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
						theTpInventoryLog.setTransType("PO");
						theTpInventoryLog.setTransDecription("PO Edited");
						theTpInventoryLog.setTransID(theVepodetail.getVePoid());
						theTpInventoryLog.setTransDetailID(theVepodetail.getVePodetailId());
						theTpInventoryLog.setProductOut(theVepodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?theVepodetail.getQuantityOrdered().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
						theTpInventoryLog.setProductIn(theVepodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?theVepodetail.getQuantityOrdered():new BigDecimal("0.0000"));
						theTpInventoryLog.setUserID(aVepo.getCreatedById());
						theTpInventoryLog.setCreatedOn(new Date());
						itsInventoryService.saveInventoryTransactions(theTpInventoryLog);
						
						
						/*TpInventoryLogMaster
						 * Created on 04-12-2015
						 * Code Starts
						 * */
						BigDecimal qo=theVepodetail.getQuantityOrdered().subtract(theVepodetail.getQuantityBilled());
						Prwarehouse theprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
						Prwarehouseinventory theprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
						TpInventoryLogMaster prmatpInventoryLogMstr=new  TpInventoryLogMaster(
								aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,theprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),theprwarehsinventory.getInventoryOnHand(),
								BigDecimal.ZERO,qo,"PO",aVepo.getVePoid(),theVepodetail.getVePodetailId(),aVepo.getPonumber(),null ,
				/*Product out*/(qo.compareTo(BigDecimal.ZERO)<0)?qo.multiply(new BigDecimal(-1)):BigDecimal.ZERO,
				/*Product in*/(qo.compareTo(BigDecimal.ZERO)>0)?qo:BigDecimal.ZERO ,
								"PO Edited",theVepodetail.getUserId(),theVepodetail.getUserName(),
								new Date());
						itsInventoryService.addTpInventoryLogMaster(prmatpInventoryLogMstr);
						/*Code Ends*/
						}
					
				}else if(oper.equals("delete")){
						aTransaction = aSession.beginTransaction();
						aTransaction.begin();
						Vepodetail aVepodetail = (Vepodetail) aSession.get(Vepodetail.class, theVepodetail.getVePodetailId());
						Boolean tplog=false;
						//RollBack
						if(jor==null){
							//Outside Purchase Order
							RollBackPrMasterPrWareHouseInventoryForPO(aVepodetail.getVePoid(),aVepodetail.getVePodetailId());
							tplog=true;
						}else if(jor!=null && jor.getReleaseType()==1){
							//Update only drop ship
							/*Eric Said No need to update inside job for onorder
							 * ID #471
							 * Velmurugan
							 * 11-12-2015
							 * */
							/*RollBackPrMasterPrWareHouseInventoryForPO(aVepodetail.getVePoid(),aVepodetail.getVePodetailId());
							tplog=true;*/
						}
						if(aVepodetail.getVePodetailId()>0 && tplog){
							Vepo aVepo=(Vepo) aSession.get(Vepo.class, aVepodetail.getVePoid());
							TpInventoryLog aTpInventoryLog = new TpInventoryLog();
							
							Prmaster aPrmaster =  productService.getProducts(" WHERE prMasterID="+aVepodetail.getPrMasterId());
							aTpInventoryLog.setPrMasterID(aVepodetail.getPrMasterId());
							aTpInventoryLog.setProductCode(aPrmaster.getItemCode());
							aTpInventoryLog.setWareHouseID(aVepo.getPrWarehouseId());
							aTpInventoryLog.setTransType("PO");
							aTpInventoryLog.setTransDecription("PO Deleted");
							aTpInventoryLog.setTransID(aVepodetail.getVePoid());
							aTpInventoryLog.setTransDetailID(aVepodetail.getVePodetailId());
							aTpInventoryLog.setProductOut(aVepodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==1?aVepodetail.getQuantityOrdered():new BigDecimal("0.0000"));
							aTpInventoryLog.setProductIn(aVepodetail.getQuantityOrdered().compareTo(new BigDecimal("0.0000"))==-1?aVepodetail.getQuantityOrdered().multiply(new BigDecimal(-1)):new BigDecimal("0.0000"));
							aTpInventoryLog.setUserID(aVepo.getCreatedById());
							aTpInventoryLog.setCreatedOn(new Date());
							itsInventoryService.saveInventoryTransactions(aTpInventoryLog);
							
							
							/*TpInventoryLogMaster
							 * Created on 04-12-2015
							 * Code Starts
							 * RollBack
							 * */
							BigDecimal oqo=aVepodetail.getQuantityOrdered().subtract(aVepodetail.getQuantityBilled());
							Prwarehouse otheprwarehouse=(Prwarehouse) aSession.get(Prwarehouse.class, aVepo.getPrWarehouseId());
							Prwarehouseinventory otheprwarehsinventory=itsInventoryService.getPrwarehouseInventory(aVepo.getPrWarehouseId(), aPrmaster.getPrMasterId());
							TpInventoryLogMaster oprmatpInventoryLogMstr=new  TpInventoryLogMaster(
									aPrmaster.getPrMasterId(),aPrmaster.getItemCode(),aVepo.getPrWarehouseId() ,otheprwarehouse.getSearchName(),aPrmaster.getInventoryOnHand(),otheprwarehsinventory.getInventoryOnHand(),
									BigDecimal.ZERO,oqo.multiply(new BigDecimal(-1)),"PO",aVepo.getVePoid(),aVepodetail.getVePodetailId(),aVepo.getPonumber(),null ,
					/*Product out*/(oqo.compareTo(BigDecimal.ZERO)>0)?oqo:BigDecimal.ZERO,
					/*Product in*/(oqo.compareTo(BigDecimal.ZERO)<0)?oqo.multiply(new BigDecimal(-1)):BigDecimal.ZERO ,
									"PO Deleted",theVepodetail.getUserId(),theVepodetail.getUserName(),
									new Date());
							itsInventoryService.addTpInventoryLogMaster(oprmatpInventoryLogMstr);
							/*Code Ends*/
						}
						
						aSession.delete(aVepodetail);
						aTransaction.commit();
				}
				
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		@Override
		public boolean updatevePOfromlinesTab(Vepo theVepo)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepo aVepo = (Vepo) aSession.get(Vepo.class, theVepo.getVePoid());
					aVepo.setSubtotal(theVepo.getSubtotal());
					aVepo.setFreight(theVepo.getFreight());
					aVepo.setTaxTotal(theVepo.getTaxTotal());
					aSession.update(aVepo);
					aTransaction.commit();
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		public boolean insertPrMasterPrWareHouseInventoryForPO(Integer VepoID,Integer VepoDetailID)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepo theVepo=(Vepo) aSession.get(Vepo.class, VepoID);
					Vepodetail theVepodetail = (Vepodetail) aSession.get(Vepodetail.class, VepoDetailID);
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class, theVepodetail.getPrMasterId());
					if(objPrmaster.getIsInventory()==1){
					BigDecimal order = objPrmaster.getInventoryOnOrder();
					objPrmaster.setInventoryOnOrder(order==null?new BigDecimal("0.0000").add(theVepodetail.getQuantityOrdered().subtract(
							theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
							)):
								order.add(theVepodetail.getQuantityOrdered().subtract(
									theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
									))
					);
					aSession.update(objPrmaster);
					
					
					
					
					Prwarehouseinventory thePrwarehouseinventory =new Prwarehouseinventory();
					String prwarehouseQuery="FROM Prwarehouseinventory WHERE prMasterID ="+theVepodetail.getPrMasterId()+" And prWarehouseID="+theVepo.getPrWarehouseId();
					Query aQuery = aSession.createQuery(prwarehouseQuery);
					ArrayList<Prwarehouseinventory> Prwarehouseinventorylist = (ArrayList<Prwarehouseinventory>) aQuery.list();
					if (!Prwarehouseinventorylist.isEmpty()) {
						Prwarehouseinventory aPrwarehouseinventory = Prwarehouseinventorylist.get(0);
						thePrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, aPrwarehouseinventory.getPrWarehouseInventoryId());
						BigDecimal Order = thePrwarehouseinventory.getInventoryOnOrder();
						thePrwarehouseinventory.setInventoryOnOrder(
								Order==null?new BigDecimal("0.0000").add(theVepodetail.getQuantityOrdered().subtract(
										theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
										)):
											Order.add(theVepodetail.getQuantityOrdered().subtract(
													theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
												))
								);
						aSession.update(thePrwarehouseinventory);
					}
					aTransaction.commit();
					
					}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		public boolean RollBackPrMasterPrWareHouseInventoryForPO(Integer vePOID,Integer vePODetailID)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepo theVepo=(Vepo) aSession.get(Vepo.class, vePOID);
					Vepodetail theVepodetail = (Vepodetail) aSession.get(Vepodetail.class, vePODetailID);
					Prmaster objPrmaster = (Prmaster) aSession.get(Prmaster.class, theVepodetail.getPrMasterId());
					if(objPrmaster.getIsInventory()==1){
					BigDecimal Order = objPrmaster.getInventoryOnOrder();
					objPrmaster.setInventoryOnOrder(Order==null?new BigDecimal("0.0000").subtract(theVepodetail.getQuantityOrdered().subtract(
							theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
							)):
								Order.subtract(theVepodetail.getQuantityOrdered().subtract(
										theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
									))
					);
					aSession.update(objPrmaster);
					
					String prwarehouseQuery="FROM Prwarehouseinventory WHERE prMasterID ="+theVepodetail.getPrMasterId()+" And prWarehouseID="+theVepo.getPrWarehouseId();
					Query aQuery = aSession.createQuery(prwarehouseQuery);
					ArrayList<Prwarehouseinventory> Prwarehouseinventorylist = (ArrayList<Prwarehouseinventory>) aQuery.list();
					if (!Prwarehouseinventorylist.isEmpty()) {
						Prwarehouseinventory aPrwarehouseinventory = Prwarehouseinventorylist.get(0);
						Prwarehouseinventory thePrwarehouseinventory = (Prwarehouseinventory) aSession.get(Prwarehouseinventory.class, aPrwarehouseinventory.getPrWarehouseInventoryId());
						BigDecimal prorder = thePrwarehouseinventory.getInventoryOnOrder();
						thePrwarehouseinventory.setInventoryOnOrder(
								prorder==null?new BigDecimal("0.0000").subtract(theVepodetail.getQuantityOrdered().subtract(
										theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
										)):
											prorder.subtract(theVepodetail.getQuantityOrdered().subtract(
												theVepodetail.getQuantityBilled()==null?BigDecimal.ZERO:theVepodetail.getQuantityBilled()
												))
								);
						aSession.update(thePrwarehouseinventory);
					}
					aTransaction.commit();
					}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		
		@Override
		public boolean updatevePODetailfromAckTab(Vepodetail theVepodetail)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepodetail aVepodetail = (Vepodetail) aSession.get(Vepodetail.class,theVepodetail.getVePodetailId());
					aVepodetail.setAcknowledgedDate(theVepodetail.getAcknowledgedDate());
					aVepodetail.setEstimatedShipDate(theVepodetail.getEstimatedShipDate());
					aVepodetail.setVendorOrderNumber(theVepodetail.getVendorOrderNumber());
					aSession.update(aVepodetail);
					aTransaction.commit();
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}

		
		
		
		@Override
		public void updatejowizardAppletData(JoWizardAppletData ajowizAppletData)
				{
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					String Query="SELECT id FROM `joWizardAppletData` WHERE joMasterID='"+ajowizAppletData.getJoMasterID()+"'";
					
					Query aQuery = aSession.createSQLQuery(Query);
					List<?> aList = aQuery.list();
					Integer joWizarappID = null;
					if(aList!=null&&aList.size()>0)
						joWizarappID = (Integer) aList.get(0);
					
					if(joWizarappID==null){
						aSession.save(ajowizAppletData);
					}else{
						JoWizardAppletData aJoWizardAppletData = (JoWizardAppletData) aSession.get(JoWizardAppletData.class,joWizarappID);
						aJoWizardAppletData.setAppletLocalUrl(ajowizAppletData.getAppletLocalUrl());
						aJoWizardAppletData.setJobNumber(ajowizAppletData.getJobNumber());
						aJoWizardAppletData.setUserLoginId(ajowizAppletData.getUserLoginId());
						aJoWizardAppletData.setJoMasterID(ajowizAppletData.getJoMasterID());
						aSession.update(aJoWizardAppletData);
					}
					
					
					aTransaction.commit();
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
			} finally {
				aSession.flush();
				aSession.close();
			}
		}

		@Override
		public Integer getCoTaxterritoryIdfmrxmasterid(Integer rxMasterID)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Integer taxid = 0;
			
			try {
				taxid = (Integer)aSession.createSQLQuery("SELECT coTaxTerritoryID FROM rxAddress WHERE rxMasterID = "+rxMasterID+" AND IsDefault = 1").uniqueResult();	
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			
			return taxid;
		}
		
		
		@Override
		public Jomaster getjoMasterAddressDetails(Integer joReleaseDetailID) {
			Session aSession = null;
			Jomaster ajomaster=null;
			try {
				aSession = itsSessionFactory.openSession();
				String Query="select joMaster.joMasterID from joMaster join joRelease on(joMaster.joMasterID=joRelease.joMasterID) join joReleaseDetail on(joRelease.joReleaseID=joReleaseDetail.joReleaseID) where joReleaseDetail.joReleaseDetailID="+joReleaseDetailID;
				
				Query aQuery = aSession.createSQLQuery(Query);
				List<?> aList = aQuery.list();
				if(aList!=null&&aList.size()>0){
					Integer joMasterID = (Integer) aList.get(0);
					ajomaster = (Jomaster) aSession.get(Jomaster.class,joMasterID);
				}
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return ajomaster;
		}
		public Integer getCustomerTaxTerritoryIDWithShipTo(Integer rxMasterID){
			Session aSession = null;
			Jomaster ajomaster=null;
			Integer coTaxTerritoryID=null;
			try{
				aSession = itsSessionFactory.openSession();
				String Query="SELECT `coTaxTerritoryID` FROM rxMaster JOIN rxAddress ON(rxMaster.rxMasterID=rxAddress.rxMasterID) WHERE  `IsShipTo`=1 AND `IsDefault`=1 AND rxMaster.rxMasterID="+rxMasterID;
				
				Query aQuery = aSession.createSQLQuery(Query);
				List<?> aList = aQuery.list();
				if(aList!=null&&aList.size()>0){
					 coTaxTerritoryID = (Integer) aList.get(0);
				}
			}catch(Exception e){
				itsLogger.error(e.getMessage(), e);
			}finally{
				aSession.flush();
				aSession.close();
			}
			return coTaxTerritoryID;
		}
		
		@Override
		public CoTaxTerritory getgetoverride_taxterritoryTaxID(Integer customerID) {
			Session aSession = null;
			CoTaxTerritory aCoTaxTerritory=null;
			try {
				aSession = itsSessionFactory.openSession();
				String Query="SELECT coTaxTerritory.coTaxTerritoryID,coTaxTerritory.County,coTaxTerritory.TaxRate FROM rxAddress Left JOIN coTaxTerritory ON(rxAddress.coTaxTerritoryID=coTaxTerritory.coTaxTerritoryID) WHERE rxAddress.IsDefault=1 AND rxAddress.IsShipTo=1 AND rxAddress.rxMasterID="+customerID;
				Query aQuery = aSession.createSQLQuery(Query);
				Iterator<?> aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aCoTaxTerritory=new CoTaxTerritory();
					if(aObj[0]!=null){
						aCoTaxTerritory.setCoTaxTerritoryId((Integer) aObj[0]);
						aCoTaxTerritory.setCounty((String) aObj[1]);
						aCoTaxTerritory.setTaxRate((BigDecimal) aObj[2]);
					}else{
						aCoTaxTerritory.setCoTaxTerritoryId(0);
					}
					
				}
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aCoTaxTerritory;
		}
		
		@Override
		public Integer updateInvoiceLineItems(Integer cuInvoiceID,Integer Operation) {
			Session aSession = null;
			try {
				if(cuInvoiceID !=null && cuInvoiceID>0){
				aSession = itsSessionFactory.openSession();
				String aSummaryQry = "UPDATE cuInvoiceDetail SET Taxable ="+Operation+" WHERE cuInvoiceID = "+cuInvoiceID;
				itsLogger.info(" Update Query = "+aSummaryQry);
				Transaction aTransaction = aSession.beginTransaction();
				aSession.createSQLQuery(aSummaryQry).executeUpdate();
				aTransaction.commit();
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return 1;
		}
		
		@Override
		public Integer getCommPaidDetails(Integer joReleaseDetailID) {
			Session aSession = null;
			Integer paidStatus = 0;
			try {
				aSession = itsSessionFactory.openSession();
				String aJoInvoiceCostQuery="SELECT joInvoiceCost.ecInvoicePeriodID FROM joInvoiceCost "
						+ "WHERE joInvoiceCost.joReleaseDetailID="+joReleaseDetailID +" AND joInvoiceCost.ecInvoicePeriodID IS NOT NULL";
				String aVeCommDetailQuery="SELECT veCommDetail.ecInvoicePeriodID FROM veCommDetail "
						+ "WHERE veCommDetail.joReleaseDetailID="+joReleaseDetailID+" AND veCommDetail.ecInvoicePeriodID IS NOT NULL";
				Query aQuery = aSession.createSQLQuery(aJoInvoiceCostQuery);
				Query bQuery = aSession.createSQLQuery(aVeCommDetailQuery);
				List<?> aList = aQuery.list();
				List<?> bList = bQuery.list();
				if(aList!=null&&aList.size()>0){
					paidStatus = 1;
				}
				if(bList!=null&&bList.size()>0){
					paidStatus = 1;
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return paidStatus;
		}
		
		@Override
		public Integer getCommPaidReleaseDetails(Integer joReleaseID,Integer joMasterID) {
			Session aSession = null;
			Integer paidStatus = 0;
			try {
				aSession = itsSessionFactory.openSession();
				String aJoInvoiceCostQuery="SELECT ecp.ecInvoicePeriodID FROM ecInvoicePeriod ecp JOIN cuInvoice cui ON ecp.cuInvoiceID = cui.cuInvoiceID "
						+ "JOIN joReleaseDetail jor ON jor.joReleaseDetailID = cui.joReleaseDetailID "
						+ "WHERE jor.joReleaseID = "+joReleaseID;
				Query aQuery = aSession.createSQLQuery(aJoInvoiceCostQuery);
				List<?> aList = aQuery.list();
				if(aList!=null && aList.size()>0){
					paidStatus = 1;
				}else if((joReleaseID==null||joReleaseID<1) && (joMasterID!=null && joMasterID>0)){
					String aVeCommDetailQuery="SELECT ecp.ecInvoicePeriodID FROM ecInvoicePeriod ecp JOIN cuInvoice cui ON ecp.cuInvoiceID = cui.cuInvoiceID "
							+ "JOIN joReleaseDetail jor ON jor.joReleaseDetailID = cui.joReleaseDetailID "
							+ "JOIN joRelease jos ON jor.joReleaseID = jos.joReleaseID WHERE jos.joMasterID = "+joMasterID;
					Query bQuery = aSession.createSQLQuery(aVeCommDetailQuery);
					List<?> bList = bQuery.list();
					if(bList!=null&&bList.size()>0){
						paidStatus = 1;
					}
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return paidStatus;
		}
		
		@Override
		public Integer updateCommissionReceived(JoRelease aJoRelease) throws JobException {

			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction = null;
			try {
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					JoRelease theJoRelease = (JoRelease) aSession.get(JoRelease.class,aJoRelease.getJoReleaseId());
					theJoRelease.setCommissionReceived(aJoRelease.getCommissionReceived());
					aSession.update(theJoRelease);
					aTransaction.commit();
				
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return 1;
		
			
		}
		
		@Override
		public ArrayList<joQuoteDetailMstr> getjoQuoteDetailMstrFromTemplate(Integer quoteTemplateHeaderID) throws JobException {
			String aPOLineItemListQry = "";
			if (quoteTemplateHeaderID != null) {
				aPOLineItemListQry = "SELECT jmstr.type,jmstr.quantity,jmstr.textbox,jmstr.texteditor,jmstr.sellprice,jmstr.cost,jmstr.manufacturer,jmstr.position ,rxm.Name,jmstr.category,linebreak "
									+"FROM joQuoteTempDetailMstr jmstr LEFT JOIN rxMaster rxm ON(jmstr.manufacturer=rxm.rxMasterID) WHERE  joQuoteTemplateHeaderID="+quoteTemplateHeaderID+" ORDER BY jmstr.position";
			}
			Session aSession = null;
			ArrayList<joQuoteDetailMstr> aQueryList = new ArrayList<joQuoteDetailMstr>();
			try {
				joQuoteDetailMstr ajoQuoteDetailMstr = null;
				aSession = itsSessionFactory.openSession();
				if (quoteTemplateHeaderID != null) {
				Query aQuery = aSession.createSQLQuery(aPOLineItemListQry);
				List<?> aList = aQuery.list();
				if (aPOLineItemListQry.length() > 0)
					if (!aList.isEmpty()) {
						Iterator<?> aIterator = aList.iterator();
						while (aIterator.hasNext()) {
							ajoQuoteDetailMstr=new joQuoteDetailMstr();
							Object[] aObj = (Object[]) aIterator.next();
							Integer type=ConvertintoInteger(aObj[0].toString());
							String Quantity="";
							if(aObj[1]!=null){
								Quantity=(String) aObj[1];
							}
							String textbox="";
							if(aObj[2]!=null){
								textbox=(String) aObj[2];
							}
							String texteditor="";
							if(aObj[3]!=null){
								texteditor=(String) aObj[3];
							}
							if(aObj[4]==null){
								aObj[4]="";
							}
							BigDecimal sellprice=ConvertintoBigDecimal(aObj[4].toString());
							if(aObj[5]==null){
								aObj[5]="";
							}
							BigDecimal cost=ConvertintoBigDecimal(aObj[5].toString());
							Integer manufacturer=ConvertintoInteger(aObj[6].toString());
							Integer position=ConvertintoInteger(aObj[7].toString());
							String vendorname="";
							if(aObj[8]!=null){
								vendorname=(String)aObj[8];
							}
							
							
							String typename="";
							if(type==1){
								typename="Title";
							}else if(type==2){
								typename="Item2";
							}else if(type==3){
								typename="Item3";
							}else if(type==4){
								typename="Price";
							}
							
							
							org.jsoup.nodes.Document doc = Jsoup.parse(texteditor);
							String text = doc.body().text();
							ajoQuoteDetailMstr.setTypename(typename);
							ajoQuoteDetailMstr.setType(type);
							ajoQuoteDetailMstr.setQuantity(Quantity);
							ajoQuoteDetailMstr.setTextbox(textbox);
							ajoQuoteDetailMstr.setTexteditor(texteditor);
							ajoQuoteDetailMstr.setDescription(text);
							ajoQuoteDetailMstr.setSellprice(sellprice);
							ajoQuoteDetailMstr.setCost(cost);
							ajoQuoteDetailMstr.setManufacturer(manufacturer);
							ajoQuoteDetailMstr.setPosition(position);
							ajoQuoteDetailMstr.setVendorname(vendorname);
							if(aObj[9]==null){
								aObj[9]=-1;
							}
							ajoQuoteDetailMstr.setCategory(ConvertintoInteger(aObj[9].toString()));
							boolean linebreak=false;
							if(aObj[10]!=null && (Byte)aObj[10]==1){
								linebreak=true;
							}
							ajoQuoteDetailMstr.setLinebreak(linebreak);
							aQueryList.add(ajoQuoteDetailMstr);
						}
					}
				}
			} catch (Exception e) {
				itsLogger.error("Exception while getting the Quote Copy LineItem list: "
						+ e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aPOLineItemListQry=null;
			}
			return aQueryList;
		}
		
		@Override
		public String getMaxInvoiceNumber() throws JobException {
			Session aSession = null;
			BigInteger maxInvoiceNumber = new BigInteger("0");
			try {
				aSession = itsSessionFactory.openSession();
				String aJoJournalStr = "SELECT MAX(CAST(InvoiceNumber AS UNSIGNED)) AS MaxInvoiceNumber FROM cuInvoice WHERE CONCAT('',InvoiceNumber * 1) = InvoiceNumber";
				Query aQuery = aSession.createSQLQuery(aJoJournalStr);
				List<?> aList = aSession.createSQLQuery(aJoJournalStr).list();
				if(aList.size()>0){
					maxInvoiceNumber = (BigInteger) aList.get(0);
				}
				else{
					maxInvoiceNumber = new BigInteger("0");
				}
				}
			catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return maxInvoiceNumber+"";
		}
		
		@Override
		public Map<String, String> getjoMasterByJoreleaseDetailID(Integer joreleasedetailid){
			Map<String,String> hmap=null;
			Session aSession = null;
			try {
				aSession = itsSessionFactory.openSession();
				String aJoJournalStr = "SELECT jm.`JobNumber`,jm.`Description`,jos.`JobStatus`,jm.joMasterID FROM joReleaseDetail jrd"
										+" LEFT JOIN joRelease jr ON(jrd.joReleaseID=jr.joReleaseID)"
										+" LEFT JOIN joMaster jm ON(jr.joMasterID=jm.joMasterID)"
										+" LEFT JOIN joStatus jos ON(jm.`joBidStatusID`=jos.`joStatusID`)"
										+" WHERE joReleaseDetailID="+joreleasedetailid;
				Query aQuery = aSession.createSQLQuery(aJoJournalStr);
				List<?> aList = aSession.createSQLQuery(aJoJournalStr).list();
				if(aList.size()>0){
					 hmap=new HashMap<String, String>();
					 Iterator<?> aIterator = aList.iterator();
						if (aIterator.hasNext()) {
							Object[] aObj = (Object[]) aIterator.next();
								hmap.put("jobNumber", (String)aObj[0]);
								hmap.put("jobName",  (String)aObj[1]);
								hmap.put("jobStatus",  (String)aObj[2]);
								hmap.put("joMasterID",  (String)aObj[3]);
						}
				}
				
				}
			catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return hmap;
		}
		
		public TpCuinvoiceLogMaster getDataforInvoiceLog(Integer cuInvoiceID)
				throws JobException {
			String aSelectQry = "SELECT cui.InvoiceNumber,jor.joReleaseID,jom.JobNumber,jom.Description, cso.SoNumber, "
					+ "vpo.PoNumber, rxm.Name, tsu.FullName FROM cuInvoice cui LEFT JOIN joReleaseDetail jor ON "
					+ "cui.joReleaseDetailID = jor.joReleaseDetailID LEFT JOIN joMaster jom ON jor.joMasterID = jom.joMasterID "
					+ "LEFT JOIN cuSO cso ON cui.cuSOID = cso.cuSOID LEFT JOIN vePO vpo ON jor.joReleaseID = vpo.joReleaseID "
					+ "LEFT JOIN rxMaster rxm ON cui.rxCustomerID = rxMasterID LEFT JOIN tsUserLogin tsu ON cui.cuAssignmentID0 = tsu.UserLoginID "
					+ "WHERE cuInvoiceID = "+ cuInvoiceID;
			Session aSession = null;
			TpCuinvoiceLogMaster aTpCuinvoiceLogMaster = new TpCuinvoiceLogMaster();
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aSelectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[]) aIterator.next();
					aTpCuinvoiceLogMaster.setInvoiceNumber((String) aObj[0]);
					aTpCuinvoiceLogMaster.setJoReleaseID((Integer) aObj[1]);
					aTpCuinvoiceLogMaster.setJobNumber((String) aObj[2]);
					aTpCuinvoiceLogMaster.setJobName((String) aObj[3]);
					aTpCuinvoiceLogMaster.setSoNumber((String) aObj[4]);
					aTpCuinvoiceLogMaster.setPONumber((String) aObj[5]);
					aTpCuinvoiceLogMaster.setCustomerName((String) aObj[6]);
					aTpCuinvoiceLogMaster.setSalesPerson((String) aObj[7]);
				}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aSelectQry=null;
			}
			return aTpCuinvoiceLogMaster;
		}
		
		public CoTaxTerritory getCoTaxTerritoryDetails(Integer coTaxTerritoryID) {
			Session aSession = null;
			CoTaxTerritory aCoTaxTerritory=null;
			Query query = null;
			try {
				aSession = itsSessionFactory.openSession();
				if(coTaxTerritoryID>0)
					{
						aCoTaxTerritory = (CoTaxTerritory) aSession.get(CoTaxTerritory.class,coTaxTerritoryID);
					}
				else
				{
					query = aSession.createQuery("from CoTaxTerritory c where c.county like :county");
					query.setParameter("county", "EXEMPT");
					aCoTaxTerritory = (CoTaxTerritory) query.uniqueResult();
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aCoTaxTerritory;
		}
		

		public String getalternatetextinhtml(String theJoQuoteHeaderID){
			String returnvalue=null;
			Session aSession = null;
			try {
				aSession = itsSessionFactory.openSession();
				String aJoJournalStr = "SELECT `Address1`,`Address2`,`Address3`,`Address4` FROM `joQuoteHeader` jqh "+
									   " JOIN joMaster jo USING(joMasterID) JOIN coDivision co USING(coDivisionID) "+
									   " WHERE jqh.joQuoteHeaderID="+theJoQuoteHeaderID+" AND co.AddressQuote=1";
				Query aQuery = aSession.createSQLQuery(aJoJournalStr);
				List<?> aList = aSession.createSQLQuery(aJoJournalStr).list();
				if(aList.size()>0){
					 Iterator<?> aIterator = aList.iterator();
						if (aIterator.hasNext()) {
							Object[] aObj = (Object[]) aIterator.next();
							//<div>
							//<div><font face="arial" size="1"><i><b>10350 Olympic Drive, Dallas TX 75220-6040 (214) 350-6871                         </b></i></font></div>
							//<div><font face="arial" size="1"><i><b>3206 Longhorn Blvd, Austin TX 78758  (512) 774-5853  </b></i></font></div>
							//<div><font face="arial" size="1"><i><b>2901 Wesley Way, Fort Worth, TX 76118-6955  (682) 253-0122            </b></i></font></div>
							//<div><font face="arial" size="1"><i><b>8788 Westpark Drive, Houston TX 77063  (713) 487-0065    </b></i></font></div>
							//</div>
							returnvalue="<div>";
							if(aObj[0]!=null){
								returnvalue=returnvalue+"<div><span style='font-family: Times New Roman, serif; font-size: 12pt;'>"+aObj[0]+"</span></div>";
							}else{
								returnvalue=returnvalue+"<div><br></div>";
							}
							if(aObj[1]!=null){
								returnvalue=returnvalue+"<div><span style='font-family: Times New Roman, serif; font-size: 12pt;'>"+aObj[1]+"</span></div>";
							}else{
								returnvalue=returnvalue+"<div><br></div>";
							}
							if(aObj[2]!=null){
								returnvalue=returnvalue+"<div><span style='font-family: Times New Roman, serif; font-size: 12pt;'>"+aObj[2]+"</span></div>";
							}else{
								returnvalue=returnvalue+"<div><br></div>";
							}
							if(aObj[3]!=null){
								returnvalue=returnvalue+"<div><span style='font-family: Times New Roman, serif; font-size: 12pt;'>"+aObj[3]+"</span></div>";
							}else{
								returnvalue=returnvalue+"<div><br></div>";
							}
							returnvalue=returnvalue+"<div><br></div></div>";
						}
				}
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnvalue;
		}
	
		@Override
		public JobQuotesBidListBean getjoQuoteDetailForPDF(Integer joQuoteHeaderID)
				throws JobException {
			String aJobSelectQry = "SELECT cuMasterType.Code, "
					+ " j.QuoteRev, "
					+ " j.CostAmount, "
					+ " j.QuoteAmount, "
					+ " j.CreatedByName, "
					+ " j.InternalNote, "
					+ " j.PrimaryQuote, "
					+ " j.joQuoteHeaderID, "
					+ " j.cuMasterTypeID, "
					+ " j.CreatedByID, (SELECT FullName AS CreatedBYFullName FROM tsUserLogin WHERE UserLoginID = j.CreatedByID  AND LoginName != 'admin'), j.Remarks,j.DiscountedPrice, "
					+ " j.DisplayQuantity , j.PrintQuantity,"
					+ " j.DisplayParagraph, j.PrintParagraph,"
					+ " j.DisplayManufacturer, j.PrintManufacturer,"
					+ " j.DisplaySpec, j.PrintSpec,"
					+ " j.DisplayCost, j.PrintCost,"
					+ " j.DisplayPrice, j.PrintPrice,"
					+ " j.DisplayMult, j.PrintMult,"
					+ " j.NotesFullWidth,"
					+ " j.LineNumbers,"
					+ " j.PrintTotal,"
					+ " j.HidePrice "
					+ " FROM joQuoteHeader j "
					+ " JOIN cuMasterType ON cuMasterType.cuMasterTypeID = j.cuMasterTypeID "
					+ " WHERE  j.joQuoteHeaderID="+joQuoteHeaderID;
			itsLogger.info("Query : " + aJobSelectQry);
			Session aSession = null;
			JobQuotesBidListBean aJobQuotesBidListBean = null;
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aJobSelectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					aJobQuotesBidListBean = new JobQuotesBidListBean();
					Object[] aObj = (Object[]) aIterator.next();
					aJobQuotesBidListBean.setJoQuoteHeaderID((Integer) aObj[7]);
					aJobQuotesBidListBean.setQuoteType((String) aObj[0]);
					aJobQuotesBidListBean.setRev((String) aObj[1]);
					aJobQuotesBidListBean.setCostAmount((BigDecimal) aObj[2]);
					aJobQuotesBidListBean.setQuoteAmount((BigDecimal) aObj[3]);
					aJobQuotesBidListBean.setCreatedByName((String) aObj[4]);
					aJobQuotesBidListBean.setInternalNote((String) aObj[5]);
					aJobQuotesBidListBean.setQuoteTypeID((Integer) aObj[8]);
					aJobQuotesBidListBean.setCreatedByID((Integer) aObj[9]);
					aJobQuotesBidListBean.setCreatedBYFullName((String) aObj[10]);
					aJobQuotesBidListBean.setQuoteRemarks((String) aObj[11]);
					aJobQuotesBidListBean.setDiscountAmount((BigDecimal) aObj[12]);
					aJobQuotesBidListBean.setDisplayQuantity((Byte) aObj[13]);
					aJobQuotesBidListBean.setPrintQuantity((Byte) aObj[14]);
					aJobQuotesBidListBean.setDisplayParagraph((Byte) aObj[15]);
					aJobQuotesBidListBean.setPrintParagraph((Byte) aObj[16]);
					aJobQuotesBidListBean.setDisplayManufacturer((Byte) aObj[17]);
					aJobQuotesBidListBean.setPrintManufacturer((Byte) aObj[18]);
					aJobQuotesBidListBean.setDisplaySpec((Byte) aObj[19]);
					aJobQuotesBidListBean.setPrintSpec((Byte) aObj[20]);
					aJobQuotesBidListBean.setDisplayCost((Byte) aObj[21]);
					aJobQuotesBidListBean.setPrintCost((Byte) aObj[22]);
					aJobQuotesBidListBean.setDisplayPrice((Byte) aObj[23]);
					aJobQuotesBidListBean.setPrintPrice((Byte) aObj[24]);
					aJobQuotesBidListBean.setDisplayMult((Byte) aObj[25]);
					aJobQuotesBidListBean.setPrintMult((Byte) aObj[26]);
					aJobQuotesBidListBean.setNotesFullWidth((Byte) aObj[27]);
					aJobQuotesBidListBean.setLineNumbers((Byte) aObj[28]);
					aJobQuotesBidListBean.setPrintTotal((Byte) aObj[29]);
					aJobQuotesBidListBean.setHidePrice((Byte) aObj[30]);
					if ((Byte) aObj[6] == 1) {
						aJobQuotesBidListBean.setPrimaryQuote(true);
					} else {
						aJobQuotesBidListBean.setPrimaryQuote(false);
					}
					;
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aJobSelectQry=null;
			}
			return aJobQuotesBidListBean;
		}

		@Override
		public List<?> getjobSearchResultsJob(String theJobNumber, String theJobName,Integer joMasterID)
				throws JobException {
			String aJobSelectQry = "SELECT joMaster.joMasterId, joMaster.jobNumber,"
					+ " joMaster.Description, joMaster.locationName, joMaster.locationCity,"
					+ " tsUserLogin.Initials, joStatus.JobStatus, rxMaster.Name,"
					+ " joMaster.CustomerPONumber, coDivision.Code, joMaster.SourceReport1,"
					+ " joMaster.bookedDate, joMaster.contractAmount, joMaster.estimatedCost"
					+ " FROM rxMaster RIGHT OUTER JOIN joStatus RIGHT OUTER JOIN coDivision RIGHT OUTER JOIN joMaster"
					+ " ON coDivision.coDivisionID = joMaster.coDivisionID ON joStatus.joStatusID = joMaster.JobStatus"
					+ " ON rxMaster.rxMasterID = joMaster.rxCustomerID LEFT OUTER JOIN tsUserLogin"
					+ " ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID WHERE joMaster.jobNumber = '"
					+ theJobNumber + "' and joMaster.joMasterID="+joMasterID;
			Session aSession = null;
			ArrayList<JobCustomerBean> aQueryList = new ArrayList<JobCustomerBean>();
			try {
				JobCustomerBean aJobCustomerBean = null;
				// Retrieve aSession from Hibernate
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aJobSelectQry);
				/**
				 * In general you are not supposed to get the column names.
				 * Actually, that's exactly what an O2R mapper is about - hiding the
				 * R details and showing only O specifics.
				 **/
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					aJobCustomerBean = new JobCustomerBean();
					Object[] aObj = (Object[]) aIterator.next();
					aJobCustomerBean.setJoMasterId((Integer) aObj[0]);
					aJobCustomerBean.setJobNumber((String) aObj[1]);
					aJobCustomerBean.setDescription((String) aObj[2]);
					aJobCustomerBean.setJobStatus((String) aObj[6]);
					/** JobStatus */
					aJobCustomerBean.setCustomerName((String) aObj[7]);
					/** Name */
					aQueryList.add(aJobCustomerBean);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
				aJobSelectQry=null;
			}
			return aQueryList;
		}
		
		@Override
		public Jomaster getSingleJobDetails(String theJobNumber,Integer aJoMasterId)
				throws JobException {
			Session aSession = null;
			Jomaster aJoMaster = new Jomaster();
			try {
				

				itsLogger.info("getSingleJobDetails()=[Connection Opened]");
				aSession = itsSessionFactory.openSession();
				if (aJoMasterId != 0) {
					aJoMaster = (Jomaster) aSession
							.get(Jomaster.class, aJoMasterId);
					if (aJoMaster != null) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						
						if (aJoMaster.getRequestedNocdate() != null) {
							itsLogger.info("Requested NOC Date in DB::"+aJoMaster.getRequestedNocdate());
							aJoMaster.setRequestedNocdate_Str(formatter.format(aJoMaster.getRequestedNocdate()));
						}
						if (aJoMaster.getReceivedNocdate() != null) {
							itsLogger.info("REceived NOC Date in DB::"+aJoMaster.getReceivedNocdate());
							aJoMaster.setReceivedNocdate_Str(formatter.format(aJoMaster.getReceivedNocdate()));
						}
						if (aJoMaster.getSentNtcdate() != null) {
							itsLogger.info("Sent Ntc Date in DB::"+aJoMaster.getSentNtcdate());
							aJoMaster.setSentNtcdate_Str(formatter.format(aJoMaster.getSentNtcdate()));
						}
						if (aJoMaster.getLienWaverSignedDate() != null) {
							aJoMaster.setLienWaverSignedDate_Str(formatter.format(aJoMaster.getLienWaverSignedDate()));
						}
						if (aJoMaster.getLienWaverThroughDate() != null) {
							aJoMaster.setLienWaverThroughDate_Str(formatter.format(aJoMaster.getLienWaverThroughDate()));
						}
						
						if (aJoMaster.getCreditStatusDate() != null) {
							aJoMaster.setCreditstadate_Str(formatter.format(aJoMaster.getCreditStatusDate()));
						}
						if (aJoMaster.getLocationAddress1() == null) {
							aJoMaster.setLocationAddress1("");
						}
						if (aJoMaster.getLocationAddress2() == null) {
							aJoMaster.setLocationAddress2("");
						}
						if (aJoMaster.getLocationCity() == null) {
							aJoMaster.setLocationCity("");
						}
						if (aJoMaster.getLocationState() == null) {
							aJoMaster.setLocationState("");
						}
						if (aJoMaster.getLocationZip() == null) {
							aJoMaster.setLocationZip("");
						}
						if (aJoMaster.getReleaseNotes() == null) {
							aJoMaster.setReleaseNotes("");
						}

					}
				} else {
					aJoMaster = new Jomaster();
					aJoMaster.setLocationAddress1("");
					aJoMaster.setLocationAddress2("");
					aJoMaster.setLocationCity("");
					aJoMaster.setLocationState("");
					aJoMaster.setLocationZip("");
				}

			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				if(aSession!=null){
				aSession.flush();
				aSession.close();
				}
				aJoMasterId = null;
				itsLogger.info("getSingleJobDetails()=[Connection Closed]");
			}
			
			return aJoMaster;
		}
		
		@Override
		public JoQuoteHeader getjoQuoteHeader(Integer aJoQuoteHeaderID)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
			try {
				//boolean update_beforedel=WhiledeleteUpdatePosition_template(ajoQuotetempHeaderID,aposition);
				
				aJoQuoteHeader = (JoQuoteHeader) aSession.get(
						JoQuoteHeader.class, aJoQuoteHeaderID);
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return aJoQuoteHeader;
		}
		
		public void updatecusovepovbandci(Integer joMasterID,Integer rxcustomerID){
			try{
				List<JoRelease> thejoReleaselst=getjoreleasebyjomasterid(joMasterID);
				if(thejoReleaselst!=null){
					Session aSession = itsSessionFactory.openSession();
					Transaction aTransaction = null;
					for(JoRelease thejoRelease:thejoReleaselst ){
						if(thejoRelease.getReleaseType()==1 || thejoRelease.getReleaseType()==4){
							//Vepo
							Integer vePOID=getVepoId(thejoRelease.getJoReleaseId());
							aTransaction =aSession.beginTransaction();
							aTransaction.begin();
							Vepo thevepo = (Vepo) aSession.get(Vepo.class,vePOID);
							thevepo.setRxBillToId(rxcustomerID);
							aSession.update(thevepo);
							aTransaction.commit();
							List<JobShippingBean> shippinglist=getShippingList("",thejoRelease.getJoReleaseId(),"");
							for(JobShippingBean ajobspngbn:shippinglist){
								
							}
							
						}else if(thejoRelease.getReleaseType()==2 || thejoRelease.getReleaseType()==5){
							//Cuso
							JobSalesOrderBean ajobsb=getSalesOrderDetails(thejoRelease.getJoReleaseId());
							Integer cuSOID=ajobsb.getCuSoid();
							aTransaction =aSession.beginTransaction();
							aTransaction.begin();
							Cuso theCuso = (Cuso) aSession.get(Cuso.class,cuSOID);
							theCuso.setRxCustomerId(rxcustomerID);
							theCuso.setRxBillToId(rxcustomerID);
							aSession.update(theCuso);
							aTransaction.commit();
							List<JobShippingBean> shippinglist=getShippingList("",thejoRelease.getJoReleaseId(),"");
							for(JobShippingBean ajobspngbn:shippinglist){
															
							}
						}
					}
				}
			}catch(Exception e){
				
			}
		}
		
		
		@Override
		public Integer addcusotodsorcommordfromInsideJob(Integer thevePoId,Cuso aCuSO)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			Integer cusoID;
			try {
				aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				cusoID  = (Integer) aSession.save(aCuSO);
				aTransaction.commit();
				if(thevePoId!=null && thevePoId>0){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepo theVepo = (Vepo) aSession.get(Vepo.class,thevePoId);
					aSession.delete(theVepo);;
					aTransaction.commit();
				}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return cusoID;
		}
		@Override
		public Boolean deleteVepo(Integer thevePoId)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			try {
				if(thevePoId!=null && thevePoId>0){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Vepo theVepo = (Vepo) aSession.get(Vepo.class,thevePoId);
					aSession.delete(theVepo);;
					aTransaction.commit();
				}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		@Override
		public Boolean deletecuso(Integer thecusoid)
				throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			try {
				if(thecusoid!=null && thecusoid>0){
					aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					Cuso theCuso = (Cuso) aSession.get(Cuso.class,thecusoid);
					aSession.delete(theCuso);;
					aTransaction.commit();
				}
			} catch (Exception excep) {
				itsLogger.error(excep.getMessage(), excep);
				JobException aJobException = new JobException(excep.getMessage(),
						excep);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		
		
		@Override
		public boolean UpdatenewQuotes_template(joQuoteTempDetailMstr thejoQuotetempDetailMstr,String Oper) throws JobException {
			Session aSession = itsSessionFactory.openSession();
			Date aDate = null;
			Integer aCustomer = null;
			try {
				if(Oper=="add"){
					Transaction aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					aSession.save(thejoQuotetempDetailMstr);
					aTransaction.commit();
				}else if(Oper=="edit"){
					Transaction aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					joQuoteTempDetailMstr ajoQuoteDetailMstr = (joQuoteTempDetailMstr) aSession.get(joQuoteTempDetailMstr.class,thejoQuotetempDetailMstr.getJoQuoteTempDetailMstrID());
					if(ajoQuoteDetailMstr!=null){
					if(thejoQuotetempDetailMstr.getType()==1){
						ajoQuoteDetailMstr.setType(thejoQuotetempDetailMstr.getType());
						ajoQuoteDetailMstr.setTexteditor(thejoQuotetempDetailMstr.getTexteditor());
					}else if(thejoQuotetempDetailMstr.getType()==2){
						ajoQuoteDetailMstr.setType(thejoQuotetempDetailMstr.getType());
						ajoQuoteDetailMstr.setQuantity(thejoQuotetempDetailMstr.getQuantity());
						ajoQuoteDetailMstr.setCost(thejoQuotetempDetailMstr.getCost());
						ajoQuoteDetailMstr.setManufacturer(thejoQuotetempDetailMstr.getManufacturer());
						ajoQuoteDetailMstr.setTexteditor(thejoQuotetempDetailMstr.getTexteditor());
						ajoQuoteDetailMstr.setCategory(thejoQuotetempDetailMstr.getCategory());
					}else if(thejoQuotetempDetailMstr.getType()==3){
						ajoQuoteDetailMstr.setType(thejoQuotetempDetailMstr.getType());
						ajoQuoteDetailMstr.setQuantity(thejoQuotetempDetailMstr.getQuantity());
						ajoQuoteDetailMstr.setCost(thejoQuotetempDetailMstr.getCost());
						ajoQuoteDetailMstr.setManufacturer(thejoQuotetempDetailMstr.getManufacturer());
						ajoQuoteDetailMstr.setTexteditor(thejoQuotetempDetailMstr.getTexteditor());
						ajoQuoteDetailMstr.setSellprice(thejoQuotetempDetailMstr.getSellprice());
						ajoQuoteDetailMstr.setCategory(thejoQuotetempDetailMstr.getCategory());
					}else if(thejoQuotetempDetailMstr.getType()==4){
						ajoQuoteDetailMstr.setType(thejoQuotetempDetailMstr.getType());
						ajoQuoteDetailMstr.setTextbox(thejoQuotetempDetailMstr.getTextbox());
						ajoQuoteDetailMstr.setSellprice(thejoQuotetempDetailMstr.getSellprice());
					}
					ajoQuoteDetailMstr.setJoQuoteTemplateHeaderID(ajoQuoteDetailMstr.getJoQuoteTemplateHeaderID());
					ajoQuoteDetailMstr.setPosition(thejoQuotetempDetailMstr.getPosition());
					ajoQuoteDetailMstr.setLinebreak(thejoQuotetempDetailMstr.isLinebreak());
					aSession.update(ajoQuoteDetailMstr);
					aTransaction.commit();
					}
				}else if(Oper=="del"){
					Transaction aTransaction = aSession.beginTransaction();
					aTransaction.begin();
					joQuoteTempDetailMstr ajoQuoteDetailMstr = (joQuoteTempDetailMstr) aSession.get(joQuoteTempDetailMstr.class, thejoQuotetempDetailMstr.getJoQuoteTempDetailMstrID());
					aSession.delete(ajoQuoteDetailMstr);
					aTransaction.commit();
				}
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				JobException aJobException = new JobException(e.getMessage(), e);
				throw aJobException;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return true;
		}
		public boolean gettemplatethereornot(String templateName){
			if(templateName==null){
				templateName="";
			}
			boolean returnvalue=false;
			String aSelectQry = " select * from joQuoteTemplateHeader where templateName like '"+ JobUtil.removeSpecialcharacterswithslash(templateName) + "' and neworoldquote=1";
			Session aSession = null;
			try {
				Cuso aCuso = new Cuso();
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aSelectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				if (aIterator.hasNext()) {
					returnvalue=true;
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				returnvalue=false;
			} finally {
				aSession.flush();
				aSession.close();
			}
			return returnvalue;
		}
		public Cuso updateTaxableandNonTaxableforCuSO(Cuso acuSO){
			Session aSession = null;
			String Query =null;
			List<?> aList = null;
			Cusodetail iteratorObj=null;
			Query aQuery;
			BigDecimal taxablesum;
			BigDecimal nontaxablesum;
			try {
				aSession = itsSessionFactory.openSession();
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Cuso thecuso = (Cuso) aSession.get(Cuso.class,acuSO.getCuSoid());
				Query = "FROM Cusodetail WHERE cuSoid= :cuSoid";
				aQuery = aSession.createQuery(Query);
				aQuery.setParameter("cuSoid", acuSO.getCuSoid());
				aList = aQuery.list();
				taxablesum=BigDecimal.ZERO;
				nontaxablesum=BigDecimal.ZERO;
				if (!aList.isEmpty()) {
					for (int i = 0; i < aList.size(); i++) {
					iteratorObj = (Cusodetail) aList.get(i);
					BigDecimal total = (iteratorObj.getQuantityOrdered()==null?new BigDecimal("0.0000"):iteratorObj.getQuantityOrdered())
							.multiply((iteratorObj.getPriceMultiplier()==null || (iteratorObj.getPriceMultiplier().compareTo(new BigDecimal("0.0000"))==0) ?new BigDecimal("1.0000"):iteratorObj.getPriceMultiplier())
									.multiply(iteratorObj.getUnitCost()==null?new BigDecimal("0.0000"):iteratorObj.getUnitCost()));
					
					taxablesum=taxablesum.add((iteratorObj.getTaxable()==1)?JobUtil.floorFigureoverall(total,2):BigDecimal.ZERO);
					nontaxablesum=nontaxablesum.add((iteratorObj.getTaxable()==0)?JobUtil.floorFigureoverall(total,2):BigDecimal.ZERO);
					}
				}
				if(thecuso.getTaxRate().compareTo(BigDecimal.ZERO)>0){
				if(thecuso.getTaxfreight()==1){
					taxablesum=taxablesum.add(thecuso.getFreight()!=null?thecuso.getFreight():BigDecimal.ZERO);
				}else{
					nontaxablesum=nontaxablesum.add(thecuso.getFreight()!=null?thecuso.getFreight():BigDecimal.ZERO);
				}
				}else{
					nontaxablesum=taxablesum.add(nontaxablesum).add(thecuso.getFreight()!=null?thecuso.getFreight():BigDecimal.ZERO);
					taxablesum=BigDecimal.ZERO;
					
				}
				thecuso.setTaxableSales(taxablesum);
				thecuso.setNonTaxableSales(nontaxablesum);
				aSession.update(thecuso);
				aTransaction.commit();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return null;
		}
		
		public Cuinvoice updateTaxableandNonTaxableforCuInvoice(Cuinvoice aCuinvoice){
			Session aSession = null;
			String Query =null;
			List<?> aList = null;
			Cuinvoicedetail iteratorObj=null;
			Query aQuery;
			BigDecimal taxablesum;
			BigDecimal nontaxablesum;
			try {
				aSession = itsSessionFactory.openSession();
				Transaction aTransaction = aSession.beginTransaction();
				aTransaction.begin();
				Cuinvoice thecuinvoice = (Cuinvoice) aSession.get(Cuinvoice.class,aCuinvoice.getCuInvoiceId());
				Query = "FROM Cuinvoicedetail WHERE cuInvoiceId= :cuInvoiceId";
				aQuery = aSession.createQuery(Query);
				aQuery.setParameter("cuInvoiceId", aCuinvoice.getCuInvoiceId());
				aList = aQuery.list();
				taxablesum=BigDecimal.ZERO;
				nontaxablesum=BigDecimal.ZERO;
				if (!aList.isEmpty()) {
					for (int i = 0; i < aList.size(); i++) {
					iteratorObj = (Cuinvoicedetail) aList.get(i);
					BigDecimal total = (iteratorObj.getQuantityBilled()==null?new BigDecimal("0.0000"):iteratorObj.getQuantityBilled())
							.multiply((iteratorObj.getPriceMultiplier()==null || (iteratorObj.getPriceMultiplier().compareTo(new BigDecimal("0.0000"))==0) ?new BigDecimal("1.0000"):iteratorObj.getPriceMultiplier())
									.multiply(iteratorObj.getUnitCost()==null?new BigDecimal("0.0000"):iteratorObj.getUnitCost()));
					
					taxablesum=taxablesum.add((iteratorObj.getTaxable()==1)?JobUtil.floorFigureoverall(total,2):BigDecimal.ZERO);
					nontaxablesum=nontaxablesum.add((iteratorObj.getTaxable()==0)?JobUtil.floorFigureoverall(total,2):BigDecimal.ZERO);
					}
				}
				if(thecuinvoice.getTaxRate().compareTo(BigDecimal.ZERO)>0){
				if(thecuinvoice.getTaxfreight()==1){
					taxablesum=taxablesum.add(thecuinvoice.getFreight()!=null?thecuinvoice.getFreight():BigDecimal.ZERO);
				}else{
					nontaxablesum=nontaxablesum.add(thecuinvoice.getFreight()!=null?thecuinvoice.getFreight():BigDecimal.ZERO);
				}
				}else{
					nontaxablesum=taxablesum.add(nontaxablesum).add(thecuinvoice.getFreight()!=null?thecuinvoice.getFreight():BigDecimal.ZERO);
					taxablesum=BigDecimal.ZERO;
					
				}
				thecuinvoice.setTaxableSales(taxablesum);
				thecuinvoice.setNonTaxableSales(nontaxablesum);
				aSession.update(thecuinvoice);
				aTransaction.commit();
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
			}
			return null;
		}
		
		@Override
		public Integer copySoTemplate(Integer cuSoId) {
			// TODO Auto-generated method stub
			
			Session aSession = null;
			Session bSession = null;
			Integer returnid = null;
			aSession = itsSessionFactory.openSession();
			//bSession = itsSessionFactory.openSession();
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			try {
				System.out.println("cuSoTemplateId==========>"+cuSoId);
				Cusotemplate cuSotemp = (Cusotemplate) aSession.get(Cusotemplate.class,
						cuSoId);		
				Cusotemplate temp=new Cusotemplate();
				//temp.setCuSoid(null);
				temp.setChangedById(cuSotemp.getChangedById());
				temp.setChangedOn(cuSotemp.getChangedOn());
				temp.setCoDivisionId(cuSotemp.getCoDivisionId());
				temp.setCostTotal(cuSotemp.getCostTotal());
				temp.setCoTaxTerritoryId(cuSotemp.getCoTaxTerritoryId());
				temp.setCreatedById(cuSotemp.getCreatedById());
				temp.setCreatedOn(	cuSotemp.getCreatedOn());	
				temp.setCuAssignmentId0(cuSotemp.getCuAssignmentId0());
				temp.setCuAssignmentId1(cuSotemp.getCuAssignmentId1());
				temp.setCuAssignmentId2(cuSotemp.getCuAssignmentId2());
				temp.setCuAssignmentId3(cuSotemp.getCuAssignmentId3());
				temp.setCuAssignmentId4(cuSotemp.getCuAssignmentId4());
			    temp.setCustomerPonumber(  cuSotemp.getCustomerPonumber());
			    temp.setCuTermsId( cuSotemp.getCuTermsId());
			    temp.setDatePromised( cuSotemp.getDatePromised());
			    temp.setFreight(  cuSotemp.getFreight());
			    temp.setFreightCost(   cuSotemp.getFreightCost());
			    temp.setJoReleaseId( cuSotemp.getJoReleaseId());
			    temp.setJoSchedDetailId(   cuSotemp.getJoSchedDetailId());
			    temp.setOrderDate( cuSotemp.getOrderDate());
			    temp.setPrFromWarehouseId( cuSotemp.getPrFromWarehouseId());
			    temp.setPrToWarehouseId(cuSotemp.getPrToWarehouseId());
			    temp.setQuickJobName( cuSotemp.getQuickJobName());
			    temp.setRxBillToAddressId(cuSotemp.getRxBillToAddressId());
			    temp.setRxBillToId( cuSotemp.getRxBillToId());
			    temp.setRxCustomerId(cuSotemp.getRxCustomerId());
			    temp.setRxShipToAddressId( cuSotemp.getRxShipToAddressId());
			    temp.setRxShipToId( cuSotemp.getRxShipToId());
			    temp.setShipDate( cuSotemp.getShipDate());
			    temp.setShipToMode(  cuSotemp.getShipToMode());
			    temp.setSingleItemTaxAmount( cuSotemp.getSingleItemTaxAmount());
			    temp.setSonumber( cuSotemp.getSonumber());
			    temp.setSubTotal(cuSotemp.getSubTotal());
			    temp.setSurtaxAmount(  cuSotemp.getSurtaxAmount());
			    temp.setSurtaxOverrideCap(cuSotemp.getSurtaxOverrideCap());
			    temp.setSurtaxRate(cuSotemp.getSurtaxRate());
			    temp.setSurtaxTotal( cuSotemp.getSurtaxTotal());
			    temp.setTag( cuSotemp.getTag());
			    temp.setTaxExempt0(cuSotemp.getTaxExempt0());
			    temp.setTaxExempt1(cuSotemp.getTaxExempt1());
			    temp.setTaxExempt2(  cuSotemp.getTaxExempt2());
			    temp.setTaxExempt3(cuSotemp.getTaxExempt3());
			    temp.setTaxExempt4(  cuSotemp.getTaxExempt4());
			    temp.setTaxExempt5( cuSotemp.getTaxExempt5());
			    temp.setTaxExempt6(cuSotemp.getTaxExempt6());
			    temp.setTaxExempt7( cuSotemp.getTaxExempt7());
			    temp.setTaxExempt8(cuSotemp.getTaxExempt8());
			    temp.setTaxRate(cuSotemp.getTaxRate());
			    temp.setTaxTotal(cuSotemp.getTaxTotal());
			    temp.setTemplateDescription(cuSotemp.getTemplateDescription());
			    temp.setTrackingNumber(cuSotemp.getTrackingNumber());
			    temp.setTransactionStatus(cuSotemp.getTransactionStatus());
			    temp.setVeShipViaId(cuSotemp.getVeShipViaId());
			    
				
               //cuSotempDetail.setNote(note);
				//aSession.update(cuSotempDetail);
				
				returnid=(Integer) aSession.save(temp);
				System.out.println("returnid==="+returnid);
				String subtablequery="INSERT INTO `cuSODetailTemplate` (cuSOID,prMasterID,Description,Note,QuantityOrdered ,QuantityReceived, UnitCost,UnitPrice, PriceMultiplier, Taxable,HasSingleItemTaxAmount,joSchedDetailID)"
+"(SELECT "+returnid+" as cuSOID,prMasterID,Description,Note,QuantityOrdered ,QuantityReceived, UnitCost,UnitPrice, PriceMultiplier, "
+"Taxable,HasSingleItemTaxAmount,joSchedDetailID FROM `cuSODetailTemplate` WHERE `cuSOID`="+ cuSoId+")";
				aSession.createSQLQuery(subtablequery).executeUpdate();
				
				aTransaction.commit();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				

			} finally {
				aSession.flush();
				aSession.close();
			//	bSession.flush();
			//	bSession.close();
			}
			return returnid;
			
			
			
		
		}
}
	