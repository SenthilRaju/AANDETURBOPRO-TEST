package com.turborep.turbotracker.job.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.company.service.CompanyService;
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
import com.turborep.turbotracker.customer.dao.RxCustomerTabViewBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Ecstatement;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.EmployeeAssign;
import com.turborep.turbotracker.job.dao.JoCustPO;
import com.turborep.turbotracker.job.dao.JoInvoiceCost;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.dao.JobReleaseBean;
import com.turborep.turbotracker.job.dao.Jochange;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.joQuoteDetailMstr;
import com.turborep.turbotracker.job.dao.joQuoteDetailPosition;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vecommdetail;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.PurchaseService;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
@RequestMapping("/jobtabs5")
public class JobReleaseFormController {
	
	Logger itsLogger = Logger.getLogger(JobReleaseFormController.class);
	
	private String itsJobNumber;
	private String itsCutomerRxName;
	private Cumaster itsCustomerRecord;
	
	private String itsTableNameUserLogin="tsUserLogin";
	private String itsTableNameRxMaster="rxMaster";
	
	private Jomaster itsJoMasterDetails;
	private String itsCustPONo;
	private List<Veshipvia> itsShipVia;
	private List<Prwarehouse> itsWareHouse;
	private List<Codivision> itsDivision;
	private List<CoTaxTerritory> itsJobSiteTaxValue;
	private List<Coaccount> itsCoAccount;
	private List<JoCustPO> itsPODetails;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	@Resource(name="salesServices")
	private SalesService itsEmpAssignedService;
	
	@Resource(name = "customerService")
	private CustomerService itsCuMasterService;
	
	@Resource(name="salesServices")
	private SalesService itsSalesServices;
	
	@Resource(name="vendorService")
	private VendorServiceInterface itsVendorService;
	
	@Resource(name="gltransactionService")
	private GltransactionService itsGltransactionService;
	
	@Resource(name = "userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;
	
	@Resource(name = "rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;
	
	
	@RequestMapping(value = "jobwizard_release",method = RequestMethod.GET)
	public String getJobsReleasePage(@RequestParam(value="jobNumber", required=false) String theJobNumber, 
									@RequestParam(value="jobName", required=false) String theJobName,
									@RequestParam(value="jobCustomer", required=false) String theJobCustomer,
									@RequestParam(value="joMasterID", required=false) Integer joMasterID,
									HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws JobException, IOException, BankingException, MessagingException {
		/*JobCustomerBean aJobCustomerBean = (JobCustomerBean) session.getAttribute(SessionConstants.JOB_GRID_OBJ);
		logger.info("Main:" + aJobCustomerBean.getDescription());
		*/
		SysAccountLinkage aSysAccountLinkage=null;
		String aAssignedSalesRep = null;
		String aAssignedCSR = null;
		String aAssignedSalesMGR = null;
		String aAssignedEngineers = null;
		String aAssignedProjMgr = null;
		setJobNumber(theJobNumber);
		setJomasterDetails(theJobNumber,joMasterID);
		Jomaster aJomaster = getJomasterDetails();
		itsLogger.info("Customer PO Number: "+aJomaster.getCustomerPonumber());
		try {
			aSysAccountLinkage=itsGltransactionService.getsysAccountLinkageDetail();
			if(aJomaster.getRxCustomerId() != null){
				Integer aRxMasterId = aJomaster.getRxCustomerId();
				setCustomerRecord(aRxMasterId);
				if(itsCustomerRecord != null){
					RxCustomerTabViewBean aRxCustomerTabViewBean = new RxCustomerTabViewBean();
					if(itsCustomerRecord.getCuAssignmentId0() != null){
						aAssignedSalesRep = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId0(), itsTableNameUserLogin);
						aRxCustomerTabViewBean.setAssignedSalesRep(aAssignedSalesRep);
					}
					if(itsCustomerRecord.getCuAssignmentId1() != null){
						aAssignedCSR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId1(), itsTableNameUserLogin);
						aRxCustomerTabViewBean.setAssignedCSRs(aAssignedCSR);
					}
					if(itsCustomerRecord.getCuAssignmentId2() != null){
						aAssignedSalesMGR = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId2(), itsTableNameUserLogin);
						aRxCustomerTabViewBean.setAssignedSalesMGRs(aAssignedSalesMGR);
					}
					if(itsCustomerRecord.getCuAssignmentId3() != null){
						aAssignedEngineers = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId3(), itsTableNameUserLogin);
						aRxCustomerTabViewBean.setAssignedEngineers(aAssignedEngineers);
					}
					if(itsCustomerRecord.getCuAssignmentId4() != null){
						aAssignedProjMgr = (String)itsEmpAssignedService.getAssignedEmployeeName(itsCustomerRecord.getCuAssignmentId4(), itsTableNameUserLogin);
						aRxCustomerTabViewBean.setAssignedProjManagers(aAssignedProjMgr);
					}
					if (itsCustomerRecord.getCuTermsId() != null) {
						CuTerms aCuterms = itsCuMasterService.getCuTerms(itsCustomerRecord.getCuTermsId());
						if(aCuterms!=null){
						aRxCustomerTabViewBean.setCustomerTerms(aCuterms.getDescription());
						}
						else{
						aCuterms= new CuTerms();
						}
					}
					if (itsCustomerRecord.getCuMasterTypeId() != null) {
						CuMasterType aCumastertype = itsCuMasterService.getCustomerType(itsCustomerRecord.getCuMasterTypeId());
						aRxCustomerTabViewBean.setCustomerType(aCumastertype.getCode());
					}
				setJobSiteTaxValue((List<CoTaxTerritory>)itsCompanyService.getCompanyTaxTerritory(aJomaster.getCoTaxTerritoryId(), itsTableNameRxMaster));
				theModel.addAttribute("customerMasterObj", getCustomerRecord());
				theModel.addAttribute("customerTabFormDataBean", aRxCustomerTabViewBean);
				}
			}
			if (aJomaster.getRxCustomerId() != null) {
				setCutomerRxName((String)itsCompanyService.getCustomerName(aJomaster.getRxCustomerId()));
			}
			theModel.addAttribute("joMasterDetails", aJomaster);
			if(aJomaster.getDescription() != null){
				String aString = aJomaster.getDescription();
				String string = aString.replaceAll("\"", "");
				theModel.addAttribute("joMasterDescription", string);
			}
			if (aJomaster.getRxCustomerId() != null) {
				theModel.addAttribute("CustomerName", getCutomerRxName());
			}
			BigDecimal estimatedAmount =aJomaster.getContractAmount();
			BigDecimal changeAmount = itsJobService.getJobChangeAmount(theJobNumber);
			if(changeAmount != null && estimatedAmount != null) {
				estimatedAmount = changeAmount.add(estimatedAmount);
			}
			theModel.addAttribute("estimatedAmount",estimatedAmount);
			setItsShipVia((List<Veshipvia>)itsJobService.getVeShipVia());
			theModel.addAttribute("veShipViaDetails",getItsShipVia());
			setItsWareHouse((List<Prwarehouse>)itsJobService.getWareHouse());
			theModel.addAttribute("prWareHouseDetails",getItsWareHouse());
			
			System.out.println("JobReleaseFormController.getJobsReleasePage()::Tracing");
			setItsCoAccount((List<Coaccount>)itsJobService.getCoAccountDetails());
			
			//theModel.addAttribute("coAccountDetails",getItsCoAccount());
			theModel.addAttribute("coAccountDetails",(List<Coaccount>)chartOfAccountsService.getAccountNumber());
			if(itsJobSiteTaxValue != null && aJomaster != null && !itsJobSiteTaxValue.isEmpty())
			{
				CoTaxTerritory aTaxterritory = getJobSiteTaxValue().get(0);
				theModel.addAttribute("taxTerritory", aTaxterritory.getCounty());
				theModel.addAttribute("taxValue", aTaxterritory.getTaxRate());
				System.out.println("taxValue"+ aTaxterritory.getTaxRate());
			} else {
				theModel.addAttribute("taxTerritory", "");
				theModel.addAttribute("taxValue", "0.00");
			}
			setItsDivision((List<Codivision>)itsCompanyService.getCompanyDivisions());
			theModel.addAttribute("divisions", getItsDivision());
			setItsPODetails((List<JoCustPO>) itsJobService.getJoCustPODetail(aJomaster.getJoMasterId()));
			theModel.addAttribute("custPONoDetails",getItsPODetails());
			
			
			if(aSysAccountLinkage!=null){
				theModel.addAttribute("SysAccountLinkage", aSysAccountLinkage);
			}
			
			theModel.addAttribute("wareHouse", (List<Prwarehouse>) itsJobService.getWareHouse());
			if (theJobNumber != "") {
				setJomasterDetails(theJobNumber,joMasterID);
				Jomaster theJomasterdet = getJomasterDetails();
				theModel.addAttribute("joMasterDetails", theJomasterdet);
			}
			theModel.addAttribute("theNewShiptocusaddress", itsCompanyService.getAllAddress(aJomaster.getRxCustomerId()));
		} catch (CompanyException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return "job/jobwizard_release";
	}
	
	@RequestMapping(value = "/saveCommissionAmount",method = RequestMethod.POST)
	public @ResponseBody JoRelease saveCommissionAmount(@RequestParam(value="releaseId", required=false) Integer theReleaseId,
			@RequestParam(value="expectedCommission", required=false) BigDecimal theExpectedCommission,
			HttpSession theSession, ModelMap theModel, HttpServletResponse theResponse) throws JobException, IOException, BankingException {
		BigDecimal commissionAmountVal = new BigDecimal("0.0000");
		JoRelease aJoRelease = null;
		itsLogger.info("inside saveCommissionAmount >>> theReleaseId = "+theReleaseId+" || expectedCommission = "+theExpectedCommission);		
		commissionAmountVal = itsJobService.saveCommissionAmount(theReleaseId, theExpectedCommission);
		aJoRelease=itsJobService.getCommissionAmount(theReleaseId);
		return aJoRelease;
	}
	
	@RequestMapping(value = "/getCommissionAmount",method = RequestMethod.POST)
	public @ResponseBody JoRelease getCommissionAmount(@RequestParam(value="releaseId", required=false) Integer theReleaseId,
			HttpSession theSession, ModelMap theModel, HttpServletResponse theResponse) throws JobException, IOException, BankingException {
		JoRelease aJoRelease = null;
		itsLogger.info("inside saveCommissionAmount >>> theReleaseId = "+theReleaseId);		
		aJoRelease=itsJobService.getCommissionAmount(theReleaseId);
		return aJoRelease;
	}
	
