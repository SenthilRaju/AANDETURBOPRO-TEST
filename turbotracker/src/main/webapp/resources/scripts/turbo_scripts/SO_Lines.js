var isNewIconAdded = false;
var isShow = false;
var mytestmethod_loc_var=true;
var so_lines_form;
var lineItemgridLoad=true;
jQuery(document).ready(function() {
	/*
	 * Added by Simon
	 * Reason for Adding : ID#567 (below 1 line)
	 */
	//$(".ui-dialog .ui-dialog-titlebar-close").css({"display":"none"});
/*console.log(typeof(ckEditorconfigforinline));
console.log(typeof(ckEditorconfig));
		if(typeof(ckEditorconfigforinline)!=undefined)
		CKEDITOR.replace('lineItemNoteID', ckEditorconfigforinline);
		else*/
	
	var jobStatus = '';
	if(typeof(getUrlVars()) != undefined && getUrlVars().indexOf("jobStatus")>-1){
		jobStatus = getUrlVars()["jobStatus"];
	if(typeof(jobStatus) != undefined && jobStatus.indexOf("Closed")>-1){
	
		$('#SaveLineSOReleaseID').css('cursor','default');
		$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		document.getElementById("SaveLineSOReleaseID").disabled = true;
	}else{
		
		if($('#SaveLineSOReleaseID').is(':disabled')){
			document.getElementById("SaveLineSOReleaseID").disabled = false;
			$('#SaveLineSOReleaseID').css('cursor','pointer');
			$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			
		}
	}
}
	
		
		PreloadData();	
		loadSOLineItemGrid();	
		console.log('SO_Lines.js Called');
		/*if(typeof(hideWithPrice) !== undefined && hideWithPrice === 'outsideJob')
		{
			$('#withPriceLine').css('display','block');
			$('#withPriceLineLabel').css('display','block');
			$('#withPrice').css('display','block');
			$('#withPriceLabel').css('display','block');
		}*/
		if(typeof(withPrice) !== undefined){
			if(withPrice === 'Checked'){
				$('#withPriceLine').prop('checked',true);
				$('#withPriceLine').css('display','block');
				$('#withPriceLineLabel').css('display','block');
				$('#withPrice').css('display','block');
				$('#withPriceLabel').css('display','block');
			}
			else if(withPrice === 'NotChecked'){
				$('#withPriceLine').prop('checked',false);
				$('#withPriceLine').css('display','block');
				$('#withPriceLineLabel').css('display','block');
				$('#withPrice').css('display','block');
				$('#withPriceLabel').css('display','block');
			}
		}
		
		jQuery("#inventoryDetailsTab").dialog({
			autoOpen:false,
			width:800,
			title:"",
			modal:true,
			buttons:{
				"Close":function(){
					$(this).dialog("close");
				}
					},
			close:function(){
				$(this).dialog("close");
			return true;}	
		});
		 if(salesOrderFlag==null){
			 $("#SOReleaseSuggestedPriceID").attr('disabled',true);
			 $("#SOReleaseSuggestedPriceID").css({"background":"#cccccc"});
		 }else{
			 $("#SOReleaseSuggestedPriceID").attr('disabled',false);
		 }
});


