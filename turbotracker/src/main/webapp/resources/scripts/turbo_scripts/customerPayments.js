//Created By Leo:   Date: March 11 2016  Purpose: Customer Payment

var opInvoiceNo ='';
var opInvoiceID='';
var opLinkageId='';
var opAmount=0.0;
var _global_periodID;
var _global_yearID;
var _globalpaidstatus = "";



// JQuery Init
jQuery(document).ready(function(){
	
	// For Reverse Button Greyed based receipt id
	if($("#recieptID").val()=="0")
	{
	$('#reversePaymentId').attr("disabled",true);
	$('#reversePaymentId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
	}
	else
	{
	$('#reversePaymentId').removeAttr("disabled");
	$('#reversePaymentId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))','cursor':'pointer'});
	}

	//Date Picker Init
	$('#paymentdateid').datepicker();
	//getcoFiscalPerliodDate("paymentdateid");
	//$('#paymentdateid').datepicker("setDate",new Date());
	loadData()
	var customerId = $("#customerID").val();
	$('#paymentReciptTypeId').val($('#PaytypeId').val());
	
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
	
	
	//init detail dialog box
	jQuery(function(){
		jQuery("#EditDetailsDlg").dialog({
			autoOpen:false,
			title:"Payment Details",
			width: "400",
			modal:true,
			close:function(){ return true;}	
		});
	});
	//$('#payingRadio').click(payingList);
	//$('#dueRadio').click(due);

	
	//PreLoad Data
	function loadData(){
		var customerID = $('#customerID').val();
		$.ajax({
			url: "./custpaymentslistcontroller/getPreLoadData",
			type: "POST",
			data : "&customerID="+customerID+'&recieptID='+$('#recieptID').val()+'&reversePaymentStatusID='+$("#reversePaymentStatusID").val(),
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
				
				
				if($("#reversePaymentStatusID").val()!="true")
				{
					if(customerID=="0")
					{
					due();
					$('#dueRadio').attr('checked',true);
					}
					else
					{
					payingList();
					$('#payingRadio').attr('checked',true);
					}
				}
				else
				{
					reversepaymentList();
					$('#payingRadio').attr('checked',true);
				}
				
			}
		});
	}
	
	// on key press event in amount field
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
//--

//open period only
function getcoFiscalPerliodDate(x)
{
	/* $.ajax({
		 
	 		url: "./banking/getcoFiscalPeriod",
	 		type: "GET",
	 		success: function(data) {
	 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
	 					
	 		}		 
	 });	 */
}

//Customer AutoComplete 
$(function() { var cache = {}; var lastXhr='';
$( "#payCustomer" ).autocomplete({
	minLength: 2, timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerID").val(id);
	due();
	resetFields();
	},
	source: function( request, response ) { var term = request.term;
		if( term in cache ){ response( cache[ term ] ); return; }
		lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
	});
});
//--

//Reset all fields
function resetFields()
{
	
	$("#payAmountId").val("$0.00");
	$('#paymentdateid').datepicker("setDate",new Date());
	$("#paymentReciptTypeId").val(0);
	$("#payCheckId").val("");
	$("#payMemoId").val("");
	$("#paymentId").val("$0.00");
	$("#creditusingId").val("$0.00");
	$("#invoiceappliedId").val("$0.00");
	$("#discountgivenId").val("$0.00");
	$("#unappliedpaymentId").val("$0.00");

}
//Due radio button enable
function due(){
		var customerID = $('#customerID').val();
		var Oper = "due";
		//$("#payDetailsId").removeAttr("disabled");
		getcoFiscalPerliodDate("paymentdateid");
		$('#paymentdateid').datepicker("setDate",new Date());
		
		$('#editInvoiceId').removeAttr("disabled");
		$('#editInvoiceId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))','cursor':'pointer'});
		$('#saveInvoiceId').removeAttr("disabled");
		$('#saveInvoiceId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))','cursor':'pointer'});
		
		$("#payCustomer").removeAttr("disabled");
		$("#payAmountId").removeAttr("disabled");
		$('#paymentdateid').removeAttr("disabled");
		$("#paymentReciptTypeId").removeAttr("disabled");
		$("#payCheckId").removeAttr("disabled");
		$("#payMemoId").removeAttr("disabled");
		
		$("#customerPaymets").jqGrid('GridUnload');
		loadInvoicesGrid(customerID, Oper);
		$("#customerPaymets").trigger("reloadGrid");
		var colPos = 12;
    var myGrid = $('#customerPaymets');
    myGrid.jqGrid('showCol', myGrid.getGridParam("colModel")[colPos].name);
    myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos + 1].name);
    
    $('#dueRadio').removeAttr("disabled");
    $('#payingRadio').attr("disabled",true);
}
//--


