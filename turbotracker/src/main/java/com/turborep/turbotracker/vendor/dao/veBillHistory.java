package com.turborep.turbotracker.vendor.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


	@Entity
	@Table(name = "veBillHistory", catalog = "")
	public class veBillHistory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "veBillHistoryID", unique = true, nullable = false)
	private Integer veBillHistoryID;
	
	@Column(name = "vePOID")
	private Integer vePOID;
	
	@Column(name = "veBillID")
	private Integer veBillID;
	
	@Column(name = "vePODetailID")
	private Integer vePODetailID;
	
	@Column(name = "quantityInvoiced")
	private BigDecimal quantityInvoiced = BigDecimal.ZERO;
	
	@Column(name = "invoiceAmount")
	private BigDecimal invoiceAmount= BigDecimal.ZERO;
	
	@Column(name = "entryDate")
	private Date entryDate;
	
	@Column(name = "enteredBy")
	private Date enteredBy;
	
	public Integer getVeBillHistoryID() {
		return veBillHistoryID;
	}
	public void setVeBillHistoryID(Integer veBillHistoryID) {
		this.veBillHistoryID = veBillHistoryID;
	}
	public Integer getVePOID() {
		return vePOID;
	}
	public void setVePOID(Integer vePOID) {
		this.vePOID = vePOID;
	}
	public Integer getVeBillID() {
		return veBillID;
	}
	public void setVeBillID(Integer veBillID) {
		this.veBillID = veBillID;
	}
	public Integer getVePODetailID() {
		return vePODetailID;
	}
	public void setVePODetailID(Integer vePODetailID) {
		this.vePODetailID = vePODetailID;
	}
	public BigDecimal getQuantityInvoiced() {
		return quantityInvoiced;
	}
	public void setQuantityInvoiced(BigDecimal quantityInvoiced) {
		this.quantityInvoiced = quantityInvoiced;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(Date enteredBy) {
		this.enteredBy = enteredBy;
	}

}
