/*jQuery(document).ready(function(){
	var projectUserID = $("#projectUserID").val();	
	getSalesReps(projectUserID);
	//$("input:button").button();
	$("#intro").hide();
	$('#search').hide();
	//setTimeout("loadgrid()", 1000);
});*/
var customerName ='';
var projectUserID =0;
var localvalues= [{id:"1",viewsname:"Accounts Receivable"}];
jQuery(document).ready(function(){
	projectUserID = $("#projectUserID").val();
	getSalesReps(projectUserID);
	
	var selectedId  = $('#SalesRepComboList').val();
	//$("input:button").button();
	$("#intro").hide();
	$('#search').hide();
	
	
	loadContactGrid();
	loadViewsGrid();
	loadcreditHoldGrid();
	$('#customersGridpager #pg_customersGridpager .ui-pg-table').css("display","block");
	
	/****
	 * Adding button to Jqgrid header
	 */
	//$('#gbox_CustomerMarginGird .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button">Open</button></div>');
	$('#gview_CreditHoldGrid .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button">Open</button></div>');
	//$('#gbox_SalesPurchaseOrdersGrid .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button" onclick="loadPoSoPopup()>Open</button></div>');
	//$('#gbox_openJobsGrid .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button" onclick="openJobsDialog()">Open</button></div>');	
	//$("input:text").addClass("ui-state-default ui-corner-all");
	
	
    $.ajax({
		url:"./employeeCrud/getSysvariableDetail?sysVariableValue=AllowEmployeestoviewtheirownCommissionStatements",
		type : "GET",
		async:false,
		success : function(data) {
			console.log('SysVariable Data:'+data.sysVariableId+' '+data.valueString+'  '+data.valueLong);
			if(data.valueLong==1){
				localvalues.push({id:'2',viewsname:'Commission Statement'});
			}
		}
	});

	 $("#viewsGrid").jqGrid('clearGridData');
	 for(var ik=0;ik<=localvalues.length;ik++) {   
		 $("#viewsGrid").jqGrid('addRowData',ik+1,localvalues[ik]);
	 }

	
	
/*     jQuery( "#AccountReceivableDiv" ).dialog({
		autoOpen: false,
		width: 1000,
		title:"AccountReceivable",
		modal: true,
		buttons:{
			"OK": function () {
				$(this).dialog("close");
			}
		},
		close: function () {
			return true;
		}
	});
     */
	 
	
	 
     jQuery( "#AccountReceivableDiv" ).dialog({
  		autoOpen: false,
  		width: 980,
  		title:"",
  		modal: true,
  		create: function (event, ui) {
		var divid="#AccountReceivableDiv";
		$("#AccountReceivableDiv").parent().find(".ui-dialog-titlebar").addClass("myARClass1");
		$(".myARClass1").html("<span style ='color:white; float:left;font-size:15px'>AccountReceivable</span><span style ='color:white; float:right;'>As Of : <input type='text' id='asofardate' onclick='setfocusforardetails(3)' style='width:100px;text-align:center'><span style='cursor:pointer;padding-left: 10px;color:white;z-index:1000' id='closear'>X</span></span>");
  		},
  		buttons:{
  		
  		},
  		close: function () {
  			return true;
  		}
  	});
     $('#asofardate').datepicker().datepicker("setDate", new Date());     
    
     $(function(){
    	$("#closear").click(function(){
    		jQuery( "#AccountReceivableDiv" ).dialog("close");
    	})
     })
     
      
      
     jQuery( "#CommissionStatementDiv" ).dialog({
    	modal: true,
    	autoOpen: false,
 		width: 1000,
 		//draggable: false,
 		title:"Commission Statements",
 		create: function (event, ui) {

     
 			var divid="#CommissionStatementDiv";
 			$("#CommissionStatementDiv").parent().find(".ui-dialog-titlebar").addClass("myCommissionClass");
 			$(".myCommissionClass").html("<span>Commission Statements</span><span style ='color:white; float:right;'><span id ='commissiondateid'> Period Ending : <select id='endperioddate' name='enperioddate' onclick='setFocusCommissionStatement()' onchange='loadCommissionStatement(this.value)'><option value='0'> -Select- </option></select></span> <span style='cursor:pointer;padding-left: 10px;color:white;z-index:1000' id='closeCommission'>X</span></span>");
 			
 			loadCurperiodID();
 			loadAllDates();	
 		}
 		
 	});
     
     $(function(){
    	 jQuery('#commissiondateid').mouseover(function(){
    		 $("#CommissionStatementDiv").dialog('option', 'draggable', false);
    		});
    	 jQuery('#commissiondateid').mouseout(function(){
    		 $("#CommissionStatementDiv").dialog('option', 'draggable', true);
    		});
    	 
     	$("#closeCommission").click(function(){
     		jQuery( "#CommissionStatementDiv" ).dialog("close");
     	})
     	

      });
      

      
     var currentPeriodID=0;
      function loadCurperiodID(){
    		$.ajax({
    			url: "./employeeCrud/getEndingPeriod",
    			type: "POST",
    			success: function(data){
    				currentPeriodID =data;
    				console.log('Current PeriodID:'+currentPeriodID);
    			}
    	 	});
    	}
      
      function loadAllDates(){
    		
    		$.ajax({
    	        url: './employeeCrud/getEndingPeriodList',
    	        type: 'POST',       
    	        success: function (data) {
    	        	console.log("all--->"+data);
    	        	checkRefs=" Period Ending : <select  id='endperioddate' style='z-index:1005;' name='enperioddatename' onchange='loadCommissionStatement(this.value)' ><option value='0'> -Select- </option>";
    				$.each(data, function(key, valueMap) {								
    						if("endingPeriodList" == key)
    						{
    							
    							$.each(valueMap, function(index, value){
    								if(value.ecPeriodId != '')
    									if(value.ecPeriodId==currentPeriodID){
    										checkRefs+='<option value="'+value.ecPeriodId+'" selected="selected">'+value.endDate+'</option>';
    										
    									}else{
    										checkRefs+='<option value='+value.ecPeriodId+'>'+value.endDate+'</option>';
    									}
    							
    							}); 
    						}
    												
    				});
    				//$("#LoadingImage").hide();
    				checkRefs += "</select>"
    				//$('#endperioddate').html(checkRefs);
    				$('#commissiondateid').empty();
    				$('#commissiondateid').append(checkRefs);
    	        }
    	    });
    	}
      
      
     jQuery( "#ARDetailsDiv" ).dialog({
 		autoOpen: false,
 		width: 850,
 		title:"",
 		modal: true,
 		create: function (event, ui) {
			 //dialogClass: 'myTitleClass';
     var divid="#ARDetailsDiv";
	$("#ARDetailsDiv").parent().find(".ui-dialog-titlebar").addClass("mySpecialClass1");
	//$(".mySpecialClass1").html("<span>As Of :<input type='text' id='asofardetailsdate' onclick='setfocusforardetails(1)' >Customer Name<input type='text' id='ardetailcustomername' onclick='setfocusforardetails(2)'><input type='button' value='change' onclick='changeardetailsgrid()'></span><span style='float:right; cursor:pointer;padding-left: 10px;' id='ardetailcloseIcon' onclick='closepopupicons()'>X</span>");
 		},
 		buttons:{
 			/*"OK": function () {
 				$(this).dialog("close");
 			}*/
 		},
 		close: function () {
 			return true;
 		}
 	});
     $('#asofardetailsdate').datepicker();
     
     jQuery("#cusinvoicetab").dialog({
			autoOpen:false,
			width:850,
			title:"Customer Invoice",
			modal:true,
			close:function(){
				return true;
			}
		});
     
     
     $("#asofardate").change(function(){
    	 $("#AccountReceivableGrid").jqGrid('GridUnload');
    	 loadAccountReceivable();
    	 $("#AccountReceivableGrid").trigger('reload');
    	 
     })
          
});


function changeardetailsgrid(){
	var rowId=jQuery("#AccountReceivableGrid").jqGrid('getGridParam', 'selrow');
	var row=jQuery("#AccountReceivableGrid").getRowData(rowId);
	var rxMasterId =row['rxMasterId'];
	var ardate=$("#asofardate").val();
	$("#ARDetailsGrid").jqGrid('GridUnload');
	loadARDetails(rxMasterId,ardate)
}
function setfocusforardetails(id){
	if(id == 1){
		$('#asofardetailsdate').focus();	
	}else if(id==2){
		$('#ardetailcustomername').focus();
	}
	else if(id = 3)
		{
		$('#asofardate').focus();	
		}
}
function closepopupicons(){
	jQuery( "#ARDetailsDiv" ).dialog("close");
}

