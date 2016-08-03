/** PO Realse function **/
var cuinvoiceID='';
jQuery(document).ready(function() {
	Loadtab();
	var jobNumber;
	if($('#operation').val() === 'create' && $('#operation').val() === 'edit'){	
		
			if($("#Cuso_ID").text() != undefined || $("#Cuso_ID").text() != '')
			{
				jobNumber = $("#Cuso_ID").text();
			}
			else
			{
				jobNumber = $("#jobNumber_ID").text();
			}
		
	}	
	else
	{
		jobNumber = $("#jobNumber_ID").text();
	}
	$(".datepicker").datepicker();
	$("#salesreleasetab").tabs({
		cache: false,
		selected:0,
		ajaxOptions: {
			data:{
				jobNumber: jobNumber,
				jobName:"'" + $("#jobName_ID").text() + "'",
				joMasterID: $("#joMaster_ID").text(),
				jobCustomer:$("#jobCustomerName_ID").text(),
				cuSOID : $("#Cuso_ID").text()
			},
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 60px;padding-top: 30px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			var $panel = $(ui.panel);
			$(this).tabs("option", { ajaxOptions: { data: {
				jobNumber: jobNumber,
				jobName:"'" + $("#jobName_ID").text() + "'",
				joMasterID: $("#joMaster_ID").text(),
				jobCustomer:$("#jobCustomerName_ID").text(),
				cuSOID : $("#Cuso_ID").text()
				
			} } });
			
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;'><img src='./../resources/scripts/jquery-autocomplete/loading11.gif'></div>");
			}
		}
	});
	$( "#salesreleasetab" ).tabs({ active: 0 });
	$('#cusinvoicetab, #soreleaselineitem').tabs({
		  select: function(event, ui){
			  CheckCustomerInvoiceSave();
		  }
		});
});
function CheckCustomerInvoiceSave(){
	var cusoId =  $('#Cuso_ID').text();
	var grid = $("#release");
	var SelectedRow = grid.jqGrid('getGridParam', 'selrow');
	var vePOID = grid.jqGrid('getCell', SelectedRow, 'vePoId');
	var joReleaseDetailid = grid.jqGrid('getCell', SelectedRow, 'joReleaseDetailid');
	if(vePOID){
		console.log('vePOID'+vePOID);
		$.ajax({
			url: "./salesOrderController/CheckCuinvoiceDetails",
			type: "POST",
			data : {"CuSOID" : vePOID, "joReleaseDetailid" :joReleaseDetailid},
			success: function(data) {
				if(data===true){
					$('#InfoSave').hide();
					$('#regularTab').show();
					PreloadDataFromInvoiceTable();
					loadCustomerInvoice();
					$("#customerInvoice_lineitems").trigger("reloadGrid");
				}
				else{
					$('#InfoSave').show();
					$('#regularTab').hide();
					PreloadDataInvoice();
				}
				}
		});
	} else {
	$.ajax({
		url: "./salesOrderController/CheckCuinvoiceDetails",
		type: "POST",
		data : {"CuSOID" : cusoId,"joReleaseDetailid" : joReleaseDetailid},
		success: function(data) {
			if(data===true){
				$('#InfoSave').hide();
				$('#regularTab').show();
				PreloadDataFromInvoiceTable();
				loadCustomerInvoice();
				$("#customerInvoice_lineitems").trigger("reloadGrid");
			}
			else{
				$('#InfoSave').show();
				$('#regularTab').hide();
				PreloadDataInvoice();
			}
			}
	});
	}
}

/** once customer invoice general tab is saved, the data will load from the cuInvoice table through this method. */

