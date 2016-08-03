package com.turborep.turbotracker.job.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.Inventory.service.InventoryConstant;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cuinvoicedetail;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Ecsplitjob;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JobSalesOrderBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.EmailParameters;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.system.dao.Sysassignment;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.OperatingSystemInfo;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.PurchaseService;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

/**
 * This class serves the controls for the Sales order in job releases tab.
 * 
 * @author Kannan Subbu
 * @version 1.9.5
 * @since 1.8.8 </code> SalesOrder </code> class
 */

@Controller
@RequestMapping("/salesOrderController")
public class SalesOrderController {

	Logger logger = Logger.getLogger(JobSubmittalFormController.class);

	/* jobservice, salesservice and company services are used. */

	@Resource(name = "jobService")
	private JobService jobService;

	@Resource(name = "salesServices")
	private SalesService salesServices;

	@Resource(name = "companyService")
	private CompanyService itsCompanyService;

	@Resource(name = "pdfService")
	private PDFService itspdfService;

	@Resource(name = "vendorService")
	private VendorServiceInterface itsVendorService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@Resource(name = "sysService")
	private SysService itsSysService;
	@Resource(name = "customerService")
	private CustomerService cuMasterService;

	@Resource(name = "rolodexService")
	private RolodexService itsCustomerService;
	
	@Resource(name="sysService")
	private SysService sysservice;

	@Resource(name = "productService")
	private ProductService productService;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;
	
	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;
	
	/**
	 * Description: This method returns the details of an existing sales orders.
	 * 
	 * @param: joReleaseID
	 * @return: jobSalesOrderBean
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 *          {@link om.turborep.turbotracker.job.dao.JobSalesOrderBean}
	 * 
	 * */

	@RequestMapping(value = "/getSalesOrderDetails", method = RequestMethod.POST)
	public @ResponseBody
	JobSalesOrderBean getSalesOrderDetails(
			@RequestParam(value = "joReleaseId", required = false) Integer joReleaseId,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		JobSalesOrderBean JobSalesOrderBean = new JobSalesOrderBean();
		try {
			JobSalesOrderBean = jobService.getSalesOrderDetails(joReleaseId);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode());
			sendTransactionException("<b>joReleaseId:</b>"+joReleaseId,"SalesOrderController",e,session,theRequest);
		}

