package com.turborep.turbotracker.vendor.dao;

import java.math.BigDecimal;
import java.sql.Date;

public class PurchaseOrdersBean {
	
	private Integer vePOID, joReleaseID, rxVendorID,veReceiveID;
	private Date createdOn;
	private String PONumber, vendorName, jobName, city, estimatedShipDate;
	private BigDecimal subtotal;
	private BigDecimal costtotal;
	private BigDecimal difference;
	private BigDecimal quantityOrdered;
	private BigDecimal inventoryOnHand;
	private BigDecimal inventoryAllocated;
	private BigDecimal inventoryOnOrder;
	private BigDecimal inventoryAvailable;
	private BigDecimal whCost;

	
	public BigDecimal getDifference() {
		return difference;
	}
	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}
	public Integer getVePOID() {
		return vePOID;
	}
	public void setVePOID(Integer vePOID) {
		this.vePOID = vePOID;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getJoReleaseID() {
		return joReleaseID;
	}
	public void setJoReleaseID(Integer joReleaseID) {
		this.joReleaseID = joReleaseID;
	}
	public Integer getRxVendorID() {
		return rxVendorID;
	}
	public void setRxVendorID(Integer rxVendorID) {
		this.rxVendorID = rxVendorID;
	}
	public String getPONumber() {
		return PONumber;
	}
	public void setPONumber(String pONumber) {
		PONumber = pONumber;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getCosttotal() {
		return costtotal;
	}
	public void setCosttotal(BigDecimal costtotal) {
		this.costtotal = costtotal;
	}
	public BigDecimal getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(BigDecimal quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEstimatedShipDate() {
		return estimatedShipDate;
	}
	public void setEstimatedShipDate(String estimatedShipDate) {
		this.estimatedShipDate = estimatedShipDate;
	}
	public BigDecimal getInventoryOnHand() {
		return inventoryOnHand;
	}
	public void setInventoryOnHand(BigDecimal inventoryOnHand) {
		this.inventoryOnHand = inventoryOnHand;
	}
	public BigDecimal getInventoryAllocated() {
		return inventoryAllocated;
	}
	public void setInventoryAllocated(BigDecimal inventoryAllocated) {
		this.inventoryAllocated = inventoryAllocated;
	}
	public BigDecimal getInventoryOnOrder() {
		return inventoryOnOrder;
	}
	public void setInventoryOnOrder(BigDecimal inventoryOnOrder) {
		this.inventoryOnOrder = inventoryOnOrder;
	}
	public BigDecimal getInventoryAvailable() {
		return inventoryAvailable;
	}
	public void setInventoryAvailable(BigDecimal inventoryAvailable) {
		this.inventoryAvailable = inventoryAvailable;
	}
	public BigDecimal getWhCost() {
		return whCost;
	}
	public void setWhCost(BigDecimal whCost) {
		this.whCost = whCost;
	}
	public Integer getVeReceiveID() {
		return veReceiveID;
	}
	public void setVeReceiveID(Integer veReceiveID) {
		this.veReceiveID = veReceiveID;
	}
	

}
