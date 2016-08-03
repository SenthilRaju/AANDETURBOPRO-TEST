var aGlobalCount;
var aGlobalVariable;
var  Checkdefaultstatus=false;
jQuery(document).ready(function() {
		
	$("#jobbiddate").datepicker();
	$(".datepicker").datepicker();
	var customerName = $('#customerName').text();
	$("#customerNameHeader").val($.trim(customerName));
	$("#lostQuotesCheck").hide();
	$("#customerChecked").css("display", "none");
	$("#QuoteId").css("display", "none");
	$("#CustomerCounty").css("display", "none");
	$("#QuotesCheck").hide();
	loadContactsGrid();
	loadGridOpenQuotes();
	var archTech = $("#isCategory1ID").val();
	var Engg = $("#isCategory2ID").val();
	var generalContract = $("#isCategory3ID").val();
	var owner = $("#isCategory4ID").val();
	var bondAgent = $("#isCategory5ID").val();
	var customer = $("#isCustomerID").val();
	var vendor = $("#isVendorID").val();
    var Emp = $("#isEmployeeID").val();
    var category6=$("#isCategory6ID").val();
    var category7=$("#isCategory7ID").val();
    var category8=$("#isCategory8ID").val();
    if(category6== 'true'){
    	$("#Category6Desc").attr("checked", true);
    }
    if(category7== 'true'){
    	$("#Category7Desc").attr("checked", true);
    }
    if(category8== 'true'){
    	$("#Category8Desc").attr("checked", true);
    }
	if(archTech === 'true'){
		$("#architectche").attr("checked", true);
		$("#architect").show();
	}
	if(Engg === 'true'){
		$("#engineerche").attr("checked", true);
		$("#engineer").show();
	}
	if(generalContract === 'true'){
		$("#caustMGR").attr("checked", true);
	}
	if(customer === 'true'){
		$("#customerchek").attr("checked", true);
		$("#customer").show();
		jQuery("#EmployeeDetailsGrid").setGridHeight(200);
		$("#customerChecked").show();
	}
	if(vendor === 'true'){
		$("#vendorcheck").attr("checked", true);
		$("#vendor").show();
	}
	if(Emp === 'true'){
		$("#employeeche").attr("checked", true);
		$("#employee").show();
	}
	if(owner === 'true'){
		$("#owner").attr("checked", true);
	}
	if(bondAgent === 'true'){
		$("#bondAgent").attr("checked", true);
	}
	var county=$("#countyID").val();
	var state= $("#stateID").val();
	if(county !== ""){
		//var countyState = county+" ("+state+")";
		//$("#County").val(countyState);
	}
	loadrolodexcategories();
});


function callRxAddressDetails()	{	
	var rolodexid = getUrlVars()["rolodexNumber"];
	loadrxAddressGrid(rolodexid);
/* 	$("#extensionaddress").hide();
	$("#addressSetting").hide();
	$("#remitaddress").hide(); */
	jQuery("#rxAddressGrid").dialog('option', 'title', 'Customer Address');
	jQuery("#rxAddressGrid").dialog("open");
}



