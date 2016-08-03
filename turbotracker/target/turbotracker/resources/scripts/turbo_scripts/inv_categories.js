/**
 * 
 */
jQuery(document).ready(function() {
	document.getElementById("categoryDetailsForm").reset();
	clearCategoryDetails();
	loadInvCategoris();
	overrideMarkupAmount();
});

var oper = 'add';
/**
 {"prCategoryId":1,"description":"Centr Upblast Roof Fans","inActive":false,"markupCost":false,"markupAmount":null}
 */
/**
 * Method to load Inventory Categories grid.
 */
function loadInvCategoris(){
	$("#inventoryCategoriesGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./inventoryList/categoriesList',
	   	colNames:['invCategoryId', 'Category', 'In Active', 'Markup Cost', 'Markup Ampunt'],
		colModel:[
			{name:'prCategoryId',index:'prCategoryId', width:40,editable:false, hidden:true, editrules:{}, editoptions:{}},
			{name:'description',index:'description', width:40,editable:false, hidden:false, editrules:{}, editoptions:{}},
			{name:'inActive',index:'inActive', width:20,editable:false, align:'center', hidden:false, formatter: 'checkbox', editrules:{}, editoptions:{}},
			{name:'markupCost',index:'markupCost', width:40,editable:false, hidden:true, editrules:{}, editoptions:{}},
			{name:'markupAmount',index:'markupAmount', width:40,editable:false, hidden:true, editrules:{}, editoptions:{}}
		],
		rowNum: 500,	
		pgbuttons: false,	
		recordtext: '',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Inventory Categoties',
		height:480,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		onSelectRow: function(rowId){
			loadInventoryCategoryDetails(rowId);
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

function loadInventoryCategoryDetails(rowId){
	oper = 'edit';
	document.getElementById("categoryDetailsForm").reset();
	var rowData = jQuery("#inventoryCategoriesGrid").getRowData(rowId);
	$("#categoryDescription").val(rowData['description']);
	var isInActive = $(rowData['inActive']).val();
	if(isInActive === 'true'){
		$("#categoryInactive").prop('checked', true);
	} else {
		$("#categoryInactive").prop('checked', false);
	}
	$("#markupAmount").text(formatCurrency(rowData['markupAmount']));
	$("#overrideInput").val(formatCurrency(rowData['markupAmount']));
	$("#invCategoryId").val(rowData['prCategoryId']);
}

function clearCategoryDetails(){
	oper = 'add';
	document.getElementById("categoryDetailsForm").reset();
	$("#categoryDescription").val("");
	$("#categoryInactive").prop('checked', false);
	$("#overrideMarkup").prop('checked', false);
	$("#markupAmount").text('');
	$("#overrideInput").val('');
	$("#invCategoryId").val('');
}

function overrideMarkupAmount(){
	if ($('#overrideMarkup').is(':checked')) {
		$('#overrideInput').removeAttr("disabled");
 	} else {
 		$('#overrideInput').prop('disabled',true);
 	}
 	return true;
}

var newDialogDiv = $(document.createElement('div'));

function SaveCategoryDetails(){
	var url = "./inventoryList/manipulateCategory";
	var formData = $("#categoryDetailsForm").serialize();
	$.ajax({
		url: url,
		type: "POST",
		data : formData+"&oper="+oper,
		success: function(data) {
			$("#lineItemGrid").trigger("reloadGrid");
			loadInvCategoris();
			clearCategoryDetails();
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Category updated successfully</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: '+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");
		}
	});
}

function deleteCategoryDetails(){
	oper = 'del';
	SaveCategoryDetails();
}