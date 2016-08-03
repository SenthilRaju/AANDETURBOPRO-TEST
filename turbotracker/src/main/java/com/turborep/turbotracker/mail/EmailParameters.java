package com.turborep.turbotracker.mail;

import java.util.List;

public class EmailParameters {
	
	public String eUserID;
	public String ePassword;
	public String ePort;
	public String ehost;
	public String eSubject;
	public String eContent;
	public String fileName;
	public String writeFileName;
	public String fromAddress;
	public String toAddress;
	public List<String> cc;
	 public String bcc;
	    
	    
	    
	    
	    
	    
	    
	    
	    
	
	public List<String> getCc() {
		return cc;
	}
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String geteUserID() {
		return eUserID;
	}
	public void seteUserID(String eUserID) {
		this.eUserID = eUserID;
	}
	public String getePassword() {
		return ePassword;
	}
	public void setePassword(String ePassword) {
		this.ePassword = ePassword;
	}
	public String getePort() {
		return ePort;
	}
	public void setePort(String ePort) {
		this.ePort = ePort;
	}
	public String getEhost() {
		return ehost;
	}
	public void setEhost(String ehost) {
		this.ehost = ehost;
	}
	public String geteSubject() {
		return eSubject;
	}
	public void seteSubject(String eSubject) {
		this.eSubject = eSubject;
	}
	public String geteContent() {
		return eContent;
	}
	public void seteContent(String eContent) {
		this.eContent = eContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getWriteFileName() {
		return writeFileName;
	}
	public void setWriteFileName(String writeFileName) {
		this.writeFileName = writeFileName;
	}
	

}
