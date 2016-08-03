package com.turborep.turbotracker.employee.dao;

import java.math.BigDecimal;

public class CommissionCalculateBean {
	private Integer joReleaseID;
	private String date="";
	private BigDecimal totalSales;
	private BigDecimal totalCosts;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(BigDecimal totalSales) {
		this.totalSales = totalSales;
	}
	public BigDecimal getTotalCosts() {
		return totalCosts;
	}
	public void setTotalCosts(BigDecimal totalCosts) {
		this.totalCosts = totalCosts;
	}
	public Integer getJoReleaseID() {
		return joReleaseID;
	}
	public void setJoReleaseID(Integer joReleaseID) {
		this.joReleaseID = joReleaseID;
	}
}