function PreloadData(){
	console.log('SO_Lines.js PreloadData');
	var cuSOID = $('#Cuso_ID').text();
	var taxRate = 0;
	var taxAmt=0;
	var taxValue =0;
	var totalAmount=0;
	var subtotal = 0;
	
	var jobNumber="";
	var joMasterID=0;
	if($("#jobNumber_ID").text()!= null && $("#jobNumber_ID").text() !=""){
		jobNumber = $("#jobNumber_ID").text();
		joMasterID=$("#joMaster_ID").text();
	}
	
	$('#customerInvoice_subTotalIDLine').val(formatCurrency(0));
	$('#customerInvoice_totalIDLine').val(formatCurrency(0));
	var rxMasterID =$('#rxCustomer_ID').text();
	if(cuSOID != '' && cuSOID != null && typeof(cuSOID)!='undefined'){
		$.ajax({
			url: "./salesOrderController/getPreLoadData",
			type: "POST",
			async:false,
			data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+"&jobNumber="+jobNumber+"&joMasterID="+joMasterID,
			success: function(data) {
				$("#so_taxfreight").val(data.Cuso.taxfreight);
				$('#soLinesEmailTimeStamp').empty();$('#soLinesEmailTimeStamp').append(data.emailTime);
				$("#SOnumberLine").val(data.Cuso.sonumber);
				$("#CustomerNameLine").val(data.CustomerName);
				$('#dateOfcustomerLine').val($("#poDate_ID").text());
				$('#customerInvoice_taxId').val(formatCurrencynodollar(data.Cuso.taxRate));
				$('#customerInvoice_frightID').val(formatCurrency(data.Cuso.freight));
				
				if(data.Cusodetail !='undefined' && data.Cusodetail != null){
					
					taxRate = data.Cuso.taxRate;
					if(data.Cuso.taxRate === null){
						taxRate = 0.0000;
					}
					if(data.Cuso.taxTotal===null){
						taxAmt=0.0000;
					}
					//alert(taxRate+'  '+data.Cusodetail.taxTotal);
					taxValue = (parseFloat(taxRate)*parseFloat(data.Cusodetail.taxTotal))/100 ;
					taxAmt = taxValue+parseFloat(data.Cusodetail.taxTotal);
					//alert(taxAmt);
					//$('#customerInvoice_taxvalue').val(formatCurrency(data.Cuso.taxTotal));
					$('#customerInvoice_taxvalue').val(formatCurrency(taxValue));
					//$('#customerInvoice_totalIDLine').val(formatCurrency(data.Cusodetail.taxTotal));
					$('#customerInvoice_totalIDLine').val(formatCurrency(taxAmt));//alert(data.Cusodetail.taxTotal);
					$('#customerInvoice_subTotalIDLine').val(formatCurrency(data.Cusodetail.taxTotal));
					
					formatTotal(data.Cuso.freight,data.Cusodetail.taxTotal);
				}
				
				 
				formattax(data.Cuso.freight);
				var createdDate = data.Cuso.createdOn;
				if (typeof createdDate != 'undefined') 
					FormatDate(createdDate);
				
				var sEmail='<option value=0>(Select Template)</option>';
				$.each(data.salesTemplate, function(key, valueMap) {
					console.log(valueMap.templateDescription +' :: '+valueMap.cuSoid);
					sEmail+='<option value='+valueMap.cuSoid+'>'+valueMap.templateDescription+'</option>';
					/*$.each(valueMap, function(index, value){
						if(index == 'templateDescription' ){
							
						}
						
					}); */
				});
				
				$('#templateListId').html(sEmail);
				
			}
		});
	}
}
function formatTotal(frieght, Total){
	var frieghtval =frieght;
	var total  = Total;
	var Total = frieghtval + total;
	$('#customerInvoice_totalIDLine').val(formatCurrency(Total));
}
function FormatDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$("#dateOfcustomerLine").val(createdDate);
}
/*var posit_job_salesorder=0;
function loadSOLineItemGrid(){	
	var cuSOID = $('#Cuso_ID').text();
	try {
	 $('#SOlineItemGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#SOlineItemPager'),
			url:'./salesOrderController/solineitemGrid',
			postData: {'cuSOID':cuSOID},
			colNames:['Product No', '','Description','Qty','Price Each', 'Mult.', 'Tax','',  'Manu. ID','cuSodetailId', 'prMasterID','SOPopup','Amount',''],
			colModel :[
		{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', 
			editoptions: {
				dataInit : function(elem) {
					$(elem).autocomplete({
						minLength: 1,timeout :1000,
							source: "jobtabs3/productCodeWithNameList",
							select: function( event, ui ){
								var ID = ui.item.id;
								var product = ui.item.label;
								$("#prMasterId").val(ID);
								var aSelectedRowId = $("#SOlineItemGrid").jqGrid('getGridParam', 'selrow');
			                	var id = ui.item.id;
			                	var product = ui.item.label;
			                	$("#"+aSelectedRowId+"_prMasterId").val(id);
			                	$("#new_row_prMasterId").val(id);
			                	
			                	var cuSodetailId = ui.item.cuSodetailId;
			                	//$("#"+aSelectedRowId+"_cuSodetailId").val(cuSodetailId);
			                	$("#new_row_cuSodetailId").val(cuSodetailId);
			                	
			                	var celValue = $('#cuSOid').val();
//			                	alert(id+" || "+celValue+" || "+cuSodetailId);
			                	
			                	//alert(" >>>>>>>> "+$("#new_row_vePoid").val()+ " || "+$("#"+aSelectedRowId+"_vePoid").val());
			                	//alert(id+" || "+product+" || "+aSelectedRowId+"_prMasterId = "+$("#"+aSelectedRowId+"_prMasterId").val()+" || new_row_prMasterId = "+$("#new_row_prMasterId").val());
			                	
								$.ajax({
							        url: './getLineItemsSO?prMasterId='+ID,
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
													if(value.pomult != undefined)
													{
														$("#new_row_priceMultiplier").val(value.pomult);
														$("#"+aSelectedRowId+"_priceMultiplier").val(value.pomult);
													}
													else
													{
														$("#new_row_priceMultiplier").val(0);
														$("#"+aSelectedRowId+"_priceMultiplier").val(0);
													}	
													$("#new_row_sopopup").val(value.sopopup);
													$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
													$("#new_row_cuSoid").val(celValue);
													$("#"+aSelectedRowId+"_cuSoid").val(celValue);
													
													if(value.isTaxable == 1)
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
								},
							error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
							});
				}
			},
			editrules:{edithidden:false,required: true}},
		{name:'noteImage', index:'noteImage', align:'right', width:10,hidden:false, editable:false, formatter:noteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
      	{name:'description', index:'description', align:'left', width:100, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
			cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:45,hidden:false, editable:true, editoptions:{size:5, alignText:'left',
			//dataEvents: [{type: 'change',fn: function(e) {myTest(this.value);}}]
			},
			editrules:{custom: true, custom_func: myTest,edithidden:true,required: true}},
		{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:customCurrencyFormatterWithoutDollar, editrules:{edithidden:true}},
		{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
		{name:'note', index:'note', align:'right', width:10,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'cuSoid', index:'cuSoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'cuSodetailId', index:'cuSodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'prMasterId',index:'prMasterId',     align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'sopopup', index:'sopopup', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
		{name:'amount', index:'amount', align:'right', width:50,hidden:false, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false},formatter:totalFormatter},
		{name:'searchImage', index:'searchImage', align:'right', width:10,hidden:false, editable:false, formatter:searchImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		],
			rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
			sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
			height:210,	width: 750, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
				posit_job_salesorder= jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
			},
			loadComplete: function(data) {
				$("#SOlineItemGrid").setSelection(1, true);
				var ids = $('#SOlineItemGrid').jqGrid('getDataIDs');
				$('#SOlineItemGrid_noteImage').removeClass("ui-state-default ui-th-column ui-th-ltr");			
			    if (ids.length>0) {
			    	console.log("if loop");
			        var sortName = $('#SOlineItemGrid').jqGrid('getGridParam','noteImage');
			        var sortOrder = $('#SOlineItemGrid').jqGrid('getGridParam','description');
			        for (var i=0;i<ids.length;i++) {
			        	
			        	$('#SOlineItemGrid').jqGrid('setCell', ids[i], 'noteImage', '', '',
			                        {style:'border-right-color: transparent !important;'});
			        	$('#SOlineItemGrid').jqGrid('setCell', ids[i], 'description', '', '',
		                        {style:'border-left-color: transparent !important;'});
			        }
			    }
			    PreloadData();
			    mytestmethod_loc_var=true;
			},
			gridComplete: function () {
	             jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_salesorder);
	             posit_job_salesorder=0;
			   },
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){
				var rowData = jQuery(this).getRowData(id); 
				var cuSODetailId = rowData["cuSodetailId"];
				$('#jobCustomerName_ID').text(cuSODetailId);
				try{
					var cusoid = $('#Cuso_ID').text();
					var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
					row=jQuery("#SOlineItemGrid").getRowData(id);
					var prMasterID = $("#"+id+"_prMasterId").val(); //row['prMasterId'];
					var cuSODetailID = $("#"+id+"_cuSodetailId").val(); //row['cuSodetailId'];
					var quantity =  $("#"+id+"_quantityOrdered").val();
					
					 Added by Zenith for Get Correct warehouse Prices 
					if(cuSODetailID===undefined){
						cuSODetailID = rowData["cuSodetailId"];
					}
					if(quantity===undefined){
						quantity = rowData["quantityOrdered"];
					}
					
					if(prMasterID===undefined){
						prMasterID = rowData["prMasterId"];
					}
					var subtotal = ($('#customerInvoice_subTotalIDLine').val()).replace(/[^0-9-.]/g, '');
					
					if(cuSODetailID != null && cuSODetailID != "" && cuSODetailID != undefined)
						$.ajax({
					        url: './salesOrderController/getSOPriceDetails?cusoid='+cusoid+'&prMasterId='+prMasterID+'&cuSODetailID='+cuSODetailID,
					        type: 'POST',       
					        success: function (data) {								
								console.log(data.product);
								console.log(data.cost);
								var marginCost = parseFloat(subtotal)-parseFloat(data.cost);
								$('#salesorder_whsecost').val(formatCurrency(parseFloat(data.product)/parseFloat(quantity)));
								$('#salesorder_ordercost').val(formatCurrency(data.cost));
								console.log(subtotal+' :: '+data.cost+'::'+marginCost)
								$('#salesorder_margintotal').val(formatCurrency(Number(marginCost)));
							
					        }
					    });
				}catch(err){
					console.log(err.message);
				}
			},
			onCellSelect : function (rowid,iCol, cellcontent, e) {
				 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
				 //console.log(e);
				},
			editurl:"./salesOrderController/manpulateSOReleaseLineItem"
	 }).navGrid("#SOlineItemPager", {
			add : false,
			edit : false,
			del : true,
			alertzIndex : 1234,
			search : false,
			refresh : false,
			pager : true,
			alertcap : "Warning",
			alerttext : 'Please select a Product'
		},
		// -----------------------edit// options----------------------//
		{},
		// -----------------------add options-------------------------//
		{},
		// -----------------------Delete options----------------------//
		{
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:12345,
			caption: "Delete Product",
			msg: 'Delete the Product Record?',
			alertzIndex:12345,
			onclickSubmit: function(params){
				var taxRate =$('#customerInvoice_taxId').val();
				var freight = $('#customerInvoice_frightID').val();
				freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
				var cusoid = $('#Cuso_ID').text();
				var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
				var key = jQuery("#SOlineItemGrid").getCell(id, 10);
				cusoid = jQuery("#SOlineItemGrid").getCell(id, 9);
				return { 'cuSodetailId' : key, 'operForAck' : '','taxRate':taxRate,'freight':freight,'cuSoid':cusoid};
				
			},
			afterSubmit:function(response,postData){
				 PreloadData();
				 return [true, loadSOLineItemGrid()];
			}
		}
	);
	 $("#SOlineItemGrid").jqGrid(
				'inlineNav',
				'#SOlineItemPager',
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
							oneditfunc : function() {
								console.log("edited");
								 $("#del_SOlineItemGrid").addClass('ui-state-disabled');
								 $("#new_row_priceMultiplier").val(0);
							},
							successfunc : function(response) {
								console.log("successfunc");
								myTest();
								 $("#SOlineItemGrid").trigger("reloadGrid");
								return true;
							},
							aftersavefunc : function(response) {
								console.log("aftersavefunc");
								myTest();
								 $("#SOlineItemGrid").trigger("reload");
							},
							errorfunc : function(rowid, response) {
								console.log('An Error');
								$("#info_dialog").css("z-index", "1234");
								$(".jqmID1").css("z-index", "1234");
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
							
							return true;
						},
						aftersavefunc : function(id) {
							//alert(" >>>>>>>>>>>> "+$("#"+aSelectedRowId+"_cuSodetailId").val());
							var aSelectedRowId = $("#SOlineItemGrid").jqGrid('getGridParam', 'selrow');
							//alert("aSelectedRowId = "+aSelectedRowId+" || prMasteId = "+$("#"+aSelectedRowId+"_prMasterId").val());
		                	$("#"+aSelectedRowId+"_prMasterId").val();
		                	var q = $("#"+aSelectedRowId+"_quantityOrdered").val();
		                	var u = $("#"+aSelectedRowId+"_unitCost").val();
		                	var p = $("#"+aSelectedRowId+"_priceMultiplier").val();
		                	//alert(q+" || "+u+" || "+p+" || "+$("#"+aSelectedRowId+"_cuSodetailId").val());
		                	if(p==0){
		                		p=1;
		                	}
		                	//alert('P value::'+p);
		                	var t = (q*u)*p;
		                	//alert(t);
//		                	$("#"+aSelectedRowId+"_netCast").val(t);
//		                	$("#new_row_netCast").val(t);
//							 alert("success"+$("#new_row_netCast").val()+ " || "+$("#"+aSelectedRowId+"_netCast").val());
							 var rowData = $('#SOlineItemGrid').jqGrid('getRowData', aSelectedRowId);
					            rowData.netCast = t;
					            $('#SOlineItemGrid').jqGrid('setRowData', aSelectedRowId, rowData);
					            $("#SOlineItemGrid").trigger("reload");
					            var tt = 0;
					            var allRowsInGrid = $('#SOlineItemGrid').jqGrid('getRowData');
								$.each(allRowsInGrid, function(index, value) {
									var ts = value.amount.replace("$","").replace(",","");
									console.log(">>>>> ts = "+ts);
									tt = tt+parseFloat(floorFigureMethod(ts,2));
								});
								var val = (parseFloat(floorFigureMethod(tt,2)) * 8.25)/100;
								console.log("val = "+val);
								$("#customerInvoice_taxvalue").val(formatCurrency(val.toFixed(2)));
								console.log(" customerInvoice_taxvalue val = "+$("#customerInvoice_taxvalue").val());
								$("#customerInvoice_subTotalIDLine").val(formatCurrency(tt.toFixed(2)));
								console.log(" customerInvoice_subTotalIDLine val = "+$("#customerInvoice_subTotalIDLine").val());
								var finalvl = formatCurrency( (tt+val).toFixed(2) );
								console.log(" finalVal = "+finalvl);
					            $("#customerInvoice_totalIDLine").val(finalvl);
//					            $(this).trigger("reloadGrid");
					            $("#del_SOlineItemGrid").removeClass('ui-state-disabled');
					            $("#SOlineItemGrid").trigger("reload");
							
							
							
							
							$("#del_SOlineItemGrid").removeClass('ui-state-disabled');
							console.log('afterSavefunc editparams');
							 $("#SOlineItemGrid").trigger("reloadGrid");
						},
						errorfunc : function(rowid, response) {
							console.log(' editParams -->>>> An Error');
							$("#info_dialog").css("z-index", "1234");
							$(".jqmID1").css("z-index", "1234");
							$("#del_SOlineItemGrid").removeClass('ui-state-disabled');
							$("#SOlineItemGrid").trigger("reload");
//							return false;

						},
						afterrestorefunc : function( id ) {
							$("#del_SOlineItemGrid").removeClass('ui-state-disabled');
							$(this).trigger("reloadGrid");
					    },
						// oneditfunc: setFareDefaults
						oneditfunc : function(id) {
							console.log('OnEditfunc');
							$("#del_SOlineItemGrid").addClass('ui-state-disabled');
							var aSelectedRowId = $("#SOlineItemGrid").jqGrid('getGridParam', 'selrow');
		                	var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
		                	var u = $("#"+aSelectedRowId+"_unitCost").val().replace("$", "");
		                	var p = $("#"+aSelectedRowId+"_priceMultiplier").val().replace("$", "");
		                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
		                	$("#"+aSelectedRowId+"_unitCost").val(u);
		                	$("#"+aSelectedRowId+"_priceMultiplier").val(p);
		                	//alert(" >>>>>>>>>>>> "+$("#"+aSelectedRowId+"_cuSodetailId").val());
		                	//$("#SOlineItemGrid").trigger("reload");
						}
					}
				});
	 if(!isNewIconAdded){
			$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"ui-icon-calculator", onClickButton:ShowNote, position: "last", title:"Edit note for line item", cursor: "pointer"});
			$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"ui-icon-folder-collapsed", onClickButton:ShowTemplateList, position: "last", title:"Add template Line items", cursor: "pointer"});
			$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"", onClickButton:showHideCost, position: "last", title:"Show/Hide Cost", cursor: "pointer"});
			isNewIconAdded = true;
		} 
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
	
};
*/
/*$("#SOlineItemGrid_ilsave").click(function() {
	console.log('SaveButton Clicked');
	myTest();
	return false;
});*/
function myTest(value, colname)
{
 if(mytestmethod_loc_var){
 var val=value;
 var result = null;
 var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
 var row=jQuery("#SOlineItemGrid").getRowData(id);
 var cusoID = $('#cuSOid').val();
 console.log("rowID=="+id);
 var prMasterID = $("#"+id+"_prMasterId").val(); //row['prMasterId'];
 var cuSODetailID = $("#"+id+"_cuSodetailId").val(); //row['cuSodetailId'];
 if(prMasterID=='new_row'){
  prMasterID = $("#new_row_prMasterId").val();
 }
 
 
 $.ajax({
  url: "./salesOrderController/getInventoryAllocatedDetails",
  type: "POST",
  async:false,
  data : {cusoid:cusoID,prmasterid:prMasterID},
  success: function(data) {
   
   
  if(data.isInventory==1 && parseFloat(val)!=0 &&parseFloat(data.inventoryAllocated) < parseFloat(val))
   {   
   //setTimeout(function(){
    "use strict";
    var orgViewModal = $.jgrid.viewModal;
             /*$.extend($.jgrid,{
                 viewModal: function (selector, options) {
                     if (options.onHide) {
                         options.orgOnHide = options.onHide;
                         options.onHide = function (h) {
                          triggerSaveAndAdd();
                             return options.orgOnHide.call(this, h);
                         }
                     }
                     return orgViewModal.call (this, selector, options);
                 }
             });*/
             /*$.jgrid.info_dialog($.jgrid.errors.errcap, "Don't have enough stock for this product",$.jgrid.edit.bClose, {
                    zIndex: 12345,
                    onClose: function () {
                     triggerSaveAndAdd();
                        return true; // allow closing
                    }
                });
                
*/    
    
    var newDialogDiv = jQuery(document.createElement('div'));
     jQuery(newDialogDiv).html('<span style="font-family: Verdana,Arial,sans-serif;"><p>Don&#39;t have enough stock for this product</p></span>');
     jQuery(newDialogDiv).dialog({modal: true, width:330, height:145, title:'Error', 
     closeOnEscape: false,
     open: function(event, ui) { $(".ui-dialog-titlebar-close").hide();$("#closedialog").trigger("click"); setTimeout(function(){$("#closedialog").trigger("click");},50);},
     buttons:{
      "Close": function(){
       jQuery(this).dialog("close");
                       triggerSaveAndAdd();
       $( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
       $("#new_row_itemCode").focus();
       return [true, ''];
      }}}).dialog("open");
   //}, 100);
   //result = [false, ""];   
   mytestmethod_loc_var=false;
   }else{
    mytestmethod_loc_var=true;
    result = [true, ''];
   }
  }
 });
 }else{
  mytestmethod_loc_var=true;
  result = [true, ''];
 }
 return result;
}
function triggerSaveAndAdd(){
	$( "#SOlineItemGrid_ilsave" ).trigger( "click" );
	$( "#SOlineItemGrid_iladd" ).trigger( "click" );
}
function formattax(frieght){
	var subTotal = $('#customerInvoice_subTotalIDLine').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var tax = $('#customerInvoice_taxvalue').val();
	tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
	var total = subTotal+tax+frieght;
	$('#customerInvoice_totalIDLine').val(formatCurrency(total));
}

/*$("#salesorder_whsecost").keyup(function(e) {
	
	var rowId = $("#SOlineItemGrid").jqGrid('getGridParam', 'selrow');
	var id = $("#release").jqGrid('getCell', rowId, 'cuSodetailId');
	var whseCost = parseFloat($(this).val().replace(/[^0-9-.]/g, ''));
	
	var allRowsInGrid = $('#SOlineItemGrid').jqGrid('getRowData');
	var aVal = new Array(); 
	var aTax = new Array();
	
	var sum = 0;
	var sum1 = 0;
	$.each(allRowsInGrid, function(index, value) {
		if(value.cuSodetailId!=id){
		aVal[index] = value.credit;
		var number1 = aVal[index].replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		sum = Number(sum) + Number(number6); 
		}
	});
	
	var commissionData = "cuSodetailId="+id+"&expectedCommission="+$(this).val();
	if (e.keyCode != 8 && e.keyCode != 46) {
	$.ajax({
		url: "./jobtabs5/saveCommissionAmount",
		type: "POST",
		async:false,
		data : commissionData,
		success: function(data){
			//alert(data);
		}
   });

	receivedAmount = sum;
	balanceAmount = CommissionAmount-receivedAmount;
	var number1 = CommissionAmount.replace("$", "");
	var number2 = number1.replace(".00", "");
	$("#expCommissionID").val(number2);
	$("#CommissionReceivedAmount").text(formatCurrency(receivedAmount));
	$("#commissionBalanceAmount").text(formatCurrency(balanceAmount));
	}
	
});
*/

/*function saveLineDetails(){
	var newDialogDiv = jQuery(document.createElement('div'));
	var Frieght = $('#customerInvoice_frightID').val();
	Frieght= parseFloat(Frieght.replace(/[^0-9-.]/g, ''));
	var cuSOID = $('#Cuso_ID').text();
	var subTotal = $('#customerInvoice_subTotalIDLine').val();
	subTotal= parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var Total = $('#customerInvoice_totalIDLine').val();
	Total= parseFloat(Total.replace(/[^0-9-.]/g, ''));
	var taxValue = $('#customerInvoice_taxvalue').val();
	taxValue = parseFloat(taxValue.replace(/[^0-9-.]/g, ''));
	
	var whseCost = $('#salesorder_whsecost').val();
	whseCost = parseFloat(whseCost.replace(/[^0-9-.]/g, ''));
	var whseCostTotal = $('#salesorder_ordercost').val();
	whseCostTotal = parseFloat(whseCostTotal.replace(/[^0-9-.]/g, ''));
	
	//alert('whseCost='+whseCost+' whseCostTotal='+whseCostTotal);
	
	var rowId = $("#SOlineItemGrid").jqGrid('getGridParam', 'selrow');
	var id = $("#SOlineItemGrid").jqGrid('getCell', rowId, 'cuSodetailId');
	
	var SO_LinesSaveDetails = 'cuSOID='+cuSOID+'&frieght='+Frieght+'&subTotal='+subTotal+'&Total='+Total+'&taxValue='+taxValue
	+'&whseCost='+whseCost+'&whseCostTotal='+whseCostTotal+'&cusoDetailID='+id;
	$.ajax({
		url: "./salesOrderController/SaveSOLines",
		type: "POST",
		data : SO_LinesSaveDetails,
		success: function(data) {
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit'|| $('#operation').val() === 'update'){
				if("Save" == $('#setButtonValue').val())
				{
					//var errorText = "Sales Order Lines Tab details are Saved.";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
					PreloadData();
					$('#showMessageLineSO').html("Saved");
					//$('#ShowInfo').html("Saved");
					 $("#SOlineItemGrid").trigger("reloadGrid");
					 $("#release").trigger("reloadGrid");
					setTimeout(function(){
						$('#showMessageLineSO').html("");						
						},3000);
					
				}
				else
				{
					if("SaveandClose" == $('#setButtonValue').val())
					{		
						var errorText = "Sales Order Lines Tab details are Saved.";
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");
						if(typeof(inventory) !== 'undefined' && inventory === 'InventoryPage')
						{
						$('#salesrelease').dialog("close");
						
						$("#inventoryDetailsGrid").trigger("reloadGrid");
						$("#release").trigger("reloadGrid");
						loadInventoryDetailsGrid();
						//$("#addquotesList").trigger("reloadGrid");
						//location.href="./inventoryDetails?token=view&inventoryId="+$("#prMasterID").text() + "&itemCode=" + $("#itemCode").text();
						}
					else
						{
						$('#salesrelease').dialog("close");
						$("#release").trigger("reloadGrid");
						PreloadData();
						}
					}
				}
			
			}
			else
			{
				var errorText = "Sales Order Lines Tab details are Saved.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");
				PreloadData();
			}
			return true;
			
		}
	});
	
}*/

function formatCurrencynodollar(strValue)
{
	if(strValue === "" || strValue == null){
		return "0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10){
		strCents = "0" + strCents;
	}
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	}
	return (((blnSign)?'':'-') + dblValue + '.' + strCents);
}