	@RequestMapping(value = "/saveCommissionInvoice",method = RequestMethod.POST)
	public @ResponseBody Integer saveCommissionInvoice(@RequestParam(value="commissionShipDate", required=false) String commissionShipDate,
													@RequestParam(value="commissionShipVia", required=false) Integer commissionShipVia,
													@RequestParam(value="commissionTrackingPro", required=false) String commissionTrackingPro,
													@RequestParam(value="commissionVendorInvoiceNo", required=false) String commissionVendorInvoiceNo,
													@RequestParam(value="commissionInvoiceAmt", required=false) String commissionInvoiceAmt,
													@RequestParam(value="commissionDateVal", required=false) String commissionDateVal,
													@RequestParam(value="commissionAmountVal", required=false) String commissionAmountVal,
													@RequestParam(value="joMasterID", required=false) Integer joMasterID,
													@RequestParam(value="joReleaseID", required=false) Integer joReleaseID,
													@RequestParam(value="veCommdetailIDName", required=false) Integer veCommdetailIDName,
													HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws JobException, IOException, BankingException, MessagingException {
		itsLogger.info("inside saveCommissionInvoice >>> commissionShipDate ::"+commissionShipDate+"\n commissionShipVia ::"+
				commissionShipVia+"\n commissionTrackingPro ::"+commissionTrackingPro+"\ncommissionVendorInvoiceNo::"
				+commissionVendorInvoiceNo+"\ncommissionInvoiceAmt ::"+commissionInvoiceAmt+"\ncommissionDateVal ::"
				+commissionDateVal+"\ncommissionAmountVal::"+commissionAmountVal+"\nVeCommdetailID::"+veCommdetailIDName);
		Vecommdetail aVecommdetail = new Vecommdetail();
		Integer joReleaseDetailID=null;
		Date commShipDate=null,commDate = null;
		String shipDate=null ,comDate = null;
		try {
			commShipDate = new SimpleDateFormat("MM/dd/yyyy").parse(commissionShipDate);
			shipDate = new SimpleDateFormat("yyyy-MM-dd").format(commShipDate);
			
			commDate = new SimpleDateFormat("MM/dd/yyyy").parse(commissionDateVal);
			comDate = new SimpleDateFormat("yyyy-MM-dd").format(commDate);
		}catch(Exception e){
			sendTransactionException("<b>commissionVendorInvoiceNo:</b>"+commissionVendorInvoiceNo,"JOB",e,session,therequest);
		}
		if(commissionInvoiceAmt!=null && !commissionInvoiceAmt.trim().equals("")){
			if(commissionInvoiceAmt.contains("$")){
				commissionInvoiceAmt = commissionInvoiceAmt.replace("$", "");
			}
			if(commissionInvoiceAmt.contains(".00")){
				commissionInvoiceAmt = commissionInvoiceAmt.replace(".00", "");
			}
			itsLogger.info("commissionInvoiceAmt::"+commissionInvoiceAmt);
			aVecommdetail.setBillAmount(new BigDecimal(commissionInvoiceAmt));
		}
		if(commissionAmountVal!=null && !commissionAmountVal.trim().equals("")){
			if(commissionAmountVal.contains("$")){
				commissionAmountVal = commissionAmountVal.replace("$", "");
			}
			if(commissionAmountVal.contains(".00")){
				commissionAmountVal = commissionAmountVal.replace(".00", "");
			}
			itsLogger.info("commissionAmountVal::"+commissionAmountVal);
			aVecommdetail.setCommAmount(new BigDecimal(commissionAmountVal));
		}
		
		aVecommdetail.setCommDate(commDate);
		aVecommdetail.setInvoiceNumber(commissionVendorInvoiceNo);
		aVecommdetail.setShipDate(commShipDate);
		aVecommdetail.setTrackingNumber(commissionTrackingPro);
		aVecommdetail.setVeShipViaId(commissionShipVia);
		aVecommdetail.setVeCommDetailId(veCommdetailIDName);
		joReleaseDetailID = itsJobService.saveCommissionInvoice(aVecommdetail,joMasterID, joReleaseID);
		return joReleaseDetailID;
	}
	
	
	@RequestMapping(value = "/getestimatedamount",method = RequestMethod.POST)
	public @ResponseBody BigDecimal getestimatedamount(@RequestParam(value="jobNumber", required=false) String theJobNumber,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession theSession, ModelMap theModel, HttpServletResponse theResponse) throws JobException, IOException, BankingException {
		System.out.println("inside getestimatedamount");
		setJobNumber(theJobNumber);
		setJomasterDetails(itsJobNumber,joMasterID);
		Jomaster aJomaster = getJomasterDetails();
		BigDecimal estimatedAmount =aJomaster.getContractAmount()==null?new BigDecimal("0.00"):aJomaster.getContractAmount();
		BigDecimal changeAmount = itsJobService.getJobChangeAmount(theJobNumber)==null?new BigDecimal("0.00"):itsJobService.getJobChangeAmount(theJobNumber);
		if(changeAmount != null && estimatedAmount != null) {
			estimatedAmount = changeAmount.add(estimatedAmount);
		}
		
		return estimatedAmount;
	}
	
	
	public String getJobNumber() {
		return itsJobNumber;
	}
	
	public void setJobNumber(String jobNumber) {
		this.itsJobNumber = jobNumber;
	}
	
	public Jomaster getJomasterDetails() {
			return itsJoMasterDetails;
	}
	
	
	public void setJomasterDetails(String theJobNumber,Integer joMasterID) throws JobException {
		this.itsJoMasterDetails = itsJobService.getSingleJobDetails(theJobNumber,joMasterID);
	}
	
	public String getCustPONo() {
		return itsCustPONo;
	}

	public void setCustPONo(String theCustPONo) {
		this.itsCustPONo = theCustPONo;
	}

	@RequestMapping(value = "release",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getReleaseList(@RequestParam(value="jobNumber", required=false) String theJobNumber,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
														HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse aResponse = null;
		List<?> aBids =null;
		try{
			aBids = itsJobService.getReleaseList(theJobNumber,joMasterID);
			aResponse = new CustomResponse();
			aResponse.setRows(aBids);
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}finally{
			aBids=null;
		}
		return aResponse;
	}
	
	@RequestMapping(value = "releaseCount",method = RequestMethod.GET)
	public @ResponseBody Integer getReleaseCount(@RequestParam(value="jobNumber", required=false) String theJobNumber,
														HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		Integer aInteger = null;
		try{
			List<?> aBids = itsJobService.getReleaseList(theJobNumber,0);
			aInteger = aBids.size();
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aInteger;
	}
	
	@RequestMapping(value = "shipping" , method = RequestMethod.GET)
	public @ResponseBody CustomResponse getShipingList(@RequestParam(value="jobNumber", required=false) String theJobNumber,
																				@RequestParam(value="joDetailsID", required=false) Integer theJoDetailsID, 
																				@RequestParam(value="releaseType", required=false) String releaseType,
																				HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse aResponse = null;
		try{
			List<?> aBids = null;
			aBids = itsJobService.getShippingList(theJobNumber, theJoDetailsID, releaseType);	
			aResponse = new CustomResponse();
			aResponse.setRows(aBids);
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber,theJoDetailsID:</b>"+theJobNumber+","+theJoDetailsID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}
	
	@RequestMapping(value = "commissionInvoiceList" , method = RequestMethod.GET)
	public @ResponseBody CustomResponse commissionInvoiceList(@RequestParam(value="jobNumber", required=false) String theJobNumber,
																				@RequestParam(value="joDetailsID", required=false) Integer theJoDetailsID, 
																				@RequestParam(value="releaseType", required=false) String releaseType,
																				HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse aResponse = null;
		try{
			List<?> aBids = null;		
			aBids = itsJobService.getCommissionInvoiceList(theJobNumber, theJoDetailsID, releaseType);	
			aResponse = new CustomResponse();
			aResponse.setRows(aBids);
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber,theJoDetailsID:</b>"+theJobNumber+","+theJoDetailsID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}
	
		 
		@RequestMapping(value = "getVeCommissionDetails" , method = RequestMethod.GET)
		public @ResponseBody Vecommdetail getVeCommissionDetails(@RequestParam(value="veCommissionDetailID", required=false) Integer veCommissionDetailID, 
															HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
			Vecommdetail aVecommdetail = null;
			try{
				aVecommdetail = itsJobService.getVeCommissionDetails(veCommissionDetailID);
			}catch (JobException e) {
				sendTransactionException("<b>veCommissionDetailID:</b>"+veCommissionDetailID,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aVecommdetail;
		}
	
	@RequestMapping(value = "/changeOrderList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getchangeOrderList(@RequestParam(value="jobNumber", required=false) String theJobNumber,
			@RequestParam(value="joMasterID", required=false) Integer joMasterId,
															HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse aResponse = null;
		try{
			List<?> aBids = itsJobService.getChangeorderList(joMasterId);
			aResponse = new CustomResponse();
			aResponse.setRows(aBids);
		}catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/invoiceCostList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse invoiceCostList(
			@RequestParam(value="joReleaseDetailID", required=false) Integer joReleaseDetailID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse aResponse = null;
		try{
			itsLogger.info("JoReleaseDetailID::"+joReleaseDetailID);
			List<JoInvoiceCost> invoiceCostList = itsJobService.getInvoiceCostList(joReleaseDetailID);

			aResponse = new CustomResponse();
			aResponse.setRows(invoiceCostList);
		}catch (JobException e) {
			sendTransactionException("<b>joReleaseDetailID:</b>"+joReleaseDetailID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "/manipulateInvoiceCostList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse ManipulateSOReleaseLineItem(
			@RequestParam(value = "joReleaseDetailID", required = false) Integer joReleaseDetailID,
			@RequestParam(value = "joInvoiceCostID", required = false) Integer joInvoiceCostID,
			@RequestParam(value = "cost", required = false) String cost,
			@RequestParam(value = "reason", required = false) String reason,
			@RequestParam(value = "oper", required = false) String Oper,
			@RequestParam(value = "enteredByName", required = false) String enteredByName,
			@RequestParam(value = "enteredByID", required = false) Integer enteredByID,
			@RequestParam(value = "enteredDate", required = false) Date enteredDate,
			@RequestParam(value = "veBillID", required = false) Integer veBillID,
			@RequestParam(value = "cuInvoiceID", required = false) Integer cuInvoiceID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		JoInvoiceCost aJoInvoiceCost = new JoInvoiceCost();
		boolean saved = false;
		itsLogger.info("joReleaseDetailID:"+joReleaseDetailID+"   "+cost+"  "+reason+"  "+enteredDate);
		if(cost !=null && cost.contains("$")){
			cost= cost.replace("$", "");
			cost= cost.replace(",", "");
		}
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (Oper.equalsIgnoreCase("del")) {
				aJoInvoiceCost.setJoInvoiceCostID(joInvoiceCostID);
				saved = itsJobService.deleteInvoiceCost(aJoInvoiceCost);
			} else {
				aJoInvoiceCost.setCost(new BigDecimal(cost));
				aJoInvoiceCost.setEnteredBy(aUserBean.getUserId());
				aJoInvoiceCost.setJoInvoiceCostID(joInvoiceCostID);
				aJoInvoiceCost.setJoReleaseDetailID(joReleaseDetailID);
				aJoInvoiceCost.setEnteredDate(enteredDate);
				aJoInvoiceCost.setReason(reason);
				aJoInvoiceCost.setVeBillID(veBillID);
				aJoInvoiceCost.setCuInvoiceID(cuInvoiceID);
				
				if (Oper.equalsIgnoreCase("add")) {
					saved = itsJobService.saveInvoiceCost(aJoInvoiceCost);
				} else if (Oper.equalsIgnoreCase("edit")) {
					saved = itsJobService.saveInvoiceCost(aJoInvoiceCost);
				}
			}
		} catch (JobException e) {
			sendTransactionException("<b>joReleaseDetailID:</b>"+joReleaseDetailID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());

		}

		return aResponse;
	}
	
	@RequestMapping(value = "/addInvoiceCost", method = RequestMethod.POST)
	public @ResponseBody
	boolean addInvoiceCost(
			@RequestParam(value = "joReleaseDetailID", required = false) Integer joReleaseDetailID,
			@RequestParam(value = "cuInvoiceID", required = false) Integer cuInvoiceID,
			@RequestParam(value = "veBillID", required = false) Integer veBillID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, JobException, MessagingException {
		boolean saved = false;
		itsLogger.info("joReleaseDetailID:"+joReleaseDetailID+"  :cuInvoiceID::"+cuInvoiceID+" :veBillID::"+veBillID);
		try {
			if(cuInvoiceID!=null || veBillID!=null){
			saved = itsJobService.updateCustomerInvoiceCost(joReleaseDetailID,cuInvoiceID,veBillID);
			}
		} catch (JobException e) {
			sendTransactionException("<b>joReleaseDetailID,cuInvoiceID:</b>"+joReleaseDetailID+","+cuInvoiceID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} 

		return saved;
	}
	
	
	@RequestMapping(value="/addRelease", method = RequestMethod.POST)
	public @ResponseBody Vepo insertJobRelease(@RequestParam(value="ReleasesName", required= false) Date theReleaseDate,
													   @RequestParam(value="joReleaseName", required= false) Integer theJoreleaseId,
													   @RequestParam(value="joReleaseDetailName", required= false) Integer theJoreleaseDetailId,
													   @RequestParam(value="vePoName", required= false) Integer thevePoId,
													   @RequestParam(value="cuSOID", required= false) Integer thecuSOID,
													   @RequestParam(value="ReleasesTypeName", required= false) Integer theReleaseType, 
													   @RequestParam(value="ManufacturerId", required = false) Integer theManuId,
													   @RequestParam(value="ReleasesManuName", required=false) String theManuName,
													   @RequestParam(value="NoteName", required=false) String theNote,
													   @RequestParam(value="AllocatedName", required= false) BigDecimal theAllocatedName, 
													   @RequestParam(value="POAmountName", required= false) BigDecimal thePOAmount, 
													   @RequestParam(value="ReleasesInvoiceName", required= false) BigDecimal theReleaseInvoiceName, 
													   @RequestParam(value="joMasterID", required= false) Integer theJoMasterID, 
													   @RequestParam(value="oper", required = false) String theOperation,
													   @RequestParam(value="custPONumber", required = false) String thecustPONumber,
													   @RequestParam(value="jobNumber", required = false) String theJobNumber,
													   @RequestParam(value="rxMasterID", required = false) Integer theRxMasterID,
													   @RequestParam(value="veFactoryId", required = false) Integer theFActoryID,
													   @RequestParam(value="customerPONumberName", required = false) String theCustomerPONumber,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, VendorException, JobException, MessagingException, UserException{

		Jomaster theJomaster = null;
		itsLogger.info("ReleasesTypeName :"+theReleaseType);
		itsLogger.info("POAmountName :"+thePOAmount+" Manufacturer ID:"+theManuId);
		if(theJobNumber != null){
			theJobNumber = theJobNumber.trim();
			theJomaster = itsJobService.getSingleJobDetails(theJobNumber,theJoMasterID);
		}
		JoRelease aJoRelease = new JoRelease();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		Vepo aVepo = new Vepo();
		Vepo theVepo = new Vepo();
		Cuso aCuSO = new Cuso();
		String CustPoNo = "";
		
		try{
			
			/**
			 * Dev:Leo Date:04/02/2015
			 * Bug ID :273
			 * Description: Allow special character
			 * */
			
			itsLogger.info("theCustomerPONumber==["+theCustomerPONumber+"]");
			if(theCustomerPONumber!=null && !theCustomerPONumber.equals("-1")){
				int startIndex = theCustomerPONumber.indexOf('(');
				theCustomerPONumber = theCustomerPONumber.substring(0, startIndex);
				System.out.println(theCustomerPONumber);
				CustPoNo = getCustomerPOnumber(theCustomerPONumber,theJoMasterID);
			}
			else
			{
				theCustomerPONumber = null;
			}
			aJoRelease.setReleaseDate(theReleaseDate);
			System.out.println("theReleaseType=="+theReleaseType);
			if(theReleaseType == null){
				theReleaseType = 1;
			}else if(theReleaseType==2){
				theReleaseType=2;
			}else if(theReleaseType==5){
				theReleaseType=5;
			}else if(theReleaseType==3){
				theReleaseType=3;
			}else if(theReleaseType==4){
				theReleaseType=4;
			}
			else if(theReleaseType != -1){
				theReleaseType =1;
			}
			System.out.println("theReleaseTypevalue=="+theReleaseType);
			aJoRelease.setReleaseType(theReleaseType);
			aJoRelease.setEstimatedBilling(theAllocatedName);
			aJoRelease.setJoMasterId(theJoMasterID);
			aJoRelease.setReleaseNote(theNote);
			aJoRelease.setPoid(theCustomerPONumber);
			aJoReleaseDetail.setVendorInvoiceAmount(thePOAmount);
			aJoReleaseDetail.setCustomerInvoiceAmount(theReleaseInvoiceName);
			aJoReleaseDetail.setJoMasterId(theJoMasterID);
			aVepo.setRxVendorId(theManuId);
			aVepo.setRxBillToId(theRxMasterID);
			aVepo.setRxBillToAddressId(theRxMasterID);
			
		//	aVepo.setRxShipToId(theRxMasterID);
		//	aVepo.setRxShipToAddressId(theRxMasterID);
			
			aVepo.setCustomerPonumber(CustPoNo);
			aVepo.setConsignment(false);
			aVepo.setMultipleAcks(false);
			if(theOperation.equals("add")){
				
			switch (theReleaseType) {
			case 1:
				
				aVepo.setBillToIndex(0);
				aVepo.setShipTo(0); //shiptoindex
				byte aShipToMode = 2;
				//aVepo.setShipToMode(aShipToMode); // shiptomode
				aVepo.setCreatedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				aVepo.setCreatedOn(new Date());
				
				
				setJomasterDetails(theJobNumber,theJoMasterID);
				//String aNewPONumber =itsJobService.getNewPONumber(theJobNumber, thevePoId);
				//aVepo.setPonumber(aNewPONumber);
				aVepo.setCustomerPonumber(CustPoNo);
				 if(theCustomerPONumber!=null){
					 aVepo.setCustomerPonumber(theCustomerPONumber.trim());	 
				 }
				aVepo.setTransactionStatus(1);
				aVepo.setOrderedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				
				if(theManuName.equals("Submittal")){
					Integer aVeFactoryId=itsJobService.getVeFactoryId(theManuId);
					aVepo.setVeFactoryId(aVeFactoryId);
					theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,theReleaseType);
				}else{
					theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,theReleaseType);
				}
				break;
			case 2:
				 //String thePONumber =itsJobService.getNewPONumber(theJobNumber, thevePoId);
				 SysUserDefault asysusrdeflt=itsEmpAssignedService.getSysUserDefault(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				 //aCuSO.setSonumber(thePONumber);
				 if(theJomaster.getCoTaxTerritoryId()>0){
					 CoTaxTerritory thecoTaxTerritory=itsJobService.getCoTaxTerritoryDetails(theJomaster.getCoTaxTerritoryId());
					 if(thecoTaxTerritory!=null){
					 aCuSO.setTaxfreight(thecoTaxTerritory.getTaxfreight());
					 }
				 }else{
					 aCuSO.setTaxfreight((byte)0);
				 }
				 
				 aCuSO.setCustomerPonumber(CustPoNo);
				 aCuSO.setRxCustomerId(theRxMasterID);
				 
				 /* List<String> addlist=new ArrayList<String>();
				 addlist.add("OverrideReleaseTaxTerritory");
				 ArrayList<Sysvariable> sysvariablelist= itsUserService.getInventorySettingsDetails(addlist);
				 if(sysvariablelist.get(0)!=null &&sysvariablelist.get(0).getValueLong()==1){
					 Integer coTaxTerritoryId=getCustomerTaxTerritoryIDWithShipTo(theRxMasterID);
					 if(coTaxTerritoryId!=null)
						 aCuSO.setCoTaxTerritoryId(coTaxTerritoryId);
				 }*/
			//	 aCuSO.setRxShipToId(itsJobService.getShipToAddressID(theRxMasterID));
				 
				 
				 aCuSO.setRxBillToId(theRxMasterID);
		
				 aCuSO.setCuTermsId(1);
				 aCuSO.setTransactionStatus(1);
				 aCuSO.setPrFromWarehouseId(asysusrdeflt.getWarehouseID());
				 aCuSO.setCoDivisionID(theJomaster.getCoDivisionId());
				 Date createdDate = new Date();
				 if(theJomaster!=null && theJomaster.getDescription()!=null){
					 aCuSO.setTag(theJomaster.getDescription());
				 }
				
				 if(theCustomerPONumber!=null){
					 aCuSO.setCustomerPonumber(theCustomerPONumber.trim());	 
				 }
				 aShipToMode = 2;
				 aCuSO.setShipToMode(aShipToMode); // shiptomode
				 aCuSO.setShipDate(createdDate);
				 aCuSO.setCuAssignmentId0(theJomaster.getCuAssignmentId0());
				 aCuSO.setCuAssignmentId1(theJomaster.getCuAssignmentId1());
				 aCuSO.setCuAssignmentId2(theJomaster.getCuAssignmentId2());
				 aCuSO.setCuAssignmentId3(theJomaster.getCuAssignmentId3());
				 aCuSO.setCuAssignmentId4(theJomaster.getCuAssignmentId4());
				 
				 aCuSO.setCreatedOn(createdDate);
				 aCuSO.setCreatedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				 
				 theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,theReleaseType);
				break;
			case 3:
				 aVepo.setCreatedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				 aVepo.setCreatedOn(new Date());
				 theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,0);
				break;
			case 4:

				aVepo.setBillToIndex(0);
				aVepo.setShipTo(0); //shiptoindex
				aShipToMode = 2;
				aVepo.setShipToMode(aShipToMode); // shiptomode
				setJomasterDetails(theJobNumber,theJoMasterID);
				//String aNewPONumber =itsJobService.getNewPONumber(theJobNumber, thevePoId);
				//aVepo.setPonumber(aNewPONumber);
				aVepo.setCustomerPonumber(CustPoNo);
				 if(thecustPONumber!=null && !thecustPONumber.trim().equals("")){
					 aVepo.setCustomerPonumber(thecustPONumber.trim());	 
				 }
				aVepo.setTransactionStatus(1);
				aVepo.setOrderedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				aVepo.setCreatedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				aVepo.setCreatedOn(new Date());
				
				if(theManuName.equals("Submittal")){
					Integer aVeFactoryId=itsJobService.getVeFactoryId(theManuId);
					aVepo.setVeFactoryId(aVeFactoryId);
					theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,theReleaseType);
				}else{
					theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,theReleaseType);
				}
				break;	
			case 5:
				// String thePONumberservice =itsJobService.getNewPONumber(theJobNumber, thevePoId);
				 SysUserDefault asysusrdefltservice=itsEmpAssignedService.getSysUserDefault(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				 //aCuSO.setSonumber(thePONumberservice);
				 aCuSO.setCustomerPonumber(CustPoNo);
				 aCuSO.setRxCustomerId(theRxMasterID);
			//	 aCuSO.setRxShipToId(itsJobService.getShipToAddressID(theRxMasterID));
				 aCuSO.setRxBillToId(theRxMasterID);
				
				 aCuSO.setCuTermsId(1);
				 aCuSO.setTransactionStatus(1);
				 aCuSO.setPrFromWarehouseId(asysusrdefltservice.getWarehouseID());
				 aCuSO.setCoDivisionID(theJomaster.getCoDivisionId());
				 /*addlist=new ArrayList<String>();
				 addlist.add("OverrideReleaseTaxTerritory");
				 sysvariablelist= itsUserService.getInventorySettingsDetails(addlist);
				 if(sysvariablelist.get(0)!=null &&sysvariablelist.get(0).getValueLong()==1){
					 Integer coTaxTerritoryId=getCustomerTaxTerritoryIDWithShipTo(theRxMasterID);
					 if(coTaxTerritoryId!=null)
						 aCuSO.setCoTaxTerritoryId(coTaxTerritoryId);
				 }*/
				 createdDate = new Date();
				 if(theJomaster!=null && theJomaster.getDescription()!=null){
					 aCuSO.setTag(theJomaster.getDescription());
				 }
				
				 if(thecustPONumber!=null && !thecustPONumber.trim().equals("")){
					 aCuSO.setCustomerPonumber(thecustPONumber.trim());	 
				 }
				 aShipToMode = 2;
				 aCuSO.setShipToMode(aShipToMode); // shiptomode
				 aCuSO.setShipDate(createdDate);
				 aCuSO.setCuAssignmentId0(theJomaster.getCuAssignmentId0());
				 aCuSO.setCuAssignmentId1(theJomaster.getCuAssignmentId1());
				 aCuSO.setCuAssignmentId2(theJomaster.getCuAssignmentId2());
				 aCuSO.setCuAssignmentId3(theJomaster.getCuAssignmentId3());
				 aCuSO.setCuAssignmentId4(theJomaster.getCuAssignmentId4());
				 
				 aCuSO.setCreatedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
				 aCuSO.setCreatedOn(createdDate);
				 
				 System.out.println("Release Typein case=="+aJoRelease.getReleaseType());
				 theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber,aCuSO,theReleaseType);
				break;
			
			}	
					
			}else if(theOperation.equals("edit")){
				aJoRelease.setJoReleaseId(theJoreleaseId);
				//aJoReleaseDetail.setJoReleaseDetailId(theJoreleaseDetailId);
				//aJoReleaseDetail.setJoReleaseId(theJoreleaseId);
				JoRelease thejoRel=itsJobService.getJoRelease(theJoreleaseId);
				
				if((thejoRel.getReleaseType()==2 && theReleaseType==1) || (thejoRel.getReleaseType()==2 && theReleaseType==4 ) ||
						(thejoRel.getReleaseType()==5 && theReleaseType==1) || (thejoRel.getReleaseType()==5 && theReleaseType==4) ||
						(thejoRel.getReleaseType()==3 && theReleaseType==1) || (thejoRel.getReleaseType()==3 && theReleaseType==4) 
						){
					//Stock Order to Drop Ship ,Stock Order to commission Order ,Service to Drop ship ,Service to comm order
						setJomasterDetails(theJobNumber,theJoMasterID);
						aVepo.setJoReleaseId(theJoreleaseId);	
						aVepo.setChangedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
						aVepo.setChangedOn(new Date());
						aVepo.setCustomerPonumber(CustPoNo);
						 if(theCustomerPONumber!=null){
							 aVepo.setCustomerPonumber(theCustomerPONumber.trim());	 
						 }
						aVepo.setTransactionStatus(1);
						aVepo.setOrderedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
						
						 thevePoId=itsJobService.addvePOfromInsideJob(aVepo,aJoRelease,theJobNumber,thecuSOID);
				}else if((thejoRel.getReleaseType()==1 && theReleaseType==2) || (thejoRel.getReleaseType()==4 && theReleaseType==2) ||
						(thejoRel.getReleaseType()==1 && theReleaseType==5) || (thejoRel.getReleaseType()==4 && theReleaseType==5) ||
						(thejoRel.getReleaseType()==3 && theReleaseType==2) || (thejoRel.getReleaseType()==3 && theReleaseType==5)
						){
					//Drop Ship to Stock Order ,commission Order to Stock Order , Drop ship to Service , commission order to Service 
					 SysUserDefault asysusrdeflt=itsEmpAssignedService.getSysUserDefault(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
					 aCuSO.setJoReleaseId(theJoreleaseId);	
					 aCuSO.setCustomerPonumber(CustPoNo);
					 aCuSO.setRxCustomerId(theRxMasterID);
					 aCuSO.setRxBillToId(theRxMasterID);
					 aCuSO.setCuTermsId(1);
					 aCuSO.setTransactionStatus(1);
					 aCuSO.setPrFromWarehouseId(asysusrdeflt.getWarehouseID());
					 aCuSO.setCoDivisionID(theJomaster.getCoDivisionId());
					 aCuSO.setSonumber(theJobNumber+JobUtil.IntToLetter(thejoRel.getSeq_Number()).toUpperCase());
					 Date createdDate = new Date();
					 if(theJomaster!=null && theJomaster.getDescription()!=null){
						 aCuSO.setTag(theJomaster.getDescription());
					 }
					 if(theCustomerPONumber!=null){
						 aCuSO.setCustomerPonumber(theCustomerPONumber.trim());	 
					 }
					 aCuSO.setShipToMode((byte)2); // shiptomode
					 aCuSO.setShipDate(createdDate);
					 aCuSO.setCuAssignmentId0(theJomaster.getCuAssignmentId0());
					 aCuSO.setCuAssignmentId1(theJomaster.getCuAssignmentId1());
					 aCuSO.setCuAssignmentId2(theJomaster.getCuAssignmentId2());
					 aCuSO.setCuAssignmentId3(theJomaster.getCuAssignmentId3());
					 aCuSO.setCuAssignmentId4(theJomaster.getCuAssignmentId4());
					 
					 aCuSO.setCreatedOn(createdDate);
					 aCuSO.setCreatedById(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
					 Integer cusoID=itsJobService.addcusotodsorcommordfromInsideJob(thevePoId,aCuSO);
				}else if(
						(thejoRel.getReleaseType()==1||thejoRel.getReleaseType()==2 ||thejoRel.getReleaseType()==4 ||thejoRel.getReleaseType()==5) && theReleaseType==3  
						){
							if(thejoRel.getReleaseType()==1 || thejoRel.getReleaseType()==4){
								//DropShip,Commission Order to Bill Only
								itsJobService.deleteVepo(thevePoId);
							}
							if(thejoRel.getReleaseType()==2 || thejoRel.getReleaseType()==5){
								//Stock,Service Order to Bill Only		
								itsJobService.deletecuso(thecuSOID);
							}
						}
				/*aVepo.setVePoid(thevePoId);
				Vepo aVePO = itsVendorService.getVePo(aVepo.getVePoid());
				aVepo.setBillToIndex(aVePO.getBillToIndex());
				aVepo.setShipTo(aVePO.getShipTo());*/
				//aVepo.setPonumber(aVePO.getPonumber());
//				aVepo.setShipToMode(aVePO.getShipToMode());
				if(theReleaseType==1){
					aVepo.setVePoid(thevePoId);
				}
				itsJobService.editRelease(aJoRelease, aJoReleaseDetail, aVepo);
			}else if(theOperation.equals("del")){
				aJoRelease.setJoReleaseId(theJoreleaseId);
				aJoRelease.setJoMasterId(theJoMasterID);
				System.out.println(theJoMasterID);
				aJoReleaseDetail.setJoReleaseDetailId(theJoreleaseDetailId);
				aJoReleaseDetail.setJoReleaseId(theJoreleaseId);
				aVepo.setJoReleaseId(theJoreleaseId);
				aVepo.setVePoid(thevePoId);
				aCuSO.setCuSoid(thecuSOID);
				itsJobService.deleteRelease(aJoRelease,aJoReleaseDetail,aVepo,aCuSO);
			}
		}catch (JobException e) {
			sendTransactionException("<b>theJoreleaseDetailId,theJoreleaseId:</b>"+theJoreleaseDetailId+","+theJoreleaseId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return theVepo;
	}
		
	public String getCutomerRxName() {
		return itsCutomerRxName;
	}

	public void setCutomerRxName(String cutomerRxName) {
		this.itsCutomerRxName = cutomerRxName;
	}


	
	@RequestMapping(value = "jobReleaseLineItemExcel",method = RequestMethod.POST)
	public @ResponseStatus(value=HttpStatus.OK) @ResponseBody  String[] pOReleaseLineItemListForExcel(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
																HttpSession session,HttpServletRequest therequest,	 HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
		List<Vepodetail> aPOLineItemDetails = null;
		String[] fileDetails = null;
		try{
			aPOLineItemDetails = itsJobService.getPOReleaseLineItem(theVepoId);
			 fileDetails = createExcel(aPOLineItemDetails,session,therequest,theResponse);
	} catch (Exception e) {
		sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
		itsLogger.error(e.getMessage());
		//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return fileDetails;
	}
	
	@RequestMapping(value = "jobReleaseLineItemDownload",method = RequestMethod.GET)
	public @ResponseStatus(value=HttpStatus.OK) @ResponseBody  void pOReleaseLineItemListForDownload(
			@RequestParam(value="fileName", required=true) String fileName,
			@RequestParam(value="fileLocation", required=true) String fileLocation,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException{
		 FileInputStream fis = null;
		try{
			String file = fileLocation;
			File f = new File(file);
			if (f.exists()) {
			theResponse.setContentType("application/xlsx");
			theResponse.setContentLength(new Long(f.length()).intValue());
			theResponse.setHeader("Content-Disposition", "attachment; filename="+fileName);
			fis = new FileInputStream(f);
			FileCopyUtils.copy(fis, theResponse.getOutputStream());
			theResponse.flushBuffer();
			} else {
			System.out.println("File"+file+"("+f.getAbsolutePath()+") does not exist");
			}
	} catch (IOException e) {
		sendTransactionException("<b>fileLocation:</b>"+fileLocation,"JOB",e,session,therequest);
		itsLogger.error(e.getMessage());
		//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		finally
		{
			fis.close();
		}
	}
	
	
	@RequestMapping(value = "jobReleaseLineItem",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getPOReleaseLineItemList(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
																	HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
		CustomResponse aResponse = new CustomResponse();
		try{
			List<Vepodetail> aPOLineItemDetails = itsJobService.getPOReleaseLineItem(theVepoId);
						aResponse.setRows(aPOLineItemDetails);
		}catch (JobException e) {
			sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}
	
	@RequestMapping(value = "generateReceivedInventoryPDF",method = RequestMethod.GET)
	public @ResponseBody void generateReceivedInventoryPDF(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
			HttpServletRequest therequest,HttpSession session, HttpServletResponse theResponse) throws JobException, IOException, SQLException, MessagingException{
		HashMap<String, Object> params = new HashMap<String, Object>();
		Connection connection = null;
		ConnectionProvider con =null;
		try{
			String path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/POReceive.jrxml");
			params.put("vePOID", theVepoId);
			Date dNow = new Date( );
			SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
			String filename="ReceivedInventory_"+ft.format(dNow)+".pdf";
			con = itspdfService.connectionForJasper();
			 connection = con.getConnection();
			ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
		}catch (Exception e) {
			sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
			e.printStackTrace();
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
		
	}
	
	@RequestMapping(value = "generatepriorReceivedInventoryPDF",method = RequestMethod.GET)
	public @ResponseBody void generatepriorReceivedInventoryPDF(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
			@RequestParam(value="veReceiveId", required=false) Integer veReceiveId, 
			HttpServletRequest therequest,HttpSession session, HttpServletResponse theResponse) throws JobException, IOException, SQLException, MessagingException{
		HashMap<String, Object> params = new HashMap<String, Object>();
		Connection connection = null;
		ConnectionProvider con = null;
		try{
			String path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/POReceiveInv.jrxml");
			params.put("vePOID", theVepoId);
			params.put("veReceiveId", veReceiveId);
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
			String filename="ReceivedInventory_"+ft.format(dNow)+".pdf";
			con = itspdfService.connectionForJasper();
			 connection = con.getConnection();
			ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
		}catch (Exception e) {
			sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
			e.printStackTrace();
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
		
	}
	
	
	@RequestMapping(value = "jobSo_ReleaseLineItem",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getSOReleaseLineItemList(@RequestParam(value="rxmanufacturerID", required=false) Integer theRxManufacturerID, 
										HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
		CustomResponse aResponse = new CustomResponse();
		try{
			List<?> aSOLineItemDetails = itsJobService.getSOReleaseLineItem(theRxManufacturerID);
			aResponse.setRows(aSOLineItemDetails);
		}catch (JobException e) {
			sendTransactionException("<b>theRxManufacturerID:</b>"+theRxManufacturerID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}

	@RequestMapping(value = "jobReleaseAck",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getPOReleaseAckList(@RequestParam(value="vePOID", required=false) Integer theVepoid, 
																HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
		CustomResponse aResponse = new CustomResponse();
		try{
			List<Vepodetail> aPOReleaseAckDetails = itsJobService.getPOReleaseAck(theVepoid);
			aResponse.setRows(aPOReleaseAckDetails);
		}catch (JobException e) {
			sendTransactionException("<b>theVepoid:</b>"+theVepoid,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}
	
	@RequestMapping(value="/manpulaterPOReleaseLineItem", method = RequestMethod.POST)
	public @ResponseBody Integer insertSubmittalSchedule(@RequestParam(value="note", required= false) String theItemCode,
			   @RequestParam(value="description", required= false) String theDescription, 
			   @RequestParam(value="quantityOrdered", required=false) BigDecimal theQuantity,
			   @RequestParam(value="unitCost", required= false) BigDecimal theUnitCost, 
			   @RequestParam(value="priceMultiplier", required= false) BigDecimal theMulti, 
			   @RequestParam(value="taxable", required = false) Boolean theTax,
			   @RequestParam(value="received", required = false) BigDecimal theQuantityReceived,
			   @RequestParam(value="total", required = false) BigDecimal theTotal,
			   @RequestParam(value="vePoid", required = false) Integer theVepoId,
			   @RequestParam(value="prMasterId", required = false) Integer thePRMasterID,
			   @RequestParam(value="vePodetailId", required = false) Integer theVepoDetailId,
			   @RequestParam(value="oper", required = false) String theOperation,
			   @RequestParam(value="posistion", required = false) Double thePosistion,
			   @RequestParam(value="taxValue", required = false) BigDecimal theTaxAmount,
			   @RequestParam(value="ackDate", required = false) String theAckDate,
			   @RequestParam(value="shipDate", required = false) String theShipDate,
			   @RequestParam(value="ackDatename", required = false) String ackDatename,
			   @RequestParam(value="shipDatename", required = false) String shipDatename,
			   @RequestParam(value="vendorOrderNumber", required = false) String theVendorOrderNumber,
			   @RequestParam(value="operForAck", required = false) String theOperForAck,
			   @RequestParam(value="ackPOID[]", required = false) ArrayList<?> theAckPOId,
			   @RequestParam(value="shipPOID[]", required = false) ArrayList<?> theShipPOId,
			   @RequestParam(value="orderNumberPOID[]", required = false) ArrayList<?> theOrderNumberPOID,
			   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		
		itsLogger.info(" theItemCode = "+theItemCode+" || theDescription = "+theDescription+" || theQuantity = "+theQuantity+" || theUnitCost = "+theUnitCost
				+"\n || theMulti = "+theMulti+" || theTax = "+theTax+" || theQuantityReceived = "+theQuantityReceived+" || theTotal = "+theTotal
				+"\n || theVepoId = "+theVepoId+" || thePRMasterID = "+thePRMasterID+" || theOperation = "+theOperation+" || thePosistion = "+thePosistion
				+"\n || theTaxAmount = "+theTaxAmount+" || "+theAckDate+" || "+theShipDate+" || "+theVendorOrderNumber+" || theOperForAck = "+theOperForAck
				+"\n || ackDatename = "+ackDatename+" || shipDatename ="+shipDatename
				+"\n || theAckPOId = "+theAckPOId+" || theShipPOId = "+theShipPOId+" || theOrderNumberPOID = "+theOrderNumberPOID);
		Vepodetail aVepodetail = new Vepodetail();
		Vepodetail vepoupdateObj  = null;
		double aTaxDecimal, aTotalTax;
		itsLogger.info("operForAck ::"+theOperation+" :: "+theOperForAck);
		itsLogger.info("theVepoDetailId : "+theVepoDetailId);
		itsLogger.info("thePRMasterID : "+thePRMasterID);
		if(theOperForAck == null)
			theOperForAck = " ";
		
		itsLogger.info("operForAck ::"+theOperation+" :: "+theOperForAck);
		itsLogger.info("theVepoDetailId : "+theVepoDetailId);
		itsLogger.info("thePRMasterID : "+thePRMasterID);
		Vepo aVepo = new Vepo();
		Float aFloat = null;
		Vepo aVepoupdateObj = null;
		BigDecimal[] costDetails = {theUnitCost,theMulti,theQuantity,theTaxAmount};
		aVepodetail.setDescription(theDescription);
		aVepodetail.setQuantityOrdered(theQuantity);
		aVepodetail.setUnitCost(theUnitCost);
		aVepodetail.setPriceMultiplier(theMulti);
		aVepodetail.setTaxable(theTax);
		
		try{			
			if(theOperForAck!=null && theOperForAck.equals("acknowlegement")){
				if(shipDatename != null && !shipDatename.equals("")){
					System.out.println( " shipdate = "+DateUtils.parseDate(shipDatename, new String[]{"MM/dd/yyyy"}));
					aVepodetail.setEstimatedShipDate(DateUtils.parseDate(shipDatename, new String[]{"MM/dd/yyyy"}));
				}if(ackDatename != null && !ackDatename.equals("")){
					System.out.println(" ackdate = "+DateUtils.parseDate(ackDatename, new String[]{"MM/dd/yyyy"}));
					aVepodetail.setAcknowledgedDate(DateUtils.parseDate(ackDatename, new String[]{"MM/dd/yyyy"}));
				}
				aVepodetail.setVendorOrderNumber(theVendorOrderNumber);
			}else if(theOperForAck!=null && theOperForAck.equals("all")){
				if(theShipDate != null && !theShipDate.equals("")){
					System.out.println( " shipdate = "+DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
					aVepodetail.setEstimatedShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
				}if(theAckDate != null && !theAckDate.equals("")){
					System.out.println(" ackdate = "+DateUtils.parseDate(theAckDate, new String[]{"MM/dd/yyyy"}));
					aVepodetail.setAcknowledgedDate(DateUtils.parseDate(theAckDate, new String[]{"MM/dd/yyyy"}));
				}
				aVepodetail.setVendorOrderNumber(theVendorOrderNumber);
			}else{
				
				if(theShipDate != null && !theShipDate.equals("")){
					System.out.println( " shipdate = "+DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
					aVepodetail.setEstimatedShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
				}if(theAckDate != null && !theAckDate.equals("")){
					System.out.println(" ackdate = "+DateUtils.parseDate(theAckDate, new String[]{"MM/dd/yyyy"}));
					aVepodetail.setAcknowledgedDate(DateUtils.parseDate(theAckDate, new String[]{"MM/dd/yyyy"}));
				}
				aVepodetail.setVendorOrderNumber(theVendorOrderNumber);
				aVepodetail.setDescription(theDescription);
				aVepodetail.setQuantityOrdered(theQuantity);
				aVepodetail.setUnitCost(theUnitCost);
				aVepodetail.setPriceMultiplier(theMulti);
				aVepodetail.setTaxable(theTax);
				
				if(theVepoDetailId != null){
					vepoupdateObj = itsJobService.getVePODetails(theVepoDetailId);
				}
				
				if(theTax!=null){
					if(theTax == true){
						aVepo = getTotalamount(aVepo,costDetails);
						aTaxDecimal =aVepo.getTaxTotal().doubleValue();
						Vepo aVepo2 = itsVendorService.getVePo(theVepoId);
						if(aVepo2.getTaxTotal() != null ){					// from second Line item record in the same po
							aFloat = aVepo2.getTaxTotal().floatValue();
							aTotalTax = aTaxDecimal + aFloat;
							BigDecimal aTaxValue = new BigDecimal(aTotalTax);
							if(vepoupdateObj != null &&  vepoupdateObj.getTaxable() != false )
							aTaxValue = new BigDecimal(aFloat);
							aVepo.setTaxTotal(aTaxValue);
						}else{											// Adding First line item record to the PO	
							aFloat = new Float(0);
							aTotalTax = aTaxDecimal + aFloat;
							BigDecimal aTaxValue = new BigDecimal(aTotalTax);
							aVepo.setTaxTotal(aTaxValue);
						}
						aVepo.setVePoid(theVepoId);
					}else if( theTax == false){
						aVepoupdateObj = itsVendorService.getVePo(theVepoId);
						BigDecimal ataxTotal=aVepoupdateObj.getTaxTotal();
						if(ataxTotal!=null){
							aVepo = getTotalamount(aVepo, costDetails);
							aTaxDecimal =aVepo.getTaxTotal().doubleValue();
							if(aVepoupdateObj.getTaxTotal() != null){
								aFloat = aVepoupdateObj.getTaxTotal().floatValue();
								aTotalTax = aFloat -aTaxDecimal;
								BigDecimal aTaxValue;
								if(vepoupdateObj != null && vepoupdateObj.getTaxable() != false)
									aTaxValue = new BigDecimal(aTotalTax);
								else
									aTaxValue = new BigDecimal(aFloat);
									aVepo.setTaxTotal(aTaxValue);
							}
						aVepo.setVePoid(theVepoId);
						}
					}
				}else{
						aVepoupdateObj = itsVendorService.getVePo(vepoupdateObj.getVePoid());
						BigDecimal ataxTotal=aVepoupdateObj.getTaxTotal();
						BigDecimal[] costDetail = {vepoupdateObj.getUnitCost(),vepoupdateObj.getPriceMultiplier(),vepoupdateObj.getQuantityOrdered(),theTaxAmount};
						if(ataxTotal!=null){
							aVepo = getTotalamount(aVepo, costDetail);
							aTaxDecimal =aVepo.getTaxTotal().doubleValue();
								aFloat = aVepoupdateObj.getTaxTotal().floatValue();
								aTotalTax = aFloat -aTaxDecimal;
								aVepoupdateObj.setTaxTotal(new BigDecimal(aTotalTax));
					}
						aVepodetail.setTaxable(vepoupdateObj.getTaxable());
				}
			}
			
			aVepodetail.setPrMasterId(thePRMasterID);
			aVepodetail.setVePoid(theVepoId);
			aVepo.setVePoid(theVepoId);
			if(theOperation.equals("add")){
				itsJobService.addPOReleaseLineItem(aVepodetail, aVepo);
			}else if(theOperation.equals("edit")){
				if(theOperForAck.equals("acknowlegement")){
					aVepodetail.setDescription(theOperForAck);
					
				}else if(theOperForAck.equals("all")){
					aVepodetail.setDescription(theOperForAck);
					aVepodetail.setShipDate(theShipPOId);
					aVepodetail.setAckDate(theAckPOId);
					aVepodetail.setOrderNumber(theOrderNumberPOID);
				}else if(thePosistion != null){
					aVepodetail.setPosistion(thePosistion);
				}
				aVepodetail.setVePodetailId(theVepoDetailId);
				itsJobService.editPOReleaseLineItem(aVepodetail, aVepo);
			}else if(theOperation.equals("del")){
				aVepodetail.setVePodetailId(theVepoDetailId);
				itsJobService.deletePOReleaseLineItem(aVepodetail,aVepoupdateObj);
			}
		}catch (JobException e) {
			sendTransactionException("<b>theVepoId,theVepoDetailId:</b>"+theVepoId+","+theVepoDetailId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}
	
	@RequestMapping(value="/manpulaterSOReleaseLineItem", method = RequestMethod.POST)
	public @ResponseBody Integer insert_So_release_Schedule(@RequestParam(value="itemCode", required= false) String theIItemCode,
														   @RequestParam(value="description", required= false) String theDescription, 
														   @RequestParam(value="boxQty", required=false) BigDecimal theQuantity,
														   @RequestParam(value="salesPrice00", required= false) BigDecimal theSalesPrice00, 
														   @RequestParam(value="pomult", required= false) BigDecimal thePOMulit, 
														   @RequestParam(value="oper", required = false) String theOperation,
														   @RequestParam(value="isTaxable", required = false) Boolean theTax,
														   @RequestParam(value="submitted", required = false) BigDecimal theAmount,
														   @RequestParam(value="prMasterId", required = false) Integer thePRMasterID,
														   @RequestParam(value="prmastreID", required = false) Integer thePRMasterIDDel,
														   @RequestParam(value="manufacturer", required = false) Integer theManufactureName, 
														   HttpSession theSession, HttpServletResponse theResponse) throws ParseException, JobException, IOException {
		Prmaster aPrmaster = new Prmaster();
		byte aTax;
		byte aAssamble = 0;
		byte aConsignment = 0;
		byte aHasInitialCost = 0;
		byte aInActive = 0;
		byte aIsInventory = 0;
		byte aPrintOnPos = 0;
		byte aPrintOnSos = 0;
		byte aSingleItemTax = 0;
		byte aPrintOnCUs = 0;
		byte aPrintOnPTs = 0;
		byte aLabor = 0;
		byte aSerialized = 0;
		aPrmaster.setItemCode(theIItemCode);
		aPrmaster.setDescription(theDescription);
		aPrmaster.setBoxQty(theQuantity);
		aPrmaster.setSalesPrice00(theSalesPrice00);
		aPrmaster.setPOMult(thePOMulit);
		if(theOperation.equals("del")){
			aTax = 0;
		}else{
			aTax = (byte) (theTax?1:0);
		}
		aPrmaster.setIsTaxable(aTax);
		aPrmaster.setAutoAssemble(aAssamble);
		aPrmaster.setConsignment(aConsignment);
		aPrmaster.setHasInitialCost(aHasInitialCost);
		aPrmaster.setInActive(aInActive);
		aPrmaster.setIsInventory(aIsInventory);
		aPrmaster.setPrintOnPos(aPrintOnPos);
		aPrmaster.setPrintOnSos(aPrintOnSos);
		aPrmaster.setSingleItemTax(aSingleItemTax);
		aPrmaster.setPrintOnCUs(aPrintOnCUs);
		aPrmaster.setPrintOnPTs(aPrintOnPTs);
		aPrmaster.setLabor(aLabor);
		aPrmaster.setSerialized(aSerialized);
		aPrmaster.setSubmitted(theAmount);
		aPrmaster.setRxMasterIdprimaryVendor(theManufactureName);
		if(theOperation.equals("add")){
//				itsJobService.addPOReleaseLineItem(aPrmaster);
		}else if(theOperation.equals("edit")){
			aPrmaster.setPrMasterId(thePRMasterID);
//				itsJobService.editPOReleaseLineItem(aPrmaster);
		}else if(theOperation.equals("del")){
			aPrmaster.setPrMasterId(thePRMasterID);
//				itsJobService.deletePORelease_Product(aPrmaster);
		}
		return 0;
	}
	
	@RequestMapping(value="/updateReleaseNote", method = RequestMethod.POST)
	public @ResponseBody Integer updateReleaseNote(@RequestParam(value="releaseNote", required= false) String theReleaseNote,
													  @RequestParam(value="joBidDate", required= false) String thebidDate,
													  @RequestParam(value="joMasterID", required= false) Integer theJoMasterID,
													  @RequestParam(value="jobName", required = false) String thejobName,
													  @RequestParam(value="jobNumber", required = false) String thejobNumber,
													  @RequestParam(value="noticeId", required = false) Integer noticeId,
													  @RequestParam(value="contactId", required = false) Integer contactId,
													  @RequestParam(value="notice", required = false) String notice,
													  @RequestParam(value="noticeName", required = false) String noticeName,
													  @RequestParam(value="otherContact", required = false) Integer otherContact,
													  @RequestParam(value="PONumberID", required = false) String PONumberID,
													  HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Jomaster aJomaster = new Jomaster();
		try{
			DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			if(thebidDate!=null && !thebidDate.trim().equals("")){
			Date d1 = df2.parse(thebidDate);
			aJomaster.setBidDate(d1);
			}
			aJomaster.setJoMasterId(theJoMasterID);
			itsLogger.info("theReleaseNote in Controller===>"+theReleaseNote);
			aJomaster.setReleaseNotes(theReleaseNote);
			aJomaster.setDescription(thejobName);
			aJomaster.setJobNumber(thejobNumber);
			
			aJomaster.setNoticeId(noticeId);
			aJomaster.setContactId(contactId);
			aJomaster.setNotice(notice);
			aJomaster.setOtherContact(otherContact);
			if(PONumberID!=null&&!PONumberID.equals(""))
			aJomaster.setCustomerPonumber(PONumberID);
			if(noticeName!=null && !noticeName.equals("")){
				itsLogger.info("noticeName:"+noticeName);
				aJomaster.setContactName(noticeName);
			}
			itsJobService.updateJobRelease(aJomaster);
		}catch (JobException e) {
			sendTransactionException("<b>thejobNumber,PONumberID:</b>"+thejobNumber+","+PONumberID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}
	
	@RequestMapping(value="/billNote", method = RequestMethod.GET)
	public @ResponseBody Integer updateBillNote(@RequestParam(value="joReleaseDate", required= false) Date theReleaseDate,
													   @RequestParam(value="joReleaseId", required= false) Integer theJoreleaseId,
													   @RequestParam(value="joReleaseType", required= false) Integer theReleaseType, 
													   @RequestParam(value="ReleaseNote", required=false) String theNote,
													   @RequestParam(value="EstimatedBilling", required= false) BigDecimal theAllocatedName, 
													   @RequestParam(value="joMasterID", required= false) Integer theJoMasterID,
													   @RequestParam(value = "billNote",required = false) String theBillNote,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		JoRelease aJoRelease = new JoRelease();
		try{
			aJoRelease.setReleaseDate(theReleaseDate);
			aJoRelease.setReleaseType(theReleaseType);
			aJoRelease.setEstimatedBilling(theAllocatedName);
			aJoRelease.setJoMasterId(theJoMasterID);
			aJoRelease.setReleaseNote(theNote);
			aJoRelease.setBillNote(theBillNote);
			aJoRelease.setJoReleaseId(theJoreleaseId);
			itsJobService.updateBillNote(aJoRelease);
		}catch (JobException e) {
			sendTransactionException("<b>theJoreleaseId:</b>"+theJoreleaseId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}
	
	// Method to enable taxes in all rows and to compute total tax.
	@RequestMapping(value="selectAllTaxes", method = RequestMethod.POST)
	public @ResponseBody Integer addAllTaxes(@RequestParam(value = "vePoId", required = false) Integer theVepoId,
												@RequestParam(value="aTaxValue", required= false) BigDecimal theTaxAmount,
												HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, VendorException, MessagingException{ 

		List<Vepodetail> aPOLineItemDetails = null;
		int count = 0;
		try {
			aPOLineItemDetails = itsJobService.getPOReleaseLineItem(theVepoId);
			for(Vepodetail vp : aPOLineItemDetails)
			{
				Vepo aVepo = new Vepo();
				aVepo.setVePoid(vp.getVePoid());
				BigDecimal[] costDetails = {vp.getUnitCost(),vp.getPriceMultiplier(),vp.getQuantityOrdered(),theTaxAmount};
				
				Vepo aVepoupdateObj = itsVendorService.getVePo(theVepoId);
				if(vp.getTaxable()==false)
				{  
					aVepo = getTotalamount(aVepo,costDetails);
					double aTaxDecimal =aVepo.getTaxTotal().doubleValue();
					Float aFloat = aVepoupdateObj.getTaxTotal().floatValue();
					double aTotalTax = aTaxDecimal + aFloat;
					BigDecimal aTaxValue = new BigDecimal(aTotalTax);
					aVepo.setTaxTotal(aTaxValue);
					vp.setTaxable(true);
//					vp.setTaxable(true);
					itsJobService.editPOReleaseLineItem(vp, aVepo);
				}
				count++;
			}
		} catch (JobException e) {
			sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return count;
	}
	@RequestMapping(value="/updatePOLineItemPosition", method = RequestMethod.POST)
	public @ResponseBody joQuoteDetailPosition updateLineItemPosition(@RequestParam(value = "selectedRowID", required = false) Integer theSelectedRowID, 
																		@RequestParam(value = "selectedPositionDetailID", required = false) Integer theSelectedPositionDetailID, 
																		@RequestParam(value = "selectedQuoteDetailID", required = false) Integer theSelectedQuoteDetailID, 
																		@RequestParam(value = "selectedJoQuoteHeaderID", required = false) Integer theSelectedJoQuoteHeaderID, 
																		@RequestParam(value = "abovePositionRowID", required = false) Integer theAbovePositionRowID, 
																		@RequestParam(value = "abovePositionDetailID", required = false) Integer theAbovePositionDetailID,
																		@RequestParam(value = "aboveQuoteDetailID", required = false) Integer theAboveQuoteDetailID,
																		@RequestParam(value="oper", required = false) String theOperation, HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, MessagingException{
		joQuoteDetailPosition aJoQuoteDetailPosition = new joQuoteDetailPosition();
		try{
			aJoQuoteDetailPosition.setSelectedRowID(theSelectedRowID);
			aJoQuoteDetailPosition.setSelectedPositionDetailID(theSelectedPositionDetailID);
			aJoQuoteDetailPosition.setSelectedQuoteDetailID(theSelectedQuoteDetailID);
			aJoQuoteDetailPosition.setSelectedJoQuoteHeaderID(theSelectedJoQuoteHeaderID);
			aJoQuoteDetailPosition.setAbovePositionRowID(theAbovePositionRowID);
			aJoQuoteDetailPosition.setAbovePositionDetailID(theAbovePositionDetailID);
			aJoQuoteDetailPosition.setAboveQuoteDetailID(theAboveQuoteDetailID);
			itsJobService.updatePOLineItemUpDownPosition(aJoQuoteDetailPosition);
			itsJobService.updatePOLineItemUpPosition(aJoQuoteDetailPosition);
		}catch (JobException e) {
			sendTransactionException("<b>theSelectedQuoteDetailID,theSelectedJoQuoteHeaderID:</b>"+theSelectedQuoteDetailID+","+theSelectedJoQuoteHeaderID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoQuoteDetailPosition;
	}

	public List<Veshipvia> getItsShipVia() {
		return itsShipVia;
	}

	public void setItsShipVia(List<Veshipvia> itsShipVia) {
		this.itsShipVia = itsShipVia;
	}

	public List<CoTaxTerritory> getJobSiteTaxValue() {
		return itsJobSiteTaxValue;
	}

	public void setJobSiteTaxValue(List<CoTaxTerritory> jobSiteTaxValue) {
		this.itsJobSiteTaxValue = jobSiteTaxValue;
	}
	
	/*@RequestMapping(value="/manuplateChangeOrder", method = RequestMethod.POST)
	public @ResponseBody Integer manuChangeOrder(@RequestParam(value="changdate", required= false) Date theChangeDate,
													   @RequestParam(value="customerPonumber", required= false) String thePoNumber,
													   @RequestParam(value="changeReason", required= false) String theChangeReason,
													   @RequestParam(value="joMasterId", required= false) Integer thejoMasterId,
													   @RequestParam(value="joChangeId", required= false) Integer thejoChangeId, 
													   @RequestParam(value="changeById", required = false) Integer theChangebyId,
													   @RequestParam(value="changeByName", required=false) String thechangeByName,
													   @RequestParam(value="oper", required = false) String operation, HttpSession session) throws ParseException{
		Jochange aJochange = new Jochange();
		Vepo aVepo = new Vepo();
		try{
			aJochange.setChangeDate(theChangeDate);
			aJochange.setJoMasterId(joMasterId)(theReleaseType);
			aJochange.setEstimatedBilling(theAllocatedName);
			aJochange.setJoMasterId(theJoMasterID);
			aJochange.setReleaseNote(theNote);
			aJoReleaseDetail.setVendorInvoiceAmount(thePOAmount);
			aJoReleaseDetail.setCustomerInvoiceAmount(theReleaseInvoiceName);
			aJoReleaseDetail.setJoMasterId(theJoMasterID);
			aVepo.setVeFactoryId(theManuId);
			if(operation.equals("add")){
				itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo);
			}else if(operation.equals("edit")){
				aJoRelease.setJoReleaseId(theJoreleaseId);
				aJoReleaseDetail.setJoReleaseDetailId(theJoreleaseDetailId);
				aJoReleaseDetail.setJoReleaseId(theJoreleaseId);
				aVepo.setJoReleaseId(theJoreleaseId);
				aVepo.setVePoid(thevePoId);
				itsJobService.editRelease(aJoRelease, aJoReleaseDetail, aVepo);
			}else if(operation.equals("del")){
				aJoRelease.setJoReleaseId(theJoreleaseId);
				aJoReleaseDetail.setJoReleaseDetailId(theJoreleaseDetailId);
				aJoReleaseDetail.setJoReleaseId(theJoreleaseId);
				aVepo.setJoReleaseId(theJoreleaseId);
				aVepo.setVePoid(thevePoId);
				itsJobService.deleteRelease(aJoRelease,aJoReleaseDetail,aVepo);
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage());
		}
		return 0;
	}*/
	public Vepo getTotalamount(Vepo vepoObj,BigDecimal[] costDetails){
		BigDecimal aUnitCost = costDetails[0];
		BigDecimal aPriceMult = costDetails[1];
		BigDecimal aQuantity = costDetails[2];
		BigDecimal theTaxAmount =costDetails[3];
		BigDecimal aTotal = null;
		double aTaxDecimal = 0;
	
		if(aUnitCost != null && aPriceMult != null && aQuantity != null){
			if(aPriceMult.compareTo(new BigDecimal("0.0000"))==0) {
				aTotal = aUnitCost.multiply(new BigDecimal("1.0000"));
				aTotal = aTotal.multiply(aQuantity);
				vepoObj.setTotalAmount(aTotal);
			} else {
				aTotal = aUnitCost.multiply(aPriceMult);
				aTotal = aTotal.multiply(aQuantity);
				vepoObj.setTotalAmount(aTotal);
			}
		}else if(aUnitCost != null && aQuantity != null){
			aTotal = aUnitCost.multiply(aQuantity);
			vepoObj.setTotalAmount(aTotal);
		}else if(aUnitCost != null && aPriceMult != null){
			aTotal = aUnitCost.multiply(aQuantity);
			vepoObj.setTotalAmount(aTotal);
		}else if(aUnitCost != null){
			vepoObj.setTotalAmount(aUnitCost);
		}
		if(aTotal==null){
			aTotal=new BigDecimal(0.0);
		}
		if(theTaxAmount==null){
			theTaxAmount=new BigDecimal(0.00);
		}
		aTaxDecimal = aTotal.floatValue() * (theTaxAmount.floatValue() / 100);
		vepoObj.setTaxTotal(new BigDecimal(aTaxDecimal));
		return vepoObj;
	}
	public String getCustomerPOnumber(String customerPOValue,int theJoMasterID) throws JobException
	{
		String customerPOnumber = "";
		List<JoCustPO> customerpoObj = null;
		JoCustPO customerpoObject = null;
		customerpoObj=itsJobService.getJoCustPODetail(theJoMasterID);
		if(itsJobService.getCustomerPONoFromJomaster(theJoMasterID).equals(customerPOValue))
			customerPOnumber = itsJobService.getCustomerPONoFromJomaster(theJoMasterID);
		if((customerpoObj.size()>0)){
		customerpoObject = customerpoObj.get(0);
		if(itsJobService.getCustomerPONoFromJomaster(theJoMasterID).equals(customerPOValue))
			customerPOnumber = itsJobService.getCustomerPONoFromJomaster(theJoMasterID);
		else if(customerpoObject.getCustomerPonumber1()!=null && customerpoObject.getCustomerPonumber1().equals(customerPOValue))
			customerPOnumber = customerpoObject.getCustomerPonumber1();
		else if(customerpoObject.getCustomerPonumber2()!=null && customerpoObject.getCustomerPonumber2().equals(customerPOValue))
			customerPOnumber = customerpoObject.getCustomerPonumber2();
		else if(customerpoObject.getCustomerPonumber3()!=null && customerpoObject.getCustomerPonumber3().equals(customerPOValue))
			customerPOnumber = customerpoObject.getCustomerPonumber3();
		else 
			customerPOnumber = customerpoObject.getCustomerPonumber4();
		}
		List<Jochange> thechangepolist=itsJobService.getChangeorderList(theJoMasterID);
		for(Jochange  thejochJochange:thechangepolist){
			if(thejochJochange.getCustomerPonumber()!=null && thejochJochange.getCustomerPonumber().equals(customerPOValue)){
				customerPOnumber=thejochJochange.getCustomerPonumber();
				break;
			}
		}
		return customerPOnumber;
		
	}
	public String[] createExcel(List<Vepodetail> poRelease,HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException{
		
		//Blank workbook
		
		XSSFWorkbook poWorkbook = new XSSFWorkbook();
		XSSFSheet poWorkSheet = poWorkbook.createSheet("PO_Release_LineItem");
		CellStyle style=poWorkbook.createCellStyle();
		int rowNumber = 1;
		Row headers = poWorkSheet.createRow(0);
			XSSFFont defaultFont= poWorkbook.createFont();
		    defaultFont.setFontHeightInPoints((short)10);
		    defaultFont.setFontName("Arial");
		    defaultFont.setColor(IndexedColors.BLACK.getIndex());
		    defaultFont.setBold(false);
		    defaultFont.setItalic(false);
		    style.setFont(defaultFont);
		    
		    CellStyle stl = poWorkbook.createCellStyle();
		    XSSFFont font= poWorkbook.createFont();
		    font.setFontHeightInPoints((short)12);
		    font.setFontName("Arial");
		    font.setBold(true);
		    font.setItalic(false);
		    font.setColor(HSSFColor.BLACK.index);
	        stl.setFont(font);
		   
		
		String[] header = {"Product No", "Description","Qty", "Cost Each", "Multiplier", "Tax","Net Each","Amount", "TaxTotal"};
		for (int i = 0; i < header.length; i++) {
			headers.createCell(i).setCellValue(header[i]);
			headers.getCell(i).setCellStyle(stl);
		}
			for(Vepodetail vp : poRelease)
			{
			Row row = poWorkSheet.createRow(rowNumber++);
			int cellNumber = 0;
			Cell cell =null;
				cell = row.createCell(cellNumber++);
				cell.setCellValue(vp.getNote());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue(vp.getDescription());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue(vp.getQuantityOrdered().toString());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue("$"+vp.getUnitCost().toString());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue(vp.getPriceMultiplier().toString());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellStyle(style);
				if(vp.getTaxable().equals("true"))
				cell.setCellValue(true);
				else
				cell.setCellValue("false");
				cell = row.createCell(cellNumber++);
				cell.setCellValue("$"+vp.getNetCast());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue("$"+vp.getQuantityBilled());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue("$"+vp.getTaxTotal());
				cell.setCellStyle(style);
				cell = row.createCell(cellNumber++);
				cell.setCellValue((String)vp.getShipDate());
				cell.setCellStyle(style);
			
		}
			String fileName = "";
			String fileloc="";
			try {
				String fileLocation = System.getProperty("java.io.tmpdir");
				DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
				Calendar cal = Calendar.getInstance();
				String fileNamePrefix = dateFormat.format(cal.getTime()); 
				fileloc=fileLocation+"POReleaseSheet"+fileNamePrefix+".xlsx";
				fileName = "POReleaseSheet"+fileNamePrefix+".xlsx";
				FileOutputStream poReleaseSheet = new FileOutputStream(fileloc);
				poWorkbook.write(poReleaseSheet);
				poReleaseSheet.close();
			
			}catch (IOException e)
			{
				sendTransactionException("<b>MethodName:</b>createExcel","JOB",e,session,therequest);		
				itsLogger.error(e.getMessage());
			}
			String[] fileDetails = {fileName,fileloc};
			return fileDetails;
	}
	
	public List<Prwarehouse> getItsWareHouse() {
		return itsWareHouse;
	}

	public void setItsWareHouse(List<Prwarehouse> itsWareHouse) {
		this.itsWareHouse = itsWareHouse;
	}

	public List<Codivision> getItsDivision() {
		return itsDivision;
	}

	public void setItsDivision(List<Codivision> itsDivision) {
		this.itsDivision = itsDivision;
	}

	public List<Coaccount> getItsCoAccount() {
		return itsCoAccount;
	}

	public void setItsCoAccount(List<Coaccount> itsCoAccount) {
		this.itsCoAccount = itsCoAccount;
	}
	
	public Cumaster getCustomerRecord() {
		return itsCustomerRecord;
	}
	public void setCustomerRecord(int rxMasterId) {
		this.itsCustomerRecord = itsCuMasterService.getCustomerDetails(rxMasterId);
	}
	
	public List<JoCustPO> getItsPODetails() {
		return itsPODetails;
	}

	public void setItsPODetails(List<JoCustPO> itsPODetails) {
		this.itsPODetails = itsPODetails;
	}
	

	@RequestMapping(value="/getVendorInvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody Vebill getVendorInvoiceDetails(@RequestParam(value="releaseDetailID", required = false) Integer theReleaseDetailID,
													   @RequestParam(value="vePODetailId", required = false) Integer theVePOId,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException, EmployeeException{
		Vebill aVebill = new Vebill();
		try{
			Integer aVeBillID = itsJobService.getVeBillID(theReleaseDetailID, theVePOId);
			Sysvariable stmtgroupbyjob= itsEmployeeService.getSysVariableDetails("ReqInvWithPO");
			
			
			if(aVeBillID != null){
				aVebill = itsJobService.getVeBillDetails(aVeBillID);
				aVebill.setVeInvmandatory(stmtgroupbyjob.getValueLong());
			}
		}catch (JobException e) {
			sendTransactionException("<b>theReleaseDetailID,theVePOId:</b>"+theReleaseDetailID+","+theVePOId,"JOB",e,session,therequest);		
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aVebill;
	}
	
	@RequestMapping(value="/getCustomerInvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody Cuinvoice getCustomerInvoiceDetails(@RequestParam(value="releaseDetailID", required = false) Integer theReleaseDetailID,
																												@RequestParam(value="customerID", required = false) Integer theCustomerID,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Cuinvoice aCuinvoice = new Cuinvoice();
		try{
			Integer aInvoiceID = itsJobService.getCustomerInoiveID(theReleaseDetailID);
			if(aInvoiceID != null){
				aCuinvoice = itsJobService.getCustomerInvoiceDetails(aInvoiceID);
				System.out.println("Before poNumber---->"+aCuinvoice.getInvoiceNumber());
				if(aCuinvoice.getInvoiceNumber() != null && aCuinvoice.getInvoiceNumber().length() > 0)
				{
					String poNumber = aCuinvoice.getInvoiceNumber();
					
					char a = poNumber.charAt(poNumber.length()-1);
					System.out.println(a);
					if(Character.isLetter(a))
					{
						poNumber = poNumber+"1";
						aCuinvoice.setInvoiceNumber(poNumber);
					}
					else if(Character.isDigit(a))
					{
						int aa = Character.getNumericValue(a);
						System.out.println(aa);
						int i = Character.getNumericValue(a);
						i+= 1;
						poNumber = poNumber.substring(0, poNumber.length()-1)+i;
						aCuinvoice.setInvoiceNumber(poNumber);
						
					}
					System.out.println("After poNumber---->"+aCuinvoice.getInvoiceNumber());
				}
			}else{
				aCuinvoice.setJoReleaseDetailId(theReleaseDetailID);
				aCuinvoice.setRxCustomerId(theCustomerID);
				aCuinvoice.setRxBillToId(theCustomerID);
				aCuinvoice.setRxShipToId(theCustomerID);
				aCuinvoice.setTransactionStatus(2);
				aCuinvoice.setApplied(false);
				aCuinvoice.setAppliedAmount(new BigDecimal(0));
				aCuinvoice.setInvoiceAmount(new BigDecimal(0));
				aCuinvoice.setTaxAmount(new BigDecimal(0));
				Date now = new Date();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String createdDate = df.format(now);
				if((createdDate != null && createdDate != "")){
					aCuinvoice.setCreatedOn(DateUtils.parseDate(createdDate, new String[]{"MM/dd/yyyy"}));
				}	
				itsJobService.addCustomerInvoice(aCuinvoice);
			}
		}catch (JobException e) {
			sendTransactionException("<b>theReleaseDetailID,theCustomerID:</b>"+theReleaseDetailID+","+theCustomerID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCuinvoice;
	}
	
	/*@RequestMapping(value="/updateVendorInvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody Integer updateVendorInvoiceDetails(@RequestParam(value="vendorDateName", required= false) String theReleaseDate,
													   @RequestParam(value="vendorInvoiceNumName", required= false) String theVendorInvoiceName,
													   @RequestParam(value="dueDateName", required= false) String theDueDate,
													   @RequestParam(value="postDateChkName", required=false) boolean thePostChk,
													   @RequestParam(value="postDateName", required= false) String thePostDateName, 
													   @RequestParam(value="shipDateName", required= false) String theShipDate,
													   @RequestParam(value = "shipViaSelectName",required = false) Integer theShipViaID,
													   @RequestParam(value = "proNumberName",required = false) String thePRoNumber,
													   @RequestParam(value = "subtotal_Name",required = false) String theSubTotal,
													   @RequestParam(value = "freight_Name",required = false) BigDecimal theFrieght,
													   @RequestParam(value = "tax_Name",required = false) BigDecimal theTax,
													   @RequestParam(value = "total_Name",required = false) String theTotal,
													   @RequestParam(value = "bal_Name",required = false) String theBalance,
													   @RequestParam(value = "veBill_Name",required = false) Integer theVeBillID,
													   @RequestParam(value = "oper",required = false) String theOper,
													   @RequestParam(value = "coAccountName",required = false) Integer theCoAccountID,
													   @RequestParam(value = "joReleaseDetailsID",required = false) Integer theJoReleaseDetailsID,
													   @RequestParam(value = "vePOID",required = false) Integer theVePOID,
													   @RequestParam(value = "rxMasterID",required = false) Integer theRxMasterID,
													   HttpSession theSession, HttpServletResponse theResponse) throws ParseException, IOException{
		Vebill aVebill = new Vebill();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		try{
			if(theReleaseDate != null && theReleaseDate != ""){
				aVebill.setReceiveDate(DateUtils.parseDate(theReleaseDate, new String[]{"MM/dd/yyyy"}));
			}
			aVebill.setInvoiceNumber(theVendorInvoiceName);
			if(theDueDate != null && theDueDate != ""){
				aVebill.setDueDate(DateUtils.parseDate(theDueDate, new String[]{"MM/dd/yyyy"}));
			}
			aVebill.setUsePostDate(thePostChk);
			if(thePostDateName != null && thePostDateName != ""){
				aVebill.setPostDate(DateUtils.parseDate(thePostDateName, new String[]{"MM/dd/yyyy"}));
			}
			if(theShipDate != null && theShipDate != ""){
				aVebill.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
			}
			aVebill.setVeShipViaId(theShipViaID);
			aVebill.setTrackingNumber(thePRoNumber);
			aVebill.setFreightAmount(theFrieght);
			aVebill.setTaxAmount(theTax);
			aVebill.setJoReleaseDetailId(theJoReleaseDetailsID);
			aVebill.setVeBillId(theVeBillID);
			aVebill.setApaccountId(theCoAccountID);
			aVebill.setRxMasterId(theRxMasterID);
			aVebill.setVePoid(theVePOID);
			aVebill.setTransactionStatus(1);
			if(theOper.equalsIgnoreCase("add")){
				aVebill.setAppliedAmount(new BigDecimal(0));
				aVebill.setBillAmount(new BigDecimal(0));
				aVebill.setTaxAmount(new BigDecimal(0));
				aVebill.setFreightAmount(new BigDecimal(0));
				aVebill.setBillDate(new Date());
				aVebill.setUsePostDate(false);
				aVebill.setPostDate(new Date());
				aVebill.setApplied(false);
				aVebill.setVoid_(false);
				aVebill.setJoReleaseDetailId(theJoReleaseDetailsID);
				aJoReleaseDetail.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
				aJoReleaseDetail.setVendorInvoiceDate(DateUtils.parseDate(theReleaseDate, new String[]{"MM/dd/yyyy"}));
				if(theSubTotal != ""){
					aJoReleaseDetail.setVendorInvoiceAmount(new BigDecimal(theSubTotal));
				}
				itsJobService.addVendorInvoiceDetails(aVebill, aJoReleaseDetail);
			}else if(theOper.equalsIgnoreCase("edit")){
				itsJobService.updateVendorInvoiceDetails(aVebill);
			}
		}catch (JobException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}*/
	
	/*
	 * Modified By: Praveenkumar
	 * date : 2014-09-02
	 * Purpose : Update cuLinkage and cuReceipttable for Payments  
	 *  
	 */
	@RequestMapping(value="/updateCustomerInvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody Cuinvoice updateCustomerInvoiceDetails(
			@RequestParam(value="customerInvoice_customerInvoiceName", required= false) String theCustomerName,
			@RequestParam(value="customerInvoice_invoiceName", required= false) String theInvoiceNameDate,
			@RequestParam(value="customerInvoice_invoiveNumberName", required= false) String theInvoiceNumber,
			@RequestParam(value="prWareHouseSelectName", required=false) Integer thePrWareHouseID,
			@RequestParam(value="shipViaCustomerSelectName", required= false) Integer theShipViaCusotmerID, 
			@RequestParam(value="shipDate", required= false) String theShipDate,
			@RequestParam(value = "customerInvoice_proNumberName",required = false) String theTrackingID,
			@RequestParam(value = "customerInvoice_assignedSalesRepId",required = false) Integer theSalesRepID,
			@RequestParam(value = "customerInvoice_assignedCSRId",required = false) Integer theCSRID,
			@RequestParam(value = "customerInvoice_assignedSalesMgrId",required = false) Integer theSalesMgrID,
			@RequestParam(value = "customerInvoice_assignedEngineerId",required = false) Integer theEngineerID,
			@RequestParam(value = "customerInvoice_assignedPrjMgrId",required = false) Integer theProjectID,
			@RequestParam(value = "customer_DivisionName",required = false) Integer theDivision,
			@RequestParam(value = "customerInvoie_PONoName",required = false) String thePONumber,
			@RequestParam(value = "customerTaxPersent",required = false) Integer theTaxTerritory,
			@RequestParam(value = "customerInvoice_dueDateName",required = false) String theDueDate,
			@RequestParam(value = "customerinvoicePaymentTermsName",required = false) Integer theCuTermsID,
			@RequestParam(value = "customerInvoice_subTotalName",required = false) BigDecimal theSubTotal,
			@RequestParam(value = "customerInvoice_frightname",required = false) BigDecimal theFrieght,
			@RequestParam(value = "customerInvoice_taxName",required = false) BigDecimal theTax,
			@RequestParam(value = "customerInvoice_totalName",required = false) BigDecimal theTotal,
			@RequestParam(value = "customerInvoice_Name",required = false) Integer theCuInvoiceID,
			@RequestParam(value = "customerInvoie_doNotMailID",required = false) Byte theDoNotMailId,
			@RequestParam(value = "customerinvoicePaymentTermsName",required = false) Integer theTermsID,
			@RequestParam(value = "cuInvHiddenId",required = false) Integer cuInvHiddenId,
		    @RequestParam(value = "oper",required = false) String theOper,
		    @RequestParam(value = "taxRate",required = false) BigDecimal taxRate,
		    @RequestParam(value = "cuSOID",required = false) Integer cuSOID,
		    @RequestParam(value = "poNumber",required = false) String poNumber,
		    @RequestParam(value = "InvoiceNo",required = false) String InvoiceNo,
		    @RequestParam(value = "joReleaseDetailsID",required = false) Integer theJoReleaseDetailsID,
		    @RequestParam(value = "releaseType",required = false) Integer releaseType,
		    @RequestParam(value = "customerID",required = false) Integer customerID,
		   // @RequestParam(value = "shipToCustomerAddressID",required = false) Integer shipToCustomerAddressID,
		    @RequestParam(value = "from",required = false) String from,
		    @RequestParam(value = "joReleaseID",required = false) Integer joReleaseID,
		    @RequestParam(value = "transaction",required = false) String transaction,
		    @RequestParam(value = "jobnodescriptionname",required = false) String jobnodescription,
		   // @RequestParam(value = "rxShiptoAddressID",required = false) Integer rxShipToAddressID,
		    //@RequestParam(value = "shipToMode",required = false) Byte shipToMode,
		    @RequestParam(value = "customerInvoiceShipToAddressName",required = false) String customerShipToAddressID,
		    @RequestParam(value = "customerInvoiceShipToAddress1",required = false) String customerShipToAddressID1,
		    @RequestParam(value = "customerInvoiceShipToAddress2",required = false) String customerShipToAddressID2,
		    @RequestParam(value = "customerInvoiceShipToCity",required = false) String customerShipToCity,
		    @RequestParam(value = "customerInvoiceShipToState",required = false) String customerShipToState,
		    @RequestParam(value = "customerInvoiceShipToZip",required = false) String customerShipToZipID,
		    @RequestParam(value = "coFiscalPeriodId",required = false) Integer coFiscalPeriodId,
		    @RequestParam(value = "coFiscalYearId",required = false) Integer coFiscalYearId,
		    @RequestParam(value = "reason",required = false) String reason,
		    @RequestParam(value = "customerInvoice_taxIdcuname",required = false) String taxAmount,
		    @RequestParam(value = "gridData",required = false) String gridData,
		    @RequestParam(value = "delData[]",required = false) ArrayList<String>  delData,
		    @RequestParam(value = "CIrxShiptoid",required = false) Integer CIrxShiptoid,
		    @RequestParam(value = "CIrxShiptomodevalue",required = false) Byte CIrxShiptomodevalue,
		    @RequestParam(value = "CIjoMasterID",required = false) Integer CIjoMasterID,
		    @RequestParam(value = "emailListCU",required = false) Integer  emailListCU,
		    @RequestParam(value = "taxfreight",required = false) Byte  taxfreight,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, BankingException, CompanyException, MessagingException, CustomerException{
		itsLogger.info("Other Address Data:\n"+customerShipToAddressID+"\n"+customerShipToAddressID1+"\n"+customerShipToAddressID2
				+"\n"+customerShipToCity+"\n"+customerShipToState+"\n"+customerShipToState+"\n"+customerShipToZipID+"\n"+theTotal);
		Cuinvoice aCuinvoice = new Cuinvoice();
		Cureceipt aCuReceipt = new Cureceipt();
		CuLinkageDetail aCuLinkageDetail = new CuLinkageDetail();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		Rxaddress theRxaddress = new Rxaddress();
		String taxAmountValue = "0.00";
		Cuinvoice aCuinvoize = null;
		try{
			itsLogger.info("ReleaseType:"+releaseType+"FrieghtAmt:"+theFrieght+" TaxRate:"+taxRate+" TaxAmount:"+taxAmount+" SubTotal:"+theSubTotal+" CostTotal:"+theTotal+" thePrWareHouseID#"+thePrWareHouseID);
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			if(taxAmount!=null && taxAmount.contains("$")){
				taxAmountValue = taxAmount.replace("$", "");
			}else if(taxAmount!=null && !taxAmount.contains("$")){
				taxAmountValue = taxAmount;
			}
			if(taxAmountValue!=null && taxAmountValue.contains(",")){
				if(taxAmountValue.indexOf(",")!=-1) {
					taxAmountValue = taxAmountValue.replaceAll(",","");
					}
				else{
				taxAmountValue = taxAmountValue.replace(",", "");
				}
				}
			
			if(theInvoiceNameDate != null && theInvoiceNameDate != ""){
				aCuinvoice.setInvoiceDate(DateUtils.parseDate(theInvoiceNameDate, new String[]{"MM/dd/yyyy"}));
			}
			//aCuinvoice.setInvoiceNumber(theInvoiceNumber);
			if(theDueDate != null && theDueDate != ""){
				aCuinvoice.setDueDate(DateUtils.parseDate(theDueDate, new String[]{"MM/dd/yyyy"}));
			}
			if(theShipDate != null && theShipDate != ""){
				aCuinvoice.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
			}
			
			aCuinvoice.setPrFromWarehouseId(thePrWareHouseID);
			aCuinvoice.setVeShipViaId(theShipViaCusotmerID);
			aCuinvoice.setTrackingNumber(theTrackingID);
			aCuinvoice.setTaxfreight(taxfreight);
				if(theFrieght!=null)
				aCuinvoice.setFreight(theFrieght);
				else
				aCuinvoice.setFreight(new BigDecimal(0));	
			
				//if(theTax!=null){
				aCuinvoice.setTaxTotal(theTax);
				aCuinvoice.setTaxAmount(new BigDecimal(taxAmountValue));
				/*}
				else{
					aCuinvoice.setTaxTotal(new BigDecimal(0));	
				}*/
					
			if(taxRate!=null && taxRate.compareTo(new BigDecimal(0))!=0){
				aCuinvoice.setTaxRate(taxRate);
			}
			
				
				  if(theTotal!=null){
					aCuinvoice.setInvoiceAmount(theTotal);
					/* CostTotal calculated from the product cost
					 * aCuinvoice.setCostTotal(theTotal);*/
				}
				else
				aCuinvoice.setCostTotal(new BigDecimal(0));
				
				if(theSubTotal!=null)
				aCuinvoice.setSubtotal(theSubTotal);
				else
				aCuinvoice.setSubtotal(new BigDecimal(0));
				
				if(theTotal.signum() == -1)
				{
					aCuinvoice.setCreditmemo(0); 
					aCuinvoice.setIscredit(1);
					aCuinvoice.setMemoStatus(0);
				}
				else
				{
					aCuinvoice.setCreditmemo(0); 
					aCuinvoice.setIscredit(0);
					aCuinvoice.setMemoStatus(0);
				}
			
			aCuinvoice.setCustomerPonumber(thePONumber);
			aCuinvoice.setCuAssignmentId0(theSalesRepID);
			aCuinvoice.setCuAssignmentId1(theCSRID);
			aCuinvoice.setCuAssignmentId2(theSalesMgrID);
			aCuinvoice.setCuAssignmentId3(theEngineerID);
			aCuinvoice.setCuAssignmentId4(theProjectID);
			aCuinvoice.setCoTaxTerritoryId(theTaxTerritory);
			aCuinvoice.setCoDivisionId(theDivision);
			aCuinvoice.setCuTermsId(theTermsID);
			aCuinvoice.setTransactionStatus(1);
			aCuinvoice.setRxCustomerId(customerID);
			aCuinvoice.setRxBillToId(customerID);
			aCuinvoice.setShipToMode(CIrxShiptomodevalue);
			aCuinvoice.setRxContactId(emailListCU);
			
			if(CIrxShiptomodevalue==0){
				//US
				aCuinvoice.setPrToWarehouseId(CIrxShiptoid);
			}else if(CIrxShiptomodevalue==1){
				//Customer
				aCuinvoice.setRxShipToId(CIrxShiptoid);
			}else if(CIrxShiptomodevalue==2){
				//jobSite
				aCuinvoice.setRxShipToId(CIjoMasterID);
			}else{
				//Other
				Integer RxotheraddressID=CIrxShiptoid;
				if(RxotheraddressID==null ||  RxotheraddressID==0){
					//Insert RxAddress
					theRxaddress = new Rxaddress();
					theRxaddress.setName(customerShipToAddressID);
					theRxaddress.setAddress1(customerShipToAddressID1);
					theRxaddress.setAddress2(customerShipToAddressID2);
					theRxaddress.setCity(customerShipToCity);
					theRxaddress.setState(customerShipToState);
					theRxaddress.setZip(customerShipToZipID);
					theRxaddress.setRxAddressId(null);
					theRxaddress.setIsBillTo(false);
					theRxaddress.setInActive(false);
					theRxaddress.setIsMailing(false);
					theRxaddress.setIsStreet(false);
					theRxaddress.setIsShipTo(true);
					RxotheraddressID= itsJobService.insertRxAddress(theRxaddress, "");
					
				}else{
					//Update RxAddress
					theRxaddress = new Rxaddress();
					theRxaddress.setName(customerShipToAddressID);
					theRxaddress.setAddress1(customerShipToAddressID1);
					theRxaddress.setAddress2(customerShipToAddressID2);
					theRxaddress.setCity(customerShipToCity);
					theRxaddress.setState(customerShipToState);
					theRxaddress.setZip(customerShipToZipID);
					theRxaddress.setRxAddressId(RxotheraddressID);
					theRxaddress.setIsBillTo(false);
					theRxaddress.setInActive(false);
					theRxaddress.setIsMailing(false);
					theRxaddress.setIsStreet(false);
					theRxaddress.setIsShipTo(true);
					itsCuMasterService.updateRxaddress(theRxaddress);
				}
				
				aCuinvoice.setRxShipToAddressId(RxotheraddressID);
			}
			
			/**Date :18-11-2015 Commented by:Leo Purpose: shiptoAddress not updating - reason customer warehse id column conflict*/
			/*if(releaseType!=1 && releaseType!=4){
				aCuinvoice.setPrToWarehouseId(CIrxShiptoid);
			}*/
			
			aCuinvoice.setJobnoDescription(jobnodescription);
			aCuinvoice.setReason(reason);
			
			if(aCuinvoice.getCuInvoiceId()!=null && aCuinvoice.getCuInvoiceId() > 0){
				aCuinvoice.setcIopenStatus(false);
				System.out.println("Testing");
			}else{
				aCuinvoice.setcIopenStatus(true);
			}
//			aCuinvoice.setInvoiceAmount(invoiceAmount)
			if(theCuInvoiceID!=null){
				aCuinvoice.setCuInvoiceId(theCuInvoiceID);
			} else {
				aCuinvoice.setCuInvoiceId(cuInvHiddenId);
			}
			//aCuinvoice.setInvoiceNumber(InvoiceNo);
			if(theJoReleaseDetailsID!=null){
			aCuinvoice.setJoReleaseDetailId(theJoReleaseDetailsID);
			}
			aCuinvoice.setTaxRate(taxRate);
			aCuinvoice.setCuSoid(cuSOID);
			if(theDoNotMailId != null){
				aCuinvoice.setDoNotMail(theDoNotMailId);
			}
			itsLogger.info("TheInvoiceNumber:"+theInvoiceNumber);
			aCuinvoize = itsJobService.getSingleCuInvoiceObj(aCuinvoice.getCuInvoiceId()==null?0:aCuinvoice.getCuInvoiceId());
			if(aCuinvoize == null){
				aCuinvoize = new Cuinvoice();
			}
			if(theOper.equalsIgnoreCase("add")){
				aCuinvoice.setCreatedById(aUserBean.getUserId());
				String invNumbr =theInvoiceNumber;
				
				//String invNumbr = itsJobService.getLastestInvoiceNumber(theInvoiceNumber);
				
			//	System.out.println("test "+test );
				/*Date now = new Date();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String createdDate = df.format(now);
				if((createdDate != null && createdDate != "")){
					aCuinvoice.setCreatedOn(DateUtils.parseDate(createdDate, new String[]{"MM/dd/yyyy"}));
				}*/	
				aCuinvoice.setCreatedOn(new Date());
				aCuinvoice.setAppliedAmount(new BigDecimal(0));
				
				BigDecimal aReleaseAllocatedAmt = itsJobService.getReleaseAllocatedAmt(joReleaseID);
				
			//	aCuinvoice.setInvoiceAmount(theTotal);
				byte b = 0;
				aCuinvoice.setDoNotMail(b);
				aCuinvoice.setApplied(false);
				aJoReleaseDetail.setCustomerInvoiceDate(DateUtils.parseDate(theInvoiceNameDate, new String[]{"MM/dd/yyyy"}));
				aJoReleaseDetail.setCustomerInvoiceAmount(theSubTotal);
				aJoReleaseDetail.setJoReleaseId(joReleaseID);
				aCuinvoice.setInvoiceNumber(invNumbr);
				
				
				
				
				itsLogger.info("===invNumbr=="+invNumbr);
				itsLogger.info("JoReleaseDetailID::"+theJoReleaseDetailsID +" ## "+aCuinvoice.getJoReleaseDetailId());
				/*if(releaseType == 1)
				{
					if(shipToMode == 1)
					{
						if(rxShipToAddressID!=null)
						{
							aCuinvoice.setRxShipToId(rxShipToAddressID);
							aCuinvoice.setRxShipToAddressId(rxShipToAddressID);
						}
					}
				}*/
				aCuinvoice = itsJobService.addCusotmerInvoiceDetails(aCuinvoice, aJoReleaseDetail,from);
				System.out.println("aCuinvoice.getCuInvoiceId() :: "+aCuinvoice.getCuInvoiceId());
				
				if(releaseType == 2){
					itsJobService.insertCustomerInvoiceLines(cuSOID,aCuinvoice.getCuInvoiceId());
				} else if (releaseType == 1) {
					itsJobService.insertCustomerInvoiceLinesFromPO(cuSOID, aCuinvoice.getCuInvoiceId(),releaseType,aReleaseAllocatedAmt);
				}
				itsJobService.updateTaxableandNonTaxableforCuInvoice(aCuinvoice);
				if(transaction!=null && transaction.equalsIgnoreCase("close")){
					itsGltransactionService.receiveCustomerInvoiceBill(aCuinvoice,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());
					if(aCuinvoice.getInvoiceAmount().compareTo(new BigDecimal("0.0000"))!=0){
						aCuinvoice.setUserName(aUserBean.getFullName());
						itsJobService.saveCustomerInvoiceLog(new Cuinvoice(),aCuinvoice,"CI-Created",1,coFiscalPeriodId,coFiscalYearId);
					}					
				}
				
			}else if(theOper.equalsIgnoreCase("edit")){
				Cuinvoice theCuinvoice = new Cuinvoice();
				Integer userID=((UserBean)session.getAttribute(SessionConstants.USER)).getUserId();
				String username=((UserBean)session.getAttribute(SessionConstants.USER)).getUserName();
				itsLogger.info("Edit operation execution");
				aCuinvoice.setChangedById(aUserBean.getUserId());
				aCuinvoice.setChangedOn(new Date());
				aCuinvoice.setUserName(aUserBean.getFullName());
				String invdate="";
				if(theInvoiceNameDate != null && theInvoiceNameDate != ""){
					aCuinvoice.setInvoiceDate(DateUtils.parseDate(theInvoiceNameDate, new String[]{"MM/dd/yyyy"}));
					
					invdate=JobUtil.convertinto12hourformat(theInvoiceNameDate,"MM/dd/yyyy","yyyy-MM-dd");
				}
				System.out.println("delData"+delData);
				boolean lineitemupdateornot=false;
				if(delData!=null){
					for(String detailID:delData){
						System.out.println("delData[i]"+detailID);
						Integer cuInvoiceDetailID=JobUtil.ConvertintoInteger(detailID);
						lineitemupdateornot=manpulateCuInvoiceReleaseLineItem(aCuinvoice.getCuInvoiceId(),null,null,null,"del",null,null,
								null,null,null,cuInvoiceDetailID,null,null,releaseType+"",userID,username);

					}
				}
				
				JsonParser parser = new JsonParser();
				if ( gridData!=null) {
					System.out.println("gridData"+gridData);
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						String desc=obj.get("description").getAsString();
						String itemCode=obj.get("itemCode").getAsString();
						String note=obj.get("note").getAsString();
						String Oper="add";
						Integer prMasterID=obj.get("prMasterId").getAsInt();
						BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
						BigDecimal quantityOrder=obj.get("quantityBilled").getAsBigDecimal();
						String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
						unitCost_String=unitCost_String.replaceAll(",", "");
						BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
						Integer cuInvoiceDetailId=null;
						if(obj.get("cuInvoiceDetailId")!=null && obj.get("cuInvoiceDetailId").getAsString()!=""&& obj.get("cuInvoiceDetailId").getAsString().length()>0){
							cuInvoiceDetailId=obj.get("cuInvoiceDetailId").getAsInt();
						}
						String Taxable=(obj.get("taxable")!=null && obj.get("taxable").getAsInt()==1)?"Yes":"NO";
						Double taxRatee=Double.parseDouble(""+aCuinvoice.getTaxRate());
						if(cuInvoiceDetailId!=null){
							Oper="edit";
						}
						boolean val=manpulateCuInvoiceReleaseLineItem(aCuinvoice.getCuInvoiceId(),desc,itemCode,note,Oper,prMasterID,priceMultiplier,
								quantityOrder,Taxable,unitCost,cuInvoiceDetailId,taxRatee,aCuinvoice.getFreight(),releaseType+"",userID,username);
					if(val){
						lineitemupdateornot=true;
					}
					}
				}
//				public Boolean manpulateCuInvoiceReleaseLineItem(
//						 Integer cuInvoiceID,String desc,String itemCode,String note,String Oper,Integer prMasterID,
//						BigDecimal priceMultiplies,BigDecimal quantityOrder,String Taxable,BigDecimal unitCost,Integer cuInvoiceDetailId,
//						Double taxRate,BigDecimal freight,String releasetype)
				
				
				Cuinvoice bCuinvoice = new Cuinvoice();
				bCuinvoice = itsJobService.updateCusotmerInvoiceDetails(aCuinvoice);
				itsJobService.updateCuInvoiceSubTotal(aCuinvoice.getCuInvoiceId(),aCuinvoice.getSubtotal());
				itsJobService.updateTaxableandNonTaxableforCuInvoice(aCuinvoice);
				//if(transaction!=null && transaction.equalsIgnoreCase("close")){
				System.out.println(aCuinvoize.getInvoiceDate()+"====="+invdate);
				System.out.println("=="+aCuinvoize.getInvoiceDate().toString().indexOf(invdate));
				Integer dateval=(aCuinvoize.getInvoiceDate()==null?"":aCuinvoize.getInvoiceDate().toString()).indexOf(invdate);
					if(aCuinvoize.getInvoiceAmount().compareTo(aCuinvoice.getInvoiceAmount())!=0  ||dateval!=0){ 
						itsLogger.info("Transaction Rollback. .. ..."+aCuinvoize.getInvoiceAmount()+"#@#"+aCuinvoice.getInvoiceAmount());
						Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CI");
						
						GlRollback glRollback = new GlRollback();
						glRollback.setVeBillID(aCuinvoice.getCuInvoiceId());
						glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
						glRollback.setPeriodID(coFiscalPeriodId);
						glRollback.setYearID(coFiscalYearId);
						glRollback.setTransactionDate(aCuinvoice.getInvoiceDate());
						
						itsGltransactionService.rollBackGlTransaction(glRollback);
						itsGltransactionService.receiveCustomerInvoiceBill(aCuinvoice,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());
						
						if(delData!=null){
							//theCuinvoice = itsJobService.getCustomerInvoiceDetails(aCuinvoice.getCuInvoiceId());
							bCuinvoice.setUserName(aUserBean.getFullName());
							bCuinvoice.setReason(reason);
							
							if(aCuinvoize.getInvoiceAmount().compareTo(bCuinvoice.getInvoiceAmount())!=0)
							itsJobService.saveCustomerInvoiceLog(aCuinvoize,bCuinvoice,"CI-Line Item(s) Deleted",1,coFiscalPeriodId,coFiscalYearId);
							else
							itsJobService.saveCustomerInvoiceLog(aCuinvoize,bCuinvoice,"CI-Line Item(s) Deleted",0,coFiscalPeriodId,coFiscalYearId);
								
						}else{
							bCuinvoice.setUserName(aUserBean.getFullName());
							bCuinvoice.setReason(reason);
							if(aCuinvoize.getSubtotal().compareTo(bCuinvoice.getSubtotal())!=0 || aCuinvoize.getFreight().compareTo(bCuinvoice.getFreight())!=0 ||
									aCuinvoize.getTaxAmount().compareTo(bCuinvoice.getTaxAmount())!=0)
							itsJobService.saveCustomerInvoiceLog(aCuinvoize,bCuinvoice,"CI-Edited",1,coFiscalPeriodId,coFiscalYearId);
							else
							itsJobService.saveCustomerInvoiceLog(aCuinvoize,bCuinvoice,"CI-Edited",0,coFiscalPeriodId,coFiscalYearId);
						}
					}else{
						if(delData!=null){
							//theCuinvoice = itsJobService.getCustomerInvoiceDetails(aCuinvoice.getCuInvoiceId());
							bCuinvoice.setUserName(aUserBean.getFullName());
							bCuinvoice.setReason(reason);
							itsJobService.saveCustomerInvoiceLog(aCuinvoize,bCuinvoice,"CI-Line Item(s) Deleted",0,coFiscalPeriodId,coFiscalYearId);
						}else{
							bCuinvoice.setUserName(aUserBean.getFullName());
							bCuinvoice.setReason(reason);
							itsJobService.saveCustomerInvoiceLog(aCuinvoize,bCuinvoice,"CI-Edited",0,coFiscalPeriodId,coFiscalYearId);
						}
					}
					//}
				
			}
			itsLogger.info("first gl transaction"+transaction+"ReleaseID"+aCuinvoice.getJoReleaseDetailId());
			
			/*if(transaction!=null && transaction.equalsIgnoreCase("close")){
				if(aCuinvoize!=null && (aCuinvoize.getInvoiceAmount().compareTo(aCuinvoice.getInvoiceAmount())!=0)){
				itsLogger.info("Rollback Insertion Transaction. .. ..."+aCuinvoize.getInvoiceAmount()+"#@#"+aCuinvoice.getInvoiceAmount());
				itsGltransactionService.receiveCustomerInvoiceBill(aCuinvoice,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());	
				}else{
						itsLogger.info("First GL Transaction");
						itsGltransactionService.receiveCustomerInvoiceBill(aCuinvoice,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());	
					}
			}*/
		}catch (JobException e) {
			sendTransactionException("<b>theCuInvoiceID:</b>"+theCuInvoiceID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCuinvoice;
	}
	
	@RequestMapping(value="/getAssigneeDetails", method = RequestMethod.POST)
	public @ResponseBody EmployeeAssign getAssigneeDetails(@RequestParam(value="assignee0", required = false) Integer theAssign0, 
															@RequestParam(value="assignee1", required = false) Integer theAssign1,
															@RequestParam(value="assignee2", required = false) Integer theAssign2,
															@RequestParam(value="assignee3", required = false) Integer theAssign3,
															@RequestParam(value="assignee4", required = false) Integer theAssign4,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
 		EmployeeAssign aEmployeeAssign = new EmployeeAssign();
		String aCuAssign0 = "";
		String aCuAssign1 = "";
		String aCuAssign2 = "";
		String aCuAssign3 = "";
		String aCuAssign4 = "";
		try{
			if(theAssign0 != null){
				aCuAssign0 = itsSalesServices.getAssignedEmployee(theAssign0);
			}if(theAssign1 != null){
				aCuAssign1 = itsSalesServices.getAssignedEmployee(theAssign1);
			}if(theAssign2 != null){
				aCuAssign2 = itsSalesServices.getAssignedEmployee(theAssign2);
			}if(theAssign3 != null){
				aCuAssign3 = itsSalesServices.getAssignedEmployee(theAssign3);
			}if(theAssign4 != null){
				aCuAssign4 = itsSalesServices.getAssignedEmployee(theAssign4);
			}
			aEmployeeAssign.setCuAssignmentId0(aCuAssign0);
			aEmployeeAssign.setCuAssignmentId1(aCuAssign1);
			aEmployeeAssign.setCuAssignmentId2(aCuAssign2);
			aEmployeeAssign.setCuAssignmentId3(aCuAssign3);
			aEmployeeAssign.setCuAssignmentId4(aCuAssign4);
		}catch (Exception e) {
			sendTransactionException("<b>MethodName:</b>getAssigneeDetails","JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
		}
		return aEmployeeAssign;	
	}
	
	@RequestMapping(value="/manupulateShipDetails", method = RequestMethod.POST)
	public @ResponseBody Vepo manupulateShipDetails(@RequestParam(value="ReleasesName", required= false) Date theReleaseDate,
													   @RequestParam(value="joReleaseName", required= false) Integer theJoreleaseId,
													   @RequestParam(value="joReleaseDetailName", required= false) Integer theJoreleaseDetailId,
													   @RequestParam(value="vePoName", required= false) Integer thevePoId,
													   @RequestParam(value="ReleasesTypeName", required= false) Integer theReleaseType, 
													   @RequestParam(value="ManufacturerId", required = false) Integer theManuId,
													   @RequestParam(value="ReleasesManuName", required=false) String theManuName,
													   @RequestParam(value="NoteName", required=false) String theNote,
													   @RequestParam(value="AllocatedName", required= false) BigDecimal theAllocatedName, 
													   @RequestParam(value="POAmountName", required= false) BigDecimal thePOAmount, 
													   @RequestParam(value="ReleasesInvoiceName", required= false) BigDecimal theReleaseInvoiceName, 
													   @RequestParam(value="joMasterID", required= false) Integer theJoMasterID, 
													   @RequestParam(value="oper", required = false) String theOperation,
													   @RequestParam(value="jobNumber", required = false) String theJobNumber,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		JoRelease aJoRelease = new JoRelease();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		Vepo aVepo = new Vepo();
		Vepo theVepo = new Vepo();
		try{
			aJoRelease.setReleaseDate(theReleaseDate);
			if(theReleaseType != -1){
				theReleaseType =1;
			}
			aJoRelease.setReleaseType(theReleaseType);
			aJoRelease.setEstimatedBilling(theAllocatedName);
			aJoRelease.setJoMasterId(theJoMasterID);
			aJoRelease.setReleaseNote(theNote);
			aJoReleaseDetail.setVendorInvoiceAmount(thePOAmount);
			aJoReleaseDetail.setCustomerInvoiceAmount(theReleaseInvoiceName);
			aJoReleaseDetail.setJoMasterId(theJoMasterID);
			aVepo.setVeFactoryId(theManuId);
			if(theOperation.equals("add")){
				String aNewPONumber =itsJobService.getNewPONumber(theJobNumber, thevePoId);
				aVepo.setPonumber(aNewPONumber);
				if(theManuName.equals("Submittal")){
					Integer aVeFactoryId=itsJobService.getVeFactoryId(theManuId);
					aVepo.setVeFactoryId(aVeFactoryId);
//					theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber);
				}else{
//					theVepo = itsJobService.addRelease(aJoRelease, aJoReleaseDetail, aVepo, theJobNumber);
				}
			}else if(theOperation.equals("edit")){
				aJoRelease.setJoReleaseId(theJoreleaseId);
				aJoReleaseDetail.setJoReleaseDetailId(theJoreleaseDetailId);
				aJoReleaseDetail.setJoReleaseId(theJoreleaseId);
				aVepo.setJoReleaseId(theJoreleaseId);
				aVepo.setVePoid(thevePoId);
//				itsJobService.editRelease(aJoRelease, aJoReleaseDetail, aVepo);
			}else if(theOperation.equals("del")){
				aJoRelease.setJoReleaseId(theJoreleaseId);
				aJoReleaseDetail.setJoReleaseDetailId(theJoreleaseDetailId);
				aJoReleaseDetail.setJoReleaseId(theJoreleaseId);
				aVepo.setJoReleaseId(theJoreleaseId);
				aVepo.setVePoid(thevePoId);
//				itsJobService.deleteRelease(aJoRelease,aJoReleaseDetail,aVepo);
			}
		}catch (JobException e) {
			sendTransactionException("<b>theJoreleaseId:</b>"+theJoreleaseId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return theVepo;
	}
	
	@RequestMapping(value="/deleteShipDetails", method = RequestMethod.GET)
	public @ResponseBody Integer deleteShipDetails(@RequestParam(value="shipDetailsID", required= false) Integer theShipDetailsId,
																						   @RequestParam(value="releaseID", required= false) Integer theReleaseId,
																						   @RequestParam(value="billID", required= false) Integer theBillID,
																						   @RequestParam(value="cuInvoiceID", required= false) Integer theCuInvoiceID,
																						   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException {
		Veshipvia aVeshipvia = new Veshipvia();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		Vebill aVebill = new Vebill();
		Cuinvoice aCuinvoice = new Cuinvoice();
		try{
			aVeshipvia.setVeShipViaId(theShipDetailsId);
			aJoReleaseDetail.setJoReleaseDetailId(theReleaseId);
			aJoReleaseDetail.setCustomerInvoiceDate(null);
			aJoReleaseDetail.setVendorInvoiceDate(null);
			aJoReleaseDetail.setCustomerInvoiceAmount(null);
			aJoReleaseDetail.setVendorInvoiceAmount(null);
			aVebill.setVeBillId(theBillID);
			aVebill.setAppliedAmount(new BigDecimal(0));
			aVebill.setBillAmount(new BigDecimal(0));
			aVebill.setTaxAmount(new BigDecimal(0));
			aVebill.setFreightAmount(new BigDecimal(0));
			aVebill.setBillDate(new Date());
			aVebill.setUsePostDate(false);
			aVebill.setPostDate(new Date());
			aVebill.setApplied(false);
			aVebill.setVoid_(false);
			aCuinvoice.setAppliedAmount(new BigDecimal(0));
			aCuinvoice.setTaxAmount(new BigDecimal(0));
			aCuinvoice.setInvoiceAmount(new BigDecimal(0));
			byte b = 0;
			aCuinvoice.setDoNotMail(b);
			aCuinvoice.setApplied(false);
			aCuinvoice.setJoReleaseDetailId(theReleaseId);
			aCuinvoice.setVeShipViaId(theShipDetailsId);
			aCuinvoice.setCuInvoiceId(theCuInvoiceID);
			itsJobService.deleteShipViaDetails(aVeshipvia, aJoReleaseDetail, aVebill, aCuinvoice);
		}catch (JobException e) {
			sendTransactionException("<b>theReleaseId,theBillID,theCuInvoiceID:</b>"+theReleaseId+","+theBillID+","+theCuInvoiceID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}
	
	@RequestMapping(value="/validatefirstrelease", method = RequestMethod.GET)
	public @ResponseBody Boolean validatefirstrelease(@RequestParam(value="joMasterID", required= true) Integer joMasterID, 
														@RequestParam(value="jobNumber", required= false) String jobNumber, 
														HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		Boolean aValidForRelease = false;
		try {
			Jomaster aJoMaster = itsJobService.getSingleJobDetails(jobNumber,joMasterID);
			if (aJoMaster.getCreditStatus() != null && aJoMaster.getCreditStatus() == 1) {
				System.out.println("Credit Status is approved.....");
				aValidForRelease = true;
			}
		} catch (JobException e) {
			sendTransactionException("<b>joMasterID:</b>"+joMasterID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aValidForRelease;
	}
	
	@RequestMapping(value="/saveCustomerPONumber", method = RequestMethod.GET)
	public @ResponseBody Boolean saveCustomerPONumber(@RequestParam(value="customerPONumber", required= true) String theCustomerPONumber, 
																											@RequestParam(value="joMasterID", required= true) Integer theJoMasterID,
																											@RequestParam(value="releaseDetail", required= true) Integer theReleaseDetail,
																											HttpSession session,HttpServletRequest therequest,
																											HttpServletResponse theResponse) throws IOException, MessagingException{
		Boolean aCustomerPONumber = false;
		try {
			aCustomerPONumber = itsJobService.saveCustomerPONumber(theCustomerPONumber, theJoMasterID, theReleaseDetail); 
		} catch (JobException e) {
			sendTransactionException("<b>joMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCustomerPONumber;
	}
	
	@RequestMapping(value="/savepolineitemssubtotal", method = RequestMethod.POST)
	public @ResponseBody Boolean savePOLineitemsSubtotal(@RequestParam(value="aPOLineItemsSubtotal", required= true) BigDecimal aPOLineItemsSubtotal,
															@RequestParam(value="aVePOID", required= true) Integer aVePOID,
															HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException{
		Boolean isSubtotlaUpdated = false;
		try {
			isSubtotlaUpdated = itsJobService.savePOLineitemsSubtotal(aVePOID, aPOLineItemsSubtotal);
		} catch (JobException e) {
			sendTransactionException("<b>aVePOID:</b>"+aVePOID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return isSubtotlaUpdated;
	}
	
	@RequestMapping(value="/getAddressAddressInformation", method = RequestMethod.GET)
	public @ResponseBody Rxaddress getAddressAddressInformation(@RequestParam(value="rxAddressID", required= true) Integer theAddressID,
															HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		Rxaddress aRxaddress = new Rxaddress();
		try {
			if(theAddressID!=null)
			aRxaddress = itsJobService.getRxAddressByRxAddressID(theAddressID);
		} catch (JobException e) {
			sendTransactionException("<b>theAddressID:</b>"+theAddressID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxaddress;
	}
	
	@RequestMapping(value="/SavePOlinetextInfo", method = RequestMethod.POST)
	public @ResponseBody Integer insertLineInfo(@RequestParam(value="lineItem[]", required= false) ArrayList<?> thePOLineItemDetails, 
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		try{
			Vepodetail aVepodetail = new Vepodetail();
			if(thePOLineItemDetails.get(0) != "" && thePOLineItemDetails.get(0) != null){
				String aInLine = thePOLineItemDetails.get(0).toString();
				//String aInLineReplace = aInLine.replaceAll("'", "&And");
				aVepodetail.setNote(aInLine);
			}
			if(thePOLineItemDetails.get(1) != "" && thePOLineItemDetails.get(1) != null){
				String aVePODetailID = thePOLineItemDetails.get(1).toString();
				aVepodetail.setVePodetailId(Integer.valueOf(aVePODetailID));
			}
			itsJobService.updatePOLineInfo(aVepodetail);
		}catch (JobException e) {
			sendTransactionException("<b>MethodName:</b>SavePOlinetextInfo","JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value="/saveChangeOrderDetails", method = RequestMethod.POST)
	public @ResponseBody Jochange saveChangeOrderDetails(@RequestParam(value="changeOrderDetails[]", required= false) ArrayList<?> theChangeOrderDetails, HttpSession session,
			HttpServletRequest therequest,HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Jochange aJochange = new Jochange();
		Jochange aJochangeReturnValue = null;
		itsLogger.info("theChangeOrderDetails::"+theChangeOrderDetails);
		try{
			if(theChangeOrderDetails.get(0) != "" && theChangeOrderDetails.get(0) != null){
				if(!String.valueOf(theChangeOrderDetails.get(0)).contains("undefined")){
				Date aDate = new Date((String) theChangeOrderDetails.get(0));
				aJochange.setChangeDate(aDate);
			}
			}
			if(theChangeOrderDetails.get(1) != "" && theChangeOrderDetails.get(1) != null){
				aJochange.setCustomerPonumber( (String) theChangeOrderDetails.get(1));
			}
			if(theChangeOrderDetails.get(2) != "" && theChangeOrderDetails.get(2) != null){
				aJochange.setChangeByName((String) theChangeOrderDetails.get(2));
			}
			if(theChangeOrderDetails.get(3) != "" && theChangeOrderDetails.get(3) != null){
				aJochange.setChangeReason((String) theChangeOrderDetails.get(3));
			}
			if(theChangeOrderDetails.get(4) != "" && theChangeOrderDetails.get(4) != null){
				BigDecimal aAmount = new BigDecimal((String) theChangeOrderDetails.get(4));
				aJochange.setChangeAmount(aAmount);
			}
			if(theChangeOrderDetails.get(5) != "" && theChangeOrderDetails.get(5) != null){
				String aJoMasterID = (String) theChangeOrderDetails.get(5);
				aJochange.setJoMasterId(Integer.valueOf(aJoMasterID));
			}
			if(theChangeOrderDetails.get(6) != "" && theChangeOrderDetails.get(6) != null){
				String aJobChangeID = (String) theChangeOrderDetails.get(6);
				aJochange.setJoChangeId(Integer.valueOf(aJobChangeID));
			}
			if(theChangeOrderDetails.get(7) != "" && theChangeOrderDetails.get(7) != null){
				String aChangeByID = (String) theChangeOrderDetails.get(7);
				aJochange.setChangeById(Integer.valueOf(aChangeByID));
			}
			if(theChangeOrderDetails.get(8) != "" && theChangeOrderDetails.get(8) != null){
				BigDecimal aCost = new BigDecimal((String) theChangeOrderDetails.get(8));
				aJochange.setChangeCost(aCost);
			}
			if(theChangeOrderDetails.get(9) != "" && theChangeOrderDetails.get(9) != null){
				aJochange.setOper((String) theChangeOrderDetails.get(9));
			}
			aJochangeReturnValue = itsJobService.saveChangeOrderDetails(aJochange);
		}catch (JobException e) {
			sendTransactionException("<b>MethodName:</b>SavePOlinetextInfo","JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJochangeReturnValue;
	}
	
	@RequestMapping(value="/getCustomerPO", method = RequestMethod.POST)
	public @ResponseBody List<JoCustPO> getJoCustPODetail(@RequestParam(value="joMasterId", required= true) Integer aJoMasterID,
															HttpServletResponse theResponse) throws IOException{
		List<JoCustPO> aCustPOList = null;
		try {
			aCustPOList = itsJobService.getJoCustPODetail(aJoMasterID);
		} catch (JobException e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCustPOList;
	}
	
	@RequestMapping(value="/getjomasterbyjoreleaseid", method = RequestMethod.GET)
	public @ResponseBody Jomaster getJomasterbyjoreleaseid(@RequestParam(value="joReleaseID", required= true) Integer aJoReleaseID,
															HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException{
		Jomaster aJoMaster = null;
		try {
			if(aJoReleaseID != null){
				aJoMaster = itsJobService.getJoMasterByJoReleaseID(aJoReleaseID);
			}
		} catch (JobException e) {
			sendTransactionException("<b>aJoReleaseID:</b>"+aJoReleaseID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoMaster;
	}
	@RequestMapping(value="/getPOGeneralDetails", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getvepoObj(@RequestParam(value="vepoID", required= true) Integer vepoID,
															HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException, EmployeeException{
		Map<String, Object> map = new HashMap<String, Object>();
		Vepo aVepo = itsJobService.getVepo(vepoID);
		Sysvariable stmtgroupbyjob= itsEmployeeService.getSysVariableDetails("ReqInvWithPO");
		map.put("vepo", aVepo);
		try {
			map.put("shipViaList", (List<Veshipvia>)itsJobService.getVeShipVia());
			map.put("veInvnoandatory",stmtgroupbyjob.getValueLong());
		} catch (JobException e) {
			sendTransactionException("<b>vepoID:</b>"+vepoID,"JOB",e,session,therequest);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value="/createCustomerInvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody Cuinvoice createCustomerInvoiceDetails(@RequestParam(value="customerInvoice_customerInvoiceName", required= false) String theCustomerName,
																@RequestParam(value="customerInvoice_invoiceName", required= false) String theInvoiceNameDate,
																@RequestParam(value="customerInvoice_invoiveNumberName", required= false) String theInvoiceNumber,
																@RequestParam(value="prWareHouseSelectName", required=false) Integer thePrWareHouseID,
																@RequestParam(value="shipViaCustomerSelectName", required= false) Integer theShipViaCusotmerID, 
																@RequestParam(value="shipDate", required= false) String theShipDate,
																@RequestParam(value = "customerInvoice_proNumberName",required = false) String theTrackingID,
																@RequestParam(value = "customerInvoice_assignedSalesRepId",required = false) Integer theSalesRepID,
																@RequestParam(value = "customerInvoice_assignedCSRId",required = false) Integer theCSRID,
																@RequestParam(value = "customerInvoice_assignedSalesMgrId",required = false) Integer theSalesMgrID,
																@RequestParam(value = "customerInvoice_assignedEngineerId",required = false) Integer theEngineerID,
																@RequestParam(value = "customerInvoice_assignedPrjMgrId",required = false) Integer theProjectID,
																@RequestParam(value = "customer_DivisionName",required = false) Integer theDivision,
																@RequestParam(value = "customerInvoie_PONoName",required = false) String thePONumber,
																@RequestParam(value = "customerTaxPersent",required = false) Integer theTaxTerritory,
																@RequestParam(value = "customerInvoice_dueDateName",required = false) String theDueDate,
																@RequestParam(value = "customerinvoicePaymentTermsName",required = false) Integer theCuTermsID,
																@RequestParam(value = "customerInvoice_subTotalName",required = false) BigDecimal theSubTotal,
																@RequestParam(value = "customerInvoice_frightname",required = false) BigDecimal theFrieght,
																@RequestParam(value = "customerInvoice_taxName",required = false) BigDecimal theTax,
																@RequestParam(value = "customerInvoice_totalName",required = false) BigDecimal theTotal,
																@RequestParam(value = "customerInvoice_Name",required = false) Integer theCuInvoiceID,
																@RequestParam(value = "customerInvoie_doNotMailName",required = false) String theDoNotMailId,
																@RequestParam(value = "customerinvoicePaymentTermsName",required = false) Integer theTermsID,
																@RequestParam(value = "cuInvHiddenId",required = false) Integer cuInvHiddenId,
															    @RequestParam(value = "oper",required = false) String theOper,
															    @RequestParam(value = "taxRate",required = false) BigDecimal taxRate,
															    @RequestParam(value = "cuSOID",required = false) Integer cuSOID,
															    @RequestParam(value = "poNumber",required = false) String poNumber,
															    @RequestParam(value = "InvoiceNo",required = false) String InvoiceNo,
															    @RequestParam(value = "joReleaseDetailsID",required = false) Integer theJoReleaseDetailsID,
															    @RequestParam(value = "releaseType",required = false) Integer releaseType,
															    @RequestParam(value = "customerID",required = false) Integer customerID,
															   // @RequestParam(value = "shipToMode",required = false) Integer theshipToMode,
															   // @RequestParam(value = "rxShipToID",required = false) Integer therxShipToID,
															    @RequestParam(value = "rxShipToOtherAddressID",required = false) Integer therxShipToOtherAddressID,
															    @RequestParam(value = "thecustomerShipToAddressID",required = false) String customerShipToAddressID,
															    @RequestParam(value = "thecustomerShipToAddressID1",required = false) String customerShipToAddressID1,
															    @RequestParam(value = "thecustomerShipToAddressID2",required = false) String customerShipToAddressID2,
															    @RequestParam(value = "thecustomerShipToCity",required = false) String customerShipToCity,
															    @RequestParam(value = "thecustomerShipToState",required = false) String customerShipToState,
															    @RequestParam(value = "thecustomerShipToZipID",required = false) String customerShipToZipID,
															    @RequestParam(value = "transaction",required = false) String transaction,
															    @RequestParam(value = "rxContactID",required = false) Integer rxContactID,
															    @RequestParam(value = "jobnodescriptionname",required = false) String jobnodescription,
															    @RequestParam(value = "CIrxShiptoid",required = false) Integer CIrxShiptoid,
															    @RequestParam(value = "CIrxShiptomodevalue",required = false) Byte CIrxShiptomodevalue,
															   // @RequestParam(value = "rxShiptoAddressID",required = false) Integer rxShiptoAddressID,
															    @RequestParam(value = "coFiscalPeriodId",required = false) Integer coFiscalPeriodId,
															    @RequestParam(value = "coFiscalYearId",required = false) Integer coFiscalYearId,
															    @RequestParam(value = "reason",required = false) String reason,
															    @RequestParam(value = "gridData",required = false) String gridData,
															    @RequestParam(value = "delData[]",required = false) ArrayList<String>  delData,
															    @RequestParam(value = "emailListCU",required = false) Integer  emailListCU,
															    @RequestParam(value = "taxfreight",required = false) Byte  taxfreight,
																HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, BankingException, CompanyException, MessagingException, CustomerException{
		
		Cuinvoice aCuinvoice = new Cuinvoice();
		Cuinvoice aCuinvoize = null;
		itsLogger.info("CustomerInvoiceCreate-->"+theTotal+"<--theOper-->"+theOper+" CuSOID# "+cuSOID);
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		Rxaddress theRxaddress = null;
		try{
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			if(theInvoiceNameDate != null && theInvoiceNameDate != ""){
				aCuinvoice.setInvoiceDate(DateUtils.parseDate(theInvoiceNameDate, new String[]{"MM/dd/yyyy"}));
			}
			aCuinvoice.setInvoiceNumber(theInvoiceNumber);
			if(theDueDate != null && theDueDate != ""){
				aCuinvoice.setDueDate(DateUtils.parseDate(theDueDate, new String[]{"MM/dd/yyyy"}));
			}
			if(theShipDate != null && theShipDate != ""){
				aCuinvoice.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
			}
			
			aCuinvoice.setPrFromWarehouseId(thePrWareHouseID);
			aCuinvoice.setVeShipViaId(theShipViaCusotmerID);
			aCuinvoice.setTrackingNumber(theTrackingID);
			aCuinvoice.setTaxfreight(taxfreight);
			
				if(theFrieght!=null)
				aCuinvoice.setFreight(theFrieght);
				else
				aCuinvoice.setFreight(new BigDecimal(0));	
			
				if(theTax!=null){
				aCuinvoice.setTaxTotal(theTax);
				aCuinvoice.setTaxAmount(theTax);
				}
				else{
				aCuinvoice.setTaxTotal(new BigDecimal(0));
				aCuinvoice.setTaxAmount(new BigDecimal(0));
				}
				
				/*if(theTotal!=null)
				aCuinvoice.setCostTotal(theTotal);
				else*/
				aCuinvoice.setCostTotal(new BigDecimal(0));
				aCuinvoice.setTaxRate(taxRate);
				
				if(theSubTotal!=null)
				aCuinvoice.setSubtotal(theSubTotal);
				else
				aCuinvoice.setSubtotal(new BigDecimal(0));
			
			aCuinvoice.setCustomerPonumber(thePONumber);
			aCuinvoice.setCuAssignmentId0(theSalesRepID);
			aCuinvoice.setCuAssignmentId1(theCSRID);
			aCuinvoice.setCuAssignmentId2(theSalesMgrID);
			aCuinvoice.setCuAssignmentId3(theEngineerID);
			aCuinvoice.setCuAssignmentId4(theProjectID);
			aCuinvoice.setCoTaxTerritoryId(theTaxTerritory);
			aCuinvoice.setCoDivisionId(theDivision);
			aCuinvoice.setCuTermsId(theTermsID);
			aCuinvoice.setReason(reason);
			
			System.out.println("jobnodescription:::"+jobnodescription);
			aCuinvoice.setJobnoDescription(jobnodescription);
			
			System.out.println(":::+++++========"+rxContactID+"+++===="+emailListCU);
			aCuinvoice.setRxContactId(emailListCU);
		
			aCuinvoice.setTransactionStatus(1);
			aCuinvoice.setRxCustomerId(customerID);
			//Newly added
			aCuinvoice.setRxBillToId(customerID);
			if(theCuInvoiceID!=null){
				aCuinvoice.setCuInvoiceId(theCuInvoiceID);
			} else {
				aCuinvoice.setCuInvoiceId(cuInvHiddenId);
			}
			aCuinvoice.setInvoiceNumber(InvoiceNo);
			aCuinvoice.setJoReleaseDetailId(theJoReleaseDetailsID);
			aCuinvoice.setTaxRate(taxRate);
			aCuinvoice.setCuSoid(cuSOID);
			
			System.out.println("transactionStatus::"+transaction);
			
			if(aCuinvoice.getCuInvoiceId()!=null && aCuinvoice.getCuInvoiceId() > 0){
				aCuinvoice.setcIopenStatus(false);
				System.out.println("Testing");
			}else{
				aCuinvoice.setcIopenStatus(true);
			}
			
			System.out.println("opentStatus::"+aCuinvoice.getcIopenStatus());
			
			if(theTotal.signum() == -1)
			{
				aCuinvoice.setCreditmemo(0); 
				aCuinvoice.setIscredit(1);
				aCuinvoice.setMemoStatus(0);
			}
			else
			{
				aCuinvoice.setCreditmemo(0); 
				aCuinvoice.setIscredit(0);
				aCuinvoice.setMemoStatus(0);
			}
			
			
			if(theDoNotMailId != null){
				if(theDoNotMailId.equals("on"))
				aCuinvoice.setDoNotMail((byte)1);
				else
				aCuinvoice.setDoNotMail((byte)0);
			}
			/*if(theshipToMode != null)
				aCuinvoice.setShipToMode(theshipToMode.byteValue());
			if(therxShipToID != null)
				aCuinvoice.setRxShipToId(therxShipToID);
			if(theshipToMode == 3 && therxShipToOtherAddressID != null)
			{
				aCuinvoice.setRxShipToAddressId(therxShipToOtherAddressID);
			}
			else{
				aCuinvoice.setRxShipToAddressId(rxShiptoAddressID);
			}
			if(theshipToMode != null && theshipToMode == 3)
			{
				theRxaddress = new Rxaddress();
				theRxaddress.setName(customerShipToAddressID);
				theRxaddress.setAddress1(customerShipToAddressID1);
				theRxaddress.setAddress2(customerShipToAddressID2);
				theRxaddress.setCity(customerShipToCity);
				theRxaddress.setState(customerShipToState);
				theRxaddress.setZip(customerShipToZipID);
				theRxaddress.setRxAddressId(therxShipToOtherAddressID);
				theRxaddress.setIsBillTo(false);
				theRxaddress.setInActive(false);
				theRxaddress.setIsMailing(false);
				theRxaddress.setIsStreet(false);
				theRxaddress.setIsShipTo(false);
				Integer rxOtherID = itsJobService.insertRxAddress(theRxaddress, "");
				aCuinvoice.setRxShipToAddressId(rxOtherID);
				
			}*/
			aCuinvoice.setShipToMode(CIrxShiptomodevalue);
			if(CIrxShiptomodevalue==0){
				//US
				aCuinvoice.setPrToWarehouseId(CIrxShiptoid);
			}else if(CIrxShiptomodevalue==1){
				//Customer
				aCuinvoice.setRxShipToId(CIrxShiptoid);
			}else if(CIrxShiptomodevalue==2){
				//jobSite
				aCuinvoice.setRxShipToId(CIrxShiptoid);
			}else{
				//Other
				Integer RxotheraddressID=CIrxShiptoid;
				if(RxotheraddressID==null ||  RxotheraddressID==0){
					//Insert RxAddress
					theRxaddress = new Rxaddress();
					theRxaddress.setName(customerShipToAddressID);
					theRxaddress.setAddress1(customerShipToAddressID1);
					theRxaddress.setAddress2(customerShipToAddressID2);
					theRxaddress.setCity(customerShipToCity);
					theRxaddress.setState(customerShipToState);
					theRxaddress.setZip(customerShipToZipID);
					theRxaddress.setRxAddressId(null);
					theRxaddress.setIsBillTo(false);
					theRxaddress.setInActive(false);
					theRxaddress.setIsMailing(false);
					theRxaddress.setIsStreet(false);
					theRxaddress.setIsShipTo(true);
					RxotheraddressID= itsJobService.insertRxAddress(theRxaddress, "");
					
				}else{
					//Update RxAddress
					theRxaddress = new Rxaddress();
					theRxaddress.setName(customerShipToAddressID);
					theRxaddress.setAddress1(customerShipToAddressID1);
					theRxaddress.setAddress2(customerShipToAddressID2);
					theRxaddress.setCity(customerShipToCity);
					theRxaddress.setState(customerShipToState);
					theRxaddress.setZip(customerShipToZipID);
					theRxaddress.setRxAddressId(RxotheraddressID);
					theRxaddress.setIsBillTo(false);
					theRxaddress.setInActive(false);
					theRxaddress.setIsMailing(false);
					theRxaddress.setIsStreet(false);
					theRxaddress.setIsShipTo(true);
					itsCuMasterService.updateRxaddress(theRxaddress);
				}
				
				aCuinvoice.setRxShipToAddressId(RxotheraddressID);
			}
			
			/**Date :18-11-2015 Commented by:Leo Purpose: shiptoAddress not updating - reason customer warehse id column conflict*/   
			/*if(releaseType!=1 && releaseType!=4){
				aCuinvoice.setPrToWarehouseId(thePrWareHouseID);
			}*/
			aCuinvoize = itsJobService.getSingleCuInvoiceObj(aCuinvoice.getCuInvoiceId()==null?0:aCuinvoice.getCuInvoiceId());
			if(aCuinvoize==null){
				aCuinvoize = new Cuinvoice();
			}
			if(theOper.equalsIgnoreCase("add")){
				aCuinvoice.setCreatedById(aUserBean.getUserId());
				aCuinvoice.setAppliedAmount(new BigDecimal(0));
				aCuinvoice.setInvoiceAmount(theTotal); // Edited by leo
			/*	Date now = new Date();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String createdDate = df.format(now);
				if((createdDate != null && createdDate != "")){
					aCuinvoice.setCreatedOn(DateUtils.parseDate(createdDate, new String[]{"MM/dd/yyyy"}));
				}	*/			
				aCuinvoice.setCreatedOn(new Date());
				aCuinvoice.setApplied(false);
				aJoReleaseDetail.setCustomerInvoiceDate(DateUtils.parseDate(theInvoiceNameDate, new String[]{"MM/dd/yyyy"}));
				aJoReleaseDetail.setCustomerInvoiceAmount(theTotal); // Edited by leo
				aCuinvoice = itsJobService.addCusotmerInvoiceDetails(aCuinvoice, aJoReleaseDetail,null);
				itsLogger.info("CustomerInvoiceCreate CIOpenStatus-->"+aCuinvoice.getcIopenStatus());
				if(cuSOID!=null && cuSOID>0){
				if(releaseType == 2){
					itsJobService.insertCustomerInvoiceLines(cuSOID,aCuinvoice.getCuInvoiceId());
				} else if (releaseType == 1) {
					itsJobService.insertCustomerInvoiceLinesFromPO(cuSOID, aCuinvoice.getCuInvoiceId(),releaseType,BigDecimal.ZERO);
				}
				}
				itsJobService.updateTaxableandNonTaxableforCuInvoice(aCuinvoice);
				if(transaction!=null && transaction.equalsIgnoreCase("close")){
					itsGltransactionService.receiveCustomerInvoiceBill(aCuinvoice,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());
					if(aCuinvoice.getInvoiceAmount().compareTo(new BigDecimal("0.0000"))!=0){
						aCuinvoice.setUserName(aUserBean.getFullName());
						itsJobService.saveCustomerInvoiceLog(new Cuinvoice(),aCuinvoice,"CI-Created",1,coFiscalPeriodId,coFiscalYearId);
					}
				}
				
			}else if(theOper.equalsIgnoreCase("edit")){
				Cuinvoice theCuinvoice = new Cuinvoice();
				Integer userID=((UserBean)session.getAttribute(SessionConstants.USER)).getUserId();
				String username=((UserBean)session.getAttribute(SessionConstants.USER)).getUserName();
				aCuinvoice.setChangedById(aUserBean.getUserId());
				aCuinvoice.setChangedOn(new Date());
				aCuinvoice.setInvoiceAmount(theTotal);
				String invdate="";
				if(theInvoiceNameDate != null && theInvoiceNameDate != ""){
					aCuinvoice.setInvoiceDate(DateUtils.parseDate(theInvoiceNameDate, new String[]{"MM/dd/yyyy"}));
					
					invdate=JobUtil.convertinto12hourformat(theInvoiceNameDate,"MM/dd/yyyy","yyyy-MM-dd");
				}
				boolean lineitemupdateornot=false;
				if(delData!=null){
					for(String detailID:delData){
						System.out.println("delData[i]"+detailID);
						Integer cuInvoiceDetailID=JobUtil.ConvertintoInteger(detailID);
						lineitemupdateornot=manpulateCuInvoiceReleaseLineItem(aCuinvoice.getCuInvoiceId(),null,null,null,"del",null,null,
								null,null,null,cuInvoiceDetailID,null,null,null,userID,username);
					}
				}
				
				
				JsonParser parser = new JsonParser();
				if ( gridData!=null) {
					System.out.println("gridData"+gridData);
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						String desc=obj.get("description").getAsString();
						String itemCode=obj.get("itemCode").getAsString();
						String note=obj.get("note").getAsString();
						String Oper="add";
						Integer prMasterID=obj.get("prMasterId").getAsInt();
						BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
						BigDecimal quantityOrder=obj.get("quantityBilled").getAsBigDecimal();
						String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
						unitCost_String=unitCost_String.replaceAll(",", "");
						BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
						Integer cuInvoiceDetailId=null;
						if(obj.get("cuInvoiceDetailId")!=null && obj.get("cuInvoiceDetailId").getAsString()!=""&& obj.get("cuInvoiceDetailId").getAsString().length()>0){
							cuInvoiceDetailId=obj.get("cuInvoiceDetailId").getAsInt();
						}
						String Taxable=(obj.get("taxable")!=null && obj.get("taxable").getAsInt()==1)?"Yes":"NO";
						Double taxRatee=Double.parseDouble(""+aCuinvoice.getTaxRate());
						if(cuInvoiceDetailId!=null){
							Oper="edit";
						}
						boolean val=manpulateCuInvoiceReleaseLineItem(aCuinvoice.getCuInvoiceId(),desc,itemCode,note,Oper,prMasterID,priceMultiplier,
								quantityOrder,Taxable,unitCost,cuInvoiceDetailId,taxRatee,aCuinvoice.getFreight(),aCuinvoice.getReleaseType(),userID,username);
						if(val){
							lineitemupdateornot=true;
						}
						theCuinvoice = itsJobService.getCustomerInvoiceDetails(aCuinvoice.getCuInvoiceId());
						aCuinvoice.setCostTotal(theCuinvoice.getCostTotal());
					}
				}else{
						theCuinvoice = itsJobService.getCustomerInvoiceDetails(aCuinvoice.getCuInvoiceId());
						aCuinvoice.setCostTotal(theCuinvoice.getCostTotal());
				}
				
				
				theCuinvoice = itsJobService.updateCusotmerInvoiceDetails(aCuinvoice);
				itsJobService.updateTaxableandNonTaxableforCuInvoice(aCuinvoice);
				Integer dateval=(aCuinvoize.getInvoiceDate()==null?"":aCuinvoize.getInvoiceDate().toString()).indexOf(invdate);
				if(transaction!=null && transaction.equalsIgnoreCase("close") ||(dateval!=0)){
					if(aCuinvoize.getInvoiceAmount().compareTo(aCuinvoice.getInvoiceAmount())!=0  ||(dateval!=0)){
					itsLogger.info("Transaction Rollback. .. ..."+aCuinvoize.getInvoiceAmount()+"#@#"+aCuinvoice.getInvoiceAmount());
					Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("CI");
					
					GlRollback glRollback = new GlRollback();
					glRollback.setVeBillID(aCuinvoice.getCuInvoiceId());
					glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
					glRollback.setPeriodID(coFiscalPeriodId);
					glRollback.setYearID(coFiscalYearId);
					glRollback.setTransactionDate(aCuinvoice.getInvoiceDate());
					
					itsGltransactionService.rollBackGlTransaction(glRollback);
					itsGltransactionService.receiveCustomerInvoiceBill(aCuinvoice,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());
					if(delData!=null){
						//theCuinvoice = itsJobService.getCustomerInvoiceDetails(aCuinvoice.getCuInvoiceId());
						theCuinvoice.setUserName(aUserBean.getFullName());
						theCuinvoice.setReason(reason);
						if(aCuinvoize.getInvoiceAmount().compareTo(theCuinvoice.getInvoiceAmount())!=0)
						itsJobService.saveCustomerInvoiceLog(aCuinvoize,theCuinvoice,"CI-Line Item(s) Deleted",1,coFiscalPeriodId,coFiscalYearId);
						else
						itsJobService.saveCustomerInvoiceLog(aCuinvoize,theCuinvoice,"CI-Line Item(s) Deleted",0,coFiscalPeriodId,coFiscalYearId);
						
					}else{
						theCuinvoice.setUserName(aUserBean.getFullName());
						theCuinvoice.setReason(reason);
						if(aCuinvoize.getSubtotal().compareTo(theCuinvoice.getSubtotal())!=0 || aCuinvoize.getFreight().compareTo(theCuinvoice.getFreight())!=0 ||
								aCuinvoize.getTaxAmount().compareTo(theCuinvoice.getTaxAmount())!=0)
						itsJobService.saveCustomerInvoiceLog(aCuinvoize,theCuinvoice,"CI-Edited",1,coFiscalPeriodId,coFiscalYearId);
						else
						itsJobService.saveCustomerInvoiceLog(aCuinvoize,theCuinvoice,"CI-Edited",0,coFiscalPeriodId,coFiscalYearId);
					}
				}else{
					if(delData!=null){
						theCuinvoice = itsJobService.getCustomerInvoiceDetails(aCuinvoice.getCuInvoiceId());
						theCuinvoice.setUserName(aUserBean.getFullName());
						theCuinvoice.setReason(reason);
						itsJobService.saveCustomerInvoiceLog(aCuinvoize,theCuinvoice,"CI-Line Item(s) Deleted",0,coFiscalPeriodId,coFiscalYearId);
					}else{
						theCuinvoice.setUserName(aUserBean.getFullName());
						theCuinvoice.setReason(reason);
						itsJobService.saveCustomerInvoiceLog(aCuinvoize,theCuinvoice,"CI-Edited",0,coFiscalPeriodId,coFiscalYearId);
					}
				}
					}
					
			}
			
		}catch (JobException e) {
			sendTransactionException("<b>theCuInvoiceID:</b>"+theCuInvoiceID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCuinvoice;
	}
	@RequestMapping(value="/addInventoryPOReleaseLineItem", method = RequestMethod.POST)
	public @ResponseBody Integer insertPOReleaseItem(@RequestParam(value="note", required= false) String theItemCode,
														   @RequestParam(value="description", required= false) String theDescription, 
														   @RequestParam(value="quantityOrdered", required=false) BigDecimal theQuantity,
														   @RequestParam(value="quantityReceived", required=false) BigDecimal theReceivedQuantity,
														   @RequestParam(value="unitCost", required= false) BigDecimal theUnitCost, 
														   @RequestParam(value="priceMultiplier", required= false) BigDecimal theMulti, 
														   @RequestParam(value="taxable", required = false) Boolean theTax,
														   
														   @RequestParam(value="vePoid", required = false) Integer theVepoId,
														   @RequestParam(value="prMasterId", required = false) Integer thePRMasterID,
														   @RequestParam(value="vePodetailId", required = false) Integer theVepoDetailId,
														   
														   @RequestParam(value="posistion", required = false) Double thePosistion,
														   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		Vepodetail aVepodetail = new Vepodetail();
		Vepodetail vepoupdateObj  = null;
		
		
		
		Float aFloat = null;
		Vepo aVepoupdateObj = null;
		try{
		aVepodetail.setDescription(theDescription);
		aVepodetail.setQuantityOrdered(theQuantity);
		aVepodetail.setQuantityReceived(theQuantity);
		aVepodetail.setUnitCost(theUnitCost);
		aVepodetail.setPriceMultiplier(theMulti);
		aVepodetail.setTaxable(theTax);
		aVepodetail.setVePoid(theVepoId);
		aVepodetail.setPrMasterId(thePRMasterID);
		aVepodetail.setVePodetailId(theVepoDetailId);
		
			
			
		}catch (Exception e) {
			sendTransactionException("<b>theCuInvoiceID:</b>"+theVepoId,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
		}
		return 0;
	}
	
	/*@RequestMapping(value="/manpulaterSOTemplateLineItem", method = RequestMethod.POST)
	public @ResponseBody Integer manpulaterSOTemplateLineItem(@RequestParam(value="note", required= false) String theItemCode,
														   @RequestParam(value="description", required= false) String theDescription, 
														   @RequestParam(value="quantityOrdered", required=false) BigDecimal theQuantity,
														   @RequestParam(value="unitCost", required= false) BigDecimal theUnitCost, 
														   @RequestParam(value="priceMultiplier", required= false) BigDecimal theMulti, 
														   @RequestParam(value="taxable", required = false) Boolean theTax,
														   @RequestParam(value="prMasterId", required = false) Integer thePRMasterID,														  
														   @RequestParam(value="operForAck", required = false) String theOperForAck,
														   @RequestParam(value="taxRate", required= false) Double theTaxRate,
														   @RequestParam(value="freight", required= false) BigDecimal theFreight,
														   @RequestParam(value="cusoID", required = false) Integer theCusoID,
														   @RequestParam(value="cuSodetailId", required = false) Integer theCuSodetailId,
														   @RequestParam(value="templateId", required = false) String templateId,
														   HttpSession theSession, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException {
		Integer cuSOID = null;
		Cusodetailtemplate aCusodetailtemplate = new Cusodetailtemplate();
		Cusodetailtemplate cusodetailtemplateObj  = null;
		Cusotemplate aCusotemplate = new Cusotemplate();
		double aTaxDecimal, aTotalTax;
		System.out.println("operForAck ::"+theOperForAck+" :: "+theOperForAck);
		Vepo aVepo = new Vepo();
		Float aFloat = null;
		BigDecimal[] costDetails = {theUnitCost,theMulti,theQuantity};
		aCusodetailtemplate.setDescription(theDescription);
		aCusodetailtemplate.setQuantityOrdered(theQuantity);
		aCusodetailtemplate.setUnitCost(theUnitCost);
		aCusodetailtemplate.setPriceMultiplier(theMulti);
		aCusodetailtemplate.setPrMasterId(thePRMasterID);
		byte aTax = (byte) (theTax?1:0);
		aCusodetailtemplate.setTaxable(aTax);
		if(templateId != null)
			aCusotemplate.setTemplateDescription(templateId);
		
		try{
			BigDecimal[] taxandTotal = computeTaxTotal(theUnitCost, theMulti,
					theQuantity, aTax, theTaxRate,
					theFreight);
			//aCusotemplate.setCuSoid(cuSOID);
			aCusotemplate.setFreight(theFreight);
			aCusotemplate.setTaxTotal(taxandTotal[1]);
			aCusotemplate.setSubTotal(taxandTotal[0]);
			aCusotemplate.setCostTotal(taxandTotal[2]);
			if(theCusoID != null){
				aCusotemplate.setCuSoid(theCusoID);
				aCusodetailtemplate.setCuSoid(theCusoID);
			}
			cuSOID = itsJobService.addSOTemplateLineItem(aCusodetailtemplate, aCusotemplate);
			
		}catch (JobException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return cuSOID;
	}*/
	@RequestMapping(value = "/manpulaterSOTemplateLineItem", method = RequestMethod.POST)
	public @ResponseBody
	Integer ManipulateSOReleaseLineItem(
			@RequestParam(value = "cusoID", required = false) Integer cuSOID,//
			@RequestParam(value = "description", required = false) String desc,//
			@RequestParam(value = "itemCode", required = false) String itemCode,
			@RequestParam(value = "note", required = false) String note,//
			@RequestParam(value = "oper", required = false) String Oper,//
			@RequestParam(value = "prMasterId", required = false) Integer prMasterID,//
			@RequestParam(value = "priceMultiplier", required = false) BigDecimal priceMultiplies,//
			@RequestParam(value = "quantityOrdered", required = false) BigDecimal quantityOrder,//
			@RequestParam(value = "taxable", required = false) String Taxable,//
			@RequestParam(value = "unitCost", required = false) BigDecimal unitCost,//
			@RequestParam(value = "cuSodetailId", required = false) Integer cuSODetailID,//
			@RequestParam(value = "taxRate", required = false) Double taxRate,//
			@RequestParam(value = "freight", required = false) BigDecimal freight,//
			@RequestParam(value="templateId", required = false) String templateId,//
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		Cusodetailtemplate aCusodetail = new Cusodetailtemplate();
		Cusotemplate aCuso = new Cusotemplate();
		Cusodetailtemplate aCusodetailObj = null;
		boolean saved = false;
		Integer cusoID = cuSOID;
		BigDecimal[] taxandTotal = null;
		BigDecimal[] editTaxTotal = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (cuSODetailID != null) {
				aCusodetailObj = itsJobService
						.getSingleCusoTemplateDetailObj(cuSODetailID);
			}
			if (Oper.equalsIgnoreCase("del")) {
				aCusodetail.setCuSodetailId(cuSODetailID);
				taxandTotal = computeTaxTotal(aCusodetailObj.getUnitCost(),
						aCusodetailObj.getPriceMultiplier(),
						aCusodetailObj.getQuantityOrdered(),
						aCusodetailObj.getTaxable(), taxRate, freight);
				aCuso.setTaxTotal(taxandTotal[1]);
				aCuso.setCuSoid(cuSOID);
				saved = itsJobService.deleteSOTemplateLineItem(aCusodetail, aCuso);
			} else {
				aCusodetail.setCuSoid(cuSOID);
				aCusodetail.setDescription(desc);
				aCusodetail.setPrMasterId(prMasterID);
				aCusodetail.setPriceMultiplier(priceMultiplies);
				System.out.println("SOTemplate quantityOrder ::: "+quantityOrder);
				if(quantityOrder != null)
					aCusodetail.setQuantityOrdered(quantityOrder);
				else
					aCusodetail.setQuantityOrdered(new BigDecimal(0.00));
				aCusodetail.setQuantityBilled(new BigDecimal(0.00));
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("on") || Taxable.equalsIgnoreCase("Yes")) {
						aCusodetail.setTaxable((byte) 1);
					} else {
						aCusodetail.setTaxable((byte) 0);
					}
				}
				aCusodetail.setUnitCost(unitCost);
				taxandTotal = new BigDecimal[3];
				;
				taxandTotal = computeTaxTotal(unitCost, priceMultiplies,
						quantityOrder, aCusodetail.getTaxable(), taxRate,
						freight);
				if(cuSOID != null && cuSOID > 0)
					aCuso.setCuSoid(cuSOID);
				if(templateId != null)
					aCuso.setTemplateDescription(templateId);
				aCuso.setFreight(freight);
				aCuso.setTaxTotal(taxandTotal[1]);
				aCuso.setSubTotal(taxandTotal[0]);
				aCuso.setCostTotal(taxandTotal[2]);
				if (Oper.equalsIgnoreCase("add")) {
					cusoID = itsJobService.addSOTemplateLineItem1(aCusodetail, aCuso);
				} else if (Oper.equalsIgnoreCase("edit")) {
					Cusotemplate editCusoObj = new Cusotemplate();
					editCusoObj.setCuSoid(cuSOID);
					byte oldTaxEnabled = aCusodetailObj.getTaxable();
					byte newTaxEnabled = aCusodetail.getTaxable();
					editTaxTotal = new BigDecimal[3];
					editTaxTotal = computeTaxTotal(
							aCusodetailObj.getUnitCost(),
							aCusodetailObj.getPriceMultiplier(),
							aCusodetailObj.getQuantityOrdered(),
							aCusodetailObj.getTaxable(), taxRate, freight);
					editCusoObj.setTaxTotal(editTaxTotal[1]);
					editCusoObj.setCostTotal(editTaxTotal[2]);
					editCusoObj.setSubTotal(editTaxTotal[0]);
					if (oldTaxEnabled != newTaxEnabled) {
						if (oldTaxEnabled == 0 && newTaxEnabled == 1) {
							taxandTotal = computeTaxTotal(unitCost,
									priceMultiplies, quantityOrder,
									newTaxEnabled, taxRate, freight);
							aCuso.setTaxTotal(taxandTotal[1]);
							aCuso.setCostTotal(taxandTotal[2]);
							aCuso.setSubTotal(taxandTotal[0]);
						} else {
							taxandTotal = computeTaxTotal(unitCost,
									priceMultiplies, quantityOrder,
									newTaxEnabled, taxRate, freight);
							aCuso.setTaxTotal(taxandTotal[1].negate());
							aCuso.setCostTotal(taxandTotal[2]);
							aCuso.setSubTotal(taxandTotal[0]);
						}
					}
					aCusodetail.setCuSodetailId(cuSODetailID);
					saved = itsJobService.editSOTemplateLineItem(aCusodetail,
							aCuso, editCusoObj);
				}
			}
		} catch (JobException e) {
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} finally {
			aCusodetail = null;
			aCuso = null;
			aCusodetailObj = null;
			saved = false;
			taxandTotal = null;
			editTaxTotal = null;
		}

		return cusoID;
	}
	
	/*public BigDecimal[] computeTaxTotal(BigDecimal unitPrice,
			BigDecimal priceMultiplier, BigDecimal quantityOrdered,
			byte taxable, Double taxRate, BigDecimal freight) {
		BigDecimal[] taxandTotal = new BigDecimal[3];
		BigDecimal subTotal = new BigDecimal(0);
		BigDecimal Total = new BigDecimal(0);
		BigDecimal taxTotal = new BigDecimal(0);
		BigDecimal taxdivisor = new BigDecimal(100);

		try {
			subTotal = unitPrice.multiply(priceMultiplier).multiply(
					quantityOrdered);
			taxandTotal[0] = subTotal;
			if (taxable == 1) {
				taxTotal = subTotal.multiply(new BigDecimal(taxRate));
				taxTotal = taxTotal.divide(taxdivisor);
				Total = subTotal.add(taxTotal).add(freight);
			} else {
				taxTotal = new BigDecimal(0);
				Total = subTotal.add(freight);
			}
			taxandTotal[1] = taxTotal;
			taxandTotal[2] = Total;
		} catch (NullPointerException e) {
			e.getMessage();
		} finally {
			subTotal = null;
			Total = null;
			taxTotal = null;
			taxdivisor = null;
		}

		return taxandTotal;

	}*/
	public BigDecimal[] computeTaxTotal(BigDecimal unitPrice,
			BigDecimal priceMultiplier, BigDecimal quantityOrdered,
			byte taxable, Double taxRate, BigDecimal freight) {
		BigDecimal[] taxandTotal = new BigDecimal[3];
		BigDecimal subTotal = new BigDecimal(0);
		BigDecimal Total = new BigDecimal(0);
		BigDecimal taxTotal = new BigDecimal(0);
		BigDecimal taxdivisor = new BigDecimal(100);

		try {
			subTotal = unitPrice.multiply(priceMultiplier).multiply(
					quantityOrdered);
			taxandTotal[0] = subTotal;
			if (taxable == 1) {
				taxTotal = subTotal.multiply(new BigDecimal(taxRate));
				taxTotal = taxTotal.divide(taxdivisor);
				Total = subTotal.add(taxTotal).add(freight);
			} else {
				taxTotal = new BigDecimal(0);
				Total = subTotal.add(freight);
			}
			taxandTotal[1] = taxTotal;
			taxandTotal[2] = Total;
		} catch (NullPointerException e) {
			itsLogger.error(e.getMessage());
		} finally {
			subTotal = null;
			Total = null;
			taxTotal = null;
			taxdivisor = null;
		}

		return taxandTotal;

	}
	
	@RequestMapping(value = "/getSOTemplateDetails", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getTransferDetails(@RequestParam("cuSoid") Integer thecuSoid,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<Cusodetailtemplate> transferList = new ArrayList<Cusodetailtemplate>();
		CustomResponse aResponse = new CustomResponse();
		try {
					 
			System.out.println("Line Item Grid ID--->"+thecuSoid);
			transferList = (ArrayList<Cusodetailtemplate>) itsJobService.getSOTemplateList(thecuSoid);
			aResponse.setRows(transferList);
		} catch (Exception e) {
			sendTransactionException("<b>thecuSoid:</b>"+thecuSoid,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}
	
	@RequestMapping(value="/saveSOTemplate", method = RequestMethod.POST)
	public @ResponseBody Integer saveSOTemplate(@RequestParam(value="templateName", required= false) String theTemplateName,
														   @RequestParam(value="subTotal", required= false) BigDecimal subTotal, 
														   @RequestParam(value="freight", required=false) BigDecimal freight,
														   @RequestParam(value="taxRate", required= false) BigDecimal taxRate, 
														   @RequestParam(value="taxValue", required= false) BigDecimal taxValue,														  
														   @RequestParam(value="total", required= false) BigDecimal total,
														   @RequestParam(value="cusoid", required= false) Integer cusoid,
														   @RequestParam(value="gridData", required= false) String gridData,
														@RequestParam(value = "DelSOLData[]",required = false) ArrayList<String>  delData,
														   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		/*templateName="+templateName+"&subTotal="+subTotal+"&freight="+freight+"&taxRate="+taxRate+"&taxValue="+taxValue
				+"&total="+total*/

		System.out.println("Grid Data"+gridData);
		
		if (delData!=null && delData.size()>0 ) {
			for(String cuSODetailID_str:delData){
				Integer cuSODetailID=JobUtil.ConvertintoInteger(cuSODetailID_str);
				Cusodetailtemplate theCusoDetailID=new Cusodetailtemplate();
				theCusoDetailID.setCuSodetailId(cuSODetailID);
				itsJobService.deleteSOTemplateLineItem(theCusoDetailID, null);
				//itsJobService.UpdatenewQuotes(ajoQuoteDetailMstr, "del");
			}
		}
		Cusotemplate theCusotemplate = new Cusotemplate();
		Integer cusoID = null;
		try {
		if(theTemplateName != null)
			theCusotemplate.setTemplateDescription(theTemplateName);
		if(subTotal != null)
			theCusotemplate.setSubTotal(subTotal);
		if(freight != null)
			theCusotemplate.setFreight(freight);
		if(taxRate != null)
			theCusotemplate.setTaxRate(taxRate);
		if(taxValue != null)
			theCusotemplate.setTaxTotal(taxValue);
		if(total != null)
			theCusotemplate.setCostTotal(total);	
		if(cusoid != null && cusoid > 0)
			theCusotemplate.setCuSoid(cusoid);
		
		cusoid = itsJobService.saveSOTemplate(theCusotemplate);
		System.out.println("CusoId"+cusoid);
	    JsonParser parser = new JsonParser();
		if ( gridData!=null) {

			System.out.println("gridData"+gridData);
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			int i=1;
			for (JsonElement ele1 : array) {
				JsonObject obj = ele1.getAsJsonObject();
				Cusodetailtemplate theCusoDetailtemplate=new Cusodetailtemplate();
				Integer CuSODetailID=null;
				String oper="";
				if(obj.get("cuSodetailId")!=null && obj.get("cuSodetailId").getAsString()!=""&& obj.get("cuSodetailId").getAsString().length()>0){
				
					CuSODetailID=obj.get("cuSodetailId").getAsInt();
					theCusoDetailtemplate.setCuSodetailId(CuSODetailID);
					oper="Edit";
					
				}
				else
					oper="Add";
				
				
				theCusoDetailtemplate.setCuSoid(cusoid);

			Integer prMasterId= obj.get("prMasterId").getAsInt();	
			if(prMasterId!=null)
				theCusoDetailtemplate.setPrMasterId(prMasterId);
			
			String description=obj.get("description").getAsString();
			if(description!=null)
				theCusoDetailtemplate.setDescription(description);
			
			
			BigDecimal quantityOrdered =BigDecimal.ZERO;
			String quantityOrdered_str=obj.get("quantityOrdered").getAsString();
			if(quantityOrdered_str!=null && quantityOrdered_str!="" && quantityOrdered_str.length()>0){
				quantityOrdered_str=quantityOrdered_str.replaceAll("\\$", "");
				quantityOrdered_str=quantityOrdered_str.replaceAll(",", "");
				quantityOrdered=JobUtil.ConvertintoBigDecimal(quantityOrdered_str);
				theCusoDetailtemplate.setQuantityOrdered(quantityOrdered);
				}
				
			BigDecimal unitCost =BigDecimal.ZERO;
			String unitCost_str=obj.get("unitCost").getAsString();
			if(unitCost_str!=null && unitCost_str!="" && unitCost_str.length()>0){
				unitCost_str=unitCost_str.replaceAll("\\$", "");
				unitCost_str=unitCost_str.replaceAll(",", "");
				unitCost=JobUtil.ConvertintoBigDecimal(unitCost_str);
				theCusoDetailtemplate.setUnitCost(unitCost);
				}
			
			BigDecimal priceMultiplier =BigDecimal.ZERO;
			String priceMultiplier_str=obj.get("unitCost").getAsString();
			if(priceMultiplier_str!=null && priceMultiplier_str!="" && priceMultiplier_str.length()>0){
					priceMultiplier_str=priceMultiplier_str.replaceAll("\\$", "");
					priceMultiplier_str=priceMultiplier_str.replaceAll(",", "");
					priceMultiplier=JobUtil.ConvertintoBigDecimal(priceMultiplier_str);
					theCusoDetailtemplate.setPriceMultiplier(priceMultiplier);
				}
			
			BigDecimal unitPrice =BigDecimal.ZERO;
			String unitPrice_str=obj.get("unitCost").getAsString();
			if(unitPrice_str!=null && unitPrice_str!="" && unitPrice_str.length()>0){
					unitPrice_str=unitPrice_str.replaceAll("\\$", "");
					unitPrice_str=unitPrice_str.replaceAll(",", "");
					unitPrice=JobUtil.ConvertintoBigDecimal(unitPrice_str);
					theCusoDetailtemplate.setUnitPrice(unitPrice);
				}
			
			String Note=obj.get("inlineNote").getAsString();
			System.out.println("INlineNote"+Note);
			if(Note!=null)
				theCusoDetailtemplate.setNote(Note);

			    theCusoDetailtemplate.setPosition(i);
				itsJobService.saveSODetailTemplate(theCusoDetailtemplate);
				i=i+1;
			}
		}
		
    	
		} catch (Exception e) {
			sendTransactionException("<b>cusoid:</b>"+cusoid,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		return cusoid;
		
	}
	
	@RequestMapping(value="/deleteSOTemplate", method = RequestMethod.POST)
	public @ResponseBody Integer deleteSOTemplate(@RequestParam(value="cuSOid", required= false) Integer cuSOid,
														   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		
		Integer cusoID = null;
		try {		
		
		cusoID = itsJobService.deleteSOTemplate(cuSOid);
		} catch (Exception e) {
			sendTransactionException("<b>cuSOid:</b>"+cuSOid,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		return cusoID;		
	}	
	
	@RequestMapping(value = "/getSOTemplate", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getSOTemplate(HttpServletResponse response,HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		ArrayList<Cusotemplate> transferList = new ArrayList<Cusotemplate>();
		CustomResponse aResponse = new CustomResponse();
		try {
					 
			System.out.println("getSOTemplate Grid ID--->");
			transferList = (ArrayList<Cusotemplate>) itsJobService
					.getSOTemplate();
			aResponse.setRows(transferList);
		} catch (Exception e) {
			sendTransactionException("<b>MethodName:</b>getSOTemplate","JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		}
		return aResponse;
	}	
	
	@RequestMapping(value="/loadSOTemplate", method = RequestMethod.POST)
	public@ResponseBody
	HashMap<String, Object> loadSOTemplate(@RequestParam(value="cuSOid", required= false) Integer cuSOid,
														   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		HashMap<String, Object> resp = null;
		Cusotemplate aCusotemplate = null;
		Cusodetailtemplate aCusoDetails = null;
		try {		
		
			aCusotemplate = itsJobService.loadSOTemplate(cuSOid);
			aCusoDetails = itsJobService.getCusoTemplateDetailObj(cuSOid);
		} catch (Exception e) {
			sendTransactionException("<b>cuSOid:</b>"+cuSOid,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		resp = new HashMap<String, Object>();
		resp.put("Cusotemplate", aCusotemplate);
		if(aCusoDetails == null)
			aCusoDetails = new Cusodetailtemplate();
		resp.put("Cusodetailtemplate", aCusoDetails);
		return resp;		
	}
	
	@RequestMapping(value="/getPriceDetails", method = RequestMethod.POST)
	public@ResponseBody
	Map<String, BigDecimal> getPriceDetails(@RequestParam(value="cusoid", required= false) Integer cuSOid,
			@RequestParam(value="prMasterId", required= false) Integer prMasterID, HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		Map<String,BigDecimal> aPrice = new HashMap<String, BigDecimal>();
		try {		
		
			aPrice = itsJobService.getSOTemplatePriceDetails(cuSOid);
		} catch (Exception e) {
			sendTransactionException("<b>cuSOid,prMasterID:</b>"+cuSOid+","+prMasterID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		
		return aPrice;		
	}
	
	@RequestMapping(value="/copyPoLineItem", method = RequestMethod.GET)
	public @ResponseBody Boolean copyPoLineItem(@RequestParam(value="vepoDetailId", required= true) Integer vePODetailId, 
			@RequestParam(value="vePOId", required= true) Integer aVePOId,
			ModelMap theModel,HttpServletResponse theResponse) throws IOException{
		try {
			itsJobService.copyPOLineItemService(vePODetailId);

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(((InventoryException) e).getItsErrorStatusCode(), e.getMessage());
			return false;
		}
		return true;
	}	
	@RequestMapping(value = "jobReleasevendorinvoice",method = RequestMethod.POST)
	public @ResponseBody CustomResponse jobReleasevendorinvoice(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
																	HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
		CustomResponse aResponse = new CustomResponse();
		try{
			List<Vepodetail> aPOLineItemDetails = itsJobService.getPOReleaseLineItemforvendorinvoice(theVepoId);
			/*List<Vebilldetail> Vebilldetaillineitem = itsJobService.getVeBillLineItemsFromRelease(theVepoId);
			List<Vepodetail> newVepodetail=new ArrayList<Vepodetail>();
			Map<Integer,BigDecimal> sortedmap=new HashMap<Integer, BigDecimal>();
			System.out.println("aPOLineItemDetails"+aPOLineItemDetails.size()+"Vebilldetaillineitem"+Vebilldetaillineitem.size());
			for(Vepodetail vepodetail: aPOLineItemDetails){
				BigDecimal quantity=new BigDecimal(0);
				for(Vebilldetail vebilldetail:Vebilldetaillineitem){
					System.out.println(vepodetail.getPrMasterId()+"==="+vebilldetail.getPrMasterId());
								if(vepodetail.getPrMasterId().equals(vebilldetail.getPrMasterId())){
									quantity=vepodetail.getQuantityBilled().subtract(vebilldetail.getQuantityBilled());
									System.out.println("sortedmap.get(vepodetail.getPrMasterId())=="+sortedmap.get(vepodetail.getPrMasterId()));
									if(sortedmap.get(vepodetail.getPrMasterId())!=null){
										sortedmap.put(vepodetail.getPrMasterId(), sortedmap.get(vepodetail.getPrMasterId()).subtract(vebilldetail.getQuantityBilled()));
									}else{
										System.out.println("vepodetail.getQuantityOrdered()==="+vepodetail.getQuantityOrdered()+"vebilldetail.getQuantityOrdered()=="+vebilldetail.getQuantityBilled());
										sortedmap.put(vepodetail.getPrMasterId(), vepodetail.getQuantityOrdered().setScale(4, BigDecimal.ROUND_DOWN).subtract(vebilldetail.getQuantityBilled().setScale(4, BigDecimal.ROUND_DOWN)));
									}
									
								}
					
					}
			}
			for(Vepodetail secondtimelist:aPOLineItemDetails){
				System.out.println("sortedmap.get(secondtimelist.getPrMasterId())=="+sortedmap.get(secondtimelist.getPrMasterId()));
				if(sortedmap.get(secondtimelist.getPrMasterId())!=null){
				BigDecimal quantity=sortedmap.get(secondtimelist.getPrMasterId());
				System.out.println("quantity-=====>"+quantity);
				if(quantity.equals(new BigDecimal(0).setScale(4, BigDecimal.ROUND_DOWN))){
					System.out.println("insi if");
					//aPOLineItemDetails.remove(i);
					
				}else{
					//secondtimelist
					secondtimelist.setQuantityOrdered(quantity);
					if(secondtimelist.getPriceMultiplier()!=null && secondtimelist.getPriceMultiplier().compareTo(BigDecimal.ZERO) >0){
						secondtimelist.setQuantityBilled(secondtimelist.getUnitCost().multiply(quantity).multiply(secondtimelist.getPriceMultiplier()));
					}else{
						secondtimelist.setQuantityBilled(secondtimelist.getUnitCost().multiply(quantity));
					}
					secondtimelist.setValidatequantity(quantity);
					System.out.println("inside the else if"+secondtimelist.getQuantityBilled());
					newVepodetail.add(secondtimelist);
				}
				}else{
					System.out.println("inside the else"+secondtimelist.getQuantityBilled());
					secondtimelist.setValidatequantity(secondtimelist.getQuantityOrdered());
					newVepodetail.add(secondtimelist);
				}
			}
			System.out.println("newVepodetail.size)"+newVepodetail.size());*/
			aResponse.setRows(aPOLineItemDetails);
			
			itsLogger.error(aPOLineItemDetails);
		}catch (JobException e) {
			sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
			e.printStackTrace();
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}
	
	
	
	
	
	/*@RequestMapping(value = "vendorinvoiceeditinsidejob",method = RequestMethod.POST)
	public @ResponseBody CustomResponse jobReleasevendorinvoice(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
																	HttpSession theSession, HttpServletResponse theResponse) throws JobException, IOException{
		CustomResponse aResponse = new CustomResponse();
		try{
			List<Vepodetail> aPOLineItemDetails = itsJobService.getPOReleaseLineItemforvendorinvoice(theVepoId);
			aResponse.setRows(aPOLineItemDetails);
			
			itsLogger.error(aPOLineItemDetails);
		}catch (JobException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aResponse;
	}*/
	
	
	
	
	
	
	@RequestMapping(value="/getVeBillDetails", method = RequestMethod.POST)
	public @ResponseBody Vebill getVeBillDetails(@RequestParam(value="releaseDetailID", required = false) Integer theReleaseDetailID,
													   @RequestParam(value="veBillID", required = false) Integer veBillID,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Vebill aVebill = new Vebill();
		try{
			 aVebill = itsJobService.getVeBillDetails(veBillID);
			 
			 System.out.println(veBillID + "::"+aVebill.getVeBillId());
			
		}catch (JobException e) {
			sendTransactionException("<b>theReleaseDetailID,veBillID:</b>"+theReleaseDetailID+","+veBillID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aVebill;
	}

@RequestMapping(value="/saveCustomerInvoiceSubtotal", method = RequestMethod.GET)
public @ResponseBody Boolean saveCustomerInvoiceSubtotal(@RequestParam(value="cuInvoiceId", required= false) Integer cuInvoiceId, 
		@RequestParam(value="amount", required= false) BigDecimal amount,
		ModelMap theModel,HttpServletResponse theResponse,
		HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
	System.out.println(amount+" :: "+cuInvoiceId);
	try {
		
		itsJobService.updateCuInvoiceSubTotal(cuInvoiceId,(amount));

	} catch (Exception e) {
		sendTransactionException("<b>cuInvoiceId:</b>"+cuInvoiceId,"JOB",e,session,therequest);
		itsLogger.error(e.getMessage(), e);
		theResponse.sendError(((InventoryException) e).getItsErrorStatusCode(), e.getMessage());
		return false;
	}
	return true;
}


@RequestMapping(value="/getBilledUnbilledAmount", method = RequestMethod.POST)
public@ResponseBody
Map<String, BigDecimal> getBilledUnbilledAmount(@RequestParam(value="joReleaseId", required= false) Integer joReleaseID,
		 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
	Map<String,BigDecimal> aPrice = new HashMap<String, BigDecimal>();
	try {		
	
		JobReleaseBean joRleaseBean  = itsJobService.getBilledUnbilled(joReleaseID);
		BigDecimal unbilled = BigDecimal.ZERO;
		BigDecimal billed = BigDecimal.ZERO; 
		BigDecimal freightAmt = BigDecimal.ZERO;
		if(joRleaseBean!=null)
		{
			unbilled=joRleaseBean.getEstimatedBilling();
			billed = joRleaseBean.getInvoiceAmount();
			freightAmt = joRleaseBean.getFreight();
		}
		
		aPrice.put("unbilled", unbilled);
		aPrice.put("billed", billed);
		aPrice.put("freightAmt", freightAmt);
	} catch (Exception e) {
		sendTransactionException("<b>joReleaseID:</b>"+joReleaseID,"JOB",e,session,therequest);
		itsLogger.error(e.getMessage(), e);
		theResponse.sendError(500, e.getCause().getMessage());
	}
	
	return aPrice;		
}


@RequestMapping(value="/getCommissionPaidDetails", method = RequestMethod.POST)
public@ResponseBody String getCommissionPaidDetails(
		@RequestParam(value="joReleaseDetailID", required= false) Integer joReleaseDetailID,
		@RequestParam(value="joCuInvoiceID", required= false) Integer cuInvoiceID,
		 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
	List<Ecstatement> ecsList = new ArrayList<Ecstatement>();
	itsLogger.info("CommissionPaid Details");
	String messages = "";
	try {		
		
		ecsList = itsSalesServices.getJobCommissionSplits(joReleaseDetailID);
		messages = itsSalesServices.getCommissionPaidDetails(cuInvoiceID);
	} catch (Exception e) {
		sendTransactionException("<b>joReleaseDetailID,cuInvoiceID:</b>"+joReleaseDetailID+","+cuInvoiceID,"JOB",e,session,therequest);
		itsLogger.error(e.getMessage(), e);
		theResponse.sendError(500, e.getCause().getMessage());
	}
	
	return messages;		
}



		@RequestMapping(value="/getOutsieJobInvoicePaymentDate", method = RequestMethod.POST)
		public@ResponseBody
		String getOutsieJobInvoicePaymentDate(@RequestParam(value="cuInvoiceID", required= false) Integer cuInvoiceID,
				 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
			//JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
			itsLogger.info("getOutsieJobInvoicePaymentDate Paid Details");
			String strDate="";
			Date unFormatDate = null;
			try {		
				
				System.out.println(cuInvoiceID);
				
				unFormatDate = itsSalesServices.getInvoicePaymentDetails(cuInvoiceID);
			//	System.out.println(aJoReleaseDetail.getJoReleaseDetailId()+"-------->");
				if(unFormatDate!=null && !unFormatDate.equals(""))
				{
				strDate = DateFormatUtils.format(unFormatDate,"MM/dd/yyyy");
				itsLogger.info("Date: "+DateFormatUtils.format(unFormatDate,"MM/dd/yyyy hh:mm a"));
				}
			} catch (Exception e) {
				sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage(), e);
				theResponse.sendError(500, e.getCause().getMessage());
			}
			
			return strDate;		
		}


	@RequestMapping(value="/getInvoicePaymentDate", method = RequestMethod.POST)
	public@ResponseBody
	String getInvoicePaymentDate(@RequestParam(value="joReleaseDetailID", required= false) Integer joReleaseDetailID,
			 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		//JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		JoReleaseDetail aJoReleaseDetail = null;
		itsLogger.info("Invoce Paid Details");
		String strDate="";
		try {		
			
			System.out.println(joReleaseDetailID);
			
			aJoReleaseDetail = itsSalesServices.getJoReleaseDetails(joReleaseDetailID);
			
		//	System.out.println(aJoReleaseDetail.getJoReleaseDetailId()+"-------->");
			if(aJoReleaseDetail!=null && aJoReleaseDetail.getPaymentDate()!=null)
			{
			strDate = DateFormatUtils.format(aJoReleaseDetail.getPaymentDate(),"MM/dd/yyyy");
			itsLogger.info("Date: "+DateFormatUtils.format(aJoReleaseDetail.getPaymentDate(),"MM/dd/yyyy hh:mm a")+"joReleaseDetailID:"+aJoReleaseDetail.getJoReleaseDetailId());
			}
		} catch (Exception e) {
			sendTransactionException("<b>joReleaseDetailID:</b>"+joReleaseDetailID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		
		return strDate;		
	}
	
	@RequestMapping(value="/getInvoiceCheckNos", method = RequestMethod.POST)
	public@ResponseBody
	String getInvoiceCheckNos(@RequestParam(value="cuInvoiceID", required= false) Integer cuInvoiceID,
			 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException, MessagingException {
		String checkNo = null;
		itsLogger.info("Invoce Paid Details");
		try {		
			checkNo = itsSalesServices.getPaidCheckRefs(cuInvoiceID);
		} catch (Exception e) {
			sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		
		return checkNo;		
	}
	
	@RequestMapping(value="/getVepoorCusoDetails", method = RequestMethod.POST)
	public@ResponseBody	Map<String, Object> getVepoorCusoDetails(@RequestParam(value="Id", required= false) Integer id,
																 @RequestParam(value="oper", required= false) String oper,
																 HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) 
																 throws JobException, IOException, VendorException, ParseException, MessagingException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		id = id==null?0:id;
		
		try {	
			if(oper.equals("vepo"))
			{
			map.put("Vepo",itsJobService.getVepo(id));
			}
			else
			{
			map.put("Cuso", itsJobService.getCusoObj(id));	
			}
			
			
		} catch (Exception e) {
			sendTransactionException("<b>id:</b>"+id,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
		}
		
		return map;		
	}

	@RequestMapping(value="/getContacts", method = RequestMethod.GET)
	public @ResponseBody	List<Rxcontact>  getContacts(@RequestParam(value="customerID", required= false) Integer id,
																 HttpSession theSession, HttpServletResponse theResponse) 
																 throws JobException{
		List<Rxcontact> alist=new ArrayList<Rxcontact>();
		try {	
			alist=itsRolodexService.getContacts(id);		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		
		return alist;		
	}
	
	@RequestMapping(value="/updatejorealseseqnumber", method = RequestMethod.GET)
	public @ResponseBody String  updatejorealseseqnumber(HttpSession theSession, HttpServletResponse theResponse) 
																 throws JobException{
		try {	
			itsJobService.updatejorealseseqnumber();		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return "success";		
	}

	
	
	@RequestMapping(value="/getCustomerInvoiceDetailsForPopup", method = RequestMethod.POST)
	public @ResponseBody Cuinvoice getCustomerInvoiceDetailsForPopup(@RequestParam(value="customerinvoiceID", required = false) Integer aInvoiceID,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Cuinvoice aCuinvoice = new Cuinvoice();
		try{
			if(aInvoiceID != null){
				aCuinvoice = itsJobService.getCustomerInvoiceDetails(aInvoiceID);
				if(aCuinvoice.getInvoiceNumber() != null && aCuinvoice.getInvoiceNumber().length() > 0)
				{
					String poNumber = aCuinvoice.getInvoiceNumber();
					
					char a = poNumber.charAt(poNumber.length()-1);
					itsLogger.info("New InvoiceNumber"+a);
					if(Character.isLetter(a))
					{
						//poNumber = poNumber+"1";
						poNumber = poNumber;
						aCuinvoice.setInvoiceNumber(poNumber);
					}
					else if(Character.isDigit(a))
					{
						int aa = Character.getNumericValue(a);
						itsLogger.info("New InvoiceNumber"+aa);
						int i = Character.getNumericValue(a);
						//i+= 1;
						poNumber = poNumber.substring(0, poNumber.length()-1)+i;
						aCuinvoice.setInvoiceNumber(poNumber);
						
					}
				}
			}
		}catch (JobException e) {
			sendTransactionException("<b>aInvoiceID:</b>"+aInvoiceID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCuinvoice;
	}
	@RequestMapping(value="/manpulateporeleaselineitem", method = RequestMethod.POST)
	public @ResponseBody Boolean manpulateporeleaselineitem(@RequestParam(value="note", required= false) String theItemCode,
			@RequestParam(value="description", required= false) String thedescription,
			@RequestParam(value="quantityOrdered", required= false) String thequantityOrdered,
			@RequestParam(value="unitCost", required= false) String theunitCost,
			@RequestParam(value="priceMultiplier", required= false) String thepriceMultiplier,
			@RequestParam(value="taxable", required= false) String thetaxable,
			@RequestParam(value="quantityBilled", required= false) String thequantityBilled,
			@RequestParam(value="vePoid", required= false) String thevePoid,
			@RequestParam(value="prMasterId", required= false) String theprMasterId,
			@RequestParam(value="vePodetailId", required= false) String thevePodetailId,
			@RequestParam(value="subtractedquantity", required= false) String thesubtractedquantity,
			//@RequestParam(value="posistion", required= false) String theposistion,
			//@RequestParam(value="upAndDown", required= false) String theupAndDown,
			//@RequestParam(value="validatequantity", required= false) String thevalidatequantity,
			HttpSession theSession, HttpServletResponse theResponse) throws JobException, IOException, VendorException, ParseException {
		BigDecimal quantityOrdered=JobUtil.ConvertintoBigDecimal(thequantityOrdered);
		BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(theunitCost.replaceAll("\\$",""));
		BigDecimal quantityBilled=JobUtil.ConvertintoBigDecimal(thequantityBilled.replaceAll("\\$",""));
		BigDecimal subtractedquantity=JobUtil.ConvertintoBigDecimal(thesubtractedquantity);
		BigDecimal pricemultiplier=JobUtil.ConvertintoBigDecimal(thepriceMultiplier);
		boolean taxable=false;
		if(thetaxable.equalsIgnoreCase("yes")){
			taxable=true;
		}
		Integer vePoid=JobUtil.ConvertintoInteger(thevePoid);
		Integer prMasterId=JobUtil.ConvertintoInteger(theprMasterId);
		Integer vePodetailId=JobUtil.ConvertintoInteger(thevePodetailId);
		System.out.println("vePodetailId=="+vePodetailId);
		//Double posistion=JobUtil.ConvertintoDouble(theposistion);
		if(vePodetailId>0){
		if(quantityOrdered.compareTo(subtractedquantity)==1){
			Vepodetail vepod=new Vepodetail();
			vepod.setQuantityOrdered(quantityOrdered.subtract(subtractedquantity));
			vepod.setVePoid(vePoid);
			vepod.setVePodetailId(vePodetailId);
			boolean returnvalue=itsJobService.updateVepoDetailquantity(vepod);
			//update vepodetail
		}
		}else{
			Vepodetail vepod=new Vepodetail();
			vepod.setDescription(thedescription);
			vepod.setQuantityOrdered(quantityOrdered);
			vepod.setUnitCost(unitCost);
			vepod.setPriceMultiplier(pricemultiplier);
			vepod.setTaxable(taxable);
			vepod.setQuantityBilled(quantityBilled);
			vepod.setNote("");
			vepod.setPrMasterId(prMasterId);
			vepod.setVePoid(vePoid);
			Integer returninteger=itsJobService.createvepodetailfromvendorinvoice(vepod);
			//insertintovepodetail
			
		}
		
		return true;
	}
	
	@RequestMapping(value="/getCustomerinvoicejobDetail", method = RequestMethod.GET)
	public @ResponseBody Cuinvoice getCustomerinvoicejobDetail(@RequestParam(value="jomasterid", required = false) Integer jomasterid,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Cuinvoice aCuinvoice=new Cuinvoice();
		try {
			aCuinvoice.setJoMasterID(jomasterid);
			aCuinvoice=itsJobService.getCustomerinvoicejobDetail(aCuinvoice);
		} catch (JobException e) {
			sendTransactionException("<b>jomasterid:</b>"+jomasterid,"JOB",e,session,therequest);
			// TODO Auto-generated catch block
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCuinvoice;
	}
	
	@RequestMapping(value="/getCustomerOverallDetail", method = RequestMethod.GET)
	public @ResponseBody Cumaster getCustomerOverallDetail(@RequestParam(value="customerid", required = false) Integer customerid,
													   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Cumaster aCumaster=new Cumaster();
		System.out.println("Test::::::::::::");
		
		try {
			setCustomerRecord(customerid);
			aCumaster=getCustomerRecord();
			aCumaster.setCurrentDate(accountingCyclesService.getCurrentServerDate());
			
		} catch (Exception e) {
			sendTransactionException("<b>customerid:</b>"+customerid,"JOB",e,session,therequest);
			// TODO Auto-generated catch block
			itsLogger.error(e.getMessage());
		}
		return aCumaster;
	}
	
	
	@RequestMapping(value="/updateHoldOveriteDetails", method = RequestMethod.GET)
	public @ResponseBody Cumaster updateHoldOveriteDetails(@RequestParam(value="customerid", required = false) Integer customerid,
													   HttpSession theSession, HttpServletResponse theResponse) throws ParseException, IOException{
		Cumaster aCumaster=new Cumaster();
		try {
			itsJobService.updatecuMasterOverriteCreditHold(customerid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			itsLogger.error(e.getMessage());
		}
		return aCumaster;
	}
	
	@RequestMapping(value="/getReleaseDetail", method = RequestMethod.GET)
	public @ResponseBody JoRelease getReleaseDetail(@RequestParam(value="joReleaseID", required = false) Integer joReleaseID,
													   HttpSession theSession, HttpServletResponse theResponse) throws ParseException, IOException{
		JoRelease aJoRelease=new JoRelease();
		try {
			aJoRelease = itsJobService.getJoRelease(joReleaseID);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			itsLogger.error(e.getMessage());
		}
		return aJoRelease;
	}
	
//	@RequestMapping(value = "/rxMasterList", method = RequestMethod.GET)
//	public @ResponseBody List<?> getrxMasterDetails(	@RequestParam("term") String term, HttpServletResponse theResponse) {
//		itsLogger.info("Received request to get search Jobs Lists = "+term);
//		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
//		try {
//			itsLogger.info("term >>>>>>> = "+term);
//			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) itsRolodexService.getCustomerNameList(term);
//			itsLogger.info("term = "+term+" || aProductCodeWithNameList.size() = "+aProductCodeWithNameList.size());
//		} catch (Exception e) {
//			itsLogger.error(e.getMessage());
//		}
//		return aProductCodeWithNameList;
//	}
	
	@RequestMapping(value = "/rxAddressInfo", method = RequestMethod.GET)
	public @ResponseBody Rxaddress getAddress(@RequestParam("rxMasterID") Integer rxMasterID,HttpServletRequest therequest,HttpSession session, HttpServletResponse theResponse) throws IOException, MessagingException {
		itsLogger.debug("Received request to get search Jobs Lists >>>>>>>>>>> rxMasterID = "+rxMasterID);
		Rxaddress rxAddress = new Rxaddress();
		try {
			if(rxMasterID!=null)
			rxAddress = itsRolodexService.getAddress(rxMasterID);
		} catch (Exception e) {
			sendTransactionException("<b>rxMasterID:</b>"+rxMasterID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
		}
		return rxAddress;
	}
	
	@RequestMapping(value = "/checkxmlparser", method = RequestMethod.POST)
	public @ResponseBody List<Vepodetail> upload(MultipartHttpServletRequest request, HttpServletResponse response,HttpServletRequest therequest,HttpSession session) throws IOException, ParserConfigurationException, SAXException, MessagingException, VendorException {                 
	 
		int prmasterid=itsJobService.getStarProductIDforXMLUpload();
		
		String returnValue="<b style='color:green;'>Success</b>";
		List<Vepodetail> lsvepoDetail=new ArrayList<Vepodetail>();
		//0. notice, we have used MultipartHttpServletRequest
	 
		//1. get the files from the request object
		Iterator<String> itr =  request.getFileNames();

		MultipartFile mpf = request.getFile(itr.next());
		System.out.println("mpf "+mpf);
		String vepoId = request.getParameter("vepoId1");
		System.out.println(vepoId);
		System.out.println(mpf.getOriginalFilename() +" uploaded!");
		InputStream fis = mpf.getInputStream();
		int theVepoId=Integer.valueOf(vepoId);
		String manID= request.getParameter("MANId1");
		int ManufacturerID=JobUtil.ConvertintoInteger(manID);
		ArrayList<Vemaster> avemasterlst=itsVendorService.getVendorMaster(String.valueOf(ManufacturerID));
		Vepo aVepo = new Vepo();
		aVepo.setVePoid(theVepoId);
		
		System.out.println("theVepoId "+theVepoId);
		System.out.println("mpf "+fis);

		try {
			
			 System.out.println("inside xmlparser");
			//	File fXmlFile = new File("C:/Users/elancheziyan/Desktop/ORD-N0529-501.xml");
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fis);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
			 
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				
			 
				System.out.println("----------------------------");
				if(avemasterlst!=null){
					Vemaster avemaster=avemasterlst.get(0);
					if(doc.getDocumentElement().getNodeName().equals("ORDER") && avemaster.getImportType()==5){
						System.out.println("Inside first method");
						lsvepoDetail=insertxmltype1(doc,prmasterid,theVepoId);
					}else if(doc.getDocumentElement().getNodeName().equals("Project")&& avemaster.getImportType()==6){
						System.out.println("Inside Second method");
						lsvepoDetail=insertxmltype2(doc,prmasterid,theVepoId);
					}else if(avemaster.getImportType()==-1 || avemaster.getImportType()==0){
						returnValue="<b style='color:red;'>Import Type Should Select in vendor</b>";
					}else{
						returnValue="<b style='color:red;'>Incorrect Xml File</b>";
					}
				}else{
					returnValue="<b style='color:red;'>There is no vendor</b>";
				}
				Vepodetail vpd=new Vepodetail();
				vpd.setDescription(returnValue);
				lsvepoDetail.add(vpd);
			    } catch (IOException e) {
			    	sendTransactionException("<b>MethodName:</b>checkxmlparser","JOB",e,session,therequest);
			    	itsLogger.error(e.getMessage(), e);
			response.sendError(500, "IO exception occurred:" + e.getMessage());
		}
		finally
		{
			fis.close();
		}
	 
		return lsvepoDetail;

	}
	@RequestMapping(value = "/checkImportTypeSelectornot", method = RequestMethod.POST)
	public @ResponseBody Boolean checkImportTypeSelectornot(@RequestParam("manufactureid") Integer manufactureid,
			HttpServletResponse response,HttpServletRequest therequest,HttpSession session)throws VendorException {                 
	 Boolean returnValue=false;
	
		ArrayList<Vemaster> avemasterlst=itsVendorService.getVendorMaster(String.valueOf(manufactureid));
		if(avemasterlst!=null){
			Vemaster avemaster=avemasterlst.get(0);
			if(avemaster.getImportType()==null || avemaster.getImportType()==-1 || avemaster.getImportType()==0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}else{
			returnValue=true;
		}
	 
		return returnValue;

	}
	
	
	
	@RequestMapping(value = "/CheckinvoiceNumberavlforvendor", method = RequestMethod.GET)
	public @ResponseBody Boolean CheckinvoiceNumberavlforvendor(@RequestParam("vendorID") Integer vendorID,
			@RequestParam("Invnumber") String Invnumber,
			HttpServletResponse response,HttpServletRequest therequest,HttpSession session)throws VendorException {                 
	 Boolean returnValue=false;
	
	 if(!Invnumber.equals(""))
		 returnValue=itsVendorService.getCheckinvoiceNumberavlforvendor(vendorID,Invnumber);
		
		return returnValue;

	}
	

	@RequestMapping(value = "/Checklineitemvalidation", method = RequestMethod.POST)
	public @ResponseBody Boolean Checklineitemistherevalidation(@RequestParam("type") Integer type,
			@RequestParam("valeID") Integer primaryID,
			HttpServletResponse response,HttpServletRequest therequest,HttpSession session)throws VendorException {                 
	 Boolean returnValue=false;
	
		 returnValue=itsVendorService.getChecklineitemisthereforrelease(type,primaryID);
		
		return returnValue;

	}
	
	
	/*@RequestMapping(value="checkxmlparser", method = RequestMethod.POST)
	public @ResponseBody void checkxmlparser(@RequestParam("aVepoId") Integer theVepoId, HttpServletResponse theResponse){
		
		int prmasterid=itsJobService.getStarProductIDforXMLUpload();
		
		Vepodetail aVepodetail = new Vepodetail();
		Vepo aVepo = new Vepo();
		aVepo.setVePoid(theVepoId);
		
		System.out.println("theVepoId "+theVepoId);
		

		 try {
			 System.out.println("inside xmlparser");
				File fXmlFile = new File("C:/Users/elancheziyan/Desktop/CSY150418A.xml");
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
			 
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				NodeList nList = doc.getElementsByTagName("SalesBill");
			 
				System.out.println("----------------------------");
			 
				for (int temp = 0; temp < nList.getLength(); temp++) {
			 
					Node nNode = nList.item(temp);
			 
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
						Element eElement = (Element) nNode;
						
						System.out.println(eElement.getAttribute("Description"));
						//System.out.println(new BigDecimal(eElement.getAttribute("ZMULTIPLY")));
						
						aVepodetail.setItemCode("*");
						aVepodetail.setDescription(eElement.getAttribute("CITEMNO"));
						aVepodetail.setQuantityOrdered(new BigDecimal(eElement.getAttribute("NORDQTY")));
						aVepodetail.setUnitCost(new BigDecimal(eElement.getAttribute("NPRICE")));
						aVepodetail.setPriceMultiplier(new BigDecimal(eElement.getAttribute("ZMULTIPLY")));
						aVepodetail.setPrMasterId(prmasterid);
						aVepodetail.setVePoid(theVepoId);
						aVepodetail.setTaxable(false);
						
						//itsJobService.saveXMLVepoDetail(aVepodetail);
						//aVepodetail.setTaxable(theTax);
			 
						System.out.println();
			 
					}
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
	}
		
		 try {
			   SampleXMLLoop();
			   
		   } catch (Exception e) {
			e.printStackTrace();
		   }
		 }

		private static void SampleXMLLoop() {
			try {
				 
				File file = new File("C:/Users/elancheziyan/Desktop/CSY150418A.xml");
			 
				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
			                             .newDocumentBuilder();
			 
				Document doc = dBuilder.parse(file);
			 
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				if (doc.hasChildNodes()) {
			 
					printNote(doc.getChildNodes());
			 
				}
			 
			    } catch (Exception e) {
				System.out.println(e.getMessage());
			    }
		}

		private static void printNote(NodeList nodeList) {
			 
		    for (int count = 0; count < nodeList.getLength(); count++) {
		 
			Node tempNode = nodeList.item(count);
		 
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
		 
				// get node name and value
				System.out.println("\n Node Name =" + tempNode.getNodeName() + "\n [OPEN]");
				System.out.println("Node Value =" + tempNode.getTextContent());
		 
				if (tempNode.hasAttributes()) {
		 
					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();
		 
					for (int i = 0; i < nodeMap.getLength(); i++) {
		 
						Node node = nodeMap.item(i);
						System.out.println("Label: " + node.getNodeName() + "\t : \t Value:" +node.getNodeValue());
						//System.out.println("attr value : " + node.getNodeValue());
		 
					}
		 
				}
		 
				if (tempNode.hasChildNodes()) {
		 
					// loop again if has child nodes
					printNote(tempNode.getChildNodes());
		 
				}
		 
				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
		 
			}
		 
		    }
	}*/
	public void sendTransactionException(String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		UserBean aUserBean=null;
		TsUserSetting objtsusersettings=null;
		try{
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
		 StringWriter errors = new StringWriter();
		 e.printStackTrace(new PrintWriter(errors));
		if(objtsusersettings.getItsmailYN()==1){
		Transactionmonitor transObj =new Transactionmonitor();
		 SendQuoteMail sendMail = new SendQuoteMail();
		 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
		 transObj.setTrackingId(trackingID);
		 transObj.setTimetotriggerd(new Date());
		 transObj.setJobStatus(jobstatus);
		 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
		 transObj.setDescription("Message :: " + errors.toString());
		 sendMail.sendTransactionInfo(transObj,therequest);
		}
		
		if(objtsusersettings.getItslogYN()==1){
			TpUsage aTpusage=new TpUsage(new Date(), jobstatus,trackingID,"Error",aUserBean.getUserId(),"Message :: " + errors.toString());
			itsUserService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	
	public List<Vepodetail> insertxmltype1(Document doc,int prmasterid,int theVepoId){
		List<Vepodetail> lstvepodetail=new ArrayList<Vepodetail>();
		
		NodeList nList = doc.getElementsByTagName("LINEITEM");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Vepodetail aVepodetail = new Vepodetail();
			Node nNode = nList.item(temp);
	 
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				System.out.println(eElement.getAttribute("ZMULTIPLY"));
				System.out.println(new BigDecimal(eElement.getAttribute("ZMULTIPLY")));
				
				aVepodetail.setItemCode("*");
				aVepodetail.setDescription(eElement.getAttribute("CITEMNO"));
				aVepodetail.setQuantityOrdered(new BigDecimal(eElement.getAttribute("NORDQTY")));
				aVepodetail.setUnitCost(new BigDecimal(eElement.getAttribute("NPRICE")));
				aVepodetail.setPriceMultiplier(new BigDecimal(eElement.getAttribute("ZMULTIPLY")));
				aVepodetail.setPrMasterId(prmasterid);
				aVepodetail.setVePoid(theVepoId);
				aVepodetail.setTaxable(false);
				aVepodetail.setInLineNote("");
				lstvepodetail.add(aVepodetail);
				//itsJobService.saveXMLVepoDetail(aVepodetail);
				//aVepodetail.setTaxable(theTax);
	 
				System.out.println();
	 
			}
		}
		return lstvepodetail;
	}
	
	public List<Vepodetail> insertxmltype2(Document doc,int prmasterid,int theVepoId) throws ParserConfigurationException, SAXException, IOException{
		List<Vepodetail> lstvepodetail=new ArrayList<Vepodetail>();
		NodeList nList = doc.getElementsByTagName("Product");
		System.out.println("nList.getLength()"+nList.getLength());
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Vepodetail aVepodetail = new Vepodetail();
			Node nNode = nList.item(temp);
	 
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
	 
				Element eElement = (Element)nList.item(temp);
				
				//listAllAttributes(eElement);
				System.out.println("LongModelName"+getTextValue(eElement,"LongModelName")+"\n Quantity"+eElement.getAttribute("Quantity")+"\n ListPrice"+eElement.getAttribute("ListPrice")+"\n PricingMultiplier"+eElement.getAttribute("PricingMultiplier"));
				aVepodetail.setItemCode("*");
				aVepodetail.setDescription(getTextValue(eElement,"LongModelName"));
				aVepodetail.setQuantityOrdered(JobUtil.floorFigureoverall(JobUtil.ConvertintoBigDecimal(getTextValue(eElement,"Quantity")),2));
				aVepodetail.setUnitCost(JobUtil.floorFigureoverall(JobUtil.ConvertintoBigDecimal(getTextValue(eElement,"ListPrice")),2));
				aVepodetail.setPriceMultiplier(JobUtil.ConvertintoBigDecimal(getTextValue(eElement,"PricingMultiplier")));
				aVepodetail.setPrMasterId(prmasterid);
				aVepodetail.setVePoid(theVepoId);
				aVepodetail.setTaxable(false);
				String description=getTextValue(eElement,"ProductDescription");
				String inlinenote=getinlinenotefromXML(nNode,eElement);
				inlinenote="<span style=\"font-family: 'Times New Roman', serif; font-size: 10pt;\">"+description+"</span>"+inlinenote;
				aVepodetail.setInLineNote(inlinenote);
				aVepodetail.setInLineNoteImage(inlinenote);
				
				
				
				lstvepodetail.add(aVepodetail);
				//itsJobService.saveXMLVepoDetail(aVepodetail);
				//aVepodetail.setTaxable(theTax);
	 
				System.out.println();
	 
			
		}
		return lstvepodetail;
	}
	public static void listAllAttributes(Element element) {
			
			System.out.println("List attributes for node: " + element.getNodeName());
			
			// get a map containing the attributes of this node 
			NamedNodeMap attributes = element.getAttributes();
	
			// get the number of nodes in this map
			int numAttrs = attributes.getLength();
	
			for (int i = 0; i < numAttrs; i++) {
				Attr attr = (Attr) attributes.item(i);
				
				String attrName = attr.getNodeName();
				String attrValue = attr.getNodeValue();
				
				System.out.println("Found attribute: " + attrName + " with value: " + attrValue);
				
			}
		}
	
		private String getTextValue(Element ele, String tagName) {
			String textVal = null;
			NodeList nl = ele.getElementsByTagName(tagName);
			if(nl != null && nl.getLength() > 0) {
				Element el = (Element)nl.item(0);
				textVal = el.getFirstChild().getNodeValue();
			}
	
			return textVal;
		}
		private String getinlinenotefromXML(Node anode, Element alement) throws ParserConfigurationException, SAXException, IOException {
			String textVal = null;
			NodeList nl = alement.getElementsByTagName("Question");
			System.out.println("nl.getLength()==="+nl.getLength());
			for(int i=0;i<nl.getLength();i++){
				if((((Element)nl.item(i)).getAttribute("Name")).equals("SalesBillXML")){
					Element eElement =(Element)nl.item(i);
					String SalesXML=getTextValue(eElement,"Answer");
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				    DocumentBuilder db = dbf.newDocumentBuilder();
				    InputSource is = new InputSource();
				    is.setCharacterStream(new StringReader(SalesXML));
				    Document doc = db.parse(is);
				    textVal=GetInlineNoteFromStringXML(doc);
				}
			}
			
			return textVal;
		}
		
		public String GetInlineNoteFromStringXML(Document doc){
			String returnValue="";
			NodeList nList = doc.getElementsByTagName("LineItems");
			System.out.println("nList.getLength()"+nList.getLength());
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Element eElement = (Element)nList.item(temp);
				NodeList nl = eElement.getElementsByTagName("LineItems");
				for (int temp1 = 0; temp1 < nl.getLength(); temp1++) {
					Element insElement = (Element)nl.item(temp1);
					NodeList nestedele = insElement.getElementsByTagName("LineItem");
					for (int temp2 = 0; temp2 < nestedele.getLength(); temp2++) {
						Element nested1Element = (Element)nestedele.item(temp2);
						String text=getTextValue(nested1Element,"Description");
						returnValue=returnValue+"<br><span style=\"font-family: 'Times New Roman', serif; font-size: 10pt;\">"+text+"</span>";
					}
				}
				
			}
			System.out.println("HTML INPUT===>"+returnValue);
			return returnValue;
		}
		
		
		@RequestMapping(value = "jobReleaseLineItemForrecieveInventory",method = RequestMethod.POST)
		public @ResponseBody CustomResponse jobReleaseLineItemForrecieveInventory(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
																		HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
			CustomResponse aResponse = new CustomResponse();
			try{
				List<Vepodetail> aPOLineItemDetails = itsJobService.jobReleaseLineItemForrecieveInventory(theVepoId);
							aResponse.setRows(aPOLineItemDetails);
			}catch (JobException e) {
				sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}
			return aResponse;
		}	
		
		public Boolean manpulateCuInvoiceReleaseLineItem(
				 Integer cuInvoiceID,String desc,String itemCode,String note,String Oper,Integer prMasterID,
				BigDecimal priceMultiplies,BigDecimal quantityOrder,String Taxable,BigDecimal unitCost,Integer cuInvoiceDetailId,
				Double taxRate,BigDecimal freight,String releasetype,Integer userID,String userName)
				throws IOException, MessagingException {
			Cuinvoicedetail aCuInvDetObj = new Cuinvoicedetail();
			Cuinvoice aCuInvObj = new Cuinvoice();
			Cuinvoicedetail aCusodetailObj = null;
			boolean saved = false;
			BigDecimal[] taxandTotal = new BigDecimal[3];
			BigDecimal[] editTaxTotal = new BigDecimal[3];
			
			aCuInvObj.setReleaseType(releasetype);
			if(unitCost==null)
				unitCost = BigDecimal.ZERO;
			if(quantityOrder == null)
				quantityOrder = BigDecimal.ZERO;
			if(priceMultiplies == null)
				priceMultiplies = BigDecimal.ZERO;

			try {
				if (null != cuInvoiceDetailId)
					aCusodetailObj = itsJobService
							.getSingleCuInvoiceDetailObj(cuInvoiceDetailId);
				
					if (Oper.equalsIgnoreCase("del")) {
						if(aCusodetailObj!=null)
						{
						aCuInvDetObj.setCuInvoiceDetailId(cuInvoiceDetailId);
						taxandTotal = computeTaxTotal(aCusodetailObj.getUnitCost(),
								aCusodetailObj.getPriceMultiplier(),
								aCusodetailObj.getQuantityBilled(),
								aCusodetailObj.getTaxable(), taxRate, freight);
						aCuInvObj.setTaxTotal(taxandTotal[1]);
						aCuInvObj.setCuInvoiceId(cuInvoiceID);
						aCuInvDetObj.setUserID(userID);
						aCuInvDetObj.setUserName(userName);
						saved = itsJobService.deleteCUInvReleaseLineItem(aCuInvDetObj,
								aCuInvObj);
						}
				
				} else {
					aCuInvDetObj.setDescription(desc);
					aCuInvDetObj.setPrMasterId(prMasterID);
					aCuInvDetObj.setPriceMultiplier(priceMultiplies);
					aCuInvDetObj.setQuantityBilled(quantityOrder);
					if (Taxable != null) {
						if ( Taxable.equals("Yes")) {
							aCuInvDetObj.setTaxable((byte) 1);
						} else {
							aCuInvDetObj.setTaxable((byte) 0);
						}
					}
					
			
					
					aCuInvDetObj.setUnitCost(unitCost);
					aCuInvDetObj.setNote(note);
					aCuInvDetObj.setCuInvoiceId(cuInvoiceID);
					taxandTotal = computeTaxTotal(unitCost, priceMultiplies,
							quantityOrder, aCuInvDetObj.getTaxable(), taxRate,
							freight);
					aCuInvObj.setCuInvoiceId(cuInvoiceID);
					aCuInvObj.setTaxTotal(taxandTotal[1]);
					aCuInvObj.setSubtotal(taxandTotal[0]);
					aCuInvObj.setCostTotal(taxandTotal[2]);
					aCuInvDetObj.setUserID(userID);
					aCuInvDetObj.setUserName(userName);
					if (Oper.equalsIgnoreCase("add")) {
						saved = itsJobService.addcuinvoiceReleaseLineItem(
								aCuInvDetObj, aCuInvObj);
					} else if (Oper.equalsIgnoreCase("edit")) {
						Cuinvoice editCuInvObj = new Cuinvoice();
						editCuInvObj.setCuInvoiceId(cuInvoiceID);
						byte oldTaxEnabled = aCusodetailObj.getTaxable()==null?0:aCusodetailObj.getTaxable();
						byte newTaxEnabled = aCuInvDetObj.getTaxable()==null?0:aCuInvDetObj.getTaxable();
						editTaxTotal = computeTaxTotal(
								aCusodetailObj.getUnitCost(),
								aCusodetailObj.getPriceMultiplier(),
								aCusodetailObj.getQuantityBilled(),
								aCusodetailObj.getTaxable(), taxRate, freight);
						editCuInvObj.setTaxTotal(editTaxTotal[1]);
						editCuInvObj.setCostTotal(editTaxTotal[2]);
						editCuInvObj.setSubtotal(editTaxTotal[0]);
						
						if (oldTaxEnabled != newTaxEnabled) {
							if (oldTaxEnabled == 0 && newTaxEnabled == 1) {
								taxandTotal = computeTaxTotal(unitCost,
										priceMultiplies, quantityOrder,
										newTaxEnabled, taxRate, freight);
								aCuInvObj.setTaxTotal(taxandTotal[1]);
								aCuInvObj.setCostTotal(taxandTotal[2]);
								aCuInvObj.setSubtotal(taxandTotal[0]);
							} else {
								taxandTotal = computeTaxTotal(unitCost,
										priceMultiplies, quantityOrder,
										newTaxEnabled, taxRate, freight);
								aCuInvObj.setTaxTotal(taxandTotal[1].negate());
								aCuInvObj.setCostTotal(taxandTotal[2]);
								aCuInvObj.setSubtotal(taxandTotal[0]);
							}
						}
						aCuInvDetObj.setNote(note);
						aCuInvDetObj.setCuInvoiceDetailId(cuInvoiceDetailId);
						aCuInvObj.setReleaseType(releasetype);
						saved = itsJobService.editCuInvoiceReleaseLineItem(
								aCuInvDetObj, aCuInvObj, editCuInvObj);
					}
				}
					
			} catch (JobException e) {
				e.printStackTrace();
			}
			return saved;
		}
		
		@RequestMapping(value = "/SaveLinesPurchaseOrder", method = RequestMethod.POST)
		public @ResponseBody String saveSOReleaseLineItem(
				@RequestParam(value = "vePOID", required = false) Integer vePOID,
				/*@RequestParam(value = "description", required = false) String desc,
				@RequestParam(value = "itemCode", required = false) String itemCode,
				@RequestParam(value = "note", required = false) String note,
				@RequestParam(value = "oper", required = false) String Oper,
				@RequestParam(value = "prMasterId", required = false) Integer prMasterID,
				@RequestParam(value = "priceMultiplier", required = false) BigDecimal priceMultiplies,
				@RequestParam(value = "quantityOrdered", required = false) BigDecimal quantityOrder,
				@RequestParam(value = "taxable", required = false) String Taxable,
				@RequestParam(value = "unitCost", required = false) BigDecimal unitCost,
				@RequestParam(value = "cuSodetailId", required = false) Integer cuSODetailID,
				@RequestParam(value = "taxRate", required = false) Double taxRate,*/
				@RequestParam(value = "frieght", required = false) BigDecimal freight,
				@RequestParam(value = "subTotal", required = false) BigDecimal subTotal,
				@RequestParam(value = "taxValue", required = false) BigDecimal taxValue,
				@RequestParam(value = "gridData",required = false) String gridData,
				@RequestParam(value = "DelPOData[]",required = false) ArrayList<String>  delData,
				HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
				throws IOException, JobException, MessagingException, ParseException {
			JsonParser parser = new JsonParser();
			if (delData!=null && delData.size()>0) {
				for(String detailID:delData){
					Integer cusoDetailID=JobUtil.ConvertintoInteger(detailID);
					Vepodetail aVepodetail=new Vepodetail();
					aVepodetail.setVePodetailId(cusoDetailID);
					aVepodetail.setVePoid(vePOID); 
					aVepodetail.setUserId(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
					aVepodetail.setUserName(((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());
					itsJobService.addPurchaseORderLineItem(aVepodetail, "delete");
				}
			}
			if ( gridData!=null) {

				System.out.println("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				System.out.println("array length==>"+array.size());
				BigDecimal whcostTotalAmount=BigDecimal.ZERO;
				int i=1;
				for (JsonElement ele1 : array) {
					boolean saved = false;
					Vepodetail aVepodetail=new Vepodetail();
					JsonObject obj = ele1.getAsJsonObject();
					
					//String itemCode=obj.get("note").getAsString();
					String desc=obj.get("description").getAsString();
					BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
					BigDecimal quantityReceived=BigDecimal.ZERO;
					if(obj.get("quantityReceived")!=null && obj.get("quantityReceived").getAsString()!=""&& obj.get("quantityReceived").getAsString().length()>0 )
					{
						quantityReceived=obj.get("quantityReceived").getAsBigDecimal();
					}
					String invoicedAmount_String=obj.get("invoicedAmount").getAsString().replaceAll("\\$", "");
					invoicedAmount_String=invoicedAmount_String.replaceAll(",", "");
					//BigDecimal invoicedAmount=JobUtil.ConvertintoBigDecimal(invoicedAmount_String);;
					String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
					unitCost_String=unitCost_String.replaceAll(",", "");
					BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
					BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
					String Taxable=obj.get("taxable").getAsString();
					if (Taxable != null) {
						if (Taxable.equalsIgnoreCase("yes")) {
							aVepodetail.setTaxable(true);
						} else {
							aVepodetail.setTaxable(false);
						}
					}
					
					Integer vePoId=null;
					if(obj.get("vePoid")!=null && obj.get("vePoid").getAsString()!=""&& obj.get("vePoid").getAsString().length()>0){
						vePoId=obj.get("vePoid").getAsInt();
					}
					Integer prMasterId=obj.get("prMasterId").getAsInt();
					Integer vePodetailId=null;
					if(obj.get("vePodetailId")!=null && obj.get("vePodetailId").getAsString()!=""&& obj.get("vePodetailId").getAsString().length()>0){
						vePodetailId=obj.get("vePodetailId").getAsInt();
					}
					String ackDate_String=obj.get("ackDate").getAsString();
					String shipDate_String=obj.get("shipDate").getAsString();
					String vendorOrderNumber=obj.get("vendorOrderNumber").getAsString();
					String inLineNote=obj.get("inLineNote").getAsString();
					
					aVepodetail.setDescription(desc);
					aVepodetail.setQuantityOrdered(quantityOrder);
					aVepodetail.setUnitCost(unitCost);
					aVepodetail.setPriceMultiplier(priceMultiplier);
					
					
					
					//Andale Mono=andale mono,times;Arial=Arial,sans-serif;Courier New=Courier New;Helvetica=helvetica;Symbol=Symbol;
					//Tahoma=Tahoma,sans-serif;
					//Times New Roman=Times New Roman,serif;Verdana=Verdana,sans-serif;
					if(inLineNote!=null){
						inLineNote=inLineNote.replaceAll("'Times New Roman', serif;", "Times New Roman, serif;");
						inLineNote=inLineNote.replaceAll("'andale mono', times;", "andale mono, times;");
						inLineNote=inLineNote.replaceAll("'Arial', sans-serif;", "Arial,sans-serif;");
						inLineNote=inLineNote.replaceAll("'Courier New';", "Courier New;");
						inLineNote=inLineNote.replaceAll("'helvetica';", "helvetica;");
						inLineNote=inLineNote.replaceAll("'Symbol';", "Symbol;");
						inLineNote=inLineNote.replaceAll("'Tahoma', sans-serif;", "Tahoma, sans-serif;");
						inLineNote=inLineNote.replaceAll("'Verdana', sans-serif;", "Verdana, sans-serif;");
					}
					aVepodetail.setNote(inLineNote);
					
					if(shipDate_String != null && !shipDate_String.equals("")){
						System.out.println( " shipdate = "+DateUtils.parseDate(shipDate_String, new String[]{"MM/dd/yyyy"}));
						aVepodetail.setEstimatedShipDate(DateUtils.parseDate(shipDate_String, new String[]{"MM/dd/yyyy"}));
					}
					if(ackDate_String != null && !ackDate_String.equals("")){
						System.out.println(" ackdate = "+DateUtils.parseDate(ackDate_String, new String[]{"MM/dd/yyyy"}));
						aVepodetail.setAcknowledgedDate(DateUtils.parseDate(ackDate_String, new String[]{"MM/dd/yyyy"}));
					}
					aVepodetail.setVendorOrderNumber(vendorOrderNumber);
					aVepodetail.setPrMasterId(prMasterId);
					aVepodetail.setVePoid(vePoId);
					aVepodetail.setPosistion(i);
					aVepodetail.setUserId(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
					aVepodetail.setUserName(((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());
					String Oper="add";
					if(vePodetailId!=null){
						Oper="edit";
						aVepodetail.setVePodetailId(vePodetailId);
					}
					itsJobService.addPurchaseORderLineItem(aVepodetail, Oper);
					i=i+1;
				}
			}
			Vepo avepo=new Vepo();
			avepo.setVePoid(vePOID);
			avepo.setSubtotal(subTotal);
			avepo.setTaxTotal(taxValue);
			avepo.setFreight(freight);
			
			
			itsJobService.updatevePOfromlinesTab(avepo);
			return null;
		}
		
		@RequestMapping(value = "/SaveAckPurchaseOrder", method = RequestMethod.POST)
		public @ResponseBody String SaveAckPurchaseOrder(
				@RequestParam(value = "gridData",required = false) String gridData,
				HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
				throws IOException, JobException, MessagingException, ParseException {
			JsonParser parser = new JsonParser();
			if ( gridData!=null) {
				System.out.println("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				System.out.println("array length==>"+array.size());
				BigDecimal whcostTotalAmount=BigDecimal.ZERO;
				for (JsonElement ele1 : array) {
					boolean saved = false;
					Vepodetail aVepodetail=new Vepodetail();
					JsonObject obj = ele1.getAsJsonObject();
					
					Integer vePodetailId=null;
					if(obj.get("vePodetailId")!=null && obj.get("vePodetailId").getAsString()!=""&& obj.get("vePodetailId").getAsString().length()>0){
						vePodetailId=obj.get("vePodetailId").getAsInt();
					}
					String ackDate_String=obj.get("ackDate").getAsString();
					String shipDate_String=obj.get("shipDate").getAsString();
					String vendorOrderNumber=obj.get("vendorOrderNumber").getAsString();
					
					if(shipDate_String != null && !shipDate_String.equals("")){
						System.out.println( " shipdate = "+DateUtils.parseDate(shipDate_String, new String[]{"MM/dd/yyyy"}));
						aVepodetail.setEstimatedShipDate(DateUtils.parseDate(shipDate_String, new String[]{"MM/dd/yyyy"}));
					}
					if(ackDate_String != null && !ackDate_String.equals("")){
						System.out.println(" ackdate = "+DateUtils.parseDate(ackDate_String, new String[]{"MM/dd/yyyy"}));
						aVepodetail.setAcknowledgedDate(DateUtils.parseDate(ackDate_String, new String[]{"MM/dd/yyyy"}));
					}
					aVepodetail.setVendorOrderNumber(vendorOrderNumber);
					if(vePodetailId!=null){
						aVepodetail.setVePodetailId(vePodetailId);
					}
					
					itsJobService.updatevePODetailfromAckTab(aVepodetail);
					
					
				}
				  
				
			}
			return null;
		}
		
		@RequestMapping(value = "/RxaddressBasedOnCustomerID",method = RequestMethod.POST)
		public @ResponseBody ArrayList<Rxaddress> RxaddressBasedOnCustomerID(@RequestParam(value="cuMasterID", required=false) Integer rxMasterID,HttpSession theSession, ModelMap theModel, HttpServletResponse theResponse) throws JobException, IOException, BankingException {
			ArrayList<Rxaddress> arxaddresslst=new ArrayList<Rxaddress>();
			try {
				arxaddresslst = itsCompanyService.getAllAddress(rxMasterID);
			} catch (CompanyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			theModel.addAttribute("theNewShiptocusaddress", arxaddresslst);
			return arxaddresslst;
		}
		
		@RequestMapping(value = "vePODetailLst",method = RequestMethod.POST)
		public @ResponseBody List<Vepodetail> getvePODetailLst(@RequestParam(value="vePoId", required=false) Integer theVepoId, 
																		HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
			List<Vepodetail> aPOLineItemDetails =new ArrayList<Vepodetail>();
			try{
				aPOLineItemDetails=itsJobService.getPOReleaseLineItem(theVepoId);
			}catch (JobException e) {
				sendTransactionException("<b>theVepoId:</b>"+theVepoId,"JOB",e,session,therequest);
				itsLogger.error(e.getMessage());
			}
			return aPOLineItemDetails;
		}
		public Integer getCustomerTaxTerritoryIDWithShipTo(Integer rxMasterID){
			Integer cotaxTerritoryID=null;
			try{
				cotaxTerritoryID=itsJobService.getCustomerTaxTerritoryIDWithShipTo(rxMasterID);
			}catch (Exception e) {
				itsLogger.error(e.getMessage());
			}
			return cotaxTerritoryID;
		}
		
		
		@RequestMapping(value = "CheckCommissionPaidorNot",method = RequestMethod.POST)
		public @ResponseBody Integer CheckCommissionPaidorNot(
				@RequestParam(value="joReleaseDetailID", required=false) Integer joReleaseDetailID, 
				HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
			Integer commissinStatus = 0;
			commissinStatus=itsJobService.getCommPaidDetails(joReleaseDetailID);
			return commissinStatus;
		}
		
		
		@RequestMapping(value = "CheckCommissionPaidRelease",method = RequestMethod.POST)
		public @ResponseBody Integer CheckCommissionPaidRelease(
				@RequestParam(value="joReleaseID", required=false) Integer joReleaseID, 
				@RequestParam(value="joMasterID", required=false) Integer joMasterID, 
				HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
			Integer commissinStatus = 0;
			commissinStatus=itsJobService.getCommPaidReleaseDetails(joReleaseID,joMasterID);
			return commissinStatus;
		}
		
		@RequestMapping(value="/updateJoReleaseCommRecieve", method = RequestMethod.GET)
		public @ResponseBody Integer updateJoReleaseCommRecieve(
														   @RequestParam(value="joReleaseId", required= false) Integer theJoreleaseId,
														   @RequestParam(value = "commissionClosedID",required = false) Boolean commissionRecieve,
														   HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
			JoRelease aJoRelease = new JoRelease();
			try{
				itsLogger.info("JoReleaseID<#>CommissionClosedID::"+theJoreleaseId+" - "+commissionRecieve);
				aJoRelease.setCommissionReceived(commissionRecieve);
				aJoRelease.setJoReleaseId(theJoreleaseId);
				itsJobService.updateCommissionReceived(aJoRelease);
			}catch (Exception e) {
			}
			return 0;
		}
		
		@RequestMapping(value = "/getInvoiceLineItems", method = RequestMethod.POST)
		public @ResponseBody
		Map<String, ArrayList<?>> getLineItems(
				@RequestParam(value = "prMasterId") Integer prMasterId,
				HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
			Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
			try {
				map.put("lineItems", (ArrayList<Prmaster>) itsPurchaseService
						.getLineItems(prMasterId));
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				sendTransactionException("<b>prMasterId:</b>"+prMasterId,"POController",e,session,theRequest);
			}
			return map;

		}
		
		@RequestMapping(value = "/addInvoiceLog", method = RequestMethod.POST)
		public @ResponseBody Integer addInvoiceLog(
				@RequestParam(value = "cuInvoiceID") Integer cuInvoiceID,
				@RequestParam(value = "action") Integer action,
				HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
			Cuinvoice aCuinvoice = new Cuinvoice();
			Cuinvoice oldCuinvoice = null;
			try {
				// action = 1 view action=2 pdf view action=3 mail sent
				String actionString = "";
					if(action!=null){
						if(action==1){
							actionString = "CI-Viewed";
						}else if(action == 2){
							actionString = "CI-PDF-Viewed";
						}else if(action == 3){
							actionString = "CI-Mail Sent";
						}else if(action == 4){
							actionString = "CI-Mail Sent Failed";
						}
					}
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				aCuinvoice = itsJobService.getCustomerInvoiceDetails(cuInvoiceID);
				aCuinvoice.setUserName(aUserBean.getFullName());
				itsJobService.saveCustomerInvoiceLog(oldCuinvoice,aCuinvoice,actionString,0,-1,null);
				
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"JobReleaseFormController",e,session,theRequest);
			}
			return 1;

		}
		
} 	