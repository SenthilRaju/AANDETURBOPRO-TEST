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
 * Eptranliability generated by hbm2java
 */
@Entity
@Table(name = "eptranliability", catalog = "")
public class Eptranliability implements java.io.Serializable {

	private Integer epTranLiabilityId;
	private Integer epTransactionId;
	private Integer epCoLiabilityId;
	private BigDecimal liabilityAmount;
	private Boolean override;

	public Eptranliability() {
	}

	public Eptranliability(Integer epTransactionId, Integer epCoLiabilityId,
			BigDecimal liabilityAmount, Boolean override) {
		this.epTransactionId = epTransactionId;
		this.epCoLiabilityId = epCoLiabilityId;
		this.liabilityAmount = liabilityAmount;
		this.override = override;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "epTranLiabilityID", unique = true, nullable = false)
	public Integer getEpTranLiabilityId() {
		return this.epTranLiabilityId;
	}

	public void setEpTranLiabilityId(Integer epTranLiabilityId) {
		this.epTranLiabilityId = epTranLiabilityId;
	}

	@Column(name = "epTransactionID")
	public Integer getEpTransactionId() {
		return this.epTransactionId;
	}

	public void setEpTransactionId(Integer epTransactionId) {
		this.epTransactionId = epTransactionId;
	}

	@Column(name = "epCoLiabilityID")
	public Integer getEpCoLiabilityId() {
		return this.epCoLiabilityId;
	}

	public void setEpCoLiabilityId(Integer epCoLiabilityId) {
		this.epCoLiabilityId = epCoLiabilityId;
	}

	@Column(name = "LiabilityAmount", scale = 4)
	public BigDecimal getLiabilityAmount() {
		return this.liabilityAmount;
	}

	public void setLiabilityAmount(BigDecimal liabilityAmount) {
		this.liabilityAmount = liabilityAmount;
	}

	@Column(name = "Override")
	public Boolean getOverride() {
		return this.override;
	}

	public void setOverride(Boolean override) {
		this.override = override;
	}

}