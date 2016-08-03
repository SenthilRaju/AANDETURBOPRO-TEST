/** PO Realse function **/
var cuinvoiceID='';
var so_openornot;
jQuery(document).ready(function() {
	Loadtab();
	var jobNumber;
	if($('#operation').val() === 'create' && $('#operation').val() === 'edit'){	
		
			if($("#Cuso_ID").text() != undefined || $("#Cuso_ID").text() != '')
			{
				jobNumber = $("#Cuso_ID").text();
			}
			else
			{
				jobNumber = $("#jobNumber_ID").text();
			}
		
	}	
	else
	{
		jobNumber = $("#jobNumber_ID").text();
	}
	$(".datepicker").datepicker();
	$("#salesreleasetab").tabs({
		cache: false,
		selected:0,
		ajaxOptions: {
			data:{
				joMasterID: $("#joMaster_ID").text(),
				jobNumber: jobNumber,
				jobName:"'" + $("#jobName_ID").text() + "'",
				joMasterID:function(){
					var joMasterID=0;
					 if($("#joMaster_ID").text()!=null &&  $("#joMaster_ID").text()!=""){
						 joMasterID= $("#joMaster_ID").text();
					 }
					 return joMasterID;
				},
				jobCustomer:$("#jobCustomerName_ID").text(),
				cuSOID : $("#Cuso_ID").text()
			},
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 60px;padding-top: 30px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			var $panel = $(ui.panel);
			$(this).tabs("option", { ajaxOptions: { data: {
				jobNumber: jobNumber,
				jobName:"'" + $("#jobName_ID").text() + "'",
				joMasterID:function(){
					var joMasterID=0;
					 if($("#joMaster_ID").text()!=null &&  $("#joMaster_ID").text()!=""){
						 joMasterID= $("#joMaster_ID").text();
					 }
					 return joMasterID;
				},
				jobCustomer:$("#jobCustomerName_ID").text(),
				cuSOID : $("#Cuso_ID").text()
				
			} } });
			
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;'><img src='./../resources/scripts/jquery-autocomplete/loading11.gif'></div>");
			}
		}
	});
	$( "#salesreleasetab" ).tabs({ active: 0 });
	$('#cusinvoicetab, #soreleaselineitem').tabs({
		  select: function(event, ui){
			  //CheckCustomerInvoiceSave();
		  }
		});
	
});
function CheckCustomerInvoiceSave(){
	console.log("CheckCustomerInvoiceSave");
	var cusoId =  $('#Cuso_ID').text();
	var grid = $("#release");
	var SelectedRow = grid.jqGrid('getGridParam', 'selrow');
	var vePOID = grid.jqGrid('getCell', SelectedRow, 'vePoId');
	var shipinggrid = $("#shiping");
	var shiping_SelectedRow = shipinggrid.jqGrid('getGridParam', 'selrow');
	var joReleaseDetailid = shipinggrid.jqGrid('getCell', shiping_SelectedRow, 'joReleaseDetailID');
	if(vePOID){
		console.log('vePOID'+vePOID);
		$.ajax({
			url: "./salesOrderController/CheckCuinvoiceDetails",
			type: "POST",
			data : {"CuSOID" : vePOID, "joReleaseDetailid" :joReleaseDetailid},
			success: function(data) {
				if(data===true){
					console.log("data true");
					$('#InfoSave').hide();
					$('#regularTab').show();
					PreloadDataFromInvoiceTable();
					$("#customerInvoice_lineitems").jqGrid('GridUnload');
					loadCustomerInvoice();
					$("#customerInvoice_lineitems").trigger("reloadGrid");
					
					$("#customerInvoice_lineitems").trigger("reloadGrid");
				}
				else{
					console.log("data false");
					$('#InfoSave').show();
//					$('#regularTab').hide();
					PreloadDataInvoice();
				}
				}
		});
	} else {
	$.ajax({
		url: "./salesOrderController/CheckCuinvoiceDetails",
		type: "POST",
		data : {"CuSOID" : cusoId,"joReleaseDetailid" : joReleaseDetailid},
		success: function(data) {
			
			if(data===true){
				$('#InfoSave').hide();
				$('#regularTab').show();
				PreloadDataFromInvoiceTable();
				$("#customerInvoice_lineitems").jqGrid('GridUnload');
				loadCustomerInvoice();
				$("#customerInvoice_lineitems").trigger("reloadGrid");
				$("#customerInvoice_lineitems").trigger("reloadGrid");
			}
			else{
				$('#InfoSave').show();
				//$('#regularTab').hide();
				PreloadDataInvoice();
			}
			}
	});
	}
}

/** once customer invoice general tab is saved, the data will load from the cuInvoice table through this method. */

