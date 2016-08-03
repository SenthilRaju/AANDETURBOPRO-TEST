var shipdateMode;
var freight;
var totalGeneral ;
var splitCommissionGridGlobalVar;
var SplitCommissionID;
var commission_PO_grid_form;
$(document).ready(function() {
	$(".datepicker").datepicker();
	$("#POReleaseID").show();
	$("#shipTo1").hide();
	$("#rxManuId").val();
	$('#loadingPOGenDiv').css({"visibility": "hidden"});
	checkQB();
	 var gridDatas = $('#PoSplitCommissionGrid').getRowData();
	 var gridDatasGlobal = JSON.stringify(gridDatas);
	splitCommissionGridGlobalVar = gridDatasGlobal;
	chkSplitCommissionPOValidation();
	

	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus.indexOf("Closed")>-1){
		$('#POReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		$('#POReleaseID').css('cursor','default');
		document.getElementById("POReleaseID").disabled = true;
	}else{
		document.getElementById("POReleaseID").disabled = false;
		$('#POReleaseID').css('cursor','pointer');
		$('#POReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	}
	
});


	
function chkSplitCommissionPOValidation(){
	var chkSplitCommissionYN = getSysvariableStatusBasedOnVariableName("SplitCommissionRequiredOnRelease");
	if(chkSplitCommissionYN!=null && chkSplitCommissionYN[0].valueLong==1){
		$('#splitCommissionPOLabel').css('display','inline-block');
		commissionRequired = 1;
	}
	}	

var operationVar;
var asubManu;
var aVepoID;
$("#freightGeneralId").keypress(function (e) {
	if (e.which != 46 && e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
         return false;
   }
  });
function loadContactList(manufacture){ 
	
	if(!manufacture){
		var errorText = "Please select a release to edit the Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning", 
								buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		$("#vendorLineNameId").val(""); $("#ourPoLineId").val(""); $("#subtotalLineId").val("");
		$("#totalLineId").val(""); $("#taxLineId").val("");$("#freightLineId").val("");
		$("#lineID").val("");
		$("#poDateLineId").val("");
	}
	$.ajax({
		url: "./jobtabs2/filterBidderList", mType: "GET", data: { 'rxMasterId' : manufacture },
		success: function(data) {						
			var select = '<select style="width:227px;margin-left: 3px;" id="contactId" name="attnName" onchange="getContactId();POGeneralTabformChanges();"><option value="-1"> - Select -</option>';
			$.each(data, function(index, value){
				quoteId = value.id;
				var quoteName = value.value;
				select +='<option value='+quoteId+'>'+quoteName+'</option>';
			});
			select += '</select>';
			$("#contacthiddenID").hide();
			$('#contactselectID').empty();
			$('#contactselectID').append(select);
		}
	});
	getContactId = function(){
		var contactId = $("#contactId").val();
		$("#rxContactId").val(contactId);
	};
}
$("#usBillto").onclick=function () {
	$('#usBillimage').attr('src', '');
};
function checkQB(){
	$.ajax({
		url: "./qbsetting", 
		mType: "GET", 
		data : {},
		success: function(data){
			if(data===null){
				$('#qbtr').show();
				$('#qbtr input').removeAttr('disabled');
			}else{
				if(data.qbEnabled==1){
					$('#qbtr').show();
					$('#qbtr input').removeAttr('disabled');
				}else if(data.qbEnabled==0){
					$('#qbtr input').prop('disabled', true);
				}
			}
			return true;
		}
	});
}


function addPOrelease(manufacture, jobNumber, quoteNumber,manufactureName,joreleaseID,vePOId,veFactoryId,aNewPONumber){ 
	loadContactList(manufacture);
	var aManuId = $("#rxCustomer_ID").text();
	$("#rxManuId").val(aManuId);
	$("#joReleaseid").val(joreleaseID);
	$("#manfactureId").val(manufacture);
	$("#manfactureName").val(manufactureName);
	$("#customerNAMEId").val(""); 
//	$("#customerNAMEId option[value=-1]").attr("selected", true);
	$("#subtotalGeneralId").val(formatCurrency(0)); 
	$("#totalGeneralId").val(formatCurrency(0)); 
	var tax2 = taxTerritory.split("%");
	$("#taxGeneralId").val(tax2[0]); 
	$("#freightGeneralId").val(formatCurrency(0));
	$("#generalID").val(formatCurrency(0)); 
	$("#Subtotal_ID").text(formatCurrency(0));
	$("#Freight_ID").text(formatCurrency(0));
	$("#Tax_ID").text(tax2[0]);
	$("#order_ID").text("");
	$("#Percentage_ID").text(formatCurrency(0));
	$("#Total_ID").text(formatCurrency(0));
	$("#veFactoryID").val(veFactoryId);
	$("#vePOID").val(vePOId);
	$("#vePO_ID").text(vePOId);
	$("#vendor_ID").text(manufactureName);
	$("#specialInstructionID").val(""); 
	$("#wantedId").val("");
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd;} 
	if(mm<10){mm='0'+mm;} 
	today = mm+'/'+dd+'/'+yyyy;
	$("#poDateId").val(today);
	$("#poDate_ID").empty();
	$("#tagId").val($("#jobName_ID").text());
	$("#ourPoId").val(aNewPONumber); 
	$("#poDate_ID").text(today);
	$("#frieghtChangesId option[value=" + -1 + "]").attr("selected", true);
	$("#shipViaId option[value=" + -1 + "]").attr("selected", true);
	jQuery("#porelease").dialog("open");
	$("#tabs_main_job").tabs("select", 4);
	return true;
}
var dataObj = new Object();


function loadDropshipRelease(releaseID,val){
	console.log('loadDropshipRelease No Entries');
	var jobnumber=$("#jobNumber_ID").text();
	$.ajax({
		url: "./jobtabs5/getReleaseDetail", 
		mType: "GET", 
		data : { 'joReleaseID' : releaseID},
		success: function(data){
			
			/*JT
			 * $("#quickbookId").val(); 
			$("#ourPoId").val(); 
			$("#order_ID").text();*/
			$("#joReleaseid").val(releaseID);
			$("#vePOID").val();
			$("#vePO_ID").text();
			$("#veFactoryID").val();
			$("#emailTimeStamp").empty(); 
			$("#emailTimeStamp").append();
			$("#POtransactionStatus").val(1);
			$("#POtransactionStatus_Dummy").val(1);
			 if($('#POtransactionStatus').val()=='2'){
					$('#PoStatusButton').val('Close');
				}
				else if($('#POtransactionStatus').val()=='0'){
					$('#PoStatusButton').val('Hold');
				}
				else if($('#POtransactionStatus').val()=='1'){
					$('#PoStatusButton').val('Open');
				}
				else if($('#POtransactionStatus').val()=='-1'){
					$('#PoStatusButton').val('Void');
				}
			
			/*JT if(qbPO != null){
				$("#quickBookID").attr('disabled','disabled');
				$("#quickbookId").attr('disabled','disabled');
			}
			if(freightCharges !== '' && freightCharges !== null){ 
				//$("#frieghtChangesId option[value=" + freightCharges + "]").attr("selected", true);
				$("#frieghtChangesId").val(freightCharges);
			}else{ 
				$("#frieghtChangesId option[value=" + -1 + "]").attr("selected", true);
			}*/
			/*JT if(shipViaId !== '' && shipViaId !== null){ 
				//$("#shipViaId option[value=" + shipViaId + "]").attr("selected", true);
				$("#shipViaId").val(shipViaId);
			}else{ 
				$("#shipViaId option[value=" + -1 + "]").attr("selected", true);
			}
			if(contactID !== '' && contactID !== null){ 
				//alert(contactID);
				$("#contactId option[value=" + contactID + "]").attr("selected", true);
				//$("#contactId").val(contactID);
			}else{ 
				$("#contactId option[value=" + -1 + "]").attr("selected", true);
			}*/
			$("#manfactureName").val();
			$("#vendor_ID").text();
			$("#tagId").val($("#jobName_ID").text());
			
			/*JT if(orderedId !== '' && orderedId !== null){
				$("#orderhiddenId").val(orderedId);
			$("#orderId").val(userName); 
			}*/
			/*if(isNaN(aCustomerPONumber)===false){
				$("#customerNAMEId option[value=-1]").attr("selected", true);
				$("#customerNAMEId option[value=" + aCustomerPONumber + "]").attr("selected", true);
			}else{
				$("#customerNAMEId option[value=-1]").attr("selected", true);
			}*/
			var billToID = 0;
			if(billToID == 0){
				usBilltoSelectAddress();
			}else if(billToID == 1){
				 customerBilltoSelectAddress();
			}else if(billToID == 2){
				otherBilltoSelectAddress();
			}
			
			/*if(shipToID == 0){
				 usShiptoSelectAddress();
			}else if(shipToID == 1){
				customerShiptoSelectAddress();
			}else if(shipToID == 2){
				 jobsiteShiptoSelectAddress();
			}else if(shipToID == 3){
				 otherShiptoSelectAddress();*/
			
			//alert($("#jobCustomerName_ID").text());
			
			//alert(shipdateMode);
			
			if(shipdateMode == 0){
				$("#shiptoradio1").removeAttr("Checked");
				$("#shiptoradio2").removeAttr("Checked");
				$("#shiptoradio3").removeAttr("Checked");
				$("#shiptoradio").removeAttr("Checked");
				
				
				 $('#shiptolabel1').removeClass("ui-state-active");
				 $('#shiptolabel2').removeClass("ui-state-active");
				 $('#shiptolabel3').removeClass("ui-state-active");
				 $('#shiptolabel4').removeClass("ui-state-active");
				
		         loadShipToAddress(shipdateMode,shipToID,$("#jobCustomerName_ID").text());
			}else if(shipdateMode == 1){
				$("#shiptoradio1").removeAttr("Checked");
				$("#shiptoradio2").removeAttr("Checked");
				$("#shiptoradio3").removeAttr("Checked");
				$("#shiptoradio").removeAttr("Checked");
				
				 $('#shiptolabel1').removeClass("ui-state-active");
				 $('#shiptolabel2').removeClass("ui-state-active");
				 $('#shiptolabel3').removeClass("ui-state-active");
				 $('#shiptolabel4').removeClass("ui-state-active");

				loadShipToAddress(shipdateMode,shipToID,$("#jobCustomerName_ID").text());
			}else if(shipdateMode == 2){
				 jobsiteShiptoSelectAddress();
			}else if(shipdateMode == 3){
				//following line commented by Zenith on 02/19/2015
				 //otherShiptoSelectAddress();
				otherShiptoAddressPO();
			}
			$("#manfactureId").val(); 
			$("#ShiphiddenId").val();
			//$("#customerNAMEId").val(customerPoNum); 
			$("#subtotalGeneralId").val(formatCurrency(0));
			//totalGeneral = Number(subTotal) + Number(taxTotal) + Number(freight);
			$("#totalGeneralId").val(formatCurrency(0));
			$("#generalID").val(formatCurrency(0));
			$("#taxGeneralId").val(0); 
			$("#Tax_ID").text(0);
			/*JT if(taxRate !== '' && taxRate !== null){
				$("#taxGeneralId").val(0); 
				$("#Tax_ID").text(0);
			}else{
				var tax2 = taxTerritory.split("%");
				$("#taxGeneralId").val(tax2[0]);
				$("#Tax_ID").text(tax2[0]);
			}*/
			$("#freightGeneralId").val(formatCurrency(0));
			
			$("#specialInstructionID").val(); 
			$("#wantedId").val();
			$("#wantedComboId").val();
			$("#Subtotal_ID").text(formatCurrency(0));
			$("#Freight_ID").text(formatCurrency(0));
			$("#Percentage_ID").text();
			$("#Total_ID").text();

			/*
			 * JT
			 * if(poDate !== '' && poDate !== null){
				var poDay = new Date().getDate();
				var poMonth = new Date().getMonth();
				var poYear = new Date().getFullYear();
				if(poDay<10){poDay='0'+poDay;} 
				if(poMonth<10){poMonth='0'+poMonth;} 
				poDate = (Number(poMonth)+1) +"/"+ poDay +"/"+ poYear;
				$("#poDateId").val(poDate);
				$("#poDate_ID").empty();
				$("#poDate_ID").text(poDate);
			}else{*/
				/*var today = new Date();
				var dd = today.getDate();
				var mm = today.getMonth()+1; 
				var yyyy = today.getFullYear();
				if(dd<10){dd='0'+dd;} 
				if(mm<10){mm='0'+mm;} 
				today = mm+'/'+dd+'/'+yyyy;*/
				var grid = $("#release");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var vePOdate = grid.jqGrid('getCell', rowId, 'released');
				$("#poDateId").val(vePOdate);
				$("#poDate_ID").empty();
				$("#tagId").val($("#jobName_ID").text());
				$("#poDate_ID").text(vePOdate);
			//}
			
			/**
			 * When we opeen line items first time onlt the data is loading properly.  if we open second time, the line item js file is already cached by DOM.
			 * So these values will not be reloaded correctly.
			 * 
			 * IT IS A SEVER BUG
			 * **/
			/****************** Data related to line items ***********************/
			$("#vendorLineNameId").val($("#vendor_ID").text()); 
			$("#ourPoLineId").val($("#order_ID").text()); 
			$("#subtotalLineId").val($("#Subtotal_ID").text()); 
			$("#totalLineId").val($("#Total_ID").text()); 
			$("#taxLineId").val($("#Tax_ID").text());
			$("#freightLineId").val(formatCurrency(freight));
			$("#lineID").val($("#Percentage_ID").text());
			$("#poDateLineId").val($("#poDate_ID").text());
			/*********************************************************************/
			
			/********************* Data related to line items ********************/
			$("#vendorAckNameId").val($("#vendor_ID").text());
			$("#ourPoAckId").val($("#order_ID").text());
			$("#subtotalKnowledgeId").val($("#Subtotal_ID").text()); 
			$("#totalKnowledgeId").val($("#Total_ID").text());
			$("#taxKnowledgeId").val($("#Tax_ID").text());
			$("#freightKnowledgeId").val(formatCurrency(freight));
			$("#KnowledgeID").val($("#Percentage_ID").text());
			$("#poDateAckId").val($("#poDate_ID").text());
			/*********************************************************************/
			
			/* Jenith Testing Ends*/
			
			$("#porelease").dialog('option', 'title', '');
			if(val === "Commission")
				$("#porelease").dialog('option', 'title', 'Commission Order');
			else
				$("#porelease").dialog('option', 'title', 'Purchase Order');
			
			jQuery("#porelease").dialog("open");
			return true;
		}
	});
	$("#poreleasetab").tabs("select", 0);
}

/** Auto suggest for Manufacturer list **/
$(function() { var cache = {}; var lastXhr='';
$( "#manfactureName" ).autocomplete({ minLength: 2, select: function( event, ui ) 
	{
		var id = ui.item.id; 
		$("#manfactureId").val(id);
		loadContactList(id);
	},
	source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
	lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
		cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
		error: function (result) {
			$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

function loadPORelease(manufacturer,vePoId,manufacturerName, aCustomerPONumber, val){
    //	totalGeneral=0;
    // aTotal=0;
    //	freight=0;
		loadContactList(manufacturer);
	var jobnumber=$("#jobNumber_ID").text();
	$("#vePO_ID").text(vePoId);
	$.ajax({
		url: "./jobtabs3/getPurchaseDetails", 
		mType: "GET", 
		data : { 'vePoID' : vePoId,'jobNumber':jobnumber },
		success: function(data){
			dataObj = data;
			setTimeout(function(){prePopulateGeneralInfo(data,manufacturer,vePoId,manufacturerName, aCustomerPONumber,"PO_Shipto");}, 500);
			$("#porelease").dialog('option', 'title', '');
			if(val === "Commission")
				$("#porelease").dialog('option', 'title', 'Commission Order');
			else
				$("#porelease").dialog('option', 'title', 'Purchase Order');
			
			$("#CI_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
			$("#SO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
			$("#PO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset();
			
			 $( "#poreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
			 $( "#poreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
			 $( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
			 setTimeout(function(){
					var new_po_generalform_values=poGeneralTabFormValidation();
				    po_General_tab_form  =  JSON.stringify(new_po_generalform_values);
					},1000);
			
			jQuery("#porelease").dialog("open");
			return true;
		}
	});
	$("#poreleasetab").tabs("select", 0);
}

function prePopulateGeneralInfo(data,manufacturer,vePoId,manufacturerName, aCustomerPONumber,divflag){

	divflag ="#"+divflag;
	$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val("");
	 $(divflag).contents().find("#shiptomodehiddenfromdbid").val("");
	var shiptomode = data.shipToMode;
	var checkshiptoid;
	//load shipto autocomplete
	loadshiptostateautocmpte(divflag);
	
	if(shiptomode == "0")
	{
		checkshiptoid = data.prWarehouseId;
	}
	else if(shiptomode == "1" || shiptomode == "2")
	{
		checkshiptoid = data.rxShipToId;
	}
	else
	{
		checkshiptoid = data.rxShipToAddressId;
	}
	
	
	if(checkshiptoid!=null)
	{
		if(data.shipToMode == 0)
		{
			$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.prWarehouseId);
		}
		else if(data.shipToMode == 1)
		{
			$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.rxShipToId);
		}
		else if(data.shipToMode == 2)
		{
			$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.rxShipToId);
		}
		else
		{
			$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(data.rxShipToAddressId);
		}
		$(divflag).contents().find("#shiptomodehiddenfromdbid").val(data.shipToMode);
		
		preloadShiptoAddress(divflag,data.vePoid,checkshiptoid,data.shipToMode,'0',$("#jobCustomerName_ID").text(),"");
		$(divflag).contents().find("#shiptomoderhiddenid").val(data.shipToMode);
	}
	else
	{
		preloadShiptoAddress(divflag,data.vePoid,null,'2','0',$("#jobCustomerName_ID").text(),"");
		$(divflag).contents().find("#shiptomoderhiddenid").val('2');
		POGeneralTabformChanges();
	}
	
	
	
	var aManuId = $("#rxCustomer_ID").text();
	$("#rxManuId").val(aManuId);
	var vePOid = data.vePoid;
	var tag = data.tag;
	var poNumber = data.ponumber;
	var customerPoNum = data.customerPonumber;
	var releaseId = data.joReleaseId;
	var shipViaId = data.veShipViaId;
	var qbPO = data.qbPO;
	var freightCharges = data.veFreightChargesId;
	var orderedId = data.orderedById;
	var subTotal = data.subtotal;
	var taxTotal = data.taxTotal;
	var taxRate = data.taxRate;
	var userName = data.tsFullName;
	freight = data.freight;
	var spcInstr = data.specialInstructions;
	var wanted = data.dateWanted;
	var wantedOnOrBefore = data.wantedOnOrBefore;
	var poDate = data.orderDate;
	shipdateMode = data.shipToMode;
	var contactID = data.rxVendorContactId;
	var factoryID = data.veFactoryId;
	var billToID = data.billTo;
	var shipToID = data.shipTo;
	var emailDate = data.emailTimeStamp;
	var rxVendorID  = data.rxVendorId;
	
	var shipToAddressID =data.rxShipToAddressId;
	
	if(shipToAddressID != null)
		$("#rxAddressShipID").val(shipToAddressID);
	
	//alert(shipToAddressID);
	
	$("#quickbookId").val(qbPO); 
	$("#ourPoId").val(poNumber); 
	$("#order_ID").text(poNumber);
	$("#joReleaseid").val(releaseId);
	$("#vePOID").val(vePOid);
	$("#vePO_ID").text(vePOid);
	$("#veFactoryID").val(factoryID);
	$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(emailDate);
	$("#POtransactionStatus").val(data.transactionStatus);
	$("#POtransactionStatus_Dummy").val(data.transactionStatus);
	console.log("In PO Release: "+data.transactionStatus);
	
	 if($('#POtransactionStatus').val()=='2'){
			$('#PoStatusButton').val('Close');
		}
		else if($('#POtransactionStatus').val()=='0'){
			$('#PoStatusButton').val('Hold');
		}
		else if($('#POtransactionStatus').val()=='1'){
			$('#PoStatusButton').val('Open');
		}
		else if($('#POtransactionStatus').val()=='-1'){
			$('#PoStatusButton').val('Void');
		}
	
	if(qbPO != null){
		$("#quickBookID").attr('disabled','disabled');
		$("#quickbookId").attr('disabled','disabled');
	}
	if(freightCharges !== '' && freightCharges !== null){ 
		//$("#frieghtChangesId option[value=" + freightCharges + "]").attr("selected", true);
		$("#frieghtChangesId").val(freightCharges);
	}else{ 
		$("#frieghtChangesId option[value=" + -1 + "]").attr("selected", true);
	}
	if(shipViaId !== '' && shipViaId !== null){ 
		//$("#shipViaId option[value=" + shipViaId + "]").attr("selected", true);
		$("#shipViaId").val(shipViaId);
	}else{ 
		$("#shipViaId option[value=" + -1 + "]").attr("selected", true);
	}
	if(contactID !== '' && contactID !== null){ 
		//alert(contactID);
		$("#contactId option[value=" + contactID + "]").attr("selected", true);
		//$("#contactId").val(contactID);
	}else{ 
		$("#contactId option[value=" + -1 + "]").attr("selected", true);
	}
	$("#manfactureName").val(manufacturerName);
	$("#vendor_ID").text(manufacturerName);
	if(tag !== '' && tag !== null){
		$("#tagId").val(tag); 
	}else{
		$("#tagId").val($("#jobName_ID").text());
	}
	if(orderedId !== '' && orderedId !== null){
		$("#orderhiddenId").val(orderedId);
	$("#orderId").val(userName); 
	}
	/*if(isNaN(aCustomerPONumber)===false){
		$("#customerNAMEId option[value=-1]").attr("selected", true);
		$("#customerNAMEId option[value=" + aCustomerPONumber + "]").attr("selected", true);
	}else{
		$("#customerNAMEId option[value=-1]").attr("selected", true);
	}*/
	if(billToID == 0){
		usBilltoSelectAddress();
	}else if(billToID == 1){
		 customerBilltoSelectAddress();
	}else if(billToID == 2){
		otherBilltoSelectAddress();
	}
	
	/*if(shipToID == 0){
		 usShiptoSelectAddress();
	}else if(shipToID == 1){
		customerShiptoSelectAddress();
	}else if(shipToID == 2){
		 jobsiteShiptoSelectAddress();
	}else if(shipToID == 3){
		 otherShiptoSelectAddress();*/
	
	//alert($("#jobCustomerName_ID").text());
	
	//alert(shipdateMode);
	
	if(shipdateMode == 0){
		$("#shiptoradio1").removeAttr("Checked");
		$("#shiptoradio2").removeAttr("Checked");
		$("#shiptoradio3").removeAttr("Checked");
		$("#shiptoradio").removeAttr("Checked");
		
		
		 $('#shiptolabel1').removeClass("ui-state-active");
		 $('#shiptolabel2').removeClass("ui-state-active");
		 $('#shiptolabel3').removeClass("ui-state-active");
		 $('#shiptolabel4').removeClass("ui-state-active");
		
      //   loadShipToAddress(shipdateMode,shipToID,$("#jobCustomerName_ID").text());
	}else if(shipdateMode == 1){
		$("#shiptoradio1").removeAttr("Checked");
		$("#shiptoradio2").removeAttr("Checked");
		$("#shiptoradio3").removeAttr("Checked");
		$("#shiptoradio").removeAttr("Checked");
		
		 $('#shiptolabel1').removeClass("ui-state-active");
		 $('#shiptolabel2').removeClass("ui-state-active");
		 $('#shiptolabel3').removeClass("ui-state-active");
		 $('#shiptolabel4').removeClass("ui-state-active");

	//	loadShipToAddress(shipdateMode,shipToID,$("#jobCustomerName_ID").text());
	}else if(shipdateMode == 2){
	//	 jobsiteShiptoSelectAddress();
	}else if(shipdateMode == 3){
		//following line commented by Zenith on 02/19/2015
		 //otherShiptoSelectAddress();
	//	otherShiptoAddressPO();
	}
	$("#manfactureId").val(manufacturer); 
	$("#ShiphiddenId").val(shipViaId);
	$("#customerNAMEId").val(customerPoNum); 
	$("#subtotalGeneralId").val(formatCurrency(subTotal));
	totalGeneral = Number(subTotal) + Number(taxTotal) + Number(freight);
	$("#totalGeneralId").val(formatCurrency(totalGeneral));
	$("#generalID").val(formatCurrency(taxTotal));
	if(taxRate !== '' && taxRate !== null){
		$("#taxGeneralId").val(taxRate); 
		$("#Tax_ID").text(taxRate);
	}else{
		var tax2 = taxTerritory.split("%");
		$("#taxGeneralId").val(tax2[0]);
		$("#Tax_ID").text(tax2[0]);
	}
	$("#freightGeneralId").val(formatCurrency(freight));
	
	$("#specialInstructionID").val(spcInstr); 
	$("#wantedId").val(wanted);
	$("#wantedComboId").val(wantedOnOrBefore);
	$("#Subtotal_ID").text(formatCurrency(subTotal));
	$("#Freight_ID").text(formatCurrency(freight));
	$("#Percentage_ID").text();
	$("#Total_ID").text();

	if(poDate !== '' && poDate !== null){
		var poDay = new Date(poDate).getDate();
		var poMonth = new Date(poDate).getMonth();
		var poYear = new Date(poDate).getFullYear();
		if(poDay<10){poDay='0'+poDay;} 
		if(poMonth<10){poMonth='0'+poMonth;} 
		poDate = (Number(poMonth)+1) +"/"+ poDay +"/"+ poYear;
		$("#poDateId").val(poDate);
		$("#poDate_ID").empty();
		$("#poDate_ID").text(poDate);
	}else{
		/*var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; 
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd;} 
		if(mm<10){mm='0'+mm;} 
		today = mm+'/'+dd+'/'+yyyy;*/
		var grid = $("#release");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var vePOdate = grid.jqGrid('getCell', rowId, 'released');
		$("#poDateId").val(vePOdate);
		$("#poDate_ID").empty();
		$("#tagId").val($("#jobName_ID").text());
		$("#poDate_ID").text(vePOdate);
	}
	
	/**
	 * When we opeen line items first time onlt the data is loading properly.  if we open second time, the line item js file is already cached by DOM.
	 * So these values will not be reloaded correctly.
	 * 
	 * IT IS A SEVER BUG
	 * **/
	/****************** Data related to line items ***********************/
	$("#vendorLineNameId").val($("#vendor_ID").text()); $("#ourPoLineId").val($("#order_ID").text()); $("#subtotalLineId").val($("#Subtotal_ID").text()); 
	$("#totalLineId").val($("#Total_ID").text()); $("#taxLineId").val($("#Tax_ID").text());$("#freightLineId").val(formatCurrency(freight));
	$("#lineID").val($("#Percentage_ID").text());
	$("#poDateLineId").val($("#poDate_ID").text());
	/*********************************************************************/
	
	/********************* Data related to line items ********************/
	$("#vendorAckNameId").val($("#vendor_ID").text()); $("#ourPoAckId").val($("#order_ID").text()); $("#subtotalKnowledgeId").val($("#Subtotal_ID").text()); 
	$("#totalKnowledgeId").val($("#Total_ID").text()); $("#taxKnowledgeId").val($("#Tax_ID").text()); $("#freightKnowledgeId").val(formatCurrency(freight));
	$("#KnowledgeID").val($("#Percentage_ID").text());
	$("#poDateAckId").val($("#poDate_ID").text());
	/*********************************************************************/
	

}



$(function() { var cache = {}; var lastXhr='';
$( "#orderId" ).autocomplete({ minLength: 1,timeout :1000, select: function( event, ui ) { var id = ui.item.id; $("#orderhiddenId").val(id); var name = ui.item.value;$("#orderId").val(name);},
source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	}
lastXhr = $.getJSON( "jobtabs3/userName", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

 $(function() { var cache = {}; var lastXhr=''; $("#manfactureId" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) {   },
source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
lastXhr = $.getJSON( "jobtabs3/manufactureList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); }); 
 
 $(function() { var cache = {}; var lastXhr=''; $( "#locationbillToCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationbillToState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
 
 $(function() { var cache = {}; var lastXhr=''; $( "#locationShipToCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationShipToState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
 
 function frieghtCost(){
	 var aEstimatedCost=$("#freightGeneralId").val();
	 var aEstimatedCst=aEstimatedCost.replace(/[^0-9\.]+/g,"");
	 aEstimatedCst = Number(aEstimatedCst);
	 
	  
	 $("#freightGeneralId").val(aEstimatedCst);
	 $("#freightLineId").val(aEstimatedCst);
	 
	 return true;
 }
 
 function frieghtFormat(){
	 	var aEstimatedCost=$("#freightGeneralId").val();
 		var aSubTotal= $('#subtotalGeneralId').val();
 		var subNumber = aSubTotal.replace(/[^0-9\.]+/g,"");
 		var taxValue = $('#generalID').val();
 		var taxNumber = taxValue.replace(/[^0-9\.]+/g,"");
 		var totalGeneral  = 0;
 		totalGeneral  = totalGeneral  + Number(aEstimatedCost) + Number(taxNumber) + Number(subNumber);
 		$('#totalGeneralId').val(formatCurrency(totalGeneral ));
 		$("#freightGeneralId").val(formatCurrency(aEstimatedCost));
 		$("#freightLineId").val(formatCurrency(aEstimatedCost));
 		$("#Freight_ID").text(formatCurrency(aEstimatedCost));
		//$("#Total_ID").text(formatCurrency(aTotal));
		return true;
 }
 
 function savePORelease(){
	 
	/*Commented By :Velmurugan
	 * Date:18-6-2015
	 * As per eric Bug 476
	 *  if($('#POtransactionStatus_Dummy').val()!="-1" && $('#POtransactionStatus_Dummy').val()!="0" && $('#POtransactionStatus_Dummy').val()!="2")
	 *  
	 *  
		{*/
	 // Shipto div id
	 divflag = "#PO_Shipto";
	 
	 chkSplitCommissionPOValidation();
	 boolean = false;
	 console.log('Save / Update');	 
	 var newDialogDiv = jQuery(document.createElement('div'));
	 if(!$('#POGeneralForm').validationEngine('validate')) {
		return false;
	}
	 var IsSaveButtonClicked  = $('#ButtonClicked').val();
	 var aFreightVal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
	 var aFreightChk = aFreightVal.split(".");
	 var aFreightSpilt = aFreightChk[0];
	 if(aFreightSpilt.length > 5){
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Please provide 5 digit figures.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); document.getElementById('freightGeneralId').focus(); }}]}).dialog("open");
			return false;
	 }
	 
	 var aGenerelFormValues = $("#POGeneralForm").serialize();
	 var aJobNumber = $("#jobNumber_ID").text();
	 var qbId = $("#quickbookId").val();
	 var aManufacture = $("#rxManuId").val();
	 var aShipAddress = $("#rxAddressShipID").val(); 
	 var aBillAddress = $("#rxAddressBillID").val(); 
	 var vendorname = encodeURIComponent($("#vendor_ID").text());
	 var veFactoryId = $("#veFactoryID").val();
	 var tagVal = 	$("#tagId").val(); 
	 var emailTimeStamp = $("#emailTimeStamp").text();
	 var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
	 var asubTotal = $("#subtotalGeneralId").val().replace(/[^0-9\.]+/g,"");
	 var aTaxTotal = $("#generalID").val().replace(/[^0-9\.]+/g,"");
	 
	
	
	// alert("Commission Grids::"+gridDataToSend);
	 var rows = jQuery("#PoSplitCommissionGrid").getDataIDs();
	 deleteCommissionSplitJobID=new Array();
	 var validatesplit=validatePOSplitCommissionTotals();
	 if(validatesplit===true){
		 for(var a=0;a<rows.length;a++)
		 {
		    row=jQuery("#PoSplitCommissionGrid").getRowData(rows[a]);
		   var id="#canDeleteID_"+rows[a];
		   var canDelete=$(id).is(':checked');
		   if(canDelete){
			  var ecSplitJobId=row['ecSplitJobId'];
			  if(ecSplitJobId!=undefined && ecSplitJobId!=null && ecSplitJobId!="" && ecSplitJobId!=0){
			 		deleteCommissionSplitJobID.push(ecSplitJobId);
			 	}
			 $('#PoSplitCommissionGrid').jqGrid('delRowData',rows[a]);
		  }
		 }
	 }
	 
	 var gridRows = $('#PoSplitCommissionGrid').getRowData();
	 var gridDataToSend = JSON.stringify(gridRows);
		
	 /** *********  IT IS IMPORTANT CODE  ************
	  var aRequestObj = {
			 JOB_NUMBER: "",
			 Manu:"",
			 asdfdas:""
	 };
	 aRequestObj = JSON.stringify(aRequestObj);
	 ************************************************** */
		 //
	var taxGeneralName= $("#taxGeneralId").val();
		 
		 var poTransactionStats = $("#POtransactionStatus").val();
		 if(validatesplit===true){
		$.ajax({
	        url: './getUserDefaults',
	        type: 'POST',       
	        success: function (datas) {
	        	//alert('WareHouseID:'+data.warehouseID);
	        	if(datas.warehouseID > 0){
	        	var aPOGenerelValues = aGenerelFormValues+"&jobNumber="+aJobNumber+"&manufaturer="+aManufacture+
	 	 			"&freightGeneralName="+afreightTotal+"&subtotalGeneralName="+asubTotal+"&vendorName=" +
	 	 			vendorname+"&toBillAddress="+aBillAddress+"&toShipAddress="+aShipAddress+"&TaxTotal="+
	 	 			aTaxTotal+"&vefactoryID="+veFactoryId+"&emailTimeStamp="+emailTimeStamp+
	 	 			"&isSaveButtonClicked="+IsSaveButtonClicked+"&qbId="+qbId+"&transactionStatus="+poTransactionStats+
	 	 			"&prWarehouseID="+ datas.warehouseID+"&customerID="+$('#JobCustomerId').val()+"&joMasterID="+$("#joMasterHiddenID").val()+
	 	 			"&rxShiptoid="+ $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val()+"&rxShiptomodevalue="+ $(divflag).contents().find("#shiptomoderhiddenid").val();
	 	 		
	 	 
	        	console.log("Transaction status: "+aPOGenerelValues);
	        	//alert(aPOGenerelValueaa);
	        	$.ajax({
	 			url: './jobtabs3/addPOReleaseNew?'+aPOGenerelValues,
	 			type : 'POST',
	 			data: {"taxGeneralName":taxGeneralName,"tagName":tagVal,"commissionSplitGridData":gridDataToSend,'commissionSplitdelData':deleteCommissionSplitJobID},
	 			success: function (data){
	 				aVepoID = data.vePoid;
	 				$("#vePO_ID").text(aVepoID);
	 				$("#vePOID").val(aVepoID);
	 				createtpusage('job-Release Tab','PO Save','Info','Job,Release Tab,Saving PO,JobNumber:'+aJobNumber+',PO ID:'+aVepoID);
	 				var errorText = "Purchase Order General Tab details are Saved.";
	 				jQuery(newDialogDiv).attr("id","msgDlg");
	 				jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	 				if($('#ButtonClicked').is(':checked')){
	 					$('#showMessage').html("Saved");
	 					var new_po_generalform_values=poGeneralTabFormValidation();
						po_General_tab_form =  JSON.stringify(new_po_generalform_values);
	 					setTimeout(function(){
	 						$('#showMessage').html("");
	 						},3000);
	 					$('#ButtonClicked').prop('checked', false);
	 					$("#POtransactionStatus_Dummy").val($("#POtransactionStatus").val());
	 				}else{
	 					jQuery("#porelease").dialog("close");$("#release").trigger("reloadGrid"); 
	 					/*jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	 					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");jQuery("#porelease").dialog("close");$("#release").trigger("reloadGrid"); }}]}).dialog("open");*/
	 				return true;
	 					}
	 				
	 				loadPOSplitCommissionList($("#joMasterHiddenID").val(),$("#joReleaseid").val());
	 				 setTimeout(function(){
	 					var new_po_generalform_values=poGeneralTabFormValidation();
	 				    po_General_tab_form  =  JSON.stringify(new_po_generalform_values);
	 				   POGeneralTabformChanges();
	 					},1000);
	 				},
	 				error: function( xhr, status, errorThrown ) {
					        console.log( "Error: " + errorThrown );
					        console.log( "Status: " + status );
					        console.dir( xhr );
					    },
					complete: function( xhr, status ) {

					    }
	        	});
	        	}else{
	        		var errorDialogText = '';
	        		var wareHouseDialogDiv = jQuery(document.createElement('div'));
	        		errorDialogText = "Please set Default 'Warehouse' and 'Division' in MyProfile";
	        			jQuery(wareHouseDialogDiv).html('<span><b style="color:red;">'+errorDialogText+'</b></span>');
	        			jQuery(wareHouseDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
	        			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	        			return false;
	        	}
	    }
		});
		 return true;
		 }
    /*Commented By :Velmurugan
	 * Date:18-6-2015
	 * As per eric Bug 476
	 * }
	else
		{
		var StatusText="";
		if($('#POtransactionStatus').val()=="-1")
			{
			StatusText ="Void";
			}
		else if($('#POtransactionStatus').val()=="0")
			{
			StatusText ="Hold";
			}
		else if(($('#POtransactionStatus').val()=="2"))
			{
			StatusText ="Closed";
			}
		
		var errorDialogText = '';
		var wareHouseDialogDiv = jQuery(document.createElement('div'));
		errorDialogText = "Purchase order status is "+StatusText+" you can't edit.";
			jQuery(wareHouseDialogDiv).html('<span><b style="color:red;">'+errorDialogText+'</b></span>');
			jQuery(wareHouseDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
	}	*/
	
 }
 
 function usShiptoAddress(){
/*	 if (shipdateMode =1){
	 $("#shipTo2").show();
	 $("#shipTo").hide();
	 $("#shipTo1").hide();
	 }else {
		 $("#shipTo1").show();
		 $("#shipTo").hide();
		 $("#shipTo2").hide();
	 }*/
	 
	 $("#shipTo1").hide();
	 $("#shipTo2").show();
	 $("#shipTo").hide();
	 
	 $("#shiptoradio1").attr("Checked","Checked");
	 $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').addClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usShipUSID = 0;
	 var updaetKey = 'shipToUSID';
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	// loadShipToAddress("0");
	 
	 loadShipToAddress(usShipUSID,0,$("#jobCustomerName_ID").text())
	 
	 if((typeof (vePOID) == 'undefined') || vePOID ==null)
		 vePOID =0;
	 
	updateBillToAndShipToAddressSetting(vePOID, usShipUSID, updaetKey);
	 return true;
	 }
 
 function customerShiptoAddress(){
	
	 
	/* $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();*/
	 
	 	$("#shipTo1").hide();
		$("#shipTo2").show();
		$("#shipTo").hide();
		
		//alert("hi");
	 
	 
	 
	 $("#shiptoradio2").attr("Checked","Checked");
	 $('#shiptolabel2').addClass("ui-state-active");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 $('#shiptolabel2').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 var vePOID = $("#vePO_ID").text();
	 var usShipCustID = 1;
	 var updaetKey = 'shipToCustomer';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var rxMasterId = $("#rxCustomer_ID").text();
	 var rxAddressId =0;
	 
	 loadShipToAddress(usShipCustID,0,$("#jobCustomerName_ID").text())
	 

	 updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);

	 /*
		operationVar = "ship";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId ,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(locationName); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=true;
				document.getElementById('locationShipToAddressID1').disabled=true;
				document.getElementById('locationShipToAddressID2').disabled=true;
				document.getElementById('locationShipToCity').disabled=true;
				document.getElementById('locationShipToState').disabled=true;
				document.getElementById('locationShipToZipID').disabled=true;
     			}
		});
		 updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);*/
		 return true;
 }
 function jobsiteShiptoAddress(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	 $("#shiptoradio3").attr("Checked","Checked");
	 $('#shiptolabel3').addClass("ui-state-active");
	 $('#shiptolabel3').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usShipCustID = $("#joMaster_ID").text();
	 var updaetKey = 'shipTo';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite_select.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 $("#locationShipToAddressID").val(CustomName); $("#locationShipToAddressID1").val(jobLocation1); $("#locationShipToAddressID2").val(jobLocation2); $("#locationShipToCity").val(jobCity);
	 $("#locationShipToState").val(jobState); $("#locationShipToZipID").val(jobZip);
		document.getElementById('locationShipToAddressID').disabled=true;
		document.getElementById('locationShipToAddressID1').disabled=true;
		document.getElementById('locationShipToAddressID2').disabled=true;
		document.getElementById('locationShipToCity').disabled=true;
		document.getElementById('locationShipToState').disabled=true;
		document.getElementById('locationShipToZipID').disabled=true;
		updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);
		loaddropshiptaxterritory(0,'JobSite');
		$("#customerShipToID").val($("#joMaster_ID").text());
		return true;
 }
 function otherShiptoAddressPO(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	
	 $("#locationShipToAddressID").val("");
	 $("#locationShipToAddressID1").val("");
	 $("#locationShipToAddressID2").val("");
	 $("#locationShipToCity").val("");
	 $("#locationShipToState").val("");
	 $("#locationShipToZipID").val("");
	 
	 $("#shiptoradio4").attr("Checked","Checked");
	 $('#shiptolabel4').addClass("ui-state-active");
	 $('#shiptolabel4').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var bidderGrid = $("#release");
	 var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	 var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'shipToAddressID');
	 var usShipCustID = 3;
	 var updaetKey = 'shipToOther';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px" });
	 document.getElementById('locationShipToAddressID').disabled=false;
	 document.getElementById('locationShipToAddressID1').disabled=false;
		document.getElementById('locationShipToAddressID2').disabled=false;
		document.getElementById('locationShipToCity').disabled=false;
		document.getElementById('locationShipToState').disabled=false;
		document.getElementById('locationShipToZipID').disabled=false;
		
		/*updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);*/
		$.ajax({
			   url : "./jobtabs3/updateBillToAndShipToSetting",
			   type : "GET",
			   async:false,
			   data : { 'vePOID': vePOID, 'isAddressID': usShipCustID, 'updaetKey': updaetKey,'rxCustomerID':$("#rxCustomer_ID").text()},
			   success : function (msg) {
				   
				
			   },
			   error : function (msg) {}
			});
		
		$.ajax({
					url : "./salesOrderController/getPreLoadData",
					type : "POST",
					data : "&vePOID=" + vePOID + "&rxMasterID=0&joMasterID="+$("#joMaster_ID").text(),
					// data :
					// "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
					success : function(data) {
							if (typeof data.vepo.rxShipToOtherAddressID != 'undefined'
								&& data.vepo.rxShipToOtherAddressID !== ''
								&& data.vepo.rxShipToOtherAddressID !== null){
							if(data.vepo.shipToMode===3){
						console.log('Jenith Your Data: '+ data.vepo.rxShipToOtherAddressID);
						var rxAddressIds = data.vepo.rxShipToOtherAddressID;
						operationVar = "shipToOther";
						$.ajax({
									url : "./jobtabs3/getBilltoAddress",
									type : "GET",
									data : {
										"customerID" : data.vepo.rxShipToOtherAddressID,
										"oper" : updaetKey
									},
									success : function(data){
										var rxAddressId = data.rxAddressId;
										var locationAddress1 = data.address1;
										var locationAddress2 = data.address2;
										var locationCity = data.city;
										var locationState = data.state;
										var locationZip = data.zip;
										var name = data.name;
										console.log(rxAddressId + " :: "
												+ locationAddress1 + " :: "
												+ locationAddress2 + " :: "
												+ locationCity + " :: "
												+ locationState + " :: "
												+ locationZip + " :: " + name);
										$("#customerShipToID").val(rxAddressId);
										$("#rxAddressShipID").val(rxAddressId); 
										$("#locationShipToAddressID").val(data.name); 
										$("#locationShipToAddressID1").val(locationAddress1); 
										$("#locationShipToAddressID2").val(locationAddress2); 
										$("#locationShipToCity").val(locationCity);
										$("#locationShipToState").val(locationState); 
										$("#locationShipToZipID").val(locationZip);
									}
								});
							}else{
								$("#rxAddressShipID").val(""); 
								$("#locationShipToAddressID").val(""); 
								$("#locationShipToAddressID1").val(""); 
								$("#locationShipToAddressID2").val(""); 
								$("#locationShipToCity").val("");
								$("#locationShipToState").val(""); 
								$("#locationShipToZipID").val("");
							}
							}else{
								$("#customerShipToID").val("");
								$("#rxAddressShipID").val(""); 
								$("#locationShipToAddressID").val(""); 
								$("#locationShipToAddressID1").val(""); 
								$("#locationShipToAddressID2").val(""); 
								$("#locationShipToCity").val("");
								$("#locationShipToState").val(""); 
								$("#locationShipToZipID").val("");
							}
					}
				});
	
	 
	/* $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : updaetKey},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(data.name); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=false;
				document.getElementById('locationShipToAddressID1').disabled=false;
				document.getElementById('locationShipToAddressID2').disabled=false;
				document.getElementById('locationShipToCity').disabled=false;
				document.getElementById('locationShipToState').disabled=false;
				document.getElementById('locationShipToZipID').disabled=false;
			}
		});*/
		
		return true;
 }
 
 function usShiptoSelectAddress(){
	 
/*	 alert(shipdateMode);
	 
	 if (shipdateMode =1){
		 $("#shipTo2").show();
		 $("#shipTo").hide();
		 $("#shipTo1").hide();
		 }else {
			 $("#shipTo1").show();
			 $("#shipTo").hide();
			 $("#shipTo2").hide();
		 }*/
	 
	 $("#shipTo2").show();
	 $("#shipTo").hide();
	 $("#shipTo1").hide();
	 
	 $("#shiptoradio1").attr("Checked","Checked");
	 $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').addClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	/* $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");*/
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 //loadShipToAddress("0");
	 return true;
 }
 function customerShiptoSelectAddress(){
	 
	// alert("hiiii"+ $("#rxAddressShipID").val()+"==");
	 
	 
	 $("#shipTo1").hide();
		$("#shipTo2").show();
		$("#shipTo").hide();
	 $("#shiptoradio2").attr("Checked","Checked");
	 $('#shiptolabel2').addClass("ui-state-active");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 $('#shiptolabel2').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 /*$('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");*/
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $("#shipTo1").hide();
		$("#shipTo").show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "ship";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(locationName); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=true;
				document.getElementById('locationShipToAddressID1').disabled=true;
				document.getElementById('locationShipToAddressID2').disabled=true;
				document.getElementById('locationShipToCity').disabled=true;
				document.getElementById('locationShipToState').disabled=true;
				document.getElementById('locationShipToZipID').disabled=true;
				}
		});
		 return true;
 }
 function jobsiteShiptoSelectAddress(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	 $("#shiptoradio3").attr("Checked","Checked");
	 $('#shiptolabel3').addClass("ui-state-active");
	 $('#shiptolabel3').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");/*
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");*/
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite_select.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 $("#locationShipToAddressID").val(CustomName); $("#locationShipToAddressID1").val(jobLocation1); $("#locationShipToAddressID2").val(jobLocation2); $("#locationShipToCity").val(jobCity);
		$("#locationShipToState").val(jobState); $("#locationShipToZipID").val(jobZip);
		document.getElementById('locationShipToAddressID').disabled=true;
		document.getElementById('locationShipToAddressID1').disabled=true;
		document.getElementById('locationShipToAddressID2').disabled=true;
		document.getElementById('locationShipToCity').disabled=true;
		document.getElementById('locationShipToState').disabled=true;
		document.getElementById('locationShipToZipID').disabled=true;
		
		//alert($("#joMaster_ID").text());
		
		$("#customerShipToID").val($("#joMaster_ID").text());
		
		
		return true;
 }
 function otherShiptoSelectAddress(){
	 $("#shipTo1").hide();
		$("#shipTo2").hide();
		$("#shipTo").show();
		
		//alert("wew");
		
	 $("#shiptoradio4").attr("Checked","Checked");
	 $('#shiptolabel4').addClass("ui-state-active");
	 $('#shiptolabel4').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	/* $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal")*/;
	 var updaetKey = 'shipToOther';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px" });
	 	var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'shipToAddressID');
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : updaetKey},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#customerShipToID").val(rxAddressId); 
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(data.name); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=false;
				document.getElementById('locationShipToAddressID1').disabled=false;
				document.getElementById('locationShipToAddressID2').disabled=false;
				document.getElementById('locationShipToCity').disabled=false;
				document.getElementById('locationShipToState').disabled=false;
				document.getElementById('locationShipToZipID').disabled=false;
			}
		});
		return true;
 }
 
 function shipToAddress(){
	var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "ship";
		$.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				//$("#locationShipToAddressID").val(locationName); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				 $("#locationShipToAddressID").val(CustomName); $("#locationShipToAddressID1").val(jobLocation1); $("#locationShipToAddressID2").val(jobLocation2); $("#locationShipToCity").val(jobCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=true;
				document.getElementById('locationShipToAddressID1').disabled=true;
				document.getElementById('locationShipToAddressID2').disabled=true;
				document.getElementById('locationShipToCity').disabled=true;
				document.getElementById('locationShipToState').disabled=true;
				document.getElementById('locationShipToZipID').disabled=true;
				}
		});
 }

 function usBilltoAddress(){
	 $('#billtolabel1').css("font-weight","bold");
	 $('#billtolabel2').css("font-weight","normal");
	 $('#billtolabel3').css("font-weight","normal");
	 $("#billtoradio1").attr("Checked","Checked");
	 
	 $('#billtolabel1').addClass("ui-state-active");
	 $('#billtolabel2').removeClass("ui-state-active");
	 $('#billtolabel3').removeClass("ui-state-active");
	 
	 var vePOID = $("#vePO_ID").text();
	 var usBillUSID = 0;
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var description= $("#billtodescription").val();
	 var address1= $("#billtoaddress1").val();
	 var address2= $("#billtoaddress2").val();
	 var city= $("#billtocity").val();
	 var state= $("#billtostate").val();
	 var zipcode= $("#billtozip").val();
	 
	 $("#locationbillToAddressID").val(description);
	 $("#locationbillToAddressID1").val(address1); 
	 $("#locationbillToAddressID2").val(address2); 
	 $("#locationbillToCity").val(city);
	 $("#locationbillToState").val(state); 
	 $("#locationbillToZipID").val(zipcode);
	 $("#billToMode").val('0');
		document.getElementById('locationbillToAddressID').disabled=true;
		document.getElementById('locationbillToAddressID1').disabled=true;
		document.getElementById('locationbillToAddressID2').disabled=true;
		document.getElementById('locationbillToCity').disabled=true;
		document.getElementById('locationbillToState').disabled=true;
		document.getElementById('locationbillToZipID').disabled=true;
		updateBillToAndShipToAddressSetting(vePOID, usBillUSID, updaetKey);
		 return true;
 }
 function customerBilltoAddress(){
	 
	 $('#billtolabel2').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel3').css("font-weight","normal");
	 $("#billtoradio2").attr("Checked","Checked");
	 
	 $('#billtolabel2').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel3').removeClass("ui-state-active");
	    
	 var vePOID = $("#vePO_ID").text();
	 var usBillCustID = 1;
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
 	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "bill";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressBillID").val(rxAddressId); 
				$("#locationbillToAddressID").val(locationName); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
				$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
				document.getElementById('locationbillToAddressID').disabled=true;
				document.getElementById('locationbillToAddressID1').disabled=true;
				document.getElementById('locationbillToAddressID2').disabled=true;
				document.getElementById('locationbillToCity').disabled=true;
				document.getElementById('locationbillToState').disabled=true;
				document.getElementById('locationbillToZipID').disabled=true;
			}
		 });
		 $("#billToMode").val('1');
		updateBillToAndShipToAddressSetting(vePOID, usBillCustID, updaetKey);
		 return true;
 }
