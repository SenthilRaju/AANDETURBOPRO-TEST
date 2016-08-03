package com.turborep.turbotracker.customer.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cushipdetail generated by hbm2java
 */
@Entity
@Table(name = "cushipdetail", catalog = "")
public class Cushipdetail implements java.io.Serializable {

	private Integer cuShipDetailId;
	private Integer cuShipId;
	private Integer cuSodetailId;
	private Integer prMasterId;
	private String description;
	private String note;
	private BigDecimal quantityShipped;

	public Cushipdetail() {
	}

	public Cushipdetail(Integer cuShipId, Integer cuSodetailId,
			Integer prMasterId, String description, String note,
			BigDecimal quantityShipped) {
		this.cuShipId = cuShipId;
		this.cuSodetailId = cuSodetailId;
		this.prMasterId = prMasterId;
		this.description = description;
		this.note = note;
		this.quantityShipped = quantityShipped;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cuShipDetailID", unique = true, nullable = false)
	public Integer getCuShipDetailId() {
		return this.cuShipDetailId;
	}

	public void setCuShipDetailId(Integer cuShipDetailId) {
		this.cuShipDetailId = cuShipDetailId;
	}

	@Column(name = "cuShipID")
	public Integer getCuShipId() {
		return this.cuShipId;
	}

	public void setCuShipId(Integer cuShipId) {
		this.cuShipId = cuShipId;
	}

	@Column(name = "cuSODetailID")
	public Integer getCuSodetailId() {
		return this.cuSodetailId;
	}

	public void setCuSodetailId(Integer cuSodetailId) {
		this.cuSodetailId = cuSodetailId;
	}

	@Column(name = "prMasterID")
	public Integer getPrMasterId() {
		return this.prMasterId;
	}

	public void setPrMasterId(Integer prMasterId) {
		this.prMasterId = prMasterId;
	}

	@Column(name = "Description", length = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "Note")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "QuantityShipped", scale = 4)
	public BigDecimal getQuantityShipped() {
		return this.quantityShipped;
	}

	public void setQuantityShipped(BigDecimal quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

}