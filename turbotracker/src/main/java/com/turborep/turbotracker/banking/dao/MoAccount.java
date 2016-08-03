/**
 * Copyright (c) 2013 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.banking.dao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Hibernate's entity bean for <b>moAccount</b> entity. <br>
 * 
 * MoAccount generated by hbm2java
 */
@Entity
@Table(name="moAccount", catalog="")
public class MoAccount  implements java.io.Serializable {


	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	private Integer moAccountId;
	private byte inActive;
	private Short accountType;
	private String description;
	private Integer coAccountIDAsset;
	private Integer coAccountIDDeposits; 
	private Integer coAccountIDInterest; 
	private Integer coAccountIDFees; 
	private BigDecimal openBalance = BigDecimal.ZERO;
	private BigDecimal additions = BigDecimal.ZERO;
	private BigDecimal subtractions = BigDecimal.ZERO;
	private BigDecimal undepositedReceipts = BigDecimal.ZERO;
	private BigDecimal unprintedPayables = BigDecimal.ZERO;
	private BigDecimal unprintedPayroll = BigDecimal.ZERO;
	private Integer nextCheckNumber = 1;
	private BigDecimal endingBalance = BigDecimal.ZERO;
	private String company1;
	private String company2;
	private String company3;
	private String company4;
	private String company5;
	private String bank1;
	private String bank2;
	private String bank3;
	private String bank4;
	private String bank5;
	private String checkCode;
	private String routingNumber;
	private String accountNumber;
	private int MoTransactionTypeId;
	private String oper;
	private Boolean logoYN;
	private Integer lineNo;
	
	public MoAccount() {
	}
	
