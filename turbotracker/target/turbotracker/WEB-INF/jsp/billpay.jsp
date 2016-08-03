<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Pay Bills</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
     <style type="text/css">
     	#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		.ui-widget-overlay {
		    background: none repeat scroll 50% 50% #2B2922;
		    opacity: 0.6;
		}
     </style>
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA;height: 750px;">
			<ul>
				<li id="billFormDetails"><a href="">Vendor Pay Bills</a></li>
			</ul>
		<br><br>
		<div class="bodyDiv" id="billFormDetails">
		<form id="billpayFormId" name="fname" method="post" >
			<table align="center" width="1060px">
				<tr>
					<td align="left">
						<table style="width: 1100px; padding-bottom: 30px;">
							<tr>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 250px; height: 84px;">
										<legend><b>Bank Account</b></legend>
										<select style="width: 250px;" id="bankAccount" onchange="changeBankAccount()">
											<c:forEach var="bankAccountsList" items="${requestScope.bankAccountsList}">
												<option value='${bankAccountsList.moAccountId}'>
													<c:out value="${bankAccountsList.description}" ></c:out>
												</option>
											</c:forEach>
										</select>
									</fieldset>
								</td>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 260px;">
										<legend><b>Prepared Transactions</b></legend>
										<table>
											<tr>
												<td><label>Deposits/Payroll:</label></td>
												<td><input type="text" id="depositsORPayrollId" style="width:80px;" disabled="disabled" value="0"></td>
											</tr>
											<tr>
												<td><label>Bills approved to Pay:</label></td>
												<td><input type="text" id="billsApprovedAmountId" style="width:80px;" disabled="disabled" value="0"></td>
											</tr>
										</table>
									</fieldset>
								</td>
								<td>
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 190px;">
										<legend><b>Balance</b></legend>
										<table>
											<tr><td><label>Current:</label></td><td><input type="text" style="width:100px;" disabled="disabled" id="current_balance"></td></tr>
											<tr><td><label>Resulting:</label></td><td><input type="text" style="width:100px;" disabled="disabled" id="resulting_ID"></td></tr>
										</table>
									</fieldset>
								</td>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="height: 84px;">
										<legend><b>Vendor Group</b></legend>
										<table>
											<tr><td><input type="text" style="width: 300px;" id="vendorBillPay_groupID" name="vendorBillPay_groupName" placeholder="Minimum 3 characters required"></td>
											<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td></tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="padding-left: 10px;">
						<table id="vendorBills" style="width:20px"></table>
					    <div id="vendorBillsPager"></div>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
	<div id="openAddPayDig">
		<form id="addPayFormID">
			<table>
				<tr>
					<td><label>Original Amount: </label></td>
					<td><label id="originalAmonut_ID"></label></td>
				</tr>
				<tr>
					<td><label>Balance: </label></td>
					<td><label id="balance_ID"></label></td>
				</tr>
				<tr>
					<td><label>Discount: </label></td>
					<td><input type="text" id="discount_ID" name="discount_Name" value="0.00" style="width: 40px;"><label> % &nbsp;</label><input type="text" id="discount1_ID" name="discount1_Name" value="0.00" style="width: 98px;"></td>
				</tr>
				<tr>
					<td><label>Paying Now: </label></td>
					<td><input type="text" id="Paying_ID" name="Paying_Name"></td>
				</tr>
			</table>
			<hr>
			<table align="right">
				<tr>
				 	<td>
		   				<input type="button" class="cancelhoverbutton turbo-tan" value="Save" onclick="saveAddPayDig()" style=" width:80px;color: white;">
						<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelAddPayDig()" style="width:80px;color: white;">
		   			</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		var moAccountsGlobal = [];
		jQuery(document).ready(function(){
			var manufacturer = "";
			moAccountsGlobal = loadBankAccountsDetails();
			$(".tabs_main").tabs({
				cache: true,
				ajaxOptions: {
					error: function(xhr, status, index, anchor) {
						$(anchor.hash).html("<div align='center' style='height: 300px;padding-top: 200px;'>"+
								"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
								"</label></div>");
					}
				},
				load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
				select: function (e, ui) {
					//window.location.hash = ui.tab.hash;
					var $panel = $(ui.panel);
					if ($panel.is(":empty")) {
						$panel.append("<div class='tab-loading' align='center' style='height: 300px;padding-top: 200px;background-color: #FAFAFA'>"+
								"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
					}
				}
			});
			loadVendorBillPay(manufacturer);
			setTimeout('loadBankAccount()', 200);
			$("#search").css("display", "none");
	   	});

		function loadBankAccountsDetails(){
			var moAccounts = []; //create a new array global
		    <c:forEach items="${requestScope.bankAccountsList}" var="moAccount">
			    moAccounts.push({moAccountId:"${moAccount.moAccountId}",
				    			description:"${moAccount.description}",
				    			additions:"${moAccount.additions}",
				    			openBalance:"${moAccount.openBalance}",
				    			subtractions:"${moAccount.subtractions}",
				    			undepositedReceipts:"${moAccount.undepositedReceipts}",
				    			unprintedPayables:"${moAccount.unprintedPayables}",
				    			unprintedPayroll:"${moAccount.unprintedPayroll}"});
		    </c:forEach>

		    return moAccounts;
		}

		function loadBankAccount(){
			var currentBalance = 0;
			var resultingBalance = 0;
			var accountID = $("#bankAccount").val();
			for(var index = 0; index < moAccountsGlobal.length; index++){
				if(accountID === moAccountsGlobal[index].moAccountId){
					currentBalance = parseFloat(moAccountsGlobal[index].openBalance) + parseFloat(moAccountsGlobal[index].additions) - parseFloat(moAccountsGlobal[index].subtractions);
					resultingBalance = currentBalance + parseFloat(moAccountsGlobal[index].undepositedReceipts) - parseFloat(moAccountsGlobal[index].unprintedPayables) - parseFloat(moAccountsGlobal[index].unprintedPayroll);
					$("#resulting_ID").val(formatCurrency(resultingBalance));
					$("#current_balance").val(formatCurrency(currentBalance));
				}
			}
			return false;
		}
		
		function changeBankAccount(){
			var currentBalance = 0;
			var resultingBalance = 0;
			var accountID = $("#bankAccount").val();
			for(var index = 0; index < moAccountsGlobal.length; index++){
				if(accountID === moAccountsGlobal[index].moAccountId){
					currentBalance = parseFloat(moAccountsGlobal[index].openBalance) + parseFloat(moAccountsGlobal[index].additions) - parseFloat(moAccountsGlobal[index].subtractions);
					resultingBalance = currentBalance + parseFloat(moAccountsGlobal[index].undepositedReceipts) - parseFloat(moAccountsGlobal[index].unprintedPayables) - parseFloat(moAccountsGlobal[index].unprintedPayroll);
					
					$("#resulting_ID").val(formatCurrency(resultingBalance));
					$("#current_balance").val(formatCurrency(currentBalance));
				}
			}
			return false;
		}
		
		/**
		function getBankAccounts(){
			$.ajax({
				async: false,
				type:"GET",
				data:{"action":"3"},
				url: "./channel",
				dataType: "json",
				success: function (data){
					jQuery('#stories').find('option').remove();
					jQuery.each(data, function (index,story) {
						// add items to List box
						jQuery("#stories").append("<option value='" + story.id + "' name = " + story.name + " class = 'storyOptions'>" + story.name + "</option>");
					});
					return true;
				},
				error: function(jqXHR, textStatus, errorThrown)
				{
					var errorText = $(jqXHR.responseText).find('u').html();
					errorDialog(errorText);
					return false;
				}
			});
		}
		*/

		function loadVendorBillPay(manufacturer){
			$("#vendorBills").jqGrid({
				url:'./vendorscontroller/vendorbills_data',
				datatype: 'JSON',
				mtype: 'GET',
				pager: jQuery('#vendorBillsPager'),
				colNames: ['Pay','RxMasterID', 'Vendor', 'Due Date', 'PO #', 'Invoice #', 'Bill Date', 'Balance', 'VeBillID', 'RxMasterID','Applying Amount'],
				colModel: [
					{name:'payBill', index:'payBill', align:'center', hidden:false, width:20, editable:true, edittype:'text', editoptions:{size:30}, formatter:billApplyingFromatter, editrules:{required:false}},
					{name:'rxMasterID', index:'rxMasterID', align:'center', hidden:true, width:20, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'vendor', index:'vendor', align:'left', hidden:false, width:120, editable:true, edittype:'text', editoptions:{size:30}, cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editrules:{required:false}},
					{name:'dueDate', index:'dueDate', align:'center', hidden:false, width:40, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
		           	{name:'poNumber', index:'poNumber', align:'center', width:40, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false}},
		           	{name:'invoiceNumber', index:'invoiceNumber', align:'center', width:40, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false}},
		           	{name:'billDate', index:'billDate', align:'center', width:40, editable:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'balance', index:'balance', align:'right', width:40, editable:true,hidden:false, editrules:{required:false}, formatter:customCurrencyFormatter },
		           	{name:'veBillID', index:'veBillID', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}},
		           	{name:'vendorRxMasterID', index:'vendorRxMasterID', align:'right', width:10, editable:true,hidden: true, editrules:{required:false}},
		           	{name:'applyingAmount', index:'applyingAmount', align:'right', width:50, editable:true,hidden: false, editrules:{required:false}, formatter:customCurrencyFormatter}
			    ],
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
				sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Pay Bills',
				height:420,	width: 1100, altRows: true, altclass:'myAltRowClass',
				postData: {
					manufacturerID: function() { return manufacturer; }
				},
				loadComplete: function(data) {
					loadVebillPaydetails();
			    },
			    ondblClickRow: function(rowid) {
			    	var rowData = jQuery(this).getRowData(rowid); 
					var balanceAmount = rowData["balance"].replace(/[^0-9\.]+/g,"");
					var balance = rowData["balance"].replace(/[^0-9\.]+/g,"");
					var vebillID = rowData["veBillID"];
					var applyingAmount = rowData["applyingAmount"];
					
					$("#originalAmonut_ID").empty();
					$("#originalAmonut_ID").text(formatCurrency(balanceAmount));
					$("#balance_ID").empty();
					$("#balance_ID").text(formatCurrency(balance));
					$.ajax({
						url: "./vendorscontroller/getBillPayDetails",
						type: "GET",
						data: {'vebilID' : vebillID},
						success: function(data) {
							if(data !== ''){
								$.each(data, function(index, value) { 
									var aDisCount = $("#discount1_ID").val().replace(/[^0-9\.]+/g,"");
									var aDisCount1 = $("#originalAmonut_ID").text().replace(/[^0-9\.]+/g,"");
									var aCalCulateNo = (aDisCount/aDisCount1)*100;
									$("#discount_ID").val("");
									$("#discount_ID").val(aCalCulateNo);
									$("#Paying_ID").val("");
									$("#Paying_ID").val(value.applyingAmount);
									$("#discount1_ID").val("");
									$("#discount1_ID").val(value.discountAmount);
								});
							}
						}
					});
			    	openAddPayDialog();
		    	},
				loadError : function (jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error:'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
				},
				jsonReader : {
					root: "rows", page: "page", total: "total", records: "records",
					repeatitems: false, cell: "cell", id: "id", userdata: "userdata"
				}
			});
		}

		function billApplyingFromatter(cellvalue, options, rowObject){
			var anApplyingAmount = rowObject.applyingAmount;
			var aBillAmount = rowObject.billAmount;
			var image = "";
			if(anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0) {
				image = '<img alt="search" src="./../resources/Icons/bill_empty.png">';
					//"../resources/Icons/bill_empty.png";
				cellvalue = image;
			} else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){
				image = '<img alt="search" src="./../resources/Icons/bill_half.png">';
				cellvalue = image;
			} else if(anApplyingAmount === aBillAmount) {
				image = '<img alt="search" src="./../resources/Icons/bill_full.png">';
				cellvalue = image;
			}else if(anApplyingAmount>aBillAmount){
				image='<img alt="search" src="./../resources/Icons/add-1.png">';
				cellvalue = image;
			}
			return cellvalue;
		}

		$(function() { var cache = {}; var lastXhr='';
			$( "#vendorBillPay_groupID" ).autocomplete({ minLength: 3,timeout :1000,
				open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	},
				select: function (event, ui) {
					var manufacturer = ui.item.manufactureID;
					$("#vendorBills").jqGrid('GridUnload');
					loadVendorBillPay(manufacturer);
					$('#vendorBills').jqGrid('setGridParam',{postData:{'manufacturerID' : manufacturer }});
					$("#vendorBills").trigger("reloadGrid"); 
				},
				source: function( request, response ) { var term = request.term;
					if ( term in cache ) { response( cache[ term ] ); 	return; 	}
					lastXhr = $.getJSON( "search/getVendorBillPayList", request, function( data, status, xhr ) { cache[ term ] = data; 
						if ( xhr === lastXhr ) { response( data ); 	} });
				},
				error: function (result) { $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); }
			}); 
		});

		function openAddPayDialog() {
			jQuery("#openAddPayDig").dialog("open");
			return true;
		}

		function saveAddPayDig(){
			var grid = $("#vendorBills");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');
			var existingApplyingAmount = grid.jqGrid('getCell', rowId, 'applyingAmount').replace(/[^0-9\.]+/g,"");
			var isAlreadyExist = false;
			if(existingApplyingAmount > 0){
				isAlreadyExist = true;
			} else {
				isAlreadyExist = false;
			}
			var aDisCount = $("#discount1_ID").val().replace(/[^0-9\.]+/g,"");
			var aPayingAmount = $("#Paying_ID").val().replace(/[^0-9\.]+/g,"");
			var aDetails = "veBillID="+aVeBillID+"&disCountPrice="+aDisCount+"&payingAmount="+aPayingAmount+"&isAlreadyExist="+isAlreadyExist;
			$.ajax({
				url: "./vendorscontroller/addBillPayDetails",
				type: "POST",
				data : aDetails,
				success: function(data) {
					jQuery("#openAddPayDig").dialog("close");
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Details Added Successfully.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
											$("#vendorBills").trigger("reloadGrid");  }}]}).dialog("open");
						return true;
				},
				error: function(jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error:'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
				}
			});
			document.getElementById("addPayFormID").reset();
		}
		
		function cancelAddPayDig(){
			jQuery("#openAddPayDig").dialog("close");
			return false;
		}

		jQuery(function(){
			jQuery("#openAddPayDig").dialog({
				autoOpen:false,
				title:"Payment Details",
				width: "350",
				modal:true,
				close:function(){ return true;}	
			});
		});
		var loadVebillPaydetails = function() {
			$.ajax({
				url: "./veInvoiceBillController/getvebillpaylist",
				type: "POST",
				success: function(data) {
					var applyingAmount = 0;
					for (var index = 0; index < data.length; index++) {
						applyingAmount = applyingAmount + Number(data[index].applyingAmount);
					}
					//alert(data[0].applyingAmount);
					$("#billsApprovedAmountId").val("-" + formatCurrency(applyingAmount));
					return true;
				},
				error: function(jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error:'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
				}
			});
		};
    </script>
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
</html>