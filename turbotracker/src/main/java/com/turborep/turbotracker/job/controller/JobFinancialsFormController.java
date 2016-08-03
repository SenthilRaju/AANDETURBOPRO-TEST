package com.turborep.turbotracker.job.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
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

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoFinancialReportTemp;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobFinanceService;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.OperatingSystemInfo;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/jobtabs6")
public class JobFinancialsFormController {
	
	Logger logger = Logger.getLogger(JobFinancialsFormController.class);
	
	private String jobNumber;
	
	private Jomaster joMasterDetails;
	private List<Cuinvoice> cuInvoiceDetails;
	private BigDecimal joChangeDetails;
	private String cutomerRxName;
	
	@Resource(name = "jobService")
	private JobService jobService;
	
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	@Resource(name = "userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "jobfinanceservice")
	private JobFinanceService jobfinanceService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@RequestMapping(value = "jobwizard_financial",method = RequestMethod.GET)
	public String getJobsFinancialPage(@RequestParam(value="jobNumber", required=false) String theJobNumber, 
									@RequestParam(value="jobName", required=false) String theJobName,
									@RequestParam(value="jobCustomer", required=false) String theJobCustomer,
									@RequestParam(value="joMasterID", required=false) Integer joMasterID,
									HttpSession session, ModelMap model, HttpServletResponse response) throws IOException, JobException {
		HashMap<String, Object> financeTabFields = null;
		
		if (joMasterID != null) {
			financeTabFields = jobfinanceService.getJobFinanceDetails(joMasterID);
		}
		model.addAttribute("financeTabFields",financeTabFields);
		return "job/jobwizard_financial";
	}

	@RequestMapping(value = "financial",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getFinancialList(@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session,HttpServletRequest therequest, HttpServletResponse theResponse) throws JobException, IOException, MessagingException {
		CustomResponse response = new CustomResponse();
		try{
			List<?> bids = jobService.getFinancialList(joMasterID);
			response.setRows(bids);
		}catch (JobException e) {
			sendTransactionException("<b>theJoMasterID:</b>"+joMasterID,"JOB",e,session,therequest);
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "financialAmountDetails", method = RequestMethod.GET)
	public @ResponseBody ModelMap getFinancialTabDetails(@RequestParam(value="jobNumber", required=false) String jobNumber,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			  									HttpServletRequest theRequest, ModelMap model,HttpServletResponse theResponse) throws JobException{
//		Integer joMasterID = null;
		HashMap<String, Object> financeTabFields = null;
		if (joMasterID != null) {
			financeTabFields = jobfinanceService.getJobFinanceDetails(joMasterID);
			//financeTabFields = jobfinanceService.getJobFinanceDetails(joMasterID);
		}
		model.addAttribute("financeTabFields",financeTabFields);
		return model;
	}
	
	@RequestMapping(value = "printFinancialReport", method = RequestMethod.GET)
	public @ResponseBody void printFinancialReport(@RequestParam(value="jobNumber", required=false) String jobNumber,
												  @RequestParam(value="noOfRows", required=false) Integer noOfRows  ,
												  @RequestParam(value="joMasterID", required=false) Integer jomasterID,
												  HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws SQLException, IOException, MessagingException{
		
		BigDecimal estimated = null;
		BigDecimal contractAmt = null;
		BigDecimal actualProfit = null;
		BigDecimal revisedContract = null;
		BigDecimal estRatio = null;
		BigDecimal actRatio = null;
		DecimalFormat format = new DecimalFormat("$#,##0.00;-$#,##0.00");
		DecimalFormat ratioFormat = new DecimalFormat("#,##0.00;-#,##0.00");
		HashMap<String, Object> financialReportInputs = null;
		String pathToReportTemplate = null;
		ConnectionProvider con = itspdfService.connectionForJasper();
		Connection connection =null;
		try {
			//Integer jomasterID = jobService.getJoMasterId(jobNumber);
			financialReportInputs = new HashMap<String, Object>();
			HashMap<String, Object> financeTabFields = null;
			ArrayList<JoFinancialReportTemp>  JoFinancialReportTemplist = new ArrayList<JoFinancialReportTemp>();
			JoFinancialReportTemplist = jobfinanceService.getFinanceReportTemp(jomasterID); 
			logger.info("Total finance List Siza::"+JoFinancialReportTemplist.size());
			financeTabFields = jobfinanceService
					.getJobFinanceDetails(jomasterID);
			noOfRows = jobfinanceService.getInvoiceCounts(jomasterID);
			if (financeTabFields != null) {
				estimated = (BigDecimal) financeTabFields.get("estimatedProfit");
				contractAmt = (BigDecimal) financeTabFields.get("contractAmount");
				if (estimated != null && contractAmt != null && contractAmt.compareTo(BigDecimal.ZERO) != 0) {
					estRatio = estimated.divide(contractAmt, 2,RoundingMode.HALF_UP);
					contractAmt = new BigDecimal(100); 
					estRatio = estRatio.multiply(contractAmt);
				}
				actualProfit = (BigDecimal) financeTabFields.get("actualProfit");
				revisedContract = (BigDecimal) financeTabFields.get("revisedContract");
				if (actualProfit != null && revisedContract != null && revisedContract.compareTo(BigDecimal.ZERO) != 0) {
					contractAmt = new BigDecimal(100); 				/*to avoid creating new object, Reused the existing one.*/
					actRatio = actualProfit.divide(revisedContract, 2,RoundingMode.HALF_UP);
					actRatio = actRatio.multiply(contractAmt);
				}
				financialReportInputs.put("joMaster", jomasterID);
				if (financeTabFields.get("estimatedCost") != null)
					financialReportInputs.put("cost", format.format(((BigDecimal) financeTabFields.get("estimatedCost")).doubleValue()));
				else
					financialReportInputs.put("cost", "$0.00");
				if (financeTabFields.get("estimatedProfit") != null)
					financialReportInputs.put("profit",format.format(estimated.doubleValue()));
				else
					financialReportInputs.put("profit", "$0.00");
				if (estRatio != null)
					financialReportInputs.put("ratio",ratioFormat.format(estRatio.doubleValue()));
				else
					financialReportInputs.put("ratio", "$0.00");
				if (financeTabFields.get("contractAmount") != null)
					financialReportInputs.put("contract", format.format(((BigDecimal) financeTabFields.get("contractAmount")).doubleValue()));
				else
					financialReportInputs.put("contract", "$0.00");
				if (financeTabFields.get("changeOrder") != null)
					financialReportInputs.put("changeorder", format.format(((BigDecimal) financeTabFields.get("changeOrder")).doubleValue()));
				else
					financialReportInputs.put("changeorder", "$0.00");
				if (financeTabFields.get("revisedContract") != null)
					financialReportInputs.put("revised", format.format(((BigDecimal) financeTabFields.get("revisedContract")).doubleValue()));
				else
					financialReportInputs.put("revised", "$0.00");
				if (financeTabFields.get("actualCost") != null)
					financialReportInputs.put("costact", format.format(((BigDecimal) financeTabFields.get("actualCost")).doubleValue()));
				else
					financialReportInputs.put("costact", "$0.00");
				if (financeTabFields.get("actualProfit") != null)
					financialReportInputs.put("profitact", format.format(((BigDecimal) financeTabFields.get("actualProfit")).doubleValue()));
				else
					financialReportInputs.put("profitact", "$0.00");
				if (actRatio != null)
					financialReportInputs.put("ratioact",
							ratioFormat.format(actRatio.doubleValue()));
				else
					financialReportInputs.put("ratioact", "$0.00");

				/**
				 * The below code is to be uncommented when customer invoice is
				 * fully functional Till then, it shows $0.00 using the next
				 * block code
				 */
				/*
				 * params.put("invoiced",
				 * financeTabFields.get("invoiced").toString());
				 * params.put("wtax",
				 * financeTabFields.get("invoicedWithTax").toString());
				 * params.put("paid",
				 * financeTabFields.get("paidToDate").toString());
				 * params.put("ar", financeTabFields.get("AR").toString());
				 * params.put("billing",
				 * financeTabFields.get("billingReminder").toString());
				 * params.put("esttax",
				 * financeTabFields.get("estimatedTax").toString());
				 * params.put("closeoutamount",
				 * financeTabFields.get("closeOut").toString());
				 */
				if (financeTabFields.get("invoiced") != null){
					financialReportInputs.put("invoiced",   format.format(((BigDecimal) financeTabFields.get("invoiced")).doubleValue()));	
				}else{
					financialReportInputs.put("invoiced",   "$0.00");
				}
				if (financeTabFields.get("invoicedWithTax") != null){
					financialReportInputs.put("wtax",  format.format(((BigDecimal) financeTabFields.get("invoicedWithTax")).doubleValue()));	
				}else{
					financialReportInputs.put("wtax",   "$0.00");
				}
				if (financeTabFields.get("paidToDate") != null){
					financialReportInputs.put("paid",  format.format(((BigDecimal) financeTabFields.get("paidToDate")).doubleValue()));	
				}else{
					financialReportInputs.put("paid",   "$0.00");
				}
				if (financeTabFields.get("AR") != null){
					financialReportInputs.put("ar",  format.format(((BigDecimal) financeTabFields.get("AR")).doubleValue()));	
				}else{
					financialReportInputs.put("ar",   "$0.00");
				}
				if (financeTabFields.get("billingReminder") != null){
					financialReportInputs.put("billing", format.format((((BigDecimal)financeTabFields.get("revisedContract")).subtract( ((BigDecimal) financeTabFields.get("invoicedwofretax")  ))).doubleValue()));
				}else{
					financialReportInputs.put("billing",   "$0.00");
				}
				if (financeTabFields.get("estimatedTax") != null){
					financialReportInputs.put("esttax", format.format(((BigDecimal) financeTabFields.get("estimatedTax")).doubleValue()));	
				}else{
					financialReportInputs.put("esttax",   "$0.00");
				}
				if (financeTabFields.get("closeOut") != null){
					financialReportInputs.put("closeoutamount",format.format(((BigDecimal) financeTabFields.get("closeOut")).doubleValue()));	
				}else{
					financialReportInputs.put("closeoutamount",   "$0.00");
				}
				
				
				
				
				// params.put("sidenotes",
				// financeTabFields.get("estimatedCost")); /*Need to get
				// 'sidenotes' from database and pass it to report here */

			}
			if (noOfRows > 0) {
				File file = new File( therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
				String absolutePath = file.getAbsolutePath();
				absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
				String SubReportPath="";
				if (OperatingSystemInfo.isWindows()) {
					logger.info("This is Windows");
					SubReportPath=absolutePath+"\\\\FinancialSubreport.jasper";
				} else if (OperatingSystemInfo.isMac()) {
					logger.info("This is Mac");
				} else if (OperatingSystemInfo.isUnix()) {
					logger.info("This is Unix or Linux");
					SubReportPath=absolutePath+"/FinancialSubreport.jasper";
				} else if (OperatingSystemInfo.isSolaris()) {
					logger.info("This is Solaris");
				} else {
					logger.info("Your OS is not support!!");
				}
				financialReportInputs.put("SubReportPath", SubReportPath);
				pathToReportTemplate = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Financialreport.jrxml");
			} else {
				pathToReportTemplate = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Finance_noRecord.jrxml");
			}
			ServletOutputStream out = theResponse.getOutputStream();
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			 connection = con.getConnection();
			JasperReport financialReport;
			financialReport = JasperCompileManager.compileReport(pathToReportTemplate);
			JasperPrint print = JasperFillManager.fillReport(financialReport, financialReportInputs, connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		}catch (Exception e) {
			sendTransactionException("<b>jobNumber:</b>"+jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(),e);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	
	@RequestMapping(value = "outstandingstmtReport", method = RequestMethod.GET)
	public @ResponseBody void outstandingstmtReport(@RequestParam(value="jobNumber", required=false) String jobNumber,
												  @RequestParam(value="noOfRows", required=false) Integer noOfRows  ,
												  @RequestParam(value="joMasterID", required=false) Integer jomasterID,
																		HttpSession session,HttpServletRequest therequest,HttpServletResponse theResponse) throws SQLException, IOException, MessagingException{
		
		String pathToReportTemplate = null;
		Connection connection  = null;
		ConnectionProvider con = itspdfService.connectionForJasper();
		try {
//			Integer jomasterID = jobService.getJoMasterId(jobNumber);
			System.out.println("No Of Rows:" );
			if (noOfRows == 0) {
				pathToReportTemplate = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/OutstandingInvoiceStmt.jrxml");
			} else {
				pathToReportTemplate = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/InvoiceSummaryStmt.jrxml");
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("jobnumber", jobNumber);
			
			connection = con.getConnection();
			ReportService.ReportCall(theResponse, map, "pdf",pathToReportTemplate, "Finance.pdf",connection);
		}catch (Exception e) {
			sendTransactionException("<b>jobNumber:</b>"+jobNumber,"JOB",e,session,therequest);
			logger.error(e.getMessage(),e);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				} 
		}
	}
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		
		this.jobNumber = jobNumber;
	}
	
	public Jomaster getJomasterDetails() {
			
			return joMasterDetails;
	}
	public void setJomasterDetails(String jobNumber,Integer joMasterID) throws JobException {
		
		this.joMasterDetails = jobService.getSingleJobDetails(jobNumber,joMasterID);
		
	}
	public List<Cuinvoice> getCuInvoiceDetails() {
		
		return cuInvoiceDetails;
	}
	public void setCuInvoiceDetails(String jobNumber) throws JobException {
		
		this.cuInvoiceDetails = jobService.getCuInvoiceDetails(jobNumber);
	}
	
	public BigDecimal getJoChangeDetails() {
		
		return joChangeDetails;
	}
	public void setJoChangeDetails(String jobNumber) throws JobException {
		this.joChangeDetails = jobService.getJobChangeAmount(jobNumber);
	}
	

	public String getCutomerRxName() {
		return cutomerRxName;
	}

	public void setCutomerRxName(String cutomerRxName) {
		this.cutomerRxName = cutomerRxName;
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