function PreloadDataFromInvoiceTable(){
	console.log("PreloadDataFromInvoiceTable called");
	$('#customerInvoice_linefrightID').val(formatCurrency(0));
	var cuInvoiceId = $('#cuinvoiceIDhidden').val();	
	if(cuInvoiceId == null || cuInvoiceId == '')
	{
		cuinvoiceID = $('#cuInvoiceID').text();
	}	
	try{
	var rxMasterID = $('#rxCustomer_ID').text();
	cuinvoiceID =cuInvoiceId; 
	$.ajax({
		url: "./salesOrderController/getPreInvoiceData",
		type: "POST",
		async:false,
		data : "&cuInvoiceId="+cuInvoiceId+"&rxMasterID="+rxMasterID,
		success: function(data) {
			$("#CustomerNameGeneral").val(data.CustomerName);
			if (typeof(data.cuInvoice) != "undefined" && data.cuInvoice != null){
			$("#customerInvoice_invoiceNumberId").val(data.cuInvoice.invoiceNumber);
			$('#customerInvoice_subTotalID').val(formatCurrency(0));
			$('#customerInvoice_totalID').val(formatCurrency(0));
			$('#customerinvoicepaymentId').val(data.cuInvoice.cuTermsId);
			$('#customerInvoice_proNumberID').val(data.cuInvoice.trackingNumber);
			$('#customerInvoie_PONoID').val(data.cuInvoice.customerPonumber);
			$('#tagJobID').val(data.cuInvoice.tag);
			var dueDate = new Date(data.cuInvoice.dueDate);
			
			var dueDateformat = (Number(dueDate.getMonth())+1)+'/'+dueDate.getDate()+'/'+dueDate.getFullYear();
			$("#customerInvoice_dueDateID").val(dueDateformat);
			var invoiceDate = new Date(data.cuInvoice.createdOn);
			var invoiceDateformat = (Number(invoiceDate.getMonth())+1)+'/'+invoiceDate.getDate()+'/'+invoiceDate.getFullYear();
			$("#customerInvoice_invoiceDateID").val(invoiceDateformat);
			var shipDate = new Date(data.cuInvoice.shipDate);
			var shipDateformat = (Number(shipDate.getMonth())+1)+'/'+shipDate.getDate()+'/'+shipDate.getFullYear();
			$("#customerInvoice_shipDateID").val(shipDateformat);
			$("#customerInvoice_lineshipDateID").val(shipDateformat);
			$("#customerInvoice_lineinvoiceDateID").val(invoiceDateformat);
			/*
			
			
			*/
			//$('#customerInvoice_frightIDcu').val(formatCurrency(data.cuInvoice.freight)); 
			//$('#customerInvoice_linefrightID').val(formatCurrency(data.cuInvoice.freight)); 
			console.log("data.cuInvoice.freight ----->"+data.cuInvoice.freight);
			console.log(whseID);
			if(data.cuInvoice.prFromWarehouseId === null)
				{
				$('#prWareHouseSelectID').val(data.cuInvoice.prFromWarehouseId);
				var x=document.getElementById("prWareHouseSelectID");
			    x.options[0].text="Drop Ship";
			    var xL=document.getElementById("prWareHouseSelectlineID")
				xL.options[0].text="Drop Ship";
			    $("#prWareHouseSelectID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectID").prop('disabled', true);
				}
			else
				{
					var x=document.getElementById("prWareHouseSelectID")
					x.options[0].text="- Select -";
					var xL=document.getElementById("prWareHouseSelectlineID")
					xL.options[0].text="- Select -";
					$("#prWareHouseSelectID").prop('disabled', false);
					$('#prWareHouseSelectID').val(data.cuInvoice.prFromWarehouseId);
				}
			
			$('#shipViaCustomerSelectlineID').val(data.cuInvoice.veShipViaId);
			$('#shipViaCustomerSelectID').val(data.cuInvoice.veShipViaId);			
			$('#customer_Divisions').val(data.cuInvoice.coDivisionId);
			$('#customerinvoice_paymentTerms').val(data.cuTerms);
			$('#customerInvoice_salesRepsList').val(data.SalesMan);
			$('#customerInvoice_CSRList').val(data.AE);
			$('#customerInvoice_SalesMgrList').val(data.Submitting);
			$('#customerInvoice_EngineersList').val(data.Costing);
			$('#customerInvoice_PrjMgrList').val(data.Ordering);
			$('#customerInvoice_lineproNumberID').val(data.cuInvoice.trackingNumber);
			$('#customerInvoice_lineinvoiceNumberId').val(data.cuInvoice.sonumber);
//			$('#customerInvoice_linetaxId').val(formatCurrency(data.cuInvoice.taxRate));
			$('#customerInvoice_lineproNumberID').val(data.cuInvoice.trackingNumber);
			$('#customerInvoice_lineinvoiceNumberId').val(data.cuInvoice.invoiceNumber);
			$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
			$("#shipToCustomerAddressID").val(data.cuInvoice.rxShipToId)
			addressToShipCustomerInvoice();
			customerinvoiceShiptoAddress(data.cuInvoice.cuInvoiceId,'cuinvoice');
			if(data.cuInvoice.doNotMail===0)
				$('#customerInvoie_doNotMailID').prop('checked',false);
			else
				$('#customerInvoie_doNotMailID').prop('checked',true);
			var createdDate = data.cuInvoice.createdOn;
			if (typeof (createdDate) != 'undefined') 
				FormatDate(createdDate);
			var shipDate = data.cuInvoice.shipDate;
			if (typeof (shipDate) != 'undefined') 
			{
			$("#customerInvoice_shipDateID").val('');
			$("#customerInvoice_shipDateID").val(UsDateFormate(new Date(shipDate)));
			}
			else
			{
			$("#customerInvoice_shipDateID").val('');
			$("#customerInvoice_shipDateID").val(UsDateFormate(new Date()));
			}
			
			$('#customerInvoice_subTotalID').val(formatCurrency(data.cuInvoice.subtotal));
			$('#customerInvoice_totalID').val(formatCurrency(data.cuInvoice.subtotal+data.cuInvoice.taxTotal+data.cuInvoice.freight));
			$('#customerInvoice_linesubTotalID').val(formatCurrency(data.cuInvoice.subtotal));
			$('#customerInvoice_linetotalID').val(formatCurrency(data.cuInvoice.subtotal+data.cuInvoice.taxTotal+data.cuInvoice.freight));
			$('#customerInvoice_frightIDcu').val(formatCurrency(data.cuInvoice.freight));
			$('#customerInvoice_linefrightID').val(formatCurrency(data.cuInvoice.freight));
			setTimeout(function(){
				$('#customerInvoice_taxIdcu').val(formatCurrency(data.cuInvoice.taxTotal));
				$('#cuGeneral_taxvalue').val(formatCurrency(data.cuInvoice.taxTotal));
				$('#CI_taxfreight').val(data.cuInvoice.taxfreight);
				setTaxTotal_CI();
			}, 3000);
			
			/*if (typeof(data.Cuinvoicedetail) != "undefined" && data.Cuinvoicedetail != null){
				console.log('PreloadDataFromInvoiceTable data.Cuinvoicedetail if block :: '+allocated);
				if(typeof(allocated) != undefined && allocated != 'undefined' && allocated != undefined && iFlag != 1){
					console.log('if--->');
					$('#customerInvoice_subTotalID').val(formatCurrency(allocated));
					console.log(allocated+' ::: '+data.cuInvoice.taxTotal+' ::: '+data.cuInvoice.freight+'  ::: Total :: '+aTotal);
					var aTotal = parseFloat(allocated)+parseFloat(data.cuInvoice.taxTotal)+parseFloat(data.cuInvoice.freight);
					console.log(allocated+' ::: '+data.cuInvoice.taxTotal+' ::: '+data.cuInvoice.freight+'  ::: Total :: '+aTotal);
					$('#customerInvoice_totalID').val(formatCurrency(aTotal));
					$('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
					$('#customerInvoice_linetotalID').val(formatCurrency(aTotal));
					$('#customerInvoice_frightIDcu').val(formatCurrency(data.cuInvoice.freight));
					$('#customerInvoice_linefrightID').val(formatCurrency(data.cuInvoice.freight));
				}
				else
					{
					console.log('else');
					$('#customerInvoice_subTotalID').val(formatCurrency(data.Cuinvoicedetail.subtotal));
					$('#customerInvoice_totalID').val(formatCurrency(data.Cuinvoicedetail.subtotal+data.cuInvoice.taxTotal+data.cuInvoice.freight));
					$('#customerInvoice_linesubTotalID').val(formatCurrency(data.Cuinvoicedetail.subtotal));
					$('#customerInvoice_linetotalID').val(formatCurrency(data.Cuinvoicedetail.subtotal+data.cuInvoice.taxTotal+data.cuInvoice.freight));
					$('#customerInvoice_frightIDcu').val(formatCurrency(data.cuInvoice.freight));
					$('#customerInvoice_linefrightID').val(formatCurrency(data.cuInvoice.freight));
					}
					
				
				}else{
					console.log('PreloadDataFromInvoiceTable data.Cuinvoicedetail else block ');
					$('#customerInvoice_subTotalID').val(formatCurrency(0));
					$('#customerInvoice_totalID').val(formatCurrency(0));
					$('#customerInvoice_linesubTotalID').val(formatCurrency(0));
					$('#customerInvoice_linetotalID').val(formatCurrency(0));
				}*/
			
			}
			formattax();
			//loadCustomerInvoice();
		}
	});
}
	catch(e)
	{
		console.log("Error: PreloadDataFromInvoiceTable :: " + e + "\n\nFalse will be returned.");
	}
}



