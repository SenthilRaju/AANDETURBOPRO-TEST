package com.turborep.turbotracker.job.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "joQLineItemsTempProp", catalog = "")
public class joQLineItemTemplateProp {
	private Integer joQLineItemsTempPropId;
	private Integer joQuoteTemplateDetailId;
	private boolean italicItem         ;
	private boolean underlineItem      ;
	private boolean boldItem           ;
	private boolean italicQuantity     ;
	private boolean underlineQuantity  ;
	private boolean boldQuantity       ;
	private boolean italicParagraph    ;
	private boolean underlineParagraph ;
	private boolean boldParagraph      ;
	private boolean italicManufacturer ;
	private boolean underlineManufactur;
	private boolean boldManufacturer   ;
	private boolean italicSpec         ;
	private boolean underlineSpec      ;
	private boolean boldSpec           ;
	private boolean italicMult         ;
	private boolean underlineMult      ;
	private boolean boldMult           ;
	private boolean italicPrice        ;
	private boolean underlinePrice     ;
	private boolean boldPrice          ;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "joQLineItemsTempPropId")
	public Integer getJoQLineItemsTempPropId() {
		return joQLineItemsTempPropId;
	}
	public void setJoQLineItemsTempPropId(Integer joQLineItemsTempPropId) {
		this.joQLineItemsTempPropId = joQLineItemsTempPropId;
	}
	@Column(name = "joQuoteTemplateDetailID")
	public Integer getJoQuoteTemplateDetailId() {
		return joQuoteTemplateDetailId;
	}
	public void setJoQuoteTemplateDetailId(Integer joQuoteTemplateDetailId) {
		this.joQuoteTemplateDetailId = joQuoteTemplateDetailId;
	}
	@Column(name = "italicItem")
	public boolean isItalicItem() {
		return italicItem;
	}
	public void setItalicItem(boolean italicItem) {
		this.italicItem = italicItem;
	}
	@Column(name = "underlineItem")
	public boolean isUnderlineItem() {
		return underlineItem;
	}
	public void setUnderlineItem(boolean underlineItem) {
		this.underlineItem = underlineItem;
	}
	@Column(name = "boldItem")
	public boolean isBoldItem() {
		return boldItem;
	}
	public void setBoldItem(boolean boldItem) {
		this.boldItem = boldItem;
	}
	@Column(name = "italicQuantity")
	public boolean isItalicQuantity() {
		return italicQuantity;
	}
	public void setItalicQuantity(boolean italicQuantity) {
		this.italicQuantity = italicQuantity;
	}
	@Column(name = "underlineQuantity")
	public boolean isUnderlineQuantity() {
		return underlineQuantity;
	}
	public void setUnderlineQuantity(boolean underlineQuantity) {
		this.underlineQuantity = underlineQuantity;
	}
	@Column(name = "boldQuantity")
	public boolean isBoldQuantity() {
		return boldQuantity;
	}
	public void setBoldQuantity(boolean boldQuantity) {
		this.boldQuantity = boldQuantity;
	}
	@Column(name = "italicParagraph")
	public boolean isItalicParagraph() {
		return italicParagraph;
	}
	public void setItalicParagraph(boolean italicParagraph) {
		this.italicParagraph = italicParagraph;
	}
	@Column(name = "underlineParagraph")
	public boolean isUnderlineParagraph() {
		return underlineParagraph;
	}
	public void setUnderlineParagraph(boolean underlineParagraph) {
		this.underlineParagraph = underlineParagraph;
	}
	@Column(name = "boldParagraph")
	public boolean isBoldParagraph() {
		return boldParagraph;
	}
	public void setBoldParagraph(boolean boldParagraph) {
		this.boldParagraph = boldParagraph;
	}
	@Column(name = "italicManufacturer")
	public boolean isItalicManufacturer() {
		return italicManufacturer;
	}
	public void setItalicManufacturer(boolean italicManufacturer) {
		this.italicManufacturer = italicManufacturer;
	}
	@Column(name = "underlineManufacturer")
	public boolean isUnderlineManufactur() {
		return underlineManufactur;
	}
	public void setUnderlineManufactur(boolean underlineManufactur) {
		this.underlineManufactur = underlineManufactur;
	}
	@Column(name = "boldManufacturer")
	public boolean isBoldManufacturer() {
		return boldManufacturer;
	}
	public void setBoldManufacturer(boolean boldManufacturer) {
		this.boldManufacturer = boldManufacturer;
	}
	@Column(name = "italicSpec")
	public boolean isItalicSpec() {
		return italicSpec;
	}
	public void setItalicSpec(boolean italicSpec) {
		this.italicSpec = italicSpec;
	}
	@Column(name = "underlineSpec")
	public boolean isUnderlineSpec() {
		return underlineSpec;
	}
	public void setUnderlineSpec(boolean underlineSpec) {
		this.underlineSpec = underlineSpec;
	}
	@Column(name = "boldSpec")
	public boolean isBoldSpec() {
		return boldSpec;
	}
	public void setBoldSpec(boolean boldSpec) {
		this.boldSpec = boldSpec;
	}
	@Column(name = "italicMult")
	public boolean isItalicMult() {
		return italicMult;
	}
	public void setItalicMult(boolean italicMult) {
		this.italicMult = italicMult;
	}
	@Column(name = "underlineMult")
	public boolean isUnderlineMult() {
		return underlineMult;
	}
	public void setUnderlineMult(boolean underlineMult) {
		this.underlineMult = underlineMult;
	}
	@Column(name = "boldMult")
	public boolean isBoldMult() {
		return boldMult;
	}
	public void setBoldMult(boolean boldMult) {
		this.boldMult = boldMult;
	}
	@Column(name = "italicPrice")
	public boolean isItalicPrice() {
		return italicPrice;
	}
	public void setItalicPrice(boolean italicPrice) {
		this.italicPrice = italicPrice;
	}
	@Column(name = "underlinePrice")
	public boolean isUnderlinePrice() {
		return underlinePrice;
	}
	public void setUnderlinePrice(boolean underlinePrice) {
		this.underlinePrice = underlinePrice;
	}
	@Column(name = "boldPrice")
	public boolean isBoldPrice() {
		return boldPrice;
	}
	public void setBoldPrice(boolean boldPrice) {
		this.boldPrice = boldPrice;
	}   
	       
	

}
