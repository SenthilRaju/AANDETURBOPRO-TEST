package com.turborep.turbotracker.product.dao;

public class PrDepartmentAccounts {

	private Integer prDepartmentId;
	private String description;
	private Boolean InActive;
	private String revenueAccount;
	private String cgsAccount;
	private Integer coAccountIDSales;
	private Integer coAccountIDCOGS;

	public Integer getCoAccountIDSales() {
		return coAccountIDSales;
	}

	public void setCoAccountIDSales(Integer coAccountIDSales) {
		this.coAccountIDSales = coAccountIDSales;
	}

	public Integer getCoAccountIDCOGS() {
		return coAccountIDCOGS;
	}

	public void setCoAccountIDCOGS(Integer coAccountIDCOGS) {
		this.coAccountIDCOGS = coAccountIDCOGS;
	}

	public PrDepartmentAccounts() {
	}

	public Integer getPrDepartmentId() {
		return prDepartmentId;
	}

	public void setPrDepartmentId(Integer prDepartmentId) {
		this.prDepartmentId = prDepartmentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getInActive() {
		return InActive;
	}

	public void setInActive(Boolean inActive) {
		this.InActive = inActive;
	}

	public String getRevenueAccount() {
		return revenueAccount;
	}

	public void setRevenueAccount(String revenueAccount) {
		this.revenueAccount = revenueAccount;
	}

	public String getCgsAccount() {
		return cgsAccount;
	}

	public void setCgsAccount(String cgsAccount) {
		this.cgsAccount = cgsAccount;
	}


}