function PreloadDataInvoice(CIdivFlag){		
	console.log('Jenith You may Here1');
	
	//alert("i am hear SalesRelease");
	
	CIdivFlag ="#"+CIdivFlag;
	$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val("");
	var cuSOID = $('#Cuso_ID').text();
	var rxMasterID = $('#rxCustomer_ID').text();
	var grid = $("#release");
	var SelectedRow = grid.jqGrid('getGridParam', 'selrow');
	var vePOID = grid.jqGrid('getCell', SelectedRow, 'vePoId');
	var count = $("#shiping").getGridParam("reccount");
	var rowId = $("#shiping").jqGrid('getGridParam', 'selrow');
	var joReleaseDetailID = '';
	var veBillID = '';
	if(rowId !=null && rowId !=undefined){
	joReleaseDetailID  = $("#shiping").jqGrid('getCell', rowId, 'joReleaseDetailID');
	veBillID = $("#shiping").jqGrid('getCell', rowId, 'veBillID');
	}
	var releaseType = grid.jqGrid('getCell', SelectedRow, 'type');
	var jobNumber = $('.jobHeader_JobNumber').val();
	
	$.ajax({
		url: "./salesOrderController/getPreLoadData",
		type: "POST",
		data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID+"&veBillID="+veBillID+"&joMasterID="+$("#joMaster_ID").text(),
		success: function(data) {
			$("#CI_taxsubtotal").val("");
			if(data.vepo !== null){
				console.log('U r in vePO');
				PreloadDataInvoiceFromPO(data,CIdivFlag);
				$('#customerInvoice_lineproNumberID').val(data.vendorInvoiceNumber);
				$('#customerInvoice_proNumberID').val(data.vendorInvoiceNumber);
			}
			else if(releaseType == "Bill Only")
			{
				if(data!=null && data.aJomaster!=null){
					$("#customerInvoie_PONoID").val(data.aJomaster.customerPonumber);
				}
				
				$.ajax({
			        url: './getUserDefaults',
			        type: 'POST',       
			        success: function (datas) {
			        	//alert('WareHouseID:'+data.warehouseID);
			        	if(datas.warehouseID > 0){
			        		var x=document.getElementById("prWareHouseSelectID")
							x.options[0].text="- Select -";
							var xL=document.getElementById("prWareHouseSelectlineID")
							xL.options[0].text="- Select -";
						 	$("#prWareHouseSelectID").prop('disabled', false);
						 	$('#prWareHouseSelectID').val(datas.warehouseID);
						 	$('#prWareHouseSelectlineID').val(datas.warehouseID);
			        	}
			        }
				});
			        	
			}
			else{
				$("#CustomerNameGeneral").val(data.CustomerName);
				if (typeof(data.Cuso) != "undefined" && data.Cuso != null){
						//$("#customerInvoice_invoiceNumberId").val(data.Cuso.sonumber);
						console.log('You r in SO * CI Number::'+data.customerInvoiceNumber);
						$("#customerInvoice_invoiceNumberId").val(data.customerInvoiceNumber);
						$('#customerInvoice_subTotalID').val(formatCurrency(0));
						$('#customerInvoice_totalID').val(formatCurrency(0));
						$('#customerinvoicepaymentId').val(data.Cuso.cuTermsId);
						$('#customerInvoice_proNumberID').val(data.Cuso.trackingNumber);
						$("#customerInvoie_PONoID").val(data.Cuso.customerPonumber);
						$('#tagJobID').val(data.Cuso.tag);
						$('#customerInvoice_frightIDcu').val(formatCurrency(0)); 
						$('#customerInvoice_linefrightID').val(formatCurrency(data.Cuso.freight)); 
						$('#CI_taxfreight').val(data.Cuso.taxfreight);
			/*if(data.Cuso.prFromWarehouseId == null)
				{
				console.log("yes=====================");
				
				$('#prWareHouseSelectID').val(-1);
				$('#prWareHouseSelectlineID').val(-1);
				
				var x=document.getElementById("prWareHouseSelectID")
			    x.options[0].text="Drop Ship"
			    $("#prWareHouseSelectID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectID").prop('disabled', true);
		        
		        var x=document.getElementById("prWareHouseSelectlineID")
			    x.options[0].text="Drop Ship"
			    $("#prWareHouseSelectlineID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectlineID").prop('disabled', true);
				
				}
			else
				{*/
				
					var x=document.getElementById("prWareHouseSelectID")
					x.options[0].text="- Select -";
					var xL=document.getElementById("prWareHouseSelectlineID")
					xL.options[0].text="- Select -";
				 	$("#prWareHouseSelectID").prop('disabled', false);
				 	$('#prWareHouseSelectID').val(data.Cuso.prFromWarehouseId);
				 	$('#prWareHouseSelectlineID').val(data.Cuso.prFromWarehouseId);
				/*}*/
			
			
			
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
			$("#shiptoIndexforcustomer").val(data.Cuso.shipToIndex);	
			//alert($("#rxShipToOtherAddressID").val()+"======="+$("#shiptoIndexforcustomer").val());
			$("#prShiptowarehouseID").val(data.Cuso.rxShipToAddressId);	
			 
			$("#customerInvoice_TaxTerritory").val(data.coTaxterritory);
        	$("#customerTaxTerritory").val(data.Cuso.coTaxTerritoryId);
			
			console.log("---=-==->>>>"+data.Cuso.shipToMode);
			//addressToShipCustomerInvoice();
			
			
			/*Commented by velmurugan
			 * Ship to Implementation new Code
			 * 19-10-2015
			 * */
			/*if(data.Cuso.shipToMode == 0)
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
				console.log('inside SalesRelease.js data.Cuso.shipToMode == 3');
				otherinvoiceShiptoAddress(data.Cuso.cuSoid,'cuso')
				}
			else if(data.Cuso.shipToMode == 2)
				{
				$("#cuinvoiceUs").hide();
				$('#shiptoModeID').val(2);
				console.log('inside SalesRelease.js data.Cuso.shipToMode == 2');
				jobsiteinvoiceShiptoAddress(data.Cuso.joReleaseId,'cuso')
				}*/
			/*New Code Implementation*/
			/*New Customer Invoice Ship To Code Starts*/
		//	alert(data.Cuso.coTaxTerritoryId+"Leo I am here"+_global_override_taxTerritory+"=="+_global_override_taxIDBasedOnCustomer);
			
			var shiptomode = data.Cuso.shipToMode;
			var checkshiptoid;
			$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val("");
			$(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val("");
			loadshiptostateautocmpte(CIdivFlag);
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
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.prToWarehouseId);
				}
				else if(data.Cuso.shipToMode == 1)
				{
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.rxShipToId);
				}
				else if(data.Cuso.shipToMode == 2)
				{
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.rxShipToId);
				}
				else
				{
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.Cuso.rxShipToAddressId);
				}
				$(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val(data.Cuso.shipToMode);
				
				// Commented by Leo: ID#532 - Even Override tax territory is enabled in setting page. customer invoice load whatever is mentioned in SO or SEO. 
				
				/*if(_global_override_taxTerritory )
				preloadShiptoAddress(CIdivFlag,data.Cuso.cuSoid,checkshiptoid,data.Cuso.shipToMode,'0',$("#jobCustomerName_ID").text(),_global_override_taxIDBasedOnCustomer);
				else*/
				preloadShiptoAddress(CIdivFlag,data.Cuso.cuSoid,checkshiptoid,data.Cuso.shipToMode,'0',$("#jobCustomerName_ID").text(),data.Cuso.coTaxTerritoryId);
				
				$(CIdivFlag).contents().find("#shiptomoderhiddenid").val(data.Cuso.shipToMode);
			}
			
			/*Code Ends */
			var createdDate = data.Cuso.createdOn;
			if (typeof (createdDate) != 'undefined') 
				FormatDate(createdDate);
			var shipDate = data.Cuso.shipDate;
			if (typeof (shipDate) != 'undefined' && shipDate != null) 
				{
				$("#customerInvoice_shipDateID").val('');
				$("#customerInvoice_shipDateID").val(UsDateFormate(new Date(data.Cuso.shipDate)));
				}
			else
				{
				$("#customerInvoice_shipDateID").val('');
				$("#customerInvoice_shipDateID").val(UsDateFormate(new Date()));
				}
			
			
			var taxsubtotal=0;
			$("#CI_taxsubtotal").val(0);
			if (typeof(data.Cusodetail) != "undefined" && data.Cusodetail != null){
				var taxrate = data.Cuso.taxRate;
				var subtotal = data.Cusodetail.taxTotal;
				var freight = data.Cuso.freight;
				var taxtotals = 0.00;
				var totalAmount=0.00;
				var datacusodetail=data.Cusodetail;
				$("#CI_taxsubtotal").val(data.Cusodetail.taxableSum);
				taxtotals=data.Cuso.taxTotal;
				totalAmount =parseFloat(taxtotals)+parseFloat(freight)+parseFloat(subtotal);
				
					console.log('1 count<=0');
					setTimeout(function(){
						$('#customerInvoice_generaltaxId').val(taxrate);
						$('#customerInvoice_subTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
						$('#customerInvoice_totalID').val(formatCurrency(totalAmount));
						$('#customerInvoice_linesubTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
						$('#customerInvoice_linetotalID').val(formatCurrency(totalAmount));
						$('#customerInvoice_frightIDcu').val(formatCurrency(data.Cuso.freight));
						$('#customerInvoice_taxIdcu').val(formatCurrency(taxtotals));
					}, 250);
				
			}
			}
			formattax();
			if(data.Cuso.cuTermsId!=null)
			paymentTermsDue(data.Cuso.cuTermsId);
			}
			
			
			
		}
	});
	
	if(releaseType =='Bill Only'){
		
		var ponumberAlphabet = grid.jqGrid('getCell', SelectedRow, 'ponumber');
		var invoicenumberwithoutprefix=$("#jobNumber_ID").text().trim()+''+ponumberAlphabet;
		var invoicenumber=preloadcustomerinvoicenumberforBillOnly(invoicenumberwithoutprefix);
		$("#customerInvoice_invoiceNumberId").val(invoicenumber);
		$("#customerInvoice_shipDateID").val('');
		$("#customerInvoice_shipDateID").val(UsDateFormate(new Date()));
		}
}
function preloadcustomerinvoicenumberforBillOnly(invoicenumberwithoutprefix){
	//alert('Called:'+joReleaseID);
	var returndata='';
	$.ajax({
		url: "./salesOrderController/preloadcustomerinvoicenumberforBillOnly",
		type: "POST",
		async:false,
		data : {"invoicenumberwithoutprefix" : invoicenumberwithoutprefix},
		success: function(data) {
			returndata=data;
			}
	});
	return returndata;
}
function preloadCustomePONumber(joReleaseID){
	//alert('Called:'+joReleaseID);
	$.ajax({
		url: "./salesOrderController/getJoReleaseDetail",
		type: "POST",
		data : {"joReleaseID" : joReleaseID},
		success: function(data) {
			//alert(data);
			/*data.itsJoReleaseID;
			data.itsJoMasterID;
			data.itsReleaseType;
			data.itsReleaseDate;
			data.itsEstimatedBilling;
			data.itsReleaseNote;
			data.itsCommissionReceived;
			data.itsCommissionDate;
			data.itsCommissionAmount;
			data.itsCancelled;
			data.POID;
			data.itsBillNote;
			data.seq_Number;*/
			console.log('PONumbers:'+data.joMasterId+'  '+data.poid);
			$('#customerInvoie_PONoID').val(data.poid);
			}
	});
}

