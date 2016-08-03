var fromDate='';
var toDate='';
var uninvoicefromDate='';
var uninvoicetoDate='';
var searchData='';		
var expenseJsonString ='';
var oldformserialize='';
var oldlgserialize =''; 
var oldformserializewithoutpo = '';		
var oldlgserializewithoutpo = '';
var oldgridFormValidation=true;
var constData='0';
var vendorinvoiceoutsiderowid;

//vendorInvoiceWithPO
var global_vendorInvoicePOForm;
var global_vendorInvoicetotalPOForm;
var global_vendorInvoicegridPOForm;

//vendorInvoiceWithOutPO
var global_vendorInvoiceWOPOForm;
var global_vendorInvoicegridWOPOForm;
var global_vendorInvoicetotalWOPOForm;
var deleteveBillDetailIDDetailId=new Array();
var deleteveBillDistributionID=new Array();

var allText = $('#apacct').html();
			jQuery(document).ready(function(){
				$('#vendorAccountList').val('2');
				$('#accountsPayableImgID').hide();
				$('#mandveinvno').hide();
				
				// $("#search").css("display", "none");
				// loadInvoicesList();
				loadInvoicesList(searchData,fromDate,toDate);
				loadUninvoicesList(searchData,fromDate,toDate);
				 $(".invReason").hide();
				
				if($('#postdate').is(":checked")){
					$('#postDate1').show();
				}else{
					$('#postDate1').hide();
				}
				
				if($('#postdatePO').is(":checked")){
					$('#postDate1PO').show();
				}else{
					$('#postDate1PO').hide();
				}
				$('#fromDate').datepicker();
				$('#toDate').datepicker();
				$("#fromDate").prop('disabled',true);
				$("#toDate").prop('disabled',true);
				$("#resetbutton").css("display", "inline-block");
				$('#uninvoicefromDate').datepicker();
				$('#uninvoicetoDate').datepicker();
				$("#uninvoicefromDate").prop('disabled',true);
				$("#uninvoicetoDate").prop('disabled',true);
				
				// on change dated field from po
				
				$("#datePO").change(function(){
					
					getDueonDayswithDate($('#rxMasterIDPayablePO').val(),$("#datePO").val(),"withPO")
					
				})
				
				// on change dated field from po
				
				$("#date").change(function(){

					getDueonDayswithDate($('#rxMasterID').val(),$("#date").val(),"withoutPO")
					
				})
			});
			
			$("#postdate").change( function() {
				if($('#postdate').is(":checked")){
					$('#postDate1').show();
				}else{
					$('#postDate1').hide();
				}
			});
			
			$("#postdatePO").change( function() {
				if($('#postdatePO').is(":checked"))
				{
					$('#postDate1PO').show();
				}
				else
				{
					$('#postDate1PO').hide();
				}
			});
			
			$('#addNewVendorInvoiceFromPODlg').bind('dialogclose', function(event) {
		    	//document.location.href ="./vendorinvoicelist";
		    });
			
			$('#addNewVendorInvoiceDlg').bind('dialogclose', function(event) {
				//document.location.href ="./vendorinvoicelist";
		    });
			
			function enableVendorDate(){
				if(document.getElementById("fromDate").disabled){
				 $("#fromDate").prop('disabled',false);
				 $("#toDate").prop('disabled',false);
				}else{
					$("#fromDate").val("");
					$("#toDate").val("");
					fromDate="";toDate="";
					$("#invoicesGrid").jqGrid('GridUnload');
					if(constData=='0'){
						loadInvoicesList(searchData,fromDate,toDate);
					}else{
						loadAccountsPayableList(searchData,fromDate,toDate);
					}
					$("#fromDate").prop('disabled',true);
					$("#toDate").prop('disabled',true);
				}
			}
			

			$( "#fromDate" ).change(function() {
				fromDate = $("#fromDate").val();
				if(constData=='0'){
					$("#invoicesGrid").jqGrid('GridUnload');
					loadInvoicesList(searchData,fromDate,toDate);
					$("#invoicesGrid").trigger("reloadGrid");
				}
				else{
					loadAccountsPayableList(searchData,fromDate,toDate);
				}
				});
			
			$( "#toDate" ).change(function() {
				toDate = $("#toDate").val();
				if(constData=='0'){
					$("#invoicesGrid").jqGrid('GridUnload');
					loadInvoicesList(searchData,fromDate,toDate);
					$("#invoicesGrid").trigger("reloadGrid");
				}
				else{
					loadAccountsPayableList(searchData,fromDate,toDate);
				}
				});
			
			function getSearchDetails()
			{
				if($('#searchJob').val() != null && $('#searchJob').val() != '')
				{
					$("#searchJob").autocomplete({
						disabled: true
					});
					// $("#searchJob").attr("autocomplete", "off");
					searchData = $('#searchJob').val();
					if(constData=='0'){
						$("#invoicesGrid").jqGrid('GridUnload');
						loadInvoicesList(searchData,fromDate,toDate);
						$("#invoicesGrid").trigger("reloadGrid");
					}else{
						loadAccountsPayableList(searchData,fromDate,toDate);
					}
					$('#searchJob').val('');
					// $("#searchJob").removeAttr("autocomplete");
				}
					
			}
			var posit_outside_VendorinvoicesGrid=0;
			function loadInvoicesList(searchData,fromDate,toDate){
				$("#invoicesGridID").empty();
				$("#invoicesGridID").append("<table id='invoicesGrid'></table><div id='invoicesGridPager'></div>");
				$("#invoicesGrid").jqGrid({
					// url:'./veInvoiceBillController/getVeBillList',
					url:'./veInvoiceBillController/getVeBillList?searchData='+searchData+'&fromDate='+fromDate+'&toDate='+toDate,
					datatype: 'JSON',
					mtype: 'POST',
					pager: jQuery('#invoicesGridPager'),
					colNames:['Bill Date','Due Date','PO Number','Invoice #', 'Payble To', 'Amount', 'Paid', 'veBillId', 'rxCustomerId','vePoid','joreleaseDetailid','Check #','Date Paid',"amt",'','',''],
					colModel :[
			           	{name:'billDate', index:'billDate', align:'center', width:30, editable:true,hidden:false,formatter:dateformatter},
			           	{name:'dueDate', index:'dueDate', align:'center', width:30, editable:true,hidden:false,formatter:dateformatter},
						{name:'ponumber', index:'ponumber', align:'left', width:30,hidden:false},
						{name:'veInvoiceNumber', index:'veInvoiceNumber', align:'', width:30,hidden:false, editable:true},
						{name:'payableTo', index:'payableTo', align:'', width:100,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal; padding-left:4px;"';}},
						{name:'billAmount', index:'billAmount', align:'right', width:30,hidden:false, editable:true, formatter:customCurrencyFormatter},
						{name:'appliedAmount', index:'appliedAmount', align:'right', width:30,hidden:false, editable:true, formatter:customCurrencyFormatter},
						{name:'veBillId', index:'veBillId', align:'center', width:80, hidden:true, editable:true},
						{name:'rxCustomerId', index:'rxCustomerId', align:'center', width:40, hidden:true, editable:true},
						{name:'vePoid', index:'vePoid', align:'center', width:40, hidden:true, editable:true},
						{name:'joreleasedetailid', index:'joreleasedetailid', align:'center', width:40, hidden:true, editable:true},
						{name:'chkNo', index:'chkNo', align:'center', width:30, hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.creditUsed!="0"){  return 'style="color: red; font-weight:bold;"';}},formatter:displayChkNo},
						{name:'datePaid', index:'datePaid', align:'center', width:30, hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.creditUsed!="0"){return 'style="color: red; font-weight:bold;"';}},formatter:dateformatter},
						{name:'amt', index:'amt', align:'center', width:30, hidden:true, editable:true},
						{name:'transaction_status', index:'transaction_status', align:'center', width:40, hidden:true, editable:true},
						{name:'creditUsed', index:'creditUsed', align:'center', width:40, hidden:true, editable:true},
						{name:'rxMasterId', index:'rxMasterId', align:'', width:30,hidden:true, editable:true},
						
					],
					rowNum: 50,
					pgbuttons: true,	
					recordtext: '',
					rowList: [50, 100, 200, 500, 1000],
					viewrecords: true,
					pager: '#invoicesGridPager',
					sortname: 'billDate', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: 'Vendor Invoice',
					// autowidth: true,
					height:547,	width: 1140,/* scrollOffset:0, */ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
					loadBeforeSend: function(xhr) {
						posit_outside_VendorinvoicesGrid= jQuery("#invoicesGrid").closest(".ui-jqgrid-bdiv").scrollTop();
					},
					loadComplete: function(data) {
						
				    },
				    gridComplete: function () {
			             jQuery("#invoicesGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_VendorinvoicesGrid);
			             posit_outside_VendorinvoicesGrid=0;
					   },
					loadError : function (jqXHR, textStatus, errorThrown) {
					    
					},
					ondblClickRow: function(rowId) {
						var rowData = jQuery(this).getRowData(rowId); 
						var jobNumber = rowData['jobNumber'];
						var jobName = "" + rowData['description'];
						var jobCustomer = rowData['customerName'];
						var jobStatus = rowData['jobStatus'];
						var veBillId = rowData['veBillId'];
						var vePoid = rowData['vePoid'];
						var ponumber = rowData['ponumber'];
						var joreleasedetailId=rowData['joreleasedetailid'];
						var transaction_status=rowData['transaction_status'];
						$('#rxMasterIDPO').val(rowData['rxMasterId']);
						/*if(transaction_status==2){
							jQuery("#addNewVeInvFmDlgbutton").css("display", "none");
							jQuery("#addNewVeInvFmDlgclsbutton").css("display", "inline-block");
						}else{
							jQuery("#addNewVeInvFmDlgbutton").css("display", "inline-block");
							jQuery("#addNewVeInvFmDlgclsbutton").css("display", "none");
						}*/
						$("#VendorInvoiceTypeID").val("");
						if(vePoid != null && vePoid != ''){
							gettaxpercentagefromvePO(vePoid);
							$("#VendorInvoiceTypeID").val("WithPO");
							console.log("preloadVendorInvoiceData vepoid and ponumber :: "+vePoid+" ::  "+ponumber);
							preloadVendorInvoiceData(veBillId);
						}
						else{
							$("#TaxID_vendorinvoice").val("0");
							$("#VendorInvoiceTypeID").val("WithOutPO");
							console.log("preloadVendorInvoiceFromJobData vepoid and ponumber :: "+vePoid+" ::  "+ponumber);
							preloadVendorInvoiceFromJobData(veBillId);
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
				}).navGrid('#invoicesGridPager',// {cloneToTop:true},
					{add:false,edit:false,del:false,refresh:true,search:false}
				);
			}
			function dateformatter(cellValue, options, rowObject){
				
				if(cellValue === null){
					return "";
				}	
				
			// console.log(cellValue);
				
				if(!isNaN(cellValue))
				{
				var date = new Date(cellValue);
				var finaldate = ("0" + (date.getMonth() + 1)).slice(-2)  + "/" +("0" + date.getDate()).slice(-2) + "/" + date.getFullYear();
				return finaldate;
				}
				else
				{
				var chFind = cellValue.indexOf(',');
				var finaldate1 ="";
				if(chFind == -1)
					{
					var date = new Date(cellValue);
					finaldate1 = finaldate1 + ("0" + (date.getMonth() + 1)).slice(-2)  + "/" +("0" + date.getDate()).slice(-2) + "/" + date.getFullYear();
					return finaldate1;	
					}
				else
					{
					var dateArray = cellValue.split(",");	
					for(var i = 0;i<dateArray.length;i++)
						{
						console.log("===>"+dateArray[i])
						var date = new Date(dateArray[i]);
						finaldate1 ="Multiple";
						
					/*
					 * if(i == dateArray.length-1) finaldate1 = finaldate1 +
					 * ("0" + (date.getMonth() + 1)).slice(-2) + "/" +("0" +
					 * date.getDate()).slice(-2) + "/" + date.getFullYear();
					 * else finaldate1 = finaldate1 + ("0" + (date.getMonth() +
					 * 1)).slice(-2) + "/" +("0" + date.getDate()).slice(-2) +
					 * "/" + date.getFullYear()+",";
					 */
						}
					return finaldate1;
					}
									
				}
			}
			
			function displayChkNo(cellValue, options, rowObject)
			{

				if(cellValue==null)
				{
					return "";
				}
				else
				{
				var chFind = cellValue.indexOf(',');
				var finaldate1 ="";
				if(chFind == -1)
					{
					if(rowObject.creditUsed != "0")
						cellValue = "Credit"
						
					return cellValue;	
					}
				else
					{
					var dateArray = cellValue.split(",");	
					for(var i = 0;i<dateArray.length;i++)
						{
						console.log("===>"+dateArray[i])
						var date = new Date(dateArray[i]);
						finaldate1 ="Multiple";
						}
					return finaldate1;
					}
				}
				
			}
			
			/** Currency formatter * */
			function customCurrencyFormatter(cellValue, options, rowObject) {
				return formatCurrency(cellValue);
			}
			jQuery(function() {
				jQuery("#addNewVendorInvoiceFromPODlg").dialog({
					autoOpen : false,
					height : 1050,
					width : 980,
					title : "Add New Vendor Invoice",
					resizable: false,
					//closeOnEscape:false,
					modal : true,
					open: function( event, ui ) {
						global_vendorInvoicePOForm=$("#addNewVendorInvoiceFromPOForm").serialize();
					    global_vendorInvoicetotalPOForm=generatevendorInvoiceFormTotalIDSeriallize();
					    //$(".ui-dialog-titlebar-close").hide(); 
						deleteveBillDetailIDDetailId=new Array();
					},
					close : function() {
						// $('#userFormId').validationEngine('hideAll');
						return true;
					}
				});
			});
			jQuery(function() {
				// addcreditDebitMemosDlg
				jQuery("#addNewVendorInvoiceDlg").dialog({
					autoOpen : false,
					height : 1000,
					width : 960,
					title : "Add New Vendor Invoice",
					modal : true,
					resizable: false,
					//closeOnEscape:false,
					open: function( event, ui ) {
						//$(".ui-dialog-titlebar-close").hide(); 
						deleteveBillDistributionID=new Array();
					},
					close : function() {
						// $('#userFormId').validationEngine('hideAll');
						$("#rxMasterID").val(0);
						$("#invoicesGrid").trigger("reloadGrid");
						$("#vendorInvoiceGrid").trigger("reloadGrid");
						
						return true;
					}
				});
			});
			
			
			jQuery(function() {
				jQuery("#EnterPODlg").dialog({
					autoOpen : false,
					height : 210,
					width : 360,
					title : "Enter PO",
					modal : true,
					close : function() {
						// $('#userFormId').validationEngine('hideAll');
						return true;
					}
				});
			});
			jQuery(function() {
				jQuery("#InvalidPODlg").dialog({
					autoOpen : false,
					height : 180,
					width : 400,
					title : "Enter PO",
					modal : true,
					close : function() {
						// $('#userFormId').validationEngine('hideAll');
						return true;
					}
				});
			});
			function ok() {
				var po = $('#po').val();
				var formData = 'po='+po;
				$("#VendorInvoiceTypeID").val("");
				if(po != null && po != ' ' && po.length > 0)
				{
					$("#VendorInvoiceTypeID").val("WithPO");
					$.ajax({
				        url: './veInvoiceBillController/getPO',
				        type: 'POST',  
				        data: formData,    
				        success: function (data) {
				        	
							  if(data.Registered != null && data.Registered != ' ' &&
							  data.Registered.length > 0) {
							  
							  console.log("Test");
							  jQuery("#EnterPODlg").dialog("close");
							  $('#InvalidPOMsg').text('All item has been invoiced for the PO #'+po);
							  jQuery("#InvalidPODlg").dialog("open"); return
							  true;
							   } else{
							 
				        		 
				        	
				        	console.log("No PO  :: "+data);
				        	console.log('Length :: '+data.length);
					        if(data.Vepo.ponumber != null && data.Vepo.ponumber != ' ' && data.Vepo.ponumber.length > 0)
					        {
					    	
					        	if(document.getElementById("addNewVendorInvoiceFromPOForm")!=null)
					        	document.getElementById("addNewVendorInvoiceFromPOForm").reset();
					        	$('#rxMasterIDPO').val("");
					        	$('#rxMasterIDPayablePO').val("");
					        	$('#vepoidPO').val("");
					        	$('#veBillIdPO').val("");
					        	$("#invStatus").val("");
					        	
					        	console.log("PO Exist :: "+data.Vepo.ponumber);
					        	console.log("Address1 :: "+data.vendorAddress.address1);
					        	
					        	if(data.veInvnoandatory == 1)
					        	{
					        	$('#veInvnomandatory').css({"display":"inherit"})
					        	$('#veInvnomandatory').attr("data-manvalue",data.veInvnoandatory);
					        	}
					        	else
					        	{
					        	$('#veInvnomandatory').css({"display":"none"})
					        	$('#veInvnomandatory').attr("data-manvalue",data.veInvnoandatory);
					        	}
					        		
					        	$('#ponumberPO').text(data.Vepo.ponumber);
					        	$('#vepoidPO').val(data.Vepo.vePoid);
					        	$('#shipviaPO').val(data.Vepo.veShipViaId);
					        	$('#freightGeneralId').val(formatCurrencynodollar(data.Vepo.freight));
								$('#taxGeneralId').val(formatCurrencynodollar(data.Vepo.taxTotal));					        	
								$('#vendorAddressPO').html(data.vendorAddress.address1);
								$('#vendorAddress1PO').html(data.vendorAddress.city).append(data.vendorAddress.state).append(data.vendorAddress.zip);
								$('#payablePO').val(data.vendorName.name);
								$('#rxMasterIDPO').val(data.Vepo.rxVendorId)
								$('#rxMasterIDPayablePO').val(data.Vepo.rxVendorId);
								$('#apacctPO').val(data.SysAccountLinkage.coAccountIdap).attr('selected', true);
								$('#jobName').text(data.Vepo.updateKey);
					        	jQuery("#EnterPODlg").dialog("close");								
								loadlineItemGrid();
								jQuery("#addNewVendorInvoiceFromPODlg").dialog("open");
								
								var d = new Date($("#duePO").val());
								d.setDate(Number(d.getDate())+Number(data.dueonDaysPO));
								$("#duedaysfmpo").val(data.dueonDaysPO);
								
							/*	var day = ("0" + d.getDate()).slice(-2);
								var month = ("0" + (d.getMonth() + 1)).slice(-2);
								var today = (month)+"/"+(day)+"/"+d.getFullYear();*/
								
								//$("#duePO").val(today);
								FormatDueDate(d);
								
								return true;			        
					        }
					        else
					        {
					        	$("#invStatus").val("");
					        	console.log("Test");
					        	jQuery("#EnterPODlg").dialog("close");
					        	$('#InvalidPOMsg').text('Cannot find purchase order #'+po);
					        	jQuery("#InvalidPODlg").dialog("open");
								return true;					        	
					        }        
				        	 }
							
				        }
				    });
				}
				else
				{
					$("#VendorInvoiceTypeID").val("WithOutPO");
					jQuery("#EnterPODlg").dialog("close");
					document.getElementById("addNewVendorInvoiceForm").reset();
					
					$.ajax({
				        url: './veInvoiceBillController/getApNumber',
				        type: 'POST',  				 
				        success: function (data) {
				        	$('#apacct').val(data.SysAccountLinkage.coAccountIdap).attr('selected', true);
				        }
					});
					document.getElementById("addNewVendorInvoiceForm").reset();
					$('#veBillIdJob').val('0');
					loadNewVendorInvoice();
					$("#vendorInvoiceGrid").trigger("reloadGrid");
					jQuery("#addNewVendorInvoiceDlg").dialog("open");
					//added by prasant for BID#1472
					//$('#vendorInvoiceGrid').jqGrid('clearGridData');
					$('#payable').val("");
					$('#vendorAddress').html("");
					$('#vendorAddress1').html("");
					$('#viStatusButton').val("Open");
					return true;
				}
				
				
			}
			function closeMsg()
			{
				jQuery("#InvalidPODlg").dialog("close");
				return true;
			}
			function cancel()
			{
				jQuery("#EnterPODlg").dialog("close");
				return true;
			}

			function addNewVendorInvoice() {
				document.getElementById("poForm").reset();
				jQuery("#EnterPODlg").dialog("open");
				return true;
			}
			var posit_outside_loadNewVendorInvoice=0;
			/*function loadNewVendorInvoice(){
				
				var linegridstatus=true;
				
				var vepoID = $('#veBillIdJob').val();
				try{
				$("#vendorInvoiceGrid").jqGrid({										
					datatype: 'JSON',
					mtype: 'POST',
					pager: jQuery('#vendorInvoicePager'),
					url:'./veInvoiceBillController/getVendorInvoice',					
					postData: {vepoID: function() { return $('#veBillIdJob').val(); }},
					colNames:['Account #','Account Description','Job #', 'Amount','veBillDistributionId','veBillId','coExpenseAccountId','joMasterId'],
					colModel :[
			           	{name:'number', index:'number', align:'left', width:20,hidden:false, editable:true},
						{name:'desc', index:'desc', align:'', width:20,hidden:false, editable:true},
						{name:'jobNumber', index:'jobNumber', align:'', width:30,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal; padding-left:4px;"';}},
						{name:'expenseAmount', index:'expenseAmount', align:'left', width:20,hidden:false, editable:true, formatter:customCurrencyFormatter},
						{name:'veBillDistributionId', index:'veBillDistributionId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
						{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
						{name:'coExpenseAccountId', index:'coExpenseAccountId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
						{name:'joMasterId', index:'joMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
						
						
					],
					rowNum: 50,
					pgbuttons: true,	
					recordtext: '',
					rowList: [50, 100, 200, 500, 1000],
					viewrecords: true,
					pager: '#vendorInvoicePager',
					sortname: 'billDate', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
					// autowidth: true,
					height:247,	width: 850, scrollOffset:0,  rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
					loadBeforeSend: function(xhr) {
						posit_outside_loadNewVendorInvoice= jQuery("#vendorInvoiceGrid").closest(".ui-jqgrid-bdiv").scrollTop();
					},
					loadComplete: function(data) {

						$("#vendorInvoiceGrid").setSelection(1, true);
						var allRowsInGrid = $('#vendorInvoiceGrid').jqGrid('getRowData');
						var aVal = new Array(); 
						var aTax = new Array();
						var sum = 0;
						var taxAmount = 0;
						var aTotal = 0;
						$.each(allRowsInGrid, function(index, value) {
							aVal[index] = value.expenseAmount;
							var number1 = aVal[index].replace("$", "");
							var number2 = number1.replace(".00", "");
							var number3 = number2.replace(",", "");
							var number4 = number3.replace(",", "");
							var number5 = number4.replace(",", "");
							var number6 = number5.replace(",", "");
							sum = Number(sum) + Number(number6); 
						});
						
						// formatCurrencynodollar
						$('#totalDist').val(formatCurrencynodollar(sum));
						$('#total').val(formatCurrencynodollar(sum));
						$('#balDue').val(formatCurrencynodollar(sum));		
						
				    },
				    gridComplete: function () {
			             jQuery("#vendorInvoiceGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_loadNewVendorInvoice);
			             posit_outside_loadNewVendorInvoice=0;
					 },
					loadError : function (jqXHR, textStatus, errorThrown) {
					    
					},
					onSelectRow:  function(id){
						var rowData = jQuery(this).getRowData(id); 
						var veBillDistributionId = rowData["veBillDistributionId"];
						var coExpenseAccountId = rowData["coExpenseAccountId"];
						var joMasterId = rowData["joMasterId"];
						$('#coAccountID').val(coExpenseAccountId);
						$('#joMasterID').val(joMasterId);
						console.log("veBillDistributionId :: "+veBillDistributionId);
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
			    	editurl:"./veInvoiceBillController/manpulateVendorInvoiceLineItemFromJob"
				}).navGrid('#vendorInvoicePager',// {cloneToTop:true},
					{add:linegridstatus,edit:linegridstatus,del:linegridstatus,refresh:linegridstatus,search:false},
					// -----------------------edit
					// options----------------------//
					{
			 	width:515, left:400, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Product",
				beforeInitData:function(formid) {
					if($('#rxMasterID').val()=== null || $('#rxMasterID').val()==='')
					{
						$('#errorMsg').show();
						$('#errorMsg').html('<span style="color:red;">*Payable is mantatory</span>');
						return false; 
					}			
					else
						{
						$('#errorMsg').hide();
						}
				},
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();
					jQuery('#TblGrid_lineItemGrid #tr_number .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_number .CaptionTD').append('Account #: ');
					jQuery('#TblGrid_lineItemGrid #tr_number .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_desc .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_desc .CaptionTD').append('Account Description123: ');
					jQuery('#TblGrid_lineItemGrid #tr_jobNumber .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_jobNumber .CaptionTD').append('Job #: ');
					jQuery('#TblGrid_lineItemGrid #tr_jobNumber .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_expenseAmount .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_expenseAmount .CaptionTD').append('Amount: ');				
					 
				},
				afterShowForm: function($form) {
					$('#number').attr('placeholder','Minimum 2 character required to get Name List');
					$('#jobNumber').attr('placeholder','Minimum 2 character required to get Name List');
					$('#expenseAmount').val($('#expenseAmount').val().replace(/[^0-9-.]/g, ''));
					$(function() { var cache = {}; var lastXhr=''; $("input#number").autocomplete({minLength: 2,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "./veInvoiceBillController/getCoAccountDetails", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){
							var ID = ui.item.id; var product = ui.item.label;$("#coAccountID").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $('#number').val(pro); $("#desc").val(pro2);}
							
							},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
						}); 
					return;
					});
					$(function() { var cache = {}; var lastXhr=''; $("input#jobNumber").autocomplete({minLength: 2,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "./veInvoiceBillController/getJobList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){
							var ID = ui.item.id; var product = ui.item.label;$("#joMasterID").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#jobNumber").val(pro2);}
							
							},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
						}); 
					return;
					});
				},
				beforeSubmit:function(postdata, formid) {
					$("#note").autocomplete("destroy"); 
					$(".ui-menu-item").hide();
					var aCoAccountID = $('#coAccountID').val();
					// var aJoMasterID = $('#joMasterID').val();
					if (aCoAccountID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; }
					// if (aJoMasterID === ""){ return [false, "Alert: Please
					// provide a valid Product (Select from suggest dropdown
					// list)."]; }
					
					var gridRows = $('#vendorInvoiceGrid').getRowData();
					var rowData = new Array();
					for (var i = 0; i < gridRows.length; i++) {
						var row = gridRows[i];
						rowData.push($.param(row));
						console.log($.param(row));
						}
					expenseJsonString = JSON.stringify(gridRows);
					return [true, ""];
				},
				onclickSubmit: function(params){
					var rxMasterId = $("#rxMasterID").val();
					var recDateId =$('#recDateId').val();
					var date = $('#date').val();
					var due = $('#due').val();
					var vendorInvoice = $('#vendorInvoice').val();
					var apacct = $('#apacct').val();							
					var postdate;							
					if($('#postdate').is(":checked"))
					{
						postdate = "on";
					}
					else
					{
						postdate = "off";
					}
					var postDate1 = $('#postDate1').val();
					var pro = $('#proPO').val();
					var totalDist = $('#totalDist').val().replace(/[^0-9-.]/g, '');
					var total = $('#total').val().replace(/[^0-9-.]/g, '');
					var balDue = $('#balDue').val().replace(/[^0-9-.]/g, '');
					var veBillId = $('#veBillIdJob').val();
					var joMasterID = $("#joMasterID").val();
					var coAccountID = $("#coAccountID").val();
					var buttonValue = $('#viStatusButton').val();
					return { 'recDateId' : recDateId, 'date' : date,'due':due,'vendorInvoice' : vendorInvoice
						, 'apacct' : apacct,'postdate':postdate, 'postDate1' : postDate1
						, 'pro' : pro,'totalDist':totalDist, 'total' : total,'balDue':balDue,'veBillId' : veBillId,'oper' : 'edit','rxMasterId' : rxMasterId
						,'joMasterID' : joMasterID,'coAccountID' : coAccountID,'buttonValue' : buttonValue};
				},
				afterSubmit:function(response,postData){
					$("#note").autocomplete("destroy"); 
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					
					 return [true, loadNewVendorInvoice()];
				}
			

					},
					// -----------------------add
					// options----------------------//
					{
						width:550, left:400, top: 300, zIndex:1040,
						closeAfterAdd:true,	reloadAfterSubmit:true,
						modal:true, jqModel:false,
						addCaption: "Add Product",
						onInitializeForm: function(form){
							
						},
						beforeInitData:function(formid) {
							if($('#rxMasterID').val()=== null || $('#rxMasterID').val()==='')
							{
								$('#errorMsg').show();
								$('#errorMsg').html('<span style="color:red;">*Payable is mantatory</span>');
								return false; 
							}			
							else
								{
								$('#errorMsg').hide();
								}
						},
						afterShowForm: function($form) {
							$('#number').attr('placeholder','Minimum 2 character required to get Name List');
							$('#jobNumber').attr('placeholder','Minimum 2 character required to get Name List');
							$(function() { var cache = {}; var lastXhr=''; $("input#number").autocomplete({minLength: 2,timeout :1000,
								source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
									lastXhr = $.getJSON( "./veInvoiceBillController/getCoAccountDetails", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
								select: function( event, ui ){
									var ID = ui.item.id; var product = ui.item.label;$("#coAccountID").val(ID);
									if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $('#number').val(pro); $("#desc").val(pro2);}
									
									},
								error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
								}); 
							return;
							});
							$(function() { var cache = {}; var lastXhr=''; $("input#jobNumber").autocomplete({minLength: 2,timeout :1000,
								source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
									lastXhr = $.getJSON( "./veInvoiceBillController/getJobList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
								select: function( event, ui ){
									var ID = ui.item.id; var product = ui.item.label;$("#joMasterID").val(ID);
									if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#jobNumber").val(pro2);}
									
									},
								error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
								}); 
							return;
							});
						},
						beforeSubmit:function(postdta, formid) {
							$("#note").autocomplete("destroy");
							$(".ui-menu-item").hide();
							var aCoAccountID = $('#coAccountID').val();
							// var aJoMasterID = $('#joMasterID').val();
							if (aCoAccountID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; }
							// if (aJoMasterID === ""){ return [false, "Alert:
							// Please provide a valid Product (Select from
							// suggest dropdown list)."]; }
							
							return [true, ""];
						},
						onclickSubmit: function(params){
							var rxMasterId = $("#rxMasterID").val();
							var recDateId =$('#recDateId').val();
							var date = $('#date').val();
							var due = $('#due').val();
							var vendorInvoice = $('#vendorInvoice').val();
							var apacct = $('#apacct').val();							
							var postdate;							
							if($('#postdate').is(":checked"))
							{
								postdate = "on";
							}
							else
							{
								postdate = "off";
							}
							var postDate1 = $('#postDate1').val();
							var pro = $('#proPO').val();
							var totalDist = $('#totalDist').val().replace(/[^0-9-.]/g, '');
							var total = $('#total').val().replace(/[^0-9-.]/g, '');
							var balDue = $('#balDue').val().replace(/[^0-9-.]/g, '');
							var veBillId = $('#veBillIdJob').val();
							if(veBillId == null || veBillId == '')
								veBillId = 0;
							var joMasterID = $("#joMasterID").val();
							var coAccountID = $("#coAccountID").val();
							var buttonValue = $('#viStatusButton').val();
							return { 'recDateId' : recDateId, 'date' : date,'due':due,'vendorInvoice' : vendorInvoice
								, 'apacct' : apacct,'postdate':postdate, 'postDate1' : postDate1
								, 'pro' : pro,'totalDist':totalDist, 'total' : total,'balDue':balDue,'veBillId' : veBillId,'oper' : 'add','rxMasterId' : rxMasterId
								,'joMasterID' : joMasterID,'coAccountID' : coAccountID,'buttonValue' : buttonValue};
						},
						afterSubmit:function(response,postData){
							$("#note").autocomplete("destroy");
							$(".ui-menu-item").hide();
							$("a.ui-jqdialog-titlebar-close").show();
							var row = JSON.parse(response.responseText);
							console.log("Vendor Invoice Rows---->"+row);
							console.log("loadNewVendorInvoice veBillId --->"+row.veBillId);
							$('#veBillIdJob').val(row.veBillId);
													
							return [true, loadNewVendorInvoice()];
						}
					},
					// -----------------------Delete
					// options----------------------//
					{	
						closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
						caption: "Delete Product",
						msg: 'Delete the Product Record?',

						onclickSubmit: function(params){
							var veBillId = $('#veBillIdJob').val();
							var id = jQuery("#vendorInvoiceGrid").jqGrid('getGridParam','selrow');
							var key = jQuery("#vendorInvoiceGrid").getCell(id, 5);// 'veBillDetailId'
																					// :
																					// key,
							var joMasterID = $("#joMasterID").val();
							return {'veBillDistributionId' : key,'oper' : 'delete','veBillId' : veBillId,'joMasterID' : joMasterID};
							
						},
						afterSubmit:function(response,postData){
							 
							 return [true, loadNewVendorInvoice()];
						}
					}
				);
			}
			catch(err) {
		        var text = "There was an error on this page.\n\n";
		        text += "Error description: " + err.message + "\n\n";
		        text += "Click OK to continue.\n\n";
		        console.log(text);
		    }
			
			if($('#viStatusButton').val()=="Paid")
			{
			$("#add_vendorInvoiceGrid").addClass("ui-state-disabled");
			$("#edit_vendorInvoiceGrid").addClass("ui-state-disabled");
			$("#del_vendorInvoiceGrid").addClass("ui-state-disabled");
			$("#refresh_vendorInvoiceGrid").addClass("ui-state-disabled");
			$("#paidStatus").css("display","inline");
			 var selrowid = $("#invoicesGrid").jqGrid('getGridParam','selrow');
			$("#chk_nofmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'chkNo'));
			$("#date_paidfmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'datePaid'));
			$('#saveTermsButton').css("display","none");
			$('#addNewVeInvFmDlgclsbuttonwithoutpo').css("display","inline");
		// $('.addinv').attr("disabled",true);
			$('#viStatusButton').attr("disabled",true);
			}
			}*/

			$(function() { var cache = {}; var lastXhr='';
			$("#payable").autocomplete({ minLength: 1,timeout :1000,
				/*
				 * open: function(){ $(".ui-autocomplete").prepend('<div
				 * style="font-size: 15px;"><b><a
				 * href="./inventoryDetails?token=new"
				 * style="color:#3E8DC6;font-family:
				 * Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New
				 * Inventory</a></b></div>');
				 * $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
				 */
				select: function (event, ui) {
					var aValue = ui.item.value;
					var valuesArray = new Array();
					valuesArray = aValue.split("|");
					var id = valuesArray[0];
					var code = valuesArray[2];
					$("#rxMasterID").val(aValue);						
					var sManu = '';
					var sAttn = '<option value="-1"> - Select - </option>';
					 $.ajax({
					        url: './getManufactureATTN?rxMasterId='+aValue,
					        type: 'POST',       
					        success: function (data) {

					        	$.each(data, function(key, valueMap) {									
									if("vendorAddress"==key)
									{
										$.each(valueMap, function(index, value){
											$('#vendorAddress').html(value.address1);
											$('#vendorAddress1').html(value.city).append(value.state).append(value.zip);						
										}); 
										
									}

									if("vendorName"==key)
									{
										$.each(valueMap, function(index, value){
											$('#payable').val(value.name);
										}); 
									}
									
									if("dueonDays" == key)
										{
										$("#duedaysfmdr").val(valueMap);
										var d = new Date($("#date").val());
										d.setDate(Number(d.getDate())+Number(valueMap));
										var day = ("0" + d.getDate()).slice(-2);
										var month = ("0" + (d.getMonth() + 1)).slice(-2);
										var today = (month)+"/"+(day)+"/"+d.getFullYear();
										$("#due").val(today);
										}
									
								});
					        	//alert($('#payable').val());
					        	//$( "#vendorInvoiceGrid_iladd" ).trigger( "click" );

					        }
					    }); 
					 $('#errorMsg').hide();
					// location.href="./inventoryDetails?token=view&inventoryId="+id
					// + "&itemCode=" + code;
					 getDefaultAccountNumber(aValue);
				},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "search/searchVendor", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} });
			});
			
			$(function() { var cache = {}; var lastXhr='';
			$("#payablePO").autocomplete({ minLength: 1,timeout :1000,
				/*
				 * open: function(){ $(".ui-autocomplete").prepend('<div
				 * style="font-size: 15px;"><b><a
				 * href="./inventoryDetails?token=new"
				 * style="color:#3E8DC6;font-family:
				 * Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New
				 * Inventory</a></b></div>');
				 * $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
				 */
				select: function (event, ui) {
					var aValue = ui.item.value;
					var valuesArray = new Array();
					valuesArray = aValue.split("|");
					var id = valuesArray[0];
					var code = valuesArray[2];
					$("#rxMasterIDPayablePO").val(aValue);						
					var sManu = '';
					var sAttn = '<option value="-1"> - Select - </option>';
					 $.ajax({
					        url: './getManufactureATTN?rxMasterId='+aValue,
					        type: 'POST',       
					        success: function (data) {

					        	$.each(data, function(key, valueMap) {									
									if("vendorAddress"==key)
									{
										$.each(valueMap, function(index, value){
											$('#vendorAddressPO').html(value.address1);
											$('#vendorAddress1PO').html(value.city).append(value.state).append(value.zip);						
										}); 
										
									}

									if("vendorName"==key)
									{
										$.each(valueMap, function(index, value){
											$('#payablePO').val(value.name);
										}); 
										
									}	
									
									if("dueonDays" == key)
									{
									$("#duedaysfmpo").val(valueMap);
									var d = new Date($("#datePO").val());
									d.setDate(Number(d.getDate())+Number(valueMap));
									var day = ("0" + d.getDate()).slice(-2);
									var month = ("0" + (d.getMonth() + 1)).slice(-2);
									var today = (month)+"/"+(day)+"/"+d.getFullYear();
									$("#duePO").val(today);
									}
								
									
								});
					        	
								
					        }
					    }); 
					
					// location.href="./inventoryDetails?token=view&inventoryId="+id
					// + "&itemCode=" + code;
				},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "search/searchVendor", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} });
			});

			function changeapacct()
			{
				var selectedText = $('#apacct option:selected').text();
				var selectedValue = $('#apacct option:selected').val();
				var arr = selectedText.split('|');
				$('#apacct option:selected').text($.trim(arr[0]));
				$('#apacct').empty();
			    var newOption = $('<option value="'+selectedValue+'" selected="selected">'+$.trim(arr[0])+'</option>');
			    $('#apacct').append(newOption);
			    $('#apacct').append(allText);
			}

			function addVendorInvoice(operation)
			{
				
				if($('#rxMasterID').val() == null || $('#rxMasterID').val() == '')
				{
					$('#errorMsg').show();
					$('#errorMsg').html('<span style="color:red;">*Payable is mantatory</span>');
					
					$("#saveTermsButton").attr("disabled",false);
					$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
					
					return false;
				}
				if($("#invStatus").val()=="addNewVendorInvoiceDlg")
					{
					
					var newformserializewithoutpo = $('#addNewVendorInvoiceForm').serialize();
					var newlgserializewithoutpo = jQuery("#vendorInvoiceGrid").jqGrid('getRowData');
					comparegridwithoutpo=comparetwogriddata(oldlgserializewithoutpo,newlgserializewithoutpo);
						if((oldformserializewithoutpo==newformserializewithoutpo)&&comparegridwithoutpo)
						{
						//$('#addNewVendorInvoiceDlg').dialog("close");
						oldformserializewithoutpo ="";
						oldlgserializewithoutpo = "";
						
						$("#saveTermsButton").attr("disabled",false);
						$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
						$("#vendorinvoiceWoPO").html("Saved.");
						setTimeout(function(){
							$("#vendorinvoiceWoPO").html("");					
							},2000);
						
						}else{
							editInvoiceDetails(operation);
						}
				
					}
				else
					{
					

					var rows = jQuery("#vendorInvoiceGrid").getDataIDs();
					var deleteinvoiceDetailId=new Array();
		 			/* for(var a=0;a<rows.length;a++)
		 			 {
		 			    row=jQuery("#vendorInvoiceGrid").getRowData(rows[a]);
		 			   var id="#canDoWOID_"+rows[a];
		 			   var canDo=$(id).is(':checked');
			 			   if(canDo){
			 				  var veBillDistributionId=row['veBillDistributionId'];
			 				  if(veBillDistributionId!=undefined && veBillDistributionId!=null && veBillDistributionId!="" && veBillDistributionId!=0){
			 				 		deleteinvoiceDetailId.push(veBillDistributionId);
			 				 	}
			 				 $('#vendorInvoiceGrid').jqGrid('delRowData',rows[a]);
			 			   }
		 			   }*/
		 			var gridRows = $('#vendorInvoiceGrid').getRowData();
					var dataToSend = JSON.stringify(gridRows);
					if($('#viStatusButton').val()=="Paid")
					{
					$('#viStatusButton').attr("disabled",true);
					}
					var totalDist1 = $('#totalDist').val().replace(/[^0-9-.]/g, '');
					var total1 = $('#total').val().replace(/[^0-9-.]/g, '');
					var balDue1 = $('#balDue').val().replace(/[^0-9-.]/g, '');
					var reasonVal=$("textarea#invreasonttextid").val();
					var buttonValue=$('#viStatusButton').val();
					
					var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
						$.ajax({
							url: "./checkAccountingCyclePeriods",
							data:{"datetoCheck":$("#date").val(),"UserStatus":checkpermission},
							type: "POST",
							success: function(data) { 

								if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
								{
									periodid=data.cofiscalperiod.coFiscalPeriodId;
									yearid = data.cofiscalperiod.coFiscalYearId;
								
									var formData = $('#addNewVendorInvoiceForm').serialize();
									var data = formData;
									

									console.log(data);
									
										 $.ajax({
									        url: './veInvoiceBillController/addVendorInvoice?'+data,
									        type: 'POST',     
									        data: {"buttonValue":buttonValue,"reason":reasonVal,
									        	"totalDist1":totalDist1,"total1":total1,"balDue1":balDue1,
									        	"gridData":dataToSend,
									        	"delData":deleteveBillDistributionID,"coFiscalPeriodId":periodid,"coFiscalYearId":yearid},
									        success: function (data) {
									        	deleteveBillDistributionID==new Array();
									        	createtpusage('Company-Vendors-Invoices & Bills','Save Vendor Invoice','Info','Company-Vendors-Invoices & Bills,Saving Vendor Invoice,rxMasterID:'+$('#rxMasterID').val());
												console.log(data);
//												if(document.getElementById("BillPayVendorInvoice")==undefined
//														|| document.getElementById("BillPayVendorInvoice")==null){
//													document.location.href ="./vendorinvoicelist";
//												}else{
												$("#vendorinvoiceWoPO").html("Saved.");
												$('#veBillIdJob').val(data);
												$("#vendorInvoiceGrid").trigger("reloadGrid");
												setTimeout(function(){
													$("#vendorinvoiceWoPO").html("");					
													},2000);
												
													if(operation=='close'){
													$("#addNewVendorInvoiceDlg").dialog("close");
													}
													$("#vendorBills").trigger("reloadGrid");
//												}
												
												return true;
									        }
									    }); 
								}
								else
									{
									
									if(data.AuthStatus == "granted")
									{	
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
															buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
														}).dialog("open");
									}
									else
									{
										showDeniedPopupvendorinvoice();
									}
									}
						  	},
				   			error:function(data){
				   				console.log('error');
				   				}
				   			});
					}
			}
			
			
			function  addNewVendorInvoiceDlgwithReason(oper)
			{
				/*var rows = jQuery("#vendorInvoiceGrid").getDataIDs();
				var deleteinvoiceDetailId=new Array();
	 			 for(var a=0;a<rows.length;a++)
	 			 {
	 			    row=jQuery("#vendorInvoiceGrid").getRowData(rows[a]);
	 			   var id="#canDoWOID_"+rows[a];
	 			   var canDo=$(id).is(':checked');
		 			   if(canDo){
		 				  var veBillDistributionId=row['veBillDistributionId'];
		 				  if(veBillDistributionId!=undefined && veBillDistributionId!=null && veBillDistributionId!="" && veBillDistributionId!=0){
		 				 		deleteinvoiceDetailId.push(veBillDistributionId);
		 				 	}
		 				 $('#vendorInvoiceGrid').jqGrid('delRowData',rows[a]);
		 			   }
	 			   }*/
	 			var gridRows = $('#vendorInvoiceGrid').getRowData();
				var dataToSend = JSON.stringify(gridRows);
				
				if($('#viStatusButton').val()=="Paid")
				{
					// $('.savehoverbutton').attr("disabled",true);
				$('#viStatusButton').attr("disabled",true);
				
				}
				var formData = $('#addNewVendorInvoiceForm').serialize();
				var totalDist1 = $('#totalDist').val().replace(/[^0-9-.]/g, '');
				var total1 = $('#total').val().replace(/[^0-9-.]/g, '');
				var balDue1 = $('#balDue').val().replace(/[^0-9-.]/g, '');
				var reasonVal=$("textarea#invreasonttextid").val();
		      	var buttonValue=$('#viStatusButton').val()
				
				var data = formData
				/*+'&buttonValue='+buttonValue+'&reason='+reasonVal+
				'&totalDist1='+totalDist1+'&total1='+total1+'&balDue1='+balDue1;*/
				//var data1=JSON.stringify(data);
				console.log(data);
				//console.log(data1);
			    // alert("reason value"+reasonVal);
				
				var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
				$.ajax({
					url: "./checkAccountingCyclePeriods",
					data:{"datetoCheck":$("#date").val(),"UserStatus":checkpermission},
					type: "POST",
					success: function(dataObj) { 
						
						if(dataObj.cofiscalperiod!=null && typeof(dataObj.cofiscalperiod.period) !== "undefined" )
						{
							periodid=dataObj.cofiscalperiod.coFiscalPeriodId;
							yearid = dataObj.cofiscalperiod.coFiscalYearId;
							
						/*	var data = formData+'&buttonValue='+buttonValue+'&reason='+reasonVal+
							'&totalDist1='+totalDist1+'&total1='+total1+'&balDue1='+balDue1;*/
								 $.ajax({
							        url: "./veInvoiceBillController/addVendorInvoice?"+data,
							        type: 'POST', 
							        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
							       
							        data: {"buttonValue":buttonValue,"reason":reasonVal,
							        	  "totalDist1":totalDist1,"total1":total1,"balDue1":balDue1,
							        	  "gridData":dataToSend,
							        	  "delData":deleteveBillDistributionID,"coFiscalPeriodId":periodid,"coFiscalYearId":yearid},
							        
							        success: function (data) {
							        	deleteveBillDistributionID=new Array();
										console.log(data);
										/*if(document.getElementById("BillPayVendorInvoice")==undefined
												|| document.getElementById("BillPayVendorInvoice")==null){
											document.location.href ="./vendorinvoicelist";
										}else{*/
										$("#vendorinvoiceWoPO").html("Saved.");
										setTimeout(function(){
											$("#vendorinvoiceWoPO").html("");					
											},2000);
										$("#vendorInvoiceGrid").trigger("reloadGrid");
										
											if(oper=="close"){
												$("#invoicesGrid").trigger("reloadGrid");
											$("#addNewVendorInvoiceDlg").dialog("close");
											}
											$("#vendorBills").trigger("reloadGrid");
										//}
										return true;
							        }
							    }); 
						}
						else
						{
							
							if(dataObj.AuthStatus == "granted")
							{	
							var newDialogDiv = jQuery(document.createElement('div'));
							jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
													buttons: [{text: "OK",click: function(){$(this).dialog("close");
													$("#invoicesGrid").trigger("reloadGrid");
													$("#addNewVendorInvoiceDlg").dialog("close");
													
													
													}}]
												}).dialog("open");
							}
							else
							{
								showDeniedPopupvendorinvoice();
							}
						}
				  	},
		   			error:function(data){
		   				console.log('error');
		   				}
		   			});		 
								 
								 
			}
			
			function addVendorInvoiceFromPO(operatorStatus)
			{
				var newform=checkFormValidation();
				if($('#rxMasterIDPO').val() == null || $('#rxMasterIDPO').val() == '')
				{
					$('#errorMsg').show();
					$('#errorMsg').html('<span style="color:red;">*Payable is mantatory</span>');
					return false;
				}
				
				if($("#invStatus").val()=="addNewVendorInvoiceFromPODlg" )
				{
							if(operatorStatus == "close")
							{
								var newlgserialize = jQuery("#lineItemGrid").jqGrid('getRowData');
								var comparegrid=comparetwogriddata(oldlgserialize,newlgserialize);
								
								console.log("=========================")
								console.log(oldlgserialize); 
								console.log("=========================")
								console.log(newlgserialize); 
								console.log("=========================")
								console.log(oldformserialize);
								console.log("=========================")
								console.log(newform);
								console.log("=========================")
								
								if((oldformserialize==newform)&&comparegrid){
									
									$('#addNewVendorInvoiceFromPODlg').dialog("close");
									oldformserialize ="";
									oldlgserialize = "";
									
									
								}else{
									editInvoiceDetails(operatorStatus);
								}
							}
							else
							{
								$("#saveStatus").html("Saved.");
								setTimeout(function(){
									$("#saveStatus").html("");					
									},2000);
							}
				}
				else
				{
													
							if($('#viStatusButtonPO').val()=="Paid")
							{
							document.getElementById("saveTermsButton").disabled = true;
							$('#viStatusButtonPO').attr("disabled",true);
							}
											
							var formData = $('#addNewVendorInvoiceForm').serialize();
							console.log(formData);
							var recDateIdPO =$('#recDateIdPO').val();
							var datePO = $('#datePO').val();
							var duePO = $('#duePO').val();
							var shipviaPO = $('#shipviaPO').val();
							var vendorInvoicePO = $('#vendorInvoicePO').val();
							var apacctPO = $('#apacctPO').val();
							var postdatePO;
							
							if($('#postdatePO').is(":checked"))
							{
								postdatePO = "on";
							}
							else
							{
								postdatePO = "off";
							}
							var postDate1PO = $('#postDate1PO').val();
							var shipDatePO = $('#shipDatePO').val();
							var proPO = $('#proPO').val();
							var subtotalGeneralId = $('#subtotalGeneralId').val().replace(/[^0-9-.]/g, '');
							var freightGeneralId = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
							var taxGeneralId = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
							var totalGeneralId = $('#totalGeneralId').val().replace(/[^0-9-.]/g, '');
							var balDuePO = $('#balDuePO').val().replace(/[^0-9-.]/g, '');
							// freight = parseFloat(freight.replace(/[^0-9-.]/g,
							// ''));
							var vepoId = $('#vepoidPO').val();
							var veBillIdPO = $('#veBillIdPO').val();
							var prMasterIDPO = $('#rxMasterIDPO').val();
							var rxMasterIDPayablePO = $('#rxMasterIDPayablePO').val();
							var oper = 'add';
						var reasonVal=	$("textarea#invreasonttextid").val()
							console.log("veBillId---->"+veBillIdPO);
							if(veBillIdPO == null || veBillIdPO == '')
								veBillIdPO = 0;				
							var generalValues =   'recDateIdPO='+recDateIdPO+'&datePO='+datePO+'&duePO='+duePO+'&shipviaPO='+shipviaPO+'&vendorInvoicePO='+vendorInvoicePO
								+'&apacctPO='+apacctPO+'&postdatePO='+postdatePO+'&postDate1PO='+postDate1PO+'&shipDatePO='+shipDatePO
								+'&proPO='+proPO+'&subtotalGeneralId='+subtotalGeneralId+'&freightGeneralId='+freightGeneralId+'&taxGeneralId='+taxGeneralId
								+'&totalGeneralId='+totalGeneralId+'&balDuePO='+balDuePO+'&vepoId='+vepoId+'&veBillIdPO='+veBillIdPO
								+'&oper='+oper+'&prMasterIDPO='+prMasterIDPO+'&rxMasterIDPayablePO='+rxMasterIDPayablePO+'&buttonValue='+$('#viStatusButtonPO').val()
							    +"&operatorStatus="+operatorStatus;
							
						
							
							$.ajax({
								url: "./veInvoiceBillController/getPoTotalequalsvendorinvoice?vePoID="+vepoId,
								type: "POST",
								// data : aVendorInvoiceDetails,
								success: function(data) {
									if(operatorStatus=="close")
									{
										 var newDialogDiv = jQuery(document.createElement('div'));
										jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the PO transaction Status?</b></span>');
										jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
										buttons:{
											"OK": function(){
												
												jQuery(this).dialog("close");
												 saveVendorInvoicesfromPO($("#datePO").val(),generalValues,"yes",operatorStatus,reasonVal);
												
											},
											Cancel: function ()	{
												jQuery(this).dialog("close");
												 saveVendorInvoicesfromPO($("#datePO").val(),generalValues,"no",operatorStatus,reasonVal);
												
											return false;	
											}}}).dialog("open");
									
									}
									else
									{
										/*$("#addNewVeInvFmDlgbuttonsave").attr("disabled",true);
										$("#addNewVeInvFmDlgbuttonsaveandclose").attr("disabled",true);
										$("#addNewVeInvFmDlgbuttonsave").addClass("disableButtons");
										$("#addNewVeInvFmDlgbuttonsaveandclose").addClass("disableButtons");*/
										 saveVendorInvoicesfromPO($("#datePO").val(),generalValues,"no",operatorStatus);
									}
								}
						
								});
						 
				}		
			}
			
			
			
			
			function addNewVendorInvoiceFromPODlgwithReason(operStatus,rollbackStatus)
			{
				if($('#viStatusButtonPO').val()=="Paid")
				{
				$('.savehoverbutton').attr("disabled",true); 
				$('#viStatusButtonPO').attr("disabled",true);
				}
				
				var formData = $('#addNewVendorInvoiceForm').serialize();
				console.log(formData);
				var recDateIdPO =$('#recDateIdPO').val();
				var datePO = $('#datePO').val();
				var duePO = $('#duePO').val();
				var shipviaPO = $('#shipviaPO').val();
				var vendorInvoicePO = $('#vendorInvoicePO').val();
				var apacctPO = $('#apacctPO').val();
				var joreleasedetid=$('#joreleasedetid').val();
				var postdatePO;
				
				if($('#postdatePO').is(":checked"))
				{
					postdatePO = "on";
				}
				else
				{
					postdatePO = "off";
				}
				var postDate1PO = $('#postDate1PO').val();
				var shipDatePO = $('#shipDatePO').val();
				var proPO = $('#proPO').val();
				var subtotalGeneralId = $('#subtotalGeneralId').val().replace(/[^0-9-.]/g, '');
				var freightGeneralId = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
				var taxGeneralId = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
				var totalGeneralId = $('#totalGeneralId').val().replace(/[^0-9-.]/g, '');
				var balDuePO = $('#balDuePO').val().replace(/[^0-9-.]/g, '');
				// freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
				var vepoId = $('#vepoidPO').val();
				var veBillIdPO = $('#veBillIdPO').val();
				var prMasterIDPO = $('#rxMasterIDPO').val();
				var rxMasterIDPayablePO = $('#rxMasterIDPayablePO').val();
				var oper = 'add';
				var reasonVal=$("textarea#invreasonttextid").val();
				
				console.log("veBillId---->"+veBillIdPO);
				if(veBillIdPO == null || veBillIdPO == '')
					veBillIdPO = 0;				
				var generalValues =   'recDateIdPO='+recDateIdPO+'&datePO='+datePO+'&duePO='+duePO+'&shipviaPO='+shipviaPO+'&vendorInvoicePO='+vendorInvoicePO
					+'&apacctPO='+apacctPO+'&postdatePO='+postdatePO+'&postDate1PO='+postDate1PO+'&shipDatePO='+shipDatePO
					+'&proPO='+proPO+'&subtotalGeneralId='+subtotalGeneralId+'&freightGeneralId='+freightGeneralId+'&taxGeneralId='+taxGeneralId
					+'&totalGeneralId='+totalGeneralId+'&balDuePO='+balDuePO+'&vepoId='+vepoId+'&veBillIdPO='+veBillIdPO
					+'&oper='+oper+'&prMasterIDPO='+prMasterIDPO+'&rxMasterIDPayablePO='+rxMasterIDPayablePO+'&buttonValue='+$('#viStatusButtonPO').val()
				    +'&joreleasedetid='+joreleasedetid+'&operatorStatus='+operStatus;
				
				$.ajax({
					url: "./veInvoiceBillController/getPoTotal?vePoID="+vepoId,
					type: "POST",
					// data : aVendorInvoiceDetails,
					success: function(data) { var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the PO transaction Status?</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
					buttons:{
						"OK": function(){
							
							jQuery(this).dialog("close");
							 saveVendorInvoicesfromPO($("#datePO").val(),generalValues,"yes",operStatus,reasonVal);
							
						},
						Cancel: function ()	{
							jQuery(this).dialog("close");
							 saveVendorInvoicesfromPO($("#datePO").val(),generalValues,"no",operStatus,reasonVal);
							
						return false;	
						}}}).dialog("open");}
				});
			}
			
			function saveVendorInvoicesfromPO(datetoCheck,generalValues,updateStatus,operatorStatus,reasonVal)
			{
				// alert(datetoCheck+"***"+updateStatus+"***"+operatorStatus);
			
	 			 
				/*var rows = jQuery("#lineItemGrid").getDataIDs();
				var deleteinvoiceDetailId=new Array();
	 			 for(var a=0;a<rows.length;a++)
	 			 {
	 			    row=jQuery("#lineItemGrid").getRowData(rows[a]);
	 			   var id="#canDoID_"+rows[a];
	 			   var canDo=$(id).is(':checked');
		 			   if(canDo){
		 				  var veBillDetailId=row['veBillDetailId'];
		 				  if(veBillDetailId!=undefined && veBillDetailId!=null && veBillDetailId!="" && veBillDetailId!=0){
		 				 		deleteinvoiceDetailId.push(veBillDetailId);
		 				 	}
		 				 $('#lineItemGrid').jqGrid('delRowData',rows[a]);
		 			   }
	 			   }*/
	 			 
				var gridRows = $('#lineItemGrid').getRowData();
				var dataToSend = JSON.stringify(gridRows);
				var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
				$.ajax({
					url: "./checkAccountingCyclePeriods",
					data:{"datetoCheck":datetoCheck,"UserStatus":checkpermission},
					type: "POST",
					success: function(data) { 
						
						if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
						{
							periodid=data.cofiscalperiod.coFiscalPeriodId;
							yearid = data.cofiscalperiod.coFiscalYearId;
							
							
								$.ajax({
							        url: './veInvoiceBillController/addVendorInvoiceFromPO?'+generalValues,
							        type: 'POST',   
							        //data: generalValues+"&updatePO=yes"+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
							            data: {'updatePO':'yes','coFiscalPeriodId':periodid,'coFiscalYearId':yearid,
							        	'gridData':dataToSend,'delData':deleteveBillDetailIDDetailId,"reason":reasonVal},
							        success: function (data) {
							        	
							        	deleteveBillDetailIDDetailId==new Array();
							        	if(operatorStatus!="save")
							        	{
							        		if(document.getElementById("BillPayVendorInvoice")==undefined
													|| document.getElementById("BillPayVendorInvoice")==null){
												document.location.href ="./vendorinvoicelist";
											}else{
												$("#addNewVendorInvoiceFromPODlg").dialog("close");
												$("#vendorBills").trigger("reloadGrid");
											}
										return true;
							        	}
							        	else
							        	{
							        	$('#veBillIdPO').val(data.veBillId);	
										/*$("#addNewVeInvFmDlgbuttonsave").removeAttr("disabled");
										$("#addNewVeInvFmDlgbuttonsaveandclose").removeAttr("disabled");
										$("#addNewVeInvFmDlgbuttonsave").removeClass("disableButtons");
										$("#addNewVeInvFmDlgbuttonsaveandclose").removeClass("disableButtons");*/
										$("#lineItemGrid").jqGrid('GridUnload');
										loadlineItemGrid();
										$("#lineItemGrid").trigger("reloadGrid");
						        		$("#saveStatus").html("Saved.");
										setTimeout(function(){
											$("#saveStatus").html("");					
											},2000);	
							        	return false;
							        	}
							        }
							    }); 
						}
						else
							{
							
							if(data.AuthStatus == "granted")
							{	
							var newDialogDiv = jQuery(document.createElement('div'));
							jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
													buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
												}).dialog("open");
							}
							else
							{
								showDeniedPopupvendorinvoice();
							}
							}
				  	},
		   			error:function(data){
		   				console.log('error');
		   				}
		   			});
			    return false;
			}
			
			
			
			function vendorInvoiceStatusJob(){
				try{
					var dataid = $('#transactionStatus').val();
				    var newDialogDiv = jQuery(document.createElement('div'));
				    jQuery(newDialogDiv).attr("id", "showSalesOrderOptions");
				    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetVIStatus(this.value)" value="Void"></span><br><br>'+
				    		/*'<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="HOLDACCPAY" onclick="onSetVIStatus(this.value)" value="HOLD(Accounting/Payment)"></span><br><br>'+*/
				    		'<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Open" onclick="onSetVIStatus(this.value)" value="Open"></span><br><br>'+
				    		'<span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="HOLDPAYONLY" onclick="onSetVIStatus(this.value)" value="HOLD(For Payment Only)"></span><br><br>'+
				    		'<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Paid" onclick="onSetVIStatus(this.value)" value="Paid"></span><br><br>');
				  
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 257,
						height : 250,
						title : "Select Order Status",
						buttons : [  ]
					}).dialog("open");
					
					$('div#showSalesOrderOptions').bind('dialogclose', function(event) {
						$("#showSalesOrderOptions").dialog("destroy").remove();
					 });
					console.log('dataid'+dataid);
					  /*
						 * if(dataid=='2'){ console.log('dataid inside'+dataid);
						 * $('#Close').css("font-weight","bold");
						 * $('#Close').css("background","#0E2E55"); }else
						 * if(dataid=='0'){
						 * $('#Hold').css("font-weight","bold");
						 * $('#Hold').css("background","#0E2E55"); }else
						 * if(dataid=='1'){
						 * $('#Open').css("font-weight","bold");
						 * $('#Open').css("background","#0E2E55"); }else
						 * if(dataid=='-1'){
						 * $('#Void').css("font-weight","bold");
						 * $('#Void').css("background","#0E2E55"); }else
						 * if(dataid=='-2'){
						 * $('#Quote').css("font-weight","bold");
						 * $('#Quote').css("background","#0E2E55"); }
						 */
					
				}catch(err){
					
					}
				
				
			}

			function onSetVIStatus(e){
				
				$("#showSalesOrderOptions").dialog("destroy").remove();
				var setStatus=0;
				console.log("test :: "+e);
				// var cuSoid = $('#Cuso_ID').text();
				switch(e)
				{
				case 'Void':
					setStatus = -1;
					$('#viStatusButton').val(e);
					$('#viStatusButtontxt').val(e);
					break;
				case 'HOLD(Accounting/Payment)':
					setStatus = -2;
					$('#viStatusButton').val('Acct. HOLD');
					$('#viStatusButtontxt').val('Acct. HOLD');
					break;
				case 'HOLD(For Payment Only)':
					setStatus = 0;
					$('#viStatusButton').val('Pmt. HOLD');
					$('#viStatusButtontxt').val('Pmt. HOLD');
					break;
				case 'Open':
					setStatus = 1;
					$('#viStatusButton').val(e);
					$('#viStatusButtontxt').val(e);
					break;
				case 'Paid':
					setStatus = 2;
					$('#viStatusButton').val(e);
					$('#viStatusButtontxt').val(e);
					break;
				default:
					setStatus = 1;
				$('#viStatusButton').val(e);
				$('#viStatusButtontxt').val(e);
					break;
				}
				/*
				 * if(setStatus==-1){
				 * 
				 * var newDialogDiv = jQuery(document.createElement('div'));
				 * jQuery(newDialogDiv).attr("id", "showPDFoptiondialog");
				 * jQuery(newDialogDiv).html('<span><label>Do you wish to
				 * print a cancel ticket?</label></span>');
				 * 
				 * jQuery(newDialogDiv).dialog({ modal : true, width : 200,
				 * height : 250, title : "Cancel Ticket", buttons :
				 * [{height:35,text: "Yes",click: function() {
				 * $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();viewPOPDF();}},
				 * {height:35,text: "No",click: function() {
				 * $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();}},
				 * {height:35,text: "Cancel",click: function() {
				 * $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();}}]
				 * }).dialog("open");
				 *  }
				 */
				/*
				 * $.ajax({ url: "./salesOrderController/setSalesOrderStatus",
				 * type: "POST", data :{cusoID:cuSoid,status:setStatus},
				 * success: function(data) {
				 * $('#showSalesOrderOptions').dialog('destroy').remove();
				 * $('#transactionStatus').val(setStatus);
				 * $('#soStatusButton').val(e);
				 *  } });
				 */
				
			}

			
			function vendorInvoiceStatusForPO(){
				try{
					var dataid = $('#transactionStatus').val();
				    var newDialogDiv = jQuery(document.createElement('div'));
				    jQuery(newDialogDiv).attr("id", "showSalesOrderOptions");
				    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetWithPOStatus(this.value)" value="Void"></span><br><br>'+
				    		/*'<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="HOLDACCPAY" onclick="onSetWithPOStatus(this.value)" value="HOLD(Accounting/Payment)"></span><br><br>'+*/
				    		'<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Open" onclick="onSetWithPOStatus(this.value)" value="Open"></span><br><br>'+
				    		'<span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="HOLDPAYONLY" onclick="onSetWithPOStatus(this.value)" value="HOLD(For Payment Only)"></span><br><br>'+
				    		'<span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="Paid" onclick="onSetWithPOStatus(this.value)" value="Paid"></span>');
				  
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 257,
						height : 250,
						title : "Select Order Status",
						buttons : [  ]
					}).dialog("open");
					
					$('div#showSalesOrderOptions').bind('dialogclose', function(event) {
						$("#showSalesOrderOptions").dialog("destroy").remove();
					 });
					console.log('dataid'+dataid);
					  /*
						 * if(dataid=='2'){ console.log('dataid inside'+dataid);
						 * $('#Close').css("font-weight","bold");
						 * $('#Close').css("background","#0E2E55"); }else
						 * if(dataid=='0'){
						 * $('#Hold').css("font-weight","bold");
						 * $('#Hold').css("background","#0E2E55"); }else
						 * if(dataid=='1'){
						 * $('#Open').css("font-weight","bold");
						 * $('#Open').css("background","#0E2E55"); }else
						 * if(dataid=='-1'){
						 * $('#Void').css("font-weight","bold");
						 * $('#Void').css("background","#0E2E55"); }else
						 * if(dataid=='-2'){
						 * $('#Quote').css("font-weight","bold");
						 * $('#Quote').css("background","#0E2E55"); }
						 */
					
				}catch(err){
					
					}
				
				
			}

			function onSetWithPOStatus(e){
				
				$("#showSalesOrderOptions").dialog("destroy").remove();
				var setStatus=0;
				console.log("test :: "+e);
				// var cuSoid = $('#Cuso_ID').text();
				switch(e)
				{
				case 'Void':
					setStatus = -1;
					$('#viStatusButtonPO').val(e);
					$('#viStatusButtonPOtxt').val(e);
					break;
				case 'HOLD(Accounting/Payment)':
					setStatus = -2;
					$('#viStatusButtonPO').val('Acct. HOLD');
					$('#viStatusButtonPOtxt').val('Acct. HOLD');
					break;
				case 'HOLD(For Payment Only)':
					setStatus = 0;
					$('#viStatusButtonPO').val('Pmt. HOLD');
					$('#viStatusButtonPOtxt').val('Pmt. HOLD');
					break;
				case 'Open':
					setStatus = 1;
					$('#viStatusButtonPO').val(e);
					$('#viStatusButtonPOtxt').val(e);
					break;
				default:
					setStatus = 2;
				$('#viStatusButtonPO').val(e);
				$('#viStatusButtonPOtxt').val(e);
					break;
				}
				/*
				 * if(setStatus==-1){
				 * 
				 * var newDialogDiv = jQuery(document.createElement('div'));
				 * jQuery(newDialogDiv).attr("id", "showPDFoptiondialog");
				 * jQuery(newDialogDiv).html('<span><label>Do you wish to
				 * print a cancel ticket?</label></span>');
				 * 
				 * jQuery(newDialogDiv).dialog({ modal : true, width : 200,
				 * height : 250, title : "Cancel Ticket", buttons :
				 * [{height:35,text: "Yes",click: function() {
				 * $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();viewPOPDF();}},
				 * {height:35,text: "No",click: function() {
				 * $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();}},
				 * {height:35,text: "Cancel",click: function() {
				 * $(this).dialog("close");$("#showSalesOrderOptions").dialog("destroy").remove();}}]
				 * }).dialog("open");
				 *  }
				 */
				/*
				 * $.ajax({ url: "./salesOrderController/setSalesOrderStatus",
				 * type: "POST", data :{cusoID:cuSoid,status:setStatus},
				 * success: function(data) {
				 * $('#showSalesOrderOptions').dialog('destroy').remove();
				 * $('#transactionStatus').val(setStatus);
				 * $('#soStatusButton').val(e);
				 *  } });
				 */
				
			}
			/*var posit_outside_loadlineItemGrid=0;
			function loadlineItemGrid()
			{
				var gridstatus=true;
				var billID ='';
				var vepoID,query;
				console.log($('#veBillIdPO').val());
				if($('#veBillIdPO').val() != null && $('#veBillIdPO').val() != '')
				{
					vepoID = $('#veBillIdPO').val();
					query = './veInvoiceBillController/vendorInvoicelineitemGrid';
					console.log("veBillIdPO Empty :: "+vepoID+" :: Query ::  "+query);
				}
				else
				{
					vepoID = $('#vepoidPO').val();
					query = './veInvoiceBillController/polineitemGrid';
					console.log("veBillIdPO Empty :: "+vepoID+" :: Query ::  "+query);
				
				}
				try {
					
					$("#jqgridLine").empty();
					$("#jqgridLine").append("<table id='lineItemGrid'></table><div id='lineItemPager'></div>");
				 $('#lineItemGrid').jqGrid({
						datatype: 'JSON',
						mtype: 'POST',
						pager: jQuery('#lineItemPager'),
						url:query,
						postData: {'vepoID':vepoID},
						loadonce: false,
						colNames:['Product No', 'Description','Qty', 'Cost Each', 'Mult.', 'Tax','Net Each', 'Amount', 'VeBillId', 'prMasterID' ,'veBillDetailId', 'vePodetailID'],//
						colModel :[
					{name:'note',index:'note',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false,required: true}},
					{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
						cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},

					{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7},editrules:{number:true,required: true}},
					{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10}, formatter:customCurrencyFormatter, formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "$ "}, editrules:{number:true,required: true}},
					{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:40,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
					{name:'taxable', index:'taxable', align:'center',  width:20, hidden:true, editable:false, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
					{name:'netCast',index:'netCast',width:50 , align:'right',formatter:customCurrencyFormatter,hidden:true},
					{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
					{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
					{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
					{name:'veBillDetailId', index:'veBillDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
					{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}}
					],
						rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
						sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
						height:210,	width: 870, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
						loadBeforeSend: function(xhr) {
							posit_outside_loadlineItemGrid= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
						},
						loadComplete: function(data) {
							$("#lineItemGrid").setSelection(1, true);
							var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
							var aVal = new Array(); 
							var aTax = new Array();
							var sum = 0;
							var taxAmount = 0;
							var aTotal = 0;
							$.each(allRowsInGrid, function(index, value) {
								aVal[index] = value.quantityBilled;
								var number1 = aVal[index].replace("$", "");
								var number2 = number1.replace(".00", "");
								var number3 = number2.replace(",", "");
								var number4 = number3.replace(",", "");
								var number5 = number4.replace(",", "");
								var number6 = number5.replace(",", "");
								sum = Number(sum) + Number(number6); 
							});
							
							$('#subtotalGeneralId').val(formatCurrencynodollar(sum));
							var freight = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
							var tax = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
							var subTotal = $('#subtotalGeneralId').val().replace("$", "");
							console.log("SubTot :: "+subTotal);
							var aTotal = parseFloat(sum) + parseFloat(freight) +parseFloat(tax);
							$('#totalGeneralId').val(formatCurrencynodollar(aTotal));
							$('#balDuePO').val(formatCurrencynodollar(aTotal));							
							
						},gridComplete:function(){
					             jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_loadlineItemGrid);
					             posit_outside_loadlineItemGrid=0;
							if(oldgridFormValidation){
								oldlgserialize = jQuery("#lineItemGrid").jqGrid('getRowData');
								oldgridFormValidation=false;
							}
							
						},
						loadError : function (jqXHR, textStatus, errorThrown){	},
						onSelectRow:  function(id){
							var rowData = jQuery(this).getRowData(id); 
							var veBillDetailId = rowData["veBillDetailId"];
							var prMasterId = rowData["prMasterId"];
							$('#rxMasterIDPO').val(prMasterId);
							console.log("veBillDetailId :: "+veBillDetailId);
						},
						onCellSelect : function (rowid,iCol, cellcontent, e) {
							},
						editurl:"./veInvoiceBillController/manpulateVendorInvoiceLineItem"
				 });
				 var selrowid = $("#invoicesGrid").jqGrid('getGridParam','selrow');
				var releasedetail= $("#invoicesGrid").jqGrid('getCell', selrowid, 'joreleasedetailid');
				 $('#lineItemGrid').jqGrid('navGrid','#lineItemPager', {add:gridstatus, edit:gridstatus,del:gridstatus,refresh:gridstatus,search:false},
							{
					 	width:515, left:400, top: 300, zIndex:1040,
						closeAfterEdit:true, reloadAfterSubmit:true,
						modal:true, jqModel:true,
						editCaption: "Edit Product",
						beforeInitData : function(formid) {
							if($('#veBillIdPO').val() ===''){
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:red;">Can not Edit before save new Invoice.</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
														buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
													}).dialog("open");
								return false;
							}
						}, 
						beforeShowForm: function (form) 
						{
							
							$("a.ui-jqdialog-titlebar-close").hide();
							jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').empty();
							jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').append('Product No: ');
							jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
							jQuery('#TblGrid_lineItemGrid #tr_description .CaptionTD').empty();
							jQuery('#TblGrid_lineItemGrid #tr_description .CaptionTD').append('Description: ');
							jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').empty();
							jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').append('Qty: ');
							jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
							jQuery('#TblGrid_lineItemGrid #tr_unitCost .CaptionTD').empty();
							jQuery('#TblGrid_lineItemGrid #tr_unitCost .CaptionTD').append('Cost Each.: ');
							jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').empty();
							jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').append('Mult.: ');
							jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
							jQuery('#TblGrid_lineItemGrid #tr_taxable .CaptionTD').empty();
							jQuery('#TblGrid_lineItemGrid #tr_taxable .CaptionTD').append('Tax: ');
							var unitPrice = $('#unitCost').val();
							unitPrice=  parseFloat(unitPrice.replace(/[^0-9-.]/g, ''));
							$('#unitCost').val(unitPrice);
							var priceMultiplier = $('#priceMultiplier').val();
							priceMultiplier=  parseFloat(priceMultiplier.replace(/[^0-9-.]/g, ''));
							$('#priceMultiplier').val(priceMultiplier);
							 
						},
						afterShowForm: function($form) {
							 $('#note').attr('placeholder','Minimum 2 character required to get Name List');
							$(function() { var cache = {}; var lastXhr=''; $("input#note").autocomplete({minLength: 1,timeout :1000,
								source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
									lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
								select: function( event, ui ){
									var ID = ui.item.id; var product = ui.item.label; $("#rxMasterIDPO").val(ID);
									$.ajax({
								        url: './getLineItems?prMasterId='+$("#rxMasterIDPO").val(),
								        type: 'POST',       
								        success: function (data) {
								        	$.each(data, function(key, valueMap) {										
												
								        		if("lineItems"==key)
												{				
													$.each(valueMap, function(index, value){						
														
															$("#description").val(value.description);
															$("#unitCost").val(value.lastCost);
															$("#priceMultiplier").val(value.pomult);
															
															if(value.isTaxable == 1)
															{
																$("#taxable").prop("checked",true);
															}
															else
																$("#taxable").prop("checked",false);
													
													}); 
													
												}														
											});
								        }
								    });
									},
								error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
								}); 
							return;
							});
						},
						beforeSubmit:function(postdata, formid) {
							$("#note").autocomplete("destroy"); 
							$(".ui-menu-item").hide();
							var aPrMasterID = $('#rxMasterIDPO').val();
							if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
							return [true, ""];
						},
						onclickSubmit: function(params){
							var recDateIdPO =$('#recDateIdPO').val();
							var datePO = $('#datePO').val();
							var duePO = $('#duePO').val();
							var shipviaPO = $('#shipviaPO').val();
							var vendorInvoicePO = $('#vendorInvoicePO').val();
							var apacctPO = $('#apacctPO').val();							
							var postdatePO;							
							if($('#postdatePO').is(":checked"))
							{
								postdatePO = "on";
							}
							else
							{
								postdatePO = "off";
							}
							var postDate1PO = $('#postDate1PO').val();
							var shipDatePO = $('#shipDatePO').val();
							var proPO = $('#proPO').val();
							var subtotalGeneralId = $('#subtotalGeneralId').val().replace(/[^0-9-.]/g, '');
							var freightGeneralId = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
							var taxGeneralId = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
							var totalGeneralId = $('#totalGeneralId').val().replace(/[^0-9-.]/g, '');
							var balDuePO = $('#balDuePO').val().replace(/[^0-9-.]/g, '');
							var vepoId = $('#vepoidPO').val();
							var veBillIdPO = $('#veBillIdPO').val();
							var prMasterIDPO= $('#rxMasterIDPO').val();
							var rxMasterIDPayablePO= $('#rxMasterIDPayablePO').val();
							
							console.log("veBillId---->"+veBillIdPO);
							if(veBillIdPO == null || veBillIdPO == '')
								veBillIdPO = 0;
							var buttonValue = $('#viStatusButtonPO').val();
							console.log("After veBillId---->"+veBillIdPO);
							return { 'recDateIdPO' : recDateIdPO, 'datePO' : datePO,'duePO':duePO, 'shipviaPO' : shipviaPO,'vendorInvoicePO' : vendorInvoicePO
								, 'apacctPO' : apacctPO,'postdatePO':postdatePO, 'postDate1PO' : postDate1PO,'shipDatePO' : shipDatePO
								, 'proPO' : proPO,'subtotalGeneralId':subtotalGeneralId, 'freightGeneralId' : freightGeneralId,'taxGeneralId' : taxGeneralId
								, 'totalGeneralId' : totalGeneralId,'balDuePO':balDuePO, 'vepoId' : vepoId,'veBillIdPO' : veBillIdPO,'oper' : 'edit','prMasterIDPO' : prMasterIDPO,'rxMasterIDPayablePO' : rxMasterIDPayablePO,'buttonValue' : buttonValue};
						},
						afterSubmit:function(response,postData){
							$("#note").autocomplete("destroy"); 
							$(".ui-menu-item").hide();
							$("a.ui-jqdialog-titlebar-close").show();
							var row = JSON.parse(response.responseText);
							console.log("Vendor Invoice Rows---->"+row);
							console.log("veBillId --->"+row.veBillId);
							$("#lineItemGrid").GridUnload();
							 return [true, loadlineItemGrid()];
						}
					

							},
							{
								width:550, left:400, top: 300, zIndex:1040,
								closeAfterAdd:true,	reloadAfterSubmit:true,
								modal:true, jqModel:false,
								addCaption: "Add Product",
								onInitializeForm: function(form){
									
								},
								afterShowForm: function($form) {
									 $('#note').attr('placeholder','Minimum 2 character required to get Name List');
									$(function() { var cache = {}; var lastXhr=''; $("input#note").autocomplete({minLength: 1,timeout :1000,
										source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
											lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
										select: function( event, ui ){
											var ID = ui.item.id; var product = ui.item.label; $("#rxMasterIDPO").val(ID);
											$.ajax({
										        url: './getLineItems?prMasterId='+$("#rxMasterIDPO").val(),
										        type: 'POST',       
										        success: function (data) {
										        	$.each(data, function(key, valueMap) {										
														
										        		if("lineItems"==key)
														{				
															$.each(valueMap, function(index, value){						
																
																	$("#description").val(value.description);
																	$("#unitCost").val(value.lastCost);
																	$("#priceMultiplier").val(value.pomult);
																	
																	if(value.isTaxable == 1)
																	{
																		$("#taxable").prop("checked",true);
																	}
																	else
																		$("#taxable").prop("checked",false);
															
															}); 
															
														}														
													});
										        }
										    });
											},
										error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
										}); 
									return;
									});
								},
								beforeSubmit:function(postdta, formid) {
									$("#note").autocomplete("destroy");
									 $(".ui-menu-item").hide();
									var aPrMasterID = $('#rxMasterIDPO').val();
									if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
									return [true, ""];
								},
								onclickSubmit: function(params){
									var recDateIdPO =$('#recDateIdPO').val();
									var datePO = $('#datePO').val();
									var duePO = $('#duePO').val();
									var shipviaPO = $('#shipviaPO').val();
									var vendorInvoicePO = $('#vendorInvoicePO').val();
									var apacctPO = $('#apacctPO').val();
									var postdatePO;							
									if($('#postdatePO').is(":checked"))
									{
										postdatePO = "on";
									}
									else
									{
										postdatePO = "off";
									}
									var postDate1PO = $('#postDate1PO').val();
									var shipDatePO = $('#shipDatePO').val();
									var proPO = $('#proPO').val();
									var subtotalGeneralId = $('#subtotalGeneralId').val().replace(/[^0-9-.]/g, '');
									var freightGeneralId = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
									var taxGeneralId = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
									var totalGeneralId = $('#totalGeneralId').val().replace(/[^0-9-.]/g, '');
									var balDuePO = $('#balDuePO').val().replace(/[^0-9-.]/g, '');
									var vepoId = $('#vepoidPO').val();
									var veBillIdPO = $('#veBillIdPO').val();
									var prMasterIDPO = $('#rxMasterIDPO').val();
									var rxMasterIDPayablePO = $('#rxMasterIDPayablePO').val();
									console.log("veBillId---->"+veBillIdPO);
									if(veBillIdPO == null || veBillIdPO == '')
										veBillIdPO = 0;
									var buttonValue = $('#viStatusButtonPO').val();
									console.log("After veBillId---->"+veBillIdPO);
									return { 'recDateIdPO' : recDateIdPO, 'datePO' : datePO,'duePO':duePO, 'shipviaPO' : shipviaPO,'vendorInvoicePO' : vendorInvoicePO
										, 'apacctPO' : apacctPO,'postdatePO':postdatePO, 'postDate1PO' : postDate1PO,'shipDatePO' : shipDatePO
										, 'proPO' : proPO,'subtotalGeneralId':subtotalGeneralId, 'freightGeneralId' : freightGeneralId,'taxGeneralId' : taxGeneralId
										, 'totalGeneralId' : totalGeneralId,'balDuePO':balDuePO, 'vepoId' : vepoId,'veBillIdPO' : veBillIdPO,'oper' : 'add','prMasterIDPO' : prMasterIDPO,'rxMasterIDPayablePO' : rxMasterIDPayablePO,'buttonValue' : buttonValue};
								},
								afterSubmit:function(response,postData){
									$("#note").autocomplete("destroy");
									$(".ui-menu-item").hide();
									$("a.ui-jqdialog-titlebar-close").show();
									var row = JSON.parse(response.responseText);
									console.log("Vendor Invoice Rows---->"+row);
									console.log("veBillId --->"+row.veBillId);
									$('#veBillIdPO').val(row.veBillId);
									var billDate = row.billDate;
									if (typeof (billDate) != 'undefined') 
										FormatShipDate(billDate);
									$("#lineItemGrid").GridUnload();
									return [true, loadlineItemGrid()];
								}
							},
							{	
								closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
								caption: "Delete Product",
								msg: 'Delete the Product Record?',

								onclickSubmit: function(params){	
									var veBillIdPO = $('#veBillIdPO').val();
									var id = jQuery("#lineItemGrid").jqGrid('getGridParam','selrow');
									var key = jQuery("#lineItemGrid").getCell(id, 11);
									return {'veBillDetailId' : key,'oper' : 'delete','veBillIdPO' : veBillIdPO};
									
								},
								afterSubmit:function(response,postData){
									var row = JSON.parse(response.responseText);
									console.log("Vendor Invoice Rows---->"+row);
									console.log("veBillId --->"+row.veBillId);
									$("#lineItemGrid").GridUnload();
									 return [true, loadlineItemGrid()];
								}
							}
						);
				}
				catch(err) {
			        var text = "There was an error on this page.\n\n";
			        text += "Error description: " + err.message + "\n\n";
			        text += "Click OK to continue.\n\n";
			        console.log(text);
			    }
				
				if($('#viStatusButtonPO').val()=="Paid")
				{
							
				$('#viStatusButtonPO').attr("disabled",true);
				
				$("#addNewVeInvFmDlgclsbutton").css("display","inline");
				$("#addNewVeInvFmDlgbutton").css("display","none");
				
				$("#paidStatus").css("display","inline");
				$("#chk_nofmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'chkNo'));
				$("#date_paidfmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'datePaid'));
				
				
				$("#add_lineItemGrid").addClass("ui-state-disabled");
				$("#edit_lineItemGrid").addClass("ui-state-disabled");
				$("#del_lineItemGrid").addClass("ui-state-disabled");
				$("#refresh_lineItemGrid").addClass("ui-state-disabled");
				}
							
			}	*/
			
			function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
				return cellValue;
			}
			
			function formatCurrencynodollar(strValue)
			{
				
				var returnval=formatCurrency(strValue);
				returnval = returnval.toString().replace(/\$|\,/g,'');
				return returnval;
				/*if(strValue === "" || strValue == null){
					return "0.00";
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
				for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
					dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
					dblValue.substring(dblValue.length-(4*i+3));
				}
				return (((blnSign)?'':'-') + dblValue + '.' + strCents);*/
			}
			
