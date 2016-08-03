jQuery(document).ready(function(){
	$("#soreleasetab").tabs();
	$("#sentdate").css("display","none");
	$("#resentdate").css("display","none");
	$("#approveddate").css("display","none");
	$("#requesteddate").css("display","none");
	$("#OMsentdate").css("display","none");
	$("#OriJobNumber").css("display", "none");
	var aQuoteNumber = $("#quoteNumberID").val();
	if(aQuoteNumber !== ''){
		$("#OriJobNumber").show();
	}
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Booked"){
		$(".customerNameField").attr("disabled", true);
	}
	parseRequestModelMap();
	var submittalMainID;
	submittalMainID = $("#submittalMainHeaderID").empty();
	submittalMainID = $("#submittalMainHeaderID").val();
	loadSubmittalListGrid(submittalMainID);
});

var newDialogDiv = jQuery(document.createElement('div'));
var errorText = '';
var aSubManufacturerId;
var ManufactName;
var joreleaseID;

function loadSubmittalListGrid(submittalMainID){
	//var jobNumber = $("#jobNumber_ID").text();
	$("#submittalList").jqGrid({
		url:'./jobtabs3/submittalProducts',
		datatype: 'JSON',
		mtype: 'GET',
		postData: { 'submittalHeaderID' : submittalMainID },
		pager:jQuery("#submittalListPager"),
		colNames:['Schedule', 'Product','Qty','Released', 'Manufacturer', 'Est. Cost ($)', 'Cost ($)', 'Status', 'JoSubmittalDetailID', 'Schedule', 'ManufatureID', 'JoSchdTempHeaderID', 'joSubmittalHeaderID'],
		colModel :[
			{name:'schedule',index:'schedule',align:'left',width:90,editable:true,hidden: true, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata)
							{return 'style="white-space: normal" ';}, editoptions:{size:25},editrules:{edithidden: false,required: false}},
           	{name:'product', index:'product', align:'left', width:120, editable:true,hidden:false, edittype:'text',
           					editoptions:{size: 25},editrules:{edithidden:false,required: true}},
			{name:'quantity', index:'quantity', align:'center', width:30,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'released', index:'released', align:'center', width:80,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'manufacture', index:'manufacture', align:'left', width:120,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},editrules:{edithidden:true,required: false}},
			{name:'estimatecost', index:'estimatecost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter,
								editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'cost', index:'cost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter,
								editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required: false}},
			{name:'status', index:'status', align:'center', width:50, hidden:false, editable:true, edittype:'text', edittype:'select',  formatter:statusFormatter,
									editoptions:{value:{'':'',0:'Open', 1:'1/2 Part.', 2:'Rel.'}}, editrules:{edithidden:true,required:false}},
			{name:'joSubmittalID',index:'joSubmittalID',align:'right',width:40,editable:true,hidden: true, edittype:'text', editoptions:{size:20},editrules:{edithidden:false,required:false}},
			{name:'scheduleIcon',index:'scheduleIcon',align:'right',width:40,editable:true,hidden: false, edittype:'text', formatter:imgFmatter,  editoptions:{size:20},editrules:{edithidden: true,required:false}},
			{name:'manufactureID',index:'manufactureID',align:'right',width:40,editable:true,hidden: true, edittype:'text', editoptions:{size:20},editrules:{edithidden: false,required:false}},
			{name:'joSchdTempHeaderID',index:'joSchdTempHeaderID',align:'right',width:40,editable:true,hidden: true, edittype:'text', editoptions:{size:20},editrules:{edithidden: false,required:false}},
			{name:'joSubmittalHeaderID',index:'joSubmittalHeaderID',align:'right',width:40,editable:true,hidden: true, edittype:'text', editoptions:{size:20},editrules:{edithidden: false,required:false}}],
		rowNum: 0,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		sortname: 'employeeId', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Submittal',
		height:270,	width: 1100,rownumbers:true, altRows: true, altclass:'myAltRowClass',
		loadComplete: function(data) {
			$("#submittalList").setSelection(1, true);
			$('#submittalList').jqGrid('getGridParam', 'userData');
			var allRowsInGrid = $('#submittalList').jqGrid('getRowData');
			var aVal = new Array(); 
			var sum = 0;
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.estimatecost;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
			});
			$('#totalCost').empty();
			$('#totalCost').append(formatCurrency(sum));
	    },
		loadError : function (jqXHR, textStatus, errorThrown){ },
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
    	onSelectRow: function(id){
			var rowData = jQuery(this).getRowData(id); 
			var product = rowData["joSubmittalID"];
			var productName = rowData["product"];
			var rxManufatureID = rowData["manufactureID"];
			var tempHeaderID = rowData["joSchdTempHeaderID"];
			$("#detailsHeader_ID").text(product);
			$("#productName_ID").text(productName);
			$("#manufacture_ID").text(rxManufatureID);
			$("#submittalschedule").empty();
			$("#submittalschedule").append("<li id='submittalschedule'><a href=''>Schedule "+" [ "+productName+" ]"+"</a></li>");
			$("#scheduleGrid").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
			var joSubmittalID = rowData["joSubmittalID"];
			loadSubmittalSchedule(product, tempHeaderID,joSubmittalID);
			$('#schedule').jqGrid('setGridParam',{postData:{'submittalDetailsID' : product, 'joSchdTempHeaderID' : tempHeaderID }});
			$("#scheduleGrid").trigger("reloadGrid");
			$("#manufacturerSubmital_ID").empty();
			$("#manufacturerSubmital_ID").text(rxManufatureID);
		},
    	editurl:"./jobtabs3/manpulaterSubmittal_Product"
	}).navGrid("#submittalListPager",
		{add:true,del:true,search: false,edit: true,pager:false, alertcap: "Warning", alerttext: 'Please select a Product'},
		//-----------------------edit options----------------------//
		{
			closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
			modal:true,jqModal:true,viewPagerButtons: false,editCaption: "Edit Product",	width: 430, top: 290, left: 207,
			beforeShowForm: function (form) 
			{
				var cost = $("#cost").val().replace(/[^0-9\.]+/g,"").replace(".00","");
				var estimate = $("#estimatecost").val().replace(/[^0-9\.]+/g,"").replace(".00","");
				$("#cost").val(cost);
				$("#estimatecost").val(estimate);
				jQuery("#tr_manufactureID", form).hide();
				jQuery("#tr_scheduleIcon", form).hide();
			   $(function() { var cache = {}; var lastXhr=''; $("#schedule" ).autocomplete({ minLength: 2,timeout :1000,
				select: function( event, ui ) { 
					var scheduleId = ui.item.id; $("#scheduledID").val(scheduleId);
					$.ajax({
						url: "./jobtabs3/productName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
						success: function(data){ var name = data.description;  $("#product").val(name); var manufactureID = data.rxManufacturerId;
						$("#manufactureID").val(manufactureID); }
					});
					 $.ajax({
							url: "./jobtabs3/ManufactureName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
							success: function(data){ var manufaurer = data; 
								$("#manufacture").val(manufaurer); }   
					 });
				},
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
     			lastXhr = $.getJSON( "jobtabs3/scheduleList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
     			error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				} }); });

			   $(function() { var cache = {}; var lastXhr=''; $("#product" ).autocomplete({ minLength: 2,timeout :1000,
				   select: function( event, ui ) { 
					   var scheduleId = ui.item.id; $("#scheduledID").val(scheduleId);
						$.ajax({
							url: "./jobtabs3/productName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
							success: function(data){ var name = data.description;  $("#product").val(name); var manufactureID = data.rxManufacturerId;
							$("#manufactureID").val(manufactureID); }
						});
						 $.ajax({
								url: "./jobtabs3/ManufactureName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
								success: function(data){ var manufaurer = data;
									$("#manufacture").val(manufaurer); }  
						 });
						 return true;
					},
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
     			lastXhr = $.getJSON( "jobtabs3/productList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
     			error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });

			   $(function() { var cache = {};  var lastXhr=''; $("#manufacture" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) { var rxManufacture = ui.item.id; $("#manufactureID").val(rxManufacture);},
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
     			lastXhr = $.getJSON( "jobtabs2/manufaturerList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
     			error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				} }); });
			   return true;
			},
			'onInitializeForm' : function(formid){
				jQuery('#TblGrid_submittalList #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
				jQuery('#TblGrid_submittalList #tr_product .DataTD').append('<td><input type="hidden" id="scheduledID" name="scheduledID" style="width: 50px;"</td>');
				jQuery('#TblGrid_submittalList #tr_submittalList .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
				jQuery('#TblGrid_submittalList #tr_manufacture .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
				jQuery('#TblGrid_submittalList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
			},
			onclickSubmit: function(params){
				var scheduledID = $("#scheduledID").val();
				var manufacureID = $("#manufactureID").val();
				var submittalHeaderID = $("#submittalMainHeaderID").val();
				return { 'scheduledID' : scheduledID, 'manufacturerID' : manufacureID, 'submittalHeaderID' : submittalHeaderID };
			},
			afterSubmit:function(response,postData){
				 return [true, loadGridToSubmittal()];
			 }
		},
		//-----------------------add options----------------------//
		{
			closeAfterAdd:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
			modal:true,
			jqModal:true,
			viewPagerButtons: false,
			addCaption: "Add Product",
			width: 430, top: 290, left: 207,
			beforeShowForm: function (form) 
			{
				if(jQuery("#submittalList").getGridParam("records") === 0 && $("#submittalMainHeaderID").val() === ''){
						errorText = "Please add revision value using 'Launch Submittal' before adding the product.";
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					jQuery(".ui-icon-closethick").trigger('click');
						return false;
				 } 
				jQuery("#tr_manufactureID", form).hide();
				jQuery("#tr_scheduleIcon", form).hide();
			   $(function() { var cache = {};  var lastXhr=''; $("#schedule" ).autocomplete({ minLength: 2,timeout :1000,
				select: function( event, ui ) { 
					var scheduleId = ui.item.id; $("#scheduledID").val(scheduleId);
					$.ajax({
						url: "./jobtabs3/productName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
						success: function(data){ var name = data.description;  $("#product").val(name); var manufactureID = data.rxManufacturerId;
						$("#manufactureID").val(manufactureID); }
					});
					 $.ajax({
							url: "./jobtabs3/ManufactureName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
							success: function(data){ var manufaurer = data; 
							$("#manufacture").val(manufaurer); } 
					 });
				},
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
     			lastXhr = $.getJSON( "jobtabs3/scheduleList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
     			error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });

			   $(function() { var cache = {};  var lastXhr=''; $("#product" ).autocomplete({ minLength: 2,timeout :1000,
				   select: function( event, ui ) { 
					   var scheduleId = ui.item.id; $("#scheduledID").val(scheduleId);
						$.ajax({
							url: "./jobtabs3/productName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
							success: function(data){ var name = data.description;  $("#product").val(name); var manufactureID = data.rxManufacturerId;
							$("#manufactureID").val(manufactureID); }
						});
						 $.ajax({
								url: "./jobtabs3/ManufactureName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
								success: function(data){ var manufaurer = data;
									$("#manufacture").val(manufaurer); }
						 });
					},
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
     			lastXhr = $.getJSON( "jobtabs3/productList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
     			error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });

			   $(function() { var cache = {};  var lastXhr=''; $("#manufacture" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) { var rxManufacture = ui.item.id; $("#manufactureID").val(rxManufacture);  },
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
     			lastXhr = $.getJSON( "jobtabs3/manufactureList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
     			error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
			   return true;
			},
			'onInitializeForm' : function(formid){
				jQuery('#TblGrid_submittalList #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
				jQuery('#TblGrid_submittalList #tr_product .DataTD').append('<td><input type="hidden" id="scheduledID" name="scheduledID" style="width: 50px;"</td>');
				jQuery('#TblGrid_submittalList #tr_submittalList .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
				jQuery('#TblGrid_submittalList #tr_manufacture .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
				jQuery('#TblGrid_submittalList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
			},
			onclickSubmit: function(params){
				var scheduledID = $("#scheduledID").val();
				var manufacureID = $("#manufactureID").val();
				var submittalHeaderID = $("#submittalMainHeaderID").val();
				return { 'scheduledID' : scheduledID, 'manufacturerID' : manufacureID, 'submittalHeaderID' : submittalHeaderID };
			},
			afterSubmit:function(response,postData){
				 return [true, loadGridToSubmittal()];
			 }
		},
			//-----------------------Delete options----------------------//
		{
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, width:270, jqModal:true, top: 356, left: 450,
			caption: "Delete Product",
			msg: 'Do you want delete selected Product?',
			onclickSubmit: function(params){
				var id = jQuery('#submittalList').jqGrid('getGridParam','selrow');
				var key = jQuery('#submittalList').getCell(id, 9);
				var submittalHeaderID = $('#submittalList').jqGrid('getCell', id,'joSubmittalHeaderID');
				var scheduledID = $('#submittalList').jqGrid('getCell', id,'joSchdTempHeaderID');
				var joSubmittalID = $('#submittalList').jqGrid('getCell', id,'joSubmittalID');
				return {  'productID' : key,'joSchdTempHeaderID' : scheduledID, 'joSubmittalHeaderID' : submittalHeaderID ,'joSubmittalID': joSubmittalID};
			}
		}
	);
}

function imgFmatter(cellValue, options, rowObject){
	var element = "<div><a onclick='submittalScheduleDialog()'><img src='./../resources/images/Schedule.png' title='Scheduled Grid' align='middle' style='padding: 2px 20px;'></a></div>";
  	 return element;
}

function statusFormatter(cellvalue, options, rowObject) {
	if(cellvalue === 0) {
		return "Open";
	} else if(cellvalue === 1) {
		return "1/2 Part.";
	} else if(cellvalue === 2){
		return "Rel.";
	}else{
		return "";
	}
}

function sent(){
	if ($('#sent_ID').is(':checked')) { 
	 	$("#sentdate").show(); 
 	} else {
	 	 $("#sentdate").hide();
 	} 
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function resent(){
	if ($('#resent_ID').is(':checked')) { 
		$("#resentdate").show(); 
 	} else {
		$("#resentdate").hide();
 	} 
}

function approved(){
	if ($('#approved_ID').is(':checked')) { 
		$("#approveddate").show(); 
	} else {
	 	 $("#approveddate").hide();
 	} 
}

function requested(){
	if ($('#requested_ID').is(':checked')) { 
	 	$("#requesteddate").show(); 
 	} else {
	 	 $("#requesteddate").hide();
 	} 
}

function OMsent() {
	if ($('#OMsent_ID').is(':checked')) { 
	 	$("#OMsentdate").show(); 
 	} else {
	 	 $("#OMsentdate").hide();
 	} 
}

function poreleasedialog() {
	var aManufacturer = $("#rxCustomer_ID").text();
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Closed" || jobStatus === "Bid" || jobStatus === "Planning" || jobStatus === "Budget" || jobStatus === "Quote" ||
			jobStatus === "Submitted" ||  jobStatus === "Closed" || jobStatus === "Lost" ||
			jobStatus === "Abandoned" || jobStatus === "Rejected" || jobStatus === "Over Budge"){
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else if(jobStatus === "Booked"){
		$.ajax({
			url: "./jobtabs3/getHoldCredit",
			type: "GET",
			data : {"customerID" : aManufacturer},
			success: function(data) {
				var validNewRelease = validateNewReleaseCount();
				if(!validNewRelease) {
					jQuery(newDialogDiv).html('<span><b style="color:red;">Credit approval required.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}else {
					var grid = $("#submittalList");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var type = "1";
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; //January is 0!
					var yyyy = today.getFullYear();
					if(dd<10){dd='0'+dd;} 
					if(mm<10){mm='0'+mm;} 
					today = mm+'/'+dd+'/'+yyyy;
					var aJoReleasedDate = today;
					var aManufacture = "Submittal";
					var aManufacturerId = grid.jqGrid('getCell',rowId,'manufactureID');
					var aJoMasterID = $("#joMaster_ID").text();
					var aJobNumber = $("#jobNumber_ID").text();
					var aReleaseDialogVar = "add";
					var aReleaseFormValues ="&joMasterID="+aJoMasterID+"&oper=" +aReleaseDialogVar+"&ReleasesName="+aJoReleasedDate+"&ReleasesTypeName="+type+
											"&ManufacturerId="+aManufacturerId+"&ReleasesManuName="+aManufacture+"&jobNumber="+aJobNumber;
					$.ajax({
						url: "./jobtabs5/addRelease",
						type: "POST",
						data : aReleaseFormValues,
						success: function(data) {
							joreleaseID =  data.joReleaseId;
							var vePOId = data.vePoid;
							var veFactoryId = data.veFactoryId;
							var aNewPONumber = data.ponumber;
							$("#release").trigger("reloadGrid");
							var grid = $("#submittalList");
							var rowId = grid.jqGrid('getGridParam', 'selrow');
							var manufacture = grid.jqGrid('getCell', rowId, 'manufactureID');
							var manufactureName = grid.jqGrid('getCell', rowId, 'manufacture');
							var quoteNumber = $("#jobQuoteNumber_ID").text();
							var	jobNumber = $("#jobNumber_ID").text();
							addPOrelease(manufacture, jobNumber, quoteNumber,manufactureName,joreleaseID,vePOId,veFactoryId,aNewPONumber);
						}
					});
				}
			} 
		});
	}
	return true;
}

function validateNewReleaseCount(){
	var isValidToAddRelease = false;
			var aJoMasterID = $("#joMaster_ID").text();
			var aJobNumber = $("#jobNumber_ID").text();
			var releaseValidateQryStr = "&joMasterID="+aJoMasterID+"&jobNumber="+aJobNumber;
			$.ajax({
				async:false,
				url: "./jobtabs5/validatefirstrelease",
				type: "GET",
				data: releaseValidateQryStr,
				success: function(result) {
					isValidToAddRelease = result;
					return true;
				}
		   });
	return isValidToAddRelease;
}

function soreleasedialog() {
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Closed" || jobStatus === "Bid" || jobStatus === "Planning" || jobStatus === "Budget" || jobStatus === "Quote" ||
			jobStatus === "Submitted" ||  jobStatus === "Closed" || jobStatus === "Lost" ||
			jobStatus === "Abandoned" || jobStatus === "Rejected" || jobStatus === "Over Budge"){
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else if(jobStatus === "Booked"){
		jQuery("#sorelease").dialog("open");
		$("#tabs_main_job").tabs("select", 4);
	}
	 if(jQuery("#submittalList").getGridParam("records") === 0){
		 errorText = "Please add value in revision field.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
	 }else if(jQuery("#submittalList").getGridParam("records") !== 0){
		 if(jobStatus === "Closed" || jobStatus === "Bid" || jobStatus === "Planning" || jobStatus === "Budget" || jobStatus === "Quote" ||
					jobStatus === "Submitted" ||  jobStatus === "Closed" || jobStatus === "Lost" ||
					jobStatus === "Abandoned" || jobStatus === "Rejected" || jobStatus === "Over Budge"){
		 		errorText = "Please change the status to 'Booked'.";
		 		jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	 	}else{
	 		jQuery("#sorelease").dialog("open");
			 $("#tabs_main_job").tabs("select", 4);
		}
	 }
	 return true;
}

function loadGridToSubmittal(){
	var submittalMainID;
	submittalMainID = $("#submittalMainHeaderID").empty();
	submittalMainID = $("#submittalMainHeaderID").val();
	$("#submittalList").jqGrid('GridUnload');
	loadSubmittalListGrid(submittalMainID);
	$("#submittalList").trigger('reload');
}

function submittalScheduleDialog() {
	$("#show_SubmittalSchedulesDiv").show();
	//jQuery("#submittalScheduleDialog").dialog("open");
}
function hideScheduleGrid(){
	$("#show_SubmittalSchedulesDiv").hide();
}
function submittaldialog() {
	 var optionTag = '';
	 var revision = $("#revisiontext").val();  var thru = $("#thrutext").val();  var planDate = $("#datePlan").val();  var submittalDate = $("#submittalDate").val();  
	 var cpoies = $("#copiestext").val();  var submittal = $("#Submittal").val();  var signed = $("#Signed").val();   var remarks = $("#remarksSheet").val(); 
	 var internal = $("#internalCommand").val(); var sheet = $("#sheetremarks").val();  var comments = $("#commentsinternal").val();
	 if(jQuery("#submittalList").getGridParam("records") === 0){
		 var aRevision = $("#revisionID").val();
		 if((aRevision !== '5' ||aRevision !== '4') && $("#submittalMainHeaderID").val() !== ''){
			 optionTag = '<option value="0">0</option><option value="1">1</option><option value="2">2</option>'+
									'<option value="3">3</option><option value="4">Add New</option><option value="5">Copy</option></select>';
			$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='0']").attr("selected", true);
			$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
			$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
			optionTag = '<option value="0">0</option><option value="1">1</option>'+
									 '<option value="4">Add New</option><option value="5">Copy</option></select>'; 
			$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='0']").attr("selected", true);
			$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
			$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
			$("#revisiontext").val(revision);   $("#thrutext").val(thru);   $("#datePlan").val(planDate);   $("#submittalDate").val(submittalDate);  
			 $("#copiestext").val(cpoies);   $("#Submittal").val(submittal);   $("#Signed").val(signed);    $("#remarksSheet").val(remarks); 
			 $("#internalCommand").val(internal);  $("#sheetremarks").val(sheet);   $("#commentsinternal").val(comments);
		 }else{
		 	$("#revisiontext").css("display", "none"); $("#thrutext").css("display", "none"); $("#datePlan").css("display", "none"); $("#submittalDate").css("display", "none");
		 	$("#copiestext").css("display", "none"); $("#Submittal").css("display", "none"); $("#remarksSheet").css("display", "none"); $("#signedsearchimage").css("display", "none");
			$("#internalCommand").css("display", "none"); $("#revisionlable").show(); $("#searchimage").css("display", "none");  $("#Signed").css("display", "none");
			$("#submittalDate").val(""); $("#copiestext").val(""); $("#Submittal").val(""); $("#submittal").val(""); $("#datePlan").val(""); 
			$("#Signed").val(""); $("#signed").val(""); $("#sheetremarks").val(""); $("#commentsinternal").val("");
		 	optionTag = '<option value="4">Add New</option><option value="5">Copy</option></select>';
			$("#revisionID").empty();
			$("#revisionID").append(optionTag);
			$("#revisionID option[value='5']").attr("selected", true);
		 }
		jQuery("#generalSubmittal").dialog("open");
	 }else if(jQuery("#submittalList").getGridParam("records") !== 0){
		jQuery("#generalSubmittal").dialog("open");
	}
		return true;
}

function submittal_previous(){
	var $tabs = $('#tabs_main_job').tabs();
	$tabs.tabs('select', 1); // switch to third tab
}

function submittal_save() {
	var submittalSent = false;
	if($('#sent_ID').is(':checked')){
		submittalSent =  true;
	}var submittalResent = false;
	if($('#resent_ID').is(':checked')){
		submittalResent =  true;
	}var submittalApproved = false;
	if($('#approved_ID').is(':checked')){
		submittalApproved =  true;
	}var requestManual = false;
	if($('#requested_ID').is(':checked')){
		requestManual =  true;
	}var manualSent = false;
	if($('#OMsent_ID').is(':checked')){
		manualSent =  true;
	}
	var submittalSentDate = $("#sentdate").val();
	var submittalResentDate = $("#resentdate").val();
	var submittalApprovedDate = $("#approveddate").val();
	var aManualQty = $("#manualQtyId").val();
	var requestDate = $("#requesteddate").val();
	var manualDate = $("#OMsentdate").val();
	var aJoMasterID = $("#joMaster_ID").text();
	var description = getUrlVars()["jobName"];
	var jobNumber = $('.jobHeader_JobNumber').val();
	$.ajax({
		url: "./jobtabs3/updateSubmittalform",
		type: "POST",
		data : "&joMasterID="+aJoMasterID+"&jobName=" +description+"&jobNumber=" +jobNumber+"&sentCheck=" +submittalSent+"&resentCheck=" +submittalResent+
		"&approvedCheck=" +submittalApproved+"&sentDate=" +submittalSentDate+"&resentDate=" +submittalResentDate+"&approvedDate=" +submittalApprovedDate+
		"&manualQty=" +aManualQty+"&manualSentCheck=" +manualSent+"&requestManualCheck=" +requestManual+"&manualSentDate=" +manualDate+"&manualRequestDate=" +requestDate,
		success: function(data) {
			var errorText = "Submittal Details Successfully Updated.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
   });
}
	
function submittal_next(){
	var $tabs = $('#tabs_main_job').tabs();
	$tabs.tabs('select', 3); // switch to third tab
}

function printSubmittalCover(){
	var grid = $("#submittalList");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var joSubmittalHeaderID = grid.jqGrid('getCell',rowId,'joSubmittalHeaderID');
	window.open("./jobtabs3/printsubmittalCover?joSubmittalHeaderID="+joSubmittalHeaderID);
}

function parseRequestModelMap() {
	var submittalSent =  $("#submittalSentID").val();
	var submittalApproved = $("#submittalApprovedID").val();
	var submittalResent = $("#submittalResentID").val();
	var submittalSentDate = customFormatDate($("#submittalSentDateID").val());
	var submittalApprovedDate = customFormatDate($("#submittalApproveDateID").val());
	var submittalResentDate = customFormatDate($("#submittalResentDateID").val());
	var aManualQty = $("#submittalMnaualQtyID").val();
	var manualSent = $("#submittalManualSentID").val();
	var requestManual = $("#submittalReuestManualID").val();
	var manualDate = customFormatDate($("#submittalManualDateID").val());
	var requestDate = customFormatDate($("#submittalReaquestDateID").val());
	
	if(submittalSent === 'true') {
		$('#sent_ID').attr('checked','checked');
		$("#sentdate").show();
		$("#sentdate").val(submittalSentDate);
	} else {
		$("#sentdate").hide();
	}
	
	if(submittalApproved === 'true') {
		$('#approved_ID').attr('checked','checked');
		$("#approveddate").show();
		$("#approveddate").val(submittalApprovedDate);
	} else {
		$("#approveddate").hide();
	}
	
	if(submittalResent === 'true') {
		$('#resent_ID').attr('checked','checked');
		$("#resentdate").show();
		$("#resentdate").val(submittalResentDate);
	} else {
		$("#resentdate").hide();
	}
	
	$("#manualQtyId").val(aManualQty);
	if(manualSent === 'true') {
		$('#OMsent_ID').attr('checked','checked');
		$("#OMsentdate").show();
		$("#OMsentdate").val(manualDate);
	} else {
		$("#OMsentdate").hide();
	}
	
	if(requestManual === 'true') {
		$('#requested_ID').attr('checked','checked');
		$("#requesteddate").show();
		$("#requesteddate").val(requestDate);
	} else {
		$("#requesteddate").hide();
	}
}

jQuery( "#sorelease" ).dialog({
	autoOpen: false,
	width: 850,
	title:"Sales Order",
	modal: true,
	buttons:{
	},
	close: function () {
		return true;
	}
});