function PreloadDataInvoiceFromPO(data,CIdivFlag){
	$("#CustomerNameGeneral").val(data.CustomerName);
	if (typeof(data.vepo) != "undefined" && data.vepo != null){
	$("#customerInvoice_invoiceNumberId").val(data.vepo.ponumber);
	$('#customerInvoice_subTotalID').val(formatCurrency(0));
	$('#customerInvoice_totalID').val(formatCurrency(0));
	if(data.Cumaster!=null){
		$('#customerinvoicepaymentId').val(data.Cumaster.cuTermsId);
		paymentTermsDue(data.Cumaster.cuTermsId);
	}
//	$('#customerInvoice_proNumberID').val(data.Cumaster.trackingNumber);
	$('#customerInvoie_PONoID').val(data.vepo.customerPonumber);
//	$('#tagJobID').val(data.Cuso.tag);
	/* BID #1012 Job (Release tab) � Freight on PO Automatically flows through to Customer Invoice and Shouldn�t [FIX by 10/23/2015]
	  * $('#customerInvoice_frightIDcu').val(formatCurrency(data.vepo.freight)); 
	 $('#customerInvoice_linefrightID').val(formatCurrency(data.vepo.freight)); */
	$('#customerInvoice_frightIDcu').val(formatCurrency(0)); 
	$('#customerInvoice_linefrightID').val(formatCurrency(0));
	 whseID = 'Drop Ship';
	if(whseID === 'Drop Ship')
	{
		console.log("yes NO=======ITs Drop Ship==============");
		var x=document.getElementById("prWareHouseSelectID");
	    x.options[0].text="Drop Ship";
	    var xL=document.getElementById("prWareHouseSelectlineID");
		xL.options[0].text="Drop Ship";
	    $("#prWareHouseSelectID option[value=-1]").attr("selected", true);
        $("#prWareHouseSelectID").prop('disabled', true);
        $('#prWareHouseSelectID').val(-1);
    	$('#prWareHouseSelectlineID').val(-1);
	
	//$('#prWareHouseSelectID').val(3);
	//$('#prWareHouseSelectlineID').val(3);
	/*$('#prWareHouseSelectID').val(data.vepo.prWarehouseId);
	$('#prWareHouseSelectlineID').val(data.vepo.prWarehouseId);*/
	}
	else
	{
		var x=document.getElementById("prWareHouseSelectID")
		x.options[0].text="- Select -";
		var xL=document.getElementById("prWareHouseSelectlineID")
		xL.options[0].text="- Select -";
		$("#prWareHouseSelectID").prop('disabled', false);
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
	$('#customerInvoice_generaltaxId').val(data.vepo.taxRate);
	console.log('customerInvoice_generaltaxId:'+data.vepo.taxRate);
	$('#cuGeneral_taxvalue').val(formatCurrency(data.vepo.taxTotal));
//	$('#customerInvoice_lineproNumberID').val(data.vepo.trackingNumber);
	$('#customerInvoice_lineinvoiceNumberId').val(data.vepo.ponumber);
	console.log("PONumber is=====>"+data.vepo.ponumber);
	$('#customerbillToAddressIDcuInvoice').val(data.CustomerName);
	$('#customerinvoice_paymentTerms').val(data.termsDesc);
	var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\-.]+/g,"");
	if(freight == null || freight==undefined || freight=="NaN" || freight==""){
		freight = 0.00;
	}
	 //var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.]+/g,"");			 
	 var tot = allocated;
	 var taxPerc = $('#customerInvoice_generaltaxId').val().replace(/[^0-9\.-]+/g,"");
	 console.log("allocated*taxPerc ::: "+(allocated*taxPerc));
	 var taxValue1 = (allocated*taxPerc)/100;
	 
	 
	 var allowfreightinTax=false;
	/* var allowreqcheckfreightintax=getSysvariableStatusBasedOnVariableName("RequireFreightwhencalculatingTaxonCustomerInvoices");
		if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax[0].valueLong==1){
			allowfreightinTax=true;
		}*/
	 if(allowfreightinTax){
		 taxValue1 = (parseFloat(allocated)+parseFloat(freight))*Number(taxPerc)/100;
	 }else{
		 taxValue1 = parseFloat(allocated)*Number(taxPerc)/100;
	 }
	 //alert(taxValue1);
	 var aTotal=parseFloat(allocated)+parseFloat(freight)+parseFloat(taxValue1);
	 
	 $("#CI_taxsubtotal").val(allocated);
	 $('#customerInvoice_taxIdcu').val(formatCurrency(taxValue1));
	 $('#cuGeneral_taxvalue').val(formatCurrency(taxValue1));
	 // var aTotal = parseFloat(allocated)+parseFloat(freight)+parseFloat(taxValue1);
	 console.log('Tax Value  :: '+formatCurrency(taxValue1)+"  :: Freigt ::  "+formatCurrency(freight)+"  :: Amt  ::"+formatCurrency(allocated) + "  :: Total  :: "+formatCurrency(aTotal));
	 $('#customerInvoice_totalID').val(formatCurrency(aTotal));
	 $('#customerInvoice_linetotalID').val(formatCurrency(aTotal));
	 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
	 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
	 iFlag = 2;
	 $("#shipToCustomerAddressID").val(data.vepo.rxShipToId)
	 $("#prShiptowarehouseID").val(data.vepo.rxShipToAddressId);	
	 /*Commented by velmurugan
		 * Ship to Implementation new Code
		 * 20-10-2015
		 * */
	 /*addressToShipCustomerInvoice();
	 
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
		}*/
	 /*New Code Implementation*/
	 /*New Customer Invoice Ship To Code Starts*/
	 var shiptomode = data.vepo.shipToMode;
	var checkshiptoid;
	$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val("");
	$(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val("");
	loadshiptostateautocmpte(CIdivFlag);
	if(shiptomode == "0")
	{
		checkshiptoid = data.vepo.prWarehouseId;
	}
	else if(shiptomode == "1" || shiptomode == "2")
	{
		checkshiptoid = data.vepo.rxShipToId;
	}
	else
	{
		checkshiptoid = data.vepo.rxShipToAddressId;
	}
	if(checkshiptoid!=null)
	{
		if(data.vepo.shipToMode == 0)
		{
			$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.vepo.prWarehouseId);
		}
		else if(data.vepo.shipToMode == 1)
		{
			$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.vepo.rxShipToId);
		}
		else if(data.vepo.shipToMode == 2)
		{
			$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.vepo.rxShipToId);
		}
		else
		{
			$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.vepo.rxShipToAddressId);
		}
		$(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val(data.vepo.shipToMode);
		/*if(_global_override_taxTerritory)
		preloadShiptoAddress(CIdivFlag,data.vepo.vePoid,checkshiptoid,data.vepo.shipToMode,'0',$("#jobCustomerName_ID").text(),_global_override_taxIDBasedOnCustomer);
		else*/
		preloadShiptoAddress(CIdivFlag,data.vepo.vePoid,checkshiptoid,data.vepo.shipToMode,'0',$("#jobCustomerName_ID").text(),"");
		$(CIdivFlag).contents().find("#shiptomoderhiddenid").val(data.vepo.shipToMode);
	}
	 			
	 /*Code Ends */
	 
	var createdDate = data.vepo.createdOn;
	if (typeof (createdDate) != 'undefined') 
		FormatDate(createdDate);
	var shipDate = data.vepo.createdOn;
	var veBillShipDate = data.vepo.shipDate;
	console.log('IJK ShipDate::'+veBillShipDate)
	if (typeof (veBillShipDate) != 'undefined' && (veBillShipDate!='' && veBillShipDate !=null))
	//	FormatShipDate(shipDate);
		{
		$("#customerInvoice_shipDateID").val('');
		$("#customerInvoice_shipDateID").val(UsDateFormate(new Date(veBillShipDate)));
		}
		else
		{
		$("#customerInvoice_shipDateID").val('');
		$("#customerInvoice_shipDateID").val(UsDateFormate(new Date()));
		}
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
		colNames:['Product No','', 'Description','Qty','Price Each', 'Mult.', 'Tax', 'Amount','Notes', 'Manu. ID','cuSodetailId', 'prMasterID'],
		colModel :[
	{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:17,
		dataEvents: [
  	       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
  	    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
  	    			  ]
	},editrules:{edithidden:false,required: true}},
	{name:'noteImage',index:'noteImage', align:'right', width:10,hidden:false, editable:false, formatter:noteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:17,
		dataEvents: [
  	       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
  	    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
  	    			  ]
	},editrules:{edithidden:false},  
		cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
	{name:'quantityBilled', index:'quantityBilled', align:'center', width:15,hidden:false, editable:true, editoptions:{size:17, alignText:'left',
		dataEvents: [
  	       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
  	    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
  	    			  ]
	},editrules:{edithidden:true,required: false}},
	{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:17, alignText:'right',
		dataEvents: [
  	       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
  	    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
  	    			  ]
	},editrules:{edithidden:true}},
	{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:17, alignText:'right',
		dataEvents: [
  	       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
  	    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
  	    			  ]
	}, formatter:customCurrencyFormatter, editrules:{edithidden:true}},
	{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:setProductTaxable, edittype:'checkbox', editrules:{edithidden:true}},
	{name:'amount', index:'amount', align:'right', width:50,hidden:false, editable:false, editoptions:{size:15, alignText:'right',
		dataEvents: [
  	       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
  	    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
  	    			  ]
	},editrules:{edithidden:true},formatter:customTotalFomatter},
	{name:'note', index:'note', align:'right', width:50,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
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
			var ids = $('#customerInvoice_lineitems').jqGrid('getDataIDs');
			$('#customerInvoice_lineitems_noteImage').removeClass("ui-state-default ui-th-column ui-th-ltr");			
		    if (ids) {
		        var sortName = $('#customerInvoice_lineitems').jqGrid('getGridParam','noteImage');
		        var sortOrder = $('#customerInvoice_lineitems').jqGrid('getGridParam','description');
		        for (var i=0;i<ids.length;i++) {
		        	
		        	$('#customerInvoice_lineitems').jqGrid('setCell', ids[i], 'noteImage', '', '',
		                        {style:'border-right-color: transparent !important;'});
		        	$('#customerInvoice_lineitems').jqGrid('setCell', ids[i], 'description', '', '',
	                        {style:'border-left-color: transparent !important;'});
		        }
		    }
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
			taxRate = parseFloat(taxRate.replace(/[^0-9\-.]/g, ''));
			var freight = $('#customerInvoice_linefrightID').val();
			freight = parseFloat(freight.replace(/[^0-9\-.]/g, ''));
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
					taxRate = parseFloat(taxRate.replace(/[^0-9\-.]/g, ''));
					var freight = $('#customerInvoice_linefrightID').val();
					freight = parseFloat(freight.replace(/[^0-9\-.]/g, ''));
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
					taxRate = parseFloat(taxRate.replace(/[^0-9\-.]/g, ''));
					var freight = $('#customerInvoice_linefrightID').val();
					freight = parseFloat(freight.replace(/[^0-9\-.]/g, ''));
					return { 'cuInvoiceDetailId' : cuInvoiceID,'taxRate':taxRate,'freight':freight};
					
				},
				afterSubmit:function(response,postData){
					PreloadDataFromInvoiceTable();
					 return [true, loadCustomerInvoice()];
				}
			}
		);
	$('#customerInvoice_lineitems').jqGrid('navButtonAdd',"#customerInvoice_lineitemspager",{ caption:"", buttonicon:"ui-icon-calculator", onClickButton:ShowInvoiceNote, position: "last", title:"Edit note for line item", cursor: "pointer"});
	}

