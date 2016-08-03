var aAddandEdit;
	var aDetailId;
	var aListDetailId='';
	var cost = $("#cost").val().replace(/[^0-9\.]+/g,"");
	var price = $("#price").val().replace(/[^0-9\.]+/g,"");
	$("#cost").val(cost); $("#price").val(price);
	$(function() {var cache = {}; var lastXhr=''; $("#product").autocomplete({ minLength: 2,select: function( event, ui ) { 
		var quoteDetailID = ui.item.id; var rxMasterID = ui.item.manufactureID; $("#quoteDetailID").val(quoteDetailID); $.ajax({
			url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID, 'rxMasterID' : rxMasterID },
			success: function(data) {
				$.each(data, function(index,value){
					manufacture = value.inlineNote;
					var detail = value.paragraph;
					var quantity = value.itemQuantity;
					var manufactureID = value.rxManufacturerID;
					var factoryID = value.detailSequenceId;
					$("#paragraph").val(detail); $("#manufacturer").val(quantity); //$("#itemQuantity").val(quantity); 
					$("#rxManufacturerID").val(manufactureID);	$("#veFactoryId").val(factoryID);						
				});
			} }); },
	source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
		lastXhr = $.getJSON( "jobtabs2/productList", request, 
				function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
				error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
	
	$(function() { var cache = {}; var lastXhr='';
	$( "#manufacturer" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; var name = ui.item.value;  
	 	$("#rxManufacturerID").val(id);
	 	$.ajax({
			url: "./jobtabs2/getFactoryID",
			type: "GET",
			data : { 'rxMasterID' : id,'descripition' : name },
			success: function(data) {
				$("#veFactoryId").val(data);
			}
	 	});
	  },
		source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
			lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
				cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
				error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });

	function deleteQuoteFrom(){
		var grid = $("#addquotesList");
		var newDialogDiv = jQuery(document.createElement('div'));
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		if(rowId !== null){
			jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Line Item?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit" : function(){
					var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
					var joQuoteID = $("#addquotesList").jqGrid('getCell', rowId, 'joQuoteDetailID');
					$.ajax({
						url: "./jobtabs2/deleteQuickQuoteDetail",
						mType: "GET",
						data : { 'joHeaderQuoteDetailID' : joQuoteID },
						success: function(data){
							$("#addquotesList").trigger("reloadGrid");
							$.each(data, function(index, value){
							});
						}
					});
					//deleteQuote(joQuoteID);
					jQuery(this).dialog("close"); 
					$("#addquotesList").trigger("reloadGrid"); 
				},
				"Cancel" : function(){ 
					jQuery(this).dialog("close"); 
				}}}).dialog("open");
		}else{
			var errorText = "Please select a line item to delete";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true,  width:300, height:150, title:"Warning",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		return true;
	}
	jQuery(function () {
		jQuery( "#addButtonDiv" ).dialog({
					autoOpen: false,
					height: 450,
					width: 600,
					title:"Add/Edit Line Item",
					modal: true,
					buttons:{
					/*	"Import Factory Quote" : function(){ },
						"Build from Stock" : function(){ },
						"Custom Build" : function(){ },
						"Submit": function()
						{
							if(aAddandEdit === "add"){
								if(!$('#addButtonForm').validationEngine('validate')) {
									return false;
								}
							addQuoteFormDialog();
							jQuery(this).dialog("close");  return true;
							} else if(aAddandEdit === "edit"){
								if(!$('#addButtonForm').validationEngine('validate')) {
									return false;
								}
							addQuoteFormDialog();
							jQuery(this).dialog("close");  return true;
							} 
						},
						Cancel: function () { jQuery(this).dialog("close");  return true; } */
					},
					close: function () {
						$('#addButtonForm').validationEngine('hideAll');
						return true;
					}
				});
		 });
	 
	function pricePercentageCost(){
		if(!$('#addButtonForm').validationEngine('validate')) {
			return false;
		}
		var percentage = $("#percentage").val();
		var costNew = $("#cost").val().replace(/[^0-9\.]+/g,"");
		if(percentage === '0'){
			priceEmpty= costNew;
			$("#price").val(priceEmpty);
			return false;
		}else if(percentage >= 100){
			$("#maginAlert").fadeOut("slow");
			$("#maginAlert").fadeIn("slow", function() {
					$("#maginAlert").fadeOut(7000);
			});
			return false;
		}else { 
			if(percentage !== ''){ 
				price = costNew / [(100-percentage)/100];
				$("#price").val("");
				$("#price").val(Math.round(price));
			}
			return true;
		}
	}

	function costPercentage(){
		if(!$('#addButtonForm').validationEngine('validate')) {
			return false;
		}
		var percentage1 = $("#percentage").val();
		var cost = $("#cost").val().replace(/[^0-9\.]+/g,"");
		var price = $("#price").val().replace(/[^0-9\.]+/g,"");
		if((price !== '' && price !== '0') && (cost !== '' && cost !== '0') && (percentage !== '' && percentage !== '0')){
		percent =  [1- (cost/price)] * 100;	
//		var percentageCostPrice = Math.round(percent);
		$("#percentage").val("");
		$("#percentage").val(percentage1);
		return true;
		}
	}
	
	function openLineItemDialog(){
		var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
		var lineItemText = $("#addquotesList").jqGrid('getCell', rowId, 'inlineNote');
		$("#lineItem").val("");
		$("#lineItem").val(lineItemText);
		if(lineItemText === false){
			lineItemText = "";
		}
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<table><tr><td><textarea id="lineItem" rows="24" cols="54">'+lineItemText+'</textarea></td></tr></table>');
		jQuery(newDialogDiv).dialog({modal: true, width:500, height:500, title:"InLine Note", 
									buttons:{
										"Submit" : function(){ var lineitem = $("#lineItem").val();  LineItemInfo(lineitem); 
										 jQuery(this).dialog("close"); $("#addquotesList").trigger("reloadGrid"); },
										"Cancel" : function(){ jQuery(this).dialog("close"); }}}).dialog("open");
	}
	
	function openFooterLineDialog(){
		var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
		var footerLineText = $("#addquotesList").jqGrid('getCell', rowId, 'productNote');
		$("#footerLine").val("");
		$("#footerLine").val(footerLineText);
		if(footerLineText === false){
			footerLineText = "";
		}
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<table><tr><td><textarea id="footerLine" rows="5" cols="58">'+footerLineText+'</textarea></td></tr></table>');
		jQuery(newDialogDiv).dialog({modal: true, height: 210,width: 535, title:"Footnote", 
								buttons:{
									"Submit" : function(){ var footerNote = $("#footerLine").val();  footerNoteInfo(footerNote); 
									jQuery(this).dialog("close"); $("#addquotesList").trigger("reloadGrid"); },
									"Cancel" : function(){ jQuery(this).dialog("close"); }}}).dialog("open");
	}
	
	function saveQuoteNavigator(){
		var newDialogDiv = jQuery(document.createElement('div'));
		if(aAddandEdit === "add"){
			if(!$('#addButtonForm').validationEngine('validate')) {
				return false;
			}
			if($("#rxManufacturerID").val() === ''){
		 		jQuery(newDialogDiv).html('<span><b style="color:red;">This Vendor is not available. Please choose another Vendor (or) Add the Vendor Using Add Manufacture.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:320, height:160, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			 }
			addQuoteFormDialog();
			jQuery("#addButtonDiv").dialog("close");  
			return true;
		}else if(aAddandEdit === "edit"){
			if(!$('#addButtonForm').validationEngine('validate')) {
				return false;
			}
			if($("#rxManufacturerID").val() === ''){
		 		jQuery(newDialogDiv).html('<span><b style="color:red;">This Vendor is not available. Please choose another Vendor (or) Add the Vendor Using Add Manufacture.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:320, height:160, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			 }
			addQuoteFormDialog();
			jQuery("#addButtonDiv").dialog("close");  
			return true;
		} 
	}

	function cancelQuoteNavigator(){
		$('#addButtonForm').validationEngine('hideAll');
		jQuery("#addButtonDiv").dialog("close");  
		return true;
	} 
	
	 function addQuoteFormDialog(){
			var newDialogDiv = jQuery(document.createElement('div'));
			if($("#quoteTypeDetail").val() === ''){
				jQuery(newDialogDiv).html('<span><b style="color:red;">Please provide "Type" and "Submitted By"</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else if($("#jobQuoteSubmittedBYFullName").val() === ''){
				jQuery(newDialogDiv).html('<span><b style="color:red;">Please provide "Type" and "Submitted By"</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			} 
		 	var addFormValues = $("#addButtonForm").serialize();
			var joQuoteHeaderIdentity= jQuery("#joQuoteheader").text();
			if(aAddandEdit === "edit"){
				addFormValues = $("#addButtonForm").serialize();
			}
			var gridInfo = true;
			if(aGlobalVariable === "copy"){
				joQuoteHeaderIdentity = $("#joHeaderID").val();
			}else{
				joQuoteHeaderIdentity = saveQuoteDetailInfo(gridInfo);}
			$("#joQuoteHeaderID").val(" ");
			$("#joQuoteHeaderID").val(joQuoteHeaderIdentity);
			$("#addquotesList").trigger("reloadGrid");
			$('#addquotesList').jqGrid('setGridParam',{postData: {'joQuoteHeaderID': joQuoteHeaderIdentity}});
			jQuery("#joQuoteheader").empty();
			jQuery("#joQuoteheader").text(joQuoteHeaderIdentity);
			var joQuoteheaderID = joQuoteHeaderIdentity;
			$("#joHeaderID").val(" ");
			$("#joHeaderID").val(joQuoteheaderID);
			var name='';
			if(Number(joQuoteHeaderIdentity) === 0){
				name = $("#joHeaderID").val();
				$("#addquotesList").trigger("reloadGrid");
				$('#addquotesList').jqGrid('setGridParam',{postData:{'joQuoteHeaderID': name}});
				jQuery("#joQuoteheader").text(name);
				joQuoteheaderID = name;
				$("#joHeaderID").val(joQuoteheaderID);
			} 
			if(joQuoteHeaderIdentity > joQuoteheaderID){
				joQuoteheaderID = joQuoteHeaderIdentity;
			}
			var addQuoteValues = addFormValues + "&joQuoteHeaderID=" +joQuoteheaderID + 
						"&quoteheaderid=" + joQuoteHeaderIdentity + "&oper=" + aAddandEdit;
			$.ajax({
				url: "./jobtabs2/manpulaterProductQuotes",
				type: "POST",
				data : addQuoteValues,
				success: function(data) {
					$("#joQuoteheader").text(joQuoteHeaderIdentity);
					$("#addquotesList").jqGrid('GridUnload');
					loadQuotesListDetails();
					$('#addquotesList').jqGrid('setGridParam',{postData: {'joQuoteHeaderID': joQuoteHeaderIdentity}});
					$("#addquotesList").trigger("reloadGrid");
					if(aGlobalConstant === "edit"){
						aDetailId=data+",";
						aListDetailId+= aDetailId;
						$("#joDetailID").val(aListDetailId);
					}else{
						$("#joDetailID").val('');
					}
					if(Number(joQuoteHeaderIdentity) === 0){
						$("#addquotesList").jqGrid('GridUnload');
						loadQuotesListDetails();
						$('#addquotesList').jqGrid('setGridParam',{postData:{'joQuoteHeaderID': name}});
						$("#addquotesList").trigger("reloadGrid");
					}
				}
		   });
		return true; 
	}
		
	function deleteQuote(joQuoteID){
		$.ajax({
			url: "./jobtabs2/deleteQuickQuoteDetail",
			mType: "GET",
			data : { 'joHeaderQuoteDetailID' :joQuoteID },
			success: function(data){
				$("#addquotesList").trigger("reloadGrid");
				$.each(data, function(index, value){
				});
			}
		});
	}