
var withPrice='NotChecked';
var so_general_form;
var commission_grid_form;
jQuery(document).ready(function(){
	$("#salesrelease").dialog({
        'height': 'auto'
    });
	//$(".ui-button-text-only .ui-button-text").css({"display":"none"});
	//.ui-dialog .ui-dialog-buttonpane
	//$(".ui-dialog .ui-dialog-buttonpane").css({"display":"none"});
	
	var jobStatus = '';
	if(typeof(getUrlVars()) != undefined && getUrlVars().indexOf("jobStatus")>-1){
		jobStatus = getUrlVars()["jobStatus"];
	if(typeof(jobStatus) != undefined && jobStatus.indexOf("Closed")>-1){
		$('#SavePOReleaseID').css('cursor','default');
		$('#SavePOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		document.getElementById("SavePOReleaseID").disabled = true;
		
	}else{
		if($('#SavePOReleaseID').is(':disabled')){
			document.getElementById("SavePOReleaseID").disabled = false;
			$('#SavePOReleaseID').css('cursor','pointer');
			$('#SavePOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		}
	}
}
$('#SObackWardId').hide();
$('#SOforWardId').hide();
$('#SObillbackWardId').hide();
$('#SObillforWardId').hide(); 
$('#dateOfcustomerGeneral').datepicker();
$('#SOShipDate').datepicker();
$('#promisedID').datepicker();
$('#promisedIDwz').datepicker();
$('#dateOfcustomerGeneral').datepicker();
var cusoid = $('#Cuso_ID').text();


console.log('SO_General.js Called');
$("#cusinvoicetab").dialog({
    open : function(event, ui) {
    	
		$("#PO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
		$("#SO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
		$("#CI_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset();
		
    	$('#cusinvoicetab').tabs({ selected: 0 });
     var cuInvoiceID = $('#cuInvoiceID').text();
     
     if (cuInvoiceID == '') {
      $("#cusinvoicetab").tabs({
       disabled : [ 1, 2 ]
      });
     } else {
      $("#cusinvoicetab").tabs({
       disabled : false
      });
     }
    }
   });     
if(cusoid===''){
	PreloadSOGeneralData(0);
	}
else{
	PreloadSOGeneralData(cusoid);

}


/*if($('#operation').val() !== 'create' && $('#operation').val() !== 'edit'){
	addressToShip();
	CustomerAddress();
}*/
if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
	$('#SObackWardId').show();
	$('#SOforWardId').show();
	
}
/*alert("Test123");
if(typeof(hideWithPrice) !== undefined && hideWithPrice === 'outsideJob')
{
	alert("Test");
	$('#withPriceLine').css('display','block');
	$('#withPriceLineLabel').css('display','block');
	$('#withPrice').css('display','block');
	$('#withPriceLabel').css('display','block');
}*/
if(typeof(withPrice) !== undefined){
	if(withPrice === 'Checked'){
		$('#withPrice').prop('checked',true);
		$('#withPriceLine').css('display','block');
		$('#withPriceLineLabel').css('display','block');
		$('#withPrice').css('display','block');
		$('#withPriceLabel').css('display','block');
	}
	else if(withPrice === 'NotChecked'){
		$('#withPrice').prop('checked',false);
		$('#withPriceLine').css('display','block');
		$('#withPriceLineLabel').css('display','block');
		$('#withPrice').css('display','block');
		$('#withPriceLabel').css('display','block');
	}
}
loadSOcategories();
jQuery("#sogeneralAlertDiv").dialog({
	autoOpen:false,
	width:445,
	title:"Quick Book",
	left: 440,
	modal:true,
	buttons:{
		"Ok":function(){
			$('#sogeneralAlertDiv').dialog("close");
			$('#salesrelease').dialog("close");
		}},
		close:function(){return true;}
});

chkSplitCommissionValidation();
});
/*
Updated by:Velmurugan
Updated on:9-1-2014
Description:while loading the page label,textbox and search image enable hide and show based upon sysassignment table
*/
function loadSOcategories(){
	
	var Category1labDesc=$("#CustomerCategory1hidden").val();
	var Category2labDesc=$("#CustomerCategory2hidden").val();
	var Category3labDesc=$("#CustomerCategory3hidden").val();
	var Category4labDesc=$("#CustomerCategory4hidden").val();
	var Category5labDesc=$("#CustomerCategory5hidden").val();
	
	//CustomerCategory1label
	$("#CustomerCategory1label").text(Category1labDesc);
	$("#CustomerCategory2label").text(Category2labDesc);
	$("#CustomerCategory3label").text(Category3labDesc);
	$("#CustomerCategory4label").text(Category4labDesc);
	$("#CustomerCategory5label").text(Category5labDesc);
	
	//customer_salesRepsList customer_CSRList customer_SalesMgrList customer_EngineersList customer_PrjMgrList
	//CustomerCategory1image
	if(Category1labDesc==undefined ||Category1labDesc==null ||Category1labDesc==""||Category1labDesc.length==0){
		$("#CustomerCategory1row").css({ display: "none" });
		
	}else{
		$("#CustomerCategory1row").css({ display: "inline-block" });
	}
	if(Category2labDesc==undefined ||Category2labDesc==null ||Category2labDesc==""||Category2labDesc.length==0){
		$("#CustomerCategory2row").css({ display: "none" });
		
	}else{
		$("#CustomerCategory2row").css({ display: "inline-block" });
	}
	if(Category3labDesc==undefined ||Category3labDesc==null ||Category3labDesc==""||Category3labDesc.length==0){
		$("#CustomerCategory3row").css({ display: "none" });
		
	}else{
		$("#CustomerCategory3row").css({ display: "inline-block" });
	}
	if(Category4labDesc==undefined ||Category4labDesc==null ||Category4labDesc==""||Category4labDesc.length==0){
		$("#CustomerCategory4row").css({ display: "none" });
		
	}else{
		$("#CustomerCategory4row").css({ display: "inline-block" });
	}
	if(Category5labDesc==undefined ||Category5labDesc==null ||Category5labDesc==""||Category5labDesc.length==0){
		$("#CustomerCategory5row").css({ display: "none" });
		
	}else{
		$("#CustomerCategory5row").css({ display: "inline-block" });
	}
	
	
}

function PreloadSOGeneralData(cusoid){
	$('#salesreleasetab').tabs({ selected: 0 });
	$('#salesreleasetab').tabs({ active: 0 });
	resetSOGeneralForm();
	
	
	$("#PO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
	$("#CI_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
	$("#SO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
	$("#SO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset();
	$("#SO_Shipto").contents().find( "#shipToRadioButtonSet #shiptoaddlabel1 span" ).text("Pickup");
	var cuSOID = cusoid;
	var rxMasterID = $('#rxCustomer_ID').text();
	var jobNumber="";
	var joMasterID=0;
	if($("#jobNumber_ID").text()!= null && $("#jobNumber_ID").text() !=""){
		jobNumber = $("#jobNumber_ID").text();
		joMasterID=$("#joMaster_ID").text();
	}
	//alert("RxMaster Id--->"+rxMasterID);
	if(cuSOID!=null && typeof(cuSOID) != "undefined"){
		
		
	$.ajax({
		url: "./salesOrderController/getPreLoadData",
		type: "POST",
		async:false,
		data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+"&jobNumber="+jobNumber+"&joMasterID="+joMasterID,
		success: function(data) {
			
			//document.getElementById("sogeneralForm").reset();
			//$("#sogeneralForm")[0].reset();
			resetSOGeneralForm();
			$("#CustomerNameGeneral").val(data.CustomerName);
			$('#soEmailTimeStamp').empty();	$('#soEmailTimeStamp').append(data.emailTime);
			$('#soLinesEmailTimeStamp').empty();$('#soLinesEmailTimeStamp').append(data.emailTime);
			if (typeof(data.Cuso) != "undefined" && data.Cuso != null){
				divflag = "#SO_Shipto";;
				$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val("");
				 $(divflag).contents().find("#shiptomodehiddenfromdbid").val("");
				 if(data.Cuso.coTaxTerritoryId==null || data.Cuso.coTaxTerritoryId==0){
						coTaxThereorNOt=true;
					}else{
						coTaxThereorNOt=false;
					}
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
					preloadShiptoAddress(divflag,data.Cuso.cuSoid,null,'2','0',$("#jobCustomerName_ID").text(),"");
					$(divflag).contents().find("#shiptomoderhiddenid").val('2');
					soGeneralformChanges();
				}
				
				
				var joReleaseIds = data.Cuso.joReleaseId;
				$("#joReleaseID").val(joReleaseIds);
				if(joReleaseIds===''||joReleaseIds===null){
					$("#CustomerNameGeneral").removeAttr("readonly");
				    $("#CustomerNameGeneral").css("background-color" , "#FFFFFF");
				    $('#splitDivs').hide();
					$("#splitDivs").children().attr("disabled","disabled");
					$("#OriginalData").children().removeAttr("disabled");
					$('#OriginalData').show();
					 $("#shiptoaddradio3").button({disabled:true});
					 $('#shiptoaddlabel3').attr('onclick','').unbind('click');
					 
					// alert("hi");
					
				}else{
					$("#CustomerNameGeneral").attr("readonly", "true"); 
				    $("#CustomerNameGeneral").css("background-color" , "#F0F0F0");
				    $('#splitDivs').show();
					//$("#splitDivs :input").prop("disabled", true);
					$("#splitDivs").children().removeAttr("disabled");
					$("#OriginalData").children().attr("disabled","disabled");
					$('#OriginalData').hide();
					loadSplitCommissionList(0,joReleaseIds);
					
				}
				
			$('#dateOfcustomerGeneral').val($("#poDate_ID").text());
			$('#billToCustomerNameGeneralID').val(data.Cuso.rxCustomerId);
			console.log('SO_General.js CustomerID:'+data.Cuso.rxCustomerId);
			billtoAddess();
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
			$('#SOshipViaId').val(data.Cuso.veShipViaId);
			$('#SOdivisionID').val(data.Cuso.coDivisionID);
			$("#shiptoIndexValuefortoggle").val(data.Cuso.shipToIndex);
			$('#terms').val(data.Cuso.cuTermsId);
			$('#salesmanID').val(data.SalesMan);
			$('#csrID').val(data.AE);
			$('#engineerID').val(data.Submitting);
			$('#salesManagerID').val(data.Costing);
			$('#projectManagerID').val(data.Ordering);
			$('#SOGeneral_taxvalue').val(formatCurrency(data.Cuso.taxTotal));
			$('#cuSOid').val(data.Cuso.cuSoid);
			$('#transactionStatus').val(data.Cuso.transactionStatus);
			//$('#taxIDwz').val();
			//$('#taxhiddenIDwz').val();
			
			$('#termswz').val(data.Cuso.cuTermsId);
			$('#termhiddenIDwz').val(data.Cuso.cuTermsId);
			$('#custIDwz').val(data.Cuso.customerPonumber);
			$('#tagJobIDwz').val(data.Cuso.tag);
			$('#SOdivisionIDwz').val(data.Cuso.coDivisionID);
			$('#promisedIDwz').val(data.Cuso.datePromised);
			
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
			$('#prToWarehouseId').val(data.Cuso.prToWarehouseId);
			console.log('WHID SO List:'+$('#prToWarehouseId').val());
			
			if($('#operation').val() === 'update'){
				$("#rxCustomer_ID").text(data.Cuso.rxBillToId);
				}
			if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
				
				$("#rxShipToOtherAddressID").val(data.Cuso.rxShipToAddressId);	
				//$("#shipTorxCustomer_ID").val(data.Cuso.rxCustomerId);
				$("#shipTorxCustomer_ID").val(data.Cuso.rxCustomerId);
				//alert(data.Cuso.rxShipToId);
				$("#shipToCustomerAddressID").val(data.Cuso.rxShipToId);
				
				if($("#rxCustomer_ID").text() != null)
					{
				//alert(data.Cuso.rxContactId);
				//	loadEmailList($("#rxCustomer_ID").text());
					$("#emailList option[value='" + data.Cuso.rxContactId + "']").attr("selected", true);
					}
				//	loadEmailList($("#rxCustomer_ID").text());
				
				var mode = data.Cuso.shipToMode;
				
				
				if(mode == 0){
					$("#shiptolabel1").addClass('ui-state-active');
					$("#shiptolabel2").removeClass('ui-state-active');
					$("#shiptolabel4").removeClass('ui-state-active');
					$("#shiptolabel3").removeClass('ui-state-active');
					
				//	shipToAddressCreate('Pick Up');
					console.log('Called Mode-1');
				}
				else if(mode == 1){
					$("#shiptolabel1").removeClass('ui-state-active');
					$("#shiptolabel2").addClass('ui-state-active');
					$("#shiptolabel4").removeClass('ui-state-active');
					$("#shiptolabel3").removeClass('ui-state-active');
					console.log('SO_General.js Called Mode-1');
				//	shipToAddressCreate('Customer');
				}
				else if(mode == 3){
					/*$("#shiptolabel1").removeClass('ui-state-active');
					$("#shiptolabel2").removeClass('ui-state-active');
					$("#shiptolabel4").addClass('ui-state-active ');
					$("#shiptolabel3").removeClass('ui-state-active');*/
					console.log('SO_General.js Called Mode-3');
				//	shipToAddressCreate('Other');
				}
				else if(mode == 2){
					$("#shiptolabel1").removeClass('ui-state-active');
					$("#shiptolabel2").removeClass('ui-state-active');
					$("#shiptolabel4").removeClass('ui-state-active');
					$("#shiptolabel3").addClass('ui-state-active');
					console.log('SO_General.js Called Mode-2');
				//	shipToAddressCreate('Job Site');
				}
				
				
				$('#SOGeneral_taxId').val((data.Cuso.taxRate));
			//	loadTaxTerritoryRate(data.Cuso.coTaxTerritoryId,"");
				
			}
			if($('#operation').val()=='edit'){
				$('#operation').val('update');
			}
//			if($('#operation').val() !== 'create' && $('#operation').val() !== 'edit'){
//				addressToShip();
//				}
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
				//$('#SOGeneral_frightID').val(formatCurrency(0));
				
			}
				formattax(data.Cuso.freight);
			}
			setTimeout(function(){
				$("#so_taxfreight").val(data.Cuso.taxfreight);
				setTaxTotal_SO();
				if($('#promisedID').val()!=""){
					console.log(data.Cuso.prFromWarehouseId);
					$('#whrhouseID').val(data.Cuso.prFromWarehouseId);
				}else{
					console.log(data.defaultWHID);
					$('#whrhouseID').val(data.defaultWHID);
				}
				
				var new_so_generalform_values=generaltabformvalidation();
			    so_general_form =  JSON.stringify(new_so_generalform_values);
			    $('#loadingDivForSOGeneralTab').css({
					"display": "none"
				});
			},3000);
			
		}
	});
  }
}


function resetSOGeneralForm(){
	$('#CustomerNameGeneral').val('');
	$('#billToCustomerNameGeneralID').val('');
	$('#transactionStatus').val('');
	$('#cuSOid').val('');
	$('#dateOfcustomerGeneral').val('');
	$('#SOnumberGeneral').val('');
	$('#SOlocationbillToAddressname').val('');
	$('#SOGenerallocationbillToAddressID1').val('');
	$('#SOGenerallocationbillToAddressID2').val('');
	$('#SOGenerallocationbillToCity').val('');
	$('#SOGenerallocationbillToState').val('');
	$('#SOGenerallocationbillToZipID').val('');
	$('#emailList').val('0');
	$('#addressID').val('');
	$('#customerShipToOtherID').val('');
	$('#SOlocationShipToAddressID').val('');
	$('#shipTorxCustomer_ID').val('');
	$('#shipToCustomerAddressID').val('');
	$('#rxShipToOtherAddressID').val('');
	$('#SOlocationShipToAddressID1').val('');
	$('#SOlocationShipToAddressID2').val('');
	$('#SOlocationShipToCity').val('');
	$('#prToWarehouseId').val('');
	$('#SOlocationShipToState').val('');
	$('#SOlocationShipToZipID').val('');
	$('#salesmanID').val('');
	$('#salesmanhiddenID').val('');
	$('#csrID').val('');
	$('#csrhiddenID').val('');
	$('#salesManagerID').val('');
	$('#salesManagerhiddenID').val('');
	$('#engineerID').val('');
	$('#engineerhiddenID').val('');
	$('#projectManagerID').val('');
	$('#projectManagerhiddenID').val('');
	$('#whrhouseID').val('0');
	$('#SOshipViaId').val('0');
	$('#poID').val('');
	$('#SOShipDate').val('');
	$('#taxID').val('');
	$('#taxhiddenID').val('');
	$('#terms').val('-1');
	$('#termhiddenID').val('');
	$('#custID').val('');
	$('#tagJobID').val('');
	$('#SOdivisionID').val('-1');
	$('#promisedID').val('');
	$('#SOGeneral_subTotalID').val('');
	$('#SOGeneral_frightID').val('');
	$('#SOGeneral_taxId').val('');
	$('#SOGeneral_taxvalue').val('');
	$('#SOGeneral_totalID').val('');
	
	if(typeof(so_lines_form)!=undefined){
		so_lines_form=undefined;
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
function chechcustomerisonhold(){
	var returnvalue=false;
	var customerid=$("#billToCustomerNameGeneralID").val();
	
	if(customerid!="")
	{
	$.ajax({
		url: 'jobtabs5/getCustomerOverallDetail',
		async:false,
		mtype : 'GET',
		//data: {'customerid':customerid},
		data: {'customerid':customerid},
		success: function (result) {
			if(result!=null){
				
			
				/*
				console.log("Hours:==>"+(24 - parseInt(crDate.getHours())))
				console.log("Min:==>"+(60 - parseInt(crDate.getMinutes())))
				console.log("Sec:==>"+(60 - parseInt(crDate.getSeconds())))
				
				var hrs = (24 - parseInt(crDate.getHours()));
				var min = (60 - parseInt(crDate.getMinutes()));
				var sec = (60 - parseInt(crDate.getSeconds()));*/
				
			
				
					if(result.creditHold){
					
						if(result.creditHoldOverride!=null)
						{
							//alert(result.currentDate);
							
							var crDate = new Date(result.creditHoldOverride);
						    var curDate = new Date(result.currentDate);
							crDate.setHours(23,59,59)
							
							if(crDate > curDate)
							{
								returnvalue= false;
							}
							else
							{
								returnvalue= true;
								
								$.ajax({
									type: "POST",
									url: "./jobtabs5/updateHoldOveriteDetails",
									data: {'customerid':customerid},
									success: function(data) {
									}
								});
							}
						}
						else
						{
						returnvalue= true;
						}
					}else{
						returnvalue= false;
					}
			}
			
		}
	});
	}
	return returnvalue;
} 

var commissionRequired =0 ;
function chkSplitCommissionValidation(){
var chkSplitCommissionYN = getSysvariableStatusBasedOnVariableName("SplitCommissionRequiredOnRelease");
if(chkSplitCommissionYN!=null && chkSplitCommissionYN[0].valueLong==1){
	$('#splitCommissionLabel').css('display','inline-block');
	commissionRequired = 1;
}
}

function saveSORelease(popupdetail){
	chkSplitCommissionValidation();
	if(globalinsideoroutsidejob){
	var Allowcreditlimitradiobutton=false;
	var newDialogDiv = jQuery(document.createElement('div'));
	var allowcheckcreditlimit=getSysvariableStatusBasedOnVariableName("CheckcreditlimitinSalesOrderOutsideofJob");
	if(allowcheckcreditlimit!=null && allowcheckcreditlimit[0].valueLong==1){
		Allowcreditlimitradiobutton=true;
	}
	var check=chechcustomerisonhold();
	if(Allowcreditlimitradiobutton&&check){
		jQuery(newDialogDiv).html('<span><b>Customer is on Hold.You must take off Hold in order to proceed.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
								buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	}
	

	
	$("#salesreleasetab").tabs({
	       disabled : false
    });
	
	boolean = false;
	 
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
	var answer=document.getElementById("whrhouseID");
	//alert($("#whrhouseID option:selected").val());
	var shipViaId = $('#SOshipViaId').val();
 	var divisionID = $('#SOdivisionID').val();
 	var promisedID = $('#promisedID').val();
 	var trackingNumber = $('#poID').val();
	trackingNumber= trackingNumber.replace('&', '<apm>');
 	trackingNumber= trackingNumber.replace('#', '<hah>');
 	trackingNumber= trackingNumber.replace('+', '<psl>');
 	trackingNumber= trackingNumber.replace('%', '<pcn>');
 	
 	var taxTerritoryID = $('#taxhiddenID').val();
 	var terms = $('#terms').val();
 	var custID =$('#custID').val();
 	var frieght = $('#SOGeneral_frightID').val(); 
 	frieght= parseFloat(frieght.replace(/[^0-9-.]/g, ''));
 	if(isNaN(frieght)){
 		frieght=0.00;
 	}
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
 	var shipTorxCustomer_ID = 0;//$('#shipTorxCustomer_ID').val();
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
 	var divthereornot=$("#OriginalData").is(":visible");
 	if(divthereornot){
 		taxTerritoryID = $('#taxhiddenIDwz').val(); 
 		terms = $('#termswz').val();
 		custID =$('#custIDwz').val();
 		divisionID = $('#SOdivisionIDwz').val();
 		promisedID = $('#promisedIDwz').val();
 		tag = $('#tagJobIDwz').val();
 	}
 	
 	// Added by Leo Date: 08/03/2016 ID#505
 	var withPriceStatus = 0;
 	if($("#withPrice").is(":checked"))
 		withPriceStatus=1;
 	
 //	alert(withPriceStatus);
 	
//	if(ShipDate === null || ShipDate === ''){
//		var validateMsg = "Please&nbsp;Provide&nbsp;Shiping&nbsp;Date."
//		 	/*jQuery(newValidateDiv).attr("id","ShipDate");
//			jQuery(newValidateDiv).html('<span><b style="color:Green;">'+validateMsg+'</b></span>');
//		
//		jQuery(newValidateDiv).dialog({modal: true, width:300, height:150, title:"Information",
//			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");
//	*/	
//		$('#soShippingDate').html(validateMsg);
//		setTimeout(function(){
//		$('#soShippingDate').html("");						
//		},3000);
//		return false;		
//	
//	}
	
	
	
 	var chkCusReqDivSal = false;
	var chk_cusReqDivinSalOrdYes = getSysvariableStatusBasedOnVariableName("RequireDivisioninSalesOrderOutsideofJob");
	if(globalinsideoroutsidejob==true && chk_cusReqDivinSalOrdYes!=null && chk_cusReqDivinSalOrdYes[0].valueLong==1)
	{
		var SOdivisionID = $("#SOdivisionID").val();
		if(divthereornot){
			SOdivisionID = $("#SOdivisionIDwz").val();
		}
		if(SOdivisionID > -1)
			chkCusReqDivSal = true;
	}
	else{
		chkCusReqDivSal = true;
	}
	
	if(!chkCusReqDivSal)
 	{
		var validateMsg = "Please&nbsp;select&nbsp;division&nbsp;value."
 			$('#soDivisionId').html(validateMsg);
			$('#soDivisionIdwz').html(validateMsg);
		   $('#divisionlabel').css('display','inline-block');
		   $('#divisionlabelwz').css('display','inline-block');
		   
 			setTimeout(function(){
 				$('#soDivisionId').html("");
 				$('#soDivisionIdwz').html("");
 			},3000);
 			return false;
 	}
 	
	if(promisedID === null || promisedID === ''){
		var validateMsg = "Please&nbsp;Provide&nbsp;Promised&nbsp;Date."
			$('#soPromisedId').html(validateMsg);
			$('#soPromisedIdwz').html(validateMsg);
			setTimeout(function(){
			$('#soPromisedId').html("");
			$('#soPromisedIdwz').html("");
			},3000);
		 	/*jQuery(newValidateDiv).attr("id","promisedID");
			jQuery(newValidateDiv).html('<span><b style="color:Green;">'+validateMsg+'</b></span>');
		
		jQuery(newValidateDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");
		*/
		return false;		
	
	}
	
	
//	if(chk_cusReqDivinSalOrdYes == 0 || chk_cusReqDivinSalOrdYes == '' || chk_cusReqDivinSalOrdYes == undefined){
//		var validateMsg = "Please&nbsp;select&nbsp;division&nbsp;value."
//		$('#soPromisedId').html(validateMsg);
//		setTimeout(function(){
//			$('#soPromisedId').html("");						
//		},3000);
//		return false;	
//	}
	
	// alert("Commission Grids::"+gridDataToSend);
	
	 var rows = jQuery("#SoSplitCommissionGrid").getDataIDs();
	 deleteCommissionSplitJobID=new Array();
	 var validatesplit=validateCommissionTotalsSavePoRelease();
	 if(validatesplit===true){
		 for(var a=0;a<rows.length;a++)
		 {
		    row=jQuery("#SoSplitCommissionGrid").getRowData(rows[a]);
		   var id="#canDeleteID_"+rows[a];
		   var canDelete=$(id).is(':checked');
		   if(canDelete){
			  var ecSplitJobId=row['ecSplitJobId'];
			  if(ecSplitJobId!=undefined && ecSplitJobId!=null && ecSplitJobId!="" && ecSplitJobId!=0){
			 		deleteCommissionSplitJobID.push(ecSplitJobId);
			 	}
			 $('#SoSplitCommissionGrid').jqGrid('delRowData',rows[a]);
		  }
		 }
	 }
	 
	 var gridRows = $('#SoSplitCommissionGrid').getRowData();
	 var gridDataToSend = JSON.stringify(gridRows);
	 
	var therxcontactid=0;
	if($("#emailList option:selected").val()!=null && $("#emailList option:selected").val()!=undefined && $("#emailList option:selected").val()!=""){
		therxcontactid=$("#emailList option:selected").val();
	}
	var shiptoIndexValuefortoggle=$("#shiptoIndexValuefortoggle").val();
	var shiptoCuAddressIDfortoggle=$("#shiptoCuAddressIDfortoggle").val();
	if(shiptoIndexValuefortoggle==undefined || shiptoIndexValuefortoggle==null || shiptoIndexValuefortoggle==""){
		shiptoIndexValuefortoggle=0;
	}
	if(shiptoCuAddressIDfortoggle==undefined || shiptoCuAddressIDfortoggle==null || shiptoCuAddressIDfortoggle==""){
		shiptoCuAddressIDfortoggle=0;
	}
	
	var so_taxfreight=$("#so_taxfreight").val();
	divflag = "#SO_Shipto";
	var SoGeneralValues =  "";
	//alert($("#shiptoCuAddressIDfortoggle").val());
		SoGeneralValues = 'cuSoid='+cuSoid+'&createdOn='+dateofSO+'&cuAssignmentId0='+SalesManId+'&cuAssignmentId1='+csrhiddenID+'&cuAssignmentId2='+salesManagerhiddenID+'&cuAssignmentId3='+engineerhiddenID+'&cuAssignmentId4='+projectManagerhiddenID+'&prFromWarehouseId='+whrhouseID+'&veShipViaId='
 							+shipViaId+ '&coDivisionID='+divisionID+'&datePromised='+promisedID+'&trackingNumber='+trackingNumber+'&freight='+frieght+'&customerPonumber='+encodeURIComponent(custID)+'&cuTermsId='+terms+'&coTaxTerritoryId='+taxTerritoryID+'&joScheddDetailID=0'
 							+'&subTotal='+subTotal+'&taxRate='+taxrate+'&taxTotal='+taxTotal+'&costTotal='+total+'&billToCustomerNameGeneralID='+billToCustomerNameGeneralID+'&shipTorxCustomer_ID='+shipTorxCustomer_ID+'&operation='+operation+'&customerShipToOtherID=0'
 							+'&shipToName='+encodeURIComponent($(divflag).contents().find("#shipToName").val())+'&shipToAddress1='+encodeURIComponent($(divflag).contents().find("#shipToAddress1").val())+'&shipToAddress2='+	encodeURIComponent($(divflag).contents().find("#shipToAddress2").val())+'&shipToCity='+encodeURIComponent($(divflag).contents().find("#shipToCity").val())+'&shipToState='+encodeURIComponent($(divflag).contents().find("#shipToState").val())+'&shipToZip='+encodeURIComponent($(divflag).contents().find("#shipToZip").val())
 							+'&rxShipToOtherAddressID=0'
 							+'&rxContactID='+therxcontactid
 							+'&prToWarehouseId=0'
							+'&shiptoIndexValuefortoggle='+shiptoIndexValuefortoggle
							+'&shiptoCuAddressIDfortoggle='+shiptoCuAddressIDfortoggle
							+'&rxShiptoid='+ $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val()+'&rxShiptomodevalue='+ $(divflag).contents().find("#shiptomoderhiddenid").val()
							+'&withpriceStatus='+withPriceStatus
							+'&sotaxfreight='+so_taxfreight;
	if(ShipDate != null && ShipDate != "")
		SoGeneralValues += '&ShipDate='+ShipDate;
 	
 	console.log("rxShipToOtherAddressID=="+$("#rxShipToOtherAddressID").val()+"==shipTorxCustomer_ID=="+shipTorxCustomer_ID);
 	console.log("SoGeneralValues----->"+SoGeneralValues);
 	var chkUserCustomerCreditYN = $("#chkUserCustomerCreditYN").val();
 	var CreditHold = $("#CreditHold").val();
 	
	 	if(chkUserCustomerCreditYN == 1 && CreditHold == "true")
	 	{
	 		$('#sogeneralAlertDiv').dialog("open");
	 	}
	 	else
	 	{
	 		 if(validatesplit || cuSoid==0){
		 	$.ajax({
				url: "./salesOrderController/addSoRelease?"+SoGeneralValues,
				type: "POST",
				async:false,
				data : {"tag":tag,"commissionSplitGridData":gridDataToSend,'commissionSplitdelData':deleteCommissionSplitJobID},
				success: function(data) {
					var CuSOId = data.cuSoid;
					var customerid = data.rxCustomerId;
					$('#rxCustomer_ID').text(customerid);
					$('#rxCustomer_ID').val(customerid);
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
					
					if( $('input:text[name=jobHeader_JobNumber_name]').val()!='undefiend')
					createtpusage('job-Release Tab','SO Save','Info','Job,Release Tab,Saving SO,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()+',SO ID:'+CuSOId);
					else
					createtpusage('Company-Customer-Sales Order','SO Save','Info','Company-Customer-Sales Order,Saving SO,SO ID:'+CuSOId)
					
						
					$('#Cuso_ID').text(CuSOId);
					var errorText = "Sales Order General Tab details are Saved.";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update' ){
						if("Save" == $('#setButtonValue').val())
						{
							/*jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");*/	
							$('#showMessageSO').html("Saved");
							//$('#ShowInfo').html("Saved");
							
							
							setTimeout(function(){
								$('#showMessageSO').html("");						
								},3000);
						}
						/*else
						{
							if("SaveandClose" == $('#setButtonValue').val())
							{	
								if(typeof(inventory) !== 'undefined' && inventory === 'InventoryPage')
									{
									$('#salesrelease').dialog("close");		
									$("#inventoryDetailsGrid").trigger("reloadGrid");
									loadInventoryDetailsGrid();
									}
								else
									{
									if($("#release").innerHTML==undefined||$("#inventoryDetailsGrid").innerHTML=="undefined"){
										$('#salesrelease').dialog("close");	
										$("#release").trigger("reloadGrid");
									}else{
										location.href = "./salesorder?oper=create";	
									}
									
								 }
							}
						}*/
					
					}
					/*else if($('#operation').val() == undefined){
						if("Save" == $('#setButtonValue').val())
						{
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");	
							$('#showMessageSO').html("Saved");
							//$('#ShowInfo').html("Saved");
							
							
							setTimeout(function(){
								$('#showMessageSO').html("");						
								},3000);
						}
						else
						{
							$('#salesrelease').dialog("close");							
							}
						
						
					}*//*else{
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");$('#salesrelease').dialog("close");}}]}).dialog("open");
					}*/
					//alert($("#joMasterID").val()+" ++++ "+$("#joReleaseID").val());
					loadSplitCommissionList($("#joMasterID").val(),$("#joReleaseID").val());
					console.log("going to call preloaddata : "+CuSOId);
					PreloadSOGeneralData(CuSOId);
					invoicethereornotforsalesorder(CuSOId);
					if(popupdetail=="close"){
						 $("#salesrelease").dialog("close");
						 $( "#salesreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
					 }else{
						 $('#showMessageSO').html("Saved");
							setTimeout(function(){
								$('#showMessageSO').html("");						
								},3000);
					 }
					console.log("after calling preloaddata : "+CuSOId);
					$( "#salesreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
					setTimeout(function(){
						var new_so_generalform_values=generaltabformvalidation();
					    so_general_form =  JSON.stringify(new_so_generalform_values);
						},1000);
					return true;
				}
		 	});
	 		 }
	 		}
 	
 	
 		
 	
	

}
 	

function addressToShip(){
//	$('#SOforWardId').hide();
//	$('#SObackWardId').hide();
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
					$('#shiptoradio3').attr('checked','checked');
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
		$('#customerShipToOtherID').val("Job site");
	}
	else
	{
		
		$('#customerShipToOtherID').val("Job site");
		$.ajax({
			url: "./salesOrderController/getJobsiteAddress",
			type: "GET",
			data : {"jobnumber" : jobnumber,"cuSOID": $('#Cuso_ID').text()},
			success: function(data) {
				$('#SOlocationbillToAddressname').val($('#CustomerNameGeneral').val());
				$('#SOlocationShipToAddressID1').val(data.locationAddress1);
				$('#SOlocationShipToAddressID2').val(data.locationAddress2);
				$('#SOlocationShipToCity').val(data.locationCity);
				$('#SOlocationShipToState').val(data.locationState);
				$('#SOlocationShipToZipID').val(data.locationZip);
				$('#SOshiptoradio4').attr('checked','checked');
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
	
	
//	alert("hello leo")
	
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
			 $("#shiptoradio2").attr("Checked","Checked");
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
		url: "./salesOrderController/getCustomerShipToAddressforSO",
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
				$('#shiptoCuAddressIDfortoggle').val(data.rxAddressId);
				$("#shipToCustomerAddressID").val(data.rxAddressId)
				
				console.log(data.rxAddressId+"=========>")
				
				if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
					loadTaxTerritoryRate(data.coTaxTerritoryId,"Customer");
				}
			
			$("#shiptoradio2").attr("Checked","Checked");
			$('#shiptolabel1').removeClass('ui-state-active');
			$('#shiptolabel2').addClass('ui-state-active');
			$('#shiptolabel3').removeClass('ui-state-active');
			$('#shiptolabel4').removeClass('ui-state-active');
			$('#addressID').val(2);
		}
});
}

	function ShipToJobSite(){

		$('#regularTable').show();
		$('#SOforWardId').hide();
		$('#SObackWardId').hide();
		$('#hiddenTable').hide();
		var rxMasterId = null;
		var jobnumber;
		jobnumber = $("#jobNumber_ID").text();
		if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
			$('#customerShipToOtherID').val("Job Site");
			$('#SOlocationShipToAddressID').prop("disabled",true);
			$('#SOlocationShipToAddressID').focus();
			rxMasterId = $("#shipToCustomerAddressID").val();
		}
		else
		{
			rxMasterId = $("#cuSO_ID").text();
		}
		
		
		$.ajax({
			url: "./salesOrderController/getJobsiteAddress",
			type: "GET",
			data : {"jobnumber" : jobnumber,"cuSOID": $('#Cuso_ID').text()},
			success: function(data) {
				
				
				if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
					
					$('#SOlocationShipToAddressID').val($('#CustomerNameGeneral').val());
				}
				else
				{
					$('#SOlocationShipToAddressID').val($('#CustomerNameGeneral').val());
				}
				$('#SOlocationShipToAddressID1').val(data.locationAddress1);
				$('#SOlocationShipToAddressID2').val(data.locationAddress2);
				$('#SOlocationShipToCity').val(data.locationCity);
				$('#SOlocationShipToState').val(data.locationState);
				$('#SOlocationShipToZipID').val(data.locationZip);
				
				if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
					loadTaxTerritoryRate(data.coTaxTerritoryId,"Job Site");
				}
				$('#addressID').val(2);
			}
	});
		$("#SOshiptoradio3").attr("Checked","Checked");
		$('#SOshiptolabel3').addClass('ui-state-active');
		$('#SOshiptolabel1').removeClass('ui-state-active');
		$('#SOshiptolabel2').removeClass('ui-state-active');
		$('#SOshiptolabel4').removeClass('ui-state-active');
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
	
	$("#SOshiptoradio4").attr("Checked","Checked");
	$('#SOshiptolabel4').addClass('ui-state-active');
	$('#SOshiptolabel1').removeClass('ui-state-active');
	$('#SOshiptolabel2').removeClass('ui-state-active');
	$('#SOshiptolabel3').removeClass('ui-state-active');
}
function formattax(frieght){
	var subTotal = $('#SOGeneral_subTotalID').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var tax = $('#SOGeneral_taxvalue').val();
	tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
	var total = subTotal+tax+frieght;
	if(isNaN(total)){
		total=0.00;
	}
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
	if(createdDate != null)
	{
		var date = new Date(createdDate);
		var CreatedOn = date.getDate();
		var createdMonth = date.getMonth()+1; 
		var createdYear = date.getFullYear();
		if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
		if(createdMonth<10)
			createdMonth='0'+createdMonth;
		createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;	
		$("#SOShipDate").val(createdDate);
	}
	else
		$("#SOShipDate").val("");
}
function sendPOEmail(poGeneralKey){
	
	console.log('Email form---- SO_General.js---SO_General.js---->'+$('#emailListCU').text()+'  :::  poGeneralKey : '+poGeneralKey);
	if($('#POtransactionStatus').val() === '-1'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not send E-mail, \nTransaction Status is 'Void'\nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	return false;
	}
	else if($('#POtransactionStatus').val() === '0'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not send E-mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									$("#cData").tigger('click');}}]}).dialog("open");
			return false;
	}
	else if($('#POtransactionStatus').val() === '2'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not send E-mail, \nTransaction Status is 'Close' \nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									$("#cData").tigger('click');}}]}).dialog("open");
			return false;
	}
	else{
	try{
		
	if(poGeneralKey == "SOJ")	
		{
		
	var rxMasterID = $('#rxCustomer_ID').text();
	var newDialogDiv = jQuery(document.createElement('div'));
	var bidderGrid = $("#salesrelease");
	var aQuotePDF = "purchase";
	if(rxMasterID === null || rxMasterID.length<= 0){
		errorText = "Please Save Sales Order to Email Sales Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	//alert('aContactID::'+rxMasterID);
	var cusotmerPONumber = $("#SOnumberGeneral").val();	
	
	var cuSOid ="";
	
	if($("#cuSOid").val()!="")
	cuSOid = $("#cuSOid").val();
	else
	cuSOid = $('#Cuso_ID').text();	
	
	
	console.log('CuSoId: '+cuSOid);
		$.ajax({ 
			url: "./vendorscontroller/GetContactDetailsFromCuso",
			mType: "GET",
			data : { 'rxMasterID' : rxMasterID},
			success: function(data){
				var aFirstname = data.firstName; 
				var aLastname = data.lastName;
				var aEmail;
				if('CuInvoice' === poGeneralKey)
					{
					if($('#emailListCU').text() != null)
						{
						aEmail=$("#emailListCU option:selected").text();
						arxContactid = $("#emailListCU option:selected").val();
						}
					else
						{
						aEmail= data.email;
						arxContactid = data.rxContactId;
						}
					}
				else
					{
					if($('#emailList').text() != null)
						{
						aEmail=$("#emailList option:selected").text();
						arxContactid = $("#emailList option:selected").val();
						}
					else
					{
						aEmail= data.email;
						arxContactid = data.rxContactId;
					}
					}
				
				//alert(arxContactid);
				
				callEmailPopup(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, cuSOid);
				
				/*var arxContact = aFirstname + ' '+aLastname;
			//alert(aEmail+arxContact);
				var arxContact = aFirstname + ' '+aLastname;
				if(aEmail === ''){
					errorText = "Are you sure you want to send this PO?";
				}else{
					errorText = "Are you sure you want to send this PO to ("+ aEmail +")?";
				}
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
				buttons:{
					"Send": function(){
						$('#loadingPODiv').css({"visibility": "visible"});
						
					
						
						jQuery(this).dialog("close");
					},
					Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");*/
			}
		});
		}
	
	else
		{
		
		
		var CuInvoice = $('#cuinvoiceIDhidden').val();
		if(CuInvoice == '' || CuInvoice == undefined)
			{
			$('#showMessageCuInvoice').css("margin-left", "300%");
			$('#showMessageCuInvoiceLine').css("margin-left", "300%");
			
			$('#showMessageCuInvoice').html("Please Save Customer Invoice.");
			$('#showMessageCuInvoiceLine').html("Please Save Customer Invoice.");
			setTimeout(function(){
				$('#showMessageCuInvoice').html("");
				$('#showMessageCuInvoiceLine').html("");
				},3000);
			return false;
			
			}
		var newDialogDiv = jQuery(document.createElement('div'));
	var bidderGrid = $("#salesrelease");
	var aQuotePDF = "purchase";
	
	var rxMasterID = $('#rxCustomerID').val();
	
	//alert('aContactID::'+rxMasterID)
	var cusotmerPONumber = $("#customerInvoice_invoiceNumberId").val();	
	//alert("rxMasterID--->"+rxMasterID+"--->InvoiceNumber--"+cusotmerPONumber);
	
		$.ajax({ 
			url: "./vendorscontroller/GetContactDetailsFromCuso",
			mType: "GET",
			data : { 'rxMasterID' : rxMasterID},
			success: function(data){
				var aFirstname = data.firstName; 
				var aLastname = data.lastName;
				var aEmail;
				if($('#emailListCU').text() != null){
					aEmail=$("#emailListCU option:selected").text();
				}
				else{
					aEmail= data.email;
				}
				
				
				var arxContactid='';
				
				try{
					arxContactid = data.rxContactId;
					//alert("Ajax Success-->"+aFirstname+"--"+aLastname+"--"+aEmail+"--"+arxContactid);
					
				}catch(err){//alert(err.message);
					
				}
				var arxContact = aFirstname + ' '+aLastname;
			//alert(aEmail+arxContact);
				var arxContact = aFirstname + ' '+aLastname;
				
				
				callEmailPopupfromInvoice(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, CuInvoice);
				
				
				
			/*	if(aEmail === ''){
					errorText = "Are you sure you want to send this PO to"+ arxContact +"?";
				}else{
					errorText = "Are you sure you want to send this PO to"+ arxContact +"("+ aEmail +")?";
				}
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
				buttons:{
					"Send": function(){
						$('#loadingPODiv').css({"visibility": "visible"});
						saveCuInvoicePDF(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, '');
						jQuery(this).dialog("close");
					},
					Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");*/
			}
		});
		
		
		
		}
		
		
		
	}catch(err){
		//alert(err.message);
		}
	return true;  
	}
}

