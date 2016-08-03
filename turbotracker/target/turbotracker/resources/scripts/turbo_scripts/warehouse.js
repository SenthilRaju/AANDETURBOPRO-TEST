var newDialogDiv = jQuery(document.createElement('div'));
var msgText;
jQuery(document).ready(function() {
	loadWarehouseGrid();
});
function loadWarehouseGrid() {
	$("#wareHouseGrid").jqGrid({
		datatype : 'json',
		mtype : 'GET',
		url : './product/warehouseGrid',
		colNames : [ 'WareHouse','Description','prWarehouseId','coAccountIdasset','coTaxTerritoryId','inActive','address1','address2','city','zip','email','isLaser','state'],
		colModel : [ 
		    {name : 'searchName',index : 'searchName',width : 40,editable : false,hidden : false,editrules : {},editoptions : {}}, 
		    {name : 'description',index : 'description',width : 90,editable : false,hidden : false,editrules : {},editoptions : {}},
			{name : 'prWarehouseId',index : 'prWarehouseId',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'coAccountIdasset',index : 'coAccountIdasset',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'coTaxTerritoryId',index : 'coTaxTerritoryId',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'inActive',index : 'inActive',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'address1',index : 'address1',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'address2',index : 'address2',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'city',index : 'city',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'zip',index : 'zip',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'email',index : 'email',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'isLaser',index : 'isLaser',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}},
			{name : 'state',index : 'state',width : 40,editable : false,hidden : true,editrules : {},editoptions : {}}
			
		],
		rowNum : 100,
		pgbuttons : false,
		recordtext : '',
		sortorder : "asc",
		altRows : true,
		altclass : 'myAltRowClass',
		imgpath : 'themes/basic/images',
		caption : 'Warehouses',
		height : 350,
		width : 350,/* scrollOffset:0, */
		rownumbers : true,
		rownumWidth : 34,
		loadonce : false,
		onSelectRow : function(rowId) {
			loadDetails(rowId);
		},
		jsonReader : {
			root : "rows",
			page : "page",
			total : "total",
			records : "records",
			repeatitems : false,
			cell : "cell",
			id : "id",
			userdata : "userdata"
		}
	});
	return true;
}
function loadDetails(rowId) {
	var grid = $('#wareHouseGrid');
	var rowData = grid.getRowData(rowId);
	var description = rowData['description'];
	var prWarehouseId = rowData['prWarehouseId'];
	var cotaxterittoryID = rowData['coTaxTerritoryId'];
	var coAccountID = rowData['coAccountIdasset'];
	var isActive = rowData['inActive'];
	var Address1 = rowData['address1'];
	var Address2 = rowData['address2'];
	var city = rowData['city'];
	var zip = rowData['zip'];
	var email = rowData['email'];
	var isLaser = rowData['isLaser'];
	var state = rowData['state'];
	var searchName = rowData['searchName'];
	$('#description').val(searchName);
	$('#warehouseID').val(prWarehouseId);
	$('#Address1').val(Address1);
	$('#Address2').val(Address2);
	$('#city').val(city);
	$('#zip').val(zip);
	$('#taxTerritory').val(cotaxterittoryID);
	$('#asset').val(coAccountID);
	$('#emailPickUp').val(email);
	$('#state').val(state);
	$('#companyName').val(description);
	if(isActive==='true'){
		$('#warehouseInactive').prop('checked', true);
	}
	if(isLaser==='true'){
		$('#laser').prop('checked', true);
	}else{
		$('input:radio[id=dot]').prop('checked', true);
	}
	loadTaxterritory(cotaxterittoryID);
}
jQuery(function() {
	jQuery("#addNewWarehouseDlg").dialog({
		autoOpen : false,
		height : 510,
		width : 850,
		title : "Add New Warehouse",
		modal : true,
		close : function() {
			// $('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});
function OpenNewWarehouseDialog(cotaxterittoryID) {
	document.getElementById("addNewWarehouseForm").reset();
	jQuery("#addNewWarehouseDlg").dialog("open");
	return true;
}
$('#taxTerritory').change(function(){
	var taxid =$('#taxTerritory').val(); 
	loadTaxterritory(taxid);
});
function loadTaxterritory(cotaxterittoryID){
	$.ajax({
		url: "./product/getTaxTerritory",
		type: "GET",
		data : {'taxID':cotaxterittoryID},
		success: function(data) {
			$('#taxRate').text(data.taxRate);
		}
	});
	}
function saveWareHouses(){
	var warehouseDetails = $('#warehouseDetails').serialize();
	$.ajax({
		url: "./product/updateWarehouse",
		type: "GET",
		data : warehouseDetails,
		success: function(data) {
			if(data===true)
				$('#wareHouseGrid').trigger("reloadGrid");
			msgOnAjax("Warehouse Successfully updated.", "green", 2000);
		},
		error: function(data){
			console.log(data);
			alert('error');
		}
	});
	
}
function addWareHouses(){
	var warehouseDetails = $('#addNewWarehouseForm').serialize();
	var state = $('#addstate').val();
	 if($('#adddescription').val()==''){
			msgOnAjax("Please provide Description","white",2000);
			return false;
	}else if(state =='' || state.length>2){
		msgOnAjax("Please choose a city and state.","white",2000);
		return false;
	}else if($('#addasset').val()=='' || $('#addtaxTerritory').val()==''){
		msgOnAjax("Please choose Asset and Tax territory from the drop down.","white",2000);
		return false;
	}
	$.ajax({
		url: "./product/updateWarehouse",
		type: "GET",
		data : warehouseDetails,
		success: function(data) {
			jQuery("#addNewWarehouseDlg").dialog("close");
			$('#wareHouseGrid').trigger("reloadGrid");
			msgOnAjax("Warehouse Successfully Added.", "green", 2000);
		},
		error: function(data){
			alert('error');
		}
	});
}

function deleteWareHouses(){
	var wareHouseID = $('#warehouseID').val();
	if(wareHouseID==''){
		msgOnAjax("Please select any warehouse from the table to delete.", "red", 3000);
		return false;
	}
	msgText = "Do you want to delete the warehouse?";
	jQuery(newDialogDiv).attr("id","msgDlg");
	jQuery(newDialogDiv).html('<span><b style="color:red;">'+msgText+'</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
	buttons:{
		"Yes": function(){
			jQuery(this).dialog("close");
				$.ajax({
					url: "./product/deleteWarehouse",
					type: "GET",
					data : {'wareHouseID':wareHouseID},
					success: function(data) {
						jQuery("#addNewWarehouseDlg").dialog("close");
						document.getElementById("warehouseDetails").reset();
						$("#wareHouseGrid").trigger("reloadGrid");
						msgOnAjax("Warehouse Successfully deleted.", "green", 2000);
					},
					error: function(data){
						alert('error');
					}
				});
		},
		"No": function ()	{
			jQuery(this).dialog("close");
		}
	}}).dialog("open");
}

/** Auto Suggests **/

$(function() { var cache = {}; var lastXhr=''; $( "#city" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
	var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#state").val(stateCode);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}}); });

$(function() { var cache = {}; var lastXhr=''; $( "#addcity" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
	var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#addstate").val(stateCode);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}}); });