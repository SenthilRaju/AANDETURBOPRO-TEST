package com.turborep.turbotracker.search.dao;

public class SearchBean {
	
	private int searchId;
	private String entity = "job";
	private String rolodexEntity = "vendors,customers,employeelist";
	private String searchText;
	private String resultedTableName;
	private int pk_fields;
	private int createdByID;
	private int changedByID;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getSearchText() {
		return searchText;
	}

	public int getSearchId() {
		return searchId;
	}

	public void setSearchId(int searchId) {
		this.searchId = searchId;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getResultedTableName() {
		return resultedTableName;
	}

	public void setResultedTableName(String resultedTableName) {
		this.resultedTableName = resultedTableName;
	}

	public int getPk_fields() {
		return pk_fields;
	}

	public void setPk_fields(int pk_fields) {
		this.pk_fields = pk_fields;
	}

	public String getRolodexEntity() {
		return rolodexEntity;
	}

	public void setRolodexEntity(String rolodexEntity) {
		this.rolodexEntity = rolodexEntity;
	}

	public int getCreatedByID() {
		return createdByID;
	}

	public void setCreatedByID(int createdByID) {
		this.createdByID = createdByID;
	}

	public int getChangedByID() {
		return changedByID;
	}

	public void setChangedByID(int changedByID) {
		this.changedByID = changedByID;
	}

}
