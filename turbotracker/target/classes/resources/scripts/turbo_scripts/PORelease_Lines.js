var areaLineItem;
var selectedLineItem;
var addbutton=0;
var invoicedReceived=1;
var Purchase_lines_form;
var POlineItemgridLoad=true;
jQuery(document).ready(function() {
		$("#lineitemdate").hide();
			$(".datepicker").datepicker();
			if(aPurchaseOrderVar === "add"){
				$("#manufacture_ID").text(" ");
			}
			setTimeout("loadLineItemGrid();", 1);
			loadLineItemsPreData();
			$("#lineItemGrid").trigger("reload");
			$('#alertmod').css('z-index','1030');
			//$('#loadingPOlineDiv').css({"visibility": "hidden"});
			var manID= $("#manufacture_ID").text();
			if(manID==""){
				
				$("#importFromXML").prop('disabled', true);
				$('#importFromXML').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
			}else{
				checkImportTypeSelectornot(manID);
			}
			
			var jobStatus = getUrlVars()["jobStatus"];
			if(jobStatus.indexOf("Closed")>-1){
				$('#SaveLinesPOButton').css('cursor','default');
				$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				document.getElementById("SaveLinesPOButton").disabled = true;
				
			}else{
				document.getElementById("SaveLinesPOButton").disabled = false;
				$('#SaveLinesPOButton').css('cursor','pointer');
				$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			}
	});


function loadLineItemsPreData(){
	
	
//alert($("#totalGeneralId").val());
	$("#totalLineId").val("0");
	
	$("#vendorLineNameId").val($("#vendor_ID").text()); $("#ourPoLineId").val($("#order_ID").text()); $("#subtotalLineId").val($("#Subtotal_ID").text()); 
	 $("#taxLineId").val($("#Tax_ID").text());$("#freightLineId").val(formatCurrency($("#freightGeneralId").val()));
	$("#lineID").val($("#Percentage_ID").text());
	$("#poDateLineId").val($("#poDate_ID").text());
};

function downloadExcel() {
	 if($('#POtransactionStatus').val() == '-1'){
			errorText = "You can not Download, \nTransaction Status is 'Void' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
		}
	 else if($('#POtransactionStatus').val() == '0'){
		 errorText = "You can not Download, \nTransaction Status is 'Hold' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else if($('#POtransactionStatus').val() == '2'){
		 errorText = "You can not Download, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else{
	$.ajax({
		url: "./jobtabs5/jobReleaseLineItemExcel",
		type: "POST",
		data : {"vePoId": $("#vePO_ID").text()},
		success: function(data){
				var fileName = data[0];
				var fileLocation =data[1];
				window.location.href = "./jobtabs5/jobReleaseLineItemDownload?&fileName="+fileName+"&fileLocation="+fileLocation;
				},
		error:function(e){
				alert("failed"+e);
				console.log(e);
				}
	});
	return true;
	 }
}

