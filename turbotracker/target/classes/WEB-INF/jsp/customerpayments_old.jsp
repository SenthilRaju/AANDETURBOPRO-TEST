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
			
	
		/* .loading {
		  display: inline-block;
		  margin: 20em;
		  border-width: 50px;
		  border-radius: 50%;
		  -webkit-animation: spin 1s linear infinite;
		     -moz-animation: spin 1s linear infinite;
		       -o-animation: spin 1s linear infinite;
		          animation: spin 1s linear infinite;
		  } */
	
		.style-1 {
		  border-style: solid;
		  border-color: #444 transparent;
		  }
		
		.style-2 {
		  border-style: double;
		  border-color: #637C92 transparent;
		  }
		
		.style-3 {
		  border-style: double;
		  border-color: #444 #fff #fff;
		  }
			
     </style>
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	<div class="tabs_main" style="padding-left: 0px;width:990px;margin:0 auto; background-color: #FAFAFA;height: 750px;">
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
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 465px;height: 160px">
										<legend><label><b>Payment</b></label></legend>
											<table>
												<tr>
													<td align="right"><label>Customer: </label></td>
													<td><input type="text" value="${requestScope.rxMAsterDetails.name}" id="payCustomer" readonly="readonly" style="width: 320px;"/>
														<input type="text" value="${requestScope.rxCustomerId}" id="customerID" style="display:none;">
														<input type="text" value="${requestScope.recieptID}" id="recieptID" style="display:none;">
													</td>
												</tr>
												<tr>
													<td align="left"><label>Amount: </label></td>
													<td><input id="payAmountId" value='<fmt:formatNumber value="${requestScope.amount}" type="currency" currencyCode="USD"/>' readonly="readonly" name="payAmountName" type="text" style="width: 100px"><label style="padding-left: 78px;padding-right: 10px">Date</label><input id="paymentdateid" align="right" type="text" class="datepicker" value="${requestScope.Date}" disabled style="width: 89px"></td></tr>
												<tr><td align="left"><label>Type: </label></td><td><select id="paymentReciptTypeId" name="paymentReciptTypeName" style="width: 100px;" >
														<option value="0">--Select--</option>
														<option value="1">Cash</option>
														<option value="2">Check</option>
														<option value="3">CreditCard</option>
														<option value="4">Other</option>
														</select>
														<input type ="hidden" id="paidStatus" />
														<input type="text" value="${requestScope.recieptTypeId}" id="PaytypeId" style="display:none;"></td></tr>
												<tr><td align="left"><label>Check #: </label></td><td><input type="text" id="payCheckId" value="${requestScope.checkNo}" name="payCheckName" maxlength ="11" style="width: 100px"></td></tr>
												<tr><td align="left"><label>Memo: </label></td><td><input type="text" id="payMemoId" name="payMemoName" style="width: 320px" value="${requestScope.memo}"></td></tr>
											</table>
									</fieldset>
								</td>
								<td style="width: 410px;padding-left:28px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 410px;height: 160px">
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
								<td>
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
								
								<td style="padding-left: 461px;" align="left"><input type="button" id="payDetailsId" name="payDetailsNmae" onclick="EditDetails()" value="Details" class="savehoverbutton turbo-blue"></td>
								<td style="padding-left: 0px;" align="left"><input type="button" id="editInvoiceId" name="payDetailsNmae" value="Edit Invoice" class="savehoverbutton turbo-blue" onclick="editCuInvoice()"></td>
								 <td style="padding-left: 0px;" align="left"><input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "payInvoice()" value="Pay Invoice" class="savehoverbutton turbo-blue"></td>
								  <td style="padding-left: 0px;" align="left"><input type="button" id="saveInvoiceId" name="saveInvoiceName" onClick = "payToLedger()" value="Save & Close" class="savehoverbutton turbo-blue"></td>
								
							</tr>
						</table>
					</td>
				</tr>
				
			</table>
		</form>
	</div>
		  <div id="LoadingDialog" style ="display:none; width:980px; height:485px;z-index:5000; top:251px;  position: absolute; background: rgba(255,255,255,0.4);">
			<span style="margin: 0 auto;margin-left:380px;"><img src='../resources/images/loaderforcupay.gif' style ="margin-top:180px;width:150px;"></span>		
		 </div>
	
	<!-- <table style="width:100%; margin: 0 auto; margin-top:25px;">
	<tr>
				<td align="right">
				<input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "SaveAndClose()" style="width:140px;" value="Save & Close" class="savehoverbutton turbo-blue">
				</td>
				</tr>
	
	</table> -->
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
					<td><input type="text" id="DisountUsed" name="discountName" onKeyUp = "calculateEndBalance()" ></td>
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
					<td colspan="2" align="right">
					<span id="paymentError" style = "color:red;clear:both;float:left"></span>
					<input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "saveDetails()" value="OK" class="savehoverbutton turbo-blue">
				      <input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "cancelEdit()" value="cancel" class="savehoverbutton turbo-blue"></td>
				</tr>	
			</table>
		</form>
	</div>
	
	
	
	<script type="text/javascript">
		var opInvoiceNo ='';
		var opInvoiceID='';
		var opLinkageId='';
		var opAmount=0.0;
		var _global_periodID;
		var _global_yearID;
		var _globalpaidstatus = "";
	
		jQuery(document).ready(function(){
			var customerId = $("#customerID").val();
			$('#paymentReciptTypeId').val($('#PaytypeId').val());
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
			$('#payingRadio').click(payingList);
			$('#dueRadio').click(due);


			
			$( "#payAmountId" ).bind( "keypress keyup blur", function() {
				
				if($( "#payAmountId" )!="")
					{
					var payAmt = $(this).val().replace(/[^0-9\.]+/g,"");
					var invoiceAmt = $("#invoiceappliedId").val().replace(/[^0-9\.]+/g,"");
					var creditUse= $("#creditusingId").val().replace(/[^0-9\.]+/g,"");
					var discUse= $("#discountgivenId").val().replace(/[^0-9\.]+/g,"");
					
					$("#unappliedpaymentId").val(formatCurrency(Number(payAmt)+Number(creditUse)-Number(invoiceAmt)));
					$("#paymentId").val(formatCurrency(payAmt));
					}
				else
					{
					$("#unappliedpaymentId").val("0.00");	
					$("#paymentId").val("0.00");
					}
				
				});
	   	});


		
		function loadData(){
			var customerID = $('#customerID').val();
			$.ajax({
				url: "./custpaymentslistcontroller/getPreLoadData",
				type: "POST",
				data : "&customerID="+customerID+'&recieptID='+$('#recieptID').val(),
				success: function(data) {

					var invoiceAmt = data.invoiceAmount;
					var creditUsage = Math.abs(data.creditUsage);
					var invoiceApplied= data.invoiceApplied;
					var discountAmt = data.discountUsed;
					
					
					var remainingBal=0;
					remainingBal = Number(invoiceAmt) - Number(data.invoiceApplied);
					 
					$('#creditusingId').val(formatCurrency(creditUsage));
					$('#invoiceappliedId').val(formatCurrency(invoiceApplied));
					$('#discountgivenId').val(formatCurrency(data.discountUsed));


					var givenAmount = $('#paymentId').val().replace(/[^0-9\.]+/g,"");
					
					
					var unAppliedAmount1 = Number(Number(Number(givenAmount) + Number(creditUsage))); // Calculate Original Payment + Credit Used + DiscountUsed
					var unAppliedAmount = Number(unAppliedAmount1.toFixed(2)) - (Number(invoiceApplied)); 
					unAppliedAmount = Number(unAppliedAmount);
					
					
					$('#unappliedpaymentId').val(formatCurrency(Number(unAppliedAmount)));
					due();
					$('#dueRadio').attr('checked',true);
					
				}
			});
		}

		// cuInvoiceGrid
		function loadInvoicesGrid(customerId,Oper){
	   		var recieptID = $('#recieptID').val();
	   		$("#customerPaymets").html();
			$("#customerPaymets").jqGrid({
				url:'./custpaymentslistcontroller/cuInvoices',
				datatype: 'JSON',
				mtype: 'GET',
				colNames: ['Paid','Invoice #', 'Customer PO #', 'Invoice Date', 'Balance', 'Discount used', 'Payment Applied', 'cuLinkagedetailId','cuInvoiceID','amount','Remaining Balance','Date Applied','Date Applied'],
				colModel: [
					{name:'payBill', index:'payBill', align:'center', hidden:false, width:20, editable:true, edittype:'text', editoptions:{size:30}, formatter:billApplyingFromatter1, editrules:{required:false}},
					{name:'invoiceNumber', index:'invoiceNumber', align:'left', hidden:false, width:60, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'customerPoNumber', index:'customerPoNumber', align:'left', hidden:false, width:60, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'receiptDate', index:'receiptDate', align:'center', hidden:false, width:60, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
		           	{name:'invoiceBalance', index:'invoiceBalance', align:'right', width:60, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	{name:'preDiscountUsed', index:'preDiscountUsed', align:'right', width:60, editable:true, edittype:'text',editoptions:{size:30}, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	/* {name:'discountUsed', index:'DiscountUsed', align:'center', width:60, editable:true, edittype:'text',editoptions:{size:30}, editrules:{required:false},  formatter:customCurrencyFormatter}, */
		           	{name:'paymentApplied', index:'paymentApplied', align:'right', width:60, editable:true, edittype:'text', editoptions:{}, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	{name:'cuInvoiceID', index:'cuInvoiceID', align:'center', width:60, editable:false,hidden:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'cuLinkagedetailId', index:'cuLinkagedetailId', align:'center', width:60, editable:false,hidden:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		        	{name:'amount', index:'amount', align:'right', width:60, editable:false,hidden:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'remaining', index:'remaining', align:'right', width:60, editable:true,hidden:false, editrules:{required:false},  formatter:customCurrencyFormatter},
		           	{name:'datePaids', index:'', width:55, align:"right", editable: true,hidden:true },
		        	{name:'datePaid', index:'datePaid', width:55, align:"right", editable: true,hidden:true }
			    ],
				
				rowNum: 50,
				pgbuttons: true,	
				recordtext: '',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: true,
				pager: '#customerPaymetsPager',
				sortname: 'invoiceNumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Invoices',
				height:350,	width: 900, rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
				postData: {
					customerId: customerId,cuRecieptID:recieptID,Oper:Oper
				},
				loadComplete: function(data) {
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				},
				onSelectRow: function(id){
					var rowData = jQuery(this).getRowData(id); 
					var remainingData = rowData["remaining"].replace('$','');
						 if (remainingData.search("-") == -1){
							 console.log('yes');
							 }
							 else{
								 
								    jQuery(this).jqGrid('setCell', id, 'payBill', 'A');					 
								    opInvoiceNo = rowData["invoiceNumber"];
									opInvoiceID=rowData["cuInvoiceID"];
									opLinkageId=rowData["cuLinkagedetailId"];
									opAmount=rowData["remaining"].replace(/[^0-9\.]+/g, "");
									console.log(rowData["remaining"]+"   "+opAmount);
							 }
					
				},
				ondblClickRow: function(rowid) {

				var rowData = jQuery(this).getRowData(rowid); 
				var invB = rowData["invoiceBalance"].replace('$','');
				invB= invB.replace(",","");
				if(!jQuery('#payingRadio').is(":checked"))
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

	   	function payComplete(){
	   		var customerID = $('#customerID').val();
	   		var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var remaining =  parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'remaining').replace(/[^0-9\.]+/g,""));
	   		var unapplied = parseFloat($('#unappliedpaymentId').val().replace(/[^0-9\.]+/g,""));
	   		var cuInvoiceId = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuInvoiceID');
			var cuLinkageDetailID = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuLinkagedetailId');
			//var discount = parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'discountUsed').replace(/[^0-9\.]+/g,""));
			var discount = parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'preDiscountUsed').replace(/[^0-9\.]+/g,""));
			var invoiceBalance = parseFloat(cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceBalance').replace(/[^0-9\.]+/g,""));
			updateInvoiceCompletePayAjax(invoiceBalance, discount, cuInvoiceId, cuLinkageDetailID, customerID);
		   	}
	   	
	   	function updateInvoiceCompletePayAjax(applied, discount, cuInvoiceId, cuLinkageDetailID, customerID){
	   		$.ajax({
				url: "./custpaymentslistcontroller/updateCompletePayment",
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

		function updateInvoicePayAjax(applied, discount, cuInvoiceId, cuLinkageDetailID, customerID){
			var overPayUrl = '';

			if($('#payingRadio').is(":checked"))
			{
   	 		oper="paidradio";
			}
   	 		else
	   	 	{
   	 		oper="dueradio";
	   	 	}
		
			
			if(parseFloat(opAmount)>0)
				{
					overPayUrl = '&opAmount='+opAmount+'&opInvoiceNo='+opInvoiceNo+'&opInvoiceID='+opInvoiceID+'&opLinkageId='+opLinkageId;
				 }
			else{
					overPayUrl = '&opAmount='+0+'&opInvoiceNo='+0+'&opInvoiceID='+0+'&opLinkageId='+0;
				}
	   		$.ajax({
				url: "./custpaymentslistcontroller/updateInvoice",
				type: "POST",
				data: 'appliedAmtName='+applied+'&discountName='+discount+'&cuInvoiceId='+cuInvoiceId+'&cuLinkageDetailID='+cuLinkageDetailID+'&customerID='+customerID
				+'&recieptID='+ $('#recieptID').val()+overPayUrl+'&oper='+oper,
				success: function(data) { 
					$('#customerPaymets').trigger("reloadGrid");
					location.reload(); 
				   	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
	   	}




	   	//Edit Customer Invoice
		function editCuInvoice(){
	   		 var cuInvoiceIdgrid= $("#customerPaymets");
	   		 var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var acuInvoiceNumber = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceNumber');

			if(rowId==null || acuInvoiceNumber=='false'){
				selectRow();
			}
			else
			{
	   		$.ajax({
				url: "./custpaymentslistcontroller/jobDetails",
				type: "POST",
				data: "&acuInvoiceNumber="+acuInvoiceNumber,
				success: function(data) { 
					if(data[0]!=null && data[1]!=null)
					{
						var urijobname=encodeBigurl(data[1]);
	   				 document.location.href= "./jobflow?token=view&jobNumber="+data[0]+"&jobName="+urijobname+"&jobStatus=Booked"+"&joMasterID="+data[2]; 
				   	$("#ui-tabs-5").trigger("click"); 
					}
					
				   	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
			}
	   	}

	   	//Validation function 
		function selectRow(){
	   		msgOnAjax("Please select any row from the grid to see the Details.","RED",6000);
			return false;
	   	}
		
	   	//cancel in popup box
	   	function cancelEdit(){
	   		$('#EditDetailsDlg').dialog("close");
	   	}

       //Save and close button click
	   	function payToLedger(){

	   		$("#LoadingDialog").css({"display":"inline"});

	   		var unapplied = $('#unappliedpaymentId').val().replace('$','');
	   		unapplied= unapplied.replace(",","");

	   		//alert(unapplied);
	   	
	   	  var hash = $(location).attr('hash');
	   	  $("#paidStatus").val(hash.substring(1));	

	   	  var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
			$.ajax({
				url: "./checkAccountingCyclePeriods",
				data:{"datetoCheck":$("#paymentdateid").val(),"UserStatus":checkpermission},
				type: "POST",
				success: function(data) { 
					if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
					{

						periodid=data.cofiscalperiod.coFiscalPeriodId;
						yearid = data.cofiscalperiod.coFiscalYearId;

					   		$.ajax({
								url: "./custpaymentslistcontroller/payMultipleInvoice",
								data:{"recieptID":$("#recieptID").val(),
									"recieptAmt":$("#payAmountId").val().replace(/[$,]+/g,""),
									"memo":$("#payMemoId").val(),
									"receiptDate":$("#paymentdateid").val(),
									"reference":$("#payCheckId").val(),
									"cuReceiptTypeId":$("#paymentReciptTypeId option:selected").val(),
									"coFiscalPeriodId":periodid,
									"coFiscalYearId":yearid,
									"paidStatus": $("#paidStatus").val()},
								type: "POST",
								success: function(data) { 
									$('#customerPaymets').trigger("reloadGrid");
									$("#LoadingDialog").css({"display":"none"});
									createtpusage('Company-Customer-Payment','Save Payment','Info','Company-Customers-Payment,Save Payment,recieptID:'+$("#recieptID").val()+',recieptAmt:'+$("#payAmountId").val()+',memo:'+$("#payMemoId").val());
									document.location.href = "./customerpaymentlist";
								   	},
					   			error:function(data){
					   				console.log('error');
					   				}
					   			});
					}
					else
						{
						
						if(data.AuthStatus == "granted")
						{	
							$("#LoadingDialog").css({"display":"none"});
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
												buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
											}).dialog("open");
						}
						else
						{
							$("#LoadingDialog").css({"display":"none"});
							showDeniedPopup();
						}
						}
			  	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
		   
	   	}
	   	
	   	

	  //due checkbox enable
	   	function due(){
	   		var customerID = $('#customerID').val();
	   		var Oper = "due";
	   		$("#payDetailsId").removeAttr("disabled");
	   	/* 	$("#payInvoiceId").removeAttr("disabled");
			$("#editInvoiceId").removeAttr("disabled");
			 */
	   		
	   		$("#customerPaymets").jqGrid('GridUnload');
	   		loadInvoicesGrid(customerID, Oper);
	   		$("#customerPaymets").trigger("reloadGrid");

	   		var colPos = 12;
	        var myGrid = $('#customerPaymets');
	        myGrid.jqGrid('showCol', myGrid.getGridParam("colModel")[colPos].name);
	        myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos + 1].name);

	   		
	   	}


	   	//Paying checkbox enable
	   	function payingList(){
	   		var customerID = $('#customerID').val();
	   		var Oper = "paying";
	   		
	   		$("#payDetailsId").attr("disabled",true);
		/* 	$("#payInvoiceId").attr("disabled",true);
			$("#editInvoiceId").attr("disabled",true);
			 */
	   		
	   		$("#customerPaymets").jqGrid('GridUnload');
	   		loadInvoicesGrid(customerID,Oper);
	   		$("#customerPaymets").trigger("reloadGrid");


	   		var colPos = 12;
	        var myGrid = $('#customerPaymets');
	        myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos ].name);
	        myGrid.jqGrid('showCol', myGrid.getGridParam("colModel")[colPos + 1].name);
	 
	   		
	   	}

	   	// End Balance calculation in popup
	   	function calculateEndBalance() {
		   	
	   		var endBalance = Number($('#balance').text().replace(/[^0-9\.]+/g,""))-((Number($('#appliedAmt').val().replace(/[^0-9\.]+/g,""))+(Number($('#DisountUsed').val().replace(/[^0-9\.]+/g,"")))));
	   		if(isNaN(endBalance)){
	   			$('#EndingBal').text(formatCurrency(0));
	   		}else{
	   		$('#EndingBal').text(formatCurrency(endBalance.toFixed(2)));
	   		}
	   	}
	   	function SaveAndClose(){
	   		document.location.href = "./customerpaymentlist";
	   	}
	   	


	   //Image Loading 
	   	function billApplyingFromatter1(cellvalue, options, rowObject){
			var anApplyingAmount = rowObject.paymentApplied;
			var aBillAmount = rowObject.invoiceBalance;
			var balanceAmt = rowObject.invoiceBalance;
			var image = "";
			 if(isNaN(balanceAmt))
				{
					balanceAmt = balanceAmt.replace("$","");
					balanceAmt = balanceAmt.replace(",","");
				} 
				
			 if(isNaN(anApplyingAmount))
				{
				 anApplyingAmount = anApplyingAmount.replace("$","");
				 anApplyingAmount = anApplyingAmount.replace(",","");
				} 
				
			 if(isNaN(aBillAmount))
				{
				 aBillAmount = aBillAmount.replace("$","");
				 aBillAmount = aBillAmount.replace(",","");
				}  

			 console.log("--ttyy---->"+anApplyingAmount)
				
			/* if(!$('#payingRadio').is(":checked"))
				{  */
				
						 if(balanceAmt < 0.00 && (anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0.00 ))
						{
							image='<img alt="search" src="./../resources/Icons/cr_grey.png" style="cursor:pointer;" onclick="payDiscountFullAmount('+balanceAmt+','+anApplyingAmount+','+options.rowId+',this)">';
							cellvalue = image;
						}
						else if(balanceAmt < 0 && (balanceAmt === anApplyingAmount|| anApplyingAmount > balanceAmt))
						{
							image = '<img alt="search" src="./../resources/Icons/cr_red.png" style="cursor:pointer;" onclick="payDiscountFullAmount('+balanceAmt+','+anApplyingAmount+','+options.rowId+',this)">';
							cellvalue = image;
						}
						else if(balanceAmt > 0 && anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0 ) {				
			
							console.log("Entered");				
							image = '<img alt="search" src="./../resources/Icons/bill_empty.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+','+options.rowId+',this)">';
							cellvalue = image;
						} 
						else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){

							image = '<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+','+options.rowId+',this)">';
							cellvalue = image;
						}
						else if(anApplyingAmount === aBillAmount) {
							image = '<img alt="search" src="./../resources/Icons/bill_full.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+','+options.rowId+',this)">';
							cellvalue = image;
						}
						else if(anApplyingAmount > aBillAmount && 0 < aBillAmount){
							
							image='<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+','+options.rowId+',this)">';
							cellvalue = image;
						}
			/*  	}
			else
				{

				

				 if(balanceAmt < 0 && (anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0 ))
					{
						image='<img alt="search" src="./../resources/Icons/cr_grey.png" style="cursor:pointer;">';payFullAmount
						cellvalue = image;
					}
					else if(anApplyingAmount < 0)
					{
						image = '<img alt="search" src="./../resources/Icons/cr_red.png" style="cursor:pointer;">';
						cellvalue = image;
					}
					else if(balanceAmt > 0 && anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0 ) {				
		
						console.log("Entered");				
						image = '<img alt="search" src="./../resources/Icons/bill_empty.png" style="cursor:pointer;">';
						cellvalue = image;
					} 
					else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){
						
						image = '<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;">';
						cellvalue = image;
					}
					else if(anApplyingAmount === aBillAmount) {
						image = '<img alt="search" src="./../resources/Icons/bill_full.png" style="cursor:pointer;">';
						cellvalue = image;
					}
					else if(anApplyingAmount>aBillAmount){
						image='<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;">';
						cellvalue = image;
					}
				} */
			  
			return cellvalue;
		}


	  //EditInvoice button click	
	   	function EditDetails(){
		   	var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
			var acuInvoiceNumber = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceNumber');
			var aDiscountUsed = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'preDiscountUsed');
			var invoiceBalance = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceBalance');
			var remaining =  cuInvoiceIdgrid.jqGrid('getCell', rowId, 'remaining');
			var paymentApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied');
			var invoiceAmt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'amount');
			if(rowId===null ){
				selectRow();
			}else{
				if(remaining.replace(/[^0-9\.]+/g,"")==opAmount){
					opAmount=0;
					}
				
						$('#creditusingId').text("0");
						$('#orgAmt').text(formatCurrency(invoiceAmt));
						$('#DisountUsed').val(formatCurrency(aDiscountUsed));
						$('#appliedAmt').val(formatCurrency(paymentApplied));
						$('#balance').text(formatCurrency(invoiceBalance));
						$('#EndingBal').text(remaining);
						if(parseFloat(opAmount)>0){
							console.log(invoiceBalance +"  "+opAmount);
							$('#balance').empty();
							$('#balance').append(formatCurrency(parseFloat(invoiceBalance.replace(/[^0-9\.]+/g,""))-parseFloat(opAmount)));

							}
	   		jQuery("#EditDetailsDlg").dialog("open");
			}
			
			calculateEndBalance();
			return true;
	   	}
	   
	   	
		//Payinvoice button click	
		function payInvoice()
		{
		
		 	var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
	   		var payBill = jQuery("#customerPaymets").jqGrid('getCell', rowId, 'payBill');	
	  		var billAmt = jQuery("#customerPaymets").jqGrid('getCell', rowId, 'invoiceBalance');	
	  		var appAmt = jQuery("#customerPaymets").jqGrid('getCell', rowId, 'paymentApplied');
	  		var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

	  		
			    var src='';
	
				if(isNaN(billAmt))
				{
				billAmt= billAmt.replace("$","");
				billAmt= billAmt.replace(",","");
				}

			 	if(isNaN(appAmt))
				{
				 appAmt = appAmt.replace("$","");
				 appAmt = appAmt.replace(",","");
				}  


			 	if(rowId!=null)
				{
			 	$("#LoadingDialog").css({"display":"inline"});	
			 	if(payBill.match("bill_empty.png"))
				   {				  
				 
				 	var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
				 	
					
					if((unApAmt-billAmt)>0)
						{
						src="./../resources/Icons/bill_full.png";
						var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData.paymentApplied = billAmt;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 		

						var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData1.remaining = 0;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
						saveDetailsfromimageclick(billAmt,rowId);
						
						}

					else if(unApAmt == 0)
						{
						$("#LoadingDialog").css({"display":"none"});
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:red;">There is no Unapplied Payment.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
												buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
											}).dialog("open");

						}
					else 
						{
						src="./../resources/Icons/bill_full.png";
						console.log("LeoTesting----"+unApAmt+"---"+billAmt+"-----"+opAmount)
						var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData.paymentApplied = unApAmt;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 
	
						var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData1.remaining = (billAmt-unApAmt);
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
						saveDetailsfromimageclick(billAmt,rowId);
						}
					
				   }
				  else
					{
					src="./../resources/Icons/bill_empty.png";
					saveDetailsfromimageclick(0,rowId);

					var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData1.remaining = Number(rowData1.remaining.replace(/[$,]+/g,""))+Number(rowData1.paymentApplied.replace(/[$,]+/g,""))+Number(rowData1.preDiscountUsed.replace(/[$,]+/g,""));
					console.log("myTotal"+rowData1.remaining);
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 

					var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData.paymentApplied = 0;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData);
	
					var rowData2 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData2.preDiscountUsed = 0;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData2);
					}
				


		 	 var payBill = jQuery("#customerPaymets").jqGrid('setCell', rowId, 'payBill','<img alt="search" '+src+' style="cursor:pointer;" onclick="payFullAmount('+billAmt+','+appAmt+','+rowId+',this)">');	

		 	createtpusage('Company-Customer-Payment','Pay Invoice','Info','Company-Customers-Payment,Pay Invoice,payBill:'+payBill+',billAmt:'+billAmt+',appAmt:'+appAmt);

				}
			 	else
				 {
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:red;">Please select a invoice.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
											buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
										}).dialog("open");
				 }	
		 	 
			}	


		//Payment Applly
		function payFullAmount(billAmt,appAmt,rowId,img)
		{
			$("#LoadingDialog").css({"display":"inline"});
			editCustomerpayment='cp';
			if(jQuery('#payingRadio').is(":checked"))
			{
				_globalpaidstatus = "paid";
				$("#paidStatus").val("paid");
				var seperator = (window.location.href.indexOf("?")===-1)?"?":"&";
				window.location.hash = "paid";
			}
						  if(img.src.match("bill_empty.png"))
						   {				  
						 
						 	var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
						 	
							
							if((unApAmt-billAmt)>0)
								{
								img.src="./../resources/Icons/bill_full.png";
								var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
								rowData.paymentApplied = billAmt;
								$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 		

								var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
								rowData1.remaining = 0;
								$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
								saveDetailsfromimageclick(billAmt,rowId);
								
								}

							else if(unApAmt == 0)
								{
								$("#LoadingDialog").css({"display":"none"});
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:red;">There is no Unapplied Payment.</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
														buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
													}).dialog("open");

								}
							else 
								{
								img.src="./../resources/Icons/bill_full.png";
								console.log("LeoTesting----"+unApAmt+"---"+billAmt+"-----"+opAmount)
								var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
								rowData.paymentApplied = unApAmt;
								$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 
			
								var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
								rowData1.remaining = (billAmt-unApAmt);
								$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
								saveDetailsfromimageclick(billAmt,rowId);
								}
							
						   }
						  else
							{
							img.src="./../resources/Icons/bill_empty.png";
							saveDetailsfromimageclick(0,rowId);

							var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
							rowData1.remaining = Number(rowData1.remaining.replace(/[$,]+/g,""))+Number(rowData1.paymentApplied.replace(/[$,]+/g,""))+Number(rowData1.preDiscountUsed.replace(/[$,]+/g,""));
							console.log("myTotal"+rowData1.remaining);
							$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 

							var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
							rowData.paymentApplied = 0;
							$('#customerPaymets').jqGrid('setRowData', rowId, rowData);
			
							var rowData2 = $('#customerPaymets').jqGrid('getRowData', rowId);
							rowData2.preDiscountUsed = 0;
							$('#customerPaymets').jqGrid('setRowData', rowId, rowData2);
							}
						
		}


		//Credit payment apply
		function payDiscountFullAmount(billAmt,appAmt,rowId,img)
		{
			editCustomerpayment='cp';
			if(jQuery('#payingRadio').is(":checked"))
			{
				_globalpaidstatus = "paid";
				$("#paidStatus").val("paid");
				var seperator = (window.location.href.indexOf("?")===-1)?"?":"&";
				window.location.hash = "paid";
				
				 if(img.src.match("cr_grey.png"))
				  {
					 $("#LoadingDialog").css({"display":"inline"});
					
				    billAmt = billAmt;
				    img.src="./../resources/Icons/cr_red.png";
					var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

					if((unApAmt-billAmt)>0)
						{
						var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData.paymentApplied = billAmt;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 
						
						var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData1.remaining = 0;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
						}
					else
						{

						var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData.paymentApplied = unApAmt;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 

						var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData1.remaining = (billAmt-unApAmt);
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
						
						}

					
					saveDetailsfromimageclick(billAmt,rowId);

				  }
			 	 else
				  {
			 		var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
					console.log("entered")
					img.src="./../resources/Icons/cr_grey.png";
					
					
					if((unApAmt+billAmt)>0)
					{
					saveDetailsfromimageclick(0,rowId);
					$("#LoadingDialog").css({"display":"inline"});
					}
					else
					{
					showCreditAlert();
					}
					
					img.src="./../resources/Icons/cr_grey.png";

					var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData1.remaining = Number(rowData1.remaining.replace(/[$,]+/g,""))+Number(rowData1.paymentApplied.replace(/[$,]+/g,""));
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
					
					var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData.paymentApplied = 0;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData);

				  }

				
			}
			else
			{
				$("#LoadingDialog").css({"display":"inline"});

				
				 if(img.src.match("cr_grey.png"))
				  {
				    billAmt = billAmt;
				    img.src="./../resources/Icons/cr_red.png";
					var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

					if((unApAmt-billAmt)>0)
						{
						var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData.paymentApplied = billAmt;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 
						
						var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData1.remaining = 0;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
						}
					else
						{

						var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData.paymentApplied = unApAmt;
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 

						var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
						rowData1.remaining = (billAmt-unApAmt);
						$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
						
						}
					saveDetailsfromimageclick(billAmt,rowId);

				  }
			 	 else
				  {

					console.log("entered")
					img.src="./../resources/Icons/cr_grey.png";
					saveDetailsfromimageclick(0,rowId);
					img.src="./../resources/Icons/cr_grey.png";

					var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData1.remaining = Number(rowData1.remaining.replace(/[$,]+/g,""))+Number(rowData1.paymentApplied.replace(/[$,]+/g,""));
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
					
					var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData.paymentApplied = 0;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData);

				  }
			}
			
			
		}

		function showCreditAlert()
		{
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">You must unapply invoices first. since this credit is used.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
									buttons: [{text: "OK",click: function(){$(this).dialog("close");location.reload(); }}]
								}).dialog("open");
		}

		// Apply Payment from Popup click

		function saveDetails(){

	   		var overPayUrl = '';
	   		var customerID = $('#customerID').val();
	   		var orgAmt = parseFloat($('#orgAmt').text().replace(/[^0-9\.]+/g,""));
	   		var endAmt = parseFloat($('#EndingBal').text().replace(/[^0-9\.-]+/g,""));
	   		var Disc = parseFloat($('#DisountUsed').val().replace(/[^0-9\.]+/g,""));
	   		var appliedAmt = parseFloat($('#appliedAmt').val().replace(/[$,]+/g,""));
	   		var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
	   		var bAmt = parseFloat($("#balance").text().replace(/[$,]+/g,""));
	   		var amtApplied=0;
			var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
	   		var alreadypaidamt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied');
			var oper ="";
	   		
	   	 	if($('#payingRadio').is(":checked"))
				{
	   	 		oper="paidradio";
				}
	   	 	else
		   	 	{
	   	 		oper="dueradio";
		   	 	}

	   		if(bAmt<0 && appliedAmt>0)
	   			appliedAmt = appliedAmt*-1; //for negative amount

	   		if(Number(unApAmt)+Number(alreadypaidamt.replace(/[$,]+/g,"")) > "0")
	   		{

		   	if(appliedAmt!="0")
			 {
		   	if(endAmt>="0")
			   	{
				   		if(appliedAmt > Number(unApAmt)+Number(alreadypaidamt.replace(/[$,]+/g,"")))
					   		{
				   			amtApplied = Number(unApAmt)+Number(alreadypaidamt.replace(/[$,]+/g,""));
					   		}
				   		else
					   		{
				   			amtApplied = appliedAmt;
					   		}
				   		
				   		var Balance = orgAmt - appliedAmt;
				   		if(Disc){
				   		Balance = Balance- Disc;
				   		}
				   		$('#balance').text(Balance);
				   		$('#EndingBal').text(Balance);
				   
						var cuInvoiceId = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuInvoiceID');
						var cuLinkageDetailID = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuLinkagedetailId');
						var invBalance = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceBalance').replace(/[$,]+/g,"");
						
						if(parseFloat(opAmount)>0)
						{
							overPayUrl = '&opAmount='+opAmount+'&opInvoiceNo='+opInvoiceNo+'&opInvoiceID='+opInvoiceID+'&opLinkageId='+opLinkageId;
						 }
						else{
							overPayUrl = '&opAmount='+0+'&opInvoiceNo='+0+'&opInvoiceID='+0+'&opLinkageId='+0;
						}
				     //  alert('TotalURL: appliedAmtName='+appliedAmt+'&discountName='+Disc+'&cuInvoiceId='+cuInvoiceId+'&cuLinkageDetailID='+cuLinkageDetailID+'&customerID='+customerID
					//		+'&recieptID='+ $('#recieptID').val()+overPayUrl);
				   		$.ajax({
							url: "./custpaymentslistcontroller/updateInvoice",
							type: "POST",
							data: 'appliedAmtName='+amtApplied+'&discountName='+Disc+'&cuInvoiceId='+cuInvoiceId+'&cuLinkageDetailID='+cuLinkageDetailID+'&customerID='+customerID
							+'&recieptID='+ $('#recieptID').val()+'&amtApplied='+amtApplied+"&ogapAmt="+invBalance+"&statusCheck=1"+overPayUrl+"&oper="+oper,
							success: function(data) { 
								$('#EditDetailsDlg').dialog("close");
								
								location.reload();
								$("#customerPaymets").jqGrid('GridUnload');
						   		loadInvoicesGrid(customerID,Oper); 
						   		$("#customerPaymets").trigger("reloadGrid");

						   		if(jQuery('#payingRadio').is(":checked"))
								{
									_globalpaidstatus = "paid";
									$("#paidStatus").val("paid");
									var seperator = (window.location.href.indexOf("?")===-1)?"?":"&";
									window.location.hash = "paid";
								}

							   	},
				   			error:function(data){
				   				console.log('error');
				   				}
				   			});
	   		return true;
			   	}
		   	else
			   	{

		   		$("#paymentError").text("Payment is Unbalanced.");
		   		setTimeout(function(){
		   			$("#paymentError").text("");				
					},3000);
				return false;
			   	}
			 }
		   	else
			   	{
		   		$("#paymentError").text("Applied Amount is Required.");
		   		setTimeout(function(){
		   			$("#paymentError").text("");				
					},3000);
				return false;
			   	}
	   		}
	   		else
		   		{
	   			var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:red;">There is no Unapplied Payment.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
										buttons: [{text: "OK",click: function(){$(this).dialog("close");location.reload(); }}]
									}).dialog("open");
				 	
	   			
		   		}
	   	}