/*while loading the page label and checkbox enable hide and show based upon sysinfo table*/
function loadrolodexcategories(){
	var Category1Desc=$("#Category1Descset").val();
	var Category2Desc=$("#Category2Descset").val();
	var Category3Desc=$("#Category3Descset").val();
	var Category4Desc=$("#Category4Descset").val();
	var Category5Desc=$("#Category5Descset").val();
	var Category6Desc=$("#Category6Descset").val();
	var Category7Desc=$("#Category7Descset").val();
	var Category8Desc=$("#Category8Descset").val();
	
	//Category1lab
	$("#Category1lab").text(Category1Desc);
	$("#Category2lab").text(Category2Desc);
	$("#Category3lab").text(Category3Desc);
	$("#Category4lab").text(Category4Desc);
	$("#Category5lab").text(Category5Desc);
	$("#Category6lab").text(Category6Desc);
	$("#Category7lab").text(Category7Desc);
	$("#Category8lab").text(Category8Desc);
	
	if(Category1Desc==undefined ||Category1Desc==null ||Category1Desc==""||Category1Desc.length==0){
		$("#engineerche").css({ display: "none" });
		$("#Category1lab").css({ display: "none" });
	}else{
		$("#engineerche").css({ display: "inline-block" });
		$("#Category1lab").css({ display: "inline-block" });
		
	}
	if(Category2Desc==undefined ||Category2Desc==null ||Category2Desc==""||Category2Desc.length==0){
		$("#architectche").css({ display: "none" });
		$("#Category2lab").css({ display: "none" });
	}else{
		$("#architectche").css({ display: "inline-block" });
		$("#Category2lab").css({ display: "inline-block" });
	}
	if(Category3Desc==undefined ||Category3Desc==null ||Category3Desc==""||Category3Desc.length==0){
		$("#caustMGR").css({ display: "none" });
		$("#Category3lab").css({ display: "none" });
	}else{
		$("#caustMGR").css({ display: "inline-block" });
		$("#Category3lab").css({ display: "inline-block" });
	}
	if(Category4Desc==undefined ||Category4Desc==null ||Category4Desc==""||Category4Desc.length==0){
		$("#owner").css({ display: "none" });
		$("#Category4lab").css({ display: "none" });
	}else{
		$("#owner").css({ display: "inline-block" });
		$("#Category4lab").css({ display: "inline-block" });
	}
	//engineerche ,architectche,caustMGR,owner,bondAgent,Category6Desc,Category7Desc,Category8Desc
	if(Category5Desc==undefined ||Category5Desc==null ||Category5Desc==""||Category5Desc.length==0){
		$("#bondAgent").css({ display: "none" });
		$("#Category5lab").css({ display: "none" });
	}else{
		document.getElementById("bondAgent").style.display="inline-block";
		document.getElementById("Category5lab").style.display="inline-block";
	}
	if(Category6Desc==undefined ||Category6Desc==null ||Category6Desc==""||Category6Desc.length==0){
		$("#Category6Desc").css({ display: "none" });
		$("#Category6lab").css({ display: "none" });
	}else{
		$("#Category6Desc").css({ display: "inline-block" });
		$("#Category6lab").css({ display: "inline-block" });
	}
	if(Category7Desc==undefined ||Category7Desc==null ||Category7Desc==""||Category7Desc.length==0){
		$("#Category7Desc").css({ display: "none" });
		$("#Category7lab").css({ display: "none" });
	}else{
		$("#Category7Desc").css({ display: "inline-block" });
		$("#Category7lab").css({ display: "inline-block" });
	}
	if(Category8Desc==undefined ||Category8Desc==null ||Category8Desc==""||Category8Desc.length==0){
		$("#Category8Desc").css({ display: "none" });
		$("#Category8lab").css({ display: "none" });
	}else{
		$("#Category8Desc").css({ display: "inline-block" });
		$("#Category8lab").css({ display: "inline-block" });
	}
	
	
}

			
function loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID){
	$("#customerFilterSearchGrid").jqGrid({
		url: "./search/salesupcomingForSale",
		datatype: 'JSON',
		mtype: 'GET',
		postData: { 'createdLoginID' : createdLogin, 'changedLoginID' : changedLogin, 'rxMaster' : rxMasterID },
		colNames: ['Bid Date', 'Project'],
		colModel :[
            {name:'bidDate',index:'bidDate',align:'center',width:30},
            {name:'jobName',index:'jobName',align:'center',width:150}],
			rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: "Customer's Bid List",
			height: 150, width: 717, altRows: true,
			altclass:'myAltRowClass',
		loadComplete: function(data) {
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
		loadError : function (jqXHR, textStatus, errorThrown){
		},
		onSelectRow: function(id){
		}
	});
}

function loadSingleCustomerGrid(createdLogin, changedLogin, rxMasterID){
	$("#singlecustomerGrid").jqGrid({
		url: "./search/salessingleupcomingForSale",
		datatype: 'JSON',
		mtype: 'GET',
		postData: { 'createdLoginID' : createdLogin, 'changedLoginID' : changedLogin, 'joMasterID' : rxMasterID },
		colNames: ['Bid Date', 'Project'],
		colModel :[
            {name:'bidDate',index:'bidDate',align:'center',width:30},
            {name:'jobName',index:'jobName',align:'center',width:150}],
			rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: "Customer's Bid List",
			height: 150, width: 717, altRows: true,
			altclass:'myAltRowClass',
		loadComplete: function(data) {
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
		loadError : function (jqXHR, textStatus, errorThrown){
		},
		onSelectRow: function(id){
		}
	});
}
		
function loadContactsGrid() {
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
	var grid = $('#EmployeeDetailsGrid');
	$("#EmployeeDetailsGrid").jqGrid({
		url: 'rolodexdetails/rolodexcontactlist',
		datatype: 'JSON',
		postData: {'rolodexNumber':rolodexNumber },
		mtype: 'POST',
		pager:jQuery("#EmployeeDetailsGridpager"),
		colNames: ['RxContactId', 'RxMasterId', 'Last Name', 'First Name', 'Role', 'Direct Line','Extension','Email', 'Cell'],
		colModel:
		[{name:'rxContactId',index:'rxContactId',align:'left',width:50, hidden: true, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 {name:'rxMasterId',index:'rxMasterId',align:'left',width:50, hidden: true, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 {name:'lastName',index:'lastName',align:'left',width:50, editable:true,editoptions:{size:30,maxlength:'40'},editrules:{required:true}},
		 {name:'firstName',index:'firstName',align:'left',width:30,editable:true,editoptions:{size:30,maxlength:'40'},editrules:{required:true}},
		 {name:'jobPosition',index:'jobPosition',align:'left',width:50,editable:true,editoptions:{size:30},editrules:{required:false}},
		 {name:'directLine',index:'directLine',align:'left',width:40,editable:true,editoptions:{size:30},editrules:{required:false}},
		 {name:'extension',index:'extension',align:'left',width:40,editable:true,editoptions:{size:30},editrules:{required:false}},
		 {name:'email', index:'email', align:'left',width:90,editable:true,editoptions:{size:30},editrules:{required:false,email:true}},
		 {name:'cell',index:'cell',align:'left',width:50,editable:true,editoptions:{size:30},editrules:{required:false}}],
		 rowNum: 0,
		 sortname:' ',
		 sortorder:"asc",
		 imgpath:'themes/basic/images',
		 caption:'Contacts',
		 height:400,
		 pgbuttons: false,recordtext:'',rowList:[],pgtext: null,
		 altRows:true,
		 altclass:'myAltRowClass',
		 rownumbers:true,
	 	 width:1100,
		 loadComplete:function(data) {
			$("#EmployeeDetailsGrid").setSelection(1, true);
		},
		onSelectRow : function(id) {
			$("#customerQuotes").show();
			$("#lostQuotesCheck").show();
			$("#QuotesCheck").show();
			lostQuotesShow();
			QuotesShow();
			var rowData = jQuery(this).getRowData(id); 
			var rxContactId = rowData['rxContactId'];
			var rxMasterId1 = rowData['rxMasterId'];
			var aFirstName = rowData['firstName'];
			var aLastName = rowData['lastName'];
			$("#dummyContactName").val(aFirstName+" "+aLastName);
			$("#contactIDhiden").val(rxContactId);
			$("#rxMasterIDhiden").val(rxMasterId1);
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			$("#OpenGridQuotes").jqGrid('GridUnload');
			loadGridOpenQuotes(rxContactId);
			$('#OpenGridQuotes').jqGrid('setGridParam',{postData:{'rxContactId' : rxContactId, 'rolodexNumber':rolodexNumber }});
			$("#OpenGridQuotes").trigger("reloadGrid"); 
			$("#customerLostQuotesGrid").jqGrid('GridUnload');
			loadLostQuotesGrid(rxContactId);
			$('#customerLostQuotesGrid').jqGrid('setGridParam',{postData:{'rxContactId' : rxContactId, 'rolodexNumber':rolodexNumber }});
			$("#customerLostQuotesGrid").trigger("reloadGrid"); 
			$("#customerQuotesGrid").jqGrid('GridUnload');
			loadQuotesGrid(rxContactId);
			$('#customerQuotesGrid').jqGrid('setGridParam',{postData:{'rxContactId' : rxContactId, 'rolodexNumber':rolodexNumber }});
			$("#customerQuotesGrid").trigger("reloadGrid");
		},
		jsonReader:{
			root:"rows",
			page:"page",
			total:"total",
			records:"records",
			repeatitems:false,
			cell:"cell",
			id:"id",
			userdata:"userdata"
    	},
		loadError: function(jqXHR, textStatus, errorThrown) {	},
		editurl:"./rolodexdetails/manpulaterxContact"
	}).navGrid("#EmployeeDetailsGridpager",
			{add:true, edit:true, del:true, search:false, view:false},
			//-----------------------edit options----------------------//
			{
				width:450, left:400, top: 300,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Contact",
				beforeShowForm: function (form) 
				{
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_lastName .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_lastName .CaptionTD').append('Last Name: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').append('First Name: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_jobPosition .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_jobPosition .CaptionTD').append('Role: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .CaptionTD').append('Direct Line: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .DataTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .DataTD').append('<td>'+
							'&nbsp;<input type="text" id="areaCodeDirID" name="areaCodeDir" style="width:50px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="3" value="" onkeyup="autoFocus(this)">&nbsp;'+
							'<input type="text" id="exchangeCodeDirID" name="exchangeCodeDir" style="width:50px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="3" value="" onkeyup="autoFocusNext(this)"> - '+ 
							'<input type="text" id="subscriberNumberDirID" name="subscriberNumberDir" style="width:80px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="4" value=""></td>');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_email .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_email .CaptionTD').append('Email: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .CaptionTD').append('Cell: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .DataTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .DataTD').append('<td>' +
							'&nbsp;<input type="text" id="areaCode1" name="contact2" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value=""  onkeyup="autoFocusCell(this)">&nbsp;'+
							'<input type="text" id="exchangeCode1" name="contact2" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value=""  onkeyup="autoFocusCellNext(this)"> - '+
							'<input type="text" id="subscriberNumber1" name="contact2" style="width:90px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="4" value="">	</td>');
					
					$(function() {var cache = {}; var lastXhr=''; $("#lastName").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
					{ var rxContactID = ui.item.id; $("#rxContactId").val(rxContactID); },
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
					lastXhr = $.getJSON( "customerList/customerFirstNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); }, 
					error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					} }); });

					$(function() {var cache = {}; var lastXhr=''; $("#firstName").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
						{ var rxContactID = ui.item.id; $("#rxContactId").val(rxContactID); },
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "customerList/customerLastNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
						error: function (result) {
						     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						}  }); });
					
					var id = jQuery("#EmployeeDetailsGrid").jqGrid('getGridParam','selrow');
					var directline = grid.jqGrid('getCell', id, 'directLine');
					var phone = grid.jqGrid('getCell', id, 'cell');
					if((phone == null || phone.length == 0)){
						$("#subscriberNumber1").val('');
						$("#areaCode1").val(''); $("#exchangeCode1").val('');
					}else{
						
						var areacode = phone.split(" ");
						 if(phone.indexOf(')') > -1)
							{
							var exchangecode = areacode[1].split("-");
							var pincode = areacode[0].replace("(", "");
							var pincode1 = pincode.replace(")", "");
							$("#subscriberNumber1").val(exchangecode[1]);
							$("#areaCode1").val(pincode1); $("#exchangeCode1").val(exchangecode[0]);
							}
						else
							{
							var exchangecode = phone.split("-");
							$("#subscriberNumber1").val(exchangecode[2]);
							$("#areaCode1").val(exchangecode[0]); $("#exchangeCode1").val(exchangecode[1]);
							}
						
					}
					 if(directline == null || directline.length == 0){
						 $("#areaCodeDirID").val('');
							$("#exchangeCodeDirID").val('');
							$("#subscriberNumberDirID").val('');
					 }else{
						var areacodeDir = directline.split(" ");
						var exchangecodeDir = areacodeDir[1].split("-");
						var pincodeDir = areacodeDir[0].replace("(", "");
						var pincode1Dir = pincodeDir.replace(")", "");
						$("#areaCodeDirID").val(pincode1Dir);
						$("#exchangeCodeDirID").val(exchangecodeDir[0]);
						$("#subscriberNumberDirID").val(exchangecodeDir[1]);
					 }
				},
				onclickSubmit: function(params){
					var areaCode=$("#areaCode1").val();
					var exchangeCode = $("#exchangeCode1").val();
					var subscriberNumber = $("#subscriberNumber1").val();
					var areaCodeDir = $("#areaCodeDirID").val();
					var exchageCodeDir =$("#exchangeCodeDirID").val();
					var subscriberNumberDir =$("#subscriberNumberDirID").val();
					var dir = '';
					var cell= '';
					if(areaCode !== ""){
						cell="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
					}
					if(areaCodeDir !== ""){
						dir ="("+areaCodeDir+") "+exchageCodeDir+"-"+subscriberNumberDir;
					}
					return { 'directDir' : dir, 'cell' : cell};
				}
			},
			//-----------------------add options----------------------//
			{
				width:450, left:400, top: 300,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:true,
				
				addCaption: "Add New Contact",
				beforeShowForm: function(form){ 
					$("#rxMasterId").val($("#CustomerId").text());
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_lastName .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_lastName .CaptionTD').append('Last Name: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').append('First Name: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_jobPosition .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_jobPosition .CaptionTD').append('Role: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .CaptionTD').append('Direct Line: ');
					
				
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .DataTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_directLine .DataTD').append('<td>'+
							'&nbsp;<input type="text" id="areaCodeDirID" name="areaCodeDir" style="width:50px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="3" value="" onkeyup="autoFocus(this)">&nbsp;'+
							'<input type="text" id="exchangeCodeDirID" name="exchangeCodeDir" style="width:50px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="3" value="" onkeyup="autoFocusNext(this)"> - '+ 
							'<input type="text" id="subscriberNumberDirID" name="subscriberNumberDir" style="width:80px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="4" value=""></td>');
					
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_extension .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_extension .CaptionTD').append('Extension:');
								
					
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_email .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_email .CaptionTD').append('Email: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .CaptionTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .CaptionTD').append('Cell: ');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .DataTD').empty();
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_cell .DataTD').append('<td>' +
							/*'&nbsp;<input type="text" id="extensionid" name="contact2" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="25" value=""  onkeyup="autoFocusCell(this)">'+*/
							'&nbsp;<input type="text" id="areaCode1" name="contact2" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value=""  onkeyup="autoFocusCell(this)">&nbsp;'+
							'<input type="text" id="exchangeCode1" name="contact2" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value=""  onkeyup="autoFocusCellNext(this)"> - '+
							'<input type="text" id="subscriberNumber1" name="contact2" style="width:80px;background-color: white" class="validation[custom[onlyNumber]]" maxlength="4" value="">	</td>');
					
					$(function() {var cache = {}; var lastXhr=''; $("#lastName").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
					{ var rxContactID = ui.item.id; $("#rxContactId").val(rxContactID); },
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
					lastXhr = $.getJSON( "customerList/customerLastNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
					error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					}  }); });

					$(function() {var cache = {}; var lastXhr=''; $("#firstName").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
					{ var rxContactID = ui.item.id; $("#rxContactId").val(rxContactID); },
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
					lastXhr = $.getJSON( "customerList/customerFirstNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
					error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					}  }); });
				},
				'onInitializeForm' : function(formid){
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_lastName .CaptionTD').append('<span style="color:red;" class="mandatory"> *</span>');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_lastName .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"  style="padding-left: 10px;">');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .CaptionTD').append('<span style="color:red;" class="mandatory"> *</span>');
					jQuery('#TblGrid_EmployeeDetailsGrid #tr_firstName .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"  style="padding-left: 10px;">');
				},
				onclickSubmit: function(params){
					var areaCode=$("#areaCode1").val();
					var exchangeCode = $("#exchangeCode1").val();
					var subscriberNumber = $("#subscriberNumber1").val();
					var areaCodeDir = $("#areaCodeDirID").val();
					var exchageCodeDir =$("#exchangeCodeDirID").val();
					var subscriberNumberDir =$("#subscriberNumberDirID").val();
					var dir = '';
					var cell= '';
					if(areaCode !== ""){
						cell="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
					}
					if(areaCodeDir !== ""){
						dir ="("+areaCodeDir+") "+exchageCodeDir+"-"+subscriberNumberDir;
					}
					return { 'directDir' : dir, 'cell' : cell};
				},
				errorTextFormat: function (data) {	}
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350,
				caption: "Delete Contact",
				msg: 'Delete the Contact Record?',

				onclickSubmit: function(params){
					var SelectedEveId = jQuery(this).jqGrid('getGridParam','selrow');
				    var arxContactId = jQuery(this).getCell(SelectedEveId, 1);
				    return {'rxContactId':arxContactId};
				}
			},
			//-----------------------search options----------------------//
			{	});
	emptyMsgDiv.insertAfter(grid.parent());
	return true;
}
		