function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
	console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+cellValue);
	if(cellValue!=undefined){
		cellValue=parseFloat(cellValue.toString()).toFixed(4);
	}
	return cellValue;
}

function line(obj)
{
	if (obj.checked == false)
    {
		withPrice = "NotChecked";
		$('#withPrice').prop('checked', false);
    }
	else
		{
		withPrice = "Checked";
		$('#withPrice').prop('checked', true);
		}
	
	
}
	/*
	 * Created by: Praveenkumar
	 * Date : 2014-09-15
	 * Purpose: To show notes in SO lineitems 
	 */
	
function noteImage(cellValue, options, rowObject){
	var element = '';
	var id="noteImageIcon_"+options.rowId;
	var test=""+options.rowId;
   if(cellValue !== '' && cellValue !== null && cellValue != undefined){
	   element = "<div><div align='center'><img src='./../resources/images/inline_jqGrid1.png' style='vertical-align: middle;' id='"+id+"' onclick=\"ShowNote('"+test+"')\"/></div></div>";	   
   }else{
	   element = "<div><div align='center'><img src='./../resources/images/inline_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick=\"ShowNote('"+test+"')\"/></div></div>";
   }
   return element;
} 
function totalFormatter(cellValue, options, rowObject){
	var rowid = options.rowId;
	var unitCost;
	var priceMultiplier;
	var quantityOrdered;
	if(rowObject['unitCost']!=undefined){
	 unitCost=rowObject['unitCost'];
	 priceMultiplier= rowObject['priceMultiplier'];
	 quantityOrdered = rowObject['quantityOrdered'];
	}else{
		unitCost=$('#'+rowid+'_unitCost').val();
		priceMultiplier=$('#'+rowid+'_priceMultiplier').val();
		quantityOrdered=$('#'+rowid+'_quantityOrdered').val();
	}

//	unitCost=replacetarget(unitCost);
	//alert(rowid +':: '+rowObject['unitCost']+' :: '+rowObject['priceMultiplier']+' :: '+rowObject['quantityOrdered'] );
	if(isNaN(unitCost)){
		unitCost=0;
	}
	if(isNaN(priceMultiplier)){
		priceMultiplier=0;
	}
	if(isNaN(quantityOrdered)){
		quantityOrdered=0;
	}
	if(priceMultiplier==0){
		priceMultiplier=1;
	}
	var total =unitCost*priceMultiplier*quantityOrdered;
	return formatCurrency(total);
	
}
function replacetarget(unitCost){
	unitCost=  unitCost.replace(/[^0-9-.]/g, '')
	return unitCost;
}
/*
 * Created by: Praveenkumar
 * Date : 2014-09-15
 * Purpose: Show Line item Note in popup 
 */

function ShowNote(row){
try{
	
	
	if(typeof(jobStatus) != "undefined")
	{
	CKEDITOR.replace('lineItemNoteID', ckEditorconfigforinline);
	}
	else
	{
	CKEDITOR.replace('lineItemNoteID', ckEditorconfig);
	}
	
	//var rows = jQuery("#SOlineItemGrid").getDataIDs();
	//var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam',row);
	$("#SaveInlineNoteID").attr("onclick","SaveSoLineItemNote('"+row+"');");
	var notes = jQuery("#SOlineItemGrid").jqGrid ('getCell', row, 'note');	  
		CKEDITOR.instances['lineItemNoteID'].setData(notes);
		
		if($("#soStatusButton").val()=="Closed"){
			$("#SaveInlineNoteID").css("display", "none");
		}else{
			$("#SaveInlineNoteID").css("display", "inline-block");
		}
		
		jQuery("#SoLineItemNote").dialog("open");
	//	$(".nicEdit-main").focus();
		return true;
	}catch(err){
		alert(err.message);
	}
}

