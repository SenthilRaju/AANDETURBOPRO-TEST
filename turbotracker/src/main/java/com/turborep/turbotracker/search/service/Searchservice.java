package com.turborep.turbotracker.search.service;

/**
 * Handles services for Search
 * @author SYSVINE\thulasi_ram
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.turborep.turbotracker.Rolodex.dao.RolodexBean;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.search.dao.SearchBean;
import com.turborep.turbotracker.search.exception.SearchException;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.util.JobUtil;

@Service("SearchServices")
@Transactional
public class Searchservice implements SearchServiceInterface {
	protected static Logger itsLogger = Logger.getLogger(Searchservice.class);

	@Resource(name = "sessionFactory")
	private SessionFactory itsSessionFactory;

	@Override
	public ArrayList<?> getRolodex() {
		itsLogger.debug("Retriving all Rolodex");
		String aRolodexQry = "SELECT * from rxMaster";
		Session aSessionaObj = null;
		ArrayList<RolodexBean> aQryList = new ArrayList<RolodexBean>();
		try {
			RolodexBean aRolodexBean = null;
			aSessionaObj = itsSessionFactory.openSession();
			Query aQueryaObj = aSessionaObj.createSQLQuery(aRolodexQry);
			Iterator<?> aIterator = aQueryaObj.list().iterator();
			while (aIterator.hasNext()) {
				aRolodexBean = new RolodexBean();
				Object[] aObj = (Object[]) aIterator.next();
				aRolodexBean.setName((String) aObj[0]);
				aRolodexBean.setPhone1((String) aObj[1]);
				aQryList.add(aRolodexBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSessionaObj.flush();
			aSessionaObj.close();
		}
		return aQryList;
	}

	@Override
	public List<?> getjobSearchResultsWithJob(String theJob) {
		itsLogger.debug("Retrieving all getjobSearchResultsWithJob");
		String aJobSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index where searchText like '"
				+ "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%' and entity = 'Job'";
		Session aSession = null;
		ArrayList<SearchBean> aQueryList = new ArrayList<SearchBean>();
		try {
			SearchBean aSearchJob = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aSearchJob = new SearchBean();
				Object[] aObj = (Object[]) aIterator.next();
				aSearchJob.setSearchId((Integer) aObj[0]);
				/** searchId **/
				aSearchJob.setEntity((String) aObj[1]);
				/** entity */
				aSearchJob.setSearchText((String) aObj[2]);
				/** searchTExt */
				aSearchJob.setResultedTableName((String) aObj[3]);
				/** tableName */
				aSearchJob.setPk_fields((Integer) aObj[4]);
				/** joMasterId */
				aQueryList.add(aSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getJobSearch(SearchBean theSearchJob) {
		itsLogger.debug("Retrieving all getJobSearch");
		String aJobSearchSelectQry = "SELECT joMaster.joMasterID, CONCAT(joMaster.JobNumber, '_', joMaster.Description, '_', tsUserLogin.FullName) AS searchText "
				+ "FROM joMaster JOIN tsUserLogin ON joMaster.cuAssignmentID0 = tsUserLogin.UserLoginID";
		Session aSession = null;
		ArrayList<SearchBean> aQueryList = new ArrayList<SearchBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new SearchBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setPk_fields((Integer) aObj[0]);
				/** joMasterId */
				theSearchJob.setSearchText((String) aObj[1]);
				/** jobNumber */
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getJobSearchList(AutoCompleteBean theSearchBean,
			String theJob) {
		itsLogger.debug("Query to Select getJobSearch");
	/*	String aSearchSelectQry = "SELECT entity, searchText, resultedTableName, pk_fields, bidDate FROM search_index WHERE entity ='"
				+ theSearchBean.getEntity()
				+ "' AND "
				+ "searchText like '"
				+ "%"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' AND bidDate <> '0000-00-00 00:00:00' "
				+ "UNION SELECT entity, searchText, resultedTableName, pk_fields, bidDate FROM search_index WHERE entity ='"
				+ theSearchBean.getEntity()
				+ "' AND "
				+ "searchText like '"
				+ "%"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' AND bidDate <> '0000-00-00 00:00:00' ORDER BY bidDate DESC"; */
		
//		String aSearchSelectQry = "SELECT entity, searchText, resultedTableName, pk_fields, search_index.bidDate, joMaster.Description FROM search_index JOIN joMaster ON (search_index.pk_fields = joMaster.joMasterID) AND entity ='"
//				+ theSearchBean.getEntity()
//				+ "' AND "
//				+ "searchText like '"
//				+ "%"
//				+ JobUtil.removeSpecialcharacterswithslash(theJob)
//				+ "%' AND search_index.bidDate <> '0000-00-00 00:00:00' "
//				+ "UNION "
//				
//				+"SELECT entity, searchText, resultedTableName, pk_fields, search_index.bidDate,joMaster.Description FROM search_index JOIN joMaster ON (search_index.pk_fields = joMaster.joMasterID) AND entity ='"
//				+ theSearchBean.getEntity()
//				+ "' AND "
//				+ "searchText like '"
//				+ "%"
//				+ JobUtil.removeSpecialcharacterswithslash(theJob)
//				+ "%' AND search_index.bidDate <> '0000-00-00 00:00:00' ORDER BY Description";
				
				
		String aSearchSelectQry = "SELECT entity, searchText, resultedTableName, pk_fields, search_index.bidDate , joMaster.Description FROM search_index JOIN joMaster ON (search_index.pk_fields = joMaster.joMasterID) AND entity ='"
			    + theSearchBean.getEntity()
			    + "' AND "
			    + "searchText like '"
			    + "%"
			    + JobUtil.removeSpecialcharacterswithslash(theJob)
			       + "%' AND search_index.bidDate <> '0000-00-00 00:00:00' ORDER BY search_index.bidDate DESC ,searchText ASC";
				
				
		System.out.println("aSearchSelectQry"+aSearchSelectQry);
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				int aIndex = 0;
				
				String[] aText = new String[3];
				String[][] aTransArray = new String[5][3];
				String aName1 = (String) aObj[2];
				System.out.println("Resulted Table Name"+aName1);
				String aName = (String) aObj[1];
				String aN = aName.replace("|", "`");
				aText = aN.split("`");
				aTransArray[aIndex] = aText;
				
				String aNumber = aTransArray[aIndex][0];
				String aSearch = aTransArray[aIndex][1];
				System.out.println("================>search JOb Name"+aNumber+aSearch);
				theSearchBean.setValue(aSearch + " [" + aNumber + "]");
				/** searchText */
				theSearchBean.setLabel(aSearch + " [" + aNumber + "]");
				/** tableName */
				theSearchBean.setId((Integer) aObj[3]);
				
				/** jobNumber */
				aQueryList.add(theSearchBean);
			}
			if (aQueryList.isEmpty()) {
				theSearchBean.setId(0);
				theSearchBean.setValue(" ");
				theSearchBean.setLabel(" ");
				aQueryList.add(theSearchBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public void insert(List<?> theJobSearch) {
		itsLogger.debug("insert");
		String aSearchInsertQry = "INSERT INTO search_index (searchId, entity, searchText, resultedTableName, pk_fields) values(?, ?, ?, ?, ?)";
		Session aSession = null;
		SearchBean aSearchList = new SearchBean();
		try {
			aSession = itsSessionFactory.openSession();
			Transaction aSearchInsert = aSession.beginTransaction();
			Query aQuery = aSession.createSQLQuery(aSearchInsertQry);
			for (int i = 0; i < theJobSearch.size(); i++) {
				aSearchList = (SearchBean) theJobSearch.get(i);
				aQuery.setInteger(1, aSearchList.getSearchId());
				aQuery.setString(1, aSearchList.getEntity());
				aQuery.setString(2, aSearchList.getSearchText());
				aQuery.setString(3, aSearchList.getResultedTableName());
				aQuery.setInteger(4, aSearchList.getPk_fields());
				aSession.save(aQuery);
				if (i % 100 == 0) {
					aSession.flush();
					aSession.clear();
				}
			}
			aSearchInsert.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchInsertQry = null;
		}
	}
	
	

	@Override
	public List<?> getRolodexSearchList(AutoCompleteBean theSearchRolodex,
			String theRolodexSearchText) {
		itsLogger.debug("getRolodexSearchList");
		String aRolodex = theSearchRolodex.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aVendors = aRolodexSear[0];
		String aCustomers = aRolodexSear[1];
		String aEmployeelist = aRolodexSear[2];
		
		
		/*String aArchitect = aRolodexSear[3];
		String aEngineer = aRolodexSear[4];
		String aGeneralcontractors = aRolodexSear[5];
		String aArchtOREngg = aRolodexSear[6];*/
		
		String category1="";
		String category2="";
		String category3="";
		String category4="";
		String category5="";
		String category6="";
		String category7="";
		String category8="";
		
		/** For Special Characters **/
		
		
	//	theRolodexSearchText = theRolodexSearchText.replaceAll("'", "\'\'");
		theRolodexSearchText = theRolodexSearchText.replaceAll("\\'", "\\\\'");
		
		Sysinfo sysinfo = null;
		try {
			sysinfo = getRoldexCategories(1);
		} catch (UserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(sysinfo!=null){
			category1=sysinfo.getRxMasterCategory1desc();
			category2=sysinfo.getRxMasterCategory2desc();
			category3= sysinfo.getRxMasterCategory3desc();
			category4= sysinfo.getRxMasterCategory4desc();
			category5= sysinfo.getRxMasterCategory5desc();
			category6= sysinfo.getRxMasterCategory6desc();
			category7=sysinfo.getRxMasterCategory7desc();
			category8=sysinfo.getRxMasterCategory8desc();
		}
		String totalcategory="";
		if(category1!=null &&category1!=""){
			totalcategory="'category1'";
		}
		if(category2!=null &&category2!=""){
				if(totalcategory!=""){
					totalcategory=totalcategory+","+"'category2'";
				}else{
					totalcategory="'category2'";
				}
		}
		if(category3!=null &&category3!=""){
			if(totalcategory!=""){
				totalcategory=totalcategory+","+"'category3'";
			}else{
				totalcategory="'category3'";
			}
		}
		if(category4!=null &&category4!=""){
			if(totalcategory!=""){
				totalcategory=totalcategory+","+"'category4'";
			}else{
				totalcategory="'category4'";
			}
		}
		if(category5!=null &&category5!=""){
			if(totalcategory!=""){
				totalcategory=totalcategory+","+"'category5'";
			}else{
				totalcategory="'category5'";
			}
		}
		if(category6!=null &&category6!=""){
			if(totalcategory!=""){
				totalcategory=totalcategory+","+"'category6'";
			}else{
				totalcategory="'category6'";
			}
		}
		if(category7!=null &&category7!=""){
			if(totalcategory!=""){
				totalcategory=totalcategory+","+"'category7'";
			}else{
				totalcategory="'category7'";
			}
		}
		if(category8!=null &&category8!=""){
			if(totalcategory!=""){
				totalcategory=totalcategory+","+"'category8'";
			}else{
				totalcategory="'category8'";
			}
		}
		

		
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ aVendors
				+ "','"
				+ aCustomers
				+ "','"
				+ aEmployeelist
				+ "',"+totalcategory+") AND searchText like '"
				+ "%|"
				+ JobUtil.removeSpecialcharacterswithslash(theRolodexSearchText)
				+ "%'"
				+ " Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ aVendors
				+ "','"
				+ aCustomers
				+ "','"
				+ aEmployeelist
				+ "',"+totalcategory+ ") AND searchText like '"
				+ "%"
				+ JobUtil.removeSpecialcharacterswithslash(theRolodexSearchText)
				+ "%' ORDER BY searchText";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			System.out.println(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchRolodex = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchRolodex.setId((Integer) aObj[0]);
				theSearchRolodex.setPk_field((Integer) aObj[4]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0];
				}
				
				String aSearchRelpace = "";
				if (aEntity.equals("employeelist")) {
					aSearchRelpace = aEntity.replace("employeelist", "EMP");
				} else if (aEntity.equals("vendors")) {
					aSearchRelpace = aEntity.replace("vendors", "VEND");
				} else if (aEntity.equals("customers")) {
					aSearchRelpace = aEntity.replace("customers", "CUST");
				} else if (aEntity.equals("Category1")) {
					//aSearchRelpace = aEntity.replace("architect", "ARCH");
					aSearchRelpace = aEntity.replace("Category1", JobUtil.subStringvalue(category1,0,4));
				} else if (aEntity.equals("Category2")) {
					//aSearchRelpace = aEntity.replace("engineer", "ENGR");
					aSearchRelpace = aEntity.replace("Category2", JobUtil.subStringvalue(category2,0,4));
				} else if (aEntity.equals("Category3")) {
					/*aSearchRelpace = aEntity.replace("generalcontractors",
							"G.C");*/
					aSearchRelpace = aEntity.replace("Category3",JobUtil.subStringvalue(category3,0,4));
				} else if (aEntity.equals("Category4")) {
					/*aSearchRelpace = aEntity.replace("architect/engineer",
							"ARCH/ENGR");*/
					aSearchRelpace = aEntity.replace("Category4",JobUtil.subStringvalue(category4,0,4));
				}else if (aEntity.equals("Category5")) {
					/*aSearchRelpace = aEntity.replace("architect/engineer",
					"ARCH/ENGR");*/
					aSearchRelpace = aEntity.replace("Category5",JobUtil.subStringvalue(category5,0,4));
				}else if (aEntity.equals("Category6")) {
					/*aSearchRelpace = aEntity.replace("architect/engineer",
					"ARCH/ENGR");*/
					aSearchRelpace = aEntity.replace("Category6",JobUtil.subStringvalue(category6,0,4));
				}else if (aEntity.equals("Category7")) {
					/*aSearchRelpace = aEntity.replace("architect/engineer",
					"ARCH/ENGR");*/
					aSearchRelpace = aEntity.replace("Category7",JobUtil.subStringvalue(category7,0,4));
				}else if (aEntity.equals("Category8")) {
					/*aSearchRelpace = aEntity.replace("architect/engineer",
					"ARCH/ENGR");*/
					aSearchRelpace = aEntity.replace("Category8",JobUtil.subStringvalue(category8,0,4));
				}
				theSearchRolodex.setValue(aSearchRelpace + ": " + aSearchtext);
				/** searchText */
				theSearchRolodex.setLabel(aSearchRelpace + ": " + aSearchtext);
				/** tableName */
				aQueryList.add(theSearchRolodex);
			}
			if (aQueryList.isEmpty()) {
				theSearchRolodex.setValue(" ");
				theSearchRolodex.setLabel(" ");
				aQueryList.add(theSearchRolodex);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}
	
	public Sysinfo getRoldexCategories(int theUserinfoId) throws UserException {
		String	aSysPriSelectQry = "SELECT rxMasterCategory1Desc,rxMasterCategory2Desc,rxMasterCategory3Desc,rxMasterCategory4Desc,rxMasterCategory5Desc,rxMasterCategory6Desc,rxMasterCategory7Desc,rxMasterCategory8Desc,prPriceLevel0,prPriceLevel1,prPriceLevel2,prPriceLevel3,prPriceLevel4,prPriceLevel5,prPriceLevel6 FROM sysInfo where sysInfoID="+theUserinfoId;
		Session aSession=null;
		Sysinfo sysinfo=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			if(aIterator.hasNext()) {
				sysinfo = new Sysinfo();
				Object[] aObj = (Object[])aIterator.next();
				sysinfo.setRxMasterCategory1desc((String)aObj[0]);
				sysinfo.setRxMasterCategory2desc((String)aObj[1]);
				sysinfo.setRxMasterCategory3desc((String)aObj[2]);
				sysinfo.setRxMasterCategory4desc((String)aObj[3]);
				sysinfo.setRxMasterCategory5desc((String)aObj[4]);
				sysinfo.setRxMasterCategory6desc((String)aObj[5]);
				sysinfo.setRxMasterCategory7desc((String)aObj[6]);
				sysinfo.setRxMasterCategory8desc((String)aObj[7]);
				
				sysinfo.setPrPriceLevel0((String)aObj[8]);
				sysinfo.setPrPriceLevel1((String)aObj[9]);
				sysinfo.setPrPriceLevel2((String)aObj[10]);
				sysinfo.setPrPriceLevel3((String)aObj[11]);
				sysinfo.setPrPriceLevel4((String)aObj[12]);
				sysinfo.setPrPriceLevel5((String)aObj[13]);
				
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			UserException aUserException = new UserException(e.getCause().getMessage(), e);
			throw aUserException;
		} finally {
			aSession.flush();
			aSession.close();
			aSysPriSelectQry=null;
		}
		return sysinfo;
	}

	@Override
	public List<?> getrolodexSearchResultsWithRolodex(String theRolodex,
			String theSearchText, String theSearch) {
		itsLogger.debug("getrolodexSearchResultsWithRolodex");
		String aRolodexSearchSelectQry = null;
		if (theSearch == "") {
			aRolodexSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
					+ "where entity like "
					+ "'%"
					+ JobUtil.removeSpecialcharacterswithslash(theRolodex)
					+ "%'"
					+ " AND searchText like " + "'%" + theSearchText + "%'";
		} else if (theSearchText == "") {
			aRolodexSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
					+ "where entity like "
					+ "'%"
					+ JobUtil.removeSpecialcharacterswithslash(theRolodex)
					+ "%'"
					+ " AND searchText like " + "'%" + JobUtil.removeSpecialcharacterswithslash(theSearch) + "%'" + " ";
		} else {
			aRolodexSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
					+ "where entity like "
					+ "'%"
					+ JobUtil.removeSpecialcharacterswithslash(theRolodex)
					+ "%'"
					+ " AND searchText like "
					+ "'%"
					+ JobUtil.removeSpecialcharacterswithslash(theSearchText)
					+ "%'"
					+ " AND searchText like " + "'%" + JobUtil.removeSpecialcharacterswithslash(theSearch) + "%'" + " ";
		}
		Session aSession = null;
		ArrayList<SearchBean> aQueryList = new ArrayList<SearchBean>();
		try {
			SearchBean aSearchJob = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRolodexSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aSearchJob = new SearchBean();
				Object[] aObj = (Object[]) aIterator.next();
				aSearchJob.setSearchId((Integer) aObj[0]);
				/** searchId **/
				aSearchJob.setEntity((String) aObj[1]);
				/** entity */
				aSearchJob.setSearchText((String) aObj[2]);
				/** searchTExt */
				aSearchJob.setResultedTableName((String) aObj[3]);
				/** tableName */
				aSearchJob.setPk_fields((Integer) aObj[4]);
				/** joMasterId */
				aQueryList.add(aSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aRolodexSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getrolodexSearchResultsWithRolodexDetails(String theRxNumber) {
		itsLogger.debug("Retrieving getrolodexSearchResultsWithRolodexDetails");
		String aRolodexSearchSelectQry = "SELECT s.entity, s.pk_fields, s.searchText, j.rxCustomerID, m.Name FROM search_index s "
				+ "LEFT JOIN joMaster j ON j.joMasterID=s.pk_fields "
				+ "RIGHT JOIN rxMaster m ON m.rxMasterId = j.rxCustomerID where s.pk_fields = '"
				+ theRxNumber + "'";
		Session aSession = null;
		ArrayList<SearchBean> aQueryList = new ArrayList<SearchBean>();
		try {
			SearchBean aSearchJob = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRolodexSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aSearchJob = new SearchBean();
				Object[] aObj = (Object[]) aIterator.next();
				String entity = (String) aObj[0];
				if (!entity.equals("Job")) {
					aSearchJob.setEntity((String) aObj[1]);
					/** entity */
					aSearchJob.setSearchText((String) aObj[2]);
					/** searchTExt */
					aSearchJob.setPk_fields((Integer) aObj[3]);
					/** joMasterId */
					aSearchJob.setResultedTableName((String) aObj[4]);
					/** tableName */
					aQueryList.add(aSearchJob);
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aRolodexSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getRolodexSearchCustomerList(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("Retrieving getRolodexSearchCustomerList");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aCustomers = aRolodexSear[1];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aCustomers)
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aCustomers)
				+ "') AND searchText like '" + "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = "";
				if(aName.contains("||")){
					aN = aName.replace("||", ",  ");
				}
				else{
					aN = aName.replace("|", ",  ");
				}
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0] + ",  " + aSearch[1];
				}else if (aSearch.length == 1) {
					aSearchtext = aSearch[0];
				}
				String aSearchRelpace = "";
				if (aEntity.equals("customers")) {
					aSearchRelpace = aEntity.replace("customers", "CUST");
				}
				theSearchJob.setValue(aSearchRelpace + ": " + aSearchtext);
				/** searchText */
				theSearchJob.setLabel(/* searchRelpace+": "+ */aSearchtext);
				/** tableName */
				theSearchJob.setManufactureID((Integer) aObj[4]);
				aQueryList.add(theSearchJob);
			}
			if (aQueryList.isEmpty()) {
				theSearchJob.setValue(" ");
				theSearchJob.setLabel(" ");
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getRolodexSearchEmployeeList(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("getRolodexSearchEmployeeList");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aEmployeelist = aRolodexSear[2];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aEmployeelist)
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aEmployeelist)
				+ "') AND searchText like '" + "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String searchtext = null;
				if (aSearch.length == 3) {
					searchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					searchtext = aSearch[0];
				}
				String searchRelpace = "";
				if (aEntity.equals("employeelist")) {
					searchRelpace = aEntity.replace("employeelist", "EMP");
				}
				theSearchJob.setValue(searchRelpace + ": " + searchtext);
				/** searchText */
				theSearchJob.setLabel(/* searchRelpace+": "+ */searchtext);
				/** tableName */
				aQueryList.add(theSearchJob);
			}
			if (aQueryList.isEmpty()) {
				theSearchJob.setValue(" ");
				theSearchJob.setLabel(" ");
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getRolodexSearchVendorList(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("getRolodexSearchVendorList");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aVendors = aRolodexSear[0];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aVendors)
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aVendors)
				+ "') AND searchText like '"
				+ "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0];
				}
				String aSearchRelpace = "";
				if (aEntity.equals("vendors")) {
					aSearchRelpace = aEntity.replace("vendors", "VEND");
				}
				theSearchJob.setManufactureID((Integer) aObj[4]);
				theSearchJob.setValue(aSearchRelpace + ": " + aSearchtext);
				/** searchText */
				theSearchJob.setLabel(/* searchRelpace+": "+ */aSearchtext);
				/** tableName */
				aQueryList.add(theSearchJob);
			}
			if (aQueryList.isEmpty()) {
				theSearchJob.setValue(" ");
				theSearchJob.setLabel(" ");
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<SearchBean> getLoginIDForSales(Integer theRolodex) {
		itsLogger.debug("getLoginIDForSales");
		String aRolodexSearchSelectQry = "SELECT CreatedByID, ChangedByID FROM rxMaster WHERE rxMasterID = '"
				+ theRolodex + "'";
		Session aSession = null;
		ArrayList<SearchBean> aQueryList = new ArrayList<SearchBean>();
		try {
			SearchBean aSearchJob = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aRolodexSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aSearchJob = new SearchBean();
				Object[] aObj = (Object[]) aIterator.next();
				if (aObj[0] != null)
					aSearchJob.setCreatedByID((Integer) aObj[0]);
				/** entity */
				if (aObj[1] != null)
					aSearchJob.setChangedByID((Integer) aObj[1]);
				/** tableName */
				aQueryList.add(aSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aRolodexSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getRolodexSearchCustomerListForUpComingBids(
			AutoCompleteBean theSearchJob, String theJob) {
		itsLogger.debug("getRolodexSearchCustomerListForUpComingBids");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aCustomers = aRolodexSear[1];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aCustomers)
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aCustomers)
				+ "') AND searchText like '" + "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				// String entity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0];
				}
				/*
				 * String searchRelpace = ""; if(entity.equals("customers")) {
				 * searchRelpace = entity.replace("customers", "CUST"); }
				 */
				theSearchJob.setValue(/* searchRelpace+": "+ */aSearchtext);
				/** searchText */
				theSearchJob.setLabel(/* searchRelpace+": "+ */aSearchtext);
				/** tableName */
				theSearchJob.setManufactureID((Integer) aObj[4]);
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getRolodexSearchArchitectList(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("getRolodexSearchArchitectList");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aArchitect = aRolodexSear[3];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aArchitect)
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aArchitect)
				+ "') AND searchText like '" + "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0];
				}
				String searchRelpace = "";
				if (aEntity.equals("architect")) {
					searchRelpace = aEntity.replace("architect", "ARCH");
				}
				theSearchJob.setValue(searchRelpace + ": " + aSearchtext);
				/** searchText */
				theSearchJob.setLabel(/* searchRelpace+": "+ */aSearchtext);
				/** tableName */
				theSearchJob.setManufactureID((Integer) aObj[4]);
				aQueryList.add(theSearchJob);
			}
			if (aQueryList.isEmpty()) {
				theSearchJob.setValue(" ");
				theSearchJob.setLabel(" ");
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getRolodexSearchEngineerList(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("getRolodexSearchEngineerList");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aEngineer = aRolodexSear[4];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aEngineer)
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ JobUtil.removeSpecialcharacterswithslash(aEngineer)
				+ "') AND searchText like '"
				+ "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0];
				}
				String searchRelpace = "";
				if (aEntity.equals("engineer")) {
					searchRelpace = aEntity.replace("engineer", "ENGR");
				}
				theSearchJob.setValue(searchRelpace + ": " + aSearchtext);
				/** searchText */
				theSearchJob.setLabel(/* searchRelpace+": "+ */aSearchtext);
				/** tableName */
				theSearchJob.setManufactureID((Integer) aObj[4]);
				aQueryList.add(theSearchJob);
			}
			if (aQueryList.isEmpty()) {
				theSearchJob.setValue(" ");
				theSearchJob.setLabel(" ");
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getJobSearchListHome(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("Query to Select getJobSearchListHome");
		String aSearchSelectQry = "SELECT entity, searchText, resultedTableName, pk_fields, bidDate FROM search_index WHERE entity ='"
				+ theSearchJob.getEntity()
				+ "' AND "
				+ "searchText like '"
				+ "%|"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT entity, searchText, resultedTableName, pk_fields, bidDate FROM search_index WHERE entity ='"
				+ theSearchJob.getEntity()
				+ "' AND "
				+ "searchText like '"
				+ "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				int aIndex = 0;
				String[] aText = new String[3];
				String[][] aTransArray = new String[5][3];
				String aName = (String) aObj[1];
				String aN = aName.replace("|", "`");
				aText = aN.split("`");
				aTransArray[aIndex] = aText;
				String number = aTransArray[aIndex][0];
				String search = aTransArray[aIndex][1];
				theSearchJob.setValue(search + " [" + number + "]");
				/** searchText */
				theSearchJob.setLabel(search + " [" + number + "]");
				/** tableName */
				theSearchJob.setId((Integer) aObj[3]);
				/** jobNumber */
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry =null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getSearchUserList(AutoCompleteBean theAutoCompleteBean,
			String theUserSearch) {
		itsLogger.debug("Query getSearchUserList");
		String aSearchSelectQry = "SELECT UserLoginID, LoginName, FullName, Initials FROM tsUserLogin WHERE LoginName LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theUserSearch)
				+ "%' AND LoginName != 'admin' ORDER BY LoginName ASC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theAutoCompleteBean.setId((Integer) aObj[0]);
				theAutoCompleteBean.setEntity((String) aObj[2]);
				String aLoginName = (String) aObj[1];
				String aInitial = (String) aObj[3];
				theAutoCompleteBean
						.setLabel(aLoginName + " [" + aInitial + "]");
				theAutoCompleteBean
						.setValue(aLoginName + " [" + aInitial + "]");
				aQueryList.add(theAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				theAutoCompleteBean.setValue(" ");
				theAutoCompleteBean.setLabel(" ");
				aQueryList.add(theAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getVendorBillPayList(AutoCompleteBean theSearchJob,
			String theJob) {
		itsLogger.debug("getRolodexSearchVendorList");
		String aRolodex = theSearchJob.getRolodexEntity();
		String[] aRolodexSear = aRolodex.split(",");
		String aVendors = aRolodexSear[0];
		String aSearchSelectQry = "SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ aVendors
				+ "') AND searchText like '"
				+ JobUtil.removeSpecialcharacterswithslash(theJob)
				+ "%' Union SELECT searchId, entity, searchText, resultedTableName, pk_fields FROM search_index "
				+ "where entity IN ('"
				+ aVendors
				+ "') AND searchText like '"
				+ "%" + JobUtil.removeSpecialcharacterswithslash(theJob) + "%'";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theSearchJob = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theSearchJob.setId((Integer) aObj[0]);
				String aEntity = (String) aObj[1];
				String aName = (String) aObj[2];
				String aN = aName.replace("|", ",  ");
				String[] aSearch = aN.split(",  ");
				String aSearchtext = null;
				if (aSearch.length == 3) {
					aSearchtext = aSearch[0] + ",  " + aSearch[2];
				} else if (aSearch.length == 2) {
					aSearchtext = aSearch[0];
				}
				String aSearchRelpace = "";
				if (aEntity.equals("vendors")) {
					aSearchRelpace = aEntity.replace("vendors", "VEND");
				}
				theSearchJob.setManufactureID((Integer) aObj[4]);
				theSearchJob.setValue(aSearchtext);
				/** searchText */
				theSearchJob.setLabel(aSearchtext);
				/** tableName */
				aQueryList.add(theSearchJob);
			}
			if (aQueryList.isEmpty()) {
				theSearchJob.setValue(" ");
				theSearchJob.setLabel(" ");
				aQueryList.add(theSearchJob);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getSearchAccountsList(AutoCompleteBean theAutoCompleteBean,
			String theAccountSearch) {
		StringBuilder aSearchSelectQry = new StringBuilder(
				"SELECT coAccountID, Number, Description FROM coAccount WHERE Description LIKE '%")
				.append(JobUtil.removeSpecialcharacterswithslash(theAccountSearch))
				.append("%' ")
				.append("UNION ")
				.append("SELECT coAccountID, Number, Description FROM coAccount WHERE Number LIKE '%")
				.append(JobUtil.removeSpecialcharacterswithslash(theAccountSearch)).append("%' ORDER BY Number ASC;");
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		List<?> aQrList =null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry.toString());
			aQrList = aQuery.list();
			if(aQrList.size()>0)
			{
			Iterator<?> aIterator = aQrList.iterator();
			while (aIterator.hasNext()) {
				theAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theAutoCompleteBean.setId((Integer) aObj[0]);
				theAutoCompleteBean.setEntity("coAccount");
				String aAccNumber = (String) aObj[1];
				String aAccDescription = (String) aObj[2];
				theAutoCompleteBean.setLabel(aAccDescription + " ["
						+ aAccNumber + "]");
				theAutoCompleteBean.setValue(aAccNumber);
				aQueryList.add(theAutoCompleteBean);
			}
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<?> getSearchShipViaList(AutoCompleteBean theAutoCompleteBean,
			String theShipViaSearch) {
		String aSearchSelectQry = "SELECT veShipViaID, Description, InActive, TrackURL, TrackPrefix, TrackSuffix FROM veShipVia WHERE Description LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theShipViaSearch) + "%'  ORDER BY Description ASC;";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theAutoCompleteBean.setId((Integer) aObj[0]);
				theAutoCompleteBean.setLabel((String) aObj[1]);
				theAutoCompleteBean.setValue((String) aObj[1]);
				aQueryList.add(theAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				theAutoCompleteBean.setValue(" ");
				theAutoCompleteBean.setLabel(" ");
				aQueryList.add(theAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<AutoCompleteBean> getInventorySearch(String theSearchString)
			throws SearchException {
		String aSearchSelectQry = "SELECT prMasterID, prDepartmentID, ItemCode, Description FROM prMaster WHERE Description LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%' OR ItemCode LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%';";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				Integer prMasterID = (Integer) aObj[0];
				aAutoCompleteBean.setId(prMasterID);
				aAutoCompleteBean.setEntity("prMaster");

				String aItemCode = (String) aObj[2];
				String aDescription = (String) aObj[3];
				aAutoCompleteBean.setLabel(aDescription + " [" + aItemCode
						+ "]");
				aAutoCompleteBean.setValue(prMasterID + "|" + aDescription
						+ "|" + aItemCode);
				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public ArrayList<AutoCompleteBean> getUserInitialsList(String theUserLogin)
			throws SearchException {
		StringBuilder salesselectQry = new StringBuilder(
				"SELECT UserLoginId, Initials ").append("FROM tsUserLogin ")
				.append("WHERE Initials like '%").append(JobUtil.removeSpecialcharacterswithslash(theUserLogin))
				.append("%' ")
				.append("AND LoginName != 'admin' ORDER BY Initials ASC;");
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			AutoCompleteBean aUserbean = null;
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(salesselectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aUserbean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				aUserbean.setId((Integer) aObj[0]);
				/** UserLoginID */
				aUserbean.setValue((String) aObj[1]);
				/** UserName */
				aUserbean.setLabel((String) aObj[1]);
				aQueryList.add(aUserbean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			salesselectQry = null;
		}
		return aQueryList;
	}

	/*public List<AutoCompleteBean> searchPOs(String theSearchString)
			throws SearchException {
		StringBuilder aPOSelectStr = new StringBuilder(
				"SELECT vePO.vePOID AS poID, vePO.rxVendorID AS VendorID, ")
				.append("vePO.PONumber AS PONumber, vePO.VendorOrderNumber AS VendPONumber, rm.Name as Vendor ")
				.append("FROM vePO vePO LEFT JOIN rxMaster rm ON vePO.rxVendorID = rm.rxMasterID ");
		StringBuilder aPOSearchQry = new StringBuilder(aPOSelectStr)
				.append("WHERE PONumber LIKE '%").append(theSearchString)
				.append("%' ").append("UNION ").append(aPOSelectStr)
				.append("WHERE vePO.VendorOrderNumber LIKE '%")
				.append(theSearchString).append("%' ").append("UNION ")
				.append(aPOSelectStr).append("WHERE rm.Name LIKE '%")
				.append(theSearchString).append("%' LIMIT 100");
		
		StringBuilder aPOSearchStr = new StringBuilder("SELECT vePO.vePOID AS poID, vePO.rxVendorID AS VendorID, vePO.PONumber AS PONumber, vePO.VendorOrderNumber AS VendPONumber, " + 
								" rm.Name AS Vendor,jm.JobNumber,vePO.CustomerPONumber FROM vePO vePO " + 
								" LEFT JOIN rxMaster rm ON vePO.rxVendorID = rm.rxMasterID " +
								" LEFT JOIN joRelease jr ON jr.joReleaseID = vePO.joReleaseID " +
								" LEFT JOIN joMaster jm ON jm.joMasterID=jr.joMasterID" +
								" LEFT JOIN joCustPO jc ON jc.joMasterID=jr.joMasterID" +
								" LEFT JOIN joChange jch ON jch.joMasterID=jr.joMasterID" +
							    " WHERE (jm.CustomerPONumber LIKE '%"+theSearchString+"%' OR jch.CustomerPONumber LIKE '%"+theSearchString+"%' OR jc.CustomerPONumber1 LIKE '%"+theSearchString+"%' OR "
					    		+ "jc.CustomerPONumber2 LIKE '%"+theSearchString+"%' OR jc.CustomerPONumber3 LIKE '%"+theSearchString+"%' OR "
					    		+ "jc.CustomerPONumber4 LIKE '%"+theSearchString+"%' OR jc.CustomerPONumber5 LIKE '%"+theSearchString+"%' OR "
					    		+ "PONumber LIKE '%"+theSearchString+"%'  OR vePO.VendorOrderNumber LIKE '%"+theSearchString+"%' OR " 
					    		+ "rm.Name LIKE '%"+theSearchString+"%' )") ;
		Session aSession = null;
		ArrayList<AutoCompleteBean> aResultedPOList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aPO = null;
		try {
			aSession = itsSessionFactory.openSession();
			//Query aQuery = aSession.createSQLQuery(aPOSearchQry.toString());
			Query aQuery = aSession.createSQLQuery(aPOSearchStr.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPO = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				String aVendorName = (String) aObj[4];
				//String aPONumber = (String) aObj[2];
				Integer aPOID = (Integer) aObj[0];
				
				String jobNo="";
				String acusPONumber="";
				if(aObj[5] !=null)
					jobNo =!aObj[5].equals("")? "["+(String)aObj[5] +"]" : "";
				if(aObj[6] !=null)
					acusPONumber =!aObj[6].equals("")? "["+(String)aObj[6] +"]" : "";
				
				aPO.setValue(aVendorName + " " + acusPONumber + " " + jobNo);
				*//** searchText *//*
				aPO.setLabel(aVendorName + " " + acusPONumber + " " + jobNo);
				*//** tableName *//*
				aPO.setId(aPOID);
				*//** jobNumber *//*
				aResultedPOList.add(aPO);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new SearchException(excep.getMessage(), excep);
		}finally {
			aSession.flush();
			aSession.close();
			aPOSelectStr = null;
			aPOSearchQry =null;
			aPOSearchStr=null;
		}
		return aResultedPOList;
	}*/
	
	public List<AutoCompleteBean> searchPOs(String theSearchString)
			throws SearchException {
		/*StringBuilder aPOSelectStr = new StringBuilder(
				"SELECT vePO.vePOID AS poID, vePO.rxVendorID AS VendorID, ")
				.append("vePO.PONumber AS PONumber, vePO.VendorOrderNumber AS VendPONumber, rm.Name as Vendor ")
				.append("FROM vePO vePO LEFT JOIN rxMaster rm ON vePO.rxVendorID = rm.rxMasterID ");
		StringBuilder aPOSearchQry = new StringBuilder(aPOSelectStr)
				.append("WHERE PONumber LIKE '%").append(theSearchString)
				.append("%' ").append("UNION ").append(aPOSelectStr)
				.append("WHERE vePO.VendorOrderNumber LIKE '%")
				.append(theSearchString).append("%' ").append("UNION ")
				.append(aPOSelectStr).append("WHERE rm.Name LIKE '%")
				.append(theSearchString).append("%' LIMIT 100");*/
		
		/*StringBuilder aPOSearchStr = new StringBuilder("SELECT vePO.vePOID AS poID, vePO.rxVendorID AS VendorID, vePO.PONumber AS PONumber, vePO.VendorOrderNumber AS VendPONumber, " + 
								" rm.Name AS Vendor,jm.JobNumber,vePO.CustomerPONumber FROM vePO vePO " + 
								" LEFT JOIN rxMaster rm ON vePO.rxVendorID = rm.rxMasterID " +
								" LEFT JOIN joRelease jr ON jr.joReleaseID = vePO.joReleaseID " +
								" LEFT JOIN joMaster jm ON jm.joMasterID=jr.joMasterID" +
								" LEFT JOIN joCustPO jc ON jc.joMasterID=jr.joMasterID" +
								" LEFT JOIN joChange jch ON jch.joMasterID=jr.joMasterID" +
							    " WHERE (jm.CustomerPONumber LIKE '%"+theSearchString+"%' OR jch.CustomerPONumber LIKE '%"+theSearchString+"%' OR jc.CustomerPONumber1 LIKE '%"+theSearchString+"%' OR "
					    		+ "jc.CustomerPONumber2 LIKE '%"+theSearchString+"%' OR jc.CustomerPONumber3 LIKE '%"+theSearchString+"%' OR "
					    		+ "jc.CustomerPONumber4 LIKE '%"+theSearchString+"%' OR jc.CustomerPONumber5 LIKE '%"+theSearchString+"%' OR "
					    		+ "PONumber LIKE '%"+theSearchString+"%'  OR vePO.VendorOrderNumber LIKE '%"+theSearchString+"%' OR " 
					    		+ "rm.Name LIKE '%"+theSearchString+"%' )") ;*/
		StringBuilder aPOSearchStr = new StringBuilder(
						"Select * from ("+
						"SELECT jm.joMasterID,jm.Description,rm.Name,jm.CustomerPONumber as ponumber,jm.JobNumber FROM joMaster jm	"+
						"  LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                           "+
						" WHERE jm.CustomerPONumber LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                          "+
						" UNION                                                                             "+
						" SELECT jm.joMasterID,jm.Description,rm.Name,jch.CustomerPONumber as ponumber,jm.JobNumber FROM joMaster jm "+
						" LEFT JOIN joChange jch ON jm.joMasterID=jch.joMasterID                            "+
						" LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                            "+
						" WHERE jch.CustomerPONumber LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                         "+
						" UNION                                                                             "+
						" SELECT jm.joMasterID,jm.Description,rm.Name,jc.CustomerPONumber1 as ponumber,jm.JobNumber FROM joMaster jm "+
						" LEFT JOIN joCustPO jc ON jc.joMasterID=jm.joMasterID                              "+
						" LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                            "+
						" WHERE jc.CustomerPONumber1 LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                         "+
						" UNION                                                                             "+
						" SELECT jm.joMasterID,jm.Description,rm.Name,jc.CustomerPONumber2 as ponumber,jm.JobNumber FROM joMaster jm "+
						" LEFT JOIN joCustPO jc ON jc.joMasterID=jm.joMasterID                              "+
						" LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                            "+
						" WHERE jc.CustomerPONumber2 LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                         "+
						" UNION                                                                             "+
						" SELECT jm.joMasterID,jm.Description,rm.Name,jc.CustomerPONumber3 as ponumber,jm.JobNumber FROM joMaster jm "+
						" LEFT JOIN joCustPO jc ON jc.joMasterID=jm.joMasterID                              "+
						" LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                            "+
						" WHERE jc.CustomerPONumber3 LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                         "+
						" UNION                                                                             "+
						" SELECT jm.joMasterID,jm.Description,rm.Name,jc.CustomerPONumber4 as ponumber,jm.JobNumber FROM joMaster jm "+
						" LEFT JOIN joCustPO jc ON jc.joMasterID=jm.joMasterID                              "+
						" LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                            "+
						" WHERE jc.CustomerPONumber4 LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                         "+
						" UNION                                                                             "+
						" SELECT jm.joMasterID,jm.Description,rm.Name,jc.CustomerPONumber5 as ponumber,jm.JobNumber FROM joMaster jm "+
						" LEFT JOIN joCustPO jc ON jc.joMasterID=jm.joMasterID                              "+
						" LEFT JOIN rxMaster rm ON jm.rxCustomerID=rm.rxMasterID                            "+
						" WHERE jc.CustomerPONumber5 LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'                                         "+
				        " ) AS dummytable  order by Name"    
				);
		
		Session aSession = null;
		ArrayList<AutoCompleteBean> aResultedPOList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aPO = null;
		try {
			aSession = itsSessionFactory.openSession();
			//Query aQuery = aSession.createSQLQuery(aPOSearchQry.toString());
			Query aQuery = aSession.createSQLQuery(aPOSearchStr.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPO = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				Integer joMasterID=(Integer)aObj[0];
				String jobname=(String)aObj[1];
				String customerName=(String)aObj[2];
				String custpoNumber=(String)aObj[3];
				String jobnumber=(String)aObj[4];
				aPO.setId(joMasterID);
				aPO.setValue(customerName+"["+custpoNumber+"]["+jobnumber+"]");
				aPO.setLabel(customerName+"["+custpoNumber+"]["+jobnumber+"]");
				aPO.setColumn1(jobname);
				aPO.setColumn2(jobnumber);
				aPO.setColumn3(customerName);
				aResultedPOList.add(aPO);
			}
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new SearchException(excep.getMessage(), excep);
		}finally {
			aSession.flush();
			aSession.close();
			/*aPOSelectStr = null;
			aPOSearchQry =null;*/
			aPOSearchStr=null;
		}
		return aResultedPOList;
	}

	/**
	 * Returns the Vendor List from the parameter passed.
	 * 
	 * @param theVendorSearch
	 * @return List of Vendor Names.
	 */
	@Override
	public List<AutoCompleteBean> getVendorSearch(String theSearchString)
			throws SearchException {
		String aSearchSelectQry = "SELECT rxMasterID,Name FROM rxMaster WHERE IsVendor =1 AND InActive<>1 AND Name LIKE '"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString) + "%' ORDER BY Name;";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				// Integer prMasterID = (Integer) aObj[0];
				// aAutoCompleteBean.setId(prMasterID);
				// aAutoCompleteBean.setEntity("prMaster");

				// String aItemCode = (String) aObj[2];
				// String aDescription = (String) aObj[3];
				Integer rxMasterID = (Integer) aObj[0];
				aAutoCompleteBean.setValue(rxMasterID.toString());
				aAutoCompleteBean.setLabel((String) aObj[1]);

				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<AutoCompleteBean> getCustomerSearch(String theSearchString)
			throws SearchException {
		// String aSearchSelectQry =
		// "SELECT rxmaster.rxMasterID,rxmaster.Name FROM rxmaster JOIN rxAddress AS address ON rxmaster.rxMasterID = address.rxMasterID WHERE rxmaster.Name LIKE '"
		// + theSearchString + "%';";
		String aSearchSelectQry = "SELECT rxmaster.rxMasterID,rxmaster.Name FROM cuMaster cumaster, rxMaster rxmaster, rxAddress address WHERE cumaster.cuMasterID = rxmaster.rxMasterID AND"
				+ " cumaster.cuMasterID = address.rxMasterID AND rxmaster.rxMasterID = address.rxMasterID AND"
				+ " rxmaster.Name IS NOT NULL AND rxmaster.Name LIKE '"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString) + "%' ORDER BY rxmaster.Name";

		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				// Integer prMasterID = (Integer) aObj[0];
				// aAutoCompleteBean.setId(prMasterID);
				// aAutoCompleteBean.setEntity("prMaster");

				// String aItemCode = (String) aObj[2];
				// String aDescription = (String) aObj[3];
				Integer rxMasterID = (Integer) aObj[0];
				aAutoCompleteBean.setValue(rxMasterID.toString());
				aAutoCompleteBean.setLabel((String) aObj[1]);

				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}

	@Override
	public List<AutoCompleteBean> getPOSearch(String theSearchString)
			throws SearchException {

		// String aSearchSelectQry =
		// "SELECT vePoid,ponumber FROM vepo WHERE ponumber LIKE '"+theSearchString+"%';";
		// String aSearchSelectQry =
		// "SELECT vo.vepoid,vo.ponumber,rx.name,vo.createdon FROM vepo AS vo INNER JOIN rxMaster AS rx ON (vo.rxVendorID= rx.rxMasterId) WHERE ((rx.name LIKE '%"
		// + theSearchString
		// + "%' OR vo.vePoid LIKE '%"
		// + theSearchString
		// + "%' OR vo.ponumber LIKE '%"
		// + theSearchString
		// + "%' OR vo.createdon LIKE '%" + theSearchString + "%'));"; 0,3,1,4,5
		String aSearchSelectQry = "SELECT vo.vePOID, vo.CreatedOn, vo.joReleaseID,vo.PONumber,j.Description,rm.Name, CASE WHEN CONCAT(vo.vePOID,IFNULL(vo.CreatedOn,''), vo.PONumber, rm.Name,j.Description) LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%' THEN 'and' ELSE '' END AS result FROM vePO vo LEFT JOIN rxMaster rm ON vo.rxVendorID = rm.rxMasterID INNER JOIN joRelease jo ON (jo.joReleaseID = vo.joReleaseID) INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) HAVING result != '' ORDER BY vo.vePOID DESC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				// Integer prMasterID = (Integer) aObj[0];
				// aAutoCompleteBean.setId(prMasterID);
				// aAutoCompleteBean.setEntity("prMaster");

				// String aItemCode = (String) aObj[2];
				// String aDescription = (String) aObj[3];0,3,1,4,5
				Integer iVepoid = (Integer) aObj[0];
				String createdOn = " ";
				if (aObj[1] != null) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss.0");
					Date d = formatter.parse(aObj[1].toString());
					createdOn = new SimpleDateFormat("dd/MM/yyyy").format(d)
							.toString();
				}
				String jobName = " ";
				if (aObj[4] != null) {
					jobName = aObj[4].toString();
					if (jobName.length() > 9) {
						jobName = jobName.substring(0, 9) + "..";
					}
				}
				String vendorName = " ";
				if (aObj[5] != null) {
					vendorName = aObj[5].toString();
					if (vendorName.length() > 9) {
						vendorName = vendorName.substring(0, 9) + "..";
					}

				}
				aAutoCompleteBean.setValue(iVepoid.toString());
				aAutoCompleteBean.setLabel("[ " + aObj[0].toString() + " || "
						+ aObj[3].toString() + " || " + createdOn + " || "
						+ jobName + " || " + vendorName + " ]");
				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			System.out.println("output error" + e.getMessage());
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;
		}
		return aQueryList;

	}

	@Override
	public List<AutoCompleteBean> getSOSearch(String theSearchString)
			throws SearchException {

		// String aSearchSelectQry =
		// "SELECT cuSOID,sonumber FROM cuso WHERE sonumber LIKE '"
		// + theSearchString + "%';";
		// String aSearchSelectQry =
		// "SELECT vo.cusoid,vo.createdon,rx.name,vo.sonumber FROM cuso AS vo INNER JOIN rxMaster AS rx ON (vo.rxCustomerID= rx.rxMasterId) WHERE ((rx.name LIKE '%"
		// + theSearchString
		// + "%' OR vo.cusoid LIKE '%"
		// + theSearchString
		// + "%' OR vo.sonumber LIKE '%"
		// + theSearchString
		// + "%' OR vo.createdon LIKE '%" + theSearchString + "%'));";
		// String aSearchSelectQry =
		// "SELECT vo.cusoid,vo.createdon,rx.name,vo.sonumber,jo.description FROM cuso AS vo INNER JOIN rxMaster AS rx  ON (vo.rxCustomerID= rx.rxMasterId) INNER JOIN joMaster AS jo ON (vo.rxCustomerID = jo.rxCustomerID) WHERE ((rx.name LIKE '%"
		// + theSearchString
		// + "%' OR vo.cusoid LIKE '%"
		// + theSearchString
		// + "%' OR vo.sonumber LIKE '%"
		// + theSearchString
		// + "%' OR jo.createdon LIKE '%" + theSearchString + "%' ));";
		String aSearchSelectQry = "SELECT so.cuSOID, so.CreatedOn, rm.Name,j.Description, so.CostTotal, so.SONumber,CASE WHEN CONCAT(so.CreatedOn, so.joReleaseID, so.rxCustomerID, so.SONumber, rm.Name,j.Description) LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%' THEN 'and' ELSE '' END AS result FROM cuSO so LEFT JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID INNER JOIN joRelease jo ON (jo.joReleaseID = so.joReleaseID)	INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) HAVING result != '' ORDER BY so.cuSOID DESC";
		boolean flag = false;
		boolean isDateSearchflag = false;
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			List bList = aQuery.list();
			if (bList.size() > 0)
				flag = true;
			else {
				aSearchSelectQry = "SELECT cuSOID,rm.Name FROM cuSO so JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID WHERE rm.Name LIKE '"
						+ JobUtil.removeSpecialcharacterswithslash(theSearchString) + "%';";
				aQuery = aSession.createSQLQuery(aSearchSelectQry);
				List aList =aQuery.list(); 
				if (aList.size() > 0)
					flag = true;
				else {
					// SimpleDateFormat dateFormat = new
					// SimpleDateFormat("yyyy-MM-dd");
					// String dateSO = dateFormat.format(theSearchString);
					// Date date = dateFormat.parse(theSearchString);
					if (theSearchString.contains("/")) {
						String[] date = theSearchString.split("/");
						if (date.length >= 2) {
							theSearchString = date[2] + "-" + date[0] + "-"
									+ date[1];
						} else if (date.length >= 1) {
							theSearchString = date[1] + "-" + date[0];
						} else {
							theSearchString = date[0];
						}

					}
					aSearchSelectQry = "SELECT cuSOID,createdOn FROM cuSO WHERE createdOn LIKE '%"
							+ JobUtil.removeSpecialcharacterswithslash(theSearchString) + "%';";
					aQuery = aSession.createSQLQuery(aSearchSelectQry);
					List cList = aQuery.list();
					if (cList.size() > 0) {
						flag = true;
						isDateSearchflag = true;
					} else {
						aAutoCompleteBean = new AutoCompleteBean();
						aAutoCompleteBean.setValue(" ");
						aAutoCompleteBean.setLabel(" ");
						aQueryList.add(aAutoCompleteBean);
					}

				}
			}
			if (flag) {

				Iterator<?> aIterator = bList.iterator();
				while (aIterator.hasNext()) {
					aAutoCompleteBean = new AutoCompleteBean();
					Object[] aObj = (Object[]) aIterator.next();
					Integer icuSOID = (Integer) aObj[0];
					aAutoCompleteBean.setValue(icuSOID.toString());
					// if (isDateSearchflag) {
					// Timestamp stamp = (Timestamp) aObj[1];
					// Date date = new Date(stamp.getTime());
					// System.out.println("date format" + date.toString());
					// SimpleDateFormat outputFormat = new SimpleDateFormat(
					// "MM/dd/yyyy");
					// SimpleDateFormat inputFormat = new SimpleDateFormat(
					// "E MMM dd HH:mm:ss Z yyyy");
					// Date d = inputFormat.parse(date.toString());
					// String dateSO = outputFormat.format(d);
					// System.out.println("Date search is----->" + dateSO);
					// aAutoCompleteBean.setLabel((String) dateSO.toString());
					// } else
					// aAutoCompleteBean.setLabel((String) aObj[1]);
					String createdOn = " ";
					if (aObj[1] != null) {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss.0");
						Date d = formatter.parse(aObj[1].toString());
						createdOn = new SimpleDateFormat("dd/MM/yyyy")
								.format(d).toString();
					}
					String vendorName = aObj[2].toString();
					/*if (vendorName.length() > 9) {
						vendorName = vendorName.substring(0, 9) + "...";
					}*/
					String jobName = aObj[3].toString();
					/*if (jobName.length() > 9) {
						jobName = jobName.substring(0, 9) + "...";
					}*/
					if(aObj[4]==null){
						aObj[4]="";
					}
					aAutoCompleteBean.setLabel("[" + aObj[5] + " || "
							+ createdOn + " || " + vendorName + " || "
							+ jobName + " || " + aObj[4] + "]");

					aQueryList.add(aAutoCompleteBean);
				}
			}

			/*
			 * if(aQueryList.isEmpty()){ aAutoCompleteBean = new
			 * AutoCompleteBean(); aAutoCompleteBean.setValue(" ");
			 * aAutoCompleteBean.setLabel(" ");
			 * aQueryList.add(aAutoCompleteBean); }
			 */
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}
	
	/*@Override
	public List<AutoCompleteBean> getCuInvoiceSearch(String theSearchString)
			throws SearchException {		
		String aSearchSelectQry = "SELECT cu.cuInvoiceID, cu.CreatedOn, rm.Name,j.Description, cu.CostTotal, cu.InvoiceNumber,cu.rxCustomerID,cu.CustomerPONumber,CASE WHEN CONCAT(cu.CreatedOn, cu.joReleaseDetailID, cu.rxCustomerID, cu.InvoiceNumber, rm.Name,j.Description,cu.CustomerPONumber) LIKE '%"
				+ theSearchString
				+ "%' THEN 'and' ELSE '' END AS result FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID INNER JOIN joRelease jo ON (jo.joReleaseID = cu.joReleaseDetailID)	INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) HAVING result != '' ORDER BY cu.cuInvoiceID DESC";
		boolean flag = false;
		boolean isDateSearchflag = false;
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			//System.out.println("CuInvoice ::" + aQuery.list().size());
			List mainlist=aQuery.list();
			System.out.println("MainQuery=="+aSearchSelectQry);
			if (mainlist.size() > 0)
				flag = true;
			else {
				aSearchSelectQry = "SELECT cuInvoiceID,rm.Name,cu.rxCustomerID FROM cuInvoice cu JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID WHERE rm.Name LIKE '"
						+ theSearchString + "%';";
				aQuery = aSession.createSQLQuery(aSearchSelectQry);
				System.out.println("firstSubquery=="+aSearchSelectQry);
				if (aQuery.list().size() > 0)
					flag = true;
				else {					
					if (theSearchString.contains("/")) {
						String[] date = theSearchString.split("/");
						if (date.length >= 2) {
							theSearchString = date[2] + "-" + date[0] + "-"
									+ date[1];
						} else if (date.length >= 1) {
							theSearchString = date[1] + "-" + date[0];
						} else {
							theSearchString = date[0];
						}

					}
					aSearchSelectQry = "SELECT cuInvoiceID,CreatedOn,rxCustomerID FROM cuInvoice WHERE CreatedOn LIKE '%"
							+ theSearchString + "%';";
					aQuery = aSession.createSQLQuery(aSearchSelectQry);
					System.out.println("SecondSubquery=="+aSearchSelectQry);
					if (aQuery.list().size() > 0) {
						flag = true;
						isDateSearchflag = true;
					} else {
						aAutoCompleteBean = new AutoCompleteBean();
						aAutoCompleteBean.setValue(" ");
						aAutoCompleteBean.setLabel(" ");
						aQueryList.add(aAutoCompleteBean);
					}

				}
			}
			if (flag) {

				Iterator<?> aIterator = mainlist.iterator();
				while (aIterator.hasNext()) {
					aAutoCompleteBean = new AutoCompleteBean();
					Object[] aObj = (Object[]) aIterator.next();
					Integer icuSOID = (Integer) aObj[0];
					aAutoCompleteBean.setValue(icuSOID.toString());					
					String createdOn = " ";
					if (aObj[1] != null) {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss.0");
						Date d = formatter.parse(aObj[1].toString());
						createdOn = new SimpleDateFormat("dd/MM/yyyy")
								.format(d).toString();
					}
					String vendorName = aObj[2].toString();
					if (vendorName.length() > 9) {
						vendorName = vendorName.substring(0, 9) + "...";
					}
					String jobName = aObj[3].toString();
					if (jobName.length() > 9) {
						jobName = jobName.substring(0, 9) + "...";
					}
					aAutoCompleteBean.setLabel("[" + aObj[5] + " || "
							+ createdOn + " || " + vendorName + " || "
							+ jobName + " || " + aObj[4] + " || " + aObj[6] +" || " + aObj[7] + "]");

					aQueryList.add(aAutoCompleteBean);
				}
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aQueryList;
	}*/
	
	@Override
	public List<AutoCompleteBean> getCuInvoiceSearch(String theSearchString)
			throws SearchException {		
		String aSearchSelectQry = "SELECT cu.cuInvoiceID, cu.CreatedOn, rm.Name,j.Description, cu.CostTotal, cu.InvoiceNumber,cu.rxCustomerID,cu.CustomerPONumber,CASE WHEN CONCAT(cu.CreatedOn, cu.joReleaseDetailID, cu.rxCustomerID, cu.InvoiceNumber, rm.Name,j.Description,cu.CustomerPONumber) LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%' THEN 'and' ELSE '' END AS result FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID INNER JOIN joRelease jo ON (jo.joReleaseID = cu.joReleaseDetailID)	INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) HAVING result != '' ORDER BY cu.cuInvoiceID DESC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			List mainlist=aQuery.list();
			System.out.println("MainQuery=="+aSearchSelectQry);
				Iterator<?> aIterator = mainlist.iterator();
				while (aIterator.hasNext()) {
					aAutoCompleteBean = new AutoCompleteBean();
					Object[] aObj = (Object[]) aIterator.next();
					Integer icuSOID = (Integer) aObj[0];
					aAutoCompleteBean.setValue(icuSOID.toString());					
					String createdOn = " ";
					if (aObj[1] != null) {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss.0");
						Date d = formatter.parse(aObj[1].toString());
						createdOn = new SimpleDateFormat("dd/MM/yyyy")
								.format(d).toString();
					}
					String vendorName = aObj[2].toString();
					String jobName = aObj[3].toString();
					aAutoCompleteBean.setLabel("[" + aObj[5] + " || "
							+ createdOn + " || " + vendorName + " || "
							+ jobName + " || " + aObj[4] + " || " + aObj[6] +" || " + aObj[7] + "]");

					aQueryList.add(aAutoCompleteBean);
				}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getCuNameSearch(String theSearchString)
			throws SearchException {		
		String aSearchSelectQry = "SELECT DISTINCT rm.Name,CASE WHEN CONCAT(cu.CreatedOn, cu.joReleaseDetailID, cu.rxCustomerID, cu.InvoiceNumber, rm.Name,j.Description,cu.CustomerPONumber) LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%' THEN 'and' ELSE '' END AS result,cu.rxCustomerID FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID INNER JOIN joRelease jo ON (jo.joReleaseID = cu.joReleaseDetailID)	INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) HAVING result != '' ORDER BY rm.Name";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			List mainlist=aQuery.list();
				Iterator<?> aIterator = mainlist.iterator();
				while (aIterator.hasNext()) {
					aAutoCompleteBean = new AutoCompleteBean();
					Object[] aObj = (Object[]) aIterator.next();
					aAutoCompleteBean.setValue(aObj[0].toString());					
	
					aAutoCompleteBean.setLabel(aObj[0].toString());
					aAutoCompleteBean.setId((Integer) aObj[2]);
					aQueryList.add(aAutoCompleteBean);
				}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getCuInvoiceIDSearch(String theSearchString)
			throws SearchException {
		String aSearchSelectQry = "SELECT cu.InvoiceNumber,cu.cuInvoiceID FROM cuInvoice cu WHERE  cu.InvoiceNumber LIKE '"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%' ORDER BY cu.cuInvoiceID DESC";
		/*String aSearchSelectQry = "SELECT DISTINCT cu.InvoiceNumber, cu.InvoiceNumber LIKE '%"
				+ theSearchString
				+ "%' AS result FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID INNER JOIN joRelease jo ON (jo.joReleaseID = cu.joReleaseDetailID)	INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) HAVING result != '' ORDER BY cu.cuInvoiceID DESC";*/
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			List mainlist=aQuery.list();
				Iterator<?> aIterator = mainlist.iterator();
				while (aIterator.hasNext()) {
					aAutoCompleteBean = new AutoCompleteBean();
					Object[] aObj = (Object[]) aIterator.next();
					aAutoCompleteBean.setValue(aObj[0].toString());					
	
					aAutoCompleteBean.setLabel(aObj[0].toString());

					aQueryList.add(aAutoCompleteBean);
				}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getWarehouseTransferSearch(String theSearchString)
			throws SearchException {
		
		
		String aSearchSelectQry = "SELECT prWH.prTransferID, prWH.TransferDate, prWH.TransNumber,rm.City, CASE WHEN CONCAT(TransferDate,TransNumber, rm.City) LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%'  THEN 'and' ELSE '' END AS result FROM prWHtransfer prWH , prWarehouse rm WHERE ReceivedDate IS NULL HAVING result != '' ORDER BY prWH.prTransferID DESC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				// Integer prMasterID = (Integer) aObj[0];
				// aAutoCompleteBean.setId(prMasterID);
				// aAutoCompleteBean.setEntity("prMaster");
				// String aItemCode = (String) aObj[2];
				// String aDescription = (String) aObj[3];0,3,1,4,5
				Integer iVepoid = (Integer) aObj[0];
				String createdOn = " ";
				if (aObj[1] != null) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss.0");
					Date d = formatter.parse(aObj[1].toString());
					createdOn = new SimpleDateFormat("dd/MM/yyyy").format(d)
							.toString();
				}
				/*String jobName = " ";
				if (aObj[4] != null) {
					jobName = aObj[4].toString();
					if (jobName.length() > 9) {
						jobName = jobName.substring(0, 9) + "..";
					}
				}
				String vendorName = " ";
				if (aObj[5] != null) {
					vendorName = aObj[5].toString();
					if (vendorName.length() > 9) {
						vendorName = vendorName.substring(0, 9) + "..";
					}

				}*/
				aAutoCompleteBean.setValue(iVepoid.toString());
				aAutoCompleteBean.setLabel("[ " + aObj[0].toString() + " || "
						+ aObj[2].toString() + " || " + createdOn + " || "
						+ aObj[3].toString() + " ]");
				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			System.out.println("output error" + e.getMessage());
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;

	}

	@Override
	public List<?> getJobSearchListContact(AutoCompleteBean theAutoCompleteBean,
			String theUserSearch) {
		itsLogger.debug("Query getSearchUserList");
		String aSearchSelectQry = "SELECT joMasterID,Description,JobNumber,BidDate FROM joMaster where JobStatus in (0,-4,6,1) AND Description LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theUserSearch)+"%' AND BidDate>=CURDATE() ORDER BY BidDate DESC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				theAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				theAutoCompleteBean.setId((Integer) aObj[0]);
				String createdOn="";
				if(aObj[3]!=null){
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss.0");
					Date d = formatter.parse(aObj[3].toString());
					createdOn = new SimpleDateFormat("dd/MM/yyyy").format(d)
							.toString();	
				}
				
				
				String desc = ""+aObj[1]+" | "+aObj[2]+" | "+createdOn;
				
				
				theAutoCompleteBean
						.setLabel(desc);
				theAutoCompleteBean
						.setValue((String)aObj[1]);
				aQueryList.add(theAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				theAutoCompleteBean.setValue(" ");
				theAutoCompleteBean.setLabel(" ");
				aQueryList.add(theAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getVendorInvoiceList(String theSearchString)
			throws SearchException {
		
		String aSearchSelectQry = "SELECT DISTINCT veBillID, BillDate, PONumber, InvoiceNumber, veBill.rxMasterID,"+ 
			" CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo,veBill.vePOID,"+			
			" CASE WHEN CONCAT(DATE_FORMAT(BillDate,'%m/%d/%Y'), IFNULL(PONumber,''), InvoiceNumber,rxMaster.Name) LIKE '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%' THEN 'and' ELSE '' END AS result"+			
			" FROM  veBill LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID"+
			" LEFT OUTER JOIN vePO ON veBill.vePOID = vePO.vePOID WHERE veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL HAVING result != ''  ORDER BY BillDate DESC";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			System.out.println("query:: " + aSearchSelectQry);
			System.out.println("query:: " + aQuery.list().size());
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				// Integer prMasterID = (Integer) aObj[0];
				// aAutoCompleteBean.setId(prMasterID);
				// aAutoCompleteBean.setEntity("prMaster");

				// String aItemCode = (String) aObj[2];
				// String aDescription = (String) aObj[3];0,3,1,4,5
				Integer aVeBillId = (Integer) aObj[0];
				String billDate = " ";
				if (aObj[1] != null) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss.0");
					Date d = formatter.parse(aObj[1].toString());
					billDate = new SimpleDateFormat("MM/dd/yyyy").format(d)
							.toString();
				}
				String poNumber = " ";
				if (aObj[2] != null) {
					poNumber = aObj[2].toString();
					/*if (poNumber.length() > 9) {
						poNumber = poNumber.substring(0, 9) + "..";
					}*/
				}
				String invoiceNumber = " ";
				if (aObj[3] != null) {
					invoiceNumber = aObj[3].toString();
					/*if (invoiceNumber.length() > 9) {
						invoiceNumber = invoiceNumber.substring(0, 9) + "..";
					}*/

				}
				String vendorName = " ";
				if (aObj[5] != null) {
					vendorName = aObj[5].toString();
					/*if (vendorName.length() > 9) {
						vendorName = vendorName.substring(0, 9) + "..";
					}*/

				}
				Integer aVepoId = null;
				if(aObj[6] != null)					
					aVepoId = (Integer) aObj[6];
				aAutoCompleteBean.setValue(aVeBillId.toString());
				aAutoCompleteBean.setLabel("[ " + billDate + " || "
						+ poNumber + " || " + invoiceNumber + " || "
						+ vendorName +" || " + aVepoId + " ]");
				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			System.out.println("output error" + e.getMessage());
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;

	}
	
	@Override
	public List<AutoCompleteBean> getSearchCustomer(String theSearchString)
			throws SearchException {		
		String aSearchSelectQry = "SELECT DISTINCT rm.Name,rm.rxMasterID FROM cuMaster cu left join rxMaster rm ON cu.cuMasterID = rm.rxMasterID where rm.Name like '%"+JobUtil.removeSpecialcharacterswithslash(theSearchString)+"%'  ORDER BY rm.Name";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			List mainlist=aQuery.list();
				Iterator<?> aIterator = mainlist.iterator();
				while (aIterator.hasNext()) {
					aAutoCompleteBean = new AutoCompleteBean();
					Object[] aObj = (Object[]) aIterator.next();
					aAutoCompleteBean.setValue(aObj[0].toString());					
	
					aAutoCompleteBean.setLabel(aObj[0].toString());
					aAutoCompleteBean.setId((Integer) aObj[1]);
					aQueryList.add(aAutoCompleteBean);
				}
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			e.printStackTrace();
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry=null;
		}
		return aQueryList;
	}
	
	@Override
	public List<AutoCompleteBean> getInventoryproductSearch(String theSearchString)
			throws SearchException {
		String aSearchSelectQry = "SELECT prMasterID, prDepartmentID, ItemCode, Description FROM prMaster WHERE IsInventory=1 and (Description LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%' OR ItemCode LIKE '%"
				+ JobUtil.removeSpecialcharacterswithslash(theSearchString)
				+ "%');";
		Session aSession = null;
		ArrayList<AutoCompleteBean> aQueryList = new ArrayList<AutoCompleteBean>();
		AutoCompleteBean aAutoCompleteBean = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aSearchSelectQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aAutoCompleteBean = new AutoCompleteBean();
				Object[] aObj = (Object[]) aIterator.next();
				Integer prMasterID = (Integer) aObj[0];
				aAutoCompleteBean.setId(prMasterID);
				aAutoCompleteBean.setEntity("prMaster");

				String aItemCode = (String) aObj[2];
				String aDescription = (String) aObj[3];
				aAutoCompleteBean.setLabel(aDescription + " [" + aItemCode
						+ "]");
				aAutoCompleteBean.setValue(prMasterID + "|" + aDescription
						+ "|" + aItemCode);
				aQueryList.add(aAutoCompleteBean);
			}
			if (aQueryList.isEmpty()) {
				aAutoCompleteBean = new AutoCompleteBean();
				aAutoCompleteBean.setValue(" ");
				aAutoCompleteBean.setLabel(" ");
				aQueryList.add(aAutoCompleteBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			SearchException aSearchException = new SearchException(
					e.getMessage());
			throw aSearchException;
		} finally {
			aSession.flush();
			aSession.close();
			aSearchSelectQry = null;
		}
		return aQueryList;
	}
}
