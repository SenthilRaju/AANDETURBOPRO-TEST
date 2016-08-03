/** PO Realse function **/
jQuery(document).ready(function() {
	$(".datepicker").datepicker();
	$("#poreleasetab").tabs({
		cache: false,
		ajaxOptions: {
			data:{
				jobNumber: $("#jobNumber_ID").text(),
				jobName:"'" + $("#jobName_ID").text() + "'",
				rxManufactureID:$("#manufacturerSubmital_ID").text(),
				joMasterID: $("#joMaster_ID").text(),
				jobCustomer:$("#jobCustomerName_ID").text()
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
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;'><img src='./../resources/scripts/jquery-autocomplete/loading11.gif'></div>");
			}
		}
	});
	var currentTab= $("#poreleasetab").tabs('option', 'selected');
    var currentTabAnchor = $("#poreleasetab").data('tabs').anchors[currentTab];
    $(currentTabAnchor).data('cache.tabs', true);
}); 

jQuery( "#porelease" ).dialog({
	autoOpen: false,
	width: 840,
	title:"Purchase Order",
	modal: true,
	close: function () {
		$("#note").autocomplete("destroy");
		 $(".ui-menu-item").hide();
		return true;
	}
});

function cancelPORelease(){
	jQuery("#porelease").dialog("close");
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

function removeA(arr){
	var what, a = arguments, L = a.length, ax;
	while(L> 1 && arr.length){
		what = a[--L];
		while((ax= arr.indexOf(what)) != -1){
			arr.splice(ax, 1);
		}
	}
	return arr;
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