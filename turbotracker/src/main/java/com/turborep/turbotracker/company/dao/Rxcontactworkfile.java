package com.turborep.turbotracker.company.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Rxcontactworkfile generated by hbm2java
 */
@Entity
@Table(name = "rxcontactworkfile", catalog = "")
public class Rxcontactworkfile implements java.io.Serializable {

	private Integer rxContactWorkFileId;
	private Integer rxContactId;
	private Integer rxMasterId;
	private Boolean alreadyExists;
	private Boolean inActive;
	private String title;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String jobPosition;
	private String phone;
	private String cell;
	private String pager;
	private String fax;
	private String email;
	private Byte viewDeleted;
	private String viewName;
	private String viewPhone;
	private Integer createdById;
	private Date createdOn;
	private Integer changedById;
	private Date changedOn;

	public Rxcontactworkfile() {
	}

	public Rxcontactworkfile(Integer rxContactId, Integer rxMasterId,
			Boolean alreadyExists, Boolean inActive, String title,
			String firstName, String middleInitial, String lastName,
			String jobPosition, String phone, String cell, String pager,
			String fax, String email, Byte viewDeleted, String viewName,
			String viewPhone, Integer createdById, Date createdOn,
			Integer changedById, Date changedOn) {
		this.rxContactId = rxContactId;
		this.rxMasterId = rxMasterId;
		this.alreadyExists = alreadyExists;
		this.inActive = inActive;
		this.title = title;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.jobPosition = jobPosition;
		this.phone = phone;
		this.cell = cell;
		this.pager = pager;
		this.fax = fax;
		this.email = email;
		this.viewDeleted = viewDeleted;
		this.viewName = viewName;
		this.viewPhone = viewPhone;
		this.createdById = createdById;
		this.createdOn = createdOn;
		this.changedById = changedById;
		this.changedOn = changedOn;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rxContactWorkFileID", unique = true, nullable = false)
	public Integer getRxContactWorkFileId() {
		return this.rxContactWorkFileId;
	}

	public void setRxContactWorkFileId(Integer rxContactWorkFileId) {
		this.rxContactWorkFileId = rxContactWorkFileId;
	}

	@Column(name = "rxContactID")
	public Integer getRxContactId() {
		return this.rxContactId;
	}

	public void setRxContactId(Integer rxContactId) {
		this.rxContactId = rxContactId;
	}

	@Column(name = "rxMasterID")
	public Integer getRxMasterId() {
		return this.rxMasterId;
	}

	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}

	@Column(name = "AlreadyExists")
	public Boolean getAlreadyExists() {
		return this.alreadyExists;
	}

	public void setAlreadyExists(Boolean alreadyExists) {
		this.alreadyExists = alreadyExists;
	}

	@Column(name = "InActive")
	public Boolean getInActive() {
		return this.inActive;
	}

	public void setInActive(Boolean inActive) {
		this.inActive = inActive;
	}

	@Column(name = "Title", length = 10)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FirstName", length = 20)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MiddleInitial", length = 1)
	public String getMiddleInitial() {
		return this.middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	@Column(name = "LastName", length = 25)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "JobPosition", length = 25)
	public String getJobPosition() {
		return this.jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	@Column(name = "Phone", length = 30)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "Cell", length = 30)
	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	@Column(name = "Pager", length = 30)
	public String getPager() {
		return this.pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}

	@Column(name = "Fax", length = 30)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "EMail", length = 64)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "ViewDeleted")
	public Byte getViewDeleted() {
		return this.viewDeleted;
	}

	public void setViewDeleted(Byte viewDeleted) {
		this.viewDeleted = viewDeleted;
	}

	@Column(name = "ViewName", length = 50)
	public String getViewName() {
		return this.viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Column(name = "ViewPhone", length = 50)
	public String getViewPhone() {
		return this.viewPhone;
	}

	public void setViewPhone(String viewPhone) {
		this.viewPhone = viewPhone;
	}

	@Column(name = "CreatedByID")
	public Integer getCreatedById() {
		return this.createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedOn", length = 0)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "ChangedByID")
	public Integer getChangedById() {
		return this.changedById;
	}

	public void setChangedById(Integer changedById) {
		this.changedById = changedById;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ChangedOn", length = 0)
	public Date getChangedOn() {
		return this.changedOn;
	}

	public void setChangedOn(Date changedOn) {
		this.changedOn = changedOn;
	}

}
