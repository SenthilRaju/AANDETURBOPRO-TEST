package com.turborep.turbotracker.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

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

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.controller.JobController;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/reportController")
public class ReportController {
	
	protected static Logger logger = Logger.getLogger(JobController.class);
	
	@Resource(name="jobService")
	private JobService itsjobService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@RequestMapping(value="/previewreport",method = RequestMethod.GET)
	public @ResponseBody void previewBookedReport (@RequestParam(value = "salesRepID", required = false) Integer cuAssignmentID,
														@RequestParam(value = "reportName", required = false) String reportName,
														HttpSession theSession, HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, SQLException, MessagingException{
		Connection connection = null;
		ConnectionProvider con = null;
		try {
		String path_JRXML = "";
		if(cuAssignmentID == null){
			path_JRXML =  theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Booked.jrxml");
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		con =  itspdfService.connectionForJasper();
		ServletOutputStream out = theResponse.getOutputStream();
		theResponse.setHeader("Content-Disposition", "inline"); 
		theResponse.setContentType("application/pdf");
		connection = con.getConnection();
		path_JRXML =  theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Booked.jrxml");
		JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
		JasperPrint print = JasperFillManager.fillReport(report, params, connection);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(print, baos);
		out.write(baos.toByteArray());
		out.flush();
		out.close();
		} catch (JRException e) {
			e.printStackTrace();
			sendTransactionException("<b>cuAssignmentID:</b>"+cuAssignmentID ,"ReportController",e,theSession,theRequest);
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
