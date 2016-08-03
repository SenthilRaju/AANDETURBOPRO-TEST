package com.turborep.turbotracker.company.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;

@Controller
@RequestMapping("/companycontroller")
public class CompanyController {

	protected static Logger logger = Logger.getLogger(CompanyController.class);

	@Resource(name = "companyService")
	private CompanyService companyService;

	@Resource(name = "rolodexService")
	private RolodexService customerService;

	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@RequestMapping(value = "/engineerRxList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getEngineerRxList(@RequestParam("term") String salesRep,
			HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> sales = null;
		try {
			sales = (ArrayList<AutoCompleteBean>) companyService
					.getEngineerRxList(JobUtil.removeSpecialcharacterswithslash(salesRep));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			 sendTransactionException("<b>salesRep:</b> "+salesRep,"Company",e,session,request);
		}
		return sales;
	}

	@RequestMapping(value = "/architectRxList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getArchitectRxList(@RequestParam("term") String salesRep,
			HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> sales = null;
		try {
			sales = (ArrayList<AutoCompleteBean>) companyService
					.getArchitectRxList(JobUtil.removeSpecialcharacterswithslash(salesRep));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>salesRep:</b> "+salesRep,"Company",e,session,request);
		}
		return sales;
	}

	@RequestMapping(value = "/GCRXList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getGCRXList(@RequestParam("term") String salesRep,
			HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> sales = null;
		try {
			sales = (ArrayList<AutoCompleteBean>) companyService
					.getGCRXList(JobUtil.removeSpecialcharacterswithslash(salesRep));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>salesRep:</b> "+salesRep,"Company",e,session,request);
		}
		return sales;
	}

