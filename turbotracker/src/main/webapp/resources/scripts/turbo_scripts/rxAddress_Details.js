/**
 * Created by :Leo    Date:Nov 7 2015
 * 
 * Purpose : for rolodex address.
 * */
var aGlobalVariableforAddress = "";

//Load address grid dialog
jQuery(function(){
		jQuery("#rxAddressGrid").dialog({
				autoOpen : false,
				modal : true,
				title:"Rolodex Address",
				width: 1070,  left: 300, top: 290,
				buttons : {  },
				close:function(){
					return true;
				}
		}); 
		
	}); 

//Load address dialog
jQuery(function(){
	jQuery("#addRolodexAddress").dialog({
			autoOpen : false,
			modal : true,
			title:"Add/Edit Address",
			width: 'auto',  left: 300, top: 290,
			buttons : {  },
			open:function(){
				if($('#customerchek').is(':checked'))
					$("#addressSetting").css({"display":"inline"});
				else
					$("#addressSetting").css({"display":"none"});
				
				if ($('#vendorcheck').is(':checked'))
					$("#remitaddress").css({"display":"inline"});
				else
					$("#remitaddress").css({"display":"none"});
				
			},
			close:function(){
				return true;
			}
	});
});


//Load address grid
function loadrxAddressGrid(rolodexid) {
		var rolodexNumber = rolodexid;
		var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
		var grid = $('#rxAddressGridCategoriesGrid');
		$("#rxAddressGridCategoriesGrid").jqGrid({
			url : 'vendorscontroller/vendoraddress',
			datatype : 'JSON',
			postData : {'rolodexNumber' : rolodexNumber },
			mtype : 'GET',
			colNames : [ 'AddressId','Address','Address2', 'City', 'State','Zip','Phone','Phone2','Fax','Mailing','Shipping','Default','RemitTo' ],
			colModel : [
			 {name : 'rxAddressId',index : 'rxAddressId',	align : 'left',	width : 100,editable : true,hidden : true,edittype : 'text'},
			 {name : 'address1',	index : 'address1',	align : 'left',	width : 100,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {size : 30,readonly : true},editrules : {edithidden : false,required : false}},
			 {name : 'address2',	index : 'address2',	align : 'left',	width : 100,	editable : true,	hidden : true,	edittype : 'text',	editoptions : {size : 30,readonly : true},editrules : {edithidden : false,required : false}},
			 {name : 'city',index : 'city',align : 'left',width : 40,hidden : false,editable : true,	editoptions : {size : 20,readonly : false,alignText : 'right'	},editrules : {	edithidden : true,required : true}},
			 {name : 'state',index : 'state',align : 'center',width : 30,hidden : false,editable : true,edittype : 'textarea',editoptions : {},editrules : {	edithidden : true,	required : false}},
			 {name : 'zip',index : 'zip',align : 'left',width : 30,hidden : false,editable : true,edittype : 'textarea',	editoptions : {},	editrules : {edithidden : true,required : false}},
			 {name : 'phone1',index : 'phone1',	align : 'left',	width : 50,	hidden : false,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {		edithidden : true,		required : false	}},
			 {name : 'phone2',index : 'phone2',	align : 'left',	width : 50,	hidden : true,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {		edithidden : true,		required : false	}},
			 {name : 'fax',	index : 'fax',	align : 'left',	width : 50,	hidden : false,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {		edithidden : true,		required : false	}},
			 {name:'isMailing', index:'isMailing', align:'center', width:30,hidden : true,editable:true, formatter:'checkbox', edittype:'checkbox'},
			 {name:'isShipTo', index:'isShipTo', align:'center', width:30,hidden : true,editable:true, formatter:'checkbox', edittype:'checkbox'},
			 {name:'isDefault', index:'isDefault', align:'center', width:30,hidden : true,editable:true, formatter:'checkbox', edittype:'checkbox'},
			 {name:'isRemitTo', index:'isRemitTo', align:'center',  width:30, hidden:true, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{required:false},editoptions: { value: "true:false"}}],
			recordtext : '',
			rowNum: 500,
			rowList : [],
			pgtext : null,
			viewrecords : false,
			sortname : ' ',
			sortorder : "asc",
			imgpath : 'themes/basic/images',
			caption : false,
			height : 200,
			rownumbers:true,
			altRows: true,
			altclass:'myAltRowClass',
			width : 1000,
			loadComplete : function(data) {		
				if ($('#vendorcheck').is(':checked'))
					$("#rxAddressGridCategoriesGrid").showCol("isRemitTo")
					
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
			loadError : function(jqXHR, textStatus, errorThrown) {	}
		});
		emptyMsgDiv.insertAfter(grid.parent());
		return true;
	}

function addAddressRolodexDetails()
{	
	aGlobalVariableforAddress = "add";
	$("#rolodexAddress1").val(''); $("#rolodexAddress2").val(''); $("#rolodexCity").val(''); $("#rolodexState").val('');$("#rolodexZip").val('');$("#officeextension").val('');
	$("#exchangeId").val('');$("#subscriberId").val(''); $("#areaId").val(''); 
	$("#exchangeId1").val('');$("#subscriberId1").val('');$("#areaId1").val('');
	$("#exchangeId2").val('');$("#subscriberId2").val('');$("#areaId2").val(''); 
	$("#shipAddress").attr("checked", false); $("#mailAddress").attr("checked", false);  $("#defaultAddress").attr("checked", false);  $("#remitCheckbox").attr("checked", false); 
	jQuery("#addRolodexAddress").dialog("open");
}

function editAddressRolodexDetails()
{
	
	$("#rolodexAddress1").val(''); $("#rolodexAddress2").val(''); $("#rolodexCity").val(''); $("#rolodexState").val('');$("#rolodexZip").val('');$("#officeextension").val('');
	$("#exchangeId").val('');$("#subscriberId").val(''); $("#areaId").val(''); 
	$("#exchangeId1").val('');$("#subscriberId1").val('');$("#areaId1").val('');
	$("#exchangeId2").val('');$("#subscriberId2").val('');$("#areaId2").val(''); 
	$("#shipAddress").attr("checked", false); $("#mailAddress").attr("checked", false);  $("#defaultAddress").attr("checked", false);  $("#remitCheckbox").attr("checked", false); 
	
	aGlobalVariableforAddress = "edit";
	var grid = $("#rxAddressGridCategoriesGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select a Address.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var address1 = grid.jqGrid('getCell', rowId, 'address1');
	var address2 = grid.jqGrid('getCell', rowId, 'address2');
	var city = grid.jqGrid('getCell', rowId, 'city');
	var state = grid.jqGrid('getCell', rowId, 'state');
	var zip = grid.jqGrid('getCell', rowId, 'zip');
	var phone1 = grid.jqGrid('getCell', rowId, 'phone1');
	var phone2 = grid.jqGrid('getCell', rowId, 'phone2');
	var fax = grid.jqGrid('getCell', rowId, 'fax');
	if(phone1 == null || phone1.length == 0){
	if(phone2 == null || phone2.length == 0){
	if(fax == null || fax.length == 0){
	}else{
		var areacode2 = fax.split(" ");
		var exchangecode2 = areacode2[1].split("-");
		var pinco = areacode2[0].replace("(", "");
		var pinco1 = pinco.replace(")", "");
		$("#areaId2").val(pinco1); 
		$("#exchangeId2").val(exchangecode2[0]);
		$("#subscriberId2").val(exchangecode2[1]);	
		}
	}else	{
		var areacode1 = phone2.split(" ");
		var exchangecode1 = areacode1[1].split("-");
		var pincode2 = areacode1[0].replace("(", "");
		var pincode3 = pincode2.replace(")", "");
		$("#areaId1").val(pincode3); 
		$("#exchangeId1").val(exchangecode1[0]);
		$("#subscriberId1").val(exchangecode1[1]);	
	if(fax == null || fax.length == 0){
	}else{
		var areacode2 = fax.split(" ");
		var exchangecode2 = areacode2[1].split("-");
		var pinco = areacode2[0].replace("(", "");
		var pinco1 = pinco.replace(")", "");
		$("#areaId2").val(pinco1); 
		$("#exchangeId2").val(exchangecode2[0]);
		$("#subscriberId2").val(exchangecode2[1]);	
		}
		}
	}else	{
		var areacode='';
		var exchangecode='';
		if(phone1!=="undefined"){
			areacode = phone1.split(" ");
		}
		if(typeof(areacode[1]) !=="undefined"){
			exchangecode = areacode[1].split("-");
		}
		var pincode = areacode[0].replace("(", "");
		var pincode1 = pincode.replace(")", "");
		$("#areaId").val(pincode1); 
		$("#exchangeId").val(exchangecode[0]);
		$("#subscriberId").val(exchangecode[1]);
	if(phone2 == null || phone2.length == 0){
	if(fax == null || fax.length == 0){
	}else{
		var areacode2 = fax.split(" ");
		var exchangecode2 = areacode2[1].split("-");
		var pinco = areacode2[0].replace("(", "");
		var pinco1 = pinco.replace(")", "");
		$("#areaId2").val(pinco1); 
		$("#exchangeId2").val(exchangecode2[0]);
		$("#subscriberId2").val(exchangecode2[1]);	
		}
	}else{
		
		  var areacode = phone2.split(" ");
	       if(phone2.indexOf(')') > -1)
	       {
	       var exchangecode = areacode[1].split("-");
	       var pincode = areacode[0].replace("(", "");
	       var pincode1 = pincode.replace(")", "");
	       $("#subscriberId1").val(exchangecode[1]);
	       $("#areaId1").val(pincode1); $("#exchangeId1").val(exchangecode[0]);
	       }
	      else
	       {
	       var exchangecode = phone2.split("-");
	       $("#subscriberId1").val(exchangecode[2]);
	       $("#areaId1").val(exchangecode[0]); $("#exchangeId1").val(exchangecode[1]);
	       }
		/*var areacode1 = phone2.split(" ");
		var exchangecode1 = areacode1[1].split("-");
		var pincode2 = areacode1[0].replace("(", "");
		var pincode3 = pincode2.replace(")", "");
		$("#areaId1").val(pincode3); 
		$("#exchangeId1").val(exchangecode1[0]);
		$("#subscriberId1").val(exchangecode1[1]);*/
	if(fax == null || fax.length == 0){
	}else{
		
		var areacode = fax.split(" ");
	       if(fax.indexOf(')') > -1)
	       {
	       var exchangecode = areacode[1].split("-");
	       var pincode = areacode[0].replace("(", "");
	       var pincode1 = pincode.replace(")", "");
	       $("#subscriberId2").val(exchangecode[1]);
	       $("#areaId2").val(pincode1); $("#exchangeId2").val(exchangecode[0]);
	       }
	      else
	       {
	       var exchangecode = fax.split("-");
	       $("#subscriberId2").val(exchangecode[2]);
	       $("#areaId2").val(exchangecode[0]); $("#exchangeId2").val(exchangecode[1]);
	       }
		
		/*var areacode2 = fax.split(" ");
		var exchangecode2 = areacode2[1].split("-");
		var pinco = areacode2[0].replace("(", "");
		var pinco1 = pinco.replace(")", "");
		$("#areaId2").val(pinco1); 
		$("#exchangeId2").val(exchangecode2[0]);
		$("#subscriberId2").val(exchangecode2[1]);*/	
			}	
		}
	}
		$("#rolodexAddress1").val(address1); $("#rolodexAddress2").val(address2); $("#rolodexCity").val(city); $("#rolodexState").val(state);
		$("#rolodexZip").val(zip);
		var ismailling = grid.jqGrid('getCell', rowId, 'isMailing');
		if (ismailling === 'Yes') { $("#mailAddress").attr("checked", true); }
		var isshiping=grid.jqGrid('getCell', rowId, 'isShipTo');
		if (isshiping === 'Yes') { $("#shipAddress").attr("checked", true); }
		var isdefault=grid.jqGrid('getCell', rowId, 'isDefault');
		if (isdefault === 'Yes') { $("#defaultAddress").attr("checked", true); }
		var isremitto=grid.jqGrid('getCell', rowId, 'isRemitTo');
		if (isremitto === 'true') { $("#remitCheckbox").attr("checked", true); } else { $("#remitCheckbox").removeAttr("checked");}
		jQuery("#addRolodexAddress").dialog("open");
}

function deleteAddressRolodexDetails()
{
	aGlobalVariableforAddress = "delete";
	var myGrid = $('#rxAddressGridCategoriesGrid');
	var selectedRowId = myGrid.jqGrid ('getGridParam', 'selrow');
	if(selectedRowId === null){
		var errorText = "Please select a Address.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var cellValue = myGrid.jqGrid ('getCell', selectedRowId, 'rxAddressId');
	$.ajax({
		url: "./companycontroller/newAddressaddoredit", 
		type: "POST",
		async:false,
		data : { 'rxAddressID' : cellValue ,"operation":aGlobalVariableforAddress},
		success: function(data){
			$("#rxAddressGridCategoriesGrid").trigger("reloadGrid");
		}
 	});
			
}

function savenewRolodexAddress(){
	
	var rolodexAddressForm = $("#rolodexAddressCustomForm").serialize();
	var areaCode=$("#areaId").val();
	var exchangeCode = $("#exchangeId").val();
	var subscriberNumber = $("#subscriberId").val();
	var contact1='';
	if(areaCode !== ''){
	contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	var areaCode1=$("#areaId1").val();
	var exchangeCode1 = $("#exchangeId1").val();
	var subscriberNumber1 = $("#subscriberId1").val();
	var contact2='';
	if(areaCode1 !== ''){
		contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
	}
	var areaCode2=$("#areaId2").val();
	var exchangeCode2 = $("#exchangeId2").val();
	var subscriberNumber2 = $("#subscriberId2").val();
	var fax='';
	if(areaCode2 !== ''){
		fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
	}
	var rolodexNumber = getUrlVars()["rolodexNumber"];
	var name1 = ""+ getUrlVars()["name"];
/*	var arr = name1.split("%20");
	name1 = "";
	for(var index = 0; index < arr.length; index++){
		name1 = name1 + arr[index] + " ";
	}
	name1 = name1.replace('(', ' ');
	name1 = name1.replace(')', ' ');*/
	
	
	if(aGlobalVariableforAddress === "add"){
		
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			var engineerForm = $("#rolodexAddressCustomForm").serialize();
			$.ajax({
				url: "companycontroller/newAddressaddoredit",
				type: "POST",
				async:false,
				data: rolodexAddressForm+ "&USPhoneNumber1="+ contact1 +"&USPhoneNumber2="+ contact2 +"&fax=" +fax +"&rolodexNumber="+ rolodexNumber +"&EmployeeLastName=" +name1+"&operation="+aGlobalVariableforAddress,
				success: function(data) {
					$("#rxAddressGridCategoriesGrid").trigger("reloadGrid");
				}     
			}); 
			jQuery("#addRolodexAddress").dialog("close");
			
	}else if(aGlobalVariableforAddress === "edit"){
		
			var grid = $("#rxAddressGridCategoriesGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var addressID=grid.jqGrid('getCell', rowId, 'rxAddressId');
			
			$.ajax({
					type:'POST',
					url: "companycontroller/newAddressaddoredit",
					data: rolodexAddressForm+ "&USPhoneNumber1="+ contact1 +"&USPhoneNumber2="+ contact2 +"&fax=" +fax +"&rolodexNumber="+ rolodexNumber +"&EmployeeLastName=" +name1+'&rxAddressID='+addressID+"&operation="+aGlobalVariableforAddress,
					success:function(data){
						$("#rxAddressGridCategoriesGrid").trigger("reloadGrid");
					}
				});
			jQuery("#addRolodexAddress").dialog("close");
	}
	}


function cancelAddressRolodexDetails(){
	$("#rxAddressGrid").dialog("close");
	return true;
}

function cancelAddRolodexAddress(){
	$('#rolodexAddressCustomForm').validationEngine('hideAll');
	$("#addRolodexAddress").dialog("close");
	return true;
}
