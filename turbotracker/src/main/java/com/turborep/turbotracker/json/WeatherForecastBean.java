/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */

package com.turborep.turbotracker.json;

public class WeatherForecastBean {

	private String itsForecastDate;
	private int itsMaxTempCelc;
	private int itsMinTempCelc;
	private int itsMaxTempFH;
	private int itsMinTempFH;
	private String itsWeatherDescription;
	private String itsWeatherIconlocation;
	private int itsCurrentTempCelc;
	private String itsCurrentWeatherDescription;
	private String itsCurrentWeatherIconlocation;
	
	public String getItsForecastDate() {
		return itsForecastDate;
	}
	public void setItsForecastDate(String itsForecastDate) {
		this.itsForecastDate = itsForecastDate;
	}
	
	public float getItsMaxTempCelc() {
		return itsMaxTempCelc;
	}
	public void setItsMaxTempCelc(int itsMaxTempCelc) {
		this.itsMaxTempCelc = itsMaxTempCelc;
	}
	
	public float getItsMinTempCelc() {
		return itsMinTempCelc;
	}
	public void setItsMinTempCelc(int itsMinTempCelc) {
		this.itsMinTempCelc = itsMinTempCelc;
	}
	
	public int getItsMaxTempFH() {
		return itsMaxTempFH;
	}
	public void setItsMaxTempFH(int itsMaxTempFH) {
		this.itsMaxTempFH = itsMaxTempFH;
	}
	
	public int getItsMinTempFH() {
		return itsMinTempFH;
	}
	public void setItsMinTempFH(int itsMinTempFH) {
		this.itsMinTempFH = itsMinTempFH;
	}
	
	public String getItsWeatherDescription() {
		return itsWeatherDescription;
	}
	public void setItsWeatherDescription(String itsWeatherDescription) {
		this.itsWeatherDescription = itsWeatherDescription;
	}
	
	public String getItsWeatherIconlocation() {
		return itsWeatherIconlocation;
	}
	public void setItsWeatherIconlocation(String itsWeatherIconlocation) {
		this.itsWeatherIconlocation = itsWeatherIconlocation;
	}
	
	public float getItsCurrentTempCelc() {
		return itsCurrentTempCelc;
	}
	public void setItsCurrentTempCelc(int itsCurrentTempCelc) {
		this.itsCurrentTempCelc = itsCurrentTempCelc;
	}
	
	public String getItsCurrentWeatherDescription() {
		return itsCurrentWeatherDescription;
	}
	public void setItsCurrentWeatherDescription(String itsCurrentWeatherDescription) {
		this.itsCurrentWeatherDescription = itsCurrentWeatherDescription;
	}
	
	public String getItsCurrentWeatherIconlocation() {
		return itsCurrentWeatherIconlocation;
	}
	public void setItsCurrentWeatherIconlocation(String itsCurrentWeatherIconlocation) {
		this.itsCurrentWeatherIconlocation = itsCurrentWeatherIconlocation;
	}
}