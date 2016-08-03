/**
 * Created by: Leo  Date:10/11/2014
 * 
 * Description: creditDebitMemos
 * 
 */

var _globalcreditdebitmemo;

$(function() { 
	

	$("#grandtotalID").val("0.00");
	showCustomerInvoiceList();

				$("#checkyesID").attr("disabled",true);
				
					var cache = {}; var lastXhr='';
					$( "#customerID" ).autocomplete({
						minLength: 2, timeout :1000,
						select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerId").val(id); },
						source: function( request, response ) { var term = request.term;
							if( term in cache ){ response( cache[ term ] ); return; }
							lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
						},
						error: function (result) {
						     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} 
						});
		
							
						$('input.alpha[$id=DescriptionID]').keyup(function() { 
							if (this.value.match(/[^a-zA-Z0-9 ]/g)) { 
							this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, ''); 
							} 
						});

						
						$('#notestextareaID').keyup(function() { 
							if (this.value.match(/[^a-zA-Z0-9 ]/g)) { 
							this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, ''); 
							} 
						});


						$("#taxterritoryID").change(function(){
							
							var amountField = $("#amountID").val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($("#amountID").val().replace(/[^0-9\.-]+/g,""));
							var frieghtField = $("#frieghtID" ).val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($("#frieghtID" ).val().replace(/[^0-9\.-]+/g,""));
							

							if($("#taxterritoryID").val()!="-1")
							{	
							$("#taxrateID").text($("#taxterritoryID option:selected").data("value"));	
							$("#tax_Rate").val($("#taxterritoryID option:selected").data("value"));
							$("#checkyesID").removeAttr("disabled");
							var taxfreight=$("#taxterritoryID option:selected").data("taxfreight");
							$("#taxfreight").val(taxfreight);
							if($("#checkyesID").is(":checked"))
							{
								var taxRate1=parseFloat($("#taxterritoryID option:selected").data("value"));
								var taxAmount1;
								if(taxfreight==1){
									taxAmount1 = (taxRate1/100)*(amountField+frieghtField);
								}else{
									taxAmount1 = (taxRate1/100)*(amountField);
								}
								
								$( "#taxAmountID" ).val(formatCurrency(taxAmount1));
								$("#grandtotalID").val(formatCurrency(taxAmount1+amountField+frieghtField));
							}
							}
							else
							{
							$("#taxrateID").text("0");	
							$("#tax_Rate").val("0");
							$("#checkyesID").attr("disabled",true);
							$( "#taxAmountID" ).val("0.00");
							}
						});
						
						

					

						$( "#frieghtID" ).bind( "keypress keyup blur", function() {
							
							$( "#taxAmountID" ).val("$0.00");
							var frieghtField = $(this).val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($(this).val().replace(/[^0-9\.-]+/g,""));
							var amountField = $("#amountID" ).val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($("#amountID" ).val().replace(/[^0-9\.-]+/g,""));
							var taxfreight=$("#taxfreight").val();
							if($("#checkyesID").is(":checked"))
							{
								var taxRate=parseFloat($("#taxterritoryID option:selected").data("value"));
								var taxAmount;
								if(taxfreight==1){
									taxAmount = (taxRate/100)*(amountField+frieghtField);
								}else{
									taxAmount =(taxRate/100)*(amountField);
								}
								console.log("Testing t"+taxAmount);
								
							if(frieghtField!=0)
							{
									if(taxAmount!="0.00" && !isNaN(taxAmount))
							    	{
							    	$("#grandtotalID").val(formatCurrency(taxAmount+amountField+frieghtField));
							    	$( "#taxAmountID" ).val(formatCurrency(taxAmount));
							    	}
							    	else
							    	{
							    	$("#grandtotalID").val(formatCurrency(amountField+frieghtField));
							    	}
							}
							else
							{
								    if(taxAmount!="0.00" && !isNaN(taxAmount))
								    {
								    	$("#grandtotalID").val(formatCurrency(taxAmount+amountField));
								    	$( "#taxAmountID" ).val(formatCurrency(taxAmount));
								    }
								    else
								    {
								    	$("#grandtotalID").val(formatCurrency(amountField));
								    }
							}
							}
							else
							{
								$( "#taxAmountID" ).val(formatCurrency(0));
								 
								if(frieghtField!=0)
								{
									$("#grandtotalID").val(formatCurrency(amountField+frieghtField));
								}
								else
								{
									$("#grandtotalID").val(formatCurrency(amountField));
								}
							}
						
							});
						
						
						$( "#amountID" ).bind( "keypress keyup blur", function() {
							
							$( "#taxAmountID" ).val("$0.00");
							var amountField = $(this).val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($(this).val().replace(/[^0-9\.-]+/g,""));
							var frieghtField = $("#frieghtID" ).val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($("#frieghtID" ).val().replace(/[^0-9\.-]+/g,""));
							var taxfreight=$("#taxfreight").val();
							if($("#checkyesID").is(":checked"))
							{
								var taxRate=parseFloat($("#taxterritoryID option:selected").data("value"));
								var taxAmount;
								if(taxfreight==1){
									taxAmount = (taxRate/100)*(amountField+frieghtField);
								}else{
									taxAmount =(taxRate/100)*(amountField);
								}
								console.log("Testing t"+taxAmount);
								
							if(amountField!=0)
							{
									if(taxAmount!="0.00" && !isNaN(taxAmount))
							    	{
							    	$("#grandtotalID").val(formatCurrency(taxAmount+amountField+frieghtField));
							    	$( "#taxAmountID" ).val(formatCurrency(taxAmount));
							    	}
							    	else
							    	{
							    	$("#grandtotalID").val(formatCurrency(amountField+frieghtField));
							    	}
							}
							else
							{
								    if(taxAmount!="0.00" && !isNaN(taxAmount))
								    {
								    	$("#grandtotalID").val(formatCurrency(taxAmount+frieghtField));
								    	$( "#taxAmountID" ).val(formatCurrency(taxAmount));
								    }
								    else
								    {
								    	$("#grandtotalID").val(formatCurrency(frieghtField));
								    }
							}
							}
							else
							{
								$( "#taxAmountID" ).val(formatCurrency(0));
								 
								if(amountField!=0)
								{
									$("#grandtotalID").val(formatCurrency(amountField+frieghtField));
								}
								else
								{
									$("#grandtotalID").val(formatCurrency(frieghtField));
								}
							}
						
							});
						
						

						$("input[type='radio']").click(function(){

							var amountField = $("#amountID").val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($("#amountID").val().replace(/[^0-9\.-]+/g,""));
							var frieghtField = $("#frieghtID" ).val().replace(/[^0-9\.-]+/g,"")==""?parseFloat("0.00"):parseFloat($("#frieghtID" ).val().replace(/[^0-9\.-]+/g,""));
							var taxfreight=$("#taxfreight").val();
							if($("#checkyesID").is(":checked"))
								{
									var taxRate1=parseFloat($("#taxterritoryID option:selected").data("value"));
									var taxAmount1 ;
									if(taxfreight==1){
										taxAmount1 = (taxRate1/100)*(amountField+frieghtField);
									}else{
										taxAmount1 = (taxRate1/100)*(amountField);
									}
									$( "#taxAmountID" ).val(formatCurrency(taxAmount1));
									$("#grandtotalID").val(formatCurrency(taxAmount1+amountField+frieghtField));
								}
							else
								{
								$( "#taxAmountID" ).val("$0.00");
								$("#grandtotalID").val(formatCurrency(amountField+frieghtField));
								}
							
							
						 })
						 
						
});