function findLenght()
{
	if($('#vendorInvoicePO').val().length > 12)
	{
		
	}
}

function FormatShipDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#ui-dialog-title-addNewVendorInvoiceFromPODlg').text(createdDate);
	console.log("Test  ::  "+$('#ui-dialog-title-addNewVendorInvoiceFromPODlg').text());
	
}
function FormatReceiveDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#recDateIdPO').val(createdDate);
	
}
function FormatDatedDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#datePO').val(createdDate);
	$('#ui-dialog-title-addNewVendorInvoiceFromPODlg').text(createdDate);
	
}
function FormatDueDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#duePO').val(createdDate);
	
}
function FormatShipPODate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#shipDatePO').val(createdDate);
	
}
function FormatPostDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#postDate1PO').val(createdDate);
	
}
function preloadVendorInvoiceData(veBillId)
{
	var formData = 'veBillId='+veBillId;
	$.ajax({
        url: './veInvoiceBillController/getveBillDetails',
        type: 'POST',  
        data: formData,    
        success: function (data) {
        	console.log("No PO  :: "+data);
        	console.log('Length :: '+data.length);
	        if(data.Vebill.veBillId != null)
	        {
	        	document.getElementById("addNewVendorInvoiceFromPOForm").reset();
	        	
	        	/*
				 * console.log("PO Exist :: "+data.Vebill.ponumber);
				 * console.log("Address1 :: "+data.vendorAddress.address1);
				 * $('#ponumberPO').text(data.Vebill.ponumber);
				 * $('#vepoidPO').val(data.Vebill.vePoid);
				 */
	        	
	        	if(data.veInvmandatory == 1)
	        	{
	        	$('#veInvnomandatory').css({"display":"inherit"})
	        	$('#veInvnomandatory').attr("data-manvalue",data.veInvmandatory);
	        	}
	        	else
	        	{
	        	$('#veInvnomandatory').css({"display":"none"})
	        	$('#veInvnomandatory').attr("data-manvalue",data.veInvmandatory);
	        	}
	        	
	        	FormatReceiveDate(data.Vebill.receiveDate);
	        	FormatDatedDate(data.Vebill.billDate);
	        	FormatDueDate(data.Vebill.dueDate);
	        	$('#shipviaPO').val(data.Vebill.veShipViaId);
	        	$('#vendorInvoicePO').val(data.Vebill.invoiceNumber);
	        	$('#apacctPO').val(data.Vebill.apaccountId).attr('selected', true);
	        	$('#ponumberPO').text(data.Vepo.ponumber);
				$('#jobName').text(data.Vepo.updateKey);
				$('#joreleasedetid').val(data.Vebill.joReleaseDetailId);
	        	console.log('usePostDate ::::  '+data.Vebill.usePostDate);
	        	if(data.Vebill.usePostDate)
        		{
	        		$('#postdatePO').prop('checked',true);
	        		$('#postDate1PO').show();
	        		FormatPostDate(data.Vebill.postDate);
        		}
	        	else
        		{
	        		$('#postdatePO').prop('checked',false);
	        		$('#postDate1PO').hide();
	        		FormatPostDate(data.Vebill.postDate);
        		}
	        	// $('#postdatePO').val(data.Vebill.usePostDate);
	        	
	        	FormatShipPODate(data.Vebill.shipDate);
	        	$('#proPO').val(data.Vebill.trackingNumber);
	        	$('#freightGeneralId').val(parseFloat(data.Vebill.freightAmount).toFixed(2));
	        	$('#taxGeneralId').val(data.Vebill.taxAmount);
	        	$('#vendorAddressPO').html(data.vendorAddress.address1);
				$('#vendorAddress1PO').html(data.vendorAddress.city).append(data.vendorAddress.state).append(data.vendorAddress.zip);
				$('#payablePO').val(data.vendorName.name);
				$('#invwithPoReason').val(data.Vebill.reason);
	        	
				$('#rxMasterIDPayablePO').val(data.Vebill.rxMasterId);
				$('#veBillIdPO').val(data.Vebill.veBillId); 
				
				$('#vepoidPO').val(data.Vebill.vePoid); 
			
				
				if(data.Vebill.transactionStatus === -1)
					{$('#viStatusButtonPO').val('Void');$('#viStatusButtonPOtxt').val('Void');}
				else if(data.Vebill.transactionStatus === 0)
					{$('#viStatusButtonPO').val('Acct. HOLD');$('#viStatusButtonPOtxt').val('Acct. HOLD');}
				else if(data.Vebill.transactionStatus === 1)
					{$('#viStatusButtonPO').val('Open');$('#viStatusButtonPOtxt').val('Open');}
				else if(data.Vebill.transactionStatus === 2)
					{$('#viStatusButtonPO').val('Paid');$('#viStatusButtonPOtxt').val('Paid');}
				else
					{$('#viStatusButtonPO').val('Pmt. HOLD');$('#viStatusButtonPOtxt').val('Pmt. HOLD');}			
				
	        	jQuery("#EnterPODlg").dialog("close");
	        	
				loadlineItemGrid();
				 jQuery( "#invStatus" ).val("addNewVendorInvoiceFromPODlg");
			// editTransactionDetails("addNewVendorInvoiceFromPODlg");
				 $(".invReason").show();
			jQuery("#addNewVendorInvoiceFromPODlg").dialog("open");
			
			setTimeout(function(){
				oldformserialize=checkFormValidation();		
				oldlgserialize = jQuery("#lineItemGrid").jqGrid('getRowData');
				},500);
			return true;
				
							        
	        }
	        else
	        {
	        	console.log("Test");
	        	jQuery("#EnterPODlg").dialog("close");
	        	$('#InvalidPOMsg').text('Cannot find purchase order #'+po);
	        	jQuery("#InvalidPODlg").dialog("open");
				return true;					        	
	        }        	
			
        }
    });
}
function closethepopup(){
	jQuery("#addNewVendorInvoiceFromPODlg").dialog("close");
	jQuery("#addNewVendorInvoiceDlg").dialog("close");
}
function preloadVendorInvoiceFromJobData(veBillId)
{
	
	var formData = 'veBillId='+veBillId;
	$.ajax({
        url: './veInvoiceBillController/getveBillDetails',
        type: 'POST',  
        data: formData,    
        success: function (data) {
        	console.log("preloadVendorInvoiceFromJobData No PO  :: "+data);
	        if(data.Vebill.veBillId != null)
	        {
	        	document.getElementById("addNewVendorInvoiceForm").reset();
	        	/*
				 * console.log("PO Exist :: "+data.Vebill.ponumber);
				 * console.log("Address1 :: "+data.vendorAddress.address1);
				 * $('#ponumberPO').text(data.Vebill.ponumber);
				 * $('#vepoidPO').val(data.Vebill.vePoid);
				 */
	        	
	        	FormatReceiveDateJob(data.Vebill.receiveDate);
	        	FormatDatedDateJob(data.Vebill.billDate);
	        	FormatDueDateJob(data.Vebill.dueDate);
	        	$('#shipvia').val(data.Vebill.veShipViaId);
	        	$('#vendorInvoice').val(data.Vebill.invoiceNumber);
	        	
	        
	        	
	        	$('#apacct').val(data.Vebill.apaccountId).attr('selected', true);;
	        	$('#ponumber').text(data.Vepo.ponumber);
				$('#jobName').text(data.Vepo.updateKey);
	        	console.log('usePostDate ::::  '+data.Vebill.usePostDate);
	        	if(data.Vebill.usePostDate)
        		{
	        		$('#postdate').prop('checked',true);
	        		$('#postDate1').show();
	        		FormatPostDateJob(data.Vebill.postDate);
        		}
	        	else
        		{
	        		$('#postdate').prop('checked',false);
	        		$('#postDate1').hide();
	        		FormatPostDateJob(data.Vebill.postDate);
        		}
	        	// $('#postdatePO').val(data.Vebill.usePostDate);
	        	
	        	$('#vendorAddress').html(data.vendorAddress.address1);
				$('#vendorAddress1').html(data.vendorAddress.city).append(data.vendorAddress.state).append(data.vendorAddress.zip);
				$('#payable').val(data.vendorName.name);
				
				
				$('#invreason').val(data.Vebill.reason);
				
	        	
				$('#rxMasterID').val(data.Vebill.rxMasterId);
				$('#veBillIdJob').val(data.Vebill.veBillId);
				console.log("TS :: "+data.Vebill.transactionStatus);
				if(data.Vebill.transactionStatus === -1)
					{$('#viStatusButton').val('Void');$('#viStatusButtontxt').val('Void');}
				else if(data.Vebill.transactionStatus === 0)
					{$('#viStatusButton').val('Acct. HOLD');$('#viStatusButtontxt').val('Acct. HOLD');}
				else if(data.Vebill.transactionStatus === 1)
					{$('#viStatusButton').val('Open');$('#viStatusButtontxt').val('Open');}
				else if(data.Vebill.transactionStatus === 2)
					{$('#viStatusButton').val('Paid');$('#viStatusButtontxt').val('Paid');}
				else
					{$('#viStatusButton').val('Pmt. HOLD');$('#viStatusButtontxt').val('Pmt. HOLD');}
				jQuery("#EnterPODlg").dialog("close");	
				
				$("#vendorInvoiceGrid").jqGrid('GridUnload');
				loadNewVendorInvoice();
				$("#vendorInvoiceGrid").prop('disabled',true);
												
				jQuery( "#invStatus" ).val("addNewVendorInvoiceDlg");
				jQuery("#addNewVendorInvoiceDlg").dialog("open");
				
				setTimeout(function(){
					oldformserializewithoutpo = $('#addNewVendorInvoiceForm').serialize();		
					oldlgserializewithoutpo = jQuery("#vendorInvoiceGrid").jqGrid('getRowData');
					},500);
				
				
				 $(".invReason").show();
				return true;
					
							        
	        }
	        else
	        {
	        	console.log("Test");
	        	jQuery("#EnterPODlg").dialog("close");
	        	$('#InvalidPOMsg').text('Cannot find purchase order #'+po);
	        	jQuery("#InvalidPODlg").dialog("open");
				return true;					        	
	        }        	
			
        }
    });
}
function FormatReceiveDateJob(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#recDateId').val(createdDate);
	
}
function FormatDatedDateJob(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#date').val(createdDate);
	$('#ui-dialog-title-addNewVendorInvoiceDlg').text(createdDate);
	
}
function FormatDueDateJob(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#due').val(createdDate);
	
}
function FormatPostDateJob(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	$('#postDate1').val(createdDate);
	
}
$(function() { var cache = {}; var lastXhr='';
$("#searchJob").autocomplete({ minLength: 1,timeout :1000,
	/*
	 * open: function(){ $(".ui-autocomplete").prepend('<div style="font-size:
	 * 15px;"><b><a href="./inventoryDetails?token=new"
	 * style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size:
	 * 0.8em;">+ Add New Inventory</a></b></div>');
	 * $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
	 */
select: function (event, ui) {
	var aValue = ui.item.value;
	console.log("Auto Suggest veBillid is :: "+aValue);
	var aText = ui.item.label;
	var valuesArray = new Array();
	valuesArray = aText.split("||");
	console.log(valuesArray);
	var aVepoId = valuesArray[4].replace("]","");
	
	// var aQryStr = "aVePOId=" + aValue;
	if($.trim(aVepoId) != "null")
		preloadVendorInvoiceData(aValue);
	else
		preloadVendorInvoiceFromJobData(aValue);
	
},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/searchVendorInvoiceNumber", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} });
});

/*******************************************************************************
 * created by: Praveenkumar Date: 2014-09-01 Purpose : Calculating freight
 * charges
 */
jQuery(document).ready(function(){
$('#freightGeneralId').keyup(function () {
	
var subtotal = Number($('#subtotalGeneralId').val().replace(/[^0-9-.]/g, ''));
	
	var freight = Number($('#freightGeneralId').val().replace(/[^0-9-.]/g, ''));
	var tax = Number($('#taxGeneralId').val().replace(/[^0-9-.]/g, ''));
	console.log(subtotal+' :: '+freight+' :: '+tax);
	var total = subtotal+freight+tax;
	$('#totalGeneralId').val(parseFloat(total).toFixed(2));
	$('#balDuePO').val(parseFloat(total).toFixed(2));
	
});
$('#taxGeneralId').keyup(function () {
	
	var subtotal = Number($('#subtotalGeneralId').val().replace(/[^0-9-.]/g, ''));
		
		var freight = Number($('#freightGeneralId').val().replace(/[^0-9-.]/g, ''));
		var tax = Number($('#taxGeneralId').val().replace(/[^0-9-.]/g, ''));
		console.log(subtotal+' :: '+freight+' :: '+tax);
		var total = subtotal+freight+tax;
		$('#totalGeneralId').val(parseFloat(total).toFixed(2));
		$('#balDuePO').val(parseFloat(total).toFixed(2));
		
	});
});


/*******************************************************************************
 * created by: Leo Date: 2014-09-26 Purpose : Reason Dialog box
 */
function editInvoiceDetails(operStatus){
	 $('#invreasondialog').data('operStatus', operStatus);
	 jQuery( "#invreasondialog" ).dialog("open");
	return true;
}

function getcoFiscalPerliodDate(x)
{
	 $.ajax({
		 
	 		url: "./banking/getcoFiscalPeriod",
	 		type: "GET",
	 		success: function(data) {
	 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
	 					
	 		}		 
	 });	 
}

function showPaymentDetailsDialog()
{
	 var selrowid = $("#invoicesGrid").jqGrid('getGridParam','selrow');
	 var bilAmt= $("#invoicesGrid").jqGrid('getCell', selrowid, 'billAmount');
	 var appAmt= $("#invoicesGrid").jqGrid('getCell', selrowid, 'amt');
	 var chkNo = $("#invoicesGrid").jqGrid('getCell', selrowid, 'chkNo');
	 var chkDate = $("#invoicesGrid").jqGrid('getCell', selrowid, 'datePaid');
	 var veBillID = $("#invoicesGrid").jqGrid('getCell', selrowid, 'veBillId');
	 $("#invoiceDetailspopup").html("");
	 if(chkNo!="Multiple")
		 {
			 $("#invoiceDetailspopup").append("<tr><td><label style='Padding:10px; line-height:2'>Invoice</label></td><td></td><td><span id='Amtinvoice' style='Padding:10px; float:right'>"+bilAmt+"</span></td></tr>" +
			 		"<tr><td width='35%'><label style='Padding:10px;line-height:2'>Check #<span id='checkNo'>"+chkNo+"</span></label></td> <td width='35%'> <span id='chekDate' style='Padding:10px;'>"+chkDate+"</span></td><td width='30%'><span id='checkAmt' style='Padding:10px;float:right'>$"+formatCurrencynodollar(appAmt)+"</span></td></tr>" +
			 		"<tr><td colspan='2'><label style='Padding:10px;line-height:2'>Balance Due</label></td> <td style='border-style:dashed; border-width: 1px;border-bottom:none;border-left:none;border-right:none;'><span id='amtBalance' style='Padding:10px;float:right; '>$"+formatCurrencynodollar(parseFloat(bilAmt.replace(/[^0-9-.]/g, ''))-parseFloat(appAmt.replace(/[^0-9-.]/g, '')))+"</span></td>" +
			 		"");
		 }
	 else
		 {
			 $.ajax({
				 
			 		url: "./veInvoiceBillController/getInvoicepaymentdetailsfordialogbox",
			 		type: "POST",
			 		data: {"veBillID":veBillID},
			 		success: function(data) {
			 			var AmtVal = 0;
			 			 html = "";
			 			 $("#invoiceDetailspopup").append("<tr><td><label style='Padding:10px; line-height:2'>Invoice</label></td><td></td><td><span id='Amtinvoice' style='Padding:10px; float:right'>"+bilAmt+"</span></td></tr>");
			 			 for(var i=0;i<data.length;i++){
			 				   html = html+"<tr><td width='35%'><label style='Padding:10px;line-height:2'>Check #<span id='checkNo'>"+data[i].checkNo+"</span></label></td> <td width='35%'> <span id='chekDate' style='Padding:10px;'>"+FormatDate(new Date(data[i].datePaid))+"</span></td><td width='30%'><span id='checkAmt' style='Padding:10px;float:right'>$"+formatCurrencynodollar(data[i].amountVal)+"</span></td></tr>";
			 				  AmtVal = AmtVal+Number(data[i].amountVal);
			 			
			 				   }
			 			  console.log("AmtVal::"+(bilAmt.replace(/[^0-9-.]/g, '')))
			 			$("#invoiceDetailspopup").append(html).append("<tr><td colspan='2'><label style='Padding:10px;line-height:2'>Balance Due</label></td> <td style='border-style:dashed; border-width: 1px;border-bottom:none;border-left:none;border-right:none;'><span id='amtBalance' style='Padding:10px;float:right; '>$"+formatCurrencynodollar(Number(bilAmt.replace(/[^0-9-.]/g, '')) - Number(AmtVal))+"</span></td>");	 
			 		}		 
			 });	
		 }
	 
	 jQuery( "#showInvoiceInfoDialog" ).dialog("open");
		return true;
	
}


jQuery(document).ready(function(){
	
	var $led = $("#invreasondialog");
	
	 jQuery( "#invreasondialog" ).dialog({
			autoOpen: false,
			width: 400,
			title:"Reason",
			modal: true,
			buttons:{
				ok:function(){
					if($("textarea#invreasonttextid").val()==""){
						$("#inverrordivreason").empty();
						$("#inverrordivreason").append("Reason required");
					}else{
						$("#inverrordivreason").empty();
						if(jQuery("#VendorInvoiceTypeID").val()=="WithPO")
							{
							//WithPo
							addNewVendorInvoiceFromPODlgwithReason($led.data('operStatus'));
							}
						else
							{
						//without po
							var oper= $('#invreasondialog').data('operStatus');
							addNewVendorInvoiceDlgwithReason(oper);
							}
						jQuery( "#invreasondialog" ).dialog("close");
					}
					
				}
			},
			close: function () {
				$("#saveTermsButton").attr("disabled",false);
				$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
				
				$('#invreasondialog').validationEngine('hideAll');
				return true;
			}
		});
	 
	 
	 
	 
	 jQuery( "#showInvoiceInfoDialog" ).dialog({
			autoOpen: false,
			width: 430,
			height: 230,
			title:"Payment Details",
			modal: true,
			buttons:{
				ok:function(){
					jQuery(this).dialog("close");
				}
			}
	 });
});


function FormatDate(createdDate){
	var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;
	return createdDate;
}

function uninvoicedClick() {
	createtpusage('Company-Vendors-Invoices & Bills','Uninvoiced View','Info','Company-Vendors-Invoices & Bills,Viewing Uninvoiced PO');
	document.getElementById("UninvoicedForm").reset();
	jQuery("#uninvoicedDlg").dialog("open");
	return true;
}

jQuery(function() {
	jQuery("#uninvoicedDlg").dialog({
		autoOpen : false,
		height :750,
		width : 1000,
		title : "Vendor Invoices/No Customer Billing",
		modal : true,
		close : function() {
			return true;
		}
	});
});

function enableuninvoiceDate(){
	if(document.getElementById("uninvoicefromDate").disabled){
	 $("#uninvoicefromDate").prop('disabled',false);
	 $("#uninvoicetoDate").prop('disabled',false);
	}else{
		$("#uninvoicefromDate").val("");
		$("#uninvoicetoDate").val("");
		uninvoicefromDate="";uninvoicetoDate="";
		$("#uninvoicesGrid").jqGrid('GridUnload');
		loadUninvoicesList(searchData,uninvoicefromDate,uninvoicetoDate);
		$("#uninvoicefromDate").prop('disabled',true);
		$("#uninvoicetoDate").prop('disabled',true);
	}
}

function uninvoicefromfun(){
	uninvoicefromDate = $("#uninvoicefromDate").val();
	$("#uninvoicesGrid").jqGrid('GridUnload');
	loadUninvoicesList(searchData,uninvoicefromDate,uninvoicetoDate);
	$("#uninvoicesGrid").trigger("reloadGrid");
}

function uninvoicetofun(){
	uninvoicetoDate = $("#uninvoicetoDate").val();
	$("#uninvoicesGrid").jqGrid('GridUnload');
	loadUninvoicesList(searchData,uninvoicefromDate,uninvoicetoDate);
	$("#uninvoicesGrid").trigger("reloadGrid");
}

/*
 * $( "#uninvoicefromDate" ).change(function() { alert('inside fun');
 * uninvoicefromDate = $("#uninvoicefromDate").val();
 * $("#uninvoicesGrid").jqGrid('GridUnload');
 * loadUninvoicesList(searchData,uninvoicefromDate,uninvoicetoDate);
 * $("#uninvoicesGrid").trigger("reloadGrid"); });
 *  $( "#uninvoicetoDate" ).change(function() { uninvoicetoDate =
 * $("#uninvoicetoDate").val(); $("#uninvoicesGrid").jqGrid('GridUnload');
 * loadUninvoicesList(searchData,uninvoicefromDate,uninvoicetoDate);
 * $("#uninvoicesGrid").trigger("reloadGrid"); });
 */
var posit_outside_loadUninvoicesList=0;
function loadUninvoicesList(searchData,uninvoicefromDate,uninvoicetoDate){
	$("#uninvoicesGrid").jqGrid({
		url:'./veInvoiceBillController/getUninvoiceList?searchData='+searchData+'&fromDate='+uninvoicefromDate+'&toDate='+uninvoicetoDate,
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#uninvoicesGridPager'),
		colNames:['Receive Date','JobNumber','Bill Date','Due Date','PO Number','Invoice #', 'Payble To','JobName', 'Amount', 'Paid', 'veBillId', 'rxCustomerId','vePoid','joreleaseDetailid','Check #','Date Paid',"amt",'',''],
		colModel :[
		           
            {name:'receiveDate', index:'receiveDate', align:'center', width:40,  editable:false,formatter:dateformatter}, 
            {name:'ponumber', index:'ponumber', align:'center', width:40,  editable:false},
           	{name:'billDate', index:'billDate', align:'center', width:30, editable:true,hidden:true,formatter:dateformatter},
           	{name:'dueDate', index:'dueDate', align:'center', width:30, editable:true,hidden:true,formatter:dateformatter},
			{name:'jobNumber', index:'jobNumber', align:'left', width:30,hidden:true},
			{name:'veInvoiceNumber', index:'veInvoiceNumber', align:'', width:30,hidden:false, editable:true},
			{name:'payableTo', index:'payableTo', align:'', width:100,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal; padding-left:4px;"';}},
			{name:'jobName', index:'jobName', align:'center', width:40,  editable:false},
			{name:'billAmount', index:'billAmount', align:'right', width:30,hidden:false, editable:true, formatter:customCurrencyFormatter},
			{name:'appliedAmount', index:'appliedAmount', align:'right', width:30,hidden:true, editable:true, formatter:customCurrencyFormatter},
			{name:'veBillId', index:'veBillId', align:'center', width:80, hidden:true, editable:false},
			{name:'rxCustomerId', index:'rxCustomerId', align:'center', width:40, hidden:true, editable:true},
			{name:'vePoid', index:'vePoid', align:'center', width:40, hidden:true, editable:true},
			{name:'joreleasedetailid', index:'joreleasedetailid', align:'center', width:40, hidden:true, editable:true},
			{name:'chkNo', index:'chkNo', align:'center', width:30, hidden:true, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.creditUsed!="0"){  return 'style="color: red; font-weight:bold;"';}},formatter:displayChkNo},
			{name:'datePaid', index:'datePaid', align:'center', width:30, hidden:true, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.creditUsed!="0"){return 'style="color: red; font-weight:bold;"';}},formatter:dateformatter},
			{name:'amt', index:'amt', align:'center', width:30, hidden:true, editable:true},
			{name:'transaction_status', index:'transaction_status', align:'center', width:40, hidden:true, editable:true},
			{name:'creditUsed', index:'creditUsed', align:'center', width:40, hidden:true, editable:true}
			
			
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#uninvoicesGridPager',
		sortname: 'billDate', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: 'Vendor Invoices/No Customer Billing',
		// autowidth: true,
		height:570,	width: 950,/* scrollOffset:0, */ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadBeforeSend: function(xhr) {
			posit_outside_loadUninvoicesList= jQuery("#uninvoicesGrid").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		loadComplete: function(data) {
			
	    },
	    gridComplete: function () {
            jQuery("#uninvoicesGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_loadUninvoicesList);
            posit_outside_loadUninvoicesList=0;
		   },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var veBillId = $("#uninvoicesGrid").jqGrid('getCell', rowId, 'veBillId');
			document.location.href = "./viewInsideJobVendorInvoice?veBillID=" + veBillId;
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
	}).navGrid('#uninvoicesGridPager',// {cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
}
function checkFormValidation(){
	var checkformvalue1=$("#payablePO").val();             
	var checkformvalue2=$("#recDateIdPO").val();  
	var checkformvalue3=$("#datePO").val();
	var checkformvalue4=$("#shipviaPO").val();
	var checkformvalue5=$("#vendorInvoicePO").val();
	var checkformvalue6=$("#apacctPO").val();
	var checkformvalue7=$("#shipDatePO").val();
	var checkformvalue8=$("#proPO").val();
	var checkformvalue9=$("#invwithPoReason").val();
	var checkformvalue10=$("#subtotalGeneralId").val();
	var checkformvalue11=$("#freightGeneralId").val();
    var checkformvalue12=$("#totalGeneralId").val();
    var checkformvalue13=$("#duePO").val();
    var checkformvalue14=$('#viStatusButtonPOtxt').val();

	var totalcheckformvalue=checkformvalue1+checkformvalue2+checkformvalue3+checkformvalue4+checkformvalue5+checkformvalue6+checkformvalue7+checkformvalue8+checkformvalue9+checkformvalue10+checkformvalue11+checkformvalue12+checkformvalue13+checkformvalue14;
	return totalcheckformvalue;
	}
function comparetwogriddata(oldgriddata,newgriddata){
	var oldgridrows=oldgriddata.length;
	var newgridrows=newgriddata.length;
	var returnvalue=true;
	if(oldgridrows!=newgridrows){
		returnvalue=false;
	}
	if(returnvalue){
		for(var i=0;i<newgriddata.length;i++){
			var oldrow=oldgriddata[i];
			var newrow=newgriddata[i];
			if(JSON.stringify(oldrow)!=JSON.stringify(newrow)){
				returnvalue=false;
			}
		}
	}
	return returnvalue;
}


function printuninvoiceList(){
	window.open('./veInvoiceBillController/printUninvoiceList?searchData='+searchData+'&fromDate='+uninvoicefromDate+'&toDate='+uninvoicetoDate);
	}

function loadAccountsPayableList(searchData,fromDate,toDate){
	$("#accountsPayableGridID").empty();
	$("#accountsPayableGridID").append("<table id='accountsPayableGrid'></table><div id='accountsPayableGridPager'></div>");
	$("#accountsPayableGrid").jqGrid({
		// url:'./veInvoiceBillController/getVeBillList',
		url:'./veInvoiceBillController/getAccountsPayableList?searchData='+searchData+'&fromDate='+fromDate+'&toDate='+toDate,
		datatype: 'JSON',
		mtype: 'POST',
		//pager: jQuery('#accountsPayableGridPager'),
		colNames:['Invoice Date','Due Date','PO #','Invoice #', 'Vendor', 'Age','Current','30 Days','60 Days','90 Days','Total', 'Paid', 'veBillId', 'rxCustomerId','vePoid','joreleaseDetailid','Check #','Date Paid',"amt",'',''],
		colModel :[
           	{name:'billDate', index:'billDate', align:'center', width:30, editable:true,hidden:false,formatter:dateformatter},
           	{name:'dueDate', index:'dueDate', align:'center', width:30, editable:true,hidden:true,formatter:dateformatter},
			{name:'ponumber', index:'ponumber', align:'left', width:30,hidden:false},
			{name:'veInvoiceNumber', index:'veInvoiceNumber', align:'', width:30,hidden:false, editable:true},
			{name:'payableTo', index:'payableTo', align:'', width:100,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal; padding-left:4px;"';}},
			{name:'age', index:'age', align:'center', width:18,hidden:false, editable:true},
			{name:'currentAmount', index:'currentAmount', align:'right', width:30,hidden:false, editable:true, formatter:customCurrencyFormatter},
			{name:'age30Amount', index:'age30Amount', align:'right', width:35,hidden:false, editable:true, formatter:customCurrencyFormatter},
			{name:'age60Amount', index:'age60Amount', align:'right', width:35,hidden:false, editable:true, formatter:customCurrencyFormatter},
			{name:'age90Amount', index:'age90Amount', align:'right', width:35,hidden:false, editable:true, formatter:customCurrencyFormatter},
			{name:'billAmount', index:'billAmount', align:'right', width:40,hidden:false, editable:true, formatter:customCurrencyFormatter},
			{name:'appliedAmount', index:'appliedAmount', align:'right', width:30,hidden:true, editable:true, formatter:customCurrencyFormatter},
			{name:'veBillId', index:'veBillId', align:'center', width:80, hidden:true, editable:true},
			{name:'rxCustomerId', index:'rxCustomerId', align:'center', width:40, hidden:true, editable:true},
			{name:'vePoid', index:'vePoid', align:'center', width:40, hidden:true, editable:true},
			{name:'joreleasedetailid', index:'joreleasedetailid', align:'center', width:40, hidden:true, editable:true},
			{name:'chkNo', index:'chkNo', align:'center', width:30, hidden:true, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.creditUsed!="0"){  return 'style="color: red; font-weight:bold;"';}},formatter:displayChkNo},
			{name:'datePaid', index:'datePaid', align:'center', width:30, hidden:true, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {if(rawObject.creditUsed!="0"){return 'style="color: red; font-weight:bold;"';}},formatter:dateformatter},
			{name:'amt', index:'amt', align:'center', width:30, hidden:true, editable:true},
			{name:'transaction_status', index:'transaction_status', align:'center', width:40, hidden:true, editable:true},
			{name:'creditUsed', index:'creditUsed', align:'center', width:40, hidden:true, editable:true}
			
		],
		rowNum: 1000000,
		pgbuttons: true,	
		recordtext: '',
		//rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		//pager: '#accountsPayableGridPager',
		sortname: 'payableTo', 
		sortorder: "asc",
		imgpath: 'themes/basic/images',	
		caption: 'Accounts Payable',
		// autowidth: true,
		height:547,	width: 1140,
		/* scrollOffset:0, */ 
		rownumbers:true, 
		altRows: true, 
		altclass:'myAltRowClass', 
		rownumWidth: 45,
		footerrow: true,
	    userDataOnFooter : true,
		loadComplete: function(data) {

			//$("#JournalsGrid").setSelection(1, true);
			var allRowsInGrid = $('#accountsPayableGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			
			var sumCurrentAmount = 0;
			var sumAge30Amount = 0;
			var sumAge60Amount = 0;
			var sumAge90Amount = 0;
			var sumBalanceAmount = 0;
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.currentAmount;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sumCurrentAmount = parseFloat(sumCurrentAmount) + parseFloat(number6); 
			});
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.age30Amount;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sumAge30Amount = parseFloat(sumAge30Amount) + parseFloat(number6); 
			});
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.age60Amount;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sumAge60Amount = parseFloat(sumAge60Amount) + parseFloat(number6); 
			});
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.age90Amount;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sumAge90Amount = parseFloat(sumAge90Amount) + parseFloat(number6); 
			});
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.billAmount;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sumBalanceAmount = parseFloat(sumBalanceAmount) + parseFloat(number6); 
			});
			
			 
			$('.footrow').css('color','#000000');
			console.log("Tot :: "+sumBalanceAmount+"----");
		    $(this).jqGrid('footerData','set',{
		    	age:"Totals",
		    	currentAmount:formatCurrency(sumCurrentAmount),
		    	age30Amount:formatCurrency(sumAge30Amount),
		    	age60Amount:formatCurrency(sumAge60Amount),
		    	age90Amount:formatCurrency(sumAge90Amount),
		    	billAmount:formatCurrency(sumBalanceAmount)
		    	});
			
		
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var jobNumber = rowData['jobNumber'];
			var jobName = "" + rowData['description'];
			var jobCustomer = rowData['customerName'];
			var jobStatus = rowData['jobStatus'];
			var veBillId = rowData['veBillId'];
			var vePoid = rowData['vePoid'];
			var ponumber = rowData['ponumber'];
			var joreleasedetailId=rowData['joreleasedetailid'];
			var transaction_status=rowData['transaction_status'];
			/*if(transaction_status==2){
				jQuery("#addNewVeInvFmDlgbutton").css("display", "none");
				jQuery("#addNewVeInvFmDlgclsbutton").css("display", "inline-block");
			}else{
				jQuery("#addNewVeInvFmDlgbutton").css("display", "inline-block");
				jQuery("#addNewVeInvFmDlgclsbutton").css("display", "none");
			}*/
			if(vePoid != null && vePoid != ''){
				console.log("preloadVendorInvoiceData vepoid and ponumber :: "+vePoid+" ::  "+ponumber);
				preloadVendorInvoiceData(veBillId);
			}
			else{
				console.log("preloadVendorInvoiceFromJobData vepoid and ponumber :: "+vePoid+" ::  "+ponumber);
				preloadVendorInvoiceFromJobData(veBillId);
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
	}).navGrid('#accountsPayableGridPager',// {cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
}

