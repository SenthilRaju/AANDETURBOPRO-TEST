jQuery(document).ready(function() {
		loadSOLineItemGrid();	
		PreloadData();
});
function PreloadData(){
	var cuSOID = $('#Cuso_ID').text();
	$('#customerInvoice_subTotalIDLine').val(formatCurrency(0));
	$('#customerInvoice_totalIDLine').val(formatCurrency(0));
	var rxMasterID =$('#rxCustomer_ID').text();
	if(cuSOID != '' && cuSOID != null && typeof(cuSOID)!='undefined'){
		$.ajax({
			url: "./salesOrderController/getPreLoadData",
			type: "POST",
			data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID,
			success: function(data) {
				$("#SOnumberLine").val(data.Cuso.sonumber);
				$("#CustomerNameLine").val(data.CustomerName);
				$('#dateOfcustomerLine').val($("#poDate_ID").text());
				$('#customerInvoice_taxId').val(formatCurrencynodollar(data.Cuso.taxRate));
				if(data.Cusodetail !='undefined' && data.Cusodetail != null){
					$('#customerInvoice_subTotalIDLine').val(formatCurrency(data.Cusodetail.taxTotal));
					$('#customerInvoice_totalIDLine').val(formatCurrency(data.Cusodetail.taxTotal));
					formatTotal(data.Cuso.freight,data.Cusodetail.taxTotal);
				}
				$('#customerInvoice_frightID').val(formatCurrency(data.Cuso.freight));
				$('#customerInvoice_taxvalue').val(formatCurrency(data.Cuso.taxTotal));
				formattax(data.Cuso.freight);
				var createdDate = data.Cuso.createdOn;
				if (typeof createdDate != 'undefined') 
					FormatDate(createdDate);
				
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
function loadSOLineItemGrid(){
	var cuSOID = $('#Cuso_ID').text();
	 $('#SOlineItemGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#lineItemPager'),
			url:'./salesOrderController/solineitemGrid',
			postData: {'cuSOID':cuSOID},
			colNames:['Product No', 'Description','Qty','Price Each', 'Mult.', 'Tax', 'Amount', 'Manu. ID','cuSodetailId', 'prMasterID'],
			colModel :[
		{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false,required: true}},
      	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
			cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:15,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true,required: false}},
		{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:customCurrencyFormatter, editrules:{edithidden:true}},
		{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
		{name:'note', index:'note', align:'right', width:50,hidden:true, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'cuSoid', index:'cuSoid', align:'right', width:50,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'cuSodetailId', index:'cuSodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
		{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}}],
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
			loadComplete: function(data) {
				$("#SOlineItemGrid").setSelection(1, true);
			},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){
				var rowData = jQuery(this).getRowData(id); 
				var cuSODetailId = rowData["cuSodetailId"];
				$('#jobCustomerName_ID').text(cuSODetailId);
			},
			editurl:"./salesOrderController/manpulateSOReleaseLineItem"
	 }).navGrid('#lineItemPager', {add:true, edit:true,del:true,refresh:true,search:false},
				//-----------------------edit options----------------------//
				{
		 	width:515, left:400, top: 300, zIndex:1040,
			closeAfterEdit:true, reloadAfterSubmit:true,
			modal:true, jqModel:true,
			editCaption: "Edit Product",
			beforeShowForm: function (form) 
			{
				$("a.ui-jqdialog-titlebar-close").hide();
				jQuery('#TblGrid_SOlineItemGrid #tr_itemCode .CaptionTD').empty();
				jQuery('#TblGrid_SOlineItemGrid #tr_itemCode .CaptionTD').append('Product No: ');
				jQuery('#TblGrid_SOlineItemGrid #tr_itemCode .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				jQuery('#TblGrid_SOlineItemGrid #tr_description .CaptionTD').empty();
				jQuery('#TblGrid_SOlineItemGrid #tr_description .CaptionTD').append('Description: ');
				jQuery('#TblGrid_SOlineItemGrid #tr_quantityOrdered .CaptionTD').empty();
				jQuery('#TblGrid_SOlineItemGrid #tr_quantityOrdered .CaptionTD').append('Qty: ');
				jQuery('#TblGrid_SOlineItemGrid #tr_quantityOrdered .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				jQuery('#TblGrid_SOlineItemGrid #tr_unitCost .CaptionTD').empty();
				jQuery('#TblGrid_SOlineItemGrid #tr_unitCost .CaptionTD').append('Cost Each.: ');
				jQuery('#TblGrid_SOlineItemGrid #tr_priceMultiplier .CaptionTD').empty();
				jQuery('#TblGrid_SOlineItemGrid #tr_priceMultiplier .CaptionTD').append('Mult.: ');
				jQuery('#TblGrid_SOlineItemGrid #tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				jQuery('#TblGrid_SOlineItemGrid #tr_taxable .CaptionTD').empty();
				jQuery('#TblGrid_SOlineItemGrid #tr_taxable .CaptionTD').append('Tax: ');
				var unitPrice = $('#unitCost').val();
				unitPrice=  parseFloat(unitPrice.replace(/[^0-9-.]/g, ''));
				$('#unitCost').val(unitPrice);
				var priceMultiplier = $('#priceMultiplier').val();
				priceMultiplier=  parseFloat(priceMultiplier.replace(/[^0-9-.]/g, ''));
				$('#priceMultiplier').val(priceMultiplier);
				 
			},
			beforeSubmit:function(postdata, formid) {
				$("#note").autocomplete("destroy"); 
				$(".ui-menu-item").hide();
				var aPrMasterID = $('#prMasterId').val();
				if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
				return [true, ""];
			},
			onclickSubmit: function(params){
				var cusoid = $('#Cuso_ID').text();
				var taxRate =$('#customerInvoice_taxId').val();
				var freight = $('#customerInvoice_frightID').val();
				freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
				return {'cuSoid':cusoid,'taxRate' : taxRate,'freight':freight, 'operForAck' : ''  };
			},
			afterSubmit:function(response,postData){
				$("#note").autocomplete("destroy"); 
				$(".ui-menu-item").hide();
				$("a.ui-jqdialog-titlebar-close").show();
				PreloadData();
				 return [true, loadSOLineItemGrid()];
			}
		

				},
				//-----------------------add options----------------------//
				{
					width:550, left:400, top: 300, zIndex:1040,
					closeAfterAdd:true,	reloadAfterSubmit:true,
					modal:true, jqModel:false,
					addCaption: "Add Product",
					onInitializeForm: function(form){
						
					},
					afterShowForm: function($form) {

						$(function() { var cache = {}; var lastXhr=''; $("input#itemCode").autocomplete({minLength: 1,timeout :1000,
							source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
								lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
							select: function( event, ui ){
								var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
								/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
								$.ajax({
							        url: './getLineItems?prMasterId='+$("#prMasterId").val(),
							        type: 'POST',       
							        success: function (data) {
							        	$.each(data, function(key, valueMap) {										
											
							        		if("lineItems"==key)
											{				
												$.each(valueMap, function(index, value){						
													
														$("#description").val(value.description);
														$("#unitCost").val(value.lastCost);
														$("#priceMultiplier").val(value.pomult);
														if(value.isTaxable == 1)
														{
															$("#taxable").prop("checked",true);
														}
														else
															$("#taxable").prop("checked",false);
												
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
					beforeSubmit:function(postdta, formid) {
						$("#note").autocomplete("destroy");
						 $(".ui-menu-item").hide();
						var aPrMasterID = $('#prMasterId').val();
						if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
						return [true, ""];
					},
					onclickSubmit: function(params){
						var taxRate =$('#customerInvoice_taxId').val();
						var freight = $('#customerInvoice_frightID').val();
						freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
						var id = $('#Cuso_ID').text();
						return { 'cuSoid' : id, 'taxRate' : taxRate,'freight':freight, 'operForAck' : '' };
					},
					afterSubmit:function(response,postData){
						$("#note").autocomplete("destroy");
						$(".ui-menu-item").hide();
						$("a.ui-jqdialog-titlebar-close").show();
						PreloadData();
						return [true, loadSOLineItemGrid()];
					}
				},
				//-----------------------Delete options----------------------//
				{	
					closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
					caption: "Delete Product",
					msg: 'Delete the Product Record?',

					onclickSubmit: function(params){
						var taxRate =$('#customerInvoice_taxId').val();
						var freight = $('#customerInvoice_frightID').val();
						freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
						var cusoid = $('#Cuso_ID').text();
						var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
						var key = jQuery("#SOlineItemGrid").getCell(id, 9);
						return { 'cuSodetailId' : key, 'operForAck' : '','taxRate':taxRate,'freight':freight,'cuSoid':cusoid};
						
					},
					afterSubmit:function(response,postData){
						 PreloadData();
						 return [true, loadSOLineItemGrid()];
					}
				}
			);
};
function formattax(frieght){
	var subTotal = $('#customerInvoice_subTotalIDLine').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var tax = $('#customerInvoice_taxvalue').val();
	tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
	var total = subTotal+tax+frieght;
	$('#customerInvoice_totalIDLine').val(formatCurrency(total));
}

function saveLineDetails(){
	var newDialogDiv = jQuery(document.createElement('div'));
	var Frieght = $('#customerInvoice_frightID').val();
	Frieght= parseFloat(Frieght.replace(/[^0-9-.]/g, ''));
	var cuSOID = $('#Cuso_ID').text();
	var subTotal = $('#customerInvoice_subTotalIDLine').val();
	subTotal= parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var Total = $('#customerInvoice_totalIDLine').val();
	Total= parseFloat(Total.replace(/[^0-9-.]/g, ''));
	
	var SO_LinesSaveDetails = 'cuSOID='+cuSOID+'&frieght='+Frieght+'&subTotal='+subTotal+'&Total='+Total;
	$.ajax({
		url: "./salesOrderController/SaveSOLines",
		type: "POST",
		data : SO_LinesSaveDetails,
		success: function(data) {
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit'){
				if(!"SaveandClose" == $('#setButtonValue').val())
				{
					var errorText = "Sales Order Lines Tab details are Saved.";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
					PreloadData();
					return true;
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
						PreloadData();
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
			
			
		}
	});
	
}

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
	
	
