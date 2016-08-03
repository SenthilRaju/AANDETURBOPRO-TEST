package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.exception.CustomerException;

@Service("paymentTermsService")
public class PaymentsTermsServiceImpl implements PaymentsTermsService {
	
	Logger itsLogger = Logger.getLogger(PaymentsTermsServiceImpl.class);
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Override
	public List<CuTerms> getcuTerms() throws CustomerException {
		Session aSession = null;
		Query query = null;
		List<CuTerms> aCuTerms = new ArrayList<CuTerms>();
		try{
			aSession = itsSessionFactory.openSession();
		 query = aSession.createQuery("from CuTerms  ORDER BY Description ASC ");
		 aCuTerms = query.list();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CustomerException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCuTerms;
	}

	@Override
	public void updateTerms(Integer cuTermsId, String description,
			boolean inActive, Integer dueDays, Integer discountDays,
			BigDecimal discountPercent, boolean dueOnDay, boolean discOnDay,
			String orderNote, String pickTicketNote1, String pickTicketNote2,
			String pickTicketNote3, String pickTicketNote4,
			String pickTicketNote5, boolean isGlobal) throws CustomerException {
		Session aSession = null;
		Transaction aTransaction = null; 
		try{
			CuTerms acuTerms = new CuTerms( description,  inActive,  dueDays,
					 discountDays,  discountPercent,  dueOnDay,
					 discOnDay,  orderNote,  pickTicketNote1, 
					 pickTicketNote2,	 pickTicketNote3,  pickTicketNote4,
					 pickTicketNote5,  isGlobal);
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if(cuTermsId!=null && cuTermsId != 0)
			{
				 acuTerms = (CuTerms) aSession.get(CuTerms.class, cuTermsId);
				if(acuTerms != null)
				{
					acuTerms.setDescription(description);
					acuTerms.setDiscOnDay(discOnDay);
					acuTerms.setDueDays(dueDays);
					acuTerms.setDiscountDays(discountDays);
					acuTerms.setDiscountPercent(discountPercent);
					acuTerms.setDueOnDay(dueOnDay);
					acuTerms.setInActive(inActive);
					acuTerms.setIsGlobal(isGlobal);
					acuTerms.setOrderNote(orderNote);
					acuTerms.setPickTicketNote1(pickTicketNote1);
					acuTerms.setPickTicketNote2(pickTicketNote2);
					acuTerms.setPickTicketNote3(pickTicketNote3);
					acuTerms.setPickTicketNote4(pickTicketNote4);
					acuTerms.setPickTicketNote5(pickTicketNote5);
					aSession.update(acuTerms);	
				}
			} else {
				aSession.save(acuTerms);
			}
			aTransaction.commit();
		} catch (Exception e) {
			aTransaction.rollback();
			itsLogger.error(e.getMessage(), e);
			throw new CustomerException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}

	@Override
	public void deleteTerms(Integer cuTermsId) throws CustomerException {
		Session aSession = null;
		Transaction aTransaction = null; 
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			if(cuTermsId != null)
			{
				CuTerms acuTerms = (CuTerms) aSession.get(CuTerms.class, cuTermsId);
				if(acuTerms != null)
				{
					aSession.delete(acuTerms);	
				}
			}
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new CustomerException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
}