function printAccountsPayable(){
//	$.ajax({
		window.open('./veInvoiceBillController/getAccountsPayableListCSV?searchData='+searchData+'&fromDate='+fromDate+'&toDate='+toDate);
//       type: 'GET',     
//        success: function (data) {
//        }
//    });
}

function vendorAccountsDetails(){
	var listVal = $('#vendorAccountList').val();
	if(listVal=='4'){
		$('#accountsPayableImgID').hide();
		uninvoicedClick();
		}
	if(listVal=='1'){
		constData='11';
		$('#invoicesGridID').css('display','none');
		$('#invoicesGridID').empty();
		$('#accountsPayableImgID').show();
		$('#accountsPayableGridID').css('display','block');
		loadAccountsPayableList(searchData,fromDate,toDate);
	}
	if(listVal=='0'){
		constData='0';
		$('#accountsPayableGridID').css('display','none');
		$('#accountsPayableImgID').hide();
		$('#accountsPayableGridID').empty();
		$('#invoicesGridID').css('display','block');
		loadInvoicesList(searchData,fromDate,toDate)
	}
	if(listVal=='2'){
		constData='0';
		$('#accountsPayableGridID').css('display','none');
		$('#accountsPayableImgID').hide();
		$('#accountsPayableGridID').empty();
		$('#invoicesGridID').css('display','block');
		loadInvoicesList(searchData,fromDate,toDate)
	}
	if(listVal=='3'){
		constData='0';
		}
	
}


