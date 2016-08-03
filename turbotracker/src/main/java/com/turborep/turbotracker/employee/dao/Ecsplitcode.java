package com.turborep.turbotracker.employee.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ecSplitCode", catalog = "")
public class Ecsplitcode {
private int ecSplitCodeID;
private String codeName;
private BigDecimal defaultPct;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "ecSplitCodeID", unique = true, nullable = false)
public int getEcSplitCodeID() {
	return ecSplitCodeID;
}

public void setEcSplitCodeID(int ecSplitCodeID) {
	this.ecSplitCodeID = ecSplitCodeID;
}
@Column(name = "CodeName")
public String getCodeName() {
	return codeName;
}
public void setCodeName(String codeName) {
	this.codeName = codeName;
}
@Column(name = "DefaultPct")
public BigDecimal getDefaultPct() {
	return defaultPct;
}
public void setDefaultPct(BigDecimal defaultPct) {
	this.defaultPct = defaultPct;
}

}
