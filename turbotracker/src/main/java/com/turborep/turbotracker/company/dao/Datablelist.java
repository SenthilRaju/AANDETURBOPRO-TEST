package com.turborep.turbotracker.company.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Datablelist generated by hbm2java
 */
@Entity
@Table(name = "datablelist", catalog = "")
public class Datablelist implements java.io.Serializable {

	private String name;

	public Datablelist() {
	}

	public Datablelist(String name) {
		this.name = name;
	}

	@Id
	@Column(name = "Name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}