function clearemailattachmentForm(){
	$("#efromaddr").val("");
	$("#etoaddr").val("");
	$("#eccaddr").val("");
	$("#esubj").val("");
	$("#econt").val("");
	$(".nicEdit-main").html("");
	$("#filelabelname").text("");
	$("#attachmentfilename").val("");

}



function callEmailPopupfromInvoice(arxContactid,aEmail, poGeneralKey, cusotmerPONumber1, cuInvoiceId){
clearemailattachmentForm();

	var cusotmerPONumber = cusotmerPONumber1;
	var vePoId = cuSOid;
	var aContactID = arxContactid;
	var toemailaddress="";
	var fullname="";
	$.ajax({ 
		url: "./vendorscontroller/GetContactDetails",
		mType: "GET",
		async:false,
		data : { 'rxContactID' : aContactID},
		success: function(data){
			var aFirstname = data.firstName;
			var aLastname = data.lastName;
			toemailaddress = data.email;
			if(aEmail==null){
				$("#etoaddr").val(toemailaddress);
			}else{
				$("#etoaddr").val(aEmail);
			}
		    fullname = aFirstname + ' '+aLastname;
		   // $('#loadingPODiv').css({"visibility": "visible"});
	
		}
	});
	$.ajax({ 
		url: "./vendorscontroller/GetFromAddressContactDetails",
		mType: "GET",
		async:false,
		data : { },
		success: function(data){
				$("#efromaddr").val(data.emailAddr);
				var ccaddr1=data.ccaddr1;
				var ccaddr2=data.ccaddr2;
				var ccaddr3=data.ccaddr3;
				var ccaddr4=data.ccaddr4;
		var ccaddress="";
		if(ccaddr1!=null && ccaddr1!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr1;
			}else{
				 ccaddress=ccaddress+","+ccaddr1;
			}
		}
		if(ccaddr2!=null && ccaddr2!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr2;
			}else{
				 ccaddress=ccaddress+","+ccaddr2;
			}
		}
		if(ccaddr3!=null && ccaddr3!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr3;
			}else{
				 ccaddress=ccaddress+","+ccaddr3;
			}
		}
		if(ccaddr4!=null && ccaddr4!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr4;
			}else{
				 ccaddress=ccaddress+","+ccaddr4;
			}
		}
		$("#eccaddr").val(ccaddress);
		$("#esubj").val("CustomerInvoice # "+cusotmerPONumber);
		$("#filelabelname").text("CustomerInvoice_"+cusotmerPONumber+".pdf");
		
		//alert(withPrice);
		
		$('#emailpopup').data('arxContactid', arxContactid);
		$('#emailpopup').data('aEmail', aEmail);
		$('#emailpopup').data('poGeneralKey', poGeneralKey);
		$('#emailpopup').data('cusotmerPONumber', cusotmerPONumber);
		$('#emailpopup').data('cuInvoiceId', cuInvoiceId);
		$('#emailpopup').data('type', "DialogCIInside");
		$("#emailpopup" ).dialog("open");
		}
	});
}




