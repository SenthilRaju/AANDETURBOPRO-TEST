package com.turborep.turbotracker.company.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoAccountBean;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Codivisionposting;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;

public interface DivisionsService {

	public List<Codivision> getListOfDivisions(int theFrom, int theRows)
			throws CompanyException;

	BigInteger getListOfDivisionsCount() throws CompanyException;

	public int getRecordDivisionsCount() throws CompanyException;

	public List<Codivision> getDivisionList() throws CompanyException;

	public List<Codivisionposting> getDivisionAlternateAccount(
			Integer theCoDivisionID) throws CompanyException;

	public Codivision getDivisions(Integer theCoDivisionID)
			throws CompanyException;

	public boolean addNewDivision(Codivision theCodivision)
			throws CompanyException;

	public Boolean deleteDivision(Integer coDivisionID) throws CompanyException;

	public Codivision updateDivision(Codivision aCodivison)
			throws CompanyException;

	public boolean deleteCoDivisionPosting(Integer coDivisionID)
			throws CompanyException;

	public ArrayList<AutoCompleteBean> getCoAccountList(
			String theProductNameWithCode) throws CompanyException;

	public boolean editCoPostingAccount(Codivisionposting theCodivisionposting)
			throws CompanyException;

	public boolean addCoPostingAccount(Codivisionposting theCodivisionposting)
			throws CompanyException;
}
