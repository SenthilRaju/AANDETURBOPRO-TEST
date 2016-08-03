jQuery(document).ready(function() {
	var addCustomer = getUrlVars()["oper"];
	if(addCustomer === "add"){
		openAddNewCustomerDialog();
	}else{
		$("#addnewcustab").css("display", "none");
	}
	loadCustomersGrid();
});

function loadCustomersGrid() {
	var aCustomerPage = $("#customersID").val();
	$("#customersGrid").jqGrid({
		datatype: 'json',
		mtype: 'GET',
		url:'./customerList',
		pager: jQuery('#customersGridpager'),
	   	colNames:['rxId', 'Name', 'Phone','Address', 'City', 'State'],
	   	colModel:[
			{name:'rxMasterId',index:'rxMasterId', width:170,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'name',index:'name', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'phone1',index:'phone1',align:'center', width:100,editable:true, formatter:phoneFormatter, editrules:{required:true}, editoptions:{size:10}},
			{name:'address1',index:'address1', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'city',index:'city', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'state',index:'state',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}}],
	   	rowNum: aCustomerPage,	
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#customersGridpager',
		sortname: 'name',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Customers',
		height:540,	width: 1150,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
			$(".ui-pg-selbox").attr("selected", aCustomerPage);
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
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId); 
    		var rxNumber = rowData['rxMasterId'];
    		var name = rowData['name'];
    		var name1 = name.replace('&', 'and');
    		var name2 = name1.replace('&', 'and');
    		var checkpermission=getGrantpermissionprivilage('Customers',0);
			if(checkpermission){
			document.location.href = "./customerdetails?rolodexNumber="+rxNumber+"&name="+'`'+name2+'`';
			}
		}
	}).navGrid('#customersGridpager',
		{add:false,edit:false,del:false,refresh:false,search:false}
	);
	return true;
}

function phoneFormatter(cellvalue, options, rowObject) {
	return formatPhone(cellvalue);
}
			
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
		return '';
	}
}

function contains(str, text) {
	return str.indexOf(text) >= 0;
}
			
function openAddNewCustomerDialog(){	
	$("#customerName").val("");
	$("#address1").val("");$("#address2").val("");$("#cityNameListID").val("");$("#stateCode").val("");$("#pinCode").val("");
	$("#areaCode").val("");$("#exchangeCode").val("");$("#subscriberNumber").val("");$("#areaCode1").val("");$("#exchangeCode1").val("");
	$("#areaCode2").val("");$("#exchangeCode2").val("");$("#subscriberNumber2").val("");$("#subscriberNumber1").val("");
	jQuery("#addnewcustab").dialog("open");
}

