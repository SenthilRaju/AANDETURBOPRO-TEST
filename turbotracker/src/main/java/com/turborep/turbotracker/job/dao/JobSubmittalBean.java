package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;



public class JobSubmittalBean {
	
	private String product;
	private String quantity;
	private String released;
	private String manufacture;
	private Integer manufactureID;
	private BigDecimal estimatecost;
	private BigDecimal cost;
	private Integer status ;
	private String schedule;
	private Integer joSubmittalID;
	private Integer joSchdTempHeaderID;
	private Integer joSubmittalHeaderID;
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setquantity(String obj) {
		this.quantity = obj;
	}
	public String getReleased() {
		return released;
	}
	public void setReleased(String obj) {
		this.released = obj;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public BigDecimal getEstimatecost() {
		return estimatecost;
	}
	public void setEstimatecost(BigDecimal obj) {
		this.estimatecost = obj;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal obj) {
		this.cost = obj;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer obj) {
		this.status = obj;
	}
	public Integer getManufactureID() {
		return manufactureID;
	}
	public void setManufactureID(Integer manufactureID) {
		this.manufactureID = manufactureID;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public Integer getJoSubmittalID() {
		return joSubmittalID;
	}
	public void setJoSubmittalID(Integer joSubmittalID) {
		this.joSubmittalID = joSubmittalID;
	}
	public Integer getJoSchdTempHeaderID() {
		return joSchdTempHeaderID;
	}
	public void setJoSchdTempHeaderID(Integer joSchdTempHeaderID) {
		this.joSchdTempHeaderID = joSchdTempHeaderID;
	}
	public Integer getJoSubmittalHeaderID() {
		return joSubmittalHeaderID;
	}
	public void setJoSubmittalHeaderID(Integer joSubmittalHeaderID) {
		this.joSubmittalHeaderID = joSubmittalHeaderID;
	}

}
