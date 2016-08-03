/**
 * ***********************************************************************************
 * Maintenance History:
 * Date                Release         Developer                   Summary
 * --------            		---------       		------------      		          --------
 * 28-Dec-2012         1.1.6         thulasi.ram@sysvine.com    Created this class.
 *
 *************************************************************************************
 *
 *
 * @author Thulasi Ram.
 * @version 1.1.6
 *
 */


package com.turborep.turbotracker.Configure;

import java.util.ResourceBundle;

/**
 * This class email the status report to the employee
 * and has the inner class for smtp password authentication. 
 */

public class Mailer
{
	private String					itsHostName;
	private String					itsUserName;
	private String					itsPassword;
	private String					itsFromAddress;
	private String					itsToAddress;
	private	String					itsRegSubject;
	private String					itsAckSubject;
	private String					itsAckContent;
	private String					itsAckWhitePaperContent;
	private String					itsAckWhitePaperAlone;
	private String					itsWhitePaperLink;
	private String					itsMailStatus;
	private String					itsMailSentInfo;
	private ResourceBundle	itsServerProperties;
	
	public Mailer(ResourceBundle itsServerProperties)
	{
		super();
		
		this.itsServerProperties 		= itsServerProperties;
		
		this.itsHostName 				= itsServerProperties.getString("mail.hostName");
		this.itsUserName 				= itsServerProperties.getString("mail.smtp.user");
		this.itsPassword 				= itsServerProperties.getString("mail.smtp.password");
		this.itsFromAddress 			= itsServerProperties.getString("mail.auto.email");
		this.itsToAddress 				= itsServerProperties.getString("mail.to");
		
		this.itsRegSubject 				= itsServerProperties.getString("mail.subject");
		
		this.itsAckSubject 				= itsServerProperties.getString("mail.acknowledgementEmailSubject");
		this.itsAckContent 				= itsServerProperties.getString("mail.acknowledgementEmailContent");
		this.itsAckWhitePaperContent 	= itsServerProperties.getString("mail.acknowledgementEmailWhitePaper");
		this.itsAckWhitePaperAlone		= itsServerProperties.getString("mail.acknowledgementEmailWhitePaperAlone");
		this.itsWhitePaperLink 			= itsServerProperties.getString("mail.whitepaper.link");
		this.itsMailStatus 				= itsServerProperties.getString("mail.status");
		this.itsMailSentInfo 			= itsServerProperties.getString("mail.sent");
	}

	/**
	 * @return the itsHostName
	 */
	public String getHostName()
	{
		return this.itsHostName;
	}
	
	/**
	 * @param theHostName the itsHostName to set
	 */
	public void setHostName(String theHostName)
	{
		this.itsHostName = theHostName;
	}
	
	/**
	 * @return the itsUserName
	 */
	public String getUserName()
	{
		return this.itsUserName;
	}
	
	/**
	 * @param theUserName the itsUserName to set
	 */
	public void setUserName(String theUserName)
	{
		this.itsUserName = theUserName;
	}
	
	/**
	 * @return the itsPassword
	 */
	public String getPassword()
	{
		return this.itsPassword;
	}
	
	/**
	 * @param thePassword the itsPassword to set
	 */
	public void setPassword(String thePassword)
	{
		this.itsPassword = thePassword;
	}
	
	/**
	 * @return the itsFomAddress
	 */
	public String getFromAddress()
	{
		return this.itsFromAddress;
	}
	
	/**
	 * @param theFromAddress the itsFromAddress to set
	 */
	public void setFromAddress(String theFromAddress)
	{
		this.itsFromAddress = theFromAddress;
	}
	
	/**
	 * @return the itsToAddress
	 */
	public String getToAddress()
	{
		return this.itsToAddress;
	}
	
	/**
	 * @param theToAddress the itsToAddress to set
	 */
	public void setToAddress(String theToAddress)
	{
		this.itsToAddress = theToAddress;
	}
	
	/**
	 * @return the itsWhitePaperLink
	 */
	public String getWhitePaperLink()
	{
		return this.itsWhitePaperLink;
	}
	
	/**
	 * @param theWhitePaperLink the itsWhitePaperLink to set
	 */
	public void setWhitePaperLink(String theWhitePaperLink)
	{
		this.itsWhitePaperLink = theWhitePaperLink;
	}
	
	/**
	 * @return the itsAckSubject
	 */
	public String getAckSubject()
	{
		return this.itsAckSubject;
	}
	
	/**
	 * @param theAckSubject the itsAckSubject to set
	 */
	public void setAckSubject(String theAckSubject)
	{
		this.itsAckSubject = theAckSubject;
	}
	
	/**
	 * @return the itsAckContent
	 */
	public String getAckContent()
	{
		return this.itsAckContent;
	}
	
	/**
	 * @param theAckContent the itsAckContent to set
	 */
	public void setAckContent(String theAckContent)
	{
		this.itsAckContent = theAckContent;
	}
	
	/**
	 * @return the itsAckWhitePaperContent
	 */
	public String getAckWhitePaperContent()
	{
		return this.itsAckWhitePaperContent;
	}
	
	/**
	 * @param theAckWhitePaperContent the itsAckWhitePaperContent to set
	 */
	public void setAckWhitePaperContent(String theAckWhitePaperContent)
	{
		this.itsAckWhitePaperContent = theAckWhitePaperContent;
	}
	/**
	 * @return the itsAckWhitePaperAlone
	 */
	public String getAckWhitePaperAlone()
	{
		return this.itsAckWhitePaperAlone;
	}
	
	/**
	 * @param theAckWhitePaperAlone the itsAckWhitePaperAlone to set
	 */
	public void setAckWhitePaperAlone(String theAckWhitePaperAlone)
	{
		this.itsAckWhitePaperContent = itsAckWhitePaperAlone;
	}
	
	/**
	 * @return the itsMailStatus
	 */
	public String getMailStatus()
	{
		return this.itsMailStatus;
	}
	
	/**
	 * @param theMailStatus the itsMailStatus to set
	 */
	public void setMailStatus(String theMailStatus)
	{
		this.itsMailStatus = theMailStatus;
	}
	
	/**
	 * @return the itsMailSentInfo
	 */
	public String getMailSentInfo()
	{
		return this.itsMailSentInfo;
	}
	
	/**
	 * @param theMailSentInfo the itsMailSent to set
	 */
	public void setMailSentInfo(String theMailSentInfo)
	{
		this.itsMailSentInfo = theMailSentInfo;
	}
	
	/**
	 * @return the itsRegSubject
	 */
	public String getRegSubject()
	{
		return this.itsRegSubject;
	}
	
	/**
	 * @param theRegSubject the itsRegSubject to set
	 */
	public void setRegSubject(String theRegSubject)
	{
		this.itsRegSubject = theRegSubject;
	}
	
	/**
	 * @param theServerProperties the itsServerProperties to set
	 */
	public void setServerProperties(ResourceBundle theServerProperties)
	{
		this.itsServerProperties = theServerProperties;
	}

	/**
	 * @return the itsServerProperties
	 */
	public ResourceBundle getServerProperties()
	{
		return this.itsServerProperties;
	}
}