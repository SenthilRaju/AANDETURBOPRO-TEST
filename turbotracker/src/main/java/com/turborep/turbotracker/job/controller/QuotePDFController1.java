package com.turborep.turbotracker.job.controller;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.turborep.turbotracker.PDF.PDFException;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.JoQuotetemplateHeader;
import com.turborep.turbotracker.job.dao.joQuoteDetailMstr;
import com.turborep.turbotracker.job.dao.joQuoteTempDetailMstr;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/quotePDFController1")
public class QuotePDFController1 {
	Logger logger = Logger.getLogger(JobQuoteFormController.class);
	
	@Resource(name = "pdfService")
	private PDFService pdfService;
	@Resource(name = "userLoginService")
	private UserService userService;
	@Resource(name = "jobService")
	private JobService jobService;
	 @RequestMapping(value="/viewoldQuotePdfForm", method = RequestMethod.GET)
		public @ResponseBody String viewoldQuotePdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
																		@RequestParam(value="architectName", required= false) String theArchitect,
																		@RequestParam(value="bidDate", required= false) String theBidDate,
																		@RequestParam(value="projectName", required= false) String theProjectNamePDF,
																		@RequestParam(value="planDate", required= false) String thePlanDate,
																		@RequestParam(value="locationCity", required= false) String theCity,
																		@RequestParam(value="locationState", required= false) String theState,
																		@RequestParam(value="quoteTOName", required= false) String theQuoteName,
																		@RequestParam(value="bidderContact", required= false) String theBidderContact,
																		@RequestParam(value="joQuoteHeaderID", required= false) String theJoQuoteHeaderID,
																		@RequestParam(value="totalPrice", required= false) String theJoTotalPrice,
																		@RequestParam(value="quoteRev", required= false) String theJoQuoteRev,
																		@RequestParam(value="QuoteThru", required= false) String theQuoteThru,
																		@RequestParam(value="todayDate", required= false) String theTodayDate,
																		@RequestParam(value="jobNumber", required= false) String theJobNumber,
																		@RequestParam(value="discountAmount", required= false) String theDiscountAmount,
																		@RequestParam(value="quoteRemarks", required= false) String theQuoteRemarks,
																		@RequestParam(value="paragraphCheck", required= false) String theParagraphCheck,
																		@RequestParam(value="manufactureCheck", required= false) String theManufactureCheck,
																		@RequestParam(value="submittedBy", required = false)String theSubmittedBy,
																		@RequestParam(value="lineJobNumber", required = false)String thelineJobNumber,
																		@RequestParam(value="viewLinePDF", required = false)String theViewLinePDF,
																		@RequestParam(value="quotes_numberandtype", required = false)String quotes_numberandtype,
																		HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException, MessagingException, UserException{
		 
		 TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
		 int height=60;
		 if(aUserLoginSetting.getQuotesFooter()!=null){
				height=80;
				StringReader sr=new StringReader(aUserLoginSetting.getQuotesFooter()+"<br>");
				List<Element> objects= HTMLWorker.parseToList(sr, null);
				  for (Element element : objects){
					  height=height+10;
				  }
			}
	    Document aPDFDocument=new Document();
	   // BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.TIMES_ROMAN, BaseFont.EMBEDDED);
	    aPDFDocument.setPageSize(PageSize.LETTER);
	    Font aBoldFont =new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD,null);
	    Font aNormalFont =new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL,null);
	    Font aBoldTitleFont =new Font(FontFamily.TIMES_ROMAN, 11, Font.NORMAL,null);
	    PdfWriter aPdfWriter=PdfWriter.getInstance(aPDFDocument,aResponse.getOutputStream());
	    
	    String aHeaderText   = "QUOTES";
		 try {
			 CSSResolver cssResolver =
				     XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
				UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				
				//** Set PDF application using response **//*
				aResponse.setContentType("application/pdf");
				
				Font aNormalTitleFont =new Font(FontFamily.HELVETICA, 11, Font.NORMAL,null);
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				FontFactory.registerDirectories();
				
				
				String aJobNumber="";
				if(theJobNumber != "" && theJobNumber != null){
					 aJobNumber = theJobNumber;
					 }
				 String quotesnumber=aJobNumber;
				 if(quotes_numberandtype!=null){
					 quotesnumber=aJobNumber+"("+quotes_numberandtype+")";
				 }
				
				
				Header aFooter=new Header();
				aFooter.setJobName(theProjectNamePDF);
				aFooter.setQuoteNumber(quotesnumber);
				aPDFDocument.setMarginMirroring(false);
				aPdfWriter.setPageEvent(aFooter);
				aFooter.setHeader(new Phrase(String.format("Page: %d",aPdfWriter.getCurrentPageNumber())));
				HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
				htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			    htmlContext.autoBookmark(false);
				aPDFDocument.open();
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				String alternatetext=getalternatetextinhtml(theJoQuoteHeaderID);
				JoQuoteHeader afJoQuoteHeader =jobService.getjoQuoteHeader(JobUtil.ConvertintoInteger(theJoQuoteHeaderID));
				getHeaderInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, null, theSubmittedBy, theTodayDate,aUserLoginSetting,alternatetext);
				if(theProjectNamePDF!=null)
					theProjectNamePDF=theProjectNamePDF.replaceAll("`and`", "&");
				getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
						theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,quotes_numberandtype,therequest);
				addDetailBandColumns(aPDFDocument, theJoQuoteHeaderID, aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice,afJoQuoteHeader,therequest);
				getFooterInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, null, theSubmittedBy, theTodayDate,aUserLoginSetting);
				
				//aPDFDocument.close();
	 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,therequest);
		}
		 return null;
	 }

	 
	 @RequestMapping(value="/viewNewQuoteBidPdfForm", method = RequestMethod.GET) 
		public @ResponseBody String viewNewQuoteBidPdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
				@RequestParam(value="architectName", required= false) String theArchitect,
				@RequestParam(value="bidDate", required= false) String theBidDate,
				@RequestParam(value="projectName", required= false) String theProjectNamePDF,
				@RequestParam(value="planDate", required= false) String thePlanDate,
				@RequestParam(value="locationCity", required= false) String theCity,
				@RequestParam(value="locationState", required= false) String theState,
				@RequestParam(value="quoteTOName", required= false) String theQuoteName,
				@RequestParam(value="bidderContact", required= false) String theBidderContact,
				@RequestParam(value="joQuoteHeaderID", required= false) String theJoQuoteHeaderID1,
				@RequestParam(value="totalPrice", required= false) String theJoTotalPrice,
				@RequestParam(value="quoteRev", required= false) String theJoQuoteRev,
				@RequestParam(value="QuoteThru", required= false) String theQuoteThru,
				@RequestParam(value="todayDate", required= false) String theTodayDate,
				@RequestParam(value="jobNumber", required= false) String theJobNumber,
				@RequestParam(value="quoteTypeID", required= false) String theQuoteTypeID, 
				@RequestParam(value="quoteRev", required= false) String theQuoteRev,
				@RequestParam(value="joMasterID", required= false) String theJoMasterID,
				@RequestParam(value="paragraphCheck", required= false) String theParagraphCheck,
				@RequestParam(value="manufactureCheck", required= false) String theManufactureCheck,
				@RequestParam(value="QuotePDF", required= false) String theQuotePDFIdnti,
				@RequestParam(value="WriteorView", required= false) String WriteorView,
				@RequestParam(value="quotes_numberandtype", required = false)String quotes_numberandtype,
				HttpSession session,HttpServletResponse aResponse, HttpServletRequest theRequest) throws IOException, DocumentException, SQLException, JobException, UserException, JRException, MessagingException{
		 
			
		 TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
		 int height=60;
		 if(aUserLoginSetting.getQuotesFooter()!=null){
				height=80;
				StringReader sr=new StringReader(aUserLoginSetting.getQuotesFooter()+"<br>");
				List<Element> objects= HTMLWorker.parseToList(sr, null);
				  for (Element element : objects){
					  height=height+10;
				  }
			}
//		 Document aPDFDocument = new Document(PageSize.A4, 36, 36, 36, 36);
		 Document aPDFDocument=new Document();
		    aPDFDocument.setPageSize(PageSize.LETTER);
		 Font aBoldFont = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD));
		 Font aNormalFont = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL));
		 Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		 String aHeaderText   = "QUOTES";
		 PdfWriter aPdfWriter =null;
		 FileOutputStream fos =null;
	try {
		if(WriteorView!=null&&WriteorView.equals("write")){
			String root = theRequest.getRealPath("/");
			String fileName="Quotes#"+theJobNumber.trim()+".pdf";
         File path = new File(root + "/uploads");
         
         	if (!path.exists()) {
                 boolean status = path.mkdirs();
             }
         //aPDFDocument.close();
         File uploadedFile = new File(path + "/" + fileName);
         System.out.println("filepath"+uploadedFile.getAbsolutePath());
         String realpathhh=uploadedFile.getAbsolutePath();
			System.out.println("quote REalPath===>"+realpathhh);
			fos = new FileOutputStream(realpathhh);
			aPdfWriter=PdfWriter.getInstance(aPDFDocument, fos);
		}else{
			 aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
			
		}
		
		UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		Integer theJoQuoteHeaderID = 0;
		String aQuoteRevId = "";
		theJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
		
		//** Set PDF application using response **//*
		aResponse.setContentType("application/pdf");
		//aBoldTitleFont.setStyle(Font.UNDERLINE);
		Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		aNormalTitleFont.setStyle(Font.UNDERLINE);
		
		FontFactory.registerDirectories();
		String aJobNumber="";
		if(theJobNumber != "" && theJobNumber != null){
			 aJobNumber = theJobNumber;
			 }
		 String quotesnumber=aJobNumber;
		 if(quotes_numberandtype!=null){
			 quotesnumber=aJobNumber+"("+quotes_numberandtype+")";
		 }
		
		Header aFooter=new Header();
		aFooter.setJobName(theProjectNamePDF);
		 aFooter.setQuoteNumber(quotesnumber);
		aPDFDocument.setMarginMirroring(false);
		aPdfWriter.setPageEvent(aFooter);
		aFooter.setHeader(new Phrase(String.format("Page: %d",aPdfWriter.getCurrentPageNumber())));
		
		aPDFDocument.open();
		aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
		
		JoQuoteHeader aJoQuoteHeader = jobService.getjoQuoteAmount(theJoQuoteHeaderID);
		String alternatetext=getalternatetextinhtml(theJoQuoteHeaderID.toString());
		getHeaderInformation(theJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, null, aJoQuoteHeader.getCreatedByName(), theTodayDate,aUserLoginSetting,alternatetext);
		
		JoQuoteHeader afJoQuoteHeader =jobService.getjoQuoteHeader(theJoQuoteHeaderID);
		if(theProjectNamePDF!=null)
			theProjectNamePDF=theProjectNamePDF.replaceAll("`and`", "&");
		
		getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
				theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,quotes_numberandtype,theRequest);
		
		addDetailBandColumns(aPDFDocument, theJoQuoteHeaderID.toString(), aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
				theManufactureCheck, theTodayDate, "", theJoTotalPrice,afJoQuoteHeader,theRequest);
		
		getFooterInformation(theJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, null, aJoQuoteHeader.getCreatedByName(), theTodayDate,aUserLoginSetting);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
		}finally{
			if(fos!=null){
				fos.close();
				}
		}
		 return null;
	 }
	 
	 
	 
	 
	 
	 @RequestMapping(value="/viewNewQuoteTemplatePdfForm", method = RequestMethod.GET)
		public @ResponseBody String viewNewQuoteTemplatePdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
																		@RequestParam(value="architectName", required= false) String theArchitect,
																		@RequestParam(value="bidDate", required= false) String theBidDate,
																		@RequestParam(value="projectName", required= false) String theProjectNamePDF,
																		@RequestParam(value="planDate", required= false) String thePlanDate,
																		@RequestParam(value="locationCity", required= false) String theCity,
																		@RequestParam(value="locationState", required= false) String theState,
																		@RequestParam(value="quoteTOName", required= false) String theQuoteName,
																		@RequestParam(value="bidderContact", required= false) String theBidderContact,
																		@RequestParam(value="joQuoteHeaderID", required= false) String theJoQuoteHeaderID,
																		@RequestParam(value="totalPrice", required= false) String theJoTotalPrice,
																		@RequestParam(value="quoteRev", required= false) String theJoQuoteRev,
																		@RequestParam(value="QuoteThru", required= false) String theQuoteThru,
																		@RequestParam(value="todayDate", required= false) String theTodayDate,
																		@RequestParam(value="jobNumber", required= false) String theJobNumber,
																		@RequestParam(value="discountAmount", required= false) String theDiscountAmount,
																		@RequestParam(value="quoteRemarks", required= false) String theQuoteRemarks,
																		@RequestParam(value="paragraphCheck", required= false) String theParagraphCheck,
																		@RequestParam(value="manufactureCheck", required= false) String theManufactureCheck,
																		@RequestParam(value="submittedBy", required = false)String theSubmittedBy,
																		@RequestParam(value="lineJobNumber", required = false)String thelineJobNumber,
																		@RequestParam(value="viewLinePDF", required = false)String theViewLinePDF,
																		HttpSession session,HttpServletResponse aResponse, HttpServletRequest theRequest)  throws IOException, DocumentException, SQLException, JobException, UserException, JRException, MessagingException{
		 
	 
	 

		 TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
		 int height=60;
		 if(aUserLoginSetting.getQuotesFooter()!=null){
				height=80;
				StringReader sr=new StringReader(aUserLoginSetting.getQuotesFooter()+"<br>");
				List<Element> objects= HTMLWorker.parseToList(sr, null);
				  for (Element element : objects){
					  height=height+10;
				  }
			}

		 
		 Document aPDFDocument=new Document();
		 aPDFDocument.setPageSize(PageSize.LETTER);
		 Font aBoldFont =new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD,null);
		 Font aNormalFont =new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL,null);
		 Font aBoldTitleFont =new Font(FontFamily.TIMES_ROMAN, 11, Font.NORMAL,null);
		 String aHeaderText   = "QUOTE Template";
		 PdfWriter aPdfWriter =null;
	try {
		/*if(WriteorView!=null&&WriteorView.equals("write")){
			String root = theRequest.getRealPath("/");
			String fileName="Quotes#"+theJobNumber.trim()+".pdf";
         File path = new File(root + "/uploads");
         
         	if (!path.exists()) {
                 boolean status = path.mkdirs();
             }
         //aPDFDocument.close();
         File uploadedFile = new File(path + "/" + fileName);
         System.out.println("filepath"+uploadedFile.getAbsolutePath());
         String realpathhh=uploadedFile.getAbsolutePath();
			System.out.println("quote REalPath===>"+realpathhh);
			aPdfWriter=PdfWriter.getInstance(aPDFDocument, new FileOutputStream(realpathhh));
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
		}else{*/
			 aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
			
		//}
		
		Integer aJoQuoteHeaderID = 0;
		String aQuoteRevId = "";
		/** get Quote Header ID **/
		
		//aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
		aJoQuoteHeaderID=JobUtil.ConvertintoInteger(theJoQuoteHeaderID);
		
		UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		//** Set PDF application using response **//*
		aResponse.setContentType("application/pdf");
		//aBoldTitleFont.setStyle(Font.UNDERLINE);
		Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		aNormalTitleFont.setStyle(Font.UNDERLINE);
		
		//** Set page Number **//*
		FontFactory.registerDirectories();
		TemplateHeader aFooter=new TemplateHeader();
		
		aPDFDocument.setMarginMirroring(false);
		aPdfWriter.setPageEvent(aFooter);
		//aFooter.setHeader(new Phrase(String.format("Page: %d",aPdfWriter.getCurrentPageNumber())));
		
		aPDFDocument.open();
		
		//** Set Header Informations Using itext Paragraph **//*
//		Paragraph aParagraphHeader = null;
//		aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, null));
//		aParagraphHeader.setSpacingAfter(4f);
//		aParagraphHeader.setIndentationRight(70f);
//		aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
//		aPDFDocument.add(aParagraphHeader);
//		aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, null));
//		aPDFDocument.add(aParagraphHeader);
		
		//** HEader with Rich Text Editor Values **//*
//		aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
//			aParagraphHeader = new Paragraph();
//			aParagraphHeader.setSpacingAfter(-20f);
//			String aHtmlString =  aUserLoginSetting.getHeaderText();
//			String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
//			String aText2 = aText1.replaceAll("`and`nbsp;", " ");
//			String[] aTextSplit = aText2.split("</i></font></b></div>");
//			String aText3 = "";
//			String aText4 = aTextSplit[0];
//			for(int i = 1;i<aTextSplit.length;i++){
//			aText3 = aTextSplit[i];
//			String textnote =aText3+"<br>";
//			aText4 = aText4+textnote;
//			}
//			List<Element> p=new ArrayList();
//			StringReader aStrReader = new StringReader(aText4);
//			p =convertHtmltoElements.convert(aStrReader); 
//			for (int k = 0; k < p.size(); ++k){
//				aParagraphHeader.add(p.get(k));
//			}
//			aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
//			aParagraphHeader.setIndentationLeft(200);
//			aParagraphHeader.setIndentationRight(0);
//			aPDFDocument.add(aParagraphHeader);
//			
//			aPDFDocument.add( Chunk.NEWLINE );
		
		//** set company logo in PDF header **//*
//			Blob aBlob = aUserLoginSetting.getCompanyLogo();
//			byte[] image = aBlob.getBytes(1, (int) aBlob.length());
//			Image aLeftImage = Image.getInstance(image);
//			aLeftImage.scaleAbsolute(90,45);
//			aLeftImage.setAbsolutePosition(30f, 750f);
//			aLeftImage.scaleToFit(150, 75);
//			aPDFDocument.add(aLeftImage);
//			PdfPTable aTable = new PdfPTable(2); 
//			aTable.setWidthPercentage(100); 
		
		
		
		
		
		
		JoQuotetemplateHeader aJoQuotetemplateHeader = jobService.getJoQuotetemplateHeader(aJoQuoteHeaderID);
		getHeaderInformation_Template(theJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, null, aJoQuotetemplateHeader.getCreatedByName(), theTodayDate,aUserLoginSetting,null);
		
		if(theProjectNamePDF!=null)
			theProjectNamePDF=theProjectNamePDF.replaceAll("`and`", "&");
		
		
		getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
				theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,null,theRequest);
		
		addDetailBandColumns_template(aPDFDocument, aJoQuoteHeaderID.toString(), aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
				theManufactureCheck, theTodayDate, "0", theJoTotalPrice,theRequest);
		
		getFooterInformation(aJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, null, aJoQuotetemplateHeader.getCreatedByName(), theTodayDate,aUserLoginSetting);
		
		
		
	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
		}
		 return null;
	 }
	 
	 
	 public void addDetailBandColumns_template(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
				Font theBoldTitleFont, String theParagraphCheck, 
				String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
				String theJoTotalPrice,HttpServletRequest therequest) throws PDFException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
			Paragraph aParagraphHeader=null;
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			DecimalFormat defaultFormat = (DecimalFormat)nf;
			defaultFormat.applyPattern("$###,##0.00");
			try{
				List<String> addlist=new ArrayList<String>();
				addlist.add("QuotechkfontsizepriceYN");
				ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
				String price_pt="4";
				if(sysvariablelist!=null && sysvariablelist.get(0).getValueLong()!=null && sysvariablelist.get(0).getValueLong()==1){
					price_pt=sysvariablelist.get(0).getValueString();
							/*Font=1: 10px
							Font=2: 13px
							Font=3: 16px
							Font=4: 18px
							Font=5: 24px
							Font=6: 32px
							Font=7: 48px*/
					if(price_pt.equals("8")){
						price_pt="1";
					}else if(price_pt.equals("10")){
						price_pt="2";
					}else if(price_pt.equals("12")){
						price_pt="3";
					}else if(price_pt.equals("14")){
						price_pt="4";
					}else if(price_pt.equals("18")){
						price_pt="5";
					}
				}
				PdfPTable aPdfPTable = null;
				List<joQuoteTempDetailMstr> ajoQuoteTempDetailMstr=jobService.getjoQuoteTempDetailMstr(JobUtil.ConvertintoInteger(theJoQuoteHeaderID));
				for(joQuoteTempDetailMstr thejoQuoteTempDetailMstr:ajoQuoteTempDetailMstr){
				
					if(thejoQuoteTempDetailMstr.getType()==1){
						if(thejoQuoteTempDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						//System.out.println(Parser.unescapeEntities(thejoQuoteTempDetailMstr.getTexteditor(),false));
						String text=thejoQuoteTempDetailMstr.getTexteditor();
						if(text==null||text.length()==0){
							text="<p>&nbsp;</p>";
						}
						StringReader sr=new StringReader(text);
						List<Element> objects= convertHtmltoElements.convert(sr,therequest);//HTMLWorker.parseToList(sr, null);
						type1ElementAdd(objects,theDocument);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}else if(thejoQuoteTempDetailMstr.getType()==2){
						if(thejoQuoteTempDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						String text=thejoQuoteTempDetailMstr.getTexteditor();
						if(text==null||text.length()==0){
							text="<p>&nbsp;</p>";
						}
						StringReader sr=new StringReader(text);
						List<Element> objects= convertHtmltoElements.convert(sr,therequest);//HTMLWorker.parseToList(sr, null);
						type2ElementAdd(objects,theDocument,thejoQuoteTempDetailMstr.getQuantity(),null,theNormalFont,theBoldFont);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}
					else if(thejoQuoteTempDetailMstr.getType()==3){
						if(thejoQuoteTempDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						String sellprice=defaultFormat.format(thejoQuoteTempDetailMstr.getSellprice());
						if(thejoQuoteTempDetailMstr.getSellprice().compareTo(BigDecimal.ZERO)==0){
							sellprice="";
						}
						String text=thejoQuoteTempDetailMstr.getTexteditor();
						if(text==null||text.length()==0){
							text="<p>&nbsp;</p>";
						}
						StringReader sr=new StringReader(text);
						List<Element> objects=convertHtmltoElements.convert(sr,therequest);//HTMLWorker.parseToList(sr, null);
						type3ElementAdd(objects,theDocument,thejoQuoteTempDetailMstr.getQuantity(),sellprice,null,null,theNormalFont,theBoldFont);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}else if(thejoQuoteTempDetailMstr.getType()==4){
						if(thejoQuoteTempDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.setSpacingAfter(2.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						StringReader sr=new StringReader("<div align='right'><font size='"+price_pt+"' face='Times New Roman'  ><b>"+thejoQuoteTempDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteTempDetailMstr.getSellprice())+"</b></font></div>");
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						 for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}
					
					
					
					
					System.out.println("Id==>"+thejoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
					System.out.println("type==>"+thejoQuoteTempDetailMstr.getType());
					System.out.println("textbox==>"+thejoQuoteTempDetailMstr.getTextbox());
					System.out.println("texteditor==>"+thejoQuoteTempDetailMstr.getTexteditor());
					System.out.println("SellPrice==>"+thejoQuoteTempDetailMstr.getSellprice());
				}
				
				/*theBoldFont =new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD,null);
				JoQuotetemplateHeader theJoQuotetemplateHeader= jobService.getJoQuotetemplateHeader(JobUtil.ConvertintoInteger(theJoQuoteHeaderID));
				if(theJoQuotetemplateHeader.getPrintTotal()==1){
					float[] aWidths = {17.0f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					String printvalue="Total   "+defaultFormat.format(theJoQuotetemplateHeader.getQuoteAmount()); 
					PdfPCell cell = new PdfPCell(new Phrase(printvalue,theBoldFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
				}*/
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	 
	 
	 
	 private String getalternatetextinhtml(String theJoQuoteHeaderID) {
			// TODO Auto-generated method stub
		String returnhtmltext=jobService.getalternatetextinhtml(theJoQuoteHeaderID);
			return returnhtmltext;
		}

	 public class Header extends PdfPageEventHelper {
		
	        protected Phrase header;
	        Image total;PdfTemplate t;
	        protected String jobName;
	        protected String quoteNumber;
	        public void setHeader(Phrase header) {
	            this.header = header;
	        }
	        public void setJobName(String jobName) {
	            this.jobName = jobName;
	        }
	        
	        public void setQuoteNumber(String quoteNumber) {
	            this.quoteNumber = quoteNumber;
	        }
	        @Override
	        public void onOpenDocument(PdfWriter writer, Document document) {
	            t = writer.getDirectContent().createTemplate(36, 16);
	            try {
	                total = Image.getInstance(t);
	                total.setRole(PdfName.ARTIFACT);
	            } catch (DocumentException de) {
	                throw new ExceptionConverter(de);
	            } 
	        }
	        @Override
	        public void onStartPage(PdfWriter writer, Document document) {
	        	 /*PdfPTable table = new PdfPTable(2);
	             try {
	                 table.setWidths(new int[]{ 24, 2});
	                 table.setTotalWidth(527);
	                 table.getDefaultCell().setFixedHeight(20);
	                 table.getDefaultCell().setBorder(Rectangle.BOTTOM);
	                 table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                 table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))));
	                 PdfPCell cell = new PdfPCell(new Phrase(total, new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))));
	                 cell.setBorder(Rectangle.BOTTOM);
	                 table.addCell(cell);
	                 PdfContentByte canvas = writer.getDirectContent();
	                 canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
	                 table.writeSelectedRows(0, -1, 36, 30, canvas);
	                 canvas.endMarkedContentSequence();
	             } catch (DocumentException de) {
	                 throw new ExceptionConverter(de);
	             }*/
	        	 try {
	        	 final int currentPageNumber = writer.getCurrentPageNumber();
		            PdfContentByte canvas = writer.getDirectContentUnder();
		           
		            PdfPTable table = new PdfPTable(1);
		            table.setWidths(new int[]{2});
	                table.setTotalWidth(527);
	                table.getDefaultCell().setFixedHeight(20);
	                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                PdfPCell cell = new PdfPCell(total);
	                 cell.setBorder(Rectangle.NO_BORDER);
	                 table.addCell(cell);
	                 canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
	                 if(currentPageNumber==1){
//	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 793, 0);
//	                	 table.writeSelectedRows(0, -1, 560, 807, canvas);
	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 745, 0);
	                	 table.writeSelectedRows(0, -1, 560, 759, canvas);
	                 }else{
//	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 811, 0);
	                	// table.writeSelectedRows(0, -1, 560, 825, canvas);
	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 765, 0);
	                	 table.writeSelectedRows(0, -1, 560, 779, canvas);
	                 }
	                 canvas.endMarkedContentSequence();
	                 
	                 if(currentPageNumber==1){
	                	 return;
	                 }else{
	                 PdfPTable table1 = new PdfPTable(3);
	                 table1.setWidths(new int[]{ 20,10,5});
	                 table1.setTotalWidth(527);
	                 table1.getDefaultCell().setFixedHeight(20);
	                 table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	                 table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	                 cell = new PdfPCell(new Phrase("Project : "+jobName,new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))));
	                 cell.setBorder(Rectangle.NO_BORDER);
	                 table1.addCell(cell);
	                 cell = new PdfPCell(new Phrase("Quote : "+quoteNumber, new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))));
	                 cell.setBorder(Rectangle.NO_BORDER);
	                 table1.addCell(cell);
	                 table1.addCell(new Phrase(String.format("",new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)))));
	                 canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
	                 table1.writeSelectedRows(0, -1,40, 774, canvas);
	                 canvas.endMarkedContentSequence();
	                 }
	                 
	                 
	                 
	        	 } catch (DocumentException de) {
	                 throw new ExceptionConverter(de);
	             }
		            
	        }
	        @Override
	        public void onEndPage(PdfWriter writer, Document document) {
	        }
	        @Override
	        public void onCloseDocument(PdfWriter writer, Document document) {
	            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
	                new Phrase(String.valueOf(writer.getPageNumber() - 1),  new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))),
	                 2, 2, 0);
	        }
	    }
	 
	 public class TemplateHeader extends PdfPageEventHelper {
			
	        protected Phrase header;
	        Image total;PdfTemplate t;
	        public void setTemplateHeader(Phrase header) {
	            this.header = header;
	        }
	        
	        @Override
	        public void onOpenDocument(PdfWriter writer, Document document) {
	            t = writer.getDirectContent().createTemplate(36, 16);
	            try {
	                total = Image.getInstance(t);
	                total.setRole(PdfName.ARTIFACT);
	            } catch (DocumentException de) {
	                throw new ExceptionConverter(de);
	            } 
	        }
	        @Override
	        public void onStartPage(PdfWriter writer, Document document) {
	        	 try {
	        	 final int currentPageNumber = writer.getCurrentPageNumber();
		            PdfContentByte canvas = writer.getDirectContentUnder();
		           
		            PdfPTable table = new PdfPTable(1);
		            table.setWidths(new int[]{2});
	                table.setTotalWidth(527);
	                table.getDefaultCell().setFixedHeight(20);
	                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                PdfPCell cell = new PdfPCell(total);
	                 cell.setBorder(Rectangle.NO_BORDER);
	                 table.addCell(cell); 
	                 canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
	                 if(currentPageNumber==1){
//	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 793, 0);
//	                	 table.writeSelectedRows(0, -1, 560, 807, canvas);
	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 745, 0);
	                	 table.writeSelectedRows(0, -1, 560, 759, canvas);
	                 }else{
//	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 811, 0);
//	                 table.writeSelectedRows(0, -1, 560, 825, canvas);
	                	 ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page %d of ", writer.getPageNumber()), new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))), 559, 765, 0);
	                	 table.writeSelectedRows(0, -1, 560, 779, canvas);
	                 }
	                 canvas.endMarkedContentSequence();
	                 
	        	 } catch (DocumentException de) {
	                 throw new ExceptionConverter(de);
	             }
		            
	        }
	        @Override
	        public void onEndPage(PdfWriter writer, Document document) {
	        }
	        @Override
	        public void onCloseDocument(PdfWriter writer, Document document) {
	            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
	                new Phrase(String.valueOf(writer.getPageNumber() - 1),  new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD))),
	                 2, 2, 0);
	        }
	    }
	 public void getHeaderInformation(String theJoQuoteHeaderID,Document document,PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru,Font boldFont,Font normalFont,Font boldTitleFont, ByteArrayOutputStream baos, String thesubmittedby, String theTodayDate,TsUserSetting aUserLoginSetting,String alternatetext) throws SQLException, MalformedURLException, IOException, DocumentException{
		 Blob aBlob = aUserLoginSetting.getCompanyLogo();
			byte[] image = aBlob.getBytes(1, (int) aBlob.length());
			Image aLeftImage = Image.getInstance(image);
			aLeftImage.scaleToFit(150, 75);
			boolean alternatext_bool=false;
			PdfPTable table = new PdfPTable(2); 
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.setWidthPercentage(100);
					 PdfPCell apdfpcell=new PdfPCell(aLeftImage);
					 apdfpcell.setBorder(Rectangle.NO_BORDER);
					 table.addCell(apdfpcell);
					 apdfpcell=new PdfPCell(new Phrase(""));
					 apdfpcell.setBorder(Rectangle.NO_BORDER);
					 apdfpcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		             table.addCell(apdfpcell);
            table.completeRow();
            		 apdfpcell=new PdfPCell();
            		 apdfpcell.setBorder(Rectangle.NO_BORDER);
            		 	List<Element> p=new ArrayList();
            		 	StringReader strReader = new StringReader("<div>"+aUserLoginSetting.getHeaderText()+"</div>");
            		 	if(alternatext_bool){
            		 		strReader = new StringReader(alternatetext);
            		 	}
            		 	
						p =  HTMLWorker.parseToList(strReader, null);//convertHtmltoElements.convert(strReader);
						System.out.println("p.size()=="+p.size());
						for (int k = 0; k < p.size(); ++k){
							System.out.println("p.get(k)====>"+p.get(k));
							apdfpcell.addElement(p.get(k));
						}
		             table.addCell(apdfpcell);
		             Paragraph aParagraphHeader = new Paragraph("QUOTATION", FontFactory.getFont(FontFactory.HELVETICA,32, Font.BOLD));
		             aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
		             apdfpcell=new PdfPCell();
		             apdfpcell.setBorder(Rectangle.NO_BORDER);
		             apdfpcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		             apdfpcell.addElement(aParagraphHeader);
		             apdfpcell.setPaddingTop(-60.0f);
		             table.addCell(apdfpcell);
		    table.completeRow();
		    document.add(table);
	 }
	 
	 private void getProjectInformation(String theProjectNamePDF, String theQuoteName, String theBidderContact, String theJobNumber, String theBidDate, String theArchitect, String thePlanDate, String theJoQuoteRev, Font theBoldFont, Font theNormalFont, 
				Font theBoldTitleFont, String theState, String theCity, String theEnginner, Document theDocument, TsUserSetting theUserLoginSetting,String quotestype,HttpServletRequest therequest) throws DocumentException, IOException {
		 String aText = "";
		 String aProjectName = "";
		 String alocation="";
		 String aQuoteName = "";
		 String aBidderContact = "";
		 String aJobNumber = "";
		 String aArchitechName = "";
		 String aEngineerName = "";
		 String aBid = "";
		 String aplan = "";
		 String aJoQuoteRev = "";
		 PdfPCell aPdfCell =null;
		 boolean isStateExists, isCityExists;
		 
		 
		 /**  Project Name in Header Box  **/
		 if(theProjectNamePDF != "" && theProjectNamePDF != null){
		 String theProjectName = theProjectNamePDF.replaceAll("_", "#");
		 aProjectName = theProjectName;
		 }
		 
		 /**  Location in Header Box  **/
		 isStateExists = theState != null && theState != "";
		 isCityExists = theCity != null && theCity != "";
		 System.out.println("theCity"+theCity+"theState.toUpperCase()");
		 if(isStateExists && isCityExists)
			 alocation=theCity+", "+theState.toUpperCase(); 
		 else if(isStateExists)
			 alocation=theState.toUpperCase();
		 else
			 alocation=theCity;
		 
		 
		 /**  Quote Name in Header Box  **/
		 if(theQuoteName != "" && theQuoteName != null){
		 aQuoteName = theQuoteName;
		 }
		 
		 /**  Atten. in Header Box  **/
		 if(theBidderContact != "" && theBidderContact != null){
		 aBidderContact = theBidderContact;
		 }
		 
		 /**  Quote number in Header Box  **/
		 if(theJobNumber != "" && theJobNumber != null){
		 aJobNumber = theJobNumber;
		 }
		 String quotesnumber=aJobNumber;
		 if(quotestype!=null){
			 quotesnumber=aJobNumber+"("+quotestype+")";
		 }
		 
		 if(theBidDate != "" && theBidDate != null){
			 String[] aBidDate1 = theBidDate.split(" ");
			 if(aBidDate1.length == 2)
			 {
			 aBid = aBidDate1[0];
			 }/*else if(aBidDate1.length == 1 && aBidDate1[0] == ""){
			 aBid = "";
			 }*/else{
			 aBid = "";
			 }
			 }

			 /**  Architect in Header Box  **/
			 if(theArchitect != "" && theArchitect != null){
			 aArchitechName = theArchitect.replaceAll("and", "&");
			 }
		 
			 
			 /**  Engineer in Header Box  **/
			 if(theEnginner != "" && theEnginner != null){
			 aEngineerName = theEnginner.replaceAll("and", "&");
			 }
		

			 /**  Plan Date in Header Box  **/
			 if(thePlanDate != "" && thePlanDate != null){
			 String[] aPlanDate1 = thePlanDate.split(" ");
			 if(aPlanDate1.length == 2)
			 {
			 aplan = aPlanDate1[0];
			 }else if(aPlanDate1.length == 1 && aPlanDate1[0] == ""){
			 aplan = "";
			 }else{
			 aplan = thePlanDate;
			 }
			 }
			 

			 /**  Revision in Header Box  **/
			 if(theJoQuoteRev != "" && theJoQuoteRev != null){
			 aJoQuoteRev = theJoQuoteRev;
			 }
			 PdfPTable amainTable = new PdfPTable(1);
			 amainTable.setWidthPercentage(100);
			 PdfPTable aTable = new PdfPTable(4);
			 aTable.setWidths(new float[] { 1.3f, 3.7f, 1.3f, 3.7f});
			 //aTable.setWidthPercentage(100);
					 aPdfCell= new PdfPCell(new Phrase("PROJECT:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setPaddingTop(10f);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aProjectName,theNormalFont));
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					 aPdfCell.setBorder(Rectangle.RIGHT);
					 aPdfCell.setPaddingTop(10f);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase("BID DATE:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.LEFT);
					 aPdfCell.setPaddingTop(10f);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aBid,theNormalFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setPaddingTop(10f); 
					 aTable.addCell(aPdfCell);
			 aTable.completeRow();
					 aPdfCell= new PdfPCell(new Phrase("LOCATION:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(alocation,theNormalFont));
					 aPdfCell.setBorder(Rectangle.RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase("ARCHITECT:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.LEFT);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aArchitechName,theNormalFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aTable.addCell(aPdfCell);
			 aTable.completeRow();
					 aPdfCell= new PdfPCell(new Phrase("QUOTE TO:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aQuoteName,theNormalFont));
					 aPdfCell.setBorder(Rectangle.RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase("ENGINEER:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.LEFT);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aEngineerName,theNormalFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aTable.addCell(aPdfCell);
			 aTable.completeRow();
					 aPdfCell= new PdfPCell(new Phrase("ATTENTION:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aBidderContact,theNormalFont));
					 aPdfCell.setBorder(Rectangle.RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase("PLAN DATE:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.LEFT);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aplan,theNormalFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aTable.addCell(aPdfCell);
			 aTable.completeRow();
					 aPdfCell= new PdfPCell(new Phrase("QUOTE #:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setPaddingBottom(10f);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(quotesnumber,theNormalFont));
					 aPdfCell.setBorder(Rectangle.RIGHT);
					 aPdfCell.setPaddingBottom(10f);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase("REVISION#:",theBoldFont));
					 aPdfCell.setBorder(Rectangle.LEFT); 
					 aPdfCell.setPaddingBottom(10f);
					 aPdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					 aTable.addCell(aPdfCell);
					 aPdfCell= new PdfPCell(new Phrase(aJoQuoteRev,theNormalFont));
					 aPdfCell.setBorder(Rectangle.NO_BORDER);
					 aPdfCell.setPaddingBottom(10f);
					 aTable.addCell(aPdfCell);
			 aTable.completeRow(); 
			 PdfPCell amainPdfCell = new PdfPCell(aTable);
			 amainTable.addCell(amainPdfCell);
			 amainTable.completeRow();
			 theDocument.add(amainTable);
			 
			 
			
			 
			 
			 /***
			  * TERMS Implementation
			  * ****/
			 
			 
			 Paragraph aParagraph = null;
			//if(theUserLoginSetting.getTermsQuote() == 1){
			aParagraph = new Paragraph(10);
			String aHtmlString =  theUserLoginSetting.getTerms();
			String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
			String aText2 = aText1.replaceAll("`and`nbsp;", " ");
			String aText3 = aText2+"\n\n\n";
			System.out.println("Terms-------->"+aText3);
			List<Element> aInputString=new ArrayList<Element>();
			StringReader aStrReader = new StringReader("<div class='testterm'>"+aText3+"</div>");
			System.out.println(aStrReader);
			aInputString =convertHtmltoElements.convert(aStrReader, therequest);
			PdfPTable atermTable = new PdfPTable(1);
			atermTable.setWidthPercentage(100);
			PdfPCell termcell=new PdfPCell();
			for (int k = 0; k < aInputString.size(); ++k){
			System.out.println("Line by Line------------------->"+aInputString.get(k));
			theDocument.add(aInputString.get(k));
			}
			atermTable.addCell(termcell);
			//theDocument.add(atermTable);
//			//aParagraph.add(aText3);
//			String sHTML = "";
//			sHTML = aText3.substring(aText3.lastIndexOf("text-align"),aText3.length());
//			sHTML = sHTML.substring(sHTML.indexOf("text-align"),sHTML.indexOf(";"));
//			sHTML = sHTML.substring(sHTML.indexOf(":")+1,sHTML.length()).trim();
//			if("left".equalsIgnoreCase(sHTML))
//			aParagraph.setAlignment(Element.ALIGN_LEFT);
//			else if("right".equalsIgnoreCase(sHTML))
//			aParagraph.setAlignment(Element.ALIGN_RIGHT);
//			else if("center".equalsIgnoreCase(sHTML))
//			aParagraph.setAlignment(Element.ALIGN_CENTER);
//			else if("justify".equalsIgnoreCase(sHTML))
//			aParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
//
//			aParagraph.setSpacingAfter(5);
//			theDocument.add(aParagraph);
	 }

	 public void addDetailBandColumns(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
				Font theBoldTitleFont, String theParagraphCheck, 
				String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
				String theJoTotalPrice,JoQuoteHeader thejoQuoteheader,HttpServletRequest therequest) throws PDFException, UserException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
			Paragraph aParagraphHeader=null;
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			DecimalFormat defaultFormat = (DecimalFormat)nf;
			defaultFormat.applyPattern("$###,##0.00");
			try{
				JoQuoteHeader theJoQuoteHeader=jobService.getjoQuoteHeader(JobUtil.ConvertintoInteger(theJoQuoteHeaderID));
				List<String> addlist=new ArrayList<String>();
				addlist.add("QuotechkfontsizepriceYN");
				ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
				String price_pt="4";
				if(sysvariablelist!=null && sysvariablelist.get(0).getValueLong()!=null && sysvariablelist.get(0).getValueLong()==1){
					price_pt=sysvariablelist.get(0).getValueString();
							/*Font=1: 10px
							Font=2: 13px
							Font=3: 16px
							Font=4: 18px
							Font=5: 24px
							Font=6: 32px
							Font=7: 48px*/
					if(price_pt.equals("8")){
						price_pt="1";
					}else if(price_pt.equals("10")){
						price_pt="2";
					}else if(price_pt.equals("12")){
						price_pt="3";
					}else if(price_pt.equals("14")){
						price_pt="4";
					}else if(price_pt.equals("18")){
						price_pt="5";
					}
				}
				PdfPTable aPdfPTable = null;
				List<joQuoteDetailMstr> ajoQuoteDetailMstr=jobService.getjoQuoteDetailMstr(JobUtil.ConvertintoInteger(theJoQuoteHeaderID));
				StyleSheet styles = new StyleSheet();
				
				// styles.loadTagStyle("li", "face", "garamond");
                // styles.loadTagStyle("span", "size", "8px");
				for(joQuoteDetailMstr thejoQuoteDetailMstr:ajoQuoteDetailMstr){
					if(thejoQuoteDetailMstr.getType()==1){
						if(thejoQuoteDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always;'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						String text=thejoQuoteDetailMstr.getTexteditor();
						if(text==null||text.length()==0){
							text="<p>&nbsp;</p>";
						}
						StringReader sr=new StringReader(text);
						List<Element> objects=convertHtmltoElements.convert(sr,therequest);//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(objects,theDocument);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}else if(thejoQuoteDetailMstr.getType()==2){
						if(thejoQuoteDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						//String text="<div style='padding-right:6px;' width='20%'><span style='font-family:helvetica, serif; font-size: 10pt;'></span></div><div width='80%'>"+thejoQuoteDetailMstr.getTexteditor()+"</div>";
						String text=thejoQuoteDetailMstr.getTexteditor();
						if(text==null||text.length()==0){
							text="<p>&nbsp;</p>";
						}
						StringReader sr=new StringReader(text);
						List<Element> objects= convertHtmltoElements.convert(sr,therequest);//HTMLWorker.parseToList(sr, null);
						//type1ElementAdd(objects,theDocument);
						type2ElementAdd(objects,theDocument,thejoQuoteDetailMstr.getQuantity(),theJoQuoteHeader.getDonotQtyitem2and3(),theNormalFont,theBoldFont);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p  style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}
					else if(thejoQuoteDetailMstr.getType()==3){
						if(thejoQuoteDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						String sellprice=defaultFormat.format(thejoQuoteDetailMstr.getSellprice());
						if(thejoQuoteDetailMstr.getSellprice().compareTo(BigDecimal.ZERO)==0){
							sellprice="";
						}
						String text=thejoQuoteDetailMstr.getTexteditor();
						if(text==null||text.length()==0){
							text="<p>&nbsp;</p>";
						}
						StringReader sr=new StringReader(text);
						List<Element> objects=convertHtmltoElements.convert(sr,therequest);//HTMLWorker.parseToList(sr, null);
						type3ElementAdd(objects,theDocument,thejoQuoteDetailMstr.getQuantity(),sellprice,theJoQuoteHeader.getDonotQtyitem2and3(),theJoQuoteHeader.getShowTotPriceonly(),theNormalFont,theBoldFont);
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p  style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}else if(thejoQuoteDetailMstr.getType()==4 && !(theJoQuoteHeader.getShowTotPriceonly()==1)){
						if(thejoQuoteDetailMstr.isLinebreak()){
							List<Element> pagebrobjects=convertHtmltoElements.convert(new StringReader("<p style='page-break-after: always'><span style='display: none;'>&nbsp;</span></p>"));//HTMLWorker.parseToList(sr, styles); 
							type1ElementAdd(pagebrobjects,theDocument);
						}
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						//aPdfPTable.setSpacingAfter(2.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						StringReader sr=new StringReader("<div align='right'><font size='"+price_pt+"' face='Times New Roman' ><b>"+thejoQuoteDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteDetailMstr.getSellprice())+"</b></font></div>");
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						 for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
						
						List<Element> emptyspaceobjects=convertHtmltoElements.convert(new StringReader("<p style='line-height: 0.50;'>&nbsp;</p>"));//HTMLWorker.parseToList(sr, styles); 
						type1ElementAdd(emptyspaceobjects,theDocument);
					}
					
					System.out.println("Id==>"+thejoQuoteDetailMstr.getJoQuoteDetailMstrID());
					System.out.println("type==>"+thejoQuoteDetailMstr.getType());
					System.out.println("textbox==>"+thejoQuoteDetailMstr.getTextbox());
					System.out.println("texteditor==>"+thejoQuoteDetailMstr.getTexteditor());
					System.out.println("SellPrice==>"+thejoQuoteDetailMstr.getSellprice());
				}
				
				
				theBoldFont =new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD,null);
				if(theJoQuoteHeader.getPrintTotal()==1 || theJoQuoteHeader.getShowTotPriceonly()==1){
					float[] aWidths = {17.0f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					String printvalue="Total   "+defaultFormat.format(theJoQuoteHeader.getQuoteAmount()); 
//					aParagraphHeader = new Paragraph(printvalue );
//					aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD, null));
					PdfPCell cell = new PdfPCell(new Phrase(printvalue,theBoldFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
				}
				if(theJoQuoteHeader.getIncludeLSD()==1){
					float[] aWidths = {17.0f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					String printvalue="Lump Sum Discount   "+defaultFormat.format(theJoQuoteHeader.getLSDValue()); 
//					aParagraphHeader = new Paragraph(printvalue );
//					aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD, null));
					PdfPCell cell = new PdfPCell(new Phrase(printvalue,theBoldFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
				}
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	 
	 public void type1ElementAdd(List<Element> objects,Document theDocument) throws DocumentException{
		 PdfPTable aPdfPTable = null;
		 float[] aWidths = {16.8f};
		 Boolean ifconblock=true;
		int i=0;
		int listlength=objects.size()-1;
		System.out.println(objects.size());
		  for (Element element : objects){
			  System.out.println(element);
			  if(ifconblock){
				    aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					if(listlength==i){
						aPdfPTable.setSpacingAfter(8.0f);
					}
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					PdfPCell cell = new PdfPCell();
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
				    cell.addElement(element);
				    ifconblock=false;
				   aPdfPTable.addCell(cell);
				   //theDocument.add(aPdfPTable);
				   theDocument.add(element);
			  }else{
				  aPdfPTable = new PdfPTable(aWidths);
				  aPdfPTable.setWidthPercentage(100);
				  if(listlength==i){
						aPdfPTable.setSpacingAfter(8.0f);
					}
				  aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				  aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				  PdfPCell cell = new PdfPCell();
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.addElement(element);
					aPdfPTable.addCell(cell);
					//theDocument.add(aPdfPTable);
					 theDocument.add(element);
			  }
			  i=i+1;
		  }
		  aPdfPTable = new PdfPTable(aWidths);
		  aPdfPTable.setWidthPercentage(100);
		  aPdfPTable.setSpacingAfter(8.0f);
		  aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		  aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		  theDocument.add(aPdfPTable);
	 }
	 
	 public void type2ElementAdd(List<Element> objects,Document theDocument,String Quantity,Short qty_showornot,Font aNormalFont,Font aboldFont) throws DocumentException{
		 PdfPTable aPdfPTable = null;
		 Paragraph aParagraphHeader=null;
		 float[] aWidths = {1.5f,15.5f};
		    
			Boolean ifconblock=true;
			Boolean quantityprint_firsttime=true;
			int i=0;
			int listlength=objects.size()-1;
			  for (Element element : objects){
				  if(ifconblock){
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					/*if(listlength==i){
						aPdfPTable.setSpacingAfter(2.0f);
					}*/
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//					aParagraphHeader = new Paragraph(Quantity);
//					aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 6, Font.NORMAL, null));
					if(quantityprint_firsttime){
						quantityprint_firsttime=false;
					}else{
						Quantity="";
					}
					if(qty_showornot!=null && qty_showornot==1){
						Quantity="";
					}
					PdfPCell cell = new PdfPCell(new Phrase(Quantity,aNormalFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPaddingLeft(0);
			        cell.setPaddingRight(6);
			        cell.setPaddingTop(0);
			        cell.setPaddingBottom(0);
					aPdfPTable.addCell(cell);
					cell = new PdfPCell();
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.addElement(element);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
					  ifconblock=false;
				  }else{
					  aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						/*if(listlength==i){
							aPdfPTable.setSpacingAfter(2.0f);
						}*/
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//						aParagraphHeader = new Paragraph("");
//						aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL, null));
						if(quantityprint_firsttime){
							quantityprint_firsttime=false;
						}else{
							Quantity="";
						}
						if(qty_showornot!=null && qty_showornot==1){
							Quantity="";
						}
						PdfPCell cell = new PdfPCell(new Phrase(Quantity,aNormalFont));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(6);
				        cell.setPaddingTop(0);
				        cell.setPaddingBottom(0);
						aPdfPTable.addCell(cell);
						cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.addElement(element);
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
				  }
				  
			  }
			
	 }
	 
	 public void type3ElementAdd(List<Element> objects,Document theDocument,String Quantity,String sellprice,Short qty_showornot,Short show_totPriceOnly,Font aNormalFont,Font aboldFont) throws DocumentException{
		 PdfPTable aPdfPTable = null;
		 Paragraph aParagraphHeader=null;
		 float[] aWidths = {1.5f,13.0f,3.0f};
		 Boolean quantityprint_firsttime=true;
			Boolean ifconblock=true;
			int i=0;
			int listlength=objects.size()-1;
			  for (Element element : objects){
				  if(ifconblock){
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					/*if(listlength==i){
						aPdfPTable.setSpacingAfter(2.0f);
					}*/
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//					aParagraphHeader = new Paragraph(Quantity);
//					aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL,null));
					if(quantityprint_firsttime){
						quantityprint_firsttime=false;
					}else{
						Quantity="";
					}
					if(qty_showornot!=null && qty_showornot==1){
						Quantity="";
					}
					PdfPCell cell = new PdfPCell(new Phrase(Quantity,aNormalFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPaddingLeft(0);
			        cell.setPaddingRight(6);
			        cell.setPaddingTop(0);
			        cell.setPaddingBottom(0);
					aPdfPTable.addCell(cell);
					cell = new PdfPCell();
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.addElement(element);
					aPdfPTable.addCell(cell);
//					aParagraphHeader = new Paragraph(sellprice);
//					aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD,null));
					if(show_totPriceOnly!=null && show_totPriceOnly==1){
						sellprice="";
					}
					cell = new PdfPCell(new Phrase(sellprice,aNormalFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPaddingLeft(0);
			        cell.setPaddingRight(0);
			        cell.setPaddingTop(0);
			        cell.setPaddingBottom(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
					  ifconblock=false;
				  }else{
					  aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						/*if(listlength==i){
							aPdfPTable.setSpacingAfter(2.0f);
						}*/
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//						aParagraphHeader = new Paragraph("");
//						aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL,null));
						PdfPCell cell = new PdfPCell(new Phrase("",aNormalFont));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(6);
				        cell.setPaddingTop(2);
				        cell.setPaddingBottom(0);
						aPdfPTable.addCell(cell);
						cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.addElement(element);
						aPdfPTable.addCell(cell);
//						aParagraphHeader = new Paragraph("");
//						aParagraphHeader.setFont(new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD,null));
						cell = new PdfPCell(new Phrase("",aNormalFont));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(0);
				        cell.setPaddingTop(0);
				        cell.setPaddingBottom(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
				  }
				  
			  }
			
	 }
	 public void getFooterInformation(String theJoQuoteHeaderID, Document document, PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru, Font boldFont, Font normalFont, Font boldTitleFont, ByteArrayOutputStream baos, String thesubmittedby, String theTodayDate,TsUserSetting aUserLoginSetting) {
			try{
				
				/*PdfContentByte cb = aPdfWriter.getDirectContent();
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb.setFontAndSize(bf, 8);  
				Font helvetica8BoldBlue = new Font(FontFamily.HELVETICA, 8, Font.BOLD, null);
				ColumnText ct = new ColumnText(cb);
				Phrase myText = new Phrase("", helvetica8BoldBlue);
				ct.setSimpleColumn(myText, 25, 65, 550, 100, 10, Element.ALIGN_CENTER);
				ct.go();*/
				String aUsrNme = "";
				if(aUserBean != null){
					if(aUserBean.getFullName() != null && aUserBean.getFullName() != ""){
						aUsrNme = aUserBean.getFullName();
					}
				}
				Integer aQuoteThru;
				if(theQuoteThru == null ){
					theQuoteThru = "";
				}
				Phrase aParseText = new Phrase("SUBMITTED BY: "+thesubmittedby+"", normalFont);
				Phrase aParseText1 = new Phrase("DATED: "+theTodayDate+"", normalFont);
				Phrase aParseText2 = new Phrase("THRU ADDENDUM: - "+theQuoteThru+" - \n", normalFont);
				PdfPTable table2 = new PdfPTable(new float[]{0.4f,1.3f,1.3f,1.3f});
				table2.setTotalWidth(document.getPageSize().getWidth());
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				if(aUserLoginSetting.getQuotesFooter()!=null){
					PdfPCell cel2=new PdfPCell();
					cel2.setColspan(4);
					cel2.setPaddingLeft(55f);
					cel2.setBorderColor(BaseColor.WHITE);
					StringReader sr=new StringReader(aUserLoginSetting.getQuotesFooter()+"<br>");
					List<Element> objects= HTMLWorker.parseToList(sr, null);
					
					  for (Element element : objects){
						  cel2.addElement(element);
					  }
					  table2.setSpacingAfter(20f);
					  table2.spacingAfter();
					  table2.addCell(cel2);
					  
				}
				PdfPCell cel=new PdfPCell();
				PdfPCell cell = new PdfPCell(aParseText);
				PdfPCell cell1 = new PdfPCell(aParseText1);
				PdfPCell cell2 = new PdfPCell(aParseText2);
				cel.setBorderColor(BaseColor.WHITE);
				table2.addCell(cel);
				cell.setPaddingBottom(10f);
				cell.setPaddingTop(8f);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);
				cell1.setPaddingBottom(10f);
				cell1.setPaddingTop(8f);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell1);
				cell2.setPaddingBottom(10f);
				cell2.setPaddingTop(8f);
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell2);
				table2.setWidthPercentage(100);
				table2.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				
				 System.out.println("Current Vertical Position=====>"+aPdfWriter.getVerticalPosition(false));
				System.out.println(aPdfWriter.getVerticalPosition(false)+"==="+table2.getTotalHeight()+15);
				if(aPdfWriter.getVerticalPosition(false)<(table2.getTotalHeight()+20)){
					document.newPage();
				}
				table2.writeSelectedRows(-25, -25, -35, document.bottom()+table2.getTotalHeight(), aPdfWriter.getDirectContent());
				
				 
				document.close();
			}catch (Exception e) {
				e.printStackTrace();
				//logger.error(e.getMessage());
			}
		}
	 
	 public void getHeaderInformation_Template(String theJoQuoteHeaderID,Document document,PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru,Font boldFont,Font normalFont,Font boldTitleFont, ByteArrayOutputStream baos, String thesubmittedby, String theTodayDate,TsUserSetting aUserLoginSetting,String alternatetext) throws SQLException, MalformedURLException, IOException, DocumentException{
		 Blob aBlob = aUserLoginSetting.getCompanyLogo();
			byte[] image = aBlob.getBytes(1, (int) aBlob.length());
			Image aLeftImage = Image.getInstance(image);
			aLeftImage.scaleToFit(150, 75);
			boolean alternatext_bool=false;
			PdfPTable table = new PdfPTable(2); 
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.setWidthPercentage(100);
					 PdfPCell apdfpcell=new PdfPCell(aLeftImage);
					 apdfpcell.setBorder(Rectangle.NO_BORDER);
					 table.addCell(apdfpcell);
					 apdfpcell=new PdfPCell(new Phrase(""));
					 apdfpcell.setBorder(Rectangle.NO_BORDER);
					 apdfpcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		             table.addCell(apdfpcell);
            table.completeRow();
            		 apdfpcell=new PdfPCell();
            		 apdfpcell.setBorder(Rectangle.NO_BORDER);
            		 	List<Element> p=new ArrayList();
            		 	StringReader strReader = new StringReader("<div>"+aUserLoginSetting.getHeaderText()+"</div>");
//            		 	if(alternatext_bool){
//            		 		strReader = new StringReader(alternatetext);
//            		 	}
            		 	
						p =  HTMLWorker.parseToList(strReader, null);
						System.out.println("p.size()=="+p.size());
						for (int k = 0; k < p.size(); ++k){
							System.out.println("p.get(k)====>"+p.get(k));
							apdfpcell.addElement(p.get(k));
						}
		             table.addCell(apdfpcell);
		             Paragraph aParagraphHeader = new Paragraph("QUOTE Template", FontFactory.getFont(FontFactory.HELVETICA,20, Font.BOLD));
		             aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
		             apdfpcell=new PdfPCell();
		             apdfpcell.setBorder(Rectangle.NO_BORDER);
		             apdfpcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		             apdfpcell.addElement(aParagraphHeader);
		             apdfpcell.setPaddingTop(-60.0f);
		             table.addCell(apdfpcell);
		    table.completeRow();
		    document.add(table);
	 }
	
	 public void sendTransactionException(String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
			UserBean aUserBean=null;
			TsUserSetting objtsusersettings=null;
			try{
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			if(objtsusersettings.getItsmailYN()==1){
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId(trackingID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus(jobstatus);
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
			}
			
			if(objtsusersettings.getItslogYN()==1){
				TpUsage aTpusage=new TpUsage(new Date(), jobstatus,trackingID,"Error",aUserBean.getUserId(),"Message :: " + errors.toString());
				userService.createTpUsage(aTpusage);
			}
			
			}catch(Exception ex){
				e.printStackTrace();
			}finally{
				aUserBean=null;
				objtsusersettings=null;
			}
		}
}