function SaveSoLineItemNote(row){
	var inlineText=  CKEDITOR.instances["lineItemNoteID"].getData(); 
	
	//var rows = jQuery("#SOlineItemGrid").getDataIDs();
	//var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
//	row=jQuery("#SOlineItemGrid").getRowData(rows[id-1]);
	  /*var notes = row['note'];
	  var cuSodetailId = row['cuSodetailId'];*/
	  //var aLineItem = new Array();
	  //aLineItem.push(inlineText);
	  var image="<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
	  if(inlineText==null || inlineText==undefined || inlineText==""){
		  image=undefined;
		  inlineText=undefined;
	  }
//	  if(isNaN(row)==true || row==undefined){
//		  $("#new_row_noteImage").val(image);
//		  $("#new_row_note").val(inlineText);
//	  }else{
	  	$("#SOlineItemGrid").jqGrid('setCell',row,'note', inlineText);  
		  $("#SOlineItemGrid").jqGrid('setCell',row,'noteImage', image);
		  
//	  }
	  
	  
	  jQuery("#SoLineItemNote").dialog("close");
	  CKEDITOR.instances['lineItemNoteID'].destroy();
	 
	  //aLineItem.push(cuSodetailId);
	/*$.ajax({
		url: "./salesOrderController/saveLineItemNote",
		type: "POST",
		data : {'lineItem' : aLineItem},
		success: function(data) {
			jQuery("#SoLineItemNote").dialog("close");
			$("#SOlineItemGrid").trigger("reloadGrid");
		}
		});*/
}

function SoCancelInLineNote(){
	jQuery("#SoLineItemNote").dialog("close");
	 CKEDITOR.instances['lineItemNoteID'].destroy();
	return false;
}

jQuery(function(){
	jQuery("#SoLineItemNote").dialog({
			autoOpen : false,
			modal : true,
			title:"InLine Note",
			height: 390,
			width: 635,
			buttons : {  },
			close:function(){
				return true;
			}	
	});
});

/*
 * Created by: Praveenkumar
 * Date : 2014-09-15
 * Purpose: Show Sales order template list  
 */
function ShowTemplateList(){
	/*var rows = jQuery("#SOlineItemGrid").getDataIDs();
	var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
	row=jQuery("#SOlineItemGrid").getRowData(rows[id-1]);
	  var notes = row['note'];
	  var cuSodetailId = row['cuSodetailId'];*/
	  
	//alert("hi");	
	//alert($("#new_row_itemCode").val());
	 $('#salesTemplateDiv').dialog('destroy');
	 var itemCode=$("#new_row_itemCode").val();
		 if(itemCode.length==0){
			 jQuery('#salesTemplateDiv').dialog({modal: true, width:300, height:200, title:"Select Template",
				 buttons: [{
				 height:35,
				 text: "Ok",
				 click: function() {
				 if($('#templateListId').val()!=0){
				 //copyTemplate($('#templateListId').val());
				 var cuSOID = $('#Cuso_ID').text();
				 var gridRows = $('#SOlineItemGrid').getRowData();
				 var dataToSend = JSON.stringify(gridRows);
				 var cusoTemplateID=$('#templateListId').val();
				 jQuery("#SOlineItemGrid").jqGrid('setGridParam',{url:"./salesOrderController/copySOTemplate",postData:{"gridData":dataToSend,"cuSOTemplateID":cusoTemplateID,"cuSOID":cuSOID}}).trigger("reloadGrid"); 
				 /*$.ajax({
				 url: "./salesOrderController/copyTemplateLineItems",
				 type: "POST",
				 data : "cuSOID="+cuSOID+"&quantity=1&cuSOTemplateID="+$('#templateListId').val(),
				 success: function(data) {
				 $("#SOlineItemGrid").trigger("reloadGrid");
				 }
				 });*/
				 lineItemgridLoad=false;
				 }
				 $(this).dialog("close");
				
				 }
				 },
				 {
				 height:35,
				 text: "Cancel",
				 click: function() {
					 $(this).dialog("close");
					 //$(this).dialog('destroy').remove();
				 }
				 }
				 ]}).dialog("open");
		 }else{
			 var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes, please save prior to continuing.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
				closeOnEscape: false,
				open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
				buttons:{
					"OK": function(){
						jQuery(this).dialog("close");
						$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
						$("#new_row_quantityOrdered").focus();
					    return false;
					}}}).dialog("open");
		 }
		
	 

}
/*
 * Created by: Praveenkumar
 * Date : 2014-09-15
 * Purpose: Add details to sales order  
 */
function copyTemplate(templateId){
	
	 var newDialogDiv = jQuery(document.createElement('div'));
	  jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<input type="text" id="quantity" /> <label id="errorhidden" style="color:red;"></label><br>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:200, title:"Enter Quantity",
							buttons: [{height:35,text: "Ok",click: function() {
								if(quantity==''){
									$('#errorhidden').text('Please Enter quantity');
									$('#errorhidden').show();
									return false;
								}
								
								var cuSOID = $('#Cuso_ID').text();
								$.ajax({
									url: "./salesOrderController/copyTemplateLineItems",
									type: "POST",
									data : "cuSOID="+cuSOID+"&quantity="+$('#quantity').val()+"&cuSOTemplateID="+templateId,
									success: function(data) {
										$("#SOlineItemGrid").trigger("reloadGrid");
									}
									});
								
								$(this).dialog("close");
								$(this).dialog('destroy').remove();
							}},{height:35,text: "Cancel",click: function() { 
							$(this).dialog("close");
							$(this).dialog('destroy').remove();
							}}]}).dialog("open");
}
/*
 * Created by: Praveenkumar
 * Date : 2014-09-16
 * Purpose: Show / Hide cost  
 */
function showHideCost(){
	if(isShow){
		$('#costdetails').hide();
		isShow = true;
	}else{
		$('#costdetails').show();
		isShow = true;
	}
	
}

/*
 * Created by: Leo
 * Date : 2015-03-24
 * Purpose: Show / Hide cost  
 */
function suggestedorQuotePrice(QuoteOrSuggestedPrice)
{
	var QuotedPricePrMasterID=$("#QuotedPricePrMasterID").val();
	 var gridRows = $('#SOlineItemGrid').getRowData();
	 var dataToSend = JSON.stringify(gridRows);
	 var subTotalIDLine=$("#customerInvoice_subTotalIDLine").val();
	 subTotalIDLine=subTotalIDLine.replace(/[^0-9-.]/g, '');
	 if(subTotalIDLine==null || subTotalIDLine=="" || subTotalIDLine==undefined){
		 subTotalIDLine=0.00;
	 }
	 //alert(QuoteOrSuggestedPrice);
	 if(QuoteOrSuggestedPrice==='SuggestedPrice'){
		// alert(QuoteOrSuggestedPrice);
		 var rowId = $("#release").jqGrid('getGridParam', 'selrow');
		 var allocatedAmount = $("#release").jqGrid('getCell', rowId, 'estimatedBilling').replace(/[^0-9-.]/g, '');
		// alert(allocatedAmount);
		 $("#customerInvoice_subTotalIDLine").val(allocatedAmount);
		 subTotalIDLine = allocatedAmount;
	 }else{
		 $("#customerInvoice_subTotalIDLine").val(subTotalIDLine);
	 }
	 lineItemgridLoad=false;
	 //so_lines_form=[];
	jQuery("#SOlineItemGrid").jqGrid('setGridParam',{url:"./salesOrderController/ApplySuggestedPrice",postData:{"gridData":dataToSend,"QuotedPricePrMasterID":QuotedPricePrMasterID,"SubTotalPrice":subTotalIDLine,"QuoteOrSuggestedPrice":QuoteOrSuggestedPrice}}).trigger("reloadGrid");
	
	/*var cuSOID = $('#cuSOid').val();
	var subTotalIDLine=$("#customerInvoice_subTotalIDLine").val();
	 subTotalIDLine=subTotalIDLine.replace(/[^0-9-.]/g, '');
	 if(subTotalIDLine==null || subTotalIDLine=="" || subTotalIDLine==undefined){
		 subTotalIDLine=0.00;
	 }
		
	$.ajax({
		url: "./salesOrderController/applySuggestedOrder",
		type: "POST",
		data : {"cuSOID":cuSOID},
		success: function(data) {
		     
		     $("#SOlineItemGrid").jqGrid('GridUnload');
		     loadSOLineItemGrid()
			 $("#SOlineItemGrid").trigger("reloadGrid");
		}
		});*/

}

/*function quotedPrice()
{
	var cuSOID = $('#cuSOid').val();
	
	$.ajax({
		url: "./salesOrderController/applyQuotedPrice",
		type: "POST",
		data : {"cuSOID":cuSOID},
		success: function(data) {
		     
		     $("#SOlineItemGrid").jqGrid('GridUnload');
		     loadSOLineItemGrid()
			 $("#SOlineItemGrid").trigger("reloadGrid");
		}
		});
	

}*/

function floorFigureMethod(figure, decimals){
    if (!decimals) decimals = 2;
    var d = Math.pow(10,decimals);
    return (parseFloat(figure*d)/d).toFixed(decimals);
}

function searchImage(cellValue, options, rowObject){
	var element = '';
	if(options.rowId=='new_row'){
		element = "<img src='./../resources/images/search.png' style='vertical-align: middle;' id='openinventorydetailspopup_"+options.rowId+"' onclick='openinventorydetailspopup("+options.rowId+");'>";
	}else{		
		element = "<img src='./../resources/images/search.png' style='vertical-align: middle;' id='openinventorydetailspopup_"+options.rowId+"' onclick='openinventorydetailspopup("+options.rowId+");'>";
	}
   return element;
} 

function openinventorydetailspopup(rowID){
	var title="Inventory Details for "+jQuery("#SOlineItemGrid").jqGrid('getCell', rowID, 'itemCode');
	var prMasterID=jQuery("#SOlineItemGrid").jqGrid('getCell', rowID, 'prMasterId');
	if(isNaN(rowID)==true){
		title=searchProduct;
		prMasterID=searchPrMasterId;
	}else{
		searchProduct=null;
		searchPrMasterId=null;
	}
	if(prMasterID!=null){
		if(prMasterID.length>20){
			prMasterID=$("#"+rowID+"_prMasterId").val();
			title="Inventory Details for "+$("#"+rowID+"_itemCode").val();
		}else{}	
	}else if(prMasterID==null){
		title=null;
		prMasterID=null;
	}else{}
	$('#inventoryDetailsTab').dialog('option', 'title', title);
	preloadInventoryDetail(prMasterID);
	$("#inventoryDetailsTab").dialog("open");
	$( "#SOlineItemGrid_iladd" ).trigger( "click" );
	//alert(rowData["itemCode"]);
}

