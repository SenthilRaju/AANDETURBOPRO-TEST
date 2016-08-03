package com.turborep.turbotracker.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.user.dao.AccessModuleConstant;
import com.turborep.turbotracker.user.dao.AccessProcedureConstant;
import com.turborep.turbotracker.user.dao.LoginBean;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsAccessModule;
import com.turborep.turbotracker.user.dao.TsAccessProcedure;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping(value ="/usercontroller",  method = RequestMethod.POST)
public class UserLoginController {

	protected static Logger itsLogger = Logger.getLogger(UserLoginController.class);
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String login(@RequestParam("userName") String theUserName,
										@RequestParam("password") String thePassword, ModelMap theModel,
										HttpServletRequest therequest, HttpSession session) throws IOException, MessagingException {
		String ipAddress = therequest.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {
			ipAddress = therequest.getRemoteAddr();
		}
		itsLogger.info(ipAddress);
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			itsLogger.info(ipAddress);
		} catch (UnknownHostException e) {
			sendTransactionException("<b>theUserName,thePassword:</b>"+theUserName+","+thePassword,"User",e,session,therequest);
			itsLogger.error(e.getMessage());
		}
		
		checkMemory();
/*		System.gc();
		itsLogger.info("\n============ After gc()============\n");
		checkMemory();*/
		
		System.out.println("Validating the User :" + theUserName);
		
