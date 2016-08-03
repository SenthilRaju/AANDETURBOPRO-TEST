<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro - Jobs</title>
		<link rel="SHORTCUT ICON" href="./../resources/Icons/TurboRepIcon.png">
		<style type="text/css">
			#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
			#mainMenuJobsPage a span{color:#FFF}
		</style> 
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<div style="width:1090px; margin:0 auto;">
			
			<table width = "105%">
			<tr>
			<td align="left">
				<span style="vertical-align:5px;">&nbsp;Received Date <input type="checkbox" name="dateRange" id="dateRange" onclick="callEnableRecieveDate();"/> 
				&nbsp;Range From:&nbsp;<input type="text" name="fromDate" id="fromDate" class="datepicker"/>&nbsp;To:&nbsp;
				<input type="text" name="toDate" id="toDate" class="datepicker"/>
				</span></td>
					<td align="right">
						<div style="float: right">
							<input type="button" value="New" class="savehoverbutton turbo-blue" onclick="locateReceiveInventory()" style="width:100px;margin-left: 10px;" />
						</div>
					</td>
				</tr>
			</table>
			
			</div>
			<input type="hidden" value="${vendorID}" id="vendorid" />
			<input type="hidden" value="${sortBy}" id="sortbydate" />
			<input type="hidden" value="${rangeFrom}" id="rangefromdate" />
			<div id="lineItemGridDiv" style="width:800px; position: relative;left: 10%;">
			<table id="lineItemGrid"></table>
			<div id="lineItemPager"></div>
			
			</div>
			<table>
		
	</table>
			<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<div>
<%-- 		<jsp:include page="../customer_invoice.jsp"></jsp:include></div> --%>
		
	
		
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/showReceivedInventoryList.js"></script>	
		<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobwizardRelease.js"></script> -->	
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/invoiceList.js"></script> -->
		
	</body>
</html>