package com.turborep.turbotracker.customer.dao;

public class StatementsBean {
	
	private Integer rxMasterID;
	private String name;
	private String email;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRxMasterID() {
		return rxMasterID;
	}
	public void setRxMasterID(Integer rxMasterID) {
		this.rxMasterID = rxMasterID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
