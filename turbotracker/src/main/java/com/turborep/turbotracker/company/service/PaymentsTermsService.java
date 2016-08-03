package com.turborep.turbotracker.company.service;

import java.math.BigDecimal;
import java.util.List;

import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.exception.CustomerException;

public interface PaymentsTermsService {

	public List<CuTerms> getcuTerms() throws CustomerException;

	public void updateTerms(Integer cuTermsId, String description,
			boolean inActive, Integer dueDays, Integer discountDays,
			BigDecimal discountPercent, boolean dueOnDay, boolean discOnDay,
			String orderNote, String pickTicketNote1, String pickTicketNote2,
			String pickTicketNote3, String pickTicketNote4,
			String pickTicketNote5, boolean isGlobal) throws CustomerException;

	public void deleteTerms(Integer cuTermsId) throws CustomerException;

}