function opendeleteNewCustomerDialog()
{
	var grid = $("#customersGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var curxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterId');
	
	if(curxMasterID!=false)
		{
		
 				var Warnintext = "Do you want to delete this Customer?";
 				var newDialogDivouter = jQuery(document.createElement('div'));
				$(newDialogDivouter).attr("id","msgdlg");
				$(newDialogDivouter).css({"text-align":"left","margin-left":"20px;"});
				jQuery(newDialogDivouter).html('<span><b style="color:red;text-align:left;">'+Warnintext+'</b></span>');
				jQuery(newDialogDivouter).dialog({modal: true, width:400, height:200, title:"Confirmation",
					buttons: [{
						
						text: "Yes",
						click: 
							function() {

							$.ajax({
								url:'./customerList/customerdeleteConfirmation',
								type:"POST",
								data: {rxCustomerID:curxMasterID},
								success:function(data){ 
		
									if(data)
										{
									var Warnintext = "You couldn't delete this customer. Only you can inactive this customer. Shall i proceed?";
									var newDialogDiv = jQuery(document.createElement('div'));
									$(newDialogDiv).attr("id","msgdlg");
									$(newDialogDiv).css({"text-align":"left","margin-left":"20px;"});
									jQuery(newDialogDiv).html('<span><b style="color:red;text-align:left;">'+Warnintext+'</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:400, height:200, title:"Confirmation",
										buttons: [{
											
													text: "Yes",
													click: 
														function() {
															
														$.ajax({
															url:'./customerList/customerInactive',
															type:"POST",
															data: {rxCustomerID:curxMasterID},
															success:function(data){ 
																jQuery(newDialogDivouter).dialog("close");
																jQuery(newDialogDiv).dialog("close");
																var newDialogDiv1 = jQuery(document.createElement('div'));
																$(newDialogDiv1).css({"text-align":"left","margin-left":"20px;"});
																jQuery(newDialogDiv1).html('<span><b style="color:Green;">Customer Inactivated.</b></span>');
																jQuery(newDialogDiv1).dialog({modal: true, width:300, height:150, title:"Success ", 
																						buttons: [{text: "OK",click: function(){ $(this).dialog("close");}}]
																					}).dialog("open");
																$("#customersGrid").trigger("reloadGrid"); 
															}
															});
		
														
														 }
												 },
												 
												 {
													 text: "Cancel",
													 click: 
														function() {
														 jQuery(newDialogDivouter).dialog("close");
														 $(this).dialog("close");
														 }
												 }
														 
		
												 ]}).dialog("open");
										}
									else
										{
										jQuery(newDialogDivouter).dialog("close");
										var newDialogDiv1 = jQuery(document.createElement('div'));
										$(newDialogDiv1).css({"text-align":"left","margin-left":"20px;"});
										jQuery(newDialogDiv1).html('<span><b style="color:Green;">Customer Deleted.</b></span>');
										jQuery(newDialogDiv1).dialog({modal: true, width:300, height:150, title:"Success ", 
																buttons: [{text: "OK",click: function(){ $(this).dialog("close");}}]
															}).dialog("open");
										$("#customersGrid").trigger("reloadGrid"); 
										
										}
								
							 	},
								error:function(data){
									var Warnintext = "Error";
									var newDialogDiv = jQuery(document.createElement('div'));
									$(newDialogDiv).attr("id","msgdlg");
									$(newDialogDiv).css({"text-align":"left","margin-left":"20px;"});
									jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
								}
								});


							
							 }
					 },
					 
					 {
						 text: "No",
						 click: 
							function() {
							 $(this).dialog("close");
							 }
					 }
							 

					 ]}).dialog("open");
				

		}
}

$(function() { var cache = {}; var lastXhr='';
$( "#searchJob" ).autocomplete({ minLength: 3, timeout :1000,
	open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./customers?oper=add" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Customer</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	},
	select: function (event, ui) {
		var name = ui.item.value;
		$.ajax({
			url: "./search/searchrolodex",
			mType: "GET", timeout : 1000,
			data : {'rolodex': name},
			success: function(data){
				 var rxId="";
				$.each(data, function(index, value){
					entityValue = value.entity;
					rxId =value.pk_fields; 
				});
				var value = name.split(": ");
				var entity = value[0];
				var text = value[1];
				var text1 = text.split(",  ");
				var searchText = text1[0];
				var search = searchText.replace('&','and');
				var search1= search.replace('&','and');
				var searchlist = "";
				if(entity == "EMP")	{
					searchlist = entity.replace("EMP","employeedetails");
				}if(entity == "VEND") {
					searchlist = entity.replace("VEND","vendordetails");
				}if(entity == "CUST") {
					searchlist = entity.replace("CUST","customerdetails");
				}if(entity == "ARCH") {
					searchlist = entity.replace("ARCH","architectDetails");
				}if(entity == "ENGR") {
					searchlist = entity.replace("ENGR","engineerDetails");
				}if(entity == "ARCH/ENGR"){
					searchlist = entity.replace("ARCH/ENGR","architectDetails");
				}if(entity == "G.C") {
					searchlist = entity.replace("G.C","rolodexdetails");
				}
				location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+'('+search1+')'+"";
			},
			error: function(Xhr) {
			}
		});
	},
	source: function( request, response ) { 
		var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	
		return; 	
		}
		lastXhr = $.getJSON( "search/searchCustomerList", request, function( data, status, xhr ) {
		cache[ term ] = data;
		response( data );
		if ( xhr === lastXhr ) { 
			response( data );	
			}
		});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });