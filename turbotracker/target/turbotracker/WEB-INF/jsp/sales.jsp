<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Turbopro - Sales</title>
	<link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="./../resources/styles/turbo-css/sales_widget.css" type="text/css"/>
</head>
<body>
	<div class="bodyDiv">
	 	<div>
			<jsp:include page="./headermenu.jsp"></jsp:include>
		</div>
	<table height="40px;"></table>
	<table align="center" class="salesDropDownTable"><tr><td> 
	<div>
		<select class="selectSalesWidth" id="SalesRepComboList" onchange="changeSalesrep()">
		<option value = "-1">all</option>
		</select> &nbsp; 
		<input type="button" value="Filter" class="filter turbo-blue"/>
	</div>
	</td>
	<td>
		<div class="customerSearchField">
			<label class="customerfont">Customer: </label>
			<!-- <input type="button" id="customerFilterButton" value="Customer Search" class="filter turbo-blue" onclick="customerFilterDialog()"/>  -->
			<input type="text" id="customerFilterListID" name="customerFilterListName" class="customerinputfield" placeholder="Minimum 3 characters required to get Customer List"/>
			<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
		</div>
	</td>
	<!-- 	<div id="customerDialogBox">
			<label>Customer: </label>&nbsp; 
			<input type="text" id="customerFilterListID" name="customerFilterListName" style="width: 300px;"/>&nbsp;
			<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
			<hr width="700px;">
			<table id="customerFilterSearchGrid"></table>
			<hr width="700px;">
			<table align="right">
				<tr>
				 	<td></td>
				 	<td align="right" style="padding-right:1px;">
						<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelUpComingCustomer()" style="width:80px;">  
					</td>
				</tr>
			</table>
		</div> 
	</div> -->
	</tr></table>
    <table class="salesDropDownTable" align="center"><tr><td align="center">
    <div id="columns" align="center" class="generalSearchColumns">
        <ul id="column1" class="column">
            <li class="widget color-green fullNameWiget" id="intro">
                <div class="widget-head">
                    <h3>Welcome ${sessionScope.user.fullName}</h3>
                </div>
                <div class="widget-content">
                    <p>Welcome ${sessionScope.user.fullName}. <br/> Here you can check Upcoming Bids, Pending Quotes and Awarded Contractors.</p>
                </div>
            </li>
            <li class="widget color-yellow gridWidths">
                <div class="widget-head SalesWidgetheader">
                    <h3>Upcoming Bids</h3>
                </div>
                <div class="widget-content gridWidths">
                    <table id="UpcomingBidsGrid" class="scroll"></table>
                </div>
            	<div id="SalesWidgetFooter">
            	<table>
	            	<tr>
    	        		<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
        	    		<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions verticalBottom" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><font class="moreOptionsInSales"><a id="upcoming"><label id="salesmore"><u>More...</u></label></a></font></td>
            		</tr>
            	</table>
            	</div>
            </li>
			<div id="tip1_down">
				<table>
					<tr>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Assigned Salesman"> <label class="label_font">Assigned Salesman &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="All Customers"><label class="label_font">All Customers</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Architect"><label class="label_font">Architect</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Engineer" id="engineer"><label class="label_font">Engineer</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="General Contractor"><label class="label_font">General Contractor</label></td>
					</tr>
				</table>
			</div>
		<li class="widget color-yellow gridWidths">
       		<div class="widget-head SalesWidgetheader">
            	<h3>Pending Quotes</h3>
           	</div>
            <div class="widget-content gridWidths">
            	<table style="padding-left:0px" id="PendingQuotesGrid2" class="scroll"></table>
            </div >
            <div id="SalesWidgetFooter">
           	<table>
            	<tr>
            		<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
		            <td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
        		    <td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" value="Quote Amount"><label class="label_font">Quote Amount</label></td>
                </tr>
              </table>
              </div>
            </li>
           <li class="widget color-yellow gridWidths">
                <div class="widget-head gridWidths SalesWidgetheader">
                    <h3>Awarded Contractors</h3>
                </div>
               	<div class="widget-content">
                    <table style="padding-left:0px" id="AwardedContractors" class="scroll"></table>
                </div>
                 <div id="SalesWidgetFooter">
                <table>
                	<tr>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Job #" checked="checked" disabled="disabled"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Job Name" checked="checked" disabled="disabled"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="General Contractor"><label class="label_font">General Contractor</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Engineer"><label class="label_font">Engineer</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Architect"><label class="label_font">Architect</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Low Bidder"><label class="label_font">Low Bidder</label></td>
            		</tr>
            	</table>
            	</div>
           </li>
       </ul>
    </div>
    </td>
    </tr>
    </table>
    	<div class="salesfooterDiv"></div>
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
					<input type="text" id="salesRepUserID" value= "${sessionScope.salesUserId}">
				</td>
			</tr>
		</table>
	</div>
 <!--   <script type="text/javascript" src="./../resources/scripts/turbo_scripts/dynamic_sales_grids.js"></script> -->
 <script type="text/javascript" src="./../resources/scripts/Nettuts-widget-source/inettuts.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/dynamic_sales_grids.min.js"></script>
</body>
</html>