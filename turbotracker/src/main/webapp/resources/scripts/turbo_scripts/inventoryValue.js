var warehouse = $('#bankAccountsID').val();
var inactive = $('#inactivelist').prop('checked');
var searchData = '';
jQuery(document).ready(function() {
	// loadInventoryGrid();
	$("#searchJob").attr("placeholder","Minimum 2 characters required to get List");
	$("#resetbutton").show();
	$('#bankAccountsID option').eq(0).prop('selected', true);
	ShowInactiveList();
	// loadInventoryValueGrid();

});

function ResetDetails() {
	searchData = '';
	$('#searchJob').val('');
	// loadInventoryGrid();
	$("#resetbutton").show();
	$('#bankAccountsID option').eq(0).prop('selected', true);
	ShowInactiveList();
}

function loadInventoryValueGrid(searchData) {
	warehouse = $('#bankAccountsID').val();
	var checked = 0;
	var inactive = $('#inactivelist').prop('checked');

	if (inactive == false) {
		checked = 0;
	} else {
		checked = 1;
	}
	checked = 0;
	var key = $('#searchJob').val();
	var aCustomerPage = $("#inventoryID").val();
	$("#inventoryValueGrid")
			.jqGrid(
					{
						datatype : 'json',
						mtype : 'GET',
						url : './inventoryList',
						postData : {
							'itemCode' : key,
							'Inactive' : checked,
							'Warehouse' : warehouse,
							'searchData' : searchData
						},
						pager : jQuery('#inventoryValueGridpager'),
						colNames : [ 'prMasterID', 'Item Code', 'Description',
								'Department', 'Category', 'Primary Vendor',
								'On Hand', 'Avg Cost', 'Total Cost' ],// 'Allocated',
																		// 'Available',
																		// 'On
																		// Order',
						colModel : [
								{
									name : 'prMasterId',
									index : 'prMasterId',
									width : 50,
									editable : false,
									hidden : true,
									editrules : {
										required : true
									},
									editoptions : {
										size : 10
									}
								},
								{
									name : 'itemCode',
									index : 'itemCode',
									width : 80,
									editable : true,
									editrules : {
										required : true
									},
									cellattr : function(rowId, tv, rawObject,
											cm, rdata) {
										return 'style="white-space: normal" ';
									},
									editoptions : {
										size : 10
									}
								},
								{
									name : 'description',
									index : 'description',
									align : 'center',
									width : 120,
									editable : true,
									editrules : {
										required : true
									},
									editoptions : {
										size : 10
									}
								},
								{
									name : 'prDepartment',
									index : 'prDepartment',
									align : 'center',
									width : 120,
									editable : true,
									editrules : {
										required : true
									},
									editoptions : {
										size : 10
									}
								},
								{
									name : 'prCategory',
									index : 'prCategory',
									align : 'center',
									width : 120,
									editable : true,
									editrules : {
										required : true
									},
									editoptions : {
										size : 10
									}
								},
								{
									name : 'vendorName',
									index : 'vendorName',
									align : 'center',
									width : 120,
									editable : true,
									editrules : {
										required : true
									},
									editoptions : {
										size : 10
									}
								},
								{
									name : 'inventoryOnHand',
									index : 'inventoryOnHand',
									align : 'center',
									width : 50,
									editable : true,
									editrules : {
										required : true
									},
									cellattr : function(rowId, tv, rawObject,
											cm, rdata) {
										return 'style="white-space: normal" ';
									},
									editoptions : {
										size : 10
									}
								},
								// {name:'inventoryAllocated',index:'inventoryAllocated',align:'center',
								// width:50,editable:true,
								// editrules:{required:true},
								// editoptions:{size:10}},
								// {name:'inventoryAvailable',index:'inventoryAvailable',align:'center',
								// width:50,editable:true,
								// editrules:{required:true},formatter:availableInventory,
								// cellattr: function (rowId, tv, rawObject, cm,
								// rdata) {return 'style="white-space: normal"
								// ';}, editoptions:{size:10}},
								// {name:'inventoryOnOrder',index:'inventoryOnOrder',
								// align:'center', width:50,editable:true,
								// editrules:{required:true}, cellattr: function
								// (rowId, tv, rawObject, cm, rdata) {return
								// 'style="white-space: normal" ';},
								// editoptions:{size:10}},
								{
									name : 'averageCost',
									index : 'averageCost',
									align : 'right',
									width : 125,
									editable : true,
									editrules : {
										required : false
									},
									editoptions : {
										size : 10
									},
									formatter : customCurrencyFormatter
								}, {
									name : 'totalCost',
									index : 'totalCost',
									align : 'right',
									width : 125,
									editable : true,
									editrules : {
										required : false
									},
									editoptions : {
										size : 10
									},
									formatter : customCurrencyFormatter
								} ],
						rowNum : aCustomerPage,
						pgbuttons : true,
						recordtext : '',
						rowList : [ 50, 100, 200, 500, 1000 ],
						viewrecords : true,
						pager : '#inventoryValueGridpager',
						sortname : 'itemCode',
						sortorder : "asc",
						altRows : true,
						altclass : 'myAltRowClass',
						imgpath : 'themes/basic/images',
						caption : 'Inventory',
						height : 450,
						width : 1150,/* scrollOffset:0, */
						rownumbers : true,
						rownumWidth : 34,
						loadonce : false,
						loadComplete : function(data) {
							if (aCustomerPage != null) {
								$(".ui-pg-selbox").attr("selected",
										aCustomerPage);
							}
							$("#inventoryValueGrid").setSelection(1, true);
							loadOnHandDetails();
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
						},
						onSelectRow : function(id) {
							loadOnHandDetails();
						},
						ondblClickRow : function(rowid) {
							var rowData = jQuery(this).getRowData(rowid);
							var prMasterID = rowData['prMasterId'];
							var ItemCode = rowData['itemCode'];
							 var checkpermission=getGrantpermissionprivilage('Inventory',0);
								if(checkpermission){
							document.location.href = "./inventoryDetails?token=view&inventoryId="
									+ prMasterID + "&itemCode=" + ItemCode;
								}
							return true;
						}
					}).navGrid('#inventoryValueGridpager', {
				add : false,
				edit : false,
				del : false,
				refresh : false,
				search : false
			});
	return true;
}

