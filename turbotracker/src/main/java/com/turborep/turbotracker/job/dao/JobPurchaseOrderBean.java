package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;
import java.util.Date;

public class JobPurchaseOrderBean {
	
	private Integer vePoid;
	private Integer createdById;
	private Integer transactionStatus;
	private Integer joReleaseId;
	private Integer rxVendorId;
	private Integer rxBillToId;
	private Integer rxBillToAddressId;
	private Integer rxShipToId;
	private Integer rxShipToAddressId;
	private Integer veShipViaId;
	private Integer prWarehouseId;
	private Integer veFreightChargesId;
	private Integer orderedById;
	private byte billToMode;
	private short shipToMode;
	private boolean acknowledged;
	private String ponumber;
	private String vendorOrderNumber;
	private String customerPonumber;
	private Date orderDate;
	private Date acknowledgementDate;
	private Date estimatedShipDate;
	private String dateWanted;
	private Integer wantedOnOrBefore;
	private String tag;
	private String specialInstructions;
	private BigDecimal subtotal;
	private BigDecimal taxTotal;
	private BigDecimal freight;
	private BigDecimal taxRate;
	private Integer rxVendorAddressId;
	private Integer rxVendorContactId;
	private Short veFactoryId;
	private String tsFullName; 
	private Integer isBillTo;
	private Integer isShipTo;
	private String emailTimeStamp;
	private String qbPO;
	
	
	/*
	 * TODO We have to clean it up
	 */
	private Integer rxBillToID;
//	private Integer rxShipToID;
	
