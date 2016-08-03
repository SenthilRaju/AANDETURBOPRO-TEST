package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Codivisionposting;
import com.turborep.turbotracker.json.AutoCompleteBean;

@Transactional
@Service("DivisionsService")
public class DivisionsServiceImpl implements DivisionsService {

	Logger itsLogger = Logger.getLogger(DivisionsServiceImpl.class);

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;

	@Override
	public List<Codivision> getListOfDivisions(int theFrom, int theRows)
			throws CompanyException {
		String aSelectDivisonsQry = "SELECT Code, Description, CoDivisionID, InActive, UseInvoiceSeqNo, InvoiceSeqNo, AddressQuote, Address1, Address2, Address3 FROM coDivision";
		ArrayList<Codivision> aListOfDivisions = null;
		Session aSession = null;Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aSelectDivisonsQry);
			aListOfDivisions = (ArrayList<Codivision>) aQuery.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;aSelectDivisonsQry =null;
		}
		return aListOfDivisions;
	}

	@Override
	public BigInteger getListOfDivisionsCount() throws CompanyException {
		BigInteger count;
		Session aSession = null;Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
		    aQuery = aSession.createQuery("FROM  Codivision");
			count = BigInteger.valueOf(aQuery.list().size());
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
		return count;
	}

	@Override
	public int getRecordDivisionsCount() throws CompanyException {
		String aDivisionCountStr = "SELECT COUNT(coDivisionID) AS count FROM coDivision";
		Session aSession = null;
		BigInteger aTotalCount = null;Query aQuery =null;
		try {
			// Retrieve session from Hibernate
			aSession = itsSessionFactory.openSession();
			 aQuery = aSession.createSQLQuery(aDivisionCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			aQuery =null;aDivisionCountStr=null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public List<Codivision> getDivisionList() throws CompanyException {
		Session aSession = null;Query aQuery =null;
		List<Codivision> aQueryList = new ArrayList<Codivision>();
		String aCustomerQry = "SELECT * FROM coDivision ORDER BY Description ASC ";
		Codivision aCoDiv = null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCoDiv = new Codivision();
				Object[] aObj = (Object[]) aIterator.next();
				aCoDiv.setCoDivisionId((Integer) aObj[0]);
				aCoDiv.setDescription((String) aObj[1]);
				aCoDiv.setCode((String) aObj[2]);
				if ((Byte) aObj[3] == 1) {
					aCoDiv.setInActive(true);
				} else {
					aCoDiv.setInActive(false);
				}
				if ((Byte) aObj[4] == 1) {
					aCoDiv.setUseInvoiceSeqNo(true);
				} else {
					aCoDiv.setUseInvoiceSeqNo(false);
				}
				aCoDiv.setInvoiceSeqNo((Integer) aObj[5]);

				if ((Byte) aObj[6] == 1) {
					aCoDiv.setAddressQuote(true);
				} else {
					aCoDiv.setAddressQuote(false);
				}
				aCoDiv.setAddress1((String) aObj[7]);
				aCoDiv.setAddress2((String) aObj[8]);
				aCoDiv.setAddress3((String) aObj[9]);
				aCoDiv.setSubAccount((String) aObj[10]);
				aCoDiv.setAccountDistribution((BigDecimal) aObj[11]);
				aCoDiv.setAddress4((String) aObj[12]);
				aCoDiv.setAdditional1((String) aObj[13]);
				aCoDiv.setAdditional2((String) aObj[14]);
				aCoDiv.setAdditional3((String) aObj[15]);
				aQueryList.add(aCoDiv);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public List<Codivisionposting> getDivisionAlternateAccount(
			Integer theCoDivisionID) throws CompanyException {
		Session aSession = null;
		List<Codivisionposting> aQueryList = new ArrayList<Codivisionposting>();
		String aCustomerQry = "SELECT cDP.*,cA.Description,cAA.Description as Des,cA.coAccountID,cAA.coAccountID as AltID  FROM coDivisionPosting cDP "
				+ " LEFT JOIN coAccount cA ON cA.coAccountID = cDP.coAccountPostID"
				+ " LEFT JOIN coAccount cAA ON cAA.coAccountID = cDP.coAccountAlternateID"
				+ " WHERE coDivisionID ='" + theCoDivisionID + "' ORDER BY cA.Description ASC";
		Codivisionposting aCoPosting = null;
		Query aQuery =null;
		try {
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aCustomerQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCoPosting = new Codivisionposting();
				Object[] aObj = (Object[]) aIterator.next();
				aCoPosting.setCoDivisionPostingId((Integer) aObj[0]);
				aCoPosting.setCoDivisionId((Integer) aObj[1]);
				aCoPosting.setCoAccountPostId((Integer) aObj[2]);
				aCoPosting.setCoAccountAlternateId((Integer) aObj[3]);
				aCoPosting.setOriginalAccount((String) aObj[4]);
				aCoPosting.setAlternateAccount((String) aObj[5]);
				aCoPosting.setOriginalID((Integer) aObj[6]);
				aCoPosting.setAlternateID((Integer) aObj[7]);
				aQueryList.add(aCoPosting);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aCustomerQry=null;aQuery=null;
		}
		return aQueryList;
	}

	@Override
	public Codivision getDivisions(Integer theCoDivisionID)
			throws CompanyException {
		Session aSession = null;
		Codivision aCodivision = null;
		try {
			aSession = itsSessionFactory.openSession();
			aCodivision = (Codivision) aSession.get(Codivision.class,
					theCoDivisionID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			
		}
		return aCodivision;
	}

	@Override
	public boolean addNewDivision(Codivision theCodivision)
			throws CompanyException {
		Session aCodivisionSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCodivisionSession.beginTransaction();
			aTransaction.begin();
			aCodivisionSession.save(theCodivision);
			aTransaction.commit();
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			CompanyException aCompanyException = new CompanyException(
					excep.getMessage(), excep);
			throw aCompanyException;
		} finally {
			aCodivisionSession.flush();
			aCodivisionSession.close();
		}
		return true;
	}

	@Override
	public Boolean deleteDivision(Integer coDivisionID) throws CompanyException {
		Session aCoDivisionSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String aCoDivisionQry;
		try {
			aTransaction = aCoDivisionSession.beginTransaction();
			aCoDivisionQry = "DELETE FROM coDivision WHERE coDivisionID ="
					+ coDivisionID;
			aCoDivisionSession.createSQLQuery(aCoDivisionQry).executeUpdate();
			aTransaction.commit();
		} catch (HibernateException e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aCoDivisionSession.flush();
			aCoDivisionSession.close();
			aCoDivisionQry=null;
		}
		return true;
	}

	@Override
	public Codivision updateDivision(Codivision theCodivision)
			throws CompanyException {
		Session aSession = itsSessionFactory.openSession();
		Codivision aCodivision = null;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aCodivision = (Codivision) aSession.get(Codivision.class,theCodivision.getCoDivisionId());
			aCodivision.setCoDivisionId(theCodivision.getCoDivisionId());
			aCodivision.setCode(theCodivision.getCode());
			aCodivision.setDescription(theCodivision.getDescription());
			aCodivision.setInActive(theCodivision.isInActive());
			aCodivision.setUseInvoiceSeqNo(theCodivision.isUseInvoiceSeqNo());
			aCodivision.setInvoiceSeqNo(theCodivision.getInvoiceSeqNo());
			aCodivision.setSubAccount(theCodivision.getSubAccount());
			aCodivision.setAccountDistribution(theCodivision.getAccountDistribution());
			aCodivision.setAddress1(theCodivision.getAddress1());
			aCodivision.setAddress2(theCodivision.getAddress2());
			aCodivision.setAddress3(theCodivision.getAddress3());
			aCodivision.setAddress4(theCodivision.getAddress4());
			aCodivision.setAddressQuote(theCodivision.isAddressQuote());
			aCodivision.setAdditional1(theCodivision.getAdditional1());
			aCodivision.setAdditional2(theCodivision.getAdditional2());
			aCodivision.setAdditional3(theCodivision.getAdditional3());
			aSession.update(aCodivision);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aCodivision;
	}

	@Override
	public boolean deleteCoDivisionPosting(Integer coDivisionID)
			throws CompanyException {

		Session aCoDivisionPostingSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		String aCoDivisionPostingQry;
		try {
			aTransaction = aCoDivisionPostingSession.beginTransaction();
			aCoDivisionPostingQry = "DELETE FROM coDivisionPosting WHERE coDivisionPostingID ="
					+ coDivisionID;
			aCoDivisionPostingSession.createSQLQuery(aCoDivisionPostingQry)
					.executeUpdate();
			aTransaction.commit();
		} catch (HibernateException e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aCoDivisionPostingSession.flush();
			aCoDivisionPostingSession.close();
			aCoDivisionPostingQry=null;
		}
		return true;
	}

	@Override
	public ArrayList<AutoCompleteBean> getCoAccountList(String theProductName)
			throws CompanyException {
		String salesselectQry = "SELECT coAccountID,Description FROM coAccount WHERE Description like '%"
				+ theProductName + "%' ORDER BY Description ASC";
		Session aSession = null;Query aQuery =null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(salesselectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				aUserbean.setLabel((String) aObj[1]);
				aUserbean.setValue((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry=null;aQuery =null;
		}
		return aQueryList;
	}

	@Override
	public boolean editCoPostingAccount(Codivisionposting theCodivisionPosting)
			throws CompanyException {
		Session aCodivPostingSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		Codivisionposting aCodivisionPosting = null;
		try {
			aTransaction = aCodivPostingSession.beginTransaction();
			aCodivisionPosting = (Codivisionposting) aCodivPostingSession.get(
					Codivisionposting.class,
					theCodivisionPosting.getCoDivisionPostingId());
			aTransaction.begin();
			aCodivisionPosting.setCoDivisionId(theCodivisionPosting
					.getCoDivisionId());
			aCodivisionPosting.setCoAccountPostId(theCodivisionPosting
					.getCoAccountPostId());
			aCodivisionPosting.setCoAccountAlternateId(theCodivisionPosting
					.getCoAccountAlternateId());
			aCodivPostingSession.update(aCodivisionPosting);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aCodivPostingSession.flush();
			aCodivPostingSession.close();

		}
		return true;
	}

	@Override
	public boolean addCoPostingAccount(Codivisionposting theCodivisionPosting)
			throws CompanyException {
		Session aCodivPostingSession = itsSessionFactory.openSession();
		Transaction aTransaction;
		try {
			aTransaction = aCodivPostingSession.beginTransaction();
			aTransaction.begin();
			aCodivPostingSession.save(theCodivisionPosting);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			CompanyException aCompanyException = new CompanyException(
					e.getMessage(), e);
			throw aCompanyException;
		} finally {
			aCodivPostingSession.flush();
			aCodivPostingSession.close();
		}
		return true;
	}
}