function loadGridOpenQuotes(rxContactId){
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
	var grid = $('#OpenGridQuotes');
	$("#OpenGridQuotes").jqGrid({
		url:'customerList/contactIdbasedOpenQuotes',
		datatype:'JSON',
		postData:{'rolodexNumber':rolodexNumber, 'rxContactId': rxContactId },
		mtype:'GET',
		colNames:[ 'Date', 'Job Number', 'Job Name', 'Contact',	'Est. Cost', 'Contract Amount','Revision','joBidderID','joMasterID', 'ContactID', 'RxMasterID', 'Job Status', 'Edit & View'],
		colModel:
			[{name:'quoteDate', index:'quoteDate',align:'center',width:40, editable:true, edittype:'text', editoptions:{size:30,readonly:true}, editrules:{edithidden:false,required:false}},
			 {name:'jobNo',index:'jobNo',align:'center',width:40,editable:true, editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'jobName',index:'jobName',align:'left',width:90,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
			 {name:'customerContact',index:'customerContact',align:'left',width:30,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
			 {name:'quoteAmount',index:'quoteAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
			 {name:'contractAmount',index:'contractAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
			 {name:'quoteRev',index:'quoteRev',align:'middle',width:25,editable:true,edittype:'',editoptions:{}, hidden : true, editrules:{edithidden:true,required:false}},
			 {name:'joBidderId',index:'joBidderId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			  {name:'joMasterId',index:'joMasterId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'contactID',index:'contactID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'rxMasterID',index:'rxMasterID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'jobStatus',index:'jobStatus',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'edit',index:'edit',align:'center',width:25, hidden: false, editrules:{edithidden:false},  formatter:imgFmatter}],
		rowNum: 0, recordtext:'',rowList:[],pgtext:null,viewrecords:false,sortname:' ',sortorder:"asc",imgpath:'themes/basic/images',
		caption:'Open Quotes',height:180,rownumbers:true,altRows:true,altclass:'myAltRowClass',width:1100,
		loadComplete:function(data) {
			if (jQuery("#OpenGridQuotes").getGridParam("records") === 0) {
				emptyMsgDiv.show();
				emptyMsgDiv.insertAfter(grid.parent());
			} else {
				emptyMsgDiv.hide();
			}
		},
		gridComplete: function () {
			$(this).mouseover(function() {
		        var valId = $(".ui-state-hover").attr("id");
		        $(this).setSelection(valId, false);
		    });
		},
		jsonReader:{
			root:"rows",page:"page",total:"total",records:"records",repeatitems:false,
            cell:"cell",id:"id",userdata:"userdata"
    	},
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId); 
    		var invoiceDate= rowData['cuInvoiceDate'];
    		var jobNumber = rowData['jobNo'];
    		var jobName = rowData['jobName'];
    		var customerCon= rowData['customerContact'];
    		var Quoteamt=rowData['quoteAmount'];
    		var cost = Quoteamt.replace(/[^0-9\.]+/g,"");
    		var cost1=cost.replace(".00","");
    		var contractAmt= rowData['contractAmount'];
    		var price = contractAmt.replace(/[^0-9\.]+/g,"");
    		var price1=price.replace(".00","");
    		var rev=rowData['quoteRev'];
    		var joBidder=rowData['joBidderId'];
    		var joMast=rowData['joMasterId'];
    		$("#jobNumberID").val(jobNumber);
    		$("#productListID1").val(jobName);
    		$("#invoiceDateID").val(invoiceDate);
    		$("#customerID1").val(customerCon);
    		$("#costID1").val(cost1);
    		$("#sellPriceID1").val(price1);
    		$("#revision1").val(rev);
    		$("#jobStatusID1").val(3);
    		$("#joBidderID").val(joBidder);
    		$("#jobMasterID").val(joMast);
    		openAddnewProduct();
		},
		loadError: function(jqXHR, textStatus, errorThrown) {	}
	});
	return true;
}

function lostQuotesShow(){
	if($('#lostQuote').is(':checked')) {
		//alert('true');
		//loadLostQuotesGrid(rxContactId);
		
		$("#Quote").prop("checked", false);
		$("#customerGridQuotes").hide();
		$("#Quotes").hide();
		$("#lostQuotes").show();
	}else if($('#Quote').is(':checked')){
		
		$("#lostQuote").prop("checked", false);
		$("#lostQuotes").hide();
		$("#customerGridQuotes").hide();
		$("#Quotes").show();
	}else{
		
		$("#Quote").prop("checked", false);
		$("#lostQuote").prop("checked", false);
		$("#customerGridQuotes").show();
		//$("#customerLostQuotesGrid").show();
		$("#lostQuotes").hide();
		$("#Quotes").hide();
	}
}

function loadLostQuotesGrid(rxContactId) {
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
	var grid = $('#customerLostQuotesGrid');
	$("#customerLostQuotesGrid").jqGrid({
		url:'customerList/customerLostQuotes',
		datatype:'JSON',
		postData:{'rolodexNumber':rolodexNumber, 'rxContactId': rxContactId },
		mtype:'GET',
		colNames:[ 'Date', 'Job Number', 'Job Name', 'Contact',	'Est. Cost', 'Contract Amount','Revision','joBidderID','joMasterID', 'ContactID', 'RxMasterID', 'Job Status', 'Edit & View'],
		colModel:
			[{name:'quoteDate', index:'quoteDate',align:'center',width:40, editable:true, edittype:'text', editoptions:{size:30,readonly:true}, editrules:{edithidden:false,required:false}},
			 {name:'jobNo',index:'jobNo',align:'center',width:40,editable:true, editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'jobName',index:'jobName',align:'left',width:90,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
			 {name:'customerContact',index:'customerContact',align:'left',width:30,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
			 {name:'quoteAmount',index:'quoteAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
			 {name:'contractAmount',index:'contractAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
			 {name:'quoteRev',index:'quoteRev',align:'middle',width:25,editable:true,edittype:'',editoptions:{}, hidden : true, editrules:{edithidden:true,required:false}},
			 {name:'joBidderId',index:'joBidderId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			  {name:'joMasterId',index:'joMasterId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'contactID',index:'contactID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'rxMasterID',index:'rxMasterID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'jobStatus',index:'jobStatus',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'edit',index:'edit',align:'center',width:25, hidden: false, editrules:{edithidden:false},  formatter:imgFmatter}],
		rowList:[],
		rowNum: 0, viewrecords:false, sortorder:"asc", imgpath: 'themes/basic/images', caption: 'Lost Quotes',
		pgbuttons: false,recordtext:'',rowList:[],pgtext: null, height:180, width:1100, rownumbers:true,
		altRows:true, altclass:'myAltRowClass',
		loadComplete:function(data) {
			if (jQuery("#customerLostQuotesGrid").getGridParam("records") === 0) {
				emptyMsgDiv.show();
				emptyMsgDiv.insertAfter(grid.parent());
				$("#customerLostQuotesGridPager").hide();
			} else {
				$("#customerLostQuotesGridPager").show();
				emptyMsgDiv.hide();
			}
		},
		gridComplete: function () {
			$(this).mouseover(function() {
		        var valId = $(".ui-state-hover").attr("id");
		        $(this).setSelection(valId, false);
		    });
		},
		jsonReader:{
			root:"rows", page:"page", total:"total", records:"records", repeatitems:false,
            cell:"cell", id:"id", userdata:"userdata"
    	},
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId); 
    		var invoiceDate= rowData['cuInvoiceDate'];
    		var jobNumber = rowData['jobNo'];
    		var jobName = rowData['jobName'];
    		var customerCon= rowData['customerContact'];
    		var Quoteamt=rowData['quoteAmount'];
    		var cost = Quoteamt.replace(/[^0-9\.]+/g,"");
    		var cost1=cost.replace(".00","");
    		var contractAmt= rowData['contractAmount'];
    		var price = contractAmt.replace(/[^0-9\.]+/g,"");
    		var price1=price.replace(".00","");
    		var rev=rowData['quoteRev'];
    		var joBidder=rowData['joBidderId'];
    		var joMast=rowData['joMasterId'];
    		$("#jobNumberID").val(jobNumber);
    		$("#productListID1").val(jobName);
    		$("#invoiceDateID").val(invoiceDate);
    		$("#customerID1").val(customerCon);
    		$("#costID1").val(cost1);
    		$("#sellPriceID1").val(price1);
    		$("#revision1").val(rev);
    		$("#jobStatusID1").val(2);
    		$("#joBidderID").val(joBidder);
    		$("#jobMasterID").val(joMast);
    		openAddnewProduct();
		},
		loadError: function(jqXHR, textStatus, errorThrown) {	}
	});
	return true;
}


function loadQuotesGrid(rxContactId) {
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
	var grid = $('#customerQuotesGrid');
	$("#customerQuotesGrid").jqGrid({
		url:'customerList/customerQuotes',
		datatype:'JSON',
		postData:{'rolodexNumber':rolodexNumber, 'rxContactId': rxContactId },
		mtype:'GET',
		colNames:[ 'Date', 'Job Number', 'Job Name', 'Contact',	'Est. Cost', 'Contract Amount','Revision','joBidderID','joMasterID', 'ContactID', 'RxMasterID', 'Job Status', 'Description',  'Edit & View'],
		colModel:
			[{name:'quoteDate', index:'quoteDate',align:'center',width:40, editable:true, edittype:'text', editoptions:{size:30,readonly:true}, editrules:{edithidden:false,required:false}},
			 {name:'jobNo',index:'jobNo',align:'center',width:40,editable:true, editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'jobName',index:'jobName',align:'left',width:90,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
			 {name:'customerContact',index:'customerContact',align:'left',width:30,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
			 {name:'quoteAmount',index:'quoteAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
			 {name:'contractAmount',index:'contractAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
			 {name:'quoteRev',index:'quoteRev',align:'middle',width:25,editable:true,edittype:'',editoptions:{}, hidden : true, editrules:{edithidden:true,required:false}},
			 {name:'joBidderId',index:'joBidderId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			  {name:'joMasterId',index:'joMasterId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'contactID',index:'contactID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'rxMasterID',index:'rxMasterID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'jobStatus',index:'jobStatus',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'description',index:'description',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
			 {name:'edit',index:'edit',align:'center',width:25, hidden: false, editrules:{edithidden:false},  formatter:imgFmatter}],
		rowList:[],
		rowNum: 0, viewrecords:false, sortorder:"asc", imgpath: 'themes/basic/images', caption: 'Quick Quotes',
		pgbuttons: false,recordtext:'',rowList:[],pgtext: null, height:180, width:1100, rownumbers:true,
		altRows:true, altclass:'myAltRowClass',
		loadComplete:function(data) {
			if (jQuery("#customerQuotesGrid").getGridParam("records") == 0) {
				emptyMsgDiv.show();
				emptyMsgDiv.insertAfter(grid.parent());
				$("#customerQuotesGridPager").hide();
			} else {
				$("#customerQuotesGridPager").show();
				emptyMsgDiv.hide();
			}
		},
		gridComplete: function () {
			$(this).mouseover(function() {
		        var valId = $(".ui-state-hover").attr("id");
		        $(this).setSelection(valId, false);
		    });
		},
		jsonReader:{
			root:"rows", page:"page", total:"total", records:"records", repeatitems:false,
            cell:"cell", id:"id", userdata:"userdata"
    	},
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId); 
    		var invoiceDate= rowData['quoteDate'];
    		var jobNumber = rowData['jobNo'];
    		var jobName = rowData['jobName'];
    		var customerCon= rowData['customerContact'];
    		var Quoteamt=rowData['quoteAmount'];
    		var cost = Quoteamt.replace(/[^0-9\.]+/g,"");
    		var cost1=cost.replace(".00","");
    		var contractAmt= rowData['contractAmount'];
    		var price = contractAmt.replace(/[^0-9\.]+/g,"");
    		var price1=price.replace(".00","");
    		var rev=rowData['quoteRev'];
    		var joBidder=rowData['joBidderId'];
    		var joMast=rowData['joMasterId'];
    		$("#editQuoteJobNumberID").val(jobNumber);
    		$("#editQuoteProductListID").val(jobName);
    		$("#editQuoteDateID").val(invoiceDate);
    		$("#editQuoteCustomerHiddenID").val(customerCon);
    		$("#editQuoteCostID").val(cost1);
    		$("#editQuoteSellPriceID").val(price1);
    		$("#editQuoteRevisionID").val(rev);
    		$("#editQuoteJobStatusID").val(1);
    		$("#editQuoteJoBidderID").val(joBidder);
    		$("#editQuoteJobMasterID").val(joMast);
    		openAddnewProduct();
		},
		loadError: function(jqXHR, textStatus, errorThrown) {	}
	});
	return true;
}

function imgFmatter(cellValue, options, rowObject){
	var element = "<div><a onclick='editQuickQuote()'><img src='./../resources/images/edit_quote.png' title='Edit Quote' align='middle' style='padding: 2px 5px;'></a>"+
											"<a onclick='viewJobPage()'><img src='./../resources/images/view_quote.png' title='View Quote' align='middle' style='padding: 2px 5px;'></a></div>";
  	 return element;
}
		
function currencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function viewJobPage(){
	var rowIdQuote;
	if ($('#lostQuote').is(':checked')){
		rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
	}else if($('#Quote').is(':checked')){
		rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
	}else{
		rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
	}
	var rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
	var newDialogDiv = jQuery(document.createElement('div'));
	if(rowId === null){
		var errorText = "Please add a record in contact grid.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var arxContactId = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'rxContactId');
		$("#customerHiddenID1").val(arxContactId);
	}
	if(rowIdQuote === null){
		var errorText = "Please Select a record";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var rowId;
		var aJobNumber = '';
		var aJobName = '';
		var aJobCustomer='';
		var aJobStatus = '';
		var joMasterId= 0;
		if ($('#lostQuote').is(':checked')){
			rowId = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'jobNo');
			aJobName = $("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'jobName');
			joMasterId=$("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'joMasterId');
			aJobCustomer='';
			aJobStatus = "Lost";
		}else if($('#Quote').is(':checked')){
			rowId = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#customerQuotesGrid").jqGrid('getCell', rowId, 'jobNo');
			aJobName = $("#customerQuotesGrid").jqGrid('getCell', rowId, 'jobName');
			joMasterId=$("#customerQuotesGrid").jqGrid('getCell', rowId, 'joMasterId');
			aJobCustomer='';
			aJobStatus = "Quote";
		}else{
			rowId = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#OpenGridQuotes").jqGrid('getCell', rowId, 'jobNo');
			aJobName = $("#OpenGridQuotes").jqGrid('getCell', rowId, 'jobName');
			joMasterId=$("#OpenGridQuotes").jqGrid('getCell', rowId, 'joMasterId');
			aJobCustomer='';
			aJobStatus = "Booked";
		}
		var urijobname=encodeBigurl(aJobName);
		var uricusname=encodeBigurl(aJobCustomer);
		var aQryStr = "jobNumber="+aJobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+aJobStatus+"&joMasterID="+joMasterId;
		if(aJobStatus != "Booked" && aJobStatus != "Closed" && aJobStatus != "Submitted"){
			$.ajax({
				url: "./job_controller/jobStatusHome", 
				mType: "GET", 
				data : { 'jobNumber' : aJobNumber ,'joMasterID':joMasterId },
				success: function(data){ 
					var checkpermission=getGrantpermissionprivilage('Main',0);
						if(checkpermission){
					document.location.href = "./jobflow?token=view&" + aQryStr;
						}
				}
		 	});
		}
		else{
			var checkpermission=getGrantpermissionprivilage('Main',0);
			if(checkpermission){
			document.location.href = "./jobflow?token=view&" + aQryStr;
			}
		}
	}
}
		
jQuery(function(){
	jQuery("#bidListDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Add New Bid",
			height: 300,
			width: 750,
			buttons : {  },
			close:function(){
				return true;
			}	
	});
});

jQuery(function(){
	jQuery("#bidListsingleDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Bid List",
			height: 300,
			width: 750,
			buttons : {  },
			close:function(){
				return true;
			}
	});
});

function cancelUpComingCustomer(){
	jQuery("#bidListDialog").dialog("close");
}

function cancelUpComingSingleCustomer(){
	jQuery("#bidListsingleDialog").dialog("close");
}
		
function openQuickQuote(){
	if ($('#customerchek').is(':checked')) {
		var rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
		if(rowId === null){
					var errorText = "Please add a record in contact grid.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
				var arxContactId = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'rxContactId');
				//var arxMasterId = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'rxMasterId');
				var aLastName = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'lastName');
				var aFirstName = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'firstName');
				$("#customerHiddenID").val(arxContactId); $("#customerID").val(aFirstName+' '+aLastName);
				var customer = $("#customerNameHeader").val(); $("#customerContactHiddenID").val(customer);
				$("#productListID").val('');$("#pharagraphID").val(''); $("#costID").val(''); $("#manufacturerID").val(''); $("#sellPriceID").val('');
				$("#revision").val('');
				var rxMasterID = getUrlVars()["rolodexNumber"]; $("#rxMasterHiddenID").val(rxMasterID); 
			/* 	$.ajax({
					url: "customerList/quickQuoteRevision",
					mType: "GET",
					data : {'rxMasterID': arxMasterId, 'rxContactID' : arxContactId},
					success: function(data){
						$("#revision").val(data);
					}
				}); */ 
				jQuery("#quickQuoteDialog").dialog("open");
		} 
		/*var customer = $("#customerNameHeader").val(); $("#customerContactHiddenID").val(customer);
		var rxMasterID = getUrlVars()["rolodexNumber"]; $("#rxMasterHiddenID").val(rxMasterID);
		jQuery("#quickQuoteDialog").dialog("open");*/
		return true;
	} else {
		var errorText = "The 'Customer' box must be checked in order to use this feature.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
}

jQuery(function(){
	jQuery("#quickQuoteDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Quick Quote",
			height: 420,
			width: 520,
			buttons : {  },
			close:function(){
				$('#quickQuoteDialog').validationEngine('hideAll');
				return true;
			}
	});
});

jQuery(function(){
	jQuery("#findProjectDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Find Project",
			height: 100,
			width: 590,
			buttons : {  },
			close:function(){
				return true;
			}
	});
});

jQuery(function(){
	jQuery("#customerAddressDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Customer Address",
			width: 'auto',
			buttons : {  },
			close:function(){
				$('#customerAddressForm').validationEngine('hideAll');
				return true;
			}
	});
});

/**  *  Format phone numbers */ 
function formatPhone(phonenum) {
	phonenum = $.trim(phonenum);
	if(contains(phonenum, "Ext")) {
		var phNoArray = new Array();
		phNoArray = phonenum.split("Ext");
		phonenum = $.trim(phNoArray[0]);
	}
	var regexObj = /^(?:\+?1[-. ]?)?(?:\(?([0-9]{3})\)?[-. ]?)?([0-9]{3})[-. ]?([0-9]{4})$/;    
	if (regexObj.test(phonenum)) {
		var parts = phonenum.match(regexObj);
		var phone = "";
		if (parts[1]) { phone += "(" + parts[1] + ") "; }
		phone += parts[2] + "-" + parts[3];
		return phone;
	} else {
		//invalid phone number
		return phonenum;
	}
}

function contains(str, text) {
	return str.indexOf(text) >= 0;
}
		
function openCustomerAddress(){
	
	/*var phone1 = $("#phone1ID").val();
	var phone2 = $("#phone2ID").val();
	var fax = $("#faxID").val();

	var aPhNo = formatPhone(phone1);
	var aPhNo2 = formatPhone(phone2);
	var aFax = formatPhone(fax);
	phone1 = aPhNo;
	phone2 = aPhNo2;
	fax = aFax;
	
	if(phone1 === null || phone1.length === 0){
		if(phone2 === null || phone2.length === 0){
			if(fax === null || fax.length === 0){
		
			}else{
				if(fax !== '' && contains(fax, " ") && fax.length >= 10){
					var areacode2 = fax.split(" ");
					var exchangecode2 = areacode2[1].split("-");
					var pinco = areacode2[0].replace("(", "");
					var pinco1 = pinco.replace(")", "");
					$("#area_Code2").val(pinco1);
					$("#exchange_Code2").val(exchangecode2[0]);
					$("#subscriber_Number2").val(exchangecode2[1]);
				}
			}
		} else {
			if(phone2 !== '' && contains(phone2, " ")){
				var areacode1 = phone2.split(" ");
				var exchangecode1 = areacode1[1].split("-");
				var pincode2 = areacode1[0].replace("(", "");
				var pincode3 = pincode2.replace(")", "");
				$("#area_Code1").val(pincode3);
				$("#exchange_Code1").val(exchangecode1[0]);
				$("#subscriber_Number1").val(exchangecode1[1]);
			}
			if(fax == null || fax.length == 0){
				
			}else{
				if(fax !== '' && contains(fax, " ") && fax.length >= 10){
					var areacode2 = fax.split(" ");
					var exchangecode2 = areacode2[1].split("-");
					var pinco = areacode2[0].replace("(", "");
					var pinco1 = pinco.replace(")", "");
					$("#area_Code2").val(pinco1);
					$("#exchange_Code2").val(exchangecode2[0]);
					$("#subscriber_Number2").val(exchangecode2[1]);
				}
			}
		}
	} else {
		if(phone1 !== '' && contains(phone1, " ")){
			var areacode = phone1.split(" ");
			var exchangecode = areacode[1].split("-");
			var pincode = areacode[0].replace("(", "");
			var pincode1 = pincode.replace(")", "");
			$("#area_Code").val(pincode1); 
			$("#exchange_Code").val(exchangecode[0]);
			$("#subscriber_Number").val(exchangecode[1]);
		}
	if(phone2 === null || phone2.length === 0){
		if(fax === null || fax.length === 0){
		} else {
			if(fax !== '' && contains(fax, " ") && fax.length >= 10){
				var areacode2 = fax.split(" ");
				var exchangecode2 = areacode2[1].split("-");
				var pinco = areacode2[0].replace("(", "");
				var pinco1 = pinco.replace(")", "");
				$("#area_Code2").val(pinco1); 
				$("#exchange_Code2").val(exchangecode2[0]);
				$("#subscriber_Number2").val(exchangecode2[1]);	
			}
		}
	} else {
		if(phone2 !== '' && contains(phone2, " ")){
			var areacode1 = phone2.split(" ");
			var exchangecode1 = areacode1[1].split("-");
			var pincode2 = areacode1[0].replace("(", "");
			var pincode3 = pincode2.replace(")", "");
			$("#area_Code1").val(pincode3); 
			$("#exchange_Code1").val(exchangecode1[0]);
			$("#subscriber_Number1").val(exchangecode1[1]);
		}
	if(fax === null || fax.length === 0) {
	} else {
		if(fax !== ''&& contains(fax, " ")){
				var areacode2 = fax.split(" ");
				var exchangecode2 = areacode2[1].split("-");
				var pinco = areacode2[0].replace("(", "");
				var pinco1 = pinco.replace(")", "");
				$("#area_Code2").val(pinco1); 
				$("#exchange_Code2").val(exchangecode2[0]);
				$("#subscriber_Number2").val(exchangecode2[1]);	
				}
			}	
		}
	}*/
	$('#customerAddressForm')[0].reset();
	//$("#rxMasterDetailsnoneordisplay").hide();
	$("#rxAddressHiddenID").val("");
	//$("#rxMasterDetailsnoneordisplay").css("display", "none");
	$("#isMailing").attr("checked", false);
	$("#isShipping").attr("checked", false);
	$("#isDefault").attr("checked", false);
	jQuery("#customerAddressDialog").dialog("open");
}

function savenewcustomerAddress(overrideoper){
	
	//alert("hi"+overrideoper);
	
	
	var areaCode=$("#area_Code").val();
	var exchangeCode = $("#exchange_Code").val();
	var subscriberNumber = $("#subscriber_Number").val();
	var contact1='';
	var isMailing = false;
	var isShipping = false;
	var isDefault = false;
	if(areaCode !== ''){
	contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	var areaCode1=$("#area_Code1").val();
	var exchangeCode1 = $("#exchange_Code1").val();
	var subscriberNumber1 = $("#subscriber_Number1").val();
	var contact2='';
	if(areaCode1 !== ''){
		contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
	}
	var areaCode2=$("#area_Code2").val();
	var exchangeCode2 = $("#exchange_Code2").val();
	var fax='';
	var subscriberNumber2 = $("#subscriber_Number2").val();
	if(areaCode2 !== ''){
		fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
	}
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var name1= $("#nameID").val();
	var engineerForm = $("#customerAddressForm").serialize();
	var address= $("#address1ID").val();
	var customer= $("#isCustomerID").val();
	var vendor= $("#isVendorID").val();
	var architect= $("#isCategory1ID").val();
	var engineer= $("#isCategory2ID").val();
	var aActive= $("#isActiveID").val();
	if(aActive==''){
		aActive = false;
	}
	if($('input#isMailing').is(':checked')){
		isMailing = true;
	}
	if($('input#isShipping').is(':checked')){
		isShipping = true;
	}
	if($('input#isDefault').is(':checked')){
		isDefault = true;
	}
	var rxAddressId=$("#rxAddressHiddenID").val();
	
	if(overrideoper==true){
		isDefault = true;
		updateotheraddress(rolodexNumber);
	}else{
		isDefault = false;
	}
	$.ajax({
			type:'POST',
			url: "customerList/newCustomerAddress",
			async:false,
			data: engineerForm+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax=" +fax  +"&rolodexNumber="+ rolodexNumber+"&existingAddress1="+ address +"&EmployeeLastName=" +name1
			+ "&CustomerId=" +customer + "&VendorId=" +vendor + "&ArchitectId=" +architect+"&EngineerId=" +engineer+"&aEmployeeActive=" +aActive+"&isMailing=" +isMailing +"&isShipping=" +isShipping+"&rxAddressId="+rxAddressId+"&isDefault="+isDefault+"&OverrideOper="+overrideoper,
			success:function(data){
				$("#cuContactsMulAdrGrid").trigger("reloadGrid"); 
			}
		});
	jQuery("#customerAddressDialog").dialog("close");
}
function updateotheraddress(rxmasternumber){
	$.ajax({
		type:'POST',
		async:false,
		url: "customerList/updateotherrxaddress",
		async:false,
		data:{"rxCustomerID":rxmasternumber}, 
		success:function(data){
		}
	});
}
function cancelnewcustomerAddress(){
	$('#customerAddressForm').validationEngine('hideAll');
	$("#customerAddressDialog").dialog("close");
	return true;
}
		
function openBidList1(){
var aInfo = true;
if(aInfo){
	var rxMasterIDvalue = $('#rxMasterIDhiden').val();
	var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html("<table id='jobbidlisttable' ></table><div id='jobbidlistpager'></div>");
	jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
							buttons: []}).dialog("open");
	//{height:35,text: "OK",click: function() { $(this).dialog("close"); }}
	return true;
}
	
var rxMasterID = getUrlVars()["rolodexNumber"];
$.ajax({
	url: "./search/searchrolodexForSale",
	mType: "GET",
	data : {'rolodex': rxMasterID},
	success: function(data){
		 var createdLogin = data.createdByID;
		 var changedLogin = data.changedByID;
		 var rxMasterID = data.pk_fields;
		 var loginID = null;
		 if(createdLogin !== 0){
			 loginID = createdLogin;
		 }else if(changedLogin !== 0){
			 loginID = changedLogin;
		 }else if(createdLogin !== 0 && changedLogin !== 0){
			 loginID = createdLogin;
		 }
		 $('#changeID').val(loginID);
		 $('#UpcomingBidsGrid').jqGrid('setGridParam',{ postData: {'salesRepId': loginID } });
		 $("#UpcomingBidsGrid").trigger("reloadGrid");
		 loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID);
		 $('#customerFilterSearchGrid').jqGrid('setGridParam',{postData:{'createdLoginID' : createdLogin, 'changedLoginID' : changedLogin, 'rxMaster' : rxMasterID }});
		 $("#customerFilterSearchGrid").trigger("reloadGrid"); 
	},
	error: function(Xhr) {
	}
});
jQuery("#bidListDialog").dialog("open");	
	return true;		
}

