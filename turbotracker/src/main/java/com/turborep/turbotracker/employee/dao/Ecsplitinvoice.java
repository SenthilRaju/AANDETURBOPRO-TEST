package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Ecsplitinvoice generated by hbm2java
 */
@Entity
@Table(name = "ecsplitinvoice", catalog = "")
public class Ecsplitinvoice implements java.io.Serializable {

	private Integer ecSplitJobId;
	private Integer joMasterId;
	private Integer rxMasterId;
	private BigDecimal allocated;

	public Ecsplitinvoice() {
	}

	public Ecsplitinvoice(Integer joMasterId, Integer rxMasterId,
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

}
