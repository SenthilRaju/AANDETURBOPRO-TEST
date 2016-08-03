package com.turborep.turbotracker.Rolodex.dao;

import javax.persistence.Column;

public class RolodexBean {
	
	private int rxMasterId;
	private String name;
	private String firstName;
	private String phone;
	private String phone1;
	private String phone2;
	private String fax;
	private String city;
	private String state;
	private String address1;
	private String address2;
	private String zip;
	private byte inactive;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String obj) {
		this.phone1 = obj;
	}
	
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String obj) {
		this.phone2 = obj;
	}
	
	public String getFax() {
		return fax;
	}
	public void setFax(String obj) {
		this.fax = obj;
	}
	
	public String getZip() {
		return zip;
	}
	public void setZip(String obj) {
		this.zip = obj;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	public String getAddress12() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public int getRxMasterId() {
		return rxMasterId;
	}
	public void setRxMasterId(int rxMasterId) {
		this.rxMasterId = rxMasterId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
    public byte getInactive() {
        return this.inactive;
    }
    
    public void setInactive(byte inactive) {
        this.inactive = inactive;
    }
}