function addcustomer(){
   var rxMasterID =  getUrlVars()["rolodexNumber"];
   var projectName =$("#customerFilterListID").val();
   var changedByID = $('#changeID').val();
   var splitprojectName= projectName.split("[");
   var replaceProjectName = splitprojectName[1].replace("]","");
   $.ajax({
	url: "./rxdetailedviewtabs/getJobDetailsList",
	mType: "GET",
	data : { 'jobNumber' : replaceProjectName},
	success: function(data) {
		jQuery("#findProjectDialog").dialog("close");
		var jomasterID = data.joMasterId;
		var bidDate = data.bidDate;
		var current = new Date(bidDate);
		var bidMonth =  current.getUTCMonth()+1;
		var biddate = current.getUTCDate()+1;
		var bidCustomerDate = bidMonth+"/"+biddate+"/"+current.getUTCFullYear();
		var currentDate = new Date();
		var currentMonth = currentDate.getMonth()+1;
		var bidCurrentDate = currentMonth+"/"+currentDate.getDate()+"/"+currentDate.getFullYear();
		if(bidCurrentDate > bidCustomerDate){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">Bid Date of the selected Project Passed away!</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		 $.ajax({
   			url: "./job_controller/updatejobList",
   			mType: "GET",
   			data : { 'jobNumber' : replaceProjectName, 'changedID' : changedByID, 'jomasterID': jomasterID, 'bidDateCustomer' : bidCustomerDate, 'rxMaster' : rxMasterID },
   			success: function(data){
   				 $('#changeID').val(data.cuAssignmentId0);
	   			 $('#UpcomingBidsGrid').jqGrid('setGridParam',{ postData: {'salesRepId': changedByID } });
				 $("#UpcomingBidsGrid").trigger("reloadGrid");
				 loadCustomerFilterGrid(changedByID, changedByID, rxMasterID);
				 $('#customerFilterSearchGrid').jqGrid('setGridParam',{postData:{'createdLoginID' : changedByID, 'changedLoginID' : changedByID, 'rxMaster' : rxMasterID }});
				 $("#customerFilterSearchGrid").trigger("reloadGrid"); 
   		   		 }
   		   	});
  			return true;
		}
	});
 }
		
