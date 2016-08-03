package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;

public class JobQuoteDetailBean {

	
	private Integer joQuoteDetailID;
	private Integer joQuoteHeaderID;
	private String Product;
	private String ProductNote;
	private String ItemQuantity;
	private String Paragraph;
	private BigDecimal Price;
	private BigDecimal Cost;
	private String inlineNote;
	private String manufacturer;
	private Integer rxManufacturerID;
	private Short veFactoryId;
	private String inLineNoteImage;
	private double position;
	private BigDecimal percentage;
	private BigDecimal mult;
	private Short spec;
	private Byte InActive;
	
	private String itemClassName;
	private String qtyClassName;
	private String paraClassName;
	private String manfcClassName;
	private String specClassName;
	private String multClassName;
	private String priceClassName;
	
	public Integer getJoQuoteDetailID() {
		return joQuoteDetailID;
	}
	public void setJoQuoteDetailID(Integer joQuoteDetailID) {
		this.joQuoteDetailID = joQuoteDetailID;
	}
	public Integer getJoQuoteHeaderID() {
		return joQuoteHeaderID;
	}
	public void setJoQuoteHeaderID(Integer joQuoteHeaderID) {
		this.joQuoteHeaderID = joQuoteHeaderID;
	}
	public String getProduct() {
		return Product;
	}
	public void setProduct(String product) {
		Product = product;
	}
	public String getProductNote() {
		return ProductNote;
	}
	public void setProductNote(String productNote) {
		ProductNote = productNote;
	}
	public String getItemQuantity() {
		return ItemQuantity;
	}
	public void setItemQuantity(String ItemQuantity) {
		this.ItemQuantity = ItemQuantity;
	}
	public String getParagraph() {
		return Paragraph;
	}
	public void setParagraph(String paragraph) {
		Paragraph = paragraph;
	}
	public BigDecimal getPrice() {
		return Price;
	}
	public void setPrice(BigDecimal price) {
		Price = price;
	}
	public BigDecimal getCost() {
		return Cost;
	}
	public void setCost(BigDecimal cost) {
		Cost = cost;
	}
	public String getInlineNote() {
		return inlineNote;
	}
	public void setInlineNote(String inlineNote) {
		this.inlineNote = inlineNote;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getRxManufacturerID() {
		return rxManufacturerID;
	}
	public void setRxManufacturerID(Integer rxManufacturerID) {
		this.rxManufacturerID = rxManufacturerID;
	}
	public Short getVeFactoryId() {
		return veFactoryId;
	}
	public void setVeFactoryId(Short veFactoryId) {
		this.veFactoryId = veFactoryId;
	}
	public String getInLineNoteImage() {
		return inLineNoteImage;
	}
	public void setInLineNoteImage(String inLineNoteImage) {
		this.inLineNoteImage = inLineNoteImage;
	}
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	public BigDecimal getPercentage() {
		return percentage;
	}
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
	public BigDecimal getMult() {
		return mult;
	}
	public void setMult(BigDecimal mult) {
		this.mult = mult;
	}
	public Short getSpec() {
		return spec;
	}
	public void setSpec(Short spec) {
		this.spec = spec;
	}
	public Byte getInActive() {
		return InActive;
	}
	public void setInActive(Byte inActive) {
		InActive = inActive;
	}
	public String getItemClassName() {
		return itemClassName;
	}
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}
	public String getQtyClassName() {
		return qtyClassName;
	}
	public void setQtyClassName(String qtyClassName) {
		this.qtyClassName = qtyClassName;
	}
	public String getParaClassName() {
		return paraClassName;
	}
	public void setParaClassName(String paraClassName) {
		this.paraClassName = paraClassName;
	}
	public String getManfcClassName() {
		return manfcClassName;
	}
	public void setManfcClassName(String manfcClassName) {
		this.manfcClassName = manfcClassName;
	}
	public String getSpecClassName() {
		return specClassName;
	}
	public void setSpecClassName(String specClassName) {
		this.specClassName = specClassName;
	}
	public String getMultClassName() {
		return multClassName;
	}
	public void setMultClassName(String multClassName) {
		this.multClassName = multClassName;
	}
	public String getPriceClassName() {
		return priceClassName;
	}
	public void setPriceClassName(String priceClassName) {
		this.priceClassName = priceClassName;
	}
}
