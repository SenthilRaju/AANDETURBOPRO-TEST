package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;

public class JobReleaseBean {
	
	private String released;
	private Short type;
	private Integer manufacturerId;
	private String manufacturer;
	private String note;
	private BigDecimal estimatedBilling;
	private BigDecimal subTotal;
	private BigDecimal taxTotal;
	private BigDecimal freight;
	private BigDecimal PO;
	private BigDecimal Allocated;
	private Integer joReleaseDetailid;
	private BigDecimal invoiceAmount;
	private Integer joReleaseId;
	private Integer vePoId;
	private String billNote;
	private String webSight;
	private String billNoteImage;
	private Integer rxMasterId;
	private String ponumber;
	private Integer rxVendorContactID;
	private Short shipToMode;
	private Integer billToAddressID;
	private Integer shipToAddressID;
	private Integer rxAddressID;
	private String customerPONumber;
	private String POID;
	private Integer cuSOID;
	private Integer RxcustomerID;
	private String qbPO;
	private boolean splitchkbox;
	private String address1;
	private String checkcloseoropen;
	private String checkcloseoropenhidden;
	private Integer transactionStatus;
	private Integer transStatus;
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	private String address2;
	private String city;
	private String state;
	private String zip;
	
	public String getReleased() {
		return released;
	}
	public void setReleased(String released) {
		this.released = released;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public BigDecimal getEstimatedBilling() {
		return estimatedBilling;
	}
	public void setEstimatedBilling(BigDecimal estimatedBilling) {
		this.estimatedBilling = estimatedBilling;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public BigDecimal getPO() {
		return PO;
	}
	public void setPO(BigDecimal pO) {
		this.PO = pO;
	}
	public BigDecimal getAllocated() {
		return Allocated;
	}
	public void setAllocated(BigDecimal allocated) {
		this.Allocated = allocated;
	}
	public Integer getJoReleaseDetailid() {
		return joReleaseDetailid;
	}
	public void setJoReleaseDetailid(Integer joReleaseDetailid) {
		this.joReleaseDetailid = joReleaseDetailid;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Integer getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public Integer getJoReleaseId() {
		return joReleaseId;
	}
	public void setJoReleaseId(Integer joReleaseId) {
		this.joReleaseId = joReleaseId;
	}
	public Integer getVePoId() {
		return vePoId;
	}
	public void setVePoId(Integer vePoId) {
		this.vePoId = vePoId;
	}
	public String getBillNote() {
		return billNote;
	}
	public void setBillNote(String billNote) {
		this.billNote = billNote;
	}
	public String getWebSight() {
		return webSight;
	}
	public void setWebSight(String webSight) {
		this.webSight = webSight;
	}
	public String getBillNoteImage() {
		return billNoteImage;
	}
	public void setBillNoteImage(String billNoteImage) {
		this.billNoteImage = billNoteImage;
	}
	public Integer getRxMasterId() {
		return rxMasterId;
	}
	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
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
	 * @return the rxVendorContactID
	 */
	public Integer getRxVendorContactID() {
		return rxVendorContactID;
	}
	/**
	 * @param rxVendorContactID the rxVendorContactID to set
	 */
	public void setRxVendorContactID(Integer rxVendorContactID) {
		this.rxVendorContactID = rxVendorContactID;
	}
	public Short getShipToMode() {
		return shipToMode;
	}
	public void setShipToMode(Short shipToMode) {
		this.shipToMode = shipToMode;
	}
	public Integer getBillToAddressID() {
		return billToAddressID;
	}
	public void setBillToAddressID(Integer billToAddressID) {
		this.billToAddressID = billToAddressID;
	}
	public Integer getShipToAddressID() {
		return shipToAddressID;
	}
	public void setShipToAddressID(Integer shipToAddressID) {
		this.shipToAddressID = shipToAddressID;
	}
	public Integer getRxAddressID() {
		return rxAddressID;
	}
	public void setRxAddressID(Integer rxAddressID) {
		this.rxAddressID = rxAddressID;
	}
	public String getCustomerPONumber() {
		return customerPONumber;
	}
	public void setCustomerPONumber(String customerPONumber) {
		this.customerPONumber = customerPONumber;
	}
	public String getPOID() {
		return POID;
	}
	public void setPOID(String aObj) {
		POID = aObj;
	}
	public Integer getCuSOID() {
		return cuSOID;
	}
	public void setCuSOID(Integer cuSOID) {
		this.cuSOID = cuSOID;
	}
	public Integer getRxcustomerID() {
		return RxcustomerID;
	}
	public void setRxcustomerID(Integer rxcustomerID) {
		RxcustomerID = rxcustomerID;
	}
	public String getQbPO() {
		return qbPO;
	}
	public void setQbPO(String qbPO) {
		this.qbPO = qbPO;
	}
	public boolean getSplitchkbox() {
		return splitchkbox;
	}
	public void setSplitchkbox(boolean splitchkbox) {
		this.splitchkbox = splitchkbox;
	}
	public String getCheckcloseoropen() {
		return checkcloseoropen;
	}
	public void setCheckcloseoropen(String checkcloseoropen) {
		this.checkcloseoropen = checkcloseoropen;
	}
	public String getCheckcloseoropenhidden() {
		return checkcloseoropenhidden;
	}
	public void setCheckcloseoropenhidden(String checkcloseoropenhidden) {
		this.checkcloseoropenhidden = checkcloseoropenhidden;
	}
	public Integer getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(Integer transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public Integer getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(Integer transStatus) {
		this.transStatus = transStatus;
	}
	
	
	
	
}