function loadAccountReceivable(){
	var selectedUser = $('#SalesRepComboList').val();
	var customerIDs = 0;
	if($('#customerName').val()!=''){
		customerIDs  = $('#customerId').val();
	}
	var ardate=$("#asofardate").val();
	
	
	$("#AccountReceivableGrid").jqGrid({
		datatype: 'json',
		url: "./projectscontroller/getCustomerAccountRecieveDetails?tsUserLoginID="+selectedUser+"&customerID="+customerIDs+"&asofardate="+ardate,
		mtype: 'GET',
		pager: jQuery('#accountReceivablepager'),
		colNames:['rxMasterId','Customer','Current','30 Days','60 Days','90 Days','Total'],
		colModel :[
           	{name:'rxMasterId', index:'rxMasterId', align:'left', width:40, editable:true,hidden:true},
			{name:'customerName', index:'customerName', align:'', width:200,hidden:false},
			{name:'currentAmt', index:'currentAmt', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			{name:'thirtyDays', index:'thirtyDays', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			{name:'sixtyDays', index:'sixtyDays', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			{name:'ninetyDays', index:'ninetyDays', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			{name:'totalDaysAmt', index:'totalDaysAmt', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			],
		rowNum: 100000,
		//rowList: [500,1000],        // disable page size dropdown
	    pgbuttons: true,     // disable page control like next, back button
//	    pgtext: null,         // disable pager text like 'Page 0 of 10'
	    viewrecords: false,
		recordtext: '',
		pager: '#accountReceivablepager',
		sortname: 'customerName', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
		//autowidth: true,
		height:300,	width: 950,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var row=jQuery("#AccountReceivableGrid").getRowData(rowId);
			var rxMasterId =row['rxMasterId'];
			$("#ARDetailsGrid").jqGrid('GridUnload');
			loadARDetails(rxMasterId,ardate);
			$("#ARDetailsDiv").dialog("open");
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
            	
            	 
    	}
    }).navGrid('#accountReceivablepager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	

	$("#AccountReceivableGrid").navButtonAdd('#accountReceivablepager', {
		  caption: "CSV",
		  title: "CSV",
		  buttonicon: "ui-icon-document",
		  onClickButton: printfirstgridcsv,
		  position: "last"
		});
	$("#AccountReceivableGrid").navButtonAdd('#accountReceivablepager', {
		  caption: "Print",
		  title: "Print",
		  buttonicon: "ui-icon-print",
		  onClickButton:printfirstgridpdf ,
		  position: "last"
		});
	
}

function printfirstgridpdf(){
	var rowId=jQuery("#AccountReceivableGrid").jqGrid('getGridParam', 'selrow');
	var selectedUser = $('#SalesRepComboList').val();
	var date=$("#asofardate").val();
	var customername="0";
	var rxMasterId ="0";
	if(rowId!=null){
	/*	showErrorMessage('Please Select a Customer');
		return false;*/
		var row=jQuery("#AccountReceivableGrid").getRowData(rowId);
	   rxMasterId =row['rxMasterId'];
	}
	var customerIDs = 0;
	if($('#customerName').val()!=''){
		customerIDs  = $('#customerId').val();
		window.open("./projectscontroller/printProjectsPDF?number=1&selectedUser="+selectedUser+"&AsOf="+date+"&Customername="+customername+"&customerID="+customerIDs);
	}else{
		window.open("./projectscontroller/printProjectsPDF?number=1&selectedUser="+selectedUser+"&AsOf="+date+"&Customername="+customername+"&customerID="+customerIDs);
	}
		
	createtpusage('Project Screen','Account Receivable Grid View','Info','Project Screen,Account Receivable Grid View,Generating Report for Customer ID:'+customerIDs);
	return false;
}

function printfirstgridcsv(){
	var rowId=jQuery("#AccountReceivableGrid").jqGrid('getGridParam', 'selrow');
	var selectedUser = $('#SalesRepComboList').val();
	var date=$("#asofardate").val();
	var customername="0";
	var rxMasterId ="0";
	if(rowId!=null){
	/*	showErrorMessage('Please Select a Customer');
		return false;*/
		var row=jQuery("#AccountReceivableGrid").getRowData(rowId);
	   rxMasterId =row['rxMasterId'];
	}
	var customerIDs = 0;
	if($('#customerName').val()!=''){
		customerIDs  = $('#customerId').val();
		window.open("./projectscontroller/printProjectsPDF?number=0&selectedUser="+selectedUser+"&AsOf="+date+"&Customername="+customername+"&customerID="+customerIDs);
	}else{
		window.open("./projectscontroller/printProjectsPDF?number=0&selectedUser="+selectedUser+"&AsOf="+date+"&Customername="+customername+"&customerID="+customerIDs);
	}
		
	createtpusage('Project Screen','Account Receivable Grid View','Info','Project Screen,Account Receivable Grid View,Generating Report for Customer ID:'+customerIDs);
	return false;
}



function loadARDetails(rxMasterId,ardate){
	var date= ardate;
	var customername=$("#ardetailscustomerid").val();
	var selectedUser = $('#SalesRepComboList').val();
	$("#ARDetailsGrid").jqGrid({
		datatype: 'json',
		url: "./projectscontroller/getARDetailsBasedonCustomer?tsUserLoginID="+selectedUser+"&customerID="+rxMasterId+"&inputDate="+date+"&Customername="+customername,
		mtype: 'GET',
		//pager: jQuery('#accountReceivablepager'),
		colNames:['invoiceid','Date','Invoice #','PO #','Amount','Age','Current','30 Days','60 Days','90 Days','Total'],
		colModel :[
		    {name:'invoiceID', index:'invoiceID', align:'', width:80,hidden:true},
           	{name:'invoiceDate', index:'invoiceDate', align:'left', width:90, editable:true,hidden:false},
			{name:'invoiceNumber', index:'invoiceNumber', align:'', width:80,hidden:false},
			{name:'poNumber', index:'poNumber', align:'', width:80,hidden:false},
			{name:'poAmount', index:'poAmount', align:'', width:80,hidden:false,formatter:currencyFormatter},
			{name:'days', index:'days', align:'center', width:80,hidden:false},
			{name:'currentAmt', index:'currentAmt', align:'right', width:80,hidden:false,formatter:currencyFormatter},
			{name:'thirtyDays', index:'thirtyDays', align:'right', width:80,hidden:false,formatter:currencyFormatter},
			{name:'sixtyDays', index:'sixtyDays', align:'right', width:80,hidden:false,formatter:currencyFormatter},
			{name:'ninetyDays', index:'ninetyDays', align:'right', width:80,hidden:false,formatter:currencyFormatter},
			{name:'totalDaysAmt', index:'totalDaysAmt', align:'right', width:80,hidden:false,formatter:currencyFormatter},
			],
		rowNum: 5000,
		rowList: [],        // disable page size dropdown
	    pgbuttons: false,     // disable page control like next, back button
	    pgtext: null,         // disable pager text like 'Page 0 of 10'
	    viewrecords: false,
		recordtext: '',
		pager: '#ARDetailspager',
		sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
		//autowidth: true,
		height:300,	width: 800,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
		      	
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			
			var row=jQuery("#ARDetailsGrid").getRowData(rowId);
			var invoiceid =row['invoiceID'];
			
			var rowId=jQuery("#AccountReceivableGrid").jqGrid('getGridParam', 'selrow');
			var rowvalue=jQuery("#AccountReceivableGrid").getRowData(rowId);
			var rxMasterId =rowvalue['rxMasterId'];
			
			window.location.href = './createinvoice?oper=create&frompage=projecttab&invoiceID='+invoiceid+"&rxMasterId="+rxMasterId;
			//$("#AccountReceivableGrid").jqGrid('GridUnload');
			///	loadAccountReceivable();
				//$("#AccountReceivableDiv").dialog("open");
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
            	
            	 
    	}
    }).navGrid('#ARDetailspager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	
	$("#ARDetailsGrid").navButtonAdd('#ARDetailspager', {
		  caption: "CSV",
		  title: "CSV",
		  buttonicon: "ui-icon-document",
		  onClickButton: printsecondgridcsv,
		  position: "last"
		});
	$("#ARDetailsGrid").navButtonAdd('#ARDetailspager', {
		  caption: "Print",
		  title: "Print",
		  buttonicon: "ui-icon-print",
		  onClickButton:printsecondgridpdf ,
		  position: "last"
		});
}

function printsecondgridpdf(){
	var rowId=jQuery("#AccountReceivableGrid").jqGrid('getGridParam', 'selrow');
	if(rowId==null){
		showErrorMessage('Please Select a Customer');
		return false;
	}
	var row=jQuery("#AccountReceivableGrid").getRowData(rowId);
	var rxMasterId =row['rxMasterId'];
	var selectedUser = $('#SalesRepComboList').val();
	var date= $("#asofardate").val();
	var customername=$("#ardetailscustomerid").val();
	window.open("./projectscontroller/printProjectsPDF?number=2&selectedUser="+selectedUser+"&AsOf="+date+"&Customername="+customername+"&customerID="+rxMasterId);
	return false;
}
function printsecondgridcsv(){
	var rowId=jQuery("#AccountReceivableGrid").jqGrid('getGridParam', 'selrow');
	if(rowId==null){
		showErrorMessage('Please Select a Customer');
		return false;
	}
	var row=jQuery("#AccountReceivableGrid").getRowData(rowId);
	var rxMasterId =row['rxMasterId'];
	var selectedUser = $('#SalesRepComboList').val();
	var date= $("#asofardate").val();
	var customername=$("#ardetailscustomerid").val();
	window.open("./projectscontroller/printProjectsPDF?number=3&selectedUser="+selectedUser+"&AsOf="+date+"&Customername="+customername+"&customerID="+rxMasterId);
	return false;
}
function loadViewsGrid(){

	$("#viewsGrid").jqGrid({
					datatype: 'local',
					mtype: 'GET',
					pager: jQuery('#viewsGridPager'),
					colNames:['Id',''],
					colModel :[
			           	{name:'id', index:'id', align:'left', width:80, editable:true,hidden:true},
						{name:'viewsname', index:'viewsname', align:'', width:80,hidden:false},
						],
					rowNum: 50,
					
					pgbuttons: false,	
					recordtext: '',
					rowList: [50, 100, 200, 500, 1000],
					viewrecords: false,
					pager: '#viewsGridPager',
					sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Views',
					//autowidth: true,
					height:100,	width: 200,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
					loadComplete: function(data) {
						
				    },
					loadError : function (jqXHR, textStatus, errorThrown) {
					    
					},
					ondblClickRow: function(rowId) {
						var row=jQuery("#viewsGrid").getRowData(rowId);
						var id =row['id'];
						if(id==1){
							$("#AccountReceivableGrid").jqGrid('GridUnload');
							loadAccountReceivable();
							$("#AccountReceivableDiv").dialog("open");
						}
						if(id==2){
							$("#CommissionStatementGrid").jqGrid('GridUnload');
							console.log($('#SalesRepComboList').val());
							if($('#SalesRepComboList').val() > 0){
							loadCommissionStatement(0);
							$("#CommissionStatementDiv").dialog("open");
							}
							else{
								var errorText ='Please Select any Salesrep to view Commission Statement!';
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:RED;">Error: ' + errorText + '</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");}}]}).dialog("open");
							}
						}
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
			    	}
			    }).navGrid('#viewsGridPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	
	
}

