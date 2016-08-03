package com.turborep.turbotracker.user.dao;

public class UserBean {

	private int userId;
	private String userName;
	private String role;
	private String fullName;
	private String initials;
	private int usergroupId;
	private boolean isSalesRep;
	private boolean isAdmin;
	private boolean isCSR;
	private boolean isEmployee;
	private boolean isWareHouse;
	private byte systemAdministrator;
	private String emailName;
	private String emailAddr;
	private String logOnName;
	private String logOnPswd;
	private byte useAuthentication;
	private String ccaddr1;
	private String ccname1;
	private String ccaddr2;
	private String ccname2;
	private String ccaddr3;
	private String ccname3;
	private String ccaddr4;
	private String ccname4;
	private String bccaddr;
	private String smtpsvr;
	private byte smtpemailActive;
	private Integer smtpport;
	private Integer userperpage;
	private String loginpswd;
	

	public UserBean(int userId, String userName, String role, boolean isSalesRep, int usergroupId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.role = role;
		this.isSalesRep = isSalesRep;
		this.usergroupId = usergroupId;
	}

	public UserBean() {
		super();
		isSalesRep = false;
		isAdmin = false;
		isCSR = false;
		isEmployee = false;
		isWareHouse = false;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public boolean isSalesRep() {
		return isSalesRep;
	}
	public void setSalesRep(boolean isSalesRep) {
		this.isSalesRep = isSalesRep;
	}

	public int getUsergroupId() {
		return usergroupId;
	}
	public void setUsergroupId(int usergroupId) {
		this.usergroupId = usergroupId;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public boolean isCSR() {
		return isCSR;
	}
	public void setCSR(boolean isCSR) {
		this.isCSR = isCSR;
	}
	
	public boolean isEmployee() {
		return isEmployee;
	}
	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}
	
	public boolean isWareHouse() {
		return isWareHouse;
	}
	public void setWareHouse(boolean isWareHouse) {
		this.isWareHouse = isWareHouse;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	 public byte getSystemAdministrator() {
        return this.systemAdministrator;
    }
	    
	 public void setSystemAdministrator(byte systemAdministrator) {
        this.systemAdministrator = systemAdministrator;
    }

	public String getEmailName() {
		return emailName;
	}

	public void setEmailName(String emailName) {
		this.emailName = emailName;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getLogOnName() {
		return logOnName;
	}

	public void setLogOnName(String logOnName) {
		this.logOnName = logOnName;
	}

	public String getLogOnPswd() {
		return logOnPswd;
	}

	public void setLogOnPswd(String logOnPswd) {
		this.logOnPswd = logOnPswd;
	}

	public byte getUseAuthentication() {
		return useAuthentication;
	}

	public void setUseAuthentication(byte useAuthentication) {
		this.useAuthentication = useAuthentication;
	}

	public String getCcaddr1() {
		return ccaddr1;
	}

	public void setCcaddr1(String ccaddr1) {
		this.ccaddr1 = ccaddr1;
	}

	public String getCcname1() {
		return ccname1;
	}

	public void setCcname1(String ccname1) {
		this.ccname1 = ccname1;
	}

	public String getCcaddr2() {
		return ccaddr2;
	}

	public void setCcaddr2(String ccaddr2) {
		this.ccaddr2 = ccaddr2;
	}

	public String getCcname2() {
		return ccname2;
	}

	public void setCcname2(String ccname2) {
		this.ccname2 = ccname2;
	}

	public String getCcaddr3() {
		return ccaddr3;
	}

	public void setCcaddr3(String ccaddr3) {
		this.ccaddr3 = ccaddr3;
	}

	public String getCcname3() {
		return ccname3;
	}

	public void setCcname3(String ccname3) {
		this.ccname3 = ccname3;
	}

	public String getCcaddr4() {
		return ccaddr4;
	}

	public void setCcaddr4(String ccaddr4) {
		this.ccaddr4 = ccaddr4;
	}

	public String getCcname4() {
		return ccname4;
	}

	public void setCcname4(String ccname4) {
		this.ccname4 = ccname4;
	}

	public String getBccaddr() {
		return bccaddr;
	}

	public void setBccaddr(String bccaddr) {
		this.bccaddr = bccaddr;
	}

	public String getSmtpsvr() {
		return smtpsvr;
	}

	public void setSmtpsvr(String smtpsvr) {
		this.smtpsvr = smtpsvr;
	}

	public byte getSmtpemailActive() {
		return smtpemailActive;
	}

	public void setSmtpemailActive(byte smtpemailActive) {
		this.smtpemailActive = smtpemailActive;
	}

	public Integer getSmtpport() {
		return smtpport;
	}

	public void setSmtpport(Integer smtpport) {
		this.smtpport = smtpport;
	}

	public Integer getUserperpage() {
		return userperpage;
	}

	public void setUserperpage(Integer userperpage) {
		this.userperpage = userperpage;
	}

	public String getLoginpswd() {
		return loginpswd;
	}

	public void setLoginpswd(String loginpswd) {
		this.loginpswd = loginpswd;
	}
}
