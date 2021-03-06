package com.turborep.turbotracker.vendor.dao;

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

/**
 * Vecommdetail generated by hbm2java
 */
@Entity
@Table(name = "veCommDetail", catalog = "")
public class Vecommdetail implements java.io.Serializable {

	private Integer veCommDetailId;
	private Integer rxMasterId;
	private Integer joReleaseDetailId;
	private Date shipDate;
	private Integer veShipViaId;
	private String trackingNumber;
	private String invoiceNumber;
	private BigDecimal billAmount;
	private Date commDate;
	private BigDecimal commAmount;

	public Vecommdetail() {
	}

	public Vecommdetail(Integer rxMasterId, Integer joReleaseDetailId,
			Date shipDate, Integer veShipViaId, String trackingNumber,
			String invoiceNumber, BigDecimal billAmount, Date commDate,
			BigDecimal commAmount) {
		this.rxMasterId = rxMasterId;
		this.joReleaseDetailId = joReleaseDetailId;
		this.shipDate = shipDate;
		this.veShipViaId = veShipViaId;
		this.trackingNumber = trackingNumber;
		this.invoiceNumber = invoiceNumber;
		this.billAmount = billAmount;
		this.commDate = commDate;
		this.commAmount = commAmount;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "veCommDetailID", unique = true, nullable = false)
	public Integer getVeCommDetailId() {
		return this.veCommDetailId;
	}

	public void setVeCommDetailId(Integer veCommDetailId) {
		this.veCommDetailId = veCommDetailId;
	}

	@Column(name = "rxMasterID")
	public Integer getRxMasterId() {
		return this.rxMasterId;
	}

	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}

	@Column(name = "joReleaseDetailID")
	public Integer getJoReleaseDetailId() {
		return this.joReleaseDetailId;
	}

	public void setJoReleaseDetailId(Integer joReleaseDetailId) {
		this.joReleaseDetailId = joReleaseDetailId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ShipDate", length = 0)
	public Date getShipDate() {
		return this.shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	@Column(name = "veShipViaID")
	public Integer getVeShipViaId() {
		return this.veShipViaId;
	}

	public void setVeShipViaId(Integer veShipViaId) {
		this.veShipViaId = veShipViaId;
	}

	@Column(name = "TrackingNumber", length = 30)
	public String getTrackingNumber() {
		return this.trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	@Column(name = "InvoiceNumber", length = 20)
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@Column(name = "BillAmount", scale = 4)
	public BigDecimal getBillAmount() {
		return this.billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CommDate", length = 0)
	public Date getCommDate() {
		return this.commDate;
	}

	public void setCommDate(Date commDate) {
		this.commDate = commDate;
	}

	@Column(name = "CommAmount", scale = 4)
	public BigDecimal getCommAmount() {
		return this.commAmount;
	}

	public void setCommAmount(BigDecimal commAmount) {
		this.commAmount = commAmount;
	}

}
