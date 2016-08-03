package com.turborep.turbotracker.util;

import java.awt.Container;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class ViewReportOwn {
	public static void main(String[] args) {
        try {
            HashMap parameterMap = new HashMap();
            parameterMap.put("rtitle", "Report Title Here");//sending the report title as a parameter.
            Report rpt = new Report(parameterMap);
            rpt.setReportName("productlist"); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {
            e.printStackTrace();
    }
	}
}
	
	 
	class Report extends JFrame {
	 
	    HashMap hm = null;
	    Connection con = null;
	    String reportName;
	 
	    public Report() {
	        setExtendedState(MAXIMIZED_BOTH);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	 
	    }
	 
	    public Report(HashMap map) {
	        this.hm = map;
	        setExtendedState(MAXIMIZED_BOTH);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	 
	    }
	 
	    public Report(HashMap map, Connection con) {
	        this.hm = map;
	        this.con = con;
	        setExtendedState(MAXIMIZED_BOTH);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        setTitle("Report Viewer");
	 
	    }
	 
	    public void setReportName(String rptName) {
	        this.reportName = rptName;
	    }
	 
	    public void callReport() {
	        JasperPrint jasperPrint = generateReport();
	        JRViewer viewer = new JRViewer(jasperPrint);
	        Container c = getContentPane();
	        c.add(viewer);
	        this.setVisible(true);
	    }
	 
	    public void callConnectionLessReport() {
	        JasperPrint jasperPrint = generateEmptyDataSourceReport();
	        JRViewer viewer = new JRViewer(jasperPrint);
	        Container c = getContentPane();
	        c.add(viewer);
	        this.setVisible(true);
	    }
	 
	    public void closeReport() {
	        //jasperViewer.setVisible(false);
	    }
	 
	    /** this method will call the report from data source*/
	    public JasperPrint generateReport() {
	        try {
	            
	            JasperPrint jasperPrint = null;
	            try {
	                /**You can also test this line if you want to display 
	                 * report from any absolute path other than the project root path*/
	                jasperPrint = JasperFillManager.fillReport("F:/CuInvoiceReportFinalNew.jasper",hm);
	                //jasperPrint = JasperFillManager.fillReport(reportName + ".jasper", hm, con);
	            } catch (JRException e) {
	                e.printStackTrace();
	            }
	            return jasperPrint;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return null;
	        }
	 
	 
	    }
	 
	    /** call this method when your report has an empty data source*/
	    public JasperPrint generateEmptyDataSourceReport() {
	        try {
	            JasperPrint jasperPrint = null;
	            if (hm == null) {
	                hm = new HashMap();
	            }
	            try {
	                jasperPrint = JasperFillManager.fillReport(reportName + ".jasper", hm, new JREmptyDataSource());
	            } catch (JRException e) {
	                e.printStackTrace();
	            }
	            return jasperPrint;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return null;
	        }
	 
	    }
	}