function callEmailPopup(arxContactid,aEmail, poGeneralKey, cusotmerPONumber1, cuSOid){
clearemailattachmentForm();
	
	var cusotmerPONumber = cusotmerPONumber1;
	var vePoId = cuSOid;
	var aContactID = arxContactid;
	var toemailaddress="";
	var fullname="";
	$.ajax({ 
		url: "./vendorscontroller/GetContactDetails",
		mType: "GET",
		async:false,
		data : { 'rxContactID' : aContactID},
		success: function(data){
			var aFirstname = data.firstName;
			var aLastname = data.lastName;
			//toemailaddress = data.email;
			//$("#etoaddr").val(toemailaddress);
		    fullname = aFirstname + ' '+aLastname;
		   // $('#loadingPODiv').css({"visibility": "visible"});
	
		}
	});
	var WareHouseID=$("#whrhouseID").val();
	if(WareHouseID==undefined ||WareHouseID==null || WareHouseID=="" ){
		WareHouseID=-1;
	}
	$.ajax({ 
		url: "./vendorscontroller/GetTOEmailWarehouseAdd",
		mType: "GET",
		async:false,
		data : { 'WareHouseID' : WareHouseID,'cuSOID':cuSOid},
		success: function(data){
			toemailaddress = data.email;
			$("#etoaddr").val(toemailaddress);
		}
	});
	
	$.ajax({ 
		url: "./vendorscontroller/GetFromAddressContactDetails",
		mType: "GET",
		async:false,
		data : { },
		success: function(data){
				$("#efromaddr").val(data.emailAddr);
				var ccaddr1=data.ccaddr1;
				var ccaddr2=data.ccaddr2;
				var ccaddr3=data.ccaddr3;
				var ccaddr4=data.ccaddr4;
		var ccaddress="";
		if(ccaddr1!=null && ccaddr1!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr1;
			}else{
				 ccaddress=ccaddress+","+ccaddr1;
			}
		}
		if(ccaddr2!=null && ccaddr2!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr2;
			}else{
				 ccaddress=ccaddress+","+ccaddr2;
			}
		}
		if(ccaddr3!=null && ccaddr3!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr3;
			}else{
				 ccaddress=ccaddress+","+ccaddr3;
			}
		}
		if(ccaddr4!=null && ccaddr4!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr4;
			}else{
				 ccaddress=ccaddress+","+ccaddr4;
			}
		}
		
		
		$("#eccaddr").val(ccaddress);
		var subj="Sales Order";
		if($("#transactionStatus").val()==-2 && $("#withPriceLine").is(":checked")){
			subj="Quotation";
		}
		$("#esubj").val(subj+" # "+cusotmerPONumber);
		var filenamee="SalesOrder";
		if($("#transactionStatus").val()==-2 && $("#withPriceLine").is(":checked")){
			filenamee="Quotation";
		}
		$("#filelabelname").text(filenamee+"_"+cusotmerPONumber+".pdf");
		
		//alert(withPrice);
		
		$('#emailpopup').data('withprice', withPrice);
		$('#emailpopup').data('arxContactid', arxContactid);
		$('#emailpopup').data('aEmail', aEmail);
		$('#emailpopup').data('poGeneralKey', poGeneralKey);
		$('#emailpopup').data('cusotmerPONumber', cusotmerPONumber);
		$('#emailpopup').data('cuSOid', cuSOid);
		$('#emailpopup').data('type', "DialogSO");
		$("#emailpopup" ).dialog("open");
		}
	});
}