//Paying radio button enable
function payingList(){
		var customerID = $('#customerID').val();
		var Oper = "paying";
		//$("#payDetailsId").attr("disabled",true);

		$('#editInvoiceId').attr("disabled",true);
		$('#editInvoiceId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
		$('#saveInvoiceId').attr("disabled",true);
		$('#saveInvoiceId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
		
		
		$("#payCustomer").attr("disabled",true);
		$("#payAmountId").attr("disabled",true);
		$('#paymentdateid').attr("disabled",true);
		$("#paymentReciptTypeId").attr("disabled",true);
		$("#payCheckId").attr("disabled",true);
		$("#payMemoId").attr("disabled",true);
		
		$("#customerPaymets").jqGrid('GridUnload');
		loadInvoicesGrid(customerID,Oper);
		$("#customerPaymets").trigger("reloadGrid");
		var colPos = 12;
    var myGrid = $('#customerPaymets');
    myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos ].name);
    myGrid.jqGrid('showCol', myGrid.getGridParam("colModel")[colPos + 1].name);
    
    $('#dueRadio').attr("disabled",true);
    $('#payingRadio').removeAttr("disabled");
}
//--

function reversepaymentList()
{
	var customerID = $('#customerID').val();
	var Oper = "reverse";
	//$("#payDetailsId").attr("disabled",true);

	$('#editInvoiceId').attr("disabled",true);
	$('#editInvoiceId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
	$('#saveInvoiceId').attr("disabled",true);
	$('#saveInvoiceId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
	$('#payDetailsId').attr("disabled",true);
	$('#payDetailsId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
	$('#reversePaymentId').attr("disabled",true);
	$('#reversePaymentId').css({'background':'-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))','cursor':'default'});
	
	$("#payCustomer").attr("disabled",true);
	$("#payAmountId").attr("disabled",true);
	$('#paymentdateid').attr("disabled",true);
	$("#paymentReciptTypeId").attr("disabled",true);
	$("#payCheckId").attr("disabled",true);
	$("#payMemoId").attr("disabled",true);
	
	$("#customerPaymets").jqGrid('GridUnload');
	loadInvoicesGrid(customerID,Oper);
	$("#customerPaymets").trigger("reloadGrid");
	var colPos = 12;
	var myGrid = $('#customerPaymets');
	myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[colPos ].name);
	myGrid.jqGrid('showCol', myGrid.getGridParam("colModel")[colPos + 1].name);

$('#dueRadio').attr("disabled",true);
$('#payingRadio').removeAttr("disabled");

}


//Cancel the payment
	function cancelPayment() {
	document.location.href = "./customerpaymentlist";
}
//--
	
	
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

			var rowData = jQuery(this).getRowData(rowid); 
			var invB = rowData["invoiceBalance"].replace('$','');
			invB= invB.replace(",","");
			
			if($("#reversePaymentStatusID").val()!="true")
			EditDetails();
				
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
	 
	 if($("#dueRadio").is(":checked"))
		 {
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
				image = '<img alt="search" src="./../resources/Icons/bill_empty.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',-1,'+options.rowId+',this)">';
				cellvalue = image;
			} 
			else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){
	
				image = '<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',-1,'+options.rowId+',this)">';
				cellvalue = image;
			}
			else if(anApplyingAmount === aBillAmount) {
				image = '<img alt="search" src="./../resources/Icons/bill_full.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',-1,'+options.rowId+',this)">';
				cellvalue = image;
			}
			else if(anApplyingAmount > aBillAmount && 0 < aBillAmount){
				
				image='<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" onclick="payFullAmount('+balanceAmt+','+anApplyingAmount+',-1,'+options.rowId+',this)">';
				cellvalue = image;
			}
		 }
	 else
		 {
		 if(balanceAmt < 0.00 && (anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0.00 ))
			{
				image='<img alt="search" src="./../resources/Icons/cr_grey.png" style="cursor:pointer;">';
				cellvalue = image;
			}
			else if(balanceAmt < 0 && (balanceAmt === anApplyingAmount|| anApplyingAmount > balanceAmt))
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
	
				image = '<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;" >';
				cellvalue = image;
			}
			else if(anApplyingAmount === aBillAmount) {
				image = '<img alt="search" src="./../resources/Icons/bill_full.png" style="cursor:pointer;" >';
				cellvalue = image;
			}
			else if(anApplyingAmount > aBillAmount && 0 < aBillAmount){
				
				image='<img alt="search" src="./../resources/Icons/bill_half.png" style="cursor:pointer;">';
				cellvalue = image;
			}
		 }
	  
	return cellvalue;
}
//--


