var newDialogDiv = jQuery(document.createElement('div'));
jQuery(document).ready(function() {
	$("#search").hide();
	$('#divqbuserName').hide();
	$('#divqbpassword').hide();
	$('#divkey').hide();
	
	$('#divhost').hide();
		$(".charts_tabs_main").tabs({
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
		document.getElementById("chartsDetailsFromID").reset();
	});
	
function validateQB()
{
	$('#divqbuserName').hide();
	$('#divqbpassword').hide();
	$('#divkey').hide();
	$('#divhost').hide();
var qbuserName = $('#qbuserName').val();
var qbpassword = $('#qbpassword').val();
var key = $('#key').val();
var host = $('#host').val();
if(qbuserName === '' || qbuserName === null)
	$('#divqbuserName').show( "fast");
	if(qbpassword === '' || qbpassword === null)
		$('#divqbpassword').show();
		if(key === '' || key === null)
			$('#divkey').show();
			if(host === '' || host === null)
				$('#divhost').show();
			if(qbuserName === '' || qbuserName === null && qbpassword === '' || qbpassword === null && key === '' || key === null
					&& host === '' || host === null)
				{
				return false;
				}
			else
				{
				saveqbSettings();
				return true;
				}
				
	}
	
function saveqbSettings()
{
var qbuserName = $('#qbuserName').val();
var qbpassword = $('#qbpassword').val();
var key = $('#key').val();
var host = $('#host').val();
var enable = false;
if($('#enable').attr('checked')) {
	enable = true;
} 
$.ajax({
	url: "./addQBSettings",
	type: "GET",
	data: {'qbuserName' : qbuserName, 'qbpassword' : qbpassword, 'key' :  key, 'host' :  host,'enable' : enable},
	success: function(data) {
		jQuery(newDialogDiv).html('<span><b style="color:green;">QuickBooks Settings Saved Successfully.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	}
});
}

	
	