/*$(function(){
	$("#emailpopup" ).dialog({
		autoOpen: false,
		height: 570,
		width: 600,
		modal: true,
		buttons: {
					
			Send:function(){  
				 var $led = $("#emailpopup");
				 
				alert("hifff"+$led.data('cuSOid'));

				if (typeof $led.data('cuSOid') === "undefined" || )
					{
					sendsubmitMailFunction();
					}
				else
					{
					viewPOPDFSave($led.data('arxContactid'),$led.data('aEmail'), $led.data('poGeneralKey'),$led.data('cusotmerPONumber') ,$led.data('cuSOid'),$led.data('withprice'));
					}
	   		 },	
		
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},	
	});
	})
*/





function sendsubmitMailFunction1(arxContactid,aEmail, poGeneralKey, cusotmerPONumber1, numberof){
	
	
	//alert(arxContactid+"-"+aEmail+"-"+poGeneralKey+"-"+cusotmerPONumber+"-"+numberof)
	
		var cuSOId = numberof;
		var cusotmerPONumber = cusotmerPONumber1;
		var aContactID = arxContactid;
		var toemailAddress=$("#etoaddr").val();
		var ccaddress=$("#eccaddr").val();
		var subject=$("#esubj").val();
		var filename=$("#filelabelname").text();
		var content=$("#econt").val();
		//content=$('.nicEdit-main').html();
		//content=nicEditors.findEditor('econt').getContent();
		var content=$('#emailform').find('.nicEdit-main').html();
		var newDialogDiv = jQuery(document.createElement('div'));
		$.ajax({ 
			url: "./sendMailServer/sendSalesOrderMail",
			mType: "POST",
			data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber,
					'toAddress':toemailAddress, 'subject':subject,
					'filename':filename,'ccaddress':ccaddress,
					'content':content
				},
			success: function(data){
				//$('#loadingPOGenDiv').css({"visibility": "hidden"});
				//$('#loadingPODiv').css({"visibility": "hidden"});
				var errorText = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
				if(data){
					errorText = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
					}
				jQuery(newDialogDiv).html('<span>'+errorText+'</span>');
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
							url: "./jobtabs3/updateSOEmailTimeStamp",
							mType: "GET",
							data : { 'coSoID' : cuSOId, 'purcheaseDate' : today},
							success: function(data){ 
								$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
								$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); 
								},error:function(e){
									 $('#loadingDivForPO').css({
											"visibility" : "hidden","display": "none"
										}); 
									}
						});
						$(this).dialog("close"); 
						$("#emailpopup" ).dialog("close");
						 //$('#loadingDiv').css({	"visibility" : "hidden"}); 
						 $('#loadingDivForPO').css({
								"visibility" : "hidden","display": "none"
							}); 
					}}] }).dialog("open");
			}
		});	
		
	}

