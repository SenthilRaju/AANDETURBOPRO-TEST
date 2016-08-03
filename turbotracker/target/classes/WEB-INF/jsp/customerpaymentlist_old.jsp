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
	<div id="addPaymentsDialog">
	<table></table>
	<fieldset style="" class= "custom_fieldset">
		<legend class="custom_legend"><label><b>Payments</b></label></legend>
		<form id="addPaymentsFormID" action="" >
		<table>
			<tr>
				<td>
				<label> Customer:<span style="color:red">*</span></label>
				</td>
				<td><input type="text" id="customerNamePayments" name = "cusotmerName" />
				<input type="text" style="display: none;" id="JobCustomerId" name="customerIDName"></td>
			</tr>
			<tr>
				<td>
				<label> Amount:<span style="color:red">*</span></label>
				</td>
				<td><input type="text" id="amountID" name = "amountName" /></td>
				<td> Date: </td>
				<td><input type="text" id="paymentDateID" readonly="readonly"  name="paymentDateName"></td>
			</tr>
			<tr>
				<td>
				<label> Type:</label>
				</td>
					<td>
						<select id="paymentTypeID" name="paymentTypeName" style="width: 170px;">
									<c:forEach var="pamenyType" items="${requestScope.pamenyType}">
										<option value="${pamenyType.cuReceiptTypeId}">
									<c:out value="${pamenyType.description}"></c:out>
								</option>
							</c:forEach>
						</select>
					</td>
			</tr>
			<tr>
				<td>
				<label> Check #:</label>
				</td>
				<td><input type="text" id="checkNumberID" name = "checkNumberName" /></td>
			</tr>
			<tr>
				<td>
				<label> Memo:</label>
				</td>
				<td><input type="text" id="memoID" name = "memoName" /></td>
			</tr>
			</table>
	</form>
			</fieldset>
			<br>
			<div id="customerPaymentGrid" >
			<table id="customernewpaymentlist"></table>
	    		<div id="cutomerpaymentGridpager"></div>
					<div id="toggleMessageID" style="text-align: right;"><b>
					Enter the Payment Information then click the save button to apply the payment to invoice.
					</b></div>
					<div id=>
					<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="savePayment()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelPayment()" style="width:80px;">
					<span id="customerpaymenterrorstatus" style="color:red"></span>
					</div>
	</div>
	<div style="display: none;">
		<input type="text" id="customerpaymentID" name="customerpaymentName" value="${requestScope.userDetails.customerperpage}">
	</div>
