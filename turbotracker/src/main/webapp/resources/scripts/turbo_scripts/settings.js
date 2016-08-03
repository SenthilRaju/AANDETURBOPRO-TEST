/*bkLib.onDomLoaded(function() { 
	new nicEditor({maxHeight : 100}).panelInstance('headerTextId');
	new nicEditor({maxHeight : 100}).panelInstance('termId');

});*/



var UserGroupID="";
jQuery(document).ready(function(){
	//new nicEditor({maxHeight : 100}).panelInstance('footerId');
	//new nicEditor({maxHeight : 100}).panelInstance('footerTextId');
	CKEDITOR.replace('headerTextId', ckEditorconfig);
	CKEDITOR.replace('termId', ckEditorconfig);
	CKEDITOR.replace('footerTextId', ckEditorconfig);
	CKEDITOR.replace('footerId', ckEditorconfig);
	
	$("#digstatus").hide();
	
	loadCustomerType();
	laodEmployeeSetting();
	loadFreightCharges();
	laodAssignrepsSetting();
	loadBidstatus();
	loadQuotesCategoryGrid();
	
	$('#date').datepicker();
	document.getElementById("customerTypeFromID").reset();

	jQuery("#groupDefaults").dialog({
		autoOpen:false,
		width:900,
		title:"Group Permission",
		modal:true,
		buttons:{
			"Save & Close":function(){
					$(this).dialog("close");
				}
				},
		close:function(){$('#groupDefaultsForm').validationEngine('hideAll');
		return true;}	
	});
	
	
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
	$("#fontsizeonPrice").val($("#fontsizeonPricehiddenID").val());
	
	if($("#chkfontsizeonPricehiddenID").val()=="0")
		$("#fontsizeonPrice").attr("disabled",true)
	else
		$("#fontsizeonPrice").attr("disabled",false)
		
		
	$("#chkfontsizeonPriceY").click(function(){
		$("#fontsizeonPrice").attr("disabled",false)
	});

	$("#chkfontsizeonPriceN").click(function()
	{
		$("#fontsizeonPrice").attr("disabled",true)
	});
	
	
	
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
	
	
	loadProductSettings($('#prodDeptIDs').val(),$('#prodID').val());
}); 

function callPermissionDialog(userGroupID) {
	UserGroupID=userGroupID;
	clearallCheckboxes();
	LoadGroupDefaultCheckboxes(userGroupID);
	jQuery("#groupDefaults").dialog("open");
}

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

function numbersonly(e){
	var unicode=e.charCode? e.charCode : e.keyCode
	if (unicode!=8){ //if the key isn't the backspace key (which we should allow)
	if (unicode<49||unicode>53) //if not a number
		{
		$("#digstatus").show();
		return false //disable key press
		}
	else
		{
		$("#digstatus").hide();
		}
	}
	}


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

function paymentTermsPage(){
	$("#settingsFormDetailsBlock").css({display: "none"});
	$("#settingsCustmerDetailsBlock").css({display: "block"});
	$('#settingsCustmerDetails').tabs({ active:1  });

	

}
function customercategorysave(){
	try{
	var companySeri = $("#customercategoryform").serialize();
	$.ajax({
		url: "./userlistcontroller/customercategorySettings",
		type: "POST",
		data : companySeri,
		success: function(data){
			createtpusage('Company-Settings','Save Customer Category','Info','Company-Settings,Saving Customer Category');
			/*$('.jobcustomerCategory1Id').val($('.customerCategory1Id').val());
			$('.jobcustomerCategory1Desc').val($('.customerCategory1Desc').val());
			
			$('.jobcustomerCategory2Id').val($('.customerCategory2Id').val());
			$('.jobcustomerCategory2Desc').val($('.customerCategory2Desc').val());
			
			$('.jobcustomerCategory3Id').val($('.customerCategory3Id').val());
			$('.jobcustomerCategory3Desc').val($('.customerCategory3Desc').val());
			
			$('.jobcustomerCategory4Id').val($('.customerCategory4Id').val());
			$('.jobcustomerCategory4Desc').val($('.customerCategory4Desc').val());
			
			$('.jobcustomerCategory5Id').val($('.customerCategory5Id').val());
			$('.jobcustomerCategory5Desc').val($('.customerCategory5Desc').val());*/
			
			
			
			$("#joCategory1Desc").val($("#customerCategory1Desc").val());
			$("#joCategory2Desc").val($("#customerCategory2Desc").val());
			$("#joCategory3Desc").val($("#customerCategory3Desc").val());
			$("#joCategory4Desc").val($("#customerCategory4Desc").val());
			$("#joCategory5Desc").val($("#customerCategory5Desc").val());
			
			jobcustomercategorysave();
			
			$("#CustomerErrorDiv").css({ color: "green" });
			$("#CustomerErrorDiv").text("Customer details updated.");
			setTimeout(function(){
				$('#CustomerErrorDiv').html("");
				},3000);
			return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			$("#CustomerErrorDiv").css({ color: "red" });
			$("#CustomerErrorDiv").text("Error:"+errorText);
			
		}
	});
}catch(err){
	alert(err.message);
}
}
/*
 *Created by:Praveenkumar
 *Date: 2014-09-09
 *Purpose: Job settings save employee assigned 
 */
function jobcustomercategorysave(){
	
	try{
	var companySeri = $("#jobcustomercategoryform").serialize();
	$.ajax({
		url: "./userlistcontroller/jobcategorySettings",
		type: "POST",
		data : companySeri,
		success: function(data){
			createtpusage('Company-Settings','Save Job Settings','Info','Saving Job Settings');
			$("#jobCustomerErrorDiv").css({ color: "green" });
			$("#jobCustomerErrorDiv").text("Job details updated.");
			setTimeout(function(){
				$('#jobCustomerErrorDiv').html("");
				},3000);
			return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			$("#CustomerErrorDiv").css({ color: "red" });
			$("#CustomerErrorDiv").text("Error:"+errorText);
			
		}
	});
}catch(err){
	alert(err.message);
}
}

// CreatedBy: Naveed
function saveQuoteFooter(){
	//var footnote =nicEditors.findEditor('footerId').getContent();
//	var footnote=tinymce.get("footerId").getContent();
	var footnote=CKEDITOR.instances["footerId"].getData();

	var id ="1";
	$.ajax({
		url: "./userlistcontroller/savequotesFooterNote",
		type: "POST",
		data: {id:id,footernote:footnote},
		success: function(data){
			createtpusage('Job-Settings','Save Quotes FooterNote','Info','Job-Settings,Saving quotes footer');
			
			$("#footerNoteSuccess").css({ color: "green" });
			$("#footerNoteSuccess").text("Quotes Footer Note Saved.");
			setTimeout(function(){
				$('#footerNoteSuccess').html("");
				},3000);
				return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			$("#footerNoteSuccess").css({ color: "red" });
			$("#footerNoteSuccess").text("Error:"+errorText);
		}
	});
}

function checkjobcategorysave(){
	//Customer Settings Category
	//customerCategory1Desc
	var customerCategory1Id=$("#customerCategory1Desc").val();
	var customerCategory2Id=$("#customerCategory2Desc").val();
	var customerCategory3Id=$("#customerCategory3Desc").val();
	var customerCategory4Id=$("#customerCategory4Desc").val();
	var customerCategory5Id=$("#customerCategory5Desc").val();
	
	
	//Job Settings Category
	//joCategory1Desc 
	var joCategory1Id=$("#joCategory1Desc").val();
	var joCategory2Id=$("#joCategory2Desc").val();
	var joCategory3Id=$("#joCategory3Desc").val();
	var joCategory4Id=$("#joCategory4Desc").val();
	var joCategory5Id=$("#joCategory5Desc").val();
	
	
	if(customerCategory1Id!=null){
		customerCategory1Id=customerCategory1Id.trim();
	}
	if(customerCategory2Id!=null){
		customerCategory2Id=customerCategory2Id.trim();
	}
	if(customerCategory3Id!=null){
		customerCategory3Id=customerCategory3Id.trim();
	}
	if(customerCategory4Id!=null){
		customerCategory4Id=customerCategory4Id.trim();
	}
	if(customerCategory5Id!=null){
		customerCategory5Id=customerCategory5Id.trim();
	}
	
	if(joCategory1Id!=null){
		joCategory1Id=joCategory1Id.trim();
	}
	if(joCategory2Id!=null){
		joCategory2Id=joCategory2Id.trim();
	}
	if(joCategory3Id!=null){
		joCategory3Id=joCategory3Id.trim();
	}
	if(joCategory4Id!=null){
		joCategory4Id=joCategory4Id.trim();
	}
	if(joCategory5Id!=null){
		joCategory5Id=joCategory5Id.trim();
	}
	
	var globalcheck=true;
	if(customerCategory1Id!=joCategory1Id){
		globalcheck=false;
	}
	if(customerCategory2Id!=joCategory2Id){
		globalcheck=false;
	}
	if(customerCategory3Id!=joCategory3Id){
		globalcheck=false;
	}
	if(customerCategory4Id!=joCategory4Id){
		globalcheck=false;
	}
	if(customerCategory5Id!=joCategory5Id){
		globalcheck=false;
	}
	
	if(globalcheck){
		jobcustomercategorysave();
	}else{
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:red;">The first (5) assignments not matched with customer settings.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
								$("#joCategory1Desc").val(customerCategory1Id);
								$("#joCategory2Desc").val(customerCategory2Id);
								$("#joCategory3Desc").val(customerCategory3Id);
								$("#joCategory4Desc").val(customerCategory4Id);
								$("#joCategory5Desc").val(customerCategory5Id);
								 }}]}).dialog("open");
	}
	
}

/*SOFooterNote*/
function saveSOfooterNote()
{
	var id ="1";
	var footerText=CKEDITOR.instances["footerTextId"].getData();
	
	
	$.ajax({
		url: "./userlistcontroller/savesoFooterText",
		type: "POST",
		data: {id:id,sofootertext:footerText},
		success: function(data){
			createtpusage('Customer-Settings','Save SO FooterNote','Info','Customer-Settings,Saving SO footer');
			
			$("#SofooterTextSuccess").css({ color: "green" });
			$("#SofooterTextSuccess").text("Footer Note Saved Successfully.");
			setTimeout(function(){
				$('#SofooterTextSuccess').html("");
				},3000);
				return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			$("#SofooterTextSuccess").css({ color: "red" });
			$("#SofooterTextSuccess").text("Error:"+errorText);
		}
	});
	
}


