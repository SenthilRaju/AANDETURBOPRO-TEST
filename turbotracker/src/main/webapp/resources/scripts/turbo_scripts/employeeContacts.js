var aGlobalVariable;
var anOldPrice;
var anOldCost;
jQuery(document).ready(function() {
var aActive = $("#inActiveID").val();
var phone1=$("#phone1ID").val();
var phone2= $("#phone2ID").val();
var archTech = $("#isCategory1ID").val();
var Engg = $("#isCategory2ID").val();
var generalContract = $("#isCategory3ID").val();
var owner = $("#isCategory4ID").val();
var bondAgent = $("#isCategory5ID").val();
var customer = $("#isCustomerID").val(); 
var vendor = $("#isVendorID").val();
var Emp = $("#isEmployeeID").val();
var fax = $("#isFaxID").val();
var areacode2 = '';
var exchangecode2 = '';
var pinco = '';
var pinco1 = '';
var areacode1 = '';
var exchangecode1 = '';
var pincode2 = '';
var pincode3 = '';
if(aActive === 'true') {
	$('#InactiveCheckID').attr('checked','checked');
}
if(phone1 === null || phone1.length === 0){
	if(phone2 === null || phone2.length === 0){
		if(fax === null || fax.length === 0){
			return false;
		}else{	
			areacode2 = fax.split(" ");
			exchangecode2 = areacode2[1].split("-");
			pinco = areacode2[0].replace("(", "");
			pinco1 = pinco.replace(")", "");
			$("#areaCode2").val(pinco1); 
			$("#exchangeCode2").val(exchangecode2[0]);
			$("#subscriberNumber2").val(exchangecode2[1]);	
	}
}else{
		areacode1 = phone2.split(" ");
		exchangecode1 = areacode1[1].split("-");
		pincode2 = areacode1[0].replace("(", "");
		pincode3 = pincode2.replace(")", "");
		$("#areaCode1").val(pincode3); 
		$("#exchangeCode1").val(exchangecode1[0]);
		$("#subscriberNumber1").val(exchangecode1[1]);	
		if(fax === null || fax.length === 0){
			return false;
		}else{
			areacode2 = fax.split(" ");
			exchangecode2 = areacode2[1].split("-");
			pinco = areacode2[0].replace("(", "");
			pinco1 = pinco.replace(")", "");
			$("#areaCode2").val(pinco1); 
			$("#exchangeCode2").val(exchangecode2[0]);
			$("#subscriberNumber2").val(exchangecode2[1]);	
			}
		}	
}else{
	var areacode='';
	var exchangecode='';
	if(phone1!==undefined){
		areacode = phone1.split(" ");
	}
	if(typeof(areacode[1]) !=="undefined"){
		exchangecode = areacode[1].split("-");
	}
	var pincode = areacode[0].replace("(", "");
	var pincode1 = pincode.replace(")", "");
	$("#areaCode").val(pincode1); 
	$("#exchangeCode").val(exchangecode[0]);
	$("#subscriberNumber").val(exchangecode[1]);
	if(phone2 === null || phone2.length === 0){
		if(fax === null || fax.length === 0){
			return false;
		}else{
			areacode2 = fax.split(" ");
			exchangecode2 = areacode2[1].split("-");
			pinco = areacode2[0].replace("(", "");
			pinco1 = pinco.replace(")", "");
			$("#areaCode2").val(pinco1); 
			$("#exchangeCode2").val(exchangecode2[0]);
			$("#subscriberNumber2").val(exchangecode2[1]);	
		}
		}else{
			areacode1 = phone2.split(" ");
			exchangecode1 = areacode1[1].split("-");
			pincode2 = areacode1[0].replace("(", "");
			pincode3 = pincode2.replace(")", "");
			$("#areaCode1").val(pincode3); 
			$("#exchangeCode1").val(exchangecode1[0]);
			$("#subscriberNumber1").val(exchangecode1[1]);
		if(fax === null || fax.length === 0){
			return false;
		}else{
			areacode2 = fax.split(" ");
			exchangecode2 = areacode2[1].split("-");
			pinco = areacode2[0].replace("(", "");
			pinco1 = pinco.replace(")", "");
			$("#areaCode2").val(pinco1); 
			$("#exchangeCode2").val(exchangecode2[0]);
			$("#subscriberNumber2").val(exchangecode2[1]);	
		}
	}
}
loadContactsGrid();
loadAddressGrid();
loadGridOpenQuotes();
$("#customerChecked").css("display", "none");
$("#QuoteId").css("display", "none");
$("#lostQuotesCheck").hide();
$("#QuotesCheck").hide();
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
		jQuery("#EmployeeDetailsGrid").setGridHeight(150);
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
return true;
});


