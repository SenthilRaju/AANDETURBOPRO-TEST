package com.turborep.turbotracker.user.dao;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="joWizardAppletData",catalog="")
public class JoWizardAppletData  implements java.io.Serializable {

	private Integer id;
	private Integer userLoginId;
	private String appletLocalUrl;
	private String jobNumber;
	private Integer joMasterID;
	
	public JoWizardAppletData() {
	}
	
	@Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="userLoginId", unique=true, nullable=false)
    public Integer getUserLoginId() {
        return this.userLoginId;
    }
    
    public void setUserLoginId(Integer userLoginId) {
        this.userLoginId = userLoginId;
    }
    
    @Column(name="appletLocalUrl")
	public String getAppletLocalUrl() {
		return appletLocalUrl;
	}


	public void setAppletLocalUrl(String appletLocalUrl) {
		this.appletLocalUrl = appletLocalUrl;
	}
	
	@Column(name="jobNumber", unique=true, nullable=false)
	public String getJobNumber() {
		return jobNumber;
	}


	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Integer getJoMasterID() {
		return joMasterID;
	}

	public void setJoMasterID(Integer joMasterID) {
		this.joMasterID = joMasterID;
	}
}
