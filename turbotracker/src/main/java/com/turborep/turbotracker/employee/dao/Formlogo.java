package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Formlogo generated by hbm2java
 */
@Entity
@Table(name = "formlogo", catalog = "")
public class Formlogo implements java.io.Serializable {

	private Integer formLogoId;
	private Integer formNo;
	private String pictureLocation;
	private Integer locTop;
	private Integer locLeft;
	private Integer sizeHeight;
	private Integer sizeWidth;
	private Integer pictureAlignment;
	private Integer sizeMode;

	public Formlogo() {
	}

	public Formlogo(Integer formNo, String pictureLocation, Integer locTop,
			Integer locLeft, Integer sizeHeight, Integer sizeWidth,
			Integer pictureAlignment, Integer sizeMode) {
		this.formNo = formNo;
		this.pictureLocation = pictureLocation;
		this.locTop = locTop;
		this.locLeft = locLeft;
		this.sizeHeight = sizeHeight;
		this.sizeWidth = sizeWidth;
		this.pictureAlignment = pictureAlignment;
		this.sizeMode = sizeMode;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "FormLogoID", unique = true, nullable = false)
	public Integer getFormLogoId() {
		return this.formLogoId;
	}

	public void setFormLogoId(Integer formLogoId) {
		this.formLogoId = formLogoId;
	}

	@Column(name = "FormNo")
	public Integer getFormNo() {
		return this.formNo;
	}

	public void setFormNo(Integer formNo) {
		this.formNo = formNo;
	}

	@Column(name = "PictureLocation")
	public String getPictureLocation() {
		return this.pictureLocation;
	}

	public void setPictureLocation(String pictureLocation) {
		this.pictureLocation = pictureLocation;
	}

	@Column(name = "locTop")
	public Integer getLocTop() {
		return this.locTop;
	}

	public void setLocTop(Integer locTop) {
		this.locTop = locTop;
	}

	@Column(name = "locLeft")
	public Integer getLocLeft() {
		return this.locLeft;
	}

	public void setLocLeft(Integer locLeft) {
		this.locLeft = locLeft;
	}

	@Column(name = "sizeHeight")
	public Integer getSizeHeight() {
		return this.sizeHeight;
	}

	public void setSizeHeight(Integer sizeHeight) {
		this.sizeHeight = sizeHeight;
	}

	@Column(name = "sizeWidth")
	public Integer getSizeWidth() {
		return this.sizeWidth;
	}

	public void setSizeWidth(Integer sizeWidth) {
		this.sizeWidth = sizeWidth;
	}

	@Column(name = "PictureAlignment")
	public Integer getPictureAlignment() {
		return this.pictureAlignment;
	}

	public void setPictureAlignment(Integer pictureAlignment) {
		this.pictureAlignment = pictureAlignment;
	}

	@Column(name = "SizeMode")
	public Integer getSizeMode() {
		return this.sizeMode;
	}

	public void setSizeMode(Integer sizeMode) {
		this.sizeMode = sizeMode;
	}

}