/*function saveMemoDetails()
{
	

	
	var creditdebitmemoformValues = $("#creditdebitmemoformID").serialize();

	$.ajax({
		 
 		url: "./creditdebitmemo/createCustomerInvoicefromMemo",
 		type: "POST",
 		data:creditdebitmemoformValues+"&Options=add",
 		success: function(data) {
 			$('#addcreditDebitMemosDlg').dialog("close");
 			
 			$("#creditDebitMemosGrid").jqGrid('GridUnload');
 			showCustomerInvoiceList();				
			$("#creditDebitMemosGrid").trigger("reloadGrid");
 			
 		}		 
 });	
	
}
*/
function saveMemoDetails()
{
	if($('#customerID').val() =="")
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("2")
	}
	else if($('#memotypeID').val() == '-1')
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("3")
	}
	else if($('#glAccountsID').val() == '-1' )
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("4")
	}
	else if($('#salesmanID').val() == '-1' )
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("5")
	}
	else if($('#divisonID').val() == '-1')
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("6")
	}
	else
	{
		var creditdebitmemoformValues = $("#creditdebitmemoformID").serialize();
		var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
		$.ajax({
			url: "./checkAccountingCyclePeriods",
			data:{"datetoCheck":$("#datepickerbox").val(),"UserStatus":checkpermission},
			type: "POST",
			success: function(data) { 
				
				if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
				{
					periodid=data.cofiscalperiod.coFiscalPeriodId;
					yearid = data.cofiscalperiod.coFiscalYearId;
					
					console.log("7")
				$.ajax({		 
			 		url: "./creditdebitmemo/createCustomerInvoicefromMemo",
			 		type: "POST",
			 		data:creditdebitmemoformValues+"&Options=add"+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
			 		success: function(data) 
				 		{
			 			createtpusage('Company-Customer-Credit/Debit Memo','Add New Memo','Info','Company-Customer-Credit/Debit Memo,Saving Memo,customerID:'+$("#customerID").val()+',glAccountsID:'+$("#glAccountsID").val());
				 			$('#addcreditDebitMemosDlg').dialog("close");
				 			
				 			$("#creditDebitMemosGrid").jqGrid('GridUnload');
				 			showCustomerInvoiceList();				
							$("#creditDebitMemosGrid").trigger("reloadGrid");
				 			
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
						showDeniedPopup();
					}
					}
		  	},
   			error:function(data){
   				console.log('error');
   				}
   			});
			
	
}
}

function editMemoDetails()
{
	var creditdebitmemoformValues = $("#creditdebitmemoformID").serialize();
	
	console.log(_globalcreditdebitmemo+"============================="+creditdebitmemoformValues);
	
	console.log("1")
	if($('#customerID').val() =="")
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("2")
	}
	else if($('#memotypeID').val() == '-1')
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("3")
	}
	else if($('#glAccountsID').val() == '-1' )
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("4")
	}
	else if($('#salesmanID').val() == '-1' )
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("5")
	}
	else if($('#divisonID').val() == '-1')
	{
		$("#creditdebiterrorstatus").html("Mandatory Fields are Required");
		setTimeout(function(){
			$('#creditdebiterrorstatus').html("");
			},3000);
		console.log("6")
	}
	else
	{
		if(creditdebitmemoformValues!=_globalcreditdebitmemo)
		{
		var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
		console.log("7")
				$.ajax({
			url: "./checkAccountingCyclePeriods",
			data:{"datetoCheck":$("#datepickerbox").val(),"UserStatus":checkpermission},
			type: "POST",
			success: function(data) { 
				
				if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
				{
					periodid=data.cofiscalperiod.coFiscalPeriodId;
					yearid = data.cofiscalperiod.coFiscalYearId;


					$.ajax({		 
				 		url: "./creditdebitmemo/createCustomerInvoicefromMemo",
				 		type: "POST",
				 		data:creditdebitmemoformValues+"&Options=edit"+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
				 		success: function(data) 
					 		{
					 			$('#addcreditDebitMemosDlg').dialog("close");
					 			
					 			$("#creditDebitMemosGrid").jqGrid('GridUnload');
					 			showCustomerInvoiceList();				
								$("#creditDebitMemosGrid").trigger("reloadGrid");
					 			
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
						showDeniedPopup();
					}
					}
		  	},
   			error:function(data){
   				console.log('error');
   				}
   			});
		}
		else
			{
			$('#addcreditDebitMemosDlg').dialog("close");
 			$("#creditDebitMemosGrid").jqGrid('GridUnload');
 			showCustomerInvoiceList();				
			$("#creditDebitMemosGrid").trigger("reloadGrid");
			}
}
}

jQuery(function() {
	jQuery("#addcreditDebitMemosDlg").dialog({
		autoOpen : false,
		height : 600,
		width : 720,
		title : "Add New Credit/Debit Memo",
		modal : true,
		open: function(event, ui) {
	        $("input").blur();
	    },
		close : function() {
			// $('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});

function createNewcreditdebitmemo()
{
	
	document.getElementById("creditdebitmemoformID").reset();
	
	$("#addcreditDebitMemosDlg").dialog("open");
	$("#datepickerbox").datepicker("setDate",new Date());
	$("#glAccountsID option[value=-1]").attr("selected", true);
	$("#taxterritoryID option[value=-1]").attr("selected", true);
	$("#salesmanID option[value=-1]").attr("selected", true);
	$("#divisonID option[value=-1]").attr("selected", true);
	$("#creditdebiterrorstatus").html("");
	$("#taxrateID").html("0.00");
	$("#tax_Rate").val("0.00");
	$("#checkyesID").attr("disabled",true);
	$("#savememo").show()
	$("#editmemo").css("display","none");
	
}

function showCustomerInvoiceList(){
	/*Controller:POController/cuInvoice_listgrid */
	$("#creditDebitMemosGrid").jqGrid({
		/*url:'./creditdebitmemo?searchData='+searchData+'&fromDate='+fromDate+'&toDate='+toDate,*/	
		url:'./creditdebitmemo',
		datatype: 'JSON',
		mtype: 'GET',
		pager: jQuery('#creditDebitMemosGridPager'),
		colNames:['cuInvoiceID','Memo #','Customer ','rxMasterID','Invoice Date','Amount','memoStatus'],
		colModel :[
           	{name:'cuInvoiceID', index:'cuInvoiceID', align:'left', width:60, editable:true,hidden:true},
        	{name:'reference', index:'reference', align:'', width:60,sorttype:'string',hidden:false},
        	{name:'customer', index:'customer', align:'left', width:300,sorttype:'string',hidden:false},	
        	{name:'rxMasterID', index:'rxMasterID', align:'left', width:100,hidden:true},    
        	{name:'receiptDate', index:'receiptDate',sorttype:'string', align:'left', width:70,hidden:false},
        	{name:'amount', index:'amount', align:'right',sorttype:'integer', width:70,hidden:false,formatter:formatCurrency },
        	{name:'memoStatus', index:'memoStatus', width:70,hidden:true }
          
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#creditDebitMemosGridPager',
		sortname: 'cuInvoiceID', sortorder: "desc",	imgpath: 'themes/basic/images',	
		caption: 'Credit/Debit Memo',
		//autowidth: true,
		height:400,	width: 900,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
	    },
	    gridComplete:function(){
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
			var acuInvoiceID = rowData['cuInvoiceId'];
			var rxMasterID = rowData['rxMasterID'];
			createtpusage('Company-Customer-Credit/Debit Memo','Grid View','Info','Company-Customer-Credit/Debit Memo,Viewing Memo,reference:'+rowData['reference']+',rxMasterID:'+rxMasterID);
			document.getElementById("creditdebitmemoformID").reset();   
			preLoadCreditDebitMemoDetails();
			$('#addcreditDebitMemosDlg').dialog("open");
			
			$("#editmemo").attr("disabled",true);
			
			
			
			
    	}
	}).navGrid('#creditDebitMemosGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
}

function preLoadCreditDebitMemoDetails()
{

 		var grid = $("#creditDebitMemosGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		
		var acuInvoiceID = grid.jqGrid('getCell', rowId, 'cuInvoiceID');
		var rxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterID');
		var memoStatus = grid.jqGrid('getCell', rowId, 'memoStatus');
		$("#creditdebiterrorstatus").html("");
		
	/*	var statusValue="";
		
		$.ajax({
			url: "./creditdebitmemo/statusChecking",
			type: "POST",
			data : "&cuInvoiceID="+acuInvoiceID,
			success: function(data) {
				statusValue=data;
			}
		});*/
		

		
		$.ajax({
		url: "./salesOrderController/getPreInvoiceData",
		type: "POST",
		data : "&cuInvoiceId="+acuInvoiceID+"&rxMasterID="+rxMasterID,
		success: function(data) {
			
			//console.log(parseInt(data.cuInvoice.invoiceNumber.substring(2)));
			
			document.getElementById("creditdebitmemoformID").reset(); 
			var subtotal=((data.cuInvoice.subtotal));
			var frieght=((data.cuInvoice.freight));
			var taxamt=((data.cuInvoice.taxAmount));
			var grandtotal=((data.cuInvoice.subtotal+data.cuInvoice.taxAmount+data.cuInvoice.freight));
			
			console.log(data.cuInvoice.description);
			$("#customerID").val(data.CustomerName);			
			if (typeof(data.cuInvoice) != "undefined" && data.cuInvoice != null){
					
			$("#memotypeID option[value=" + data.cuInvoice.iscredit + "]").attr("selected", true);		
			$("#datepickerbox").val(FormatDate(data.cuInvoice.invoiceDate));
			$('#DescriptionID').val(data.cuInvoice.note);
			$('#notestextareaID').val(data.cuInvoice.description);		
			$('#amountID').val(formatCurrency((subtotal<0)?subtotal*-1:subtotal));
			$('#frieghtID').val(formatCurrency((frieght<0)?frieght*-1:frieght));
			$('#taxAmountID').val(formatCurrency((taxamt<0)?taxamt*-1:taxamt));
			$('#grandtotalID').val(formatCurrency((grandtotal<0)?grandtotal*-1:grandtotal));
			
			$('#memoID').val((data.cuInvoice.invoiceNumber).substring(0,2)+""+(parseInt(data.cuInvoice.invoiceNumber.substring(2))-1));			
			$('#customerpoID').val(data.cuInvoice.customerPonumber); 			
			$("#glAccountsID option[value=" + data.Cuinvoicedetail.coAccountID + "]").attr("selected", true);
			$("#taxterritoryID option[value=" + data.cuInvoice.coTaxTerritoryId + "]").attr("selected", true);
			$("#salesmanID option[value=" + data.cuInvoice.cuAssignmentId0 + "]").attr("selected", true);
			$("#divisonID option[value=" + data.cuInvoice.coDivisionId + "]").attr("selected", true);
			$("#taxrateID").text(data.cuInvoice.taxRate);
			$("#tax_Rate").val(data.cuInvoice.taxRate);
			$("#invoiceID").val(data.cuInvoice.cuInvoiceId);
			$("#cuinvoiceInvoiceID").val(data.Cuinvoicedetail.cuInvoiceDetailId);
			$("#JobCustomerId").val(data.cuInvoice.rxCustomerId)
			$("#taxfreight").val(data.cuInvoice.taxfreight);
			console.log(data.cuInvoice.taxRate);
			
			
			if(data.Cuinvoicedetail.taxable == 1)
				{
				$( "#checkyesID" ).prop( "checked", true );
				$( "#checkyesID" ).removeAttr( "disabled");
				}
			else
				{
				$( "#checknoID" ).prop( "checked", true );
				}
			
				if(memoStatus == 1)
				{
					$("#editmemo").show()
					$("#savememo").css("display","none");
				}
				else
				{
					$("#editmemo").css("display","none");
					$("#savememo").css("display","none");
					
					$("#creditdebiterrorstatus").html("Memo RollBack You Can't Edit").css({"font-weight":"bold","font-size":"14px"});
					
				}
				
				_globalcreditdebitmemo = $("#creditdebitmemoformID").serialize();
				$("#editmemo").removeAttr("disabled");
				
			}
			
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
	return createdDate;
}
