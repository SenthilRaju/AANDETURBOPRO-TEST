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
@Table(name="writecheckDetails", catalog="")
public class WritecheckDetails implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WritecheckDetails() {
		
	}
	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="wcDetailsID", unique=true, nullable=false)
	private Integer wcDetailsID;
	@Column(name="moAccountID")
	private Integer moAccountID;
	@Column(name="moTransactionId")
	private Integer moTransactionId;
	@Column(name="moTransactionDate")
	private Date moTransactionDate;
	@Column(name="moTransAmount")
	private BigDecimal moTransAmount;
	@Column(name="moLinkappliedAmount")
	private BigDecimal moLinkappliedAmount;
	@Column(name="moLinkdiscount")
	private BigDecimal moLinkdiscount;
	@Column(name="checkno")
	private String checkno;
	@Column(name="userID")
	private Integer userID;
	@Column(name="rxMasterID")
	private Integer rxMasterID;
	@Column(name="rxAddressID")
	private Integer rxAddressID;
	@Column(name="veBillID")
	private Integer veBillID;
	@Column(name="veBillDate")
	private Date veBillDate;
	@Column(name="invoiceNumber")
	private String invoiceNumber;
	@Column(name="poNumber")
	private String poNumber;
	@Column(name="veBillAmount")
	private BigDecimal veBillAmount;
	@Column(name="veBillAppliedAmt")
	private BigDecimal veBillAppliedAmt;
	@Column(name="veBillBalance")
	private BigDecimal veBillBalance;
	@Column(name="voidStatus")
	private Integer voidStatus;
	

	public Integer getWcDetailsID() {
		return wcDetailsID;
	}
	public void setWcDetailsID(Integer wcDetailsID) {
		this.wcDetailsID = wcDetailsID;
	}
	public Integer getMoAccountID() {
		return moAccountID;
	}
	public void setMoAccountID(Integer moAccountID) {
		this.moAccountID = moAccountID;
	}
	public Integer getMoTransactionId() {
		return moTransactionId;
	}
	public void setMoTransactionId(Integer moTransactionId) {
		this.moTransactionId = moTransactionId;
	}
	public Date getMoTransactionDate() {
		return moTransactionDate;
	}
	public void setMoTransactionDate(Date moTransactionDate) {
		this.moTransactionDate = moTransactionDate;
	}
	public BigDecimal getMoTransAmount() {
		return moTransAmount;
	}
	public void setMoTransAmount(BigDecimal moTransAmount) {
		this.moTransAmount = moTransAmount;
	}
	public BigDecimal getMoLinkappliedAmount() {
		return moLinkappliedAmount;
	}
	public void setMoLinkappliedAmount(BigDecimal moLinkappliedAmount) {
		this.moLinkappliedAmount = moLinkappliedAmount;
	}
	public BigDecimal getMoLinkdiscount() {
		return moLinkdiscount;
	}
	public void setMoLinkdiscount(BigDecimal moLinkdiscount) {
		this.moLinkdiscount = moLinkdiscount;
	}

	public String getCheckno() {
		return checkno;
	}
	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getRxMasterID() {
		return rxMasterID;
	}
	public void setRxMasterID(Integer rxMasterID) {
		this.rxMasterID = rxMasterID;
	}
	public Integer getRxAddressID() {
		return rxAddressID;
	}
	public void setRxAddressID(Integer rxAddressID) {
		this.rxAddressID = rxAddressID;
	}
	public Integer getVeBillID() {
		return veBillID;
	}
	public void setVeBillID(Integer veBillID) {
		this.veBillID = veBillID;
	}
	public Date getVeBillDate() {
		return veBillDate;
	}
	public void setVeBillDate(Date veBillDate) {
		this.veBillDate = veBillDate;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public BigDecimal getVeBillAmount() {
		return veBillAmount;
	}
	public void setVeBillAmount(BigDecimal veBillAmount) {
		this.veBillAmount = veBillAmount;
	}
	public BigDecimal getVeBillAppliedAmt() {
		return veBillAppliedAmt;
	}
	public void setVeBillAppliedAmt(BigDecimal veBillAppliedAmt) {
		this.veBillAppliedAmt = veBillAppliedAmt;
	}
	public BigDecimal getVeBillBalance() {
		return veBillBalance;
	}
	public void setVeBillBalance(BigDecimal veBillBalance) {
		this.veBillBalance = veBillBalance;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getVoidStatus() {
		return voidStatus;
	}
	public void setVoidStatus(Integer voidStatus) {
		this.voidStatus = voidStatus;
	}
	
}
