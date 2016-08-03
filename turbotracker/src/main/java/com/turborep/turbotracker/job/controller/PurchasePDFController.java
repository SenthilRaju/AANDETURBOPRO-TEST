/**
 * This Class is Used to View the Purchase Order as PDF
 */
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
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.turborep.turbotracker.PDF.PDFGenerator;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.PurchaseService;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;


@Controller
@RequestMapping("/purchasePDFController")
public class PurchasePDFController {
	
Logger logger = Logger.getLogger(JobQuoteFormController.class);
	
	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;

	@Resource(name="vendorService")
	private VendorServiceInterface itsVendorService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	
	@RequestMapping(value="/viewPurchasePdfForm", method = RequestMethod.GET)
	public @ResponseBody String viewPdfFrom(@RequestParam(value="vePOID", required= false) Integer theVePoId, @RequestParam(value="puchaseOrder", required= false) String thePuchaseOrder,
											HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException, MessagingException{
		/** A4 page  With Document **/
			Document aPDFDocument = new Document(PageSize.A4, 30, 100, 30, 30);
			String aHeaderText   = "ORDER STATUS";
			ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			FileOutputStream fos = null;
			Rxcontact aRxcontact = new Rxcontact();
			try{
				/** Set PDF application using response **/
				aResponse.setContentType("application/pdf");
				File aDir1 = new File ("/var/quotePDF/");
				if(thePuchaseOrder.equalsIgnoreCase("purchaseGen")){
					fos =  new FileOutputStream(aDir1.getPath()+"/PuchaseGeneralOrder.pdf");
					PdfWriter.getInstance(aPDFDocument, fos);
				}else{
					PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				}
				Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
				aBoldTitleFont.setStyle(Font.UNDERLINE);
				Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				/** Open PDF Document **/
				aPDFDocument.open();
				
				/** Set Header Informations Using itext Paragraph **/
				Paragraph aParagraphHeader = null;
				aParagraphHeader = new Paragraph(aHeaderText, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
				aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
				aPDFDocument.add(aParagraphHeader);
				
				/** Space for header and body **/
				aPDFDocument.add( Chunk.NEWLINE );
				
				/** get values from vepo details **/
				Vepo aPO = itsVendorService.getVePo(theVePoId);
				
				/**font declaration **/
				Font theBoldFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
				Font theNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				
				/** table declaration**/
				PdfPTable aTable = new PdfPTable(2); 
				aTable.setWidthPercentage(50);
				
				/** aPdforderdetailscell**/
				PdfPCell aPdforderdetailscell = new PdfPCell();
				aPdforderdetailscell.setBorder(Rectangle.NO_BORDER);
				
				Phrase aDate= new Phrase("  DATE: ", theBoldFont  );
				aPdforderdetailscell.addElement(aDate);
				Phrase aVendor= new Phrase("  VENDOR: ", theBoldFont  );
				aPdforderdetailscell.addElement(aVendor);
				Phrase aEmpty= new Phrase(" ", theBoldFont  );
				aPdforderdetailscell.addElement(aEmpty);
				aPdforderdetailscell.addElement(aEmpty);
				Phrase aAttn= new Phrase("  ATTN: ", theBoldFont  );
				aPdforderdetailscell.addElement(aAttn);
				Phrase aOrderdate= new Phrase("  ORDER DATE: ", theBoldFont  );
				aPdforderdetailscell.addElement(aOrderdate);
				Phrase aOurpo= new Phrase("  OUR P.O#: ", theBoldFont  );
				aPdforderdetailscell.addElement(aOurpo);
				Phrase aJob= new Phrase("  JOB: ", theBoldFont  );
				aPdforderdetailscell.addElement(aJob);
				Phrase aPOVAL= new Phrase("  PO # ",theBoldFont);
				aPdforderdetailscell.addElement(aPOVAL);
				Phrase aShipto= new Phrase("  SHIP TO: ", theBoldFont  );
				aPdforderdetailscell.addElement(aShipto);
				
				/** get vendor and ship to details**/
				Rxaddress aRxAddress = null;
				Rxaddress aShioToRxAddress = null;
				//Integer rxAddressID = itsJobService.getRxAddressID(aPO.getRxVendorId());
				if(aPO.getRxVendorId() != null){
					aRxAddress = itsJobService.getRxAddressByRxAddressID(aPO.getRxVendorId());
				}
				String theVendorAddress =  "";
				String theShipToAddress =  "";
				String aOrderDate = "";
				String addressType = "";
				Integer addressId = null;
				aRxcontact = itsJobService.getContactDetails(aPO.getRxVendorContactId());
				String contactName = null;
				if(aRxcontact!=null){
					contactName = aRxcontact.getFirstName()+" "+aRxcontact.getLastName();
				}
				if(aPO.getShipToMode()==0){
					aShioToRxAddress = itsPurchaseService.getSelectedShiptoaddress("warehouse", aPO.getPrWarehouseId());
				}else if(aPO.getShipToMode()==1){
					aShioToRxAddress = itsPurchaseService.getSelectedShiptoaddress("customer", aPO.getRxShipToId());
				}else if(aPO.getShipToMode()==2){
					aShioToRxAddress = itsPurchaseService.getSelectedShiptoaddress("jobsite", aPO.getRxShipToId());
				}else if(aPO.getShipToMode()==3){
					aShioToRxAddress = itsPurchaseService.getSelectedShiptoaddress("other", aPO.getRxShipToAddressId());
				}
				int ij = 0;
				logger.info("rxAddressID# "+aRxAddress);
				if(aRxAddress != null){
					if(aRxAddress.getAddress1() != null || aRxAddress.getAddress2() != null || aRxAddress.getCity() != null || aRxAddress.getState() != null || aRxAddress.getZip() != null){
						theVendorAddress = aRxAddress.getAddress1()==null?"": aRxAddress.getAddress1()+ ", "+ aRxAddress.getAddress2()==null?"": aRxAddress.getAddress2()+ ", " + aRxAddress.getCity()==null?"":aRxAddress.getCity()+ ",  \n" + aRxAddress.getState() + " " + aRxAddress.getZip();
					}
				}
				if(aShioToRxAddress != null){
					if(aShioToRxAddress.getAddress1() != null || aShioToRxAddress.getAddress2() != null || aShioToRxAddress.getCity() != null || aShioToRxAddress.getState() != null || aShioToRxAddress.getZip() != null){
						if(aShioToRxAddress.getAddress1() ==null){
							ij++;
						}if(aShioToRxAddress.getAddress2() ==null){
							ij++;
						}if(aShioToRxAddress.getCity() ==null){
							ij++;
						}if(aShioToRxAddress.getState() ==null){
							ij++;
						}if(aShioToRxAddress.getZip() ==null){
							ij++;
						}
						theShipToAddress = aShioToRxAddress.getAddress1() + ", " + aShioToRxAddress.getAddress2() + ", \n" + aShioToRxAddress.getCity() + ", \n" + aShioToRxAddress.getState() + " " + aShioToRxAddress.getZip();
					}
				}
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				if(aPO.getOrderDate() != null){
					aOrderDate = dateFormat.format(aPO.getOrderDate());
				}
				/** aPdfordervaluecell**/
				logger.info("theVendorAddress# "+theVendorAddress);
				PdfPCell aPdfordervaluecell = new PdfPCell();
				aPdfordervaluecell.setBorder(Rectangle.NO_BORDER);
				Phrase bEmpty= new Phrase(" ", theBoldFont  );
				Date date = new Date();
				if(date!=null){
				Phrase aDateVAL= new Phrase(dateFormat.format(date),theNormalFont);
				aPdfordervaluecell.addElement(aDateVAL);
				}
				Phrase aVendorVAL= new Phrase(theVendorAddress, theNormalFont);
				aPdfordervaluecell.addElement(aVendorVAL);
				for(int i=0;i<=ij;i++){
					aPdfordervaluecell.addElement(bEmpty);
				}
				if(contactName!=null && !contactName.trim().equals("")){
				Phrase aAttnVAL= new Phrase(contactName, theNormalFont);
				aPdfordervaluecell.addElement(aAttnVAL);
				}else{
					aPdfordervaluecell.addElement(bEmpty);
				}
				if(aOrderDate!=null && !aOrderDate.trim().equals("")){
				Phrase aOrderdateVAL= new Phrase(aOrderDate, theNormalFont);
				aPdfordervaluecell.addElement(aOrderdateVAL);
				}
				else{
					aPdfordervaluecell.addElement(bEmpty);
				}
				if(aPO.getPonumber()!=null && !aPO.getPonumber().trim().equals("")){
					Phrase aOurpoVAL= new Phrase(aPO.getPonumber(), theNormalFont);
					aPdfordervaluecell.addElement(aOurpoVAL);
				}
				else{
					aPdfordervaluecell.addElement(bEmpty);
				}
				if(aPO.getTag()!=null && !aPO.getTag().trim().equals("")){
					Phrase aJobName= new Phrase(aPO.getTag(), theNormalFont);
					aPdfordervaluecell.addElement(aJobName);
				}else{
					aPdfordervaluecell.addElement(bEmpty);
				}
				if(aPO.getCustomerPonumber()!=null && !aPO.getCustomerPonumber().trim().equals("")){
				Phrase aJobVAL= new Phrase(aPO.getCustomerPonumber(), theNormalFont);
				aPdfordervaluecell.addElement(aJobVAL);
				}else{
					aPdfordervaluecell.addElement(bEmpty);
				}
				Phrase aShiptoVAL= new Phrase(theShipToAddress, theNormalFont);
				aPdfordervaluecell.addElement(aShiptoVAL);
				aTable.addCell(aPdforderdetailscell);
				aTable.addCell(aPdfordervaluecell);
				aPDFDocument.add(aTable);
	
				/** Space for header and body **/
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				
				/** purchase general title value InformAtion **/
				Paragraph aParagraphInfo = null;
				aParagraphInfo = new Paragraph("PLEASE	PROVIDE THE FOLLOWING INFORMATION:",  aNormalTitleFont);
				aParagraphInfo.setIndentationLeft(130);
				aParagraphInfo.setAlignment(Element.ALIGN_CENTER);
				aPDFDocument.add(aParagraphInfo);
				aPDFDocument.add( Chunk.NEWLINE );
				aParagraphInfo = new Paragraph("YOU ORDER#:  ______________________________________",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setIndentationLeft(110);
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphInfo);
				aParagraphInfo = new Paragraph("SCHEDULED SHIP DATE:  ______________________________________",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setIndentationLeft(70);
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphInfo);
				aParagraphInfo = new Paragraph("NEW SCHEDULED SHIP:  ______________________________________",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setIndentationLeft(70);
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphInfo);
				aParagraphInfo = new Paragraph("IF SHIPPED, DATE SHIPPED:  ______________________________________",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setIndentationLeft(70);
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphInfo);
				aParagraphInfo = new Paragraph("CARRIED NAME:  ______________________________________",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setIndentationLeft(70);
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphInfo);
				aParagraphInfo = new Paragraph("PRO#:  ______________________________________",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aParagraphInfo.setIndentationLeft(70);
				aParagraphInfo = new Paragraph("SHIPPED:      COMPLETE_______      PARTIAL_______   ",  FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new Color(0, 0, 0)));
				aParagraphInfo.setAlignment(Element.ALIGN_RIGHT);
				aParagraphInfo.setIndentationLeft(70);
				aPDFDocument.add(aParagraphInfo);
				
				/** Space for header and body **/
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				aPDFDocument.add( Chunk.NEWLINE );
				
				/** Footer Info **/
				Paragraph aParagraphFooter = null;
				aParagraphFooter = new Paragraph("PLEASE RETURN THE INFORMATION VIA FAX AS SOON AS POSSIBLE, THANK YOU!", aBoldTitleFont);
				aParagraphFooter.setIndentationLeft(100);
				aParagraphFooter.setAlignment(Element.ALIGN_CENTER);
				aPDFDocument.add(aParagraphFooter);
				
				aPDFDocument.close();
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>theVePoId:</b>"+theVePoId,"PurchasePDFController",e,session,therequest);
			}
			finally
			{
				if(fos!=null){
				fos.close();
				}
			}
		return null;
	}
	
	public HashMap<String, Object>  SetBillToParameter(HashMap<String, Object> params,Vepo theVePO,TsUserSetting theUserLoginSetting
			,Rxaddress theRxAddressBillTo, Rxmaster theRxmaster,Rxaddress theRxAddressOtherBillTo	
			){
		String BilltoName="";
		String BilltoAddress1="";
		String BilltoAddress2="";
		String BilltoCity="";
		String BilltoState="";
		String BilltoZip="";
		if (theVePO.getBillToIndex() == null
				|| theVePO.getBillToIndex() == 0) {
			//if (theUserLoginSetting.getItsBillTo() != 0) {
				if (!theUserLoginSetting.getBillToDescription()
						.equalsIgnoreCase("")) {
					BilltoName = theUserLoginSetting.getBillToDescription();
				}
				if (!theUserLoginSetting.getBillToAddress1()
						.equalsIgnoreCase("")) {
					BilltoAddress1 =theUserLoginSetting.getBillToAddress1();
				}
				if (!theUserLoginSetting.getBillToAddress2()
						.equalsIgnoreCase("")) {
					BilltoAddress2 =theUserLoginSetting.getBillToAddress2();
				}
				if (!theUserLoginSetting.getBillToCity().equalsIgnoreCase(
						"")) {
					BilltoCity = theUserLoginSetting.getBillToCity();
				}
				if (!theUserLoginSetting.getBillToState().equalsIgnoreCase(
						"")) {
					if (theUserLoginSetting.getBillToCity()
							.equalsIgnoreCase("")) {
						BilltoState = theUserLoginSetting.getBillToState();
					} else {
						BilltoState = theUserLoginSetting.getBillToState();
					}
				}
				if (!theUserLoginSetting.getBillToZip()
						.equalsIgnoreCase("")) {
					BilltoZip =  theUserLoginSetting.getBillToZip();
				}
			//}
		} else if (theVePO.getBillToIndex() == 1) {
			
			if (theRxAddressBillTo != null) {
				if (!theRxAddressBillTo.getName().equalsIgnoreCase("")) {
					BilltoName = theRxAddressBillTo.getName();
				} else if (!theRxmaster.getName().equalsIgnoreCase("")) {
					BilltoName = theRxmaster.getName() ;
				}
				if (!theRxAddressBillTo.getAddress1().equalsIgnoreCase("")) {
					BilltoAddress1 = theRxAddressBillTo.getAddress1() ;
				}
				if (!theRxAddressBillTo.getAddress2().equalsIgnoreCase("")) {
					BilltoAddress2 = theRxAddressBillTo.getAddress2() ;
				}
				if (!theRxAddressBillTo.getCity().equalsIgnoreCase("")) {
					BilltoCity = theRxAddressBillTo.getCity();
				}
				if (!theRxAddressBillTo.getState().equalsIgnoreCase("")) {
					if (theRxAddressBillTo.getCity().equalsIgnoreCase("")) {
						BilltoState = theRxAddressBillTo.getState();
					} else {
						BilltoState = theRxAddressBillTo.getState();
					}
				}
				if (!theRxAddressBillTo.getZip().equalsIgnoreCase("")) {
					BilltoZip = theRxAddressBillTo.getZip();
				}
				
			}
		} else if (theVePO.getBillToIndex() == 2) {
			
			if (theRxAddressOtherBillTo != null) {
				if (!theRxAddressOtherBillTo.getName().equalsIgnoreCase("")) {
					BilltoName = theRxAddressOtherBillTo.getName();
				}
				if (!theRxAddressOtherBillTo.getAddress1()
						.equalsIgnoreCase("")) {
					BilltoAddress1 = theRxAddressOtherBillTo.getAddress1() ;
				}
				if (!theRxAddressOtherBillTo.getAddress2()
						.equalsIgnoreCase("")) {
					BilltoAddress2 = theRxAddressOtherBillTo.getAddress2() ;
				}
				if (!theRxAddressOtherBillTo.getCity().equalsIgnoreCase("")) {
					BilltoCity = theRxAddressOtherBillTo.getCity();
				}
				if (!theRxAddressOtherBillTo.getState()
						.equalsIgnoreCase("")) {
					if (theRxAddressOtherBillTo.getCity().equalsIgnoreCase(
							"")) {
						BilltoState = theRxAddressOtherBillTo.getState();
					} else {
						BilltoState =  theRxAddressOtherBillTo.getState();
					}
				}
				if (!theRxAddressOtherBillTo.getZip().equalsIgnoreCase("")) {
					BilltoZip = theRxAddressOtherBillTo.getZip();
				}
				
			}
		}
		
		params.put("BILLTO_NAME",BilltoName);params.put("BILLTO_ADDRESS1",BilltoAddress1);
		params.put("BILLTO_ADDRESS2",BilltoAddress2);
		params.put("BILLTO_CITY",BilltoCity);
		params.put("BILLTO_STATE",BilltoState);
		params.put("BILLTO_ZIP", BilltoZip);
		return params;
	}
	
	public HashMap<String, Object> SetShipTOParameter(HashMap<String, Object> params,List<Prwarehouse> thePrwarehouses,
			Vepo theVePO,Integer theShipToAddrID,Jomaster theJomaster,Rxaddress theRxAddressOtherShipTo,
			Rxmaster theRxmaster,Rxaddress theRxAddressShipTo
			){
		String ShipToName="";
		String ShipToAddress1="";
		String ShipToAddress2="";
		String ShipToCity="";
		String ShipToState="";
		String ShipToZip="";
		String Tag="";
		
	//	if(theVePO.getJoReleaseId()>)
		if(theVePO.getShipToMode()==0){
			for(int prInd=0;prInd<thePrwarehouses.size();prInd++){
				
				
				if(thePrwarehouses.get(prInd).getPrWarehouseId()==theVePO.getPrWarehouseId()){
					if (!thePrwarehouses.get(prInd).getDescription()
							.equalsIgnoreCase("")) {
						ShipToName = thePrwarehouses.get(prInd).getDescription() ;
						if (theVePO != null) {
							if (!theVePO.getTag().equalsIgnoreCase(
									"")) {
								Tag =  "c/o : "
										+theVePO.getTag() ;
							}
						}
					}
					
					if (thePrwarehouses.get(prInd).getAddress1()!= null && !thePrwarehouses.get(prInd).getAddress1()
							.equalsIgnoreCase("")) {
						ShipToAddress1 = thePrwarehouses.get(prInd)
										.getAddress1() ;
					}
					if (thePrwarehouses.get(prInd).getAddress2()!=null && !thePrwarehouses.get(prInd).getAddress2()
							.equalsIgnoreCase("")) {
						ShipToAddress2 = thePrwarehouses.get(prInd)
										.getAddress2() ;
					}
					if (thePrwarehouses.get(prInd).getCity()!=null && !thePrwarehouses.get(prInd).getCity()
							.equalsIgnoreCase("")) {
						ShipToCity =  thePrwarehouses.get(prInd).getCity();
					}
					if (thePrwarehouses.get(prInd).getState()!= null && !thePrwarehouses.get(prInd).getState()
							.equalsIgnoreCase("")) {
					if (thePrwarehouses.get(prInd).getCity()!=null && thePrwarehouses.get(prInd).getCity()
								.equalsIgnoreCase("")) {
							ShipToState =  thePrwarehouses.get(prInd)
											.getState();
						} else {
							ShipToState = thePrwarehouses.get(prInd)
											.getState();
						}
					}
					if (thePrwarehouses.get(prInd).getZip() != null && !thePrwarehouses.get(prInd).getZip()
							.equalsIgnoreCase("")) {
						ShipToZip =thePrwarehouses.get(prInd).getZip();
					}

				
				}
			}
		}
		else if(theVePO.getShipToMode()==1){

			if (theRxAddressShipTo != null) {
				if (theRxmaster.getName() != null &&!theRxmaster.getName().equalsIgnoreCase("")) {
					ShipToName = theRxmaster.getName();
					if (theVePO != null) {
						if (theVePO.getTag() != null && !theVePO.getTag().equalsIgnoreCase("")) {
							Tag = " c/o :   "
									+ theVePO.getTag() ;
						}
					}
				}
				if (theRxAddressShipTo.getAddress1() != null && !theRxAddressShipTo.getAddress1().equalsIgnoreCase(
						"")) {
					ShipToAddress1 =theRxAddressShipTo.getAddress1();
				}
				if (theRxAddressShipTo.getAddress2() !=null && !theRxAddressShipTo.getAddress2().equalsIgnoreCase(
						"")) {
					ShipToAddress2 = theRxAddressShipTo.getAddress2();
				}
				if (theRxAddressShipTo.getCity() != null && !theRxAddressShipTo.getCity().equalsIgnoreCase("")) {
					ShipToCity = theRxAddressShipTo.getCity();
				}
				if (theRxAddressShipTo.getState() != null && !theRxAddressShipTo.getState().equalsIgnoreCase("")) {
					if (theRxAddressShipTo.getCity().equalsIgnoreCase(
							"")) {
						ShipToState =  theRxAddressShipTo.getState();
					} else {
						ShipToState =  theRxAddressShipTo.getState();
					}
				}
				if (theRxAddressShipTo.getZip() != null && !theRxAddressShipTo.getZip().equalsIgnoreCase("")) {
					ShipToZip =  theRxAddressShipTo.getZip();
				}
				
			}
		
			
		}
		else if(theVePO.getShipToMode()==2){
			if (theJomaster != null) {
				if (theRxmaster.getName() != null && !theRxmaster.getName().equalsIgnoreCase("")) {
					ShipToName = theRxmaster.getName() ;
					if (theVePO != null) {
						if (theVePO.getTag() != null && !theVePO.getTag().equalsIgnoreCase("")) {
							Tag = " c/o : "
									+ theVePO.getTag();
						}
					}
				}
				if (theJomaster.getLocationAddress1() != null && !theJomaster.getLocationAddress1()
						.equalsIgnoreCase("")) {
					ShipToAddress1 = theJomaster.getLocationAddress1() ;
				}
				if (theJomaster.getLocationAddress2() != null && !theJomaster.getLocationAddress2()
						.equalsIgnoreCase("")) {
					ShipToAddress2 =  theJomaster.getLocationAddress2() ;
				}
				if (theJomaster.getLocationCity() != null && !theJomaster.getLocationCity().equalsIgnoreCase("")) {
					ShipToCity = theJomaster.getLocationCity();
				}
				if (theJomaster.getLocationState() !=null && !theJomaster.getLocationState()
						.equalsIgnoreCase("")) {
					if (theJomaster.getLocationCity()!= null && theJomaster.getLocationCity().equalsIgnoreCase(
							"")) {
						ShipToState = theJomaster.getLocationState();
					} else {
						ShipToState = theJomaster.getLocationState();
					}
				}
				if (theJomaster.getLocationZip()!=null && !theJomaster.getLocationZip().equalsIgnoreCase("")) {
					ShipToZip = theJomaster.getLocationZip();
				}
				/*
				 * aShipToAddress = theRxmaster.getName()+
				 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				 * +theJomaster.getLocationAddress1() +
				 * ",\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t "
				 * + theJomaster.getLocationAddress2() +
				 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				 * + theJomaster.getLocationCity() + ", " +
				 * theJomaster.getLocationState() + ", " +
				 * theJomaster.getLocationZip();
				 */
			}
		} else if(theVePO.getShipToMode()==3){

			if (theRxAddressOtherShipTo != null) {
				if (theRxAddressOtherShipTo.getName() != null&& !theRxAddressOtherShipTo.getName().equalsIgnoreCase("")) {
					ShipToName = theRxAddressOtherShipTo.getName();
					if (theVePO != null) {
						if (theVePO.getTag() != null && !theVePO.getTag().equalsIgnoreCase("")) {
							Tag ="c/o : "
									+ theVePO.getTag() ;
						}
					}
				}
				if (theRxAddressOtherShipTo.getAddress1()!=null && !theRxAddressOtherShipTo.getAddress1()
						.equalsIgnoreCase("")) {
					ShipToAddress1=theRxAddressOtherShipTo.getAddress1();
				}
				if (theRxAddressOtherShipTo.getAddress2()!=null && !theRxAddressOtherShipTo.getAddress2()
						.equalsIgnoreCase("")) {
					ShipToAddress2= theRxAddressOtherShipTo.getAddress2() ;
				}
				if (theRxAddressOtherShipTo.getCity()!=null && !theRxAddressOtherShipTo.getCity().equalsIgnoreCase("")) {
					ShipToCity=theRxAddressOtherShipTo.getCity();
				}
				if (theRxAddressOtherShipTo.getState()!=null && !theRxAddressOtherShipTo.getState()
						.equalsIgnoreCase("")) {
					if (theRxAddressOtherShipTo.getCity().equalsIgnoreCase(
							"")) {
						ShipToState = theRxAddressOtherShipTo.getState();
					} else {
						ShipToState= theRxAddressOtherShipTo.getState();
					}
				}
				if (theRxAddressOtherShipTo.getZip() != null && !theRxAddressOtherShipTo.getZip().equalsIgnoreCase("")) {
					ShipToZip =theRxAddressOtherShipTo.getZip();
				}
				/*
				 * aShipToAddress = theRxAddressOtherShipTo.getName()+
				 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				 * +theRxAddressOtherShipTo.getAddress1() +
				 * ",\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
				 * theRxAddressOtherShipTo.getAddress2() +
				 * "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
				 * theRxAddressOtherShipTo.getCity() + ", " +
				 * theRxAddressOtherShipTo.getState() + ", " +
				 * theRxAddressOtherShipTo.getZip();
				 */
			}
		}
		//jenith ENd
		
		params.put("SHIPTO_NAME", ShipToName);
		params.put("SHIPTO_ADDRESS1", ShipToAddress1);
		params.put("SHIPTO_ADDRESS2", ShipToAddress2);
		params.put("SHIPTO_CITY", ShipToCity);
		params.put("SHIPTO_STATE", ShipToState);
		params.put("SHIPTO_ZIP", ShipToZip);
		
		return params;
	}
	
	public HashMap<String, Object> SetShipTO(HashMap<String, Object> params,List<Prwarehouse> thePrwarehouses,
			Vepo theVePO,Integer theShipToAddrID,Jomaster theJomaster,Rxaddress theRxAddressOtherShipTo,
			Rxmaster theRxmaster,Rxaddress theRxAddressShipTo) throws VendorException{
		
		Rxaddress rxadd = new Rxaddress();
		
		if(theVePO.getShipToMode()==0){
		rxadd = itsPurchaseService.getSelectedShiptoaddress("warehouse", theVePO.getPrWarehouseId());
		}else if(theVePO.getShipToMode()==1){
		rxadd = itsPurchaseService.getSelectedShiptoaddress("customer", theVePO.getRxShipToId());
		}else if(theVePO.getShipToMode()==2){
		rxadd = itsPurchaseService.getSelectedShiptoaddress("jobsite", theVePO.getRxShipToId());
		}else if(theVePO.getShipToMode()==3){
		rxadd = itsPurchaseService.getSelectedShiptoaddress("other", theVePO.getRxShipToAddressId());
		}
		
		params.put("SHIPTO_NAME", rxadd.getName());
		params.put("SHIPTO_ADDRESS1", rxadd.getAddress1());
		params.put("SHIPTO_ADDRESS2",rxadd.getAddress2());
		params.put("SHIPTO_CITY", rxadd.getCity());
		params.put("SHIPTO_STATE", rxadd.getState());
		params.put("SHIPTO_ZIP", rxadd.getZip());
		
		return params;
	}
	
	
	
	@RequestMapping(value = "/viewPDFLineItemForm", method = RequestMethod.GET)
	public @ResponseBody void viewPDFLineItemForm(@RequestParam(value="vePOID", required= false) Integer theVePoId, 
			@RequestParam(value="puchaseOrder", required= false) String thePuchaseOrder,
			@RequestParam(value="jobNumber", required= false) String theJobNumber, 
			@RequestParam(value="rxMasterID", required= false) Integer theRxMasterID,
			@RequestParam(value="manufacturerID", required= false) Integer theManufacturerID, 
			@RequestParam(value="shipToAddrID", required= false) Integer theShipToAddrID,
			@RequestParam(value="WriteorView", required= false) String WriteorView,
			@RequestParam(value="releaseType", required= false) String releaseType,
			@RequestParam(value="joMasterID", required= false) Integer joMasterID,
			HttpSession session, HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, DocumentException, VendorException, UserException, JobException, SQLException, MessagingException{
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/Vendor_PurchaseOrder.jrxml");
		
			con = itspdfService.connectionForJasper();
			TsUserSetting aUserLoginSetting = null;
			aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
			logger.info("RxMasterID::"+theRxMasterID+"$$JobNumber::"+theJobNumber);
			if(null == theRxMasterID)
			{
				
				try {
					Integer aRxMasterId = itsPurchaseService.getVendorRxMasterID(theVePoId);
					theRxMasterID = aRxMasterId;
					logger.info("RxMasterID-1::"+theRxMasterID+"$$JobNumber::"+theJobNumber);
				} catch (VendorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(theJobNumber==null||theJobNumber.equals("")){
				theJobNumber = itsPurchaseService.getJobNumberFromVePO(theVePoId);
				logger.info("RxMasterIDApplied::"+theRxMasterID+"$$JobNumberEmpty::"+theJobNumber);
				
			}
			
			
			Vepo aPO = null;
			Veshipvia aVeshipvia = null;
			TsUserLogin aLogin = null;
			List<Vepodetail> aVePODetail = null;
			List<Prwarehouse> aPrwarehouses = null;
			List<Vefreightcharges> aVefreightcharges = null;
			System.out.println("vepoid::"+theVePoId);
			boolean booleanvalue=false;
			String purchaseorCommission="PurchaseOrder_";
			if(releaseType!=null && releaseType.equals("Commission")){
				purchaseorCommission="CommissionOrder_";
				booleanvalue=true;
			}
			if(theVePoId != null){
				aPO = itsVendorService.getVePo(theVePoId);
				
				filename=purchaseorCommission+aPO.getPonumber()+".pdf";
				
				if(aPO.getOrderedById() != null){
					aLogin = userService.getSingleUserDetails(aPO.getOrderedById());
				}
				aVePODetail = itsJobService.getPOReleaseLineItem(theVePoId);
			}
			
			
			Rxaddress aRxAddress = null;
			if(aPO.getRxVendorId() != null){
				aRxAddress = itsJobService.getRxAddress(aPO.getRxVendorId());
				System.out.println("Hello I am from method");
			}
			Rxmaster aRxmasterName = null;
			if(aPO.getRxVendorId() != null){
				aRxmasterName = itsJobService.getSingleRxMasterDetails(aPO.getRxVendorId());
			}
			if(aPO.getVeShipViaId() != null){
				aVeshipvia = itsVendorService.getVeShipVia(aPO.getVeShipViaId());
			}
			Rxcontact aRxcontact = null;
			if(aPO.getRxVendorContactId() != null){
				aRxcontact = itsVendorService.getContactDetails(aPO.getRxVendorContactId());
			}
			Rxmaster aRxmasterAddress = null;
			if(theManufacturerID != null){
				aRxmasterAddress = itsJobService.getSingleRxMasterDetails(theManufacturerID);
			}
			VeFactory aVendorDetail = null;
			if(theManufacturerID != null){
				aVendorDetail = itsVendorService.getSingleVeFactoryDetails(theManufacturerID);
			}
			Rxmaster aRxmaster = null;
			if(theRxMasterID != null){
				aRxmaster = itsJobService.getSingleRxMasterDetails(theRxMasterID);
			}
			Rxaddress aRxAddressBillTo = null;
			String billToName = null;
			Rxmaster billToRxmaster;
			if(aPO.getRxBillToId() != null){
				aRxAddressBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToId(), "bill");
				billToRxmaster = itsJobService.getSingleRxMasterDetails(aPO.getRxBillToId());
				aRxAddressBillTo.setName(billToRxmaster.getName());
			}
			Rxaddress aRxAddressShipTo = null;
			if(aPO.getRxShipToId() != null){
				aRxAddressShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToAddressId(), "shipToCus");
			}
			Rxaddress aRxAddressOtherBillTo = null;
			if(aPO.getRxBillToAddressId() != null){
				aRxAddressOtherBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToAddressId(), "billTo");
			}
			Rxaddress aRxAddressOtherShipTo = null;
			if(aPO.getRxShipToAddressId() != null){
				aRxAddressOtherShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToAddressId(), "shipToOther");
			}
			Jomaster aJomaster = null;
			logger.info("JobNumber::"+theJobNumber);
			if(theJobNumber != "" && theJobNumber != null){
				aJomaster = itsJobService.getSingleJobDetails(theJobNumber,joMasterID);
				logger.info("JobNumber::"+aJomaster.getLocationCity());
			}
			aPrwarehouses = itsJobService.getWareHouse();
			
			String add1="";
			String add2="";
			String city="";
			String sta="";
			String zipp="";
			
			if(aRxAddress.getAddress1()!=null){
				add1=aRxAddress.getAddress1();
			}if(aRxAddress.getAddress2()!=null){
				add2=aRxAddress.getAddress2();
			}if(aRxAddress.getCity()!=null){
				city=aRxAddress.getCity();
			}if(aRxAddress.getState()!=null){
				sta=aRxAddress.getState();
			}if(aRxAddress.getZip()!=null){
				zipp=aRxAddress.getZip();
			}
			
			
			params.put("PONUMBER", aPO.getPonumber());params.put("VENDORNAME", aRxmasterName.getName());params.put("ADDRESS1", add1+" "+add2);
			params.put("CITY", city+" "+ sta);
			params.put("Zip", zipp);params.put("TAG", aPO.getTag());
			 
			params=SetBillToParameter(params,aPO, aUserLoginSetting	, aRxAddressBillTo,  aRxmaster, aRxAddressOtherBillTo);
			
			params=SetShipTO(params, aPrwarehouses,aPO,theShipToAddrID, aJomaster, aRxAddressOtherShipTo,aRxmaster,aRxAddressShipTo);
			
			String aATTN = "";
			if (aRxcontact != null) {
				if (aRxcontact.getFirstName() != ""
						&& aRxcontact.getLastName() != "") {
					aATTN = aRxcontact.getFirstName().replaceAll("null", "")
							+ " "
							+ aRxcontact.getLastName().replaceAll("null", "");
				}
			}
			
			params.put("ATTN", aATTN);
			
			String aFrieght = "";
			String aShipVia = "";
			String aOrderBy = "";
			String aOrderByDate = "";
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			if (aPO.getVeFreightChargesId() != null) {
				
				aVefreightcharges = itsJobService.getFreights();
				 for(int a=0;a<aVefreightcharges.size();a++){
					 if (aPO.getVeFreightChargesId()==aVefreightcharges.get(a).getVeFreightChargesId()) {
						 aFrieght=aVefreightcharges.get(a).getDescription();
						 
					 }
				 }
			}
			if (aVeshipvia != null) {
				if (aVeshipvia.getDescription() != null
						&& aVeshipvia.getDescription() != "") {
					aShipVia = aVeshipvia.getDescription();
				}
			}
			if (aPO.getOrderDate() != null) {
				aOrderByDate = dateFormat.format(aPO.getOrderDate());
			}
			
			if (aLogin != null) {
				aOrderBy = aLogin.getFullName();
			} else {
				aOrderBy = "";
			}
			
			
			
			params.put("ORDER_DATE", aOrderByDate);
			params.put("ORDERED_BY", aOrderBy);params.put("SHIP_VIA", aShipVia);params.put("DATE_WANTED", aPO.getDateWanted());
			params.put("CUSTOMER_PO", aPO.getCustomerPonumber());params.put("FRIEGHT_CHG", aFrieght);
			params.put("SPECIAL_INSTRUCTIONS", aPO.getSpecialInstructions());
			
			String aSubTotal = "$0.00";
			String aFreightCost = "$0.00";
			String aTaxRate = "$0.00";
			String aPOTotal = "$0.00";
			double aPOTotalInt = 0;
			BigDecimal aTotalSub = new BigDecimal(0);
			Double aTotalSubInt = 0.00;
			String aTotalInt = "";
			Format format = NumberFormat.getCurrencyInstance(new Locale("en",
					"us"));
			if (!aVePODetail.isEmpty()) {
				for (int index = 0; index < aVePODetail.size(); index++) {
					BigDecimal aUnitCost = JobUtil.floorFigureoverall(aVePODetail.get(index).getUnitCost(),2);
					BigDecimal aPriceMult = aVePODetail.get(index).getPriceMultiplier();
					BigDecimal aQuantity = JobUtil.floorFigureoverall(aVePODetail.get(index).getQuantityOrdered(),2);
					if(aUnitCost==null){
						aUnitCost=BigDecimal.ZERO;
					}
					if(aPriceMult==null||aPriceMult.compareTo(new BigDecimal("1"))==0){
						aPriceMult=new BigDecimal(1);;
					}
					if(aQuantity==null){
						aQuantity=BigDecimal.ZERO;
					}
					aTotalSub=aTotalSub.add(JobUtil.floorFigureoverall(((aUnitCost.multiply((aPriceMult.compareTo(new BigDecimal("0.0000"))==0?aPriceMult=new BigDecimal("1.0000"):aPriceMult))).multiply(aQuantity)),2));
					/*if (aUnitCost != null && aPriceMult != null
							&& aQuantity != null) {
						if(aPriceMult.compareTo(new BigDecimal("0.0000"))==0){
							aPriceMult=new BigDecimal("1.0000");
						}
						aTotalSub = aUnitCost.multiply(aPriceMult);
						aTotalSub = JobUtil.floorFigureoverall(aTotalSub.multiply(aQuantity),2);
					} else if (aUnitCost != null && aQuantity != null) {
						aTotalSub =JobUtil.floorFigureoverall( aUnitCost.multiply(aQuantity),2);
					} else if (aUnitCost != null && aPriceMult != null) {
						aTotalSub = JobUtil.floorFigureoverall(aUnitCost.multiply(aQuantity),2);
					} else if (aUnitCost != null) {
						aTotalSub = JobUtil.floorFigureoverall(aVePODetail.get(index).getUnitCost(),2);
					}*/
					
				}
				aTotalInt = aTotalSub.toString();
				aTotalSubInt = Double.valueOf(aTotalInt);
				aSubTotal = format.format(aTotalSubInt);
			}
			if (aPO.getFreight() != null) {
				aFreightCost = format.format(aPO.getFreight());
			}
			if (aPO.getTaxTotal() != null) {
				aTaxRate = format.format(aPO.getTaxTotal());
			}
			if (aPO.getTaxTotal() != null) {
				Double asub = aTotalSubInt;
				String aFright = "";
				String aTax = "";
				Double aFrightChanges = new Double(0);
				Double aTaxChanges = new Double(0);
				BigDecimal afreight = aPO.getFreight();
				BigDecimal atax = aPO.getTaxTotal();
				if (afreight != null) {
					aFright = afreight.toString();
					aFrightChanges = Double.valueOf(aFright);
				}
				if (atax != null) {
					aTax = atax.toString();
					aTaxChanges = Double.valueOf(aTax);
				}
				aPOTotalInt = asub + aFrightChanges + aTaxChanges;
				aPOTotal = format.format(aPOTotalInt);
			}
			
			params.put("SUB_TOTAL", aSubTotal);
			params.put("FREIGHT", aFreightCost);
			params.put("SALES_TAX", aTaxRate);
			params.put("PO_TOTAL", aPOTotal);
			params.put("VEPOID", theVePoId);
			
			String NoticeToParameter="";
			
			if(aPO.getJoReleaseId()!=null){
			Jomaster thejomaster=itsJobService.getJoMasterDetails(aPO.getJoReleaseId());
			String noticeID="";
			if(thejomaster.getNoticeId()!=null &&  thejomaster.getNoticeId()!=-1){
				if(thejomaster.getNoticeId()==0){
					noticeID="Notice to ";
				}else if(thejomaster.getNoticeId()==1){
					noticeID="24 Hrs Notice to ";
				}else if(thejomaster.getNoticeId()==2){
					noticeID="48 Hrs Notice to ";
				}
			}
			String contactname="";
			if(thejomaster.getOtherContact()!=null){
			if(thejomaster.getOtherContact()==0){
			if(thejomaster.getContactId()!=null&&thejomaster.getContactId()!=-1){
				Rxcontact thecontact=itsJobService.ContactsBasedonID(thejomaster.getContactId());
				contactname=thecontact.getFirstName()+" "+thecontact.getLastName()+" ";
			}
			}else{
				if(thejomaster.getContactName()!=null)
				contactname = thejomaster.getContactName()+" ";
			}
			}
			String notice="";
			if(thejomaster.getNotice()!=null){
				notice=thejomaster.getNotice();
			}
			NoticeToParameter=noticeID+contactname+notice;
			}
			params.put("NoticeToParameter", NoticeToParameter);
			params.put("releaseType", booleanvalue);
			 connection = con.getConnection();
			if(WriteorView!=null&&WriteorView.equals("write")){
				ReportService.WriteReportCall(theRequest,theResponse,params,"pdf",path_JRXML,filename,connection);
			}else{
				ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
			}
			
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>theVePoId:</b>"+theVePoId,"PurchasePDFController",e,session,theRequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	//Due To Alignment issue .Itext is converted in to jrxml on 12/10/2014
	/*@RequestMapping(value="/viewPDFLineItemForm", method = RequestMethod.GET)
	public @ResponseBody String viewPDFLineItemForm(@RequestParam(value="vePOID", required= false) Integer theVePoId, 
														@RequestParam(value="puchaseOrder", required= false) String thePuchaseOrder,
														@RequestParam(value="jobNumber", required= false) String theJobNumber, 
														@RequestParam(value="rxMasterID", required= false) Integer theRxMasterID,
														@RequestParam(value="manufacturerID", required= false) Integer theManufacturerID, 
														@RequestParam(value="shipToAddrID", required= false) Integer theShipToAddrID,
														HttpSession session, HttpServletResponse aResponse) throws IOException, DocumentException{
		*//** A4 page  With Document **//*
		if(null == theRxMasterID)
		{
			try {
				Integer theRxMasterId = itsPurchaseService.getRxMasterID(theVePoId);
				theRxMasterID = theRxMasterId;
			} catch (VendorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			TsUserSetting aUserLoginSetting = null;
			PDFGenerator aGenerator = new PDFGenerator();
			Integer noOfLinesInHeader = 0;
			String header = "";
			try{
				*//** Set PDF application using response **//*
				aResponse.setContentType("application/pdf");
				PdfWriter aPdfWriter = null;
				For Windows Configuration
//				File aDir1 = new File ("D://var/quotePDF/");
				
				For Linux Configuration
				File aDir1 = new File ("/var/quotePDF/");
				
				
				if(thePuchaseOrder.equalsIgnoreCase("purchase")){
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, new FileOutputStream(aDir1.getPath()+"/PuchaseOrder.pdf"));
				}else{
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				}
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				aBoldTitleFont.setStyle(Font.UNDERLINE);
				Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
				
				*//** Set page Number **//*
				HeaderFooter aFooter = null;
				aFooter= new HeaderFooter(new Phrase("Page: "), true);
				aFooter.setBorder(Rectangle.NO_BORDER);
				aFooter.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.setHeader(aFooter);
				aPDFDocument.addTitle("");
				
				*//** Open PDF Document **//*
				aPDFDocument.open();
				
				Vepo aPO = null;
				Veshipvia aVeshipvia = null;
				TsUserLogin aLogin = null;
				List<Vepodetail> aVePODetail = null;
				List<Prwarehouse> aPrwarehouses = null;
				System.out.println("vepoid::"+theVePoId);
				if(theVePoId != null){
					aPO = itsVendorService.getVePo(theVePoId);
					if(aPO.getOrderedById() != null){
						aLogin = userService.getSingleUserDetails(aPO.getOrderedById());
					}
					aVePODetail = itsJobService.getPOReleaseLineItem(theVePoId);
				}
				
				*//** HEader with Rich Text Editor Values **//*
				Paragraph aParagraphHeader = null;
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					aParagraphHeader = new Paragraph();
					aParagraphHeader.setSpacingBefore(6);
//					aParagraphHeader.setSpacingAfter(28);
					String aHtmlString =  aUserLoginSetting.getHeaderText();
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String aText3 = aText2+"\n\n";
					noOfLinesInHeader = 1+StringUtils.countOccurrencesOf(aText3, "<br>"); 
					ArrayList poHeaderItems = new ArrayList();
					StringReader aStrReader = new StringReader(aText3);
					poHeaderItems = HTMLWorker.parseToList(aStrReader, null);
					int aLinesCount = 0;
					for (int k = 0; k < poHeaderItems.size(); ++k){
						aParagraphHeader.add((com.lowagie.text.Element)poHeaderItems.get(k));
						aLinesCount++;
					}
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					header=aParagraphHeader.toString();
					aPDFDocument.add(aParagraphHeader);
				}else{
					String aHeader = "\n";
					String aHeaderText1 = "\n";
					String aHeaderText2 = "\n";
					String aHeaderText3 = "\n\n\n\n";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText3, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}
				
				Rxaddress aRxAddress = null;
				if(aPO.getRxVendorId() != null){
					aRxAddress = itsJobService.getRxAddress(aPO.getRxVendorId());
				}
				Rxmaster aRxmasterName = null;
				if(aPO.getRxVendorId() != null){
					aRxmasterName = itsJobService.getSingleRxMasterDetails(aPO.getRxVendorId());
				}
				if(aPO.getVeShipViaId() != null){
					aVeshipvia = itsVendorService.getVeShipVia(aPO.getVeShipViaId());
				}
				Rxcontact aRxcontact = null;
				if(aPO.getRxVendorContactId() != null){
					aRxcontact = itsVendorService.getContactDetails(aPO.getRxVendorContactId());
				}
				Rxmaster aRxmasterAddress = null;
				if(theManufacturerID != null){
					aRxmasterAddress = itsJobService.getSingleRxMasterDetails(theManufacturerID);
				}
				VeFactory aVendorDetail = null;
				if(theManufacturerID != null){
					aVendorDetail = itsVendorService.getSingleVeFactoryDetails(theManufacturerID);
				}
				Rxmaster aRxmaster = null;
				if(theRxMasterID != null){
					aRxmaster = itsJobService.getSingleRxMasterDetails(theRxMasterID);
				}
				Rxaddress aRxAddressBillTo = null;
				String billToName = null;
				Rxmaster billToRxmaster;
				if(aPO.getRxBillToId() != null){
					aRxAddressBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToId(), "bill");
					billToRxmaster = itsJobService.getSingleRxMasterDetails(aPO.getRxBillToId());
					aRxAddressBillTo.setName(billToRxmaster.getName());
				}
				Rxaddress aRxAddressShipTo = null;
				if(aPO.getRxShipToId() != null){
					aRxAddressShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToId(), "ship");
				}
				Rxaddress aRxAddressOtherBillTo = null;
				if(aPO.getRxBillToAddressId() != null){
					aRxAddressOtherBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToAddressId(), "billTo");
				}
				Rxaddress aRxAddressOtherShipTo = null;
				if(aPO.getRxShipToAddressId() != null){
					aRxAddressOtherShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToAddressId(), "shipTo");
				}
				Jomaster aJomaster = null;
				if(theJobNumber != "" && theJobNumber != null){
					aJomaster = itsJobService.getSingleJobDetails(theJobNumber);
				}
				aPrwarehouses = itsJobService.getWareHouse();
				
				*//** set company logo in PDF header **//*
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					aLeftImage.scaleAbsolute(97,84);
					aLeftImage.setAbsolutePosition(35f, 715f);
					aPDFDocument.add(aLeftImage);
				}else{
					Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					aLeftImage.scaleAbsolute(97,84);
					aLeftImage.setAbsolutePosition(35f, 700f);
					aPDFDocument.add(aLeftImage);
				}
				//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				
				Blob blob =  aUserLoginSetting.getCompanyLogo();
				byte[] image = blob.getBytes(1, (int) blob.length());
				Image aRightImage = Image.getInstance(image);				
				aRightImage.scaleAbsolute(97,84);
				aRightImage.setAbsolutePosition(30f, 718f);//aRightImage.setAbsolutePosition(450f, 705f);				
				aPDFDocument.add(aRightImage);
				PdfContentByte cb = aPdfWriter.getDirectContent();
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					cb.roundRectangle(420f, 735f, 150f, 59f, 10f);
				}else{
					cb.roundRectangle(420f, 710f, 150f, 59f, 10f);
				}
			    cb.stroke();
			    PdfPTable aPdfTable = new PdfPTable(1);
			    aPdfTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
				PdfPCell pdfShipToCell = new PdfPCell();
				pdfShipToCell.setBorder(Rectangle.NO_BORDER);
				pdfShipToCell.setPaddingLeft(300);
				String[] headerLinesArray = header.replaceAll("\n", "__").split("__");
				LinkedList<String> aList = new LinkedList<String>(Arrays.asList(headerLinesArray)); // headerLinesArray;
				int index;
				for (index = 0; index < aList.size(); index++) {
					if(aList.get(index).trim().equals(",")){
						aList.remove(index);
					}
				}
				noOfLinesInHeader = aList.size()-1;
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					if(noOfLinesInHeader == 2)
						pdfShipToCell.setPaddingTop(-35);
					else if(noOfLinesInHeader == 3)
						pdfShipToCell.setPaddingTop(-50);
					else if(noOfLinesInHeader == 4){
						pdfShipToCell.setPaddingTop(-85);
//						aPDFDocument.add( Chunk.NEWLINE );
					}else if(noOfLinesInHeader == 5){
						pdfShipToCell.setPaddingTop(-85);
//						aPDFDocument.add( Chunk.NEWLINE );
//						aPDFDocument.add( Chunk.NEWLINE );
					}else if(noOfLinesInHeader == 1){
						pdfShipToCell.setPaddingTop(-20);
					} else {
						pdfShipToCell.setPaddingTop(-115);
					}
					pdfShipToCell.setPaddingBottom(50);
				}else{
					pdfShipToCell.setPaddingTop(-95);
					pdfShipToCell.setPaddingBottom(0);
				}
				Phrase aShipTo = new Phrase("Purchase Order \n", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
				aShipTo.add(new Phrase("	No. "+aPO.getPonumber().replaceAll("null", ""), FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD, new Color(0, 0, 0))));
				pdfShipToCell.addElement(aShipTo);
				aPdfTable.addCell(pdfShipToCell);
				aPDFDocument.add(aPdfTable);
			    
			    Paragraph aParagraphPONum = new Paragraph();
				aParagraphPONum = new Paragraph("PURCHASE ORDER: \n"+aPO.getPonumber().replace("null", ""), aBoldFont);
				aParagraphPONum.setIndentationLeft(0);
				aParagraphPONum.setIndentationRight(30);
				aParagraphPONum.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphPONum); 
				
				aGenerator.getProjectInformation(aPDFDocument, aUserLoginSetting, aBoldFont, aNormalFont, aPdfWriter, aRxAddress, aPO, aUserBean, aVeshipvia, aRxcontact, aRxmaster, aRxAddressBillTo, aPrwarehouses, aJomaster, 
						aRxAddressShipTo, aRxmasterAddress, aVendorDetail, theShipToAddrID, aRxAddressOtherShipTo, aRxAddressOtherBillTo, aLogin, aRxmasterName,noOfLinesInHeader);
				
				aGenerator.getQuotesLineItems(aPDFDocument, aBoldFont, aNormalFont, aBoldTitleFont, theVePoId, aVePODetail);
				
				aGenerator.diplayFooter(aPDFDocument,aBoldFont,aNormalFont,aBoldTitleFont,aResponse, aPO, aPdfWriter, aVePODetail);
				
				aPDFDocument.close();
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		return null;
	}*/
	
	@RequestMapping(value="/viewPDFLineItemFormVoid", method = RequestMethod.GET)
	public @ResponseBody String viewPDFLineItemFormVoid(@RequestParam(value="vePOID", required= false) Integer theVePoId, 
														@RequestParam(value="puchaseOrder", required= false) String thePuchaseOrder,
														@RequestParam(value="jobNumber", required= false) String theJobNumber, 
														@RequestParam(value="rxMasterID", required= false) Integer theRxMasterID,
														@RequestParam(value="manufacturerID", required= false) Integer theManufacturerID, 
														@RequestParam(value="shipToAddrID", required= false) Integer theShipToAddrID,
														@RequestParam(value="joMasterID", required= false) Integer joMasterID,
														HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException,MessagingException{
		/** A4 page  With Document **/
		if(null == theRxMasterID)
		{
			try {
				Integer theRxMasterId = itsPurchaseService.getRxMasterID(theVePoId);
				theRxMasterID = theRxMasterId;
			} catch (VendorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
			Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			TsUserSetting aUserLoginSetting = null;
			PDFGenerator aGenerator = new PDFGenerator();
			Integer noOfLinesInHeader = 0;
			String header = "";
			FileOutputStream fos=null;
			try{
				/** Set PDF application using response **/
				aResponse.setContentType("application/pdf");
				PdfWriter aPdfWriter = null;
				File aDir1 = new File ("/var/quotePDF/");
				if(thePuchaseOrder.equalsIgnoreCase("purchase")){
					fos = new FileOutputStream(aDir1.getPath()+"/PuchaseOrder.pdf");
					aPdfWriter = PdfWriter.getInstance(aPDFDocument,fos);
				}else{
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				}
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				aBoldTitleFont.setStyle(Font.UNDERLINE);
				Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
				
				/** Set page Number **/
				HeaderFooter aFooter = null;
				aFooter= new HeaderFooter(new Phrase("Page: "), true);
				aFooter.setBorder(Rectangle.NO_BORDER);
				aFooter.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.setHeader(aFooter);
				aPDFDocument.addTitle("");
				
				/** Open PDF Document **/
				aPDFDocument.open();
				
				Vepo aPO = null;
				Veshipvia aVeshipvia = null;
				TsUserLogin aLogin = null;
				List<Vepodetail> aVePODetail = null;
				List<Prwarehouse> aPrwarehouses = null;
				System.out.println("vepoid::"+theVePoId);
				if(theVePoId != null){
					aPO = itsVendorService.getVePo(theVePoId);
					if(aPO.getOrderedById() != null){
						aLogin = userService.getSingleUserDetails(aPO.getOrderedById());
					}
					aVePODetail = itsJobService.getPOReleaseLineItem(theVePoId);
				}
				
				/** HEader with Rich Text Editor Values **/
				Paragraph aParagraphHeader = null;
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					aParagraphHeader = new Paragraph();
					aParagraphHeader.setSpacingBefore(6);
//					aParagraphHeader.setSpacingAfter(28);
					String aHtmlString =  aUserLoginSetting.getHeaderText();
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String aText3 = aText2+"\n\n";
					noOfLinesInHeader = 1+StringUtils.countOccurrencesOf(aText3, "<br>"); 
					ArrayList poHeaderItems = new ArrayList();
					StringReader aStrReader = new StringReader(aText3);
					poHeaderItems = HTMLWorker.parseToList(aStrReader, null);
					int aLinesCount = 0;
					for (int k = 0; k < poHeaderItems.size(); ++k){
						aParagraphHeader.add((com.lowagie.text.Element)poHeaderItems.get(k));
						aLinesCount++;
					}
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					header=aParagraphHeader.toString();
					aPDFDocument.add(aParagraphHeader);
				}else{
					String aHeader = "\n";
					String aHeaderText1 = "\n";
					String aHeaderText2 = "\n";
					String aHeaderText3 = "\n\n\n\n";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText3, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}
				
				Rxaddress aRxAddress = null;
				if(aPO.getRxVendorId() != null){
					aRxAddress = itsJobService.getRxAddress(aPO.getRxVendorId());
				}
				Rxmaster aRxmasterName = null;
				if(aPO.getRxVendorId() != null){
					aRxmasterName = itsJobService.getSingleRxMasterDetails(aPO.getRxVendorId());
				}
				if(aPO.getVeShipViaId() != null){
					aVeshipvia = itsVendorService.getVeShipVia(aPO.getVeShipViaId());
				}
				Rxcontact aRxcontact = null;
				if(aPO.getRxVendorContactId() != null){
					aRxcontact = itsVendorService.getContactDetails(aPO.getRxVendorContactId());
				}
				Rxmaster aRxmasterAddress = null;
				if(theManufacturerID != null){
					aRxmasterAddress = itsJobService.getSingleRxMasterDetails(theManufacturerID);
				}
				VeFactory aVendorDetail = null;
				if(theManufacturerID != null){
					aVendorDetail = itsVendorService.getSingleVeFactoryDetails(theManufacturerID);
				}
				Rxmaster aRxmaster = null;
				if(theRxMasterID != null){
					aRxmaster = itsJobService.getSingleRxMasterDetails(theRxMasterID);
				}
				Rxaddress aRxAddressBillTo = null;
				String billToName = null;
				Rxmaster billToRxmaster;
				if(aPO.getRxBillToId() != null){
					aRxAddressBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToId(), "bill");
					billToRxmaster = itsJobService.getSingleRxMasterDetails(aPO.getRxBillToId());
					aRxAddressBillTo.setName(billToRxmaster.getName());
				}
				Rxaddress aRxAddressShipTo = null;
				if(aPO.getRxShipToId() != null){
					aRxAddressShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToId(), "ship");
				}
				Rxaddress aRxAddressOtherBillTo = null;
				if(aPO.getRxBillToAddressId() != null){
					aRxAddressOtherBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToAddressId(), "billTo");
				}
				Rxaddress aRxAddressOtherShipTo = null;
				if(aPO.getRxShipToAddressId() != null){
					aRxAddressOtherShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToAddressId(), "shipTo");
				}
				Jomaster aJomaster = null;
				if(theJobNumber != "" && theJobNumber != null){
					aJomaster = itsJobService.getSingleJobDetails(theJobNumber,joMasterID);
				}
				aPrwarehouses = itsJobService.getWareHouse();
				
				/** set company logo in PDF header **/
				/*if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					aLeftImage.scaleAbsolute(97,84);
					aLeftImage.setAbsolutePosition(35f, 715f);
					aPDFDocument.add(aLeftImage);
				}else{
					Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
					aLeftImage.scaleAbsolute(97,84);
					aLeftImage.setAbsolutePosition(35f, 700f);
					aPDFDocument.add(aLeftImage);
				}*/
				//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				
				Blob blob =  aUserLoginSetting.getCompanyLogo();
				byte[] image = blob.getBytes(1, (int) blob.length());
				Image aRightImage = Image.getInstance(image);				
				aRightImage.scaleAbsolute(97,84);
				aRightImage.setAbsolutePosition(30f, 718f);//aRightImage.setAbsolutePosition(450f, 705f);				
				aPDFDocument.add(aRightImage);
				PdfContentByte cb = aPdfWriter.getDirectContent();
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					cb.roundRectangle(420f, 735f, 150f, 59f, 10f);
				}else{
					cb.roundRectangle(420f, 710f, 150f, 59f, 10f);
				}
			    cb.stroke();
			    PdfPTable aPdfTable = new PdfPTable(1);
			    aPdfTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
				PdfPCell pdfShipToCell = new PdfPCell();
				pdfShipToCell.setBorder(Rectangle.NO_BORDER);
				pdfShipToCell.setPaddingLeft(300);
				String[] headerLinesArray = header.replaceAll("\n", "__").split("__");
				LinkedList<String> aList = new LinkedList<String>(Arrays.asList(headerLinesArray)); // headerLinesArray;
				int index;
				for (index = 0; index < aList.size(); index++) {
					if(aList.get(index).trim().equals(",")){
						aList.remove(index);
					}
				}
				noOfLinesInHeader = aList.size()-1;
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					if(noOfLinesInHeader == 2)
						pdfShipToCell.setPaddingTop(-35);
					else if(noOfLinesInHeader == 3)
						pdfShipToCell.setPaddingTop(-50);
					else if(noOfLinesInHeader == 4){
						pdfShipToCell.setPaddingTop(-85);
//						aPDFDocument.add( Chunk.NEWLINE );
					}else if(noOfLinesInHeader == 5){
						pdfShipToCell.setPaddingTop(-85);
//						aPDFDocument.add( Chunk.NEWLINE );
//						aPDFDocument.add( Chunk.NEWLINE );
					}else if(noOfLinesInHeader == 1){
						pdfShipToCell.setPaddingTop(-20);
					} else {
						pdfShipToCell.setPaddingTop(-115);
					}
					pdfShipToCell.setPaddingBottom(50);
				}else{
					pdfShipToCell.setPaddingTop(-95);
					pdfShipToCell.setPaddingBottom(0);
				}
				Phrase aShipTo = new Phrase("Purchase Order \n", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
				aShipTo.add(new Phrase("	No. "+aPO.getPonumber().replaceAll("null", "")+", Void", FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD, new Color(0, 0, 0))));
				pdfShipToCell.addElement(aShipTo);
				aPdfTable.addCell(pdfShipToCell);
				aPDFDocument.add(aPdfTable);
			     
