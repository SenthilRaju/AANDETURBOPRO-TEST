package com.turborep.turbotracker.job.dao;

import java.math.BigDecimal;

public class JobQuotesBidListBean {

	private Integer joQuoteHeaderID;
	private String bidder;
	private boolean low;
	private String contact;
	private String quoteType;
	private String lastQuote;
	private String rev;
	private String rep;
	private Integer bidderId;
	private Integer rxContactId;
	private Integer joMasterId;
	private Integer rxMasterId;
	private String jobNumber;
	
	private BigDecimal discountAmount;
	private BigDecimal costAmount;
	private BigDecimal quoteAmount;
	private String createdByName;
	private String InternalNote;
	private boolean primaryQuote;
	private Integer quoteTypeID;
	private Integer createdByID;
	private String CreatedBYFullName;
	private String quoteRemarks;
	private String email;
	private byte displayQuantity;
	private byte printQuantity;
	private byte displayParagraph;
	private byte printParagraph;
	private byte displayManufacturer;
	private byte printManufacturer;
	private byte displayCost;
	private byte printCost;
	private byte displayPrice;
	private byte printPrice;
	private byte displayMult;
	private byte printMult;
	private byte displaySpec;
	private byte printSpec;
	private byte notesFullWidth;
	private byte lineNumbers;
	private byte printTotal;
	private byte hidePrice;
	
	private boolean includeLSD;
	private boolean donotQtyitem2and3;
	private boolean showTotPriceonly;
	private boolean includeTotprice;
	private BigDecimal lsdValue;
	
	
	private Integer quoteemailstatus=0;
	