function PreloadDataFromInvoiceTable(){
	var cuInvoiceId = $('#cuinvoiceIDhidden').val();
	var rxMasterID = $('#rxCustomer_ID').text();
	cuinvoiceID =cuInvoiceId; 
	$.ajax({
		url: "./salesOrderController/getPreInvoiceData",
		type: "POST",
		data : "&cuInvoiceId="+cuInvoiceId+"&rxMasterID="+rxMasterID,
		success: function(data) {
			$("#CustomerNameGeneral").val(data.CustomerName);
			if (typeof(data.cuInvoice) != "undefined" && data.cuInvoice != null){
			$("#customerInvoice_invoiceNumberId").val(data.cuInvoice.invoiceNumber);
			$('#customerInvoice_subTotalID').val(formatCurrency(0));
			$('#customerInvoice_totalID').val(formatCurrency(0));
			$('#customerinvoicepaymentId').val(data.cuInvoice.cuTermsId);
			$('#customerInvoice_proNumberID').val(data.cuInvoice.trackingNumber);
			$('#customerInvoie_PONoID').val(data.cuInvoice.customerPonumber);
			$('#tagJobID').val(data.cuInvoice.tag);
			$('#customerInvoice_frightIDcu').val(formatCurrency(data.cuInvoice.freight)); 
			$('#customerInvoice_linefrightID').val(formatCurrency(data.cuInvoice.freight)); 
			$('#prWareHouseSelectID').val(data.cuInvoice.prToWarehouseId);
			$('#shipViaCustomerSelectlineID').val(data.cuInvoice.veShipViaId);
			$('#shipViaCustomerSelectID').val(data.cuInvoice.veShipViaId);
			$('#customer_Divisions').val(data.cuInvoice.coDivisionId);
			$('#customerinvoice_paymentTerms').val(data.cuTerms);
			$('#customerInvoice_salesRepsList').val(data.SalesMan);
			$('#customerInvoice_CSRList').val(data.AE);
			$('#customerInvoice_SalesMgrList').val(data.Submitting);
			$('#customerInvoice_EngineersList').val(data.Costing);
			$('#customerInvoice_PrjMgrList').val(data.Ordering);
			$('#customerInvoice_taxIdcu').val(formatCurrency(data.cuInvoice.taxTotal));
			$('#cuGeneral_taxvalue').val(formatCurrency(data.cuInvoice.taxTotal));
			$('#customerInvoice_lineproNumberID').val(data.cuInvoice.trackingNumber);
			$('#customerInvoice_lineinvoiceNumberId').val(data.cuInvoice.sonumber);
//			$('#customerInvoice_linetaxId').val(formatCurrency(data.cuInvoice.taxRate));
			$('#customerInvoice_lineproNumberID').val(data.cuInvoice.trackingNumber);
			$('#customerInvoice_lineinvoiceNumberId').val(data.cuInvoice.invoiceNumber);
			$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
			addressToShipCustomerInvoice();
			if(data.cuInvoice.doNotMail===0)
				$('#customerInvoie_doNotMailID').prop('checked',false);
			else
				$('#customerInvoie_doNotMailID').prop('checked',true);
			var createdDate = data.cuInvoice.createdOn;
			if (typeof (createdDate) != 'undefined') 
				FormatDate(createdDate);
			var shipDate = data.cuInvoice.shipDate;
			if (typeof (shipDate) != 'undefined') 
				FormatShipDate(shipDate);
			if (typeof(data.Cuinvoicedetail) != "undefined" && data.Cuinvoicedetail != null){
				$('#customerInvoice_subTotalID').val(formatCurrency(data.Cuinvoicedetail.taxTotal));
				$('#customerInvoice_totalID').val(formatCurrency(data.Cuinvoicedetail.taxTotal+data.cuInvoice.taxTotal+data.cuInvoice.freight));
				$('#customerInvoice_linesubTotalID').val(formatCurrency(data.Cuinvoicedetail.taxTotal));
				$('#customerInvoice_linetotalID').val(formatCurrency(data.Cuinvoicedetail.taxTotal+data.cuInvoice.taxTotal+data.cuInvoice.freight));
				}else{
					$('#customerInvoice_subTotalID').val(formatCurrency(0));
					$('#customerInvoice_totalID').val(formatCurrency(0));
					$('#customerInvoice_linesubTotalID').val(formatCurrency(0));
					$('#customerInvoice_linetotalID').val(formatCurrency(0));
				}
			}
			formattax();
			//loadCustomerInvoice();
		}
	});
}