function openFindProject(){
	$("#customerFilterListID").val("");
var rxMasterID = getUrlVars()["rolodexNumber"];
$.ajax({
	url: "./search/searchrolodexForSale",
	mType: "GET",
	data : {'rolodex': rxMasterID},
	success: function(data){
		/* var createdLogin = data.createdByID;
		 var changedLogin = data.changedByID;
		 var loginID = 0;
		 if(createdLogin != 0){
			 loginID = createdLogin;
		 }else if(changedLogin != 0){
			 loginID = changedLogin;
		 }else if(createdLogin != 0 && changedLogin != 0){
			 loginID = createdLogin;
		 }
		 
		 var projectCustomerArray = new Array();	
		 $.ajax({
				url: "./search/getCustomerFindProject",
				mType: "GET",
				data : {'loginIDForCustomer': loginID },
				success: function(data){
					$.each(data, function(index, value) { 
						projectCustomerArray[index] = value.label;
					});
						$("#customerFilterListID").autocomplete({
							minLength: 3, source: projectCustomerArray, open: function(){ 
								$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./jobflow?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New</a></b></div>');
								 }, select: function (event, ui) {
								var descriptionName = ui.item.value;
								$.ajax({
									url: "./search/getsingleproject",
									mType: "GET",
									data : {'description': descriptionName },
									success: function(data){
										var name = data;
										var master = 
										jQuery("#findProjectDialog").dialog("close");
										jQuery("#bidListDialog").dialog("close");
										jQuery("#bidListsingleDialog").dialog("open");
										 $("#singlecustomerGrid").jqGrid('GridUnload');
										loadSingleCustomerGrid(loginID, loginID, data);
										$('#singlecustomerGrid').jqGrid('setGridParam',{postData:{'createdLoginID' : loginID, 'changedLoginID' : loginID, 'rxMaster' : data }});
										$("#singlecustomerGrid").trigger("reloadGrid"); 
									}
								});
							 },
						});
     				 },
     				error: function(Xhr) {
    				}
				});*/
			},
			error: function(Xhr) {
			}
		});
	jQuery("#findProjectDialog").dialog("open");
	return true;
}

function saveQuickQuote(){
	if(!$('#quickQuoteFormID').validationEngine('validate')) {
		return false;
	}
	var quickQuoteValues = $("#quickQuoteFormID").serialize();
	//var choosefile = $("#attachedID").val();
	//var quickQuote = quickQuoteValues/*& + "&choosefileattach="+choosefile*/;
	$.ajax({
		url: "./customerList/addQuickQuote",
		type: "POST",
		data: quickQuoteValues,
		success: function(data) {
			$("#OpenGridQuotes").trigger("reloadGrid"); 
			$("#customerLostQuotesGrid").trigger("reloadGrid"); 
			$("#customerQuotesGrid").trigger("reloadGrid"); 
			jQuery("#quickQuoteDialog").dialog("close");
		}
	});
	return true;
}

function viewQuickQuote(){
	var aInfo = true;
	if(aInfo){
		var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return true;
	}
}

function faxQuickQuote(){
	var aInfo = true;
	if(aInfo){
		var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return true;
	}		
}
		
function emailQuickQuote(){
	var aInfo = true;
	if(aInfo){
		var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return true;
	}
}
		
function cancelQuickQuote(){
	jQuery("#quickQuoteDialog").dialog("close");
	$('#quickQuoteDialog').validationEngine('hideAll');
	return true;
}
		

