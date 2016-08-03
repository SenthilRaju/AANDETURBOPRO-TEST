package com.turborep.turbotracker.job.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "jocategory", catalog = "")
public class jocategory implements java.io.Serializable{
private int joCategoryID;
private String Category;

private int joCategory1ID;
private String Category1desc;

private int joCategory2ID;
private String Category2desc;

private int joCategory3ID;
private String Category3desc;

private int joCategory4ID;
private String Category4desc;

private int joCategory5ID;
private String Category5desc;

private int joCategory6ID;
private String Category6desc;

private int joCategory7ID;
private String Category7desc;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "JoCategoryID", unique = true, nullable = false)
public int getJoCategoryID() {
	return joCategoryID;
}
public void setJoCategoryID(int joCategoryID) {
	this.joCategoryID = joCategoryID;
}

@Column(name = "Category")
public String getCategory() {
	return Category;
}
public void setCategory(String category) {
	Category = category;
}

@Transient
public int getJoCategory1ID() {
	return joCategory1ID;
}
public void setJoCategory1ID(int joCategory1ID) {
	this.joCategory1ID = joCategory1ID;
}
@Transient
public String getCategory1desc() {
	return Category1desc;
}
public void setCategory1desc(String category1desc) {
	Category1desc = category1desc;
}
@Transient
public int getJoCategory2ID() {
	return joCategory2ID;
}
public void setJoCategory2ID(int joCategory2ID) {
	this.joCategory2ID = joCategory2ID;
}
@Transient
public String getCategory2desc() {
	return Category2desc;
}
public void setCategory2desc(String category2desc) {
	Category2desc = category2desc;
}
@Transient
public int getJoCategory3ID() {
	return joCategory3ID;
}
public void setJoCategory3ID(int joCategory3ID) {
	this.joCategory3ID = joCategory3ID;
}
@Transient
public String getCategory3desc() {
	return Category3desc;
}
public void setCategory3desc(String category3desc) {
	Category3desc = category3desc;
}
@Transient
public int getJoCategory4ID() {
	return joCategory4ID;
}
public void setJoCategory4ID(int joCategory4ID) {
	this.joCategory4ID = joCategory4ID;
}
@Transient
public String getCategory4desc() {
	return Category4desc;
}
public void setCategory4desc(String category4desc) {
	Category4desc = category4desc;
}
@Transient
public int getJoCategory5ID() {
	return joCategory5ID;
}
public void setJoCategory5ID(int joCategory5ID) {
	this.joCategory5ID = joCategory5ID;
}
@Transient
public String getCategory5desc() {
	return Category5desc;
}
public void setCategory5desc(String category5desc) {
	Category5desc = category5desc;
}
@Transient
public int getJoCategory6ID() {
	return joCategory6ID;
}
public void setJoCategory6ID(int joCategory6ID) {
	this.joCategory6ID = joCategory6ID;
}
@Transient
public String getCategory6desc() {
	return Category6desc;
}
public void setCategory6desc(String category6desc) {
	Category6desc = category6desc;
}
@Transient
public int getJoCategory7ID() {
	return joCategory7ID;
}
public void setJoCategory7ID(int joCategory7ID) {
	this.joCategory7ID = joCategory7ID;
}
@Transient
public String getCategory7desc() {
	return Category7desc;
}
public void setCategory7desc(String category7desc) {
	Category7desc = category7desc;
}
}