//var field1, field2,
var myCustomCheck = function (value, colname) {
	if(value<0 ){
		//alert('inside');
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
		 return [false, "cost cant be negative"];
		 }
	else
		return [true];
};
/*var posit_job_lineGrid=0;
function loadLineItemGrid() {
	$("#lineItemGrid").trigger("reloadGrid");
	$("#lineGrid").empty();
	$("#lineGrid").append("<table id='lineItemGrid'></table><div id='lineItemPager'></div>");
	 $('#lineItemGrid').jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#lineItemPager'),
		url:'./jobtabs5/jobReleaseLineItem',
		//postData: {vePoId : function() { return $("#vePO_ID").text();}},
		postData: {vePoId :function(){
			var vePOID=$("#vePO_ID").text();
			if(vePOID==null || vePOID==''){
				vePOID=0;
				}
			return vePOID;
			} },
		colNames:["Product No","","Description","Qty",'Received','Invoiced',  'Cost Each', 'Mult.', 'Tax','Net Each','Billed', 'VepoID', 'prMasterID' , 'vePodetailID','Posiion', 'Move', 'TaxTotal','InlineNote'],
		colModel :[
			{name:'note',index:'note',align:'left',width:50,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				 dataInit: function (elem) {
					 	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
			            $(elem).autocomplete({
			                source: 'jobtabs3/productCodeWithNameList',
			                minLength: 1,
			                select: function (event, ui) {
			                	
			                	var id = ui.item.id;
			                	var product = ui.item.label;
			                	$("#"+aSelectedRowId+"_prMasterId").val(id);
			                	$("#new_row_prMasterId").val(id);
			                	
			                	var myGrid = $('#vePOID'),
			                    celValue = myGrid.val();
//			                	alert(celValue);
			                	
//			                	alert(" >>>>>>>> "+$("#new_row_vePoid").val()+ " || "+$("#"+aSelectedRowId+"_vePoid").val());
			                	//alert(id+" || "+product+" || "+aSelectedRowId+"_prMasterId = "+$("#"+aSelectedRowId+"_prMasterId").val()+" || new_row_prMasterId = "+$("#new_row_prMasterId").val());
			                	$.ajax({
							        url: './getLineItems?prMasterId='+id,
							        type: 'POST',       
							        success: function (data) {
							        	$.each(data, function(key, valueMap) {										
											
							        		if("lineItems"==key)
											{				
												$.each(valueMap, function(index, value){						
													
														$("#new_row_description").val(value.description);
														$("#"+aSelectedRowId+"_description").val(value.description);
														$("#new_row_unitCost").val(value.lastCost);
														$("#"+aSelectedRowId+"_unitCost").val(value.lastCost);
														$("#new_row_priceMultiplier").val(value.pomult);
														$("#"+aSelectedRowId+"_priceMultiplier").val(value.pomult);
														$("#new_row_sopopup").val(value.sopopup);
														$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
														$("#new_row_vePoid").val(celValue);
														$("#"+aSelectedRowId+"_vePoid").val(celValue);
														
														ID#383
														 * if(value.isTaxable == 1)
														{
															$("#new_row_taxable").prop("checked",true);
															$("#"+aSelectedRowId+"_taxable").prop("checked",true);
														}
														else
														{
															$("#new_row_taxable").prop("checked",false);
															$("#"+aSelectedRowId+"_taxable").prop("checked",false);
														}												
												});
												$("#new_row_description").focus();
												$("#"+aSelectedRowId+"_description").focus();
												
												$("#new_row_quantityOrdered").focus();
												$("#"+aSelectedRowId+"_quantityOrdered").focus();
											}							        		
										});
							        }
							    });
			                }
			            });
			      }
				}, editrules:{edithidden:true,required: true}},
			{name:'inLineNoteImage', index:'inLineNoteImage', align:'center', width:10,hidden:false,editable:false,formatter: inlineNoteImage},
			{name:'description', index:'description', align:'left', width:70, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7},editrules:{number:true,required: true}},
			{name:'quantityReceived', index:'quantityReceived', align:'right', width:30,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
			{name:'invoicedAmount', index:'invoicedAmount', align:'right', width:30,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
			{name:'unitCost', index:'unitCost', align:'right', width:30,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10}, formatter:customCurrencyFormatter, formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "$ "}, editrules:{custom: true, custom_func: myCustomCheck,required: true}},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:20,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
			{name:'netCast',index:'netCast',width:30 , align:'right',formatter:customCurrencyFormatter},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:true, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
			{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'posistion',index:'posistion',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
			{name:'upAndDown',index:'upAndDown',align:'left',width:30, formatter: upAndDownImage},
			{name:'taxTotal', index:'taxTotal', align:'center', width:20,hidden:true},
			{name:'inLineNote', index:'inLineNote', align:'center', width:20,hidden:true}],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 765, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
		loadBeforeSend: function(xhr) {
			posit_job_lineGrid= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
			//alert("hello");
		},
		loadComplete: function(data) {
			$("a.ui-jqdialog-titlebar-close").show();
//			$("#lineItemGrid").setSelection(1, true);
			var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
			var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'taxTotal');
			//setTimeout("loadLineItemsPreData()", 500);
			var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
			});
			$('#subtotalGeneralId').val(formatCurrency(sum));
			$('#subtotalLineId').val(formatCurrency(sum));
			$('#subtotalKnowledgeId').val(formatCurrency(sum));
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					//var taxValue = $('#taxLineId').val();
					var taxValue=$("#taxGeneralId").val();
					taxValue=taxValue.replace(".00", "");
					taxValue=taxValue.replace("%", "");
					taxAmount = taxAmount + Number(number6)*(Number(taxValue)/100);
				}
			});
			$('#generalID').val(formatCurrency(taxAmount));
			$('#lineID').val(formatCurrency(aSelectedPositionDetailID));
			$('#KnowledgeID').val(formatCurrency(taxAmount));
			var freightLineVal= $('#freightLineId').val();
			var number1 = freightLineVal.replace("$", "");
			var number2 = number1.replace(".00", "");
			var number3 = number2.replace(",", "");
			var number4 = number3.replace(",", "");
			var number5 = number4.replace(",", "");
			var number6 = number5.replace(",", "");
			
			
			
			//var frieghtvalue=Number($("#Freight_ID").text().replace(/[^0-9\.]+/g,""));
			var frieghtvalue=Number($("#freightGeneralId").val().replace(/[^0-9\.]+/g,""));
			$("#lineID").val(formatCurrency(taxAmount));
			$("#KnowledgeID").val(formatCurrency(taxAmount));
		//	aTotal = aTotal + sum + taxAmount + Number(number6)+frieghtvalue;
			aTotal = aTotal + sum + taxAmount+frieghtvalue;
			
			console.log("aTotal::"+aTotal+"::sum::"+sum+"::taxAmount::"+taxAmount+"::frieght::"+Number(number6)+"::frieghtvalue::"+frieghtvalue);
			
			$('#totalGeneralId').val(formatCurrency(aTotal));
			
			$('#totalLineId').val(formatCurrency(aTotal));
			$('#totalKnowledgeId').val(formatCurrency(aTotal));
			$("#lineItemGrid").trigger("reload");
			//$("#freightLineId").val(formatCurrency(freight));
			$("#freightLineId").val($("#freightGeneralId").val());
			var taxpercent=$("#taxGeneralId").val();
			//$("#taxLineId").val($("#Tax_ID").text());
			$("#taxLineId").val(taxpercent.replace("%", ""));
			var last = $(this).jqGrid('getGridParam','records');
			var hideDownIcon = Number(last)+1;
			var alignUpIcon = Number(last);
			if(last){
				$("#"+hideDownIcon+"_downIcon").css("display", "none");
				$("#"+alignUpIcon+"_upIcon").css({"position": "relative","left":"-9px","padding":"0px 12px "});
			}
			$("#lineItemGrid").trigger("reloadGrid");
			$("#Ack").trigger("reloadGrid");
			
			$('#lineItemGrid_inLineNoteImage').removeClass("ui-state-default ui-th-column ui-th-ltr");
			var ids = $('#lineItemGrid').jqGrid('getDataIDs');
		    if (ids) {
		    	console.log("if loop");
		        var sortName = $('#lineItemGrid').jqGrid('getGridParam','inLineNoteImage');
		        var sortOrder = $('#lineItemGrid').jqGrid('getGridParam','description');
		        for (var i=0;i<ids.length;i++) {
		        	console.log("if loop ::: "+ids[i]);
		        	$('#lineItemGrid').jqGrid('setCell', ids[i], 'inLineNoteImage', '', '',
		                        {style:'border-right-color: transparent !important;'});
		        	$('#lineItemGrid').jqGrid('setCell', ids[i], 'description', '', '',
	                        {style:'border-left-color: transparent !important;'});
//		        	alert(ids[i]);
//		        	$('#lineItemGrid').jqGrid('setCell', ids[i], 'netCast', '', '',
//	                        {style:'border-left-color: transparent !important;'});
		        }
		    }
			
		},
		beforeSaveCell:function(rowid, cellname, value, iRow, iCol){
			if(cellname=='unitCost')
				alert('before');
		},
		beforeSaveRow: function(options, rowid) { 
			alert('before');
			},
		gridComplete: function () {
		       jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_lineGrid);
		       posit_job_lineGrid=0;
			$(this).mouseover(function() {
		        var valId = $(".ui-state-hover").attr("id");
		        $(this).setSelection(valId, false);
		    });
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow: function(id){
			
			selectedLineItem = id;			
		   },
		   
//		   ondblClickRow: function(rowid) {
//			    $(this).jqGrid('editGridRow', rowid,
//			                        {recreateForm:true,closeAfterEdit:true,
//			                         closeOnEscape:true,reloadAfterSubmit:false});
//			},
		ondblClickRow: function(id){
//			$(this).jqGrid('editGridRow', id);
//			var lastSel = '';
////			var aCost = $("#lineItemGrid").jqGrid('getCell', id, 'unitCost');
//		     if(id && id!==lastSel){ 
////			     $("#"+lastSel+"_unitCost").val(aCost).
//		        jQuery(this).restoreRow(lastSel); 
//		        lastSel=id; 
//		     }
////		     $("#"+id+"_unitCost").val(aCost).replace(/[^0-9\.]+/g,"");
//		     
//		     if($('#POtransactionStatus').val() == '-1'){
//					errorText = "You can not Edit Line Items, \nTransaction Status is 'Void' \nChange Status to Open.";
//					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
//					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
//											buttons: [{height:35,text: "OK",click: function() {
//												$(this).dialog("close");
//												$("#cData").tigger('click');}}]}).dialog("open");
//				return false;
//				}
//			 else if($('#POtransactionStatus').val() == '0'){
//				 errorText = "You can not Edit Line Items, \nTransaction Status is 'Hold' \nChange Status to Open.";
//					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
//					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
//											buttons: [{height:35,text: "OK",click: function() {
//												$(this).dialog("close");
//												$("#cData").tigger('click');}}]}).dialog("open");
//				return false;
//			 }
//			 else if($('#POtransactionStatus').val() == '2'){
//				 errorText = "You can not Edit Line Items, \nTransaction Status is 'Close' \nChange Status to Open.";
//					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
//					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
//											buttons: [{height:35,text: "OK",click: function() {
//												$(this).dialog("close");
//												$("#cData").tigger('click');}}]}).dialog("open");
//				return false;
//			 }
//			 else{
//			// Commented By Jenith on 15-09-2014
//		     //jQuery(this).editRow(id, true);
//			 }
		},
		editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
	}).navGrid("#lineItemPager", {
		add : false,
		edit : false,
		del : true,
		alertzIndex : 10000,
		search : false,
		refresh : false,
		pager : true,
		alertcap : "Warning",
		alerttext : 'Please select a Product'
	},
	// -----------------------edit// options----------------------//
	{},
	// -----------------------add options----------------------//
	{},
	// -----------------------Delete options----------------------//
	{
		closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
		caption: "Delete Product",
		msg: 'Delete the Product Record?',
		beforeInitData : function(formid) {
			 if($('#POtransactionStatus').val() == '-1'){
					errorText = "You can not Delete Line Items, \nTransaction Status is 'Void' \nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() {
												$(this).dialog("close");
												$("#cData").tigger('click');}}]}).dialog("open");
				return false;
				}
			 else if($('#POtransactionStatus').val() == '0'){
				 errorText = "You can not Delete Line Items, \nTransaction Status is 'Hold' \nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() {
												$(this).dialog("close");
												$("#cData").tigger('click');}}]}).dialog("open");
				return false;
			 }
			 else if($('#POtransactionStatus').val() == '2'){
				 errorText = "You can not Delete Line Items, \nTransaction Status is 'Close' \nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() {
												$(this).dialog("close");
												$("#cData").tigger('click');}}]}).dialog("open");
				return false;
			 }
			 else{
			 return true;
			 }
		 },
		onclickSubmit: function(params){
			var id = jQuery("#lineItemGrid").jqGrid('getGridParam','selrow');
			var key = jQuery("#lineItemGrid").getCell(id, 14);
			var aTaxValue = $('#taxLineId').val();
			return { 'vePodetailId' : key, 'operForAck' : '' ,'taxValue' : aTaxValue};			
		}
	});
	 $("#lineItemGrid").jqGrid(
				'inlineNav',
				'#lineItemPager',
				{
					edit : true,
					add : true,
					zIndex : 10000,
					refresh : false,
					cloneToTop : true,
					alertzIndex : 10000,
					
					addParams : {
						position: "last",
						addRowParams : {
							keys : false,
							oneditfunc : function() {
								console.log("edited");
								 $("#del_lineItemGrid").addClass('ui-state-disabled');
							},
							successfunc : function(response) {
								return true;
							},
							aftersavefunc : function(response) {
							},
							errorfunc : function(rowid, response) {
								console.log('An Error');
								$("#info_dialog").css("z-index", "10000");
								$(".jqmID1").css("z-index", "10000");
								return false;
							},
							afterrestorefunc : function(rowid) {
								console.log("afterrestorefunc");
							}
						}
					},
					editParams : {
						keys : false,
						successfunc : function(response) {
							var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
							//alert("aSelectedRowId = "+aSelectedRowId);
		                	$("#"+aSelectedRowId+"_prMasterId").val();
		                	var q = $("#"+aSelectedRowId+"_quantityOrdered").val();
		                	var u = $("#"+aSelectedRowId+"_unitCost").val();
		                	var p = $("#"+aSelectedRowId+"_priceMultiplier").val();
		                	console.log(">>>>> inside edit param = ");
		                	
		                	//alert(q+" || "+u+" || "+p);
		                	if(p==0){
		                		p=1;
		                	}
		                	if(u<0)
		                		u=-(u)
		                	var t = (q*u)*p;
		                	
		                	console.log(">>>>> t= "+t);
//		                	alert(t);
//		                	$("#"+aSelectedRowId+"_netCast").val(t);
//		                	$("#new_row_netCast").val(t);
//							 alert("success"+$("#new_row_netCast").val()+ " || "+$("#"+aSelectedRowId+"_netCast").val());
							 var rowData = $('#lineItemGrid').jqGrid('getRowData', aSelectedRowId);
					            rowData.netCast = t;
					            $('#lineItemGrid').jqGrid('setRowData', aSelectedRowId, rowData);
					            $("#lineItemGrid").trigger("reload");
					            var tt = 0;
					            var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
								$.each(allRowsInGrid, function(index, value) {
									var ts = value.netCast.replace("$","").replace(",","");
									console.log(">>>>> ts = "+ts);
									tt = tt+parseFloat(ts);
								});
								
								var val = (parseFloat(tt) * 8.25)/100;
								console.log("val = "+val);
								$("#lineID").val(formatCurrency(val.toFixed(2)));
								console.log(" lineId val = "+$("#lineID").val());
								$("#subtotalLineId").val(formatCurrency(tt.toFixed(2)));
								console.log(" subtotal lineId val = "+$("#subtotalLineId").val());
								var finalvl = formatCurrency( (tt+val).toFixed(2) );
								console.log(" finalVal = "+finalvl);
					            $("#totalLineId").val(finalvl);
					            $(this).trigger("reloadGrid");
					            $("#del_lineItemGrid").removeClass('ui-state-disabled');
							return true;
						},
						aftersavefunc : function(id) {
							$("#del_lineItemGrid").removeClass('ui-state-disabled');
							console.log('afterSavefunc editparams');
							$("#lineItemGrid").trigger("reloadGrid");
						},
						errorfunc : function(rowid, response) {
							console.log(' editParams -->>>> An Error');
							$("#info_dialog").css("z-index", "10000");
							$(".jqmID1").css("z-index", "10000");
							$("#del_lineItemGrid").removeClass('ui-state-disabled');
							return false;

						},
						afterrestorefunc : function( id ) {
							$("#del_lineItemGrid").removeClass('ui-state-disabled');
							$(this).trigger("reloadGrid");
					    },
						// oneditfunc: setFareDefaults
						oneditfunc : function(id) {
							console.log('OnEditfunc');
							$("#del_lineItemGrid").removeClass('ui-state-disabled');
							var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
		                	var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
		                	 q = q.replace(",", "");
		                	var u = $("#"+aSelectedRowId+"_unitCost").val().replace("$", "");
		                	 u = u.replace(",", "");
		                	var p = $("#"+aSelectedRowId+"_priceMultiplier").val().replace("$", "");
		                	  p = p.replace(",", "");
		                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
		                	$("#"+aSelectedRowId+"_unitCost").val(u);
		                	$("#"+aSelectedRowId+"_priceMultiplier").val(p);
						}
					}
				});
//	 $.extend($.jgrid.jqModal, {zIndex: 1234});
//	 $.extend($.jgrid.nav, {alertzIndex: 1234});
//	 $($("##lineItemPager_left table tbody")[0]).find('tr').find(".ui-pg-button ui-state-disabled").hide();
	 var custombuttonthereornot=document.getElementById("copyrowcustombutton");
	 if(custombuttonthereornot == null){
		 $('#lineItemGrid').jqGrid('navButtonAdd',"#lineItemPager",{ caption:"",id:"copyrowcustombutton", buttonicon:"ui-icon-newwin", onClickButton:copyOption, position: "last", title:"Copy Row", cursor: "pointer"});
	 }
	 $("#lineItemGrid").jqGrid('navButtonAdd',"#lineItemPager",{ caption:"", buttonicon:"ui-icon-document", onClickButton:inlineItem, position: "after", title:"Inline Note", cursor: "pointer"});
	 $("#lineItemGrid").jqGrid('navButtonAdd',"#lineItemPager",{ caption:"", buttonicon:"ui-icon-bookmark", onClickButton:showInvoicedReceived, position: "after", title:"Show Received/Invoiced", cursor: "pointer"});
}*/

