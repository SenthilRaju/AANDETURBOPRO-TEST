var salesOrderFlag=null;
var estimatedamountRelease;
var withPrice = 'NotChecked';
var hideWithPrice = "outsideJob";
var _blockjsfiles =true;
var fromDate='';
var toDate='';
var searchData='';
var globalinsideoroutsidejob=true;
var so_openornot;
var _global_override_taxTerritory=false;
var _dummyglobal_override_taxTerritory=false;
var _global_override_taxIDBasedOnCustomer;
jQuery(document).ready(function(){
	$("#fromDate").prop('disabled',true);
	 $("#toDate").prop('disabled',true);
	$('#withPriceLine').css('display','block');
	$('#withPriceLineLabel').css('display','block');
	$('#withPrice').css('display','block');
	$('#withPriceLabel').css('display','block');	
	$("#resetbutton").css("display", "inline-block");
		//$("input:text").addClass("ui-state-default ui-corner-all");	
	
	//alert("InventoryCusoid in js--"+InventoryCusoid);
	/*if(InventoryCusoid != null && InventoryCusoid != '')
		{
		alert("Inside Inventory");
		$('#operation').val('update');
		if(InventoryrxMasterid != null && InventoryrxMasterid !='')
			$('#rxCustomer_ID').text(InventoryrxMasterid);
		$('#Cuso_ID').text(InventoryCusoid);
		//PreloadDataFromInventory(InventoryCusoid);
		$('#salesrelease').dialog({height:710});
		$('#salesrelease').dialog("open");
		}*/
	   $("#salesrelease").dialog({
	    open : function(event, ui) {
	    $('#salesreleasetab').tabs({ selected: 0 });
	     var cusoid = $('#Cuso_ID').text();
	     loadshiptostateautocmpte("#SO_Shipto");
	     if (cusoid == '') {
	      $("#salesreleasetab").tabs({
	       disabled : [ 1, 2 ]
	      });
	      $('#loadingDivForSOGeneralTab').css({
				"display": "none"
			});
	     } else {
	      $("#salesreleasetab").tabs({
	       disabled : false
	      });
	     }
	     so_openornot=1;
	     setTimeout(function(){
				var cuSoid =  $('#Cuso_ID').text();
				if(typeof(cuSoid) == undefined || cuSoid == '' || cuSoid == 0 ){
					$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
				}	
				
				var new_so_generalform_values=generaltabformvalidation();
			    so_general_form =  JSON.stringify(new_so_generalform_values);
				},1000);
	    },
	    close: function () {
	    	$('#salesrelease').dialog("close");	
			$("#release").trigger("reloadGrid");
			return true;
		}
	   });
	   
	   
	   if($("#operation").val() != "projectSO")
		   {
			   if(typeof(inventory ) === 'undefined' || inventory !== 'InventoryPage')
				   {
				   $('#salesrelease').bind('dialogclose', function(event) {
				    	
				    	document.location.href ="./salesorder?oper=create";
				    });
				   }
			   else if(typeof(inventory ) !== 'undefined' || inventory === 'InventoryPage')
				   {
					$("#inventoryDetailsGrid").trigger("reloadGrid");
					loadInventoryDetailsGrid();
				   }
			   showsalesordergridList(searchData,fromDate,toDate);
		   }
	   else
		   {
			   if(typeof(inventory ) === 'undefined' || inventory !== 'InventoryPage')
				   {
				   $('#salesrelease').bind('dialogclose', function(event) {
				    	
				    	document.location.href ="./salesorder?oper=create";
				    });
				   }
			   else if(typeof(inventory ) !== 'undefined' || inventory === 'InventoryPage')
				   {
					$("#inventoryDetailsGrid").trigger("reloadGrid");
					loadInventoryDetailsGrid();
				   }
			   showsalesordergridList(searchData,fromDate,toDate);
			   
				var acuSoid = $('#pjcusoID').val();
				var aMasterID =	$('#rxCustomer_ID').val();
				
				
				$('#rxCustomer_ID').text(aMasterID);
				$('#Cuso_ID').text(acuSoid);
				$('#operation').val('update');
				createtpusage('Company-Customer-Sales Order','SO Grid-Data View','Info','Company-Customer-Sales Order,SO Grid-Data View,SO ID:'+acuSoid+',Customer_ID:'+aMasterID);	
				//PreloadData(acuSoid);
				loadEmailList(aMasterID);
				PreloadDataFromInventory(acuSoid);
				$('#salesrelease').dialog("open");
			   
			   
		   }
});

