var count = 1;
var dupl= null;
var addClicked = 1;
jQuery(document).ready(function() {
	$('#priceTR').hide();
	$('#search').css('visibility','hidden');
	$('#date').datepicker();
	console.log("called");
	$("#salesOrderTemplateGrid").jqGrid({
		datatype: 'json',
		mtype: 'GET',
		url:'./jobtabs5/getSOTemplate',		
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['cuSoid', 'Description'],
	   	colModel:[
			{name:'cuSoid',index:'cuSoid', width:100,editable:true, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'templateDescription',index:'templateDescription',align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}}
			
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Template',
		height:425,	width: 200,/*scrollOffset:0,*/
		rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
			/*$('#salesOrderTemplateGrid').setSelection(1,true);
			var rowData = jQuery("#salesOrderTemplateGrid").getRowData(1);    		
    		//loadaddSalesOrderTemplateDetails(rowData['cuSoid']);
    		$('#cuSoid').val(rowData['cuSoid']);
    		preloadsoTemplate(rowData['cuSoid']);
    		dupl = rowData['cuSoid'];*/
	    	
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
    	gridComplete: function () {
    	
    	},
    	onSelectRow: function(rowId){
    		var rowData = jQuery("#salesOrderTemplateGrid").getRowData(rowId);    		
    		//loadaddSalesOrderTemplateDetails(rowData['cuSoid']);
    		$('#cuSoid1').val(rowData['cuSoid']);
    		addClicked = 2;
    		count = 1;
    		dupl = rowData['cuSoid'];    	
    		$('#errorMsg').hide();    
    		$('#showhidePrice').val('Show Price');
			$('#priceTR').hide();
    		loadaddSalesOrderTemplateDetails();
    		preloadsoTemplate(rowData['cuSoid']);
    		
    	},
    	ondblClickRow: function(rowId) {
  
		}
	})
	
	
	/*.navGrid('#chartsOfTermsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	loadaddSalesOrderTemplateDetails();
	/*$("#addSalesOrderTemplateGrid").jqGrid('navButtonAdd', "#addSalesOrderTemplateGridPager", {
	    caption: "", title: "Find ", buttonicon: "ui-icon-search",
	    onClickButton: function () {
	    	var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
			console.log("Row Details--->"+id)
			var key = jQuery("#addSalesOrderTemplateGrid").getCell(id, 10);
			var code;
			if(key!=null && key!="")
			location.href="./inventoryDetails?token=view&inventoryId="+key + "&itemCode=" + code;
	    }
	});*/
	return true;

});
jQuery(function(){
	jQuery("#SoLineItemNote").dialog({
			autoOpen : false,
			modal : true,
			title:"InLine Note",
			height: 350,
			width: 600,
			buttons : {  },
			close:function(){
				areaLine.removeInstance('lineItemNoteID');
				return true;
			}	
	});
});
var newDialogDiv = jQuery(document.createElement('div'));
var prMasterId ="";
var inventoryOrderPoint = "";
var inventoryOrderQuantity = "";
var editUrl = "";


