package com.turborep.turbotracker.customer.dao;

// Generated Jul 9, 2014 5:57:18 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cusodetailtemplate generated by hbm2java
 */
@Entity
@Table(name = "cuSODetailTemplate", catalog = "")
public class Cusodetailtemplate implements java.io.Serializable {

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
	private byte taxable;
	private Byte hasSingleItemTaxAmount;
	private Integer joSchedDetailId;
	private Integer salesOrderDeleteIDImage;
	private String noteImage;
	private String inlineNote;
	private Integer position;

	public Cusodetailtemplate() {
	}

	public Cusodetailtemplate(byte taxable) {
		this.taxable = taxable;
	}

	public Cusodetailtemplate(Integer cuSoid, Integer prMasterId,
			String description, String note, BigDecimal quantityOrdered,
			BigDecimal quantityReceived, BigDecimal quantityBilled,
			BigDecimal unitCost, BigDecimal unitPrice,
			BigDecimal priceMultiplier, byte taxable,
			Byte hasSingleItemTaxAmount, Integer joSchedDetailId) {
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

	@Column(name = "Taxable", nullable = false)
	public byte getTaxable() {
		return this.taxable;
	}

	public void setTaxable(byte taxable) {
		this.taxable = taxable;
	}

	@Column(name = "HasSingleItemTaxAmount")
	public Byte getHasSingleItemTaxAmount() {
		return this.hasSingleItemTaxAmount;
	}

	public void setHasSingleItemTaxAmount(Byte hasSingleItemTaxAmount) {
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
	public String getNoteImage() {
		return noteImage;
	}

	public void setNoteImage(String noteImage) {
		this.noteImage = noteImage;
	}
	@Transient
	public String getInlineNote() {
		return inlineNote;
	}

	public void setInlineNote(String inlineNote) {
		this.inlineNote = inlineNote;
	}
	
	@Transient
	public Integer getSalesOrderDeleteIDImage() {
		return salesOrderDeleteIDImage;
	}
	@Transient
	public void setSalesOrderDeleteIDImage(Integer salesOrderDeleteIDImage) {
		this.salesOrderDeleteIDImage = salesOrderDeleteIDImage;
	}
	@Column(name = "position")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
