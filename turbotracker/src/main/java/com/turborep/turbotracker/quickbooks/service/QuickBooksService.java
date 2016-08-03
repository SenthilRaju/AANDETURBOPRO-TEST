package com.turborep.turbotracker.quickbooks.service;

import java.util.List;

import com.turborep.turbotracker.quickbooks.dao.QbConfig;
import com.turborep.turbotracker.vendor.dao.Vepodetail;

/**
 * 
 * @author vish_pepala
 */
public interface QuickBooksService  {
	
	public void saveQB(QbConfig theQbpo );

	public void updateQB(QbConfig theQbpo);

	public Integer getQbConfig(String aQbuserName);

	public QbConfig getQbConfigBean(String userName);

	public List<Vepodetail> getVepodetailList(Integer theVePOId);

	public String getItemCode(Integer thePrMasterId);
	
}