function availableInventory(cellValue, options, rowObject) {
	var onHand = rowObject.inventoryOnHand;
	var allocated = rowObject.inventoryAllocated;
	var available = onHand - allocated;
	return available;
}
$(function() {
	var cache = {};
	var lastXhr = '';
	$("#searchJob")
			.autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var aValue = ui.item.value;
							alert(aValue);
							var valuesArray = new Array();
							valuesArray = aValue.split("|");
							alert(valuesArray[2]);
						//	$("#inventorysearch").val(valuesArray[1]);
							var id = valuesArray[0];
							var code = valuesArray[2];
							//alert(code);
							// alert(id+" || "+code);
							/*location.href = "./inventoryDetails?token=view&inventoryId="
									+ id + "&itemCode=" + code;*/
							$("#searchJob").val(valuesArray[2]);
							loadInventoryValueGrid(valuesArray[2]);
						},
						open : function() {
							$(".ui-autocomplete")
									.prepend(
											'<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
							$('.ui-autocomplete-loading').removeClass(
									"ui-autocomplete-loading");
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("search/searchinventory",
									request, function(data, status, xhr) {
										cache[term] = data;
										if (xhr === lastXhr) {
											response(data);
										}
									});
						},
						error : function(result) {
							$('.ui-autocomplete-loading').removeClass(
									"ui-autocomplete-loading");
						}
					});
});
// function getSearchDetails()
// {
// console.log("ItemCode is--------------------->"+$('#searchJob').val());
// if($('#searchJob').val() != null && $('#searchJob').val() != '')
// {
// location.href="./inventory?token=view&key="+$('#searchJob').val();
// return true;
// }
// else
// return false;
//		
// }
/** Add Inventory Dialog Box * */
/** Add User Dialog Function * */
jQuery(function() {
	jQuery("#addNewInventoryDialog").dialog({
		autoOpen : false,
		height : 300,
		width : 730,
		title : "Add New Inventory",
		modal : true,
		close : function() {
			$('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});

/** Add New Inventory * */
function addNewInventory() {
	 var checkpermission=getGrantpermissionprivilage('Inventory',0);
		if(checkpermission){
	document.location.href = "./inventoryDetails?token=new";
		}
	return true;
}

/*******************************************************************************
 * ShowInactiveList() - Showing inactive
 * 
 */
function ShowInventoryList() {
	/*
	 * var warehouse = $('#bankAccountsID').val(); var inactive =
	 * $('#inactivelist').prop('checked');
	 * 
	 * if(inactive==false){ checked =0; }else{ checked =1; } checked =0;
	 * 
	 * if(warehouse==0){ return false; }
	 */
	/*
	 * var key = $('#searchJob').val(); jQuery("#inventoryValueGrid") .jqGrid()
	 * .setGridParam({ url :
	 * './inventoryList?itemCode='+key+'&Inactive='+checked+'&Warehouse='+warehouse+"&page=1&rows=50" })
	 * .trigger("reloadGrid");
	 */
	$("#inventoryValueGrid").jqGrid('GridUnload');
	loadInventoryValueGrid(searchData);

}

function ShowInactiveList() {
	// alert($('#inactivelist').prop('checked'));
	var warehouse = $('#bankAccountsID').val();
	if (warehouse == 0) {
		return false;
	}
	var checked = 0;
	var inactive = $('#inactivelist').prop('checked');
	if (inactive == false) {
		checked = 0;
	} else {
		checked = 1;
	}
	var itemcodevalue = '';
	jQuery("#inventoryGrid").jqGrid().setGridParam(
			{
				url : './inventoryList?itemCode=&Inactive=' + checked
						+ '&Warehouse=' + warehouse + "&page=1&rows=50"
			}).trigger("reloadGrid");

}

function loadOnHandDetailsGrid(prMasterID) {
	// var prMasterID = $("#prMasterID").text();

	try {
		$('#OnHandDetailsGrid').jqGrid(
				{
					datatype : 'JSON',
					mtype : 'POST',
					pager : jQuery('#OnHandDetailsPager'),
					url : './inventoryList/getOnHandItemDetails',
					postData : {
						'prMasterID' : prMasterID
					},
					colNames : [ 'Warehouse', 'Inventory On Hand',
							'Inventory Allocated', 'Inventory Available',
							'Inventory On Order', ],
					colModel : [ {
						name : 'city',
						index : 'city',
						align : 'center',
						width : 40,
						editable : true,
						hidden : false
					}, {
						name : 'inventoryOnHand',
						index : 'inventoryOnHand',
						align : 'center',
						width : 60,
						hidden : false,
						cellattr : function(rowId, tv, rawObject, cm, rdata) {
							return 'style="white-space: normal" ';
						}
					}, {
						name : 'inventoryAllocated',
						index : 'inventoryAllocated',
						align : 'center',
						width : 60,
						hidden : false
					}, {
						name : 'inventoryAvailable',
						index : 'inventoryAvailable',
						align : 'center',
						width : 60,
						hidden : false
					}, {
						name : 'inventoryOnOrder',
						index : 'inventoryOnOrder',
						align : 'center',
						width : 60,
						hidden : false
					} ],
					rowNum : 50,
					pgbuttons : true,
					recordtext : '',
					rowList : [ 50, 100, 200, 500, 1000 ],
					viewrecords : true,
					pager : '#OnHandDetailsPager',
					sortname : 'Name',
					sortorder : "asc",
					imgpath : 'themes/basic/images',
					caption : '',
					// autowidth: true,
					height : 160,
					width : 1120,/* scrollOffset:0, */
					rownumbers : true,
					altRows : true,
					altclass : 'myAltRowClass',
					rownumWidth : 45,
					loadComplete : function(data) {

					},
					loadError : function(jqXHR, textStatus, errorThrown) {
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
					},
					onSelectRow : function(id) {

					},
					ondblClickRow : function(rowid) {

					}
				}).navGrid('#OnHandDetailsPager',// {cloneToTop:true},
		{
			add : false,
			edit : false,
			del : false,
			refresh : false,
			search : false
		});
	} catch (err) {
		var text = "There was an error on this page.\n\n";
		text += "Error description: " + err.message + "\n\n";
		text += "Click OK to continue.\n\n";
		console.log(text);
	}
}

function loadOnHandDetails() {
	try {

		var rowId = $("#inventoryValueGrid").jqGrid('getGridParam', 'selrow');
		var prMasterId = $("#inventoryValueGrid").jqGrid('getCell', rowId,
				'prMasterId');

		$("#OnHandDetailsGrid").jqGrid('GridUnload');
		// loadOnHandDetailsGrid(prMasterId);
		$("#OnHandDetailsGrid").trigger("reloadGrid");
	} catch (err) {

	}
}
function getSearchDetails() {
	console.log("ItemCode is = " + $('#searchJob').val());
	if ($('#searchJob').val() != null && $('#searchJob').val() != '') {
		searchData = $('#searchJob').val();
		$("#inventoryValueGrid").jqGrid('GridUnload');
		loadInventoryValueGrid(searchData);
		$("#inventoryValueGrid").trigger("reloadGrid");
		return true;
	} else
		return false;

}

function ShowInactiveList() {
	// alert($('#inactivelist').prop('checked'));
	var warehouse = $('#bankAccountsID').val();
	if (warehouse == 0) {
		return false;
	}
	var checked = 0;
	var inactive = $('#inactivelist').prop('checked');
	if (inactive == false) {
		checked = 0;
	} else {
		checked = 1;
	}
	var itemcodevalue = $('#searchJob').val();
	$("#inventoryValueGrid").jqGrid('GridUnload');
	loadInventoryValueGrid();
	$("#inventoryValueGrid").trigger("reloadGrid");

}

function exportInventoryList() {
    createtpusage('Inventory-Inventory Value','Inventory Value CSV Report','Info','Inventory,Generating Inventory Value CSV Report,warehouseID:'+ $('#bankAccountsID').val());
	window.open("./inventoryList/printInventoryCSV?inactive=0&itemCode="
			+ $('#searchJob').val() + "&warehouseID="
			+ $('#bankAccountsID').val()+"&warehouseName="+$( "#bankAccountsID option:selected" ).text());
	return false;

}