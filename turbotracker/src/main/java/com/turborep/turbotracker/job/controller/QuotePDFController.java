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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.turborep.turbotracker.PDF.PDFException;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JoQuoteHeader;
import com.turborep.turbotracker.job.dao.JoQuoteProperties;
import com.turborep.turbotracker.job.dao.JoQuoteTemplateProperties;
import com.turborep.turbotracker.job.dao.JoQuotetemplateHeader;
import com.turborep.turbotracker.job.dao.JobQuoteDetailBean;
import com.turborep.turbotracker.job.dao.Joquotecolumn;
import com.turborep.turbotracker.job.dao.joQuoteDetailMstr;
import com.turborep.turbotracker.job.dao.joQuoteTempDetailMstr;
import com.turborep.turbotracker.job.dao.testforquotes;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.job.service.PdfRtfToHTML;
import com.turborep.turbotracker.job.service.QuoteTemplateService;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.OperatingSystemInfo;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/quotePDFController")
public class QuotePDFController {

Logger logger = Logger.getLogger(JobQuoteFormController.class);
	
	@Resource(name = "pdfService")
	private PDFService pdfService;
	
	@Resource(name = "jobService")
	private JobService jobService;

	@Resource(name = "userLoginService")
	private UserService userService;
	
	@Resource(name = "quoteTemplateService")
	private QuoteTemplateService quoteTemplateService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name="sysService")
	private SysService sysservice;
	
	private String sViewLinePDF;
	
