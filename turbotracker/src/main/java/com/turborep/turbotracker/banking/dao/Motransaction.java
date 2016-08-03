/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.banking.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.math.BigInteger;
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
 * Motransaction generated by hbm2java
 */
@Entity
@Table(name = "moTransaction", catalog = "")
public class Motransaction implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer moTransactionId;
	private Integer rxMasterId;
	private Integer rxAddressId;
	private Integer coAccountId;
	private Integer moAccountId;
	private Date transactionDate;
	private Short moTransactionTypeId;
	private Short checkType;
	private String reference;
	private String description;
	private Byte void_;
	private Byte reconciled;
	private Byte tempRec;
	private Byte printed;
	private BigDecimal amount;
	private BigDecimal theAmount;
	private Byte directDeposit;
	private String transDate;
	private BigDecimal withDrawel;
	private Short moTypeId;
	private BigDecimal balance;
	private String displaydiscription;
	private BigDecimal currentbalance;
	private BigDecimal resultingbalance;
	private boolean status;
	private String futureorcurrent;
	private String memo;
	private BigInteger lastCheckNo;
	private boolean pageflagVoid;
	private String uniquechkRef;
	
	public Motransaction() {
	}

	public Motransaction(Date transactionDate, Byte void_,
			Byte reconciled, Byte tempRec, Byte printed,
			BigDecimal amount, Byte directDeposit) {
		this.transactionDate = transactionDate;
		this.void_ = void_;
		this.reconciled = reconciled;
		this.tempRec = tempRec;
		this.printed = printed;
		this.amount = amount;
		this.directDeposit = directDeposit;
	}

	public Motransaction(Integer rxMasterId, Integer rxAddressId,
			Integer coAccountId, Integer moAccountId, Date transactionDate,
			Short moTransactionTypeId, Short checkType, String reference,
			String description, Byte void_, Byte reconciled,
			Byte tempRec, Byte printed, BigDecimal amount, Byte directDeposit,String memo) {
		this.rxMasterId = rxMasterId;
		this.rxAddressId = rxAddressId;
		this.coAccountId = coAccountId;
		this.moAccountId = moAccountId;
		this.transactionDate = transactionDate;
		this.moTransactionTypeId = moTransactionTypeId;
		this.checkType = checkType;
		this.reference = reference;
		this.description = description;
		this.void_ = void_;
		this.reconciled = reconciled;
		this.tempRec = tempRec;
		this.printed = printed;
		this.amount = amount;
		this.directDeposit = directDeposit;
		this.memo = memo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "moTransactionID", unique = true, nullable = false)
	public Integer getMoTransactionId() {
		return this.moTransactionId;
	}

	public void setMoTransactionId(Integer moTransactionId) {
		this.moTransactionId = moTransactionId;
	}

	@Column(name = "rxMasterID")
	public Integer getRxMasterId() {
		return this.rxMasterId;
	}

	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}

	@Column(name = "rxAddressID")
	public Integer getRxAddressId() {
		return this.rxAddressId;
	}

	public void setRxAddressId(Integer rxAddressId) {
		this.rxAddressId = rxAddressId;
	}

	@Column(name = "coAccountID")
	public Integer getCoAccountId() {
		return this.coAccountId;
	}

	public void setCoAccountId(Integer coAccountId) {
		this.coAccountId = coAccountId;
	}

	@Column(name = "moAccountID")
	public Integer getMoAccountId() {
		return this.moAccountId;
	}

	public void setMoAccountId(Integer moAccountId) {
		this.moAccountId = moAccountId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TransactionDate", nullable = false, length = 0)
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Column(name = "moTransactionTypeID")
	public Short getMoTransactionTypeId() {
		return this.moTransactionTypeId;
	}

	public void setMoTransactionTypeId(Short moTransactionTypeId) {
		this.moTransactionTypeId = moTransactionTypeId;
	}

	@Column(name = "CheckType")
	public Short getCheckType() {
		return this.checkType;
	}

	public void setCheckType(Short checkType) {
		this.checkType = checkType;
	}

	@Column(name = "Reference", length = 12)
	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Column(name = "Description", length = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "Void", nullable = false)
	public Byte getVoid_() {
		return this.void_;
	}

	public void setVoid_(Byte void_) {
		this.void_ = void_;
	}

	@Column(name = "Reconciled", nullable = false)
	public Byte getReconciled() {
		return this.reconciled;
	}

	public void setReconciled(Byte reconciled) {
		this.reconciled = reconciled;
	}

	@Column(name = "TempRec", nullable = false)
	public Byte getTempRec() {
		return this.tempRec;
	}

	public void setTempRec(Byte tempRec) {
		this.tempRec = tempRec;
	}

	@Column(name = "Printed", nullable = false)
	public Byte getPrinted() {
		return this.printed;
	}

	public void setPrinted(Byte printed) {
		this.printed = printed;
	}

	@Column(name = "Amount", nullable = false, scale = 4)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "DirectDeposit", nullable = false)
	public Byte getDirectDeposit() {
		return directDeposit;
	}

	public void setDirectDeposit(Byte directDeposit) {
		this.directDeposit = directDeposit;
	}

	@Transient
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	@Transient
	public BigDecimal getWithDrawel() {
		return withDrawel;
	}

	public void setWithDrawel(BigDecimal withDrawel) {
		this.withDrawel = withDrawel;
	}
	
	
	public BigDecimal getBalance(){
		return balance;
	}
	
	public void setBalance(BigDecimal balance){
		this.balance = balance;
	}

	@Transient
	public Short getMoTypeId() {
		return moTypeId;
	}

	public void setMoTypeId(Short moTypeId) {
		this.moTypeId = moTypeId;
	}
	@Transient
	public String getDisplaydiscription() {
		return displaydiscription;
	}

	public void setDisplaydiscription(String displaydiscription) {
		this.displaydiscription = displaydiscription;
	}
	@Transient
	public BigDecimal getCurrentbalance() {
		return currentbalance;
	}

	public void setCurrentbalance(BigDecimal currentbalance) {
		this.currentbalance = currentbalance;
	}
	@Transient
	public BigDecimal getResultingbalance() {
		return resultingbalance;
	}

	public void setResultingbalance(BigDecimal resultingbalance) {
		this.resultingbalance = resultingbalance;
	}
	@Column(name = "status")
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Transient
	public String getFutureorcurrent() {
		return futureorcurrent;
	}

	public void setFutureorcurrent(String futureorcurrent) {
		this.futureorcurrent = futureorcurrent;
	}
	
	@Transient
	public BigInteger getLastCheckNo() {
		return lastCheckNo;
	}

	public void setLastCheckNo(BigInteger lastCheckNo) {
		this.lastCheckNo = lastCheckNo;
	}

	@Transient
	public BigDecimal getTheAmount() {
		return theAmount;
	}

	public void setTheAmount(BigDecimal theAmount) {
		this.theAmount = theAmount;
	}

	public boolean isPageflagVoid() {
		return pageflagVoid;
	}

	public void setPageflagVoid(boolean pageflagVoid) {
		this.pageflagVoid = pageflagVoid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUniquechkRef() {
		return uniquechkRef;
	}

	public void setUniquechkRef(String uniquechkRef) {
		this.uniquechkRef = uniquechkRef;
	}
	
	
	
	
}