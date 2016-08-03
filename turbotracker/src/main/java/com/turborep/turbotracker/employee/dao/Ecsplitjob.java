package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * Ecsplitjob generated by hbm2java
 */
@Entity
@Table(name = "ecSplitJob", catalog = "")
public class Ecsplitjob implements java.io.Serializable {

	private Integer ecSplitJobId;
	private Integer joMasterId;
	private Integer rxMasterId;
	private BigDecimal allocated;
	private Integer ecSplitCodeID;
	private String rep;
	private String splittype;
	private Integer joReleaseID;
	private Integer createdByID;
	private Integer changedByID;
	private Date createdOn;
	private Date changedOn;
	public Ecsplitjob() {
	}

	public Ecsplitjob(Integer joMasterId, Integer rxMasterId,
			BigDecimal allocated) {
		this.joMasterId = joMasterId;
		this.rxMasterId = rxMasterId;
		this.allocated = allocated;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ecSplitJobID", unique = true, nullable = false)
	public Integer getEcSplitJobId() {
		return this.ecSplitJobId;
	}

	public void setEcSplitJobId(Integer ecSplitJobId) {
		this.ecSplitJobId = ecSplitJobId;
	}

	@Column(name = "joMasterID")
	public Integer getJoMasterId() {
		return this.joMasterId;
	}

	public void setJoMasterId(Integer joMasterId) {
		this.joMasterId = joMasterId;
	}

	@Column(name = "rxMasterID")
	public Integer getRxMasterId() {
		return this.rxMasterId;
	}

	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}

	@Column(name = "Allocated", scale = 4)
	public BigDecimal getAllocated() {
		return this.allocated;
	}

	public void setAllocated(BigDecimal allocated) {
		this.allocated = allocated;
	}
    
	@Column(name="ecSplitCodeID")
	public Integer getEcSplitCodeID() {
		return ecSplitCodeID;
	}

	public void setEcSplitCodeID(Integer ecSplitCodeID) {
		this.ecSplitCodeID = ecSplitCodeID;
	}
   @Transient
	public String getRep() {
		return rep;
	}

	public void setRep(String rep) {
		this.rep = rep;
	}

	@Column(name="splitType")
	public String getSplittype() {
		return splittype;
	}

	public void setSplittype(String splittype) {
		this.splittype = splittype;
	}
	@Column(name="joReleaseID")
	public Integer getJoReleaseID() {
		return joReleaseID;
	}

	public void setJoReleaseID(Integer joReleaseID) {
		this.joReleaseID = joReleaseID;
	}

	@Column(name = "CreatedByID", length = 0)
	public Integer getCreatedByID() {
		return createdByID;
	}

	public void setCreatedByID(Integer createdByID) {
		this.createdByID = createdByID;
	}

	@Column(name = "ChangedByID", length = 0)
	public Integer getChangedByID() {
		return changedByID;
	}

	public void setChangedByID(Integer changedByID) {
		this.changedByID = changedByID;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ChangedOn", length = 0)
	public Date getChangedOn() {
		return changedOn;
	}

	public void setChangedOn(Date changedOn) {
		this.changedOn = changedOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", length = 0)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
}