function getDueonDayswithDate(dueondaysforrxMasterid,dateValue,poStatus)
{
	var d = new Date(dateValue);
	var today='';
		
		$.ajax({
        url: './veInvoiceBillController/getDueOnDays?rxMasterID='+dueondaysforrxMasterid,
        type: 'POST', 
        async: false,
        success: function (data) {
        	today =data.dueonDaysPO ;
        	
        	$("#duedaysfmpojob").val(today);
        	d.setDate(Number(d.getDate())+Number(data.dueonDaysPO));
				var day = ("0" + d.getDate()).slice(-2);
				var month = ("0" + (d.getMonth() + 1)).slice(-2);
				today = (month)+"/"+(day)+"/"+d.getFullYear();
				
			if(poStatus == "withPO")
				$("#duePO").val(today);
			else
				$("#due").val(today);
				
        }
	
	});

}




//New Method

var posit_outside_loadlineItemGrid=0;
function loadlineItemGrid()
{
	var gridstatus=true;
	var billID ='';
	var vepoID,query;
	console.log($('#veBillIdPO').val());
	if($('#veBillIdPO').val() != null && $('#veBillIdPO').val() != '')
	{
		vepoID = $('#veBillIdPO').val();
		query = './veInvoiceBillController/vendorInvoicelineitemGrid';
		console.log("veBillIdPO Empty :: "+vepoID+" :: Query ::  "+query);
	}
	else
	{
		vepoID = $('#vepoidPO').val();
		query = './veInvoiceBillController/polineitemGrid';
		console.log("veBillIdPO Empty :: "+vepoID+" :: Query ::  "+query);
	
	}
	try {
		
		$("#jqgridLine").empty();
		$("#jqgridLine").append("<table id='lineItemGrid'></table><div id='lineItemPager' style='display:none;'></div>");
	 $('#lineItemGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#lineItemPager'),
			url:query,
			postData: {'vepoID':vepoID},
			loadonce: false,
			colNames:['Product No','', 'Description','Qty', 'Cost Each', 'Mult.', 'Tax','Net Each', 'Amount', 'VeBillId', 'prMasterID' ,'veBillDetailId', 'vePodetailID','<img src="./../resources/images/delete.png" style="vertical-align: middle;">',''],//
			colModel :[
		{name:'prItemCode',index:'prItemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
			dataEvents: [
		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select(); 
  		   		       				  } },
		    			  { type: 'click', data: { i: 7 }, fn: function(e) {var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select(); } }
		    			  ],
			dataInit: function (elem) {
			 	//var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	            $(elem).autocomplete({
	                source: 'jobtabs3/productCodeWithNameList',
	                minLength: 1,autoFocus: true,
	                select: function (event, ui) {
	                	var IncTaxOnPOAndInvoices=getSysvariableStatusBasedOnVariableName('IncTaxOnPOAndInvoices');
	                	var id = ui.item.id;
	                	var product = ui.item.label;
	                	var aSelectedRowId =elem.closest('tr').id;
	                	$("#"+aSelectedRowId+"_prMasterId").val(id);
	                	$("#new_row_prMasterId").val(id);
	                	
	                	$.ajax({
					        url: './getLineItems?prMasterId='+id,
					        type: 'POST',       
					        success: function (data) {
					        	
					        	$.each(data, function(key, valueMap) {										
									
					        		if("lineItems"==key)
									{				
										$.each(valueMap, function(index, value){						
											
												$("#new_row_description").val(value.description);
												$("#"+aSelectedRowId+"_description").val(value.description);
												$("#new_row_unitCost").val(value.lastCost);
												$("#"+aSelectedRowId+"_unitCost").val(value.lastCost);
												$("#new_row_priceMultiplier").val(value.pomult);
												$("#"+aSelectedRowId+"_priceMultiplier").val(value.pomult);
												console.log(value.isTaxable+"=="+IncTaxOnPOAndInvoices+"=="+IncTaxOnPOAndInvoices[0].valueLong);
												
												/*					
												 * Eric gave this explanation
												 * 	   Bring
												Taxable    TaxOnSettings  taxonpo  taxincludeornot
													0			0			0			no
													0			1			1			yes
													1			0			0			no
													1			1			1			yes
													*/
												
												
												var includetaxchecked=(IncTaxOnPOAndInvoices==null)?0:IncTaxOnPOAndInvoices[0].valueLong;
												if((value.isTaxable == 1 && includetaxchecked==1)
												|| (value.isTaxable == 0 && includetaxchecked==1)		
												)	
												{
													$("#new_row_taxable").prop("checked",true);
													$("#"+aSelectedRowId+"_taxable").prop("checked",true);
												}
												else
												{
													$("#new_row_taxable").prop("checked",false);
													$("#"+aSelectedRowId+"_taxable").prop("checked",false);
												}			
																	
										});
										
										$("#new_row_description").focus();
										$("#"+aSelectedRowId+"_description").focus();
									}							        		
								});
					        }
					    });
	                }
	            });
	      }	
		},editrules:{edithidden:false,required: true}},
		{name:'inLineNoteImage', index:'inLineNoteImage', align:'right', width:25,hidden:false, editable:false, formatter:veInvinlineNoteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
		{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50,
			dataEvents: [
		       			  { type: 'focus', data: { i: 7 }, fn: function(e) {
		       				  var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select(); 
  		   		       				  } },
		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
		    				  var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select();
  		   		       				  } },
	                        {
	                            type: 'keypress',
	                            fn: function(e) {
	                           	 var key = e.which;                           	
	                       		 if(key == 13)  // the enter key code
	                       		  {
	                       			var rowid=$(e.target).closest('tr')[0].id;
	                       			Calculategrideditrowvalues_VI(rowid);
	                       			 $("#lineItemGrid_ilsave").trigger("click");
	                       		      return true;
	                       		  }
	                                             }
	                            }
		    			  ]	
		},editrules:{edithidden:false},  
			cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},

		{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7,
			dataEvents: [
  			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
  				  var rowobji=$(e.target).closest('tr.jqgrow');
 		       		var textboxid=rowobji.attr('id');
	   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
	   		       				  e.target.select();
	   		       				  } },
			  { type: 'click', data: { i: 7 }, fn: function(e) { 
				  var rowobji=$(e.target).closest('tr.jqgrow');
	   		       		var textboxid=rowobji.attr('id');
   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
   		       				  e.target.select();
			  } },
	                        {
	                         type: 'change',
	                         fn: function(e) {
	                        	 var rowid=$(e.target).closest('tr')[0].id;
	                       			Calculategrideditrowvalues_VI(rowid);
	                         }
	                        },
	                        {
	                            type: 'keypress',
	                            fn: function(e) {
	                           	 var key = e.which;                           	
	                       		 if(key == 13)  // the enter key code
	                       		  {
	                       			var rowid=$(e.target).closest('tr')[0].id;
	                       			Calculategrideditrowvalues_VI(rowid);
	                       			 $("#lineItemGrid_ilsave").trigger("click");
	                       		      return true;
	                       		  }
	                                             }
	                            }

	                       ]		
		},editrules:{number:true,required: true}},
		{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10,
			dataEvents: [ { type: 'focus', data: { i: 7 }, fn: function(e) {
				var rowobji=$(e.target).closest('tr.jqgrow');
   		       		var textboxid=rowobji.attr('id');
 		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
 		       				  e.target.select();
			} },
						  { type: 'click', data: { i: 7 }, fn: function(e) { 
							  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();
						  } },
	                        {
	                         type: 'change',
	                         fn: function(e) {
	                        	 var rowid=$(e.target).closest('tr')[0].id;
	                        	 Calculategrideditrowvalues_VI(rowid);
	                         }
	                        },
	                        {
	                            type: 'keypress',
	                            fn: function(e) {
	                           	 var key = e.which;
	                      
	                       		 if(key == 13)  // the enter key code
	                       		  {
	                       			var rowid=$(e.target).closest('tr')[0].id;
	                       			Calculategrideditrowvalues_VI(rowid);
	                       			$("#lineItemGrid_ilsave").trigger("click")
	                       		  }
	                                             }
	                            }   
	      	                        
	                        ]		
		}, formatter:customCurrencyFormatter, formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "$ "}, editrules:{number:true,required: true}},
		{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:40,hidden:false, editable:true, editoptions:{size:15, alignText:'right',
			dataEvents: [ { type: 'focus', data: { i: 7 }, fn: function(e) {
				var rowobji=$(e.target).closest('tr.jqgrow');
		       		var textboxid=rowobji.attr('id');
	   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
	   		       				  e.target.select();
	   		       				  } },
						  { type: 'click', data: { i: 7 }, fn: function(e) {
							  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();
							  
						  } },
	                        {
	                         type: 'change',
	                         fn: function(e) {
	                        	 var rowid=$(e.target).closest('tr')[0].id;
	                        	 Calculategrideditrowvalues_VI(rowid);
	                         }
	                        },
	                        
	                        {
	                            type: 'keypress',
	                            fn: function(e) {
	                           	 var key = e.which;                           	
	                       		 if(key == 13)  // the enter key code
	                       		  {
	                       			var rowid=$(e.target).closest('tr')[0].id;
	                       			Calculategrideditrowvalues_VI(rowid);
		                       		
	                       			$("#lineItemGrid_ilsave").trigger("click");
	                       		  }
	                                             }
	                            }

	                        
	                        
	                     ]	
		}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
		{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
		{name:'netCast',index:'netCast',width:50 , align:'right',formatter:customCurrencyFormatter,hidden:true},
		{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right',readonly: 'readonly',
			dataEvents: [
		       			  { type: 'focus', data: { i: 7 }, fn: function(e) {
		       				var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select();
		       				  
		       			  } },
		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
		    				  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#lineItemGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();
		    			  } },
		    			  {
		                        type: 'keypress',
		                        fn: function(e) {
		                         var key = e.which;
		                  
		                      if(key == 13)  // the enter key code
		                       {
		                       $("#lineItemGrid_ilsave").trigger("click");
		                       }
		                                         }
		                        } 
		    			  ]		
		},editrules:{edithidden:true}},
		{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
		{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{custom:true,custom_func:check_productNofromoutside}},
		{name:'veBillDetailId', index:'veBillDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
		{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
		{name:'canDo', index:'canDo', align:'center',  width:20, hidden:false, editable:false, formatter:canDocheckboxFormatterVIPO,   editrules:{edithidden:true}},
		{name:'note',index:'note', align:'right', width:10,hidden:true, editable:false,editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}}
		],
			rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
			sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
			height:482.5,	width: 870, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
			loadBeforeSend: function(xhr) {
				posit_outside_loadlineItemGrid= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
			},
			loadComplete: function(data) {
				$("#lineItemGrid").setSelection(1, true);
				var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
				var aVal = new Array(); 
				var aTax = new Array();
				var sum = 0;
				var taxAmount = 0;
				var aTotal = 0;
				$.each(allRowsInGrid, function(index, value) {
					aVal[index] = value.quantityBilled;
					var number1 = aVal[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					sum = Number(sum) + Number(number6); 
				});
				
				gettaxpercentagefromvePO(vepoID);

				$('#subtotalGeneralId').val(formatCurrencynodollar(sum));
				var freight = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
				var tax = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
				var subTotal = $('#subtotalGeneralId').val().replace("$", "");
				console.log("SubTot :: "+subTotal);
				var aTotal = Number(floorFigureoverall(sum,2)) + parseFloat(freight) +Number(tax);
				$('#totalGeneralId').val(formatCurrencynodollar(aTotal));
				$('#balDuePO').val(formatCurrencynodollar(aTotal));	
				
		             $( "#lineItemGrid_iladd" ).trigger( "click" );
				if(oldgridFormValidation){
					oldlgserialize = jQuery("#lineItemGrid").jqGrid('getRowData');
					oldgridFormValidation=false;
				}
				var gridRows = $('#lineItemGrid').getRowData();
				global_vendorInvoicegridPOForm =  JSON.stringify(gridRows);
				global_vendorInvoicetotalPOForm=generatevendorInvoiceFormTotalIDSeriallize();
				global_vendorInvoicePOForm=$("#addNewVendorInvoiceFromPOForm").serialize();
				
			},gridComplete:function(){
				jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_loadlineItemGrid);
			},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){
				vendorinvoiceoutsiderowid=id;
				posit_outside_loadlineItemGrid= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
				/*var rowData = jQuery(this).getRowData(id); 
				var veBillDetailId = rowData["veBillDetailId"];
				var prMasterId = rowData["prMasterId"];
				$('#rxMasterIDPO').val(prMasterId);*/
				//console.log("veBillDetailId :: "+veBillDetailId);
			},
			ondblClickRow: function(rowid) {
				if(rowid=="new_row"){
					 
				 }else{
					 posit_outside_loadlineItemGrid= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
					 $("#lineItemGrid_ilcancel").trigger("click");
				     $("#lineItemGrid_iledit").trigger("click");
				 }
			},
			onCellSelect : function (rowid,iCol, cellcontent, e) {
				},
			//editurl:"./veInvoiceBillController/manpulateVendorInvoiceLineItem"
			cellsubmit: 'clientArray',
			editurl: 'clientArray',
	 });
	 var selrowid = $("#invoicesGrid").jqGrid('getGridParam','selrow');
	var releasedetail= $("#invoicesGrid").jqGrid('getCell', selrowid, 'joreleasedetailid');
	$("#lineItemGrid").jqGrid("navGrid","#lineItemPager", {
		add : false,
		edit : false,
		del : false,
		search:false,refresh:false}
	);
	$("#lineItemGrid").jqGrid("inlineNav","#lineItemPager", {
		add : true,
		addtitle:"Add",
		edit : true,
		edittitle:"Edit",
		save: true,
		savetitle:"Save",
		cancel : true,
		addParams: {
	       // position: "afterSelected",
			position: "last",
	        addRowParams: {
				keys : false,
				oneditfunc : function(rowid) {
					$("#info_dialog").css("z-index", "10000");
					var unitcost=$("#"+rowid+"_unitCost").val();
					removedollarsymbol(unitcost,rowid+"_unitCost");
				},
				successfunc : function(response) {
					$("#info_dialog").css("z-index", "12345");
						return true;
				},
				aftersavefunc : function(response) {
					$("#info_dialog").css("z-index", "12345");
					var ids = $("#lineItemGrid").jqGrid('getDataIDs');
					var veaccrrowid;
					if(ids.length==1){
						veaccrrowid = 0;
					}else{
						var idd = jQuery("#lineItemGrid tr").length;
						for(var i=0;i<ids.length;i++){
							if(idd<ids[i]){
								idd=ids[i];
							}
						}
						 veaccrrowid= idd;
					}
					if(vendorinvoiceoutsiderowid=="new_row"){
					$("#" + vendorinvoiceoutsiderowid).attr("id", Number(veaccrrowid)+1);
					var candoidrownum=Number(veaccrrowid)+1;
					$("#canDoID_new_row").attr("id", "canDoID_"+candoidrownum);
					$("#canDoVIID_new_row").attr("id","canDoVIID_"+candoidrownum);
					$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+candoidrownum);
					$("#canDoVIID_"+candoidrownum).attr("onclick","deleteRowFromJqGrid("+candoidrownum+");setvendorInvoicegridtotal();");
					$("#noteImageIcon_"+candoidrownum).attr("onclick","ShowNote('"+candoidrownum+"');");
					}
					/*var grid=$("#lineItemGrid");
					grid.jqGrid('resetSelection');
				    var dataids = grid.getDataIDs();
				    for (var i=0, il=dataids.length; i < il; i++) {
				        grid.jqGrid('setSelection',dataids[i], false);
				    }
				    */
				    setvendorInvoicegridtotal();
				    setTimeout(function(){
						 $("#lineItemGrid").jqGrid('resetSelection');
						 var grid=$("#lineItemGrid");
							grid.jqGrid('resetSelection');
						    var dataids = grid.getDataIDs();
						    for (var i=0, il=dataids.length; i < il; i++) {
						        grid.jqGrid('setSelection',dataids[i], false);
						    }
						    $( "#lineItemGrid_iladd" ).trigger( "click" );
						 $("#lineItemGrid").jqGrid('setSelection','new_row', true);
						},300);
					//alert("insidee");
				},
				errorfunc : function(rowid, response) {
					$("#info_dialog").css("z-index", "12345");
					return false;
				},
				afterrestorefunc : function(rowid) {
					$("#info_dialog").css("z-index", "12345");
				}
			}
	    },
	    editParams: {
	        // the parameters of editRow
	        key: false,
	        oneditfunc: function (rowid) {
	        	$("#info_dialog").css("z-index", "10000");
	        	//$("#new_row_unitCost").val(value.lastCost);
				var unitcost=$("#"+rowid+"_unitCost").val();
				removedollarsymbol(unitcost,rowid+"_unitCost");
					
	           // alert("row with rowid=" + rowid + " is editing.");
	        },
	   	successfunc : function(response) {
	   		$("#info_dialog").css("z-index", "12345");
	   		//alert("successfunc");
				return true;
		},
		aftersavefunc : function(response) {


			$("#info_dialog").css("z-index", "10000");
			
			var ids = $("#lineItemGrid").jqGrid('getDataIDs');
			var veaccrrowid;
			if(ids.length==1){
				veaccrrowid = 0;
			}else{
				var idd = jQuery("#lineItemGrid tr").length;
				for(var i=0;i<ids.length;i++){
					if(idd<ids[i]){
						idd=ids[i];
					}
				}
				 veaccrrowid= idd;
			}
			if(vendorinvoiceoutsiderowid=="new_row"){
				$("#" + vendorinvoiceoutsiderowid).attr("id", Number(veaccrrowid)+1);
				var candoidrownum=Number(veaccrrowid)+1;
				$("#canDoID_new_row").attr("id", "canDoID_"+candoidrownum);
				$("#canDoVIID_new_row").attr("id","canDoVIID_"+candoidrownum);
				$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+candoidrownum);
				$("#canDoVIID_"+candoidrownum).attr("onclick","deleteRowFromJqGrid("+candoidrownum+");setvendorInvoicegridtotal();");
				$("#noteImageIcon_"+candoidrownum).attr("onclick","ShowNote('"+candoidrownum+"');");
			}
			
			/*var grid=$("#lineItemGrid");
			grid.jqGrid('resetSelection');
		    var dataids = grid.getDataIDs();
		    for (var i=0, il=dataids.length; i < il; i++) {
		        grid.jqGrid('setSelection',dataids[i], false);
		    }
			
		    setvendorInvoicegridtotal();*/
			 setvendorInvoicegridtotal();
			    setTimeout(function(){
					 $("#lineItemGrid").jqGrid('resetSelection');
					 var grid=$("#lineItemGrid");
						grid.jqGrid('resetSelection');
					    var dataids = grid.getDataIDs();
					    for (var i=0, il=dataids.length; i < il; i++) {
					        grid.jqGrid('setSelection',dataids[i], false);
					    }
					    $( "#lineItemGrid_iladd" ).trigger( "click" );
					 $("#lineItemGrid").jqGrid('setSelection','new_row', true);
					},300);
		},
		errorfunc : function(rowid, response) {
			$("#info_dialog").css("z-index", "12345");
			return false;
		},
		afterrestorefunc : function(rowid) {
			$("#info_dialog").css("z-index", "12345");
		}
	    
	    },restoreAfterSelect :false
	});
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
	
	/*if($('#viStatusButtonPO').val()=="Paid")
	{
				
	$('#viStatusButtonPO').attr("disabled",true);
	
	$("#addNewVeInvFmDlgclsbutton").css("display","inline");
	$("#addNewVeInvFmDlgbutton").css("display","none");
	
	$("#paidStatus").css("display","inline");
	$("#chk_nofmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'chkNo'));
	$("#date_paidfmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'datePaid'));
	
	
	$("#add_lineItemGrid").addClass("ui-state-disabled");
	$("#edit_lineItemGrid").addClass("ui-state-disabled");
	$("#del_lineItemGrid").addClass("ui-state-disabled");
	$("#refresh_lineItemGrid").addClass("ui-state-disabled");
	}*/
				
}	
function veInvinlineNoteImage(cellValue, options, rowObject){
	var element = '';
	var id="noteImageIcon_"+options.rowId;
	var test=""+options.rowId;
	
	var noteValue = $("#lineItemGrid").jqGrid('getCell',options.rowId,'note');
	if(noteValue==false){
		noteValue=rowObject.note;
	}
	console.log("noteValue"+noteValue);
   if(noteValue != '' && noteValue != null && noteValue != undefined){
	   element = "<div><div align='center'><img src='./../resources/images/inline_jqGrid1.png' style='vertical-align: middle;' id='"+id+"' onclick=\"ShowNote('"+test+"')\"/></div></div>";	   
   }else{
	   element = "<div><div align='center'><img src='./../resources/images/inline_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick=\"ShowNote('"+test+"')\"/></div></div>";
   }
   return element;
} 

