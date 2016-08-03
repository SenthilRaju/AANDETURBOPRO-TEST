package com.turborep.turbotracker.sales.dao;

public class SalesRepBean {
	
	private int UserLoginID;
	private int LoginName;
	private String Salesrep;
	private int salesRepId;
	private int rxMasterID;
	
	private String sortcolumn;
	private String sortby;
	
	public int getUserLoginID(){
		return UserLoginID;
	}
	public void setUserLoginID(int UserLoginID){
		this.UserLoginID=UserLoginID;
	}
	
	public int getLoginName(){
		return LoginName;
	}
	public void setLoginName(int LoginName){
		this.LoginName=LoginName;
	}
	public String getSalesrep(){
		return Salesrep;
	}
	public void setSalesrep(String Salesrep){
		this.Salesrep=Salesrep;
	}
	public int getSalesRepId() {
		return salesRepId;
	}
	public void setSalesRepId(int salesRepId) {
		this.salesRepId = salesRepId;
	}
	public String getSortcolumn() {
		return sortcolumn;
	}
	public void setSortcolumn(String sortcolumn) {
		this.sortcolumn = sortcolumn;
	}
	public String getSortby() {
		return sortby;
	}
	public void setSortby(String sortby) {
		this.sortby = sortby;
	}
	public int getRxMasterID() {
		return rxMasterID;
	}
	public void setRxMasterID(int rxMasterID) {
		this.rxMasterID = rxMasterID;
	}
	
}