/*	*//**
	 * Method for View PDf Document
	 * @param theEnginner
	 * @param theArchitect
	 * @param theBidDate
	 * @param theProjectNamePDF
	 * @param thePlanDate
	 * @param theCity
	 * @param theState
	 * @param theQuoteName
	 * @param theBidderContact
	 * @param theJoQuoteHeaderID
	 * @param theJoTotalPrice
	 * @param theJoQuoteRev
	 * @param theQuoteThru
	 * @param theTodayDate
	 * @param theJobNumber
	 * @param theDiscountAmount
	 * @param theQuoteRemarks
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param session
	 * @param aResponse
	 * @return PDF Document
	 * @throws IOException
	 * @throws DocumentException
 * @throws MessagingException 
	 */
	
	
	@RequestMapping(value="/viewnewQuotePdfForm1", method = RequestMethod.GET)
	public @ResponseBody String viewPdfFrom(@RequestParam(value="enginnerName", required= false) String theEnginner,
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
																	HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException, MessagingException{
		
		
		if("viewLinePDF".equalsIgnoreCase(theViewLinePDF))
		{
			boolean saved = false;
			sViewLinePDF = theViewLinePDF;
			try{
				int thejoMaster =0;
				JoQuoteHeader aJoQuoteHeader = new JoQuoteHeader();
				//aJoQuoteHeader = quoteTemplateService.insertQuoteTemplates(thejoMaster, Integer.valueOf(theJoQuoteHeaderID));
				logger.info(theJoQuoteHeaderID);
				//theJoQuoteHeaderID = String.valueOf(aJoQuoteHeader.getJoQuoteTemplateHeaderId());
						
				saved = true;
			} catch (Exception excep) {
				logger.error(excep.getMessage(), excep);
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",excep,session,therequest);
			}
		}
		theProjectNamePDF = theProjectNamePDF.replace("`and`", "&");
			//** A4 page  With Document **//*
			Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			String aHeaderText   = "QUOTES";
			String aFooterText  = "QUOTATIONS VALID 30 CALENDAR DAYS FROM BID DATE. PRICES FIRM FOR RELEASE 60 DAYS FROM BID DATE " +
											"AND SUBJECT TO 1.5% PER MONTH ESCALATION THEREAFTER. MATERIAL QUOTED BASED ON INFORMATION " +
											"CONTAINED IN THE REFERENCED PORTION OF THE PLANS AND SPECIFICATIONS ONLY.  STANDARD FACTORY " +
											"CONDITIONS OF SALE APPLY.";
			ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			TsUserSetting aUserLoginSetting = null;
			try{
				//** get User Details **//*
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				
				//** Set PDF application using response **//*
				aResponse.setContentType("application/pdf");
				PdfWriter aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
				Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				//aBoldTitleFont.setStyle(Font.UNDERLINE);
				Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				//** Set page Number **//*
				HeaderFooter aFooter = null;
				aFooter= new HeaderFooter(new Phrase("Page: "), true);
				aFooter.setBorder(Rectangle.NO_BORDER);
				aFooter.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.setHeader(aFooter);
				aPDFDocument.addTitle("");
				
				//** Open PDF Document **//*
				aPDFDocument.open();
				
				//** Set Header Informations Using itext Paragraph **//*
				Paragraph aParagraphHeader = null;
				aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setSpacingAfter(4f);
				aParagraphHeader.setIndentationRight(70f);
				aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, new Color(255, 255, 255)));
				aPDFDocument.add(aParagraphHeader);
				
				//** HEader with Rich Text Editor Values **//*
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				if(aUserLoginSetting.getHeaderQuote() == 1){
					aParagraphHeader = new Paragraph();
//					aParagraphHeader.setSpacingBefore(-10);
					aParagraphHeader.setSpacingAfter(-20f);
					String aHtmlString =  aUserLoginSetting.getHeaderText();
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String[] aTextSplit = aText2.split("</i></font></b></div>");
					String aText3 = "";
					String aText4 = aTextSplit[0];
					for(int i = 1;i<aTextSplit.length;i++){
					aText3 = aTextSplit[i];
					String textnote =aText3+"<br>";
					aText4 = aText4+textnote;
					}
					ArrayList p=new ArrayList();
					StringReader aStrReader = new StringReader(aText4);
					p = HTMLWorker.parseToList(aStrReader, null);
					for (int k = 0; k < p.size(); ++k){
						aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
					}
					aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
					aParagraphHeader.setIndentationLeft(200);
					aParagraphHeader.setIndentationRight(0);
					System.out.println("aParagraphHeader===>"+aParagraphHeader);
					aPDFDocument.add(aParagraphHeader);
				}else if (aUserLoginSetting.getHeaderQuote() != 1 && aUserLoginSetting.getQuote() != 1){
					String aHeader = "";
					String aHeaderText1 = "";
					String aHeaderText2 = "";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}else{
					String aHeader = "\n";
					String aHeaderText1 = "\n";
					String aHeaderText2 = "\n";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}
				aPDFDocument.add( Chunk.NEWLINE );
				
				//** set company logo in PDF header **//*
				if(aUserLoginSetting.getQuote() == 1){
					Blob aBlob = aUserLoginSetting.getCompanyLogo();
					byte[] image = aBlob.getBytes(1, (int) aBlob.length());
					//Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					Image aLeftImage = Image.getInstance(image);
					aLeftImage.scaleAbsolute(200,100);
					aLeftImage.setAbsolutePosition(30f, 675f);
					aPDFDocument.add(aLeftImage);
					//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					PdfPTable aTable = new PdfPTable(2); 
					aTable.setWidthPercentage(100); 
				}
				
				//** get Project InformAtion **//*
				getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
														theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,quotes_numberandtype);
				
				//** get Quote Line items **//*
				if(!"viewLinePDF".equalsIgnoreCase(theViewLinePDF))
				{
					getQuotesLineItems(aPDFDocument, theJoQuoteHeaderID, aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, session);
				}
				if("viewLinePDF".equalsIgnoreCase(theViewLinePDF))
				{
					getViewQuotesLineItems(aPDFDocument, theJoQuoteHeaderID, aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, session);
				}
				
				//** get Footer Information **//*
				getFooterInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, " ", theTodayDate,aUserLoginSetting);
		
			
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,therequest);
			}
		return null;
	}

	@RequestMapping(value="/viewQuoteTemplatePdfForm", method = RequestMethod.GET)
	public @ResponseBody String viewQuoteTemplatePdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
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
																	HttpSession session,HttpServletResponse theResponse, HttpServletRequest theRequest)  throws IOException, DocumentException, SQLException, JobException, UserException, JRException, MessagingException{
		
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		JoQuoteTemplateProperties aPropertyID = null;
		String path_JRXML =null;
		String filename=null;
		Connection connection = null;
		ConnectionProvider con = null;
		try
		{
		
		// Fetching the Quotes Columns
				ArrayList<Joquotecolumn> joquotecolumnLst=sysservice. getjobQuotesColumn();
				//int qouteLst =joquotecolumnLst.size();
				
				// Fetching the Quotes Columns Properties
				UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				aPropertyID = (JoQuoteTemplateProperties) jobService.getuserquoteTemplateProperty(aUserBean.getUserId());
		
		path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/QuoteLineItemTemplatePdf.jrxml");
		
		File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
		String absolutePath = file.getAbsolutePath();
		absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
							
		if (OperatingSystemInfo.isWindows()) {
			logger.info("This is Windows");
			absolutePath=absolutePath+"\\\\QuoteLineItemTemplatePdfsubreport.jasper";
		} else if (OperatingSystemInfo.isMac()) {
			logger.info("This is Mac");
		} else if (OperatingSystemInfo.isUnix()) {
			logger.info("This is Unix or Linux");
			absolutePath=absolutePath+"/QuoteLineItemTemplatePdfsubreport.jasper";
		} else if (OperatingSystemInfo.isSolaris()) {
			logger.info("This is Solaris");
		} else {
			logger.info("Your OS is not support!!");
		}
		int lineitemheight=15;
		JasperDesign jd  = JRXmlLoader.load(path_JRXML);
		JRDesignBand band = new JRDesignBand();
		JRDesignBand detailband = new JRDesignBand();
		//
		boolean notesFullWidth=false;
		if(aPropertyID.getNotesFullWidth()==1){
			notesFullWidth=true;
		}
		
		
		band.setHeight(lineitemheight);
		band.setSplitType(SplitTypeEnum.STRETCH);
		detailband.setHeight(lineitemheight);
		
		
		
		//InlineNote detailband
		JRDesignBand indetailband = new JRDesignBand();
		indetailband.setHeight(lineitemheight);
		
		
		// Prepare Visible Column List
		List<Joquotecolumn> visibleLst =prepareQuoteTemplateVisibleLst(joquotecolumnLst,aPropertyID);
		int qouteLst =visibleLst.size();
		System.out.println("No of columns :" + qouteLst);
		
		
		
		//Show rownumber
		boolean PrintLineNumbers=false;
		if(aPropertyID.getLineNumbers()==1){
			PrintLineNumbers=true;
		}
		params.put("columnshowrownum", PrintLineNumbers);
		
		int productnotecount=jobService.getProductNoteCount(Integer.parseInt(theJoQuoteHeaderID), 1);
		boolean footerheaderprint=true;
		if(productnotecount==0){
			footerheaderprint=false;
		}
		params.put("footernotes_show", footerheaderprint);
		
		Map<String,Integer> percentlst=getpercentagebasedoncolumn1(visibleLst);
		//if rownumber checked
		if(PrintLineNumbers){
		DynamicReportbean rowbean=new DynamicReportbean();
		rowbean.setX(0);
		rowbean.setY(0);
		rowbean.setWidth(20);
		rowbean.setHeight(lineitemheight);
		rowbean.setBold(true);
		//if(percentlst.get("column8")!=null){	
			rowbean.setText("$F{rownum}");
		//}else{
		//	rowbean.setText("$V{REPORT_COUNT}");
		//}
		JRDesignTextField rowtextField = ReportService.AddColumnTextField(rowbean);
		rowtextField.setPattern("#,##0.");
		detailband.addElement(rowtextField);
		}
		int TOTAL_PAGE_WIDTH = 533;
		int SPACE_BETWEEN_COLS = 5;
		//boolean man_thereornot=getpercentagebasedoncolumn(visibleLst);
		// int columnWidth = (TOTAL_PAGE_WIDTH - (SPACE_BETWEEN_COLS * (qouteLst - 1))) / qouteLst;
		
		int xvalue=20;
		for(int i=0;i<qouteLst;i++){
			int percentage=percentlst.get(visibleLst.get(i).getColumnName());
			/*int percentage=14;
		
			if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column1")){
				 percentage=(100-((qouteLst-1)*percentage)) ;
				 if(man_thereornot){
					 percentage=percentage-6;
					}
			}
			if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column4")){
				 percentage=20 ;
			}*/
			
			System.out.println(visibleLst.get(i).getColumnName()+"visibleLst.contains==>"+visibleLst.contains("column4"));
			System.out.println("percentage==>"+percentage);
			int columnWidth=(percentage*TOTAL_PAGE_WIDTH)/100;
			System.out.println("width=="+columnWidth+"X==="+xvalue);
			DynamicReportbean bean=new DynamicReportbean();
			bean.setX(xvalue);
			bean.setY(0);
			bean.setWidth(columnWidth);
			bean.setHeight(lineitemheight);
			bean.setText(visibleLst.get(i).getColumn1label());
			bean.setPrintRepeatedValues(true);
			bean.setFontsize(8);
			bean.setBold(aPropertyID.getBoldHeader()==1?true:false);
			bean.setBold(false);
			bean.setUnderline(aPropertyID.getUnderlineHeader()==1?true:false);
			bean.setMarkup("html");
				
			JRDesignStaticText headerStaticText=ReportService.AddColumnHeader(bean);
			headerStaticText.setFontSize(11);
			band.addElement(headerStaticText);
                
			DynamicReportbean tfbean=new DynamicReportbean();
			tfbean.setX(xvalue);
			tfbean.setY(0);
			tfbean.setWidth(columnWidth);
			tfbean.setHeight(lineitemheight);
			
			//tfbean.setBold(checkColumnBold(visibleLst.get(i).getColumnName(),aPropertyID));
			//tfbean.setUnderline(checkColumnUnderLine(visibleLst.get(i).getColumnName(),aPropertyID));
			tfbean.setText(selectTemplateColumn(visibleLst.get(i).getColumnName(),footerheaderprint));
			JRDesignTextField textField =ReportService.AddColumnTextField(tfbean);
			if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column8") || visibleLst.get(i).getColumnName().equalsIgnoreCase("column8")){
				textField.setPattern("$ #,##0.00");
				//JRDesignExpression jrDesignexp = new JRDesignExpression();
				//jrDesignexp.setText("$F{Price}.compareTo( BigDecimal.ZERO)==0 ? null : $F{Price} ");
				//jrDesignexp.setValueClassName("java.math.BigDecimal");
				//textField.setPrintWhenExpression(jrDesignexp);
			}
			//JRDesignStyle normalStyle =ReportService.getNormalStyle();
			//textField.setStyle(normalStyle);
			detailband.addElement(textField);
			jd.setColumnHeader(band);
			xvalue=xvalue+columnWidth;
		}
		
		if(notesFullWidth){
			DynamicReportbean notesbean=new DynamicReportbean();
			notesbean.setX(20);
			notesbean.setY(0);
			notesbean.setWidth(533);
			notesbean.setHeight(lineitemheight);
			notesbean.setText("$F{InlineNote}");
			JRDesignTextField notestextField = ReportService.AddColumnTextField(notesbean);
			//notestextField.setPrintWhenDetailOverflows(true);
			//notestextField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			notestextField.setRemoveLineWhenBlank(true);
			indetailband.addElement(notestextField);
		}
		
		JRDesignSection overallreport=((JRDesignSection)jd.getDetailSection());
		overallreport.addBand(detailband);
		if(notesFullWidth){
		overallreport.addBand(indetailband);
		}
		filename="quotes.pdf";
		
		//UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		con = itspdfService.connectionForJasper();
				
		String aUsrNme = "";
		if(aUserBean != null){
			if(aUserBean.getFullName() != null && aUserBean.getFullName() != ""){
				aUsrNme = aUserBean.getFullName();
			}
		}
		
		Integer aQuoteThru;
		if(theQuoteThru != null && theQuoteThru != ""){
			aQuoteThru = Integer.parseInt(theQuoteThru);
		}else{
			aQuoteThru = 0;
		}
		
		boolean isStateExists = theState != null && theState != "";
		boolean isCityExists = theCity != null && theCity != "";
		String location=" ";
		if(isStateExists && isCityExists)
			location =theCity+", "+theState.toUpperCase(); 
		else if(isStateExists)
			location =theState.toUpperCase();
		else
			location =theCity;
			
		params.put("joQuoteTemplateHeaderID", theJoQuoteHeaderID);
		
		//Other Details
				params.put("project", theProjectNamePDF);
				params.put("location", location);
				params.put("quoteto", theQuoteName);
				params.put("attention", theBidderContact);
				params.put("quote", theJobNumber);
				params.put("biddate", theBidDate);
				params.put("architect", theArchitect);
				params.put("Engineer", theEnginner);
				params.put("plandate", thePlanDate);
				params.put("revision", theJoQuoteRev);
				
				//Footer details
				params.put("submittedby", aUsrNme);
				params.put("dated", theTodayDate);
				params.put("thruaddendum", aQuoteThru);
		
		
				params.put("inlinenoteshow", notesFullWidth);
				params.put("Dir", absolutePath);
				
				String remarks=null;
				List<JoQuotetemplateHeader> joquotetemplateheaderlist=jobService.getQuotesTemplateHeaderDetails(Integer.parseInt(theJoQuoteHeaderID));
				remarks=joquotetemplateheaderlist.get(0).getRemarks();
				params.put("remarks", remarks);
				
				
				
				boolean printTotal=false;
				
				//Print total
				if(aPropertyID.getPrintTotal()==1){
					printTotal=true;
				}
				params.put("total_show",printTotal);
				//String footernotes="<u><b>Notes Please Read</b></u> <br> 1.Hello  <br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test<br> 2.test";
				//params.put("FooterNotes", footernotes);
				
				
				
				TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				//String terms=aUserLoginSetting.getTerms();
				JRDesignBand titleband=(JRDesignBand) jd.getTitle();
				JRElement[] ele=titleband.getElements();
				JRDesignTextField terms=(JRDesignTextField) ele[ele.length-1];
				
				//titleband.setHeight(titleband.getHeight()+8);
				//JRElement[] ele=titleband.getElements();
				//JRDesignComponentElement elee=(JRDesignComponentElement)ele[ele.length-1];
				//elee.setHeight(ele[ele.length-1].getHeight()+8);
				//elee.setPrintWhenDetailOverflows(true);
				//System.out.println("ele[ele.length-1].getHeight()"+ele[ele.length-1].getHeight());
				//JRDesignComponentElement elee=(JRDesignComponentElement) ele[ele.length-1].get;
				//System.out.println("elee.getHeight()"+elee.getHeight());
				List<String> termslist=new ArrayList<String>();
				
				String aHtmlString =  aUserLoginSetting.getTerms();
				if(aHtmlString!=null){
				String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
				String aText2 = aText1.replaceAll("`and`nbsp;", " ");
				String aText3 = aText2+"\n\n\n";
				System.out.println("Terms-------->"+aText3);
				if(aText3.contains("text-align")){
				String sHTML = "";
				sHTML = aText3.substring(aText3.lastIndexOf("text-align"),aText3.length());
				sHTML = sHTML.substring(sHTML.indexOf("text-align"),sHTML.indexOf(";"));
				sHTML = sHTML.substring(sHTML.indexOf(":")+1,sHTML.length()).trim();
				if("left".equalsIgnoreCase(sHTML))
					terms.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
				else if("right".equalsIgnoreCase(sHTML))
					terms.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
				else if("center".equalsIgnoreCase(sHTML))
					terms.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
				else if("justify".equalsIgnoreCase(sHTML))
					terms.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
				}else{
					terms.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
				}
				}
				
				
		 connection = con.getConnection();
		ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
		return null;
	}
	
	
	private String checkNull(String property){
		if(property == null || property.equals(null))
			return " ";
		else
			return property;
	}
	/**
	 * Method For Save AS PDF Document
	 * @param theEnginner
	 * @param theArchitect
	 * @param theBidDate
	 * @param theProjectNamePDF
	 * @param thePlanDate
	 * @param theCity
	 * @param theState
	 * @param theQuoteName
	 * @param theBidderContact
	 * @param theJoQuoteHeaderID
	 * @param theJoTotalPrice
	 * @param theJoQuoteRev
	 * @param theQuoteThru
	 * @param theTodayDate
	 * @param theJobNumber
	 * @param theQuoteTypeID
	 * @param theQuoteRev
	 * @param theJoMasterID
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theQuotePDFIdnti
	 * @param session
	 * @param aResponse
	 * @return save PDF Document
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MessagingException 
	 */
	@RequestMapping(value="/SaveAsBidList", method = RequestMethod.GET)
	public @ResponseBody String saveAsBidList(@RequestParam(value="enginnerName", required= false) String theEnginner,
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
																	@RequestParam(value="quoteTypeID", required= false) String theQuoteTypeID, 
																	@RequestParam(value="quoteRev", required= false) String theQuoteRev,
																	@RequestParam(value="joMasterID", required= false) String theJoMasterID,
																	@RequestParam(value="paragraphCheck", required= false) String theParagraphCheck,
																	@RequestParam(value="manufactureCheck", required= false) String theManufactureCheck,
																	@RequestParam(value="QuotePDF", required= false) String theQuotePDFIdnti,
																	HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException, MessagingException{
			theProjectNamePDF = theProjectNamePDF.replace("`and`", "&");
			/** A4 page  With Document **/
			Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			String aHeaderText   = "QUOTE";
			String aFooterText  =""; /*"QUOTATIONS VALID 30 CALENDAR DAYS FROM BID DATE. PRICES FIRM FOR RELEASE 60 DAYS FROM BID DATE "+
											"AND SUBJECT TO 1.5% PER MONTH ESCALATION THEREAFTER. MATERIAL QUOTED BASED ON INFORMATION " +
											"CONTAINED IN THE REFERENCED PORTION OF THE PLANS AND SPECIFICATIONS ONLY.  STANDARD FACTORY " +
											"CONDITIONS OF SALE APPLY."*/
			ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			TsUserSetting aUserLoginSetting =null;
			Integer aJoQuoteHeaderID = 0;
			String aQuoteRevId = "";
			String aDiscount = "";
			FileOutputStream fos = null;
			try{
				/** GEt Quote Revision **/
				String aQuoteRev = pdfService.getQuotesRev(theQuoteTypeID, theQuoteRev, theJoMasterID);
				
				/** get Quote Header ID **/
				if(!aQuoteRev.isEmpty()){
					aQuoteRevId = "value";
					aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
				}else{
					aQuoteRevId = "NotQuote";
					aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
				}
				JoQuoteHeader aJoQuoteHeader = jobService.getjoQuoteAmount(aJoQuoteHeaderID);
				BigDecimal aQuoteAmount = aJoQuoteHeader.getQuoteAmount();
				Format format = NumberFormat.getCurrencyInstance(new Locale("en", "us"));
				theJoTotalPrice = format.format(aQuoteAmount);
				theJoQuoteHeaderID = Integer.toString(aJoQuoteHeaderID);
				
				/** get User Details **/
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				
				/** Set PDF application using response **/
				aResponse.setContentType("application/pdf");
				PdfWriter aPdfWriter = null;
				File aDir1 = new File ("/var/quotePDF/");

				if(theQuotePDFIdnti.equalsIgnoreCase("QuotePDF")){
					fos = new FileOutputStream(aDir1.getPath()+"/Quotes.pdf");
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, fos);
				}else{
					aResponse.setHeader("Content-Disposition", "attachment;filename=Quotes.pdf");
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				}
				Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
				Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
				aBoldTitleFont.setStyle(Font.UNDERLINE);
				Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				/** Set page Number **/
				HeaderFooter aFooter = null;
				aFooter= new HeaderFooter(new Phrase("Page: ", aBoldFont), true);
				aFooter.setBorder(Rectangle.NO_BORDER);
				aFooter.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.setHeader(aFooter);
				aPDFDocument.addTitle("");
				
				/** Open PDF Document **/
				aPDFDocument.open();
				
				/** Set Header Informations Using itext Paragraph **//*
				Paragraph aParagraphHeader = null;
				aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aPDFDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, new Color(255, 255, 255)));
				aPDFDocument.add(aParagraphHeader);*/
				
				/** Set Header Informations Using itext Paragraph **/
				Paragraph aParagraphHeader = null;
				aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setSpacingAfter(4f);
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER );
				aPDFDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, new Color(255, 255, 255)));
				aPDFDocument.add(aParagraphHeader);
				
				/** HEader with Rich Text Editor Values **/
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				if(aUserLoginSetting.getHeaderQuote() == 1){
					aParagraphHeader = new Paragraph();
//					aParagraphHeader.setSpacingBefore(-10);
					aParagraphHeader.setSpacingAfter(5f);
					String aHtmlString =  aUserLoginSetting.getHeaderText();
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String[] aTextSplit = aText2.split("</i></font></b></div>");
					String aText3 = "";
					String aText4 = aTextSplit[0];
					for(int i = 1;i<aTextSplit.length;i++){
					aText3 = aTextSplit[i];
					String textnote =aText3+"<br>";
					aText4 = aText4+textnote;
					}
					ArrayList p=new ArrayList();
					StringReader aStrReader = new StringReader(aText4);
					p = HTMLWorker.parseToList(aStrReader, null);
					for (int k = 0; k < p.size(); ++k){
						aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
					}
					aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
					aParagraphHeader.setIndentationLeft(200);
					aParagraphHeader.setIndentationRight(0);
					aPDFDocument.add(aParagraphHeader);
				}else if (aUserLoginSetting.getHeaderQuote() != 1 && aUserLoginSetting.getQuote() != 1){
					String aHeader = "";
					String aHeaderText1 = "";
					String aHeaderText2 = "";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}else{
					String aHeader = "\n";
					String aHeaderText1 = "\n";
					String aHeaderText2 = "\n";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}
				aPDFDocument.add( Chunk.NEWLINE );
				
				
				/** HEader with Rich Text Editor Values **/
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				if(aUserLoginSetting.getHeaderQuote() == 1){
					aParagraphHeader = new Paragraph();
					aParagraphHeader.setSpacingBefore(6);
					String aHtmlString =  aUserLoginSetting.getHeaderText();
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String aText3 = aText2+"\n\n";
					ArrayList p=new ArrayList();
					StringReader aStrReader = new StringReader(aText3);
					p = HTMLWorker.parseToList(aStrReader, null);
					for (int k = 0; k < p.size(); ++k){
						aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
					}
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aParagraphHeader.setIndentationLeft(80);
					aParagraphHeader.setIndentationRight(90);
//					aPDFDocument.add();
				}else if (aUserLoginSetting.getHeaderQuote() != 1 && aUserLoginSetting.getQuote() != 1){
					String aHeader = "";
					String aHeaderText1 = "";
					String aHeaderText2 = "";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}else{
					String aHeader = "\n";
					String aHeaderText1 = "\n";
					String aHeaderText2 = "\n";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}
				aPDFDocument.add( Chunk.NEWLINE );
				
				/** set company logo in PDF header **/
				if(aUserLoginSetting.getQuote() == 1){
					Blob aBlob = aUserLoginSetting.getCompanyLogo();
					byte[] image = aBlob.getBytes(1, (int) aBlob.length());
					//Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					Image aLeftImage = Image.getInstance(image);
					aLeftImage.scaleAbsolute(200,90);
					aLeftImage.setAbsolutePosition(30f, 660f);
					aPDFDocument.add(aLeftImage);
					//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					/*Image aRightImage = Image.getInstance(image);
					aRightImage.scaleAbsolute(97,84);
					aRightImage.setAbsolutePosition(450f, 710f) ;
					aPDFDocument.add(aRightImage);*/
					PdfPTable aTable = new PdfPTable(2); 
					aTable.setWidthPercentage(100); 
				}
				
				List<JoQuoteHeader> aJoQuoteHeaders = null;
				if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
					aJoQuoteHeaders =
							(List<JoQuoteHeader>) jobService.getQuotesHeaderDetails(Integer.parseInt(theJoQuoteHeaderID));
				}
				for(int index=0; index < aJoQuoteHeaders.size(); index++){
					aDiscount = aJoQuoteHeaders.get(index).getDiscountedPrice().toString();
				}
				
				/** get Project InformAtion **/
				getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
														theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,null);
				
				/** get Quote Line items **/
				getBidQuotesLineItems(aPDFDocument, theJoQuoteHeaderID, aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, theManufactureCheck, theTodayDate, aDiscount, theJoTotalPrice, session);
				
				/** get Footer Information **/
				getBidFooterInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, aFooterText, theTodayDate);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,therequest);
			}
			finally
			{
				fos.close();
			}
		aPDFDocument.close();
		return null;
	}

	/**
	 * Method for getting Project Information 
	 * @param theProjectNamePDF
	 * @param theQuoteName
	 * @param theBidderContact
	 * @param theJobNumber
	 * @param theBidDate
	 * @param theArchitect
	 * @param thePlanDate
	 * @param theJoQuoteRev
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theState
	 * @param theCity
	 * @param theEnginner
	 * @param theDocument
	 * @param theUserLoginSetting
	 */
	/*private void getProjectInformation(String theProjectNamePDF, String theQuoteName, String theBidderContact, String theJobNumber, String theBidDate, String theArchitect, String thePlanDate, String theJoQuoteRev, Font theBoldFont, Font theNormalFont, 
																								Font theBoldTitleFont, String theState, String theCity, String theEnginner, Document theDocument, TsUserSetting theUserLoginSetting) {
		String aText = "";
		String aProjectName = "";
		String aQuoteName = "";
		String aBidderContact = "";
		String aJobNumber = "";
		String aArchitechName = "";
		String aEngineerName = "";
		String aBid = "";
		String aplan = "";
		String aJoQuoteRev = "";
		boolean isStateExists, isCityExists;
		try{
			PdfPTable aTable = new PdfPTable(2); 
			aTable.setWidthPercentage(100); 
			PdfPCell aPdfWordProjectCell = new PdfPCell();
			Color aColor = new Color(124, 252, 0);
			aPdfWordProjectCell.setBorderColorLeft(aColor);
			
			*//**  Project Name in Header Box  **//*
			if(theProjectNamePDF != "" && theProjectNamePDF != null){
				String theProjectName = theProjectNamePDF.replaceAll("_", "#");
				aProjectName = theProjectName;
			}
			Phrase aProject = new Phrase("      PROJECT:    ", theBoldFont);
			aProject.add(new Phrase(aProjectName,theNormalFont));
			Phrase aLocation = new Phrase("   LOCATION:    ", theBoldFont); 
			isStateExists = theState != null && theState != "";
			isCityExists = theCity != null && theCity != "";
			System.out.println("theCity"+theCity+"theState.toUpperCase()");
			if(isStateExists && isCityExists)
				aLocation.add(new Phrase(theCity+", "+theState.toUpperCase(), theNormalFont)); 
			else if(isStateExists)
				aLocation.add(new Phrase(theState.toUpperCase(), theNormalFont));
			else
				aLocation.add(new Phrase(theCity, theNormalFont));
			
			*//**  Quote Name in Header Box  **//*
			if(theQuoteName != "" && theQuoteName != null){
				aQuoteName = theQuoteName;
			}
			Phrase aQuote = new Phrase("   QUOTE TO:    ", theBoldFont);
			aQuote.add(new Phrase(aQuoteName, theNormalFont));
			
			*//**  Atten. in Header Box  **//*
			if(theBidderContact != "" && theBidderContact != null){
				aBidderContact = theBidderContact;
			}
			Phrase aAttention = new Phrase("ATTENTION:    ", theBoldFont);
			aAttention.add(new Phrase(aBidderContact, theNormalFont));
			
			*//**  Quote number in Header Box  **//*
			if(theJobNumber != "" && theJobNumber != null){
				aJobNumber = theJobNumber;
			}
			Phrase aQuoteNumber = new Phrase("      QUOTE #:    ", theBoldFont);
			aQuoteNumber.add(new Phrase(aJobNumber, theNormalFont));
			
			*//**  empty value in Header Box  **//*
			Phrase aSpace = new Phrase("ATTEN", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(255, 255, 255))));
			aSpace.add(new Phrase("", theNormalFont));
			aPdfWordProjectCell.addElement(aProject);
			aPdfWordProjectCell.addElement(aLocation);
			aPdfWordProjectCell.addElement(aQuote);
			aPdfWordProjectCell.addElement(aAttention);
			aPdfWordProjectCell.addElement(aQuoteNumber);
			aPdfWordProjectCell.addElement(aSpace);
			aTable.addCell(aPdfWordProjectCell);
			PdfPCell pdfWordBidDateCell = new PdfPCell();
			aPdfWordProjectCell.setBorderColor(aColor);
			
			*//**  Bid Date in Header Box  **//*
			Phrase aBidDate = new Phrase("      BID DATE:    ", theBoldFont);
			if(theBidDate != "" && theBidDate != null){
				String[] aBidDate1 = theBidDate.split(" ");
				if(aBidDate1.length == 2)
				{
					aBid = aBidDate1[0];
				}else if(aBidDate1.length == 1 && aBidDate1[0] == ""){
					aBid = "";
				}else{
					aBid = "";
				}
			}
			aBidDate.add(new Phrase(aBid, theNormalFont));
			
			*//**  Architect in Header Box  **//*
			if(theArchitect != "" && theArchitect != null){
				aArchitechName = theArchitect.replaceAll("and", "&");
			}
			Phrase aArchitect = new Phrase(" ARCHITECT:    ", theBoldFont); 
			aArchitect.add(new Phrase(aArchitechName, theNormalFont));
			
			*//**  Architect in Header Box  **//*
			if(theEnginner != "" && theEnginner != null){
				aEngineerName = theEnginner.replaceAll("and", "&");
			}
			Phrase aEngineer = new Phrase("    ENGINEER:    ", theBoldFont);
			aEngineer.add(new Phrase(aEngineerName, theNormalFont));
			
			*//**  Plan Date in Header Box  **//*
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
			Phrase aPlanDate = new Phrase("  PLAN DATE:    ", theBoldFont);
			aPlanDate.add(new Phrase(aplan, theNormalFont));
			
			*//**  Revision in Header Box  **//*
			if(theJoQuoteRev != "" && theJoQuoteRev != null){
				aJoQuoteRev = theJoQuoteRev;
			}
			Phrase aRevision = new Phrase("       Revision #:    ", theBoldFont); 
			aRevision.add(new Phrase(aJoQuoteRev, theNormalFont)); 
			pdfWordBidDateCell.addElement(aBidDate);
			pdfWordBidDateCell.addElement(aArchitect);
			pdfWordBidDateCell.addElement(aEngineer);
			pdfWordBidDateCell.addElement(aPlanDate);
			pdfWordBidDateCell.addElement(aRevision);
			aTable.addCell(pdfWordBidDateCell);
			theDocument.add(aTable);
			theDocument.add(Chunk.NEWLINE);
			theUserLoginSetting = userService.getSingleUserSettingsDetails(1);
			Paragraph aParagraph = null;
			//if(theUserLoginSetting.getTermsQuote() == 1){
				aParagraph = new Paragraph(10);
				String aHtmlString =  theUserLoginSetting.getTerms();
				String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
				String aText2 = aText1.replaceAll("`and`nbsp;", " ");
				String aText3 = aText2+"\n\n\n";
				System.out.println("Terms-------->"+aText3);
				ArrayList aInputString=new ArrayList();
				StringReader aStrReader = new StringReader(aText3);
				aInputString = HTMLWorker.parseToList(aStrReader, null);
				for (int k = 0; k < aInputString.size(); ++k){
					System.out.println("Line by Line------------------->"+aInputString.get(k));
					aParagraph.add((com.lowagie.text.Element)aInputString.get(k));
				}
				//aParagraph.add(aText3);
				String sHTML = "";
				sHTML = aText3.substring(aText3.lastIndexOf("text-align"),aText3.length());
				sHTML = sHTML.substring(sHTML.indexOf("text-align"),sHTML.indexOf(";"));
				sHTML = sHTML.substring(sHTML.indexOf(":")+1,sHTML.length()).trim();
				if("left".equalsIgnoreCase(sHTML))
					aParagraph.setAlignment(Element.ALIGN_LEFT);
				else if("right".equalsIgnoreCase(sHTML))
					aParagraph.setAlignment(Element.ALIGN_RIGHT);
				else if("center".equalsIgnoreCase(sHTML))
					aParagraph.setAlignment(Element.ALIGN_CENTER);
				else if("justify".equalsIgnoreCase(sHTML))
					aParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
				
				//aParagraph.setAlignment(Element.ALIGN_LEFT);
				aParagraph.setSpacingAfter(5);
				theDocument.add(aParagraph);
		//	}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}*/
	
	/**
	 * Method for Quote Line Items
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @throws PDFException 
	 */
	public void getQuotesLineItems(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont, String theParagraphCheck, 
																				String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, HttpSession session) throws PDFException {
		List<JobQuoteDetailBean> aQuotesTableValue = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		JoQuoteTemplateProperties p = new JoQuoteTemplateProperties();
		JoQuoteTemplateProperties pp = new JoQuoteTemplateProperties();
		try{			
				
			p = jobService.getuserquoteTemplateProperty(aUserBean.getUserId());
			
			BeanUtils.copyProperties(p, pp);
			
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				aQuotesTableValue =
						(List<JobQuoteDetailBean>) jobService.getQuotesTemplateDetailList(Integer.parseInt(theJoQuoteHeaderID));
			}
			/** GEt Line Item in Array List **/
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==1){
				logger.info("paragraphAndManufacturerIsEnabled -1");
				paragraphAndManufacturerIsEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"");
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==0){
				logger.info("paragraphIsEnabledAndManufacturerIsDisabled -2");
				paragraphIsEnabledAndManufacturerIsDisabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,""); 
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==1){
				logger.info("paragraphIsDisabledAndManufacturerIsEnabled -3");
				paragraphIsDisabledAndManufacturerIsEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,""); 
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==0){
				logger.info("paragraphIsDisabledAndManufacturerIsDisabled -4");
				paragraphIsDisabledAndManufacturerIsDisabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==0){
				logger.info("paragraphOnlyEnabled -5");
				paragraphOnlyEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==1){
				logger.info("manufacturerOnlyEnabled -6");
				manufacturerOnlyEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==1){
				logger.info("paragraphAndManufacturerEnabled -7");
				paragraphAndManufacturerEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==0){
				logger.info("noColumnSelected -8");
				noColumnSelected(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"");
			}
			Paragraph aParagraphSUM = null;
			aParagraphSUM = new Paragraph("\n", 
					FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
			aParagraphSUM.setAlignment(Element.ALIGN_RIGHT);
			theDocument.add(aParagraphSUM);
			if(theDiscountAmount != ""){
				if(theDiscountAmount.equalsIgnoreCase("$0.00") || theDiscountAmount.equalsIgnoreCase("0.0000")){
					theDiscountAmount = "";
				}
			}
			if(theDiscountAmount != "" && theDiscountAmount != null ){
				float[] subTotWidths = {1.5f, 0.80f};
				PdfPTable aPdfPTableSubTotal = new PdfPTable(subTotWidths); 
				aPdfPTableSubTotal.setWidthPercentage(42);
				aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableSubTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableSubTotal.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableSubTotal.addCell(new Phrase("SUBTOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.TOP);
				aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.TOP);
				String aTotalPriceSub = theJoTotalPrice.replace(".0000", ".00");
				String aJoTotalPriceSub =  "$ "+aTotalPriceSub;
				aPdfPTableSubTotal.addCell(new Phrase(aJoTotalPriceSub+"\n\n", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				theDocument.add(aPdfPTableSubTotal);
				float[] Diswidths = {1.5f, 0.80f};
				PdfPTable aPdfPTableDis = new PdfPTable(Diswidths); 
				aPdfPTableDis.setWidthPercentage(42);
				aPdfPTableDis.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableDis.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableDis.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableDis.addCell(new Phrase("DISCOUNT: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				String aJoDisPrice1 = theDiscountAmount.replace("$", "$ ");
				String aJoDisPrice =aJoDisPrice1;
				aPdfPTableDis.addCell(new Phrase(aJoDisPrice+"\n\n", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				theDocument.add(aPdfPTableDis);
			}else{
				if(theJoTotalPrice != "" && theJoTotalPrice != null){
					float[] widths1 = {1.5f, 0.80f};
					PdfPTable aPdfPTable1 = new PdfPTable(widths1); 
					aPdfPTable1.setWidthPercentage(42);
					aPdfPTable1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable1.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					if(pp.getPrintTotal() == 1)
					{
						aPdfPTable1.addCell(new Phrase("TOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					else
					{
						aPdfPTable1.addCell(new Phrase(" ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					//aPdfPTable1.addCell(new Phrase("TOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					System.out.println("pp.getPrintTotal()---->"+pp.getPrintTotal());
					if(pp.getPrintTotal() == 1)
					{
						aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
						aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
						String aJoTotalPrice1 = theJoTotalPrice.replace("$", "");
						String aJoTotalPrice = "$ "+aJoTotalPrice1;
						aPdfPTable1.addCell(new Phrase(aJoTotalPrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					else
					{
						aPdfPTable1.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					//aPdfPTable1.addCell(new Phrase(aJoTotalPrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					theDocument.add(aPdfPTable1);
				}
			}
			theDocument.add( Chunk.NEWLINE );
			if(aQuotesTableValue != null){
				Paragraph aParagraphNotes = null;
				Integer aIndex = 0;
				String aTitle = "";
				String aAlbString = "";
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					String aProductNote = null;
					if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
						if(!aTitle.equalsIgnoreCase("NOTES")){
							aParagraphNotes = new Paragraph("NOTES: ", 
									FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
							aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
							theDocument.add(aParagraphNotes);
							aTitle = "NOTES"; 
						}
						aIndex = aIndex + 1;
						if(aIndex == 1){ aAlbString = "A"; } if(aIndex == 2){ aAlbString = "B"; } if(aIndex == 3){ aAlbString = "C"; } if(aIndex == 4){ aAlbString = "D"; } if(aIndex == 5){ aAlbString = "E"; }
						if(aIndex == 6){ aAlbString = "F"; } if(aIndex == 7){ aAlbString = "G"; } if(aIndex == 8){ aAlbString = "H"; } if(aIndex == 9){ aAlbString = "I"; } if(aIndex == 10){ aAlbString = "J"; }
						if(aIndex == 11){ aAlbString = "K"; } if(aIndex == 12){ aAlbString = "L"; } if(aIndex == 13){ aAlbString = "M"; } if(aIndex == 14){ aAlbString = "N"; }
						if(aIndex == 15){ aAlbString = "O"; } if(aIndex == 16){ aAlbString = "P"; } if(aIndex == 17){ aAlbString = "Q"; } if(aIndex == 18){ aAlbString = "R"; } 
						if(aIndex == 19){ aAlbString = "S"; } if(aIndex == 20){ aAlbString = "T"; } if(aIndex == 21){ aAlbString = "U"; } if(aIndex == 22){ aAlbString = "V"; }
						if(aIndex == 23){ aAlbString = "W"; } if(aIndex == 24){ aAlbString = "X"; } if(aIndex == 25){ aAlbString = "Y"; } if(aIndex == 26){ aAlbString = "Z"; }
						aProductNote = "("+aAlbString+") "+aQuotesTableValue.get(index).getProductNote()+" \n";
					}
					aParagraphNotes = new Paragraph(aProductNote, 
							FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
					aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
					theDocument.add(aParagraphNotes);
				}
			}
			theDocument.add( Chunk.NEWLINE );
		} catch (PDFException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (JobException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		}
	}

	public void getBidQuotesLineItems(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont, String theParagraphCheck, 
			String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, HttpSession session) throws PDFException {
			List<JobQuoteDetailBean> aQuotesTableValue = null;
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			JoQuoteProperties p = new JoQuoteProperties();
			JoQuoteTemplateProperties pp = new JoQuoteTemplateProperties();
			try{			
			
			p = jobService.getuserquoteProperty(aUserBean.getUserId());
			
			BeanUtils.copyProperties(p, pp);
			
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
			aQuotesTableValue =
			(List<JobQuoteDetailBean>) jobService.getQuotesDetailList(Integer.parseInt(theJoQuoteHeaderID));
			}
			/** GEt Line Item in Array List **/
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==1){
			logger.info("paragraphAndManufacturerIsEnabled -1");
			paragraphAndManufacturerIsEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes");
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==0){
			logger.info("paragraphIsEnabledAndManufacturerIsDisabled -2");
			paragraphIsEnabledAndManufacturerIsDisabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes"); 
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==1){
			logger.info("paragraphIsDisabledAndManufacturerIsEnabled -3");
			paragraphIsDisabledAndManufacturerIsEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes"); 
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==0){
			logger.info("paragraphIsDisabledAndManufacturerIsDisabled -4");
			paragraphIsDisabledAndManufacturerIsDisabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==0){
			logger.info("paragraphOnlyEnabled -5");
			paragraphOnlyEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==1){
			logger.info("manufacturerOnlyEnabled -6");
			manufacturerOnlyEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==1){
			logger.info("paragraphAndManufacturerEnabled -7");
			paragraphAndManufacturerEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==0){
			logger.info("noColumnSelected -8");
			noColumnSelected(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
			theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue, pp,"bidQuotes");
			}
			Paragraph aParagraphSUM = null;
			aParagraphSUM = new Paragraph("\n", 
			FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
			aParagraphSUM.setAlignment(Element.ALIGN_RIGHT);
			theDocument.add(aParagraphSUM);
			if(theDiscountAmount != ""){
			if(theDiscountAmount.equalsIgnoreCase("$0.00") || theDiscountAmount.equalsIgnoreCase("0.0000")){
			theDiscountAmount = "";
			}
			}
			if(theDiscountAmount != "" && theDiscountAmount != null ){
			float[] subTotWidths = {1.5f, 0.80f};
			PdfPTable aPdfPTableSubTotal = new PdfPTable(subTotWidths); 
			aPdfPTableSubTotal.setWidthPercentage(42);
			aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableSubTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
			aPdfPTableSubTotal.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			aPdfPTableSubTotal.addCell(new Phrase("SUBTOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.TOP);
			aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.TOP);
			String aTotalPriceSub = theJoTotalPrice.replace(".0000", ".00");
			String aJoTotalPriceSub =  "$ "+aTotalPriceSub;
			aPdfPTableSubTotal.addCell(new Phrase(aJoTotalPriceSub+"\n\n", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			theDocument.add(aPdfPTableSubTotal);
			float[] Diswidths = {1.5f, 0.80f};
			PdfPTable aPdfPTableDis = new PdfPTable(Diswidths); 
			aPdfPTableDis.setWidthPercentage(42);
			aPdfPTableDis.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableDis.setHorizontalAlignment(Element.ALIGN_RIGHT);
			aPdfPTableDis.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			aPdfPTableDis.addCell(new Phrase("DISCOUNT: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			String aJoDisPrice1 = theDiscountAmount.replace("$", "$ ");
			String aJoDisPrice =aJoDisPrice1;
			aPdfPTableDis.addCell(new Phrase(aJoDisPrice+"\n\n", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			theDocument.add(aPdfPTableDis);
			}else{
			if(theJoTotalPrice != "" && theJoTotalPrice != null){
			float[] widths1 = {1.5f, 0.80f};
			PdfPTable aPdfPTable1 = new PdfPTable(widths1); 
			aPdfPTable1.setWidthPercentage(42);
			aPdfPTable1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			aPdfPTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			if(pp.getPrintTotal() == 1)
			{
			aPdfPTable1.addCell(new Phrase("TOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			}
			else
			{
			aPdfPTable1.addCell(new Phrase(" ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			}
			//aPdfPTable1.addCell(new Phrase("TOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			System.out.println("pp.getPrintTotal()---->"+pp.getPrintTotal());
			if(pp.getPrintTotal() == 1)
			{
			aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
			aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
			String aJoTotalPrice1 = theJoTotalPrice.replace("$", "");
			String aJoTotalPrice = "$ "+aJoTotalPrice1;
			aPdfPTable1.addCell(new Phrase(aJoTotalPrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			}
			else
			{
			aPdfPTable1.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			}
			//aPdfPTable1.addCell(new Phrase(aJoTotalPrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			theDocument.add(aPdfPTable1);
			}
			}
			theDocument.add( Chunk.NEWLINE );
			if(aQuotesTableValue != null){
			Paragraph aParagraphNotes = null;
			Integer aIndex = 0;
			String aTitle = "";
			String aAlbString = "";
			for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
			String aProductNote = null;
			if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
			if(!aTitle.equalsIgnoreCase("NOTES")){
			aParagraphNotes = new Paragraph("NOTES: ", 
			FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
			aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
			theDocument.add(aParagraphNotes);
			aTitle = "NOTES"; 
			}
			aIndex = aIndex + 1;
			if(aIndex == 1){ aAlbString = "A"; } if(aIndex == 2){ aAlbString = "B"; } if(aIndex == 3){ aAlbString = "C"; } if(aIndex == 4){ aAlbString = "D"; } if(aIndex == 5){ aAlbString = "E"; }
			if(aIndex == 6){ aAlbString = "F"; } if(aIndex == 7){ aAlbString = "G"; } if(aIndex == 8){ aAlbString = "H"; } if(aIndex == 9){ aAlbString = "I"; } if(aIndex == 10){ aAlbString = "J"; }
			if(aIndex == 11){ aAlbString = "K"; } if(aIndex == 12){ aAlbString = "L"; } if(aIndex == 13){ aAlbString = "M"; } if(aIndex == 14){ aAlbString = "N"; }
			if(aIndex == 15){ aAlbString = "O"; } if(aIndex == 16){ aAlbString = "P"; } if(aIndex == 17){ aAlbString = "Q"; } if(aIndex == 18){ aAlbString = "R"; } 
			if(aIndex == 19){ aAlbString = "S"; } if(aIndex == 20){ aAlbString = "T"; } if(aIndex == 21){ aAlbString = "U"; } if(aIndex == 22){ aAlbString = "V"; }
			if(aIndex == 23){ aAlbString = "W"; } if(aIndex == 24){ aAlbString = "X"; } if(aIndex == 25){ aAlbString = "Y"; } if(aIndex == 26){ aAlbString = "Z"; }
			aProductNote = "("+aAlbString+") "+aQuotesTableValue.get(index).getProductNote()+" \n";
			}
			aParagraphNotes = new Paragraph(aProductNote, 
			FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
			aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
			theDocument.add(aParagraphNotes);
			}
			}
			theDocument.add( Chunk.NEWLINE );
			} catch (PDFException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
			} catch (DocumentException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
			} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
			} catch (JobException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
			} catch (Exception e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
			}
			}
		
	
	/**
	 * Method For Paragraph and Manufacture is disabled
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 */
	public void paragraphIsDisabledAndManufacturerIsDisabled(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont,
			String theParagraphCheck, String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) {
		Integer aIndexTable = 1;
		Integer aAlbaIndex = 1;
		String aAlbaString = "";
		try{
			PdfPTable aPdfPTable = null;
			/*float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
			aPdfPTable = new PdfPTable(widths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] widths = {0.3f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.65f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						//float[] aWidths = {0.3f, 1.65f, 0.80f};
						float[] aWidths = {0.1f, 1.65f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						float[] aWidths = {0.3f, 1.65f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				
					
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}



				/*else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}*/
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				//aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
				aPdfPTable.addCell(new Phrase("Qty", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);
			PdfPTable aPdfPTableValues = null;//new PdfPTable(widths); 
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] widths = {0.3f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTableValues = new PdfPTable(widths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.65f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 1.65f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = {0.3f, 1.65f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}
			/*aPdfPTableValues.setWidthPercentage(100); 
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aQuantity = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						}else{
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					if(aQuotesTableValue.get(index).getItemQuantity() != null && aQuotesTableValue.get(index).getItemQuantity() != ""){
						aQuantity = aQuotesTableValue.get(index).getItemQuantity();
					}
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
                        aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
                            aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
                            if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
                            if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
                            if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
                            if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; }
                            if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
                            if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
                            if(pp != null)
                            {
                                if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00") && aSinglePrice.trim().equalsIgnoreCase(""))
                                {
                                    aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
                                    aIndexTable = aIndexTable + 1;
                                }
                                else
                                {
                                    aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
                                }
                            }
                            //aPdfPTableValues.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                        }else{
                            aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
                            if(pp != null)
                            {
                                if(pp.getLineNumbers() ==1)
                                {
                                    aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
                                    aIndexTable = aIndexTable + 1;
                                }
                                else
                                {
                                    aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
                                }
                            }
                            /*aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
                            aIndexTable = aIndexTable + 1;*/
                        }
                    }else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{					
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);*/
					if(pp != null)
					{													
							
						if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==1)
						{
							aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
							
							//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
						}
						else if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==0)
						{
							aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
							//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
						}
						else if(pp.getUnderlineQuantity()==0 && pp.getBoldQuantity()==1)
						{
							aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
							//theBoldTitleFont.setStyle(Font.BOLD);
						}
						else
						{
							aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
							//theBoldTitleFont.setStyle(Font.NORMAL);
						}
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
						//aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					//aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));*/
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
						String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`","#");
						text1 = text1.replaceAll("`EOP`","%");
						text1 = text1.replaceAll("`FOP`","^");	
						text1 = text1.replaceAll("`GOP`amp;","&");
						text1 = text1.replaceAll("`HOP`","*");
						text1 = text1.replaceAll("`IOP`","(");
						text1 = text1.replaceAll("`JOP`",")");
						text1 = text1.replaceAll("`AOP`","!");
						text1 = text1.replaceAll("`BOP`","@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");	
						String text3 = text2+"\n\n";
						if(text3.startsWith("{"))
							text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
						if(text3.contains("<li>")){	
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p=new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(37f);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setColspan(6);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k){
							inlineNote.addElement((com.lowagie.text.Element)p.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
						}
					}
				}
				theDocument.add(aPdfPTableValues);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Method For no column selected
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 */
	public void noColumnSelected(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont,
																													String theParagraphCheck, String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) {
		Integer aIndexTable = 1;
		Integer aAlbaIndex = 1;
		String aAlbaString = "";
		try{
			/*float[] widths = {0.3f, 1.97f, 1f};
			PdfPTable aPdfPTable = new PdfPTable(widths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			PdfPTable aPdfPTable = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] widths = {0.3f, 1.97f, 1f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1f};
				aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = {0.3f, 1.97f, 1f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						//float[] aWidths = {0.3f, 1.97f, 1f};
						float[] aWidths = {0.1f, 1.97f, 1f};
						
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						float[] aWidths = {0.3f, 1.97f, 1f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				
					
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			}

			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);			
			/*PdfPTable aPdfPTableValues = new PdfPTable(widths); 
			aPdfPTableValues.setWidthPercentage(100); 
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			PdfPTable aPdfPTableValues = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] widths = {0.3f, 1.97f, 1f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1f};
				aPdfPTableValues = new PdfPTable(widths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = {0.3f, 1.97f, 1f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 1.97f, 1f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = {0.3f, 1.97f, 1f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						}else{
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					if((aQuotesTableValue.get(index).getPrice() != null) && (aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) != 0){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								logger.info("Index Calculating-1: "+aSinglePrice);
								if(pp.getLineNumbers() ==1 && ! aSinglePrice.equalsIgnoreCase("$ 0.00") && !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							//aPdfPTableValues.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
						}else{
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00") && !aSinglePrice.equals(""))
								{
									logger.info("Index Calculating-1");
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							/*aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
							aIndexTable = aIndexTable + 1;*/
						}
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{							
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);*/
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						
					}
					/*aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));*/
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
						String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`","#");
						text1 = text1.replaceAll("`EOP`","%");
						text1 = text1.replaceAll("`FOP`","^");	
						text1 = text1.replaceAll("`GOP`amp;","&");
						text1 = text1.replaceAll("`HOP`","*");
						text1 = text1.replaceAll("`IOP`","(");
						text1 = text1.replaceAll("`JOP`",")");
						text1 = text1.replaceAll("`AOP`","!");
						text1 = text1.replaceAll("`BOP`","@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");	
						String text3 = text2+"\n\n";
						if(text3.startsWith("{"))
							text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
						if(text3.contains("<li>")){	
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p=new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(52f);
						inlineNote.setColspan(6);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k){
							inlineNote.addElement((com.lowagie.text.Element)p.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
						}
					}
				}
				theDocument.add(aPdfPTableValues);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Method For Paragraph and Manufacture is disabled
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 */
	public void paragraphOnlyEnabled(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont,
																													String theParagraphCheck, String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) {
		Integer aIndexTable = 1;
		Integer aAlbaIndex = 1;
		String aAlbaString = "";
		try{
			/*float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
			PdfPTable aPdfPTable = new PdfPTable(widths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			PdfPTable aPdfPTable = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] widths = {0.3f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.65f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
//						float[] aWidths = {0.3f, 1.65f, 0.80f};
						float[] aWidths = {0.1f, 1.65f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						float[] aWidths = {0.3f, 1.65f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				
					
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}



				/*else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}*/
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				//aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
				aPdfPTable.addCell(new Phrase("Paragraph", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);
			//PdfPTable aPdfPTableValues = new PdfPTable(widths); 
			PdfPTable aPdfPTableValues = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] widths = {0.3f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTableValues = new PdfPTable(widths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.65f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 1.65f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = {0.3f, 1.65f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}

			/*aPdfPTableValues.setWidthPercentage(100); 
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aPragraph = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						}else{
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					if(aQuotesTableValue.get(index).getItemQuantity() != null && aQuotesTableValue.get(index).getItemQuantity() != ""){
						aPragraph = aQuotesTableValue.get(index).getParagraph();
					}
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00") && !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							//aPdfPTableValues.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
						}else{
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00")&& !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							/*aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
							aIndexTable = aIndexTable + 1;*/
						}
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{							
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);*/
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					if(pp != null)
					{
													
							
							if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineParagraph()==0 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aPragraph, theNormalFont));
					}
					//aPdfPTableValues.addCell(new Phrase(aPragraph, theNormalFont));
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
					}
					/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));*/
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
						String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`","#");
						text1 = text1.replaceAll("`EOP`","%");
						text1 = text1.replaceAll("`FOP`","^");	
						text1 = text1.replaceAll("`GOP`amp;","&");
						text1 = text1.replaceAll("`HOP`","*");
						text1 = text1.replaceAll("`IOP`","(");
						text1 = text1.replaceAll("`JOP`",")");
						text1 = text1.replaceAll("`AOP`","!");
						text1 = text1.replaceAll("`BOP`","@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");	
						String text3 = text2+"\n\n";
						if(text3.startsWith("{"))
							text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
						if(text3.contains("<li>")){	
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p=new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(37f);
						inlineNote.setColspan(6);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k){
							inlineNote.addElement((com.lowagie.text.Element)p.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
						}
					}
				}
				theDocument.add(aPdfPTableValues);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Method For Paragraph and Manufacture is disabled
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 */
	public void manufacturerOnlyEnabled(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont,
																													String theParagraphCheck, String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) {
		Integer aIndexTable = 1;
		Integer aAlbaIndex = 1;
		String aAlbaString = "";
		try{
			/*float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
			PdfPTable aPdfPTable = new PdfPTable(widths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			PdfPTable aPdfPTable = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] widths = {0.3f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.65f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						//float[] aWidths = {0.3f, 1.65f, 0.80f};
						float[] aWidths = {0.1f, 1.65f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						float[] aWidths = {0.3f, 1.65f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				
					
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}



				/*else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}*/
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				//aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Manufacturer", theBoldTitleFont));
			}
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);
			/*PdfPTable aPdfPTableValues = new PdfPTable(widths); 
			aPdfPTableValues.setWidthPercentage(100); 
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			PdfPTable aPdfPTableValues = null;
/*			if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] widths = {0.3f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTableValues = new PdfPTable(widths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.65f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 1.65f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = {0.3f, 1.65f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.65f, 0.80f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aPragraph = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						}else{
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					if(aQuotesTableValue.get(index).getItemQuantity() != null && aQuotesTableValue.get(index).getItemQuantity() != ""){
						aPragraph = aQuotesTableValue.get(index).getManufacturer();
					}
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00") && !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							//aPdfPTableValues.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
						}else{
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00")&& !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							/*aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
							aIndexTable = aIndexTable + 1;*/
						}
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{
							
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);*/
					if(pp != null)
					{
						
							if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineParagraph()==0 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aPragraph, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aPragraph, theNormalFont));
					}
					//aPdfPTableValues.addCell(new Phrase(aPragraph, theNormalFont));
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
					}
					/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));*/
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
						String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`","#");
						text1 = text1.replaceAll("`EOP`","%");
						text1 = text1.replaceAll("`FOP`","^");	
						text1 = text1.replaceAll("`GOP`amp;","&");
						text1 = text1.replaceAll("`HOP`","*");
						text1 = text1.replaceAll("`IOP`","(");
						text1 = text1.replaceAll("`JOP`",")");
						text1 = text1.replaceAll("`AOP`","!");
						text1 = text1.replaceAll("`BOP`","@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");	
						String text3 = text2+"\n\n";
						if(text3.startsWith("{"))
							text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
						if(text3.contains("<li>")){	
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p=new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(37f);
						inlineNote.setColspan(6);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k){
							inlineNote.addElement((com.lowagie.text.Element)p.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
						}
					}
				}
				theDocument.add(aPdfPTableValues);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	/**
	 * Method For Paragraph  is disabled  and Manufacture is enabled
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 */
	public void paragraphIsDisabledAndManufacturerIsEnabled(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont,
																											String theParagraphCheck, String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) {
		Integer aIndexTable = 1;
		Integer aAlbaIndex = 1;
		String aAlbaString = "";
		try{
			/*float[] widths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
			PdfPTable aPdfPTable = new PdfPTable(widths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			PdfPTable aPdfPTable = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] widths = {0.3f, 0.65f, 1.5f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
				aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 0.65f, 1.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						//float[] aWidths = {0.3f, 0.65f, 1.5f, 0.80f};
						float[] aWidths = {0.1f, 0.65f, 1.5f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						float[] aWidths = {0.3f, 0.65f, 1.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				
					
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}
			if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
				}
					
					/*aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);*/
				//}
				/*else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}*/
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				
				//aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
				aPdfPTable.addCell(new Phrase("Qty", theBoldTitleFont));
			
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				//aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Manufacturer", theBoldTitleFont));
			}
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			

			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);
			//PdfPTable aPdfPTableValues = new PdfPTable(widths);
			PdfPTable aPdfPTableValues = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths);
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] widths = {0.3f, 0.65f, 1.5f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths);
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
				aPdfPTableValues = new PdfPTable(widths);
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 0.65f, 1.5f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 0.65f, 1.5f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = {0.3f, 0.65f, 1.5f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 0.65f, 1.5f, 0.80f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}
			/*aPdfPTableValues.setWidthPercentage(100); 
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aQuantity = ""; 
					String aManufacturer = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						}else{
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					if(aQuotesTableValue.get(index).getItemQuantity() != null && aQuotesTableValue.get(index).getItemQuantity() != ""){
						aQuantity = aQuotesTableValue.get(index).getItemQuantity();
					}
					 if(aQuotesTableValue.get(index).getManufacturer() != null && aQuotesTableValue.get(index).getManufacturer() != ""){
						 aManufacturer = aQuotesTableValue.get(index).getManufacturer();
					 }
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00") && !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							//aPdfPTableValues.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
						}else{
							/*aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
							aIndexTable = aIndexTable + 1;*/
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1)
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
						}
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{							
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);*/
					if(pp != null)
					{
													
							
							if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineQuantity()==0 && pp.getBoldQuantity()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					/*aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);*/
					if(pp != null)
					{
													
							
							if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineManufacturer()==0 && pp.getBoldManufacturer()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					//aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
					/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));*/
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
					}
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
						String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`","#");
						text1 = text1.replaceAll("`EOP`","%");
						text1 = text1.replaceAll("`FOP`","^");	
						text1 = text1.replaceAll("`GOP`amp;","&");
						text1 = text1.replaceAll("`HOP`","*");
						text1 = text1.replaceAll("`IOP`","(");
						text1 = text1.replaceAll("`JOP`",")");
						text1 = text1.replaceAll("`AOP`","!");
						text1 = text1.replaceAll("`BOP`","@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");	
						String text3 = text2+"\n\n";
						if(text3.startsWith("{"))
							text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
						if(text3.contains("<li>")){	
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p=new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(35f);
						inlineNote.setColspan(6);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k){
							inlineNote.addElement((com.lowagie.text.Element)p.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
						}
					}
				}
				theDocument.add(aPdfPTableValues);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 *  Method For Paragraph  is enabled  and Manufacture is disabled
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 */
	public void paragraphIsEnabledAndManufacturerIsDisabled(Document theDocument, String theJoQuoteHeaderID, 
			Font theBoldFont, Font theNormalFont, Font theBoldTitleFont,String theParagraphCheck, String theManufactureCheck, 
			String theTodayDate, String theDiscountAmount, String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,
			JoQuoteTemplateProperties pp,String type) {
		Integer aIndexTable = 1;
		Integer aAlbaIndex = 1;
		String aAlbaString = "";
		try{
			/*float[] widths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
			PdfPTable aPdfPTable = new PdfPTable(widths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			PdfPTable aPdfPTable = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] widths = {0.3f, 1.10f, 1f, 0.80f};
					aPdfPTable = new PdfPTable(widths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
				aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						logger.info("Called Item,Qty,Paragraph");
						float[] aWidths = {0.3f, 1.97f, 1.10f, 1f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						logger.info("Called Qty Align Missed");
						//float[] aWidths = {0.3f, 1.10f, 1f, 0.80f};
						float[] aWidths = {0.1f, 1.10f, 1f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						logger.info("Called Qty,paragraph Checked");
						float[] aWidths = {0.08f, 0.08f, 1f,1f};
						aPdfPTable = new PdfPTable(aWidths);
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}

			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}
					
				/*aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);*/
				//}
				/*else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}*/
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				//aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
				aPdfPTable.addCell(new Phrase("Qty", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				
				//aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
				aPdfPTable.addCell(new Phrase("Paragraph", theBoldTitleFont));
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);
			PdfPTable aPdfPTableValues = null; 
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] widths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths);
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] widths = {0.3f, 1.10f, 1f, 0.80f};
					aPdfPTableValues = new PdfPTable(widths);
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] widths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
				aPdfPTableValues = new PdfPTable(widths);
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = {0.3f, 1.97f, 1.10f, 1f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 1.10f, 1f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = {0.3f, 1.10f, 1f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 1.10f, 1f, 0.80f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}
			/*aPdfPTableValues.setWidthPercentage(100); 
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aQuantity = ""; 
					String aPharagraph = "";
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						}else{
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					if(aQuotesTableValue.get(index).getItemQuantity() != null && aQuotesTableValue.get(index).getItemQuantity() != ""){
						aQuantity = aQuotesTableValue.get(index).getItemQuantity();
					}
					 if(aQuotesTableValue.get(index).getParagraph() != null && aQuotesTableValue.get(index).getParagraph() != ""){
						 aPharagraph = aQuotesTableValue.get(index).getParagraph();
					 }
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								if(pp.getLineNumbers() == 1 && aSinglePrice.equalsIgnoreCase("$ 0.00") )
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							
						}else{
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							if(pp != null)
							{
								if(pp.getLineNumbers() == 1)
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
						}
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{							
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);*/
					if(pp != null)
					{
													
							
							if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineQuantity()==0 && pp.getBoldQuantity()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					/*aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);*/
					if(pp != null)
					{
													
							
							if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineParagraph()==0 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
					}
					//aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
					/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));*/
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
					}
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
						String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`","#");
						text1 = text1.replaceAll("`EOP`","%");
						text1 = text1.replaceAll("`FOP`","^");	
						text1 = text1.replaceAll("`GOP`amp;","&");
						text1 = text1.replaceAll("`HOP`","*");
						text1 = text1.replaceAll("`IOP`","(");
						text1 = text1.replaceAll("`JOP`",")");
						text1 = text1.replaceAll("`AOP`","!");
						text1 = text1.replaceAll("`BOP`","@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");	
						String text3 = text2+"\n\n";
						if(text3.startsWith("{"))
							text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
						if(text3.contains("<li>")){	
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p=new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(35f);
						inlineNote.setColspan(6);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k){
							inlineNote.addElement((com.lowagie.text.Element)p.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
						}
					}
				}
				theDocument.add(aPdfPTableValues);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void paragraphAndManufacturerEnabled(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
			Font theBoldTitleFont, String theParagraphCheck, 
			String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
			String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) throws PDFException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
		try{
			logger.info("Print Header Or Not? :"+pp.getPrintHeader());
			/*float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
			PdfPTable aPdfPTable = new PdfPTable(aWidths);
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_LEFT);
			aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
			PdfPTable aPdfPTable = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					float[] aWidths = { 0.3f, 0.90f, 1.5f, 0.80f };
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
			}
			else
			{
				float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
				{
					float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				else
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f };
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						//float[] aWidths = { 0.3f, 0.90f, 1.5f, 0.80f };
						float[] aWidths = {0.1f, 0.50f, 0.90f, 1.5f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						float[] aWidths = { 0.28f, 0.03f, 0.9f,1.5f };
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
				
				}
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
				aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
			}
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintHeader() ==1)
				{
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				}



				/*else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}*/
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			}
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					logger.info("Paragraph Called Here");
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11,PdfCell.ALIGN_LEFT))));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				//aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
				aPdfPTable.addCell(new Phrase("Paragraph", theBoldTitleFont));
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			if(pp != null)
			{
				if(pp.getPrintHeader() ==1)
				{
					logger.info("Manufacturer Called Here");
					if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
					}
					else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11,PdfCell.ALIGN_LEFT))));
					}
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				}
				else
				{
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				//aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
			}
			else
			{
				aPdfPTable.addCell(new Phrase("Manufacturer", theBoldTitleFont));
			}
			
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			if(pp != null)
			{
				
				if(pp.getPrintPrice() ==1)
				{
					if(pp.getPrintHeader() ==1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					//aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
				}
					
			}
			else
				aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
			theDocument.add(aPdfPTable);
			
			/*PdfPTable aPdfPTableValues = new PdfPTable(aWidths);
			aPdfPTableValues.setWidthPercentage(100);
			aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
			PdfPTable aPdfPTableValues = null;
			/*if(pp != null)
			{
				if(pp.getPrintItem() ==1)
				{
					float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else
				{
					float[] aWidths = { 0.3f, 0.90f, 1.5f, 0.80f };
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
			}
			else
			{
				float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}*/
			if(pp != null)
			{
				if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
				{
					float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f, 0.80f };
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				else 
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
					{
						float[] aWidths = { 0.3f, 1.97f, 0.90f, 1.5f };
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = { 0.3f, 0.90f, 1.5f, 0.80f };
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else
					{
						float[] aWidths = { 0.3f, 0.90f, 1.5f };
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
				}
				
			}
			else
			{
				float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
				aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
			}
			if (theJoQuoteHeaderID != null && theJoQuoteHeaderID != "") {
				aQuotesTableValue = (List<JobQuoteDetailBean>) jobService
						.getQuotesTemplateDetailList(Integer
								.parseInt(theJoQuoteHeaderID));
					for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aPharagraph = "";
					String aManufacturer = ""; 
					String aPrice = null;
					String aSinglePrice = "";
							if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
							if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							aAlbaIndex = aAlbaIndex +1;
						} else {
							aProduct = aQuotesTableValue.get(index)
									.getProduct();
						}
					}
					if (aQuotesTableValue.get(index).getParagraph() != null
							&& aQuotesTableValue.get(index).getParagraph() != "") {
						aPharagraph = aQuotesTableValue.get(index)
								.getParagraph();
					}
					if (aQuotesTableValue.get(index).getManufacturer() != null
							&& aQuotesTableValue.get(index).getManufacturer() != "") {
						aManufacturer = aQuotesTableValue.get(index)
								.getManufacturer();
					}
					Format format = NumberFormat
							.getCurrencyInstance(new Locale("en", "in"));
					if (aQuotesTableValue.get(index).getPrice() != null) {
						aPrice = aQuotesTableValue.get(index).getPrice()
								.toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(
								aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.",
								"");
						aSinglePrice = "$ " + aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					logger.info("GetPrice: "+aQuotesTableValue.get(index).getPrice());
					
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0))
						{
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00") && !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							//aPdfPTableValues.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
						} else {
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							if(pp != null)
							{
								if(pp.getLineNumbers() ==1 && !aSinglePrice.equalsIgnoreCase("$ 0.00")&& !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							/*aPdfPTableValues.addCell(new Phrase(aIndexTable
									.toString() + ". ", FontFactory.getFont(
									FontFactory.HELVETICA, 11, Font.BOLD)));
							aIndexTable = aIndexTable + 1;*/
						}
					} else {
						aPdfPTableValues.getDefaultCell()
								.setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues
								.addCell(new Phrase(" ", theNormalFont));
					}
					if (aSinglePrice.equalsIgnoreCase("$ 0.00")) {
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{
							logger.info("Its Called Item");
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					/*aPdfPTableValues
							.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_LEFT);*/
					if(pp != null)
					{
						
							if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineParagraph()==0 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
					}
					/*aPdfPTableValues.addCell(new Phrase(aPharagraph,
							theNormalFont));*/
					if(pp != null)
					{					
							
							if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineManufacturer()==0 && pp.getBoldManufacturer()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
					}
					/*aPdfPTableValues.addCell(new Phrase(aManufacturer,
							theNormalFont));*/
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{							
							logger.info("Its Called Single Price");
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
						//aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					else
					{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
					}
					/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(
							Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice,
							theNormalFont));*/
					String aInlineNote = aQuotesTableValue.get(index)
							.getInlineNote();
					if (aInlineNote != null) {
						String text1 = aInlineNote.replaceAll("`and`amp;", "&")
								.replaceAll("andnbsp;", " ");
						text1 = text1.replaceAll("`COP`", "#");
						text1 = text1.replaceAll("`EOP`", "%");
						text1 = text1.replaceAll("`FOP`", "^");
						text1 = text1.replaceAll("`GOP`amp;", "&");
						text1 = text1.replaceAll("`HOP`", "*");
						text1 = text1.replaceAll("`IOP`", "(");
						text1 = text1.replaceAll("`JOP`", ")");
						text1 = text1.replaceAll("`AOP`", "!");
						text1 = text1.replaceAll("`BOP`", "@");
						text1 = text1.replaceAll("`GOP`nbsp;", "  ");
						String text2 = text1.replaceAll("`and`nbsp;", " ");
						String text3 = text2 + "\n\n";
						if (text3.startsWith("{"))
							text3 = PdfRtfToHTML.rtfToHtml(new StringReader(
									text3));
						if (text3.contains("<li>")) {
							text3 = text3.replaceAll("<br>", "");

						}
						ArrayList p = new ArrayList();
						StringReader strReader = new StringReader(text3);
						p = HTMLWorker.parseToList(strReader, null);
						PdfPCell inlineNote = new PdfPCell();
						inlineNote.setPaddingRight(70f);
						inlineNote.setPaddingLeft(35f);
						inlineNote.setColspan(6);
						inlineNote.setPaddingTop(-7f);
						inlineNote.setBorder(Rectangle.NO_BORDER);
						for (int k = 0; k < p.size(); ++k) {
							inlineNote.addElement((com.lowagie.text.Element) p
									.get(k));
						}
						aPdfPTableValues.addCell(inlineNote);
					}
				}
			}
			theDocument.add(aPdfPTableValues);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		}
}
	/**
	 * Method For Paragraph and Manufacture is enabled 
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @param aQuotesTableValue
	 * @throws PDFException 
	 */
	public void paragraphAndManufacturerIsEnabled(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
													Font theBoldTitleFont, String theParagraphCheck, 
													String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
													String theJoTotalPrice, List<JobQuoteDetailBean> aQuotesTableValue,JoQuoteTemplateProperties pp,String type) throws PDFException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
			try{
				
				/*float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
				PdfPTable aPdfPTable = new PdfPTable(aWidths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("", theBoldTitleFont));*/
				PdfPTable aPdfPTable = null;
				if(pp != null)
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==1)
					{
						float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
					}
					else
					{
						if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
						{
							float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
						}
						else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
						{
							float[] aWidths = {0.1f, 0.50f, 0.90f, 1.5f, 0.80f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							//aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
						}
						else
						{
							float[] aWidths = {0.3f, 0.50f, 0.90f, 1.5f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
						}
					
						
					}
				}
				else
				{
					float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
				}
				
				if(pp != null)
				{
					if(pp.getPrintItem() ==1 && pp.getPrintHeader() == 1)
					{
						//aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
						/*if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
							
							//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
						}
						else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE|Font.NORMAL))));
							//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
						}
						else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
							//theBoldTitleFont.setStyle(Font.BOLD);
						}
						else
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL))));
							//theBoldTitleFont.setStyle(Font.NORMAL);
						}*/
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Item", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						
					}
					else
					{
						aPdfPTable.addCell(new Phrase("", theBoldTitleFont));
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
				}
				else
				{
					aPdfPTable.addCell(new Phrase("Item", theBoldTitleFont));
					
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				
				
				if(pp != null)
				{
					/*if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==1)
					{
						aPdfPTable.addCell(new Phrase("Qty.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						
						//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
					}
					else if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==0)
					{
						aPdfPTable.addCell(new Phrase("Qty.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE|Font.NORMAL))));
						//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
					}
					else if(pp.getUnderlineQuantity()==0 && pp.getBoldQuantity()==1)
					{
						aPdfPTable.addCell(new Phrase("Qty.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						//theBoldTitleFont.setStyle(Font.BOLD);
					}
					else
					{
						aPdfPTable.addCell(new Phrase("Qty.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL))));
						//theBoldTitleFont.setStyle(Font.NORMAL);
					}*/
					if(pp.getPrintHeader() == 1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Qty", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
					
					}
					else
						aPdfPTable.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						
				}
				else
					aPdfPTable.addCell(new Phrase("Qty", theBoldTitleFont));
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				if(pp != null)
				{
					/*if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==0)
					{
						aPdfPTable.addCell(new Phrase("Paragraph.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE|Font.NORMAL))));
					}
					else if(pp.getUnderlineParagraph()==0 && pp.getBoldParagraph()==1)
					{
						aPdfPTable.addCell(new Phrase("Paragraph.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else
					{
						aPdfPTable.addCell(new Phrase("Paragraph.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL))));
					}*/
					if(pp.getPrintHeader() == 1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Paragraph", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
					
					}
					else
						aPdfPTable.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					
						
				}
				else
					aPdfPTable.addCell(new Phrase("Paragraph", theBoldTitleFont));
				if(pp != null)
				{
					/*if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
					else if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==0)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE|Font.NORMAL))));
					}
					else if(pp.getUnderlineManufacturer()==0 && pp.getBoldManufacturer()==1)
					{
						aPdfPTable.addCell(new Phrase("Manufacturer.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
					}
					else	
					{
						aPdfPTable.addCell(new Phrase("Manufacturer.", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL))));
					}*/
					if(pp.getPrintHeader() == 1)
					{
						if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
						{
							aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
						}
						else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
						}
						else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
						{
							aPdfPTable.addCell(new Phrase("Manufacturer", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
						}
					
					}
					else
						aPdfPTable.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
						
				}
				else
				{
					aPdfPTable.addCell(new Phrase("Manufacturer", theBoldTitleFont));
				}
				
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				if(pp != null)
				{
					if(pp.getPrintPrice() ==1)
					{
						if(pp.getPrintHeader() == 1)
						{
							if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 1)
							{
								aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
							}
							else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 1)
							{
								aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD))));
							}
							else if(pp.getUnderlineHeader() == 1 && pp.getBoldHeader() == 0)
							{
								aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.UNDERLINE))));
							}
							else if(pp.getUnderlineHeader() == 0 && pp.getBoldHeader() == 0)
							{
								aPdfPTable.addCell(new Phrase("Price", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11))));
							}
						
						}
						else
							aPdfPTable.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD|Font.UNDERLINE))));
					}
						
				}
				else
					aPdfPTable.addCell(new Phrase("Price", theBoldTitleFont));
				theDocument.add(aPdfPTable);
				PdfPTable aPdfPTableValues = null;
				if(pp != null)
				{
					if(pp.getPrintItem() ==1 && pp.getPrintPrice() == 1)
					{
						float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}
					else 
					{
						if(pp.getPrintItem() ==1 && pp.getPrintPrice() ==0)
						{
							float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f};
							aPdfPTableValues = new PdfPTable(aWidths); 
							aPdfPTableValues.setWidthPercentage(100); 
							aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
						}
						else if(pp.getPrintItem() ==0 && pp.getPrintPrice() ==1)
						{
							float[] aWidths = {0.3f, 0.50f, 0.90f, 1.5f, 0.80f};
							aPdfPTableValues = new PdfPTable(aWidths); 
							aPdfPTableValues.setWidthPercentage(100); 
							aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
						}
						else
						{
							float[] aWidths = {0.3f, 0.50f, 0.90f, 1.5f};
							aPdfPTableValues = new PdfPTable(aWidths); 
							aPdfPTableValues.setWidthPercentage(100); 
							aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
						}
					}
					/*else
					{
						
						float[] aWidths = {0.3f, 0.50f, 0.90f, 1.5f, 0.80f};
						aPdfPTableValues = new PdfPTable(aWidths); 
						aPdfPTableValues.setWidthPercentage(100); 
						aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
					}*/
				}
				else
				{
					float[] aWidths = {0.3f, 1.97f, 0.50f, 0.90f, 1.5f, 0.80f};
					aPdfPTableValues = new PdfPTable(aWidths); 
					aPdfPTableValues.setWidthPercentage(100); 
					aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
				}
				/*PdfPTable aPdfPTableValues = new PdfPTable(aWidths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableValues.getDefaultCell().setPaddingTop(3f);*/
				if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
					if(type.equals("bidQuotes")){
						aQuotesTableValue =
								(List<JobQuoteDetailBean>) jobService.getQuotesDetailList(Integer.parseInt(theJoQuoteHeaderID));
					}else{
					aQuotesTableValue =
							(List<JobQuoteDetailBean>) jobService.getQuotesTemplateDetailList(Integer.parseInt(theJoQuoteHeaderID));
					}
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aQuantity = ""; 
					String aPharagraph = "";
					String aManufacturer = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							aProduct = aQuotesTableValue.get(index).getProduct()+" "+"("+aAlbaString+")";
							System.out.println("aAlbaIndex----->"+aAlbaIndex+"---->aProduct---"+aProduct);
							aAlbaIndex = aAlbaIndex +1;
						}else{
							System.out.println("Else");
								aProduct = aQuotesTableValue.get(index).getProduct();
						}
					}
					if(aQuotesTableValue.get(index).getItemQuantity() != null && aQuotesTableValue.get(index).getItemQuantity() != ""){
						aQuantity = aQuotesTableValue.get(index).getItemQuantity();
					}
					 if(aQuotesTableValue.get(index).getParagraph() != null && aQuotesTableValue.get(index).getParagraph() != ""){
						 aPharagraph = aQuotesTableValue.get(index).getParagraph();
					 }
					 if(aQuotesTableValue.get(index).getManufacturer() != null && aQuotesTableValue.get(index).getManufacturer() != ""){
						 aManufacturer = aQuotesTableValue.get(index).getManufacturer();
					 }
					 Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
					 if(aQuotesTableValue.get(index).getPrice() == null)
					 {
						 aQuotesTableValue.get(index).setPrice(new BigDecimal(0.00));
					 }
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toPlainString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						String aCurrensyPrice = format.format(new BigDecimal(aSpiltPrice[0]));
						String aReplaceCurrency = aCurrensyPrice.replace("Rs.", "");
						aSinglePrice = "$ "+aReplaceCurrency;
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if((aQuotesTableValue.get(index).getPrice() == null) || ((aQuotesTableValue.get(index).getPrice().compareTo(new BigDecimal(0))) == 0)){
							aPdfPTableValues.getDefaultCell().setPaddingTop(2f);
							if(aAlbaIndex == 1){ aAlbaString = "A"; } if(aAlbaIndex == 2){ aAlbaString = "B"; } if(aAlbaIndex == 3){ aAlbaString = "C"; } if(aAlbaIndex == 4){ aAlbaString = "D"; } if(aAlbaIndex == 5){ aAlbaString = "E"; }
							if(aAlbaIndex == 6){ aAlbaString = "F"; } if(aAlbaIndex == 7){ aAlbaString = "G"; } if(aAlbaIndex == 8){ aAlbaString = "H"; } if(aAlbaIndex == 9){ aAlbaString = "I"; } if(aAlbaIndex == 10){ aAlbaString = "J"; }
							if(aAlbaIndex == 11){ aAlbaString = "K"; } if(aAlbaIndex == 12){ aAlbaString = "L"; } if(aAlbaIndex == 13){ aAlbaString = "M"; } if(aAlbaIndex == 14){ aAlbaString = "N"; }
							 if(aAlbaIndex == 15){ aAlbaString = "O"; } if(aAlbaIndex == 16){ aAlbaString = "P"; } if(aAlbaIndex == 17){ aAlbaString = "Q"; } if(aAlbaIndex == 18){ aAlbaString = "R"; } 
							if(aAlbaIndex == 19){ aAlbaString = "S"; } if(aAlbaIndex == 20){ aAlbaString = "T"; } if(aAlbaIndex == 21){ aAlbaString = "U"; } if(aAlbaIndex == 22){ aAlbaString = "V"; }
							if(aAlbaIndex == 23){ aAlbaString = "W"; } if(aAlbaIndex == 24){ aAlbaString = "X"; } if(aAlbaIndex == 25){ aAlbaString = "Y"; } if(aAlbaIndex == 26){ aAlbaString = "Z"; }
							if(pp != null)
							{
								if(pp.getLineNumbers() == 1 && !aSinglePrice.equalsIgnoreCase("$ 0.00")&& !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							//aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
						}else{
							aPdfPTableValues.getDefaultCell().setPaddingTop(3f);
							if(pp != null)
							{
								if(pp.getLineNumbers() == 1 && !aSinglePrice.equalsIgnoreCase("$ 0.00")&& !aSinglePrice.equals(""))
								{
									aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
									aIndexTable = aIndexTable + 1;
								}
								else
								{
									aPdfPTableValues.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
								}
							}
							/*aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
							System.out.println("Else Price aIndexTable---"+aIndexTable);
							aIndexTable = aIndexTable + 1;*/
						}
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", theNormalFont));
					}
					if(aSinglePrice.equalsIgnoreCase("$ 0.00")){
						System.out.println("Single Price $ 0.00=====>"+aSinglePrice);
						aSinglePrice = " ";
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(pp != null)
					{
						if(pp.getPrintItem() ==1)
						{							
							
							if(pp.getUnderlineItem()==1 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineItem()==1 && pp.getBoldItem()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineItem()==0 && pp.getBoldItem()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					
					/*aPdfPTableValues.addCell(new Phrase(aProduct, theNormalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);*/
					if(pp != null)
					{
													
							
							if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineQuantity()==1 && pp.getBoldQuantity()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineQuantity()==0 && pp.getBoldQuantity()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aQuantity, theNormalFont));
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					}
					
					if(pp != null)
					{
													
							
							if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineParagraph()==1 && pp.getBoldParagraph()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineParagraph()==0 && pp.getBoldParagraph()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aPharagraph, theNormalFont));
					}
					if(pp != null)
					{
													
							
							if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlineManufacturer()==1 && pp.getBoldManufacturer()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlineManufacturer()==0 && pp.getBoldManufacturer()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
						
					}
					else
					{
						aPdfPTableValues.addCell(new Phrase(aManufacturer, theNormalFont));
					}					
					
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					if(pp != null)
					{
						if(pp.getPrintPrice() ==1)
						{			
							System.out.println("aSinglePrice ------>"+aSinglePrice);
							
							if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD|Font.UNDERLINE))));
								
								//theBoldTitleFont.setStyle(Font.BOLD|Font.UNDERLINE);
							}
							else if(pp.getUnderlinePrice()==1 && pp.getBoldPrice()==0)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE|Font.NORMAL))));
								//theBoldTitleFont.setStyle(Font.UNDERLINE|Font.NORMAL);
							}
							else if(pp.getUnderlinePrice()==0 && pp.getBoldPrice()==1)
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
								//theBoldTitleFont.setStyle(Font.BOLD);
							}
							else
							{
								aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
								//theBoldTitleFont.setStyle(Font.NORMAL);
							}
							
							/*aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							aPdfPTableValues.getDefaultCell().setPaddingLeft(300f);*/
						}
						/*else
						{
							aPdfPTableValues.addCell(new Phrase("", theNormalFont));
							aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						}*/
					}
					else
					{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTableValues.addCell(new Phrase(aSinglePrice, theNormalFont));
					}
					
					String aInlineNote  = aQuotesTableValue.get(index).getInlineNote();
					if(aInlineNote!= null){
					String text1  = aInlineNote.replaceAll("`and`amp;", "&").replaceAll("andnbsp;", " ");
					text1 = text1.replaceAll("`COP`","#");
					text1 = text1.replaceAll("`EOP`","%");
					text1 = text1.replaceAll("`FOP`","^");	
					text1 = text1.replaceAll("`GOP`amp;","&");
					text1 = text1.replaceAll("`HOP`","*");
					text1 = text1.replaceAll("`IOP`","(");
					text1 = text1.replaceAll("`JOP`",")");
					text1 = text1.replaceAll("`AOP`","!");
					text1 = text1.replaceAll("`BOP`","@");
					text1 = text1.replaceAll("`GOP`nbsp;", "  ");
					String text2 = text1.replaceAll("`and`nbsp;", " ");	
					String text3 = text2+"\n\n";
					if(text3.startsWith("{"))
						text3=PdfRtfToHTML.rtfToHtml(new StringReader(text3));
					if(text3.contains("<li>")){	
						text3 = text3.replaceAll("<br>", "");

					}
					ArrayList p=new ArrayList();
					StringReader strReader = new StringReader(text3);
					p = HTMLWorker.parseToList(strReader, null);
					PdfPCell inlineNote = new PdfPCell();
					inlineNote.setPaddingRight(70f);
					inlineNote.setPaddingLeft(35f);
					inlineNote.setColspan(6);
					inlineNote.setPaddingTop(-7f);
					inlineNote.setBorder(Rectangle.NO_BORDER);
					for (int k = 0; k < p.size(); ++k){
						inlineNote.addElement((com.lowagie.text.Element)p.get(k));
					}
					aPdfPTableValues.addCell(inlineNote);
					}
				}
			}
			theDocument.add(aPdfPTableValues);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PDFException(e.getMessage(), e);
		}
	}

	/**
	 * Getting Footer Information
	 * @param theJoQuoteHeaderID
	 * @param document
	 * @param aPdfWriter
	 * @param aUserBean
	 * @param theQuoteThru
	 * @param boldFont
	 * @param normalFont
	 * @param boldTitleFont
	 * @param baos
	 * @param aFooterText
	 * @param theTodayDate
	 */
	public void getFooterInformation(String theJoQuoteHeaderID, Document document, PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru, Font boldFont, Font normalFont, Font boldTitleFont, ByteArrayOutputStream baos, String thesubmittedby, String theTodayDate,TsUserSetting aUserLoginSetting) {
		try{
//			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
//				aJoQuoteHeaders =
//						(List<JoQuotetemplateHeader>) jobService.getQuotesTemplateHeaderDetails(Integer.parseInt(theJoQuoteHeaderID));
//			for(int index=0; index < aJoQuoteHeaders.size(); index++){
//				aRemarks = aJoQuoteHeaders.get(index).getRemarks();
//				}
//			}
//			if(aRemarks != "" && aRemarks!= null){
//				Paragraph aParagraphRemarks = null;
//				aParagraphRemarks = new Paragraph("REMARKS: ", 
//						FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
//				aParagraphRemarks.setAlignment(Element.ALIGN_LEFT);
//				document.add(aParagraphRemarks);
//				aParagraphRemarks = new Paragraph(aRemarks, 
//						FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
//				aParagraphRemarks.setAlignment(Element.ALIGN_LEFT);
//				document.add(aParagraphRemarks);
//			}
//			document.add( Chunk.NEWLINE );document.add( Chunk.NEWLINE );
//			PdfPTable footertable = new PdfPTable(new float[]{4.2f});
//			footertable.setTotalWidth(document.getPageSize().getWidth());
//			PdfPCell footercell = new PdfPCell();
//			footercell.setBorder(Rectangle.NO_BORDER);
//			footercell.setPadding(0);
//			
//			PdfContentByte fb1 = aPdfWriter.getDirectContent();
//			if(aUserLoginSetting.getQuotesFooter()!=null){
//			StringReader sr=new StringReader(aUserLoginSetting.getQuotesFooter());
//			List<Element> objects= HTMLWorker.parseToList(sr, null);
//			  for (Element element : objects){
//				  footercell.addElement(element);
//			  }
//			  footertable.addCell(footercell);
//			  footertable.setWidthPercentage(80);
//			  footertable.setHorizontalAlignment(Element.ALIGN_CENTER);
//			  footertable.writeSelectedRows(0, -1, 34, 803, fb1);
//			}
			
			
			
			
			PdfContentByte cb = aPdfWriter.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.setFontAndSize(bf, 8);  
			Font helvetica8BoldBlue = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK);
			ColumnText ct = new ColumnText(cb);
			Phrase myText = new Phrase("", helvetica8BoldBlue);
			ct.setSimpleColumn(myText, 25, 65, 550, 100, 10, Element.ALIGN_CENTER);
			ct.go();
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
			int height=60;
			PdfContentByte cb1 = aPdfWriter.getDirectContent();
			Phrase aParseText = new Phrase("SUBMITTED BY: "+thesubmittedby+"", normalFont);
			Phrase aParseText1 = new Phrase("DATED: "+theTodayDate+"", normalFont);
			Phrase aParseText2 = new Phrase("THRU ADDENDUM: - "+theQuoteThru+" - \n", normalFont);
			PdfPTable table2 = new PdfPTable(new float[]{0.4f,1.3f,1.3f,1.3f});
			table2.setTotalWidth(document.getPageSize().getWidth());
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			if(aUserLoginSetting.getQuotesFooter()!=null){
				height=80;
				PdfPCell cel2=new PdfPCell();
				cel2.setColspan(4);
				cel2.setPaddingLeft(55f);
				cel2.setBorderColor(Color.WHITE);
				StringReader sr=new StringReader(aUserLoginSetting.getQuotesFooter()+"<br>");
				List<Element> objects= HTMLWorker.parseToList(sr, null);
				
				  for (Element element : objects){
					  cel2.addElement(element);
					  ArrayList<Chunk> obj= element.getChunks();
					  float paraHeight = 0.0f;
					  for(Chunk chk:obj){
						  paraHeight= chk.getWidthPoint()/515;
						  for(float i=paraHeight;i>0;){
							  height=height+12;
							  i=i-1;
							  document.add(Chunk.NEWLINE);
						  }
					  }
					 
					  
				  }
				  table2.setSpacingAfter(20f);
				  table2.spacingAfter();
				  table2.addCell(cel2);
				  
			}
			PdfPCell cel=new PdfPCell();
			PdfPCell cell = new PdfPCell(aParseText);
			PdfPCell cell1 = new PdfPCell(aParseText1);
			PdfPCell cell2 = new PdfPCell(aParseText2);
			cel.setBorderColor(Color.WHITE);
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
			
			
			table2.writeSelectedRows(-25, -25, -35, height, cb1);
			
			/* ColumnText.showTextAligned(aPdfWriter.getDirectContent(),
	                    Element.ALIGN_CENTER, new Phrase(String.format("page %d",aPdfWriter.getPageNumber())),
	                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);*/
			 
			document.close();
		}catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	public void getBidFooterInformation(String theJoQuoteHeaderID, Document document, PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru, Font boldFont, Font normalFont, Font boldTitleFont, ByteArrayOutputStream baos, String aFooterText, String theTodayDate) {
		String aRemarks = "";
		List<JoQuoteHeader> aJoQuoteHeaders = null;
		try{
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				aJoQuoteHeaders =
						(List<JoQuoteHeader>) jobService.getQuotesHeaderDetails(Integer.parseInt(theJoQuoteHeaderID));
			for(int index=0; index < aJoQuoteHeaders.size(); index++){
				aRemarks = aJoQuoteHeaders.get(index).getRemarks();
				}
			}
			if(aRemarks != "" && aRemarks!= null){
				Paragraph aParagraphRemarks = null;
				aParagraphRemarks = new Paragraph("REMARKS: ", 
						FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
				aParagraphRemarks.setAlignment(Element.ALIGN_LEFT);
				document.add(aParagraphRemarks);
				aParagraphRemarks = new Paragraph(aRemarks, 
						FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphRemarks.setAlignment(Element.ALIGN_LEFT);
				document.add(aParagraphRemarks);
			}
			document.add( Chunk.NEWLINE );document.add( Chunk.NEWLINE );
			PdfContentByte cb = aPdfWriter.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.setFontAndSize(bf, 8);  
			Font helvetica8BoldBlue = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK);
			ColumnText ct = new ColumnText(cb);
			Phrase myText = new Phrase(aFooterText, helvetica8BoldBlue);
			ct.setSimpleColumn(myText, 25, 65, 550, 100, 10, Element.ALIGN_CENTER);
			ct.go();
			String aUsrNme = "";
			if(aUserBean != null){
				if(aUserBean.getFullName() != null && aUserBean.getFullName() != ""){
					aUsrNme = aUserBean.getFullName();
				}
			}
			Integer aQuoteThru;
			if(theQuoteThru != null && theQuoteThru != ""){
				aQuoteThru = Integer.parseInt(theQuoteThru);
			}else{
				aQuoteThru = 0;
			}
			PdfContentByte cb1 = aPdfWriter.getDirectContent();
			Phrase aParseText = new Phrase("SUBMITTED BY: "+aUsrNme+"", normalFont);
			Phrase aParseText1 = new Phrase("DATED: "+theTodayDate+"", normalFont);
			Phrase aParseText2 = new Phrase("THRU ADDENDUM: - "+aQuoteThru+" - \n", normalFont);
			PdfPTable table2 = new PdfPTable(new float[]{0.4f,1.3f,1.3f,1.3f});
			table2.setTotalWidth(document.getPageSize().getWidth());
			PdfPCell cel=new PdfPCell();
			PdfPCell cell = new PdfPCell(aParseText);
			PdfPCell cell1 = new PdfPCell(aParseText1);
			PdfPCell cell2 = new PdfPCell(aParseText2);
			cel.setBorderColor(Color.WHITE);
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
			table2.writeSelectedRows(-25, -25, -35, 60, cb1);
			document.close();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
/*public void printQuotes(HttpSession theSession){
		JoQuoteProperty aProperty = null;
		FastReportBuilder drb = new FastReportBuilder();
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		Integer aUserId = aUserBean.getUserId();
		ar.com.fdvs.dj.domain.constants.Font afont = new ar.com.fdvs.dj.domain.constants.Font();
		try {
			afont.setFontName("arial");
			afont.setFontSize(16);
			afont.setUnderline(true);
			Style aStyle = new Style();
			aStyle.setFont(afont);
			drb.setTitle("BidList");
			drb.setTitleStyle(aStyle);
		aProperty = (JoQuoteProperty) jobService.getuserquoteProperty(aUserId);
		if(aProperty.getPrintQuantity()==1)
			drb.addColumn("Quantity", "Quantity", String.class.getName(),30);
		if(aProperty.getPrintCost() == 1)
				drb.addColumn("Cost", "Cost", String.class.getName(),30);
		if(aProperty.getPrintPrice() == 1)
			drb.addColumn("Price", "Price", String.class.getName(),30);
		if(aProperty.getPrintManufacturer() == 1)
			drb.addColumn("Manufacturer", "Manufacturer", String.class.getName(),30);
		if(aProperty.getPrintParagraph() == 1)
			drb.addColumn("Paragraph", "Paragraph", String.class.getName(),30);
		if(aProperty.getPrintMult() == 1)
			drb.addColumn("Mult", "Mult", String.class.getName(),30);
		if(aProperty.getPrintSpec() == 1)
			drb.addColumn("Spec", "Spec", String.class.getName(),30);
		drb.setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true);
		DynamicReport dr = drb.build();
//		JRDataSource ds =    //Create a JRDataSource, the Collection used
 		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager());    //Creates the JasperPrint object, we pass as a Parameter
 		JasperViewer.viewReport(jp);
		} catch (ColumnBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Method for Quote Line Items
	 * @param theDocument
	 * @param theJoQuoteHeaderID
	 * @param theBoldFont
	 * @param theNormalFont
	 * @param theBoldTitleFont
	 * @param theParagraphCheck
	 * @param theManufactureCheck
	 * @param theTodayDate
	 * @param theDiscountAmount
	 * @param theJoTotalPrice
	 * @throws PDFException 
	 */
	public void getViewQuotesLineItems(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, Font theBoldTitleFont, String theParagraphCheck, 
																				String theManufactureCheck, String theTodayDate, String theDiscountAmount, String theJoTotalPrice, HttpSession session) throws PDFException {
		List<JobQuoteDetailBean> aQuotesTableValue = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		JoQuoteTemplateProperties p = new JoQuoteTemplateProperties();
		try{
			
			p = jobService.getuserquoteTemplateProperty(aUserBean.getUserId());
			
			
			//Eric said to remove bold and underline for line items only so i set the bold and underline as 0
			//If want to add bold and underline na just comment the below code
			//---Start----//
			p.setBoldItem((byte) 0);
			p.setBoldManufacturer((byte) 0);
			p.setBoldMult((byte) 0);
			p.setBoldParagraph((byte) 0);
			p.setBoldQuantity((byte) 0);
			p.setBoldPrice((byte) 0);
			p.setBoldSpec((byte) 0);
			p.setBoldCost((byte) 0);
			
			p.setUnderlineItem((byte) 0);
			p.setUnderlineManufacturer((byte) 0);
			p.setUnderlineMult((byte) 0);
			p.setUnderlineParagraph((byte) 0);
			p.setUnderlinePrice((byte) 0);
			p.setUnderlineQuantity((byte) 0);
			p.setUnderlineSpec((byte) 0);
			p.setUnderlineCost((byte) 0);
			
			//--End--//
			
			if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
				//aQuotesTableValue =(List<JobQuoteDetailBean>) jobService.getQuotesDetailList(Integer.parseInt(theJoQuoteHeaderID));
				aQuotesTableValue =(List<JobQuoteDetailBean>) jobService.getQuotesTemplateDetailList(Integer.parseInt(theJoQuoteHeaderID));
				
			}
			if(theJoTotalPrice == null || theJoTotalPrice == "")
			{
				theJoTotalPrice = "0.00";
			}
			/** GEt Line Item in Array List **/
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==1){ 
				paragraphAndManufacturerIsEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,"");
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==0){ 
				paragraphIsEnabledAndManufacturerIsDisabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,""); 
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==1){
				paragraphIsDisabledAndManufacturerIsEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,""); 
			}
			if(p.getPrintQuantity()==1 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==0){
				paragraphIsDisabledAndManufacturerIsDisabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==0){
				paragraphOnlyEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==1){
				manufacturerOnlyEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==1 && p.getPrintManufacturer()==1){
				paragraphAndManufacturerEnabled(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,"");
			}
			if(p.getPrintQuantity()==0 && p.getPrintParagraph()==0 && p.getPrintManufacturer()==0){
				noColumnSelected(theDocument, theJoQuoteHeaderID, theBoldFont, theNormalFont, theBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice, aQuotesTableValue,p,"");
			}
			Paragraph aParagraphSUM = null;
			System.out.println("inside calling pdf controller");
			aParagraphSUM = new Paragraph("\n", 
					FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
			aParagraphSUM.setAlignment(Element.ALIGN_RIGHT);
			theDocument.add(aParagraphSUM);
			if(theDiscountAmount != ""){
				if(theDiscountAmount.equalsIgnoreCase("$0.00") || theDiscountAmount.equalsIgnoreCase("0.0000")){
					theDiscountAmount = "";
				}
			}
			
			if(theDiscountAmount != "" && theDiscountAmount != null ){
				float[] subTotWidths = {1.5f, 0.80f};
				PdfPTable aPdfPTableSubTotal = new PdfPTable(subTotWidths); 
				aPdfPTableSubTotal.setWidthPercentage(42);
				aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableSubTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableSubTotal.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableSubTotal.addCell(new Phrase("SUBTOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.TOP);
				aPdfPTableSubTotal.getDefaultCell().setBorder(Rectangle.TOP);
				String aTotalPriceSub = theJoTotalPrice.replace(".0000", ".00");
				String aJoTotalPriceSub =  "$ "+aTotalPriceSub;
				aPdfPTableSubTotal.addCell(new Phrase(aJoTotalPriceSub+"\n\n", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				theDocument.add(aPdfPTableSubTotal);
				float[] Diswidths = {1.5f, 0.80f};
				PdfPTable aPdfPTableDis = new PdfPTable(Diswidths); 
				aPdfPTableDis.setWidthPercentage(42);
				aPdfPTableDis.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTableDis.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableDis.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTableDis.addCell(new Phrase("DISCOUNT: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				String aJoDisPrice1 = theDiscountAmount.replace("$", "$ ");
				String aJoDisPrice =aJoDisPrice1;
				aPdfPTableDis.addCell(new Phrase(aJoDisPrice+"\n\n", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
				theDocument.add(aPdfPTableDis);
			}else{
				if(theJoTotalPrice != "" && theJoTotalPrice != null){
					float[] widths1 = {1.5f, 0.80f};
					PdfPTable aPdfPTable1 = new PdfPTable(widths1); 
					aPdfPTable1.setWidthPercentage(42);
					aPdfPTable1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable1.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					if(p.getPrintTotal() ==1)
					{
						aPdfPTable1.addCell(new Phrase("TOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
						aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
						aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
					}
					else
					{
						aPdfPTable1.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					//aPdfPTable1.addCell(new Phrase("TOTAL: ", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					/*aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
					aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);*/
					String aJoTotalPrice1 = theJoTotalPrice.replace("$", "");
					String aJoTotalPrice = "$ "+aJoTotalPrice1;
					if(p.getPrintPrice() ==1)
					{
						aPdfPTable1.addCell(new Phrase(aJoTotalPrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					else
					{
						aPdfPTable1.addCell(new Phrase("", new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					}
					//aPdfPTable1.addCell(new Phrase(aJoTotalPrice, new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
					theDocument.add(aPdfPTable1);
				}
			}
			theDocument.add( Chunk.NEWLINE );
			if(aQuotesTableValue != null){
				Paragraph aParagraphNotes = null;
				Integer aIndex = 0;
				String aTitle = "";
				String aAlbString = "";
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					String aProductNote = null;
					if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
						if(!aTitle.equalsIgnoreCase("NOTES")){
							aParagraphNotes = new Paragraph("NOTES: ", 
									FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
							aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
							theDocument.add(aParagraphNotes);
							aTitle = "NOTES"; 
						}
						aIndex = aIndex + 1;
						if(aIndex == 1){ aAlbString = "A"; } if(aIndex == 2){ aAlbString = "B"; } if(aIndex == 3){ aAlbString = "C"; } if(aIndex == 4){ aAlbString = "D"; } if(aIndex == 5){ aAlbString = "E"; }
						if(aIndex == 6){ aAlbString = "F"; } if(aIndex == 7){ aAlbString = "G"; } if(aIndex == 8){ aAlbString = "H"; } if(aIndex == 9){ aAlbString = "I"; } if(aIndex == 10){ aAlbString = "J"; }
						if(aIndex == 11){ aAlbString = "K"; } if(aIndex == 12){ aAlbString = "L"; } if(aIndex == 13){ aAlbString = "M"; } if(aIndex == 14){ aAlbString = "N"; }
						if(aIndex == 15){ aAlbString = "O"; } if(aIndex == 16){ aAlbString = "P"; } if(aIndex == 17){ aAlbString = "Q"; } if(aIndex == 18){ aAlbString = "R"; } 
						if(aIndex == 19){ aAlbString = "S"; } if(aIndex == 20){ aAlbString = "T"; } if(aIndex == 21){ aAlbString = "U"; } if(aIndex == 22){ aAlbString = "V"; }
						if(aIndex == 23){ aAlbString = "W"; } if(aIndex == 24){ aAlbString = "X"; } if(aIndex == 25){ aAlbString = "Y"; } if(aIndex == 26){ aAlbString = "Z"; }
						aProductNote = "("+aAlbString+") "+aQuotesTableValue.get(index).getProductNote()+" \n";
					}
					aParagraphNotes = new Paragraph(aProductNote, 
							FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
					aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
					theDocument.add(aParagraphNotes);
				}
			}
			theDocument.add( Chunk.NEWLINE );
		} catch (PDFException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (JobException e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new PDFException(e.getMessage(), e);
		}
	}
	
	
	public boolean getpercentagebasedoncolumn(List<Joquotecolumn> visiblelist){
		boolean returnvalue=false;
		for(Joquotecolumn joobj:visiblelist){
			if(joobj.getColumnName().equalsIgnoreCase("column4")){
				returnvalue=true;
			}
			
		}
		return returnvalue;
	}
	public Map<String, Integer> getpercentagebasedoncolumn1(List<Joquotecolumn> visiblelist){
		boolean returnvalue=false;
		int Item_Man=45;
		int other_percentage=55;
		//Longwidth
		boolean itemthereornot=false;
		boolean manthereornot=false;
		
		// EqualWidth
		boolean qtythereornot=false;			
		boolean specthereornot=false;
		boolean multthereornot=false;
		boolean pricethereornot=false;
		boolean parathereornot=false;
		
		List<Integer> perclst=new ArrayList<Integer>();
		Map<String,Integer> mp=new HashMap<String, Integer>();
		int sumof3items=0;
		int sumof4items=0;
		for(Joquotecolumn joobj:visiblelist){
			//Small width columns
			if(joobj.getColumnName().equalsIgnoreCase("column2")){
				mp.put("column2",10);//Qty
				qtythereornot=true;
				other_percentage=other_percentage-10;
				sumof4items=sumof4items+1;
			}
			if(joobj.getColumnName().equalsIgnoreCase("column5")){
				mp.put("column5",10);//spec		
				specthereornot=true;
				other_percentage=other_percentage-10;
				sumof4items=sumof4items+1;
			}
			if(joobj.getColumnName().equalsIgnoreCase("column7")){
				mp.put("column7",10);//mult
				multthereornot=true;
				other_percentage=other_percentage-10;
				sumof4items=sumof4items+1;
			}
			if(joobj.getColumnName().equalsIgnoreCase("column8")){
				mp.put("column8",10);//price
				pricethereornot=true;
				other_percentage=other_percentage-10;
				sumof4items=sumof4items+1;
			}
			if(joobj.getColumnName().equalsIgnoreCase("column3")){
				mp.put("column3",15);
				parathereornot=true;//Para
				other_percentage=other_percentage-15;
				sumof4items=sumof4items+1;
			}
			
			
			
			//Long width
			if(joobj.getColumnName().equalsIgnoreCase("column1")){
				itemthereornot=true;//Item
				sumof3items=sumof3items+1;
			}
			
			if(joobj.getColumnName().equalsIgnoreCase("column4")){
				manthereornot=true;//Man
				sumof3items=sumof3items+1;
			}
			
		}
		int totalbal_percentage=Item_Man+other_percentage;
		if(sumof3items==0){
			if(sumof4items!=0){
			int widthpercentage=100/sumof4items;
			mp=new HashMap<String, Integer>();
		
				if(qtythereornot){
					mp.put("column2",widthpercentage);
				}
				if(specthereornot){
					mp.put("column5",widthpercentage);
				}
				if(multthereornot){
					mp.put("column7",widthpercentage);
				}
				if(pricethereornot){
					mp.put("column8",widthpercentage);
				}
				if(parathereornot){
					mp.put("column3",widthpercentage);
				}
			}
				
		}else{
			int widthpercentage=totalbal_percentage/sumof3items;
			if(itemthereornot){
				mp.put("column1",widthpercentage);
			}
			if(manthereornot){
				mp.put("column4",widthpercentage);
			}
		}
		
		
		return mp;
	}
		@RequestMapping(value="/viewnewQuotePdfForm", method = RequestMethod.GET) 
		public @ResponseBody String viewdynamicreportPdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
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
																		HttpSession session,HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, DocumentException, SQLException, JobException, UserException, JRException, MessagingException{
			
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			JoQuoteProperties aPropertyID = null;
			String path_JRXML =null;
			String subreportpath_JRXML=null;
			String filename=null;
			Connection connection =null;
			ConnectionProvider con = null;
			try
			{
			// Fetching the Quotes Columns
					ArrayList<Joquotecolumn> joquotecolumnLst=sysservice. getjobQuotesColumn();
					//int qouteLst =joquotecolumnLst.size();
					
					// Fetching the Quotes Columns Properties
					UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
					aPropertyID = (JoQuoteProperties) jobService.getuserquoteProperty(aUserBean.getUserId());
			
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/QuoteLineItemPdf.jrxml");
			//QuoteLineItemPdfsubreport.jrxml
			File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
			String absolutePath = file.getAbsolutePath();
			//subreportpath_JRXML = theRequest.getSession().getServletContext().getRealPath("///resources///jasper_reports///");
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignBand band = new JRDesignBand();
			JRDesignBand detailband = new JRDesignBand();
			//
			int bandheight=20;
			boolean notesFullWidth=false;
			if(aPropertyID.getNotesFullWidth()==1){
				notesFullWidth=true;
				bandheight=20;
			}
			int lineitemheight=15;
			
			band.setHeight(lineitemheight);
			band.setSplitType(SplitTypeEnum.STRETCH);
			detailband.setHeight(lineitemheight);
			//detailband.setSplitType(SplitTypeEnum.STRETCH);
			
			//InlineNote detailband
			JRDesignBand indetailband = new JRDesignBand();
			indetailband.setHeight(lineitemheight);
			
			// Prepare Visible Column List
			List<Joquotecolumn> visibleLst =prepareVisibleLst(joquotecolumnLst,aPropertyID);
			int qouteLst =visibleLst.size();
			System.out.println("No of columns :" + qouteLst);
			
			
			
			//Show rownumber
			boolean PrintLineNumbers=false;
			if(aPropertyID.getLineNumbers()==1){
				PrintLineNumbers=true;
			}
			params.put("columnshowrownum", PrintLineNumbers);
			
			int productnotecount=jobService.getProductNoteCount(Integer.parseInt(theJoQuoteHeaderID), 0);
			boolean footerheaderprint=true;
			if(productnotecount==0){
				footerheaderprint=false;
			}
			params.put("footernotes_show", footerheaderprint);
			
			Map<String,Integer> percentlst=getpercentagebasedoncolumn1(visibleLst);
			//if rownumber checked
			if(PrintLineNumbers){
			
			DynamicReportbean rowbean=new DynamicReportbean();
			rowbean.setX(0);
			rowbean.setY(0);
			rowbean.setWidth(20);
			rowbean.setHeight(lineitemheight);
			rowbean.setBold(true);
			System.out.println("percentlst.get('column8')==>"+percentlst.get("column8"));
			//if(percentlst.get("column8")!=null){	
				rowbean.setText("$F{rownum}");
			//}else{
			//	rowbean.setText("$V{REPORT_COUNT}");
			//}
			JRDesignTextField rowtextField = ReportService.AddColumnTextField(rowbean);
			rowtextField.setPattern("#,##0.");
			detailband.addElement(rowtextField);
			
			}
			int TOTAL_PAGE_WIDTH = 533;
			int SPACE_BETWEEN_COLS = 5;
			// int columnWidth = (TOTAL_PAGE_WIDTH - (SPACE_BETWEEN_COLS * (qouteLst - 1))) / qouteLst;
			int xvalue=20;
			
			for(int i=0;i<qouteLst;i++){
				int percentage=percentlst.get(visibleLst.get(i).getColumnName());
			
				/*if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column1")){
					 percentage=(100-((qouteLst-1)*percentage)) ;
					 if(man_thereornot){
						 percentage=percentage-6;
						}
				}
				if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column4")){
					 percentage=20 ;
				}*/
				System.out.println(visibleLst.get(i).getColumnName()+"visibleLst.contains==>"+visibleLst.contains("column4"));
				System.out.println("percentage==>"+percentage);
				int columnWidth=(percentage*TOTAL_PAGE_WIDTH)/100;
				System.out.println("width=="+columnWidth+"X==="+xvalue);
				DynamicReportbean bean=new DynamicReportbean();
				bean.setX(xvalue);
				bean.setY(0);
				bean.setWidth(columnWidth);
				bean.setHeight(lineitemheight);
				bean.setText(visibleLst.get(i).getColumn1label());
				bean.setPrintRepeatedValues(true);
				bean.setFontsize(8);
				bean.setBold(aPropertyID.getBoldHeader()==1?true:false);
				bean.setBold(false);
				bean.setUnderline(aPropertyID.getUnderlineHeader()==1?true:false);
				bean.setMarkup("html");
					
				JRDesignStaticText headerStaticText=ReportService.AddColumnHeader(bean);
				headerStaticText.setFontSize(11);
				band.addElement(headerStaticText);
	                
				DynamicReportbean tfbean=new DynamicReportbean();
				tfbean.setX(xvalue);
				tfbean.setY(0);
				tfbean.setWidth(columnWidth);
				tfbean.setHeight(lineitemheight);
				
				//tfbean.setBold(checkColumnBold(visibleLst.get(i).getColumnName(),aPropertyID));
				//tfbean.setUnderline(checkColumnUnderLine(visibleLst.get(i).getColumnName(),aPropertyID));
				tfbean.setText(selectColumn(visibleLst.get(i).getColumnName(),footerheaderprint));
				JRDesignTextField textField =ReportService.AddColumnTextField(tfbean);
				if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column8") || visibleLst.get(i).getColumnName().equalsIgnoreCase("column8")){
					textField.setPattern("$ #,##0.00");
					//JRDesignExpression jrDesignexp = new JRDesignExpression();
					//jrDesignexp.setText("$F{Price}.compareTo( BigDecimal.ZERO)==0 ? null : $F{Price} ");
					//jrDesignexp.setValueClassName("java.math.BigDecimal");
					//textField.setPrintWhenExpression(jrDesignexp);
				}
				//JRDesignStyle normalStyle =ReportService.getNormalStyle();
				//textField.setStyle(normalStyle);
				detailband.addElement(textField);
				jd.setColumnHeader(band);
				xvalue=xvalue+columnWidth;
			}
			
			if(notesFullWidth){
				DynamicReportbean notesbean=new DynamicReportbean();
				notesbean.setX(20);
				notesbean.setY(0);
				notesbean.setWidth(523);
				notesbean.setHeight(lineitemheight);
				notesbean.setText("$F{InlineNote}");
				JRDesignTextField notestextField = ReportService.AddColumnTextField(notesbean);
				//notestextField.setPrintWhenDetailOverflows(true);
				//notestextField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
				//notestextField.setStyleNameReference("AlignCheck");
				notestextField.setRemoveLineWhenBlank(true);
				indetailband.addElement(notestextField);
			}
			
			
			
			JRDesignSection overallreport=((JRDesignSection)jd.getDetailSection());
			overallreport.addBand(detailband);
			if(notesFullWidth){
				overallreport.addBand(indetailband);
				}
			filename="quotes.pdf";
			
			//UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			con = itspdfService.connectionForJasper();
					
			String aUsrNme = "";
			if(aUserBean != null){
				if(aUserBean.getFullName() != null && aUserBean.getFullName() != ""){
					aUsrNme = aUserBean.getFullName();
				}
			}
			
			Integer aQuoteThru;
			if(theQuoteThru != null && theQuoteThru != ""){
				aQuoteThru = Integer.parseInt(theQuoteThru);
			}else{
				aQuoteThru = 0;
			}
			
			boolean isStateExists = theState != null && theState != "";
			boolean isCityExists = theCity != null && theCity != "";
			String location=" ";
			if(isStateExists && isCityExists)
				location =theCity+", "+theState.toUpperCase(); 
			else if(isStateExists)
				location =theState.toUpperCase();
			else
				location =theCity;
				
				params.put("joquoteheaderid", theJoQuoteHeaderID);
					//Other Details
					params.put("project", theProjectNamePDF);
					params.put("location", location);
					params.put("quoteto", theQuoteName);
					params.put("attention", theBidderContact);
					params.put("quote", theJobNumber);
					params.put("biddate", theBidDate);
					params.put("architect", theArchitect);
					params.put("Engineer", theEnginner);
					params.put("plandate", thePlanDate);
					params.put("revision", theJoQuoteRev);
					absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
					
			        if (OperatingSystemInfo.isWindows()) {
						logger.info("This is Windows");
						absolutePath=absolutePath+"\\\\QuoteLineItemPdfsubreport.jasper";
					} else if (OperatingSystemInfo.isMac()) {
						logger.info("This is Mac");
					} else if (OperatingSystemInfo.isUnix()) {
						logger.info("This is Unix or Linux");
						absolutePath=absolutePath+"/QuoteLineItemPdfsubreport.jasper";
					} else if (OperatingSystemInfo.isSolaris()) {
						logger.info("This is Solaris");
					} else {
						logger.info("Your OS is not support!!");
					}

					
					
					//Footer details
					System.out.println(absolutePath);
					params.put("Dir", absolutePath);
					params.put("submittedby", theSubmittedBy);
					System.out.println("theTodayDate"+theTodayDate);
					params.put("dated", theTodayDate);
					params.put("thruaddendum", aQuoteThru);
					params.put("inlinenoteshow", notesFullWidth);
					//System.out.println("subreportpath_JRXML=="+absolutePath);
					String remarks=null;
					JoQuoteHeader aJoQuoteHeader = jobService.getjoQuoteAmount(Integer.parseInt(theJoQuoteHeaderID));
					remarks=aJoQuoteHeader.getRemarks();
					params.put("remarks", remarks);
				
					boolean printTotal=false;
					
					//Print total
					if(aPropertyID.getPrintTotal()==1){
						printTotal=true;
					}
					params.put("total_show",printTotal);
					TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
					//String terms=aUserLoginSetting.getTerms();
					JRDesignBand titleband=(JRDesignBand) jd.getTitle();
					JRElement[] ele=titleband.getElements();
					JRDesignTextField terms=(JRDesignTextField) ele[ele.length-1];
					
					//titleband.setHeight(titleband.getHeight()+8);
					//JRElement[] ele=titleband.getElements();
					//JRDesignComponentElement elee=(JRDesignComponentElement)ele[ele.length-1];
					//elee.setHeight(ele[ele.length-1].getHeight()+8);
					//elee.setPrintWhenDetailOverflows(true);
					//System.out.println("ele[ele.length-1].getHeight()"+ele[ele.length-1].getHeight());
					//JRDesignComponentElement elee=(JRDesignComponentElement) ele[ele.length-1].get;
					//System.out.println("elee.getHeight()"+elee.getHeight());
					List<String> termslist=new ArrayList<String>();
					String aHtmlString =  aUserLoginSetting.getTerms();
					if(aHtmlString!=null){
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String aText3 = aText2+"\n\n\n";
					System.out.println("Terms-------->"+aText3);
					
					String sHTML = "left";
					if(aText3.contains("text-align")){
					sHTML = aText3.substring(aText3.lastIndexOf("text-align"),aText3.length());
					sHTML = sHTML.substring(sHTML.indexOf("text-align"),sHTML.indexOf(";"));
					sHTML = sHTML.substring(sHTML.indexOf(":")+1,sHTML.length()).trim();
					if("left".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
					else if("right".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
					else if("center".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
					else if("justify".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
					}else{
						terms.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
					}
					}
					
					
			connection = con.getConnection();
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(),e);
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
			}
			finally
			{
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			}
			return null;
		}
	
	private int getcolumnwidth(String clmnvalue){
		int width=90;
		if(clmnvalue.equals("column1")){
			width=170;
		}else if(clmnvalue.equals("column2")){
			width=90;
		}else if(clmnvalue.equals("column3")){
			width=90;
		}else if(clmnvalue.equals("column4")){
			width=90;
		}else if(clmnvalue.equals("column5")){
			width=90;
		}else if(clmnvalue.equals("column6")){
			width=90;
		}else if(clmnvalue.equals("column7")){
			width=90;
		}else if(clmnvalue.equals("column8")){
			width=90;
		}
		return width;
	}
	 private List<Joquotecolumn> prepareVisibleLst(
			ArrayList<Joquotecolumn> joquotecolumnLst,
			JoQuoteProperties aPropertyID) {
		 List<Joquotecolumn> visibleLst = new ArrayList<Joquotecolumn>();
		 
		 for(int i=0;i<joquotecolumnLst.size();i++){
			 if(checkVisibleLst(joquotecolumnLst.get(i).getColumnName(),aPropertyID))
					 visibleLst.add(joquotecolumnLst.get(i));
		 }
		 
		return visibleLst;
	}

	 
	 
	
	private boolean checkVisibleLst(String clmnvalue,
			JoQuoteProperties dto) {
		boolean bprint=true;
		if(clmnvalue.equals("column1")){
			bprint = dto.getPrintItem()==1?true:false;
		}else if(clmnvalue.equals("column2")){
			bprint = dto.getPrintQuantity()==1?true:false;
		}else if(clmnvalue.equals("column3")){
			bprint = dto.getPrintParagraph()==1?true:false;
		}else if(clmnvalue.equals("column4")){
			bprint = dto.getPrintManufacturer()==1?true:false;
		}else if(clmnvalue.equals("column5")){
			bprint = dto.getPrintSpec()==1?true:false;
		}else if(clmnvalue.equals("column6")){
			bprint = dto.getPrintCost()==1?true:false;
		}else if(clmnvalue.equals("column7")){
			bprint = dto.getPrintMult()==1?true:false;
		}else if(clmnvalue.equals("column8")){
			bprint = dto.getPrintPrice()==1?true:false;
			if(bprint){
				if(dto.getHidePrice()==1){
					bprint = false;
				}
			}
		}
			
		return bprint;
	}


	private boolean checkColumnBold(String value,
			JoQuoteProperties propertyDto) {
		boolean result=false;
		
		 if(value.equalsIgnoreCase("column1")){
			 result =propertyDto.getBoldItem()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column2")){
			 result =propertyDto.getBoldQuantity()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column3")){
			 result =propertyDto.getBoldParagraph()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column4")){
			 result =propertyDto.getBoldManufacturer()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column5")){
			 result =propertyDto.getBoldSpec()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column6")){
			 result =propertyDto.getBoldCost()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column7")){
			 result =propertyDto.getBoldMult()==1?true:false;
		 }else  if(value.equalsIgnoreCase("column8")){
			 result =propertyDto.getBoldPrice()==1?true:false;
		 }
		return result;
	}

	 private boolean checkColumnUnderLine(String value,
				JoQuoteProperties propertyDto) {
			boolean result=false;
			
			 if(value.equalsIgnoreCase("column1")){
				 result =propertyDto.getUnderlineItem()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column2")){
				 result =propertyDto.getUnderlineQuantity()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column3")){
				 result =propertyDto.getUnderlineParagraph()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column4")){
				 result =propertyDto.getUnderlineManufacturer()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column5")){
				 result =propertyDto.getUnderlineSpec()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column6")){
				 result =propertyDto.getUnderlineCost()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column7")){
				 result =propertyDto.getUnderlineMult()==1?true:false;
			 }else  if(value.equalsIgnoreCase("column8")){
				 result =propertyDto.getUnderlinePrice()==1?true:false;
			 }
			return result;
		}
	 
	private String selectColumn(String value,boolean footernote) {
		 String result = "";
		 if(value.equalsIgnoreCase("column1")){
			 String iffooternote="+$F{Product}+";
			 if(footernote){
				 iffooternote="+$F{Product}+ ($F{footerrownum}==0 ? \"\" : \"(\"+Character.toUpperCase((char)($F{footerrownum}))+\")\" )+";
			 }
			  result="( $F{underlineItem}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldItem}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicItem}.equals( \"1\" ) ? \"<i>\":\"\")"+iffooternote+"( $F{italicItem}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldItem}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineItem}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Product}";
		 }else  if(value.equalsIgnoreCase("column2")){
			  result="( $F{underlineQuantity}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldQuantity}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicQuantity}.equals( \"1\" ) ? \"<i>\":\"\")+( $F{ItemQuantity}.equals(\"0\") ? \"\" : $F{ItemQuantity} )+( $F{italicQuantity}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldQuantity}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineQuantity}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{ItemQuantity}";
		 }else  if(value.equalsIgnoreCase("column3")){
			 result="( $F{underlineParagraph}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldParagraph}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicParagraph}.equals( \"1\" ) ? \"<i>\":\"\")+$F{Paragraph}+( $F{italicParagraph}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldParagraph}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineParagraph}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Paragraph}";
		 }else  if(value.equalsIgnoreCase("column4")){
			 result="( $F{underlineManufacturer}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldManufacturer}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicManufacturer}.equals( \"1\" ) ? \"<i>\":\"\")+$F{Manufacturer}+( $F{italicManufacturer}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldManufacturer}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineManufacturer}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Manufacturer}";
		 }else  if(value.equalsIgnoreCase("column5")){
			 result="( $F{underlineSpec}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldSpec}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicSpec}.equals( \"1\" ) ? \"<i>\":\"\")+( $F{Spec}.equals(\"0\") ? \"\" : $F{Spec} )+( $F{italicSpec}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldSpec}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineSpec}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Spec}";
		 }else  if(value.equalsIgnoreCase("column6")){
			 result ="$F{Cost}";
		 }else  if(value.equalsIgnoreCase("column7")){
			 result="( $F{underlineMult}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldMult}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicMult}.equals( \"1\" ) ? \"<i>\":\"\")+( $F{Mult}.equals(\"0\") ? \"\" : $F{Mult})+( $F{italicMult}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldMult}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineMult}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			 //result ="$F{Mult}";
		 }else  if(value.equalsIgnoreCase("column8")){
			 result="( $F{underlinePrice}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldPrice}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicPrice}.equals( \"1\" ) ? \"<i>\":\"\")+($F{Price}.equals( \"\" ) ? \"\" : \"\\$\"+$F{Price} )+( $F{italicPrice}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldPrice}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlinePrice}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Price}";
		 }
		return result;
	}

	
	@RequestMapping(value = "/printBalanceSheet", method = RequestMethod.GET)
	public @ResponseBody
	void printCSVCompanyContacts(
			@RequestParam(value = "showaccount", required = false) boolean showaccount,
			@RequestParam(value = "cofiscalperiodid", required = false) Integer cofiscalperiodid,
			@RequestParam(value = "asofdate", required = false) String asofdate,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, SQLException, MessagingException {
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/balancesheet.jrxml");
			filename="BalanceSheet.pdf";
			con = itspdfService.connectionForJasper();
			params.put("accountnochkbx", showaccount);
			params.put("cofiscalperiodID", cofiscalperiodid);
			params.put("as_of", asofdate);
			
			 connection = con.getConnection();
			ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>asofdate:</b>"+asofdate,"QuotePDFController",e,session,theRequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}	
		}
	}
    //Template Methods
	 private List<Joquotecolumn> prepareQuoteTemplateVisibleLst(
				ArrayList<Joquotecolumn> joquotecolumnLst,
				JoQuoteTemplateProperties aPropertyID) {
			 List<Joquotecolumn> visibleLst = new ArrayList<Joquotecolumn>();
			 
			 for(int i=0;i<joquotecolumnLst.size();i++){
				 if(checkVisibleTemplateLst(joquotecolumnLst.get(i).getColumnName(),aPropertyID)){
						 visibleLst.add(joquotecolumnLst.get(i));
				 }
			 }
			 
			return visibleLst;
		}
	 private boolean checkVisibleTemplateLst(String clmnvalue,
			 JoQuoteTemplateProperties dto) {
			boolean bprint=true;
			if(clmnvalue.equals("column1")){
				bprint = dto.getPrintItem()==1?true:false;
			}else if(clmnvalue.equals("column2")){
				bprint = dto.getPrintQuantity()==1?true:false;
			}else if(clmnvalue.equals("column3")){
				bprint = dto.getPrintParagraph()==1?true:false;
			}else if(clmnvalue.equals("column4")){
				bprint = dto.getPrintManufacturer()==1?true:false;
			}else if(clmnvalue.equals("column5")){
				bprint = dto.getPrintSpec()==1?true:false;
			}else if(clmnvalue.equals("column6")){
				bprint = dto.getPrintCost()==1?true:false;
			}else if(clmnvalue.equals("column7")){
				bprint = dto.getPrintMult()==1?true:false;
			}else if(clmnvalue.equals("column8")){
				bprint = dto.getPrintPrice()==1?true:false;
				if(bprint){
					if(dto.getHidePrice()==1){
						bprint = false;
					}
				}
			}
				
			return bprint;
		}
	 
	 private String selectTemplateColumn(String value,boolean footernote) {
		 String result = "";
		 if(value.equalsIgnoreCase("column1")){
			// Character.toUpperCase((char)(96+$V{REPORT_COUNT}))
			 String iffooternote="+$F{Product}+";
			 if(footernote){
				 iffooternote="+$F{Product}+ ($F{footerrownum}==0 ? \"\" : \"(\"+Character.toUpperCase((char)($F{footerrownum}))+\")\" )+";
			 }
			  result="( $F{underlineItem}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldItem}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicItem}.equals( \"1\" ) ? \"<i>\":\"\")"+iffooternote+"( $F{italicItem}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldItem}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineItem}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Product}";
		 }else  if(value.equalsIgnoreCase("column2")){
			  result="( $F{underlineQuantity}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldQuantity}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicQuantity}.equals( \"1\" ) ? \"<i>\":\"\")+( $F{ItemQuantity}.equals(\"0\") ? \"\" : $F{ItemQuantity} )+( $F{italicQuantity}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldQuantity}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineQuantity}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{ItemQuantity}";
		 }else  if(value.equalsIgnoreCase("column3")){
			 result="( $F{underlineParagraph}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldParagraph}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicParagraph}.equals( \"1\" ) ? \"<i>\":\"\")+$F{Paragraph}+( $F{italicParagraph}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldParagraph}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineParagraph}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Paragraph}";
		 }else  if(value.equalsIgnoreCase("column4")){
			 result="( $F{underlineManufacturer}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldManufacturer}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicManufacturer}.equals( \"1\" ) ? \"<i>\":\"\")+$F{Manufacturer}+( $F{italicManufacturer}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldManufacturer}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineManufacturer}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Manufacturer}";
		 }else  if(value.equalsIgnoreCase("column5")){
			 result="( $F{underlineSpec}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldSpec}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicSpec}.equals( \"1\" ) ? \"<i>\":\"\")+( $F{Spec}.equals(\"0\") ? \"\" : $F{Spec} )+( $F{italicSpec}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldSpec}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineSpec}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Spec}";
		 }else  if(value.equalsIgnoreCase("column6")){
			 result ="$F{Cost}";
		 }else  if(value.equalsIgnoreCase("column7")){
			 result="( $F{underlineMult}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldMult}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicMult}.equals( \"1\" ) ? \"<i>\":\"\")+( $F{Mult}.equals(\"0\") ? \"\" : $F{Mult} )+( $F{italicMult}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldMult}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlineMult}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			 //result ="$F{Mult}";
		 }else  if(value.equalsIgnoreCase("column8")){
			 result="( $F{underlinePrice}.equals( \"1\" ) ? \"<u>\" : \"\" )+( $F{boldPrice}.equals( \"1\" ) ? \"<b>\" : \"\" )+( $F{italicPrice}.equals( \"1\" ) ? \"<i>\":\"\")+($F{Price}.equals( \"\" ) ? \"\" : \"\\$\"+$F{Price} )+( $F{italicPrice}.equals( \"1\" ) ? \"</i>\" : \"\" )+( $F{boldPrice}.equals( \"1\" ) ? \"</b>\" : \"\" )+( $F{underlinePrice}.equals( \"1\" ) ? \"</u>\" : \"\" )";
			// result ="$F{Price}";
		 }
		return result;
	}
	 
	 @RequestMapping(value="/viewQuoteBidPdfForm", method = RequestMethod.GET) 
		public @ResponseBody String viewQuoteBidPdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
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
				@RequestParam(value="quoteTypeID", required= false) String theQuoteTypeID, 
				@RequestParam(value="quoteRev", required= false) String theQuoteRev,
				@RequestParam(value="joMasterID", required= false) String theJoMasterID,
				@RequestParam(value="paragraphCheck", required= false) String theParagraphCheck,
				@RequestParam(value="manufactureCheck", required= false) String theManufactureCheck,
				@RequestParam(value="QuotePDF", required= false) String theQuotePDFIdnti,
				@RequestParam(value="WriteorView", required= false) String WriteorView,
				HttpSession session,HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, DocumentException, SQLException, JobException, UserException, JRException, MessagingException{
		 //PdfWriter.getInstance(document, new FileOutputStream(filename));
			
		 String aQuoteRev = pdfService.getQuotesRev(theQuoteTypeID, theQuoteRev, theJoMasterID);
		 Integer aJoQuoteHeaderID = 0;
			String aQuoteRevId = "";
			/** get Quote Header ID **/
			if(!aQuoteRev.isEmpty()){
				aQuoteRevId = "value";
				aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
			}else{
				aQuoteRevId = "NotQuote";
				aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
			}
		 
		 
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			JoQuoteProperties aPropertyID = null;
			String path_JRXML =null;
			String subreportpath_JRXML=null;
			Connection connection =null;
			ConnectionProvider con = null;
			String filename=null;
			int lineitemheight=15;
			
			try
			{
			// Fetching the Quotes Columns
					ArrayList<Joquotecolumn> joquotecolumnLst=sysservice. getjobQuotesColumn();
					//int qouteLst =joquotecolumnLst.size();
					
					// Fetching the Quotes Columns Properties
					UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
					aPropertyID = (JoQuoteProperties) jobService.getuserquoteProperty(aUserBean.getUserId());
			
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/QuoteLineItemPdf.jrxml");
			//QuoteLineItemPdfsubreport.jrxml
			File file = new File( theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/"));
			String absolutePath = file.getAbsolutePath();
			//subreportpath_JRXML = theRequest.getSession().getServletContext().getRealPath("///resources///jasper_reports///");
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignBand band = new JRDesignBand();
			JRDesignBand detailband = new JRDesignBand();
			//
			int bandheight=20;
			boolean notesFullWidth=false;
			if(aPropertyID.getNotesFullWidth()==1){
				notesFullWidth=true;
				bandheight=20;
			}
			
			
			band.setHeight(lineitemheight);
			band.setSplitType(SplitTypeEnum.STRETCH);
			detailband.setHeight(lineitemheight);
			//detailband.setSplitType(SplitTypeEnum.STRETCH);
			
			//InlineNote detailband
			JRDesignBand indetailband = new JRDesignBand();
			indetailband.setHeight(lineitemheight);
			
			// Prepare Visible Column List
			List<Joquotecolumn> visibleLst =prepareVisibleLst(joquotecolumnLst,aPropertyID);
			int qouteLst =visibleLst.size();
			System.out.println("No of columns :" + qouteLst);
			
			
			
			//Show rownumber
			boolean PrintLineNumbers=false;
			if(aPropertyID.getLineNumbers()==1){
				PrintLineNumbers=true;
			}
			params.put("columnshowrownum", PrintLineNumbers);
			
			
			int productnotecount=jobService.getProductNoteCount(aJoQuoteHeaderID, 0);
			boolean footerheaderprint=true;
			if(productnotecount==0){
				footerheaderprint=false;
			}
			params.put("footernotes_show", footerheaderprint);
			Map<String,Integer> percentlst=getpercentagebasedoncolumn1(visibleLst);
			//if rownumber checked
			if(PrintLineNumbers){
			DynamicReportbean rowbean=new DynamicReportbean();
			rowbean.setX(0);
			rowbean.setY(0);
			rowbean.setWidth(20);
			rowbean.setHeight(lineitemheight);
			rowbean.setBold(true);
			System.out.println("percentlst.get('column8')==>"+percentlst.get("column8"));
			//if(percentlst.get("column8")!=null){	
				rowbean.setText("$F{rownum}");
			//}else{
			//	rowbean.setText("$V{REPORT_COUNT}");
			//}
			JRDesignTextField rowtextField = ReportService.AddColumnTextField(rowbean);
			rowtextField.setPattern("#,##0.");
			detailband.addElement(rowtextField);
			}
			int TOTAL_PAGE_WIDTH = 533;
			int SPACE_BETWEEN_COLS = 5;
			boolean man_thereornot=getpercentagebasedoncolumn(visibleLst);
			// int columnWidth = (TOTAL_PAGE_WIDTH - (SPACE_BETWEEN_COLS * (qouteLst - 1))) / qouteLst;
			int xvalue=20;
			
			for(int i=0;i<qouteLst;i++){
				int percentage=percentlst.get(visibleLst.get(i).getColumnName());
			
				/*if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column1")){
					 percentage=(100-((qouteLst-1)*percentage)) ;
					 if(man_thereornot){
						 percentage=percentage-6;
						}
				}
				if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column4")){
					 percentage=20 ;
				}*/
				System.out.println(visibleLst.get(i).getColumnName()+"visibleLst.contains==>"+visibleLst.contains("column4"));
				System.out.println("percentage==>"+percentage);
				int columnWidth=(percentage*TOTAL_PAGE_WIDTH)/100;
				System.out.println("width=="+columnWidth+"X==="+xvalue);
				DynamicReportbean bean=new DynamicReportbean();
				bean.setX(xvalue);
				bean.setY(0);
				bean.setWidth(columnWidth);
				bean.setHeight(lineitemheight);
				bean.setText(visibleLst.get(i).getColumn1label());
				bean.setPrintRepeatedValues(true);
				bean.setFontsize(8);
				bean.setBold(aPropertyID.getBoldHeader()==1?true:false);
				bean.setBold(false);
				bean.setUnderline(aPropertyID.getUnderlineHeader()==1?true:false);
				bean.setMarkup("html");
					
				JRDesignStaticText headerStaticText=ReportService.AddColumnHeader(bean);
				headerStaticText.setFontSize(11);
				band.addElement(headerStaticText);
	                
				DynamicReportbean tfbean=new DynamicReportbean();
				tfbean.setX(xvalue);
				tfbean.setY(0);
				tfbean.setWidth(columnWidth);
				tfbean.setHeight(lineitemheight);
				
				//tfbean.setBold(checkColumnBold(visibleLst.get(i).getColumnName(),aPropertyID));
				//tfbean.setUnderline(checkColumnUnderLine(visibleLst.get(i).getColumnName(),aPropertyID));
				tfbean.setText(selectColumn(visibleLst.get(i).getColumnName(),footerheaderprint));
				JRDesignTextField textField =ReportService.AddColumnTextField(tfbean);
				if(visibleLst.get(i).getColumnName().equalsIgnoreCase("column8") || visibleLst.get(i).getColumnName().equalsIgnoreCase("column8")){
					textField.setPattern("$ #,##0.00");
					//JRDesignExpression jrDesignexp = new JRDesignExpression();
					//jrDesignexp.setText("$F{Price}.compareTo( BigDecimal.ZERO)==0 ? null : $F{Price} ");
					//jrDesignexp.setValueClassName("java.math.BigDecimal");
					//textField.setPrintWhenExpression(jrDesignexp);
				}
				//JRDesignStyle normalStyle =ReportService.getNormalStyle();
				//textField.setStyle(normalStyle);
				detailband.addElement(textField);
				jd.setColumnHeader(band);
				xvalue=xvalue+columnWidth;
			}
			
			if(notesFullWidth){
				DynamicReportbean notesbean=new DynamicReportbean();
				notesbean.setX(20);
				notesbean.setY(0);
				notesbean.setWidth(523);
				notesbean.setHeight(lineitemheight);
				notesbean.setText("$F{InlineNote}");
				JRDesignTextField notestextField = ReportService.AddColumnTextField(notesbean);
				//notestextField.setPrintWhenDetailOverflows(true);
				//notestextField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
				notestextField.setRemoveLineWhenBlank(true);
				indetailband.addElement(notestextField);
			}
			
			
			
			JRDesignSection overallreport=((JRDesignSection)jd.getDetailSection());
			overallreport.addBand(detailband);
			if(notesFullWidth){
				overallreport.addBand(indetailband);
				}
			filename="quotes.pdf";
			
			//UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			con = itspdfService.connectionForJasper();
					
			String aUsrNme = "";
			if(aUserBean != null){
				if(aUserBean.getFullName() != null && aUserBean.getFullName() != ""){
					aUsrNme = aUserBean.getFullName();
				}
			}
			
			Integer aQuoteThru = 0;
			boolean atleastOneAlpha = theQuoteThru.matches(".*[a-zA-Z]+.*");
			if(theQuoteThru != null && theQuoteThru != ""){
				if(!atleastOneAlpha){
				aQuoteThru = Integer.parseInt(theQuoteThru);
				}
			}else{
				aQuoteThru = 0;
			}
			
			JoQuoteHeader aJoQuoteHeader = jobService.getjoQuoteAmount(aJoQuoteHeaderID);
			boolean isStateExists = theState != null && theState != "";
			boolean isCityExists = theCity != null && theCity != "";
			String location=" ";
			if(isStateExists && isCityExists)
				location =theCity+", "+theState.toUpperCase(); 
			else if(isStateExists)
				location =theState.toUpperCase();
			else
				location =theCity;
				
				params.put("joquoteheaderid", aJoQuoteHeaderID);
					//Other Details
					params.put("project", theProjectNamePDF);
					params.put("location", location);
					params.put("quoteto", theQuoteName);
					params.put("attention", theBidderContact);
					params.put("quote", theJobNumber);
					params.put("biddate", theBidDate);
					params.put("architect", theArchitect);
					params.put("Engineer", theEnginner);
					params.put("plandate", thePlanDate);
					params.put("revision", aJoQuoteHeader.getQuoteRev());
					absolutePath  = absolutePath.replaceAll("\\\\", "\\\\\\\\");
					
			        if (OperatingSystemInfo.isWindows()) {
						logger.info("This is Windows");
						absolutePath=absolutePath+"\\\\QuoteLineItemPdfsubreport.jasper";
					} else if (OperatingSystemInfo.isMac()) {
						logger.info("This is Mac");
					} else if (OperatingSystemInfo.isUnix()) {
						logger.info("This is Unix or Linux");
						absolutePath=absolutePath+"/QuoteLineItemPdfsubreport.jasper";
					} else if (OperatingSystemInfo.isSolaris()) {
						logger.info("This is Solaris");
					} else {
						logger.info("Your OS is not support!!");
					}

					
					
					//Footer details
					System.out.println(absolutePath);
					params.put("Dir", absolutePath);
					params.put("submittedby", aJoQuoteHeader.getCreatedByName());
					System.out.println("theTodayDate"+theTodayDate);
					params.put("dated", theTodayDate);
					params.put("thruaddendum", aQuoteThru);
					params.put("inlinenoteshow", notesFullWidth);
					//System.out.println("subreportpath_JRXML=="+absolutePath);
					
					
					
					
					boolean printTotal=false;
					
					//Print total
					if(aPropertyID.getPrintTotal()==1){
						printTotal=true;
					}
					params.put("total_show",printTotal);
					String remarks=null;
					
					remarks=aJoQuoteHeader.getRemarks();
					params.put("remarks", remarks);
					


					
					
					
					TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
					//String terms=aUserLoginSetting.getTerms();
					JRDesignBand titleband=(JRDesignBand) jd.getTitle();
					JRElement[] ele=titleband.getElements();
					JRDesignTextField terms=(JRDesignTextField) ele[ele.length-1];
					
					//titleband.setHeight(titleband.getHeight()+8);
					//JRElement[] ele=titleband.getElements();
					//JRDesignComponentElement elee=(JRDesignComponentElement)ele[ele.length-1];
					//elee.setHeight(ele[ele.length-1].getHeight()+8);
					//elee.setPrintWhenDetailOverflows(true);
					//System.out.println("ele[ele.length-1].getHeight()"+ele[ele.length-1].getHeight());
					//JRDesignComponentElement elee=(JRDesignComponentElement) ele[ele.length-1].get;
					//System.out.println("elee.getHeight()"+elee.getHeight());
					List<String> termslist=new ArrayList<String>();
					String aHtmlString =  aUserLoginSetting.getTerms();
					if(aHtmlString!=null){
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String aText3 = aText2+"\n\n\n";
					System.out.println("Terms-------->"+aText3);
					String sHTML = "";
					if(aText3.contains("text-align")){
					sHTML = aText3.substring(aText3.lastIndexOf("text-align"),aText3.length());
					sHTML = sHTML.substring(sHTML.indexOf("text-align"),sHTML.indexOf(";"));
					sHTML = sHTML.substring(sHTML.indexOf(":")+1,sHTML.length()).trim();
					
					if("left".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
					else if("right".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
					else if("center".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
					else if("justify".equalsIgnoreCase(sHTML))
						terms.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
				
					
					}else{
						terms.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
					}
					}
					
					
					
			 connection = con.getConnection();
			if(WriteorView!=null&&WriteorView.equals("write")){
				ReportService.dynamicWriteReportCall(theRequest,theResponse,params,"pdf",jd,"Quotes#"+theJobNumber.trim()+".pdf",connection);
			}else{
				ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
			}
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
			}
			finally
			{
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			}
			return null;
		}
	 
	 
	 
	 //Old Quote Method
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
		 Document aPDFDocument = new Document(PageSize.A4, 36, 36, 36, 36);
		 Font aBoldFont = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD));
		 Font aNormalFont = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL));
		 Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		 PdfWriter aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
		 ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
		 PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
		 String aHeaderText   = "QUOTES";
	try {
		
		UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		
		//** Set PDF application using response **//*
		aResponse.setContentType("application/pdf");
		//aBoldTitleFont.setStyle(Font.UNDERLINE);
		Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		aNormalTitleFont.setStyle(Font.UNDERLINE);
		
		//** Set page Number **//*
//		HeaderFooter aFooter = null;
//		aFooter= new HeaderFooter(new Phrase("Page: "), true);
//		aFooter.setBorder(Rectangle.NO_BORDER);
//		aFooter.setAlignment(Element.ALIGN_RIGHT);
//		aPDFDocument.setHeader(aFooter);
//		aPDFDocument.addTitle("");
		FontFactory.registerDirectories();
		aPDFDocument.open();
		
		HeaderFooter aFooter = null;
		aFooter= new HeaderFooter(new Phrase("Page: "), true);
		aFooter.setBorder(Rectangle.NO_BORDER);
		aFooter.setAlignment(Element.ALIGN_RIGHT);
		aPDFDocument.setHeader(aFooter);
		
		aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
		String alternatetext=getalternatetextinhtml(theJoQuoteHeaderID);
		getHeaderInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, theSubmittedBy, theTodayDate,aUserLoginSetting,alternatetext);
		
		//** Set Header Informations Using itext Paragraph **//*
//		Paragraph aParagraphHeader = null;
//		aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 0)));
//		aParagraphHeader.setSpacingAfter(4f);
//		aParagraphHeader.setIndentationRight(70f);
//		aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
//		aPDFDocument.add(aParagraphHeader);
//		aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, new Color(255, 255, 255)));
//		aPDFDocument.add(aParagraphHeader);
		
		//** HEader with Rich Text Editor Values **//*
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
//			ArrayList p=new ArrayList();
//			StringReader aStrReader = new StringReader(aText4);
//			p = HTMLWorker.parseToList(aStrReader, null);
//			for (int k = 0; k < p.size(); ++k){
//				aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
//			}
//			aParagraphHeader.setIndentationLeft(200);
//			aParagraphHeader.setIndentationRight(0);
//			aPDFDocument.add(aParagraphHeader);
		
			
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
		
		
		
		
		
		
		if(theProjectNamePDF!=null)
			theProjectNamePDF=theProjectNamePDF.replaceAll("`and`", "&");
		
		
		getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
				theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,quotes_numberandtype);
		
		addDetailBandColumns(aPDFDocument, theJoQuoteHeaderID, aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
				theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice);
		
		getFooterInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, theSubmittedBy, theTodayDate,aUserLoginSetting);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,therequest);
		}
		 return null;
	 }
	 
	

	/*@RequestMapping(value="/viewsampleQuotePdfForm", method = RequestMethod.GET)
		public @ResponseBody String viewsampleQuotePdfForm(@RequestParam(value="enginnerName", required= false) String theEnginner,
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
																		HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException, MessagingException{
		 
		 Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
		 Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
		 Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
		 Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		 PdfWriter aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
		 ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
		 PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
	try {
		TsUserSetting aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
		UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		HeaderFooter aFooter = null;
		aFooter= new HeaderFooter(new Phrase("Page: "), true);
		aFooter.setBorder(Rectangle.NO_BORDER);
		aFooter.setAlignment(Element.ALIGN_RIGHT);
		aPDFDocument.setHeader(aFooter);
		aPDFDocument.addTitle("");
		aPDFDocument.open();
		
		getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
				theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting);
		
		addoldDetailBandColumns(aPDFDocument, theJoQuoteHeaderID, aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
						theManufactureCheck, theTodayDate, theDiscountAmount, theJoTotalPrice);
		
		
		getFooterInformation(theJoQuoteHeaderID, aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, " ", theTodayDate,aUserLoginSetting);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,therequest);
		}
		 return null;
	 }*/
	 
	 
		
		public void addoldDetailBandColumns(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
				Font theBoldTitleFont, String theParagraphCheck, 
				String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
				String theJoTotalPrice) throws PDFException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
			Paragraph aParagraphHeader=null;
			try{
				PdfPTable aPdfPTable = null;
				List<testforquotes> testquote=jobService.getTestQuotefromtable();
				for(testforquotes tstqts:testquote){
				
					if(tstqts.getType()==1){
						StringReader sr=new StringReader(tstqts.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  theDocument.add(element);
						  }
					}else if(tstqts.getType()==2){
						float[] aWidths = {1.3f,15.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase(tstqts.getTextbox(), new Font(FontFactory.getFont(FontFactory.HELVETICA, 10,null))));
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						StringReader sr=new StringReader(tstqts.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  System.out.println(element);
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
					}
					else if(tstqts.getType()==3){
						float[] aWidths = {1.3f,13.5f,2.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase(tstqts.getTextbox(), new Font(FontFactory.getFont(FontFactory.HELVETICA, 10,null))));
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						StringReader sr=new StringReader(tstqts.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						
						
						aParagraphHeader = new Paragraph("$"+String.valueOf(tstqts.getSellprice()), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
						cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingTop(2f);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(cell);
						
						theDocument.add(aPdfPTable);
					}else if(tstqts.getType()==4){
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(new Phrase("TotalNet Price...  $"+String.valueOf(tstqts.getSellprice()), new Font(FontFactory.getFont(FontFactory.HELVETICA, 10,null))));
						theDocument.add(aPdfPTable);
					}
					
					
					System.out.println("Id==>"+tstqts.getId());
					System.out.println("type==>"+tstqts.getType());
					System.out.println("textbox==>"+tstqts.getTextbox());
					System.out.println("texteditor==>"+tstqts.getTexteditor());
					System.out.println("SellPrice==>"+tstqts.getSellprice());
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/**
		 * Method for getting Project Information 
		 * @param theProjectNamePDF
		 * @param theQuoteName
		 * @param theBidderContact
		 * @param theJobNumber
		 * @param theBidDate
		 * @param theArchitect
		 * @param thePlanDate
		 * @param theJoQuoteRev
		 * @param theBoldFont
		 * @param theNormalFont
		 * @param theBoldTitleFont
		 * @param theState
		 * @param theCity
		 * @param theEnginner
		 * @param theDocument
		 * @param theUserLoginSetting
		 */
		private void getQuoteProjectInformation(String theProjectNamePDF, String theQuoteName, String theBidderContact, String theJobNumber, String theBidDate, String theArchitect, String thePlanDate, String theJoQuoteRev, Font theBoldFont, Font theNormalFont, 
																									Font theBoldTitleFont, String theState, String theCity, String theEnginner, Document theDocument, TsUserSetting theUserLoginSetting) {
			String aProjectName = "";
			String aQuoteName = "";
			String aBidderContact = "";
			String aJobNumber = "";
			String aArchitechName = "";
			String aEngineerName = "";
			String aBid = "";
			String aplan = "";
			String aJoQuoteRev = "";
			String Location="";
			boolean isStateExists, isCityExists;
			try {
				Paragraph aParagraphHeader=null;
				aParagraphHeader = new Paragraph("PROPOSAL", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				theDocument.add(aParagraphHeader);
				
				if(theProjectNamePDF != "" && theProjectNamePDF != null){
					String theProjectName = theProjectNamePDF.replaceAll("_", "#");
					aProjectName = theProjectName;
				}
				
				isStateExists = theState != null && theState != "";
				isCityExists = theCity != null && theCity != "";
				if(isStateExists && isCityExists)
					Location=theCity+", "+theState.toUpperCase(); 
				else if(isStateExists)
					Location=theState.toUpperCase(); 
				else
					Location=theCity; 
				
				
				/**Attention**/
				if(theBidderContact != "" && theBidderContact != null){
					aBidderContact = theBidderContact;
				}
				
				
				/**  Quote Name in Header Box  **/
				if(theQuoteName != "" && theQuoteName != null){
					aQuoteName = theQuoteName;
				}
				
				
				
				PdfPTable aPdfPTable = new PdfPTable(3); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.setWidths(new int[]{ 4, 1,2});
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				PdfPCell cell = new PdfPCell();
				cell.setBorder(Rectangle.NO_BORDER);
				
				
				
				
				
				Blob aBlob = theUserLoginSetting.getCompanyLogo();
				byte[] image = aBlob.getBytes(1, (int) aBlob.length());
				Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/styles/welcomeCSS/welcomeImages/logofooter_bartos.png"));
				//Image aLeftImage = Image.getInstance(image);
				aLeftImage.scaleAbsolute(100,50);
				aLeftImage.setAbsolutePosition(10f, 675f);
				cell.addElement(new Chunk(aLeftImage, 5, -5));
				
				String Terms="<DIV><DIV><font face='arial' size='1'><i>10350 Olympic Drive, Dallas TX 75220-6040 (214) 350-6871 Fax (214) 350-3481</i></font></DIV><DIV><font face='arial' size='1'><i>3206 Longhorn Blvd, Austin TX 78758  (512) 774-5853 Fax (512) 381-0131</i></font></DIV><DIV><font face='arial' size='1'><i>2901 Wesley Way, Fort Worth, TX 76118-6955  (682) 253-0122 Fax (682) 253-0135</i></font></DIV><DIV><font face='arial' size='1'><i>8788 Westpark Drive, Houston TX 77063  (713) 487-0065 Fax (713) 952-7209</i></font></DIV></DIV><DIV><br></DIV>";
				StringReader sr=new StringReader(Terms);
				List<Element> objects= HTMLWorker.parseToList(sr, null);
				  for (Element element : objects){
					  cell.addElement(element);
				  }
				cell.setRowspan(6);
				aPdfPTable.addCell(cell);
				
				
				
				
				
				aParagraphHeader = new Paragraph("DATE:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable.addCell(cell);
				aParagraphHeader = new Paragraph("January 22,2015", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(cell);
				
				aParagraphHeader = new Paragraph("TO:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable.addCell(cell);
				//aParagraphHeader = new Paragraph(aQuoteName, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphHeader = new Paragraph("Crawford Services", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(cell);
				
				aParagraphHeader = new Paragraph("ATTN:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable.addCell(cell);
				//aParagraphHeader = new Paragraph(aBidderContact, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphHeader = new Paragraph("Brice Crump", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(cell);
				
				aParagraphHeader = new Paragraph("PROJECT:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable.addCell(cell);
				aParagraphHeader = new Paragraph(aProjectName, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(cell);
				
				aParagraphHeader = new Paragraph("LOCATION:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable.addCell(cell);
				aParagraphHeader = new Paragraph(Location, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
				cell = new PdfPCell(aParagraphHeader);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(cell);
				
				
				cell = new PdfPCell();
				cell.setColspan(2);
				cell.setBorder(Rectangle.NO_BORDER);
				aPdfPTable.addCell(cell);
				/*String Terms="<DIV><DIV><font face='arial' size='1'><i>10350 Olympic Drive, Dallas TX 75220-6040 (214) 350-6871 Fax (214) 350-3481</i></font></DIV><DIV><font face='arial' size='1'><i>3206 Longhorn Blvd, Austin TX 78758  (512) 774-5853 Fax (512) 381-0131</i></font></DIV><DIV><font face='arial' size='1'><i>2901 Wesley Way, Fort Worth, TX 76118-6955  (682) 253-0122 Fax (682) 253-0135</i></font></DIV><DIV><font face='arial' size='1'><i>8788 Westpark Drive, Houston TX 77063  (713) 487-0065 Fax (713) 952-7209</i></font></DIV></DIV><DIV><br></DIV>";
				cell = new PdfPCell();
				cell.setColspan(3);
				cell.setBorder(Rectangle.NO_BORDER);
				StringReader sr=new StringReader(Terms);
				List<Element> objects= HTMLWorker.parseToList(sr, null);
				  for (Element element : objects){
					  cell.addElement(element);
				  }
				aPdfPTable.addCell(cell);*/
				
				
				theDocument.add(aPdfPTable);
				
				//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/styles/welcomeCSS/welcomeImages/logofooter_bartos.png"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public void getQuoteFooterInformation(String theJoQuoteHeaderID, Document document, PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru, Font boldFont, Font normalFont, Font boldTitleFont, ByteArrayOutputStream baos, String aFooterText, String theTodayDate) {
			String aRemarks = "";
			List<JoQuotetemplateHeader> aJoQuoteHeaders = null;
			Paragraph aParagraphHeader=null;
			try{
				if(theJoQuoteHeaderID != null && theJoQuoteHeaderID != ""){
					aJoQuoteHeaders =
							(List<JoQuotetemplateHeader>) jobService.getQuotesTemplateHeaderDetails(Integer.parseInt(theJoQuoteHeaderID));
				for(int index=0; index < aJoQuoteHeaders.size(); index++){
					aRemarks = aJoQuoteHeaders.get(index).getRemarks();
					}
				}
				/*if(aRemarks != "" && aRemarks!= null){
					Paragraph aParagraphRemarks = null;
					aParagraphRemarks = new Paragraph("REMARKS: ", 
							FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
					aParagraphRemarks.setAlignment(Element.ALIGN_LEFT);
					document.add(aParagraphRemarks);
					aParagraphRemarks = new Paragraph(aRemarks, 
							FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
					aParagraphRemarks.setAlignment(Element.ALIGN_LEFT);
					document.add(aParagraphRemarks);
				}*/
				document.add( Chunk.NEWLINE );document.add( Chunk.NEWLINE );
				PdfContentByte cb = aPdfWriter.getDirectContent();
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb.setFontAndSize(bf, 8);  
				Font helvetica8BoldBlue = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK);
				ColumnText ct = new ColumnText(cb);
				Phrase myText = new Phrase(aFooterText, helvetica8BoldBlue);
				ct.setSimpleColumn(myText, 25, 65, 550, 100, 10, Element.ALIGN_CENTER);
				ct.go();
				String aUsrNme = "";
				if(aUserBean != null){
					if(aUserBean.getFullName() != null && aUserBean.getFullName() != ""){
						aUsrNme = aUserBean.getFullName();
					}
				}
				Integer aQuoteThru;
				if(theQuoteThru != null && theQuoteThru != ""){
					aQuoteThru = Integer.parseInt(theQuoteThru);
				}else{
					aQuoteThru = 0;
				}
				
				PdfPTable table2 = new PdfPTable(new float[]{0.4f,2.5f,1.3f});
				table2.setTotalWidth(document.getPageSize().getWidth());
				PdfContentByte cb1 = aPdfWriter.getDirectContent();
				
				PdfPCell cel=new PdfPCell();
				cel.setBorder(Rectangle.NO_BORDER);
				table2.addCell(cel);
				
				String notes="Notes: Equipment and accessories not specifically mentioned are not included. Taxes are not included. Products are guaranteed only in so far as warrented by the manufacturer. Equipment for which payment has not been recieved operates without benefit of warranty. Credit terms are Net 30 Days.Frieght is F.O.B. Shipping Point. Returns may not be made without written authorization. This quotation is submitted for acceptance within 30 days provided materials are released for production within 60 days.";
				aParagraphHeader = new Paragraph(notes, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
				PdfPCell cell = new PdfPCell(aParagraphHeader);
				//cell.setBorder(Rectangle.NO_BORDER);
				//cell.setPaddingBottom(8f);
				//cell.setPaddingTop(2f);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				//cell.setBorderColor(Color.WHITE);
				table2.addCell(cell);
				
				
				aParagraphHeader = new Paragraph("Signed:______________________", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
				PdfPCell cell1 = new PdfPCell(aParagraphHeader);
				cell1.setPaddingBottom(10f);
				cell1.setPaddingTop(8f);
				//cell1.setBorder(Rectangle.NO_BORDER);
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
				table2.addCell(cell1);
				
				
				
				
				table2.setWidthPercentage(100);
				table2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.writeSelectedRows(-25, -25, -35, 60, cb1);
				document.close();
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		/*public void addDetailBandColumns(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
				Font theBoldTitleFont, String theParagraphCheck, 
				String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
				String theJoTotalPrice) throws PDFException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
			Paragraph aParagraphHeader=null;
			NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
			try{
				PdfPTable aPdfPTable = null;
				List<joQuoteDetailMstr> ajoQuoteDetailMstr=jobService.getjoQuoteDetailMstr(ConvertintoInteger(theJoQuoteHeaderID));
				for(joQuoteDetailMstr thejoQuoteDetailMstr:ajoQuoteDetailMstr){
				
					if(thejoQuoteDetailMstr.getType()==1){
						float[] aWidths = {16.8f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.setSpacingAfter(8.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase(thejoQuoteDetailMstr.getQuantity(), new Font(FontFactory.getFont(FontFactory.HELVETICA, 10,null))));
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  cell.addElement(element);
						  }
						  aPdfPTable.addCell(cell);
						  theDocument.add(aPdfPTable);
					}else if(thejoQuoteDetailMstr.getType()==2){
						float[] aWidths = {1.3f,15.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						String htmlcontent="<div><font size='2' face='helvetica' >"+thejoQuoteDetailMstr.getQuantity()+"</font></div>";
						StringReader srqty=new StringReader(htmlcontent);
						List<Element> objectsqty= HTMLWorker.parseToList(srqty, null);
						  for (Element element : objectsqty){
							  cell.addElement(element);
						  }
						
						aPdfPTable.addCell(cell);
						
						
						
						
						cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  System.out.println(element);
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
					}
					else if(thejoQuoteDetailMstr.getType()==3){
						float[] aWidths = {1.3f,13.5f,2.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aPdfPTable.addCell(new Phrase(thejoQuoteDetailMstr.getQuantity(), new Font(FontFactory.getFont(FontFactory.HELVETICA, 10,null))));
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						
						
						aParagraphHeader = new Paragraph(defaultFormat.format(thejoQuoteDetailMstr.getSellprice()), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
						cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingTop(2f);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(cell);
						
						theDocument.add(aPdfPTable);
					}else if(thejoQuoteDetailMstr.getType()==4){
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						StringReader sr=new StringReader("<div align='right'><font size='1' face='helvetica' ><b>"+thejoQuoteDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteDetailMstr.getSellprice())+"</b></font></div>");
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						 for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
					}
					
					System.out.println("Id==>"+thejoQuoteDetailMstr.getJoQuoteDetailMstrID());
					System.out.println("type==>"+thejoQuoteDetailMstr.getType());
					System.out.println("textbox==>"+thejoQuoteDetailMstr.getTextbox());
					System.out.println("texteditor==>"+thejoQuoteDetailMstr.getTexteditor());
					System.out.println("SellPrice==>"+thejoQuoteDetailMstr.getSellprice());
				}
				
				List<JoQuoteHeader> aJoQuoteHeaderlst=jobService.getQuotesHeaderDetails(ConvertintoInteger(theJoQuoteHeaderID));
				JoQuoteHeader theJoQuoteHeader=aJoQuoteHeaderlst.get(0);
				if(theJoQuoteHeader.getPrintTotal()==1){
					float[] aWidths = {17.0f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					String printvalue="Total   "+defaultFormat.format(theJoQuoteHeader.getQuoteAmount()); 
					aParagraphHeader = new Paragraph(printvalue, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
					PdfPCell cell = new PdfPCell(aParagraphHeader);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}*/
		
		/*public void addDetailBandColumns(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
				Font theBoldTitleFont, String theParagraphCheck, 
				String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
				String theJoTotalPrice) throws PDFException {
			Integer aIndexTable = 1;
			Integer aAlbaIndex = 1;
			String aAlbaString = "";
			Paragraph aParagraphHeader=null;
			NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
			try{
				PdfPTable aPdfPTable = null;
				List<joQuoteDetailMstr> ajoQuoteDetailMstr=jobService.getjoQuoteDetailMstr(ConvertintoInteger(theJoQuoteHeaderID));
				for(joQuoteDetailMstr thejoQuoteDetailMstr:ajoQuoteDetailMstr){
				
					if(thejoQuoteDetailMstr.getType()==1){
						float[] aWidths = {16.8f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.setSpacingAfter(8.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  System.out.println(element);
							  cell.addElement(element);
						  }
						  aPdfPTable.addCell(cell);
						  theDocument.add(aPdfPTable);
					}else if(thejoQuoteDetailMstr.getType()==2){
						float[] aWidths = {1.5f,15.5f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.setSpacingAfter(8.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aParagraphHeader = new Paragraph(thejoQuoteDetailMstr.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(6);
				        cell.setPaddingTop(6);
				        cell.setPaddingBottom(0);
						aPdfPTable.addCell(cell);
						cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  System.out.println(element);
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
					}
					else if(thejoQuoteDetailMstr.getType()==3){
						float[] aWidths = {1.5f,13.0f,3.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.setSpacingAfter(8.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						
						aParagraphHeader = new Paragraph(thejoQuoteDetailMstr.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(6);
				        cell.setPaddingTop(6);
				        cell.setPaddingBottom(0);
						aPdfPTable.addCell(cell);
						cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						  for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						
						String sellprice=defaultFormat.format(thejoQuoteDetailMstr.getSellprice());
						if(thejoQuoteDetailMstr.getSellprice().compareTo(BigDecimal.ZERO)==0){
							sellprice="";
						}
						aParagraphHeader = new Paragraph(sellprice, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0)));
						cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(0);
				        cell.setPaddingTop(6);
				        cell.setPaddingBottom(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(cell);
						
						theDocument.add(aPdfPTable);
					}else if(thejoQuoteDetailMstr.getType()==4){
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.setSpacingAfter(8.0f);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						StringReader sr=new StringReader("<div align='right'><font size='4' face='helvetica' ><b>"+thejoQuoteDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteDetailMstr.getSellprice())+"</b></font></div>");
						List<Element> objects= HTMLWorker.parseToList(sr, null);
						 for (Element element : objects){
							  cell.addElement(element);
						  }
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
					}
					
					System.out.println("Id==>"+thejoQuoteDetailMstr.getJoQuoteDetailMstrID());
					System.out.println("type==>"+thejoQuoteDetailMstr.getType());
					System.out.println("textbox==>"+thejoQuoteDetailMstr.getTextbox());
					System.out.println("texteditor==>"+thejoQuoteDetailMstr.getTexteditor());
					System.out.println("SellPrice==>"+thejoQuoteDetailMstr.getSellprice());
				}
				
				List<JoQuoteHeader> aJoQuoteHeaderlst=jobService.getQuotesHeaderDetails(ConvertintoInteger(theJoQuoteHeaderID));
				JoQuoteHeader theJoQuoteHeader=aJoQuoteHeaderlst.get(0);
				if(theJoQuoteHeader.getPrintTotal()==1){
					float[] aWidths = {17.0f};
					aPdfPTable = new PdfPTable(aWidths); 
					aPdfPTable.setWidthPercentage(100);
					aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					String printvalue="Total   "+defaultFormat.format(theJoQuoteHeader.getQuoteAmount()); 
					aParagraphHeader = new Paragraph(printvalue, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0)));
					PdfPCell cell = new PdfPCell(aParagraphHeader);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setPadding(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.addCell(cell);
					theDocument.add(aPdfPTable);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}*/
		
		public Integer ConvertintoInteger(String Stringvalue){
			Integer returnvalue=0;
			try {
				returnvalue=Integer.parseInt(Stringvalue);
			} catch (NumberFormatException e) {
				return returnvalue;
			}
			
			return returnvalue;
		}
		public BigDecimal ConvertintoBigDecimal(String Stringvalue){
			BigDecimal returnvalue=new BigDecimal(0);
			try {
				logger.info("Stringvalue"+Stringvalue);
				System.out.println("Stringvalue"+Stringvalue);
				returnvalue=new BigDecimal(Stringvalue);
			} catch (Exception e) {
				e.printStackTrace();
				return returnvalue;
			}
			
			return returnvalue;
		}

		 /*@RequestMapping(value="/viewNewQuoteBidPdfForm", method = RequestMethod.GET) 
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
			 
			 Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			 Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
			 Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
			 Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			 
			 ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
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
				PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			}else{
				 aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				 PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
				
			}
			
			Integer aJoQuoteHeaderID = 0;
			String aQuoteRevId = "";
			*//** get Quote Header ID **//*
			
			aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
		 
			
			
			UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			//** Set PDF application using response **//*
			aResponse.setContentType("application/pdf");
			//aBoldTitleFont.setStyle(Font.UNDERLINE);
			Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			aNormalTitleFont.setStyle(Font.UNDERLINE);
			
			//** Set page Number **//*
			HeaderFooter aFooter = null;
			aFooter= new HeaderFooter(new Phrase("Page: "), true);
			aFooter.setBorder(Rectangle.NO_BORDER);
			aFooter.setAlignment(Element.ALIGN_RIGHT);
			aPDFDocument.setHeader(aFooter);
			aPDFDocument.addTitle("");
			
			
			
			
			
			aPDFDocument.open();
			

			
			
			
			
			//** Set Header Informations Using itext Paragraph **//*
			Paragraph aParagraphHeader = null;
			aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 0)));
			aParagraphHeader.setSpacingAfter(4f);
			aParagraphHeader.setIndentationRight(70f);
			aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
			aPDFDocument.add(aParagraphHeader);
			aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, new Color(255, 255, 255)));
			aPDFDocument.add(aParagraphHeader);
			
			//** HEader with Rich Text Editor Values **//*
			aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
			//if(aUserLoginSetting.getHeaderQuote() == 1){
				aParagraphHeader = new Paragraph();
//				aParagraphHeader.setSpacingBefore(-10);
				aParagraphHeader.setSpacingAfter(-20f);
				String aHtmlString =  aUserLoginSetting.getHeaderText();
				String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
				String aText2 = aText1.replaceAll("`and`nbsp;", " ");
				String[] aTextSplit = aText2.split("</i></font></b></div>");
				String aText3 = "";
				String aText4 = aTextSplit[0];
				for(int i = 1;i<aTextSplit.length;i++){
				aText3 = aTextSplit[i];
				String textnote =aText3+"<br>";
				aText4 = aText4+textnote;
				}
				ArrayList p=new ArrayList();
				StringReader aStrReader = new StringReader(aText4);
				p = HTMLWorker.parseToList(aStrReader, null);
				for (int k = 0; k < p.size(); ++k){
					aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
				}
				aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
				aParagraphHeader.setIndentationLeft(200);
				aParagraphHeader.setIndentationRight(0);
				aPDFDocument.add(aParagraphHeader);
			
				
				aPDFDocument.add( Chunk.NEWLINE );
			
			//** set company logo in PDF header **//*
				Blob aBlob = aUserLoginSetting.getCompanyLogo();
				byte[] image = aBlob.getBytes(1, (int) aBlob.length());
				//Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				Image aLeftImage = Image.getInstance(image);
				aLeftImage.scaleAbsolute(90,45);
				aLeftImage.setAbsolutePosition(30f, 750f);
				aLeftImage.scaleToFit(150, 75);
				aPDFDocument.add(aLeftImage);
				//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				PdfPTable aTable = new PdfPTable(2); 
				aTable.setWidthPercentage(100); 
			
			
			
			
			
			
			JoQuoteHeader aJoQuoteHeader = jobService.getjoQuoteAmount(aJoQuoteHeaderID);
			
			
			
			getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
					aJoQuoteHeader.getQuoteRev(), aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting);
			
			addDetailBandColumns(aPDFDocument, aJoQuoteHeaderID.toString(), aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
					theManufactureCheck, theTodayDate, "0", theJoTotalPrice);
			
			getFooterInformation(aJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, aJoQuoteHeader.getCreatedByName(), theTodayDate,aUserLoginSetting);
			
			if(WriteorView!=null&&WriteorView.equals("write")){
				aPDFDocument.close();
				
			}
			
			
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
			}
		finally
		{
			if(fos!=null){
			fos.close();
			}
		}
			 return null;
		 
		 
		 }*/
		 
		 
		 
		 
		 
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

			 
			 Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			 Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
			 Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
			 Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			 
			 ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
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
				 PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
				
			//}
			
			Integer aJoQuoteHeaderID = 0;
			String aQuoteRevId = "";
			/** get Quote Header ID **/
			
			//aJoQuoteHeaderID = pdfService.getQuoteHeaderID(theQuoteTypeID, theQuoteRev, theJoMasterID, aQuoteRevId);
			aJoQuoteHeaderID=ConvertintoInteger(theJoQuoteHeaderID);
			
			UserBean aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			
			//** Set PDF application using response **//*
			aResponse.setContentType("application/pdf");
			//aBoldTitleFont.setStyle(Font.UNDERLINE);
			Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			aNormalTitleFont.setStyle(Font.UNDERLINE);
			
			//** Set page Number **//*
			HeaderFooter aFooter = null;
			aFooter= new HeaderFooter(new Phrase("Page: "), true);
			aFooter.setBorder(Rectangle.NO_BORDER);
			aFooter.setAlignment(Element.ALIGN_RIGHT);
			aPDFDocument.setHeader(aFooter);
			aPDFDocument.addTitle("");
			
			
			
			
			FontFactory.registerDirectories();
			aPDFDocument.open();
			

			
			
			
			
			//** Set Header Informations Using itext Paragraph **//*
			Paragraph aParagraphHeader = null;
			aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 0)));
			aParagraphHeader.setSpacingAfter(4f);
			aParagraphHeader.setIndentationRight(70f);
			aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
			aPDFDocument.add(aParagraphHeader);
			aParagraphHeader = new Paragraph("Quote", FontFactory.getFont(FontFactory.HELVETICA, 2, Font.BOLD, new Color(255, 255, 255)));
			aPDFDocument.add(aParagraphHeader);
			
			//** HEader with Rich Text Editor Values **//*
			aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
			//if(aUserLoginSetting.getHeaderQuote() == 1){
				aParagraphHeader = new Paragraph();
//				aParagraphHeader.setSpacingBefore(-10);
				aParagraphHeader.setSpacingAfter(-20f);
				String aHtmlString =  aUserLoginSetting.getHeaderText();
				String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
				String aText2 = aText1.replaceAll("`and`nbsp;", " ");
				String[] aTextSplit = aText2.split("</i></font></b></div>");
				String aText3 = "";
				String aText4 = aTextSplit[0];
				for(int i = 1;i<aTextSplit.length;i++){
				aText3 = aTextSplit[i];
				String textnote =aText3+"<br>";
				aText4 = aText4+textnote;
				}
				ArrayList p=new ArrayList();
				StringReader aStrReader = new StringReader(aText4);
				p = HTMLWorker.parseToList(aStrReader, null);
				for (int k = 0; k < p.size(); ++k){
					aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
				}
				aParagraphHeader.setAlignment(Element.ALIGN_RIGHT);
				aParagraphHeader.setIndentationLeft(200);
				aParagraphHeader.setIndentationRight(0);
				aPDFDocument.add(aParagraphHeader);
			
				
				aPDFDocument.add( Chunk.NEWLINE );
			
			//** set company logo in PDF header **//*
				Blob aBlob = aUserLoginSetting.getCompanyLogo();
				byte[] image = aBlob.getBytes(1, (int) aBlob.length());
				//Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				Image aLeftImage = Image.getInstance(image);
				aLeftImage.scaleAbsolute(90,45);
				aLeftImage.setAbsolutePosition(30f, 750f);
				aLeftImage.scaleToFit(150, 75);
				aPDFDocument.add(aLeftImage);
				//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				PdfPTable aTable = new PdfPTable(2); 
				aTable.setWidthPercentage(100); 
			
			
			
			
			
			
			JoQuotetemplateHeader aJoQuotetemplateHeader = jobService.getJoQuotetemplateHeader(aJoQuoteHeaderID);
			
			if(theProjectNamePDF!=null)
				theProjectNamePDF=theProjectNamePDF.replaceAll("`and`", "&");
			
			
			getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
					theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,null);
			
			addDetailBandColumns_template(aPDFDocument, aJoQuoteHeaderID.toString(), aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
					theManufactureCheck, theTodayDate, "0", theJoTotalPrice);
			
			getFooterInformation(aJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, aJoQuotetemplateHeader.getCreatedByName(), theTodayDate,aUserLoginSetting);
			
			
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTransactionException("<b>theQuoteName:</b>"+theQuoteName,"QuotePDFController",e,session,theRequest);
			}
			 return null;
		 }
		 
		 
		 /*public void addDetailBandColumns_template(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
					Font theBoldTitleFont, String theParagraphCheck, 
					String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
					String theJoTotalPrice) throws PDFException {
				Integer aIndexTable = 1;
				Integer aAlbaIndex = 1;
				String aAlbaString = "";
				Paragraph aParagraphHeader=null;
				NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
				try{
					PdfPTable aPdfPTable = null;
					List<joQuoteTempDetailMstr> ajoQuoteTempDetailMstr=jobService.getjoQuoteTempDetailMstr(ConvertintoInteger(theJoQuoteHeaderID));
					for(joQuoteTempDetailMstr thejoQuoteTempDetailMstr:ajoQuoteTempDetailMstr){
					
						if(thejoQuoteTempDetailMstr.getType()==1){
							float[] aWidths = {16.8f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.setSpacingAfter(8.0f);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							//aPdfPTable.addCell(new Phrase(thejoQuoteTempDetailMstr.getQuantity(), new Font(FontFactory.getFont(FontFactory.HELVETICA, 10,null))));
							PdfPCell cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							StringReader sr=new StringReader(thejoQuoteTempDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							  for (Element element : objects){
								  cell.addElement(element);
							  }
							  aPdfPTable.addCell(cell);
							  theDocument.add(aPdfPTable);
						}else if(thejoQuoteTempDetailMstr.getType()==2){
							float[] aWidths = {1.5f,15.5f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.setSpacingAfter(8.0f);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							
							aParagraphHeader = new Paragraph(thejoQuoteTempDetailMstr.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
							PdfPCell cell = new PdfPCell(aParagraphHeader);
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPaddingLeft(0);
					        cell.setPaddingRight(6);
					        cell.setPaddingTop(6);
					        cell.setPaddingBottom(0);
							aPdfPTable.addCell(cell);
							
							cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							StringReader sr=new StringReader(thejoQuoteTempDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							  for (Element element : objects){
								  System.out.println(element);
								  cell.addElement(element);
							  }
							aPdfPTable.addCell(cell);
							theDocument.add(aPdfPTable);
						}
						else if(thejoQuoteTempDetailMstr.getType()==3){
							float[] aWidths = {1.5f,13.0f,3.0f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.setSpacingAfter(8.0f);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							
							aParagraphHeader = new Paragraph(thejoQuoteTempDetailMstr.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
							PdfPCell cell = new PdfPCell(aParagraphHeader);
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPaddingLeft(0);
					        cell.setPaddingRight(6);
					        cell.setPaddingTop(6);
					        cell.setPaddingBottom(0);
							aPdfPTable.addCell(cell);
							
							cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							StringReader sr=new StringReader(thejoQuoteTempDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							  for (Element element : objects){
								  cell.addElement(element);
							  }
							aPdfPTable.addCell(cell);
							String sellprice=defaultFormat.format(thejoQuoteTempDetailMstr.getSellprice());
							if(thejoQuoteTempDetailMstr.getSellprice().compareTo(BigDecimal.ZERO)==0){
								sellprice="";
							}
							
							aParagraphHeader = new Paragraph(sellprice, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0)));
							cell = new PdfPCell(aParagraphHeader);
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPaddingLeft(0);
					        cell.setPaddingRight(0);
					        cell.setPaddingTop(6);
					        cell.setPaddingBottom(0);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							aPdfPTable.addCell(cell);
							
							theDocument.add(aPdfPTable);
						}else if(thejoQuoteTempDetailMstr.getType()==4){
							float[] aWidths = {17.0f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.setSpacingAfter(8.0f);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							PdfPCell cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							StringReader sr=new StringReader("<div align='right'><font size='4' face='helvetica' ><b>"+thejoQuoteTempDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteTempDetailMstr.getSellprice())+"</b></font></div>");
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							 for (Element element : objects){
								  cell.addElement(element);
							  }
							aPdfPTable.addCell(cell);
							theDocument.add(aPdfPTable);
						}
						
						
						
						
						System.out.println("Id==>"+thejoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
						System.out.println("type==>"+thejoQuoteTempDetailMstr.getType());
						System.out.println("textbox==>"+thejoQuoteTempDetailMstr.getTextbox());
						System.out.println("texteditor==>"+thejoQuoteTempDetailMstr.getTexteditor());
						System.out.println("SellPrice==>"+thejoQuoteTempDetailMstr.getSellprice());
					}
					
					
					JoQuotetemplateHeader theJoQuotetemplateHeader= jobService.getJoQuotetemplateHeader(ConvertintoInteger(theJoQuoteHeaderID));
					if(theJoQuotetemplateHeader.getPrintTotal()==1){
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						String printvalue="Total   "+defaultFormat.format(theJoQuotetemplateHeader.getQuoteAmount()); 
						aParagraphHeader = new Paragraph(printvalue, FontFactory.getFont(FontFactory.HELVETICA, 14,  Font.BOLD, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
					}
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}*/
		 
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
		 
		 public void type3ElementAdd(List<Element> objects,Document theDocument,String Quantity,String sellprice) throws DocumentException{
			 PdfPTable aPdfPTable = null;
			 Paragraph aParagraphHeader=null;
			 float[] aWidths = {1.5f,13.0f,3.0f};
			    
				Boolean ifconblock=true;
				int i=0;
				int listlength=objects.size()-1;
				  for (Element element : objects){
					  if(ifconblock){
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						if(listlength==i){
							aPdfPTable.setSpacingAfter(8.0f);
						}
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aParagraphHeader = new Paragraph(Quantity, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(6);
				        cell.setPaddingTop(6);
				        cell.setPaddingBottom(0);
						aPdfPTable.addCell(cell);
						cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPadding(0);
						cell.addElement(element);
						aPdfPTable.addCell(cell);
						aParagraphHeader = new Paragraph(sellprice, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0)));
						cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(0);
				        cell.setPaddingTop(6);
				        cell.setPaddingBottom(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						aPdfPTable.addCell(cell);
						theDocument.add(aPdfPTable);
						  ifconblock=false;
					  }else{
						  aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							if(listlength==i){
								aPdfPTable.setSpacingAfter(8.0f);
							}
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							aParagraphHeader = new Paragraph("", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
							PdfPCell cell = new PdfPCell(aParagraphHeader);
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPaddingLeft(0);
					        cell.setPaddingRight(6);
					        cell.setPaddingTop(6);
					        cell.setPaddingBottom(0);
							aPdfPTable.addCell(cell);
							cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							cell.addElement(element);
							aPdfPTable.addCell(cell);
							aParagraphHeader = new Paragraph("", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0)));
							cell = new PdfPCell(aParagraphHeader);
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPaddingLeft(0);
					        cell.setPaddingRight(0);
					        cell.setPaddingTop(6);
					        cell.setPaddingBottom(0);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							aPdfPTable.addCell(cell);
							theDocument.add(aPdfPTable);
					  }
					  
				  }
				
		 }
		 
		 public void addDetailBandColumns(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
					Font theBoldTitleFont, String theParagraphCheck, 
					String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
					String theJoTotalPrice) throws PDFException {
				Integer aIndexTable = 1;
				Integer aAlbaIndex = 1;
				String aAlbaString = "";
				Paragraph aParagraphHeader=null;
				NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
				try{
					PdfPTable aPdfPTable = null;
					List<joQuoteDetailMstr> ajoQuoteDetailMstr=jobService.getjoQuoteDetailMstr(ConvertintoInteger(theJoQuoteHeaderID));
					for(joQuoteDetailMstr thejoQuoteDetailMstr:ajoQuoteDetailMstr){
					
						if(thejoQuoteDetailMstr.getType()==1){
							StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							type1ElementAdd(objects,theDocument);
						}else if(thejoQuoteDetailMstr.getType()==2){
							StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							type2ElementAdd(objects,theDocument,thejoQuoteDetailMstr.getQuantity());
						}
						else if(thejoQuoteDetailMstr.getType()==3){
							String sellprice=defaultFormat.format(thejoQuoteDetailMstr.getSellprice());
							if(thejoQuoteDetailMstr.getSellprice().compareTo(BigDecimal.ZERO)==0){
								sellprice="";
							}
							StringReader sr=new StringReader(thejoQuoteDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							type3ElementAdd(objects,theDocument,thejoQuoteDetailMstr.getQuantity(),sellprice);
						}else if(thejoQuoteDetailMstr.getType()==4){
							float[] aWidths = {17.0f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.setSpacingAfter(8.0f);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							PdfPCell cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							StringReader sr=new StringReader("<div align='right'><font size='4' face='helvetica' ><b>"+thejoQuoteDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteDetailMstr.getSellprice())+"</b></font></div>");
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							 for (Element element : objects){
								  cell.addElement(element);
							  }
							aPdfPTable.addCell(cell);
							theDocument.add(aPdfPTable);
						}
						
						System.out.println("Id==>"+thejoQuoteDetailMstr.getJoQuoteDetailMstrID());
						System.out.println("type==>"+thejoQuoteDetailMstr.getType());
						System.out.println("textbox==>"+thejoQuoteDetailMstr.getTextbox());
						System.out.println("texteditor==>"+thejoQuoteDetailMstr.getTexteditor());
						System.out.println("SellPrice==>"+thejoQuoteDetailMstr.getSellprice());
					}
					
					List<JoQuoteHeader> aJoQuoteHeaderlst=jobService.getQuotesHeaderDetails(ConvertintoInteger(theJoQuoteHeaderID));
					JoQuoteHeader theJoQuoteHeader=aJoQuoteHeaderlst.get(0);
					if(theJoQuoteHeader.getPrintTotal()==1){
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						String printvalue="Total   "+defaultFormat.format(theJoQuoteHeader.getQuoteAmount()); 
						aParagraphHeader = new Paragraph(printvalue, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
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
		 
		 public void addDetailBandColumns_template(Document theDocument, String theJoQuoteHeaderID, Font theBoldFont, Font theNormalFont, 
					Font theBoldTitleFont, String theParagraphCheck, 
					String theManufactureCheck, String theTodayDate, String theDiscountAmount, 
					String theJoTotalPrice) throws PDFException {
				Integer aIndexTable = 1;
				Integer aAlbaIndex = 1;
				String aAlbaString = "";
				Paragraph aParagraphHeader=null;
				NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
				try{
					PdfPTable aPdfPTable = null;
					List<joQuoteTempDetailMstr> ajoQuoteTempDetailMstr=jobService.getjoQuoteTempDetailMstr(ConvertintoInteger(theJoQuoteHeaderID));
					for(joQuoteTempDetailMstr thejoQuoteTempDetailMstr:ajoQuoteTempDetailMstr){
					
						if(thejoQuoteTempDetailMstr.getType()==1){
							StringReader sr=new StringReader(thejoQuoteTempDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							type1ElementAdd(objects,theDocument);
						}else if(thejoQuoteTempDetailMstr.getType()==2){
							StringReader sr=new StringReader(thejoQuoteTempDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							type2ElementAdd(objects,theDocument,thejoQuoteTempDetailMstr.getQuantity());
						}
						else if(thejoQuoteTempDetailMstr.getType()==3){
							String sellprice=defaultFormat.format(thejoQuoteTempDetailMstr.getSellprice());
							if(thejoQuoteTempDetailMstr.getSellprice().compareTo(BigDecimal.ZERO)==0){
								sellprice="";
							}
							StringReader sr=new StringReader(thejoQuoteTempDetailMstr.getTexteditor());
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							type3ElementAdd(objects,theDocument,thejoQuoteTempDetailMstr.getQuantity(),sellprice);
						}else if(thejoQuoteTempDetailMstr.getType()==4){
							float[] aWidths = {17.0f};
							aPdfPTable = new PdfPTable(aWidths); 
							aPdfPTable.setWidthPercentage(100);
							aPdfPTable.setSpacingAfter(8.0f);
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							PdfPCell cell = new PdfPCell();
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPadding(0);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							StringReader sr=new StringReader("<div align='right'><font size='4' face='helvetica' ><b>"+thejoQuoteTempDetailMstr.getTextbox()+"  "+defaultFormat.format(thejoQuoteTempDetailMstr.getSellprice())+"</b></font></div>");
							List<Element> objects= HTMLWorker.parseToList(sr, null);
							 for (Element element : objects){
								  cell.addElement(element);
							  }
							aPdfPTable.addCell(cell);
							theDocument.add(aPdfPTable);
						}
						
						
						
						
						System.out.println("Id==>"+thejoQuoteTempDetailMstr.getJoQuoteTempDetailMstrID());
						System.out.println("type==>"+thejoQuoteTempDetailMstr.getType());
						System.out.println("textbox==>"+thejoQuoteTempDetailMstr.getTextbox());
						System.out.println("texteditor==>"+thejoQuoteTempDetailMstr.getTexteditor());
						System.out.println("SellPrice==>"+thejoQuoteTempDetailMstr.getSellprice());
					}
					
					
					JoQuotetemplateHeader theJoQuotetemplateHeader= jobService.getJoQuotetemplateHeader(ConvertintoInteger(theJoQuoteHeaderID));
					if(theJoQuotetemplateHeader.getPrintTotal()==1){
						float[] aWidths = {17.0f};
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						String printvalue="Total   "+defaultFormat.format(theJoQuotetemplateHeader.getQuoteAmount()); 
						aParagraphHeader = new Paragraph(printvalue, FontFactory.getFont(FontFactory.HELVETICA, 14,  Font.BOLD, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
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
					   theDocument.add(aPdfPTable);
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
						theDocument.add(aPdfPTable);
				  }
				  i=i+1;
			  }
		 }
		 
		 public void type2ElementAdd(List<Element> objects,Document theDocument,String Quantity) throws DocumentException{
			 PdfPTable aPdfPTable = null;
			 Paragraph aParagraphHeader=null;
			 float[] aWidths = {1.5f,15.5f};
			    
				Boolean ifconblock=true;
				int i=0;
				int listlength=objects.size()-1;
				  for (Element element : objects){
					  if(ifconblock){
						aPdfPTable = new PdfPTable(aWidths); 
						aPdfPTable.setWidthPercentage(100);
						if(listlength==i){
							aPdfPTable.setSpacingAfter(8.0f);
						}
						aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						aParagraphHeader = new Paragraph(Quantity, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
						PdfPCell cell = new PdfPCell(aParagraphHeader);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setPaddingLeft(0);
				        cell.setPaddingRight(6);
				        cell.setPaddingTop(6);
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
							if(listlength==i){
								aPdfPTable.setSpacingAfter(8.0f);
							}
							aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
							aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							aParagraphHeader = new Paragraph("", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new Color(0, 0, 0)));
							PdfPCell cell = new PdfPCell(aParagraphHeader);
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setPaddingLeft(0);
					        cell.setPaddingRight(6);
					        cell.setPaddingTop(6);
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
		 
		 
		 public void getHeaderInformation(String theJoQuoteHeaderID, Document document, PdfWriter aPdfWriter, UserBean aUserBean, String theQuoteThru, Font boldFont, Font normalFont, Font boldTitleFont, ByteArrayOutputStream baos, String thesubmittedby, String theTodayDate,TsUserSetting aUserLoginSetting,String alternatetext) {
				try{
					//Image
					Blob aBlob = aUserLoginSetting.getCompanyLogo();
					byte[] image = aBlob.getBytes(1, (int) aBlob.length());
					Image aLeftImage = Image.getInstance(image);
					//aLeftImage.scaleAbsolute(90,45);
					//aLeftImage.setAbsolutePosition(30f, 750f);
					aLeftImage.scaleToFit(150, 75);
					
					
					
					boolean alternatext_bool=false;
					/*if(alternatetext!=null){
						alternatext_bool=true;
					}*/
					
					PdfPTable table = new PdfPTable(2); 
					table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					table.setWidthPercentage(100);
							 PdfPCell apdfpcell=new PdfPCell(aLeftImage);
							 apdfpcell.setBorder(Rectangle.NO_BORDER);
							 table.addCell(apdfpcell);
							 apdfpcell=new PdfPCell(new Phrase("Page: "+aPdfWriter.getPageNumber()));
							 apdfpcell.setBorder(Rectangle.NO_BORDER);
							 apdfpcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				             table.addCell(apdfpcell);
		            table.completeRow();
		            		 apdfpcell=new PdfPCell();
		            		 apdfpcell.setBorder(Rectangle.NO_BORDER);
		            		 	ArrayList p=new ArrayList();
		            		 	StringReader strReader = new StringReader(aUserLoginSetting.getHeaderText());
		            		 	if(alternatext_bool){
		            		 		strReader = new StringReader(alternatetext);
		            		 	}
								p = HTMLWorker.parseToList(strReader, null);
								for (int k = 0; k < p.size(); ++k){
									apdfpcell.addElement((com.lowagie.text.Element)p.get(k));
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
				}catch (Exception e) {
					//e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		 private void getProjectInformation(String theProjectNamePDF, String theQuoteName, String theBidderContact, String theJobNumber, String theBidDate, String theArchitect, String thePlanDate, String theJoQuoteRev, Font theBoldFont, Font theNormalFont, 
					Font theBoldTitleFont, String theState, String theCity, String theEnginner, Document theDocument, TsUserSetting theUserLoginSetting,String quotestype) throws DocumentException, IOException {
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
			ArrayList aInputString=new ArrayList();
			StringReader aStrReader = new StringReader(aText3);
			aInputString = HTMLWorker.parseToList(aStrReader, null);
			for (int k = 0; k < aInputString.size(); ++k){
			System.out.println("Line by Line------------------->"+aInputString.get(k));
			aParagraph.add((com.lowagie.text.Element)aInputString.get(k));
			}
			//aParagraph.add(aText3);
			String sHTML = "";
			sHTML = aText3.substring(aText3.lastIndexOf("text-align"),aText3.length());
			sHTML = sHTML.substring(sHTML.indexOf("text-align"),sHTML.indexOf(";"));
			sHTML = sHTML.substring(sHTML.indexOf(":")+1,sHTML.length()).trim();
			if("left".equalsIgnoreCase(sHTML))
			aParagraph.setAlignment(Element.ALIGN_LEFT);
			else if("right".equalsIgnoreCase(sHTML))
			aParagraph.setAlignment(Element.ALIGN_RIGHT);
			else if("center".equalsIgnoreCase(sHTML))
			aParagraph.setAlignment(Element.ALIGN_CENTER);
			else if("justify".equalsIgnoreCase(sHTML))
			aParagraph.setAlignment(Element.ALIGN_JUSTIFIED);

			//aParagraph.setAlignment(Element.ALIGN_LEFT);
			aParagraph.setSpacingAfter(5);
			theDocument.add(aParagraph);
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
			 Document aPDFDocument = new Document(PageSize.A4, 36, 36, 36, 36);
			 Font aBoldFont = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD));
			 Font aNormalFont = new Font(FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL));
			 Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			 ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
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
				PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			}else{
				 aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				 PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
				
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
			aPDFDocument.open();
			
			HeaderFooter aFooter = null;
			aFooter= new HeaderFooter(new Phrase("Page: "), true);
			aFooter.setBorder(Rectangle.NO_BORDER);
			aFooter.setAlignment(Element.ALIGN_RIGHT);
			aPDFDocument.setHeader(aFooter);
			
			aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
			
			JoQuoteHeader aJoQuoteHeader = jobService.getjoQuoteAmount(theJoQuoteHeaderID);
			String alternatetext=getalternatetextinhtml(theJoQuoteHeaderID.toString());
			getHeaderInformation(theJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, aJoQuoteHeader.getCreatedByName(), theTodayDate,aUserLoginSetting,alternatetext);
			
			
			if(theProjectNamePDF!=null)
				theProjectNamePDF=theProjectNamePDF.replaceAll("`and`", "&");
			
			
			
			getProjectInformation(theProjectNamePDF, theQuoteName, theBidderContact, theJobNumber, theBidDate, theArchitect, thePlanDate, 
					theJoQuoteRev, aBoldFont, aNormalFont, aBoldTitleFont, theState, theCity, theEnginner, aPDFDocument, aUserLoginSetting,quotes_numberandtype);
			
			addDetailBandColumns(aPDFDocument, theJoQuoteHeaderID.toString(), aBoldFont, aNormalFont, aBoldTitleFont, theParagraphCheck, 
					theManufactureCheck, theTodayDate, "", theJoTotalPrice);
			
			getFooterInformation(theJoQuoteHeaderID.toString(), aPDFDocument, aPdfWriter, aUserBean, theQuoteThru, aBoldFont, aNormalFont, aBoldTitleFont, aByteArrayOutputStream, aJoQuoteHeader.getCreatedByName(), theTodayDate,aUserLoginSetting);
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
		 
		 private String getalternatetextinhtml(String theJoQuoteHeaderID) {
				// TODO Auto-generated method stub
			String returnhtmltext=jobService.getalternatetextinhtml(theJoQuoteHeaderID);
				return returnhtmltext;
			}
		 
		 
}

