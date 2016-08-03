package com.turborep.turbotracker.sales.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.DocumentException;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.OperatingSystemInfo;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/salescontroller")
public class Salescontroller {
	
	protected static Logger itsLogger = Logger.getLogger("controller"); 
	
	@Resource(name="salesServices")
	private SalesService itsSalesServices;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ArrayList<UserBean> getAll(){
		itsLogger.debug("Received request to get all user");
		// Retrieve all users from the service
		ArrayList<UserBean> aSales = (ArrayList<UserBean>) itsSalesServices.getSalesRep();
		return aSales; 
	}
	
	@RequestMapping(value="/salesrep", method = RequestMethod.GET)
	public @ResponseBody List<?> getSalesRep(@RequestParam("term") String theSalesRep)
	{
		itsLogger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsSalesServices.getsalesrepList(theSalesRep);
		return aSales;
	}
	
	@RequestMapping(value="/salesupcoming",method = RequestMethod.GET)
	public @ResponseBody CustomResponse upcoming(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sortBy,
			@RequestParam(value="sord", required=false) String sortOrder,
			@RequestParam("salesRepId") int theSalesRepNo,
			@RequestParam("rxCustomerID") int rxCustomerID
			, HttpSession aSession) {
		CustomResponse aResponse = new CustomResponse();
		int aSalesUserId = (Integer)aSession.getAttribute(SessionConstants.SALES_USERID);
		if(aSalesUserId != theSalesRepNo){
			aSession.setAttribute(SessionConstants.SALES_USERID, theSalesRepNo);
		}
		int aSalesRepId = theSalesRepNo;
		itsLogger.debug("Sales rep id: " + aSalesRepId);
		itsLogger.debug("SalesUpcoming Page"+page);
		itsLogger.debug("SalesUpcoming rows"+rows);
		itsLogger.debug("SalesUpcoming sidx"+sortBy);
		itsLogger.debug("SalesUpcoming sord"+sortOrder);
		itsLogger.debug("rxCustomerID====="+rxCustomerID);
		SalesRepBean aSalesRep = new SalesRepBean();
		
		if(sortBy!=null){
			if(sortBy.equals("bidDate")){
				aSalesRep.setSortcolumn("BidDate");
			}else if(sortBy.equals("jobName")){
				aSalesRep.setSortcolumn("Description");
			}else if(sortBy.equals("jobNo")){
				aSalesRep.setSortcolumn("JobNumber");
			}else if(sortBy.equals("assignedSalesman")){
				aSalesRep.setSortcolumn("asignedSales");
			}else if(sortBy.equals("assignedCustomers")){
				aSalesRep.setSortcolumn("asignedCust");
			}else if(sortBy.equals("allCustomer")){
				aSalesRep.setSortcolumn("allCust");
			}else if(sortBy.equals("architect")){
				aSalesRep.setSortcolumn("architect");
			}else if(sortBy.equals("engineer")){
				aSalesRep.setSortcolumn("engineer");
			}else if(sortBy.equals("generalContractor")){
				aSalesRep.setSortcolumn("generalContract");
			}
			
			aSalesRep.setSortby(sortOrder);
		}
		
		aSalesRep.setSalesRepId(theSalesRepNo);
		aSalesRep.setRxMasterID(rxCustomerID);
		ArrayList<JobsBean> aUpcomingJobsList = (ArrayList<JobsBean>) itsSalesServices.getUpcoming(aSalesRep);
		aResponse.setRows(aUpcomingJobsList);
		aResponse.setRecords(String.valueOf(aUpcomingJobsList.size()) );
		aResponse.setPage(1);
		return aResponse;
	}
	
