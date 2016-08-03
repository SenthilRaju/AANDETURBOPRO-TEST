package com.turborep.turbotracker.user.dao;


public class AccessModuleConstant {
	static enum MasterSettings {
		HOME(20001),
		JOB(20002),
		SALES(20003),
		PROJECT(20004),
		COMPANY(20005),
		INVENTORY(20006),
		BANKING(20007),
		FINANCIAL(20008);
		
		
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
