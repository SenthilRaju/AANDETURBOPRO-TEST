package com.turborep.turbotracker.job.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.turborep.turbotracker.job.dao.JoFinancialReportTemp;
import com.turborep.turbotracker.job.exception.JobException;

public interface JobFinanceService {
	
	/**
	 * @param {@link Integer} JomasterID
	 * @return Finance tab UI Values as {@link HashMap}
	 * @throws {@link JobException}
	 * */
	
	public HashMap<String, Object> getJobFinanceDetails (Integer Jomaster) throws JobException;
	public Integer getInvoiceCounts(Integer JoMasterID) throws JobException;
	public ArrayList<JoFinancialReportTemp> getFinanceReportTemp(Integer JoMasterID) throws JobException ;
}
