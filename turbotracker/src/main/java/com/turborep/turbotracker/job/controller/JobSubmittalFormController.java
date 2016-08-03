package com.turborep.turbotracker.job.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.json.JSONArray;
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
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.employee.dao.Ecsplitcode;
import com.turborep.turbotracker.employee.dao.Ecsplitjob;
import com.turborep.turbotracker.employee.dao.EmMasterBean;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.dao.JobPurchaseOrderBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.job.dao.Joscheduledetail;
import com.turborep.turbotracker.job.dao.Joschedulemodel;
import com.turborep.turbotracker.job.dao.Josubmittaldetail;
import com.turborep.turbotracker.job.dao.Josubmittalheader;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.quickbooks.service.QuickBooksService;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
@RequestMapping("/jobtabs3")
public class JobSubmittalFormController {

	Logger logger = Logger.getLogger(JobSubmittalFormController.class);

	private Integer itsColumnLength;
	private Jomaster jomasterDetails;
	private Josubmittalheader joSubmittalDetails;
	private String cutomerRxName;
	private final String USER_AGENT = "Mozilla/5.0";

	@Resource(name = "jobService")
	private JobService jobService;

	@Resource(name="userLoginService")
	private UserService userService;

	
	@Resource(name = "companyService")
	private CompanyService companyService;

	@Resource(name = "vendorService")
	private VendorServiceInterface itsVendorService;

	@Resource(name = "pdfService")
	private PDFService itspdfService;

	@Resource(name = "qbService")
	private QuickBooksService itsQuickBooksService;
	
	@Resource(name = "sysService")
	private SysService itsSysService;

	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;
	