function otherBilltoAddress(){
	 $('#billtolabel3').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel2').css("font-weight","normal");
	 $("#billtoradio3").attr("Checked","Checked");
	 $('#billtolabel3').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel2').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usBillOtherID = 2;
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px"});
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px"});
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px"});
	 	var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'billToAddressID');
	 $.ajax({
		url: "./jobtabs3/getBilltoAddress",
		type: "GET",
		data : {"customerID" : rxMasterId,"oper" : updaetKey },
		success: function(data) {
			var locationName = $("#jobCustomerName_ID").text();
			var rxAddressId = data.rxAddressId;
			var locationAddress1 = data.address1;
			var locationAddress2 = data.address2;
			var locationCity = data.city;
			var locationState = data.state;
			var locationZip = data.zip;
			$("#rxAddressBillID").val(rxAddressId); 
			$("#locationbillToAddressID").val(data.name); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
			$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
			document.getElementById('locationbillToAddressID').disabled=false;
			document.getElementById('locationbillToAddressID1').disabled=false;
			document.getElementById('locationbillToAddressID2').disabled=false;
			document.getElementById('locationbillToCity').disabled=false;
			document.getElementById('locationbillToState').disabled=false;
			document.getElementById('locationbillToZipID').disabled=false;
		}
	 });
	 $("#billToMode").val('3');
	updateBillToAndShipToAddressSetting(vePOID, usBillOtherID, updaetKey);
	return true;
}	


