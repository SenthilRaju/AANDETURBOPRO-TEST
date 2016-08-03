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
@Table(name = "tpInventoryLog", catalog = "")
public class TpInventoryLog {
	 private Integer tpInventoryLogID;
	 private Integer prMasterID;
	 private String productCode;
	 private Integer wareHouseID;
	 private String transType;
	 private Integer transID;
	 private Integer transDetailID;
	 private BigDecimal productOut;
	 private BigDecimal productIn;
	 private Integer userID;
	 private Date createdOn;
	 private String transDecription;
	 
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
	
	@Column(name = "wareHouseID")
	public Integer getWareHouseID() {
		return wareHouseID;
	}
	public void setWareHouseID(Integer wareHouseID) {
		this.wareHouseID = wareHouseID;
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
	
	@Column(name = "userID")
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
	@Column(name = "createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "transDecription")
	public String getTransDecription() {
		return transDecription;
	}

	public void setTransDecription(String transDecription) {
		this.transDecription = transDecription;
	}

	@Column(name = "transDetailID")
	public Integer getTransDetailID() {
		return transDetailID;
	}

	public void setTransDetailID(Integer transDetailID) {
		this.transDetailID = transDetailID;
	}
     
	 
}
