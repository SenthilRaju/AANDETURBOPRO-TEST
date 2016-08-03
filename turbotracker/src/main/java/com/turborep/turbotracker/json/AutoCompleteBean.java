package com.turborep.turbotracker.json;

import java.math.BigDecimal;
import java.util.Date;

public class AutoCompleteBean {

	private int id;
	private int manufactureID;
	private String label;
	private String value;
	private String entity = "job";
	private String rolodexEntity = "vendors,customers,employeelist,architect,engineer,generalcontractors,architect/engineer,users,accounts";
	private BigDecimal taxValue;
	private Date bidDate;
	private String checkNo;
	private String memo;
	private short typeId;
	private int rxAddressID;
	private String accountDescription;
	private byte dueOnDay;
	private short dueDays;
	private String inventoryAdjustmentDesc;
	private BigDecimal inventoryOnHand;
	private BigDecimal inventoryCost;
	private int pk_field;
	private String column1;
	private String column2;
	private String column3;
	private byte taxfreight;
	
	public String getInventoryAdjustmentDesc() {
		return inventoryAdjustmentDesc;
	}
	public void setInventoryAdjustmentDesc(String inventoryAdjustmentDesc) {
		this.inventoryAdjustmentDesc = inventoryAdjustmentDesc;
	}
	
	public short getDueOnDay() {
		return dueOnDay;
	}
	public void setDueOnDay(byte dueOnDay) {
		this.dueOnDay = dueOnDay;
	}
	public short getDueDays() {
		return dueDays;
	}
	public void setDueDays(short dueDays) {
		this.dueDays = dueDays;
	}
	public short getTypeId() {
		return typeId;
	}
	public void setTypeId(short typeId) {
		this.typeId = typeId;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getRolodexEntity() {
		return rolodexEntity;
	}

	public void setRolodexEntity(String rolodexEntity) {
		this.rolodexEntity = rolodexEntity;
	}
	public BigDecimal getTaxValue() {
		return taxValue;
	}
	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}
	public int getManufactureID() {
		return manufactureID;
	}
	public void setManufactureID(int manufactureID) {
		this.manufactureID = manufactureID;
	}
	public Date getBidDate() {
		return bidDate;
	}
	public void setBidDate(Date bidDate) {
		this.bidDate = bidDate;
	}
	public int getRxAddressID() {
		return rxAddressID;
	}
	public void setRxAddressID(int rxAddressID) {
		this.rxAddressID = rxAddressID;
	}
	public String getAccountDescription() {
		return accountDescription;
	}
	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}
	public BigDecimal getInventoryOnHand() {
		return inventoryOnHand;
	}
	public void setInventoryOnHand(BigDecimal inventoryOnHand) {
		this.inventoryOnHand = inventoryOnHand;
	}
	public BigDecimal getInventoryCost() {
		return inventoryCost;
	}
	public void setInventoryCost(BigDecimal inventoryCost) {
		this.inventoryCost = inventoryCost;
	}
	public int getPk_field() {
		return pk_field;
	}
	public void setPk_field(int pk_field) {
		this.pk_field = pk_field;
	}
	public String getColumn1() {
		return column1;
	}
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	public String getColumn2() {
		return column2;
	}
	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	public String getColumn3() {
		return column3;
	}
	public void setColumn3(String column3) {
		this.column3 = column3;
	}
	public byte getTaxfreight() {
		return taxfreight;
	}
	public void setTaxfreight(byte taxfreight) {
		this.taxfreight = taxfreight;
	}
	
}