function showsalesordergridList(searchData,fromDate,toDate){
	try{
	searchData = $('#searchJob').val();
	$("#SalesOrdersGrid").jqGrid({
		url:'./so_listgrid?searchData='+searchData+'&fromDate='+fromDate+'&toDate='+toDate,
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#SalesOrdersGridPager'),
		colNames:[ 'Created On','vePOID','SO #','rxVendorID', 'joReleaseID', 'Customer','Job Name','Cost','Sell Price','Difference'],
		colModel :[
		    {name:'createdOn', index:'createdOn', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
           	{name:'vePOID', index:'vePOID', align:'left', width:40, editable:true,hidden:true},
			{name:'ponumber', index:'ponumber', align:'left', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'rxVendorID', index:'rxVendorID', align:'', width:80,hidden:true},
			{name:'joReleaseID', index:'joReleaseID', align:'center', width:30,hidden:true},
			{name:'vendorName', index:'vendorName', align:'left', width:80, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
			{name:'jobName', index:'jobName', align:'', width:40,hidden:false},			
			{name:'costtotal', index:'costtotal', align:'right', width:40,hidden:false,formatter:customCurrencyFormatterSellPrice},
			{name:'subtotal', index:'subtotal', align:'right', width:40,hidden:false,formatter:customCurrencyFormatter},
			{name:'difference', index:'difference', align:'right', width:40,hidden:false,formatter:customCurrencyFormatter}
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#SalesOrdersGridPager',
		sortname: 'createdOn', sortorder: "DESC",	imgpath: 'themes/basic/images',	caption: 'Sales order',
		//autowidth: true,
		height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
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
    		
    		
			var rowData = jQuery(this).getRowData(rowid); 
			var acuSoid = rowData['vePOID']; 
			var aMasterID = rowData['rxVendorID'];
			var aJoReleaseID = rowData['joReleaseID'];
			
			var soNumber =  rowData['ponumber'];
			 var re = /[A-Za-z]+$/;
			 if(re.test(soNumber)){
				 loadSplitCommissionList(0,aJoReleaseID)
				 	$('#splitDivs').show();
					//$("#splitDivs :input").prop("disabled", true);
					$("#splitDivs").children().removeAttr("disabled");
					$("#OriginalData").children().attr("disabled","disabled");
					$('#OriginalData').hide();
			 }else{
				 	$('#splitDivs').hide();
					$("#splitDivs").children().attr("disabled","disabled");
					$("#OriginalData").children().removeAttr("disabled");
					$('#OriginalData').show();
			 }
			
			$('#rxCustomer_ID').text(aMasterID);
			$('#Cuso_ID').text(acuSoid);
			invoicethereornotforsalesorder(acuSoid);
			$('#operation').val('update');
			createtpusage('Company-Customer-Sales Order','SO Grid-Data View','Info','Company-Customer-Sales Order,SO Grid-Data View,SO ID:'+acuSoid+',Customer_ID:'+aMasterID);	
			//PreloadData(acuSoid);
			loadEmailList(aMasterID);
			PreloadDataFromInventory(acuSoid);
			
			if(globalinsideoroutsidejob){
				var allowdivision=false;
				var allowcheckdivisionrequired=getSysvariableStatusBasedOnVariableName("RequireDivisioninSalesOrderOutsideofJob");
				if(allowcheckdivisionrequired!=null && allowcheckdivisionrequired[0].valueLong==1){
					allowdivision=true;
				}
				//alert(allowdivision);
				if(allowdivision){
					$('#divisionlabel').css('display','inline-block');
				}else{
					$('#divisionlabel').css('display','none');
				}
			}
			
			
			$('#salesrelease').dialog("open");
			
    	}
	}).navGrid('#SalesOrdersGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);	
	}catch(err){
		alert(err.message);
	}
}
function callEnableDate(){
	if(document.getElementById("fromDate").disabled){
	 $("#fromDate").prop('disabled',false);
	 $("#toDate").prop('disabled',false);
	}else{
		$("#fromDate").val("");
		 $("#toDate").val("");
		 fromDate="";toDate="";
		 $("#SalesOrdersGrid").jqGrid('GridUnload');
		 showsalesordergridList(searchData,fromDate,toDate);
		$("#fromDate").prop('disabled',true);
		 $("#toDate").prop('disabled',true);
	}
}

function dateFormatter(cellValue, options, rowObject) {
	/**var aDate = cellValue;
	var aDateArray = aDate.split("-");//2012-05-04
	var newDate = "" + aDateArray[1] + "/" + aDateArray[2] + "/" + aDateArray[0];*/
	if(cellValue == null)
		cellValue = "";
	return cellValue;
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}
function customCurrencyFormatterSellPrice(cellValue, options, rowObject) {
	//console.log("--"+cellValue)
	return formatCurrency(cellValue);
}
function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
	return cellValue;
}
function addNewSO()
{
	$('#splitDivs').hide();
	$("#splitDivs").children().attr("disabled","disabled");
	$("#OriginalData").children().removeAttr("disabled");
	$('#OriginalData').show();
	$("#dateOfcustomerGeneral").prop('disabled', false);
	if(globalinsideoroutsidejob){
	var allowdivision=false;
	var allowcheckdivisionrequired=getSysvariableStatusBasedOnVariableName("RequireDivisioninSalesOrderOutsideofJob");
	if(allowcheckdivisionrequired!=null && allowcheckdivisionrequired[0].valueLong==1){
		allowdivision=true;
	}
	if(allowdivision){
		$('#divisionlabel').css('display','inline-block');
	}else{
		$('#divisionlabel').css('display','none');
	}
	}
	$('#salesrelease').dialog({height:830});
	$('#salesrelease').dialog("open");
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function alphabetSeq(cellValue, options, rowObject){
	try {
		 var element = "";
		 if(cellValue !== null){
			 var aPonumber=cellValue;
			 var aPONumSplitArray = new Array();
			 aPONumSplitArray = $.trim(aPonumber).split("-");
			 var aStr = aPONumSplitArray[1].match(/[A-z]/g);
			 for(var i=0;i<aStr.length;i++){
				 element = element + (aStr[i]);
			 }
		 }
		return element;
	} catch (e) {
		alert("Error:" + e + "\n\nFalse will be returned.");
		return false;
	}
}
	
	function billImage(cellValue, options, rowObject){
		var element = '';
	   if(cellValue !== '' && cellValue !== null){
		   element = "<img src='./../resources/Icons/dollar.png' style='vertical-align: middle;'>";
	   }else if(cellValue === ''){
		   element = "";
	   }else if(cellValue === null){
		   element = "";
	   }else{
		   element = "";
	   }
	return element;
	} 

	
	function typeFormatter(cellvalue, options, rowObject) {
		if(cellvalue === null){
			return "";
		}else if(cellvalue === -1){
			return "";
		}else if(cellvalue === 1) {
			return "Drop Ship";
		} else if(cellvalue === 2) {
			return "Stock Order";
		} else if(cellvalue === 3) {
			return "Bill Only";
		} else if(cellvalue === 4) {
			return "Commission";
		} else if(cellvalue === 5) {
			return "Service";
		}
	}
	
	function billAmountCal(){
		 $('#SalesOrdersGrid').jqGrid('getGridParam', 'userData');
		var allRowsInGrid = $('#SalesOrdersGrid').jqGrid('getRowData');
		var allocateamount=$("#allocate").text();
		var unAllocatedAmount = $("#unAllocated").text().replace(/[^0-9\.]+/g,"");
		var aVal = new Array();
		var sum = 0;
		$.each(allRowsInGrid, function(index, value) { 
			aVal[index] = value.estimatedBilling;
			sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.]+/g,""));
		});
		/*if(typeof(estimatedamountRelease)!== undefined && estimatedamountRelease !== '' &&  estimatedamountRelease!== null){
			estimatedamountRelease=estimatedamountRelease.replace(/[^0-9\.]+/g,"");
			$("#estimated").text(formatCurrency(estimatedamountRelease));
			$('#allocate').empty();
			$("#allocate").text(formatCurrency(sum));
			$('#unAllocated').text(formatCurrency(estimatedamountRelease-sum));
		}*/
		var sumTotal = '';
		var allocatedamount=allocateamount.replace(/[^0-9\.]+/g,"");
		if(unAllocatedAmount > allocatedamount){
			sumTotal = Number(allocatedamount) - Number(unAllocatedAmount);
		}if(allocatedamount > unAllocatedAmount){
			sumTotal = Number(unAllocatedAmount) - Number(allocatedamount);
		}
//		$("#estimate").empty();
//		$("#estimate").text(formatCurrency(sumTotal));
	 }
	
	function viewPOPDF(){
		var cusoID = $('#Cuso_ID').text();
		var check = withPrice;
		console.log("Line -->"+check);
		if(cusoID != '' && cusoID != undefined)
			window.open("./salesOrderController/printSalesOrderReport?cusoID="+cusoID+"&price="+check);
		return true;
	}
	

	