/*UserListController*/
function saveCompanySetting(){
	var companySeri = $("#companyFormId").serialize();
	var headerTextReplace=CKEDITOR.instances["headerTextId"].getData();
	var termsReplace=CKEDITOR.instances["termId"].getData();
	
/*	if(terms!=""){
		terms=terms.replace("face=\"arial\"","face=\"Arial\"");
		terms=terms.replace("face=\"courier new\"","face=\"Courier New\"");
		terms=terms.replace("face=\"helvetica\"","face=\"Helvetica\"");
		terms=terms.replace("face=\"times new roman\"","face=\"Times New Roman\"");
	}*/
//	var termsReplace = terms.replace(/&/g, "`and`");
//	var headerTextReplace = headerText.replace(/&/g, "`and`");
	
	
/*	var headerTextReplace=headerText.replace(/<[^>]*>/ig, ' ')
	   .replace(/<\/[^>]*>/ig, ' ')
	   .replace(/&nbsp;|&#160;/gi, ' ')
	   .replace(/\s+/ig, ' ')
	   .trim();
	
	var termsReplace=terms.replace(/<[^>]*>/ig, ' ')
	   .replace(/<\/[^>]*>/ig, ' ')
	   .replace(/&nbsp;|&#160;/gi, ' ')
	   .replace(/\s+/ig, ' ')
	   .trim();*/
	
	
	/*eight categories text field*/
	var Category1Desc=$("#Category1Desc").val();
	var Category2Desc=$("#Category2Desc").val();
	var Category3Desc=$("#Category3Desc").val();
	var Category4Desc=$("#Category4Desc").val();
	var Category5Desc=$("#Category5Desc").val();
	var Category6Desc=$("#Category6Desc").val();
	var Category7Desc=$("#Category7Desc").val();
	var Category8Desc=$("#Category8Desc").val();
	var companydetails = companySeri //+"&headerText="+headerTextReplace+"&terms="+termsReplace;
	var newDialogDiv = jQuery(document.createElement('div'));
	
	
	
	var GroupDefaults_1=$("#GroupDefaults_1").val();
	var GroupDefaults_1id=$("#GroupDefaults_1id").val();

	var GroupDefaults_2=$("#GroupDefaults_2").val();
	var GroupDefaults_2id=$("#GroupDefaults_2id").val();

	var GroupDefaults_3=$("#GroupDefaults_3").val();
	var GroupDefaults_3id=$("#GroupDefaults_3id").val();

	var GroupDefaults_4=$("#GroupDefaults_4").val();
	var GroupDefaults_4id=$("#GroupDefaults_4id").val();

	var GroupDefaults_5=$("#GroupDefaults_5").val();
	var GroupDefaults_5id=$("#GroupDefaults_5id").val();

	var GroupDefaults_6=$("#GroupDefaults_6").val();
	var GroupDefaults_6id=$("#GroupDefaults_6id").val();

	var GroupDefaults_7=$("#GroupDefaults_7").val();
	var GroupDefaults_7id=$("#GroupDefaults_7id").val();

	var GroupDefaults_8=$("#GroupDefaults_8").val();
	var GroupDefaults_8id=$("#GroupDefaults_8id").val();
	

	
	$.ajax({
		url: "./userlistcontroller/companySettings?"+companydetails,
		type: "POST",
		//ajaxGridOptions: { contentType: "application/x-www-form-urlencoded; charset=UTF-8" },
		data : {"headerText":headerTextReplace,"terms":termsReplace},
		success: function(data){
			createtpusage('Company-Settings','Save Company Settings','Info','Company-Settings,Saving settings,headerText:'+headerTextReplace);
			$("#errorDIV").css({ color: "green" });
			$("#errorDIV").text("Company settings updated.");
			setTimeout(function(){
				$('#errorDIV').html("");
				},3000);
			//document.location.href = "./settings";
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;">User details updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
									document.location.href = "./settings"; }}]}).dialog("open");*/
				return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			$("#errorDIV").css({ color: "red" });
			$("#errorDIV").text("Error:"+errorText);
			/*jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: "'+errorText+'"</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error.", 
					 				buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); }}]}).dialog("open");
			*/
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
	createtpusage('Company-Settings','Logo Upload','Info','Company-Settings,Uploading Logo');
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
//Jenith Test Snippets
//var newDialogDiv = jQuery(document.createElement('div'));

function loadCustomerType(){
	$("#chartsOfCuTypesGrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:"./customerList/getCustomerTypes",
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['Description', 'Code', 'MasterId','In Active'],
	   	colModel:[
			{name:'description',index:'description',align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
			{name:'code',index:'code', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'cuMasterTypeId',index:'cuMasterTypeId', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Customer Types',
		height:96,	width: 300,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		document.getElementById("customerTypeFromID").reset();
    		loadTypeDetails(rowId);
    	},
    	ondblClickRow: function(rowId) {
    		document.getElementById("customerTypeFromID").reset();
    		loadTypeDetails(rowId);
		}
	})/*.navGrid('#chartsOfTypesGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
	
}
function loadTypeDetails(rowId){
	var rowData = jQuery("#chartsOfCuTypesGrid").getRowData(rowId); 
	var description = rowData['description'];
	var inActive = rowData['inActive'];
	var code = rowData['code'];
	var typeId = rowData['cuMasterTypeId'];
	
	$("#cuMasterTypeIdId").val(typeId);
	$("#typeCodeId").val(code);
	$("#typeDescriptionId").val(description);
	$("#typeActiveId").val(inActive);
	return true;
}
function saveCuTypeData() {
	/*var cuMasterTypeId =  $("#cuMasterTypeIdId").val();
	var typeCode =  $("#typeCodeId").val();
	var typeDescription =  $("#typeDescriptionId").val();
	var typeActive =  $("#typeActiveId").val();*/
	var additionalInfo = $("#customerTypeFromID").serialize();
	$.ajax({
		url: "./customerList/saveCustomerTypes",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			createtpusage('Company-Settings','Save Customer Type','Info','Company-Settings,Saving Customer Type,Description:'+ $("#typeDescriptionId").val());
				$("#chartsOfCuTypesGrid").trigger("reloadGrid");
				var validateMsg = "<b style='color:Green;'>Customer Type Saved Successfully.</b>";	
				$("#CusTypeMsg").css("display", "block");
				$('#CusTypeMsg').html(validateMsg);
				setTimeout(function(){
				$('#CusTypeMsg').html("");						
				},2000);
				document.getElementById("customerTypeFromID").reset();
				$("#cuMasterTypeIdId").val("");
				$('#settings_tabs').tabs({ selected: 1 });
				$('#settings_tabs').tabs({ active: 1 });
		}
	});
}
function deleteCuType() {
	var cuMasterTypeId =  $("#cuMasterTypeIdId").val();
	$.ajax({
		url: "./customerList/deleteCustomerTypes",
		type: "POST",
		data :  { 'cuMasterTypeId' : cuMasterTypeId },
		success: function(data) {
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Customer Type Deleted successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); 
									document.getElementById("customerTypeFromID").reset();  }}]}).dialog("open");*/
			
			createtpusage('Company-Settings','Delete Customer Type','Info','Company-Settings,Deleting Customer Type,cuMasterTypeId:'+cuMasterTypeId);
			$("#chartsOfCuTypesGrid").trigger("reloadGrid");
			document.getElementById("customerTypeFromID").reset();
			$("#cuMasterTypeIdId").val("");
			var validateMsg = "<b style='color:red;'>Customer Type Deleted Successfully.</b>";	
			$("#CusTypeMsg").css("display", "block");
			$('#CusTypeMsg').html(validateMsg);
			setTimeout(function(){
			$('#CusTypeMsg').html("");						
			},2000);
			$('#settings_tabs').tabs({ selected: 1 });
			$('#settings_tabs').tabs({ active: 1 });
		}
	});
}

