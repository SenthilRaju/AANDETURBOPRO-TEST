package com.turborep.turbotracker.job.dao;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "jobHistory", catalog = "")
public class JobHistory implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer jobHistoryID;
	private String jobNumber;
	private String jobName;
	private Date jobOpenedDate;
	private String jobOpenedDateStr;
	private String jobBidDateStr;
	private Date bidDate;
	private Integer joMasterID;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "jobHistoryID", unique = true, nullable = false)
	public Integer getJobHistoryID() {
		return jobHistoryID;
	}
	public void setJobHistoryID(Integer jobHistoryID) {
		this.jobHistoryID = jobHistoryID;
	}
	
	@Column(name = "jobNumber", length = 45)
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "jobName", length = 100)
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = "`"+jobName+"`";
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "jobOpenedDate", length = 0)
	public Date getJobOpenedDate() {
		return jobOpenedDate;
	}
	public void setJobOpenedDate(Date jobOpenedDate) {
		this.jobOpenedDate = jobOpenedDate;
	}
	@Transient
	public String getJobOpenedDateStr() {
		return jobOpenedDateStr;
	}
	public void setJobOpenedDateStr(String jobOpenedDateStr) {
		this.jobOpenedDateStr = jobOpenedDateStr;
	}
	
	@Transient
	public String getJobBidDateStr() {
		return jobBidDateStr;
	}
	public void setJobBidDateStr(String jobBidDateStr) {
		this.jobBidDateStr = jobBidDateStr;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BidDate", length = 0)
	public Date getBidDate() {
		return bidDate;
	}
	public void setBidDate(Date date) {
		this.bidDate = date;
	}
	@Column(name = "joMasterID")
	public Integer getJoMasterID() {
		return joMasterID;
	}
	public void setJoMasterID(Integer joMasterID) {
		this.joMasterID = joMasterID;
	}
}
