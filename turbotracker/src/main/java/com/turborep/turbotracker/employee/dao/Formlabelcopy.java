package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Formlabelcopy generated by hbm2java
 */
@Entity
@Table(name = "formlabelcopy", catalog = "")
public class Formlabelcopy implements java.io.Serializable {

	private Integer formHeaderId;
	private Integer formNo;
	private String section;
	private String name;
	private String caption;
	private Boolean hide;
	private Boolean useTopOffset;
	private Integer locTop;
	private Integer locLeft;
	private Integer sizeHeight;
	private Integer sizeWidth;
	private String style;

	public Formlabelcopy() {
	}

	public Formlabelcopy(Integer formNo, String section, String name,
			String caption, Boolean hide, Boolean useTopOffset, Integer locTop,
			Integer locLeft, Integer sizeHeight, Integer sizeWidth, String style) {
		this.formNo = formNo;
		this.section = section;
		this.name = name;
		this.caption = caption;
		this.hide = hide;
		this.useTopOffset = useTopOffset;
		this.locTop = locTop;
		this.locLeft = locLeft;
		this.sizeHeight = sizeHeight;
		this.sizeWidth = sizeWidth;
		this.style = style;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "FormHeaderID", unique = true, nullable = false)
	public Integer getFormHeaderId() {
		return this.formHeaderId;
	}

	public void setFormHeaderId(Integer formHeaderId) {
		this.formHeaderId = formHeaderId;
	}

	@Column(name = "FormNo")
	public Integer getFormNo() {
		return this.formNo;
	}

	public void setFormNo(Integer formNo) {
		this.formNo = formNo;
	}

	@Column(name = "Section", length = 50)
	public String getSection() {
		return this.section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	@Column(name = "Name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Caption")
	public String getCaption() {
		return this.caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Column(name = "Hide")
	public Boolean getHide() {
		return this.hide;
	}

	public void setHide(Boolean hide) {
		this.hide = hide;
	}

	@Column(name = "UseTopOffset")
	public Boolean getUseTopOffset() {
		return this.useTopOffset;
	}

	public void setUseTopOffset(Boolean useTopOffset) {
		this.useTopOffset = useTopOffset;
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

	@Column(name = "Style")
	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
