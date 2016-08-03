package com.turborep.turbotracker.job.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Jostatus generated by hbm2java
 */
@Entity
@Table(name = "jostatus", catalog = "")
public class Jostatus implements java.io.Serializable {

	private int joStatusId;
	private String jobStatus;

	public Jostatus() {
	}

	public Jostatus(int joStatusId) {
		this.joStatusId = joStatusId;
	}

	public Jostatus(int joStatusId, String jobStatus) {
		this.joStatusId = joStatusId;
		this.jobStatus = jobStatus;
	}

	@Id
	@Column(name = "joStatusID", unique = true, nullable = false)
	public int getJoStatusId() {
		return this.joStatusId;
	}

	public void setJoStatusId(int joStatusId) {
		this.joStatusId = joStatusId;
	}

	@Column(name = "JobStatus", length = 12)
	public String getJobStatus() {
		return this.jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

}