jQuery(document).ready(function() {
	$(".ledger_tabs_main").tabs({
		cache: true,
		ajaxOptions: {
			data: {  },
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 386px;padding-top: 200px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			//window.location.hash = ui.tab.hash;
			var $panel = $(ui.panel);
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"+
						"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
			}
		}
	});
});

function resetAll(){
	$("#customerInvoice option[value='2']").attr("selected", true);
	$("#vendorBills option[value='2']").attr("selected", true);
	$("#bankingPayroll option[value='2']").attr("selected", true);
	$("#bankingTransaction option[value='2']").attr("selected", true);
	$("#otherInventory option[value='2']").attr("selected", true);
	$("#journalEntries option[value='2']").attr("selected", true);
	return false;
}

function postAll(){
	$("#customerInvoice option[value='1']").attr("selected", true);
	$("#vendorBills option[value='1']").attr("selected", true);
	$("#bankingPayroll option[value='1']").attr("selected", true);
	$("#bankingTransaction option[value='1']").attr("selected", true);
	$("#otherInventory option[value='1']").attr("selected", true);
	$("#journalEntries option[value='1']").attr("selected", true);
	return false;
}