function showInvoicedReceived(){
	// jQuery("#lineItemGrid").jqGrid('hideCol', jQuery("#gridid").getGridParam("colModel")[6].name);
	// jQuery("#lineItemGrid").jqGrid('showCol', jQuery("#gridid").getGridParam("colModel")[6].name);
	//$("#lineItemGrid").trigger("reloadGrid");
	
	if(invoicedReceived==1){
		jQuery("#lineItemGrid").jqGrid('showCol', jQuery("#lineItemGrid").getGridParam("colModel")[7].name);
		jQuery("#lineItemGrid").jqGrid('showCol', jQuery("#lineItemGrid").getGridParam("colModel")[8].name);
		$('#lineItemGrid').setGridWidth(845, true);
		invoicedReceived=0;
		}
	else
		{
		jQuery("#lineItemGrid").jqGrid('hideCol', jQuery("#lineItemGrid").getGridParam("colModel")[7].name);
		jQuery("#lineItemGrid").jqGrid('hideCol', jQuery("#lineItemGrid").getGridParam("colModel")[8].name);
		$('#lineItemGrid').setGridWidth(765, true);
		invoicedReceived=1;
		}
	
	/*  var jsonList1='';
	 var selectedRow=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
	                        var rows = jQuery("#lineItemGrid").getDataIDs();
	                        for(a=0;a<rows.length;a++)
	                        {
	                                        row=jQuery("#lineItemGrid").getRowData(rows[a]);
	                                        var notes_test = row['inLineNote'];
	                                        if(a==selectedRow){
	                                        notes_test  = textboxvalue;
	                                        }
	                                        if(a == 0)
	                                         jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
	                                else
	                                        jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
	                                        
	                        }
	                        $("#lineItemGrid").trigger("reloadGrid"); */
}

/*function copyOption(){
		 if($('#POtransactionStatus').val() == '-1'){
				errorText = "You can not Edit Line Items, \nTransaction Status is 'Void' \nChange Status to Open.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() {
											$(this).dialog("close");
											$("#cData").tigger('click');}}]}).dialog("open");
			return false;
			}
		 else if($('#POtransactionStatus').val() == '0'){
			 errorText = "You can not Edit Line Items, \nTransaction Status is 'Hold' \nChange Status to Open.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() {
											$(this).dialog("close");
											$("#cData").tigger('click');}}]}).dialog("open");
			return false;
		 }
		 else if($('#POtransactionStatus').val() == '2'){
			 errorText = "You can not Edit Line Items, \nTransaction Status is 'Close' \nChange Status to Open.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() {
											$(this).dialog("close");
											$("#cData").tigger('click');}}]}).dialog("open");
			return false;
		 }
		 else{
	
	if(selectedLineItem != undefined){
		try{
		var rows = jQuery("#lineItemGrid").getDataIDs();
		row=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
		var vepodetailid = row['vePodetailId'];
		
		$.ajax({
			url: "./jobtabs5/copyPoLineItem",
			type: "GET",
			data : {vepoDetailId:vepodetailid,vePOId:''},
			success: function(data) {
				loadLineItemGrid();
				
			}
		});
		selectedLineItem = '';
		}catch(err){
			alert(err.message);
		}
	}else{
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:red;">Please Select a Row</b></span>');
									jQuery(newDialogDiv).dialog(
											{
												modal : true,
												width : 300,
												height : 150,
												title : "Warning",
												buttons : [ {
													height : 35,
													text : "OK",
													click : function() {
														$(this).dialog("close");
													}
												} ]
											}).dialog("open");
	}
	return true;
}
}*/

function upAndDownImage(cellValue, options, rowObject){
	var element = "<div>";
	var upIconID = options.rowId;
	var downID = options.rowId;
	var downIconID = Number(downID)+1;
	if(options.rowId === '1'){
		element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px 13px;vertical-align: middle;position: relative;left: 9px;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		//element += "<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='vertical-align: middle;'></a>";
		element += "</div>";
	}else {
		element +=	"<a id='"+upIconID+"_upIcon' onclick='upPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/upArrowLineItem.png' title='Move Up & Save'></a>";
		element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		//element += 	"<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px;vertical-align: middle;'></a>";
		element += "</div>";
	}
	return element;
}

function upPOLineItem() {
	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId === null){
		var errorText = "Please select a line item to move to up or down.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'posistion');
	var aSelectedPODetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePodetailId');
	var aSelectedVePOID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePoid');
	var aAbovePositionRowID = Number(aSelectedRowId) - 1;
	var aAbovePODetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'vePodetailId');
	var aAbovePositionDetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'posistion');
	var aUpLineItem = 'upLineItem';
	//updatePOLineItemPosition(aSelectedRowId, aSelectedPositionDetailID, aSelectedPODetailID, aSelectedVePOID, aAbovePositionRowID, aAbovePositionDetailID, aAbovePODetailID, aUpLineItem);
	return false;
}

function downPOLineItem() {
	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId === null){
		var errorText = "Please select a line item to move to up or down.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'posistion');
	var aSelectedPODetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePodetailId');
	var aSelectedVePOID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePoid');
	var aAbovePositionRowID = Number(aSelectedRowId) + 1;
	var aAbovePositionDetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'posistion');
	var aAbovePODetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'vePodetailId');
	var aUpLineItem = 'downLineItem';
	//updatePOLineItemPosition(aSelectedRowId, aSelectedPositionDetailID, aSelectedPODetailID, aSelectedVePOID, aAbovePositionRowID, aAbovePositionDetailID, aAbovePODetailID, aUpLineItem);
	return false;
}

function inlineNoteImage(cellValue, options, rowObject){
	var element = '';
	if(cellValue !== '' && cellValue !== null && cellValue != undefined){
		element = "<img src='./../resources/images/lineItem_new.png' title='Line Items' style='vertical-align: middle;'>";
	   return element;
	}else 
		return element;
} 

function updatePOLineItemPosition(aSelectedRowId, aSelectedPositionDetailID, aSelectedPODetailID, aSelectedVePOID, aAbovePositionRowID, aAbovePositionDetailID, aAbovePODetailID, aUpLineItem){
	$.ajax({
		url: "./jobtabs5/updatePOLineItemPosition",
		type: "POST",
		data : {'selectedRowID' : aSelectedRowId, 'selectedPositionDetailID' : aSelectedPositionDetailID , 'selectedQuoteDetailID' : aSelectedPODetailID, 'selectedJoQuoteHeaderID' : aSelectedVePOID, 'abovePositionRowID' : aAbovePositionRowID,
											'abovePositionDetailID' : aAbovePositionDetailID, 'aboveQuoteDetailID' : aAbovePODetailID, 'oper' : aUpLineItem},
		success: function(data) {
			$("#lineItemGrid").jqGrid('GridUnload');
			loadLineItemGrid();
			$("#lineItemGrid").trigger("reloadGrid");
			/*var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");*/
		}
	});
	return false;
}

