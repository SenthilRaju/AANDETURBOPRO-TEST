package com.turborep.turbotracker.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.log4j.Logger;

import javax.print.*;

import com.turborep.turbotracker.user.controller.UserListController;

public class ReportUtils {
	
	protected static Logger itsLogger = Logger.getLogger(UserListController.class);
	  @SuppressWarnings("unused")
	public static String toHtml(String rtf) {
		  if(!rtf.isEmpty() || rtf != null || rtf != "")
		  {
			  ByteArrayInputStream input= new ByteArrayInputStream(rtf.getBytes());
	          StringWriter writer = new StringWriter();
	          try {
	                  RTFEditorKit rtfEditorKit = new RTFEditorKit();
	                  HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
	                  Document doc = rtfEditorKit.createDefaultDocument();
	                  rtfEditorKit.read(input, doc, 0);
	                  htmlEditorKit.write(writer, doc, 0, doc.getLength());
	          } catch (Exception ex) {
	        	  itsLogger.error("Error during converting rtf 2 html", ex);
	          }
	          String html = writer.toString();
	          System.out.println(html);
	          return html;
		  }
		  else return "";
  }


	public static String[] getPrinters() {
		String[] printerNames = null;
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				null, null);
		printerNames = new String[printServices.length];
		for (int i = 0; i < printServices.length; i++) {
			printerNames[i] = printServices[i].getName();
		}
		return printerNames;
	}
	
	public static BigDecimal truncateDecimal(BigDecimal x,int numberofDecimals)
	{
		if(x==null){
			x=new BigDecimal("0.0000");
		}
	    if ( x.compareTo(new BigDecimal(0)) > 0) {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
	    } else {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
	    }
	}
	  
}

