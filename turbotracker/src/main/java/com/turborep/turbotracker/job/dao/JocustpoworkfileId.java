package com.turborep.turbotracker.job.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JocustpoworkfileId generated by hbm2java
 */
@Embeddable
public class JocustpoworkfileId implements java.io.Serializable {

	private Integer joCustPoworkFileId;
	private String ponumber;
	private String description;

	public JocustpoworkfileId() {
	}

	public JocustpoworkfileId(Integer joCustPoworkFileId, String ponumber,
			String description) {
		this.joCustPoworkFileId = joCustPoworkFileId;
		this.ponumber = ponumber;
		this.description = description;
	}

	@Column(name = "joCustPOWorkFileID")
	public Integer getJoCustPoworkFileId() {
		return this.joCustPoworkFileId;
	}

	public void setJoCustPoworkFileId(Integer joCustPoworkFileId) {
		this.joCustPoworkFileId = joCustPoworkFileId;
	}

	@Column(name = "PONumber", length = 20)
	public String getPonumber() {
		return this.ponumber;
	}

	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}

	@Column(name = "Description", length = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof JocustpoworkfileId))
			return false;
		JocustpoworkfileId castOther = (JocustpoworkfileId) other;

		return ((this.getJoCustPoworkFileId() == castOther
				.getJoCustPoworkFileId()) || (this.getJoCustPoworkFileId() != null
				&& castOther.getJoCustPoworkFileId() != null && this
				.getJoCustPoworkFileId().equals(
						castOther.getJoCustPoworkFileId())))
				&& ((this.getPonumber() == castOther.getPonumber()) || (this
						.getPonumber() != null
						&& castOther.getPonumber() != null && this
						.getPonumber().equals(castOther.getPonumber())))
				&& ((this.getDescription() == castOther.getDescription()) || (this
						.getDescription() != null
						&& castOther.getDescription() != null && this
						.getDescription().equals(castOther.getDescription())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getJoCustPoworkFileId() == null ? 0 : this
						.getJoCustPoworkFileId().hashCode());
		result = 37 * result
				+ (getPonumber() == null ? 0 : this.getPonumber().hashCode());
		result = 37
				* result
				+ (getDescription() == null ? 0 : this.getDescription()
						.hashCode());
		return result;
	}

}