function inlineItem(){
	if($('#POtransactionStatus').val() == '-1'){
		 var newDialogDiv = jQuery(document.createElement('div'));
			errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Void' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
		}
	 else if($('#POtransactionStatus').val() == '0'){
		 var newDialogDiv = jQuery(document.createElement('div'));
		 errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Hold' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	/* else if($('#POtransactionStatus').val() == '2'){
		 var newDialogDiv = jQuery(document.createElement('div'));
		 errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }*/
	 else{
	var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select a line item to add Line Note";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var lineItemText = $("#lineItemGrid").jqGrid('getCell', rowId, 'inLineNote');
	var JopInlinetext =lineItemText.replace("&And", "'");
	//areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('inlineItemId');
	//$(".nicEdit-main").empty();
	$(".nicEdit-main").append(JopInlinetext);
	
	if($('#POtransactionStatus').val()=='2'){
		$("#PoSaveInLineItemID").css("display", "none");
	}else{
		$("#PoSaveInLineItemID").css("display", "inline-block");
	}
	
	jQuery("#POInLineItem").dialog("open");
	$(".nicEdit-main").focus();
	return true;
}
}

function POLineItemInfo(){
	var inlineText= $('#POInLineItemID').find('.nicEdit-main').html();
	var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	var vePodetailId = $("#lineItemGrid").jqGrid('getCell', rowId, 'vePodetailId');
	var aLineItem = new Array();
	aLineItem.push(inlineText);
	aLineItem.push(vePodetailId);
	$.ajax({
		url: "./jobtabs5/SavePOlinetextInfo",
		type: "POST",
		data : {'lineItem' : aLineItem},
		success: function(data){
		//	areaLine.removeInstance('inlineItemId');
			jQuery("#POInLineItem").dialog("close");
			$("#lineItemGrid").trigger("reloadGrid");
		}
	});
}

function POCancelInLineNote(){
	//areaLine.removeInstance('inlineItemId');
	jQuery("#POInLineItem").dialog("close");
	CKEDITOR.instances['inlineItemId'].destroy();
	return false;
}

jQuery(function(){
	jQuery("#POInLineItem").dialog({
			autoOpen : false,
			modal : true,
			title:"InLine Note",
			height: 385,
			width: 625,	
			buttons : {  },
			close:function(){
				return true;
			}	
	});
});

function lineitem(){
	if ($('#lineitemcheck').is(':checked')) { 
	 	$("#lineitemdate").show();
 	} else {
	 	 $("#lineitemdate").hide();
 	} 
}

var savePOLineItems = function(){
	

	 var aVePOID = $("#vePO_ID").text();
	 var aPOLineItemsSubtotal = $("#subtotalLineId").val();
	 var errorText = "Line Items are Saved.";
	 jQuery(newDialogDiv).attr("id","msgDlg");
	 jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	 if(aPOLineItemsSubtotal.replace(/[^0-9\.]+/g,"") <= 0.00){
		 jQuery( "#porelease" ).dialog("close");
		 return false;
	 }
	 $.ajax({
			url: "./jobtabs5/savepolineitemssubtotal",
			type: "POST",
			data: {'aPOLineItemsSubtotal':aPOLineItemsSubtotal.replace(/[^0-9\.]+/g,""), 'aVePOID':aVePOID},
			success: function(data) {
				if(data){
					if($('#Buttonchoosed').is(':checked')){
						$('#ShowInfo').html("Your PO Release Line items are saved successfully.");
						/*setTimeout(function(){$('div#ShowInfo').text("");},2000);*/
						$('#Buttonchoosed').prop('checked', false);
						savePo();
					}else{
						$( "#note" ).autocomplete("destroy");
						$(".ui-menu-item").hide();
						savePORelease();
						jQuery( "#porelease" ).dialog("close");
						$("#release").trigger("reloadGrid");
					}
						return true;
					}			
				}
		});		
};
var showUploadForm = function(){
	 if($('#POtransactionStatus').val() == '-1'){
			errorText = "You can not Import any data , \nTransaction Status is 'Void' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
		}
	 else if($('#POtransactionStatus').val() == '0'){
		 errorText = "You can not Import any data, \nTransaction Status is 'Hold' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else if($('#POtransactionStatus').val() == '2'){
		 errorText = "You can not Inport any data, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else{
		 $("#uploadExcel_Form").slideDown();
	 }
	
};

var showXmlUploadForm = function(){
	 if($('#POtransactionStatus').val() == '-1'){
			errorText = "You can not Import any data , \nTransaction Status is 'Void' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
		}
	 else if($('#POtransactionStatus').val() == '0'){
		 errorText = "You can not Import any data, \nTransaction Status is 'Hold' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else if($('#POtransactionStatus').val() == '2'){
		 errorText = "You can not Inport any data, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else{
		 $("#uploadXml_Form").slideDown();
	 }
	
};

function enableTaxAllRecords(){
	var aTaxValue = $('#taxLineId').val();
	
	$.ajax({
			url: "./jobtabs5/selectAllTaxes",
			type: "POST",
			data : {"vePoId": $("#vePO_ID").text(),"aTaxValue":aTaxValue},
			success: function(data){
				$("#refresh_lineItemGrid").trigger("click");
				},
			error:function(e){
					alert("failed"+e);
					console.log(e);
				}
	});
	return true;
}
function uploadJqueryForm(){
	var vepoId = $("#vePO_ID").text();
	$("#vepoId").val(vepoId);
	$('#result').html('');
	/** blocking the UI**/
	$.blockUI({ css: { 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		'z-index':'1010px',
		color: '#fff'
	} }); 
	/************/
	$("#form2").ajaxForm({
		url: './fileupload/excelupload',
		success:function(data) {
			$('#result').html('<span><b style="color:green;">'+data+'</b></span>');
			 setTimeout(function () {$('#result').html(""); }, 1000);
			//$('#result').show();
			$("#lineItemGrid").trigger("reloadGrid");
			$("#uploadExcel_Form").slideUp('slow');
			setTimeout($.unblockUI, 2000);
		},
		error: function(jqXhr, textStatus, errorThrown){
			$("#uploadExcel_Form").slideUp('slow');
			setTimeout($.unblockUI, 2000);
			var errorText = $(jqXhr.responseText).find('u').html();
			$('#result').html('<span><b style="color:red;">'+errorText+'</b></span>');
			 setTimeout(function () {$('#result').html(""); }, 1000);
			//$('#result').show();
		},
		dataType:"text"
	}).submit();
}
function cancelFormSubmit() {
	$("#uploadExcel_Form").slideUp('slow');
}
function cancelXmlFormSubmit() {
	$("#uploadXml_Form").slideUp('slow');
}
function savexmlfunction(){
	if($("#xmlfileid").val()==null || $("#xmlfileid").val()==""){
		$('#result1').html('<span><b style="color:red;">Choose a file.</b></span>');
		setTimeout(function () {$('#result1').html(""); }, 1000);
		
	return false;
	}
	var ext = $('#xmlfileid').val().split('.').pop().toLowerCase();
	if($.inArray(ext, ['xml']) == -1) {
		$('#result1').html('<span><b style="color:red;">You Should select xml file.</b></span>');
		setTimeout(function () {$('#result1').html(""); }, 1000);
	    return false;
	}
	
	var vepoId = $("#vePO_ID").text();
	var manID= $("#manufacture_ID").text();
	$("#MANId1").val(manID);
	$("#vepoId1").val(vepoId);
	$('#result1').html('');
	//** blocking the UI**//*
	$.blockUI({ css: { 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		'z-index':'1010px',
		color: '#fff'
	} }); 
	//************//*
	$("#XmlUploadID").ajaxForm({
		url: './jobtabs5/checkxmlparser',
		success:function(data) {
			console.log(data.length);
			var result=data[data.length-1].description;
			console.log(result);
			var resultData=new Array();
			resultData=data;
			$('#result1').html("<span>"+result+"</span>");
			//$('#result1').show();
			setTimeout(function () {$('#result1').html(""); }, 5000);
			var gridRows = $('#lineItemGrid').getRowData();
			var xmlData=new Array();
			for(var k=0;k<resultData.length;k++){
				if(k<resultData.length-1){
					xmlData.push(resultData[k]);
				}
			}
			var dataToSend = JSON.stringify(gridRows);
			var xmldataToSend=JSON.stringify(xmlData);
			POlineItemgridLoad=false;
			jQuery("#lineItemGrid").jqGrid('setGridParam',{url:"./vendorscontroller/XMLUploadData",postData:{"gridData":dataToSend,"XmlData":xmldataToSend}}).trigger("reloadGrid");
			//$("#lineItemGrid").trigger("reloadGrid");
			$("#uploadXml_Form").slideUp('slow');
			setTimeout($.unblockUI, 4000);
			$("#xmlfileid").val("");
		},
		error: function(jqXhr, textStatus, errorThrown){
			$("#uploadXml_Form").slideUp('slow');
			setTimeout($.unblockUI, 2000);
			var errorText = $(jqXhr.responseText).find('u').html();
			$('#result1').html('<span><b style="color:red;">'+errorText+'</b></span>');
			setTimeout(function () {$('#result1').html(""); }, 4000);
			$("#xmlfileid").val("");
			//$('#result1').show();
		}
	}).submit();

/*var aVepoId=$('#vePO_ID').val();

	$.ajax({
		url: "./jobtabs5/checkxmlparser",
		type: "POST",
		data : {"aVepoId": $("#vePO_ID").text()},
		success: function(data){
		$("#lineItemGrid").trigger("reloadGrid");
		$("#uploadXml_Form").slideUp('slow');
			},
		error:function(e){
				alert("failed"+e);
				console.log(e);
			}
});*/

}
Array.prototype.remove = function(val) {
    var i = this.indexOf(val);
         return i>-1 ? this.splice(i, 1) : [];
  };
