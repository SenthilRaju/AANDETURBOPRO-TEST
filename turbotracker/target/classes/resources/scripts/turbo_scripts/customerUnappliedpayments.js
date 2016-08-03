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
			 
			$('#creditusingId').val(formatCurrency(0));
			$('#invoiceappliedId').val(formatCurrency(0));
			$('#discountgivenId').val(formatCurrency(0));
			
			var givenAmount = $('#paymentId').val().replace(/[^0-9\.]+/g,"");
			$('#unappliedpaymentId').val(formatCurrency(Number(givenAmount)));
			due();
			$('#dueRadio').attr('checked',true);
			
		}
	});
}
	
//Load InvoiceGrid
function loadInvoicesGrid(customerId,Oper){
   		var recieptID = $('#recieptID').val();
   		$("#customerPaymets").html();
		$("#customerPaymets").jqGrid({
			url:'./custpaymentslistcontroller/cuInvoices',
			datatype: 'JSON',
			mtype: 'GET',
			colNames: ['Paid','Invoice #', 'Customer PO #', 'Invoice Date', 'Balance', 'Discount used', 'Payment Applied', 'cuLinkagedetailId','cuInvoiceID','amount','Remaining Balance','Date Applied','Date Applied',''],
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
	        	{name:'datePaid', index:'datePaid', width:55, align:"right", editable: true,hidden:true },
	           	{name:'payBillChkbox', index:'payBillChkbox', align:'center',  width:20, hidden:true, editable:false, formatter:payBillCheckbox,   editrules:{edithidden:true}}
		    ],
			
			rowNum: 5000,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			//multiselect:true,
			//pager: '#customerPaymetsPager',
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

			},
			jsonReader : {
				root: "rows", page: "page", total: "total", records: "records",
				repeatitems: false, cell: "cell", id: "id", userdata: "userdata"
			},
		});
}
//--

//Create checkbox for internally 
function payBillCheckbox(cellValue, options, rowObject){
	var id="payBillChk_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"' value='false'>";
	return element;
}

function due(){
		var customerID = $('#customerID').val();
		var Oper = "due";
		$("#payDetailsId").removeAttr("disabled");
		
		$("#customerPaymets").jqGrid('GridUnload');
		loadInvoicesGrid(customerID, Oper);
		$("#customerPaymets").trigger("reloadGrid");
		$('#payingRadio').attr("disabled",true);
	
	}

function editCuInvoice(){
	var customerID = $('#customerID').val();
		 var cuInvoiceIdgrid= $("#customerPaymets");
		 var rowId = cuInvoiceIdgrid.jqGrid('getGridParam', 'selrow');
	var cuInvoiceID = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'cuInvoiceID');

	if(rowId==null){
		selectRow();
	}
	else
	{
		window.location.href = './createinvoice?oper=create&frompage=customerpaymentpage&invoiceID='+cuInvoiceID+"&rxMasterId="+customerID;
	}
}

//Validation function 
function selectRow(){
		msgOnAjax("Please select any row from the grid to see the Details.","RED",6000);
	return false;
}
//--


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
				image = '<img alt="search" src="./../resources/Icons/bill_empty.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',0,'+options.rowId+',this)">';
				cellvalue = image;
			} 
			else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){
	
				image = '<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',0,'+options.rowId+',this)">';
				cellvalue = image;
			}
			else if(anApplyingAmount === aBillAmount) {
				image = '<img alt="search" src="./../resources/Icons/bill_full.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',0,'+options.rowId+',this)">';
				cellvalue = image;
			}
			else if(anApplyingAmount > aBillAmount && 0 < aBillAmount){
				
				image='<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',0,'+options.rowId+',this)">';
				cellvalue = image;
			}
		 
	
	return cellvalue;
}
//--

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

			  if(payBill.match("bill_empty.png"))
			   {				  
			 
			 	var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
			 	
					if((unApAmt-billAmt)>0)
					{
					src="./../resources/Icons/bill_full.png";
					saveDetailsfromimageclick(billAmt,rowId);
					
					var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData.paymentApplied = billAmt;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 		

					var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData1.remaining = 0;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
					
					$("#payBillChk_"+rowId).attr("checked",true);
					
					}

					else if(unApAmt == 0)
					{
					$("#LoadingDialog").css({"display":"none"});
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:red;">There is no Unapplied Payment.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
											buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
										}).dialog("open");
					$("#payBillChk_"+rowId).attr("checked",false);

					}
					else 
					{
					src="./../resources/Icons/bill_full.png";
					saveDetailsfromimageclick(billAmt,rowId);
					
					var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData.paymentApplied = unApAmt;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 

					var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData1.remaining = (billAmt-unApAmt);
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
					
					$("#payBillChk_"+rowId).attr("checked",true);
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
				
				$("#payBillChk_"+rowId).attr("checked",false);
				}

	 		var payBill = jQuery("#customerPaymets").jqGrid('setCell', rowId, 'payBill','<img alt="search" '+src+' style="cursor:pointer;" onclick="payFullAmount('+billAmt+','+appAmt+',0,'+rowId+',this)">');	

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

