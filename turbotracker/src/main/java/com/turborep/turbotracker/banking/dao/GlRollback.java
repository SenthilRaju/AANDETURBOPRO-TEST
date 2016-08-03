/**
		 *Created by : LEO   Date: 05/20/2016
		 *Purpose : For RollBack 
 **/

package com.turborep.turbotracker.banking.dao;

import java.util.Date;

public class GlRollback {

private Integer veBillID,coLedgerSourceID,yearID,periodID;
private Date transactionDate;

public Integer getVeBillID() {
	return veBillID;
}
public void setVeBillID(Integer veBillID) {
	this.veBillID = veBillID;
}
public Integer getCoLedgerSourceID() {
	return coLedgerSourceID;
}
public void setCoLedgerSourceID(Integer coLedgerSourceID) {
	this.coLedgerSourceID = coLedgerSourceID;
}
public Integer getYearID() {
	return yearID;
}
public void setYearID(Integer yearID) {
	this.yearID = yearID;
}
public Integer getPeriodID() {
	return periodID;
}
public void setPeriodID(Integer periodID) {
	this.periodID = periodID;
}
public Date getTransactionDate() {
	return transactionDate;
}
public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
}

}