function laodEmployeeSetting(){
	$("#empSplitTypesGrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:'./jobtabs3/empSplitTypeListGrid',
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['Description', 'Default%', 'EcSplitCodeID'],
	   	colModel:[
			{name:'codeName',index:'codeName',align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
			{name:'defaultPct',index:'defaultPct', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}},
			{name:'ecSplitCodeID',index:'ecSplitCodeID', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Types',
		height:250,	width: 300,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "ecSplitCodeID",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		 var rowData = jQuery(this).getRowData(rowId); 
    		 $("#ecSplitCodeHiddenID").val(rowData["ecSplitCodeID"]);
    		 $("#splittypedescription").val(rowData["codeName"]);
    		 $("#spltypdefaultper").val(rowData["defaultPct"]);
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#chartsOfTypesGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
	
}
function loadFreightCharges(){
	try{
	$("#freightChargesGrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:"./companycontroller/getFreightCharges",
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['Code', 'Description','In Active','AskForNote'],
	   	colModel:[
			{name:'veFreightChargesId',index:'veFreightChargesId',align:'left', width:100,editable:true, hidden:true,editrules:{required:false}, editoptions:{size:10}},
			{name:'description',index:'description', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'askForNote',index:'askForNote', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}}
			
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Freight List',
		height:250,	width: 300,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		
    		
    		var rowData = jQuery("#freightChargesGrid").getRowData(rowId); 
    		var description = rowData['description'];
    		var inActive = rowData['inActive'];
    		var freightId = rowData['veFreightChargesId'];
    		var askForNote = rowData['askForNote'];
    		
    		$("#freigthDescriptionID").val(description);
    		$('#freightid').val(freightId);
    		
    		
    		if(inActive=='true'){
    			
    			$('#inactivefreight').prop('checked', true);
    		}else{
    			$('#inactivefreight').prop('checked', false);	
    		}
    		
    		if(askForNote=='true'){
    			$('#askfornote').prop('checked', true);	
    		}else{
    			$('#askfornote').prop('checked', false);	
    		}
    		
    		console.log(inActive+" :: "+askForNote);
    		
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#chartsOfTypesGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
	}	catch(err){
		alert(err.message);
	}
}

function addFreightDetails(){
	
	var freightId = $('#freightid').val();
	var description = $('#freigthDescriptionID').val();
	var inactive = $('#inactivefreight').is(':checked');
	var askforNote = $('#askfornote').is(':checked');
	var oper = true;
	
	$.ajax({
        url: "./companycontroller/updateFreightCharges",
        data: {veFreightChargesId:freightId,description:description,askForNote:askforNote,inActive:inactive,operation:oper},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Add Frieght Charges','Info','Company-Settings,Adding Frieght Charges,freightId:'+freightId);
        	$("#freightChargesGrid").trigger("reloadGrid");
        	document.getElementById("freightdetailsid").reset();
        }
   });
}
	function saveFreightDetails(){
		
		var freightId = $('#freightid').val();
		var description = $('#freigthDescriptionID').val();
		var inactive = $('#inactivefreight').is(':checked');
		var askforNote = $('#askfornote').is(':checked');
		var oper = false;
		
		$.ajax({
	        url: "./companycontroller/updateFreightCharges",
	        data: {veFreightChargesId:freightId,description:description,askForNote:askforNote,inActive:inactive,operation:oper},
	        type: 'POST',
	        success: function(data){
	        	createtpusage('Company-Settings','Save Frieght Charges','Info','Company-Settings,Saving Frieght Charges,freightId:'+freightId);
	        	$("#freightChargesGrid").trigger("reloadGrid");
	        	document.getElementById("freightdetailsid").reset();
	        }
	   });

}
	
function deleteFreightDetail(){
	var freightId = $('#freightid').val();
	$.ajax({
        url: "./companycontroller/deleteFreightCharges",
        data: {veFreightChargesId:freightId},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Delete Frieght Charges','Info','Company-Settings,Deleting Frieght Charges,freightId:'+freightId);
        	$("#freightChargesGrid").trigger("reloadGrid");
        	document.getElementById("freightdetailsid").reset();
        }
   });
}
function laodAssignrepsSetting(){
	$("#assignrepsgrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['Description', 'Default%', 'MasterId','In Active'],
	   	colModel:[
			{name:'description',index:'description',align:'left', width:100,editable:true, hidden:true,editrules:{required:false}, editoptions:{size:10}},
			{name:'code',index:'code', width:60,editable:true, editrules:{required:true},hidden:true, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'cuMasterTypeId',index:'cuMasterTypeId', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Employee',
		height:250,	width: 300,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#chartsOfTypesGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
	
}
function loadBidstatus(){
	try{
	$("#bidstatusgrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:"./job_controller/getBidStatusList",
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['Code', 'Description','In Active','Code'],
	   	colModel:[
			{name:'joBidStatusId',index:'joBidStatusId',align:'left', width:100,editable:true, hidden:true,editrules:{required:false}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'code',index:'code', width:50,editable:true, editrules:{required:true},  editoptions:{size:10}}
				
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Bid status',
		height:250,	width: 300,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		
    		
    		var rowData = jQuery("#bidstatusgrid").getRowData(rowId); 
    		var description = rowData['description'];
    		var inActive = rowData['inActive'];
    		var freightId = rowData['joBidStatusId'];
    		var bidcode = rowData['code'];
    		
    		$("#BidDescriptionID").val(description);
    		$('#bidstatusid').val(freightId);
    		$('#bidcodeid').val(bidcode);
    		
    		if(inActive=='true'){
    			
    			$('#inactivebid').prop('checked', true);
    		}else{
    			$('#inactivebid').prop('checked', false);	
    		}
    		
    		
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#chartsOfTypesGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
	}catch(err){
		alert(err.message);
	}
}

function addBidStatus(){
	
	var jobidid = $('#bidstatusid').val();
	var description = $('#BidDescriptionID').val();
	var inactive = $('#inactivebid').is(':checked');
	var code = $('#bidcodeid').val();
	
	
	var oper = true;
	
	$.ajax({
        url: "./job_controller/updateJoBidStatus",
        data: {joBidStatus:jobidid,description:description,code:code,inActive:inactive,operation:oper},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Add Bid Status','Info','Company-Settings,Adding Bid Status,description:'+description);
        	$("#bidstatusgrid").trigger("reloadGrid");
        	document.getElementById("bidstatusgridform").reset();
        }
   });
}

function editBidStatus(){
	var jobidid = $('#bidstatusid').val();
	var description = $('#BidDescriptionID').val();
	var inactive = $('#inactivebid').is(':checked');
	var code = $('#bidcodeid').val();
	
	
	var oper = false;
	
	$.ajax({
        url: "./job_controller/updateJoBidStatus",
        data: {joBidStatus:jobidid,description:description,code:code,inActive:inactive,operation:oper},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Edit Bid Status','Info','Company-Settings,Editing Bid Status,description:'+description);
        	$("#bidstatusgrid").trigger("reloadGrid");
        	document.getElementById("bidstatusgridform").reset();
        }
   });
}
function deleteBidStatus(){
	var jobidid = $('#bidstatusid').val();
	$.ajax({
        url: "./job_controller/deletejoBidStatus",
        data: {jobidstatusid:jobidid},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Delete Bid Status','Info','Company-Settings,Deleting Bid Status,description:'+jobidid);
        	$("#bidstatusgrid").trigger("reloadGrid");
        	document.getElementById("bidstatusgridform").reset();
        }
   });
}



function loadQuotesCategoryGrid(){
	try{
	$("#quotesCategoryGrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:"./job_controller/getjoQuotesCategoryList",
		colNames:['', 'Description'],
	   	colModel:[
			{name:'id',index:'id',align:'left', width:100,editable:true, hidden:true,editrules:{required:false}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, editoptions:{size:10}}				
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Quote Category',
		height:230,	width: 275,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		var rowData = jQuery("#quotesCategoryGrid").getRowData(rowId);
    		$('#quotesCategoryid').val(rowId);
    		var description = rowData['description'];    		
    		$("#quotesCategoryDescription").val(description);    		
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#chartsOfTypesGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
	}catch(err){
		alert(err.message);
	}
}

function addQuotesCategory(){
	
	var id = $('#quotesCategoryid').val();
	var description = $('#quotesCategoryDescription').val();	
	
	var oper = true;
	
	$.ajax({
        url: "./job_controller/updateQuotesCategory",
        data: {id:id,description:description,operation:oper},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Add Quotes Category','Info','Adding Quotes Category,description:'+description);
        	$("#quotesCategoryGrid").trigger("reloadGrid");
        	$('#quotesCategoryid').val('');
        	$('#quotesCategoryDescription').val('');
        }
   });
}

function editQuotesCategory(){
	var id = $('#quotesCategoryid').val();
	var description = $('#quotesCategoryDescription').val();
	
	var oper = false;
	if(id!=null && id !="")
	{
		$.ajax({
	        url: "./job_controller/updateQuotesCategory",
	        data: {id:id,description:description,operation:oper},
	        type: 'POST',
	        success: function(data){
	        	createtpusage('Company-Settings','Edit Quotes Category','Info','Editing Quotes Category,description:'+description);
	        	$("#quotesCategoryGrid").trigger("reloadGrid");
	        	$('#quotesCategoryid').val('');
	        	$('#quotesCategoryDescription').val('');
	        }
	   });
	}
}
function deleteQuotesCategory(){
	var id = $('#quotesCategoryid').val();
	
	if(id != null && id != "")
	{
		$.ajax({
	        url: "./job_controller/deleteQuotesCategory",
	        data: {id:id},
	        type: 'POST',
	        success: function(data){
	        	createtpusage('Company-Settings','Delete Quotes Category','Info','Deleting Quotes Category,id:'+id);
	        	$("#quotesCategoryGrid").trigger("reloadGrid");
	        	$('#quotesCategoryid').val('');
	        	$('#quotesCategoryDescription').val('');
	        }
	   });
	}
}

function exportCompanyContacts()
{
	createtpusage('Company-Settings','Print Company Contacts CSV','Info','Company-Settings,Printing Company Contacts CSV');
	window.open("./companycontroller/printCSVCompanyContacts?number=1");
	return false;
	
}
function exportCustomerContacts()
{
	createtpusage('Company-Settings','Print Customer Contacts CSV','Info','Company-Settings,Printing Customer Contacts CSV');
	window.open("./companycontroller/printCSVCompanyContacts?number=2");
	return false;
	
}

function exportVendorContacts(){
	createtpusage('Company-Settings','Print Vendor Contacts CSV','Info','Company-Settings,Printing Vendor Contacts CSV');
	window.open("./companycontroller/printCSVCompanyContacts?number=3");
	return false;
	
	
}

/*
 *Created by:Velmurugan
 *Date: 2014-09-17
 *Purpose: Jobquotecolumn defaults functionality 
 */
function checkColumnQuoteDefaults(){
	var totallist=[];
	var ordervalue=false;
	var checkvalue=false;
	for(var i=1;i<9;i++){
		var value=$("#pOrder"+i+"ID").val();
		if(value.trim().length==0 || isNaN(value)){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">Enter numeric values</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
									 }}]}).dialog("open");
		}
		if((value>8 || value==0)&& value.trim().length!=0){
			ordervalue=true;
		}
		totallist.push(value);
	}
	if(ordervalue){
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:red;">Number should be greater than 0 and less than 8</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
								 }}]}).dialog("open");
	}else{
		
		var current = null;
		var cnt = 0;
		totallist.sort();
		for (var i = 0; i < totallist.length; i++) {
		    if (totallist[i] != current) {
		        if (cnt > 0) {
		          //  alert(current + ' comes --> ' + cnt + ' times<br>');
		        }
		        current = totallist[i];
		        cnt = 1;
		    } else {
		        cnt++;
		    }
		    if(cnt>1){
		    	checkvalue=true;
		    	//alert(current + ' comes --> ' + cnt + ' times');
		    }
		}
		if(checkvalue){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">Duplicate values not allowed</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
									 }}]}).dialog("open");
			
		}else{
			saveColumnQuoteDefaults();
		}
		
	}
}
function saveColumnQuoteDefaults(){
	
	try{
	var quotecolumnformseri = $("#jobquotecolumnform").serialize();
	$.ajax({
		url: "./userlistcontroller/jobColumnQuoteDefaults",
		type: "POST",
		data : quotecolumnformseri,
		success: function(data){
			
			$("#jobcolumnquotedefault").css({ color: "green" });
			$("#jobcolumnquotedefault").text("Column Quote Defaults updated.");
			setTimeout(function(){
				$('#jobcolumnquotedefault').html("");
				},3000);
			return true;
		},
		error: function(jqXhr, textStatus, errorThrown){
			var errorText = $(jqXhr.responseText).find('u').html();
			/*$("#CustomerErrorDiv").css({ color: "red" });
			$("#CustomerErrorDiv").text("Error:"+errorText);*/
			
		}
	});
}catch(err){
	alert(err.message);
}
}
function savePriceTier(){
	var inputdata = 'prPriceLevel0='+$('#prPriceLevel0').val()+'&prPriceLevel1='+$('#prPriceLevel1').val()+'&prPriceLevel2='+$('#prPriceLevel2').val()+'&prPriceLevel3='+$('#prPriceLevel3').val()+'&prPriceLevel4='+$('#prPriceLevel4').val()+'&prPriceLevel5='+$('#prPriceLevel5').val();
	console.log(inputdata);
	
	$.ajax({
        url: "./userlistcontroller/savePriceTier",
        data: inputdata,
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Save Price Tier','Info','Company-Settings,Saving Price Tier');
        	$("#tierpricingmsgdiv").css({ color: "green" });
			$("#tierpricingmsgdiv").text("Tier Pricing Details updated.");
			setTimeout(function(){
				$('#tierpricingmsgdiv').html("");
				},3000);       	
        }
   });	
}
function saveCommissionSettings(){
	var chk_invoiceCombineYes="0";
	var txt_adjdescription1=$("#txt_adjdescription1").val();
	var txt_adjdescription2=$("#txt_adjdescription2").val();
	var txt_adjdescription3=$("#txt_adjdescription3").val();
	var txt_adjdescription4=$("#txt_adjdescription4").val();
	var txt_commissiondesc1=$("#txt_commissiondesc1").val();
	var txt_commissiondesc2=$("#txt_commissiondesc2").val();
	var txt_commissiondesc3=$("#txt_commissiondesc3").val();
	var txt_commissiondesc4=$("#txt_commissiondesc4").val();
	
	var chk_invoiceCombineYes = $("input[name='chk_invoiceCombineYes']:checked").val();
	var chk_viewCommissionYes="0";
	chk_viewCommissionYes=$("input[name='chk_viewCommissionYes']:checked").val();
	var chk_allocatedProfitYes="0";
	chk_allocatedProfitYes=$("input[name='chk_allocatedProfitYes']:checked").val();
	var chk_applyCreditYes="0";
	chk_applyCreditYes=$("input[name='chk_applyCreditYes']:checked").val();
	
//	txt_adjdescription1 txt_adjdescription2 txt_adjdescription3 txt_adjdescription4  txt_commissiondesc1 txt_commissiondesc2 txt_commissiondesc3 txt_commissiondesc4
//	chk_invoiceCombineYes  chk_viewCommissionYes chk_allocatedProfitYes chk_applyCreditYes
	
	$.ajax({
        url: "./inventoryList/saveCommissionSettings",
        data: {'txt_adjdescription1':txt_adjdescription1, 'txt_adjdescription2' : txt_adjdescription2,
        		'txt_adjdescription3':txt_adjdescription3,'txt_adjdescription4':txt_adjdescription4,
        		'txt_commissiondesc1':txt_commissiondesc1,'txt_commissiondesc2':txt_commissiondesc2,
        		'txt_commissiondesc3':txt_commissiondesc3,'txt_commissiondesc4':txt_commissiondesc4,
        		'chk_invoiceCombineYes':chk_invoiceCombineYes,'chk_allocatedProfitYes':chk_allocatedProfitYes,
        		'chk_viewCommissionYes':chk_viewCommissionYes,'chk_applyCreditYes':chk_applyCreditYes
               },
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Save Commission Settings','Info','Company-Settings,Saving Commission Settings');
        	$("#commissionSettingMsg").text("Settings Updated Successfully");
        	setTimeout(function(){
        		$('#commissionSettingMsg').text("");
			},3000);
        }
   });	
	
}
function saveinventorysettings(){
	var txt_overridewarehouse=$("#txt_overridewarehouse").val();
	var chk_invoverridewarehouseYes="0";
	if(document.getElementById("chk_invoverridewarehouseYes").checked){
		chk_invoverridewarehouseYes="1";
	} 
	var chk_invpurOrderCostYes="0";
	if(document.getElementById("chk_invpurOrderCostYes").checked){
		chk_invpurOrderCostYes="1";
	}
	var chk_invwareavgcostYes="0";
	if(document.getElementById("chk_invwareavgcostYes").checked){
		chk_invwareavgcostYes="1";
	}
	var chk_invbinLocationYes="0";
	if(document.getElementById("chk_invbinLocationYes").checked){
		chk_invbinLocationYes="1";
	}
	var chk_invWeightYes="0";
	if(document.getElementById("chk_invWeightYes").checked){
		chk_invWeightYes="1";
	}
	var chk_invWarhouseaddressYes="0";
	if(document.getElementById("chk_invWarhouseaddressYes").checked){
		chk_invWarhouseaddressYes="1";
	}
	$.ajax({
        url: "./inventoryList/saveInventorysettings",
        data: {'txt_overridewarehouse':txt_overridewarehouse, 'chk_invoverridewarehouseYes' : chk_invoverridewarehouseYes,
        		'chk_invpurOrderCostYes':chk_invpurOrderCostYes,'chk_invwareavgcostYes':chk_invwareavgcostYes,
        		'chk_invbinLocationYes':chk_invbinLocationYes,'chk_invWeightYes':chk_invWeightYes,
        		'chk_invWarhouseaddressYes':chk_invWarhouseaddressYes  
               },
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Save Inventory Settings','Info','Company-Settings,Saving Inventory Settings');
        	$("#but_inventorysettingserrordiv").text("Settings Updated Successfully");
        	setTimeout(function(){
				$('#but_inventorysettingserrordiv').text("");
				},3000);
        }
   });
}


