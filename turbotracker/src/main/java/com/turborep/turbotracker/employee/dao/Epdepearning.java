package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Epdepearning generated by hbm2java
 */
@Entity
@Table(name = "epdepearning", catalog = "")
public class Epdepearning implements java.io.Serializable {

	private Integer epDepEarningId;
	private Integer epCoDepartmentId;
	private Integer epCoEarningId;
	private Integer coAccountId;

	public Epdepearning() {
	}

	public Epdepearning(Integer epCoDepartmentId, Integer epCoEarningId,
			Integer coAccountId) {
		this.epCoDepartmentId = epCoDepartmentId;
		this.epCoEarningId = epCoEarningId;
		this.coAccountId = coAccountId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "epDepEarningID", unique = true, nullable = false)
	public Integer getEpDepEarningId() {
		return this.epDepEarningId;
	}

	public void setEpDepEarningId(Integer epDepEarningId) {
		this.epDepEarningId = epDepEarningId;
	}

	@Column(name = "epCoDepartmentID")
	public Integer getEpCoDepartmentId() {
		return this.epCoDepartmentId;
	}

	public void setEpCoDepartmentId(Integer epCoDepartmentId) {
		this.epCoDepartmentId = epCoDepartmentId;
	}

	@Column(name = "epCoEarningID")
	public Integer getEpCoEarningId() {
		return this.epCoEarningId;
	}

	public void setEpCoEarningId(Integer epCoEarningId) {
		this.epCoEarningId = epCoEarningId;
	}

	@Column(name = "coAccountID")
	public Integer getCoAccountId() {
		return this.coAccountId;
	}

	public void setCoAccountId(Integer coAccountId) {
		this.coAccountId = coAccountId;
	}

}