function ShowNote(row){
	/*try{
		*/
		//var jobStatus=$('#jobStatusList').val();
		//console.log("JobStatus"+jobStatus);
		if(typeof(jobStatus) != "undefined")
		{
		CKEDITOR.replace('lineItemNoteID', ckEditorconfigforinline);
		}
		else
		{
		CKEDITOR.replace('lineItemNoteID', ckEditorconfig);
		}
		
		//var rows = jQuery("#SOlineItemGrid").getDataIDs();
		//var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam',row);
		$("#SaveInlineNoteID").attr("onclick","SaveVeInvLineItemNote('"+row+"');");
		var notes = jQuery("#lineItemGrid").jqGrid ('getCell', row, 'note');	  
			CKEDITOR.instances['lineItemNoteID'].setData(notes);
			
			if($('#jobStatusList').val()== 4){
				$("#SaveInlineNoteID").css("display", "none");
			}else{
				$("#SaveInlineNoteID").css("display", "inline-block");
			}
			
			jQuery("#veInvLineItemNote").dialog("open");
		//	$(".nicEdit-main").focus();
			return true;
		/*}catch(err){
			console.log(err.message);
			alert(err.message);
		}*/
	}

	function SaveVeInvLineItemNote(row){
		var inlineText=  CKEDITOR.instances["lineItemNoteID"].getData(); 
		
		//var rows = jQuery("#SOlineItemGrid").getDataIDs();
		//var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
//		row=jQuery("#SOlineItemGrid").getRowData(rows[id-1]);
		  /*var notes = row['note'];
		  var cuSodetailId = row['cuSodetailId'];*/
		  //var aLineItem = new Array();
		  //aLineItem.push(inlineText);
		  var image="<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
		  if(inlineText==null || inlineText==undefined || inlineText==""){
			  image=undefined;
			  inlineText=undefined;
		  }
//		  if(isNaN(row)==true || row==undefined){
//			  $("#new_row_noteImage").val(image);
//			  $("#new_row_note").val(inlineText);
//		  }else{
		  	$("#lineItemGrid").jqGrid('setCell',row,'note', inlineText);
		  	 console.log($("#lineItemGrid").jqGrid('getCell',row,'note')  );
			  $("#lineItemGrid").jqGrid('setCell',row,'inLineNoteImage', image);
			  
//		  }
		  
		  
		  jQuery("#veInvLineItemNote").dialog("close");
		  CKEDITOR.instances['lineItemNoteID'].destroy();
		 
		  //aLineItem.push(cuSodetailId);
		/*$.ajax({
			url: "./salesOrderController/saveLineItemNote",
			type: "POST",
			data : {'lineItem' : aLineItem},
			success: function(data) {
				jQuery("#SoLineItemNote").dialog("close");
				$("#SOlineItemGrid").trigger("reloadGrid");
			}
			});*/
	}

	function SoCancelInLineNote(){
		jQuery("#veInvLineItemNote").dialog("close");
		 CKEDITOR.instances['lineItemNoteID'].destroy();
		return false;
	}

	jQuery(function(){
		jQuery("#veInvLineItemNote").dialog({
				autoOpen : false,
				modal : true,
				title:"InLine Note",
				height: 390,
				width: 635,
				buttons : {  },
				close:function(){
					return true;
				}	
		});
	});


