<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Turbopro - Sales</title>
	<link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="./../resources/styles/turbo-css/sales_widget.css" type="text/css"/>
	<style>
	.ui-dialog-titlebar{
	background:-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55)) !important;
	color: #000000;
	}
	.mySpecialClass{
	background:-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55));
	color:black;
	}
	.ui-dialog .ui-dialog-titlebar{
	color:black !important;
	}
		.ui-state-highlight,
	.ui-widget-content .ui-state-highlight,
	.ui-widget-header .ui-state-highlight .myTitleClass .mySpecialClass{
		border: 1px solid #fcefa1;
		background: -moz-linear-gradient(-90deg, #F8F8AB, #CDBF55); background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55));
		color: #000000;
	}
	.ui-state-highlight a,
	.ui-widget-content .ui-state-highlight a,
	.ui-widget-header .ui-state-highlight a {
		color: #000000;
	}
	.salesDropDownTab{
	width: 825px;
	}

	</style>
</head>
<body>
	<div class="bodyDiv">
	 	<div>
			<jsp:include page="./headermenu.jsp"></jsp:include>
		</div>
	<table height="40px;"></table>
	
    <table class="salesDropDownTable" align="center"><tr><td align="center">
    <div id="columns" align="center" class="generalSearchColumns">
   <ul id="column1" class="column">
    <table>
    	<tr>
    	<td rowspan="2" style="vertical-align: top;">
    		 <li class="widgetView widget color-yellow">
                <div class="widget-head SalesWidgetheader">
                    <h3>Views</h3>
                </div>
                <div class="widget-content">
                   	<table id="viewsGrid" class="scroll"></table>
                   	<div id="viewsGridPager"></div>
                </div>
            	<div class="SalesWidgetFooter">
    				<table><tr><td>&nbsp;</td></tr></table>
				</div>
			</li>
		</td>
    	<td colspan="2">
        	<table align="center" class="salesDropDownTab">
        		<tr><td> 
				<div>
					<select class="selectSalesWidth" id="SalesRepComboList" onchange="changeSalesrep()" onkeypress="checkpermission()" onclick="checkpermission()">
					<option value = "-1">All</option>
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
						<input type="hidden" name="FilterrxCustomerID" id="FilterrxCustomerID"/>
						<input type = "button" value="Clear" style="background: #D5C964;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" id="clearCustomer" onClick="clearCustomer()"/>
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
			</tr>
		</table>
        
            <li class="widget color-green fullNameWiget" id="intro">
                <div class="widget-head">
                    <h3>Welcome ${sessionScope.user.fullName}</h3>
                </div>
                <div class="widget-content">
                    <p>Welcome ${sessionScope.user.fullName}. <br/> Here you can check Upcoming Bids, Pending Quotes and Awarded Contractors.</p>
                </div>
            </li>
        </td>
        </tr>
        <tr>
        <td width="500px">
            <li class="widget color-yellow">
                <div class="widget-head SalesWidgetheader">
                    <h3>Upcoming Bids</h3>
                    <span style="float: right; margin-right: 2em;"><input type = "button" value="Open" style="background: #D5C964;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:black !important; border: solid; border-width: 1px;" id="clearCustomer" onClick="callUpcomingDialog()"/></span>
                </div>
                <div class="widget-content">
                    <table id="UpcomingBidsGrid" class="scroll"></table>
                </div>
            	<div class="SalesWidgetFooter">
            	<table>
	            	<!-- <tr>
    	        		<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
        	    		<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions verticalBottom" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><font class="moreOptionsInSales"><a id="upcoming"><label id="salesmore"><u>More...</u></label></a></font></td>
            		</tr> -->
            		  <tr><td>&nbsp;</td></tr>
            		
            	</table>
            	</div>
            </li>
          
			<!-- <div id="tip1_down">
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
			</div> -->
			 </td>
           <td>
		<li class="widget color-yellow ">
       		<div class="widget-head SalesWidgetheader" id="PendingJobsHeader">
            	<h3>Pending Jobs</h3>
            	<span style="float: right; margin-right: 2em;"><input type = "button" value="Open" style="background: #D5C964;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:black !important; border: solid; border-width: 1px;" id="clearCustomer" onClick="OpenPendingBidsgridDialog()"/></span>
           	</div>
            <div class="widget-content ">
            	<table style="padding-left:0px" id="PendingQuoteGrid" class="scroll"></table>
            </div >
            <div class="SalesWidgetFooter" id="PendingJobsFooter">
           	<table>
            	<!-- <tr>
            		<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
		            <td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
        		    <td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" value="Quote Amount"><label class="label_font">Quote Amount</label></td>
                </tr> -->
                 <tr><td>&nbsp;</td></tr>
              </table>
              </div>
            </li>
		</td>
		</tr>
		<tr>
		<td></td>
		<td>
		<li class="widget color-yellow ">
       		<div class="widget-head SalesWidgetheader" id="quotedJobsHeader">
            	<h3>Quoted Jobs</h3>
            	<span style="float: right; margin-right: 2em;"><input type = "button" value="Open" style="background: #D5C964;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:black !important; border: solid; border-width: 1px;" id="clearCustomer" onClick="callQuotedJobDialog();"/></span>
           	</div>
            <div class="widget-content ">
            	<table style="padding-left:0px" id="quotedJobGrid" class="scroll"></table>
            </div >
            <div class="SalesWidgetFooter" id="quotedJobsFooter">
           	<table>
            	<!-- <tr>
            		<td class="verticalBottom"><input type="checkbox" class="QuotedOptions" onchange="addRemoveQuotedGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
		            <td class="verticalBottom"><input type="checkbox" class="QuotedOptions" onchange="addRemoveQuotedGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
        		    <td class="verticalBottom"><input type="checkbox" class="QuotedOptions" onchange="addRemoveQuotedGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="QuotedOptions" onchange="addRemoveQuotedGridColumns()" checked="checked" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="QuotedOptions" onchange="addRemoveQuotedGridColumns()" checked="checked" value="Quote Amount"><label class="label_font">Quote Amount</label></td>
                </tr> -->
                <tr><td>&nbsp;</td></tr>
              </table>
              </div>
            </li>
            </td>
            <td>
           <li class="widget color-yellow ">
                <div class="widget-head  SalesWidgetheader">
                    <h3>Awarded Contractors</h3>
                    <span style="float: right; margin-right: 2em;"><input type = "button" value="Open" style="background: #D5C964;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:black !important; border: solid; border-width: 1px;" id="clearCustomer" onClick="openAwardedContractorsDialog();"/></span>
                </div>
               	<div class="widget-content"> 
                    <table style="padding-left:0px" id="AwardedContractors" class="scroll"></table>
                </div>
                 <div class="SalesWidgetFooter">
                <table>
                	<!-- <tr>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Job #" checked="checked" disabled="disabled"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Job Name" checked="checked" disabled="disabled"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="General Contractor"><label class="label_font">General Contractor</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Engineer"><label class="label_font">Engineer</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Architect"><label class="label_font">Architect</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="AwardedOptions" onchange="addRemoveAwardedGridColumns()" value="Low Bidder"><label class="label_font">Low Bidder</label></td>
            		</tr> -->
            		 <tr><td>&nbsp;</td></tr>
            	</table>
            	</div>
           </li>
           </td>
           </tr>
       </table>
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
	<div id="displayQuotedJob" style="display: none;">

				<table id="quotedJobGridDialog"></table>
				<!-- <div id="quotedJobGridPager"></div> -->
				<div class="SalesWidgetFooter" style="height: 30px; width: 1115px; vertical-align: middle; " align="center">
					<table>
            	<tr>
            		<td class="verticalBottom"><input type="checkbox" class="quotedJobOptions" onchange="quotedGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
		            <td class="verticalBottom"><input type="checkbox" class="quotedJobOptions" onchange="quotedGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
        		    <td class="verticalBottom"><input type="checkbox" class="quotedJobOptions" onchange="quotedGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="quotedJobOptions" onchange="quotedGridColumns()" checked="checked" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
            		<td class="verticalBottom"><input type="checkbox" class="quotedJobOptions" onchange="quotedGridColumns()" checked="checked" value="Quote Amount"><label class="label_font">Quote Amount</label></td>
            		<td class="verticalBottom"><input type="image" src="./../resources/images/printerz.png" title="Print" onclick="printQuotedJobs()" style="background: #EEDEBC;margin-left: 7em;"></td>
                </tr>
              </table>
				</div>
	</div>
		<div id="displayUpcomingBids" style="display: none;">
 			<div class="widget-content">
				<table id="UpcomingBidsGridDialog" class="scroll"></table>
			</div>
			<div class="SalesWidgetFooter">
            	<table>
	            	<tr>
    	        		<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
        	    		<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions verticalBottom" onchange="addRemoveUpcomingGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
            			<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Assigned Salesman"> <label class="label_font">Assigned Salesman &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="All Customers"><label class="label_font">All Customers</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Architect"><label class="label_font">Architect</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="Engineer" id="engineer"><label class="label_font">Engineer</label></td>
						<td class="verticalBottom"><input type="checkbox" class="UpcomingOptions" onchange="addRemoveUpcomingGridColumns()" value="General Contractor"><label class="label_font">General Contractor</label></td>
            		</tr>
            	</table>
            </div>
	</div>
	<div id="displayPendingBidsDialog" style="display: none;">
 		<div class="widget-content">
			<table id="PendingQuoteGridDialog" class="scroll"></table>
		</div>
		<div class="SalesWidgetFooter" id="PendingJobsFooter">
           	<table>
           		<tr>
           			<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Date"> <label class="label_font">Date &nbsp;&nbsp;</label></td>
	            	<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
       		    	<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
           			<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" value="Assigned Customers"><label class="label_font">Assigned Customers</label></td>
           			<td class="verticalBottom"><input type="checkbox" class="PendingOptions" onchange="addRemovePendingGridColumns()" checked="checked" value="Quote Amount"><label class="label_font">Quote Amount</label></td>
               	</tr>
            </table>
       </div>
	</div>
	
	<div id="displayAwardedContractorsDialog" style="display: none;">
               	<div class="widget-content">
                    <table style="padding-left:0px" id="AwardedContractorsDialog" class="scroll"></table>
                </div>
                 <div class="SalesWidgetFooter">
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
          </div>
          
          <div id="addBidListDialog">
	<form action="" id="bidListFormId">
			<table>
				<tr>
			
									<td><label>Division:</label></td>
									<td>
											<select id="blDivisionID" name="divisionName" style="width: 150px;">
													<option value="0">All</option>														
											</select>
									</td>
								</tr>
								<tr>			
									<td><label>Date Range from:</label></td>
									<td><input type="text" id="bidListFromDate" name="bidListFromDate"  style="width: 90px;">
									</td>
									<td><label>To:</label></td>
									<td><input type="text" id="bidListToDate" name="bidListToDate" style="width: 90px;">
									</td>
								</tr>
								<tr>			
									<td><label>Include Engineer?</label></td>
									<td>
                            <input type="checkbox" value='1' id="blIncludeEngineer" name="blIncludeEngineer" onclick="changeIE()"><label class="labelfont" for="blIncludeEngineer" id="blIncludeEngineerlabel">(no)</label>
									</td>
								</tr>
								<tr>			
									<td><label>Include job bid details?</label></td>
									<td>
                            <input type="checkbox" value='1' id="blIncludeJobBid" name="blIncludeJobBid"><label class="labelfont" for="blIncludeJobBid" id="blIncludeJobBidlabel">(no)</label>
									</td>
								</tr>
								<tr>			
									<td><label>Group by Division?</label></td>
									<td>
                            <input type="checkbox" value='1' id="blGroupByDiv" name="blGroupByDiv"><label class="labelfont" for="blGroupByDiv" id="blGroupByDivlabel">(no)</label>
									</td>
								</tr>
							</table>
					
					</td>
				</tr>
				</table>
				<br>
				
			<table>
				<tr>
				
					<td align="center"><input type="button" id="viewBLButton" name="viewBLButtonName" value="View" class="savehoverbutton turbo-blue" onclick="generateBidListPdf()" style=" width:65px;"></td>
					<td align="center"><input type="button" id="cancelBLButton" name="cancelBLButtonName" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 65px;" onclick="cancelBidList()"></td>
					<td><span style="color:red;" id ="errorStatus"></span></td>
				</tr>
			</table>
	</form>
</div>
	
   <script type="text/javascript" src="./../resources/scripts/turbo_scripts/dynamic_sales_grids.js"></script>
 <script type="text/javascript" src="./../resources/scripts/Nettuts-widget-source/inettuts.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/dynamic_sales_grids.min.js"></script> -->
</body>
</html>