function usBilltoSelectAddress(){
	$('#billtolabel1').css("font-weight","bold");
	$('#billtolabel2').css("font-weight","normal");
	$('#billtolabel3').css("font-weight","normal");
	$("#billtoradio1").attr("Checked","Checked");
	
	$('#billtolabel1').addClass("ui-state-active");
	$('#billtolabel2').removeClass("ui-state-active");
	$('#billtolabel3').removeClass("ui-state-active");
	var description= $("#billtodescription").val();
	var address1= $("#billtoaddress1").val();
	var address2= $("#billtoaddress2").val();
	var city= $("#billtocity").val();
	var state= $("#billtostate").val();
	var zipcode= $("#billtozip").val();
	$("#locationbillToAddressID").val(description);
	$("#locationbillToAddressID1").val(address1); 
	$("#locationbillToAddressID2").val(address2); 
	$("#locationbillToCity").val(city);
	$("#locationbillToState").val(state); 
	$("#locationbillToZipID").val(zipcode);
	document.getElementById('locationbillToAddressID').disabled=true;
	document.getElementById('locationbillToAddressID1').disabled=true;
	document.getElementById('locationbillToAddressID2').disabled=true;
	document.getElementById('locationbillToCity').disabled=true;
	document.getElementById('locationbillToState').disabled=true;
	document.getElementById('locationbillToZipID').disabled=true;
	return true;
}
function customerBilltoSelectAddress(){
	 $('#billtolabel2').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel3').css("font-weight","normal");
	 $("#billtoradio2").attr("Checked","Checked");
	 $('#billtolabel2').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel3').removeClass("ui-state-active");
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "bill";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressBillID").val(rxAddressId); 
				$("#locationbillToAddressID").val(locationName); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
				$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
				document.getElementById('locationbillToAddressID').disabled=true;
				document.getElementById('locationbillToAddressID1').disabled=true;
				document.getElementById('locationbillToAddressID2').disabled=true;
				document.getElementById('locationbillToCity').disabled=true;
				document.getElementById('locationbillToState').disabled=true;
				document.getElementById('locationbillToZipID').disabled=true;
			}
		 });
		 return true;
}
function otherBilltoSelectAddress(){
	 $('#billtolabel3').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel2').css("font-weight","normal");
	 $("#billtoradio3").attr("Checked","Checked");
	 $('#billtolabel3').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel2').removeClass("ui-state-active");
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px"});
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px"});
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px"});
	 	var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'billToAddressID');
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : updaetKey },
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressBillID").val(rxAddressId); 
				$("#locationbillToAddressID").val(data.name); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
				$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
				document.getElementById('locationbillToAddressID').disabled=false;
				document.getElementById('locationbillToAddressID1').disabled=false;
				document.getElementById('locationbillToAddressID2').disabled=false;
				document.getElementById('locationbillToCity').disabled=false;
				document.getElementById('locationbillToState').disabled=false;
				document.getElementById('locationbillToZipID').disabled=false;
			}
		 });
		return true;
}	

 function billToAddress(){
	/* if($('#usBillId').is(':checked')){
		 $("#locationbillToAddressID").val("Bache Sales Agency"); $("#locationbillToAddressID1").val("P.O. Box 921369"); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("Norcross");
			$("#locationbillToState").val("GA"); $("#locationbillToZipID").val("30010");
			document.getElementById('locationbillToAddressID').disabled=true;
			document.getElementById('locationbillToAddressID1').disabled=true;
			document.getElementById('locationbillToAddressID2').disabled=true;
			document.getElementById('locationbillToCity').disabled=true;
			document.getElementById('locationbillToState').disabled=true;
			document.getElementById('locationbillToZipID').disabled=true;
	 }else if($('#customerBillId').is(':checked')){*/
		var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "bill";
		 $.ajax({
				url: "./jobtabs3/getBilltoAddress",
				type: "GET",
				data : {"customerID" : rxMasterId,"oper" : operationVar},
				success: function(data) {
					var locationName = $("#jobCustomerName_ID").text();
					var rxAddressId = data.rxAddressId;
					var locationAddress1 = data.address1;
					var locationAddress2 = data.address2;
					var locationCity = data.city;
					var locationState = data.state;
					var locationZip = data.zip;
					$("#rxAddressBillID").val(rxAddressId); 
					$("#locationbillToAddressID").val(""); $("#locationbillToAddressID1").val(""); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("");
					/*$("#locationbillToAddressID").val("Bache Sales Agency"); $("#locationbillToAddressID1").val("P.O. Box 921369"); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("Norcross");*/
					//$("#locationbillToAddressID").val(locationName); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
					$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
					document.getElementById('locationbillToAddressID').disabled=true;
					document.getElementById('locationbillToAddressID1').disabled=true;
					document.getElementById('locationbillToAddressID2').disabled=true;
					document.getElementById('locationbillToCity').disabled=true;
					document.getElementById('locationbillToState').disabled=true;
					document.getElementById('locationbillToZipID').disabled=true;
					}
				});
	/* }else if($('#otherBillId').is(':checked')){ 
		 $("#locationbillToAddressID").val(""); $("#locationbillToAddressID1").val(""); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("");
			$("#locationbillToState").val(""); $("#locationbillToZipID").val("");
			document.getElementById('locationbillToAddressID').disabled=false;
			document.getElementById('locationbillToAddressID1').disabled=false;
			document.getElementById('locationbillToAddressID2').disabled=false;
			document.getElementById('locationbillToCity').disabled=false;
			document.getElementById('locationbillToState').disabled=false;
			document.getElementById('locationbillToZipID').disabled=false;
	 }*/
 }

 function sendPOGENEmail(){
		var bidderGrid = $("#release");
		var aQuotePDF = "purchaseGen";
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var errorText = '';
		var cusotmerPONumber = $("#ourPoId").val();
		var newDialogDiv = jQuery(document.createElement('div'));
		if(bidderGridRowId === null){
			errorText = "Please click one of the Order to Email Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		var aContactID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxVendorContactID');
		if(aContactID === ''){
			errorText = "Please Add Contact for Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}else{
			$.ajax({ 
				url: "./vendorscontroller/GetContactDetails",
				mType: "GET",
				data : { 'rxContactID' : aContactID},
				success: function(data){
					var aFirstname = data.firstName;
					var aLastname = data.lastName;
					var aEmail = data.email;
					var arxContact = aFirstname + ' '+aLastname;
					if(aEmail ==''){
						errorText = "Are you sure you want to send this PO General to"+ arxContact +"?";
					}else{
						errorText = "Are you sure you want to send this PO General to"+ arxContact +"("+ aEmail +")?";
					}
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
					buttons:{
						"Send": function(){
							$('#loadingPOGenDiv').css({"visibility": "visible"});
							openPOGenPDF(aQuotePDF);
							sentMailPOGenFunction(aContactID,aEmail, cusotmerPONumber);
							jQuery(this).dialog("close");
						},
						Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
				}
			});
		}
		return true;
	}

	function sentMailPOGenFunction(aContactID,aEmail, cusotmerPONumber){
		
		if(aEmail === ''){
			var errorText = "Please Provide a Mail ID.";
			jQuery(newDialogDiv).html('<form id="mailToAddress"><table><tr><td><span><b style="color:red;">'+errorText+'</b></span></td></tr>'+
					'<tr><td style="height: 5px;"></td></tr>' +
			'<tr><td><label>Mail ID: </label><input type="text" id="mailToAddress_ID" name="mailToAddress_name" class="validate[custom[email]]" style="width: 250px;" /></td></tr></table></form><hr>');
			jQuery(newDialogDiv).dialog({modal: true, width:430, height:180, title:"Message", 
				buttons:{
					"Submit & Send": function(){
						if(!$('#mailToAddress').validationEngine('validate')) {
							return false;
						}
						var aEmailAddress = $("#mailToAddress_ID").val();
						saveMailAddress(aEmailAddress, aContactID);
						$('#loadingPOGenDiv').css({"visibility": "visible"});
						$.ajax({ 
							url: "./sendMailServer/sendPOGenMail",
							mType: "POST",
							data : {'contactID' : aContactID, 'POPDF' : 'poGeneral' , 'PONumber' : cusotmerPONumber},
							success: function(data){
								$('#loadingPOGenDiv').css({"visibility": "hidden"});
								var errorText = "Mail Sent Successfully.";
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}] }).dialog("open");
							}
						});
						jQuery(this).dialog("close");
						return true;
					},
					Cancel: function ()	{jQuery(this).dialog("close");}
				}}).dialog("open");
		}else{
			$.ajax({ 
				url: "./sendMailServer/sendPOGenMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'POPDF' : 'poGeneral' , 'PONumber' : cusotmerPONumber},
				success: function(data){
					$('#loadingPOGenDiv').css({"visibility": "hidden"});
					var errorText = "Mail Sent Successfully.";
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}] }).dialog("open");
				}
			});
		}
	}

	function saveMailAddress(aEmailAddress, aContactID){
		$.ajax({ 
			url: "./jobtabs2/updateEmailAddress",
			mType: "GET",
			data : { 'email' : aEmailAddress, 'contactID' : aContactID },
			success: function(data){
			}
		});
	}
	function openPOGenPDF(aQuotePDF){
		var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
		if(aQuotePDF === 'purchaseGen'){
			$.ajax({
				   type : "GET",
				   url : "./purchasePDFController/viewPurchasePdfForm",
				   data : { 'vePOID' : vePOID, 'puchaseOrder' : aQuotePDF },
				   documenttype: "application\pdf",
			        async: false,
			        cache: false,
			   success : function (msg) {
			   },
			   error : function (msg) {}
			});
			 return true;
		}
	}
	
	function viewPOGENPDF(){
		createtpusage('job-Release Tab','PO-Cofirm Button','Info','Job-Release Tab,Confirming PO-Status,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
		var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
		var aQuotePDF = "PO";
		window.open("./purchasePDFController/viewPurchasePdfForm?vePOID="+vePOID+"&puchaseOrder="+aQuotePDF);
		return true;
	}
	
	function updateBillToAndShipToAddressSetting(vePOID, usAddressID, updateKey){
		$.ajax({
		   url : "./jobtabs3/updateBillToAndShipToSetting",
		   type : "GET",
		   data : { 'vePOID': vePOID, 'isAddressID': usAddressID, 'updaetKey': updateKey,'rxCustomerID':$("#rxCustomer_ID").text()},
		   success : function (msg) {
			   
			
		   },
		   error : function (msg) {}
		});
		return true;
	}
	
	function updateShipToAddressValue(theVePOID, shiptoindex, operation,shiptoMode){
		//alert("hi");
		$.ajax({
			   url : "./jobtabs3/updateShipToValue",
			   type : "GET",
			   data : { 'vePOID': theVePOID, 'shiptoindex': shiptoindex,'operation':operation,'shiptoMode':shiptoMode},
			   success : function (data) {
				   
			//	   alert(data.shipToMode+"----"+data.shipTo+"---"+data.rxVendorId);
				   
				   loadShipToAddress(data.shipToMode,data.shipTo,"")
			   },
			   error : function (data) {}
			});
			return true;
	}
	
	function createQuickBooks(){
		var jobNumber = $('.jobHeader_JobNumber').val();
		var jobName = $('#jobHeader_JobName_id').val();
		var customerName = $('#jobHeader_JobCustomerName_id').val();
		var manfactureName = $('#manfactureName').val();
		var vePOID = $("#vePOID").val();
		var items = [];
		var qbinputs = [];
		var inputs = {};
		var iteminputs = {};
		
		$.ajax({
			 url : "./jobtabs3/getVepodetailList",
			 type : "GET",
			 async: false,
			 data : { 'vePOID': vePOID},
			 success : function (data) {
				 $.each(data, function( index, value ) {
					 $.ajax({
						   url : "./jobtabs3/getItemCode",
						   type : "GET",
						   async: false,
						   data : { 'prMasterId': value.prMasterId},
						   success : function (data) {
							   iteminputs["ItemName"] = data;
						   },
						   error : function (msg) { alert(msg); }
					 });
					 iteminputs["Description"] = value.description;
					 iteminputs["Quantity"] = value.quantityOrdered+"";
					 iteminputs["rate"] = value.unitCost+"";
					 items.push(iteminputs);
				 });
				 
			 },
			 
			 error : function (msg) { alert(msg); }
		});
		var details = {};
		details["VendorName"] = manfactureName;
		details["customername"] = customerName;
		inputs["details"] = details;
		inputs["item"] = items;
		qbinputs.push(inputs);

		$.blockUI({ css: { 
				border: 'none', 
				padding: '15px', 
				backgroundColor: '#000', 
				'-webkit-border-radius': '10px', 
				'-moz-border-radius': '10px', 
				opacity: .5, 
				'z-index':'1010px',
				color: '#fff'
			} }); 

		  $.ajax({  
		        url: 'http://50.192.42.51/api/values' ,
		        data:  {'details':JSON.stringify(inputs)},  
		        type: "POST",  
		        crossDomain: true,
		        dataType:'application/json',
		        success: function(data) {
	        }, 
		        error: function(data) {
		        	var response = data.responseText;
		        	var obj =  $.parseJSON(response);
		        	if(obj.Data.ponumber !== null){
		        		setTimeout($.unblockUI, 200);
		        		$('#quickbookId').val(obj.Data.ponumber);
		        		savePo();
		        		$("#quickBookID").attr('disabled','disabled');
		        		$("#quickbookId").attr('disabled','disabled');
		        	} else{
		        		setTimeout($.unblockUI, 200);
		        		var errorText = obj.Data.status;
		        		var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error", 
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}] }).dialog("open");
		        	}
		        }
		    });  
		    return true;
	}
	
