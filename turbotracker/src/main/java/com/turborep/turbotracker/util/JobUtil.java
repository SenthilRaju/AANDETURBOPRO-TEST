package com.turborep.turbotracker.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;

import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobServiceImpl;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.exception.VendorException;



public class JobUtil {
	
	protected static Logger itsLogger = Logger.getLogger(JobUtil.class);
	@Resource(name = "sessionFactory")
	private static SessionFactory itsSessionFactory;
/**
 * methd that returns the vepo Object by ID
 * @param vePOID
 * @return
 */
public static  Vepo getVepo (Integer vePOID){
	Session aSession = null;
	Vepo aVepo = null;
	try{
		aSession = itsSessionFactory.openSession();
		aVepo = (Vepo) aSession.get(Vepo.class,vePOID);
	}
	finally {
		aSession.flush();
		aSession.close();
	}
	return aVepo;
}

public static Integer getAutoIncrement(SessionFactory aSessionFactory,String tablename) throws  FileNotFoundException, SQLException {
	BigInteger aTotalCount = null;
	 Session aSession = null;
	 Connection connection = null;
	 String aJobCountStr = null;
	 try {
	final SessionFactoryImplementor sessionFactoryImplementor =(SessionFactoryImplementor) aSessionFactory;

	final Settings settings = sessionFactoryImplementor.getSettings();

	ConnectionProvider connectionProvider = settings.getConnectionProvider();

	connection = connectionProvider.getConnection();
	DatabaseMetaData databaseMetaData = connection.getMetaData();
	String url = databaseMetaData.getURL();

			
	 String dbname=url.substring(url.indexOf("3306/"), url.length());
		dbname=dbname.replaceAll("3306/","").trim();
		aJobCountStr = "SELECT AUTO_INCREMENT FROM information_schema.TABLES	WHERE TABLE_SCHEMA = '"+dbname+"'	AND TABLE_NAME ='"+tablename+"'";
		
			// Retrieve session from Hibernate
			aSession = aSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
			System.out.println(aTotalCount);
		
	 }catch(Exception e){
		 System.out.println("e.getMessage()"+e.getMessage());
	 }
	 finally {
			aSession.flush();
			aSession.close();
			connection.close();
			aJobCountStr = null;
		}

	
	return aTotalCount.intValue();
}
public static String convertinto12hourformat(String input){
	 
     //Date/time pattern of input date
     DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     //Date/time pattern of desired output date
     DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
     Date date = null;
     String output = null;
     try{
        //Conversion of input String to date
  	  date= df.parse(input);
        //old date format to new date format
  	  output = outputformat.format(date);
  	}catch(ParseException pe){
  	    pe.printStackTrace();
  	 }
     return output;
 }

public static Integer ConvertintoInteger(String Stringvalue){
	Integer returnvalue=0;
	try {
		if(Stringvalue!=null){
		returnvalue=Integer.parseInt(Stringvalue);
		}
	} catch (NumberFormatException e) {
		return returnvalue;
	}
	
	return returnvalue;
}
public static Double ConvertintoDouble(String Stringvalue){
	Double returnvalue=0.00;
	try {
		returnvalue=Double.parseDouble(Stringvalue);
	} catch (NumberFormatException e) {
		return returnvalue;
	}
	
	return returnvalue;
}
public static BigDecimal ConvertintoBigDecimal(String Stringvalue){
	BigDecimal returnvalue=new BigDecimal(0);
	try {
		itsLogger.info("Stringvalue"+Stringvalue);
		if(Stringvalue.contains("\"")){
			Stringvalue = Stringvalue.replaceAll("\"", "");
		}
		returnvalue=new BigDecimal(Stringvalue);
		itsLogger.info("PriceMultiplier::"+returnvalue);
		return returnvalue;
	} catch (Exception e) {
		return returnvalue;
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
  }

public static String getYesterdayDateString() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);    
    return dateFormat.format(cal.getTime());
}

public static String convertintoProperFormat(String texteditorfromjsp){
	String texteditor="";
	if(texteditorfromjsp!=null){
	texteditor=texteditorfromjsp.replaceAll("size: xx-small", "size: 7pt");
	texteditor=texteditor.replaceAll("size: x-small","size: 7.5pt");
	texteditor=texteditor.replaceAll("size: xx-large","size: 7.5pt");
	texteditor=texteditor.replaceAll("size: x-large","size: 7.5pt");
	texteditor=texteditor.replaceAll("size: smaller","size: 10pt");
	texteditor=texteditor.replaceAll("size: small", "size: 10pt");
	texteditor=texteditor.replaceAll("size: medium", "size: 12pt");
	texteditor=texteditor.replaceAll("size: larger", "size: 14pt");
	texteditor=texteditor.replaceAll("size: large", "size: 13.5pt");
	
	//Andale Mono=andale mono,times;Arial=Arial,sans-serif;Courier New=Courier New;Helvetica=helvetica;Symbol=Symbol;
	//Tahoma=Tahoma,sans-serif;
	//Times New Roman=Times New Roman,serif;Verdana=Verdana,sans-serif;
	texteditor=texteditor.replaceAll("'Times New Roman', serif;", "Times New Roman, serif;");
	texteditor=texteditor.replaceAll("'andale mono', times;", "andale mono, times;");
	texteditor=texteditor.replaceAll("'Arial', sans-serif;", "Arial,sans-serif;");
	texteditor=texteditor.replaceAll("'Courier New';", "Courier New;");
	texteditor=texteditor.replaceAll("'helvetica';", "helvetica;");
	texteditor=texteditor.replaceAll("'Symbol';", "Symbol;");
	texteditor=texteditor.replaceAll("'Tahoma', sans-serif;", "Tahoma, sans-serif;");
	texteditor=texteditor.replaceAll("'Verdana', sans-serif;", "Verdana, sans-serif;");
	
	}
	
return texteditor;
}

