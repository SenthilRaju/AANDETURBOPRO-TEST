package com.turborep.turbotracker.user.dao;

import com.turborep.turbotracker.user.dao.AccessModuleConstant.MasterSettings;

public class AccessProcedureConstant {

	static enum MasterSettings {
		//HOME 22000
		New_Job(22001),
		Quick_book(22002),
		
		//JOB 23000
		Main(23001),
		Quotes(23002),
		Submittal(23003),
		Credit(23004),
		Release(23005),
		Financial(23006),
		Journal(23007),
		
		//Sales 24000
		Sales(24001),
		Sales_Filter(24002),
		
		//Project 25000
		Project(25001),
		Project_Filter(25002),
		
		//Company 26000
		Customers(26001),
		Payments(26002),
		Statements(26003),
		Sales_Order(26004),
		Invoice(26005),
		Finance_Changes(26006),
		Tax_Adjustments(26007),
		CreditDebit_Memos(26008),
		Sales_Order_Template(26009),
		Purchase_Orders(26010),
		Vendors(26011),
		Pay_Bills(26012),
		Invoice_Bills(26013), 
		Employees(26014),
		Commissions(26015), 
		Rolodex(26016), 
		Users(26017), 
		Settings(26018),
		
		//Inventory 27000
		Inventory(27001),
		Categories(27002),
		Warehouses(27003),
		Receive_Inventory(27004),
		Transfer(27005),
		Order_Points(27006),
		Inventory_Value(27007),
		Count(27008),
		Adjustments(27009),
		Transactions(27010),
		
		//Banking 28000
		Banking(28001),
		Write_Checks(28002),
		Reissue_Checks(28003),
		Recouncile_Accounts(28004),
		
		//Financial 29000
		Chart_Accounts(29001),
		Divisions(29002),
		Tax_Territories(29003),
		General_Ledger(29004),
		Journal_Entries(29005),
		Accounting_Cycles(29006),
		GL_Transactions(29007),
		OpenPeriod_PostingOnly(29008);
		
		
Integer sysvariableid;

MasterSettings(Integer sysvariableid) {
	this.sysvariableid = sysvariableid;
}

public Integer getSysvariableid() {
	return sysvariableid;
}

public void setSysvariableid(Integer sysvariableid) {
	this.sysvariableid = sysvariableid;
}
}

public static Integer getConstantSysvariableId(String menu) {
return MasterSettings.valueOf(menu).getSysvariableid();
}


}