	public MoAccount(byte inActive, Integer nextCheckNumber) {
		this.inActive = inActive;
		this.nextCheckNumber = nextCheckNumber;
	}
	public MoAccount(byte inActive, Short accountType, String description, Integer coAccountIDAsset, Integer coAccountIDDeposits, Integer coAccountIDInterest, Integer coAccountIDFees, BigDecimal openBalance, BigDecimal additions, BigDecimal subtractions, BigDecimal undepositedReceipts, 
						BigDecimal unprintedPayables, BigDecimal unprintedPayroll, Integer nextCheckNumber, BigDecimal endingBalance, String company1, String company2, String company3, 
						String company4, String company5, String bank1, String bank2, String bank3, String bank4, String bank5, String checkCode, String routingNumber, String accountNumber) {
		this.inActive = inActive;
		this.accountType = accountType;
		this.description = description;
		this.coAccountIDAsset = coAccountIDAsset;
		this.coAccountIDDeposits = coAccountIDDeposits;
		this.coAccountIDFees = coAccountIDFees;
		this.coAccountIDInterest = coAccountIDInterest;
		this.openBalance = openBalance;
		this.additions = additions;
		this.subtractions = subtractions;
		this.undepositedReceipts = undepositedReceipts;
		this.unprintedPayables = unprintedPayables;
		this.unprintedPayroll = unprintedPayroll;
		this.nextCheckNumber = nextCheckNumber;
		this.endingBalance = endingBalance;
		this.company1 = company1;
		this.company2 = company2;
		this.company3 = company3;
		this.company4 = company4;
		this.company5 = company5;
		this.bank1 = bank1;
		this.bank2 = bank2;
		this.bank3 = bank3;
		this.bank4 = bank4;
		this.bank5 = bank5;
		this.checkCode = checkCode;
		this.routingNumber = routingNumber;
		this.accountNumber = accountNumber;
	}
   
	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="moAccountID", unique=true, nullable=false)
	public Integer getMoAccountId() {
	    return this.moAccountId;
	}
	
	public void setMoAccountId(Integer moAccountId) {
	    this.moAccountId = moAccountId;
	}
	
	@Column(name="InActive", nullable=false)
	public byte getInActive() {
	    return this.inActive;
	}
	
	public void setInActive(byte inActive) {
	    this.inActive = inActive;
	}
	
	
	@Column(name="AccountType")
	public Short getAccountType() {
	    return this.accountType;
	}
	
	public void setAccountType(Short accountType) {
	    this.accountType = accountType;
	}
	
	
	@Column(name="Description", length=50)
	public String getDescription() {
	    return this.description;
	}
	
	public void setDescription(String description) {
	    this.description = description;
	}
	
	@Column(name="coAccountIDAsset")
	public Integer getCoAccountIDAsset() {
		return coAccountIDAsset;
	}

	public void setCoAccountIDAsset(Integer coAccountIDAsset) {
		this.coAccountIDAsset = coAccountIDAsset;
	}

	@Column(name="coAccountIDDeposits")
	public Integer getCoAccountIDDeposits() {
		return coAccountIDDeposits;
	}

	public void setCoAccountIDDeposits(Integer coAccountIDDeposits) {
		this.coAccountIDDeposits = coAccountIDDeposits;
	}

	@Column(name="coAccountIDInterest")
	public Integer getCoAccountIDInterest() {
		return coAccountIDInterest;
	}

	public void setCoAccountIDInterest(Integer coAccountIDInterest) {
		this.coAccountIDInterest = coAccountIDInterest;
	}

	@Column(name="coAccountIDFees")
	public Integer getCoAccountIDFees() {
		return coAccountIDFees;
	}

	public void setCoAccountIDFees(Integer coAccountIDFees) {
		this.coAccountIDFees = coAccountIDFees;
	}

	@Column(name="OpenBalance", scale=4)
	public BigDecimal getOpenBalance() {
	    return this.openBalance;
	}
	
	public void setOpenBalance(BigDecimal openBalance) {
	    this.openBalance = openBalance;
	}
	
	
	@Column(name="Additions", scale=4)
	public BigDecimal getAdditions() {
	    return this.additions;
	}
	
	public void setAdditions(BigDecimal additions) {
	    this.additions = additions;
	}
	
	
	@Column(name="Subtractions", scale=4)
	public BigDecimal getSubtractions() {
	    return this.subtractions;
	}
	
	public void setSubtractions(BigDecimal subtractions) {
	    this.subtractions = subtractions;
	}
	
	
	@Column(name="UndepositedReceipts", scale=4)
	public BigDecimal getUndepositedReceipts() {
	    return this.undepositedReceipts;
	}
	
	public void setUndepositedReceipts(BigDecimal undepositedReceipts) {
	    this.undepositedReceipts = undepositedReceipts;
	}
	
	
	@Column(name="UnprintedPayables", scale=4)
	public BigDecimal getUnprintedPayables() {
	    return this.unprintedPayables;
	}
	
	public void setUnprintedPayables(BigDecimal unprintedPayables) {
	    this.unprintedPayables = unprintedPayables;
	}
	
	
	@Column(name="UnprintedPayroll", scale=4)
	public BigDecimal getUnprintedPayroll() {
	    return this.unprintedPayroll;
	}
	
	public void setUnprintedPayroll(BigDecimal unprintedPayroll) {
	    this.unprintedPayroll = unprintedPayroll;
	}
	
	
	@Column(name="NextCheckNumber", nullable=false)
	public int getNextCheckNumber() {
	    return this.nextCheckNumber;
	}
	
	public void setNextCheckNumber(int nextCheckNumber) {
	    this.nextCheckNumber = nextCheckNumber;
	}
	
	
	@Column(name="EndingBalance", scale=4)
	public BigDecimal getEndingBalance() {
	    return this.endingBalance;
	}
	
	public void setEndingBalance(BigDecimal endingBalance) {
	    this.endingBalance = endingBalance;
	}
	
	
	@Column(name="Company1", length=50)
	public String getCompany1() {
	    return this.company1;
	}
	
	public void setCompany1(String company1) {
	    this.company1 = company1;
	}
	
	
	@Column(name="Company2", length=50)
	public String getCompany2() {
	    return this.company2;
	}
	
	public void setCompany2(String company2) {
	    this.company2 = company2;
	}
	
	
	@Column(name="Company3", length=50)
	public String getCompany3() {
	    return this.company3;
	}
	
	public void setCompany3(String company3) {
	    this.company3 = company3;
	}
	
	
	@Column(name="Company4", length=50)
	public String getCompany4() {
	    return this.company4;
	}
	
	public void setCompany4(String company4) {
	    this.company4 = company4;
	}
	
	
	@Column(name="Company5", length=50)
	public String getCompany5() {
	    return this.company5;
	}
	
	public void setCompany5(String company5) {
	    this.company5 = company5;
	}
	
	
	@Column(name="Bank1", length=50)
	public String getBank1() {
	    return this.bank1;
	}
	
	public void setBank1(String bank1) {
	    this.bank1 = bank1;
	}
	
	
	@Column(name="Bank2", length=50)
	public String getBank2() {
	    return this.bank2;
	}
	
	public void setBank2(String bank2) {
	    this.bank2 = bank2;
	}
	
	
	@Column(name="Bank3", length=50)
	public String getBank3() {
	    return this.bank3;
	}
	
	public void setBank3(String bank3) {
	    this.bank3 = bank3;
	}
	
	
	@Column(name="Bank4", length=50)
	public String getBank4() {
	    return this.bank4;
	}
	
	public void setBank4(String bank4) {
	    this.bank4 = bank4;
	}
	
	
	@Column(name="Bank5", length=50)
	public String getBank5() {
	    return this.bank5;
	}
	
	public void setBank5(String bank5) {
	    this.bank5 = bank5;
	}
	
	
	@Column(name="CheckCode", length=50)
	public String getCheckCode() {
	    return this.checkCode;
	}
	
	public void setCheckCode(String checkCode) {
	    this.checkCode = checkCode;
	}
	
	
	@Column(name="RoutingNumber", length=50)
	public String getRoutingNumber() {
	    return this.routingNumber;
	}
	
	public void setRoutingNumber(String routingNumber) {
	    this.routingNumber = routingNumber;
	}
	
	
	@Column(name="AccountNumber", length=50)
	public String getAccountNumber() {
	    return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
	    this.accountNumber = accountNumber;
	}

@Transient
	public int getMoTransactionTypeId() {
		return MoTransactionTypeId;
	}


	public void setMoTransactionTypeId(int moTransactionTypeId) {
		MoTransactionTypeId = moTransactionTypeId;
	}

	@Transient
	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	@Column(name="LogoYN")
	public Boolean getLogoYN() {
		return logoYN;
	}
	public void setLogoYN(Boolean logoYN) {
		this.logoYN = logoYN;
	}
	
	@Column(name="LineNo")
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
}


