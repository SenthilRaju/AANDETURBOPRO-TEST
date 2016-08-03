package com.turborep.turbotracker.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.turborep.turbotracker.job.controller.DynamicReportbean;
import com.turborep.turbotracker.mail.SendQuoteMail;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

public class ReportService {
	public static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	public static final String MEDIA_TYPE_PDF = "application/pdf";
	public static final String EXTENSION_TYPE_EXCEL = "xls";
	public static final String EXTENSION_TYPE_PDF = "pdf";
	 
	protected static Logger itsLogger = Logger.getLogger(ReportService.class);
	public static  void ReportCall(HttpServletResponse response ,HashMap jasperParameter,String type, String jrxmlPath,String fileName,Connection conn) throws SQLException, IOException {
		ByteArrayOutputStream baos =null;
		try {
			/*
			 * JasperReport is the object that holds our compiled jrxml file
			 */
			JasperReport jasperReport;

			/*
			 * JasperPrint is the object contains report after result filling
			 * process
			 */
			JasperPrint jasperPrint;

			// connection is the data source we used to fetch the data from

			
						jasperReport = JasperCompileManager.compileReport(jrxmlPath);

						jasperParameter.put(JRPdfExporterParameter.FORCE_LINEBREAK_POLICY, true);
						jasperParameter.put(JRPdfExporterParameter.FORCE_SVG_SHAPES, true);
			// filling report with data from data source			
			
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					jasperParameter, conn);

			// exporting process
			// 1- export to PDF
			/*JasperExportManager.exportReportToPdfFile(jasperPrint, C:/Users/user/velmurugan/Sekar/arima/WebContent/reports/sample_report.pdf");*/
			
			baos = new ByteArrayOutputStream();
			export(type, jasperPrint,response,  baos,fileName);
			write(response, baos);
			
		} catch (JRException e) {
			e.printStackTrace();
		}finally{
			if(baos!=null){
				try {
					baos.close();
					baos=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();;
					conn=null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
		}
	}
	public static  HttpServletResponse export(String type, 
			JasperPrint jp, 
			HttpServletResponse response,
			ByteArrayOutputStream baos,String fileName) {
		try{
		if (type.equalsIgnoreCase(EXTENSION_TYPE_EXCEL)) {
			// Export to output stream
			exportXls(jp, baos);
			 
			// Set our response properties
			// Here you can declare a custom filename
			//String fileName = "UserReport.xls";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
		   // Set content type
		    response.setContentType(MEDIA_TYPE_EXCEL);
			response.setContentLength(baos.size());
						
			
			return response;
		}
		
		if (type.equalsIgnoreCase(EXTENSION_TYPE_PDF)) {
			// Export to output stream
			exportPdf(jp, baos);
			 
			// Set our response properties
			// Here you can declare a custom filename
			//String fileName = "UserReport.pdf";
			response.setHeader("Content-Disposition", "inline; filename="+ fileName);
			
			// Set content type
			response.setContentType(MEDIA_TYPE_PDF);
			response.setContentLength(baos.size());
			
			return response;
			
		} 
		}catch(Exception e){
			
		}finally{
			if(baos!=null){
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			baos=null;
			type=null;
		}
		throw new RuntimeException("No type set for type " + type);
	}

	public static void exportXls(JasperPrint jp, ByteArrayOutputStream baos) {
		// Create a JRXlsExporter instance
		JRXlsExporter exporter = new JRXlsExporter();
		 
		// Here we assign the parameters jp and baos to the exporter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// Excel specific parameters
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		 
		try {
			exporter.exportReport();
			
		} catch (JRException e) {
			throw new RuntimeException(e);
		}finally{
			if(baos!=null){
				try {
					baos.close();
					baos=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			exporter=null;
				
		}
	}
	public static void exportPdf(JasperPrint jp, ByteArrayOutputStream baos) {
		// Create a JRXlsExporter instance
		JRPdfExporter exporter = new JRPdfExporter();
		 
		// Here we assign the parameters jp and baos to the exporter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		try {
			exporter.exportReport();
			
		} catch (JRException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(baos!=null){
			try {
				baos.close();
				baos=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		}
	}
	public static void write( HttpServletResponse response,
			ByteArrayOutputStream baos) throws IOException {
		ServletOutputStream outputStream  = null;
		try {
			
			// Retrieve output stream
			outputStream = response.getOutputStream();
			// Write to output stream
			baos.writeTo(outputStream);
			// Flush the stream
			outputStream.flush();
			
			/*// Remove download token
			tokenService.remove(token);*/
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally{
			if(baos!=null){
				baos.close();
				baos=null;
				}
			if(outputStream!=null){
				outputStream.close();
				outputStream=null;
			}
		}
	}
	
	public static  void dynamicReportCall(HttpServletResponse response ,HashMap jasperParameter,String type, JasperDesign  newjd,String fileName,Connection conn) throws SQLException, IOException {
		ByteArrayOutputStream baos =null;
		try {
			/*
			 * JasperReport is the object that holds our compiled jrxml file
			 */
			JasperReport jasperReport;

			/*
			 * JasperPrint is the object contains report after result filling
			 * process
			 */
			JasperPrint jasperPrint;

			// connection is the data source we used to fetch the data from
			
						jasperReport = JasperCompileManager.compileReport(newjd);
						
						//If want to see dynamic jrxml file enable below code
						//JRXmlWriter.writeReport(jasperReport, "E://vel.jrxml", "UTF-8");

			// filling report with data from data source			
						
			
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					jasperParameter, conn);
			
			
			
			// exporting process
			// 1- export to PDF
			//JasperExportManager.exportReportToPdfFile(jasperPrint, "E:/vel/sample_report.pdf");
			
			baos = new ByteArrayOutputStream();
			export(type, jasperPrint,response,  baos,fileName);
			write(response, baos);
		} catch (JRException e) {
			e.printStackTrace();
		}finally{
			if(baos!=null){
				baos.close();
				baos=null;
			}
			if(conn!=null){
			conn.close();
			conn=null;
			}
		}
	}
	
	// Add ColumnHeader
		public static JRDesignStaticText  AddColumnHeader(DynamicReportbean bean){
			// 0 3 100 20 header1 true 8 true "html"
			JRDesignStaticText headerStaticText = new JRDesignStaticText();
	        headerStaticText.setX(bean.getX());
	        headerStaticText.setY(bean.getY());
	        headerStaticText.setWidth(bean.getWidth());
	        headerStaticText.setHeight(bean.getHeight());
	        headerStaticText.setText(bean.getText());
	        headerStaticText.setPrintRepeatedValues(bean.isPrintRepeatedValues());
	        headerStaticText.setFontName("SansSerif");
	        headerStaticText.setFontSize(10);
	        //headerStaticText.setFontSize(bean.fontsize);
	        headerStaticText.setBold(bean.isBold());
	        headerStaticText.setMarkup(bean.getMarkup());
	        headerStaticText.setUnderline(bean.isUnderline());
	        //headerStaticText.getLineBox().setLeftPadding(2);
	        //headerStaticText.getLineBox().setRightPadding(2);
	        return headerStaticText;
		}
		
		 // Add Textfield
		public static JRDesignTextField  AddColumnTextField(DynamicReportbean bean){
			JRDesignExpression expression = new JRDesignExpression();
			JRDesignTextField textField = new JRDesignTextField();
			 //expression.setValueClass(String.class);
		     expression.setText(bean.getText());
		     textField.setExpression(expression);
	        textField.setX(bean.getX());
	        textField.setY(bean.getY());
	        textField.setHeight(bean.getHeight());
	        textField.setWidth(bean.getWidth());
	        textField.setPrintWhenDetailOverflows(true);
	        textField.setBlankWhenNull(true);
	        textField.setStretchWithOverflow(true);
	        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
	        textField.setPositionType(PositionTypeEnum.FLOAT);
	        textField.setBold(bean.isBold());
	        textField.setUnderline(bean.isUnderline());
	        textField.setMarkup("html");
	        textField.setFontName("SansSerif");
	        textField.setFontSize(10);
	        
	      
	        return textField;
		}
		
		/*public static JRDesignStyle getNormalStyle() {
	        JRDesignStyle normalStyle = new JRDesignStyle();
	        normalStyle.setName("Sans_Normal");
	        normalStyle.setDefault(true);
	        normalStyle.setFontName("SansSerif");
	        normalStyle.setFontSize(8);
	        normalStyle.setPdfFontName("Helvetica");
	        normalStyle.setPdfEncoding("Cp1252");
	        normalStyle.setPdfEmbedded(false);
	        return normalStyle;
	    }*/
		
		public static  void WriteReportCall(HttpServletRequest request,HttpServletResponse response ,HashMap jasperParameter,String type, String jrxmlPath,String fileName,Connection conn) throws SQLException {
			try {
				String root = request.getRealPath("/");
                File path = new File(root + "/uploads");
                
                	if (!path.exists()) {
                      boolean status = path.mkdirs();
                    }
                
                File uploadedFile = new File(path + "/" + fileName);
                itsLogger.info("filepath"+uploadedFile.getAbsolutePath());
                String realpathhh=uploadedFile.getAbsolutePath();
                itsLogger.info("REalPath===>"+realpathhh);
				/*
				 * JasperReport is the object that holds our compiled jrxml file
				 */
				JasperReport jasperReport;

				/*
				 * JasperPrint is the object contains report after result filling
				 * process
				 */
				JasperPrint jasperPrint;

				// connection is the data source we used to fetch the data from

				
							jasperReport = JasperCompileManager.compileReport(jrxmlPath);

				// filling report with data from data source			
				
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						jasperParameter, conn);
				//JRXmlWriter.writeReport(jasperReport, "E://vel.jrxml", "UTF-8");
				// exporting process
				// 1- export to PDF
				JasperExportManager.exportReportToPdfFile(jasperPrint, realpathhh);
				
				/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
				export(type, jasperPrint,response,  baos,fileName);
				write(response, baos);*/
				conn.close();
			} catch (JRException e) {
				e.printStackTrace();
			}finally{
				if(conn!=null){
					conn.close();
					conn=null;
				}
			}
		}
		
		
		public static  void dynamicWriteReportCall(HttpServletRequest request,HttpServletResponse response ,HashMap jasperParameter,String type, JasperDesign  newjd,String fileName,Connection conn) throws SQLException {
			try {
				String root = request.getRealPath("/");
                File path = new File(root + "/uploads");
                
                 if (!path.exists()) {
                        boolean status = path.mkdirs();
                    }
                	
                System.out.println("===>"+fileName);	
                
                File uploadedFile = new File(path + "/" + fileName);
                itsLogger.info("filepath"+uploadedFile.getAbsolutePath());
                String realpathhh=uploadedFile.getAbsolutePath();
                itsLogger.info("REalPath===>"+realpathhh);
				/*
				 * JasperReport is the object that holds our compiled jrxml file
				 */
				JasperReport jasperReport;

				/*
				 * JasperPrint is the object contains report after result filling
				 * process
				 */
				JasperPrint jasperPrint;

				// connection is the data source we used to fetch the data from

				
				jasperReport = JasperCompileManager.compileReport(newjd);

				// filling report with data from data source			
				
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						jasperParameter, conn);
				//JRXmlWriter.writeReport(jasperReport, "E://vel.jrxml", "UTF-8");
				// exporting process
				// 1- export to PDF
				JasperExportManager.exportReportToPdfFile(jasperPrint, realpathhh);
				
				/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
				export(type, jasperPrint,response,  baos,fileName);
				write(response, baos);*/
				conn.close();
			} catch (JRException e) {
				e.printStackTrace();
			}finally{
				if(conn!=null){
					conn.close();
					conn=null;
				}
			}
		}
		
		public static  void ReportDownload(HttpServletResponse response ,HashMap jasperParameter,String type, String jrxmlPath,String fileName,Connection conn) throws SQLException, IOException {
			ByteArrayOutputStream baos =null;
			ServletOutputStream out =null;
			try {
				/*
				 * JasperReport is the object that holds our compiled jrxml file
				 */
				JasperReport jasperReport;
				out = response.getOutputStream();
				/*
				 * JasperPrint is the object contains report after result filling
				 * process
				 */
				
				response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
				response.setContentType("application/pdf");
				JasperPrint jasperPrint;

				// connection is the data source we used to fetch the data from

				
				jasperReport = JasperCompileManager.compileReport(jrxmlPath);

				// filling report with data from data source			
				
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						jasperParameter, conn);

				// exporting process
				// 1- export to PDF
				/*JasperExportManager.exportReportToPdfFile(jasperPrint, C:/Users/user/velmurugan/Sekar/arima/WebContent/reports/sample_report.pdf");*/
				
				baos = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
				out.write(baos.toByteArray());
				
			} catch (JRException e) {
				e.printStackTrace();
			}finally{
				if(conn!=null){
					conn.close();
					conn=null;
				}
				if(baos!=null){
					baos.close();
					baos=null;
				}
				if(out!=null){
					out.close();
					out=null;
				}
			}
		}
}