	@RequestMapping(value="/salesPending",method = RequestMethod.GET)
	public @ResponseBody CustomResponse pending(
			@RequestParam("salesRepId")Integer theSalesRepNo, 
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sortBy,
			@RequestParam(value="sord", required=false) String sortOrder,
			@RequestParam(value="rxCustomerID", required=false) Integer rxCustomerID,
			HttpSession theSession){
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.SALES_USERID);
		if(aSalesUserId != theSalesRepNo){
			theSession.setAttribute(SessionConstants.SALES_USERID, theSalesRepNo);
		}
		int aSalesRepId = theSalesRepNo;
		itsLogger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		aSalesRep.setRxMasterID(rxCustomerID);
		ArrayList<JobsBean> aUpcomingJobsList = (ArrayList<JobsBean>) itsSalesServices.getPending(aSalesRep,rxCustomerID);
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aUpcomingJobsList);
		aResponse.setRecords( String.valueOf(aUpcomingJobsList.size()) );
		aResponse.setPage(1);
		return aResponse;
		
	}
	@RequestMapping(value="/salesQuoted",method = RequestMethod.GET)
	public @ResponseBody CustomResponse quotedJobs(
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="rows", required=false) Integer rows,
			@RequestParam(value="sidx", required=false) String sortBy,
			@RequestParam(value="sord", required=false) String sortOrder,
			@RequestParam("salesRepId")Integer theSalesRepNo, 
			@RequestParam("rxCustomerID") int rxCustomerID,
			HttpSession theSession){
			itsLogger.info("Page - rows - sortID - sortOrder : "+page+"-"+rows+"-"+sortBy+"-"+sortOrder);
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.SALES_USERID);
		if(aSalesUserId != theSalesRepNo){
			theSession.setAttribute(SessionConstants.SALES_USERID, theSalesRepNo);
		}
		int aSalesRepId = theSalesRepNo;
		itsLogger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		aSalesRep.setRxMasterID(rxCustomerID);
		ArrayList<JobsBean> aUpcomingJobsList = (ArrayList<JobsBean>) itsSalesServices.getQuotedJobs(aSalesRep,sortBy,sortOrder);
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aUpcomingJobsList);
		aResponse.setRecords( String.valueOf(aUpcomingJobsList.size()) );
		aResponse.setPage(1);
		return aResponse;
		
	}
	
	@RequestMapping(value="/salesawarded",method = RequestMethod.GET)
	public @ResponseBody CustomResponse awarded(
			@RequestParam("salesRepId")Integer theSalesRepNo,
			@RequestParam("rxCustomerID") int rxCustomerID,
			HttpSession theSession){
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.SALES_USERID);
		if(aSalesUserId != theSalesRepNo){
			theSession.setAttribute(SessionConstants.SALES_USERID, theSalesRepNo);
		}
		int aSalesRepId = theSalesRepNo;
		itsLogger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		aSalesRep.setRxMasterID(rxCustomerID);
		ArrayList<JobsBean> aUpcomingJobsList = (ArrayList<JobsBean>) itsSalesServices.getAwarded(aSalesRep);
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aUpcomingJobsList);
		aResponse.setRecords( String.valueOf(aUpcomingJobsList.size()) );
		aResponse.setPage(1);
		return aResponse;
	}
	
	/* Added to get SO Customer Name */
	@RequestMapping(value="/customerName", method = RequestMethod.GET)
	public @ResponseBody List<?> getCustomerName(@RequestParam("term") String theCustomerName)
	{
		itsLogger.debug("Received request to get Customer Lists");
		ArrayList<AutoCompleteBean> aCustomer = (ArrayList<AutoCompleteBean>) itsSalesServices.getCustomerNameList(theCustomerName);
		return aCustomer;
	}
	
	/*
	 * Method for populating the sales order quote dropdown in home jsp
	 * */
	@RequestMapping(value="/salesorderlist", method = RequestMethod.GET)
	public @ResponseBody List<?> getSalesOrderList(@RequestParam("term") String salesOrder)
	{
		itsLogger.debug("Received request to get SalesOrderList");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsSalesServices.getSalesOrderList(salesOrder);
		return aSales;
	}
	/*
	 * Method for populating the Division List dropdown in home jsp
	 * */
	@RequestMapping(value="/divisionlist", method = RequestMethod.GET)
	public @ResponseBody List<?> getDivisionlist(@RequestParam("term") String divisionname)
	{
		itsLogger.debug("Received request to get Divisionlist Lists");
		ArrayList<AutoCompleteBean> aSales = (ArrayList<AutoCompleteBean>) itsSalesServices.getDivisionlist(divisionname);
		return aSales;
	}
	
	
	@RequestMapping(value = "/printQuotedJobs", method = RequestMethod.GET)
	public @ResponseBody
	void printCuInvoiceReport(
			@RequestParam(value = "employName", required = false) String empName,
			@RequestParam(value = "employID", required = false) String empID,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession theSession)
			throws IOException, MessagingException, SQLException {
		UserBean aUserBean= (UserBean) theSession.getAttribute(SessionConstants.USER);
		Connection connection = null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = theRequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/PrintQuotedJobs.jrxml");
			con = itspdfService.connectionForJasper();
			params.put("empName", empName);
			params.put("empID", empID);
			
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
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>empName:</b>"+empName,"Salescontroller",e,theSession,theRequest);
		} catch (JRException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>empName:</b>"+empName,"Salescontroller",e,theSession,theRequest);
		} catch (SQLException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>empName:</b>"+empName,"Salescontroller",e,theSession,theRequest);
		}
		finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value = "/getBidListPdf", method = RequestMethod.GET)
	public @ResponseBody void printBidListPdf( 
			@RequestParam(value="blDivisionID", required= false) Integer blDivisionID,
			@RequestParam(value="bidListFromDate", required= false) String bidListFromDate, 
			@RequestParam(value="bidListToDate", required= false) String bidListToDate,
			@RequestParam(value="includeEngineer", required= false) Boolean isIncludeEngineer,
			@RequestParam(value="biddetails", required= false) Boolean isBiddetails,
			HttpSession session, HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession theSession) throws IOException, DocumentException, ParseException, MessagingException, SQLException, JRException, UserException{
		Connection connection = null;
		ConnectionProvider con = null;
		UserBean aUserBean= (UserBean) theSession.getAttribute(SessionConstants.USER);
		System.out.println("blDivisionID "+blDivisionID+bidListFromDate+bidListToDate);
			try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/BidListReport.jrxml");
			String condition = "";
			if(blDivisionID==null || blDivisionID==0){
				condition= "";
			}else{
				condition= " AND jom.coDivisionID="+blDivisionID+"";
			}
		
			con = itspdfService.connectionForJasper();
			
			String DATE_FORMAT_NOW = "MM/dd/yy h:mm a";
		    Date date = new Date();
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		    String stringDate = sdf.format(date );
		    String filename="PurchaseOrder_"+stringDate+".pdf";
		    
		    String newbidListFromDate=null;
		    String newbidListToDate=null;
		    
		    if(bidListFromDate!=null && !bidListFromDate.trim().equals("")){
				SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf1.parse(bidListFromDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    newbidListFromDate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+newbidListFromDate);
			}
			if(bidListToDate!=null && !bidListToDate.trim().equals("")){
				SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf1.parse(bidListToDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    newbidListToDate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+bidListToDate);
			}
		    
		    JasperDesign jd  = JRXmlLoader.load(path_JRXML);
		    String query = "SELECT DATE_FORMAT(BidDate, '%W') AS days, DATE_FORMAT(BidDate, '%M %d %Y') AS Dates, rxm.name,jom.Description,DATE_FORMAT(BidDate, '%h') AS bidtime,"
                           +"  jom.LocationCity,jom.LocationState,jom.coDivisionID,"
                           +"  IF (Source1 =1,(SELECT ValueString FROM sysVariable WHERE sysVariableID = 2014005037),\"\") AS source1,"
                           +"   IF (Source2 =1,(SELECT ValueString FROM sysVariable WHERE sysVariableID = 2014005038),\"\") AS source2,"
                           +"   IF (Source3 =1,(SELECT ValueString FROM sysVariable WHERE sysVariableID = 2014005039),\"\") AS source3,"
                           +"   IF (Source4 =1,(SELECT ValueString FROM sysVariable WHERE sysVariableID = 2014005040),\"\") AS source4,"
                           +"   sourceReport1,otherSource,PlanNumbers,BinNumber,DATE_FORMAT(PlanDate,'%m/%d/%Y') AS PlanDate,"
                           +"    jom.JobNumber AS JobNo,(SELECT rx.name FROM rxMaster rx WHERE rxMasterID=jom.rxCategory2 )AS engineer,"
                           +"    (SELECT FullName FROM tsUserLogin WHERE UserLoginID=jom.cuAssignmentID0)AS SalesRep,PlanNumbers"
                           +"    FROM joMaster jom"
                           +"   LEFT JOIN joBidder job ON jom.joMasterID=job.joMasterID"
                           +"   LEFT JOIN rxMaster rxm ON job.rxMasterID = rxm.rxMasterID"
                           +"    WHERE DATE(BidDate) >= ' "+newbidListFromDate+"' AND DATE(BidDate) <=' "+newbidListToDate+"' "+condition+" AND jom.JobStatus IN ('0','1') ORDER BY BidDate";
		System.out.println("query "+query);
		    JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
		    
		    File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
			String absolutePath = file.getAbsolutePath();
			absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
			
			if (OperatingSystemInfo.isWindows()) {
				itsLogger.info("This is Windows");
				absolutePath=absolutePath+"\\\\BidListReport_subreport.jasper";
			} else if (OperatingSystemInfo.isMac()) {
				itsLogger.info("This is Mac");
			} else if (OperatingSystemInfo.isUnix()) {
				itsLogger.info("This is Unix or Linux");
				absolutePath=absolutePath+"/BidListReport_subreport.jasper";
			} else if (OperatingSystemInfo.isSolaris()) {
				itsLogger.info("This is Solaris");
			} else {
				itsLogger.info("Your OS is not support!!");
			}
			
			params.put("currentdate&time",stringDate);
			params.put("fromdate",bidListFromDate);
			params.put("todate",bidListToDate);
			params.put("blDivisionID",blDivisionID);
			params.put("includeEngineer",isIncludeEngineer);
			params.put("biddetails",isBiddetails);
			params.put("newbidListFromDate",newbidListFromDate);
			params.put("newbidListToDate",newbidListToDate);
			params.put("subReportPath", absolutePath);
			
			List<String> addlist=new ArrayList<String>();
			//Job Settings
			addlist.add("PlanSpecLabel1");
			addlist.add("PlanSpecLabel2");
			addlist.add("SourceCheckBox1");
			addlist.add("SourceCheckBox2");
			addlist.add("SourceCheckBox3");
			addlist.add("SourceCheckBox4");
			addlist.add("SourceLabel1");
			addlist.add("SourceLabel2");
			ArrayList<Sysvariable> sysvariablelist= itsUserService.getInventorySettingsDetails(addlist);
			int i=0;
			for(Sysvariable theSysvariable:sysvariablelist){
			if(i==0){
				params.put("txt_PlanSpecLabel1", theSysvariable.getValueString());
			}else if(i==1){
				params.put("txt_PlanSpecLabel2", theSysvariable.getValueString());
			}/*else if(i==2){
				params.put("txt_SourceCheckBox1", theSysvariable.getValueString());
			}else if(i==3){
				params.put("txt_SourceCheckBox2", theSysvariable.getValueString());
			}else if(i==4){
				params.put("txt_SourceCheckBox3", theSysvariable.getValueString());
			}else if(i==5){
				params.put("txt_SourceCheckBox4", theSysvariable.getValueString());
			}else*/ if(i==6){
				params.put("txt_SourceLabel1", theSysvariable.getValueString());
			}else if(i==7){
				params.put("txt_SourceLabel2", theSysvariable.getValueString());
			}
			i=i+1;
			}
			
			connection = con.getConnection();

				ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
		
		} catch (SQLException e) {
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>blDivisionID:</b>"+blDivisionID,"Salescontroller",e,theSession,theRequest);
		}
			finally{
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