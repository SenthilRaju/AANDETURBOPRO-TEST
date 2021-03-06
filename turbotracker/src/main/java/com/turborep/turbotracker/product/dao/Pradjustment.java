package com.turborep.turbotracker.product.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Pradjustment generated by hbm2java
 */
@Entity
@Table(name = "pradjustment", catalog = "")
public class Pradjustment implements java.io.Serializable {

	private Integer prAdjustmentId;
	private Integer prMasterId;

	public Pradjustment() {
	}

	public Pradjustment(Integer prMasterId) {
		this.prMasterId = prMasterId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "prAdjustmentID", unique = true, nullable = false)
	public Integer getPrAdjustmentId() {
		return this.prAdjustmentId;
	}

	public void setPrAdjustmentId(Integer prAdjustmentId) {
		this.prAdjustmentId = prAdjustmentId;
	}

	@Column(name = "prMasterID")
	public Integer getPrMasterId() {
		return this.prMasterId;
	}

	public void setPrMasterId(Integer prMasterId) {
		this.prMasterId = prMasterId;
	}

}
