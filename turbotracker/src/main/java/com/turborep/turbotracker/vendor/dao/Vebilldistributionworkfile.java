package com.turborep.turbotracker.vendor.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Vebilldistributionworkfile generated by hbm2java
 */
@Entity
@Table(name = "vebilldistributionworkfile", catalog = "")
public class Vebilldistributionworkfile implements java.io.Serializable {

	private Integer veBillDistributionWorkFileId;
	private Integer coExpenseAccountId;
	private BigDecimal expenseAmount;

	public Vebilldistributionworkfile() {
	}

	public Vebilldistributionworkfile(Integer coExpenseAccountId,
			BigDecimal expenseAmount) {
		this.coExpenseAccountId = coExpenseAccountId;
		this.expenseAmount = expenseAmount;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "veBillDistributionWorkFileID", unique = true, nullable = false)
	public Integer getVeBillDistributionWorkFileId() {
		return this.veBillDistributionWorkFileId;
	}

	public void setVeBillDistributionWorkFileId(
			Integer veBillDistributionWorkFileId) {
		this.veBillDistributionWorkFileId = veBillDistributionWorkFileId;
	}

	@Column(name = "coExpenseAccountID")
	public Integer getCoExpenseAccountId() {
		return this.coExpenseAccountId;
	}

	public void setCoExpenseAccountId(Integer coExpenseAccountId) {
		this.coExpenseAccountId = coExpenseAccountId;
	}

	@Column(name = "ExpenseAmount", scale = 4)
	public BigDecimal getExpenseAmount() {
		return this.expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

}
