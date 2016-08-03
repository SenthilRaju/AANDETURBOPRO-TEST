/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.user.service;

import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.company.dao.ChSegments;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.exception.SysException;
import com.turborep.turbotracker.user.dao.LoginBean;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsAccessModule;
import com.turborep.turbotracker.user.dao.TsAccessProcedure;
import com.turborep.turbotracker.user.dao.TsUserGroupLink;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
/**
 *  
 * @author SYSVINE\vish_pepala
 */
public interface UserService {

	public UserBean login(LoginBean theUser) throws UserException;
	
	public List<TsUserLogin> getAllUserList() throws UserException;
	
	public ArrayList<AutoCompleteBean> getUserAutoSuggestList(String keyword) throws UserException;
	
	public List<TsUserLogin> getUserList(int from, int to, byte aInActive) throws UserException;
	
	public int getRecordCount(byte aInActive) throws UserException;

	public TsUserLogin addUser(TsUserLogin aUserLogin) throws UserException;

	public TsUserLogin getSingleUserDetails(Integer theUserID) throws UserException;

	public TsUserLogin updateUser(TsUserLogin aUserLogin) throws UserException;

	public ArrayList<TsUserGroupLink> getUserGroupDetails(Integer theUserID) throws UserException;

	public String getCheckUserName(String theUserName) throws UserException;

	public TsUserSetting companySetting(TsUserSetting aUserSetting) throws UserException;

	public List<TsUserSetting> getCompanySettings(int userId) throws UserException;

	public TsUserSetting getSingleUserSettingsDetails(int i) throws UserException;

	public TsUserLogin updateEmailProperties(TsUserLogin aUserLogin) throws UserException;

	public TsUserLogin getUserZipCode(int aUserId) throws UserException;

	public TsUserLogin updatePerPage(TsUserLogin aUserLogin, String aUserID) throws UserException;

	public boolean updateUserDetails(TsUserLogin aUserLogin) throws UserException;

	/**
	 * Method is used to get User Details for Quote.
	 * @param aUserId
	 * @return {@link TsUserLogin}
	 * @throws UserException 
	 */
	public TsUserLogin getUserDetails(Integer theUserId) throws UserException;

	/**
	 * Method is used to Update ShipTo Address.
	 * @param aPrwareNorCross
	 * @param aPrwareBirmingham
	 * @throws UserException
	 */
	public boolean updateShipToAddress(Prwarehouse aPrwareNorCross, Prwarehouse aPrwareBirmingham) throws UserException;

	public List<TsAccessModule> getAccessModuleDetails() throws UserException;

	public boolean updateUserPermission(Sysprivilege aSysprivilege) throws UserException;

	public List<Sysprivilege> getSysPrivilageDetails(Integer userId) throws UserException;

	public Integer getSysPrivilageID(Integer theUserId, Integer theUsergroupId, String theHomeName) throws UserException;

	public boolean deleteSystemPrivilage(Sysprivilege theSysprivilege) throws UserException;

	public List<Sysprivilege> getSysPrivilageInfo(String theUserName, Integer theUserId) throws UserException;

	public Boolean uploadImage(TsUserSetting theUserSetting) throws UserException;

	public Integer addUserDefaults(SysUserDefault aSysUserDefault) throws SysException;
	
	public SysUserDefault getSysUserDefault(Integer userId) throws SysException;
	
	public Boolean insertchSegments(ChSegments chsegments) throws UserException;
	
	public Boolean updatechSegments(ChSegments chObj) throws UserException;
	
	public List<ChSegments> getchSegments1() throws UserException; 
	
	public List<ChSegments> getchSegments()throws UserException;
	
	public ArrayList<Sysvariable> getInventorySettingsDetails(List<String> listString) throws UserException;
	
	public void updateJoWizardAppletData(String jobNumber, Integer userLoginId, String appletLocalUrl) throws UserException;

	public String getAppletUrl(String jobNumber, Integer userLoginId,Integer joMasterID) throws UserException;

	public SysUserDefault getSysUserDefault(int userid);

	public boolean updatetsAccessModule(List<TsAccessModule> theTsAccessModule);

	public boolean updateTsAccessProcedure(List<TsAccessProcedure> theTsAccessProcedure);

	/*public boolean updategrouppermission(grouppermission thegrouppermission);*/
	
	public boolean updateTsUserGroupLink(Boolean deleteBool, TsUserGroupLink theUserLogin) throws UserException;

	public boolean createTpUsage(TpUsage aTpusage);

	public boolean checkUserMailID(String emailid);

	public boolean updatetsuserlogin(String emailid, String newpassword);

	public boolean updatechangepassword(Integer theLoginID, String newpswd);

	public TsUserSetting saveQuoteFooternote(TsUserSetting aUserSetting);
	
	public TsUserSetting saveSoFooterText(TsUserSetting aUserSetting);
	
	public Integer saveVendorProductDetails(TsUserSetting theUserSetting);

	public Integer getTaxTerritorySettingsValue(Integer sysVariableId);

}
