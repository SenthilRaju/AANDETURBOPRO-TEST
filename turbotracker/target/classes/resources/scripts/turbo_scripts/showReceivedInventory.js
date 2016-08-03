var _globalvarReceiveInvgrid;

jQuery(document).ready(function() {
	
	loadLineItemGrid();
	
	});

$( "#receivedInventorydate" ).datepicker();
var grid;
var data = "${vendorName}";
var recievedPrev;
var prodId = getParameterByName('vePOID');
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var globalrowid=0;

function loadLineItemGrid() {
var _globalValue = 0;
	grid = $("#lineItemGrid"),
	 getColumnIndex = function (columnName) {
        var cm = $(this).jqGrid('getGridParam', 'colModel'), i, l = cm.length;
        for (i = 0; i < l; i++) {
            if ((cm[i].index || cm[i].name) === columnName) {
                return i; // return the colModel index
            }
        }
        return -1;
    };
$("#lineItemGrid").trigger("reloadGrid");
 $('#lineItemGrid').jqGrid({
	datatype: 'JSON',
	mtype: 'POST',
	pager: jQuery('#lineItemPager'),
	url:'./jobtabs5/jobReleaseLineItemForrecieveInventory',
	postData: {vePoId :function(){var vePOID=prodId;if(vePOID==null || vePOID==''){	vePOID=0;}return vePOID;} },
	colNames:['Product No', 'Description','Ordered Qty','Received Quantity','Difference','Amount', 'VepoID', 'prMasterID' , 'vePodetailID'],
	colModel :[
		{name:'note',index:'note',align:'left',width:60,editable:false,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false,required: true}},
       	{name:'description', index:'description', align:'left', width:80, editable:false,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
		{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:50,hidden:false, editable:false, editoptions:{readOnly:true,size:5, alignText:'left',maxlength:7},editrules:{number:true,required: true}},
		{name:'quantityReceived', index:'quantityReceived', align:'center', width:50,hidden:false, editable:true, editoptions:{size:5, alignText:'right',maxlength:7},editrules:{number:true,required: true}},
		{name:'difference', index:'difference', align:'center', width:50,hidden:false, editable:false, editoptions:{readOnly:true,size:5, alignText:'left',maxlength:7},editrules:{number:true,required: true}},
		{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
		{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
		{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}}
		],
	rowNum: 0, pgbuttons: false, recordtext: '', cellEdit: true,
	rowList: [], pgtext: null, viewrecords: false,
	sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
	height:210,	width: 770, hoverrows:false, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
	scrollrows : true,
	multiselect: false,
	beforeEditCell:function(rowid,cellname,value,iRow,iCol) {
		_globalValue = value;
	},
	beforeSaveCell:function(rowid,cellname,value,iRow,iCol) {
		var rowData = jQuery("#lineItemGrid").getRowData(rowid);
		var receivedQuantity =$("#"+rowid+"_quantityReceived").val();
		var quantityOrdered = rowData['quantityOrdered'];
		if(Number(quantityOrdered)<Number(receivedQuantity)){
			var newDialogDiv = jQuery(document.createElement('div'));
		    jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Received Quantity not exceeds ordered quantity.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
			
			$this.jqGrid('restoreCell', iRow, iCol, true);
		}
	},
	afterSaveCell : function(rowid,name,val,iRow,iCol) {
		if(name == 'quantityReceived') {
			var quantBill = jQuery("#lineItemGrid").jqGrid('getCell',rowid,iCol-1);
			var quantRece = jQuery("#lineItemGrid").jqGrid('getCell',rowid,iCol);
			var rowData = $('#lineItemGrid').jqGrid('getRowData', rowid);
			rowData.difference = parseFloat(quantBill)-parseFloat(quantRece);
			$('#lineItemGrid').jqGrid('setRowData', rowid, rowData);
		}
	},
	loadComplete: function(data) {
		_globalvarReceiveInvgrid = JSON.stringify( $('#lineItemGrid').jqGrid('getRowData'));
		
	},
	gridComplete: function () {
	},
	loadError : function (jqXHR, textStatus, errorThrown){	},
	onSelectRow: function(id){
	$("#lineItemGrid").jqGrid('resetSelection');
	},
	ondblClickRow: function(id){},
	alertzIndex:1050,
	cellsubmit: 'clientArray',
	editurl: 'clientArray',
	});
	
}


function generateReceivedInventoryXl() {
	window.open('./jobtabs5/generateReceivedInventoryPDF?vePoId='+prodId);
	return false;
}


function formatCurrencynodollar(strValue)
{
	if(strValue === "" || strValue == null){
		return "0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10){
		strCents = "0" + strCents;
	}
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	}
	return (((blnSign)?'':'-') + dblValue + '.' + strCents);
}
function saveReceivedInventory(){
	var vepoid = getParameterByName('vePOID');
	var gridRows = $('#lineItemGrid').getRowData();
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	var dataToSend = JSON.stringify(gridRows);
	var recDate = $('#receivedInventorydate').val();
	var veReceiveID =$("#veReceivedID").val();
	
	additionalInfo="&recDate="+recDate+"&veReceiveID="+veReceiveID+"&vePOID="+vepoid;
	
	$.ajax({
		url: "./rolodexforms/updateReceiveAllInventory?"+additionalInfo,
		type: "POST",
		data : {"gridData":dataToSend},
		beforeSend: function(d){
			$("#LoadingDialog").show();
		},
		success: function(data) {
			
			createtpusage('Receive Inventory','Receive Inventory ALL','Info','Receive Inventory,Receive Inventory All,receivedInventorydate:'+recDate);
			document.location.href="./showReceivedInventory?vePOID="+vepoid+"&veReceiveID="+data;
			$("#veReceivedID").val(data);
			$("#LoadingDialog").hide();
			//location.reload();
			
		}
	});
}
	
	function closeReceivedInventory(){
		
		var varReceiveInvgrid = JSON.stringify( $('#lineItemGrid').jqGrid('getRowData'));
		if(varReceiveInvgrid != _globalvarReceiveInvgrid){
		
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Are you sure you want to save this thing into the database??</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
				buttons: [{text: "Yes",click: function() { saveandcloseReceivedInventory(); $(this).dialog("close");}},
		          {text: "No",click: function() {document.location.href="./showReceivedInventoryList?vendorID=0&sortBy=0&rangeFrom=to";	 $(this).dialog("close");}}]
			}).dialog("open");
		}else{
			document.location.href="./showReceivedInventoryList?vendorID=0&sortBy=0&rangeFrom=to";
		}
			
	}
	
	
	function saveandcloseReceivedInventory(){
		
		var gridRows = $('#lineItemGrid').getRowData();
		var vepoid = getParameterByName('vePOID');
		var recDate = $('#receivedInventorydate').val();
		
		var rowData = new Array();
		for (var i = 0; i < gridRows.length; i++) {
			var row = gridRows[i];
			rowData.push($.param(row));
			}
		var dataToSend = JSON.stringify(gridRows);
		var recDate = $('#receivedInventorydate').val();
		var veReceiveID =$("#veReceivedID").val();
		
		additionalInfo="&recDate="+recDate+"&veReceiveID="+veReceiveID+"&vePOID="+vepoid;
		
		$.ajax({
			url: "./rolodexforms/updateReceiveAllInventory?"+additionalInfo,
			type: "POST",
			data : {"gridData":dataToSend},
			success: function(data) {
				
				createtpusage('Receive Inventory','Receive Inventory ALL','Info','Receive Inventory,Receive Inventory All,receivedInventorydate:'+recDate);
				document.location.href="./showReceivedInventoryList?vendorID=0&sortBy=0&rangeFrom=to";	
			//	location.reload();
				
			}
		});
		
	}
	/*
	var recDate = $('#receivedInventorydate').val();
	var vepoid = getParameterByName('vePOID');
	
	if(recDate!=''){
		var dateUpdate="receivedDate="+recDate+"&vePOId="+vepoid+"&veReceiveID="+veReceiveID;
		 $.ajax({
				url: "rolodexforms/updateReceiveInventoryDate",
				type : 'POST',
				data: dateUpdate,
				success: function (data){
//					console.log(JSON.stringify(data));
					createtpusage('Receive Inventory','Receive Inventory Save','Info','Receive Inventory,Saving Receive Inventory,prMasterId:'+prMasterId+',vePoid:'+vePoid);
					document.location.href="./showReceivedInventoryList?vendorID=0&sortBy=0&rangeFrom=to";		
					
				}
				});
	}*/