function setvendorInvoicegridtotal(){
var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
var aVal = new Array(); 
var aTax = new Array();
var sum = 0;
var taxAmount = 0;
var aTotal = 0;
var a=0;
var rows = jQuery("#lineItemGrid").getDataIDs();
for(a=0;a<rows.length;a++)
{
	if(rows[a]!='new_row'){
	var row=jQuery("#lineItemGrid").getRowData(rows[a]);
	var id="#canDoID_"+rows[a];
	var canDo=$(id).is(':checked');
	if(!canDo){
	var	number = row['quantityBilled'];
	var number1 = number.replace("$", "");
	var number2 = number1.replace(".00", "");
	var number3 = number2.replace(",", "");
	var number4 = number3.replace(",", "");
	var number5 = number4.replace(",", "");
	var number6 = number5.replace(",", "");
	sum = Number(sum) + Number(number6); 

	
	//tax calculation 
	var taxable=row['taxable'];
	var taxpercent=$('#TaxID_vendorinvoice').val();
	if (taxable === 'Yes' && !canDo){
		var qb = number.replace(/[^0-9\.-]+/g,"");
		var eachamount=parseFloat(floorFigureoverall(qb,""),2);
		var multiplyamount=eachamount*taxpercent/100;
		taxAmount=Number(taxAmount)+Number(multiplyamount);
			}
			}
	}
}

//var taxpercent=$('#TaxID_vendorinvoice').val();
//$.each(allRowsInGrid, function(index, value) { 
//	aVal[index] = value.taxable;
//	if (aVal[index] === 'Yes'){
//		aTax[index] = value.quantityBilled;
//		var number1 = aTax[index].replace(/[^0-9\.-]+/g,"");
//		var eachamount=parseFloat(floorFigureoverall(number1,""),2);
//		var multiplyamount=eachamount*taxpercent/100;
//		taxAmount=Number(taxAmount)+Number(multiplyamount);
//	}
//});
$('#taxGeneralId').val(formatCurrencynodollar(taxAmount));

$('#subtotalGeneralId').val(formatCurrencynodollar(sum));
var freight = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
var tax = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
var subTotal = $('#subtotalGeneralId').val().replace("$", "");
console.log("SubTot :: "+subTotal);
var aTotal = Number(floorFigureoverall(sum,2)) + parseFloat(freight) +parseFloat(tax);
$('#totalGeneralId').val(formatCurrencynodollar(aTotal));
$('#balDuePO').val(formatCurrencynodollar(aTotal));
}

