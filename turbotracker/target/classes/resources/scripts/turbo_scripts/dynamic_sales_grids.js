var salesUserId = $("#salesRepUserID").val();	
	getSalesReps(salesUserId);
		$(window).bind('load', function() {
			$('#upcoming').bubbletip($('#tip1_down'), {
				deltaDirection: 'down',
				offsetTop: 115	
			});
		});
		
		jQuery(document).ready(function(){
			/*$('.ui-dialog-titlebar').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
			$('.ui-dialog-titlebar').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
			$('.ui-dialog-titlebar').css('color','black');
			$('.mySpecialClass').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
			$('.mySpecialClass').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
			$('.mySpecialClass').css('color','black');
			$('.ui-widget-header').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
			$('.ui-widget-header').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
			$('.ui-widget-header').css('color','black');*/
			//$('.ui-dialog').css('padding','0.7em 1em');
			$('.ui-dialog-titlebar').css('padding','0.7em 1em');
			
			/*$('#bidListFromDate').datepicker().datepicker("setDate", new Date());
			$("#bidListToDate").datepicker().datepicker("setDate", new Date());*/
			
			$("#bidListFromDate").datepicker({
					 onSelect: function (selected) {
						 $('#errorStatus').html("Note: Date range is restricted to 1 month.");
						 setTimeout(function(){
								$('#errorStatus').html("");
								},3000);
					 }
					 });
	               
              /*  onSelect: function (selected) {
                   var dt = new Date(selected);
                   $("#bidListToDate").datepicker();
                    $("#bidListToDate").datepicker("setDate", dt);
                    $("#bidListToDate").datepicker("option", "minDate", 0);
                    $("#bidListToDate").datepicker("option", "maxDate", 30);
                }
            });*/
			
			$("#bidListToDate").datepicker({
			    beforeShow: function() {
			        var startDate = $("#bidListFromDate").datepicker('getDate');
			        var minDate = $("#bidListFromDate").datepicker('getDate');
			        if (startDate != null) {
			        	
			        	$(this).datepicker('setDate',startDate);
			        	minDate.setDate(minDate.getDate());
			        	$(this).datepicker('option', 'minDate',minDate);
			            startDate.setDate(startDate.getDate()+30);
			            $(this).datepicker('option', 'maxDate',startDate);
			        }
			    }
			});
			
			
			
			
			//$('#bidListFromDate').datepicker({ dateFormat: 'yy-mm-dd' }).val();
			//$('#bidListToDate').datepicker({ dateFormat: 'yy-mm-dd' }).val();
			
			addRemovePendingGridColumns();
			$('#search').hide();
			$("#intro").hide();
			//$("input:button").button();
			setTimeout("loadSalesGrid();", 1000);
			//$("#").removeClass("ui-corner-all");
			var createdLogin = '';
			var changedLogin = '';
			var rxMasterID = '';
			loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID);
			loadViewsGrid();
		});
		
		var localvalues= [{id:"1",viewsname:"Bid List"}];
		
		function loadViewsGrid(){

			$("#viewsGrid").jqGrid({
							datatype: 'local',
							mtype: 'POST',
							data:localvalues,
							pager: jQuery('#viewsGridPager'),
							colNames:['ID',''],
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
							sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: null,
							//autowidth: true,
							height:100,	width: 200,/*scrollOffset:0,*/ rownumbers:false, altRows: false, altclass:'myAltRowClass', rownumWidth: 45,
							loadComplete: function(data) {
								
						    },
							loadError : function (jqXHR, textStatus, errorThrown) {
							    
							},
							ondblClickRow: function(rowId) {
								if(rowId==1){
									loadBidList();
									$("#AccountReceivableDiv").dialog("open");
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
		
		function loadBidList(){
		
		$.ajax({
	        url: './getUserDefaults',
	        type: 'POST',       
	        success: function (data) {
	        	var selected='';
	        	var blDivision = "<option value='0'>All</option>";
				$.each(data, function(key, valueMap) {
					if("divisions"==key)
					{
						$.each(valueMap, function(index, value){
							//if(data.divisionID==value.coDivisionId){selected="selected";}else{selected="";}
							if(value.description != null && value.description.trim() != '')
								blDivision+='<option value='+value.coDivisionId+' '+selected+'>'+value.description+'</option>';
						
						}); 
					}
				});

				$('#blDivisionID').html(blDivision);
				
	        }
	    });
		
		jQuery("#addBidListDialog").dialog("open");	
		document.getElementById("bidListFormId").reset();
		
		}
		
		jQuery( function(){
			jQuery("#addBidListDialog").dialog({
				autoOpen:false,
				height:230,
				width:600,
				title:"Bid List",
				modal:true,
				close:function(){
					$('#bidListFormId').validationEngine('hideAll');
					return true;
				}
			});
		});
		
		var newDialogDiv = jQuery(document.createElement('div'));
		
		function generateBidListPdf(){
			createtpusage('Sales Screen','Bidlist View','Info','Sales,BidList,Generate Report');
			var includeEngineer = false;
			var biddetails = false;
			var blDivisionID = $("#blDivisionID").val();
			var bidListFromDate = $("#bidListFromDate").val();
			var bidListToDate = $("#bidListToDate").val();
			if($('#blIncludeEngineer').is(':checked'))
			 includeEngineer = true;
			if($('#blIncludeJobBid').is(':checked'))
				biddetails = true;
			if( bidListFromDate.length==0  || bidListToDate.length== 0){
				errorText = "Please select  Dates.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			createtpusage('Sales Screen','Bidlist View','Info','Sales,BidList,Generating Reports with DivisionID:'+blDivisionID+',Dates:'+bidListFromDate+' '+bidListToDate);
			window.open("./salescontroller/getBidListPdf?blDivisionID="+blDivisionID+"&bidListFromDate="+bidListFromDate+"&bidListToDate="+bidListToDate+"&includeEngineer="+includeEngineer+
					"&biddetails="+biddetails);

			/*$.ajax({
				type: "GET",
				url: "./salescontroller/getBidListPdf",
				data: { 'blDivisionID': blDivisionID,'bidListFromDate': bidListFromDate,'bidListToDate': bidListToDate},
				dataType: "json",
				success: function (data) {
					
					window.open("./salescontroller/getBidListPdf?blDivisionID="+blDivisionID+"&bidListFromDate="+bidListFromDate+"&bidListToDate="+bidListToDate);
				},
				error: function (msg) {
					
				}
			});*/
		}
		
		function cancelBidList(){
			createtpusage('Sales Screen','Bidlist View Cancel','Info','Sales,BidList,Cancelling BidList');
			jQuery("#addBidListDialog").dialog("close");	
		}
		
		$("#blIncludeEngineer").click(function() {
		    $("#blIncludeEngineerlabel").text(this.checked ? "(yes)" : "(no)");
		});
		
		$("#blIncludeJobBid").click(function() {
		    $("#blIncludeJobBidlabel").text(this.checked ? "(yes)" : "(no)");
		});
		
		$("#blGroupByDiv").click(function() {
		    $("#blGroupByDivlabel").text(this.checked ? "(yes)" : "(no)");
		});
		
		
		function formatCurrency(strValue)
		{
			if(strValue === "" || strValue === null){
				return "$0.00";
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
			for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++)
				dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
				dblValue.substring(dblValue.length-(4*i+3));
			return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
		}

		jQuery(function () {
			jQuery( "#customerDialogBox" ).dialog({
				autoOpen: false,
				height: 330,
				width: 730,
				title:"Change Order ",
				modal: true,
				buttons:{	},
				close: function () {
					return true;
				}
			});
		});
		
		function customerFilterDialog(){
			jQuery( "#customerDialogBox" ).dialog( "open" );
		}

		$(function() { var cache = {}; var lastXhr = '';
		$( "#customerFilterListID" ).autocomplete({ minLength: 3,timeout :1000,
			select: function (event, ui) {
				var rxMasterID = ui.item.id; 
				/*$.ajax({
					url: "./search/searchrolodexForSale",
					mType: "GET",
					data : {'rolodex': rxMasterID},
					success: function(data){
						 var createdLogin = data.createdByID;
						 var changedLogin = data.changedByID;*/
						// var rxMasterID = data.pk_fields;
						/* var loginID = 0;
						 if(createdLogin !== 0){
							 loginID = createdLogin;
						 }else if(changedLogin !== 0){
							 loginID = changedLogin;
						 }else if(createdLogin !== 0 && changedLogin !== 0){
							 loginID = createdLogin;
						 }*/
						$("#FilterrxCustomerID").val(rxMasterID);
						 var loginID = $('SalesRepComboList').val();
						 $('#UpcomingBidsGrid').jqGrid('setGridParam',{ postData: {'salesRepId': loginID,'rxCustomerID':rxMasterID } });
						 $("#UpcomingBidsGrid").trigger("reloadGrid");
						 $('#quotedJobGrid').jqGrid('setGridParam',{ postData: {'salesRepId': loginID,'rxCustomerID':rxMasterID } });
						 $("#quotedJobGrid").trigger("reloadGrid");
						 $('#PendingQuoteGrid').jqGrid('setGridParam',{ postData: {'salesRepId': loginID ,'rxCustomerID':rxMasterID} });
						 $("#PendingQuoteGrid").trigger("reloadGrid");
						 $('#AwardedContractors').jqGrid('setGridParam',{ postData: {'salesRepId': loginID ,'rxCustomerID':rxMasterID} });
						 $("#AwardedContractors").trigger("reloadGrid");
						 
						 
					/*	 loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID);
						 $('#customerFilterSearchGrid').jqGrid('setGridParam',{postData:{'createdLoginID' : createdLogin, 'changedLoginID' : changedLogin, 'rxMaster' : rxMasterID }});
						 $("#customerFilterSearchGrid").trigger("reloadGrid"); */ 
					/*},
					error: function(Xhr) {
					}
				});*/
			},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON(/* "search/searchCustomerListForUpComingBids"*/ "salescontroller/customerName", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

		function loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID){
			$("#customerFilterSearchGrid").jqGrid({
				url: "./search/salesupcomingForSale",
				datatype: 'JSON',
				mtype: 'GET',
				postData: { 'createdLoginID' : createdLogin, 'changedLoginID' : changedLogin, 'rxMaster' : rxMasterID },
				colNames: ['Job #', 'CustomerUpComingList'],
				colModel :[
                    {name:'jobNo',index:'jobNo',align:'center',width:30},
                    {name:'jobName',index:'jobName',align:'center',width:150}],
					rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
					sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Customer Filter UpComing List',
					height: 200, width: 470, altRows: true,
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

		function cancelUpComingCustomer(){
			jQuery("#customerDialogBox").dialog("close");
		}

	var arrColNamesUpcomingBids = ['joMasterID',"Date","Job Name", "Job #"];
	var arrColModelUpcomingBids = [{name:'joMasterId', index:'joMasterId',hidden:true, align:'center', width:35},{name:'bidDate', index:'bidDate', align:'center', width:35},{name:'jobName', index:'jobName', align:'', width:90},{name:'jobNo', index:'jobNo', align:'center', width:30}];

	var arrColNamesPendingQuotes = ['joMasterID','Date','Job Name', "Job #"];
	var arrColModelPendingQuotes = [{name:'joMasterId', index:'joMasterId',hidden:true, align:'center', width:35},{name:'bidDate', index:'bidDate', align:'center', width:35},{name:'jobName', index:'jobName', align:'', width:90},{name:'jobNo', index:'jobNo', align:'center', width:30}];

	var arrColNamesAwarded = ['joMasterID','Job #','Job Name'];
	var arrColModelAwarded = [{name:'joMasterId', index:'joMasterId',hidden:true, align:'center', width:35},{name:'jobNo', index:'jobNo', align:'center', width:30}, {name:'jobName', index:'jobName', align:'', width:90}];

	/**
	 * function to load grids.
	 * @returns {Boolean}
	 */
	function loadSalesGrid() {
		upcomingBids_grid (arrColNamesUpcomingBids, arrColModelUpcomingBids);
		quotedJob_grid (arrColNamesPendingQuotes, arrColModelPendingQuotes);
		pendingBids_grid(arrColNamesPendingQuotes, arrColModelPendingQuotes);
		awarded_grid (arrColNamesAwarded, arrColModelAwarded);
		//awardedGridDialog(arrColNamesAwarded, arrColModelAwarded);
		return true;
	}
	
	/******col names and col models, currently these are common for all sales grids******/
	/*var colName_jobNo = "Job #";
	var colModel_jobNo = {name:'jobNo', index:'jobNo', align:'center', width:60};*/
	
	var colName_assignedSalesman = "Assigned Salesman";
	var colModel_assignedSalesman = {name:'assignedSalesman', index:'assignedSalesman', align:'center', width:60};
	
	var colName_assignedCustomer = "Assigned Customers";
	var colModel_assignedcustomer = {name:'assignedCustomers',index:'assignedCustomers',align:'center',width:60};
	
	var colName_allCustomers = "All Customers";
	var colModel_allCustomers = {name:'allCustomer',index:'allCustomer',align:'center',width:60};
	
	var colName_architect = "Architect";
	var colModel_architect = {name:'architect',index:'architect',align:'center',width:60};
	
	var colName_engineer = "Engineer";
	var colModel_engineer = {name:'engineer',index:'engineer',align:'center',width:60};
	
	var colName_generalContractor = "General Contractor";
	var colModel_generalContractor = {name:'generalContractor',index:'generalContractor',align:'center',width:60};
	
	var colName_assignedTakeoffby = "Assigned Take-off By";
	var colModel_assignedTakeoffby = {name:'assignedTakeoffBy',index:'assignedTakeoffBy',align:'center',width:60};
	
	var colName_assignedQuoteby = "Assigned Quote By";
	var colModel_assignedQuoteby = {name:'assignedQuoteBy',index:'assignedQuoteBy',align:'center',width:60};
	
	var colName_lowBidder = "Low Bidder";
	var colModel_lowBidder = {name:'lowbidder',index:'lowbidder',align:'center',width:60};
	
	var colName_QuoteAmount = "Quote Amount($)";
	var colModel_QuoteAmount = {name:'quoteAmount',index:'quoteAmount',align:'center',width:35, formatter:customCurrencyFormatter};
	
	
	/*************************************************************************************/
	
	/**
	 * This function is used to add/remove the 'Upcoming Bids' grid column dynamically based on user request.
	 */
	function addRemoveUpcomingGridColumns() {
		//var Engineer=document.getElementById('engineer').value; 
		var boxes = document.getElementsByClassName("UpcomingOptions");
		for (var i=0;i<boxes.length;i++) {
			var box = boxes[i];
			if (box.type === "checkbox" && box.checked) {
				
				if (box.value != "Date" && box.value != "Job Name" && !isExistInArray(arrColNamesUpcomingBids, box.value)){
					/*if (box.value === "Job #" && !isExistInArray(arrColNamesUpcomingBids, "Job #")) {
						arrColNamesUpcomingBids.push(colName_jobNo);
						arrColModelUpcomingBids.push(colModel_jobNo);
					} else*/ if (box.value === "Assigned Salesman" && !isExistInArray(arrColNamesUpcomingBids, "Assigned Salesman")) {
						arrColNamesUpcomingBids.push(colName_assignedSalesman);
						arrColModelUpcomingBids.push(colModel_assignedSalesman);
					} else if(box.value === "Assigned Customers" && !isExistInArray(arrColNamesUpcomingBids, "Assigned Customers")){
						arrColNamesUpcomingBids.push(colName_assignedCustomer);
						arrColModelUpcomingBids.push(colModel_assignedcustomer);
					}else if(box.value ==="All Customers" && !isExistInArray(arrColNamesUpcomingBids,"All Customers")){
						arrColNamesUpcomingBids.push(colName_allCustomers);
						arrColModelUpcomingBids.push(colModel_allCustomers);
					} else if(box.value === "Architect" && !isExistInArray(arrColNamesUpcomingBids, "Architect")){
						arrColNamesUpcomingBids.push(colName_architect);
						arrColModelUpcomingBids.push(colModel_architect); 
					}else if(box.value==="Engineer" && !isExistInArray(arrColNamesUpcomingBids,"Engineer")){
						arrColNamesUpcomingBids.push(colName_engineer);
						arrColModelUpcomingBids.push(colModel_engineer);
					}else if(box.value==="General Contractor" && !isExistInArray(arrColNamesUpcomingBids,"General Contractor")){
						arrColNamesUpcomingBids.push(colName_generalContractor);
						arrColModelUpcomingBids.push(colModel_generalContractor);
					}
					
				}
			}else if (box.type === "checkbox" && !(box.checked)) {
				/*if (box.value === "Job #"){
					removeA(arrColNamesUpcomingBids,colName_jobNo);
					removeA(arrColModelUpcomingBids,colModel_jobNo);
				} else */if (box.value === "Assigned Salesman"){
					removeA(arrColNamesUpcomingBids,colName_assignedSalesman);
					removeA(arrColModelUpcomingBids,colModel_assignedSalesman);
				}else if(box.value==="Assigned Customers"){
					removeA(arrColNamesUpcomingBids,colName_assignedCustomer);
					removeA(arrColModelUpcomingBids,colModel_assignedcustomer);
				}else if(box.value==="All Customers"){
					removeA(arrColNamesUpcomingBids,colName_allCustomers);
					removeA(arrColModelUpcomingBids,colModel_allCustomers);
				}else if(box.value==="Architect"){
					removeA(arrColNamesUpcomingBids,colName_architect);
					removeA(arrColModelUpcomingBids,colModel_architect);
				}else if(box.value==="Engineer"){
					removeA(arrColNamesUpcomingBids,colName_engineer);
					removeA(arrColModelUpcomingBids,colModel_engineer);
				}else if(box.value==="General Contractor"){
					removeA(arrColNamesUpcomingBids,colName_generalContractor);
					removeA(arrColModelUpcomingBids,colModel_generalContractor);
				}
			}
		}
		
		$("#UpcomingBidsGridDialog").jqGrid('GridUnload');
		upcomingBidsgridDialog(arrColNamesUpcomingBids, arrColModelUpcomingBids);
		$("#UpcomingBidsGridDialog").trigger("reloadGrid");
		return true;
	}

	var upcomingBids_grid = function (arrColNamesUpcomingBids, arrColModelUpcomingBids) {
		var salesrepId = jQuery("#SalesRepComboList").val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#UpcomingBidsGrid").jqGrid({
			url:"./salescontroller/salesupcoming",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames: arrColNamesUpcomingBids,
			colModel: arrColModelUpcomingBids,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'bidDate',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:200,	width: 470, rownumbers: true, rownumWidth: 45,
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
			//editurl:'/employeelistcontroller?type=manipulate'
		    ondblClickRow: function(rowid) {
		    	var sel_id = $('#UpcomingBidsGrid').jqGrid('getGridParam', 'selrow');
		    	var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Upcoming Bid Opening Job','Info','Sales,Traversing through Upcoming Bid,JobName:'+aJobName+',JobNo:'+aJobNo); 
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
		});
		
		//$("#UpcomingBidsGrid").trigger("reloadGrid");
		return true;
	};

	/***********************Pending Quotes****************************/
	
	function addRemoveQuotedGridColumns(){
		var boxes = document.getElementsByClassName("QuotedOptions");
		for (var i=0;i<boxes.length;i++) {
			var box = boxes[i];
			if (box.type === "checkbox" && box.checked) {
				if (box.value != "Date" && box.value != "Job Name" && box.value != "Assigned" && !isExistInArray(arrColNamesPendingQuotes, box.value)) {
					/*if (box.value === "Job #" && !isExistInArray(arrColNamesPendingQuotes, "Job #")) {
						arrColNamesPendingQuotes.push(colName_jobNo);
						arrColModelPendingQuotes.push(colModel_jobNo);
					} else*/ if(box.value === "Assigned Customers" && !isExistInArray(arrColNamesPendingQuotes, "Assigned Customers")){
						arrColNamesPendingQuotes.push(colName_assignedCustomer);
						arrColModelPendingQuotes.push(colModel_assignedcustomer);
					} else if(box.value === "Quote Amount" && !isExistInArray(arrColNamesPendingQuotes,"Quote Amount($)")){
						arrColNamesPendingQuotes.push(colName_QuoteAmount);
						arrColModelPendingQuotes.push(colModel_QuoteAmount);
					}
				}
			} else if (box.type === "checkbox" && !(box.checked)) {
				/*if (box.value === "Job #") {
					removeA(arrColNamesPendingQuotes, colName_jobNo);
					removeA(arrColModelPendingQuotes, colModel_jobNo);
				} else*/ 
				if (box.value === "Assigned Customers") {
					removeA(arrColNamesPendingQuotes, colName_assignedCustomer);
					removeA(arrColModelPendingQuotes, colModel_assignedcustomer);
				} else if (box.value === "Quote Amount") {
					removeA(arrColNamesPendingQuotes,colName_QuoteAmount);
					removeA(arrColModelPendingQuotes,colModel_QuoteAmount);
				}
			}
		}
		$("#quotedJobGrid").jqGrid('GridUnload');
		quotedJob_grid(arrColNamesPendingQuotes, arrColModelPendingQuotes);
		//setPendingBidsValues(arrColNamesPendingQuotes);
		$("#quotedJobGrid").trigger("reloadGrid");
		return true;
	}
	
	function quotedGridColumns(){
		var boxes = document.getElementsByClassName("quotedJobOptions");
		for (var i=0;i<boxes.length;i++) {
			var box = boxes[i];
			if (box.type === "checkbox" && box.checked) {
				if (box.value != "Date" && box.value != "Job Name" && box.value != "Assigned" && !isExistInArray(arrColNamesPendingQuotes, box.value)) {
					/*if (box.value === "Job #" && !isExistInArray(arrColNamesPendingQuotes, "Job #")) {
						arrColNamesPendingQuotes.push(colName_jobNo);
						arrColModelPendingQuotes.push(colModel_jobNo);
					} else*/ if(box.value === "Assigned Customers" && !isExistInArray(arrColNamesPendingQuotes, "Assigned Customers")){
						arrColNamesPendingQuotes.push(colName_assignedCustomer);
						arrColModelPendingQuotes.push(colModel_assignedcustomer);
					} else if(box.value === "Quote Amount" && !isExistInArray(arrColNamesPendingQuotes,"Quote Amount($)")){
						arrColNamesPendingQuotes.push(colName_QuoteAmount);
						arrColModelPendingQuotes.push(colModel_QuoteAmount);
					}
				}
			} else if (box.type === "checkbox" && !(box.checked)) {
				/*if (box.value === "Job #") {
					removeA(arrColNamesPendingQuotes, colName_jobNo);
					removeA(arrColModelPendingQuotes, colModel_jobNo);
				} else*/ 
				if (box.value === "Assigned Customers") {
					removeA(arrColNamesPendingQuotes, colName_assignedCustomer);
					removeA(arrColModelPendingQuotes, colModel_assignedcustomer);
				} else if (box.value === "Quote Amount") {
					removeA(arrColNamesPendingQuotes,colName_QuoteAmount);
					removeA(arrColModelPendingQuotes,colModel_QuoteAmount);
				}
			}
		}
		$("#quotedJobGridDialog").jqGrid('GridUnload');
		quotedJobGrid(arrColNamesPendingQuotes, arrColModelPendingQuotes);
		//setPendingBidsValues(arrColNamesPendingQuotes);
		$("#quotedJobGridDialog").trigger("reloadGrid");
		return true;
	}
	
	function addRemovePendingGridColumns() {
		
		
		var boxes = document.getElementsByClassName("PendingOptions");
		for (var i=0;i<boxes.length;i++) {
			var box = boxes[i];
			if (box.type === "checkbox" && box.checked) {
				if (box.value != "Date" && box.value != "Job Name" && box.value != "Assigned" && !isExistInArray(arrColNamesPendingQuotes, box.value)) {
					/*if (box.value === "Job #" && !isExistInArray(arrColNamesPendingQuotes, "Job #")) {
						arrColNamesPendingQuotes.push(colName_jobNo);
						arrColModelPendingQuotes.push(colModel_jobNo);
					} else*/ if(box.value === "Assigned Customers" && !isExistInArray(arrColNamesPendingQuotes, "Assigned Customers")){
						arrColNamesPendingQuotes.push(colName_assignedCustomer);
						arrColModelPendingQuotes.push(colModel_assignedcustomer);
					} else if(box.value === "Quote Amount" && !isExistInArray(arrColNamesPendingQuotes,"Quote Amount($)")){
						arrColNamesPendingQuotes.push(colName_QuoteAmount);
						arrColModelPendingQuotes.push(colModel_QuoteAmount);
					}
				}
			} else if (box.type === "checkbox" && !(box.checked)) {
				/*if (box.value === "Job #") {
					removeA(arrColNamesPendingQuotes, colName_jobNo);
					removeA(arrColModelPendingQuotes, colModel_jobNo);
				} else*/ 
				if (box.value === "Assigned Customers") {
					removeA(arrColNamesPendingQuotes, colName_assignedCustomer);
					removeA(arrColModelPendingQuotes, colModel_assignedcustomer);
				} else if (box.value === "Quote Amount") {
					removeA(arrColNamesPendingQuotes,colName_QuoteAmount);
					removeA(arrColModelPendingQuotes,colModel_QuoteAmount);
				}
			}
		}
		$("#PendingQuoteGridDialog").jqGrid('GridUnload');
		pendingBidsgridDialog(arrColNamesPendingQuotes, arrColModelPendingQuotes);
		//setPendingBidsValues(arrColNamesPendingQuotes);
		$("#PendingQuoteGridDialog").trigger("reloadGrid");
		return true;
	}
	
	var pendingBids_grid = function (arrColNamesPendingQuotes, arrColModelPendingQuotes){
		var salesrepId = $('#SalesRepComboList').val();
		console.log('Jenith-1: '+salesrepId);
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		console.log('Jenith-2: '+salesrepId);
		$("#PendingQuoteGrid").jqGrid({
			url:"./salescontroller/salesPending",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames: arrColNamesPendingQuotes,
			colModel: arrColModelPendingQuotes,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:200,	width: 470,rownumbers: true,
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
			ondblClickRow: function(rowid) {
				var sel_id = $('#PendingQuoteGrid').jqGrid('getGridParam', 'selrow');
				var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Pending Job Grid Traverse','Info','Sales,Traversing through Pending Job,JobName:'+aJobName+',JobNo:'+aJobNo); 
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
			//editurl:'/employeelistcontroller?type=manipulate'
		});
		return true;
	};
	
	function OpenPendingBidsgridDialog(){
		$("#PendingQuoteGridDialog").jqGrid('GridUnload');
		pendingBidsgridDialog (arrColNamesPendingQuotes, arrColModelPendingQuotes);
		$("#PendingQuoteGridDialog").trigger("reloadGrid");
		
		createtpusage('Sales Screen','Open Pending Jobs','Info','Sales,Opening Pending Jobs');
		
		var dialogWidth = 1150;
		$('.myPendingClass').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
		$('.myPendingClass').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
		$('.myPendingClass').css('color','black');
		//$('.ui-dialog').css('padding','0.7em 1em');
		$('.ui-dialog-titlebar').css('padding','0.7em 1em');
		jQuery("#displayPendingBidsDialog").dialog({
			 closeOnEscape: true,  
			create: function (event, ui) {
				dialogClass: 'myPendingTitleClass';
				$("#displayPendingBidsDialog").parent().find(".ui-dialog-titlebar").addClass("myPendingClass");
	            $(".myPendingClass").html('<span style="float:left;" id="headClass">Pending Bids</span><span style="float:right; cursor:pointer;" id="myPendingCloseIcon">Close</span>');
	        },
			autoOpen:false, 
			height:540, 
			width:dialogWidth,
			top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], 
			modal:true, 
			title:'Pending Bids',  
			iframe: true,
			open: function(event, ui) {  
	            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
	        },
			close:function() {
					jQuery(this).dialog("close");
			}
		});
		 $("span#myPendingCloseIcon").click(function() {
		        $( "#displayPendingBidsDialog" ).dialog( "close" );
		    });// Add quote History
		jQuery("#displayPendingBidsDialog").dialog("open");
		
	}
	
	var pendingBidsgridDialog = function (arrColNamesPendingQuotes, arrColModelPendingQuotes){
		var salesrepId = $('#SalesRepComboList').val();
		console.log('Jenith-1: '+salesrepId);
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		console.log('Jenith-2: '+salesrepId);
		$("#PendingQuoteGridDialog").jqGrid({
			url:"./salescontroller/salesPending",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames: arrColNamesPendingQuotes,
			colModel: arrColModelPendingQuotes,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:438,	width: 1115,rownumbers: true,
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
			ondblClickRow: function(rowid) {
				var sel_id = $('#PendingQuoteGridDialog').jqGrid('getGridParam', 'selrow');
//				var aJobName = $('#PendingQuoteGridDialog').jqGrid('getCell', sel_id, 2);
//				var aJobNo = $('#PendingQuoteGridDialog').jqGrid('getCell', sel_id, 3);
				var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Pending Job Grid Traverse','Info','Sales,Traversing through Pending Job,JobName:'+aJobName+',JobNo:'+aJobNo); 
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
			//editurl:'/employeelistcontroller?type=manipulate'
		});
		return true;
	};
	
	var quotedJob_grid = function (arrColNamesPendingQuotes, arrColModelPendingQuotes){
		//var empId =      $('#SalesRepComboList').val();
		var salesrepId = $('#SalesRepComboList').val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#quotedJobGrid").jqGrid({
			url:"./salescontroller/salesQuoted",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames: arrColNamesPendingQuotes,
			colModel: arrColModelPendingQuotes,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:200,	width: 470,rownumbers: true,
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
			ondblClickRow: function(rowid) {
				var sel_id = $('#quotedJobGrid').jqGrid('getGridParam', 'selrow');
				var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Quoted Job Grid Traverse','Info','Sales,Traversing through Quoted Job,JobName:'+aJobName+',JobNo:'+aJobNo); 
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
			//editurl:'/employeelistcontroller?type=manipulate'
		});
		return true;
	};
	
	
	/***********************Awarded contractores****************************//*
	function addRemoveAwardedGridColumns() {
		var boxes = document.getElementsByClassName("AwardedOptions");
		for (var i=0;i<boxes.length;i++) {  
			var box = boxes[i];
			if (box.type === "checkbox" && box.checked) {
				if (box.value != "Date" && box.value != "Job Name" && box.value != "Assigned" && !isExistInArray(arrColNamesAwarded, box.value)){
					if (box.value === "Job #" && !isExistInArray(arrColNamesAwarded, "Job #")) {
						arrColNamesAwarded.push(colName_jobNo);
						arrColModelAwarded.push(colModel_jobNo);
					} else if (box.value === "Assigned Salesman" && !isExistInArray(arrColNamesAwarded, "Assigned Salesman")) {
						arrColNamesAwarded.push(colName_assignedSalesman);
						arrColModelAwarded.push(colModel_assignedSalesman);
					}else if(box.value === "Assigned Customers" && !isExistInArray(arrColNamesAwarded,"Assigned Customers")){
						arrColNamesAwarded.push(colName_assignedCustomer);
						arrColModelAwarded.push(colModel_assignedcustomer);
					}else if(box.value === "All Customers" && !isExistInArray(arrColNamesAwarded,"All Customers")){
						arrColNamesAwarded.push(colName_allCustomers);
						arrColModelAwarded.push(colModel_allCustomers);
					} else if(box.value === "Architect" && !isExistInArray(arrColNamesAwarded, "Architect")){
						arrColNamesAwarded.push(colName_architect);
						arrColModelAwarded.push(colModel_architect); 
					}else if(box.value === "Engineer" && !isExistInArray(arrColNamesAwarded,"Engineer")){
						arrColNamesAwarded.push(colName_engineer);
						arrColModelAwarded.push(colModel_engineer);
					}else if(box.value === "General Contractor" && !isExistInArray(arrColNamesAwarded,"General Contractor")){
						arrColNamesAwarded.push(colName_generalContractor);
						arrColModelAwarded.push(colModel_generalContractor);
					}else if(box.value === "Low Bidder" && !isExistInArray(arrColNamesAwarded,"Low Bidder")){
						arrColNamesAwarded.push(colName_lowBidder);
						arrColModelAwarded.push(colModel_lowBidder);
					}
				}
			} else if (box.type === "checkbox" && !(box.checked)) {
				if (box.value === "Job #"){
					removeA(arrColNamesAwarded,colName_jobNo);
					removeA(arrColModelAwarded,colModel_jobNo);
				} else if (box.value === "Assigned Salesman"){
					removeA(arrColNamesAwarded,colName_assignedSalesman);
					removeA(arrColModelAwarded,colModel_assignedSalesman);
				}else if(box.value === "Assigned Customers"){
					removeA(arrColNamesAwarded,colName_assignedCustomer);
					removeA(arrColModelAwarded,colModel_assignedcustomer);
				}else if(box.value === "All Customers"){
					removeA(arrColNamesAwarded,colName_allCustomers);
					removeA(arrColModelAwarded,colModel_allCustomers);
				}else if(box.value === "Architect"){
					removeA(arrColNamesAwarded,colName_architect);
					removeA(arrColModelAwarded,colModel_architect);
				}else if(box.value === "Engineer"){
					removeA(arrColNamesAwarded,colName_engineer);
					removeA(arrColModelAwarded,colModel_engineer);
				}else if(box.value === "General Contractor"){
					removeA(arrColNamesAwarded,colName_generalContractor);
					removeA(arrColModelAwarded,colModel_generalContractor);
				}else if(box.value === "Low Bidder"){
					removeA(arrColNamesAwarded,colName_lowBidder);
					removeA(arrColModelAwarded,colModel_lowBidder);
				}
					
			}
		}
		$("#AwardedContractorsDialog").jqGrid('GridUnload');
		awardedGridDialog(arrColNamesAwarded, arrColModelAwarded);
		//setPendingBidsValues(arrColNamesAwarded);
		$("#AwardedContractorsDialog").trigger("reloadGrid");
		return true;
	}*/
	
	function clearCustomer(){
		 $('#customerName').val('');
		 $('#FilterrxCustomerID').val('');
		 $('#customerFilterListID').val('');
		 $("#UpcomingBidsGrid").jqGrid('GridUnload');
		 upcomingBids_grid (arrColNamesUpcomingBids, arrColModelUpcomingBids);
		 $("#UpcomingBidsGrid").trigger("reloadGrid");
		 $('#quotedJobGrid').jqGrid('GridUnload');
		 quotedJob_grid (arrColNamesPendingQuotes, arrColModelPendingQuotes);
		 $("#quotedJobGrid").trigger("reloadGrid");
		 $('#PendingQuoteGrid').jqGrid('GridUnload');
		 pendingBids_grid(arrColNamesPendingQuotes, arrColModelPendingQuotes);
		 $("#PendingQuoteGrid").trigger("reloadGrid");
		 $('#AwardedContractors').jqGrid('GridUnload');
		 awarded_grid (arrColNamesAwarded, arrColModelAwarded);
		 $("#AwardedContractors").trigger("reloadGrid");
	}
	
	var awarded_grid = function (arrColNamesAwarded, arrColModelAwarded){
		var salesrepId = jQuery("#SalesRepComboList").val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#AwardedContractors").jqGrid({
			url:"./salescontroller/salesawarded",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames:arrColNamesAwarded,
			colModel:arrColModelAwarded,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'bidDate',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:200,	width: 470, rownumbers: true,
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
			ondblClickRow: function(rowid) {
				var sel_id = $('#AwardedContractors').jqGrid('getGridParam', 'selrow');
//				var aJobName = $('#AwardedContractors').jqGrid('getCell', sel_id, 2);
//				var aJobNo = $('#AwardedContractors').jqGrid('getCell', sel_id, 1);
				var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Awarded Contractors Traverse','Info','Sales,Traversing through Awarded Contractors,JobName:'+aJobName+',JobNo:'+aJobNo);  
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
			//editurl:'/employeelistcontroller?type=manipulate'
		});

		return true;
	};
	
	/*****************generic functions: useful for some general checks****************/
	/**
	 * This function is used to check the whether a value is exist or not in an array.
	 * @param array, value
	 * @returns {@link Boolean} 
	 */
	function isExistInArray(array, value) {
		if(jQuery.inArray(value, array) === -1){
			return false;
		} else {
			return true;
		}
	}
	/**
	 * This method is used to used remove a value from array.
	 * @param arr
	 * @returns arr
	 */
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
	
	function getSalesReps(selectedUserId) {
		$.ajax({
			url: "./salescontroller",
			success: function(data){
				var aSalesRepOptions ='';
				$.each(data, function(key, value) { 
					aSalesRepOptions = aSalesRepOptions + '<option value="'+value.userId+'">'+value.userName+'</option>';
				});
				$("#SalesRepComboList").append(aSalesRepOptions);
				$("#SalesRepComboList option[value=" + selectedUserId + "]").attr("selected", true);
			},
			error: function(Xhr) {
			}
		});	 
	}
	
	function mouseoverpending(){
		$('#morepending').show();
	}
	
	function mouseoutpending(){
		$('#morepending').hide();
	}
	
	function mouseoverawarded(){
		$('#moreawarded').show();
	}
	
	function mouseoutawarded(){
		$('#moreawarded').hide();
	}
	
	$("#upcoming").hover(function() {
	    $("#moreupcoming").slideToggle('500').css({"display":"block","position":"top left"});
	}, function() {
	    $("#moreupcoming").slideToggle('500');
	});
	function checkpermission(){
		var UserLoginID=$("#userLogin_Id").val();
		$.ajax({
		    url: "./getSysPrivilage",
		    data: {'accessPage':'Sales_Filter','userGroupID':0, 'UserLoginID':UserLoginID},
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
		    data: {'accessPage':'Sales_Filter','userGroupID':0, 'UserLoginID':UserLoginID},
		    type: 'POST',
		    success: function(data){
		    	if(data.Value == "granted")
		    	{
		    		var salesid=jQuery("#SalesRepComboList").val();
		    		$('#UpcomingBidsGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		    		$('#UpcomingBidsGrid').trigger("reloadGrid");
		    		
		    		$('#PendingQuoteGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		    		$('#PendingQuoteGrid').trigger("reloadGrid");
		    		
		    		$('#quotedJobGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		    		$('#quotedJobGrid').trigger("reloadGrid");
		    		
		    		$('#AwardedContractors').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		    		$('#AwardedContractors').trigger("reloadGrid");
		    	}
		    	else
		    		showDeniedPopup();
		    }, error: function(error) {
		    	showDeniedPopup();
			}
		});
	}
	
	function customCurrencyFormatter(cellValue, options, rowObject) {
		return formatCurrency(cellValue);
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
				//var status = "";
				$.each(data, function(index, value) {
					status = value.jobStatus;
					jobCustomer = value.customerName;
				});
				if (jobCustomer === null) {
					jobCustomer = "";
				}
				updateJobstatus(jobNumber,status,joMasterID);
				var urijobname=encodeBigurl(jobName);
				var uricusname=encodeBigurl(jobCustomer);
				qryStr = "jobNumber=" + jobNumber	+ "&jobName=" + urijobname + "&jobCustomer=" + uricusname	 + "&joMasterID="+joMasterID;
				return qryStr;
			},
			error : function(Xhr) {
			}
		});
		return qryStr;
}
		
	
	function callUpcomingDialog(){
		$("#UpcomingBidsGridDialog").jqGrid('GridUnload');
		upcomingBidsgridDialog (arrColNamesUpcomingBids, arrColModelUpcomingBids);
		$("#UpcomingBidsGridDialog").trigger("reloadGrid");
		
		createtpusage('Sales Screen','Open Upcoming Bids','Info','Sales,Opening Upcoming Bids');
		
		var dialogWidth = 1150;
		$('.myUpcomingClass').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
		$('.myUpcomingClass').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
		$('.myUpcomingClass').css('color','black');
		//$('.ui-dialog').css('padding','0.7em 1em');
		$('.ui-dialog-titlebar').css('padding','0.7em 1em');
		jQuery("#displayUpcomingBids").dialog({
			 closeOnEscape: true,  
			create: function (event, ui) {
				dialogClass: 'myUpcomingTitleClass';
				$("#displayUpcomingBids").parent().find(".ui-dialog-titlebar").addClass("myUpcomingClass");
	            $(".myUpcomingClass").html('<span style="float:left;" id="headClass">Upcoming Bids</span><span style="float:right; cursor:pointer;" id="myUpcomingCloseIcon">Close</span>');
	        },
			autoOpen:false, 
			height:540, 
			width:dialogWidth,
			top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], 
			modal:true, 
			title:'Upcoming Bids',  
			iframe: true,
			open: function(event, ui) {  
	            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
	        },
			close:function() {
					jQuery(this).dialog("close");
			}
		});
		 $("span#myUpcomingCloseIcon").click(function() {
		        $( "#displayUpcomingBids" ).dialog( "close" );
		    });// Add quote History
		jQuery("#displayUpcomingBids").dialog("open");
		
		
		
	}
	
	var upcomingBidsgridDialog = function (arrColNamesUpcomingBids, arrColModelUpcomingBids) {
		console.log(arrColNamesUpcomingBids);
		console.log("===============================");
		console.log(arrColModelUpcomingBids);
		var salesrepId = jQuery("#SalesRepComboList").val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#UpcomingBidsGridDialog").jqGrid({
			url:"./salescontroller/salesupcoming",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames: arrColNamesUpcomingBids,
			colModel: arrColModelUpcomingBids,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'bidDate',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:438,	width: 1115, rownumbers: true, rownumWidth: 45,
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
			//editurl:'/employeelistcontroller?type=manipulate'
		    ondblClickRow: function(rowid) {
		    	var sel_id = $('#UpcomingBidsGridDialog').jqGrid('getGridParam', 'selrow');
		    	var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Upcoming Bid Opening Job','Info','Sales,Traversing through Upcoming Bid,JobName:'+aJobName+',JobNo:'+aJobNo); 
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
		});
		
		//$("#UpcomingBidsGrid").trigger("reloadGrid");
		return true;
	};
	
	function callQuotedJobDialog(){
		$("#quotedJobGridDialog").jqGrid('GridUnload');
		quotedJobGrid (arrColNamesPendingQuotes, arrColModelPendingQuotes);
		$("#quotedJobGridDialog").trigger("reloadGrid");
		
		createtpusage('Sales Screen','Open Quoted Jobs','Info','Sales,Opening Quoted Jobs');
		
		var dialogWidth = 1150;
		$('.myQuotedClass').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
		$('.myQuotedClass').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
		$('.myQuotedClass').css('color','black');
		//$('.ui-dialog').css('padding','0.7em 1em');
		$('.ui-dialog-titlebar').css('padding','0.7em 1em');
		jQuery("#displayQuotedJob").dialog({
			 closeOnEscape: true,  
			create: function (event, ui) {
				dialogClass: 'myQuotedTitleClass';
				$("#displayQuotedJob").parent().find(".ui-dialog-titlebar").addClass("myQuotedClass");
	            $(".myQuotedClass").html('<span style="float:left;" id="headClass">Quoted Jobs</span><span style="float:right; cursor:pointer;" id="myQuotedCloseIcon">Close</span>');
	        },
			autoOpen:false, 
			height:540, 
			width:dialogWidth,
			top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], 
			modal:true, 
			title:'Quoted Jobs',  
			iframe: true,
			open: function(event, ui) {  
	            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
	        },
			close:function() {
					jQuery(this).dialog("close");
			}
		});
		 $("span#myQuotedCloseIcon").click(function() {
		        $( "#displayQuotedJob" ).dialog( "close" );
		    });// Add quote History
		jQuery("#displayQuotedJob").dialog("open");
		
	}
	
	function openAwardedContractorsDialog(){
		$("#AwardedContractorsDialog").jqGrid('GridUnload');
		awardedGridDialog(arrColNamesAwarded, arrColModelAwarded);;
		$("#AwardedContractorsDialog").trigger("reloadGrid");
		createtpusage('Sales Screen','Open Awarded Contractors','Info','Sales,Opening Awarded Contractors');
		var dialogWidth = 1150;
		$('.myAwardedClass').css('background','-moz-linear-gradient(-90deg, #F8F8AB, #CDBF55)');
		$('.myAwardedClass').css('background','-webkit-gradient(linear, 0% 0%, 0% 100%, from(#F8F8AB), to(#CDBF55))');
		$('.myAwardedClass').css('color','black');
		//$('.ui-dialog').css('padding','0.7em 1em');
		$('.ui-dialog-titlebar').css('padding','0.7em 1em');
		jQuery("#displayAwardedContractorsDialog").dialog({
			 closeOnEscape: true,  
			create: function (event, ui) {
				dialogClass: 'myAwardedTitleClass';
				$("#displayAwardedContractorsDialog").parent().find(".ui-dialog-titlebar").addClass("myAwardedClass");
	            $(".myAwardedClass").html('<span style="float:left;" id="headClass">Awarded Contractors</span><span style="float:right; cursor:pointer;" id="myAwardedCloseIcon">Close</span>');
	        },
			autoOpen:false, 
			height:540, 
			width:dialogWidth,
			top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], 
			modal:true, 
			title:'Awarded Contractors',  
			iframe: true,
			open: function(event, ui) {  
	            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
	        },
			close:function() {
					jQuery(this).dialog("close");
			}
		});
		 $("span#myAwardedCloseIcon").click(function() {
		        $( "#displayAwardedContractorsDialog" ).dialog( "close" );
		    });
		jQuery("#displayAwardedContractorsDialog").dialog("open");
		
	}
	
	var awardedGridDialog = function (arrColNamesAwarded, arrColModelAwarded){
		var salesrepId = jQuery("#SalesRepComboList").val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#AwardedContractorsDialog").jqGrid({
			url:"./salescontroller/salesawarded",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames:arrColNamesAwarded,
			colModel:arrColModelAwarded,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'bidDate',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:438,	width: 1115, rownumbers: true,
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
			ondblClickRow: function(rowid) {
				var sel_id = $('#AwardedContractorsDialog').jqGrid('getGridParam', 'selrow');
				var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Awarded Contractors Traverse','Info','Sales,Traversing through Awarded Contractors,JobName:'+aJobName+',JobNo:'+aJobNo);
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
			//editurl:'/employeelistcontroller?type=manipulate'
		});

		return true;
	};
	
	/***********************Awarded contractores****************************/
	function addRemoveAwardedGridColumns() {
		var boxes = document.getElementsByClassName("AwardedOptions");
		for (var i=0;i<boxes.length;i++) {  
			var box = boxes[i];
			if (box.type === "checkbox" && box.checked) {
				if (box.value != "Date" && box.value != "Job Name" && box.value != "Assigned" && !isExistInArray(arrColNamesAwarded, box.value)){
					if (box.value === "Job #" && !isExistInArray(arrColNamesAwarded, "Job #")) {
						arrColNamesAwarded.push(colName_jobNo);
						arrColModelAwarded.push(colModel_jobNo);
					} else if (box.value === "Assigned Salesman" && !isExistInArray(arrColNamesAwarded, "Assigned Salesman")) {
						arrColNamesAwarded.push(colName_assignedSalesman);
						arrColModelAwarded.push(colModel_assignedSalesman);
					}else if(box.value === "Assigned Customers" && !isExistInArray(arrColNamesAwarded,"Assigned Customers")){
						arrColNamesAwarded.push(colName_assignedCustomer);
						arrColModelAwarded.push(colModel_assignedcustomer);
					}else if(box.value === "All Customers" && !isExistInArray(arrColNamesAwarded,"All Customers")){
						arrColNamesAwarded.push(colName_allCustomers);
						arrColModelAwarded.push(colModel_allCustomers);
					} else if(box.value === "Architect" && !isExistInArray(arrColNamesAwarded, "Architect")){
						arrColNamesAwarded.push(colName_architect);
						arrColModelAwarded.push(colModel_architect); 
					}else if(box.value === "Engineer" && !isExistInArray(arrColNamesAwarded,"Engineer")){
						arrColNamesAwarded.push(colName_engineer);
						arrColModelAwarded.push(colModel_engineer);
					}else if(box.value === "General Contractor" && !isExistInArray(arrColNamesAwarded,"General Contractor")){
						arrColNamesAwarded.push(colName_generalContractor);
						arrColModelAwarded.push(colModel_generalContractor);
					}else if(box.value === "Low Bidder" && !isExistInArray(arrColNamesAwarded,"Low Bidder")){
						arrColNamesAwarded.push(colName_lowBidder);
						arrColModelAwarded.push(colModel_lowBidder);
					}
				}
			} else if (box.type === "checkbox" && !(box.checked)) {
				if (box.value === "Job #"){
					removeA(arrColNamesAwarded,colName_jobNo);
					removeA(arrColModelAwarded,colModel_jobNo);
				} else if (box.value === "Assigned Salesman"){
					removeA(arrColNamesAwarded,colName_assignedSalesman);
					removeA(arrColModelAwarded,colModel_assignedSalesman);
				}else if(box.value === "Assigned Customers"){
					removeA(arrColNamesAwarded,colName_assignedCustomer);
					removeA(arrColModelAwarded,colModel_assignedcustomer);
				}else if(box.value === "All Customers"){
					removeA(arrColNamesAwarded,colName_allCustomers);
					removeA(arrColModelAwarded,colModel_allCustomers);
				}else if(box.value === "Architect"){
					removeA(arrColNamesAwarded,colName_architect);
					removeA(arrColModelAwarded,colModel_architect);
				}else if(box.value === "Engineer"){
					removeA(arrColNamesAwarded,colName_engineer);
					removeA(arrColModelAwarded,colModel_engineer);
				}else if(box.value === "General Contractor"){
					removeA(arrColNamesAwarded,colName_generalContractor);
					removeA(arrColModelAwarded,colModel_generalContractor);
				}else if(box.value === "Low Bidder"){
					removeA(arrColNamesAwarded,colName_lowBidder);
					removeA(arrColModelAwarded,colModel_lowBidder);
				}
					
			}
		}
		$("#AwardedContractorsDialog").jqGrid('GridUnload');
		awardedGridDialog(arrColNamesAwarded, arrColModelAwarded);
		//setPendingBidsValues(arrColNamesAwarded);
		$("#AwardedContractorsDialog").trigger("reloadGrid");
		return true;
	}
	

	var quotedJobGrid = function (arrColNamesPendingQuotes, arrColModelPendingQuotes){
		//callQuotedJobDialog();
		//var empName = $('#SalesRepComboList option:selected').text();
		//var empId = $('#SalesRepComboList').val();
		var salesrepId = $('#SalesRepComboList option:selected').val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#quotedJobGridDialog").jqGrid({
			url:"./salescontroller/salesQuoted",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId,'rxCustomerID':function(){
				var rxcustomerid=$("#FilterrxCustomerID").val();
				if(rxcustomerid!=null && rxcustomerid!=undefined && rxcustomerid!=""){
					return rxcustomerid;
				}else{
					return 0;
				}
				 
			}},
			pager: '',
			colNames: arrColNamesPendingQuotes,
			colModel: arrColModelPendingQuotes,
			rowNum: 10000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption:null,
			height:445,	width: 1115,rownumbers: true,
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
			ondblClickRow: function(rowid) {
				var sel_id = $('#quotedJobGridDialog').jqGrid('getGridParam', 'selrow');
				var rowData = jQuery(this).getRowData(sel_id); 
				var aJobNo = rowData['jobNo'];
				var aJobName = rowData['jobName'];
				var joMasterID=rowData['joMasterId'];
				var qryStr = getQryStr(aJobNo, aJobName,joMasterID);
				var queryString = "token=view&" + qryStr;
				createtpusage('Sales Screen','Quoted Job Grid Traverse','Info','Sales,Traversing through Quoted Job,JobName:'+aJobName+',JobNo:'+aJobNo);  
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?" + queryString;
				}
			}
			//editurl:'/employeelistcontroller?type=manipulate'
		});
		return true;		
	}
	
	function printQuotedJobs() {
		var empName = $('#SalesRepComboList option:selected').text();
		var empId = $('#SalesRepComboList').val();
		window.open("./salescontroller/printQuotedJobs?employName="+empName+"&employID="+empId);
		return true;
	}