function sentMailPOFunction(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId){
	console.log("sentMailPOFunction from so_general.js"+vePoId);
	console.log("PO or SO ID for Send Mail: "+vePoId);
	var newDialogDiv = jQuery(document.createElement('div'));
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
					$('#loadingPODiv').css({"visibility": "visible"});
					$.ajax({ 
						url: "./sendMailServer/sendSOMail",
						mType: "GET",
						data : {'contactID' : aContactID, 'SONumber' : cusotmerPONumber},
						success: function(data){
						
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
								console.log("timestamp="+today);
								if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
								if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
								$.ajax({ 
									url: "./jobtabs3/updateEmailStampValue",
									mType: "GET",
									data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
									success: function(data){
										$("#soEmailTimeStamp").empty(); $("#soEmailTimeStamp").append(today);
										$("#soLinesEmailTimeStamp").empty(); $("#soLinesEmailTimeStamp").append(today); 
										
									}
								});
								$(this).dialog("close"); 
							}}] }).dialog("open");
						}
					});
					jQuery(this).dialog("close");
					return true;
				},
				Cancel: function ()	{jQuery(this).dialog("close");}
			}}).dialog("open");
	}else{
		if(poGeneralKey === 'poGeneral'){
			$('#loadingPOGenDiv').css({"visibility": "visible"});
			$.ajax({ 
				url: "./sendMailServer/sendSOMail",
				mType: "GET",
				data : {'contactID' : aContactID, 'SONumber' : cusotmerPONumber},
				success: function(data){
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
		}
		else if(poGeneralKey === 'poLineItems'){

			$('#loadingPOGenDiv').css({"visibility": "visible"});
			$.ajax({ 
				url: "./sendMailServer/sendSOMail",
				mType: "GET",
				data : {'contactID' : aContactID, 'SONumber' : cusotmerPONumber},
				success: function(data){
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
								success: function(data){ $("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); }
							});
							$(this).dialog("close"); 
						}}] }).dialog("open");
				}
			});
		
		}
		else{
			vePoId = $('#Cuso_ID').text();
			
			$.ajax({ 
				url: "./sendMailServer/sendSOMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'SONumber' : cusotmerPONumber},
				success: function(data){
					$('#loadingPODiv').css({"visibility": "hidden"});
					var errorText = "Mail Sent Successfully.";
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
						buttons: [{height:35,text: "OK",click: function() {
							var today = new Date();
							var dd = today.getDate();
							var mm = today.getMonth()+1; 
							var yyyy = today.getFullYear().toString();
							var hours = today.getHours();
							var minutes = today.getMinutes();
							var ampm = hours >= 12 ? 'PM' : 'AM';
							hours = hours % 12;
							hours = hours ? hours : 12;
							if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
							if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
							console.log(today);
							$.ajax({ 
								url: "./jobtabs3/updateSOEmailTimeStamp",
								mType: "GET",
								data : { 'coSoID' : vePoId, 'purcheaseDate' : today},
								success: function(data){ 
									$("#soEmailTimeStamp").empty(); $("#soEmailTimeStamp").append(today);
									$("#soLinesEmailTimeStamp").empty(); $("#soLinesEmailTimeStamp").append(today); 
								}
							});
							$(this).dialog("close"); 
						}}] }).dialog("open");
				}
			});
		}
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
		$('#shiptolabel4').addClass('ui-state-active');
	}
}

