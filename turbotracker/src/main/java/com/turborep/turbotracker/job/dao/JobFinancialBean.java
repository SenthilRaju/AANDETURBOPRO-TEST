package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;

public class JobFinancialBean {
	 
	private String invoiceNumber;
	private String invoiceDate;
	private String datePaid;
	private BigDecimal invoiceTotal;
	private Byte currentDue;
	private BigDecimal currentDue1;
	private BigDecimal currentDue1withtax;
	
	private BigDecimal thirtyDays;
	private BigDecimal thirtyDayswithtax;
	private BigDecimal sixtyDays;
	private BigDecimal sixtyDayswithtax;
	private BigDecimal ninetyDays;
	private BigDecimal ninetyDayswithtax;
	private BigDecimal subTotal;
	private BigDecimal appliedAmount;
	private BigDecimal freight;
	private BigDecimal paidAmt;
	
	
	
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public BigDecimal getAppliedAmount() {
		return appliedAmount;
	}
	public void setAppliedAmount(BigDecimal appliedAmount) {
		this.appliedAmount = appliedAmount;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getDatePaid() {
		return datePaid;
	}
	public void setDatePaid(String datePaid) {
		this.datePaid = datePaid;
	}
	
	
	public BigDecimal getInvoiceTotal() {
		return invoiceTotal;
	}
	public void setInvoiceTotal(BigDecimal invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	public Byte getCurrentDue() {
		return currentDue;
	}
	public void setCurrentDue(Byte currentDue) {
		this.currentDue = currentDue;
	}
	public BigDecimal getThirtyDays() {
		return thirtyDays;
	}
	public void setThirtyDays(BigDecimal thirtyDays) {
		this.thirtyDays = thirtyDays;
	}
	public BigDecimal getSixtyDays() {
		return sixtyDays;
	}
	public void setSixtyDays(BigDecimal sixtyDays) {
		this.sixtyDays = sixtyDays;
	}
	public BigDecimal getNinetyDays() {
		return ninetyDays;
	}
	public void setNinetyDays(BigDecimal ninetyDays) {
		this.ninetyDays = ninetyDays;
	}
	public BigDecimal getCurrentDue1() {
		return currentDue1;
	}
	public void setCurrentDue1(BigDecimal currentDue1) {
		this.currentDue1 = currentDue1;
	}
	public BigDecimal getCurrentDue1withtax() {
		return currentDue1withtax;
	}
	public void setCurrentDue1withtax(BigDecimal currentDue1withtax) {
		this.currentDue1withtax = currentDue1withtax;
	}
	public BigDecimal getThirtyDayswithtax() {
		return thirtyDayswithtax;
	}
	public void setThirtyDayswithtax(BigDecimal thirtyDayswithtax) {
		this.thirtyDayswithtax = thirtyDayswithtax;
	}
	public BigDecimal getSixtyDayswithtax() {
		return sixtyDayswithtax;
	}
	public void setSixtyDayswithtax(BigDecimal sixtyDayswithtax) {
		this.sixtyDayswithtax = sixtyDayswithtax;
	}
	public BigDecimal getNinetyDayswithtax() {
		return ninetyDayswithtax;
	}
	public void setNinetyDayswithtax(BigDecimal ninetyDayswithtax) {
		this.ninetyDayswithtax = ninetyDayswithtax;
	}
	public BigDecimal getPaidAmt() {
		return paidAmt;
	}
	public void setPaidAmt(BigDecimal paidAmt) {
		this.paidAmt = paidAmt;
	}	
	

}
