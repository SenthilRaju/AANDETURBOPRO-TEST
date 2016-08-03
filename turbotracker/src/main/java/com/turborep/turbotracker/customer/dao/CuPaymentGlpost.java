package com.turborep.turbotracker.customer.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cuPaymentGlpost", catalog="")
public class CuPaymentGlpost implements java.io.Serializable{
	private Integer cuPaymentGlpostID;
	private Integer cuLinkageDetailID;
	private int groupID;
	private Integer rxCustomerID;
	private Integer cuInvoiceID;
	private int postStatus;
	
	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="cuPaymentGlpostID", unique=true, nullable=false)
	public Integer getCuPaymentGlpostID() {
		return cuPaymentGlpostID;
	}
	public void setCuPaymentGlpostID(Integer cuPaymentGlpostID) {
		this.cuPaymentGlpostID = cuPaymentGlpostID;
	}
	
	@Column(name="cuLinkageDetailID")
	public Integer getCuLinkageDetailID() {
		return cuLinkageDetailID;
	}
	public void setCuLinkageDetailID(Integer cuLinkageDetailID) {
		this.cuLinkageDetailID = cuLinkageDetailID;
	}
	
	@Column(name="groupID")
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	@Column(name="postStatus")
	public int getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(int postStatus) {
		this.postStatus = postStatus;
	}
	
	@Column(name="rxCustomerID")
	public Integer getRxCustomerID() {
		return rxCustomerID;
	}
	public void setRxCustomerID(Integer rxCustomerID) {
		this.rxCustomerID = rxCustomerID;
	}
	
	@Column(name="cuInvoiceID")
	public Integer getCuInvoiceID() {
		return cuInvoiceID;
	}
	public void setCuInvoiceID(Integer cuInvoiceID) {
		this.cuInvoiceID = cuInvoiceID;
	}
	
	
	
	
}