function PreloadDataInvoice(){		
	var cuSOID = $('#Cuso_ID').text();
	var rxMasterID = $('#rxCustomer_ID').text();
	var grid = $("#release");
	var SelectedRow = grid.jqGrid('getGridParam', 'selrow');
	var vePOID = grid.jqGrid('getCell', SelectedRow, 'vePoId');
	var jobNumber = $('.jobHeader_JobNumber').val();
	$.ajax({
		url: "./salesOrderController/getPreLoadData",
		type: "POST",
		data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber,
		success: function(data) {
			if(data.vepo !== null){
				PreloadDataInvoiceFromPO(data);
			}else{
			$("#CustomerNameGeneral").val(data.CustomerName);
			if (typeof(data.Cuso) != "undefined" && data.Cuso != null){
			$("#customerInvoice_invoiceNumberId").val(data.Cuso.sonumber);
			$('#customerInvoice_subTotalID').val(formatCurrency(0));
			$('#customerInvoice_totalID').val(formatCurrency(0));
			$('#customerinvoicepaymentId').val(data.Cuso.cuTermsId);
			$('#customerInvoice_proNumberID').val(data.Cuso.trackingNumber);
			$('#customerInvoie_PONoID').val(data.Cuso.customerPonumber);
			$('#tagJobID').val(data.Cuso.tag);
			$('#customerInvoice_frightIDcu').val(formatCurrency(0)); 
			$('#customerInvoice_linefrightID').val(formatCurrency(data.Cuso.freight)); 
			$('#prWareHouseSelectID').val(data.Cuso.prFromWarehouseId);
			$('#prWareHouseSelectlineID').val(data.Cuso.prFromWarehouseId);
			$('#shipViaCustomerSelectID').val(data.Cuso.veShipViaId);
			$('#customer_Divisions').val(data.Cuso.coDivisionID);
			$('#customerinvoice_paymentTerms').val(data.Cuso.cuTermsId);
			$('#customerInvoice_salesRepsList').val(data.SalesMan);
			$('#customerInvoice_CSRList').val(data.AE);
			$('#customerInvoice_SalesMgrList').val(data.Submitting);
			$('#customerInvoice_EngineersList').val(data.Costing);
			$('#customerInvoice_PrjMgrList').val(data.Ordering);
			$('#customerInvoice_salesRepId').val(data.Cuso.cuAssignmentId0);
			$('#customerInvoice_CSRId').val(data.Cuso.cuAssignmentId1);
			$('#customerInvoice_salesMgrId').val(data.Cuso.cuAssignmentId2);
			$('#customerInvoice_engineerId').val(data.Cuso.cuAssignmentId3);
			$('#customerInvoice_prjMgrId').val(data.Cuso.cuAssignmentId4);
			$('#customerInvoice_taxIdcu').val(formatCurrency(0));
			$('#cuGeneral_taxvalue').val(formatCurrency(data.Cuso.taxTotal));
			$('#customerInvoice_lineproNumberID').val(data.Cuso.trackingNumber);
			$('#customerInvoice_lineinvoiceNumberId').val(data.Cuso.sonumber);
			$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
			$('#customerinvoice_paymentTerms').val(data.termsDesc);
			addressToShipCustomerInvoice();
			var createdDate = data.Cuso.createdOn;
			if (typeof (createdDate) != 'undefined') 
				FormatDate(createdDate);
			var shipDate = data.Cuso.shipDate;
			if (typeof (shipDate) != 'undefined') 
				FormatShipDate(shipDate);
			/*if (typeof(data.Cusodetail) != "undefined" && data.Cusodetail != null){
				$('#customerInvoice_subTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
				$('#customerInvoice_totalID').val(formatCurrency(data.Cusodetail.taxTotal+data.Cuso.taxTotal+data.Cuso.freight));
				$('#customerInvoice_linesubTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
				$('#customerInvoice_linetotalID').val(formatCurrency(data.Cusodetail.taxTotal+data.Cuso.taxTotal+data.Cuso.freight));
			}*/
			}
			formattax();
			}
		}
	});
}

function PreloadDataInvoiceFromPO(data){
	$("#CustomerNameGeneral").val(data.CustomerName);
	if (typeof(data.vepo) != "undefined" && data.vepo != null){
	$("#customerInvoice_invoiceNumberId").val(data.vepo.ponumber);
	$('#customerInvoice_subTotalID').val(formatCurrency(0));
	$('#customerInvoice_totalID').val(formatCurrency(0));
	$('#customerinvoicepaymentId').val(data.Cumaster.cuTermsId);
//	$('#customerInvoice_proNumberID').val(data.Cumaster.trackingNumber);
	$('#customerInvoie_PONoID').val(data.vepo.customerPonumber);
//	$('#tagJobID').val(data.Cuso.tag);
	$('#customerInvoice_frightIDcu').val(formatCurrency(0)); 
	$('#customerInvoice_linefrightID').val(formatCurrency(data.vepo.freight)); 
	$('#prWareHouseSelectID').val(data.vepo.prWarehouseId);
	$('#prWareHouseSelectlineID').val(data.vepo.prWarehouseId);
	$('#shipViaCustomerSelectlineID').val(data.vepo.veShipViaId);
	$('#shipViaCustomerSelectID').val(data.vepo.veShipViaId);
	$('#customer_Divisions').val(data.aJomaster.coDivisionId);
	$('#customerInvoice_salesRepsList').val(data.SalesMan);
	$('#customerInvoice_CSRList').val(data.AE);
	$('#customerInvoice_SalesMgrList').val(data.Submitting);
	$('#customerInvoice_EngineersList').val(data.Costing);
	$('#customerInvoice_PrjMgrList').val(data.Ordering);
	$('#customerInvoice_salesRepId').val(data.aJomaster.cuAssignmentId0);
	$('#customerInvoice_CSRId').val(data.aJomaster.cuAssignmentId1);
	$('#customerInvoice_salesMgrId').val(data.aJomaster.cuAssignmentId2);
	$('#customerInvoice_engineerId').val(data.aJomaster.cuAssignmentId3);
	$('#customerInvoice_prjMgrId').val(data.aJomaster.cuAssignmentId4);
	$('#customerInvoice_taxIdcu').val(formatCurrency(0));
	$('#cuGeneral_taxvalue').val(formatCurrency(data.vepo.taxTotal));
//	$('#customerInvoice_lineproNumberID').val(data.vepo.trackingNumber);
	$('#customerInvoice_lineinvoiceNumberId').val(data.vepo.ponumber);
	$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
	$('#customerinvoice_paymentTerms').val(data.termsDesc);
	addressToShipCustomerInvoice();
	var createdDate = data.vepo.createdOn;
	if (typeof (createdDate) != 'undefined') 
		FormatDate(createdDate);
	var shipDate = data.vepo.createdOn;
	if (typeof (shipDate) != 'undefined') 
		FormatShipDate(shipDate);
	}
	formattax();
	}