		return JobSalesOrderBean;
	}
	
	
	@RequestMapping(value = "/getJoReleaseDetail", method = RequestMethod.POST)
	public @ResponseBody
	JoRelease getJoReleaseDetail(
			@RequestParam(value = "joReleaseID", required = false) Integer joReleaseID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		JoRelease thejorelease = new JoRelease();
			 try {
				thejorelease=jobService.getJoRelease(joReleaseID);
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTransactionException("<b>joReleaseID:</b>"+joReleaseID,"SalesOrderController",e,session,theRequest);
			}

		return thejorelease;
	}
	
	@RequestMapping(value = "/getJobDetailsFromReleaseDetail", method = RequestMethod.POST)
	public @ResponseBody
	Jomaster getJobDetailsFromReleaseDetail(
			@RequestParam(value = "joReleasedetailID", required = false) Integer joReleasedetailID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		Integer joMasterID=0;
		try {
			joMasterID = jobService.getJobNumberFromReleaseDetail(joReleasedetailID);
			aJomaster = jobService.getSingleJobDetails(joMasterID);
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>joReleasedetailID:</b>"+joReleasedetailID,"SalesOrderController",e,session,theRequest);
		}

		return aJomaster;
	}

	@RequestMapping(value = "/getJobDetailsFromRelease", method = RequestMethod.POST)
	public @ResponseBody
	Jomaster getJobDetailsFromRelease(
			@RequestParam(value = "joReleaseID", required = false) Integer joReleaseID,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		String jobNumber;
		try {
			aJomaster = jobService.getJobNumberFromRelease(joReleaseID);
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>joReleaseID:</b>"+joReleaseID,"SalesOrderController",e,session,theRequest);
		}

		return aJomaster;
	}
	
	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.GET)
	public @ResponseBody
	Rxmaster getCustomerDetails(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Rxmaster aRxmaster = null;
		logger.info("getCustomerDetails: "+therxMasterId);
		try {
			if (therxMasterId != null) {
				aRxmaster = new Rxmaster();
				aRxmaster = jobService.getCustomerDetails(therxMasterId);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"SalesOrderController",e,session,theRequest);
		}
		return aRxmaster;
	}

	/**
	 * Description: This method returns the Unique Sales order ID (cuSOID) of an
	 * existing sales orders.
	 * 
	 * @param: jobNumber {@link String}
	 * @return: cuSOID {@link Integer}
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 * 
	 * */

	@RequestMapping(value = "/getCuSOID", method = RequestMethod.POST)
	public @ResponseBody
	Integer getCuSOID(
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Integer cuSOID = 0;

		try {
			cuSOID = jobService.getCuSOID(jobNumber);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode());
			sendTransactionException("<b>jobNumber:</b>"+jobNumber,"SalesOrderController",e,session,theRequest);
		}

		return cuSOID;
	}

	/**
	 * Description: This method returns all the sales order Object {@link Cuso}
	 * of existing Sales Orders.
	 * 
	 * @param: jobNumber {@link String}
	 * @return: customresponse
	 *          {@link com.turborep.turbotracker.json.CustomResponse}
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 *          {@link com.turborep.turbotracker.json.CustomResponse}
	 * */

	@RequestMapping(value = "/solineitemGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getSOLineItems(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		System.out.println("Line Items--->" + cuSOID);
		try {
			List<?> aSOLineItemDetails = jobService
					.getSOReleaseLineItem(cuSOID); /*
													 * Retrieve all sales orders
													 * from the service
													 */
			aResponse.setRows(aSOLineItemDetails); /*
													 * assign those objects to
													 * the custom response
													 */
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}

		return aResponse; /*
						 * Spring will automatically convert our CustomResponse
						 * as JSON object. This is triggered by the
						 * @ResponseBody annotation. It knows this because the
						 * JqGrid has set the headers to accept JSON format when
						 * it make a request Spring by default uses Jackson to
						 * convert the object to JSON
						 */
	}

	/**
	 * Description: This method returns a sales order Object {@link Cuso} and
	 * all the line items of the corresponding sales order {@link Cusodetail}.
	 * 
	 * @param: rxMasterId {@link String}, cuSOID {@link Integer}
	 * @return: resp {@link HashMap<String,Object>}
	 * @exception: JobException
	 * @throws: IOException
	 * */
	@RequestMapping(value = "/deletePoItem", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse deletePoItem(
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		System.out.println("on delete action" + vePOID);
		Vepodetail avePOObj = new Vepodetail();
		Vepo vePo = new Vepo();
		vePo.setVePoid(vePOID);
		avePOObj.setVePodetailId(vePOID);
		jobService.deletePOLineItem(avePOObj, vePo);

		return null;
	}
	@RequestMapping(value = "/getPreLoadData", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getPreLoadData(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			@RequestParam(value = "rxMasterID", required = false) String rxMasterId,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "joReleaseDetailID", required = false) Integer joReleaseDetailID,
			@RequestParam(value = "veBillID", required = false) Integer veBillID,
			@RequestParam(value = "joMasterID", required = false) Integer jomasterid,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws Exception {
		String salesMan = null;
		String assignedEmployee = null;
		String costing = null;
		String submitting = null;
		String ordering = null;
		String customerName = null;
		Cumaster aCumaster = null;
		Jomaster aJomaster = null;
		String tableName = "tsUserLogin";
		String termsDesc = null;
		Cuso aCuso = null;
		Vepo aVepo = null;
		Integer rxMasterID = null;
		String coTaxterritory = null;
		Vepodetail aVepodetail = null;
		Cusodetail aCusoDetails = null;
		Jomaster joMaster =null;
	
		if (rxMasterId != null && !rxMasterId.trim().equals("")) {
			rxMasterID = Integer.parseInt(rxMasterId);
			if(rxMasterId.contains("null")){
				rxMasterId=null;
			}
		}
		
		HashMap<String, Object> resp = null;
		Integer prTOWarehouseID = 0;
		try {
//			aJomaster = jobService.getSingleJobDetails(jobNumber);
			logger.info("jobNumber ID"+jobNumber);
			if(jomasterid!=null){
			aJomaster = jobService.getSingleJobDetails(jomasterid);
			}
			if (cuSOID != null && cuSOID != 0) {
				aCuso = jobService.getCusoObj(cuSOID);
				
				jomasterid = itsPurchaseService.getRxCustomerID(aCuso.getJoReleaseId());
				if(jomasterid!=null)
				{
				joMaster = jobService.getSingleJobDetails(jomasterid);
				joMaster.setDescription(itsPurchaseService.getVendor(joMaster.getRxCustomerId()));
				}				
				
				aCusoDetails = jobService.getCusoDetailObj(cuSOID);
				
				if (aCuso != null && aCuso.getCuTermsId() != null && aCuso.getCuTermsId()!=-1) {
					System.out.println("aCuso.getCuTermsId() :: "+aCuso.getCuTermsId());
					termsDesc = jobService.getCuTerms(aCuso.getCuTermsId());
				}
				if (aCuso != null) {
					if (aCuso.getCuAssignmentId0() != null) {
						salesMan = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId0(), tableName);
					}
					if (aCuso.getCuAssignmentId1() != null) {
						assignedEmployee = salesServices
								.getAssignedEmployeeName(
										aCuso.getCuAssignmentId1(), tableName);
					}
					if (aCuso.getCuAssignmentId2() != null) {
						costing = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId2(), tableName);
					}
					if (aCuso.getCuAssignmentId3() != null) {
						submitting = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId3(), tableName);
					}
					if (aCuso.getCuAssignmentId4() != null) {
						ordering = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId4(), tableName);
					}
					
					coTaxterritory = jobService.getCoTaxterritory(aCuso.getCoTaxTerritoryId());
					
				}
				
				//aCuso.setSonumber(itsSysService.getSysSequenceNumberwithSO("cuInvoice").toString());
				
				
				prTOWarehouseID = jobService.getPrToWarehouseID(cuSOID);
					
				
			} else if (vePOID != null) {
				aVepo = itsVendorService.getVePo(vePOID);
				
				
				jomasterid = itsPurchaseService.getRxCustomerID(aVepo.getJoReleaseId());
				if(jomasterid!=null)
				{
				joMaster = jobService.getSingleJobDetails(jomasterid);
				joMaster.setDescription(itsPurchaseService.getVendor(joMaster.getRxCustomerId()));
				}	
				
				if(veBillID!=null && veBillID>0){
				Vebill aVebill = itsVendorService.getVebill(veBillID);
				aVepo.setVeShipViaId(aVebill.getVeShipViaId());
				aVepo.setShipDate(aVebill.getShipDate()+"");
				}
				System.out.println("Before Checking poNumber---->"+aVepo.getPonumber());
				if(aVepo.getPonumber() != null && aVepo.getPonumber().length() > 0)
				{
					Integer countofinvoiceforrelease=jobService.getnumberofInvoiceNumber( aVepo.getPonumber());
					countofinvoiceforrelease=countofinvoiceforrelease+1;
					
					String newCustomerInvoiceNumber = "";
					Integer requireCuInvoiceNumberOrNot = 0;
					List<String> addlist=new ArrayList<String>();
					addlist.add("RequireNewNumbersForCuInvoices");
					ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
					requireCuInvoiceNumberOrNot = sysvariablelist.get(0).getValueLong()==null?0:sysvariablelist.get(0).getValueLong();
					newCustomerInvoiceNumber = sysvariablelist.get(0).getValueString()==null?"":sysvariablelist.get(0).getValueString();
					
					aVepo.setPonumber(requireCuInvoiceNumberOrNot>0?newCustomerInvoiceNumber:aVepo.getPonumber()+countofinvoiceforrelease);
					
					System.out.println("After Checking poNumber---->"+(requireCuInvoiceNumberOrNot>0?newCustomerInvoiceNumber:aVepo.getPonumber()+countofinvoiceforrelease));
					/*
					String poNumber = aVepo.getPonumber();
					
					char a = poNumber.charAt(poNumber.length()-1);
					System.out.println(a);
					if(Character.isLetter(a))
					{
						poNumber = poNumber+"1";
						aVepo.setPonumber(poNumber);
					}
					else if(Character.isDigit(a))
					{
						int aa = Character.getNumericValue(a);
						System.out.println(aa);
						int i = Character.getNumericValue(a);
						i+= 1;
						poNumber = poNumber.substring(0, poNumber.length()-1)+i;
						
						//aJoMaster.setJobNumber(itsSysService.getSysSequenceNumber("joMaster").toString());
						
						aVepo.setPonumber(poNumber);
						
					}
					System.out.println("After Checking poNumber---->"+aVepo.getPonumber());
				*/}
				
				aVepodetail = jobService.getVepoDetailOBJ(vePOID);
				aCumaster = jobService.getSingleCuMasterDetails(rxMasterID);
				if (aCumaster != null && aCumaster.getCuTermsId() != null) {
					termsDesc = jobService.getCuTerms(aCumaster.getCuTermsId());
				}
				if (aJomaster != null) {
					if (aJomaster.getCuAssignmentId0() != null) {
						salesMan = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId0(), tableName);
					}
					if (aJomaster.getCuAssignmentId1() != null) {
						assignedEmployee = salesServices
								.getAssignedEmployeeName(
										aJomaster.getCuAssignmentId1(),
										tableName);
					}
					if (aJomaster.getCuAssignmentId2() != null) {
						costing = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId2(), tableName);
					}
					if (aJomaster.getCuAssignmentId3() != null) {
						submitting = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId3(), tableName);
					}
					if (aJomaster.getCuAssignmentId4() != null) {
						ordering = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId4(), tableName);
					}
					
				}
			}
			if(!rxMasterId.equals("0")){
			customerName = jobService.getCustomerName(rxMasterId);
			}
			if (customerName == null || customerName == "") {
				if (aCuso != null && aCuso.getRxCustomerId() != null)
					customerName = jobService.getCustomerName(aCuso
							.getRxCustomerId().toString());
			}
			String vendorInvoiceNumber = jobService.getvendorInvoiceNumber(joReleaseDetailID);
			System.out.println("hello");
			/*if(aCuso!=null && aCuso.getJoReleaseId()!=null){
					if(aCuso.getSonumber()!= null && aCuso.getSonumber().length() > 0){
						Integer countofinvoiceforrelease=jobService.getnumberofInvoiceNumber(aCuso.getSonumber());
						countofinvoiceforrelease=countofinvoiceforrelease+1;
						aCuso.setSonumber(aCuso.getSonumber()+countofinvoiceforrelease);
					}
			}*/
			
			
			
			resp = new HashMap<String, Object>();
			if(aJomaster!=null){
				
				if(aJomaster.getRxCustomerId()!=null){
					Cumaster acuMaster= cuMasterService.getCustomerDetails(aJomaster.getRxCustomerId());
					if(acuMaster!=null){
						resp.put("defaultWHID",acuMaster.getPrWarehouseId());
					}else{
						resp.put("defaultWHID","0");
					}
					
				}else{
					resp.put("defaultWHID","0");
				}
			}else{
					resp.put("defaultWHID","0");
				}
			
			resp.put("Cuso", aCuso);
			resp.put("vendorInvoiceNumber", vendorInvoiceNumber );
			System.out.println("vendorInvoiceNumber :: "+vendorInvoiceNumber);
			SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			if(aCuso!=null){
			if(aCuso.getEmailTimeStamp()!=null){
				String aDateFormat = fromUser.format(aCuso.getEmailTimeStamp());
				resp.put("emailTime", aDateFormat);
			}
			}
			// CI New Invoice Number ID#465 on 2015-12-15
			String newInvoiceNumber = "";
			Integer requireInvoiceNumberOrNot = 0;
			List<String> addlist=new ArrayList<String>();
			addlist.add("RequireNewNumbersForCuInvoices");
			ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
			requireInvoiceNumberOrNot = sysvariablelist.get(0).getValueLong()==null?0:sysvariablelist.get(0).getValueLong();
			newInvoiceNumber = sysvariablelist.get(0).getValueString()==null?"":sysvariablelist.get(0).getValueString();
			
			if(aCuso!=null && aCuso.getSonumber()!=null){
			String customerInvoiceNumber = jobService.getLastestInvoiceNumber(aCuso.getSonumber());
			resp.put("customerInvoiceNumber", requireInvoiceNumberOrNot>0?newInvoiceNumber:customerInvoiceNumber );
			}
			
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			//SysUserDefault asysusrdeflt=salesServices.getSysUserDefault(aUserBean.getUserId());
			List<?> cusoTemplates = salesServices.getCusoTemplateList();
			resp.put("vepo", aVepo);
			resp.put("Cumaster", aCumaster);
			resp.put("Cusodetail", aCusoDetails);
			resp.put("Vepodetail", aVepodetail);
			resp.put("CustomerName", customerName);
			resp.put("SalesMan", salesMan);
			resp.put("aJomaster", aJomaster);
			resp.put("ashiptoJomaster", joMaster);
			resp.put("Cumaster", aCumaster);
			resp.put("AE", assignedEmployee);
			resp.put("Costing", costing);
			resp.put("Submitting", submitting);
			resp.put("Ordering", ordering);
			resp.put("termsDesc", termsDesc);
			resp.put("coTaxterritory", coTaxterritory);
			resp.put("salesTemplate", cusoTemplates);
			resp.put("wareHouse", jobService.getWareHouse());
			/*resp.put("cuInvoiceNo", itsSysService.getSysSequenceNumberwithSO("cuInvoice").toString());*/
			resp.put("cuInvoiceNo", newInvoiceNumber);
			resp.put("prTOWarehouseID", prTOWarehouseID);
			
			//resp.put("myprofile", asysusrdeflt);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}finally{
			salesMan = null;
			assignedEmployee = null;
			costing = null;
			submitting = null;
			ordering = null;
			customerName = null;
			aCumaster = null;
			aJomaster = null;
			tableName = null;
			termsDesc = null;
			aCuso = null;
			aVepo = null;
			rxMasterID = null;
			coTaxterritory = null;
			aVepodetail = null;
			aCusoDetails = null;
		}

		return resp;
	}
	@RequestMapping(value = "/getPreLoadData2", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getPreLoadData2(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			@RequestParam(value = "rxMasterID", required = false) String rxMasterId,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "joReleaseDetailID", required = false) Integer joReleaseDetailID,
			@RequestParam(value = "veBillID", required = false) Integer veBillID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws Exception {
		String salesMan = null;
		String assignedEmployee = null;
		String costing = null;
		String submitting = null;
		String ordering = null;
		String customerName = null;
		Cumaster aCumaster = null;
		Jomaster aJomaster = null;
		String tableName = "tsUserLogin";
		String termsDesc = null;
		Cuso aCuso = null;
		Vepo aVepo = null;
		Integer rxMasterID = null;
		String coTaxterritory = null;
		Vepodetail aVepodetail = null;
		Cusodetail aCusoDetails = null;
		Jomaster joMaster =null;
		Integer jomasterid = null;
	
		if (rxMasterId != null && !rxMasterId.trim().equals("")) {
			rxMasterID = Integer.parseInt(rxMasterId);
			if(rxMasterId.contains("null")){
				rxMasterId=null;
			}
		}
		
		HashMap<String, Object> resp = null;
		Integer prTOWarehouseID = 0;
		try {
			logger.info("jobNumber ID"+jobNumber);
			if(cuSOID != null && cuSOID != 0){
				aCuso = jobService.getCusoObj(cuSOID);
				aJomaster=jobService.getJobNumberFromRelease(aCuso.getJoReleaseId());
				jomasterid=aJomaster.getJoMasterId();
			}else if (vePOID != null) {
				aVepo = itsVendorService.getVePo(vePOID);
				aJomaster=jobService.getJobNumberFromRelease(aVepo.getJoReleaseId());
				jomasterid=aJomaster.getJoMasterId();
			}
			
			if (cuSOID != null && cuSOID != 0) {
				
				
				//jomasterid = itsPurchaseService.getRxCustomerID(aCuso.getJoReleaseId());
				if(jomasterid!=null)
				{
				joMaster = jobService.getSingleJobDetails(jomasterid);
				joMaster.setDescription(itsPurchaseService.getVendor(joMaster.getRxCustomerId()));
				}				
				
				aCusoDetails = jobService.getCusoDetailObj(cuSOID);
				
				if (aCuso != null && aCuso.getCuTermsId() != null && aCuso.getCuTermsId()!=-1) {
					System.out.println("aCuso.getCuTermsId() :: "+aCuso.getCuTermsId());
					termsDesc = jobService.getCuTerms(aCuso.getCuTermsId());
				}
				if (aCuso != null) {
					if (aCuso.getCuAssignmentId0() != null) {
						salesMan = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId0(), tableName);
					}
					if (aCuso.getCuAssignmentId1() != null) {
						assignedEmployee = salesServices
								.getAssignedEmployeeName(
										aCuso.getCuAssignmentId1(), tableName);
					}
					if (aCuso.getCuAssignmentId2() != null) {
						costing = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId2(), tableName);
					}
					if (aCuso.getCuAssignmentId3() != null) {
						submitting = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId3(), tableName);
					}
					if (aCuso.getCuAssignmentId4() != null) {
						ordering = salesServices.getAssignedEmployeeName(
								aCuso.getCuAssignmentId4(), tableName);
					}
					
					coTaxterritory = jobService.getCoTaxterritory(aCuso.getCoTaxTerritoryId());
					
				}
				
				//aCuso.setSonumber(itsSysService.getSysSequenceNumberwithSO("cuInvoice").toString());
				
				
				prTOWarehouseID = jobService.getPrToWarehouseID(cuSOID);
					
				
			} else if (vePOID != null) {
				aVepo = itsVendorService.getVePo(vePOID);
				
				
				jomasterid = itsPurchaseService.getRxCustomerID(aVepo.getJoReleaseId());
				if(jomasterid!=null)
				{
				joMaster = jobService.getSingleJobDetails(jomasterid);
				joMaster.setDescription(itsPurchaseService.getVendor(joMaster.getRxCustomerId()));
				}	
				
				if(veBillID!=null && veBillID>0){
				Vebill aVebill = itsVendorService.getVebill(veBillID);
				aVepo.setVeShipViaId(aVebill.getVeShipViaId());
				aVepo.setShipDate(aVebill.getShipDate()+"");
				}
				System.out.println("Before Checking poNumber---->"+aVepo.getPonumber());
				if(aVepo.getPonumber() != null && aVepo.getPonumber().length() > 0)
				{
					Integer countofinvoiceforrelease=jobService.getnumberofInvoiceNumber( aVepo.getPonumber());
					countofinvoiceforrelease=countofinvoiceforrelease+1;
					
					String newCustomerInvoiceNumber = "";
					Integer requireCuInvoiceNumberOrNot = 0;
					List<String> addlist=new ArrayList<String>();
					addlist.add("RequireNewNumbersForCuInvoices");
					ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
					requireCuInvoiceNumberOrNot = sysvariablelist.get(0).getValueLong()==null?0:sysvariablelist.get(0).getValueLong();
					newCustomerInvoiceNumber = sysvariablelist.get(0).getValueString()==null?"":sysvariablelist.get(0).getValueString();
					
					aVepo.setPonumber(requireCuInvoiceNumberOrNot>0?newCustomerInvoiceNumber:aVepo.getPonumber()+countofinvoiceforrelease);
					
					System.out.println("After Checking poNumber---->"+(requireCuInvoiceNumberOrNot>0?newCustomerInvoiceNumber:aVepo.getPonumber()+countofinvoiceforrelease));
					/*
					String poNumber = aVepo.getPonumber();
					
					char a = poNumber.charAt(poNumber.length()-1);
					System.out.println(a);
					if(Character.isLetter(a))
					{
						poNumber = poNumber+"1";
						aVepo.setPonumber(poNumber);
					}
					else if(Character.isDigit(a))
					{
						int aa = Character.getNumericValue(a);
						System.out.println(aa);
						int i = Character.getNumericValue(a);
						i+= 1;
						poNumber = poNumber.substring(0, poNumber.length()-1)+i;
						
						//aJoMaster.setJobNumber(itsSysService.getSysSequenceNumber("joMaster").toString());
						
						aVepo.setPonumber(poNumber);
						
					}
					System.out.println("After Checking poNumber---->"+aVepo.getPonumber());
				*/}
				
				aVepodetail = jobService.getVepoDetailOBJ(vePOID);
				aCumaster = jobService.getSingleCuMasterDetails(rxMasterID);
				if (aCumaster != null && aCumaster.getCuTermsId() != null) {
					termsDesc = jobService.getCuTerms(aCumaster.getCuTermsId());
				}
				if (aJomaster != null) {
					if (aJomaster.getCuAssignmentId0() != null) {
						salesMan = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId0(), tableName);
					}
					if (aJomaster.getCuAssignmentId1() != null) {
						assignedEmployee = salesServices
								.getAssignedEmployeeName(
										aJomaster.getCuAssignmentId1(),
										tableName);
					}
					if (aJomaster.getCuAssignmentId2() != null) {
						costing = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId2(), tableName);
					}
					if (aJomaster.getCuAssignmentId3() != null) {
						submitting = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId3(), tableName);
					}
					if (aJomaster.getCuAssignmentId4() != null) {
						ordering = salesServices.getAssignedEmployeeName(
								aJomaster.getCuAssignmentId4(), tableName);
					}
					
				}
			}
			if(!rxMasterId.equals("0")){
			customerName = jobService.getCustomerName(rxMasterId);
			}
			if (customerName == null || customerName == "") {
				if (aCuso != null && aCuso.getRxCustomerId() != null)
					customerName = jobService.getCustomerName(aCuso
							.getRxCustomerId().toString());
			}
			String vendorInvoiceNumber = jobService.getvendorInvoiceNumber(joReleaseDetailID);
			System.out.println("hello");
			/*if(aCuso!=null && aCuso.getJoReleaseId()!=null){
					if(aCuso.getSonumber()!= null && aCuso.getSonumber().length() > 0){
						Integer countofinvoiceforrelease=jobService.getnumberofInvoiceNumber(aCuso.getSonumber());
						countofinvoiceforrelease=countofinvoiceforrelease+1;
						aCuso.setSonumber(aCuso.getSonumber()+countofinvoiceforrelease);
					}
			}*/
			
			
			
			resp = new HashMap<String, Object>();
			if(aJomaster!=null){
				
				if(aJomaster.getRxCustomerId()!=null){
					Cumaster acuMaster= cuMasterService.getCustomerDetails(aJomaster.getRxCustomerId());
					if(acuMaster!=null){
						resp.put("defaultWHID",acuMaster.getPrWarehouseId());
					}else{
						resp.put("defaultWHID","0");
					}
					
				}else{
					resp.put("defaultWHID","0");
				}
			}else{
					resp.put("defaultWHID","0");
				}
			
			resp.put("Cuso", aCuso);
			resp.put("vendorInvoiceNumber", vendorInvoiceNumber );
			System.out.println("vendorInvoiceNumber :: "+vendorInvoiceNumber);
			SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			if(aCuso!=null){
			if(aCuso.getEmailTimeStamp()!=null){
				String aDateFormat = fromUser.format(aCuso.getEmailTimeStamp());
				resp.put("emailTime", aDateFormat);
			}
			}
			// CI New Invoice Number ID#465 on 2015-12-15
			String newInvoiceNumber = "";
			Integer requireInvoiceNumberOrNot = 0;
			List<String> addlist=new ArrayList<String>();
			addlist.add("RequireNewNumbersForCuInvoices");
			ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
			requireInvoiceNumberOrNot = sysvariablelist.get(0).getValueLong()==null?0:sysvariablelist.get(0).getValueLong();
			newInvoiceNumber = sysvariablelist.get(0).getValueString()==null?"":sysvariablelist.get(0).getValueString();
			
			if(aCuso!=null && aCuso.getSonumber()!=null){
			String customerInvoiceNumber = jobService.getLastestInvoiceNumber(aCuso.getSonumber());
			resp.put("customerInvoiceNumber", requireInvoiceNumberOrNot>0?newInvoiceNumber:customerInvoiceNumber );
			}
			
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			//SysUserDefault asysusrdeflt=salesServices.getSysUserDefault(aUserBean.getUserId());
			List<?> cusoTemplates = salesServices.getCusoTemplateList();
			resp.put("vepo", aVepo);
			resp.put("Cumaster", aCumaster);
			resp.put("Cusodetail", aCusoDetails);
			resp.put("Vepodetail", aVepodetail);
			resp.put("CustomerName", customerName);
			resp.put("SalesMan", salesMan);
			resp.put("aJomaster", aJomaster);
			resp.put("ashiptoJomaster", joMaster);
			resp.put("Cumaster", aCumaster);
			resp.put("AE", assignedEmployee);
			resp.put("Costing", costing);
			resp.put("Submitting", submitting);
			resp.put("Ordering", ordering);
			resp.put("termsDesc", termsDesc);
			resp.put("coTaxterritory", coTaxterritory);
			resp.put("salesTemplate", cusoTemplates);
			resp.put("wareHouse", jobService.getWareHouse());
			/*resp.put("cuInvoiceNo", itsSysService.getSysSequenceNumberwithSO("cuInvoice").toString());*/
			resp.put("cuInvoiceNo", newInvoiceNumber);
			resp.put("prTOWarehouseID", prTOWarehouseID);
			
			//resp.put("myprofile", asysusrdeflt);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}finally{
			salesMan = null;
			assignedEmployee = null;
			costing = null;
			submitting = null;
			ordering = null;
			customerName = null;
			aCumaster = null;
			aJomaster = null;
			tableName = null;
			termsDesc = null;
			aCuso = null;
			aVepo = null;
			rxMasterID = null;
			coTaxterritory = null;
			aVepodetail = null;
			aCusoDetails = null;
		}

		return resp;
	}

	/**
	 * Description: This method returns a Customer Invoice Object
	 * {@link Cuinvoice} and all the line items of the corresponding customer
	 * invoice{@link Cuinvoicedetail}.
	 * 
	 * @param: rxMasterId {@link String}, cuInvoiceID {@link Integer}
	 * @return: resp {@link HashMap<String,Object>}
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 * */

	@RequestMapping(value = "/getPreInvoiceData", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getPreInvoiceData(
			@RequestParam(value = "cuInvoiceId", required = false) Integer cuInvoiceID,
			@RequestParam(value = "rxMasterID", required = false) String rxMasterId,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {

		String CustomerName = null;
		String tableName = "tsUserLogin";
		String cuTerms = null;
		String SalesMan = null;
		String AE = null;
		String Costing = null;
		String Submitting = null;
		String Ordering = null;
		Cuinvoice aCuinvoice = null;
		Cuinvoicedetail aCuinvoicedetail = null;
		HashMap<String, Object> resp = null;

		try {
			if (cuInvoiceID != null) {
				aCuinvoice = jobService.getSingleCuInvoiceObj(cuInvoiceID);
				System.out.println("SOC Before poNumber---->"+aCuinvoice.getInvoiceNumber());
				if(aCuinvoice.getInvoiceNumber() != null && aCuinvoice.getInvoiceNumber().length() > 0)
				{
					String poNumber = aCuinvoice.getInvoiceNumber();
					
					//System.out.println("SOC Test poNumber------>"+);
					
					char a = poNumber.charAt(poNumber.length()-1);
					System.out.println(a);
					if(Character.isLetter(a))
					{
						
						//poNumber = poNumber+"1";
						poNumber = poNumber;
						aCuinvoice.setInvoiceNumber(poNumber);
					}
					else if(Character.isDigit(a))
					{
						int aa = Character.getNumericValue(a);
						System.out.println(aa);
						int i = Character.getNumericValue(a);
						i+= 1;
						String pono="";
						boolean firstCharIsLetter =false;
						
						firstCharIsLetter = Character.isLetter(poNumber.charAt(0));
						
						if(firstCharIsLetter)
						{
						if(aCuinvoice.getCreditmemo()!=0)
						{
						pono = poNumber.substring(0, 2);
						System.out.println(pono);
						String digits = poNumber.replaceAll("[^0-9.]", "");
						poNumber = pono+""+(Integer.parseInt(digits)+1);
						aCuinvoice.setInvoiceNumber(poNumber);
						}
						else
						{
							aCuinvoice.setInvoiceNumber(poNumber);
						}
						}
						else
						{						
						//poNumber = poNumber.substring(0, poNumber.length()-1)+i;
						aCuinvoice.setInvoiceNumber(poNumber);
						}
						
						
					}
					System.out.println("SOC After poNumber---->"+aCuinvoice.getInvoiceNumber());
				}
				if(rxMasterId != null)
					CustomerName = jobService.getCustomerName(rxMasterId);
				else
					CustomerName = jobService.getCustomerName(aCuinvoice.getRxCustomerId().toString());
				
				if (aCuinvoice != null) {
					if (aCuinvoice.getCuAssignmentId0() != null) {
						SalesMan = salesServices.getAssignedEmployeeName(
								aCuinvoice.getCuAssignmentId0(), tableName);
					}
					if (aCuinvoice.getCuAssignmentId1() != null) {
						AE = salesServices.getAssignedEmployeeName(
								aCuinvoice.getCuAssignmentId1(), tableName);
					}
					if (aCuinvoice.getCuAssignmentId2() != null) {
						Costing = salesServices.getAssignedEmployeeName(
								aCuinvoice.getCuAssignmentId2(), tableName);
					}
					if (aCuinvoice.getCuAssignmentId3() != null) {
						Submitting = salesServices.getAssignedEmployeeName(
								aCuinvoice.getCuAssignmentId3(), tableName);
					}
					if (aCuinvoice.getCuAssignmentId4() != null) {
						Ordering = salesServices.getAssignedEmployeeName(
								aCuinvoice.getCuAssignmentId4(), tableName);
					}
					if (aCuinvoice.getCuTermsId() != null && aCuinvoice.getCuTermsId()!=-1) {
						
						logger.info("aCuinvoice.getCuTermsId():"+aCuinvoice.getCuTermsId());
						cuTerms = jobService.getCuTerms(aCuinvoice
								.getCuTermsId());
					}
				}
				aCuinvoicedetail = jobService
						.getCuInvoiceDetailObj(cuInvoiceID);
				resp = new HashMap<String, Object>();
				SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				if(aCuinvoice.getPrintDate()!=null){
					String aDateFormat = fromUser.format(aCuinvoice.getPrintDate());
					resp.put("emailTimestamp", aDateFormat);
				}
				Jomaster ajoMaster=null;
				if(aCuinvoice.getJoReleaseDetailId()!=null){
					ajoMaster=jobService.getjoMasterAddressDetails(aCuinvoice.getJoReleaseDetailId());
				}
				resp.put("joMaster", ajoMaster);
				resp.put("cuInvoice", aCuinvoice);
				resp.put("Cuinvoicedetail", aCuinvoicedetail);
				resp.put("CustomerName", CustomerName);
				resp.put("SalesMan", SalesMan);
				resp.put("AE", AE);
				resp.put("Costing", Costing);
				resp.put("Submitting", Submitting);
				resp.put("Ordering", Ordering);
				resp.put("cuTerms", cuTerms);
				resp.put("wareHouse", jobService.getWareHouse());
			}
			else
			{
				resp = new HashMap<String, Object>();
				Calendar c=new GregorianCalendar();
				c.add(Calendar.DATE, 30);
				Date d=c.getTime();
				String date="";
				SimpleDateFormat sp = new SimpleDateFormat("MM/dd/yyyy");
				date = sp.format(new Date());
				resp.put("date", date);
				resp.put("shipdate", date);
				resp.put("duedate", sp.format(d));
				resp.put("wareHouse", jobService.getWareHouse());
			}
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"SalesOrderController",e,session,theRequest);
		}

		return resp;
	}

	/**
	 * Description: This method returns a Customer Invoice Object
	 * {@link Cuinvoice} and all the line items of the corresponding customer
	 * invoice{@link Cuinvoicedetail}.
	 * 
	 * @param: rxMasterId {@link String}, cuInvoiceID {@link Integer}
	 * @return: custom response {@link CustomResponse}
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 *          {@link SalesOrderController#computeTaxTotal(BigDecimal, BigDecimal, BigDecimal, byte, Double, BigDecimal)}
	 * */
	/*@RequestMapping(value = "/manpulateSOReleaseLineItem", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse ManipulateSOReleaseLineItem(
			@RequestParam(value = "cuSoid", required = false) Integer cuSOID,
			@RequestParam(value = "description", required = false) String desc,
			@RequestParam(value = "itemCode", required = false) String itemCode,
			@RequestParam(value = "note", required = false) String note,
			@RequestParam(value = "oper", required = false) String Oper,
			@RequestParam(value = "prMasterId", required = false) Integer prMasterID,
			@RequestParam(value = "priceMultiplier", required = false) BigDecimal priceMultiplies,
			@RequestParam(value = "quantityOrdered", required = false) BigDecimal quantityOrder,
			@RequestParam(value = "taxable", required = false) String Taxable,
			@RequestParam(value = "unitCost", required = false) BigDecimal unitCost,
			@RequestParam(value = "cuSodetailId", required = false) Integer cuSODetailID,
			@RequestParam(value = "taxRate", required = false) Double taxRate,
			@RequestParam(value = "freight", required = false) BigDecimal freight,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		Cusodetail aCusodetail = new Cusodetail();
		Cuso aCuso = new Cuso();
		Cusodetail aCusodetailObj = null;
		boolean saved = false;
		logger.info("CusoDetailID: CusoID :: "+cuSODetailID+"   "+itemCode+"  "+prMasterID+" "+cuSOID);
		BigDecimal[] taxandTotal = null;
		BigDecimal[] editTaxTotal = null;
		
		if(unitCost==null)
			unitCost = BigDecimal.ZERO;
		if(quantityOrder == null)
			quantityOrder = BigDecimal.ZERO;
		if(priceMultiplies == null)
			priceMultiplies = BigDecimal.ZERO;
		

		try {
			if (cuSODetailID != null && !cuSODetailID.equals("null") && !cuSODetailID.equals(null)) {
				aCusodetailObj = jobService
						.getSingleCusoDetailObj(cuSODetailID);
			}
			if (Oper.equalsIgnoreCase("del")) {
				aCusodetail.setCuSodetailId(cuSODetailID);
				taxandTotal = computeTaxTotal(aCusodetailObj.getUnitCost(),
						aCusodetailObj.getPriceMultiplier(),
						aCusodetailObj.getQuantityOrdered(),
						aCusodetailObj.getTaxable(), taxRate, freight);
				aCuso.setTaxTotal(taxandTotal[1]);
				aCuso.setCuSoid(cuSOID);
				saved = jobService.deleteSOReleaseLineItem(aCusodetail, aCuso);
			} else {
				aCusodetail.setCuSoid(cuSOID);
				aCusodetail.setDescription(desc);
				aCusodetail.setPrMasterId(prMasterID);
				aCusodetail.setPriceMultiplier(priceMultiplies);
				BigDecimal qo = new BigDecimal(0.00);
				if(quantityOrder.toString() != null && quantityOrder.toString() != "")
					qo = new BigDecimal(quantityOrder.toString());
				System.out.println(" qo = "+qo);
				aCusodetail.setQuantityOrdered(qo.setScale(2, RoundingMode.FLOOR));
				aCusodetail.setQuantityBilled(new BigDecimal(0.00));
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("on")) {
						aCusodetail.setTaxable((byte) 1);
					} else {
						aCusodetail.setTaxable((byte) 0);
					}
				}
				aCusodetail.setUnitCost(unitCost.setScale(2, RoundingMode.FLOOR));
				aCusodetail.setNote(note);
				taxandTotal = new BigDecimal[3];
				taxandTotal = computeTaxTotal(unitCost.setScale(2, RoundingMode.FLOOR), priceMultiplies,
						quantityOrder.setScale(2, RoundingMode.FLOOR), aCusodetail.getTaxable(), taxRate,
						freight);
				aCuso.setCuSoid(cuSOID);
				aCuso.setFreight(freight);
				aCuso.setTaxTotal(taxandTotal[1].setScale(2, RoundingMode.FLOOR));
				aCuso.setSubTotal(taxandTotal[0].setScale(2, RoundingMode.FLOOR));
				aCuso.setCostTotal(taxandTotal[2].setScale(2, RoundingMode.FLOOR));
				
				System.out.println(" taxandTotal[1] = "+taxandTotal[1]+" || taxandTotal[0] = "+taxandTotal[0]+" || taxandTotal[2] = "+taxandTotal[2]+" || cuSOID = "+cuSOID);
				
				aCuso.setCuSoid(cuSOID);
				
				if (Oper.equalsIgnoreCase("add")) {
					saved = jobService.addSOReleaseLineItem(aCusodetail, aCuso);
				} else if (Oper.equalsIgnoreCase("edit")) {
					Cuso editCusoObj = new Cuso();
					editCusoObj.setCuSoid(cuSOID);
					byte oldTaxEnabled = 0;					
					byte newTaxEnabled = 0;
					newTaxEnabled = aCusodetail.getTaxable();
					
					BigDecimal data_unitCost = new BigDecimal(0),
							data_priceMultiplier = new BigDecimal(0),
							data_quantityOrdered = new BigDecimal(0);
					
					if(aCusodetailObj != null)
					{
						if(aCusodetailObj.getTaxable() != null)
							oldTaxEnabled = aCusodetailObj.getTaxable();
						editTaxTotal = new BigDecimal[3];
						data_unitCost = aCusodetailObj.getUnitCost().setScale(2, RoundingMode.FLOOR);
						data_priceMultiplier = aCusodetailObj.getPriceMultiplier();
						data_quantityOrdered = aCusodetailObj.getQuantityOrdered().setScale(2, RoundingMode.FLOOR);
					}
					editTaxTotal = computeTaxTotal(data_unitCost, data_priceMultiplier, data_quantityOrdered, 
							oldTaxEnabled, taxRate, freight);
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
					saved = jobService.editSOReleaseLineItem(aCusodetail,
							aCuso, editCusoObj);
				}
			}
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		} finally {
			aCusodetail = null;
			aCuso = null;
			aCusodetailObj = null;
			saved = false;
			taxandTotal = null;
			editTaxTotal = null;
		}

		return aResponse;
	}*/

	/**
	 * Description: This method is used for computing the tax, subtotal and
	 * total of sales orders and customer invoice.
	 * 
	 * @param: unitPrice {@link BigDecimal}, priceMultiplier {@link BigDecimal},
	 *         quantityOrdered {@link BigDecimal}, taxable {@link Byte}, taxRate
	 *         {@link BigDecimal},freight {@link BigDecimal}
	 * @return: taxAndTotal {@link BigDecimal}
	 * @exception: JobException
	 * @throws: IOException
	 * */

	public BigDecimal[] computeTaxTotal(BigDecimal unitPrice,
			BigDecimal priceMultiplier, BigDecimal quantityOrdered,
			byte taxable, Double taxRate, BigDecimal freight) {
		BigDecimal[] taxandTotal = new BigDecimal[3];
		BigDecimal subTotal = new BigDecimal(0);
		BigDecimal Total = new BigDecimal(0);
		BigDecimal taxTotal = new BigDecimal(0);
		BigDecimal taxdivisor = new BigDecimal(100);
		logger.info("J Multiplier::"+priceMultiplier);
		try {
			if(priceMultiplier.compareTo(new BigDecimal("0.0000"))==0){
				priceMultiplier=new BigDecimal("1.0000");
			}
			logger.info(" taxable = "+taxable+" || UnitPrice: "+unitPrice+"\nPrice Multiplier: "+priceMultiplier+"\nQuantity Ordered: "+quantityOrdered);
			subTotal = unitPrice.multiply(priceMultiplier).multiply(quantityOrdered);
			logger.info(" subTotal.negate() = "+subTotal.negate()+" || "+freight);
			taxandTotal[0] = subTotal;
			if (taxable == 1) {
				taxTotal = subTotal.multiply(new BigDecimal(taxRate));
				taxTotal = taxTotal.divide(taxdivisor);
				Total = subTotal.add(taxTotal).add(freight);
				System.out.println(" taxTotal = "+taxTotal+" || taxTotal = "+taxTotal+" || Total = "+Total);
			} else {
				taxTotal = new BigDecimal(0);
				if(freight==null)
					Total = subTotal;
				else
					Total = subTotal.add(freight);
				System.out.println(" taxTotal = "+taxTotal+" || Total = "+Total);
			}
			taxandTotal[1] = taxTotal;
			taxandTotal[2] = Total;
			System.out.println(" subTotal = "+subTotal+" || taxTotal = "+taxTotal+" || Total = "+Total);
		} catch (NullPointerException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			subTotal = null;
			Total = null;
			taxTotal = null;
			taxdivisor = null;
		}

		return taxandTotal;

	}

	/**
	 * Description: This method is used for computing the tax, subtotal and
	 * total of sales orders and customer invoice.
	 * 
	 * @param: therxMasterId {@link Integer}, theOper {@link String}
	 * @return: aRxaddress {@link Rxaddress}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/getBilltoAddress", method = RequestMethod.GET)
	public @ResponseBody
	Rxaddress getBilltoAddress(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			@RequestParam(value = "oper", required = false) String theOper,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Rxaddress aRxaddress = null;

		try {
			if (therxMasterId != null) {
				aRxaddress = new Rxaddress();
				aRxaddress = jobService.getRxMasterBillAddress(therxMasterId,
						theOper);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"SalesOrderController",e,session,theRequest);
		}
		return aRxaddress;
	}

	/**
	 * Description: This method is used for retrieving the customer address.
	 * 
	 * @param: therxMasterId {@link Integer}
	 * @return: aRxaddress {@link Rxaddress}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/getCustomerName", method = RequestMethod.GET)
	public @ResponseBody
	Rxaddress getCustomerAddress(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Rxaddress aRxaddress = null;
		try {
			if (therxMasterId != null) {
				aRxaddress = new Rxaddress();
				aRxaddress = jobService.getRxAddress(therxMasterId);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"SalesOrderController",e,session,theRequest);
		}
		return aRxaddress;
	}

	/**
	 * Description: This method is used for inserting new Sales Order.
	 * 
	 * @param: cuso {@link Cuso}
	 * @return: acuso {@link Cuso}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */
/*
	@RequestMapping(value = "/addSoRelease", method = RequestMethod.POST)
	public @ResponseBody
	Cuso InsertSORelease(
			@RequestParam(value = "cuSoid", required = false) Integer thecuSOID,// required=true
			@RequestParam(value = "createdOn", required = false) Date createdOn,
			@RequestParam(value = "ShipDate", required = false) Date ShipDate,
			@RequestParam(value = "cuAssignmentId0", required = false) Integer cuAssignmentId0,
			@RequestParam(value = "cuAssignmentId1", required = false) Integer cuAssignmentId1,
			@RequestParam(value = "cuAssignmentId2", required = false) Integer cuAssignmentId2,
			@RequestParam(value = "cuAssignmentId3", required = false) Integer cuAssignmentId3,
			@RequestParam(value = "cuAssignmentId4", required = false) Integer cuAssignmentId4,
			@RequestParam(value = "prFromWarehouseId", required = false) Integer prFromWarehouseId,
			@RequestParam(value = "veShipViaId", required = false) Integer veShipViaId,
			@RequestParam(value = "coDivisionID", required = false) Integer coDivisionID,
			@RequestParam(value = "datePromised", required = false) String datePromised,
			@RequestParam(value = "customerPonumber", required = false) String customerPonumber,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "freight", required = false) BigDecimal freight,
			@RequestParam(value = "trackingNumber", required = false) String thePoNumber,
			@RequestParam(value = "cuTermsId", required = false) Integer cuTermsId,
			@RequestParam(value = "coTaxTerritoryId", required = false) Integer coTaxTerritoryId,
			@RequestParam(value = "joScheddDetailID", required = false) Integer joScheddDetailID,
			@RequestParam(value = "costTotal", required = false) BigDecimal costTotal,
			@RequestParam(value = "subTotal", required = false) BigDecimal subTotal,
			@RequestParam(value = "taxTotal", required = false) BigDecimal taxTotal,
			@RequestParam(value = "taxRate", required = false) BigDecimal taxRate,
			@RequestParam(value = "billToCustomerNameGeneralID", required = false) Integer billToCustomerNameGeneralID,
			@RequestParam(value = "shipTorxCustomer_ID", required = false) Integer shipTorxCustomer_ID,
			@RequestParam(value = "operation", required = false) String operation,
			@RequestParam(value = "customerShipToOtherID", required = false) String theCustomerShipToOtherID,
			@RequestParam(value = "shipToOtherNameID", required = false) String theShipToOtherNameID,
			@RequestParam(value = "shipToOtherAddress1", required = false) String theShipToOtherAddress1,
			@RequestParam(value = "shipToOtherAddress2", required = false) String theShipToOtherAddress2,
			@RequestParam(value = "shipToOtherCity", required = false) String theShipToOtherCity,
			@RequestParam(value = "shipToOtherState", required = false) String theShipToOtherState,
			@RequestParam(value = "shipToOtherZip", required = false) String theShipToOtherZip,
			@RequestParam(value = "rxShipToOtherAddressID", required = false) String therxShipToOtherAddressID,
			@RequestParam(value = "rxContactID", required = false) Integer rxContactID,
			@RequestParam(value = "prToWarehouseId", required = false) Integer prToWarehouseId,
			HttpSession theSession, ModelMap theModel,
			HttpServletResponse theResponse) throws IOException {
		Cuso acuSO = new Cuso();
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		Rxaddress theShipToOtherAddress = new Rxaddress();
		
		System.out.println("TTTTTTTEST=========>"+rxContactID);
		try {
			acuSO.setCuSoid(thecuSOID);
			if (cuAssignmentId0 != null) {
				acuSO.setCuAssignmentId0(cuAssignmentId0);
			}
			if (cuAssignmentId1 != null) {
				acuSO.setCuAssignmentId1(cuAssignmentId1);
			}
			if (cuAssignmentId2 != null) {
				acuSO.setCuAssignmentId2(cuAssignmentId2);
			}
			if (cuAssignmentId3 != null) {
				acuSO.setCuAssignmentId3(cuAssignmentId3);
			}
			if (cuAssignmentId4 != null) {
				acuSO.setCuAssignmentId4(cuAssignmentId4);
			}
			if (prFromWarehouseId != null
					&& prFromWarehouseId != -1 &&  prFromWarehouseId != 0) {
				acuSO.setPrFromWarehouseId(prFromWarehouseId);
			}
			if (prToWarehouseId != null &&  prToWarehouseId != -1 
					&& prToWarehouseId != 0) {
				acuSO.setPrToWarehouseId(prToWarehouseId);
			}
			if (veShipViaId != -1 && veShipViaId != null && veShipViaId != 0) {
				acuSO.setVeShipViaId(veShipViaId);
			}
			if (therxShipToOtherAddressID != null
					&& therxShipToOtherAddressID != "")
				acuSO.setRxShipToAddressId(Integer
						.valueOf(therxShipToOtherAddressID));

			if (coTaxTerritoryId != null && coTaxTerritoryId != 0)
				acuSO.setCoTaxTerritoryId(coTaxTerritoryId);
			acuSO.setCoDivisionID(coDivisionID);
			acuSO.setDatePromised(datePromised);
			acuSO.setCustomerPonumber(customerPonumber);
			acuSO.setTrackingNumber(thePoNumber);
			acuSO.setTag(tag);
			acuSO.setFreight(freight);
			acuSO.setCuTermsId(cuTermsId);
			acuSO.setChangedById(aUserBean.getUserId());
			acuSO.setShipDate(ShipDate);
			Date date = new Date();
			acuSO.setChangedOn(date);
			if (createdOn != null)
				acuSO.setCreatedOn(createdOn);

			acuSO.setJoScheddDetailID(joScheddDetailID);
			acuSO.setSubTotal(subTotal);
			acuSO.setTaxRate(taxRate);
			acuSO.setTaxTotal(taxTotal);
			acuSO.setCostTotal(costTotal);
			acuSO.setRxContactId(rxContactID);
			if ("create".equalsIgnoreCase(operation)
					|| "edit".equalsIgnoreCase(operation)
					|| "update".equalsIgnoreCase(operation)) {
				if (billToCustomerNameGeneralID != null
						&& billToCustomerNameGeneralID != 0) {
					acuSO.setRxCustomerId(billToCustomerNameGeneralID);
					acuSO.setRxBillToId(billToCustomerNameGeneralID);
				}

				// acuSO.setRxBillToAddressId(billToCustomerNameGeneralID);
				if (shipTorxCustomer_ID != null && shipTorxCustomer_ID != 0) {
					acuSO.setRxShipToId(shipTorxCustomer_ID);
				}
				if (shipTorxCustomer_ID != null)
					acuSO.setShipToMode((byte) 1);
				if (theCustomerShipToOtherID != null
						&& "Other".equalsIgnoreCase(theCustomerShipToOtherID)) {
					theShipToOtherAddress.setFax(theCustomerShipToOtherID);
					theShipToOtherAddress.setAddress1(theShipToOtherAddress1);
					theShipToOtherAddress.setAddress2(theShipToOtherAddress2);
					theShipToOtherAddress.setCity(theShipToOtherCity);
					theShipToOtherAddress.setState(theShipToOtherState);
					theShipToOtherAddress.setIsBillTo(false);
					theShipToOtherAddress.setInActive(false);
					theShipToOtherAddress.setIsMailing(false);
					theShipToOtherAddress.setIsStreet(false);
					theShipToOtherAddress.setIsShipTo(false);
					theShipToOtherAddress.setOtherShipTo(3);
					theShipToOtherAddress.setZip(theShipToOtherZip);
					// aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
					theShipToOtherAddress.setName(theShipToOtherNameID);
					// acuSO.setRxShipToAddressId(theCustomerShipToAddressID);
					acuSO.setShipToMode((byte) 2);
				}

				if ("Pick Up".equalsIgnoreCase(theCustomerShipToOtherID)) {
					acuSO.setShipToMode((byte) 1);
				} else if ("Customer"
						.equalsIgnoreCase(theCustomerShipToOtherID)) {
					acuSO.setShipToMode((byte) 2);
				} else if ("Other".equalsIgnoreCase(theCustomerShipToOtherID)) {
					acuSO.setShipToMode((byte) 3);
				} else {
					acuSO.setShipToMode((byte) 4);
				}

			}
			if (!"create".equalsIgnoreCase(operation))
				acuSO = jobService
						.updateSOGeneral(acuSO, theShipToOtherAddress);
			else {
				
				 * acuSO.setRxCustomerId(billToCustomerNameGeneralID);
				 * acuSO.setRxBillToId(billToCustomerNameGeneralID);
				 * //acuSO.setRxBillToAddressId(billToCustomerNameGeneralID);
				 * acuSO.setRxShipToId(shipTorxCustomer_ID);
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
				 * acuSO.setShipToMode((byte)2); }
				 
				acuSO.setTransactionStatus(1);
				acuSO = jobService.addSOGeneral(acuSO, theShipToOtherAddress);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		if ("create".equalsIgnoreCase(operation)
				|| "edit".equalsIgnoreCase(operation)) {
			theModel.addAttribute("jobNumber", acuSO.getJoReleaseId());
		}
		return acuSO;
	}*/

	@RequestMapping(value = "/addSoRelease", method = RequestMethod.POST)
	public @ResponseBody
	Cuso InsertSORelease(
	@RequestParam(value = "cuSoid", required = false) Integer thecuSOID,// required=true
	@RequestParam(value = "createdOn", required = false) Date createdOn,
	@RequestParam(value = "ShipDate", required = false) Date ShipDate,
	@RequestParam(value = "cuAssignmentId0", required = false) Integer cuAssignmentId0,
	@RequestParam(value = "cuAssignmentId1", required = false) Integer cuAssignmentId1,
	@RequestParam(value = "cuAssignmentId2", required = false) Integer cuAssignmentId2,
	@RequestParam(value = "cuAssignmentId3", required = false) Integer cuAssignmentId3,
	@RequestParam(value = "cuAssignmentId4", required = false) Integer cuAssignmentId4,
	@RequestParam(value = "prFromWarehouseId", required = false) Integer prFromWarehouseId,
	@RequestParam(value = "veShipViaId", required = false) Integer veShipViaId,
	@RequestParam(value = "coDivisionID", required = false) Integer coDivisionID,
	@RequestParam(value = "datePromised", required = false) String datePromised,
	@RequestParam(value = "customerPonumber", required = false) String customerPonumber,
	@RequestParam(value = "tag", required = false) String tag,
	@RequestParam(value = "freight", required = false) BigDecimal freight,
	@RequestParam(value = "trackingNumber", required = false) String thePoNumber,
	@RequestParam(value = "cuTermsId", required = false) Integer cuTermsId,
	@RequestParam(value = "coTaxTerritoryId", required = false) Integer coTaxTerritoryId,
	@RequestParam(value = "joScheddDetailID", required = false) Integer joScheddDetailID,
	@RequestParam(value = "costTotal", required = false) BigDecimal costTotal,
	@RequestParam(value = "subTotal", required = false) BigDecimal subTotal,
	@RequestParam(value = "taxTotal", required = false) BigDecimal taxTotal,
	@RequestParam(value = "taxRate", required = false) BigDecimal taxRate,
	@RequestParam(value = "billToCustomerNameGeneralID", required = false) Integer billToCustomerNameGeneralID,
	@RequestParam(value = "shipTorxCustomer_ID", required = false) Integer shipTorxCustomer_ID,
	@RequestParam(value = "operation", required = false) String operation,
	@RequestParam(value = "customerShipToOtherID", required = false) String theCustomerShipToOtherID,
	@RequestParam(value = "shipToName", required = false) String theShipToOtherNameID,
	@RequestParam(value = "shipToAddress1", required = false) String theShipToOtherAddress1,
	@RequestParam(value = "shipToAddress2", required = false) String theShipToOtherAddress2,
	@RequestParam(value = "shipToCity", required = false) String theShipToOtherCity,
	@RequestParam(value = "shipToState", required = false) String theShipToOtherState,
	@RequestParam(value = "shipToZip", required = false) String theShipToOtherZip,
	@RequestParam(value = "rxShipToOtherAddressID", required = false) String therxShipToOtherAddressID,
	@RequestParam(value = "rxContactID", required = false) Integer rxContactID,
	@RequestParam(value = "prToWarehouseId", required = false) Integer prToWarehouseId,
	@RequestParam(value = "commissionSplitGridData",required = false) String commissionSplitGridData,
	@RequestParam(value = "commissionSplitdelData[]",required = false) ArrayList<String>  commissionSplitDelData,
	@RequestParam(value = "shiptoCuAddressIDfortoggle", required = false) Integer shiptoCuAddressIDfortoggle,
	@RequestParam(value = "rxShiptoid", required = false) Integer theShiptoid,
	@RequestParam(value = "rxShiptomodevalue", required = false) byte therxShiptomodevalue,
	@RequestParam(value = "withpriceStatus", required = false) boolean withpriceStatus,
	@RequestParam(value = "sotaxfreight", required = false) byte sotaxfreight,
	HttpSession theSession, ModelMap theModel,
	HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
	Cuso acuSO = new Cuso();
	UserBean aUserBean;
	aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
	Rxaddress theShipToOtherAddress = new Rxaddress();
	Boolean saved = false;
	Ecsplitjob ecsplitjob = null;
	Jomaster aJomaster = new Jomaster();
	System.out.println("TTTTTTTEST=========>"+rxContactID+" || ShipDate = "+ShipDate+" ||"+withpriceStatus);
	try {
	acuSO.setCuSoid(thecuSOID);
	if (cuAssignmentId0 != null) {
	acuSO.setCuAssignmentId0(cuAssignmentId0);
	}
	if (cuAssignmentId1 != null) {
	acuSO.setCuAssignmentId1(cuAssignmentId1);
	}
	if (cuAssignmentId2 != null) {
	acuSO.setCuAssignmentId2(cuAssignmentId2);
	}
	if (cuAssignmentId3 != null) {
	acuSO.setCuAssignmentId3(cuAssignmentId3);
	}
	if (cuAssignmentId4 != null) {
	acuSO.setCuAssignmentId4(cuAssignmentId4);
	}
	if (prFromWarehouseId != null
	&& prFromWarehouseId != -1 && prFromWarehouseId != 0) {
	acuSO.setPrFromWarehouseId(prFromWarehouseId);
	}

	if (veShipViaId != -1 && veShipViaId != null && veShipViaId != 0) {
	acuSO.setVeShipViaId(veShipViaId);
	}

	if (coTaxTerritoryId != null && coTaxTerritoryId != 0)
	acuSO.setCoTaxTerritoryId(coTaxTerritoryId);
	acuSO.setCoDivisionID(coDivisionID);
	acuSO.setDatePromised(datePromised);
	acuSO.setCustomerPonumber(customerPonumber);
	if(sotaxfreight==1 || sotaxfreight==0){
		acuSO.setTaxfreight(sotaxfreight);
	}else{
		acuSO.setTaxfreight((byte) 0);
	}
	
 	
 	if(thePoNumber.contains("<apm>")){
 		thePoNumber = thePoNumber.replaceAll("<apm>", "&");
 	}
 	if(thePoNumber.contains("<hah>")){
 		thePoNumber = thePoNumber.replaceAll("<hah>", "#");
 	}
 	if(thePoNumber.contains("<psl>")){
 		thePoNumber = thePoNumber.replaceAll("<psl>", "+");
	}
 	if(thePoNumber.contains("<pcn>")){
 		thePoNumber = thePoNumber.replaceAll("<pcn>", "%");
	}
	
	acuSO.setTrackingNumber(thePoNumber);
	acuSO.setTag(tag);
	acuSO.setFreight(freight);
	acuSO.setCuTermsId(cuTermsId);

	if(ShipDate != null )
	acuSO.setShipDate(ShipDate);

	acuSO.setJoScheddDetailID(joScheddDetailID);
	acuSO.setSubTotal(subTotal);
	acuSO.setTaxRate(taxRate);
	acuSO.setTaxTotal(taxTotal);
	acuSO.setCostTotal(costTotal);
	acuSO.setRxContactId(rxContactID);
	acuSO.setWithpriceStatus(withpriceStatus);
	
	if ("create".equalsIgnoreCase(operation)
	|| "edit".equalsIgnoreCase(operation)
	|| "update".equalsIgnoreCase(operation)) {
	if (billToCustomerNameGeneralID != null
	&& billToCustomerNameGeneralID != 0) {
	acuSO.setRxCustomerId(billToCustomerNameGeneralID);
	acuSO.setRxBillToId(billToCustomerNameGeneralID);
	}

	acuSO.setShipToMode(therxShiptomodevalue);
	
	if (therxShiptomodevalue == 0) {
		acuSO.setPrToWarehouseId(theShiptoid);
	}
	else if(therxShiptomodevalue == 1)
	{
		acuSO.setRxShipToId(theShiptoid);
	}
	else if(therxShiptomodevalue == 2)
	{
		acuSO.setRxShipToId(theShiptoid);
	}
	else if(therxShiptomodevalue == 3)
	{
	theShipToOtherAddress.setFax("Other");
	theShipToOtherAddress.setAddress1(theShipToOtherAddress1);
	theShipToOtherAddress.setAddress2(theShipToOtherAddress2);
	theShipToOtherAddress.setCity(theShipToOtherCity);
	theShipToOtherAddress.setState(theShipToOtherState);
	theShipToOtherAddress.setIsBillTo(false);
	theShipToOtherAddress.setInActive(false);
	theShipToOtherAddress.setIsMailing(false);
	theShipToOtherAddress.setIsStreet(false);
	theShipToOtherAddress.setIsShipTo(true);
	theShipToOtherAddress.setOtherShipTo(3);
	theShipToOtherAddress.setZip(theShipToOtherZip);
	// aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
	theShipToOtherAddress.setName(theShipToOtherNameID);
	acuSO.setRxShipToAddressId(theShiptoid);
	}

	}
	System.out.println(datePromised+"==createdOn==="+createdOn);
	if (!"create".equalsIgnoreCase(operation)){
	acuSO.setCreatedOn(createdOn);
	acuSO.setChangedById(aUserBean.getUserId());
	acuSO.setChangedOn(new Date());

	acuSO = jobService.updateSOGeneral(acuSO, theShipToOtherAddress);
	}
	else {
	//acuSO.setCreatedOn(new Date());
	acuSO.setCreatedOn(createdOn);
	acuSO.setCreatedById(aUserBean.getUserId());
	acuSO.setTransactionStatus(1);

	acuSO = jobService.addSOGeneral(acuSO, theShipToOtherAddress);
	}
	logger.info("SO SplitCommission GridData::"+commissionSplitGridData);
	logger.info("SO SplitCommission DelGridData::"+commissionSplitDelData);
	aJomaster = jobService.getSingleJobDetailsfromoutside(acuSO.getCuSoid());
	JsonParser parser = new JsonParser();
	if ( commissionSplitGridData !=null) {
		System.out.println("gridData"+commissionSplitGridData);
		JsonElement ele = parser.parse(commissionSplitGridData);
		JsonArray array = ele.getAsJsonArray();
		System.out.println("array length==>"+array.size());
		for (JsonElement ele1 : array) {
			JsonObject obj = ele1.getAsJsonObject();
			String ecSplitJobId=obj.get("ecSplitJobId").getAsString();
			Integer salesRepID=obj.get("rxMasterId").getAsInt();
			BigDecimal allocated=obj.get("allocated").getAsBigDecimal();
			String splittype=obj.get("splittype").getAsString();
			String ecSplitCodeID=obj.get("ecSplitCodeID").getAsString();
			
			ecsplitjob = new Ecsplitjob();
			logger.info("JoMaster ## joReleaseID ==>"+aJomaster.getJoMasterId() +" ## "+acuSO.getJoReleaseId());
			ecsplitjob.setJoReleaseID(acuSO.getJoReleaseId());
			ecsplitjob.setJoMasterId(aJomaster.getJoMasterId());
			ecsplitjob.setRxMasterId(salesRepID);
			ecsplitjob.setAllocated(allocated);
			if(!ecSplitCodeID.equals("")){
				ecsplitjob.setEcSplitCodeID(Integer.parseInt(ecSplitCodeID));
			}
			ecsplitjob.setSplittype(splittype);
			if (ecSplitJobId.equals("")) {
					ecsplitjob.setCreatedByID(aUserBean.getUserId());
					ecsplitjob.setCreatedOn(new Date());
					saved = jobService.addSplitCommission(ecsplitjob);
				}else{
					ecsplitjob.setEcSplitJobId(Integer.parseInt(ecSplitJobId));
					ecsplitjob.setChangedByID(aUserBean.getUserId());
					ecsplitjob.setChangedOn(new Date());
					saved = jobService.updateSplitCommission(ecsplitjob);
				}	
		}
	}
	if (commissionSplitDelData!=null && commissionSplitDelData.size()>0) {
		for(String detailID:commissionSplitDelData){
			System.out.println("delData[i]"+detailID);
			Integer ecSplitJobIdDelete=JobUtil.ConvertintoInteger(detailID);
			saved = jobService.deleteSplitCommission(ecSplitJobIdDelete);
		}
	}
	if(acuSO.getCuSoid()>0){
	jobService.updateTaxableandNonTaxableforCuSO(acuSO);
	}
	} catch (JobException e) {
		
	logger.error(e.getMessage(), e);
	e.printStackTrace();
	theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
	sendTransactionException("<b>thecuSOID:</b>"+thecuSOID,"SalesOrderController",e,theSession,theRequest);
	}
	if ("create".equalsIgnoreCase(operation)
	|| "edit".equalsIgnoreCase(operation)) {
	theModel.addAttribute("jobNumber", acuSO.getJoReleaseId());
	}
	return acuSO;
	}
	/**
	 * Description: This method is used for inserting new Sales Order line
	 * items.
	 * 
	 * @param: {@link Cusodetail} attributes of Cusodetail Object.
	 * @return: saved {@link Boolean}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/SaveSOLines", method = RequestMethod.POST)
	public @ResponseBody
	Boolean saveSOLinesDetails(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			@RequestParam(value = "subTotal", required = false) BigDecimal subTotal,
			@RequestParam(value = "Total", required = false) BigDecimal total,
			@RequestParam(value = "frieght", required = false) BigDecimal thefrieght,
			@RequestParam(value = "taxValue", required = false) BigDecimal taxValue,
			@RequestParam(value = "whseCost", required = false) BigDecimal whseCost,
			@RequestParam(value = "whseCostTotal", required = false) BigDecimal whseCostTotal,
			@RequestParam(value = "cusoDetailID", required = false) Integer cuSoDetailID,
			
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Cuso aCuso = new Cuso();
		Cusodetail aCusodetail = new Cusodetail();
		Boolean saved = false;
		
		try {
			aCuso.setCuSoid(cuSOID);
			aCuso.setFreight(thefrieght);
			aCuso.setSubTotal(subTotal);
			aCuso.setCostTotal(whseCostTotal);
			aCuso.setTaxTotal(taxValue);
			aCuso.setWhseCostTotal(whseCostTotal);
			
			saved = jobService.saveSoLines(aCuso);
			aCusodetail.setCuSodetailId(cuSoDetailID);
			aCusodetail.setWhseCost(whseCost);
			saved = jobService.saveCusoDetailWhseCost(aCusodetail);
			
			
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}
		return saved;
	}

	/**
	 * Description: This method is used for getting job site address.
	 * 
	 * @param: jobnumber {@link String}
	 * @return: aJomaster {@link Jomaster}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/getJobsiteAddress", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster getJobSiteAddress(
			@RequestParam(value = "jobnumber", required = false) String jobnumber,
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();

		try {
			if (jobnumber != null) {
				System.out.println("JobNumber is:" + jobnumber);
				Cuso aCuso = jobService.getCusoObj(cuSOID);
				aJomaster=jobService.getJobNumberFromRelease(aCuso.getJoReleaseId());
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>jobnumber:</b>"+jobnumber,"SalesOrderController",e,session,theRequest);
		}
		return aJomaster;
	}
	
	
	@RequestMapping(value = "/getJobsiteAddressfromoutside", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster getJobsiteAddressfromoutside(@RequestParam(value = "cusoid", required = false) Integer cusoid,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();

		try {
			if (cusoid != null) {
				aJomaster = jobService.getSingleJobDetailsfromoutside(cusoid);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>jobnumber:</b>"+cusoid,"SalesOrderController",e,session,theRequest);
		}
		return aJomaster;
	}
	

	/**
	 * Description: This method is used for checking wether a invoice exists or
	 * not for the given sales order ID. It is the method called initially in
	 * customer invoice, to load the customer invoice line items or not
	 * 
	 * @param: CuSOID {@link Integer}
	 * @return: Exists {@link Boolean}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/CheckCuinvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody
	Boolean CheckCuinvoiceDetails(
			@RequestParam(value = "CuSOID", required = false) Integer cuSOID,
			@RequestParam(value = "joReleaseDetailid", required = false) Integer joReleaseDetailid,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Boolean Exists = true;

		try {
			if (cuSOID != 0 && cuSOID != null) {
				Exists = jobService.checkInitialSaveCuInvoice(cuSOID,
						joReleaseDetailid);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}
		return Exists;
	}

	/**
	 * Description: This method is used for getting job site address.
	 * 
	 * @param: jobnumber {@link String}
	 * @return: aJomaster {@link Jomaster}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/cuInvlineitemGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getcuInvLineItems(
			@RequestParam(value = "cuInvoiceID", required = false) Integer cuInvoiceID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws JobException, IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();

		try {
			List<?> aSOLineItemDetails = jobService.getcuInvoiceReleaseLineItem(cuInvoiceID);
			aResponse.setRows(aSOLineItemDetails);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"SalesOrderController",e,session,theRequest);
		}
		return aResponse;
	}

	/**
	 * Description: This method is used for add, edit and delete the invoice.
	 * (CRUD Operations)
	 * 
	 * @param: cuInvoice {@link Cuinvoice} attributes of invoices.
	 * @return: {@link Cuinvoice}
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 * */

	@RequestMapping(value = "/manpulatecuInvoiceReleaseLineItem", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse manpulateCuInvoiceReleaseLineItem(
			@RequestParam(value = "cuInvoiceId", required = false) Integer cuInvoiceID,
			@RequestParam(value = "description", required = false) String desc,
			@RequestParam(value = "itemCode", required = false) String itemCode,
			@RequestParam(value = "note", required = false) String note,
			@RequestParam(value = "oper", required = false) String Oper,
			@RequestParam(value = "prMasterId", required = false) Integer prMasterID,
			@RequestParam(value = "priceMultiplier", required = false) BigDecimal priceMultiplies,
			@RequestParam(value = "quantityBilled", required = false) BigDecimal quantityOrder,
			@RequestParam(value = "taxable", required = false) String Taxable,
			@RequestParam(value = "unitCost", required = false) BigDecimal unitCost,
			@RequestParam(value = "cuInvoiceDetailId", required = false) Integer cuInvoiceDetailId,
			@RequestParam(value = "taxRate", required = false) Double taxRate,
			@RequestParam(value = "freight", required = false) BigDecimal freight,
			@RequestParam(value = "releasetype",required = false) String releasetype,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		Cuinvoicedetail aCuInvDetObj = new Cuinvoicedetail();
		Cuinvoice aCuInvObj = new Cuinvoice();
		Cuinvoicedetail aCusodetailObj = null;
		boolean saved = false;
		BigDecimal[] taxandTotal = new BigDecimal[3];
		BigDecimal[] editTaxTotal = new BigDecimal[3];
		
		if(unitCost==null)
			unitCost = BigDecimal.ZERO;
		if(quantityOrder == null)
			quantityOrder = BigDecimal.ZERO;
		if(priceMultiplies == null)
			priceMultiplies = BigDecimal.ZERO;

		try {
			if (null != cuInvoiceDetailId)
				aCusodetailObj = jobService
						.getSingleCuInvoiceDetailObj(cuInvoiceDetailId);
			if (Oper.equalsIgnoreCase("del")) {
				aCuInvDetObj.setCuSodetailId(cuInvoiceDetailId);
				taxandTotal = computeTaxTotal(aCusodetailObj.getUnitCost(),
						aCusodetailObj.getPriceMultiplier(),
						aCusodetailObj.getQuantityBilled(),
						aCusodetailObj.getTaxable(), taxRate, freight);
				aCuInvObj.setTaxTotal(taxandTotal[1]);
				aCuInvObj.setCuInvoiceId(cuInvoiceID);
				saved = jobService.deleteCUInvReleaseLineItem(aCuInvDetObj,
						aCuInvObj);
			} else {
				aCuInvDetObj.setDescription(desc);
				aCuInvDetObj.setPrMasterId(prMasterID);
				aCuInvDetObj.setPriceMultiplier(priceMultiplies);
				aCuInvDetObj.setQuantityBilled(quantityOrder);
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("on") || Taxable.equals("Yes")) {
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
				if (Oper.equalsIgnoreCase("add")) {
					saved = jobService.addcuinvoiceReleaseLineItem(
							aCuInvDetObj, aCuInvObj);
				} else if (Oper.equalsIgnoreCase("edit")) {
					Cuinvoice editCuInvObj = new Cuinvoice();
					editCuInvObj.setCuInvoiceId(cuInvoiceID);
					byte oldTaxEnabled = aCusodetailObj.getTaxable();
					byte newTaxEnabled = aCuInvDetObj.getTaxable();
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
					aCuInvDetObj.setCuInvoiceDetailId(cuInvoiceDetailId);
					aCuInvObj.setReleaseType(releasetype);
					saved = jobService.editCuInvoiceReleaseLineItem(
							aCuInvDetObj, aCuInvObj, editCuInvObj);
				}
			}
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"SalesOrderController",e,session,theRequest);
		}
		return aResponse;
	}

	/**
	 * Description: This method is used for printing the sales order report for
	 * the releases.
	 * 
	 * @param: cusoID {@link Cuso} attributes of invoices.
	 * @return: {@link Void}
	 * @throws IOException
	 * @throws JobException 
	 * @throws MessagingException 
	 * @throws ProductException 
	 * @throws VendorException 
	 * @throws UserException 
	 * @exception: JobException
	 * */

	@RequestMapping(value = "/printSalesOrderReport", method = RequestMethod.GET)
	public @ResponseBody
	void printSalesOrderReport(
			@RequestParam(value = "cusoID", required = false) Integer cusoID,@RequestParam(value = "price", required = false) String price,
			@RequestParam(value="WriteorView", required= false) String WriteorView,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, JobException, MessagingException, ProductException, VendorException, UserException {
		Connection connection =null;InputStream imageStream =null;
			ConnectionProvider con = null;
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				String path_JRXML = null;
				JasperDesign jd  = null;
				String filename="";
				logger.info("print Price:"+price);
				Cuso cuso=jobService.getSingleCUSODetails(cusoID);
				if("NotChecked".equalsIgnoreCase(price))
				{//salesReportFinal.jrxml
					//salesReportFinalPrice
					filename="SalesOrderWithoutprice.pdf";
					path_JRXML = theRequest
							.getSession()
							.getServletContext()
							.getRealPath(
									"/resources/jasper_reports/salesReportFinalPrice1.jrxml");
					
					
					List<String> addlist=new ArrayList<String>();
					addlist.add("ShowWeightonPickTickets");
					addlist.add("ShowBinLocationonPickTickets");
					addlist.add("UseWarehousesaddressonPickTickets");
					ArrayList<Sysvariable> sysvariablelist=new ArrayList<Sysvariable>();
								
					try {
						sysvariablelist = userService.getInventorySettingsDetails(addlist);
					} catch (UserException e) {
						e.printStackTrace();
					}
					
					int i=0;
					boolean showWeight=false;
					boolean showBin=false;
					boolean WhsePickTicket=false;
					
					for (Sysvariable aSysvariable : sysvariablelist) {
						if (aSysvariable.getValueLong() != null) {
							if (aSysvariable.getValueLong() == 1) {
								if (i == 0) {
									showWeight = true;
								} else if (i == 1) {
									showBin = true;
								} else if (i == 2) {
									params.put("WarehousePickTicket", 1);
									WhsePickTicket=true;
								}
								// break;
							}
						}
						i = i + 1;
					}
					
					String warehouseAddress="";
					if(WhsePickTicket){
						Prwarehouse apPrwarehouse=productService.getWarehouseDetail(cuso.getPrFromWarehouseId());
						warehouseAddress="<div><div><font face='arial' size='1'><i><b>"+apPrwarehouse.getDescription()+" ,"+apPrwarehouse.getAddress1()+" ,"+apPrwarehouse.getCity()+" "+apPrwarehouse.getState()+" "+apPrwarehouse.getZip()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></i></font></div><div><br></div></div>";
					}
					params.put("WHSAddress", warehouseAddress);
					// Dynamically added the column the in jasper report

					jd = JRXmlLoader.load(path_JRXML);
					JRDesignBand band = new JRDesignBand();
					JRDesignBand detailband = new JRDesignBand();
					band.setHeight(20);
					band.setSplitType(SplitTypeEnum.STRETCH);
					detailband.setHeight(15);
					detailband.setSplitType(SplitTypeEnum.STRETCH);

					int k = 5;
					
					String[] colnames={"QTY","SHIPPED","B/O","ITEM CODE","DESCRIPTION","",""};
					String[] colfields={"$F{QuantityOrdered}"," "," ","$F{ItemCode}","$F{Description}","",""};
					
					if (showBin) {
						colnames[k]="BIN";
						colfields[k]="$F{Bin}";
						k = k + 1;
					}
					if (showWeight) {
						colnames[k]="TOTAL WEIGHT";
						colfields[k]="$F{lbz}+\"lbz\"+$F{ounces}+\"oz\"";
						k = k + 1;
						
					}
					int xp=5,xp1=5;int Desc_wght=220;
					for(i=0;i<k;i++){
						DynamicReportbean bean=new DynamicReportbean();
						//bean.setX((590/k)*i);
						bean.setY(0);
						System.out.println("X ="+ i+ "\t Xp="+xp);
						if (colnames[i].equalsIgnoreCase("QTY") || colnames[i].equalsIgnoreCase("B/O")){
							bean.setX(xp);
							bean.setWidth(30);
							xp+=30;
						}else if ( colnames[i].equalsIgnoreCase("ITEM CODE")) {
							bean.setX(xp);
							bean.setWidth(100);
							xp+=100;
						}else if ( colnames[i].equalsIgnoreCase("SHIPPED")) {
							bean.setX(xp);
							bean.setWidth(55);
							xp+=55;
						}else if ( colnames[i].equalsIgnoreCase("BIN")) {
							bean.setX(xp);
							bean.setWidth(45);
							xp+=45;
						}else if(colnames[i].equalsIgnoreCase("DESCRIPTION")){
							bean.setX(xp);
							if(!showBin){
								Desc_wght=Desc_wght+45;
							}
							if(!showWeight){
								Desc_wght=Desc_wght+85;
							}
							bean.setWidth(Desc_wght);
							xp+=Desc_wght;
						}else if ( colnames[i].equalsIgnoreCase("TOTAL WEIGHT")) {
							bean.setX(xp);
							bean.setWidth(85);
							xp+=85;
						}
						else{
							bean.setX(xp);
							bean.setWidth(220);
							xp+=220;
						}
						
						bean.setHeight(15);
						bean.setText(colnames[i]);
						bean.setPrintRepeatedValues(true);
						bean.setFontsize(8);
						bean.setBold(true);
						bean.setUnderline(true);
						bean.setMarkup("html");
							
						JRDesignStaticText headerStaticText=ReportService.AddColumnHeader(bean);
						headerStaticText.setFontSize(11);
						if(colnames[i].equalsIgnoreCase("BIN")){
							headerStaticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
						}
						band.addElement(headerStaticText);
						jd.setColumnHeader(band);
						if (!colnames[i].equalsIgnoreCase("SHIPPED")
								&& !colnames[i].equalsIgnoreCase("B/O")) {
							DynamicReportbean tfbean = new DynamicReportbean();
							//tfbean.setX((590 / k) * i);
							tfbean.setY(0);
							System.out.println("X ="+i+ "\t Xp="+xp1);
							if(colnames[i].equalsIgnoreCase("QTY")){
								tfbean.setX(xp1);
								tfbean.setWidth(30);
								xp1+=30;
							}else if ( colnames[i].equalsIgnoreCase("ITEM CODE")) {
								tfbean.setX(xp1);
								tfbean.setWidth(100);
								xp1+=100;
							}else if ( colnames[i].equalsIgnoreCase("BIN")) {
								tfbean.setX(xp1);
								tfbean.setWidth(45);
								xp1+=45;
							}else if (colnames[i].equalsIgnoreCase("TOTAL WEIGHT") ){
								tfbean.setX(xp1+2);
								tfbean.setWidth(85);
								xp1+=85;
							}else if(colnames[i].equalsIgnoreCase("DESCRIPTION")){
								tfbean.setX(xp1);
								tfbean.setWidth(Desc_wght);
								xp1+=Desc_wght;
							}else{
								tfbean.setX(xp1);
								tfbean.setWidth(220);
								xp1+=220;
							}
							tfbean.setHeight(15);
							tfbean.setBold(false);
							tfbean.setUnderline(false);
							System.out.println("colfields["+i+"]=="+colfields[i]);
							tfbean.setText(colfields[i]);
							JRDesignTextField textField = ReportService
									.AddColumnTextField(tfbean);
							if (colnames[i].equalsIgnoreCase("QTY")) {
								textField.setPattern("###0.####");
							}
							detailband.addElement(textField);
						} else {
							bean = new DynamicReportbean();
							//bean.setX((590 / k) * i);
							//bean.setWidth(100);
							bean.setY(0);
							if (colnames[i].equalsIgnoreCase("B/O")){
								bean.setX(xp1);
								bean.setWidth(30);
								xp1+=30;
							}else if ( colnames[i].equalsIgnoreCase("SHIPPED")) {
													bean.setX(xp1);
								bean.setWidth(55);
								xp1+=55;
							}
							bean.setHeight(15);
							bean.setText("   ");
							bean.setPrintRepeatedValues(true);
							bean.setFontsize(8);
							bean.setBold(true);
							bean.setUnderline(true);
							bean.setMarkup("html");
							headerStaticText = ReportService.AddColumnHeader(bean);
							headerStaticText.setFontSize(11);
							detailband.addElement(headerStaticText);
						}
			       }
					
					JRDesignBand indetailband = new JRDesignBand();
					indetailband.setHeight(15);


					DynamicReportbean notesbean=new DynamicReportbean();
					//notesbean.setX(20);
					notesbean.setX(220);
					notesbean.setY(0);
					//notesbean.setWidth(570);
					notesbean.setWidth(Desc_wght);
					notesbean.setHeight(10);
					notesbean.setText("$F{Note}");
					notesbean.setMarkup("html");
					JRDesignTextField notestextField = ReportService.AddColumnTextField(notesbean);
					notestextField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
					notestextField.setRemoveLineWhenBlank(true);
					notestextField.setPrintWhenDetailOverflows(true);
					notestextField.setFontName("Times New Roman");
					notestextField.setFontSize(8);
					notestextField.setPdfEmbedded(true);
					notestextField.setPdfFontName("Times New Roman");
					indetailband.addElement(notestextField);
					JRDesignSection overallreport=((JRDesignSection)jd.getDetailSection());
					overallreport.addBand(detailband);
					overallreport.addBand(indetailband);

					//((JRDesignSection)jd.getDetailSection()).addBand(detailband);
					
					
				}
				else
				{
					filename="SalesOrderWithprice.pdf";
					path_JRXML = theRequest
							.getSession()
							.getServletContext()
							.getRealPath(
									"/resources/jasper_reports/salesReportFinal.jrxml");
					ArrayList<Sysvariable> sysvariablelist=new ArrayList<Sysvariable>();
					List<String> addlist=new ArrayList<String>();
					addlist.add("RemoveEXTLISTcolumnfromSalesOrderPDF");
					addlist.add("RemoveMULTcolumnfromSalesOrderPDF");
					
					sysvariablelist = userService.getInventorySettingsDetails(addlist);
					;
					Boolean removeextlst=false;
					Boolean removemult=false;
					if (sysvariablelist.get(0).getValueLong() == 1) {
							removeextlst = true;
					}
					if (sysvariablelist.get(1).getValueLong() == 1) {
						removemult = true;
					}
					jd = JRXmlLoader.load(path_JRXML);
					JRDesignBand jdb=(JRDesignBand) jd.getColumnHeader();
					JRDesignSection jds=(JRDesignSection) jd.getDetailSection();
					JRBand jrb=jds.getBandsList().get(0);
					int width=0;
					if(removeextlst){
						width=width+65;
					}
					if(removemult){
						width=width+55;
					}

					JRDesignElement jr3h=(JRDesignElement) jdb.getElementByKey("column2header");
					jr3h.setWidth(jr3h.getWidth()+width);
					JRDesignElement  jr3d=(JRDesignElement) jrb.getElementByKey("column7detail1");
					jr3d.setWidth(jr3d.getWidth()+width);
					JRDesignElement jr4h=(JRDesignElement) jdb.getElementByKey("column4header");
					jr4h.setX(jr3h.getX()+jr3h.getWidth()+4);
					JRDesignElement  jr4d=(JRDesignElement) jrb.getElementByKey("column3detail1");
					jr4d.setX(jr4h.getX());
					JRDesignElement jr5h=(JRDesignElement) jdb.getElementByKey("column5header");
					jr5h.setX(jr4h.getX()+jr4h.getWidth()+4);
					JRDesignElement  jr5d=(JRDesignElement) jrb.getElementByKey("column4detail1");
					jr5d.setX(jr5h.getX());
					JRDesignElement jr6h=(JRDesignElement) jdb.getElementByKey("column6header");
					JRDesignElement  jr6d=(JRDesignElement) jrb.getElementByKey("column5detail1");
					params.put("removeextlst",!removeextlst);
					params.put("removeMult",!removemult);
					System.out.println("Test");
				}
				
				List<String>  Shiptomodeaddresslist=salesServices.getsalesordershiptoAddressDetails(cuso);
				Rxaddress rxadd = new Rxaddress();
				
				if(cuso.getShipToMode()==0){
					rxadd = itsPurchaseService.getSelectedShiptoaddress("warehouse", cuso.getPrToWarehouseId());
					}else if(cuso.getShipToMode()==1){
					rxadd = itsPurchaseService.getSelectedShiptoaddress("customer", cuso.getRxShipToId());
					}else if(cuso.getShipToMode()==2){
					rxadd = itsPurchaseService.getSelectedShiptoaddress("jobsite", cuso.getRxShipToId());
					}else if(cuso.getShipToMode()==3){
					rxadd = itsPurchaseService.getSelectedShiptoaddress("other", cuso.getRxShipToAddressId());
					}
				if(rxadd!=null)
				{
				params.put("billName",rxadd.getName());
				params.put("billAddress1",rxadd.getAddress1());
				params.put("billAddress2",rxadd.getAddress2());
				params.put("billCity",rxadd.getCity());
				params.put("BilState",rxadd.getState());
				params.put("BillZip",rxadd.getZip());
				}
				else
				{
					params.put("billName","");
					params.put("billAddress1","");
					params.put("billAddress2","");
					params.put("billCity","");
					params.put("BilState","");
					params.put("BillZip","");
				}
				
				//params.put("billCity",billCity);
				//List<String>  soldtomodeaddresslist=salesServices.getsalesorder_soldtoAddressDetails(cuso);
				Rxaddress aRxaddress = new Rxaddress();
				aRxaddress = jobService.getRxMasterBillAddress(cuso.getRxBillToId(),"bill");
				
				if(aRxaddress==null||(aRxaddress.getAddress1()==""&& aRxaddress.getAddress2()==""))
					aRxaddress = jobService.getRxMasterBillAddress(cuso.getRxBillToId(),"billTo");
				
				params.put("soldtoName",aRxaddress.getName());
				params.put("soldtoAddress1",aRxaddress.getAddress1());
				params.put("soldtoAddress2",aRxaddress.getAddress2());
				params.put("soldtoCity",aRxaddress.getCity());
				params.put("soldtoState",aRxaddress.getState());
				params.put("soldtoZip",aRxaddress.getZip());
				
				/*int l=0;
				String city="";
				for(String value:soldtomodeaddresslist){
					if(l==0){
						params.put("soldtoName",value);
					}
					if(l==1){
						params.put("soldtoAddress1",value);
					}else if(l==2){
						params.put("soldtoAddress2",value==null || value.trim()==""?null:value);
					}else if(l==3){
						params.put("soldtoCity",value);
						//city =value;
					}else if(l==4){
						params.put("soldtoState",value);
						//city= city+","+ value;
					}else if(l==5){
						params.put("soldtoZip",value);
						//city= city+"  "+ value;
					}
					
					l=l+1;
				}*/
				//params.put("soldtoCity",city);
				params.put("cusoID", cusoID);
				
				
				String NoticeToParameter="";
				
				if(cuso.getJoReleaseId()!=null){
				Jomaster thejomaster=jobService.getJoMasterDetails(cuso.getJoReleaseId());
				String noticeID="";
				if(thejomaster.getNoticeId()!=null &&  thejomaster.getNoticeId()!=-1){
					if(thejomaster.getNoticeId()==0){
						noticeID="Notice to ";
					}else if(thejomaster.getNoticeId()==1){
						noticeID="24 Hrs Notice to ";
					}else if(thejomaster.getNoticeId()==2){
						noticeID="48 Hrs Notice to ";
					}
				}
				String contactname="";
				if(thejomaster.getOtherContact()!=null && thejomaster.getOtherContact()==0){
				if(thejomaster.getContactId()!=null&&thejomaster.getContactId()!=-1){
					Rxcontact thecontact=jobService.ContactsBasedonID(thejomaster.getContactId());
					contactname=thecontact.getFirstName()+" "+thecontact.getLastName()+" ";
				}
				}else{
					if(thejomaster.getContactName()!=null)
					contactname = thejomaster.getContactName()+" ";
				}
				String notice="";
				if(thejomaster.getNotice()!=null){
					notice=thejomaster.getNotice();
				}
				NoticeToParameter=noticeID+contactname+notice;
				}
				System.out.println("NoticeToParameter=="+NoticeToParameter);
				params.put("NoticeToParameter", NoticeToParameter);
				
				
				JoRelease thejorelease=jobService.getJoRelease(cuso.getJoReleaseId());
				
				con = itspdfService.connectionForJasper();
				connection = con.getConnection();
				
				TsUserSetting objtsusersettings = userService.getSingleUserSettingsDetails(1);
				 Blob blob =  objtsusersettings.getCompanyLogo();
				 imageStream =blob.getBinaryStream();
				params.put("HeaderImage", imageStream);
				params.put("SOFooterText", objtsusersettings.getSoFooterText());
				params.put("HeaderText",((objtsusersettings.getHeaderText().replaceAll("`and`nbsp;", " ")).replaceAll("`", "")).replaceAll("amp;"," "));
				
				
				
				
				if(WriteorView!=null&&WriteorView.equals("write")){
					filename = "SalesOrder_"+cuso.getSonumber()+".pdf";
					
					if("NotChecked".equalsIgnoreCase(price) && jd!=null){
						ReportService.dynamicWriteReportCall(theRequest,theResponse,params,"pdf",jd,filename,connection);
					}else{
						if(thejorelease.getReleaseType()==5){
							String Headername="Service Order";
							if(cuso.getTransactionStatus()==-2){
								Headername="Quotation";
							}
							params.put("HeaderName", Headername);
						}else{
							String Headername= "Sales Order";
							if(cuso.getTransactionStatus()==-2){
								Headername="Quotation";
							}
							params.put("HeaderName",Headername);
						}
						ReportService.WriteReportCall(theRequest,theResponse,params,"pdf",path_JRXML,filename,connection);
					}
				}else{
					if("NotChecked".equalsIgnoreCase(price) && jd!=null){
						ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
					}else{
						if(thejorelease.getReleaseType()==5){
							String Headername= "Service Order";
							if(cuso.getTransactionStatus()==-2){
								Headername="Quotation";
							}
							params.put("HeaderName", Headername);
						}else{
							String Headername= "Sales Order";
							if(cuso.getTransactionStatus()==-2){
								Headername="Quotation";
							}
							params.put("HeaderName", Headername);
						}
						ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
						//ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
					}
				}
			}  catch (JRException e) {
				logger.error(e.getMessage());
				theResponse.sendError(500, e.getMessage());
				sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
			} catch (SQLException e) {
				logger.error(e.getMessage());
				theResponse.sendError(500, e.getMessage());
				sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
			}finally{
				try {
					if(imageStream!=null){
						imageStream.close();
					}
					if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	
	@RequestMapping(value = "/printSalesOrderReport2", method = RequestMethod.GET)
	public @ResponseBody
	void printSalesOrderReport2(@RequestParam(value = "cusoID", required = false) Integer cusoID,@RequestParam(value = "price", required = false) String price,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session)
			throws IOException, JobException, MessagingException {	
		Connection connection =null;
			ConnectionProvider con = null;
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				String path_JRXML = null;
				if("NotChecked".equalsIgnoreCase(price))
				{//salesReportFinal.jrxml
					//salesReportFinalPrice
					path_JRXML = theRequest
							.getSession()
							.getServletContext()
							.getRealPath(
									"/resources/jasper_reports/salesReportFinalPrice.jrxml");
					List<String> addlist=new ArrayList<String>();
					addlist.add("ShowWeightonPickTickets");
					addlist.add("ShowBinLocationonPickTickets");
					addlist.add("UseWarehousesaddressonPickTickets");
					ArrayList<Sysvariable> sysvariablelist=new ArrayList<Sysvariable>();
					
					
					try {
						sysvariablelist = userService.getInventorySettingsDetails(addlist);
					} catch (UserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int i=0;
					for(Sysvariable aSysvariable:sysvariablelist){
						if(aSysvariable.getValueLong()!=null){
						if(aSysvariable.getValueLong()==1){
							if(i==0){
							params.put("showWeight",true);
							}else if(i==1){
							params.put("showBin",true);
							}else if(i==2){
								params.put("WarehousePickTicket",1);
							}
							
							//break;
						}
						}
						
						i=i+1;
					}
					
					
				}
				else
				{
					path_JRXML = theRequest
							.getSession()
							.getServletContext()
							.getRealPath(
									"/resources/jasper_reports/salesReportFinal.jrxml");
					
				}
				
				
				
				
				
				
				Cuso cuso=jobService.getSingleCUSODetails(cusoID);
				List<String>  Shiptomodeaddresslist=salesServices.getsalesordershiptoAddressDetails(cuso);
				int k=0;
				for(String value:Shiptomodeaddresslist){
					if(k==0){
						params.put("billName",value);
					}
					if(k==1){
						params.put("billAddress1",value);
					}
					if(k==2){
						params.put("billAddress2",value);
					}
					if(k==3){
						params.put("billCity",value);
					}
					if(k==4){
						params.put("BilState",value);
					}
					if(k==5){
						params.put("BillZip",value);
					}
					k=k+1;
				}
				
				
				con = itspdfService.connectionForJasper();
				
				params.put("cusoID", cusoID);
				ServletOutputStream out = theResponse.getOutputStream();
				theResponse.setHeader("Content-Disposition", "inline");
				theResponse.setContentType("application/pdf");
				connection = con.getConnection();
				JasperReport report;
				report = JasperCompileManager.compileReport(path_JRXML);
				JasperPrint print = JasperFillManager.fillReport(report, params,
						connection);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(print, baos);
				out.write(baos.toByteArray());
				out.flush();
				out.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
				theResponse.sendError(500, e.getMessage());
				sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
			} catch (JRException e) {
				logger.error(e.getMessage());
				theResponse.sendError(500, e.getMessage());
				sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
			} catch (SQLException e) {
				logger.error(e.getMessage());
				theResponse.sendError(500, e.getMessage());
				sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
			}finally{
				try {
					if(con!=null){
						con.closeConnection(connection);
						con=null;
						}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	@RequestMapping(value = "/saveSalesOrderReport", method = RequestMethod.GET)
	public @ResponseBody
	void saveSalesOrderReport(
			@RequestParam(value = "cusoID", required = false) Integer cusoID,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, MessagingException {
		Connection connection =null;
		FileOutputStream outputstream=null;
		ConnectionProvider con =null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/salesReportFinal.jrxml");
			con = itspdfService.connectionForJasper();
			params.put("cusoID", cusoID);
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			connection = con.getConnection();
			JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params,
					connection);
			
			File pdf = new File("/var/quotePDF/salesOrder.pdf"); //for Linux
			//File pdf = new File("F:\\Jenith\\salesOrder.pdf"); //for Windows
			
			System.out.println("------------------>>>>>Testing");
			outputstream=new FileOutputStream(pdf);
			JasperExportManager.exportReportToPdfStream(print,outputstream);
		} catch (IOException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
		} catch (JRException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>cusoID:</b>"+cusoID,"SalesOrderController",e,session,theRequest);
		}finally{
			outputstream.close();
			try {
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Description: This method is used to get {@link Vepo} Object when a Po is
	 * inserted as Customer Invoice Directly..
	 * 
	 * @param: vepoID {@link Vepo} attributes of vepo
	 * @return: {@link Vepo}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: VendorException
	 * */
	@RequestMapping(value = "/getvepo", method = RequestMethod.POST)
	public @ResponseBody
	Vepo getPOInsertedCuInvoice(
			@RequestParam(value = "vepoID", required = false) Integer vepoID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws JobException, IOException, MessagingException {
		Vepo aVepo = null;
		try {
			aVepo = itsVendorService.getVePo(vepoID);
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>vepoID:</b>"+vepoID,"SalesOrderController",e,session,theRequest);
		}
		return aVepo;
	}

	@RequestMapping(value = "/taxRateTerritory", method = RequestMethod.POST)
	public @ResponseBody
	CoTaxTerritory gettaxRateTerritory(
			@RequestParam(value = "coTaxTerritoryId", required = false) Integer coTaxTerritoryId,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws JobException, IOException, MessagingException {
		CoTaxTerritory aCoTaxTerritory = null;
		logger.info("TaxTerrirory Called::"+coTaxTerritoryId);
		try {
			aCoTaxTerritory = itsVendorService
					.getTaxRateTerritory(coTaxTerritoryId);
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>coTaxTerritoryId:</b>"+coTaxTerritoryId,"SalesOrderController",e,session,theRequest);
		}
		return aCoTaxTerritory;
	}

	@RequestMapping(value = "/getShipToOtherAddress", method = RequestMethod.GET)
	public @ResponseBody
	Rxaddress getShipToOtherAddress(
			@RequestParam(value = "addressID", required = false) Integer therxAddressId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Rxaddress aRxaddress = null;
		try {
			if (therxAddressId != null) {
				aRxaddress = new Rxaddress();
				aRxaddress = jobService
						.getRxAddressFromAddressID(therxAddressId);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxAddressId:</b>"+therxAddressId,"SalesOrderController",e,session,theRequest);
		}
		return aRxaddress;
	}

	@RequestMapping(value = "/getCustomerShipToAddress", method = RequestMethod.GET)
	public @ResponseBody
	Rxaddress getCustomerShipToAddress(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Rxaddress aRxaddress = null;
		try {
			if (therxMasterId != null) {
				aRxaddress = new Rxaddress();
				aRxaddress = jobService.getShipToAddress(therxMasterId);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"SalesOrderController",e,session,theRequest);
		}
		return aRxaddress;
	}
	
	@RequestMapping(value = "/getCustomerShipToAddressforSO", method = RequestMethod.GET)
	public @ResponseBody
	Rxaddress getCustomerShipToAddressforSO(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Rxaddress aRxaddress = null;
		try {
			if (therxMasterId != null) {
				aRxaddress = new Rxaddress();
				aRxaddress = jobService.getShipToAddressforSO(therxMasterId);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"SalesOrderController",e,session,theRequest);
		}
		return aRxaddress;
	}
	
	@RequestMapping(value = "/getCustomerShipToAddressforToggle", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getCustomerShipToAddressforToggle(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		List<Rxaddress> aRxaddress = null;
		try {
			if (therxMasterId != null) {
				aRxaddress = (List<Rxaddress>) jobService.getShipToAddressforToggle(therxMasterId);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"SalesOrderController",e,session,theRequest);
		}
		return aRxaddress;
	}

	@RequestMapping(value = "/getSONumber", method = RequestMethod.POST)
	public @ResponseBody
	Cuso getCustomerShipToAddress(
			@RequestParam(value = "SONumber", required = false) String theSONumber,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Cuso aCuso = null;
		try {
			if (theSONumber != null) {
				aCuso = new Cuso();
				aCuso = jobService.getCUSOobjFromSONumber(theSONumber);
			}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theSONumber:</b>"+theSONumber,"SalesOrderController",e,session,theRequest);
		}
		return aCuso;
	}

	@RequestMapping(value = "/customerInvoicelineitemGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getcustomerInvoicelineitemGrid(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		System.out.println("Line Items--->" + cuSOID);
		try {
			List<?> aSOLineItemDetails = jobService
					.getcustomerInvoicelineitem(cuSOID); /*
														 * Retrieve all sales
														 * orders from the
														 * service
														 */
			aResponse.setRows(aSOLineItemDetails); /*
													 * assign those objects to
													 * the custom response
													 */
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}

		return aResponse; /*
						 * Spring will automatically convert our CustomResponse
						 * as JSON object. This is triggered by the
						 * @ResponseBody annotation. It knows this because the
						 * JqGrid has set the headers to accept JSON format when
						 * it make a request Spring by default uses Jackson to
						 * convert the object to JSON
						 */
	}

	@RequestMapping(value = "/getPriceDetails", method = RequestMethod.POST)
	public @ResponseBody
	Cuinvoice getPriceDetails(
			@RequestParam(value = "cuInvoiceID", required = false) Integer cuInvoiceID,
			 HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session)
			throws IOException, JobException, MessagingException {
		Cuinvoice aCuinvoice = null;
		try {
			aCuinvoice = itsVendorService.getCuInvoiceObj(cuInvoiceID);
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>cuInvoiceID:</b>"+cuInvoiceID,"SalesOrderController",e,session,theRequest);
		}
		return aCuinvoice;
	}
	
	@RequestMapping(value = "/checkInvoiceNumber", method = RequestMethod.POST)
	public @ResponseBody
	String checkInvoiceNumber(
			@RequestParam(value = "sonumber", required = false) String sonumber,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		String aInvoiceNumber = null;
		try {
			aInvoiceNumber = itsVendorService.checkInvoiceNumber(sonumber);
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>sonumber:</b>"+sonumber,"SalesOrderController",e,session,theRequest);
		}
		return aInvoiceNumber;
	}
	
	@RequestMapping(value="/printBatchInvoice", method = RequestMethod.GET)
	public @ResponseBody void printBatchInvoice(@RequestParam(value = "batchInvoiceCuID", required = false) String batchInvoiceCuID,
			@RequestParam(value = "batchInvoiceFromDate", required = false) String batchInvoiceFromDate,
			@RequestParam(value = "batchInvoiceToDate", required = false) String batchInvoiceToDate,
			@RequestParam(value = "rangeInvoiceFrom", required = false) String rangeInvoiceFrom,
			@RequestParam(value = "rangeInvoiceTo", required = false) String rangeInvoiceTo,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session
		) throws IOException, MessagingException{
		Connection connection =null;TsUserSetting	objtsusersettings=null;
		ConnectionProvider con = null;
		InputStream imageStream =null;
		try{
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/BatchCustomerInvoice.jrxml");
			String filename="BatchInvoice.pdf";
			System.out.println("data "+batchInvoiceCuID+batchInvoiceFromDate+batchInvoiceToDate+rangeInvoiceFrom+rangeInvoiceTo);
			
			File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
			String absolutePath = file.getAbsolutePath();
			absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
			String ShipToPath="";
			if (OperatingSystemInfo.isWindows()) {
				logger.info("This is Windows");
				ShipToPath=absolutePath+"\\\\BillTo_ShipTo.jasper";
				absolutePath=absolutePath+"\\\\BatchInvoiceSubreport.jasper";
			} else if (OperatingSystemInfo.isMac()) {
				logger.info("This is Mac");
			} else if (OperatingSystemInfo.isUnix()) {
				logger.info("This is Unix or Linux");
				ShipToPath=absolutePath+"/BillTo_ShipTo.jasper";
				absolutePath=absolutePath+"/BatchInvoiceSubreport.jasper";
			} else if (OperatingSystemInfo.isSolaris()) {
				logger.info("This is Solaris");
			} else {
				logger.info("Your OS is not support!!");
			}
			
				String invoicefromdate=null;
				String invoicetodate=null;
				String yesterDayDate=JobUtil.getYesterdayDateString();
				if(batchInvoiceFromDate!=null && !batchInvoiceFromDate.trim().equals("")){
					SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
				    Date convertedCurrentDate = sdf1.parse(batchInvoiceFromDate);
				    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
				    invoicefromdate=sdff.format(convertedCurrentDate );
				    System.out.println("Formated From Date:"+invoicefromdate);
				}
				if(batchInvoiceToDate!=null && !batchInvoiceToDate.trim().equals("")){
					SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
				    Date convertedCurrentDate = sdf1.parse(batchInvoiceToDate);
				    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
				    invoicetodate=sdff.format(convertedCurrentDate );
				    System.out.println("Formated From Date:"+invoicetodate);
				}
				
			objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);	
			con = itspdfService.connectionForJasper();
			Blob blob =  objtsusersettings.getCompanyLogo();
			 imageStream =blob.getBinaryStream();
			//BufferedImage image = ImageIO.read(imageStream);
			params.put("HeaderImage", imageStream);
			params.put("HeaderText",((objtsusersettings.getHeaderText().replaceAll("`and`nbsp;", " ")).replaceAll("`", "")).replaceAll("amp;"," "));
			params.put("subReportPath", absolutePath);
			params.put("ShipToPath", ShipToPath);
			
			String remitTo = ( objtsusersettings.getRemitToDescription() !=null && ! objtsusersettings.getRemitToDescription().equals("") ? objtsusersettings.getRemitToDescription() +",": "" )
					+( objtsusersettings.getRemitToAddress1() !=null && !objtsusersettings.getRemitToAddress1().equals("") ?objtsusersettings.getRemitToAddress1()+"," : "" )
					+( objtsusersettings.getRemitToAddress2() !=null && !objtsusersettings.getRemitToAddress2().equals("") ? objtsusersettings.getRemitToAddress2()+"," : "" )
					+( objtsusersettings.getRemitToCity() !=null && !objtsusersettings.getRemitToCity().equals("") ? objtsusersettings.getRemitToCity()+"," : "" )
					+( objtsusersettings.getRemitToState() !=null && !objtsusersettings.getRemitToState().equals("") ? objtsusersettings.getRemitToState()+"," : "" )
					+ ( objtsusersettings.getRemitToZip() !=null && !objtsusersettings.getRemitToZip().equals("")? objtsusersettings.getRemitToZip() : "" );
			params.put("remitTo",remitTo );
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			
			connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
		   // String query ="SELECT joRelease.ReleaseType, tsUserLogin.Initials AS SalesRep,(CASE WHEN cuInvoiceDetail.PriceMultiplier IS NULL THEN (CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END *cuInvoiceDetail.QuantityBilled) ELSE ((CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END )*cuInvoiceDetail.QuantityBilled*cuInvoiceDetail.PriceMultiplier )END ) AS total, veShipVia.Description AS ShippedVia, joMaster.Description AS Job, cuInvoice.*, tsUserLogin.Initials,cuTerms.Description AS termsdesc,cuInvoice.ShipToMode AS ShipToMode, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Description ELSE      (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxM.Name FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxM.rxMasterId) ELSE         (CASE WHEN cuInvoice.ShipToMode=2 THEN (SELECT rxM.Name FROM rxMaster rxM WHERE rxM.rxMasterId =cuInvoice.rxCustomerID) ELSE            (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Name FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoName, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Address1 ELSE     (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.address1 FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId)  ELSE          (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationAddress1) ELSE             (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Address1 FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END)  END) END) AS shiptoAddress1, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Address2 ELSE (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.address2 FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId) ELSE  (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationAddress2) ELSE (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Address2 FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END)  END) END) AS shiptoAddress2,   (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.City ELSE (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.city FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId)   ELSE  (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationCity) ELSE (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.City FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoCity,  (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.State ELSE (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.state FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId)  ELSE  (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationState) ELSE (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.State FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoState, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Zip ELSE  (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.zip FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId) ELSE   (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationZip) ELSE  (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Zip FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoZip,  (SELECT rxMaster.Name FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID)  AS billtoName, (SELECT rxAddress.address1 FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoAddress1, (SELECT rxAddress.address2 FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoAddress2, (SELECT rxAddress.city FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoCity, (SELECT rxAddress.state FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoState, (SELECT rxAddress.zip FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoZip,  (SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo FROM (joMaster RIGHT JOIN joRelease ON joMaster.joMasterID = joRelease.joMasterID LEFT JOIN joReleaseDetail ON joReleaseDetail.joReleaseID=joRelease.joReleaseID) RIGHT JOIN (veShipVia RIGHT JOIN (cuInvoice LEFT JOIN tsUserLogin ON cuInvoice.cuAssignmentID0 = tsUserLogin.UserLoginID) ON veShipVia.veShipViaID = cuInvoice.veShipViaID) ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID LEFT JOIN cuInvoiceDetail ON cuInvoiceDetail.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN tsUserLogin ON tsUserLogin.UserLoginID = cuInvoice.cuAssignmentID0 LEFT JOIN cuTerms ON cuTerms.cuTermsID = cuInvoice.cuTermsID  LEFT JOIN prWarehouse ON prWarehouse.prWarehouseID=cuInvoice.rxShipToAddressID LEFT JOIN rxMaster ON rxMaster.rxMasterID=cuInvoice.rxCustomerID LEFT JOIN rxAddress ON rxAddress.rxMasterId=cuInvoice.rxCustomerID";
		  
		    String query ="SELECT (CASE WHEN cuInvoiceDetail.PriceMultiplier IS NULL THEN (CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END *cuInvoiceDetail.QuantityBilled) ELSE ((CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END )*cuInvoiceDetail.QuantityBilled*cuInvoiceDetail.PriceMultiplier )END ) AS total,"+
" joRelease.ReleaseType, tsUserLogin.Initials AS SalesRep,veShipVia.Description AS ShippedVia,"+
" cuInvoiceDetail.PriceMultiplier,cuInvoiceDetail.UnitCost,cuInvoiceDetail.QuantityBilled,"+
" joMaster.Description AS Job, cuInvoice.*, tsUserLogin.Initials,"+
" cuTerms.Description AS termsdesc,cuInvoice.ShipToMode AS ShipToMode,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.address1,billto.address1) AS address1,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.address2,billto.address2) AS address2,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.city,billto.city) AS city,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.state,billto.state) AS state,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.zip,billto.zip) AS zip,rxMaster.Name,"+
//" rxAddress.address1,rxAddress.address2,rxAddress.city,rxAddress.state,rxAddress.zip,rxMaster.Name,"+
" cuInvoice.prToWarehouseID AS prwarehouseid,cuInvoice.rxCustomerID AS customerID,joMaster.joMasterID,"+
" cuInvoice.rxShipToAddressID "+
" FROM cuInvoice"+
" LEFT JOIN cuInvoiceDetail ON cuInvoiceDetail.cuInvoiceID = cuInvoice.cuInvoiceID"+
" LEFT JOIN joReleaseDetail ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID"+
" LEFT JOIN joRelease ON joReleaseDetail.joReleaseID=joRelease.joReleaseID"+
" LEFT JOIN joMaster ON joMaster.joMasterID = joRelease.joMasterID"+
" LEFT JOIN cuTerms ON cuTerms.cuTermsID = cuInvoice.cuTermsID"+
" LEFT JOIN tsUserLogin ON tsUserLogin.UserLoginID = cuInvoice.cuAssignmentID0"+
" LEFT JOIN rxMaster ON rxMaster.rxMasterID=cuInvoice.rxCustomerID"+
" LEFT JOIN rxAddress ON (rxMaster.rxMasterID=rxAddress.rxMasterID AND rxAddress.IsMailing = 1)"+
" LEFT JOIN rxAddress billto ON (rxMaster.rxMasterID=billto.rxMasterID AND billto.IsDefault = 1)"+
" LEFT JOIN veShipVia ON (veShipVia.veShipViaID = cuInvoice.veShipViaID)";
		    
   
		    
if(batchInvoiceCuID.length()>0 && !batchInvoiceCuID.equals("0")){
	 if(!query.contains("WHERE")){
		 query=query+" WHERE  rxMaster.rxMasterID ="+batchInvoiceCuID;
		}else{
			query=query+" And  rxMaster.rxMasterID ="+batchInvoiceCuID;
		}
	
}


//From Date and ToDate Checking
/*if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0){
	if(!query.contains("WHERE")){
		query=query+ " WHERE cuInvoice.CreatedOn BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"'";
	}else{
		query=query+ " And cuInvoice.CreatedOn BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"'";
	}
	
}else if(batchInvoiceFromDate.length()>0 && !(batchInvoiceToDate.length()>0)){
	if(!query.contains("WHERE")){
		query=query+ " WHERE cuInvoice.CreatedOn >'"+invoicefromdate+"'";
	}else{
		query=query+ " And cuInvoice.CreatedOn >'"+invoicefromdate+"'";
	}
	
}else if(!(batchInvoiceFromDate.length()>0) && (batchInvoiceToDate.length()>0)){
	if(!query.contains("WHERE")){
		query=query+ " WHERE cuInvoice.CreatedOn <'"+invoicetodate+"'";
	}else{
		query=query+ " And cuInvoice.CreatedOn <'"+invoicetodate+"'";
	}
	
}*/


//From Date and ToDate Checking
/*if(rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0){
	if(!query.contains("WHERE")){
		query=query+ " WHERE (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))";
	}else{
		query=query+ " AND (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))";
				//+ "// OR (cuInvoice.cuInvoiceID  >= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  <=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')))";
	}
	
}else if(rangeInvoiceFrom.length()>0 && !(rangeInvoiceTo.length()>0)){
	if(!query.contains("WHERE")){
		query=query+ " WHERE (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"'))";
	}else{
		query=query+ " And (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"'))";
	}
	
}else if(!(rangeInvoiceFrom.length()>0) && (rangeInvoiceTo.length()>0)){
	if(!query.contains("WHERE")){
		query=query+ " WHERE (cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))";
	}else{
		query=query+ " AND (cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))";
	}
	
}*/


			
			if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0 && rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0){
				if(!query.contains("WHERE")){
					query=query+" WHERE Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' AND (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) GROUP BY cuInvoice.cuInvoiceID";
				}else{
					query=query+" AND Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' AND (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) GROUP BY cuInvoice.cuInvoiceID";
				}
				
			}else if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0){
				if(!query.contains("WHERE")){
				query=query+" WHERE  Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' GROUP BY cuInvoice.cuInvoiceID";
				}else{
				query=query+" And Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' GROUP BY cuInvoice.cuInvoiceID";
				}
			}
			else if(rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0){
				if(!query.contains("WHERE")){
				query=query+" WHERE  (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))  GROUP BY cuInvoice.cuInvoiceID";
				}else{
				query=query+" And (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))  GROUP BY cuInvoice.cuInvoiceID";
				}
			}
//			else if(batchInvoiceCuID.length()>0 &&batchInvoiceCuID!="0")
//				query=query+" WHERE rxMaster.rxMasterID = "+batchInvoiceCuID+" GROUP BY cuInvoice.cuInvoiceID";
			/*else if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0)
				query=query+" WHERE cuInvoice.CreatedOn BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' GROUP BY cuInvoice.cuInvoiceID";
			else if(rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0)
				query=query+" WHERE ((cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) OR (cuInvoice.cuInvoiceID  >= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  <=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))) GROUP BY cuInvoice.cuInvoiceID";
			*/
			else{
				if(!query.contains("WHERE")){
					query=query+" where Date(cuInvoice.CreatedOn) ='"+yesterDayDate+"' GROUP BY cuInvoice.cuInvoiceID";
				}else{
					query=query+" GROUP BY cuInvoice.cuInvoiceID";
				}
				
			}
			query=query+" ORDER BY rxMaster.Name ASC";	
				
			System.out.println("query::"+query);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>joReleaseId:</b>"+batchInvoiceCuID,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				objtsusersettings=null;
				if(imageStream!=null){
					imageStream.close();
				}
				if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@RequestMapping(value = "/printCuInvoiceReport", method = RequestMethod.GET)
	public @ResponseBody
	void printCuInvoiceReport(
			@RequestParam(value = "CuInvoice", required = false) Integer CuInvoice,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, MessagingException, VendorException {
		Connection connection =null;
		ConnectionProvider con =null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
				//			"/resources/jasper_reports/CuInvoiceReportFinal.jrxml");
							"/resources/jasper_reports/CuInvoiceReportFinalNew.jrxml");
			con = itspdfService.connectionForJasper();
			Cuinvoice aCuinvoice = new Cuinvoice();
			try {
				aCuinvoice = jobService.getSingleCuInvoiceObj(CuInvoice);
			} catch (JobException e2) {
				e2.printStackTrace();
			}
			String ShipToName="";
			String ShipToAddress1="";
			String ShipToAddress2="";
			String ShipToCity="";
			String ShipToState="";
			String ShipToZip="";
			
			
			String Tag="";
			List<Prwarehouse> aPrwarehouses = null;
			try {
				aPrwarehouses = jobService.getWareHouse();
			} catch (JobException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Rxaddress aRxAddressShipto = null;
			Rxaddress aRxAddressBillto = null;
			Jomaster aJomaster = null;
			
			if(aCuinvoice.getShipToMode()==0){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("warehouse", aCuinvoice.getPrToWarehouseId());
			}else if(aCuinvoice.getShipToMode()==1){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("customer", aCuinvoice.getRxShipToId());
			}else if(aCuinvoice.getShipToMode()==2){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("jobsite", aCuinvoice.getRxShipToId());
			}else if(aCuinvoice.getShipToMode()==3){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("other", aCuinvoice.getRxShipToAddressId());
			}
		
			String BillToName="";
			String BillToAddress1="";
			String BillToAddress2="";
			String BillToCity="";
			String BillToState="";
			String BillToZip="";
			
			try {
				aRxAddressBillto = jobService.getRxMasterBillAddress(aCuinvoice.getRxBillToId(), "bill");
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (aRxAddressBillto != null) {
				if (aRxAddressBillto.getName() != null &&!aRxAddressBillto.getName().equalsIgnoreCase("")) {
					BillToName = aRxAddressBillto.getName();
				}
				if (aRxAddressBillto.getAddress1() != null && !aRxAddressBillto.getAddress1().equalsIgnoreCase(
						"")) {
					BillToAddress1 =aRxAddressBillto.getAddress1();
				}
				if (aRxAddressBillto.getAddress2() !=null && !aRxAddressBillto.getAddress2().equalsIgnoreCase(
						"")) {
					BillToAddress2 = aRxAddressBillto.getAddress2();
				}
				if (aRxAddressBillto.getCity() != null && !aRxAddressBillto.getCity().equalsIgnoreCase("")) {
					BillToCity = aRxAddressBillto.getCity();
				}
				if (aRxAddressBillto.getState() != null && !aRxAddressBillto.getState().equalsIgnoreCase("")) {
					if (aRxAddressBillto.getCity().equalsIgnoreCase(
							"")) {
						BillToState =  aRxAddressBillto.getState();
					} else {
						BillToState =  aRxAddressBillto.getState();
					}
				}
				if (aRxAddressBillto.getZip() != null && !aRxAddressBillto.getZip().equalsIgnoreCase("")) {
					BillToZip =  aRxAddressBillto.getZip();
				}
				
			}
			params.put("billtoName", BillToName);
			params.put("billtoAddress1", BillToAddress1);
			params.put("billtoAddress2", BillToAddress2);
			params.put("billtoCity", BillToCity);
			params.put("billtoState", BillToState);
			params.put("billtoZip", BillToZip);
			params.put("CuInvoice", CuInvoice);
			
			if(aRxAddressShipto!=null)
			{
			params.put("shiptoName", aRxAddressShipto.getName());
			params.put("shiptoAddress1", aRxAddressShipto.getAddress1());
			params.put("shiptoAddress2", aRxAddressShipto.getAddress2());
			params.put("shiptoCity", aRxAddressShipto.getCity());
			params.put("shiptoState", aRxAddressShipto.getState());
			params.put("shiptoZip", aRxAddressShipto.getZip());
			}
			else
			{
				params.put("shiptoName", "");
				params.put("shiptoAddress1", "");
				params.put("shiptoAddress2", "");
				params.put("shiptoCity", "");
				params.put("shiptoState","");
				params.put("shiptoZip","");
			}
		
			ServletOutputStream out = theResponse.getOutputStream();
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			connection = con.getConnection();
			JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params,
					connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		} catch (JRException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "/saveCustomerInvoiceReport", method = RequestMethod.GET)
	public @ResponseBody
	void saveCustomerInvoiceReport(
			@RequestParam(value = "CuInvoice", required = false) Integer CuInvoice,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, JobException, MessagingException, VendorException {
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
							//"/resources/jasper_reports/CuInvoiceReportFinal.jrxml");
							"/resources/jasper_reports/CuInvoiceReportFinalNew.jrxml");
			
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			con = itspdfService.connectionForJasper();
			Cuinvoice aCuinvoice = new Cuinvoice();
			try {
				aCuinvoice = jobService.getSingleCuInvoiceObj(CuInvoice);
			} catch (JobException e2) {
				e2.printStackTrace();
			}
			String ShipToName="";
			String ShipToAddress1="";
			String ShipToAddress2="";
			String ShipToCity="";
			String ShipToState="";
			String ShipToZip="";
			String Tag="";
			List<Prwarehouse> aPrwarehouses = null;
			try {
				aPrwarehouses = jobService.getWareHouse();
			} catch (JobException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Rxaddress aRxAddressShipto = null;
			Rxaddress aRxAddressBillto = null;
			Jomaster aJomaster = null;
		
			if(aCuinvoice.getShipToMode()==0){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("warehouse", aCuinvoice.getPrToWarehouseId());
			}else if(aCuinvoice.getShipToMode()==1){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("customer", aCuinvoice.getRxShipToId());
			}else if(aCuinvoice.getShipToMode()==2){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("jobsite", aCuinvoice.getRxShipToId());
			}else if(aCuinvoice.getShipToMode()==3){
				aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("other", aCuinvoice.getRxShipToAddressId());
			}
			
			String BillToName="";
			String BillToAddress1="";
			String BillToAddress2="";
			String BillToCity="";
			String BillToState="";
			String BillToZip="";
			
			try {
				aRxAddressBillto = jobService.getRxMasterBillAddress(aCuinvoice.getRxBillToId(), "bill");
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (aRxAddressBillto != null) {
				if (aRxAddressBillto.getName() != null &&!aRxAddressBillto.getName().equalsIgnoreCase("")) {
					BillToName = aRxAddressBillto.getName();
				}
				if (aRxAddressBillto.getAddress1() != null && !aRxAddressBillto.getAddress1().equalsIgnoreCase(
						"")) {
					BillToAddress1 =aRxAddressBillto.getAddress1();
				}
				if (aRxAddressBillto.getAddress2() !=null && !aRxAddressBillto.getAddress2().equalsIgnoreCase(
						"")) {
					BillToAddress2 = aRxAddressBillto.getAddress2();
				}
				if (aRxAddressBillto.getCity() != null && !aRxAddressBillto.getCity().equalsIgnoreCase("")) {
					BillToCity = aRxAddressBillto.getCity();
				}
				if (aRxAddressBillto.getState() != null && !aRxAddressBillto.getState().equalsIgnoreCase("")) {
					if (aRxAddressBillto.getCity().equalsIgnoreCase(
							"")) {
						BillToState =  aRxAddressBillto.getState();
					} else {
						BillToState =  aRxAddressBillto.getState();
					}
				}
				if (aRxAddressBillto.getZip() != null && !aRxAddressBillto.getZip().equalsIgnoreCase("")) {
					BillToZip =  aRxAddressBillto.getZip();
				}
				
			}
			params.put("billtoName", BillToName);
			params.put("billtoAddress1", BillToAddress1);
			params.put("billtoAddress2", BillToAddress2);
			params.put("billtoCity", BillToCity);
			params.put("billtoState", BillToState);
			params.put("billtoZip", BillToZip);
			params.put("CuInvoice", CuInvoice);
			if(aRxAddressShipto!=null)
			{
			params.put("shiptoName", aRxAddressShipto.getName());
			params.put("shiptoAddress1", aRxAddressShipto.getAddress1());
			params.put("shiptoAddress2", aRxAddressShipto.getAddress2());
			params.put("shiptoCity", aRxAddressShipto.getCity());
			params.put("shiptoState", aRxAddressShipto.getState());
			params.put("shiptoZip", aRxAddressShipto.getZip());
			}
			else
			{
				params.put("shiptoName", "");
				params.put("shiptoAddress1", "");
				params.put("shiptoAddress2", "");
				params.put("shiptoCity", "");
				params.put("shiptoState","");
				params.put("shiptoZip","");
			}
		
			boolean donotmail=false;
			if(aCuinvoice.getDoNotMail()==1){
				donotmail=true;
			}
			params.put("donotMail", donotmail);
			
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			connection = con.getConnection();
			JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params,
					connection);
			/*File pdf = new File("/var/quotePDF/customerInvoice.pdf");
			JasperExportManager.exportReportToPdfStream(print,
					new FileOutputStream(pdf));
					*/
			ReportService.dynamicWriteReportCall(theRequest,theResponse,params,"pdf",jd,"CustomerInvoice_"+aCuinvoice.getInvoiceNumber()+".pdf",connection);		
			
		}catch (JRException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//updateSalesOrderStatus cusoID/status
	@RequestMapping(value = "/setSalesOrderStatus", method = RequestMethod.POST)
	public @ResponseBody
	Integer getCuSOID(
			@RequestParam(value = "cusoID", required = false) Integer cuSoId,
			@RequestParam(value = "status", required = false) Integer status,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		Integer joMasterID=salesServices.updateSalesOrderStatus(cuSoId, status,((UserBean)session.getAttribute(SessionConstants.USER)).getUserId(),((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());
			return joMasterID;
		
	}
	
	@RequestMapping(value = "/printWarehouseTransferReport", method = RequestMethod.GET)
	public @ResponseBody
	void printWarehouseTransferReport(
			@RequestParam(value = "prTransferID", required = false) Integer prTransferID,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, MessagingException {
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			System.out.println("prTransferID-----"+prTransferID);
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/warehouseTransferReport.jrxml");
			con = itspdfService.connectionForJasper();
			params.put("prTransferID", prTransferID);
			ServletOutputStream out = theResponse.getOutputStream();
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			connection = con.getConnection();
			JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params,
					connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>prTransferID:</b>"+prTransferID,"SalesOrderController",e,session,theRequest);
		} catch (JRException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>prTransferID:</b>"+prTransferID,"SalesOrderController",e,session,theRequest);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>prTransferID:</b>"+prTransferID,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "/updateSOEmailTimeStampValue", method = RequestMethod.GET)
	public @ResponseBody Cuso updateEmailStampValue(
			@RequestParam(value = "cuSOID", required = false) Integer theCuSOId,
			@RequestParam(value = "mailSentDate", required = false) String theSoMailDate,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Cuso aCuso = new Cuso();
		try {
			aCuso.setEmailTimeStamp(DateUtils.parseDate(theSoMailDate,
					new String[] { "MM/dd/yyyy hh:mm a" }));
			aCuso.setCuSoid(theCuSOId);
			salesServices.updateEmailStampValue(aCuso);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>theCuSOId:</b>"+theCuSOId,"SalesOrderController",e,session,theRequest);
		}
		return aCuso;
	}
	
	
	
	@RequestMapping(value = "/loadEmpCategories", method = RequestMethod.GET)
	public @ResponseBody Sysassignment loadEmpCategories(HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Sysassignment aSysassignment = new Sysassignment();
		try {
			aSysassignment=sysservice.getCustomerCategories();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//sendTransactionException("<b>theCuSOId:</b>"+theCuSOId,"SalesOrderController",e,session,theRequest);
		}
		return aSysassignment;
	}
	
/*
 * Created by: Praveenkumar
 * Date : 2014-09-15
 * Purpose: Saving Sales Order Line item note
 * JS: so_lines.js
 * 
 */
	
	@RequestMapping(value = "/saveLineItemNote", method = RequestMethod.POST)
	public @ResponseBody Boolean saveLineItemNote(@RequestParam(value="lineItem[]", required= false) ArrayList<?> thePOLineItemDetails,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Boolean isSaved = false;
		
		String Notes = null;
		Integer cuSoDetailID = 0;
		try {
			if(thePOLineItemDetails.get(0) != "" && thePOLineItemDetails.get(0) != null){
				String aInLine = thePOLineItemDetails.get(0).toString();
				String aInLineReplace = aInLine.replaceAll("'", "&And");
				Notes = aInLineReplace;
				logger.info("Notes in Error::"+Notes);
			}
			if(thePOLineItemDetails.get(1) != "" && thePOLineItemDetails.get(1) != null){
				String aVePODetailID = thePOLineItemDetails.get(1).toString();
				cuSoDetailID = (Integer.valueOf(aVePODetailID));
			}
			
			isSaved = jobService.saveSalesOrderNote(cuSoDetailID,Notes);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>cuSODetailID:</b>"+cuSoDetailID,"SalesOrderController",e,session,theRequest);
		}

		return isSaved;
	}
	
	@RequestMapping(value = "/saveInvoiceLineItemNote", method = RequestMethod.POST)
	public @ResponseBody Boolean saveInvoiceLineItemNote(
			@RequestParam(value = "cuInvoiceDetailId", required = false) Integer cuInvoiceDetailId,
			@RequestParam(value = "note", required = false) String notes,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Boolean isSaved = false;
		try {
			isSaved = jobService.saveCustomerInvoiceNote(cuInvoiceDetailId, notes);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>cuInvoiceDetailId:</b>"+cuInvoiceDetailId,"SalesOrderController",e,session,theRequest);
		}

		return isSaved;
	}
	
	/*
	 * Created by: Praveenkumar
	 * Date : 2014-09-15
	 * Purpose: Saving Sales Order Line item note
	 * JS: so_lines.js
	 * 
	 */
	/*@RequestMapping(value = "/copyTemplateLineItems", method = RequestMethod.POST)
	public @ResponseBody Boolean copyTemplateLineItems(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			@RequestParam(value = "quantity", required = false) Integer quantity,
			@RequestParam(value = "cuSOTemplateID", required = false) Integer cuSOTemplateID,
			
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Boolean isSaved = false;
		try {
			isSaved = jobService.copySOTemplateLineItems(cuSOID,quantity,cuSOTemplateID);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}

		return isSaved;
	}*/
	/*	
	 * Created by: Praveenkumar
	 * Date : 2014-09-15
	 * Purpose: show Sales Order line item cost
	 * JS: so_lines.js
	 * 
	 */
	@RequestMapping(value="/getSOPriceDetails", method = RequestMethod.POST)
	public@ResponseBody
	Map<String, BigDecimal> getPriceDetails(@RequestParam(value="cusoid", required= false) Integer cuSOid,
			@RequestParam(value="prMasterId", required= false) Integer prMasterID,
			@RequestParam(value="cuSODetailID", required= false) Integer cuSODetailID,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws JobException, IOException, VendorException, MessagingException {
		Map<String,BigDecimal> aPrice = new HashMap<String, BigDecimal>();
		try {		
			logger.info("Warehouse Detail Calculation Called! ");
			aPrice = salesServices.getTemplatePriceDetails(cuSOid,cuSODetailID,prMasterID);
		} catch (Exception e) {
			
			theResponse.sendError(500, e.getCause().getMessage());
			sendTransactionException("<b>cuSOid:</b>"+cuSOid,"SalesOrderController",e,session,theRequest);
		}
		
		return aPrice;		
	}
	
	@RequestMapping(value="/getInventoryAllocatedDetails", method = RequestMethod.POST)
	public@ResponseBody
	Prmaster getInventoryAllocatedDetails(@RequestParam(value="cusoid", required= false) Integer cuSOid,
			@RequestParam(value="prmasterid", required= false) Integer prmasterid,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws JobException, IOException, VendorException, MessagingException {
		Prmaster objprmaster=new Prmaster();
		try {		
			objprmaster = salesServices.getInventoryAllocatedDetailsservice(cuSOid,prmasterid);
		} catch (Exception e) {
			theResponse.sendError(500, e.getCause().getMessage());
			sendTransactionException("<b>cuSOid:</b>"+cuSOid,"SalesOrderController",e,session,theRequest);
		}
		
		return objprmaster;		
	}
	
	
	
	@RequestMapping(value = "/printinsidejobCuInvoiceReport", method = RequestMethod.GET)
	public @ResponseBody
	void printinsidejobCuInvoiceReport(
			@RequestParam(value = "CuInvoice", required = false) Integer CuInvoice,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, MessagingException, VendorException, JRException {
		Connection connection =null;
		JasperDesign jd  = null;
		ConnectionProvider con = null;
		try {
			List<String> addlist=new ArrayList<String>();
			addlist.add("IncludeListcolumnoninvoices");
			addlist.add("IncludeExtListcolumnoninvoices");
			addlist.add("IncludeMultcolumnoninvoices");
			ArrayList<Sysvariable> sysvariablelist=new ArrayList<Sysvariable>();
						
			try {
				sysvariablelist = userService.getInventorySettingsDetails(addlist);
			} catch (UserException e) {
				e.printStackTrace();
			}
			
			/*int i=0;
			boolean listColumn=false;
			boolean ExtListColumn=false;
			boolean MultColumn=false;
			
			for (Sysvariable aSysvariable : sysvariablelist) {
				if (aSysvariable.getValueLong() != null) {
					if (aSysvariable.getValueLong() == 1) {
						if (i == 0) {
							listColumn = true;
						} else if (i == 1) {
							ExtListColumn = true;
						} else if (i == 2) {
							MultColumn=true;
						}
						// break;
					}
				}
				i = i + 1;
			}
			*/
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/CuInvoiceReportFinalNew.jrxml");
			
			jd = JRXmlLoader.load(path_JRXML);
			con = itspdfService.connectionForJasper();
			Cuinvoice aCuinvoice = new Cuinvoice();
			try {
				aCuinvoice = jobService.getSingleCuInvoiceObj(CuInvoice);
			} catch (JobException e2) {
				e2.printStackTrace();
			}
			String ShipToName="";
			String ShipToAddress1="";
			String ShipToAddress2="";
			String ShipToCity="";
			String ShipToState="";
			String ShipToZip="";
			String Tag="";
			List<Prwarehouse> aPrwarehouses = null;
			try {
				aPrwarehouses = jobService.getWareHouse();
			} catch (JobException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Rxaddress aRxAddressShipto = null;
			Rxaddress aRxAddressBillto = null;
			Jomaster aJomaster = null;
			
				if(aCuinvoice.getShipToMode()==0){
					aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("warehouse", aCuinvoice.getPrToWarehouseId());
				}else if(aCuinvoice.getShipToMode()==1){
					aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("customer", aCuinvoice.getRxShipToId());
				}else if(aCuinvoice.getShipToMode()==2){
					aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("jobsite", aCuinvoice.getRxShipToId());
				}else if(aCuinvoice.getShipToMode()==3){
					aRxAddressShipto = itsPurchaseService.getSelectedShiptoaddress("other", aCuinvoice.getRxShipToAddressId());
				}
			
			
			String BillToName="";
			String BillToAddress1="";
			String BillToAddress2="";
			String BillToCity="";
			String BillToState="";
			String BillToZip="";
			
			try {
				aRxAddressBillto = jobService.getRxMasterBillAddress(aCuinvoice.getRxBillToId(), "bill");
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (aRxAddressBillto != null) {
				if (aRxAddressBillto.getName() != null &&!aRxAddressBillto.getName().equalsIgnoreCase("")) {
					BillToName = aRxAddressBillto.getName();
				}
				if (aRxAddressBillto.getAddress1() != null && !aRxAddressBillto.getAddress1().equalsIgnoreCase(
						"")) {
					BillToAddress1 =aRxAddressBillto.getAddress1();
				}
				if (aRxAddressBillto.getAddress2() !=null && !aRxAddressBillto.getAddress2().equalsIgnoreCase(
						"")) {
					BillToAddress2 = aRxAddressBillto.getAddress2();
				}
				if (aRxAddressBillto.getCity() != null && !aRxAddressBillto.getCity().equalsIgnoreCase("")) {
					BillToCity = aRxAddressBillto.getCity();
				}
				if (aRxAddressBillto.getState() != null && !aRxAddressBillto.getState().equalsIgnoreCase("")) {
					if (aRxAddressBillto.getCity().equalsIgnoreCase(
							"")) {
						BillToState =  aRxAddressBillto.getState();
					} else {
						BillToState =  aRxAddressBillto.getState();
					}
				}
				if (aRxAddressBillto.getZip() != null && !aRxAddressBillto.getZip().equalsIgnoreCase("")) {
					BillToZip =  aRxAddressBillto.getZip();
				}
				
			}
			/*params.put("incListCol", listColumn);
			params.put("incExtListCol", ExtListColumn);
			params.put("incMultCol", MultColumn);*/
			params.put("incListCol", true);
			params.put("incExtListCol", true);
			params.put("incMultCol", true);
			params.put("billtoName", BillToName);
			params.put("billtoAddress1", BillToAddress1);
			params.put("billtoAddress2", BillToAddress2);
			params.put("billtoCity", BillToCity);
			params.put("billtoState", BillToState);
			params.put("billtoZip", BillToZip);
			params.put("CuInvoice", CuInvoice);
			if(aRxAddressShipto!=null)
			{
			params.put("shiptoName", aRxAddressShipto.getName());
			params.put("shiptoAddress1", aRxAddressShipto.getAddress1());
			params.put("shiptoAddress2", aRxAddressShipto.getAddress2());
			params.put("shiptoCity", aRxAddressShipto.getCity());
			params.put("shiptoState", aRxAddressShipto.getState());
			params.put("shiptoZip", aRxAddressShipto.getZip());
			}
			else
			{
				params.put("shiptoName", "");
				params.put("shiptoAddress1", "");
				params.put("shiptoAddress2", "");
				params.put("shiptoCity", "");
				params.put("shiptoState","");
				params.put("shiptoZip","");
			}
		
			boolean donotmail=false;
			if(aCuinvoice.getDoNotMail()==1){
				donotmail=true;
			}
			params.put("donotMail", donotmail);
			connection = con.getConnection();
			
			//Header 
			JRBand bandH =   jd.getColumnHeader();
			JRElement[] eleH=bandH.getElements();
			
			/*int xx=142;
			int Hdescriptionwidth=140;
			if(!listColumn){
				Hdescriptionwidth=Hdescriptionwidth+57;
			}
			if(!ExtListColumn){
				Hdescriptionwidth=Hdescriptionwidth+69;
			}
			if(!MultColumn){
				Hdescriptionwidth=Hdescriptionwidth+76;
			}
			
			eleH[2].setWidth(Hdescriptionwidth);
			xx=xx+Hdescriptionwidth;
			if(listColumn){
				eleH[3].setX(xx);
				xx=xx+57;
			}
			xx=xx+4;
			if(ExtListColumn){
				eleH[4].setX(xx);
				xx=xx+69;
			}
			xx=xx+3;
			if(MultColumn){
				eleH[5].setX(xx);
				xx=xx+76;
			}*/
			
			
			
			
			//Detail Band
			JRBand[] banda =  (JRBand[]) jd.getAllBands();
			JRElement[] ele=banda[3].getElements();
			
			/*int x=142;
			int descriptionwidth=140;
			if(!listColumn){
				descriptionwidth=descriptionwidth+57;
			}
			if(!ExtListColumn){
				descriptionwidth=descriptionwidth+69;
			}
			if(!MultColumn){
				descriptionwidth=descriptionwidth+76;
			}
			
			ele[2].setWidth(descriptionwidth);
			eleH[2].setWidth(descriptionwidth);
			x=x+descriptionwidth;
			if(listColumn){
				//Detail Band
				ele[3].setX(x);
				
				//Header Band
						eleH[3].setX(x); //Header
						eleH[4].setX(x+32); //Line
				x=x+57;
			}
			x=x+4;
			if(ExtListColumn){
				//Detail Band
				ele[4].setX(x);
				
				//Header Band
				eleH[5].setX(x); //Header
				eleH[6].setX(x+20); //Line
				x=x+69;
			}
			x=x+3;
			if(MultColumn){
				//Detail Band
				ele[5].setX(x);
				
				//Header Band
				eleH[7].setX(x); //Header
				eleH[8].setX(x+45); //Line
				x=x+76;
			}
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,"CuInvoice.pdf",connection);*/
			
			ServletOutputStream out = theResponse.getOutputStream();
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			connection = con.getConnection();
			JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params,
					connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>CuInvoice:</b>"+CuInvoice,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "/setBillOnlyStatus", method = RequestMethod.POST)
	public @ResponseBody Boolean getsetBillOnlyStatus(
			@RequestParam(value = "joReleaseID", required = false) Integer joReleaseID,
			@RequestParam(value = "status", required = false) Integer status,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		salesServices.updatesetBillOnlyStatus(joReleaseID, status);
			return true;
		
	}
	
	
	/*@RequestMapping(value = "/applySuggestedOrder", method = RequestMethod.POST)
	public @ResponseBody Boolean applySuggestedOrder(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		
	     	salesServices.updateSuggestedOrderIncuSoDetails(cuSOID);
	     	
			return true;
		
	}*/
	
	@RequestMapping(value = "/applyQuotedPrice", method = RequestMethod.POST)
	public @ResponseBody Boolean applyQuotedPrice(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		
	     	salesServices.updateQuotedPriceIncuSoDetails(cuSOID);
	     	
			return true;
		
	}
	
	@RequestMapping(value = "/preloadcustomerinvoicenumberforBillOnly", method = RequestMethod.POST)
	public @ResponseBody String preloadcustomerinvoicenumberforBillOnly(
			@RequestParam(value = "invoicenumberwithoutprefix", required = false) String invoicenumberwithoutprefix,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		
		Integer countofinvoiceforrelease=jobService.getnumberofInvoiceNumber(invoicenumberwithoutprefix);
		countofinvoiceforrelease=countofinvoiceforrelease+1;
		String newCustomerInvoiceNumber=invoicenumberwithoutprefix+countofinvoiceforrelease;
		
		String newInvoiceNumber = "";
		Integer requireInvoiceNumberOrNot = 0;
		List<String> addlist=new ArrayList<String>();
		addlist.add("RequireNewNumbersForCuInvoices");
		ArrayList<Sysvariable> sysvariablelist;
		try {
			sysvariablelist = userService.getInventorySettingsDetails(addlist);
			requireInvoiceNumberOrNot = sysvariablelist.get(0).getValueLong()==null?0:sysvariablelist.get(0).getValueLong();
			newInvoiceNumber = sysvariablelist.get(0).getValueString()==null?"":sysvariablelist.get(0).getValueString();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return requireInvoiceNumberOrNot>0?newInvoiceNumber:newCustomerInvoiceNumber;
	}
	
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
			userService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	
	@RequestMapping(value = "/saveSoTempLineItemNote", method = RequestMethod.POST)
	public @ResponseBody Boolean saveSoTempLineItemNote(@RequestParam(value="lineItem[]", required= false) ArrayList<?> thePOLineItemDetails,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		Boolean isSaved = false;
		
		String Notes = null;
		Integer cuSoDetailID = 0;
		try {
			if(thePOLineItemDetails.get(0) != "" && thePOLineItemDetails.get(0) != null){
				String aInLine = thePOLineItemDetails.get(0).toString();
				String aInLineReplace = aInLine.replaceAll("'", "&And");
				Notes = aInLineReplace;
				logger.info("Notes in Error::"+Notes);
			}
			if(thePOLineItemDetails.get(1) != "" && thePOLineItemDetails.get(1) != null){
				String aVePODetailID = thePOLineItemDetails.get(1).toString();
				cuSoDetailID = (Integer.valueOf(aVePODetailID));
			}
			
			isSaved = jobService.saveSalesOrderTempNote(cuSoDetailID,Notes);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>cuSOTempDetailID:</b>"+cuSoDetailID,"SalesOrderController",e,session,theRequest);
		}

		return isSaved;
	}
	
	
	@RequestMapping(value = "/copySoTempLineItemNote", method = RequestMethod.POST)
	public @ResponseBody Integer copySoTempLineItemNote(@RequestParam(value = "cuSoId", required = false) Integer cuSoId,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
         int newId;
		
         newId = jobService.copySoTemplate(cuSoId);
		
		System.out.println("cuSoTemplateId"+cuSoId);
		
		

		return newId;
	}
	
	
	
	
	@RequestMapping(value = "/getproductWareHouseCost", method = RequestMethod.POST)
	public @ResponseBody
	BigDecimal getproductWareHouseCost(
			@RequestParam(value = "prMasterID", required = false) Integer prMasterID,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		BigDecimal WareHseCost=itsInventoryService.getWarehouseCost(prMasterID);
		return WareHseCost;
	}
	
	
	/**
	 * Description: This method is used for inserting new Sales Order line
	 * items.
	 * 
	 * @param: {@link Cusodetail} attributes of Cusodetail Object.
	 * @return: saved {@link Boolean}
	 * @throws IOException
	 * @throws MessagingException 
	 * @exception: JobException
	 * */

	/**
	 * Description: This method returns a Customer Invoice Object
	 * {@link Cuinvoice} and all the line items of the corresponding customer
	 * invoice{@link Cuinvoicedetail}.
	 * 
	 * @param: rxMasterId {@link String}, cuInvoiceID {@link Integer}
	 * @return: custom response {@link CustomResponse}
	 * @throws MessagingException 
	 * @exception: JobException
	 * @throws: IOException
	 *          {@link SalesOrderController#computeTaxTotal(BigDecimal, BigDecimal, BigDecimal, byte, Double, BigDecimal)}
	 * */
	@RequestMapping(value = "/saveSOReleaseLineItem", method = RequestMethod.POST)
	public @ResponseBody String saveSOReleaseLineItem(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
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
			@RequestParam(value = "Total", required = false) BigDecimal Total,
			@RequestParam(value = "taxValue", required = false) BigDecimal taxValue,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "DelSOData[]",required = false) ArrayList<String>  delData,
			@RequestParam(value = "withPriceLineStatus", required = false) boolean withPriceLineStatus,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		JsonParser parser = new JsonParser();
		UserBean aUserBean=null;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		if (delData!=null && delData.size()>0) {
			for(String detailID:delData){
				Integer cusoDetailID=JobUtil.ConvertintoInteger(detailID);
				Cusodetail aCusodetail=new Cusodetail();
				aCusodetail.setUserID(aUserBean.getUserId());
				aCusodetail.setUserName(aUserBean.getUserName());
				aCusodetail.setCuSodetailId(cusoDetailID);
				aCusodetail.setCuSoid(cuSOID);
				jobService.addSalesORderReleaseLineItem(aCusodetail,"delete");
			}
		}
		if ( gridData!=null) {

			System.out.println("gridData"+gridData);
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			List<JsonElement> list=new ArrayList<JsonElement>();
			int len = array.size();
			if (array != null) { 
			   for (int i=0;i<len;i++){ 
			    list.add(array.get(i));
			   } 
			}
			//Remove the element from arraylist
			list.remove(list.size()-1);
			System.out.println("Before array length==>"+array.size());
			array=new JsonArray();
			for(int i=0;i<list.size();i++){
				array.add(list.get(i));
			}
			BigDecimal whcostTotalAmount=BigDecimal.ZERO;
			int i=1;
			for (JsonElement ele1 : array) {
				boolean saved = false;
				Cusodetail aCusodetail=new Cusodetail();
				JsonObject obj = ele1.getAsJsonObject();
				String desc=obj.get("description").getAsString();
				String itemCode=obj.get("itemCode").getAsString();
				BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
				String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
				unitCost_String=unitCost_String.replaceAll(",", "");
				String priceMultiplier_String=obj.get("priceMultiplier").getAsString();
				BigDecimal priceMultiplier=JobUtil.ConvertintoBigDecimal(priceMultiplier_String);
				String taxable=obj.get("priceMultiplier").getAsString();
				String note=obj.get("note").getAsString();
				Integer cuSodetailId=null;
				if(obj.get("cuSodetailId")!=null && obj.get("cuSodetailId").getAsString()!=""&& obj.get("cuSodetailId").getAsString().length()>0){
					cuSodetailId=obj.get("cuSodetailId").getAsInt();
				}
				Integer prMasterID=obj.get("prMasterId").getAsInt();
				BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
				String Taxable=obj.get("taxable").getAsString();
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("yes")) {
						aCusodetail.setTaxable((byte) 1);
					} else {
						aCusodetail.setTaxable((byte) 0);
					}
				}
				
				String amount_String=obj.get("amount").getAsString().replaceAll("\\$", "");
				amount_String=amount_String.replaceAll(",", "");
				BigDecimal amount=JobUtil.ConvertintoBigDecimal(amount_String);
				String Oper="add";
				if(cuSodetailId!=null){
					Oper="edit";
				}
				BigDecimal whseCost=obj.get("whseCost").getAsBigDecimal();
				BigDecimal overAllwhseProduct=JobUtil.floorFigureoverall(whseCost.multiply(quantityOrder),2);
				whcostTotalAmount=whcostTotalAmount.add(overAllwhseProduct);
				
				aCusodetail.setCuSoid(cuSOID);
				aCusodetail.setDescription(desc);
				aCusodetail.setPrMasterId(prMasterID);
				aCusodetail.setPriceMultiplier(priceMultiplier);
				aCusodetail.setQuantityOrdered(JobUtil.floorFigureoverall(quantityOrder,2));
				aCusodetail.setUnitCost(JobUtil.floorFigureoverall(unitCost,2));
				aCusodetail.setNote(note);
				aCusodetail.setWhseCost(whseCost);
				aCusodetail.setCuSodetailId(cuSodetailId);
				aCusodetail.setPosition(i);
				aCusodetail.setUserID(aUserBean.getUserId());
				aCusodetail.setUserName(aUserBean.getUserName());
				if(Oper=="add"){
					aCusodetail.setQuantityBilled(new BigDecimal(0.00));
				}
				System.out.println("cuSodetailId===>>"+cuSodetailId);
				saved = jobService.addSalesORderReleaseLineItem(aCusodetail,Oper);
				i=i+1;
			}
			Cuso acuso=new Cuso();
			acuso.setCuSoid(cuSOID);
			acuso.setSubTotal(subTotal);
			acuso.setCostTotal(whcostTotalAmount);
			acuso.setTaxTotal(taxValue);
			acuso.setFreight(freight);
			acuso.setWhseCostTotal(whcostTotalAmount);
			acuso.setWithpriceStatus(withPriceLineStatus);
			jobService.updateCuso(acuso);

			if(acuso!=null && acuso.getCuSoid()>0){
				jobService.updateTaxableandNonTaxableforCuSO(acuso);
				}
			
		}
		
		return null;
	}
	
	@RequestMapping(value = "/copySOTemplate", method = RequestMethod.POST)
	public @ResponseBody CustomResponse copySOTemplate(
			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "cuSOTemplateID", required = false) Integer cuSOTemplateID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		UserBean aUserBean=null;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			ArrayList<Cusodetail> overAllcuSODetailLst=new ArrayList<Cusodetail>();
			JsonParser parser = new JsonParser();
			if ( gridData!=null) {

				System.out.println("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				List<JsonElement> list=new ArrayList<JsonElement>();
				int len = array.size();
				if (array != null) { 
				   for (int i=0;i<len;i++){ 
				    list.add(array.get(i));
				   } 
				}
				//Remove the element from arraylist
				list.remove(list.size()-1);
				System.out.println("Before array length==>"+array.size());
				array=new JsonArray();
				for(int i=0;i<list.size();i++){
					array.add(list.get(i));
				}
				System.out.println("After array length==>"+array.size());
				BigDecimal whcostTotalAmount=BigDecimal.ZERO;
				for (JsonElement ele1 : array) {
					boolean saved = false;
					Cusodetail aCusodetail=new Cusodetail();
					JsonObject obj = ele1.getAsJsonObject();
					String desc=obj.get("description").getAsString();
					String itemCode=obj.get("itemCode").getAsString();
					BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
					String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
					unitCost_String=unitCost_String.replaceAll(",", "");
					String priceMultiplier_String=obj.get("priceMultiplier").getAsString();
					BigDecimal priceMultiplier=JobUtil.ConvertintoBigDecimal(priceMultiplier_String);
					String taxable=obj.get("priceMultiplier").getAsString();
					String note=obj.get("note").getAsString();
					Integer cuSodetailId=null;
					if(obj.get("cuSodetailId")!=null && obj.get("cuSodetailId").getAsString()!=""&& obj.get("cuSodetailId").getAsString().length()>0){
						cuSodetailId=obj.get("cuSodetailId").getAsInt();
					}
					Integer prMasterID=obj.get("prMasterId").getAsInt();
					BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
					String Taxable=obj.get("taxable").getAsString();
					if (Taxable != null) {
						if (Taxable.equalsIgnoreCase("yes")) {
							aCusodetail.setTaxable((byte) 1);
						} else {
							aCusodetail.setTaxable((byte) 0);
						}
					}
					
					String amount_String=obj.get("amount").getAsString().replaceAll("\\$", "");
					amount_String=amount_String.replaceAll(",", "");
					BigDecimal amount=JobUtil.ConvertintoBigDecimal(amount_String);
					BigDecimal whseCost=obj.get("whseCost").getAsBigDecimal();
					aCusodetail.setCuSoid(cuSOID);
					aCusodetail.setDescription(desc);
					aCusodetail.setPrMasterId(prMasterID);
					aCusodetail.setPriceMultiplier(priceMultiplier);
					aCusodetail.setQuantityOrdered(JobUtil.floorFigureoverall(quantityOrder,2));
					aCusodetail.setUnitCost(JobUtil.floorFigureoverall(unitCost,2));
					aCusodetail.setNote(note);
					aCusodetail.setWhseCost(whseCost);
					aCusodetail.setItemCode(itemCode);
					aCusodetail.setCuSodetailId(cuSodetailId);
					aCusodetail.setUserID(aUserBean.getUserId());
					aCusodetail.setUserName(aUserBean.getUserName());
					overAllcuSODetailLst.add(aCusodetail);
				}
			}
			ArrayList<Cusodetail> cusodetaillst= jobService.copySOTemplateLineItems(cuSOID,cuSOTemplateID);
			for(Cusodetail aCusodetail:cusodetaillst){
				overAllcuSODetailLst.add(aCusodetail);
			}
			aResponse.setRows(overAllcuSODetailLst);
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}

		return aResponse;
	}
	
	@RequestMapping(value = "/ApplySuggestedPrice", method = RequestMethod.POST)
	public @ResponseBody CustomResponse ApplySuggestedPrice(
			@RequestParam(value = "QuotedPricePrMasterID", required = false) Integer QuotedPricePrMasterID,
			@RequestParam(value = "gridData",required = false) String gridData,
			@RequestParam(value = "SubTotalPrice", required = false) BigDecimal SubTotalPrice,
			@RequestParam(value = "QuoteOrSuggestedPrice", required = false) String QuoteOrSuggestedPrice,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		UserBean aUserBean=null;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			BigDecimal unitCost = new BigDecimal("0.0000");
			Boolean QPAlreadythereornot=true;
			ArrayList<Cusodetail> overAllcuSODetailLst=new ArrayList<Cusodetail>();
			JsonParser parser = new JsonParser();
			boolean flag = false;
			boolean testFlag = false;
			if ( gridData!=null) {

				System.out.println("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				List<JsonElement> list=new ArrayList<JsonElement>();
				int len = array.size();
				if (array != null) { 
				   for (int i=0;i<len;i++){ 
				    list.add(array.get(i));
				   } 
				}
				//Remove the element from arraylist
				list.remove(list.size()-1);
				System.out.println("Before array length==>"+array.size());
				array=new JsonArray();
				for(int i=0;i<list.size();i++){
					array.add(list.get(i));
				}
				System.out.println("After array length==>"+array.size());
				BigDecimal whcostTotalAmount=BigDecimal.ZERO;
				for (JsonElement ele1 : array) {
					boolean saved = false;
					Cusodetail aCusodetail=new Cusodetail();
					JsonObject obj = ele1.getAsJsonObject();
					String desc=obj.get("description").getAsString();
					String itemCode=obj.get("itemCode").getAsString();
					BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
					String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
					unitCost_String=unitCost_String.replaceAll(",", "");
					String priceMultiplier_String=obj.get("priceMultiplier").getAsString();
					BigDecimal priceMultiplier=JobUtil.ConvertintoBigDecimal(priceMultiplier_String);
					String taxable=obj.get("priceMultiplier").getAsString();
					String note=obj.get("note").getAsString();
					Integer cuSodetailId=null;
					if(obj.get("cuSodetailId")!=null && obj.get("cuSodetailId").getAsString()!=""&& obj.get("cuSodetailId").getAsString().length()>0){
						cuSodetailId=obj.get("cuSodetailId").getAsInt();
					}
					Integer prMasterID=obj.get("prMasterId").getAsInt();
					unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
					String Taxable=obj.get("taxable").getAsString();
					if (Taxable != null) {
						if (Taxable.equalsIgnoreCase("yes")) {
							aCusodetail.setTaxable((byte) 1);
						} else {
							aCusodetail.setTaxable((byte) 0);
						}
					}
					Integer cuSOID=null;
					if(obj.get("cuSOID")!=null && obj.get("cuSOID").getAsString()!=""&& obj.get("cuSOID").getAsString().length()>0){
						cuSOID=obj.get("cuSOID").getAsInt();
					}
					String amount_String=obj.get("amount").getAsString().replaceAll("\\$", "");
					amount_String=amount_String.replaceAll(",", "");
					BigDecimal amount=JobUtil.ConvertintoBigDecimal(amount_String);
					BigDecimal whseCost=obj.get("whseCost").getAsBigDecimal();
					aCusodetail.setCuSoid(cuSOID);
					aCusodetail.setDescription(desc);
					aCusodetail.setPrMasterId(prMasterID);
					aCusodetail.setPriceMultiplier(priceMultiplier);
					aCusodetail.setQuantityOrdered(JobUtil.floorFigureoverall(quantityOrder,2));
					aCusodetail.setUnitCost(BigDecimal.ZERO);
					aCusodetail.setNote(note);
					aCusodetail.setWhseCost(whseCost);
					aCusodetail.setItemCode(itemCode);
					aCusodetail.setCuSodetailId(cuSodetailId);
					aCusodetail.setUserID(aUserBean.getUserId());
					aCusodetail.setUserName(aUserBean.getUserName());
					if(unitCost.compareTo(new BigDecimal("0.0000"))==0){
						if(!testFlag && QuotedPricePrMasterID!=prMasterID){
							flag = true;
							testFlag = true;
						}
					} 
					if(QuotedPricePrMasterID==prMasterID){
						QPAlreadythereornot=false;
						if(QuoteOrSuggestedPrice.equals("QuotedPrice")){
							if(flag){
								aCusodetail.setUnitCost(new BigDecimal("0.0000"));
							}else{
							aCusodetail.setUnitCost(unitCost);
							}
							}else{
							aCusodetail.setUnitCost(SubTotalPrice);
						}
						
					}
					overAllcuSODetailLst.add(aCusodetail);
				}
				if(QPAlreadythereornot){
					Cusodetail aCusodetail=new Cusodetail();
					aCusodetail.setDescription("Quoted Price");
					aCusodetail.setPrMasterId(QuotedPricePrMasterID);
					aCusodetail.setPriceMultiplier(new BigDecimal(1));
					aCusodetail.setQuantityOrdered(new BigDecimal(1));
					if(QuoteOrSuggestedPrice.equals("QuotedPrice")){
					logger.info("Unit Price::"+unitCost);
					aCusodetail.setUnitCost(SubTotalPrice);
					}else{
					aCusodetail.setUnitCost(SubTotalPrice);
					}
					aCusodetail.setTaxable((byte) 1);
					aCusodetail.setWhseCost(itsInventoryService.getWarehouseCost(QuotedPricePrMasterID));
					aCusodetail.setItemCode("QP");
					overAllcuSODetailLst.add(aCusodetail);
				}
			}
			
			aResponse.setRows(overAllcuSODetailLst);
			int k=1;
			for(Cusodetail thecsd:overAllcuSODetailLst){
				System.out.println((k++)+"From List==>"+thecsd.getItemCode());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>ApplySuggestedPrice:</b>","SalesOrderController",e,session,theRequest);
		}

		return aResponse;
	}
	@RequestMapping(value = "/getcuInvoice", method = RequestMethod.POST)
	public @ResponseBody Cuinvoice getInvoiceData(
			@RequestParam(value = "cuInvoiceId", required = false) Integer cuInvoiceID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws JobException {
		Cuinvoice aCuinvoice =new Cuinvoice();
		try{
		 aCuinvoice = jobService.getSingleCuInvoiceObj(cuInvoiceID);
		}catch(Exception e){
			e.printStackTrace();
		}
		return aCuinvoice;
	}
	
	@RequestMapping(value = "/LoadSolineitemDetails", method = RequestMethod.POST)
	public @ResponseBody
	List<Cusodetail> getCusodetail(@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		System.out.println("Line Items--->" + cuSOID);
		ArrayList<Cusodetail> cusodtllist=new ArrayList<Cusodetail>();
		try {
			 cusodtllist = (ArrayList<Cusodetail>) jobService.getSOReleaseLineItem(cuSOID); 
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}

		return cusodtllist; 
	}
	
	@RequestMapping(value = "/getoverride_taxterritory", method = RequestMethod.POST)
	public @ResponseBody CoTaxTerritory getoverride_taxterritory(@RequestParam(value = "customerID", required = false) Integer customerID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CoTaxTerritory aCoTaxTerritory=null;
		try {
			aCoTaxTerritory = jobService.getgetoverride_taxterritoryTaxID(customerID); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>getoverride_taxterritory Method</b>","SalesOrderController",e,session,theRequest);
		}
		return aCoTaxTerritory; 
	}
	
	
	@RequestMapping(value = "/updateTaxableOnInventory", method = RequestMethod.POST)
	public @ResponseBody Integer updateTaxableOnInventory(
			@RequestParam(value = "cuInvoiceID", required = false) Integer cuInvoiceID,
			@RequestParam(value = "operation", required = false) Integer operation,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		Integer updateStatus=null;
		try {
			updateStatus = jobService.updateInvoiceLineItems(cuInvoiceID,operation); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>getoverride_taxterritory Method</b>","SalesOrderController",e,session,theRequest);
		}
		return updateStatus; 
	}
	
	@RequestMapping(value = "/checkSalesOrderInvoicedornot", method = RequestMethod.POST)
	public @ResponseBody
	Boolean checkSalesOrderInvoicedornot(
			@RequestParam(value = "CuSOID", required = false) Integer cuSOID,
			HttpServletResponse theResponse,HttpServletRequest theRequest,HttpSession session) throws IOException, MessagingException {
		Boolean Exists = false;

		try {
			if (cuSOID != 0 && cuSOID != null) {
				Exists =salesServices.checkSalesOrderInvoicedornot(cuSOID);
						}
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>cuSOID:</b>"+cuSOID,"SalesOrderController",e,session,theRequest);
		}
		return Exists;
	}
	@RequestMapping(value="/getInvoicePDF", method = RequestMethod.POST)
	public @ResponseBody void getInvoicePDF(@RequestParam(value = "batchInvoiceCuID", required = false) String batchInvoiceCuID,
			@RequestParam(value = "batchInvoiceFromDate", required = false) String batchInvoiceFromDate,
			@RequestParam(value = "batchInvoiceToDate", required = false) String batchInvoiceToDate,
			@RequestParam(value = "rangeInvoiceFrom", required = false) String rangeInvoiceFrom,
			@RequestParam(value = "rangeInvoiceTo", required = false) String rangeInvoiceTo,
			@RequestParam(value = "cuInvoiceIdArray[]",required =false) ArrayList<String> cuInvoiceList,
			@RequestParam(value = "rxCustomerIdArray[]",required =false) ArrayList<String> rxCustomerIdArray,
			@RequestParam(value = "emailListArray[]",required =false) ArrayList<String> emailListArray,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session
		) throws IOException, MessagingException{
		Connection connection =null;TsUserSetting	objtsusersettings=null;
		InputStream imageStream =null;
		
		List<String>ccBccEmailIDs=null;
		
		
		Boolean emailStatus=false;
		int k=0;
		try{
			
			UserBean aUserBean = (UserBean)session.getAttribute("user");
		    TsUserLogin aTsUserLogin = this.userService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
		    String aFromAddress = aTsUserLogin.getEmailAddr();
		   // String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
		    String aUserID = aTsUserLogin.getLogOnName();
		    String aPassword = aTsUserLogin.getLogOnPswd();
		    String aPort = aTsUserLogin.getSmtpport().toString();
		    String ahost = aTsUserLogin.getSmtpsvr();
            
		    ccBccEmailIDs=salesServices.getListOfCCMailId( aTsUserLogin.getUserLoginId());
		    
		    
		    
		    EmailParameters eParam=new EmailParameters();
		    

			eParam.seteUserID(aUserID);
			eParam.setePassword(aPassword);
			eParam.setEhost(ahost);
			eParam.setePort(aPort);
			eParam.seteSubject("Customer Invoices");
			eParam.seteContent("");
			eParam.setFromAddress(aFromAddress);
			 HashMap<Integer, ArrayList<Cuinvoice>> hashmap = 
				        new HashMap<Integer, ArrayList<Cuinvoice>>();
			 int i=0;
			for(String rxcustomerID:rxCustomerIdArray){
				if(hashmap.get(Integer.parseInt(rxcustomerID))!=null){
					ArrayList<Cuinvoice> invlst= hashmap.get(Integer.parseInt(rxcustomerID));
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(Integer.parseInt(rxcustomerID));
					newobj.setCuInvoiceId(Integer.parseInt(cuInvoiceList.get(i)));
					newobj.setEmailList(emailListArray.get(i));
					invlst.add(newobj);
					hashmap.put(Integer.parseInt(rxcustomerID), invlst);
				}else{
					ArrayList<Cuinvoice> invlst= new ArrayList<Cuinvoice>();
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(Integer.parseInt(rxcustomerID));
					newobj.setCuInvoiceId(Integer.parseInt(cuInvoiceList.get(i)));
					newobj.setEmailList(emailListArray.get(i));
					invlst.add(newobj);
					hashmap.put(Integer.parseInt(rxcustomerID), invlst);
				}
				i=i+1;
			}
			
			for (Entry<Integer, ArrayList<Cuinvoice>> entry : hashmap.entrySet()) {
			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			    HashMap<String, ArrayList<Cuinvoice>> custbasedemailmap = 
				        new HashMap<String, ArrayList<Cuinvoice>>();
			    for(Cuinvoice thecuinv:entry.getValue()){
				if(custbasedemailmap.get(thecuinv.getEmailList())!=null){
					ArrayList<Cuinvoice> invlst= custbasedemailmap.get(thecuinv.getEmailList());
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(thecuinv.getRxCustomerId());
					newobj.setCuInvoiceId(thecuinv.getCuInvoiceId());
					newobj.setEmailList(thecuinv.getEmailList());
					invlst.add(newobj);
					custbasedemailmap.put(thecuinv.getEmailList(), invlst);
				}else{
					ArrayList<Cuinvoice> invlst= new ArrayList<Cuinvoice>();
					Cuinvoice newobj=new Cuinvoice();
					newobj.setRxCustomerId(thecuinv.getRxCustomerId());
					newobj.setCuInvoiceId(thecuinv.getCuInvoiceId());
					newobj.setEmailList(thecuinv.getEmailList());
					invlst.add(newobj);
					custbasedemailmap.put(thecuinv.getEmailList(), invlst);
				}
			    }
		     
			for (Entry<String, ArrayList<Cuinvoice>> entryobj : custbasedemailmap.entrySet()) { 
				String cuinvoiceID=null;
				Integer rxcustomerID=0;
				String emailID=entryobj.getKey();
				for(Cuinvoice thecuinv:entryobj.getValue()){
					rxcustomerID=thecuinv.getRxCustomerId();
					if(cuinvoiceID==null){
						cuinvoiceID=thecuinv.getCuInvoiceId()+"";
					}else{
						cuinvoiceID=cuinvoiceID+","+thecuinv.getCuInvoiceId();
					}
				}
				
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/BatchCustomerInvoice.jrxml");
			String writeFileName="EmailInvoice_"+aUserBean.getUserId()+"_"+k+".pdf";
			System.out.println("data "+batchInvoiceCuID+batchInvoiceFromDate+batchInvoiceToDate+rangeInvoiceFrom+rangeInvoiceTo);
			
			File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
			String absolutePath = file.getAbsolutePath();
			absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
			String ShipToPath="";
			if (OperatingSystemInfo.isWindows()) {
				logger.info("This is Windows");
				ShipToPath=absolutePath+"\\\\BillTo_ShipTo.jasper";
				absolutePath=absolutePath+"\\\\BatchInvoiceSubreport.jasper";
			} else if (OperatingSystemInfo.isMac()) {
				logger.info("This is Mac");
			} else if (OperatingSystemInfo.isUnix()) {
				logger.info("This is Unix or Linux");
				ShipToPath=absolutePath+"/BillTo_ShipTo.jasper";
				absolutePath=absolutePath+"/BatchInvoiceSubreport.jasper";
			} else if (OperatingSystemInfo.isSolaris()) {
				logger.info("This is Solaris");
			} else {
				logger.info("Your OS is not support!!");
			}
			
				String invoicefromdate=null;
				String invoicetodate=null;
				String yesterDayDate=JobUtil.getYesterdayDateString();
				if(batchInvoiceFromDate!=null && !batchInvoiceFromDate.trim().equals("")){
					SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
				    Date convertedCurrentDate = sdf1.parse(batchInvoiceFromDate);
				    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
				    invoicefromdate=sdff.format(convertedCurrentDate );
				    System.out.println("Formated From Date:"+invoicefromdate);
				}
				if(batchInvoiceToDate!=null && !batchInvoiceToDate.trim().equals("")){
					SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
				    Date convertedCurrentDate = sdf1.parse(batchInvoiceToDate);
				    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
				    invoicetodate=sdff.format(convertedCurrentDate );
				    System.out.println("Formated From Date:"+invoicetodate);
				}
				
			objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);	
			ConnectionProvider con = itspdfService.connectionForJasper();
			Blob blob =  objtsusersettings.getCompanyLogo();
			 imageStream =blob.getBinaryStream();
			//BufferedImage image = ImageIO.read(imageStream);
			params.put("HeaderImage", imageStream);
			params.put("HeaderText",((objtsusersettings.getHeaderText().replaceAll("`and`nbsp;", " ")).replaceAll("`", "")).replaceAll("amp;"," "));
			params.put("subReportPath", absolutePath);
			params.put("ShipToPath", ShipToPath);
			
			String remitTo = ( objtsusersettings.getRemitToDescription() !=null && ! objtsusersettings.getRemitToDescription().equals("") ? objtsusersettings.getRemitToDescription() +",": "" )
					+( objtsusersettings.getRemitToAddress1() !=null && !objtsusersettings.getRemitToAddress1().equals("") ?objtsusersettings.getRemitToAddress1()+"," : "" )
					+( objtsusersettings.getRemitToAddress2() !=null && !objtsusersettings.getRemitToAddress2().equals("") ? objtsusersettings.getRemitToAddress2()+"," : "" )
					+( objtsusersettings.getRemitToCity() !=null && !objtsusersettings.getRemitToCity().equals("") ? objtsusersettings.getRemitToCity()+"," : "" )
					+( objtsusersettings.getRemitToState() !=null && !objtsusersettings.getRemitToState().equals("") ? objtsusersettings.getRemitToState()+"," : "" )
					+ ( objtsusersettings.getRemitToZip() !=null && !objtsusersettings.getRemitToZip().equals("")? objtsusersettings.getRemitToZip() : "" );
			params.put("remitTo",remitTo );
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			
			connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
		   // String query ="SELECT joRelease.ReleaseType, tsUserLogin.Initials AS SalesRep,(CASE WHEN cuInvoiceDetail.PriceMultiplier IS NULL THEN (CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END *cuInvoiceDetail.QuantityBilled) ELSE ((CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END )*cuInvoiceDetail.QuantityBilled*cuInvoiceDetail.PriceMultiplier )END ) AS total, veShipVia.Description AS ShippedVia, joMaster.Description AS Job, cuInvoice.*, tsUserLogin.Initials,cuTerms.Description AS termsdesc,cuInvoice.ShipToMode AS ShipToMode, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Description ELSE      (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxM.Name FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxM.rxMasterId) ELSE         (CASE WHEN cuInvoice.ShipToMode=2 THEN (SELECT rxM.Name FROM rxMaster rxM WHERE rxM.rxMasterId =cuInvoice.rxCustomerID) ELSE            (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Name FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoName, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Address1 ELSE     (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.address1 FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId)  ELSE          (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationAddress1) ELSE             (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Address1 FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END)  END) END) AS shiptoAddress1, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Address2 ELSE (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.address2 FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId) ELSE  (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationAddress2) ELSE (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Address2 FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END)  END) END) AS shiptoAddress2,   (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.City ELSE (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.city FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId)   ELSE  (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationCity) ELSE (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.City FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoCity,  (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.State ELSE (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.state FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId)  ELSE  (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationState) ELSE (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.State FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoState, (CASE WHEN cuInvoice.ShipToMode=0 THEN prWarehouse.Zip ELSE  (CASE WHEN cuInvoice.ShipToMode=1 THEN (SELECT rxA.zip FROM rxAddress rxA,rxMaster rxM WHERE rxM.rxMasterId =rxA.rxMasterId AND rxA.rxMasterId=cuInvoice.rxCustomerID AND isShipTo = 1 GROUP BY rxA.rxMasterId) ELSE   (CASE WHEN cuInvoice.ShipToMode=2 THEN (joMaster.LocationZip) ELSE  (CASE WHEN cuInvoice.ShipToMode=3 THEN (SELECT rxA.Zip FROM rxAddress rxA WHERE rxA.rxAddressId = cuInvoice.rxShipToAddressID)ELSE \"\" END ) END) END) END) AS shiptoZip,  (SELECT rxMaster.Name FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID)  AS billtoName, (SELECT rxAddress.address1 FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoAddress1, (SELECT rxAddress.address2 FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoAddress2, (SELECT rxAddress.city FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoCity, (SELECT rxAddress.state FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoState, (SELECT rxAddress.zip FROM rxAddress LEFT JOIN rxMaster ON rxAddress.rxMasterID = rxMaster.rxMasterID WHERE rxAddress.rxMasterId =cuInvoice.rxBillToID AND IsMailing = 1 GROUP BY rxAddress.rxMasterID) AS billtoZip,  (SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo FROM (joMaster RIGHT JOIN joRelease ON joMaster.joMasterID = joRelease.joMasterID LEFT JOIN joReleaseDetail ON joReleaseDetail.joReleaseID=joRelease.joReleaseID) RIGHT JOIN (veShipVia RIGHT JOIN (cuInvoice LEFT JOIN tsUserLogin ON cuInvoice.cuAssignmentID0 = tsUserLogin.UserLoginID) ON veShipVia.veShipViaID = cuInvoice.veShipViaID) ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID LEFT JOIN cuInvoiceDetail ON cuInvoiceDetail.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN tsUserLogin ON tsUserLogin.UserLoginID = cuInvoice.cuAssignmentID0 LEFT JOIN cuTerms ON cuTerms.cuTermsID = cuInvoice.cuTermsID  LEFT JOIN prWarehouse ON prWarehouse.prWarehouseID=cuInvoice.rxShipToAddressID LEFT JOIN rxMaster ON rxMaster.rxMasterID=cuInvoice.rxCustomerID LEFT JOIN rxAddress ON rxAddress.rxMasterId=cuInvoice.rxCustomerID";
		  
		    String query ="SELECT (CASE WHEN cuInvoiceDetail.PriceMultiplier IS NULL THEN (CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END *cuInvoiceDetail.QuantityBilled) ELSE ((CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END )*cuInvoiceDetail.QuantityBilled*cuInvoiceDetail.PriceMultiplier )END ) AS total,"+
" joRelease.ReleaseType, tsUserLogin.Initials AS SalesRep,veShipVia.Description AS ShippedVia,"+
" cuInvoiceDetail.PriceMultiplier,cuInvoiceDetail.UnitCost,cuInvoiceDetail.QuantityBilled,"+
" joMaster.Description AS Job, cuInvoice.*, tsUserLogin.Initials,"+
" cuTerms.Description AS termsdesc,cuInvoice.ShipToMode AS ShipToMode,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.address1,billto.address1) AS address1,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.address2,billto.address2) AS address2,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.city,billto.city) AS city,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.state,billto.state) AS state,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.zip,billto.zip) AS zip,rxMaster.Name,"+
//" rxAddress.address1,rxAddress.address2,rxAddress.city,rxAddress.state,rxAddress.zip,rxMaster.Name,"+
" cuInvoice.prToWarehouseID AS prwarehouseid,cuInvoice.rxCustomerID AS customerID,joMaster.joMasterID,"+
" cuInvoice.rxShipToAddressID "+
" FROM cuInvoice"+
" LEFT JOIN cuInvoiceDetail ON cuInvoiceDetail.cuInvoiceID = cuInvoice.cuInvoiceID"+
" LEFT JOIN joReleaseDetail ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID"+
" LEFT JOIN joRelease ON joReleaseDetail.joReleaseID=joRelease.joReleaseID"+
" LEFT JOIN joMaster ON joMaster.joMasterID = joRelease.joMasterID"+
" LEFT JOIN cuTerms ON cuTerms.cuTermsID = cuInvoice.cuTermsID"+
" LEFT JOIN tsUserLogin ON tsUserLogin.UserLoginID = cuInvoice.cuAssignmentID0"+
" LEFT JOIN rxMaster ON rxMaster.rxMasterID=cuInvoice.rxCustomerID"+
" LEFT JOIN rxAddress ON (rxMaster.rxMasterID=rxAddress.rxMasterID AND rxAddress.IsMailing = 1)"+
" LEFT JOIN rxAddress billto ON (rxMaster.rxMasterID=billto.rxMasterID AND billto.IsDefault = 1)"+
" LEFT JOIN veShipVia ON (veShipVia.veShipViaID = cuInvoice.veShipViaID)"
+ "  WHERE  rxMaster.rxMasterID ="+rxcustomerID 
+" and cuInvoice.cuInvoiceID in("+cuinvoiceID+") ";
		    
   
		    
		    
		    

if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0){
				if(!query.contains("WHERE")){
					query=query+" WHERE Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' ";
				}else{
					query=query+" AND Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"'";
				}
				
			}else if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0){
				if(!query.contains("WHERE")){
				query=query+" WHERE  Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' ";
				}else{
				query=query+" And Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' ";
				}
			}
			query=query+" GROUP BY cuInvoice.cuInvoiceID  ORDER BY rxMaster.Name ASC";
			
				
			System.out.println("query::"+query);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			//ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			ReportService.dynamicWriteReportCall(theRequest,theResponse,params,"pdf",jd,writeFileName,connection);

			eParam.setToAddress(emailID);
			eParam.setWriteFileName(writeFileName);
			eParam.setFileName("Customer Invoices.pdf");
			eParam.setCc(ccBccEmailIDs);
			SendQuoteMail sendBulkMail=new SendQuoteMail();
			emailStatus=sendBulkMail.sendBulkMailAttachment(eParam,theRequest);
		    System.out.println("Email Status"+emailStatus);
			if(emailStatus==true){
				for(Cuinvoice updateEmailDate:entryobj.getValue()){
				     itsPurchaseService.updateEmailInvoiceDate(updateEmailDate.getCuInvoiceId(),0,emailStatus);
				}
			}
			else if(emailStatus==false){
				for(Cuinvoice updateEmailDate:entryobj.getValue()){
				     itsPurchaseService.updateEmailInvoiceDate(updateEmailDate.getCuInvoiceId(),0,emailStatus);
				}
				
			}
			
			k=k+1;
			}
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>joReleaseId:</b>"+batchInvoiceCuID,"SalesOrderController",e,session,theRequest);
		}finally{
			try {
				objtsusersettings=null;
				imageStream.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@RequestMapping(value="/getSettingTaxTerritory",  method = RequestMethod.POST)
	public @ResponseBody  Integer getStatusTaxTerritory(@RequestParam(value="settingsTaxTerritory",required=false) String settingsTaxTerritory,  HttpServletRequest request ,
			HttpSession session, HttpServletResponse response)
	{
		Integer settingsTaxTerritoryValue=null;
		System.out.println(settingsTaxTerritory);
		Integer sysVariableId=InventoryConstant.getConstantSysvariableId(settingsTaxTerritory);
		System.out.println("sysVariableId"+sysVariableId);
		settingsTaxTerritoryValue=userService.getTaxTerritorySettingsValue(sysVariableId);
		return settingsTaxTerritoryValue;
	}
}