function checkImportTypeSelectornot(manufactureid){
	if(manufactureid==""||manufactureid==null){
		manufactureid=0;
	}
	$.ajax({
		url: "./jobtabs5/checkImportTypeSelectornot",
		type: "POST",
		data : {"manufactureid": manufactureid},
		success: function(data){
			
			if(data==true){
				$("#importFromXML").prop('disabled', true);
				$('#importFromXML').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
			}else{
				$("#importFromXML").prop('disabled', false);
				$('#importFromXML').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			}
			
			},
		error:function(e){
				
			}
});
}



//Changes Method
function loadLineItemGrid() {
	
	var schgrid='<table id="lineItemGrid"></table><div id="lineItemPager"></div>';	
	$('#jqgridLine').empty();
	$('#jqgridLine').append(schgrid);
	grid = $("#lineItemGrid"),
    getColumnIndex = function (columnName) {
        var cm = $(this).jqGrid('getGridParam', 'colModel'), i, l = cm.length;
        for (i = 0; i < l; i++) {
            if ((cm[i].index || cm[i].name) === columnName) {
                return i; 
            }
        }
        return -1;
    };
	 $('#lineItemGrid').jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		url:'./jobtabs5/jobReleaseLineItem',
		postData: {vePoId :function(){
			var vePOID=$("#vePO_ID").text();
			if(vePOID==null || vePOID==''){
				vePOID=0;
				}
			return vePOID;
			} },
		pager: jQuery('#lineItemPager'),
		colNames:["Product No","", "Description1","Notes",'NotesDesc','Qty','Received','Invoiced', 'Cost Each', 'Mult.', 'Tax','Net Each', 'Amount', 'VepoID', 'prMasterID' , 'vePodetailID','Posiion', 'Move', 'TaxTotal', 'Ack.','Ship','Order #','InLineNote','<img src="./../resources/images/delete.png" style="vertical-align: middle;">','',''],
		colModel :[
			{name:'note',index:'note',align:'left',width:60,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				dataInit: function (elem) {
		            $(elem).autocomplete({
		                source: 'jobtabs3/productCodeWithNameList',
		                minLength: 1,
		                select: function (event, ui) {
		                	var IncTaxOnPOAndInvoices=getSysvariableStatusBasedOnVariableName('IncTaxOnPOAndInvoices');
		                	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
		                	$("#"+aSelectedRowId+"_ackDate").datepicker();
		            		$("#"+aSelectedRowId+"_shipDate").datepicker();
		                	var id = ui.item.id;
		                	var product = ui.item.label;
		                	$("#"+aSelectedRowId+"_prMasterId").val(id);
		                	$("#new_row_prMasterId").val(id);
		                	
		                	var celValue = $('#vePOID').val();
		                	$.ajax({
						        url: './getLineItems?prMasterId='+id,
						        type: 'POST',       
						        success: function (data) {
						        	$.each(data, function(key, valueMap) {										
										
						        		if("lineItems"==key)
										{				
											$.each(valueMap, function(index, value){						
												
													$("#new_row_description").val(value.description);
													$("#"+aSelectedRowId+"_description").val(value.description);
													$("#new_row_unitCost").val(value.lastCost);
													$("#"+aSelectedRowId+"_unitCost").val(value.lastCost);
													$("#new_row_priceMultiplier").val(value.pomult);
													$("#"+aSelectedRowId+"_priceMultiplier").val(value.pomult);
													$("#new_row_sopopup").val(value.sopopup);
													$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
													$("#new_row_vePoid").val(celValue);
													$("#"+aSelectedRowId+"_vePoid").val(celValue);
												console.log(value.isTaxable+"=="+IncTaxOnPOAndInvoices+"=="+IncTaxOnPOAndInvoices[0].valueLong);
												/*					
												 * Eric gave this explanation
												 * 	   Bring
												Taxable    TaxOnSettings  taxonpo  taxincludeornot
													0			0			0			no
													0			1			1			yes
													1			0			0			no
													1			1			1			yes
													*/
												
												
												var includetaxchecked=(IncTaxOnPOAndInvoices==null)?0:IncTaxOnPOAndInvoices[0].valueLong;
												if((value.isTaxable == 1 && includetaxchecked==1)
												|| (value.isTaxable == 0 && includetaxchecked==1)		
												)		
													{
														$("#new_row_taxable").prop("checked",true);
														$("#"+aSelectedRowId+"_taxable").prop("checked",true);
													}
													else
													{
														$("#new_row_taxable").prop("checked",false);
														$("#"+aSelectedRowId+"_taxable").prop("checked",false);
													}
											
											}); 												
											$("#new_row_description").focus();
											$("#"+aSelectedRowId+"_description").focus();
										}	
									});
																		
									
						        }
						    });
		                }
		            });
		      }
			,dataEvents: [
		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
		    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
		    			  ]
					}},
			{name:'inLineNoteImage', index:'inLineNoteImage', align:'center', width:10,hidden:false,editable:false,formatter: inlineNoteImageFormater},
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50,
           		dataEvents: [
			       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
			    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
			    			  ]},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'notes', index:'notes', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
			{name:'notesdesc', index:'notesdesc', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7,decimalPlaces: 2,
				dataEvents: [
			       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
			    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
			    			  ]},formatter:callFloorFigureMethod,editrules:{number:true,required: true}},
			{name:'quantityReceived', index:'quantityReceived', align:'center', width:40,hidden:true, editable:false, editoptions:{size:5, alignText:'right'},editrules:{edithidden:true}},
			{name:'invoicedAmount', index:'invoicedAmount', align:'center', width:40,hidden:true, editable:false, editoptions:{size:5, alignText:'right'},editrules:{edithidden:true}},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10,decimalPlaces: 2,
				dataEvents: [
			       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
			    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
			    			  ]
			}, formatter:customCurrencyFormatter, editrules:{edithidden: true}},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:40,hidden:false, editable:true, editoptions:{size:15, alignText:'right',decimalPlaces: 4,
				dataEvents: [
			       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
			    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
			    			  ]
			}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
			{name:'netCast',index:'netCast',width:50 , align:'right',formatter:customCurrencyFormatter,hidden:true},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:true, formatter:POtotalFormatter, editoptions:{size:15, alignText:'right',
				dataEvents: [
			       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
			    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
			    			  ]	
			},editrules:{edithidden:true}},
			{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{custom:true,custom_func:prmasteridvalidation}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'posistion',index:'posistion',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
			{name:'upAndDown',index:'upAndDown',align:'left',width:50, formatter: upAndDownImage,hidden:true},
			{name:'taxTotal', index:'taxTotal', align:'center', width:20,hidden:true},
			{name:'ackDate', index:'ackDate', align:'center', width:50,hidden:true, editable:true,   editoptions:{size:15, align:'center',},editrules:{edithidden:false}},//formatter:'date',, formatoptions:{ srcformat: 'd-m-Y',newformat:'m/d/Y'}
			{name:'shipDate', index:'shipDate', align:'center', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'center'}, editrules:{edithidden:false}},//formatter:'date',, formatoptions:{srcformat: 'd-m-Y',newformat:'m/d/Y'}
			{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
			{name:'inLineNote', index:'inLineNote', align:'center', width:20,hidden:true, editable:true},
			{name:'canDeletePO', index:'canDeletePO', align:'center',  width:20, hidden:false, editable:false, formatter:canDeletePOCheckboxFormatter,   editrules:{edithidden:true}},
			{name:'itemcodeforref', index:'', align:'center', width:20,hidden:true, editable:true},
			{name:'prmasteridforref', index:'', align:'center', width:20,hidden:true, editable:true}
			],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 765, rownumbers:true, altRows: true,viewrecords: true, altclass:'myAltRowClass',
		//editurl:"./jobtabs5/manpulaterPOReleaseLineItem",
		caption: 'Line Item',
		
		jsonReader : {
			root: "rows",
			page: "page",
			total: "total",
			records: "records",
			repeatitems: false,
			cell: "cell",
			id: "id",
			userdata: "userdata",
			
		},
		loadBeforeSend: function(xhr) {
			posit_outside_purchaseorder= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		loadComplete: function(data) {
		},
		gridComplete: function () {
	      	jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_purchaseorder);
	        posit_outside_purchaseorder=0;
	        if(POlineItemgridLoad){
           	 var gridRows = $('#lineItemGrid').getRowData();
           	Purchase_lines_form =  JSON.stringify(gridRows);
	             POlineItemgridLoad=true;
            }
	        
            SetoverAllPOTotal();
            POLineItemTabformChanges();
            //TinyMCETextEditorForAllInitsetup(560,220,true);
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow:  function(id){
			selectedLineItemGrid=id;
			id= id.replace(/[^0-9\.]+/g,"");					
			selectedLineItem = id;
		},	   
		ondblClickRow: function(id){
			var lastSel = '';
		     if(id && id!==lastSel){ 
		        jQuery(this).restoreRow(lastSel); 
		        lastSel=id; 
		     }
		     $("#lineItemGrid_iledit").trigger("click");
		},
		cellsubmit: 'clientArray',
		editurl: 'clientArray',
		alertzIndex:1050,
		shrinkToFit : true
	 }).navGrid("#lineItemPager", {
			add : false,
			edit : false,
			del : false,
			alertzIndex : 10000,
			search : false,
			refresh : false,
			pager : true,
			alertcap : "Warning",
			alerttext : 'Please select a Product'
		},
		// -----------------------edit// options----------------------//
		{},
		// -----------------------add options----------------------//
		{},
		// -----------------------Delete options----------------------//
		{}); 

	 $("#lineItemGrid").jqGrid(
				'inlineNav',
				'#lineItemPager',
				{
					edit : true,
					add : true,
					zIndex : 1234,
					refresh : false,
					cloneToTop : true,
					alertzIndex : 1234,
					
					addParams : {
						position: "last",
						addRowParams : {
							keys : false,
							oneditfunc : function(id) {
								
								$("#"+id+"_ackDate").datepicker();
								$("#"+id+"_shipDate").datepicker();
								getProductDetails();
							},
							successfunc : function(response) {
								return true;
							},
							aftersavefunc : function(response) {
								var ids = $("#lineItemGrid").jqGrid('getDataIDs');
								var veaccrrowid;
								if(ids.length==1){
									veaccrrowid = 0;
								}else{
									var idd = jQuery("#lineItemGrid tr").length;
									for(var i=0;i<ids.length;i++){
										if(idd<ids[i]){
											idd=ids[i];
										}
									}
									 veaccrrowid= idd;
								}
								console.log("Add"+selectedLineItemGrid); 
								if(selectedLineItemGrid=="new_row"){
									console.log("IFselectedLineItemGrid"+selectedLineItemGrid); 
									$("#" + selectedLineItemGrid).attr("id", Number(veaccrrowid)+1);
									var changeidnum=Number(veaccrrowid)+1;
									//$("#canDeleteSOID_new_row").attr("id","canDeleteSOID_"+changeidnum);
									}
								
								var grid=$("#lineItemGrid");
								grid.jqGrid('resetSelection');
							    var dataids = grid.getDataIDs();
							    for (var i=0, il=dataids.length; i < il; i++) {
							        grid.jqGrid('setSelection',dataids[i], false);
							    }
							    SetoverAllPOTotal();
							    POLineItemTabformChanges();
							},
							errorfunc : function(rowid, response) {
								return false;
							},
							afterrestorefunc : function(rowid) {
								
							}
						}
					},
					editParams : {
						keys : false,
						position: "last",
						successfunc : function(response) {
							return true;
						},
						aftersavefunc : function(id) {

							var ids = $("#lineItemGrid").jqGrid('getDataIDs');
							var veaccrrowid;
							if(ids.length==1){
								veaccrrowid = 0;
							}else{
								var idd = jQuery("#lineItemGrid tr").length;
								for(var i=0;i<ids.length;i++){
									if(idd<ids[i]){
										idd=ids[i];
									}
								}
								 veaccrrowid= idd;
							}
							console.log("Edit"+selectedLineItemGrid); 
							if(selectedLineItemGrid=="new_row"){
								console.log("IF selectedLineItemGrid"+selectedLineItemGrid);
								$("#" + selectedLineItemGrid).attr("id", Number(veaccrrowid)+1);
								var changeidnum=Number(veaccrrowid)+1;
								//$("#canDeleteSOID_new_row").attr("id","canDeleteSOID_"+changeidnum);
								}
							
							var grid=$("#lineItemGrid");
							grid.jqGrid('resetSelection');
						    var dataids = grid.getDataIDs();
						    for (var i=0, il=dataids.length; i < il; i++) {
						        grid.jqGrid('setSelection',dataids[i], false);
						    }
						    SetoverAllPOTotal();
						    POLineItemTabformChanges();
						},
						errorfunc : function(rowid, response) {
							
						},
						afterrestorefunc : function( id ) {
							
						},
						oneditfunc : function(id) {
							console.log('OnEditfunc'+id);
							checkProductDetailsforvalidation($("#"+id+"_prMasterId").val());
							$("#"+id+"_ackDate").datepicker();
							$("#"+id+"_shipDate").datepicker();
							var unitcost=$("#"+id+"_unitCost").val();
							unitcost=unitcost.replace(/[^0-9\-.]+/g,"");
							if(unitcost==undefined ||unitcost==""||unitcost==null){
								unitcost=0.00;
							}
							var amount=$("#"+id+"_quantityBilled").val();
							amount=amount.replace(/[^0-9\-.]+/g,"");
							if(amount==undefined ||amount==""||amount==null){
								amount=0.00;
							}
							$("#"+id+"_quantityBilled").val(amount);
							$("#"+id+"_unitCost").val(unitcost);
						
						}
					}
				});
	 
	     //Drag And DROP
	 		jQuery("#lineItemGrid").jqGrid('sortableRows');
	 		jQuery("#lineItemGrid").jqGrid('gridDnD');
	 		
	 		
			 var custombuttonthereornot=document.getElementById("copyrowcustombutton");
			 if(custombuttonthereornot == null){
				 $('#lineItemGrid').jqGrid('navButtonAdd',"#lineItemPager",{ caption:"",id:"copyrowcustombutton", buttonicon:"ui-icon-newwin", onClickButton:copyOption, position: "last", title:"Copy Row", cursor: "pointer"});
			 }
	 		$("#lineItemGrid").jqGrid('navButtonAdd',"#lineItemPager",{ caption:"", buttonicon:"ui-icon-document", onClickButton:openLineItemNoteDialog, position: "after", title:"Inline Note", cursor: "pointer"});
	 		$("#lineItemGrid").jqGrid('navButtonAdd',"#lineItemPager",{ caption:"", buttonicon:"ui-icon-bookmark", onClickButton:showInvoicedReceived, position: "after", title:"Show Received/Invoiced", cursor: "pointer"});

	 		$("#lineItemGrid_iladd").click(function() {
	 			$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 			$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 			document.getElementById("SaveLinesPOButton").disabled = true;
	 			document.getElementById("CancelLinesPOButton").disabled = true;
	 		});
	 		$("#lineItemGrid_iledit").click(function() {
	 			$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 			$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 			document.getElementById("SaveLinesPOButton").disabled = true;
	 			document.getElementById("CancelLinesPOButton").disabled = true;
	 		});

	 		$("#lineItemGrid_ilsave").click(function() {
	 			
	 			if($("#infocnt").text()=="2");
	 			$("#infocnt").text("Invalid Product. Please select product from the list");
	 			
	 			if($("#info_head").text()!="Error")
				{
		 			document.getElementById("SaveLinesPOButton").disabled = false;
		 			document.getElementById("CancelLinesPOButton").disabled = false;
		 			if($("#release").val()==undefined){
		 				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
			 			$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
		 			}
		 			else
		 				{
		 				$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			 			$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		 				}
				}
	 		    });


	 		$("#lineItemGrid_ilcancel").click(function() {
	 			document.getElementById("SaveLinesPOButton").disabled = false;
	 			document.getElementById("CancelLinesPOButton").disabled = false;
	 			$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	 			$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	 		});
}