function noteImage(cellValue, options, rowObject){
	var element = '';
   if(cellValue !== '' && cellValue !== null && cellValue != undefined){
	   element = "<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
   }
   return element;
}

function setProductTaxable(cellValue, options, rowObject){
	var element = '';
	var taxValue =  $('#customerInvoice_TaxTerritory').val();
   if((taxValue !== null && taxValue.length>0) && taxValue.toLowerCase()!='exempt'){
	   var id="taxableID_"+options.rowId;
	   element = "<input type='checkbox' id='"+id+"' checked='checked' disabled>";
   }else{
	   element = "<input type='checkbox' id='"+id+"'  disabled>";
   }
   return element;

}

function ShowInvoiceNote(){
	try{
		var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
		var id = jQuery("#customerInvoice_lineitems").jqGrid('getGridParam','selrow');
		row=jQuery("#customerInvoice_lineitems").getRowData(rows[id-1]);
		  var notes = row['note'];
		  console.log('notes::'+notes);
		  var cuInvoiceDetailId = row['cuInvoiceDetailId'];
		 /* var lineITemNotes = notes.replace("&And", "'");
		   areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('InvoiceLineItemNoteID');
			$(".nicEdit-main").empty();
			$(".nicEdit-main").append(lineITemNotes);*/
		  
		  	CKEDITOR.instances['InvoiceLineItemNoteID'].setData(lineITemNotes);
			jQuery("#InvoiceLineItemNote").dialog("open");
		  
			jQuery("#InvoiceLineItemNote").dialog("open");
		//	$(".nicEdit-main").focus();
			return true;
		}catch(err){
			alert(err.message);
		}
	}

	function SaveInvoiceLineItemNote(){
		var inlineText=  CKEDITOR.instances["InvoiceLineItemNoteID"].getData(); 
		var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
		var id = jQuery("#customerInvoice_lineitems").jqGrid('getGridParam','selrow');
		row=jQuery("#customerInvoice_lineitems").getRowData(rows[id-1]);
		  var notes = row['note'];
		  var cuInvoiceDetailId = row['cuInvoiceDetailId'];
		$.ajax({
			url: "./salesOrderController/saveInvoiceLineItemNote",
			type: "POST",
			data : "cuInvoiceDetailId="+cuInvoiceDetailId+"&note="+inlineText,
			success: function(data) {
				jQuery("#InvoiceLineItemNote").dialog("close");
				$("#customerInvoice_lineitems").trigger("reloadGrid");
			}
			});
	}

	function CancelInvoiceInLineNote(){
		//areaLine.removeInstance('InvoiceLineItemNoteID');
		jQuery("#InvoiceLineItemNote").dialog("close");
		return false;
	}

	jQuery(function(){
		jQuery("#InvoiceLineItemNote").dialog({
				autoOpen : false,
				modal : true,
				title:"InLine Note",
				height: 390,
				width: 635,
				buttons : {  },
				close:function(){
				//	areaLine.removeInstance('InvoiceLineItemNoteID');
					return true;
				}	
		});
	});