	/**
	 * @return the vePoid
	 */
	public Integer getVePoid() {
		return vePoid;
	}
	/**
	 * @param vePoid the vePoid to set
	 */
	public void setVePoid(Integer vePoid) {
		this.vePoid = vePoid;
	}
	/**
	 * @return the createdById
	 */
	public Integer getCreatedById() {
		return createdById;
	}
	/**
	 * @param createdById the createdById to set
	 */
	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}
	/**
	 * @return the transactionStatus
	 */
	public Integer getTransactionStatus() {
		return transactionStatus;
	}
	/**
	 * @param transactionStatus the transactionStatus to set
	 */
	public void setTransactionStatus(Integer transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	/**
	 * @return the joReleaseId
	 */
	public Integer getJoReleaseId() {
		return joReleaseId;
	}
	/**
	 * @param joReleaseId the joReleaseId to set
	 */
	public void setJoReleaseId(Integer joReleaseId) {
		this.joReleaseId = joReleaseId;
	}
	/**
	 * @return the rxVendorId
	 */
	public Integer getRxVendorId() {
		return rxVendorId;
	}
	/**
	 * @param rxVendorId the rxVendorId to set
	 */
	public void setRxVendorId(Integer rxVendorId) {
		this.rxVendorId = rxVendorId;
	}
	/**
	 * @return the rxBillToId
	 */
	public Integer getRxBillToId() {
		return rxBillToId;
	}
	/**
	 * @param rxBillToId the rxBillToId to set
	 */
	public void setRxBillToId(Integer rxBillToId) {
		this.rxBillToId = rxBillToId;
	}
	/**
	 * @return the rxBillToAddressId
	 */
	public Integer getRxBillToAddressId() {
		return rxBillToAddressId;
	}
	/**
	 * @param rxBillToAddressId the rxBillToAddressId to set
	 */
	public void setRxBillToAddressId(Integer rxBillToAddressId) {
		this.rxBillToAddressId = rxBillToAddressId;
	}
	/**
	 * @return the rxShipToId
	 */
	public Integer getRxShipToId() {
		return rxShipToId;
	}
	/**
	 * @param rxShipToId the rxShipToId to set
	 */
	public void setRxShipToId(Integer rxShipToId) {
		this.rxShipToId = rxShipToId;
	}
	/**
	 * @return the rxShipToAddressId
	 */
	public Integer getRxShipToAddressId() {
		return rxShipToAddressId;
	}
	/**
	 * @param rxShipToAddressId the rxShipToAddressId to set
	 */
	public void setRxShipToAddressId(Integer rxShipToAddressId) {
		this.rxShipToAddressId = rxShipToAddressId;
	}
	/**
	 * @return the veShipViaId
	 */
	public Integer getVeShipViaId() {
		return veShipViaId;
	}
	/**
	 * @param veShipViaId the veShipViaId to set
	 */
	public void setVeShipViaId(Integer veShipViaId) {
		this.veShipViaId = veShipViaId;
	}
	/**
	 * @return the prWarehouseId
	 */
	public Integer getPrWarehouseId() {
		return prWarehouseId;
	}
	/**
	 * @param prWarehouseId the prWarehouseId to set
	 */
	public void setPrWarehouseId(Integer prWarehouseId) {
		this.prWarehouseId = prWarehouseId;
	}
	/**
	 * @return the veFreightChargesId
	 */
	public Integer getVeFreightChargesId() {
		return veFreightChargesId;
	}
	/**
	 * @param veFreightChargesId the veFreightChargesId to set
	 */
	public void setVeFreightChargesId(Integer veFreightChargesId) {
		this.veFreightChargesId = veFreightChargesId;
	}
	/**
	 * @return the orderedById
	 */
	public Integer getOrderedById() {
		return orderedById;
	}
	/**
	 * @param orderedById the orderedById to set
	 */
	public void setOrderedById(Integer orderedById) {
		this.orderedById = orderedById;
	}
	/**
	 * @return the billToMode
	 */
	public byte getBillToMode() {
		return billToMode;
	}
	/**
	 * @param billToMode the billToMode to set
	 */
	public void setBillToMode(byte billToMode) {
		this.billToMode = billToMode;
	}
	/**
	 * @return the shipToMode
	 */
	public short getShipToMode() {
		return shipToMode;
	}
	/**
	 * @param shipToMode the shipToMode to set
	 */
	public void setShipToMode(short shipToMode) {
		this.shipToMode = shipToMode;
	}
	/**
	 * @return the acknowledged
	 */
	public boolean isAcknowledged() {
		return acknowledged;
	}
	/**
	 * @param acknowledged the acknowledged to set
	 */
	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}
	/**
	 * @return the ponumber
	 */
	public String getPonumber() {
		return ponumber;
	}
	/**
	 * @param ponumber the ponumber to set
	 */
	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}
	/**
	 * @return the vendorOrderNumber
	 */
	public String getVendorOrderNumber() {
		return vendorOrderNumber;
	}
	/**
	 * @param vendorOrderNumber the vendorOrderNumber to set
	 */
	public void setVendorOrderNumber(String vendorOrderNumber) {
		this.vendorOrderNumber = vendorOrderNumber;
	}
	/**
	 * @return the customerPonumber
	 */
	public String getCustomerPonumber() {
		return customerPonumber;
	}
	/**
	 * @param customerPonumber the customerPonumber to set
	 */
	public void setCustomerPonumber(String customerPonumber) {
		this.customerPonumber = customerPonumber;
	}
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the acknowledgementDate
	 */
	public Date getAcknowledgementDate() {
		return acknowledgementDate;
	}
	/**
	 * @param acknowledgementDate the acknowledgementDate to set
	 */
	public void setAcknowledgementDate(Date acknowledgementDate) {
		this.acknowledgementDate = acknowledgementDate;
	}
	/**
	 * @return the estimatedShipDate
	 */
	public Date getEstimatedShipDate() {
		return estimatedShipDate;
	}
	/**
	 * @param estimatedShipDate the estimatedShipDate to set
	 */
	public void setEstimatedShipDate(Date estimatedShipDate) {
		this.estimatedShipDate = estimatedShipDate;
	}
	/**
	 * @return the dateWanted
	 */
	public String getDateWanted() {
		return dateWanted;
	}
	/**
	 * @param dateWanted the dateWanted to set
	 */
	public void setDateWanted(String dateWanted) {
		this.dateWanted = dateWanted;
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return the specialInstructions
	 */
	public String getSpecialInstructions() {
		return specialInstructions;
	}
	/**
	 * @param specialInstructions the specialInstructions to set
	 */
	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	/**
	 * @return the subtotal
	 */
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	/**
	 * @return the taxTotal
	 */
	public BigDecimal getTaxTotal() {
		return taxTotal;
	}
	/**
	 * @param taxTotal the taxTotal to set
	 */
	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}
	/**
	 * @return the freight
	 */
	public BigDecimal getFreight() {
		return freight;
	}
	/**
	 * @param freight the freight to set
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	/**
	 * @return the taxRate
	 */
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	/**
	 * @param taxRate the taxRate to set
	 */
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	/**
	 * @return the rxVendorAddressId
	 */
	public Integer getRxVendorAddressId() {
		return rxVendorAddressId;
	}
	/**
	 * @param rxVendorAddressId the rxVendorAddressId to set
	 */
	public void setRxVendorAddressId(Integer rxVendorAddressId) {
		this.rxVendorAddressId = rxVendorAddressId;
	}
	/**
	 * @return the rxVendorContactId
	 */
	public Integer getRxVendorContactId() {
		return rxVendorContactId;
	}
	/**
	 * @param rxVendorContactId the rxVendorContactId to set
	 */
	public void setRxVendorContactId(Integer rxVendorContactId) {
		this.rxVendorContactId = rxVendorContactId;
	}
	/**
	 * @return the tsFullName
	 */
	public String getTsFullName() {
		return tsFullName;
	}
	/**
	 * @param tsFullName the tsFullName to set
	 */
	public void setTsFullName(String tsFullName) {
		this.tsFullName = tsFullName;
	}
	/**
	 * @return the veFactoryId
	 */
	public Short getVeFactoryId() {
		return veFactoryId;
	}
	/**
	 * @param veFactoryId the veFactoryId to set
	 */
	public void setVeFactoryId(Short veFactoryId) {
		this.veFactoryId = veFactoryId;
	}
	
	public Integer getBillTo() {
		return isBillTo;
	}

	public void setBillTo(Integer isBillTo) {
		this.isBillTo = isBillTo;
	}
	
	public Integer getShipTo() {
		return isShipTo;
	}

	public void setShipTo(Integer isShipTo) {
		this.isShipTo = isShipTo;
	}
	public String getEmailTimeStamp() {
		return emailTimeStamp;
	}
	public void setEmailTimeStamp(String emailTimeStamp) {
		this.emailTimeStamp = emailTimeStamp;
	}
	
	/**
	 * 
	 * @return wantedOnOrBefore
	 */
	public Integer getWantedOnOrBefore() {
		return wantedOnOrBefore;
	}
	public void setWantedOnOrBefore(Integer wantedOnOrBefore) {
		this.wantedOnOrBefore = wantedOnOrBefore;
	}
	public String getQbPO() {
		return qbPO;
	}
	public void setQbPO(String qbPO) {
		this.qbPO = qbPO;
	}
	
	public Integer getRxBillToID() {
		return rxBillToID;
	}
	public void setRxBillToID(Integer rxBillToID) {
		this.rxBillToID = rxBillToID;
	}
	
	
}