package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Epcoliability generated by hbm2java
 */
@Entity
@Table(name = "epcoliability", catalog = "")
public class Epcoliability implements java.io.Serializable {

	private Integer epCoLiabilityId;
	private Boolean inActive;
	private String description;
	private boolean builtIn;
	private Byte calculationType;
	private BigDecimal amount;
	private BigDecimal maximum;
	private Integer sortOrder;

	public Epcoliability() {
	}

	public Epcoliability(boolean builtIn) {
		this.builtIn = builtIn;
	}

	public Epcoliability(Boolean inActive, String description, boolean builtIn,
			Byte calculationType, BigDecimal amount, BigDecimal maximum,
			Integer sortOrder) {
		this.inActive = inActive;
		this.description = description;
		this.builtIn = builtIn;
		this.calculationType = calculationType;
		this.amount = amount;
		this.maximum = maximum;
		this.sortOrder = sortOrder;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "epCoLiabilityID", unique = true, nullable = false)
	public Integer getEpCoLiabilityId() {
		return this.epCoLiabilityId;
	}

	public void setEpCoLiabilityId(Integer epCoLiabilityId) {
		this.epCoLiabilityId = epCoLiabilityId;
	}

	@Column(name = "InActive")
	public Boolean getInActive() {
		return this.inActive;
	}

	public void setInActive(Boolean inActive) {
		this.inActive = inActive;
	}

	@Column(name = "Description", length = 20)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "BuiltIn", nullable = false)
	public boolean isBuiltIn() {
		return this.builtIn;
	}

	public void setBuiltIn(boolean builtIn) {
		this.builtIn = builtIn;
	}

	@Column(name = "CalculationType")
	public Byte getCalculationType() {
		return this.calculationType;
	}

	public void setCalculationType(Byte calculationType) {
		this.calculationType = calculationType;
	}

	@Column(name = "Amount", scale = 4)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "Maximum", scale = 4)
	public BigDecimal getMaximum() {
		return this.maximum;
	}

	public void setMaximum(BigDecimal maximum) {
		this.maximum = maximum;
	}

	@Column(name = "SortOrder")
	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
