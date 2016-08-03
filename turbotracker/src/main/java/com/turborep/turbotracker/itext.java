package com.turborep.turbotracker;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
 




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
 
public class itext {
 
    class Header extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;
 
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                font = new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            } 
        }
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
        	/* PdfPTable table = new PdfPTable(1);
        	 table.setTotalWidth(527);
        	 table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
            PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			canvas.endMarkedContentSequence();
			table.writeSelectedRows(0, -1, 36, 30, canvas);*/
        	
        	 PdfPTable table = new PdfPTable(3);
             try {
                 table.setWidths(new int[]{24, 24, 2});
                 table.setTotalWidth(527);
                 table.getDefaultCell().setFixedHeight(20);
                 table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                 table.addCell(new Phrase("Test", font));
                 table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                 table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
                 PdfPCell cell = new PdfPCell(total);
                 cell.setBorder(Rectangle.BOTTOM);
                 table.addCell(cell);
                 PdfContentByte canvas = writer.getDirectContent();
                 canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                 table.writeSelectedRows(0, -1, 36, 30, canvas);
                 canvas.endMarkedContentSequence();
             } catch (DocumentException de) {
                 throw new ExceptionConverter(de);
             }
			//ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page: %d",writer.getCurrentPageNumber())), 559, 793, 0);
        }
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
        	/* PdfPTable table = new PdfPTable(1);
        	 table.setTotalWidth(527);
        	 table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
            PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			canvas.endMarkedContentSequence();
			table.writeSelectedRows(0, -1, 36, 30, canvas);*/
        	
//        	 PdfPTable table = new PdfPTable(3);
//             try {
//                 table.setWidths(new int[]{24, 24, 2});
//                 table.setTotalWidth(527);
//                 table.getDefaultCell().setFixedHeight(20);
//                 table.getDefaultCell().setBorder(Rectangle.BOTTOM);
//                 table.addCell(new Phrase("Test", font));
//                 table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                 table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
//                 PdfPCell cell = new PdfPCell(total);
//                 cell.setBorder(Rectangle.BOTTOM);
//                 table.addCell(cell);
//                 PdfContentByte canvas = writer.getDirectContent();
//                 canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
//                 table.writeSelectedRows(0, -1, 36, 30, canvas);
//                 canvas.endMarkedContentSequence();
//             } catch (DocumentException de) {
//                 throw new ExceptionConverter(de);
//             }
			//ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("Page: %d",writer.getCurrentPageNumber())), 559, 793, 0);
        }
 
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber() - 1), font),
                2, 2, 0);
        }
    }
 
    public static final String DEST = "D:\\uploads\\Paragraph.pdf";
 
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new itext().createPdf(DEST);
    }
 
    public void createPdf(String dest) throws IOException, DocumentException {
    	FileOutputStream fos=new FileOutputStream(dest);
        try {
			Font font =  new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			Font bold =  new Font(FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			Document document = new Document(PageSize.A4.rotate());
			PdfWriter writer = PdfWriter.getInstance(document, fos);
			writer.setPageEvent(new Header());
			document.open();
			
			document.add(Chunk.NEXTPAGE);
			document.add(Chunk.NEXTPAGE);
			document.add(Chunk.NEXTPAGE);
			document.add(Chunk.NEXTPAGE);
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fos.close();
		}
    }
 
    public void process(PdfPTable table, String line, Font font) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            table.addCell(new Phrase(tokenizer.nextToken(), font));
        }
    }
}