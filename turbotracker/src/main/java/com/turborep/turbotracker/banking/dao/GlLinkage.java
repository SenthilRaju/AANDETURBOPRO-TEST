package com.turborep.turbotracker.banking.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="glLinkage", catalog="")
public class GlLinkage implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="glLinkageId", unique=true, nullable=false)
	private Integer glLinkageId;
	private Date entryDate;
	private Integer coLedgerSourceId;
	private Integer glTransactionId;
	private Integer veBillID;
	private Integer status;
	
	
	
	public Integer getGlLinkageId() {
		return glLinkageId;
	}
	public void setGlLinkageId(Integer glLinkageId) {
		this.glLinkageId = glLinkageId;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Integer getCoLedgerSourceId() {
		return coLedgerSourceId;
	}
	public void setCoLedgerSourceId(Integer coLedgerSourceId) {
		this.coLedgerSourceId = coLedgerSourceId;
	}
	public Integer getGlTransactionId() {
		return glTransactionId;
	}
	public void setGlTransactionId(Integer glTransactionId) {
		this.glTransactionId = glTransactionId;
	}
	public Integer getVeBillID() {
		return veBillID;
	}
	public void setVeBillID(Integer veBillID) {
		this.veBillID = veBillID;
	}
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