function loadPOSplitCommissionList(joMasterID,joReleaseID){
	createtpusage('Drop Ship-Release Tab','View Split-Commission','Info','Job,Release Tab,Drop Ship Split Commission ,JoReleaseId:'+$("#jobreleasehiddenId").val());
	$('#PoSplitCommissionGrid').jqGrid('GridUnload');
	$("#PoSplitCommission").empty();
	$("#PoSplitCommission").append("<table id='PoSplitCommissionGrid'></table><div id='PoSplitCommissionGridPager'></div>");
	$('#PoSplitCommissionGrid').jqGrid({
	datatype: 'JSON',
	mtype: 'POST',
	pager: jQuery('#PoSplitCommissionGridPager'),
	url:'./jobtabs3/jobCommissionListGrid',
	postData: {'JoMasterId':joMasterID,'JoReleaseId':joReleaseID,'tabpage':'JoRelease'},
	colNames:[ 'Id','Rep','', '% Allocated', 'Split Type','','<img src="./../resources/images/delete.png" style="vertical-align: middle;">'],
	colModel :[
				{name:'ecSplitJobId', index:'ecSplitJobId', align:'left',editable:true, width:25,hidden:true},
	           	{name:'rep', index:'rep', align:'left', width:48, editable:true,hidden:false, editoptions:{size:12,
					 dataInit: function (elem) {
						
							//alert("aSelectedRowId = "+aSelectedRowId+" || prMasteId = "+$("#"+aSelectedRowId+"_prMasterId").val());
				            $(elem).autocomplete({
				                source: 'jobtabs3/getEmployeewithNameList',
				                minLength: 1,
				                select: function (event, ui) {  ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  
				                var product = ui.item.label; 
				                $("#releaserepId").val(id);
				                var selectedRowId = $("#PoSplitCommissionGrid").jqGrid('getGridParam', 'selrow');
				                $("#"+selectedRowId+"_rxMasterId").val(id);
				                
				                $.ajax({
							        url: "./jobtabs3/getEmployeeCommissionDetail",
							        data: {id:id},
							        type: 'GET',
							        success: function(data){
							        	 var aSelectedRowId = $("#PoSplitCommissionGrid").jqGrid('getGridParam', 'selrow');
								        if(data!=null)
								        	/*$("#"+aSelectedRowId+"_allocated").val(data.jobCommissions);*/
								        	$("#"+aSelectedRowId+"_allocated").val("100");
							        	
							        }
							   }); 
				                
				                } }); $(elem).keypress(function (e) {
					                
				                    if (!e) e = window.event;
				                    if (e.keyCode == '13') {
				                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
				                        return false;
				                    }
				                });}	},editrules:{edithidden:false,required: true}},
				{name:'rxMasterId', index:'rxMasterId', align:'left',editable:true, width:32,hidden:true, editoptions:{size:6}},
				{name:'allocated', index:'allocated', align:'center',editable:true, width:32,hidden:false, editoptions:{size:6}, editrules:{custom:true,custom_func:check_posplitcommision}},
				//{name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:40,hidden:true},
				{name:'splittype', index:'splittype', align:'',editable:true, width:70,hidden:false,  editoptions:{size:19,
					 dataInit: function (elem) {
				            $(elem).autocomplete({
				                source: 'jobtabs3/getSplitTypewithNameList',
				                minLength: 1,
				                select: function (event, ui) { ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  
				                var product = ui.item.label;
				                $("#releasesplittypeId").val(id);
				                var selectedRowId = $("#PoSplitCommissionGrid").jqGrid('getGridParam', 'selrow');
				                $("#"+selectedRowId+"_ecSplitCodeID").val(id);
								//Ajax starting point
								/* Commented By Zenith
								 * $.ajax({
								        url: "./jobtabs3/getpercentagebasedonsplittype",
								        data: {id:id},
								        type: 'GET',
								        success: function(data){
									        if(data!=null)
									        	$("#allocated").val(data.defaultPct);
								        	
								        }
								   }); */
								//Ajax End Part
				               
				                } }); $(elem).keypress(function (e) {
					                
				                    if (!e) e = window.event;
				                    if (e.keyCode == '13') {
				                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
				                        return false;
				                    }
				                });}	}, editrules:{edithidden:false,required: false}},
				 {name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:10,hidden:true, editoptions:{size:6}},
				 {name:'canDelete', index:'canDelete', align:'center',  width:20, hidden:false, editable:false, formatter:canDeleteCheckboxFormatter,   editrules:{edithidden:true}}],
				 altRows:true,
				 altclass:'myAltRowClass',
				 cellsubmit: 'clientArray',
				 editurl: 'clientArray',
				 height:120,
				 imgpath:'themes/basic/images',
				 rowNum: 0,
				 sortname:'rep',
				 sortorder:"asc",
				 pgbuttons: false,
				 recordtext:'',
				 rowList:[],
				 pgtext: null,
				 rownumbers:false,
			 	 width:425,
			 	 //footerrow: true,
			    // userDataOnFooter : true,
			     viewrecords: false,
			     loadonce: false,
			     cellEdit: false,
			     cmTemplate: {sortable:false},
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
	gridComplete:function(){
		 checkPOCommissionPaidorNot(joMasterID,joReleaseID);
		 var gridRows = $('#PoSplitCommissionGrid').getRowData();
		 commission_PO_grid_form=JSON.stringify(gridRows);
		},
	loadComplete: function(data) {
		var allRowsInGrid = $('#PoSplitCommissionGrid').jqGrid('getRowData');
		var  count= $('#PoSplitCommissionGrid').getGridParam('reccount');
		var rowid=$("#jorowhiddenId").val();
		
		if(rowid!=null && rowid!=""){}
		
		var aVal = new Array(); 
		var sum = 0;
		$.each(allRowsInGrid, function(index, value) {
			aVal[index] = value.allocated;
			sum = Number(sum) + Number(aVal[index]); 
		});
		releaseCommissionSplitsGridsum=sum;
		
	},
	loadError : function (jqXHR, textStatus, errorThrown){	},
	onSelectRow:  function(id){
		SplitCommissionID=id;
		 var rowData = jQuery(this).getRowData(id); 
		 releaseselectedid=rowData["ecSplitJobId"];
		 releaseupdatecommsplitgrid=Number(releaseCommissionSplitsGridsum)-Number(rowData["allocated"]);
	 },
	onCellSelect : function (rowid,iCol, cellcontent, e) {
		 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
		 //console.log(e);
		
		},
	//editurl:"./jobtabs3/manipulateSplitCommission"
	}).navGrid("#PoSplitCommissionGridPager", {
		add : false,
		edit : false,
		del : false,
		search : false,
		refresh : false,
		pager : false,
		alertzIndex : 12345,
		alertcap : "Warning",
		alerttext : 'Please select Sales Rep'
	});
	$("#PoSplitCommissionGrid").jqGrid(
			'inlineNav',
			'#PoSplitCommissionGridPager',
			{
				edit : true,
				edittext : "Edit",
				add : true,
				addtext : "Add",
				cancel : true,
				canceltext :"Cancel",
				savetext : "Save",
				refresh : true,
				alertzIndex : 12345,
				addParams : {
					addRowParams : {
						keys : false,
						oneditfunc : function() {
							console.log("edited");
							$("#new_row_rep").focus();
							$("#del_PoSplitCommissionGrid").addClass('ui-state-disabled');
							$("#info_dialog").css("z-index", "12345");
							$(".jqmID1").css("z-index", "12345");
						},
						successfunc : function(response) {
							console.log("successfunc");
							console.log(response);
							return true;
						},
						aftersavefunc : function(id) {
							$("#info_dialog").css("z-index", "12345");
							console.log("aftersavefunc");
							var ids = $("#PoSplitCommissionGrid").jqGrid('getDataIDs');
							var cuinvrowid;
							if(ids.length==1){
								cuinvrowid = 0;
							}else{
								var idd = jQuery("#PoSplitCommissionGrid tr").length;
								for(var i=0;i<ids.length;i++){
									if(idd<ids[i]){
										idd=ids[i];
									}
								}
								cuinvrowid= idd;
							}
							if(SplitCommissionID=="new_row"){
							$("#" + SplitCommissionID).attr("id", Number(cuinvrowid)+1);
							var candoidrownum=Number(cuinvrowid)+1;
							$("#canDeleteID_new_row").attr("id", "canDeleteID_"+candoidrownum);
							}
							var rids = $('#PoSplitCommissionGrid').jqGrid('getDataIDs');
							var nth_row_id = rids[0];
							validatePoCommissionTotals(nth_row_id);
							 $("#PoSplitCommissionGrid").trigger("reload");
								var grid=$("#PoSplitCommissionGrid");
								grid.jqGrid('resetSelection');
							    var dataids = grid.getDataIDs();
							    for (var i=0, il=dataids.length; i < il; i++) {
							        grid.jqGrid('setSelection',dataids[i], false);
							    }
							    POGeneralTabformChanges();
						},
						errorfunc : function(rowid, response) {
							console.log('An Error');
							$("#info_dialog").css("z-index", "12345");
							$(".jqmID1").css("z-index", "12345");
							return false;
						},
						afterrestorefunc : function(rowid) {
							console.log("afterrestorefunc");
							POGeneralTabformChanges();
						}
					}
				},
				editParams : {
					keys : false,
					successfunc : function(response) {
						console.log(response.responseText);
						console.log('successfunc - editParams');
						return true;
					},
					aftersavefunc : function(id) {
				        $("#del_PoSplitCommissionGrid").removeClass('ui-state-disabled');

				        var ids = $("#PoSplitCommissionGrid").jqGrid('getDataIDs');
						var cuinvrowid;
						if(ids.length==1){
							cuinvrowid = 0;
						}else{
							var idd = jQuery("#PoSplitCommissionGrid tr").length;
							for(var i=0;i<ids.length;i++){
								if(idd<ids[i]){
									idd=ids[i];
								}
							}
							cuinvrowid= idd;
						}
						if(SplitCommissionID=="new_row"){
							$("#" + SplitCommissionID).attr("id", Number(cuinvrowid)+1);
							var candoidrownum=Number(cuinvrowid)+1;
							$("#canDeleteID_new_row").attr("id", "canDeleteID_"+candoidrownum);
						}
						var rids = $('#PoSplitCommissionGrid').jqGrid('getDataIDs');
						var nth_row_id = rids[0];
						validatePoCommissionTotals(nth_row_id);
						$("#PoSplitCommissionGrid").trigger("reload");
						var grid=$("#PoSplitCommissionGrid");
						grid.jqGrid('resetSelection');
						var dataids = grid.getDataIDs();
						for (var i=0, il=dataids.length; i < il; i++) {
						   grid.jqGrid('setSelection',dataids[i], false);
						} 
						console.log('afterSavefunc editparams');
						$("#info_dialog").css("z-index", "12345");
						POGeneralTabformChanges();
					},
					errorfunc : function(rowid, response) {
						console.log(' editParams -->>>> An Error');
						$("#info_dialog").css("z-index", "12345");
						$(".jqmID1").css("z-index", "12345");
						$("#del_PoSplitCommissionGrid").removeClass('ui-state-disabled');
						$("#PoSplitCommissionGrid").trigger("reload");
						//return false;
	
					},
					afterrestorefunc : function( id ) {
						$("#del_PoSplitCommissionGrid").removeClass('ui-state-disabled');
						console.log('editParams -> afterrestorefunc');
						POGeneralTabformChanges();
				    },
					// oneditfunc: setFareDefaults
					oneditfunc : function(id) {
						console.log('OnEditfunc');
						$("#"+id+"_rep").focus();
						$("#del_PoSplitCommissionGrid").addClass('ui-state-disabled');
						$("#info_dialog").css("z-index", "12345");
						$(".jqmID1").css("z-index", "12345");
	                	/*var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
	                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
	                	alert(" >>>>>>>>>>>> "+$("#"+aSelectedRowId+"_cuSodetailId").val());
	                	 */
						}
				}
		});
}