function inlineNoteImageFormater(cellValue, options, rowObject){
	 console.log('Cell Value: q'+cellValue+'q');
		var element = '';
		if(cellValue !== '' && cellValue !== null && cellValue != undefined && cellValue.trim().length>0){
		   element = "<img src='./../resources/images/lineItem_new.png' title='Line Items' style='vertical-align: middle;'>";
	   }
	return element;
	}



function prmasteridvalidation( value, colname ) {
	
	
	if($("#new_row_prMasterId").val()!=0)
	{
		if( $("#new_row_prMasterId").val() != undefined)	
		{
			if($("#new_row_note").val()!=$("#new_row_itemcodeforref").val() && $("#new_row_prMasterId").val() == $("#new_row_prmasteridforref").val()){
				
					$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
					$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
					document.getElementById("SaveLinesPOButton").disabled = true;
					document.getElementById("CancelLinesPOButton").disabled = true;
				
					setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
					result = [false, ''];
			}
			else{
					document.getElementById("SaveLinesPOButton").disabled = false;
					document.getElementById("CancelLinesPOButton").disabled = false;
					$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
					$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
				 result = [true,""];
			}
		}
		else
		{
			var grid = $("#lineItemGrid");
			var rowId =  grid.jqGrid('getGridParam', 'selrow');

			var note = $("#"+rowId+"_note").val();
			var itemcodeforref = grid.jqGrid('getCell', rowId, 'itemcodeforref');
			var prMasterId = $("#"+rowId+"_prMasterId").val();
			var prmasteridforref = grid.jqGrid('getCell', rowId, 'prmasteridforref');
			
			
			//alert(note+"=="+itemcodeforref+"=="+ prMasterId+"=="+prmasteridforref);
		if(note!=itemcodeforref && prMasterId == prmasteridforref){
				
				$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				document.getElementById("SaveLinesPOButton").disabled = true;
				document.getElementById("CancelLinesPOButton").disabled = true;
			
				setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
				result = [false, ''];
		}
		else{
				document.getElementById("SaveLinesPOButton").disabled = false;
				document.getElementById("CancelLinesPOButton").disabled = false;
				$('#SaveLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
				$('#CancelLinesPOButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			 result = [true,""];
		}
			
		}
	}
	else
	{
		setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
		result = [false, ''];
	}
	
	return result;
}




function POtotalFormatter(cellValue, options, rowObject){
	var rowid = options.rowId;
	
	var unitCost=rowObject['unitCost'];
	var priceMultiplier= rowObject['priceMultiplier'];
	var quantityOrdered = rowObject['quantityOrdered'];
	console.log("unitCost=="+unitCost);
	if(unitCost!=undefined){
	   if((unitCost+"").contains("$")){
		   unitCost=unitCost.replace(/[^0-9-.]/g, '');
		   unitCost=Number(floorFigureoverall(unitCost,2));
	   }
	}
	if(isNaN(unitCost)){
		unitCost=0;
	}
	if(isNaN(priceMultiplier)){
		priceMultiplier=0;
	}else{
		priceMultiplier=Round_priceMultiplier(priceMultiplier);
	}
	if(isNaN(quantityOrdered)){
		quantityOrdered=0;
	}else{
		quantityOrdered=Number(floorFigureoverall(quantityOrdered,2));
	}
	if(priceMultiplier==0){
		priceMultiplier=1;
	}
	var total =unitCost*priceMultiplier*quantityOrdered;
	return formatCurrency(total);
	
}

function checkProductDetailsforvalidation(prdouctid){
	
	var rowId =  $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	$("#lineItemGrid").jqGrid('setCell',rowId,'prmasteridforref', prdouctid);
	
	$.ajax({
        url: './getLineItems?prMasterId='+prdouctid,
        type: 'POST',       
        success: function (data) {
        	$.each(data, function(key, valueMap) {										
				
        		if("lineItems"==key)
				{	$.each(valueMap, function(index, value){		
        			 $("#lineItemGrid").jqGrid('setCell',rowId,'itemcodeforref', value.itemCode);
				});
				}	
		});
	   }
	});
}

function getProductDetails(){
	var celValue = $('#vePOID').val();
	$.ajax({
	    url: "./userlistcontroller/getVendorProductDetails",
	    type: 'POST',
	    success: function(data){
	    	console.log(']ENI'+data);
        	$("#new_row_prMasterId").val(data);
        	$("#new_row_prmasteridforref").val(data);
	    	$.ajax({
		        url: './getLineItems?prMasterId='+data,
		        type: 'POST',       
		        success: function (data) {
		        	var IncTaxOnPOAndInvoices=getSysvariableStatusBasedOnVariableName('IncTaxOnPOAndInvoices');
		        	$.each(data, function(key, valueMap) {										
						
		        		if("lineItems"==key)
						{				
							$.each(valueMap, function(index, value){						
								    var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
								    $("#new_row_note").val(value.itemCode);
								    $("#new_row_itemcodeforref").val(value.itemCode);
									$("#new_row_description").val(value.description); // value.description
									$("#new_row_unitCost").val(value.lastCost);
									$("#new_row_priceMultiplier").val(value.pomult);
									$("#new_row_sopopup").val(value.sopopup);
									$("#new_row_vePoid").val(celValue);
									var includetaxchecked=(IncTaxOnPOAndInvoices==null)?0:IncTaxOnPOAndInvoices[0].valueLong;
									if((value.isTaxable == 1 && includetaxchecked==1)
									|| (value.isTaxable == 0 && includetaxchecked==1)		
									){
										$("#new_row_taxable").prop("checked",true);
									}
									else
									{
										$("#new_row_taxable").prop("checked",false);
									}
							
							}); 												
						}	
					});
														
		        	$("#new_row_description").focus();
					
		        }
		    });
	    	
	    }
	});
}

function canDeletePOCheckboxFormatter(cellValue, options, rowObject){
	var id="canDeletePOID_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"' onclick='SetoverAllPOTotal();deletePOCheckboxChanges(this.id);POLineItemTabformChanges();' value='false'>";
	return element;
}

function deletePOCheckboxChanges(id){
	id="#"+id;
	console.log("deletePOCheckboxChanges::"+id);
    var canDo=$(id).is(':checked');
    if(canDo){
    	$(id).val("true");
    }else{
    	$(id).val("false");
    }
}

function POLineItemTabformChanges(formvalue){
	console.log("test");
	 var gridRows = $('#lineItemGrid').getRowData();
	    var new_PO_lines_form =  JSON.stringify(gridRows);
     if($('#lineItemGrid').val()!=undefined){
   	 if(new_PO_lines_form != Purchase_lines_form){
   	if(formvalue=="TabChange"){
   		$( "#poreleasetab ul li:nth-child(1)" ).addClass("ui-state-disabled");
   		$( "#poreleasetab ul li:nth-child(3)" ).addClass("ui-state-disabled");
 	  var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,Before move to other tab have to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					$( "#poreleasetab ul li:nth-child(1)" ).addClass("ui-state-disabled");
					$( "#poreleasetab ul li:nth-child(3)" ).addClass("ui-state-disabled");
				    return false;
				}}}).dialog("open");
   	}else{
   		$( "#poreleasetab ul li:nth-child(1)" ).addClass("ui-state-disabled");
   		$( "#poreleasetab ul li:nth-child(3)" ).addClass("ui-state-disabled");
   	}
   }else{
   	$( "#poreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
   	$( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
   }
    }
	return false;
}

