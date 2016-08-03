package com.turborep.turbotracker.job.service;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.turborep.turbotracker.job.dao.JobQuoteDetailBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.util.SessionConstants;

@Service("pdfService")
@Transactional
public class PDFServiceImpl implements PDFService{

	protected static Logger itsLogger = Logger.getLogger(JobServiceImpl.class);
	protected Phrase itsHeader;
	protected PdfPTable itsFooter;
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Override
	   public void openPDFStream(Jomaster theJomaster, HttpSession theSession, HttpServletResponse theResponse) throws MalformedURLException, IOException, URISyntaxException, JobException {
		    Frame parent = new Frame();
	  	    FileDialog fd = new FileDialog(parent, "Save",FileDialog.LOAD);
	  	    fd.setDirectory(".");
	  	    fd.show();
	  	  if (fd.getFile() != null) {
			/*URL aFilePath = this.getClass().getClassLoader().getResource("../.././resources/PDF/");
			System.out.println(aFilePath);
			File file = new File(aFilePath.toURI().getPath());
			System.out.println(file);
			String aGetPathInString = aFilePath.getPath();
			String aFileName = "Quotes.pdf";*/
//	  		openPDFLocation(aGetPathInString, aFileName, theJomaster, session);
	  		openPDFLocation(fd.getDirectory(), fd.getFile(), theJomaster, theSession, theResponse);
	  	  }
	   }
		
