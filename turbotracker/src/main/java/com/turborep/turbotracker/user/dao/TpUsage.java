package com.turborep.turbotracker.user.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TPUsage",catalog="")
public class TpUsage {
	
	private Integer tpUsageID;
	private Date datetime;
	private String screen;
	private String action;
	private String logLevel;
	private Integer userID;
	private String description;
	
	public TpUsage(){}
	public TpUsage(Date datetime,String screen,String action,String logLevel,Integer userID,String description){
		this.datetime=datetime;
		this.screen=screen;
		this.action=action;
		this.logLevel=logLevel;
		this.userID=userID;
		this.description=description;
	}
	
	@Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="tpUsageID", unique=true, nullable=false)
	public Integer getTpUsageID() {
		return tpUsageID;
	}
	public void setTpUsageID(Integer tpUsageID) {
		this.tpUsageID = tpUsageID;
	}
	
	@Column(name="createdDate", nullable=false)
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	 @Column(name="screen")
	public String getScreen() {
		return screen;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	
	 @Column(name="action")
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	 @Column(name="logLevel")
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	
	 @Column(name="userID")
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
	 @Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
