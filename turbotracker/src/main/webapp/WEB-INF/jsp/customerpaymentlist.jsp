<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
 <meta http-equiv="refresh" content="900" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - CustomerPayments List</title>
		<style type="text/css">
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		</style>
</head>
<body>
<div style="background-color: #FAFAFA">
	<div>
		 <jsp:include page="./headermenu.jsp"></jsp:include> 
	</div>
	<table style="width:600px;margin:0 auto;">
	<tr>
	<td><label style="font-family: Verdana,Arial,sans-serif;color:#00377A;font-weight:bold;font-size:18px;">Search:</label>&nbsp;<input type="text" id="searchpaymentstext" style="width:400px;padding-left:5px;"/>   <input type="button" value="   Search" class="searchbutton" onclick="searchcustomerpayments()"></td>
	</tr>
	<tr>
	<td><label style="font-family: Verdana,Arial,sans-serif;color:#00377A;font-weight:bold;font-size:18px;">From:</label><input type="text" placeholder="MM/DD/YYYY" readonly id="fromdate" style = "margin-left:20px;padding-left:5px; width: 173px;"/><label style="font-family: Verdana,Arial,sans-serif;color:#00377A;font-weight:bold;font-size:18px;">&nbsp;To:&nbsp;</label> <input type="text" placeholder="MM/DD/YYYY" readonly id="todate" style ="padding-left:5px;  width: 173px;"/></td>
	</tr>
	</table>
	<table style="width:600px;margin:0 auto;">
			<tr>
			<td align="right">
				
				<table style="">
					<tr>
			    		  <!-- <td><input type="button" value="Show Unapplied Payments" class="advancedSearchbutton" style="width:170px;" id="" onclick="showunappliedamount()"> --> 
			    		  <input type="button" value="  Add" class="add" id="addpaymentlist" onclick="addPayment()"></td>
					</tr>
					
					
			    </table>
			</td>
	    </tr>
	    <tr id="allcustomerpaymentID">
	    	<td>
	    		<table id="customerpaymentlist"></table>
	    		<div id="cutomerpaymentGridpager"></div>
	    	</td>
	    </tr>
	</table>
	<div style="padding-top: 25px;">
		<table id="footer">
			<tr>
				<td colspan="2">
					<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
				</td>
			</tr>
		</table>
	</div>
	
	<script type="text/javascript">
	/** Document Ready Function cuReceiptTypeID **/
	jQuery(document).ready(function(){
		loadCutomerPaymentList('./custpaymentslistcontroller');
		$('#fromdate').datepicker();
		$('#todate').datepicker();
		$("#search").css("display", "none");
	});

	//Load Payment Grid
	function loadCutomerPaymentList(theUrl){
	var aCupayPage = $("#customerpaymentID").val();
	$("#customerpaymentlist").jqGrid({
		datatype: 'JSON',
		mtype: 'GET',
		url: theUrl,
		pager: jQuery('#cutomerpaymentGridpager'),
		colNames:['rxCustomerId','Date', 'Customer','Check No.','Memo','Amount($)','cuReceiptTypeID','RecieptId','Delete','Amount($)',''],
	   	colModel:[
      		{name:'rxMasterID',index:'rxMasterID', align:'right', width:80,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'receiptDate',index:'receiptDate', align:'center', width:80,editable:true, editrules:{required:true}, editoptions:{size:10}, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.reversePaymentStatus==true){  return 'style="color: red; "';}}},
			{name:'customer',index:'customer', align:'left', width:170,editable:true, editrules:{required:true},editoptions:{size:10}, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.reversePaymentStatus==true){  return 'style="color: red; "';}}},
			{name:'reference',index:'reference', align:'center', width:80,editable:true, editrules:{required:true},editoptions:{size:10}, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.reversePaymentStatus==true){  return 'style="color: red; "';}}},
			{name:'memo',index:'memo', width:100, align:'center', editable:true, editrules:{required:true},editoptions:{size:10}, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.reversePaymentStatus==true){ return 'style="color: red; font-weight:bold;"'}},formatter:updateMemoValue},
			{name:'amount',index:'amount', width:80, align:'right', editable:true, editrules:{required:true},editoptions:{size:10},  formatter:customCurrencyFormatter, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.reversePaymentStatus==true){  return 'style="color: red; "';}}},
			{name:'cuReceiptTypeID',index:'cuReceiptTypeID', width:80, align:'right',hidden:true, editable:true, editrules:{required:true},editoptions:{size:10}},
			{name:'cuReceiptID',index:'cuReceiptID', width:100, align:'center', editable:false,hidden:true, editrules:{required:true},editoptions:{size:10}},
			{name:'Delete', index:'Option', align:'center', hidden:true, width:60, editable:true, edittype:'text', editoptions:{size:30}, formatter:deleteImgFormatter, editrules:{required:false}},
			{name:'paymentApplied',index:'paymentApplied', width:80, align:'right',hidden:true, editable:true, editrules:{required:true},editoptions:{size:10},  formatter:customCurrencyFormatter},
			{name:'reversePaymentStatus',index:'reversePaymentStatus', width:80, align:'right',hidden:true},
		],
		rowNum: 50,
		pgbuttons: true,
		altRows: true,
		altclass:'myAltRowClass',
		recordtext: '',
		rowList: [50, 100, 500, 1000],	viewrecords: true,
		pager: '#cutomerpaymentGridpager',
		sortname: 'receiptDate', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: 'Customer Payments',
		height:500,	width: 1000,/*scrollOffset:0,*/ rownumbers:true,
		loadComplete: function(data) {
			//$(".ui-pg-selbox").attr("selected", aTaxPage);
		},
		loadError : function (jqXHR, textStatus, errorThrown) {
			var errorText = $(jqXHR.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:RED;">Error: ' + errorText + '</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");}}]}).dialog("open");
			return false;
		},
		jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
    	ondblClickRow: function(rowid) {
    		var rowData = jQuery(this).getRowData(rowid); 
    		var rxCustomerId = rowData['rxMasterID'];
			var customer = rowData['customer'];
			var checkNo = rowData['reference'];
			var memo = rowData['memo'];
			var amount = rowData['amount'].replace(	/[^0-9\.]+/g,"");
			var recieptID = rowData['cuReceiptID'];
			var Date = rowData['receiptDate'];
			var recieptTypeId = rowData['cuReceiptTypeID'].replace(/[^0-9\.]+/g,"");
			var reversePaymentStatus = rowData['reversePaymentStatus'];
			console.log(recieptTypeId);
			document.location.href = "./customerpayments?rxCustomerId="+rxCustomerId+"&customer="+encodeURIComponent(customer)+"&checkNo="+encodeURIComponent(checkNo)+"&amount="+amount+"&recieptID="+recieptID+"&Date="+Date+"&memo="+encodeURIComponent(memo)+"&recieptTypeId="+recieptTypeId+"&reversepaymentstatus="+reversePaymentStatus;
		},
		 onSelectRow: function(id){
			var rowData = jQuery(this).getRowData(id); 
			var recieptId = rowData["cuReceiptID"];
		} 
	}).navGrid('#userGridpager',
			{add:false,edit:false,del:false,refresh:false,search:false});
	}
	
	function updateMemoValue(cellvalue, options, rowObject)
	{
	if(rowObject.reversePaymentStatus)	
	cellvalue = "PAYMENT REVERSED";

	return cellvalue;
	}

	
	function searchcustomerpayments(){
		try{
		var searchTextvalue = $('#searchpaymentstext').val();
		var fromdate = $('#fromdate').val();
		var todate = $('#todate').val();
		var status = 'empty';
		if(fromdate!='' && todate!='')
			status = 'both';
		if(fromdate=='' && todate!='')
			status = 'to';
		if(fromdate!='' && todate=='')
			status = 'from';
		
		jQuery("#customerpaymentlist").jqGrid('GridUnload');
	    loadCutomerPaymentList('./custpaymentslistcontroller/searchCustomerPayments?searchText='+searchTextvalue+'&fromdate='+fromdate+'&todate='+todate+'&status='+status);
	    jQuery("#customerpaymentlist").trigger("reloadGrid");
		}catch(err){
		}
	}

	function deleteImgFormatter(cellvalue, options, rowObject){
		image = '<a href="#" onClick="deletePayment()"><img alt="search" src="./../resources/images/delete.png">';
		cellvalue = image;
		return cellvalue;
	}

	
	$(function () {
	    $("#fromdate").datepicker({
	        changeMonth: true,
	        changeYear: true,
	        onChangeMonthYear: function (year, month, inst) {
	            var curDate = $(this).datepicker("getDate");
	            if (curDate == null)
	            return; 
	            if (curDate.getYear() != year || curDate.getMonth() != month - 1) {
	                curDate.setYear(year);
	                curDate.setMonth(month - 1);
	                $(this).datepicker("setDate", curDate);
	            }
	        }
	    });
	});


	//Add Payment Button Click
	function addPayment() {
		document.location.href = "./customerpayments";
	}

	
</script>
</body>
</html>