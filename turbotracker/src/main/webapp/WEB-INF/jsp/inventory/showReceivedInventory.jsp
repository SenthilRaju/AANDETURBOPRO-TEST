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
			<div style="width:800px; margin:0 auto;">
			<table style="width:979px;margin:0 auto;margin-left:-4px;">
				<tr>
					<td width="58%">
						<fieldset class= " ui-widget-content ui-corner-all">
							<legend><label class="fontclass"><b>Received From</b></label></legend>
							<label><b>${vendorName}</b></label><br>
							<label>${address1}</label><br>
							<label>${address2}</label><br>
							<label>${city}</label><br>
							<label>${state} ${zip}</label><br>
						</fieldset>					
					</td>
					<td width="50%">
						<table style="margin-left: 5px;">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
										<legend><label class="fontclass"><b>Our PO#</b></label></legend>
										<label>${PONumber}</label><br>
									</fieldset>
								</td>
							</tr>
							<tr>
									<td>
									<fieldset class= " ui-widget-content ui-corner-all">
										<legend><label class="fontclass"><b>Receive Date</b></label></legend>
										<input type="text" name="receivedate" id="receivedInventorydate" value="${receivedDate}" />
										<input type="hidden" name="veReceivedID" id="veReceivedID" value="${veReceiveID}" style="width:50px;"/>
									</fieldset>
								</td>
							</tr>
						</table>					
					</td>
				</tr>
			</table>
			</div>
			<div id="lineItemGridDiv" style="width:800px; margin:0 auto;">
				<table id="lineItemGrid"></table>
				<div id="lineItemPager"></div>
				<br>
				<table style="width:779px;margin:0 auto;margin-left:-4px;">
					<tr>
						<td colspan="2">	
						<input type="button" id="ReceiveAllSaveID" class="cancelhoverbutton turbo-tan" value="Receive All" onclick="saveReceiveAllInventory()" style="width:90px;position: relative;">						
							<input type="button" id="POSaveID" class="cancelhoverbutton turbo-tan" value="Save" onclick="saveReceivedInventory()" style="width:90px;position: relative;">
							<input type="button" id="POCloseID" class="cancelhoverbutton turbo-tan" value="Close" onclick="closeReceivedInventory()" style="width:90px;position: relative;">
							<span style = "float: right;">
							<input type="button" id="priorreceipts" class="cancelhoverbutton turbo-tan" value="Prior Receipts" onclick="priorReceivedInventory()" style="width:100px;" />
							<input type="button" id="showReceivedInventoryPrint" class="cancelhoverbutton turbo-tan" value="Print" onclick="generateReceivedInventoryXl();" style="width:90px;">
							</span>
						</td>
					</tr>
				</table>
				<fieldset class= "custom_fieldset" style="height: 50px;width: 710px; visibility: hidden;">
					<legend class="custom_legend"><label><b>Totals</b></label></legend>
					<table style="width: 735px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative; "><input type="text" style="width: 100px; text-align:right" id="subtotalLineId" name="subtotalLineName" disabled="disabled"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" name="freightLineName" disabled="disabled"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxLineId" name="taxLineName" disabled="disabled"></td>
							<td><label style="right: 0px;position: relative;">% &nbsp;</label></td><td><input type="text" id="lineID" name="lineName" style="width: 100px;text-align:right;" disabled="disabled"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalGeneralId" name="totalLineName" disabled="disabled"></td>
						</tr>
					</table>
				</fieldset>				
			</div>
			<table></table>
			<div id="priorpopup" style="width:100px;height:100px;">
			 <table id ="priortable" width ="100%">
			 
			 </table>
			</div>
			<div id="LoadingDialog" style ="display:none; width:933px; height:400px;z-index:5000; top:200px; left:200px;  position: absolute; background: rgba(255,255,255,0.4);">
			<span style="margin: 0 auto;margin-left:380px;"><img src='../resources/images/loaderforcupay.gif' style ="margin-top:180px;width:150px;"></span>		
		 </div>
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
<%-- 		<div><jsp:include page="../customer_invoice.jsp"></jsp:include></div> --%>		
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/showReceivedInventory.js"></script>	
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobwizardRelease.js"></script>	 -->
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/invoiceList.js"></script> -->		
	</body>
</html>