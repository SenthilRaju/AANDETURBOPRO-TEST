package com.turborep.turbotracker.job.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker; // deprecated
import com.itextpdf.text.pdf.PdfWriter;

	public class QuotePDfController3 {
	  // itextpdf-5.4.1.jar  http://sourceforge.net/projects/itext/files/iText/
	  public static void main(String ... args ) throws IOException {
		  FileOutputStream fis=null;
	    try {
	     fis=new FileOutputStream("D:\\uploads\\testpdf1.pdf");
	      Document document = new Document(PageSize.LETTER);
	      PdfWriter.getInstance(document, fis);
	      document.open();
	      document.addAuthor("Real Gagnon");
	      document.addCreator("Real's HowTo");
	      document.addSubject("Thanks for your support");
	      document.addCreationDate();
	      document.addTitle("Please read this");

	      HTMLWorker htmlWorker = new HTMLWorker(document);
	      String str = "<div><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Carrier chiller model 23XRV</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>Sized for 300 tons</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Dual-rotor screw compressor</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>Stacked configuration</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Unit mounted VFD (refrigerant cooled)</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Freestanding line reactor</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>460 volt/3 phase/60 hertz power</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Chiller control panel</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>BACnet interface control package</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>R-134a refrigerant – shipped charged in machine</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>Refrigerant isolation valve package</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Full factory acoustic insulation</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>Soleplate package</span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>Grooved connections </span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>3-way head pressure control valve, <em>shipped loose for mounting and wiring by others</em></span></p><p style='margin-left: 126.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>o<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp; </span>Chiller shipped completely assembled</span></p><p style='margin-left: 90.0pt; text-align: justify; text-indent: -18.0pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'>-<span style='font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; line-height: normal;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Carrier commercial service startup</span></p></div>";
	      
	      
	      List<Element> objects=htmlWorker.parseToList(new StringReader(str),null);
	      for(Element ele:objects){
	    	  document.add(ele);
	      }
	      document.close();
	      System.out.println("Done");
	      }
	    catch (Exception e) {
	      e.printStackTrace();
	    }finally{
	    	fis.close();
	    }
	}
}
