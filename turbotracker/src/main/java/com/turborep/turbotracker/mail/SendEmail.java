package com.turborep.turbotracker.mail;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turborep.turbotracker.Configure.Mailer;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
@RequestMapping({"/sendMailServer"})
public class SendEmail
{
  private ResourceBundle serverProperties;
  private Mailer mailerOBJ;
  protected static Logger itsLogger = Logger.getLogger(SendEmail.class);
  @Resource(name="vendorService")
  private VendorServiceInterface itsVendorService;
  @Resource(name="userLoginService")
  private UserService itsUserService;
  
  public void init(ServletConfig config)
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    this.mailerOBJ = new Mailer(this.serverProperties);
  }
  
  @RequestMapping(value={"/sendMail"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public void main(String[] args, @RequestParam(value="subject", required=false) String theMailSubject, @RequestParam(value="toAddress", required=false) String theToAddress, @RequestParam(value="emailName", required=false) String theEmailName, @RequestParam(value="emailAddr", required=false) String theEmailAddr, @RequestParam(value="logOnName", required=false) String theLogOnName, @RequestParam(value="logOnPassword", required=false) String theLogOnPassword, @RequestParam(value="CCAddr1", required=false) String theCCAddr1, @RequestParam(value="CCAddr2", required=false) String theCCAddr2, @RequestParam(value="CCAddr3", required=false) String theCCAddr3, @RequestParam(value="CCAddr4", required=false) String theCCAddr4, @RequestParam(value="CCName1", required=false) String theCCName1, @RequestParam(value="CCName2", required=false) String theCCName2, @RequestParam(value="CCName3", required=false) String theCCName3, @RequestParam(value="CCName4", required=false) String theCCName4, @RequestParam(value="BCCAddr", required=false) String theBCCAddr, @RequestParam(value="SMTPSer", required=false) String theSMTPSer, @RequestParam(value="SMTPPort", required=false) String theSMTPPort, @RequestParam(value="jobNumber", required=false) String theJobNumber, HttpServletRequest request, HttpSession session)
    throws MessagingException, IOException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    String aFromAddress = theEmailAddr.replaceAll("'", "");
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = theLogOnName.replaceAll("'", "");
    String aPassword = theLogOnPassword.replaceAll("'", "");
    String aPort = theSMTPPort.replaceAll("'", "");
    String ahost = theSMTPSer.replaceAll("'", "");
    String aToAddress = theToAddress.replaceAll("'", "");
    String aSubject = theMailSubject.replaceAll("'", "");
    String aCCAddr1 = theCCAddr1.replaceAll("'", "");
    String aCCAddr2 = theCCAddr2.replaceAll("'", "");
    String aCCAddr3 = theCCAddr3.replaceAll("'", "");
    String aCCAddr4 = theCCAddr4.replaceAll("'", "");
    String aCCName1 = theCCName1.replaceAll("'", "");
    String aCCName2 = theCCName2.replaceAll("'", "");
    String aCCName3 = theCCName3.replaceAll("'", "");
    String aCCName4 = theCCName4.replaceAll("'", "");
    String aBCCAddr = theBCCAddr.replaceAll("'", "");
    String aQuoteIdentify = "Quote.";
    String aPONumber = "";
    String aMessage = "The attached quote is in PDF format.  If you have trouble reading it a free reader can be download at http://www.adobe.com/\n\n";
    try
    {
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, aMessage, aSSL_FACTORY, theJobNumber, aCCAddr1, aCCAddr2, aCCAddr3, aCCAddr4, 
        aCCName1, aCCName2, aCCName3, aCCName4, aBCCAddr, aQuoteIdentify, aPONumber);
      sendMail.send(aUserID, aPassword, aPort, ahost, request);
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theMailSubject:</b>"+theMailSubject ,"SendEmail",e,session,request);
    }
  }
  
  @RequestMapping(value={"/sendPOMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public void mainPOMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="PONumber", required=false) String thePONumber, HttpSession session, HttpServletRequest request)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    String aToAddress = aRxcontact.getEmail();
    String aSubject = "Purchase Order # " + thePONumber + ".";
    String aMessage = "";
    String theJobNumber = "";
    String aCCAddr1 = aTsUserLogin.getCcaddr1();
    String aCCAddr2 = aTsUserLogin.getCcaddr2();
    String aCCAddr3 = aTsUserLogin.getCcaddr3();
    String aCCAddr4 = aTsUserLogin.getCcaddr4();
    String aCCName1 = aTsUserLogin.getCcname1();
    String aCCName2 = aTsUserLogin.getCcname1();
    String aCCName3 = aTsUserLogin.getCcname1();
    String aCCName4 = aTsUserLogin.getCcname1();
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String aQuoteIdentify = "";
    try
    {
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, aMessage, aSSL_FACTORY, theJobNumber, aCCAddr1, aCCAddr2, aCCAddr3, aCCAddr4, 
        aCCName1, aCCName2, aCCName3, aCCName4, aBCCAddr, aQuoteIdentify, thePONumber);
      sendMail.send(aUserID, aPassword, aPort, ahost, request);
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,request);
    }
  }
  
  @RequestMapping(value={"/sendSOMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public void mainSOMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="SONumber", required=false) String thePONumber, HttpSession session, HttpServletRequest request)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    String aToAddress = aRxcontact.getEmail();
    String aSubject = "Sales Order # ";
    if (thePONumber != null) {
      aSubject = "Sales Order # " + thePONumber + ".";
    }
    String aMessage = "";
    String theJobNumber = "";
    String aCCAddr1 = aTsUserLogin.getCcaddr1();
    String aCCAddr2 = aTsUserLogin.getCcaddr2();
    String aCCAddr3 = aTsUserLogin.getCcaddr3();
    String aCCAddr4 = aTsUserLogin.getCcaddr4();
    String aCCName1 = aTsUserLogin.getCcname1();
    String aCCName2 = aTsUserLogin.getCcname1();
    String aCCName3 = aTsUserLogin.getCcname1();
    String aCCName4 = aTsUserLogin.getCcname1();
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String aQuoteIdentify = "salesOrder";
    try
    {
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, aMessage, aSSL_FACTORY, theJobNumber, aCCAddr1, aCCAddr2, aCCAddr3, aCCAddr4, 
        aCCName1, aCCName2, aCCName3, aCCName4, aBCCAddr, aQuoteIdentify, thePONumber);
      sendMail.send(aUserID, aPassword, aPort, ahost, request);
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,request);
    }
  }
  
  @RequestMapping(value={"/sendPOGenMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public void mainPOGen(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="POPDF", required=false) String thePOPDFID, @RequestParam(value="PONumber", required=false) String thePONumber, HttpSession session, HttpServletRequest request)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    String aToAddress = aRxcontact.getEmail();
    String aSubject = "Purchase General Order # " + thePONumber + ".";
    String aMessage = "";
    String theJobNumber = "";
    String aCCAddr1 = aTsUserLogin.getCcaddr1();
    String aCCAddr2 = aTsUserLogin.getCcaddr2();
    String aCCAddr3 = aTsUserLogin.getCcaddr3();
    String aCCAddr4 = aTsUserLogin.getCcaddr4();
    String aCCName1 = aTsUserLogin.getCcname1();
    String aCCName2 = aTsUserLogin.getCcname1();
    String aCCName3 = aTsUserLogin.getCcname1();
    String aCCName4 = aTsUserLogin.getCcname1();
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String aQuoteIdentify = thePOPDFID;
    try
    {
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, aMessage, aSSL_FACTORY, theJobNumber, aCCAddr1, aCCAddr2, aCCAddr3, aCCAddr4, 
        aCCName1, aCCName2, aCCName3, aCCName4, aBCCAddr, aQuoteIdentify, thePONumber);
      sendMail.send(aUserID, aPassword, aPort, ahost, request);
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,request);
    }
  }
  
  @RequestMapping(value={"/sendPOAckMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public void mainPOAck(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="POPDF", required=false) String thePOPDFID, @RequestParam(value="PONumber", required=false) String thePONumber, HttpSession session, HttpServletRequest request)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    String aToAddress = aRxcontact.getEmail();
    String aSubject = "Purchase Acknowledgment Order # " + thePONumber + ".";
    String aMessage = "";
    String theJobNumber = "";
    String aCCAddr1 = aTsUserLogin.getCcaddr1();
    String aCCAddr2 = aTsUserLogin.getCcaddr2();
    String aCCAddr3 = aTsUserLogin.getCcaddr3();
    String aCCAddr4 = aTsUserLogin.getCcaddr4();
    String aCCName1 = aTsUserLogin.getCcname1();
    String aCCName2 = aTsUserLogin.getCcname1();
    String aCCName3 = aTsUserLogin.getCcname1();
    String aCCName4 = aTsUserLogin.getCcname1();
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String aQuoteIdentify = thePOPDFID;
    try
    {
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, aMessage, aSSL_FACTORY, theJobNumber, aCCAddr1, aCCAddr2, aCCAddr3, aCCAddr4, 
        aCCName1, aCCName2, aCCName3, aCCName4, aBCCAddr, aQuoteIdentify, thePONumber);
      sendMail.send(aUserID, aPassword, aPort, ahost, request);
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,request);
    }
  }
  
  @RequestMapping(value={"/sendCuInvoiceMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public String mainCuInvoiceMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="InvoiceNumber", required=false) String theInvoiceNumber, HttpSession session, HttpServletRequest request)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    String aToAddress = aRxcontact.getEmail();
    String aSubject = "Customer Invoice # ";
    if (theInvoiceNumber != null) {
      aSubject = "Customer Invoice # " + theInvoiceNumber + ".";
    }
    String aMessage = "";
    String theJobNumber = "";
    String aCCAddr1 = aTsUserLogin.getCcaddr1();
    String aCCAddr2 = aTsUserLogin.getCcaddr2();
    String aCCAddr3 = aTsUserLogin.getCcaddr3();
    String aCCAddr4 = aTsUserLogin.getCcaddr4();
    String aCCName1 = aTsUserLogin.getCcname1();
    String aCCName2 = aTsUserLogin.getCcname1();
    String aCCName3 = aTsUserLogin.getCcname1();
    String aCCName4 = aTsUserLogin.getCcname1();
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String aQuoteIdentify = "CustomerInvoice";
    try
    {
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, aMessage, aSSL_FACTORY, theJobNumber, aCCAddr1, aCCAddr2, aCCAddr3, aCCAddr4, 
        aCCName1, aCCName2, aCCName3, aCCName4, aBCCAddr, aQuoteIdentify, theInvoiceNumber);
      sendMail.send(aUserID, aPassword, aPort, ahost, request);
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,request);
    }
    return "success";
  }
  
  @RequestMapping(value={"/sendOutsitePurchaseOrderMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Boolean sendOutsitePurchaseOrderMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="PONumber", required=false) String thePONumber, @RequestParam(value="toAddress", required=false) String thetoAddress, @RequestParam(value="subject", required=false) String thesubject, @RequestParam(value="filename", required=false) String thefilename, @RequestParam(value="ccaddress", required=false) String ccaddress, @RequestParam(value="content", required=false) String content, HttpServletRequest therequest, HttpSession session)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    //String aFromAddress = aTsUserLogin.getSmtpport()==587?aTsUserLogin.getLogOnName():aTsUserLogin.getEmailAddr();
    String aToAddress = thetoAddress;
    itsLogger.info("aFromName"+aFromAddress);
    String aSubject = thesubject;
    String aMessage = "";
    String theJobNumber = "";
    



    String theccaddress = ccaddress;
    



    String aBCCAddr = aTsUserLogin.getBccaddr();
    String thecontent = content;
    String aQuoteIdentify = "";
    boolean returnvalue=false;
    try
    {
      String writepdfname = "PurchaseOrder_" + thePONumber + ".pdf";
      thefilename = "PurchaseOrder.pdf";
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, thecontent, aSSL_FACTORY, theJobNumber, aBCCAddr, thePONumber, thefilename, theccaddress, writepdfname);
      returnvalue=sendMail.sendMailAttachment(aUserID, aPassword, aPort, ahost, therequest);
      
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,therequest);
    }
    return returnvalue;
  }
  
  
  
  @RequestMapping(value={"/sendSalesOrderMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Boolean sendSalesOrderMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="PONumber", required=false) String thePONumber, @RequestParam(value="toAddress", required=false) String thetoAddress, @RequestParam(value="subject", required=false) String thesubject, @RequestParam(value="filename", required=false) String thefilename, @RequestParam(value="ccaddress", required=false) String ccaddress, @RequestParam(value="content", required=false) String content, HttpServletRequest therequest, HttpSession session)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    //String aFromAddress = aTsUserLogin.getSmtpport()==587?aTsUserLogin.getLogOnName():aTsUserLogin.getEmailAddr();
    String aToAddress = thetoAddress;
    itsLogger.info("aFromName"+aFromAddress);
    String aSubject = thesubject;
    String aMessage = "";
    String theJobNumber = "";
    String theccaddress = ccaddress;
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String thecontent = content;
    String aQuoteIdentify = "";
    boolean returnvalue=false;
    try
    {
      String writepdfname = "SalesOrder_" + thePONumber + ".pdf";
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, thecontent, aSSL_FACTORY, theJobNumber, aBCCAddr, thePONumber, thefilename, theccaddress, writepdfname);
       returnvalue=sendMail.sendMailAttachment(aUserID, aPassword, aPort, ahost, therequest);
      
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,therequest);
    }
    return returnvalue;
  }
  
  
  
  @RequestMapping(value={"/sendsamplemail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public void sendsamplemail(@RequestParam(value="from", required=false) String from, @RequestParam(value="user", required=false) String user, @RequestParam(value="pwd", required=false) String pwd, @RequestParam(value="to", required=false) String to, @RequestParam(value="host", required=false) String host, @RequestParam(value="port", required=false) String port, HttpServletResponse theresponse, HttpServletRequest therequest, HttpSession thesession)
    throws IOException, MessagingException
  {
	  PrintWriter out = null;
    out = theresponse.getWriter();
    
    Properties props = System.getProperties();

    final String userID = user;
    final String password = pwd;
    
    String itsFrom = from;
    String thehost = host;
    String thePort = port;
    
    String itsTo = to;
    try
    {
      URL attachFile = null;

      props.setProperty("mail.transport.protocol", "smtp");
      props.setProperty("mail.smtp.host", thehost);
      
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.protocol", "smtp");
      
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.port", thePort);
      props.put("mail.smtp.socketFactory.port", thePort);
      props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      props.put("mail.smtp.socketFactory.fallback", "false");
      props.setProperty("mail.smtp.quitwait", "false");
      Session session = Session.getDefaultInstance(props, new Authenticator()
      {
        protected PasswordAuthentication getPasswordAuthentication()
        {
          return new PasswordAuthentication(userID, password);
        }
      });
      MimeMessage message = new MimeMessage(session);
      
      InternetAddress fromAddress = null;
      InternetAddress toAddress = null;
      try
      {
        fromAddress = new InternetAddress(itsFrom);
        toAddress = new InternetAddress(itsTo);
      }
      catch (AddressException e)
      {
        e.printStackTrace();
      }
      message.setFrom(fromAddress);
    //  message.setRecipient(Message.RecipientType.TO, toAddress);

      message.setSubject("TurbotestEmail");
      message.setText("<b>Hello ThereM</b>", "utf-8", "html");
      Transport transport = session.getTransport("smtp");
      transport.connect(thehost, userID, password);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
      
      out.print("Mail sent sucessfully\nFrom:" + fromAddress + "\n" + "To:" + toAddress + "\n" + 
        "user:" + userID + "\n" + "pwd:" + password + "\n" + 
        "host:" + thehost + "\n" + "port:" + thePort);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      out.print("Errror Pls Check Your Details:\nFrom:" + itsFrom + "\n" + "To:" + itsTo + "\n" + 
        "user:" + userID + "\n" + "pwd:" + password + "\n" + 
        "host:" + thehost + "\n" + "port:" + thePort + "\n\nDescription:" + e.getMessage());
      sendTransactionException("<b>from:</b>"+from ,"SendEmail",e,thesession,therequest);
    }
    finally{
    	out.close();
    }
  }
  
  @RequestMapping(value={"/sendOutsiteQuotesMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Boolean sendOutsiteQuotesMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="jobnumber", required=false) String jobnumber, @RequestParam(value="toAddress", required=false) String thetoAddress, @RequestParam(value="subject", required=false) String thesubject, @RequestParam(value="filename", required=false) String thefilename, @RequestParam(value="ccaddress", required=false) String ccaddress, @RequestParam(value="content", required=false) String content, HttpServletRequest therequest, HttpSession session)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
   // String aFromAddress = aTsUserLogin.getSmtpport()==587?aTsUserLogin.getLogOnName():aTsUserLogin.getEmailAddr();
    String aToAddress = thetoAddress;
    itsLogger.info("aFromName"+aFromAddress);
    String aSubject = thesubject;
    String aMessage = "";
    String theJobNumber = "";
    



    String theccaddress = ccaddress;
    


    boolean returnvalue=false;
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String thecontent = content;
    String aQuoteIdentify = "";
    String writepdfname = "Quotes#" + jobnumber.trim() + ".pdf";
    //thefilename = "Quote_" + jobnumber.trim() + ".pdf";
    try
    {
      String filename = "";
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, thecontent, aSSL_FACTORY, theJobNumber, aBCCAddr, jobnumber, thefilename, theccaddress, writepdfname);
      returnvalue=sendMail.sendMailAttachment(aUserID, aPassword, aPort, ahost, therequest);
      return returnvalue;
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,therequest);
    }
    return returnvalue;
  }
  
 
  
  @RequestMapping(value={"/sendCustomerInvoiceMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Boolean sendCustomerInvoiceMail(String[] args, @RequestParam(value="contactID", required=false) Integer theContactID, @RequestParam(value="PONumber", required=false) String thePONumber, @RequestParam(value="toAddress", required=false) String thetoAddress, @RequestParam(value="subject", required=false) String thesubject, @RequestParam(value="filename", required=false) String thefilename, @RequestParam(value="ccaddress", required=false) String ccaddress, @RequestParam(value="content", required=false) String content, HttpServletRequest therequest, HttpSession session)
    throws MessagingException, IOException, UserException, VendorException
  {
    this.serverProperties = ResourceBundle.getBundle("com.turborep.turbotracker.Configure.Config");
    Rxcontact aRxcontact = this.itsVendorService.getContactDetails(theContactID);
    
    UserBean aUserBean = (UserBean)session.getAttribute("user");
    TsUserLogin aTsUserLogin = this.itsUserService.getSingleUserDetails(Integer.valueOf(aUserBean.getUserId()));
    String aFromAddress = aTsUserLogin.getEmailAddr();
    String aSSL_FACTORY = this.serverProperties.getString("mail.SSL_FACTORY");
    String aUserID = aTsUserLogin.getLogOnName();
    String aPassword = aTsUserLogin.getLogOnPswd();
    String aPort = aTsUserLogin.getSmtpport().toString();
    String ahost = aTsUserLogin.getSmtpsvr();
    //String aFromAddress = aTsUserLogin.getSmtpport()==587?aTsUserLogin.getLogOnName():aTsUserLogin.getEmailAddr();
    itsLogger.info("aFromName"+aFromAddress);
    String aToAddress = thetoAddress;
    
    String aSubject = thesubject;
    String aMessage = "";
    String theJobNumber = "";
    



    String theccaddress = ccaddress;
   
    itsLogger.info("BCCAdress::"+aTsUserLogin.getBccaddr());
    String aBCCAddr = aTsUserLogin.getBccaddr();
    String thecontent = content;
    String aQuoteIdentify = "";
    boolean returnvalue=false;
    try
    {
      String writepdfname = "CustomerInvoice_" + thePONumber + ".pdf";
      thefilename = "CustomerInvoice.pdf";
      SendQuoteMail sendMail = new SendQuoteMail(aFromAddress, aToAddress, aSubject, thecontent, aSSL_FACTORY, theJobNumber, aBCCAddr, thePONumber, thefilename, theccaddress, writepdfname);
      returnvalue=sendMail.sendMailAttachment(aUserID, aPassword, aPort, ahost, therequest);
      
    }
    catch (Exception e)
    {
      itsLogger.error(e.getMessage(), e);
      sendTransactionException("<b>theContactID:</b>"+theContactID ,"SendEmail",e,session,therequest);
    }
    return returnvalue;
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