function SaveLinesPurchaseOrder(popupdetail){
	var newDialogDiv = jQuery(document.createElement('div'));
	
	var vePOID = $("#vePOID").val();
	var Frieght = $('#freightLineId').val();
	Frieght= parseFloat(Frieght.replace(/[^0-9-.]/g, ''));
	var subTotal = $('#subtotalLineId').val();
	subTotal= parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var Total = $('#totalLineId').val();
	Total= parseFloat(Total.replace(/[^0-9-.]/g, ''));
	var taxValue = $('#lineID').val();
	taxValue = parseFloat(taxValue.replace(/[^0-9-.]/g, ''));


	  var rows = jQuery("#lineItemGrid").getDataIDs();
	  var deletePOLineDetailID=new Array();
		 for(var a=0;a<rows.length;a++)
		 {
		    row=jQuery("#lineItemGrid").getRowData(rows[a]);
		   var id="#canDeletePOID_"+rows[a];
		   var canDeletepO=$(id).is(':checked');
		   console.log(rows[a]+"canDeletepO=="+canDeletepO);
		   if(canDeletepO){
			  var var_vepoDetailID=row['vePodetailId'];
			  if(var_vepoDetailID!=undefined && var_vepoDetailID!=null && var_vepoDetailID!="" && var_vepoDetailID!=0){
				  deletePOLineDetailID.push(var_vepoDetailID);
			 	}
			 $('#lineItemGrid').jqGrid('delRowData',rows[a]);
		  }
	} 
	
	var gridRows = $('#lineItemGrid').getRowData();
	var dataToSend = JSON.stringify(gridRows);

		
	$.ajax({
		url: "./jobtabs5/SaveLinesPurchaseOrder",
		type: "POST",
		async:false,
		data :{ "vePOID":vePOID,"frieght":Frieght,"subTotal":subTotal,"Total":Total,"taxValue":taxValue,			
			"gridData":dataToSend,
			"DelPOData":deletePOLineDetailID
			},
		success: function(data) {
			$('#ShowInfo').html("Saved");
			POlineItemgridLoad=true;
			jQuery("#lineItemGrid").jqGrid('setGridParam',{url:'./jobtabs5/jobReleaseLineItem',postData: {vePoId :function(){var vePOID=$("#vePOID").val();if(vePOID==null || vePOID==''){vePOID=0;}return vePOID;}}}).trigger("reloadGrid");
			 setTimeout(function(){
				$('#ShowInfo').html("");						
				},3000); 
			 $( "#poreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
			 $( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
			 if(popupdetail=="close"){
				 $("#porelease").dialog("close");
			 }
				
			// $( "#salesreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
		}
	});
}

function closePurchaseOrderLineItemTab(){
    var gridRows = $('#lineItemGrid').getRowData();
    var new_PO_lines_form =  JSON.stringify(gridRows);
    if(new_PO_lines_form != Purchase_lines_form){
  	  var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			//closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"Yes": function(){
					jQuery(this).dialog("close");
					SaveLinesPurchaseOrder("close");
				    return false;
				},
				"No": function ()	{
					jQuery(this).dialog("close");
					$("#porelease").dialog("close");
					$( "#poreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
				  	$( "#poreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
				  	$( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
				return false;	
				}}}).dialog("open");
    }else{
    	$("#porelease").dialog("close");
    }
	
}
function openLineItemNoteDialog(){
	 CKEDITOR.replace('inlineItemId', ckEditorconfigforinline);
	if($('#POtransactionStatus').val() == '-1'){
		 var newDialogDiv = jQuery(document.createElement('div'));
			errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Void' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
		}
	 else if($('#POtransactionStatus').val() == '0'){
		 var newDialogDiv = jQuery(document.createElement('div'));
		 errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Hold' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }else{
		 
		var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
		if(rowId === null){
			var errorText = "Please select a line item to add Line Note";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		var lineItemText = $("#lineItemGrid").jqGrid('getCell', rowId, 'inLineNote');
		//var JopInlinetext =lineItemText.replace("&And", "'");
		//areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('inlineItemId');
		//$(".nicEdit-main").empty();
		//$(".nicEdit-main").append(lineItemText);
	//	tinyMCE.get("inlineItemId").setContent(lineItemText);
		CKEDITOR.instances['inlineItemId'].setData(lineItemText);
		
		if($('#POtransactionStatus').val()=='2'){
			$("#PoSaveInLineItemID").css("display", "none");
		}else{
			$("#PoSaveInLineItemID").css("display", "inline-block");
		}
		
		jQuery("#POInLineItem").dialog("open");
		//$(".nicEdit-main").focus();
		return true;
	 }
}
function SetoverAllPOTotal(){
	var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
	var aVal = new Array(); 
	var aTax = new Array();
	var sum = 0;
	var taxAmount = 0;
	var aTotal = 0;
	var ids = $("#lineItemGrid").jqGrid('getDataIDs');
	$.each(allRowsInGrid, function(index, value) {
		aVal[index] = value.quantityBilled;
		var number1 = aVal[index].replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		
		var id="#canDeletePOID_"+ids[index];
		var canDo=$(id).is(':checked');
		if(!canDo){
		sum = Number(sum) + Number(number6); 
		
		aVal[index] = value.taxable;
		if (aVal[index] === 'Yes'){
			aTax[index] = value.quantityBilled;
			var number1 = aTax[index].replace("$", "");
			var number2 = number1.replace(".00", "");
			var number3 = number2.replace(",", "");
			var number4 = number3.replace(",", "");
			var number5 = number4.replace(",", "");
			var number6 = number5.replace(",", "");
			var taxValue = $('#taxLineId').val();
			taxAmount = taxAmount + Number(number6)*(taxValue/100);
		}
		}
	});
	console.log("taxAmount=="+taxAmount);
	$('#subtotalGeneralId').val(formatCurrency(sum));
	$('#subtotalLineId').val(formatCurrency(sum));
	$('#subtotalKnowledgeId').val(formatCurrency(sum));
	$('#generalID').val(formatCurrency(taxAmount));
	$('#lineID').val(formatCurrency(taxAmount));
	$('#KnowledgeID').val(formatCurrency(taxAmount));
	
	var frieghtvalue=Number($("#freightGeneralId").val().replace(/[^0-9\.]+/g,""));
	if(frieghtvalue==undefined ||frieghtvalue=="" || frieghtvalue==null ||frieghtvalue=="null"){
		frieghtvalue=0.00;
		}
	aTotal = aTotal + sum + taxAmount +frieghtvalue;
	$('#totalGeneralId').val(formatCurrency(aTotal));
	$('#totalLineId').val(formatCurrency(aTotal));
	$('#totalKnowledgeId').val(formatCurrency(aTotal));
	$("#freightLineId").val(formatCurrency(frieghtvalue));
	//$("#taxLineId").val($("#Tax_ID").text());
}

function saveLineItemNote(){
//		var lineItemNote= $('#POInLineItemID').find('.nicEdit-main').html();
//		var grid = $("#lineItemGrid");
//		var rowId = grid.jqGrid('getGridParam', 'selrow');
//		console.log("Line Item Note: "+lineItem);
//		//alert('Selected Row Item: '+rowId);
//		//var lineItem = $("#lineItemNote").val();
//		$("#lineItemGrid").jqGrid('setCell', rowId,'notes', lineItem);
//		$("#lineItemGrid").jqGrid('setCell', rowId,'inlineNoteImage',lineItem);
//		var lineItem ='';
//		lineItem = lineItemNote.replace(/"/g, "'");


	//var inlineText= $('#POInLineItemID').find('.nicEdit-main').html();
	var inlineText=  CKEDITOR.instances["inlineItemId"].getData(); //tinyMCE.get("inlineItemId").getContent();
	var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	var vePodetailId = $("#lineItemGrid").jqGrid('getCell', rowId, 'vePodetailId');
	console.log(vePodetailId);

	var image="<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
//	var plaintxt=tinyMCE.get("inlineItemId").getContent({ format: 'text' });
	var plaintxt=CKEDITOR.instances["inlineItemId"].getData();
	  if(plaintxt==null || plaintxt==undefined || plaintxt=="" || plaintxt.trim().length==0){
		  image=undefined;
		  inlineText=undefined;
	  }
	  $("#lineItemGrid").jqGrid('setCell',rowId,'inLineNoteImage', image);
	  $("#lineItemGrid").jqGrid('setCell',rowId,'inLineNote', inlineText);
	  
	  jQuery("#POInLineItem").dialog("close");
	  CKEDITOR.instances['inlineItemId'].destroy();
	  
	//var aLineItem = new Array();
	//aLineItem.push(inlineText);
	//aLineItem.push(vePodetailId);
	/* $.ajax({
		url: "./jobtabs5/SavePOlinetextInfo",
		type: "POST",
		data : {'lineItem' : aLineItem},
		success: function(data){
			areaLine.removeInstance('inlineItemId');
			jQuery("#POInLineItem").dialog("close");
			$("#lineItemGrid").trigger("reloadGrid");
		}
	}); */
	
	/* element = "";
	var abillNote = $("#billNote").val();
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
	var aJoMasterID = $("#joMaster_ID").text();
	var aJoReleasedDate = grid.jqGrid('getCell', rowId, 'released');
	var aJoReleaseType = grid.jqGrid('getCell', rowId, 'type');
	if(aJoReleaseType === 'Drop Ship')
		aJoReleaseType = 1;
	else if(aJoReleaseType === 'Stock Order')
		aJoReleaseType = 2;
	else if(aJoReleaseType === 'Bill Only')
		aJoReleaseType = 3;
	else if(aJoReleaseType === 'Commission')
		aJoReleaseType = 4;
	else if(aJoReleaseType === 'Service')
		aJoReleaseType = 5;
	var aReleaseNote = grid.jqGrid('getCell', rowId, 'note');
	var aEstimateAllocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
	var costAllocated = aEstimateAllocated.replace(/[^0-9\.]+/g,"");
	var aCostAllocated = Number(costAllocated);
	//http://localhost:8080/turbotracker/turbo/jobtabs5/billNote?&joReleaseId=66153&billNote=This%20is%20Test&joMasterID=38191&joReleaseDate=03/03/2014%20&joReleaseType=Drop%20Ship&ReleaseNote=fans&EstimatedBilling=2688
	//joReleaseId=66153&billNote=This%20is%20Test&joMasterID=38191&joReleaseDate=03/03/2014%20&joReleaseType=Drop%20Ship&ReleaseNote=fans&EstimatedBilling=2688
	$.ajax({
		url: "./jobtabs5/billNote",
		type: "GET",
		data : "&joReleaseId="+aJoReleaseId+"&billNote=" +abillNote+"&joMasterID=" +aJoMasterID+"&joReleaseDate=" +aJoReleasedDate+
				"&joReleaseType=" +aJoReleaseType+"&ReleaseNote=" +aReleaseNote+"&EstimatedBilling=" +aCostAllocated,
		success: function(data) { 
	*/
	//$("#lineItemGrid").jqGrid('GridUnload');
//		 var jsonList1='';
//		try{
//		///var selectedRow=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
//		var rows = jQuery("#lineItemGrid").getDataIDs();
//		var rowr=jQuery("#lineItemGrid").getRowData(rows[rowId-1]);
//		var notes_test = rowr['inLineNote'];
//		rowId = rowId.replace(/[^0-9\.]+/g,"");
//		for(a=0;a<rows.length;a++)
//		{
//			var row=jQuery("#lineItemGrid").getRowData(rows[a]);
//			notes_test=row['inLineNote'];
//		if(a==(rowId-1)){
//		notes_test = lineItem;
//		}
	
//		if(a == 0){
//		jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+notes_test+"\",notesdesc:\""+notes_test+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
//		}else{
//		jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+notes_test+"\",notesdesc:\""+notes_test+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
//		}
//		console.log(jsonList1+a);
	
//		}
//		}
//		catch(e){
//			console.log('Error in load: '+e.message);
//		}
//		loadLinesItemGrid();
//		areaLine.removeInstance('inlineItemId');
//		jQuery("#POInLineItem").dialog("close");
//		$("#lineItemGrid").trigger("reloadGrid");
	/* $("#release").trigger("reloadGrid");
			var errorText = "Bill Note Successfully Updated.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
			/*jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");*/
		/* 	return false;
		}
	});  */
	
}

function copyOption(){
	 if($('#POtransactionStatus').val() == '-1'){
			errorText = "You can not Edit Line Items, \nTransaction Status is 'Void' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
		}
	 else if($('#POtransactionStatus').val() == '0'){
		 errorText = "You can not Edit Line Items, \nTransaction Status is 'Hold' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else if($('#POtransactionStatus').val() == '2'){
		 errorText = "You can not Edit Line Items, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	 else{
		 var rowid=$('#lineItemGrid').getGridParam('selrow');
if(rowid != undefined){
	try{
		var ids = $("#lineItemGrid").jqGrid('getDataIDs');
		var veaccrrowid;
		if(ids.length==0){
			veaccrrowid = 0;
		}else{
			var idd = jQuery("#lineItemGrid tr").length;
			for(var i=0;i<ids.length;i++){
				if(idd<ids[i]){
					idd=ids[i];
				}
			}
			 veaccrrowid= idd;
		}
		var data=$("#lineItemGrid").jqGrid('getRowData', rowid);
		data["vePodetailId"]="";
		data["vePoid"]=$("#vePOID").val();
		POlineItemgridLoad=false;
		$("#lineItemGrid").addRowData(Number(veaccrrowid)+1,data, 'last');
		var grid=$("#lineItemGrid");
		grid.jqGrid('resetSelection');
	    var dataids = grid.getDataIDs();
	    for (var i=0, il=dataids.length; i < il; i++) {
	        grid.jqGrid('setSelection',dataids[i], false);
	    }
		SetoverAllPOTotal();
        POLineItemTabformChanges();
	/* var gridRows = $('#lineItemGrid').getRowData();
	 var dataToSend = JSON.stringify(gridRows);
	 POlineItemgridLoad=true;
	jQuery("#lineItemGrid").jqGrid('setGridParam',{url:"./vendorscontroller/ReorderInsertNew",postData:{"gridData":dataToSend,"WareHouseId" : WareHouseId, "VendorID" : VendorID}}).trigger("reloadGrid");
	*/
	/*$.ajax({
		url: "./jobtabs5/copyPoLineItem",
		type: "GET",
		data : {vepoDetailId:vepodetailid,vePOId:''},
		success: function(data) {
			loadLineItemGrid();
			
		}
	});*/
	selectedLineItem = '';
	}catch(err){
		alert(err.message);
	}
}else{
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:red;">Please Select a Row</b></span>');
								jQuery(newDialogDiv).dialog(
										{
											modal : true,
											width : 300,
											height : 150,
											title : "Warning",
											buttons : [ {
												height : 35,
												text : "OK",
												click : function() {
													$(this).dialog("close");
												}
											} ]
										}).dialog("open");
}
return true;
}
}