			    /*Paragraph aParagraphPONum = new Paragraph();
				aParagraphPONum = new Paragraph("PURCHASE ORDER: \n"+aPO.getPonumber().replace("null", ""), aBoldFont);
				aParagraphPONum.setIndentationLeft(0);
				aParagraphPONum.setIndentationRight(30);
				aParagraphPONum.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.add(aParagraphPONum);*/ 
				
				aGenerator.getProjectInformation(aPDFDocument, aUserLoginSetting, aBoldFont, aNormalFont, aPdfWriter, aRxAddress, aPO, aUserBean, aVeshipvia, aRxcontact, aRxmaster, aRxAddressBillTo, aPrwarehouses, aJomaster, 
						aRxAddressShipTo, aRxmasterAddress, aVendorDetail, theShipToAddrID, aRxAddressOtherShipTo, aRxAddressOtherBillTo, aLogin, aRxmasterName,noOfLinesInHeader);
				
				aGenerator.getQuotesLineItems(aPDFDocument, aBoldFont, aNormalFont, aBoldTitleFont, theVePoId, aVePODetail);
				
				aGenerator.diplayFooter(aPDFDocument,aBoldFont,aNormalFont,aBoldTitleFont,aResponse, aPO, aPdfWriter, aVePODetail);
				
				aPDFDocument.close();
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>theVePoId:</b>"+theVePoId,"PurchasePDFController",e,session,therequest);
			}
			finally
			{
				if(fos!=null)
				fos.close();
			}
		return null;
	}
	
	@RequestMapping(value="/viewPDFAckForm", method = RequestMethod.GET)
	public @ResponseBody String viewPDFAckForm(@RequestParam(value="vePOID", required= false) Integer theVePoId, @RequestParam(value="puchaseOrder", required= false) String thePuchaseOrder,
																							@RequestParam(value="jobNumber", required= false) String theJobNumber, @RequestParam(value="rxMasterID", required= false) Integer theRxMasterID,
																							@RequestParam(value="joMasterID", required= false) Integer joMasterID,
																							@RequestParam(value="manufacturerID", required= false) Integer theManufacturerID, @RequestParam(value="shipToAddrID", required= false) Integer theShipToAddrID,
																							HttpSession session, HttpServletResponse aResponse,HttpServletRequest therequest) throws IOException, DocumentException, MessagingException{
		/** A4 page  With Document **/
			Document aPDFDocument = new Document(PageSize.A4, 30, 30, 30, 30);
			ByteArrayOutputStream aByteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(aPDFDocument, aByteArrayOutputStream);
			TsUserSetting aUserLoginSetting = null;
			PDFGenerator aGenerator = new PDFGenerator();
			Integer noOfLinesInHeader = 0;
			String header = "";
			float[] aWidths = {2.5f,7.5f,4.0f};
			PdfPTable aPdfPTable = new PdfPTable(aWidths); 
			aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			FileOutputStream fos = null;
			try{
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				/** Set PDF application using response **/
				aResponse.setContentType("application/pdf");
				PdfWriter aPdfWriter = null;
				File aDir1 = new File ("/var/quotePDF/");
				if(thePuchaseOrder.equalsIgnoreCase("purchaseAck")){
					fos = new FileOutputStream(aDir1.getPath()+"/PuchaseAcknowledgmentOrder.pdf");
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, fos);
				}else{
					aPdfWriter = PdfWriter.getInstance(aPDFDocument, aResponse.getOutputStream());
				}
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				Font aBoldTitleFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				aBoldTitleFont.setStyle(Font.UNDERLINE);
				Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
				aNormalTitleFont.setStyle(Font.UNDERLINE);
				
				Font aBoldFont = new Font(FontFactory.getFont(FontFactory.COURIER, 11, Font.BOLD));
				Font aNormalFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
				
				/** Set page Number **/
				HeaderFooter aFooter = null;
				aFooter= new HeaderFooter(new Phrase("Page: "), true);
				aFooter.setBorder(Rectangle.NO_BORDER);
				aFooter.setAlignment(Element.ALIGN_RIGHT);
				aPDFDocument.setHeader(aFooter);
				aPDFDocument.addTitle("");
				
				/** Open PDF Document **/
				aPDFDocument.open();
				
				Blob aBlob = aUserLoginSetting.getCompanyLogo();
				byte[] image = aBlob.getBytes(1, (int) aBlob.length());
				//Image aLeftImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				Image aLeftImage = Image.getInstance(image);
				aLeftImage.scaleAbsolute(110,80);
				aLeftImage.setAbsolutePosition(25f, 725f);
				//aPDFDocument.add(aLeftImage);
				//Image aRightImage = Image.getInstance(this.getClass().getClassLoader().getResource("../.././resources/Icons/Quote Form.png"));
				//PdfPTable aTable = new PdfPTable(2); 
				//aTable.setWidthPercentage(100); 
				aPdfPTable.addCell(aLeftImage);
				
				
				
				
				
				
				
				
				
				
				
				
				Vepo aPO = null;
				Veshipvia aVeshipvia = null;
				TsUserLogin aLogin = null;
				List<Vepodetail> aVePODetail = null;
				List<Prwarehouse> aPrwarehouses = null;
				if(theVePoId != null){
					aPO = itsVendorService.getVePo(theVePoId);
					if(aPO.getOrderedById() != null){
						aLogin = userService.getSingleUserDetails(aPO.getOrderedById());
					}
					aVePODetail = itsJobService.getPOReleaseAck(theVePoId);
				}
				
				/** HEader with Rich Text Editor Values **/
				Paragraph aParagraphHeader = null;
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					aParagraphHeader = new Paragraph();
					aParagraphHeader.setSpacingBefore(6);
					String aHtmlString =  aUserLoginSetting.getHeaderText();
					String aText1  = aHtmlString.replaceAll("`and`amp;", "&");
					String aText2 = aText1.replaceAll("`and`nbsp;", " ");
					String aText3 = aText2+"\n\n";
					noOfLinesInHeader = 1+StringUtils.countOccurrencesOf(aText3, "<br>"); 
					ArrayList p=new ArrayList();
					StringReader aStrReader = new StringReader(aText3);
					p = HTMLWorker.parseToList(aStrReader, null);
					for (int k = 0; k < p.size(); ++k){
						aParagraphHeader.add((com.lowagie.text.Element)p.get(k));
					}
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					header = aParagraphHeader.toString();
					PdfPCell cell = new PdfPCell(aParagraphHeader);
					cell.setBorder(Rectangle.NO_BORDER);
					aPdfPTable.addCell(cell);
					//aPDFDocument.add(aParagraphHeader);
					String[] h = header.split("\n");
					noOfLinesInHeader = h.length;
				}else{
					String aHeader = "\n";
					String aHeaderText1 = "\n";
					String aHeaderText2 = "\n";
					String aHeaderText3 = "\n\n\n\n";
					aParagraphHeader = new Paragraph(aHeader, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText1, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText2, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
					aParagraphHeader = new Paragraph(aHeaderText3, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
					aParagraphHeader.setIndentationLeft(30);
					aParagraphHeader.setIndentationRight(90);
					aParagraphHeader.setAlignment(Element.ALIGN_CENTER);
					aPDFDocument.add(aParagraphHeader);
				}
					
				
				Rxaddress aRxAddress = null;
				if(aPO.getRxVendorId() != null){
					aRxAddress = itsJobService.getRxAddress(aPO.getRxVendorId());
				}
				Rxmaster aRxmasterName = null;
				if(aPO.getRxVendorId() != null){
					aRxmasterName = itsJobService.getSingleRxMasterDetails(aPO.getRxVendorId());
				}
				if(aPO.getVeShipViaId() != null){
					aVeshipvia = itsVendorService.getVeShipVia(aPO.getVeShipViaId());
				}
				Rxcontact aRxcontact = null;
				if(aPO.getRxVendorContactId() != null){
					aRxcontact = itsVendorService.getContactDetails(aPO.getRxVendorContactId());
				}
				Rxmaster aRxmasterAddress = null;
				if(theManufacturerID != null){
					aRxmasterAddress = itsJobService.getSingleRxMasterDetails(theManufacturerID);
				}
				VeFactory aVendorDetail = null;
				if(theManufacturerID != null){
					aVendorDetail = itsVendorService.getSingleVeFactoryDetails(theManufacturerID);
				}
				Rxmaster aRxmaster = null;
				if(theRxMasterID != null){
					aRxmaster = itsJobService.getSingleRxMasterDetails(theRxMasterID);
				}
				Rxaddress aRxAddressBillTo = null;
				if(aPO.getRxBillToId() != null){
					logger.info("Bill Calling");
					aRxAddressBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToId(), "bill");
				}
				Rxaddress aRxAddressShipTo = null;
				if(aPO.getRxBillToId() != null){
					logger.info("Ship Calling");
					aRxAddressShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToId(), "ship");
				}
				Rxaddress aRxAddressOtherBillTo = null;
				if(aPO.getRxBillToAddressId() != null){
					logger.info("BillTo Calling");
					aRxAddressOtherBillTo = itsJobService.getRxMasterBillAddress(aPO.getRxBillToAddressId(), "billTo");
				}
				Rxaddress aRxAddressOtherShipTo = null;
				if(aPO.getRxShipToAddressId() != null){
					logger.info("ShipTo Calling");
					aRxAddressOtherShipTo = itsJobService.getRxMasterBillAddress(aPO.getRxShipToAddressId(), "shipToOther");
				}
				Jomaster aJomaster = null;
				if(theJobNumber != "" && theJobNumber != null){
					aJomaster = itsJobService.getSingleJobDetails(theJobNumber,joMasterID);
				}
				aPrwarehouses = itsJobService.getWareHouse();
				
				PdfContentByte cb = aPdfWriter.getDirectContent();
				if(aUserLoginSetting.getHeaderPurchaseOrders() == 1){
					cb.roundRectangle(420f, 753f, 147f, 43f, 10f);
				}else{
					cb.roundRectangle(420f, 735f, 147f, 43f, 10f);
				}
			    cb.stroke();
			   // PdfPTable aPdfTable = new PdfPTable(1);
			   // aPdfTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
				PdfPCell pdfShipToCell = new PdfPCell();
				pdfShipToCell.setBorder(Rectangle.NO_BORDER);
				Phrase aShipTo = new Phrase("    Order Acknowledgement \n", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0)));
				aShipTo.add(new Phrase("    No. "+aPO.getPonumber().replaceAll("null", ""), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new Color(0, 0, 0))));
				pdfShipToCell.addElement(aShipTo);
				aPdfPTable.addCell(pdfShipToCell);
				aPDFDocument.add(aPdfPTable);
				
				
				
				
				
				
				
				
				
				
				
				aGenerator.getProjectInformation(aPDFDocument, aUserLoginSetting, aBoldFont, aNormalFont, aPdfWriter, aRxAddress, aPO, aUserBean, aVeshipvia, aRxcontact, aRxmaster, aRxAddressBillTo, aPrwarehouses, aJomaster, 
						aRxAddressShipTo, aRxmasterAddress, aVendorDetail, theShipToAddrID, aRxAddressOtherShipTo, aRxAddressOtherBillTo, aLogin, aRxmasterName,noOfLinesInHeader);
				
				aGenerator.getQuotesAck(aPDFDocument, aBoldFont, aNormalFont, aBoldTitleFont, theVePoId, aVePODetail);
				
				aGenerator.diplayAckFooter(aPDFDocument,aBoldFont,aNormalFont,aBoldTitleFont,aResponse, aPO, aPdfWriter);
				
				aPDFDocument.close();
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTransactionException("<b>theVePoId:</b>"+theVePoId,"PurchasePDFController",e,session,therequest);
			}
			finally 
			{
				fos.close();
			}
		return null;
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