<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=100" > -->
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Turbopro - Projects</title>
	 
	<link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="./../resources/styles/turbo-css/project_widget.css" type="text/css"/>
</head> 
<body>
	<div class="bodyDiv">
	<div>
		<jsp:include page="./headermenu.jsp"></jsp:include>
	</div>
	
	<table align="center" class="salesDropDownTable">
		<tr>
			<td align="left">
				<div>
					<select class="selectSalesWidth" id="SalesRepComboList" onchange="changeSalesrep()" onkeypress="checkpermission_project()" onclick="checkpermission_project()"></select> &nbsp; 
					<input type="button" value="Filter" class="filter turbo-blue"/>
				</div>
			</td>
			<td  align="right">
			<label>Customer:</label><input type="text" id = "customerName" style="width:180px;" placeholder="Minimum 3 characters required" />
			<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" onclick="customerRolodex()">
			<input type = "button" value="Clear" style="background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" id="clearCustomer" onClick="clearCustomer()"/>
			<input type="hidden" id = "customerId" />
			</td>
		</tr>
	</table>
	<table style="margin: 0 auto;">
				<tr valign="top">
				<td>
						<table id="viewsGrid"></table>
						<div id="viewsGridPager"></div>
				</td>
				<td><input type = "button" class="buttonscss" value="In Submittal" style="background: rgb(26, 183, 204);width: 100px;height: 30px;font-weight: bold;font-size: 14px;color: white !important;"/></td>
				<td><input type = "button" class="buttonscss" value="Pending Credit" style="background: rgb(26, 183, 204);width: 120px;height: 30px;font-weight: bold;font-size: 14px;color: white !important;" /></td>
				<td><input type = "button" class="buttonscss" value="Dormant"  style="background: rgb(26, 183, 204);width: 100px;height: 30px;font-weight: bold;font-size: 14px;color: white !important;" /></td>
				<td><input type = "button" class="buttonscss" value="Customer List"  style="background: rgb(26, 183, 204);width: 120px;height: 30px;font-weight: bold;font-size: 14px;color: white !important;" onclick="showCustomerList()"/></td>
				<td>
				<table style="width:250px !important;border: 3px solid rgb(127, 214, 255);border-radius: 14px;height: 175px;">
				<tr><td><label>Overall Accounts Receivable</label><label></label></td></tr>
				<tr><td><label>Current:  </label><label id="currentar"></label></td></tr>
				<tr><td><label>30 days:  </label><label id="thirty" ></label></td></tr>
				<tr><td><label>60 days:  </label><label id="sixty" ></label></td></tr>
				<tr><td><label>90 days:  </label><label id="ninty" ></label></td></tr>
				<tr><td><label>Total Due:  </label><label id="artotal"></label></td></tr>
				</table>
				<div id="loaderforproject" style="margin-top: -132px;margin-left: 75px;"><img alt="loaderforproject" src="./../resources/images/loading-25aug.gif"    style="width:80px"></div>
				</td>
				<td>
				<table style="width:250px !important;border: 3px solid rgb(127, 214, 255);border-radius: 14px;height: 175px;">
				<tr><td><label>Overall Profit Margin YTD: %</label><label id="marginvalue"></label></td></tr>
				<tr><td><label>Year-to-Date Sales:</label><label id="ytd"></label></td></tr>
				 <tr><td><label>Prior Year-to-Date Sales:</label><label id="priorytd"></label></td></tr>
				
				</table>
				</td>
				
				</tr>

			<tr><td></td><td colspan="6">
    	<table style="width:950px; margin: 0 auto;">
    			<tr>
					<td>
						<table id="openJobsGrid" class="openJobsGrids"></table>
						<div id="openJobsGridPager"></div>
					</td>
					<td>
						<table id="SalesPurchaseOrdersGrid"></table>
						<div id="SalesPurchaseOrdersGridPager"></div>
					</td>
		
				</tr>
				<tr style="height:15px;"></tr>
				<tr>
					<td>
						<table id="CustomerMarginGird"></table>
						<div id="CustomerMarginGirdPager"></div>
					</td>
					<td>
						<table id="CreditHoldGrid"></table>
						<div id="CreditHoldGridPager"></div>
					</td>
		
				</tr>
    
    	</table>
    	</td>
    	</tr>
    	</table>
    	<div id="openjobsforpopup" style="display:none;">
    		<table id="openJobsGridpop" ></table>
			<div id="openJobsGridPagerpop"></div>
    	</div>
    	<div id="openPOSODialog" style="display:none;cursor:pointer !important;">
    		<div id="pOsODialogID" >
    		<table id="poSoGrid" ></table>
			<div id="poSoGridPager"></div>
			</div>
    	</div>
    	<div id="CustomerMarginGirdpopup" style="display:none;cursor:pointer !important;">
    		<table id="CustomerMarginGirdpop" ></table>
						<div id="CustomerMarginGirdPagerpop"></div>
    	</div>
    <div class="projectfooterDiv"></div>
    <table id="footer">
			<tr>
				<td colspan="2">
					<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
				</td>
			</tr>
		</table>
	</div>
	
		<div id="CustomerList" style="display:none;">
						<table style="padding-left:0px" id="customersGrid" class="scroll"></table>
						<div id="customersGridpager" class="scroll" style="text-align:right;"></div>
		</div>	
		<div id="ContactsList" style="display:none;">
						<table style="padding-left:0px" id="contactsGrid" class="scroll"></table>
						<div id="customersGridpager" class="scroll" style="text-align:right;"></div>
						<input type="hidden" id="rxMasterId" />
		</div>	
	<div id="AccountReceivableDiv" style="display:none;">
						<table style="padding-left:0px" id="AccountReceivableGrid" class="scroll"></table>
						<div id="accountReceivablepager" class="scroll" style="text-align:right;"></div>
	</div>
	
	 <div id="ARDetailsDiv" style="display:none;">
						<table style="padding-left:0px" id="ARDetailsGrid" class="scroll"></table>
						<div id="ARDetailspager" class="scroll" style="text-align:right;"></div>
						<input type="hidden" id="ardetailscustomerid"  value="0">
	</div> 
	
	<div id="CommissionStatementDiv" style="display:none;">
						<table style="padding-left:0px" id="CommissionStatementGrid" class="scroll"></table>
						<div id="CommissionStatementPager" class="scroll" style="text-align:right;"></div>
	</div>
	
	<div style="display: none;">
		<table>
			<tr>
				<td>
					<input type="text" id="projectUserID" value= "${sessionScope.projectsUserId}">
				</td>
			</tr>
		</table>
	</div>
	<input type="hidden" id="userLogin_Id" value="${sessionScope.userDetails.userId}">
  <!--  <script type="text/javascript" src="./../resources/scripts/turbo_scripts/dynamic_projects_grids.js"></script> -->
  <script type="text/javascript" src="./../resources/scripts/Nettuts-widget-source/inettuts.js"></script>
     <script type="text/javascript" src="./../resources/scripts/turbo_scripts/dynamic_projects_grids.js"></script>
      
</body>
</html>