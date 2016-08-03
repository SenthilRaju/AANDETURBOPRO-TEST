package com.turborep.turbotracker.company.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "chSegments", catalog = "")
public class ChSegments {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "segmentID", unique = true, nullable = false)
	private int segmentid;
	@Column(name = "segmentsName")
	private String segmentsName;	
	@Column(name = "requiredStatus")
	private int requiredstatus;	
	@Column(name = "digitsAllowed")
	private int digitsallowed;
	
	
	
	public int getSegmentid() {
		return segmentid;
	}
	public void setSegmentid(int segmentid) {
		this.segmentid = segmentid;
	}
	public String getSegmentsName() {
		return segmentsName;
	}
	public void setSegmentsName(String segmentsName) {
		this.segmentsName = segmentsName;
	}
	public int getRequiredstatus() {
		return requiredstatus;
	}
	public void setRequiredstatus(int requiredstatus) {
		this.requiredstatus = requiredstatus;
	}
	public int getDigitsallowed() {
		return digitsallowed;
	}
	public void setDigitsallowed(int digitsallowed) {
		this.digitsallowed = digitsallowed;
	}	

}