	public Integer getJoQuoteHeaderID() {
		return joQuoteHeaderID;
	}
	public void setJoQuoteHeaderID(Integer joQuoteHeaderID) {
		this.joQuoteHeaderID = joQuoteHeaderID;
	}
	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public boolean isLow() {
		return low;
	}
	public void setLow(boolean b) {
		this.low = b;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getQuoteType() {
		return quoteType;
	}
	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}
	public String getLastQuote() {
		return lastQuote;
	}
	public void setLastQuote(String lastQuote) {
		this.lastQuote = lastQuote;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String obj) {
		this.rev = obj;
	}
	public String getRep() {
		return rep;
	}
	public void setRep(String rep) {
		this.rep = rep;
	}
	
	
	
	
	public BigDecimal getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(BigDecimal obj) {
		this.costAmount = obj;
	}
	public BigDecimal getQuoteAmount() {
		return quoteAmount;
	}
	public void setQuoteAmount(BigDecimal quoteAmount) {
		this.quoteAmount = quoteAmount;
	}
	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	public String getInternalNote() {
		return InternalNote;
	}
	public void setInternalNote(String internalNote) {
		InternalNote = internalNote;
	}
	public boolean isPrimaryQuote() {
		return primaryQuote;
	}
	public void setPrimaryQuote(boolean primaryQuote) {
		this.primaryQuote = primaryQuote;
	}
	public Integer getBidderId() {
		return bidderId;
	}
	public void setBidderId(Integer bidderId) {
		this.bidderId = bidderId;
	}
	public Integer getRxContactId() {
		return rxContactId;
	}
	public void setRxContactId(Integer rxContactId) {
		this.rxContactId = rxContactId;
	}
	public Integer getJoMasterId() {
		return joMasterId;
	}
	public void setJoMasterId(Integer joMasterId) {
		this.joMasterId = joMasterId;
	}
	public Integer getRxMasterId() {
		return rxMasterId;
	}
	public void setRxMasterId(Integer rxMasterId) {
		this.rxMasterId = rxMasterId;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	public Integer getCreatedByID() {
		return createdByID;
	}
	public void setCreatedByID(Integer createdByID) {
		this.createdByID = createdByID;
	}
	public Integer getQuoteTypeID() {
		return quoteTypeID;
	}
	public void setQuoteTypeID(Integer quoteTypeID) {
		this.quoteTypeID = quoteTypeID;
	}
	public String getCreatedBYFullName() {
		return CreatedBYFullName;
	}
	public void setCreatedBYFullName(String createdBYFullName) {
		CreatedBYFullName = createdBYFullName;
	}
	public String getQuoteRemarks() {
		return quoteRemarks;
	}
	public void setQuoteRemarks(String quoteRemarks) {
		this.quoteRemarks = quoteRemarks;
	}
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal obj) {
		this.discountAmount =obj;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte getDisplayQuantity() {
		return displayQuantity;
	}
	public void setDisplayQuantity(byte displayQuantity) {
		this.displayQuantity = displayQuantity;
	}
	public byte getPrintQuantity() {
		return printQuantity;
	}
	public void setPrintQuantity(byte printQuantity) {
		this.printQuantity = printQuantity;
	}
	public byte getDisplayParagraph() {
		return displayParagraph;
	}
	public void setDisplayParagraph(byte displayParagraph) {
		this.displayParagraph = displayParagraph;
	}
	public byte getPrintParagraph() {
		return printParagraph;
	}
	public void setPrintParagraph(byte printParagraph) {
		this.printParagraph = printParagraph;
	}
	public byte getDisplayManufacturer() {
		return displayManufacturer;
	}
	public void setDisplayManufacturer(byte displayManufacturer) {
		this.displayManufacturer = displayManufacturer;
	}
	public byte getPrintManufacturer() {
		return printManufacturer;
	}
	public void setPrintManufacturer(byte printManufacturer) {
		this.printManufacturer = printManufacturer;
	}
	public byte getDisplayCost() {
		return displayCost;
	}
	public void setDisplayCost(byte displayCost) {
		this.displayCost = displayCost;
	}
	public byte getPrintCost() {
		return printCost;
	}
	public void setPrintCost(byte printCost) {
		this.printCost = printCost;
	}
	public byte getDisplayPrice() {
		return displayPrice;
	}
	public void setDisplayPrice(byte displayPrice) {
		this.displayPrice = displayPrice;
	}
	public byte getPrintPrice() {
		return printPrice;
	}
	public void setPrintPrice(byte printPrice) {
		this.printPrice = printPrice;
	}
	public byte getDisplayMult() {
		return displayMult;
	}
	public void setDisplayMult(byte displayMult) {
		this.displayMult = displayMult;
	}
	public byte getPrintMult() {
		return printMult;
	}
	public void setPrintMult(byte printMult) {
		this.printMult = printMult;
	}
	public byte getDisplaySpec() {
		return displaySpec;
	}
	public void setDisplaySpec(byte displaySpec) {
		this.displaySpec = displaySpec;
	}
	public byte getPrintSpec() {
		return printSpec;
	}
	public void setPrintSpec(byte printSpec) {
		this.printSpec = printSpec;
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
	public Integer getQuoteemailstatus() {
		return quoteemailstatus;
	}

	public void setQuoteemailstatus(Integer quoteemailstatus) {
		this.quoteemailstatus = quoteemailstatus;
	}
	
	
	
	public boolean isIncludeLSD() {
		return includeLSD;
	}
	public void setIncludeLSD(boolean includeLSD) {
		this.includeLSD = includeLSD;
	}
	public boolean isDonotQtyitem2and3() {
		return donotQtyitem2and3;
	}
	public void setDonotQtyitem2and3(boolean donotQtyitem2and3) {
		this.donotQtyitem2and3 = donotQtyitem2and3;
	}
	public boolean isShowTotPriceonly() {
		return showTotPriceonly;
	}
	public void setShowTotPriceonly(boolean showTotPriceonly) {
		this.showTotPriceonly = showTotPriceonly;
	}
	public boolean isIncludeTotprice() {
		return includeTotprice;
	}
	public void setIncludeTotprice(boolean includeTotprice) {
		this.includeTotprice = includeTotprice;
	}
	public BigDecimal getLsdValue() {
		return lsdValue;
	}
	public void setLsdValue(BigDecimal lsdValue) {
		this.lsdValue = lsdValue;
	}
	
}