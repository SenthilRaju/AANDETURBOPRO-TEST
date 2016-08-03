/**
 * 
 */
package com.turborep.turbotracker.employee.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.RxJournal;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.employee.dao.CommissionStatementBean;
import com.turborep.turbotracker.employee.dao.Ecperiod;
import com.turborep.turbotracker.employee.dao.Ecstatement;
import com.turborep.turbotracker.employee.dao.EmMasterBean;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomGenericResponse;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.ReportUtils;
import com.turborep.turbotracker.util.SessionConstants;

/**
 * Handles CRUD requests for Employees
 * 
 */
@Controller
@RequestMapping("/employeeCrud")
public class EmployeeController {

	protected static Logger itsLogger = Logger.getLogger(EmployeeController.class);

	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeServiceInter;

	@Resource(name="rolodexService")
	private RolodexService itsEmployeeService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name="accountingCyclesService")
	AccountingCyclesService accountingCyclesService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	/**
	 * The default method when a request to /users is made. This essentially
	 * retrieves all users, which are wrapped inside a CustomUserResponse
	 * object. The object is automatically converted to JSON when returning back
	 * the response. The @ResponseBody is responsible for this behavior.
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	/*
	@RequestMapping(method = RequestMethod.GET) public @ResponseBody CustomEmployeeResponse getAll() {
	   logger.debug("Received request to get all Employee");
	   
	   // Retrieve all users from the service 
	   List<Rxmaster> employee = employeeServiceInter.getAll();
	   
	   // Initialize our custom user response wrapper 
	   CustomEmployeeResponse response = new CustomEmployeeResponse();
	   
	   // Assign the result from the service to this response
	   response.setRows(employee);
	   
	   // Assign the total number of records found. This is used for paging
	   response.setRecords( String.valueOf(employee.size()) );
	   
	   // Since our service is just a simple service for teaching purposes 
	   // We didn't really do any paging. But normally your DAOs or your persistence layer should support this 
	   // Assign a dummy page 
	   response.setPage( "1" );
	   
	   // Same. Assign a dummy total pages 
	   response.setTotal( "10" );
	   
	   // Return the response 
	   // Spring will automatically convert our CustomUserResponse as JSON object. 
	   // This is triggered by the @ResponseBody annotation. 
	   // It knows this because the JqGrid has set the headers to accept JSON format when it made a request 
	   // Spring by default uses Jackson to convert the object to JSON 
	   return response; 
	} */

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAll(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "employeeActive", required = false) Integer theInactive,
			HttpSession session, HttpServletResponse response,HttpServletRequest request)
			throws IOException, MessagingException {

		String aEmployeeID = "employee";
		String aRolodexWhere = "WHERE isEmployee = 1";
		int aTotalCount = itsEmployeeService.getRecordCountEmployee(
				aRolodexWhere, theInactive);
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		TsUserLogin aUserLogin = null;
		CustomResponse aResponse = null;
		try {
			aUserLogin = new TsUserLogin();
			aResponse = new CustomResponse();
			aUserLogin.setEmployeeperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, aEmployeeID);
			int theFrom, theTo;
			if(theRows==null){
				theRows=50;
			}
			theTo = (theRows * thePage);
			theFrom = theTo - theRows;
			if (theTo > aTotalCount)
				theTo = aTotalCount;
			itsLogger.debug("Retriving all the data of customer");
			List<?> aCustomer = itsEmployeeService.getEmployees(theFrom, theTo,
					theInactive);
			aResponse.setRows(aCustomer);
			aResponse.setRecords(String.valueOf(aCustomer.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount
					/ (double) theRows));
		} catch (UserException e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		} catch (Exception e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			sendTransactionException("<b>Tracking ID:</b> Employeedetails","Employee",e,session,request);
		}
		return aResponse;
	}

	/**
	 * Edit the current user.
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	CustomGenericResponse edit(@RequestParam("id") int theId,
			@RequestParam("firstName") String theFirstName,
			@RequestParam("searchName") String theLastName,
			@RequestParam("middleInitial") String theMiddleInitial,
			@RequestParam("phone1") String thePhone,HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		itsLogger.debug("Received request to edit user");
		// Construct our user object
		// Assign the values from the parameters
		Rxmaster aEmployee = new Rxmaster();
		aEmployee.setRxMasterId(theId);
		aEmployee.setFirstName(theFirstName);
		aEmployee.setSearchName(theLastName);
		aEmployee.setMiddleInitial(theMiddleInitial);
		aEmployee.setPhone1(thePhone);
		// Do custom validation here or in your service
		// Call service to edit
		Boolean aSuccess = itsEmployeeServiceInter.edit(aEmployee);
		// Check if successful
		if (aSuccess == true) {
			// Success. Return a custom response
			CustomGenericResponse aResponse = new CustomGenericResponse();
			aResponse.setSuccess(true);
			aResponse.setMessage("Action successful!");
			return aResponse;
		} else {
			// A failure. Return a custom response as well
			CustomGenericResponse aResponse = new CustomGenericResponse();
			aResponse.setSuccess(false);
			aResponse.setMessage("Action failure!");
			return aResponse;
		}
	}

	/**
	 * Add a new user
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	CustomGenericResponse add(@RequestParam("firstName") String theFirstName,
			@RequestParam("searchName") String theLastName,
			@RequestParam("middleInitial") String theMiddleInitial,
			@RequestParam("phone1") String phone,HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		itsLogger.debug("Received request to add a new user");
		// Construct our new user object. Take note the id is not required.
		// Assign the values from the parameters
		Rxmaster aEmployee = new Rxmaster();
		aEmployee.setFirstName(theFirstName);
		aEmployee.setSearchName(theLastName);
		aEmployee.setMiddleInitial(theMiddleInitial);
		aEmployee.setPhone1(phone);
		// Do custom validation here or in your service
		// Call service to add
		Boolean aSuccess = itsEmployeeServiceInter.add(aEmployee);
		// Check if successful
		if (aSuccess == true) {
			// Success. Return a custom response
			CustomGenericResponse aResponse = new CustomGenericResponse();
			aResponse.setSuccess(true);
			aResponse.setMessage("Action successful!");
			return aResponse;
		} else {
			// A failure. Return a custom response as well
			CustomGenericResponse aResponse = new CustomGenericResponse();
			aResponse.setSuccess(false);
			aResponse.setMessage("Action failure!");
			return aResponse;
		}
	}

	/**
	 * Delete an existing user
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	CustomGenericResponse delete(@RequestParam("id") int theId,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		itsLogger.debug("Received request to delete an existing user");
		// Construct our user object. We just need the id for deletion.
		// Assign the values from the parameters
		Rxmaster aEmployee = new Rxmaster();
		aEmployee.setRxMasterId(theId);
		aEmployee.setFirstName("");
		aEmployee.setSearchName("");
		// Do custom validation here or in your service
		// Call service to add
		Boolean aSuccess = itsEmployeeServiceInter.delete(theId);
		// Check if successful
		if (aSuccess == true) {
			// Success. Return a custom response
			CustomGenericResponse aResponse = new CustomGenericResponse();
			aResponse.setSuccess(true);
			aResponse.setMessage("Action successful!");
			return aResponse;
		} else {
			// A failure. Return a custom response as well
			CustomGenericResponse aResponse = new CustomGenericResponse();
			aResponse.setSuccess(false);
			aResponse.setMessage("Action failure!");
			return aResponse;
		}
	}

	@RequestMapping(value = "/employeecontact", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse employeeContact(
			@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		itsLogger.debug("Sales rep id: " + theRolodexId);
		Rxcontact aRxcontact = new Rxcontact();
		aRxcontact.setRxMasterId(theRolodexId);
		ArrayList<Rxcontact> aRxcontactsList = (ArrayList<Rxcontact>) itsEmployeeService
				.getEmployeeContactDetails(aRxcontact);
		CustomResponse customResponse = new CustomResponse();
		customResponse.setRows(aRxcontactsList);
		customResponse.setRecords(String.valueOf(aRxcontactsList.size()));
		customResponse.setPage(1);
		return customResponse;
	}

	@RequestMapping(value = "/employeeaddress", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse customerAddress(
			@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {

		itsLogger.debug("Sales rep id: " + theRolodexId);
		Rxaddress aRxaddress = new Rxaddress();
		aRxaddress.setRxMasterId(theRolodexId);
		ArrayList<Rxaddress> aRxaddressList = (ArrayList<Rxaddress>) itsEmployeeService
				.getEmployeeAddressDetails(aRxaddress);
		CustomResponse customResponse = new CustomResponse();
		customResponse.setRows(aRxaddressList);
		customResponse.setRecords(String.valueOf(aRxaddressList.size()));
		customResponse.setPage(1);
		return customResponse;
	}

	@RequestMapping(value = "/employeejournal", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse employeeJournal(
			@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {

		itsLogger.debug("Sales rep id: " + theRolodexId);
		RxJournal aRxJounal = new RxJournal();
		aRxJounal.setRxMasterId(theRolodexId);
		ArrayList<RxJournal> aRxjournalsList = (ArrayList<RxJournal>) itsEmployeeService
				.getRxJournalDetails(aRxJounal);
		CustomResponse customResponse = new CustomResponse();
		customResponse.setRows(aRxjournalsList);
		customResponse.setRecords(String.valueOf(aRxjournalsList.size()));
		customResponse.setPage(1);
		return customResponse;
	}

	@RequestMapping(value = "/CSRList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getCSRList(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search CSRList");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getCSRList(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/salesMGR", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getSalesMGRList(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search salesMGR Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getSalesMGRList(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/projectManager", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getProjectManagerList(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search Project Manager Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getProjectManagerList(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/engineer", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getEngineerList(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search Engineer Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getEngineerList(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/quotes", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getQuotesList(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search quotes Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getQuotesList(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/paymentType", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getPaymentTermType(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search paymentType Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getPaymentTermType(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/customerType", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getCustomerType(@RequestParam("term") String salesRep) {
		itsLogger.debug("Received request to get search customerType Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getCustomerType(salesRep);
		return aSales;
	}

	@RequestMapping(value = "/warehouseType", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getWarehouseType(@RequestParam("term") String theSalesRep) {
		itsLogger.debug("Received request to get search warehouseType Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsEmployeeServiceInter
				.getWarehouseType(theSalesRep);
		return aSales;
	}

	@RequestMapping(value = "/addNewEmployeeList", method = RequestMethod.GET)
	public @ResponseBody
	Rxmaster addEmployee(
			@RequestParam(value = "employeeName", required = false) String theEmployeeName,
			@RequestParam(value = "employeeName1", required = false) String thefirstName,
			@RequestParam(value = "address1Name", required = false) String theAddress1,
			@RequestParam(value = "address2Name", required = false) String theAddress2,
			@RequestParam(value = "stateCodeName", required = false) String theState,
			@RequestParam(value = "cityNameListName", required = false) String theCity,
			@RequestParam(value = "pinCodeName", required = false) String thePinCode,
			@RequestParam(value = "USPhoneNumber", required = false) String thePhone1,
			@RequestParam(value = "USPhone_Number", required = false) String thePhone2,
			@RequestParam(value = "fax", required = false) String theFax,
			@RequestParam(value = "officeExt", required = false) String theOfficeExt,
			@RequestParam(value = "employeeCheckName", required = false) boolean theIsEmployee,
			@RequestParam(value = "aActiveEmployee", required = false) boolean theActive,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		itsLogger.debug("Add New Employee");
		Rxmaster aEmployee = new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		Rxmaster aNewEmployee = null;
		UserBean aUserBean=null;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			aEmployee.setName(theEmployeeName);
			boolean inActive = false;
			boolean IsStreet = false;
			boolean IsMailing = false;
			boolean IsBillTo = true;
			boolean IsShipTo = true;
			theIsEmployee = true;
			aEmployee.setFirstName(thefirstName);
			String asearchName = theEmployeeName.replace(" ", "");
			if (asearchName.length() > 10) {
				String asearchText = asearchName.substring(0, 10);
				aEmployee.setSearchName(asearchText.toUpperCase());
			} else {
				aEmployee.setSearchName(asearchName.toUpperCase());
			}
			aEmployee.setInActive(theActive);
			aEmployee.setPhone1(thePhone1);
			aEmployee.setPhone2(thePhone2);
			aEmployee.setPhone3(theOfficeExt);
			aEmployee.setFax(theFax);
			aEmployee.setIsEmployee(theIsEmployee);
			aRxaddress.setName(theEmployeeName);
			if (theAddress1 != null && !theAddress1.equalsIgnoreCase("")) {
				String aAddress1 = theAddress1.trim();
				aRxaddress.setAddress1(aAddress1);
			}
			if (theAddress2 != null && !theAddress2.equalsIgnoreCase("")) {
				String aAddress2 = theAddress2.trim();
				aRxaddress.setAddress2(aAddress2);
			}
			if (theCity != null && !theCity.equalsIgnoreCase("")) {
				String aCity = theCity.trim();
				aRxaddress.setCity(aCity);
			}
			if (theState != null && !theState.equalsIgnoreCase("")) {
				String aState = theState.trim();
				aRxaddress.setState(aState);
			}
			aRxaddress.setZip(thePinCode);
			aRxaddress.setInActive(inActive);
			aRxaddress.setIsMailing(IsMailing);
			aRxaddress.setIsShipTo(IsShipTo);
			aRxaddress.setIsStreet(IsStreet);
			aRxaddress.setIsBillTo(IsBillTo);
			aRxaddress.setOtherBillTo(2);
			aRxaddress.setOtherShipTo(3);
			aRxaddress.setIsDefault(true);
			aNewEmployee = itsEmployeeServiceInter.addNewEmployee(aEmployee,
					aRxaddress);
			itsEmployeeService.updateChildTableBaseonChkbx(aNewEmployee.getRxMasterId(), aUserBean.getUserId());
			/*if (aNewEmployee != null && aNewEmployee.getRxMasterId() != null) {
				Emmaster aemmaster = new Emmaster();
				aemmaster.setEmMasterId(aNewEmployee.getRxMasterId());
				itsEmployeeServiceInter.addemMaster(aemmaster);

			}*/
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>Employee ID:</b> "+aNewEmployee.getRxMasterId(),"Employee",e,session,request);
		}
		return aNewEmployee;
	}

	@RequestMapping(value = "/deleteEmployeeDetail", method = RequestMethod.POST)
	public @ResponseBody
	boolean deleteEmployee(
			@RequestParam(value = "employeeID", required = false) Integer theRxMasterId,
			HttpSession session, HttpServletResponse response,HttpServletRequest request)
			throws CompanyException, IOException, MessagingException {
		Rxmaster aEmployee = new Rxmaster();
		try {
			aEmployee.setRxMasterId(theRxMasterId);
			itsEmployeeServiceInter.deleteEmployeeDetails(aEmployee);
		} catch (CompanyException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>Employee ID:</b> "+theRxMasterId,"Employee",e,session,request);
		}
		return true;
	}

	@RequestMapping(value = "/employeeCommisions", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse employeeCommisions(
			@RequestParam(value = "endPeriodID", required = false) Integer endPeriodID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		CustomResponse customResponse = new CustomResponse();
		try {
			ArrayList<EmMasterBean> aEmmasters = (ArrayList<EmMasterBean>) itsEmployeeService
					.getEmployeeCommissions(endPeriodID);
			customResponse.setRows(aEmmasters);
			customResponse.setRecords(String.valueOf(aEmmasters.size()));
			customResponse.setPage(1);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>endPeriodID :</b> "+endPeriodID,"Employee",e,session,request);
		}
		return customResponse;
	}

	
	@RequestMapping(value="/generateEmployeeCommissionsSheet",method = RequestMethod.GET)
	public @ResponseBody void generateEmployeeCommissionsSheet(
			@RequestParam(value = "endperioddate") String endperioddate,
			@RequestParam(value = "periodID") Integer ecPeriodID,
			HttpServletResponse theResponse, HttpServletRequest theRequest,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) 
					throws IOException, MessagingException, SQLException{

		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/CommissionSheet.jrxml");
			con = itspdfService.connectionForJasper();
			
			params.put("ecPeriod", ecPeriodID);
			params.put("tillDate", endperioddate);
		
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
		}
		catch (JRException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>EcPeriodID:</b>"+ecPeriodID,"EmployeeCommission",e,session,theRequest);
		}
		 catch (SQLException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>EcPeriodID:</b>"+ecPeriodID,"EmployeeCommission",e,session,theRequest);
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
		
		
			
	
	@RequestMapping(value="/generateEmployeeCommissionsCSV",method = RequestMethod.GET)
	public @ResponseBody void generateEmployeeCommissionsCSV(
			@RequestParam(value = "endperioddate") String endperioddate,
			@RequestParam(value = "ecStatementID") Integer ecStmtID,
			HttpServletResponse theResponse, HttpServletRequest theRequest,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException, SQLException{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		String As_Of="";
		String Commission1="";
		String Commission2="";
		String Commission3="";
		String Commission4="";
		String Adjustment1="";
		String Adjustment2="";
		String Adjustment3="";
		String Adjustment4="";
		BigDecimal Commission1_Value=null;
		BigDecimal Commission2_Value= null;
		BigDecimal Commission3_Value= null;
		BigDecimal Commission4_Value= null;
		BigDecimal Adjustment1_Value= null;
		BigDecimal Adjustment2_Value= null;
		BigDecimal Adjustment3_Value= null;
		BigDecimal Adjustment4_Value=null;
		BigDecimal Commission_Sum=null;
		BigDecimal Adjustment_Sum=null;
		BigDecimal openingBalance= null;
	    BigDecimal netChange= null;
	    BigDecimal payment= null;
	    BigDecimal closingBal= null;
	    BigDecimal netChngOpenBal= null;
	    BigDecimal grossTotal= null;
	    BigDecimal paidTotal= null;
		
	    String formatCommission1_Value=null;
	    String formatCommission2_Value= null;
	    String formatCommission3_Value= null;
	    String formatCommission4_Value= null;
	    String formatAdjustment1_Value= null;
	    String formatAdjustment2_Value= null;
	    String formatAdjustment3_Value= null;
	    String formatAdjustment4_Value=null;
	    String formatCommission_Sum=null;
	    String formatAdjustment_Sum=null;
	    String formatopeningBalance= null;
	    String formatnetChange= null;
	    String formatpayment= null;
	    String formatclosingBal= null;
	    String formatnetChngOpenBal= null;
	    String formatGrossTotal= null;
	    String formatPaidTotal= null;
	    Connection connection = null;
	    ConnectionProvider con =null;
		Ecstatement aEcstatement = new Ecstatement();
		List<String> addlist=new ArrayList<String>();
		addlist.add("AdjustmentDescription1");
		addlist.add("AdjustmentDescription2");
		addlist.add("AdjustmentDescription3");
		addlist.add("AdjustmentDescription4");
		addlist.add("CommissionDescription1");
		addlist.add("CommissionDescription2");
		addlist.add("CommissionDescription3");
		addlist.add("CommissionDescription4");
		
		
		String str = "";
		ArrayList<Sysvariable> sysvariablelist;
		try {
			sysvariablelist = itsUserService.getInventorySettingsDetails(addlist);
				Adjustment1=sysvariablelist.get(0).getValueString();
				Adjustment2=sysvariablelist.get(1).getValueString();
				Adjustment3=sysvariablelist.get(2).getValueString();
				Adjustment4=sysvariablelist.get(3).getValueString(); 
				Commission1=sysvariablelist.get(4).getValueString();
				Commission2=sysvariablelist.get(5).getValueString();
				Commission3=sysvariablelist.get(6).getValueString();
				Commission4=sysvariablelist.get(7).getValueString();
				 
				aEcstatement = (Ecstatement) itsEmployeeServiceInter.getEmployeeCommissionsStatement(ecStmtID).get(0);
				
				Map<String,BigDecimal> aStatementTotals = new HashMap<String, BigDecimal>();
				aStatementTotals = itsEmployeeServiceInter.getCommissionsStatementTotals(aEcstatement.getEcStatementId());
				grossTotal = aStatementTotals.get("profitTotal");
				paidTotal = aStatementTotals.get("amounDueTotal");
				if(paidTotal==null){
					paidTotal = new BigDecimal("0.0000");
				}
				 Commission1_Value= aEcstatement.getJobCommissions();
				 Commission2_Value= new BigDecimal(0.00);
				 Commission3_Value= new BigDecimal(0.00);
				 Commission4_Value= new BigDecimal(0.00);
				 Adjustment1_Value= aEcstatement.getRepDeduct1()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct1();
				 Adjustment2_Value= aEcstatement.getRepDeduct2()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct2();
				 Adjustment3_Value= aEcstatement.getRepDeduct3()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct3();
				 Adjustment4_Value= aEcstatement.getRepDeduct4()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct4();
				 Commission_Sum=Commission1_Value.add(Commission2_Value).add(Commission3_Value).add(Commission4_Value);
				 Adjustment_Sum=Adjustment1_Value.add(Adjustment2_Value).add(Adjustment3_Value).add(Adjustment4_Value);
				 itsLogger.info("Sum of Commissions:"+Commission_Sum);
				 openingBalance= aEcstatement.getOpeningBalance();
				 netChange= Commission_Sum.add(Adjustment_Sum);
				 netChngOpenBal = openingBalance.add(netChange);
				 payment= aEcstatement.getPayment();
				 closingBal= netChngOpenBal.subtract(payment);
					
				 DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
				 formatSymbols.setDecimalSeparator('.');
     				formatSymbols.setGroupingSeparator(' ');
					String strange = "#,##0.##";
					DecimalFormat df = new DecimalFormat(strange, formatSymbols);
					//df.setGroupingSize(4);
					formatopeningBalance = "$ ";
					formatopeningBalance = formatopeningBalance +""+df.format(ReportUtils.truncateDecimal(openingBalance,2).doubleValue());
					formatCommission1_Value="$ ";
					formatCommission1_Value = formatCommission1_Value+df.format(ReportUtils.truncateDecimal(Commission1_Value,2).doubleValue());
					formatCommission2_Value="$ ";
					formatCommission2_Value = formatCommission2_Value+df.format(ReportUtils.truncateDecimal(Commission2_Value,2).doubleValue());
					formatCommission3_Value="$ ";
					formatCommission3_Value = formatCommission3_Value+df.format(ReportUtils.truncateDecimal(Commission3_Value,2).doubleValue());
					formatCommission4_Value="$ ";
					formatCommission4_Value = formatCommission4_Value+df.format(ReportUtils.truncateDecimal(Commission4_Value,2).doubleValue());
					formatAdjustment1_Value = "$ ";
					formatAdjustment1_Value = formatAdjustment1_Value+df.format(ReportUtils.truncateDecimal(Adjustment1_Value,2).doubleValue());
					formatAdjustment2_Value = "$ ";
					formatAdjustment2_Value = formatAdjustment2_Value+df.format(ReportUtils.truncateDecimal(Adjustment2_Value,2).doubleValue());
					formatAdjustment3_Value = "$ ";
					formatAdjustment3_Value = formatAdjustment3_Value+df.format(ReportUtils.truncateDecimal(Adjustment3_Value,2).doubleValue());
					formatAdjustment4_Value = "$ ";
					formatAdjustment4_Value = formatAdjustment4_Value+df.format(ReportUtils.truncateDecimal(Adjustment4_Value,2).doubleValue());
					formatCommission_Sum = "$ ";
					formatCommission_Sum = formatCommission_Sum+df.format(ReportUtils.truncateDecimal(Commission_Sum,2).doubleValue());
				    formatAdjustment_Sum = "$ ";
				    formatAdjustment_Sum = formatAdjustment_Sum+df.format(ReportUtils.truncateDecimal(Adjustment_Sum,2).doubleValue());
				    formatnetChange = "$ ";
				    formatnetChange = formatnetChange+df.format(ReportUtils.truncateDecimal(netChange,2).doubleValue());
				    formatpayment = "$ ";
				    formatpayment = formatpayment+df.format(ReportUtils.truncateDecimal(payment,2).doubleValue());
				    formatclosingBal = "$ ";
				    formatclosingBal = formatclosingBal+df.format(ReportUtils.truncateDecimal(closingBal,2).doubleValue());
				    formatnetChngOpenBal = "$ ";
				    formatnetChngOpenBal = formatnetChngOpenBal+df.format(ReportUtils.truncateDecimal(netChngOpenBal,2).doubleValue());
				    formatGrossTotal= "$ ";
				    formatGrossTotal = formatGrossTotal+df.format(ReportUtils.truncateDecimal(grossTotal,2));
				    formatPaidTotal= "$ ";
				    formatPaidTotal = formatPaidTotal+df.format(ReportUtils.truncateDecimal(paidTotal,2));
				    itsLogger.info("GrossTotal::PaidTotal="+formatGrossTotal+"::"+formatPaidTotal);
					itsLogger.info("Opening Balance:"+ReportUtils.truncateDecimal(openingBalance,2));				 
			for(Sysvariable theSysvariable:sysvariablelist){
				 str += theSysvariable.getValueString()+",";
			}
		} catch (UserException e1) {
			e1.printStackTrace();
			sendTransactionException("<b>ecStatementID :</b> "+ecStmtID,"Employee",e1,session,request);
		}
			
		
		try{
			if(endperioddate!=null && endperioddate!="" && endperioddate.length()!=0){
				As_Of=endperioddate;
			}
			/* ID#455 */
			String path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/EmployeeJobCommissionCSV.jrxml");
			/*String path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/EmployeeJobCommission.jrxml");*/
			Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
			String filename="CommissionCoverSheet_"+ft.format(dNow)+".xls";
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("As_Of", As_Of);
			params.put("labels", str);
			params.put("ecStatementID", aEcstatement.getEcStatementId());
			params.put("Adjustment1", Adjustment1);
			params.put("Adjustment2", Adjustment2);
			params.put("Adjustment3", Adjustment3);
			params.put("Adjustment4", Adjustment4);
			params.put("Commission1", Commission1);
			params.put("Commission2", Commission2);
			params.put("Commission3", Commission3);
			params.put("Commission4", Commission4);
			params.put("Commission1_Value", formatCommission1_Value);
			params.put("Commission2_Value", formatCommission2_Value);
			params.put("Commission3_Value", formatCommission3_Value);
			params.put("Commission4_Value", formatCommission4_Value);
			params.put("Adjustment1_Value", formatAdjustment1_Value);
			params.put("Adjustment2_Value", formatAdjustment2_Value);
			params.put("Adjustment3_Value", formatAdjustment3_Value);
			params.put("Adjustment4_Value", formatAdjustment4_Value);
			params.put("Commission_Sum", formatCommission_Sum);
			params.put("Adjustment_Sum", formatAdjustment_Sum);
			params.put("openingBalance", formatopeningBalance);
			params.put("netChange", formatnetChange);
			params.put("payment", formatpayment);
			params.put("netChngOpenBal", formatnetChngOpenBal);
			params.put("closingBal", formatclosingBal);
			params.put("EmployeeName", aEcstatement.getRepName());
			params.put("comments", aEcstatement.getComment());
			params.put("Total_Gross", formatGrossTotal);
			params.put("Total_Paid", formatPaidTotal);
			connection = con.getConnection();
			
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			Integer returnvalue=itsEmployeeServiceInter.CheckEmployeeCommissionCount(aEcstatement.getEcStatementId());
			itsLogger.info("returnvalue===="+returnvalue);
			/*String query = "SELECT 'emptyrow' AS JobNumber, 'emptyrow' AS Description,0.00 AS EstimatedProfit,0.00 AS ProfitToDate, 0.00 AS ProfitPaidOnToDate, 0.00 AS  ProfitCurrent,0.00 AS  CommissionRate, 0.00 AS CommissionToDate, 0.00 AS PaidToDate, 0.00 AS AmountDue,'empty' AS Name, 'empty' AS FirstName, 0 AS rxCustomerID ,(SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo FROM DUAL";*/
			String query = "SELECT 0 as ExpjoMasterID, 0 as ExprxMasterID, 0 AS joReleaseID2, 0 as joReleaseID, 0 as rxMasterID, "
					+ "'emptyrow' as Name, 'emptyrow' as FirstName, 'emptyrow' as JobNumber, 'emptyrow' AS JobName, "
					+ "'emptyrow' as QuickJobName, 'emptyrow' as releaseType, 0.00 as Sales,0.00 AS TotalSales, 0.00 AS TotalProfit, "
					+ "'emptyrow' as Reference, now() as DatePaid, 0.00 as Profit, 0.00 as CommissionRate, 0.00 as AmountDue,"
					+ "0.00 AS Gross, (SELECT headertext FROM tsUserSetting) AS header, "
					+ "(SELECT companylogo FROM tsUserSetting) AS logo FROM DUAL"; 
			if(returnvalue>0){
			/*
			 query = "SELECT joMaster.JobNumber, joMaster.Description, joMaster.EstimatedProfit, ecJobs.ProfitToDate, ecJobs.ProfitPaidOnToDate, ecJobs.ProfitToDate-ecJobs.ProfitPaidOnToDate AS ProfitCurrent, ecJobs.CommissionRate, ecJobs.CommissionToDate, ecJobs.PaidToDate, ecJobs.AmountDue, rxMaster.Name, rxMaster.FirstName, joMaster.rxCustomerID ,(SELECT headertext FROM tsUserSetting) AS header,"
					+"(SELECT companylogo FROM tsUserSetting) AS logo FROM rxMaster RIGHT JOIN (joMaster RIGHT JOIN ecJobs ON joMaster.joMasterID = ecJobs.joMasterID) ON rxMaster.rxMasterID = joMaster.rxCustomerID "
					+"WHERE ecJobs.ecStatementID= "+aEcstatement.getEcStatementId()+" ORDER BY rxMaster.Name, rxMaster.FirstName, joMaster.Description";
			*/
				String fromDate = As_Of;
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				Date dtt = df.parse(fromDate);
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		        String date = DATE_FORMAT.format(dtt);
				
		        query = "SELECT ecInvoicePeriod.ExpjoMasterID, ecInvoicePeriod.ExprxMasterID, ecInvoicePeriod.joReleaseID AS joReleaseID2, "
						+ "joReleaseDetail.joReleaseID, rxMaster.rxMasterID,  TRIM(BOTH ' ' FROM rxMaster.Name) AS NAME,  TRIM(BOTH ' ' FROM rxMaster.FirstName) "
						+ "AS FirstName,          (CASE             WHEN (joMaster.JobNumber IS NOT NULL)THEN TRIM(BOTH ' ' FROM joMaster.JobNumber)        "
						+ "     WHEN (joM.JobNumber IS NOT NULL) THEN TRIM(BOTH ' ' FROM joM.JobNumber)                       ELSE '-'             END)AS JobNumber, "
						+ " (CASE             WHEN (joMaster.JobNumber IS NOT NULL)THEN TRIM(BOTH ' ' FROM joMaster.Description)            "
						+ " WHEN (joM.JobNumber IS NOT NULL) THEN TRIM(BOTH ' ' FROM joM.Description)                    "
						+ "   ELSE (TRIM(BOTH ' ' FROM rxMaster.Name))             END)AS JobName,  cuInvoice.QuickJobName,  "
						+ "IFNULL(joReleaseType.Description,'Sales') AS releaseType,"
						+ "  IFNULL(ecInvoicePeriod.Sales,0.0000) AS Sales,  IFNULL(ecInvoicePeriod.Sales,0.0000) AS TotalSales, IFNULL(ecInvoicePeriod.Profit,0.0000) AS TotalProfit, ecInvoicePeriod.Reference,  "
						+ "ecInvoicePeriod.DatePaid, IFNULL(ecInvoiceRepSplit.Profit,0.0000) AS Profit, IFNULL(ecInvoiceRepSplit.CommissionRate,0.0000) AS CommissionRate,  IFNULL(ecInvoiceRepSplit.AmountDue,0.0000) AS AmountDue,"
						+ "IFNULL(cuInvoice.SubTotal,0.0000) + IFNULL(cuInvoice.Freight,0.0000) AS Gross,"
						+ "(SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo"
						+ "  FROM (joReleaseDetail RIGHT JOIN (((ecInvoiceRepSplit LEFT JOIN ecInvoicePeriod"
						+ " ON  ecInvoiceRepSplit.ecInvoicePeriodID = ecInvoicePeriod.ecInvoicePeriodID) "
						+ " LEFT JOIN rxMaster ON ecInvoicePeriod.rxCustomerID = rxMaster.rxMasterID)"
						+ "  LEFT JOIN cuInvoice ON ecInvoicePeriod.cuInvoiceID = cuInvoice.cuInvoiceID) "
						+ " ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID     )    "
						/*+ " LEFT JOIN veCommDetail veC ON ecInvoicePeriod.Reference = veC.InvoiceNumber  "*/
						+ " LEFT JOIN veCommDetail veC ON ecInvoicePeriod.ecInvoicePeriodID = veC.ecInvoicePeriodID  "
						+ " LEFT JOIN joReleaseDetail joRD ON joRD.joReleaseDetailID = veC.joReleaseDetailID  "
						+ " LEFT JOIN joRelease joR ON joR.joReleaseID = joRD.joReleaseID   "
						+ " LEFT JOIN joMaster joM ON joM.joMasterID = joR.joMasterID "
						+ " LEFT JOIN joMaster ON joReleaseDetail.joMasterID = joMaster.joMasterID"
						+ " LEFT JOIN joReleaseType ON ecInvoicePeriod.ReleaseType = joReleaseType.joReleaseTypeID "
						+ " WHERE ecInvoiceRepSplit.ecStatementID = "
						+ aEcstatement.getEcStatementId()
						+ " AND ecInvoiceRepSplit.AmountDue <> 0.0000  AND ecInvoicePeriod.Sales <> 0.0000 "
						+ " ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";
				/*+ " AND ecInvoiceRepSplit.AmountDue != 0.0000 AND ecInvoicePeriod.DatePaid<='"+date+"'  ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";*/
			itsLogger.info("Commission Query:"+query);
			}
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			ReportService.dynamicReportCall(response,params,"xls",jd,filename,connection);
		}catch (Exception e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>ecStatementID :</b> "+ecStmtID,"Employee",e,session,request);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value="/viewEmployeeCommissionStatement",method = RequestMethod.GET)
	public @ResponseBody void viewEmployeeCommissionStatement(
			@RequestParam(value = "endperioddate") String endperioddate,
			@RequestParam(value = "ecStatementID") Integer ecStmtID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException, SQLException{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		String As_Of="";
		String Commission1="";
		String Commission2="";
		String Commission3="";
		String Commission4="";
		String Adjustment1="";
		String Adjustment2="";
		String Adjustment3="";
		String Adjustment4="";
		BigDecimal Commission1_Value=null;
		BigDecimal Commission2_Value= null;
		BigDecimal Commission3_Value= null;
		BigDecimal Commission4_Value= null;
		BigDecimal Adjustment1_Value= null;
		BigDecimal Adjustment2_Value= null;
		BigDecimal Adjustment3_Value= null;
		BigDecimal Adjustment4_Value=null;
		BigDecimal Commission_Sum=null;
		BigDecimal Adjustment_Sum=null;
		BigDecimal openingBalance= null;
	    BigDecimal netChange= null;
	    BigDecimal payment= null;
	    BigDecimal closingBal= null;
	    BigDecimal netChngOpenBal= null;
	    BigDecimal grossTotal= null;
	    BigDecimal paidTotal= null;
		
	    String formatCommission1_Value=null;
	    String formatCommission2_Value= null;
	    String formatCommission3_Value= null;
	    String formatCommission4_Value= null;
	    String formatAdjustment1_Value= null;
	    String formatAdjustment2_Value= null;
	    String formatAdjustment3_Value= null;
	    String formatAdjustment4_Value=null;
	    String formatCommission_Sum=null;
	    String formatAdjustment_Sum=null;
	    String formatopeningBalance= null;
	    String formatnetChange= null;
	    String formatpayment= null;
	    String formatclosingBal= null;
	    String formatnetChngOpenBal= null;
	    String formatGrossTotal= null;
	    String formatPaidTotal= null;
	    Connection connection = null;
	    ConnectionProvider con = null;
		Ecstatement aEcstatement = new Ecstatement();
		List<String> addlist=new ArrayList<String>();
		addlist.add("AdjustmentDescription1");
		addlist.add("AdjustmentDescription2");
		addlist.add("AdjustmentDescription3");
		addlist.add("AdjustmentDescription4");
		addlist.add("CommissionDescription1");
		addlist.add("CommissionDescription2");
		addlist.add("CommissionDescription3");
		addlist.add("CommissionDescription4");
		
		
		String str = "";
		ArrayList<Sysvariable> sysvariablelist;
		try {
			sysvariablelist = itsUserService.getInventorySettingsDetails(addlist);
				Adjustment1=sysvariablelist.get(0).getValueString();
				Adjustment2=sysvariablelist.get(1).getValueString();
				Adjustment3=sysvariablelist.get(2).getValueString();
				Adjustment4=sysvariablelist.get(3).getValueString(); 
				Commission1=sysvariablelist.get(4).getValueString();
				Commission2=sysvariablelist.get(5).getValueString();
				Commission3=sysvariablelist.get(6).getValueString();
				Commission4=sysvariablelist.get(7).getValueString();
				 
				aEcstatement = (Ecstatement) itsEmployeeServiceInter.getEmployeeCommissionsStatement(ecStmtID).get(0);
				
				Map<String,BigDecimal> aStatementTotals = new HashMap<String, BigDecimal>();
				aStatementTotals = itsEmployeeServiceInter.getCommissionsStatementTotals(aEcstatement.getEcStatementId());
				grossTotal = aStatementTotals.get("profitTotal");
				paidTotal = aStatementTotals.get("amounDueTotal");
				if(paidTotal==null){
					paidTotal = new BigDecimal("0.0000");
				}
				 Commission1_Value= aEcstatement.getJobCommissions();
				 Commission2_Value= new BigDecimal(0.00);
				 Commission3_Value= new BigDecimal(0.00);
				 Commission4_Value= new BigDecimal(0.00);
				 Adjustment1_Value= aEcstatement.getRepDeduct1()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct1();
				 Adjustment2_Value= aEcstatement.getRepDeduct2()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct2();
				 Adjustment3_Value= aEcstatement.getRepDeduct3()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct3();
				 Adjustment4_Value= aEcstatement.getRepDeduct4()==null? new BigDecimal("0.0000"):aEcstatement.getRepDeduct4();
				 Commission_Sum=Commission1_Value.add(Commission2_Value).add(Commission3_Value).add(Commission4_Value);
				 Adjustment_Sum=Adjustment1_Value.add(Adjustment2_Value).add(Adjustment3_Value).add(Adjustment4_Value);
				 itsLogger.info("Sum of Commissions:"+Commission_Sum);
				 openingBalance= aEcstatement.getOpeningBalance();
				 netChange= Commission_Sum.add(Adjustment_Sum);
				 netChngOpenBal = openingBalance.add(netChange);
				 payment= aEcstatement.getPayment();
				 closingBal= netChngOpenBal.subtract(payment);
					
				 DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
				 formatSymbols.setDecimalSeparator('.');
     				formatSymbols.setGroupingSeparator(' ');
					String strange = "#,##0.##";
					DecimalFormat df = new DecimalFormat(strange, formatSymbols);
					//df.setGroupingSize(4);
					formatopeningBalance = "$ ";
					formatopeningBalance = formatopeningBalance +""+df.format(ReportUtils.truncateDecimal(openingBalance,2).doubleValue());
					formatCommission1_Value="$ ";
					formatCommission1_Value = formatCommission1_Value+df.format(ReportUtils.truncateDecimal(Commission1_Value,2).doubleValue());
					formatCommission2_Value="$ ";
					formatCommission2_Value = formatCommission2_Value+df.format(ReportUtils.truncateDecimal(Commission2_Value,2).doubleValue());
					formatCommission3_Value="$ ";
					formatCommission3_Value = formatCommission3_Value+df.format(ReportUtils.truncateDecimal(Commission3_Value,2).doubleValue());
					formatCommission4_Value="$ ";
					formatCommission4_Value = formatCommission4_Value+df.format(ReportUtils.truncateDecimal(Commission4_Value,2).doubleValue());
					formatAdjustment1_Value = "$ ";
					formatAdjustment1_Value = formatAdjustment1_Value+df.format(ReportUtils.truncateDecimal(Adjustment1_Value,2).doubleValue());
					formatAdjustment2_Value = "$ ";
					formatAdjustment2_Value = formatAdjustment2_Value+df.format(ReportUtils.truncateDecimal(Adjustment2_Value,2).doubleValue());
					formatAdjustment3_Value = "$ ";
					formatAdjustment3_Value = formatAdjustment3_Value+df.format(ReportUtils.truncateDecimal(Adjustment3_Value,2).doubleValue());
					formatAdjustment4_Value = "$ ";
					formatAdjustment4_Value = formatAdjustment4_Value+df.format(ReportUtils.truncateDecimal(Adjustment4_Value,2).doubleValue());
					formatCommission_Sum = "$ ";
					formatCommission_Sum = formatCommission_Sum+df.format(ReportUtils.truncateDecimal(Commission_Sum,2).doubleValue());
				    formatAdjustment_Sum = "$ ";
				    formatAdjustment_Sum = formatAdjustment_Sum+df.format(ReportUtils.truncateDecimal(Adjustment_Sum,2).doubleValue());
				    formatnetChange = "$ ";
				    formatnetChange = formatnetChange+df.format(ReportUtils.truncateDecimal(netChange,2).doubleValue());
				    formatpayment = "$ ";
				    formatpayment = formatpayment+df.format(ReportUtils.truncateDecimal(payment,2).doubleValue());
				    formatclosingBal = "$ ";
				    formatclosingBal = formatclosingBal+df.format(ReportUtils.truncateDecimal(closingBal,2).doubleValue());
				    formatnetChngOpenBal = "$ ";
				    formatnetChngOpenBal = formatnetChngOpenBal+df.format(ReportUtils.truncateDecimal(netChngOpenBal,2).doubleValue());
				    formatGrossTotal= "$ ";
				    formatGrossTotal = formatGrossTotal+df.format(ReportUtils.truncateDecimal(grossTotal,2));
				    formatPaidTotal= "$ ";
				    formatPaidTotal = formatPaidTotal+df.format(ReportUtils.truncateDecimal(paidTotal,2));
				    itsLogger.info("GrossTotal::PaidTotal="+formatGrossTotal+"::"+formatPaidTotal);
					itsLogger.info("Opening Balance:"+ReportUtils.truncateDecimal(openingBalance,2));				 
			for(Sysvariable theSysvariable:sysvariablelist){
				 str += theSysvariable.getValueString()+",";
			}
		} catch (UserException e1) {
			e1.printStackTrace();
			sendTransactionException("<b>ecStatementID :</b> "+ecStmtID,"Employee",e1,session,request);
		}
			
		
		try{
			if(endperioddate!=null && endperioddate!="" && endperioddate.length()!=0 && !endperioddate.equals("-Select-")){
				As_Of=endperioddate;
			}
			else
			{
				 List<Ecperiod>ecperiod = itsEmployeeServiceInter.getEcPeriodStartEnd(null);
				 
				 SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
			     String datedefault = DATE_FORMAT.format(ecperiod.get(0).getPeriodEndingDate());
			     As_Of=datedefault;
			}
			
			String path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/EmployeeJobCommission.jrxml");
			 Date dNow = new Date( );
		      SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
			String filename="CommissionCoverSheet_"+ft.format(dNow)+".pdf";
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("As_Of", As_Of);
			params.put("labels", str);
			params.put("ecStatementID", aEcstatement.getEcStatementId());
			params.put("Adjustment1", Adjustment1);
			params.put("Adjustment2", Adjustment2);
			params.put("Adjustment3", Adjustment3);
			params.put("Adjustment4", Adjustment4);
			params.put("Commission1", Commission1);
			params.put("Commission2", Commission2);
			params.put("Commission3", Commission3);
			params.put("Commission4", Commission4);
			params.put("Commission1_Value", formatCommission1_Value);
			params.put("Commission2_Value", formatCommission2_Value);
			params.put("Commission3_Value", formatCommission3_Value);
			params.put("Commission4_Value", formatCommission4_Value);
			params.put("Adjustment1_Value", formatAdjustment1_Value);
			params.put("Adjustment2_Value", formatAdjustment2_Value);
			params.put("Adjustment3_Value", formatAdjustment3_Value);
			params.put("Adjustment4_Value", formatAdjustment4_Value);
			params.put("Commission_Sum", formatCommission_Sum);
			params.put("Adjustment_Sum", formatAdjustment_Sum);
			params.put("openingBalance", formatopeningBalance);
			params.put("netChange", formatnetChange);
			params.put("payment", formatpayment);
			params.put("netChngOpenBal", formatnetChngOpenBal);
			params.put("closingBal", formatclosingBal);
			params.put("EmployeeName", aEcstatement.getRepName());
			params.put("comments", aEcstatement.getComment());
			params.put("Total_Gross", formatGrossTotal);
			params.put("Total_Paid", formatPaidTotal);
			connection = con.getConnection();
			
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			Integer returnvalue=itsEmployeeServiceInter.CheckEmployeeCommissionCount(aEcstatement.getEcStatementId());
			itsLogger.info("returnvalue===="+returnvalue);
			/*String query = "SELECT 'emptyrow' AS JobNumber, 'emptyrow' AS Description,0.00 AS EstimatedProfit,0.00 AS ProfitToDate, 0.00 AS ProfitPaidOnToDate, 0.00 AS  ProfitCurrent,0.00 AS  CommissionRate, 0.00 AS CommissionToDate, 0.00 AS PaidToDate, 0.00 AS AmountDue,'empty' AS Name, 'empty' AS FirstName, 0 AS rxCustomerID ,(SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo FROM DUAL";*/
			String query = "SELECT 0 as ExpjoMasterID, 0 as ExprxMasterID, 0 AS joReleaseID2, 0 as joReleaseID, 0 as rxMasterID, "
					+ "'emptyrow' as Name, 'emptyrow' as FirstName, 'emptyrow' as JobNumber, 'emptyrow' AS JobName, "
					+ "'emptyrow' as QuickJobName, 'emptyrow' as releaseType, 0.00 as Sales,0.00 AS TotalSales, 0.00 AS TotalProfit, "
					+ "'emptyrow' as Reference, now() as DatePaid, 0.00 as Profit, 0.00 as CommissionRate, 0.00 as AmountDue,"
					+ "0.00 AS Gross, (SELECT headertext FROM tsUserSetting) AS header, "
					+ "(SELECT companylogo FROM tsUserSetting) AS logo FROM DUAL"; 
			if(returnvalue>0){
			/*
			 query = "SELECT joMaster.JobNumber, joMaster.Description, joMaster.EstimatedProfit, ecJobs.ProfitToDate, ecJobs.ProfitPaidOnToDate, ecJobs.ProfitToDate-ecJobs.ProfitPaidOnToDate AS ProfitCurrent, ecJobs.CommissionRate, ecJobs.CommissionToDate, ecJobs.PaidToDate, ecJobs.AmountDue, rxMaster.Name, rxMaster.FirstName, joMaster.rxCustomerID ,(SELECT headertext FROM tsUserSetting) AS header,"
					+"(SELECT companylogo FROM tsUserSetting) AS logo FROM rxMaster RIGHT JOIN (joMaster RIGHT JOIN ecJobs ON joMaster.joMasterID = ecJobs.joMasterID) ON rxMaster.rxMasterID = joMaster.rxCustomerID "
					+"WHERE ecJobs.ecStatementID= "+aEcstatement.getEcStatementId()+" ORDER BY rxMaster.Name, rxMaster.FirstName, joMaster.Description";
			*/
				String fromDate = As_Of;
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				Date dtt = df.parse(fromDate);
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		        String date = DATE_FORMAT.format(dtt);
				
		        query = "SELECT ecInvoicePeriod.ExpjoMasterID, ecInvoicePeriod.ExprxMasterID, ecInvoicePeriod.joReleaseID AS joReleaseID2, "
						+ "joReleaseDetail.joReleaseID, rxMaster.rxMasterID,  TRIM(BOTH ' ' FROM rxMaster.Name) AS NAME,  TRIM(BOTH ' ' FROM rxMaster.FirstName) "
						+ "AS FirstName,          (CASE             WHEN (joMaster.JobNumber IS NOT NULL)THEN TRIM(BOTH ' ' FROM joMaster.JobNumber)        "
						+ "     WHEN (joM.JobNumber IS NOT NULL) THEN TRIM(BOTH ' ' FROM joM.JobNumber)                       ELSE '-'             END)AS JobNumber, "
						+ " (CASE             WHEN (joMaster.JobNumber IS NOT NULL)THEN TRIM(BOTH ' ' FROM joMaster.Description)            "
						+ " WHEN (joM.JobNumber IS NOT NULL) THEN TRIM(BOTH ' ' FROM joM.Description)                    "
						+ "   ELSE (TRIM(BOTH ' ' FROM rxMaster.Name))             END)AS JobName,  cuInvoice.QuickJobName,  "
						+ "IFNULL(joReleaseType.Description,'Sales') AS releaseType,"
						+ "  IFNULL(ecInvoicePeriod.Sales,0.0000) as Sales,  IFNULL(ecInvoicePeriod.Sales,0.0000) AS TotalSales, IFNULL(ecInvoicePeriod.Profit,0.0000) AS TotalProfit, ecInvoicePeriod.Reference,  "
						+ "ecInvoicePeriod.DatePaid, IFNULL(ecInvoiceRepSplit.Profit,0.0000) as Profit, IFNULL(ecInvoiceRepSplit.CommissionRate,0.0000) as CommissionRate,  IFNULL(ecInvoiceRepSplit.AmountDue,0.0000) as AmountDue,"
						+ "IFNULL(cuInvoice.SubTotal,0.0000) + IFNULL(cuInvoice.Freight,0.0000) AS Gross,"
						+ "(SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo"
						+ "  FROM (joReleaseDetail RIGHT JOIN (((ecInvoiceRepSplit LEFT JOIN ecInvoicePeriod"
						+ " ON  ecInvoiceRepSplit.ecInvoicePeriodID = ecInvoicePeriod.ecInvoicePeriodID) "
						+ " LEFT JOIN rxMaster ON ecInvoicePeriod.rxCustomerID = rxMaster.rxMasterID)"
						+ "  LEFT JOIN cuInvoice ON ecInvoicePeriod.cuInvoiceID = cuInvoice.cuInvoiceID) "
						+ " ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID     )    "
						/*+ " LEFT JOIN veCommDetail veC ON ecInvoicePeriod.Reference = veC.InvoiceNumber  "*/
						+ " LEFT JOIN veCommDetail veC ON ecInvoicePeriod.ecInvoicePeriodID = veC.ecInvoicePeriodID  "
						+ " LEFT JOIN joReleaseDetail joRD ON joRD.joReleaseDetailID = veC.joReleaseDetailID  "
						+ " LEFT JOIN joRelease joR  ON (joR.joReleaseID = joRD.joReleaseID OR joR.joReleaseID=ecInvoicePeriod.joReleaseID)  "
						+ " LEFT JOIN joMaster joM ON joM.joMasterID = joR.joMasterID "
						+ " LEFT JOIN joMaster ON joReleaseDetail.joMasterID = joMaster.joMasterID"
						+ " LEFT JOIN joReleaseType ON ecInvoicePeriod.ReleaseType = joReleaseType.joReleaseTypeID "
						+ " WHERE ecInvoiceRepSplit.ecStatementID = "
						+ aEcstatement.getEcStatementId()
						//+ " AND ecInvoiceRepSplit.AmountDue <> 0.0000  AND ecInvoicePeriod.Sales <> 0.0000 "
						+ " ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";
				/*+ " AND ecInvoiceRepSplit.AmountDue != 0.0000 AND ecInvoicePeriod.DatePaid<='"+date+"'  ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";*/
			itsLogger.info("Commission Query:"+query);
			}
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			ReportService.dynamicReportCall(response,params,"pdf",jd,filename,connection);
		}catch (Exception e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>ecStatementID :</b> "+ecStmtID,"Employee",e,session,request);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value="/getEmployeeAdjustmentToPayment",method = RequestMethod.GET)
	public @ResponseBody String getEmployeeAdjustmentToPayment(
			@RequestParam(value = "endPeriodID") Integer endPeriodID, 
			@RequestParam(value = "userLoginID") Integer userLoginID,
			@RequestParam(value="ecStatementID") Integer ecStatementID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException{
			String str = "";
			Session aSession = null;
		try{
			aSession = itsSessionFactory.openSession();
			List<String> addlist=new ArrayList<String>();
			addlist.add("AdjustmentDescription1");
			addlist.add("AdjustmentDescription2");
			addlist.add("AdjustmentDescription3");
			addlist.add("AdjustmentDescription4");
			ArrayList<Sysvariable> sysvariablelist= itsUserService.getInventorySettingsDetails(addlist);
			
			String selectQuery = "SELECT RepDeduct1, RepDeduct2, RepDeduct3, RepDeduct4,Payment, ecStatement.Comment FROM ecStatement WHERE ecStatementID="+ecStatementID+" AND ecPeriodID="+endPeriodID+" AND RepLoginID="+userLoginID;
			org.hibernate.Query aQuery = aSession.createSQLQuery(selectQuery);
			List<?> list = aQuery.list();
			Iterator<?> aIterator = list.iterator();
				
			for(Sysvariable theSysvariable:sysvariablelist){
				 str += theSysvariable.getValueString()+",";
			}
			//str = str.substring(0, str.length()-1);
			if(list != null)
			{
				if (aIterator.hasNext())
				{
					Object[] aObj = (Object[]) aIterator.next();
					str += aObj[0]+","+aObj[1]+","+aObj[2]+","+aObj[3]+","+aObj[4]+","+aObj[5];
				}				
			}
			System.out.println(" str value :: "+str);
		}catch (Exception e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>ecStatementID :</b> "+ecStatementID,"Employee",e,session,request);
		}
		return str;
	}
	
	@RequestMapping(value = "/checkPeriodExists", method = RequestMethod.POST)
	public @ResponseBody
	Integer checkPeriodExists(
			@RequestParam(value = "newPeriodDate", required = false) String newPeriodDate,
			HttpServletResponse theResponse) {
		Integer inStatus = 0;
		inStatus = itsEmployeeServiceInter.checkPeriodExists(newPeriodDate);
		return inStatus;
	}


	@RequestMapping(value = "/commisionsNewPeriod", method = RequestMethod.POST)
	public @ResponseBody
	Integer commisionsNewPeriod(
			@RequestParam(value = "newPeriodDate", required = false) String newPeriodDate,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		Ecperiod aEcperiod = new Ecperiod();
		itsLogger.info("NewPeriodDate" + newPeriodDate);
		List<Ecperiod> ecPeriodList = new ArrayList<Ecperiod>();
		Integer inStatus = 0;
		Sysvariable aSysvariable = null;
		Sysvariable jobSysvariable = null;
		try {
			aEcperiod = itsEmployeeServiceInter.addNewCommissionPeriod(newPeriodDate);
			itsLogger.info("New Period ID: " + aEcperiod.getEcPeriodId());
			if (aEcperiod.getEcPeriodId() > 0) {
				Integer insStatus = itsEmployeeServiceInter.saveNewPeriod(aEcperiod.getEcPeriodId());
				if (insStatus > 0) {
					aSysvariable = itsEmployeeServiceInter.getSysVariableDetails("CommissionSpecialCode");
					jobSysvariable = itsEmployeeServiceInter.getSysVariableDetails("InvoicesCombinedforJobs");
					ecPeriodList = itsEmployeeServiceInter.getEcPeriodStartEnd("");
					itsLogger.info("Periods Size:"+ecPeriodList.size());
					Calendar cal = Calendar.getInstance();
					String startDate ="";
					if(ecPeriodList.size()>1){
						cal.setTime(ecPeriodList.get(1).getPeriodEndingDate());
						cal.add(Calendar.DATE, 1); // minus number would decrement the days
						startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
						itsLogger.info("stDate"+ ecPeriodList.get(1).getPeriodEndingDate());
					}else{
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						String dateInString = "01-06-2004";
						Date date = sdf.parse(dateInString);
						cal.setTime(date);
						startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
						
					}
					
					String endDate = new SimpleDateFormat("yyyy-MM-dd")
							.format(ecPeriodList.get(0).getPeriodEndingDate());
					itsLogger.info("stDate"
							+ " <=>" + startDate + "\n End Date:"
							+ ecPeriodList.get(0).getPeriodEndingDate()
							+ " <=>" + endDate);
					/*Commented on 2015-07-02 Rep Functionalities
					 * if(jobSysvariable.getValueLong()!=null && jobSysvariable.getValueLong()>0){
						itsLogger.info("If Condition Sysvariable contains");
						itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
						inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Calculate can Comment
						inStatus = itsEmployeeServiceInter.calCulateJobReleaseCommissions(startDate, endDate,ecPeriodList);
						inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
						inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
						
						if (aSysvariable.getValueString() != null
								&& aSysvariable.getValueString().contains("/CJ")) { //Calculate can Comment
								inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
						 	} else {
								inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
								} //Calculate can Comment
						}else{
							itsLogger.info("else Condition Sysvariable not contains");
							itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
							inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Repeated here again check Conditions can Comment
							inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
							inStatus = itsEmployeeServiceInter.calculateFactoryCommissions(startDate, endDate,ecPeriodList); //Calculate can Comment
							inStatus = itsEmployeeServiceInter.removeInactiveEntries(startDate, endDate,ecPeriodList); //Calculate can Comment
							}
					    inStatus = itsEmployeeServiceInter.removeInactiveEntries(startDate, endDate,ecPeriodList); //Calculate can Comment
						inStatus = itsEmployeeServiceInter.calCulateOverrides(startDate, endDate,ecPeriodList); //Calculate can Comment
						inStatus = itsEmployeeServiceInter.calculateMiscJobs(startDate, endDate,ecPeriodList); //Calculate can Comment
						inStatus = itsEmployeeServiceInter.calculateCommissionTotals(startDate, endDate,ecPeriodList,"no");
						*/					
					itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
					//inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Calculate can Comment
					//inStatus = itsEmployeeServiceInter.calCulateJobReleaseCommissions(startDate, endDate,ecPeriodList);
					//On 04-11-2015 inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
					inStatus = itsEmployeeServiceInter.savePaidInvoicesFromSQLView(startDate, endDate,ecPeriodList);
					//On 04-11-2015 inStatus = itsEmployeeServiceInter.calculateAddedCostCommission(startDate, endDate, ecPeriodList);
					//On 04-11-2015 inStatus = itsEmployeeServiceInter.calculateUnpaidInvoiceAddedCost(startDate, endDate, ecPeriodList);
					//On 04-11-2015 inStatus = itsEmployeeServiceInter.calculatePartialPaidInvoiceAddedCost(startDate, endDate, ecPeriodList);
					// On 09-08-2015 inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
					// On 09-08-2015 inStatus = itsEmployeeServiceInter.calCulateOverrides(startDate, endDate,ecPeriodList); //Calculate can Comment
					// On 09-08-2015 inStatus = itsEmployeeServiceInter.calculateMiscJobs(startDate, endDate,ecPeriodList); //Calculate can Comment
					inStatus = itsEmployeeServiceInter.removeInactiveEntries(startDate, endDate,ecPeriodList); //Calculate can Comment
					inStatus = itsEmployeeServiceInter.calculateCommissionTotals(startDate, endDate,ecPeriodList,"no");
					
					/* Previous Code
					 * if(jobSysvariable.getValueLong() != null && jobSysvariable.getValueLong()>0){
					itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
					 inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList);
					inStatus = itsEmployeeServiceInter.calCulateJobReleaseCommissions(startDate, endDate,ecPeriodList);
						if (aSysvariable.getValueString() != null
								&& aSysvariable.getValueString().contains("CJ")) {
							inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
						} else {
							inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
						}
					}
					else{
						itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
						inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Repeated here again check Conditions can Comment
						inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
						inStatus = itsEmployeeServiceInter.calculateFactoryCommissions(startDate, endDate,ecPeriodList);//Calculate can Comment
						inStatus = itsEmployeeServiceInter.removeInactiveEntries(startDate, endDate,ecPeriodList);//Calculate can Comment
						}
					inStatus = itsEmployeeServiceInter.calCulateOverrides(startDate, endDate,ecPeriodList);//Calculate can Comment
					inStatus = itsEmployeeServiceInter.calculateMiscJobs(startDate, endDate,ecPeriodList);//Calculate can Comment
					inStatus = itsEmployeeServiceInter.calculateCommissionTotals(startDate, endDate,ecPeriodList,"no");
					*/
					itsLogger.info("Insert Success " + inStatus);
					

				}
			}

		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			e.printStackTrace();
			sendTransactionException("<b>TrackingID :</b> Commisions NewPeriod","Employee",e,session,request);
		}
		return inStatus;
	}
	
	

	@RequestMapping(value = "/reCalculateStatus", method = RequestMethod.POST)
	public @ResponseBody
	Integer reCalculateStatus(
			@RequestParam(value = "endDate", required = false) Integer endPeriodID) {
		List<Ecperiod> ecPeriodList = new ArrayList<Ecperiod>();
		itsLogger.info("End Period: " + endPeriodID);
		try {
			ecPeriodList = itsEmployeeServiceInter.isPeriodsExists(endPeriodID);
		} catch (EmployeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer status = 0;
		itsLogger.info("Size of List:"+ecPeriodList.size());
		if(ecPeriodList.size()>0){
			status = 1;
		}
		return status;
	}
	
	@RequestMapping(value = "/reCalculateCommission", method = RequestMethod.POST)
	public @ResponseBody
	Integer reCalculateCommission(
			@RequestParam(value = "endDate", required = false) String endPeriodID,
			@RequestParam(value = "calculatePayment", required = false) String calculatePayment,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		List<Ecperiod> ecPeriodList = new ArrayList<Ecperiod>();
		Integer inStatus = 0;
		Sysvariable aSysvariable = null;
		Sysvariable jobSysvariable = null;
		itsLogger.info("End Period: " + endPeriodID);
		if(endPeriodID==null){
			endPeriodID="";
		}
		try {
			aSysvariable = itsEmployeeServiceInter.getSysVariableDetails("CommissionSpecialCode");
			jobSysvariable = itsEmployeeServiceInter.getSysVariableDetails("InvoicesCombinedforJobs");
			ecPeriodList = itsEmployeeServiceInter.getEcPeriodStartEnd(endPeriodID);
			Calendar cal = Calendar.getInstance();
			String startDate ="";
			if(ecPeriodList.size()>1){
				cal.setTime(ecPeriodList.get(1).getPeriodEndingDate());
				cal.add(Calendar.DATE, 1); // minus number would decrement the days
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				itsLogger.info("stDate"+ ecPeriodList.get(1).getPeriodEndingDate());
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String dateInString = "01-06-2004";
				Date date = sdf.parse(dateInString);
				cal.setTime(ecPeriodList.get(0).getPeriodEndingDate());
				cal.add(Calendar.DAY_OF_YEAR, -30);
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				
			}
			
			String endDate = new SimpleDateFormat("yyyy-MM-dd")
					.format(ecPeriodList.get(0).getPeriodEndingDate());
			itsLogger.info("stDate"
					+ " <=>" + startDate + "\n End Date:"
					+ ecPeriodList.get(0).getPeriodEndingDate()
					+ " <=>" + endDate);
			
			/* Rep Functionalities Replication
			 * if(jobSysvariable.getValueLong()!=null && jobSysvariable.getValueLong()>0){
				itsLogger.info("If Condition Sysvariable contains");
				itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
				inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Calculate can Comment
				inStatus = itsEmployeeServiceInter.calCulateJobReleaseCommissions(startDate, endDate,ecPeriodList);
				inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
				inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
				
				if (aSysvariable.getValueString() != null
						&& aSysvariable.getValueString().contains("/CJ")) { //Calculate can Comment
						inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
				 	} else {
						inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
						} //Calculate can Comment
				}else{
					itsLogger.info("else Condition Sysvariable not contains");
					itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
					inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Repeated here again check Conditions can Comment					
					inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
					inStatus = itsEmployeeServiceInter.calculateFactoryCommissions(startDate, endDate,ecPeriodList); //Calculate can Comment
					inStatus = itsEmployeeServiceInter.removeInactiveEntries(startDate, endDate,ecPeriodList); //Calculate can Comment
					}
				*/
				itsEmployeeServiceInter.deleteCommissionsDetails(ecPeriodList.get(0).getEcPeriodId());
				//inStatus = itsEmployeeServiceInter.calCulateJobCommissions(startDate, endDate,ecPeriodList); //Calculate can Comment
				//inStatus = itsEmployeeServiceInter.calCulateJobReleaseCommissions(startDate, endDate,ecPeriodList);
				//On 03-11-2015 inStatus = itsEmployeeServiceInter.calculatePaidInvoices(startDate, endDate,ecPeriodList);
				inStatus = itsEmployeeServiceInter.savePaidInvoicesFromSQLView(startDate, endDate,ecPeriodList);
				//On 03-11-2015 inStatus = itsEmployeeServiceInter.calculateAddedCostCommission(startDate, endDate, ecPeriodList);
				//On 03-11-2015 inStatus = itsEmployeeServiceInter.calculateUnpaidInvoiceAddedCost(startDate, endDate, ecPeriodList);
				//On 03-11-2015 inStatus = itsEmployeeServiceInter.calculatePartialPaidInvoiceAddedCost(startDate, endDate, ecPeriodList);
				// On 09-08-2015 inStatus = itsEmployeeServiceInter.calculateWarehouseInvoices(startDate, endDate,ecPeriodList);
				// On 09-08-2015 inStatus = itsEmployeeServiceInter.calCulateOverrides(startDate, endDate,ecPeriodList); //Calculate can Comment
				// On 09-08-2015 inStatus = itsEmployeeServiceInter.calculateMiscJobs(startDate, endDate,ecPeriodList); //Calculate can Comment
				inStatus = itsEmployeeServiceInter.removeInactiveEntries(startDate, endDate,ecPeriodList); //Calculate can Comment
				inStatus = itsEmployeeServiceInter.calculateCommissionTotals(startDate, endDate,ecPeriodList,calculatePayment);
				
			itsLogger.info("Insert Success " + inStatus);
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>ReCalculate Commission :</b> reCalculateCommission","Employee",e,session,request);
		}
		return inStatus;
	}
	
	@RequestMapping(value = "/reverseCommissionPeriod", method = RequestMethod.POST)
	public @ResponseBody
	Integer reverseCommissionPeriod(
			@RequestParam(value = "endDate", required = false) String endPeriodID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		List<Ecperiod> ecPeriodList = new ArrayList<Ecperiod>();
		Integer inStatus = 0;
		Sysvariable aSysvariable = null;
		Sysvariable jobSysvariable = null;
		itsLogger.info("End Period: " + endPeriodID);
		if(endPeriodID==null){
			endPeriodID="";
		}
		try {
			aSysvariable = itsEmployeeServiceInter.getSysVariableDetails("CommissionSpecialCode");
			jobSysvariable = itsEmployeeServiceInter.getSysVariableDetails("InvoicesCombinedforJobs");
			ecPeriodList = itsEmployeeServiceInter.getEcPeriodStartEnd(endPeriodID);
			Calendar cal = Calendar.getInstance();
			String startDate ="";
			if(ecPeriodList.size()>1){
				cal.setTime(ecPeriodList.get(1).getPeriodEndingDate());
				cal.add(Calendar.DATE, 1); // minus number would decrement the days
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				itsLogger.info("stDate"+ ecPeriodList.get(1).getPeriodEndingDate());
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String dateInString = "01-06-2004";
				Date date = sdf.parse(dateInString);
				cal.setTime(ecPeriodList.get(0).getPeriodEndingDate());
				cal.add(Calendar.DAY_OF_YEAR, -30);
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				
			}
			String endDate = new SimpleDateFormat("yyyy-MM-dd")
					.format(ecPeriodList.get(0).getPeriodEndingDate());
			itsLogger.info("stDate"
					+ " <=>" + startDate + "\n End Date:"
					+ ecPeriodList.get(0).getPeriodEndingDate()
					+ " <=>" + endDate);
					inStatus = itsEmployeeServiceInter.reverseCommissionPeriod(startDate, endDate,ecPeriodList);
			itsLogger.info("Delete Success " + inStatus);
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage());
		}
		return inStatus;
	}

	@RequestMapping(value="/saveCommissionAdjustment",method = RequestMethod.POST)
	public @ResponseBody String saveCommissionAdjustment(
			@RequestParam(value = "repDeduct1") BigDecimal repDeduct1, 
			@RequestParam(value = "repDeduct2") BigDecimal repDeduct2,
			@RequestParam(value = "repDeduct3") BigDecimal repDeduct3, 
			@RequestParam(value = "repDeduct4") BigDecimal repDeduct4,
			@RequestParam(value = "payment") BigDecimal payment,
			@RequestParam(value = "commentTxt") String comment,
			@RequestParam(value = "endPeriodID") Integer periodID, 
			@RequestParam(value = "userLoginID") Integer userLoginID,
			@RequestParam(value = "ecStatementID") Integer ecStatementID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {
	String str = "";
	Session aSession = null;
	try{
	aSession = itsSessionFactory.openSession();
	//UPDATE ecStatement SET RepDeduct1=1, RepDeduct2=2, RepDeduct3=3, RepDeduct4=4, Adjustments=10 WHERE ecStatementID=1 AND RepLoginID=4;
	BigDecimal adjustment = repDeduct1.add(repDeduct2.add(repDeduct3.add(repDeduct4)));
	String updateQry = "UPDATE ecStatement as ec SET RepDeduct1="+repDeduct1+", " +
	"RepDeduct2="+repDeduct2+", RepDeduct3="+repDeduct3+", RepDeduct4="+repDeduct4+"," +
	" Adjustments="+adjustment+", Payment="+payment+", ec.Comment='"+comment+"' WHERE ecStatementID="+ecStatementID+" AND RepLoginID="+userLoginID;
	System.out.println(" updateQuery :: "+updateQry);
	int status = aSession.createSQLQuery(updateQry).executeUpdate();

	// PreparedStatement preparedStmt = aSession.prepareStatement(updateQry);
	str="success";
	}catch (Exception e) {
	e.printStackTrace();
	}finally{
	aSession.flush();
	aSession.close();
	}
	return str;
	}

	@RequestMapping(value = "/getEndingPeriodList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getCheckList(HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, JobException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Ecperiod> aEcperiodList = (ArrayList<Ecperiod>) itsEmployeeServiceInter
					.getPeriodEnding();
			if (aEcperiodList != null && aEcperiodList.size() > 0)
				map.put("endingPeriodList", aEcperiodList);
			else
				map.put("endingPeriodList", new ArrayList<Ecperiod>());
		} catch (Exception e) {
		}
		return map;
	}
	
	@RequestMapping(value = "/getEndingPeriodListfortrialBalance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getEndingPeriodListfortrialBalance(HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, JobException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		ArrayList sysInfoArrayList = new ArrayList();
		try {
			ArrayList<Cofiscalperiod> aEcperiodList = (ArrayList<Cofiscalperiod>) accountingCyclesService.getAllfiscalPeriod();
			ArrayList<Codivision> alCodivision = (ArrayList<Codivision>)itsCompanyService.getCompanyDivisions();
			if (aEcperiodList != null && aEcperiodList.size() > 0)
				map.put("endingPeriodList", aEcperiodList);
			else
				map.put("endingPeriodList", new ArrayList<Cofiscalperiod>());
			
			if (alCodivision != null && alCodivision.size() > 0)
				map.put("alCodivision", alCodivision);
			else
				map.put("alCodivision", new ArrayList<Codivision>());
			
		} catch (Exception e) {
		}
		return map;
	}

	@RequestMapping(value = "/getEndingPeriod", method = RequestMethod.POST)
	public @ResponseBody
	Integer getEndingPeriod(HttpSession session, HttpServletResponse response,HttpServletRequest request)
			throws IOException, JobException {
		Integer periodID = 0;
		try {
			periodID = itsEmployeeServiceInter.getLastEcPeriod();
		} catch (Exception e) {
		}
		return periodID;
	}

	@RequestMapping(value = "/updateCommissions", method = RequestMethod.POST)
	public @ResponseBody
	Emmaster updateCommissions(
			@RequestParam(value = "commJobProfitName", required = false) BigDecimal theCommJobProfitName,
			@RequestParam(value = "commBuySellProfitName", required = false) BigDecimal theCommBuySellProfitName,
			@RequestParam(value = "commStockOrderName", required = false) BigDecimal theCommStockOrderName,
			@RequestParam(value = "factoryCommissionName", required = false) BigDecimal theFactoryCommissionName,
			@RequestParam(value = "quotaNAme", required = false) BigDecimal theQuotaNAme,
			@RequestParam(value = "specialJobNumberName", required = false) boolean theSpecialJobNumberName,
			@RequestParam(value = "totalProfitBounsOnlyName", required = false) boolean theTotalProfitBounsOnlyName,
			@RequestParam(value = "engCommissionName", required = false) BigDecimal theEngCommissionName,
			@RequestParam(value = "prefixJobNumberNAme", required = false) String thePrefixJobNumberNAme,
			@RequestParam(value = "sequenceFollowsName", required = false) String theSequenceFollowsName,
			@RequestParam(value = "salaryDeductionsName", required = false) BigDecimal repDeduct1,
			@RequestParam(value = "insurenceDeductionsName", required = false) BigDecimal repDeduct2,
			@RequestParam(value = "otherDeductionsName", required = false) BigDecimal repDeduct3,
			@RequestParam(value = "paymentPeriodName", required = false) Integer paymentPeriod,
			@RequestParam(value = "getBounsCommissionsName", required = false) boolean theGetBounsCommissionsName,
			@RequestParam(value = "YTD0Name", required = false) BigDecimal theYTD0Name,
			@RequestParam(value = "YTD1Name", required = false) BigDecimal theYTD1Name,
			@RequestParam(value = "YTD2Name", required = false) BigDecimal theYTD2Name,
			@RequestParam(value = "YTD3Name", required = false) BigDecimal theYTD3Name,
			@RequestParam(value = "YTD4Name", required = false) BigDecimal theYTD4Name,
			@RequestParam(value = "YTD5Name", required = false) BigDecimal theYTD5Name,
			@RequestParam(value = "YTD6Name", required = false) BigDecimal theYTD6Name,
			@RequestParam(value = "YTD7Name", required = false) BigDecimal theYTD7Name,
			@RequestParam(value = "rate0Name", required = false) BigDecimal theRate0Name,
			@RequestParam(value = "rate1Name", required = false) BigDecimal theRate1Name,
			@RequestParam(value = "rate2Name", required = false) BigDecimal theRate2Name,
			@RequestParam(value = "rate3Name", required = false) BigDecimal theRate3Name,
			@RequestParam(value = "rate4Name", required = false) BigDecimal theRate4Name,
			@RequestParam(value = "rate5Name", required = false) BigDecimal theRate5Name,
			@RequestParam(value = "rate6Name", required = false) BigDecimal theRate6Name,
			@RequestParam(value = "rate7Name", required = false) BigDecimal theRate7Name,
			@RequestParam(value = "commissionID", required = false) Integer theCommissionID,
			@RequestParam(value = "dayafter", required = false) Integer dayafter,
			@RequestParam(value = "payOn", required = false) String payOn,
			@RequestParam(value = "commissionPayment", required = false) String customerInvoice,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		itsLogger.info("Employee StockOrderCommissions: "
				+ theCommStockOrderName + " customerInvoice:" + customerInvoice
				+ " dayafter:" + dayafter+" payOn:"+payOn);
		Emmaster aEmmaster = new Emmaster();
		try {
			byte aTotalProfitBounsOnlyName = (byte) (theTotalProfitBounsOnlyName ? 1
					: 0);
			aEmmaster.setCommissionJobProfit(theCommJobProfitName);
			aEmmaster.setCommissionBuySellProfit(theCommBuySellProfitName);
			aEmmaster.setCommissionFactory(theFactoryCommissionName);
			aEmmaster.setQuota(theQuotaNAme);
			aEmmaster.setProfitBonus(aTotalProfitBounsOnlyName);
			aEmmaster.setEnginComm(theEngCommissionName);
			aEmmaster.setJobNumberPrefix(thePrefixJobNumberNAme);
			aEmmaster.setJobNumberSequence(theSequenceFollowsName);
			aEmmaster.setJobNumberGenerate(theSpecialJobNumberName);
			aEmmaster.setGetsCommission(theGetBounsCommissionsName);
			aEmmaster.setCommissionInvoiceProfit(theCommStockOrderName);
			aEmmaster.setRepDeduct1(repDeduct1);
			aEmmaster.setRepDeduct2(repDeduct2);
			aEmmaster.setRepDeduct3(repDeduct3);
			aEmmaster.setPaymentPeriod(paymentPeriod);
			// aEmmaster.setRepDeduct4(repDeduct4);
			aEmmaster.setBonusComm1(theYTD0Name);
			aEmmaster.setBonusComm2(theYTD1Name);
			aEmmaster.setBonusComm3(theYTD2Name);
			aEmmaster.setBonusComm4(theYTD3Name);
			aEmmaster.setBonusComm5(theYTD4Name);
			aEmmaster.setBonusComm6(theYTD5Name);
			aEmmaster.setBonusComm7(theYTD6Name);
			aEmmaster.setBonusComm8(theYTD7Name);
			aEmmaster.setBonusLevel1(theRate0Name);
			aEmmaster.setBonusLevel2(theRate1Name);
			aEmmaster.setBonusLevel3(theRate2Name);
			aEmmaster.setBonusLevel4(theRate3Name);
			aEmmaster.setBonusLevel5(theRate4Name);
			aEmmaster.setBonusLevel6(theRate5Name);
			aEmmaster.setBonusLevel7(theRate6Name);
			aEmmaster.setBonusLevel8(theRate7Name);
			aEmmaster.setEmMasterId(theCommissionID);
			if(customerInvoice !=null && customerInvoice.equalsIgnoreCase("customerinvoice")) {
				itsLogger.info(" Commission On Customer Invoice is True");
				aEmmaster.setDayAfter(dayafter);
				aEmmaster.setCommAfterPayment(0);
			}else if(customerInvoice!=null && customerInvoice.equalsIgnoreCase("customerpayment")){
				itsLogger.info(" Commission On Customer Payment is True");
				aEmmaster.setCommAfterPayment(1);
			}
			if(payOn !=null && payOn.equalsIgnoreCase("onsellprice")){
				aEmmaster.setCommPayOn(0);
			}
			else if(payOn !=null && payOn.equalsIgnoreCase("ongrossprofit")){
				aEmmaster.setCommPayOn(1);
			}
			itsEmployeeService.updateCommissions(aEmmaster);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			e.printStackTrace();
			sendTransactionException("<b>Update Employee :</b> "+theCommissionID,"Employee",e,session,request);
		}
		return aEmmaster;
	}

	@RequestMapping(value = "/updateCommissionsGeneral", method = RequestMethod.POST)
	public @ResponseBody
	Emmaster updateCommissionsGeneral(
			@RequestParam(value = "commissionID", required = false) Integer theCommissionID,
			@RequestParam(value = "division", required = false) Integer division,
			@RequestParam(value = "empType", required = false) Byte empType,
			@RequestParam(value = "empMaritalStatus", required = false) Byte empMaritalStatus,
			@RequestParam(value = "empGender", required = false) String empGender,
			@RequestParam(value = "birthDate", required = false) String birthDate,
			@RequestParam(value = "hireDate", required = false) String hireDate,
			
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException, MessagingException {
		itsLogger.info("Employee division: "+ division + " empType:" + empType+ " empMaritalStatus:" + empMaritalStatus+" empGender:"+empGender);
		Emmaster aEmmaster = new Emmaster();
		Date dateBirth = null,dateHired = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if(birthDate!=null && !birthDate.equals("")){
				dateBirth = sdf.parse(birthDate);
			}
			if(hireDate!=null && !hireDate.equals("")){
				dateHired = sdf.parse(hireDate);
			}
			aEmmaster.setEmMasterId(theCommissionID);
			aEmmaster.setCoDivisionId(division);
			aEmmaster.setEmploymentType(empType);
			aEmmaster.setMaritalStatus(empMaritalStatus);
			aEmmaster.setSex(empGender);
			aEmmaster.setBirthDate(dateBirth);
			aEmmaster.setHireDate(dateHired);
			
			itsEmployeeService.updateCommissionsGeneral(aEmmaster);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			e.printStackTrace();
			sendTransactionException("<b>Update Employee :</b> "+theCommissionID,"Employee",e,session,request);
		}
		return aEmmaster;
	}
	
	@RequestMapping(value = "/getCoFiscalPeriodList", method = RequestMethod.POST)
	public @ResponseBody Map<String, ArrayList<?>> getgetCoFiscalPeriodListCheckList(
			HttpSession session, HttpServletResponse response,HttpServletRequest request)
			throws IOException, JobException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Cofiscalperiod> aEcperiodList = (ArrayList<Cofiscalperiod>)itsEmployeeServiceInter.getCoFiscalPeriodList();
			if(aEcperiodList != null && aEcperiodList.size() > 0)
				map.put("endingPeriodList", aEcperiodList);
			else
				map.put("endingPeriodList", new ArrayList<Ecperiod>());
		} catch (Exception e) {
		}
		return map;
	}
	
	@RequestMapping(value = "/getSysvariableDetail", method = RequestMethod.GET)
	public @ResponseBody Sysvariable getSysvariableDetail(@RequestParam(value = "sysVariableValue", required = false) String sysVariableValue,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		Sysvariable aSysvariable = null;
		try {
			itsLogger.info("New Period ID: " + sysVariableValue);
				aSysvariable = itsEmployeeServiceInter.getSysVariableDetails(sysVariableValue);
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			e.printStackTrace();
		}
		return aSysvariable;
	}
	
	@RequestMapping(value="/viewCommissionStatementProject", method = RequestMethod.GET)
	public @ResponseBody CustomResponse viewCommissionStatementProject(
			@RequestParam(value = "userLoginID" , required = false) Integer UserLoginID,
			@RequestParam(value = "PeriodID" , required = false) Integer PeriodID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request){
		CustomResponse customResponse = new CustomResponse();
		Integer ecStatementID = 0;
		try{
		itsLogger.info("viewCommissionStatementProject"+UserLoginID+" Period:"+PeriodID);
		EmMasterBean  aEmmasters = itsEmployeeService.getEmployeeCommissionStatement(UserLoginID,PeriodID);
		if(UserLoginID!=null && UserLoginID>0){
			ecStatementID = aEmmasters.getEcStatementID();
		}
		else{
			ecStatementID = 0;
		}
		ArrayList<CommissionStatementBean> commissionStmts = new ArrayList<CommissionStatementBean>();
		commissionStmts = itsEmployeeServiceInter.getCommissionStatements(ecStatementID);
		customResponse.setRows(commissionStmts);
		customResponse.setRecords(String.valueOf(commissionStmts.size()));
		//response.setPage(1);
		}catch (Exception e) {
			itsLogger.error(e.getMessage());
		}
		return customResponse;
	}
	

	@RequestMapping(value="/vieweCommissionStatement",method = RequestMethod.GET)
	public @ResponseBody void vieweCommissionStatement(
			@RequestParam(value = "employeeID") Integer employeeID,
			HttpSession session, HttpServletResponse response,HttpServletRequest request) throws SQLException{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		EmMasterBean  aEmmasters = itsEmployeeService.getEmployeeCommissionStatement(employeeID,0);
		
		List<Ecperiod> ecPeriodList = new ArrayList<Ecperiod>();
		try {
			ecPeriodList = itsEmployeeServiceInter.getEcPeriodStartEnd("");
		} catch (EmployeeException e2) {
			e2.printStackTrace();
		}
		String endDate = new SimpleDateFormat("MM/dd/yyyy")
					.format(ecPeriodList.get(0).getPeriodEndingDate());
		
		
		String As_Of="";
		String Commission1="";
		String Commission2="";
		String Commission3="";
		String Commission4="";
		String Adjustment1="";
		String Adjustment2="";
		String Adjustment3="";
		String Adjustment4="";
		BigDecimal Commission1_Value=null;
		BigDecimal Commission2_Value= null;
		BigDecimal Commission3_Value= null;
		BigDecimal Commission4_Value= null;
		BigDecimal Adjustment1_Value= null;
		BigDecimal Adjustment2_Value= null;
		BigDecimal Adjustment3_Value= null;
		BigDecimal Adjustment4_Value=null;
		BigDecimal Commission_Sum=null;
		BigDecimal Adjustment_Sum=null;
		BigDecimal openingBalance= null;
	    BigDecimal netChange= null;
	    BigDecimal payment= null;
	    BigDecimal closingBal= null;
	    BigDecimal netChngOpenBal= null;
		
	    String formatCommission1_Value=null;
	    String formatCommission2_Value= null;
	    String formatCommission3_Value= null;
	    String formatCommission4_Value= null;
	    String formatAdjustment1_Value= null;
	    String formatAdjustment2_Value= null;
	    String formatAdjustment3_Value= null;
	    String formatAdjustment4_Value=null;
	    String formatCommission_Sum=null;
	    String formatAdjustment_Sum=null;
	    String formatopeningBalance= null;
	    String formatnetChange= null;
	    String formatpayment= null;
	    String formatclosingBal= null;
	    String formatnetChngOpenBal= null;
	    Connection connection = null;
	    ConnectionProvider con = null;
		Ecstatement aEcstatement = new Ecstatement();
		List<String> addlist=new ArrayList<String>();
		addlist.add("AdjustmentDescription1");
		addlist.add("AdjustmentDescription2");
		addlist.add("AdjustmentDescription3");
		addlist.add("AdjustmentDescription4");
		addlist.add("CommissionDescription1");
		addlist.add("CommissionDescription2");
		addlist.add("CommissionDescription3");
		addlist.add("CommissionDescription4");
		
		
		String str = "";
		ArrayList<Sysvariable> sysvariablelist;
		try {
			sysvariablelist = itsUserService.getInventorySettingsDetails(addlist);
				Adjustment1=sysvariablelist.get(0).getValueString();
				Adjustment2=sysvariablelist.get(1).getValueString();
				Adjustment3=sysvariablelist.get(2).getValueString();
				Adjustment4=sysvariablelist.get(3).getValueString(); 
				Commission1=sysvariablelist.get(4).getValueString();
				Commission2=sysvariablelist.get(5).getValueString();
				Commission3=sysvariablelist.get(6).getValueString();
				Commission4=sysvariablelist.get(7).getValueString();
				 
				aEcstatement = (Ecstatement) itsEmployeeServiceInter.getEmployeeCommissionsStatement(aEmmasters.getEcStatementID()).get(0);
				 Commission1_Value= aEcstatement.getJobCommissions();
				 Commission2_Value= new BigDecimal(0.00);
				 Commission3_Value= new BigDecimal(0.00);
				 Commission4_Value= new BigDecimal(0.00);
				 Adjustment1_Value= aEcstatement.getRepDeduct1();
				 Adjustment2_Value= aEcstatement.getRepDeduct2();
				 Adjustment3_Value= aEcstatement.getRepDeduct3();
				 Adjustment4_Value= aEcstatement.getRepDeduct4();
				 Commission_Sum=Commission1_Value.add(Commission2_Value).add(Commission3_Value).add(Commission4_Value);
				 Adjustment_Sum=Adjustment1_Value.add(Adjustment2_Value).add(Adjustment3_Value).add(Adjustment4_Value);
				 itsLogger.info("Sum of Commissions:"+Commission_Sum);
				 openingBalance= aEcstatement.getOpeningBalance();
				 netChange= Commission_Sum.add(Adjustment_Sum);
				 netChngOpenBal = openingBalance.add(netChange);
				 payment= aEcstatement.getPayment();
				 closingBal= netChngOpenBal.subtract(payment);
					
				 DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
				 formatSymbols.setDecimalSeparator('.');
     				formatSymbols.setGroupingSeparator(' ');
					String strange = "#,##0.##";
					DecimalFormat df = new DecimalFormat(strange, formatSymbols);
					//df.setGroupingSize(4);
					formatopeningBalance = "$ ";
					formatopeningBalance = formatopeningBalance +""+df.format(ReportUtils.truncateDecimal(openingBalance,2).doubleValue());
					formatCommission1_Value="$ ";
					formatCommission1_Value = formatCommission1_Value+df.format(ReportUtils.truncateDecimal(Commission1_Value,2).doubleValue());
					formatCommission2_Value="$ ";
					formatCommission2_Value = formatCommission2_Value+df.format(ReportUtils.truncateDecimal(Commission2_Value,2).doubleValue());
					formatCommission3_Value="$ ";
					formatCommission3_Value = formatCommission3_Value+df.format(ReportUtils.truncateDecimal(Commission3_Value,2).doubleValue());
					formatCommission4_Value="$ ";
					formatCommission4_Value = formatCommission4_Value+df.format(ReportUtils.truncateDecimal(Commission4_Value,2).doubleValue());
					formatAdjustment1_Value = "$ ";
					formatAdjustment1_Value = formatAdjustment1_Value+df.format(ReportUtils.truncateDecimal(Adjustment1_Value,2).doubleValue());
					formatAdjustment2_Value = "$ ";
					formatAdjustment2_Value = formatAdjustment2_Value+df.format(ReportUtils.truncateDecimal(Adjustment2_Value,2).doubleValue());
					formatAdjustment3_Value = "$ ";
					formatAdjustment3_Value = formatAdjustment3_Value+df.format(ReportUtils.truncateDecimal(Adjustment3_Value,2).doubleValue());
					formatAdjustment4_Value = "$ ";
					formatAdjustment4_Value = formatAdjustment4_Value+df.format(ReportUtils.truncateDecimal(Adjustment4_Value,2).doubleValue());
					formatCommission_Sum = "$ ";
					formatCommission_Sum = formatCommission_Sum+df.format(ReportUtils.truncateDecimal(Commission_Sum,2).doubleValue());
				    formatAdjustment_Sum = "$ ";
				    formatAdjustment_Sum = formatAdjustment_Sum+df.format(ReportUtils.truncateDecimal(Adjustment_Sum,2).doubleValue());
				    formatnetChange = "$ ";
				    formatnetChange = formatnetChange+df.format(ReportUtils.truncateDecimal(netChange,2).doubleValue());
				    formatpayment = "$ ";
				    formatpayment = formatpayment+df.format(ReportUtils.truncateDecimal(payment,2).doubleValue());
				    formatclosingBal = "$ ";
				    formatclosingBal = formatclosingBal+df.format(ReportUtils.truncateDecimal(closingBal,2).doubleValue());
				    formatnetChngOpenBal = "$ ";
				    formatnetChngOpenBal = formatnetChngOpenBal+df.format(ReportUtils.truncateDecimal(netChngOpenBal,2).doubleValue());
				
					itsLogger.info("Opening Balance:"+ReportUtils.truncateDecimal(openingBalance,2));				 
			for(Sysvariable theSysvariable:sysvariablelist){
				 str += theSysvariable.getValueString()+",";
			}
		} catch (UserException e1) {
			e1.printStackTrace();
		}
			
		
		try{
			if(endDate!=null && endDate!="" && endDate.length()!=0){
				As_Of=endDate;
			}
			String path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/EmployeeJobCommission.jrxml");
			 Date dNow = new Date( );
		      SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
			String filename="CommissionCoverSheet_"+ft.format(dNow)+".pdf";
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("As_Of", As_Of);
			params.put("labels", str);
			params.put("ecStatementID", aEcstatement.getEcStatementId());
			params.put("Adjustment1", Adjustment1);
			params.put("Adjustment2", Adjustment2);
			params.put("Adjustment3", Adjustment3);
			params.put("Adjustment4", Adjustment4);
			params.put("Commission1", Commission1);
			params.put("Commission2", Commission2);
			params.put("Commission3", Commission3);
			params.put("Commission4", Commission4);
			params.put("Commission1_Value", formatCommission1_Value);
			params.put("Commission2_Value", formatCommission2_Value);
			params.put("Commission3_Value", formatCommission3_Value);
			params.put("Commission4_Value", formatCommission4_Value);
			params.put("Adjustment1_Value", formatAdjustment1_Value);
			params.put("Adjustment2_Value", formatAdjustment2_Value);
			params.put("Adjustment3_Value", formatAdjustment3_Value);
			params.put("Adjustment4_Value", formatAdjustment4_Value);
			params.put("Commission_Sum", formatCommission_Sum);
			params.put("Adjustment_Sum", formatAdjustment_Sum);
			params.put("openingBalance", formatopeningBalance);
			params.put("netChange", formatnetChange);
			params.put("payment", formatpayment);
			params.put("netChngOpenBal", formatnetChngOpenBal);
			params.put("closingBal", formatclosingBal);
			params.put("EmployeeName", aEcstatement.getRepName());
			params.put("comments", aEcstatement.getComment());
			connection = con.getConnection();
			
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			Integer returnvalue=itsEmployeeServiceInter.CheckEmployeeCommissionCount(aEcstatement.getEcStatementId());
			itsLogger.info("returnvalue===="+returnvalue);
			/*String query = "SELECT 'emptyrow' AS JobNumber, 'emptyrow' AS Description,0.00 AS EstimatedProfit,0.00 AS ProfitToDate, 0.00 AS ProfitPaidOnToDate, 0.00 AS  ProfitCurrent,0.00 AS  CommissionRate, 0.00 AS CommissionToDate, 0.00 AS PaidToDate, 0.00 AS AmountDue,'empty' AS Name, 'empty' AS FirstName, 0 AS rxCustomerID ,(SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo FROM DUAL";*/
			String query = "SELECT 0 as ExpjoMasterID, 0 as ExprxMasterID, 0 AS joReleaseID2, 0 as joReleaseID, 0 as rxMasterID, "
					+ "'emptyrow' as Name, 'emptyrow' as FirstName, 'emptyrow' as JobNumber, 'emptyrow' AS JobName, "
					+ "'emptyrow' as QuickJobName, 'emptyrow' as releaseType, 0.00 as Sales,0.00 AS TotalSales, 0.00 AS TotalProfit, "
					+ "'emptyrow' as Reference, 'emptyrow' as DatePaid, 0.00 as Profit, 0.00 as CommissionRate, 0.00 as AmountDue,"
					+ "0.00 AS Gross, (SELECT headertext FROM tsUserSetting) AS header, "
					+ "(SELECT companylogo FROM tsUserSetting) AS logo FROM DUAL"; 
			if(returnvalue>0){
			/*
			 query = "SELECT joMaster.JobNumber, joMaster.Description, joMaster.EstimatedProfit, ecJobs.ProfitToDate, ecJobs.ProfitPaidOnToDate, ecJobs.ProfitToDate-ecJobs.ProfitPaidOnToDate AS ProfitCurrent, ecJobs.CommissionRate, ecJobs.CommissionToDate, ecJobs.PaidToDate, ecJobs.AmountDue, rxMaster.Name, rxMaster.FirstName, joMaster.rxCustomerID ,(SELECT headertext FROM tsUserSetting) AS header,"
					+"(SELECT companylogo FROM tsUserSetting) AS logo FROM rxMaster RIGHT JOIN (joMaster RIGHT JOIN ecJobs ON joMaster.joMasterID = ecJobs.joMasterID) ON rxMaster.rxMasterID = joMaster.rxCustomerID "
					+"WHERE ecJobs.ecStatementID= "+aEcstatement.getEcStatementId()+" ORDER BY rxMaster.Name, rxMaster.FirstName, joMaster.Description";
			*/
				query = "SELECT ecInvoicePeriod.ExpjoMasterID, ecInvoicePeriod.ExprxMasterID, ecInvoicePeriod.joReleaseID AS joReleaseID2,"
						+ "joReleaseDetail.joReleaseID, rxMaster.rxMasterID, rxMaster.Name, rxMaster.FirstName, IFNULL(joMaster.JobNumber,'-') AS JobNumber, "
						+ "IFNULL(joMaster.Description,rxMaster.Name) AS JobName, cuInvoice.QuickJobName, (CASE ecInvoicePeriod.ReleaseType "
						+ "WHEN  1 THEN 'Drop Ship' WHEN  2 THEN 'Stock Order' WHEN  3 THEN 'Bill Only' WHEN  4 THEN 'Commission' "
						+ "WHEN  5 THEN 'Service' WHEN ecInvoicePeriod.ReleaseType IS NULL THEN 'Sales' END ) as releaseType, ecInvoicePeriod.Sales, "
						+ "IFNULL(ecInvoicePeriod.Sales,0.0000) AS TotalSales, IFNULL(ecInvoicePeriod.Profit,0.0000) AS TotalProfit, ecInvoicePeriod.Reference, ecInvoicePeriod.DatePaid,"
						+ "IFNULL(ecInvoiceRepSplit.Profit,0.0000) AS Profit, IFNULL(ecInvoiceRepSplit.CommissionRate,0.0000) AS CommissionRate , IFNULL(ecInvoiceRepSplit.AmountDue,0.0000) AS AmountDue,IFNULL(cuInvoice.SubTotal,0.0000) + IFNULL(cuInvoice.Freight,0.0000) "
						+ "AS Gross,(SELECT headertext FROM tsUserSetting) AS header, (SELECT companylogo FROM tsUserSetting) AS logo "
						+ "FROM(joReleaseDetail RIGHT JOIN (((ecInvoiceRepSplit LEFT JOIN ecInvoicePeriod ON "
						+ "ecInvoiceRepSplit.ecInvoicePeriodID = ecInvoicePeriod.ecInvoicePeriodID) LEFT JOIN rxMaster ON "
						+ "ecInvoicePeriod.rxCustomerID = rxMaster.rxMasterID) LEFT JOIN cuInvoice ON ecInvoicePeriod.cuInvoiceID = cuInvoice.cuInvoiceID) "
						+ "ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) LEFT JOIN joMaster ON "
						+ "joReleaseDetail.joMasterID = joMaster.joMasterID WHERE ecInvoiceRepSplit.ecStatementID = "+aEcstatement.getEcStatementId()+""
						+ " AND ecInvoiceRepSplit.AmountDue <> 0.0000 and ecInvoicePeriod.Sales <> 0.0000 ORDER BY joMaster.JobNumber, cuInvoice.InvoiceNumber";
			itsLogger.info("Commission Query:"+query);
			}
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			ReportService.dynamicReportCall(response,params,"pdf",jd,filename,connection);
		}catch (Exception e) {
			itsLogger.error(e.getMessage());
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
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
			itsUserService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
}