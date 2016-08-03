/**
 * 
 */
jQuery(document).ready(function() {
	document.getElementById("departmentDetailsForm").reset();
	clearDepartmentDetails();
	loadInvDepartment();
});

var oper = 'add';

function loadInvDepartment(){
	$("#inventoryDepartmentsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./inventoryList/departmentsAccountsList',
	   	colNames:['invDepartmentId', 'Department','InActive', 'RevenueID','COGSID', 'Revenue', 'CGS'],
		colModel:[
			{name:'prDepartmentId',index:'prDepartmentId', width:40,editable:false, hidden:true, editrules:{}, editoptions:{}},
			{name:'description',index:'description', width:40,editable:false, hidden:false, editrules:{}, editoptions:{}},
			{name:'inActive',index:'inActive', width:20,editable:false, align:'center', hidden:false, formatter: 'checkbox', editrules:{}, editoptions:{}},
			{name:'coAccountIDSales',index:'coAccountIDSales', width:20,editable:false,align:'left', hidden:true, editrules:{}, editoptions:{}},
			{name:'coAccountIDCOGS',index:'coAccountIDCOGS', width:40,editable:false, hidden:true, editrules:{}, editoptions:{}},
			{name:'revenueAccount',index:'revenueAccount', width:20,editable:false,align:'left', hidden:true, editrules:{}, editoptions:{}},
			{name:'cgsAccount',index:'cgsAccount', width:40,editable:false, hidden:true, editrules:{}, editoptions:{}}
		],
		rowNum: 100,	
		pgbuttons: false,	
		recordtext: '',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Inventory Departments',
		height:150,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		onSelectRow: function(rowId){
			loadInventoryDepartmentDetails(rowId);
		},
		jsonReader: {
			root: "rows",
			page: "page",
			total: "total",
			records: "records",
			repeatitems: false,
			cell: "cell",
			id: "id",
			userdata: "userdata"
		}
	});
	return true;
}

function loadInventoryDepartmentDetails(rowId){
	oper = 'edit';
	document.getElementById("departmentDetailsForm").reset();
	var rowData = jQuery("#inventoryDepartmentsGrid").getRowData(rowId);
	$("#departmentName").val(rowData['description']);
	var isInActive = $(rowData['inActive']).val();
	if(isInActive === 'true'){
		$("#departmentInactive").prop('checked', true);
	} else {
		$("#deaprtmentInactive").prop('checked', false);
	}
	$("#revenueAccount").val(rowData['revenueAccount']);
	$("#cgsAccount").val(rowData['cgsAccount']);
	$("#invDepartmentId").val(rowData['prDepartmentId']);
	$("#coAccountIdsales").val(rowData['coAccountIDSales']);
	$("#coAccountIdcogs").val(rowData['coAccountIDCOGS']);
}

function clearDepartmentDetails(){
	oper = 'add';
	document.getElementById("departmentDetailsForm").reset();
	$("#departmentName").text('');
	$("#revenueValue").text('');
	$("#cgsValue").text('');
	$("#invDepartmentId").val('');
	$("#coAccountIdcogs").val('');
	$("#coAccountIdsales").val('');
}

function SaveDepartmentDetails(){
	var url = "./inventoryList/manipulateDepartment";
	var validatecheck=validateinventorydepartmentcheck();
	if(validatecheck){
	var formData = $("#departmentDetailsForm").serialize();
	$.ajax({
		url: url,
		type: "POST",
		data : formData+"&oper="+oper,
		success: function(data) {
			createtpusage('Company-Settings','Save Inventory Departments','Info','Company-Settings,Saving Inventory Departments,department:'+$("#departmentName").val());
			$("#inventoryDepartmentsGrid").trigger("reloadGrid");
			loadInvDepartment();
			clearDepartmentDetails();
			$("#inventorydepartdiv").html("Department updated Successfully");
			setTimeout(function(){
				$('#inventorydepartdiv').html("");
				},3000);
			
//			var newDialogDiv = $(document.createElement('div'));
//			jQuery(newDialogDiv).html('<span><b style="color:Green;">Department updated Successfully</b></span>');
//			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
//									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");}}]}).dialog("open");
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			$("#inventorydeparterrordiv").html(errorText);
			setTimeout(function(){
				$('#inventorydeparterrordiv').html("");
				},3000);
			/*jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: '+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");*/
		}
	});
	}
}
function validateinventorydepartmentcheck(){
	var departmentName=$("#departmentName").val();
	var revenueAccount=$("#revenueAccount").val();
	var cgsAccount=$("#cgsAccount").val();
	if(departmentName==null||departmentName==undefined||departmentName==""||departmentName.trim().length==0){
		$('#inventorydeparterrordiv').html("required Description");
		setTimeout(function(){
			$('#inventorydeparterrordiv').html("");
			},3000);
		return false;
	}else if(revenueAccount==null||revenueAccount==undefined||revenueAccount==""||revenueAccount.trim().length==0){
		$('#inventorydeparterrordiv').html("required Revenue");
		setTimeout(function(){
			$('#inventorydeparterrordiv').html("");
			},3000);
		return false;
	}else if(cgsAccount==null||cgsAccount==undefined||cgsAccount==""||cgsAccount.trim().length==0){
		$('#inventorydeparterrordiv').html("required Cost of goods sold");
		setTimeout(function(){
			$('#inventorydeparterrordiv').html("");
			},3000);
		return false;
	}
	return true;
}

function deleteDepartmentDetails(){
	oper = 'del';
	SaveDepartmentDetails();
}
/**
 * This Method shows the message through notification icon on inventory departments page.
 * */
function showMessage() {
	$('#showDepartmentMessage').hover
}

$(function() { var cache = {}; var lastXhr='';
$( "#revenueAccount" ).autocomplete({ minLength: 2,timeout :1000, clearcache : true,
select: function( event, ui ) { var id = ui.item.id; var number=ui.item.Value;$("#coAccountIdsales").val(id); $("#revenueAccount").val(number); },
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "inventoryList/AccountListAuto", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
	});
});

$(function() { var cache = {}; var lastXhr='';
$( "#cgsAccount" ).autocomplete({ minLength: 2,timeout :1000, clearcache : true,
select: function( event, ui ) { var id = ui.item.id; var number=ui.item.Value;$("#coAccountIdcogs").val(id); $("#cgsAccount").val(number); },
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "inventoryList/AccountListAuto", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
	});
});