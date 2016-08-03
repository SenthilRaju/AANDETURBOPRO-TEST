var newDialogDiv = jQuery(document.createElement('div'));
var aOper;
var constJoReleaseDetailID=0;
var constCuInvoiceID=0;
var constVeBillID=0;
jQuery(document).ready(function(){
	
});

function addCostGrid(JoReleaseDetaiID,cuInvoiceID,veBillID){
	constJoReleaseDetailID = JoReleaseDetaiID;
	constCuInvoiceID = cuInvoiceID;
	constVeBillID =veBillID;
$("#gridDatas").empty();
$("#gridDatas").append("<table id='addCostGrid'></table><div id='addCostPager'></div>");
	try{
	$("#addCostGrid").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#addCostPager'),
		url:'./jobtabs5/invoiceCostList',
		postData: {'joReleaseDetailID':JoReleaseDetaiID},
		colNames: ["Date","Entered By","Reason","Cost","","","",""],
		colModel :[
	        {name:'enteredDate', index:'enteredDate', align:'center', width:40, editable:true,hidden:false,
	        	editoptions:{size:13,readonly:false,dataInit:function(element) {
	        		
	        	}},editrules:{required: false},
	        	formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
	        {name:'enteredByName', index:'enteredByName', align:'center', width:30, editable:false,hidden:false, edittype:'text', editoptions:{size:9,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'reason', index:'reason', align:'left', width:160, editable:true,hidden:false, edittype:'text', editoptions:{size:64,readonly:false},editrules:{edithidden:false,required:false}},
	        {name:'cost', index:'cost', align:'right', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:9,readonly:false},editrules:{edithidden:false,required:false}, formatter:customCurrencyFormatter},
	        {name:'joInvoiceCostID', index:'joInvoiceCostID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'joReleaseDetailID', index:'joReleaseDetailID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'veBillID', index:'veBillID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'cuInvoiceID', index:'cuInvoiceID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}}
	        ],
	        rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
			sortname: 'enteredDate', sortorder: "asc", imgpath: 'themes/basic/images',
			width: 950, rownumbers:true, altRows: true, altclass:'myAltRowClass',caption:null,
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
			loadComplete: function(data) {},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){},
			onCellSelect : function (rowid,iCol, cellcontent, e) {},
			editurl:"./jobtabs5/manipulateInvoiceCostList"
	 }).navGrid("#addCostPager", {
			add : false,
			edit : false,
			del : true,
			alertzIndex : 10000,
			search : false,
			refresh : false,
			pager : true,
			alertcap : "Warning",
			alerttext : 'Please select a Line'
		},
		// -----------------------edit// options----------------------//
		{},
		// -----------------------add options----------------------//
		{},
		// -----------------------Delete options----------------------//
		{
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:12345,
			caption: "Delete Cost Detail",
			msg: 'Delete the Cost Record?',

			onclickSubmit: function(params){
				var id = jQuery("#addCostGrid").jqGrid('getGridParam','selrow');
				var key = jQuery("#addCostGrid").getCell(id, 5);
				return { 'joInvoiceCostID' : key};
				}/*,
			afterSubmit:function(response,postData){
				$("#addCostGrid").trigger("reloadGrid");
			}*/
		});
	 $("#addCostGrid").jqGrid(
				'inlineNav',
				'#addCostPager',
				{
					edit : true,
					add : true,
					zIndex : 10000,
					refresh : false,
					cloneToTop : true,
					alertzIndex : 10000,
					
					addParams : {
						position: "last",
						addRowParams : {
							keys : false,
							oneditfunc : function() {
								$("#new_row_enteredDate").datepicker();
								var date = new Date();
								var CreatedOn = date.getDate();
								var createdMonth = date.getMonth()+1; 
								var createdYear = date.getFullYear();
								if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
								if(createdMonth<10){createdMonth='0'+createdMonth;} 
								createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
								$("#new_row_enteredDate").val(createdDate);
								
								$('#new_row_joReleaseDetailID').val(constJoReleaseDetailID);
								$('#new_row_veBillID').val(constVeBillID);
								$('#new_row_cuInvoiceID').val(constCuInvoiceID);
							},
							successfunc : function(response) {
								$('#addCostMsg').css('display','block');
								$('#addCostMsg').html('<font color="green">Saved Successfully</font>');
								
								$("#addCostGrid").trigger("reloadGrid");
								
								return true;
							},
							aftersavefunc : function(response) {
								setTimeout(function(){
									$('#addCostMsg').html("");
									},2000);
								
								$("#addCostGrid").trigger("reloadGrid");
								return true;
							},
							errorfunc : function(rowid, response) {
								$("#addCostGrid").trigger("reloadGrid");
								return false;
							},
							afterrestorefunc : function(rowid) {
								console.log("afterrestorefunc");
							}
						}
					},
					editParams : {
						keys : false,
						oneditfunc : function(id) {
							//$('#'+rowid+'_joReleaseDetailID').val(constJoReleaseDetailID);
							$("#"+id+"_enteredDate").datepicker();
							var date = new Date();
							var CreatedOn = date.getDate();
							var createdMonth = date.getMonth()+1; 
							var createdYear = date.getFullYear();
							if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
							if(createdMonth<10){createdMonth='0'+createdMonth;} 
							createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
							//$("#"+id+"_enteredDate").val(createdDate);
							console.log('OnEditfunc');
						},
						successfunc : function(response) {
							$('#addCostMsg').css('display','block');
							$('#addCostMsg').html('<font color="green">Saved Successfully</font>');
							return true;
						},
						aftersavefunc : function(id) {
							console.log('afterSavefunc editparams');
											
							setTimeout(function(){
								$('#addCostMsg').html("");
								},2000);
							
							 $("#addCostGrid").trigger("reloadGrid");
							 return true;
						},
						errorfunc : function(rowid, response) {
							console.log(' editParams -->>>> An Error');
							$("#addCostGrid").trigger("reload");
							return false;

						},
						afterrestorefunc : function( id ) {
					    }
						
					}
	});
	}
	catch(err) {
		alert(err.message)
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
        
    }
}

