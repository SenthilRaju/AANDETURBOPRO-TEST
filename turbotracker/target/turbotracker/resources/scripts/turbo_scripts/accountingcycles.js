jQuery(document).ready(
	function() {
		$('#date').datepicker();
		$(".charts_tabs_main").tabs({
				cache : true,
				ajaxOptions: {
					data: {},
					error: function(xhr, status, index, anchor) {
						$(anchor.hash).html("<div align='center' style='height: 386px;padding-top: 200px;'>"
												+ "<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."
												+ "</label></div>");
					}
				},
				load: function(e, ui) {
					$(ui.panel).find(".tab-loading").remove();
				},
				select : function(e, ui) {
					// window.location.hash =
					// ui.tab.hash;
					var $panel = $(ui.panel);
					if ($panel.is(":empty")) {
						$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"
									+ "<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
					}
				}
			});
		document.getElementById("reversePeriodDialogFormID").reset();
	});

var newDialogDiv = jQuery(document.createElement('div'));

function reversePeriodDialogForm() {
	document.getElementById("reversePeriodDialogFormID").reset();
	$("#editsysInfoId").val($("#accountingBasicsId").val());
	jQuery("#reversePeriodDialog").dialog("open");
	return true;
}

function closePeriodDialogForm() {
	document.getElementById("closePeriodDialogFormID").reset();
	$('#nextPeriodTo').datepicker({
		dateFormat : 'mm/dd/yy',
			onSelect: function(dateText) {
				var dat = dateText.split("/");
				if(dat[0] == "12" && dat[1] == "31") {			
				var date = new Date(dateText);
				var date1 = new Date(dateText);
				newDate = new Date(date.setDate(date.getDate() + 1));
				var tempDate =   ("0" + (newDate.getMonth() + 1)).slice(-2) + "/" + ("0" + newDate.getDate()).slice(-2) + "/" +newDate.getFullYear();
				newDate1 = new Date(date1.setFullYear(date1.getFullYear() + 1));
				
				var tempDate1 = ("0" + (newDate1.getMonth() + 1)).slice(-2)  + "/" +("0" + newDate1.getDate()).slice(-2) + "/" + newDate1.getFullYear();
				$('#nextYearFrom').val(tempDate);
				$('#nextYearTo').val(tempDate1);				
				}				
			  }
	});
	$("#closesysInfoId").val($("#accountingBasicsId").val());
	jQuery("#closePeriodDialog").dialog("open");
	return true;
}

jQuery(function() {
	jQuery("#reversePeriodDialog").dialog({
		autoOpen : false,
		height : 240,
		width : 400,
		title : "Reverse Period",
		modal : true,
		close : function() {
			return true;
		}
	});

	jQuery("#closePeriodDialog").dialog({
		autoOpen : false,
		height : 200,
		width : 653,
		title : "Reverse Period",
		modal : true,
		close : function() {
			return true;
		}
	});
});

function closeReverse() {
	document.getElementById("reversePeriodDialogFormID").reset();
	jQuery("#reversePeriodDialog").dialog("close");
	return true;
}

function closePeriod() {
	document.getElementById("closePeriodDialogFormID").reset();
	jQuery("#closePeriodDialog").dialog("close");
	return true;
}

function reversePeriod() {
	var reversePeriodId = $("#editreversePeriodId").val();
	var editsysInfoId = $("#editsysInfoId").val();
	$
			.ajax({
				url : "./reversePeriod",
				type : "POST",
				data : {
					'reversePeriodId' : reversePeriodId,
					'editsysInfoId' : editsysInfoId
				},
				success : function(data) {
					jQuery(newDialogDiv)
							.html(
									'<span><b style="color:Green;">Account Cycle Reversed successfully.</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Success.",
						buttons : [ {
							height : 35,
							text : "OK",
							click : function() {
								$(this).dialog("close");
								jQuery("#reversePeriodDialog").dialog("close");
								document.location.reload(true);
							}
						} ]
					}).dialog("open");
				}
			});
}

function closePeriodSubmit() {
	$.ajax({
		url : "./closePeriod",
		type : "POST",
		data : {
			'nextPeriodFrom' : jQuery("#nextPeriodFrom").val(),
			'nextPeriodTo' : jQuery("#nextPeriodTo").val(),
			'nextYearFrom' : jQuery("#nextYearFrom").val(),
			'nextYearTo' : jQuery("#nextYearTo").val(),
			'closesysInfoId' : jQuery("#closesysInfoId").val()
		},
		success : function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Account Cycle Closed successfully.</b></span>');
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 300,
				height : 150,
				title : "Success.",
				buttons : [ {
					height : 35,
					text : "OK",
					click : function() {
						$(this).dialog("close");
						jQuery("#closePeriodDialog").dialog("close");
						document.location.reload(true);
					}
				} ]
			}).dialog("open");
		}
	});
}

function dateIncrement(currentDate){
	var nextDate = new Date(currentDate);
    nextDate.setDate(nextDate.getDate() + 1);
    return nextDate;
}
