/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.home;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.turborep.turbotracker.home.exception.WeatherException;
import com.turborep.turbotracker.json.WeatherForecastBean;

public class WeatherSer {
	
	/**http://free.worldweatheronline.com/feed/weather.ashx?q=33915&format=json&num_of_days=5&key=085db41a2a121943122711*/
	/**http://api.worldweatheronline.com/free/v1/weather.ashx?q=33915&format=json&num_of_days=5&key=4cg5md4epmkzmc52d77gqvnf*/  /*New URL*/
	private long itsZipCode = 30060;
	private String itsApiKey = "4cg5md4epmkzmc52d77gqvnf";
	private int itsForecastDays = 3;
	private Logger logger = Logger.getLogger(WeatherSer.class);
	
	public WeatherSer(long theZipCode, String theApiKey, int theForecastDays){
		setZipCode(theZipCode);
		setApiKey(theApiKey);
		setForecastDays(theForecastDays);
	}
	
	public WeatherSer(){
		super();
	}
	
	/**
	 * This method is used to connect the weather webservice of world weather online.
	 * @return weather webservice response as {@link String}
	 * @throws WeatherException
	 */
	private String connectWebservice() throws WeatherException{
		String aUrlStr = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + getZipCode() + "&format=json&num_of_days=" + getForecastDays() + "&key="+getApiKey();
		try {
			/**  ========================= Proxy settings ================================	**/
			/**System.getProperties().put("http.proxyHost", "http://192.168.43.1");
			System.getProperties().put("http.proxyPort", "3128");
			System.getProperties().put("http.proxyUser", "vish_pepala");
			System.getProperties().put("http.proxyPassword", "S@Naidu");
			System.getProperties().put("http.proxySet", "true");
			/******************************************************************/
			
			PostMethod post = new PostMethod(aUrlStr);
			post.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
			HttpClient httpclient = new HttpClient();
			

			
			httpclient.executeMethod(post);
			String aResponse = post.getResponseBodyAsString();
			//logger.info(aResponse);
			return aResponse;
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			WeatherException aWeatherException = new WeatherException(e.getMessage(), e);
			throw aWeatherException;
		} catch (HttpException e) {
			logger.error(e.getMessage(), e);
			WeatherException aWeatherException = new WeatherException(e.getMessage(), e);
			throw aWeatherException;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			WeatherException aWeatherException = new WeatherException(e.getMessage(), e);
			throw aWeatherException;
		}
	}

	public ArrayList<WeatherForecastBean> getWeathrtForecast() throws WeatherException, ParseException{
		WeatherForecastBean aWeatherForecastBean = null;
		String aStr = connectWebservice();
		ArrayList<WeatherForecastBean> aWeatherForecastList = new ArrayList<WeatherForecastBean>();
		try {
			String aCurrentweatherDesc;
			JSONObject aWeatherObj = new JSONObject(aStr).getJSONObject("data");
			WeatherForecastBean aCurrentWeatherBean = new WeatherForecastBean();
			JSONObject aCurrentWeatherObj = aWeatherObj.getJSONArray("current_condition").getJSONObject(0);
			JSONArray aWeatherDescObj = aCurrentWeatherObj.getJSONArray("weatherDesc");
			JSONObject aCurrDescObj = (JSONObject) aWeatherDescObj.get(0);
			aCurrentweatherDesc = aCurrDescObj.getString("value");
			aCurrentWeatherBean.setItsCurrentWeatherDescription(aCurrentweatherDesc);
			aCurrentWeatherBean.setItsCurrentTempCelc(aCurrentWeatherObj.getInt("temp_C"));
			aCurrentWeatherBean.setItsCurrentWeatherIconlocation(((JSONObject)aCurrentWeatherObj.getJSONArray("weatherIconUrl").get(0)).getString("value"));
			aWeatherForecastList.add(aCurrentWeatherBean);
			JSONArray aWeatherArray = aWeatherObj.getJSONArray("weather");
			for (int index = 0; index < aWeatherArray.length(); index++) {
				aWeatherForecastBean = new WeatherForecastBean();
				JSONObject aWeatherForecastArr = aWeatherArray.getJSONObject(index);
				aWeatherForecastBean.setItsForecastDate(aWeatherForecastArr.getString("date"));
				aWeatherForecastBean.setItsMaxTempCelc((int) aWeatherForecastArr.getInt("tempMaxC"));
				aWeatherForecastBean.setItsMinTempCelc((int) aWeatherForecastArr.getInt("tempMinC"));
				aWeatherForecastBean.setItsMaxTempFH((int) aWeatherForecastArr.getInt("tempMaxF"));
				aWeatherForecastBean.setItsMinTempFH((int) aWeatherForecastArr.getInt("tempMinF"));
				aWeatherForecastBean.setItsWeatherDescription(((JSONObject)aWeatherForecastArr.getJSONArray("weatherDesc").get(0)).getString("value"));
				aWeatherForecastBean.setItsWeatherIconlocation(((JSONObject)aWeatherForecastArr.getJSONArray("weatherIconUrl").get(0)).getString("value"));
				aWeatherForecastList.add(aWeatherForecastBean);
			}
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			ParseException aParseException = new ParseException(e.getMessage(), e.getErrorOffset());
			throw aParseException;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			WeatherException aWeatherException = new WeatherException(e.getMessage(), e);
			throw aWeatherException;
		}
		return aWeatherForecastList;
	}
	
	public long getZipCode() {
		return itsZipCode;
	}

	public void setZipCode(long itsZipCode) {
		this.itsZipCode = itsZipCode;
	}

	public String getApiKey() {
		return itsApiKey;
	}

	public void setApiKey(String itsApiKey) {
		this.itsApiKey = itsApiKey;
	}

	public int getForecastDays() {
		return itsForecastDays;
	}

	public void setForecastDays(int itsForecastDays) {
		this.itsForecastDays = itsForecastDays;
	}
	
	public static void main(String args[]){
		WeatherSer aWeatherSer = new WeatherSer();
		try {
			aWeatherSer.connectWebservice();
		} catch (WeatherException e) {
			aWeatherSer.logger.error(e.getMessage(), e);
		}
	}
}
