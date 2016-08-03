package com.turborep.turbotracker.finance.dao;

import java.util.Date;

public class Transactionmonitor {

	public Transactionmonitor() {
		// TODO Auto-generated constructor stub
	}

	private String headermsg;
	private String trackingId;
	private String jobStatus;
	private String username;
	private Date timetotriggerd;
	private Object Description;
	
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getTimetotriggerd() {
		return timetotriggerd;
	}
	public void setTimetotriggerd(Date timetotriggerd) {
		this.timetotriggerd = timetotriggerd;
	}

	public String getHeadermsg() {
		return headermsg;
	}
	public void setHeadermsg(String headermsg) {
		this.headermsg = headermsg;
	}
	public Object getDescription() {
		return Description;
	}
	public void setDescription(Object description) {
		this.Description = description;
	}
	
	
	
	
}