var aDivision =$('#warehousedefault').val();
$("#whrhouseID option[value=" + aDivision + "]").attr("selected", true);

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
	select: function( event, ui ) { var id = ui.item.id; $("#taxhiddenID").val(id); $("#SOGeneral_taxId").val(ui.item.taxValue); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });
$(function() { var cache = {}, lastXhr;
$( "#taxIDwz" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; 
	$("#taxhiddenIDwz").val(id);
	$("#SOGeneral_taxId").val(ui.item.taxValue); 
	$("#so_taxfreight").val(ui.item.taxfreight);
	setTaxTotal_SO();
	},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });


$(function() { var cache = {}, lastXhr;
$( "#termIDwz" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#termhiddenIDwz").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
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

/*$(function() { var cache = {}, lastXhr;
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
*/

function general(obj)
{
	if (obj.checked == false)
    {
		withPrice = "NotChecked";
    }
	else
		{
		withPrice = "Checked";
		}
}

function setStatusButton(){
	try{
	console.log('Transaction Status Called');
	var dataid = $('#transactionStatus').val();
	var name = '';
	if(dataid=='-1'){
		$('#soStatusButton').val('Void');
	}else if(dataid=='-2'){
		$('#soStatusButton').val('Quote');
	}else if(dataid=='0'){
		$('#soStatusButton').val('Hold');
	}else if(dataid=='1'){
		$('#soStatusButton').val('Open');
	}else if(dataid=='2'){
		$('#soStatusButton').val('Closed');
	}else if(dataid=='3'){
		$('#soStatusButton').val('Book as Job');
	}
	}catch(err){
		
		
	}
}

var lastEditRow = 0;
function loadSplitCommissionList(joMasterID,joReleaseID){
	createtpusage('Drop Ship-Release Tab','View Split-Commission','Info','Job,Release Tab,Drop Ship Split Commission ,JoReleaseId:'+$("#jobreleasehiddenId").val());
	$('#SoSplitCommissionGrid').jqGrid('GridUnload');
	$("#SoSplitCommission").empty();
	$("#SoSplitCommission").append("<table id='SoSplitCommissionGrid'></table><div id='SoSplitCommissionGridPager'></div>");
	$('#SoSplitCommissionGrid').jqGrid({
	datatype: 'JSON',
	mtype: 'POST',
	pager: jQuery('#SoSplitCommissionGridPager'),
	url:'./jobtabs3/jobCommissionListGrid',
	postData: {'JoMasterId':joMasterID,'JoReleaseId':joReleaseID,'tabpage':'JoRelease'},
	colNames:[ 'Rep','', '% Allocated', 'Split Type','','<img src="./../resources/images/delete.png" style="vertical-align: middle;">','ecSplitJobId'],
	colModel :[
				
	           	{name:'rep', index:'rep', align:'left', width:48, editable:true,hidden:false, editoptions:{size:12,
					 dataInit: function (elem) {
						
							//alert("aSelectedRowId = "+aSelectedRowId+" || prMasteId = "+$("#"+aSelectedRowId+"_prMasterId").val());
				            $(elem).autocomplete({
				                source: 'jobtabs3/getEmployeewithNameList',
				                minLength: 1,
				                select: function (event, ui) {  ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  
				                var product = ui.item.label; 
				                $("#releaserepId").val(id);
				                var selectedRowId = $("#SoSplitCommissionGrid").jqGrid('getGridParam', 'selrow');
				                $("#"+selectedRowId+"_rxMasterId").val(id);
				                
				                $.ajax({
							        url: "./jobtabs3/getEmployeeCommissionDetail",
							        data: {id:id},
							        type: 'GET',
							        success: function(data){
							        	 var aSelectedRowId = $("#SoSplitCommissionGrid").jqGrid('getGridParam', 'selrow');
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
				{name:'allocated', index:'allocated', align:'center',editable:true, width:32,hidden:false, editoptions:{size:6}},
				//{name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:40,hidden:true},
				{name:'splittype', index:'splittype', align:'',editable:true, width:70,hidden:false,  editoptions:{size:19,
					 dataInit: function (elem) {
				            $(elem).autocomplete({
				                source: 'jobtabs3/getSplitTypewithNameList',
				                minLength: 1,
				                select: function (event, ui) { ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  
				                var product = ui.item.label;
				                $("#releasesplittypeId").val(id);
				                var selectedRowId = $("#SoSplitCommissionGrid").jqGrid('getGridParam', 'selrow');
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
				 {name:'canDelete', index:'canDelete', align:'center',  width:20, hidden:false, editable:false, formatter:canDeleteCheckboxFormatter,   editrules:{edithidden:true}},
				 {name:'ecSplitJobId', index:'ecSplitJobId', align:'left',editable:true, width:25,hidden:true}],
	rowNum: 10, 
	pgbuttons: false, 
	recordtext: '', 
	rowList: [], 
	pgtext: null, 
	viewrecords: false,
	sortname: 'rep', 
	sortorder: "asc", 
	imgpath: 'themes/basic/images', 
	caption: false,
	height:120,	
	width: 425, 
	rownumbers:false, 
	altRows: true, 
	altclass:'myAltRowClass', 
	caption: '',
	cellsubmit: 'clientArray',
	editurl: 'clientArray',
	loadonce: false,
	cellEdit: false,
	jsonReader : {
		root: "rows",
		page: "page",
		total: "total",
		records: "records",
		repeatitems: false,
		cell: "cell",
		id: "ecSplitJobId",
		userdata: "userdata"
	},
	gridComplete:function(){
		checkCommissionPaidorNot(joMasterID,joReleaseID);
		var gridRows = $('#SoSplitCommissionGrid').getRowData();
	    commission_grid_form=JSON.stringify(gridRows);
		},
	loadComplete: function(data) {
		var allRowsInGrid = $('#SoSplitCommissionGrid').jqGrid('getRowData');
		var  count= $('#SoSplitCommissionGrid').getGridParam('reccount');
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
		SplitCommissionID = id;
		 var rowData = jQuery(this).getRowData(id); 
		 releaseselectedid=rowData["ecSplitJobId"];
		 releaseupdatecommsplitgrid=Number(releaseCommissionSplitsGridsum)-Number(rowData["allocated"]);
	 },
	onCellSelect : function (rowid,iCol, cellcontent, e) {
		 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
		 //console.log(e);
		},
	//editurl:"./jobtabs3/manipulateSplitCommission"
	}).navGrid("#SoSplitCommissionGridPager", {
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
	$("#SoSplitCommissionGrid").jqGrid(
			'inlineNav',
			'#SoSplitCommissionGridPager',
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
							$("#del_SoSplitCommissionGrid").addClass('ui-state-disabled');
						},
						successfunc : function(response) {
							console.log("successfunc");
							console.log(response);
							return true;
						},
						aftersavefunc : function(id) {
							console.log("aftersavefunc");
							
						 	 var ids = $("#SoSplitCommissionGrid").jqGrid('getDataIDs');
								var cuinvrowid;
								if(ids.length==1){
									cuinvrowid = 0;
								}else{
									var idd = jQuery("#SoSplitCommissionGrid tr").length;
									for(var i=0;i<ids.length;i++){
										if(idd<ids[i]){
											idd=ids[i];
										}
									}
									cuinvrowid= idd;
								}
								if(SplitCommissionID=="new_row"){
									$("#" + SplitCommissionID).attr("id", Number(cuinvrowid)+1);
								}
								var rids = $('#SoSplitCommissionGrid').jqGrid('getDataIDs');
								var nth_row_id = rids[0];
								var rids = $('#SoSplitCommissionGrid').jqGrid('getDataIDs');
								var nth_row_id = rids[0];
								validateCommissionTotals(nth_row_id);
								$("#SoSplitCommissionGrid").trigger("reload");
								var grid=$("#SoSplitCommissionGrid");
								grid.jqGrid('resetSelection');
								var dataids = grid.getDataIDs();
								for (var i=0, il=dataids.length; i < il; i++) {
								   grid.jqGrid('setSelection',dataids[i], false);
								}
						},
						errorfunc : function(rowid, response) {
							console.log('An Error');
							$("#info_dialog").css("z-index", "1234");
							$(".jqmID1").css("z-index", "1234");
							return false;
						},
						afterrestorefunc : function(rowid) {
							console.log("afterrestorefunc");
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
				        $("#del_SoSplitCommissionGrid").removeClass('ui-state-disabled');
				        var ids = $("#SoSplitCommissionGrid").jqGrid('getDataIDs');
						var cuinvrowid;
						if(ids.length==1){
							cuinvrowid = 0;
						}else{
							var idd = jQuery("#SoSplitCommissionGrid tr").length;
							for(var i=0;i<ids.length;i++){
								if(idd<ids[i]){
									idd=ids[i];
								}
							}
							cuinvrowid= idd;
						}
						if(SplitCommissionID=="new_row"){
							$("#" + SplitCommissionID).attr("id", Number(cuinvrowid)+1);
						}
						var rids = $('#SoSplitCommissionGrid').jqGrid('getDataIDs');
						var nth_row_id = rids[0];
						validateCommissionTotals(nth_row_id);
						$("#SoSplitCommissionGrid").trigger("reload");
						var grid=$("#SoSplitCommissionGrid");
						grid.jqGrid('resetSelection');
						var dataids = grid.getDataIDs();
						for (var i=0, il=dataids.length; i < il; i++) {
						   grid.jqGrid('setSelection',dataids[i], false);
						}
						console.log('afterSavefunc editparams');
					},
					errorfunc : function(rowid, response) {
						console.log(' editParams -->>>> An Error');
						$("#info_dialog").css("z-index", "1234");
						$(".jqmID1").css("z-index", "1234");
						$("#del_SoSplitCommissionGrid").removeClass('ui-state-disabled');
						$("#SoSplitCommissionGrid").trigger("reload");
						//return false;
	
					},
					afterrestorefunc : function( id ) {
						$("#del_SoSplitCommissionGrid").removeClass('ui-state-disabled');
						console.log('editParams -> afterrestorefunc');
				    },
					// oneditfunc: setFareDefaults
					oneditfunc : function(id) {
						$("#"+id+"_rep").focus();
						console.log('OnEditfunc');
						$("#del_SoSplitCommissionGrid").addClass('ui-state-disabled');
	                	/*var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
	                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
	                	alert(" >>>>>>>>>>>> "+$("#"+aSelectedRowId+"_cuSodetailId").val());
	                	 */
						}
				}
		});
}


function checkCommissionPaidorNot(joMasterID,JoReleaseID){
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
		  var idis = $("#SoSplitCommissionGrid").jqGrid('getDataIDs');
		for(var i=0;i<=idis.length;i++){
			$("#canDeleteID_"+idis[i]).prop("disabled", true);
		}
		$("#SoSplitCommissionGrid_iladd").addClass('ui-state-disabled');
		$("#SoSplitCommissionGrid_iledit").addClass('ui-state-disabled');
}
}


function validateCommissionTotals(id){
	var rows = jQuery("#SoSplitCommissionGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {
			 total = 0;
		    row=jQuery("#SoSplitCommissionGrid").getRowData(rows[a]);
		    var id="#canDeleteID_"+rows[a];
			var canDelete=$(id).is(':checked');
			if(!canDelete){
		    total=row['allocated'];
			}
		    total = Number(total);
		    if(isNaN(total)){
		    	total=Number(1);
		    }
		    grandTotal = Number(grandTotal) + total; 
		 }
		 if(parseInt(grandTotal)<=100){
			return true;
		 }else{ 
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","commissionDilg");
				jQuery(newDialogDiv).html('<span><b style="color:RED;">Sum of allocated should below 100'+'</b></span>');
				/*jQuery(newDialogDiv).dialog({ closeOnEscape: false,dialogClass: "no-close", modal: true, hide: 'explode', width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
					 //jQuery("#SoSplitCommissionGrid").jqGrid('restoreRow',id);
					
					var lastRowID = $("#SoSplitCommissionGrid tbody:first tr:nth-child(2)").attr('id');
					 jQuery("#SoSplitCommissionGrid").jqGrid('setSelection',lastRowID, true);
					 $('#SoSplitCommissionGrid_iledit').trigger('click');
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
							var lastRowID = $("#SoSplitCommissionGrid tbody:first tr:nth-child(2)").attr('id');
							
							/*if(id!='new_row'){
								lastRowID=id;
							}
							*/
							 jQuery("#SoSplitCommissionGrid").jqGrid('setSelection',lastRowID, true);
							 $('#SoSplitCommissionGrid_iledit').trigger('click');
						}
					}
				}).dialog("open");
				jQuery("a.ui-dialog-titlebar-close").hide();
				
				
				return false;
		 }
}
//SoSplitCommissionGrid_ilcancel
function validateCommissionTotalsSavePoRelease(){
	var rows = jQuery("#SoSplitCommissionGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {total=0;
		    row=jQuery("#SoSplitCommissionGrid").getRowData(rows[a]);
		    var id="#canDeleteID_"+rows[a];
			var canDelete=$(id).is(':checked');
			if(!canDelete){
		    total=row['allocated'];
			}
		   // total+=parseInt(total);
		    grandTotal = Number(grandTotal) + Number(total); 
		 }
	var splitCommGridDatas = $('#SoSplitCommissionGrid').getRowData();
	var splitCommGridDataLocal = JSON.stringify(splitCommGridDatas);
		 
	if(parseInt(grandTotal)<100 && (rows.length==0 && commissionRequired==1)){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:RED;">Please add Split Commission <br>Sum of allocated should be 100 </b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");
					if(commissionRequired==1 && parseInt(grandTotal)>0){
						var rows = $("#SoSplitCommissionGrid")[0].rows;
					    lastRowDOM = rows[rows.length-1];
						$("#SoSplitCommissionGrid").jqGrid('setSelection', lastRowDOM .attr('id'));
						$('#SoSplitCommissionGrid_iledit').trigger('click');
					}
					}}]}).dialog("open");
				return false;
		 }
	else if(parseInt(grandTotal)<100 && rows.length!=0){
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDilg");
		jQuery(newDialogDiv).html('<span><b style="color:RED;">Please add Split Commission <br>Sum of allocated should be 100 </b></span>');
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
	var element = "<input type='checkbox' id='"+id+"' onclick='deleteCheckboxChanges(this.id)'>";
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


function closeSOGeneralItemTab(){
	var new_so_generalform_values=generaltabformvalidation();
    var new_so_general_form =  JSON.stringify(new_so_generalform_values);
    var gridRows = $('#SoSplitCommissionGrid').getRowData();
    var newcommissiongridform=JSON.stringify(gridRows);
    
    
    var gridreturnvalue=false;
    if(typeof(commission_grid_form)!="undefined"){
    	gridreturnvalue=(commission_grid_form!=newcommissiongridform)?true:false;
    }
   
    if(new_so_general_form != so_general_form || gridreturnvalue){
  	  var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"Yes": function(){
					jQuery(this).dialog("close");
					saveSORelease("close");
				    return false;
				},
				"No": function ()	{
					jQuery(this).dialog("close");
					$("#salesrelease").dialog("close");
				return false;	
				}}}).dialog("open");
    }else{
  	  $("#salesrelease").dialog("close");
    }
	
}
function generaltabformvalidation(){
	var value1=$("#CustomerNameGeneral").val();
	var value2=$("#dateOfcustomerGeneral").val();
	var value3=$("#SOnumberGeneral").val();
	var value4=$("#SOlocationbillToAddressname").val();
	var value5=$("#SOGenerallocationbillToAddressID1").val();
	var value6=$("#locationbillToAddress2").val();
	var value7=$("#SOGenerallocationbillToCity").val();
	var value8=$("#SOGenerallocationbillToState").val();
//	var value9=$("#SOlocationShipToAddressIDeditable").val();
//	var value10=$("#SOlocationShipToAddressID1editable").val();
//	var value11=$("#SOlocationShipToAddressID2editable").val();
//	var value12=$("#SOlocationShipToCityeditable").val();
//	var value13=$("#SOlocationShipToStateeditable").val();
//	var value14=$("#SOlocationShipToZipIDeditable").val();
	var value15=$("#salesmanID").val();
	console.log("value15="+value15);
	var value16=$("#csrID").val();
	console.log("value16="+value16);
	var value17=$("#salesManagerID").val();
	console.log("value17="+value17);
	var value18=$("#engineerID").val();
	console.log("value18="+value18);
	var value19=$("#projectManagerID").val();
	console.log("value19="+value19);
	var value20=$("#whrhouseID").val();
	var value21=$("#SOshipViaId").val();
	var value22=$("#poID").val();
	var value23=$("#SOShipDate").val();
	var value24=$("#taxID").val();
	var value25=$("#terms").val();
	var value26=$("#custid").val();
	var value27=$("#tagJobID").val();
	var value28=$("#SOdivisionID").val();
	var value29=$("#promisedID").val();
	var value30=$("#taxIDwz").val();
	var value31=$("#termswz").val();
	var value32=$("#custidwz").val();
	var value33=$("#tagJobIDwz").val();
	var value34=$("#SOdivisionIDwz").val();
	var value35=$("#promisedIDwz").val();
	var value36=$("#SOGeneral_subTotalID").val();
		value36=customCurrencyFormatter(value36);
	var value37=$("#SOGeneral_frightID").val();
		value37=customCurrencyFormatter(value37);
	var value38=$("#SOGeneral_taxId").val();
	var value39=$("#SOGeneral_taxvalue").val();
		value39=customCurrencyFormatter(value39);
	var value40=$("#SOGeneral_totalID").val();
		value40=customCurrencyFormatter(value40);
//	var value41=$('#SOshiptoradio1').is(':checked');
//	var value42=$('#SOshiptoradio2').is(':checked');
//	var value43=$('#SOshiptoradio3').is(':checked');
//	var value44=$('#SOshiptolabel4').is(':checked');
		
	var value9=$("#SO_Shipto").contents().find("#shipToName").val();	
	var	value10=$("#SO_Shipto").contents().find("#shipToAddress1").val();
	var	value11=$("#SO_Shipto").contents().find("#shipToAddress2").val();
	var	value12=$("#SO_Shipto").contents().find("#shipToCity").val();
	var	value13=$("#SO_Shipto").contents().find("#shipToState").val();
	var	value14=$("#SO_Shipto").contents().find("#shipToZip").val();
	var value44=$("#SO_Shipto").contents().find("#shiptomoderhiddenid").val();
	var value45=$("#SO_Shipto").contents().find("#shiptoindexhiddenid").val();
	var value46=$("#SO_Shipto").contents().find("#shiptocusindexhiddenid").val();
	var divthereornot=$("#OriginalData").is(":visible");
 	if(divthereornot){
 		value24 = ""; 
 		value25 = ""; 
 		value26 = ""; 
 		value27 = ""; 
 		value28 = ""; 
 		value29 = ""; 
 	// 	console.log("Vaal 24=29"+value24+"=="+value25+"=="+value26+"=="+value27+"=="+value28+"=="+value29);
 	}else{
 		value30 =""; 
 		value31 ="";
 		value32 ="";
 		value33 ="";
 		value34 ="";
 		value35 ="";
 	//	console.log("Vaal 30=35"+value30+"=="+value31+"=="+value32+"=="+value33+"=="+value34+"=="+value35);
 	}
 	
 	if(value44!=3){
 		value9 ="";
 		value10="";
 		value11="";
 		value12="";
 		value13="";
 		value14="";
 	}
 	
 	//console.log(value28);
	var value=value1+value2+value3+value4+value5+value6+value7+value8+value9+value10+value11+value12+value13+value14+
	value15+value16+value17+value18+value19+value20+value21+value22+value23+value24+value25+value26+value27+
	value28+value29+value30+value31+value32+value33+value34+value35+value36+value37+value38+value39+value40+
	//value41+value42+value43+
	value44+value45+value46;
	return value;
}

function soGeneralformChanges(formvalue){
	console.log("soGeneralformChanges");
	var new_so_generalform_values=generaltabformvalidation();
    var new_so_general_form = JSON.stringify(new_so_generalform_values);
    var gridRows = $('#SoSplitCommissionGrid').getRowData();
    var newcommissiongridform=JSON.stringify(gridRows);
    var value=false;
    if(typeof(commission_grid_form)!="undefined"){
    	console.log("commission_grid_form"+commission_grid_form);
    	if(commission_grid_form!=newcommissiongridform){
    		value=true;
    	}
    }
    console.log("Testing purpose /n"+new_so_general_form+"/n"+so_general_form);
    console.log(new_so_general_form +"========"+ so_general_form);
    if(new_so_general_form != so_general_form || value){
    	console.log("new_so_general_form"+new_so_general_form);
		console.log("so_general_form"+so_general_form);
		console.log("newcommissiongridform"+newcommissiongridform);
    	if(formvalue=="TabChange"){
    		$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
    		 $('#loadingDivForSOGeneralTab').css({
    				"display": "none"
    			}); 
    		var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes, please save prior to continuing.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
				    return false;
				}}}).dialog("open");
    	}else{
    		$('#loadingDivForSOGeneralTab').css({
				"display": "none"
			});
    		$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
    	}
    }else{
    	$( "#salesreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
    }
	return false;
}
function soLineItemformChanges(formvalue){
	console.log("soLineItemformChanges");
	  var gridRows = $('#SOlineItemGrid').getRowData();
      var new_so_lines_form =  JSON.stringify(gridRows);
      
      if($('#SOlineItemGrid').val()!=undefined && typeof(so_lines_form)!="undefined"){
      if(new_so_lines_form != so_lines_form){
    	  console.log(new_so_lines_form+"========="+so_lines_form);
    	  console.log("LineItemForm");
    	if(formvalue=="TabChange"){
    		 $( "#salesreleasetab ul li:nth-child(1)" ).addClass("ui-state-disabled");
    		 $('#loadingDivForSOGeneralTab').css({
 				"display": "none"
 			}); 
  	  var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes, please save prior to continuing.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					$( "#salesreleasetab ul li:nth-child(1)" ).addClass("ui-state-disabled");
				    return false;
				}}}).dialog("open");
    	}else{
    		 $('#loadingDivForSOGeneralTab').css({
  				"display": "none"
  			});
    		$( "#salesreleasetab ul li:nth-child(1)" ).addClass("ui-state-disabled");
    	}
    	
    	
    }else{
    	$( "#salesreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
    }
     }
	return false;
}