function savenewEngineerAddress(){
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var name1 = $("#nameID").val();
	var engineerForm = $("#addNewEmployeeForm").serialize();
	var address = $("#address1ID").val();
	var searchName=$("#searchNameID").val();
	var customer=$("#isCustomerID").val();
	var vendor=$("#isVendorID").val();
	var isEmployee=$("#isEmployeeID").val();
	var architect= $("#isCategory1ID").val();
	var engineer=$("#isCategory2ID").val();
	if(!$('#addNewEmployeeForm').validationEngine('validate')) {
		return false;
	}
	var areaCode=$("#areaCode").val();
	var exchangeCode = $("#exchangeCode").val();
	var subscriberNumber = $("#subscriberNumber").val();
	var contact1='';
	if(areaCode !== ''){
	contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	var areaCode1=$("#areaCode1").val();
	var exchangeCode1 = $("#exchangeCode1").val();
	var subscriberNumber1 = $("#subscriberNumber1").val();
	var contact2='';
	if(areaCode1 !== ''){
		contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
	}
	var areaCode2=$("#areaCode2").val();
	var exchangeCode2 = $("#exchangeCode2").val();
	var subscriberNumber2 = $("#subscriberNumber2").val();
	var fax='';
	if(areaCode2 !== ''){
		fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
	}
	var aActive = false;
	if($('#InactiveCheckID').is(':checked')){
		aActive =  true;
	}
	$.ajax({
			type:'POST',
			url: "customerList/newCustomerAddress",
			data: engineerForm+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax=" +fax +"&rolodexNumber="+ rolodexNumber+"&existingAddress1="+ address +"&Name=" +name1+ "&EmployeeId=" +isEmployee +"&searchName=" +searchName+
			"&CustomerId=" +customer + "&VendorId=" +vendor + "&ArchitectId=" +architect+"&EngineerId=" +engineer+"&EmployeeId=" +isEmployee+"&aEmployeeActive=" +aActive,
			success:function(data){
				createtpusage('Company-Employee','Save Employee Address','Info','Company-Employee,Saving Address,EmployeeId:'+isEmployee);
				window.location.reload();
			}
		});
	return true;
}
	
function loadAddressGrid() {
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
	var grid = $('#EmployeeDetailCategoriesGrid');
	$("#EmployeeDetailCategoriesGrid").jqGrid({
		url : 'vendorscontroller/vendoraddress',
		datatype : 'JSON',
		postData : {'rolodexNumber' : rolodexNumber },
		mtype : 'GET',
		colNames : [ 'Address', 'City', 'State', 'Country',	'Zip', 'Phone', 'Fax' ],
		colModel : 
		[{name : 'address1',index : 'address1',	align : 'left',	width : 110,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {size : 30,readonly : true},editrules : {edithidden : false,required : false}},
		 {name : 'city',index : 'city',align : 'left',width : 40,hidden : false,editable : true,	editoptions : {size : 20,readonly : false,alignText : 'right'	},editrules : {	edithidden : true,required : true}},
		 {name : 'state',index : 'state',align : 'left',width : 40,hidden : false,editable : true,edittype : 'textarea',editoptions : {},editrules : {	edithidden : true,	required : false}},
		 {name : 'zip',index : 'zip',align : 'left',width : 30,hidden : false,editable : true,edittype : 'textarea',	editoptions : {},	editrules : {edithidden : true,required : false}},
		 {name : 'address2',index : 'address2',	align : 'left',	width : 50,	hidden : false,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {edithidden : true,		required : false	}},
		 {name : 'name',	index : 'name',	align : 'left',	width : 40,	hidden : false,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {edithidden : true,	required : false	}} ],
		recordtext : '',
		rowList : [],
		pgtext : null,
		viewrecords : false,
		sortname : ' ',
		sortorder : "asc",
		imgpath : 'themes/basic/images',
		caption : 'Adderss',
		height : 200,
		rownumbers:true,
		altRows: true,
		altclass:'myAltRowClass',
		width : 1100,
		loadComplete : function(data) {		},
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
		loadError : function(jqXHR, textStatus, errorThrown) {	}
	});
	emptyMsgDiv.insertAfter(grid.parent());
	return true;
}

function loadContactsGrid() {
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
	var grid = $('#EmployeeDetailsGrid');
	$("#EmployeeDetailsGrid").jqGrid({
		url: './vendorscontroller/vendorcontact',
		datatype: 'JSON',
		postData: {'rolodexNumber' : rolodexNumber },
		mtype: 'GET',
		colNames: [ 'RxContactId','Last Name', 'First Name', 'Role', 'Phone', 'Direct Line', 'Email', 'CellPhone', 'Division' ],
		colModel: 
		[{name: 'rxContactId',index:'rxContactId',align:'left',width:50,hidden: true, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 {name: 'lastName',index : 'lastName',align : 'left',width : 60, editable : true,hidden : false,editoptions : {size : 30,readonly : true},editrules : {edithidden : false,required : false}},
		 {name: 'firstName',index : 'firstName',align : 'left',width : 60,hidden : false,editable : true, editoptions : {size : 20,readonly : false,alignText : 'right'	},editrules : {edithidden : true,required : true}},
		 {name: 'jobPosition',index : 'jobPosition',align : 'left',	width : 75,	hidden : false,editable : true,	editoptions : {},editrules: {edithidden: true,required : false	}},
		 {name: 'phone',index : 'phone',align : 'left',width : 50,hidden : true,	editable : true,editoptions : {},editrules : {edithidden: true, required : false }},
		 {name: 'directLine',index : 'directLine',align : 'left',width : 50,hidden : false,editable : true,editoptions : {},editrules : {edithidden: true,required : false}},
		 {name: 'email', index : 'email', align : 'left',width : 90,hidden : false,editable : true,editoptions: {},editrules : {	edithidden: true, required : false}},
		 {name: 'cell',index : 'cell',align : 'left',width : 50,hidden : false,	editable : true,editoptions : {},editrules : {edithidden: true, required : false	}},
		 {name: 'division',index : 'division',align : 'left',width : 50,hidden : false,editable : true,editoptions : {},editrules : {edithidden: true,	required : false}}],
		 rowNum: 0,
		recordtext: '',
		rowList: [],
		pgtext: null,
		viewrecords: false,
		sortname: ' ',
		sortorder: "asc",
		imgpath: 'themes/basic/images',
		height: 350,
		altRows: true,
		altclass:'myAltRowClass',
		rownumbers:true,
		width: 1100,
		loadComplete: function(data) {	
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
		jsonReader: {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
		loadError : function(jqXHR, textStatus, errorThrown) {	}
	});
	emptyMsgDiv.insertAfter(grid.parent());
	return true;
}

$(function() { var cache = {}; var lastXhr=''; $( "#locationCity" ).autocomplete({ minLength: 2,timeout :1000,
select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationState").val(stateCode);},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}}); });

function addEmployeeContact(){
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

function addEngineerContacts(){	
	aGlobalVariable = "add";
	document.getElementById("engineerContactCustomForm").reset();
	$("#ContactIDTD").hide(); 
	jQuery("#engineerContactCustom").dialog("open");
}

jQuery(function(){
	jQuery("#engineerContactCustom").dialog({
			autoOpen : false,
			modal : true,
			title:"Add/Edit Contact",
			width: 400,  left: 300, top: 290,
			buttons : {  },
			close:function(){
				$('#engineerContactCustomForm').validationEngine('hideAll');
				return true;
			}
	});
});
	
function editEngineerContacts(){
	aGlobalVariable = "edit";
	var grid = $("#EmployeeDetailsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select a Contact.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var rxContactID = grid.jqGrid('getCell', rowId, 'rxContactId');
	var lastName = grid.jqGrid('getCell', rowId, 'lastName');
	var firstName = grid.jqGrid('getCell', rowId, 'firstName');
	var role = grid.jqGrid('getCell', rowId, 'jobPosition');
	var directline = grid.jqGrid('getCell', rowId, 'directLine');
	var email = grid.jqGrid('getCell', rowId, 'email');
	var division = grid.jqGrid('getCell', rowId, 'division');
	var phone = grid.jqGrid('getCell', rowId, 'cell');
	if(phone === null || phone.length === 0){
		$("#subscriber1").val(''); $("#areaCode1").val(''); $("#exchangeCode1").val('');
		$("#ContactIDTD").hide(); 
		jQuery("#engineerContactCustom").dialog("open");
	}else{
		var areacode='';
		var exchangecode='';
		if(phone!==undefined){
			areacode = phone.split(" ");
		}
		if(typeof(areacode[1]) !=="undefined"){
			exchangecode = areacode[1].split("-");
		}
		var pincode = areacode[0].replace("(", "");
		var pincode1 = pincode.replace(")", "");
		$("#subscriber1").val(exchangecode[1]);
		$("#areaCode1").val(pincode1); $("#exchangeCode1").val(exchangecode[0]);
	}
	 if(directline === null || directline.length === 0){
		 $("#areaCodeDirID").val('');
			$("#exchangeCodeDirID").val('');
			$("#subscriberNumberDirID").val('');
			$("#ContactIDTD").hide(); 
	 }else{
		var areacodeDir = directline.split(" ");
		var exchangecodeDir = areacodeDir[1].split("-");
		var pincodeDir = areacodeDir[0].replace("(", "");
		var pincode1Dir = pincodeDir.replace(")", "");
		$("#areaCodeDirID").val(pincode1Dir);
		$("#exchangeCodeDirID").val(exchangecodeDir[0]);
		$("#subscriberNumberDirID").val(exchangecodeDir[1]);
	 }
		$("#rxContactId").val(rxContactID); $("#lastName").val(lastName); $("#firstName").val(firstName); 
		$("#jobPosition").val(role); $("#email").val(email);  $("#directLine").val(directline);$("#division").val(division);
		$("#ContactIDTD").hide();
		jQuery("#engineerContactCustom").dialog("open");
		return true;
}

function deleteRolodexContact(){
	aGlobalVariable = "del";
	var grid = $("#EmployeeDetailsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var newDialogDiv = jQuery(document.createElement('div'));
	if(rowId === null){
		var errorText = "Please select a Contact.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
	
	jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Contact Record?</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
		buttons:{
			"Submit": function(){
				deleteContact();
				jQuery(this).dialog("close");
				$("#EmployeeDetailsGrid").trigger("reloadGrid");
			},
			Cancel: function ()	{jQuery(this).dialog("close");}
			}}).dialog("open");
	return true;
	}
}

function deleteContact(){
	var grid = $("#EmployeeDetailsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var rxContactID = grid.jqGrid('getCell', rowId, 'rxContactId');
	var rxMasterID= getUrlVars()['rolodexNumber'];
	$.ajax({
		url: "rolodexdetails/manpulaterxContact",
		type: "POST",
		async:false,
		data : "&oper="+aGlobalVariable +"&rxMasterId=" +rxMasterID +"&rxContactId=" + rxContactID,
		success: function(data) {
			$("#EmployeeDetailsGrid").trigger("reloadGrid");
			$("#OpenGridQuotes").trigger("reloadGrid"); 
			$("#customerLostQuotesGrid").trigger("reloadGrid"); 
		}
	});
	return true;
}

function saveRolodexContact(){
	var contactListValue = $("#engineerContactCustomForm").serialize();
	var rxMasterId = getUrlVars()["rolodexNumber"];
	var areaCode=$("#areaCode1").val();
	var exchangeCode = $("#exchangeCode1").val();
	var subscriberNumber = $("#subscriber1").val();
	var areaCodeDir = $("#areaCodeDirID").val();
	var exchageCodeDir =$("#exchangeCodeDirID").val();
	var subscriberNumberDir =$("#subscriberNumberDirID").val();
	var cell= '';
	var dir = '';
	if(areaCode !== ""){
		cell="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	if(areaCodeDir !== ""){
		dir ="("+areaCodeDir+") "+exchageCodeDir+"-"+subscriberNumberDir;
	}
	if(aGlobalVariable === "add"){
	$.ajax({
		url: "rolodexdetails/manpulaterxContact",
		type: "POST",
		async:false,
		data : contactListValue + "&oper="+aGlobalVariable +"&rxMasterId=" +rxMasterId +"&cell=" + cell+"&directDir="+dir,
		success: function(data) {
			$("#EmployeeDetailsGrid").trigger("reloadGrid");
		}
	});
	}else if(aGlobalVariable === "edit"){
		$.ajax({
			url: "rolodexdetails/manpulaterxContact",
			type: "POST",
			async:false,
			data : contactListValue + "&oper="+aGlobalVariable +"&rxMasterId=" + rxMasterId +"&cell=" + cell+"&directDir="+dir,
			success: function(data) {
				$("#EmployeeDetailsGrid").trigger("reloadGrid");
				var errorText = "Contact Successfully Edited.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			}
		});
	}
	return true;
}

function submitContact(){
	if(aGlobalVariable === "add"){
		if(!$('#engineerContactCustomForm').validationEngine('validate')) {
			return false;
		}
	saveRolodexContact();
	jQuery("#engineerContactCustom").dialog("close");
	}else if(aGlobalVariable === "edit"){
		if(!$('#engineerContactCustomForm').validationEngine('validate')) {
			return false;
		}
		saveRolodexContact();
		$('#engineerContactCustomForm').validationEngine('hideAll');
		jQuery("#engineerContactCustom").dialog("close");
	}
	return true;
}

function cancelContact(){
	$('#engineerContactCustomForm').validationEngine('hideAll');
	jQuery("#engineerContactCustom").dialog("close");
	return true;
}

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
	$.ajax({
		url: "rolodexdetails/updateRolodex",
		type: "POST",
		async:false,
		data :"&rolodexNumber=" +rolodexNumber+"&isCustomer="+aCustomer +"&isVendor="+aVendor +"&isEmployee="+aEmployee +"&isArchitect="+aArchitect +"&isEngineer="+aEngineer +"&isGCAndConstr="+aGCorConstrMGR +"&isOwner="+aOwner +"&isBondAgent="+aBondAgent,
		success: function(data) {
		}
	});
	return true;
}

function openQuickQuote(){
	var errorText = '';
	var newDialogDiv = jQuery(document.createElement('div'));
	if ($('#customerchek').is(':checked')) {
		var rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
		if(rowId === null){
				errorText = "Please add a record in contact grid.";
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
		errorText = "The 'Customer' box must be checked in order to use this feature.";
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

$(function() {var cache = {}; var lastXhr=''; $("#customerID").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
{ var rxMasterid = ui.item.id; $("#customerHiddenID").val(rxMasterid); /* var rxMasterid = ui.item.manufactureID; $("#rxMasterHiddenID").val(rxMasterid);*/},
source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
lastXhr = $.getJSON( "jobtabs4/customerContactList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

$(function() {var cache = {}; var lastXhr=''; $("#productListID").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) { 
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

function lostQuotesShow(){
	if($('#lostQuote').is(':checked')) {
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


function loadQuotesGrid(rxContactId) {
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
	var grid = $('#customerQuotesGrid');
	$("#customerQuotesGrid").jqGrid({
		url:'customerList/customerQuotes',
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
		rowNum: 0, viewrecords:false, sortorder:"asc", imgpath: 'themes/basic/images', caption: 'Quick Quotes',
		pgbuttons: false,recordtext:'',rowList:[],pgtext: null, height:180, width:1100, rownumbers:true,
		altRows:true, altclass:'myAltRowClass',
		loadComplete:function(data) {
			if (jQuery("#customerQuotesGrid").getGridParam("records") === 0) {
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
											"<a onclick='viewJobPage()'><img src='./../resources/images/view_quote.png' title='View Job' align='middle' style='padding: 2px 5px;'></a></div>";
  	 return element;
}

function viewJobPage(){
	var rowIdQuote;
	var errorText = ''; 
	var rowId;
	if ($('#lostQuote').is(':checked')){
		rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
	}else if($('#Quote').is(':checked')){
		rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
	}else{
		rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
	}
	rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
	var newDialogDiv = jQuery(document.createElement('div'));
	if(rowId === null){
		errorText = "Please add a record in contact grid.";
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
		errorText = "Please Select a record";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var aJobNumber = '';
		var aJobName = '';
		var aJobCustomer='';
		var aJobStatus = '';
		var joMasterID=0
		if ($('#lostQuote').is(':checked')){
			rowId = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'jobNo');
			aJobName = $("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'jobName');
			joMasterID=$("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'joMasterId');
			
			aJobCustomer='';
			aJobStatus = "Lost";
		}else if($('#Quote').is(':checked')){
			rowId = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#customerQuotesGrid").jqGrid('getCell', rowId, 'jobNo');
			aJobName = $("#customerQuotesGrid").jqGrid('getCell', rowId, 'jobName');
			joMasterID=$("#customerQuotesGrid").jqGrid('getCell', rowId, 'joMasterId');
			aJobCustomer='';
			aJobStatus = "Quote";
		}else{
			rowId = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
			aJobNumber = $("#OpenGridQuotes").jqGrid('getCell', rowId, 'jobNo');
			aJobName = $("#OpenGridQuotes").jqGrid('getCell', rowId, 'jobName');
			joMasterID=$("#OpenGridQuotes").jqGrid('getCell', rowId, 'joMasterId');
			aJobCustomer='';
			aJobStatus = "Booked";
		}
		var checkpermission=getGrantpermissionprivilage('Main',0);
		if(checkpermission){
			var urijobname=encodeBigurl(aJobName);
			var uricusname=encodeBigurl(aJobCustomer);
		var aQryStr = "jobNumber="+aJobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+aJobStatus+"&joMasterID="+joMasterID;
		document.location.href = "./jobflow?token=view&" + aQryStr;
		}
	}
	return true;
}
		
function editQuickQuote(){
	var rowIdQuote;
	var errorText='';
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
		errorText = "Please add a record in contact grid.";
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
		errorText = "Please Select a record";
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
		$("#editQuoteJobStatusID option[value="+Status+"]").attr("selected", true);
		jQuery("#editQuickQuoteDialog").dialog("open");
	}
	return true;
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
			return false;
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
			beforeShowForm: function (form) {	
				var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
				var rowData = $("#addquotesList").getRowData(rowId);
		      	var Cost = rowData['cost'];
		        anOldCost = Cost;
		      	var Price = rowData['price'];
		      	anOldPrice = Price;
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
				var costValue = aCurrentCost-anOldCost;
				var aCurrentPrice = $("#price").val().replace(/[^0-9\.]+/g,"");
				var priceValue = aCurrentPrice-anOldPrice;
				totalcost = totalcost + costValue;
				totalPrice = totalPrice + priceValue;
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
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					jQuery(this).dialog("close");
					return true;
					}
				});
			}
		},
		//-----------------------Delete options----------------------//
		{
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, width:270, jqModal:true, top: 296, left: 450,zIndex:1234,
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
				return { 'joQuoteDetailID' : key};
			}});
}

function inlineFormatter(cellValue, options, rowObject){
	var element = "<div style='width: 52px;'><a onclick='openLineItemDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/lineItem.png' title='Inline Item' align='middle' style='vertical-align: middle;'></a>"+
							"<a onclick='openFooterLineDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/footNote.png' title='Foot Note' align='middle' style='vertical-align: middle;'></a></div>";
  	 return element;
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
		
function currencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}
