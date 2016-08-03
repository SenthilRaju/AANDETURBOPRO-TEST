package com.turborep.turbotracker.settings.service;

import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedulemodel;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.settings.exception.SettingsException;

public interface ModelMaintenanceService {

	public List<Joschedtempcolumn> getColumnName(Integer theSchedModelID) throws SettingsException;

	public List<?> getModelMaintenaceList(Integer theJoSchedTempHeaderID,
			String theParameters,String theSearch) throws SettingsException;

	
	public List<?> getSchedDescription() throws SettingsException;

	public List<?> getProductCategory(String aKeyword) throws SettingsException;

	public void addScheduleModel(Joschedulemodel aJoschedulemodel) throws SettingsException;

	public void editScheduleModel(Joschedulemodel aJoschedulemodel) throws SettingsException;

	public void deleteScheduleModel(Integer theJoScheduleModelId) throws SettingsException;

	public List<?> getModelMaintenaceList(Integer theJoSchedTempHeaderID,
			String theParameters, int aFrom, int aTo,String theSearch) throws SettingsException;

	public ArrayList<AutoCompleteBean> getItemCode(String aKeyword) throws SettingsException;

	public void updateproductCategory(Integer aScheduleModelID,
			Integer aPrMasterId) throws SettingsException;

	public String getProductCategory(Integer joScheduleModelID) throws SettingsException;

	public String getManufacurersLink(Integer joSchedTempHeaderId) throws SettingsException;

}
