package com.turborep.turbotracker.job.dao;


public class joQuoteDetailPosition {

	private Integer selectedRowID;
	private double selectedPositionDetailID; 
	private Integer selectedQuoteDetailID;
	private Integer selectedJoQuoteHeaderID;
	private Integer abovePositionRowID;
	private double abovePositionDetailID;
	private Integer aboveQuoteDetailID;
	
	public Integer getSelectedQuoteDetailID() {
		return selectedQuoteDetailID;
	}
	public void setSelectedQuoteDetailID(Integer selectedQuoteDetailID) {
		this.selectedQuoteDetailID = selectedQuoteDetailID;
	}
	public Integer getSelectedJoQuoteHeaderID() {
		return selectedJoQuoteHeaderID;
	}
	public void setSelectedJoQuoteHeaderID(Integer selectedJoQuoteHeaderID) {
		this.selectedJoQuoteHeaderID = selectedJoQuoteHeaderID;
	}
	public Integer getAbovePositionRowID() {
		return abovePositionRowID;
	}
	public void setAbovePositionRowID(Integer abovePositionRowID) {
		this.abovePositionRowID = abovePositionRowID;
	}
	public Integer getAboveQuoteDetailID() {
		return aboveQuoteDetailID;
	}
	public void setAboveQuoteDetailID(Integer aboveQuoteDetailID) {
		this.aboveQuoteDetailID = aboveQuoteDetailID;
	}
	public Integer getSelectedRowID() {
		return selectedRowID;
	}
	public void setSelectedRowID(Integer selectedRowID) {
		this.selectedRowID = selectedRowID;
	}
	public double getSelectedPositionDetailID() {
		return selectedPositionDetailID;
	}
	public void setSelectedPositionDetailID(double selectedPositionDetailID) {
		this.selectedPositionDetailID = selectedPositionDetailID;
	}
	public double getAbovePositionDetailID() {
		return abovePositionDetailID;
	}
	public void setAbovePositionDetailID(double abovePositionDetailID) {
		this.abovePositionDetailID = abovePositionDetailID;
	}
	
}