function saveJobSettingsSysVariable() {
	var txt_overridewarehouse=$("#txt_overridewarehouse").val();
	
	var chkjobcheckoffYN="0";
	if($('input:radio[name=chkjobcheckoffYN]:checked').val()==="Yes"){
		chkjobcheckoffYN="1";
	}
	var chkchangejobYN="0";
	if($('input:radio[name=chkchangejobYN]:checked').val()==="Yes"){
		chkchangejobYN="1";
	}
	var chkmemoYN="0";
	if($('input:radio[name=chkmemoYN]:checked').val()==="Yes"){
		chkmemoYN="1";
	}
	var chkjobnoticelbl1YN="0";
	if($('input:radio[name=chkjobnoticelbl1YN]:checked').val()==="Yes"){
		chkjobnoticelbl1YN="1";
	}
	var chkjobnoticereporttype1YN="0";
	if($('input:radio[name=chkjobnoticereporttype1YN]:checked').val()==="Yes"){
		chkjobnoticereporttype1YN="1";
	}
	var chkjobnoticelbl2YN="0";
	if($('input:radio[name=chkjobnoticelbl2YN]:checked').val()==="Yes"){
		chkjobnoticelbl2YN="1";
	}
	var chkjobnoticereporttype2YN="0";
	if($('input:radio[name=chkjobnoticereporttype2YN]:checked').val()==="Yes"){
		chkjobnoticereporttype2YN="1";
	}
	var chkjobnoticelbl3YN="0";
	if($('input:radio[name=chkjobnoticelbl3YN]:checked').val()==="Yes"){
		chkjobnoticelbl3YN="1";
	}
	var chkjobnoticereporttype3YN="0";
	if($('input:radio[name=chkjobnoticereporttype3YN]:checked').val()==="Yes"){
		chkjobnoticereporttype3YN="1";
	}
	var chkbidtocurdateYN="0";
	if($('input:radio[name=chkbidtocurdateYN]:checked').val()==="Yes"){
		chkbidtocurdateYN="1";
	}
	var chkdeftakeoffYN="0";
	if($('input:radio[name=chkdeftakeoffYN]:checked').val()==="Yes"){
		chkdeftakeoffYN="1";
	}
	var chkdefsalesrepYN="0";
	if($('input:radio[name=chkdefsalesrepYN]:checked').val()==="Yes"){
		chkdefsalesrepYN="1";
	}
	var chkdefdivisionYN="0";
	if($('input:radio[name=chkdefdivisionYN]:checked').val()==="Yes"){
		chkdefdivisionYN="1";
	}
	var chkaddendumYN="0";
	if($('input:radio[name=chkaddendumYN]:checked').val()==="Yes"){
		chkaddendumYN="1";
	}
	var chkdefcreditYN="0";
	if($('input:radio[name=chkdefcreditYN]:checked').val()==="Yes"){
		chkdefcreditYN="1";
	}
	var chkdefaultquoteYN="0";
	if($('input:radio[name=chkdefaultquoteYN]:checked').val()==="Yes"){
		chkdefaultquoteYN="1";
	}
	var chkjobdetailotherlblYN="0";
	if($('input:radio[name=chkjobdetailotherlblYN]:checked').val()==="Yes"){
		chkjobdetailotherlblYN="1";
	}
	var chkeditsentquoteYN="0";
	if($('input:radio[name=chkeditsentquoteYN]:checked').val()==="Yes"){
		chkeditsentquoteYN="1";
	}
	
	var chkjobnoticelbl1TXT = $("#chkjobnoticelbl1TXT").val();
	var chkjobnoticereporttype1TXT = $("#chkjobnoticereporttype1TXT").val();
	var chkjobnoticelbl2TXT = $("#chkjobnoticelbl2TXT").val();
	var chkjobnoticereporttype2TXT = $("#chkjobnoticereporttype2TXT").val();
	var chkjobnoticelbl3TXT = $("#chkjobnoticelbl3TXT").val();
	var chkjobnoticereporttype3TXT = $("#chkjobnoticereporttype3TXT").val();
	var chkjodetreplabTXT = $("#chkjodetreplabTXT").val();
	var chkcloselastinvchkDays = $("#chkcloselastinvchkDays").val();
	 
     
	var sourceCheckBox1 = $("#sourceCheckBox1").val();
	var sourceCheckBox2 = $("#sourceCheckBox2").val();
	var sourceCheckBox3 = $("#sourceCheckBox3").val();
	var sourceCheckBox4 = $("#sourceCheckBox4").val();
	var sourceLabel1 = $("#sourceLabel1").val();
	var sourceLabel2 = $("#sourceLabel2").val();
	
	var chkdefJobTaxTerritoryYN = 0;
	if($('input:radio[name=chkdefJobTaxTerritoryYN]:checked').val()==="Yes"){
		chkdefJobTaxTerritoryYN="1";
		
	}
	
	var chkjodetreplabYN="0";
	if($('input:radio[name=chkjodetreplabYN]:checked').val()==="Yes"){
		chkjodetreplabYN="1";
		
	}
	var chkappcocusinvYN="0";
	if($('input:radio[name=chkappcocusinvYN]:checked').val()==="Yes"){
		chkappcocusinvYN="1";
	}
	var chkreqCPOcreatjobYN="0";
	if($('input:radio[name=chkreqCPOcreatjobYN]:checked').val()==="Yes"){
		chkreqCPOcreatjobYN="1";
	}
	
	
	var chkcloselastinvchkYN="0";
	if($('input:radio[name=chkcloselastinvchkYN]:checked').val()==="Yes"){
		chkcloselastinvchkYN="1";
	}
	var chkreqdivcreatjobYN="0";
	if($('input:radio[name=chkreqdivcreatjobYN]:checked').val()==="Yes"){
		chkreqdivcreatjobYN="1";
	}
	var chkcuscredlimcrjobsYN="0";
	if($('input:radio[name=chkcuscredlimcrjobsYN]:checked').val()==="Yes"){
		chkcuscredlimcrjobsYN="1";
	}
	var chkalwbookjobscamtYN="0";
//	alert(" chkalwbookjobscamtYN = "+chkjodetreplabYN+ " || "+$('input:radio[name=chkalwbookjobscamtYN]:checked').val());
	if($('input:radio[name=chkalwbookjobscamtYN]:checked').val()==="Yes"){
		chkalwbookjobscamtYN="1";
//		alert(" chkalwbookjobscamtYN = >>>>>>>>>> "+chkjodetreplabYN);
	}
	var chkimpponewcustomerYN="0";
	if($('input:radio[name=chkimpponewcustomerYN]:checked').val()==="Yes"){
		chkimpponewcustomerYN="1";
	}
	var chkshwbilremjobstYN="0";
	if($('input:radio[name=chkshwbilremjobstYN]:checked').val()==="Yes"){
		chkshwbilremjobstYN="1";
	}
	var chkhidepostjobsYN="0";
	if($('input:radio[name=chkhidepostjobsYN]:checked').val()==="Yes"){
		chkhidepostjobsYN="1";
	}
	var chkonalsuqottempYN="0";
	if($('input:radio[name=chkonalsuqottempYN]:checked').val()==="Yes"){
		chkonalsuqottempYN="1";
	}
	var chkalrwaichekedYN="0";
	if($('input:radio[name=chkalrwaichekedYN]:checked').val()==="Yes"){
		chkalrwaichekedYN="1";
	}
	var chkwnocrordYN="0";
	if($('input:radio[name=chkwnocrordYN]:checked').val()==="Yes"){
		chkwnocrordYN="1";
	}
	var chktkcoschordYN="0";
//	if($('input:radio[name=chktkcoschordYN]:checked').val()==="Yes"){
//		chktkcoschordYN="1";
//	}
	
	var chkUserCustomerCreditYN="0";
	if($('input:radio[name=chkUserCustomerCreditYN]:checked').val()==="Yes"){
		chkUserCustomerCreditYN="1";
	}
	
	var chkshowbranchYN="0";
	if($('input:radio[name=chkshowbranchYN]:checked').val()==="Yes"){
		chkshowbranchYN="1";
	}
	var chkscrejobstYN="0";
	if($('input:radio[name=chkscrejobstYN]:checked').val()==="Yes"){
		chkscrejobstYN="1";
	}
	var chkreqengbookjobYN="0";
	if($('input:radio[name=chkreqengbookjobYN]:checked').val()==="Yes"){
		chkreqengbookjobYN="1";
	}
	var chkReqSplitCommissionYN="0";
	if($('input:radio[name=chkSplitCommissionYN]:checked').val()==="Yes"){
		chkReqSplitCommissionYN="1";
	}
	var planSpecLabel1=$("#planSpecLabel1").val();
	var planSpecLabel2=$("#planSpecLabel2").val();
	
	var chkdefOverRideTaxTerritoryYN ="0";
	if($('input:radio[name=chkdefOverRideTaxTerritoryYN]:checked').val()==="Yes"){
		chkdefOverRideTaxTerritoryYN="1";
	}

	/* EditedBy : Naveed	Date: 26Nov2015*/
	/* Desc : ID# 454 - Quote Settings */
	
	var chkI2I3QtyYN ="0";
	if($('input:radio[name=chkI2I3QtyYN]:checked').val()==="Yes"){
		chkI2I3QtyYN="1";
	}
	var chkI2I3CostYN ="0";
	if($('input:radio[name=chkI2I3CostYN]:checked').val()==="Yes"){
		chkI2I3CostYN="1";
	}
	var chkI3SellPriceYN ="0";
	if($('input:radio[name=chkI3SellPriceYN]:checked').val()==="Yes"){
		chkI3SellPriceYN="1";
	}
	var chkI2I3ManufYN ="0";
	if($('input:radio[name=chkI2I3ManufYN]:checked').val()==="Yes"){
		chkI2I3ManufYN="1";
	}
	var chkI2I3CatYN ="0";
	if($('input:radio[name=chkI2I3CatYN]:checked').val()==="Yes"){
		chkI2I3CatYN="1";
	}
	var chkSPpriceYN ="0";
	if($('input:radio[name=chkSPpriceYN]:checked').val()==="Yes"){
		chkSPpriceYN="1";
	}
	
	
	var chkfontsizeonPriceYN ="0";
	var fontsizeonPriceValue ="";
	if($('input:radio[name=chkfontsizeonPricelblYN]:checked').val()==="Yes"){
		chkfontsizeonPriceYN="1";
		fontsizeonPriceValue = $("#fontsizeonPrice").val();
	}

	
	
//	alert(" chkUserCustomerCreditYN = "+chkUserCustomerCreditYN);
//	alert('chkjobcheckoffYN = '+chkjobcheckoffYN+' chkchangejobYN '+chkchangejobYN+'chkmemoYN'+chkmemoYN+'chkjobnoticelbl1YN'+chkjobnoticelbl1YN+
//        	'chkjobnoticereporttype1YN'+chkjobnoticereporttype1YN+'chkjobnoticelbl2YN'+chkjobnoticelbl2YN+'chkjobnoticereporttype2YN'+chkjobnoticereporttype2YN+
//        	'chkjobnoticelbl3YN'+chkjobnoticelbl3YN+'chkjobnoticereporttype3YN'+chkjobnoticereporttype3YN+
//        	'chkjobnoticelbl1TXT'+ chkjobnoticelbl1TXT+'chkjobnoticereporttype1TXT' + chkjobnoticereporttype1TXT+'chkjobnoticelbl2TXT' +chkjobnoticelbl2TXT+
//        	'chkjobnoticereporttype2TXT'+chkjobnoticereporttype2TXT+'chkjobnoticelbl3TXT'+chkjobnoticelbl3TXT+'chkjobnoticereporttype3TXT'+chkjobnoticereporttype3TXT+
//        	'chkjodetreplabTXT'+chkjodetreplabTXT+'chkcloselastinvchkDays'+chkcloselastinvchkDays+
//        	'chkbidtocurdateYN'+chkbidtocurdateYN+'chkdeftakeoffYN'+chkdeftakeoffYN+'chkdefsalesrepYN'+chkdefsalesrepYN+'chkdefdivisionYN'+chkdefdivisionYN+'chkaddendumYN'+chkaddendumYN+
//			'chkdefcreditYN'+chkdefcreditYN+'chkdefaultquoteYN'+chkdefaultquoteYN+'chkjobdetailotherlblYN'+chkjobdetailotherlblYN+
//			'chkeditsentquoteYN'+chkeditsentquoteYN+'sourceCheckBox1'+sourceCheckBox1+'sourceCheckBox2'+sourceCheckBox2+'sourceCheckBox3'+sourceCheckBox3+'sourceCheckBox4'+sourceCheckBox4+
//			'sourceLabel1'+sourceLabel1+'sourceLabel2'+sourceLabel2+
//			'chkjodetreplabYN'+chkjodetreplabYN+'chkappcocusinvYN'+chkappcocusinvYN+'chkcloselastinvchkYN'+chkcloselastinvchkYN+
//			'chkreqdivcreatjobYN'+chkreqdivcreatjobYN+'chkcuscredlimcrjobsYN'+chkcuscredlimcrjobsYN+'chkalwbookjobscamtYN'+chkalwbookjobscamtYN+
//			'chkimpponewcustomerYN'+chkimpponewcustomerYN+'chkshwbilremjobstYN'+chkshwbilremjobstYN+'chkhidepostjobsYN'+chkhidepostjobsYN+
//			'chkonalsuqottempYN'+chkonalsuqottempYN+'chkalrwaichekedYN'+chkalrwaichekedYN+'chkwnocrordYN'+chkwnocrordYN+'chktkcoschordYN'+chktkcoschordYN+
//			'chkshowbranchYN'+chkshowbranchYN+'chkscrejobstYN'+chkscrejobstYN+'chkreqengbookjobYN'+chkreqengbookjobYN+'planSpecLabel1'+planSpecLabel1+
//			'planSpecLabel2'+planSpecLabel2);
	
	$.ajax({
        url: "./inventoryList/saveJobSettingsSysVariable",
        data: {'chkjobcheckoffYN':chkjobcheckoffYN, 'chkchangejobYN':chkchangejobYN, 'chkmemoYN':chkmemoYN, 'chkjobnoticelbl1YN':chkjobnoticelbl1YN,
        	'chkjobnoticereporttype1YN':chkjobnoticereporttype1YN, 'chkjobnoticelbl2YN':chkjobnoticelbl2YN, 'chkjobnoticereporttype2YN':chkjobnoticereporttype2YN,
        	'chkjobnoticelbl3YN':chkjobnoticelbl3YN, 'chkjobnoticereporttype3YN':chkjobnoticereporttype3YN, 
        	'chkjobnoticelbl1TXT': chkjobnoticelbl1TXT, 'chkjobnoticereporttype1TXT' : chkjobnoticereporttype1TXT, 'chkjobnoticelbl2TXT' :chkjobnoticelbl2TXT,
        	'chkjobnoticereporttype2TXT':chkjobnoticereporttype2TXT, 'chkjobnoticelbl3TXT':chkjobnoticelbl3TXT, 'chkjobnoticereporttype3TXT':chkjobnoticereporttype3TXT,
        	'chkjodetreplabTXT':chkjodetreplabTXT, 'chkcloselastinvchkDays':chkcloselastinvchkDays,
        	'chkbidtocurdateYN':chkbidtocurdateYN, 'chkdeftakeoffYN':chkdeftakeoffYN, 'chkdefsalesrepYN':chkdefsalesrepYN, 'chkdefdivisionYN':chkdefdivisionYN, 'chkaddendumYN':chkaddendumYN,
			'chkdefcreditYN':chkdefcreditYN, 'chkdefaultquoteYN':chkdefaultquoteYN, 'chkjobdetailotherlblYN':chkjobdetailotherlblYN,
			'chkeditsentquoteYN':chkeditsentquoteYN, 'sourceCheckBox1':sourceCheckBox1, 'sourceCheckBox2':sourceCheckBox2, 'sourceCheckBox3':sourceCheckBox3, 'sourceCheckBox4':sourceCheckBox4,
			'sourceLabel1':sourceLabel1, 'sourceLabel2':sourceLabel2,
			'chkjodetreplabYN':chkjodetreplabYN, 'chkappcocusinvYN':chkappcocusinvYN, 'chkcloselastinvchkYN':chkcloselastinvchkYN,
			'chkreqdivcreatjobYN':chkreqdivcreatjobYN,'chkreqCPOcreatjobYN':chkreqCPOcreatjobYN, 'chkcuscredlimcrjobsYN':chkcuscredlimcrjobsYN, 'chkalwbookjobscamtYN':chkalwbookjobscamtYN,
			'chkimpponewcustomerYN':chkimpponewcustomerYN, 'chkshwbilremjobstYN':chkshwbilremjobstYN, 'chkhidepostjobsYN':chkhidepostjobsYN,
			'chkonalsuqottempYN':chkonalsuqottempYN, 'chkalrwaichekedYN':chkalrwaichekedYN, 'chkwnocrordYN':chkwnocrordYN, 'chktkcoschordYN':chktkcoschordYN,
			'chkshowbranchYN':chkshowbranchYN, 'chkscrejobstYN':chkscrejobstYN, 'chkreqengbookjobYN':chkreqengbookjobYN, 'planSpecLabel1':planSpecLabel1,
			'planSpecLabel2':planSpecLabel2, 'chkdefJobTaxTerritoryYN':chkdefJobTaxTerritoryYN, 'chkUserCustomerCreditYN' : chkUserCustomerCreditYN,'chkReqSplitCommissionYN':chkReqSplitCommissionYN,
			'chkdefOverRideTaxTerritoryYN':chkdefOverRideTaxTerritoryYN,'chkI2I3QtyYN':chkI2I3QtyYN,'chkI2I3CostYN':chkI2I3CostYN,'chkI3SellPriceYN':chkI3SellPriceYN,
			'chkI2I3ManufYN':chkI2I3ManufYN,'chkI2I3CatYN':chkI2I3CatYN,'chkSPpriceYN':chkSPpriceYN,'chkfontsizeonPriceYN':chkfontsizeonPriceYN,'fontsizeonPriceValue':fontsizeonPriceValue },
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Save Job Settings','Info','Company-Settings,Saving Job Settings');
        	$("#but_jobsettings").text("Settings Updated Successfully");
        	setTimeout(function(){
				$('#but_jobsettings').text("");
			},3000);
        }
   });
	
}

