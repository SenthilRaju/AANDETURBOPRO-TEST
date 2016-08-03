package com.turborep.turbotracker.finance.dao;

import java.math.BigDecimal;
import java.util.Date;


public class GlReporting  {
	private Integer coLedgerHeaderID;
	private Integer glTransactionID;
	private Integer coFiscalPeriodID;
	private Integer period;
	private String pStartDate;
	private String pEndDate;
	private Integer coFiscalYearID;
	private String fiscalYear;
	private String yStartDate;
	private String yEndDate;
	private Integer coLedgerSourceID;
	private String journalID;
	private String sDescription;
	private Integer sourceNo;
	private String postDate;
	private Integer transactionReferenceID;
	private String reference;
	private Integer posted;
	private Integer coLedgerDetailID;
	private Integer coAccountID;
	private String number;
	private String aDescription;
	private BigDecimal amount;
	private BigDecimal totalAmount;
	private String moTDescription;
	private String glTDescription;
	
	
	public Integer getCoLedgerHeaderID() {
		return coLedgerHeaderID;
	}
	public void setCoLedgerHeaderID(Integer coLedgerHeaderID) {
		this.coLedgerHeaderID = coLedgerHeaderID;
	}
	public Integer getCoFiscalPeriodID() {
		return coFiscalPeriodID;
	}
	public void setCoFiscalPeriodID(Integer coFiscalPeriodID) {
		this.coFiscalPeriodID = coFiscalPeriodID;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getpStartDate() {
		return pStartDate;
	}
	public void setpStartDate(String pStartDate) {
		this.pStartDate = pStartDate;
	}
	public String getpEndDate() {
		return pEndDate;
	}
	public void setpEndDate(String pEndDate) {
		this.pEndDate = pEndDate;
	}
	public Integer getCoFiscalYearID() {
		return coFiscalYearID;
	}
	public void setCoFiscalYearID(Integer coFiscalYearID) {
		this.coFiscalYearID = coFiscalYearID;
	}
	public String getFiscalYear() {
		return fiscalYear;
	}
	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
	public String getyStartDate() {
		return yStartDate;
	}
	public void setyStartDate(String yStartDate) {
		this.yStartDate = yStartDate;
	}
	public String getyEndDate() {
		return yEndDate;
	}
	public void setyEndDate(String yEndDate) {
		this.yEndDate = yEndDate;
	}
	public Integer getCoLedgerSourceID() {
		return coLedgerSourceID;
	}
	public void setCoLedgerSourceID(Integer coLedgerSourceID) {
		this.coLedgerSourceID = coLedgerSourceID;
	}
	public String getJournalID() {
		return journalID;
	}
	public void setJournalID(String journalID) {
		this.journalID = journalID;
	}
	public String getsDescription() {
		return sDescription;
	}
	public void setsDescription(String sDescription) {
		this.sDescription = sDescription;
	}
	public Integer getSourceNo() {
		return sourceNo;
	}
	public void setSourceNo(Integer sourceNo) {
		this.sourceNo = sourceNo;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public Integer getTransactionReferenceID() {
		return transactionReferenceID;
	}
	public void setTransactionReferenceID(Integer transactionReferenceID) {
		this.transactionReferenceID = transactionReferenceID;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Integer getPosted() {
		return posted;
	}
	public void setPosted(Integer posted) {
		this.posted = posted;
	}
	public Integer getCoLedgerDetailID() {
		return coLedgerDetailID;
	}
	public void setCoLedgerDetailID(Integer coLedgerDetailID) {
		this.coLedgerDetailID = coLedgerDetailID;
	}
	public Integer getCoAccountID() {
		return coAccountID;
	}
	public void setCoAccountID(Integer coAccountID) {
		this.coAccountID = coAccountID;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getaDescription() {
		return aDescription;
	}
	public void setaDescription(String aDescription) {
		this.aDescription = aDescription;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getGlTransactionID() {
		return glTransactionID;
	}
	public void setGlTransactionID(Integer glTransactionID) {
		this.glTransactionID = glTransactionID;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getMoTDescription() {
		return moTDescription;
	}
	public void setMoTDescription(String moTDescription) {
		this.moTDescription = moTDescription;
	}
	public String getGlTDescription() {
		return glTDescription;
	}
	public void setGlTDescription(String glTDescription) {
		this.glTDescription = glTDescription;
	}
	
}
