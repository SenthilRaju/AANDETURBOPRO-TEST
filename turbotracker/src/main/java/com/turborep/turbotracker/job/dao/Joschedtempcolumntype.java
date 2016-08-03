package com.turborep.turbotracker.job.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Joschedtempcolumntype generated by hbm2java
 */
@Entity
@Table(name = "joSchedTempColumnType", catalog = "")
public class Joschedtempcolumntype implements java.io.Serializable {

	private int joSchedTempColumnTypeId;
	private String description;

	public Joschedtempcolumntype() {
	}

	public Joschedtempcolumntype(int joSchedTempColumnTypeId) {
		this.joSchedTempColumnTypeId = joSchedTempColumnTypeId;
	}

	public Joschedtempcolumntype(int joSchedTempColumnTypeId, String description) {
		this.joSchedTempColumnTypeId = joSchedTempColumnTypeId;
		this.description = description;
	}

	@Id
	@Column(name = "joSchedTempColumnTypeID", unique = true, nullable = false)
	public int getJoSchedTempColumnTypeId() {
		return this.joSchedTempColumnTypeId;
	}

	public void setJoSchedTempColumnTypeId(int joSchedTempColumnTypeId) {
		this.joSchedTempColumnTypeId = joSchedTempColumnTypeId;
	}

	@Column(name = "Description", length = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