$("#inp_cusNewSeqNumCuInvoices").keypress(function(){
	$('#newCustInvoiceNumberMsg').text("");
	
});

$("#inp_cusNewSeqNumCuInvoices").keydown(function(e){
    var key = e.charCode || e.keyCode || 0;
    // allow backspace, tab, delete, enter, arrows, numbers and keypad numbers ONLY
    // home, end, period, and numpad decimal
    return (
        key == 8 || 
        key == 9 ||
        key == 13 ||
        key == 46 ||
        key == 110 ||
        key == 190 ||
        (key >= 35 && key <= 40) ||
        (key >= 48 && key <= 57) ||
        (key >= 96 && key <= 105));
 });


function saveCompanySettingsSysVariable() {
	var inpCusNewSeqNumCuInvoicesID = $('#inp_cusNewSeqNumCuInvoices').val();
	var maxInvoiceNumber = '0';

	$.ajax({
        url: "./userlistcontroller/getMaxInvoiceNumber",
        data: {'userID':0},
        async: false,
        type: 'GET',
        success: function(datas){
        	maxInvoiceNumber = datas;
        }
   });
	
	if(Number(inpCusNewSeqNumCuInvoicesID)<=Number(maxInvoiceNumber)){
		$("#newCustInvoiceNumberMsg").text("Invoices Created < #"+maxInvoiceNumber);
		return false;
	}
	
	var txt_overridewarehouse=$("#txt_overridewarehouse").val();
	
	var chk_cusStGpbyJobYes="0";
	if($('input:radio[id=chk_cusStGpbyJobYes]:checked').val()==="yes"){
		chk_cusStGpbyJobYes="1";
	}
	var chk_cusStShowBillRemYes="0";
	if($('input:radio[id=chk_cusStShowBillRemYes]:checked').val()==="yes"){
		chk_cusStShowBillRemYes="1";
	}
	var chk_cusCreLiminSalOrdYes="0";
	if($('input:radio[id=chk_cusCreLiminSalOrdYes]:checked').val()==="yes"){
		chk_cusCreLiminSalOrdYes="1";
	}
	var chk_cusCreLiminQuickBookYes="0";
	if($('input:radio[id=chk_cusCreLiminQuickBookYes]:checked').val()==="yes"){
		chk_cusCreLiminQuickBookYes="1";
	}
	var chk_taxTerCuInvAftSaveYes="0"
	if($('input:radio[id=chk_taxTerCuInvAftSaveYes]:checked').val()==="yes"){
			chk_taxTerCuInvAftSaveYes="1";
	}	
	var chk_cusReqDivinSalOrdYes="0";
	if($('input:radio[id=chk_cusReqDivinSalOrdYes]:checked').val()==="yes"){
		chk_cusReqDivinSalOrdYes="1";
	}
	var chk_cusReqDivinCusInvYes="0";
	if($('input:radio[id=chk_cusReqDivinCusInvYes]:checked').val()==="yes"){
		chk_cusReqDivinCusInvYes="1";
	}
	//Added by leo - ID#509
	var chk_cusRemExtListfmSalOrdpdfYes="0";
	if($('input:radio[id=chk_cusRemExtListfmSalOrdpdfYes]:checked').val()==="yes"){
		chk_cusRemExtListfmSalOrdpdfYes="1";
	}
	var chk_cusRemMultfmSalOrdpdfYes="0";
	if($('input:radio[id=chk_cusRemMultfmSalOrdpdfYes]:checked').val()==="yes"){
		chk_cusRemMultfmSalOrdpdfYes="1";
	}
	//
	var chk_cusIncSalTaxYes="0";
	if($('input:radio[id=chk_cusIncSalTaxYes]:checked').val()==="yes"){
		chk_cusIncSalTaxYes="1";
	}
	var chk_cusIncFreightYes="0";
	if($('input:radio[id=chk_cusIncFreightYes]:checked').val()==="yes"){
		chk_cusIncFreightYes="1";
	}
	
	var chk_cusIncListYes="0";
	if($('input:radio[id=chk_cusIncListYes]:checked').val()==="yes"){
		chk_cusIncListYes="1";
	}
	var chk_cusIncExtListYes="0";
	if($('input:radio[id=chk_cusIncExtListYes]:checked').val()==="yes"){
		chk_cusIncExtListYes="1";
	}
	var chk_cusIncMultYes="0";
	if($('input:radio[id=chk_cusIncMultYes]:checked').val()==="yes"){
		chk_cusIncMultYes="1";
	}
	
	
	var chk_cusUseDivAddYes="0";
	if($('input:radio[id=chk_cusUseDivAddYes]:checked').val()==="yes"){
		chk_cusUseDivAddYes="1";
	}
	var chk_cusAllblCusInvYes="0";
	if($('input:radio[id=chk_cusAllblCusInvYes]:checked').val()==="yes"){
		chk_cusAllblCusInvYes="1";
	}
	var chk_cusAllblCusInvYes="0";
	if($('input:radio[id=chk_cusAllblCusInvYes]:checked').val()==="yes"){
		chk_cusAllblCusInvYes="1";
	}
	var chk_cusAllblProdinSalOrdYes="0";
	if($('input:radio[id=chk_cusAllblProdinSalOrdYes]:checked').val()==="yes"){
		chk_cusAllblProdinSalOrdYes="1";
	}
	var chk_cusReqProinSalOrderYes="0";
	if($('input:radio[id=chk_cusReqProinSalOrderYes]:checked').val()==="yes"){
		chk_cusReqProinSalOrderYes="1";
	}
	var chk_cusReqfreinCuInvoicesYes="0";
	if($('input:radio[id=chk_cusReqfreinCuInvoicesYes]:checked').val()==="yes"){
		chk_cusReqfreinCuInvoicesYes="1";
	}
	var chk_cusReqSeqNumCuInvoicesYes="0";
	if($('input:radio[id=chk_cusReqSeqNumCuInvoicesYes]:checked').val()==="yes"){
		chk_cusReqSeqNumCuInvoicesYes="1";
	}
	
	var inp_cusNewSeqNumCuInvoices = "";
	if($('#inp_cusNewSeqNumCuInvoices').val() != null && $('#inp_cusNewSeqNumCuInvoices').val().length>0){
		inp_cusNewSeqNumCuInvoices = $('#inp_cusNewSeqNumCuInvoices').val();
	}
	
//	alert('chk_cusStGpbyJobYes'+chk_cusStGpbyJobYes+'chk_cusStShowBillRemYes'+chk_cusStShowBillRemYes+'chk_cusCreLiminSalOrdYes'+chk_cusCreLiminSalOrdYes+
//        	'chk_cusCreLiminQuickBookYes'+chk_cusCreLiminQuickBookYes+'chk_cusReqDivinSalOrdYes'+chk_cusReqDivinSalOrdYes+'chk_cusReqDivinCusInvYes'+chk_cusReqDivinCusInvYes+
//        	'chk_cusIncSalTaxYes'+chk_cusIncSalTaxYes+'chk_cusIncFreightYes'+chk_cusIncFreightYes+'chk_cusUseDivAddYes'+chk_cusUseDivAddYes+'chk_cusAllblCusInvYes'+chk_cusAllblCusInvYes+
//        	'chk_cusAllblCusInvYes'+chk_cusAllblCusInvYes+'chk_cusAllblProdinSalOrdYes'+chk_cusAllblProdinSalOrdYes+'chk_cusReqProinSalOrderYes'+chk_cusReqProinSalOrderYes);
	$.ajax({
        url: "./inventoryList/saveCustomerSettingsSysVariable",
      //chk_cusStGpbyJobYes chk_cusStShowBillRemYes chk_cusCreLiminSalOrdYes chk_cusCreLiminQuickBookYes chk_cusReqDivinSalOrdYes chk_cusReqDivinCusInvYes
    	//chk_cusIncSalTaxYes chk_cusIncFreightYes chk_cusUseDivAddYes chk_cusallowblinSalOrdYes chk_cusAllblCusInvYes chk_cusAllblProdinSalOrdYes chk_cusReqProinSalOrderYes
        data: {'chk_cusStGpbyJobYes':chk_cusStGpbyJobYes, 'chk_cusStShowBillRemYes':chk_cusStShowBillRemYes, 'chk_cusCreLiminSalOrdYes':chk_cusCreLiminSalOrdYes,
        	'chk_cusCreLiminQuickBookYes':chk_cusCreLiminQuickBookYes,'chk_taxTerCuInvAftSaveYes':chk_taxTerCuInvAftSaveYes ,'chk_cusReqDivinSalOrdYes':chk_cusReqDivinSalOrdYes, 'chk_cusReqDivinCusInvYes':chk_cusReqDivinCusInvYes,
        	'chk_cusIncSalTaxYes':chk_cusIncSalTaxYes, 'chk_cusIncFreightYes':chk_cusIncFreightYes, 'chk_cusIncListYes':chk_cusIncListYes, 'chk_cusIncExtListYes':chk_cusIncExtListYes,
        	 'chk_cusIncMultYes':chk_cusIncMultYes,'chk_cusUseDivAddYes':chk_cusUseDivAddYes, 'chk_cusAllblCusInvYes':chk_cusAllblCusInvYes,
        	'chk_cusAllblCusInvYes':chk_cusAllblCusInvYes, 'chk_cusAllblProdinSalOrdYes':chk_cusAllblProdinSalOrdYes, 'chk_cusReqProinSalOrderYes':chk_cusReqProinSalOrderYes,
        	'chk_cusReqfreinCuInvoicesYes':chk_cusReqfreinCuInvoicesYes,'chk_cusReqSeqNumCuInvoicesYes':chk_cusReqSeqNumCuInvoicesYes,'inp_cusNewSeqNumCuInvoices':inp_cusNewSeqNumCuInvoices,
        	'chk_cusRemExtListfmSalOrdpdfYes':chk_cusRemExtListfmSalOrdpdfYes,'chk_cusRemMultfmSalOrdpdfYes':chk_cusRemMultfmSalOrdpdfYes},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Save Customer Settings','Info','Company-Settings,Saving Customer Settings');
        	$("#but_vendorsettings").text("Settings Updated Successfully");
        	setTimeout(function(){
				$('#but_vendorsettings').text("");
			},3000);
        }
   });
	
}

