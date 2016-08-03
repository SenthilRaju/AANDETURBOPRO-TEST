package com.turborep.turbotracker.job.dao;

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
 * Josubmittalheader generated by hbm2java
 */
@Entity
@Table(name = "joSubmittalHeader", catalog = "")
public class Josubmittalheader implements java.io.Serializable {

	private Integer joSubmittalHeaderId;
	private Integer createdById;
	private Date createdOn;
	private Integer changedById;
	private Date changedOn;
	private Integer joMasterId;
	private Byte revision;
	private Integer submittalById;
	private Date submittalDate;
	private String copies;
	private String revisionNote;
	private String remarkNote;
	private Date planDate;
	private String addendum;
	private Integer signedById;
	private boolean forRecordOnly;

	public Josubmittalheader() {
	}

	public Josubmittalheader(boolean forRecordOnly) {
		this.forRecordOnly = forRecordOnly;
	}

	public Josubmittalheader(Integer createdById, Date createdOn,
			Integer changedById, Date changedOn, Integer joMasterId,
			Byte revision, Integer submittalById, Date submittalDate,
			String copies, String revisionNote, String remarkNote,
			Date planDate, String addendum, Integer signedById,
			boolean forRecordOnly) {
		this.createdById = createdById;
		this.createdOn = createdOn;
		this.changedById = changedById;
		this.changedOn = changedOn;
		this.joMasterId = joMasterId;
		this.revision = revision;
		this.submittalById = submittalById;
		this.submittalDate = submittalDate;
		this.copies = copies;
		this.revisionNote = revisionNote;
		this.remarkNote = remarkNote;
		this.planDate = planDate;
		this.addendum = addendum;
		this.signedById = signedById;
		this.forRecordOnly = forRecordOnly;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "joSubmittalHeaderID", unique = true, nullable = false)
	public Integer getJoSubmittalHeaderId() {
		return this.joSubmittalHeaderId;
	}

	public void setJoSubmittalHeaderId(Integer joSubmittalHeaderId) {
		this.joSubmittalHeaderId = joSubmittalHeaderId;
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

	@Column(name = "joMasterID")
	public Integer getJoMasterId() {
		return this.joMasterId;
	}

	public void setJoMasterId(Integer joMasterId) {
		this.joMasterId = joMasterId;
	}

	@Column(name = "Revision")
	public Byte getRevision() {
		return this.revision;
	}

	public void setRevision(Byte revision) {
		this.revision = revision;
	}

	@Column(name = "SubmittalByID")
	public Integer getSubmittalById() {
		return this.submittalById;
	}

	public void setSubmittalById(Integer submittalById) {
		this.submittalById = submittalById;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SubmittalDate", length = 0)
	public Date getSubmittalDate() {
		return this.submittalDate;
	}

	public void setSubmittalDate(Date submittalDate) {
		this.submittalDate = submittalDate;
	}

	@Column(name = "Copies", length = 6)
	public String getCopies() {
		return this.copies;
	}

	public void setCopies(String copies) {
		this.copies = copies;
	}

	@Column(name = "RevisionNote")
	public String getRevisionNote() {
		return this.revisionNote;
	}

	public void setRevisionNote(String revisionNote) {
		this.revisionNote = revisionNote;
	}

	@Column(name = "RemarkNote")
	public String getRemarkNote() {
		return this.remarkNote;
	}

	public void setRemarkNote(String remarkNote) {
		this.remarkNote = remarkNote;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PlanDate", length = 0)
	public Date getPlanDate() {
		return this.planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	@Column(name = "Addendum", length = 1)
	public String getAddendum() {
		return this.addendum;
	}

	public void setAddendum(String addendum) {
		this.addendum = addendum;
	}

	@Column(name = "SignedByID")
	public Integer getSignedById() {
		return this.signedById;
	}

	public void setSignedById(Integer signedById) {
		this.signedById = signedById;
	}

	@Column(name = "ForRecordOnly", nullable = false)
	public boolean isForRecordOnly() {
		return this.forRecordOnly;
	}

	public void setForRecordOnly(boolean forRecordOnly) {
		this.forRecordOnly = forRecordOnly;
	}

}