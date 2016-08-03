package com.turborep.turbotracker.banking.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="journalentryhistory", catalog="")
public class journalentryhistory implements java.io.Serializable{

	
	private Integer journalentryhistoryID;
	private String reference;
	private String reasondesc;
	private Date createddate;
	private String editordelete;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "journalentryhistoryID", unique = true, nullable = false)
	public Integer getJournalentryhistoryID() {
		return journalentryhistoryID;
	}
	public void setJournalentryhistoryID(Integer journalentryhistoryID) {
		this.journalentryhistoryID = journalentryhistoryID;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getReasondesc() {
		return reasondesc;
	}
	public void setReasondesc(String reasondesc) {
		this.reasondesc = reasondesc;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public String getEditordelete() {
		return editordelete;
	}
	public void setEditordelete(String editordelete) {
		this.editordelete = editordelete;
	}
	
}