function savejobsettingsdefault(){
//	  chkjobcheckoffYN chkchangejobYN chkmemoYN chkjobnoticelbl1YN  chkjobnoticereporttype1YN chkjobnoticelbl2YN chkjobnoticereporttype2YN
//      chkjobnoticelbl3YN chkjobnoticereporttype3YN chkbidtocurdateYN chkdeftakeoffYN chkdefsalesrepYN chkdefdivisionYN chkaddendumYN
//		 chkdefcreditYN chkdefaultquoteYN chkjobdetailotherlblYN chkeditsentquoteYN - chkjodetreplabYN chkappcocusinvYN 
//		 chkcloselastinvchkYN chkreqdivcreatjobYN chkcuscredlimcrjobsYN chkalwbookjobscamtYN chkimpponewcustomerYN chkshwbilremjobstYN chkhidepostjobsYN 
//		 chkonalsuqottempYN  chkalrwaichekedYN chkwnocrordYN chktkcoschordYN chkshowbranchYN chkscrejobstYN chkreqengbookjobYN
	
	var chkreqengbookjobYN="0";
	if(document.getElementById("chkreqengbookjobYN").checked){
		chkreqengbookjobYN="1";
	}
	var chkscrejobstYN="0";
	if(document.getElementById("chkscrejobstYN").checked){
		chkscrejobstYN="1";
	}
	var chkshowbranchYN="0";
	if(document.getElementById("chkshowbranchYN").checked){
		chkshowbranchYN="1";
	}
	
	var chktkcoschordYN="0";
//	if(document.getElementById("chktkcoschordYN").checked){
//		chktkcoschordYN="1";
//	}
	var chkwnocrordYN="0";
	if(document.getElementById("chkwnocrordYN").checked){
		chkwnocrordYN="1";
	}
	var chkalrwaichekedYN="0";
	if(document.getElementById("chkalrwaichekedYN").checked){
		chkalrwaichekedYN="1";
	}
	var chkonalsuqottempYN="0";
	if(document.getElementById("chkonalsuqottempYN").checked){
		chkonalsuqottempYN="1";
	}
	var chkhidepostjobsYN="0";
	if(document.getElementById("chkhidepostjobsYN").checked){
		chkhidepostjobsYN="1";
	}
	var chkshwbilremjobstYN="0";
	if(document.getElementById("chkshwbilremjobstYN").checked){
		chkshwbilremjobstYN="1";
	}
	var chkimpponewcustomerYN="0";
	if(document.getElementById("chkimpponewcustomerYN").checked){
		chkimpponewcustomerYN="1";
	}
	var chkalwbookjobscamtYN="0";
	if(document.getElementById("chkalwbookjobscamtYN").checked){
		chkalwbookjobscamtYN="1";
	}
	var chkcuscredlimcrjobsYN="0";
	if(document.getElementById("chkcuscredlimcrjobsYN").checked){
		chkcuscredlimcrjobsYN="1";
	}
	var chkreqdivcreatjobYN="0";
	if(document.getElementById("chkreqdivcreatjobYN").checked){
		chkreqdivcreatjobYN="1";
	}
	
	
	var chkreqCPOcreatjobYN="0";
	if(document.getElementById("chkreqCPOcreatjobYN").checked){
		chkreqCPOcreatjobYN="1";
	}
	
	var chkcloselastinvchkYN="0";
	if(document.getElementById("chkcloselastinvchkYN").checked){
		chkcloselastinvchkYN="1";
	}
	var chkappcocusinvYN="0";
	if(document.getElementById("chkappcocusinvYN").checked){
		chkappcocusinvYN="1";
	}
	var chkjodetreplabYN="0";
	if(document.getElementById("chkjodetreplabYN").checked){
		chkjodetreplabYN="1";
	}
	var chkeditsentquoteYN="0";
	if(document.getElementById("chkeditsentquoteYN").checked){
		chkeditsentquoteYN="1";
	}
	var chkjobdetailotherlblYN="0";
	if(document.getElementById("chkjobdetailotherlblYN").checked){
		chkjobdetailotherlblYN="1";
	}
	var chkdefaultquoteYN="0";
	if(document.getElementById("chkdefaultquoteYN").checked){
		chkdefaultquoteYN="1";
	}
	var chkdefcreditYN="0";
	if(document.getElementById("chkdefcreditYN").checked){
		chkdefcreditYN="1";
	}
	var chkaddendumYN="0";
	if(document.getElementById("chkaddendumYN").checked){
		chkaddendumYN="1";
	}
	var chkdefdivisionYN="0";
	if(document.getElementById("chkdefdivisionYN").checked){
		chkdefdivisionYN="1";
	}
	var chkdefsalesrepYN="0";
	if(document.getElementById("chkdefsalesrepYN").checked){
		chkdefsalesrepYN="1";
	}
	var chkdeftakeoffYN="0";
	if(document.getElementById("chkdeftakeoffYN").checked){
		chkdeftakeoffYN="1";
	}
	var chkbidtocurdateYN="0";
	if(document.getElementById("chkbidtocurdateYN").checked){
		chkbidtocurdateYN="1";
	}
	var chkjobnoticereporttype3YN="0";
	if(document.getElementById("chkjobnoticereporttype3YN").checked){
		chkjobnoticereporttype3YN="1";
	}
	var chkjobnoticelbl3YN="0";
	if(document.getElementById("chkjobnoticelbl3YN").checked){
		chkjobnoticelbl3YN="1";
	}
	var chkjobnoticereporttype2YN="0";
	if(document.getElementById("chkjobnoticereporttype2YN").checked){
		chkjobnoticereporttype2YN="1";
	}
	var chkjobnoticelbl2YN="0";
	if(document.getElementById("chkjobnoticelbl2YN").checked){
		chkjobnoticelbl2YN="1";
	}
	
	
	
	
	
	
	
	
	var chkjobcheckoffYN="0";
	if(document.getElementById("chkjobcheckoffYN").checked){
		chkjobcheckoffYN="1";
	} 
	var chkchangejobYN="0";
	if(document.getElementById("chkchangejobYN").checked){
		chkchangejobYN="1";
	} 
	var chkmemoYN="0";
	if(document.getElementById("chkmemoYN").checked){
		chkmemoYN="1";
	} 
	var chkjobnoticelbl1YN="0";
	if(document.getElementById("chkjobnoticelbl1YN").checked){
		chkjobnoticelbl1YN="1";
	} 
	var chkjobnoticereporttype1YN="0";
	if(document.getElementById("chkjobnoticereporttype1YN").checked){
		chkjobnoticereporttype1YN="1";
	} 
	
	
	
	
	
}


