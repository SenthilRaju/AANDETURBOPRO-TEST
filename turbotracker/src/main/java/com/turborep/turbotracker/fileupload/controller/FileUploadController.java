package com.turborep.turbotracker.fileupload.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.turborep.turbotracker.fileupload.exception.FileUploadException;
import com.turborep.turbotracker.fileupload.service.FileUploadService;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

	Logger logger = Logger.getLogger(FileUploadController.class);
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "fileUploadService")
	private FileUploadService fileUploadService;
	
	@RequestMapping(value = "/excelupload", method = RequestMethod.POST)
	public @ResponseBody String upload(MultipartHttpServletRequest request, HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {                 
	 
		//0. notice, we have used MultipartHttpServletRequest
	 
		//1. get the files from the request object
		Iterator<String> itr =  request.getFileNames();

		MultipartFile mpf = request.getFile(itr.next());
		String vepoId = request.getParameter("vepoId");
		System.out.println(vepoId);
		System.out.println(mpf.getOriginalFilename() +" uploaded!");

		try {
			//just temporary save file info into ufile
			Integer length = mpf.getBytes().length;
			byte[] bytes= mpf.getBytes();
			String type = mpf.getContentType();
			String name = mpf.getOriginalFilename();
			InputStream fis = mpf.getInputStream();
			fileUploadService.insertExcelData(fis, Integer.valueOf(vepoId));
			
		    fis.close();
		    
		} catch (IOException e) {
			sendTransactionException("<b>MethodName:</b>excelupload","FileUpload",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(500, "IO exception occurred:" + e.getMessage());
		} catch (FileUploadException e) {
			sendTransactionException("<b>MethodName:</b>excelupload","FileUpload",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(e.getErrorStatusCode(), "file upload Exception:" + e.getMessage());
		}
	 
		return "Success";

	}
	
	@RequestMapping(value = "/uploadcompanylogo", method = RequestMethod.POST)
	public @ResponseBody String uploadCompanyLogo(MultipartHttpServletRequest request, HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {                 
	 
		//0. notice, we have used MultipartHttpServletRequest
	 
		//1. get the files from the request object
		Iterator<String> itr =  request.getFileNames();
		System.out.println("Uploading...");
		MultipartFile mpf = request.getFile(itr.next());

		try {
			//just temporary save file info into ufile
			Integer length = mpf.getBytes().length;
			byte[] bytes= mpf.getBytes();
			
			String type = mpf.getContentType();
			String name = mpf.getOriginalFilename();
			InputStream fis = mpf.getInputStream();
			Blob blob = new SerialBlob(bytes);
			fileUploadService.uploadCompanyLogo(blob);
		    fis.close();
		
		} catch (IOException e) {
			sendTransactionException("<b>MethodName:</b>excelupload","FileUpload",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(500, "IO exception occurred:" + e.getMessage());
		} catch (SerialException e) {
			sendTransactionException("<b>MethodName:</b>excelupload","FileUpload",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(500, "IO exception occurred:" + e.getMessage());
		} catch (SQLException e) {
			sendTransactionException("<b>MethodName:</b>excelupload","FileUpload",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(500, "IO exception occurred:" + e.getMessage());
		} catch (FileUploadException e) {
			sendTransactionException("<b>MethodName:</b>excelupload","FileUpload",e,session,therequest);
			logger.error(e.getMessage(), e);
			response.sendError(500, "IO exception occurred:" + e.getMessage());
		}
		return "Success";
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