$(function() { var cache = {}; var lastXhr='';
$( "#customerFilterListID" ).autocomplete({ minLength: 3,timeout :1000,
	select: function (event, ui) {
		},
	open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./jobflow?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "search/searchjoblist", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

		

$(function() {var cache = {}; var lastXhr=''; $("#customerID").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
{ var rxMasterid = ui.item.id; $("#customerHiddenID").val(rxMasterid); /* var rxMasterid = ui.item.manufactureID; $("#rxMasterHiddenID").val(rxMasterid);*/},
source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
lastXhr = $.getJSON( "jobtabs4/customerContactList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

$(function() {var cache = {}; var lastXhr=''; $("#productListID").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
	{ 
		var quoteDetailID = ui.item.id; $("#productHiddenID").val(quoteDetailID); $.ajax({
			url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID },
			success: function(data) {
				$.each(data, function(index,value){
					var manufacture = value.inlineNote;
					var manufactureID = value.rxManufacturerID;
					$("#manufacturerID").val(manufacture);   $("#manufacterHiddenID").val(manufactureID);							
				});
			} }); },
	source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	lastXhr = $.getJSON( "jobtabs2/productList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} }); });

$(function() { var cache = {}; var lastXhr=''; $( "#manufacturerID" ).autocomplete({ minLength: 1, timeout :1000,
select: function( event, ui ) { /*var id = ui.item.id;*/ var manufacId = ui.item.manufactureID; $("#manufacterHiddenID").val(manufacId); },
source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); 
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

jQuery(function(){
	jQuery("#editQuickQuoteDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Edit Quote",
			height: 380,
			width: 520,
			buttons : {  },
			close:function(){
				$('#editQuickQuoteDialog').validationEngine('hideAll');
				return true;
			}
	});
});

function editQuickQuote(){
	var rowIdQuote;
	if ($('#lostQuote').is(':checked')){
		rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
	}else if($('#Quote').is(':checked')){
		rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
	}else{
		rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
	}
	var rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
	var newDialogDiv = jQuery(document.createElement('div'));
	if(rowId === null){
		var errorText = "Please add a record in contact grid.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var arxContactId = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'rxContactId');
		$("#customerHiddenID1").val(arxContactId);
	}
	if(rowIdQuote === null){
		var errorText = "Please Select a record";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var aJobNumber = '';
		var aJobName = '';
		var aContact = '';
		var aQuoteAmount = '';
		var aContractAmount = '';
		var aRevision = '';
		var aJoBidderID = '';
		var aJoMasterID = '';
		var aDate = '';
		var aRxContactID = '';
		var aRxMasterID = '';
		var Status;
		var aDescriptionID = '';
		if ($('#lostQuote').is(':checked')){
			rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
			rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobNo');
			aJobName = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobName');
			aContact = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'customerContact');
			aQuoteAmount = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteAmount');
			aContractAmount = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'contractAmount');
			aRevision = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteRev');
			aJoBidderID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'joBidderId');
			aJoMasterID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'joMasterId');
			aDate = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteDate');
			aRxContactID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'contactID');
			aRxMasterID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'rxMasterID');
			aDescriptionID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'description'); 
			// aJobStatus = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobStatus');
			aTotalCost = aQuoteAmount.replace(/[^0-9\.]+/g,"").replace(".00", ""); 
			aTotalPrice = aContractAmount.replace(/[^0-9\.]+/g,"").replace(".00", "");
			Status = 2;
		}else if($('#Quote').is(':checked')){
			rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobNo');
			aJobName = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobName');
			aContact = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'customerContact');
			aQuoteAmount = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteAmount');
			aContractAmount = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'contractAmount');
			aRevision = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteRev');
			aJoBidderID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'joBidderId');
			aJoMasterID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'joMasterId');
			aDate = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteDate');
			aRxContactID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'contactID');
			aRxMasterID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'rxMasterID');
			aDescriptionID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'description'); 
			// aJobStatus = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobStatus');
			aTotalCost = aQuoteAmount.replace(/[^0-9\.]+/g,"").replace(".00", ""); 
			aTotalPrice = aContractAmount.replace(/[^0-9\.]+/g,"").replace(".00", "");
			Status = 1;
		}else{
			rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'jobNo');
			aJobName = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'jobName');
			aContact = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'customerContact');
			aQuoteAmount = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'quoteAmount');
			aContractAmount = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'contractAmount');
			aRevision = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'quoteRev');
			aJoBidderID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'joBidderId');
			aJoMasterID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'joMasterId');
			aDate = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'quoteDate');
			aRxContactID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'contactID');
			aRxMasterID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'rxMasterID');
			aDescriptionID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'description'); 
			// aJobStatus = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobStatus');
			aTotalCost = aQuoteAmount.replace(/[^0-9\.]+/g,"").replace(".00", ""); 
			aTotalPrice = aContractAmount.replace(/[^0-9\.]+/g,"").replace(".00", "");
			Status = 3;
		}
		$("#editQuoteRxContactID").val(aRxContactID); $("#editQuoteRxMasterID").val(aRxMasterID);
		$("#customerHiddenID").val(""); $("#editQuoteCustomerID").val(aContact);
		$("#editQuoteProductListID").val(aJobName); $("#editQuoteCostID").val(aTotalCost); $("#editQuoteSellPriceID").val(aTotalPrice);
		$("#editQuoteRevisionID").val(aRevision); $("#editQuoteDateID").val(aDate);
		$("#editQuoteJobNumberID").val(aJobNumber); $("#editQuoteJoBidderID").val(aJoBidderID); $("#editQuoteJobMasterID").val(aJoMasterID);
		$("#editQuoteDescriptionID").val(aDescriptionID);
		$("#editQuoteJobStatusID option[value="+Status+"]").attr("selected", true);
		jQuery("#editQuickQuoteDialog").dialog("open");
	}
}
		
function updateQuickQuote(){
	if(!$('#editQuickQuoteFormID').validationEngine('validate')) {
		return false;
	}
	var quickQuoteValues = $("#editQuickQuoteFormID").serialize();
	$.ajax({
		url: "./customerList/updateQuickQuote",
		type: "POST",
		data: quickQuoteValues,
		success: function(data) {
			$("#OpenGridQuotes").trigger("reloadGrid"); 
			$("#customerLostQuotesGrid").trigger("reloadGrid"); 
			$("#customerQuotesGrid").trigger("reloadGrid"); 
			jQuery("#editQuickQuoteDialog").dialog("close");
		}
	});
	return true;
}

function cancelUpdateQuickQuote(){
	jQuery("#editQuickQuoteDialog").dialog("close");
	$('#editQuickQuoteDialog').validationEngine('hideAll');
}
/*Update rolodex categories in rxmaster table
 * Controller:rolodexdetailscontroller
 * 
 * */
function updateRolodex(){
	var rolodexNumber=getUrlVars()["rolodexNumber"];
	var aCustomer=false;
	var aVendor=false;
	var aEmployee=false;
	var aArchitect=false;
	var aEngineer=false;
	var aGCorConstrMGR=false;
	var aOwner=false;
	var aBondAgent=false;
	var category6=false;
	var category7=false;
	var category8=false;
	
	if ($('#customerchek').is(':checked')){
			aCustomer=true;
	}if ($('#vendorcheck').is(':checked')){
			aVendor=true;
	}if ($('#employeeche').is(':checked')){
			aEmployee=true;
	}if ($('#engineerche').is(':checked')){
			aEngineer=true;
	}if ($('#architectche').is(':checked')){
			aArchitect=true;
	}if ($('#caustMGR').is(':checked')){
		aGCorConstrMGR=true;
	}if ($('#owner').is(':checked')){
		aOwner=true;
	}if ($('#bondAgent').is(':checked')){
		aBondAgent=true;
	}
	if ($('#Category6Desc').is(':checked')){
		category6=true;
	}
	if ($('#Category7Desc').is(':checked')){
		category7=true;
	}
	if ($('#Category8Desc').is(':checked')){
		category8=true;
	}
	$.ajax({
		url: "rolodexdetails/updateRolodex",
		type: "POST",
		async:false,
		data :"&rolodexNumber=" +rolodexNumber+"&isCustomer="+aCustomer +"&isVendor="+aVendor +"&isEmployee="+aEmployee +"&isArchitect="+aArchitect +"&isEngineer="+aEngineer +"&isGCAndConstr="+aGCorConstrMGR +"&isOwner="+aOwner +"&isBondAgent="+aBondAgent+"&category6="+category6+"&category7="+category7+"&category8="+category8,
		success: function(data) {
		}
	});
}
			
function QuotesShow(){
	if($('#Quote').is(':checked')) {
		$("#lostQuote").prop("checked", false);
		$("#customerGridQuotes").hide();
		$("#lostQuotes").hide();
		$("#Quotes").show();
	}else if($('#lostQuote').is(':checked')){
		$("#Quote").prop("checked", false);
		$("#lostQuotes").show();
		$("#customerGridQuotes").hide();
		$("#Quotes").hide();
	}else{
		$("#lostQuote").prop("checked", false);
		$("#Quote").prop("checked", false);
		$("#customerGridQuotes").show();
		$("#lostQuotes").hide();
		$("#Quotes").hide();
	}
}

function openAddnewProduct(){
	var joMasterid= $("#editQuoteJobMasterID").val();
	var theQuoteRev = $("#editQuoteRevisionID").val();
	var typeID = $("#TypeDetailID").val();
	var revision;
	$.ajax({
		url: "./customerList/QuoteHeaderID",
		type: "GET",
		data: {'joMasterId' : joMasterid,'quoteRev' : theQuoteRev,'quoteTypeId' :  typeID},
		success: function(data) {
			joQuoteHeaderID = data.joQuoteHeaderId;
			typeID = data.cuMasterTypeID;
			revision = data.quoteRev;
			$("#revisionId").val(revision);
			$("#joQuoteHeaderID").val(joQuoteHeaderID);
			if(joQuoteHeaderID === null){
				var errorText = "Please add a Quote in Quotes Tab.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			else{
			$.ajax({
				url: "./customerList/typeID",
				type: "GET",
				data: {'quoteTypeId' :  typeID},
				success: function(data)	{
					$("#TypeDetail").val(data);
				}
			});
			$("#addquotesList").jqGrid('GridUnload');
			loadQuotesListDetails(joQuoteHeaderID);
			$("#addquotesList").trigger("reload");
			jQuery("#addProductDialog").dialog("open");	
			}	
		}
	});
}

jQuery(function(){
	jQuery("#addProductDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Add Product",
			height: 700,
			width: 850,
			buttons : {
			 "Close":function(){
				jQuery(this).dialog("close");
			} },
			close:function(){
				$('#addProductDialog').validationEngine('hideAll');
				return true;
			}
	});
});

