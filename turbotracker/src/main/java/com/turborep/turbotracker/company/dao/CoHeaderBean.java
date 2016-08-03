package com.turborep.turbotracker.company.dao;

import java.util.Date;

public class CoHeaderBean {
	private Integer coLedgerHeaderId;
	private Integer coFiscalPeriodId;
	private Integer coLedgerSourceId;
	private Byte sourceNo;
	private Integer transactionReferenceId;
	private Date postDate;
	private String description;
	private String reference;
	private Boolean posted;
	
	
	public Integer getCoLedgerHeaderId() {
		return coLedgerHeaderId;
	}
	public void setCoLedgerHeaderId(Integer coLedgerHeaderId) {
		this.coLedgerHeaderId = coLedgerHeaderId;
	}
	public Integer getCoFiscalPeriodId() {
		return coFiscalPeriodId;
	}
	public void setCoFiscalPeriodId(Integer coFiscalPeriodId) {
		this.coFiscalPeriodId = coFiscalPeriodId;
	}
	public Integer getCoLedgerSourceId() {
		return coLedgerSourceId;
	}
	public void setCoLedgerSourceId(Integer coLedgerSourceId) {
		this.coLedgerSourceId = coLedgerSourceId;
	}
	public Byte getSourceNo() {
		return sourceNo;
	}
	public void setSourceNo(Byte sourceNo) {
		this.sourceNo = sourceNo;
	}
	public Integer getTransactionReferenceId() {
		return transactionReferenceId;
	}
	public void setTransactionReferenceId(Integer transactionReferenceId) {
		this.transactionReferenceId = transactionReferenceId;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Boolean getPosted() {
		return posted;
	}
	public void setPosted(Boolean posted) {
		this.posted = posted;
	}
	

}