var posit_job_salesorder=0;
var soLines_selectRow;
var checkelement;
function loadSOLineItemGrid(){	
	var cuSOID = $('#Cuso_ID').text();
	try {
	 $('#SOlineItemGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#SOlineItemPager'),
			url:'./salesOrderController/solineitemGrid',
			postData: {'cuSOID':cuSOID},
			colNames:['Product No', '','Description','Qty','Price Each', 'Mult.', 'Tax','',  'Manu. ID','cuSodetailId', 'prMasterID','SOPopup','Amount','','WhCost','<img src="./../resources/images/delete.png" style="vertical-align: middle;">'],
			colModel :[
		{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', 
			editoptions: {
				dataInit : function(elem) {
					$(elem).autocomplete({
						minLength: 1,timeout :1000,autoFocus: true,
							source: "jobtabs3/productCodeWithNameList",
							select: function( event, ui ){
								var ID = ui.item.id;
								var product = ui.item.label;
								$("#prMasterId").val(ID);
								searchPrMasterId=ID;
								var aSelectedRowId = $("#SOlineItemGrid").jqGrid('getGridParam', 'selrow');
			                	var id = ui.item.id;
			                	var product = ui.item.label;
			                	$("#"+aSelectedRowId+"_prMasterId").val(id);
			                	$("#new_row_prMasterId").val(id);
			                	
			                	var cuSodetailId = ui.item.cuSodetailId;
			                	//$("#"+aSelectedRowId+"_cuSodetailId").val(cuSodetailId);
			                	$("#new_row_cuSodetailId").val(cuSodetailId);
			                	
			                	var celValue = $('#cuSOid').val();
			                	

//			                	alert(id+" || "+celValue+" || "+cuSodetailId);
			                	
			                	//alert(" >>>>>>>> "+$("#new_row_vePoid").val()+ " || "+$("#"+aSelectedRowId+"_vePoid").val());
			                	//alert(id+" || "+product+" || "+aSelectedRowId+"_prMasterId = "+$("#"+aSelectedRowId+"_prMasterId").val()+" || new_row_prMasterId = "+$("#new_row_prMasterId").val());
								$.ajax({
							        url: './getLineItemsSO?prMasterId='+ID,
							        type: 'POST',       
							        success: function (data) {
							        	$.each(data, function(key, valueMap) {										
											
							        		if("lineItems"==key)
											{				
												$.each(valueMap, function(index, value){						
													
													searchProduct=value.itemCode;
													$("#new_row_description").val(value.description);
													$("#"+aSelectedRowId+"_description").val(value.description);
													$("#new_row_unitCost").val(value.lastCost);
													$("#"+aSelectedRowId+"_unitCost").val(value.lastCost);
													if(value.pomult != undefined)
													{
														//parseFloat(cellValue.toString()).toFixed(4)
														$("#new_row_priceMultiplier").val(value.pomult);
														$("#"+aSelectedRowId+"_priceMultiplier").val(parseFloat(value.pomult).toFixed(4));
													}
													else
													{
														$("#new_row_priceMultiplier").val(0);
														$("#"+aSelectedRowId+"_priceMultiplier").val(0);
													}	
													$("#new_row_sopopup").val(value.sopopup);
													$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
													$("#new_row_cuSoid").val(celValue);
													$("#"+aSelectedRowId+"_cuSoid").val(celValue);
													
													if(value.isTaxable == 1)
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
												//$("#new_row_itemCode").val(value.itemCode);
												$("#new_row_description").focus();
												$("#"+aSelectedRowId+"_description").focus();
												/*$("#new_row_quantityOrdered").focus();
												$("#"+aSelectedRowId+"_quantityOrdered").focus();*/
												
												/*
												 * Added by Simon
												 * Reason for Adding : ID#567 (below 2 lines)
												 */
												setproductWareHouseCost(aSelectedRowId,ID);
							                	$("#new_row_quantityOrdered").val("1");
							                	CalculatesoLinegrideditrowvalues(soLines_selectRow);
												
											}	
										});
																			
										
							        }
							    });
								},
							error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
							});
				},dataEvents: [
				       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       			  
				       			var rowobji=$(e.target).closest('tr.jqgrow');
				    			 var textboxid=rowobji.attr('id');
			    			  		 soLines_selectRow=textboxid;
			    			  			//jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			  		jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
				    			  		e.target.select(); 
				       			  } },
				    			  { type: 'click', data: { i: 7 }, fn: function(e) {
				    				  var rowobji=$(e.target).closest('tr.jqgrow');
				    			 var textboxid=rowobji.attr('id');
			    			  		 soLines_selectRow=textboxid;
			    			  			//jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			  		jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
			    			  		
				    				  e.target.select();
				    			  } 
				    			   
				    			  },
			                        {
				                         type: 'keypress',
				                         fn: function(e) {
				                        	 var key = e.which;
				                    		 if(key == 13 || key==9)  // the enter key code
				                    		  {
				                    			// changePosition(soLines_selectRow);
				                    		    //return false;  
				                    		  }
				                         }
				                        }
				    			  ]
			},
			editrules:{custom_func: myTest,edithidden:false,required: true}},
		{name:'noteImage', index:'noteImage', align:'right', width:25,hidden:false, editable:false, formatter:noteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
      	{name:'description', index:'description', align:'left', width:140, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:75,
      		dataEvents: [
		       			  { type: 'focus', data: { i: 7 }, fn: function(e) {
		       				var rowobji=$(e.target).closest('tr.jqgrow');
			    			 var textboxid=rowobji.attr('id');
		    			  		 soLines_selectRow=textboxid;
		    			  			//jQuery("#SOlineItemGrid").jqGrid('resetSelection');
			    			  		jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		       				  e.target.select(); 

		       			  } },
		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
		    				  var rowobji=$(e.target).closest('tr.jqgrow');
		    				  var textboxid=rowobji.attr('id');
			    			  soLines_selectRow=textboxid;
			    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    		  jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		    				  e.target.select();
		    				  changePosition(soLines_selectRow);
		    			  }
		    			   },
		    			  { type: 'change', data: { i: 7 }, fn: function(e) {
		    			  var rowobji=$(e.target).closest('tr.jqgrow');
			    		  var textboxid=rowobji.attr('id');
		    			  soLines_selectRow=textboxid;
		    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
			    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
			    			e.target.select();
		    				changePosition(soLines_selectRow);
		    			  } },
	                        {
		    				  	/*
								 * Added by Simon
								 * Reason for Adding : ID#567
								 */
		                         type: 'keypress',
		                         fn: function(e) {
		                        	 var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 searchPrMasterId=null;
		                 			     searchProduct=null;
		                    			 CalculatesoLinegrideditrowvalues(soLines_selectRow);
		                    			 $("#SOlineItemGrid_ilsave").trigger("click");
		                 			    $( "#SOlineItemGrid_iladd" ).trigger( "click" );
		                    		    return false;  
		                    		  }
		                         }
		                        }
		    			  ]
      	},editrules:{custom_func: myTest,edithidden:false},  
			cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',
			dataEvents: [
				  			  { type: 'focus', data: { i: 7 }, fn: function(e) {
				  				 var rowobji=$(e.target).closest('tr.jqgrow');
					    		  var textboxid=rowobji.attr('id');
				    			  soLines_selectRow=textboxid;
				    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
					    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
				  			  CalculatesoLinegrideditrowvalues(soLines_selectRow);  
				  			  console.log("test target select");
				  			  e.target.select();
				  			  } },
							  { type: 'click', data: { i: 7 }, fn: function(e) {  
								  CalculatesoLinegrideditrowvalues(soLines_selectRow);
								  var rowobji=$(e.target).closest('tr.jqgrow');
					    		  var textboxid=rowobji.attr('id');
				    			  soLines_selectRow=textboxid;
				    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
					    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
			    				  //changePosition(soLines_selectRow);
							  console.log("test target  click");
							  e.target.select();
			    			  } },
	                        {
	                         type: 'change',
	                         fn: function(e) {
	                               CalculatesoLinegrideditrowvalues(soLines_selectRow);
	                               	/*
									 * Added by Simon
									 * Reason for Adding : ID#567
									 */
	                               //changePosition(soLines_selectRow);
	                         }
	                        },
	                        {
	                        	/*
								 * Added by Simon
								 * Reason for Adding : ID#567
								 */
		                         type: 'keypress',
		                         fn: function(e) {
		                        	 var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 searchPrMasterId=null;
		                 			     searchProduct=null;
		                    			 CalculatesoLinegrideditrowvalues(soLines_selectRow);
		                    			 $("#SOlineItemGrid_ilsave").trigger("click");
		                 			    $( "#SOlineItemGrid_iladd" ).trigger( "click" );
		                    		    return false;  
		                    		  }
		                         }
		                        }
	                    ],decimalPlaces: 2
			},
			editrules:{custom: true, custom_func: myTest,edithidden:true,required: true}},
		{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right',
			dataEvents: [
			             { type: 'focus', data: { i: 7 }, fn: function(e) { 
			            	 var rowobji=$(e.target).closest('tr.jqgrow');
				    		  var textboxid=rowobji.attr('id');
			    			  soLines_selectRow=textboxid;
			    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		    				 // changePosition(soLines_selectRow);
			             CalculatesoLinegrideditrowvalues(soLines_selectRow);
			             e.target.select(); 
			             } },
						  { type: 'click', data: { i: 7 }, fn: function(e) { 
							  CalculatesoLinegrideditrowvalues(soLines_selectRow);
							  var rowobji=$(e.target).closest('tr.jqgrow');
				    		  var textboxid=rowobji.attr('id');
			    			  soLines_selectRow=textboxid;
			    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		    				 // changePosition(soLines_selectRow);
		    			  e.target.select();
		    			  } },
	                        {
	                         type: 'change',
	                         fn: function(e) {
	                               CalculatesoLinegrideditrowvalues(soLines_selectRow);
	                         }
	                        },
	                        {
	                        	/*
								 * Added by Simon
								 * Reason for Adding : ID#567
								 */
		                         type: 'keypress',
		                         fn: function(e) {
		                        	 var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 searchPrMasterId=null;
		                 			    searchProduct=null;
		                    			 CalculatesoLinegrideditrowvalues(soLines_selectRow);
		                    			 $("#SOlineItemGrid_ilsave").trigger("click");
		                 			    $( "#SOlineItemGrid_iladd" ).trigger( "click" );
		                    		    return false;  
		                    		  }
		                         }
		                        }
	                    ],decimalPlaces: 2	
		},editrules:{edithidden:true}},
		{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:20,hidden:false, editable:true, editoptions:{size:15, alignText:'right',
			dataEvents: [
			             { type: 'focus', data: { i: 7 }, fn: function(e) { 
			             CalculatesoLinegrideditrowvalues(soLines_selectRow);
			             var rowobji=$(e.target).closest('tr.jqgrow');
			    		  var textboxid=rowobji.attr('id');
		    			  soLines_selectRow=textboxid;
		    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
			    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		    			  e.target.select();
		    			  } },
						  { type: 'click', data: { i: 7 }, fn: function(e) {
							  
							  var rowobji=$(e.target).closest('tr.jqgrow');
				    		  var textboxid=rowobji.attr('id');
			    			  soLines_selectRow=textboxid;
			    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
							  CalculatesoLinegrideditrowvalues(soLines_selectRow);
		    				  //changePosition(soLines_selectRow);
						  e.target.select(); 
						  }},
	                        {
	                         type: 'change',
	                         fn: function(e) {
	                               CalculatesoLinegrideditrowvalues(soLines_selectRow);
	                         }
	                        },
	                        /*
							 * Added by Simon
							 * Reason for Adding : ID#567
							 */
	                        {
		                         type: 'keypress',
		                         fn: function(e) {
		                        	 var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			searchPrMasterId=null;
		                 			    searchProduct=null;
		                    			 CalculatesoLinegrideditrowvalues(soLines_selectRow);
		                    			 $("#SOlineItemGrid_ilsave").trigger("click");
		                 			    $( "#SOlineItemGrid_iladd" ).trigger( "click" );
		                    		    return false;  
		                    		  }
		                         }
		                        }
	                    ],decimalPlaces: 4
			
		}, formatter:customCurrencyFormatterWithoutDollar, editrules:{edithidden:true}},
		{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
		{name:'note', index:'note', align:'right', width:10,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'cuSoid', index:'cuSoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'cuSodetailId', index:'cuSodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'prMasterId',index:'prMasterId',     align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'sopopup', index:'sopopup', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
		{name:'amount', index:'amount', align:'right', width:50,hidden:false, editable:false, editoptions:{size:15, alignText:'right',
			dataEvents: [
		       			  { type: 'focus', data: { i: 7 }, fn: function(e) {
		       				 var rowobji=$(e.target).closest('tr.jqgrow');
				    		  var textboxid=rowobji.attr('id');
			    			  soLines_selectRow=textboxid;
			    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		       				  e.target.select();
		       				  } },
		    			  { type: 'click', data: { i: 7 }, fn: function(e) {
		    				  var rowobji=$(e.target).closest('tr.jqgrow');
				    		  var textboxid=rowobji.attr('id');
			    			  soLines_selectRow=textboxid;
			    			  //jQuery("#SOlineItemGrid").jqGrid('resetSelection');
				    			jQuery("#SOlineItemGrid").jqGrid('setSelection',soLines_selectRow, true);
		    				  e.target.select(); 
		    				  } }
		    			  ]
		},editrules:{edithidden:false},formatter:totalFormatter},
		{name:'searchImage', index:'searchImage', align:'right', width:10,hidden:false, editable:false, formatter:searchImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'whseCost',index:'whseCost',align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'canDeleteSO', index:'canDeleteChkBox', align:'center',  width:20, hidden:false, editable:false, formatter:canDeleteSOCheckboxFormatter,   editrules:{edithidden:true}}
		],
			rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
			sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
			height:482.5,	width: 750, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
				posit_job_salesorder= jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
			},
			loadComplete: function(data) {
				$("#SOlineItemGrid").setSelection(1, true);
				var ids = $('#SOlineItemGrid').jqGrid('getDataIDs');
				//$('#SOlineItemGrid_noteImage').removeClass("ui-state-default ui-th-column ui-th-ltr");			
			    /*if (ids.length>0) {
			    	console.log("if loop");
			        var sortName = $('#SOlineItemGrid').jqGrid('getGridParam','noteImage');
			        var sortOrder = $('#SOlineItemGrid').jqGrid('getGridParam','description');
			        for (var i=0;i<ids.length;i++) {
			        	
			        	$('#SOlineItemGrid').jqGrid('setCell', ids[i], 'noteImage', '', '',
			                        {style:'border-right-color: transparent !important;'});
			        	$('#SOlineItemGrid').jqGrid('setCell', ids[i], 'description', '', '',
		                        {style:'border-left-color: transparent !important;'});
			        }
			    }*/
			    mytestmethod_loc_var=true;
			    /*
				 * Added by Simon
				 * Reason for Adding : ID#567
				 */
			    $( "#SOlineItemGrid_iladd" ).trigger( "click" );
			    $("#salesorder_whsecost").val(formatCurrency("0.00"));
			    //$(".ui-icon-folder-collapsed").css({"background":"url('./../resources/images/jqGrid_template_icon.png')"});
			    /*
				 * Added by Simon
				 * Reason for Adding : ID#567
				 */
			    if(salesOrderFlag==null){			    	
			    	$(".ui-icon-folder-collapsed").css({"width":"76px","height":"21.5px","background":"url('./../resources/images/inline_template5.png')","position":"inherit","top":"-2px"});
			    }else{
			    	$(".ui-icon-folder-collapsed").css({"width":"76px","height":"21.5px","background":"url('./../resources/images/inline_template.PNG')","position":"inherit","top":"-2px"});			    	
			    }
			    //$(".ui-icon .ui-icon-folder-collapsed").css({"width":"68px","height":"16px","background-image":"url('./../resources/images/jqGrid_template_icon.png')"});
			    searchPrMasterId=null;
			    searchProduct=null;
			    
	             if(lineItemgridLoad){
	            	 PreloadData();
	            	 var gridRows = $('#SOlineItemGrid').getRowData();
		             so_lines_form =  JSON.stringify(gridRows);
		             lineItemgridLoad=true;
	             }
	             setsoLinegridTotal();
	             soLineItemformChanges();
			},
			gridComplete: function () {
				 $('#loadingDivForSOGeneralTab').css({
						"display": "none"
					});
	             jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_salesorder);
	             //posit_job_salesorder=0;
	             searchProduct=null;
	         	 searchPrMasterId=null;
	             
	             $("#SaveLineSOReleaseID").prop("disabled",false);
	             //$( "#SOlineItemGrid_iladd" ).trigger( "click" );
			   },
			loadError : function (jqXHR, textStatus, errorThrown){},
			onSelectRow:  function(id){
				
				soLines_selectRow=id;
				jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
				var rowData = jQuery(this).getRowData(id); 
				var cuSODetailId = rowData["cuSodetailId"];
				$('#jobCustomerName_ID').text(cuSODetailId);
				setshowWarehouseCost(id);
				 posit_job_salesorder= jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
				/*try{
					var cusoid = $('#Cuso_ID').text();
					var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
					row=jQuery("#SOlineItemGrid").getRowData(id);
					var prMasterID = $("#"+id+"_prMasterId").val(); //row['prMasterId'];
					var cuSODetailID = $("#"+id+"_cuSodetailId").val(); //row['cuSodetailId'];
					var quantity =  $("#"+id+"_quantityOrdered").val();
					
					 Added by Zenith for Get Correct warehouse Prices 
					if(cuSODetailID===undefined){
						cuSODetailID = rowData["cuSodetailId"];
					}
					if(quantity===undefined){
						quantity = rowData["quantityOrdered"];
					}
					
					if(prMasterID===undefined){
						prMasterID = rowData["prMasterId"];
					}
					var subtotal = ($('#customerInvoice_subTotalIDLine').val()).replace(/[^0-9-.]/g, '');
					
					if(cuSODetailID != null && cuSODetailID != "" && cuSODetailID != undefined)
						if(quantity){
							
						}
						$.ajax({
					        url: './salesOrderController/getSOPriceDetails',
					        data:{"cusoid":cusoid,"prMasterId":prMasterId,"cuSODetailID":cuSODetailID,"Quantity":quantity},
					        type: 'POST',       
					        success: function (data) {								
								console.log(data.product);
								console.log(data.cost);
								var marginCost = parseFloat(subtotal)-parseFloat(data.cost);
								$('#salesorder_whsecost').val(formatCurrency(parseFloat(data.product)/parseFloat(quantity)));
								$('#salesorder_ordercost').val(formatCurrency(data.cost));
								console.log(subtotal+' :: '+data.cost+'::'+marginCost)
								$('#salesorder_margintotal').val(formatCurrency(Number(marginCost)));
							
					        }
					    });
				}catch(err){
					console.log(err.message);
				}*/
			},
			ondblClickRow: function(rowid) {
				/* $("#SOlineItemGrid").jqGrid('resetSelection');
				 jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');*/
				 //$("#SOlineItemGrid").jqGrid("setSelection", rowid, true);
				/*
				 * Added by Simon
				 * Reason for Adding : ID#567
				 */
				 if(rowid=="new_row"){
					 
				 }else{
					 /*if($("#new_row_itemCode").val()==undefined){
						 $("#SOlineItemGrid_ilsave").trigger("click");
					     $("#SOlineItemGrid_iledit").trigger("click");
					 }else{
						 $("#SOlineItemGrid_ilcancel").trigger("click");
					     $("#SOlineItemGrid_iledit").trigger("click");
					 } */
					 
					 posit_job_salesorder= jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
					 $("#SOlineItemGrid_ilcancel").trigger("click");
				     $("#SOlineItemGrid_iledit").trigger("click");
				 }
			   },
			onCellSelect : function (rowid,iCol, cellcontent, e) {
				 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
				 //console.log(e);
			},
			//editurl:"./salesOrderController/manpulateSOReleaseLineItem"
				cellsubmit: 'clientArray',
				editurl: 'clientArray',
	 }).navGrid("#SOlineItemPager", {
			add : false,
			edit : false,
			del : false,
			alertzIndex : 1234,
			search : false,
			refresh : false,
			pager : true,
			alertcap : "Warning",
			alerttext : 'Please select a Product'
		},
		// -----------------------edit// options----------------------//
		{},
		// -----------------------add options-------------------------//
		{},
		// -----------------------Delete options----------------------//
		{}
	);
	 $("#SOlineItemGrid").jqGrid(
				'inlineNav',
				'#SOlineItemPager',
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
							oneditfunc : function() {
								//$("#del_SOlineItemGrid").addClass('ui-state-disabled');
								$("#new_row_priceMultiplier").val(0);
								if(isShow){
									$('#costdetails').hide();
									isShow = false;
								}
								//posit_job_salesorder= jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
							},
							successfunc : function(response) {
								return true;
							},
							aftersavefunc : function(response) {
								var ids = $("#SOlineItemGrid").jqGrid('getDataIDs');
								var veaccrrowid;
								if(ids.length==1){
									veaccrrowid = 0;
								}else{
									var idd = jQuery("#SOlineItemGrid tr").length;
									for(var i=0;i<ids.length;i++){
										if(idd<ids[i]){
											idd=ids[i];
										}
									}
									 veaccrrowid= idd;
									 $("#new_row_itemCode").focus();
								}
								if(soLines_selectRow=="new_row"){
								$("#" + soLines_selectRow).attr("id", Number(veaccrrowid)+1);
								var changeidnum=Number(veaccrrowid)+1;
								$("#canDeleteSOID_new_row").attr("id","canDeleteSOID_"+changeidnum);
								$("#openinventorydetailspopup_new_row").attr("id","openinventorydetailspopup_"+changeidnum);
								$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+changeidnum);
								//$("#openinventorydetailspopup_new_row").attr("id", Number(veaccrrowid)+1);openinventorydetailspopup_
//								$("#canDeleteSOID_new_row").attr("onclick","setsoLinegridTotal();deleteSOCheckboxChanges("+(Number(veaccrrowid)+1)+")");
								$("#openinventorydetailspopup_"+changeidnum).attr("onclick","openinventorydetailspopup("+changeidnum+")");
								$("#canDeleteSOID_"+changeidnum).attr("onclick","deleteRowFromJqGrid("+changeidnum+");setsoLinegridTotal();soLineItemformChanges();");
								$("#noteImageIcon_"+changeidnum).attr("onclick","ShowNote('"+changeidnum+"');");
								
								setshowWarehouseCost(Number(veaccrrowid)+1);
								}else{
									setshowWarehouseCost(soLines_selectRow);
								}
								
								
								/*var grid=$("#SOlineItemGrid");
								grid.jqGrid('resetSelection');
							    var dataids = grid.getDataIDs();
							    for (var i=0, il=dataids.length; i < il; i++) {
							        grid.jqGrid('setSelection',dataids[i], false);
							    }*/
//							    $("#del_SOlineItemGrid").removeClass('ui-state-disabled');
								setsoLinegridTotal();
								soLineItemformChanges();
								 $( "#SOlineItemGrid_iladd" ).trigger( "click" );
								 $("#SOlineItemGrid").jqGrid('resetSelection');
								 var grid=$("#SOlineItemGrid");
									grid.jqGrid('resetSelection');
								    var dataids = grid.getDataIDs();
								    for (var i=0, il=dataids.length; i < il; i++) {
								        grid.jqGrid('setSelection',dataids[i], false);
								    }
								 $("#SOlineItemGrid").jqGrid('setSelection','new_row', true);
							},
							errorfunc : function(rowid, response) {
								//$("#info_dialog").css("z-index", "1234");
								
								$("#info_dialog").css({"z-index":"1500","border":"1px solid"})
								$(".jqmID1").css("z-index", "1234");
								return false;
							},
							afterrestorefunc : function(rowid) {
								
							}
						}
					},
					editParams : {
						keys : false,
						successfunc : function(response) {
							return true;
						},
						aftersavefunc : function(id) {
							//var priceMultiplier=$("#"+id+"_priceMultiplier").val();
							var ids = $("#SOlineItemGrid").jqGrid('getDataIDs');
							var veaccrrowid;
							if(ids.length==1){
								veaccrrowid = 0;
							}else{
								var idd = jQuery("#SOlineItemGrid tr").length;
								for(var i=0;i<ids.length;i++){
									if(idd<ids[i]){
										idd=ids[i];
									}
								}
								 veaccrrowid= idd;
								 $("#new_row_itemCode").focus();
							}
							if(soLines_selectRow=="new_row"){
							$("#" + soLines_selectRow).attr("id", Number(veaccrrowid)+1);
							var changeidnum=Number(veaccrrowid)+1;
							$("#canDeleteSOID_new_row").attr("id","canDeleteSOID_"+changeidnum);
							$("#openinventorydetailspopup_new_row").attr("id","openinventorydetailspopup_"+changeidnum);
							$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+changeidnum);
							$("#canDeleteSOID_"+changeidnum).attr("onclick","deleteRowFromJqGrid("+changeidnum+");setsoLinegridTotal();soLineItemformChanges();");
							$("#openinventorydetailspopup_"+changeidnum).attr("onclick","openinventorydetailspopup("+changeidnum+")");
							$("#noteImageIcon_"+changeidnum).attr("onclick","ShowNote('"+changeidnum+"');");
							
							//$("#canDeleteSOID_new_row").attr("onclick","setsoLinegridTotal();deleteSOCheckboxChanges("+(Number(veaccrrowid)+1)+")");
							//$("#openinventorydetailspopup_new_row").attr("onclick","openinventorydetailspopup("+(Number(veaccrrowid)+1)+")");
							setshowWarehouseCost(Number(veaccrrowid)+1);
							}else{
								setshowWarehouseCost(soLines_selectRow);
							}
							
							
							
							
							setsoLinegridTotal();
							soLineItemformChanges();
							$( "#SOlineItemGrid_iladd" ).trigger( "click" );
							$("#SOlineItemGrid").jqGrid('resetSelection');
							var grid=$("#SOlineItemGrid");
							grid.jqGrid('resetSelection');
						    var dataids = grid.getDataIDs();
						    for (var i=0, il=dataids.length; i < il; i++) {
						        grid.jqGrid('setSelection',dataids[i], false);
						    }
							$( "#SOlineItemGrid" ).jqGrid('setSelection','new_row', true);
							//$("#del_SOlineItemGrid").removeClass('ui-state-disabled');
							
						},
						errorfunc : function(rowid, response) {
							$("#info_dialog").css({"z-index":"1500","border":"1px solid"})
						},
						afterrestorefunc : function( id ) {
							
						},
						oneditfunc : function(id) {
							console.log('OnEditfunc'+id);
							var unitcost=$("#"+id+"_unitCost").val();
							unitcost=unitcost.replace(/[^0-9\-.]+/g,"");
							if(unitcost==undefined ||unitcost==""||unitcost==null){
								unitcost=0.00;
							}
							var amount=$( "#SOlineItemGrid").jqGrid("getCell",id,'amount');
							amount=amount.replace(/[^0-9\-.]+/g,"");
							if(amount==undefined ||amount==""||amount==null){
								amount=0.00;
							}
							$("#"+id+"_amount").val(amount);
							$("#"+id+"_unitCost").val(unitcost);
							
							if(isShow){
								$('#costdetails').hide();
								isShow = false;
							}
							//posit_job_salesorder= jQuery("#SOlineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
						}
					},restoreAfterSelect :false
				});
	//Drag And DROP
		jQuery("#SOlineItemGrid").jqGrid('sortableRows');
		jQuery("#SOlineItemGrid").jqGrid('gridDnD');
		/*
		 * Added by Simon
		 * Reason for Adding : ID#567 (below 5 lines)
		 */
		$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"ui-icon-folder-collapsed", onClickButton:ShowTemplateList, position: "last", title:"Add template Line items", cursor: "pointer"});
		$("#SOlineItemGrid_ilsave").css({"display":"none"});
		$("#SOlineItemGrid_iladd").css({"display":"none"});
		$("#SOlineItemGrid_iledit").css({"display":"none"});
		$("#SOlineItemGrid_ilcancel").css({"display":"none"});
		
		//jQuery("#SOlineItemGrid").jqGrid('editGridRow',"newRowId",{height:280,reloadAfterSubmit:false});
		/*jQuery("#SOlineItemGrid").jqGrid('setSelection', "newRowId");  // select new row
		jQuery("#SOlineItemGrid").jqGrid('editRow', "newRowId", true); // editing the row
*/	 
		
		/*if(!isNewIconAdded){
			$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"ui-icon-calculator", onClickButton:ShowNote, position: "last", title:"Edit note for line item", cursor: "pointer"});
			$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"ui-icon-folder-collapsed", onClickButton:ShowTemplateList, position: "last", title:"Add template Line items", cursor: "pointer"});
			$('#SOlineItemGrid').jqGrid('navButtonAdd',"#SOlineItemPager",{ caption:"", buttonicon:"", onClickButton:showHideCost, position: "last", title:"Show/Hide Cost", cursor: "pointer"});
			isNewIconAdded = true;
		} */
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
	
	/*
	 * Edited by Simon
	 * Reason for Adding : ID#567
	 */
	$("#SOlineItemGrid_iladd").click(function() {
			/*if($("#release").val()==undefined){
				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
			}else{
				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
			}
			//document.getElementById("SaveLineSOReleaseID").disabled = true;
			//document.getElementById("closeLineSOReleaseID").disabled = true;
			document.getElementById("SOReleaseQuotedPriceID").disabled = true;
			document.getElementById("SOReleaseSuggestedPriceID").disabled = true;*/
		});
		/*
		 * Edited by Simon
		 * Reason for Adding : ID#567
		 */
		$("#SOlineItemGrid_iledit").click(function() {
			/*if($("#release").val()==undefined){
				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	 			
			}else{
				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
				$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
			}
			//document.getElementById("SaveLineSOReleaseID").disabled = true;
			//document.getElementById("closeLineSOReleaseID").disabled = true;
			document.getElementById("SOReleaseQuotedPriceID").disabled = true;
			document.getElementById("SOReleaseSuggestedPriceID").disabled = true;*/
		});

		/*
		 * Edited by Simon
		 * Reason for Adding : ID#567
		 */
		$("#SOlineItemGrid_ilsave").click(function() {
			searchProduct=null;
			searchPrMasterId=null;
			/*
			$("#info_dialog").css({"z-index":"1500","border":"1px solid"})
			
			if($("#new_row_prMasterId").val()!=undefined && $("#new_row_prMasterId").val()=="")
			$("#infocnt").text("Invalid Product. Please select product from the list");
			
			if($("#info_head").text()!="Error")
			{
			document.getElementById("SaveLineSOReleaseID").disabled = false;
			document.getElementById("closeLineSOReleaseID").disabled = false;
			document.getElementById("SOReleaseQuotedPriceID").disabled = false;
			document.getElementById("SOReleaseSuggestedPriceID").disabled = false;
				if($("#release").val()==undefined){
					$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
		 			$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
		 			$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
		 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
		 		   
				}else{
					$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
					$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
					$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
				   
				}
			}
			
			 */});


		/*
		 * Edited by Simon
		 * Reason for Adding : ID#567
		 */
		$("#SOlineItemGrid_ilcancel").click(function() {/*
			document.getElementById("SaveLineSOReleaseID").disabled = false;
			document.getElementById("closeLineSOReleaseID").disabled = false;
			document.getElementById("SOReleaseQuotedPriceID").disabled = false;
			document.getElementById("SOReleaseSuggestedPriceID").disabled = false;
			if($("#release").val()==undefined){
				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	 			$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	 			$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	 		   
			}else{
				$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
				$('#closeLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
				$('#SOReleaseQuotedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	 			$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			}
			*/});
}

