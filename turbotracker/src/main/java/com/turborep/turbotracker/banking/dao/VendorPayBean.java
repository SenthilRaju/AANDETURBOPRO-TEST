package com.turborep.turbotracker.banking.dao;

import java.math.BigDecimal;

public class VendorPayBean {

private Integer vebillpayId,vebillID;
private BigDecimal applyingAmount, discountAmount;
public Integer getVebillpayId() {
	return vebillpayId;
}
public void setVebillpayId(Integer vebillpayId) {
	this.vebillpayId = vebillpayId;
}
public Integer getVebillID() {
	return vebillID;
}
public void setVebillID(Integer vebillID) {
	this.vebillID = vebillID;
}
public BigDecimal getApplyingAmount() {
	return applyingAmount;
}
public void setApplyingAmount(BigDecimal applyingAmount) {
	this.applyingAmount = applyingAmount;
}
public BigDecimal getDiscountAmount() {
	return discountAmount;
}
public void setDiscountAmount(BigDecimal discountAmount) {
	this.discountAmount = discountAmount;
}
}