function Calculategrideditrowvalues_VI(editrowid){
	   var unitCost=$("#"+editrowid+"_unitCost" ).val();
		 unitCost=unitCost.replace(/[^0-9\.-]+/g,"");
		 if(unitCost==""){
			 unitCost=0;
		 }else if(unitCost<0){
			  
			jQuery(newDialogDiv).html('<span><b>Cost cant be negative</b></span>');
				jQuery(newDialogDiv).dialog({modal: false, width:300, height:150, title:"Error", 
				buttons:{
					"Close": function(){
						 $("#"+editrowid+"_unitCost").val('');
						 $("#"+editrowid+"quantityBilled").val('0.00');
						 $("#"+editrowid+"_unitCost").focus();
						jQuery(this).dialog("close");
			
					}
				}
				});
				
				 $("#"+editrowid+"_unitCost").val('');
				 $("#"+editrowid+"quantityBilled").val('0.00');
				 $("#"+editrowid+"_unitCost").focus();
			  
			  return false;
				/* setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
				result = [false, 'Quantity should not less than 0'];
				return result;*/
		 }
		 unitCost=Number(floorFigureoverall(unitCost,2));
		 var multiplier=$("#"+editrowid+"_priceMultiplier" ).val();
		 if(isNaN(multiplier)||multiplier==""||multiplier==0){
			 multiplier=0;
		 }else{
			 multiplier=Round_priceMultiplier(multiplier);
			 $("#"+editrowid+"_priceMultiplier").val(/*formatCurrency(*/multiplier/*)*/);
		 }
		
		 var quantity=$("#"+editrowid+"_quantityOrdered" ).val();
		 if(isNaN(quantity)||quantity==""||quantity==0){
			 quantity=0;
		 }else{
			 quantity=Number(floorFigureoverall(quantity,2));
		 }
		 
	   var amount=unitCost*quantity;
		 if(multiplier!=0){
			 amount=multiplier*amount;
		 }
		 amount=Number(floorFigureoverall(amount, 2));

		 $("#"+editrowid+"_quantityOrdered" ).val(/*formatCurrency(*/quantity/*)*/);
		 $("#"+editrowid+"_unitCost").val(/*formatCurrency(*/unitCost/*)*/);
		 $("#"+editrowid+"_quantityBilled" ).val(/*formatCurrency(*/amount/*)*/);
		 
		return true;
	    	 
	}

function generatevendorInvoiceFormTotalIDSeriallize(){
	var totalGeneralId=$("#totalGeneralId").val()+"";
	if(totalGeneralId!=null && totalGeneralId!=undefined && totalGeneralId!=""){
		totalGeneralId=totalGeneralId.replace(/[^0-9\.-]+/g,"");
		totalGeneralId=totalGeneralId.replace(",","");
	}
		return totalGeneralId;
}

function check_productNofromoutside( value, colname ) {
	 var result = null;
	if(value.length==0){
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
	
				result = [false, 'Invalid Product No. Please select from dropdown list.'];
				
				/*$("#lineItemGrid_iladd").addClass("ui-state-disabled");
				$("#lineItemGrid_iledit").addClass("ui-state-disabled");
				$("#lineItemGrid_ilsave").removeClass("ui-state-disabled");
				$("#lineItemGrid_ilcancel").removeClass("ui-state-disabled");*/
				
				globalcheckvalidation=false;
			
	}
	else{
		/*$("#lineItemGrid_iladd").removeClass("ui-state-disabled");
		$("#lineItemGrid_iledit").removeClass("ui-state-disabled");
		$("#lineItemGrid_ilsave").addClass("ui-state-disabled");
		$("#lineItemGrid_ilcancel").addClass("ui-state-disabled");*/
		 result = [true,""];
		 globalcheckvalidation=true;
	}
	return result;
}


function SaveVendorInvoicewithPO(operation){
	var vendorInvoiceDetails=$("#addNewVendorInvoiceFromPOForm").serialize();
	var gridRows = $('#lineItemGrid').getRowData();
	var vendorInvoiceGridDetails= JSON.stringify(gridRows);
	//var vendorInvoiceGridDetails =generatevendorInvoiceFormTotalIDSeriallize();
	
	var vendorInvoiceDetailsTotal=generatevendorInvoiceFormTotalIDSeriallize();
	if(global_vendorInvoicePOForm != vendorInvoiceDetails){
		console.log(global_vendorInvoicePOForm+"==="+vendorInvoiceDetails);
	}
	if(global_vendorInvoicegridPOForm != vendorInvoiceGridDetails){
		console.log(global_vendorInvoicegridPOForm+"==="+vendorInvoiceGridDetails);
	}
	if(global_vendorInvoicetotalPOForm != vendorInvoiceDetailsTotal){
		console.log(global_vendorInvoicetotalPOForm+"==="+vendorInvoiceDetailsTotal);
	}
	
	console.log($("#veInvnomandatory").data("manvalue")+"---"+operation);
	
	if($("#veInvnomandatory").data("manvalue")==1 && $("#vendorInvoicePO").val() =="" && operation !='close')
		{
		$('#mandveinvno').show();
		setTimeout(function(){
			$('#mandveinvno').hide();
			}, 2000);
		}
	else
		{	
		var itemCode=$("#new_row_prItemCode").val();
		if(itemCode!=undefined){
	if($('#veBillIdPO').val()!=null && $('#veBillIdPO').val()!="" ){
	if(global_vendorInvoicePOForm != vendorInvoiceDetails || global_vendorInvoicegridPOForm != vendorInvoiceGridDetails || global_vendorInvoicetotalPOForm != vendorInvoiceDetailsTotal)
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
		closeOnEscape: false,
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
		buttons:{
			"Yes": function(){
				jQuery(this).dialog("close");
				addVendorInvoiceFromPO('close');
			    return false;
			},
			"No": function ()	{
				
				 jQuery(this).dialog("close");
				 jQuery("#addNewVendorInvoiceFromPODlg").dialog("close");
			return false;	
			}}}).dialog("open");
	}else{
		jQuery("#addNewVendorInvoiceFromPODlg").dialog("close");
	}
	}else{
		if(operation=='close'){
			jQuery("#addNewVendorInvoiceFromPODlg").dialog("close");
		}else{
			addVendorInvoiceFromPO('close');
		}
		
	}
		}else{
			 var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes, please save prior to continuing.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
				closeOnEscape: false,
				open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
				buttons:{
					"OK": function(){
						jQuery(this).dialog("close");
						//$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
						//$("#new_row_quantityOrdered").focus();
					   // return false;
					}}}).dialog("open");
		 }
		}
}
function removedollarsymbol(value,id){
	if(value!=null && value!=""){
		value=value.replace("$","");
		$("#"+id).val(value);
	}
}
function canDocheckboxFormatter(cellValue, options, rowObject){
	var id="canDoID_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"' onclick='setvendorInvoicegridtotal();clickcheckboxChanges(this.id)'>";
return element;
}
function clickcheckboxChanges(id){
	id="#"+id;
	console.log(id);
    var canDo=$(id).is(':checked');
    if(canDo){
    	$(id).val("true");
    }else{
    	$(id).val("false");
    }
}







