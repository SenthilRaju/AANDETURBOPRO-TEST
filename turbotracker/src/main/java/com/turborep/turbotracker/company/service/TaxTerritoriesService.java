package com.turborep.turbotracker.company.service;

import java.util.List;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;

public interface TaxTerritoriesService {
	
	public Boolean deleteTaxTerritory(Integer coTaxTerritoryID) throws CompanyException;

	public List<CoTaxTerritory> getTaxTerritoryList() throws CompanyException;

	public Boolean addTerritory(CoTaxTerritory theNewTerritory) throws CompanyException;
	
	public CoTaxTerritory getSingleTaxTerritory(Integer TaxTerritoryID) throws CompanyException;
	
	public Boolean editTaxTerritory(CoTaxTerritory theTaxTerritory)throws CompanyException;

}