/*	function viewPOPDFSave(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, numberof,withPrice){
		
		var newDialogDiv = jQuery(document.createElement('div'));
		var check = withPrice;
		var cusoID = $('#Cuso_ID').text();
		if(cusoID != '' && cusoID != undefined)
			jQuery(newDialogDiv).dialog({close:false,modal: true, width:300, height:150, title:"Generating PDF", 
				buttons: [] }).dialog("open");
			$.ajax({ 
				url: "./salesOrderController/printSalesOrderReport",
				mType: "GET",
				data : { 'cusoID' : cusoID,"price":check,"WriteorView":"write"},
				success: function(data){ 
					//jQuery(newDialogDiv).dialog("close");
					sendsubmitMailFunction1(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, numberof); 
					}
			});
			
		return true;
	}
	*/
	
	function PreloadDataFromInventory(cusoid){
		$('#salesreleasetab').tabs({ selected: 0 });
		$('#salesreleasetab').tabs({ active: 0 });
		
		var cuSOID = cusoid;
		var rxMasterID = $('#rxCustomer_ID').text();
	//	alert("RxMaster Id--->"+rxMasterID);
		if(cuSOID!=null && typeof(cuSOID) != "undefined"){
		$.ajax({
			url: "./salesOrderController/getPreLoadData",
			type: "POST",
			data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID,
			success: function(data) {
				//Load Shipto Address
				if(data.ashiptoJomaster!=null)
				GLB_joMasterID=data.ashiptoJomaster.joMasterId;
				loadCustomerAddress(data.Cuso.rxCustomerId);
				
				var divflag = "#SO_Shipto";;
				$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val("");
				 $(divflag).contents().find("#shiptomodehiddenfromdbid").val("");
				var shiptomode = data.Cuso.shipToMode;
				var checkshiptoid;
				//load shipto autocomplete
				loadshiptostateautocmpte(divflag);
				
				if(shiptomode == "0")
				{
					checkshiptoid = data.Cuso.prToWarehouseId;
				}
				else if(shiptomode == "1" || shiptomode == "2")
				{
					checkshiptoid = data.Cuso.rxShipToId;
				}
				else
				{
					checkshiptoid = data.Cuso.rxShipToAddressId;
				}
				
				
				if(checkshiptoid!=null)
				{
					if(data.Cuso.shipToMode == 0)
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.prToWarehouseId);
					}
					else if(data.Cuso.shipToMode == 1)
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.rxShipToId);
					}
					else if(data.Cuso.shipToMode == 2)
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.rxShipToId);
					}
					else
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.rxShipToAddressId);
					}
					//alert(data.shipToMode)
					$(divflag).contents().find("#shiptomodehiddenfromdbid").val(data.Cuso.shipToMode);
					preloadShiptoAddress(divflag,data.Cuso.cuSoid,checkshiptoid,data.Cuso.shipToMode,'0',$("#jobCustomerName_ID").text(),data.Cuso.coTaxTerritoryId);
					$(divflag).contents().find("#shiptomoderhiddenid").val(data.Cuso.shipToMode);
				}
				else
				{
					preloadShiptoAddress(divflag,data.Cuso.cuSoid,null,'2','0',$("#jobCustomerName_ID").text(),data.Cuso.coTaxTerritoryId);
					$(divflag).contents().find("#shiptomoderhiddenid").val('2');
				}
				
				var joReleaseIds = data.Cuso.joReleaseId;
				if(joReleaseIds===null){
				    $('#splitDivs').hide();
					$("#splitDivs").children().attr("disabled","disabled");
					$("#OriginalData").children().removeAttr("disabled");
					$('#OriginalData').show();
					 $("#shiptoaddradio3").button({disabled:true});
					 $('#shiptoaddlabel3').attr('onclick','').unbind('click');
				}else{
				    $('#splitDivs').show();
					//$("#splitDivs :input").prop("disabled", true);
					$("#splitDivs").children().removeAttr("disabled");
					$("#OriginalData").children().attr("disabled","disabled");
					$('#OriginalData').hide();
					loadSplitCommissionList(0,joReleaseIds)
				}
				
				$("#CustomerNameGeneral").val(data.CustomerName);
				$('#soEmailTimeStamp').empty();	$('#soEmailTimeStamp').append(data.emailTime);
				$('#soLinesEmailTimeStamp').empty();$('#soLinesEmailTimeStamp').append(data.emailTime);
				if (typeof(data.Cuso) != "undefined" && data.Cuso != null){
				$('#dateOfcustomerGeneral').val($("#poDate_ID").text());
				$("#SOnumberGeneral").val(data.Cuso.sonumber);
				$('#SOGeneral_subTotalID').val(formatCurrency(0));
				$('#SOGeneral_totalID').val(formatCurrency(0));
				$('#termhiddenID').val(data.Cuso.cuTermsId);
				$('#poID').val(data.Cuso.trackingNumber);
				$('#promisedID').val(data.Cuso.datePromised);
				$('#CreatedOn').val(data.Cuso.createdOn);
				//alert("Hello");
				$('#custID').val(data.Cuso.customerPonumber);
				$('#tagJobID').val(data.Cuso.tag);
				$('#SOGeneral_frightID').val(formatCurrency(data.Cuso.freight)); 
				$('#salesmanID').val(data.SalesMan);
				$('#csrID').val(data.Cuso.cuAssignmentId1);
				$('#whrhouseID').val(data.Cuso.prFromWarehouseId);
				$('#SOshipViaId').val(data.Cuso.veShipViaId);
				$('#SOdivisionID').val(data.Cuso.coDivisionID);
				$('#terms').val(data.Cuso.cuTermsId);
				$('#salesmanID').val(data.SalesMan);
				$('#csrID').val(data.AE);
				$('#engineerID').val(data.Submitting);
				$('#salesManagerID').val(data.Costing);
				$('#projectManagerID').val(data.Ordering);
				$('#SOGeneral_taxvalue').val(formatCurrency(data.Cuso.taxTotal));
				$('#cuSOid').val(data.Cuso.cuSoid);
				
				//$('#taxIDwz').val();
				//$('#taxhiddenIDwz').val();
				//alert(data.Cuso.customerPonumber);
				$('#termswz').val(data.Cuso.cuTermsId);
				$('#termhiddenIDwz').val(data.Cuso.cuTermsId);
				$('#custIDwz').val(data.Cuso.customerPonumber);
				$('#tagJobIDwz').val(data.Cuso.tag);
				$('#SOdivisionIDwz').val(data.Cuso.coDivisionID);
				$('#promisedIDwz').val(data.Cuso.datePromised);
				//$('#soDivisionIdwz').val();
				//$('#soPromisedIdwz').val();
				
				$('#prToWarehouseId').val(data.Cuso.prToWarehouseId);
				console.log('WHID SO List:'+$('#prToWarehouseId').val());
				
				if($('#operation').val() === 'update'){
					$("#rxCustomer_ID").text(data.Cuso.rxBillToId);
					}
				if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
					
					$("#rxShipToOtherAddressID").val(data.Cuso.rxShipToAddressId);	
					$("#shipTorxCustomer_ID").val(data.Cuso.rxCustomerId);
					$("#shipToCustomerAddressID").val(data.Cuso.rxShipToId);
					$("#shiptoIndexValuefortoggle").val(data.Cuso.shipToIndex);
					
					if($("#rxCustomer_ID").text() != null)
						{
					//alert(data.Cuso.rxContactId);
					//	loadEmailList($("#rxCustomer_ID").text());
						$("#emailList option[value='" + data.Cuso.rxContactId + "']").attr("selected", true);
						}
					//	loadEmailList($("#rxCustomer_ID").text());
					$('#transactionStatus').val(data.Cuso.transactionStatus);
					console.log('Transaction Status:'+data.Cuso.transactionStatus);
					
					// --- Added by:Leo   Date:08/03/2016  ID#:505
					if(data.Cuso.withpriceStatus)
					{
						$('#withPrice').attr('checked',true);
				    	$('#withPriceLine').attr('checked',true);
				    	withPrice='Checked';
					}
					else
					{
						$('#withPrice').attr('checked',false);
				    	$('#withPriceLine').attr('checked',false);
				    	withPrice='NotChecked';
					}
					//---- 
					
					setStatusButton();
					var mode = data.Cuso.shipToMode;
					
					if(mode == 0){
						$("#shiptolabel1").addClass('ui-state-active');
						$("#shiptolabel2").removeClass('ui-state-active');
						$("#shiptolabel4").removeClass('ui-state-active');
						$("#shiptolabel3").removeClass('ui-state-active');
						//shipToAddressCreate('Pick Up');
						console.log('salesOrder.js Called Mode-0');
					}
					else if(mode == 1){
						console.log('salesOrder.js Called Mode-1');
						$("#shiptolabel1").removeClass('ui-state-active');
						$("#shiptolabel2").addClass('ui-state-active');
						$("#shiptolabel4").removeClass('ui-state-active');
						$("#shiptolabel3").removeClass('ui-state-active');
						//shipToAddressCreate('Customer');
					}
					else if(mode == 3){
						console.log('salesOrder.js Called Mode-3');
						$("#shiptolabel1").removeClass('ui-state-active');
						$("#shiptolabel2").removeClass('ui-state-active');
						$("#shiptolabel4").addClass('ui-state-active');
						$("#shiptolabel3").removeClass('ui-state-active');
						//shipToAddressCreate('Other');
					}
					else if(mode == 2){
						console.log('salesOrder.js Called Mode-2');
						$("#shiptolabel1").removeClass('ui-state-active');
						$("#shiptolabel2").removeClass('ui-state-active');
						$("#shiptolabel4").removeClass('ui-state-active');
						$("#shiptolabel3").addClass('ui-state-active');
						//shipToAddressCreate('Job Site');
					}
					
					$('#SOGeneral_taxId').val((data.Cuso.taxRate));
					loadTaxTerritoryRate(data.Cuso.coTaxTerritoryId,"");
					
				}
				if($('#operation').val() !== 'create' && $('#operation').val() !== 'edit'){
					addressToShip();
					}
				var createdDate = data.Cuso.createdOn;
				if (typeof (createdDate) != 'undefined') 
					FormatDate(createdDate);
				var shipDate = data.Cuso.shipDate;
				if (typeof (shipDate) != 'undefined') 
					FormatShipDate(shipDate);
				if (typeof(data.Cusodetail) != "undefined" && data.Cusodetail != null){
					$('#SOGeneral_subTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
					$('#SOGeneral_totalID').val(formatCurrency(data.Cusodetail.taxTotal));
					}
				else{
					$('#SOGeneral_subTotalID').val(formatCurrency(0));
					$('#SOGeneral_totalID').val(formatCurrency(0));
					$('#SOGeneral_frightID').val(formatCurrency(0));
					
				}
					formattax(data.Cuso.freight);
				}
				setTimeout(function(){
					$("#so_taxfreight").val(data.Cuso.taxfreight);
					setTaxTotal_SO();
				$('#loadingDivForSOGeneralTab').css({
					"display": "none"
				});
				var new_so_generalform_values=generaltabformvalidation();
			    so_general_form =  JSON.stringify(new_so_generalform_values);
				},1000);
			}
		});
	  }
	}
	function getSearchDetails()
	{
		if($('#searchJob').val() != null && $('#searchJob').val() != '')
		{
			$("#searchJob").autocomplete({
						disabled: true
					});
			//$("#searchJob").attr("autocomplete", "off");
			searchData = $('#searchJob').val();			
			$("#SalesOrdersGrid").jqGrid('GridUnload');
			showsalesordergridList(searchData,fromDate,toDate);
			$("#SalesOrdersGrid").trigger("reloadGrid");
			$('#searchJob').val('');
			//$("#searchJob").removeAttr("autocomplete");
		}			
	}	

	function ResetDetails() {
		searchData = '';
		$('#searchJob').val('');
		showsalesordergridList(searchData,fromDate,toDate);
		$("#SalesOrdersGrid").trigger("reloadGrid");
	}
	
	$( "#fromDate" ).change(function() {
		fromDate = $("#fromDate").val();
		$("#SalesOrdersGrid").jqGrid('GridUnload');
		showsalesordergridList(searchData,fromDate,toDate);
		$("#SalesOrdersGrid").trigger("reloadGrid");
		});
	$( "#toDate" ).change(function() {
		toDate = $("#toDate").val();
		$("#SalesOrdersGrid").jqGrid('GridUnload');
		showsalesordergridList(searchData,fromDate,toDate);
		$("#SalesOrdersGrid").trigger("reloadGrid");
		});

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

	function global_override_taxIDBasedOnCustomer(){
		//alert("hileo");
		
		var customerID=$("#JobCustomerId").val();
		$.ajax({
			url : "./salesOrderController/getoverride_taxterritory",
			type : "POST",
			data : {"customerID" : customerID},
			success:function(data) {
				if(data!=null){
					_global_override_taxIDBasedOnCustomer=data.coTaxTerritoryId;
				}
			}
		});
	}
	
	function invoicethereornotforsalesorder(cusoid){
		$.ajax({
			url: "./salesOrderController/checkSalesOrderInvoicedornot",
			type: "POST",
			data : {"CuSOID" : cusoid},
			success: function(data) {
				if(data==true){
					$("#dateOfcustomerGeneral").prop('disabled', true);
				}else{
					$("#dateOfcustomerGeneral").prop('disabled', false);
				}
				}
			});
	}