package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;
import java.util.Date;

public class JobSalesOrderBean {
	
	private Integer cuSoid;
	private Integer createdById;
	private Date createdOn;
	private Integer changedById;
	private Date changedOn;
	private Integer transactionStatus;
	private Integer joReleaseId;
	private Integer rxCustomerId;
	private Integer rxBillToId;
	private Integer rxBillToAddressId;
	private Integer rxShipToId;
	private Integer rxShipToAddressId;
	private Integer veShipViaId;
	private Integer prFromWarehouseId;
	private Integer prToWarehouseId;
	private Integer cuTermsId;
	private byte shipToMode;
	private Integer coTaxTerritoryId;
	private String sonumber;
	private String customerPonumber;
	private String datePromised;
	private String quickJobName;
	private Date orderDate;
	private Date shipDate;
	private BigDecimal freight;
	private BigDecimal costTotal;
	private BigDecimal subTotal;
	private BigDecimal taxTotal;
	private BigDecimal taxRate;
	private Integer cuAssignmentId0;
	private Integer cuAssignmentId1;
	private Integer cuAssignmentId2;
	private Integer cuAssignmentId3;
	private Integer cuAssignmentId4;
	private String trackingNumber;
	private Boolean surtaxOverrideCap;
	private BigDecimal surtaxTotal;
	private BigDecimal surtaxRate;
	private BigDecimal surtaxAmount;
	private BigDecimal singleItemTaxAmount;
	private Integer joScheddDetailID;
	private String tag;
	private BigDecimal freightCost;
	private Integer coDivisionID;
	private boolean taxExempt1;
	private boolean taxExempt2;
	private boolean taxExempt3;
	private boolean taxExempt4;
	private boolean taxExempt5;
	private boolean taxExempt6;
	private boolean taxExempt7;
	private boolean taxExempt8;
	private boolean taxExempt0;
	private String mailTimeStamp;
	public Integer getCuSoid() {
		return cuSoid;
	}
	public void setCuSoid(Integer cuSoid) {
		this.cuSoid = cuSoid;
	}
	public Integer getCreatedById() {
		return createdById;
	}
	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getChangedById() {
		return changedById;
	}
	public void setChangedById(Integer changedById) {
		this.changedById = changedById;
	}
	public Date getChangedOn() {
		return changedOn;
	}
	public void setChangedOn(Date changedOn) {
		this.changedOn = changedOn;
	}
	public Integer getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(Integer transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public Integer getJoReleaseId() {
		return joReleaseId;
	}
	public void setJoReleaseId(Integer joReleaseId) {
		this.joReleaseId = joReleaseId;
	}
	public Integer getRxCustomerId() {
		return rxCustomerId;
	}
	public void setRxCustomerId(Integer rxCustomerId) {
		this.rxCustomerId = rxCustomerId;
	}
	public Integer getRxBillToId() {
		return rxBillToId;
	}
	public void setRxBillToId(Integer rxBillToId) {
		this.rxBillToId = rxBillToId;
	}
	public Integer getRxBillToAddressId() {
		return rxBillToAddressId;
	}
	public void setRxBillToAddressId(Integer rxBillToAddressId) {
		this.rxBillToAddressId = rxBillToAddressId;
	}
	public Integer getRxShipToId() {
		return rxShipToId;
	}
	public void setRxShipToId(Integer rxShipToId) {
		this.rxShipToId = rxShipToId;
	}
	public Integer getRxShipToAddressId() {
		return rxShipToAddressId;
	}
	public void setRxShipToAddressId(Integer rxShipToAddressId) {
		this.rxShipToAddressId = rxShipToAddressId;
	}
	public Integer getVeShipViaId() {
		return veShipViaId;
	}
	public void setVeShipViaId(Integer veShipViaId) {
		this.veShipViaId = veShipViaId;
	}
	public Integer getPrFromWarehouseId() {
		return prFromWarehouseId;
	}
	public void setPrFromWarehouseId(Integer prFromWarehouseId) {
		this.prFromWarehouseId = prFromWarehouseId;
	}
	public Integer getPrToWarehouseId() {
		return prToWarehouseId;
	}
	public void setPrToWarehouseId(Integer prToWarehouseId) {
		this.prToWarehouseId = prToWarehouseId;
	}
	public Integer getCuTermsId() {
		return cuTermsId;
	}
	public void setCuTermsId(Integer cuTermsId) {
		this.cuTermsId = cuTermsId;
	}
	public byte getShipToMode() {
		return shipToMode;
	}
	public void setShipToMode(byte shipToMode) {
		this.shipToMode = shipToMode;
	}
	public Integer getCoTaxTerritoryId() {
		return coTaxTerritoryId;
	}
	public void setCoTaxTerritoryId(Integer coTaxTerritoryId) {
		this.coTaxTerritoryId = coTaxTerritoryId;
	}
	public String getSonumber() {
		return sonumber;
	}
	public void setSonumber(String sonumber) {
		this.sonumber = sonumber;
	}
	public String getCustomerPonumber() {
		return customerPonumber;
	}
	public void setCustomerPonumber(String customerPonumber) {
		this.customerPonumber = customerPonumber;
	}
	public String getDatePromised() {
		return datePromised;
	}
	public void setDatePromised(String datePromised) {
		this.datePromised = datePromised;
	}
	public String getQuickJobName() {
		return quickJobName;
	}
	public void setQuickJobName(String quickJobName) {
		this.quickJobName = quickJobName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getShipDate() {
		return shipDate;
	}
	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public BigDecimal getCostTotal() {
		return costTotal;
	}
	public void setCostTotal(BigDecimal costTotal) {
		this.costTotal = costTotal;
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
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public Integer getCuAssignmentId0() {
		return cuAssignmentId0;
	}
	public void setCuAssignmentId0(Integer cuAssignmentId0) {
		this.cuAssignmentId0 = cuAssignmentId0;
	}
	public Integer getCuAssignmentId1() {
		return cuAssignmentId1;
	}
	public void setCuAssignmentId1(Integer cuAssignmentId1) {
		this.cuAssignmentId1 = cuAssignmentId1;
	}
	public Integer getCuAssignmentId2() {
		return cuAssignmentId2;
	}
	public void setCuAssignmentId2(Integer cuAssignmentId2) {
		this.cuAssignmentId2 = cuAssignmentId2;
	}
	public Integer getCuAssignmentId3() {
		return cuAssignmentId3;
	}
	public void setCuAssignmentId3(Integer cuAssignmentId3) {
		this.cuAssignmentId3 = cuAssignmentId3;
	}
	public Integer getCuAssignmentId4() {
		return cuAssignmentId4;
	}
	public void setCuAssignmentId4(Integer cuAssignmentId4) {
		this.cuAssignmentId4 = cuAssignmentId4;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public Boolean getSurtaxOverrideCap() {
		return surtaxOverrideCap;
	}
	public void setSurtaxOverrideCap(Boolean surtaxOverrideCap) {
		this.surtaxOverrideCap = surtaxOverrideCap;
	}
	public BigDecimal getSurtaxTotal() {
		return surtaxTotal;
	}
	public void setSurtaxTotal(BigDecimal surtaxTotal) {
		this.surtaxTotal = surtaxTotal;
	}
	public BigDecimal getSurtaxRate() {
		return surtaxRate;
	}
	public void setSurtaxRate(BigDecimal surtaxRate) {
		this.surtaxRate = surtaxRate;
	}
	public BigDecimal getSurtaxAmount() {
		return surtaxAmount;
	}
	public void setSurtaxAmount(BigDecimal surtaxAmount) {
		this.surtaxAmount = surtaxAmount;
	}
	public BigDecimal getSingleItemTaxAmount() {
		return singleItemTaxAmount;
	}
	public void setSingleItemTaxAmount(BigDecimal singleItemTaxAmount) {
		this.singleItemTaxAmount = singleItemTaxAmount;
	}
	public Integer getJoScheddDetailID() {
		return joScheddDetailID;
	}
	public void setJoScheddDetailID(Integer joScheddDetailID) {
		this.joScheddDetailID = joScheddDetailID;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public BigDecimal getFreightCost() {
		return freightCost;
	}
	public void setFreightCost(BigDecimal freightCost) {
		this.freightCost = freightCost;
	}
	public Integer getCoDivisionID() {
		return coDivisionID;
	}
	public void setCoDivisionID(Integer coDivisionID) {
		this.coDivisionID = coDivisionID;
	}
	public boolean isTaxExempt1() {
		return taxExempt1;
	}
	public void setTaxExempt1(boolean taxExempt1) {
		this.taxExempt1 = taxExempt1;
	}
	public boolean isTaxExempt2() {
		return taxExempt2;
	}
	public void setTaxExempt2(boolean taxExempt2) {
		this.taxExempt2 = taxExempt2;
	}
	public boolean isTaxExempt3() {
		return taxExempt3;
	}
	public void setTaxExempt3(boolean taxExempt3) {
		this.taxExempt3 = taxExempt3;
	}
	public boolean isTaxExempt4() {
		return taxExempt4;
	}
	public void setTaxExempt4(boolean taxExempt4) {
		this.taxExempt4 = taxExempt4;
	}
	public boolean isTaxExempt5() {
		return taxExempt5;
	}
	public void setTaxExempt5(boolean taxExempt5) {
		this.taxExempt5 = taxExempt5;
	}
	public boolean isTaxExempt6() {
		return taxExempt6;
	}
	public void setTaxExempt6(boolean taxExempt6) {
		this.taxExempt6 = taxExempt6;
	}
	public boolean isTaxExempt7() {
		return taxExempt7;
	}
	public void setTaxExempt7(boolean taxExempt7) {
		this.taxExempt7 = taxExempt7;
	}
	public boolean isTaxExempt8() {
		return taxExempt8;
	}
	public void setTaxExempt8(boolean taxExempt8) {
		this.taxExempt8 = taxExempt8;
	}
	public boolean isTaxExempt0() {
		return taxExempt0;
	}
	public void setTaxExempt0(boolean taxExempt0) {
		this.taxExempt0 = taxExempt0;
	}
	public String getMailTimeStamp() {
		return mailTimeStamp;
	}
	public void setMailTimeStamp(String mailTimeStamp) {
		this.mailTimeStamp = mailTimeStamp;
	}
	

}
