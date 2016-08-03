package com.turborep.turbotracker.job.dao;

import java.math.BigInteger;

/**
 * Used as bean class to retrieve jobs list module
 * @author SYSVINE\vish_pepala
 */
public class JobCustomerBean {

	private int			joMasterId;						/** joMaster id				**/
	private String	jobNumber;
	private String	locationName;					/**	job location name 		**/
	private String	locationCity;					/**	job location city 		**/
	private String	Initials;						/**	UserLoginClone.Initials	**/
	private String	jobStatus;						/** jobStatus				**/
	private String	customserName;					/**	rxMaster.Name			**/
	private String	CustomerPONumber;
	private String	Code;
	private String	SourceReport1;
	private String	bookedDate;
	private String	contractAmount;
	private String 	estimatedCost;
	
	private String	Description;						/** job name **/
	private String 	bidDate;
	private String 	division;
	private String 	takeoff;
	private String 	quoteBy;
	private String 	bidList;
	private short 	bidStatus;
	private BigInteger  bidListCount;
	
	public int getJoMasterId() {
		return joMasterId;
	}
	public void setJoMasterId(int joMasterId) {
		this.joMasterId = joMasterId;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String obj) {
		this.jobNumber = obj;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationCity() {
		return locationCity;
	}
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getInitials() {
		return Initials;
	}
	public void setInitials(String initials) {
		Initials = initials;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getCustomerName() {
		return customserName;
	}
	public void setCustomerName(String name) {
		this.customserName = name;
	}
	public String getCustomerPONumber() {
		return CustomerPONumber;
	}
	public void setCustomerPONumber(String customerPONumber) {
		CustomerPONumber = customerPONumber;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getSourceReport1() {
		return SourceReport1;
	}
	public void setSourceReport1(String sourceReport1) {
		SourceReport1 = sourceReport1;
	}
	public String getBookedDate() {
		return bookedDate;
	}
	public void setBookedDate(String bookedDate) {
		this.bookedDate = bookedDate;
	}
	public String getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(String obj) {
		this.contractAmount = String.valueOf(obj);
	}
	public String getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(String estimatedCost) {
		this.estimatedCost = String.valueOf(estimatedCost);
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getBidDate() {
		return bidDate;
	}
	public void setBidDate(String bidDate) {
		this.bidDate = bidDate;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getTakeoff() {
		return takeoff;
	}
	public void setTakeoff(String takeoff) {
		this.takeoff = takeoff;
	}
	public String getQuoteBy() {
		return quoteBy;
	}
	public void setQuoteBy(String quoteBy) {
		this.quoteBy = quoteBy;
	}
	public String getBidList() {
		return bidList;
	}
	public void setBidList(String bidList) {
		this.bidList = bidList;
	}
	public BigInteger getBidListCount() {
		return bidListCount;
	}
	public void setBidListCount(BigInteger bidListCount) {
		this.bidListCount = bidListCount;
	}
	public short getBidStatus() {
		return bidStatus;
	}
	public void setBidStatus(short bidStatus) {
		this.bidStatus = bidStatus;
	}
}
