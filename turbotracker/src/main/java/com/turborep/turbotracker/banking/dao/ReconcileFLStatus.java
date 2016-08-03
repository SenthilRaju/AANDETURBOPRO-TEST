package com.turborep.turbotracker.banking.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reconcileFLStatus", catalog="")
public class ReconcileFLStatus {

	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="finishLaterID", unique=true, nullable=false)
	int finishLaterID;
	@Column(name="moTransactionID")
	int moTransactionID;
	@Column(name="moAccountID")
	int moAccountID;
	@Column(name="tempRecstatus")
	int tempRecstatus;
	@Column(name="userID")
	int userID;
	@Column(name="entryDate")
	Date entryDate;
	
	
	public int getFinishLaterID() {
		return finishLaterID;
	}
	public void setFinishLaterID(int finishLaterID) {
		this.finishLaterID = finishLaterID;
	}
	public int getMoTransactionID() {
		return moTransactionID;
	}
	public void setMoTransactionID(int moTransactionID) {
		this.moTransactionID = moTransactionID;
	}
	public int getMoAccountID() {
		return moAccountID;
	}
	public void setMoAccountID(int moAccountID) {
		this.moAccountID = moAccountID;
	}
	public int getTempRecstatus() {
		return tempRecstatus;
	}
	public void setTempRecstatus(int tempRecstatus) {
		this.tempRecstatus = tempRecstatus;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	
}
