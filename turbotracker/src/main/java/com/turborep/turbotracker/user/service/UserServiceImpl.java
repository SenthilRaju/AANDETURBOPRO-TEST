package com.turborep.turbotracker.user.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.service.InventoryConstant;
import com.turborep.turbotracker.company.dao.ChSegments;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.exception.SysException;
import com.turborep.turbotracker.user.dao.JoWizardAppletData;
import com.turborep.turbotracker.user.dao.LoginBean;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsAccessModule;
import com.turborep.turbotracker.user.dao.TsAccessProcedure;
import com.turborep.turbotracker.user.dao.TsUserGroupLink;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;

@Service("userLoginService")
@Transactional
public class UserServiceImpl implements UserService {

protected static Logger itsLogger = Logger.getLogger(UserServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public UserBean login(LoginBean theUser) throws UserException {
		String aUserSelectQry = "SELECT tsUserGroup.UserGroupID, " +
										" tsUserLogin.UserLoginID, " +
										" tsUserLogin.LoginName, " +
										" tsUserLogin.FullName, " +
										" tsUserLogin.Initials," +
										" tsUserLogin.SystemAdministrator," +
										" tsUserLogin.EmailName," +
										" tsUserLogin.EmailAddr," +
										" tsUserLogin.LogOnName," +
										" tsUserLogin.LogOnPswd," +
										" tsUserLogin.UseAuthentication," +
										" tsUserLogin.CCName1," +
										" tsUserLogin.CCName2," +
										" tsUserLogin.CCName3," +
										" tsUserLogin.CCName4," +
										" tsUserLogin.CCAddr1," +
										" tsUserLogin.CCAddr2," +
										" tsUserLogin.CCAddr3," +
										" tsUserLogin.CCAddr4," +
										" tsUserLogin.BCCaddr," +
										" tsUserLogin.SMTPSvr," +
										" tsUserLogin.SMTPEmailActive," +
										" tsUserLogin.SMTPPort," +
										" tsUserLogin.userPerPage, " +
										" tsUserLogin.customerPerPage, " +
										" tsUserLogin.vendorPerPage, " +
										" tsUserLogin.employeePerPage, " +
										" tsUserLogin.rolodexPerPage," +
										" tsUserLogin.LoginPassword" +
										" FROM tsUserGroup LEFT JOIN tsUserGroupLink " +
										" ON tsUserGroup.UserGroupID=tsUserGroupLink.UserGroupID RIGHT JOIN tsUserLogin " +
										" ON tsUserGroupLink.UserLoginID=tsUserLogin.UserLoginID "  +
										" WHERE BINARY tsUserLogin.LoginName = '" + theUser.getUserName()  + "'" +
										" AND BINARY tsUserLogin.LoginPassword = '" + theUser.getPassword()+ "'" +
										" AND Inactive = 0";
		Session aSession = null;
		UserBean aUser = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUserSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			aUser = new UserBean();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				if (aObj[0] != null) {
					aUser.setUsergroupId((Integer) aObj[0]);
					if ((Integer) aObj[0] == 1) { 				//checking for sales rep
						aUser.setSalesRep(true);
					} else if ((Integer) aObj[0] == 2) { 		//checking for CSR
						aUser.setCSR(true);
					} else if ((Integer) aObj[0] == 3) { 		//checking for Employee
						aUser.setEmployee(true);
					} else if ((Integer) aObj[0] == 4) { 		//checking for Admin
						aUser.setAdmin(true);
						aUser.setRole("Administrator");
					} else if ((Integer) aObj[0] == 5) { 		//checking for Warehouse
						aUser.setWareHouse(true);
					}
				}
				aUser.setUserId((Integer) aObj[1]);
				aUser.setUserName((String) aObj[2]);
				aUser.setFullName((String) aObj[3]);
				aUser.setInitials((String) aObj[4]);
				aUser.setSystemAdministrator((Byte) aObj[5]);
				aUser.setEmailName((String) aObj[6]);
				aUser.setEmailAddr((String) aObj[7]);
				aUser.setLogOnName((String) aObj[8]);
				aUser.setLogOnPswd((String) aObj[9]);
				aUser.setUseAuthentication((Byte) aObj[10]);
				aUser.setCcname1((String) aObj[11]);
				aUser.setCcname2((String) aObj[12]);
				aUser.setCcname3((String) aObj[13]);
				aUser.setCcname4((String) aObj[14]);
				aUser.setCcaddr1((String) aObj[15]);
				aUser.setCcaddr2((String) aObj[16]);
				aUser.setCcaddr3((String) aObj[17]);
				aUser.setCcaddr4((String) aObj[18]);
				aUser.setBccaddr((String) aObj[19]);
				aUser.setSmtpsvr((String) aObj[20]);
				aUser.setSmtpemailActive((Byte) aObj[21]);
				aUser.setSmtpport((Integer) aObj[22]);
				aUser.setUserperpage((Integer) aObj[23]);
				aUser.setLoginpswd((String)aObj[28]);
			}
			itsLogger.info("=========================== User login info:  User '" + aUser.getUserName() + "' was logged in now.  Full name of the user is: '" + aUser.getFullName() + "' ========================");
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			UserException aUserException = new UserException("", excep);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aUserSelectQry = null;
		}
		return aUser;
	}

	@Override
	public List<TsUserLogin> getAllUserList() throws UserException {
		Session aSession = null;
		List<TsUserLogin> aQueryList = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery("FROM  TsUserLogin WHERE Initials IS NOT NULL AND Inactive = 0 AND LoginName != 'admin'");
			aQueryList = aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getUserAutoSuggestList(String theKeyword) throws UserException {
		String aCustomerTypeSelectQry = "SELECT UserLoginID, FullName, Initials FROM tsUserLogin WHERE FullName LIKE '%"+theKeyword+"%' AND LoginName != 'admin' AND Inactive=0 ORDER BY FullName ASC";
		Session aSession=null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try{
			AutoCompleteBean aUserbean = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aCustomerTypeSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[])aIterator.next();
				aUserbean.setId((Integer)aObj[0]);				/** UserLoginID */
				String FullName = (String)aObj[1];
				String Initials = (String)aObj[2];
				aUserbean.setValue(FullName);					/** UserName	*/
				aUserbean.setLabel(FullName + "("+Initials+")");
				aQueryList.add(aUserbean);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerTypeSelectQry = null;
		}
		return aQueryList;
	}

	public List<TsUserLogin> getUserList(int theFrom, int theTo, byte theUserActive) throws UserException {
		String userSelectQry = "";
		if(theUserActive == 0){
			userSelectQry = "SELECT * FROM tsUserLogin WHERE FullName IS NOT NULL AND Inactive = '"+theUserActive+"' AND LoginName != 'admin' ORDER BY FullName LIMIT "+theFrom+","+theTo+"";
		} else {
			userSelectQry = "SELECT * FROM tsUserLogin WHERE FullName IS NOT NULL AND LoginName != 'admin' ORDER BY FullName LIMIT "+theFrom+","+theTo+"";
		}
		Session aSession=null;
		List<TsUserLogin> aQueryList = new ArrayList<TsUserLogin>();
		try{
			TsUserLogin aUserList = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(userSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aUserList = new TsUserLogin();
				Object[] aObj = (Object[])aIterator.next();
				aUserList.setUserLoginId((Integer) aObj[0]);
				aUserList.setLoginName((String) aObj[1]);
				aUserList.setLoginPassword((String) aObj[2]);
				aUserList.setFullName((String) aObj[3]);
				aUserList.setInitials((String) aObj[4]);
				aUserList.setSystemAdministrator((Byte) aObj[5]);
				aUserList.setSalesRep((Byte) aObj[6]);
				aUserList.setEmployee0((Byte) aObj[7]);
				aUserList.setEmployee1((Byte) aObj[8]);
				aUserList.setEmployee2((Byte) aObj[9]);
				aUserList.setEmployee3((Byte) aObj[10]);
				aUserList.setEmployee4((Byte) aObj[11]);
				if((String) aObj[14] != null){
					aUserList.setEmailAddr((String) aObj[14]);
				}
				aUserList.setInactive((Byte) aObj[12]);
			//	aUserList.setLoginStatus((Integer) aObj[45]);
				aQueryList.add(aUserList);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			userSelectQry = null;
		}
		return aQueryList;
	}
		
	@Override
	public int getRecordCount(byte theUserActive) throws UserException {
		String aUserCountStr = "";
		if(theUserActive == 0){
			aUserCountStr = "SELECT COUNT(UserLoginID) AS count FROM tsUserLogin where FullName IS NOT NULL AND Inactive ='"+theUserActive+"' AND LoginName != 'admin'";
		} else {
			aUserCountStr = "SELECT COUNT(UserLoginID) AS count FROM tsUserLogin where FullName IS NOT NULL AND LoginName != 'admin'";
		}
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUserCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aUserCountStr=null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public TsUserLogin addUser(TsUserLogin theUserLogin) throws UserException {
		Session aUserSession = itsSessionFactory.openSession();
		Transaction aUserTransaction;
		TsUserLogin aLogin = null;
		try {
			aUserTransaction = aUserSession.beginTransaction();
			aUserTransaction.begin();
			aUserSession.save(theUserLogin);
			aUserTransaction.commit();
			aLogin = (TsUserLogin) aUserSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aUserSession.flush();
			aUserSession.close();
		}
		return aLogin;
	}

	@Override
	public Integer addUserDefaults(SysUserDefault theSysUserDefault) throws SysException {
		Session aUserSession = itsSessionFactory.openSession();
		Transaction aUserTransaction;
		Integer status = 0;
		SysUserDefault newSysUserDefault = new SysUserDefault();
		SysUserDefault aSysUserDefault = new SysUserDefault();
		try {
			aUserTransaction = aUserSession.beginTransaction();
			aUserTransaction.begin();
			newSysUserDefault = getSysUserDefault(theSysUserDefault.getUserLoginID());
			if(newSysUserDefault.getUserLoginID()==null){
				itsLogger.info("Inserting  SysUserDefault");
			status = (Integer)aUserSession.save(theSysUserDefault);
			}
			else{
				itsLogger.info("Now in Update Area :"+newSysUserDefault.getUserLoginID());
				aSysUserDefault = (SysUserDefault) aUserSession.get(SysUserDefault.class, newSysUserDefault.getSysUserDefaultID());
				aSysUserDefault.setCoDivisionID(theSysUserDefault.getCoDivisionID());
				aSysUserDefault.setWarehouseID(theSysUserDefault.getWarehouseID());
				aUserSession.update(aSysUserDefault);
			}
			aUserTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			
		} finally {
			aUserSession.flush();
			aUserSession.close();
		}
		return status;
	}
	@Override
	public SysUserDefault getSysUserDefault(Integer userId) throws SysException {
		String aQry = "FROM SysUserDefault WHERE UserLoginID="+userId;
		Session aSession = null;
		List<SysUserDefault> aQueryList = null;
		SysUserDefault aSysUserDefault =null;
		try {
			aSession = itsSessionFactory.openSession();
			Query query = aSession.createQuery(aQry);
			aQueryList = query.list();
			if(aQueryList.size() < 1){
				aSysUserDefault = new SysUserDefault();
				itsLogger.error("No Data is available Query is : \n" + aQry);
			}else{
				aSysUserDefault = aQueryList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new SysException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		return aSysUserDefault;
	}
	
	@Override
	public TsUserLogin getSingleUserDetails(Integer theUserID) throws UserException {
		Session aSession = null;
		TsUserLogin aUserLogin = null;
		try {
			aSession = itsSessionFactory.openSession();
			itsLogger.info("----->"+theUserID);
			aUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aUserLogin;
	}

	@Override
	public TsUserLogin updateUser(TsUserLogin theUserLogin) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		TsUserLogin aLogin = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
			aTsUserLogin.setLoginName(theUserLogin.getLoginName());
			aTsUserLogin.setLoginPassword(theUserLogin.getLoginPassword());
			aTsUserLogin.setInitials(theUserLogin.getInitials());
			aTsUserLogin.setFullName(theUserLogin.getFullName());
			aTsUserLogin.setSystemAdministrator(theUserLogin.getSystemAdministrator());
			aTsUserLogin.setInactive(theUserLogin.getInactive());
			aTsUserLogin.setEmployee0(theUserLogin.getEmployee0());
			aTsUserLogin.setEmployee1(theUserLogin.getEmployee1());
			aTsUserLogin.setEmployee2(theUserLogin.getEmployee2());
			aTsUserLogin.setEmployee3(theUserLogin.getEmployee3());
			aTsUserLogin.setEmployee4(theUserLogin.getEmployee4());
			aTsUserLogin.setUserLoginId(theUserLogin.getUserLoginId());
			aTsUserLogin.setUserzipcode(theUserLogin.getUserzipcode());
			aTsUserLogin.setUserperpage(theUserLogin.getUserperpage());
			aTsUserLogin.setCustomerperpage(theUserLogin.getCustomerperpage());
			aTsUserLogin.setVendorperpage(theUserLogin.getVendorperpage());
			aTsUserLogin.setEmployeeperpage(theUserLogin.getEmployeeperpage());
			aTsUserLogin.setRolodexperpage(theUserLogin.getRolodexperpage());
			if(theUserLogin.getAppletLocalUrl() != null)
				aTsUserLogin.setAppletLocalUrl(theUserLogin.getAppletLocalUrl());
			aSession.update(aTsUserLogin);
			transaction.commit();
			aLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLogin;
	}
	
	@Override
	public void updateJoWizardAppletData(String jobNumber, Integer userLoginId, String appletLocalUrl) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		JoWizardAppletData aLogin = null;
		String userSelectQry = "SELECT * FROM joWizardAppletData WHERE jobNumber = '"+jobNumber+"'";//userLoginId = "+userLoginId+" AND 
		try {
			int id = 0;
			Query aQuery = aSession.createSQLQuery(userSelectQry);
			List list = aQuery.list();
			if(list != null && list.size() > 0)
			{
				Iterator<?> aIterator = list.iterator();
				while(aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					id = (Integer) aObj[0];
				}
			}
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			JoWizardAppletData joWizardAppletData = null;
			if(id != 0 && id > 0)
			{
				joWizardAppletData = new JoWizardAppletData();
				joWizardAppletData = (JoWizardAppletData) aSession.get(JoWizardAppletData.class, id);
				joWizardAppletData.setJobNumber(jobNumber);
				joWizardAppletData.setUserLoginId(userLoginId);
				
				if(appletLocalUrl != null)
					joWizardAppletData.setAppletLocalUrl(appletLocalUrl);
				aSession.update(joWizardAppletData);
			}
			else
			{
				joWizardAppletData = new JoWizardAppletData();
				joWizardAppletData.setJobNumber(jobNumber);
				joWizardAppletData.setUserLoginId(userLoginId);
				
				if(appletLocalUrl != null)
					joWizardAppletData.setAppletLocalUrl(appletLocalUrl);
				aSession.save(joWizardAppletData);
			}
			transaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			userSelectQry = null;
		}
	}
	
	@Override
	public String getAppletUrl(String jobNumber, Integer userLoginId,Integer joMasterID) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		String userSelectQry = "SELECT * FROM joWizardAppletData WHERE joMasterID = '"+joMasterID+"'";//userLoginId = "+userLoginId+" AND 
		String returnString = "";
		try {
			int id = 0;
			Query aQuery = aSession.createSQLQuery(userSelectQry);
			List list = aQuery.list();
			if(list != null && list.size() > 0)
			{
				Iterator<?> aIterator = list.iterator();
				while(aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					id = (Integer) aObj[0];
					returnString =  (String) aObj[1];
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			userSelectQry = null;
		}
		return returnString;
	}

	@Override
	public ArrayList<TsUserGroupLink> getUserGroupDetails(Integer theUserID) throws UserException {
		 String userSelectQry = "SELECT UserGroupLinkID, UserLoginID, UserGroupID FROM tsUserGroupLink WHERE UserLoginID = "+theUserID+" ORDER BY UserGroupID";
			Session aSession=null;
			ArrayList<TsUserGroupLink> aQueryList = new ArrayList<TsUserGroupLink>();
			try{
				TsUserGroupLink aUserList = null;
				aSession=itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(userSelectQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				while(aIterator.hasNext()) {
					aUserList = new TsUserGroupLink();
					Object[] aObj = (Object[])aIterator.next();
					aUserList.setUserGroupLinkId((Integer) aObj[0]);
					aUserList.setUserLoginID((Integer) aObj[1]);
					aUserList.setUserGroupID((Integer) aObj[2]);
					aQueryList.add(aUserList);
				}
			} catch(Exception e) {
				itsLogger.error(e.getMessage(),e);
				UserException aUserException = new UserException(e.getCause().getMessage(), e);
				throw aUserException;
			} finally {
				aSession.flush();
				aSession.close();
				userSelectQry=null;
			}
		return aQueryList;
	}

	@Override
	public String getCheckUserName(String theUserName) throws UserException {
		String aUserName = "SELECT LoginName FROM tsUserLogin WHERE LoginName = '"+theUserName+"' AND LoginName != 'admin'";
		Session aSession=null;
		String aSalesRep = "";
		try{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aUserName);
			List<?> aList = aQuery.list();
			if(!aList.isEmpty())
				aSalesRep = (String) aList.get(0);
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aUserName = null;
		}
		return aSalesRep;
	}

	@Override
	public TsUserSetting companySetting(TsUserSetting theUserSetting) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		TsUserSetting aLoginSetting = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserSetting aTsUserLogin = (TsUserSetting) aSession.get(TsUserSetting.class, theUserSetting.getCompanyId());
			aTsUserLogin.setHeaderText(theUserSetting.getHeaderText());
			aTsUserLogin.setTerms(theUserSetting.getTerms());
			aTsUserLogin.setQuote(theUserSetting.getQuote());
			aTsUserLogin.setQuickQuote(theUserSetting.getQuickQuote());
			aTsUserLogin.setInvoices(theUserSetting.getInvoices());
			aTsUserLogin.setPurchaseOrders(theUserSetting.getPurchaseOrders());
			aTsUserLogin.setHeaderQuote(theUserSetting.getHeaderQuote());
			aTsUserLogin.setHeaderQuickQuote(theUserSetting.getHeaderQuickQuote());
			aTsUserLogin.setHeaderInvoices(theUserSetting.getHeaderInvoices());
			aTsUserLogin.setHeaderPurchaseOrders(theUserSetting.getHeaderPurchaseOrders());
			aTsUserLogin.setTermsQuote(theUserSetting.getTermsQuote());
			aTsUserLogin.setTermsQuickQuote(theUserSetting.getTermsQuickQuote());
			aTsUserLogin.setTermsInvoices(theUserSetting.getTermsInvoices());
			aTsUserLogin.setTermsPurchaseOrders(theUserSetting.getTermsPurchaseOrders());
			aTsUserLogin.setBillToAddress1(theUserSetting.getBillToAddress1());
			aTsUserLogin.setBillToAddress2(theUserSetting.getBillToAddress2());
			aTsUserLogin.setBillToCity(theUserSetting.getBillToCity());
			aTsUserLogin.setBillToState(theUserSetting.getBillToState());
			aTsUserLogin.setBillToZip(theUserSetting.getBillToZip());
			aTsUserLogin.setBillToDescription(theUserSetting.getBillToDescription());
			
			aTsUserLogin.setRemitToAddress1(theUserSetting.getRemitToAddress1());
			aTsUserLogin.setRemitToAddress2(theUserSetting.getRemitToAddress2());
			aTsUserLogin.setRemitToCity(theUserSetting.getRemitToCity());
			aTsUserLogin.setRemitToState(theUserSetting.getRemitToState());
			aTsUserLogin.setRemitToZip(theUserSetting.getRemitToZip());
			aTsUserLogin.setRemitToDescription(theUserSetting.getRemitToDescription());
			
			aTsUserLogin.setCompanyId(theUserSetting.getCompanyId());
			aSession.update(aTsUserLogin);
			transaction.commit();
			aLoginSetting = (TsUserSetting) aSession.get(TsUserSetting.class, theUserSetting.getCompanyId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLoginSetting;
	}
	
	
	public TsUserSetting saveQuoteFooternote(TsUserSetting theUserSetting) {
		Session aSession = itsSessionFactory.openSession();
		TsUserSetting aLoginSetting = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserSetting aTsUserLogin = (TsUserSetting) aSession.get(TsUserSetting.class, theUserSetting.getCompanyId());
			if(aTsUserLogin!=null)
				aTsUserLogin.setQuotesFooter(theUserSetting.getQuotesFooter()); 
			transaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLoginSetting;
	}
	
	
	public TsUserSetting saveSoFooterText(TsUserSetting theUserSetting) {
		Session aSession = itsSessionFactory.openSession();
		TsUserSetting aLoginSetting = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserSetting aTsUserLogin = (TsUserSetting) aSession.get(TsUserSetting.class, theUserSetting.getCompanyId());
			if(aTsUserLogin!=null)
				aTsUserLogin.setSoFooterText(theUserSetting.getSoFooterText()); 
			transaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLoginSetting;
	}
	
	@Override
	public List<TsUserSetting> getCompanySettings(int theUserId) throws UserException {
		itsLogger.debug("companySetting");
		String aCompantSettingList =  "SELECT CompanyID, LoginID, Quote, QuickQuote, Invoices, PurchaseOrders, HeaderText, Terms " +
										"FROM tsUserSetting " +
										"WHERE LoginID = " + theUserId;
			ArrayList<TsUserSetting> aQueryList = new ArrayList<TsUserSetting>();
			TsUserSetting aTsUserSetting = null;
			Session aSession = null;
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aCompantSettingList);
				Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aTsUserSetting = new TsUserSetting();
				Object[] aObj = (Object[])aIterator.next();
				aTsUserSetting.setCompanyId((Integer)aObj[0]);
				aTsUserSetting.setLoginId((Integer)aObj[1]);
				aTsUserSetting.setQuote((Byte)aObj[2]);
				aTsUserSetting.setQuickQuote((Byte)aObj[3]);
				aTsUserSetting.setInvoices((Byte)aObj[4]);
				aTsUserSetting.setPurchaseOrders((Byte)aObj[5]);
				aTsUserSetting.setHeaderText((String)aObj[6]);
				aTsUserSetting.setTerms((String)aObj[7]);
				aQueryList.add(aTsUserSetting);
			}
		} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
				UserException aUserException = new UserException(e.getCause().getMessage(), e);
				throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aCompantSettingList = null;
		}
		return  aQueryList;
	}

	@Override
	public TsUserSetting getSingleUserSettingsDetails(int theId) throws UserException {
		Session aSession = null;
		TsUserSetting aUserLoginSetting = null;
		try {
			aSession = itsSessionFactory.openSession();
			aUserLoginSetting = (TsUserSetting) aSession.get(TsUserSetting.class, theId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aUserLoginSetting;
	}

	@Override
	public TsUserLogin updateEmailProperties(TsUserLogin theUserLogin) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		TsUserLogin aLogin = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
			aTsUserLogin.setSmtpemailActive(theUserLogin.getSmtpemailActive());
			aTsUserLogin.setEmailName(theUserLogin.getEmailName());
			aTsUserLogin.setEmailAddr(theUserLogin.getEmailAddr());
			aTsUserLogin.setUseAuthentication(theUserLogin.getUseAuthentication());
			aTsUserLogin.setLogOnName(theUserLogin.getLogOnName());
			aTsUserLogin.setLogOnPswd(theUserLogin.getLogOnPswd());
			aTsUserLogin.setCcaddr1(theUserLogin.getCcaddr1());
			aTsUserLogin.setCcaddr2(theUserLogin.getCcaddr2());
			aTsUserLogin.setCcaddr3(theUserLogin.getCcaddr3());
			aTsUserLogin.setCcaddr4(theUserLogin.getCcaddr4());
			aTsUserLogin.setCcname1(theUserLogin.getCcname1());
			aTsUserLogin.setCcname2(theUserLogin.getCcname2());
			aTsUserLogin.setCcname3(theUserLogin.getCcname3());
			aTsUserLogin.setCcname4(theUserLogin.getCcname4());
			aTsUserLogin.setBccaddr(theUserLogin.getBccaddr());
			aTsUserLogin.setSmtpsvr(theUserLogin.getSmtpsvr());
			aTsUserLogin.setSmtpport(theUserLogin.getSmtpport());
			aTsUserLogin.setUserLoginId(theUserLogin.getUserLoginId());
			aSession.update(aTsUserLogin);
			transaction.commit();
			aLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLogin;
	}

	@Override
	public TsUserLogin getUserZipCode(int theUserId) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		TsUserLogin aLogin = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			aLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserId);
			transaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLogin;
	}

	@Override
	public TsUserLogin updatePerPage(TsUserLogin theUserLogin, String thePerPageID) throws UserException {
		itsLogger.debug("updatePerPage");
		Session aSession = itsSessionFactory.openSession();
		TsUserLogin aLogin = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
			if(thePerPageID.equalsIgnoreCase("user")){
				aTsUserLogin.setUserperpage(theUserLogin.getUserperpage());
			}else if(thePerPageID.equalsIgnoreCase("customer")){
				aTsUserLogin.setCustomerperpage(theUserLogin.getCustomerperpage());
			}else if(thePerPageID.equalsIgnoreCase("vendor")){
				aTsUserLogin.setVendorperpage(theUserLogin.getVendorperpage());
			}else if(thePerPageID.equalsIgnoreCase("employee")){
				aTsUserLogin.setEmployeeperpage(theUserLogin.getEmployeeperpage());
			}else if(thePerPageID.equalsIgnoreCase("rolodex")){
				aTsUserLogin.setRolodexperpage(theUserLogin.getRolodexperpage());
			}else if(thePerPageID.equalsIgnoreCase("chartsAccount")){
				aTsUserLogin.setChartsperpage(theUserLogin.getChartsperpage());
			}else if(thePerPageID.equalsIgnoreCase("bankingAccount")){
				aTsUserLogin.setBankingperpage(theUserLogin.getBankingperpage());
			}else if(thePerPageID.equalsIgnoreCase("inventory")){
				aTsUserLogin.setInventoryperpage(theUserLogin.getInventoryperpage());
			}
			aTsUserLogin.setUserLoginId(theUserLogin.getUserLoginId());
			aSession.update(aTsUserLogin);
			transaction.commit();
			aLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getMessage(),e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aLogin;
	}

	@Override
	public boolean updateUserDetails(TsUserLogin theUserLogin) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserLogin.getUserLoginId());
			aTsUserLogin.setActiveUserList(theUserLogin.getActiveUserList());
			aTsUserLogin.setActiveEmployeeList(theUserLogin.getActiveEmployeeList());
			transaction.commit();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		}finally {
			aSession.flush();
			aSession.close();
		}
		return false;		
	}
	
	@Override
	public boolean updateTsUserGroupLink(Boolean deleteBool, TsUserGroupLink theUserGroupLink) throws UserException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			
			if(!deleteBool)
			{
				itsLogger.info("inside !deleteBool condiition ");
				if(theUserGroupLink.getUserGroupLinkId() != null)
				{
					TsUserGroupLink aTsUserGroupLink = (TsUserGroupLink) aSession.get(TsUserGroupLink.class, theUserGroupLink.getUserGroupLinkId());
					aTsUserGroupLink.setUserGroupID(theUserGroupLink.getUserGroupID());
					aTsUserGroupLink.setUserLoginID(theUserGroupLink.getUserLoginID());
				}
				else
					aSession.save(theUserGroupLink);
			}
			else
				aSession.delete(theUserGroupLink);			
			transaction.commit();
		}catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		}finally {
			aSession.flush();
			aSession.close();
		}
		return false;		
	}
	
	@Override
	public TsUserLogin getUserDetails(Integer theUserId) throws UserException{
		Session aSession = null;
		TsUserLogin aTsUserLogin = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTsUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theUserId);
		}catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		}finally {
			aSession.flush();
			aSession.close();
		}
		return aTsUserLogin;
	}

	@Override
	public boolean updateShipToAddress(Prwarehouse thePrwareNorCross, Prwarehouse thePrwareBirmingham) throws UserException {
		Session aPrNorCrossSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = aPrNorCrossSession.beginTransaction();
			transaction.begin();
			Prwarehouse aPrNorCross = (Prwarehouse) aPrNorCrossSession.get(Prwarehouse.class, thePrwareNorCross.getPrWarehouseId());
			if(aPrNorCross != null){
				aPrNorCross.setDescription(thePrwareNorCross.getDescription());
				aPrNorCross.setAddress1(thePrwareNorCross.getAddress1());
				aPrNorCross.setAddress2(thePrwareNorCross.getAddress2());
				aPrNorCross.setCity(thePrwareNorCross.getCity());
				aPrNorCross.setState(thePrwareNorCross.getState());
				aPrNorCross.setZip(thePrwareNorCross.getZip());
				aPrNorCross.setPrWarehouseId(thePrwareNorCross.getPrWarehouseId());
				aPrNorCrossSession.update(aPrNorCross);
				transaction.commit();
			}
			transaction = aPrNorCrossSession.beginTransaction();
			transaction.begin();
			Prwarehouse aPrNorBirmi = (Prwarehouse) aPrNorCrossSession.get(Prwarehouse.class, thePrwareBirmingham.getPrWarehouseId());
			if(aPrNorBirmi != null){
				aPrNorBirmi.setDescription(thePrwareBirmingham.getDescription());
				aPrNorBirmi.setAddress1(thePrwareBirmingham.getAddress1());
				aPrNorBirmi.setAddress2(thePrwareBirmingham.getAddress2());
				aPrNorBirmi.setCity(thePrwareBirmingham.getCity());
				aPrNorBirmi.setState(thePrwareBirmingham.getState());
				aPrNorBirmi.setZip(thePrwareBirmingham.getZip());
				aPrNorBirmi.setPrWarehouseId(thePrwareBirmingham.getPrWarehouseId());
				aPrNorCrossSession.update(aPrNorBirmi);
				transaction.commit();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aPrNorCrossSession.flush();
			aPrNorCrossSession.close();
		}
		return true;
	}

	@Override
	public List<TsAccessModule> getAccessModuleDetails() throws UserException {
		Session aSession = null;
		List<TsAccessModule> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	Query query = aSession.createQuery("FROM  TsAccessModule");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			UserException aUserException = new UserException(excep.getCause().getMessage(), excep);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}

	@Override
	public boolean updateUserPermission(Sysprivilege theSysprivilege) throws UserException {
		Session aUserPermissionSession = itsSessionFactory.openSession();
		Transaction aUserPermissionTransaction;
		try {
			aUserPermissionTransaction = aUserPermissionSession.beginTransaction();
			aUserPermissionTransaction.begin();
			aUserPermissionSession.save(theSysprivilege);
			aUserPermissionTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aUserPermissionSession.flush();
			aUserPermissionSession.close();
		}
		return false;
	}

	@Override
	public List<Sysprivilege> getSysPrivilageDetails(Integer theUserId) throws UserException {
		String	aSysPriSelectQry = "SELECT * FROM sysPrivilege WHERE UserLoginID = "+theUserId;
		Session aSession=null;
		List<Sysprivilege> aQueryList = new ArrayList<Sysprivilege>();
		Sysprivilege aSysprivilege = null;
		Query aQuery = null;
		Iterator<?> aIterator = null;
		try{
			aSession=itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aSysprivilege = new Sysprivilege();
				Object[] aObj = (Object[])aIterator.next();
				aSysprivilege.setSysPrivilegeId((Integer) aObj[0]);
				aSysprivilege.setAccessProcedureId((Integer) aObj[1]);
				aSysprivilege.setUserLoginId((Integer) aObj[2]);
				aSysprivilege.setUserGroupId((Integer) aObj[3]);
				aSysprivilege.setSysPrivilegeId((Integer) aObj[4]);
				aQueryList.add(aSysprivilege);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
			aSysprivilege = null;
			aQuery = null;
			aIterator = null;
		}
		return aQueryList;
	}

	@Override
	public Integer getSysPrivilageID(Integer theUserId, Integer theUsergroupId, String theAccessProcedureID) throws UserException {
		Integer aPrivilageID = null;
		String aSelectQry = "SELECT sysPrivilegeID FROM sysPrivilege WHERE  AccessProcedureID = "+Integer.valueOf(theAccessProcedureID)+" AND UserLoginID = "+theUserId+" AND UserGroupID ="+theUsergroupId;
		Session aGetPrivilageSession = null;
		try {
			aGetPrivilageSession = itsSessionFactory.openSession();
			Query aQuery = aGetPrivilageSession.createSQLQuery(aSelectQry);
			List aList = aQuery.list();
			if(!aList.isEmpty()){
				aPrivilageID = (Integer)aList.get(0);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		}finally {
			aGetPrivilageSession.flush();
			aGetPrivilageSession.close();
			aSelectQry = null;
		}
		return aPrivilageID;
	}

	@Override
	public boolean deleteSystemPrivilage(Sysprivilege theSysprivilege) throws UserException {
		Session aSystemPrivilageSession = itsSessionFactory.openSession();
		Transaction aPrivilageTransaction;
		String aSystemPrivilageQry =null;
		try {
			aPrivilageTransaction = aSystemPrivilageSession.beginTransaction();
			aSystemPrivilageQry = "DELETE FROM sysPrivilege WHERE sysPrivilegeID = " + theSysprivilege.getSysPrivilegeId();
			aSystemPrivilageSession.createSQLQuery(aSystemPrivilageQry).executeUpdate();
			aPrivilageTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSystemPrivilageSession.flush();
			aSystemPrivilageSession.close();
			aSystemPrivilageQry = null;
		}
		return false;
	}

	@Override
	public List<Sysprivilege> getSysPrivilageInfo(String theUserName, Integer theUserId) throws UserException {
		String	aSysPriSelectQry = "SELECT * FROM sysPrivilege WHERE UserLoginID = "+theUserName;
		/*Commented By Velmurugan
		 * 6/3/2015
		 * User Permission tab checkbox not checked when login from another user
		 * BID #211
		 * */
				//+" AND UserGroupID ="+theUserId;
		Session aSession=null;;
		List<Sysprivilege> aQueryList = new ArrayList<Sysprivilege>();
		try{
			Sysprivilege aSysprivilege = null;
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aSysprivilege = new Sysprivilege();
				Object[] aObj = (Object[])aIterator.next();
				aSysprivilege.setSysPrivilegeId((Integer) aObj[0]);
				aSysprivilege.setAccessProcedureId((Integer) aObj[1]);
				aSysprivilege.setUserLoginId((Integer) aObj[2]);
				aSysprivilege.setUserGroupId((Integer) aObj[3]);
				aSysprivilege.setSysPrivilegeId((Integer) aObj[4]);
				aQueryList.add(aSysprivilege);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public Boolean uploadImage(TsUserSetting theUserSetting) throws UserException {
		Session aUploadImageSession = itsSessionFactory.openSession();
		Transaction aUploadTransaction;
		TsUserSetting aSetting = null;
		try {
			aUploadTransaction = aUploadImageSession.beginTransaction();
			aUploadTransaction.begin();
			aSetting = (TsUserSetting) aUploadImageSession.get(TsUserSetting.class, theUserSetting.getCompanyId());
			aSetting.setCompanyLogo(theUserSetting.getCompanyLogo());
			aSetting.setCompanyId(theUserSetting.getCompanyId());
			aUploadImageSession.update(aSetting);
			aUploadTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aUploadImageSession.flush();
			aUploadImageSession.close();
		}
		return true;
	}

	@Override
	public Boolean insertchSegments(ChSegments chsegments) throws UserException {
		Session aUserPermissionSession = itsSessionFactory.openSession();
		Transaction aUserPermissionTransaction;
		String aSystemPrivilageQry="";
		try {
			aUserPermissionTransaction = aUserPermissionSession.beginTransaction();
			aUserPermissionTransaction.begin();
			aUserPermissionSession.save(chsegments);
			aUserPermissionTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aUserPermissionSession.flush();
			aUserPermissionSession.close();
		}
		return false;
	}	
	
	
	@Override
	public Boolean updatechSegments(ChSegments chObj) throws UserException {
		Session aUserPermissionSession = itsSessionFactory.openSession();
		Transaction aUserPermissionTransaction;
		try {
			aUserPermissionTransaction = aUserPermissionSession.beginTransaction();
			aUserPermissionTransaction.begin();
			
			ChSegments aChSegments = (ChSegments) aUserPermissionSession.get(ChSegments.class, chObj.getSegmentid());
			aChSegments.setSegmentsName(chObj.getSegmentsName());
			aChSegments.setRequiredstatus(chObj.getRequiredstatus());
			aChSegments.setDigitsallowed(chObj.getDigitsallowed());
			aUserPermissionSession.update(aChSegments);
			aUserPermissionTransaction.commit();
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aUserPermissionSession.flush();
			aUserPermissionSession.close();
		}
		return false;
	}

	@Override
	public List<ChSegments> getchSegments() throws UserException {
		
		Session aSession = null;
		List<ChSegments> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	Query query = aSession.createQuery("FROM  ChSegments where requiredstatus =1");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			UserException aUserException = new UserException(excep.getCause().getMessage(), excep);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}
	
	@Override
	public List<ChSegments> getchSegments1() throws UserException {
		
		Session aSession = null;
		List<ChSegments> aQueryList = null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			// Create a Hibernate query (HQL)
		 	Query query = aSession.createQuery("FROM  ChSegments");
			// Retrieve all
			aQueryList = query.list();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			UserException aUserException = new UserException(excep.getCause().getMessage(), excep);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}

	
	@Override
	public ArrayList<Sysvariable> getInventorySettingsDetails(List<String> listString) throws UserException {
		
		Session aSession=null;
		String	aSysPriSelectQry = null;
		ArrayList<Sysvariable> aQueryList = new ArrayList<Sysvariable>();
		Query aQuery = null;
		Iterator<?> aIterator = null;
		try{
			Sysvariable aSysvariable = null;
			aSession=itsSessionFactory.openSession();
			
			for(String variablename:listString){
				
				Integer sysVariableId=InventoryConstant.getConstantSysvariableId(variablename);
				aSysPriSelectQry = "SELECT sysVariableID,ValueLong,ValueCurrency,ValueString,ValueDate,ValueMemo FROM sysVariable WHERE sysVariableID="+sysVariableId;
				aQuery = aSession.createSQLQuery(aSysPriSelectQry);
				aIterator = aQuery.list().iterator();
				if(aIterator.hasNext()) {
					aSysvariable = new Sysvariable();
					Object[] aObj = (Object[])aIterator.next();
					aSysvariable.setSysVariableId((Integer) aObj[0]);
					aSysvariable.setValueLong((Integer) aObj[1]);
					aSysvariable.setValueCurrency((BigDecimal) aObj[2]);
					aSysvariable.setValueString((String) aObj[3]);
					aSysvariable.setValueDate((Date) aObj[4]);
					aSysvariable.setValueMemo((String) aObj[5]);
					aQueryList.add(aSysvariable);
				}else{
					aSysvariable = new Sysvariable();
					aQueryList.add(aSysvariable);
				}
				
			}			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
			aQuery = null;
			aIterator = null;
		}
		return aQueryList;
	}
	@Override
	public SysUserDefault getSysUserDefault(int userid)  {
		String	aSysPriSelectQry = "SELECT SysUserDefaultID,UserLoginID,WarehouseID,coDivisionID FROM SysUserDefault WHERE UserLoginID="+userid;
		Session aSession=null;
		SysUserDefault aSysUserDefault = new SysUserDefault();;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			
			if(aIterator.hasNext()) {
				Object[] aObj = (Object[])aIterator.next();
				aSysUserDefault.setSysUserDefaultID((Integer) aObj[0]);
				aSysUserDefault.setUserLoginID((Integer) aObj[1]);
				aSysUserDefault.setWarehouseID((Integer) aObj[2]);
				aSysUserDefault.setCoDivisionID((Integer) aObj[3]);
				
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry = null;
		}
		return aSysUserDefault;
	}
	
	/*@Override
	public boolean updategrouppermission(grouppermission thegrouppermission)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			grouppermission agrouppermission = (grouppermission) SysinfoSession.get(grouppermission.class, thegrouppermission.getGrouppermissionID());
			if(agrouppermission != null){
				
			}else{
				transaction = SysinfoSession.beginTransaction();
				SysinfoSession.save(thegrouppermission);
				transaction.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}*/
	
	@Override
	public boolean updatetsAccessModule(List<TsAccessModule> aTsAccessModule)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			for(TsAccessModule theTsAccessModule:aTsAccessModule){
			TsAccessModule agrouppermission = (TsAccessModule) SysinfoSession.get(TsAccessModule.class, theTsAccessModule.getAccessModuleID());
			if(agrouppermission != null){
				
			}else{
				transaction = SysinfoSession.beginTransaction();
				SysinfoSession.save(theTsAccessModule);
				transaction.commit();
			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	
	@Override
	public boolean updateTsAccessProcedure(List<TsAccessProcedure> aTsAccessProcedure)  {
		Session SysinfoSession = itsSessionFactory.openSession();
		Transaction transaction = null;
		try {
			for(TsAccessProcedure theTsAccessProcedure:aTsAccessProcedure){
			TsAccessProcedure agrouppermission = (TsAccessProcedure) SysinfoSession.get(TsAccessProcedure.class, theTsAccessProcedure.getAccessProcedureID());
			if(agrouppermission != null){
				
			}else{
				transaction = SysinfoSession.beginTransaction();
				SysinfoSession.save(theTsAccessProcedure);
				transaction.commit();
			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			
		} finally {
			SysinfoSession.flush();
			SysinfoSession.close();
		}
		return true;
	}
	
	@Override
	public boolean createTpUsage(TpUsage aTpUsage) {
		Session aUserSession = itsSessionFactory.openSession();
		Transaction aUserTransaction;
		try {
			aUserTransaction = aUserSession.beginTransaction();
			aUserTransaction.begin();
			aUserSession.save(aTpUsage);
			aUserTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			
		} finally {
			aUserSession.flush();
			aUserSession.close();
		}
		return true;
	}
	@Override
	public boolean checkUserMailID(String MailID) {
		
		Session aSession = itsSessionFactory.openSession();
		String userSelectQry = "SELECT * FROM tsUserLogin WHERE EmailAddr='"+MailID+"' and Inactive='0'";
		Boolean returnValue=false;
		try {
			int id = 0;
			Query aQuery = aSession.createSQLQuery(userSelectQry);
			List list = aQuery.list();
			if(list != null && list.size() > 0)
			{
			    //SendQuoteMail sendMail = new SendQuoteMail(aUserID, MailID, subj, thecontent, aSSL_FACTORY, theJobNumber,null, thePONumber,null, theccaddress, null);
				returnValue=true;
				
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			userSelectQry = null;
		}
		return returnValue;
	}
	
	@Override
	public boolean updatetsuserlogin(String MailID,String newpswd) {
		
		Session aSession = itsSessionFactory.openSession();
		String userSelectQry = "update tsUserLogin set LoginPassword='"+newpswd+"' WHERE EmailAddr='"+MailID+"' and Inactive='0'";
		try {
			Query aQuery = aSession.createSQLQuery(userSelectQry);
			aQuery.executeUpdate();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
			userSelectQry = null;
		}
		return true;
	}
	
	@Override
	public boolean updatechangepassword(Integer theLoginID, String newpswd) {
		Session aSession = itsSessionFactory.openSession();
		TsUserLogin aLogin = null;
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserLogin aTsUserLogin = (TsUserLogin) aSession.get(TsUserLogin.class, theLoginID);
			aTsUserLogin.setLoginPassword(newpswd);
			aSession.update(aTsUserLogin);
			transaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	public Integer saveVendorProductDetails(TsUserSetting theUserSetting) {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction transaction = aSession.beginTransaction();
			transaction.begin();
			TsUserSetting aTsUserLogin = (TsUserSetting) aSession.get(TsUserSetting.class, theUserSetting.getCompanyId());
			if(aTsUserLogin!=null)
				aTsUserLogin.setVendorProductID(theUserSetting.getVendorProductID());
				aTsUserLogin.setVendorProductDeptID(theUserSetting.getVendorProductDeptID());
			transaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return 1;
	}

	@Override
	public Integer getTaxTerritorySettingsValue(Integer sysVariableId) {
		// TODO Auto-generated method stub
		
		 Integer taxTerritorySettingValue=null;
		Session aSession = null;
		Sysvariable Sysvariable = null;
		try{
			aSession = itsSessionFactory.openSession();
			Sysvariable = (Sysvariable) aSession.get(Sysvariable.class, sysVariableId);
			
			taxTerritorySettingValue=Sysvariable.getValueLong();
		}catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
		}finally {
			aSession.flush();
			aSession.close();
		}
		
		
		
		return taxTerritorySettingValue;
	}
	
}