//End Balance calculation in popup
function calculateEndBalance() {

	var endBalance = Number($('#balance').text().replace(/[^0-9\.]+/g,""))-((Number($('#appliedAmt').val().replace(/[^0-9\.]+/g,""))+(Number($('#DisountUsed').val().replace(/[^0-9\.]+/g,"")))));
	if(isNaN(endBalance)){
		$('#EndingBal').text(formatCurrency(0));
	}else{
	$('#EndingBal').text(formatCurrency(endBalance.toFixed(2)));
	}
}
//--


//Edit Customer Invoice
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
//--

//Validation function 
function selectRow(){
		msgOnAjax("Please select any row from the grid to see the Details.","RED",6000);
	return false;
}
//--


//cancel in popup box
function cancelEdit(){
	$('#EditDetailsDlg').dialog("close");
}
//--

// Save and close	
function SaveAndClose(){
	document.location.href = "./customerpaymentlist";
}
//--

//EditInvoice button click	
function EditDetails(){
	
	if($("#payingRadio").is(":checked"))
		{
		$("#payInvoiceId").css("display","none");
		}
	else
		{
		$("#payInvoiceId").css("display","inherit");
		}
	
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
//--	


//Possitive Invoices check off and check on  
function payFullAmount(billAmt,appAmt,discountAmt,rowId,img)
{
//	alert(img);
	//$("#LoadingDialog").css({"display":"inline"});
	editCustomerpayment='cp';
	
	console.log("BillAmt="+billAmt+"AppAmt="+appAmt+"DiscountAmt = "+discountAmt);
	
	if(discountAmt!=null && discountAmt == "-1")
		{
			
		  if(img.src.match("bill_empty.png"))
		   {				  
		 
		 	var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
		 	
		 	if(appAmt!=null && appAmt!="0")
		 	{
		 		if((unApAmt-appAmt)>0)
				{
		 			img.src="./../resources/Icons/bill_full.png";
					saveDetailsfromimageclick(appAmt,rowId);
					
					var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData.paymentApplied = appAmt;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData);
					
					var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
					rowData1.remaining = billAmt - appAmt;
					$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
		
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
	else
		{
		
		
			var unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
		
				img.src="./../resources/Icons/bill_half.png";
				saveDetailsfromimageclick(appAmt,rowId);
				
				var rowData = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowData.paymentApplied = appAmt;
				$('#customerPaymets').jqGrid('setRowData', rowId, rowData); 		
				
				var rowDataDisc = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowDataDisc.preDiscountUsed = discountAmt;
				$('#customerPaymets').jqGrid('setRowData', rowId, rowDataDisc); 		
	
				var rowData1 = $('#customerPaymets').jqGrid('getRowData', rowId);
				rowData1.remaining = billAmt-(appAmt+discountAmt);
				$('#customerPaymets').jqGrid('setRowData', rowId, rowData1); 
				
				$("#payBillChk_"+rowId).attr("checked",true);
				
			
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

//Credit Alert
function showCreditAlert()
{
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:red;">You must unapply invoices first. since this credit is used.</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
							buttons: [{text: "OK",click: function(){$(this).dialog("close");location.reload(); }}]
						}).dialog("open");
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
					var invoiceAmount=$("#invoiceappliedId").val();
					calc_unApplied = Number.parseFloat(payAmt)+Number.parseFloat(creditAmt); // Calculation of credit total
					calc_unApplied=floorFigureoverall(calc_unApplied,2);
					var invoiceAppliedId=$("#invoiceappliedId").val();
					$("#unappliedpaymentId").val(formatCurrency(Number.parseFloat(calc_unApplied) - Number.parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,"")))); // total for unappliedPayment
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

			$("#unappliedpaymentId").val(formatCurrency((Number.parseFloat(payAmt)+Number.parseFloat(creditAmt))- parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""))));
			
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

						$("#unappliedpaymentId").val(formatCurrency(Number.parseFloat(unApAmt)  + Number.parseFloat(ogpayApplied)));	
						$("#invoiceappliedId").val(formatCurrency(Number.parseFloat(invAmt)-Number.parseFloat(ogpayApplied)));
						}
					else
						{
						$("#unappliedpaymentId").val(formatCurrency(Number.parseFloat(unApAmt)  + Number.parseFloat(ogpayApplied)));	
						$("#invoiceappliedId").val(formatCurrency(Number.parseFloat(invAmt)-Number.parseFloat(ogpayApplied)));
						}
				
				}
				else
				{

				$("#unappliedpaymentId").val(formatCurrency(Number.parseFloat(unApAmt) + Number.parseFloat(ogpayApplied)));	
				$("#invoiceappliedId").val(formatCurrency(Number.parseFloat(invAmt)-Number.parseFloat(ogpayApplied)));
				}

				//if(ogdisAmt!=0)
					$("#discountgivenId").val(formatCurrency(discAmt - ogdisAmt));
				
			 	amtApplied = Number(ogpayApplied)+Number(ogdisAmt);
				appliedAmt = 0;
				
			
			}
			else
			{
				$("#creditusingId").val(formatCurrency(Number(creditAmt) + Number(ogpayApplied)));
				$("#unappliedpaymentId").val(formatCurrency(Number.parseFloat(unApAmt) + Number.parseFloat(ogpayApplied)));
				
				//if(ogdisAmt!=0)
					$("#discountgivenId").val(formatCurrency(discAmt - ogdisAmt));
				amtApplied = Number.parseFloat(ogpayApplied)+Number.parseFloat(ogdisAmt);
				appliedAmt = 0;

			}
		}

   		return true;
}