</div>
	<script type="text/javascript">
	/** Document Ready Function cuReceiptTypeID **/
	jQuery(document).ready(function(){
		$('#paymentDateID').datepicker();
		loadCutomerPaymentList('./custpaymentslistcontroller');
		$('#fromdate').datepicker();
		$('#todate').datepicker();
		
		getcoFiscalPerliodDate("paymentDateID");
		$('#paymentDateID').datepicker("setDate",new Date());
		
		$("#search").css("display", "none");
		jQuery(function() {
			jQuery("#addPaymentsDialog").dialog({autoOpen : false,modal : true,title : "Add Payments",height : 300,width : 550,buttons : {},close : function() {return true;}});
		});
	});

	function loadCutomerPaymentList(theUrl){
	var aCupayPage = $("#customerpaymentID").val();
	$("#customerpaymentlist").jqGrid({
		datatype: 'JSON',
		mtype: 'GET',
		url: theUrl,
		pager: jQuery('#cutomerpaymentGridpager'),
		colNames:['rxCustomerId','Date', 'Customer','Check No.','Memo','Amount($)','cuReceiptTypeID','RecieptId','Delete','Amount($)'],
	   	colModel:[
      		{name:'rxMasterID',index:'rxMasterID', align:'right', width:80,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'receiptDate',index:'receiptDate', align:'center', width:80,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'customer',index:'customer', align:'left', width:170,editable:true, editrules:{required:true},editoptions:{size:10}},
			{name:'reference',index:'reference', align:'center', width:80,editable:true, editrules:{required:true},editoptions:{size:10}},
			{name:'memo',index:'memo', width:100, align:'center', editable:true, editrules:{required:true},editoptions:{size:10}},
			{name:'amount',index:'amount', width:80, align:'right', editable:true, editrules:{required:true},editoptions:{size:10},  formatter:customCurrencyFormatter},
			{name:'cuReceiptTypeID',index:'cuReceiptTypeID', width:80, align:'right',hidden:true, editable:true, editrules:{required:true},editoptions:{size:10}},
			{name:'cuReceiptID',index:'cuReceiptID', width:100, align:'center', editable:false,hidden:true, editrules:{required:true},editoptions:{size:10}},
			{name:'Delete', index:'Option', align:'center', hidden:true, width:60, editable:true, edittype:'text', editoptions:{size:30}, formatter:deleteImgFormatter, editrules:{required:false}},
			{name:'paymentApplied',index:'paymentApplied', width:80, align:'right',hidden:true, editable:true, editrules:{required:true},editoptions:{size:10},  formatter:customCurrencyFormatter},
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
			console.log(recieptTypeId);
			
			document.location.href = "./customerpayments?rxCustomerId="+rxCustomerId+"&customer="+encodeURIComponent(customer)+"&checkNo="+encodeURIComponent(checkNo)+"&amount="+amount+"&recieptID="+recieptID+"&Date="+Date+"&memo="+encodeURIComponent(memo)+"&recieptTypeId="+recieptTypeId;

			/* $.post("./customerpayments", {"rxCustomerId": rxCustomerId,"customer":customer,"checkNo":checkNo,"amount":amount,"recieptID":recieptID,"Date":Date,"memo":memo,"recieptTypeId":recieptTypeId}, function(result){
		    }); */
		},
		 onSelectRow: function(id){
			var rowData = jQuery(this).getRowData(id); 
			var recieptId = rowData["cuReceiptID"];
			
			
		} 
	}).navGrid('#userGridpager',
			{add:false,edit:false,del:false,refresh:false,search:false});
	}
	function addPayment() {
		$('#addPaymentsDialog').dialog("open");
	}
	
	$(function() { var cache = {}; var lastXhr='';
	$( "#customerNamePayments" ).autocomplete({
		minLength: 2, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerId").val(id); },
		source: function( request, response ) { var term = request.term;
			if( term in cache ){ response( cache[ term ] ); return; }
			lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
		});
	});
	function cancelPayment() {
		$('#addPaymentsFormID :input').val(" ");
		$('#addPaymentsDialog').dialog("close");
	}
	function savePayment() {


			 if($('#JobCustomerId').val() == "")
				{
				$('#customerpaymenterrorstatus').html("Mandatory fields are Required");
				setTimeout(function(){
					$('#customerpaymenterrorstatus').html("");
					},3000);
				}
			else if($('#amountID').val() == "")
				{
				$('#customerpaymenterrorstatus').html("Mandatory fields are Required");
				setTimeout(function(){
					$('#customerpaymenterrorstatus').html("");
					},3000);
				}			
			else if($('#paymentTypeID').val()==2)
				{
				if($('#checkNumberID').val()=="")
					{

					$('#customerpaymenterrorstatus').html("Check# Required");
					setTimeout(function(){
						$('#customerpaymenterrorstatus').html("");
						},3000);
					} 
				else
					{

					var payFromData = $('#addPaymentsFormID').serialize();
					
					var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
			   		$.ajax({
						url: "./checkAccountingCyclePeriods",
						data:{"datetoCheck":$("#paymentDateID").val(),"UserStatus":checkpermission},
						type: "POST",
						success: function(data) { 
							if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
							{
								periodid=data.cofiscalperiod.coFiscalPeriodId;
								yearid = data.cofiscalperiod.coFiscalYearId;
								
								payFromData = payFromData + "&periodid="+periodid+"&yearid="+yearid
								
							 	$.ajax({
									url:"./custpaymentslistcontroller/saveCustomerPayments",
									type: "POST",
									data: payFromData,
									success: function(data) {
										/* $('#addPaymentsFormID :input').val(" ");
										$('#addPaymentsDialog').dialog("close");
										$('#customerpaymentlist').trigger("reloadGrid"); */
										createtpusage('Company-Customer-Payment','Save Payment','Info','Company-Customer-Payment,Save Payment,rxCustomerId='+data.rxCustomerId+',customer='+$("#customerNamePayments").val()+",checkNo="+data.reference+",amount="+data.amount+",recieptID="+data.cuReceiptId+",Date="+$("#paymentDateID").val()+",memo="+data.memo+",recieptTypeId="+data.cuReceiptTypeId);
										console.log(data);
									//	console.log("1. No"+"\n 2."+data.amount+"\n 3."+data.cuReceiptTypeId+"\n 4."+data.memo+"\n 5."+data.rxCustomerId+"\n 6."+data.reference);
										document.location.href = "./customerpayments?rxCustomerId="+data.rxCustomerId+"&customer="+encodeURIComponent($("#customerNamePayments").val())+"&checkNo="+encodeURIComponent(data.reference)+"&amount="+data.amount+"&recieptID="+data.cuReceiptId+"&Date="+$("#paymentDateID").val()+"&memo="+encodeURIComponent(data.memo)+"&recieptTypeId="+data.cuReceiptTypeId;
									},
									error: function(data){
										console.log(data);
									}
								}); 
							}
							else
								{
								
								if(data.AuthStatus == "granted")
								{	
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
														buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
													}).dialog("open");
								}
								else
								{
									showDeniedPopup();
								}
								}
					  	},
			   			error:function(data){
			   				console.log('error');
			   				}
			   			});
								

					}
				}
			
			else
				{
				var payFromData = $('#addPaymentsFormID').serialize();
				var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
		   		$.ajax({
					url: "./checkAccountingCyclePeriods",
					data:{"datetoCheck":$("#paymentDateID").val(),"UserStatus":checkpermission},
					type: "POST",
					success: function(data) { 
						if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
						{

							periodid=data.cofiscalperiod.coFiscalPeriodId;
							yearid = data.cofiscalperiod.coFiscalYearId;
							
							payFromData = payFromData + "&periodid="+periodid+"&yearid="+yearid
							
						 	$.ajax({
								url:"./custpaymentslistcontroller/saveCustomerPayments",
								type: "POST",
								data: payFromData,
								success: function(data) {
									/* $('#addPaymentsFormID :input').val(" ");
									$('#addPaymentsDialog').dialog("close");
									$('#customerpaymentlist').trigger("reloadGrid"); */
				
									console.log(data);
								//	console.log("1. No"+"\n 2."+data.amount+"\n 3."+data.cuReceiptTypeId+"\n 4."+data.memo+"\n 5."+data.rxCustomerId+"\n 6."+data.reference);
								//	document.location.href = "./customerpayments?rxCustomerId="+data.rxCustomerId+"&customer="+$("#customerNamePayments").val()+"&checkNo="+data.reference+"&amount="+data.amount+"&recieptID="+data.cuReceiptId+"&Date="+$("#paymentDateID").val()+"&memo="+data.memo+"&recieptTypeId="+data.cuReceiptTypeId;
									document.location.href = "./customerpaymentsfromgrid?rxCustomerId="+data.rxCustomerId+"&customer="+encodeURIComponent($("#customerNamePayments").val())+"&checkNo="+encodeURIComponent(data.reference)+"&amount="+data.amount+"&recieptID="+data.cuReceiptId+"&Date="+$("#paymentDateID").val()+"&memo="+encodeURIComponent(data.memo)+"&recieptTypeId="+data.cuReceiptTypeId;
								},
								error: function(data){
									console.log(data);
								}
							}); 
						}
						else
							{
							if(data.AuthStatus == "granted")
							{	
							var newDialogDiv = jQuery(document.createElement('div'));
							jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
													buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
												}).dialog("open");
							}
							else
							{
								showDeniedPopup();
							}
							}
				  	},
		   			error:function(data){
		   				console.log('error');
		   				}
		   			});
				}
	}
	function deleteImgFormatter(cellvalue, options, rowObject){
			image = '<a href="#" onClick="deletePayment()"><img alt="search" src="./../resources/images/delete.png">';
			cellvalue = image;
			return cellvalue;
	}
	
	function deletePayment(){
		var grid = $('#customerpaymentlist');
		var rowid = $('#customerpaymentlist').jqGrid('getGridParam', 'selrow');
		var rxCustomerID = grid.jqGrid('getCell', rowid, 'rxMasterID');
		var cuRecieptID = grid.jqGrid('getCell', rowid, 'cuReceiptID');
		var customer = grid.jqGrid('getCell', rowid, 'customer');
		if(!rxCustomerID){
			msgOnAjax("Please select any row to roll Back.", "red", 2000);
			return false;
		}
		var div = jQuery(document.createElement('div'));
		$(div).html('<span style="color:red;"">Do you want to roll back the payment from <b>'+customer+'</b>?</span>');
		$(div).dialog({modal: true, width:300, height:150, title:"Warning",
			 buttons: {
				 "Yes": function(){
					 jQuery(this).dialog("close");	
					 $.ajax({
							url: "./custpaymentslistcontroller/deleteCustomerPayments",
							type: "POST",
							data: 'cuReceiptID='+cuRecieptID+'&rxMasterID='+rxCustomerID,
							success: function(data) { 
								msgOnAjax("Payment Successfully Deleted.", "Green", 2000);
								 $('#customerpaymentlist').trigger("reloadGrid");
							   	},
				   			error:function(data){
				   				console.log('error');
				   				}
				   			});
				 },
				 "No": function(){jQuery(this).dialog("close");	}
			 }}).dialog("open");
   		return true;
	}
	
	function searchcustomerpayments(){
		
		try{
		var searchTextvalue = $('#searchpaymentstext').val();
		var fromdate = $('#fromdate').val();
		var todate = $('#todate').val();
		
		var status = 'empty';
	if(fromdate!='' && todate!=''){
	
			status = 'both';
		}
		if(fromdate=='' && todate!=''){
			
			status = 'to';
		}
		if(fromdate!='' && todate==''){
			
			status = 'from';
		}
		
		
		jQuery("#customerpaymentlist").jqGrid('GridUnload');
		/* jQuery("#customerpaymentlist")
	    .jqGrid()
	    .setGridParam({
	        url : './custpaymentslistcontroller/searchCustomerPayments?searchText='+searchTextvalue+'&fromdate='+fromdate+'&todate='+todate+'&status='+status
	    })
	    .trigger("reloadGrid"); */
	    loadCutomerPaymentList('./custpaymentslistcontroller/searchCustomerPayments?searchText='+searchTextvalue+'&fromdate='+fromdate+'&todate='+todate+'&status='+status);
	    jQuery("#customerpaymentlist").trigger("reloadGrid");
	    
		//clearsearch();
		}catch(err){
			//alert(err.message);
		}
	}
	
	function showunappliedamount(){
		
		try{
		
		var fromdate = $('#fromdate').val();
		var todate = $('#todate').val();
		
		var status = 'empty';
	if(fromdate!='' && todate!=''){
	
			status = 'both';
		}
		if(fromdate=='' && todate!=''){
			
			status = 'to';
		}
		if(fromdate!='' && todate==''){
			
			status = 'from';
		}

		jQuery("#customerpaymentlist").jqGrid('GridUnload');
	/* 	jQuery("#customerpaymentlist")
	    .jqGrid()
	    .setGridParam({
	       
	        url : './custpaymentslistcontroller/showUnappliedAmount?fromdate='+fromdate+'&todate='+todate+'&status='+status
	    })
	    .trigger("reloadGrid"); */

		 loadCutomerPaymentList('./custpaymentslistcontroller/showUnappliedAmount?fromdate='+fromdate+'&todate='+todate+'&status='+status);
		 jQuery("#customerpaymentlist").trigger("reloadGrid");
	    
		
		jQuery("#customerpaymentlist").jqGrid('hideCol', jQuery("#customerpaymentlist").getGridParam("colModel")[6].name);
		jQuery("#customerpaymentlist").jqGrid('showCol', jQuery("#customerpaymentlist").getGridParam("colModel")[10].name);
		clearsearch();
	       // myGrid.jqGrid('showCol', myGrid.getGridParam("colModel")[colPos + 1].name);
		}catch(err){
		//alert(err.message);	
		}
	}
	function clearsearch(){
		$('#searchpaymentstext').val('');
		$('#fromdate').val('');
		$('#todate').val('');
		
	}
	/* $(function() { var cache = {}; var lastXhr='';
	$( "#searchpaymentstext" ).autocomplete({ minLength: 3,timeout :1000,sortResults: true,
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		select: function (event, ui) {
			$('#searchpaymentstext').val('');
			var id = ui.item.id;
			var rxCustomerID = ui.item.manufactureID;
			
			var dateval = new Date(ui.item.bidDate);
			var amount = ui.item.taxValue;
			var memo = ui.item.memo;
			var checkNo = ui.item.checkNo;
			var customer = ui.item.rolodexEntity;
			var recieptTypeId = ui.item.typeId;
			 dateval = (dateval.getMonth()+1)+'/'+dateval.getDate()+'/'+dateval.getFullYear();
			 
			//alert(':'+id+':'+rxCustomerID+' ::' +ui.item.rolodexEntity+'::'+dateval+' :: '+amount+':'+memo+' :: '+checkNo);
			document.location.href ="./customerpayments?rxCustomerId="+rxCustomerID+"&customer="+customer+"&checkNo="+checkNo+"&amount="+amount+"&recieptID="+id+"&Date="+dateval+"&memo="+memo+"&recieptTypeId="+recieptTypeId;
			
	      },
		source: function( request, response ) { var term = request.term;
			if(term in cache) {response(cache[term] ); return;}
			lastXhr = $.getJSON( "custpaymentslistcontroller/searchCustomerPayment", request, function(data, status, xhr){cache[term]=data; if(xhr===lastXhr){response(data);} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  });
	}); */
	
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
	function getcoFiscalPerliodDate(x)
	 {
		 $.ajax({
			 
		 		url: "./banking/getcoFiscalPeriod",
		 		type: "GET",
		 		success: function(data) {
		 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
		 					
		 		}		 
		 });	 
	 }



	

	
</script>
</body>
</html>