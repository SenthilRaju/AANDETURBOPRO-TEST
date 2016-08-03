package com.turborep.turbotracker.mail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.util.SessionConstants;

public class SendQuoteMail {

	private String itsFrom;
	private String itsTo;
	private String itsSubject;
	private String itsText;
	private String itsJobNumber;
	private String itsCCAddr1;
	private String itsCCAddr2;
	private String itsCCAddr3;
	private String itsCCAddr4;
	private String itsCCName1;
	private String itsCCName2;
	private String itsCCName3;
	private String itsCCName4;
	private String itsBCCAddr;
	private String itsQuoteInfo;
	private String itsPOOrderNo;
	private String userID;
	private String password;
	private String filename;
	private String ccAddress;
	private String writepdfname;
	private Pattern pattern;
	private Matcher matcher;
	
	
	
	
  	private static final String EMAIL_PATTERN = 
  	  		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
  	  		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	
	protected static Logger itsLogger = Logger.getLogger(SendQuoteMail.class);
	
	
	
	public SendQuoteMail(String theEmailFromAddress, String theSendTo, String theEmailSubjectTxt, String theEmailMsgTxt, String theSSL_FACTORY, String theJobNumber, 
											String theCCAddr1, String theCCAddr2, String theCCAddr3, String theCCAddr4, String theCCName1, String theCCName2, String theCCName3, String theCCName4, String theBCCAddr, String theQuoteIdentify, String thePONumber)
	{
		this.itsFrom = theEmailFromAddress;
		 this.itsTo = theSendTo;
		this.itsSubject = theEmailSubjectTxt;
		this.itsText = theEmailMsgTxt;
		this.itsJobNumber = theJobNumber;
		this.itsCCAddr1 = theCCAddr1;
		this.itsCCAddr2 = theCCAddr2;
		this.itsCCAddr3 = theCCAddr3;
		this.itsCCAddr4 = theCCAddr4;
		this.itsCCName1 = theCCName1;
		this.itsCCName2 = theCCName2;
		this.itsCCName3 = theCCName3;
		this.itsCCName4 = theCCName4;
		this.itsBCCAddr = theBCCAddr;
		this.itsQuoteInfo = theQuoteIdentify;
		this.itsPOOrderNo = thePONumber;
	}
	
