/**
 * 
 */
jQuery(document).ready(function() {
	document.getElementById("categoryDetailsForm").reset();
	clearCategoryDetails();
	loadInvCategoris();
	overrideMarkupAmount();
	editInvCategoryGlobalForm = $("#categoryDetailsForm").serialize();
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
		caption: 'Inventory Categories',
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
	document.getElementById("overrideMarkup").checked=false;
	if(rowData['markupCost']=="true"){
		document.getElementById("overrideMarkup").checked=true;
		document.getElementById("overrideInput").disabled=false;
	}else{
		document.getElementById("overrideInput").disabled=true;
	}
   
	$("#markupAmount").text(formatCurrency(rowData['markupAmount']));
	$("#overrideInput").val(formatCurrency(rowData['markupAmount']));
	$("#invCategoryId").val(rowData['prCategoryId']);
	
	editInvCategoryGlobalForm = $("#categoryDetailsForm").serialize();
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
function formvalidate(){
	var description=$("#categoryDescription").val();
	if(description==null || description=="" || description.length==0){
		$("#savebuttonerrordiv").html("*Required Description");
		setTimeout(function() {
            $('#savebuttonerrordiv').html("");
            }, 2000);
		return false;
	}
	
	return true;
}
function SaveCategoryDetails(){
	var markupamount=$("#overrideInput").val().replace(/[^0-9\.-]+/g,"");
	$("#overrideInput").val(markupamount);
	
	var url = "./inventoryList/manipulateCategory";
	var formData = $("#categoryDetailsForm").serialize();
	var validation=formvalidate();
	if(validation){
		var description=$("#categoryDescription").val();
		var categoryInactive="false";
		if(document.getElementById("categoryInactive").checked){
			categoryInactive="true";
		}
		var overrideMarkup="0"
		if(document.getElementById("overrideMarkup").checked){
			overrideMarkup="1";
		}
		var overrideMarkupInput=$("#overrideInput").val();
		var invCategoryId=$("#invCategoryId").val();
		var appendvalues="&description="+description+"&categoryInactive="+categoryInactive+"&overrideMarkup="+overrideMarkup+"&overrideMarkupInput="+overrideMarkupInput+"&invCategoryId="+invCategoryId+"&oper="+oper;
		$.ajax({
			url: url,
			type: "POST",
			data : appendvalues,
			success: function(data) {
			//	alert(oper);
				if(oper=='add')
				createtpusage('Inventory-Categories','Inventory Category Save','Info','Inventory,Category Save,description:'+description);
				else if(oper=='edit')
					createtpusage('Inventory-Categories','Inventory Category Update','Info','Inventory,Category Update,description:'+description);
				else if(oper=='del')
					createtpusage('Inventory-Categories','Inventory Category Delete','Info','Inventory,Category Delete,description:'+description);
				$("#lineItemGrid").trigger("reloadGrid");
				$("#inventoryCategoriesGrid").trigger("reloadGrid");
				//loadInvCategoris();
				//clearCategoryDetails();
				$("#markupAmount").text(formatCurrency(overrideMarkupInput));
				$("#savebuttonsuccessdiv").html("Category updated successfully");
				setTimeout(function() {
	                $('#savebuttonsuccessdiv').html("");
	                }, 2000);
				/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Category updated successfully</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
										buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");*/
			},
			error: function(jqXhr, textStatus, errorThrown){
				var errorText = $(jqXhr.responseText).find('u').html();
				jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: '+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error.", 
										buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");
			}
		});
	}
	
}

function deleteCategoryDetails(){
	oper = 'del';
	SaveCategoryDetails();
	clearCategoryDetails();
}