function formattax(){
	var subTotal = $('#customerInvoice_subTotalID').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9\-.]/g, ''));
	var tax = $('#customerInvoice_taxIdcu').val();
	tax= parseFloat(tax.replace(/[^0-9\-.]/g, ''));
	var frieght = $('#customerInvoice_frightIDcu').val();
	frieght= parseFloat(frieght.replace(/[^0-9\-.]/g, ''));
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
	
		$("#PO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
		$("#CI_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset( "destroy" );
		$("#SO_Shipto").contents().find( "#shipToRadioButtonSet" ).buttonset();
		$("#SO_Shipto").contents().find( "#shipToRadioButtonSet #shiptoaddlabel1 span" ).text("Pickup");
		
		//GlobalPage_Validation=2;
		 $("#SOlineItemGrid").jqGrid('clearGridData');
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
		
		$("#salesreleasetab").tabs({
		       disabled : false
		      });
		
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
					console.log('SalesRelease.js addressToShipCustomerInvoice'+data.address1);
					$('#customerbillToAddressID1cuInvoice').val(data.address1);
					$('#customerbillToAddressID2cuInvoice').val(data.address2);
					$('#customerbillToCitycuInvoice').val(data.city);
					$('#customerbillToStatecuInvoice').val(data.state);
					$('#customerbillToZipIDcuInvoice').val(data.zip);
					}
			});
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
	    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetSalesStatus(this.value)" value="Void"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Quote" onclick="onSetSalesStatus(this.value)" value="Quote"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Hold" onclick="onSetSalesStatus(this.value)" value="OnHold"></span><br><br><span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="Open" onclick="onSetSalesStatus(this.value)" value="Open"></span><br><span id="bookasjobsspan"><br><input type = "button" style="width: 85%;"  class="cancelhoverbutton turbo-tan" id="BookasJob" onclick="onSetSalesStatus(this.value)" value="Book as Job"><br></span><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Close" onclick="onSetSalesStatus(this.value)" value="Closed" /></span>');
	  
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 200,
			height : 250,
			title : "Select Order Status",
			open:function(){
				var sonumber=$("#SOnumberGeneral").val();
				$(newDialogDiv).css("height","208");
				$("#BookasJob").prop('disabled', false);
				$("#BookasJob").css("opacity", "1");
				if ( !isNaN(sonumber)) {
				    // Validation failed
					var cusoid=$('#Cuso_ID').text();
					$.ajax({
						url: "./salesOrderController/checkSalesOrderInvoicedornot",
						type: "POST",
						data : {"CuSOID" : cusoid},
						success: function(data) {
							if(data==true){
								$("#bookasjobsspan").css("display", "block");
								$("#BookasJob").prop('disabled', true);
								$("#BookasJob").css("opacity", "0.5");
								$(newDialogDiv).css("height","235");
							}else{
								$("#bookasjobsspan").css("display", "block");
								$(newDialogDiv).css("height","235");
							}
							}
						});
				}else{
					
					$("#bookasjobsspan").css("display", "none");
				}
					
			},
			buttons : [  ]
		}).dialog("open");
		
		$('div#showSalesOrderOptions').bind('dialogclose', function(event) {
			$("#showSalesOrderOptions").dialog("destroy").remove();
		 });
		console.log('Transaction Status dataid'+dataid);
		  if(dataid=='2'){
			  console.log('dataid inside'+dataid);
			   	$('#Close').css("font-weight","bold");
		    	$('#Close').css("background","#0E2E55");
		    	$('#withPrice').attr('checked',false);
		    	$('#withPriceLine').attr('checked',false);
		    	withPrice='NotChecked';
		    }else  if(dataid=='0'){
			   	$('#Hold').css("font-weight","bold");
		    	$('#Hold').css("background","#0E2E55");
		    	$('#withPrice').attr('checked',false);
		    	$('#withPriceLine').attr('checked',false);
		    	withPrice='NotChecked';
		    }else  if(dataid=='1'){
			   	$('#Open').css("font-weight","bold");
		    	$('#Open').css("background","#0E2E55");
		    	$('#withPrice').attr('checked',false);
		    	$('#withPriceLine').attr('checked',false);
		    	withPrice='NotChecked';
		    }else  if(dataid=='-1'){
			   	$('#Void').css("font-weight","bold");
		    	$('#Void').css("background","#0E2E55");
		    	$('#withPrice').attr('checked',false);
		    	$('#withPriceLine').attr('checked',false);
		    	withPrice='NotChecked';
		    }else  if(dataid=='-2'){
			   	$('#Quote').css("font-weight","bold");
		    	$('#Quote').css("background","#0E2E55");
		    	$('#withPrice').attr('checked',true);
		    	$('#withPriceLine').attr('checked',true);
		    	withPrice='Checked';
		    }else  if(dataid=='3'){
			   	$('#BookasJob').css("font-weight","bold");
		    	$('#BookasJob').css("background","#0E2E55");
		    	$('#withPrice').attr('checked',false);
		    	$('#withPriceLine').attr('checked',false);
		    	withPrice='NotChecked';
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
	case 'Book as Job':
		setStatus = 3;
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
	if(cuSoid==null || cuSoid=="" || cuSoid==0 || cuSoid==undefined){
		var newDialogDiv2 = jQuery(document.createElement('div'));
		var errorText = "You Should Save Before Change";
		jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
		jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Warning", 
			buttons : [ {
								height : 35,
								text : "OK",
								click : function() {
									$(this).dialog("close");
									return false;
						}
					}]
						}).dialog("open");
		
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
	/*if(setStatus == 3 && ($('#tagJobID').val()==null || $('#tagJobID').val()=="")){
		
		    var newDialogDiv2 = jQuery(document.createElement('div'));
			var errorText = "You should enter tag.";
			jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
			jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Warning", 
				buttons : [ {
									height : 35,
									text : "OK",
									click : function() {
										$(this).dialog("close");
										return false;
							}
						}]
							}).dialog("open");
		
	}*/
	$.ajax({
		url: "./salesOrderController/setSalesOrderStatus",
		type: "POST",
		async:false,
		data :{cusoID:cuSoid,status:setStatus},
		success: function(data) {
			$('#showSalesOrderOptions').dialog('destroy').remove();
			$('#transactionStatus').val(setStatus);
			$('#soStatusButton').val(e);
			if(setStatus==3 && cuSoid!=null && cuSoid!=undefined && cuSoid!=""){
				var urlData="";
				var urijobname=encodeBigurl($('#tagJobIDwz').val());
				urlData=urlData+"token=view"+"&jobNumber="+$("#SOnumberGeneral").val()+"&jobName="+urijobname+"&jobStatus='Booked'"+"&joMasterID="+data;
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?"+urlData;
				}
			
			}
			
			
		}
	});
	
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