/*
function saveReceiveAllInventory(){
	var gridRows = $('#lineItemGrid').getRowData();
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	var dataToSend = JSON.stringify(gridRows);
	var recDate = $('#receivedInventorydate').val();
	var veReceiveID =$("#veReceivedID").val();
	
	additionalInfo="&recDate="+recDate+"&gridData="+dataToSend+"&veReceiveID="+veReceiveID;
	
	$.ajax({
		url: "./rolodexforms/updateReceiveAllInventory",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			createtpusage('Receive Inventory','Receive Inventory ALL','Info','Receive Inventory,Receive Inventory All,receivedInventorydate:'+recDate);
			$("#veReceivedID").val(data);
			$("#lineItemGrid").trigger("reloadGrid");
			//location.reload();
		}
	});
}*/

function saveReceiveAllInventory(){
	
		var ids = $('#lineItemGrid').jqGrid('getDataIDs');
		var recqty = 0;
		var diff = 0;
	    for (var i = 0; i < ids.length; i++) 
	    {
	    	recqty = $('#lineItemGrid').jqGrid('getCell', ids[i], 'quantityOrdered'); 
	    	$("#lineItemGrid").jqGrid('setCell',ids[i],"quantityReceived",recqty);
	    	$("#lineItemGrid").jqGrid('setCell',ids[i],"difference","0");
	    }
	
}

jQuery(function() {
	jQuery("#priorpopup").dialog({
		autoOpen : false,
		height : 270,
		width : 250,
		title : "Partial Receipts",
		modal : true,
		close : function() {
			// $('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});

function priorReceivedInventory()
{
	var vepoid = getParameterByName('vePOID');
	$.ajax({
		url: "./rolodexforms/getAllpriorreceivedinventory",
		type: "POST",
		data : {"vePOId":vepoid},
		success: function(data) {
			$("#priortable").html("");
			$("#priortable").append("<tr><th colspan = '2' style ='border:1px solid #99CCFF; background-color:#99CCFF; color:white' align='center'>Date Received</th></tr>");
			
			for(var i=0;i<data.length;i++)
				{
				if(data[i].receiveDate!=null)
				{
				var d = new Date(data[i].receiveDate);
				$("#priortable").append("<tr><td style ='font-size:14px'>"+UsDateFormate(d)+"</td><td><img alt='search' src='./../resources/scripts/jquery-autocomplete/search.png' style ='width:15px;' title='Prior Inventory' onclick='priorinventory("+data[i].veReceiveId+");'></td><tr>");
				}
				}
			jQuery("#priorpopup").dialog("open");
		}
	});
	return true;
}

function UsDateFormate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	return createdDate;
}

function priorinventory(veReceiveID)
{
	window.open('./jobtabs5/generatepriorReceivedInventoryPDF?vePoId='+prodId+'&veReceiveId='+veReceiveID);
	return false;
}
