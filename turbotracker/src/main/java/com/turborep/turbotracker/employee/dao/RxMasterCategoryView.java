package com.turborep.turbotracker.employee.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "rxMasterCategoryView", catalog = "BacheCompany")
public class RxMasterCategoryView implements java.io.Serializable{

	private int rxMasterCategoryViewid;
	private Integer cuAssignmentId0;
	private Integer cuAssignmentId1;
	private Integer cuAssignmentId2;
	private Integer cuAssignmentId3;
	private Integer cuAssignmentId4;
	private String cuAssignmentName0;
	private String cuAssignmentName1;
	private String cuAssignmentName2;
	private String cuAssignmentName3;
	private String cuAssignmentName4;
	

	
	@Id
	@Column(name = "rxMasterCategoryViewID", unique = true, nullable = false)
	public int getRxMasterCategoryViewid() {
		return this.rxMasterCategoryViewid;
	}

	public void setRxMasterCategoryViewid(int rxMasterCategory1id) {
		this.rxMasterCategoryViewid = rxMasterCategory1id;
	}

	@Column(name = "cuAssignmentID0")
	public Integer getCuAssignmentId0() {
		return this.cuAssignmentId0;
	}

	public void setCuAssignmentId0(Integer cuAssignmentId0) {
		this.cuAssignmentId0 = cuAssignmentId0;
	}

	@Column(name = "cuAssignmentID1")
	public Integer getCuAssignmentId1() {
		return this.cuAssignmentId1;
	}

	public void setCuAssignmentId1(Integer cuAssignmentId1) {
		this.cuAssignmentId1 = cuAssignmentId1;
	}

	@Column(name = "cuAssignmentID2")
	public Integer getCuAssignmentId2() {
		return this.cuAssignmentId2;
	}

	public void setCuAssignmentId2(Integer cuAssignmentId2) {
		this.cuAssignmentId2 = cuAssignmentId2;
	}

	@Column(name = "cuAssignmentID3")
	public Integer getCuAssignmentId3() {
		return this.cuAssignmentId3;
	}

	public void setCuAssignmentId3(Integer cuAssignmentId3) {
		this.cuAssignmentId3 = cuAssignmentId3;
	}

	@Column(name = "cuAssignmentID4")
	public Integer getCuAssignmentId4() {
		return this.cuAssignmentId4;
	}

	public void setCuAssignmentId4(Integer cuAssignmentId4) {
		this.cuAssignmentId4 = cuAssignmentId4;
	}
	@Transient
	public String getCuAssignmentName0() {
		return cuAssignmentName0;
	}

	public void setCuAssignmentName0(String cuAssignmentName0) {
		this.cuAssignmentName0 = cuAssignmentName0;
	}
	@Transient
	public String getCuAssignmentName1() {
		return cuAssignmentName1;
	}

	public void setCuAssignmentName1(String cuAssignmentName1) {
		this.cuAssignmentName1 = cuAssignmentName1;
	}
	@Transient
	public String getCuAssignmentName2() {
		return cuAssignmentName2;
	}

	public void setCuAssignmentName2(String cuAssignmentName2) {
		this.cuAssignmentName2 = cuAssignmentName2;
	}
	@Transient
	public String getCuAssignmentName3() {
		return cuAssignmentName3;
	}

	public void setCuAssignmentName3(String cuAssignmentName3) {
		this.cuAssignmentName3 = cuAssignmentName3;
	}
	@Transient
	public String getCuAssignmentName4() {
		return cuAssignmentName4;
	}

	public void setCuAssignmentName4(String cuAssignmentName4) {
		this.cuAssignmentName4 = cuAssignmentName4;
	}

}