function loadCommissionStatement(periodID){
	
	var selectedUser = $('#SalesRepComboList').val();
	$("#CommissionStatementDiv").empty();
	$("#CommissionStatementDiv").append("<table style='padding-left:0px' id='CommissionStatementGrid' class='scroll'></table><div id='CommissionStatementPager' class='scroll' style='text-align:right;'></div>");
	
	$("#CommissionStatementGrid").jqGrid({
		datatype: 'json',
		url: "./employeeCrud/viewCommissionStatementProject?userLoginID="+selectedUser+"&PeriodID="+periodID,
		mtype: 'GET',
		//pager: jQuery('#accountReceivablepager'),
		colNames:['Job#','Job Name','Release Type','Customer Invoice#','Invoice Paid Date','Gross Profit','Commission Margin %','Commission Paid','ecStatementID'],
		colModel :[
           	{name:'jobNumber', index:'jobNumber', align:'left', width:40, editable:true,hidden:false},
			{name:'jobName', index:'jobName', align:'left', width:200,hidden:false},
			{name:'releaseType', index:'releaseType', align:'center', width:50,hidden:false},
			{name:'reference', index:'reference', align:'center', width:50,hidden:false},
			{name:'datePaid', index:'datePaid', align:'center', width:50,hidden:false,formatter:FormatCommissionDate},
			{name:'ecRepSplitProfit', index:'ecRepSplitProfit', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			{name:'ecInrepsplitCommissionRate', index:'ecInrepsplitCommissionRate', align:'center', width:50,hidden:false},
			{name:'ecInrepsplitAmountDue', index:'ecInrepsplitAmountDue', align:'right', width:50,hidden:false,formatter:currencyFormatter},
			{name:'ecStatementID', index:'ecStatementID', align:'right', width:50,hidden:true}
			],
		rowNum: 5000,
		rowList: [],        // disable page size dropdown
	    pgbuttons: false,     // disable page control like next, back button
	    pgtext: null,         // disable pager text like 'Page 0 of 10'
	    viewrecords: false,
		recordtext: '',
		pager: '#CommissionStatementPager',
		sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
		//autowidth: true,
		height:300,	width: 950,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var row=jQuery("#CommissionStatementGrid").getRowData(rowId);
			var rxMasterId =row['jobNumber'];
			
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
            	
            	 
    	}
    }).navGrid('#CommissionStatementPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	
	
	/* For ID#458
	 */ $("#CommissionStatementGrid").navButtonAdd('#CommissionStatementPager', {
		  caption: "Print",
		  title: "Print",
		  buttonicon: "ui-icon-print",
		  onClickButton:printCommissionPDF ,
		  position: "last"
		});
	$("#CommissionStatementGrid").navButtonAdd('#CommissionStatementPager', {
		  caption: "CSV",
		  title: "CSV",
		  buttonicon: "ui-icon-document",
		  onClickButton: printCommissionCSV,
		  position: "last"
		});
	/*$("#CommissionStatementGrid").navButtonAdd('#CommissionStatementPager', {
		  caption: "Print",
		  title: "Click here to add new record",
		  buttonicon: "ui-icon-print",
		  onClickButton:printCommissionPdf,
		  position: "last"
		});
*/
	
}

function printCommissionPDF(){
	$("#CommissionStatementGrid").setSelection(1, true);
	var rowId=jQuery("#CommissionStatementGrid").jqGrid('getGridParam', 'selrow');
	var row=jQuery("#CommissionStatementGrid").getRowData(rowId);
	var ecStatementID =row['ecStatementID'];
	var periodDate =$("#endperioddate :selected").text();
	var url="./employeeCrud/viewEmployeeCommissionStatement?endperioddate="+periodDate+"&ecStatementID="+ecStatementID;
	window.open(url);
	return false;
}
function printCommissionCSV(){
	$("#CommissionStatementGrid").setSelection(1, true);
	var rowId=jQuery("#CommissionStatementGrid").jqGrid('getGridParam', 'selrow');
	var row=jQuery("#CommissionStatementGrid").getRowData(rowId);
	var ecStatementID =row['ecStatementID'];
	var periodDate =$("#endperioddate :selected").text();
	window.open('./employeeCrud/generateEmployeeCommissionsCSV?endperioddate='+periodDate+'&ecStatementID='+ecStatementID);
	return false;
}


		function printCommissionPdf(){
			var selectedUser = $('#SalesRepComboList').val();
			window.open("./employeeCrud/vieweCommissionStatement?employeeID="+selectedUser);
			return false;
		}


		function loadOpenJobs(userLoginID){
			var rxMsaterId = -1;
			if($('#customerName').val()!=''){
				rxMsaterId  = $('#customerId').val();
			}
			$("#openJobsGrid").jqGrid({
				datatype: 'JSON',
				url:'./projectscontroller/getOpenedJobs?rxCustomerID='+rxMsaterId+'&tsUserLoginID='+userLoginID,
				mtype: 'POST',
				pager: jQuery('#openJobsGridPager'),
				colNames:['joMasterId','rxMasterID','rxCustomerID','Customer', 'Job Name','Job#','$Amount','Released/UnInvoiced','Released/Invoiced','Unreleased'],
				colModel :[
				    {name:'joMasterId', index:'joMasterId', align:'left', width:80, editable:true,hidden:true},
		           	{name:'rxMasterID', index:'rxMasterID', align:'left', width:80, editable:true,hidden:true},
					{name:'rxCustomerID', index:'rxCustomerID', align:'', width:80,hidden:true},
					{name:'customerName', index:'customerName', align:'center', width:120,hidden:false},
					{name:'jobName', index:'jobName', align:'', width:90,hidden:false},
					{name:'jobNumber', index:'jobNumber', align:'', width:40,hidden:false},
					{name:'contractAmount', index:'contractAmount', align:'right', width:40,hidden:false,formatter:currencyFormatter},
					{name:'allocated', index:'allocated', align:'right', width:80,hidden:true,formatter:currencyFormatter},
					{name:'invoiced', index:'invoiced', align:'right', width:80,hidden:true,formatter:currencyFormatter},
					{name:'unreleased', index:'unreleased', align:'', width:80,hidden:true,formatter:currencyFormatter}
					
					],
				rowNum: 50,
				pgbuttons: true,	
				recordtext: '',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: false,
				pager: '#openJobsGridPager',
				sortname: 'joMasterID', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: 'Open Jobs',
				//autowidth: true,
				height:250,	width: 500,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown) {
				    
				},
				ondblClickRow: function(rowId) {
					/*var rows = jQuery("#openJobsGrid").getDataIDs();
					row=jQuery("#openJobsGrid").getRowData(rows[rowId-1]);
					var jobName = row['jobName'];
					var jobNumber = row['jobNumber'];
					window.location.href = './jobflow?token=view&jobNumber='+jobNumber+'&jobName='+jobName;	*/	
					
					
					var checkpermission=getGrantpermissionprivilage('Main',0);
					if(checkpermission){
					var rowData = jQuery(this).getRowData(rowId); 
					var jobNumber = rowData['jobNumber'];
					var jobName = $.trim(rowData['jobName']);
					var joMasterID=rowData['joMasterId'];
					var jobStatus = "";
					$.ajax({
						url: "./job_controller/jobStatusHome",  
						mType: "GET", 
						async:false,
						data : { 'jobNumber' : jobNumber ,'joMasterID':joMasterID},
						success: function(data){ 
							jobStatus = data;
							var urijobname=encodeBigurl(jobName);
							var aQryStr = "./jobflow?token=view&jobNumber="+jobNumber +"&jobName="+urijobname + "&jobStatus="+ jobStatus+"&joMasterID="+joMasterID;
							createtpusage('job-Main Tab','view','Info','Job,Main Tab,View,JobNumber:'+jobNumber); 	
							var checkpermission=getGrantpermissionprivilage('Main',0);
							if(checkpermission){
							document.location.href = aQryStr;
							}
						}
				 	});
					}
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
		    	}
		    }).navGrid('#openJobsGridPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
			
			$('#gbox_openJobsGrid .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button" onclick="openJobsDialog()">Open</button></div>');
		}
		
		function loadSalesPurchaseGrid(userloginid){
			
			
			var rxMsaterId = -1;
			if($('#customerName').val()!=''){
				rxMsaterId  = $('#customerId').val();
			}
			$("#SalesPurchaseOrdersGrid").jqGrid({
				datatype: 'JSON',
				url:'./projectscontroller/getPurchaseSalesOrder?rxCustomerID='+rxMsaterId+'&tsUserLoginID='+userloginid+'&fromDate=&todate=',
				mtype: 'POST',
				pager: jQuery('#SalesPurchaseOrdersGridPager'),
				colNames:['vepoID','rxVendorId', 'joReleaseId', 'OrderDate','Order #','Vendor', 'Vendor #','Ship Date'],
				colModel :[				           
		           	{name:'vePoid', index:'vePoid', align:'left', width:80, editable:true,hidden:true},
		           	{name:'rxVendorId', index:'rxVendorId', align:'left', width:80, editable:true,hidden:true},
		           	{name:'joReleaseId', index:'joReleaseId', align:'left', width:80, editable:true,hidden:true},
		        	{name:'dateWanted', index:'dateWanted', align:'', width:80,hidden:false},
					{name:'ponumber', index:'ponumber', align:'center', width:80,hidden:false},
					{name:'vendorName', index:'vendorName', align:'', width:80,hidden:false},
					{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'', width:80,hidden:false},
					{name:'shipDate', index:'shipDate', align:'', width:80,hidden:false}],
				rowNum: 50,
				pgbuttons: true,	
				recordtext: '',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: false,
				//pager: '#SalesPurchaseOrdersGridPager',
				sortname: 'dateWant', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: 'Open Purchase Order/Sales Order',
				//autowidth: true,
				height:250,	width: 450,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown) {
				    
				},
				ondblClickRow: function(rowId) {
					var rows = jQuery("#SalesPurchaseOrdersGrid").getDataIDs();
					var row = jQuery("#SalesPurchaseOrdersGrid").getRowData(rows[rowId-1]);
					var vePoid = row['vePoid'];
					var rxVendorId = row['rxVendorId'];
					var ponumber = row['ponumber'];
					var vendorName = row['vendorName'];
					var vendorOrderNumber = row['vendorOrderNumber'];
					var joReleaseId = row['joReleaseId'];
					var JobNumber = "", jobCustomerName_ID = "", project_aMasterID = "";
					//alert(" joReleaseId = "+joReleaseId+" || rxVendorId = "+rxVendorId+" || "+vePoid+" || "+ponumber+" || "+vendorName+" || "+vendorOrderNumber+" || "+vendorName.indexOf("Warehouse"));
					if(vendorName.indexOf("Warehouse") == 0)
					{
						//alert(vendorName.indexOf("Warehouse")+" --------- "+vePoid);
//						PreloadDataFromInventory(vePoid, rxVendorId);
						$.ajax({
							url:"./projectscontroller/getJoMasterData?joReleaseId="+joReleaseId,
							type : "GET",
							async:false,
							success : function(data) {
								//alert(data);
								//returnString = JobNumber+"@"+rxCustomerID+"@"+joMasterID+"@"+Description;
								var dd = data.split("@");
								JobNumber = dd[0];
								jobCustomerName_ID = dd[1];
								project_aMasterID = dd[2];
								
								$("#jobNumber_ID").val(dd[0]);
								$("#jobCustomerName_ID").val(dd[1]);
								$("#jobName_ID").val(dd[3]);
								$("#Cuso_ID").text(vePoid);
								//alert(""+vePoid);
								//alert(vePoid+" || "+project_aMasterID+" || "+vePoid+" || "+JobNumber+" || "+joReleaseId);
//								PreloadDataFromInventory(vePoid, project_aMasterID, vePoid, JobNumber, joReleaseId);
								//(cusoid, rxMasterID, vePOID, jobNumber, joReleaseDetailID)
							}
						});
//						url : "./salesOrderController/getPreLoadData",
//						type: "POST",
//						data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
//						
//						window.location.href = "./project_so?project_aMasterID="+project_aMasterID+"&project_acuSoid="+vePoid;
						//window.location.href = "./project_so?project_acuSoid="+vePoid+"&project_aMasterID="+project_aMasterID;
						
						
						//document.location.href ="./salesorder?cusoid="+vePoid+"&customerid="+jobCustomerName_ID+"&oper=projectSO";
						
						document.location.href ="./salesorder?oper=create";
						
//						+'&project_vePOID='+vePOID
//							+'&project_jobNumber='+jobNumber+"&project_joReleaseDetailID="+joReleaseDetailID;
					}
					if(vendorName.indexOf("Warehouse") < 0)
					{
						//alert(vendorName.indexOf("Warehouse")+" ======= "+vePoid);
						window.location.href = "./editpurchaseorder?token=view&aVePOId=" + vePoid;
					}
					
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
		    	}
		    }).navGrid('#SalesPurchaseOrdersGridPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
			$('#gbox_SalesPurchaseOrdersGrid .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button" onclick="openPoSoDialog()">Open</button></div>');
		}
		
		
		////////////
		
		
		var sEmail = "";
		function loadEmailList(rxMasterID)
		{
			$.ajax({ 
				url: "./rxdetailedviewtabs/getEmailList",
				mType: "GET",
				data : {'rxMasterID' : rxMasterID},
				success: function(data){
					sEmail = "";
					$.each(data, function(key, valueMap) {
					if("emailList" == key)
					{
						$.each(valueMap, function(index, value){
							if(value.email != null && value.email.trim() != '')
							sEmail+='<option value='+value.rxContactId+'>'+value.email+'</option>';
						
						}); 
						$('#emailList').html(sEmail);
					} 
					});
				}
			});
			
		}
		
		
		function PreloadDataFromInventory(cusoid, rxMasterID, vePOID, jobNumber, joReleaseDetailID){
			$('#salesreleasetab').tabs({ selected: 0 });
			$('#salesreleasetab').tabs({ active: 0 });
			
			//alert("&cuSOID="+cusoid+"&rxMasterID="+rxMasterID+"&vePOID="+vePOID+"&jobNumber="+jobNumber+"&joReleaseDetailID="+joReleaseDetailID);
			var cuSOID = cusoid;
//			var rxMasterID = $('#rxCustomer_ID').text();
			//alert("RxMaster Id--->"+rxMasterID);
//			@RequestParam(value = "cuSOID", required = false) Integer cuSOID,
//			@RequestParam(value = "rxMasterID", required = false) String rxMasterId,
//			@RequestParam(value = "vePOID", required = false) Integer vePOID,
//			@RequestParam(value = "jobNumber", required = false) String jobNumber,
//			@RequestParam(value = "joReleaseDetailID", required = false) Integer joReleaseDetailID,
//			if(cuSOID!=null && typeof(cuSOID) != "undefined"){
				$.ajax({
					url: "./salesOrderController/getPreLoadData",
					type: "POST",
					data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
					success: function(data) {
						console.log(data);
						if(data.vepo !== null){
							PreloadDataInvoiceFromPO(data);
							$('#customerInvoice_lineproNumberID').val(data.vendorInvoiceNumber);
							$('#customerInvoice_proNumberID').val(data.vendorInvoiceNumber);
						}else{
						$("#CustomerNameGeneral").val(data.CustomerName);
						if (typeof(data.Cuso) != "undefined" && data.Cuso != null){
						$("#customerInvoice_invoiceNumberId").val(data.Cuso.sonumber);
						$('#customerInvoice_subTotalID').val(formatCurrency(0));
						$('#customerInvoice_totalID').val(formatCurrency(0));
						$('#customerinvoicepaymentId').val(data.Cuso.cuTermsId);
						
						$('#customerInvoice_proNumberID').val(data.Cuso.trackingNumber);
						$('#customerInvoie_PONoID').val(data.Cuso.customerPonumber);
						$('#tagJobID').val(data.Cuso.tag);
						$('#customerInvoice_frightIDcu').val(formatCurrency(0)); 
						$('#customerInvoice_linefrightID').val(formatCurrency(data.Cuso.freight)); 
						
						if(whseID === 'Drop Ship')
							{
							console.log("yes=====================");
							$('#prWareHouseSelectID').val(3);
							$('#prWareHouseSelectlineID').val(3);
							}
						else
							{
							$('#prWareHouseSelectID').val(data.Cuso.prFromWarehouseId);
							$('#prWareHouseSelectlineID').val(data.Cuso.prFromWarehouseId);
							}
						
						
						
						$('#shipViaCustomerSelectID').val(data.Cuso.veShipViaId);
						$('#customer_Divisions').val(data.Cuso.coDivisionID);
						$('#customerinvoice_paymentTerms').val(data.Cuso.cuTermsId);
						$('#customerInvoice_salesRepsList').val(data.SalesMan);
						$('#customerInvoice_CSRList').val(data.AE);
						$('#customerInvoice_SalesMgrList').val(data.Submitting);
						$('#customerInvoice_EngineersList').val(data.Costing);
						$('#customerInvoice_PrjMgrList').val(data.Ordering);
						$('#customerInvoice_salesRepId').val(data.Cuso.cuAssignmentId0);
						$('#customerInvoice_CSRId').val(data.Cuso.cuAssignmentId1);
						$('#customerInvoice_salesMgrId').val(data.Cuso.cuAssignmentId2);
						$('#customerInvoice_engineerId').val(data.Cuso.cuAssignmentId3);
						$('#customerInvoice_prjMgrId').val(data.Cuso.cuAssignmentId4);
						$('#customerInvoice_taxIdcu').val(formatCurrency(0));
						$('#cuGeneral_taxvalue').val(formatCurrency(data.Cuso.taxTotal));
						$('#customerInvoice_lineproNumberID').val(data.Cuso.trackingNumber);
						$('#customerInvoice_lineinvoiceNumberId').val(data.Cuso.sonumber);
						$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
						$('#customerinvoice_paymentTerms').val(data.termsDesc);
						$("#shipToCustomerAddressID").val(data.Cuso.rxShipToId);
						$("#rxShipToOtherAddressID").val(data.Cuso.rxShipToAddressId);	
						
						
						console.log("---=-==->>>>"+data.Cuso.shipToMode);
						addressToShipCustomerInvoice();
						
						if(data.Cuso.shipToMode == 0)
							{
							$('#shiptoModeID').val(0);
							usinvoiceShiptoAddress(data.Cuso.cuSoid,'cuso')
							console.log('inside SalesRelease.js data.Cuso.shipToMode == 0');
							}
						else if(data.Cuso.shipToMode == 1)
							{
							$("#cuinvoiceUs").hide();
							$('#shiptoModeID').val(1);
							console.log('inside SalesRelease.js data.Cuso.shipToMode == 1');
							customerinvoiceShiptoAddress(data.Cuso.cuSoid,'cuso');
							}
						else if(data.Cuso.shipToMode == 3)
							{
							$("#cuinvoiceUs").hide();
							$('#shiptoModeID').val(3);
						//	$("#shipToCustomerAddressID").val()
							console.log('inside SalesRelease.js data.Cuso.shipToMode == 3');
							otherinvoiceShiptoAddress(data.Cuso.cuSoid,'cuso')
							}
						else if(data.Cuso.shipToMode == 2)
							{
							$("#cuinvoiceUs").hide();
							$('#shiptoModeID').val(2);
							console.log('inside SalesRelease.js data.Cuso.shipToMode == 2');
							jobsiteinvoiceShiptoAddress(data.Cuso.joReleaseId,'cuso')
							}
						
						
						var createdDate = data.Cuso.createdOn;
						if (typeof (createdDate) != 'undefined') 
							FormatDate(createdDate);
						var shipDate = data.Cuso.shipDate;
						if (typeof (shipDate) != 'undefined') 
							FormatShipDate(shipDate);
						if (typeof(data.Cusodetail) != "undefined" && data.Cusodetail != null){
							$('#customerInvoice_subTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
							$('#customerInvoice_totalID').val(formatCurrency(data.Cusodetail.taxTotal+data.Cuso.taxTotal+data.Cuso.freight));
							$('#customerInvoice_linesubTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
							$('#customerInvoice_linetotalID').val(formatCurrency(data.Cusodetail.taxTotal+data.Cuso.taxTotal+data.Cuso.freight));
						}
						}
						formattax();
						paymentTermsDue(data.Cuso.cuTermsId);
						}
					}
				});
			}

			function PreloadDataInvoiceFromPO(data){
				$("#CustomerNameGeneral").val(data.CustomerName);
				if (typeof(data.vepo) != "undefined" && data.vepo != null){
				$("#customerInvoice_invoiceNumberId").val(data.vepo.ponumber);
				$('#customerInvoice_subTotalID').val(formatCurrency(0));
				$('#customerInvoice_totalID').val(formatCurrency(0));
				if(data.Cumaster!=null){
					$('#customerinvoicepaymentId').val(data.Cumaster.cuTermsId);
					paymentTermsDue(data.Cumaster.cuTermsId);
				}
//				$('#customerInvoice_proNumberID').val(data.Cumaster.trackingNumber);
				$('#customerInvoie_PONoID').val(data.vepo.customerPonumber);
//				$('#tagJobID').val(data.Cuso.tag);
				$('#customerInvoice_frightIDcu').val(formatCurrency(0)); 
				$('#customerInvoice_linefrightID').val(formatCurrency(data.vepo.freight)); 
				if(whseID === 'Drop Ship')
				{
				console.log("yes NO=====================");
				//$('#prWareHouseSelectID').val(3);
				//$('#prWareHouseSelectlineID').val(3);
				$('#prWareHouseSelectID').val(data.vepo.prWarehouseId);
				$('#prWareHouseSelectlineID').val(data.vepo.prWarehouseId);
				}
				else
				{
				$('#prWareHouseSelectID').val(data.vepo.prWarehouseId);
				$('#prWareHouseSelectlineID').val(data.vepo.prWarehouseId);
				}
				/*$('#prWareHouseSelectID').val(data.vepo.prWarehouseId);
				$('#prWareHouseSelectlineID').val(data.vepo.prWarehouseId);*/
				$('#shipViaCustomerSelectlineID').val(data.vepo.veShipViaId);
				$('#shipViaCustomerSelectID').val(data.vepo.veShipViaId);
				$('#customer_Divisions').val(data.aJomaster.coDivisionId);
				$('#customerInvoice_salesRepsList').val(data.SalesMan);
				$('#customerInvoice_CSRList').val(data.AE);
				$('#customerInvoice_SalesMgrList').val(data.Submitting);
				$('#customerInvoice_EngineersList').val(data.Costing);
				$('#customerInvoice_PrjMgrList').val(data.Ordering);
				$('#customerInvoice_salesRepId').val(data.aJomaster.cuAssignmentId0);
				$('#customerInvoice_CSRId').val(data.aJomaster.cuAssignmentId1);
				$('#customerInvoice_salesMgrId').val(data.aJomaster.cuAssignmentId2);
				$('#customerInvoice_engineerId').val(data.aJomaster.cuAssignmentId3);
				$('#customerInvoice_prjMgrId').val(data.aJomaster.cuAssignmentId4);
				$('#customerInvoice_taxIdcu').val(formatCurrency(0));
				$('#cuGeneral_taxvalue').val(formatCurrency(data.vepo.taxTotal));
//				$('#customerInvoice_lineproNumberID').val(data.vepo.trackingNumber);
				$('#customerInvoice_lineinvoiceNumberId').val(data.vepo.ponumber);
				console.log("PONumber is=====>"+data.vepo.ponumber);
				$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
				$('#customerinvoice_paymentTerms').val(data.termsDesc);
				var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.]+/g,"");
				 //var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.]+/g,"");			 
				 var tot = allocated;
				 var taxPerc = $('#customerInvoice_generaltaxId').val().replace(/[^0-9\.]+/g,"");
				 console.log("allocated*taxPerc ::: "+(allocated*taxPerc));
				 var taxValue1 = (allocated*taxPerc)/100;
				 $('#customerInvoice_taxIdcu').val(formatCurrency(taxValue1));
				 $('#cuGeneral_taxvalue').val(formatCurrency(taxValue1));
				 var aTotal = parseFloat(allocated)+parseFloat(freight)+parseFloat(taxValue1);
				 console.log('Tax Value  :: '+taxValue1+"  :: Freigt ::  "+freight+"  :: Amt  ::"+allocated + "  :: Total  :: "+aTotal);
				 $('#customerInvoice_totalID').val(formatCurrency(aTotal));
				 $('#customerInvoice_linetotalID').val(formatCurrency(aTotal));
				 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
				 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
				 iFlag = 2;
				 $("#shipToCustomerAddressID").val(data.vepo.rxShipToId)
				 addressToShipCustomerInvoice();
				 customerinvoiceShiptoAddress(data.vepo.vePoid,'vepo');
				 
				 if(data.vepo.shipToMode == 0)
					{
					 console.log('inside SalesRelease.js data.vepo.shipToMode == 0');
					usinvoiceShiptoAddress(data.vepo.vePoid,'vepo')
					$('#shiptoModeID').val(0);
					}
				else if(data.vepo.shipToMode == 1)
					{
					console.log('inside SalesRelease.js data.vepo.shipToMode == 1');
					$("#cuinvoiceUs").hide();
					$('#shiptoModeID').val(1);
					customerinvoiceShiptoAddress(data.vepo.vePoid,'vepo');
					}
				else if(data.vepo.shipToMode == 3)
					{
					console.log('inside SalesRelease.js data.vepo.shipToMode == 3');
					$("#cuinvoiceUs").hide();
				//	$("#shipToCustomerAddressID").val()
					$('#shiptoModeID').val(3);
					otherinvoiceShiptoAddress(data.vepo.vePoid,'vepo')
					}
				else if(data.vepo.shipToMode == 2)
					{
					console.log('inside SalesRelease.js data.vepo.shipToMode == 2');
					$("#cuinvoiceUs").hide();
					$('#shiptoModeID').val(2);
					jobsiteinvoiceShiptoAddress(data.vepo.joReleaseId,'vepo')
					}
				 
				var createdDate = data.vepo.createdOn;
				if (typeof (createdDate) != 'undefined') 
					FormatDate(createdDate);
				var shipDate = data.vepo.createdOn;
				if (typeof (shipDate) != 'undefined') 
					FormatShipDate(shipDate);
				}
				formattax();
				}
			function loadCustomerInvoice(){
				var cuinvoiceID = $('#cuinvoiceIDhidden').val();	
				if(cuinvoiceID == null || cuinvoiceID == '')
				{
					cuinvoiceID = $('#cuInvoiceID').text();
					$('#cuinvoiceIDhidden').val(cuinvoiceID);
				}
				$("#customerInvoice_lineitems").jqGrid({
					datatype: 'JSON',
					mtype: 'POST',
					pager: jQuery('#customerInvoice_lineitemspager'),
					url:'./salesOrderController/cuInvlineitemGrid',
					postData: {'cuInvoiceID':cuinvoiceID},
					colNames:['Product No', 'Description','Qty','Price Each', 'Mult.', 'Tax', 'Amount', 'Manu. ID','cuSodetailId', 'prMasterID'],
					colModel :[
				{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:17},editrules:{edithidden:false,required: true}},
			 	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:17},editrules:{edithidden:false},  
					cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
				{name:'quantityBilled', index:'quantityBilled', align:'center', width:15,hidden:false, editable:true, editoptions:{size:17, alignText:'left'},editrules:{edithidden:true,required: false}},
				{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:17, alignText:'right'},editrules:{edithidden:true}},
				{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:17, alignText:'right'}, formatter:customCurrencyFormatter, editrules:{edithidden:true}},
				{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
				{name:'note', index:'note', align:'right', width:50,hidden:true, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
				{name:'cuInvoiceId', index:'cuInvoiceId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
				{name:'cuInvoiceDetailId', index:'cuInvoiceDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
				{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}}],
					rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
					sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
					height:210,	width: 750, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
					loadComplete: function(data) {
						$("#customerInvoice_lineitems").setSelection(1, true);
					},
					loadError : function (jqXHR, textStatus, errorThrown){	},
					onSelectRow:  function(id){
						
					},
					editurl:"./salesOrderController/manpulatecuInvoiceReleaseLineItem"
			}).navGrid('#customerInvoice_lineitemspager', {add:true, edit:true,del:true,refresh:true,search:false},
						//-----------------------edit options----------------------//
						{
				 	width:400,left:630, top: 550, zIndex:1040,
					closeAfterEdit:true, reloadAfterSubmit:true,
					modal:true, jqModel:true,
					editCaption: "Edit Customer Invoice",
					beforeShowForm: function (form) 
					{
						$("a.ui-jqdialog-titlebar-close").hide();
						jQuery('#TblGrid_customerInvoice_lineitems#tr_itemCode .CaptionTD').append('Product No: ');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_itemCode .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_description .CaptionTD').empty();
						jQuery('#TblGrid_customerInvoice_lineitems#tr_description .CaptionTD').append('Description: ');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_quantityBilled.CaptionTD').empty();
						jQuery('#TblGrid_customerInvoice_lineitems#tr_quantityBilled.CaptionTD').append('Qty: ');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_quantityBilled .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_unitCost .CaptionTD').empty();
						jQuery('#TblGrid_customerInvoice_lineitems#tr_unitCost .CaptionTD').append('Cost Each.: ');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_priceMultiplier .CaptionTD').empty();
						jQuery('#TblGrid_customerInvoice_lineitems#tr_priceMultiplier .CaptionTD').append('Mult.: ');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
						jQuery('#TblGrid_customerInvoice_lineitems#tr_taxable .CaptionTD').empty();
						jQuery('#TblGrid_customerInvoice_lineitems#tr_taxable .CaptionTD').append('Tax: ');
						var unitCost = $('#unitCost').val().replace(/[^0-9-.]/g, '');
						$('#unitCost').val(unitCost);
						var priceMultiplier = $('#priceMultiplier').val();
						priceMultiplier=  parseFloat(priceMultiplier.replace(/[^0-9-.]/g, ''));
						$('#priceMultiplier').val(priceMultiplier);
						 
					},
					beforeSubmit:function(postdata, formid) {
						$("#note").autocomplete("destroy"); 
						$(".ui-menu-item").hide();
						var aPrMasterID = $('#prMasterId').val();
						if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
						return [true, ""];
					},
					onclickSubmit: function(params){
						var cuinvoiceID = $('#cuinvoiceIDhidden').val();
						if(cuinvoiceID == null || cuinvoiceID == '')
						{
							cuinvoiceID = $('#cuInvoiceID').text();
						}
						console.log('cuinvoiceID  :: '+cuinvoiceID);
						var taxRate =$('#customerInvoice_linetaxId').val();
						taxRate = parseFloat(taxRate.replace(/[^0-9-.]/g, ''));
						var freight = $('#customerInvoice_linefrightID').val();
						freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
						return {'cuInvoiceId':cuinvoiceID,'taxRate' : taxRate,'freight':freight, 'operForAck' : ''  };
					},
					afterSubmit:function(response,postData){
						$("#note").autocomplete("destroy"); 
						$(".ui-menu-item").hide();
						$("a.ui-jqdialog-titlebar-close").show();
						PreloadDataFromInvoiceTable();
						 return [true, loadCustomerInvoice()];
					}
				

						},
						//-----------------------add options----------------------//
						{
						width:400, left:630, top: 550, zIndex:1040,
							closeAfterAdd:true,	reloadAfterSubmit:true,
							modal:true, jqModel:false,
							addCaption: "Add customer Invoice Line Item",
							onInitializeForm: function(form){
								
							},
							afterShowForm: function($form) {

								$(function() { var cache = {}; var lastXhr=''; $("input#itemCode").autocomplete({minLength: 1,timeout :1000,
									source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
										lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
									select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
										if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
									error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
									}); 
								return;
								});
							},
							beforeSubmit:function(postdta, formid) {
								$("#note").autocomplete("destroy");
								 $(".ui-menu-item").hide();
								var aPrMasterID = $('#prMasterId').val();
								if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
								return [true, ""];
							},
							onclickSubmit: function(params){
								var cuinvoiceID = $('#cuinvoiceIDhidden').val();
								if(cuinvoiceID == null || cuinvoiceID == '')
								{
									cuinvoiceID = $('#cuInvoiceID').text();
								}
								console.log('cuinvoiceID  :: '+cuinvoiceID);
								var taxRate =$('#customerInvoice_linetaxId').val();
								taxRate = parseFloat(taxRate.replace(/[^0-9-.]/g, ''));
								var freight = $('#customerInvoice_linefrightID').val();
								freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
								return { 'cuInvoiceId' : cuinvoiceID, 'taxRate' : taxRate,'freight':freight, 'operForAck' : '' };
							},
							afterSubmit:function(response,postData){
								$("#note").autocomplete("destroy");
								$(".ui-menu-item").hide();
								$("a.ui-jqdialog-titlebar-close").show();
								PreloadDataFromInvoiceTable();
								return [true, loadCustomerInvoice()];
							}
						},
						//-----------------------Delete options----------------------//
						{	
							closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
							caption: "Delete",
							msg: 'Do you want to delete the line item?',
							color:'red',

							onclickSubmit: function(params){
								var grid = $("#customerInvoice_lineitems");
								var rowId = grid.jqGrid('getGridParam', 'selrow');
								var cuInvoiceID = grid.jqGrid('getCell', rowId, 'cuInvoiceDetailId');
								var taxRate =$('#customerInvoice_linetaxId').val();
								taxRate = parseFloat(taxRate.replace(/[^0-9-.]/g, ''));
								var freight = $('#customerInvoice_linefrightID').val();
								freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
								return { 'cuInvoiceDetailId' : cuInvoiceID,'taxRate':taxRate,'freight':freight};
								
							},
							afterSubmit:function(response,postData){
								PreloadDataFromInvoiceTable();
								 return [true, loadCustomerInvoice()];
							}
						}
					);
				}

			function formattax(){
				var subTotal = $('#customerInvoice_subTotalID').val(); 
				subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
				var tax = $('#customerInvoice_taxIdcu').val();
				tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
				var frieght = $('#customerInvoice_frightIDcu').val();
				frieght= parseFloat(frieght.replace(/[^0-9-.]/g, ''));
				var total = subTotal+tax+frieght;
				$('#customerInvoice_totalID').val(formatCurrency(total));
			}
			function Loadtab(){
			jQuery( "#salesrelease").dialog({
				autoOpen: false,
				width: 840,
				title:"Sales Order",
				modal: true,
				open: function(){
					setTimeout(function(){
						var new_so_generalform_values=generaltabformvalidation();
					    so_general_form =  JSON.stringify(new_so_generalform_values);
						},1000);
					
			       },
				close: function () {
					$("#note").autocomplete("destroy");
					 $(".ui-menu-item").hide();
					return true;
				}
			});
			}

			function cancelPORelease(){
				jQuery("#porelease").dialog("close");
				jQuery("#salesrelease").dialog("close");
				$("#release").trigger("reloadGrid");
				return true;
			}

			function isExistInArray(array, value) {
				if(jQuery.inArray(value, array) === -1){
					return false;
				} else {
					return true;
				}
			}



			function viewPOAckPDF(){
				var bidderGrid = $("#release");
				var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
				var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
				var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
				var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
				var aInteger = Number(aShipToModeId);
				var jobNumber = $.trim($("#jobNumber_ID").text());
				var rxMasterID = $("#rxCustomer_ID").text();
				var joMasterID = $("#joMaster_ID").text();
				var aQuotePDF = "ack";
				window.open("./purchasePDFController/viewPDFAckForm?vePOID="+vePOID+"&puchaseOrder="+aQuotePDF+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aInteger+"&jobNumber="+ jobNumber+"&joMasterID="+joMasterID);
				return true;
			}
			function addressToShipCustomerInvoice(){
				
				
				var rxMasterId = $("#rxCustomer_ID").text(); 
				operationVar = "bill";
					 $.ajax({
							url: "./salesOrderController/getBilltoAddress",
							type: "GET",
							data : {"customerID" : rxMasterId,"oper" : operationVar},
							success: function(data) {
								console.log('addressToShipCustomerInvoice');
								$('#customerbillToAddressID1cuInvoice').val(data.address1);
								$('#customerbillToAddressID2cuInvoice').val(data.address2);
								$('#customerbillToCitycuInvoice').val(data.city);
								$('#customerbillToStatecuInvoice').val(data.state);
								$('#customerbillToZipIDcuInvoice').val(data.zip);
								}
						});
			}
			
			function FormatCommissionDate(cellvalue, options, rowObject){
				var date = new Date(cellvalue);
				var CreatedOn = date.getDate();
				var createdMonth = date.getMonth()+1; 
				var createdYear = date.getFullYear();
				if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
				if(createdMonth<10){createdMonth='0'+createdMonth;} 
				createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
				return createdDate;
			}
			
			function FormatDate(createdDate){
				var date = new Date(createdDate);
				var CreatedOn = date.getDate();
				var createdMonth = date.getMonth()+1; 
				var createdYear = date.getFullYear();
				if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
				if(createdMonth<10){createdMonth='0'+createdMonth;} 
				createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
				$("#customerInvoice_invoiceDateID").val(createdDate);
			}
			function FormatShipDate(createdDate){
				var date = new Date(createdDate);
				var CreatedOn = date.getDate();
				var createdMonth = date.getMonth()+1; 
				var createdYear = date.getFullYear();
				if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
				if(createdMonth<10){createdMonth='0'+createdMonth;} 
				createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
				$("#customerInvoice_shipDateID").val(createdDate);
			}

			/** Method for adding a Purchase order as customer invoice in release tab. *//*
			function addPOAsCustomerInvoice() {	
				
				PreloadDataInvoiceFromPO(vePOID);
			}*/
			function callSalesOrderStatus(){
				try{
					var dataid = $('#transactionStatus').val();
				    var newDialogDiv = jQuery(document.createElement('div'));
				    jQuery(newDialogDiv).attr("id", "showSalesOrderOptions");
				    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetSalesStatus(this.value)" value="Void"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Quote" onclick="onSetSalesStatus(this.value)" value="Quote"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Hold" onclick="onSetSalesStatus(this.value)" value="OnHold"></span><br><br><span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="Open" onclick="onSetSalesStatus(this.value)" value="Open"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Close" onclick="onSetSalesStatus(this.value)" value="Closed" /></span>');
				  
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 200,
						height : 250,
						title : "Select Order Status",
						buttons : [  ]
					}).dialog("open");
					
					$('div#showSalesOrderOptions').bind('dialogclose', function(event) {
						$("#showSalesOrderOptions").dialog("destroy").remove();
					 });
					console.log('dataid'+dataid);
					  if(dataid=='2'){
						  console.log('dataid inside'+dataid);
						   	$('#Close').css("font-weight","bold");
					    	$('#Close').css("background","#0E2E55");
					    	$('#withPrice').attr('checked',false);
					    	$('#withPriceLine').attr('checked',false);
					    }else  if(dataid=='0'){
						   	$('#Hold').css("font-weight","bold");
					    	$('#Hold').css("background","#0E2E55");
					    	$('#withPrice').attr('checked',false);
					    	$('#withPriceLine').attr('checked',false);
					    }else  if(dataid=='1'){
						   	$('#Open').css("font-weight","bold");
					    	$('#Open').css("background","#0E2E55");
					    	$('#withPrice').attr('checked',false);
					    	$('#withPriceLine').attr('checked',false);
					    }else  if(dataid=='-1'){
						   	$('#Void').css("font-weight","bold");
					    	$('#Void').css("background","#0E2E55");
					    	$('#withPrice').attr('checked',false);
					    	$('#withPriceLine').attr('checked',false);
					    }else  if(dataid=='-2'){
						   	$('#Quote').css("font-weight","bold");
					    	$('#Quote').css("background","#0E2E55");
					    	$('#withPrice').attr('checked',true);
					    	$('#withPriceLine').attr('checked',true);
					    }
					
				}catch(err){
					
					}
				
				
			}

			function onSetSalesStatus(e){
				
				$("#showSalesOrderOptions").dialog("destroy").remove();
				var setStatus=0;
				var cuSoid =  $('#Cuso_ID').text();
				switch(e)
				{
				case 'Void':
					setStatus = -1;
					$('#withPrice').attr('checked',false);
					$('#withPriceLine').attr('checked',false);
					withPrice='NotChecked';
					break;
				case 'Quote':
					setStatus = -2;
					$('#withPrice').attr('checked',true);
					$('#withPriceLine').attr('checked',true);
					withPrice='Checked';
					break;
				case 'OnHold':
					setStatus = 0;
					$('#withPrice').attr('checked',false);
					$('#withPriceLine').attr('checked',false);
					withPrice='NotChecked';
					break;
				case 'Open':
					setStatus = 1;
					$('#withPrice').attr('checked',false);
					$('#withPriceLine').attr('checked',false);
					withPrice='NotChecked';
					break;
				default:
					setStatus = 2;
					$('#withPrice').attr('checked',false);
					$('#withPriceLine').attr('checked',false);
					withPrice='NotChecked';
					break;
				}
				if(setStatus==-1){
					
					 var newDialogDiv = jQuery(document.createElement('div'));
					    jQuery(newDialogDiv).attr("id", "showPDFoptiondialog");
					    jQuery(newDialogDiv).html('<span><label>Do you wish to print a cancel ticket?</label></span>');
					  
						jQuery(newDialogDiv).dialog({
							modal : true,
							width : 200,
							height : 250,
							title : "Cancel Ticket",
							buttons : [{height:35,text: "Yes",click: function() { $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();viewPOPDF();}},
							           {height:35,text: "No",click: function() { $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();}},
							           {height:35,text: "Cancel",click: function() { $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();}}]
						}).dialog("open");
					
				}
				$.ajax({
					url: "./salesOrderController/setSalesOrderStatus",
					type: "POST",
					data :{cusoID:cuSoid,status:setStatus},
					success: function(data) {
						$('#showSalesOrderOptions').dialog('destroy').remove();
						$('#transactionStatus').val(setStatus);
						$('#soStatusButton').val(e);
						
					}
				});
				
			}
		
		////////////
		
		
		function loadCustomerProfitMargin(id){
			$("#CustomerMarginGird").jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./projectscontroller/getProfitMargin?tsUserLoginID='+id+"&rxCustomerID=-1&fromdate=&todate=",
				pager: jQuery('#CustomerMarginGirdPager'),
				colNames:['Customer','Sales','Profit', 'Margin'],
				colModel :[
		           	{name:'customerName', index:'customerName', align:'left', width:140, editable:true,hidden:false},
					{name:'sales', index:'sales', align:'right', width:50,hidden:false,formatter:currencyFormatter},
					{name:'profit', index:'profit', align:'right', width:50,hidden:false,formatter:currencyFormatter},
					{name:'margin', index:'margin', align:'right', width:30,hidden:false},
					],
				rowNum: 50,
				pgbuttons: true,	
				recordtext: '',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: false,
				//pager: '#CustomerMarginGirdPager',
				sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Customer Profit Margin',
				//autowidth: true,
				height:250,	width: 500,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown) {
				    
				},
				ondblClickRow: function(rowId) {
					
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
		    	}
		    }).navGrid('#CustomerMarginGirdPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
			$('#gbox_CustomerMarginGird .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><button id="Button1Id" type="button" class="titlebutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" role="button" onclick="openMarginPopup()">Open</button></div>');

		}
		function loadcreditHoldGrid(){
			var selectedRep = $('#SalesRepComboList').val();
			$("#CreditHoldGrid").jqGrid({
				datatype: 'JSON',
				url:'./projectscontroller/projects_pendingOrder?salesRepId='+projectUserID,
				mtype: 'GET',
				pager: jQuery('#CreditHoldGridPager'),
				colNames:['Customer','Total $ Due','Last Payment', 'Pay $Due'],
				colModel :[
		           	{name:'vePOID', index:'vePOID', align:'left', width:80, editable:true,hidden:false},
					{name:'rxVendorID', index:'rxVendorID', align:'', width:80,hidden:false},
					{name:'joReleaseID', index:'joReleaseID', align:'center', width:80,hidden:false},
					{name:'jobName', index:'jobName', align:'', width:80,hidden:false},
					],
				rowNum: 50,
				pgbuttons: true,	
				recordtext: '',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: false,
				//pager: '#CreditHoldGridPager',
				sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Credit Hold',
				//autowidth: true,
				height:250,	width: 450,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
				loadComplete: function(data) {
					
			    },
				loadError : function (jqXHR, textStatus, errorThrown) {
				    
				},
				ondblClickRow: function(rowId) {
					
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
		    	}
		    }).navGrid('#CreditHoldGridPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
		}
		
		function loadCustomersGrid(projectUserID) {
			try{
				var selectedUser = $('#SalesRepComboList').val();
				
			$("#customersGrid").jqGrid({
				datatype: 'json',
				mtype: 'GET',
				url:'./customerList/getAllCustomerFromLogin?tsUserLoginID='+projectUserID,
				pager: jQuery('#customersGridpager'),
			   	colNames:['rxId', 'Customer', 'Address', 'City', 'State','Phone',''],
			   	colModel:[
					{name:'rxMasterId',index:'rxMasterId', width:170,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'name',index:'name', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
					{name:'address1',index:'address1', width:90,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
					{name:'city',index:'city', width:40,editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'state',index:'state',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'phone1',index:'phone1',align:'center', width:70,editable:true, formatter:phoneFormatter, editrules:{required:true}, editoptions:{size:10}},
					{name:'phone1',index:'phone1',align:'center', width:60,editable:true, editrules:{required:true}, editoptions:{size:10},formatter:contactFormatter}
					],
			   	rowNum: 50,	
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
				caption: null,
				height:400,	width: 950,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
				loadonce: false,
				loadComplete:function(data) {
					//$(".ui-pg-selbox").attr("selected", aCustomerPage);
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
			
			}catch(err){
				alert(err.message);
			}
		}
		
		function loadContactGrid() {
			try{
				
			$("#contactsGrid").jqGrid({
				datatype: 'json',
				mtype: 'POST',
				url:'./getCustomerContacts?rxMasterId='+$('#rxMasterId').val(),
				pager: jQuery('#customersGridpager'),
			   	colNames:['rxId', 'Name', 'Role', 'Direct Line', 'Cell ','Email'],
			   	colModel:[
					{name:'rxContactId',index:'rxContactId', width:100,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'firstName',index:'firstName', width:80,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
					{name:'jobPosition',index:'jobPosition', width:80,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
					{name:'directLine',index:'directLine', width:70,editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'cell',index:'cell',align:'center', width:70,editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'email',index:'email',align:'center', width:100,editable:true, editrules:{required:true}, editoptions:{size:10},formatter:emailFormatter}
					
					],
			   	rowNum: 50,	
				pgbuttons: true,	
				recordtext: '',
				rowList: [50, 100, 200, 500, 1000],
				viewrecords: true,
				pager: '#contactsGridPager',
				sortname: 'name',
				sortorder: "asc",
				altRows: true,
				altclass:'myAltRowClass',
				imgpath: 'themes/basic/images',
				caption: null,
				height:250,	width: 700,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
				loadonce: false,
				loadComplete:function(data) {
					//$(".ui-pg-selbox").attr("selected", aCustomerPage);
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
		    	}
			}).navGrid('#contactsGridPager',
				{add:false,edit:false,del:false,refresh:false,search:false}
			);
			}catch(err){
				alert(err.message);
			}
		}
		
function checkpermission_project(){
			var UserLoginID=$("#userLogin_Id").val();
			$.ajax({
			    url: "./getSysPrivilage", 
			    data: {'accessPage':'Project_Filter','userGroupID':0, 'UserLoginID':UserLoginID},
			    type: 'POST',
			    success: function(data){
			    	if(data.Value == "granted")
			    	{
			    		return true;
			    	}
			    	else
			    		showDeniedPopup();
			    }, error: function(error) {
			    	showDeniedPopup();
				}
			});
		}
function changeSalesrep() {
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Sales, userGroupID = 0 , UserLoginID = '+UserLoginID);
	$.ajax({
	    url: "./getSysPrivilage",
	    data: {'accessPage':'Project_Filter','userGroupID':0, 'UserLoginID':UserLoginID},
	    type: 'POST',
	    success: function(data){
	    	if(data.Value == "granted")
	    	{
	    		var salesid = jQuery("#SalesRepComboList").val();
	    		$("#customersGrid").jqGrid('GridUnload');
	    		loadCustomersGrid(salesid);
	    		$("#customersGrid").trigger("reloadGrid");
	    		
	    		
	    		$("#CustomerMarginGird").jqGrid('GridUnload');
	    		loadCustomerProfitMargin(salesid);
	    		$("#CustomerMarginGird").trigger("reloadGrid");
	    		
	    		$("#openJobsGrid").jqGrid('GridUnload');
	    		loadOpenJobs(salesid);
	    		$("#openJobsGrid").trigger("reloadGrid");
	    		
	    		$("#SalesPurchaseOrdersGrid").jqGrid('GridUnload');
	    		loadSalesPurchaseGrid(salesid);
	    		$("#SalesPurchaseOrdersGrid").trigger("reloadGrid");
	    		
	    		$('#Booked').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	    		$('#Booked').trigger("reloadGrid");
	    		
	    		$('#pendingOrderGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	    		$('#pendingOrderGrid').trigger("reloadGrid");
	    		
	    		$('#PendingCreditGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	    		$('#PendingCreditGrid').trigger("reloadGrid");
	    		
	    		$('#dormantProjectsGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	    		$('#dormantProjectsGrid').trigger("reloadGrid");
	    		
	    		$('#orderTrackingGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	    		$('#orderTrackingGrid').trigger("reloadGrid");
	    		getCustomerARDetail(salesid);
	    		return true;
	    	}
	    	else
	    		showDeniedPopup();
	    }, error: function(error) {
	    	showDeniedPopup();
		}
	});
}

function getSalesReps(selecteduserId) {
	var getSelected = 0;
	$.ajax({
		url: "./salescontroller",
		success: function(data){
			var aSalesRepOptions ='<option value="'+0+'">All</option>';
			$.each(data, function(key, value) { 
				
				aSalesRepOptions = aSalesRepOptions + '<option value="'+value.userId+'">'+value.userName+'</option>';
				if(selecteduserId==value.userId){
					
					getSelected = value.userId;
				}
				
			});
			$("#SalesRepComboList").append(aSalesRepOptions);
			
			$("#SalesRepComboList option[value=" +getSelected + "]").attr("selected", true);
			loadOpenJobs(getSelected);
			loadSalesPurchaseGrid(getSelected);
			loadCustomerProfitMargin(getSelected);
			loadCustomersGrid(getSelected);
			getCustomerARDetail(getSelected);
			var sysadmin = $("#userLoginID").val();
			if(sysadmin !=1){
//				$("#SalesRepComboList").attr("disabled", "disabled");
			}
			
		},
		error: function(Xhr) {
			
		}
	});	 
	
}

function getQryStr(jobNumber, jobName,joMasterID) {
	var qryStr = "";
	$.ajax({
		async: false,
		url : "./search/searchjobcustomer",
		mType : "GET",
		data : {
			'jobnumber' : jobNumber,
			'jobname' : jobName,
			'joMasterID':joMasterID
		},
		success : function(data) {
			var jobCustomer = "";
			var status = "";
			$.each(data, function(index, value) {
				status = value.jobStatus;
				jobCustomer = value.customerName;
			});
			if (jobCustomer === null) {
				jobCustomer = "";
			}
			updateJobstatus(jobNumber,status,joMasterID);
			qryStr = "jobNumber=" + jobNumber	+ "&jobName=" + jobName + "&jobCustomer=" + jobCustomer	+"&joMasterID="+joMasterID;
			return qryStr;
		},
		error : function(Xhr) {
		}
	});
	return qryStr;
}

function isExistInArray(array, value) {
	if(jQuery.inArray(value, array) === -1){
		return false;
	} else {
		return true;
	}
}

function removeA(arr){
    var what, a = arguments, L = a.length, ax;
    while(L> 1 && arr.length){
        what = a[--L];
        while((ax= arr.indexOf(what)) != -1){
            arr.splice(ax, 1);
        }
    }
    return arr;
}

function formatCurrency(strValue)
{
	if(strValue == "" || strValue == null){
		return "$0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10)
		strCents = "0" + strCents;
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++)
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
}
/***
 * Customer Autocomplete function
 * */



$(function() { var cache = {}; var lastXhr='';
$( "#customerName" ).autocomplete({
	minLength: 2, timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerId").val(id);
	var selectedUser = $('#SalesRepComboList').val();
	
	$("#openJobsGrid").jqGrid('GridUnload');
	loadOpenJobs(selectedUser);
	$("#openJobsGrid").trigger("reloadGrid");
	
	
	$("#SalesPurchaseOrdersGrid").jqGrid('GridUnload');
	loadSalesPurchaseGrid(selectedUser);
	$("#SalesPurchaseOrdersGrid").trigger("reloadGrid");
	
	 // reloadPoSOPopup();
	/*$("#openJobsGridpop").jqGrid('GridUnload');
	loadOpenJobsPopup(selectedUser);
	$("#openJobsGridpop").trigger("reloadGrid");*/
	//Set the sales man and csr based upon customer .Call to job controller java
	
	},
	source: function( request, response ) { var term = request.term;
		if( term in cache ){ response( cache[ term ] ); return; }
		lastXhr = $.getJSON("customerList/customerNameListEmployee?tsUserLoginID="+$('#SalesRepComboList').val(),request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});
});

$(function() { var cache = {}; var lastXhr='';
$( "#ardetailcustomername" ).autocomplete({
	minLength: 2, timeout :1000,
	select: function( event, ui ) { 
	var id = ui.item.id; 
	$("#ardetailscustomerid").val(id);
	
	//$("#customerId").val(id);
	//var selectedUser = $('#SalesRepComboList').val();
	
	
	
	},
	source: function( request, response ) { var term = request.term;
		if( term in cache ){ response( cache[ term ] ); return; }
		lastXhr = $.getJSON("customerList/customerNameListEmployee?tsUserLoginID="+$('#SalesRepComboList').val(),request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});
});

/****
 * Clear customer details and customer name
 * 
 */

function clearCustomer(){
	$('#customerName').val('');
	$('#customerId').val('');
	var selectedUser = $('#SalesRepComboList').val();
	
	
	$("#openJobsGrid").jqGrid('GridUnload');
	loadOpenJobs(selectedUser);
	$("#openJobsGrid").trigger("reloadGrid");
	
	$("#SalesPurchaseOrdersGrid").jqGrid('GridUnload');
	loadSalesPurchaseGrid(selectedUser);
	$("#SalesPurchaseOrdersGrid").trigger("reloadGrid");
	
	
	/*$("#openJobsGridpop").jqGrid('GridUnload');
	loadOpenJobsPopup(selectedUser);
	$("#openJobsGridpop").trigger("reloadGrid");*/
}

/***
 * Created by: Praveen kumar
 * date : 2014-09-23
 * Purpose: Magnify icon click function
 * 
 */
function customerRolodex(){
	var checkpermission=getGrantpermissionprivilage('Customers',0);
	if(checkpermission){
	var rxNumber = $('#customerId').val();
	var name = $('#customerName').val();
	
		if(rxNumber!='' && name!=''){
			var name1 = name.replace('&', 'and');
			var name2 = name1.replace('&', 'and');
			document.location.href = "./customerdetails?rolodexNumber="+rxNumber+"&name="+'`'+name2+'`';
		}
	}
}

/***
 * Created by: Praveenkumar
 * Date : 2014-09-23
 * Purpose : show customer List in Popup
 * 
 */
function showCustomerList(){

	jQuery('#CustomerList').dialog(
			{
				modal : true,
				width : 970,
				height : 550,
				title : "Customer List",
				 create: function (event, ui) {
					 dialogClass: 'myTitleClass';
			$("#CustomerList").parent().find(".ui-dialog-titlebar").addClass("mySpecialClass");
			$(".mySpecialClass").html('Customer List<span style="float:right; cursor:pointer;padding-left: 10px;" id="myCloseIcon">X</span><span style="float:right; cursor:pointer;"><img alt="Export Csv" src="./../resources/images/csv_text.png" onclick="exportCustomerContacts()" width="25"></span>');
				 }

			}).dialog("open");
	
	
	    
	
	$("span#myCloseIcon").click(function() {

		$( "#CustomerList" ).dialog( "close" );
		});
	
}

function phoneFormatter(cellvalue, options, rowObject) {
	if(cellvalue != null && cellvalue!='null'){
		return formatPhone(cellvalue);	
	}else{
		return '';
	}
	
}
function currencyFormatter(cellvalue, options, rowObject) {
	if(cellvalue != null && cellvalue!='null'){
		return formatCurrency(cellvalue);	
	}else{
		return '';
	}
	
}
function contactFormatter(cellvalue, options, rowObject){
	var element = "<input type='button' id ='getCustomerContacts' style='background: #758fbd;width: 75px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;' value='Contacts' onclick='showContacts("+options.rowId+")'>";
	return element ; 
}

function emailFormatter(cellvalue, options, rowObject){
	var element = "<a href='mailto:"+cellvalue+"' target='_top' style='color:rgb(41, 52, 134);'>"+cellvalue+"</a>";
	return element ; 
}
function showContacts(rowId){
	
	var rows = jQuery("#customersGrid").getDataIDs();
	var row=jQuery("#customersGrid").getRowData(rows[rowId-1]);
	var rxMasterId =row['rxMasterId'];
	
	customerName = row['name'];
	$('#rxMasterId').val(rxMasterId);
	
	$("#contactsGrid").jqGrid('GridUnload');
	loadContactGrid();
	$("#contactsGrid").trigger("reloadGrid");
	
	
	jQuery('#ContactsList').dialog(
			{
				modal : true,
				width : 750,
				height : 380,
				title : "Contacts",
			
			}).dialog("open");
}

function exportCustomerContacts()
{
	createtpusage('Project Screen','Customer List','Info','Project Screen,Customer List Grid View,Generating CSV Customer List Report');
	window.open("./companycontroller/printCustomerListEmployeeCSV?tsUserLoginID="+$('#SalesRepComboList').val());
	return false;
	
}

function getCustomerARDetail(userLoginID){
	try{
		
		$("#loaderforproject").show();	
	$.ajax({
		url: "./projectscontroller/getCustomerARDetails?tsUserLoginID="+userLoginID,
		success: function(data){
			
		$('#currentar').text(formatCurrency(data.ar));
		$('#thirty').text(formatCurrency(data.thirty));
		$('#sixty').text(formatCurrency(data.sixty));
		$('#ninty').text(formatCurrency(data.ninty));
		var total = Number(data.ar+data.thirty+data.sixty+data.ninty);
		$('#artotal').text(formatCurrency(total));
		$('#marginvalue').text((data.margin));
		$('#ytd').text(formatCurrency(data.ytd));
	//	$('#priorytd').text(formatCurrency(data.priorytd));
		$('#priorytd').text(formatCurrency('0'));
		
		$("#loaderforproject").hide();	
		
		},
		error: function(Xhr) {
			
		}
	});	 
}catch(err){
	
}
	
}
function openMarginPopup(){
	loadCustomerProfitMarginPopup(projectUserID);
	jQuery('#CustomerMarginGirdpopup').dialog(
				{
					modal : true,
					width : 980,
					height : 610,
					title : "Customer Profit Margin",
					 create: function (event, ui) {
	                     dialogClass: 'myTitleClass';
	                     $("#CustomerMarginGirdpopup").parent().find(".ui-dialog-titlebar").addClass("mySpecialClass");
	         $(".mySpecialClass").html('<div>Customer Profit Margin<input type="checkbox" id="daterange" name="daterange" />Date Range:<input type="text" id="rangefromdate" placeholder="From date" onclick="setFocus(1)"/>To:<input type="text"placeholder="To Date" id="todate" onclick="setFocus(2)" /><span id ="closedialog" style="float:right;cursor:pointer;color:white;font-size:18px" onClick="closeDialogMargin()">X</span></div>');
	     },
				}).dialog("open");
	
	$('#rangefromdate').datepicker();
	$('#todate').datepicker();
				}
function openJobsDialog(){
	loadOpenJobsPopup(projectUserID,-1);
	//openJobsGrid
	jQuery('#openjobsforpopup').dialog(
			{
				modal : true,
				width : 1020,
				height : 600,
				title : "Opened Jobs",
				 create: function (event, ui) {
                     dialogClass: 'myTitleClass';
                     $("#openjobsforpopup").parent().find(".ui-dialog-titlebar").addClass("mySpecialClass");
         $(".mySpecialClass").html('<div>Opened Jobs<input type="checkbox" id="daterange" name="daterange" />Date Range:<input type="text" id="rangefromdate" placeholder="From date" onclick="setFocus(1)"/>To:<input type="text"placeholder="To Date" id="todate" onclick="setFocus(2)" />Customer : <input type="text" id="openJobscustomerName" placeholder="Minimum 3 characters required" onclick="setFocus(3)" /><input type="hidden" id="openJobscustomerId" style="float:right;background: #758fbd;width: 60px;height: 20px;font-weight: bold;font-size: 12px;color:white !important;" /><input type="button" style="background: #758fbd;width: 60px;height: 25px;font-weight: bold;font-size: 12px;color:white !important;" value="Clear" onclick="clearpopuptext()" /><span id ="closedialog" style="float:right;cursor:pointer;color:white;font-size:18px" onClick="closeDialog()">X</span></div>');
     },
			}).dialog("open");
	
	$(function() { var cache = {}; var lastXhr='';
	$( "#openJobscustomerName" ).autocomplete({
		minLength: 2, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id;$("#openJobscustomerId").val(id);
		var selectedUser = $('#SalesRepComboList').val();
		reloadOpenJobsPopup();
		
		},
		source: function( request, response ) { var term = request.term;
			if( term in cache ){ response( cache[ term ] ); return; }
			lastXhr = $.getJSON("customerList/customerNameListEmployee?tsUserLoginID="+$('#SalesRepComboList').val(),request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
	});
	});
$('#rangefromdate').datepicker();
$('#todate').datepicker();

$( "#rangefromdate" ).change(function() {
	reloadOpenJobsPopup();
	
	});
$( "#todate" ).change(function() {
	reloadOpenJobsPopup();
	});
$('#openjobsforpopup').bind('dialogclose', function(event) {
	
	$('#rangefromdate').val('');
	$('#todate').val('');
	$( "#openJobscustomerName" ).val('');
	$("#openJobscustomerId").val('');
	var selectedUser = $('#SalesRepComboList').val();
	$("#openJobsGridpop").jqGrid('GridUnload');
	loadOpenJobsPopup(selectedUser,-1);
	$("#openJobsGridpop").trigger("reloadGrid");
	
});
}



function openPoSoDialog(){
	loadPoSoPopup(projectUserID,-1);
	jQuery('#openPOSODialog').dialog(
			{
				modal : true,
				width : 1000,
				height : 590,
				title : "Opened Jobs",
				 create: function (event, ui) {
                     dialogClass: 'myTitlePoSoClass';
                     $("#openPOSODialog").parent().find(".ui-dialog-titlebar").addClass("myPoSOClass");
         $(".myPoSOClass").html('<div>Open Purchase Order/Sales Order<input type="checkbox" id="daterangePoSo" name="daterangePoSo" />Date Range:<input type="text" id="rangefromdatePoSo" placeholder="From date" style="width:80px" onclick="setFocusPoSo(1)"/>To:<input type="text"placeholder="To Date" style="width:80px" id="todatePoSo" onclick="setFocusPoSo(2)" />Customer : <input type="text" id="openJobscustomerNamePoSo" placeholder="Minimum 3 characters required" onclick="setFocusPoSo(3)" /><input type="hidden" id="openJobscustomerIdPoSo"  /><input type="button" onclick="clearpopuptextPoSo()" value="Clear" style="background: #758fbd;width: 60px;height: 25px;font-weight: bold;font-size: 12px;color:white !important;"/><span id ="closedialogPoSo" style="float:right;cursor:pointer;color:white;" onClick="closePoSoDialogPoSo();"><font size="4" color="White">X</font></span></div>');
         reloadPoSOPopup();
     },
     
			}).dialog("open");
	
	$(function() { var cache = {}; var lastXhr='';
	$( "#openJobscustomerNamePoSo" ).autocomplete({
		minLength: 2, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id;$("#openJobscustomerIdPoSo").val(id);
		var selectedUser = $('#SalesRepComboList').val();
		reloadPoSOPopup();
		
		},
		source: function( request, response ) { var term = request.term;
			if( term in cache ){ response( cache[ term ] ); return; }
			lastXhr = $.getJSON("customerList/customerNameListEmployee?tsUserLoginID="+$('#SalesRepComboList').val(),request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
	});
	});
$('#rangefromdatePoSo').datepicker();
$('#todatePoSo').datepicker();

$( "#rangefromdatePoSo" ).change(function() {
	reloadPoSOPopup();
	});
$( "#todatePoSo" ).change(function() {
	reloadPoSOPopup();
	});
$('#openPOSODialog').bind('dialogclose', function(event) {
	
	$('#rangefromdatePoSo').val('');
	$('#todatePoSo').val('');
	$( "#openJobscustomerNamePoSo" ).val('');
	$("#openJobscustomerIdPoSo").val('');
	var selectedUser = $('#SalesRepComboList').val();
	$("#poSoGrid").jqGrid('GridUnload');
	loadPoSoPopup(selectedUser,-1);
	$("#poSoGrid").trigger("reloadGrid");
	
});
}


function reloadPoSOPopup(){
	var selectedUser = $('#SalesRepComboList').val();
	
	var id = $("#openJobscustomerIdPoSo").val();
	if(id == undefined || id == ''){
		id = -1;
	}
	
	$("#poSoGrid").jqGrid('GridUnload');
	loadPoSoPopup(selectedUser,id);
	$("#poSoGrid").trigger("reloadGrid");
}

function loadPoSoPopup(userId,cusID){

	var rxMsaterId = -1;
	if($('#customerName').val()!=''){
		rxMsaterId  = $('#customerId').val();
	}
	
	var fromdatePoSo = $('#rangefromdatePoSo').val();
	if(fromdatePoSo == undefined){
		fromdatePoSo = '';
	}
	var todatePoSo= $('#todatePoSo').val();
	if(todatePoSo == undefined){
		todatePoSo = '';
	}
	console.log('Test Here-POPup');
	try{
	
		$("#load_poSoGrid").show();
		
	$("#poSoGrid").jqGrid({
		datatype: 'JSON',
		url:'./projectscontroller/getPurchaseSalesOrder?rxCustomerID='+cusID+'&tsUserLoginID='+userId+'&fromdate='+fromdatePoSo+'&todate='+todatePoSo,
		mtype: 'POST',
		pager: '#poSoGridPager',
		colNames:['vepoID','rxVendorId', 'joReleaseId', 'OrderDate','Order #','Vendor', 'Vendor #','Ship Date'],
		colModel :[				           
           	{name:'vePoid', index:'vePoid', align:'left', width:80, editable:true,hidden:true},
        	{name:'rxVendorId', index:'rxVendorId', align:'left', width:80, editable:true,hidden:true},
           	{name:'joReleaseId', index:'joReleaseId', align:'left', width:80, editable:true,hidden:true},
			{name:'dateWanted', index:'dateWanted', align:'', width:101,hidden:false},
			{name:'ponumber', index:'ponumber', align:'center', width:114,hidden:false},
			{name:'vendorName', index:'vendorName', align:'', width:452,hidden:false},
			{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'', width:133,hidden:false},
			{name:'shipDate', index:'shipDate', align:'', width:185,hidden:false}],
									
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: false,
		//pager: '#SalesPurchaseOrdersGridPager',
		sortname: 'dateWanted', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: null,
		//autowidth: true,
		height:472,	width: 972,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
				
		loadComplete: function(data) {
			$("#load_poSoGrid").hide();
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		
		ondblClickRow: function(rowId) {
			
			var rows = jQuery("#poSoGrid").getDataIDs();
			var row = jQuery("#poSoGrid").getRowData(rows[rowId-1]);
			var vePoid = row['vePoid'];
			var rxVendorId = row['rxVendorId'];
			var ponumber = row['ponumber'];
			var vendorName = row['vendorName'];
			var vendorOrderNumber = row['vendorOrderNumber'];
			var joReleaseId = row['joReleaseId'];
			var JobNumber = "", jobCustomerName_ID = "", project_aMasterID = "";
			if(vendorName.indexOf("Warehouse") == 0)
			{
				$.ajax({
					url:"./projectscontroller/getJoMasterData?joReleaseId="+joReleaseId,
					type : "GET",
					async:false,
					success : function(data) {
						var dd = data.split("@");
						JobNumber = dd[0];
						jobCustomerName_ID = dd[1];
						project_aMasterID = dd[2];
						
						$("#jobNumber_ID").val(dd[0]);
						$("#jobCustomerName_ID").val(dd[1]);
						$("#jobName_ID").val(dd[3]);
						$("#Cuso_ID").text(vePoid);
					}
				});
				//document.location.href ="./salesorder?cusoid="+vePoid+"&customerid="+project_aMasterID+"&oper=projectSO";
				document.location.href ="./salesorder?oper=create";
			}
			if(vendorName.indexOf("Warehouse") < 0)
			{
				window.location.href = "./editpurchaseorder?token=view&aVePOId=" + vePoid;
			}
			
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
    	}
    }).navGrid('#poSoGridPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	}catch(err){
		alert(err.message);
	}
}

function reloadOpenJobsPopup(){
	var selectedUser = $('#SalesRepComboList').val();
	
	var id = $("#openJobscustomerId").val();
	if(id == undefined || id == ''){
		id = -1;
	}
	$("#openJobsGridpop").jqGrid('GridUnload');
	loadOpenJobsPopup(selectedUser,id);
	$("#openJobsGridpop").trigger("reloadGrid");
}
function loadOpenJobsPopup(userLoginID,customerId){
	var rxMsaterId = -1;
	if($('#customerName').val()!=''){
		rxMsaterId  = $('#customerId').val();
	}
	var fromdate = $('#rangefromdate').val();
	if(fromdate == undefined){
		fromdate = '';
	}
	var todate= $('#todate').val();
	if(todate == undefined){
		todate = '';
	}
	var LoginID=$("#SalesRepComboList").val();
	
	$("#openJobsGridpop").jqGrid({
		datatype: 'JSON',
		url:'./projectscontroller/getOpenedJobs?rxCustomerID='+customerId+'&tsUserLoginID='+LoginID+'&todate='+todate+'&fromdate='+fromdate,
		mtype: 'POST',
		pager: jQuery('#openJobsGridPagerpop'),
		colNames:['joMasterID','rxMasterID','rxCustomerID','Customer', 'Job Name','Job#','$Amount','Released/UnInvoiced','Released/Invoiced','Unreleased'],
		colModel :[
		    {name:'joMasterId', index:'joMasterId', align:'left', width:80, editable:true,hidden:true},
           	{name:'rxMasterID', index:'rxMasterID', align:'left', width:80, editable:true,hidden:true},
			{name:'rxCustomerID', index:'rxCustomerID', align:'', width:80,hidden:true},
			{name:'customerName', index:'customerName', align:'center', width:120,hidden:false},
			{name:'jobName', index:'jobName', align:'', width:90,hidden:false},
			{name:'jobNumber', index:'jobNumber', align:'', width:40,hidden:false},
			{name:'contractAmount', index:'contractAmount', align:'right', width:40,hidden:false,formatter:currencyFormatter},
			{name:'allocated', index:'allocated',  align:'right', width:80,hidden:false,formatter:currencyFormatter},
			{name:'invoiced', index:'invoiced',  align:'right', width:80,hidden:false,formatter:currencyFormatter},
			{name:'unreleased', index:'unreleased',  align:'right', width:80,hidden:false,formatter:currencyFormatter}
			
			],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: false,
		pager: '#openJobsGridPagerpop',
		sortname: 'joMasterID', sortorder: "desc",	imgpath: 'themes/basic/images',	caption:null,
		//autowidth: true,
		height:480,	width: 950,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rows = jQuery("#openJobsGridpop").getDataIDs();
			row=jQuery("#openJobsGridpop").getRowData(rows[rowId-1]);
			var jobName = row['jobName'];
			var jobNumber = row['jobNumber'];
			var joMasterID=row['joMasterId'];
			var urijobname=encodeBigurl(jobName);
			window.location.href = './jobflow?token=view&jobNumber='+jobNumber+'&jobName='+urijobname+"&joMasterID="+joMasterID;
			
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
    	}
    }).navGrid('#openJobsGridPagerpop',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	
	//$('#gbox_openJobsGridPagerpop .ui-jqgrid-title').after('<div id="jqGridButtonDiv"><input type="checkbox" name="daterange" /><input type="text" id="fromdate" /><input type="text" id="todate" /><input type="text" id="openJobscustomerName" placeholder="Minimum 3 characters required"/></div>');
}

//ContactsList

function setFocusPoSo(id){
	
	
	if(id == 1){
		if($('#daterangePoSo').is(":checked")==true){
			$('#rangefromdatePoSo').focus();	
			
		}
		
	}if(id == 2){
		if($('#daterangePoSo').is(":checked")==true){
			$('#todatePoSo').focus();	
		}
		
	}if(id == 3){
		$('#openJobscustomerNamePoSo').focus();
		//$('#openJobscustomerNamePoSo').setCursorPosition(0);
	}
		
	}




function setFocus(id){
	
	
if(id == 1){
	if($('#daterange').is(":checked")==true){
		$('#rangefromdate').focus();	
	}
	
}if(id == 2){
	if($('#daterange').is(":checked")==true){
		$('#todate').focus();	
	}
	
}if(id == 3){
	$('#openJobscustomerName').focus();
	//$('#openJobscustomerName').setCursorPosition(0);
}
	
}

function setFocusCommissionStatement(){
//	$('#endperioddate').focus();
	console.log("test");
//	$('#testcommissioncheckbox').focus();	
}

function closePoSoDialogPoSo(){
	jQuery('#openPOSODialog').dialog("close");	
}

function closeDialog(){
	jQuery('#openjobsforpopup').dialog("close");
	
}

function clearpopuptextPoSo(){
	$('#rangefromdatePoSo').val('');
	$('#todatePoSo').val('');
	$('#openJobscustomerIdPoSo').val('');
	$('#openJobscustomerNamePoSo').val('');
	$('#daterangePoSo').attr('checked', false); 
	reloadPoSOPopup();	
}
function closeDialogMargin(){
	jQuery('#CustomerMarginGirdpopup').dialog("close");	
}

function clearpopuptext(){
	$('#rangefromdate').val('');
	$('#todate').val('');
	$('#openJobscustomerId').val('');
	$('#openJobscustomerName').val('');
	$('#daterange').attr('checked', false); 
	reloadOpenJobsPopup();	
}

function loadCustomerProfitMarginPopup(id){
	/*var rxMsaterId = -1;
	if($('#customerName').val()!=''){
		rxMsaterId  = $('#customerId').val();
	}
	var fromdate = $('#rangefromdate').val();
	if(fromdate == undefined){
		fromdate = '';
	}
	var todate= $('#todate').val();
	if(todate == undefined){
		todate = '';
	}*/
	
	var selid=$("#SalesRepComboList").val();
	$("#CustomerMarginGirdpop").jqGrid('GridUnload');
	$("#CustomerMarginGirdpop").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		url:'./projectscontroller/getProfitMargin?tsUserLoginID='+selid+"&rxCustomerID=-1&fromdate=&todate=",
		pager: '#CustomerMarginGirdPagerpop',
		colNames:['Customer','Sales','Profit', 'Margin','LYSales','LYProfit'],
		colModel :[
           	{name:'customerName', index:'customerName', align:'left', width:150, editable:true,hidden:false},
			{name:'sales', index:'sales', align:'right', width:60,hidden:false,formatter:currencyFormatter},
			{name:'profit', index:'profit', align:'right', width:60,hidden:false,formatter:currencyFormatter},
			{name:'margin', index:'margin', align:'right', width:60,hidden:false},
			{name:'margin1', index:'margin', align:'', width:60,hidden:false},
			{name:'margin2', index:'margin', align:'', width:60,hidden:false},
			],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: false,
		//pager: '#CustomerMarginGirdPagerpop',
		sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption:null,
		//autowidth: true,
		height:500,	width: 950,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			
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
    	}
    }).navGrid('#CustomerMarginGirdPagerpop',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
}

function showErrorMessage(errorText){
	
	var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				}