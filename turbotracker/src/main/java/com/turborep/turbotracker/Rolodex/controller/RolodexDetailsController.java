package com.turborep.turbotracker.Rolodex.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.RxJournal;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysassignment;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("rolodexdetails")
public class RolodexDetailsController {

	Logger itsLogger = Logger.getLogger("controller"); 
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;

	@Resource(name="sysService")
	private SysService sysservice;
	
	private Rxaddress itsRxAddressDetails;
	
	private Rxmaster itsRxMasterDetails;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@RequestMapping(value="")
	public String getRolodexDetailPage( @RequestParam(value="rolodexNumber", required=false) String theRolodexNumber, 
																	@RequestParam(value="name", required=false) String theCustomerName, 
																	ModelMap theModel,  HttpSession theSession,HttpServletRequest request,
																	HttpServletResponse response) throws IOException, UserException, MessagingException  {

		itsLogger.debug("Received request to show customers page");
		Sysinfo sysinfo=null;
		Sysassignment sysassignment=null;
		if (theRolodexNumber != null) {
			theSession.setAttribute("rolodexNumber", theRolodexNumber);
			theSession.setAttribute("rxMasterId", theRolodexNumber);
			try {
				setRxMasterDetails(theRolodexNumber, theSession, request, response);
			} catch (CompanyException e) {
				itsLogger.error(e.getMessage(), e);
				response.sendError(e.getItsErrorStatusCode(), e.getMessage());
				sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"RolodexDetailsController",e,theSession,request);
			}
			Rxmaster aRxmaster = getRxMasterDetails();
			theModel.addAttribute("name", aRxmaster.getName());
			theModel.addAttribute("phone", aRxmaster.getPhone1());
			theModel.addAttribute("rxMasterDetails", aRxmaster);
			
			
			sysinfo=sysservice.getRoldexCategories(1);
			
			
			List<?> aCustomer = itsRolodexService.getEmployees(0, 1000, 0);
			/*Getting rolodox categories  from sysinfo table*/
			if(sysinfo!=null){
				theModel.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
				theModel.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
				theModel.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
				theModel.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
				theModel.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
				theModel.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
				theModel.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
				theModel.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
			}
			
			sysassignment=sysservice.getCustomerCategories();
			if(sysassignment!=null){
				theModel.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
				theModel.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
				theModel.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
				theModel.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
				theModel.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
				/*model.addAttribute("CustomerCategoryId1",sysassignment.getCustomerCategoryId1());
				model.addAttribute("CustomerCategoryId2", sysassignment.getCustomerCategoryId2());
				model.addAttribute("CustomerCategoryId3", sysassignment.getCustomerCategoryId3());
				model.addAttribute("CustomerCategoryId4", sysassignment.getCustomerCategoryId4());
				model.addAttribute("CustomerCategoryId5", sysassignment.getCustomerCategoryId5());*/
			}
			// session.setAttribute("rxMasterId",
			// Integer.valueOf(theRolodexNumber));
			/* session.setAttribute("rolodexName", theCustomerName); */
			theModel.addAttribute("rxMasterDetails", aRxmaster);
			
			
		}
		return "rolodex/rolodexdetails";
	}
	
	public Rxmaster getRxMasterDetails() {
		return itsRxMasterDetails;
	}

	public void setRxMasterDetails(String theRolodexNumber,HttpSession theSession,HttpServletRequest request,
			HttpServletResponse response) throws CompanyException, IOException, MessagingException {
		
		try {
			this.itsRxMasterDetails = itsCompanyService.getEmployeeDetails(theRolodexNumber);
		} catch (CompanyException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage()); 
			sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"RolodexDetailsController",e,theSession,request);
		};
	}

	
	@RequestMapping(value = "/rolodex_contacts", method = RequestMethod.GET)
	public String getCustomerContactsPage(@RequestParam(value="rolodexID", required=false) String theRolodexNumber,
																			ModelMap theModel, HttpSession theSession,
																			HttpServletRequest theRequest, HttpServletResponse response) throws IOException, MessagingException {
		Rxmaster aRxmaster = null;
		//List<?> aRxaddress=null;
		Rxaddress aRxaddress=null;
		try {
			setRxMasterDetails(theRolodexNumber, theSession, theRequest, response);
			theModel.remove(aRxaddress);
			setRxAddressDetails(theRolodexNumber, theSession, theRequest, response);
			aRxmaster = getRxMasterDetails();
			aRxaddress = getRxAddressDetails();
			theModel.addAttribute("rxMasterDetails", aRxmaster);
			theModel.addAttribute("rxAddressDetails", aRxaddress);
		} catch (CompanyException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"RolodexDetailsController",e,theSession,theRequest);
		}
		return "rolodex/rolodex_contacts";
	}
	
	@RequestMapping(value="/rolodexcontactlist",method = RequestMethod.POST)
	public @ResponseBody CustomResponse employeeContact(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId) {
		itsLogger.debug("Sales rep id: " + theRolodexId);
		Rxcontact aRxcontact = new Rxcontact();
		aRxcontact.setRxMasterId(theRolodexId);
		ArrayList<Rxcontact> aRxcontactsList = (ArrayList<Rxcontact>) itsRolodexService.getRolodexContactDetails(aRxcontact);
		CustomResponse response = new CustomResponse();
		response.setRows(aRxcontactsList);
		return response;
	}
	
	/**
	 * This method is used to add a new contact for a rolodex.
	 * @param theDirectLine {@link String}
	 * @param theCell {@link String}
	 * @param theEmail {@link String}
	 * @param theFirstName {@link String}
	 * @param theRole {@link String}
	 * @param theLastName {@link String}
	 * @param theRxMasterId {@link Integer}
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	@RequestMapping(value="/manpulaterxContact", method = RequestMethod.POST)
	public @ResponseBody Integer addRolodexContact (@RequestParam(value = "directLine", required = false) String theDirectLine1,
									  @RequestParam(value = "directDir", required = false) String theDirectLine,
									  @RequestParam(value = "cell", required = false) String theCell,
									  @RequestParam(value = "email", required = false) String theEmail,
									  @RequestParam(value = "firstName", required = false) String theFirstName,
									  @RequestParam(value = "jobPosition", required = false) String theRole,
									  @RequestParam(value = "lastName", required = false) String theLastName,
									  @RequestParam(value = "rxMasterId", required = false) Integer theRxMasterId,
									  @RequestParam(value = "oper", required = false) String  theOperation,
									  @RequestParam(value = "division", required = false) String theDivision,
									  @RequestParam(value = "extension", required = false) String theExtension,
									  @RequestParam(value = "rxContactId", required = false) Integer theRxContactId, HttpSession theSession,
										HttpServletRequest theRequest, HttpServletResponse response) throws IOException, MessagingException {
		Rxcontact aRxcontact = new Rxcontact();
		try{
			aRxcontact.setCell(theCell);
			aRxcontact.setFirstName(theFirstName);
			aRxcontact.setLastName(theLastName);
			aRxcontact.setEmail(theEmail);
			aRxcontact.setJobPosition(theRole);
			aRxcontact.setRxMasterId(theRxMasterId);
			aRxcontact.setInActive(false);
			aRxcontact.setDirectLine(theDirectLine);
			aRxcontact.setDivision(theDivision);
			aRxcontact.setExtension(theExtension);
			if(theOperation.equals("add")){
				itsRolodexService.addRolodexContact(aRxcontact);
			} else if (theOperation.equals("edit")) {
				aRxcontact.setRxContactId(theRxContactId);
				itsRolodexService.editRolodexContact(aRxcontact);
			}if(theOperation.equals("del")){
				aRxcontact.setRxContactId(theRxContactId);
				itsRolodexService.getBidderIDList(aRxcontact);
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexNumber:</b>"+theRxMasterId,"RolodexDetailsController",e,theSession,theRequest);
		}
		return 0;
	}
	
	@RequestMapping(value="/addNewRolodex", method = RequestMethod.POST)
	public @ResponseBody Integer addNewRolodex () {
		return 0;
	}
	
	/**
	 * This method is used to add a new jounal for a rolodex.
	 * @param theDate  {@link Date}
	 * @param theEntity  {@link String}
	 * @param theUser  {@link String}
	 * @throws ParseException 
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	@RequestMapping(value="/manpulaterxJournal", method = RequestMethod.POST)
	public @ResponseBody int addRolodexJournal (@RequestParam(value="entryMemo", required= false ) String theEntity,
												@RequestParam(value = "oper", required = false) String theOperation,
												@RequestParam(value = "rxMasterId", required = false) Integer theRxMasterId,
												@RequestParam(value = "rxJournalId", required = false) Integer theRxJournalId, HttpSession theSession,HttpServletRequest theRequest, HttpServletResponse response) throws ParseException, IOException, MessagingException{
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		RxJournal aRxjournal = new RxJournal();
		Date aDate;
		
		try{
			GregorianCalendar aCalendar = new GregorianCalendar();
			aDate = aCalendar.getTime();
			aRxjournal.setEntryDate(aDate);	
			aRxjournal.setEntryMemo(theEntity);
			aRxjournal.setRxMasterId(theRxMasterId);
			aRxjournal.setUserLoginId(aUserBean.getUserId());
			if(theOperation.equals("add")){
				itsRolodexService.addRolodexJournal(aRxjournal);
			}else if(theOperation.equals("edit")){
				aRxjournal.setRxJournalId(theRxJournalId);
				itsRolodexService.updateRolodexJournal(aRxjournal);
			}else if(theOperation.equals("del")){
				aRxjournal.setRxJournalId(theRxJournalId);
				itsRolodexService.deleteRolodexJournal(aRxjournal);
			}
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRxMasterId:</b>"+theRxMasterId,"RolodexDetailsController",e,theSession,theRequest);
		}
		return 0;
	}
	
	@RequestMapping(value="/rolodexJournal",method = RequestMethod.GET)
	public @ResponseBody CustomResponse vendorJournal(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId) {
		itsLogger.debug("Sales rep id: " + theRolodexId);
		RxJournal aRxJounal = new RxJournal();
		aRxJounal.setRxMasterId(theRolodexId);
		ArrayList<RxJournal> aRxjournalsList = (ArrayList<RxJournal>) itsRolodexService .getRxJournalDetails(aRxJounal);
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aRxjournalsList);
		return aResponse;
	}
	public Rxaddress getRxAddressDetails() {
		return itsRxAddressDetails;
	}

	public void setRxAddressDetails(String theRolodexNumber, HttpSession theSession,HttpServletRequest theRequest, HttpServletResponse response) throws CompanyException, IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			this.itsRxAddressDetails = itsCompanyService.getEmployeeAddressDetails(theRolodexNumber);
		} catch (CompanyException e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexNumber:</b>"+theRolodexNumber,"RolodexDetailsController",e,theSession,theRequest);
		}
	}
	
	@RequestMapping(value="/updateRolodex",method = RequestMethod.POST)
	public @ResponseBody boolean rolodexUpdate(@RequestParam(value = "rolodexNumber", required = false) int theRolodexId,
																						@RequestParam(value = "isCustomer", required = false) boolean theCustomer,
																						@RequestParam(value = "isVendor", required = false) boolean theVendor,
																						@RequestParam(value = "isEmployee", required = false) boolean theEmployee,
																						@RequestParam(value = "isArchitect", required = false) boolean theArchitect,
																						@RequestParam(value = "isEngineer", required = false) boolean theEngineer,
																						@RequestParam(value = "isGCAndConstr", required = false) boolean theGCAndConstr,
																						@RequestParam(value = "isOwner", required = false) boolean theOwner,
																						@RequestParam(value = "isBondAgent", required = false) boolean theBondAgent,
																						@RequestParam(value = "category6", required = false) boolean category6,
																						@RequestParam(value = "category7", required = false) boolean category7,
																						@RequestParam(value = "category8", required = false) boolean category8,
																						HttpSession session,HttpServletRequest theRequest, HttpServletResponse response
																						) throws IOException, MessagingException {
		itsLogger.debug("Sales rep id: " + theRolodexId+category7);
		Rxmaster aRxmaster = new Rxmaster();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try{
			aRxmaster.setRxMasterId(theRolodexId);
			aRxmaster.setIsCustomer(theCustomer);
			aRxmaster.setIsVendor(theVendor);
			aRxmaster.setIsEmployee(theEmployee);
			aRxmaster.setIsCategory1(theEngineer);
			aRxmaster.setIsCategory2(theArchitect);
			aRxmaster.setIsCategory3(theGCAndConstr);
			aRxmaster.setIsCategory4(theOwner);
			aRxmaster.setIsCategory5(theBondAgent);
			aRxmaster.setIsCategory6(category6);
			aRxmaster.setIsCategory7(category7);
			aRxmaster.setIsCategory8(category8);
			
			itsRolodexService.updateRolodexDetails(aRxmaster,aUserBean.getUserId());
		}catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>theRolodexId:</b>"+theRolodexId,"RolodexDetailsController",e,session,theRequest);
		}
		return true;
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
}