	@RequestMapping(value = "/companyTax", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getCompanyTaxList(@RequestParam("term") String salesRep,
			HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> sales = null;
		try {
			sales = (ArrayList<AutoCompleteBean>) companyService
					.getCompanyTaxList(JobUtil.removeSpecialcharacterswithslash(salesRep));
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>salesRep:</b> "+salesRep,"Company",e,session,request);
		}
		return sales;
	}

	@RequestMapping(value = "newCompanyAddress", method = RequestMethod.POST)
	public @ResponseBody
	Rxmaster addCustomerAddress(
			@RequestParam(value = "locationAddress1", required = false) String address1,
			@RequestParam(value = "locationAddress2", required = false) String address2,
			@RequestParam(value = "locationState", required = false) String state,
			@RequestParam(value = "locationZip", required = false) String pinCode,
			@RequestParam(value = "USPhoneNumber", required = false) String phone1,
			@RequestParam(value = "USPhone_Number", required = false) String phone2,
			@RequestParam(value = "fax", required = false) String fax,
			@RequestParam(value = "locationCity", required = false) String city,
			@RequestParam(value = "rolodexNumber", required = false) Integer theRxmasterId,
			@RequestParam(value = "existingAddress1", required = false) String theAddress,
			@RequestParam(value = "EmployeeLastName", required = false) String thename,
			@RequestParam(value = "mailAddress", required = false) boolean themailID,
			@RequestParam(value = "shipAddress", required = false) boolean theshipID,
			@RequestParam(value = "operation", required = false) String theOper,
			@RequestParam(value = "VendorId", required = false) boolean theVendorId,
			@RequestParam(value = "CustomerId", required = false) boolean theCustomerId,
			@RequestParam(value = "EmployeeId", required = false) boolean theEmployeeId,
			@RequestParam(value = "ArchitectId", required = false) boolean theArchitectId,
			@RequestParam(value = "remitCheckbox", required = false) boolean remitto,
			@RequestParam(value = "EngineerId", required = false) boolean theEngineerId,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		Rxmaster aCustomer = new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		Rxmaster aNewCustomer = null;
		try {
			String afirstName = "";
			String asearchName = "";
			boolean inActive = false;
			boolean IsStreet = false;
			boolean IsBillTo = true;
			boolean IsShipTo = true;
			
			System.out.println("=================>"+remitto);
			
			aCustomer.setRxMasterId(theRxmasterId);
			if (thename == null || thename.trim().equalsIgnoreCase("") || thename.equals("null")) {
				aCustomer.setName("");
			} else {
				String aCustomerName = thename.replaceAll("`", "");
				aCustomer.setName(aCustomerName);
			}
			aCustomer.setFirstName(afirstName);
			aCustomer.setSearchName(asearchName);
			aCustomer.setInActive(inActive);
			aCustomer.setPhone1(phone1);
			aCustomer.setPhone2(phone2);
			aCustomer.setFax(fax);
			aCustomer.setIsVendor(theVendorId);
			aCustomer.setIsCustomer(theCustomerId);
			aCustomer.setIsEmployee(theEmployeeId);
			aCustomer.setIsCategory1(theArchitectId);
			aCustomer.setIsCategory2(theEngineerId);
			aRxaddress.setRxMasterId(theRxmasterId);
			if (address1 != null && !address1.equalsIgnoreCase("")) {
				String aAddress1 = address1.trim();
				aRxaddress.setAddress1(aAddress1);
			}
			if (address2 != null && !address2.equalsIgnoreCase("")) {
				String aAddress2 = address2.trim();
				aRxaddress.setAddress2(aAddress2);
			}
			if (city != null && !city.equalsIgnoreCase("")) {
				String aCity = city.trim();
				aRxaddress.setCity(aCity);
			}
			if (state != null && !state.equalsIgnoreCase("")) {
				String aState = state.trim();
				aRxaddress.setState(aState);
			}
			aRxaddress.setZip(pinCode);
			aRxaddress.setInActive(inActive);
			aRxaddress.setIsMailing(themailID);
			aRxaddress.setIsShipTo(theshipID);
			aRxaddress.setIsStreet(IsStreet);
			aRxaddress.setIsBillTo(IsBillTo);
			aRxaddress.setIsShipTo(IsShipTo);
			aRxaddress.setOtherBillTo(2);
			aRxaddress.setOtherShipTo(3);
			aRxaddress.setPhone1(phone1);
			aRxaddress.setPhone2(phone2);
			aRxaddress.setFax(fax);
			aRxaddress.setIsRemitTo(remitto);
			if(remitto)
				customerService.updateRemittorxaddress(theRxmasterId);
			
			if (theOper.equals("edit")) {
				Integer aRxAddressID = customerService.getRxAddressID(
						theRxmasterId, theAddress);
				if (aRxAddressID != null) {
					aRxaddress.setRxAddressId(aRxAddressID);
				}
				aNewCustomer = new Rxmaster();
				logger.info(" >>>>>>> aCustomer.getRxMasterId() = "+aCustomer.getRxMasterId());
				 itsRolodexService.updatedefaultstatusbasedoncustomer(aRxaddress);
//				aNewCustomer = customerService.addNewCustomerAddress(aCustomer,
//						aRxaddress);
			} else if (theOper.equals("add")) {
				aNewCustomer = new Rxmaster();
				aNewCustomer = customerService.addNewVendorAddress(aRxaddress);
			}
			else if(theOper.equals("delete")){
				Integer aRxAddressID = customerService.getRxAddressID(
						theRxmasterId, theAddress);
				if (aRxAddressID != null) {
					aRxaddress.setRxAddressId(aRxAddressID);
				}
				itsRolodexService.deleteAddressBasedonCustomer(aRxaddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendTransactionException("<b>rxMasterID:</b> "+aCustomer.getRxMasterId(),"Company",e,session,request);
		}
		return aNewCustomer;
	}
	
	@RequestMapping(value = "newAddressaddoredit", method = RequestMethod.POST)
	public @ResponseBody
	boolean addrolodexAddressforAllScreens(
			@RequestParam(value = "rxAddressID", required = false) Integer rxAddressID,
			@RequestParam(value = "rolodexAddress1", required = false) String address1,
			@RequestParam(value = "rolodexAddress2", required = false) String address2,
			@RequestParam(value = "rolodexCity", required = false) String city,
			@RequestParam(value = "rolodexState", required = false) String state,
			@RequestParam(value = "rolodexZip", required = false) String pinCode,
			@RequestParam(value = "USPhoneNumber1", required = false) String phone1,
			@RequestParam(value = "USPhoneNumber2", required = false) String phone2,
			@RequestParam(value = "fax", required = false) String fax,
			@RequestParam(value = "rolodexNumber", required = false) Integer theRxmasterId,
			@RequestParam(value = "mailAddress", required = false) boolean themailID,
			@RequestParam(value = "shipAddress", required = false) boolean theshipID,
			@RequestParam(value = "defaultAddress", required = false) boolean thedefaultAddress,
			@RequestParam(value = "operation", required = false) String theOper,
			@RequestParam(value = "remitCheckbox", required = false) boolean remitto,
			@RequestParam(value = "EmployeeLastName", required = false) String name,
			HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		Rxmaster aCustomer = new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		try {
			
			aRxaddress.setRxMasterId(theRxmasterId);
			if (address1 != null && !address1.equalsIgnoreCase("")) {
				String aAddress1 = address1.trim();
				aRxaddress.setAddress1(aAddress1);
			}
			if (address2 != null && !address2.equalsIgnoreCase("")) {
				String aAddress2 = address2.trim();
				aRxaddress.setAddress2(aAddress2);
			}
			if (city != null && !city.equalsIgnoreCase("")) {
				String aCity = city.trim();
				aRxaddress.setCity(aCity);
			}
			if (state != null && !state.equalsIgnoreCase("")) {
				String aState = state.trim();
				aRxaddress.setState(aState);
			}
			aRxaddress.setZip(pinCode);
			aRxaddress.setInActive(false);
			aRxaddress.setIsMailing(themailID);
			aRxaddress.setIsShipTo(theshipID);
			aRxaddress.setIsStreet(false);
			aRxaddress.setOtherBillTo(2);
			aRxaddress.setOtherShipTo(3);
			aRxaddress.setPhone1(phone1);
			aRxaddress.setPhone2(phone2);
			aRxaddress.setFax(fax);
			aRxaddress.setIsRemitTo(remitto);
			
			if(name!=null)
			name=name.replaceAll("`","");
			
			aRxaddress.setName(name);
			
			// Is Mailing Address
			aRxaddress.setIsMailing(themailID);
			aRxaddress.setIsBillTo(themailID);
			// Is Shipto Address
			aRxaddress.setIsShipTo(theshipID);
			// Is Default Address
			aRxaddress.setIsDefault(thedefaultAddress);
		
			
			if(remitto)
				customerService.updateRemittorxaddress(theRxmasterId);
			if(thedefaultAddress)
				customerService.updateDefaultrxaddress(theRxmasterId);
			
			
			 if (theOper.equals("add")) {
					itsRolodexService.addoreditRolodexAddress( aRxaddress, theOper);
			}
			else if (theOper.equals("edit")) {
					aRxaddress.setRxAddressId(rxAddressID);
					itsRolodexService.addoreditRolodexAddress( aRxaddress,  theOper);
			} 
			else if(theOper.equals("delete")){
				if (rxAddressID != null) {
					aRxaddress.setRxAddressId(rxAddressID);
					itsRolodexService.addoreditRolodexAddress( aRxaddress, theOper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendTransactionException("<b>rxMasterID:</b> "+aCustomer.getRxMasterId(),"Company",e,session,request);
		}
		return true;
	}
	
	@RequestMapping(value = "/deleteCustomerAddress", method = RequestMethod.GET)
	public @ResponseBody boolean deleteCustomerAddress(
			@RequestParam(value = "rxAddressId", required = true) Integer rxAddressId,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException
	{
		logger.info("/deleteCustomerAddress"+rxAddressId);
		try {
			customerService.deleteVendorAddress(rxAddressId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>rxMasterID:</b> "+rxAddressId,"Company",e,session,request);
		}
		return true;
	}

	@RequestMapping(value = "/getCustomerName", method = RequestMethod.GET)
	public @ResponseBody
	String getCustomerName(@RequestParam("rxMasterID") Integer RxMasterID,
			HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		String customerName = "";
		try {
			customerName = companyService.getCustomerName(RxMasterID);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>rxMasterID:</b> "+RxMasterID,"Company",e,session,request);
		}
		return customerName;
	}

	// getFreightCharges
	@RequestMapping(value = "/getFreightCharges", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getCustomerTypes(HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();

		try {
			List<?> itsFreightCharge = companyService.getFreightCharges();
			response.setRows(itsFreightCharge);
		} catch (Exception e) {
			sendTransactionException("<b>Tracking ID:</b> getFreightCharges","Company",e,session,request);
		}
		return response;
	}

	@RequestMapping(value = "/updateFreightCharges", method = RequestMethod.POST)
	public @ResponseBody
	Boolean updateFreightCharges(
			@RequestParam(value = "veFreightChargesId", required = false) Integer theFreightId,
			@RequestParam(value = "description", required = false) String theDescription,
			@RequestParam(value = "askForNote", required = false) Boolean askForNote,
			@RequestParam(value = "inActive", required = false) Boolean theInactive,
			@RequestParam(value = "operation", required = false) Boolean isAdd,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws CompanyException {

		Vefreightcharges veFreight = new Vefreightcharges();
		if (!isAdd) {
			veFreight.setVeFreightChargesId(theFreightId);
		}
		System.out.println(theFreightId + " :: " + theDescription + " :: "
				+ askForNote + " :: " + theInactive + " :: " + isAdd);
		veFreight.setAskForNote(askForNote);
		veFreight.setInActive(theInactive);
		veFreight.setDescription(theDescription);
		companyService.updateFreightChargesService(veFreight, isAdd);
		return true;
	}

	// deleteFreightCharges
	@RequestMapping(value = "/deleteFreightCharges", method = RequestMethod.POST)
	public @ResponseBody
	Boolean deleteFreightCharges(
			@RequestParam(value = "veFreightChargesId", required = false) Integer theFreightId,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session) throws CompanyException {

		companyService.deleteFreightChargesService(theFreightId);
		return true;
	}
	
	@RequestMapping(value = "/printCSVCompanyContacts", method = RequestMethod.GET)
public @ResponseBody
void printCSVCompanyContacts(
		@RequestParam(value = "number", required = false) Integer number,
		HttpServletResponse theResponse,HttpServletRequest request,HttpSession session)
		throws IOException, MessagingException {
		Connection connection = null;
		ConnectionProvider con =null;
	try {
		HashMap<String, Object> params = new HashMap<String, Object>();
		String path_JRXML =null;
		String filename=null;
		System.out.println("insid the printCSVCompanyContacts");
		if(number==2){
			path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/customersettingscsv.jrxml");
			filename="CustomerContacts.csv";
		}
		else if(number==3){
			path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/vendorsettingscsv.jrxml");
			filename="VendorContacts.csv";
		}else{
			path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/companysettingscsv.jrxml");
			filename="CompanyContacts.csv";
		}
		con = itspdfService.connectionForJasper();
		//Have to set Params
		//params.put("CuInvoice", CuInvoice);
		
		connection = con.getConnection();
		
		ReportService.ReportCall(theResponse,params,"xls",path_JRXML,filename,connection);
		

	} catch (SQLException e) {
		logger.error(e.getMessage());
		theResponse.sendError(500, e.getMessage());
		sendTransactionException("<b>Tracking ID:</b> printCSVCompanyContacts","Company",e,session,request);
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
	@RequestMapping(value = "/printCustomerListEmployeeCSV", method = RequestMethod.GET)
	public @ResponseBody
	void printCustomerListEmployeeCSV(
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			HttpServletResponse theResponse,HttpServletRequest request,HttpSession session)
			throws IOException, JRException, MessagingException {
		Connection connection =null;
		ConnectionProvider con=null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/ContactsFromEmployeeAssigned.jrxml");
			String filename="CustomerContacts.csv";
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("tsUserLoginID", tsUserLoginID);
			
			connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			String query = "SELECT CONCAT(rxContact.FirstName,' ',rxContact.LastName) AS contactName,rxContact.DirectLine,rxContact.EMail,rxContact.Cell,rxContact.JobPosition,rxContact.Title,rxMaster.rxMasterID,rxMaster.name,rxMaster.phone1,rxAddress.city,rxAddress.state,rxAddress.Address1 FROM rxMaster LEFT JOIN rxAddress ON (rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.IsDefault=1) JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID LEFT JOIN  rxContact ON rxContact.rxMasterID = rxMaster.rxMasterID WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT NULL AND rxMaster.name <> '(missing)' AND rxMaster.name <> ''  ORDER BY rxMaster.Name ASC";
			if(tsUserLoginID!=0){ 
			query = "SELECT CONCAT(rxContact.FirstName,' ',rxContact.LastName) AS contactName,rxContact.DirectLine,rxContact.EMail,rxContact.Cell,rxContact.JobPosition,rxContact.Title,rxMaster.rxMasterID,rxMaster.name,rxMaster.phone1,rxAddress.city,rxAddress.state,rxAddress.Address1 FROM rxMaster LEFT JOIN rxAddress ON (rxMaster.rxMasterID = rxAddress.rxMasterID AND rxAddress.IsDefault=1) JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID LEFT JOIN  rxContact ON rxContact.rxMasterID = rxMaster.rxMasterID WHERE rxMaster.isCustomer = 1 AND rxMaster.name IS NOT NULL AND rxMaster.name <> '(missing)' AND rxMaster.name <> '' AND (cuMaster.cuAssignmentID0 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID1 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID2 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID3 = "+tsUserLoginID+" OR cuMaster.cuAssignmentID4 = "+tsUserLoginID+"  ) ORDER BY rxMaster.Name ASC";
			}
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			
			ReportService.dynamicReportCall(theResponse,params,"xls",jd,filename,connection);
			//ReportService.ReportCall(theResponse,params,"xls",path_JRXML,filename,connection);
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> printCSVCompanyContacts","Company",e,session,request);
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
	
	@RequestMapping(value = "/positivepayPDFdownload", method = RequestMethod.GET)
	public @ResponseBody
	void positivepayPDFdownload(
			@RequestParam(value = "positivePayDate", required = false) String  positivePayDate,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session)
			throws IOException, MessagingException, SQLException {
		Connection connection = null;
		ConnectionProvider con =null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/positivePay.jrxml");
			filename="PositivePay.csv";
			
			con = itspdfService.connectionForJasper();
			//Have to set Params
			String inputFormat="MM/dd/yyyy";
			String outputFormat="yyyy-MM-dd";
			params.put("CheckDate", JobUtil.convertDateFormat(positivePayDate, inputFormat, outputFormat));
			
			connection = con.getConnection();
			//ReportService.ReportDownload(theResponse,params,"csv",path_JRXML,filename,connection);
			ReportService.ReportCall(theResponse,params,"xls",path_JRXML,filename,connection);
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value = "/comparetwoGridObject", method = RequestMethod.GET)
	public @ResponseBody Boolean comparetwoGridObject(
			@RequestParam(value = "oldGridData", required = false) String  oldGridData,
			@RequestParam(value = "newGridData", required = false) String  newGridData,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session)
			{
		Boolean returnValue=false;
		if(oldGridData!=null && newGridData!=null){
			if(oldGridData.equals(newGridData)){
				returnValue=true;
			}
		}
			return returnValue;
		}
}