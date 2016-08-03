package com.turborep.turbotracker.finance.dao;

import java.math.BigDecimal;

public class GlReportingAccounts {
	
	/**
	 * Created by : Leo    Date: 12-09-2014
	 * 
	 * Description: GLTransaction Report Generate.
	 * 
	 * */
	
	private Integer coAccountID;
	private String coAccountNumber;
	private String coAccountDesc;
	private String year;	
	private Integer period;
	private BigDecimal cumulativebalance;
	private BigDecimal periodbalance;
	private BigDecimal ytdBalance;
	
	private String pBalance;
	private String yBalance;
	
	private String journalID;
	private String journalDesc;
	private BigDecimal journalBalance;

	
	private Integer sourceid;
	private String description;
	private String entrydate;
	private String transactionDate;
	private Integer gltransactionid;
	private BigDecimal credit;
	private BigDecimal debit;
	private String balanceDesc;
	
	public String getCoAccountNumber() {
		return coAccountNumber;
	}
	public void setCoAccountNumber(String coAccountNumber) {
		this.coAccountNumber = coAccountNumber;
	}
	public String getCoAccountDesc() {
		return coAccountDesc;
	}
	public void setCoAccountDesc(String coAccountDesc) {
		this.coAccountDesc = coAccountDesc;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public BigDecimal getPeriodbalance() {
		return periodbalance;
	}
	public void setPeriodbalance(BigDecimal periodbalance) {
		this.periodbalance = periodbalance;
	}
	public BigDecimal getCumulativebalance() {
		return cumulativebalance;
	}
	public void setCumulativebalance(BigDecimal cumulativebalance) {
		this.cumulativebalance = cumulativebalance;
	}
	public String getJournalID() {
		return journalID;
	}
	public void setJournalID(String journalID) {
		this.journalID = journalID;
	}
	public String getJournalDesc() {
		return journalDesc;
	}
	public void setJournalDesc(String journalDesc) {
		this.journalDesc = journalDesc;
	}
	public BigDecimal getJournalBalance() {
		return journalBalance;
	}
	public void setJournalBalance(BigDecimal journalBalance) {
		this.journalBalance = journalBalance;
	}
	public Integer getSourceid() {
		return sourceid;
	}
	public void setSourceid(Integer sourceid) {
		this.sourceid = sourceid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}
	public Integer getGltransactionid() {
		return gltransactionid;
	}
	public void setGltransactionid(Integer gltransactionid) {
		this.gltransactionid = gltransactionid;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public BigDecimal getDebit() {
		return debit;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	public Integer getCoAccountID() {
		return coAccountID;
	}
	public void setCoAccountID(Integer coAccountID) {
		this.coAccountID = coAccountID;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public BigDecimal getYtdBalance() {
		return ytdBalance;
	}
	public void setYtdBalance(BigDecimal ytdBalance) {
		this.ytdBalance = ytdBalance;
	}
	public String getBalanceDesc() {
		return balanceDesc;
	}
	public void setBalanceDesc(String balanceDesc) {
		this.balanceDesc = balanceDesc;
	}
	public String getpBalance() {
		return pBalance;
	}
	public void setpBalance(String pBalance) {
		this.pBalance = pBalance;
	}
	public String getyBalance() {
		return yBalance;
	}
	public void setyBalance(String yBalance) {
		this.yBalance = yBalance;
	}
	
	

}