//Possitive Invoices check off and check on  
function payFullAmount(billAmt,appAmt,discountAmt,rowId,img)
{
//	alert(img);
	//$("#LoadingDialog").css({"display":"inline"});
	editCustomerpayment='cp';
	
	console.log("BillAmt="+billAmt+"AppAmt="+appAmt+"DiscountAmt = "+discountAmt);
	

		  if(img.src.match("bill_empty.png"))
		   {				  
		 
		 	var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
		 	
				if((unApAmt-billAmt)>0)
				{
				img.src="./../resources/Icons/bill_full.png";
				saveDetailsfromimageclick(billAmt,rowId);
				
				var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowData.paymentApplied = billAmt;
				$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 		

				var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowData1.remaining = 0;
				$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
				
				$("#payBillChk_"+rowId).attr("checked",true);
				
				}

				else if(unApAmt == 0)
				{
				$("#LoadingDialog").css({"display":"none"});
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:red;">There is no Unapplied Payment.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
										buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
									}).dialog("open");
				$("#payBillChk_"+rowId).attr("checked",false);

				}
				else 
				{
				img.src="./../resources/Icons/bill_full.png";
				saveDetailsfromimageclick(billAmt,rowId);
				
				var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowData.paymentApplied = unApAmt;
				$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 

				var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowData1.remaining = (billAmt-unApAmt);
				$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
				
				$("#payBillChk_"+rowId).attr("checked",true);
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
			
			$("#payBillChk_"+rowId).attr("checked",false);
			}
		
	
}
//--


//Negative or Credit payment apply
function payDiscountFullAmount(billAmt,appAmt,rowId,img)
	//	$("#LoadingDialog").css({"display":"inline"});
{
		
		 if(img.src.match("cr_grey.png"))
		  {
			 
		    billAmt = billAmt;
		    img.src="./../resources/Icons/cr_red.png";
			var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
			
			saveDetailsfromimageclick(billAmt,rowId);
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
			$("#payBillChk_"+rowId).attr("checked",true);

		  }
	 	 else
		  {
			
			var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
			var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
			
			console.log(Number(rowData1.paymentApplied.replace(/[$,-]+/g,""))+"==="+unApAmt)
			
			if(Number(rowData1.paymentApplied.replace(/[$,-]+/g,"")) > unApAmt)
			{
				$("#LoadingDialog").css({"display":"none"});
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:red;">You must unapply invoices first. since this credit is used.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
										buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
									}).dialog("open");
			}
			else
			{
			img.src="./../resources/Icons/cr_grey.png";
			saveDetailsfromimageclick(0,rowId);
			img.src="./../resources/Icons/cr_grey.png";

		
			rowData1.remaining = Number(rowData1.remaining.replace(/[$,]+/g,""))+Number(rowData1.paymentApplied.replace(/[$,]+/g,""));
			$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
			
			var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
			rowData.paymentApplied = 0;
			$('#customerPaymets').jqGrid('setRowData', rowId, rowData);

			$("#payBillChk_"+rowId).attr("checked",false);
			}
		  }
	
	
}

//--



