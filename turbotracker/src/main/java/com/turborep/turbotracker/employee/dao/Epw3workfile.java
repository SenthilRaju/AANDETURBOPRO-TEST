package com.turborep.turbotracker.employee.dao;

// Generated Jan 23, 2012 5:39:26 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Epw3workfile generated by hbm2java
 */
@Entity
@Table(name = "epw3workfile", catalog = "")
public class Epw3workfile implements java.io.Serializable {

	private Integer epW3workFileId;
	private String la;
	private String lc;
	private String ld;
	private String le;
	private String lf1;
	private String lf2;
	private String lf3;
	private String lf4;
	private String l15a;
	private String l15b;
	private String contact;
	private String email;
	private String phone1ac;
	private String phone1;
	private String phone2ac;
	private String phone2;
	private String l1;
	private String l2;
	private String l3;
	private String l4;
	private String l5;
	private String l6;
	private String l7;
	private String l8;
	private String l9;
	private String l10;
	private String l11;
	private String l12;
	private String l16;
	private String l17;
	private String l18;
	private String l19;

	public Epw3workfile() {
	}

	public Epw3workfile(String la, String lc, String ld, String le, String lf1,
			String lf2, String lf3, String lf4, String l15a, String l15b,
			String contact, String email, String phone1ac, String phone1,
			String phone2ac, String phone2, String l1, String l2, String l3,
			String l4, String l5, String l6, String l7, String l8, String l9,
			String l10, String l11, String l12, String l16, String l17,
			String l18, String l19) {
		this.la = la;
		this.lc = lc;
		this.ld = ld;
		this.le = le;
		this.lf1 = lf1;
		this.lf2 = lf2;
		this.lf3 = lf3;
		this.lf4 = lf4;
		this.l15a = l15a;
		this.l15b = l15b;
		this.contact = contact;
		this.email = email;
		this.phone1ac = phone1ac;
		this.phone1 = phone1;
		this.phone2ac = phone2ac;
		this.phone2 = phone2;
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		this.l5 = l5;
		this.l6 = l6;
		this.l7 = l7;
		this.l8 = l8;
		this.l9 = l9;
		this.l10 = l10;
		this.l11 = l11;
		this.l12 = l12;
		this.l16 = l16;
		this.l17 = l17;
		this.l18 = l18;
		this.l19 = l19;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "epW3WorkFileID", unique = true, nullable = false)
	public Integer getEpW3workFileId() {
		return this.epW3workFileId;
	}

	public void setEpW3workFileId(Integer epW3workFileId) {
		this.epW3workFileId = epW3workFileId;
	}

	@Column(name = "la", length = 50)
	public String getLa() {
		return this.la;
	}

	public void setLa(String la) {
		this.la = la;
	}

	@Column(name = "lc", length = 50)
	public String getLc() {
		return this.lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	@Column(name = "ld", length = 50)
	public String getLd() {
		return this.ld;
	}

	public void setLd(String ld) {
		this.ld = ld;
	}

	@Column(name = "le", length = 50)
	public String getLe() {
		return this.le;
	}

	public void setLe(String le) {
		this.le = le;
	}

	@Column(name = "lf1", length = 50)
	public String getLf1() {
		return this.lf1;
	}

	public void setLf1(String lf1) {
		this.lf1 = lf1;
	}

	@Column(name = "lf2", length = 50)
	public String getLf2() {
		return this.lf2;
	}

	public void setLf2(String lf2) {
		this.lf2 = lf2;
	}

	@Column(name = "lf3", length = 50)
	public String getLf3() {
		return this.lf3;
	}

	public void setLf3(String lf3) {
		this.lf3 = lf3;
	}

	@Column(name = "lf4", length = 50)
	public String getLf4() {
		return this.lf4;
	}

	public void setLf4(String lf4) {
		this.lf4 = lf4;
	}

	@Column(name = "l15a", length = 50)
	public String getL15a() {
		return this.l15a;
	}

	public void setL15a(String l15a) {
		this.l15a = l15a;
	}

	@Column(name = "l15b", length = 50)
	public String getL15b() {
		return this.l15b;
	}

	public void setL15b(String l15b) {
		this.l15b = l15b;
	}

	@Column(name = "Contact", length = 50)
	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "Email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "Phone1AC", length = 3)
	public String getPhone1ac() {
		return this.phone1ac;
	}

	public void setPhone1ac(String phone1ac) {
		this.phone1ac = phone1ac;
	}

	@Column(name = "Phone1", length = 12)
	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	@Column(name = "Phone2AC", length = 3)
	public String getPhone2ac() {
		return this.phone2ac;
	}

	public void setPhone2ac(String phone2ac) {
		this.phone2ac = phone2ac;
	}

	@Column(name = "Phone2", length = 50)
	public String getPhone2() {
		return this.phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Column(name = "l1", length = 50)
	public String getL1() {
		return this.l1;
	}

	public void setL1(String l1) {
		this.l1 = l1;
	}

	@Column(name = "l2", length = 50)
	public String getL2() {
		return this.l2;
	}

	public void setL2(String l2) {
		this.l2 = l2;
	}

	@Column(name = "l3", length = 50)
	public String getL3() {
		return this.l3;
	}

	public void setL3(String l3) {
		this.l3 = l3;
	}

	@Column(name = "l4", length = 50)
	public String getL4() {
		return this.l4;
	}

	public void setL4(String l4) {
		this.l4 = l4;
	}

	@Column(name = "l5", length = 50)
	public String getL5() {
		return this.l5;
	}

	public void setL5(String l5) {
		this.l5 = l5;
	}

	@Column(name = "l6", length = 50)
	public String getL6() {
		return this.l6;
	}

	public void setL6(String l6) {
		this.l6 = l6;
	}

	@Column(name = "l7", length = 50)
	public String getL7() {
		return this.l7;
	}

	public void setL7(String l7) {
		this.l7 = l7;
	}

	@Column(name = "l8", length = 50)
	public String getL8() {
		return this.l8;
	}

	public void setL8(String l8) {
		this.l8 = l8;
	}

	@Column(name = "l9", length = 50)
	public String getL9() {
		return this.l9;
	}

	public void setL9(String l9) {
		this.l9 = l9;
	}

	@Column(name = "l10", length = 50)
	public String getL10() {
		return this.l10;
	}

	public void setL10(String l10) {
		this.l10 = l10;
	}

	@Column(name = "l11", length = 50)
	public String getL11() {
		return this.l11;
	}

	public void setL11(String l11) {
		this.l11 = l11;
	}

	@Column(name = "l12", length = 50)
	public String getL12() {
		return this.l12;
	}

	public void setL12(String l12) {
		this.l12 = l12;
	}

	@Column(name = "l16", length = 50)
	public String getL16() {
		return this.l16;
	}

	public void setL16(String l16) {
		this.l16 = l16;
	}

	@Column(name = "l17", length = 50)
	public String getL17() {
		return this.l17;
	}

	public void setL17(String l17) {
		this.l17 = l17;
	}

	@Column(name = "l18", length = 50)
	public String getL18() {
		return this.l18;
	}

	public void setL18(String l18) {
		this.l18 = l18;
	}

	@Column(name = "l19", length = 50)
	public String getL19() {
		return this.l19;
	}

	public void setL19(String l19) {
		this.l19 = l19;
	}

}
