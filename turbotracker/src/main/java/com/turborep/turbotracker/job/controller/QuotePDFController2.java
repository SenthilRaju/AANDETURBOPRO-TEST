package com.turborep.turbotracker.job.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;

import javax.annotation.Resource;

import com.itextpdf.text.Image;
//import com.pdfcrowd.Client;
//import com.pdfcrowd.PdfcrowdError;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;

public class QuotePDFController2 {
	static String RESULT="D:\\uploads\\file.html";
	private UserService userService;
	@Resource(name = "jobService")
	public static void main(String[] args) throws UserException {
		new QuotePDFController2().createPDF();
	}
	public void createPDF(){
//
//        try 
//        {
//            FileOutputStream fileStream;     
// 
//            // create an API client instance
//            Client client = new Client("turbo", "f02bdb6a54402832f0332068ad5abc48");
//            client.setPageMargins("36pt", "36pt", "36pt", "36pt");
//            
//            String image="<img src='http://pdfcrowd.com/static/images/logo_transparent_blue.png' />";
//            String Page="Page: 1";
//            String HeaderText="<div><div><font face='arial' size='1'><i><b>10350 Olympic Drive, Dallas TX 75220-6040 (214) 350-6871</b></i></font></div><div><font face='arial' size='1'><i><b>3206 Longhorn Blvd, Austin TX 78758  (512) 774-5853  </b></i></font></div><div><font face='arial' size='1'><i><b>2901 Wesley Way, Fort Worth, TX 76118-6955  (682) 253-0122            </b></i></font></div><div><font face='arial' size='1'><i><b>8788 Westpark Drive, Houston TX 77063  (713) 487-0065    </b></i></font></div></div><div></div>";
//            String QuotesText="<font face='Helvetica' size='32'><b>QUOTATION</b></font>";
//            String HeaderTable="<table width='100%'>"
//            		+ "<tr width='100%'>"
//            		+ "<td width='50%'>"+image+"</td><td width='50%' align='right'>"+Page+"</td>"
//            		+ "</tr><tr>"
//            		+ "<td width='50%'>"+HeaderText+"</td><td width='50%' align='right'>"+QuotesText+"</td>"
//            		+ "</tr>"
//            		+ "</table>";
//            client.setHeaderFooterPageExcludeList("1");
//            client.setHeaderHtml("<table align='right' style='vertical-align:bottom'><tr align='right'><td>Page: %p</td></tr></table>");
//            fileStream = new FileOutputStream("D:\\uploads\\file4.pdf");
//            String html=HeaderTable+"<div><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Header</span></div><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>T1</span><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>T2</span></li></ul></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>T3</span></li></ul>";
//            client.convertHtml(html, fileStream);
//            fileStream.close();
//
//           
//            Integer ntokens = client.numTokens();
//        }
//        catch(PdfcrowdError why) {
//            System.err.println(why.getMessage());
//        }
//        catch(IOException exc) {
//            // handle the exception
//        }
//    
	}
}