//WithoutPO New Implementation
function loadNewVendorInvoice(){
	
	var linegridstatus=true;
	
	var vepoID = $('#veBillIdJob').val();
	try{
	$("#vendorInvoiceGrid").jqGrid({										
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#vendorInvoicePager'),
		url:'./veInvoiceBillController/getVendorInvoice',					
		postData: {vepoID: function() { return $('#veBillIdJob').val(); }},
		colNames:['Account #','Account Description','Job #', 'Amount','veBillDistributionId','veBillId','coExpenseAccountId','joMasterId','<img src="./../resources/images/delete.png" style="vertical-align: middle;">','',''],
		colModel :[
           	{name:'number', index:'number', align:'left', width:20,hidden:false, editable:true,editoptions:{
           	 dataInit: function (elem) {
				 	
		            $(elem).autocomplete({
		                source: './veInvoiceBillController/getCoAccountDetails',
		                minLength: 1,autoFocus: true,
		                select: function (event, ui) {
		                	var aSelectedRowId =elem.closest('tr').id;
		                	var ID = ui.item.id; var product = ui.item.label;
		                	$("#"+aSelectedRowId+"_coExpenseAccountId").val(ID);
		    				$("#new_row_coExpenseAccountId").val(ID);
		    				$("#new_row_coexpenseaccountidforref").val(ID);
		    				if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]","");
		    				$("#"+aSelectedRowId+"_number").val(pro);
		    				$("#new_row_numberforref").val(pro[0].trim());
		    				$("#new_row_number").val(pro);
		    				
		    				 $("#vendorInvoiceGrid").jqGrid('setCell',aSelectedRowId,'desc', pro2);
		    				 $("#"+aSelectedRowId+"_jobNumber").val("");
		    				//$("#"+aSelectedRowId+"_desc").val(pro2);
		    				//$("#new_row_desc").val(pro2);
		    				}
		                }
		            });
		      },
		      dataEvents: [
	   		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
	   		       			var rowobji=$(e.target).closest('tr.jqgrow');
	   		       		var textboxid=rowobji.attr('id');
	   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
	   		       				  e.target.select(); 
	   		       				  } },
	   		    			  { type: 'click', data: { i: 7 }, fn: function(e) {
	   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
	  	   		       		var textboxid=rowobji.attr('id');
		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
		   		       				  e.target.select(); 
		   		       				  } },
                        { type: 'keypress', data: { i: 7 }, fn: function(e) {
   		    				var key = e.which;
                    		 if(key == 13)  // the enter key code
                    		  {
                    			 $("#vendorInvoiceGrid_ilsave").trigger("click");
	                    		    return false;  
                    		  }} 
   		    			 }
	   		    			  ]	
           	},editrules:{edithidden:false,required: true}
           	},
			{name:'desc', index:'desc', align:'', width:20,hidden:false, editable:false,editoptions:{
				dataEvents: [
      		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
	   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();  } },
    		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
	   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();  } },
                            { type: 'keypress', data: { i: 7 }, fn: function(e) {
       		    				var key = e.which;
                        		 if(key == 13)  // the enter key code
                        		  {
                        			 $("#vendorInvoiceGrid_ilsave").trigger("click");
    	                    		    return false;  
                        		  }} 
       		    			 }
    		    			  ]	}},
			{name:'jobNumber', index:'jobNumber', align:'', width:30,hidden:false, editable:true,editoptions:{
				dataEvents: [
     		       			  { type: 'focus', data: { i: 7 }, fn: function(e) {
	   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select(); 
	  		   		       				  } },
   		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select(); 
  		   		       				  } },
                          { type: 'keypress', data: { i: 7 }, fn: function(e) {
     		    				var key = e.which;
                      		 if(key == 13)  // the enter key code
                      		  {
                      			 $("#vendorInvoiceGrid_ilsave").trigger("click");
  	                    		    return false;  
                      		  }} 
     		    			 }
   		    			  ]	,
				dataInit: function (elem) {
				 	var aSelectedRowId = $("#vendorInvoiceGrid").jqGrid('getGridParam', 'selrow');
		            $(elem).autocomplete({
		                source: './veInvoiceBillController/getJobList',
		                minLength: 1,autoFocus: true,
		                select: function (event, ui) {
		                	var ID = ui.item.id; var product = ui.item.label;
		                	$("#"+aSelectedRowId+"_joMasterId").val(ID);
		    				$("#new_row_joMasterId").val(ID);
		    				if(product.indexOf('-[') !== -1){
		    					var pro = product.split("-["); 
		    					var pro2 = pro[1].replace("]",""); 
		    					$("#"+aSelectedRowId+"_jobNumber").val(pro2);
			    				$("#new_row_jobNumber").val(pro2);
		    					}
		                	
		                }
		            });
		      }
           	}	
				
				, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal; padding-left:4px;"';}},
			{name:'expenseAmount', index:'expenseAmount', align:'left', width:20,hidden:false, editable:true, formatter:customCurrencyFormatter,editoptions:{
				dataEvents: [
     		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
	   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select(); 
	  		   		       				  } },
   		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
   		    				  var rowobji=$(e.target).closest('tr.jqgrow');
  	  	   		       		var textboxid=rowobji.attr('id');
  		   		       		jQuery("#vendorInvoiceGrid").jqGrid('setSelection',textboxid, true);
  		   		       				  e.target.select(); 
  		   		       				  } },
                          { type: 'keypress', data: { i: 7 }, fn: function(e) {
     		    				var key = e.which;
                      		 if(key == 13)  // the enter key code
                      		  {
                      			 $("#vendorInvoiceGrid_ilsave").trigger("click");
  	                    		    return false;  
                      		  }} 
     		    			 }
   		    			  ]	
				
			}},
			{name:'veBillDistributionId', index:'veBillDistributionId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'coExpenseAccountId', index:'coExpenseAccountId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false},editrules:{custom:true,custom_func:coaccountidvalidation}},
			{name:'joMasterId', index:'joMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'canDoVIWO', index:'canDoVIWO', align:'center',  width:20, hidden:false, editable:false, formatter:canDocheckboxFormatterWOPO,   editrules:{edithidden:true}},
			{name:'numberforref', index:'', align:'center', width:20,hidden:true, editable:true},
			{name:'coexpenseaccountidforref', index:'', align:'center', width:20,hidden:true, editable:true}
			
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#vendorInvoicePager',
		sortname: 'billDate', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
		// autowidth: true,
		height:482.5,	width: 850,/* scrollOffset:0, */ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadBeforeSend: function(xhr) {
			posit_outside_loadNewVendorInvoice= jQuery("#vendorInvoiceGrid").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		loadComplete: function(data) {
			
			$("#vendorInvoiceGrid").setSelection(1, true);
			var allRowsInGrid = $('#vendorInvoiceGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.expenseAmount;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
			});
			
			// formatCurrencynodollar
			$('#totalDist').val(formatCurrencynodollar(sum));
			$('#total').val(formatCurrencynodollar(sum));
			$('#balDue').val(formatCurrencynodollar(sum));		
			
             global_vendorInvoiceWOPOForm=generatewopoFormSeriallize();
             $( "#vendorInvoiceGrid_iladd" ).trigger( "click" );
              
              
              setTimeout(function(){ var gridRows = $('#vendorInvoiceGrid').getRowData();global_vendorInvoicegridWOPOForm=JSON.stringify(gridRows);
             global_vendorInvoicetotalWOPOForm=generatevendorInvoiceWOPOFormTotalIDSeriallize();},300);
            //alert("first");
	    },
	    gridComplete: function () {
	    	jQuery("#vendorInvoiceGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_loadNewVendorInvoice);
	    	setnewvendorinvoiceGridDetails();
	    	//alert("second");
		 },
			ondblClickRow: function(rowid) {
				if(rowid=="new_row"){
					 
				 }else{
					 var rowData = jQuery(this).getRowData(rowid); 
						var veBillDistributionId = rowData["veBillDistributionId"];
						var coExpenseAccountId = rowData["coExpenseAccountId"];
						var joMasterId = rowData["joMasterId"];
						$('#coAccountID').val(coExpenseAccountId);
						$('#joMasterID').val(joMasterId);
						console.log("veBillDistributionId :: "+veBillDistributionId);
					 posit_outside_loadNewVendorInvoice= jQuery("#vendorInvoiceGrid").closest(".ui-jqgrid-bdiv").scrollTop();
					 $("#vendorInvoiceGrid_ilcancel").trigger("click");
				     $("#vendorInvoiceGrid_iledit").trigger("click");
				 }
			},
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		onSelectRow:  function(id){
			VeInvoicewoPODetailrowid=id;
			posit_outside_loadNewVendorInvoice= jQuery("#vendorInvoiceGrid").closest(".ui-jqgrid-bdiv").scrollTop();
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
    	//editurl:"./veInvoiceBillController/manpulateVendorInvoiceLineItemFromJob"
    	cellsubmit: 'clientArray',
		editurl: 'clientArray'
	}).navGrid("#vendorInvoicePager", {
		add : false,
		edit : false,
		del : false,
		search : false,
		refresh : false,
		pager : false,
	});
	$("#vendorInvoiceGrid").jqGrid(
			'inlineNav',
			'#vendorInvoicePager',{
				edit : true,
				edittext : "Edit",
				add : true,
				addtext : "Add",
				cancel : true,
				canceltext :"Cancel",
				savetext : "Save",
				refresh : true,
				alertzIndex : 10000,
				addParams : {
					position: "last",
					addRowParams : {
								keys : false,
								oneditfunc : function(id) {
									var expenseAmount=$("#"+id+"_expenseAmount").val();
									expenseAmount=expenseAmount.replace(/[^0-9\-.]+/g,"");
									if(expenseAmount==undefined ||expenseAmount==""||expenseAmount==null){
										expenseAmount=0.00;
									}
									$("#"+id+"_expenseAmount").val(expenseAmount);
									if(id=='new_row'){
										var rxMasterID=$("#rxMasterID").val();
										getDefaultAccountNumber(rxMasterID);
										
										}
								},
								successfunc : function(response) {
									console.log(response);
									return true;
								},
								aftersavefunc : function(response) {

									var ids = $("#vendorInvoiceGrid").jqGrid('getDataIDs');
									var veinvrowid;
									if(ids.length==1){
										veinvrowid = 0;
									}else{
										var idd = jQuery("#vendorInvoiceGrid tr").length;
										for(var i=0;i<ids.length;i++){
											if(idd<ids[i]){
												idd=ids[i];
											}
										}
										veinvrowid= idd;
									}
									if(VeInvoicewoPODetailrowid=="new_row"){
										var candoidrownum=Number(veinvrowid)+1;
									$("#" + VeInvoicewoPODetailrowid).attr("id", candoidrownum);
									$("#canDoVIWOID_new_row").attr("id","canDoVIWOID_"+candoidrownum);
									$("#canDoVIWOID_"+candoidrownum).attr("onclick","deleteRowFromJqGridWO("+candoidrownum+");setnewvendorinvoiceGridDetails();");
									}
									setnewvendorinvoiceGridDetails();
									setTimeout(function(){
										 $("#vendorInvoiceGrid").jqGrid('resetSelection');
										 var grid=$("#vendorInvoiceGrid");
											grid.jqGrid('resetSelection');
										    var dataids = grid.getDataIDs();
										    for (var i=0, il=dataids.length; i < il; i++) {
										        grid.jqGrid('setSelection',dataids[i], false);
										    }
										    $( "#vendorInvoiceGrid_iladd" ).trigger( "click" );
										 $("#vendorInvoiceGrid").jqGrid('setSelection','new_row', true);
										},300);
								    
								
								},
								
								errorfunc : function(rowid, response) {
									$("#info_dialog").attr('style', 'z-index: 10000 !important');
									$(".jqmID1").attr('style', 'z-index: 10000 !important');
									return false;
								},
								afterrestorefunc : function(rowid) {
									// alert("afterrestorefunc");
								}
							}
						},
				editParams : {
					keys : false,
//					refresh : true,
					successfunc : function(response) {
							console.log(response.responseText);
							return true;
								},
					aftersavefunc : function(id) {


						var ids = $("#vendorInvoiceGrid").jqGrid('getDataIDs');
						var veinvrowid;
						if(ids.length==1){
							veinvrowid = 0;
						}else{
							var idd = jQuery("#vendorInvoiceGrid tr").length;
							for(var i=0;i<ids.length;i++){
								if(idd<ids[i]){
									idd=ids[i];
								}
							}
							veinvrowid= idd;
						}
						if(VeInvoicewoPODetailrowid=="new_row"){
							var candoidrownum=Number(veinvrowid)+1;
						$("#" + VeInvoicewoPODetailrowid).attr("id", candoidrownum);
						$("#canDoVIWOID_new_row").attr("id","canDoVIWOID_"+candoidrownum);
						$("#canDoVIWOID_"+candoidrownum).attr("onclick","deleteRowFromJqGridWO("+candoidrownum+");setnewvendorinvoiceGridDetails();");
						}
						
						var grid=$("#vendorInvoiceGrid");
						grid.jqGrid('resetSelection');
					    var dataids = grid.getDataIDs();
					    for (var i=0, il=dataids.length; i < il; i++) {
					        grid.jqGrid('setSelection',dataids[i], false);
					    }
					    
						//formatCurrency(sum)
					
					    setnewvendorinvoiceGridDetails();
					    setTimeout(function(){
							 $("#vendorInvoiceGrid").jqGrid('resetSelection');
							 var grid=$("#vendorInvoiceGrid");
								grid.jqGrid('resetSelection');
							    var dataids = grid.getDataIDs();
							    for (var i=0, il=dataids.length; i < il; i++) {
							        grid.jqGrid('setSelection',dataids[i], false);
							    }
							    $( "#vendorInvoiceGrid_iladd" ).trigger( "click" );
							 $("#vendorInvoiceGrid").jqGrid('setSelection','new_row', true);
							},300);
					
					},
					errorfunc : function(rowid, response) {
						$("#info_dialog").attr('style', 'z-index: 10000 !important');
						$(".jqmID1").attr('style', 'z-index: 10000 !important');
						return false;
					},

					oneditfunc : function(id) {
						console.log('OnEditfunc'+id);
						checkAccountsDetailsforvalidation($("#"+id+"_coExpenseAccountId").val(),$("#"+id+"_number").val());
						var expenseAmount=$("#"+id+"_expenseAmount").val();
						expenseAmount=expenseAmount.replace(/[^0-9\-.]+/g,"");
						if(expenseAmount==undefined ||expenseAmount==""||expenseAmount==null){
							expenseAmount=0.00;
						}
						$("#"+id+"_expenseAmount").val(expenseAmount);
						if(id=='new_row'){
							var rxMasterID=$("#rxMasterID").val();
							getDefaultAccountNumber(rxMasterID);
							}
					}
					
				}
				
						,restoreAfterSelect :false
			});
	
}catch(err) {
    var text = "There was an error on this page.\n\n";
    text += "Error description: " + err.message + "\n\n";
    text += "Click OK to continue.\n\n";
    console.log(text);
}

function checkAccountsDetailsforvalidation(accountid,number){
	
	var rowId =  $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	$("#vendorInvoiceGrid").jqGrid('setCell',rowId,'numberforref', number);
	$("#vendorInvoiceGrid").jqGrid('setCell',rowId,'coexpenseaccountidforref', accountid);
}

function coaccountidvalidation( value, colname ) {
	
//	alert($("#new_row_number").val()+"=="+$("#new_row_numberforref").val()+"=="+$("#new_row_coExpenseAccountId").val()+"=="+$("#new_row_coexpenseaccountidforref").val())
	
	if($("#new_row_coExpenseAccountId").val()!=0)
	{
		if( $("#new_row_coExpenseAccountId").val() != undefined)	
		{
			if($("#new_row_number").val()!=$("#new_row_numberforref").val() && $("#new_row_coExpenseAccountId").val() == $("#new_row_coexpenseaccountidforref").val()){
				
					$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
					document.getElementById("saveTermsButton").disabled = true;
				
					setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
					result = [false, ''];
			}
			else{
					
					$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
					document.getElementById("saveTermsButton").disabled = false;
					
				 result = [true,""];
			}
		}
		else
		{
			var grid = $("#vendorInvoiceGrid");
			var rowId =  grid.jqGrid('getGridParam', 'selrow');

			var note = $("#"+rowId+"_number").val();
			var itemcodeforref = grid.jqGrid('getCell', rowId, 'numberforref');
			var prMasterId = $("#"+rowId+"_coExpenseAccountId").val();
			var prmasteridforref = grid.jqGrid('getCell', rowId, 'coexpenseaccountidforref');
			
			
			//alert(note+"=="+itemcodeforref+"=="+ prMasterId+"=="+prmasteridforref);
		if(note!=itemcodeforref && prMasterId == prmasteridforref){
				
				$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
				document.getElementById("saveTermsButton").disabled = true;
			
				setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
				result = [false, ''];
		}
		else{
				$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
				document.getElementById("saveTermsButton").disabled = false;
			
			 result = [true,""];
		}
			
		}
	}
	else
	{
		setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
		result = [false, ''];
	}
	
	return result;
}





/*$("#vendorInvoiceGrid_iladd").click(function() {
	if($("#release").val()==undefined){
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
			
	}else{
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		
	}
	document.getElementById("saveTermsButton").disabled = true;
	
});
$("#vendorInvoiceGrid_iledit").click(function() {
	if($("#release").val()==undefined){
		$('#SaveLineSOReleaseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
		
	}else{
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		
	}
	document.getElementById("saveTermsButton").disabled = true;

});*/

/*$("#vendorInvoiceGrid_ilsave").click(function() {

	if($("#info_head").text()!="Error")
	{
		document.getElementById("saveTermsButton").disabled = false;
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	}else{
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	}	
 });*/

/*$("#vendorInvoiceGrid_ilsave").click(function() {
	
	$("#infocnt").text("Invalid Account. Please select Account from the list");
	$("#info_dialog").css({"z-index":"15000","border":"1px solid"})
	
	if($("#info_head").text()!="Error")
	{
		document.getElementById("saveTermsButton").disabled = false;
			document.getElementById("saveTermsButton").disabled = false;
			$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	}
	else{
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
	}
	
});*/





$("#vendorInvoiceGrid_ilcancel").click(function() {

	if($("#info_head").text()!="Error")
	{
		document.getElementById("saveTermsButton").disabled = false;
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
	}else{
		$('#saveTermsButton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	}
	
 });


/*if($('#viStatusButton').val()=="Paid")
{
$("#add_vendorInvoiceGrid").addClass("ui-state-disabled");
$("#edit_vendorInvoiceGrid").addClass("ui-state-disabled");
$("#del_vendorInvoiceGrid").addClass("ui-state-disabled");
$("#refresh_vendorInvoiceGrid").addClass("ui-state-disabled");
$("#paidStatus").css("display","inline");
 var selrowid = $("#invoicesGrid").jqGrid('getGridParam','selrow');
$("#chk_nofmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'chkNo'));
$("#date_paidfmPO").text($("#invoicesGrid").jqGrid('getCell', selrowid, 'datePaid'));
$('#saveTermsButton').css("display","none");
$('#addNewVeInvFmDlgclsbuttonwithoutpo').css("display","inline");
$('#viStatusButton').attr("disabled",true);
}*/
}



function setnewvendorinvoiceGridDetails(){
	var allRowsInGrid = $('#vendorInvoiceGrid').jqGrid('getRowData');
	var aVal = new Array(); 
	var aTax = new Array();
	var sum = 0;
	var taxAmount = 0;
	var aTotal = 0;


	var rows = jQuery("#vendorInvoiceGrid").getDataIDs();
	for(a=0;a<rows.length;a++)
	{
		if(rows[a]!='new_row'){
		var row=jQuery("#vendorInvoiceGrid").getRowData(rows[a]);
		var id="#canDoWOID_"+rows[a];
		var canDo=$(id).is(':checked');
		if(!canDo){
		var	number = row['expenseAmount'];
		var number1 = number.replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		sum = Number(sum) + Number(number6);
		}
		}
	}
	// formatCurrencynodollar
	$('#totalDist').val(formatCurrencynodollar(sum));
	$('#total').val(formatCurrencynodollar(sum));
	$('#balDue').val(formatCurrencynodollar(sum));
}

function SaveVendorInvoicewithoutPO(operation){
	
	var itemCode=$("#new_row_number").val();
	if(itemCode!=undefined){
	//$('#SOReleaseSuggestedPriceID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	
	var vendorInvoiceDetails=generatewopoFormSeriallize();
	var gridRows = $('#vendorInvoiceGrid').getRowData();
	var vendorInvoiceGridDetails= JSON.stringify(gridRows);
	//var vendorInvoiceGridDetails =generatevendorInvoiceFormTotalIDSeriallize();
	var vendorInvoiceDetailsTotal=generatevendorInvoiceWOPOFormTotalIDSeriallize();
	if(global_vendorInvoiceWOPOForm != vendorInvoiceDetails){
		console.log(global_vendorInvoiceWOPOForm+"==="+vendorInvoiceDetails);
	}
	if(global_vendorInvoicegridWOPOForm != vendorInvoiceGridDetails){
		console.log(global_vendorInvoicegridWOPOForm+"==="+vendorInvoiceGridDetails);
	}
	if(global_vendorInvoicetotalWOPOForm != vendorInvoiceDetailsTotal){
		console.log(global_vendorInvoicetotalWOPOForm+"==="+vendorInvoiceDetailsTotal);
	}
//	console.log("global_vendorInvoiceWOPOForm"+global_vendorInvoiceWOPOForm);
//	console.log("vendorInvoiceDetails"+vendorInvoiceDetails);
	//$('#rxMasterID').val(data.Vebill.rxMasterId);
	//$('#veBillIdJob').val(data.Vebill.veBillId);
	if($('#veBillIdJob').val()!=null && $('#veBillIdJob').val()!="" && ($('#veBillIdJob').val()!= 0 ||operation=='close')){
	if(global_vendorInvoiceWOPOForm != vendorInvoiceDetails || global_vendorInvoicegridWOPOForm != vendorInvoiceGridDetails || global_vendorInvoicetotalWOPOForm != vendorInvoiceDetailsTotal)
	{
		
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
		closeOnEscape: false,
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
		buttons:{
			"Yes": function(){
				jQuery(this).dialog("close");
				addVendorInvoice(operation);
			    return false;
			},
			"No": function ()	{
				 jQuery(this).dialog("close");
				 jQuery("#addNewVendorInvoiceDlg").dialog("close");
				 
			return false;	
			}}}).dialog("open");
	}else{
		if(operation=="close"){
		jQuery("#addNewVendorInvoiceDlg").dialog("close");
		}
	}
	}else{
		if(operation=='close'){
			jQuery("#addNewVendorInvoiceDlg").dialog("close");
		}else{
			addVendorInvoice(operation);
		}
		
	}
	}else{
		 var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes, please save prior to continuing.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					//$( "#salesreleasetab ul li:nth-child(2)" ).addClass("ui-state-disabled");
					//$("#new_row_quantityOrdered").focus();
				   // return false;
				}}}).dialog("open");
	 }
}

function generatevendorInvoiceWOPOFormTotalIDSeriallize(){
	var totalGeneralId=$("#total").val()+"";
	if(totalGeneralId!=null && totalGeneralId!=undefined && totalGeneralId!=""){
		totalGeneralId=totalGeneralId.replace(/[^0-9\.-]+/g,"");
		totalGeneralId=totalGeneralId.replace(",","");
	}
		return totalGeneralId;
}

/*function canDocheckboxFormatterWOPO(cellValue, options, rowObject){
	var id="canDoWOID_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"' onclick='setnewvendorinvoiceGridDetails();clickcheckboxChanges(this.id)'>";
return element;
}*/
function drillintojob(){
	var aSelectedRowId = $("#invoicesGrid").jqGrid('getGridParam', 'selrow');
	var rowData = jQuery("#invoicesGrid").getRowData(aSelectedRowId); 
	var joreleasedetailid=rowData['joreleasedetailid'];
	if(joreleasedetailid!=null && joreleasedetailid>0){
	$.ajax({
		url: "./job_controller/jobDetailsfromReleaseDetail",  
		mType: "GET", 
		async:false,
		data : { 'joreleasedetailid' : joreleasedetailid },
		success: function(data){ 
			if(data!=null){
				var jobStatus = data["jobStatus"];
				var jobName= data["jobName"];
				var jobNumber= data["jobNumber"];
				var joMasterIDDD=data["joMasterID"];
				var urijobname=encodeBigurl(jobName);
				var aQryStr = "./jobflow?token=view&jobNumber="+jobNumber +"&jobName="+urijobname + "&jobStatus="+ jobStatus+"&joMasterID="+joMasterIDDD;
				createtpusage('job-Main Tab','view','Info','Job,Main Tab,View,JobNumber:'+jobNumber); 	
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = aQryStr;
				}
			}
		}
 	});
	}
}
function gettaxpercentagefromvePO(vepoid){
	$.ajax({
		url:"./jobtabs5/getPOGeneralDetails",
		type: "GET",
		data : {'vepoID' : vepoid},
		success: function(data) {
			$.each(data, function(key, valueMap) {
				if("vepo" == key)
				{
					$.each(valueMap, function(index, value){
					$('#TaxID_vendorinvoice').val(valueMap.taxRate);
					});
				}
			});		
		}
	});
}


function showDeniedPopupvendorinvoice()
{
	var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
							buttons: [{height:35,text: "OK",click: function() { 
								$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
								$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
								$(this).dialog("close"); }}]}).dialog("open");
	return true;
}
function canDocheckboxFormatterVIPO(cellValue, options, rowObject){
	var id="canDoVIID_"+options.rowId;
	//clickVIcheckboxChanges(this.id)
	var element = "<img src='./../resources/images/delete_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick='deleteRowFromJqGrid("+options.rowId+");setvendorInvoicegridtotal();'>";
return element;
}

function deleteRowFromJqGrid(jqGridRowId)
{
	 var veBillDetailId = jQuery("#lineItemGrid").jqGrid ('getCell', jqGridRowId, 'veBillDetailId');
	 if(veBillDetailId!=undefined && veBillDetailId!=null && veBillDetailId!="" && veBillDetailId!=0  && jqGridRowId!='new_row'){
		 deleteveBillDetailIDDetailId.push(veBillDetailId);
		 console.log(deleteveBillDetailIDDetailId.length+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			 $('#lineItemGrid').jqGrid('delRowData',jqGridRowId);
	 	}else{
	 		$('#lineItemGrid').jqGrid('delRowData',jqGridRowId);
	 	}
	 //$( "#vendorinvoice1_iladd" ).trigger( "click" );
	 jQuery("#lineItemGrid").jqGrid("setSelection", "new_row");
	 jQuery("#new_row_note").focus();
}
function canDocheckboxFormatterWOPO(cellValue, options, rowObject){
	var id="canDoVIWOID_"+options.rowId;
var element = "<img src='./../resources/images/delete_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick='deleteRowFromJqGridWO("+options.rowId+");setnewvendorinvoiceGridDetails();'>";
return element;
}

function deleteRowFromJqGridWO(jqGridRowId)
{
	 var veBillDetailId = jQuery("#vendorInvoiceGrid").jqGrid ('getCell', jqGridRowId, 'veBillDistributionId');
	 if(veBillDetailId!=undefined && veBillDetailId!=null && veBillDetailId!="" && veBillDetailId!=0 && jqGridRowId!='new_row'){
		 deleteveBillDistributionID.push(veBillDetailId);
		 console.log(deleteveBillDistributionID.length+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			 $('#vendorInvoiceGrid').jqGrid('delRowData',jqGridRowId);
	 	}else{
	 		$('#vendorInvoiceGrid').jqGrid('delRowData',jqGridRowId);
	 	}
	 
	 jQuery("#vendorInvoiceGrid").jqGrid("setSelection", "new_row");
	 jQuery("#new_row_number").focus();
}
function getDefaultAccountNumber(masterID){
	/**
	 * Added by Simon
	 * Reason for changing ID #576
	 */
	$.ajax({
        url: './getAandD?rxMasterId='+masterID,
        type: 'GET',       
        success: function (data) {
        	$( "#vendorInvoiceGrid_iladd" ).trigger( "click" );
        	$.each(data, function(key, valueMap) {									
				if("number"==key)
				{
					if(valueMap==null || valueMap==''){
						accountNumber='';
					}else{													
						accountNumber=valueMap;
					}
					$("#new_row_number").val(accountNumber);
					
				}

				if("description"==key)
				{
					if(valueMap==null || valueMap==''){
						accountDescription='';
					}else{													
						accountDescription=valueMap;
					}
					 $( "#vendorInvoiceGrid").jqGrid("setCell","new_row",'desc',accountDescription);
				}
				if("ceaId"==key)
				{
					if(valueMap==null || valueMap==''){
						coExpenseAccountId='';
					}else{													
						coExpenseAccountId=valueMap;
					}
					$("#new_row_coExpenseAccountId").val(coExpenseAccountId);
				}
				$("#new_row_number").focus();
			});
        }});
	
}

function generatewopoFormSeriallize(){
	var payable=$("#payable").val();
	var recDateId=$("#recDateId").val();
	var vendorInvoice=$("#vendorInvoice").val();
	var apacct=$("#apacct").val();
	var due=$("#due").val();
	var viStatusButtontxt=$("#viStatusButtontxt").val();
	var overallvariable=payable+recDateId+vendorInvoice+apacct+due+viStatusButtontxt;
	return overallvariable;
}