// Apply Payment from image click
// Developed by Leo on july 13

		
		function saveDetailsfromimageclick(billAmt,rowId1){
	   		var overPayUrl = '';
	   		var customerID = $('#customerID').val();
	   		var cuInvoiceIdgrid= $("#customerPaymets");
	   		var rowId = rowId1;
	   		var appliedAmt = 0;
	   		var amtApplied = 0;
	   		
			var cuInvoiceId = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuInvoiceID');
			var cuLinkageDetailID = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuLinkagedetailId');
			var ogapAmt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'invoiceBalance').replace(/[$,]+/g,"");
			var ogdisAmt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'preDiscountUsed').replace(/[$,]+/g,"");
			var ogpayApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
			var ogRemaining = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'remaining').replace(/[$,]+/g,"");
			var paymentID=$("#paymentId").val();
			
			var invAmt = 0;
			var unApAmt = 0;
			var discAmt = 0;
			var payAmt =0;
			var creditAmt = 0;
			var calcValue = 0;

			if($('#payingRadio').is(":checked"))
			{
   	 		oper="paidradio";
			}
   	 		else
	   	 	{
   	 		oper="dueradio";
	   	 	}
			
			
			if(parseFloat(opAmount)>0)
			{
				overPayUrl = '&opAmount='+opAmount+'&opInvoiceNo='+opInvoiceNo+'&opInvoiceID='+opInvoiceID+'&opLinkageId='+opLinkageId;
			}
			else
			{
				overPayUrl = '&opAmount='+0+'&opInvoiceNo='+0+'&opInvoiceID='+0+'&opLinkageId='+0;
			}


			if(billAmt>0) // billAmount only
			{
				//alert("0<"+billAmt);
				payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
				creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
				invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
				discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
				unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

				var calc_unApplied=0;

					if(Number(unApAmt)>Number(billAmt))
					{
						$("#invoiceappliedId").val(formatCurrency(Number(invAmt)+Number(billAmt))); // Add Invoice Amount  
						calc_unApplied = Number(payAmt)+Number(creditAmt); // Calculation of credit total
						$("#unappliedpaymentId").val(formatCurrency(Number(calc_unApplied) - parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,"")))); // total for unappliedPayment
					}
					else
					{
						$("#invoiceappliedId").val(formatCurrency(Number(invAmt)+Number(unApAmt)));
						$("#unappliedpaymentId").val(formatCurrency(0));
					}
				console.log($("#unappliedpaymentId").val());
				amtApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
				appliedAmt = amtApplied;

				
				
			}
			else if(billAmt<0) // Credit amt only
			{
				//alert("0>"+billAmt);
				creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
				$("#creditusingId").val(formatCurrency(Number(creditAmt)+Math.abs(billAmt)));

				payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
				creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
				discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
				invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
				unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

				$("#unappliedpaymentId").val(formatCurrency((Number(payAmt)+Number(creditAmt))- parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""))));
				
				amtApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
				appliedAmt = amtApplied;
			}
			else // clear bills
			{
				//alert("0 ="+billAmt);
				
				
				payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
				creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
				discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
				invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
				unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

				
				if(ogpayApplied>0)
				{
					//alert("Remain"+ogRemaining);
					
					if(ogRemaining!=0)
					{
						//alert("unapp"+unApAmt+"ogapAmount"+ogapAmt+"ogdisAmt"+ogdisAmt);
						//When remaining amount is there
						
						if(unApAmt<0)
							{
	
							$("#unappliedpaymentId").val(formatCurrency(Number(unApAmt)  + parseFloat(ogpayApplied)));	
							$("#invoiceappliedId").val(formatCurrency(Number(invAmt)-parseFloat(ogpayApplied)));
							}
						else
							{
							$("#unappliedpaymentId").val(formatCurrency(Number(unApAmt)  + parseFloat(ogpayApplied)));	
							$("#invoiceappliedId").val(formatCurrency(Number(invAmt)-parseFloat(ogpayApplied)));
							}
					
					}
					else
					{

				//	alert("unapp"+unApAmt+"ogapAmount"+ogapAmt+"ogdisAmt"+ogdisAmt+"ogpayApplied"+ogpayApplied);
							
					$("#unappliedpaymentId").val(formatCurrency(Number(unApAmt) + parseFloat(ogpayApplied)));	
					$("#invoiceappliedId").val(formatCurrency(Number(invAmt)-parseFloat(ogpayApplied)));
					}

					if(ogdisAmt!=0)
						$("#discountgivenId").val(formatCurrency(discAmt - ogdisAmt));
					
				 	amtApplied = Number(ogpayApplied)+Number(ogdisAmt);
					appliedAmt = 0;
					
				
				}
				else
				{
					$("#creditusingId").val(formatCurrency(Number(creditAmt) + Number(ogpayApplied)));
					$("#unappliedpaymentId").val(formatCurrency(Number(unApAmt) + Number(ogpayApplied)));
					
					if(ogdisAmt!=0)
						$("#discountgivenId").val(formatCurrency(discAmt - ogdisAmt));
					amtApplied = Number(ogpayApplied)+Number(ogdisAmt);
					appliedAmt = 0;

				}
			}

			//alert(ogdisAmt);


	   		$.ajax({
				url: "./custpaymentslistcontroller/updateInvoice",
				type: "POST",
				data: 'appliedAmtName='+appliedAmt+'&discountName='+ogdisAmt+'&cuInvoiceId='+cuInvoiceId+'&cuLinkageDetailID='+cuLinkageDetailID+'&customerID='+customerID
				+'&recieptID='+ $('#recieptID').val()+'&amtApplied='+amtApplied+"&ogapAmt="+ogapAmt+"&statusCheck=1"+overPayUrl+"&oper="+oper,
				success: function(data) { 
					cuInvoiceIdgrid.jqGrid('setCell', rowId1, 'cuLinkagedetailId', data);	
					editCustomerpaymentGlobalForm = JSON.stringify($('#customerPaymets').getRowData());
					$("#LoadingDialog").css({"display":"none"});
				   	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
	   		return true;
	   	}

		function getURLParameter(name) {
			  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
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