function CalculatesoLinegrideditrowvalues(editrowid){
	   var unitCost=$("#"+editrowid+"_unitCost" ).val()+"";
		 unitCost=unitCost.replace(/[^0-9\.-]+/g,"");
		 if(unitCost==""){
			 unitCost=0;
		 }else if(unitCost<0){
			 var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b>Cost cant be negative</b></span>');
				jQuery(newDialogDiv).dialog({modal: false, width:300, height:150, title:"Error", 
				buttons:{
					"Close": function(){
						 $("#"+editrowid+"_unitCost").val('');
						 //$("#"+editrowid+"_amount").val('0.00');
						 //$("#" + editrowid).find('td').eq('13').html('$0.00');
						 $( "#SOlineItemGrid").jqGrid("setCell",editrowid,'amount','0.00');
						 $("#"+editrowid+"_unitCost").focus();
						jQuery(this).dialog("close");
			
					}
				}
				});
				
				 $("#"+editrowid+"_unitCost").val('');
				 //$("#"+editrowid+"_amount").val('0.00');
				 //$("#" + editrowid).find('td').eq('13').html('$0.00');
				 $( "#SOlineItemGrid").jqGrid("setCell",editrowid,'amount','0.00');
				 $("#"+editrowid+"_unitCost").focus();
			  
			  return;
				/* setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
				result = [false, 'Quantity should not less than 0'];
				return result;*/
		 }
		 unitCost=Number(floorFigureoverall(unitCost,2));
		 
		 
		 var multiplier=$("#"+editrowid+"_priceMultiplier" ).val();
		 if(isNaN(multiplier)||multiplier==""||multiplier==0){
			 multiplier=0;
			}else{
				multiplier=Round_priceMultiplier(multiplier);
				 $("#"+editrowid+"_priceMultiplier").val(/*formatCurrency(*/multiplier/*)*/);
			}
		 
		 var quantity=$("#"+editrowid+"_quantityOrdered" ).val();
		 if(isNaN(quantity)||quantity==""||quantity==0){
			 quantity=0;
		 }else{
			 quantity=Number(floorFigureoverall(quantity,2));
		 }
		 
	   var amount=unitCost*quantity;
		 if(multiplier!=0){
			 amount=multiplier*amount;
		 }
		 amount=Number(floorFigureoverall(amount, 2));
		 $("#"+editrowid+"_quantityOrdered").val(quantity);
		 $("#"+editrowid+"_unitCost").val(unitCost);
		 //$("#"+editrowid+"_amount" ).val(amount);
		 //$("#" + editrowid).find('td').eq('13').html("$"+parseFloat(amount));
		 $( "#SOlineItemGrid").jqGrid("setCell",editrowid,'amount',amount);
	    	 
	}
