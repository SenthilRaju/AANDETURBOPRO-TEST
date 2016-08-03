package com.turborep.turbotracker.Inventory.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tpInventoryLogMaster", catalog = "")
public class TpInventoryLogMaster {
	private Integer tpInventoryLogID;
	private Integer prMasterID;
	private String productCode;
	private Integer warehouseID ;
	private String warehouseName ;
	private BigDecimal onHand ;
	private BigDecimal wrOnHand ;
	private BigDecimal allocated ;
	private BigDecimal onOrder ;
	private String transType ;
	private Integer transID ;
	private Integer transDetailID ;
	private String referenceNo ;
	private String refRelatedNo ;
	private BigDecimal productOut ;
	private BigDecimal productIn ;
	private String transDecription ;
	private Integer userId ;
	private String userName;
	private Date createdOn;
	
	public TpInventoryLogMaster(){};
	public TpInventoryLogMaster(Integer prMasterID,String productCode,Integer warehouseID ,String warehouseName ,BigDecimal onHand ,BigDecimal wrOnHand ,
			BigDecimal allocated ,BigDecimal onOrder ,String transType ,Integer transID ,Integer transDetailID ,String referenceNo ,
			String refRelatedNo ,BigDecimal productOut ,BigDecimal productIn ,String transDecription ,Integer userId ,String userName,Date createdOn){
		this.prMasterID=prMasterID; 
		this.productCode=productCode; 
		this.warehouseID=warehouseID; 
		this.warehouseName=warehouseName; 
		this.onHand=onHand; 
		this.wrOnHand=wrOnHand; 
		this.allocated=allocated; 
		this.onOrder=onOrder; 
		this.transType=transType; 
		this.transID=transID; 
		this.transDetailID=transDetailID; 
		this.referenceNo=referenceNo; 
		this.refRelatedNo=refRelatedNo; 
		this.productOut=productOut; 
		this.productIn=productIn; 
		this.transDecription=transDecription; 
		this.userId=userId; 
		this.userName=userName; 
		this.createdOn=createdOn;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "tpInventoryLogID", unique = true, nullable = false)
	public Integer getTpInventoryLogID() {
		return tpInventoryLogID;
	}
	public void setTpInventoryLogID(Integer tpInventoryLogID) {
		this.tpInventoryLogID = tpInventoryLogID;
	}
	@Column(name = "prMasterID")
	public Integer getPrMasterID() {
		return prMasterID;
	}
	public void setPrMasterID(Integer prMasterID) {
		this.prMasterID = prMasterID;
	}
	@Column(name = "productCode")
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	@Column(name = "warehouseID")
	public Integer getWarehouseID() {
		return warehouseID;
	}
	public void setWarehouseID(Integer warehouseID) {
		this.warehouseID = warehouseID;
	}
	@Column(name = "warehouseName")
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	@Column(name = "onHand")
	public BigDecimal getOnHand() {
		return onHand;
	}
	public void setOnHand(BigDecimal onHand) {
		this.onHand = onHand;
	}
	@Column(name = "wrOnHand")
	public BigDecimal getWrOnHand() {
		return wrOnHand;
	}
	public void setWrOnHand(BigDecimal wrOnHand) {
		this.wrOnHand = wrOnHand;
	}
	@Column(name = "allocated")
	public BigDecimal getAllocated() {
		return allocated;
	}
	public void setAllocated(BigDecimal allocated) {
		this.allocated = allocated;
	}
	@Column(name = "onOrder")
	public BigDecimal getOnOrder() {
		return onOrder;
	}
	public void setOnOrder(BigDecimal onOrder) {
		this.onOrder = onOrder;
	}
	@Column(name = "transType")
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	@Column(name = "transID")
	public Integer getTransID() {
		return transID;
	}
	public void setTransID(Integer transID) {
		this.transID = transID;
	}
	@Column(name = "transDetailID")
	public Integer getTransDetailID() {
		return transDetailID;
	}
	public void setTransDetailID(Integer transDetailID) {
		this.transDetailID = transDetailID;
	}
	@Column(name = "referenceNo")
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	@Column(name = "refRelatedNo")
	public String getRefRelatedNo() {
		return refRelatedNo;
	}
	public void setRefRelatedNo(String refRelatedNo) {
		this.refRelatedNo = refRelatedNo;
	}
	@Column(name = "productOut")
	public BigDecimal getProductOut() {
		return productOut;
	}
	public void setProductOut(BigDecimal productOut) {
		this.productOut = productOut;
	}
	@Column(name = "productIn")
	public BigDecimal getProductIn() {
		return productIn;
	}
	public void setProductIn(BigDecimal productIn) {
		this.productIn = productIn;
	}
	@Column(name = "transDecription")
	public String getTransDecription() {
		return transDecription;
	}
	public void setTransDecription(String transDecription) {
		this.transDecription = transDecription;
	}
	@Column(name = "userId")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Column(name = "userName")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	
}
