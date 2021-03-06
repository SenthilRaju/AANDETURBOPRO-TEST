	var salesUserId = $("#salesRepUserID").val();	
	getSalesReps(salesUserId);
		$(window).bind('load', function() {
			$('#upcoming').bubbletip($('#tip1_down'), {
				deltaDirection: 'down',
				offsetTop: 115	
			});
		});
		
		jQuery(document).ready(function(){
			$('#search').hide();
			$("#intro").hide();
			//$("input:button").button();
			setTimeout("loadSalesGrid();", 1000);
			//$("#").removeClass("ui-corner-all");
			var createdLogin = '';
			var changedLogin = '';
			var rxMasterID = '';
			loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID);
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
				var rxMasterID = ui.item.manufactureID; 
				$.ajax({
					url: "./search/searchrolodexForSale",
					mType: "GET",
					data : {'rolodex': rxMasterID},
					success: function(data){
						 var createdLogin = data.createdByID;
						 var changedLogin = data.changedByID;
						// var rxMasterID = data.pk_fields;
						 var loginID = 0;
						 if(createdLogin !== 0){
							 loginID = createdLogin;
						 }else if(changedLogin !== 0){
							 loginID = changedLogin;
						 }else if(createdLogin !== 0 && changedLogin !== 0){
							 loginID = createdLogin;
						 }
						 $('#UpcomingBidsGrid').jqGrid('setGridParam',{ postData: {'salesRepId': loginID } });
						 $("#UpcomingBidsGrid").trigger("reloadGrid");
					/*	 loadCustomerFilterGrid(createdLogin, changedLogin, rxMasterID);
						 $('#customerFilterSearchGrid').jqGrid('setGridParam',{postData:{'createdLoginID' : createdLogin, 'changedLoginID' : changedLogin, 'rxMaster' : rxMasterID }});
						 $("#customerFilterSearchGrid").trigger("reloadGrid"); */ 
					},
					error: function(Xhr) {
					}
				});
			},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "search/searchCustomerListForUpComingBids", request, function( data, status, xhr ) { cache[ term ] = data; 
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
					rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
					sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Customer Filter UpComing List',
					height: 150, width: 700, altRows: true,
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

	var arrColNamesUpcomingBids = ["Date","Job Name", "Job #"];
	var arrColModelUpcomingBids = [{name:'bidDate', index:'bidDate', align:'center', width:60},{name:'jobName', index:'jobName', align:'', width:90},{name:'jobNo', index:'jobNo', align:'center', width:30}];

	var arrColNamesPendingQuotes = ['Date','Job Name', "Job #"];
	var arrColModelPendingQuotes = [{name:'bidDate', index:'bidDate', align:'center', width:60},{name:'jobName', index:'jobName', align:'', width:90},{name:'jobNo', index:'jobNo', align:'center', width:30}];

	var arrColNamesAwarded = ['Job #','Job Name'];
	var arrColModelAwarded = [{name:'jobNo', index:'jobNo', align:'center', width:30}, {name:'jobName', index:'jobName', align:'', width:90}];

	/**
	 * function to load grids.
	 * @returns {Boolean}
	 */
	function loadSalesGrid() {
		upcomingBids_grid (arrColNamesUpcomingBids, arrColModelUpcomingBids);
		pendingBids_grid (arrColNamesPendingQuotes, arrColModelPendingQuotes);
		awarded_grid (arrColNamesAwarded, arrColModelAwarded);
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
	var colModel_QuoteAmount = {name:'quoteAmount',index:'quoteAmount',align:'center',width:60, formatter:customCurrencyFormatter};
	
	
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
		$("#UpcomingBidsGrid").jqGrid('GridUnload');
		upcomingBids_grid(arrColNamesUpcomingBids, arrColModelUpcomingBids);
		$("#UpcomingBidsGrid").trigger("reloadGrid");
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
			postData: {'salesRepId':salesrepId},
			pager: '',
			colNames: arrColNamesUpcomingBids,
			colModel: arrColModelUpcomingBids,
			rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'bidDate',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:100,	width: 1130, rownumbers: true, rownumWidth: 45,
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
				var aJobName = $('#UpcomingBidsGrid').jqGrid('getCell', sel_id, 2);
				var aJobNo = $('#UpcomingBidsGrid').jqGrid('getCell', sel_id, 3);
				var qryStr = getQryStr(aJobNo, aJobName);
				var queryString = "token=view&" + qryStr;
				document.location.href = "./jobflow?" + queryString;
			}
		});
		
		//$("#UpcomingBidsGrid").trigger("reloadGrid");
		return true;
	};

	/***********************Pending Quotes****************************/
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
				} else if (box.value === "Quote Amount($)") {
					removeA(arrColNamesPendingQuotes,colName_QuoteAmount);
					removeA(arrColModelPendingQuotes,colModel_QuoteAmount);
				}
			}
		}
		$("#PendingQuotesGrid2").jqGrid('GridUnload');
		pendingBids_grid(arrColNamesPendingQuotes, arrColModelPendingQuotes);
		//setPendingBidsValues(arrColNamesPendingQuotes);
		$("#PendingQuotesGrid2").trigger("reloadGrid");
		return true;
	}
	
	var pendingBids_grid = function (arrColNamesPendingQuotes, arrColModelPendingQuotes){
		var salesrepId = jQuery("#SalesRepComboList").val();
		if (salesrepId === null) {
			salesrepId = 10;
		} else {
			salesrepId = jQuery("#SalesRepComboList").val();
		}
		$("#PendingQuotesGrid2").jqGrid({
			url:"./salescontroller/salespending",
			datatype: 'json',
			mtype: 'GET',
			postData: {'salesRepId':salesrepId},
			pager: '',
			colNames: arrColNamesPendingQuotes,
			colModel: arrColModelPendingQuotes,
			rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:150,	width: 1130,rownumbers: true,
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
				var sel_id = $('#PendingQuotesGrid2').jqGrid('getGridParam', 'selrow');
				var aJobName = $('#PendingQuotesGrid2').jqGrid('getCell', sel_id, 2);
				var aJobNo = $('#PendingQuotesGrid2').jqGrid('getCell', sel_id, 3);
				var qryStr = getQryStr(aJobNo, aJobName);
				var queryString = "token=view&" + qryStr;
				document.location.href = "./jobflow?" + queryString;
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
		$("#AwardedContractors").jqGrid('GridUnload');
		awarded_grid(arrColNamesAwarded, arrColModelAwarded);
		//setPendingBidsValues(arrColNamesAwarded);
		$("#AwardedContractors").trigger("reloadGrid");
		return true;
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
			postData: {'salesRepId':salesrepId},
			pager: '',
			colNames:arrColNamesAwarded,
			colModel:arrColModelAwarded,
			rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
			sortname: 'bidDate',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
			height:100,	width: 1130, rownumbers: true,
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
				var aJobName = $('#AwardedContractors').jqGrid('getCell', sel_id, 2);
				var aJobNo = $('#AwardedContractors').jqGrid('getCell', sel_id, 1);
				var qryStr = getQryStr(aJobNo, aJobName);
				var queryString = "token=view&" + qryStr;
				document.location.href = "./jobflow?" + queryString;
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
	
	function changeSalesrep() {
		var salesid=jQuery("#SalesRepComboList").val();
		$('#UpcomingBidsGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		$('#UpcomingBidsGrid').trigger("reloadGrid");
		
		$('#PendingQuotesGrid2').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		$('#PendingQuotesGrid2').trigger("reloadGrid");
		
		$('#AwardedContractors').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
		$('#AwardedContractors').trigger("reloadGrid");
	}
	
	function customCurrencyFormatter(cellValue, options, rowObject) {
		return formatCurrency(cellValue);
	}
	
	function getQryStr(jobNumber, jobName) {
		var qryStr = "";
		$.ajax({
			async: false,
			url : "./search/searchjobcustomer",
			mType : "GET",
			data : {
				'jobnumber' : jobNumber,
				'jobname' : jobName
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
				updateJobstatus(jobNumber,status);
				qryStr = "jobNumber=" + jobNumber	+ "&jobName=" + jobName + "&jobCustomer=" + jobCustomer	 + "";
				return qryStr;
			},
			error : function(Xhr) {
			}
		});
		return qryStr;
	}