package com.turborep.turbotracker.product.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Prtransferdetail generated by hbm2java
 */
@Entity
@Table(name = "prTransferDetail", catalog = "")
public class Prtransferdetail implements java.io.Serializable {

	private Integer prTransferDetailId;
	private Integer prTransferId;
	private Integer prMasterId;
	private String description;
	private BigDecimal quantityTransfered;
	private BigDecimal inventoryOnHand;
	private String itemCode;
	public Prtransferdetail() {
	}

	public Prtransferdetail(Integer prTransferId, Integer prMasterId,
			String description, BigDecimal quantityTransfered) {
		this.prTransferId = prTransferId;
		this.prMasterId = prMasterId;
		this.description = description;
		this.quantityTransfered = quantityTransfered;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "prTransferDetailID", unique = true, nullable = false)
	public Integer getPrTransferDetailId() {
		return this.prTransferDetailId;
	}

	public void setPrTransferDetailId(Integer prTransferDetailId) {
		this.prTransferDetailId = prTransferDetailId;
	}

	@Column(name = "prTransferID")
	public Integer getPrTransferId() {
		return this.prTransferId;
	}

	public void setPrTransferId(Integer prTransferId) {
		this.prTransferId = prTransferId;
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

	@Column(name = "QuantityTransfered", scale = 4)
	public BigDecimal getQuantityTransfered() {
		return this.quantityTransfered;
	}

	public void setQuantityTransfered(BigDecimal quantityTransfered) {
		this.quantityTransfered = quantityTransfered;
	}
	
	@Transient
	public BigDecimal getInventoryOnHand() {
		return inventoryOnHand;
	}

	public void setInventoryOnHand(BigDecimal inventoryOnHand) {
		this.inventoryOnHand = inventoryOnHand;
	}
	@Transient
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

}