function updateUserPermission(checkboxvalue,accessProcedurename){
		//var checkedornot=$("#"+checkboxvalue).is(':checked');
		var checkedornot=$('input:checkbox[id='+checkboxvalue+']').is(':checked');
	$.ajax({
        url: "./userlistcontroller/updatesysprivilage",
        data: {'checkedornot':checkedornot,'accessProcedurename':accessProcedurename,'userGroupID':UserGroupID},
        type: 'POST',
        success: function(data){
        	
        }
   });
}
function LoadGroupDefaultCheckboxes(userGroupID){
	$.ajax({
        url: "./userlistcontroller/GetGroupDefaultPermission",
        data: {'userGroupID':userGroupID},
        type: 'GET',
        success: function(data){
        	for(var i=0;i<data.length;i++){
        		checkedcheckboxes(data[i].procedureName,data[i].privilegeValue);
        	}
        }
   });
}
function checkedcheckboxes(procedurename,privilagevalue){
	
	if(procedurename=="New Job"){
		checkboxenableOrdisable('HomenewJobID',privilagevalue);
	}else if(procedurename=="Quick book"){
		checkboxenableOrdisable('HomeQuick_bookID',privilagevalue);
	}
	
	else if(procedurename=="Main"){
		checkboxenableOrdisable('jobMainID',privilagevalue);
	}else if(procedurename=="Quotes"){
		checkboxenableOrdisable('jobQuotesID',privilagevalue);
	}else if(procedurename=="Submittal"){
		checkboxenableOrdisable('jobSubmittalID',privilagevalue);
	}else if(procedurename=="Credit"){
		checkboxenableOrdisable('jobCreditID',privilagevalue);
	}else if(procedurename=="Release"){
		checkboxenableOrdisable('jobReleaseID',privilagevalue);
	}else if(procedurename=="Financial"){
		checkboxenableOrdisable('jobFinancialID',privilagevalue);
	}else if(procedurename=="Journal"){
		checkboxenableOrdisable('jobJournalID',privilagevalue);
	}
	
	
	
	else if(procedurename=="Sales"){
		checkboxenableOrdisable('salesID',privilagevalue);
	}else if(procedurename=="Sales Filter"){
		checkboxenableOrdisable('sales_FilterID',privilagevalue);
	}
	
	else if(procedurename=="Project"){
		checkboxenableOrdisable('projectID',privilagevalue);
	}else if(procedurename=="Project Filter"){
		checkboxenableOrdisable('project_FilterID',privilagevalue);
	}
	
	else if(procedurename=="Customers"){
		checkboxenableOrdisable('CompanyCustomerID',privilagevalue);
	}else if(procedurename=="Payments"){
		checkboxenableOrdisable('CompanyPaymentsID',privilagevalue);
	}else if(procedurename=="Statements"){
		checkboxenableOrdisable('CompanyStatementsID',privilagevalue);
	}else if(procedurename=="Sales Order"){
		checkboxenableOrdisable('CompanySales_OrderID',privilagevalue);
	}else if(procedurename=="Invoice"){
		checkboxenableOrdisable('CompanyInvoiceID',privilagevalue);
	}else if(procedurename=="Finance Changes"){
		checkboxenableOrdisable('CompanyFinance_ChangesID',privilagevalue);
	}else if(procedurename=="Tax Adjustments"){
		checkboxenableOrdisable('CompanyTax_AdjustmentsID',privilagevalue);
	}else if(procedurename=="Credit/Debit Memos"){
		checkboxenableOrdisable('CompanyCreditDebit_MemosID',privilagevalue);
	}
	
		
	else if(procedurename=="Sales Order Template"){
		checkboxenableOrdisable('CompanySales_Order_TemplateID',privilagevalue);
	}else if(procedurename=="Purchase Orders"){
		checkboxenableOrdisable('CompanyPurchase_OrdersID',privilagevalue);
	}else if(procedurename=="Vendors"){
		checkboxenableOrdisable('CompanyVendorID',privilagevalue);
	}else if(procedurename=="Pay Bills"){
		checkboxenableOrdisable('CompanyPay_BillsID',privilagevalue);
	}else if(procedurename=="Invoice & Bills"){
		checkboxenableOrdisable('CompanyInvoice_BillsID',privilagevalue);
	}else if(procedurename=="Employees"){
		checkboxenableOrdisable('CompanyEmployeesID',privilagevalue);
	}else if(procedurename=="Commissions"){
		checkboxenableOrdisable('CompanyCommissionsID',privilagevalue);
	}else if(procedurename=="Rolodex"){
		checkboxenableOrdisable('CompanyRolodexID',privilagevalue);
	}else if(procedurename=="Users"){
		checkboxenableOrdisable('CompanyUsersID',privilagevalue);
	}else if(procedurename=="Settings"){
		checkboxenableOrdisable('CompanySettingsID',privilagevalue);
	}
	
	
	else if(procedurename=="Inventory"){
		checkboxenableOrdisable('inventoryID',privilagevalue);
	}else if(procedurename=="Categories"){
		checkboxenableOrdisable('inventoryCategoriesID',privilagevalue);
	}else if(procedurename=="Warehouses"){
		checkboxenableOrdisable('inventoryWarehousesID',privilagevalue);
	}else if(procedurename=="Receive Inventory"){
		checkboxenableOrdisable('inventoryReceive_InventoryID',privilagevalue);
	}else if(procedurename=="Transfer"){
		checkboxenableOrdisable('inventoryTransferID',privilagevalue);
	}else if(procedurename=="Order Points"){
		checkboxenableOrdisable('inventoryOrder_PointsID',privilagevalue);
	}else if(procedurename=="Inventory Value"){
		checkboxenableOrdisable('inventoryInventory_ValueID',privilagevalue);
	}else if(procedurename=="Count"){
		checkboxenableOrdisable('inventoryCountID',privilagevalue);
	}else if(procedurename=="Adjustments"){
		checkboxenableOrdisable('inventoryAdjustmentsID',privilagevalue);
	}else if(procedurename=="Banking"){
		checkboxenableOrdisable('BankingID',privilagevalue);
	}else if(procedurename=="Transactions"){
		checkboxenableOrdisable('inventoryTransactionsID',privilagevalue);
	}
	
	else if(procedurename=="Write Checks"){
		checkboxenableOrdisable('BankingWrite_ChecksID',privilagevalue);
	}else if(procedurename=="Reissue Checks"){
		checkboxenableOrdisable('BankingReissue_ChecksID',privilagevalue);
	}else if(procedurename=="Recouncile Accounts"){
		checkboxenableOrdisable('BankingRecouncile_AccountsID',privilagevalue);
	}else if(procedurename=="Chart Accounts"){
		checkboxenableOrdisable('FinancialChart_AccountsID',privilagevalue);
	}else if(procedurename=="Divisions"){
		checkboxenableOrdisable('FinancialDivisionsID',privilagevalue);
	}else if(procedurename=="Tax Territories"){
		checkboxenableOrdisable('FinancialTax_TerritoriesID',privilagevalue);
	}else if(procedurename=="General Ledger"){
		checkboxenableOrdisable('FinancialGeneral_LedgerID',privilagevalue);
	}else if(procedurename=="Journal Entries"){
		checkboxenableOrdisable('FinancialJournal_EntriesID',privilagevalue);
	}else if(procedurename=="Accounting Cycles"){
		checkboxenableOrdisable('FinancialAccounting_CyclesID',privilagevalue);
	}else if(procedurename=="GL Transactions"){
		checkboxenableOrdisable('FinancialGL_TransactionsID',privilagevalue);
	}else if(procedurename=="Open Period Posting Only"){
		checkboxenableOrdisable('FinancialOpenPeriod_PostingID',privilagevalue);
	}
		
	
	
		
}
function checkboxenableOrdisable(id,privilagevalue){
	if(privilagevalue==1){
		$('input:checkbox[id='+id+']').attr("checked", true);	
		//$("#"+id).attr("checked", true);	
	}else{
		$('input:checkbox[id='+id+']').attr("checked", false);
		//$("#"+id).attr("checked", false);
	}
}

function clearallCheckboxes()
{
	$("#HomenewJobID").attr("checked", false);
	$("#HomeQuick_bookID").attr("checked", false);
	$("#jobMainID").attr("checked", false);
	$("#jobQuotesID").attr("checked", false);
	$("#jobSubmittalID").attr("checked", false);
	$("#jobCreditID").attr("checked", false);
	$("#jobReleaseID").attr("checked", false);
	$("#jobFinancialID").attr("checked", false);
	$("#jobJournalID").attr("checked", false);
	$("#salesID").attr("checked", false);
	$("#sales_FilterID").attr("checked", false);
	$('input:checkbox[id=projectID]').attr("checked", false);
	//$("#projectID").attr("checked", false);
	$("#project_FilterID").attr("checked", false);
	$("#CompanyCustomerID").attr("checked", false);
	$("#CompanyPaymentsID").attr("checked", false);
	$("#CompanyStatementsID").attr("checked", false);
	$("#CompanySales_OrderID").attr("checked", false);
	$("#CompanyInvoiceID").attr("checked", false);
	$("#CompanyFinance_ChangesID").attr("checked", false);
	$("#CompanyTax_AdjustmentsID").attr("checked", false);
	$("#CompanyCreditDebit_MemosID").attr("checked", false);
	$("#CompanySales_Order_TemplateID").attr("checked", false);
	$("#CompanyVendorID").attr("checked", false);
	$("#CompanyPurchase_OrdersID").attr("checked", false);
	$("#CompanyPay_BillsID").attr("checked", false);
	$("#CompanyInvoice_BillsID").attr("checked", false);
	$("#CompanyEmployeesID").attr("checked", false);
	$("#CompanyCommissionsID").attr("checked", false);
	$("#CompanyRolodexID").attr("checked", false);
	$("#CompanyUsersID").attr("checked", false);
	$("#CompanySettingsID").attr("checked", false);
	$("#inventoryID").attr("checked", false);
	$("#inventoryCategoriesID").attr("checked", false);
	$("#inventoryWarehousesID").attr("checked", false);
	$("#inventoryReceive_InventoryID").attr("checked", false);
	$("#inventoryTransferID").attr("checked", false);
	$("#inventoryOrder_PointsID").attr("checked", false);
	$("#inventoryInventory_ValueID").attr("checked", false);
	$("#inventoryCountID").attr("checked", false);
	$("#inventoryTransactionsID").attr("checked", false);
	$("#inventoryAdjustmentsID").attr("checked", false);
	$("#BankingID").attr("checked", false);
	$("#BankingWrite_ChecksID").attr("checked", false);
	$("#BankingReissue_ChecksID").attr("checked", false);
	$("#BankingRecouncile_AccountsID").attr("checked", false);
	$("#FinancialChart_AccountsID").attr("checked", false);
	$("#FinancialDivisionsID").attr("checked", false);
	$("#FinancialTax_TerritoriesID").attr("checked", false);
	$("#FinancialGeneral_LedgerID").attr("checked", false);
	$("#FinancialJournal_EntriesID").attr("checked", false);
	$("#FinancialAccounting_CyclesID").attr("checked", false);
	$("#FinancialGL_TransactionsID").attr("checked", false);
	$("#FinancialOpenPeriod_PostingID").attr("checked", false);
}

