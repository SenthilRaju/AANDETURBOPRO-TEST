bkLib.onDomLoaded(function() { 
	new nicEditor({maxHeight : 100}).panelInstance('headerTextId');
	new nicEditor({maxHeight : 100}).panelInstance('termId');
});

jQuery(document).ready(function(){
	$('#search').css('visibility','hidden');
	loadwareHouseBilToAddress();
	$(".tabs_main").tabs({
		cache: true,
		ajaxOptions: {
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

	//var aHeaderTxt = "${requestScope.headerTextSettings}";
	//var aTerms = "${requestScope.termsTextSettings}";
	//$("#headerTextId").val(aHeaderTxt);
	//$("#termId").val(aTerms);
	var aQuote = $("#quote_ID").val();
	var aQuickQuote = $("#quickQuote_ID").val();
	var aInvoices = $("#invoice_ID").val();
	var aPurchaseOrder = $("#purchaseOrder_ID").val();
	var aQuote1 = $("#headerQuote_ID").val();
	var aQuickQuote1 = $("#headerQuick_ID").val();
	var aInvoices1 = $("#headerInvoice_ID").val();
	var aPurchaseOrder1 = $("#headerPurchaseOrder_ID").val();
	var aQuote2 = $("#termsQuote_ID").val();
	var aQuickQuote2 = $("#termsQuickQuote_ID").val();
	var aInvoices2 = $("#termsInvoice_ID").val();
	var aPurchaseOrder2 = $("#termsPO_ID").val();

	if(aQuote === '1'){
		$("#1quote").attr("checked", true);
	}
	if(aQuickQuote === '1'){
		$("#1quickquote").attr("checked", true);
	}
	if(aInvoices === '1'){
		$("#1invoices").attr("checked", true);
	}
	if(aPurchaseOrder === '1'){
		$("#1puchaseorder").attr("checked", true);
	}
	if(aQuote1 === '1'){
		$("#quote11").attr("checked", true);
	}
	if(aQuickQuote1 === '1'){
		$("#quickquote11").attr("checked", true);
	}
	if(aInvoices1 === '1'){
		$("#invoices11").attr("checked", true);
	}
	if(aPurchaseOrder1 === '1'){
		$("#puchaseorder11").attr("checked", true);
	}
	if(aQuote2 === '1'){
		$("#quote21").attr("checked", true);
	}
	if(aQuickQuote2 === '1'){
		$("#quickquote21").attr("checked", true);
	}
	if(aInvoices2 === '1'){
		$("#invoices21").attr("checked", true);
	}
	if(aPurchaseOrder2 === '1'){
		$("#puchaseorder21").attr("checked", true);
	}
	
});

/*function uploadImage(){
		var file = $('#ucompanydetailspload').val();
		var name = file.name;
		var size = file.size;
		var type = file.type;
		$.ajax({
			url: "./userlistcontroller/uploadImage",
			mType: type,
			data : {'uploadImage' : name},
			success: function(data){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Image Uploaded Sucessfully.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
					document.location.href = "./settings"; }}]}).dialog("open");
				return true;
			}
		});
		if(size <= 50000){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color: red;">Upload Size is 50kb only</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
}*/

function uploadImage() {
	var file_data = $("#avatar").val();   // Getting the properties of file from file field
	$.ajax({
            url: "./userlistcontroller/upload_avatar",
            data: {'imagePath':file_data, 'userSettingID' : '1'},
            type: 'POST',
            success: function(data){
            	
            }
       });
	return false;
}

function saveCompanySetting(){
	var companySeri = $("#companyFormId").serialize();
	var headerText= nicEditors.findEditor('headerTextId').getContent();
	var terms =nicEditors.findEditor('termId').getContent();
	var termsReplace = terms.replace(/&/g, "`and`");
	var headerTextReplace = headerText.replace(/&/g, "`and`");
	var companydetails = companySeri+"&headerText="+headerTextReplace+"&terms="+termsReplace;
	var newDialogDiv = jQuery(document.createElement('div'));
	$.ajax({
		url: "./userlistcontroller/companySettings",
		type: "POST",
		data : companydetails,
		success: function(data){
			jQuery(newDialogDiv).html('<span><b style="color:Green;">User details updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
									document.location.href = "./settings"; }}]}).dialog("open");
				return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: "'+errorText+'"</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");
		}
	});
}

function uploadJqueryForm(){
	$('#result').html('');
	/** blocking the UI**/
	$.blockUI({ css: { 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		'z-index':'1010px',
		color: '#fff'
	} }); 
	/************/
	$("#form2").ajaxForm({
		url: './fileupload/uploadcompanylogo',
		success:function(data) {
			$('#result').html('<span><b style="color:green;">'+data+'</b></span>');
			$('#result').show();
			window.location.reload(true); 
			setTimeout($.unblockUI, 2000);
		},
		error: function(jqXhr, textStatus, errorThrown){
			setTimeout($.unblockUI, 2000);
			var errorText = $(jqXhr.responseText).find('u').html();
			$('#result').html('<span><b style="color:red;">'+errorText+'</b></span>');
			$('#result').show();
		},
		dataType:"text"
	}).submit();
}
