package com.turborep.turbotracker.json;

import java.util.List;

public class CustomResponse {

	/**
	 * Current page of the query
	 */
	private Integer page;
	
	/**
	 * Total pages for the query
	 */
	private int total;
	
	/**
	 * Total number of records for the query
	 */
	private String records;
	
	/**
	 * An array that contains the actual objects
	 */
	private List<?> rows;

	
	public CustomResponse() {
	}
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page2) {
		this.page = page2;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int i) {
		this.total = i;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}
