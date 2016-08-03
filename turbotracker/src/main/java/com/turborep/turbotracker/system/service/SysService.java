package com.turborep.turbotracker.system.service;

import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.job.dao.Joquotecolumn;
import com.turborep.turbotracker.job.dao.jocategory;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.system.dao.Sysassignment;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.user.dao.TsUserGroup;
import com.turborep.turbotracker.user.dao.TsUserGroupLink;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.exception.UserException;

public interface SysService {

	public Integer getSysSequenceNumber(String theTableName) throws Exception;
	public boolean updateRolodexCategories(Sysinfo sysinfo) throws UserException;
	public Sysinfo getRoldexCategories(int theUserinfoId) throws UserException;
	public Sysassignment getCustomerCategories() throws UserException;
	public boolean updateCustomerCategories(Sysassignment sysassign);
	public jocategory getJobCategories() throws UserException;
	public boolean updateJobCategories(jocategory thejocategory);
	public boolean updateTierPriceLevel(Sysinfo sysassign);
	public Joquotecolumn getjoQuoteColumn() throws UserException;
	public boolean updatejobColumnQuoteDefaults(Joquotecolumn theJoquotecolumn);
	public Integer getSequenceNumber(String theTableName) throws Exception;
	public Integer updateSequenceNumber(String theTableName) throws Exception;
	public ArrayList<Joquotecolumn> getjobQuotesColumn() throws UserException;
	
	public Integer getSysSequenceNumberwithSO(String theTableName) throws Exception;
	public boolean updateGroupDefaults(TsUserGroup thegroupDefaults);
	public TsUserGroup getgroupDefaults(Integer groupDefaultsID) throws JobException;
	public boolean updatesysprivilage(Sysprivilege theSysprivilege);
	public ArrayList<Sysprivilege> getGroupdefaultSysPrivileageLst(Sysprivilege theSysprivilege) throws UserException;
	public TsUserGroupLink getUserGroupLink(Integer userLoginId, Integer groupID) throws JobException;
	public Sysprivilege getSysPrivileageLst(Sysprivilege theSysprivilege,String Condition) throws UserException;
	public ArrayList<TsUserLogin> getallUserID();
	
}
