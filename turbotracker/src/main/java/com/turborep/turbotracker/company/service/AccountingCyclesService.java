package com.turborep.turbotracker.company.service;

import java.util.Date;
import java.util.List;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cobalance;
import com.turborep.turbotracker.company.dao.Cofiscalperiod;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.system.dao.Sysinfo;


public interface AccountingCyclesService {

	public Sysinfo getSysinfo() throws CompanyException;

	public Cofiscalyear getCurrentYear(Integer currentFiscalYearId) throws CompanyException;

	public Cofiscalperiod getCurrentPeriod(Integer currentPeriodId) throws CompanyException;

	public List<Cofiscalperiod> getCurrentYearList(Integer currentFiscalYearId) throws CompanyException;
	
	public List<Cofiscalyear> getfiscalYearList() throws CompanyException;

	public void updateSiteInfo(Integer sysInfoId, 
			Integer reversePeriodId) throws CompanyException;

	public Integer getYearId(String yearValue) throws CompanyException;

	public Integer getPeriodId(Date nextDate, Integer yearId) throws CompanyException;

	public Integer createYear(Date nextYearFrom, Date nextYearTo, String year,String description) throws CompanyException;

	public void createPeriodId(Date nextPeriodFrom, Date nextPeriodTo,
			Integer yearId,boolean flag,Integer period) throws CompanyException; //Added one arg by leo
	
	public void updatePeriodId(Date nextPeriodFrom, Date nextPeriodTo,
			Integer yearId,boolean flag,Integer period) throws CompanyException;

	public void updateSiteInfo(Integer sysInfoId, Integer periodId,
			Integer yearId) throws CompanyException; 
	
	
	/**
	 * Created by: leo
	 * Dated: 3/6/2015
	 * 
	 * */
	
	public void savecoFiscalYear(String year) throws CompanyException; 
	
	public Integer openorclosePeriodIdbasedonperiod(Integer period ,Integer yearid,Boolean opStatus) throws CompanyException;
	
	public List<?> getActivitycount(Integer year)throws CompanyException;
	
	public void updatecoFiscalYearDetails(Integer year,String desc)throws CompanyException;
	
	public void updatesysInfoDetailsinitially()throws CompanyException;
	
	public Cofiscalperiod getAllOpenPeriods(Date dateto)throws CompanyException;
	
	public Cofiscalperiod getCurrentOpenPeriods(Date dateto,Integer yearID,Integer periodID)throws CompanyException;
	
	public Date getOpenCofiscalperiod()throws CompanyException;
	
	public boolean getperiodAvailableStatus(Integer period,Integer yearID)throws CompanyException;
	
	public Integer getperiodAvailable(Integer period,Integer yearID)throws CompanyException;
	
	public List<?> getMostRecentPeriods()throws CompanyException;
	
	public Date getCurrentServerDate()throws CompanyException;
	
	public List<?> getAllfiscalPeriod()throws CompanyException;
	
	public Date getopenPeriods() throws CompanyException;
	
	public void updateAllperiosstatusclosed(Integer yearId) throws CompanyException;
	
	public Cofiscalperiod getPeriodDetailswithyearid(Integer yearId)throws CompanyException;
	
	public boolean updateCurrentYeartoopen(Integer currentFiscalYearId) throws CompanyException;
	
	public boolean getCoBalanceValues(Integer cofiscalPeriodID)throws CompanyException ;
	
	public List<Cofiscalperiod> getcofiperiodvalues(Integer closeperiodid,Integer currentperiodid)throws CompanyException;
}
