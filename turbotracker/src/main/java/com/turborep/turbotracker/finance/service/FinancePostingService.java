/**
 * 
 */
package com.turborep.turbotracker.finance.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Cofiscalyear;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.finance.dao.GlReporting;
import com.turborep.turbotracker.finance.exception.DrillDownException;
import com.turborep.turbotracker.finance.exception.FinancePostingException;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.user.dao.UserBean;


/**
 * 
 * Handles CRUD services for Finance DrillDown
 * 
 * @author Jenith
 */

public interface FinancePostingService {

	public Integer postVeBill(Integer veBillid, String tableColumn,String acType) throws FinancePostingException;
	
	
}
