package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;
import java.util.Date;

public class JobShippingBean {
	
	private String shipDate;
	private String shippingLine;
	private String vendorDate;
	private String vendorInvoice;
	private BigDecimal vendorAmount;
	private String customerDate;
	private  BigDecimal customerAmount;
	private  Integer veShipViaID;
	private  Integer joReleaseDetailID;
	private  Integer veBillID;
	private  Integer coAccountID;
	private  Integer cuInvoiceID;
	private  String invoiceDate;
	private  Short shiptoMode;
	private  BigDecimal vendorsubtotalAmt;
	private  Short transactionStatus;
	private String vechkNo;
	private String vedatePaid;
	private  String vendorAppliedAmt;
	private  Integer veCommDetailID;
	private  Boolean cIopenStatus;
	private  String veBillDate;
	private String webSight;
	private String trackingNumber;
	
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getShippingLine() {
		return shippingLine;
	}
	public void setShippingLine(String shippingLine) {
		this.shippingLine = shippingLine;
	}
	public String getVendorDate() {
		return vendorDate;
	}
	public void setVendorDate(String vendorDate) {
		this.vendorDate = vendorDate;
	}
	public String getVendorInvoice() {
		return vendorInvoice;
	}
	public void setVendorInvoice(String vendorInvoice) {
		this.vendorInvoice = vendorInvoice;
	}
	public BigDecimal getVendorAmount() {
		return vendorAmount;
	}
	public void setVendorAmount(BigDecimal vendorAmount) {
		this.vendorAmount = vendorAmount;
	}
	public String getCustomerDate() {
		return customerDate;
	}
	public void setCustomerDate(String customerDate) {
		this.customerDate = customerDate;
	}
	public BigDecimal getCustomerAmount() {
		return customerAmount;
	}
	public void setCustomerAmount(BigDecimal customerAmount) {
		this.customerAmount = customerAmount;
	}
	public Integer getVeShipViaID() {
		return veShipViaID;
	}
	public void setVeShipViaID(Integer veShipViaID) {
		this.veShipViaID = veShipViaID;
	}
	public Integer getJoReleaseDetailID() {
		return joReleaseDetailID;
	}
	public void setJoReleaseDetailID(Integer joReleaseDetailID) {
		this.joReleaseDetailID = joReleaseDetailID;
	}
	public Integer getVeBillID() {
		return veBillID;
	}
	public void setVeBillID(Integer veBillID) {
		this.veBillID = veBillID;
	}
	public Integer getCoAccountID() {
		return coAccountID;
	}
	public void setCoAccountID(Integer coAccountID) {
		this.coAccountID = coAccountID;
	}
	public Integer getCuInvoiceID() {
		return cuInvoiceID;
	}
	public void setCuInvoiceID(Integer cuInvoiceID) {
		this.cuInvoiceID = cuInvoiceID;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Short getShiptoMode() {
		return shiptoMode;
	}
	public void setShiptoMode(Short shiptoMode) {
		this.shiptoMode = shiptoMode;
	}
	public BigDecimal getVendorsubtotalAmt() {
		return vendorsubtotalAmt;
	}
	public void setVendorsubtotalAmt(BigDecimal vendorsubtotalAmt) {
		this.vendorsubtotalAmt = vendorsubtotalAmt;
	}
	public Short getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(Short transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getVechkNo() {
		return vechkNo;
	}
	public void setVechkNo(String vechkNo) {
		this.vechkNo = vechkNo;
	}
	public String getVedatePaid() {
		return vedatePaid;
	}
	public void setVedatePaid(String vedatePaid) {
		this.vedatePaid = vedatePaid;
	}
	
	public Integer getVeCommDetailID() {
		return veCommDetailID;
	}
	public void setVeCommDetailID(Integer veCommDetailID) {
		this.veCommDetailID = veCommDetailID;
	}
	public Boolean getcIopenStatus() {
		return cIopenStatus;
	}
	public void setcIopenStatus(Boolean cIopenStatus) {
		this.cIopenStatus = cIopenStatus;
	}
	public String getVendorAppliedAmt() {
		return vendorAppliedAmt;
	}
	public void setVendorAppliedAmt(String vendorAppliedAmt) {
		this.vendorAppliedAmt = vendorAppliedAmt;
	}
	public String getVeBillDate() {
		return veBillDate;
	}
	public void setVeBillDate(String veBillDate) {
		this.veBillDate = veBillDate;
	}
	public String getWebSight() {
		return webSight;
	}
	public void setWebSight(String webSight) {
		this.webSight = webSight;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
}