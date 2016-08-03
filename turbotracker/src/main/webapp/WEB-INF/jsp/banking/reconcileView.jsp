<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Banking Accounts</title>
		<style type="text/css">
			#mainmenuBankingPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuBankingPage a{background:url('./../resources/styles/turbo-css/images/home_white_banking.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuBankingPage ul li a{background: none; }
		</style>
		
	</head>
	<style>
	#search
	{
	display: none;
	}
	
	</style>
	
	<body>
		<div style="background-color: #FAFAFA">
		
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<div style="width:100%; margin:0 auto;">
			<center>
				<h2 style="font-family: Verdana,Arial,sans-serif;"><label>Reconcile Accounts</label></h2>
			</center>
			<center><h3><label>${moaccount.description}</label></h3></center>
			<table style="width:100%;position: absolute;top: 30%;left:5%;">
			<tr>
			<td>
			<input type="hidden" id="accounttypeid" value="${moaccount.moAccountId}"/>
			<input type="hidden" id="opeiningbalance" value="${openbalance}"/>
			<input type="hidden" id="endingbalance" value="${endbalance}"/>
			</td>
			</tr>
			<tr>
			<td width="40%"><div id="paymentgrid"></div>
			<table id="paymentgridtable" ></table>
			<div id="paymentgridpager"></div>
			<label style="font-weight:bold;" id="paymentdebitcount"></label><label style="font-weight:bold;padding-left: 20%;" id="paymentdebittotal"></label>
			</td>
			<td width="2%">&nbsp;</td>
			<td width="50%"><div id="depositgrid"></div>
			<table id="depositgridtable" ></table>
			<div id="depositgridpager"></div>
			<label style="font-weight:bold;" id="depositcount"></label><label style="font-weight:bold;padding-left:30%;" id="deposittotal" ></label>
			
			</td>
			
			</tr>
			<tr><td>
			<input type="button" id="markalldebits" class="cancelhoverbutton turbo-tan" value="Mark All" onclick="markalldebits(2)" style="width:90px;margin-top:25px;">
			
			</td>
			<td width="2%">&nbsp;</td>
			<td>
			<input type="button" id="markallcredits" class="cancelhoverbutton turbo-tan" value="Mark All" onclick="markalldebits(0)" style="width:90px;margin-top:25px;">
			
			</td>
			</tr>
			<tr>
			<td align="right" >
			<table><tr><td>Cleared Balance :</td><td><label id="clearbalanceid">0.000</label></td></tr>
			<tr><td>Statement Ending :</td><td><input type="text" id="statementendid"  value ="${endbalance}" style="border:none;color: #00377A;font-size:14.5px;"/></td></tr>
			<tr><td>Difference :</td><td><label id="differenceid" >0.000</label></td></tr>
			</table>
			</td>
			<td width="2%">&nbsp;</td>
			<td align="center">
				<input type="button" id="reconcileaccount" class="cancelhoverbutton turbo-tan" value="Finish Later" onclick="finishLater()" style="width:90px;margin-top:25px;">
				<input type="button" id="reconcilebutton" class="cancelhoverbutton turbo-tan" value="Reconcile Now" onclick="reconcileNow()" style="width:100px;margin-top:25px;">
			</td>
			</tr>
			</table>
		
			
			</div>
			<div style="padding-top: 22px;position: absolute;top: 170%;width: 100%;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<script>
		var clearedbalance = 0,debitTotal=0,creditsTotal=0,debitcount=0,creditcount=0,reccount;
		var clropening =$('#opeiningbalance').val();
		
		clearedbalance = clearedbalance+parseFloat(clropening);
		$('#statementendid').val(formatCurrency($('#endingbalance').val()));
		//alert(Number($('#endingbalance').val())+"-----"+-Number(clearedbalance))
		$('#differenceid').text(formatCurrency(Number(clearedbalance)-Number($('#endingbalance').val())))

		 
		var isnotsetvalue = true;
		
		
		function markalldebits(transactiontype){
			var checked = 1;
		
			 if(transactiontype==0){
				 var grid1 = $("#depositgridtable");
				 creditsTotal=0;
				 creditcount=0;
				 
				 if($('#markallcredits').val()=='Mark All'){
						checked = 1;
						$('#markallcredits').val('Unmark All');	

						
						grid1.jqGrid('resetSelection');
					    var ids = grid1.getDataIDs();
					    for (var i=0, il=ids.length; i < il; i++) {
					       // grid1.jqGrid('setSelection',ids[i], true);
					        grid1.find('#'+ids[i]+' input[type=checkbox]').prop('checked',true);
					    }
					    oncalculatetotalCredits();
					    
					}else{
						checked = 0;
						$('#markallcredits').val('Mark All');
						grid1.jqGrid('resetSelection');
					    var ids = grid1.getDataIDs();
					    for (var i=0, il=ids.length; i < il; i++) {
					    	  grid1.find('#'+ids[i]+' input[type=checkbox]').prop('checked',false);
					    }

					    oncalculatetotalCredits();
					}
				
				 
			 }else{
				 var grid = $("#paymentgridtable");

				 debitTotal = 0;
					debitcount=0;	 
				 
				 if($('#markalldebits').val()=='Mark All'){
						checked = 1;
						$('#markalldebits').val('Unmark All');	
						
						grid.jqGrid('resetSelection');
					    var ids = grid.getDataIDs();
					    for (var i=0, il=ids.length; i < il; i++) {
					      //  grid.jqGrid('setSelection',ids[i], true);
					    	  grid.find('#'+ids[i]+' input[type=checkbox]').prop('checked',true);
					    }

					    oncalculatetotal();
					    
					}else{
						checked = 0;
						$('#markalldebits').val('Mark All');
						grid.jqGrid('resetSelection');
					    var ids = grid.getDataIDs();
					    for (var i=0, il=ids.length; i < il; i++) {
					      //  grid.jqGrid('setSelection',ids[i], true);
					    	  grid.find('#'+ids[i]+' input[type=checkbox]').prop('checked',false);
					    }

					    oncalculatetotal();
					}
				 
					
					
			 }
			
			clearedbalance=0;
			clearedbalance = clearedbalance+parseFloat(clropening)+creditsTotal-debitTotal;
			var statementend = $('#statementendid').val().replace(/[^0-9\.]+/g,"");
			 
			 var ttl = creditsTotal-debitTotal;
			$('#differenceid').text( formatCurrency (ttl-statementend+parseFloat(clropening)));
			 
			console.log($('#markalldebits').val()+ " :: "+checked);
			    	  $.ajax({
							url: "banking/updateDebitsTransactionAll",
							type : 'POST',
							data: {moAccountID: $('#accounttypeid').val(),tempRec:checked,transactionType:transactiontype},
							success: function (data){
							
							}
							});
		}
	
		jQuery(document).ready(function() {
			
			onloadGrid();
			onloadDepositGrid();
			$("#reconcilebutton").hide();

			// on change event statement ending
			$('#statementendid').bind('blur keyup keypress',function(){
				var calcAmt = Number($("#clearbalanceid").text().replace(/[^0-9\.]+/g,"")) - Number($('#statementendid').val().replace(/[^0-9\.]+/g,"")) ;
				$('#differenceid').text(formatCurrency(calcAmt));
				
			});

			
			//If differce is zero  
			$('#differenceid').bind('DOMSubtreeModified', function(event) {
				var diffVal = $(this).text().replace(/[^0-9\.]+/g,"");
				if(diffVal == "0.00")
					{
					$("#differenceid").text("*** Reconcile Now ***");
					$("#reconcilebutton").show();
					} 
				else
					{
					$("#reconcilebutton").hide();
					}
		    })
		
	});
		$( "#reconcileendbalancedate" ).datepicker();
		function onloadGrid(){
			//clearedbalance=$('#opeiningbalance').val();
			try{
				grid = $("#paymentgridtable"),
				 getColumnIndex = function (columnName) {
		            var cm = $(this).jqGrid('getGridParam', 'colModel'), i, l = cm.length;
		            for (i = 0; i < l; i++) {
		                if ((cm[i].index || cm[i].name) === columnName) {
		                    return i; // return the colModel index
		                }
		            }
		            return -1;
		        };
			$("#paymentgridtable").trigger("reloadGrid");
			 $('#paymentgridtable').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#paymentgridpager'),
				url:'./banking/getReconcileTransactions',
				postData: {moAccountID : $('#accounttypeid').val(),transactionType:2},
				colNames:['Clr', 'Date','Reference','Payee','Amount', 'Transaction ID', 'CoAccountId'],
				colModel :[
							 {name: 'tempRec', index: 'tempRec', width: 20, align: 'center',formatter: 'checkbox',edittype:'checkbox', editoptions: { value: '1:0' },  formatoptions: { disabled: false }},
				           	{name:'transDate', index:'transDate', align:'left', width:40, hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
								cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
							{name:'reference', index:'reference', align:'center', width:40,hidden:false,  editoptions:{size:5, alignText:'left',maxlength:7}},
							{name:'description', index:'description', align:'center', width:60,hidden:false,  editoptions:{size:5, alignText:'left',maxlength:7},editrules:{required: true},formatter:currencyAddittionValueDebits},
							{name:'amount', index:'amount', align:'right', width:40,hidden:false,  formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
							{name:'moTransactionId', index:'moTransactionId', align:'right', width:5,hidden:true,  editoptions:{size:15, alignText:'right',maxlength:10}},
							{name:'coAccountID', index:'coAccountID', align:'right', width:5,hidden:true,  editoptions:{size:15, alignText:'right'}}
							/* {name:'rxMasterId', index:'rxMasterId', align:'right', width:5,hidden:true,  editoptions:{size:15, alignText:'right',maxlength:10}} */],
							
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'moTransactionId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height:570,	width: 580, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Payments and Checks(Debit)',
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
				loadComplete: function(data) {
					 var iCol = getColumnIndexByName ($(this), 'tempRec'), rows = this.rows, i, c = rows.length;
	                    for (i = 0; i < c; i += 1) {
	                    	
	                        $(rows[i].cells[iCol]).click(function (e) {
	                        	  var id = $(e.target).closest('tr')[0].id,
	                                isChecked = $(e.target).is(':checked');
	                        	   var dataFromCellByColumnIndex = jQuery('#paymentgridtable').jqGrid ('getCell', id, 6);	
	                        	   var checkvalue = (isChecked? '1': '0');
	                        	   oncalculatetotal();
	                        	  //alert(dataFromCellByColumnIndex+' is '+checkvalue);
	                        	  
	                        	  $.ajax({
										url: "banking/updateReconcileFL",
										type : 'POST',
										data: {moTransactionId:dataFromCellByColumnIndex,tempRec:checkvalue,moAccountID:$('#accounttypeid').val()},
										success: function (data){
										
										}
										});
	                        });
	                    }
	                    
	                    
	                    $("a.ui-jqdialog-titlebar-close").show();
					$("#paymentgridtable").setSelection(1, true);
					
					$("#paymentgridtable").trigger("reloadGrid");
					$("#Ack").trigger("reloadGrid");
				},
				gridComplete: function () {
					$(this).mouseover(function() {
				        var valId = $(".ui-state-hover").attr("id");
				        $(this).setSelection(valId, false);
				    });
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow: function(id){
					
				   },
				ondblClickRow: function(id){
					var lastSel = '';
//					var aCost = $("#lineItemGrid").jqGrid('getCell', id, 'unitCost');
				     if(id && id!==lastSel){ 
//					     $("#"+lastSel+"_unitCost").val(aCost).
				        jQuery(this).restoreRow(lastSel); 
				        lastSel=id; 
				     }
//				     $("#"+id+"_unitCost").val(aCost).replace(/[^0-9\.]+/g,"");
				     jQuery(this).editRow(id, true);
				},
				alertzIndex:1050,
				//editurl:"rolodexforms/updateReceiveInventory"
			}).navGrid('#paymentgridpager', {add:true, edit:true, del:false, search:false, view:true},
					//-----------------------edit options----------------------//
					{
				url:"banking/updateDebitsTransaction",
				width:515, left:400, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Debits",
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();
					jQuery('#TblGrid_paymentgridtable #tr_transDate .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_transDate .CaptionTD').append('Transaction Date: ');
					jQuery('#TblGrid_paymentgridtable #tr_reference .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_reference .CaptionTD').append('Cheque: ');
					jQuery('#TblGrid_paymentgridtable #tr_description .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_description .CaptionTD').append('Payee: ');
					jQuery('#TblGrid_paymentgridtable #tr_amount .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_amount .CaptionTD').append('Amount: ');
					$('#transDate').datepicker();
					var amount = $('#amount').val();
					amount=  parseFloat(amount.replace(/[^0-9-.]/g, ''));
					$('#amount').val(amount);
					
				},afterShowForm: function($form) {

					$(function() { var cache = {}; var lastXhr=''; $("input#description").autocomplete({minLength: 1,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "jobtabs3/rxMasterDetails", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){
							var ID = ui.item.id; var product = ui.item.label; $("#rxMasterId").val(ID);
							/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
							$.ajax({
						        url: './getRxMasterName?rxMasterId='+$("#rxMasterId").val(),
						        type: 'POST',       
						        success: function (data) {
						        	$.each(data, function(key, valueMap) {										
										
						        		if("lineItems"==key)
										{				
											$.each(valueMap, function(index, value){						
												$("#rxMasterId").val(value.rxMasterId);
												//alert(value.rxMasterId);
													
											}); 												
										}	
									});
																		
									
						        }
						    });
							},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
						}); 
					return;
					});
				},
				beforeSubmit:function(postdata, formid) {
					
					
					return [true, ""];
				},
				onclickSubmit: function(params){
					
					var checkname = $('#reference').val();
					var transDate = $('#transDate').val();
					var rxMasterId = $('#rxMasterId').val();
					var tempRec = $('#tempRec').val();
					var amount = $('#amount').val();
					if ($('#tempRec').is(":checked"))
					{
						tempRec = 1;
					}else{
						tempRec = 0;
					}
					var moTransactionId = $('#moTransactionId').val();
					
					//alert(checkname+ " : "+ transDate+" : "+rxMasterId+" : "+amount+" : "+tempRec+" : "+moTransactionId);
					return { 'moTransactionId' : moTransactionId, 'tempRec' : tempRec,'transDate': transDate,'rxMasterId':rxMasterId,'amount':amount};
				},
				afterSubmit:function(response,postData){
					 
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					 return [true, onloadGrid()];
				}
			},
					//-----------------------add options----------------------//
					{

				url:"banking/createDebitsTransaction",
				width:515, left:400, top: 300, zIndex:1040,
				closeAfterAdd:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				addCaption: "Add Debits",
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();
					jQuery('#TblGrid_paymentgridtable #tr_transDate .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_transDate .CaptionTD').append('Transaction Date: ');
					jQuery('#TblGrid_paymentgridtable #tr_reference .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_reference .CaptionTD').append('Cheque: ');
					jQuery('#TblGrid_paymentgridtable #tr_description .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_description .CaptionTD').append('Payee: ');
					jQuery('#TblGrid_paymentgridtable #tr_amount .CaptionTD').empty();
					jQuery('#TblGrid_paymentgridtable #tr_amount .CaptionTD').append('Amount: ');
					$('#transDate').datepicker();
					
					
				},afterShowForm: function($form) {

					$(function() { var cache = {}; var lastXhr=''; $("input#description").autocomplete({minLength: 1,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "jobtabs3/rxMasterDetails", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){
							var ID = ui.item.id; var product = ui.item.label; $("#rxMasterId").val(ID);
							/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
							$.ajax({
						        url: './getRxMasterName?rxMasterId='+$("#rxMasterId").val(),
						        type: 'POST',       
						        success: function (data) {
						        	$.each(data, function(key, valueMap) {										
										
						        		if("lineItems"==key)
										{				
											$.each(valueMap, function(index, value){						
												$("#rxMasterId").val(value.rxMasterId);
												//alert(value.rxMasterId);
													
											}); 												
										}	
									});
																		
									
						        }
						    });
							},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
						}); 
					return;
					});
				},
				beforeSubmit:function(postdata, formid) {
					
					
					return [true, ""];
				},
				onclickSubmit: function(params){
					
					var checkname = $('#reference').val();
					var transDate = $('#transDate').val();
					var rxMasterId = $('#rxMasterId').val();
					var tempRec = $('#tempRec').val();
					var amount = $('#amount').val();
					var description = $('#description').val();
					
					if ($('#tempRec').is(":checked"))
					{
						tempRec = 1;
					}else{
						tempRec = 0;
					}
					var moTransactionId = $('#moTransactionId').val();
					var accounttypeid = $('#accounttypeid').val();
					
					//alert(checkname+ " : "+ transDate+" : "+rxMasterId+" : "+amount+" : "+tempRec+" : "+moTransactionId);
					
					return { 'moTransactionId' : moTransactionId, 'tempRec' : tempRec,'transDate': transDate,'rxMasterId':rxMasterId,'amount':amount,'description':description,'transactionDate':transDate,'moAccountId':accounttypeid};
				},
				afterSubmit:function(response,postData){
					 
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					 return [true, onloadGrid()];
				}
			},
					//-----------------------Delete options----------------------//
					{
					},
					//-----------------------search options----------------------//
					{	});
			}catch(err){alert(err.message);}
			
		}
		
		
		function onloadDepositGrid(){
			try{
				grid = $("#depositgridtable"),
				 getColumnIndex = function (columnName) {
		            var cm = $(this).jqGrid('getGridParam', 'colModel'), i, l = cm.length;
		            for (i = 0; i < l; i++) {
		                if ((cm[i].index || cm[i].name) === columnName) {
		                    return i; // return the colModel index
		                }
		            }
		            return -1;
		        };
			$("#depositgridtable").trigger("reloadGrid");
			 $('#depositgridtable').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#depositgridpager'),
				url:'./banking/getReconcileTransactions',
				postData: {moAccountID : $('#accounttypeid').val(),transactionType:0},
				colNames:['Clr', 'Date','Reference','Payee','Amount', 'Transaction ID', 'CoAccountId'],
				colModel :[
							 {name: 'tempRec', index: 'tempRec', width: 20, align: 'center',formatter: 'checkbox', edittype:'checkbox',editoptions: { value: '1:0' },                   formatoptions: { disabled: false }},
				           	{name:'transDate', index:'transDate', align:'left', width:40, hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
								cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
							{name:'reference', index:'reference', align:'center', width:40,hidden:false,  formatter:currencyAddittionValueCredits},
							{name:'description', index:'description', align:'center', width:70,hidden:false, editable:false, editoptions:{size:5, alignText:'left',maxlength:7}},
							{name:'amount', index:'amount', align:'right', width:50,hidden:false,  formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'}},
							{name:'moTransactionId', index:'moTransactionId', align:'right', width:5,hidden:true,  editoptions:{size:15, alignText:'right',maxlength:10},  editrules:{required: true}},
							{name:'coAccountID', index:'coAccountID', align:'right', width:5,hidden:true,  editoptions:{size:15, alignText:'right'}, editrules:{number:true,required: true}}],
							
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'moTransactionId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height:570,	width: 580, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Deposits (Credits)',
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
				loadComplete: function(data) {
					 var iCol = getColumnIndexByName ($(this), 'tempRec'), rows = this.rows, i, c = rows.length;
	                    for (i = 0; i < c; i += 1) {
	                    	$(rows[i].cells[iCol]).click(function (e) {
	                        	  var id = $(e.target).closest('tr')[0].id,
	                                isChecked = $(e.target).is(':checked');
	                        	   var dataFromCellByColumnIndex = jQuery('#depositgridtable').jqGrid ('getCell', id, 6);	
	                        	   var checkvalue = (isChecked? '1': '0');
	                        	   oncalculatetotalCredits();
	                        	  //alert(dataFromCellByColumnIndex+' is '+checkvalue);
	                        	  
	                        	  $.ajax({
										url: "banking/updateReconcileFL",
										type : 'POST',
										data: {moTransactionId:dataFromCellByColumnIndex,tempRec:checkvalue,moAccountID:$('#accounttypeid').val()},
										success: function (data){
										
										}
										});
	                        });
	                    }
	               
					$("a.ui-jqdialog-titlebar-close").show();
					$("#depositgridtable").setSelection(1, true);
					
					$("#depositgridtable").trigger("reloadGrid");
					$("#Ack").trigger("reloadGrid");
				},
				gridComplete: function () {
					$(this).mouseover(function() {
				        var valId = $(".ui-state-hover").attr("id");
				        $(this).setSelection(valId, false);
				    });
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow: function(id){
					
				   },
				ondblClickRow: function(id){
					var lastSel = '';
//					var aCost = $("#lineItemGrid").jqGrid('getCell', id, 'unitCost');
				     if(id && id!==lastSel){ 
//					     $("#"+lastSel+"_unitCost").val(aCost).
				        jQuery(this).restoreRow(lastSel); 
				        lastSel=id; 
				     }
//				     $("#"+id+"_unitCost").val(aCost).replace(/[^0-9\.]+/g,"");
				     jQuery(this).editRow(id, true);
				},
				alertzIndex:1050,
				//editurl:"rolodexforms/updateReceiveInventory"
			}).navGrid('#depositgridpager', {add:false, edit:true, del:false, search:false, view:false},
					//-----------------------edit options----------------------//
					{

				url:"banking/updateDebitsTransaction",
				width:515, left:400, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Debits",
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();
					jQuery('#TblGrid_depositgridtable #tr_transDate .CaptionTD').empty();
					jQuery('#TblGrid_depositgridtable #tr_transDate .CaptionTD').append('Transaction Date: ');
					jQuery('#TblGrid_depositgridtable #tr_reference .CaptionTD').empty();
					jQuery('#TblGrid_depositgridtable #tr_reference .CaptionTD').append('Cheque: ');
					jQuery('#TblGrid_depositgridtable #tr_description .CaptionTD').empty();
					jQuery('#TblGrid_depositgridtable #tr_description .CaptionTD').append('Payee: ');
					jQuery('#TblGrid_depositgridtable #tr_amount .CaptionTD').empty();
					jQuery('#TblGrid_depositgridtable #tr_amount .CaptionTD').append('Amount: ');
					$('#transDate').datepicker();
					var amount = $('#amount').val();
					amount=  parseFloat(amount.replace(/[^0-9-.]/g, ''));
					$('#amount').val(amount);
					
				},afterShowForm: function($form) {

					$(function() { var cache = {}; var lastXhr=''; $("input#description").autocomplete({minLength: 1,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "jobtabs3/rxMasterDetails", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){
							var ID = ui.item.id; var product = ui.item.label; $("#rxMasterId").val(ID);
							/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
							$.ajax({
						        url: './getRxMasterName?rxMasterId='+$("#rxMasterId").val(),
						        type: 'POST',       
						        success: function (data) {
						        	$.each(data, function(key, valueMap) {										
										
						        		if("lineItems"==key)
										{				
											$.each(valueMap, function(index, value){						
												$("#rxMasterId").val(value.rxMasterId);
												//alert(value.rxMasterId);
													
											}); 												
										}	
									});
																		
									
						        }
						    });
							},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
						}); 
					return;
					});
				},
				beforeSubmit:function(postdata, formid) {
					
					
					return [true, ""];
				},
				onclickSubmit: function(params){
					
					var checkname = $('#reference').val();
					var transDate = $('#transDate').val();
					var rxMasterId = $('#rxMasterId').val();
					var tempRec = $('#tempRec').val();
					var amount = $('#amount').val();
					if ($('#tempRec').is(":checked"))
					{
						tempRec = 1;
					}else{
						tempRec = 0;
					}
					var moTransactionId = $('#moTransactionId').val();
					
					//alert(checkname+ " : "+ transDate+" : "+rxMasterId+" : "+amount+" : "+tempRec+" : "+moTransactionId);
					return { 'moTransactionId' : moTransactionId, 'tempRec' : tempRec,'transDate': transDate,'amount':amount};
				},
				afterSubmit:function(response,postData){
					 
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					 return [true, onloadDepositGrid()];
				}
			
					},
					//-----------------------add options----------------------//
					{
					},
					//-----------------------Delete options----------------------//
					{
					},
					//-----------------------search options----------------------//
					{	});
			}catch(err){alert(err.message);}
			
		}
		 getColumnIndexByName = function(grid, columnName) {
             var cm = grid.jqGrid('getGridParam', 'colModel'), i, l;
             for (i = 0, l = cm.length; i < l; i += 1) {
                 if (cm[i].name === columnName) {
                     return i; // return the index
                 }
             }
             return -1;
         }
		 function oncalculatetotal(){
			// alert($('#differenceid').text()+"======"+$('#clearbalanceid').text());
			 var rows = jQuery("#paymentgridtable").getDataIDs();
			 var count = 0;
			 var totalamount= 0;
			 for(a=0;a<rows.length;a++)
			 {
			    row=jQuery("#paymentgridtable").getRowData(rows[a]);
			    if(row['tempRec']=='1'){
			    	var number = parseFloat( row['amount'].replace(/[^0-9\.]+/g,""));
			    	totalamount = parseFloat(totalamount)+parseFloat(number);
			    	console.log(number+" :: "+row['tempRec']);
			    	count++;
			    }

			 }
			 $('#paymentdebitcount').text(count+'check,debit');
			 $('#paymentdebittotal').text(formatCurrency(totalamount.toFixed(2)));
			 debitTotal=totalamount.toFixed(2);
			 var statementend = $('#statementendid').val().replace(/[^0-9\.]+/g,"");
			 
			 var ttl =  parseFloat(creditsTotal)-parseFloat(debitTotal);
			 $('#differenceid').text( formatCurrency ((parseFloat(ttl)-parseFloat(statementend)+parseFloat(clropening)).toFixed(2)));
			 $('#clearbalanceid').text(formatCurrency((parseFloat(ttl)+parseFloat(clropening)).toFixed(2)));
			 
		 }
		function oncalculatetotalCredits(){
			 
			 var rows = jQuery("#depositgridtable").getDataIDs();
			 var count = 0;
			 var totalamount= 0.00;
			 for(a=0;a<rows.length;a++)
			 {
			    row=jQuery("#depositgridtable").getRowData(rows[a]);
			    if(row['tempRec']=='1'){
			    	var number = parseFloat(row['amount'].replace(/[^0-9\.-]+/g,""));
			    	totalamount = parseFloat(totalamount)+parseFloat(number);
			    	count++;
			    }
			 }
			
			 $('#depositcount').text(count+'credit(s)');
			 $('#deposittotal').text(formatCurrency(totalamount.toFixed(2)));
			 creditsTotal=totalamount.toFixed(2);
			 var statementend = $('#statementendid').val().replace(/[^0-9\.]+/g,"");
			 var ttl = parseFloat(creditsTotal)-parseFloat(debitTotal);
			$('#differenceid').text( formatCurrency ((parseFloat(ttl)-parseFloat(statementend)+parseFloat(clropening)).toFixed(2)));
			 $('#clearbalanceid').text(formatCurrency((parseFloat(ttl)+parseFloat(clropening)).toFixed(2)));
		 }
		function currencyAddittionValueCredits(cellValue, options, rowObject) {
			if(rowObject['tempRec']==1){
				try{
				creditcount++;
				creditsTotal= creditsTotal+rowObject['amount'];
				$('#deposittotal').text(formatCurrency(creditsTotal));
				clearedbalance = clearedbalance+rowObject['amount'];
				$('#clearbalanceid').text(formatCurrency(clearedbalance));
				var statementend = $('#statementendid').val().replace(/[^0-9\.]+/g,"");
				 
				$('#differenceid').text( formatCurrency(parseFloat(clearedbalance) - parseFloat(statementend)));
				
				if(creditcount == options.rowId){
					 $('#markallcredits').val('Unmark all');
				 }else{
					 $('#markallcredits').val('Mark all');
				 }
				
				$('#depositcount').text(creditcount+'credit(s)');
				}catch(err){alert(err.message);}
			}else{
				$('#deposittotal').text(formatCurrency(creditsTotal));
				$('#clearbalanceid').text(formatCurrency(clearedbalance));
				$('#depositcount').text(creditcount+'credit(s)');
			}
			return cellValue;
		}
		function currencyAddittionValueDebits(cellValue, options, rowObject) {
			if(rowObject['tempRec']=='1'){
				debitcount++;
				debitTotal = debitTotal+rowObject['amount'];
				$('#paymentdebittotal').text(formatCurrency(debitTotal));
				clearedbalance = clearedbalance-rowObject['amount'];
				 $('#clearbalanceid').text(formatCurrency(clearedbalance));
				 var statementend = $('#statementendid').val().replace(/[^0-9\.]+/g,"");
				 $('#differenceid').text( formatCurrency(parseFloat(clearedbalance) - parseFloat(statementend)));
				 $('#paymentdebitcount').text(debitcount+'check,debit');
				 
				 
				 var reccount1 = jQuery("#paymentgridtable").jqGrid('getGridParam', 'records');
				 var jqGridObj = $('#paymentgridtable');
				
				 console.log(debitcount+" :: "+ options.rowId+" : "+jqGridObj.getGridParam("reccount") );
				 if(debitcount == options.rowId){
					 $('#markalldebits').val('Unmark all');
				 }else{
					 $('#markalldebits').val('Mark all');
				 }
			}else{
				$('#paymentdebittotal').text(formatCurrency(debitTotal));
				 $('#clearbalanceid').text(formatCurrency(clearedbalance));
				 $('#paymentdebitcount').text(debitcount+'check,debit');
			}
			return cellValue;
		}
		
		function finishLater(){
			 var difference = $('#differenceid').text().replace(/[^0-9\.]+/g,"");
			 if(parseFloat(difference)!=0.00){
				 var information = "Would you like to finish later?";
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
											buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");
											  $.ajax({
													url: "banking/updateTempReconcile",
													type : 'POST',
													data: {moAccountID:$('#accounttypeid').val()},
													success: function (data){
														reloadTransaction();
													}
													});
											 }},
											 {height:35,text: "No",click: function() { $(this).dialog("close");cancelReconcilation()}},
											
											]}).dialog("open");
					return true;
			 }else{
				 reloadTransaction();
			 }
			
		}

		function cancelReconcilation()
		{
			 var information = "Are you sure to Cancel?";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");
										location.reload();
										 }},
										 {height:35,text: "No",click: function() { $(this).dialog("close");
										 $.ajax({
												url: "banking/updateTempReconcile",
												type : 'POST',
												data: {moAccountID:$('#accounttypeid').val()},
												success: function (data){
													reloadTransaction();
												}
												});
										 }},
										
										]}).dialog("open");

		}



		
		function reconcileNow(){

			var moAccountID= $('#accounttypeid').val();
			var closingBalance= $('#statementendid').val().replace(/[^0-9\.-]+/g,"");
			var openingBalance= $('#opeiningbalance').val();		

					  $.ajax({
							url: "banking/updateReconcile",
							type : 'POST',
							data: {moAccountId:moAccountID,closingBalance:closingBalance,openingBalance:openingBalance},
							success: function (data){
								document.location.href = "./bankingAccounts";
							}
							});

			
			}
		
		function reloadTransaction(){
			var aUserLoginID = $("#userLoginID").val();
			if(aUserLoginID !== '1'){
				if($("#bankingProcedureID1").val() === ''){
					var aInfo = true;
					if(aInfo){
						var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
						return true;
					}
				}else{
					document.location.href = "./bankingAccounts";
				}
			}else{
				document.location.href = "./bankingAccounts";
			}
		}



		
		</script>
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bankingAccounts.js"></script>
	</body>
</html>