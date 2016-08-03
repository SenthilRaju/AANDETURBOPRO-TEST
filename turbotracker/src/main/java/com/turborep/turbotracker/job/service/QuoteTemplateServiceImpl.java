/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.job.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.job.dao.JoQuoteDetail;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateDetail;
import com.turborep.turbotracker.job.dao.JoQuotetemplateHeader;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.exception.QuoteTemplateException;

@Service("quoteTemplateService")
@Transactional
public class QuoteTemplateServiceImpl implements QuoteTemplateService {

	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	Logger itsLogger = Logger.getLogger(QuoteTemplateServiceImpl.class);
	
	@Override
	public List<JoQuotetemplateHeader> loadQuoteTemplate() throws  QuoteTemplateException {
		List<JoQuotetemplateHeader> aQuoteTemplates = null;
		Session aSession = itsSessionFactory.openSession();
		/**
		 * SELECT * FROM joQuoteTemplateHeader WHERE GlobalTemplate = 1 OR OwnerID = -1  ORDER BY TemplateName;
		 * */
		try {
			Criteria aCriteria = aSession.createCriteria(JoQuotetemplateHeader.class);
			Criterion aGlobalTemplate = Restrictions.eq("neworoldquote", 1);
			//LogicalExpression anOrExpression = Restrictions.or(aGlobalTemplate, aOwnerID);
			aCriteria.addOrder(Order.asc("templateName"));
			aCriteria.add(aGlobalTemplate);
			
			//Criterion aGlobalTemplate = Restrictions.eq("globalTemplate", true);
			//	Criterion aOwnerID = Restrictions.eq("ownerId", -1);
			//LogicalExpression anOrExpression = Restrictions.or(aGlobalTemplate, aOwnerID);
			//aCriteria.add(anOrExpression);
			aQuoteTemplates = aCriteria.list();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQuoteTemplates;
	}

	@Override
	public List<JoQuoteTemplateDetail> getQuoteTemplateLineItems(Integer joQuoteTemplateHeaderId) throws  QuoteTemplateException{
		List<JoQuoteTemplateDetail> aTemplateLineItemsList =  new ArrayList<JoQuoteTemplateDetail>();;
		Session aSession = itsSessionFactory.openSession();
		String query = "select j.joQuoteTemplateDetailID,j.joQuoteTemplateHeaderID,j.Product,j.ProductNote,j.ItemQuantity,j.Paragraph,"
				+ "j.rxManufacturerID,j.Price,j.InlineNote,j.Cost,j.Subtotal,j.veFactoryID,j.Mult,j.Spec,j.FmtBold,j.FmtUnderline,"
				+ "j.FmtItalic,j.FmtLarge,j.DetailSequenceID,j.PricingNote,j.POSITION,j.Percentage, r.Name, "
				+"jqprop.italicItem,jqprop.underlineItem,jqprop.boldItem,"
				+"jqprop.italicQuantity,jqprop.underlineQuantity,jqprop.boldQuantity,"
				+"jqprop.italicParagraph,jqprop.underlineParagraph,jqprop.boldParagraph,"
				+"jqprop.italicManufacturer,jqprop.underlineManufacturer,jqprop.boldManufacturer,"
				+"jqprop.italicSpec,jqprop.underlineSpec,jqprop.boldSpec,"
				+"jqprop.italicMult,jqprop.underlineMult,jqprop.boldMult,"
				+"jqprop.italicPrice,jqprop.underlinePrice,jqprop.boldPrice "
				
//				+ ",IF(ISNULL (jqprop.italicItem),'0',jqprop.italicItem) AS italicItem,"
//				+ "IF(ISNULL (jqprop.underlineItem),'0',jqprop.underlineItem) AS underlineItem,"
//				+ "IF(ISNULL (jqprop.boldItem),'0',jqprop.boldItem) AS boldItem,"
//				+ "IF(ISNULL (jqprop.italicQuantity),'0',jqprop.italicQuantity) AS italicQuantity,"
//				+ "IF(ISNULL (jqprop.underlineQuantity),'0',jqprop.underlineQuantity) AS underlineQuantity,"
//				+ "IF(ISNULL (jqprop.boldQuantity),'0',jqprop.boldQuantity) AS boldQuantity,"
//				+ "IF(ISNULL (jqprop.italicParagraph),'0',jqprop.italicParagraph) AS italicParagraph,"
//				+ "IF(ISNULL (jqprop.underlineParagraph),'0',jqprop.underlineParagraph) AS underlineParagraph,"
//				+ "IF(ISNULL (jqprop.boldParagraph),'0',jqprop.boldParagraph) AS boldParagraph,"
//				+ "IF(ISNULL (jqprop.italicManufacturer),'0',jqprop.italicManufacturer) AS italicManufacturer,"
//				+ "IF(ISNULL (jqprop.underlineManufacturer),'0',jqprop.underlineManufacturer) AS underlineManufacturer,"
//				+ "IF(ISNULL (jqprop.boldManufacturer),'0',jqprop.boldManufacturer) AS boldManufacturer,"
//				+ "IF(ISNULL (jqprop.italicSpec),'0',jqprop.italicSpec) AS italicSpec,"
//				+ "IF(ISNULL (jqprop.underlineSpec),'0',jqprop.underlineSpec) AS underlineSpec,"
//				+ "IF(ISNULL (jqprop.boldSpec),'0',jqprop.boldSpec) AS boldSpec,"
//				+ "IF(ISNULL (jqprop.italicMult),'0',jqprop.italicMult) AS italicMult, "
//				+ "IF(ISNULL (jqprop.underlineMult),'0',jqprop.underlineMult) AS underlineMult,"
//				+ "IF(ISNULL (jqprop.boldMult),'0',jqprop.boldMult) AS boldMult,"
//				+ "IF(ISNULL (jqprop.italicPrice),'0',jqprop.italicPrice) AS italicPrice,"
//				+ "IF(ISNULL (jqprop.underlinePrice),'0',jqprop.underlinePrice) AS underlinePrice, "
//				+ "IF(ISNULL (jqprop.boldPrice),'0',jqprop.boldPrice) AS boldPrice "
				+ "from joQuoteTemplateDetail as j LEFT JOIN rxMaster as r on r.rxMasterID = j.rxManufacturerID LEFT JOIN joQLineItemsTempProp jqprop ON(j.joQuoteTemplateDetailId=jqprop.joQuoteTemplateDetailId)  where j.joQuoteTemplateHeaderID = " + joQuoteTemplateHeaderId;
		try {
			Query aQuery = aSession.createSQLQuery(query);
			Iterator<?> aIterator  = aQuery.list().iterator();
			while(aIterator.hasNext()){
				Object[] aObj = (Object[])aIterator.next();
				JoQuoteTemplateDetail atemp = new JoQuoteTemplateDetail();
				atemp.setJoQuoteTemplateDetailId((Integer) aObj[0]);
				atemp.setJoQuoteTemplateHeaderId(joQuoteTemplateHeaderId);
				atemp.setProduct((String) aObj[2]);
				atemp.setProductNote((String) aObj[3]);
				atemp.setItemQuantity((String) aObj[4]);
				atemp.setParagraph((String) aObj[5]);
				atemp.setRxManufacturerId((Integer) aObj[6]);
				atemp.setPrice((BigDecimal) aObj[7]);
				atemp.setInlineNote((String) aObj[8]);
				atemp.setCost((BigDecimal) aObj[9]);
				atemp.setMult((BigDecimal) aObj[12]);
				atemp.setSpec((Short) aObj[13]);
				atemp.setPosition((Double) aObj[20]);
				atemp.setManufacturerName((String) aObj[22]);
				atemp.setInLineNoteImage((String) aObj[8]);
				String itemclassname=" ";
				//System.out.println("(Byte)aObj[16])"+(Byte)aObj[16]);
				if(aObj[23]!=null&&((Byte)aObj[23])==1){
					itemclassname=itemclassname+" griditalic";
				}
				if(aObj[24]!=null&&((Byte)aObj[24])==1){
					itemclassname=itemclassname+" gridunderline";
				}
				if(aObj[25]!=null&&((Byte)aObj[25])==1){
					itemclassname=itemclassname+" gridbold";
				}
				atemp.setItemClassName(itemclassname);
				
				String quantityclassname=" ";
				
				if(aObj[26]!=null&&((Byte)aObj[26])==1){
					quantityclassname=quantityclassname+" griditalic";
				}
				if(aObj[27]!=null&&((Byte)aObj[27])==1){
					quantityclassname=quantityclassname+" gridunderline";
				}
				if(aObj[28]!=null&&((Byte)aObj[28])==1){
					quantityclassname=quantityclassname+" gridbold";
				}
				atemp.setQtyClassName(quantityclassname);
				
				String paragraphclassname=" ";
				
				if(aObj[29]!=null&&((Byte)aObj[29])==1){
					paragraphclassname=paragraphclassname+" griditalic";
				}
				if(aObj[30]!=null&&((Byte)aObj[30])==1){
					paragraphclassname=paragraphclassname+" gridunderline";
				}
				if(aObj[31]!=null&&((Byte)aObj[31])==1){
					paragraphclassname=paragraphclassname+" gridbold";
				}
				atemp.setParaClassName(paragraphclassname);
				
				String Manclassname=" ";
				
				if(aObj[32]!=null&&((Byte)aObj[32])==1){
					Manclassname=Manclassname+" griditalic";
				}
				if(aObj[33]!=null&&((Byte)aObj[33])==1){
					Manclassname=Manclassname+" gridunderline";
				}
				if(aObj[34]!=null&&((Byte)aObj[34])==1){
					Manclassname=Manclassname+" gridbold";
				}
				atemp.setManfcClassName(Manclassname);
				
				String Specclassname=" ";
				
				if(aObj[35]!=null&&((Byte)aObj[35])==1){
					Specclassname=Specclassname+" griditalic";
				}
				if(aObj[36]!=null&&((Byte)aObj[36])==1){
					Specclassname=Specclassname+" gridunderline";
				}
				if(aObj[37]!=null&&((Byte)aObj[37])==1){
					Specclassname=Specclassname+" gridbold";
				}
				atemp.setSpecClassName(Specclassname);
				
				String Multclassname=" ";
				
				if(aObj[38]!=null&&((Byte)aObj[38])==1){
					Multclassname=Multclassname+" griditalic";
				}
				if(aObj[39]!=null&&((Byte)aObj[39])==1){
					Multclassname=Multclassname+" gridunderline";
				}
				if(aObj[40]!=null&&((Byte)aObj[40])==1){
					Multclassname=Multclassname+" gridbold";
				}
				atemp.setMultClassName(Multclassname);
				
				String Priceclassname=" ";
				
				if(aObj[41]!=null&&((Byte)aObj[41])==1){
					Priceclassname=Priceclassname+" griditalic";
				}
				if(aObj[42]!=null&&((Byte)aObj[42])==1){
					Priceclassname=Priceclassname+" gridunderline";
				}
				if(aObj[43]!=null&&((Byte)aObj[43])==1){
					Priceclassname=Priceclassname+" gridbold";
				}
				atemp.setPriceClassName(Priceclassname);
				
				aTemplateLineItemsList.add(atemp);
				}
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		}finally {
			aSession.flush();
			aSession.close();
		}
		return aTemplateLineItemsList;
	}
	@Override
	public JoQuoteHeader insertQuoteTemplates(Integer joMaster,Integer joQuoteTemplateHeaderID) throws  QuoteTemplateException {
		Session ajoQuoteTemplateSession = null;
		//Session ajoQuoteTemplateDetailSession = null;
		Transaction aTransaction = null;
		try{
			ajoQuoteTemplateSession = itsSessionFactory.openSession();
			aTransaction = ajoQuoteTemplateSession.beginTransaction();
			aTransaction.begin();
			JoQuotetemplateHeader aJoTemp = (JoQuotetemplateHeader)ajoQuoteTemplateSession.get(JoQuotetemplateHeader.class,joQuoteTemplateHeaderID); 
			JoQuoteHeader aJoHeadObj = new JoQuoteHeader();
			aJoHeadObj.setJoMasterID(joMaster);
			aJoHeadObj.setCuMasterTypeID(aJoTemp.getCuMasterTypeId());
			aJoHeadObj.setQuoteRev(aJoTemp.getQuoteRev());
			aJoHeadObj.setQuoteAmount(aJoTemp.getQuoteAmount());
			aJoHeadObj.setCostAmount(aJoTemp.getCostAmount());
			aJoHeadObj.setCreatedById(aJoTemp.getOwnerId());
			aJoHeadObj.setRemarks(aJoTemp.getRemarks());
//			aJoHeadObj.setQuoteYesNo1((Byte)aJoTemp.getQuoteYesNo1());
			aJoHeadObj.setDateCreated(new Date());
			aJoHeadObj.setDisplayQuantity((byte)(aJoTemp.isDisplayQuantity()?1:0));
			aJoHeadObj.setDisplayCost((byte)(aJoTemp.isDisplayCost()?1:0));
			aJoHeadObj.setDisplayPrice((byte)(aJoTemp.isDisplayPrice()?1:0));
			aJoHeadObj.setDisplayParagraph((byte)(aJoTemp.isDisplayParagraph()?1:0));
			aJoHeadObj.setDisplayManufacturer((byte)(aJoTemp.isPrintQuantity()?1:0));
			aJoHeadObj.setPrintQuantity((byte)(aJoTemp.isPrintQuantity()?1:0));
			aJoHeadObj.setPrintParagraph((byte)(aJoTemp.isPrintParagraph()?1:0));
			aJoHeadObj.setPrintManufacturer((byte)(aJoTemp.isPrintManufacturer()?1:0));
			aJoHeadObj.setPrintCost((byte)(aJoTemp.isPrintCost()?1:0));
			aJoHeadObj.setPrintPrice((byte)(aJoTemp.isPrintPrice()?1:0));
			aJoHeadObj.setNotesFullWidth((byte)(aJoTemp.isNotesFullWidth()?1:0));
			aJoHeadObj.setLineNumbers((byte)(aJoTemp.isLineNumbers()?1:0));
			Integer ajoQuoteHeaderID = (Integer) ajoQuoteTemplateSession.save(aJoHeadObj);
			JoQuoteHeader aJoHead = (JoQuoteHeader)ajoQuoteTemplateSession.get(JoQuoteHeader.class,ajoQuoteHeaderID); 
			aJoHead.setJoQuoteTemplateHeaderId(ajoQuoteHeaderID);
			aTransaction.commit();
//			ajoQuoteTemplateDetailSession = itsSessionFactory.openSession();
//			aTransaction = ajoQuoteTemplateDetailSession.beginTransaction();
//			aTransaction.begin();
//			String aInsertQuoteTemplateDetail = "INSERT joQuoteDetail "+
//					"(joQuoteHeaderID,Product, "+
//					"ProductNote, "+
//					"ItemQuantity, "+
//					"Paragraph, "+	
//					"rxManufacturerID, "+
//					"Price, "+	
//					"InlineNote, "+	
//					"Cost, "+	
//					"Subtotal, "+
//					"veFactoryID, "+
//					"Mult, "+
//					"Spec, "+
//					"FmtBold, "+	
//					"FmtUnderline, "+
//					"FmtItalic, "+	
//					"FmtLarge, "+	
//					"DetailSequenceID, "+
//					"PricingNote "+	
//					") "+
//					"SELECT "+ ajoQuoteHeaderID+" ," +
//					"Product, "+
//					"ProductNote, "+
//					"ItemQuantity, "+
//					"Paragraph, "+	
//					"rxManufacturerID, "+	
//					"Price, "+	
//					"InlineNote, "+	
//					"Cost, "+	
//					"Subtotal, "+
//					"veFactoryID, "+
//					"Mult, "+
//					"Spec, "+
//					"FmtBold, "+
//					"FmtUnderline, "+
//					"FmtItalic, "+	
//					"FmtLarge, "+	
//					"DetailSequenceID, "+
//					"PricingNote "+	
//					"FROM joQuoteTemplateDetail WHERE joQuoteTemplateHeaderID= "+joQuoteTemplateHeaderID+";";
			 CopyFromQuoteTemplatetoquotewithproperties(ajoQuoteHeaderID,joQuoteTemplateHeaderID);
			 //ajoQuoteTemplateDetailSession.createSQLQuery(aInsertQuoteTemplateDetail).executeUpdate();
			//aTransaction.commit();
			return aJoHead;
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			ajoQuoteTemplateSession.flush();
			ajoQuoteTemplateSession.close();
		//	ajoQuoteTemplateDetailSession.flush();
			//ajoQuoteTemplateDetailSession.close();
		}
	}
	
	
	public boolean CopyFromQuoteTemplatetoquotewithproperties(Integer ajoQuoteHeaderID,Integer joQuoteTemplateHeaderID) {
		Session aSession = null;
		//Integer joQuoteReciptID = null;
		//Integer joQLineItemsPropId = 0;
		//joQLineItemTemplateProp ajoQLineItemsProp = null;
		try {
			aSession = itsSessionFactory.openSession();
			String Selectqry = "SELECT  Product,ProductNote,ItemQuantity,Paragraph,rxManufacturerID,Price,InlineNote,Cost,Subtotal," 
		+"veFactoryID,Mult,Spec,FmtBold,FmtUnderline,FmtItalic,FmtLarge,DetailSequenceID,PricingNote,joQuoteTemplateDetailID,POSITION FROM joQuoteTemplateDetail"
		+" WHERE joQuoteTemplateHeaderID= "+joQuoteTemplateHeaderID;
			Query aQuery = aSession.createSQLQuery(Selectqry);
			try {
				Iterator<?> aIterator = aQuery.list().iterator();
				while (aIterator.hasNext()) {
					JoQuoteDetail ajoqdetail=new JoQuoteDetail();
					Object[] aObj = (Object[]) aIterator.next();
					ajoqdetail.setJoQuoteHeaderID(ajoQuoteHeaderID);
					ajoqdetail.setProduct((String) aObj[0]);
					ajoqdetail.setProductNote((String) aObj[1]);
					ajoqdetail.setItemQuantity((String) aObj[2]);
					ajoqdetail.setParagraph((String) aObj[3]);
					ajoqdetail.setRxManufacturerID((Integer) aObj[4]);
					ajoqdetail.setPrice((BigDecimal) aObj[5]);
					ajoqdetail.setInlineNote((String) aObj[6]);
					ajoqdetail.setCost((BigDecimal) aObj[7]);
					ajoqdetail.setSubtotal((Byte) aObj[8]);
					ajoqdetail.setVeFactoryId((Short) aObj[9]);
					ajoqdetail.setMult((BigDecimal) aObj[10]);
					ajoqdetail.setSpec((Short) aObj[11]);
					ajoqdetail.setFmtBold((Byte) aObj[12]);
					ajoqdetail.setFmtUnderline((Byte) aObj[13]);
					ajoqdetail.setFmtItalic((Byte) aObj[14]);
					ajoqdetail.setFmtLarge((Byte) aObj[15]);
					ajoqdetail.setDetailSequenceId((Integer) aObj[16]);
					ajoqdetail.setPricingNote((String) aObj[17]);
					ajoqdetail.setPosition((Double) aObj[19]);
					int lineitemid=addQuoteLineItemDetail(ajoqdetail);
					addpropertiesfromtemplatetoquote(lineitemid,ajoQuoteHeaderID,(Integer) aObj[18]);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(), e);
			}
			

		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	public boolean addpropertiesfromtemplatetoquote(Integer lineitemid,Integer ajoQuoteHeaderID,Integer joQuoteTemplateDetailID) throws  QuoteTemplateException {
		Session aSession = null;
		Transaction aTransaction = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			String query="INSERT joQLineItemsProp(joQuoteDetailID, italicItem  ,underlineItem ,boldItem,italicQuantity,underlineQuantity,boldQuantity,italicParagraph,underlineParagraph, boldParagraph,italicManufacturer,underlineManufacturer,boldManufacturer,italicSpec,underlineSpec,boldSpec,italicMult,underlineMult,boldMult,italicPrice,underlinePrice,boldPrice)"
						+" select "+lineitemid+",italicItem  ,underlineItem ,boldItem,italicQuantity,underlineQuantity,boldQuantity,italicParagraph,underlineParagraph,boldParagraph,italicManufacturer,underlineManufacturer,boldManufacturer,italicSpec,underlineSpec,boldSpec,italicMult,underlineMult,boldMult,italicPrice,underlinePrice,boldPrice from joQLineItemsTempProp "
						+ "where joQuoteTemplateDetailID="+joQuoteTemplateDetailID;
			aSession.createSQLQuery(query).executeUpdate();
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return true;
	}
	
	public int addQuoteLineItemDetail(JoQuoteDetail theJoQuoteDetail) throws  QuoteTemplateException {
		Session aSession = null;
		Transaction aTransaction = null;
		Integer joQuotedetailId = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			joQuotedetailId = (Integer) aSession.save(theJoQuoteDetail);
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return joQuotedetailId.intValue();
	}
	
	
	@Override
	public Integer addQuoteTemplate(JoQuotetemplateHeader thejoHeader) throws  QuoteTemplateException {
		Session aSession = null;
		Transaction aTransaction = null;
		Integer joQuoteTemplateHeaderId = null;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			joQuoteTemplateHeaderId = (Integer) aSession.save(thejoHeader);
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return joQuoteTemplateHeaderId;
	}
	
	@Override
	public Boolean editQuoteTemplate(JoQuotetemplateHeader thejoHeader) throws  QuoteTemplateException {
		Session aSession = null;
		Transaction aTransaction = null;
		JoQuotetemplateHeader ajoHeader = new JoQuotetemplateHeader();
		Boolean edited = false;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			ajoHeader = (JoQuotetemplateHeader)aSession.get(JoQuotetemplateHeader.class,thejoHeader.getJoQuoteTemplateHeaderId());
			ajoHeader.setTemplateName(thejoHeader.getTemplateName());
			ajoHeader.setRemarks(thejoHeader.getRemarks());
			ajoHeader.setCuMasterTypeId(thejoHeader.getCuMasterTypeId());
			ajoHeader.setPrintTotal(thejoHeader.getPrintTotal());
			ajoHeader.setCostAmount(thejoHeader.getCostAmount());
			//System.out.println("thejoHeader.getJoQuoteTemplateHeaderId()"+thejoHeader.getQuoteAmount());
			ajoHeader.setQuoteAmount(thejoHeader.getQuoteAmount());
			aSession.update(ajoHeader);
			aTransaction.commit();
			edited = true;
		} catch (Exception e){
			e.printStackTrace();
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return edited;
	}
	
	
	@Override
	public Integer editQuoteTemplateAmounts(JoQuotetemplateHeader thejoHeader) throws  QuoteTemplateException {
		Session aSession = null;
		Transaction aTransaction = null;
		JoQuotetemplateHeader ajoHeader = new JoQuotetemplateHeader();
		Integer edited = 0;
		try{
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			ajoHeader = (JoQuotetemplateHeader)aSession.get(JoQuotetemplateHeader.class,thejoHeader.getJoQuoteTemplateHeaderId());
			itsLogger.info("CostAmount: "+thejoHeader.getCostAmount()+"/n Quote Amouunt:"+thejoHeader.getQuoteAmount());
			ajoHeader.setCostAmount(thejoHeader.getCostAmount());
			ajoHeader.setQuoteAmount(thejoHeader.getQuoteAmount());
			aSession.update(ajoHeader);
			aTransaction.commit();
			edited = 1;
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return edited;
	}
	
	
	@Override
	public JoQuotetemplateHeader getTemplateDetailsAmounts(Integer quoteTemplateHeaderID) throws JobException {
		String aJobSelectQry = "SELECT SUM(Cost) AS costAmount,SUM(Price) AS totalAmount FROM joQuoteTemplateDetail WHERE joQuoteTemplateHeaderID ="+ quoteTemplateHeaderID;
		Session aSession = null;
		//String aCreatedBy = "";
		JoQuotetemplateHeader aJoQuotetemplateHeader = new JoQuotetemplateHeader();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				
				aJoQuotetemplateHeader.setCostAmount((BigDecimal) aObj[0]);
				aJoQuotetemplateHeader.setQuoteAmount((BigDecimal) aObj[1]);
			}	
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting Data from Quote: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuotetemplateHeader;
	}
	@Override
	public Boolean deleteQuoteTemplate(JoQuotetemplateHeader ajoHeader) throws  QuoteTemplateException{
		Boolean Deleted = false;
		Session ajoQuoteTemplateHeader = null;
		Transaction aTransaction =null;
		JoQuotetemplateHeader aHeader = new JoQuotetemplateHeader();
		//JoQuoteTemplateDetail aDetail = new JoQuoteTemplateDetail();
		try{
			ajoQuoteTemplateHeader = itsSessionFactory.openSession();
			aTransaction = ajoQuoteTemplateHeader.beginTransaction();
			aTransaction.begin();
			aHeader = (JoQuotetemplateHeader)ajoQuoteTemplateHeader.get(JoQuotetemplateHeader.class, ajoHeader.getJoQuoteTemplateHeaderId());
			ajoQuoteTemplateHeader.delete(aHeader);
			aTransaction.commit();
			ajoQuoteTemplateHeader = itsSessionFactory.openSession();
			aTransaction = ajoQuoteTemplateHeader.beginTransaction();
			aTransaction.begin();
			String deleteQuotetemplateLines = "DELETE from joQuoteTemplateDetail where joQuoteTemplateHeaderID = "+ajoHeader.getJoQuoteTemplateHeaderId()+";";
			ajoQuoteTemplateHeader.createSQLQuery(deleteQuotetemplateLines).executeUpdate();
			aTransaction.commit();
			Deleted =true;
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			ajoQuoteTemplateHeader.flush();
			ajoQuoteTemplateHeader.close();
		}
		return Deleted;
	}
	
	@Override
	public void addTemplateLineItem(JoQuoteTemplateDetail aDetail) throws  QuoteTemplateException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.save(aDetail);
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public void editTemplateLineItem(JoQuoteTemplateDetail aDetail) throws  QuoteTemplateException {
		Session aSession = itsSessionFactory.openSession();
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.update(aDetail);
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	@Override
	public Integer deleteTemplateLineItem(JoQuoteTemplateDetail aDetail) throws  QuoteTemplateException {
		Session aSession = itsSessionFactory.openSession();
		int a =0;
		try {
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aSession.delete(aDetail);
			aTransaction.commit();
			a=1;
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return a;
	}
	
	@Override
	public void updatelineItemInlineNote(JoQuoteTemplateDetail theDetail) throws QuoteTemplateException{
		Session aSession = itsSessionFactory.openSession();
		JoQuoteTemplateDetail aDetail = null;
		try{
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aDetail = (JoQuoteTemplateDetail) aSession.get(JoQuoteTemplateDetail.class, theDetail.getJoQuoteTemplateDetailId());
			aDetail.setInlineNote(theDetail.getInlineNote());
			aSession.update(aDetail);
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public void updatelineItemProductNote(JoQuoteTemplateDetail theDetail) throws QuoteTemplateException{
		Session aSession = itsSessionFactory.openSession();
		JoQuoteTemplateDetail aDetail = null;
		try{
			Transaction aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aDetail = (JoQuoteTemplateDetail) aSession.get(JoQuoteTemplateDetail.class, theDetail.getJoQuoteTemplateDetailId());
			aDetail.setProductNote(theDetail.getProductNote());
			aSession.update(aDetail);
			aTransaction.commit();
		} catch (Exception e){
			itsLogger.error(e.getMessage(),e);
			QuoteTemplateException aQuoteExcep= new QuoteTemplateException(e.getMessage(),e );
			throw aQuoteExcep;
		} finally {
			aSession.flush();
			aSession.close();
		}
	}
	
	@Override
	public JoQuotetemplateHeader getTemplateDetailsMSTRAmounts(Integer quoteTemplateHeaderID) throws JobException {
		String aJobSelectQry = "SELECT SUM(cost) AS costAmount,SUM(sellprice) AS totalAmount FROM joQuoteTempDetailMstr WHERE joQuoteTemplateHeaderID="+ quoteTemplateHeaderID;
		Session aSession = null;
		//String aCreatedBy = "";
		JoQuotetemplateHeader aJoQuotetemplateHeader = new JoQuotetemplateHeader();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				if( aObj[0]!=null){
					aJoQuotetemplateHeader.setCostAmount((BigDecimal) aObj[0]);
					}else{
					aJoQuotetemplateHeader.setCostAmount(BigDecimal.ZERO);
					}
					if( aObj[1]!=null){
					aJoQuotetemplateHeader.setQuoteAmount((BigDecimal) aObj[1]);
					}else{
					aJoQuotetemplateHeader.setQuoteAmount(BigDecimal.ZERO);
					}
			}	
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting Data from Quote: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuotetemplateHeader;
	}
	@Override
	public JoQuoteHeader getDetailsMSTRAmounts(Integer quoteHeaderID) throws JobException {
		String aJobSelectQry = "SELECT SUM(cost) AS costAmount,SUM(sellprice) AS totalAmount FROM joQuoteDetailMstr WHERE joQuoteHeaderID ="+ quoteHeaderID;
		Session aSession = null;
		//String aCreatedBy = "";
		JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				Object[] aObj = (Object[]) aIterator.next();
				if( aObj[0]!=null){
				aJoQuoteHeader.setCostAmount((BigDecimal) aObj[0]);
				}else{
					aJoQuoteHeader.setCostAmount(BigDecimal.ZERO);
				}
				if( aObj[1]!=null){
				aJoQuoteHeader.setQuoteAmount((BigDecimal) aObj[1]);
				}else{
					aJoQuoteHeader.setQuoteAmount(BigDecimal.ZERO);
				}
			}	
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			JobException aJobException = new JobException(
					"Exception occurred while getting Data from Quote: "
							+ excep.getMessage(), excep);
			throw aJobException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aJoQuoteHeader;
	}
}
