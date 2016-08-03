package com.turborep.turbotracker.vendor.dao;

import java.math.BigDecimal;
import java.util.Date;

public class SalesOrderBean {
	
	private Integer cuSoid;
	private Integer cuSodetailId;
	private Date createdOn;
	private String datePromised;
	private String name;
	private String jobName;
	private BigDecimal quantityOrdered;
	private String city;
	private Integer rxCustomerId;
	private String number;
	
	public Integer getCuSoid() {
		return cuSoid;
	}
	public void setCuSoid(Integer cuSoid) {
		this.cuSoid = cuSoid;
	}
	public Integer getCuSodetailId() {
		return cuSodetailId;
	}
	public void setCuSodetailId(Integer cuSodetailId) {
		this.cuSodetailId = cuSodetailId;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getDatePromised() {
		return datePromised;
	}
	public void setDatePromised(String datePromised) {
		this.datePromised = datePromised;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public BigDecimal getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(BigDecimal quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getRxCustomerId() {
		return rxCustomerId;
	}
	public void setRxCustomerId(Integer rxCustomerId) {
		this.rxCustomerId = rxCustomerId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	

}
