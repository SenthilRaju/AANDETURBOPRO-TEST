package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;

public class JobsBean {
	private String 	bidDate;
	private String	jobName;
	private String	jobNo;
	private String	assignedSalesman;
	private String	assignedCustomers;
	private String	allCustomer;
	private String	architect;
	private String	engineer;
	private String	generalContractor;
	private String	assignedTakeoffBy;
	private String	assignedQuotesBy;
	private String	lowbidder;
	private BigDecimal	quoteAmount;
	
	private String bookedDate;
	private String customer;
	private String customerContact;
	private BigDecimal contractAmount;
	private String lastActivityDate;
	private String purchaseOrderNo;
	private String orderDate;
	private String vendor;
	private String vendorOrderNo;
	private String shipDate;
	private String cuInvoiceDate;
	private String quoteRev;
	private Integer joBidderId;
	private Integer joMasterId;
	private String quoteDate;
	private Integer contactID;
	private Integer rxMasterID;
	private Integer jobStatus;
	private String description;
	
	public String getBidDate() {
		return bidDate;
	}
	public void setBidDate(String string) {
		this.bidDate = string;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	public String getAssignedSalesman() {
		return assignedSalesman;
	}
	public void setAssignedSalesman(String assignedSalesman) {
		this.assignedSalesman = assignedSalesman;
	}
	
	public String getAssignedCustomers() {
		return assignedCustomers;
	}
	public void setAssignedCustomers(String assignedCustomers) {
		this.assignedCustomers = assignedCustomers;
	}
	
	public String getAllCustomer() {
		return allCustomer;
	}
	public void setAllCustomer(String allCustomer) {
		this.allCustomer = allCustomer;
	}
	
	public String getArchitect() {
		return architect;
	}
	public void setArchitect(String architect) {
		this.architect = architect;
	}
	
	public String getEngineer() {
		return engineer;
	}
	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}
	
	public String getGeneralContractor() {
		return generalContractor;
	}
	public void setGeneralContractor(String generalContractor) {
		this.generalContractor = generalContractor;
	}
	
	public String getAssignedTakeoffBy() {
		return assignedTakeoffBy;
	}
	public void setAssignedTakeoffBy(String assignedTakeoffBy) {
		this.assignedTakeoffBy = assignedTakeoffBy;
	}
	
	public String getAssignedQuotesBy() {
		return assignedQuotesBy;
	}
	public void setAssignedQuotesBy(String assignedQuotesBy) {
		this.assignedQuotesBy = assignedQuotesBy;
	}
	
	public String getLowbidder() {
		return lowbidder;
	}
	public void setLowbidder(String lowbidder) {
		this.lowbidder = lowbidder;
	}
	
	public BigDecimal getQuoteAmount() {
		return quoteAmount;
	}
	public void setQuoteAmount(BigDecimal obj) {
		this.quoteAmount = obj;
	}
	
	
	
	public String getBookedDate() {
		return bookedDate;
	}
	public void setBookedDate(String bookedDate) {
		this.bookedDate = bookedDate;
	}
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String cuStomerContact) {
		this.customerContact = cuStomerContact;
	}
	
	public BigDecimal getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}
	
	public String getLastActivityDate() {
		return lastActivityDate;
	}
	public void setLastActivityDate(String lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}
	
	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}
	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	public String getVendorOrderNo() {
		return vendorOrderNo;
	}
	public void setVendorOrderNo(String vendorOrderNo) {
		this.vendorOrderNo = vendorOrderNo;
	}
	
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getCuInvoiceDate() {
		return cuInvoiceDate;
	}
	public void setCuInvoiceDate(String cuInvoiceDate) {
		this.cuInvoiceDate = cuInvoiceDate;
	}
	public String getQuoteRev() {
		return quoteRev;
	}
	public void setQuoteRev(String quoteRev) {
		this.quoteRev = quoteRev;
	}
	public Integer getJoBidderId() {
		return joBidderId;
	}
	public void setJoBidderId(Integer joBidderId) {
		this.joBidderId = joBidderId;
	}
	public Integer getJoMasterId() {
		return joMasterId;
	}
	public void setJoMasterId(Integer joMasterId) {
		this.joMasterId = joMasterId;
	}
	public String getQuoteDate() {
		return quoteDate;
	}
	public void setQuoteDate(String quoteDate) {
		this.quoteDate = quoteDate;
	}
	public Integer getContactID() {
		return contactID;
	}
	public void setContactID(Integer contactID) {
		this.contactID = contactID;
	}
	public Integer getRxMasterID() {
		return rxMasterID;
	}
	public void setRxMasterID(Integer rxMasterID) {
		this.rxMasterID = rxMasterID;
	}
	public Integer getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}