package com.turborep.turbotracker.customer.dao;

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
 * Cusodetail generated by hbm2java
 */
@Entity
@Table(name = "cuSODetail", catalog = "")
public class Cusodetail implements java.io.Serializable {

	private Integer cuSodetailId;
	private Integer cuSoid;
	private Integer prMasterId;
	private String description;
	private String note;
	private BigDecimal quantityOrdered;
	private BigDecimal quantityReceived;
	private BigDecimal quantityBilled;
	private BigDecimal unitCost;
	private BigDecimal unitPrice;
	private BigDecimal priceMultiplier;
	private Byte taxable;
	private boolean hasSingleItemTaxAmount;
	private Integer joSchedDetailId;
	private String itemCode;
	private String noteImage;
	private BigDecimal whseCost;
	private Integer position;

	private BigDecimal TaxTotal;

	private Integer userID;
	private String userName;
	private BigDecimal TaxableSum;
	
	public Cusodetail() {
	}

	public Cusodetail(boolean hasSingleItemTaxAmount) {
		this.hasSingleItemTaxAmount = hasSingleItemTaxAmount;
	}

	public Cusodetail(Integer cuSoid, Integer prMasterId, String description,
			String note, BigDecimal quantityOrdered,
			BigDecimal quantityReceived, BigDecimal quantityBilled,
			BigDecimal unitCost, BigDecimal unitPrice,
			BigDecimal priceMultiplier, Byte taxable,
			boolean hasSingleItemTaxAmount, Integer joSchedDetailId) {
		this.cuSoid = cuSoid;
		this.prMasterId = prMasterId;
		this.description = description;
		this.note = note;
		this.quantityOrdered = quantityOrdered;
		this.quantityReceived = quantityReceived;
		this.quantityBilled = quantityBilled;
		this.unitCost = unitCost;
		this.unitPrice = unitPrice;
		this.priceMultiplier = priceMultiplier;
		this.taxable = taxable;
		this.hasSingleItemTaxAmount = hasSingleItemTaxAmount;
		this.joSchedDetailId = joSchedDetailId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cuSODetailID", unique = true, nullable = false)
	public Integer getCuSodetailId() {
		return this.cuSodetailId;
	}

	public void setCuSodetailId(Integer cuSodetailId) {
		this.cuSodetailId = cuSodetailId;
	}

	@Column(name = "cuSOID")
	public Integer getCuSoid() {
		return this.cuSoid;
	}

	public void setCuSoid(Integer cuSoid) {
		this.cuSoid = cuSoid;
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

	@Column(name = "QuantityOrdered", scale = 4)
	public BigDecimal getQuantityOrdered() {
		return this.quantityOrdered;
	}

	public void setQuantityOrdered(BigDecimal quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	@Column(name = "QuantityReceived", scale = 4)
	public BigDecimal getQuantityReceived() {
		return this.quantityReceived;
	}

	public void setQuantityReceived(BigDecimal quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	@Column(name = "QuantityBilled", scale = 4)
	public BigDecimal getQuantityBilled() {
		return this.quantityBilled;
	}

	public void setQuantityBilled(BigDecimal quantityBilled) {
		this.quantityBilled = quantityBilled;
	}

	@Column(name = "UnitCost", scale = 4)
	public BigDecimal getUnitCost() {
		return this.unitCost;
	}

	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

	@Column(name = "UnitPrice", scale = 4)
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "PriceMultiplier", scale = 4)
	public BigDecimal getPriceMultiplier() {
		return this.priceMultiplier;
	}

	public void setPriceMultiplier(BigDecimal priceMultiplier) {
		this.priceMultiplier = priceMultiplier;
	}

	@Column(name = "Taxable")
	public Byte getTaxable() {
		return this.taxable;
	}

	public void setTaxable(Byte taxable) {
		this.taxable = taxable;
	}

	@Column(name = "HasSingleItemTaxAmount", nullable = false)
	public boolean isHasSingleItemTaxAmount() {
		return this.hasSingleItemTaxAmount;
	}

	public void setHasSingleItemTaxAmount(boolean hasSingleItemTaxAmount) {
		this.hasSingleItemTaxAmount = hasSingleItemTaxAmount;
	}

	@Column(name = "joSchedDetailID")
	public Integer getJoSchedDetailId() {
		return this.joSchedDetailId;
	}

	public void setJoSchedDetailId(Integer joSchedDetailId) {
		this.joSchedDetailId = joSchedDetailId;
	}
	@Transient
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	@Transient
	public BigDecimal getTaxTotal() {
		return TaxTotal;
	}

	public void setTaxTotal(BigDecimal taxTotal) {
		TaxTotal = taxTotal;
	}
	@Transient
	public String getNoteImage() {
		return noteImage;
	}

	public void setNoteImage(String noteImage) {
		this.noteImage = noteImage;
	}

	@Column(name = "whseCost")
	public BigDecimal getWhseCost() {
		return whseCost;
	}

	public void setWhseCost(BigDecimal whseCost) {
		this.whseCost = whseCost;
	}

	@Column(name = "position")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	@Transient
	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Transient
	public BigDecimal getTaxableSum() {
		return TaxableSum;
	}

	public void setTaxableSum(BigDecimal taxableSum) {
		TaxableSum = taxableSum;
	}
	

}