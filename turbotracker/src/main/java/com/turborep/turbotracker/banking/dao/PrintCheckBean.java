package com.turborep.turbotracker.banking.dao;

import java.math.BigDecimal;
import java.util.Date;

public class PrintCheckBean {
	
	private short pagebreak;
	private Integer moTransactionID,rxMasterID,rxAddressID;
	private BigDecimal amount, amountPaid, discount,billAmount,appliedAmount;
	private String description,transactionDate,reference,invoiceNo,ponumber;
	private Date billDate;
	public short getPagebreak() {
		return pagebreak;
	}
	public void setPagebreak(short pagebreak) {
		this.pagebreak = pagebreak;
	}
	public Integer getMoTransactionID() {
		return moTransactionID;
	}
	public void setMoTransactionID(Integer moTransactionID) {
		this.moTransactionID = moTransactionID;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Integer getRxMasterID() {
		return rxMasterID;
	}
	public void setRxMasterID(Integer rxMasterID) {
		this.rxMasterID = rxMasterID;
	}
	public Integer getRxAddressID() {
		return rxAddressID;
	}
	public void setRxAddressID(Integer rxAddressID) {
		this.rxAddressID = rxAddressID;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPonumber() {
		return ponumber;
	}
	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public BigDecimal getAppliedAmount() {
		return appliedAmount;
	}
	public void setAppliedAmount(BigDecimal appliedAmount) {
		this.appliedAmount = appliedAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

}
