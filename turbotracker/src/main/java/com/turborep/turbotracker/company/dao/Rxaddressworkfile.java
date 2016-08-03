package com.turborep.turbotracker.company.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Rxaddressworkfile generated by hbm2java
 */
@Entity
@Table(name = "rxaddressworkfile", catalog = "")
public class Rxaddressworkfile implements java.io.Serializable {

	private RxaddressworkfileId id;

	public Rxaddressworkfile() {
	}

	public Rxaddressworkfile(RxaddressworkfileId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "rxAddressWorkFileId", column = @Column(name = "rxAddressWorkFileID", nullable = false)),
			@AttributeOverride(name = "rxAddressId", column = @Column(name = "rxAddressID")),
			@AttributeOverride(name = "rxMasterId", column = @Column(name = "rxMasterID")),
			@AttributeOverride(name = "alreadyExists", column = @Column(name = "AlreadyExists")),
			@AttributeOverride(name = "isDeleted", column = @Column(name = "IsDeleted")),
			@AttributeOverride(name = "inActive", column = @Column(name = "InActive")),
			@AttributeOverride(name = "address1", column = @Column(name = "Address1", length = 40)),
			@AttributeOverride(name = "address2", column = @Column(name = "Address2", length = 40)),
			@AttributeOverride(name = "city", column = @Column(name = "City", length = 30)),
			@AttributeOverride(name = "state", column = @Column(name = "State", length = 2)),
			@AttributeOverride(name = "zip", column = @Column(name = "Zip", length = 10)),
			@AttributeOverride(name = "isStreet", column = @Column(name = "IsStreet")),
			@AttributeOverride(name = "isMailing", column = @Column(name = "IsMailing")),
			@AttributeOverride(name = "isBillTo", column = @Column(name = "IsBillTo")),
			@AttributeOverride(name = "isShipTo", column = @Column(name = "IsShipTo")),
			@AttributeOverride(name = "coTaxTerritoryId", column = @Column(name = "coTaxTerritoryID")) })
	public RxaddressworkfileId getId() {
		return this.id;
	}

	public void setId(RxaddressworkfileId id) {
		this.id = id;
	}

}
