package com.turborep.turbotracker.vendor.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "veFactory", catalog = "")
public class VeFactory {

	private int veFactoryID;
	private String description;
	private boolean inActive;
	private int rxMasterId;
	
	public VeFactory(){
		
	}
	
	public VeFactory(int veFactoryID, String description, boolean inActive) {
		super();
		this.setVeFactoryID(veFactoryID);
		this.description = description;
		this.inActive = inActive;
	}
	
	@Id
	@Column(name = "veFactoryID", unique = true, nullable = false)
	public int getVeFactoryID() {
		return veFactoryID;
	}
	
	public void setVeFactoryID(int veFactoryID) {
		this.veFactoryID = veFactoryID;
	}
	
	@Column(name = "Description", length = 50)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "InActive", nullable = false)
	public boolean isInActive() {
		return inActive;
	}
	public void setInActive(boolean inActive) {
		this.inActive = inActive;
	}

	public int getRxMasterId() {
		return rxMasterId;
	}

	public void setRxMasterId(int rxMasterId) {
		this.rxMasterId = rxMasterId;
	}

	
}
