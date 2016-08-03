package com.turborep.turbotracker.finance.dao;

import java.math.BigDecimal;
import java.util.Date;

public class GlTransactionTest {

	private Integer glTransactionId;
	
	private String coAccountNumber;
	private String coAccountDesc;
	private String transactionDesc;
	
	private String reference;
	private BigDecimal debit;
	private BigDecimal credit;
	
	
	
	public Integer getGlTransactionId() {
		return glTransactionId;
	}
	public void setGlTransactionId(Integer glTransactionId) {
		this.glTransactionId = glTransactionId;
	}

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
	public String getTransactionDesc() {
		return transactionDesc;
	}
	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
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
	
	
	
	

}