$(function() { var cache = {}; var lastXhr=''; $( "#SOlocationShipToCityeditable" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
	var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#SOlocationShipToStateeditable").val(stateCode);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} }); });

function setTaxTotal_SO(){
		var cuSoid =  $('#Cuso_ID').text();
		var totalamount=$("#SOGeneral_subTotalID").val();
		if(totalamount==undefined ||totalamount=="" ||totalamount==null){
			totalamount=0.00;
		}
		if((totalamount+"").contains("$")){
			totalamount=totalamount.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		}
		 var freight=$("#SOGeneral_frightID").val();
		 if(freight==undefined ||freight=="" ||freight==null){
			 freight=0.00;
			}
		 if((freight+"").contains("$")){
			 freight=freight.replace(/[^0-9\.-]+/g,"").replace(".00", "");
			}
		 
		 var taxRate=$("#SOGeneral_taxId").val();
		 var totalWithTax=getsubtotalwithtax(cuSoid);
	     var taxamount=(Number(totalWithTax)*Number(taxRate))/100;
	     var taxchecked=$("#so_taxfreight").val()==1?true:false;
		 var taxforfreight=0;
	     if(taxRate>0 && taxchecked && freight!=null && freight>0){
	    	 taxforfreight=taxRate*freight/100;
			 taxamount=taxamount+taxforfreight;
	     }
		 if(taxamount<0)
			 taxamount=-taxamount;
		 
		 $("#SOGeneral_taxvalue").val(formatCurrency(taxamount));
		 $("#customerInvoice_taxvalue").val(formatCurrency(taxamount));
		 
		 var overalltotal=Number(totalamount)+Number(floorFigureoverall(freight,2))+Number(floorFigureoverall(taxamount,2));
		 $("#SOGeneral_totalID").val(formatCurrency(overalltotal));
		 $("#customerInvoice_taxId").val(formatCurrency(taxamount));
}
function getsubtotalwithtax(cuSoid){
	var totalamt=0.00;
	if(cuSoid!=undefined && cuSoid!=null && cuSoid!="" && cuSoid!=0){
	$.ajax({
		url: "./salesOrderController/LoadSolineitemDetails",
		type: "POST",
		async:false,
		data : {"cuSOID":cuSoid},
		success: function(data) {
			for(var i=0;i<data.length;i++){
				var unitcost=data[i].unitCost;
				var quantity=data[i].quantityOrdered;
				var pmult=data[i].priceMultiplier;
				if(pmult==null||pmult==""||pmult==0){
					pmult=1;
				}
				var taxable=data[i].taxable;
				if(taxable==1){
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
function triggertitle(){
	var status=$("#soStatusButton").val();
	if(status=="Quote"){
		if(document.getElementById("contactEmailID_general")!=undefined && document.getElementById("contactEmailID_general")!=null){
			document.getElementById("contactEmailID_general").title = "Email Quote";
		}
		if(document.getElementById("contactEmailID_lines")!=undefined && document.getElementById("contactEmailID_lines")!=null){
			document.getElementById("contactEmailID_lines").title = "Email Quote";
		}
	}else{
		if(document.getElementById("contactEmailID_general")!=undefined && document.getElementById("contactEmailID_general")!=null){
			document.getElementById("contactEmailID_general").title = "Email Sales Order";
		}
		if(document.getElementById("contactEmailID_lines")!=undefined && document.getElementById("contactEmailID_lines")!=null){
			document.getElementById("contactEmailID_lines").title = "Email Sales Order";
		}
	}
}