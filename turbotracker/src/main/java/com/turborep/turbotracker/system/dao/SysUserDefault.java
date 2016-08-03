package com.turborep.turbotracker.system.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "SysUserDefault", catalog = "")
public class SysUserDefault {
	private Integer sysUserDefaultID;
	private Integer userLoginID;
	private Integer warehouseID;
	private Integer coDivisionID;
	
	public SysUserDefault() {
	}

	public SysUserDefault(Integer sysUserDefaultID) {
		this.sysUserDefaultID = sysUserDefaultID;
	}

	public SysUserDefault(Integer sysUserDefaultID, Integer userLoginID,Integer warehouseID, Integer coDivisionID) {
		this.sysUserDefaultID = sysUserDefaultID;
		this.userLoginID = userLoginID;
		this.warehouseID = warehouseID;
		this.coDivisionID = coDivisionID;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SysUserDefaultID", unique = true, nullable = false)
	public Integer getSysUserDefaultID() {
		return sysUserDefaultID;
	}
	public void setSysUserDefaultID(Integer sysUserDefaultID) {
		this.sysUserDefaultID = sysUserDefaultID;
	}
	
	@Column(name = "UserLoginID")
	public Integer getUserLoginID() {
		return userLoginID;
	}
	public void setUserLoginID(Integer userLoginID) {
		this.userLoginID = userLoginID;
	}
	
	@Column(name = "WarehouseID")
	public Integer getWarehouseID() {
		return warehouseID;
	}
	public void setWarehouseID(Integer warehouseID) {
		this.warehouseID = warehouseID;
	}
	
	@Column(name = "coDivisionID")
	public Integer getCoDivisionID() {
		return coDivisionID;
	}
	public void setCoDivisionID(Integer coDivisionID) {
		this.coDivisionID = coDivisionID;
	}
	
}
