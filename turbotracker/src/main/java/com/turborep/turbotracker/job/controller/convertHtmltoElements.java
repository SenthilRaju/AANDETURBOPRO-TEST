package com.turborep.turbotracker.job.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class convertHtmltoElements {
	 public static final String HTML = "<ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test1</span><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test1in</span></li></ul></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test2</span><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test2 in</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test3 in</span></li></ul></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test3</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test4</span></li></ul>";
	    public static final String DEST = "D:\\uploads\\Paragraph.pdf";
	 
	static List<Element> elements;
	/*public static List<Element> convert(StringReader HtmlContent) throws IOException{
		 XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {
			 @Override
			    public void add(final Writable w) {
			        if (w instanceof WritableElement) {
			        	elements=((WritableElement) w).elements();
			        }
			    }

				
			}, HtmlContent);
		 return elements;
	}*/
	public static List<Element> convert(StringReader HtmlContent) throws IOException{

		 
        // Parse HTML into Element list
 
        // CSS
        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
 
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
 
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline html = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
 
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        try {
			p.parse(HtmlContent);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
        List<Element> obj=elements;
    
		 return obj;
	}
	
	public static List<Element> convert(StringReader HtmlContent,HttpServletRequest theRequest) throws IOException{

		 
        // Parse HTML into Element list
 
        // CSS
        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        System.out.println("Path==>"+Thread.currentThread().getContextClassLoader());
        String csspath = theRequest.getSession().getServletContext().getRealPath("/resources/styles/defaultpdf.css");
        InputStream csspathtest=new FileInputStream(csspath);
       System.out.println("=="+csspathtest);
        CssFile cssfiletest = XMLWorkerHelper.getCSS(csspathtest);
        cssResolver.addCss(cssfiletest);   
        
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
 
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline html = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
 
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        
       /* StyleSheet ST = new StyleSheet();
        ST.loadStyle(HtmlTags.BODY, HtmlTags.FACE, "Arial Unicode MS");
        ST.loadStyle(HtmlTags.BODY, HtmlTags.ENCODING, BaseFont.IDENTITY_H);
        p.set*/
        try {
			p.parse(HtmlContent);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			csspathtest.close();
		}
 
        List<Element> obj=elements;
    
		 return obj;
	}
//	 public static void main(String[] args) throws IOException, DocumentException {
//	        File file = new File(DEST);
//	        file.getParentFile().mkdirs();
//	        new convertHtmltoElements().createPdf(DEST);
//	    }
//	 
//	 
//	    public void createPdf(String file) throws IOException, DocumentException {
//	 
//	        // Parse HTML into Element list
//	 
//	        // CSS
//	        CSSResolver cssResolver =
//	                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
//	 
//	        // HTML
//	        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
//	        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
//	        htmlContext.autoBookmark(false);
//	 
//	        // Pipelines
//	        ElementList elements = new ElementList();
//	        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
//	        HtmlPipeline html = new HtmlPipeline(htmlContext, end);
//	        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
//	 
//	        // XML Worker
//	        XMLWorker worker = new XMLWorker(css, true);
//	        XMLParser p = new XMLParser(worker);
//	        try {
//				p.parse(new StringReader(HTML));
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	 
//	        Document document = new Document();
//	        try {
//				PdfWriter.getInstance(document, new FileOutputStream(file));
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	        document.open();
//	        List<Element> obj=elements;
//	        for (Element e : obj) {
//	            document.add(e);
//	        }
//	        
//	 
//	        document.close();
//	    }
	 
}