function checkPOCommissionPaidorNot(joMasterID,JoReleaseID){
	var comStatus = 0;
	$.ajax({
		url: "./jobtabs5/CheckCommissionPaidRelease",
		type: "POST",
		async:false,
		data : {"joReleaseID":JoReleaseID,"joMasterID":joMasterID},
		success: function(data) {
			comStatus = data;
		}
	});
	
	if(comStatus==1){
		  var idis = $("#PoSplitCommissionGrid").jqGrid('getDataIDs');
		for(var i=0;i<=idis.length;i++){
			$("#canDeleteID_"+idis[i]).prop("disabled", true);
		}
		$("#PoSplitCommissionGrid_iladd").addClass('ui-state-disabled');
		$("#PoSplitCommissionGrid_iledit").addClass('ui-state-disabled');
	}
}

function validatePoCommissionTotals(id){
	console.log(id);
	var rows = jQuery("#PoSplitCommissionGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {
			total = 0;
		    row=jQuery("#PoSplitCommissionGrid").getRowData(rows[a]);
		    var id="#canDeleteID_"+rows[a];
			var canDelete=$(id).is(':checked');
			if(!canDelete){
		    total=row['allocated'];
			}
		    total = Number(total);
		    if(isNaN(total)){
		    	total=Number(1);
		    }
		    grandTotal = Number(grandTotal) + Number(total); 
		 }
		 console.log(grandTotal);
		 if(parseInt(grandTotal)<=100){
			return true;
		 }else{ 
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should below 100'+'</b></span>');
				/*jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
					// jQuery("#PoSplitCommissionGrid").jqGrid('restoreRow',id);
					jQuery("#PoSplitCommissionGrid").jqGrid('setSelection',id, true);
					 $('#PoSplitCommissionGrid_iledit').trigger('click');
					 return false;
					}}]}).dialog("open");*/
				
				jQuery(newDialogDiv).dialog({
					bgiframe: true,
					modal: true,
					autoOpen:false,
					closeOnEscape:false,
					draggable:false,
					resizable:false,
					width:300,
					height:150,
					title:"Information",
					buttons: {
						'OK': function() {
							$(this).dialog("close");
							
							var lastRowID = $("#PoSplitCommissionGrid tbody:first tr:nth-child(2)").attr('id');
							/*if(id!='new_row'){
								lastRowID=id;
							}*/
							 jQuery("#PoSplitCommissionGrid").jqGrid('setSelection',lastRowID, true);
							 $('#PoSplitCommissionGrid').trigger('click');
							 return false;
						}
					}
				}).dialog("open");
				jQuery("a.ui-dialog-titlebar-close").hide();
				
				
				return false;
		 }
}
//PoSplitCommissionGrid_ilcancel
function validatePOSplitCommissionTotals(){
	var rows = jQuery("#PoSplitCommissionGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {  total = 0;
		    row=jQuery("#PoSplitCommissionGrid").getRowData(rows[a]);
		    var id="#canDeleteID_"+rows[a];
			var canDelete=$(id).is(':checked');
			if(!canDelete){
		    total=row['allocated'];
			}
		   // total+=parseInt(total);
		    
		    grandTotal = Number(grandTotal) + Number(total); 
		 }
	var splitCommGridDatas = $('#PoSplitCommissionGrid').getRowData();
	var splitCommGridDataLocal = JSON.stringify(splitCommGridDatas);

	if(parseInt(grandTotal)<100 && (rows.length==0 && commissionRequired==1)){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:RED;">Please add Split Commission <br>Sum of allocated should be 100 </b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");
				return false;
		 }
	else if(parseInt(grandTotal)<100 && rows.length!=0){
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDilg");
		jQuery(newDialogDiv).html('<span><b style="color:RED;">Please add Split Commission <br>Sum of allocated should be 100 </b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");
		return false;
	}else if(parseInt(grandTotal)>100 && rows.length!=0){
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDilg");
		jQuery(newDialogDiv).html('<span><b style="color:RED;">Sum of allocated should be 100 </b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");
		return false;
	}
	else{
			 return true;
		 }
}

/*	else if(splitCommissionGridGlobalVar == splitCommGridDataLocal){
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).attr("id","msgDilg");
	jQuery(newDialogDiv).html('<span><b style="color:Green;">Split CommissionSum of allocated should be 100 </b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
		buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");*/

function canDeleteCheckboxFormatter(cellValue, options, rowObject){
	var id="canDeleteID_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"'  onclick='deleteCheckboxChanges(this.id)'>";
	return element;
}

function deleteCheckboxChanges(id){
	id="#"+id;
	console.log("deleteCheckboxChanges::"+id);
    var canDo=$(id).is(':checked');
    if(canDo){
    	$(id).val("true");
    }else{
    	$(id).val("false");
    }
}
function POGeneralTabformChanges(formvalue){
	//console.log("Hello I am from POGeneralTabformChanges");
	var new_po_generalform_values=poGeneralTabFormValidation();
    var new_po_general_form =  JSON.stringify(new_po_generalform_values);
    var gridRows = $('#PoSplitCommissionGrid').getRowData();
    var newPOcommissiongridform=JSON.stringify(gridRows);
    console.log("po_General_tab_form==>"+po_General_tab_form);
    console.log("new_po_general_form==>"+new_po_general_form);
    //console.log("commission_PO_grid_form==>"+commission_PO_grid_form);
    //console.log("newPOcommissiongridform==>"+newPOcommissiongridform);
    var gridreturnvalue=(commission_PO_grid_form!=newPOcommissiongridform)?true:false;
    if(commission_PO_grid_form==undefined){
    	gridreturnvalue=false;
    }
	 if(po_General_tab_form !=new_po_general_form  || gridreturnvalue){
    	if(formvalue=="TabChange"){
    		$( "#poreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
    		$( "#poreleasetab ul li:nth-child(3)" ).addClass("ui-state-disabled");
  	  var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,Before move to other tab have to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					$( "#poreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
					$( "#poreleasetab ul li:nth-child(3)" ).addClass("ui-state-disabled");
				    return false;
				}}}).dialog("open");
    	}else{
    		$( "#poreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
    		$( "#poreleasetab ul li:nth-child(3)" ).addClass("ui-state-disabled");
    	}
    }else{
    	$( "#poreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
    	$( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
    }
	
	return false;
}

function poGeneralTabFormValidation(){
	var value1="";var value2="";
	var value3="";var value4="";
	var value5="";var value6="";
	var value7="";var value8="";
	var value9="";var value10="";
	var value11="";var value12="";
	var value13=$("#orderId").val();	var value14=$("#frieghtChangesId").val();
	var value15=$("#shipViaId").val();	var value16=$("#wantedId").val();
	var value17=$("#wantedComboId").val();	var value18=$("#manfactureName").val();
	var value19=$("#contactId").val();	var value20=$("#poDateId").val();
	var value21=$("#customerNAMEId").val();	var value22=$("#ourPoId").val();
	var value23=$("#tagId").val();	var value24=$("#specialInstructionID").val();
	var value25=$("#subtotalGeneralId").val();	var value26=$("#freightGeneralId").val();
	var value27=$("#generalID").val();	var value28=$("#totalGeneralId").val();
	//var value29=$("#shiptoradio1").is(":checked");var value30=$("#shiptoradio2").is(":checked");
	//var value31=$("#shiptoradio3").is(":checked");var value32=$("#shiptoradio4").is(":checked");
	var value29=$("#PO_Shipto").contents().find("#shiptomoderhiddenid").val();
	var value33=$("#billtoradio1").is(":checked");var value34=$("#billtoradio2").is(":checked");
	var value35=$("#billtoradio3").is(":checked");
	var value36=$("#PO_Shipto").contents().find("#shiptoindexhiddenid").val();
	var value37=$("#PO_Shipto").contents().find("#shiptocusindexhiddenid").val();
	
	if(value29==3){
		value7=$("#PO_Shipto").contents().find("#shipToName").val();	
		value8=$("#PO_Shipto").contents().find("#shipToAddress1").val();
		value9=$("#PO_Shipto").contents().find("#shipToAddress2").val();
		value10=$("#PO_Shipto").contents().find("#shipToCity").val();
		value11=$("#PO_Shipto").contents().find("#shipToState").val();
		value12=$("#PO_Shipto").contents().find("#shipToZip").val();
//		 value7=$("#locationShipToAddressID").val();	 value8=$("#locationShipToAddressID1").val();
//		 value9=$("#locationShipToAddressID2").val();	 value10=$("#locationShipToCity").val();
//		 value11=$("#locationShipToState").val();	 value12=$("#locationShipToZipID").val();
	}
	if(value35)
	{
		 value1=$("#locationbillToAddressID").val();	 value2=$("#locationbillToAddressID1").val();
		 value3=$("#locationbillToAddressID2").val();	 value4=$("#locationbillToCity").val();
		 value5=$("#locationbillToState").val();	 value6=$("#locationbillToZipID").val();
	}
	
	if((value25+"").contains("$")){
		value25=value25.replace(/[^0-9\.-]+/g,"");
    }
	if((value26+"").contains("$")){
		value26=value26.replace(/[^0-9\.-]+/g,"");
    }
	if((value27+"").contains("$")){
		value27=value27.replace(/[^0-9\.-]+/g,"");
    }
	if((value28+"").contains("$")){
		value28=value28.replace(/[^0-9\.-]+/g,"");
    }
	
	var value=value1+value2+value3+value4+value5+value6+value7+value8+value9+value10+value11+value12+value13+value14+
	value15+value16+value17+value18+value19+value20+value21+value22+value23+value24+value25+value26+value27+
	value28+value29
	//+value30+value31+value32
	+value33+value34+value35+value36+value37;
	return value;
}
function closePOGeneralItemTab(){
	var new_po_generalform_values=poGeneralTabFormValidation();
    var new_po_general_form =  JSON.stringify(new_po_generalform_values);
    var gridRows = $('#PoSplitCommissionGrid').getRowData();
    var newPOcommissiongridform=JSON.stringify(gridRows);
    console.log("po_General_tab_form==>"+po_General_tab_form);
    console.log("new_po_general_form==>"+new_po_general_form);
    console.log("commission_PO_grid_form==>"+commission_PO_grid_form);
    console.log("newPOcommissiongridform==>"+newPOcommissiongridform);
    var gridreturnvalue=(commission_PO_grid_form!=newPOcommissiongridform)?true:false;
    if(commission_PO_grid_form==undefined){
    	gridreturnvalue=false;
    }
    console.log("gridreturnvalue=="+gridreturnvalue);
    var generaltabform=(po_General_tab_form !=new_po_general_form )?true:false;
    console.log("generaltabform=="+generaltabform);
    if(po_General_tab_form !=new_po_general_form  || gridreturnvalue){
  	  var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			//closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"Yes": function(){
					jQuery(this).dialog("close");
					savePORelease();
				    return false;
				},
				"No": function ()	{
					jQuery(this).dialog("close");
					jQuery("#porelease").dialog("close");
					$( "#poreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
				  	$( "#poreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
				  	$( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
				return false;	
				}}}).dialog("open");
    }else{
    	jQuery("#porelease").dialog("close");
    }
	
}

function setTaxTotal_PO(){
	var releaseGridRowId = $("#release").jqGrid('getGridParam', 'selrow');
	var vePOID = $("#release").jqGrid('getCell', releaseGridRowId, 'vePoId');
	var totalamount=$("#subtotalGeneralId").val();
	if(totalamount==undefined ||totalamount=="" ||totalamount==null){
		totalamount=0.00;
	}
	totalamount=totalamount.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 var freight=$("#freightGeneralId").val();
	 if(freight==undefined ||freight=="" ||freight==null){
		 freight=0.00;
		}
	 freight=freight.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 var taxRate=$("#taxGeneralId").val();
	 var totalWithTax=getsubtotalwithtax_PO(vePOID);
     var taxamount=(Number(totalWithTax)*Number(taxRate))/100;
	 if(taxamount<0)
		 taxamount=-taxamount;
	 
	 $("#generalID").val(formatCurrency(taxamount));
	 var overalltotal=Number(totalamount)+Number(floorFigureoverall(freight,2))+Number(floorFigureoverall(taxamount,2));
	 $("#totalGeneralId").val(formatCurrency(overalltotal));
}
function getsubtotalwithtax_PO(vepoid){
var totalamt=0.00;
if(vepoid!=undefined && vepoid!=null && vepoid!="" && vepoid!=0){
$.ajax({
	url: "./jobtabs5/vePODetailLst",
	type: "POST",
	async:false,
	data : {"vePoId":vepoid},
	success: function(data) {
		for(var i=0;i<data.length;i++){
			var unitcost=data[i].unitCost;
			var quantity=data[i].quantityOrdered;
			var pmult=data[i].priceMultiplier;
			if(pmult==null||pmult==""||pmult==0){
				pmult=1;
			}
			var taxable=data[i].taxable;
			if(taxable==1||taxable==true){
				unitcost=Number(floorFigureoverall(unitcost,2));
				quantity=Number(floorFigureoverall(quantity,2));
				pmult=Number(pmult);
				totalamt=Number(totalamt)+Number(floorFigureoverall((unitcost*quantity*pmult),2));
			}
		}
		
	}
});
}
return Number(totalamt);
}


function check_posplitcommision( value, colname ) {
	 var result = null;
	 
	 var rows = jQuery("#PoSplitCommissionGrid").getDataIDs();
		var total = 0;
		var grandTotal = value;
		var row = '';
			 for(a=0;a<rows.length;a++)
			 {
				total = 0;
			    row=jQuery("#PoSplitCommissionGrid").getRowData(rows[a]);
			    var id="#canDeleteID_"+rows[a];
				var canDelete=$(id).is(':checked');
				if(!canDelete){
			    total=row['allocated'];
			    if(isNaN(total)){
			    	total=0;
			    }
				}
			    total = Number(total);
			   
			    grandTotal = Number(grandTotal) + Number(total); 
			 }
			 console.log(grandTotal);
	 
	if(parseInt(grandTotal)>100){
				 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 300);
				result = [false, '<b style="color:Green;">Sum of allocated should below 100'+'</b>'];
	}else{
		$("#PoSplitCommissionGrid_iladd").removeClass("ui-state-disabled");
		$("#PoSplitCommissionGrid_iledit").removeClass("ui-state-disabled");
		$("#PoSplitCommissionGrid_ilsave").addClass("ui-state-disabled");
		$("#PoSplitCommissionGrid_ilcancel").addClass("ui-state-disabled");
		 result = [true,""];
	}
	return result;
}