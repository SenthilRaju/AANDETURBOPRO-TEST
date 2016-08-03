package com.turborep.turbotracker.job.controller;

public class DynamicReportbean {
	public int x;
	public int y;
	public int width;
	public int height;
	public String text;
	public boolean PrintRepeatedValues=false;
	public int fontsize;
	public boolean bold=false;
	public String markup;
	public boolean underline=false;
	
	
	
	//expression
	public String ValueClass;
	
	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getText() {
		return text;
	}
	public void setText(String labeltext) {
		this.text = labeltext;
	}
	public boolean isPrintRepeatedValues() {
		return PrintRepeatedValues;
	}
	public void setPrintRepeatedValues(boolean printRepeatedValues) {
		PrintRepeatedValues = printRepeatedValues;
	}
	public int getFontsize() {
		return fontsize;
	}
	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}
	public boolean isBold() {
		return bold;
	}
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	public String getMarkup() {
		return markup;
	}
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	public boolean isUnderline() {
		return underline;
	}
	public void setUnderline(boolean underline) {
		this.underline = underline;
	}
	public String getValueClass() {
		return ValueClass;
	}
	public void setValueClass(String valueClass) {
		ValueClass = valueClass;
	}
}
