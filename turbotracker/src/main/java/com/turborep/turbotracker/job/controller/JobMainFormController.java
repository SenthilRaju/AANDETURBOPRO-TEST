
package com.turborep.turbotracker.job.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoCustPO;
import com.turborep.turbotracker.job.dao.JobCustomerBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.jocategory;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.JoWizardAppletData;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping(value="/jobtabs1")
public class JobMainFormController {

	Logger itsLogger = Logger.getLogger(JobMainFormController.class); 

	private String itsCustomerName;
	private String itsJobNumber;
	private String itsAddress;
	private String itsCreatedEHB;
	private String itsChangedEHB;
	private String itsTableNameUserLogin="tsUserLogin";
	private String itsTableNameRxMaster="rxMaster";
	private String itsCutomerRxName;
	private Jomaster itsJomasterDetails;
	private List<CoTaxTerritory> itsJobSiteTaxValue;
	private List<UserBean> itsJuoteBy;
	private List<Codivision> itsDivision;
	private List<CoTaxTerritory> itsJobSiteTaxTerritory;
	private List<?> itsBidStatus;
	
	/**
	 * Getting the services
	 */
	@Resource(name="jobService")
	private JobService itsJobService;
	
	@Resource(name="salesServices")
	private SalesService itsSalesServices;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	@Resource(name = "sysService")
	private SysService itsSysService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@RequestMapping(value = "jobwizardmain", method = RequestMethod.GET)
	public String getJobsMainPage (	@RequestParam(value="jobNumber", required=false) String theJobNumber, 
																@RequestParam(value="jobName", required=false) String theJobName,
																@RequestParam(value="jobCustomer", required=false) String theJobCustomer,
																@RequestParam(value="joMasterID", required=false) Integer joMasterID,
																HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws IOException, JobException, UserException, MessagingException {
		String aAssignedSalesRep = null;
		String aAssignedCSR = null;
		String aAssignedSalesMGR = null;
		String aAssignedEngineers = null;
		String aAssignedProjMgr = null;
		String aAssignedtakeOff = null;
		String aAssignedQuoteBy = null;
		String aArchitect = null;
		String aEngineersRX = null;
		String aGeneralContractors = null;
		jocategory jocategorylist=null;
		boolean datathereornot=false;
		itsLogger.debug("Received request to show jobwizardmain page");
		Jomaster aJomaster = null;
		JoCustPO aCustPO = new  JoCustPO();
		try {
			jocategorylist=itsJobService.getJobCategories();
			if (session.getAttribute(SessionConstants.JOB_TOKEN)!=null && session.getAttribute(SessionConstants.JOB_TOKEN).equals("viewJob")) {
				JobCustomerBean aJobCustomerBean = new JobCustomerBean();
				aJobCustomerBean.setJobNumber(theJobNumber);
				aJobCustomerBean.setDescription(theJobName);
				aJobCustomerBean.setCustomerName(theJobCustomer);
				setJobNumber(theJobNumber);
				setJomasterDetails(theJobNumber,joMasterID);
				theModel.clear();
				theModel.addAttribute(SessionConstants.JOB_GRID_OBJ, aJobCustomerBean);
				aJomaster = getJomasterDetails();
				theModel.addAttribute("joMasterDetails", aJomaster);
				if(aJomaster != null){
						if(aJomaster.getCuAssignmentId0() != null){
							aAssignedSalesRep = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId0(), itsTableNameUserLogin);
						}
						if(aJomaster.getCuAssignmentId1() != null){
							aAssignedCSR = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId1(), itsTableNameUserLogin);
						}
						if(aJomaster.getCuAssignmentId2() != null){
							aAssignedSalesMGR = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId2(), itsTableNameUserLogin);
						}
						if(aJomaster.getCuAssignmentId3() != null){
							aAssignedEngineers = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId3(), itsTableNameUserLogin);
						}
						if(aJomaster.getCuAssignmentId4() != null){
							aAssignedProjMgr = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId4(), itsTableNameUserLogin);
						}
						if(aJomaster.getCuAssignmentId5() != null){
							aAssignedtakeOff = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId5(), itsTableNameUserLogin);
						}
						if(aJomaster.getCuAssignmentId6() != null){
							aAssignedQuoteBy = (String)itsSalesServices.getAssignedEmployeeName(aJomaster.getCuAssignmentId6(), itsTableNameUserLogin);
						}
						if (aJomaster.getRxCategory1() != null) {
								aArchitect = (String)itsCompanyService.getRolodexName(aJomaster.getRxCategory1(), itsTableNameRxMaster);
						}
						if (aJomaster.getRxCategory2() != null) {
							aEngineersRX = (String)itsCompanyService.getRolodexName(aJomaster.getRxCategory2(), itsTableNameRxMaster);
						}
						if (aJomaster.getRxCategory3() != null) {
							aGeneralContractors = (String)itsCompanyService.getRolodexName(aJomaster.getRxCategory3(), itsTableNameRxMaster);
						}
						setJobSiteTaxValue((List<CoTaxTerritory>)itsCompanyService.getCompanyTaxTerritory(aJomaster.getCoTaxTerritoryId(), itsTableNameRxMaster));
						if (aJomaster.getRxCustomerId() != null) {
							setCutomerRxName((String)itsCompanyService.getCustomerName(aJomaster.getRxCustomerId()));
						}
						if(aJomaster.getDescription() != null){
							String aString = aJomaster.getDescription();
							String aString1 = aString.replaceAll("\"", "");
							theModel.addAttribute("joMasterDescription", aString1);
						}
						if(aJomaster.getJoMasterId() != null){
							aCustPO = itsJobService.getSingleCusotomerPODetails(aJomaster.getJoMasterId());
							theModel.addAttribute("customerPODetails", aCustPO);
						}
						if(aJomaster.getJoMasterId() != null){
							 datathereornot=itsJobService.getsplitcommission_chkbox_status(aJomaster.getJoMasterId());
							System.out.println("jomasterId"+datathereornot);
						}
						
						theModel.addAttribute("AssignedSalesRep", aAssignedSalesRep);
						theModel.addAttribute("AssignedCSRs", aAssignedCSR);
						theModel.addAttribute("AssignedSalesMGRs", aAssignedSalesMGR);
						theModel.addAttribute("AssignedProjManagers", aAssignedProjMgr);
						theModel.addAttribute("AssignedEngineers", aAssignedEngineers);
						theModel.addAttribute("AssignedTakeOff", aAssignedtakeOff);
						theModel.addAttribute("AssignedQuoteBy", aAssignedQuoteBy);
						theModel.addAttribute("AssignedArchitects", aArchitect);
						theModel.addAttribute("AssignedEngineering", aEngineersRX);
						theModel.addAttribute("AssignedGcConstrMgrs", aGeneralContractors);
						theModel.addAttribute("Assignchkboxtickstatus",datathereornot);
						if (aJomaster.getRxCustomerId() != null && getCutomerRxName() != null) {
							String aString = getCutomerRxName();
							String aString2 = aString.replaceAll("'", "");
							theModel.addAttribute("CustomerName", aString2);
						}
						
				}
			}
			setDivision((List<Codivision>)itsCompanyService.getCompanyDivisions());
			theModel.addAttribute("divisions", getDivision());
			setBidStatusForBidDateForm((List<?>)itsJobService.getBidStatusList(0));
            theModel.addAttribute("bidStatusForBidDateForm", getBidStatusForBidDateForm());
			if(itsJobSiteTaxValue != null && aJomaster != null && !itsJobSiteTaxValue.isEmpty())
			{
				CoTaxTerritory aTaxterritory = getJobSiteTaxValue().get(0);
				theModel.addAttribute("taxTerritory", aTaxterritory.getCounty());
				theModel.addAttribute("taxValue", aTaxterritory.getTaxRate());
			} else {
				theModel.addAttribute("taxTerritory", "");
				theModel.addAttribute("taxValue", "0.00");
			}
			
			if(jocategorylist!=null){
				theModel.addAttribute("JobCategory1", jocategorylist.getCategory1desc());
				theModel.addAttribute("JobCategory2",jocategorylist.getCategory2desc());
				theModel.addAttribute("JobCategory3", jocategorylist.getCategory3desc());
				theModel.addAttribute("JobCategory4", jocategorylist.getCategory4desc());
				theModel.addAttribute("JobCategory5", jocategorylist.getCategory5desc());
				theModel.addAttribute("JobCategory6", jocategorylist.getCategory6desc());
				theModel.addAttribute("JobCategory7", jocategorylist.getCategory7desc());
			}
		} catch (CompanyException e) {
			sendTransactionException("<b>theJobNumber,theJobCustomer:</b>"+theJobNumber+","+theJobCustomer,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return "job/jobwizardmain";
	}

	@RequestMapping(value = "createjob", method = RequestMethod.POST)
	public @ResponseBody Jomaster createNewJob (@RequestParam(value="jobDescription", required=false) String theDescription,
																							@RequestParam(value="jobHeader_JobName_name", required=false) String theJobName_jobHeader,
																							@RequestParam(value="jobCustomer", required=false) String theRolodexCustomer,
																							@RequestParam(value="jobHeader_JobNumber_name", required=false) String theJobHeader_JobNumber_name,
																							@RequestParam(value="jobHeader_JobCustomer_name", required=false) String theJobHeader_JobCustomer_name, 
																							@RequestParam(value="empCustomerID", required=false) String theEmpCustomerID, 
																							@RequestParam(value="jobcontractorcustomer", required=false) String jobcontractorcustomer,
																							@RequestParam(value="locationAddress1", required=false) String theJoblocationAddress1,
																							@RequestParam(value="locationAddress2", required=false) String theJoblocationAddress2,
																							@RequestParam(value="joBidStatusId", required=false) Integer thejoBidStatusId,
																							@RequestParam(value="locationCity", required=false) String theJoblocationCity,
																							@RequestParam(value="locationState", required=false) String theJoblocationState,
																							@RequestParam(value="locationZip", required=false) String theJoblocationZip,
																							@RequestParam(value="bidDate", required=false) String theBidDate,
																							@RequestParam(value="bookedDate", required=false) String theBookedDate,
																							@RequestParam(value="closedDate", required=false) String theClosedDate,
																							@RequestParam(value="empSalesMan", required=false) Integer theCuAssignmentID0,
																							@RequestParam(value="empCSR", required=false) Integer theCuAssignmentID1,
																							@RequestParam(value="empSalesMgr", required=false) Integer theCuAssignmentID2,
																							@RequestParam(value="empEngineer", required=false) Integer theCuAssignmentID3,
																							@RequestParam(value="empPrjMgr", required=false) Integer theCuAssignmentID4,
																							@RequestParam(value="empTakeOff", required=false) Integer theCAssignmentID5,
																							@RequestParam(value="empQuoteBy", required=false) Integer theCuAssignmentID6,
																							@RequestParam(value="createdDate", required=false) String theCreatedDate,
																							@RequestParam(value="changedDate", required=false) String theChangedDate,
																							@RequestParam(value="teamArchitect", required=false) Integer theRxCategory1,
																							@RequestParam(value="teamEngineer", required=false) Integer theRxCategory2,
																							@RequestParam(value="teamGC", required=false) Integer theRxCategory3,
																							@RequestParam(value="coDivision", required=false) Integer theCoDivision,
																							@RequestParam(value="taxPersent", required=false) Integer theTaxTerritory,
																							@RequestParam(value="jobmainPONumber", required=false) String theCustomerPONumber,
																							@RequestParam(value="jobCustomerID", required=false) Integer theCustomerID,
																							@RequestParam(value="originalbidDate", required=false) String originalbidDate,
																							@RequestParam(value="webpageurltoopen", required=false) String webpageurltoopen,
																							HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws Exception {
		Jomaster aJoMaster = new Jomaster();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			aJoMaster.setLocationName(jobcontractorcustomer);
			aJoMaster.setDescription(theJobName_jobHeader);
			aJoMaster.setLocationAddress1(theJoblocationAddress1);
			aJoMaster.setLocationAddress2(theJoblocationAddress2);
			aJoMaster.setLocationCity(theJoblocationCity);
			aJoMaster.setLocationState(theJoblocationState);
			aJoMaster.setLocationZip(theJoblocationZip);
			aJoMaster.setJobStatus(0);
			aJoMaster.setCreatedById(aUserBean.getUserId());
			if(theBidDate != null && theBidDate.length() > 10) {
				aJoMaster.setBidDate(DateUtils.parseDate(theBidDate, new String[]{"MM/dd/yyyy hh:mm"}));
			} else if(theBidDate != null && theBidDate != "") {
				aJoMaster.setBidDate(DateUtils.parseDate(theBidDate, new String[]{"MM/dd/yyyy"}));
			}
			
			if(originalbidDate!=null && originalbidDate.length()>10){
				aJoMaster.setOriginalBidDate(DateUtils.parseDate(originalbidDate, new String[]{"MM/dd/yyyy hh:mm"}));
			}else if(originalbidDate != null && originalbidDate != ""){
				aJoMaster.setOriginalBidDate(DateUtils.parseDate(originalbidDate, new String[]{"MM/dd/yyyy"}));
			}
			if(theBookedDate != null && theBookedDate != "" && theBookedDate.length() > 10) {
				aJoMaster.setBookedDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy hh:mm"}));
			} else if(theBookedDate != null && theBookedDate != "") {
				aJoMaster.setBookedDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy"}));
			}
			if (theClosedDate != null && theClosedDate != "" && theClosedDate.length() > 10) {
				aJoMaster.setClosedDate(DateUtils.parseDate(theClosedDate, new String[]{"MM/dd/yyyy hh:mm"}));
			} else if (theClosedDate != null && theClosedDate != "") {
				aJoMaster.setClosedDate(DateUtils.parseDate(theClosedDate, new String[]{"MM/dd/yyyy"}));
			}
			System.out.println("Sales Man ID :: "+theCuAssignmentID0+"  :: Sales Mgr  ::  "+theCuAssignmentID2);
			aJoMaster.setCuAssignmentId0(theCuAssignmentID0);
			aJoMaster.setCuAssignmentId1(theCuAssignmentID1);
			aJoMaster.setCuAssignmentId2(theCuAssignmentID2);
			aJoMaster.setCuAssignmentId3(theCuAssignmentID3);
			aJoMaster.setCuAssignmentId4(theCuAssignmentID4);
			aJoMaster.setCuAssignmentId5(theCAssignmentID5);
			aJoMaster.setCuAssignmentId6(theCuAssignmentID6);
			aJoMaster.setRxCustomerId(theCustomerID);
			aJoMaster.setCustomerPonumber(theCustomerPONumber);
			aJoMaster.setJoBidStatusId(thejoBidStatusId);
			if(theCreatedDate != null  && theCreatedDate != "") {
				aJoMaster.setCreatedOn(DateUtils.parseDate(theCreatedDate, new String[]{"MM/dd/yyyy"}));
			}
			aJoMaster.setRxCategory1(theRxCategory1);
			aJoMaster.setRxCategory2(theRxCategory2);
			aJoMaster.setRxCategory3(theRxCategory3);
			aJoMaster.setCoDivisionId(theCoDivision);
			aJoMaster.setCoTaxTerritoryId(theTaxTerritory);
			aJoMaster.setCreatedOn(new Date());
			aJoMaster.setChangedOn(new Date());
			/*Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
			String format3 = new SimpleDateFormat("yy MM dd").format(now);
			String[] split = format3.split(" ");
			String jobNumber = "";
			if(theCuAssignmentID0 != null)
			{
				Emmaster aEmmaster = itsJobService.getSalesManDetails(theCuAssignmentID0);
				
				if(aEmmaster.getJobNumberGenerate())
				{
					//jobNumber = aEmmaster.getJobNumberPrefix()+""+aEmmaster.getJobNumberSequence();
					if(aEmmaster.getJobNumberPrefix().length() != 0 && aEmmaster.getJobNumberSequence() != 0)
					{
						jobNumber = aEmmaster.getJobNumberPrefix()+"-";
						String theJobNumber = itsJobService.getJobNumber(jobNumber);
						if(theJobNumber.trim().length() > 0)
						{
							String[] sp = theJobNumber.split("-");
							String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
							if(sLen.length() <= 1)
								jobNumber = aEmmaster.getJobNumberPrefix()+"-000"+sLen;
							else if(sLen.length() <= 2)
								jobNumber = aEmmaster.getJobNumberPrefix()+"-00"+sLen;
							else if(sLen.length() <= 3)
								jobNumber = aEmmaster.getJobNumberPrefix()+"-0"+sLen;
							else 
								jobNumber = aEmmaster.getJobNumberPrefix()+"-"+sLen;
						}
						else
						{
							jobNumber = aEmmaster.getJobNumberPrefix()+"-"+aEmmaster.getJobNumberSequence().toString();
						}
					}
					else if(aEmmaster.getJobNumberSequence() == null || aEmmaster.getJobNumberSequence() == 0)
					{
						jobNumber = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1]+"-";
						String theJobNumber = itsJobService.getJobNumber(jobNumber);
						if(theJobNumber.trim().length() > 0)
						{
							String[] sp = theJobNumber.split("-");
							String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
							if(sLen.length() <= 1)
								jobNumber = aEmmaster.getJobNumberPrefix()+"-000"+sLen;
							else if(sLen.length() <= 2)
								jobNumber = aEmmaster.getJobNumberPrefix()+"-00"+sLen;
							else if(sLen.length() <= 3)
								jobNumber = aEmmaster.getJobNumberPrefix()+"-0"+sLen;
							else 
								jobNumber = aEmmaster.getJobNumberPrefix()+"-"+sLen;
							
						}
						else
						{
							jobNumber = aEmmaster.getJobNumberPrefix()+""+split[0]+""+split[1]+"-0001";
						}
					}
					else if(aEmmaster.getJobNumberPrefix() == null || aEmmaster.getJobNumberPrefix().length() == 0)
					{
						jobNumber = aEmmaster.getJobNumberSequence().toString();
						String theJobNumber = itsJobService.getJobNumber(jobNumber);
						if(theJobNumber.trim().length() > 0)
						{
							String sLen = String.valueOf(Integer.valueOf(theJobNumber)+1);
							if(sLen.length() <= 1)
								jobNumber = split[0]+""+split[1]+"-000"+sLen;
							else if(sLen.length() <= 2)
								jobNumber = split[0]+""+split[1]+"-00"+sLen;
							else if(sLen.length() <= 3)
								jobNumber = split[0]+""+split[1]+"-0"+sLen;
							else 
								jobNumber = split[0]+""+split[1]+"-"+sLen;
						}
						else
						{
							jobNumber = String.valueOf(aEmmaster.getJobNumberSequence());
						}
						
					}
					else{
						jobNumber = split[0]+""+split[1]+"-";
						String theJobNumber = itsJobService.getJobNumber(jobNumber);
						if(theJobNumber.trim().length() > 0)
						{
							String[] sp = theJobNumber.split("-");
							String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
							if(sLen.length() <= 1)
								jobNumber = split[0]+""+split[1]+"-000"+sLen;
							else if(sLen.length() <= 2)
								jobNumber = split[0]+""+split[1]+"-00"+sLen;
							else if(sLen.length() <= 3)
								jobNumber = split[0]+""+split[1]+"-0"+sLen;
							else 
								jobNumber = split[0]+""+split[1]+"-"+sLen;
						}
						else
						{
							jobNumber = split[0]+""+split[1]+"-0001";
						}
					}
					aJoMaster.setJobNumber(jobNumber);
				}
				else
				{
					jobNumber = split[0]+""+split[1]+"-";
					String theJobNumber = itsJobService.getJobNumber(jobNumber);
					if(theJobNumber.trim().length() > 0)
					{
						String[] sp = theJobNumber.split("-");
						String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
						if(sLen.length() <= 1)
							jobNumber = split[0]+""+split[1]+"-000"+sLen;
						else if(sLen.length() <= 2)
							jobNumber = split[0]+""+split[1]+"-00"+sLen;
						else if(sLen.length() <= 3)
							jobNumber = split[0]+""+split[1]+"-0"+sLen;
						else 
							jobNumber = split[0]+""+split[1]+"-"+sLen;
					}
					else
					{
						jobNumber = split[0]+""+split[1]+"-0001";
					}
					aJoMaster.setJobNumber(jobNumber);
				}
			}
			else
			{

				jobNumber = split[0]+""+split[1]+"-";
				String theJobNumber = itsJobService.getJobNumber(jobNumber);
				if(theJobNumber.trim().length() > 0)
				{
					String[] sp = theJobNumber.split("-");
					String sLen = String.valueOf(Integer.valueOf(sp[1])+1);
					if(sLen.length() <= 1)
						jobNumber = split[0]+""+split[1]+"-000"+sLen;
					else if(sLen.length() <= 2)
						jobNumber = split[0]+""+split[1]+"-00"+sLen;
					else if(sLen.length() <= 3)
						jobNumber = split[0]+""+split[1]+"-0"+sLen;
					else 
						jobNumber = split[0]+""+split[1]+"-"+sLen;
				}
				else
				{
					jobNumber = split[0]+""+split[1]+"-0001";
				}
				aJoMaster.setJobNumber(jobNumber);
			
			}*/
			aJoMaster.setJobNumber(itsSysService.getSysSequenceNumber("joMaster").toString());
			Integer joMasterID=itsJobService.add(aJoMaster);
			aJoMaster.setJoMasterId(joMasterID);
			if(webpageurltoopen!=null && webpageurltoopen!=""){
				JoWizardAppletData ajowizAppletData=new JoWizardAppletData();
				ajowizAppletData.setAppletLocalUrl(webpageurltoopen);
				ajowizAppletData.setJobNumber(aJoMaster.getJobNumber());
				ajowizAppletData.setUserLoginId(aUserBean.getUserId());
				ajowizAppletData.setJoMasterID(joMasterID);
				itsJobService.updatejowizardAppletData(ajowizAppletData);
			}
		} catch (JobException e) {
			sendTransactionException("<b>theJobNumber:</b>"+aJoMaster.getJobNumber(),"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		itsLogger.info("========================== Job Inserted ===========================");
		return aJoMaster;
	}
	
	@RequestMapping(value = "addNewEngineer", method = RequestMethod.POST)
	public @ResponseBody Rxmaster createNewEngineer (@RequestParam(value="name", required=false) String theName,
																									@RequestParam(value="USPhoneNumber", required=false) String thePhone1,
																									@RequestParam(value="USPhone_Number", required=false) String thePhone2,
																									@RequestParam(value="fax", required=false) String theFax,
																									@RequestParam(value="locationAddress1", required=false) String thelocationAddress1,
																									@RequestParam(value="locationAddress2", required=false) String thelocationAddress2,
																									@RequestParam(value="locationCity", required=false) String thelocationCity,
																									@RequestParam(value="locationState", required=false) String thelocationState,
																									@RequestParam(value="locationZip", required=false) String thelocationZip,
																									HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Rxmaster aRxmaster=new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		try{
			aRxmaster.setFirstName("");
			aRxmaster.setName(theName);
			aRxmaster.setPhone1(thePhone1);
			aRxmaster.setPhone2(thePhone2);
			aRxmaster.setIsCategory1(false);
			aRxmaster.setIsCategory2(true);
			aRxmaster.setIsCategory3(false);
			aRxmaster.setIsCategory4(false);
			aRxmaster.setIsCategory5(false);
			aRxmaster.setIsProspect(false);
			aRxmaster.setInActive(false);
			aRxmaster.setIsVendor(false);
			aRxmaster.setIsEmployee(false);
			aRxmaster.setIsCustomer(false);
			aRxmaster.setSearchName("");
			aRxmaster.setFax(theFax);
			aRxaddress.setAddress1(thelocationAddress1);
			aRxaddress.setAddress2(thelocationAddress2);
			aRxaddress.setCity(thelocationCity);
			aRxaddress.setState(thelocationState);
			aRxaddress.setZip(thelocationZip);
			aRxaddress.setInActive(false);
			aRxaddress.setIsBillTo(false);
			aRxaddress.setIsMailing(false);
			aRxaddress.setIsStreet(false);
			aRxaddress.setIsShipTo(false);
			itsJobService.addDesignTeam(aRxmaster, aRxaddress);
		}catch (JobException e){
			sendTransactionException("<b>EngineerName:</b>"+theName,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxmaster;
	}
	
	@RequestMapping(value = "addNewArchitect", method = RequestMethod.POST)
	public @ResponseBody Rxmaster createNewArchitect (@RequestParam(value="name", required=false) String theName,
																									@RequestParam(value="USPhoneNumber", required=false) String thePhone1,
																									@RequestParam(value="USPhone_Number", required=false) String thePhone2,
																									@RequestParam(value="fax", required=false) String theFax,
																									@RequestParam(value="locationAddress1", required=false) String thelocationAddress1,
																									@RequestParam(value="locationAddress2", required=false) String thelocationAddress2,
																									@RequestParam(value="locationCity", required=false) String thelocationCity,
																									@RequestParam(value="locationState", required=false) String thelocationState,
																									@RequestParam(value="locationZip", required=false) String thelocationZip,
																									HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Rxmaster aRxmaster=new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		try{
			aRxmaster.setFirstName("");
			aRxmaster.setName(theName);
			aRxmaster.setPhone1(thePhone1);
			aRxmaster.setPhone2(thePhone2);
			aRxmaster.setIsCategory1(true);
			aRxmaster.setIsCategory2(false);
			aRxmaster.setIsCategory3(false);
			aRxmaster.setIsCategory4(false);
			aRxmaster.setIsCategory5(false);
			aRxmaster.setIsProspect(false);
			aRxmaster.setInActive(false);
			aRxmaster.setIsVendor(false);
			aRxmaster.setIsEmployee(false);
			aRxmaster.setIsCustomer(false);
			aRxmaster.setSearchName("");
			aRxmaster.setFax(theFax);
			aRxaddress.setAddress1(thelocationAddress1);
			aRxaddress.setAddress2(thelocationAddress2);
			aRxaddress.setCity(thelocationCity);
			aRxaddress.setState(thelocationState);
			aRxaddress.setZip(thelocationZip);
			aRxaddress.setInActive(false);
			aRxaddress.setIsBillTo(false);
			aRxaddress.setIsMailing(false);
			aRxaddress.setIsStreet(false);
			aRxaddress.setIsShipTo(false);
			itsJobService.addDesignTeam(aRxmaster, aRxaddress);
		}catch (JobException e){
			sendTransactionException("<b>ArchitectName:</b>"+theName,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxmaster;
	}
	
	@RequestMapping(value = "addNewContractor", method = RequestMethod.POST)
	public @ResponseBody Rxmaster createNewContractor (@RequestParam(value="name", required=false) String theName,
																										@RequestParam(value="USPhoneNumber", required=false) String thePhone1,
																										@RequestParam(value="USPhone_Number", required=false) String thePhone2,
																										@RequestParam(value="fax", required=false) String theFax,
																										@RequestParam(value="locationAddress1", required=false) String thelocationAddress1,
																										@RequestParam(value="locationAddress2", required=false) String thelocationAddress2,
																										@RequestParam(value="locationCity", required=false) String thelocationCity,
																										@RequestParam(value="locationState", required=false) String thelocationState,
																										@RequestParam(value="locationZip", required=false) String thelocationZip,
																										HttpSession session,HttpServletRequest therequest, ModelMap theModel, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		Rxmaster aRxmaster=new Rxmaster();
		Rxaddress aRxaddress = new Rxaddress();
		try{
			aRxmaster.setFirstName("");
			aRxmaster.setName(theName);
			aRxmaster.setPhone1(thePhone1);
			aRxmaster.setPhone2(thePhone2);
			aRxmaster.setIsCategory1(false);
			aRxmaster.setIsCategory2(false);
			aRxmaster.setIsCategory3(true);
			aRxmaster.setIsCategory4(false);
			aRxmaster.setIsCategory5(false);
			aRxmaster.setIsProspect(false);
			aRxmaster.setInActive(false);
			aRxmaster.setIsVendor(false);
			aRxmaster.setIsEmployee(false);
			aRxmaster.setIsCustomer(false);
			aRxmaster.setSearchName("");
			aRxmaster.setFax(theFax);
			aRxaddress.setAddress1(thelocationAddress1);
			aRxaddress.setAddress2(thelocationAddress2);
			aRxaddress.setCity(thelocationCity);
			aRxaddress.setState(thelocationState);
			aRxaddress.setZip(thelocationZip);
			aRxaddress.setInActive(false);
			aRxaddress.setIsBillTo(false);
			aRxaddress.setIsMailing(false);
			aRxaddress.setIsStreet(false);
			aRxaddress.setIsShipTo(false);
			itsJobService.addDesignTeam(aRxmaster, aRxaddress);
		}catch (JobException e){
			sendTransactionException("<b>ArchitectName:</b>"+theName,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aRxmaster;
	}
	
	public String getCustomerName() {
		return itsCustomerName;
	}
	public void setCustomerName(String customerName) {
		this.itsCustomerName = customerName;
	}
	public String getJobNumber() {
		return itsJobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.itsJobNumber = jobNumber;
	}
	public Jomaster getJomasterDetails() {
		return itsJomasterDetails;
	}
	public void setJomasterDetails(String jobNumber,Integer joMasterID) throws JobException {
		this.itsJomasterDetails = itsJobService.getSingleJobDetails(jobNumber,joMasterID);
	}
	public String getAddress() {
		return itsAddress;
	}
	public void setAddress(String address) {
		this.itsAddress = address;
	}
	public String getCreatedEHB() {
		return itsCreatedEHB;
	}
	public void setCreatedEHB(String createdEHB) {
		this.itsCreatedEHB = createdEHB;
	}
	
	public String getChangedEHB() {
		return itsChangedEHB;
	}
	public void setChangedEHB(String changedEHB) {
		this.itsChangedEHB = changedEHB;
	}
	
	public List<UserBean> getQuoteBy() {
		return itsJuoteBy;
	}
	public void setQuoteBy(List<UserBean> quoteBy) {
		this.itsJuoteBy = quoteBy;
	}
	public List<Codivision> getDivision() {
		return itsDivision;
	}
	public void setDivision(List<Codivision> division) {
		this.itsDivision = division;
	}
	
	public List<CoTaxTerritory> getJobSiteTaxTerritory() {
		return itsJobSiteTaxTerritory;
	}
	public void setJobSiteTaxTerritory(List<CoTaxTerritory> jobSiteTaxTerritory) {
		this.itsJobSiteTaxTerritory = jobSiteTaxTerritory;
	}
	public String getCutomerRxName() {
		return itsCutomerRxName;
	}

	public void setCutomerRxName(String cutomerRxName) {
		this.itsCutomerRxName = cutomerRxName;
	}

	public List<CoTaxTerritory> getJobSiteTaxValue() {
		return itsJobSiteTaxValue;
	}

	public void setJobSiteTaxValue(List<CoTaxTerritory> jobSiteTaxValue) {
		this.itsJobSiteTaxValue = jobSiteTaxValue;
	}

	@RequestMapping(value="/jobCustomerName", method = RequestMethod.GET)
	public @ResponseBody String getCustomerName(@RequestParam(value= "jobCustomerID") String theCustomerID,
			HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws JobException, IOException, MessagingException{
		itsLogger.debug("Received request to get search Jobs Lists");
		String aCustomerID = "";
		try{
			aCustomerID = itsJobService.getCustomerName(theCustomerID);
			if(aCustomerID == null){
				aCustomerID = "";
			}
		}catch (JobException e) {
			sendTransactionException("<b>theCustomerID:</b>"+theCustomerID,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aCustomerID;
	}
	
	@RequestMapping(value="/saveCustomerPODetails", method = RequestMethod.POST)
	public @ResponseBody JoCustPO saveCustomerPODetails(@RequestParam(value="customerPODetails[]", required= false) ArrayList<?> theCustomerPODetails,
														@RequestParam(value="aCustomerPOReqObj", required= false) String aCustomerPOReqObj,
														HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws ParseException, IOException, MessagingException{
		JoCustPO aJoCustPO = new  JoCustPO();
		Jomaster aJomaster = new  Jomaster();
		JoCustPO aJoCustPOReturnValue = null;
		Integer aCustomerPOID = null;
		JSONObject aCustPOsObject = null;
		try{
			aCustPOsObject = new JSONObject(aCustomerPOReqObj);
			JSONObject object1 = aCustPOsObject.getJSONObject("aRequestObj1");
			JSONObject object2 = aCustPOsObject.getJSONObject("aRequestObj2");
			JSONObject object3 = aCustPOsObject.getJSONObject("aRequestObj3");
			JSONObject object4 = aCustPOsObject.getJSONObject("aRequestObj4");
			JSONObject object5 = aCustPOsObject.getJSONObject("aRequestObj5");
			JSONObject object6 = aCustPOsObject.getJSONObject("aRequestObj6");

			/**
			 * {
			 * "aRequestObj1":{"covered_material1":"test1","po_number1":"123-456","contractAmount":"1500.00"},
			 * "aRequestObj2":{"covered_material2":"test2","po_number2":"123-789","po_amount2":"120"},
			 * "aRequestObj3":{"covered_material3":"test3","po_number3":"111-110","po_amount3":"100"},
			 * "aRequestObj4":{"covered_material4":"","po_number4":"","po_amount4":"0"},
			 * "aRequestObj5":{"covered_material5":"","po_number5":"","po_amount5":"0"},
			 * "aRequestObj6":{"covered_material6":"","po_number6":"","po_amount6":"0"},
			 * "joMasterID":"38064"
			 * }
			 * */
			
			/**
			 * [test1, 123-456, 1500.00, test2, 123-789, 120, test3, 111-110, 100, , , 0, , , 0, , , 0, 38064]
			 * */
			Integer joCustPoid;
			Integer joMasterId = aCustPOsObject.getInt("joMasterID");
			String podesc0 = object1.getString("covered_material1");
			String podesc1 = object2.getString("covered_material2");
			String podesc2 = object3.getString("covered_material3");
			String podesc3 = object4.getString("covered_material4");
			String podesc4 = object5.getString("covered_material5");
			String podesc5 = object6.getString("covered_material6");
			String customerPonumber1 = object1.getString("po_number1");
			String customerPonumber2 = object2.getString("po_number2");
			String customerPonumber3 = object3.getString("po_number3");
			String customerPonumber4 = object4.getString("po_number4");
			String customerPonumber5 = object5.getString("po_number5");
			String customerPonumber6 = object6.getString("po_number6");
			BigDecimal poamount0 = BigDecimal.valueOf(object1.getDouble("contractAmount"));
			BigDecimal poamount1 = BigDecimal.valueOf(object2.getDouble("po_amount2"));
			BigDecimal poamount2 = BigDecimal.valueOf(object3.getDouble("po_amount3"));
			BigDecimal poamount3 = BigDecimal.valueOf(object4.getDouble("po_amount4"));
			BigDecimal poamount4 = BigDecimal.valueOf(object5.getDouble("po_amount5"));
			BigDecimal poamount5 = BigDecimal.valueOf(object6.getDouble("po_amount6"));
			
			if(!podesc0.equals("") && podesc0 != null) {aJoCustPO.setPodesc0(podesc0);}
			if(!podesc1.equals("") && podesc1 != null) {aJoCustPO.setPodesc1(podesc1);}
			if(!podesc2.equals("") && podesc2 != null) {aJoCustPO.setPodesc2(podesc2);}
			if(!podesc3.equals("") && podesc3 != null) {aJoCustPO.setPodesc3(podesc3);}
			if(!podesc4.equals("") && podesc4 != null) {aJoCustPO.setPodesc4(podesc4);}
			if(!podesc5.equals("") && podesc5 != null) {aJoCustPO.setPodesc5(podesc5);}

			if(!customerPonumber1.equals("") && customerPonumber1 != null) {aJomaster.setCustomerPonumber(customerPonumber1);}
			if(!customerPonumber2.equals("") && customerPonumber2 != null) {aJoCustPO.setCustomerPonumber1(customerPonumber2);}
			if(!customerPonumber3.equals("") && customerPonumber3 != null) {aJoCustPO.setCustomerPonumber2(customerPonumber3);}
			if(!customerPonumber4.equals("") && customerPonumber4 != null) {aJoCustPO.setCustomerPonumber3(customerPonumber4);}
			if(!customerPonumber5.equals("") && customerPonumber5 != null) {aJoCustPO.setCustomerPonumber4(customerPonumber5);}
			if(!customerPonumber6.equals("") && customerPonumber6 != null) {aJoCustPO.setCustomerPonumber5(customerPonumber6);}
			
			if(!poamount0.equals("") && poamount0 != null) {aJoCustPO.setPoamount0(poamount0);}
			if(!poamount1.equals("") && poamount1 != null) {aJoCustPO.setPoamount1(poamount1);}
			if(!poamount2.equals("") && poamount2 != null) {aJoCustPO.setPoamount2(poamount2);}
			if(!poamount3.equals("") && poamount3 != null) {aJoCustPO.setPoamount3(poamount3);}
			if(!poamount4.equals("") && poamount4 != null) {aJoCustPO.setPoamount4(poamount4);}
			if(!poamount5.equals("") && poamount5 != null) {aJoCustPO.setPoamount5(poamount5);}
			
			
			/*if(theCustomerPODetails.get(0) != "" && theCustomerPODetails.get(0) != null){
				aJoCustPO.setPodesc0((String) theCustomerPODetails.get(0));
			}
			if(theCustomerPODetails.get(1) != "" && theCustomerPODetails.get(1) != null){
				aJomaster.setCustomerPonumber((String) theCustomerPODetails.get(1));
			}
			if(theCustomerPODetails.get(2) != "" && theCustomerPODetails.get(2) != null){
				BigDecimal aPOAmount0 = new BigDecimal((String) theCustomerPODetails.get(2));
				aJoCustPO.setPoamount0(aPOAmount0);
			}
			if(theCustomerPODetails.get(3) != "" && theCustomerPODetails.get(3) != null){
				aJoCustPO.setPodesc1((String) theCustomerPODetails.get(3));
			}
			if(theCustomerPODetails.get(4) != "" && theCustomerPODetails.get(4) != null){
				aJoCustPO.setCustomerPonumber1((String) theCustomerPODetails.get(4));
			}
			if(theCustomerPODetails.get(5) != "" && theCustomerPODetails.get(5) != null){
				BigDecimal aPOAmount1 = new BigDecimal((String) theCustomerPODetails.get(5));
				aJoCustPO.setPoamount1(aPOAmount1);
			}
			if(theCustomerPODetails.get(6) != "" && theCustomerPODetails.get(6) != null){
				aJoCustPO.setPodesc2((String) theCustomerPODetails.get(6));
			}
			if(theCustomerPODetails.get(7) != "" && theCustomerPODetails.get(7) != null){
				aJoCustPO.setCustomerPonumber2((String) theCustomerPODetails.get(7));
			}
			if(theCustomerPODetails.get(8) != "" && theCustomerPODetails.get(8) != null){
				BigDecimal aPOAmount2 = new BigDecimal((String) theCustomerPODetails.get(8));
				aJoCustPO.setPoamount2(aPOAmount2);
			}
			if(theCustomerPODetails.get(9) != "" && theCustomerPODetails.get(9) != null){
				aJoCustPO.setPodesc3((String) theCustomerPODetails.get(9));
			}
			if(theCustomerPODetails.get(10) != "" && theCustomerPODetails.get(10) != null){
				aJoCustPO.setCustomerPonumber3((String) theCustomerPODetails.get(10));
			}
			if(theCustomerPODetails.get(11) != "" && theCustomerPODetails.get(11) != null){
				BigDecimal aPOAmount3 = new BigDecimal((String) theCustomerPODetails.get(11));
				aJoCustPO.setPoamount3(aPOAmount3);
			}
			if(theCustomerPODetails.get(12) != "" && theCustomerPODetails.get(12) != null){
				aJoCustPO.setPodesc4((String) theCustomerPODetails.get(12));
			}
			if(theCustomerPODetails.get(13) != "" && theCustomerPODetails.get(13) != null){
				aJoCustPO.setCustomerPonumber4((String) theCustomerPODetails.get(13));
			}
			if(theCustomerPODetails.get(14) != "" && theCustomerPODetails.get(14) != null){
				BigDecimal aPOAmount4 = new BigDecimal((String) theCustomerPODetails.get(14));
				aJoCustPO.setPoamount4(aPOAmount4);
			}
			if(theCustomerPODetails.get(15) != "" && theCustomerPODetails.get(15) != null){
				aJoCustPO.setPodesc5((String) theCustomerPODetails.get(15));
			}
			if(theCustomerPODetails.get(16) != "" && theCustomerPODetails.get(16) != null){
				aJoCustPO.setCustomerPonumber5((String) theCustomerPODetails.get(16));
			}
			if(theCustomerPODetails.get(17) != "" && theCustomerPODetails.get(17) != null){
				BigDecimal aPOAmount5 = new BigDecimal((String) theCustomerPODetails.get(17));
				aJoCustPO.setPoamount5(aPOAmount5);
			}*/
			if(joMasterId != null){
				aJoCustPO.setJoMasterId(joMasterId);
				aJomaster.setJoMasterId(joMasterId);
				aCustomerPOID = itsJobService.getJoCustPOID(joMasterId);
			}
			if(aCustomerPOID != null){
				aJoCustPO.setOper("edit");
				aJoCustPO.setJoCustPoid(aCustomerPOID);
			}else{
				aJoCustPO.setOper("add");
			}
			aJoCustPOReturnValue = itsJobService.saveCustomerPONumner(aJoCustPO, aJomaster);
		}catch (JobException e) {
			sendTransactionException("<b>aCustomerPOReqObj:</b>"+aCustomerPOReqObj,"JOB",e,session,therequest);
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aJoCustPOReturnValue;
	}
	public List<?> getBidStatusForBidDateForm() {
        
		return itsBidStatus;
    }
    public void setBidStatusForBidDateForm(List<?> bidStatus) {
        this.itsBidStatus = bidStatus;
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