function setsoLinegridTotal(){
	 var ids = $("#SOlineItemGrid").jqGrid('getDataIDs'); 
	 var totalamount=0;
	 var taxamount=0;
	 var taxcellValue;
	 var taxpercentage=$('#customerInvoice_taxId').val();
	 if(taxpercentage==null||taxpercentage==""||taxpercentage==undefined){
		 taxpercentage=0;
	 }
		if(taxpercentage.length>0){
			taxpercentage=Number(floorFigureoverall(taxpercentage,2));
		}
		
	 for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		 cellValue =$("#SOlineItemGrid").jqGrid ('getCell', selectedRowId, 'amount');
		 taxcellValue=$("#SOlineItemGrid").jqGrid ('getCell', selectedRowId, 'taxable');
		 var id="#canDeleteSOID_"+selectedRowId;
		 var canDo=$(id).is(':checked');
		 if(taxcellValue=="Yes"){
			 var eachamount=parseFloat(floorFigureoverall(cellValue.replace(/[^0-9\.-]+/g,""),2));
			 var multiplyamount=eachamount*Number(taxpercentage)/100;
			 if(!canDo){
			 taxamount=Number(taxamount)+Number(multiplyamount);
			
			 }
		 }
		var cellvalueamt=Number(parseFloat(cellValue.replace(/[^0-9\.-]+/g,"")).toFixed(3));
		//alert(parseFloat(cellValue.replace(/[^0-9\.-]+/g,"")).toFixed(2));
		if(!canDo){
		 totalamount=cellvalueamt+totalamount;
		 totalamount=Number(floorFigureoverall(totalamount,2));
		}
	 }
	 var freight=$("#customerInvoice_frightID").val();
	 freight=freight.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 var taxchecked=$("#so_taxfreight").val()==1?true:false;
	 var taxforfreight=0;
	 if(taxpercentage>0  && taxchecked && freight!=null &&freight>0){
		 taxforfreight=taxpercentage*freight/100;
		 taxamount=taxamount+taxforfreight;
	 }
	 
	 if(isNaN(totalamount)){
		 totalamount=0;
	 }
	 //floorFigureoverall(
	 $("#customerInvoice_subTotalIDLine").val(formatCurrency(totalamount,2));
	
	 
	 if(taxamount<0)
		 taxamount=-taxamount;
	 $("#customerInvoice_taxvalue").val(formatCurrency(taxamount));
	 

	 var overalltotal=Number(totalamount)+Number(floorFigureoverall(freight,2))+Number(floorFigureoverall(taxamount,2));
	 $("#customerInvoice_totalIDLine").val(formatCurrency(overalltotal));
	 

}