//--

//Apply Payment from Popup click

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
		var alreadypaidamt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
		var alreadypaiddiscamt = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'preDiscountUsed').replace(/[$,]+/g,"");
		var ogRemaining = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'remaining').replace(/[$,]+/g,"");
		var oper ="";
		
		var imgValue = new Image();
		
		imgValue.src="./../resources/Icons/bill_empty.png";
		
		/*if(Disc=="0.0")
			imgValue.src="./../resources/Icons/bill_full.png";
		else
			imgValue.src="./../resources/Icons/bill_half.png";
			*/
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

//	alert(appliedAmt+"=="+(Number(unApAmt)+Number(alreadypaidamt.replace(/[$,]+/g,""))));
		
	if(Number(unApAmt)+Number(alreadypaidamt.replace(/[$,]+/g,"")) > "0")
	{

   	if(appliedAmt!="0")
	 {
   	if(endAmt>="0" )
	 {
   		if(appliedAmt<=Number(unApAmt)+Number(alreadypaidamt.replace(/[$,]+/g,"")))
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
				
				
				if(appliedAmt>0) // billAmount only
				{
					payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
					creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
					invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
					discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
					unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));
					
					$("#discountgivenId").val(formatCurrency((Number(discAmt)-Number(alreadypaiddiscamt))+Number(Disc)));
					payFullAmount(invBalance,appliedAmt,Disc,rowId,imgValue);
					
					amtApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
					appliedAmt = amtApplied;
					
				}
				else // Credit amt only
				{
					creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
					$("#creditusingId").val(formatCurrency(Number(creditAmt)+Math.abs(appliedAmt)));

					payAmt = parseFloat($("#paymentId").val().replace(/[$,]+/g,""));	
					creditAmt = parseFloat($("#creditusingId").val().replace(/[$,]+/g,""));
					discAmt = parseFloat($("#discountgivenId").val().replace(/[$,]+/g,""));
					invAmt = parseFloat($("#invoiceappliedId").val().replace(/[$,]+/g,""));
					unApAmt = parseFloat($("#unappliedpaymentId").val().replace(/[$,]+/g,""));

					amtApplied = cuInvoiceIdgrid.jqGrid('getCell', rowId, 'paymentApplied').replace(/[$,]+/g,"");
					appliedAmt = amtApplied;
				}
				
				$('#EditDetailsDlg').dialog("close");	
				
		return true;
   	 }
   	else
   		{
   		$("#paymentError").text("Your applied amount is greater than the added payment amount");
   		setTimeout(function(){
   			$("#paymentError").text("");				
			},3000);
		return false;
   		}
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
								buttons: [{text: "OK",click: function(){$(this).dialog("close");}}]
							}).dialog("open");
		 	
			
   		}
	}


