package com.turborep.turbotracker.banking.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="glTransaction", catalog="")
public class GlTransaction implements java.io.Serializable{
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
	
	private String poNumber;
	private String transactionDesc;
	private BigDecimal debit;
	private BigDecimal credit;
	private String fyear;
	private String coAccountDesc;
	private BigDecimal bankOpeningBalance;
	private BigDecimal bankClosingBalance;
	
	private Integer oldcoAccountId;
	private BigDecimal olddebit;
	private BigDecimal oldcredit;
	private String oldcoAccountNumber;
	private Integer status;
	private Integer hiddenstatus;
	
	private Date transactionDate;
	private String enteredBy;
	private String jereference;
	
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

	public String getFyear() {
		return fyear;
	}
	public void setFyear(String fyear) {
		this.fyear = fyear;
	}
	
	@Column(name="reference")
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getTransactionDesc() {
		return transactionDesc;
	}
	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}
	public BigDecimal getDebit() {
		return debit;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Transient
	public String getCoAccountDesc() {
		return coAccountDesc;
	}
	
	public void setCoAccountDesc(String coAccountDesc) {
		this.coAccountDesc = coAccountDesc;
	}
	
	@Transient
	public BigDecimal getBankOpeningBalance() {
		return bankOpeningBalance;
	}
	
	public void setBankOpeningBalance(BigDecimal bankOpeningBalance) {
		this.bankOpeningBalance = bankOpeningBalance;
	}
	
	@Transient
	public BigDecimal getBankClosingBalance() {
		return bankClosingBalance;
	}
	
	public void setBankClosingBalance(BigDecimal bankClosingBalance) {
		this.bankClosingBalance = bankClosingBalance;
	}
	
	@Transient
	public Integer getOldcoAccountId() {
		return oldcoAccountId;
	}
	public void setOldcoAccountId(Integer oldcoAccountId) {
		this.oldcoAccountId = oldcoAccountId;
	}
	@Transient
	public BigDecimal getOlddebit() {
		return olddebit;
	}
	public void setOlddebit(BigDecimal olddebit) {
		this.olddebit = olddebit;
	}
	@Transient
	public BigDecimal getOldcredit() {
		return oldcredit;
	}
	public void setOldcredit(BigDecimal oldcredit) {
		this.oldcredit = oldcredit;
	}
	@Transient
	public String getOldcoAccountNumber() {
		return oldcoAccountNumber;
	}
	public void setOldcoAccountNumber(String oldcoAccountNumber) {
		this.oldcoAccountNumber = oldcoAccountNumber;
	}
	@Transient
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Transient
	public Integer getHiddenstatus() {
		return hiddenstatus;
	}
	public void setHiddenstatus(Integer hiddenstatus) {
		this.hiddenstatus = hiddenstatus;
	}
	
	
	@Column(name="transactionDate")
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	@Column(name="enteredBy")
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	
	@Transient
	public String getJereference() {
		return jereference;
	}
	public void setJereference(String jereference) {
		this.jereference = jereference;
	}
	
	
	

}