	public SendQuoteMail() {
		// TODO Auto-generated constructor stub
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	public SendQuoteMail(String theEmailFromAddress, String theSendTo, String theEmailSubjectTxt, String theEmailMsgTxt, String theSSL_FACTORY, String theJobNumber, 
			 String theBCCAddr, String thePONumber,String filename,String ccAddress,String writepdfname)
			{
			this.itsFrom = theEmailFromAddress;
			this.itsTo = theSendTo;
			this.itsSubject = theEmailSubjectTxt;
			this.itsText = theEmailMsgTxt;
			this.itsJobNumber = theJobNumber;
			this.itsBCCAddr = theBCCAddr;
			this.itsPOOrderNo = thePONumber;
			this.filename=filename;
			this.ccAddress=ccAddress;
			this.writepdfname=writepdfname;
			}
	
	
	public static void sendStatement(String fromAddress, String toAddress,String theUserID, String thePassword, String thePort, String thehost, String filePath) throws MessagingException{
		  String to=toAddress;  /*For UAT */
		  itsLogger.info("Sending the Mail to :" + toAddress);
		  to  = "kannan.subbu@in.sysvine.com"; /*Please comment out this line in uat.*/
		  final String user=theUserID;
		  final String password=thePassword;
		   
		  Properties properties = System.getProperties();  
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.debug", "false");	
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", thehost);
			properties.put("mail.smtp.port", thePort);
		  
		  Session session = Session.getDefaultInstance(properties,  
		   new javax.mail.Authenticator() {  
		   protected PasswordAuthentication getPasswordAuthentication() {  
		   return new PasswordAuthentication(user,password);  
		   }  
		  });  
		     
		  try{  
			  
		    MimeMessage message = new MimeMessage(session);  
		    message.setFrom(new InternetAddress(user));  	
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		    message.setSubject("Statements");  
		      
		    BodyPart messageBodyPart1 = new MimeBodyPart();  
		    messageBodyPart1.setText("The attached quote is in PDF format.  If you have trouble reading it a free reader can be downloaded at http://www.adobe.com/");  
		    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		    
			String path = filePath;
			DataSource source = new FileDataSource(path); 
		    messageBodyPart2.setDataHandler(new DataHandler(source));
		    messageBodyPart2.setFileName("Statements.pdf");  
		     
		    Multipart multipart = new MimeMultipart();  
		    multipart.addBodyPart(messageBodyPart1);  
		    multipart.addBodyPart(messageBodyPart2);  
		  
		    message.setContent(multipart);  
		     
		    Transport transport = session.getTransport("smtp"); 
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
		    
		    File file = FileUtils.getFile(path);
		    file.delete();
		   
		    itsLogger.info("customer Statements, Mail Sent Successfully and file deleted.....");
		} catch (MessagingException e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new MessagingException(e.getMessage(), e);
		}
	}
	
	public void send(String theUserID, String thePassword, String thePort, String thehost,HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
		
		Properties props = new Properties();
		try{
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "false");					  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", thehost); 
			props.put("mail.smtp.port", thePort); 
			props.put("mail.smtp.user", theUserID); 
			props.put("mail.smtp.password", thePassword);
			
			//while uploading in to the server should comment this below line otherwise not working email sending in uat
	 /*       props.put("mail.smtp.socketFactory.port", thePort);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			*/
			 if(thehost.equals("smtp.gmail.com")||thehost.equals("secure.emailsrvr.com")){
	        	 props.put("mail.smtp.socketFactory.port", thePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }
	         /*else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * */
			userID = theUserID;
			password = thePassword;
//			Session session = Session.getDefaultInstance(props, null); 
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(userID, password);
			    }
			});
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		try {
				fromAddress = new InternetAddress(itsFrom);
//				itsTo = "velsubha@gmail.com";
				toAddress = new InternetAddress(itsTo);
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			
			int a=0;
			Address[] toArray;
			StringTokenizer to = new StringTokenizer(itsTo,",;"); 
			toArray = new InternetAddress[to.countTokens()];
			while(to.hasMoreTokens()) { 
			toArray[a] =new InternetAddress(to.nextToken()); 
			a++;
			} 
			message.setRecipients(RecipientType.TO, toArray);
		
			if(ccAddress!=null && ccAddress!=""){
				int x=0;
				Address[] ccArray;
				StringTokenizer cc = new StringTokenizer(ccAddress, ",;"); 
				ccArray = new InternetAddress[cc.countTokens()];
				while(cc.hasMoreTokens()) { 
				ccArray[x] =new InternetAddress(cc.nextToken()); 
				x++;
				} 
				message.setRecipients(Message.RecipientType.CC, ccArray);
			}
			
			message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			message.setSubject(itsSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(itsText);
			multiPart.addBodyPart(bodyContent);
			
			File dir1 = new File ("/var/quotePDF/");

			String path = "";
			URL attachFile = null;
			if(itsQuoteInfo.equalsIgnoreCase("Quote.")){
				String root = request.getRealPath("/");
				File filepath = new File(root + "/uploads");
	           // File attachFilename = new File(path + "/" + filename);
				//path = dir1.getCanonicalPath()+"/Quotes.pdf";
				//attachFile = new URL("http://"+path);
				String pdfname="Quotes#"+itsJobNumber.replaceAll("'", "")+".pdf";
				 File attachFilename = new File(filepath + "/" + pdfname);
//				attachFile = new URL("file://"+path);
				if (attachFilename != null) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName("Quote #"+itsJobNumber.replaceAll("'", "")+".pdf");
					multiPart.addBodyPart(attachment);
				}
			}else if(itsQuoteInfo.equalsIgnoreCase("poGeneral")){
				path = dir1.getCanonicalPath()+"/PuchaseGeneralOrder.pdf";
				attachFile = new URL("http://"+path);
				if (attachFile != null) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFile.getFile())));
					attachment.setFileName("P.O. General # "+itsPOOrderNo+".pdf");
					multiPart.addBodyPart(attachment);
				}
			}else if(itsQuoteInfo.equalsIgnoreCase("poAck")){
				
				path = dir1.getCanonicalPath()+"/PuchaseAcknowledgmentOrder.pdf";
				attachFile = new URL("http://"+path);
				if (attachFile != null) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFile.getFile())));
					attachment.setFileName("P.O. Acknowledgment # "+itsPOOrderNo+".pdf");
					multiPart.addBodyPart(attachment);
				}
			}else if(itsQuoteInfo.equalsIgnoreCase("salesOrder")){
				String root = request.getRealPath("/");
				File filepath = new File(root + "/uploads");
				File attachFilename = new File(filepath + "/" + "SalesOrderWithoutprice.pdf");
				//path = dir1.getCanonicalPath()+"/salesOrder.pdf";
			//attachFile = new URL("http://"+path);
				if (attachFilename != null) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName("salesOrder # "+itsPOOrderNo+".pdf");
					multiPart.addBodyPart(attachment);
				}
			}else if(itsQuoteInfo.equalsIgnoreCase("CustomerInvoice")){				
				path = dir1.getCanonicalPath()+"/customerInvoice.pdf";				
				attachFile = new URL("http://"+path);
				if (attachFile != null) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFile.getFile())));
					attachment.setFileName("CustomerInvoice # "+itsPOOrderNo+".pdf");
					multiPart.addBodyPart(attachment);
				}
			}else{
				path = dir1.getCanonicalPath()+"/PuchaseOrder.pdf";
				attachFile = new URL("http://"+path);
//				attachFile = new URL("file://"+path);
				if (attachFile != null) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFile.getFile())));
					attachment.setFileName("P.O. # "+itsPOOrderNo+".pdf");
					multiPart.addBodyPart(attachment);
				}
			}
			message.setContent(multiPart);
			Transport transport = session.getTransport("smtp"); 
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
		} catch (MessagingException e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}
	}
	/*public void sendMailAttachment(String theUserID, String thePassword, String thePort, String thehost,HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
		Properties props = new Properties();
		try{
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "true");					  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", thehost); 
			
			props.put("mail.smtp.user", theUserID); 
			props.put("mail.smtp.password", thePassword);
			
			//while uploading in to the server should comment this below line otherwise not working email sending in uat
	        props.put("mail.smtp.socketFactory.port", thePort);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			 if(thehost.equals("smtp.gmail.com")||thehost.equals("secure.emailsrvr.com")){
	        	 System.out.println("insi security");
	        	 props.put("mail.smtp.socketFactory.port", thePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }else{
	        	 props.put("mail.smtp.port", thePort); 
	         }
	         else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * 
			
			
			
			userID = theUserID;
			password = thePassword;
			
			
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(userID, password);
			    }
			});
			
			System.out.println("Session Port : " + session.getProperties().getProperty("mail.smtp.port"));
			
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		try {
				fromAddress = new InternetAddress(itsFrom);
				System.out.println("on send mail attachement");
				toAddress = new InternetAddress(itsTo);
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			message.setRecipient(RecipientType.TO, toAddress);
			if(ccAddress!=null && ccAddress!=""){
				message.setRecipients(Message.RecipientType.CC, ccAddress);
			}
			
			//message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			message.setSubject(itsSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			System.out.println("itsText=="+itsText);
			bodyContent.setText(itsText);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writepdfname);
            System.out.println("httpattachFile"+attachFilename);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(filename);
					multiPart.addBodyPart(attachment);
			message.setContent(multiPart);
			Transport transport = session.getTransport("smtp"); 
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			System.out.println("Mail sent ");
		} catch (MessagingException e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}
	}*/
	
	/*@SuppressWarnings("finally")
	public Boolean sendMailAttachment(String theUserID, String thePassword, String thePort, String thehost,HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
        System.out.println("UserId=="+theUserID+"Password=="+thePassword);
		boolean testsendornot=false;
		Properties props = new Properties();
		try{
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "true");					  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", thehost); 
			
			props.put("mail.smtp.user", theUserID); 
			props.put("mail.smtp.password", thePassword);
			
			//while uploading in to the server should comment this below line otherwise not working email sending in uat
	        props.put("mail.smtp.socketFactory.port", thePort);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			 if(thehost.equals("smtp.gmail.com")||thehost.equals("secure.emailsrvr.com")){
	        	 System.out.println("insi security");
	        	 props.put("mail.smtp.socketFactory.port", thePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }else{
	        	 props.put("mail.smtp.port", thePort); 
	         }
	         else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * 
			
			
			
			userID = theUserID;
			password = thePassword;
			
			
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(userID, password);
			    }
			});
			
			System.out.println("Session Port : " + session.getProperties().getProperty("mail.smtp.port"));
			
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		try {
				fromAddress = new InternetAddress(itsFrom);
				System.out.println("on send mail attachement");
				toAddress = new InternetAddress(itsTo);
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			message.setRecipient(RecipientType.TO, toAddress);
			if(ccAddress!=null && ccAddress!=""){
				message.setRecipients(Message.RecipientType.CC, ccAddress);
			}
			
			//message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			message.setSubject(itsSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			System.out.println("itsText=="+itsText);
			bodyContent.setText(itsText);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writepdfname);
            System.out.println("httpattachFile"+attachFilename);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(filename);
					multiPart.addBodyPart(attachment);
			message.setContent(multiPart);
			Transport transport = session.getTransport("smtp"); 
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			System.out.println("Mail sent ");
			testsendornot=true;
			//return true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}finally{
			return testsendornot;
		}
		
	}*/
	
	@SuppressWarnings("finally")
	public Boolean sendMailAttachment(String theUserID, String thePassword, String thePort, String thehost,HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
		boolean testsendornot=false;
		Properties props =null;
				props =new Properties();
		try{
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "false");					  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", thehost); 
			
			props.put("mail.smtp.user", theUserID); 
			props.put("mail.smtp.password", thePassword);
			props.put("mail.smtp.port", thePort);
			
			//while uploading in to the server should comment this below line otherwise not working email sending in uat
	      /*  props.put("mail.smtp.socketFactory.port", thePort);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");*/
			 if(thehost.equals("smtp.gmail.com")||thehost.equals("secure.emailsrvr.com")){
	        	 props.put("mail.smtp.socketFactory.port", thePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }else{
	        	 props.put("mail.smtp.port", thePort); 
	         }
	         /*else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * */
			
			
			
			userID = theUserID;
			password = thePassword;
			Session session =null;
			session=Session.getDefaultInstance(props, null);
			/*Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(userID, password);
			    }
			});*/
			
			UserBean aUserBean;
			String usernames="";
			aUserBean = (UserBean) request.getSession().getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				usernames = aUserBean.getFullName();
			}
			
		
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		//try {
				fromAddress = new InternetAddress(itsFrom,usernames);
			//	toAddress = new InternetAddress(itsTo);
			/*} catch (AddressException e) {	
				e.printStackTrace();
			}*/
			message.setFrom(fromAddress);
			
			int a=0;
			Address[] toArray;
			StringTokenizer to = new StringTokenizer(itsTo,",;"); 
			toArray = new InternetAddress[to.countTokens()];
			while(to.hasMoreTokens()) { 
			toArray[a] =new InternetAddress(to.nextToken()); 
			a++;
			} 
			message.setRecipients(RecipientType.TO, toArray);
		
			if(ccAddress!=null && ccAddress!=""){
				int x=0;
				Address[] ccArray;
				StringTokenizer cc = new StringTokenizer(ccAddress, ",;"); 
				ccArray = new InternetAddress[cc.countTokens()];
				while(cc.hasMoreTokens()) { 
				ccArray[x] =new InternetAddress(cc.nextToken()); 
				x++;
				} 
				message.setRecipients(Message.RecipientType.CC, ccArray);
			}
			if(itsBCCAddr!=null && itsBCCAddr!=""){
				message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			}
			message.setSubject(itsSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(itsText);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			if(filename!=null){
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writepdfname);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(filename);
					multiPart.addBodyPart(attachment);
			}
			message.setContent(multiPart);
			
			int port =Integer.valueOf(thePort);
			if(port==587){
				//transport.connect(thehost,Integer.valueOf(thePort), theUserID, thePassword);
				testsendornot=sendFrom587(theUserID,thePassword,thePort,thehost,request);
			}else{
			Transport transport = session.getTransport("smtp"); 
			System.out.println("thehost : " + thehost +" theUserID " +theUserID +" thePassword"+thePassword);
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			itsLogger.info("Mail sent ");
			testsendornot=true;
			}
			//return true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}finally{
			return testsendornot;
		}
		
	}
	
	@SuppressWarnings("finally")
	public Boolean sendFrom587(final String theUserID, final String thePassword, String thePort, String thehost,HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
		boolean testsendornot=false;
	//	Properties props =null;
		try{
			
			  Properties props = new Properties();
			    props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.starttls.enable", "true");
			    props.put("mail.smtp.host", thehost);
			    props.put("mail.smtp.port", thePort);
			    props.put("mail.debug", "false");	
			
			userID = theUserID;
			password = thePassword;
			Session session1 = Session.getInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(theUserID, thePassword);
			    }
			});
			
			
			MimeMessage message = new MimeMessage(session1); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
			/*
			UserBean aUserBean;
			String usernames="";
			aUserBean = (UserBean) request.getSession().getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				usernames = aUserBean.getFullName();
			}*/
			
			
		try {
			/* for mask actual sender from other Email ID
			 * fromAddress = new InternetAddress(itsFrom);*/
			//fromAddress = new InternetAddress(itsFrom,usernames);
			fromAddress = new InternetAddress(theUserID,itsFrom);
			//fromAddress.setPersonal(usernames);
			toAddress = new InternetAddress(itsTo);
			
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			
			int a=0;
			Address[] toArray;
			StringTokenizer to = new StringTokenizer(itsTo,",;"); 
			toArray = new InternetAddress[to.countTokens()];
			while(to.hasMoreTokens()) { 
			toArray[a] =new InternetAddress(to.nextToken()); 
			a++;
			} 
			message.setRecipients(RecipientType.TO, toArray);
			//Following line Added by Jenith for Set Custom Email for ReplyTo:
			message.setReplyTo(new javax.mail.Address[]{new javax.mail.internet.InternetAddress(itsFrom)});
			
			if(ccAddress!=null && ccAddress!=""){
				int x=0;
				Address[] ccArray;
				StringTokenizer cc = new StringTokenizer(ccAddress, ",;"); 
				ccArray = new InternetAddress[cc.countTokens()];
				while(cc.hasMoreTokens()) { 
				ccArray[x] =new InternetAddress(cc.nextToken()); 
				x++;
				} 
				message.setRecipients(Message.RecipientType.CC, ccArray);
			}
			if(itsBCCAddr!=null && itsBCCAddr!=""){
				message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			}
			message.setSubject(itsSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(itsText);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			
			if(filename!=null){
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writepdfname);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(filename);
					multiPart.addBodyPart(attachment);
			}
			message.setContent(multiPart);
			
			
			Transport transport = session1.getTransport("smtp"); 
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			itsLogger.info("Mail sent ");
			testsendornot=true;
			//return true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}finally{
			return testsendornot;
		}
		
	}
	/*@SuppressWarnings("finally")
	private Boolean sendFrom587(final String theUserID,final String thePassword,String toID,String bodyMsg) {
		// TODO Auto-generated method stub
		 final String username = "eric@aandespecialties.com";
		    final String password = "#turb0Pr0";
			Boolean returnvalue=false; 
		    Properties props = new Properties();
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.host", "outlook.office365.com");
		    props.put("mail.smtp.port", "587");

		    Session session = Session.getInstance(props,
		      new javax.mail.Authenticator() {
		        protected PasswordAuthentication getPasswordAuthentication() {
		            return new PasswordAuthentication(theUserID, thePassword);
		        }
		      });

		    try {

		        Message message = new MimeMessage(session);
		        message.setFrom(new InternetAddress(theUserID));
		        message.setRecipients(Message.RecipientType.TO,
		            InternetAddress.parse(toID));
		        message.setSubject("Test");
		        message.setText(bodyMsg);

		        Transport.send(message);

		        System.out.println("Done");
		        returnvalue=true;
		    } catch (MessagingException e) {
		        throw new RuntimeException(e);
		    }finally{
				return returnvalue;
			}
		
	}*/
	
	@SuppressWarnings("finally")
	public Boolean sendTransactionInfo(Transactionmonitor transObj,HttpServletRequest request) throws IOException, MessagingException{

		String theUserID="kamal.nazar2014@gmail.com",  thePassword="Passw0rd@0123",  thePort="465",  thehost="smtp.gmail.com";
		boolean testsendornot=false;
		Properties props =null;
				props =new Properties();
		try{
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "false");				  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", thehost); 
			
			props.put("mail.smtp.user", theUserID); 
			props.put("mail.smtp.password", thePassword);
			props.put("mail.smtp.port", thePort);
		
			 if(thehost.equals("smtp.gmail.com")||thehost.equals("secure.emailsrvr.com")){
	        	 props.put("mail.smtp.socketFactory.port", thePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }else{
	        	 props.put("mail.smtp.port", thePort); 
	         }
	       
			
			Session session =null;
			session=Session.getDefaultInstance(props, null);
				
			
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		try {
				fromAddress = new InternetAddress(theUserID);
				toAddress = new InternetAddress("v4.naveed@excelblaze.com");
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			message.addRecipient(RecipientType.TO, toAddress);
			message.setRecipients(Message.RecipientType.CC, "v4.jenith@excelblaze.com,v4.velmurugan@excelblaze.com");
			
			
			message.setSubject(transObj.getHeadermsg());
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(""+transObj.getTrackingId()+"<br>"
					+ "<b>Job Type: </b>"+transObj.getJobStatus()+"<br>"
						+ "<b>Username: </b>"+transObj.getUsername()+"<br>"
							+ "<b>DateTime: </b>"+transObj.getTimetotriggerd()+"<br>"
								+ "<b>Description: </b>"+transObj.getDescription()
							+ "");
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			
	
			message.setContent(multiPart);
			int port =Integer.valueOf(thePort);
			if(port==587){
				testsendornot=sendFrom587(theUserID,thePassword,thePort,thehost,request);
			}else{
			Transport transport = session.getTransport("smtp"); 
			transport.connect(thehost, theUserID, thePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			itsLogger.info("Mail sent ");
			testsendornot=true;
			}
			//return true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}finally{
			return testsendornot;
		}
		
	
	}
	
	@SuppressWarnings("finally")
	public Boolean sendBulkMailAttachment( EmailParameters eParam,  HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
	/*	boolean testsendornot=false;
	
	
		Properties props =null;
				props =new Properties();
	
			 String eUserID=null;
			 String ePassword=null;
			 String ePort=null;
			 String ehost=null;
			 String eSubject=null;
			 String eContent=null;
			 String writeFileName=null;
			 String attachmentFileName=null;
			 String fromEmailAddress=null;
			 String toEmailAddress=null;
		try{
			
			eUserID=eParam.geteUserID();
			ePassword=eParam.getePassword();
			ePort=eParam.getePort();
			ehost=eParam.getEhost();
			eSubject=eParam.geteSubject();
			eContent=eParam.geteContent();
			writeFileName=eParam.getWriteFileName();
			attachmentFileName=eParam.getFileName();
			fromEmailAddress=eParam.getFromAddress();
			toEmailAddress=eParam.getToAddress();
			
			
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "false");					  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", ehost); 
			
			props.put("mail.smtp.user", eUserID); 
			props.put("mail.smtp.password", ePassword);
			props.put("mail.smtp.port", ePort);
			
			//while uploading in to the server should comment this below line otherwise not working email sending in uat
	        props.put("mail.smtp.socketFactory.port", thePort);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			 if(ehost.equals("smtp.gmail.com")||ehost.equals("secure.emailsrvr.com")){
	        	 props.put("mail.smtp.socketFactory.port", ePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }else{
	        	 props.put("mail.smtp.port", ePort); 
	         }
	         else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * 
			
			
			
			userID = eUserID;
			password = ePassword;
			Session session =null;
			session=Session.getDefaultInstance(props, null);
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(userID, password);
			    }
			});
			
			UserBean aUserBean;
			String usernames="";
			aUserBean = (UserBean) request.getSession().getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				usernames = aUserBean.getFullName();
			}
			
		
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		//try {
				fromAddress = new InternetAddress(fromEmailAddress);
				toAddress =new InternetAddress(toEmailAddress);
			//	toAddress = new InternetAddress(itsTo);
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			
			int a=0;
			Address[] toArray;
			StringTokenizer to = new StringTokenizer(itsTo,",;"); 
			toArray = new InternetAddress[to.countTokens()];
			while(to.hasMoreTokens()) { 
			toArray[a] =new InternetAddress(to.nextToken()); 
			a++;
			} 
		message.setRecipients(RecipientType.TO, toArray);
		message.setRecipients(RecipientType.TO,toEmailAddress);
		
			if(ccAddress!=null && ccAddress!=""){
				int x=0;
				Address[] ccArray;
				StringTokenizer cc = new StringTokenizer(ccAddress, ",;"); 
				ccArray = new InternetAddress[cc.countTokens()];
				while(cc.hasMoreTokens()) { 
				ccArray[x] =new InternetAddress(cc.nextToken()); 
				x++;
				} 
				message.setRecipients(Message.RecipientType.CC, ccArray);
			}
			if(itsBCCAddr!=null && itsBCCAddr!=""){
				message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			}
			message.setSubject(eSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(eContent);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			if(writeFileName!=null){
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writeFileName);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(attachmentFileName);
					multiPart.addBodyPart(attachment);
			}
			message.setContent(multiPart);
			
			int port =Integer.valueOf(ePort);
			if(port==587){
				//transport.connect(thehost,Integer.valueOf(thePort), theUserID, thePassword);
				testsendornot=sendBulkEmailFrom587(eUserID,ePassword,ePort,ehost,fromEmailAddress,toEmailAddress,eSubject,eContent,writeFileName,attachmentFileName,request);
			}else{
			Transport transport = session.getTransport("smtp"); 
			System.out.println("thehost : " + ehost +" theUserID " +eUserID +" thePassword"+ePassword);
			transport.connect(ehost, eUserID, ePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			itsLogger.info("Mail sent ");
			testsendornot=true;
			}
			//return true;
		} catch (Exception e) {
			itsLogger.info("Mail Not sent ");
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}
		
		finally{
			return testsendornot;
		}*/
		
		boolean testsendornot=false;
		
		
		Properties props =null;
				props =new Properties();
	
			 String eUserID=null;
			 String ePassword=null;
			 String ePort=null;
			 String ehost=null;
			 String eSubject=null;
			 String eContent=null;
			 String writeFileName=null;
			 String attachmentFileName=null;
			 String fromEmailAddress=null;
			 String toEmailAddress=null;
		try{
			
			eUserID=eParam.geteUserID();
			ePassword=eParam.getePassword();
			ePort=eParam.getePort();
			ehost=eParam.getEhost();
			eSubject=eParam.geteSubject();
			eContent=eParam.geteContent();
			writeFileName=eParam.getWriteFileName();
			attachmentFileName=eParam.getFileName();
			fromEmailAddress=eParam.getFromAddress();
			toEmailAddress=eParam.getToAddress();
			
			
			
			props.put("mail.smtp.starttls.enable", "true"); 
			props.setProperty("mail.transport.protocol", "smtp"); 
			props.put("mail.debug", "false");					  
			props.put("mail.smtp.auth", "true"); 				  
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.host", ehost); 
			
			props.put("mail.smtp.user", eUserID); 
			props.put("mail.smtp.password", ePassword);
			props.put("mail.smtp.port", ePort);
			
			//while uploading in to the server should comment this below line otherwise not working email sending in uat
	      /*  props.put("mail.smtp.socketFactory.port", thePort);
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");*/
			 if(ehost.equals("smtp.gmail.com")||ehost.equals("secure.emailsrvr.com")){
	        	 props.put("mail.smtp.socketFactory.port", ePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }else{
	        	 props.put("mail.smtp.port", ePort);
	        	 
	         }
	         /*else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * */
			
			
			
			userID = eUserID;
			password = ePassword;
			Session session =null;
			session=Session.getDefaultInstance(props, null);
			/*Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(userID, password);
			    }
			});*/
			
			UserBean aUserBean;
			String usernames="";
			aUserBean = (UserBean) request.getSession().getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				usernames = aUserBean.getFullName();
			}
			
		
			MimeMessage message = new MimeMessage(session); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		//try {
				fromAddress = new InternetAddress(fromEmailAddress);
				toAddress =new InternetAddress(toEmailAddress);
				
			//	toAddress = new InternetAddress(itsTo);
			/*} catch (AddressException e) {	
				e.printStackTrace();
			}*/
			message.setFrom(fromAddress);
			
	/*		int a=0;
			Address[] toArray;
			StringTokenizer to = new StringTokenizer(itsTo,",;"); 
			toArray = new InternetAddress[to.countTokens()];
			while(to.hasMoreTokens()) { 
			toArray[a] =new InternetAddress(to.nextToken()); 
			a++;
			} 
		message.setRecipients(RecipientType.TO, toArray);*/
		message.setRecipients(RecipientType.TO,toEmailAddress);
	 
		//added by prasant #549 date 04/07/2016 for adding cc and Bcc
		
	System.out.println("------------------------------------------------------>"+eParam.getCc());
	List<String> ccs=eParam.getCc();
	InternetAddress ccArray = new InternetAddress();
	String recipientList ="";
	for(int j=0;j<ccs.size()-1;j++)
	{
	if(ccs.get(j)!=""&&ccs.get(j)!=null&&j!=4){
		if(ccs.get(j)!="")
		recipientList=recipientList+ccs.get(j)+",";
		}
	
	}
	System.out.println("recipientList---------------------------------------------------------------------->"+recipientList);
	
	
	
	String validmail="";
	String[] recipient = recipientList.split(",");
	int count=0;
	for(String valid:recipient)
		if(valid!="")
		{
		validmail=validmail+valid+",";	
		count++;
		}
	System.out.println("valid mail list---------------------------------------------------------------------->"+validmail);
	InternetAddress[] recipientAddress = new InternetAddress[count];
	 recipient = validmail.split(",");
	
	System.out.println("............................................................................................>>>>>>>>length:"+recipientAddress.length);
	int counter = 0;
	for (String recipient1 : recipient) {
		if(recipient1!="")
	    recipientAddress[counter] = new InternetAddress(recipient1.trim());
	    counter++;
	}
	
	
	
	
	
	
	
			if(eParam.getCc()!=null){
			//for(String emailID:eParam.getCc())
				for(int j=0;j<ccs.size();j++)
				{
				/*{
				if(ccs.get(j)!=""&&ccs.get(j)!=null&&j!=4){
					if(j==0){
					 System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$CCC:"+ccs.get(j));
			    ccArray=new InternetAddress((ccs.get(j)));
					}
				}*/
				
				 if(j==4)
					if(eParam.getCc().get(j).toString()!=""){
						Address Bcc=new InternetAddress((String)(ccs.get(j)).toString());
						message.setRecipient(Message.RecipientType.BCC, Bcc);
						System.out.println("*********************************");
						System.out.println("BCC:"+(ccs.get(j)).toString());
						System.out.println("*********************************");
						}
				}
				} 
	
			message.setRecipients(Message.RecipientType.CC, recipientAddress);
				
				
				
				
			
			/*if(itsBCCAddr!=null && itsBCCAddr!=""){
				message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			}*/
			message.setSubject(eSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(eContent);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			if(writeFileName!=null){
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writeFileName);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(attachmentFileName);
					multiPart.addBodyPart(attachment);
			}
			message.setContent(multiPart);
			
			int port =Integer.valueOf(ePort);
			if(port==587){
				//transport.connect(thehost,Integer.valueOf(thePort), theUserID, thePassword);
				testsendornot=sendBulkEmailFrom587(eUserID,ePassword,ePort,ehost,fromEmailAddress,toEmailAddress,eSubject,eContent,writeFileName,attachmentFileName,request);
			}else{
			Transport transport = session.getTransport("smtp"); 
			System.out.println("thehost : " + ehost +" theUserID " +eUserID +" thePassword"+ePassword);
			transport.connect(ehost, eUserID, ePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			boolean flag=new SendQuoteMail().validate(toEmailAddress);
			itsLogger.info("Mail sent ");
			if(flag)
			testsendornot=true;
			else
				testsendornot=false;	
			}
			//return true;
		}
		
		 
		catch (Exception e) {
			
			 testsendornot=false;
			 
			itsLogger.info("Mail Not sent ");
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
			
		}
		
		finally{
			return testsendornot;
		}
		
	}
	
	//added by prasant 
	public  boolean validate(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
	
	@SuppressWarnings("finally")
	public Boolean sendBulkEmailFrom587(final String eUserID, final String ePassword, String ePort, String ehost,
			String fromEmailAddress,String toEmailAddress,String eSubject,String eContent,String writeFileName,
			String attachmentFileName,HttpServletRequest request) throws IOException, MessagingException{
		//Properties props = System.getProperties();
		boolean testsendornot=false;
	//	Properties props =null;
		try{
			
			  Properties props = new Properties();
			    props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.starttls.enable", "true");
			    props.put("mail.smtp.host", ehost);
			    props.put("mail.smtp.port", ePort);
			    props.put("mail.debug", "false");	
			
			userID = eUserID;
			password = ePassword;
			Session session1 = Session.getInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication(eUserID, ePassword);
			    }
			});
			
			
			MimeMessage message = new MimeMessage(session1); 
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		//try {
				fromAddress = new InternetAddress(eUserID,fromEmailAddress);
				toAddress =new InternetAddress(toEmailAddress);
			//	toAddress = new InternetAddress(itsTo);
			/*} catch (AddressException e) {	
				e.printStackTrace();
			}*/
			message.setFrom(fromAddress);
			
	/*		int a=0;
			Address[] toArray;
			StringTokenizer to = new StringTokenizer(itsTo,",;"); 
			toArray = new InternetAddress[to.countTokens()];
			while(to.hasMoreTokens()) { 
			toArray[a] =new InternetAddress(to.nextToken()); 
			a++;
			} 
		message.setRecipients(RecipientType.TO, toArray);*/
		message.setRecipients(RecipientType.TO,toEmailAddress);
		
	/*		if(ccAddress!=null && ccAddress!=""){
				int x=0;
				Address[] ccArray;
				StringTokenizer cc = new StringTokenizer(ccAddress, ",;"); 
				ccArray = new InternetAddress[cc.countTokens()];
				while(cc.hasMoreTokens()) { 
				ccArray[x] =new InternetAddress(cc.nextToken()); 
				x++;
				} 
				message.setRecipients(Message.RecipientType.CC, ccArray);
			}
			if(itsBCCAddr!=null && itsBCCAddr!=""){
				message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			}*/
			message.setSubject(eSubject);
			Multipart multiPart = new MimeMultipart();
			BodyPart bodyContent = new MimeBodyPart();
			bodyContent.setText(eContent);
			bodyContent.setHeader("Content-Type","text/html");
			multiPart.addBodyPart(bodyContent);
			if(writeFileName!=null){
			String root = request.getRealPath("/");
            File path = new File(root + "/uploads");
            File attachFilename = new File(path + "/" + writeFileName);
            
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(new DataHandler(new FileDataSource(attachFilename)));
					attachment.setFileName(attachmentFileName);
					multiPart.addBodyPart(attachment);
			}
			message.setContent(multiPart);
			
			Transport transport = session1.getTransport("smtp"); 
			transport.connect(ehost, eUserID, ePassword); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			itsLogger.info("Mail sent ");
			testsendornot=true;
			//return true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new MessagingException(e.getMessage(), e);
		}finally{
			return testsendornot;
		}
		
	}
	
	
	
	
}