	@RequestMapping(value = "jobwizard_submittal", method = RequestMethod.GET)
	public String getJobsSubmittalPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session,HttpServletRequest therequest, ModelMap theModel,
			HttpServletResponse theResponse) throws IOException, JobException, MessagingException {
		String aSubmittalBy = null;
		String aSingedBy = null;
		try {
			setJomasterDetails(theJobNumber,joMasterID);
			setJoSubmittalDetails(theJobNumber,joMasterID);
			Jomaster aJomaster = getJomasterDetails();
			Josubmittalheader aJosubmittalheader = getJoSubmittalDetails();
			if (aJosubmittalheader != null) {
				if (aJosubmittalheader.getSubmittalById() != null) {
					aSubmittalBy = (String) jobService
							.getSubmittalName(aJosubmittalheader
									.getSubmittalById());
				}
				if (aJosubmittalheader.getSignedById() != null) {
					aSingedBy = (String) jobService
							.getSignedName(aJosubmittalheader.getSignedById());
				}
			}
			if (aJomaster.getRxCustomerId() != null) {
				try {
					setCutomerRxName((String) companyService
							.getCustomerName(aJomaster.getRxCustomerId()));
				} catch (CompanyException e) {
					logger.error(e.getMessage(), e);
					theResponse.sendError(e.getItsErrorStatusCode(),
							e.getMessage());
				}
			}
			theModel.addAttribute("joMasterDetails", aJomaster);
			if (aJomaster.getDescription() != null) {
				String aString = aJomaster.getDescription();
				String string = aString.replaceAll("\"", "");
				theModel.addAttribute("joMasterDescription", string);
			}
			theModel.addAttribute("joSubmittalDetails", aJosubmittalheader);
			if (aJomaster.getRxCustomerId() != null) {
				theModel.addAttribute("CustomerName", getCutomerRxName());
			}
			theModel.addAttribute("submittalName", aSubmittalBy);
			theModel.addAttribute("signedName", aSingedBy);
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber,theJobCustomer:</b>"+theJobNumber+","+theJobCustomer,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return "job/jobwizard_submittal";
	}

	public Jomaster getJomasterDetails() {
		return jomasterDetails;
	}

	public void setJomasterDetails(String jobNumber,Integer joMasterID) throws JobException {
		this.jomasterDetails = jobService.getSingleJobDetails(jobNumber,joMasterID);
	}

	public Josubmittalheader getJoSubmittalDetails() {
		return joSubmittalDetails;
	}

	private void setJoSubmittalDetails(String jobNumber,Integer joMasterID) throws JobException {
		this.joSubmittalDetails = jobService
				.getSingleSubmittalDetails(jobNumber, joMasterID);
	}

	@RequestMapping(value = "submittal", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getSubmittalList(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws JobException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			List<?> bids = jobService.getSubmittalList(theJobNumber);
			response.setRows(bids);
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "submittalProducts", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getSubmittalProductList(
			@RequestParam(value = "submittalHeaderID", required = false) String theSubmittalID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest)
			throws JobException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			List<?> submittalProduct = jobService
					.getSubmittalProductList(theSubmittalID);
			response.setRows(submittalProduct);
		} catch (JobException e) {
			sendTransactionException("<b>theSubmittalID:</b>"+theSubmittalID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "submittalSheduled", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getSubmittalSheduledList(
			@RequestParam(value = "submittalDetailsID", required = false) Integer theSubmitID,
			@RequestParam(value = "theParameters", required = false) String theParameters,
			@RequestParam(value = "theKeys[]", required = false) String[] theKeys,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws JobException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			List<?> submittalSheduled = jobService.getSubmittalSheduledList(
					theSubmitID, theParameters);
			List<Map<String, Object>> aSchedule = new ArrayList<Map<String, Object>>();
			Iterator<?> aIterator = submittalSheduled.iterator();
			while (aIterator.hasNext()) {
				Map<String, Object> aObject = new HashMap<String, Object>();
				Object[] aObj = (Object[]) aIterator.next();
				for (int index = 0; index < theKeys.length; index++) {
					aObject.put(theKeys[index], aObj[index]);

				}
				aSchedule.add(aObject);
			}
			response.setRows(aSchedule);

		} catch (JobException e) {
			sendTransactionException("<b>theSubmitID:</b>"+theSubmitID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/scheduleList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getScheduleList(@RequestParam("term") String theScheduleText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aScheduleList = null;
		try {
			aScheduleList = (ArrayList<AutoCompleteBean>) jobService
					.getScheduleList(theScheduleText);
		} catch (JobException e) {
			sendTransactionException("<b>theScheduleText:</b>"+theScheduleText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aScheduleList;
	}

	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getProductList(@RequestParam("term") String theProductText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductList = null;
		try {
			aProductList = (ArrayList<AutoCompleteBean>) jobService
					.getProductList(theProductText);
		} catch (JobException e) {
			sendTransactionException("<b>theScheduleText:</b>"+theProductText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductList;
	}

	@RequestMapping(value = "/manufactureList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getManufactureList(@RequestParam("term") String theManufactureText,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aManufactureList = null;
		try {
			aManufactureList = (ArrayList<AutoCompleteBean>) jobService
					.getManufactureList(theManufactureText);
		} catch (JobException e) {
			sendTransactionException("<b>theManufactureText:</b>"+theManufactureText,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aManufactureList;
	}

	@RequestMapping(value = "/productName", method = RequestMethod.GET)
	public @ResponseBody
	Joschedtempheader getProductName(
			@RequestParam(value = "scheduleHeaderId", required = false) Integer theProductName,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		Joschedtempheader aProductName = null;
		try {
			aProductName = jobService.getProductName(theProductName);
		} catch (JobException e) {
			sendTransactionException("<b>theProductName:</b>"+theProductName,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductName;
	}

	@RequestMapping(value = "/getHoldCredit", method = RequestMethod.GET)
	public @ResponseBody
	Cumaster getHoldCreditName(
			@RequestParam(value = "customerID", required = false) Integer theProductName,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws IOException, MessagingException {
		Cumaster aCumaster = null;
		try {
			if (theProductName != null) {
				aCumaster = new Cumaster();
				aCumaster = jobService.getSingleCuMasterDetails(theProductName);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theProductName:</b>"+theProductName,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCumaster;
	}

	@RequestMapping(value = "/ManufactureName", method = RequestMethod.GET)
	public @ResponseBody
	String getManufactureName(
			@RequestParam(value = "scheduleHeaderId", required = false) String theManufactureName,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		String aManufactureName = "";
		try {
			Integer aManufactureId = jobService
					.getManufactureId(theManufactureName);
			aManufactureName = jobService.getManufactureName(aManufactureId);
			if (aManufactureName == null) {
				aManufactureName = "";
			}
		} catch (JobException e) {
			sendTransactionException("<b>theManufactureName:</b>"+theManufactureName,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aManufactureName;
	}

	@RequestMapping(value = "/manpulaterSubmittal_Product", method = RequestMethod.POST)
	public @ResponseBody
	Integer insertSubmittalProduct(
			@RequestParam(value = "scheduledID", required = false) Integer theScheduler,
			@RequestParam(value = "product", required = false) String theProduct,
			@RequestParam(value = "quantity", required = false) String theQuantity,
			@RequestParam(value = "released", required = false) String theRelesed,
			@RequestParam(value = "manufacturerID", required = false) Integer theManufacture,
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "submittalHeaderID", required = false) Integer theSubmittalID,
			@RequestParam(value = "productID", required = false) Integer theProductID,
			@RequestParam(value = "estimatecost", required = false) BigDecimal theEstimate,
			@RequestParam(value = "cost", required = false) BigDecimal theCost,
			@RequestParam(value = "status", required = false) Integer theStatus,
			@RequestParam(value = "joSubmittalID", required = false) Integer theSubmittalMainID,
			@RequestParam(value = "joSchdTempHeaderID", required = false) Integer theSchedTempMainID,
			@RequestParam(value = "joSubmittalHeaderID", required = false) Integer theJoSubmittalHeaderID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, JobException, IOException, MessagingException {
		Josubmittaldetail aJosubmittaldetail = new Josubmittaldetail();
		try {
			aJosubmittaldetail.setJoSchedTempHeaderId(theScheduler);
			aJosubmittaldetail.setProduct(theProduct);
			aJosubmittaldetail.setQuantity(theQuantity);
			aJosubmittaldetail.setParagraph(theRelesed);
			aJosubmittaldetail.setRxManufacturerId(theManufacture);
			aJosubmittaldetail.setJoSubmittalHeaderId(theSubmittalID);
			aJosubmittaldetail.setEstimatedCost(theEstimate);
			aJosubmittaldetail.setCost(theCost);
			aJosubmittaldetail.setStatus(theStatus);
			if (operation.equals("add")) {
				jobService.addSubmittal_Product(aJosubmittaldetail);
			} else if (operation.equals("edit")) {
				aJosubmittaldetail.setJoSubmittalDetailId(theSubmittalMainID);
				aJosubmittaldetail.setJoSchedTempHeaderId(theSchedTempMainID);
				aJosubmittaldetail
						.setJoSubmittalHeaderId(theJoSubmittalHeaderID);
				jobService.updateSubmittal_Product(aJosubmittaldetail);
			} else {
				aJosubmittaldetail.setJoSubmittalDetailId(theSubmittalMainID);
				aJosubmittaldetail.setJoSchedTempHeaderId(theSchedTempMainID);
				aJosubmittaldetail
						.setJoSubmittalHeaderId(theJoSubmittalHeaderID);
				jobService.deleteSubmittal_Product(aJosubmittaldetail);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theSubmittalID:</b>"+theSubmittalID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "/modelNoList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getModelNoList(@RequestParam("term") String theModelNo,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aModelNoList = null;
		try {
			aModelNoList = (ArrayList<AutoCompleteBean>) jobService
					.getModelNoList(theModelNo);
		} catch (JobException e) {
			sendTransactionException("<b>theModelNo:</b>"+theModelNo,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aModelNoList;
	}

	@RequestMapping(value = "/scheduledModelID", method = RequestMethod.GET)
	public @ResponseBody
	Joschedulemodel geScheduledModel(
			@RequestParam(value = "scheduleModelId", required = false) Integer theScheduledModelID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		Joschedulemodel aJoschedulemodel = null;
		try {
			aJoschedulemodel = jobService.geScheduledModel(theScheduledModelID);
		} catch (JobException e) {
			sendTransactionException("<b>theScheduledModelID:</b>"+theScheduledModelID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoschedulemodel;
	}

	@RequestMapping(value = "/scheduledTag", method = RequestMethod.GET)
	public @ResponseBody
	Joscheduledetail geScheduledTag(
			@RequestParam(value = "scheduleModelId", required = false) Integer theScheduledModelID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		Joscheduledetail aJoscheduledetail = null;
		try {
			aJoscheduledetail = jobService.geScheduledTag(theScheduledModelID);
		} catch (JobException e) {
			sendTransactionException("<b>theScheduledModelID:</b>"+theScheduledModelID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoscheduledetail;
	}

	@RequestMapping(value = "/manpulaterSubmittal_Schedule", method = RequestMethod.POST)
	public @ResponseBody
	Integer insertSubmittalSchedule(
			@RequestParam(value = "theHeaderID", required = false) Integer theHeaderID,
			@RequestParam(value = "theJoScheduleDetailID", required = false) Integer theJoScheduleDetailID,
			@RequestParam(value = "col1", required = false) String col01,
			@RequestParam(value = "col2", required = false) String col02,
			@RequestParam(value = "col3", required = false) String col03,
			@RequestParam(value = "col4", required = false) String col04,
			@RequestParam(value = "col5", required = false) String col05,
			@RequestParam(value = "col6", required = false) String col06,
			@RequestParam(value = "col7", required = false) String col07,
			@RequestParam(value = "col8", required = false) String col08,
			@RequestParam(value = "col9", required = false) String col09,
			@RequestParam(value = "col10", required = false) String col10,
			@RequestParam(value = "col11", required = false) String col11,
			@RequestParam(value = "col12", required = false) String col12,
			@RequestParam(value = "col13", required = false) String col13,
			@RequestParam(value = "col14", required = false) String col14,
			@RequestParam(value = "col15", required = false) String col15,
			@RequestParam(value = "col16", required = false) String col16,
			@RequestParam(value = "col17", required = false) String col17,
			@RequestParam(value = "col18", required = false) String col18,
			@RequestParam(value = "col19", required = false) String col19,
			@RequestParam(value = "col20", required = false) String col20,
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "Model", required = false) String theModel,
			@RequestParam(value = "line", required = false) Integer theLine,
			@RequestParam(value = "modelNoID", required = false) Integer theModelNoID,
			@RequestParam(value = "Qty", required = false) BigDecimal theQty,
			@RequestParam(value = "joSubmittalId", required = false) Integer theJoSubmittalId,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws ParseException, IOException, MessagingException {
		Joscheduledetail aJoscheduledetail = new Joscheduledetail();
		try {
			aJoscheduledetail.setJoScheduleModelId(theModelNoID);
			aJoscheduledetail.setQuantity(theQty);
			aJoscheduledetail.setCol01(col01);
			aJoscheduledetail.setCol02(col02);
			aJoscheduledetail.setCol03(col03);
			aJoscheduledetail.setCol04(col04);
			aJoscheduledetail.setCol05(col05);
			aJoscheduledetail.setCol06(col06);
			aJoscheduledetail.setCol07(col07);
			aJoscheduledetail.setCol08(col08);
			aJoscheduledetail.setCol09(col09);
			aJoscheduledetail.setCol10(col10);
			aJoscheduledetail.setCol11(col11);
			aJoscheduledetail.setCol12(col12);
			aJoscheduledetail.setCol13(col13);
			aJoscheduledetail.setCol14(col14);
			aJoscheduledetail.setCol15(col15);
			aJoscheduledetail.setCol16(col16);
			aJoscheduledetail.setCol17(col17);
			aJoscheduledetail.setCol18(col18);
			aJoscheduledetail.setCol19(col19);
			aJoscheduledetail.setCol20(col20);
			aJoscheduledetail.setJoLineNumber(theLine);
			aJoscheduledetail.setJoSubmittalDetailId(theJoSubmittalId);
			if (operation.equals("add")) {
				jobService.addSubmittalSchedule(aJoscheduledetail);
			} else if (operation.equals("edit")) {
				aJoscheduledetail.setJoScheduleDetailId(theJoScheduleDetailID);
				jobService.editSubmittalSchedule(aJoscheduledetail);
			} else if (operation.equals("del")) {
				aJoscheduledetail.setJoScheduleDetailId(theJoScheduleDetailID);
				jobService.deleteSubmittalSchedule(theJoScheduleDetailID);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theHeaderID:</b>"+theHeaderID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = "createSubmittal", method = RequestMethod.POST)
	public @ResponseBody
	Josubmittalheader createNewSubmittalForm(
			@RequestParam(value = "submittal_Date", required = false) String theSubmittDate,
			@RequestParam(value = "copiestext_name", required = false) String theCopiesNo,
			@RequestParam(value = "submittal_name", required = false) String thesubmittalName,
			@RequestParam(value = "submittal_hiddenName", required = false) Integer theSubmittalID,
			@RequestParam(value = "revision_name", required = false) Byte theOptionName,
			@RequestParam(value = "addendum_name", required = false) String theAddendum,
			@RequestParam(value = "date_PlanName", required = false) String thePlanDate,
			@RequestParam(value = "signed_name", required = false) String theSignedName,
			@RequestParam(value = "signed_hiddenName", required = false) Integer theSignedHiddenName,
			@RequestParam(value = "sheet_remarks", required = false) String theSheetReMarks,
			@RequestParam(value = "comments_internal", required = false) String theComments,
			@RequestParam(value = "architectsList_name", required = false) String theArchitect,
			@RequestParam(value = "engineersRXList_name", required = false) String theEnginner,
			@RequestParam(value = "salesRep_name", required = false) String theSalesRep,
			@RequestParam(value = "jobName", required = false) String jobNumber,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			HttpSession session,HttpServletRequest therequest, ModelMap theModel,
			HttpServletResponse theResponse) throws IOException, JobException,
			ParseException, MessagingException {
		
		Josubmittalheader aJosubmittalheader = new Josubmittalheader();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (theSubmittDate != null && theSubmittDate != "") {
				aJosubmittalheader.setSubmittalDate(DateUtils.parseDate(
						theSubmittDate, new String[] { "MM/dd/yyyy" }));
			}
			aJosubmittalheader.setCopies(theCopiesNo);
			aJosubmittalheader.setSubmittalById(theSubmittalID);
			aJosubmittalheader.setRevision(theOptionName);
			aJosubmittalheader.setAddendum(theAddendum);
			if (thePlanDate != null && thePlanDate != "") {
				aJosubmittalheader.setPlanDate(DateUtils.parseDate(thePlanDate,
						new String[] { "MM/dd/yyyy" }));
			}
			aJosubmittalheader.setSignedById(theSignedHiddenName);
			aJosubmittalheader.setRemarkNote(theSheetReMarks);
			aJosubmittalheader.setRevisionNote(theComments);
			aJosubmittalheader.setJoMasterId(joMasterID);
			aJosubmittalheader.setCreatedById(aUserBean.getUserId());
			Date theDate;
			GregorianCalendar aCalendar = new GregorianCalendar();
			theDate = aCalendar.getTime();
			aJosubmittalheader.setCreatedOn(theDate);
			jobService.addSubmittal(aJosubmittalheader);
		} catch (JobException e) {
			sendTransactionException("<b>jobNumber:</b>"+jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		logger.info("*********** Submittal Inserted ***********");
		return aJosubmittalheader;
	}

	@RequestMapping(value = "updateSubmittal", method = RequestMethod.POST)
	public @ResponseBody
	Josubmittalheader updateSubmittalForm(
			@RequestParam(value = "submittal_Date", required = false) String theSubmittDate,
			@RequestParam(value = "copiestext_name", required = false) String theCopiesNo,
			@RequestParam(value = "Submittal_name", required = false) String thesubmittalName,
			@RequestParam(value = "submittal_hiddenName", required = false) Integer theSubmittalID,
			@RequestParam(value = "revision_name", required = false) Byte theOptionName,
			@RequestParam(value = "addendum_name", required = false) String theAddendum,
			@RequestParam(value = "date_PlanName", required = false) String thePlanDate,
			@RequestParam(value = "signed_name", required = false) String theSignedName,
			@RequestParam(value = "signed_hiddenName", required = false) Integer theSignedHiddenName,
			@RequestParam(value = "remarkSheet", required = false) String theSheetReMarks,
			@RequestParam(value = "internalComments", required = false) String theComments,
			@RequestParam(value = "architectsList_name", required = false) String theArchitect,
			@RequestParam(value = "engineersRXList_name", required = false) String theEnginner,
			@RequestParam(value = "salesRep_name", required = false) String theSalesRep,
			@RequestParam(value = "jobName", required = false) String jobNumber,
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			HttpSession theSession, ModelMap theModel,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException,
			ParseException, MessagingException {

		Josubmittalheader aJosubmittalheader = new Josubmittalheader();
		int submittalHeader = jobService
				.getSubmittalHeaderIDByJoMaster(joMasterID);
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			if (theSubmittDate != null && theSubmittDate != "") {
				aJosubmittalheader.setSubmittalDate(DateUtils.parseDate(
						theSubmittDate, new String[] { "MM/dd/yyyy" }));
			}
			aJosubmittalheader.setCopies(theCopiesNo);
			aJosubmittalheader.setSubmittalById(theSubmittalID);
			aJosubmittalheader.setRevision(theOptionName);
			aJosubmittalheader.setAddendum(theAddendum);
			if (thePlanDate != null && thePlanDate != "") {
				aJosubmittalheader.setPlanDate(DateUtils.parseDate(thePlanDate,
						new String[] { "MM/dd/yyyy" }));
			}
			aJosubmittalheader.setSignedById(theSignedHiddenName);
			aJosubmittalheader.setRemarkNote(theSheetReMarks);
			aJosubmittalheader.setRemarkNote(remarks);
			aJosubmittalheader.setRevisionNote(theComments);
			aJosubmittalheader.setJoMasterId(joMasterID);
			aJosubmittalheader.setCreatedById(aUserBean.getUserId());
			Date theDate;
			GregorianCalendar aCalendar = new GregorianCalendar();
			theDate = aCalendar.getTime();
			aJosubmittalheader.setCreatedOn(theDate);
			aJosubmittalheader.setJoSubmittalHeaderId(submittalHeader);
			jobService.updateSubmittal(aJosubmittalheader);
		} catch (JobException e) {
			sendTransactionException("<b>jobNumber:</b>"+jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		logger.info("*********** Submittal updated ***********");
		return aJosubmittalheader;
	}

	@RequestMapping(value = "/getPurchaseDetails", method = RequestMethod.GET)
	public @ResponseBody
	JobPurchaseOrderBean getPurchaseDetails(
			@RequestParam(value = "vePoID", required = false) Integer thevePOID,
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse)
			throws IOException, MessagingException {
		JobPurchaseOrderBean aJobPurchaseOrderBean = new JobPurchaseOrderBean();
		try {
			aJobPurchaseOrderBean = jobService.getPurchaseDetails(thevePOID,
					theJobNumber);
		} catch (JobException e) {
			sendTransactionException("<b>thevePOID:</b>"+thevePOID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJobPurchaseOrderBean;
	}

	@RequestMapping(value = "/userName", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getUserName(@RequestParam("term") String theUserName,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aUserNameList = null;
		try {
			aUserNameList = (ArrayList<AutoCompleteBean>) jobService
					.getUserName(theUserName);
		} catch (JobException e) {
			sendTransactionException("<b>theUserName:</b>"+theUserName,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aUserNameList;
	}

	/*
	 * @RequestMapping(value="/frieghtChange", method = RequestMethod.GET)
	 * public @ResponseBody List<?> getFrieghtChange(@RequestParam("term")
	 * String theFrieghtChange, HttpServletResponse theResponse) throws
	 * IOException{ logger.debug("Received request to get search Jobs Lists");
	 * ArrayList<AutoCompleteBean> aFrieghtChangeList = null; try{
	 * aFrieghtChangeList = (ArrayList<AutoCompleteBean>)
	 * jobService.getFrieghtChange(theFrieghtChange); }catch (JobException e) {
	 * logger.error(e.getMessage());
	 * theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage()); }
	 * return aFrieghtChangeList; }
	 */

	/*
	 * @RequestMapping(value="/shipVia", method = RequestMethod.GET) public
	 * @ResponseBody List<?> getShipVia(@RequestParam("term") String theShipVia,
	 * HttpServletResponse theResponse) throws IOException, JobException{
	 * logger.debug("Received request to get search Jobs Lists");
	 * ArrayList<AutoCompleteBean> aShipViaList = null; try{ aShipViaList =
	 * (ArrayList<AutoCompleteBean>) jobService.getShipVia(theShipVia); }catch
	 * (JobException e) { logger.error(e.getMessage());
	 * theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage()); }
	 * return aShipViaList; }
	 */
//	/getPayeeNameList

	@RequestMapping(value = "/getPayeeNameList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getPayeeNameList(
			@RequestParam("term") String theProductNameWithCode,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) jobService
					.getPayeeNameList(theProductNameWithCode);
		} catch (JobException e) {
			sendTransactionException("<b>theProductNameWithCode:</b>"+theProductNameWithCode,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductCodeWithNameList;
	}
	@RequestMapping(value = "/productCodeWithNameList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getProductWithNameList(
			@RequestParam("term") String theProductNameWithCode,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			
			
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) jobService
					.getProductWithNameList(theProductNameWithCode);
		} catch (JobException e) {
			sendTransactionException("<b>theProductNameWithCode:</b>"+theProductNameWithCode,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductCodeWithNameList;
	}
	@RequestMapping(value = "/rxMasterDetails", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getrxMasterDetails(
			@RequestParam("term") String theProductNameWithCode,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) jobService
					.getPayeeNameList(theProductNameWithCode);
		} catch (JobException e) {
			sendTransactionException("<b>theProductNameWithCode:</b>"+theProductNameWithCode,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductCodeWithNameList;
	}
	@RequestMapping(value = "/productCodeList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getProductCodeList(HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest)
			throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) jobService
					.getLineItemProductList();
		} catch (JobException e) {
			sendTransactionException("<b>MethodName:</b>getProductCodeList","JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aProductCodeWithNameList;
	}

	@RequestMapping(value = "/productSingleName", method = RequestMethod.GET)
	public @ResponseBody
	Prmaster getProductSingleNameList(
			@RequestParam(value = "itemCode", required = false) String theProductNameWithCode,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		Prmaster aPrmaster = null;
		try {
			Integer productID = jobService
					.getProductIDBaseName(theProductNameWithCode);
			aPrmaster = jobService.getProductSingleNameList(productID);
		} catch (JobException e) {
			sendTransactionException("<b>theProductNameWithCode:</b>"+theProductNameWithCode,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPrmaster;
	}

	@RequestMapping(value = "/scheduledColumnName", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getScheduledColumnName(
			@RequestParam(value = "tempHeaderID", required = false) Integer theSchelduleModelID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<Joschedtempcolumn> aJoschedtempcolumns = null;
		try {
			aJoschedtempcolumns = (ArrayList<Joschedtempcolumn>) jobService
					.getScheduledColumnName(theSchelduleModelID);
			itsColumnLength = aJoschedtempcolumns.size();
		} catch (JobException e) {
			sendTransactionException("<b>theSchelduleModelID:</b>"+theSchelduleModelID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoschedtempcolumns;
	}

	@RequestMapping(value = "sheduledColumnModel", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getSheduledColumnModel(
			@RequestParam(value = "submittalDetails", required = false) String theSubmittalDetailsID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try {
			List<?> submittalSheduled = jobService.getSheduledColumnModel(
					theSubmittalDetailsID, itsColumnLength);
			response.setRows(submittalSheduled);
		} catch (JobException e) {
			sendTransactionException("<b>theSubmittalDetailsID:</b>"+theSubmittalDetailsID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}

	public String getCutomerRxName() {
		return cutomerRxName;
	}

	public void setCutomerRxName(String cutomerRxName) {
		this.cutomerRxName = cutomerRxName;
	}

	@RequestMapping(value = "/addPORelease", method = RequestMethod.POST)
	public @ResponseBody
	Vepo updatePOReleaseRelease(
			@RequestParam(value = "vendorName", required = false) String theVendorName,
			@RequestParam(value = "attnName", required = false) Integer theAttn,
			@RequestParam(value = "tagName", required = false) String theTag,
			@RequestParam(value = "manufactName", required = false) Integer theManufacture,
			@RequestParam(value = "toBillAddress", required = false) Integer thebilltoAddress,
			@RequestParam(value = "toShipAddress", required = false) Integer theShiptoAddress,
			@RequestParam(value = "orderhiddenName", required = false) String theOrderHidden,
			@RequestParam(value = "poDateName", required = false) String thePODate,
			@RequestParam(value = "frieghtChangesName", required = false) Integer theFrieghtChange,
			@RequestParam(value = "customerName", required = false) String theCustomerPOName,
			@RequestParam(value = "orderhiddenName", required = false) Integer theOrderedBy,
			@RequestParam(value = "shipViaName", required = false) Integer theShipViaName,
			@RequestParam(value = "ourPoName", required = false) String theOurPOName,
			@RequestParam(value = "wantedName", required = false) String theWantedName,
			@RequestParam(value = "wantedCombo", required = false) Integer theWantedOnOrBefore,
			@RequestParam(value = "specialInstructionName", required = false) String theSpecialInst,
			@RequestParam(value = "qbId", required = false) String theQBId,
			@RequestParam(value = "subtotalGeneralName", required = false) BigDecimal theSubtotal,
			@RequestParam(value = "freightGeneralName", required = false) BigDecimal theFreight,
			@RequestParam(value = "taxGeneralName", required = false) BigDecimal theTotalGemnrel,
			@RequestParam(value = "generalName", required = false) BigDecimal theGenerelName,
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "vefactoryID", required = false) Integer theVefactoryID,
			@RequestParam(value = "manufaturer", required = false) Integer theManufaturerID,
			@RequestParam(value = "jorelease", required = false) Integer thejoReleaseId,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "TaxTotal", required = false) BigDecimal thetaxTotal,
			@RequestParam(value = "locationbillToAddressID", required = false) String theAddresName,
			@RequestParam(value = "locationbillToAddress1", required = false) String theAddres1,
			@RequestParam(value = "locationbillToAddress2", required = false) String theAddres2,
			@RequestParam(value = "locationbillToCity", required = false) String theCity,
			@RequestParam(value = "locationbillToState", required = false) String theState,
			@RequestParam(value = "locationbillToZip", required = false) String theZip,
			@RequestParam(value = "locationShipToAddressID", required = false) String theShipAddresName,
			@RequestParam(value = "locationShipToAddress1", required = false) String theShipAddres1,
			@RequestParam(value = "locationShipToAddress2", required = false) String theShipAddres2,
			@RequestParam(value = "locationShipToCity", required = false) String theShipCity,
			@RequestParam(value = "locationShipToState", required = false) String theShipState,
			@RequestParam(value = "locationShipToZip", required = false) String theShipZip,
			@RequestParam(value = "emailTimeStamp", required = false) String theEmailTimeStamp,
			@RequestParam(value = "customerShipToName", required = false) Integer theCustomerShipToName,
			@RequestParam(value = "customerBillName", required = false) Integer theCustomerBillName,
			@RequestParam(value = "transactionStatus", required = false) Integer transactionStatus,
			@RequestParam(value = "prWarehouseID", required = false) Integer prWarehouseID,
			@RequestParam(value = "customerID", required = false) Integer customerID,
			@RequestParam(value = "commissionSplitGridData",required = false) String commissionSplitGridData,
			@RequestParam(value = "commissionSplitdelData[]",required = false) ArrayList<String>  commissionSplitDelData,
			
			HttpSession session,HttpServletRequest therequest, ModelMap theModel,
			HttpServletResponse theResponse) throws JobException, IOException,
			ParseException, VendorException, MessagingException {
		Vepo aVepo = new Vepo();
		Vepo aVepo2 = new Vepo();
		Rxaddress aRxaddress = new Rxaddress();
		Rxaddress aRxaddressShipTo = new Rxaddress();
		JoRelease aJoRelease = new JoRelease();
		Ecsplitjob ecsplitjob = null;
		Boolean saved = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		logger.info("1.shiptoindex"+theShiptoAddress);
		logger.info("2.shiptoMode"+theShiptoAddress);
		logger.info("3.shiptoRxAddress"+theShiptoAddress);
		logger.info("Contact ID:"+theAttn);
		logger.info(theShipAddresName+"===="+aVepo.getShipTo());
		logger.info("Transaction Status JobSubmittal Contrl::"+transactionStatus);
		
		try {
			aVepo.setTag(theTag);
			aVepo.setRxVendorContactId(theAttn);
			aVepo.setRxVendorId(theManufacture);
			aVepo.setJoReleaseId(thejoReleaseId);
			aVepo.setRxBillToId(theManufaturerID);
			aVepo.setRxShipToId(theManufaturerID);
			//aVepo.setRxVendorContactId(contactId);
			if (theQBId != null && !theQBId.equals("")) {
				aVepo.setQbPO(theQBId);
			}
			if (theOrderHidden != null && !theOrderHidden.equals("")) {
				aVepo.setCreatedById(Integer.valueOf(theOrderHidden));
				aVepo.setOrderedById(Integer.valueOf(theOrderHidden));
			}
			aVepo.setVeFactoryId(theVefactoryID);
			if (theOrderedBy == 0) {
				if (aUserBean != null) {
					aVepo.setOrderedById(aUserBean.getUserId());
				}
			} else {
				aVepo.setOrderedById(theOrderedBy);
			}
			aVepo.setPonumber(theOurPOName);
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String createdDate = df.format(now);
			if ((createdDate != null && createdDate != "")) {
				aVepo.setCreatedOn(DateUtils.parseDate(createdDate,
						new String[] { "MM/dd/yyyy" }));
			}
			if (thePODate != null && thePODate != "") {
				aVepo.setOrderDate(DateUtils.parseDate(thePODate.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			if (theEmailTimeStamp != null && theEmailTimeStamp != "") {
				
				if(theEmailTimeStamp.toLowerCase().contains("am")||theEmailTimeStamp.toLowerCase().contains("pm"))
				aVepo.setEmailTimeStamp(DateUtils.parseDate(theEmailTimeStamp,new String[] { "MM/dd/yyyy hh:mm a" }));
				else
				aVepo.setEmailTimeStamp(DateUtils.parseDate(theEmailTimeStamp,new String[] { "MM/dd/yyyy hh:mm " }));	
			}
			aVepo.setVeShipViaId(theShipViaName);
			aVepo.setVeFreightChargesId(theFrieghtChange);
			aVepo.setCustomerPonumber(theCustomerPOName);
			aVepo.setDateWanted(theWantedName);
			aVepo.setWantedOnOrBefore(theWantedOnOrBefore);
			aVepo.setSpecialInstructions(theSpecialInst);
			aVepo.setFreight(theFreight);
			aVepo.setTaxRate(theTotalGemnrel);
			aVepo.setSubtotal(theSubtotal);
			aVepo.setTaxTotal(thetaxTotal);
			aVepo.setVePoid(jobService.getVepoId(thejoReleaseId));
			if(aVepo.getVePoid()!=null && aVepo.getVePoid()>0){
			Vepo aVePO = itsVendorService.getVePo(aVepo.getVePoid());
			aVepo.setBillToIndex(aVePO.getBillToIndex());
			aVepo.setShipTo(aVePO.getShipTo());
			aVepo.setShipToMode(aVePO.getShipToMode());
			}
			aVepo.setTransactionStatus(transactionStatus);
			aVepo.setRxShipToAddressId(theCustomerShipToName);
			aVepo.setPrWarehouseId(prWarehouseID);
			
			if (aVepo.getBillToIndex() != null && aVepo.getBillToIndex() == 2) {
				aRxaddress.setAddress1(theAddres1);
				aRxaddress.setAddress2(theAddres2);
				aRxaddress.setCity(theCity);
				aRxaddress.setState(theState);
				aRxaddress.setZip(theZip);
				aRxaddress.setIsBillTo(false);
				aRxaddress.setInActive(false);
				aRxaddress.setIsMailing(false);
				aRxaddress.setIsStreet(false);
				aRxaddress.setIsShipTo(false);
				aRxaddress.setOtherBillTo(2);
				aRxaddress.setRxMasterId(theCustomerBillName);
				aRxaddress.setName(theAddresName);
				aVepo.setRxBillToAddressId(theCustomerBillName);
			}
			if (aVepo.getShipToMode() == 3) {
				aRxaddressShipTo.setAddress1(theShipAddres1);
				aRxaddressShipTo.setAddress2(theShipAddres2);
				aRxaddressShipTo.setCity(theShipCity);
				aRxaddressShipTo.setState(theShipState);
				aRxaddressShipTo.setIsBillTo(false);
				aRxaddressShipTo.setInActive(false);
				aRxaddressShipTo.setIsMailing(false);
				aRxaddressShipTo.setIsStreet(false);
				aRxaddressShipTo.setIsShipTo(false);
				aRxaddressShipTo.setOtherShipTo(3);
				aRxaddressShipTo.setZip(theShipZip);
				aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
				aRxaddressShipTo.setName(theShipAddresName);
				aVepo.setRxShipToAddressId(theCustomerShipToName);
			}
			
			aVepo.setJobCustomerID(customerID);
			aJoRelease.setJoReleaseId(thejoReleaseId);
			aVepo2 = jobService.updatePOGeneral(aVepo, aRxaddress,
					aRxaddressShipTo, aJoRelease);
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
					logger.info("JoMaster ## joReleaseID ==>"+joMasterID +" ## "+thejoReleaseId);
					ecsplitjob.setJoReleaseID(thejoReleaseId);
					ecsplitjob.setJoMasterId(joMasterID);
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
		} catch (JobException e) {
			sendTransactionException("<b>thejoReleaseId:</b>"+thejoReleaseId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		logger.info("*********** Submittal updated ***********");
		return aVepo2;
	}
	/*
	 * Created By :  Created Date : 02/05/2014 Method Use : To save the PO
	 * order Input : PO request values Output : PO values will be stored
	 * successfully into the Database. Exception : JobException
	 */
	@RequestMapping(value = "/addPO", method = RequestMethod.POST)
	public @ResponseBody
	Vepo addPO(
			@RequestParam(value = "vendorName", required = false) String theVendorName,
			@RequestParam(value = "attnName", required = false) Integer theAttn,
			@RequestParam(value = "tagName", required = false) String theTag,
			@RequestParam(value = "manufactName", required = false) Integer theManufacture,			
			@RequestParam(value = "orderhiddenName", required = false) String theOrderHidden,
			@RequestParam(value = "poDateName", required = false) String thePODate,
			@RequestParam(value = "frieghtChangesName", required = false) Integer theFrieghtChange,
			@RequestParam(value = "customerName", required = false) String theCustomerPOName,
			@RequestParam(value = "orderName", required = false) Integer theOrderedBy,
			@RequestParam(value = "shipViaName", required = false) Integer theShipViaName,
			@RequestParam(value = "ourPoName", required = false) String theOurPOName,
			@RequestParam(value = "wantedName", required = false) String theWantedName,			
			@RequestParam(value = "specialInstructionName", required = false) String theSpecialInst,			
			@RequestParam(value = "subtotalGeneralName", required = false) BigDecimal theSubtotal,
			@RequestParam(value = "freightGeneralName", required = false) BigDecimal theFreight,			
			@RequestParam(value = "manufaturer", required = false) Integer theManufaturerID,
			@RequestParam(value = "jorelease", required = false) Integer thejoReleaseId,
			@RequestParam(value = "TaxTotal", required = false) BigDecimal thetaxTotal,
			@RequestParam(value = "customerBillToOtherID", required = false) String customerBillToOtherID,
			@RequestParam(value = "customerShipToOtherID", required = false) String customerShipToOtherID,
			@RequestParam(value = "locationbillToAddressID", required = false) String theAddresName,
			@RequestParam(value = "locationbillToAddress1", required = false) String theAddres1,
			@RequestParam(value = "locationbillToAddress2", required = false) String theAddres2,
			@RequestParam(value = "locationbillToCity", required = false) String theCity,
			@RequestParam(value = "locationbillToState", required = false) String theState,
			@RequestParam(value = "locationbillToZip", required = false) String theZip,
			@RequestParam(value = "locationShipToAddressID", required = false) String theShipAddresName,
			@RequestParam(value = "locationShipToAddress1", required = false) String theShipAddres1,
			@RequestParam(value = "locationShipToAddress2", required = false) String theShipAddres2,
			@RequestParam(value = "locationShipToCity", required = false) String theShipCity,
			@RequestParam(value = "locationShipToState", required = false) String theShipState,
			@RequestParam(value = "locationShipToZip", required = false) String theShipZip,			
			@RequestParam(value = "customerShipToName", required = false) Integer theCustomerShipToName,
			@RequestParam(value = "customerBillName", required = false) Integer theCustomerBillName,
			@RequestParam(value = "gridData", required = false) String gridData,
			@RequestParam(value = "buttonId", required = false) String buttonId,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			@RequestParam(value = "manfactureID", required = false) Integer therxID,
			@RequestParam(value = "taxPerc", required = false) BigDecimal thetaxPerc,
			@RequestParam(value = "customerBillToAddressID", required = false) Integer theCustomerBillToAddressID,
			@RequestParam(value = "customerShipToAddressID", required = false) Integer theCustomerShipToAddressID,
			@RequestParam(value = "prWarehouseID", required = false) Integer prWarehouseID,
			@RequestParam(value= "ourpo", required=false) String ourpo,
			@RequestParam(value= "transactionStatus", required=false) Integer transactionStatus,
			@RequestParam(value= "shiptomodeName", required=false) byte shiptomode,
			 ModelMap theModel,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException,
			ParseException, VendorException, MessagingException {	
		
		logger.info("Ship to Mode: "+shiptomode);
		logger.info("Shipto: "+customerShipToOtherID);

		Vepo aVepo = new Vepo();
		Vepo aVepo2 = new Vepo();
		Rxaddress aRxaddress = new Rxaddress();
		Rxaddress aRxaddressShipTo = new Rxaddress();
		JoRelease aJoRelease = new JoRelease();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (null != theTag) {
				aVepo.setTag(theTag);
			}
			if (null != theAttn && theAttn > -1) {
				aVepo.setRxVendorContactId(theAttn);
			}
			if (null != theManufacture && theManufacture > -1) {
				aVepo.setRxVendorId(theManufacture);
			} else {
				aVepo.setRxVendorId(therxID);
			}
			if (null != thejoReleaseId && thejoReleaseId > 0) {
				aVepo.setJoReleaseId(thejoReleaseId);
			}
			if (null != theManufaturerID && theManufaturerID > 0) {
				aVepo.setRxBillToId(theManufaturerID);
				aVepo.setRxShipToId(theManufaturerID);
			}

			String theQBId = "Test";
			if (theQBId != null && !theQBId.equals("")) {
				aVepo.setQbPO(theQBId);
			}
			if (theOrderHidden != "" && theOrderHidden != null) {
				aVepo.setCreatedById(Integer.valueOf(theOrderHidden));
			}
			Integer theVefactoryID = 1;
			aVepo.setVeFactoryId(theVefactoryID);
			if (theOrderedBy == 0) {
				if (aUserBean != null) {
					aVepo.setOrderedById(aUserBean.getUserId());
				}
			} else {
				aVepo.setOrderedById(theOrderedBy);
			}
			if (null != theOurPOName && theOurPOName.trim().length() > 0) {
				aVepo.setPonumber(theOurPOName);
			}
			if (prWarehouseID != null) {
				if ("Us".equalsIgnoreCase(customerBillToOtherID)) {
					aVepo.setPrWarehouseId(prWarehouseID);
					aVepo.setRxShipToAddressId(prWarehouseID);
				}
			}

			
			
			if (thePODate != null && thePODate != "") {
				aVepo.setOrderDate(DateUtils.parseDate(thePODate.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			/*
			 * String theEmailTimeStamp = "05/15/2014"; if(theEmailTimeStamp !=
			 * null && theEmailTimeStamp != ""){
			 * aVepo.setEmailTimeStamp(DateUtils.parseDate(theEmailTimeStamp,
			 * new String[]{"MM/dd/yyyy"})); }
			 */
			if(transactionStatus!=null){
				aVepo.setTransactionStatus(transactionStatus);
			}
			else{
				aVepo.setTransactionStatus(1);
			}
			
			aVepo.setVeShipViaId(theShipViaName);
			aVepo.setVeFreightChargesId(theFrieghtChange);
			aVepo.setCustomerPonumber(theCustomerPOName);
			aVepo.setDateWanted(theWantedName);
			// aVepo.setWantedOnOrBefore(theWantedOnOrBefore);
			aVepo.setSpecialInstructions(theSpecialInst);
			aVepo.setFreight(theFreight);
			aVepo.setShipToMode(shiptomode);
			// BigDecimal theTotalGemnrel = new BigDecimal(12);
			aVepo.setTaxRate(thetaxPerc);
			aVepo.setSubtotal(theSubtotal);
			aVepo.setTaxTotal(thetaxTotal);
			// aVepo.setVePoid(jobService.getVepoId(thejoReleaseId));
			aVepo.setConsignment(true);
			aVepo.setMultipleAcks(true);
			if (null == theCustomerBillName) {
				if (null != theCustomerBillToAddressID) {
					aVepo.setRxBillToId(theCustomerBillName);
					aVepo.setRxBillToAddressId(theCustomerBillToAddressID);
					
					
				}
				if ("Customer".equalsIgnoreCase(customerBillToOtherID)) {
					aVepo.setBillToIndex(1);
				} else {
					// aVepo.setRxBillToId(0);
					aVepo.setBillToIndex(0);
				}

			} else {
				aVepo.setRxBillToId(theCustomerBillName);
				aVepo.setRxBillToAddressId(theCustomerBillToAddressID);

				if ("Customer".equalsIgnoreCase(customerBillToOtherID)) {
					aVepo.setBillToIndex(1);
				}
			}
			if (null == theCustomerShipToName) {
				if (null != theCustomerShipToAddressID) {
					aVepo.setRxShipToId(theCustomerShipToName);
					aVepo.setRxShipToAddressId(theCustomerShipToAddressID);
		
				}
				if ("Customer".equalsIgnoreCase(customerShipToOtherID)) {
					aVepo.setShipTo(1);
				} else {
					// aVepo.setRxShipToId(0);
					aVepo.setShipTo(0);
				}

			} else {
				aVepo.setRxShipToId(theCustomerShipToName);
				aVepo.setRxShipToAddressId(theCustomerShipToAddressID);
				aVepo.setShipTo(0);
				if ("Customer".equalsIgnoreCase(customerShipToOtherID)) {
					aVepo.setShipTo(1);
				}
			}

			/*
			 * Vepo aVePO = itsVendorService.getVePo(aVepo.getVePoid());
			 * aVepo.setBillToIndex(aVePO.getBillToIndex());
			 * aVepo.setShipTo(aVePO.getShipTo());
			 */
			aVepo.setRxShipToAddressId(prWarehouseID);
			//aVepo.setPrWarehouseId(prWarehouseID);
			
			if ("Other".equalsIgnoreCase(customerBillToOtherID)) {

				aRxaddress.setFax(customerBillToOtherID);
				aRxaddress.setAddress1(theAddres1);
				aRxaddress.setAddress2(theAddres2);
				aRxaddress.setCity(theCity);
				aRxaddress.setState(theState);
				aRxaddress.setZip(theZip);
				aRxaddress.setIsBillTo(false);
				aRxaddress.setInActive(false);
				aRxaddress.setIsMailing(false);
				aRxaddress.setIsStreet(false);
				aRxaddress.setIsShipTo(false);
				aRxaddress.setOtherBillTo(2);
				// aRxaddress.setRxMasterId(theCustomerBillName);
				aRxaddress.setName(theAddresName);
				aVepo.setRxBillToAddressId(theCustomerBillName);
				aVepo.setBillToIndex(2);
			}
			if ("Job site".equalsIgnoreCase(customerShipToOtherID)) {
				aRxaddressShipTo.setFax(customerShipToOtherID);
				aRxaddressShipTo.setAddress1(theShipAddres1);
				aRxaddressShipTo.setAddress2(theShipAddres2);
				aRxaddressShipTo.setCity(theShipCity);
				aRxaddressShipTo.setState(theShipState);
				aRxaddressShipTo.setIsBillTo(false);
				aRxaddressShipTo.setInActive(false);
				aRxaddressShipTo.setIsMailing(false);
				aRxaddressShipTo.setIsStreet(false);
				aRxaddressShipTo.setIsShipTo(false);
				aRxaddressShipTo.setOtherShipTo(3);
				aRxaddressShipTo.setZip(theShipZip);
				// aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
				aRxaddressShipTo.setName(theShipAddresName);
				aVepo.setRxShipToAddressId(theCustomerShipToAddressID);
				aVepo.setShipTo(3);
			}
			// aVepo.setShipToMode(aVePO.getShipToMode());
			if ("Other".equalsIgnoreCase(customerShipToOtherID) ) {
				aRxaddressShipTo.setFax(customerShipToOtherID);
				aRxaddressShipTo.setAddress1(theShipAddres1);
				aRxaddressShipTo.setAddress2(theShipAddres2);
				aRxaddressShipTo.setCity(theShipCity);
				aRxaddressShipTo.setState(theShipState);
				aRxaddressShipTo.setIsBillTo(false);
				aRxaddressShipTo.setInActive(false);
				aRxaddressShipTo.setIsMailing(false);
				aRxaddressShipTo.setIsStreet(false);
				aRxaddressShipTo.setIsShipTo(false);
				aRxaddressShipTo.setOtherShipTo(3);
				aRxaddressShipTo.setZip(theShipZip);
				// aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
				aRxaddressShipTo.setName(theShipAddresName);
				aVepo.setRxShipToAddressId(theCustomerShipToAddressID);
				aVepo.setShipTo(2);
			}
			if (null != vePOID) {
				aVepo.setVePoid(vePOID);
			}
			
			if(ourpo!=null){
				if(ourpo.length()>0){
					aVepo.setPonumber(ourpo);	
				}
			}
			aVepo2 = jobService.addPOGeneral(aVepo, aRxaddress,
					aRxaddressShipTo, aJoRelease);

			String result = null;
			JsonParser parser = new JsonParser();
			System.out.println("Grid Data----->" + gridData);
			Vepodetail objVepodetail = null;
			
			BigDecimal AmtforCalculating=new BigDecimal(0);
			BigDecimal taxAmountfromlineitem=new BigDecimal(0);
			
			List<Vepodetail> alVepodetails = new ArrayList<Vepodetail>();
			if (null != gridData) {
				JsonElement ele = parser.parse(gridData);
				if(ele.isJsonArray()){
			
				JsonArray array = ele.getAsJsonArray();

				for (JsonElement ele1 : array) {
					objVepodetail = new Vepodetail();
					JsonObject obj = ele1.getAsJsonObject();
					// objVepodetail.setNote(obj.get("note").toString());
					String sDesc = obj.get("description").getAsString();
					
					System.out.println("==>Test1:::"+sDesc);
					
//					sDesc = sDesc.replaceAll("^\"|\"$", "");
						
					System.out.println("==>Test2:::"+sDesc);
					
					objVepodetail.setDescription(sDesc);
					// objVepodetail.setDescription(obj.get("description").toString());
					
					System.out.println("------------>>"+obj.get("unitCost").getAsString());
					
					objVepodetail.setQuantityOrdered(obj.get("quantityOrdered")
							.getAsBigDecimal());
					objVepodetail.setQuantityReceived(obj.get("quantityReceived")
							.getAsBigDecimal());
					String sUnitCost = obj.get("unitCost").getAsString().replaceAll("[$,]", "");
					objVepodetail.setUnitCost(new BigDecimal(sUnitCost));
					objVepodetail.setPriceMultiplier(obj.get("priceMultiplier")
							.getAsBigDecimal());
					System.out.println("--------------------->"
							+ obj.get("taxable").getAsString());
					// String isTaxable = obj.get("taxable").getAsString();
					if ("Yes"
							.equalsIgnoreCase(obj.get("taxable").getAsString())
							|| "true".equalsIgnoreCase(obj.get("taxable")
									.getAsString())) {
						System.out.println("Inside True");
						objVepodetail.setTaxable(true);
						
						AmtforCalculating = objVepodetail.getQuantityOrdered().multiply(objVepodetail.getUnitCost()).multiply(objVepodetail.getPriceMultiplier());
						
						taxAmountfromlineitem = taxAmountfromlineitem.add(new BigDecimal(AmtforCalculating.floatValue()*(aVepo.getTaxRate().floatValue()/100)));
						
						System.out.println("::-->"+taxAmountfromlineitem);
					} else {
						objVepodetail.setTaxable(false);
						
					}

					// String sNetCast = obj.get("netCast").getAsString();
					// objVepodetail.setNetCast(new
					// BigDecimal(sNetCast.substring(1,sNetCast.length())));
					String sQuantityBilled = obj.get("quantityBilled")
							.getAsString();
					objVepodetail
							.setQuantityBilled(new BigDecimal(sQuantityBilled
									.substring(1, sQuantityBilled.length())
									.replace(",", "")));
					String ackDate = obj.get("ackDate").getAsString().trim();
					System.out.println("ackDate" + ackDate);
					if (null != ackDate
							&& (!ackDate.equalsIgnoreCase("undefined"))
							&& ackDate.replace("Â", "").trim().length() > 1) {
						objVepodetail.setAcknowledgedDate(DateUtils.parseDate(
								ackDate.trim(), new String[] { "MM/dd/yyyy" }));
					}

					String shipDate = obj.get("shipDate").getAsString().trim();
					if (null != shipDate
							&& (!shipDate.equalsIgnoreCase("undefined"))
							&& shipDate.replace("Â", "").trim().length() > 1) {
						objVepodetail
								.setEstimatedShipDate(DateUtils.parseDate(
										shipDate.trim(),
										new String[] { "MM/dd/yyyy" }));
					}

					// objVepodetail.setVePoid(vepoid);
					String sNotes = "";
					if(obj.get("notes").toString()==null){
						sNotes = obj.get("notesdesc").getAsString();
					}
					else{
						sNotes = obj.get("notes").getAsString();
					}
					//sNotes = sNotes.replaceAll("^\"|\"$", "");
					objVepodetail.setNote(sNotes.trim());
					// objVepodetail.setNotesDesc(sNotes);
					objVepodetail.setPrMasterId(obj.get("prMasterId")
							.getAsInt());
					objVepodetail.setVendorOrderNumber(obj.get(
							"vendorOrderNumber").getAsString());

					objVepodetail.setVePoid(aVepo2.getVePoid());
					/*
					 * objVepodetail.setPosistion(obj.get("posistion").getAsDouble
					 * ());
					 * objVepodetail.setTaxTotal(obj.get("taxTotal").getAsBigDecimal
					 * ());
					 * objVepodetail.setInLineNote(obj.get("inLineNote").toString
					 * ());
					 */
					alVepodetails.add(objVepodetail);

					// [{"note":"ARROWS-SF","description":"test","quantityOrdered":"4","unitCost":"$44.00","priceMultiplier":"4.0000","taxable":"Yes","netCast":"$0.00",
					// "quantityBilled":"$0.00","vePoid":"","prMasterId":"1580","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg3_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""},{"note":"RPSRC","description":"test","quantityOrdered":"2","unitCost":"$22.00","priceMultiplier":"2.0000","taxable":"No","netCast":"$0.00","quantityBilled":"$0.00","vePoid":"","prMasterId":"1574","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg2_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""},{"note":"PENNDX","description":"test","quantityOrdered":"3","unitCost":"$30.00","priceMultiplier":"3.0000","taxable":"Yes","netCast":"$0.00","quantityBilled":"$0.00","vePoid":"","prMasterId":"1552","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg1_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""}]

				}
				}
			} else {
				objVepodetail = new Vepodetail();
				objVepodetail.setVePoid(aVepo2.getVePoid());
				alVepodetails.add(objVepodetail);
			}
			// gridData = gridData.replaceAll("([a-zA-Z0-9])(\")([a-zA-Z0-9])",
			// "$1\\\\\\\\\"$3");

			try {
				jobService.addPOLineDetails(alVepodetails,taxAmountfromlineitem);
				aVepo2.setTaxTotal(taxAmountfromlineitem);
				
			} catch (JobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	/*		System.out.println("VEPOID us------------------------->"+ aVepo2.getVePoid());
			System.out.println("getRxBillToId us------------------------->"+ aVepo2.getRxBillToId());
			System.out.println("getRxShipToId us------------------------->"+ aVepo2.getRxShipToId());
			System.out.println("getRxBillToAddressId us------------------------->"+ aVepo2.getRxBillToAddressId());
			System.out.println("getRxShipToAddressId us------------------------->"+ aVepo2.getRxShipToAddressId());
			System.out.println("taxTotal us------------------------->"+ taxAmountfromlineitem);*/
		} catch (Exception /* JobException */e) {
			sendTransactionException("<b>theVendorName:</b>"+theVendorName,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			// theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		logger.info("*********** Submittal updated ***********");

		return aVepo2;
	}

	@RequestMapping(value = "/addPOLineDetails", method = RequestMethod.POST)
	public @ResponseBody
	String addPOLineDetails(
			@RequestParam(value = "gridData", required = true) String gridData,
			@RequestParam(value = "vepoid", required = true) Integer vepoid,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		String result = null;
		JsonParser parser = new JsonParser();
		JsonElement ele = parser.parse(gridData);
		JsonArray array = ele.getAsJsonArray();

		List<Vepodetail> alVepodetails = new ArrayList<Vepodetail>();
		for (JsonElement ele1 : array) {
			Vepodetail objVepodetail = new Vepodetail();
			JsonObject obj = ele1.getAsJsonObject();
			objVepodetail.setNote(obj.get("note").toString().trim());
			objVepodetail.setDescription(obj.get("description").toString());
			objVepodetail.setQuantityOrdered(obj.get("quantityOrdered")
					.getAsBigDecimal());
			String sUnitCost = obj.get("unitCost").getAsString();
			objVepodetail.setUnitCost(new BigDecimal(sUnitCost.substring(1,
					sUnitCost.length())));
			objVepodetail.setPriceMultiplier(obj.get("priceMultiplier")
					.getAsBigDecimal());
			objVepodetail.setTaxable(obj.get("taxable").getAsBoolean());
			String sNetCast = obj.get("netCast").getAsString();
			objVepodetail.setNetCast(new BigDecimal(sNetCast.substring(1,
					sNetCast.length())));
			String sQuantityBilled = obj.get("quantityBilled").getAsString();
			objVepodetail.setQuantityBilled(new BigDecimal(sQuantityBilled
					.substring(1, sQuantityBilled.length())));
			objVepodetail.setVePoid(vepoid);
			objVepodetail.setPrMasterId(obj.get("prMasterId").getAsInt());
			/*
			 * objVepodetail.setPosistion(obj.get("posistion").getAsDouble());
			 * objVepodetail.setTaxTotal(obj.get("taxTotal").getAsBigDecimal());
			 * objVepodetail.setInLineNote(obj.get("inLineNote").toString());
			 */
			alVepodetails.add(objVepodetail);

			// [{"note":"ARROWS-SF","description":"test","quantityOrdered":"4","unitCost":"$44.00","priceMultiplier":"4.0000","taxable":"Yes","netCast":"$0.00",
			// "quantityBilled":"$0.00","vePoid":"","prMasterId":"1580","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg3_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""},{"note":"RPSRC","description":"test","quantityOrdered":"2","unitCost":"$22.00","priceMultiplier":"2.0000","taxable":"No","netCast":"$0.00","quantityBilled":"$0.00","vePoid":"","prMasterId":"1574","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg2_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""},{"note":"PENNDX","description":"test","quantityOrdered":"3","unitCost":"$30.00","priceMultiplier":"3.0000","taxable":"Yes","netCast":"$0.00","quantityBilled":"$0.00","vePoid":"","prMasterId":"1552","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg1_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""}]

		}
		try {
			
			BigDecimal taxAmt = new BigDecimal(0);
			
			result = jobService.addPOLineDetails(alVepodetails,taxAmt);
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>vepoid:</b>"+vepoid,"JOB",e,session,therequest);
		}
		return result;
	}

	@RequestMapping(value = "/updateSubmittalform", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateSubmittalForm(
			@RequestParam(value = "sentCheck", required = false) boolean theSent,
			@RequestParam(value = "sentDate", required = false) String theSentDate,
			@RequestParam(value = "resentCheck", required = false) boolean theResent,
			@RequestParam(value = "resentDate", required = false) String theResentDate,
			@RequestParam(value = "approvedCheck", required = false) boolean theApproved,
			@RequestParam(value = "approvedDate", required = false) String theApprovedDate,
			@RequestParam(value = "manualSentCheck", required = false) boolean theManualSent,
			@RequestParam(value = "manualSentDate", required = false) String theManualSentDate,
			@RequestParam(value = "requestManualCheck", required = false) boolean theManualRequest,
			@RequestParam(value = "manualRequestDate", required = false) String theManualRequestDate,
			@RequestParam(value = "manualQty", required = false) Integer theManualQty,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			@RequestParam(value = "jobName", required = false) String thejobName,
			@RequestParam(value = "jobNumber", required = false) String thejobNumber,
			 HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest)
			throws ParseException, IOException, MessagingException {
		Jomaster aJomaster = new Jomaster();
		try {
			aJomaster.setJoMasterId(theJoMasterID);
			aJomaster.setDescription(thejobName);
			aJomaster.setJobNumber(thejobNumber);
			aJomaster.setSubmittalSent(theSent);
			if (theSentDate != null && !theSentDate.equalsIgnoreCase("")) {
				Date aSentDate = new Date(theSentDate);
				aJomaster.setSubmittalSentDate(aSentDate);
			}
			if (theResentDate != null && !theResentDate.equalsIgnoreCase("")) {
				aJomaster.setSubmittalResent(theResent);
				Date aResentDate = new Date(theResentDate);
				aJomaster.setSubmittalResentDate(aResentDate);
			}
			if (theApprovedDate != null
					&& !theApprovedDate.equalsIgnoreCase("")) {
				aJomaster.setSubmittalApproved(theApproved);
				Date aApprovedDate = new Date(theApprovedDate);
				aJomaster.setSubmittalApprovedDate(aApprovedDate);
			}
			aJomaster.setManualQty(theManualQty);
			aJomaster.setManualSent(theManualSent);
			if (theManualSentDate != null
					&& !theManualSentDate.equalsIgnoreCase("")) {
				Date amanualSentDate = new Date(theManualSentDate);
				aJomaster.setManualDate(amanualSentDate);
			}
			if (theManualRequestDate != null
					&& !theManualRequestDate.equalsIgnoreCase("")) {
				aJomaster.setRequestManual(theManualRequest);
				Date aReqDate = new Date(theManualRequestDate);
				aJomaster.setRequestDate(aReqDate);
			}
			jobService.updateJobSubmittal(aJomaster);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theJoMasterID:</b>"+theJoMasterID,"JOB",e,session,therequest);
		}
		return 0;
	}

	/*
	 * @RequestMapping(value="/getDefaultWarehouseAddress", method =
	 * RequestMethod.GET) public @ResponseBody Cumaster
	 * getDefaultWarehouseAddress(@RequestParam(value="customerID",
	 * required=false) Integer theProductName) { Cumaster aCumaster = null; try
	 * { if(theProductName != null){ aCumaster = new Cumaster(); aCumaster =
	 * jobService.getCuMasterDefaultWarehouse(theProductName); } } catch
	 * (Exception e) { logger.error(e.getMessage(),e); } return aCumaster; }
	 */

	@RequestMapping(value = "/getBilltoAddress", method = RequestMethod.GET)
	public @ResponseBody
	Rxaddress getBilltoAddress(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			@RequestParam(value = "oper", required = false) String theOper,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		Rxaddress aRxaddress = null;
		System.out.println("PrMasterID ---------->"+therxMasterId+"==="+theOper);
		try {
			if (therxMasterId != null) {
				aRxaddress = new Rxaddress();
				aRxaddress = jobService.getRxMasterBillAddress(therxMasterId,
						theOper);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"JOB",e,session,therequest);
		}
		return aRxaddress;
	}
	
	@RequestMapping(value = "/getJobAddress", method = RequestMethod.GET)
	public @ResponseBody
	Jomaster getJobAddress(
			@RequestParam(value = "customerID", required = false) Integer therxMasterId,
			@RequestParam(value = "oper", required = false) String theOper,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		Jomaster aJomaster = null;
		Rxmaster aRxmaster = null;
		System.out.println("customerID ---------->"+therxMasterId);
		try {
			if (therxMasterId != null) {
				aJomaster = new Jomaster();
				aJomaster = jobService.getSingleJobDetails(therxMasterId);
				aRxmaster = new Rxmaster();
				if(aJomaster.getRxCustomerId()!=null && aJomaster.getRxCustomerId()>0){
				aRxmaster = jobService.getSingleRxMasterDetails(aJomaster.getRxCustomerId());
				aJomaster.setCustomerPonumber(aRxmaster.getName());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId,"JOB",e,session,therequest);
		}
		return aJomaster;
	}

	@RequestMapping(value = "/updateBillToAndShipToSetting", method = RequestMethod.GET)
	public @ResponseBody
	Vepo updateBillToAndShipToSetting(
			@RequestParam(value = "vePOID", required = false) Integer theVePOId,
			@RequestParam(value = "isAddressID", required = false) Integer theAddressID,
			@RequestParam(value = "updaetKey", required = false) String theUpdateKey,
			@RequestParam(value = "rxCustomerID", required = false) Integer isshiptoCustomerID,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException{
		Vepo aVepo = new Vepo();
		try {
			if (theUpdateKey.equalsIgnoreCase("billTo")) {
				aVepo.setBillToIndex(theAddressID);
			}
			else if (theUpdateKey.equalsIgnoreCase("shipTo")) {
				
				System.out.println(isshiptoCustomerID+"-------------->>>>>++"+theAddressID);
				aVepo.setShipTo(0);
				byte shptomode = 2;
				aVepo.setShipToMode(shptomode);
				aVepo.setRxShipToId(isshiptoCustomerID);
				aVepo.setRxShipToAddressId(theAddressID);
			}
			else if (theUpdateKey.equalsIgnoreCase("shipToUSID")) {
				
				System.out.println(isshiptoCustomerID+"-------------->>>>>++"+theAddressID);
				aVepo.setShipTo(0);
				byte shptomode = 0;
				aVepo.setShipToMode(shptomode);
				aVepo.setRxShipToId(isshiptoCustomerID);
				aVepo.setRxShipToAddressId(theAddressID);
			}
			
			else if (theUpdateKey.equalsIgnoreCase("shipToCustomer")) {
				
				System.out.println(isshiptoCustomerID+"-------------->>>>>++"+theAddressID);
				aVepo.setShipTo(0);
				byte shptomode = 1;
				aVepo.setShipToMode(shptomode);
				aVepo.setRxShipToId(isshiptoCustomerID);
				aVepo.setRxShipToAddressId(theAddressID);
			}
			else if(theUpdateKey.equalsIgnoreCase("shipToOther"))
			{
				aVepo.setShipTo(0);
				byte shptomode = 3;
				aVepo.setShipToMode(shptomode);
				aVepo.setRxShipToId(isshiptoCustomerID);
				aVepo.setRxShipToAddressId(theAddressID);
			}
			
			aVepo.setUpdateKey(theUpdateKey);
			aVepo.setVePoid(theVePOId);
			jobService.updateBillToAndShipToSetting(aVepo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>theVePOId:</b>"+theVePOId,"JOB",e,session,therequest);
		}
		return aVepo;
	}

	@RequestMapping(value = "/updateShipToValue", method = RequestMethod.GET)
	public @ResponseBody
	Vepo updateShipToValue(
			@RequestParam(value = "vePOID", required = false) Integer theVePOId,
			@RequestParam(value = "shiptoindex", required = false) Integer shiptoindex,
			@RequestParam(value = "operation", required = false) String operation,
			@RequestParam(value = "shiptoMode", required = false) byte shiptoMode,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException
		{
		Vepo aVepo = new Vepo();
		try {
			
			
			aVepo.setShipToMode(shiptoMode);
			
			if(shiptoMode==0)
			aVepo.setUpdateKey("shipToUSID");
			else
			aVepo.setUpdateKey("shipToCustomer");	
			
			
			if(operation.equals("Add"))
			{
			aVepo.setShipTo(shiptoindex);
			}
			else
			{
			aVepo.setShipTo(-shiptoindex);	
			}
			
			
			aVepo.setVePoid(theVePOId);
			aVepo = jobService.updateBillToAndShipToSetting(aVepo);
			
			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>theVePOId:</b>"+theVePOId,"JOB",e,session,therequest);
		}
		System.out.println("====tt=---"+aVepo.getShipTo()+"::WarehouseID::"+aVepo.getPrWarehouseId());
		return aVepo;
	}

	@RequestMapping(value = "/updateEmailStampValue", method = RequestMethod.GET)
	public @ResponseBody
	Vepo updateEmailStampValue(
			@RequestParam(value = "vePOID", required = false) Integer theVePOId,
			@RequestParam(value = "purcheaseDate", required = false) String thePurcheaseDate,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		Vepo aVepo = new Vepo();
		logger.info("Date Is Null:"+thePurcheaseDate);
		try {
			if(theVePOId!=null && thePurcheaseDate!=null){
			/*aVepo.setEmailTimeStamp(DateUtils.parseDate(thePurcheaseDate,
					new String[] { "MM/dd/yyyy hh:mm a" }));*/
			aVepo.setEmailTimeStamp(new Timestamp(new Date().getTime()));
			aVepo.setVePoid(theVePOId);
			jobService.updateEmailStampValue(aVepo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>theVePOId:</b>"+theVePOId,"JOB",e,session,therequest);
		}
		return aVepo;
	}
	

	@RequestMapping(value = "/updateSOEmailTimeStamp", method = RequestMethod.GET)
	public @ResponseBody
	Cuso updateSOEmailTimeStamp(
			@RequestParam(value = "coSoID", required = false) Integer cuSoid,
			@RequestParam(value = "purcheaseDate", required = false) String thePurcheaseDate,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		Cuso aCuso = new Cuso();
		logger.info("CuSoID: "+cuSoid);
		logger.info("Date: "+thePurcheaseDate);
		try {
			aCuso.setEmailTimeStamp(DateUtils.parseDate(thePurcheaseDate,
					new String[] { "MM/dd/yyyy hh:mm a" }));
			aCuso.setCuSoid(cuSoid);
			jobService.updateSOEmailTimeStamp(aCuso);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>cuSoid:</b>"+cuSoid,"JOB",e,session,therequest);
		}
		return aCuso;
	}

	@RequestMapping(value = "printsubmittalCover", method = RequestMethod.GET)
	public @ResponseBody
	void printSubmittalCover(
			@RequestParam(value = "joSubmittalHeaderID", required = false) Integer joSubmittalHeaderID,
			 HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws SQLException, IOException, MessagingException {

		// @RequestParam(value="joSubmittalHeaderID", required=false) Integer
		// joSubmittalHeaderID
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = therequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/submittal_cover.jrxml");
			con = itspdfService.connectionForJasper();
			params.put("joSubmittalHeaderID", joSubmittalHeaderID);
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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>joSubmittalHeaderID:</b>"+joSubmittalHeaderID,"JOB",e,session,therequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}

	}

	@RequestMapping(value = "/createQuickBooks", method = RequestMethod.GET)
	public @ResponseBody
	String createQuickBooks(HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		String number = "";
		Random rand = new Random();
		try {
			number = "QB" + (rand.nextInt(999999999));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>createQuickBooks</b>","JOB",e,session,therequest);
		}
		return number;
	}

	@RequestMapping(value = "/getVepodetailList", method = RequestMethod.GET)
	public @ResponseBody	
	List<Vepodetail> getPOItemList(
			@RequestParam(value = "vePOID", required = false) Integer theVePOId,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		List<Vepodetail> theVepodetailList = new ArrayList<Vepodetail>();
		try {
			theVepodetailList = itsQuickBooksService
					.getVepodetailList(theVePOId);
			if (theVepodetailList == null)
				return new ArrayList<Vepodetail>();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>theVePOId:</b>"+theVePOId,"JOB",e,session,therequest);
		}
		return theVepodetailList;
	}

	@RequestMapping(value = "/getItemCode", method = RequestMethod.GET)
	public @ResponseBody
	String getItemCode(
			@RequestParam(value = "prMasterId", required = false) Integer thePrMasterId,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException {
		String aItemCode = "";
		try {
			aItemCode = itsQuickBooksService.getItemCode(thePrMasterId);
			if (aItemCode == null)
				return "";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>thePrMasterId:</b>"+thePrMasterId,"JOB",e,session,therequest);
		}
		return aItemCode;
	}

	@RequestMapping(value = "/addPOLineItems", method = RequestMethod.POST)
	public @ResponseBody
	Vepodetail addPOLineItems(
			@RequestParam(value = "gridData", required = false) String gridData,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			 ModelMap theModel,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException,
			ParseException, VendorException, MessagingException {
		Vepo aVepo2 = new Vepo();
		Vepodetail objVepodetail = null;
		List<Vepodetail> alVepodetails = new ArrayList<Vepodetail>();
		JsonParser parser = new JsonParser();
		if (null != gridData && gridData.length() > 6) {
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();

			for (JsonElement ele1 : array) {
				objVepodetail = new Vepodetail();
				JsonObject obj = ele1.getAsJsonObject();
				// objVepodetail.setNote(obj.get("note").toString());
				String sDesc = obj.get("description").toString();
				sDesc = sDesc.replaceAll("^\"|\"$", "");
				objVepodetail.setDescription(sDesc);
				// objVepodetail.setDescription(obj.get("description").toString());
				objVepodetail.setQuantityOrdered(obj.get("quantityOrdered")
						.getAsBigDecimal());
				String sUnitCost = obj.get("unitCost").getAsString();
				objVepodetail.setUnitCost(new BigDecimal(sUnitCost.substring(1,
						sUnitCost.length())));
				objVepodetail.setPriceMultiplier(obj.get("priceMultiplier")
						.getAsBigDecimal());
				System.out.println("--------------------->"
						+ obj.get("taxable").getAsString());
				// String isTaxable = obj.get("taxable").getAsString();
				if ("Yes".equalsIgnoreCase(obj.get("taxable").getAsString())
						|| "true".equalsIgnoreCase(obj.get("taxable")
								.getAsString())) {
					System.out.println("Inside True");
					objVepodetail.setTaxable(true);
				} else {
					objVepodetail.setTaxable(false);
				}

				// String sNetCast = obj.get("netCast").getAsString();
				// objVepodetail.setNetCast(new
				// BigDecimal(sNetCast.substring(1,sNetCast.length())));
				String sQuantityBilled = obj.get("quantityBilled")
						.getAsString();
				System.out.println("sQuantityBilled " + sQuantityBilled);
				if (sQuantityBilled != null
						&& (!sQuantityBilled.equals("null"))) {

					objVepodetail
							.setQuantityBilled(new BigDecimal(sQuantityBilled
									.substring(1, sQuantityBilled.length())
									.replace(",", "")));
				}
				System.out.println("objVepodetail.getQuantityBilled()"
						+ objVepodetail.getQuantityBilled());
				String ackDate = obj.get("ackDate").getAsString().trim();
				System.out.println("ackDate" + ackDate);
				if (null != ackDate && (!ackDate.equals("null"))
						&& (!ackDate.equalsIgnoreCase("undefined"))
						&& ackDate.replace("Â", "").trim().length() > 1) {
					objVepodetail.setAcknowledgedDate(DateUtils.parseDate(
							ackDate.trim(), new String[] { "MM/dd/yyyy" }));
				}

				String shipDate = obj.get("shipDate").getAsString().trim();
				if (null != shipDate && (!shipDate.equals("null"))
						&& (!shipDate.equalsIgnoreCase("undefined"))
						&& shipDate.replace("Â", "").trim().length() > 1) {
					objVepodetail.setEstimatedShipDate(DateUtils.parseDate(
							shipDate.trim(), new String[] { "MM/dd/yyyy" }));
				}

				// objVepodetail.setVePoid(vepoid);
				String sNotes = obj.get("notesdesc").toString();
				sNotes = sNotes.replaceAll("^\"|\"$", "");
				objVepodetail.setNote(sNotes.trim());
				char ch = '"';
				System.out.println("quantityReceived :: "
						+ obj.get("received").getAsString()
								.replaceAll(ch + "", ""));
				String recQty = obj.get("received").getAsString()
						.replaceAll(ch + "", "");
				BigDecimal quantityReceived = new BigDecimal(0);
				if (!recQty.equals("")) {
					quantityReceived = new BigDecimal(obj.get("received")
							.getAsString().replaceAll(ch + "", ""));
				}
				System.out.println("isAddNew :: " + obj.get("isAddNew"));

				objVepodetail.setQuantityReceived(quantityReceived);
				// objVepodetail.setNotesDesc(sNotes);
				objVepodetail.setPrMasterId(obj.get("prMasterId").getAsInt());
				objVepodetail.setVendorOrderNumber(obj.get("vendorOrderNumber")
						.getAsString());

				objVepodetail.setVePoid(vePOID);
				/*
				 * objVepodetail.setPosistion(obj.get("posistion").getAsDouble())
				 * ;
				 * objVepodetail.setTaxTotal(obj.get("taxTotal").getAsBigDecimal
				 * ());
				 * objVepodetail.setInLineNote(obj.get("inLineNote").toString
				 * ());
				 */
				alVepodetails.add(objVepodetail);

				// [{"note":"ARROWS-SF","description":"test","quantityOrdered":"4","unitCost":"$44.00","priceMultiplier":"4.0000","taxable":"Yes","netCast":"$0.00",
				// "quantityBilled":"$0.00","vePoid":"","prMasterId":"1580","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg3_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""},{"note":"RPSRC","description":"test","quantityOrdered":"2","unitCost":"$22.00","priceMultiplier":"2.0000","taxable":"No","netCast":"$0.00","quantityBilled":"$0.00","vePoid":"","prMasterId":"1574","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg2_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""},{"note":"PENNDX","description":"test","quantityOrdered":"3","unitCost":"$30.00","priceMultiplier":"3.0000","taxable":"Yes","netCast":"$0.00","quantityBilled":"$0.00","vePoid":"","prMasterId":"1552","vePodetailId":"","posistion":"","upAndDown":"<div><a id=\"jqg1_upIcon\" onclick=\"upPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/upArrowLineItem.png\" title=\"Move Up & Save\"></a><a id=\"NaN_downIcon\" onclick=\"downPOLineItem()\" style=\"padding: 2px;vertical-align: middle;\"><img src=\"./../resources/images/downArrowLineItem.png\" title=\"Move Down & Save\"></a><a onclick=\"inlineItem()\"><img src=\"./../resources/images/lineItem_new.png\" title=\"Line Items\" align=\"middle\" style=\"padding: 2px;vertical-align: middle;\"></a></div>","taxTotal":"","inLineNote":""}]

			}
		}
		try {
			
			BigDecimal taxAmt = new BigDecimal(0);
			
			jobService.addPOLineDetails(alVepodetails,taxAmt);
			
		} catch (JobException e) {
			e.printStackTrace();
			sendTransactionException("<b>vePOID:</b>"+vePOID,"JOB",e,session,therequest);
		}
		return alVepodetails.get(alVepodetails.size()-1);

	}
	
	@RequestMapping(value = "/getEmployeewithNameList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getEmployeewithNameList(
			@RequestParam("term") String theProductNameWithCode,
		HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) jobService
					.getEmployeewithNameList(theProductNameWithCode);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theProductNameWithCode:</b>"+theProductNameWithCode,"JOB",e,session,therequest);
		}
		return aProductCodeWithNameList;
	}
	
	@RequestMapping(value = "/manipulateSplitCommission", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse ManipulateSplitCommission(
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "tabpage", required = false) String tabpage,
			@RequestParam(value = "joReleaseID", required = false) Integer thejoReleaseId,
			@RequestParam(value = "commissionSplitGridData",required = false) String commissionSplitGridData,
			@RequestParam(value = "commissionSplitdelData[]",required = false) ArrayList<String>  commissionSplitDelData,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		Ecsplitjob ecsplitjob = new Ecsplitjob();
		UserBean aUserBean = null;
		boolean saved = false;
		Integer rxMasterID = null;
		try {
			aUserBean = new UserBean();
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			/*if (Oper.equalsIgnoreCase("del")) {
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
				aCusodetail.setQuantityOrdered(quantityOrder);
				aCusodetail.setQuantityBilled(new BigDecimal(0.00));
				if (Taxable != null) {
					if (Taxable.equalsIgnoreCase("on")) {
						aCusodetail.setTaxable((byte) 1);
					} else {
						aCusodetail.setTaxable((byte) 0);
					}
				}
			*/	
			
			logger.info("manipulatesplitcom==JoMasterID="+joMasterID+"==tabpage:"+tabpage+"-joreleaseid:"+thejoReleaseId+" Delete Data::"+commissionSplitDelData);
			/*if (Oper.equalsIgnoreCase("add")) {
				System.out.println("inside add ");
				if(tabpage!=null && tabpage.equals("JoRelease")){
					ecsplitjob.setJoReleaseID(joreleaseid);
					ecsplitjob.setJoMasterId(jomasterid);
				}
				else{
					ecsplitjob.setJoMasterId(jomasterid);
				}
				ecsplitjob.setRxMasterId(rxmasterid);
				ecsplitjob.setAllocated(percentage);
				ecsplitjob.setEcSplitCodeID(splittypeId);
				ecsplitjob.setSplittype(splitType);
				ecsplitjob.setCreatedByID(aUserBean.getUserId());
				ecsplitjob.setCreatedOn(new Date());
				saved = jobService.addSplitCommission(ecsplitjob);
				}else if (Oper.equalsIgnoreCase("del")) {
					System.out.println("ecSplitJobId=="+ecSplitJobId);
					
					saved = jobService.deleteSplitCommission(ecSplitJobId);
				}else if (Oper.equalsIgnoreCase("edit")) {
					if(tabpage!=null && tabpage.equalsIgnoreCase("JoRelease")){
							ecsplitjob.setJoReleaseID(joreleaseid);
							ecsplitjob.setJoMasterId(jomasterid);
						}
					ecsplitjob.setAllocated(percentage);
					ecsplitjob.setEcSplitCodeID(splittypeId);
					ecsplitjob.setSplittype(splitType);
					
					if(rxmasterid!=null){
					   ecsplitjob.setRxMasterId(rxmasterid);
					}
					if(jomasterid!=null){
					ecsplitjob.setJoMasterId(jomasterid);
					}
					ecsplitjob.setAllocated(percentage);
					if(splittypeId!=null)
					{ 
						ecsplitjob.setEcSplitCodeID(splittypeId);
					}
					
					ecsplitjob.setEcSplitJobId(ecSplitJobId);
					ecsplitjob.setChangedByID(aUserBean.getUserId());
					ecsplitjob.setChangedOn(new Date());
					
					saved = jobService.updateSplitCommission(ecsplitjob);
					 Earliest Comment
					Cuso editCusoObj = new Cuso();
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
					saved = jobService.editSOReleaseLineItem(aCusodetail,
							aCuso, editCusoObj);
				}Earlient Comment
		        }*/
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
					rxMasterID = salesRepID;
					ecsplitjob = new Ecsplitjob();
					logger.info("JoMaster ## joReleaseID ==>"+joMasterID +" ## "+thejoReleaseId);
					ecsplitjob.setJoReleaseID(thejoReleaseId);
					ecsplitjob.setJoMasterId(joMasterID);
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
		} catch (JobException e) {
			logger.debug("Job Submittal Controller manipulateSplitCommission");
			sendTransactionException("<b>rxmasterid:</b>"+rxMasterID,"JOB",e,session,therequest);
		}finally {
			
		}

		return aResponse;
	}

	@RequestMapping(value = "/jobCommissionListGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse jobCommissionListGrid(
			@RequestParam(value = "JoMasterId", required = false) Integer JoMasterId,
			@RequestParam(value = "JoReleaseId", required = false) Integer JoReleaseId,
			@RequestParam(value = "tabpage", required = false) String tabpage,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			
			List<?> aSOLineItemDetails =null;
			if(tabpage!=null){
			if(tabpage.equals("JoRelease")){
				aSOLineItemDetails = jobService.getjobCommissionListGrid(JoMasterId,JoReleaseId);
			}else{
				aSOLineItemDetails = jobService.getjobCommissionListGrid(JoMasterId);
			}
		}
			
			 
			/*
													 * Retrieve all sales orders
													 * from the service
													 */
			System.out.println("jobCommissionListGrid JOM::"+JoMasterId+"JoReleaseId"+JoReleaseId+" Page::"+tabpage);
			aResponse.setRows(aSOLineItemDetails); /*
													 * assign those objects to
													 * the custom response
													 */
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			sendTransactionException("<b>JoMasterId:</b>"+JoMasterId,"JOB",e,session,therequest);
			//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
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
	//empSplitTypeListGrid
	@RequestMapping(value = "/empSplitTypeListGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse empSplitTypeListGrid(
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try {
			List<?> aSOempSplitTypeList = jobService.getempSplitTypeListGrid(); 
			/*
													 * Retrieve all sales orders
													 * from the service
													 */
			System.out.println("empSplitTypeListGrid");
			aResponse.setRows(aSOempSplitTypeList); /*
													 * assign those objects to
													 * the custom response
													 */
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>empSplitTypeListGrid:</b>","JOB",e,session,therequest);
			//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}

		return aResponse; 
	}
	@RequestMapping(value = "/getSplitTypewithNameList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getSplitTypewithNameList(@RequestParam("term") String theProductNameWithCode,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) jobService.getSplitTypewithNameList(theProductNameWithCode);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theProductNameWithCode:</b>"+theProductNameWithCode,"JOB",e,session,therequest);
		}
		return aProductCodeWithNameList;
	}
	
	@RequestMapping(value = "/getpercentagebasedonsplittype", method = RequestMethod.GET)
	public @ResponseBody Ecsplitcode getpercentagebasedonsplittype(@RequestParam("id") Integer id,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		Ecsplitcode ecsplitcode = null;
		try {
			ecsplitcode =  jobService.getpercentagebasedonsplittype(id);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>id:</b>"+id,"JOB",e,session,therequest);
		}
		return ecsplitcode;
	}
		
	@RequestMapping(value = "/getEmployeeCommissionDetail", method = RequestMethod.GET)
	public @ResponseBody EmMasterBean getEmployeeCommissionDetail(@RequestParam("id") Integer id,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		EmMasterBean aEmMasterBean = null;
		try {
			aEmMasterBean =  jobService.getEmployeeJobProfitCommission(id);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>getEmployeeCommissionDetail id:</b>"+id,"JOB",e,session,therequest);
		}
		return aEmMasterBean;
	}
	
	//Quickbookprojectid,cusoId,QuickbookDivisionid,Quickbookdate,Quickbookjobtype,locationAddressID1,locationAddressID2,locationcityid,locationState,locationZipID,QuickbookCustomerId
	//salesRepId,customer_PO,QuickbookCSRId,TaxTerritory,contractamount,estimatedcost
	@RequestMapping(value = "/Quickbookjobsubmit", method = RequestMethod.GET)
	public @ResponseBody String getQuickbookjobsubmit(
			@RequestParam(value="Quickbookprojectid", required=false) String theJobName_jobHeader,
			@RequestParam(value="QuickbookCustomer", required=false) String theRolodexCustomer,
			@RequestParam(value="jobcontractorcustomer", required=false) String jobcontractorcustomer,
			@RequestParam(value="locationAddress1", required=false) String theJoblocationAddress1,
			@RequestParam(value="locationAddress2", required=false) String theJoblocationAddress2,
			@RequestParam(value="locationCity", required=false) String theJoblocationCity,
			@RequestParam(value="locationState", required=false) String theJoblocationState,
			@RequestParam(value="locationZipID", required=false) String theJoblocationZip,
			@RequestParam(value="Quickbookdate", required=false) String theBookedDate,
			@RequestParam(value="salesRepId", required=false) Integer theCuAssignmentID0,
			@RequestParam(value="QuickbookCSRId", required=false) Integer theCuAssignmentID1,
			@RequestParam(value="QuickbookDivisionid", required=false) Integer theCoDivision,
			@RequestParam(value="TaxTerritory", required=false) Integer theTaxTerritory,
			@RequestParam(value="Quickbook_TaxTerritory", required=false) Double theTaxvalue,
			@RequestParam(value="QuickbookCustomerId", required=false) Integer theCustomerID,
			@RequestParam(value="QuickbookCustomerId", required=false) String theEmpCustomerID,
			@RequestParam(value="customer_PO", required=false) String theCustomerPONumber,
			@RequestParam(value="contractamount", required=false) double contractamount,
			@RequestParam(value="estimatedcost", required=false) double estimatedcost,
			@RequestParam(value="date", required=false) String date,
			HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		boolean save=false;
		UserBean aUserBean;
		String returnvalue="";  
		String Gseqnum="0";
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		System.out.println("inside the java"+theJobName_jobHeader+"=="+theRolodexCustomer+"=="+theJoblocationAddress1+"=="+theJoblocationAddress2+"=="+theJoblocationCity+"=="+theJoblocationState+"=="+theJoblocationZip+"=="+theBookedDate+"=="+theCuAssignmentID0+"=="+theCuAssignmentID1+"=="+theCoDivision+"=="+theTaxTerritory+"=="+theCustomerID+"=="+theEmpCustomerID+"=="+theCustomerPONumber);
		try {
			//Quickbookprojectid,QuickbookCustomer,locationAddress1,locationAddress2,locationCity,locationState,
			//locationZipID,Quickbookdate,salesRepId,QuickbookCSRId,QuickbookDivisionid,TaxTerritory,QuickbookCustomerId,
		       

			Jomaster aJoMaster = new Jomaster();
			aJoMaster.setDescription(theJobName_jobHeader);
			aJoMaster.setLocationAddress1(theJoblocationAddress1);
			aJoMaster.setLocationAddress2(theJoblocationAddress2);
			aJoMaster.setLocationCity(theJoblocationCity);
			aJoMaster.setLocationState(theJoblocationState);
			aJoMaster.setLocationZip(theJoblocationZip);
			aJoMaster.setJobStatus(3);
			aJoMaster.setCreditStatus((byte)1);
			aJoMaster.setCreditContact0(0);
			aJoMaster.setCreditContact1(0);
			aJoMaster.setCreditContact2(0);
			aJoMaster.setClaimFiled((byte)0);
			aJoMaster.setLocationName(jobcontractorcustomer);
			
			aJoMaster.setCreatedById(aUserBean.getUserId());
			if(theBookedDate != null && theBookedDate != "" && theBookedDate.length() > 10) {
				aJoMaster.setBookedDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy hh:mm"}));
				aJoMaster.setBidDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy hh:mm"}));
			} else if(theBookedDate != null && theBookedDate != "") {
				aJoMaster.setBookedDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy"}));
				aJoMaster.setBidDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy"}));
			}
			aJoMaster.setCuAssignmentId0(theCuAssignmentID0);
			aJoMaster.setCuAssignmentId1(theCuAssignmentID1);
			aJoMaster.setRxCustomerId(theCustomerID);
			aJoMaster.setCoDivisionId(theCoDivision);
			aJoMaster.setCoTaxTerritoryId(theTaxTerritory);
			aJoMaster.setCreatedOn(new Date());
			aJoMaster.setChangedOn(new Date());
			//aJoMaster.setJobNumber();
			aJoMaster.setCustomerPonumber(theCustomerPONumber);
			BigDecimal contractAmount = BigDecimal.valueOf(contractamount);
			BigDecimal estimatedCost = BigDecimal.valueOf(estimatedcost);
			aJoMaster.setContractAmount(contractAmount);
			aJoMaster.setEstimatedCost(estimatedCost);
			BigDecimal estimatedProfit = BigDecimal.valueOf(contractamount-estimatedcost);
			aJoMaster.setEstimatedProfit(estimatedProfit);
			
			String ourpo =aJoMaster.getJobNumber()+"A";
			if(theCustomerPONumber==null || theCustomerPONumber==""){
				theCustomerPONumber="";
			}
			//Jenith on 2014-09-04 for add Quote number in Quick job too
			
			Date aDate = null;
			    aDate = DateUtils.parseDate(date, new String[]{"MM/dd/yyyy"});
				TsUserLogin aUserSalesRep = userService.getUserDetails(theCuAssignmentID0);
				String aUserInitial = aUserSalesRep.getInitials();
				String arrVals[] = date.split("/");
				if(arrVals[0].length() == 1) {arrVals[0] = "0".concat(arrVals[0]); }
				String aNewJobNumber = arrVals[2].substring(2, 4).concat(arrVals[0]).concat("-");
				/*if(jobNumber.contains("-")){
					aNewJobNumber = aUserInitial.concat(aNewJobNumber);
				}*/
				String aNewBookedJobNumber = jobService.getNewJobNumber(aNewJobNumber);
				Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
				String format3 = new SimpleDateFormat("yy MM dd").format(now);
				String[] split = format3.split(" ");
				aNewBookedJobNumber = "";
					
					//aNewBookedJobNumber = aUserInitial.concat(aNewBookedJobNumber);
					
					String jobNumberNew = "";
					if(theCuAssignmentID0 != null)
					{
						logger.info("assignmentid========="+theCuAssignmentID0);
						Emmaster aEmmaster = jobService.getEmployeeDetailLoginID(theCuAssignmentID0);
						//logger.info("EmMaster : "+aEmmaster.getJobNumberGenerate() +" "+aEmmaster.getJobNumberPrefix() +" "+aEmmaster.getJobNumberSequence());
						if(aEmmaster!=null)
						{
						if(aEmmaster.getJobNumberGenerate())
						{
							//jobNumberNew = aEmmaster.getJobNumberPrefix()+"-"+aEmmaster.getJobNumberSequence();
							
							if(aEmmaster.getJobNumberPrefix()!=null && aEmmaster.getJobNumberPrefix().length() != 0 && 
									aEmmaster.getJobNumberSequence() != null && aEmmaster.getJobNumberSequence().length()!=0 )
							{
								/*jobNumberNew = aEmmaster.getJobNumberPrefix()+"";
								String theJobNumber = jobService.getJobNumber(jobNumberNew);*/
								
								if(aEmmaster.getJobNumberSequence().toString().trim().length()> 0){
									jobNumberNew = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence();
									//String[] sp = theJobNumber.split("-");
									//String numberOnly= theJobNumber.replaceAll("[^0-9]", "");
									//String sLen = String.valueOf(Integer.valueOf(numberOnly)+1);
									
									if(aEmmaster.getJobNumberSequence().toString().trim().length()<= 1)
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence();
									else if(aEmmaster.getJobNumberSequence().toString().trim().length() <= 2)
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence();
									else if(aEmmaster.getJobNumberSequence().toString().trim().length() <= 3)
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence();
									else 
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence();
								}else{
									jobNumberNew = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence().toString();
								}
							}
							else if(aEmmaster.getJobNumberSequence()==null || aEmmaster.getJobNumberSequence().trim().equals(""))
							{
								jobNumberNew = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1];
								logger.info("New Job Number: "+jobNumberNew);
								String seq = jobService.getJobNumbersequence(jobNumberNew);
								String sequences = null;
								if(seq.equals("0")){
									sequences = jobService.getJobNumberSequenceDate(jobNumberNew);
									if(!sequences.equals("0")){
										seq=sequences.substring(sequences.length()-2);
									}
								}
								Integer Seqconver=JobUtil.ConvertintoInteger(seq);
								Seqconver=Seqconver+1;
								if(Seqconver.toString().length()==1){
									jobNumberNew=jobNumberNew+"0"+Seqconver.toString();	
								}/*Changed by Velmurugan
								 * Date:30-07-2015
								Due to 100 seqnumber job number has not generate
								else if(Seqconver.toString().length()==2){*/
								else if(Seqconver.toString().length()>1){ 	
									jobNumberNew=jobNumberNew+Seqconver.toString();	
								}
								Gseqnum=Seqconver.toString();
								System.out.println("===Jobnumber===="+jobNumberNew);
								//jobNumberNew=jobNumberNew+
								
								/*if(theJobNumber.trim().length() > 0) {
									String[] sp = theJobNumber.split("-");
									String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
									if(sLen.length() <= 1)
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1]+"-00"+sLen;
									else if(sLen.length() <= 2)
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1]+"-0"+sLen;
									else if(sLen.length() <= 3)
										jobNumberNew = aEmmaster.getJobNumberPrefix()+"-0"+sLen;
									else 
										jobNumberNew = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1]+"-"+sLen;
								}else{
									jobNumberNew = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1]+"-001";
								}*/
							}
							else if(aEmmaster.getJobNumberPrefix() == null || aEmmaster.getJobNumberPrefix().length() == 0){
								jobNumberNew = aEmmaster.getJobNumberSequence().toString();
								//String newnum = split[0]+""+split[1]+"-"+jobNumberNew;
								String newnum =jobNumberNew;
								//String theJobNumber = jobService.getJobNumber(newnum);
								if(newnum.trim().length() > 0){
									//String sLen = String.valueOf(Integer.valueOf(theJobNumber)+1);
									jobNumberNew =newnum;
								}else{
									jobNumberNew = String.valueOf(aEmmaster.getJobNumberSequence());
								}
							}else{
								jobNumberNew = split[0]+""+split[1]+"-";
								String theJobNumber = jobService.getJobNumber(jobNumberNew);
								if(theJobNumber.trim().length() > 0){
									String[] sp = theJobNumber.split("-");
									String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
									/*if(sLen.length() <= 1)
										jobNumberNew = split[0]+""+split[1]+"-000"+sLen;
									else if(sLen.length() <= 2)
										jobNumberNew = split[0]+""+split[1]+"-00"+sLen;
									else if(sLen.length() <= 3)
										jobNumberNew = split[0]+""+split[1]+"-0"+sLen;
									else 
										jobNumberNew = split[0]+""+split[1]+"-"+sLen;*/
									Gseqnum=sLen;						
									jobNumberNew = getFourDigitNo(sLen, split[0],split[1]);
								}else{
									Gseqnum="001";
									jobNumberNew = split[0]+""+split[1]+"-001";
								}
							}
							
							logger.info("New Job Number: "+jobNumberNew);
							aJoMaster.setJobNumber(jobNumberNew);
							
							/*String emMaster_jobNumberSequence = null;
							String[] splitNewNum= null;
							if(jobNumberNew.contains("-")){
								splitNewNum = jobNumberNew.split("-");
								emMaster_jobNumberSequence = splitNewNum[1];
							}else{
								emMaster_jobNumberSequence = jobNumberNew.replaceAll("[^0-9]", "");
							}
							
							logger.info("Number For Insert: "+emMaster_jobNumberSequence +" Length: "+ emMaster_jobNumberSequence.length());
							Integer insertNewSeq = (Integer.parseInt(emMaster_jobNumberSequence))+1;
							String insValue = insertNewSeq+"";
							String zeroVal="";
							if(insValue.length()<emMaster_jobNumberSequence.length()){
								Integer seqLength = emMaster_jobNumberSequence.length() - insValue.length();
							  for(int i=0;i<seqLength;i++){
								  zeroVal+="0";
							  }
							  zeroVal+=insertNewSeq+"";
							}else{
								zeroVal = insertNewSeq+"";
							}
							*/

							if(aEmmaster.getJobNumberSequence()!=null){
								if(!aEmmaster.getJobNumberSequence().equals("")){
									aEmmaster.setJobNumberSequence((Integer.parseInt(aEmmaster.getJobNumberSequence())+1)+"");
									aEmmaster = itsRolodexService.updateCommissions(aEmmaster);
								}
							}
						}else{
							jobNumberNew = split[0]+""+split[1]+"-";
							String theJobNumber = jobService.getJobNumber(jobNumberNew);
							if(theJobNumber.trim().length() > 0){
								String[] sp = theJobNumber.split("-");
								String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
								Gseqnum=sLen;
								/*if(sLen.length() <= 1)
									jobNumberNew = split[0]+""+split[1]+"-000"+sLen;
								else if(sLen.length() <= 2)
									jobNumberNew = split[0]+""+split[1]+"-00"+sLen;
								else if(sLen.length() <= 3)
									jobNumberNew = split[0]+""+split[1]+"-0"+sLen;
								else 
									jobNumberNew = split[0]+""+split[1]+"-"+sLen;*/
								
								jobNumberNew = getFourDigitNo(sLen, split[0],split[1]);
							}else{
								Gseqnum="001";
								jobNumberNew = split[0]+""+split[1]+"-001";
							}
							aJoMaster.setJobNumber(jobNumberNew);
						}
						}
					}else{
						jobNumberNew = split[0]+""+split[1]+"-";
						String theJobNumber = jobService.getJobNumber(jobNumberNew);
						if(theJobNumber.trim().length() > 0){
							String[] sp = theJobNumber.split("-");
							String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
							if(sLen.length() <= 1){
								Gseqnum="00"+sLen;
								jobNumberNew = split[0]+""+split[1]+"-00"+sLen;
							}
							else if(sLen.length() <= 2){
								Gseqnum="0"+sLen;
								jobNumberNew = split[0]+""+split[1]+"-0"+sLen;
							}
							else {
								Gseqnum=""+sLen;
								jobNumberNew = split[0]+""+split[1]+"-"+sLen;
							}
							jobNumberNew = sLen;
						}else{
							Gseqnum="001";
							jobNumberNew = split[0]+""+split[1]+"-001";
						}
						aJoMaster.setJobNumber(jobNumberNew);
					}
					
					
					//aJoMaster.setJobNumber(jobNumber);
					//aJomaster.setJoMasterId(joMasterID);
				    /*aNewJobNumber = aNewJobNumber+"-";
				    String aNewJobString = jobService.getNewJobNumber(aNewJobNumber);*/
					//aJomaster.setJobNumber(aNewBookedJobNumber);
					aJoMaster.setQuoteNumber(itsSysService.getSysSequenceNumber("joMaster").toString());
					//aJomaster2 = jobService.updateORIJobNumber(aJomaster);
					
					//jobService.deleteRecentlyOpendJob(aJomaster3);
					//return jobNumberNew;
			
			//------Add Quote No in Quick Job End -------//

			
				//Date aDate = null;
		       aDate = DateUtils.parseDate(date, new String[]{"MM/dd/yyyy"});
		      // TsUserLogin aUserSalesRep = userService.getUserDetails(theCuAssignmentID0);
              // String aUserInitial = aUserSalesRep.getInitials();
               //String arrVals[] = date.split("/");
               //if(arrVals[0].length() == 1) {arrVals[0] = "0".concat(arrVals[0]); }
               //String aNewJobNumber = arrVals[2].substring(2, 4).concat(arrVals[0]).concat("-");
               //String aNewBookedJobNumber="";
               try{
            	   aNewBookedJobNumber = jobService.getNewJobNumber(aNewJobNumber);   
               }catch(Exception e){
            	   aNewBookedJobNumber = jobService.getNewJobNumber("001");
            	   
               }
               
              // aJoMaster.setJobNumber(aNewBookedJobNumber);
			   aNewBookedJobNumber = aUserInitial.concat(aNewBookedJobNumber);
			   
			   aJoMaster.setSeqnum(Gseqnum);
               Integer joMasterId = jobService.addQuickJob(aJoMaster);                           
               System.out.println("aNewBookedJobNumber"+aNewBookedJobNumber+"A");
               String FromPage="Home";
               //theRolodexCustomer,theCustomerID
               System.out.println("ponumber=="+aNewBookedJobNumber);
               JoRelease jorelease=new JoRelease();
               JoReleaseDetail joreleasedetail=new JoReleaseDetail();
               Integer joReleaseId=0;
           	/*	if(joMasterId>0){
	               jorelease.setJoMasterId(joMasterId);
	               jorelease.setReleaseType(1);
	               jorelease.setReleaseDate(aJoMaster.getBookedDate());
	               joReleaseId=jobService.addJoRelease(jorelease);
               	}
               	if(joReleaseId>0 && joMasterId>0){
	               joreleasedetail.setJoReleaseId(joReleaseId);
	               joreleasedetail.setJoMasterId(joMasterId);
	               Integer joReleasedetailId=jobService.addJoReleaseDetail(joreleasedetail);
               	}   */                   
               
               Sysvariable aSysvariable=new Sysvariable();
               aSysvariable=itsEmployeeService.getSysVariableDetails("DefaulttoApprovedcreditforanewJob");
               if(aSysvariable.getValueLong()==1){
   				if(joMasterId != null){
   				Jomaster ajoMaster=new  Jomaster();
   				ajoMaster.setJoMasterId(joMasterId);
   				ajoMaster.setCreditStatus((byte) 1);
   				Date today =new Date();
   			    ajoMaster.setCreditStatusDate(today);
   			    ajoMaster.setWho0(aUserBean.getUserId());
   				jobService.UpDateCreditInfoDetails(ajoMaster);
   				}
   				}
               	returnvalue="FromPage="+FromPage+"&jobNumber="+jobNumberNew+"&OurPO="+aNewBookedJobNumber+"A&POnumber="+theCustomerPONumber+"&jobName="+theJobName_jobHeader+"&CustomerName="+theRolodexCustomer+"&CustomerId="+theCustomerID+"&JoReleaseId="+joReleaseId+"&Location1="+aJoMaster.getLocationAddress1()+"&Location2="+aJoMaster.getLocationAddress2()+"&LocationCity="+aJoMaster.getLocationCity()+"&LocationState="+aJoMaster.getLocationState()+"&LocationZip="+aJoMaster.getLocationZip()+"&TaxTerritory="+theTaxvalue+"&joMasterID="+joMasterId;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			sendTransactionException("<b>theJobName_jobHeader:</b>"+theJobName_jobHeader,"JOB",e,session,therequest);
		}
		return returnvalue;
	}
	
	
	
	private String getFourDigitNo(String sLen, String val1,String val2) {
		String jobNumberNew;
		if(sLen.length() <= 1)
			jobNumberNew = val1+""+val2+"-00"+sLen;
		else if(sLen.length() <= 2)
			jobNumberNew = val1+""+val2+"-0"+sLen;
		/*else if(sLen.length() <= 3)
			jobNumberNew = val1+""+val2+"-0"+sLen;*/
		else 
			jobNumberNew = val1+""+val2+"-"+sLen;
		
		System.out.println("Inside getFourDigitNo--------->>>>>" + jobNumberNew);
		return jobNumberNew;
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


	@RequestMapping(value = "/addPONew", method = RequestMethod.POST)
	public @ResponseBody
	Vepo addPONew(
			@RequestParam(value = "vendorName", required = false) String theVendorName,
			@RequestParam(value = "attnName", required = false) Integer theAttn,
			@RequestParam(value = "tagName", required = false) String theTag,
			@RequestParam(value = "manufactName", required = false) Integer theManufacture,			
			@RequestParam(value = "orderhiddenName", required = false) String theOrderHidden,
			@RequestParam(value = "poDateName", required = false) String thePODate,
			@RequestParam(value = "frieghtChangesName", required = false) Integer theFrieghtChange,
			@RequestParam(value = "customerName", required = false) String theCustomerPOName,
			@RequestParam(value = "orderName", required = false) Integer theOrderedBy,
			@RequestParam(value = "shipViaName", required = false) Integer theShipViaName,
			@RequestParam(value = "ourPoName", required = false) String theOurPOName,
			@RequestParam(value = "wantedName", required = false) String theWantedName,			
			@RequestParam(value = "specialInstructionName", required = false) String theSpecialInst,			
			@RequestParam(value = "subtotalGeneralName", required = false) BigDecimal theSubtotal,
			@RequestParam(value = "freightGeneralName", required = false) BigDecimal theFreight,			
			@RequestParam(value = "manufaturer", required = false) Integer theManufaturerID,
			@RequestParam(value = "jorelease", required = false) Integer thejoReleaseId,
			@RequestParam(value = "TaxTotal", required = false) BigDecimal thetaxTotal,
			@RequestParam(value = "customerBillToOtherID", required = false) String customerBillToOtherID,
			@RequestParam(value = "customerShipToOtherID", required = false) String customerShipToOtherID,
			@RequestParam(value = "locationbillToAddressID", required = false) String theAddresName,
			@RequestParam(value = "locationbillToAddress1", required = false) String theAddres1,
			@RequestParam(value = "locationbillToAddress2", required = false) String theAddres2,
			@RequestParam(value = "locationbillToCity", required = false) String theCity,
			@RequestParam(value = "locationbillToState", required = false) String theState,
			@RequestParam(value = "locationbillToZip", required = false) String theZip,
			@RequestParam(value = "shipToName", required = false) String theShipAddresName,
			@RequestParam(value = "shipToAddress1", required = false) String theShipAddres1,
			@RequestParam(value = "shipToAddress2", required = false) String theShipAddres2,
			@RequestParam(value = "shipToCity", required = false) String theShipCity,
			@RequestParam(value = "shipToState", required = false) String theShipState,
			@RequestParam(value = "shipToZip", required = false) String theShipZip,
			@RequestParam(value = "customerShipToName", required = false) Integer theCustomerShipToName,
			@RequestParam(value = "customerBillName", required = false) Integer theCustomerBillName,
			@RequestParam(value = "gridData", required = false) String gridData,
			@RequestParam(value = "buttonId", required = false) String buttonId,
			@RequestParam(value = "vePOID", required = false) Integer vePOID,
			@RequestParam(value = "manfactureID", required = false) Integer therxID,
			@RequestParam(value = "taxPerc", required = false) BigDecimal thetaxPerc,
			@RequestParam(value = "customerBillToAddressID", required = false) Integer theCustomerBillToAddressID,
			@RequestParam(value = "customerShipToAddressID", required = false) Integer theCustomerShipToAddressID,
			@RequestParam(value = "prWarehouseID", required = false) Integer prWarehouseID,
			@RequestParam(value= "ourpo", required=false) String ourpo,
			@RequestParam(value= "transactionStatus", required=false) Integer transactionStatus,
			@RequestParam(value = "rxShiptoid", required = false) Integer theShiptoid,
			@RequestParam(value = "rxShiptomodevalue", required = false) byte therxShiptomodevalue,
			 ModelMap theModel,HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws JobException, IOException,
			ParseException, VendorException, MessagingException {	
		
		logger.info("Shipto: "+customerShipToOtherID);

		Vepo aVepo = new Vepo();
		Vepo aVepo2 = new Vepo();
		Rxaddress aRxaddress = new Rxaddress();
		Rxaddress aRxaddressShipTo = new Rxaddress();
		JoRelease aJoRelease = new JoRelease();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (null != theTag) {
				aVepo.setTag(theTag);
			}
			if (null != theAttn && theAttn > -1) {
				aVepo.setRxVendorContactId(theAttn);
			}
			if (null != theManufacture && theManufacture > -1) {
				aVepo.setRxVendorId(theManufacture);
			} else {
				aVepo.setRxVendorId(therxID);
			}
			if (null != thejoReleaseId && thejoReleaseId > 0) {
				aVepo.setJoReleaseId(thejoReleaseId);
			}
			if (null != theManufaturerID && theManufaturerID > 0) {
				aVepo.setRxBillToId(theManufaturerID);
				aVepo.setRxShipToId(theManufaturerID);
			}

			String theQBId = "Test";
			if (theQBId != null && !theQBId.equals("")) {
				aVepo.setQbPO(theQBId);
			}
			if (theOrderHidden != "" && theOrderHidden != null) {
				aVepo.setCreatedById(Integer.valueOf(theOrderHidden));
			}
			Integer theVefactoryID = 1;
			aVepo.setVeFactoryId(theVefactoryID);
			if (theOrderedBy == 0) {
				if (aUserBean != null) {
					aVepo.setOrderedById(aUserBean.getUserId());
				}
			} else {
				aVepo.setOrderedById(theOrderedBy);
			}
			if (null != theOurPOName && theOurPOName.trim().length() > 0) {
				aVepo.setPonumber(theOurPOName);
			}
			
			
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String createdDate = df.format(now);
			if ((createdDate != null && createdDate != "")) {
				aVepo.setCreatedOn(DateUtils.parseDate(createdDate,
						new String[] { "MM/dd/yyyy" }));
			}
			if (thePODate != null && thePODate != "") {
				aVepo.setOrderDate(DateUtils.parseDate(thePODate.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			if(transactionStatus!=null){
				aVepo.setTransactionStatus(transactionStatus);
			}
			else{
				aVepo.setTransactionStatus(1);
			}
			
			aVepo.setVeShipViaId(theShipViaName);
			aVepo.setVeFreightChargesId(theFrieghtChange);
			aVepo.setCustomerPonumber(theCustomerPOName);
			aVepo.setDateWanted(theWantedName);
			aVepo.setSpecialInstructions(theSpecialInst);
			aVepo.setFreight(theFreight);
			aVepo.setShipToMode(therxShiptomodevalue);
			aVepo.setTaxRate(thetaxPerc);
			aVepo.setSubtotal(theSubtotal);
			aVepo.setTaxTotal(thetaxTotal);
			aVepo.setConsignment(true);
			aVepo.setMultipleAcks(true);
			if (null == theCustomerBillName) {
				if (null != theCustomerBillToAddressID) {
					aVepo.setRxBillToId(theCustomerBillName);
					aVepo.setRxBillToAddressId(theCustomerBillToAddressID);
					
					
				}
				if ("Customer".equalsIgnoreCase(customerBillToOtherID)) {
					aVepo.setBillToIndex(1);
				} else {
					aVepo.setBillToIndex(0);
				}

			} else {
				aVepo.setRxBillToId(theCustomerBillName);
				aVepo.setRxBillToAddressId(theCustomerBillToAddressID);

				if ("Customer".equalsIgnoreCase(customerBillToOtherID)) {
					aVepo.setBillToIndex(1);
				}
			}
			/*if (null == theCustomerShipToName) {
				if (null != theCustomerShipToAddressID) {
					aVepo.setRxShipToId(theCustomerShipToName);
					aVepo.setRxShipToAddressId(theCustomerShipToAddressID);
		
				}
				if ("Customer".equalsIgnoreCase(customerShipToOtherID)) {
					aVepo.setShipTo(1);
				} else {
					aVepo.setShipTo(0);
				}

			} else {
				aVepo.setRxShipToId(theCustomerShipToName);
				aVepo.setRxShipToAddressId(theCustomerShipToAddressID);
				aVepo.setShipTo(0);
				if ("Customer".equalsIgnoreCase(customerShipToOtherID)) {
					aVepo.setShipTo(1);
				}
			}

			aVepo.setRxShipToAddressId(prWarehouseID);*/
			
			if ("Other".equalsIgnoreCase(customerBillToOtherID)) {

				aRxaddress.setFax(customerBillToOtherID);
				aRxaddress.setAddress1(theAddres1);
				aRxaddress.setAddress2(theAddres2);
				aRxaddress.setCity(theCity);
				aRxaddress.setState(theState);
				aRxaddress.setZip(theZip);
				aRxaddress.setIsBillTo(false);
				aRxaddress.setInActive(false);
				aRxaddress.setIsMailing(false);
				aRxaddress.setIsStreet(false);
				aRxaddress.setIsShipTo(false);
				aRxaddress.setOtherBillTo(2);
				aRxaddress.setName(theAddresName);
				aVepo.setRxBillToAddressId(theCustomerBillName);
				aVepo.setBillToIndex(2);
			}
			
			if (therxShiptomodevalue == 0) {
				aVepo.setPrWarehouseId(theShiptoid);
			}
			else if(therxShiptomodevalue == 1)
			{
				aVepo.setRxShipToId(theShiptoid);
				aVepo.setPrWarehouseId(prWarehouseID);
			}
			else if(therxShiptomodevalue == 2)
			{
				aVepo.setRxShipToId(theShiptoid);
				aVepo.setPrWarehouseId(prWarehouseID);
			}
			else if(therxShiptomodevalue == 3)
			{
				aVepo.setPrWarehouseId(prWarehouseID);
				aRxaddressShipTo.setFax(customerShipToOtherID);
				aRxaddressShipTo.setAddress1(theShipAddres1);
				aRxaddressShipTo.setAddress2(theShipAddres2);
				aRxaddressShipTo.setCity(theShipCity);
				aRxaddressShipTo.setState(theShipState);
				aRxaddressShipTo.setIsBillTo(false);
				aRxaddressShipTo.setInActive(false);
				aRxaddressShipTo.setIsMailing(false);
				aRxaddressShipTo.setIsStreet(false);
				aRxaddressShipTo.setIsShipTo(false);
				aRxaddressShipTo.setOtherShipTo(3);
				aRxaddressShipTo.setZip(theShipZip);
				aRxaddressShipTo.setName(theShipAddresName);
				aVepo.setRxShipToAddressId(theShiptoid);
				aVepo.setShipTo(3);
			}
			if (null != vePOID) {
				aVepo.setVePoid(vePOID);
			}
			
			if(ourpo!=null){
				if(ourpo.length()>0){
					aVepo.setPonumber(ourpo);	
				}
			}
			aVepo2 = jobService.addPOGeneral(aVepo, aRxaddress,aRxaddressShipTo, aJoRelease);

			//try {
				//jobService.addPOLineDetails(alVepodetails,taxAmountfromlineitem);
				
			//} catch (JobException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
	
		} catch (Exception e) {
			sendTransactionException("<b>theVendorName:</b>"+theVendorName,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
		}
		logger.info("*********** Submittal updated ***********");

		return aVepo2;
	}
	
	
	@RequestMapping(value = "/addPOReleaseNew", method = RequestMethod.POST)
	public @ResponseBody
	Vepo updatePOReleaseReleaseNew(
			@RequestParam(value = "vendorName", required = false) String theVendorName,
			@RequestParam(value = "attnName", required = false) Integer theAttn,
			@RequestParam(value = "tagName", required = false) String theTag,
			@RequestParam(value = "manufactName", required = false) Integer theManufacture,
			@RequestParam(value = "toBillAddress", required = false) Integer thebilltoAddress,
			@RequestParam(value = "toShipAddress", required = false) Integer theShiptoAddress,
			@RequestParam(value = "orderhiddenName", required = false) String theOrderHidden,
			@RequestParam(value = "poDateName", required = false) String thePODate,
			@RequestParam(value = "frieghtChangesName", required = false) Integer theFrieghtChange,
			@RequestParam(value = "customerName", required = false) String theCustomerPOName,
			@RequestParam(value = "orderhiddenName", required = false) Integer theOrderedBy,
			@RequestParam(value = "shipViaName", required = false) Integer theShipViaName,
			@RequestParam(value = "ourPoName", required = false) String theOurPOName,
			@RequestParam(value = "wantedName", required = false) String theWantedName,
			@RequestParam(value = "wantedCombo", required = false) Integer theWantedOnOrBefore,
			@RequestParam(value = "specialInstructionName", required = false) String theSpecialInst,
			@RequestParam(value = "qbId", required = false) String theQBId,
			@RequestParam(value = "subtotalGeneralName", required = false) BigDecimal theSubtotal,
			@RequestParam(value = "freightGeneralName", required = false) BigDecimal theFreight,
			@RequestParam(value = "taxGeneralName", required = false) BigDecimal theTotalGemnrel,
			@RequestParam(value = "generalName", required = false) BigDecimal theGenerelName,
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "vefactoryID", required = false) Integer theVefactoryID,
			@RequestParam(value = "manufaturer", required = false) Integer theManufaturerID,
			@RequestParam(value = "jorelease", required = false) Integer thejoReleaseId,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "TaxTotal", required = false) BigDecimal thetaxTotal,
			@RequestParam(value = "locationbillToAddressID", required = false) String theAddresName,
			@RequestParam(value = "locationbillToAddress1", required = false) String theAddres1,
			@RequestParam(value = "locationbillToAddress2", required = false) String theAddres2,
			@RequestParam(value = "locationbillToCity", required = false) String theCity,
			@RequestParam(value = "locationbillToState", required = false) String theState,
			@RequestParam(value = "locationbillToZip", required = false) String theZip,
			@RequestParam(value = "shipToName", required = false) String theShipAddresName,
			@RequestParam(value = "shipToAddress1", required = false) String theShipAddres1,
			@RequestParam(value = "shipToAddress2", required = false) String theShipAddres2,
			@RequestParam(value = "shipToCity", required = false) String theShipCity,
			@RequestParam(value = "shipToState", required = false) String theShipState,
			@RequestParam(value = "shipToZip", required = false) String theShipZip,
			@RequestParam(value = "emailTimeStamp", required = false) String theEmailTimeStamp,
			@RequestParam(value = "customerShipToName", required = false) Integer theCustomerShipToName,
			@RequestParam(value = "customerBillName", required = false) Integer theCustomerBillName,
			@RequestParam(value = "transactionStatus", required = false) Integer transactionStatus,
			@RequestParam(value = "prWarehouseID", required = false) Integer prWarehouseID,
			@RequestParam(value = "customerID", required = false) Integer customerID,
			@RequestParam(value = "rxShiptoid", required = false) Integer theShiptoid,
			@RequestParam(value = "rxShiptomodevalue", required = false) byte therxShiptomodevalue,
			@RequestParam(value = "commissionSplitGridData",required = false) String commissionSplitGridData,
			@RequestParam(value = "commissionSplitdelData[]",required = false) ArrayList<String>  commissionSplitDelData,
			
			HttpSession session,HttpServletRequest therequest, ModelMap theModel,
			HttpServletResponse theResponse) throws JobException, IOException,
			ParseException, VendorException, MessagingException {
		Vepo aVepo = new Vepo();
		Vepo aVepo2 = new Vepo();
		Rxaddress aRxaddress = new Rxaddress();
		Rxaddress aRxaddressShipTo = new Rxaddress();
		JoRelease aJoRelease = new JoRelease();
		Ecsplitjob ecsplitjob = null;
		Boolean saved = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		logger.info("1.shiptoindex"+theShiptoAddress);
		logger.info("2.shiptoMode"+theShiptoAddress);
		logger.info("3.shiptoRxAddress"+theShiptoAddress);
		logger.info("Contact ID:"+theAttn);
		logger.info("prWarehouseID#:"+prWarehouseID);
		logger.info(theShipAddresName+"===="+aVepo.getShipTo());
		
		
		try {
			aVepo.setTag(theTag);
			aVepo.setRxVendorContactId(theAttn);
			//aVepo.setRxVendorId(theManufacture);
			aVepo.setJoReleaseId(thejoReleaseId);
			aVepo.setRxBillToId(theManufaturerID);
			//aVepo.setRxShipToId(theManufaturerID);
		
			//aVepo.setRxVendorContactId(contactId);
			if (theQBId != null && !theQBId.equals("")) {
				aVepo.setQbPO(theQBId);
			}
			if (theOrderHidden != null && !theOrderHidden.equals("")) {
				aVepo.setCreatedById(Integer.valueOf(theOrderHidden));
				aVepo.setOrderedById(Integer.valueOf(theOrderHidden));
			}
			aVepo.setVeFactoryId(theVefactoryID);
			if (theOrderedBy == 0) {
				if (aUserBean != null) {
					aVepo.setOrderedById(aUserBean.getUserId());
				}
			} else {
				aVepo.setOrderedById(theOrderedBy);
			}
			aVepo.setPonumber(theOurPOName);
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String createdDate = df.format(now);
			if ((createdDate != null && createdDate != "")) {
				aVepo.setCreatedOn(DateUtils.parseDate(createdDate,
						new String[] { "MM/dd/yyyy" }));
			}
			if (thePODate != null && thePODate != "") {
				aVepo.setOrderDate(DateUtils.parseDate(thePODate.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			if (theEmailTimeStamp != null && theEmailTimeStamp != "") {
				
				if(theEmailTimeStamp.toLowerCase().contains("am")||theEmailTimeStamp.toLowerCase().contains("pm"))
				aVepo.setEmailTimeStamp(DateUtils.parseDate(theEmailTimeStamp,new String[] { "MM/dd/yyyy hh:mm a" }));
				else
				aVepo.setEmailTimeStamp(DateUtils.parseDate(theEmailTimeStamp,new String[] { "MM/dd/yyyy hh:mm " }));	
			}
			aVepo.setVeShipViaId(theShipViaName);
			aVepo.setVeFreightChargesId(theFrieghtChange);
			aVepo.setCustomerPonumber(theCustomerPOName);
			aVepo.setDateWanted(theWantedName);
			aVepo.setWantedOnOrBefore(theWantedOnOrBefore);
			aVepo.setSpecialInstructions(theSpecialInst);
			aVepo.setFreight(theFreight);
			aVepo.setTaxRate(theTotalGemnrel);
			aVepo.setSubtotal(theSubtotal);
			aVepo.setTaxTotal(thetaxTotal);
			aVepo.setVePoid(jobService.getVepoId(thejoReleaseId));
			if(aVepo.getVePoid()!=null && aVepo.getVePoid()>0){
			Vepo aVePO = itsVendorService.getVePo(aVepo.getVePoid());
			aVepo.setRxVendorId(aVePO.getRxVendorId());
			aVepo.setBillToIndex(aVePO.getBillToIndex());
			aVepo.setShipTo(aVePO.getShipTo());
			aVepo.setShipToMode(therxShiptomodevalue);
				if(aVepo.getShipToMode()==0)
				{
					aVepo.setPrWarehouseId(theShiptoid);
				}
				else if(aVepo.getShipToMode()==1||aVepo.getShipToMode()==2)
				{
					aVepo.setRxShipToId(theShiptoid);
					aVepo.setPrWarehouseId(prWarehouseID);
				}
			}
						
			aVepo.setTransactionStatus(transactionStatus);
			//aVepo.setRxShipToAddressId(theCustomerShipToName);
			
			
			if (aVepo.getBillToIndex() != null && aVepo.getBillToIndex() == 2) {
				aVepo.setPrWarehouseId(prWarehouseID);
				aRxaddress.setAddress1(theAddres1);
				aRxaddress.setAddress2(theAddres2);
				aRxaddress.setCity(theCity);
				aRxaddress.setState(theState);
				aRxaddress.setZip(theZip);
				aRxaddress.setIsBillTo(false);
				aRxaddress.setInActive(false);
				aRxaddress.setIsMailing(true);
				aRxaddress.setIsStreet(false);
				aRxaddress.setIsShipTo(false);
				aRxaddress.setOtherBillTo(2);
				aRxaddress.setRxMasterId(theCustomerBillName);
				aRxaddress.setName(theAddresName);
				aVepo.setRxBillToAddressId(theCustomerBillName);
			}
			if (aVepo.getShipToMode() == 3) {
				aVepo.setPrWarehouseId(prWarehouseID);
				aRxaddressShipTo.setAddress1(theShipAddres1);
				aRxaddressShipTo.setAddress2(theShipAddres2);
				aRxaddressShipTo.setCity(theShipCity);
				aRxaddressShipTo.setState(theShipState);
				aRxaddressShipTo.setIsBillTo(false);
				aRxaddressShipTo.setInActive(false);
				aRxaddressShipTo.setIsMailing(false);
				aRxaddressShipTo.setIsStreet(false);
				aRxaddressShipTo.setIsShipTo(false);
				aRxaddressShipTo.setOtherShipTo(3);
				aRxaddressShipTo.setZip(theShipZip);
				aRxaddressShipTo.setRxMasterId(theCustomerShipToName);
				aRxaddressShipTo.setName(theShipAddresName);
				aRxaddressShipTo.setCoTaxTerritoryId(jobService.getCoTaxterritoryIdfmrxmasterid(theManufaturerID));
				aVepo.setRxShipToAddressId(theCustomerShipToName);
			}
			
			aVepo.setJobCustomerID(customerID);
			aJoRelease.setJoReleaseId(thejoReleaseId);
			aVepo2 = jobService.updatePOGeneral(aVepo, aRxaddress,
					aRxaddressShipTo, aJoRelease);
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
					logger.info("JoMaster ## joReleaseID ==>"+joMasterID +" ## "+thejoReleaseId);
					ecsplitjob.setJoReleaseID(thejoReleaseId);
					ecsplitjob.setJoMasterId(joMasterID);
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
		} catch (JobException e) {
			sendTransactionException("<b>thejoReleaseId:</b>"+thejoReleaseId,"JOB",e,session,therequest);
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		logger.info("*********** Submittal updated ***********");
		return aVepo2;
	}
}


