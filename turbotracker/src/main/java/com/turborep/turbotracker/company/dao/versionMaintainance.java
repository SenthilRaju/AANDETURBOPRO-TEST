package com.turborep.turbotracker.company.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "versionMaintainance", catalog = "")
public class versionMaintainance {
private int id;
private String appversion;
private String dbversion;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", unique = true, nullable = false)
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
@Column(name = "appversion")
public String getAppversion() {
	return appversion;
}
public void setAppversion(String appversion) {
	this.appversion = appversion;
}
@Column(name = "dbversion")
public String getDbversion() {
	return dbversion;
}
public void setDbversion(String dbversion) {
	this.dbversion = dbversion;
}
}