		LoginBean aUserLogin = new LoginBean();
		aUserLogin.setUserName(theUserName);
		aUserLogin.setPassword(thePassword);
		UserBean aUser = null;TsAccessModule aTsAccessModule=null;TsAccessProcedure aTsAccessProcedure=null;
		
		
		//HttpSession session = UserLoginController.session();
//		String aPathInfo = theRequest.getRequestURI().substring(theRequest.getContextPath().length());
		try {
			aUser = itsUserService.login(aUserLogin);
			
			if (aUser == null) {
				theModel.addAttribute("error", "Invalid");
				return "redirect:login";
			} else if (aUser.getUserName() == null) {
				theModel.addAttribute("error", "Invalid Login");
				return "redirect:login";
			}
			session.setAttribute(SessionConstants.USER, aUser);
			session.setAttribute(SessionConstants.SALES_USERID, aUser.getUserId());
			session.setAttribute(SessionConstants.PROJECTS_USERID, aUser.getUserId());
			SysUserDefault asysusrdeflt=itsUserService.getSysUserDefault(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
			session.setAttribute(SessionConstants.SYSUSERDEFAULT, asysusrdeflt);
			
			TsUserSetting atsusersettings=itsUserService.getSingleUserSettingsDetails(1);
			session.setAttribute(SessionConstants.TSUSERSETTINGS, atsusersettings);
			
			TpUsage aTpusage=new TpUsage(new Date(), "Login Screen","Login","Info",aUser.getUserId(),"User Loggedin: "+theUserName);
			itsUserService.createTpUsage(aTpusage);
			
			
			/*List<TsAccessModule> TsAccessModulelst=new ArrayList<TsAccessModule>();
			
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("HOME"),"Home");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("JOB"),"Job");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("SALES"),"Sales");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("PROJECT"),"Project");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Company");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Inventory");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("BANKING"),"Banking");
			TsAccessModulelst.add(aTsAccessModule);
			aTsAccessModule=new TsAccessModule(AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Financial");
			TsAccessModulelst.add(aTsAccessModule);
			itsUserService.updatetsAccessModule(TsAccessModulelst);
						
			List<TsAccessProcedure> TsAccessProcedurelst=new ArrayList<TsAccessProcedure>();
			
			//Home
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("New_Job"),AccessModuleConstant.getConstantSysvariableId("HOME"),"New Job",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Quick_book"),AccessModuleConstant.getConstantSysvariableId("HOME"),"Quick book",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			
			//Job
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Main"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Main",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Quotes"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Quotes",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Submittal"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Submittal",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Credit"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Credit",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Release"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Release",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Financial"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Financial",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Journal"),AccessModuleConstant.getConstantSysvariableId("JOB"),"Journal",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			
			//Sales
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Sales"),AccessModuleConstant.getConstantSysvariableId("SALES"),"Sales",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Sales_Filter"),AccessModuleConstant.getConstantSysvariableId("SALES"),"Sales Filter",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			
			//Project
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Project"),AccessModuleConstant.getConstantSysvariableId("PROJECT"),"Project",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Project_Filter"),AccessModuleConstant.getConstantSysvariableId("PROJECT"),"Project Filter",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			
			//Company
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Customers"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Customers",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Payments"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Payments",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Statements"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Statements",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Sales_Order"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Sales Order",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Invoice"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Invoice",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Finance_Changes"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Finance Changes",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Tax_Adjustments"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Tax Adjustments",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("CreditDebit_Memos"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Credit/Debit Memos",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Sales_Order_Template"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Sales Order Template",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Vendors"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Vendors",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Purchase_Orders"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Purchase Orders",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Pay_Bills"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Pay Bills",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Invoice_Bills"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Invoice & Bills",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Employees"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Employees",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Commissions"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Commissions",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Rolodex"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Rolodex",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Users"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Users",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Settings"),AccessModuleConstant.getConstantSysvariableId("COMPANY"),"Settings",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			
			//Inventory
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Inventory"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Inventory",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Categories"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Categories",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Warehouses"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Warehouses",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Receive_Inventory"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Receive Inventory",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Transfer"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Transfer",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Order_Points"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Order Points",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Inventory_Value"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Inventory Value",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Count"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Count",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Adjustments"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Adjustments",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Transactions"),AccessModuleConstant.getConstantSysvariableId("INVENTORY"),"Transactions",null);
			
			//Banking
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Banking"),AccessModuleConstant.getConstantSysvariableId("BANKING"),"Banking",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Write_Checks"),AccessModuleConstant.getConstantSysvariableId("BANKING"),"Write Checks",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Reissue_Checks"),AccessModuleConstant.getConstantSysvariableId("BANKING"),"Reissue Checks",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Recouncile_Accounts"),AccessModuleConstant.getConstantSysvariableId("BANKING"),"Recouncile Accounts",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			
			//Financial
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Chart_Accounts"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Chart Accounts",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Divisions"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Divisions",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Tax_Territories"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Tax Territories",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("General_Ledger"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"General Ledger",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Journal_Entries"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Journal Entries",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("Accounting_Cycles"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Accounting Cycles",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("GL_Transactions"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"GL Transactions",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			aTsAccessProcedure=new TsAccessProcedure(AccessProcedureConstant.getConstantSysvariableId("OpenPeriod_PostingOnly"),AccessModuleConstant.getConstantSysvariableId("FINANCIAL"),"Open Period Posting Only",null);
			TsAccessProcedurelst.add(aTsAccessProcedure);
			itsUserService.updateTsAccessProcedure(TsAccessProcedurelst);*/
			
			return "redirect:home?success=true";
		} catch (UserException excep) {
			sendTransactionException("<b>theUserName,thePassword:</b>"+theUserName+","+thePassword,"User",excep,session,therequest);
			excep.printStackTrace();
			theModel.addAttribute("error", excep.getMessage());
			return "redirect:login";
		}
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(ModelMap theModel, HttpSession theSession) {
		itsLogger.debug("Invalidating the session");
		
		UserBean aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
			if(aUserBean!=null)
			{	
			TpUsage aTpusage=new TpUsage(new Date(), "Logout Tab","Logout","Info",aUserBean.getUserId(),"User Loggedout: "+aUserBean.getUserName());
			itsUserService.createTpUsage(aTpusage);
			System.gc();
			//itsSessionFactory.close();
			theSession.invalidate();
			}
		return "redirect:../";
	}
	
	public static HttpSession session() {
	    ServletRequestAttributes aAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return aAttr.getRequest().getSession(true); // true == allow create
	}	
	
	/**
	 * This method is used to check and print the usage of java heap memory.
	 */
	private void checkMemory(){
		Iterator<?> aIter = ManagementFactory.getMemoryPoolMXBeans().iterator();
		while (aIter.hasNext())
		{
		    MemoryPoolMXBean aItem = (MemoryPoolMXBean) aIter.next();
		    String aName = aItem.getName();
		    MemoryType aType = aItem.getType();
		    MemoryUsage aUsage = aItem.getUsage();
		    MemoryUsage aPeak = aItem.getPeakUsage();
		    MemoryUsage aCollections = aItem.getCollectionUsage();
		    itsLogger.info("\n \t TestName: " + aName + "\n \t Type: " + aType + "\n \t Usage: " + aUsage + "\n \t Peak: " + aPeak + "\n \t Collections: " + aCollections + "\n");
		}
	}
	
	@RequestMapping(value = "resetPassword", method = RequestMethod.GET)
	public @ResponseBody Boolean resetPassword(@RequestParam("mailid") String emailid,ModelMap theModel,
			HttpSession theSession,HttpServletRequest therequest) {
	    boolean mailId_presentornot=itsUserService.checkUserMailID(emailid);
	    if(mailId_presentornot){
	    	String aUserID="eric@aandespecialties.com";
			String aPassword="#turb0Pr0";
			String aPort="587";
			String ahost="smtp.office365.com";
			
			String subj="Reset Password - TurboPro";
			
			
	    char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		Random random = new SecureRandom();
	    char[] result = new char[6];
	    for (int i = 0; i < result.length; i++) {
	        // picks a random index out of character set > random character
	        int randomCharIndex = random.nextInt(characterSet.length);
	        result[i] = characterSet[randomCharIndex];
	    }
	    String newpassword= new String(result);
	    itsLogger.info("newpassword "+newpassword);
	    String thecontent="New Password  :"+newpassword;
	    SendQuoteMail sendMail = new SendQuoteMail(aUserID, emailid, subj, thecontent, null, null,null, null,null, null, null);
	    try {
			boolean sendvalue=sendMail.sendMailAttachment(aUserID, aPassword, aPort, ahost, therequest);
			boolean updatepswd=itsUserService.updatetsuserlogin(emailid,newpassword);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    } 
	    
	    
	    
	    return mailId_presentornot;
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
