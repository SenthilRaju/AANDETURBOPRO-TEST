var stCustomer, endCustomer, Query;
var newDialogDiv = jQuery(document.createElement('div'));
var displayName = '';
var data1 = '';
var email = '';
var rxMasterID = '';
var stIndex=0;
var endIndex=0;
//var rxMasterIds = [];

jQuery(document).ready(function() {
	$("#search").hide();
	$('#statementDate').datepicker().datepicker("setDate", new Date());
	$("#exclusionDate").datepicker().datepicker("setDate", new Date());
	$('#emailSubmit').click(function() {
		email = $('#mailId').val();
		setTimeout($.unblockUI, 100);
		emailStatement(null);
	});
	$('#cancelSubmit').click(function() {
		setTimeout($.unblockUI, 100);
	});
	
	$('#stCustomerID').change(function(){
		
		$('#endCustomerID').val($('#stCustomerID option:selected').val());
	})
	
	
	
});

function openUrl(url) {
	window.open(url);
	setTimeout('', 30000);
}
$('#stCustomerID').on('change', function() {
	stCustomer = $(this).find('option:selected').text();
	stIndex = $("#stCustomerID")[0].selectedIndex;
});
$('#endCustomerID').on('change', function() {
	endCustomer = $(this).find('option:selected').text();
	endIndex = $("#endCustomerID")[0].selectedIndex;
});

jQuery( function(){
	jQuery("#emailDetailsDialog").dialog({
		autoOpen:false,
		height:500,
		width:1220,
		//title:"Batch Invoice Report",
		modal:true,
		close:function(){
			clearCuStatementInput();
			$("#cStatementEmailGrid").jqGrid("GridUnload")
			$("#cStatementEmailGrid").trigger("reloadGrid");
			return true;
		}
	});
});

