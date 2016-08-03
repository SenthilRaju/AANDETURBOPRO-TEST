<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
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
	<table height="40px;"></table>
	<table align="center" class="salesDropDownTable">
		<tr>
			<td align="left">
				<div>
					<select class="selectSalesWidth" id="SalesRepComboList" onchange="changeSalesrep()"></select> &nbsp; 
					<input type="button" value="Filter" class="filter turbo-blue"/>
				</div>
			</td>
		</tr>
	</table>
	<!-- <div id="head"><h1>Projects</h1></div> -->
    <table class="salesDropDownTable" align="center"><tr><td align="center">
    <div id="columns" align="center">
        <ul id="column1" class="column">
            <li class="widget color-green" id="intro">
                <div class="widget-head ProjectWidgetHeader">
                    <h3>Welcome ${sessionScope.user.fullName}</h3>
                </div>
                <div class="widget-content">
                    <p>Welcome ${sessionScope.user.fullName}. <br/> The Here you can check Booked In Submittal , Pending Order,Pending Credit , Dorment Projects and Order Tracking.</p>
                </div>
            </li>
            <li class="widget color-blue">
                <div class="widget-head ProjectWidgetHeader">
                    <h3>Booked In Submittal</h3>
                </div>
                <div class="widget-content">
                    <table style="padding-left:0px" id="Booked" class="scroll"></table>
                </div>
                <div class="ProjectWidgetFooter">
                 <table>
	            	<tr>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" checked="checked" disabled="disabled" value="Date"><label class="label_font">Booked Date &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" value="SalesMan"><label class="label_font">Salesman&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" value="Customer"><label class="label_font">Customer&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" value="Customer Contact"><label class="label_font">Customer Contact&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" value="Engineer"><label class="label_font">Engineer&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="BookedInSubmittal" onchange="addRemoveBookedInSubmittalGridColumns()" value="Contract Amount"><label class="label_font">Contract Amount&nbsp;&nbsp;</label></td>
					</tr>
				</table> 
            </div>
            </li>
            <li class="widget color-blue">
                <div class="widget-head ProjectWidgetHeader">
                    <h3>Booked Pending Credit</h3>
                </div>
                <div class="widget-content">
                    <table style="padding-left:0px" id="PendingCreditGrid" class="scroll"></table>
                </div>
                <div class="ProjectWidgetFooter">
                <table>
	            	<tr>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" checked="checked" disabled="disabled" value="Date"><label class="label_font">Booked Date &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" value="SalesMan"><label class="label_font">Salesman&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" value="Customer"><label class="label_font">Customer&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" value="Customer Contact"><label class="label_font">Customer Contact&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" value="Engineer"><label class="label_font">Engineer&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingCreditOptions" onchange="addRemovePendingCreditGridColumns()" value="Contract Amount"><label class="label_font">Contract Amount&nbsp;&nbsp;</label></td>
					</tr>
				</table>
				</div> 
            </li>
            <li class="widget color-blue">
                <div class="widget-head ProjectWidgetHeader">
                    <h3>Booked Pending Order</h3>
                </div>
                <div class="widget-content">
                    <table style="padding-right:0px" id="pendingOrderGrid" class="scroll"></table>
                </div>
                <div class="ProjectWidgetFooter">
                <table>
	            	<tr>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" checked="checked" disabled="disabled" value="Date"><label class="label_font">Booked Date &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" value="SalesMan"><label class="label_font">Salesman&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" value="Customer"><label class="label_font">Customer&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" value="Customer Contact"><label class="label_font">Customer Contact&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" value="Engineer"><label class="label_font">Engineer&nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="PendingOrderOptions" onchange="addRemovePendingOrderGridColumns()" value="Contract Amount"><label class="label_font">Contract Amount&nbsp;&nbsp;</label></td>
					</tr>
				</table> 
            </div>
            </li>
              
            
                 <li class="widget color-blue">
                <div class="widget-head ProjectWidgetHeader">
                    <h3>Dormant Projects</h3>
                </div>
                <div class="widget-content">
                    <table style="padding-right:0px" id="dormantProjectsGrid" class="scroll"></table>
                </div>
                <div class="ProjectWidgetFooter">
                <table>
	            	<tr>
						<td class="verticalBottom"><input type="checkbox" class="DormantProjects" onchange="addRemoveDormantProjectsGridColumns()" checked="checked" disabled="disabled" value="Job #"> <label class="label_font">Job # &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="DormantProjects" onchange="addRemoveDormantProjectsGridColumns()" checked="checked" disabled="disabled" value="Job Name"> <label class="label_font">Job Name &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="DormantProjects" onchange="addRemoveDormantProjectsGridColumns()" checked="checked" disabled="disabled" value="Customer"><label class="label_font">Customer</label></td>
						<td class="verticalBottom"><input type="checkbox" class="DormantProjects" onchange="addRemoveDormantProjectsGridColumns()" value="Customer Contact"><label class="label_font">Customer Contact</label></td>
						<td class="verticalBottom"><input type="checkbox" class="DormantProjects" onchange="addRemoveDormantProjectsGridColumns()" value="Salesman"><label class="label_font">Salesman</label></td>
						<td class="verticalBottom"><input type="checkbox" class="DormantProjects" onchange="addRemoveDormantProjectsGridColumns()" value="Last Activity Date"><label class="label_font">Last Activity Date</label></td>
					</tr>
				</table>
				</div>
            </li>
              <li class="widget color-blue">
                <div class="widget-head ProjectWidgetHeader">  
                    <h3>Order Tracking</h3>
                </div>
                <div class="widget-content">
                    <table style="padding-center:0px" id="orderTrackingGrid" class="scroll"></table>	
                </div>
                <div class="ProjectWidgetFooter">
                <table>
	            	<tr>
						<td class="verticalBottom"><input type="checkbox" class="orderTracking" onchange="addRemoveOrderTrackingGridColumns()" checked="checked" disabled="disabled" value="Purchase Order #"> <label class="label_font">Purchase Order # &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="orderTracking" onchange="addRemoveOrderTrackingGridColumns()" checked="checked" disabled="disabled" value="Order Date"> <label class="label_font">Order Date &nbsp;&nbsp;</label></td>
						<td class="verticalBottom"><input type="checkbox" class="orderTracking" onchange="addRemoveOrderTrackingGridColumns()" checked="checked" disabled="disabled" value="Vendor"><label class="label_font">Vendor</label></td>
						<td class="verticalBottom"><input type="checkbox" class="orderTracking" onchange="addRemoveOrderTrackingGridColumns()" value="Vendor Order #"><label class="label_font">Vendor Order #</label></td>
						<td class="verticalBottom"><input type="checkbox" class="orderTracking" onchange="addRemoveOrderTrackingGridColumns()" value="Ship Date"><label class="label_font">Ship Date</label></td>
					</tr>
				</table>
				</div>
			</li>
		   </ul>
    	  </div>
    	</td>
      </tr>
    </table>
    <div class="projectfooterDiv"></div>
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
					<input type="text" id="projectUserID" value= "${sessionScope.projectsUserId}">
				</td>
			</tr>
		</table>
	</div>
  <!--  <script type="text/javascript" src="./../resources/scripts/turbo_scripts/dynamic_projects_grids.js"></script> -->
  <script type="text/javascript" src="./../resources/scripts/Nettuts-widget-source/inettuts.js"></script>
     <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/dynamic_projects_grids.min.js"></script> 
</body>
</html>