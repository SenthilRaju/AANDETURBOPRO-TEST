<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro - Jobs</title>
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
			<table style="width:979px;margin:0 auto;">
				<tr>
				<td><input type="checkbox" name="dateRange" id="dateRange" onclick="callEnableDate();"/>
				<span style="vertical-align:5px;">Date Range: <input type="text" name="fromDate" id="fromDate" class="datepicker"/>&nbsp;Thru<input type="text" name="toDate" id="toDate" class="datepicker"/>
				</span></td>
					<td align="right">
						<div>
							<input  type="button"  class="add" value="   Add" alt="Add new Job" onclick="addNewPO()"/>&nbsp;
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table id="PurchaseOrdersGrid"></table>
						<div id="PurchaseOrdersGridPager"></div>
					</td>
				</tr>
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
		
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/po_list.js"></script>
	</body>
</html>