function loadQuotesListDetails(joQuoteHeaderID){
	var joQuoteheaderID = joQuoteHeaderID;
	$("#addquotesList").jqGrid({
	datatype: 'JSON',
	url:'./jobtabs2/getQuoteListDetails',
	mtype: 'GET',
	postData: {'joQuoteHeaderID':joQuoteheaderID},
	pager: jQuery('#addquotespager'),
	colNames:['Notes','Description', 'Qty.', 'Paragraph', 'Vendors','Cost', 'Sell Price', 'QuoteDetailID', 'ManufacturerID', 'QuoteHeaderID', 'FactoryID','Inline Note','Posiion','Foot Note'],
	colModel:[
		{name:'inlineItem',index:'inlineItem',align:'right',width:30,editable: false,hidden: true, formatter: inlineFormatter},
		{name:'product',index:'product',align:'left',width:60,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:30,readonly:false},editrules:{edithidden:false,required: true}},
		{name:'itemQuantity',index:'itemQuantity',align:'center',width:20,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'paragraph',index:'paragraph',align:'left',width:60, cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';},editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'manufacturer',index:'manufacturer',align:'left',width:60, cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';},editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required: true}},
		{name:'cost',index:'cost',align:'right',width:25,editable:true,hidden:false,formatter:customCurrencyFormatter, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'price',index:'price',align:'right',width:25,editable:true,hidden:false,formatter:customCurrencyFormatter, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'joQuoteDetailID',index:'joQuoteDetailID',align:'left',width:30,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'rxManufacturerID',index:'rxManufacturerID',align:'left',width:30,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'joQuoteHeaderID',index:'joQuoteHeaderID',align:'left',width:30,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'veFactoryId',index:'veFactoryId',align:'left',width:3090,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'inlineNote',index:'inlineNote',align:'left',width:60,editable:true,hidden: true, edittype:'textarea', editoptions:{cols:30, rows:2, size:40,readonly:false},editrules:{edithidden:false,required:false}},
		{name:'position',index:'position',align:'left',width:90,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
		{name:'productNote',index:'productNote',align:'left',width:60,editable:true,hidden: true, edittype:'textarea', editoptions:{ cols:30, rows:2, size:40,readonly:false},editrules:{edithidden:false,required:false}}
	],
	rowNum: 1000,pgbuttons: false,recordtext: '',rowList: [],pgtext: null,viewrecords: true,
	sortname: 'Product', sortorder: "asc", imgpath: 'themes/basic/images',caption: 'Line Items',
	height:450,width: 800,
	//scrollOffset: 0,
	loadComplete: function(data) {
		$("#addquotesList").setSelection(1, true);
		var gridData = jQuery("#addquotesList").getRowData();
		var totalcost = 0;
		var totalPrice = 0;
		for(var index = 0; index < gridData.length; index++){
			var rowData = gridData[index];
			var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
			totalcost = totalcost + Number(cost);
			var price = rowData["price"].replace(/[^0-9\.]+/g,"");
			totalPrice = totalPrice + Number(price);
		}
		$("#totalPriceID").val(formatCurrency(totalPrice));
		$("#totalCostID").val(formatCurrency(totalcost));
		var joQuoteHeader = $("#joQuoteHeaderID").val();
		$.ajax({
			url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
			type: "GET",
			data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
			success: function(data) { }
		}); 
	},
	afterInsertRow: function(rowid, aData) {
		var aPositionID = $("#addquotesList").jqGrid('getCell', rowid, 'position');
		if(aPositionID === '0'){
			var aQuoteDetailID = $("#addquotesList").jqGrid('getCell', rowid, 'joQuoteDetailID');
			$.ajax({
				url: "./jobtabs2/updateInlineItemPosition",
				type: "GET",
				data : { 'joQuoteDetailID' : aQuoteDetailID },
				success: function(data) {
					$("#addquotesList").trigger("reloadGrid");
				}
		 	});
		}
	},
	loadError : function (jqXHR, textStatus, errorThrown){	},
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
		
	},
	editurl:'./jobtabs2/manpulaterProductQuotes'
}).navGrid('#addquotespager',{alertzIndex: 3234,search: false,pager:false, alertcap: "Warning", alerttext: 'Please select a Product'},
		//-----------------------edit options----------------------//
		{
			closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,zIndex:1234,
			modal:true,jqModal:true,viewPagerButtons: false,editCaption: "Edit Product",width: 430, top: 235, left: 320,
			beforeShowForm: function (form) 
			{	
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('Description: ');
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').append('Qty.: ');
				jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').append('Paragraph: ');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('Vendors: ');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').append('Cost: ');
				jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').append('Sell Price: ');
				jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').append('Inline Note: ');
				jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').append('Foot Note: ');
				aGlobalVariable = "edit";
				var cost = $("#cost").val().replace(/[^0-9\.]+/g,"");
				var price = $("#price").val().replace(/[^0-9\.]+/g,"");
				$("#cost").val(Number(cost)); $("#price").val(Number(price));
				$(function() {var cache = {}; var lastXhr=''; $("#product").autocomplete({ minLength: 2,select: function( event, ui ) 
					{ 
						var quoteDetailID = ui.item.id; var rxMasterID = ui.item.manufactureID; $("#quoteDetailID").val(quoteDetailID); $.ajax({
							url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID, 'rxMasterID' : rxMasterID },
							success: function(data) {
								$.each(data, function(index,value){
									manufacture = value.inlineNote;
									var detail = value.paragraph;
									var quantity = value.itemQuantity;
									var manufactureID = value.rxManufacturerID;
									var factoryID = value.detailSequenceId;
									$("#paragraph").val(detail); $("#manufacturer").val(quantity); //$("#itemQuantity").val(quantity); 
									$("#rxManufacturerID").val(manufactureID);	$("#veFactoryId").val(factoryID);						
								});
							} }); },
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "jobtabs2/productList", request, 
								function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
								error: function (result) {
								     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
								}  }); });

				$(function() { var cache = {}; var lastXhr='';
				$( "#manufacturer" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; var name = ui.item.value;  
				 	$("#rxManufacturerID").val(id);
				 	$.ajax({
						url: "./jobtabs2/getFactoryID",
						type: "GET",
						data : { 'rxMasterID' : id,'descripition' : name },
						success: function(data) {
							$("#veFactoryId").val(data);
						}
				 	});
				  },
					source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
						lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
							cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
							error: function (result) {
							     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
			},
			'onInitializeForm' : function(formid){
				jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 5 characters long" style="padding-left: 10px;"><br>');
				jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;"><br>');
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_inlineNote').show();
				jQuery('#TblGrid_addquotesList #tr_productNote').show();
			},
			beforeSubmit: function(postdata, formid){
				
				if($("#quoteTypeDetail").val() === ''){
					return [false, "Please provide 'Type' and 'Submitted By'"];
				}else if($("#jobQuoteSubmittedBYFullName").val() === ''){
					return [false, "Please provide 'Type' and 'Submitted By'"];
				}
				return [true,""];
			},
			afterSubmit: function(){
				$('#cData').trigger('click');
				$("#addquotesList").trigger("reloadGrid");
				var gridData = jQuery("#addquotesList").getRowData();
				var totalcost = 0;
				var totalPrice = 0;
				for(var index = 0; index < gridData.length; index++){
					var rowData = gridData[index];
					var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
					totalcost = totalcost + Number(cost);
					var price = rowData["price"].replace(/[^0-9\.]+/g,"");
					totalPrice = totalPrice + Number(price);
				}
				var aCurrentCost = $("#cost").val().replace(/[^0-9\.]+/g,"");
				var aCurrentPrice = $("#price").val().replace(/[^0-9\.]+/g,"");
				totalcost = totalcost + Number(aCurrentCost);
				totalPrice = totalPrice + Number(aCurrentPrice);
				var joQuoteHeader = $("#joQuoteHeaderID").val();
				$.ajax({
					url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
					type: "GET",
					data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
					success: function(data) {
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated successfully.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					jQuery(this).dialog("close");
					return true;
					}
				}); 
			},
			onclickSubmit: function(params){
				
			}
		},
		//-----------------------add options----------------------//
		{
			closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
			modal:true,
			jqModal:true,
			viewPagerButtons: false,
			addCaption: "Add Product",
			width: 430, top: 235, left: 320,zIndex:1234,
			beforeShowForm: function (form) 
			{
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('Description: ');
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').append('Qty.: ');
				jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').append('Paragraph: ');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('Vendors: ');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').append('Cost: ');
				jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').append('Sell Price: ');
				jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').append('Inline Note: ');
				jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').empty();
				jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').append('Foot Note: ');
				$(function() {var cache = {}; var lastXhr=''; $("#product").autocomplete({ minLength: 2,select: function( event, ui ) 
					{ 
						var quoteDetailID = ui.item.id; var rxMasterID = ui.item.manufactureID; $("#quoteDetailID").val(quoteDetailID); $.ajax({
							url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID, 'rxMasterID' : rxMasterID },
							success: function(data) {
								$.each(data, function(index,value){
									manufacture = value.inlineNote;
									var detail = value.paragraph;
									var quantity = value.itemQuantity;
									var manufactureID = value.rxManufacturerID;
									var factoryID = value.detailSequenceId;
									$("#paragraph").val(detail); $("#manufacturer").val(quantity); //$("#itemQuantity").val(quantity); 
									$("#rxManufacturerID").val(manufactureID);	$("#veFactoryId").val(factoryID);						
								});
							} }); },
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "jobtabs2/productList", request, 
								function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
								error: function (result) {
								     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
								}  }); });

				$(function() { var cache = {}; var lastXhr='';
				$( "#manufacturer" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; var name = ui.item.value;  
				 	$("#rxManufacturerID").val(id);
				 	$.ajax({
						url: "./jobtabs2/getFactoryID",
						type: "GET",
						data : { 'rxMasterID' : id,'descripition' : name },
						success: function(data) {
							$("#veFactoryId").val(data);
						}
				 	});
				  },
					source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
						lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
							cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
							error: function (result) {
							     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
			},
			'onInitializeForm' : function(formid){
				jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 5 characters long" style="padding-left: 10px;"><br>');
				jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;"><br>');
				jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
				jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
				jQuery('#TblGrid_addquotesList #tr_inlineNote').show();
				jQuery('#TblGrid_addquotesList #tr_productNote').show();
			},
			onclickSubmit: function(params){
				aGlobalVariable = "add";
				var joQuoteHeaderIdentity= joQuoteheaderID;
				if(aGlobalVariable !== "edit"){
					$("#joQuoteHeaderID").val(joQuoteHeaderIdentity);
					$('#addquotesList').jqGrid('setGridParam',{postData: {'joQuoteHeaderID': joQuoteHeaderIdentity}});
					jQuery("#joQuoteheader").text(joQuoteHeaderIdentity);
					joQuoteheaderID = joQuoteHeaderIdentity;
					$("#joHeaderID").val(joQuoteheaderID);
				}
				if(Number(joQuoteHeaderIdentity) === 0){
					var name = $("#joHeaderID").val();
					$('#addquotesList').jqGrid('setGridParam',{postData:{'joQuoteHeaderID': name}});
					jQuery("#joQuoteheader").text(name);
					joQuoteheaderID = name;
					$("#joHeaderID").val(joQuoteheaderID);
				} 
				if(joQuoteHeaderIdentity > joQuoteheaderID){
					joQuoteheaderID = joQuoteHeaderIdentity;
				}
				return { 'joQuoteHeaderID' : joQuoteheaderID, 'quoteheaderid' : joQuoteHeaderIdentity};
			},
			beforeSubmit: function(postdata, formid){
				aGlobalVariable = "edit";
				if($("#quoteTypeDetail").val() === ''){
					return [false, "Please provide 'Type' and 'Submitted By'"];
				}else if($("#jobQuoteSubmittedBYFullName").val() === ''){
					return [false, "Please provide 'Type' and 'Submitted By'"];
				}
				return [true,""];
			},
			afterSubmit: function(){
				$('#cData').trigger('click');
				$("#addquotesList").trigger("reloadGrid"); 
				var gridData = jQuery("#addquotesList").getRowData();
				var totalcost = 0;
				var totalPrice = 0;
				for(var index = 0; index < gridData.length; index++){
					var rowData = gridData[index];
					var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
					totalcost = totalcost + Number(cost);
					var price = rowData["price"].replace(/[^0-9\.]+/g,"");
					totalPrice = totalPrice + Number(price);
				}
				var aCurrentCost = $("#cost").val().replace(/[^0-9\.]+/g,"");
				var aCurrentPrice = $("#price").val().replace(/[^0-9\.]+/g,"");
				totalcost = totalcost + Number(aCurrentCost);
				totalPrice = totalPrice + Number(aCurrentPrice);
				var joQuoteHeader = $("#joQuoteHeaderID").val();
				$.ajax({
					url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
					type: "GET",
					data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
					success: function(data) {
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated successfully.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"keySuccess.", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					jQuery(this).dialog("close");
					return true;
					}
				});
			}
		},
		//-----------------------Delete options----------------------//
		{
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true, width:270, top: 296, left: 450,zIndex:1234,
			caption: "Delete Product",
			msg: 'Delete the Product?',
			onclickSubmit: function(params){
				aGlobalVariable = "del";
				var id = jQuery("#addquotesList").jqGrid('getGridParam','selrow');
				var key = jQuery("#addquotesList").getCell(id, 7);
				var costValue = jQuery("#addquotesList").getCell(id, 5);
				var priceValue = jQuery("#addquotesList").getCell(id, 6);
				var gridData = jQuery("#addquotesList").getRowData();
				var totalcost = 0;
				var totalPrice = 0;
				for(var index = 0; index < gridData.length; index++){
					var rowData = gridData[index];
					var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
					totalcost = totalcost + Number(cost);
					var price = rowData["price"].replace(/[^0-9\.]+/g,"");
					totalPrice = totalPrice + Number(price);
				}
				var aCurrentCost = costValue.replace(/[^0-9\.]+/g,"");
				var aCurrentPrice = priceValue.replace(/[^0-9\.]+/g,"");
				totalcost = totalcost - Number(aCurrentCost);
				totalPrice = totalPrice - Number(aCurrentPrice);
				var joQuoteHeader = $("#joQuoteHeaderID").val();
				$.ajax({
					url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
					type: "GET",
					data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
					success: function(data) {
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated successfully.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					jQuery(this).dialog("close");
					return true;
					}
				});
				return { 'joQuoteDetailID' : key };
			}
		}
	); }

function inlineFormatter(cellValue, options, rowObject){
	var element = "<div style='width: 52px;'><a onclick='openLineItemDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/lineItem.png' title='Inline Item' align='middle' style='vertical-align: middle;'></a>"+
							"<a onclick='openFooterLineDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/footNote.png' title='Foot Note' align='middle' style='vertical-align: middle;'></a></div>";
  	 return element;
} 

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

$(function() { var cache = {}; var lastXhr='';
$("#TypeDetail").autocomplete({
	minLength: 1, timeout: 1000,
	select: function(event, ui) {
		var id = ui.item.id; 
		$("#TypeDetailID").val(id);
	},
	source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
		lastXhr = $.getJSON("employeeCrud/customerType", request, function(data, status, xhr) {
			cache[term] = data; if (xhr === lastXhr) {response(data); } 
		});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
	});
});

 $(function() { var cache = {}; var lastXhr=''; $( "#County" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
	var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); var CountyId =  ui.item.id;
	$("#countyId").val(CountyId); $("#countyState").val(stateCode);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "rxdetailedviewtabs/countyAndstate", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}}); });
 
 
 function openBidList(){
	 var rxMasterIDvalue = $('#rxMasterIDhiden').val();
	 			jQuery("#openjobbidlist").dialog({
				width:500,
				title:"Bid List",
				modal:true,
				buttons:{	},
				close:function(){$('#opensplitcommisionForm').validationEngine('hideAll');
				return true;}	
			});
			var rxMasterIDvalue = $('#rxMasterIDhiden').val();
			/*./commissionsplits_listgrid*/
			$("#jobbidlistfromcustomer").jqGrid({
				url:'./job_controller/getJobBidListByCustomer?rxMasterID='+rxMasterIDvalue,
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#jobbidlistfromcustomerPager'),
				colNames:[ 'Bid Date', 'Job', 'Contact','JobNumber','joMasterID','Bid Status'],
				colModel :[
		           	{name:'bidDate',sortable:true, index:'bidDate', align:'left', width:40, editable:true,hidden:false},
					{name:'description', sortable:true,index:'description', align:'left',editable:true, width:80,hidden:false},
					{name:'quoteBy', index:'quoteBy', sortable:true,align:'',editable:true, width:40,hidden:false},
					{name:'jobNumber', index:'jobNumber', sortable:true,align:'',editable:true, width:40,hidden:true},
					{name:'joMasterId', index:'joMasterId', sortable:true,align:'',editable:true, width:40,hidden:true},
					{name:'bidStatus', index:'bidStatus', sortable:true,align:'',editable:true, width:40,hidden:true},
						],
				rowNum: 10,
				pgbuttons: true,
				 sortable: true,
				recordtext: '',
				sortname:'bidDate',
				sortorder:'desc',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: false,
				pgtext:null,
				pager: '#jobbidlistfromcustomerPager',
				imgpath: 'themes/basic/images',	
				//autowidth: true,
				height:250,	width: 475,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown) {
				    
				},
				ondblClickRow: function(rowId) {
					var rowData = jQuery(this).getRowData(rowId); 
					var jobName = rowData['description'];
					var jobNumber= rowData['jobNumber'];
					var jobStatus= rowData['bidStatus'];
					var joMasterId= rowData['joMasterId'];
					var urijobname=encodeBigurl(jobName);
					var aQryStr = "./jobflow?token=view&jobNumber="+jobNumber +"&jobName="+urijobname + "&jobStatus="+ jobStatus+"&joMasterID="+joMasterId+"&from=customer";
					
					var checkpermission=getGrantpermissionprivilage('Main',0);
					if(checkpermission){
					document.location.href = aQryStr;
					}
					//var aVePOId = rowData['vePOID'];
					//var aQryStr = "aVePOId=" + aVePOId;
					//document.location.href = "./editpurchaseorder?token=view&" + aQryStr;//./purchaseorder?token=view&" + aQryStr;
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
		    	editurl:""
			}).navGrid('#jobbidlistfromcustomerPager', {add:false, edit:false, del:false, search:false, view:false},
					{
				width:550, left:400, top: 300, zIndex:1040,
				
				closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
				modal:true, jqModel:true,
				addCaption: "Add Product",
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();						
					jQuery('#TblGrid_CommissionSplitsGrid #tr_rep .CaptionTD').empty();
					jQuery('#TblGrid_CommissionSplitsGrid #tr_rep .CaptionTD').append('Rep: ');
					jQuery('#TblGrid_CommissionSplitsGrid #tr_percentage .CaptionTD').empty();
					jQuery('#TblGrid_CommissionSplitsGrid #tr_percentage .CaptionTD').append('% Allocated: ');
					jQuery('#TblGrid_CommissionSplitsGrid #tr_splittype .CaptionTD').empty();
					jQuery('#TblGrid_CommissionSplitsGrid #tr_splittype .CaptionTD').append('Split Type: ');
					//$('#editmodlineItemGrid').css('z-index','1030');
					$("#cData").click(function(){
						$("#rep").autocomplete("destroy");
						 $(".ui-menu-item").hide();
						$("a.ui-jqdialog-titlebar-close").show();
					});
				}, 
				onInitializeForm: function(form){
					
				},
				afterShowForm: function($form) {
					
				},
				beforeSubmit:function(postdta, formid) {
					
				},onclickSubmit: function(options, postdata){
					
				},	
				afterSubmit:function(response,postData){
					
				}
			}
			
			); 
	  return true;
	}
 $(function() { var cache = {}; var lastXhr='';
	$( "#jobsearchlist" ).autocomplete({ minLength: 3,timeout :1000,sortResults: true,
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		select: function (event, ui) {
			var name = ui.item.value;
			var id = ui.item.id;
			$('#jomasterHidden').val(id);
			
	      },
		source: function( request, response ) { var term = request.term;
			if(term in cache) {response(cache[term] ); return;}
			lastXhr = $.getJSON( "search/searchjobincontacts", request, function(data, status, xhr){cache[term]=data; if(xhr===lastXhr){response(data);} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  });
	});
function addNewBid(){
	$('#jobbiddate').val('');
	$('#jobsearchlist').val('');
	  $('#bankAccountsID').prop('selectedIndex',0);
	var aOper="add";
	jQuery("#addjibbidlist").dialog({
		width:500,
		title:"Add Bid",
		modal:true,
		buttons:[{height:35,text: "Submit",click: function() {
			var contactid = $('#bankAccountsID').val();
			var rxmasterid = $('#rxMasterIDhiden').val();
			var jomasterid = $('#jomasterHidden').val();
			//alert(contactid+" :: "+rxmasterid+" :: "+ jomasterid+" :: "+biDate);
			var quotetypeId = $('#cuMastertype').val();
			var inParams ="rxMasterID="+rxmasterid+"&rxContactId="+contactid+"&joMasterId="+jomasterid+"&quoteTypeId="+quotetypeId;
			$.ajax({
				url: "./job_controller/saveJobBidListByCustomer?"+inParams,
				type: "POST",
				success: function(data) {
//					if(aOper === 'add'){
//						var validateMsg = "<b style='color:Green; align:right;'>Change Order Details Added.</b>";	
//						$("#bidMsg").css("display", "block");
//						$('#bidMsg').html(validateMsg);
//						setTimeout(function(){
//						$('#bidMsg').html("");						
//						},2000);
						 $("#addjibbidlist").dialog("close"); 
//						 $("#jobbidlistfromcustomer").trigger("reloadGrid"); 
//						 
//						return true;
//					}else if(aOper === 'edit'){
//						var validateMsg = "<b style='color:Green; align:right;'>Change Order Details Updated.</b>";	
//						$("#bidMsg").css("display", "block");
//						$('#bidMsg').html(validateMsg);
//						setTimeout(function(){
//						$('#bidMsg').html("");						
//						},2000);
//						 $("#addjibbidlist").dialog("close"); 
//						 
//						return true;
//					}
					$("#jobbidlistfromcustomer").trigger("reloadGrid");
				}
			});
			
		}},
		{height:35,text: "Cancel",click: function() { $(this).dialog("close"); }}],
		close:function(){}	
	});
	
}-                          

function closebidlistgrid(){
	$("#openjobbidlist").dialog("close");
}