function loadaddSalesOrderTemplateDetails(){
	console.log("second");
	/*$("#jqGrid").empty();
	$("#jqGrid").append("<table id='addSalesOrderTemplateGrid'></table><div id='addSalesOrderTemplateGridPager'></div>");*/
	var rowData = "";
	var id = $('#cuSoid1').val();	
	if(id != null && id != '' && count === 1){
		console.log("Length---"+id.length);
		dupl = id;
		id = null;
		count = 2;
		$("#addSalesOrderTemplateGrid").trigger("reloadGrid");		
		loadaddSalesOrderTemplateDetails();
	}
	//var aWareHouseID = prwarid;	
	$("#addSalesOrderTemplateGrid").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#addSalesOrderTemplateGridPager'),
		//url:'./jobtabs5/jobReleaseLineItem',	
		url:'./jobtabs5/getSOTemplateDetails',
		postData: {'cuSoid':function () { if(dupl!=null){return dupl;}else{return 0;} }},
		colNames:['Product No','', 'Description','Qty', 'Price Each', 'Mult.', 'Tax', 'Amount','<img src="./../resources/images/delete.png" style="vertical-align: middle;"', 'VepoID', 'prMasterID' , 'vePodetailID','InlineNote'],
		colModel :[
			{name:'note',index:'note',align:'left',width:60,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				 dataInit: function (elem) {
					 
			            $(elem).autocomplete({
			                source: 'jobtabs3/productCodeWithNameList',
			                minLength: 1,
			                select: function (event, ui) { ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });
			                var id = ui.item.id;  
			                var product = ui.item.label; 
			                var aSelectedRowId = $("#addSalesOrderTemplateGrid").jqGrid('getGridParam', 'selrow');
			                $("#addSalesOrderTemplateGrid").jqGrid('setCell', aSelectedRowId,'prMasterId', id);

			    
			                $.ajax({
						        url: './getLineItemsForSOTemplate?prMasterId='+id,
						        type: 'POST',       
						        success: function (data) {
						        	$.each(data, function(key, valueMap) {										
										
						        		if("lineItems"==key)
										{				
											$.each(valueMap, function(index, value){						
												
													$("#description").val(value.description);
													$("#unitCost").val(value.lastCost);
													//alert("Unit Cast---->"+value.lastCost);
													$("#priceMultiplier").val(value.pomult);
													//alert("POMultiplexer--->"+value.pomult);
													
													if(value.isTaxable == 1)
													{
														$("#taxable").prop("checked",true);
													}
													else
														$("#taxable").prop("checked",false);
														
														
													
													 
												/* $('#shipToAddressID').val(value.name);
												$('#shipToAddressID1').val(value.address1);
												$('#shipToAddressID2').val(value.address2);
												$('#shipToCity').val(value.city);
												$('#shipToState').val(value.state);
												$('#shipToZipID').val(value.zip); */
											
											}); 
											
										}	
										

										
									});
																		
									
						        }
						    });
			                } }); $(elem).keypress(function (e) {
				                
			                    if (!e) e = window.event;
			                    if (e.keyCode == '13') {
			                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
			                        return false;
			                    }
			                });}	},editrules:{edithidden:false,required: true}},
			 {name:'noteImage', index:'noteImage', align:'right', width:10,hidden:false, editable:false, formatter:noteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			 {name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},			
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7}},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10}, formatter:customCurrencyFormatter, formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "$ "}, editrules:{number:true,required: true}},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:40,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:true, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:false,required: false}},
			//{name:'unitPrice',index:'unitPrice',width:50 , align:'right',formatter:customCurrencyFormatter,hidden:true},
			{name:'unitPrice', index:'unitPrice', align:'right', width:50,hidden:false, editable:true, formatter:customSOTotalFormatter, editoptions:{size:15, alignText:'right',readonly: 'readonly'},editrules:{edithidden:true}},
			{name:'salesOrderDeleteIDImage',index:'salesOrderDeleteIDImage', width:5,editable:false, hidden:false,  formatter:salesOrderDeleteImageFormatter,editrules:{required:false}, editoptions:{size:10}},
		   	
			{name:'cuSoid', index:'cuSoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'cuSodetailId', index:'cuSodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'inlineNote', index:'inlineNote', align:'right', width:10,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			
			],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 920, rownumbers:true, altRows: true,viewrecords: true, altclass:'myAltRowClass', caption: 'Line Item',
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
			$("#addSalesOrderTemplateGrid").setSelection(1,true);
		},
		gridComplete: function () {
			setSOTempLinegridTotal();
			
//			$(this).mouseover(function() {
//		        var valId = $(".ui-state-hover").attr("id");
//		        $(this).setSelection(valId, false);
//		    });
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow: function(id){
			
		   },
		ondblClickRow: function(id){
			var lastSel = '';
		     if(id && id!==lastSel){ 
		        jQuery(this).restoreRow(lastSel); 
		        lastSel=id; 
		     }
		     $("#addSalesOrderTemplateGrid_iledit").trigger("click");
		    // jQuery(this).editRow(id, true);
		    
		},
		alertzIndex:1050,
		cellsubmit: 'clientArray',
		editurl: 'clientArray',pager: jQuery('#addSalesOrderTemplateGridPager')
		//editurl:"./jobtabs5/manpulaterSOTemplateLineItem"
	}).navGrid('#addSalesOrderTemplateGridPager', {add:false, edit:false,del:false,refresh:false,search:false},
			//-----------------------edit options----------------------//
			{/*
	 	width:515, left:400, top: 300, zIndex:1040,
		closeAfterEdit:true, reloadAfterSubmit:true,
		modal:true, jqModel:true,
		editCaption: "Edit Product",
		beforeShowForm: function (form) 
		{
			$("a.ui-jqdialog-titlebar-close").hide();
			jQuery('#TblGrid_SOlineItemGrid #tr_itemCode .CaptionTD').empty();
			jQuery('#TblGrid_SOlineItemGrid #tr_itemCode .CaptionTD').append('Product No: ');
			jQuery('#TblGrid_SOlineItemGrid #tr_itemCode .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
			jQuery('#TblGrid_SOlineItemGrid #tr_description .CaptionTD').empty();
			jQuery('#TblGrid_SOlineItemGrid #tr_description .CaptionTD').append('Description: ');
			jQuery('#TblGrid_SOlineItemGrid #tr_quantityOrdered .CaptionTD').empty();
			jQuery('#TblGrid_SOlineItemGrid #tr_quantityOrdered .CaptionTD').append('Qty: ');
			jQuery('#TblGrid_SOlineItemGrid #tr_quantityOrdered .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
			jQuery('#TblGrid_SOlineItemGrid #tr_unitCost .CaptionTD').empty();
			jQuery('#TblGrid_SOlineItemGrid #tr_unitCost .CaptionTD').append('Cost Each.: ');
			jQuery('#TblGrid_SOlineItemGrid #tr_priceMultiplier .CaptionTD').empty();
			jQuery('#TblGrid_SOlineItemGrid #tr_priceMultiplier .CaptionTD').append('Mult.: ');
			jQuery('#TblGrid_SOlineItemGrid #tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
			jQuery('#TblGrid_SOlineItemGrid #tr_taxable .CaptionTD').empty();
			jQuery('#TblGrid_SOlineItemGrid #tr_taxable .CaptionTD').append('Tax: ');
			var unitPrice = $('#unitCost').val();
			unitPrice=  parseFloat(unitPrice.replace(/[^0-9-.]/g, ''));
			$('#unitCost').val(unitPrice);
			var priceMultiplier = $('#priceMultiplier').val();
			priceMultiplier=  parseFloat(priceMultiplier.replace(/[^0-9-.]/g, ''));
			$('#priceMultiplier').val(priceMultiplier);
			 
		},
		beforeInitData:function(formid) {
			if($('#templateId').val()=== null || $('#templateId').val()==='')
			{
				$('#errorMsg').show();
				$('#errorMsg').html('<span style="color:red;">*Template Description is mantatory</span>');
				return false; 
			}			
			else
				{
				$('#errorMsg').hide();
				}
		},
		beforeSubmit:function(postdata, formid) {
			$("#note").autocomplete("destroy"); 
			$(".ui-menu-item").hide();
			var aPrMasterID = $('#prMasterId').val();
			if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
			return [true, ""];
		},
		onclickSubmit: function(params){			
			var prMasterId = $('#prMasterId').val();
			var freight = $('#customerInvoice_frightID').val();
			var taxRate = $('#customerInvoice_taxId').val();
			console.log("Before taxRate--->"+taxRate+":::freight--"+freight);
			if($('#cuSoid1').val() != null)
				cusoID = $('#cuSoid1').val()
			else
				cusoID = null;	
			if(freight != null){}						
			else{
				freight = 0.00;
			}
			if(taxRate != null && taxRate.length > 0){}						
			else{
				taxRate = 0.00;
			}
			console.log("After taxRate--->"+taxRate+":::freight--"+freight+":::cusoID==="+cusoID);
			var templateID = $('#templateId').val();
			var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
			console.log("Row Details--->"+id)
			var key = jQuery("#addSalesOrderTemplateGrid").getCell(id, 12);
			return { 'cuSodetailId' : key,'prMasterId' : prMasterId, 'taxRate' : taxRate,'freight' : freight, 'operForAck' : 'edit','cusoID' : cusoID,'templateId' : templateID };
		},
		afterSubmit:function(response,postData){
			$("#note").autocomplete("destroy"); 
			$(".ui-menu-item").hide();
			$("a.ui-jqdialog-titlebar-close").show();
			var row = response.responseText;
			$('#cuSoid1').val(row);
			console.log("Row---->"+$('#cuSoid1').val());
			preloadsoTemplate($('#cuSoid1').val());
			//$("#salesOrderTemplateGrid").trigger("reloadGrid");
			count = 1;
			 return [true, loadaddSalesOrderTemplateDetails()];
		}
	

			*/},
			//-----------------------add options----------------------//
			{/*
				width:550, left:400, top: 300, zIndex:1040,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add Product",
				onInitializeForm: function(form){
					
				},
				beforeInitData:function(formid) {
					if($('#templateId').val()=== null || $('#templateId').val()==='')
					{
						$('#errorMsg').show();
						$('#errorMsg').html('<span style="color:red;">*Template Description is mantatory</span>');
						return false; 
					}					
					else
						{
						$('#errorMsg').hide();
						}
					
				},
				afterShowForm: function($form) {

					$(function() { var cache = {}; var lastXhr=''; $("input#note").autocomplete({minLength: 1,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){
							var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} 
							$.ajax({
						        url: './getLineItemsSO?prMasterId='+$("#prMasterId").val(),
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
					var aPrMasterID = $('#prMasterId').val();
					if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
					return [true, ""];
				},
				onclickSubmit: function(params){					
					var prMasterId = $('#prMasterId').val();
					var freight = $('#customerInvoice_frightID').val();
					var taxRate = $('#customerInvoice_taxId').val();
					console.log("Before taxRate--->"+taxRate+":::freight--"+freight);
					if($('#cuSoid1').val() != null)
						cusoID = $('#cuSoid1').val()
					else
						cusoID = null;	
					if(freight != null){}						
					else{
						freight = 0.00;
					}
					if(taxRate != null && taxRate.length > 0){}						
					else{
						taxRate = 0.00;
					}
					console.log("After taxRate--->"+taxRate+":::freight--"+freight);
					var templateID = $('#templateId').val();
					var cusoid = $('#Cuso_ID').text();
					var taxRate =$('#customerInvoice_taxId').val();
					var freight = $('#customerInvoice_frightID').val();
					freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
					return {'cuSoid':cusoid,'taxRate' : taxRate,'freight':freight, 'operForAck' : ''  };
					return { 'prMasterId' : prMasterId, 'taxRate' : taxRate,'freight' : freight, 'operForAck' : 'add','cusoID' : cusoID,'templateId' : templateID };
				},
				afterSubmit:function(response,postData){
					$("#note").autocomplete("destroy");
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					var row = response.responseText;
					console.log("Rows---->"+row);
					console.log("cuSoid--->"+row['cuSoid']);
					$.each(row,function(key,value){
						if('cuSoid' === key)
							console.log("TTTTTTTTTTTTTTTT----------->"+value);
					});
					console.log("WWWWWWWWWWWWWWW----------->"+response.responseText.prTransferId);
					var obj = $.parseJSON(response.responseText);
					console.log("TTTTTTTTTTTTTTTT----------->"+obj['cuSoid']);
					$('#cuSoid1').val(row);
					console.log("Row---->"+$('#cuSoid1').val());
					preloadsoTemplate($('#cuSoid1').val());
					//$("#salesOrderTemplateGrid").trigger("reloadGrid");
					count = 1;
					return [true, loadaddSalesOrderTemplateDetails()];
				}
			*/},
			//-----------------------Delete options----------------------//
			{/*	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
				caption: "Delete Product",
				msg: 'Delete the Product Record?',
				beforeInitData:function(formid) {
					if($('#templateId').val()=== null || $('#templateId').val()==='')
					{
						$('#errorMsg').show();
						$('#errorMsg').html('<span style="color:red;">*Template Description is mantatory</span>');
						return false; 
					}			
					else
						{
						$('#errorMsg').hide();
						}
				},
				onclickSubmit: function(params){
					var freight = $('#customerInvoice_frightID').val();
					var taxRate = $('#customerInvoice_taxId').val();
					freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
					if($('#cuSoid1').val() != null)
						cusoID = $('#cuSoid1').val()
					else
						cusoID = null;	
					if(freight != null){}						
					else{
						freight = 0.00;
					}
					if(taxRate != null && taxRate.length > 0){}						
					else{
						taxRate = 0.00;
					}
					var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
					console.log("Row Details--->"+id)
					var key = jQuery("#addSalesOrderTemplateGrid").getCell(id, 12);
					console.log("Id--->"+key);
					return { 'cuSodetailId' : key, 'operForAck' : 'del','taxRate':taxRate,'freight':freight,'cusoID':cusoID};
					
				},
				afterSubmit:function(response,postData){
					var row = response.responseText;
					$('#cuSoid1').val(row);
					console.log("Row---->"+$('#cuSoid1').val());
					preloadsoTemplate($('#cuSoid1').val());
					//$("#salesOrderTemplateGrid").trigger("reloadGrid");
					count = 1;
					 return [true, loadaddSalesOrderTemplateDetails()];
				}
			*/}
		);
	
	
	
	 $("#addSalesOrderTemplateGrid").jqGrid(
				'inlineNav',
				'#addSalesOrderTemplateGridPager',
				{
					edit : true,
					add : true,
					zIndex : 1234,
					refresh : false,
					cloneToTop : true,
					alertzIndex : 1234,
					addParams : {
						position: "last",
						addRowParams : {
							keys : true,
							oneditfunc : function(id) {
								var ucost=$("#"+id+"_unitCost").val();
								ucost=ucost.replace(/[^0-9\.-]+/g,"");
								$("#"+id+"_unitCost").val(ucost);
							},
							successfunc : function(response) {
								return true;
							},
							aftersavefunc : function(response) {
								var inlineText= $('#SoLineItemNoteForm').find('.nicEdit-main').html();
								 var  inId= $("#addSalesOrderTemplateGrid").jqGrid('getGridParam', 'selrow');
								   	row=jQuery("#addSalesOrderTemplateGrid").getRowData(inId);
									 var notes = row['inlineNote'];
									 console.log("After Save -"+notes);
									   if(notes !== '' && notes !== null && notes != undefined){
										   $("#addSalesOrderTemplateGrid").jqGrid('setCell', inId,'noteImage', "<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>");							
									   }else{ 
										   $("#addSalesOrderTemplateGrid").jqGrid('setCell', inId,'noteImage', null); 
										   $('#addSalesOrderTemplateGrid').jqGrid('setCell', inId,'inlineNote' ,null);
										   
									   }
								
								var ids = $("#addSalesOrderTemplateGrid").jqGrid('getDataIDs');
								var veaccrrowid;
								if(ids.length==1){
									veaccrrowid = 0;
								}else{
									var idd = jQuery("#addSalesOrderTemplateGrid tr").length;
									for(var i=0;i<ids.length;i++){
										if(idd<ids[i]){
											idd=ids[i];
										}
									}
									 veaccrrowid= idd;
								}
								var aSelectedRowId = $("#addSalesOrderTemplateGrid").jqGrid('getGridParam', 'selrow');
								var changerowid;
								if(aSelectedRowId=="new_row"){
									console.log("IFselectedLineItemGrid"+aSelectedRowId); 
									$("#" + aSelectedRowId).attr("id", Number(veaccrrowid)+1);
									changerowid=Number(veaccrrowid)+1;
									
									$("#jqgh_addSalesOrderTemplateGridCheckbox_new_row").attr("id","jqgh_addSalesOrderTemplateGridCheckbox_"+changerowid);
								}else{
									 changerowid=aSelectedRowId;
								}
								var grid=$("#addSalesOrderTemplateGrid");
								grid.jqGrid('resetSelection');
							    var dataids = grid.getDataIDs();
							    for (var i=0, il=dataids.length; i < il; i++) {
							        grid.jqGrid('setSelection',dataids[i], false);
							    }
							   
							    setSOTempLinegridTotal();
							    
							   /* $("#joQuoteHeaderIDforimg_"+changerowid).removeAttr("tabindex");
							    $("#"+changerowid+"_linebreak").removeAttr("tabindex");
							    $("#"+changerowid+"_custombutton").removeAttr("tabindex");
							    validatequotedisabledbuttons();
							    $("#SaveQuoteButtonID").css({ opacity: 'initial'});
							    $("#SaveQuoteButtonID").attr("disabled", false);*/
								
								

							},
							errorfunc : function(rowid, response) {
								return false;
							},
							afterrestorefunc : function(rowid) {
							}
						}
					},
					editParams : {
						keys : true,
						successfunc : function(response) {
							return true;
						},
						aftersavefunc : function(response) {
							 var  inId= $("#addSalesOrderTemplateGrid").jqGrid('getGridParam', 'selrow');
							   	row=jQuery("#addSalesOrderTemplateGrid").getRowData(inId);
								 var notes = row['inlineNote'];
								   if(notes !== '' && notes !== null && notes != undefined){
									   $("#addSalesOrderTemplateGrid").jqGrid('setCell', inId,'noteImage', "<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>");		
								   }else {
									   $("#addSalesOrderTemplateGrid").jqGrid('setCell', inId,'noteImage', null); 
									   $('#addSalesOrderTemplateGrid').jqGrid('setCell', inId,'inlineNote' ,null);
								   }
							var ids = $("#addSalesOrderTemplateGrid").jqGrid('getDataIDs');
							var veaccrrowid;
							if(ids.length==1){
								veaccrrowid = 0;
							}else{
								var idd = jQuery("#addSalesOrderTemplateGrid tr").length;
								for(var i=0;i<ids.length;i++){
									if(idd<ids[i]){
										idd=ids[i];
									}
								}
								 veaccrrowid= idd;
							}
							var aSelectedRowId = $("#addSalesOrderTemplateGrid").jqGrid('getGridParam', 'selrow');
							var changerowid;
							if(aSelectedRowId=="new_row"){
								console.log("IFselectedLineItemGrid"+aSelectedRowId); 
								$("#" + aSelectedRowId).attr("id", Number(veaccrrowid)+1);
								changerowid=Number(veaccrrowid)+1;
								
								$("#jqgh_addSalesOrderTemplateGridCheckbox_new_row").attr("id","jqgh_addSalesOrderTemplateGridCheckbox_"+changerowid);
							}else{
								 changerowid=aSelectedRowId;
							}
							var grid=$("#addSalesOrderTemplateGrid");
							grid.jqGrid('resetSelection');
						    var dataids = grid.getDataIDs();
						    for (var i=0, il=dataids.length; i < il; i++) {
						        grid.jqGrid('setSelection',dataids[i], false);
						    }
						    setSOTempLinegridTotal();
						},
						errorfunc : function(rowid, response) {
							
						},
						afterrestorefunc : function( id ) {
						},
						oneditfunc : function(id) {
							var ucost=$("#"+id+"_unitCost").val();
							ucost=ucost.replace(/[^0-9\.-]+/g,"");
							$("#"+id+"_unitCost").val(ucost);
						}
					},restoreAfterSelect :false
				});
	//Drag And DROP
		jQuery("#addSalesOrderTemplateGrid").jqGrid('sortableRows');
		jQuery("#addSalesOrderTemplateGrid").jqGrid('gridDnD');
	/*.navButtonAdd('#addSalesOrderTemplateGridPager',{
			   caption:"", 
			   buttonicon:"ui-icon-search", 
			   onClickButton: function(){ 
				   var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
					console.log("Row Details--->"+id)
					var key = jQuery("#addSalesOrderTemplateGrid").getCell(id, 10);
					var code;
					location.href="./inventoryDetails?token=view&inventoryId="+key + "&itemCode=" + code;
			   }, 
			   position:"last"
			});*/
		var custombuttonthereornot=document.getElementById("inlinenotecustombutton");
    if(custombuttonthereornot == null){
			$('#addSalesOrderTemplateGrid').jqGrid('navButtonAdd',"#addSalesOrderTemplateGridPager",{ caption:"", id:"inlinenotecustombutton",buttonicon:"ui-icon-calculator", onClickButton:ShowNote, position: "last", title:"Edit note for line item", cursor: "pointer"});
	 }
	$("#showOrderPointsButtons").css("display","block");
	return true;
}


