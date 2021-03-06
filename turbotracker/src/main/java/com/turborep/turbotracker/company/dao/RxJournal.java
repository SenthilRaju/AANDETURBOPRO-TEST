package com.turborep.turbotracker.company.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Rxjournal generated by hbm2java
 */
@Entity
@Table(name = "rxJournal", catalog = "")
public class RxJournal implements java.io.Serializable {

	private Integer rxJournalId;
	private Integer rxMasterId;
	private Integer rxJournalTypeId;
	private Integer userLoginId;
	private Date entryDate;
	private String entryMemo;
	private String name;
	private String entryDateString;

	public RxJournal() {
	}

	public RxJournal(Integer rxMasterId, Integer rxJournalTypeId,
			Integer userLoginId, Date entryDate, String entryMemo) {
		this.rxMasterId = rxMasterId;
		this.rxJournalTypeId = rxJournalTypeId;
		this.userLoginId = userLoginId;
		this.entryDate = entryDate;
		this.entryMemo = entryMemo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rxJournalID", unique = true, nullable = false)
	public Integer getRxJournalId() {
		return this.rxJournalId;
	}

	public void setRxJournalId(Integer rxJournalId) {
		this.rxJournalId = rxJournalId;
	}

	@Column(name = "rxMasterID")
	public Integer getRxMasterId() {
		return this.rxMasterId;
	}

	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}

	@Column(name = "rxJournalTypeID")
	public Integer getRxJournalTypeId() {
		return this.rxJournalTypeId;
	}

	public void setRxJournalTypeId(Integer rxJournalTypeId) {
		this.rxJournalTypeId = rxJournalTypeId;
	}

	@Column(name = "UserLoginID")
	public Integer getUserLoginId() {
		return this.userLoginId;
	}

	public void setUserLoginId(Integer userLoginId) {
		this.userLoginId = userLoginId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EntryDate", length = 0)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "EntryMemo")
	public String getEntryMemo() {
		return this.entryMemo;
	}

	public void setEntryMemo(String entryMemo) {
		this.entryMemo = entryMemo;
	}

	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Transient
	public String getEntryDateString() {
		return entryDateString;
	}

	public void setEntryDateString(String entryDateString) {
		this.entryDateString = entryDateString;
	}

}