function GroupByprocedureSelect(selectedgroup){
	if(selectedgroup=='Customer'){
		if($('#CompanyCustomerID').is(':checked')){
			checkboxenableOrdisable('CompanyCustomerID',1);
			updateUserPermission('CompanyCustomerID','Customers');
			checkboxenableOrdisable('CompanyPaymentsID',1);
			updateUserPermission('CompanyPaymentsID','Payments');
			checkboxenableOrdisable('CompanyStatementsID',1);
			updateUserPermission('CompanyStatementsID','Statements');
			checkboxenableOrdisable('CompanySales_OrderID',1);
			updateUserPermission('CompanySales_OrderID','Sales_Order');
			checkboxenableOrdisable('CompanyInvoiceID',1);
			updateUserPermission('CompanyInvoiceID','Invoice');
			checkboxenableOrdisable('CompanyFinance_ChangesID',1);
			updateUserPermission('CompanyFinance_ChangesID','Finance_Changes');
			checkboxenableOrdisable('CompanyTax_AdjustmentsID',1);
			updateUserPermission('CompanyTax_AdjustmentsID','Tax_Adjustments');
			checkboxenableOrdisable('CompanyCreditDebit_MemosID',1);
			updateUserPermission('CompanyCreditDebit_MemosID','CreditDebit_Memos');
			checkboxenableOrdisable('CompanySales_Order_TemplateID',1);
			updateUserPermission('CompanySales_Order_TemplateID','Sales_Order_Template');
		}else{
			checkboxenableOrdisable('CompanyCustomerID',-1);
			updateUserPermission('CompanyCustomerID','Customers');
			checkboxenableOrdisable('CompanyPaymentsID',-1);
			updateUserPermission('CompanyPaymentsID','Payments');
			checkboxenableOrdisable('CompanyStatementsID',-1);
			updateUserPermission('CompanyStatementsID','Statements');
			checkboxenableOrdisable('CompanySales_OrderID',-1);
			updateUserPermission('CompanySales_OrderID','Sales_Order');
			checkboxenableOrdisable('CompanyInvoiceID',-1);
			updateUserPermission('CompanyInvoiceID','Invoice');
			checkboxenableOrdisable('CompanyFinance_ChangesID',-1);
			updateUserPermission('CompanyFinance_ChangesID','Finance_Changes');
			checkboxenableOrdisable('CompanyTax_AdjustmentsID',-1);
			updateUserPermission('CompanyTax_AdjustmentsID','Tax_Adjustments');
			checkboxenableOrdisable('CompanyCreditDebit_MemosID',-1);
			updateUserPermission('CompanyCreditDebit_MemosID','CreditDebit_Memos');
			checkboxenableOrdisable('CompanySales_Order_TemplateID',-1);
			updateUserPermission('CompanySales_Order_TemplateID','Sales_Order_Template');
		}
	}else if(selectedgroup=='Vendor'){
		if($('#CompanyVendorID').is(':checked')){
			checkboxenableOrdisable('CompanyVendorID',1);
			updateUserPermission('CompanyVendorID','Vendors');
			checkboxenableOrdisable('CompanyPurchase_OrdersID',1);
			updateUserPermission('CompanyPurchase_OrdersID','Purchase_Orders');
			checkboxenableOrdisable('CompanyPay_BillsID',1);
			updateUserPermission('CompanyPay_BillsID','Pay_Bills');
			checkboxenableOrdisable('CompanyInvoice_BillsID',1);
			updateUserPermission('CompanyInvoice_BillsID','Invoice_Bills');
			
		}else{
			checkboxenableOrdisable('CompanyVendorID',-1);
			updateUserPermission('CompanyVendorID','Vendors');
			checkboxenableOrdisable('CompanyPurchase_OrdersID',-1);
			updateUserPermission('CompanyPurchase_OrdersID','Purchase_Orders');
			checkboxenableOrdisable('CompanyPay_BillsID',-1);
			updateUserPermission('CompanyPay_BillsID','Pay_Bills');
			checkboxenableOrdisable('CompanyInvoice_BillsID',-1);
			updateUserPermission('CompanyInvoice_BillsID','Invoice_Bills');
			
		}
	}else if(selectedgroup=='Employee'){
		if($('#CompanyEmployeesID').is(':checked')){
				checkboxenableOrdisable('CompanyEmployeesID',1);
				updateUserPermission('CompanyEmployeesID','Employees');
				checkboxenableOrdisable('CompanyCommissionsID',1);
				updateUserPermission('CompanyCommissionsID','Commissions');
			}else{
				checkboxenableOrdisable('CompanyEmployeesID',-1);
				updateUserPermission('CompanyEmployeesID','Employees');
				checkboxenableOrdisable('CompanyCommissionsID',-1);
				updateUserPermission('CompanyCommissionsID','Commissions');
			}
	}else if(selectedgroup=='Sales'){
		if($('#salesID').is(':checked')){
			    checkboxenableOrdisable('salesID',1);
				updateUserPermission('salesID','Sales');
				checkboxenableOrdisable('sales_FilterID',1);
				updateUserPermission('sales_FilterID','Sales_Filter');
			}else{
				checkboxenableOrdisable('salesID',-1);
				updateUserPermission('salesID','Sales');
				checkboxenableOrdisable('sales_FilterID',-1);
				updateUserPermission('sales_FilterID','Sales_Filter');
			}
	}else if(selectedgroup=='Project'){
		
		if($('input:checkbox[id=projectID]').is(':checked')){
				checkboxenableOrdisable('projectID',1);
				updateUserPermission('projectID','Project');
				checkboxenableOrdisable('project_FilterID',1);
				updateUserPermission('project_FilterID','Project_Filter'); 
			}else{
				checkboxenableOrdisable('projectID',-1);
				updateUserPermission('projectID','Project');
				checkboxenableOrdisable('project_FilterID',-1);
				updateUserPermission('project_FilterID','Project_Filter');
			}
	}else if(selectedgroup=='Banking'){
		if($('#BankingID').is(':checked')){
				checkboxenableOrdisable('BankingID',1);
				updateUserPermission('BankingID','Banking');
				checkboxenableOrdisable('BankingWrite_ChecksID',1);
				updateUserPermission('BankingWrite_ChecksID','Write_Checks');
				checkboxenableOrdisable('BankingReissue_ChecksID',1);
				updateUserPermission('BankingReissue_ChecksID','Reissue_Checks');
				checkboxenableOrdisable('BankingRecouncile_AccountsID',1);
				updateUserPermission('BankingRecouncile_AccountsID','Recouncile_Accounts');
			}else{
				checkboxenableOrdisable('BankingID',-1);
				updateUserPermission('BankingID','Banking');
				checkboxenableOrdisable('BankingWrite_ChecksID',-1);
				updateUserPermission('BankingWrite_ChecksID','Write_Checks');
				checkboxenableOrdisable('BankingReissue_ChecksID',-1);
				updateUserPermission('BankingReissue_ChecksID','Reissue_Checks');
				checkboxenableOrdisable('BankingRecouncile_AccountsID',-1);
				updateUserPermission('BankingRecouncile_AccountsID','Recouncile_Accounts');
				
			}
	}else if(selectedgroup=='Inventory'){
		if($('#inventoryID').is(':checked')){
				checkboxenableOrdisable('inventoryID',1);
				updateUserPermission('inventoryID','Inventory');
				checkboxenableOrdisable('inventoryCategoriesID',1);
				updateUserPermission('inventoryCategoriesID','Categories');
				checkboxenableOrdisable('inventoryWarehousesID',1);
				updateUserPermission('inventoryWarehousesID','Warehouses');
				checkboxenableOrdisable('inventoryReceive_InventoryID',1);
				updateUserPermission('inventoryReceive_InventoryID','Receive_Inventory');
				checkboxenableOrdisable('inventoryTransferID',1);
				updateUserPermission('inventoryTransferID','Transfer');
				checkboxenableOrdisable('inventoryOrder_PointsID',1);
				updateUserPermission('inventoryOrder_PointsID','Order_Points');
				checkboxenableOrdisable('inventoryInventory_ValueID',1);
				updateUserPermission('inventoryInventory_ValueID','Inventory_Value');
				checkboxenableOrdisable('inventoryCountID',1);
				updateUserPermission('inventoryCountID','Count'); 
				checkboxenableOrdisable('inventoryTransactionsID',1);
				updateUserPermission('inventoryTransactionsID','Transactions');
				checkboxenableOrdisable('inventoryAdjustmentsID',1);
				updateUserPermission('inventoryAdjustmentsID','Adjustments');
				
			}else{
				checkboxenableOrdisable('inventoryID',-1);
				updateUserPermission('inventoryID','Inventory');
				checkboxenableOrdisable('inventoryCategoriesID',-1);
				updateUserPermission('inventoryCategoriesID','Categories');
				checkboxenableOrdisable('inventoryWarehousesID',-1);
				updateUserPermission('inventoryWarehousesID','Warehouses');
				checkboxenableOrdisable('inventoryReceive_InventoryID',-1);
				updateUserPermission('inventoryReceive_InventoryID','Receive_Inventory');
				checkboxenableOrdisable('inventoryTransferID',-1);
				updateUserPermission('inventoryTransferID','Transfer');
				checkboxenableOrdisable('inventoryOrder_PointsID',-1);
				updateUserPermission('inventoryOrder_PointsID','Order_Points');
				checkboxenableOrdisable('inventoryInventory_ValueID',-1);
				updateUserPermission('inventoryInventory_ValueID','Inventory_Value');
				checkboxenableOrdisable('inventoryCountID',-1);
				updateUserPermission('inventoryCountID','Count'); 
				checkboxenableOrdisable('inventoryTransactionsID',-1);
				updateUserPermission('inventoryTransactionsID','Transactions');
				checkboxenableOrdisable('inventoryAdjustmentsID',-1);
				updateUserPermission('inventoryAdjustmentsID','Adjustments');
				
			}
	}
	
	
}
function updateJobTabClick(divid,accessProcedurename){
	if($('input:checkbox[id='+divid+']').is(':checked')){
		checkboxenableOrdisable('jobMainID',1);
		updateUserPermission('jobMainID','Main');
		updateUserPermission(divid,accessProcedurename);
	}else{
		updateUserPermission(divid,accessProcedurename);
	}
}

function onclickjobmaintab(divid,accessProcedurename){
	if($('input:checkbox[id='+divid+']').is(':checked')){
		updateUserPermission('jobMainID','Main');
	}else{
		updateUserPermission('jobMainID','Main');
		checkboxenableOrdisable('jobQuotesID',-1);
		updateUserPermission('jobQuotesID','Quotes');
		checkboxenableOrdisable('jobSubmittalID',-1);
		updateUserPermission('jobSubmittalID','Submittal');
		checkboxenableOrdisable('jobCreditID',-1);
		updateUserPermission('jobCreditID','Credit');
		checkboxenableOrdisable('jobReleaseID',-1);
		updateUserPermission('jobReleaseID','Release');
		checkboxenableOrdisable('jobFinancialID',-1);
		updateUserPermission('jobFinancialID','Financial');
		checkboxenableOrdisable('jobJournalID',-1);
		updateUserPermission('jobJournalID','Journal');
	}
}

$(function() {  var cache = {}; var lastXhr=''; $("#prodCodeID").autocomplete({minLength: 1,timeout :1000,
	source: "jobtabs3/productCodeWithNameList",
	select: function( event, ui ){
		var ID = ui.item.id;
		var product = ui.item.label;
		$("#prodID").val(ui.item.id);
		$.ajax({
	        url: './getLineItemsSO?prMasterId='+ID,
	        type: 'POST',       
	        success: function (data) {
	        	$.each(data, function(key, valueMap) {
	        		if("lineItems"==key)
					{				
						$.each(valueMap, function(index, value){
							$("#prodDescID").val(value.description);
						});
					}	
				});
	        }
	    });
		},
	error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
	});
	});

function loadProductSettings(deptID,prodID){
	$.ajax({
        url: './getLineItemsSO?prMasterId='+prodID,
        type: 'POST',       
        success: function (data) {
        	$.each(data, function(key, valueMap) {
        		if("lineItems"==key)
				{				
					$.each(valueMap, function(index, value){
						$("#prodCodeID").val(value.itemCode);
						$("#prodDescID").val(value.description);
					});
				}	
			});
        }
    });
	$("#prodDepartmentId option[value='" +deptID+ "']").attr("selected", true);
}

function saveProductsSetting(){
	var prodCode = $('#prodCodeID').val();
	
	var prodDepartment =  $('#prodDepartmentId').val();
	var prodId = $('#prodID').val();
	var errorTxt = '';
	if(prodCode==''){
		$("#prodCodeNameMsg").empty();
		$("#prodCodeNameMsg").append('Please Select any Product');
		setTimeout(function(){
			$("#prodCodeNameMsg").empty();						
			},2000);
	}else if(prodDepartment<0){
		$("#prodDeptMsg").empty();
		$("#prodDeptMsg").append('please select any one Detpartment');
		setTimeout(function(){
			$("#prodDeptMsg").empty();						
			},2000);
		
	}
	else{
	$.ajax({
        url: "./userlistcontroller/updateVendorProductDetails",
        data: {'vendorProductID':prodId,'vendorProductDeptID':prodDepartment,'companyID':1},
        type: 'POST',
        success: function(data){
        	$("#vendorServiceID").append('Vendor Product/Service saved successfully !');
    		setTimeout(function(){
    			$("#vendorServiceID").empty();						
    			},2000);
        }
   });
	}
	}

function saveVendorSettings() {
	
	var chk_venpoDesStatusYN ="0";
	if($("#chk_venpoDesYes").is(':checked')){
		chk_venpoDesStatusYN="1";
	}
	var chk_vencmpanylogoStatusYN ="0";
	if($("#chk_vencmpanylogoYes").is(':checked')){
		chk_vencmpanylogoStatusYN="1";
	}
	var chk_vendorPhnoYesYN ="0";
	if($("#chk_vendorPhnoYes").is(':checked')){
		chk_vendorPhnoYesYN="1";
	}
	var chk_venproductidStatusYN ="0";
	if($("#chk_venproductidYes").is(':checked')){
		chk_venproductidStatusYN="1";
	}
	var chk_venclosePoStatusYN ="0";
	if($("#chk_venclosePoYes").is(':checked')){
		chk_venclosePoStatusYN="1";
	}
	var chk_venreqInvStatusYN ="0";
	if($("#chk_venreqInvYes").is(':checked')){
		chk_venreqInvStatusYN="1";
	}
	var chk_vendefaultInvStatusYN ="0";
	if($("#chk_vendefaultInvYes").is(':checked')){
		chk_vendefaultInvStatusYN="1";
	}
	var chk_IncTaxPOInvStatusYN ="0";
	if($("#chk_IncTaxPOInvYes").is(':checked')){
		chk_IncTaxPOInvStatusYN="1";
	}
	
	
	
	$.ajax({
        //url: "./inventoryList/saveJobSettingsSysVariable",
		url: "./inventoryList/saveVendorSettings",
        data: {'chk_venpoDesStatusYN':chk_venpoDesStatusYN,'chk_vencmpanylogoStatusYN':chk_vencmpanylogoStatusYN,'chk_vendorPhnoYesYN':chk_vendorPhnoYesYN,
        		'chk_venproductidStatusYN':chk_venproductidStatusYN,'chk_venclosePoStatusYN':chk_venclosePoStatusYN,
        	'chk_venreqInvStatusYN':chk_venreqInvStatusYN,'chk_vendefaultInvStatusYN':chk_vendefaultInvStatusYN,'chk_IncTaxPOInvStatusYN':chk_IncTaxPOInvStatusYN},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Save Vendor Settings','Info','Vendor-Settings,Saving Vendor Settings');
        	$("#but_vndrsettings").text("Settings Updated Successfully");
        	setTimeout(function(){
				$('#but_vndrsettings').text("");
			},3000);
        }
   });
	
}