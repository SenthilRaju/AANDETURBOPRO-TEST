package com.turborep.turbotracker.finance.dao;

import java.math.BigDecimal;

public class TrialBalance
{
  private Integer glTransactionId;
  private Integer coAccountID;
  private String coAccountNumber;
  private String coAccountDesc;
  private BigDecimal pdebits;
  private BigDecimal pcredits;
  private BigDecimal ydebits;
  private BigDecimal ycredits;
  
  public Integer getGlTransactionId()
  {
    return this.glTransactionId;
  }
  
  public void setGlTransactionId(Integer glTransactionId)
  {
    this.glTransactionId = glTransactionId;
  }
  
  public Integer getCoAccountID()
  {
    return this.coAccountID;
  }
  
  public void setCoAccountID(Integer coAccountID)
  {
    this.coAccountID = coAccountID;
  }
  
  public String getCoAccountNumber()
  {
    return this.coAccountNumber;
  }
  
  public void setCoAccountNumber(String coAccountNumber)
  {
    this.coAccountNumber = coAccountNumber;
  }
  
  public String getCoAccountDesc()
  {
    return this.coAccountDesc;
  }
  
  public void setCoAccountDesc(String coAccountDesc)
  {
    this.coAccountDesc = coAccountDesc;
  }
  
  public BigDecimal getPdebits()
  {
    return this.pdebits;
  }
  
  public void setPdebits(BigDecimal pdebits)
  {
    this.pdebits = pdebits;
  }
  
  public BigDecimal getPcredits()
  {
    return this.pcredits;
  }
  
  public void setPcredits(BigDecimal pcredits)
  {
    this.pcredits = pcredits;
  }
  
  public BigDecimal getYdebits()
  {
    return this.ydebits;
  }
  
  public void setYdebits(BigDecimal ydebits)
  {
    this.ydebits = ydebits;
  }
  
  public BigDecimal getYcredits()
  {
    return this.ycredits;
  }
  
  public void setYcredits(BigDecimal ycredits)
  {
    this.ycredits = ycredits;
  }
}
