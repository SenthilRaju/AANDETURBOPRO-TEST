package com.turborep.turbotracker.job.controller;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
 



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
public class NestedListHtml {
	 
    public static final String HTML = "<ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test1</span><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test1in</span></li></ul></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test2</span><ul><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test2 in</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>test3 in</span></li></ul></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test3</span></li><li><span style='font-family: Times New Roman, serif; font-size: 10pt;'>Test4</span></li></ul>";
    public static final String DEST = "D:\\uploads\\Paragraph.pdf";
 
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedListHtml().createPdf(DEST);
    }
 
 
    public void createPdf(String file) throws IOException, DocumentException {
 
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
			p.parse(new StringReader(HTML));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
        // step 1
        Document document = new Document();
        FileOutputStream fos=null;
        try {
        	fos=new FileOutputStream(file);
			PdfWriter.getInstance(document, fos);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			fos.close();
		}
        document.open();
        for (Element e : elements) {
            document.add(e);
        }
        document.add(Chunk.NEWLINE);
        Paragraph para = new Paragraph();
        for (Element e : elements) {
            para.add(e);
        }
        document.add(para);
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(2);
        table.addCell("Nested lists don't work in a cell");
        PdfPCell cell = new PdfPCell();
        for (Element e : elements) {
            cell.addElement(e);
        }
        table.addCell(cell);
        document.add(table);
 
        document.close();
    }
 
}
