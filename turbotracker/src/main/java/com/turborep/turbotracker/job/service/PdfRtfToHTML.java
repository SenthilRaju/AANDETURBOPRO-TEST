package com.turborep.turbotracker.job.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

public class PdfRtfToHTML {
/*	public static void main(String[] args) throws IOException {
		String richText = "<p dir='ltr' style='line-height:1.15;margin-top:0pt;margin-bottom:0pt;' id='docs-internal-guid-7c73977b-27b2-688b-f037-31497f383085'><span style='font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:bold;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;'>return</span></p><br><span style='font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:bold;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;'></span><p dir='ltr' style='line-height:1.15;margin-top:0pt;margin-bottom:0pt;'><span style='font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:bold;font-style:italic;font-variant:normal;text-decoration:underline;vertical-align:baseline;'>test the string </span><span style='font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:normal;font-style:italic;font-variant:normal;text-decoration:underline;vertical-align:baseline;'>the </span></p><ul style='margin-top:0pt;margin-bottom:0pt;'><li dir='ltr' style='list-style-type:disc;font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:normal;font-style:italic;font-variant:normal;text-decoration:none;vertical-align:baseline;'><span style='font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:normal;font-style:italic;font-variant:normal;text-decoration:underline;vertical-align:baseline;'></span><br></li><li dir='ltr' style='list-style-type:disc;font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:normal;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;'><span style='font-size:15px;font-family:Arial;color:#000000;background-color:transparent;font-weight:normal;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;'>theb verix</span></li></ul>";
		String richTaxt = "{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1033{\\fonttbl{\\f0\\fnil\\fcharset0 Times;}{\\f1\\fnil\\fcharset0 MS Sans Serif;}}"
+"\\viewkind4\\uc1\\pard\\b\\f0\\fs20 Note: This count is an approximation for your use in determining labor. Final count will be based on contractor furnished shop drawings. Pricing can be adjusted accordingly."
+"\\par \\b0\\f1\\fs17 \\par }";
		String Html = rtfToHtml(new StringReader(richTaxt));
		System.out.println("Html is : \n"+Html);
		String Html1 = rtfToHtml(new StringReader(richText));
		System.out.println("Html is : \n"+Html1);
	}*/
	
	public static String rtfToHtml(Reader rtf) throws IOException {
		JEditorPane p = new JEditorPane();
		p.setContentType("text/rtf");
		EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
		try {
			kitRtf.read(rtf, p.getDocument(), 0);
			kitRtf = null;
			EditorKit kitHtml = p.getEditorKitForContentType("text/html");
			Writer writer = new StringWriter();
			kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
			return writer.toString();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