function setproductWareHouseCost(selrowId,prMasterID){
	$.ajax({
		url: "./salesOrderController/getproductWareHouseCost",
		type: "POST",
		async:false,
		data :{"prMasterID":prMasterID},
		success: function(data) {
			$("#"+selrowId+"_whseCost").val(data);
		}
	});
}
function setshowWarehouseCost(id){
	if(id!="new_row"){
		var row=jQuery("#SOlineItemGrid").getRowData(id);
		var productCost=row['whseCost'];
		if(productCost==null || productCost=="" || productCost==undefined){
			productCost=0.00;
		}
		$("#salesorder_whsecost").val(formatCurrency(productCost));
	}else{
		$("#salesorder_whsecost").val(formatCurrency(0));
	}
	var rows = jQuery("#SOlineItemGrid").getDataIDs();
	var grandTotal = 0;
	 for(a=0;a<rows.length-1;a++)
	 {
	    rowdata=jQuery("#SOlineItemGrid").getRowData(rows[a]);
	    var eachproductCost=rowdata['whseCost'];
	    if(eachproductCost==null || eachproductCost=="" || eachproductCost==undefined){
	    	eachproductCost=0.00;
		}
	    var quantity=rowdata['quantityOrdered'];
	    if(quantity==null || quantity=="" || quantity==undefined){
	    	quantity=0.00;
		}
	    var warehsecost=Number(eachproductCost)*Number(quantity);
	    grandTotal=grandTotal+parseFloat(warehsecost);
	      }
	 $("#salesorder_ordercost").val(formatCurrency(grandTotal));
	 var subTotalIDLine=$("#customerInvoice_subTotalIDLine").val();
	 subTotalIDLine=subTotalIDLine.replace(/[^0-9-.]/g, '');
	 if(subTotalIDLine==null || subTotalIDLine=="" || subTotalIDLine==undefined){
		 subTotalIDLine=0.00;
	 }
	 var margintotal=Number(subTotalIDLine)-Number(grandTotal);
	 $("#salesorder_margintotal").val(formatCurrency(margintotal));
	
}


function saveLineDetails(popupdetail){
	 var itemCode=$("#new_row_itemCode").val();
//	 if(itemCode.length==0){
	 if(itemCode!=undefined){
		 var newDialogDiv = jQuery(document.createElement('div'));
			var Frieght = $('#customerInvoice_frightID').val();
			Frieght= parseFloat(Frieght.replace(/[^0-9-.]/g, ''));
			var cuSOID = $('#Cuso_ID').text();
			var subTotal = $('#customerInvoice_subTotalIDLine').val();
			subTotal= parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
			var Total = $('#customerInvoice_totalIDLine').val();
			Total= parseFloat(Total.replace(/[^0-9-.]/g, ''));
			var taxValue = $('#customerInvoice_taxvalue').val();
			taxValue = parseFloat(taxValue.replace(/[^0-9-.]/g, ''));
			
		 	var withPriceLineStatus = 0;
		 	if($("#withPriceLine").is(":checked")){
		 		withPriceLineStatus=1;
		 	}else{}
			var SO_LinesSaveDetails = 'cuSOID='+cuSOID+'&frieght='+Frieght+'&subTotal='+subTotal+'&Total='+Total+'&taxValue='+taxValue;
			var gridRows = $('#SOlineItemGrid').getRowData();
			var dataToSend = JSON.stringify(gridRows);
			
			$.ajax({
				url: "./salesOrderController/saveSOReleaseLineItem",
				type: "POST",
				async:false,
				data :{ "cuSOID":cuSOID,"frieght":Frieght,"subTotal":subTotal,"Total":Total,"taxValue":taxValue,			
					"gridData":dataToSend,"DelSOData":cuSodetailIdsToBeDeleted,"withPriceLineStatus":withPriceLineStatus},
				success: function(data) {
					cuSodetailIdsToBeDeleted=[];
					$('#showMessageLineSO').html("Saved");
					lineItemgridLoad=true;
					jQuery("#SOlineItemGrid").jqGrid('setGridParam',{url:"./salesOrderController/solineitemGrid",postData: {'cuSOID':cuSOID}}).trigger("reloadGrid");
					setTimeout(function(){
						$('#showMessageLineSO').html("");						
						},3000);
					 if(popupdetail=="close"){
						 $("#salesrelease").dialog("close");
					 }
					 $( "#salesreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
					
				}
			});
	 }else{
		 var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes, please save prior to continuing.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					//$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
					//$("#new_row_quantityOrdered").focus();
				   // return false;
				}}}).dialog("open");
	 }
	
	/*for(var i=0;i<cuSodetailIdsToBeDeleted.length;i++){
		alert(itemCodes[i]);
	}*/
	 /*var rows = jQuery("#SOlineItemGrid").getDataIDs();
	 	deleteSOLineDetailID=new Array();
		 for(var a=0;a<rows.length;a++)
		 {
		    row=jQuery("#SOlineItemGrid").getRowData(rows[a]);
		   var id="#canDeleteSOID_"+rows[a];
		   var canDeleteSO=$(id).is(':checked');
		   console.log(rows[a]+"canDeleteSO=="+canDeleteSO);
		   if(canDeleteSO){
			  var var_cusoDetailID=row['cuSodetailId'];
			  if(var_cusoDetailID!=undefined && var_cusoDetailID!=null && var_cusoDetailID!="" && var_cusoDetailID!=0){
				  deleteSOLineDetailID.push(var_cusoDetailID);
			 	}
			 $('#SOlineItemGrid').jqGrid('delRowData',rows[a]);
		  }
	}
	*/
	
}

function closeSOLineItemTab(){
	  var gridRows = $('#SOlineItemGrid').getRowData();
      var new_so_lines_form =  JSON.stringify(gridRows);
      if(new_so_lines_form != so_lines_form){
    	  saveLineDetails("close");
    	  /*var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"Yes": function(){
					jQuery(this).dialog("close");
					saveLineDetails("close");
				    return false;
				},
				"No": function ()	{
					jQuery(this).dialog("close");
					$("#salesrelease").dialog("close");
				return false;	
				}}}).dialog("open");*/
      }else{
    	  so_lines_form="[]";
    	  $("#salesrelease").dialog("close");
      }
	
}

function canDeleteSOCheckboxFormatter(cellValue, options, rowObject){
	var id="canDeleteSOID_"+options.rowId;
	//deleteSOCheckboxChanges(this.id);
	var element = "<img src='./../resources/images/delete_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick='deleteRowFromJqGrid("+options.rowId+");setsoLinegridTotal();soLineItemformChanges();'>";
	return element;
}
function deleteRowFromJqGrid(jqGridRowId)
{
	 var cuSodetailId = jQuery("#SOlineItemGrid").jqGrid ('getCell', jqGridRowId, 'cuSodetailId');
	 if(!cuSodetailId==false){
		 cuSodetailIdsToBeDeleted.push(cuSodetailId);
		 $('#SOlineItemGrid').jqGrid('delRowData',jqGridRowId);
		 $( "#SOlineItemGrid_iladd" ).trigger( "click" );		 
	 }else{
		 $('#SOlineItemGrid').jqGrid('delRowData',jqGridRowId);
		 $( "#SOlineItemGrid_iladd" ).trigger( "click" );
	 }
	 $('#SOlineItemGrid').jqGrid('setSelection', "new_row");
	 $("#new_row_itemCode").focus();
}
function deleteSOCheckboxChanges(id){
	id="#"+id;
    var canDo=$(id).is(':checked');
    if(canDo){
    	$(id).val("true");
    }else{
    	$(id).val("false");
    }
}
function changePosition(soLines_selectRow){
	if(soLines_selectRow=="new_row"){
	 if($("#"+soLines_selectRow+"_itemCode").val().length==0 || ltrim($("#"+soLines_selectRow+"_itemCode").val()).length==0){
		 $("#"+soLines_selectRow+"_itemCode").val("");
		 $("#"+soLines_selectRow+"_itemCode").focus();
	  }else{
		}
	}else{
	}
}
function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
}
var searchPrMasterId=null;
var searchProduct=null;
$("#salesorder_whsecost").val(formatCurrency("0.00"));
$('#costdetails').show();
var cuSodetailIdsToBeDeleted=[];