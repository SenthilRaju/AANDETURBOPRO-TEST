package com.turborep.turbotracker.banking.dao;

import java.math.BigDecimal;

public class GlAccountAmount {
	
	private Integer coAccountid;
	private String coAccountNumber;
	private BigDecimal QuantityBilled;
	private BigDecimal UnitCost;
	private BigDecimal PriceMultiplier;
	
	
	
	
	public Integer getCoAccountid() {
		return coAccountid;
	}
	public void setCoAccountid(Integer coAccountid) {
		this.coAccountid = coAccountid;
	}
	public String getCoAccountNumber() {
		return coAccountNumber;
	}
	public void setCoAccountNumber(String coAccountNumber) {
		this.coAccountNumber = coAccountNumber;
	}
	public BigDecimal getQuantityBilled() {
		return QuantityBilled;
	}
	public void setQuantityBilled(BigDecimal quantityBilled) {
		QuantityBilled = quantityBilled;
	}
	public BigDecimal getUnitCost() {
		return UnitCost;
	}
	public void setUnitCost(BigDecimal unitCost) {
		UnitCost = unitCost;
	}
	public BigDecimal getPriceMultiplier() {
		return PriceMultiplier;
	}
	public void setPriceMultiplier(BigDecimal priceMultiplier) {
		PriceMultiplier = priceMultiplier;
	}
	
	

}
