package com.turborep.turbotracker.settings.service;

import java.util.List;

import com.turborep.turbotracker.job.dao.Joschedaccessory;
import com.turborep.turbotracker.job.dao.Joschedtempcolumn;
import com.turborep.turbotracker.job.dao.Joschedtempcolumntype;
import com.turborep.turbotracker.job.dao.Joschedtempheader;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.settings.exception.SettingsException;

public interface ShedTempService {

	public List<Joschedtempheader> getSchedDescription() throws SettingsException;

	public List<AutoCompleteBean> getManufacturer(String aName) throws SettingsException;

	public List<Joschedtempcolumn> getScheduleDetails(int theschedTempHeaderID)throws SettingsException;

	public List<Joschedtempcolumn> getScheduleDetails(int aFrom, int aTo, Integer theschedTempHeaderID) throws SettingsException;

	public List<Joschedtempcolumntype> getScheduleType() throws SettingsException;

	public void deleteJoSchedTempColumn(Integer theJoSchedTempColumnId) throws SettingsException;

	public void editJoSchedTempColumn(Integer theJoSchedTempColumnId,
			String theDisplayText, Integer theDisplayWidth,
			String thePrintText, Integer thePrintWidth, Boolean theInactive,
			Boolean theCopyDefaults, Integer theImportToOrder,Integer theTypeId) throws SettingsException;

	public void addJoSchedTempColumn(Joschedtempcolumn theJoschedtempcolumn) throws SettingsException;

	public int getScheduleCount(Integer theHeaderId) throws SettingsException;

	public void addTemplateDescription(Joschedtempheader theJoschedtempheader) throws SettingsException;

	public boolean checkProduct(String aCode) throws SettingsException;

	public Integer getHeaderId(String aCode) throws SettingsException;

	public void saveJoschedtempcolumn(Joschedtempcolumn aJoschedtemp) throws SettingsException;

	public List<Joschedaccessory> getAccessories(Integer aJoSchedTempColumnId) throws SettingsException;

	public void deleteAccessories(Integer theJoSchedAccessoryId) throws SettingsException;

	public void editAccessories(Integer theJoSchedAccessoryId, String theCode,
			String theDescription) throws SettingsException;

	public void saveAccessories(Joschedaccessory theJoschedaccessory) throws SettingsException;


}
