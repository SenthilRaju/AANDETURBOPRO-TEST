package com.turborep.turbotracker.mail;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class sendmailexample {
	
	
	//Checked working fine
	  final static String userID = "kamal.nazar2014@gmail.com";
      final static String password ="Passw0rd@0123" ;
      final static String itsFrom = "kamal.nazar2014@gmail.com";
      final static String thehost = "smtp.gmail.com";
      final static String thePort = "465";
      final static String itsTo = "vel.123.567.89@mail.com";
      
	
	//checked working fine
//	final static String userID = "eric@aandespecialties.com";
//    final static String password ="#turb0Pr0" ;
//    final static String itsFrom = "eric@aandespecialties.com";
//    final static String thehost = "smtp.office365.com";
//    final static String thePort = "587";
//    final static String itsTo = "velsubha@gmail.com";
    
    //not working authenticate fail
//    final static String userID = "ebache@1hvac.com";
//    final static String password ="Falcons7" ;
//    final static String itsFrom = "ebache@1hvac.com";
//    final static String thehost = "smtp.office365.com";
//    final static String thePort = "587";
//    final static String itsTo = "velsubha@gmail.com";
    
	/* Email: Karen@1hvac.com
	 Angie@1hvac.com
	 Emily@1hvac.com

	 Logins: Kholthaus
	 AHemmert
	 Eklosterman

	 Passwords for all:
	 Sammy
*/
//	Checked working fine
//	final static String userID = "Eklosterman@1hvac.com";
//    final static String password ="Sammy" ;
//    final static String itsFrom = "Emily@1hvac.com";
//    final static String thehost = "smtp.office365.com";
//    final static String thePort = "587";
//    final static String itsTo = "velsubha@gmail.com";

	//Checked Working fine
//	final static String userID = "AHemmert@1hvac.com";
//    final static String password ="Sammy" ;
//    final static String itsFrom = "angie@1hvac.com";
//    final static String thehost = "smtp.office365.com";
//    final static String thePort = "587";
//    final static String itsTo = "velsubha@gmail.com";
   
	//checked Working 
//	 	final static String userID = "Kholthaus@1hvac.com";
//	    final static String password ="Sammy" ;
//	    final static String itsFrom = "karen@1hvac.com";
//	    final static String thehost = "smtp.office365.com";
//	    final static String thePort = "587";
//	    final static String itsTo = "velsubha@gmail.com";
	   
//    
    
    
    
    
    //Checked not working PKIX path validation failed
//	final static String userID = "eric@aespecialties.comcastbiz.net";
//    final static String password ="ErBa0076" ;
//    final static String itsFrom = "bbache@bacheco.com";
//    final static String thehost = "mail.bacheco.com";
//    final static String thePort = "587";
//    final static String itsTo = "velsubha@gmail.com";

    
    //Checked working
//	final static String userID = "ebache@theunderwoodcompany.com";
//    final static String password ="UnderwoodEB123" ;
//    final static String itsFrom = "ebache@theunderwoodcompany.com";
//    final static String thehost = "secure.emailsrvr.com";
//    final static String thePort = "465";
//    final static String itsTo = "velsubha@gmail.com";

    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties props = System.getProperties(); 
		try{
			URL attachFile = null;

			
			
			  props.setProperty("mail.transport.protocol", "smtp");
	          props.setProperty("mail.smtp.host", thehost);
	         // props.put("mail.smtp.starttls.enable", "true");
	         // props.put("mail.smtp.protocol", "smtp");
	          //props.put("mail.debug", "true");
	          props.put("mail.smtp.auth", "true");
	        //  props.put("mail.smtp.port", thePort);
	         // props.put("mail.smtp.socketFactory.fallback", "false");
	         // props.setProperty("mail.smtp.quitwait", "false");
	          props.put("mail.smtp.sendpartial", "false");
//	          props.put("mail.smtp.reportsuccess", "true");
			
	         //if ericaendspecialties it should comment while using gmailid it should uncomment
	          
	          
	         if(thehost.equals("smtp.gmail.com")||thehost.equals("secure.emailsrvr.com")){
	        	 props.put("mail.smtp.socketFactory.port", thePort);
		         props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	         }
	         /*else
	          * smtp.office365.com
	          * smtp.emailsrvr.com
	          * */
			
	         
			
			
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session);
			//session.setDebug(true);
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
		try {
				fromAddress = new InternetAddress(itsFrom);
				toAddress = new InternetAddress(itsTo);
			} catch (AddressException e) {	
				e.printStackTrace();
			}
			message.setFrom(fromAddress);
			message.setRecipient(RecipientType.TO, toAddress);
			//message.setRecipients(Message.RecipientType.CC, ccAddress);
			//message.setRecipients(Message.RecipientType.BCC, itsBCCAddr);
			message.setSubject("TurbotestEmail");
			message.setText("<b>test mail</b>", "utf-8", "html");  
			Transport transport = session.getTransport("smtp"); 
			transport.connect(thehost, userID, password); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			System.out.println("Mail sent from test mail java");
		} catch (AddressException e) {

		e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
