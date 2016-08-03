package com.turborep.turbotracker.job.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Ecsplitcode;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoQuoteCategory;
import com.turborep.turbotracker.job.dao.JobHistory;
import com.turborep.turbotracker.job.dao.Jobidder;
import com.turborep.turbotracker.job.dao.Jobidstatus;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.exception.QuoteTemplateException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.JoWizardAppletData;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/job_controller")
public class JobController {

	protected static Logger logger = Logger.getLogger(JobController.class);
	
	@Resource(name="jobService")
	private JobService jobService;
	
	@Resource(name="userLoginService")
	private UserService userService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;

	@Resource(name = "customerService")
	private CustomerService itsCuMasterService;
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;
	
	@Resource(name = "companyService")
	private CompanyService	itsCompanyService;
	/**
	 * The default method when a request to /users is made.
	 * This essentially retrieves all users, which are wrapped inside a CustomUserResponse object.
	 * The object is automatically converted to JSON when returning back the response.
	 * The @ResponseBody is responsible for this behavior.
	 * @throws JobException 
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAll(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord, HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		int from, to;
		CustomResponse response = null;
		response = new CustomResponse();
		List<?> jobs = null;
		try{
			BigInteger aTotalCount = jobService.getJobsCount();
			to = (rows * page);
			from = to - rows;
			if (to > aTotalCount.intValue()) to = aTotalCount.intValue();
			// Retrieve all users from the service
			jobs = jobService.getCustomJobs(from, rows);
			// Initialize our custom user response wrapper
			// Assign the result from the service to this response
			response.setRows(jobs);
			// Assign the total number of records found. This is used for paging
			response.setRecords( String.valueOf(jobs.size()) );
			response.setPage( page );
			//response.setTotal(aBigInt.intValue());
			response.setTotal((int) Math.ceil((double)aTotalCount.intValue() / (double) rows));
			// Return the response
			// Spring will automatically convert our CustomResponse as JSON object. 
			// This is triggered by the @ResponseBody annotation. 
			// It knows this because the JqGrid has set the headers to accept JSON format when it made a request
			// Spring by default uses Jackson to convert the object to JSON
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>MethodName:</b>getAll","JOB",e,session,therequest);
		}
		return response;
	}

	@RequestMapping(value="/jobStatus",method = RequestMethod.GET)
	public @ResponseBody String getJobStatus (@RequestParam(value = "jobNumber", required = false) String jobNumber,
											@RequestParam(value = "jobStatus", required = false) Integer status,
											@RequestParam(value = "dateType", required = false) String dateType,
											@RequestParam(value = "date", required = false) String date,
											@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
											HttpServletResponse theResponse,HttpSession session, HttpServletRequest therequest) throws JobException, ParseException, IOException, MessagingException {
		Date aDate = null;
		String Gseqnum="0";
		try {
			aDate = DateUtils.parseDate(date, new String[]{"MM/dd/yyyy"});
			Integer jobStatus = jobService.UpdateJobStatus(dateType, aDate, joMasterID, status);
			if(status == 3){ //7/24/2013
				Jomaster aJoMasterByID = jobService.getJoMasterByJobNumber(jobNumber);
				
				if(JobUtil.containsOnlyNumbers(aJoMasterByID.getJobNumber())){
				if(aJoMasterByID!=null)
				{
				TsUserLogin aUserSalesRep = userService.getUserDetails(aJoMasterByID.getCuAssignmentId0());
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
				Jomaster aJomaster = new Jomaster();
				Jomaster aJomaster2 = null;
				String jobNumberNew = "";
				logger.info("Custemer AssignmentID: "+aJoMasterByID.getCuAssignmentId0()+" rxCustomerId: "+aJoMasterByID.getRxCustomerId());
				if(aJoMasterByID.getCuAssignmentId0() != null)
					{
					Emmaster aEmmaster = jobService.getEmployeeDetailLoginID(aJoMasterByID.getCuAssignmentId0());
					logger.info("EmMaster : "+aEmmaster.getJobNumberGenerate() +" "+aEmmaster.getJobNumberPrefix() +" "+aEmmaster.getJobNumberSequence());
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
						aJomaster.setJobNumber(jobNumberNew);
						
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
						aJomaster.setJobNumber(jobNumberNew);
					
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
					aJomaster.setJobNumber(jobNumberNew);
				}
				
				aJomaster.setJoMasterId(joMasterID);
			    /*aNewJobNumber = aNewJobNumber+"-";
			    String aNewJobString = jobService.getNewJobNumber(aNewJobNumber);*/
				//aJomaster.setJobNumber(aNewBookedJobNumber);
				aJomaster.setQuoteNumber(jobNumber);
				if(Gseqnum==null){
					Gseqnum ="001";
				}
				aJomaster.setSeqnum(Gseqnum);
				aJomaster2 = jobService.updateORIJobNumber(aJomaster);
				Jomaster aJomaster3 = new Jomaster();
				aJomaster3.setJobNumber(jobNumber);
				jobService.deleteRecentlyOpendJob(aJomaster3);
				return jobNumberNew;
				}
			}
				else{
					return aJoMasterByID.getJobNumber();
				}
			}
			return jobStatus.toString();
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			sendTransactionException("<b>jobNumber, status, dateType, date:</b>"+jobNumber+","+status+","+dateType+","+date,"JOB",e,session,therequest);
			return "0";
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>jobNumber, status, dateType, date:</b>"+jobNumber+","+status+","+dateType+","+date,"JOB",e,session,therequest);
			return "0";
		}
	}
	
	// CreatedBy : Naveed		Date: 02-Sep -2014
	// Desc : Check the jobNumber length and fixed to 4 digit
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

	@RequestMapping(value="/updateJobStatus",method = RequestMethod.GET)
	public @ResponseBody boolean getUpdateStatus (@RequestParam(value = "jobNumber", required = false) String theJobNumber,
											     @RequestParam(value = "jobStatus", required = false) Integer theStatus,
											     @RequestParam(value = "joMasterID", required = false) Integer joMasterID,
											     HttpServletRequest therequest,HttpSession session,HttpServletResponse theResponse) throws IOException, MessagingException {
	   String ajobno = theJobNumber.replace(" ", "");
	   Jomaster aJomaster =new Jomaster();
	   try{
		   aJomaster.setJobNumber(ajobno);
		   aJomaster.setJobStatus(theStatus);
		   aJomaster.setJoMasterId(joMasterID);
		   jobService.UpdateStatus(aJomaster);
	   }catch (JobException e) {
		logger.error(e.getMessage(), e);
		theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		sendTransactionException("<b>jobNumber, status</b>"+theJobNumber+","+theStatus,"JOB",e,session,therequest);
	   }
	    return false;
	}
	
	@RequestMapping(value="/jobHistory", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getHistory(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord, 
			HttpSession session ,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try{
			List<JobHistory> jobs = (List<JobHistory>) jobService.getJobsHistory();
			response.setRows(jobs);
			response.setRecords( String.valueOf(jobs.size()) );
			response.setPage( page );
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>Method Name:</b>jobHistory","JOB",e,session,therequest);
		}
		return response;
	}
	
	@RequestMapping(value="/jobStatusHome", method = RequestMethod.GET)
	public @ResponseBody String getJobStatusHome(@RequestParam(value="jobNumber",required=false) String theJobNumber,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpServletResponse theResponse,HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException{
		Date adate=null;
		String aJobName = "";
		try{
			Integer ajobStatus = jobService.getJobStaus(joMasterID);
			aJobName = jobService.getjobStatusName(ajobStatus);
			if(ajobStatus != 3 && ajobStatus != 4 && ajobStatus != 5){
				jobService.UpdateJobStatus("", adate,joMasterID, ajobStatus);
			}
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theJobNumber:</b>"+theJobNumber,"JOB",e,session,therequest);
		}
		return aJobName;
	}
	
	@RequestMapping(value="/updatejob",method = RequestMethod.POST)
	public @ResponseBody Jomaster updateJob ( @RequestParam(value="jobDescription", required=false) String theDescription,
								@RequestParam(value="jobCusromerId", required=false) Integer theRolodexCustomer,
								@RequestParam(value="jobcontractorcustomer", required=false) String jobcontractorcustomer,
								@RequestParam(value="locationAddress1", required=false) String theJoblocationAddress1,
								@RequestParam(value="locationAddress2", required=false) String theJoblocationAddress2,
								@RequestParam(value="locationCity", required=false) String theJoblocationCity,
								@RequestParam(value="locationState", required=false) String theJoblocationState,
								@RequestParam(value="locationZip", required=false) String theJoblocationZip,
								@RequestParam(value="bidDate", required=false) String theBidDate,
								@RequestParam(value="bookedDate", required=false) String theBookedDate,
								@RequestParam(value="closedDate", required=false) String theClosedDate,
								@RequestParam(value="empSalesMan", required=false) Integer cuAssignmentID0,
								@RequestParam(value="empCSR", required=false) Integer cuAssignmentID1,
								@RequestParam(value="empSalesMgr", required=false) Integer cuAssignmentID2,
								@RequestParam(value="joBidStatusId", required=false) Integer thejoBidStatusId,
								@RequestParam(value="empEngineer", required=false) Integer cuAssignmentID3,
								@RequestParam(value="empPrjMgr", required=false) Integer cuAssignmentID4,
								@RequestParam(value="empTakeOff", required=false) Integer cuAssignmentID5,
								@RequestParam(value="empQuoteBy", required=false) Integer cuAssignmentID6,
								@RequestParam(value="teamArchitect", required=false) Integer rxCategory1,
								@RequestParam(value="teamEngineer", required=false) Integer rxCategory2,
								@RequestParam(value="teamGC", required=false) Integer rxCategory3,
								@RequestParam(value="coDivision", required=false) Integer coDivision,
								@RequestParam(value="taxPersent", required=false) Integer taxTerritory,
								@RequestParam(value="jobCustomer", required=false) String theJobCustomer,
								@RequestParam(value="salesRep", required=false) String theSalesRep,
								@RequestParam(value="csrList", required=false) String theCSRList,
								@RequestParam(value="salesMgrList", required=false) String theSalesMgr,
								@RequestParam(value="enggList", required=false) String theEnggList,
								@RequestParam(value="prjMgerList", required=false) String thePrjMgrList,
								@RequestParam(value="takeoffList", required=false) String theTakeOff,
								@RequestParam(value="quoteByList", required=false) String theQuoteBy,
								@RequestParam(value="architectList", required=false) String theArchitect,
								@RequestParam(value="enggRXList", required=false) String theEnggRXList,
								@RequestParam(value="GcList", required=false) String theGcList,
								@RequestParam(value="taxTerritory", required=false) Integer taxTerritoryID,
								@RequestParam(value="jobHeader_JobCustomer_name", required=false) String thejobCustomerName,
								@RequestParam(value="empCustomerID", required=false) Integer thejobCustomerIDName,
								@RequestParam(value="jobmainPONumber", required=false) String theCustomerPONumber,
								@RequestParam(value="joMasterId", required=false) Integer joMasterId, 
								@RequestParam(value="originalbidDate", required=false) String theOriginalBidDate,
								@RequestParam(value="jobStatusID", required=false) Integer jobStatusID,
								@RequestParam(value="webpageurltoopen", required=false) String webpageurltoopen,
								HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, ParseException, MessagingException {
		
		Jomaster aJoMaster = new Jomaster();
		Jomaster aJomasterValues = new Jomaster();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			String aJobName = theDescription.replaceAll("`", "");
			aJoMaster.setDescription(aJobName);
			if((thejobCustomerName != null && thejobCustomerName != "") && (thejobCustomerIDName != null)){
			aJoMaster.setRxCustomerId(thejobCustomerIDName);
			}else{
			aJoMaster.setRxCustomerId(null);
			}
			aJoMaster.setLocationName(jobcontractorcustomer);
			aJoMaster.setLocationAddress1(theJoblocationAddress1);
			aJoMaster.setLocationAddress2(theJoblocationAddress2);
			aJoMaster.setLocationCity(theJoblocationCity);
			aJoMaster.setLocationState(theJoblocationState);
			aJoMaster.setLocationZip(theJoblocationZip);
			aJoMaster.setChangedById(aUserBean.getUserId());
			aJoMaster.setJoBidStatusId(thejoBidStatusId);
			aJoMaster.setJobStatus(jobStatusID);
			if(theBidDate == null || theBidDate.equals("")) {
				aJoMaster.setBidDate(new Date());
			} else if(theBidDate.length() > 10) {
				aJoMaster.setBidDate(DateUtils.parseDate(theBidDate, new String[]{"MM/dd/yyyy HH:mm"}));
			} else {
				aJoMaster.setBidDate(DateUtils.parseDate(theBidDate, new String[]{"MM/dd/yyyy"}));
			}
			if(theBookedDate != null && theBookedDate != "" && theBookedDate.length() > 10) {
				aJoMaster.setBookedDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy HH:mm"}));
			} else if (theBookedDate != null && theBookedDate != "") {
				aJoMaster.setBookedDate(DateUtils.parseDate(theBookedDate, new String[]{"MM/dd/yyyy"}));
			}
			if (theClosedDate != null && theClosedDate != "" && theClosedDate.length() > 10) {
				aJoMaster.setClosedDate(DateUtils.parseDate(theClosedDate, new String[]{"MM/dd/yyyy HH:mm"}));
			} else if (theClosedDate != null && theClosedDate != "") {
				aJoMaster.setClosedDate(DateUtils.parseDate(theClosedDate, new String[]{"MM/dd/yyyy"}));
			}
			if(theSalesRep != null && theSalesRep != ""){
				aJoMaster.setCuAssignmentId0(cuAssignmentID0);
			} else {
				cuAssignmentID0 = null;
				aJoMaster.setCuAssignmentId0(cuAssignmentID0);
			}
			if(theCSRList != null && theCSRList != ""){
				aJoMaster.setCuAssignmentId1(cuAssignmentID1);
			} else {
				cuAssignmentID1 = null;
				aJoMaster.setCuAssignmentId1(cuAssignmentID1);
			}
			if(theSalesMgr != null && theSalesMgr != ""){
				aJoMaster.setCuAssignmentId2(cuAssignmentID2);
			} else {
				cuAssignmentID2 = null;
				aJoMaster.setCuAssignmentId2(cuAssignmentID2);
			}
			if(theEnggList != null && theEnggList != ""){
				aJoMaster.setCuAssignmentId3(cuAssignmentID3);
			} else {
				cuAssignmentID3 = null;
				aJoMaster.setCuAssignmentId3(cuAssignmentID3);
			}
			if(thePrjMgrList != null && thePrjMgrList != ""){
				aJoMaster.setCuAssignmentId4(cuAssignmentID4);
			} else {
				cuAssignmentID4 = null;
				aJoMaster.setCuAssignmentId4(cuAssignmentID4);
			}
			if(theTakeOff != null && theTakeOff != ""){
				aJoMaster.setCuAssignmentId5(cuAssignmentID5);
			} else {
				cuAssignmentID5 = null;
				aJoMaster.setCuAssignmentId5(cuAssignmentID5);
			}
			if(theQuoteBy != null && theQuoteBy != ""){
				aJoMaster.setCuAssignmentId6(cuAssignmentID6);
			} else {
				cuAssignmentID6 = null;
				aJoMaster.setCuAssignmentId6(cuAssignmentID6);
			}
			if(theArchitect != null && theArchitect != ""){
				aJoMaster.setRxCategory1(rxCategory1);
			} else {
				rxCategory1 = null;
				aJoMaster.setRxCategory1(rxCategory1);
			}
			if(theEnggRXList != null && theEnggRXList != ""){
				aJoMaster.setRxCategory2(rxCategory2);
			} else {
				rxCategory2 =null;
				aJoMaster.setRxCategory2(rxCategory2);
			}
			if(theGcList != null && theGcList != ""){
				aJoMaster.setRxCategory3(rxCategory3);
			} else {
				rxCategory3 = null;
				aJoMaster.setRxCategory3(rxCategory3);
			}
			aJoMaster.setCoDivisionId(coDivision);
			logger.info("CoTaxTerritoryID:"+taxTerritory);
			if(taxTerritory != null && taxTerritory > 0){
				aJoMaster.setCoTaxTerritoryId(taxTerritory);
			} else {
				if(taxTerritoryID!=null && taxTerritoryID>0){
					aJoMaster.setCoTaxTerritoryId(taxTerritoryID);
				}else{
					aJoMaster.setCoTaxTerritoryId(0);
				}
			}
			if(theOriginalBidDate != null && theOriginalBidDate != "" && theOriginalBidDate.length() > 10) {
				aJoMaster.setOriginalBidDate(DateUtils.parseDate(theOriginalBidDate, new String[]{"MM/dd/yyyy HH:mm"}));
			} else if (theOriginalBidDate != null && theOriginalBidDate != "") {
				aJoMaster.setOriginalBidDate(DateUtils.parseDate(theOriginalBidDate, new String[]{"MM/dd/yyyy"}));
			}
			aJoMaster.setCustomerPonumber(theCustomerPONumber);
			aJoMaster.setJoMasterId(joMasterId);
			aJoMaster.setRxCustomerId(theRolodexCustomer);
			logger.info("CoTaxTerritoryID:"+aJoMaster.getCoTaxTerritoryId());
			aJomasterValues = jobService.edit(aJoMaster);
			if(webpageurltoopen!=null && webpageurltoopen!=""){
				JoWizardAppletData ajowizAppletData=new JoWizardAppletData();
				ajowizAppletData.setAppletLocalUrl(webpageurltoopen);
				ajowizAppletData.setJobNumber(aJomasterValues.getJobNumber());
				ajowizAppletData.setUserLoginId(aUserBean.getUserId());
				ajowizAppletData.setJoMasterID(joMasterId);
				jobService.updatejowizardAppletData(ajowizAppletData);
			}
			
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>jobCusromerId,joMasterId:</b>"+theRolodexCustomer+","+joMasterId,"JOB",e,session,therequest);
		}
		logger.info("========================== Updated Job ===========================");
		return aJomasterValues;
	}
	
	@RequestMapping(value="/updatejobList",method = RequestMethod.GET)
	public @ResponseBody Jomaster updatejobList (@RequestParam(value="jobNumber", required=false) String thejobNumber,
								@RequestParam(value="changedID", required=false) Integer theChangedBy,
								@RequestParam(value="jomasterID", required=false) Integer theJoMasterId,
								@RequestParam(value="bidDateCustomer", required=false) String theBidDate,
								@RequestParam(value="rxMaster", required=false) Integer theRxMasterID,
								 HttpSession session, ModelMap theModel, HttpServletResponse theResponse
								 ,HttpServletRequest therequest) throws IOException, MessagingException {
		Jomaster aJoMaster = new Jomaster();
		Rxmaster aRxmaster = new Rxmaster();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if(thejobNumber != null && thejobNumber != ""){
				aJoMaster.setJobNumber(thejobNumber);
			}else{
				thejobNumber = null;
				aJoMaster.setJobNumber(thejobNumber);
			}
			Jomaster aJomaster2 = jobService.getSingleJobDetails(thejobNumber,theJoMasterId);
			Date aBidDate = aJomaster2.getBidDate();
			if(aBidDate == null || aBidDate.equals("")) {
				aJoMaster.setBidDate(new Date());
			} else {
				aJoMaster.setBidDate(aBidDate);
			}
			if(theChangedBy == null){
				aJoMaster.setCuAssignmentId0(aUserBean.getUserId());
				aRxmaster.setChangedById(aUserBean.getUserId());
				aRxmaster.setRxMasterId(theRxMasterID);
				jobService.updateCustomerRxMaster(aRxmaster);
			}else{
				aJoMaster.setCuAssignmentId0(theChangedBy);			
			}
			aJoMaster.setJoMasterId(theJoMasterId);
			aJoMaster.setJobStatus(0);
			jobService.updateCustomerBidList(aJoMaster);
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theJoMasterId,theRxMasterID:</b>"+theJoMasterId+","+theRxMasterID,"JOB",e,session,therequest);
		}
		return aJoMaster;
	}
	
	@RequestMapping(value="/cityAndState", method = RequestMethod.GET)
	public @ResponseBody List<?> getCityAndState(@RequestParam("term") String theCityAndState ,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		ArrayList<AutoCompleteBean> aCityAndState = null;
		try{
			aCityAndState = (ArrayList<AutoCompleteBean>) jobService.getCityAndState(theCityAndState);
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theCityAndState:</b>"+theCityAndState,"JOB",e,session,therequest);
		}
		return aCityAndState;
	}
	
	// Retrieve Next and Previous Job
	@RequestMapping(value="/jobPreviousandNext", method = RequestMethod.GET)
	public @ResponseBody  List<?> getjobPreviousandNext(@RequestParam("jobNumber") String thejobNumber,
			@RequestParam("term") String theterm,HttpSession session, ModelMap model, 
			HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException{
		List<?> jobSearch=null;
		try {
			int JomasterId = jobService.getJoMasterId(thejobNumber);
			int thejomaster = 0;
			if(theterm.equals("previous")){
				thejomaster = JomasterId-1;
			}else if(theterm.equals("next")){
				thejomaster = JomasterId+1;
			}
			jobSearch = jobService.getJobpage(thejomaster,theterm);
			while(jobSearch.isEmpty()){
				if(theterm.equals("previous")){
					thejomaster = thejomaster-1;
					jobSearch = jobService.getJobpage(thejomaster,theterm);
				}else if(theterm.equals("next")){
					thejomaster = thejomaster+1;
					jobSearch = jobService.getJobpage(thejomaster,theterm);
				}
			}
			}catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>thejobNumber,theterm:</b>"+thejobNumber+","+theterm,"JOB",e,session,therequest);
		}
		return jobSearch;
	}
	
	@RequestMapping(value="/jobMasterId", method = RequestMethod.GET)
	public @ResponseBody  Integer getjobMaster(@RequestParam("term") String theterm,HttpSession session, ModelMap model,
			HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException{
		Integer thejomaster = 0;
		try {
			thejomaster = jobService.getFirstLastJomastID(theterm);
			}catch (JobException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theterm:</b>"+theterm+","+theterm,"JOB",e,session,therequest);
		}
		return thejomaster;
	}
	
	@RequestMapping(value="/deletejob",method = RequestMethod.POST)
	public @ResponseBody Jomaster deletejob( @RequestParam(value="joMasterID", required=false) Integer theJomaterID,
		@RequestParam(value="jobNumber", required=false) String theJobNumber,HttpServletRequest therequest,HttpSession session, HttpServletResponse theResponse) throws IOException, MessagingException{
		Jomaster aJomaster = new Jomaster();
		try{
			aJomaster.setJoMasterId(theJomaterID);
			aJomaster.setJobNumber(theJobNumber);
			jobService.deleteMainJob(aJomaster);
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theJomaterID:</b>"+theJomaterID,"JOB",e,session,therequest);
		}
		return aJomaster;
	}
	
	@RequestMapping(value="/ORI_QuoteNumber", method = RequestMethod.POST)
	public @ResponseBody Jomaster updateORIJobNumber(@RequestParam(value="jobNumber",required=false) String theORI_JobNumber, 
																							@RequestParam(value="joMasterID",required=false) Integer theJoMasterID,
																							@RequestParam(value="aQuoteNumber",required=false) String aNewJobNumber,
																							HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		Jomaster aJomaster = new Jomaster();
		Jomaster aJomaster2 = null;
		try{
			aJomaster.setJoMasterId(theJoMasterID);
		    aNewJobNumber = aNewJobNumber+"-";
		    String aNewJobString = jobService.getNewJobNumber(aNewJobNumber);
			aJomaster.setJobNumber(aNewJobString);
			aJomaster.setQuoteNumber(theORI_JobNumber);
			aJomaster2 = jobService.updateORIJobNumber(aJomaster);
			Jomaster aJomaster3 = new Jomaster();
			aJomaster3.setJobNumber(theORI_JobNumber);
			jobService.deleteRecentlyOpendJob(aJomaster3);
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>theORI_JobNumber,theJoMasterID:</b>"+theORI_JobNumber+","+theJoMasterID,"JOB",e,session,therequest);
		}
		return aJomaster2;
	}
	
	@RequestMapping(value="/previewreport",method = RequestMethod.GET)
	public @ResponseBody Boolean previewBookedReport (@RequestParam(value = "salesRepID", required = false) Integer cuAssignmentID,
														@RequestParam(value = "employeeID", required = false) Integer employeeID,
														@RequestParam(value = "customerID", required = false) Integer customerID,
														@RequestParam(value = "reportName", required = false) String reportName,
														HttpSession session, HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, SQLException, MessagingException{
		String query ="";
		Boolean ReportRowsExists = false;
		Connection connection =null;
		TsUserSetting	objtsusersettings=null;
		InputStream imageStream =null;
		ConnectionProvider con =null;
		try {
			con=itspdfService.connectionForJasper();
			connection = con.getConnection();
		String path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Booked.jrxml");
		String fileName="Booked";
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(reportName.equalsIgnoreCase("Booked")){
			fileName="Booked";
		if(cuAssignmentID == null){
			path_JRXML =  theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Booked.jrxml");
		}
		else{
			params.put("salesRepID", cuAssignmentID);
			path_JRXML =  theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/BookedOne.jrxml");
		}
		
		ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,fileName,connection);
		}
		if(reportName.equalsIgnoreCase("openJobs")){
			objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
			Blob blob =  objtsusersettings.getCompanyLogo();
			imageStream =blob.getBinaryStream();
			params.put("HeaderImage", imageStream);
			params.put("HeaderText",((objtsusersettings.getHeaderText().replaceAll("`and`nbsp;", " ")).replaceAll("`", "")).replaceAll("amp;"," "));
			
			/*query ="SELECT J.EstimatedProfit,"+
					" J.ActualProfitToDate,"+
					" J.Description as Project, "+
					" J.JobNumber, "+
					" J.ContractAmount,"+
					" R.Released,"+
					" R.CommissionSales,"+
					" C.Changes,"+
					" I.Invoiced,"+
					" J.ChangedOn,"+
					" U.FullName AS Salesrep,"+
					" U.Initials AS RepInitials,"+
					" U.UserLoginID,"+
					" X.Name AS Customer,"+
					" X.rxMasterID,"+
					" J.coDivisionID as Division,"+
					" D.Description AS DivisionName "+
					" FROM rxMaster AS X"+
					" RIGHT JOIN ((((joMaster AS J"+
					" LEFT JOIN (SELECT joChange.joMasterID, Sum(joChange.ChangeAmount) AS Changes FROM joMaster"+
					" RIGHT JOIN joChange ON joMaster.joMasterID = joChange.joMasterID WHERE (joMaster.JobStatus=3)"+
					" GROUP BY joChange.joMasterID) AS C ON J.joMasterID = C.joMasterID) LEFT JOIN (SELECT joMaster.joMasterID, Sum(Subtotal) AS Invoiced"+
					" FROM joMaster RIGHT JOIN (joReleaseDetail"+
					" RIGHT JOIN cuInvoice ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) ON joMaster.joMasterID = joReleaseDetail.joMasterID"+
					" WHERE (joMaster.JobStatus=3) AND (cuInvoice.TransactionStatus>0) GROUP BY joMaster.joMasterID) AS I ON J.joMasterID = I.joMasterID)"+
					" LEFT JOIN (SELECT joRelease.joMasterID, Sum(joRelease.EstimatedBilling) AS Released,"+
					" Sum( (CASE WHEN CommissionReceived=1 THEN joRelease.EstimatedBilling ELSE 0 END) ) As CommissionSales"+
					" FROM joMaster RIGHT JOIN joRelease ON joMaster.joMasterID = joRelease.joMasterID WHERE (joMaster.JobStatus=3)"+
					" GROUP BY joRelease.joMasterID) AS R ON J.joMasterID = R.joMasterID) LEFT JOIN tsUserLogin AS U ON J.cuAssignmentID0 = U.UserLoginID) ON X.rxMasterID = J.rxCustomerID"+
					" LEFT JOIN coDivision"+
					" AS D ON J.coDivisionID = D.coDivisionID "+
					" WHERE (J.JobStatus=3)";*/
			 /*query="SELECT IF((IFNULL(J.EstimatedProfit,0.0000)-IFNULL(J.ActualProfitToDate,0.0000))<0.0000,0.0000,(IFNULL(J.EstimatedProfit,0.0000)-IFNULL(J.ActualProfitToDate,0.0000))) AS EstimatedProfit ,J.ActualProfitToDate,J.Description AS Project,J.JobNumber,J.ContractAmount,R.Released,R.CommissionSales, "
					 +"C.Changes,I.Invoiced,J.ChangedOn,U.FullName AS Salesrep,U.Initials AS RepInitials,X.Name AS Customer,J.coDivisionID AS Division, "
					 +"D.Description AS DivisionName FROM joMaster AS J LEFT JOIN (SELECT joChange.joMasterID, SUM(joChange.ChangeAmount) AS Changes FROM joMaster "
					 +"RIGHT JOIN joChange ON joMaster.joMasterID = joChange.joMasterID WHERE (joMaster.JobStatus=3) "
					 +"GROUP BY joChange.joMasterID) AS C ON J.joMasterID = C.joMasterID LEFT JOIN (SELECT joMaster.joMasterID, SUM(cuInvoice.InvoiceAmount) AS Invoiced "
					 +"FROM joMaster RIGHT JOIN (joReleaseDetail "
					 +"RIGHT JOIN cuInvoice ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) ON joMaster.joMasterID = joReleaseDetail.joMasterID "
					 +"WHERE (joMaster.JobStatus=3) AND (cuInvoice.TransactionStatus>0) GROUP BY joMaster.joMasterID) AS I ON J.joMasterID = I.joMasterID " 
					 +"LEFT JOIN (SELECT joRelease.joMasterID, SUM(joRelease.EstimatedBilling) AS Released, "
					 +"SUM( (CASE WHEN CommissionReceived=1 THEN joRelease.EstimatedBilling ELSE 0 END) ) AS CommissionSales "
					 +"FROM joMaster RIGHT JOIN joRelease ON joMaster.joMasterID = joRelease.joMasterID WHERE (joMaster.JobStatus=3) "
					 +"GROUP BY joRelease.joMasterID) AS R ON J.joMasterID = R.joMasterID  "
					 +"LEFT JOIN UserLoginClone AS U ON J.cuAssignmentID0 = U.UserLoginID  "
					 +"LEFT JOIN coDivision AS D ON J.coDivisionID = D.coDivisionID  "
					 +"LEFT JOIN rxMaster AS X ON X.rxMasterID = J.rxCustomerID WHERE (J.JobStatus=3) ";*/
			query="SELECT RXM.Name AS Customer,JOM.Description AS Project,JOM.JobNumber,TSU.Initials AS RepInitials,"
					+ "(IFNULL(JOM.ContractAmount,0.0000)+IFNULL((SELECT SUM(IFNULL(ChangeAmount,0.0000)) FROM joChange WHERE joMasterID = JOM.joMasterID),0.0000)) AS Released, "
					+ "IFNULL(I.SumSubTotal,0.0000) AS Invoiced,(IFNULL((IFNULL(JOM.ContractAmount,0.0000)+(IFNULL((SELECT SUM(IFNULL(ChangeAmount,0.0000)) FROM joChange WHERE joMasterID = JOM.joMasterID),0.0000)) - "
					+ "IFNULL((I.SumSubTotal),0.0000)),0.0000) - IFNULL(((IFNULL(JOM.ContractAmount,0.0000)+IFNULL((SELECT SUM(IFNULL(ChangeAmount,0.0000)) FROM joChange WHERE joMasterID = JOM.joMasterID),0.0000)) - "
					+ "IFNULL(R.SumREstimatedBilling,0.0000)),0.0000)) AS  UnInvoiced, IFNULL(((IFNULL(JOM.ContractAmount,0.0000)+IFNULL((SELECT SUM(IFNULL(ChangeAmount,0.0000)) "
					+ "FROM joChange WHERE joMasterID = JOM.joMasterID),0.0000)) - IFNULL(R.SumREstimatedBilling,0.0000)),0.0000) AS CommissionSales, "
					+ "IFNULL(((IFNULL(I.SumInvoiceAmount,0.0000)-IFNULL(I.SumTaxAmount,0.0000))-(IFNULL(VB.SumVBBillAmount,0.0000)-"
					+ "IFNULL(VB.SumVBTaxamount,0.0000))-(IFNULL(P.SumCusoCostTotal,0.0000))),0.0000) AS ActualProfitToDate, "
					+ "IFNULL(IF((IFNULL(JOM.EstimatedProfit,0.0000)-IFNULL(JOM.ActualProfitToDate,0.0000))<0.0000,0.0000,(IFNULL(JOM.EstimatedProfit,0.0000)-"
					+ "IFNULL(JOM.ActualProfitToDate,0.0000))),0.0000) AS EstimatedProfit, JOM.ChangedOn AS ChangedOn,"
					+ "IFNULL((SELECT SUM(IFNULL(ChangeAmount,0.0000)) FROM joChange WHERE joMasterID = JOM.joMasterID),0.0000) AS Changes, "
					+ "IFNULL(JOM.ContractAmount,0.0000) AS ContractAmount FROM joMaster AS JOM "
					+ "LEFT JOIN tsUserLogin TSU ON TSU.UserLoginID=JOM.cuAssignmentID0 "
					+ "LEFT JOIN (SELECT joMaster.joMasterID,SUM(IFNULL(joRelease.EstimatedBilling,0.0000)) AS SumREstimatedBilling FROM joMaster "
					+ "RIGHT JOIN joRelease ON joMaster.joMasterID = joRelease.joMasterID  WHERE (joMaster.JobStatus=3)  GROUP BY joMaster.joMasterID) AS R ON JOM.joMasterID = R.joMasterID "
					+ "LEFT JOIN (SELECT joMaster.joMasterID, "
					+ "SUM(IFNULL(cuInvoice.InvoiceAmount,0.0000)) AS SumInvoiceAmount, SUM(IFNULL(cuInvoice.Subtotal,0.0000)) AS SumSubTotal, "
					+ "SUM(IFNULL(cuInvoice.Freight,0.0000)) AS SumFreight, SUM(IFNULL(cuInvoice.TaxAmount,0.0000)) AS SumTaxAmount, "
					+ "SUM(IFNULL(cuInvoice.AppliedAmount,0.0000)) AS SumAppliedAmount, SUM(IFNULL(cuInvoice.DiscountAmt,0.0000)) AS SumDiscountAmt "
					+ "FROM joMaster RIGHT JOIN (joReleaseDetail RIGHT JOIN cuInvoice ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID) ON joMaster.joMasterID = joReleaseDetail.joMasterID WHERE (joMaster.JobStatus=3  AND cuInvoice.TransactionStatus>0) GROUP BY joMaster.joMasterID) AS I ON JOM.joMasterID = I.joMasterID "
					+ "LEFT JOIN (SELECT joMaster.joMasterID, SUM(IFNULL(veBill.BillAmount,0.0000)) AS SumVBBillAmount,SUM(IFNULL(veBill.TaxAmount,0.0000)) AS SumVBTaxamount, "
					+ "SUM(IFNULL(veBill.AdditionalFreight,0.0000)) AS SumVBAdditional,IFNULL(veBill.TransactionStatus,0) AS vBTransactionStatus FROM joMaster "
					+ "RIGHT JOIN (joReleaseDetail RIGHT JOIN veBill ON joReleaseDetail.joReleaseDetailID = veBill.joReleaseDetailID) "
					+ "ON joMaster.joMasterID = joReleaseDetail.joMasterID WHERE (joMaster.JobStatus=3 AND veBill.TransactionStatus>0) "
					+ "GROUP BY joMaster.joMasterID) AS VB ON JOM.joMasterID = VB.joMasterID LEFT JOIN (SELECT joMaster.joMasterID, SUM(IFNULL(vePO.Subtotal,0.0000)) AS SumVepoSubtotal, SUM(IFNULL(vePO.TaxTotal,0.0000)) AS SumVepoTaxTotal, "
					+ "SUM(IFNULL(vePO.Freight,0.0000)) AS SumVepoFreight FROM joMaster RIGHT JOIN (joRelease RIGHT JOIN vePO ON joRelease.joReleaseID = vePO.joReleaseID) ON joMaster.joMasterID = joRelease.joMasterID WHERE (joMaster.JobStatus=3)GROUP BY joMaster.joMasterID) AS VP ON "
					+ "JOM.joMasterID = VP.joMasterID LEFT JOIN (SELECT joMaster.joMasterID, SUM(IFNULL(cuSO.CostTotal,0.0000)) AS SumCusoCostTotal FROM joMaster RIGHT JOIN (joReleaseDetail RIGHT JOIN cuSO ON joReleaseDetail.joReleaseID = cuSO.joReleaseID) ON joMaster.joMasterID = joReleaseDetail.joMasterID WHERE (joMaster.JobStatus=3) "
					+ "GROUP BY joMaster.joMasterID) AS P ON JOM.joMasterID = P.joMasterID LEFT JOIN rxMaster RXM ON JOM.rxCustomerID = RXM.rxMasterID WHERE JOM.JobStatus=3 ";
			
			if(employeeID==null && customerID==null){
				query= query+" GROUP BY JOM.joMasterID ORDER BY RXM.Name, JOM.JobNumber;" ;
			}
			else if(employeeID == null && customerID !=null){
				query= query+" AND JOM.rxCustomerID="+customerID;
				query= query+" GROUP BY JOM.joMasterID ORDER BY RXM.Name, JOM.JobNumber;" ;
				
			}else if(customerID==null && employeeID!=null){
				query+=" AND TSU.UserLoginID ="+employeeID;
				query+=" GROUP BY JOM.joMasterID ORDER BY RXM.Name, JOM.JobNumber;" ;
			}
			else{
				query+=" AND JOM.rxCustomerID="+customerID;
				query+=" AND TSU.UserLoginID ="+employeeID;
				query+=" GROUP BY JOM.joMasterID ORDER BY RXM.Name, JOM.JobNumber;" ;
			}
			System.out.println("OpenJobs Query::#"+query);
			path_JRXML =  theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/OpenJobs.jrxml");
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,"OPen",connection);
		}
		if(reportName.equalsIgnoreCase("openpo")){
			path_JRXML =  theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/OpenPO_new.jrxml");
			
			ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,"OpenPO",connection); 
		}
		
		
		} catch (JRException e) {
			e.printStackTrace();
			sendTransactionException("<b>cuAssignmentID,employeeID,customerID:</b>"+cuAssignmentID+","+employeeID+","+customerID,"JOB",e,session,theRequest);
		}
		finally
		{
			if(imageStream!=null){
				imageStream.close();
			}
			if(con!=null){
			con.closeConnection(connection);
			con=null;
			}
		}
		return ReportRowsExists;
		
	}
	@RequestMapping(value = "/getBidStatusList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getCustomerTypes(HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse response = new CustomResponse();

		try {
			List<?> itsBidStatus = jobService.getBidStatusList();
			response.setRows(itsBidStatus);
		} catch (Exception e) {
			sendTransactionException("<b>MethodName:</b>getBidStatusList","JOB",e,session,therequest);
		}
		return response;
	}
	@RequestMapping(value = "/updateJoBidStatus", method = RequestMethod.POST)
	public @ResponseBody
	Boolean updateJoBidStatus(
			@RequestParam(value = "joBidStatus", required = false) Integer theFreightId,
			@RequestParam(value = "description", required = false) String theDescription,
			@RequestParam(value = "code", required = false) String theCode,
			@RequestParam(value = "inActive", required = false) Boolean theInactive,
			@RequestParam(value = "operation", required = false) Boolean isAdd,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		try{
		Jobidstatus aJoBid = new Jobidstatus();
		if (!isAdd) {
			aJoBid.setJoBidStatusId(theFreightId);
		}
		System.out.println(theFreightId + " :: " + theDescription + " :: " + theInactive + " :: " + isAdd);
		aJoBid.setCode(theCode); 
		aJoBid.setInActive(theInactive);
		aJoBid.setDescription(theDescription);
		jobService.updateJoBidStatusService(aJoBid, isAdd);
		}catch(Exception e){
			sendTransactionException("<b>MethodName:</b>updateJoBidStatus","JOB",e,session,therequest);
		}
		return true;
	}
	@RequestMapping(value = "/deletejoBidStatus", method = RequestMethod.POST)
	public @ResponseBody
	Boolean deletejoBidStatus(
			@RequestParam(value = "jobidstatusid", required = false) Integer theJoBidstatusId,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		try{
				jobService.deleteJoBidStatusService(theJoBidstatusId);
		}catch(Exception e){
			sendTransactionException("<b>MethodName:</b>deletejoBidStatus","JOB",e,session,therequest);
		}
		return true;
	}
	
	
	
	
	@RequestMapping(value = "/updateEmployeeCommissionsplitype", method = RequestMethod.POST)
	public @ResponseBody Boolean updateEmployeeCommissionsplitype(
			@RequestParam(value = "ecSplitCodeId", required = false) Integer ecSplitCodeID,
			@RequestParam(value = "description", required = false) String theDescription,
			@RequestParam(value= "percentage",required=false)BigDecimal Percentage,
			@RequestParam(value = "operation", required = false) Boolean isAdd,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		try{
		Ecsplitcode ecsplitcode = new Ecsplitcode();
		if (!isAdd) {
			ecsplitcode.setEcSplitCodeID(ecSplitCodeID);
		}
		ecsplitcode.setCodeName(theDescription);
		ecsplitcode.setDefaultPct(Percentage);
		jobService.updateEmpCommissionsplitypeService(ecsplitcode, isAdd);
		}catch(Exception e){
			sendTransactionException("<b>ecSplitCodeID:</b>"+ecSplitCodeID,"JOB",e,session,therequest);
		}
		return true;
	}
	@RequestMapping(value = "/deleteEmployeeCommissionsplitype", method = RequestMethod.POST)
	public @ResponseBody
	Boolean deleteEmployeeCommissionsplitype(
			@RequestParam(value = "ecSplitCodeId", required = false) Integer ecSplitCodeID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		try{
		jobService.deleteEmpCommissionsplitypeService(ecSplitCodeID);
		}catch(Exception e){
			sendTransactionException("<b>ecSplitCodeID:</b>"+ecSplitCodeID,"JOB",e,session,therequest);
		}
		return true;
	}
	@RequestMapping(value = "/getJobBidListByCustomer",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getJobBidListByCustomer(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord,
			@RequestParam(value="rxMasterID", required=false) Integer rxMasterId,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		System.out.println("sort by option"+sord+" sidx::"+sidx);
		int from, to;
		CustomResponse response = null;
		response = new CustomResponse();
		List<?> jobs = null;
		try{
			
			BigInteger aTotalCount = jobService.getJobsCount();
			to = (rows * page);
			from = to - rows;
			if (to > aTotalCount.intValue()) to = aTotalCount.intValue();
			jobs = jobService.getJobBidList(from, rows,rxMasterId,sord,sidx);
			response.setRows(jobs);
			response.setRecords( String.valueOf(jobs.size()) );
			response.setPage( page );
			response.setTotal((int) Math.ceil((double)aTotalCount.intValue() / (double) rows));
			
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>rxMasterId:</b>"+rxMasterId,"JOB",e,session,therequest);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/saveJobBidListByCustomer",method = RequestMethod.POST)
	public @ResponseBody void saveJobBidListByCustomer(
			@RequestParam(value="rxMasterID", required=false) Integer rxMasterId,
			@RequestParam(value="rxContactId", required=false) Integer rxContactId,
			@RequestParam(value="joMasterId", required=false) Integer joMasterId,
			
			@RequestParam(value="quoteTypeId", required=false) Integer quoteTypeId,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
			logger.info("Insert Called");
			logger.info("rxMasterId "+rxMasterId);
			logger.info("rxContactId "+rxContactId);
			logger.info("joMasterId "+joMasterId);
			
		Jobidder aJobidder = new Jobidder();
		try{
			aJobidder.setJoMasterId(joMasterId);
			aJobidder.setRxContactId(rxContactId);
			aJobidder.setRxMasterId(rxMasterId);
			if(quoteTypeId!=null){
				aJobidder.setCuMasterTypeId(quoteTypeId);	
			}
			
			boolean isNew = false;
			aJobidder = jobService.savBidData(aJobidder,isNew);
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException("<b>rxMasterId, rxContactId ,joMasterId:</b>"+rxMasterId+","+rxContactId+","+joMasterId,"JOB",e,session,therequest);
		}
	}
	@RequestMapping(value="/Customerbaseddropdowns", method = RequestMethod.POST)
	public @ResponseBody Cumaster Customerbaseddropdowns(@RequestParam(value="id",required=false) int id, 
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		Cumaster cumaster = null;
		Sysvariable aSysvariable = null;
		 List<AutoCompleteBean> aTaxList = null;
		try{
			 cumaster=itsCuMasterService.getUserbasedonCustomer(id);
			 aSysvariable = itsEmployeeService.getSysVariableDetails("DefaultJobTaxTerritorytoCustomerTax");
			 cumaster.setDefaultTaxTerritory(aSysvariable.getValueLong());
			 aTaxList = (List<AutoCompleteBean>) itsCompanyService.getcuDefaultTerritoryList(id);
			 
			 if(aSysvariable.getValueLong() == 1)
			 {
				 if(aTaxList!=null)
				 {
				 for(AutoCompleteBean ataxObj : aTaxList)
				 {
					 System.out.println("TaxID |>>"+ataxObj.getId()+":: TaxRate |>>"+ataxObj.getValue()+":: TaxCity |>>"+ataxObj.getEntity());
					 
					 cumaster.setCoTaxID(ataxObj.getId());
					 cumaster.setTaxRate(ataxObj.getValue());
					 cumaster.setTaxName(ataxObj.getEntity());
				 }
				 }
			 }
			 
			//jobService.deleteRecentlyOpendJob(aJomaster3);
		}catch (/*Job*/Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b>id:</b>"+id,"JOB",e,session,therequest);
			//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return cumaster;
	}
	
	
	//JoCategory Annamalai Code
	@RequestMapping(value = "/getjoQuotesCategoryList", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getjoQuotesCategoryList(HttpServletResponse theResponse) {
		CustomResponse response = new CustomResponse();

		try {
			List<?> itsBidStatus = jobService.getjoQuotesCategoryList();
			response.setRows(itsBidStatus);
		} catch (Exception e) {

		}
		return response;
	}
	
	@RequestMapping(value = "/updateQuotesCategory", method = RequestMethod.POST)
	public @ResponseBody Boolean updateQuotesCategory(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "description", required = false) String theDescription,
			@RequestParam(value = "operation", required = false) Boolean isAdd,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		try{
		JoQuoteCategory aJoQuoteCategory = new JoQuoteCategory();
		if (!isAdd) {
			aJoQuoteCategory.setId(id);
		}
		System.out.println(id + " :: " + theDescription+ " :: " + isAdd+" || aJoBid.getId() = "+aJoQuoteCategory.getId());
		aJoQuoteCategory.setDescription(theDescription);
		jobService.updateQuotesCategory(aJoQuoteCategory, isAdd);
		}catch(Exception e){
			sendTransactionException("<b>id:</b>"+id,"JOB",e,session,therequest);
		}
		return true;
	}
	
	@RequestMapping(value = "/deleteQuotesCategory", method = RequestMethod.POST)
	public @ResponseBody Boolean deleteQuotesCategory (	@RequestParam(value = "id", required = false) Integer id,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws JobException, IOException, MessagingException {
		try{
		jobService.deleteQuotesCategory(id);
		}catch(Exception e){
			sendTransactionException("<b>id:</b>"+id,"JOB",e,session,therequest);
		}
		return true;
	}
	
	//Annamalai code
	@RequestMapping(value = "/getjoQuotesCategoryData", method = RequestMethod.GET)
	public @ResponseBody ArrayList<JoQuoteCategory> getNewQuoteTemplateHeader(HttpSession session, 
			HttpServletResponse theResponse,HttpServletRequest therequest)
			throws ParseException, IOException, QuoteTemplateException, MessagingException {
		ArrayList<JoQuoteCategory> aJoQuoteCategory = new ArrayList<JoQuoteCategory>();
		try {
			aJoQuoteCategory=(ArrayList<JoQuoteCategory>) jobService.getjoQuotesCategoryList();
			logger.info(" aJoQuoteCategory.size()  :: "+aJoQuoteCategory.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>MethodName:</b>getjoQuotesCategoryData","JOB",e,session,therequest);
		}
		return aJoQuoteCategory;
	}
	
	@RequestMapping(value = "/getJobCreditStatus", method = RequestMethod.GET)
	public @ResponseBody ArrayList<Sysvariable> getJobCreditStatus(
			@RequestParam(value = "jomasterID", required = false) Integer jomasterID
			,HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, MessagingException
			 {
		ArrayList<Sysvariable> aSysvariablelst = new ArrayList<Sysvariable>();
		Sysvariable aSysvariable=new Sysvariable();
		UserBean aUserBean;
		
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			aSysvariable=itsEmployeeService.getSysVariableDetails("DefaulttoApprovedcreditforanewJob");
			if(aSysvariable.getValueLong()==1){
				if(jomasterID != null){
				Jomaster ajoMaster=new  Jomaster();
				ajoMaster.setJoMasterId(jomasterID);
				ajoMaster.setCreditStatus((byte) 1);
				Date today =new Date();
			    ajoMaster.setCreditStatusDate(today);
			    ajoMaster.setWho0(aUserBean.getUserId());
				jobService.UpDateCreditInfoDetails(ajoMaster);
				}
			}
			aSysvariablelst.add(aSysvariable);
			
			//For CreditAmountcheckbox for jobsettings
			aSysvariable=itsEmployeeService.getSysVariableDetails("AllowbookingJobswithnoContractAmount");
			aSysvariablelst.add(aSysvariable);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>jomasterID:</b>"+jomasterID,"JOB",e,session,therequest);
		}
		return aSysvariablelst;
	}
	
	@RequestMapping(value = "/getJobSettingStatus", method = RequestMethod.GET)
	public @ResponseBody Sysvariable getJobSettingStatus(
			@RequestParam(value = "Constantvariablename", required = false) String Constantvariablename
			,HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, MessagingException
			 {
		Sysvariable aSysvariable=new Sysvariable();
		try {
			aSysvariable=itsEmployeeService.getSysVariableDetails(Constantvariablename);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>Constantvariablename:</b>"+Constantvariablename,"JOB",e,session,therequest);
		}
		return aSysvariable;
	}
	
	@RequestMapping(value = "/jobDetailsfromReleaseDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> jobDetailsfromReleaseDetail(
			@RequestParam(value = "joreleasedetailid", required = false) Integer joreleasedetailid
			,HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, MessagingException
			 {
		Map<String,String> hmap=new HashMap<String, String>();
		try {
			hmap=jobService.getjoMasterByJoreleaseDetailID(joreleasedetailid);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>jobDetailsfromReleaseDetail:</b>","JOB",e,session,therequest);
		}
		return hmap;
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
	
	@RequestMapping(value = "/getjoQuotesCategoryDatainJson", method = RequestMethod.GET)
	public @ResponseBody String getjoQuotesCategoryDatainJson(HttpSession session, 
			HttpServletResponse theResponse,HttpServletRequest therequest)
			throws ParseException, IOException, QuoteTemplateException, MessagingException {
		String returnvalue = null;
		try {
			ArrayList<JoQuoteCategory> aJoQuoteCategory=(ArrayList<JoQuoteCategory>) jobService.getjoQuotesCategoryList();
			JSONObject jobj=new JSONObject();
			
			jobj.put("0", "");
			for(JoQuoteCategory jqc:aJoQuoteCategory){
				jobj.put(jqc.getId().toString(), jqc.getDescription());
			}
			returnvalue=jobj.toString();
			System.out.println("Json Object Stringify==="+jobj.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>MethodName:</b>getjoQuotesCategoryData","JOB",e,session,therequest);
		}
		return returnvalue;
	}
}