//Apply Payment from image click
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
		
		//alert(ogpayApplied+"=="+billAmt+"=="+paymentID)
		
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
			payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
			creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
			invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
			discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
			unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

			var calc_unApplied=0;

				if(Number(unApAmt)+Number(ogpayApplied)>=Number(billAmt))
				{
					$("#invoiceappliedId").val(formatCurrency(Number(invAmt)-Number(ogpayApplied)+Number(billAmt))); // Add Invoice Amount  
					calc_unApplied = Number(payAmt)+Number(creditAmt); // Calculation of credit total
					$("#unappliedpaymentId").val(formatCurrency(Number(calc_unApplied) - parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,"")))); // total for unappliedPayment
				}
				else
				{
					$("#invoiceappliedId").val(formatCurrency(Number(invAmt)-Number(ogpayApplied)+Number(unApAmt)));
					$("#unappliedpaymentId").val(formatCurrency(0));
				}
			console.log($("#unappliedpaymentId").val());
			amtApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
			appliedAmt = amtApplied;

			
			
		}
		else if(billAmt<0) // Credit amt only
		{
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
			payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
			creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
			discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
			invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
			unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
			if(ogpayApplied>0)
			{
				
				if(ogRemaining!=0)
				{
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

				$("#unappliedpaymentId").val(formatCurrency(Number(unApAmt) + parseFloat(ogpayApplied)));	
				$("#invoiceappliedId").val(formatCurrency(Number(invAmt)-parseFloat(ogpayApplied)));
				}

				//if(ogdisAmt!=0)
					$("#discountgivenId").val(formatCurrency(discAmt - ogdisAmt));
				
			 	amtApplied = Number(ogpayApplied)+Number(ogdisAmt);
				appliedAmt = 0;
				
			
			}
			else
			{
				$("#creditusingId").val(formatCurrency(Number(creditAmt) + Number(ogpayApplied)));
				$("#unappliedpaymentId").val(formatCurrency(Number(unApAmt) + Number(ogpayApplied)));
				
				//if(ogdisAmt!=0)
					$("#discountgivenId").val(formatCurrency(discAmt - ogdisAmt));
				amtApplied = Number(ogpayApplied)+Number(ogdisAmt);
				appliedAmt = 0;

			}
		}

 		return true;
}

//--


//Save Payments while save and close button click
function payToLedger(){

	$("#LoadingDialog").css({"display":"inherit"});
	//Get Paid Rows only
	var rows = jQuery("#customerPaymets").getDataIDs();
	paidInvoiceDetails=new Array();
	var i =0;
	 for(var a=0;a<rows.length;a++)
	 {
	    row=jQuery("#customerPaymets").getRowData(rows[a]);
	    
	    console.log($('#customerPaymets').jqGrid('getCell',row,'customerPaymets_payBill'))
	    
	   var id="#payBillChk_"+rows[a];
	   var canDelete=$(id).is(':checked');
	   if(canDelete){
		   row=jQuery("#customerPaymets").getRowData(rows[a]);
		   paidInvoiceDetails[i]=row;
		   i++;
	  }
	 }
	 
	 console.log(paidInvoiceDetails);
	 
	 
	var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
	 
	//Check period is open
/*	$.ajax({
		url: "./checkAccountingCyclePeriods",
		data:{"datetoCheck":$("#paymentdateid").val(),"UserStatus":checkpermission},
		type: "POST",
		success: function(data) { 
				if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
				{
	*/
				//	periodid=data.cofiscalperiod.coFiscalPeriodId;
				//	yearid = data.cofiscalperiod.coFiscalYearId;
					
					//Apply Payment 
					$.ajax({
					url: "./custpaymentslistcontroller/payMultipleInvoice",
					data:{
					"rxcustomerID":$("#customerID").val(),
					"recieptID":$("#recieptID").val(),
					"recieptAmt":$("#payAmountId").val().replace(/[$,]+/g,""),
					"memo":$("#payMemoId").val(),
					"receiptDate":$("#paymentdateid").val(),
					"reference":$("#payCheckId").val(),
					"cuReceiptTypeId":$("#paymentReciptTypeId option:selected").val(),
					"discountAmt":$("#discountgivenId").val().replace(/[$,]+/g,""),
					"paidInvoiceDetails":JSON.stringify(paidInvoiceDetails)},
					type: "POST",
					success: function(data) { 
						$('#customerPaymets').trigger("reloadGrid");
						$("#LoadingDialog").css({"display":"none"});
						createtpusage('Company-Customer-Payment','Save Payment','Info','Company-Customers-Payment,Save Payment,recieptID:'+$("#recieptID").val()+',recieptAmt:'+$("#payAmountId").val()+',memo:'+$("#payMemoId").val());
						$("#LoadingDialog").css({"display":"none"});
						document.location.href = "./customerunappliedpaymentlist";
						
					   	},
		   			error:function(data){
		   				console.log('error');
		   				}
		   			});
					
			/*	}
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
	*/
}
