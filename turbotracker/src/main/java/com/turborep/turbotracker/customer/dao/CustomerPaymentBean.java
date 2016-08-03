package com.turborep.turbotracker.customer.dao;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerPaymentBean {

	private Integer cuReceiptID, rxMasterID,cuInvoiceID,cuLinkagedetailId;
	private Short cuReceiptTypeID;
	private String customer, reference, memo, customerPoNumber,InvoiceNumber;
	private BigDecimal amount,invoiceBalance,DiscountUsed,paymentApplied,remaining,	preDiscountUsed,prePaymentApplied;
	private String receiptDate,datePaid;
	private Byte memoStatus;
	private boolean reversePaymentStatus;
	
	
	public Integer getCuReceiptID() {
		return cuReceiptID;	
	}
	public void setCuReceiptID(Integer cuReceiptID) {
		this.cuReceiptID = cuReceiptID;
	}
	
	public BigDecimal getInvoiceBalance() {
		return invoiceBalance;
	}
	public void setInvoiceBalance(BigDecimal invoiceBalance) {
		this.invoiceBalance = invoiceBalance;
	}
	public BigDecimal getDiscountUsed() {
		return DiscountUsed;
	}
	public void setDiscountUsed(BigDecimal discountUsed) {
		DiscountUsed = discountUsed;
	}
	public BigDecimal getPaymentApplied() {
		return paymentApplied;
	}
	public void setPaymentApplied(BigDecimal paymentApplied) {
		this.paymentApplied = paymentApplied;
	}
	public BigDecimal getRemaining() {
		return remaining;
	}
	public void setRemaining(BigDecimal remaining) {
		this.remaining = remaining;
	}
	public BigDecimal getPreDiscountUsed() {
		return preDiscountUsed;
	}
	public void setPreDiscountUsed(BigDecimal preDiscountUsed) {
		this.preDiscountUsed = preDiscountUsed;
	}
	public BigDecimal getPrePaymentApplied() {
		return prePaymentApplied;
	}
	public void setPrePaymentApplied(BigDecimal prePaymentApplied) {
		this.prePaymentApplied = prePaymentApplied;
	}
	public Integer getRxMasterID() {
		return rxMasterID;
	}
	public void setRxMasterID(Integer rxMastweID) {
		this.rxMasterID = rxMastweID;
	}
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getCustomerPoNumber() {
		return customerPoNumber;
	}
	public void setCustomerPoNumber(String customerPoNumber) {
		this.customerPoNumber = customerPoNumber;
	}
	public String getInvoiceNumber() {
		return InvoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}
	public Short getCuReceiptTypeID() {
		return cuReceiptTypeID;
	}
	public void setCuReceiptTypeID(Short cuReceiptTypeID) {
		this.cuReceiptTypeID = cuReceiptTypeID;
	}
	public Integer getCuInvoiceID() {
		return cuInvoiceID;
	}
	public void setCuInvoiceID(Integer cuInvoiceID) {
		this.cuInvoiceID = cuInvoiceID;
	}
	public Integer getCuLinkagedetailId() {
		return cuLinkagedetailId;
	}
	public void setCuLinkagedetailId(Integer cuLinkagedetailId) {
		this.cuLinkagedetailId = cuLinkagedetailId;
	}
	public Byte getMemoStatus() {
		return memoStatus;
	}
	public void setMemoStatus(Byte memoStatus) {
		this.memoStatus = memoStatus;
	}
	public String getDatePaid() {
		return datePaid;
	}
	public void setDatePaid(String datePaid) {
		this.datePaid = datePaid;
	}
	public boolean isReversePaymentStatus() {
		return reversePaymentStatus;
	}
	public void setReversePaymentStatus(boolean reversePaymentStatus) {
		this.reversePaymentStatus = reversePaymentStatus;
	}
	
	
	
	
}