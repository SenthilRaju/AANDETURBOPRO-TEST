package com.turborep.turbotracker.product.dao;

import java.io.Serializable;
import java.util.Date;

public class WarehouseTransferBean implements Serializable{
	
	private Integer prTransferId;
	private Date transferDate;
	private String from;
	private String to;
	private Integer transactionNumber;
	private String desc;
	private Date receivedDate;
	public Integer getPrTransferId() {
		return prTransferId;
	}
	public void setPrTransferId(Integer prTransferId) {
		this.prTransferId = prTransferId;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Integer getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	

}
