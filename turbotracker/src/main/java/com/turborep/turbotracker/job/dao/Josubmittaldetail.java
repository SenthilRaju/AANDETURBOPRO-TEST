package com.turborep.turbotracker.job.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Josubmittaldetail generated by hbm2java
 */
@Entity
@Table(name = "joSubmittalDetail", catalog = "")
public class Josubmittaldetail implements java.io.Serializable {

	private Integer joSubmittalDetailId;
	private Integer joSubmittalHeaderId;
	private Integer joLineNumber;
	private Integer joSchedTempHeaderId;
	private String product;
	private String quantity;
	private String paragraph;
	private Integer rxManufacturerId;
	private String fnEngineer;
	private String manufacturerOverride;
	private Integer status;
	private BigDecimal cost;
	private BigDecimal estimatedCost;
	private boolean importToPo;
	private String altManufacturer;
	private String released;
	private String engineerNote;

	public Josubmittaldetail() {
	}

	public Josubmittaldetail(Integer joSubmittalHeaderId, Integer joLineNumber,
			Integer joSchedTempHeaderId, String product, String quantity,
			String paragraph, Integer rxManufacturerId, String fnEngineer,
			String manufacturerOverride, Integer status, BigDecimal cost,
			BigDecimal estimatedCost, boolean importToPo, String altManufacturer,
			String released, String engineerNote) {
		this.joSubmittalHeaderId = joSubmittalHeaderId;
		this.joLineNumber = joLineNumber;
		this.joSchedTempHeaderId = joSchedTempHeaderId;
		this.product = product;
		this.quantity = quantity;
		this.paragraph = paragraph;
		this.rxManufacturerId = rxManufacturerId;
		this.fnEngineer = fnEngineer;
		this.manufacturerOverride = manufacturerOverride;
		this.status = status;
		this.cost = cost;
		this.estimatedCost = estimatedCost;
		this.importToPo = importToPo;
		this.altManufacturer = altManufacturer;
		this.released = released;
		this.engineerNote = engineerNote;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "joSubmittalDetailID", unique = true, nullable = false)
	public Integer getJoSubmittalDetailId() {
		return this.joSubmittalDetailId;
	}

	public void setJoSubmittalDetailId(Integer joSubmittalDetailId) {
		this.joSubmittalDetailId = joSubmittalDetailId;
	}

	@Column(name = "joSubmittalHeaderID")
	public Integer getJoSubmittalHeaderId() {
		return this.joSubmittalHeaderId;
	}

	public void setJoSubmittalHeaderId(Integer joSubmittalHeaderId) {
		this.joSubmittalHeaderId = joSubmittalHeaderId;
	}

	@Column(name = "joLineNumber")
	public Integer getJoLineNumber() {
		return this.joLineNumber;
	}

	public void setJoLineNumber(Integer joLineNumber) {
		this.joLineNumber = joLineNumber;
	}

	@Column(name = "joSchedTempHeaderID")
	public Integer getJoSchedTempHeaderId() {
		return this.joSchedTempHeaderId;
	}

	public void setJoSchedTempHeaderId(Integer joSchedTempHeaderId) {
		this.joSchedTempHeaderId = joSchedTempHeaderId;
	}

	@Column(name = "Product", length = 30)
	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@Column(name = "Quantity", length = 12)
	public String getQuantity() {
		return this.quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@Column(name = "Paragraph", length = 12)
	public String getParagraph() {
		return this.paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}

	@Column(name = "rxManufacturerID")
	public Integer getRxManufacturerId() {
		return this.rxManufacturerId;
	}

	public void setRxManufacturerId(Integer rxManufacturerId) {
		this.rxManufacturerId = rxManufacturerId;
	}

	@Column(name = "FN_Engineer")
	public String getFnEngineer() {
		return this.fnEngineer;
	}

	public void setFnEngineer(String fnEngineer) {
		this.fnEngineer = fnEngineer;
	}

	@Column(name = "ManufacturerOverride", length = 30)
	public String getManufacturerOverride() {
		return this.manufacturerOverride;
	}

	public void setManufacturerOverride(String manufacturerOverride) {
		this.manufacturerOverride = manufacturerOverride;
	}

	@Column(name = "Status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "Cost", scale = 4)
	public BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Column(name = "EstimatedCost", scale = 4)
	public BigDecimal getEstimatedCost() {
		return this.estimatedCost;
	}

	public void setEstimatedCost(BigDecimal estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	@Column(name = "ImportToPO", nullable = false)
	public boolean isImportToPo() {
		return importToPo;
	}

	public void setImportToPo(boolean importToPo) {
		this.importToPo = importToPo;
	}

	@Column(name = "AltManufacturer", length = 50)
	public String getAltManufacturer() {
		return altManufacturer;
	}

	public void setAltManufacturer(String altManufacturer) {
		this.altManufacturer = altManufacturer;
	}

	@Column(name = "Released", length = 50)
	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	@Column(name = "EngineerNote")
	public String getEngineerNote() {
		return engineerNote;
	}

	public void setEngineerNote(String engineerNote) {
		this.engineerNote = engineerNote;
	}

}
