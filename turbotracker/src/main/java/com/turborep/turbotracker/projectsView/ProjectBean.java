package com.turborep.turbotracker.projectsView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class  ProjectBean {
	
	private String customerName;
	
	private Integer rxMasterId;
	private Integer joMasterId;
	private String jobNumber;
	private String jobName;
	private BigDecimal contractAmount;
	private BigDecimal allocated;
	private BigDecimal invoiced;
	private BigDecimal unreleased;
	private BigDecimal profit;
	private BigDecimal margin;
	private BigDecimal sales;
	private BigDecimal currentAmt;
	private BigDecimal thirtyDays;
	private BigDecimal sixtyDays;
	private BigDecimal ninetyDays;
	private BigDecimal totalDaysAmt;
	
	private String invoiceDate;
	private String invoiceNumber;
	private String poNumber;
	private BigDecimal poAmount;
	private Integer days;
	private Integer invoiceID;
	private Date inv_date;
	
	
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	public BigDecimal getSales() {
		return sales;
	}
	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}
	public BigDecimal getUnreleased() {
		return unreleased;
	}
	public void setUnreleased(BigDecimal unreleased) {
		this.unreleased = unreleased;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getRxMasterId() {
		return rxMasterId;
	}
	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}
	public Integer getJoMasterId() {
		return joMasterId;
	}
	public void setJoMasterId(Integer joMasterId) {
		this.joMasterId = joMasterId;
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
	public BigDecimal getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}
	public BigDecimal getAllocated() {
		return allocated;
	}
	public void setAllocated(BigDecimal allocated) {
		this.allocated = allocated;
	}
	public BigDecimal getInvoiced() {
		return invoiced;
	}
	public void setInvoiced(BigDecimal invoiced) {
		this.invoiced = invoiced;
	}
	public BigDecimal getCurrentAmt() {
		return currentAmt;
	}
	public void setCurrentAmt(BigDecimal currentAmt) {
		this.currentAmt = currentAmt;
	}
	public BigDecimal getThirtyDays() {
		return thirtyDays;
	}
	public void setThirtyDays(BigDecimal thirtyDays) {
		this.thirtyDays = thirtyDays;
	}
	public BigDecimal getSixtyDays() {
		return sixtyDays;
	}
	public void setSixtyDays(BigDecimal sixtyDays) {
		this.sixtyDays = sixtyDays;
	}
	public BigDecimal getNinetyDays() {
		return ninetyDays;
	}
	public void setNinetyDays(BigDecimal ninetyDays) {
		this.ninetyDays = ninetyDays;
	}
	public BigDecimal getTotalDaysAmt() {
		return totalDaysAmt;
	}
	public void setTotalDaysAmt(BigDecimal totalDaysAmt) {
		this.totalDaysAmt = totalDaysAmt;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public BigDecimal getPoAmount() {
		return poAmount;
	}
	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date = formatter.parse(invoiceDate);
			this.inv_date=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Integer getInvoiceID() {
		return invoiceID;
	}
	public void setInvoiceID(Integer invoiceID) {
		this.invoiceID = invoiceID;
	}
	public Date getInv_date() {
		return inv_date;
	}
}