		private void openPDFLocation(String theDirctory, String theFileName, Jomaster theJomaster, HttpSession theSession, HttpServletResponse theResponse) throws MalformedURLException, IOException, JobException {
			Document aDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			String text = "TERMS:NET 30 DAYS OR FACTORY STANDARD, SUBJECT TO CREDIT APPROVAL.TAXES:NONE INCLUDED." +
					"SHIPMENT:F.O.B.FACTORY OR WAREHOUSE.FREIGHT:PREPAID & ALLOWED WITH $25 NOTIFICATION CHARGE " +
					"ADDED AS REQUESTED.\n\n";
					String headerText   = "QUOTE";
					String headerText0 = "THE UNDERWOOD COMPANIES";
					String headerText1 = "UNDERWOOD AIR SYSTEMS";
					String headerText2 = "UNDERWOOD AIR SYSTEMS * UNDERWOOD HVAC * LOCKWOOD\n";
					String headerText3 = "4450 Commerce Drive SW\nAtlanta,GA 30336\nPHONE (404) 505-2500 \u00a0  FAX (404) 505-2555";
					String aFooterText  = "QUOTATIONS VALID 30 CALENDAR DAYS FROM BID DATE. PRICES FIRM FOR RELEASE 60 DAYS FROM BID DATE " +
													"AND SUBJECT TO 1.5% PER MONTH ESCALATION THEREAFTER. MATERIAL QUOTED BASED ON INFORMATION " +
													"CONTAINED IN THE REFERENCED PORTION OF THE PLANS AND SPECIFICATIONS ONLY.  STANDARD FACTORY " +
													"CONDITIONS OF SALE APPLY.";
			boolean isStateExists, isCityExists;
			FileOutputStream outputstream=null;
		     try {
		    	 UserBean aUserBean;
				 aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
				  outputstream=new FileOutputStream(theDirctory+theFileName+".pdf");
		    	 Date theDate;
				GregorianCalendar aCalendar = new GregorianCalendar();
				theDate = aCalendar.getTime();
				PdfWriter.getInstance(aDocument, outputstream);
//		    	PdfWriter.getInstance(document, new FileOutputStream("/home/likewise-open/SYSVINE/thulasi_ram/workspace/bache/src/main/webapp/resources/PDF/"+theFileName));
		    	Font boldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				Font normalFont = new Font(FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL));
				Font boldTitleFont = new Font(FontFactory.getFont(FontFactory.TIMES, 11, Font.BOLD));
				boldTitleFont.setStyle(Font.UNDERLINE);
				Font normalTitleFont = new Font(FontFactory.getFont(FontFactory.TIMES, 11, Font.NORMAL));
				normalTitleFont.setStyle(Font.UNDERLINE);
//				Rectangle pageSize = aPdfWriter.getPageSize(1);
				HeaderFooter footer = null;
				footer= new HeaderFooter(new Phrase("Page :"), true);
				footer.setBorder(Rectangle.NO_BORDER);
				footer.setAlignment(Element.ALIGN_RIGHT);
				aDocument.setHeader(footer);
				aDocument.addTitle("");
				aDocument.open();
				Paragraph aParagraphHeader = null;
				aParagraphHeader = new Paragraph(headerText, FontFactory.getFont(FontFactory.COURIER, 19, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph(headerText0, FontFactory.getFont(FontFactory.COURIER, 19, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph(headerText1, FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph(headerText2, FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphHeader);
				aParagraphHeader = new Paragraph(headerText3, FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphHeader);
				aDocument.add( Chunk.NEWLINE );
				/*if(theJoQuoteRev != null && theJoQuoteRev != ""){
					PdfPTable aPdfPTable = new PdfPTable(1); 
					aPdfPTable.setWidthPercentage(27); 
					aPdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					aPdfPTable.addCell(new Phrase("NOTE REVISED QUOTE - REV #"+theJoQuoteRev+"", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD, new Color(0, 0, 0))));
					document.add(aPdfPTable);
				}*/
				Image leftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				leftImage.scaleAbsolute(97,84);
				leftImage.setAbsolutePosition(60f, 700f);
				aDocument.add(leftImage);
				Image rightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				rightImage.scaleAbsolute(97,84);
				rightImage.setAbsolutePosition(435f, 700f);
				aDocument.add(rightImage);
				PdfPTable table = new PdfPTable(2); 
				table.setWidthPercentage(100); 
				PdfPCell pdfWordProjectCell = new PdfPCell();
				Color aColor = new Color(124, 252, 0);
				pdfWordProjectCell.setBorderColorLeft(aColor);
				Phrase aProject = new Phrase("  PROJECT: ", boldFont);
				aProject.add(new Phrase(theJomaster.getDescription(),normalFont));
				Phrase aLocation = new Phrase(" LOCATION: ", boldFont); 
				isStateExists = theJomaster.getLocationState() != null && theJomaster.getLocationState() != "";
				isCityExists = theJomaster.getLocationCity() != null && theJomaster.getLocationCity() != "";
				if(isStateExists && isCityExists)
					aLocation.add(new Phrase(theJomaster.getLocationCity()+", "+theJomaster.getLocationState(), normalFont)); 
				else if(isStateExists)
					aLocation.add(new Phrase(theJomaster.getLocationState(), normalFont));
				else
					aLocation.add(new Phrase(theJomaster.getLocationCity(), normalFont));
				Phrase aQuote = new Phrase(" QUOTE TO: ", boldFont);
				aQuote.add(new Phrase(theJomaster.getBinNumber(), normalFont));
				Phrase aAttention = new Phrase("ATTENTION: ", boldFont);
				aAttention.add(new Phrase(theJomaster.getCreditNotes(), normalFont));
				Phrase aQuoteNumber = new Phrase("  QUOTE #: ", boldFont); 
				aQuoteNumber.add(new Phrase(theJomaster.getJobNumber(), normalFont));
				Phrase aSpace = new Phrase("ATTEN", new Font(FontFactory.getFont(FontFactory.TIMES, 11, Font.BOLD, new Color(255, 255, 255))));
				aSpace.add(new Phrase("", normalFont));
				pdfWordProjectCell.addElement(aProject);
				pdfWordProjectCell.addElement(aLocation);
				pdfWordProjectCell.addElement(aQuote);
				pdfWordProjectCell.addElement(aAttention);
				pdfWordProjectCell.addElement(aQuoteNumber);
				pdfWordProjectCell.addElement(aSpace);
				table.addCell(pdfWordProjectCell);
				PdfPCell pdfWordBidDateCell = new PdfPCell();
				pdfWordProjectCell.setBorderColor(aColor);
				Phrase aBidDate = new Phrase(" BID DATE: ", boldFont);
				aBidDate.add(new Phrase(theJomaster.getBidDate().toString(), normalFont));
				Phrase aArchitect = new Phrase(" ARCHITECT:  ", boldFont); 
				aArchitect.add(new Phrase(theJomaster.getAddendumReceived(), normalFont)); 
				Phrase aEngineer = new Phrase("    ENGINEER:  ", boldFont);
				aEngineer.add(new Phrase(theJomaster.getAddendumQuotedThru(), normalFont));
				Phrase aPlanDate = new Phrase("  PLAN DATE:  ", boldFont);
				aPlanDate.add(new Phrase(theJomaster.getPlanDate().toString(), normalFont));
				pdfWordBidDateCell.addElement(aBidDate);
				pdfWordBidDateCell.addElement(aArchitect);
				pdfWordBidDateCell.addElement(aEngineer);
				pdfWordBidDateCell.addElement(aPlanDate);
				table.addCell(pdfWordBidDateCell);
				aDocument.add(table);		
				Paragraph aParagraph = null;
				aParagraph = new Paragraph(text, FontFactory.getFont(FontFactory.TIMES, 8, Font.BOLD, new Color(0, 0, 0)));
				aParagraph.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraph);
				aDocument.add( Chunk.NEWLINE );
				aDocument.add( Chunk.NEWLINE );
				float[] widths = {0.2f, 1.97f, 0.3f, 0.72f, 1.5f, 0.80f};
				PdfPTable aPdfPTable = new PdfPTable(widths); 
				aPdfPTable.setWidthPercentage(100);
				aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				aPdfPTable.addCell(new Phrase("No.", boldTitleFont));
				aPdfPTable.addCell(new Phrase("Item", boldTitleFont));
				aPdfPTable.addCell(new Phrase("Qty.", boldTitleFont));
				aPdfPTable.addCell(new Phrase("Paragraph", boldTitleFont));
				aPdfPTable.addCell(new Phrase("Manufacturer", boldTitleFont));
				aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable.addCell(new Phrase("Price", boldTitleFont));
				aDocument.add(aPdfPTable);
				
			
				PdfPTable aPdfPTableValues = new PdfPTable(widths); 
				aPdfPTableValues.setWidthPercentage(100); 
				aPdfPTableValues.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				List<JobQuoteDetailBean> aQuotesTableValue =
						(List<JobQuoteDetailBean>) itsJobService.getQuotesDetailList(theJomaster.getJoMasterId());
				Integer aIndexTable = 0;
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					aPdfPTable.getRowspanHeight(index, index);
					String aProduct = ""; 
					String aQuantity = ""; 
					String aPharagraph = "";
					String aManufacturer = ""; 
					String aPrice = null;
					String aSinglePrice = "";
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aIndexTable = aIndexTable + 1;
						aProduct = aQuotesTableValue.get(index).getProduct();
					/*}else{
						aIndexTable = aIndexTable + 1;*/
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
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice = aQuotesTableValue.get(index).getPrice().toString();
						String aPriceReplace = aPrice.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						aSinglePrice = "$"+aSpiltPrice[0]+".00";
					}
					if(aQuotesTableValue.get(index).getProduct() != null && aQuotesTableValue.get(index).getProduct() != ""){
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(aIndexTable.toString()+". ", normalFont));
					}else{
						aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						aPdfPTableValues.addCell(new Phrase(" ", normalFont));
					}
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					aPdfPTableValues.addCell(new Phrase(aProduct, normalFont));
					aPdfPTableValues.addCell(new Phrase(aQuantity, normalFont));
					aPdfPTableValues.addCell(new Phrase(aPharagraph, normalFont));
					aPdfPTableValues.addCell(new Phrase(aManufacturer, normalFont));
					aPdfPTableValues.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					aPdfPTableValues.addCell(new Phrase(aSinglePrice, normalFont));
				}
				aDocument.add(aPdfPTableValues);
				aDocument.add( Chunk.NEWLINE );
				Integer aTotalPrice1 = 0;
				Integer aSinglePrice1 = 0;
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					String aPrice1 = null;
					if(aQuotesTableValue.get(index).getPrice() != null){
						aPrice1 = aQuotesTableValue.get(index).getPrice().toString();
						String aPriceReplace = aPrice1.replace(".", ",");
						String[] aSpiltPrice = aPriceReplace.split(",");
						aSinglePrice1 = Integer.parseInt(aSpiltPrice[0]);
						aTotalPrice1 = aTotalPrice1 + aSinglePrice1;
					}
				}
				Paragraph aParagraphSUM = null;
				aParagraphSUM = new Paragraph("\n", 
						FontFactory.getFont(FontFactory.TIMES, 12, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphSUM.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphSUM);
				float[] widths1 = {1.5f, 0.80f};
				PdfPTable aPdfPTable1 = new PdfPTable(widths1); 
				aPdfPTable1.setWidthPercentage(42);
				aPdfPTable1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				aPdfPTable1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				aPdfPTable1.addCell(new Phrase("SUMMATION: ", new Font(FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD))));
				aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
				aPdfPTable1.getDefaultCell().setBorder(Rectangle.TOP);
				aPdfPTable1.addCell(new Phrase("$"+aTotalPrice1.toString()+".00", new Font(FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD))));
				aDocument.add(aPdfPTable1);
				aDocument.add( Chunk.NEWLINE);
				aDocument.add( Chunk.NEWLINE );
				Paragraph aParagraphNotes = null;
				aParagraphNotes = new Paragraph("NOTES : ", 
						FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, new Color(0, 0, 0)));
				aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
				aDocument.add(aParagraphNotes);
				aDocument.add( Chunk.NEWLINE );
				Integer aIndex = 0;
				for(int index = 0 ; index < aQuotesTableValue.size() ; index++){
					String aProductNote = null;
					if(aQuotesTableValue.get(index).getProductNote() != null && aQuotesTableValue.get(index).getProductNote() != ""){
						aIndex = aIndex + 1;
						aProductNote = "("+aIndex+") "+aQuotesTableValue.get(index).getProductNote()+" \n\n";
					}
					aParagraphNotes = new Paragraph(aProductNote, 
							FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL, new Color(0, 0, 0)));
					aParagraphNotes.setAlignment(Element.ALIGN_LEFT);
					aDocument.add(aParagraphNotes);
				}
				aDocument.add( Chunk.NEWLINE );aDocument.add( Chunk.NEWLINE );
				Paragraph aParagraphFooter = null;
				aParagraphFooter = new Paragraph(aFooterText, 
						FontFactory.getFont(FontFactory.TIMES, 8, Font.BOLD, new Color(0, 0, 0)));
				aParagraphFooter.setAlignment(Element.ALIGN_CENTER);
				aDocument.add(aParagraphFooter);
				aDocument.add( Chunk.NEWLINE );
				PdfPTable aFooter = new PdfPTable(new float[]{2.0f,1.0f,1.0f});
				aFooter.setWidthPercentage(100);
				String aUsrNme = "";
				if(theJomaster.getCustomerPonumber() != null && theJomaster.getCustomerPonumber() != ""){
					aUsrNme = theJomaster.getCustomerPonumber();
				}
				aFooter.addCell(new Phrase("SUBMITTED BY:   "+aUsrNme+"", normalFont));
				aFooter.addCell(new Phrase("DATED:   "+theDate.toString()+"", normalFont));
				Integer aQuoteThru;
				if(theJomaster.getBidTime() != null && theJomaster.getBidTime() != ""){
					aQuoteThru = Integer.parseInt(theJomaster.getBidTime());
				}else{
					aQuoteThru = 0;
				}
				aFooter.addCell(new Phrase("THRU ADDENDUM:   - "+aQuoteThru+" - \n", normalFont));
				aDocument.add(aFooter);
		        aDocument.close();
		     } catch (FileNotFoundException e) {
		        e.printStackTrace();
		     } catch (DocumentException e) {
		        e.printStackTrace();
		     } catch (JobException e) {
				itsLogger.error(e.getMessage());
				theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			}finally{
				outputstream.close();
			}
		}
	
		@Override
		public Integer getQuoteHeaderID(String theQuoteTypeID, String theQuoteRev, String theJoMasterID, String theQuoteID) {
			int  aJoHeaderID = 0;
			String aSelectAll = "";
			String aSelectWithCuMaster = "";
			String aSelectWithQuoteRev = "";
			String aSelectWithJomaster = "";
			if(theQuoteID.equalsIgnoreCase("value")){
				aSelectAll = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' and cumastertypeID = '"+theQuoteTypeID+"' ORDER BY QuoteRev DESC";
				aSelectWithCuMaster = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' and cumastertypeID ='"+theQuoteTypeID+"' ORDER BY QuoteRev DESC";
				aSelectWithQuoteRev = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' ORDER BY QuoteRev DESC";
				aSelectWithJomaster = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' ORDER BY QuoteRev DESC";
			}else{
				aSelectAll = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' and cumastertypeID = '"+theQuoteTypeID+"' ORDER BY joQuoteHeaderID DESC";
				aSelectWithCuMaster = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' and cumastertypeID ='"+theQuoteTypeID+"' ORDER BY joQuoteHeaderID DESC";
				aSelectWithQuoteRev = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' ORDER BY joQuoteHeaderID DESC";
				aSelectWithJomaster = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' ORDER BY joQuoteHeaderID DESC";
			}
			Session session = null;
			try {
				session = itsSessionFactory.openSession();
				Query queryAll = session.createSQLQuery(aSelectAll);
				List<?> aListAll = queryAll.list();
				Query queryCumas = session.createSQLQuery(aSelectWithCuMaster);
				List<?> aListCuMas = queryCumas.list();
				Query queryQuote = session.createSQLQuery(aSelectWithQuoteRev);
				List<?> aListQuote = queryQuote.list();
				Query queryJoma = session.createSQLQuery(aSelectWithJomaster);
				List<?> aListJoma = queryJoma.list();
				if(!queryAll.list().isEmpty()){
					aJoHeaderID = (Integer) aListAll.get(0);
				}else if(!queryCumas.list().isEmpty()){
					aJoHeaderID = (Integer) aListCuMas.get(0);
				}else if(!queryCumas.list().isEmpty()){
						aJoHeaderID = (Integer) aListQuote.get(0);
				}else{
					aJoHeaderID = (Integer) aListJoma.get(0);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(),e);
			}finally {
				session.flush();
				session.close();
				
				aSelectAll =null;
				aSelectWithCuMaster =null;
				aSelectWithQuoteRev  =null;
				aSelectWithJomaster  =null;
			}
			return aJoHeaderID;
		}
		
		@Override
		public Integer getHeaderID(Integer theQuoteTypeID, Integer theJoMasterID) {
			int  aJoHeaderID = 0;
			String	aSelectAll = "SELECT joQuoteHeaderID FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' and cumastertypeID = '"+theQuoteTypeID+"' ORDER BY QuoteRev DESC";
			Session aSession = null;
			try {
				aSession = itsSessionFactory.openSession();
				Query queryAll = aSession.createSQLQuery(aSelectAll);
				List<?> aListAll = queryAll.list();
				if(!queryAll.list().isEmpty()){
					aJoHeaderID = (Integer) aListAll.get(0);
				}
			} catch (Exception e) {
				itsLogger.error(e.getMessage(),e);
			}finally {
				aSession.flush();
				aSession.close();
				aSelectAll =null;
			}
			return aJoHeaderID;
		}
		
		@Override
		public String getQuotesRev(String theQuoteTypeID, String theQuoteRev, String theJoMasterID) {
			String aSelectAll = "";
			if(theQuoteTypeID != ""){
				aSelectAll = "SELECT QuoteRev FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' and cumastertypeID = '"+theQuoteTypeID+"' ORDER BY joQuoteHeaderID DESC";
			}else{
				aSelectAll = "SELECT QuoteRev FROM joQuoteHeader where jomasterID = '"+theJoMasterID+"' ORDER BY joQuoteHeaderID DESC";
			}
			Session aSession = null;
			String aQuoteRev = "";
			try {
				aSession = itsSessionFactory.openSession();
				Query aQuery = aSession.createSQLQuery(aSelectAll);
				List<?> aList = aQuery.list();
				if(!aList.isEmpty())
					aQuoteRev = (String) aList.get(0);
			}catch (Exception e) {
				itsLogger.error("Exception while getting the PO LineItem list: " + e.getMessage(), e);
			} finally {
				aSession.flush();
				aSession.close();
				
				aSelectAll =null;
			}
			return aQuoteRev;
		}
		
		@Override
		public ConnectionProvider  connectionForJasper(){
			Session printBeanSession =itsSessionFactory.openSession();
		    SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) printBeanSession.getSessionFactory();
		    ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		    printBeanSession.flush();
		    printBeanSession.close();
			return connectionProvider;
		}
}
