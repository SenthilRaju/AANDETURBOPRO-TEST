package com.turborep.turbotracker.banking.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//Generated Apr 07, 2015 1:20:26 PM by Leo

@Entity
@Table(name = "veBillPaymentHistory", catalog = "")
public class VeBillPaymentHistory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer paymentHistoryID;
	private Integer veBillID;
	private String checkNo;
	private Date datePaid;
	private BigDecimal amountVal;
	
	public VeBillPaymentHistory() {
		super();
	}
	public VeBillPaymentHistory(Integer paymentHistoryID, Integer veBillID,
			String checkNo, Date datePaid,BigDecimal amountVal) {
		super();
		this.paymentHistoryID = paymentHistoryID;
		this.veBillID = veBillID;
		this.checkNo = checkNo;
		this.datePaid = datePaid;
		this.amountVal = amountVal;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "paymentHistoryID", unique = true, nullable = false)
	public Integer getPaymentHistoryID() {
		return paymentHistoryID;
	}
	public void setPaymentHistoryID(Integer paymentHistoryID) {
		this.paymentHistoryID = paymentHistoryID;
	}
	
	@Column(name = "veBillID")
	public Integer getVeBillID() {
		return veBillID;
	}
	public void setVeBillID(Integer veBillID) {
		this.veBillID = veBillID;
	}
	
	@Column(name = "checkNo")
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	@Column(name = "datePaid")
	public Date getDatePaid() {
		return datePaid;
	}
	public void setDatePaid(Date datePaid) {
		this.datePaid = datePaid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Column(name = "amountVal")
	public BigDecimal getAmountVal() {
		return amountVal;
	}
	public void setAmountVal(BigDecimal amountVal) {
		this.amountVal = amountVal;
	}	
	
	
	
}
