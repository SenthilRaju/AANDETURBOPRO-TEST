package com.turborep.turbotracker.job.dao;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="joQuoteTemplateProperties")
public class JoQuoteTemplateProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int joQuotePropertiesID;

	@Column(nullable=false)
	private byte displayCost;

	@Column(nullable=false)
	private byte displayManufacturer;

	@Column(nullable=false)
	private byte displayMult;

	@Column(nullable=false)
	private byte displayParagraph;

	@Column(nullable=false)
	private byte displayPrice;

	@Column(nullable=false, length=45)
	private byte displayQuantity;

	@Column(nullable=false)
	private byte displaySpec;

	@Column(nullable=false)
	private byte printCost;

	@Column(nullable=false)
	private byte printManufacturer;

	@Column(nullable=false)
	private byte printMult;

	@Column(nullable=false)
	private byte printParagraph;

	@Column(nullable=false)
	private byte printPrice;

	@Column(nullable=false)
	private byte printQuantity;

	@Column(nullable=false)
	private byte printSpec;
	
	@Column(nullable=false)
	private byte underlineCost;

	@Column(nullable=false)
	private byte underlineManufacturer;

	@Column(nullable=false)
	private byte underlineMult;

	@Column(nullable=false)
	private byte underlineParagraph;

	@Column(nullable=false)
	private byte underlinePrice;

	@Column(nullable=false)
	private byte underlineQuantity;

	@Column(nullable=false)
	private byte underlineSpec;
	@Column(nullable=false)
	private byte boldCost;

	@Column(nullable=false)
	private byte boldManufacturer;

	@Column(nullable=false)
	private byte boldMult;

	@Column(nullable=false)
	private byte boldParagraph;

	@Column(nullable=false)
	private byte boldPrice;

	@Column(nullable=false)
	private byte boldQuantity;

	@Column(nullable=false)
	private byte boldSpec;	
	
	@Column(nullable=false)
	private byte displayItem;
	
	@Column(nullable=false)
	private byte printItem;
	
	@Column(nullable=false)
	private byte underlineItem;
	
	@Column(nullable=false)
	private byte boldItem;
	
	@Column(nullable=false)
	private byte displayHeader;
	
	@Column(nullable=false)
	private byte printHeader;
	
	@Column(nullable=false)
	private byte underlineHeader;
	
	@Column(nullable=false)
	private byte boldHeader;

	@Column(nullable=false)
	private int userLoginID;
	
	@Column(nullable=false)
	private byte notesFullWidth;
	
	@Column(nullable=false)
	private byte lineNumbers;
	
	@Column(nullable=false)
	private byte printTotal;
	
	@Column(nullable=false)
	private byte hidePrice;

	public JoQuoteTemplateProperties() {	}

	public int getJoQuotePropertiesID() {
		return this.joQuotePropertiesID;
	}

	public void setJoQuotePropertiesID(int joQuotePropertiesID) {
		this.joQuotePropertiesID = joQuotePropertiesID;
	}

	public byte getDisplayCost() {
		return this.displayCost;
	}

	public void setDisplayCost(byte displayCost) {
		this.displayCost = displayCost;
	}

	public byte getDisplayManufacturer() {
		return this.displayManufacturer;
	}

	public void setDisplayManufacturer(byte displayManufacturer) {
		this.displayManufacturer = displayManufacturer;
	}

	public byte getDisplayMult() {
		return this.displayMult;
	}

	public void setDisplayMult(byte displayMult) {
		this.displayMult = displayMult;
	}

	public byte getDisplayParagraph() {
		return this.displayParagraph;
	}
	
	public void setDisplayParagraph(byte displayParagraph) {
		this.displayParagraph = displayParagraph;
	}

	public byte getDisplayPrice() {
		return this.displayPrice;
	}

	public void setDisplayPrice(byte displayPrice) {
		this.displayPrice = displayPrice;
	}

	public byte getDisplayQuantity() {
		return this.displayQuantity;
	}

	public void setDisplayQuantity(byte displayQuantity) {
		this.displayQuantity = displayQuantity;
	}

	public byte getDisplaySpec() {
		return this.displaySpec;
	}

	public void setDisplaySpec(byte displaySpec) {
		this.displaySpec = displaySpec;
	}

	public byte getPrintCost() {
		return this.printCost;
	}

	public void setPrintCost(byte printCost) {
		this.printCost = printCost;
	}

	public byte getPrintManufacturer() {
		return this.printManufacturer;
	}

	public void setPrintManufacturer(byte printManufacturer) {
		this.printManufacturer = printManufacturer;
	}

	public byte getPrintMult() {
		return this.printMult;
	}

	public void setPrintMult(byte printMult) {
		this.printMult = printMult;
	}

	public byte getPrintParagraph() {
		return this.printParagraph;
	}

	public void setPrintParagraph(byte printParagraph) {
		this.printParagraph = printParagraph;
	}

	public byte getPrintPrice() {
		return this.printPrice;
	}

	public void setPrintPrice(byte printPrice) {
		this.printPrice = printPrice;
	}

	public byte getPrintQuantity() {
		return this.printQuantity;
	}

	public void setPrintQuantity(byte printQuantity) {
		this.printQuantity = printQuantity;
	}

	public byte getPrintSpec() {
		return this.printSpec;
	}

	public void setPrintSpec(byte printSpec) {
		this.printSpec = printSpec;
	}

	public int getUserLoginID() {
		return this.userLoginID;
	}

	public void setUserLoginID(int userLoginID) {
		this.userLoginID = userLoginID;
	}

	public byte getNotesFullWidth() {
		return notesFullWidth;
	}

	public void setNotesFullWidth(byte notesFullWidth) {
		this.notesFullWidth = notesFullWidth;
	}

	public byte getLineNumbers() {
		return lineNumbers;
	}

	public void setLineNumbers(byte lineNumbers) {
		this.lineNumbers = lineNumbers;
	}

	public byte getPrintTotal() {
		return printTotal;
	}

	public void setPrintTotal(byte printTotal) {
		this.printTotal = printTotal;
	}

	public byte getHidePrice() {
		return hidePrice;
	}

	public void setHidePrice(byte hidePrice) {
		this.hidePrice = hidePrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public byte getUnderlineCost() {
		return underlineCost;
	}

	public void setUnderlineCost(byte underlineCost) {
		this.underlineCost = underlineCost;
	}

	public byte getUnderlineManufacturer() {
		return underlineManufacturer;
	}

	public void setUnderlineManufacturer(byte underlineManufacturer) {
		this.underlineManufacturer = underlineManufacturer;
	}

	public byte getUnderlineMult() {
		return underlineMult;
	}

	public void setUnderlineMult(byte underlineMult) {
		this.underlineMult = underlineMult;
	}

	public byte getUnderlineParagraph() {
		return underlineParagraph;
	}

	public void setUnderlineParagraph(byte underlineParagraph) {
		this.underlineParagraph = underlineParagraph;
	}

	public byte getUnderlinePrice() {
		return underlinePrice;
	}

	public void setUnderlinePrice(byte underlinePrice) {
		this.underlinePrice = underlinePrice;
	}

	public byte getUnderlineQuantity() {
		return underlineQuantity;
	}

	public void setUnderlineQuantity(byte underlineQuantity) {
		this.underlineQuantity = underlineQuantity;
	}

	public byte getUnderlineSpec() {
		return underlineSpec;
	}

	public void setUnderlineSpec(byte underlineSpec) {
		this.underlineSpec = underlineSpec;
	}

	public byte getBoldCost() {
		return boldCost;
	}

	public void setBoldCost(byte boldCost) {
		this.boldCost = boldCost;
	}

	public byte getBoldManufacturer() {
		return boldManufacturer;
	}

	public void setBoldManufacturer(byte boldManufacturer) {
		this.boldManufacturer = boldManufacturer;
	}

	public byte getBoldMult() {
		return boldMult;
	}

	public void setBoldMult(byte boldMult) {
		this.boldMult = boldMult;
	}

	public byte getBoldParagraph() {
		return boldParagraph;
	}

	public void setBoldParagraph(byte boldParagraph) {
		this.boldParagraph = boldParagraph;
	}

	public byte getBoldPrice() {
		return boldPrice;
	}

	public void setBoldPrice(byte boldPrice) {
		this.boldPrice = boldPrice;
	}

	public byte getBoldQuantity() {
		return boldQuantity;
	}

	public void setBoldQuantity(byte boldQuantity) {
		this.boldQuantity = boldQuantity;
	}

	public byte getBoldSpec() {
		return boldSpec;
	}

	public void setBoldSpec(byte boldSpec) {
		this.boldSpec = boldSpec;
	}

	public byte getDisplayItem() {
		return displayItem;
	}

	public void setDisplayItem(byte displayItem) {
		this.displayItem = displayItem;
	}

	public byte getPrintItem() {
		return printItem;
	}

	public void setPrintItem(byte printItem) {
		this.printItem = printItem;
	}

	public byte getUnderlineItem() {
		return underlineItem;
	}

	public void setUnderlineItem(byte underlineItem) {
		this.underlineItem = underlineItem;
	}

	public byte getBoldItem() {
		return boldItem;
	}

	public void setBoldItem(byte boldItem) {
		this.boldItem = boldItem;
	}

	public byte getDisplayHeader() {
		return displayHeader;
	}

	public void setDisplayHeader(byte displayHeader) {
		this.displayHeader = displayHeader;
	}

	public byte getPrintHeader() {
		return printHeader;
	}

	public void setPrintHeader(byte printHeader) {
		this.printHeader = printHeader;
	}

	public byte getUnderlineHeader() {
		return underlineHeader;
	}

	public void setUnderlineHeader(byte underlineHeader) {
		this.underlineHeader = underlineHeader;
	}

	public byte getBoldHeader() {
		return boldHeader;
	}

	public void setBoldHeader(byte boldHeader) {
		this.boldHeader = boldHeader;
	}

}