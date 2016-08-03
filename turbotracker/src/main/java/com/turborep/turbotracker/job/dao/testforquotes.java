package com.turborep.turbotracker.job.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Jostatus generated by hbm2java
 */
@Entity
@Table(name = "testforquotes", catalog = "")
public class testforquotes implements java.io.Serializable {

	
	private int id;
	private int type;
	private String textbox;
	private String texteditor;
	private BigDecimal  sellprice;
	

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Column(name = "textbox")
	public String getTextbox() {
		return textbox;
	}

	public void setTextbox(String textbox) {
		this.textbox = textbox;
	}
	
	@Column(name = "texteditor")
	public String getTexteditor() {
		return texteditor;
	}

	public void setTexteditor(String texteditor) {
		this.texteditor = texteditor;
	}
	
	@Column(name = "sellprice")
	public BigDecimal getSellprice() {
		return sellprice;
	}

	public void setSellprice(BigDecimal sellprice) {
		this.sellprice = sellprice;
	}
	
	
	
	

}