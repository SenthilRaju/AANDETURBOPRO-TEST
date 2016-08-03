package com.turborep.turbotracker.vendor.dao;

import java.math.BigDecimal;
import java.util.Date;

public class VendorBillsBean {

	private Integer veBillId, rxMasterId,vePoid;
	private String PONumber, payableTo, veInvoiceNumber;
	private BigDecimal billAmount, appliedAmount;
	private Date billDate;
	private Date dueDate,receiveDate;
	private String chkNo;
	private String datePaid;
	private String amt;
	private Integer transaction_status,jobStatus;
	private String creditUsed;
	private String jobNumber,jobName,customerName;
	private BigDecimal currentAmount;
	private Integer age;
	private BigDecimal age30Amount;
	private BigDecimal age60Amount;
	private BigDecimal age90Amount;
	
	
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	private Integer joreleasedetailid;
	
	public Integer getVeBillId() {
		return veBillId;
	}
	public void setVeBillId(Integer veBillId) {
		this.veBillId = veBillId;
	}
	
	public Integer getRxMasterId() {
		return rxMasterId;
	}
	public void setRxMasterId(Integer rxCustomerId) {
		this.rxMasterId = rxCustomerId;
	}
	
	public String getPONumber() {
		return PONumber;
	}
	public void setPONumber(String pONumber) {
		PONumber = pONumber;
	}
	
	public String getPayableTo() {
		return payableTo;
	}
	public void setPayableTo(String payableTo) {
		this.payableTo = payableTo;
	}
	
	public String getVeInvoiceNumber() {
		return veInvoiceNumber;
	}
	public void setVeInvoiceNumber(String veInvoiceNumber) {
		this.veInvoiceNumber = veInvoiceNumber;
	}
	
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	
	public BigDecimal getAppliedAmount() {
		return appliedAmount;
	}
	public void setAppliedAmount(BigDecimal appliedAmount) {
		this.appliedAmount = appliedAmount;
	}
	
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Integer getVePoid() {
		return vePoid;
	}
	public void setVePoid(Integer vePoid) {
		this.vePoid = vePoid;
	}
	public Integer getJoreleasedetailid() {
		return joreleasedetailid;
	}
	public void setJoreleasedetailid(Integer joreleasedetailid) {
		this.joreleasedetailid = joreleasedetailid;
	}
	public String getChkNo() {
		return chkNo;
	}
	public void setChkNo(String chkNo) {
		this.chkNo = chkNo;
	}
	public String getDatePaid() {
		return datePaid;
	}
	public void setDatePaid(String datePaid) {
		this.datePaid = datePaid;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public Integer getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(Integer transaction_status) {
		this.transaction_status = transaction_status;
	}
	public String getCreditUsed() {
		return creditUsed;
	}
	public void setCreditUsed(String creditUsed) {
		this.creditUsed = creditUsed;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}
	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public BigDecimal getAge30Amount() {
		return age30Amount;
	}
	public void setAge30Amount(BigDecimal age30Amount) {
		this.age30Amount = age30Amount;
	}
	public BigDecimal getAge60Amount() {
		return age60Amount;
	}
	public void setAge60Amount(BigDecimal age60Amount) {
		this.age60Amount = age60Amount;
	}
	public BigDecimal getAge90Amount() {
		return age90Amount;
	}
	public void setAge90Amount(BigDecimal age90Amount) {
		this.age90Amount = age90Amount;
	}
	
	
	
	
}
