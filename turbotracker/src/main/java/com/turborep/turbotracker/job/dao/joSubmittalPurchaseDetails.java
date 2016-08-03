package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;
import java.util.Date;

public class joSubmittalPurchaseDetails {

	private String name;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String tag;
	private String PONumber;
	private String customerPONumber;
	private BigDecimal subtotal;
	private BigDecimal taxTotal;
	private BigDecimal freight;
	private BigDecimal taxRate;
	private String locationName;
	private String location1;
	private String location2;
	private String locationCity;
	private String locationState;
	private String locationZip;
	private String manufacturer;
	private String initials;
	private Byte IsBillTo;
	private Byte IsShipTo;
	private Integer veFreightChargesID;
	private String description;
	private Integer veShipViaID;
	private String shipname;
	private String specialInstructions;
	private String dateWanted;
	private Date orderDate;
	private Integer vePOID;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return the pONumber
	 */
	public String getPONumber() {
		return PONumber;
	}
	/**
	 * @param pONumber the pONumber to set
	 */
	public void setPONumber(String pONumber) {
		PONumber = pONumber;
	}
	/**
	 * @return the customerPONumber
	 */
	public String getCustomerPONumber() {
		return customerPONumber;
	}
	/**
	 * @param customerPONumber the customerPONumber to set
	 */
	public void setCustomerPONumber(String customerPONumber) {
		this.customerPONumber = customerPONumber;
	}
	/**
	 * @return the subtotal
	 */
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	/**
	 * @return the taxTotal
	 */
	public BigDecimal getTaxTotal() {
		return taxTotal;
	}
	/**
	 * @param taxTotal the taxTotal to set
	 */
	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}
	/**
	 * @return the freight
	 */
	public BigDecimal getFreight() {
		return freight;
	}
	/**
	 * @param freight the freight to set
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	/**
	 * @return the taxRate
	 */
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	/**
	 * @param taxRate the taxRate to set
	 */
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * @return the location1
	 */
	public String getLocation1() {
		return location1;
	}
	/**
	 * @param location1 the location1 to set
	 */
	public void setLocation1(String location1) {
		this.location1 = location1;
	}
	/**
	 * @return the location2
	 */
	public String getLocation2() {
		return location2;
	}
	/**
	 * @param location2 the location2 to set
	 */
	public void setLocation2(String location2) {
		this.location2 = location2;
	}
	/**
	 * @return the locationCity
	 */
	public String getLocationCity() {
		return locationCity;
	}
	/**
	 * @param locationCity the locationCity to set
	 */
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	/**
	 * @return the locationState
	 */
	public String getLocationState() {
		return locationState;
	}
	/**
	 * @param locationState the locationState to set
	 */
	public void setLocationState(String locationState) {
		this.locationState = locationState;
	}
	/**
	 * @return the locationZip
	 */
	public String getLocationZip() {
		return locationZip;
	}
	/**
	 * @param locationZip the locationZip to set
	 */
	public void setLocationZip(String locationZip) {
		this.locationZip = locationZip;
	}
	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}
	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	/**
	 * @return the initials
	 */
	public String getInitials() {
		return initials;
	}
	/**
	 * @param initials the initials to set
	 */
	public void setInitials(String initials) {
		this.initials = initials;
	}
	/**
	 * @return the isBillTo
	 */
	public Byte getIsBillTo() {
		return IsBillTo;
	}
	/**
	 * @param isBillTo the isBillTo to set
	 */
	public void setIsBillTo(Byte isBillTo) {
		this.IsBillTo = isBillTo;
	}
	/**
	 * @return the isShipTo
	 */
	public Byte getIsShipTo() {
		return IsShipTo;
	}
	/**
	 * @param isShipTo the isShipTo to set
	 */
	public void setIsShipTo(Byte isShipTo) {
		this.IsShipTo = isShipTo;
	}
	/**
	 * @return the veFreightChargesID
	 */
	public Integer getVeFreightChargesID() {
		return veFreightChargesID;
	}
	/**
	 * @param veFreightChargesID the veFreightChargesID to set
	 */
	public void setVeFreightChargesID(Integer veFreightChargesID) {
		this.veFreightChargesID = veFreightChargesID;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the veShipViaID
	 */
	public Integer getVeShipViaID() {
		return veShipViaID;
	}
	/**
	 * @param veShipViaID the veShipViaID to set
	 */
	public void setVeShipViaID(Integer veShipViaID) {
		this.veShipViaID = veShipViaID;
	}
	/**
	 * @return the specialInstructions
	 */
	public String getSpecialInstructions() {
		return specialInstructions;
	}
	/**
	 * @param specialInstructions the specialInstructions to set
	 */
	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	/**
	 * @return the dateWanted
	 */
	public String getDateWanted() {
		return dateWanted;
	}
	/**
	 * @param dateWanted the dateWanted to set
	 */
	public void setDateWanted(String dateWanted) {
		this.dateWanted = dateWanted;
	}
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the vePOID
	 */
	public Integer getVePOID() {
		return vePOID;
	}
	/**
	 * @param vePOID the vePOID to set
	 */
	public void setVePOID(Integer vePOID) {
		this.vePOID = vePOID;
	}
	/**
	 * @return the shipname
	 */
	public String getShipname() {
		return shipname;
	}
	/**
	 * @param shipname the shipname to set
	 */
	public void setShipname(String shipname) {
		this.shipname = shipname;
	}
}