//Reverse Payment
function reversePayment(){
	$("#reversePaymentId").attr("disabled",true);
	$("#LoadingDialog").css({"display":"inherit","width":"993px","height":"753px","top":"0"});
	$.ajax({
		url: "./custpaymentslistcontroller/reversePaymentvalidation",
		type: "POST",
		data: {"recieptID":$('#recieptID').val()},
		success: function(data) { 
			$("#LoadingDialog").css({"display":"none"});
			if(data)
				deniedReversePayment();
			else
				applyReversePayment();
		   	},
			error:function(data){
				console.log('error');
				}
			});
}
//--

//Give popup if commission paid 
function deniedReversePayment()
{
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:red;">Employee Commissions have already been applied. You cannot reverse this customer payment</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
							buttons: [{text: "OK",click: function(){$(this).dialog("close");$("#reversePaymentId").removeAttr("disabled"); }}]
						}).dialog("open");
}
//--

//Give popup before process reverse payment
function applyReversePayment()
{
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:red;">Are you sure you want to Reverse Payment?</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
							buttons: [
							          {text: "Yes",click: function(){ callReversepayment();$(this).dialog("close"); }},
							          {text: "No",click: function(){$(this).dialog("close");$("#reversePaymentId").removeAttr("disabled"); }}
							          ]
						}).dialog("open");
}
//--

// Apply Reverse Payment
function callReversepayment()
{
	$("#LoadingDialog").css({"display":"inherit"});
	$.ajax({
		url: "./custpaymentslistcontroller/applyReversePayment",
		type: "POST",
		data: {"recieptID":$('#recieptID').val()},
		success: function(data) { 
			
			$("#LoadingDialog").css({"display":"none"});
			document.location.href = "./customerpaymentlist";
			
		   	},
			error:function(data){
				console.log('error');
				}
			});
}
//--





// Save Payments while save and close button click
function payToLedger(){
	
	 
	 if($('#customerID').val() == "" ||$('#customerID').val() == "0")
		{
		msgOnAjax("Mandatory fields are Required.","RED",6000);
		}
	 	if($('#paymentReciptTypeId').val() == "" ||$('#paymentReciptTypeId').val() == "0")
		{
		msgOnAjax("Mandatory fields are Required.","RED",6000);
		}
		else if($('#paymentReciptTypeId').val()==2)
		{
			if($('#payCheckId').val()=="")
			{
			msgOnAjax("Check# Required.","RED",6000);
			} 
			else
			{
				applypayments();
			}
		}
		else
			{
			applypayments();
			}
	
}

function applypayments()
{

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
	
					periodid=data.cofiscalperiod.coFiscalPeriodId;
					yearid = data.cofiscalperiod.coFiscalYearId;
					*/
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
						document.location.href = "./customerpaymentlist";
						
					   	},
		   			error:function(data){
		   				console.log('error');
		   				}
		   			});
					
		/*		}
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
   		});*/
	
}
   	



