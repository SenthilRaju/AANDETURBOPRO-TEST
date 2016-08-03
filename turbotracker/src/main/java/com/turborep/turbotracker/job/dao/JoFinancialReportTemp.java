package com.turborep.turbotracker.job.dao;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name = "joFinancialReportTemp", catalog = "")
public class JoFinancialReportTemp {
	private Integer joFinancialReportTempID;
	private Integer joReleaseID;
	private Integer joMasterID;
	private Date frtReleaseDate;
	private String name;
	private String frtReleaseType;
	private String frtReleaseNote;
	private BigDecimal frtpoCost;
	private Date frtAcknowledgementDate;
	private Date frtEstimatedShipDate;
	private Date frtShipDate;
	private String frtVendorOrderNumber;
	private String frtVendorInvoiceNumber;
	private BigDecimal frtInvoicedCost;
	private BigDecimal frtOtherFreight;
	private String frtCuInvoiceNumber;
	private BigDecimal frtCustomerAmount;
	private BigDecimal frtCustomerBalance;
	private String frtCustomerName;
	private String frtjobNumber;
	private String frtDescription;
	private String frtPO;
	private String frtReleaseSeq;
	private Integer frtseq_Number;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ecSplitJobID", unique = true, nullable = false)
	public Integer getJoFinancialReportTempID() {
		return joFinancialReportTempID;
	}
	public void setJoFinancialReportTempID(Integer joFinancialReportTempID) {
		this.joFinancialReportTempID = joFinancialReportTempID;
	}
	@Column(name = "joReleaseID")
	public Integer getJoReleaseID() {
		return joReleaseID;
	}
	public void setJoReleaseID(Integer joReleaseID) {
		this.joReleaseID = joReleaseID;
	}
	@Column(name = "frtReleaseDate")
	public Date getFrtReleaseDate() {
		return frtReleaseDate;
	}
	public void setFrtReleaseDate(Date frtReleaseDate) {
		this.frtReleaseDate = frtReleaseDate;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "frtReleaseType")
	public String getFrtReleaseType() {
		return frtReleaseType;
	}
	public void setFrtReleaseType(String frtReleaseType) {
		this.frtReleaseType = frtReleaseType;
	}
	
	@Column(name = "frtpoCost")
	public BigDecimal getFrtpoCost() {
		return frtpoCost;
	}
	public void setFrtpoCost(BigDecimal frtpoCost) {
		this.frtpoCost = frtpoCost;
	}
	
	@Column(name = "frtReleaseNote")
	public String getFrtReleaseNote() {
		return frtReleaseNote;
	}
	public void setFrtReleaseNote(String frtReleaseNote) {
		this.frtReleaseNote = frtReleaseNote;
	}
	
	@Column(name = "frtAcknowledgementDate")
	public Date getFrtAcknowledgementDate() {
		return frtAcknowledgementDate;
	}
	public void setFrtAcknowledgementDate(Date frtAcknowledgementDate) {
		this.frtAcknowledgementDate = frtAcknowledgementDate;
	}
	
	@Column(name = "frtEstimatedShipDate")
	public Date getFrtEstimatedShipDate() {
		return frtEstimatedShipDate;
	}
	public void setFrtEstimatedShipDate(Date frtEstimatedShipDate) {
		this.frtEstimatedShipDate = frtEstimatedShipDate;
	}
	
	@Column(name = "frtShipDate")
	public Date getFrtShipDate() {
		return frtShipDate;
	}
	public void setFrtShipDate(Date frtShipDate) {
		this.frtShipDate = frtShipDate;
	}
	
	@Column(name = "frtVendorOrderNumber")
	public String getFrtVendorOrderNumber() {
		return frtVendorOrderNumber;
	}
	public void setFrtVendorOrderNumber(String frtVendorOrderNumber) {
		this.frtVendorOrderNumber = frtVendorOrderNumber;
	}
	
	@Column(name = "frtVendorInvoiceNumber")
	public String getFrtVendorInvoiceNumber() {
		return frtVendorInvoiceNumber;
	}
	public void setFrtVendorInvoiceNumber(String frtVendorInvoiceNumber) {
		this.frtVendorInvoiceNumber = frtVendorInvoiceNumber;
	}
	
	@Column(name = "frtInvoicedCost")
	public BigDecimal getFrtInvoicedCost() {
		return frtInvoicedCost;
	}
	public void setFrtInvoicedCost(BigDecimal frtInvoicedCost) {
		this.frtInvoicedCost = frtInvoicedCost;
	}
	
	@Column(name = "frtOtherFreight")
	public BigDecimal getFrtOtherFreight() {
		return frtOtherFreight;
	}
	public void setFrtOtherFreight(BigDecimal frtOtherFreight) {
		this.frtOtherFreight = frtOtherFreight;
	}
	
	@Column(name = "frtCuInvoiceNumber")
	public String getFrtCuInvoiceNumber() {
		return frtCuInvoiceNumber;
	}
	public void setFrtCuInvoiceNumber(String frtCuInvoiceNumber) {
		this.frtCuInvoiceNumber = frtCuInvoiceNumber;
	}
	
	@Column(name = "frtCustomerAmount")
	public BigDecimal getFrtCustomerAmount() {
		return frtCustomerAmount;
	}
	public void setFrtCustomerAmount(BigDecimal frtCustomerAmount) {
		this.frtCustomerAmount = frtCustomerAmount;
	}
	
	@Column(name = "frtCustomerBalance")
	public BigDecimal getFrtCustomerBalance() {
		return frtCustomerBalance;
	}
	public void setFrtCustomerBalance(BigDecimal frtCustomerBalance) {
		this.frtCustomerBalance = frtCustomerBalance;
	}
	
	@Column(name = "frtCustomerName")
	public String getFrtCustomerName() {
		return frtCustomerName;
	}
	public void setFrtCustomerName(String frtCustomerName) {
		this.frtCustomerName = frtCustomerName;
	}
	
	@Column(name = "frtjobNumber")
	public String getFrtjobNumber() {
		return frtjobNumber;
	}
	public void setFrtjobNumber(String frtjobNumber) {
		this.frtjobNumber = frtjobNumber;
	}
	
	@Column(name = "frtDescription")
	public String getFrtDescription() {
		return frtDescription;
	}
	public void setFrtDescription(String frtDescription) {
		this.frtDescription = frtDescription;
	}
	
	@Column(name = "frtPO")
	public String getFrtPO() {
		return frtPO;
	}
	public void setFrtPO(String frtPO) {
		this.frtPO = frtPO;
	}
	
	@Column(name = "frtReleaseSeq")
	public String getFrtReleaseSeq() {
		return frtReleaseSeq;
	}
	public void setFrtReleaseSeq(String frtReleaseSeq) {
		this.frtReleaseSeq = frtReleaseSeq;
	}
	
	@Column(name = "frtseq_Number")
	public Integer getFrtseq_Number() {
		return frtseq_Number;
	}
	public void setFrtseq_Number(Integer frtseq_Number) {
		this.frtseq_Number = frtseq_Number;
	}
	
	@Column(name = "joMasterID")
	public Integer getJoMasterID() {
		return joMasterID;
	}
	public void setJoMasterID(Integer joMasterID) {
		this.joMasterID = joMasterID;
	}

}
