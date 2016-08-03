package com.turborep.turbotracker.employee.dao;

import java.math.BigDecimal;


public class EmMasterBean {
	private Integer emMasterId;
	private Integer userLoginId;
	private String webName;
	private BigDecimal jobCommissions;
	private BigDecimal otherCommissions;
	private BigDecimal adjustments;
	private BigDecimal payment;
	private Integer ecStatementID;
	
	public Integer getEcStatementID() {
		return ecStatementID;
	}
	public void setEcStatementID(Integer ecStatementID) {
		this.ecStatementID = ecStatementID;
	}
	public Integer getEmMasterId() {
		return emMasterId;
	}
	public void setEmMasterId(Integer emMasterId) {
		this.emMasterId = emMasterId;
	}
	public Integer getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(Integer userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public BigDecimal getJobCommissions() {
		return jobCommissions;
	}
	public void setJobCommissions(BigDecimal jobCommissions) {
		this.jobCommissions = jobCommissions;
	}
	public BigDecimal getOtherCommissions() {
		return otherCommissions;
	}
	public void setOtherCommissions(BigDecimal otherCommissions) {
		this.otherCommissions = otherCommissions;
	}
	public BigDecimal getAdjustments() {
		return adjustments;
	}
	public void setAdjustments(BigDecimal adjustments) {
		this.adjustments = adjustments;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
}
