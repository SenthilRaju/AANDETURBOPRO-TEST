var fromDate='';
var toDate='';
var uninvoicefromDate='';
var uninvoicetoDate='';
var searchData='';		
var expenseJsonString ='';
var allText = $('#apacct').html();
			jQuery(document).ready(function(){
				
				//$("#search").css("display", "none");
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
				
				var billid = getUrlVars()["veBillID"];
				fetchvendorinvoiceDetails(billid);
				
				
				
				
			});
			
			function fetchvendorinvoiceDetails(billid){
				$.ajax({
			        url: './veInvoiceBillController/getVeBillListOutSide',
			        type: 'POST',  
			        data: {'veBillID':billid},  
			        success: function (data) {
			        	//loadlineItemGrid();
						var jobNumber = data.jobNumber;
						var jobName =data.jobName;
						var jobCustomer = data.customerName;
						var jobStatus = data.jobStatus;
						var veBillId =billid;
						var vePoid =data.vePoid;
						var ponumber = data.ponumber;
						var joreleasedetailId=data.joreleasedetailid;
						var transaction_status=data.transaction_status;
						if(transaction_status==2){
							jQuery("#addNewVeInvFmDlgbutton").css("display", "none");
							jQuery("#addNewVeInvFmDlgclsbutton").css("display", "inline-block");
						}else{
							jQuery("#addNewVeInvFmDlgbutton").css("display", "inline-block");
							jQuery("#addNewVeInvFmDlgclsbutton").css("display", "none");
						}
						if(vePoid != null && vePoid != '' && ponumber != null && ponumber != ''){
							console.log("preloadVendorInvoiceData vepoid and ponumber :: "+vePoid+" ::  "+ponumber);
							preloadVendorInvoiceData(veBillId);
						}
						else{
							console.log("preloadVendorInvoiceFromJobData vepoid and ponumber :: "+vePoid+" ::  "+ponumber);
							preloadVendorInvoiceFromJobData(veBillId);
						}
						
						
			        }
			    });
			}			
			
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
		    	document.location.href ="./vendorinvoicelist";
		    });
			
			$('#addNewVendorInvoiceDlg').bind('dialogclose', function(event) {
				document.location.href ="./vendorinvoicelist";
		    });
			
			
			

			
			function getSearchDetails()
			{
				if($('#searchJob').val() != null && $('#searchJob').val() != '')
				{
					$("#searchJob").autocomplete({
						disabled: true
					});
					//$("#searchJob").attr("autocomplete", "off");
					searchData = $('#searchJob').val();
					$('#searchJob').val('');
					//$("#searchJob").removeAttr("autocomplete");
				}
					
			}
			
			function dateformatter(cellValue, options, rowObject){
				
				if(cellValue === null){
					return "";
				}	
				
			//	console.log(cellValue);
				
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
						
					/*	if(i == dateArray.length-1)
						finaldate1 = finaldate1 + ("0" + (date.getMonth() + 1)).slice(-2)  + "/" +("0" + date.getDate()).slice(-2) + "/" + date.getFullYear();
						else
						finaldate1 = finaldate1 + ("0" + (date.getMonth() + 1)).slice(-2)  + "/" +("0" + date.getDate()).slice(-2) + "/" + date.getFullYear()+",";*/
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
			
			/** Currency formatter **/
			function customCurrencyFormatter(cellValue, options, rowObject) {
				return formatCurrency(cellValue);
			}
			jQuery(function() {
				/*jQuery("#addNewVendorInvoiceFromPODlg").dialog({
					autoOpen : false,
					height : 720,
					width : 980,
					title : "Add New Vendor Invoice",
					modal : true,
					close : function() {
						// $('#userFormId').validationEngine('hideAll');
						return true;
					}
				});*/
			});
			jQuery(function() {
				//addcreditDebitMemosDlg
				jQuery("#addNewVendorInvoiceDlg").dialog({
					autoOpen : false,
					height : 710,
					width : 960,
					title : "Add New Vendor Invoice",
					modal : true,
					close : function() {
						// $('#userFormId').validationEngine('hideAll');
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
					width : 360,
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
				if(po != null && po != ' ' && po.length > 0)
				{
					$.ajax({
				        url: './veInvoiceBillController/getPO',
				        type: 'POST',  
				        data: formData,    
				        success: function (data) {
				        	
				        	
				        	 if(data.Registered != null && data.Registered != ' ' && data.Registered.length > 0)
				        		 {
				        		 
				        		 console.log("Test");
						        	jQuery("#EnterPODlg").dialog("close");
						        	$('#InvalidPOMsg').text('Already Invoiced this purchase order #'+po);
						        	jQuery("#InvalidPODlg").dialog("open");
									return true;				
				        		 
				        		 }
				        	 else{
				        		 
				        	
				        	console.log("No PO  :: "+data);
				        	console.log('Length :: '+data.length);
					        if(data.Vepo.ponumber != null && data.Vepo.ponumber != ' ' && data.Vepo.ponumber.length > 0)
					        {
					        	document.getElementById("addNewVendorInvoiceFromPOForm").reset();
					        	console.log("PO Exist :: "+data.Vepo.ponumber);
					        	console.log("Address1 :: "+data.vendorAddress.address1);
					        	$('#ponumberPO').text(data.Vepo.ponumber);
					        	$('#vepoidPO').val(data.Vepo.vePoid);
					        	$('#shipviaPO').val(data.Vepo.veShipViaId);
					        	$('#freightGeneralId').val(formatCurrencynodollar(data.Vepo.freight));
								$('#taxGeneralId').val(formatCurrencynodollar(data.Vepo.taxTotal));					        	
								$('#vendorAddressPO').html(data.vendorAddress.address1);
								$('#vendorAddress1PO').html(data.vendorAddress.city).append(data.vendorAddress.state).append(data.vendorAddress.zip);
								$('#payablePO').val(data.vendorName.name);
								$('#rxMasterIDPayablePO').val(data.Vepo.rxVendorId);
								$('#apacctPO').val(data.SysAccountLinkage.coAccountIdap).attr('selected', true);
								$('#jobName').text(data.Vepo.updateKey);
					        	jQuery("#EnterPODlg").dialog("close");								
								loadlineItemGrid();
								jQuery("#addNewVendorInvoiceFromPODlg").dialog("open");
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
							
				        }
				    });
				}
				else
				{
					jQuery("#EnterPODlg").dialog("close");
					document.getElementById("addNewVendorInvoiceForm").reset();
					
					$.ajax({
				        url: './veInvoiceBillController/getApNumber',
				        type: 'POST',  				 
				        success: function (data) {
				        	$('#apacct').val(data.SysAccountLinkage.coAccountIdap).attr('selected', true);
				        }
					});
					
					loadNewVendorInvoice();
					jQuery("#addNewVendorInvoiceDlg").dialog("open");
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
					//autowidth: true,
					height:247,	width: 850,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
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
						
						//formatCurrencynodollar
						$('#totalDist').val(formatCurrencynodollar(sum));
						$('#total').val(formatCurrencynodollar(sum));
						$('#balDue').val(formatCurrencynodollar(sum));		
						
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
				}).navGrid('#vendorInvoicePager',//{cloneToTop:true},
					{add:linegridstatus,edit:linegridstatus,del:linegridstatus,refresh:linegridstatus,search:false},
					//-----------------------edit options----------------------//
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
					//var aJoMasterID = $('#joMasterID').val();
					if (aCoAccountID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; }
					//if (aJoMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
					
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
					//-----------------------add options----------------------//
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
							//var aJoMasterID = $('#joMasterID').val();
							if (aCoAccountID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; }
							//if (aJoMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; }  
							
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
					//-----------------------Delete options----------------------//
					{	
						closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
						caption: "Delete Product",
						msg: 'Delete the Product Record?',

						onclickSubmit: function(params){
							var veBillId = $('#veBillIdJob').val();
							var id = jQuery("#vendorInvoiceGrid").jqGrid('getGridParam','selrow');
							var key = jQuery("#vendorInvoiceGrid").getCell(id, 5);//'veBillDetailId' : key,
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
		//	$('.addinv').attr("disabled",true); 
			$('#viStatusButton').attr("disabled",true);
			}
			}

			$(function() { var cache = {}; var lastXhr='';
			$("#payable").autocomplete({ minLength: 1,timeout :1000,
				/* open: function(){ 
					$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
					$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				},  */
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
								});
					        	
								
					        }
					    }); 
					 $('#errorMsg').hide();
					//location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
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
				/* open: function(){ 
					$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
					$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				},  */
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
								});
					        	
								
					        }
					    }); 
					
					//location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
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
				//$( "select option:selected" )
				var selectedText = $('#apacct option:selected').text();
				var selectedValue = $('#apacct option:selected').val();
				//var allText = $('#apacct').html();
				//alert(allText);
				var arr = selectedText.split('|');
				$('#apacct option:selected').text($.trim(arr[0]));
				$('#apacct').empty();
			    var newOption = $('<option value="'+selectedValue+'" selected="selected">'+$.trim(arr[0])+'</option>');
			    //allText = newOption+allText;
			    //alert(allText);
			    $('#apacct').append(newOption);
			    $('#apacct').append(allText);
				//alert("$('#apacct').val() :: "+$('#apacct').val()+"  $('#apacct').text():: "+$('#apacct option:selected').text());
				//$('#apacct').text($('#apacct').val());
			}

			function addVendorInvoice()
			{
				/*if(!$('#addNewVendorInvoiceForm').validationEngine('validate')) {
					return false;
				}*/
				if($('#rxMasterID').val() == null || $('#rxMasterID').val() == '')
				{
					$('#errorMsg').show();
					$('#errorMsg').html('<span style="color:red;">*Payable is mantatory</span>');
					return false;
				}
				

				if($("#invStatus").val()=="addNewVendorInvoiceDlg")
					{
					editInvoiceDetails();
					}
				else
					{
					if($('#viStatusButton').val()=="Paid")
					{
					//$('.savehoverbutton').attr("disabled",true); 
					$('#viStatusButton').attr("disabled",true);
					}
					var totalDist1 = $('#totalDist').val().replace(/[^0-9-.]/g, '');
					var total1 = $('#total').val().replace(/[^0-9-.]/g, '');
					var balDue1 = $('#balDue').val().replace(/[^0-9-.]/g, '');
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
									var data = formData+'&buttonValue='+$('#viStatusButton').val()+'&reason='+$('#invreasonttextid').val()+
									'&totalDist1='+totalDist1+'&total1='+total1+'&balDue1='+balDue1+"&gridData="+expenseJsonString+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid;
									console.log(data);
									
										 $.ajax({
									        url: './veInvoiceBillController/addVendorInvoice',
									        type: 'POST',     
									        data: data,
									        success: function (data) {
									        	createtpusage('Company-Vendors-Pay Bills','Save Vendor Invoice','Info','Company-Vendors-Pay Bills,Saving Vendor Invoice,rxMasterID:'+$('#rxMasterID').val());
												console.log(data);
												document.location.href ="./vendorinvoicelist";
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
			
			
			function  addNewVendorInvoiceDlgwithReason()
			{
				if($('#viStatusButton').val()=="Paid")
				{
					//$('.savehoverbutton').attr("disabled",true); 
				$('#viStatusButton').attr("disabled",true);
				
				}

			//	alert(expenseJsonString);
				
				var formData = $('#addNewVendorInvoiceForm').serialize();
				
				var totalDist1 = $('#totalDist').val().replace(/[^0-9-.]/g, '');
				var total1 = $('#total').val().replace(/[^0-9-.]/g, '');
				var balDue1 = $('#balDue').val().replace(/[^0-9-.]/g, '');
				
			
				
				var data = formData+'&buttonValue='+$('#viStatusButton').val()+'&reason='+$('#invreasonttextid').val()+
				'&totalDist1='+totalDist1+'&total1='+total1+'&balDue1='+balDue1+"&gridData="+expenseJsonString;
				console.log(data);
				 $.ajax({
			        url: './veInvoiceBillController/addVendorInvoice',
			        type: 'POST',     
			        data: data,
			        success: function (data) {
			        	
						console.log(data);
						document.location.href ="./vendorinvoicelist";
						return true;
			        }
			    }); 
			}
			
			function addVendorInvoiceFromPO(operatorStatus)
			{
				$('#addNewVeInvFmDlgbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
				document.getElementById("addNewVeInvFmDlgbutton").disabled = true;

				if($('#rxMasterIDPO').val() == null || $('#rxMasterIDPO').val() == '')
				{
					$('#errorMsg').show();
					$('#errorMsg').html('<span style="color:red;">*Payable is mantatory</span>');
					return false;
				}
				
				if($("#invStatus").val()=="addNewVendorInvoiceFromPODlg")
					{
					editInvoiceDetails();
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
				//freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
				var vepoId = $('#vepoidPO').val();
				var veBillIdPO = $('#veBillIdPO').val();
				var prMasterIDPO = $('#rxMasterIDPO').val();
				var rxMasterIDPayablePO = $('#rxMasterIDPayablePO').val();
				var oper = 'add';
				console.log("veBillId---->"+veBillIdPO);
				if(veBillIdPO == null || veBillIdPO == '')
					veBillIdPO = 0;				
				var generalValues =   'recDateIdPO='+recDateIdPO+'&datePO='+datePO+'&duePO='+duePO+'&shipviaPO='+shipviaPO+'&vendorInvoicePO='+vendorInvoicePO
					+'&apacctPO='+apacctPO+'&postdatePO='+postdatePO+'&postDate1PO='+postDate1PO+'&shipDatePO='+shipDatePO
					+'&proPO='+proPO+'&subtotalGeneralId='+subtotalGeneralId+'&freightGeneralId='+freightGeneralId+'&taxGeneralId='+taxGeneralId
					+'&totalGeneralId='+totalGeneralId+'&balDuePO='+balDuePO+'&vepoId='+vepoId+'&veBillIdPO='+veBillIdPO
					+'&oper='+oper+'&prMasterIDPO='+prMasterIDPO+'&rxMasterIDPayablePO='+rxMasterIDPayablePO+'&buttonValue='+$('#viStatusButtonPO').val()
				    +'&reason='+$("#invreasonttextid").val();
				
				
				$.ajax({
					url: "./veInvoiceBillController/getPoTotalequalsvendorinvoice?vePoID="+vepoId,
					type: "POST",
					//data : aVendorInvoiceDetails,
					success: function(data) {
						if(data){
							
							 var newDialogDiv = jQuery(document.createElement('div'));
							jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the PO transaction Status?</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
							buttons:{
								"OK": function(){
									
									var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
									$.ajax({
										url: "./checkAccountingCyclePeriods",
										data:{"datetoCheck":$("#datePO").val(),"UserStatus":checkpermission},
										type: "POST",
										success: function(data) { 
											
											if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
											{
												periodid=data.cofiscalperiod.coFiscalPeriodId;
												yearid = data.cofiscalperiod.coFiscalYearId;
												
												
									
													$.ajax({
												        url: './veInvoiceBillController/addVendorInvoiceFromPO',
												        type: 'POST',     
												        data: generalValues+"&updatePO=yes"+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
												        success: function (data) {
												        	document.getElementById("addNewVeInvFmDlgbutton").disabled = false;
															$('#addNewVeInvFmDlgbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
												        	createtpusage('Company-Vendors-Invoices & Bills','Saving Invoice','Info','Company-Vendors-Invoices & Bills,Saving Invoice,vepoId:'+vepoId);
															console.log(data);
															document.location.href ="./vendorinvoicelist";
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
								    
									
									jQuery(this).dialog("close");
								    //jQuery("#openvendorinvoice").dialog("close");
								    return true;
								},
								Cancel: function ()	{
									jQuery(this).dialog("close");
									
									var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
									$.ajax({
										url: "./checkAccountingCyclePeriods",
										data:{"datetoCheck":$("#datePO").val(),"UserStatus":checkpermission},
										type: "POST",
										success: function(data) { 
											
											if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
											{
												periodid=data.cofiscalperiod.coFiscalPeriodId;
												yearid = data.cofiscalperiod.coFiscalYearId;

									
													$.ajax({
												        url: './veInvoiceBillController/addVendorInvoiceFromPO',
												        type: 'POST',     
												        data: generalValues+"&updatePO=no"+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
												        success: function (data) {
												        	
															console.log(data);
															document.location.href ="./vendorinvoicelist";
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
													
									//jQuery("#openvendorinvoice").dialog("close");
								return false;	
								}}}).dialog("open");
						}
					}
				});
					 
					}
			}
			
			
			function addNewVendorInvoiceFromPODlgwithReason()
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
				//freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
				var vepoId = $('#vepoidPO').val();
				var veBillIdPO = $('#veBillIdPO').val();
				var prMasterIDPO = $('#rxMasterIDPO').val();
				var rxMasterIDPayablePO = $('#rxMasterIDPayablePO').val();
				var oper = 'add';
				console.log("veBillId---->"+veBillIdPO);
				if(veBillIdPO == null || veBillIdPO == '')
					veBillIdPO = 0;				
				var generalValues =   'recDateIdPO='+recDateIdPO+'&datePO='+datePO+'&duePO='+duePO+'&shipviaPO='+shipviaPO+'&vendorInvoicePO='+vendorInvoicePO
					+'&apacctPO='+apacctPO+'&postdatePO='+postdatePO+'&postDate1PO='+postDate1PO+'&shipDatePO='+shipDatePO
					+'&proPO='+proPO+'&subtotalGeneralId='+subtotalGeneralId+'&freightGeneralId='+freightGeneralId+'&taxGeneralId='+taxGeneralId
					+'&totalGeneralId='+totalGeneralId+'&balDuePO='+balDuePO+'&vepoId='+vepoId+'&veBillIdPO='+veBillIdPO
					+'&oper='+oper+'&prMasterIDPO='+prMasterIDPO+'&rxMasterIDPayablePO='+rxMasterIDPayablePO+'&buttonValue='+$('#viStatusButtonPO').val()
				    +'&reason='+$("#invreasonttextid").val();
				
				$.ajax({
					url: "./veInvoiceBillController/getPoTotal?vePoID="+vepoId,
					type: "POST",
					//data : aVendorInvoiceDetails,
					success: function(data) {
						console.log('Data Server Subtotal-invoice Amount: '+data.replace(/[^0-9\.]+/g,"")+' - '+totalGeneralId);
						
						var vbsubtot = data.split('-*-');
						
						if(parseFloat(vbsubtot)<= parseFloat(totalGeneralId)){
							 var newDialogDiv = jQuery(document.createElement('div'));
							jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the PO transaction Status?</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
							buttons:{
								"OK": function(){
									var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
									$.ajax({
										url: "./checkAccountingCyclePeriods",
										data:{"datetoCheck":$("#datePO").val(),"UserStatus":checkpermission},
										type: "POST",
										success: function(data) { 
											
											if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
											{
												periodid=data.cofiscalperiod.coFiscalPeriodId;
												yearid = data.cofiscalperiod.coFiscalYearId;
									
														$.ajax({
													        url: './veInvoiceBillController/addVendorInvoiceFromPO',
													        type: 'POST',     
													        data: generalValues+'&updatePO=yes'+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
													        success: function (data) {
													        	
																console.log(data);
																document.location.href ="./vendorinvoicelist";
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
									
									jQuery(this).dialog("close");
								    //jQuery("#openvendorinvoice").dialog("close");
								    return true;
								},
								Cancel: function ()	{
									jQuery(this).dialog("close");
									
									var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
									$.ajax({
										url: "./checkAccountingCyclePeriods",
										data:{"datetoCheck":$("#datePO").val(),"UserStatus":checkpermission},
										type: "POST",
										success: function(data) { 
											
											if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
											{
												periodid=data.cofiscalperiod.coFiscalPeriodId;
												yearid = data.cofiscalperiod.coFiscalYearId;

												$.ajax({
												        url: './veInvoiceBillController/addVendorInvoiceFromPO',
												        type: 'POST',     
												        data: generalValues+'&updatePO=no'+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
												        success: function (data) {
												        	
															console.log(data);
															document.location.href ="./vendorinvoicelist";
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
													
									//jQuery("#openvendorinvoice").dialog("close");
								return false;	
								}}}).dialog("open");
						}
					}
				});
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
					  /* if(dataid=='2'){
						  console.log('dataid inside'+dataid);
						   	$('#Close').css("font-weight","bold");
					    	$('#Close').css("background","#0E2E55");
					    }else  if(dataid=='0'){
						   	$('#Hold').css("font-weight","bold");
					    	$('#Hold').css("background","#0E2E55");
					    }else  if(dataid=='1'){
						   	$('#Open').css("font-weight","bold");
					    	$('#Open').css("background","#0E2E55");
					    }else  if(dataid=='-1'){
						   	$('#Void').css("font-weight","bold");
					    	$('#Void').css("background","#0E2E55");
					    }else  if(dataid=='-2'){
						   	$('#Quote').css("font-weight","bold");
					    	$('#Quote').css("background","#0E2E55");
					    } */
					
				}catch(err){
					
					}
				
				
			}

			function onSetVIStatus(e){
				
				$("#showSalesOrderOptions").dialog("destroy").remove();
				var setStatus=0;
				console.log("test :: "+e);
				//var cuSoid =  $('#Cuso_ID').text();
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
				/* if(setStatus==-1){
					
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
					
				} */
				/* $.ajax({
					url: "./salesOrderController/setSalesOrderStatus",
					type: "POST",
					data :{cusoID:cuSoid,status:setStatus},
					success: function(data) {
						$('#showSalesOrderOptions').dialog('destroy').remove();
						$('#transactionStatus').val(setStatus);
						$('#soStatusButton').val(e);
						
					}
				}); */
				
			}

			
			function vendorInvoiceStatusForPO(){
				try{
					var dataid = $('#transactionStatus').val();
				    var newDialogDiv = jQuery(document.createElement('div'));
				    jQuery(newDialogDiv).attr("id", "showSalesOrderOptions");
				    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetWithPOStatus(this.value)" value="Void"></span><br><br>'+
				    		/*'<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="HOLDACCPAY" onclick="onSetVIStatus(this.value)" value="HOLD(Accounting/Payment)"></span><br><br>'+*/
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
					  /* if(dataid=='2'){
						  console.log('dataid inside'+dataid);
						   	$('#Close').css("font-weight","bold");
					    	$('#Close').css("background","#0E2E55");
					    }else  if(dataid=='0'){
						   	$('#Hold').css("font-weight","bold");
					    	$('#Hold').css("background","#0E2E55");
					    }else  if(dataid=='1'){
						   	$('#Open').css("font-weight","bold");
					    	$('#Open').css("background","#0E2E55");
					    }else  if(dataid=='-1'){
						   	$('#Void').css("font-weight","bold");
					    	$('#Void').css("background","#0E2E55");
					    }else  if(dataid=='-2'){
						   	$('#Quote').css("font-weight","bold");
					    	$('#Quote').css("background","#0E2E55");
					    } */
					
				}catch(err){
					
					}
				
				
			}

			function onSetWithPOStatus(e){
				
				$("#showSalesOrderOptions").dialog("destroy").remove();
				var setStatus=0;
				console.log("test :: "+e);
				//var cuSoid =  $('#Cuso_ID').text();
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
				/* if(setStatus==-1){
					
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
					
				} */
				/* $.ajax({
					url: "./salesOrderController/setSalesOrderStatus",
					type: "POST",
					data :{cusoID:cuSoid,status:setStatus},
					success: function(data) {
						$('#showSalesOrderOptions').dialog('destroy').remove();
						$('#transactionStatus').val(setStatus);
						$('#soStatusButton').val(e);
						
					}
				}); */
				
			}
			function loadlineItemGrid()
			{
				var gridstatus=true;
				
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
				 $('#lineItemGrid').jqGrid({
						datatype: 'JSON',
						mtype: 'POST',
						pager: jQuery('#lineItemPager'),
						url:query,
						postData: {'vepoID':function(){
							if(vepoID==null || vepoID==''){
								vepoID=0;
							}
								return vepoID;
							
						}},
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
					{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
					],
						rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
						sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
						shrinkToFit: true,
						width: 870,height:100, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
							/*$('#freightGeneralId').val(formatCurrencynodollar(data.Vepo.freight));
							$('#taxGeneralId').val(formatCurrencynodollar(data.Vepo.taxTotal));*/
							var freight = $('#freightGeneralId').val().replace(/[^0-9-.]/g, '');
							var tax = $('#taxGeneralId').val().replace(/[^0-9-.]/g, '');
							var subTotal = $('#subtotalGeneralId').val().replace("$", "");
							console.log("SubTot :: "+subTotal);
							var aTotal = parseFloat(sum) + parseFloat(freight) +parseFloat(tax);
							$('#totalGeneralId').val(formatCurrencynodollar(aTotal));
							$('#balDuePO').val(formatCurrencynodollar(aTotal));							
							
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
							 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
							 //console.log(e);
							},
						editurl:"./veInvoiceBillController/manpulateVendorInvoiceLineItem"
				 });
				 var selrowid = $("#invoicesGrid").jqGrid('getGridParam','selrow');
				var releasedetail= $("#invoicesGrid").jqGrid('getCell', selrowid, 'joreleasedetailid');
				if(releasedetail<=0)
				 $('#lineItemGrid').jqGrid('navGrid','#lineItemPager', {add:gridstatus, edit:gridstatus,del:gridstatus,refresh:gridstatus,search:false},
							//-----------------------edit options----------------------//
							{
					 	width:515, left:400, top: 300, zIndex:1040,
						closeAfterEdit:true, reloadAfterSubmit:true,
						modal:true, jqModel:true,
						editCaption: "Edit Product",
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
							$(function() { var cache = {}; var lastXhr=''; $("input#note").autocomplete({minLength: 2,timeout :1000,
								source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
									lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
								select: function( event, ui ){
									var ID = ui.item.id; var product = ui.item.label; $("#rxMasterIDPO").val(ID);
									/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
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
							//freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
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
//							return {'cuSoid':cusoid,'taxRate' : taxRate,'freight':freight, 'operForAck' : ''  };
						},
						afterSubmit:function(response,postData){
							$("#note").autocomplete("destroy"); 
							$(".ui-menu-item").hide();
							$("a.ui-jqdialog-titlebar-close").show();
							var row = JSON.parse(response.responseText);
							console.log("Vendor Invoice Rows---->"+row);
							console.log("veBillId --->"+row.veBillId);
							//$('#veBillIdPO').val(row.veBillId);
							//PreloadData();
							$("#lineItemGrid").GridUnload();
							 return [true, loadlineItemGrid()];
						}
					

							},
							//-----------------------add options----------------------//
							{
								width:550, left:400, top: 300, zIndex:1040,
								closeAfterAdd:true,	reloadAfterSubmit:true,
								modal:true, jqModel:false,
								addCaption: "Add Product",
								onInitializeForm: function(form){
									
								},
								afterShowForm: function($form) {
									 $('#note').attr('placeholder','Minimum 2 character required to get Name List');
									$(function() { var cache = {}; var lastXhr=''; $("input#note").autocomplete({minLength: 2,timeout :1000,
										source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
											lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
										select: function( event, ui ){
											var ID = ui.item.id; var product = ui.item.label; $("#rxMasterIDPO").val(ID);
											/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
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
									//freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
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
									
									
									//PreloadData();
									$("#lineItemGrid").GridUnload();
									return [true, loadlineItemGrid()];
								}
							},
							//-----------------------Delete options----------------------//
							{	
								closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
								caption: "Delete Product",
								msg: 'Delete the Product Record?',

								onclickSubmit: function(params){	
									var veBillIdPO = $('#veBillIdPO').val();
									var id = jQuery("#lineItemGrid").jqGrid('getGridParam','selrow');
									var key = jQuery("#lineItemGrid").getCell(id, 11);//'veBillDetailId' : key,
									return {'veBillDetailId' : key,'oper' : 'delete','veBillIdPO' : veBillIdPO};
									
								},
								afterSubmit:function(response,postData){
									var row = JSON.parse(response.responseText);
									console.log("Vendor Invoice Rows---->"+row);
									console.log("veBillId --->"+row.veBillId);
									//$('#veBillIdPO').val(row.veBillId);
									//PreloadData();
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
				
			//	$('.addinv1').attr("disabled",true); 
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
							
			}	
			
			function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
				return cellValue;
			}
			
			function formatCurrencynodollar(strValue)
			{
				if(strValue === "" || strValue == null){
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
				return (((blnSign)?'':'-') + dblValue + '.' + strCents);
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
	        	
	        	/*console.log("PO Exist :: "+data.Vebill.ponumber);
	        	console.log("Address1 :: "+data.vendorAddress.address1);
	        	$('#ponumberPO').text(data.Vebill.ponumber);
	        	$('#vepoidPO').val(data.Vebill.vePoid);*/
	        
	        	
	        	FormatReceiveDate(data.Vebill.receiveDate);
	        	FormatDatedDate(data.Vebill.billDate);
	        	FormatDueDate(data.Vebill.dueDate);
	        	$('#shipviaPO').val(data.Vebill.veShipViaId);
	        	$('#vendorInvoicePO').val(data.Vebill.invoiceNumber);
	        	$('#apacctPO').val(data.Vebill.apaccountId).attr('selected', true);
	        	$('#ponumberPO').text(data.Vepo.ponumber);
				$('#jobName').text(data.Vepo.updateKey);
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
	        	//$('#postdatePO').val(data.Vebill.usePostDate);
	        	
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
				{('#viStatusButtonPO').val('Void');$('#viStatusButtonPOtxt').val('Void');}
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
			//	editTransactionDetails("addNewVendorInvoiceFromPODlg");
				 $(".invReason").show();
			jQuery("#addNewVendorInvoiceFromPODlg").dialog("open");
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
	        	FormatReceiveDateJob(data.Vebill.receiveDate);
	        	FormatDatedDateJob(data.Vebill.billDate);
	        	FormatDueDateJob(data.Vebill.dueDate);
	        	$('#shipvia').val(data.Vebill.veShipViaId);
	        	$('#vendorInvoice').val(data.Vebill.invoiceNumber);
	        	
	        
	        	$('#apacct').val(data.Vebill.apaccountId).attr('selected', true);
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
	        	//$('#postdatePO').val(data.Vebill.usePostDate);
	        	
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
				loadNewVendorInvoice();
				
				jQuery( "#invStatus" ).val("addNewVendorInvoiceDlg");
				//jQuery("#addNewVendorInvoiceDlg").dialog("open");
				 $(".invReason").show();
				return true;
					
							        
	        }
	        else
	        {
	        	console.log("Test");
	        	jQuery("#EnterPODlg").dialog("close");
	        	$('#InvalidPOMsg').text('Cannot find purchase order #'+po);
	        	//jQuery("#InvalidPODlg").dialog("open");
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
	/*open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	},*/
select: function (event, ui) {
	var aValue = ui.item.value;
	console.log("Auto Suggest veBillid is :: "+aValue);
	var aText = ui.item.label;
	var valuesArray = new Array();
	valuesArray = aText.split("||");
	console.log(valuesArray);
	var aVepoId = valuesArray[4].replace("]","");
	
	//var aQryStr = "aVePOId=" + aValue;
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

/***
*created by: Praveenkumar 
*Date: 2014-09-01
*Purpose : Calculating freight charges
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
});


/***
*created by: Leo 
*Date: 2014-09-26
*Purpose : Reason Dialog box
*/
function editInvoiceDetails(){					
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
	
	 jQuery( "#invreasondialog" ).dialog({
			autoOpen: false,
			width: 400,
			title:"Reason",
			modal: true,
			buttons:{
				ok:function(){
					if($("#invreasonttextid").val()==""){
						$("#inverrordivreason").empty();
						$("#inverrordivreason").append("Reason required");
					}else{
						$("#inverrordivreason").empty();
						
						if(jQuery("#invStatus").val()=="addNewVendorInvoiceFromPODlg")
							{
							
							addNewVendorInvoiceFromPODlgwithReason();
							}
						else
							{
						
							addNewVendorInvoiceDlgwithReason();
							}
					}
				}
			},
			close: function () {
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





