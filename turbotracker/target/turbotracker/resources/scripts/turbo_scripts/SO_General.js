$(document).ready(function(){
$('#SObackWardId').hide();
$('#SOforWardId').hide();
$('#SObillbackWardId').hide();
$('#SObillforWardId').hide();
$('#dateOfcustomerGeneral').datepicker();
$('#SOShipDate').datepicker();
$('#promisedID').datepicker();
var cusoid = $('#Cuso_ID').text();

if(cusoid===''){
	PreloadData(0);}
else
	PreloadData(cusoid);
if($('#operation').val() !== 'create' && $('#operation').val() !== 'edit'){
	
	addressToShip();

	CustomerAddress();
}
if($('#operation').val() === 'create' || $('#operation').val() === 'edit'){
	$('#SObackWardId').show();
	$('#SOforWardId').show();
	
}

});
function PreloadData(cusoid){	
	$('#salesreleasetab').tabs({ selected: 0 });
	$('#salesreleasetab').tabs({ active: 0 });
	var cuSOID = cusoid;
	var rxMasterID = $('#rxCustomer_ID').text();
	if(cuSOID!=null && typeof(cuSOID) != "undefined"){
	$.ajax({
		url: "./salesOrderController/getPreLoadData",
		type: "POST",
		data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID,
		success: function(data) {
			$("#CustomerNameGeneral").val(data.CustomerName);
			if (typeof(data.Cuso) != "undefined" && data.Cuso != null){
			$('#dateOfcustomerGeneral').val($("#poDate_ID").text());
			$("#SOnumberGeneral").val(data.Cuso.sonumber);
			$('#SOGeneral_subTotalID').val(formatCurrency(0));
			$('#SOGeneral_totalID').val(formatCurrency(0));
			$('#termhiddenID').val(data.Cuso.cuTermsId);
			$('#poID').val(data.Cuso.trackingNumber);
			$('#promisedID').val(data.Cuso.datePromised);
			$('#CreatedOn').val(data.Cuso.createdOn);
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
			
			
			if($('#operation').val() === 'update'){
				$("#rxCustomer_ID").text(data.Cuso.rxBillToId);
				}
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
				
				$("#rxShipToOtherAddressID").val(data.Cuso.rxShipToAddressId);	
				$("#shipTorxCustomer_ID").val(data.Cuso.rxCustomerId);
				$("#shipToCustomerAddressID").val(data.Cuso.rxShipToId);
				
				var mode = data.Cuso.shipToMode;
				if(mode == 1){
					$("#shiptolabel1").addClass('ui-state-active');
					shipToAddressCreate('Pick Up');
				}
				else if(mode == 2){
					$("#shiptolabel2").addClass('ui-state-active');
					$("#shiptolabel1").removeClass('ui-state-active');					
					shipToAddressCreate('Customer');
				}
				else if(mode == 3){
					$("#shiptolabel4").addClass('ui-state-active');
					shipToAddressCreate('Other');
				}
				else
					$("#shiptolabel3").addClass('ui-state-active');
				
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
			
		}
	});
  }
}


/*function updateBillToAndShipToAddressSetting(vePOID, usAddressID, updateKey){
	$.ajax({
	   url : "./jobtabs3/updateBillToAndShipToSetting",
	   type : "GET",
	   data : { 'vePOID': vePOID, 'isAddressID': usAddressID, 'updaetKey': updateKey},
	   success : function (msg) {
		   
	   },
	   error : function (msg) {}
	});
	return true;
}
*/

function saveSORelease(){
	 var newDialogDiv = jQuery(document.createElement('div'));
	 var newValidateDiv = jQuery(document.createElement('div'));
	var cuSoid =  $('#Cuso_ID').text();
	if(typeof(cuSoid) == undefined || cuSoid == '' || cuSoid == 0 ){
		if($('#operation').val() === 'create'){
			cuSoid = newcusoID;
		}
	}	
	var dateofSO=$('#dateOfcustomerGeneral').val();
	var ShipDate = $('#SOShipDate').val();
	var SalesManId = $('#salesmanhiddenID').val();
	var csrhiddenID = $('#csrhiddenID').val();
	var salesManagerhiddenID =$('#salesManagerhiddenID').val();
	var engineerhiddenID = $('#engineerhiddenID').val();
	var projectManagerhiddenID=$('#projectManagerhiddenID').val();
	var whrhouseID =$('#whrhouseID').val();
	var shipViaId = $('#SOshipViaId').val();
 	var divisionID = $('#SOdivisionID').val();
 	var promisedID = $('#promisedID').val();
 	var trackingNumber = $('#poID').val();
 	var taxTerritoryID = $('#taxhiddenID').val();
 	var terms = $('#terms').val();
 	var custID =$('#custID').val();
 	var frieght = $('#SOGeneral_frightID').val(); 
 	frieght= parseFloat(frieght.replace(/[^0-9-.]/g, ''));
 	var tag = $('#tagJobID').val();
 	var Total = $('#SOGeneral_subTotalID').val();
 	var addressId =$('#addressID').val();
 	Total = parseFloat(Total.replace(/[^0-9-.]/g, ''));
 	$("#shipTo1").hide();
 	formattax(frieght);
 	var taxTotal = $('#SOGeneral_taxvalue').val();
 	taxTotal = parseFloat(taxTotal.replace(/[^0-9-.]/g, ''));
 	if(isNaN(taxTotal) || taxTotal == 'NaN')
 		taxTotal = 0;
 	var taxrate = $('#SOGeneral_taxId').val();
 	var subTotal = $('#SOGeneral_subTotalID').val();
 	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
 	if(isNaN(subTotal) || subTotal == 'NaN')
 		subTotal = 0;
 	var total = $('#SOGeneral_totalID').val();
 	total =  parseFloat(total.replace(/[^0-9-.]/g, ''));
 	if(isNaN(total) || total == 'NaN')
 		total = 0;
 	
 	var billToCustomerNameGeneralID = $('#billToCustomerNameGeneralID').val();
 	var shipTorxCustomer_ID = $('#shipTorxCustomer_ID').val();
 	var operation = $('#operation').val();
 	var customerShipToOtherID = $('#customerShipToOtherID').val();
 	if(customerShipToOtherID == ''){
 		customerShipToOtherID = $('#customerShipToOtherIDeditable').val("Other");
 	}
 	
 	var date = $.datepicker.formatDate('mm/dd/yy', new Date());
 	
 	if(dateofSO === null || dateofSO === ''){
 		var date = $.datepicker.formatDate('mm/dd/yy', new Date());
 		dateofSO = date;
 	}
 		
	if(ShipDate === null || ShipDate === ''){
		var validateMsg = "Shiping Date field is Mandotory."
		 	jQuery(newValidateDiv).attr("id","ShipDate");
			jQuery(newValidateDiv).html('<span><b style="color:Green;">'+validateMsg+'</b></span>');
		
		jQuery(newValidateDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		
		return false;		
	
	}
	if(promisedID === null || promisedID === ''){
		var validateMsg = "Promised filed is Mandotory."
		 	jQuery(newValidateDiv).attr("id","promisedID");
			jQuery(newValidateDiv).html('<span><b style="color:Green;">'+validateMsg+'</b></span>');
		
		jQuery(newValidateDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		
		return false;		
	
	}
 	
 	var SoGeneralValues =  'cuSoid='+cuSoid+'&createdOn='+dateofSO+'&cuAssignmentId0='+SalesManId+'&cuAssignmentId1='+csrhiddenID+'&cuAssignmentId2='+salesManagerhiddenID+'&cuAssignmentId3='+engineerhiddenID+'&cuAssignmentId4='+projectManagerhiddenID+'&prFromWarehouseId='+whrhouseID+'&veShipViaId='
 							+shipViaId+ '&coDivisionID='+divisionID+'&datePromised='+promisedID+'&trackingNumber='+trackingNumber+'&tag='+tag+'&freight='+frieght+'&customerPonumber='+custID+'&cuTermsId='+terms+'&coTaxTerritoryId='+taxTerritoryID+'&joScheddDetailID='+addressId
 							+'&subTotal='+subTotal+'&taxRate='+taxrate+'&taxTotal='+taxTotal+'&costTotal='+total+'&ShipDate='+ShipDate+'&billToCustomerNameGeneralID='+billToCustomerNameGeneralID+'&shipTorxCustomer_ID='+shipTorxCustomer_ID+'&operation='+operation+'&customerShipToOtherID='+customerShipToOtherID
 							+'&shipToOtherNameID='+$('#SOlocationShipToAddressIDeditable').val()+'&shipToOtherAddress1='+$('#SOlocationShipToAddressID1editable').val()+'&shipToOtherAddress2='+$('#SOlocationShipToAddressID2editable').val()+'&shipToOtherCity='+$('#SOlocationShipToCityeditable').val()+'&shipToOtherState='+$('#SOlocationShipToStateeditable').val()+'&shipToOtherZip='+$('#SOlocationShipToZipIDeditable').val()
 							+'&rxShipToOtherAddressID='+$("#rxShipToOtherAddressID").val();
 	
 	
 	console.log("SoGeneralValues----->"+SoGeneralValues);
 	
 	$.ajax({
		url: "./salesOrderController/addSoRelease",
		type: "POST",
		data : SoGeneralValues,
		success: function(data) {
			var CuSOId = data.cuSoid;
			if(CuSOId != null || CuSOId != '')
				$('#operation').val('edit');
			var sonumber;
			if($('#operation').val() === 'create'  || $('#operation').val() === 'edit'){
				sonumber = data.sonumber;
				$('#SOnumberGeneral').val(sonumber);
				//$("#jobNumber_ID").text(CuSOId);
				$("#rxShipToOtherAddressID").val(data.rxShipToAddressId);				
				FormatDate(data.createdOn);
			}
			$("#salesreleasetab").tabs({
			       disabled : false
			      });
			
			$('#Cuso_ID').text(CuSOId);
			var errorText = "Sales Order General Tab details are Saved.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit'){
				if("Save" == $('#setButtonValue').val())
				{
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");					
				}
				else
				{
					if("SaveandClose" == $('#setButtonValue').val())
					{					
						document.location.href = "./salesorder?oper=create";
					}
				}
			
			}
			else
			{
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");
			}
			return true;
			}
 	});
}
 	

function addressToShip(){
	$('#SOforWardId').hide();
	$('#SObackWardId').hide();
	//$('#customerShipToOtherID').val("Job Site");
var rxMasterId = $("#rxCustomer_ID").text();
if($('#operation').val() === 'create' || $('#operation').val() === 'edit'){
	$("#SOlocationShipToAddressIDeditable").prop("disabled",true);
	 $("#SOlocationShipToAddressID1editable").prop("disabled",true);
	 $("#SOlocationShipToAddressID2editable").prop("disabled",true);
	 $("#SOlocationShipToCityeditable").prop("disabled",true);
	 $("#SOlocationShipToStateeditable").prop("disabled",true);
	 $("#SOlocationShipToZipIDeditable").prop("disabled",true);
	 $("#SOlocationShipToAddressIDeditable").val("");
	 $("#SOlocationShipToAddressID1editable").val("");
	 $("#SOlocationShipToAddressID2editable").val("");
	 $("#SOlocationShipToCityeditable").val("");
	 $("#SOlocationShipToStateeditable").val("");
	 $("#SOlocationShipToZipIDeditable").val("");
}
else
{
	operationVar = "bill";
	 $.ajax({
			url: "./salesOrderController/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				$('#SOlocationbillToAddressname').val($('#CustomerNameGeneral').val());
				$('#SOGenerallocationbillToAddressID1').val(data.address1);
				$('#SOGenerallocationbillToAddressID2').val(data.address2);
				$('#SOGenerallocationbillToCity').val(data.city);
				$('#SOGenerallocationbillToState').val(data.state);
				$('#SOGenerallocationbillToZipID').val(data.zip);
				if($('#operation').val() !== 'create' && $('#operation').val() !== 'edit'&& $('#operation').val() !== 'update'){
					$('#SOshiptoradio3').attr('checked','checked');
					$('#shiptolabel3').addClass('ui-state-active');
				}
				
				$('#addressID').val(3);
				}
		});
	 
	 if($('#operation').val() === 'update'){
			$("#SOlocationShipToAddressIDeditable").prop("disabled",true);
			 $("#SOlocationShipToAddressID1editable").prop("disabled",true);
			 $("#SOlocationShipToAddressID2editable").prop("disabled",true);
			 $("#SOlocationShipToCityeditable").prop("disabled",true);
			 $("#SOlocationShipToStateeditable").prop("disabled",true);
			 $("#SOlocationShipToZipIDeditable").prop("disabled",true);
			 $("#SOlocationShipToAddressIDeditable").val("");
			 $("#SOlocationShipToAddressID1editable").val("");
			 $("#SOlocationShipToAddressID2editable").val("");
			 $("#SOlocationShipToCityeditable").val("");
			 $("#SOlocationShipToStateeditable").val("");
			 $("#SOlocationShipToZipIDeditable").val("");
		}
	 
}

}
function shipToAddress(){
	$('#regularTable').show();
	$('#SOforWardId').hide();
	$('#SObackWardId').hide();
	$('#hiddenTable').hide();
	var jobnumber = $("#SOnumberGeneral").val();
	
	jobnumber = jobnumber.substring(0,jobnumber.length-1);
	if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
		$('#SOlocationShipToAddressID').prop("disabled",true);
		$('#SOlocationShipToAddressID1').prop("disabled",true);
		$('#SOlocationShipToAddressID2').prop("disabled",true);
		$('#SOlocationShipToCity').prop("disabled",true);
		$('#SOlocationShipToState').prop("disabled",true);
		$('#SOlocationShipToZipID').prop("disabled",true);
		
		$('#SOlocationShipToAddressID').val("");
		$('#SOlocationShipToAddressID1').val("");
		$('#SOlocationShipToAddressID2').val("");
		$('#SOlocationShipToCity').val("");
		$('#SOlocationShipToState').val("");
		$('#SOlocationShipToZipID').val("");
		$('#SOlocationShipToAddressID').focus(function(){
	        $(this).data('placeholder',$(this).attr('placeholder'))
	        $(this).attr('placeholder','');
	     });
	}
	else
	{
		$.ajax({
			url: "./salesOrderController/getJobsiteAddress",
			type: "GET",
			data : {"jobnumber" : jobnumber},
			success: function(data) {
				$('#SOlocationbillToAddressname').val($('#CustomerNameGeneral').val());
				$('#SOlocationShipToAddressID1').val(data.locationAddress1);
				$('#SOlocationShipToAddressID2').val(data.locationAddress2);
				$('#SOlocationShipToCity').val(data.locationCity);
				$('#SOlocationShipToState').val(data.locationState);
				$('#SOlocationShipToZipID').val(data.locationZip);
				$('#SOshiptoradio3').attr('checked','checked');
				$("#shiptolabel4").removeClass('ui-state-active');
				$("#shiptolabel1").removeClass('ui-state-active');
				$("#shiptolabel2").removeClass('ui-state-active');
				$("#shiptolabel3").addClass('ui-state-active');
				$('#addressID').val(2);
				}
		});
	}
	
}
function pickupAddress(){
	$('#regularTable').show();
	$('#SOforWardId').show();
	$('#SObackWardId').show();
	$('#hiddenTable').hide();
	$('#customerShipToOtherID').val("Pick Up");
	var cuSoid =  $('#Cuso_ID').text();
	if(cuSoid != null && cuSoid != '')
	{
		$.ajax({
			url: "./product/getPickUpAddress",
			type: "POST",
			data : {"CusoID" : cuSoid},
			success: function(data) {
				$('#SOlocationShipToAddressID').val(data.description);
				$('#SOlocationShipToAddressID1').val(data.address1);
				$('#SOlocationShipToAddressID2').val(data.address2);
				$('#SOlocationShipToCity').val(data.city);
				$('#SOlocationShipToState').val(data.state);
				$('#SOlocationShipToZipID').val(data.zip);
				$('#SOshiptoradio1').attr('checked','checked');				 
				$('#addressID').val(2);
				if('update' === $('#operation').val()){
					loadTaxTerritoryRate(data.coTaxTerritoryId,"Pick Up");
				}
			}
	});
		
		
	}
	else
	{
		loadPickUpAddress();
				
	}
	
	$('#shiptolabel1').addClass('ui-state-active');
	$('#shiptolabel2').removeClass('ui-state-active');
	$('#shiptolabel3').removeClass('ui-state-active');
	$('#shiptolabel4').removeClass('ui-state-active');
	
	if (!$('#shiptolabel1').hasClass("ui-state-active")) {
	}
	else
		{
		}
	
}
function CustomerAddress(){
	$('#regularTable').show();
	$('#SOforWardId').hide();
	$('#SObackWardId').hide();
	$('#hiddenTable').hide();
	var rxMasterId = null;
	if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
		$('#customerShipToOtherID').val("Customer");
		$('#SOlocationShipToAddressID').prop("disabled",false);
		$('#SOlocationShipToAddressID').focus();		
	}	
	rxMasterId = $("#rxCustomer_ID").text();
	
	
	$.ajax({
		url: "./salesOrderController/getCustomerName",
		type: "GET",
		data : {"customerID" : rxMasterId},
		success: function(data) {
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
				
				$('#SOlocationShipToAddressID').val(data.name);
				
			}
			else
			{
				$('#SOlocationShipToAddressID').val($('#CustomerNameGeneral').val());
			}
			
			$('#SOlocationShipToAddressID1').val(data.address1);
			$('#SOlocationShipToAddressID2').val(data.address2);
			$('#SOlocationShipToCity').val(data.city);
			$('#SOlocationShipToState').val(data.state);
			$('#SOlocationShipToZipID').val(data.zip);
			//$('#SOshiptoradio2').attr('checked','checked');
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
				loadTaxTerritoryRate(data.coTaxTerritoryId,"Customer");
			}
			$('#shiptolabel1').removeClass('ui-state-active');
			$('#shiptolabel2').addClass('ui-state-active');
			$('#shiptolabel3').removeClass('ui-state-active');
			$('#shiptolabel4').removeClass('ui-state-active');
			$('#addressID').val(2);
		}
});
}
function ShipToCustomerAddress(){
	$('#regularTable').show();
	$('#SOforWardId').hide();
	$('#SObackWardId').hide();
	$('#hiddenTable').hide();
	var rxMasterId = null;
	if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
		$('#customerShipToOtherID').val("Customer");
		$('#SOlocationShipToAddressID').prop("disabled",false);
		$('#SOlocationShipToAddressID').focus();
		rxMasterId = $("#shipToCustomerAddressID").val();
	}
	else
	{
		rxMasterId = $("#rxCustomer_ID").text();
	}
	
	$.ajax({
		url: "./salesOrderController/getCustomerShipToAddress",
		type: "GET",
		data : {"customerID" : rxMasterId},
		success: function(data) {
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
				
				$('#SOlocationShipToAddressID').val(data.name);
				
			}
			else
			{
				$('#SOlocationShipToAddressID').val($('#CustomerNameGeneral').val());
			}
			
			$('#SOlocationShipToAddressID1').val(data.address1);
			$('#SOlocationShipToAddressID2').val(data.address2);
			$('#SOlocationShipToCity').val(data.city);
			$('#SOlocationShipToState').val(data.state);
			$('#SOlocationShipToZipID').val(data.zip);
			//$('#SOshiptoradio2').attr('checked','checked');
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
				loadTaxTerritoryRate(data.coTaxTerritoryId,"Customer");
			}
			$('#shiptolabel1').removeClass('ui-state-active');
			$('#shiptolabel2').addClass('ui-state-active');
			$('#shiptolabel3').removeClass('ui-state-active');
			$('#shiptolabel4').removeClass('ui-state-active');
			$('#addressID').val(2);
		}
});
}
function otherShiptoAddress(){
	$('#regularTable').hide();
	$('#hiddenTable').show();
	$('#customerShipToOtherIDeditable').val("Other");
	$('#customerShipToOtherID').val("Other");
	//$('#SOshiptoradio04').removeAttr("disabled");	
	if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
		if($("#rxShipToOtherAddressID").val() != null && $("#rxShipToOtherAddressID").val() != '')
		{
			$.ajax({
				url: "./salesOrderController/getShipToOtherAddress",
				type: "GET",
				data : {"addressID" : $("#rxShipToOtherAddressID").val()},
				success: function(data) {
					$('#SOlocationShipToAddressIDeditable').val(data.name);
					$('#SOlocationShipToAddressID1editable').val(data.address1);
					$('#SOlocationShipToAddressID2editable').val(data.address2);
					$('#SOlocationShipToCityeditable').val(data.city);
					$('#SOlocationShipToStateeditable').val(data.state);
					$('#SOlocationShipToZipIDeditable').val(data.zip);
					//$('#SOshiptoradio2').attr('checked','checked');
					loadTaxTerritoryRate("","Other");
					$('#addressID').val(2);
				}
			});		
			
		}
		else
		{
			 $("#SOlocationShipToAddressIDeditable").attr("disabled",false);
			 $("#SOlocationShipToAddressID1editable").attr("disabled",false);
			 $("#SOlocationShipToAddressID2editable").attr("disabled",false);
			 $("#SOlocationShipToCityeditable").attr("disabled",false);
			 $("#SOlocationShipToStateeditable").attr("disabled",false);
			 $("#SOlocationShipToZipIDeditable").attr("disabled",false);
		}		
		
	}	
	
}
function formattax(frieght){
	var subTotal = $('#SOGeneral_subTotalID').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var tax = $('#SOGeneral_taxvalue').val();
	tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
	var total = subTotal+tax+frieght;
	$('#SOGeneral_totalID').val(formatCurrency(total));
}
function FormatDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$("#dateOfcustomerGeneral").val(createdDate);
}
function FormatShipDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$("#SOShipDate").val(createdDate);
}
function sendPOEmail(poGeneralKey){
	alert('123');
	try{
		var newDialogDiv = jQuery(document.createElement('div'));
	var bidderGrid = $("#salesrelease");
	var aQuotePDF = "purchase";
	//alert('123123');
	var rxMasterID = $('#rxCustomer_ID').text();
	//alert('aContactID::'+rxMasterID)
	var cusotmerPONumber = $("#SOnumberGeneral").val();
	alert(cusotmerPONumber);
	
		$.ajax({ 
			url: "./vendorscontroller/GetContactDetailsFromCuso",
			mType: "GET",
			data : { 'rxMasterID' : rxMasterID},
			success: function(data){
				var aFirstname = data.firstName;
				var aLastname = data.lastName;
				var aEmail = data.email;
				var arxContact = aFirstname + ' '+aLastname;
				var arxContactid='';
				try{
					arxContactid = data.rxContactId;
					
				}catch(err){//alert(err.message);}
					
				}
				//alert(arxContactid +" :: "+data.rxContactId);
				
			
				var arxContact = aFirstname + ' '+aLastname;
				if(aEmail === ''){
					errorText = "Are you sure you want to send this PO to"+ arxContact +"?";
				}else{
					errorText = "Are you sure you want to send this PO to"+ arxContact +"("+ aEmail +")?";
				}
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
				buttons:{
					"Send": function(){
						$('#loadingPODiv').css({"visibility": "visible"});
						viewPOPDFSave(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, '');
						//alert('012');
						
						
						jQuery(this).dialog("close");
					},
					Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
			}
		});
		
	}catch(err){alert(err.message);}
	return true;
}

function sentMailPOFunction(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId){
	
	try{
		var newDialogDiv = jQuery(document.createElement('div'));
	$('#loadingPOGenDiv').css({"visibility": "visible"});
	//alert('11');
	$.ajax({ 
		url: "./sendMailServer/sendSOMail",
		mType: "POST",
		data : {'contactID' : aContactID, 'SONumber' : cusotmerPONumber},
		success: function(data){
			//alert('empty email 1 -' +aContactID+cusotmerPONumber);
			$('#loadingPOGenDiv').css({"visibility": "hidden"});
			$('#loadingPODiv').css({"visibility": "hidden"});
			var errorText = "Mail Sent Successfully.";
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
				buttons: [{height:35,text: "OK",click: function() {
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; 
					var yyyy = today.getFullYear().toString().substr(2,2);
					var hours = today.getHours();
					var minutes = today.getMinutes();
					var ampm = hours >= 12 ? 'PM' : 'AM';
					hours = hours % 12;
					hours = hours ? hours : 12;
					if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
					if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
					$.ajax({ 
						url: "./jobtabs3/updateEmailStampValue",
						mType: "GET",
						data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
						success: function(data){ $("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today); }
					});
					$(this).dialog("close"); 
				}}] }).dialog("open");
		}
	});
	}catch(err){
		//alert(err.message);
	}
}
function activeButtoSet(type)
{
	if(type === 'Pick Up')
	{
		$('#shiptolabel1').addClass('ui-state-active');
		$('#shiptolabel2').removeClass('ui-state-active');
		$('#shiptolabel3').removeClass('ui-state-active');
		$('#shiptolabel4').removeClass('ui-state-active');
	}
	else if(type === 'Customer')
	{
		$('#shiptolabel1').removeClass('ui-state-active');
		$('#shiptolabel2').addClass('ui-state-active');
		$('#shiptolabel3').removeClass('ui-state-active');
		$('#shiptolabel4').removeClass('ui-state-active');
	}
	else if(type === 'Other')
	{
		$('#shiptolabel1').removeClass('ui-state-active');
		$('#shiptolabel2').removeClass('ui-state-active');
		$('#shiptolabel3').removeClass('ui-state-active');
		$('#SOshiptolabel4').addClass('ui-state-active');
	}
}

var aDivision = "${requestScope.CuSoDetail.prFromWarehouseId}";
$("#whrhouseID option[value=" + aDivision + "]").attr("selected", true);



/**--- Ajax auto suggest functions ---*/
$(function() { var cache = {}, lastXhr;
$( "#salesmanID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesmanhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#csrID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#csrhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#salesManagerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesManagerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#engineerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#engineerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#projectManagerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#projectManagerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	} }); });

$(function() { var cache = {}, lastXhr;
$( "#taxID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#taxhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });	

$(function() { var cache = {}, lastXhr;
$( "#termID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#termhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#viaID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#viahiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "jobtabs3/shipVia", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#CustomerNameGeneral" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) {
		var id = ui.item.id; $("#CustomerNameGeneralID").val(id); 
		},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/customerName", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });


