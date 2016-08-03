<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Home</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
     <link rel="SHORTCUT ICON" href="./../resources/Icons/TurboRepIcon.png">
     <style type="text/css">
     	/* Columns section */
			.widget { width: 650px; }
			#columns .column { width: 645px;}
			#header #menu{ margin:0 auto; padding-top:1px; }
			#columns #column1 .widget { margin: 20px 5px 0 0px;}
			#columns #columnLast .widget { margin: 20px 15px 0 5px; }
		/* Columns section */
			#columns .column {float: none;width: 645px;	/* Min-height: */min-height: 50px;height: auto !important;height: 50px;}
		/* Select the header Icon */
			#mainMenuHomePage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuHomePage a{background:url('./../resources/styles/turbo-css/images/turbo_app_home_hover_icon.png') no-repeat 0px 4px;color:#FFF;} 
			.weatherWidget{height:215px; overflow-y: hidden; color: #FFF; padding-left: 10px;}
			.weatherWidget table tr td label{color: #FFF; font-family: Verdana, Arial, sans-serif; font-size: 12px;}
			.widgetBorders{border-radius : 5px;}
			.widgetHeader{background: -moz-linear-gradient(-90deg, #8C9BA9, #4A5F71); background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#8C9BA9), to(#4A5F71));}
			.ui-autocomplete {
			    max-height: 200px;
			    overflow-y: auto;
			}
     </style>
    
</head>
<body>
	<form id="homeFormId" name="fname" method="post" >
<div class="bodyDiv">
<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
<table align="center" class="generalSearchTable">
	<tr align="left">
		<td class="generalSearchTd">
			<div id="columns" align="center" class="generalSearchColumns">
		        <ul id="column1" class="column">
		            <li class="widgetForFullNameLable widget widgetBorders" id="intro">
		                <div class="widget-head widgetHeader">
		                    <h3 class="widget-headForNameDiv">Welcome ${sessionScope.user.fullName}</h3>
		                </div>
		                <div class="widget-content contentForSearchBoxLable" align="left">
		                    <p class="contentForSearchBoxpara" style="padding: 5px;">Please use this widget to search for Jobs, Employees, Vendors and Customers.</p>
		                </div>
		                <div class="menu">
							<div class="ui-widget">
								<div class="demo">
									<table class="font" align="center">
										<tr><td></td><td style="height:20px;"><div id="errormsg" style="color:red;padding-left:20px;display:none" class="rolodexLabel" ><p>Please use this widget to search for Jobs, Em</p></div></td></tr>
										<tr>
											<td  colspan="1" align="right"><label><b class="rolodexLabel">Rolodex:</b></label></td>
											<td class="rolodexLabelTD">
												<div>
													<input  id="rolodex" name="rolodex" type="text" class="rolodexSearchInput" style="border-width: 3px;" onKeydown='Javascript: if (event.keyCode==13) getRolodex();'  placeholder="Minimum 3 characters required to get Rolodex List">
												</div>
											</td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getRolodex()"> </td>
											<td><input type="button" value="Advanced" class="advancedSearchInactive" onclick=""> </td>
										</tr>
										<tr height="20px;">
											<td></td>
										</tr>
										<!-- <tr>
											<td align="right"><label style="padding-left: 10px"> Type Minimum Three Letters in this Job search box </label></td>
										</tr>  -->
										<tr>
											<td align="right"><label><b class="rolodexLabel">Job:</b></label></td>
											<td class="jobSearchTD"><input type="text" class="rolodexSearchInput" style="border-width: 3px;" title="" onKeydown='Javascript: if (event.keyCode==13) getJobs();'id="jobsearch"  placeholder="Minimum 3 characters required to get Job List"></td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getJobs()" > </td> 
											<td><input type="button" value="Advanced" class="advancedSearchbutton" onclick="openAdvancedSearchPopup()"> </td>
										</tr>
										<tr>
											<td align="right"></td>
											<td>
												<label style="padding-left:75px;"><a onclick="openNewJob(); createtpusage('New JOB','create','Info','Home,Create,New Job');" class="alink">+ New Job</a> </label>
												<label class="newlinkLabel" style="display:none;"><a onclick="openAdvancedSearchPopup()" class="alink">+ Advanced Search</a> </label>
												<label style="padding-left:105px; font-family:Verdana,Arial,sans-serif; font-weight:bold;"><a style="color:#4A5F71; cursor: pointer;" class="alink" onclick="QuickbookPopup()">+ Quick Book</a></label>
											</td>
										</tr>
										<tr>
											<td align="right"><label><b class="rolodexLabel">Inventory:</b></label></td>
											<td class="rolodexLabelTD"><input type="text" class="rolodexSearchInput" style="border-width: 3px;" onKeydown='Javascript: if (event.keyCode==13) search();'id="inventorysearch" placeholder="Minimum 3 characters required to get Inventory list"></td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getInventory()"> </td>
											<td><input type="button" value="Advanced" class="advancedSearchInactive" onclick=""> </td>
										</tr>
										<tr height  = "20px">
											<td></td>
										</tr>
										<tr>
											<td align="right"><label><b class="rolodexLabel">PO:</b></label></td>
											<td class="rolodexLabelTD"><input type="text" class="rolodexSearchInput"  onKeydown='Javascript: if (event.keyCode==13) search();' id="posearch" style="border-width: 3px;" placeholder="Minimum 3 characters required to get PO list"></td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getPO()"> </td>
											<td><input type="button" value="Advanced" class="advancedSearchInactive" onclick=""> </td>
										</tr>
								</table>
								</div>
							</div>
						</div>
		            </li>
				</ul>
			</div>
		</td>
		
		<td class="rolodexSearchInput">
	  <!-- <input type="text" name="betweenfrom" id="betweenfrom" >
		<input type="text" name="betweento" id="betweento" >
	 <input type="button" name="" id="" value="tempinsert" onclick="insertintoemmaster()"/> -->  
			<div>
				<table id="lastOpenedJobs"></table>
			</div>
		</td>
	</tr>
</table>
<div align="center" class="realseNotesDiv">
	<table class="home_down">
		<tr>
			<td style="width: 300px;">
				<h3 class="versionLableH3">Version <%=ResourceBundle.getBundle("version").getString("app.jdbc.appversion") %>:</h3>
				<div id="home_down_image" class="down_home" style="width: 320px;">
					<ul class="realseNotesUl">
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #397  Vendor (Rolodex)-New UI & Functionality for Remit To on Addresses</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #443 Home (PO Search)-Customer PO Needs to Show in Search</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #445 Job (Quotes tab)-PDF Header and Logo Change</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #428 Email-Allow Users to Send to Multiple Addresses Throughout Program</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #440 Job (Release tab)-Need Abillity to Move Line Items in Purchase Order/Sales Order</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #444 Job (Release tab)-Functionality for 'Track' Button</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #449 Customer Invoice(Outside)-Need to reflect  inventory onHand without reducing allocated</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #452 Job (Release tab)-Do Not Include Tax in Billed and UnBilled</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• ID #434 Job (Release tab)- UI for Purchase Order, Inline Notes Should Lineup Under Description</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #987 Job (Release tab)-Sales Order Release Lines Are Duplicating</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #1066 Job (Release tab)-Duplicating Line Items on Purchase Order</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #1061 Job (Release tab)-Tax Defaults Not Working</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #1068 Customer (Payments)-Tax Balance on Customer Payment [DMW150901B1]</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #1072 Job (Release tab)-UI for Warehouse on Sales Order Not Holding</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #1075 Home (Quick Book)-Customer Tax Territory Not Autopopulating</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #992 Job (Release tab)-Paid Invoice Dates Not Consistent on Invoice vs. Customer Payment [CSY150227A1]</li>
							<li class="hightimglist"></li>
							<li class="imglist" >• BID #959 Job (Release tab)-Acknowledgement tab Quantities Column Missing</li>
							<li class="hightimglist"></li>
					</ul>
				</div>
			</td>
			<td>
				<div class="weatherWidget"></div>
			</td>
			<td>
			</td>
		</tr>
	</table>
</div>
</div>
		</form>
		<div class="divHeight"></div>
	</body>
	<div class="bodyDiv">
	<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	<div style="display: none;">
		<table>
			<tr>
				<td>
					<input type="text" id="homeHiddenUseNameID" value="${sessionScope.user.userName}">
				</td>
				<td>
					<input type="text" id="homeHiddenUserID" value="${sessionScope.user.userId}">
				</td>
			</tr>
		</table>
	</div>
	<div id="weather"></div>
	<div id="QuickbookpopupDiv">
	<form  id="QuickbookpopupForm" name="QuickbookpopupForm">
	<div id="errorDiv" style="color: red"></div>
		<table>
		        <tr>
		        	<input id="cusCreLiminQuickBookYes" type="hidden" value="${requestScope.cusCreLiminQuickBookYes }"/>
		        	<input id="chkUserCustomerCreditYN" type="hidden" value="${requestScope.chkUserCustomerCreditYN }"/>
		        	<input id="CreditHold" type="hidden"/>
					<td colspan="2"><label>Project: </label><span style="color: red">*</span>
					<input id="Quickbookprojectid" name="Quickbookprojectid" type="text"  style="width:430px;"></td>
					<td><label>Sales Order(Quote): </label></td>
					<td><input type="hidden" id="cusoId" name="cusoId"><input type="text" id="QuickBook_salesorderQuote" name="QuickBook_salesorderQuote" style="width: 80%">&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
				</tr>
				<tr>
					<td><label>Division:</label><span style="color: red" id="HomeDivisionSpanID"></span><!-- <input type="hidden" id="QuickbookDivisionid" name="QuickbookDivisionid">
					<input type="text" id="QuickbookDivision" name="QuickbookDivision"> -->&nbsp;
					<select class="headerManuOptionWidth" id="QuickbookDivisionid" name="QuickbookDivisionid">
									<option value=""> - Select - </option>
									<c:forEach var="coDivision" items="${requestScope.divisions}">
										<option value="${coDivision.coDivisionId}">
											<c:out value="${coDivision.description}" ></c:out>
										</option>
									</c:forEach>
								</select>

					<!-- <img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"> --></td>
					<td><label>Book Date: </label><input type="text" id="Quickbookdate" name="Quickbookdate" class="datepickerCustom"></td>
				
					<!--<td colspan="2"><label>Pick Type</label><input type="hidden" id="Quickbookjobtypeid" name="Quickbookjobtypeid">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 <select name="Quickbookjobtype" id="Quickbookjobtype" style="width:50%">
					<option value="">--Select--</option>
					<option value="1">Equipment</option>
					<option value="2">Controls</option>
					<option value="3">Service</option>
					<option value="4">Parts</option>
					<option value="5">Maintainence</option>
					</select> -->
					<!-- <input type="text" id="Quickbookjobtype" name="Quickbookjobtype"> &nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">--></td>
				</tr>
				<tr>
					<td colspan="2" >
								<fieldset style="width:480px" class= "custom_fieldset">
			 				<legend class="custom_legend"><label><b>Job Location</b></label></legend>
			 				<table id="addressField" >
				 				<tr>
				 					<td style="width: 90px"><label>Address: </label></td>
				 					<td><input type="text" id="jobcontractorcustomer" name="jobcontractorcustomer" class="validate[maxSize[100]" style="width: 350px;" value=""></td>
				 				</tr>
				 				<tr>
				 					<td style="width: 90px"><label></label></td>
									<td><input type="text" id="locationAddressID1" name="locationAddress1" class="validate[maxSize[100]" style="width: 350px;" value=""></td>
								</tr>
				 				<tr>
				 					<td style="width: 90px"><label></label></td>
									<td><input type="text" id="locationAddressID2" name="locationAddress2" class="validate[maxSize[40]" style="width: 350px;" value=""></td>
								</tr>
			 					<tr>
			 						<td  style="width: 90px"><label>City-State: </label></td>
			 						<td><input type="text" id="locationCity" name="locationCity" style="width: 140px;" value="" placeholder="Minimum 2 characters required">
											<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;<label> - </label>
			 								<input type="hidden" id="locationcityid" name="locationcityid" />
			 								<input type="text" id="locationState" name="locationState" style="width: 30px; text-transform: uppercase" value="" maxlength="2"  placeholder="1 char. requi.">
			 								<label>Zip: </label><input type="text" id="locationZipID" name="locationZipID" style="width: 75px;" value="">
			 						</td>
			 					</tr>
			 				</table>
						</fieldset>
					</td>
					<td colspan="2" rowspan="3">
					<textarea rows="20" cols="38" readonly="readonly">Quick buy/sell is used to enter track single buy sell orders.After completing this screen a job will be booked and you will be taken to the PO
					</textarea>
					</td>
				</tr>
				<tr >
				<td colspan="2"><label>Customer :</label><span style="color: red">*</span><input type="hidden" id="QuickbookCustomerId" name="QuickbookCustomerId"  ><input name="QuickbookCustomer_name" id="QuickbookCustomer_name" type="text" value=""  style="width:415px;" placeholder="Minimum 2 characters required">&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
				</tr>
				<tr >
					<!-- <td><label>Sales Rep:</label><span style="color: red">*</span><input type="hidden" id="salesRepId" name="empSalesMan"><input  type="text" id="Quickbook_salesRepsList"  value=""  placeholder="Minimum 1 characters required"  onkeyup="salesmanAdminValidation(this)" style="width: 49%">&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td> -->
					<td><label>Sales Rep:</label><span style="color: red">*</span><input type="hidden" id="salesRepId" name="empSalesMan"><input  type="text" id="Quickbook_salesRepsList"  value=""  placeholder="Minimum 1 characters required"  onkeyup="" style="width: 49%">&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
					<td><label>Customer PO #: </label><span style="color:red;" id="jobCPORequiredSpanID"></span><input type="text" id="customer_PO" name="customer_PO" style="width: 48%"></td>
				</tr>
				<tr>
					<td><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CSR:</label><input type="hidden" id="QuickbookCSRId"  name="QuickbookCSRId" ><input  type="text" id="Quickbook_CSRList"  value=""  placeholder="Minimum 1 characters required" style="width:60%">&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
					<td><label>Job Site Tax Territory:</label><span style="color: red">*</span><input type="hidden" id="TaxTerritory" name="TaxTerritory"  ><input  type="text" id="Quickbook_TaxTerritory"  value=""  placeholder="Minimum 2 characters required" style="width:30%">&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"> <label id = "TaxValue"></label></td>
				</tr>
				<tr>
					<td><label>Contract Amount:</label><span style="color: red" id="conAmt_labelid">*</span><input type="text" id="contractamount" name="contractamount"    style="width:40%"></td>
					<td><label>Estimated Cost: </label><span style="color: red" id="estAmt_labelid">*</span><input type="text" id="estimatedcost" name="estimatedcost" style="width:48%"></td>
				</tr>			
		</table>
	</form>
	<div>
		<br/>
		<hr/>
	</div>
	<table>
		<tr>
		<td colspan="2">
			<div style="float: right;margin-left: 698px;">
				<input type="button" id="QBSaveID" class="saveQBhoverbutton turbo-tan" value="Save" onclick="SaveQuickBook()" style="width:62px; height:32px; position: relative; left: 31px;">
				<input type="button" id="QBSaveCloseID" class="saveQBhoverbutton turbo-tan" value="Close" onclick="closeQuickBook()" style="width:70px; height:32px; position: relative; left: 32px;">
			<br/>
			</div>
			
		</td>
		</tr>
	</table>
	</div>
	<div>
	</div>
	<div id="QuickbookpopupAlertDiv">
		<table>
		        <tr>
		        	<td>
		        		<div>Customer is on Hold. You must take off Hold in order to proceed.</div>
		        	</td>
				</tr>					
		</table>
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jquery.vticker.js"></script>
	 <script type="'text/javascript">
	 $(window).load(function(){ $('#scroller').vTicker(); });
     </script>
     <script type="text/javascript" src="./../resources/web-plugins/jquery.ui.datetimepicker.min.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/home.js"></script> 
<!--  	  <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/home.min.js"></script>  -->
</html>	