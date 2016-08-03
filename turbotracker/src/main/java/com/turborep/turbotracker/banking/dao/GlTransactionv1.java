package com.turborep.turbotracker.banking.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="glTransaction", catalog="")
public class GlTransactionv1 implements java.io.Serializable{
private static final long serialVersionUID = 1L;
	
	private Integer glTransactionId;
	private Integer coFiscalPeriodId;
	private Integer period;
	private Date pStartDate;
	private Date pEndDate;
	private Integer coFiscalYearId;
	private Date yStartDate;
	private Date yEndDate;
	private String journalId;
	private String journalDesc;
	private Date entrydate;
	private Integer coAccountId;
	private String coAccountNumber;
	private BigDecimal amount;
	private Integer fyear;
	
	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="glTransactionId", unique=true, nullable=false)
	public Integer getGlTransactionId() {
		return glTransactionId;
	}
	public void setGlTransactionId(Integer glTransactionId) {
		this.glTransactionId = glTransactionId;
	}
	public Integer getCoFiscalPeriodId() {
		return coFiscalPeriodId;
	}
	public void setCoFiscalPeriodId(Integer coFiscalPeriodId) {
		this.coFiscalPeriodId = coFiscalPeriodId;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Date getpStartDate() {
		return pStartDate;
	}
	public void setpStartDate(Date pStartDate) {
		this.pStartDate = pStartDate;
	}
	public Date getpEndDate() {
		return pEndDate;
	}
	public void setpEndDate(Date pEndDate) {
		this.pEndDate = pEndDate;
	}
	public Integer getCoFiscalYearId() {
		return coFiscalYearId;
	}
	public void setCoFiscalYearId(Integer coFiscalYearId) {
		this.coFiscalYearId = coFiscalYearId;
	}
	public Date getyStartDate() {
		return yStartDate;
	}
	public void setyStartDate(Date yStartDate) {
		this.yStartDate = yStartDate;
	}
	public Date getyEndDate() {
		return yEndDate;
	}
	public void setyEndDate(Date yEndDate) {
		this.yEndDate = yEndDate;
	}
	public String getJournalId() {
		return journalId;
	}
	public void setJournalId(String journalId) {
		this.journalId = journalId;
	}
	public String getJournalDesc() {
		return journalDesc;
	}
	public void setJournalDesc(String journalDesc) {
		this.journalDesc = journalDesc;
	}
	public Date getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
	}
	public Integer getCoAccountId() {
		return coAccountId;
	}
	public void setCoAccountId(Integer coAccountId) {
		this.coAccountId = coAccountId;
	}
	public String getCoAccountNumber() {
		return coAccountNumber;
	}
	public void setCoAccountNumber(String coAccountNumber) {
		this.coAccountNumber = coAccountNumber;
	}
	@Column(name = "Amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getFyear() {
		return fyear;
	}
	public void setFyear(Integer fyear) {
		this.fyear = fyear;
	}
	
	

}