public static boolean containsOnlyNumbers(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (!Character.isDigit(str.charAt(i)))
        return false;
    }
    return true;
  }

public static String subStringvalue(String value,int from,int to){
	String returnValue="";
	if(value!=null){
		if(value.length()>to){
			returnValue=value.substring(from,to).toUpperCase();
		}else{
			returnValue=value.toUpperCase();
		}
	}
	return returnValue;
}
public static String convertDateFormat(String input,String inputFormat,String outputFormat){
	 //"yyyy-MM-dd HH:mm:ss aa"
    DateFormat df = new SimpleDateFormat(inputFormat);
    //Date/time pattern of desired output date
    DateFormat outputformat = new SimpleDateFormat(outputFormat);
    Date date = null;
    String output = null;
    try{
       //Conversion of input String to date
 	  date= df.parse(input);
       //old date format to new date format
 	  output = outputformat.format(date);
 	}catch(ParseException pe){
 	    pe.printStackTrace();
 	 }
    return output;
}

public static String removeSpecialcharacterswithslash(String valueWithSp){
	if(valueWithSp==null){
		valueWithSp="";
	}
	if(valueWithSp.contains("\\")){
		valueWithSp=valueWithSp.replaceAll("\\\\", "@@@@@@@@@");
		String[] array=valueWithSp.split("@@@@@@@@@");
		valueWithSp="";
		for(int i=0;i<array.length;i++){
			if(i==array.length-1){
				valueWithSp=valueWithSp+array[i];
			}else{
				valueWithSp=valueWithSp+array[i]+"\\\\\\\\";
			}
			
		}
		System.out.println("replaceeverything=="+valueWithSp);
		/*int a=valueWithSp.indexOf("\\");
		String text1=valueWithSp.substring(0,a);
		System.out.println("substring== 0to backwardslash"+text1);
		String text2=valueWithSp.substring(a+1,valueWithSp.length());
		System.out.println("substring== center to backwardslash"+text2);
		valueWithSp=text1+"\\\\\\\\"+text2;*/
	}
	 String specialChars = "/*!@#%^&*()\"{}_[]|?/<>,.'";
	    ArrayList<String> spincluded=new ArrayList<String>();
	    for (int i = 0; i < valueWithSp.length(); i++) {
	        if (specialChars.contains(valueWithSp.substring(i, i+1))) {
	        	if(!spincluded.contains(valueWithSp.substring(i, i+1))){
	        		spincluded.add(valueWithSp.substring(i, i+1));
	        	}
	        }
	    }
	    
	    for(String rep:spincluded){
	    		valueWithSp=valueWithSp.replaceAll("\\"+rep, "\\\\"+rep);
	    }
	 return valueWithSp;
}

public static BigDecimal floorFigureoverall(BigDecimal figure,Integer decimals){
	BigDecimal returnValue=BigDecimal.ZERO;
	try{
		Boolean check=checkhowmanydecimaloverall(figure);
		if(check){
			figure=figure.setScale(3, BigDecimal.ROUND_HALF_EVEN);
			if (decimals==null) decimals = 2;
		    BigDecimal d = new BigDecimal(Math.pow(10,decimals));
		    returnValue= cutitintodecimal((figure.multiply(d)).divide(d));
		}else{
			returnValue= figure.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		}
	}catch(Exception e){
		returnValue=new BigDecimal(0.00);
	}
	return returnValue;
}
public static Boolean checkhowmanydecimaloverall(BigDecimal Value){
	Boolean returnValue=false;
	try{
		String x_str = Value.toString();
		String Str[] =x_str.split("\\.");
		String decimal_digits =Str[1];
		if(decimal_digits.length() <=2){
			returnValue= false;
		} else{
			returnValue= true;
		}
	}catch(Exception e){
		returnValue=false;
	}
	return returnValue;
}
public static BigDecimal cutitintodecimal(BigDecimal figurevalue){
	BigDecimal returnValue=BigDecimal.ZERO;
	try{
		String x_str = figurevalue.toString();
		String[] str=x_str.split("\\.");
		if(str[1]!=null){
			String substr=str[1].toString();
			String suffix=substr.substring(0,2);
			returnValue=new BigDecimal(str[0]+"."+suffix);
		}else{
			returnValue=figurevalue;
		}
	}catch(Exception e){
		returnValue=BigDecimal.ZERO;
	}
	return returnValue;	
}
public static String convertinto12hourformat(String input,String inputformat,String opformat){
	 
    //Date/time pattern of input date
    DateFormat df = new SimpleDateFormat(inputformat);
    //Date/time pattern of desired output date
    DateFormat outputformat = new SimpleDateFormat(opformat);
    Date date = null;
    String output = null;
    try{
       //Conversion of input String to date
 	  date= df.parse(input);
       //old date format to new date format
 	  output = outputformat.format(date);
 	}catch(ParseException pe){
 	    pe.printStackTrace();
 	 }
    return output;
}
}
