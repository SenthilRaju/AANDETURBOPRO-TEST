package com.turborep.turbotracker.job.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class Itext {

	static String RESULT="D:\\uploads\\Paragraph.pdf";
	List<Element> elements;
	
	 public static void main(String[] args) throws IOException, DocumentException {
	        File file = new File(RESULT);
	        file.getParentFile().mkdirs();
	        new Itext().createPdf(RESULT);
	    }
	 
	 public void createPdf(String dest) throws IOException, DocumentException { 
		 
		 FileOutputStream fos=null;
		 try {
			CSSResolver cssResolver =
			     XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
 /*StringReader sr=new StringReader("<div><span style='font-family: Times New Roman, serif; font-size: 10pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'></span></span>"
				 +"<p><u><strong>TEST!:</strong></u></p>"
				 +"<p>&nbsp;</p>"
				 +"<ul>"
				 +"<li>"
				 +"<p>Test1</p>"
				 +"</li>"
				 +"<li>"
				 +"<p>Test2</p>"
				 +"</li>"
				 +"<li>"
				 +"<p>Test3</p>"
				 +"</li>"
				 +"<li>"
				 +"<p>Test4</p>"
				 +"</li>"
				 +"</ul>"
				 +"</div>");*/
 
 StringReader sr=new StringReader("<div><p style='text-align:center;'>testing</p><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test1</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test2</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test3</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test4</span></li></ul></div>");

 Map<String, String> pc2 = new HashMap<String, String>();
 StyleSheet styles = new StyleSheet();
 
    // HTML
   /* HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
    htmlContext.autoBookmark(false);*/

    // Pipelines
    /*ElementList elements = new ElementList();
    ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
    HtmlPipeline html = new HtmlPipeline(htmlContext, end);
    CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

    // XML Worker
    XMLWorker worker = new XMLWorker(css, true);
    XMLParser p = new XMLParser(worker);
    p.parse(sr);*/

    // step 1
    Document document = new Document();
    fos=new FileOutputStream(dest);
    PdfWriter.getInstance(document, fos);
    document.open();
//    elements=HTMLWorker.parseToList(sr, styles);
    XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {
			 @Override
			    public void add(final Writable w) {
			        if (w instanceof WritableElement) {
			            elements = ((WritableElement) w).elements();
			        }
			    }

				
			}, sr);
    document.add(new Phrase("test-===="));
    PdfPTable table = new PdfPTable(1);
    PdfPCell cell = new PdfPCell();
    for (Element e : elements) {
			cell.addElement(e);
    	//document.add(e);
    }
    table.addCell(cell);
    document.add(table);

    document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fos.close();
		}
	    }
	   /* public void createPdf(String dest) throws IOException, DocumentException {
	    	FileOutputStream fos=null;
	        try {
	        	

	        	StringReader sr=new StringReader("<div><span style='font-family: Times New Roman, serif; font-size: 10pt;'><span style='font-family: Times New Roman, serif; font-size: 10pt;'></span></span>"
+"<p><u><strong>TEST!:</strong></u></p>"
+"<p>&nbsp;</p>"
+"<ul>"
+"<li>"
+"<p>Test1</p>"
+"</li>"
+"<li>"
+"<p>Test2</p>"
+"</li>"
+"<li>"
+"<p>Test3</p>"
+"</li>"
+"<li>"
+"<p>Test4</p>"
+"</li>"
+"</ul>"
+"</div>");
	        	fos=new FileOutputStream(dest);
				Document document = new Document();
				PdfWriter.getInstance(document, fos);
				document.open();
				Font font = new Font(FontFactory.getFont(FontFactory.HELVETICA, 32, Font.BOLD));
				Paragraph para = new Paragraph("Test", font);
				PdfPTable table = new PdfPTable(1);
				 List<Element> objects= HTMLWorker.parseToList(sr, null);
				PdfPCell cell = new PdfPCell();
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				 
				XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {
				    public void add(final Writable w) {
				        if (w instanceof WritableElement) {
				            elements = ((WritableElement) w).elements();
				        }
				    }
				}, sr);
				 
	            for (Element element : objects){
					 cell.addElement((Element) element);
				 }
				table.addCell(cell);
				
				
				document.add(table);
				document.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				fos.close();
			}
	    }*/
	    
	 /*public static void main(String[] args) {

    Document document = new Document(PageSize.A4, 30, 30, 30, 30);
    FileOutputStream fos=null;
    try {
    	fos=new FileOutputStream(RESULT);
      PdfWriter.getInstance(document,fos);
      Font aNormalTitleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
		aNormalTitleFont.setStyle(Font.UNDERLINE);
		
      FontFactory.registerDirectories();
      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
      document.open();
      StringReader sr=new StringReader("<DIV><span style='font-family: Times New Roman, serif; font-size: 10pt;'>This IS Times NEW Roman ON Item3</span></DIV>"+
									   "<DIV><span style='font-family: Arial, sans-serif; font-size: 12pt;'>This IS Arial 12 ON Item3</span></DIV>"+
									   "<DIV><span style='font-family: Courier New; font-size: 14pt;'>This IS Courier NEW 14 ON Item3</span></DIV>"+
									   "<DIV><span style='font-family: Tahoma, sans-serif; font-size: 18pt;'>This IS Tahoma 18 ON Item3</span></DIV>");
      List<Element> objects= HTMLWorker.parseToList(sr, null);
      float[] aWidths = {16.8f};
	  for (Element element : objects){
		  PdfPTable aPdfPTable = new PdfPTable(aWidths);
	      //document.add(element);
	      aPdfPTable.setWidthPercentage(100);
			aPdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			aPdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPadding(0);
		    cell.addElement(element);
		   aPdfPTable.addCell(cell);
		   document.add(aPdfPTable);
	  }
      document.close();

    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  }*/
	
 
 /*public static void main(String[] args)
	        throws DocumentException, IOException {
	        // step 1
	        Document document = new Document();
	        // step 2
	        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
	        // step 3
	        document.open();
	        // step 4
	        PdfPTable table = new PdfPTable(2);
	        table.setWidthPercentage(100);
	        Phrase p = new Phrase(
	            "Dr. iText or: How I Learned to Stop Worrying " +
	            "and Love the Portable Document Format.");
	        PdfPCell cell = new PdfPCell(p);
	        table.addCell("default leading / spacing");
	        table.addCell(cell);
	        table.addCell("absolute leading: 20");
	        cell.setLeading(20f, 0f);
	        table.addCell(cell);
	        table.addCell("absolute leading: 3; relative leading: 1.2");
	        cell.setLeading(3f, 1.2f);
	        table.addCell(cell);
	        table.addCell("absolute leading: 0; relative leading: 1.2");
	        cell.setLeading(0f, 1.2f);
	        table.addCell(cell);
	        table.addCell("no leading at all");
	        cell.setLeading(0f, 0f);
	        table.addCell(cell);
	        cell = new PdfPCell(new Phrase(
	            "Dr. iText or: How I Learned to Stop Worrying and Love PDF"));
	        table.addCell("padding 10");
	        cell.setPadding(10);
	        table.addCell(cell);
	        table.addCell("padding 0");
	        cell.setPadding(0);
	        table.addCell(cell);
	        table.addCell("different padding for left, right, top and bottom");
	        cell.setPaddingLeft(20);
	        cell.setPaddingRight(50);
	        cell.setPaddingTop(0);
	        cell.setPaddingBottom(5);
	        table.addCell(cell);
	        p = new Phrase("iText in Action Second Edition");
	        table.getDefaultCell().setPadding(2);
	        table.getDefaultCell().setUseAscender(false);
	        table.getDefaultCell().setUseDescender(false);
	        table.addCell("padding 2; no ascender, no descender");
	        table.addCell(p);
	        table.getDefaultCell().setUseAscender(true);
	        table.getDefaultCell().setUseDescender(false);
	        table.addCell("padding 2; ascender, no descender");
	        table.addCell(p);
	        table.getDefaultCell().setUseAscender(false);
	        table.getDefaultCell().setUseDescender(true);
	        table.addCell("padding 2; descender, no ascender");
	        table.addCell(p);
	        table.getDefaultCell().setUseAscender(true);
	        table.getDefaultCell().setUseDescender(true);
	        table.addCell("padding 2; ascender and descender");
	        cell.setPadding(2);
	        cell.setUseAscender(true);
	        cell.setUseDescender(true);
	        table.addCell(p);
	        document.add(table);
	        // step 5
	        document.close();
	    }*/
	 
   /* *//** Path to the resulting PDF file. *//*
    public static final String RESULT= "D:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/turbotracker/uploads/hello.pdf";
 
    *//**
     * Creates a PDF file: hello.pdf
     * @param    args    no arguments needed
     *//*
    public static void main(String[] args)
    	throws DocumentException, IOException {
    	new Itext().createPdf(RESULT);
    }
 
    *//**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException 
     *//*
    public void createPdf(String filename)
	throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }*/
	
	
	
	
	/*public static void main(String[] args) {
		for (int i = 1;i<150;i++) {
		    System.out.println("i="+i+" -> "+IntToLetter(i));
		  }
		
	}
	
	 public static String IntToLetter(int Int) {
		    if (Int<27){
		      return Character.toString((char)(Int+96));
		    } else {
		      if (Int%26==0) {
		        return IntToLetter((Int/26)-1)+IntToLetter(((Int-1)%26+1));
		      } else {
		        return IntToLetter(Int/26)+IntToLetter(Int%26);
		      }
		    }
		  }*/

}
