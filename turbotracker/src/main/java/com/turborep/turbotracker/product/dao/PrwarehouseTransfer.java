package com.turborep.turbotracker.product.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="prWHtransfer", catalog = "")
public class PrwarehouseTransfer implements Serializable{
	private Integer prTransferId;
	private Date transferDate;
	private Integer prFromWarehouseId;
	private Integer prToWarehouseId;
	private Integer transactionNumber;
	private String desc;
	private Date receivedDate;
	private Date estreceiveDate;
	private Integer coAccountID;
	private Integer adjustCogAccountID;
	private String reasonCode;
	
	@Column(name="CreatedById")
	private Integer CreatedById;
	@Column(name="CreatedOn")
	private Date CreatedOn;
	
	private Integer screenno;
	
	
	public PrwarehouseTransfer()
	{
		
	}
	public PrwarehouseTransfer(Integer prTransferId, Date transferDate,
			Integer prFromWarehouseId, Integer prToWarehouseId,
			Integer transactionNumber, String desc, Date receivedDate, Date estreceiveDate) {
		super();
		this.prTransferId = prTransferId;
		this.transferDate = transferDate;
		this.prFromWarehouseId = prFromWarehouseId;
		this.prToWarehouseId = prToWarehouseId;
		this.transactionNumber = transactionNumber;
		this.desc = desc;
		this.receivedDate = receivedDate;
		this.estreceiveDate = estreceiveDate;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="prTransferID",unique=true,nullable=false)
	public Integer getPrTransferId() {
		return prTransferId;
	}
	public void setPrTransferId(Integer prTransferId) {
		this.prTransferId = prTransferId;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TransferDate")
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	@Column(name="prFromWarehouseID")
	public Integer getPrFromWarehouseId() {
		return prFromWarehouseId;
	}
	public void setPrFromWarehouseId(Integer prFromWarehouseId) {
		this.prFromWarehouseId = prFromWarehouseId;
	}
	@Column(name="prToWarehouseID")
	public Integer getPrToWarehouseId() {
		return prToWarehouseId;
	}
	public void setPrToWarehouseId(Integer prToWarehouseId) {
		this.prToWarehouseId = prToWarehouseId;
	}
	@Column(name="TransNumber")
	public Integer getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	@Column(name="Description")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ReceivedDate")
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EstreceiveDate")
	public Date getEstreceiveDate() {
		return estreceiveDate;
	}
	public void setEstreceiveDate(Date estreceiveDate) {
		this.estreceiveDate = estreceiveDate;
	}
	public Integer getCoAccountID() {
		return coAccountID;
	}
	public void setCoAccountID(Integer coAccountID) {
		this.coAccountID = coAccountID;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public Integer getAdjustCogAccountID() {
		return adjustCogAccountID;
	}
	public void setAdjustCogAccountID(Integer adjustCogAccountID) {
		this.adjustCogAccountID = adjustCogAccountID;
	}
	public Integer getCreatedByID() {
		return CreatedById;
	}
	public void setCreatedByID(Integer createdByID) {
		CreatedById = createdByID;
	}
	public Date getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(Date createdOn) {
		CreatedOn = createdOn;
	}
	//If screen no1-Transfer
	//If screen no2-Adjustment
	//If Screen no3-Count
	@Column(name="screenno")
	public Integer getScreenno() {
		return screenno;
	}
	public void setScreenno(Integer screenno) {
		this.screenno = screenno;
	}
	
	

}
