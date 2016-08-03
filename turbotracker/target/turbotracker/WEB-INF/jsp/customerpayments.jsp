<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - CustomerPayments</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
     <style type="text/css">
     	#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
     </style>
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA;height: 750px;">
			<ul>
				<li id="customerFormDetails"><a href="">Customer Payments</a></li>
			</ul>
		<br><br>
	<div class="bodyDiv" id="customerFormDetails">
			<form id="customerFormId"  method="post" >
			<table style="width: 900px;" align="center">
				<tr>
					<td>
					<table style="padding-bottom: 10px;">
							<tr>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 400px;height: 150px">
										<legend><label><b>Payment</b></label></legend>
											<table>
												<tr>
													<td align="right"><label>Customer: </label></td>
													<td><input type="text" value="${requestScope.rxMAsterDetails.name}" id="payCustomer" style="width: 300px;"/>
														<input type="text" value="${requestScope.rxCustomerId}" id="customerID" style="display:none;">
														<input type="text" value="${requestScope.recieptID}" id="recieptID" style="display:none;">
													</td>
												</tr>
												<tr>
													<td align="left"><label>Amount: </label></td>
													<td><input id="payAmountId" value='<fmt:formatNumber value="${requestScope.amount}" type="currency" currencyCode="USD"/>' name="payAmountName" type="text" style="width: 100px"><label style="padding-left: 85px;padding-right: 10px">Date</label><input id="paymentdateid" align="right" type="text" class="datepicker" value="${requestScope.Date}" style="width: 70px"></td></tr>
												<tr><td align="left"><label>Type: </label></td><td><select id="paymentReciptTypeId" name="paymentReciptTypeName" style="width: 100px;" >
														<option value="0">--Select--</option>
														<option value="1">Cash</option>
														<option value="2">Check</option>
														<option value="3">CreditCard</option>
														<option value="4">Other</option>
														</select>
														<input type="text" value="${requestScope.recieptTypeId}" id="PaytypeId" style="display:none;"></td></tr>
												<tr><td align="left"><label>Check #: </label></td><td><input type="text" id="payCheckId" value="${requestScope.checkNo}" name="payCheckName" style="width: 100px"></td></tr>
												<tr><td align="left"><label>Memo: </label></td><td><input type="text" id="payMemoId" name="payMemoName" style="width: 300px" value="${requestScope.memo}"></td></tr>
											</table>
									</fieldset>
								</td>
								<td style="width: 410px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 410px;height: 150px">
										<legend><label><b>Usage</b></label></legend>
										<table style="width: 400px;">
											<tr><td><label>Payment: </label></td><td><input type="text" id="paymentId" name="paymentName"  readonly="readonly" value='<fmt:formatNumber value="${requestScope.amount}" type="currency" currencyCode="USD"/>'></td></tr>
											<tr><td><label>Credit using: </label></td><td><input type="text" id="creditusingId" name="creditusingName" readonly="readonly"></td></tr>
											<tr><td><label>Invoice Applied: </label></td><td><input type="text" id="invoiceappliedId" name="invoiceappliedName" readonly="readonly"></td></tr>
											<tr><td><label>Discount Given: </label></td><td><input type="text" id="discountgivenId" name="discountgivenName" readonly="readonly"></td></tr>
											<tr><td><label>Unapplied Payment: </label></td><td><input type="text" id="unappliedpaymentId" name="unappliedpaymentName" readonly="readonly"></td></tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table id="customerPaymets"></table>
						 <div id="customerPaymetsPager"></div>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td style="padding-left: 0px;" align="left"><input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "pay()" value="Pay Invoice" class="savehoverbutton turbo-blue"></td>
								<td style="padding-left: 10px;" align="left"><input type="button" id="payDetailsId" name="payDetailsNmae" onclick="EditDetails()" value="Details" class="savehoverbutton turbo-blue"></td>
								<td style="padding-left: 10px;" align="left"><input type="button" id="payDetailsId" name="payDetailsNmae" value="Edit Invoice" class="savehoverbutton turbo-blue" onclick="editCuInvoice()"></td>
								 <td style="padding-left: 485px;">
									<fieldset style="width: 130px;height:35px" class="ui-widget-content ui-corner-all">
										<legend><label><b>Show Invoices</b></label></legend>
										<table>
											<tr>
												<td style="padding-bottom: 0px"><label><input type="radio" name="group1" id="dueRadio">Due</label></td>
												<td style="padding-bottom: 0px"><label><input type="radio" name="group1" id="payingRadio">Paying</label></td>
											</tr>
										</table>
									</fieldset>
								</td> 
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
	<div id="EditDetailsDlg">
		<form id="editDetailForm">
			<table>
				<tr>
					<td><label>Original Amount</label></td>
					<td><label id="orgAmt">$</label></td>
				</tr>
				<tr>
					<td><label>Balance</label></td>
					<td><label id="balance">$</label></td>
				</tr>
				<tr>
					<td><label>Discount</label></td>
					<td><input type="text" id="DisountUsed" name="discountName" ></td>
				</tr>
				<tr>
					<td><label>Applied Amount</label></td>
					<td><input type="text" id="appliedAmt" name="appliedAmtName" onKeyUp = "calculateEndBalance()"></td>
				</tr>
				<tr>
					<td><label>Ending Balance</label></td>
					<td><label id="EndingBal">$</label></td>
				</tr>
				<tr>
					<td>
					<input style="visibility:hidden;">
					</td>
				</tr>
				<tr>
					<td><input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "saveDetails()" value="OK" class="savehoverbutton turbo-blue"></td>
					<td><input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "cancelEdit()" value="cancel" class="savehoverbutton turbo-blue"></td>
				</tr>	
			</table>
		</form>
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			var customerId = $("#customerID").val();
			$('#paymentReciptTypeId').val($('#PaytypeId').val());
			loadInvoicesGrid(customerId,"due");
			calculateEndBalance();
			loadData();
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
			$("#search").css("display", "none");
			jQuery(function(){
				jQuery("#EditDetailsDlg").dialog({
					autoOpen:false,
					title:"Payment Details",
					width: "400",
					modal:true,
					close:function(){ return true;}	
				});
			});
			$('#dueRadio').click(due);
			$('#payingRadio').click(payingList);
	   	});

		function loadData(){
			var customerID = $('#customerID').val();
			$.ajax({
				url: "./custpaymentslistcontroller/getPreLoadData",
				type: "POST",
				data : "&customerID="+customerID+'&recieptID='+$('#recieptID').val(),
				success: function(data) {
					var creditUsage = Math.abs(data.creditUsage);
					var invoiceApplied= data.invoiceApplied;
					$('#creditusingId').val(formatCurrency(creditUsage));
					$('#invoiceappliedId').val(formatCurrency(invoiceApplied));
					$('#discountgivenId').val(formatCurrency(data.discountUsed));
					var givenAmount = $('#paymentId').val().replace(/[^0-9\.]+/g,"");
					var unAppliedAmount1 = Number(Number(givenAmount) + Number(creditUsage));
					/**
					13787.30 + 13787.3 - 13787
					*/
					var unAppliedAmount = Number(unAppliedAmount1 - invoiceApplied);
					unAppliedAmount = Number(unAppliedAmount);
					$('#unappliedpaymentId').val(formatCurrency(Math.abs(Number(unAppliedAmount))));
					$('#dueRadio').attr('checked',true);
				}
			});
		}
	   	function loadInvoicesGrid(customerId,Oper){
	   		var recieptID = $('#recieptID').val();
			$("#customerPaymets").jqGrid({
				url:'./custpaymentslistcontroller/cuInvoices',
				datatype: 'JSON',
				mtype: 'GET',
				pager: jQuery('#customerPaymetsPager'),
				colNames: ['Paid','Invoice #', 'Customer PO #', 'Invoice Date', 'Balance', 'Discount used', 'Payment Applied', 'cuLinkagedetailId','cuInvoiceID','amount','Remaining Balance'],
				colModel: [
					{name:'payBill', index:'payBill', align:'center', hidden:false, width:20, editable:true, edittype:'text', editoptions:{size:30}, formatter:billApplyingFromatter, editrules:{required:false}},
					{name:'invoiceNumber', index:'invoiceNumber', align:'center', hidden:false, width:60, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'customerPoNumber', index:'customerPoNumber', align:'center', hidden:false, width:60, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'receiptDate', index:'receiptDate', align:'center', hidden:false, width:60, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
		           	{name:'invoiceBalance', index:'invoiceBalance', align:'center', width:60, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	{name:'discountUsed', index:'DiscountUsed', align:'center', width:60, editable:true, edittype:'text',editoptions:{size:30}, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	{name:'paymentApplied', index:'paymentApplied', align:'center', width:60, editable:true, edittype:'text', editoptions:{}, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	{name:'cuInvoiceID', index:'cuInvoiceID', align:'center', width:60, editable:false,hidden:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'cuLinkagedetailId', index:'cuLinkagedetailId', align:'center', width:60, editable:false,hidden:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		        	{name:'amount', index:'amount', align:'center', width:60, editable:false,hidden:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'remaining', index:'remaining', align:'center', width:60, editable:true,hidden:false, editrules:{required:false},  formatter:customCurrencyFormatter}
			    ],
				rowNum: 100,pgbuttons: false,recordtext: '',rowList: [],pgtext: null,viewrecords: false,
				sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Invoices',
				height:350,	width: 900, altRows: true, altclass:'myAltRowClass',
				postData: {
					customerId: customerId,cuRecieptID:recieptID,Oper:Oper
				},
				loadComplete: function(data) {
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				},
				ondblClickRow: function(rowid) {
					EditDetails();
				},
				jsonReader : {
					root: "rows", page: "page", total: "total", records: "records",
					repeatitems: false, cell: "cell", id: "id", userdata: "userdata"
				},
			});
		}
	   	
	   	function pay(){
	   		var customerID = $('#customerID').val();
	   		var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var remaining =  parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'remaining').replace(/[^0-9\.]+/g,""));
	   		var unapplied = parseFloat($('#unappliedpaymentId').val().replace(/[^0-9\.]+/g,""));
	   		var cuInvoiceId = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuInvoiceID');
			var cuLinkageDetailID = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuLinkagedetailId');
			var discount = parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'discountUsed').replace(/[^0-9\.]+/g,""));
			var invoiceBalance = parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceBalance').replace(/[^0-9\.]+/g,""));
			if(remaining === 0){
				updateInvoicePayAjax(0, discount, cuInvoiceId, cuLinkageDetailID, customerID);
				return true;
			}
	   		if(unapplied != 0 && (unapplied>remaining)){
	   			updateInvoicePayAjax(invoiceBalance, discount, cuInvoiceId, cuLinkageDetailID, customerID);
	   			return true;
	   		} else {
	   			msgOnAjax("Paying partially", "red", 3000);
	   			var paymentApplied = parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[^0-9\.]+/g,""));
	   			paymentApplied = paymentApplied + unapplied;
	   			updateInvoicePayAjax(paymentApplied, discount, cuInvoiceId, cuLinkageDetailID, customerID);
	   			return true;
	   		}
	   	}
	   	function updateInvoicePayAjax(applied, discount, cuInvoiceId, cuLinkageDetailID, customerID){
	   		$.ajax({
				url: "./custpaymentslistcontroller/updateInvoice",
				type: "POST",
				data: 'appliedAmtName='+applied+'&discountName='+discount+'&cuInvoiceId='+cuInvoiceId+'&cuLinkageDetailID='+cuLinkageDetailID+'&customerID='+customerID
				+'&recieptID='+ $('#recieptID').val(),
				success: function(data) { 
					$('#customerPaymets').trigger("reloadGrid");
					location.reload(); 
				   	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
	   	}
	   	function billApplyingFromatter(cellvalue, options, rowObject){
			var aBillAmount = rowObject.paymentApplied;
			var remaining = rowObject.remaining;
			if(aBillAmount >= 0  ){
				var image = "";
				if( aBillAmount === null || aBillAmount===0 ) {
					image = '<img alt="search" src="./../resources/Icons/bill_empty.png">';
					cellvalue = image;
				} else if(aBillAmount > 0 && remaining >0){
					image = '<img alt="search" src="./../resources/Icons/bill_half.png">';
					cellvalue = image;
				} else if(remaining === 0) {
					image = '<img alt="search" src="./../resources/Icons/bill_full.png">';
					cellvalue = image;
				} else{
				image = '<img alt="search" src="./../resources/Icons/add-1.png">';
				cellvalue = image;
			}
			}else{
				image = '<img alt="search" src="./../resources/Icons/add-1.png">';
				cellvalue = image;
			}
			
			return cellvalue;
		}
	   	
		function editCuInvoice(){
	   		 var cuInvoiceIdgrid= $("#customerPaymets");
	   		 var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var acuInvoiceNumber = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceNumber');
			if(rowId===null && acuInvoiceNumber==='false'){
				selectRow();
			}
	   		$.ajax({
				url: "./custpaymentslistcontroller/jobDetails",
				type: "POST",
				data: "&acuInvoiceNumber="+acuInvoiceNumber,
				success: function(data) { 
	   				 document.location.href="./jobflow?token=view&jobNumber="+data[0]+"&jobName="+data[1]+"&jobStatus=Booked"; 
				   	$("#ui-tabs-5").trigger("click"); 
				   	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
	   	}
	   	
		function selectRow(){
	   		msgOnAjax("Please select any row from the grid to see the Details.","RED",6000);
			return false;
	   	}
		
	   	function EditDetails(){
		   	var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var acuInvoiceNumber = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceNumber');
			var aDiscountUsed = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'discountUsed');
			var invoiceBalance = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceBalance');
			var remaining =  cuInvoiceIdgrid.jqGrid('getCell', rowId, 'remaining');
			var paymentApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied');
			var invoiceAmt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'amount');
			if(rowId===null ){
				selectRow();
			}else{
						$('#creditusingId').text("0");
						$('#orgAmt').text(formatCurrency(invoiceAmt));
						$('#DisountUsed').val(formatCurrency(aDiscountUsed));
						$('#appliedAmt').val(formatCurrency(paymentApplied));
						$('#balance').text(formatCurrency(invoiceBalance));
						$('#EndingBal').text(remaining);
	   		jQuery("#EditDetailsDlg").dialog("open");
			}
			return true;
	   	}
	   	
	   	function cancelEdit(){
	   		$('#EditDetailsDlg').dialog("close");
	   	}
	   	
	   	function saveDetails(){
	   		var customerID = $('#customerID').val();
	   		var orgAmt = parseFloat($('#orgAmt').text().replace(/[^0-9\.]+/g,""));
	   		var Disc = parseFloat($('#DisountUsed').val().replace(/[^0-9\.]+/g,""));
	   		var appliedAmt = parseFloat($('#appliedAmt').val().replace(/[^0-9\.]+/g,""));
	   		/*var balanceInPayment = parseFloat($('#unappliedpaymentId').val().replace(/[^0-9\.]+/g,""));
	   		 if(appliedAmt>balanceInPayment) {
	   			msgOnAjax('Remaining balance in the payment is less than the applied amount ','red',5000);
	   			return false;
	   		}
	   		if(appliedAmt>orgAmt){	
	   			msgOnAjax('Applied amount should be less than original Amount.','red',3000);
	   			return false;
	   		} */
	   		var Balance = orgAmt - appliedAmt;
	   		if(Disc){
	   		Balance = Balance- Disc;
	   		}
	   		$('#balance').text(Balance);
	   		$('#EndingBal').text(Balance);
	   		var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var cuInvoiceId = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuInvoiceID');
			var cuLinkageDetailID = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuLinkagedetailId');
	   		$.ajax({
				url: "./custpaymentslistcontroller/updateInvoice",
				type: "POST",
				data: 'appliedAmtName='+appliedAmt+'&discountName='+Disc+'&cuInvoiceId='+cuInvoiceId+'&cuLinkageDetailID='+cuLinkageDetailID+'&customerID='+customerID
				+'&recieptID='+ $('#recieptID').val(),
				success: function(data) { 
					$('#EditDetailsDlg').dialog("close");
					location.reload(); 
				   	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
	   		return true;
	   	}
	   	function due(){
	   		var customerID = $('#customerID').val();
	   		var Oper = "due";
	   		$("#customerPaymets").jqGrid('GridUnload');
	   		loadInvoicesGrid(customerID, Oper);
	   		$("#customerPaymets").trigger("reloadGrid");
	   	}
	   	function payingList(){
	   		var customerID = $('#customerID').val();
	   		var Oper = "paying";
	   		$("#customerPaymets").jqGrid('GridUnload');
	   		loadInvoicesGrid(customerID,Oper);
	   		$("#customerPaymets").trigger("reloadGrid");
	   	}
	   	function calculateEndBalance() {
	   		var endBalance = parseFloat($('#balance').text().replace(/[^0-9\.]+/g,""))-((parseFloat($('#appliedAmt').val().replace(/[^0-9\.]+/g,""))+(parseFloat($('#DisountUsed').val().replace(/[^0-9\.]+/g,"")))));
	   		if(isNaN(endBalance)){
	   			$('#EndingBal').text(formatCurrency(0));
	   		}else{
	   		$('#EndingBal').text(formatCurrency(endBalance));
	   		}
	   	}
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