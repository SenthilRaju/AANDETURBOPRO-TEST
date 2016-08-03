package com.turborep.turbotracker.search.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

@Entity
@Table(name = "search_index", catalog = "")
public class search_index implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(search_index.class);
	
	private int searchId;
	private String entity;
	private String searchText;
	private String resultedTableName;
	private int pk_fields;
	private Date bidDate;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "searchId", unique = true, nullable = false)
	public int getSearchId() {
		return searchId;
	}

	public void setSearchId(int searchId) {
		this.searchId = searchId;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	@Column(name = "searchText")
	public String getSearchText() {
		return searchText;
	}

	public void setResultedTableName(String resultedTableName) {
		this.resultedTableName = resultedTableName;
	}

	@Column(name = "resultedTableName")
	public String getResultedTableName() {
		return resultedTableName;
	}
	
	public void setPk_fields(int pk_fields) {
		this.pk_fields = pk_fields;
	}
	
	@Column(name = "pk_fields")
	public int getPk_fields() {
		return pk_fields;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	@Column(name = "entity")
	public String getEntity() {
		return entity;
	}

	public void setBidDate(Date bidDate) {
		this.bidDate = bidDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "bidDate", length = 0)
	public Date getBidDate() {
		return bidDate;
	}
}
