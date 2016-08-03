/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.job.service;

import java.util.List;

import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateDetail;
import com.turborep.turbotracker.job.dao.JoQuotetemplateHeader;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.exception.QuoteTemplateException;

public interface QuoteTemplateService {

	public List<JoQuotetemplateHeader> loadQuoteTemplate() throws  QuoteTemplateException;

	public List<JoQuoteTemplateDetail> getQuoteTemplateLineItems(Integer joQuoteTemplateHeaderId) throws QuoteTemplateException;
	
	//public void insertQuoteTemplates(Integer joMaster,Integer joQuoteTemplateHeaderID) throws QuoteTemplateException ;
	public JoQuoteHeader insertQuoteTemplates(Integer joMaster,Integer joQuoteTemplateHeaderID) throws QuoteTemplateException ;
	
	public Boolean editQuoteTemplate(JoQuotetemplateHeader ajoHeader) throws QuoteTemplateException ;

	public void editTemplateLineItem(JoQuoteTemplateDetail aDetail) throws QuoteTemplateException;
	
	public void addTemplateLineItem(JoQuoteTemplateDetail aDetail) throws QuoteTemplateException;

	public Integer addQuoteTemplate(JoQuotetemplateHeader thejoHeader) throws QuoteTemplateException;
	
	public Boolean deleteQuoteTemplate(JoQuotetemplateHeader ajoHeader) throws QuoteTemplateException ;

	public void updatelineItemInlineNote(JoQuoteTemplateDetail theDetail) throws QuoteTemplateException;
	
	public void updatelineItemProductNote(JoQuoteTemplateDetail theDetail) throws QuoteTemplateException;
	
	public Integer deleteTemplateLineItem(JoQuoteTemplateDetail aDetail) throws QuoteTemplateException;
	
	public JoQuotetemplateHeader getTemplateDetailsAmounts(Integer quoteTemplateHeaderID) throws JobException;
	
	public Integer editQuoteTemplateAmounts(JoQuotetemplateHeader ajoHeader) throws QuoteTemplateException ;

	public JoQuoteHeader getDetailsMSTRAmounts(Integer quoteHeaderID)throws JobException;

	public JoQuotetemplateHeader getTemplateDetailsMSTRAmounts(Integer quoteTemplateHeaderID) throws JobException;
	
}
