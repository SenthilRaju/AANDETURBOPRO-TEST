package com.turborep.turbotracker.job.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
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
 * Jochange generated by hbm2java
 */
@Entity
@Table(name = "joChange", catalog = "")
public class Jochange implements java.io.Serializable {

	private Integer joChangeId;
	private Integer joMasterId;
	private Date changeDate;
	private String customerPonumber;
	private Integer changeById;
	private String changeByName;
	private String changeReason;
	private BigDecimal changeAmount;
	private String changdate;
	private BigDecimal changeCost;
	private String oper;

	public Jochange() {
	}

	public Jochange(Integer joMasterId, Date changeDate,
			String customerPonumber, Integer changeById, String changeByName,
			String changeReason, BigDecimal changeAmount) {
		this.joMasterId = joMasterId;
		this.changeDate = changeDate;
		this.customerPonumber = customerPonumber;
		this.changeById = changeById;
		this.changeByName = changeByName;
		this.changeReason = changeReason;
		this.changeAmount = changeAmount;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "joChangeID", unique = true, nullable = false)
	public Integer getJoChangeId() {
		return this.joChangeId;
	}

	public void setJoChangeId(Integer joChangeId) {
		this.joChangeId = joChangeId;
	}

	@Column(name = "joMasterID")
	public Integer getJoMasterId() {
		return this.joMasterId;
	}

	public void setJoMasterId(Integer joMasterId) {
		this.joMasterId = joMasterId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ChangeDate", length = 0)
	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "CustomerPONumber", length = 20)
	public String getCustomerPonumber() {
		return this.customerPonumber;
	}

	public void setCustomerPonumber(String customerPonumber) {
		this.customerPonumber = customerPonumber;
	}

	@Column(name = "ChangeByID")
	public Integer getChangeById() {
		return this.changeById;
	}

	public void setChangeById(Integer changeById) {
		this.changeById = changeById;
	}

	@Column(name = "ChangeByName", length = 15)
	public String getChangeByName() {
		return this.changeByName;
	}

	public void setChangeByName(String changeByName) {
		this.changeByName = changeByName;
	}

	@Column(name = "ChangeReason")
	public String getChangeReason() {
		return this.changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	@Column(name = "ChangeAmount", scale = 4)
	public BigDecimal getChangeAmount() {
		return this.changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	@Column(name = "ChangeCost", scale = 4)
	public BigDecimal getChangeCost() {
		return changeCost;
	}

	public void setChangeCost(BigDecimal changeCost) {
		this.changeCost = changeCost;
	}
	
	@Transient
	public String getChangdate() {
		return changdate;
	}

	public void setChangdate(String changdate) {
		this.changdate = changdate;
	}

	@Transient
	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

}