function SaveCloseOrderPoints(){
	$("#addSalesOrderTemplateGrid").trigger("reloadGrid");
}
function SaveOrderPoints(){
	$("#addSalesOrderTemplateGrid").trigger("reloadGrid");
}

function salesOrderDeleteImageFormatter(cellValue, options, rowObject){
	var position=rowObject['position'];
	var id="jqgh_addSalesOrderTemplateGridCheckbox_"+options.rowId;
	var element = '';
	element = "<input type='checkbox' id='"+id+"' onclick='setSOTempLinegridTotal();' value=false>";
   
return element;
}

function upAndDownImage(cellValue, options, rowObject){
	var element = "<div>";
	var upIconID = options.rowId;
	var downID = options.rowId;
	var downIconID = Number(downID)+1;
	if(options.rowId === '1'){
		element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px 13px;vertical-align: middle;position: relative;left: 9px;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		element += "<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='vertical-align: middle;'></a>";
		element += "</div>";
	}else {
		element +=	"<a id='"+upIconID+"_upIcon' onclick='upPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/upArrowLineItem.png' title='Move Up & Save'></a>";
		element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		element += 	"<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px;vertical-align: middle;'></a>";
		element += "</div>";
	}
	return element;
}




function customSOTotalFormatter(cellValue, options, rowObject) {
	var total =0;
//	try{
		console.log("test");
		var multiplier= rowObject['priceMultiplier'];
		var q= rowObject['quantityOrdered'];
		var unitcost= rowObject['unitCost'];
		if((unitcost+"").contains("$")){
			unitcost=unitcost.replace(/[^0-9\.-]+/g,"");
		}
		if(unitcost==undefined ||unitcost==""||unitcost==null){
			unitcost=0.00;
		}else{
			unitcost=Number(floorFigureoverall(unitcost,2));	
		}
		if(q==undefined ||q==""||q==null){
			q=0;
		}else{
			q=Number(floorFigureoverall(q,2));
		}
		if(multiplier==undefined ||multiplier==null||multiplier==0||multiplier==""){
			multiplier=1;
		}else{
			multiplier=Round_priceMultiplier(multiplier);
		}
		
		if(multiplier==0){
			multiplier=1;
		}
		console.log(multiplier+"=="+unitcost+"=="+q);
		total = (Number(multiplier)*Number(unitcost)*Number(q));
//	}catch(err){
//		console.log('error on loading grid'+err.message);
//	}
	return formatCurrency(total);
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


function addSOTemplate()
{
	if(addClicked != 1 && ($('#cuSoid1').val() !== null || $('#cuSoid1').val() !== '' || $('#cuSoid1').val() !== '0'))
	{
		var newDialogDiv = jQuery(document.createElement('div'));
	    jQuery(newDialogDiv).attr("id", "msgDlg");	
	    jQuery(newDialogDiv).html(
				'<span style="color:Green;">Template already exist.Do you want to create a new one.</span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 400,
			height : 150,
			title : "Information",
			buttons : [ {
				height : 35,
				text : "Yes",
				click : function() {				
					$(this).dialog("close");	
					//addNew();
					addNewSOTemplate();
				}
			},{
				height : 35,
				text : "No",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
	//$('#errorMsg').show();
	//$('#errorMsg').html('<span style="color:red;">*Please save</span>');
	return false;	
	}
	else if($('#templateId').val() == null || $('#templateId').val() == '')
		{
		$('#errorMsg').show();
		$('#errorMsg').html('<span style="color:red;">*Template Description is mantatory</span>');
		return false;		
		}
	else
		{
		$('#errorMsg').hide();
			var templateName = $('#templateId').val();
			var subTotal = $('#customerInvoice_subTotalIDLine').val().replace(/[^0-9\.]+/g,"");
			var freight = $('#customerInvoice_frightID').val().replace(/[^0-9\.]+/g,"");
			var taxRate = $('#customerInvoice_taxId').val().replace(/[^0-9\.]+/g,"");
			var taxValue = $('#customerInvoice_taxvalue').val().replace(/[^0-9\.]+/g,"");
			var total = $('#customerInvoice_totalIDLine').val().replace(/[^0-9\.]+/g,"");
			var cusoid = $('#cuSoid1').val();
			var info = "templateName="+templateName+"&subTotal="+subTotal+"&freight="+freight+"&taxRate="+taxRate+"&taxValue="+taxValue
			+"&total="+total+"&cusoid="+cusoid;
			console.log("info---->"+info);
			$.ajax({
				url: "./jobtabs5/saveSOTemplate",
				type: "POST",
				data : info,
				success: function(data) {
					$('#cuSoid1').val(data);	
					//$('#showMessageSOTemplate').html("Saved");
					//$('#ShowInfo').html("Saved");
					
					$("#salesOrderTemplateGrid").trigger("reloadGrid");
					addNewSOTemplate();
					/*setTimeout(function(){
						$('#showMessageSOTemplate').html("");						
						},3000);*/
				}
			});
		}
}
function addNew()
{

	$('#errorMsg').hide();
		var templateName = $('#templateId').val();
		var subTotal = $('#customerInvoice_subTotalIDLine').val().replace(/[^0-9\.]+/g,"");
		var freight = $('#customerInvoice_frightID').val().replace(/[^0-9\.]+/g,"");
		var taxRate = $('#customerInvoice_taxId').val().replace(/[^0-9\.]+/g,"");
		var taxValue = $('#customerInvoice_taxvalue').val().replace(/[^0-9\.]+/g,"");
		var total = $('#customerInvoice_totalIDLine').val().replace(/[^0-9\.]+/g,"");
		var cusoid = $('#cuSoid1').val();
		var info = "templateName="+templateName+"&subTotal="+subTotal+"&freight="+freight+"&taxRate="+taxRate+"&taxValue="+taxValue
		+"&total="+total+"&cusoid="+0;
		console.log("info---->"+info);
		$.ajax({
			url: "./jobtabs5/saveSOTemplate",
			type: "POST",
			data : info,
			success: function(data) {
				$('#cuSoid1').val(data);	
				//$('#showMessageSOTemplate').html("Saved");
				//$('#ShowInfo').html("Saved");
				
				$("#salesOrderTemplateGrid").trigger("reloadGrid");
				addNewSOTemplate();
				/*setTimeout(function(){
					$('#showMessageSOTemplate').html("");						
					},3000);*/
			}
		});
	
	}
function deleteSOTemplate()
{
	if($('#cuSoid1').val() == null || $('#cuSoid1').val() == '' || $('#cuSoid1').val() == '0')
		{
		$('#errorMsg').show();
		$('#errorMsg').html('<span style="color:red;">*Please save</span>');
		return false;	
		}
	else
		{
		var newDialogDiv2 = jQuery(document.createElement('div'));
		var errorText = "Are You Sure You want to Delete this Template ?";
		jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
		jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Info", 
			buttons : [ {
								height : 35,
								text : "Yes",
								click : function() {
									$(this).dialog("close");
									$('#errorMsg').hide();
									var id = "cuSOid="+$('#cuSoid1').val();
									//alert("delete id--->"+id);
									$.ajax({
										url: "./jobtabs5/deleteSOTemplate",
										type: "POST",
										data : id,
										success: function(data) {
											$('#cuSoid1').val(data);					
											$("#salesOrderTemplateGrid").trigger("reloadGrid");
											addNewSOTemplate();
											
										}
									});
									
									return true;
						}
					},{
						height : 35,
						text : "NO",
						click : function() {
							$(this).dialog("close");
							return false;
				}
			}]
						}).dialog("open");
		
		
		
		
		}

}

function preloadsoTemplate(cuSoid)
{
	if(cuSoid == null || cuSoid == '')
	{
	return false;	
	}
else
	{
	var id = "cuSOid="+cuSoid;
	//alert("id--->"+id);
	$.ajax({
		url: "./jobtabs5/loadSOTemplate",
		type: "POST",
		data : id,
		success: function(data) {
			$('#templateId').val(data.Cusotemplate.templateDescription);
			//$('#customerInvoice_subTotalIDLine').val(data.subTotal);
			//$('#customerInvoice_frightID').val(data.freight);
			//$('#customerInvoice_taxId').val(data.taxRate);
			//$('#customerInvoice_taxvalue').val(data.taxTotal);
			//$('#customerInvoice_totalIDLine').val(data.costTotal);
			$('#customerInvoice_taxId').val(formatSOTemplateCurrency(data.Cusotemplate.taxRate));
			if(data.Cusodetailtemplate !='undefined' && data.Cusodetailtemplate != null){
				$('#customerInvoice_subTotalIDLine').val(formatSOTemplateCurrency(data.Cusodetailtemplate.unitPrice));
				$('#customerInvoice_totalIDLine').val(formatSOTemplateCurrency(data.Cusodetailtemplate.unitPrice));
				formatTotal(data.Cusotemplate.freight,data.Cusodetailtemplate.unitPrice);
			}
			$('#customerInvoice_frightID').val(formatSOTemplateCurrency(data.Cusotemplate.freight));
			$('#customerInvoice_taxvalue').val(formatSOTemplateCurrency(data.Cusotemplate.taxTotal));
			formattax(data.Cusotemplate.freight);
			
		}
	});
	}
}

function formatTotal(frieght, Total){
	var frieghtval =frieght;
	var total  = Total;
	var Total = frieghtval + total;
	$('#customerInvoice_totalIDLine').val(formatSOTemplateCurrency(Total));
}

function saveSOTemplate()
{

	if($('#templateId').val() == null || $('#templateId').val() == '')
		{
		$('#errorMsg').show();
		$('#errorMsg').html('<span style="color:red;">*Template Description is mantatory</span>');
		return false;		
		}
/*	else if($('#cuSoid1').val() == null || $('#cuSoid1').val() == '' || $('#cuSoid1').val() == '0')
		{
		console.log("error");
		$('#errorMsg').show();
		$('#errorMsg').html('<span style="color:red;">*Please save</span>');
		return false;	
		}*/
	else
		{
		$('#errorMsg').hide();
		
			var templateName = $('#templateId').val();
			var subTotal = $('#customerInvoice_subTotalIDLine').val().replace(/[^0-9\.]+/g,"");
			var freight = $('#customerInvoice_frightID').val().replace(/[^0-9\.]+/g,"");
			var taxRate = $('#customerInvoice_taxId').val().replace(/[^0-9\.]+/g,"");
			var taxValue = $('#customerInvoice_taxvalue').val().replace(/[^0-9\.]+/g,"");
			var total = $('#customerInvoice_totalIDLine').val().replace(/[^0-9\.]+/g,"");
			var cusoid = $('#cuSoid1').val();
/*			var info = "templateName="+templateName+"&subTotal="+subTotal+"&freight="+freight+"&taxRate="+taxRate+"&taxValue="+taxValue
			+"&total="+total+"&cusoid="+cusoid+"&gridData="+gridData;*/
			
			  var rows = jQuery("#addSalesOrderTemplateGrid").getDataIDs();
			  var deletecuSODetailIDArray=new Array();
			  for(var a=0;a<rows.length;a++)
				 {
				    row=jQuery("#addSalesOrderTemplateGrid").getRowData(rows[a]);
				   var id="#jqgh_addSalesOrderTemplateGridCheckbox_"+rows[a];
				   var canDelete=$(id).is(':checked');
				   if(canDelete){
					  var cuSODetailID=row['cuSodetailId'];
					  if(cuSODetailID!=undefined && cuSODetailID!=null && cuSODetailID!="" && cuSODetailID!=0){
						  deletecuSODetailIDArray.push(cuSODetailID);
					 	}
					 $('#addSalesOrderTemplateGrid').jqGrid('delRowData',rows[a]);
				     }
				  }	
			var gridRows = $('#addSalesOrderTemplateGrid').getRowData();
			var gridData = JSON.stringify(gridRows);
			//console.log("info---->"+info);
			$.ajax({
				url: "./jobtabs5/saveSOTemplate",
				type: "POST",
				data : {			
					"templateName":templateName,
					"subTotal":subTotal,
					"freight":freight,
					"taxRate":taxRate,
					"taxValue":taxValue,
					"total":total,
					"cusoid":cusoid,
					"gridData":gridData,
					"DelSOLData":deletecuSODetailIDArray
				},
				success: function(data) {
					$('#cuSoid1').val(data);	
					$('#showMessageSOTemplate').html("Saved");
					$('#ShowInfo').html("Saved");
					createtpusage('Company-Customer-Sales Order Template','Save SO Template','Info','Company-Customer-Sales Order Template,Saving Template,SO ID:'+data);
					$("#salesOrderTemplateGrid").trigger("reloadGrid");
					$("#addSalesOrderTemplateGrid").trigger("reloadGrid");
					
					setTimeout(function(){
						$('#showMessageSOTemplate').html("");						
						},3000);
				}
			});
		}

	}
function formattax(frieght){
	var subTotal = $('#customerInvoice_subTotalIDLine').val(); 
	subTotal = parseFloat(subTotal.replace(/[^0-9-.]/g, ''));
	var tax = $('#customerInvoice_taxvalue').val();
	tax= parseFloat(tax.replace(/[^0-9-.]/g, ''));
	var total = subTotal+tax+frieght;
	$('#customerInvoice_totalIDLine').val(formatSOTemplateCurrency(total));
}
function addNewSOTemplate()
{
	$('#templateId').val("");
	$('#customerInvoice_subTotalIDLine').val("0.00");
	$('#customerInvoice_frightID').val("0.00");
	$('#customerInvoice_taxId').val("0.00");
	$('#customerInvoice_taxvalue').val("0.00");
	$('#customerInvoice_totalIDLine').val("0.00");
	$('#cuSoid1').val("0");
	count = 1;
	loadaddSalesOrderTemplateDetails();
	
}
function formatSOTemplateCurrency(strValue)
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

function showPrice()
{
	var cusoid = $('#cuSoid1').val();
	var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
	var key = jQuery("#addSalesOrderTemplateGrid").getCell(id, 10);
	var info = "prMasterId="+key+"&cusoid="+cusoid;
	$.ajax({
		url: "./jobtabs5/getPriceDetails",
		type: "POST",
		data : info,
		success: function(data) {
			console.log(data.product);
			console.log(data.margin);
			console.log(data.cost);
			console.log(data.percentage);	
			$('#whse').text(data.product);
			$('#cost').text(data.cost);
			$('#margin').text(data.margin);
			$('#perc').text(data.percentage);
			var value = $('#showhidePrice').val();
			if(value === 'Show Price')
				{
				$('#showhidePrice').val('Hide Price');
				$('#priceTR').show();
				}
			else
				{
				$('#showhidePrice').val('Show Price');
				$('#priceTR').hide();
				}
			
		}
	});
	
	
	}
function noteImage(cellValue, options, rowObject){
	var element = '';
   if(cellValue !== '' && cellValue !== null && cellValue != undefined){
	   element = "<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
   }
   return element;
} 


function ShowNote(){
	try{
		var rows = jQuery("#addSalesOrderTemplateGrid").getDataIDs();
		var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
		row=jQuery("#addSalesOrderTemplateGrid").getRowData(id);
		  var notes = row['inlineNote'];
		  var cuSodetailId = row['cuSodetailId'];
		  var lineITemNotes = notes.replace("&And", "'");
		   areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('lineItemNoteID');
			$(".nicEdit-main").empty();
			$(".nicEdit-main").append(lineITemNotes);
			jQuery("#SoLineItemNote").dialog("open");
			$(".nicEdit-main").focus();
			return true;
		}catch(err){
			alert(err.message);
		}
	}

	function SaveSoTempLineItemNote(){
	var inlineText= $('#SoLineItemNoteForm').find('.nicEdit-main').html();
	var id = jQuery("#addSalesOrderTemplateGrid").jqGrid('getGridParam','selrow');
	$('#addSalesOrderTemplateGrid').jqGrid('setCell', id,'inlineNote' ,inlineText);
	  var cuSoTempdetailId = row['cuSodetailId'];
	  
	  if(inlineText !== '' && inlineText !== null && inlineText != undefined){
		   $("#addSalesOrderTemplateGrid").jqGrid('setCell', id,'noteImage', "<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>");		
	   }else {
		   console.log("To Remove");
		   $("#addSalesOrderTemplateGrid").jqGrid('setCell', id,'noteImage', null); 
		   $('#addSalesOrderTemplateGrid').jqGrid('setCell', id,'inlineNote' ,null);
	   }
	  jQuery("#SoLineItemNote").dialog("close");
	  
	 /* var aLineItem = new Array();
	  aLineItem.push(inlineText);
	  aLineItem.push(cuSoTempdetailId);
	$.ajax({
		url: "./salesOrderController/saveSoTempLineItemNote",
		type: "POST",
		data : {'lineItem' : aLineItem},
		success: function(data) {
			jQuery("#SoLineItemNote").dialog("close");
			$("#addSalesOrderTemplateGrid").trigger("reloadGrid");
		}
		});*/
}
	
	function CopySoTempLineItemNote(){
		//var cuSoId = $('#cuSoid1').val();	
		//var gridRows = $('#addSalesOrderTemplateGrid').getRowData();
		var ids = $("#addSalesOrderTemplateGrid").jqGrid('getDataIDs');
		for(var i=0;i<ids.length;i++){
			
			$("#addSalesOrderTemplateGrid").jqGrid('setCell', i,'cuSodetailId', null);
			$("#addSalesOrderTemplateGrid").jqGrid('setCell', i,'cuSoid', null);
		}
		
		$('#cuSoid1').val('');
		$('#templateId').val('');
   
	/*$.ajax({
		url: "./salesOrderController/copySoTempLineItemNote",
		type: "POST",
		data : {			
			   "cuSoId" : cuSoId,
			   "gridData":gridData
			    },
		success: function(data) {
			$("#salesOrderTemplateGrid").trigger("reloadGrid");
			loadaddSalesOrderTemplateDetails();
			$('#cuSoid1').val(data);
			$('#templateId').val('');
			//jQuery("#SoLineItemNote").dialog("close");			
		}
		});*/
}
	
	
	function setSOTempLinegridTotal(){
		console.log("setSOTempLinegridTotal ")
		 var ids = $("#addSalesOrderTemplateGrid").jqGrid('getDataIDs'); 
		 var Subtotal=0;
		 var taxamount=0;
		 var taxcellValue;
		 var taxpercentage=$('#customerInvoice_taxId').val();
	
		 
		 if(taxpercentage==null||taxpercentage==""||taxpercentage==undefined){
			 taxpercentage=0;
		 }
			if(taxpercentage.length>0){
				taxpercentage=Number(floorFigureoverall(taxpercentage,2));
			}
			
		 for(var i=0;i<ids.length;i++){
			 var selectedRowId=ids[i];
			 var id="#jqgh_addSalesOrderTemplateGridCheckbox_"+selectedRowId;
			 var canDo=$(id).is(':checked');
			 cellValue =$("#addSalesOrderTemplateGrid").jqGrid ('getCell', selectedRowId, 'unitPrice');
			 if(cellValue!=undefined && cellValue!=null && cellValue!="" && cellValue!="" &&  !canDo){
				 var cellvalueamt=Number(parseFloat(cellValue.replace(/[^0-9\.-]+/g,"")).toFixed(3));
				 Subtotal=cellvalueamt+Subtotal;
				 Subtotal=Number(floorFigureoverall(Subtotal,2));
			 }
		 }
		 var freight=$("#customerInvoice_frightID").val();
		 freight=freight.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		 if(taxpercentage>0){
			 taxamount=taxpercentage*Subtotal/100;
		 }
		 
		 if(isNaN(Subtotal)){
			 Subtotal=0;
		 }
		 //floorFigureoverall(
		 $("#customerInvoice_subTotalIDLine").val(formatCurrency(Subtotal,2));
		
		 
		 if(taxamount<0)
			 taxamount=-taxamount;
		 $("#customerInvoice_taxvalue").val(formatCurrency(taxamount));
		 

		 var overalltotal=Number(Subtotal)+Number(floorFigureoverall(freight,2))+Number(floorFigureoverall(taxamount,2));
		 
		 $("#customerInvoice_totalIDLine").val(formatCurrency(overalltotal));
		 

	}
	

function SoCancelTempInLineNote(){
	areaLine.removeInstance('lineItemNoteID');
	jQuery("#SoLineItemNote").dialog("close");
	return false;
}