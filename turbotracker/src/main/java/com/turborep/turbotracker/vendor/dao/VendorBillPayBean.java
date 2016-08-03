package com.turborep.turbotracker.vendor.dao;

import java.math.BigDecimal;

public class VendorBillPayBean {

	public VendorBillPayBean() {
		
	}

	private String itsDueDate, itsBillDate;
	private String itsVendor, itsPONumber, itsInvoiceNumber;
	private BigDecimal itsBalance, itsBillAmount, itsAppliedAmount, itsFreightAmount, itsApplyingAmount,itsDiscountAmount;
	private int itsVeBillID, itsVendorRxMasterID;
	private int discountDays;
	private BigDecimal discountPercent;
	private byte dueOnDay;
	private short tranStatus;
	private String reason;
	
	
	
	
	
	
	
	
	
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public short getTranStatus() {
		return tranStatus;
	}
	public void setTranStatus(short tranStatus) {
		this.tranStatus = tranStatus;
	}
	
	
	public String getDueDate() {
		return itsDueDate;
	}
	public void setDueDate(String itsDueDate) {
		this.itsDueDate = itsDueDate;
	}
	public String getVendor() {
		return itsVendor;
	}
	public void setVendor(String itsVendor) {
		this.itsVendor = itsVendor;
	}
	public String getPONumber() {
		return itsPONumber;
	}
	public void setPONumber(String itsPONumber) {
		this.itsPONumber = itsPONumber;
	}
	public String getInvoiceNumber() {
		return itsInvoiceNumber;
	}
	public void setInvoiceNumber(String itsInvoiceNumber) {
		this.itsInvoiceNumber = itsInvoiceNumber;
	}
	public BigDecimal getBalance() {
		return itsBalance;
	}
	public void setBalance(BigDecimal itsBalance) {
		this.itsBalance = itsBalance;
	}
	public BigDecimal getBillAmount() {
		return itsBillAmount;
	}
	public void setBillAmount(BigDecimal itsBillAmount) {
		this.itsBillAmount = itsBillAmount;
	}
	public BigDecimal getAppliedAmount() {
		return itsAppliedAmount;
	}
	public void setAppliedAmount(BigDecimal itsAppliedAmount) {
		this.itsAppliedAmount = itsAppliedAmount;
	}
	public BigDecimal getFreightAmount() {
		return itsFreightAmount;
	}
	public void setFreightAmount(BigDecimal itsFreightAmount) {
		this.itsFreightAmount = itsFreightAmount;
	}
	public int getVeBillID() {
		return itsVeBillID;
	}
	public void setVeBillID(int itsVeBillID) {
		this.itsVeBillID = itsVeBillID;
	}
	public int getVendorRxMasterID() {
		return itsVendorRxMasterID;
	}
	public void setVendorRxMasterID(int itsVendorRxMasterID) {
		this.itsVendorRxMasterID = itsVendorRxMasterID;
	}
	public String getBillDate() {
		return itsBillDate;
	}
	public void setBillDate(String itsBillDate) {
		this.itsBillDate = itsBillDate;
	}
	public BigDecimal getApplyingAmount() {
		return itsApplyingAmount;
	}
	public void setApplyingAmount(BigDecimal applyingAmount) {
		this.itsApplyingAmount = applyingAmount;
	}
	
	public int getDiscountDays() {
		return discountDays;
	}
	public void setDiscountDays(int discountDays) {
		this.discountDays = discountDays;
	}
	public BigDecimal getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(BigDecimal discountPercent) {
		this.discountPercent = discountPercent;
	}
	public byte isDueOnDay() {
		return dueOnDay;
	}
	public void setDueOnDay(byte dueOnDay) {
		this.dueOnDay = dueOnDay;
	}
	
	public BigDecimal getDiscountAmount() {
		return itsDiscountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.itsDiscountAmount = discountAmount;
	}
	
	
}
