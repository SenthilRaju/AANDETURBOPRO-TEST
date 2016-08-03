package com.turborep.turbotracker.product.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Prorderpoint generated by hbm2java
 */
@Entity
@Table(name = "prOrderPoint", catalog = "")
public class Prorderpoint implements java.io.Serializable {

	private Integer prOrderPointId;
	private Integer prMasterId;
	private Integer prWarehouseId;
	private BigDecimal inventoryOrderPoint;
	private BigDecimal inventoryOrderQuantity;

	public Prorderpoint() {
	}

	public Prorderpoint(Integer prMasterId, Integer prWarehouseId,
			BigDecimal inventoryOrderPoint, BigDecimal inventoryOrderQuantity) {
		this.prMasterId = prMasterId;
		this.prWarehouseId = prWarehouseId;
		this.inventoryOrderPoint = inventoryOrderPoint;
		this.inventoryOrderQuantity = inventoryOrderQuantity;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "prOrderPointID", unique = true, nullable = false)
	public Integer getPrOrderPointId() {
		return this.prOrderPointId;
	}

	public void setPrOrderPointId(Integer prOrderPointId) {
		this.prOrderPointId = prOrderPointId;
	}

	@Column(name = "prMasterID")
	public Integer getPrMasterId() {
		return this.prMasterId;
	}

	public void setPrMasterId(Integer prMasterId) {
		this.prMasterId = prMasterId;
	}

	@Column(name = "prWarehouseID")
	public Integer getPrWarehouseId() {
		return this.prWarehouseId;
	}

	public void setPrWarehouseId(Integer prWarehouseId) {
		this.prWarehouseId = prWarehouseId;
	}

	@Column(name = "InventoryOrderPoint", scale = 4)
	public BigDecimal getInventoryOrderPoint() {
		return this.inventoryOrderPoint;
	}

	public void setInventoryOrderPoint(BigDecimal inventoryOrderPoint) {
		this.inventoryOrderPoint = inventoryOrderPoint;
	}

	@Column(name = "InventoryOrderQuantity", scale = 4)
	public BigDecimal getInventoryOrderQuantity() {
		return this.inventoryOrderQuantity;
	}

	public void setInventoryOrderQuantity(BigDecimal inventoryOrderQuantity) {
		this.inventoryOrderQuantity = inventoryOrderQuantity;
	}

}