function loadCustomerInvoice(){
	var cuinvoiceID = $('#cuinvoiceIDhidden').val();
	$("#customerInvoice_lineitems").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#customerInvoice_lineitemspager'),
		url:'./salesOrderController/cuInvlineitemGrid',
		postData: {'cuInvoiceID':cuinvoiceID},
		colNames:['Product No', 'Description','Qty','Price Each', 'Mult.', 'Tax', 'Amount', 'Manu. ID','cuSodetailId', 'prMasterID'],
		colModel :[
	{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:17},editrules:{edithidden:false,required: true}},
 	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:17},editrules:{edithidden:false},  
		cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
	{name:'quantityBilled', index:'quantityBilled', align:'center', width:15,hidden:false, editable:true, editoptions:{size:17, alignText:'left'},editrules:{edithidden:true,required: false}},
	{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:17, alignText:'right'},editrules:{edithidden:true}},
	{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:17, alignText:'right'}, formatter:customCurrencyFormatter, editrules:{edithidden:true}},
	{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
	{name:'note', index:'note', align:'right', width:50,hidden:true, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
	{name:'cuInvoiceId', index:'cuInvoiceId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
	{name:'cuInvoiceDetailId', index:'cuInvoiceDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
	{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}}],
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
			$("#customerInvoice_lineitems").setSelection(1, true);
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow:  function(id){
			
		},
		editurl:"./salesOrderController/manpulatecuInvoiceReleaseLineItem"
}).navGrid('#customerInvoice_lineitemspager', {add:true, edit:true,del:true,refresh:true,search:false},
			//-----------------------edit options----------------------//
			{
	 	width:400,left:630, top: 550, zIndex:1040,
		closeAfterEdit:true, reloadAfterSubmit:true,
		modal:true, jqModel:true,
		editCaption: "Edit Customer Invoice",
		beforeShowForm: function (form) 
		{
			$("a.ui-jqdialog-titlebar-close").hide();
			jQuery('#TblGrid_customerInvoice_lineitems#tr_itemCode .CaptionTD').append('Product No: ');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_itemCode .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_description .CaptionTD').empty();
			jQuery('#TblGrid_customerInvoice_lineitems#tr_description .CaptionTD').append('Description: ');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_quantityBilled.CaptionTD').empty();
			jQuery('#TblGrid_customerInvoice_lineitems#tr_quantityBilled.CaptionTD').append('Qty: ');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_quantityBilled .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_unitCost .CaptionTD').empty();
			jQuery('#TblGrid_customerInvoice_lineitems#tr_unitCost .CaptionTD').append('Cost Each.: ');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_priceMultiplier .CaptionTD').empty();
			jQuery('#TblGrid_customerInvoice_lineitems#tr_priceMultiplier .CaptionTD').append('Mult.: ');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
			jQuery('#TblGrid_customerInvoice_lineitems#tr_taxable .CaptionTD').empty();
			jQuery('#TblGrid_customerInvoice_lineitems#tr_taxable .CaptionTD').append('Tax: ');
			var unitCost = $('#unitCost').val().replace(/[^0-9-.]/g, '');
			$('#unitCost').val(unitCost);
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
			var cuinvoiceID = $('#cuinvoiceIDhidden').val();
			var taxRate =$('#customerInvoice_linetaxId').val();
			taxRate = parseFloat(taxRate.replace(/[^0-9-.]/g, ''));
			var freight = $('#customerInvoice_linefrightID').val();
			freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
			return {'cuInvoiceId':cuinvoiceID,'taxRate' : taxRate,'freight':freight, 'operForAck' : ''  };
		},
		afterSubmit:function(response,postData){
			$("#note").autocomplete("destroy"); 
			$(".ui-menu-item").hide();
			$("a.ui-jqdialog-titlebar-close").show();
			 return [true, loadCustomerInvoice()];
		}
	

			},
			//-----------------------add options----------------------//
			{
			width:400, left:630, top: 550, zIndex:1040,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add customer Invoice Line Item",
				onInitializeForm: function(form){
					
				},
				afterShowForm: function($form) {

					$(function() { var cache = {}; var lastXhr=''; $("input#itemCode").autocomplete({minLength: 1,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
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
					var cuinvoiceID = $('#cuinvoiceIDhidden').val();
					var taxRate =$('#customerInvoice_linetaxId').val();
					taxRate = parseFloat(taxRate.replace(/[^0-9-.]/g, ''));
					var freight = $('#customerInvoice_linefrightID').val();
					freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
					return { 'cuInvoiceId' : cuinvoiceID, 'taxRate' : taxRate,'freight':freight, 'operForAck' : '' };
				},
				afterSubmit:function(response,postData){
					$("#note").autocomplete("destroy");
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					return [true, loadCustomerInvoice()];
				}
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
				caption: "Delete",
				msg: 'Do you want to delete the line item?',
				color:'red',

				onclickSubmit: function(params){
					var grid = $("#customerInvoice_lineitems");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var cuInvoiceID = grid.jqGrid('getCell', rowId, 'cuInvoiceDetailId');
					var taxRate =$('#customerInvoice_linetaxId').val();
					taxRate = parseFloat(taxRate.replace(/[^0-9-.]/g, ''));
					var freight = $('#customerInvoice_linefrightID').val();
					freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
					return { 'cuInvoiceDetailId' : cuInvoiceID,'taxRate':taxRate,'freight':freight};
					
				},
				afterSubmit:function(response,postData){
					 PreloadData();
					 return [true, loadCustomerInvoice()];
				}
			}
		);
	}

function formattax(){
	var subTotal = $('#customerInvoice_subTotalID').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var tax = $('#customerInvoice_taxIdcu').val();
	tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
	var frieght = $('#customerInvoice_frightIDcu').val();
	frieght= parseFloat(frieght.replace(/[^0-9-.]/g, ''));
	var total = subTotal+tax+frieght;
	$('#customerInvoice_totalID').val(formatCurrency(total));
}
function Loadtab(){
jQuery( "#salesrelease").dialog({
	autoOpen: false,
	width: 840,
	title:"Sales Order",
	modal: true,
	close: function () {
		$("#note").autocomplete("destroy");
		 $(".ui-menu-item").hide();
		return true;
	}
});
}

function cancelPORelease(){
	jQuery("#salesrelease").dialog("close");
	$("#release").trigger("reloadGrid");
	return true;
}

function isExistInArray(array, value) {
	if(jQuery.inArray(value, array) === -1){
		return false;
	} else {
		return true;
	}
}



function viewPOAckPDF(){
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	var aQuotePDF = "ack";
	window.open("./purchasePDFController/viewPDFAckForm?vePOID="+vePOID+"&puchaseOrder="+aQuotePDF+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aInteger+"&jobNumber="+ jobNumber);
	return true;
}
function addressToShipCustomerInvoice(){
	var rxMasterId = $("#rxCustomer_ID").text(); 
	operationVar = "ship";
		 $.ajax({
				url: "./salesOrderController/getBilltoAddress",
				type: "GET",
				data : {"customerID" : rxMasterId,"oper" : operationVar},
				success: function(data) {
					$('#customerbillToAddressID1cuInvoice').val(data.address1);
					$('#customerbillToAddressID2cuInvoice').val(data.address2);
					$('#customerbillToCitycuInvoice').val(data.city);
					$('#customerbillToStatecuInvoice').val(data.state);
					$('#customerbillToZipIDcuInvoice').val(data.zip);
					}
			});
}
function FormatDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$("#customerInvoice_invoiceDateID").val(createdDate);
}
function FormatShipDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$("#customerInvoice_shipDateID").val(createdDate);
}

/** Method for adding a Purchase order as customer invoice in release tab. *//*
function addPOAsCustomerInvoice() {	
	
	PreloadDataInvoiceFromPO(vePOID);
}*/