function applyAddCost(JoReleaseDetaiID,cuInvoiceID,veBillID){
	var comStatus = 0;
	$.ajax({
		url: "./jobtabs5/CheckCommissionPaidorNot",
		type: "POST",
		data : "joReleaseDetailID="+JoReleaseDetaiID,
		success: function(data) {
			comStatus = data;
		}
	});
	
	if(comStatus==0){
		addCostGrid(JoReleaseDetaiID,cuInvoiceID,veBillID);
		$('.ui-dialog-titlebar-close').css('display','none');
		jQuery("#addCost").dialog("open");
	}else{
	jQuery(newDialogDiv).html("<span><b>Commission already calculated Can't add Cost to this Invoice </b></span>");
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
	buttons:{
		"OK": function(){
			jQuery(this).dialog("close");
		}
	}
	});
}
}
function addCostClose(){
	if(constCuInvoiceID==null){
		constCuInvoiceID="";
	}
	if(constVeBillID==null){
		constVeBillID="";
	}
	//alert("constVeBillID::"+constVeBillID);
	$.ajax({
		url: "./jobtabs5/addInvoiceCost",
		type: "POST",
		data : "joReleaseDetailID="+constJoReleaseDetailID+"&cuInvoiceID="+constCuInvoiceID+"&veBillID="+constVeBillID,
		success: function(data) {
			$("#shiping").trigger("reloadGrid");
			$("#release").trigger("reloadGrid");
			jQuery( "#addCost" ).dialog( "close" );
		}
	});
	//loadBillingEntireJob();
	return false;
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function FormatDate(cellValue, options, rowObject){
	console.log(cellValue);
	var createdDate ='';
	var date = new Date(cellValue);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	return createdDate;
}

jQuery(function () {
	jQuery( "#addCost" ).dialog({
		autoOpen: false,
		closeOnEscape: false,
		width: 970,
		title:"Added Cost to Invoice",
		modal: true,	
		buttons:{} 
	});
});


		