function sendBulkEmail()
{

	jQuery("#emailDetailsDialog").dialog("open");	
	
	endIndex = stIndex;
	var newDialogDiv = jQuery(document.createElement('div'));
	var startingid = $('#stCustomerID').val();
	var endingid = $('#endCustomerID').val();
	var rxMasterIds = [];
	for(var i =parseInt(stIndex);i<=parseInt(endIndex);i++)
	{
		if($("#stCustomerID option").eq(parseInt(i)).val()!==''){
			rxMasterIds.push($("#stCustomerID option").eq(parseInt(i)).val()); 
		}
	}
	var daterange = $('#exclusionDate').val();
	var isCredit=0;

	if($('#warehouseInactive').is(":checked"))
		{
		isCredit =1;
		}
	console.log("isCredit::"+isCredit)
	if(Number(startingid)>Number(endingid)){
		var temp =startingid;
		startingid =endingid;
		endingid=temp;
	}
	$("#rxMasterIds").val(rxMasterIds.join());
	
	var rxMasterID=$("#rxMasterIds").val();
	console.log(rxMasterID)
	$("#cStatementEmailGrid").jqGrid({
		url:'./cStatementEmailListGrid',
		postData:{
			"rxMasterIds":rxMasterID,
			"exclusionDate":daterange,
			"warehouseInactive":isCredit,
		},
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#cStatementEmailGridPager'),
		colNames:['<input type="checkbox"  value=false>','Sent Date','Invoice Number','Customer','Email','','',''],
		colModel :[
		           	{name:'emailCheckBox',index:'emailCheckBox', align:'center',width:20,editable:false, hidden:false,  formatter:emailCheckBoxFormatter,editrules:{required:false}, editoptions:{size:10}},
		           	{name:'sentstatementEmailDate', index:'sentstatementEmailDate', align:'center', width:133,hidden:false,
						cellattr: function (rowId, tv, rawObject, cm, rdata) {
							if(rawObject['statementEmailStatus']==2)
							return 'style="color: red" ';}
						},
		           	{name:'invoiceNumber', index:'invoiceNumber', align:'left', width:128,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		           	{name:'quickJobName', index:'quickJobName', align:'left', width:323, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
		          	{name:'emailList',index:'emailList',width:100,hidden:false,editable:true,formatter:getCustomerEmailDetails },
		           	{name:'cuInvoiceId', index:'cuInvoiceId', align:'left', width:88, editable:true,hidden:true},
					{name:'rxCustomerId', index:'rxCustomerId', align:'', width:40,hidden:true},
					{name:'statementEmailStatus',index:'statementEmailStatus',align:'',hidden:true}
					],
		rowNum: 10000,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#cStatementEmailGridPager',
		sortname: 'createdOn', sortorder: "desc",	imgpath: 'themes/basic/images',	
		caption: 'Invoices to be Sent',
		//autowidth: true,
		height:345,	width: 1180,scrollOffset:0, rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadBeforeSend: function(xhr) {
		},
		loadComplete: function(data) {
			//$('#cInvoiceEmailGrid').setColProp('emailList', { editoptions: {} });
	    },
	    gridComplete:function(){
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
    	ondblClickRow: function(rowid) {
			
    	}
	}).navGrid('#cStatementEmailGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
	
	//document.getElementById("emailDetailsFormId").reset();
}
function emailCheckBoxFormatter(cellValue, options, rowObject){
	
	var id="emailCheckBox_"+options.rowId;
	var element = '';
	element = "<input type='checkbox' id='"+id+"'>";
   
return element;
}

function cancelEmail(){
	
	clearCuStatementInput();	
	jQuery("#emailDetailsDialog").dialog("close");	
	$("#cStatementEmailGrid").jqGrid("GridUnload")
	$("#cStatementEmailGrid").trigger("reloadGrid");
}


function sendEmail(){
	
	var rxMasterID=$("#rxMasterIds").val();
	var daterange = $('#exclusionDate').val();
	var isCredit=0;
	
	 var ids = $("#cStatementEmailGrid").jqGrid('getDataIDs'); 
	 var newDialogDiv = jQuery(document.createElement('div'));
		  var cuInvoiceIdArray=new Array();
		  var rxCustomerIdArray=new Array();
		  var emailListArray=new Array();
	 
	 var valid=false;
	 for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		 var id="#emailCheckBox_"+selectedRowId;
		 var selID="#emailSelectBox_"+selectedRowId;
		
		 var canDo=$(id).is(':checked');
		// cellValue =$("#cInvoiceEmailGrid").jqGrid ('getCell', selectedRowId, 'emailList');
		 //countOptionTag="select"+selID+" option";
		var selVal=$(selID).val();
		 
		// var count= $(countOptionTag).length;
		if(canDo && selVal ==-1 ){
			valid=true;
			break;
			}
	 }
	 
	 if(valid)
		 {
		 jQuery(newDialogDiv).html('<span><b style="color:red;"> Should Select Email ID</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		 }
	 else
		 {
		 
		 $('#loadingDivForBulkEmail').css({
				"display": "block"
			}); 
	    	 var idList = $("#cStatementEmailGrid").jqGrid('getDataIDs'); 
			 for(var j=0;j<idList.length;j++){
				 var selectedRowId=idList[j];
				 var id1="#emailCheckBox_"+selectedRowId;
				 var selID="#emailSelectBox_"+selectedRowId;
				 var canDo=$(id1).is(':checked');
				 if(canDo){
					 
				   var cuInvoiceId =$("#cStatementEmailGrid").jqGrid ('getCell', selectedRowId, 'cuInvoiceId');
				   console.log("cuInvoiceId"+cuInvoiceId);
				   cuInvoiceIdArray.push(cuInvoiceId);
				   var rxCustomerId=$("#cStatementEmailGrid").jqGrid ('getCell', selectedRowId, 'rxCustomerId');
				   console.log("rxCustomerId"+rxCustomerId);
				   rxCustomerIdArray.push(rxCustomerId);
				   var emailList= $(selID).val();
				   console.log("emailList"+emailList);
				   emailListArray.push(emailList);
				 }
			 }
		  
		 
		 
			$.ajax({
				url: "./customerList/getStatementPDF",
				type: "POST",
				data : 
					{
					"rxMasterIds":rxMasterID,
					"exclusionDate":daterange,
					"warehouseInactive":isCredit,
					"cuInvoiceIdArray":cuInvoiceIdArray,
					"rxCustomerIdArray":rxCustomerIdArray,
					"emailListArray":emailListArray,
					},

				success: function(data) {
					$("#cStatementEmailGrid").trigger("reloadGrid");
					$('#loadingDivForBulkEmail').css({
							"display": "none"
						}); 
					//jQuery("#InvoiceLineItemNote").dialog("close");
					
				}
				});
		 
		 }
}




function selectAllCheckBox()
{
	var selectButtonVal=$('#selectCheckBox').val();
	console.log("selectButtonVal"+selectButtonVal);
	var bool='';
	if(selectButtonVal=="Select All"){
		bool=true;
		$('#selectCheckBox').prop('value','Un Select');
	}else{
		bool=false;
		$('#selectCheckBox').prop('value','Select All');
	}
	console.log("Bool"+bool);
	var ids = $("#cStatementEmailGrid").jqGrid('getDataIDs');
	
	
	 for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		 console.log(bool);
	//	 console.log("Selected Row Id",selectedRowId);
		 var id="#emailCheckBox_"+selectedRowId;
		 $(id).prop('checked',bool);
	 }
	
}
function getCustomerEmailDetails(cellValue, options, rowObject)
{
	var id="emailSelectBox_"+options.rowId;
	
	var selectBox='';
		selectBox="<select id='"+id+"'><option value=-1>-----SELECT-----</option>" +cellValue+"</select>";	
		
	return selectBox;
}


function clearCuStatementInput()
{
	$('#stCustomerID').val("");
	$('#endCustomerID').val("");
	$("#rxMasterIds").val("");
/*	$("#exclusionDate").val("");
	$("#statementDate").val("");*/
}
function printTrigger(elementId) {
	var getMyFrame = document.getElementById(elementId);
	getMyFrame.focus();
	getMyFrame.contentWindow.print();
}
function emailStatement(obj) {
	var statementDate = $('#statementDate').val();
	if (obj != null) {
		var emailAndrxMasterID = obj.id;
		var split = emailAndrxMasterID.split("rxMasterID=");
		email = split[0];
		rxMasterID = split[1];
	}
	if (email == '' || email == null) {
		$.blockUI({
			message : $('#emailForm')
		});
	}
	if (email != null || email != '') {
		$
				.ajax({
					url : './customerList/emailStatement?filename='
							+ rxMasterID + '_statements.pdf&rxMasterID='
							+ rxMasterID + "&toEmailaddress=" + email
							+ "&StatementDate=" + statementDate,
					type : "GET",
					success : function(data) {
						filePath = data;
						msgOnAjax("Generated the PDF", "green", 2000);
						setTimeout(
								'msgOnAjax("email Sent successfully", "green", 2000)',
								3000);
					},
					error : function(data) {
						msgOnAjax(
								"Unable to send the mail. Please check the email server port and server host names.",
								"red", 4000);
					}
				});
	}
}