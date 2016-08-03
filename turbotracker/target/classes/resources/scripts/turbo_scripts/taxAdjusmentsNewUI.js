var _global_customerID;


$(function() { var cache = {}, lastXhr;
$( "#customertext" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) {
		var id = ui.item.id; $("#rxCustomerID").val(id); _global_customerID =id;
		},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/customerName", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });
$(function() { var cache = {}, lastXhr;
$( "#ogtaxterritoryoriginal" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#ogtaxterritoryid").val(id); 
	$("#ogTaxValue").html(ui.item.taxValue+"%"); 
	$('#taxsetting').val(ui.item.taxfreight);
	taxAmountCalc("og",ui.item.taxValue);
	},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });	

//taxterritorycorrected
$(function() { var cache = {}, lastXhr;
$( "#crtaxterritorycorrected" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#crtaxterritoryid").val(id);$("#crTaxValue").html(ui.item.taxValue+"%"); taxAmountCalc("cr",ui.item.taxValue);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });	

jQuery(function() {
	
	$("#ogbankAccountsID").attr("disabled",true);
	$("#crtaxterritorycorrected").attr("disabled",true);
	//$("#ogtaxterritoryoriginal").attr("disabled",true);
	
	jQuery("#cramounttext").on("keypress keyup blur",function(){
		
		if($("#crtaxcheck").is(":checked"))
		taxAmountCalc("cr",$("#crTaxValue").html().replace(/[^0-9\.-]+/g,""))
		grandTotalCalculation("cr");
		
	})
	
	jQuery("#crfrieghttext").on("keypress keyup blur",function(){
		if($("#crtaxcheck").is(":checked"))
			taxAmountCalc("cr",$("#crTaxValue").html().replace(/[^0-9\.-]+/g,""))
		grandTotalCalculation("cr");
	})
	
	jQuery("#ogamounttext").on("keypress keyup blur",function(){
		grandTotalCalculation("og");
	})
	
	jQuery("#ogfrieghttext").on("keypress keyup blur",function(){
		grandTotalCalculation("og");
	})
	
		
	
	$("#dateid").val(UsDateFormate(new Date()))
	jQuery("#TaxAdjustmentDlg").dialog({
		autoOpen : true,
		height : 300,
		width : 400,
		title : "Tax Adjustment",
		position: {
             my: "center",
             at: "center",
             of: $('#dgshowPos')
         },
		modal : true,
		close : function() {
			// $('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});


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




function cancel()
{
	jQuery("#TaxAdjustmentDlg").dialog("close");
	//jQuery("#taxAdjustmentUIDiv").css({"display":"inline"});
	return true;
}

function getCustomerInvoicefrominvNo()
{
	
	if($("#cuInvNoid").val()!="")
		{
		$.ajax({
			url: "./creditdebitmemo/getCuInvoicefminvoiceno",
			type: "GET",
			data : {invoiceNumber:$("#cuInvNoid").val()},
			success: function(data) {
				
					if(data.cuInvoiceId!=null)
					{
						console.log("==>"+data.taxAdjustmentStatus);
						if(data.transactionStatus == "3")
						//if(data.taxAdjustmentStatus != "1")
						{
						jQuery("#TaxAdjustmentDlg").dialog("close");
						jQuery("#taxAdjustmentUIDiv").css({"display":"inline"});
						
						$("#customertext").val(data.customerName);
						$("#rxCustomerID").val(data.rxCustomerId);
						$("#invoiceNoAdj").html(data.invoiceNumber);
						$("#cuInvoiceID").val(data.cuInvoiceId);
						$("#hiddenSalerep").val(data.cuAssignmentId0);
						$("#ogamounttext").val(formatCurrency(data.subtotal));
						$("#ogfrieghttext").val(formatCurrency(data.freight));
						$("#ogbankAccountsID").val(data.coAccountID);
						$("#ogtaxterritoryoriginal").val(data.cotaxdescription);
						$("#ogtaxcheck").attr("checked",true);
						
						$("#ogtax").val(formatCurrency(data.taxAmount));
						$("#oggrandtotaltext").val(formatCurrency(data.invoiceAmount));
						if(data.taxRate!=null)
						$("#ogTaxValue").html(data.taxRate+"%");
						$("#ogtaxterritoryid").val(data.coTaxTerritoryId);
						$("#ogDivisionID").val(data.coDivisionId);
						$("#taxsetting").val(data.taxfreight);
						$("#taxsum").val(data.taxSum);
						
						
						/*
						$("#cramounttext").val(formatCurrency(data.subtotal));
						$("#crfrieghttext").val(formatCurrency(data.freight));
						$("#crbankAccountsID").val(data.coAccountID);
						$("#crtaxterritorycorrected").val(data.cotaxdescription);
						$("#crtaxterritorycorrected").removeAttr("disabled");
						$("#crtaxcheck").attr("checked",true);
						$("#crtax").val(formatCurrency(data.taxAmount));
						$("#crgrandtotaltext").val(formatCurrency(data.invoiceAmount));
						if(data.taxRate!=null)
						$("#crTaxValue").html(data.taxRate+"%");
						$("#crtaxterritoryid").val(data.coTaxTerritoryId);
						$("#crDivisionID").val(data.coDivisionId);*/
						
						
						$("#customertext").attr("disabled",true);
						$("#invoiceNoAdj").attr("disabled",true);
						$("#ogamounttext").attr("disabled",true);
						$("#ogfrieghttext").attr("disabled",true);
						//$("#ogtaxterritoryoriginal").attr("disabled",true);
						$("#ogtaxcheck").attr("disabled",true);
						$("#ogtax").attr("disabled",true);
						$("#oggrandtotaltext").attr("disabled",true);
						$("#crtax").attr("disabled",true);
						$("#crgrandtotaltext").attr("disabled",true);
						}
						else
						{
							$("#invErrorStatus").html("Job Status must be Booked. Please change and do it again.");
							//$("#invErrorStatus").html("Already Tax Adjustment was done for this Invoice number");
							setTimeout(function(){
								$('#invErrorStatus').html("");	
								},2000);
						}
					}
					else
					{
						$("#invErrorStatus").html("Invalid Invoice Number");
						setTimeout(function(){
							$('#invErrorStatus').html("");	
							},2000);
					}
			}
		});
		}
	else
		{
		jQuery("#TaxAdjustmentDlg").dialog("close");
		jQuery("#taxAdjustmentUIDiv").css({"display":"inline"});
		}
	
}


function cancelTaxAdjustments()
{
	window.location.href = window.location.href;
}


function checktaxable(textValue,opt)
{
	if(opt=="cr")
		{
			if(!$("#crtaxcheck").is(":checked"))
			{
				jQuery("#crtax").val(formatCurrency("0"));
				$("#crtaxterritorycorrected").attr("disabled",true);
				grandTotalCalculation(opt);
			}
			else
			{
				$("#crtaxterritorycorrected").removeAttr("disabled");
				taxAmountCalc(opt,$("#crTaxValue").html().replace(/[^0-9\.-]+/g,""))
			}
		}
	else
		{
			if(!$("#ogtaxcheck").is(":checked"))
			{
				jQuery("#ogtax").val(formatCurrency("0"));
				//$("#ogtaxterritoryoriginal").attr("disabled",true);
				grandTotalCalculation(opt);
			}
			else
			{
				$("#ogtaxterritoryoriginal").removeAttr("disabled");
				taxAmountCalc(opt,$("#ogTaxValue").html().replace(/[^0-9\.-]+/g,""))
			}
		}
}

function taxAmountCalc(opt,tax)
{
	var taxRate = tax;
	/*var crsubamounttext = jQuery("#cramounttext").val().replace(/[^0-9\.-]+/g,"");
	var crfrieghttext =  jQuery("#crfrieghttext").val().replace(/[^0-9\.-]+/g,"");
	*/
	var ogsubamounttext = jQuery("#ogamounttext").val().replace(/[^0-9\.-]+/g,"");
	var ogfrieghttext =  jQuery("#ogfrieghttext").val().replace(/[^0-9\.-]+/g,"");
	
	console.log( 'ogfrieghttext : ' + ogfrieghttext +  '\t ogsubamounttext : ' +ogsubamounttext);
	console.log(taxRate);
	var taxsum=$("#taxsum").val();
	var taxfreight=$("#taxsetting").val();
		/*if(opt=="cr")
		{
			jQuery("#crtax").val("");
			jQuery("#crtax").val(formatCurrency((Number(crsubamounttext)+Number(crfrieghttext))*(Number(tax)/100)));
			grandTotalCalculation(opt);
		} else	{*/
			jQuery("#ogtax").val("");
			if(taxfreight=="1")
			{
			jQuery("#ogtax").val(formatCurrency((Number(taxsum)+Number(ogfrieghttext))*(Number(tax)/100)));	
			}
			else
			{
			jQuery("#ogtax").val(formatCurrency((Number(taxsum))*(Number(tax)/100)));	
			}
			
			grandTotalCalculation(opt);
//		}
}

function grandTotalCalculation(option)
{
	var ogamounttext = jQuery("#ogamounttext").val().replace(/[^0-9\.-]+/g,"");
	var ogfrieghttext = jQuery("#ogfrieghttext").val().replace(/[^0-9\.-]+/g,"");
	var ogtaxAmount = jQuery("#ogtax").val().replace(/[^0-9\.-]+/g,"");

	console.log('ogamounttext : ' + ogamounttext + 'ogfrieghttext : ' + ogfrieghttext +  'ogtaxAmount : ' +ogtaxAmount);
	if(option=="cr")
	{
	jQuery("#crgrandtotaltext").val("");
	jQuery("#crgrandtotaltext").val(formatCurrency(Number(cramounttext)+Number(crfrieghttext)+Number(crtaxAmount)));	
	}
	else
	{
	jQuery("#crgrandtotaltext").val("");
	jQuery("#oggrandtotaltext").val(formatCurrency(Number(ogamounttext)+Number(ogfrieghttext)+Number(ogtaxAmount)));	
	}
	
}

function saveTaxAdjustments()
{
	var crTaxable;
	var ogTaxable;
	
	if($("#crtaxcheck").is(":checked"))
		crTaxable = "yes";
	else
		crTaxable = "no";
	
	if($("#ogtaxcheck").is(":checked"))
		ogTaxable = "yes";
	else
		ogTaxable = "no";
		
	
	var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
	$.ajax({
		url: "./checkAccountingCyclePeriods",
		data:{"datetoCheck":$("#dateid").val(),"UserStatus":checkpermission},
		type: "POST",
		success: function(data) { 
			
			if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
			{
				periodid=data.cofiscalperiod.coFiscalPeriodId;
				yearid = data.cofiscalperiod.coFiscalYearId;
				
				/*$.ajax({
					url: "./creditdebitmemo/createCustomerInvoicefromMemo",
					type: "POST",
					data : {invoiceDate: $("#dateid").val(), 
						DescriptionName:$("#invoiceNoAdj").html(), 
						customerIDName: $("#rxCustomerID").val(), 
						notestextareaName: $("#notestext").val(), 
						amountName: $("#ogamounttext").val().replace(/[^0-9\.-]+/g,""), 
						frieghtName: $("#ogfrieghttext").val().replace(/[^0-9\.-]+/g,""), 
						taxAmountName: $("#ogtax").val().replace(/[^0-9\.-]+/g,""), 
						grandtotalName: $("#oggrandtotaltext").val().replace(/[^0-9\.-]+/g,""), 
						salesmanName: $("#hiddenSalerep").val(), 
						glAccountsName: $("#ogbankAccountsID option:selected").val(), 
						taxabe: ogTaxable, 
						taxterritoryName: $("#ogtaxterritoryid").val(), 
						coFiscalPeriodId: periodid, 
						coFiscalYearId: yearid, 
						divisonName: $("#ogDivisionID").val(), 
						memotypeName: "1",
						Options: "add", 
						tax_RateName:$("#ogTaxValue").html().replace(/[^0-9\.-]+/g,""),
						cuinvoiceInvoiceID:$("#cuInvoiceID").val()},
					success: function(data) {
							$.post("./creditdebitmemo/createCustomerInvoicefromMemo", { 
								invoiceDate: $("#dateid").val(), 
								DescriptionName:$("#invoiceNoAdj").html(), 
								customerIDName: $("#rxCustomerID").val(), 
								notestextareaName: $("#notestext").val(), 
								amountName: $("#cramounttext").val().replace(/[^0-9\.-]+/g,""), 
								frieghtName: $("#crfrieghttext").val().replace(/[^0-9\.-]+/g,""), 
								taxAmountName: $("#crtax").val().replace(/[^0-9\.-]+/g,""), 
								grandtotalName: $("#crgrandtotaltext").val().replace(/[^0-9\.-]+/g,""), 
								salesmanName: $("#hiddenSalerep").val(), 
								glAccountsName: $("#crbankAccountsID option:selected").val(), 
								taxabe: crTaxable, 
								taxterritoryName: $("#crtaxterritoryid").val(), 
								coFiscalPeriodId: periodid, 
								coFiscalYearId: yearid, 
								divisonName: $("#crDivisionID").val(), 
								memotypeName: "0", 
								Options: "add", 
								tax_RateName:$("#crTaxValue").html().replace(/[^0-9\.-]+/g,""),
								cuinvoiceInvoiceID:$("#cuInvoiceID").val()});
							
						$("#updatedstatus").html("Successfully Updated.");
						setTimeout(function(){
							$('#updatedstatus').html("");	
							},2000);
						createtpusage('Company-Customer-Tax Adjustments','Update Tax','Info','Company-Customer-Tax Adjustments,Updating Tax,DescriptionName:'+$("#invoiceNoAdj").html()+',invoiceDate:'+$("#dateid").val());
					},
					complete:function()
					{
						document.getElementById("txAdForm").reset();
						$('#crtaxcheck').removeAttr("checked");
						$('#ogtaxcheck').removeAttr("checked");
						$("#crTaxValue").html("");
						$("#ogTaxValue").html("");
						$("#invoiceNoAdj").html("");
						
					}
					});*/
				
				$.ajax({
					url: "./creditdebitmemo/updateTaxAdjustment",
					type: "POST",
					data : {invoiceDate: $("#dateid").val(), 
						DescriptionName:$("#invoiceNoAdj").html(), 
						customerIDName: $("#rxCustomerID").val(), 
						notestextareaName: $("#notestext").val(), 
						amountName: $("#ogamounttext").val().replace(/[^0-9\.-]+/g,""), 
						frieghtName: $("#ogfrieghttext").val().replace(/[^0-9\.-]+/g,""), 
						taxAmountName: $("#ogtax").val().replace(/[^0-9\.-]+/g,""), 
						grandtotalName: $("#oggrandtotaltext").val().replace(/[^0-9\.-]+/g,""), 
						salesmanName: $("#hiddenSalerep").val(), 
						glAccountsName: $("#ogbankAccountsID option:selected").val(), 
						taxabe: ogTaxable, 
						taxterritoryName: $("#ogtaxterritoryid").val(), 
						coFiscalPeriodId: periodid, 
						coFiscalYearId: yearid, 
						divisonName: $("#ogDivisionID").val(), 
						memotypeName: "1",
						Options: "add", 
						tax_RateName:$("#ogTaxValue").html().replace(/[^0-9\.-]+/g,""),
						cuinvoiceInvoiceID:$("#cuInvoiceID").val(),
						taxsetting:$("#taxsetting").val()
						
					},
					success: function(data) {
											
						$("#updatedstatus").html("Successfully Updated.");
						setTimeout(function(){
							$('#updatedstatus').html("");	
							},2000);
						//createtpusage('Company-Customer-Tax Adjustments','Update Tax','Info','Company-Customer-Tax Adjustments,Updating Tax,DescriptionName:'+$("#invoiceNoAdj").html()+',invoiceDate:'+$("#dateid").val());
					},
					complete:function()
					{
						document.getElementById("txAdForm").reset();
						$('#ogtaxcheck').removeAttr("checked");
						$("#ogTaxValue").html("");
						$("#invoiceNoAdj").html("");
						
					}
					});
					
			}else {
				if(data.AuthStatus == "granted")
				{	
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
										buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
									}).dialog("open");
				}else {
					showDeniedPopup();
				}
			}
	  	},
			error:function(data){
				console.log('error');
				}
			});
}
