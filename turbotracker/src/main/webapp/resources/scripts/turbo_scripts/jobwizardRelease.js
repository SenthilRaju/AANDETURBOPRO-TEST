/** document ready Function for Release Tab **/
//callvendorinvoicesave();
var deleteveBillDetailIDDetailId=new Array();

var salesOrderFlag='';
var shipViaInvoiceID;
var invoiceFreightAmount;
var aReleaseDialogVar;
var aPurchaseOrderVar;
var  aShipDialogVar;
var globalinsideoroutsidejob=false;
var errorText;
var vendorinvoice1rowid;
var estimatedamountRelease;
var newDialogDiv = jQuery(document.createElement('div'));
var aDate = new Date();
var currentMonth = aDate.getMonth()+1;
var currenDate = currentMonth+"/"+aDate.getDate()+"/"+aDate.getFullYear();
var whseID;
var allocated;
var iFlag = 1;
var custombutton=false;
var globalcheckvalidation=true;
var releasselectrowid=1;
var customerName='';
var _globalvarvenodorinvoiceform;
var _globalvarvenodorinvoicegrid;


var _globaloldcustomerInvoiceform;
var _globaloldcustomerInvoicegrid;
var _globaloldcustomerInvoiceformTotal;
var globalTaxTerritory=null;
var SplitCommissionID;
var _global_override_taxTerritory=false;
var _global_override_taxIDBasedOnCustomer;
var coTaxThereorNOt=false;
$(document).keydown(function (e) {
    var preventKeyPress;
    if (e.keyCode == 8) {
        var d = e.srcElement || e.target;
        switch (d.tagName.toUpperCase()) {
            case 'TEXTAREA':
                preventKeyPress = d.readOnly || d.disabled;
                break;
            case 'SELECT':
                preventKeyPress = d.readOnly || d.disabled;
                break;
            case 'INPUT':
                preventKeyPress = d.readOnly || d.disabled ||
                    (d.attributes["type"] && $.inArray(d.attributes["type"].value.toLowerCase(), ["radio", "checkbox", "submit", "button"]) >= 0);
                break;
            case 'DIV':
                preventKeyPress = d.readOnly || d.disabled || !(d.attributes["contentEditable"] && d.attributes["contentEditable"].value == "true");
                break;
            default:
                preventKeyPress = true;
                break;
        }
    }
    else
        preventKeyPress = false;

    if (preventKeyPress)
        e.preventDefault();
});


jQuery(document).ready(function(){
	$("#mailTimestampLines").hide();
	$("#mailTimestampGeneral").hide();
	$('#mandveinvno').hide();
	
	$('#commissionDialogShipDateVal').datepicker();
	$('#commissionDialogCommissionDateVal').datepicker();
	CKEDITOR.replace('InvoiceLineItemNoteID', ckEditorconfigforinline);
	
loadCustomerPONumber();
loadPONumbers();

	var allocatedz = $('#allocate').text();
	var estimatedz = $('#estimate').text();
	var unAllocated = $('#unAllocated').text();
	
	if(allocatedz==''){
		$('#allocate').text(formatCurrency(0));
	}
	if(estimatedz==''){
		$('#estimate').text(formatCurrency(0));
	}
	if(unAllocated==''){
		$('#unAllocated').text(formatCurrency(0));
	}
	
	 //vendor invoice grid
	$(".datepicker").datepicker();
	$(".vendorInvDatepicker").datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	setTimeout("loadReleaseGrid();", 1);
	/*Commented by Zenith for avoid duplicate rows loadShipingGrid();*/
	$("#OriJobNumber").css("display", "none");
	var aQuoteNumber = $("#quoteNumber").val();
	if(aQuoteNumber !== ''){
		$("#OriJobNumber").show();
	}
	$("#datedID").val(currenDate);
	$("#postDateID").css("display", "none");
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Booked"){
		$(".customerNameField").attr("disabled", true);
	}
	$("#loadingPODiv").css({"visibility": "hidden"});
	//loadVendorInvoice(vePOID);
	estimatedamountRelease =$("#contractAmount_release").val();
	if(typeof estimatedamountRelease != 'undefined' && estimatedamountRelease !== '' &&  estimatedamountRelease!== null){
		$("#estimate").empty();
		estimatedamountRelease=estimatedamountRelease.replace(/[^0-9\.-]+/g,"");
		if(estimatedamountRelease===null || estimatedamountRelease===''){
			estimatedamountRelease=0;
		}
		$("#estimate").text(formatCurrency(estimatedamountRelease));
	}
	 $("#releaseddate").datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	
	$( "#ReleasesID" ).datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	$( "#releasesTypeID" ).change(function() {
		if($(this).val()==2){
			$('#ReleaseTable tr:nth-child(7)').hide();
			$('#ManufacturerId').val(0);
			$('#ReleasesManuID').val('SalesOrder');
		}else if($(this).val()==3){
			$('#ReleaseTable tr:nth-child(7)').hide();
			$('#ManufacturerId').val(0);
			$('#ReleasesManuID').val('ServiceOrder');
		}else if($(this).val()==5){
			$('#ReleaseTable tr:nth-child(7)').hide();
			$('#ManufacturerId').val(0);
			$('#ReleasesManuID').val('ServiceOrder');
		}else{
			$('#ReleaseTable tr:nth-child(7)').show();
			$('#ManufacturerId').val('');
			$('#ReleasesManuID').val('');
		}
		});
	
	jQuery("#openSplitcommreleaseDia").dialog({
		autoOpen:false,
		width:460,
		title:"",
		modal:true,
		buttons:{
			"Save & Close":function(){
				var joMasterId=$('#joMasterHiddenID').val();
				var grid = $("#release");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var joReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
				
				var allRowsInGrid = $('#releaseCommissionSplitsGrid').jqGrid('getRowData');
				var aVal = new Array(); 
				var aTax = new Array();
				
				var sum = 0;
				$.each(allRowsInGrid, function(index, value) {
					aVal[index] = value.allocated;
					var number1 = aVal[index];
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					sum = Number(sum) + Number(number6); 
					if(number6=='100'){
						singleUserCommission=1;
					}
				});
			
				if(Number(sum)<100){
					var newDialogDiv2 = jQuery(document.createElement('div'));
					var errorText = "Allocated % is not Equal to 100%";
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
				else if(Number(sum)>100){
					var newDialogDiv2 = jQuery(document.createElement('div'));
					var errorText = "Allocated % is Greater than 100 Please Delete Excess Values";
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
				else{
					 var jorCommissionSplitGridRows = $('#releaseCommissionSplitsGrid').getRowData();
					 var jorSplitCommissionGridData = JSON.stringify(jorCommissionSplitGridRows);
					// alert("Commission Grids::"+gridDataToSend);
					
					 var rows = jQuery("#releaseCommissionSplitsGrid").getDataIDs();
					 deleteCommissionSplitJorID=new Array();
						 for(var a=0;a<rows.length;a++)
						 {
						    row=jQuery("#releaseCommissionSplitsGrid").getRowData(rows[a]);
						   var id="#canDeleteJorID_"+rows[a];
						   var canDeleteJor=$(id).is(':checked');
						   if(canDeleteJor){
							  var ecSplitJobId=row['ecSplitJobId'];
							  if(ecSplitJobId!=undefined && ecSplitJobId!=null && ecSplitJobId!="" && ecSplitJobId!=0){
								  deleteCommissionSplitJorID.push(ecSplitJobId);
							 	}
							 $('#releaseCommissionSplitsGrid').jqGrid('delRowData',rows[a]);
						  }
					}
					
					$.ajax({
			 			url: './jobtabs3/manipulateSplitCommission',
			 			type : 'POST',
			 			data: {'joMasterID':joMasterId,'joReleaseID':joReleaseId,'tabpage':'JoRelease','commissionSplitGridData':jorSplitCommissionGridData,'commissionSplitdelData':deleteCommissionSplitJorID},
			 			success: function (data){
			 				calculateSplit($("#joReleaseid").val());
			 				$("#release").trigger("reloadGrid");

			 				}
			        	});
					$("#jobreleasehiddenId").val("");
					$('#openSplitcommreleaseDia').dialog("close");
				}
				
				}
				},
		close:function(){
			$("#jobreleasehiddenId").val("");
			$('#opensplitcommisionreleaseForm').validationEngine('hideAll');
		return true;}	
	});
	
	
jQuery("#openPaidCommissionDialog").dialog({
		autoOpen:false,
		width:500,
		title:"",
		modal:true,
		buttons:{
			"Close":function(){
					$('#openPaidCommissionDialog').dialog("close");
				}
				}/*,
		close:function(){
			$(this).dialog("close");
		return true;}*/	
	});
	
	
	var releaseCommissionSplitsGridsum=0;
	var releaseselectedid=0;
	var releaseupdatecommsplitgrid=0;
		/*./commissionsplits_listgrid*/
	
	
		 console.log('Before loadEmailList');
		 loadEmailList($('#rxCustomer_ID').text());	
		 loadjobcustomerinvoicecategories();
		 
		/* $("#vendorinvoice1_ilsave").click(function() {
			 $("#info_dialog").css("z-index", "10000");
		 });*/
		 $("#customerInvoice_generaltaxId").val($("#TaxValue").text().replace("%",""));
		 var hiddennoticeId=$("#hiddennoticeId").val();
		 var hiddencontactId=$("#hiddencontactId").val();
		 var hiddennotice=$("#hiddennotice").val();
		 var hiddenContactNameId = $("#hiddenContactNameId").val();
		 var hiddenotherContactChkID = $("#otherContactChkID").val();
		 $("#noticeId").val(hiddennoticeId);
		 if(hiddennotice==null){
			 hiddennotice="";
		 }
		 //alert(hiddennoticeId+" "+hiddencontactId+" "+hiddennotice);
		 $("#notice").val(hiddennotice);
		 if(hiddencontactId==='' || hiddencontactId===null){
			 hiddencontactId='-1';
		 }
		 console.log('ContactID::'+hiddencontactId+' '+hiddenContactNameId+' ' +hiddenotherContactChkID);
		 if(hiddenotherContactChkID=='1'){
			 $("#noticeNameID").val(hiddenContactNameId);
			 document.getElementById("noticeContactID").checked = true;
			 $('#contactId_Release').hide();
			 $('#noticeNameID').show();
			 loadReleasecontactId(0);
		 }else{
		 loadReleasecontactId(hiddencontactId);
		 }
	//	 billnoteinlinenote = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 300}).panelInstance('billNote');
		 billnoteinlinenote = CKEDITOR.replace('billNote', ckEditorconfigforinline);
		
		  $("#datedID").change(function(){
				getDueonDayswithDate( $("#rxMasterID").val()); 
		  });
		
		  chkSplitCommissionBOValidation();
		  
		  /*$( "#CuInvoiceSaveID" ).click(function() {
			  $('#imgInvoicePDF').empty();
			  $('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

			  $('#imgInvoiceEmail').empty();
			  $('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');
			});
		  $( "#CuInvoiceSaveCloseID" ).click(function() {
			  $('#imgInvoicePDF').empty();
			  $('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

			  $('#imgInvoiceEmail').empty();
			  $('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');
			});*/
		  $('#freightBillAmount').text(formatCurrency(0));
		  var chkoverridetaxterritory=getSysvariableStatusBasedOnVariableName("OverrideReleaseTaxTerritory");
		  if(chkoverridetaxterritory!=null && chkoverridetaxterritory[0].valueLong==1){
			  _global_override_taxTerritory=true;
		  }else{
			  _global_override_taxTerritory=false;
		  }
		  global_override_taxIDBasedOnCustomer();
		  
		  $(":button").dblclick(function(e){
		       
		       return false;
		      
		         });
});

var lastID =0;
function loadCommissionPaidSales(releaseID){
	/*if($('#zTooltip'+releaseID).text()!='' && lastID==releaseID){
		$('#zTooltip'+releaseID).empty();
		$('#zTooltip'+releaseID).append("<table id='showReleaseCommissionSplitsGrid'></table><div id='showReleaseCommissionSplitsGridPager'></div>");
	}else{*/
	lastID = releaseID;
	var jomasterid=$('#joMasterHiddenID').val();
	/*createtpusage('job-Release Tab','Save Split-Commission','Info','Job,Release Tab,Save Split Commission,JoReleaseId:'+joreleaseid);*/
	createtpusage('Drop Ship-Release Tab','View Split-Commission','Info','Job,Release Tab,Drop Ship Split Commission ,JoReleaseId:'+$("#jobreleasehiddenId").val());
	$('#showReleaseCommissionSplitsGrid').jqGrid('GridUnload');
	$("#showCommissionSplitsGridDivID").empty();
	$("#showCommissionSplitsGridDivID").append("<table id='showReleaseCommissionSplitsGrid'></table><div id='showReleaseCommissionSplitsGridPager'></div>");
	$('#showReleaseCommissionSplitsGrid').jqGrid({
	datatype: 'JSON',
	mtype: 'POST',
	url:'./jobtabs3/jobCommissionListGrid',
	postData: {'JoMasterId':jomasterid,'JoReleaseId':releaseID,'tabpage':'JoRelease'},
	colNames:[ 'Id','Rep','', '% Allocated', 'Split Type',''],
	colModel :[
				{name:'ecSplitJobId', index:'ecSplitJobId', align:'left',editable:true, width:25,hidden:true},
	           	{name:'rep', index:'rep', align:'left', width:50, editable:true,hidden:false, editoptions:{size:12},editrules:{edithidden:false,required: true}},
				{name:'rxMasterId', index:'rxMasterId', align:'left',editable:true, width:10,hidden:true, editoptions:{size:6}},
				{name:'allocated', index:'allocated', align:'center',editable:true, width:25,hidden:false, editoptions:{size:6}},
				//{name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:40,hidden:true},
				{name:'splittype', index:'splittype', align:'',editable:true, width:50,hidden:false,  editoptions:{size:19}, editrules:{edithidden:false,required: true}},
				{name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:10,hidden:true, editoptions:{size:6}}],
	pgbuttons: false, 
	recordtext: '', 
	rowList: [], 
	pgtext: null, 
	viewrecords: false,
	sortname: 'rep', 
	sortorder: "asc", 
	imgpath: 'themes/basic/images', 
	caption: false,
	height:100,	
	width: 300, 
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
		/*$('#zTooltip'+releaseID).empty();
		$('#zTooltip'+releaseID).append($('#showCommissionSplitsGridDivID').html());*/
		},
	loadComplete: function(data) {
		/*$('#zTooltip'+releaseID).empty();
		$('#zTooltip'+releaseID).append($('#showCommissionSplitsGridDivID').html());*/
		/*
		var allRowsInGrid = $('#releaseCommissionSplitsGrid').jqGrid('getRowData');
		var count= $('#releaseCommissionSplitsGrid').getGridParam('reccount');
		var rowid=$("#jorowhiddenId").val();
		
		if(rowid!=null && rowid!=""){}
		
		var aVal = new Array(); 
		var sum = 0;
		$.each(allRowsInGrid, function(index, value) {
			aVal[index] = value.allocated;
			sum = Number(sum) + Number(aVal[index]); 
		});
		releaseCommissionSplitsGridsum=sum;
	*/},
	loadError : function (jqXHR, textStatus, errorThrown){	},
	onSelectRow:  function(id){/*
		SplitCommissionID = id;
		 var rowData = jQuery(this).getRowData(id); 
		 releaseselectedid=rowData["ecSplitJobId"];
		 releaseupdatecommsplitgrid=Number(releaseCommissionSplitsGridsum)-Number(rowData["allocated"]);
	 */},
	onCellSelect : function (rowid,iCol, cellcontent, e) {
		}
	});
/*}*/
	return ($("#showCommissionSplitsGridDivID").html());
}

var commissionBORequired =0 ;
function chkSplitCommissionBOValidation(){
var chkSplitCommissionYN = getSysvariableStatusBasedOnVariableName("SplitCommissionRequiredOnRelease");
if(chkSplitCommissionYN!=null && chkSplitCommissionYN[0].valueLong==1){
	$('#splitCommissionLabel').css('display','inline-block');
	commissionBORequired = 1;
}
}

/*function calculateSplit(){
	createtpusage('job-Release Tab','View Split-Commission','Info','Job,Release Tab,View Release,JoReleaseId:'+$("#jobreleasehiddenId").val());
	$('#releaseCommissionSplitsGrid').jqGrid('GridUnload');
	$("#CommissionSplitsGridDivID").empty();
	$("#CommissionSplitsGridDivID").append("<table id='releaseCommissionSplitsGrid'></table><div id='releaseCommissionSplitsGridPager'></div>");
	$('#releaseCommissionSplitsGrid').jqGrid({
	datatype: 'JSON',
	mtype: 'POST',
	pager: jQuery('#releaseCommissionSplitsGridPager'),
	url:'./jobtabs3/jobCommissionListGrid',
	postData: {'JoMasterId':document.getElementById("joMasterHiddenID").value,'JoReleaseId':$("#jobreleasehiddenId").val(),'tabpage':'JoRelease'},
	colNames:[ 'Id','Rep', '% Allocated', 'Split Type'],
	colModel :[
				{name:'ecSplitJobId', index:'ecSplitJobId', align:'left',editable:true, width:40,hidden:true},
	           	{name:'rep', index:'rep', align:'left', width:40, editable:true,hidden:false, editoptions:{size:40,
					 dataInit: function (elem) {
						 
				            $(elem).autocomplete({
				                source: 'jobtabs3/getEmployeewithNameList',
				                minLength: 1,
				                select: function (event, ui) {  ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  var product = ui.item.label; $("#releaserepId").val(id);
				                
				                $.ajax({
							        url: "./jobtabs3/getEmployeeCommissionDetail",
							        data: {id:id},
							        type: 'GET',
							        success: function(data){
								        if(data!=null)
								        	$("#allocated").val(data.jobCommissions);
							        	
							        }
							   }); 
				                
				                } }); $(elem).keypress(function (e) {
					                
				                    if (!e) e = window.event;
				                    if (e.keyCode == '13') {
				                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
				                        return false;
				                    }
				                });}	},editrules:{edithidden:false,required: true}},

				{name:'allocated', index:'allocated', align:'left',editable:true, width:40,hidden:false},
				//{name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:40,hidden:true},
				{name:'splittype', index:'splittype', align:'',editable:true, width:80,hidden:false,  editoptions:{size:40,
					 dataInit: function (elem) {
						 
				            $(elem).autocomplete({
				                source: 'jobtabs3/getSplitTypewithNameList',
				                minLength: 1,
				                select: function (event, ui) { ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  var product = ui.item.label; $("#releasesplittypeId").val(id);
								//Ajax starting point
								 Commented By Zenith
								 * $.ajax({
								        url: "./jobtabs3/getpercentagebasedonsplittype",
								        data: {id:id},
								        type: 'GET',
								        success: function(data){
									        if(data!=null)
									        	$("#allocated").val(data.defaultPct);
								        	
								        }
								   }); 
								//Ajax End Part
				               
				                } }); $(elem).keypress(function (e) {
					                
				                    if (!e) e = window.event;
				                    if (e.keyCode == '13') {
				                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
				                        return false;
				                    }
				                });}	}, editrules:{edithidden:false,required: true}}
				
],
rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
sortname: 'rep', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
height:400,	width: 475, rownumbers:false, altRows: true, altclass:'myAltRowClass', caption: '',
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
	},
loadComplete: function(data) {
	var allRowsInGrid = $('#releaseCommissionSplitsGrid').jqGrid('getRowData');

	var  count= $('#releaseCommissionSplitsGrid').getGridParam('reccount');
	var rowid=$("#jorowhiddenId").val();
	
	if(rowid!=null && rowid!=""){
	if(count>0){
		jQuery("#release").find('#'+rowid+'  input[type=checkbox]').prop('checked',true);
		}else{
			jQuery("#release").find('#'+rowid+'  input[type=checkbox]').prop('checked',false);
			}
	}
	
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
	
	 var rowData = jQuery(this).getRowData(id); 
	 releaseselectedid=rowData["ecSplitJobId"];
	 releaseupdatecommsplitgrid=Number(releaseCommissionSplitsGridsum)-Number(rowData["allocated"]);
 },
onCellSelect : function (rowid,iCol, cellcontent, e) {
	 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
	 //console.log(e);
	},
editurl:"./jobtabs3/manipulateSplitCommission"
}).navGrid('#releaseCommissionSplitsGridPager', {add:false, edit:false,del:false,refresh:false,search:false},
	//-----------------------edit options----------------------//
	{
	width:515, left:400, top: 300, zIndex:1040,
closeAfterEdit:true, reloadAfterSubmit:true,
modal:true, jqModel:true,
editCaption: "Edit Record",
beforeShowForm: function (form) 
{

	$("a.ui-jqdialog-titlebar-close").hide();						
	jQuery('#TblGrid_CommissionSplitsGrid #tr_rep .CaptionTD').empty();
	jQuery('#TblGrid_CommissionSplitsGrid #tr_rep .CaptionTD').append('Rep: ');
	jQuery('#TblGrid_CommissionSplitsGrid #tr_allocated .CaptionTD').empty();
	jQuery('#TblGrid_CommissionSplitsGrid #tr_allocated .CaptionTD').append('% Allocated: ');
	jQuery('#TblGrid_CommissionSplitsGrid #tr_splittype .CaptionTD').empty();
	jQuery('#TblGrid_CommissionSplitsGrid #tr_splittype .CaptionTD').append('Split Type: ');
	//$('#editmodlineItemGrid').css('z-index','1030');
	 $("#cData").click(function(){
		$("#rep").autocomplete("destroy");
		 $(".ui-menu-item").hide();
		$("a.ui-jqdialog-titlebar-close").show();
	}); 

},
beforeInitData:function(formid) {
	 if(releaseupdatecommsplitgrid<100){
		return true;
		
		}else{ 
			 var newDialogDiv = jQuery(document.createElement('div'));
		 
			jQuery(newDialogDiv).attr("id","msgDilg");
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should below 100</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");

			return false;
			}
	
	
},
afterShowForm: function($form) {
},
beforeSubmit:function(postdta, formid) {
	 var inputdata=$('#allocated').val();
	var sum=parseFloat(releaseupdatecommsplitgrid)+parseFloat(inputdata);
	console.log('Allocated:'+inputdata);
	if((parseInt(inputdata))>100){
		return [false, "Do not Allocate More than 100% "];
	}else{
		if(sum<=100){
			releaseCommissionSplitsGridsum=sum;
			return [true, ""];
			}else{
				return [false, "The sum of allocated should below 100"];
			}
	}
	//alert("CommissionSplitsGridsum"+CommissionSplitsGridsum);
	 
},
onclickSubmit: function(params){
	
	var rxmasterid=$('#releaserepId').val();
	var jomasterid=$('#joMasterHiddenID').val();
	var percentage=$('#allocated').val();
	var splittypeID=$('#releasesplittypeId').val();
	var splittype=$('#splittype').val();
	var joreleaseid=$("#jobreleasehiddenId").val();
	createtpusage('job-Release Tab','Edit Split-Commission','Info','Job,Release Tab,Edit Split Commission,JoReleaseId:'+joreleaseid);
	return { 'rxmasterid' : rxmasterid, 'jomasterid' : jomasterid,'percentage':percentage,'splittype':splittype,'splittypeID':splittypeID,'tabpage':'JoRelease','joreleaseid':joreleaseid,'oper' : 'Edit','ecSplitJobId':releaseselectedid};
},
afterSubmit:function(response,postData){
	//alert("inside3");
	 $("#note").autocomplete("destroy");
	$(".ui-menu-item").hide();
	$("a.ui-jqdialog-titlebar-close").show();
	PreloadData();
	return [true, loadSOLineItemGrid()]; 
	return [true,""];
}
	},
	//-----------------------add options----------------------//
	{
		width:550, left:400, top: 300, zIndex:1040,
		closeAfterAdd:true,	reloadAfterSubmit:true,
		modal:true, jqModel:false,
		addCaption: "Add Split Commission",
		onInitializeForm: function(form){
			
		},
		beforeInitData:function(formid) {
			 if(releaseCommissionSplitsGridsum<=100){
				return true;
				
				}else{ 
					 var newDialogDiv = jQuery(document.createElement('div'));
				 
					jQuery(newDialogDiv).attr("id","msgDilg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should below 100</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");

					return false;
					}
			
			
		},
		beforeShowForm: function($form){
			
			},
		afterShowForm: function($form) {
		},
		beforeSubmit:function(postdta, formid) {
			 var inputdata=$('#allocated').val();
			var sum=parseFloat(releaseCommissionSplitsGridsum)+parseFloat(inputdata);
			
			console.log('Allocated:'+inputdata);
			if((parseInt(inputdata))>100){
				return [false, "Do not Allocate 100% "];
			}else{
				if(sum<=100){
					releaseCommissionSplitsGridsum=sum;
					return [true, ""];
					}else{
						return [false, "The sum of allocated should below and equal 100"];
						}
			}
			
			//alert("CommissionSplitsGridsum"+CommissionSplitsGridsum);
			
			 
		},
		onclickSubmit: function(params){
			
			var rxmasterid=$('#releaserepId').val();
			var jomasterid=$('#joMasterHiddenID').val();
			var percentage=$('#allocated').val();
			var splittypeID=$('#releasesplittypeId').val();
			var splittype=$('#splittype').val();
			var joreleaseid=$("#jobreleasehiddenId").val();
			createtpusage('job-Release Tab','Save Split-Commission','Info','Job,Release Tab,Save Split Commission,JoReleaseId:'+joreleaseid);
			return { 'rxmasterid' : rxmasterid, 'jomasterid' : jomasterid,'percentage':percentage,'splittype':splittype,'splittypeID':splittypeID,'oper' : 'add' ,'tabpage':'JoRelease','joreleaseid':joreleaseid};
		},
		afterSubmit:function(response,postData){
			//alert("inside3");
			 $("#note").autocomplete("destroy");
			$(".ui-menu-item").hide();
			$("a.ui-jqdialog-titlebar-close").show();
			PreloadData();
			return [true, loadSOLineItemGrid()]; 
			return [true,""];
		}
	},
	//-----------------------Delete options----------------------//
	{	
		closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
		caption: "Delete Commission",
		msg: 'Delete the Split Commission Record?',

		onclickSubmit: function(params){
			//CommissionSplitsGridr
			createtpusage('job-Release Tab','Delete Split-Commission','Info','Job,Release Tab,Delete Split Commission,ecSplitJObID:'+releaseselectedid);
			return {'oper' : 'del','ecSplitJobId':releaseselectedid};
			 var taxRate =$('#customerInvoice_taxId').val();
			var freight = $('#customerInvoice_frightID').val();
			freight = parseFloat(freight.replace(/[^0-9-.]/g, ''));
			var cusoid = $('#Cuso_ID').text();
			var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam','selrow');
			var key = jQuery("#SOlineItemGrid").getCell(id, 9);
			return { 'cuSodetailId' : key, 'operForAck' : '','taxRate':taxRate,'freight':freight,'cuSoid':cusoid};
		 	
		},
		afterSubmit:function(response,postData){
			  PreloadData(); 
			 return [true, ""];
		}
	});
}*/

function calculateSplit(joReleaseID){
	var rxmasterid=$('#releaserepId').val();
	var jomasterid=$('#joMasterHiddenID').val();
	var percentage=$('#allocated').val();
	var splittypeID=$('#releasesplittypeId').val();
	var splittype=$('#splittype').val();
	var joreleaseid=$("#jobreleasehiddenId").val();
	/*createtpusage('job-Release Tab','Save Split-Commission','Info','Job,Release Tab,Save Split Commission,JoReleaseId:'+joreleaseid);*/
	createtpusage('Drop Ship-Release Tab','View Split-Commission','Info','Job,Release Tab,Drop Ship Split Commission ,JoReleaseId:'+$("#jobreleasehiddenId").val());
	$('#releaseCommissionSplitsGrid').jqGrid('GridUnload');
	$("#CommissionSplitsGridDivID").empty();
	$("#CommissionSplitsGridDivID").append("<table id='releaseCommissionSplitsGrid'></table><div id='releaseCommissionSplitsGridPager'></div>");
	$('#releaseCommissionSplitsGrid').jqGrid({
	datatype: 'JSON',
	mtype: 'POST',
	pager: jQuery('#releaseCommissionSplitsGridPager'),
	url:'./jobtabs3/jobCommissionListGrid',
	postData: {'JoMasterId':jomasterid,'JoReleaseId':joReleaseID,'tabpage':'JoRelease'},
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
				                var selectedRowId = $("#releaseCommissionSplitsGrid").jqGrid('getGridParam', 'selrow');
				                $("#"+selectedRowId+"_rxMasterId").val(id);
				                
				                $.ajax({
							        url: "./jobtabs3/getEmployeeCommissionDetail",
							        data: {id:id},
							        type: 'GET',
							        success: function(data){
							        	 var aSelectedRowId = $("#releaseCommissionSplitsGrid").jqGrid('getGridParam', 'selrow');
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
				                var selectedRowId = $("#releaseCommissionSplitsGrid").jqGrid('getGridParam', 'selrow');
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
				                });}	}, editrules:{edithidden:false,required: true}},
				 {name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:10,hidden:true, editoptions:{size:6}},
				 {name:'canDelete', index:'canDelete', align:'center',  width:20, hidden:false, editable:false, formatter:canDeleteCheckboxFormatter,   editrules:{edithidden:true}}],
	rowNum: 0, 
	pgbuttons: false, 
	recordtext: '', 
	rowList: [], 
	pgtext: null, 
	viewrecords: false,
	sortname: 'rep', 
	sortorder: "asc", 
	imgpath: 'themes/basic/images', 
	caption: false,
	height:200,	
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
		checkCommissionPaidorNotJoMaster(jomasterid,joReleaseID);
		},
	loadComplete: function(data) {
		var allRowsInGrid = $('#releaseCommissionSplitsGrid').jqGrid('getRowData');
		var count= $('#releaseCommissionSplitsGrid').getGridParam('reccount');
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
	}).navGrid("#releaseCommissionSplitsGridPager", {
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
	$("#releaseCommissionSplitsGrid").jqGrid(
			'inlineNav',
			'#releaseCommissionSplitsGridPager',
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
							$("#del_releaseCommissionSplitsGrid").addClass('ui-state-disabled');
						},
						successfunc : function(response) {
							console.log("successfunc");
							console.log(response);
							return true;
						},
						aftersavefunc : function(id) {
							console.log("aftersavefunc");
							var ids = $("#releaseCommissionSplitsGrid").jqGrid('getDataIDs');
							var cuinvrowid;
							if(ids.length==1){
								cuinvrowid = 0;
							}else{
								var idd = jQuery("#releaseCommissionSplitsGrid tr").length;
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
							var rids = $('#PoSplitCommissionGrid').jqGrid('getDataIDs');
							var nth_row_id = rids[0];
							validateReleaseSplitCommissionTotals(nth_row_id);
							$("#releaseCommissionSplitsGrid").trigger("reload");
							var grid=$("#releaseCommissionSplitsGrid");
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
				        $("#del_releaseCommissionSplitsGrid").removeClass('ui-state-disabled');
				        var ids = $("#releaseCommissionSplitsGrid").jqGrid('getDataIDs');
						var cuinvrowid;
						if(ids.length==1){
							cuinvrowid = 0;
						}else{
							var idd = jQuery("#releaseCommissionSplitsGrid tr").length;
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
						var rids = $('#PoSplitCommissionGrid').jqGrid('getDataIDs');
						var nth_row_id = rids[0];
						validateReleaseSplitCommissionTotals(nth_row_id);
						$("#releaseCommissionSplitsGrid").trigger("reload");
						var grid=$("#releaseCommissionSplitsGrid");
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
						$("#del_releaseCommissionSplitsGrid").removeClass('ui-state-disabled');
						$("#releaseCommissionSplitsGrid").trigger("reload");
						//return false;
	
					},
					afterrestorefunc : function( id ) {
						$("#del_releaseCommissionSplitsGrid").removeClass('ui-state-disabled');
						console.log('editParams -> afterrestorefunc');
				    },
					// oneditfunc: setFareDefaults
					oneditfunc : function(id) {
						console.log('OnEditfunc');
						$("#"+id+"_rep").focus();
						$("#del_releaseCommissionSplitsGrid").addClass('ui-state-disabled');
	                	/*var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
	                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
	                	alert(" >>>>>>>>>>>> "+$("#"+aSelectedRowId+"_cuSodetailId").val());
	                	 */
						}
				}
		});
}


function checkCommissionPaidorNotJoMaster(joMasterID,JoReleaseID){
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
		  var idis = $("#releaseCommissionSplitsGrid").jqGrid('getDataIDs');
		for(var i=0;i<=idis.length;i++){
			$("#canDeleteJorID_"+idis[i]).prop("disabled", true);
		}
		$("#releaseCommissionSplitsGrid_iladd").addClass('ui-state-disabled');
		$("#releaseCommissionSplitsGrid_iledit").addClass('ui-state-disabled');
}
}

function setgridtotal(){
	 var ids = $("#vendorinvoice1").jqGrid('getDataIDs'); 
	 var totalamount=0;
	 var taxamount=0;
	 var taxcellValue;
	 var taxpercentage=$('#dropshipTaxID_release').val();
	 if(taxpercentage==null||taxpercentage==""||taxpercentage==undefined){
		 taxpercentage=0;
	 }
		if(taxpercentage.length>0){
			taxpercentage=Number(floorFigureoverall(taxpercentage,2));
			//alert(taxpercentage);
		}
	 for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		 if(selectedRowId!='new_row'){
		 cellValue =$("#vendorinvoice1").jqGrid ('getCell', selectedRowId, 'quantityBilled');
		 taxcellValue=$("#vendorinvoice1").jqGrid ('getCell', selectedRowId, 'taxable');
		 var id="#canDoVIID_"+selectedRowId;
			console.log(id);
			var canDo=$(id).is(':checked');
		 if(taxcellValue=="Yes" && !canDo){
			 var eachamount=parseFloat(floorFigureoverall(cellValue.replace(/[^0-9\.-]+/g,""),2));
			 var multiplyamount=eachamount*taxpercentage/100;
			 taxamount=Number(taxamount)+Number(multiplyamount);
		 }
		var cellvalueamt=Number(floorFigureoverall(cellValue.replace(/[^0-9\.-]+/g,""),2));
		//alert(parseFloat(cellValue.replace(/[^0-9\.-]+/g,"")).toFixed(2));
		var totalfloorfig=Number(parseFloat(totalamount).toFixed(2));
		//alert("cellval="+cellvalueamt+"totalfloorfig="+totalfloorfig+"TotalAmt="+(cellvalueamt+totalfloorfig));
		 //totalamount=Number(floorFigureoverall(totalamount,2))+Number(floorFigureoverall(cellValue.replace(/[^0-9\.-]+/g,""),2));
		
		if(!canDo){
		 totalamount=cellvalueamt+totalfloorfig;
		 totalamount=parseFloat(totalamount).toFixed(2);
		}
		 //alert(totalamount);
	 }
	 }
	 if(isNaN(totalamount)){
		 totalamount=0;
	 }
	 //floorFigureoverall(
	 $("#subtotal_ID").val(formatCurrency(totalamount));
	 var freight=$("#freight_ID").val();
	 freight=freight.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 var taxvalue=$("#tax_ID").val();
	 
	 taxvalue=taxvalue.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 if(taxamount<0)
		 taxamount=-taxamount;
	 $("#tax_ID").val(formatCurrency(taxamount));
	 var overalltotal=Number(floorFigureoverall(totalamount,2))+Number(floorFigureoverall(freight,2))+Number(floorFigureoverall(taxamount,2));
	 $("#total_ID").val(formatCurrency(overalltotal));
	 $("#bal_ID").val(formatCurrency(overalltotal));
	 
	 $("#vendorinvoice1").trigger("reload");
}

function setgridtotal1(){
	 var ids = $("#vendorinvoice1").jqGrid('getDataIDs'); 
	 var totalamount=0;
	 var taxamount=0;
	 var taxcellValue;
	 var taxpercentage=$("#TaxValue").text();
		if(taxpercentage.length>0){
			taxpercentage=parseFloat(taxpercentage);
			//alert(taxpercentage);
		}
	 for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		 cellValue =$("#vendorinvoice1").jqGrid ('getCell', selectedRowId, 'amount');
		// taxcellValue=$("#vendorinvoice1").jqGrid ('getCell', selectedRowId, 'taxable');
		 taxcellValue=$("#tax_ID").val().replace(/[^0-9\.-]+/g,"");
		 if(taxpercentage>0){
			 var eachamount=parseFloat(cellValue.replace(/[^0-9\.-]+/g,""));
			 var multiplyamount=eachamount*taxpercentage/100;
			 taxamount=parseFloat(taxamount)+parseFloat(multiplyamount);
		 }
		 totalamount=parseFloat(totalamount)+parseFloat(cellValue.replace(/[^0-9\.-]+/g,""));
	 }
	 if(isNaN(totalamount)){
		 totalamount=0;
	 }
	 $("#subtotal_ID").val(formatCurrency(totalamount));
	 var freight=$("#freight_ID").val();
	 freight=freight.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 var taxvalue=$("#tax_ID").val();
	 taxvalue=taxvalue.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 $("#tax_ID").val(formatCurrency(taxamount.toFixed(2)));
	 
	 var overalltotal=parseFloat(totalamount.toFixed(2))+parseFloat(freight)+parseFloat(taxamount.toFixed(2));
	 $("#total_ID").val(formatCurrency(overalltotal));
	 $("#bal_ID").val(formatCurrency(overalltotal));
}

/*
Updated by:Velmurugan
Updated on:9-1-2014
Description:while loading the page label,textbox and search image enable hide and show based upon sysassignment table
*/
function loadjobcustomerinvoicecategories(){

	var Category1labDesc=$("#CustomerCategory1hidden").val();
	var Category2labDesc=$("#CustomerCategory2hidden").val();
	var Category3labDesc=$("#CustomerCategory3hidden").val();
	var Category4labDesc=$("#CustomerCategory4hidden").val();
	var Category5labDesc=$("#CustomerCategory5hidden").val();
	
	//CustomerCategory1label
	$("#JobCu_inv_Category1label").text(Category1labDesc);
	$("#JobCu_inv_Category2label").text(Category2labDesc);
	$("#JobCu_inv_Category3label").text(Category3labDesc);
	$("#JobCu_inv_Category4label").text(Category4labDesc);
	$("#JobCu_inv_Category5label").text(Category5labDesc);
	
	//customerInvoice__salesRepsList customerInvoice__CSRList customerInvoice__SalesMgrList customerInvoice__JobCu_inv_sList customerInvoice_PrjMgrList
	//CustomerCategory1image
	if(Category1labDesc==undefined ||Category1labDesc==null ||Category1labDesc==""||Category1labDesc.length==0){
		$("#JobCu_inv_Category1label").css({ display: "none" });
		$("#customerInvoice_salesRepsList").css({ display: "none" });
		$("#JobCu_inv_Category1image").css({ display: "none" });
	}else{
		$("#JobCu_inv_Category1label").css({ display: "inline-block" });
		$("#customerInvoice_salesRepsList").css({ display: "inline-block" });
		$("#JobCu_inv_Category1image").css({ display: "inline-block" });
	}
	if(Category2labDesc==undefined ||Category2labDesc==null ||Category2labDesc==""||Category2labDesc.length==0){
		$("#JobCu_inv_Category2label").css({ display: "none" });
		$("#customerInvoice_CSRList").css({ display: "none" });
		$("#JobCu_inv_Category2image").css({ display: "none" });
	}else{
		$("#JobCu_inv_Category2label").css({ display: "inline-block" });
		$("#customerInvoice_CSRList").css({ display: "inline-block" });
		$("#JobCu_inv_Category2image").css({ display: "inline-block" });
	}
	if(Category3labDesc==undefined ||Category3labDesc==null ||Category3labDesc==""||Category3labDesc.length==0){
		$("#JobCu_inv_Category3label").css({ display: "none" });
		$("#customerInvoice_SalesMgrList").css({ display: "none" });
		$("#JobCu_inv_Category3image").css({ display: "none" });
	}else{
		$("#JobCu_inv_Category3label").css({ display: "inline-block" });
		$("#customerInvoice_SalesMgrList").css({ display: "inline-block" });
		$("#JobCu_inv_Category3image").css({ display: "inline-block" });
	}
	if(Category4labDesc==undefined ||Category4labDesc==null ||Category4labDesc==""||Category4labDesc.length==0){
		$("#JobCu_inv_Category4label").css({ display: "none" });
		$("#customerInvoice_EngineersList").css({ display: "none" });
		$("#JobCu_inv_Category4image").css({ display: "none" });
	}else{
		$("#JobCu_inv_Category4label").css({ display: "inline-block" });
		$("#customerInvoice_EngineersList").css({ display: "inline-block" });
		$("#JobCu_inv_Category4image").css({ display: "inline-block" });
	}
	if(Category5labDesc==undefined ||Category5labDesc==null ||Category5labDesc==""||Category5labDesc.length==0){
		$("#JobCu_inv_Category5label").css({ display: "none" });
		$("#customerInvoice_PrjMgrList").css({ display: "none" });
		$("#JobCu_inv_Category5image").css({ display: "none" });
	}else{
		$("#JobCu_inv_Category5label").css({ display: "inline-block" });
		$("#customerInvoice_PrjMgrList").css({ display: "inline-block" });
		$("#JobCu_inv_Category5image").css({ display: "inline-block" });
	}
	
}
/** cancel dialog for Vendor Invoice **/
function cancelvendorInvoice(){
	$("#release").trigger("reloadGrid");
	jQuery("#openvendorinvoice").dialog("close");
	return false;
}

/** Currency formatter **/
function customCurrencyFormatter(cellValue, options, rowObject) {
	if(isNaN(cellValue)){
		if(cellValue!=undefined)
			cellValue = cellValue.replace(/[^0-9\.-]+/g,"");	
	}
	return formatCurrency(cellValue);
}
	
/** dialog for Vendor Invoice **/
jQuery(function(){
	jQuery("#openvendorinvoice").dialog({
		autoOpen:false,
		width:880,
		modal:true,
		resizable: false,
		//closeOnEscape:false,
		open: function(event, ui) { //$(".ui-dialog-titlebar-close").hide(); 
		deleteveBillDetailIDDetailId=new Array();
		},
		close:function(){ $("#release").trigger("reloadGrid"); return true;}	
	});
}); ;
	


/** release grid columns with Data **/

var arrColNamesRelease = [ "","", "","","","Split","ReleaseId","Released","Type","ManufacturerId","rxMasterId","Manufacturer","Note","","Allocated", "JoDetailedID", "VePOID","CusoID", "Bill Note", "Web Sight" , "PONumber", "ShipTo", "rxContact", 'Email Time', 'BillToAdd', 'ShipToAdd', 'AddressID', 'CustomerPONumber','Address1','Address2','City','State','Zip', 'POID'];
var arrColModelRelease = [
                          	{name : 'transStatus',index : 'transStatus',align : 'center',width : 8,editable : false,hidden : true},
                          	{name:'checkcloseoropenhidden',index:'checkcloseoropenhidden',align:'center',width:8,editable: false,hidden:true },
                          	{name:'checkcloseoropen',index:'checkcloseoropen',align:'center',width:8,editable: false,hidden:false , formatter:releasestatusImage},
                          	{name:'ponumber',index:'ponumber',align:'center',width:8,editable: false,hidden:false},
                          	/*{name:'ponumber',index:'ponumber',align:'center',width:8,editable: false,hidden:false, formatter: alphabetSeq},*/
							{name : 'transactionStatus',index : 'transactionStatus',align : 'center',width : 8,editable : false,hidden : true},
                          	{name:'splitchkbox', index: 'splitchkbox', width: 8, align: 'center',formatter:releaseSplitCommissionImage,editable:false},
                          	{name:'joReleaseId', index:'joReleaseId', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'released', index:'released', align:'center', width:20, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'type', index:'type', align:'center', width:30,editable:true,hidden:false, edittype:'select',formatter:typeFormatter, editoptions:{value:{1:'Drop Ship',2:'Stock Order',3:'Bill Only',4:'Commission',5:'Service'}}},
							{name:'manufacturerId', index:'manufacturerId', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'rxMasterId', index:'rxMasterId', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'manufacturer', index:'manufacturer', align:'left', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'note', index:'note', align:'left', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'billNoteImage',index:'billNoteImage',align:'right',width:4,editable: false,hidden: false, formatter: billImage},
							{name:'estimatedBilling', index:'estimatedBilling', align:'right', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}, formatter:customCurrencyFormatter},
							{name:'joReleaseDetailid', index:'joReleaseDetailid', align:'left', width:30, editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'vePoId', index:'vePoId', align:'left', width:20, editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'cuSOID', index:'cuSOID', align:'left', width:20, editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'billNote', index:'billNote', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'webSight', index:'webSight', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'ponumber', index:'ponumber', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'shipToMode', index:'shipToMode', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'rxVendorContactID', index:'rxVendorContactID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'emailTimeStamp', index:'emailTimeStamp', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'billToAddressID', index:'billToAddressID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'shipToAddressID', index:'shipToAddressID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'rxAddressID', index:'rxAddressID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'customerPONumber', index:'customerPONumber', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							
							
							{name:'address1', index:'address1', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'address2', index:'address2', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'city', index:'city', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'state', index:'state', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'zip', index:'zip', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							
							{name:'poid', index:'poid', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}}];

var colName_poAmount = "PO Amount";
var colModel_poAmount = {name:'po', index:'po', align:'right', width:30 , formatter:customCurrencyFormatter};

var colName_InvoiceAmount = "Invoice Amount";
var colModel_InvoiceAmount = {name:'invoiceAmount', index:'invoiceAmount', align:'right', width:30, formatter:customCurrencyFormatter};

/** release grid columns hide/show function **/

function checkPoAmount() {
	var poAmt = document.getElementsByClassName("PoCheck");
	for(var i=0;i<poAmt.length;i++) {
		var box = poAmt[i];
		if(box.type === "checkbox" && box.checked) {
			if (box.value != "Released" && box.value != "Type" && box.value != "Manufacturer" && box.value != "Note" && box.value != "Allocated" &&!isExistInArray(arrColNamesRelease, box.value)){
				if (box.value === "poAmount" && !isExistInArray(arrColNamesRelease, "poAmount")) {
					arrColNamesRelease.push(colName_poAmount);
					arrColModelRelease.push(colModel_poAmount);
				}
			}
		}else if (box.type === "checkbox" && !(box.checked)) {
			if (box.value === "poAmount"){
				removeA(arrColNamesRelease,colName_poAmount);
				removeA(arrColModelRelease,colModel_poAmount);
			}
		}
	}
	$("#release").jqGrid('GridUnload');
	release_grid(arrColNamesRelease, arrColModelRelease);
	$("#release").trigger("reloadGrid");
	return true;
}


function checkInvoAmount() {
	var poAmt = document.getElementsByClassName("InvoCheck");
	for(var i=0;i<poAmt.length;i++) {
		var box = poAmt[i];
		if(box.type === "checkbox" && box.checked) {
			if (box.value != "Released" && box.value != "Type" && box.value != "Manufacturer" && box.value != "Note" && box.value != "Allocated" &&!isExistInArray(arrColNamesRelease, box.value)){
				if (box.value === "invoiceAmount" && !isExistInArray(arrColNamesRelease, "invoiceAmount")) {
					arrColNamesRelease.push(colName_InvoiceAmount);
					arrColModelRelease.push(colModel_InvoiceAmount);
				  }
			}
		}else if (box.type === "checkbox" && !(box.checked)) {
			 if (box.value === "invoiceAmount"){
				removeA(arrColNamesRelease,colName_InvoiceAmount);
				removeA(arrColModelRelease,colModel_InvoiceAmount);
			}
		}
	}
	$("#release").jqGrid('GridUnload');
	release_grid(arrColNamesRelease, arrColModelRelease);
	$("#release").trigger("reloadGrid");
	return true;
}
/**
 * Javascript method to get/seperate alphabet sequence number from PoNumber.  
 * By default it will get all the alphabets.  So to het the exact Release letter, we need to split the POnumber at "-".
 * @param cellValue
 * @param options
 * @param rowObject
 * @returns {String} alphabet sequence of PO
 */
/*var stop=false;
function splittypecheckbox(cellValue, options, rowObject) {
	
    
	
	var cellvalue = cellValue + "";
	cellvalue = cellvalue.toLowerCase();
	var bchk = cellvalue.search(/(false|0|no|off|n)/i) < 0 ? " checked=\"checked\"" : "";
	
	if(!stop){
		alert(cellvalue);
		alert(bchk);
		stop=true;
	}
	
	    return "<input type='checkbox' onclick=\"ajaxSave('" + options.rowId + "', this);\" " + bchk + " value='" + cellvalue + "'  />";
}
function ajaxSave(rowid, curCheckbox) {
    alert('Ahoj');
}*/
function alphabetSeq(cellValue, options, rowObject){
	try {
		
		 var element = "";
		console.log('Cell Value:'+cellValue);
		 if(cellValue !== null){
			 var aPonumber=cellValue;
			 var aPONumSplitArray = new Array();
			 var jobNum = $("#jobNumber_ID").text().trim();
			 var len1 = jobNum.length;
			 console.log('jobNum :: '+jobNum);
			 console.log('aPonumber :: '+aPonumber);
			 var aStr ='' ;
			 try{
				 aStr = aPonumber.substr(len1);
				 console.log('Jenith PONum: '+len1+' '+aStr);
				 
			/*
			 *  aPONumSplitArray = $.trim(aPonumber).split("-");
				 aStr = aPONumSplitArray[1].match(/[A-z]/g);
				 
				 Added By Commented By Zenith
			 *  if (/^[a-zA-Z0-9]*$/.test('-') == false) {
				 aPONumSplitArray = $.trim(aPonumber).split("-");
				 aStr = aPONumSplitArray[1].match(/[A-z]/g);
				 }*/
			 
			 //alert("3  "+cellValue);
			
			// if(aPONumSplitArray[1]==null){
			//	 aStr = aPONumSplitArray[0].match(/[A-z]/g);
			 //}else{
			
				
			 }catch(err){
				 
				 var unsplitponumber =$.trim(aPonumber).substring(aPonumber.length-4,aPonumber.length);
				 aStr=unsplitponumber.match(/[A-z]/g);
			 }
				 
			 //}
			  
			 
			 for(var i=0;i<aStr.length;i++){
			
				 element = element + (aStr[i]);
			
			 }
		 }
		return aStr;
	} catch (e) {
		//alert(e);
		console.log("Error:" + e + "\n\nFalse will be returned.");
		return false;
	}
} 

function billImage(cellValue, options, rowObject){
	var element = '';
   if(cellValue !== '' && cellValue !== null){
	   element = "<img src='./../resources/Icons/dollar.png' style='vertical-align: middle;'>";
   }else if(cellValue === ''){
	   element = "";
   }else if(cellValue === null){
	   element = "";
   }else{
	   element = "";
   }
return element;
} 

function releasestatusImage(cellValue, options, rowObject) {
	var element = '';
	var transstatus=rowObject['transStatus'];
	var type=rowObject['type'];
	console.log("transstatus=="+transstatus);
	
	/*if (cellValue == "true") {
		element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
	} else if (cellValue == "false") {
		element = "<img src='./../resources/images/circle_minus.png' style='vertical-align: middle;'>";
	}else if(cellValue=="close"){
		element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
	}*/
 if(type==1||type==2 ||type==4 ||type==5){
	 if(transstatus==-1){
			//-1 -Void
		 element = "<img src='./../resources/Icons/close.png' style='vertical-align: middle;'>";
		}else if(transstatus==0){
			//0 -Hold
			element = "<img src='./../resources/Icons/stop.png' style='vertical-align: middle;'>";
			
		}else if(transstatus==1){
			//1 -Open
			 if (cellValue == "false") {
					element = "<img src='./../resources/images/circle_minus.png' style='vertical-align: middle;'>";
				}else{
					element="";
				}
		}else if(transstatus==2){
			//2 -Close
			element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
		}else{
			if (cellValue == "true") {
				element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
			} else if (cellValue == "false") {
				element = "<img src='./../resources/images/circle_minus.png' style='vertical-align: middle;'>";
			}else if(cellValue=="close"){
				element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
			}
		}
	}else{
		if (cellValue == "true") {
			element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
		} else if (cellValue == "false") {
			element = "<img src='./../resources/images/circle_minus.png' style='vertical-align: middle;'>";
		}else if(cellValue=="close"){
			element = "<img src='./../resources/images/circle_tick.png' style='vertical-align: middle;'>";
		}
	}
	
	
	
	
	return element;
}

var forCommission = 0;
function releaseSplitCommissionImage(cellValue, options, rowObject){
	var element = '';
	var transstatus=rowObject['transStatus'];
	var type=rowObject['type'];
	var joReleaseID=rowObject['joReleaseId'];
	if (cellValue) {
		forCommission=forCommission+1;
		/*if(transstatus==-1){
			element = "<img src='./../resources/Icons/check_red.png' style='vertical-align: middle;'>";
		}else if(transstatus==0){
			element = "<img src='./../resources/Icons/check_red.png' style='vertical-align: middle;'>";
		}else if(transstatus==1){
			element = "<img src='./../resources/Icons/check_orange.png' style='vertical-align: middle;'>";
		}else if(transstatus==2){
			element = "<img src='./../resources/Icons/check_blue.png' style='vertical-align: middle;'>";
		}else{
			element = "<img src='./../resources/Icons/check_green.png' style='vertical-align: middle;'>";
		}*/
		element = "<span style='width:10px;height:10px;' onmouseover='showMyCommissions("+joReleaseID+")' onmouseout='hideMyCommission("+joReleaseID+")' >  <img src='./../resources/Icons/check_green.png' style='vertical-align: middle; mouse:pointer;'> </span><span id='zTooltip"+joReleaseID+"'></span>";
		//loadCommissionPaidSales(joReleaseID);
		$('#zTooltip'+joReleaseID).css("width","300px");
		$('#zTooltip'+joReleaseID).css("padding","2px 2px");
		$('#zTooltip'+joReleaseID).css("margin-top","0");
		$('#zTooltip'+joReleaseID).css("margin-left"," -120px");
		$('#zTooltip'+joReleaseID).css("opacity","0");
		$('#zTooltip'+joReleaseID).css("visibility","hidden");
		$('#zTooltip'+joReleaseID).css("z-index","2147483647");
		$('#zTooltip'+joReleaseID).css("position","absolute");
		$('#zTooltip'+joReleaseID).css("font-family","Arial");
		$('#zTooltip'+joReleaseID).css("font-size","12px");
		$('#zTooltip'+joReleaseID).css("font-style","normal");
		$('#zTooltip'+joReleaseID).css("border-radius","3px");
		$('#zTooltip'+joReleaseID).css("box-shadow","2px 2px 2px #999");
		$('#zTooltip'+joReleaseID).css("-webkit-transition-property","opacity, margin-top, visibility, margin-left");
		$('#zTooltip'+joReleaseID).css("-webkit-transition-duration","0.4s, 0.3s, 0.4s, 0.3s");
		$('#zTooltip'+joReleaseID).css("-webkit-transition-timing-function","ease-in-out, ease-in-out, ease-in-out, ease-in-out");
		$('#zTooltip'+joReleaseID).css("transition-property","opacity, margin-top, visibility, margin-left");
		$('#zTooltip'+joReleaseID).css("transition-duration","0.4s, 0.3s, 0.4s, 0.3s");
		$('#zTooltip'+joReleaseID).css("transition-timing-function","ease-in-out, ease-in-out, ease-in-out, ease-in-out");
	}
	
	return element;
}

function showMyCommissions(joReleaseIDs){
	$('#zTooltip'+joReleaseIDs).empty();
	$('#zTooltip'+joReleaseIDs).append(loadCommissionPaidSales(joReleaseIDs));
	$('#zTooltip'+joReleaseIDs).css("width","300px");
	$('#zTooltip'+joReleaseIDs).css("padding","2px 2px");
	$('#zTooltip'+joReleaseIDs).css("position","absolute");
	$('#zTooltip'+joReleaseIDs).css("font-family","Arial");
	$('#zTooltip'+joReleaseIDs).css("font-size","12px");
	$('#zTooltip'+joReleaseIDs).css("font-style","normal");
	$('#zTooltip'+joReleaseIDs).css("border-radius","3px");
	$('#zTooltip'+joReleaseIDs).css("box-shadow","2px 2px 2px #999");
	$('#zTooltip'+joReleaseIDs).css("-webkit-transition-property","opacity, margin-top, visibility, margin-left");
	$('#zTooltip'+joReleaseIDs).css("-webkit-transition-duration","0.4s, 0.3s, 0.4s, 0.3s");
	$('#zTooltip'+joReleaseIDs).css("-webkit-transition-timing-function","ease-in-out, ease-in-out, ease-in-out, ease-in-out");
	$('#zTooltip'+joReleaseIDs).css("transition-property","opacity, margin-top, visibility, margin-left");
	$('#zTooltip'+joReleaseIDs).css("transition-duration","0.4s, 0.3s, 0.4s, 0.3s");
	$('#zTooltip'+joReleaseIDs).css("transition-timing-function","ease-in-out, ease-in-out, ease-in-out, ease-in-out");
	$('#zTooltip'+joReleaseIDs).css("z-index","12345");
	$('#zTooltip'+joReleaseIDs).css("opacity","1");
	$('#zTooltip'+joReleaseIDs).css("text-decoration","none");
	$('#zTooltip'+joReleaseIDs).css("visibility","visible");
	$('#zTooltip'+joReleaseIDs).css("overflow","visible");
	$('#zTooltip'+joReleaseIDs).css("margin-top","20px");
	$('#zTooltip'+joReleaseIDs).css("display","inline");
	$('#zTooltip'+joReleaseIDs).css("margin-left","-90px");
}

function hideMyCommission(joReleaseIDs){
	$('#zTooltip'+joReleaseIDs).empty();
	$('#zTooltip'+joReleaseIDs).css("width","300px");
	$('#zTooltip'+joReleaseIDs).css("padding","2px 2px");
	$('#zTooltip'+joReleaseIDs).css("margin-top","0");
	$('#zTooltip'+joReleaseIDs).css("margin-left"," -120px");
	$('#zTooltip'+joReleaseIDs).css("opacity","0");
	$('#zTooltip'+joReleaseIDs).css("visibility","hidden");
	$('#zTooltip'+joReleaseIDs).css("z-index","10");
	$('#zTooltip'+joReleaseIDs).css("position","absolute");
	$('#zTooltip'+joReleaseIDs).css("font-family","Arial");
	$('#zTooltip'+joReleaseIDs).css("font-size","12px");
	$('#zTooltip'+joReleaseIDs).css("font-style","normal");
	$('#zTooltip'+joReleaseIDs).css("border-radius","3px");
	$('#zTooltip'+joReleaseIDs).css("box-shadow","2px 2px 2px #999");
	$('#zTooltip'+joReleaseIDs).css("-webkit-transition-property","opacity, margin-top, visibility, margin-left");
	$('#zTooltip'+joReleaseIDs).css("-webkit-transition-duration","0.4s, 0.3s, 0.4s, 0.3s");
	$('#zTooltip'+joReleaseIDs).css("-webkit-transition-timing-function","ease-in-out, ease-in-out, ease-in-out, ease-in-out");
	$('#zTooltip'+joReleaseIDs).css("transition-property","opacity, margin-top, visibility, margin-left");
	$('#zTooltip'+joReleaseIDs).css("transition-duration","0.4s, 0.3s, 0.4s, 0.3s");
	$('#zTooltip'+joReleaseIDs).css("transition-timing-function","ease-in-out, ease-in-out, ease-in-out, ease-in-out");
}

//getcolumnindexbyname
getColumnIndexByName = function(grid, columnName) {
    var cm = grid.jqGrid('getGridParam', 'colModel'), i, l;
    for (i = 0, l = cm.length; i < l; i += 1) {
        if (cm[i].name === columnName) {
            return i; // return the index
        }
    }
    return -1;
}
/** get release grid values **/
var boolean = false;
var posit_job_release=0;
var release_grid = function (arrColNamesRelease, arrColModelRelease){
	$("#release").jqGrid({
		url:'./jobtabs5/release',
		datatype: 'JSON',
		mtype: 'GET',
		//pager: jQuery('#releasepager'),
		colNames : arrColNamesRelease,
		colModel : arrColModelRelease,
		
		rowNum: 0,	pgbuttons: false,/*autowidth: true,*/recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	
		height:240,	width: 1080, altRows: true, altclass:'myAltRowClass',
		postData: {jobNumber: function() { return $("#jobNumber_ID").text(); },joMasterID: $("#joMaster_ID").text()},
		loadBeforeSend: function(xhr) {
			posit_job_release= jQuery("#release").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		/*afterInsertRow : function(rowid, rowdata)
		{
			console.log("billNoteImage :::"+rowdata.billNoteImage+"  :value.estimatedBilling::  "+value.estimatedBilling);
		    if (rowdata.billNoteImage !== '' && rowdata.billNoteImage !== )
		    {
		    	value.estimatedBilling = "<img src='./../resources/Icons/dollar.png' style='vertical-align: middle;'>" +"  "+value.estimatedBilling;
		    	console.log("billNoteImage :::"+rowdata.billNoteImage+"  :value.estimatedBilling::  "+value.estimatedBilling);
		    	$(this).jqGrid("setRowData", rowid, rowdata);
		    }

		}*/
		gridComplete: function() {
			 jQuery("#release").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_release);
             posit_job_release=0;
		    var rowData = $("#release").getRowData();
		 
		    for (var i = 0; i < rowData.length; i++) 
		    {
		        $("#release").jqGrid('setRowData', rowData[i], false, {color:'red'});
		    }
		    $("#release").jqGrid('setSelection', releasselectrowid, true);
	    	
	    	if(rowData.length>0)
	    		{
	    		 var rowDataHiddenID = $("#release").jqGrid('getCell', 1, 'joReleaseId');
	    		 	
	    		 jQuery('#shiping').jqGrid('clearGridData')
				 loadShipingGrid(rowDataHiddenID,"");
	    		}else{
	    			$("#shiping").trigger("reloadGrid");
	    		}
	    	
	    	if(boolean)
			{
				var gridRows = $('#release').getRowData();
				var rowid = parseInt(gridRows.length);
				//Commented By Zenith on 25-03-2015
				var onSelectRowHandler = $("#release").jqGrid("getGridParam", "onSelectRow");
				onSelectRowHandler.call($("#release")[0], rowid);
//				alert(rowid);
				var ondblClickRowHandler = $("#release").jqGrid("getGridParam", "ondblClickRow");					
				ondblClickRowHandler.call($("#release")[0], rowid);
				
			}
		},
		loadComplete: function(data) {
			 var iCol = getColumnIndexByName ($(this), 'splitchkbox'), rows = this.rows, i, c = rows.length;
			 for (i = 0; i < c; i += 1) {
             	
                 $(rows[i].cells[iCol]).click(function (e) {
                 	  var id = $(e.target).closest('tr')[0].id,
                         isChecked = $(e.target).is(':checked');
                      var rowDataHiddenID = $("#release").jqGrid('getCell', id, 'joReleaseId');
                      var rowData = $("#release").jqGrid('getCell', id, 'billNoteImage');
                      var rowalphabetic=$("#release").jqGrid('getCell', id, 'ponumber');
                      var releaseType=$("#release").jqGrid('getCell', id, 'type');
                       $("#jotitlehiddenId").val(rowalphabetic);
                       $("#release").jqGrid('setSelection', id, true);
                       $("#jorowhiddenId").val(id);
                 	   $("#jobreleasehiddenId").val(rowDataHiddenID);
                 	  // var dataFromCellByColumnIndex = jQuery('#paymentgridtable').jqGrid ('getCell', id, 6);	
                 	   var checkvalue = (isChecked? 'true': 'false');
                 	/*   if(releaseType=='Bill Only'){
                 		  calculateSplit(rowDataHiddenID);
                 		  //addempinsplitcomrelease($("#jobreleasehiddenId").val(),rowalphabetic);
                 		  addempinsplitcomrelease(rowDataHiddenID,rowalphabetic);
                 	   }*/
                 	
                 });
             }
			 
			 
			var gridRowsgg = $('#changeOrderGrid').getRowData();
       		var rowDatagg = new Array();
       		//$("#PONumberID").empty();
       		//$("#PONumberID").append("<option value='-1'>-Select-</option>");
       		//$("#customerPONumberSelectID").empty();
       		//$("#customerPONumberSelectID").append("<option value='-1'>-Select-</option>");
       		
       		//for (var i = 0; i < gridRowsgg.length; i++) {
       		//	var rowgg = gridRowsgg[i];
       		//	$("#PONumberID").append("<option value='"+rowgg['customerPonumber']+"'>"+rowgg['customerPonumber']+"</option>");
       		//	$("#customerPONumberSelectID").append("<option value='"+rowgg['customerPonumber']+"'>"+rowgg['customerPonumber']+"()</option>");
       		//}
			loadPONumbers();
			loadCustomerPONumber();
            
			$("#release").setSelection(releasselectrowid, true);
			var grid = $("#release");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			//var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
			var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'customerPONumber');
			/*var cusPONumber =  grid.jqGrid('getCell', rowId, 'customerPONumber');
			$("#PONumberID").append("<option value='"+cusPONumber+"'>"+cusPONumber+"</option>");
   			$("#customerPONumberSelectID").append("<option value='"+cusPONumber+"'>"+cusPONumber+"()</option>");*/
			
			
			if($("#customerpodetails").val() !=null || $("#customerpodetails").val()!='' ){
				//$("#PONumberID option[value=" + $("#customerpodetails").val() + "]").attr("selected", true);
				$("#PONumberID").val($("#customerpodetails").val());
			}else{
				//$("#PONumberID option[value=-1]").attr("selected", true);
				$("#PONumberID").val(" - Select - ");
			}
			billAmountCal();
			var allocateamount=$("#allocate").text();
			var unAllocatedAmount = $("#unAllocated").text().replace(/[^0-9\.-]+/g,"");
			
			var sumTotal = '';
			var allocatedamount=allocateamount.replace(/[^0-9\.-]+/g,"");
			sumTotal = Number(allocatedamount) + Number(unAllocatedAmount);			
			//$('#release').jqGrid('setCell',"","billNoteImage","",{border-right-color: transparent;});
			var ids = $('#release').jqGrid('getDataIDs');
			$('#release_billNoteImage').removeClass("ui-state-default ui-th-column ui-th-ltr");			
		    if (ids) {
		    	console.log("if loop");
		        var sortName = $('#release').jqGrid('getGridParam','billNoteImage');
		        var sortOrder = $('#release').jqGrid('getGridParam','estimatedBilling');
		        for (var i=0;i<ids.length;i++) {
		        	console.log("if loop ::: "+ids[i]);
		        	$('#release').jqGrid('setCell', ids[i], 'billNoteImage', '', '',
		                        {style:'border-right-color: transparent !important;'});
		        	$('#release').jqGrid('setCell', ids[i], 'estimatedBilling', '', '',
	                        {style:'border-left-color: transparent !important;'});
		        }
		    }
		    
			//.ui-jqgrid tr.ui-row-ltr td { border-right-color: transparent; }
			//$("#estimate").empty();
			//$("#estimate").text(formatCurrency(sumTotal));
			/*var allRowsInGrid = $('#release').jqGrid('getRowData');
			$.each(allRowsInGrid, function(index, value) {
				var cellValue = value.billNoteImage;
				var allocateAmt = value.estimatedBilling;
				console.log("Image--->"+cellValue+"::::::"+allocateAmt);
				var element;
				 if(cellValue !== '' && cellValue !== null){
					   element = "<img src='./../resources/Icons/dollar.png' style='vertical-align: middle;'>" +"  "+value.estimatedBilling;
				   }else if(cellValue === ''){
					   element = value.estimatedBilling;
				   }else if(cellValue === null){
					   element = value.estimatedBilling;
				   }else{
					   element = value.estimatedBilling;
				   }
				 console.log("Final value--->"+element);
				 value.estimatedBilling =element;	
				 jQuery("#release").setCell (index,'estimatedBilling',value.estimatedBilling)
				 loadComplete:{
			
			
		
			});*/
		    loadBillingEntireJob();
		    
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
			releasselectrowid=id;
			var rowData = jQuery(this).getRowData(id);
			var releaseType = rowData["type"];
			var releaseTypeIDs="";			
			if(releaseType=="Drop Ship"){releaseTypeIDs="1";$("#commissionID").hide();$("#billedTDID").show();}
			if(releaseType=="Stock Order"){releaseTypeIDs="2";$("#commissionID").hide();$("#billedTDID").show();}
			if(releaseType=="Bill Only"){releaseTypeIDs="3";$("#commissionID").hide();$("#billedTDID").show();}
			if(releaseType=="Commission"){
				releaseTypeIDs="4";
				$("#billedTDID").hide();
				$("#commissionID").show();
			}
			if(releaseType=="Service"){releaseTypeIDs="5";$("#commissionID").hide();$("#billedTDID").show();}
			if(releaseType=="--Select--"){releaseTypeIDs="-1";$("#commissionID").hide();$("#billedTDID").show();}
			$('#releasesTypeID option[value="'+releaseTypeIDs+'"]').attr('selected', 'selected');
			
			var joDetailId = rowData["joReleaseDetailid"];
			var manufacturerID = rowData["rxMasterId"];
			var manufacturerName = rowData["manufacturer"];
			var PONumberID = rowData["ponumber"];
			var customerPONumber = rowData["customerPONumber"];
			var aCustomerPONumber = rowData["poid"];
			var cuInvoiceID = rowData["cuInvoiceID"];
			var vePOID = rowData["vePoId"];
			var shipToID = rowData["shipToMode"];
			var joReleaseId = rowData["joReleaseId"];
			var releaseType = rowData["type"];
			if(releaseType==='Commission'){
				loadCommissionShipingGrid(joReleaseId,releaseType);
				$("#commissionFields").show();
				$("#billedID").empty();
				$("#billedID").text('Received');
				$("#unBilledID").empty();
				$("#unBilledID").text('Balance');
				getCommissionAmount();
			}else{
				$("#commissionFields").hide();
				$("#billedID").empty();
				$("#billedID").text('Billed');
				$("#unBilledID").empty();
				$("#unBilledID").text('Unbilled');
				 jQuery('#shiping').jqGrid('clearGridData')
				 loadShipingGrid(joReleaseId,releaseType);
				
			}
			$('#shiping').jqGrid('setGridParam',{postData: {jobNumber: function() { return $("#jobNumber_ID").text(); }, "joDetailsID" : joDetailId}});
			//Commented by Jenith for avoid Dubling $("#shiping").trigger("reloadGrid"); 
			$("#manufacture_ID").text(manufacturerID);
			//$("#vendorinvoice1").jqGrid('GridUnload');
			//loadVendorInvoice(vePOID);
			$('#vendorinvoice1').jqGrid('setGridParam',{postData: {vePoId: function() { return vePOID; }}});
			$("#vendorinvoice1").trigger("reloadGrid");
			$("#customerInvoice_lineitems").jqGrid('GridUnload');
			$('#customerInvoice_lineitems').jqGrid('setGridParam',{postData: {cuInvoiceID: function() { return cuInvoiceID; }}});
			$("#customerInvoice_lineitems").trigger("reloadGrid");
			$("#manufacurterNameID").val(manufacturerName);
			$("#rxMasterID").val(manufacturerID);
			setvendorFullAddress(manufacturerID);
			$("#addressNameID").text(rowData["address1"]+"\n"+rowData["address2"]+"\n"+rowData["city"]+"\n"+rowData["state"]+rowData["zip"]);
			$("#poNumberID").val($("#jobNumber_ID").text()+""+PONumberID);
			loadcusoID(joReleaseId);	
			if(shipToID == 0){
				$("#shipTo").show();
				$("#shipTo1").hide();
			}else{
				$("#shipTo1").show();
				$("#shipTo").hide();
			}
			
			getVendorDetails(joDetailId, vePOID);
			
			$("#customerPONumberID").val("");
			$("#customerPONumberID").val(customerPONumber);
			if($("#customerpodetails").val() !=null || $("#customerpodetails").val()!=''){
				$("#PONumberID").val(aCustomerPONumber);
			}else{
				$("#PONumberID").val(" - Select - ");
			}
			
			billedUnbilled(joReleaseId);
		},
    	ondblClickRow: function(rowid) {
    		  chkoverridetaxterritory=getSysvariableStatusBasedOnVariableName("OverrideReleaseTaxTerritory");
    		  if(chkoverridetaxterritory!=null && chkoverridetaxterritory[0].valueLong==1){
    			  _global_override_taxTerritory=true;
    		  }else{
    			  _global_override_taxTerritory=false;
    		  }
    		resetSOGeneralForm();
    		global_override_taxIDBasedOnCustomer();
    		editreleasedialog(rowid);
    		var rowDatas = jQuery(this).getRowData(rowid);
    		var joReleaseId = rowDatas["joReleaseId"];
    		loadSplitCommissionList($("#joMasterHiddenID").val(),joReleaseId);
    		loadPOSplitCommissionList($("#joMasterHiddenID").val(),joReleaseId);
    	}
	});
};
var posit_job_shiping=0;
/** Shipping grid  column with values**/
 function loadShipingGrid(joDetailId,releaseType){
	 //<table id="shiping" style="width:20px"></table><div id="shipingpager"></div>
	 document.getElementById("customerInvoicebtnID").disabled = false;
	 $('#customerInvoicebtnID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
	 $("#shiping").jqGrid('GridUnload');
	 $("#shipingGrid").empty();
	 $("#shipingGrid").append("<table id='shiping'></table><div id='shipingpager'></div>");
	 try{
	var jobnumber = $("#jobNumber_ID").text();
	
	if(joDetailId==undefined || releaseType==undefined){
		return false;
	}
	
	 $("#shiping").jqGrid({
	url:'./jobtabs5/shipping?jobNumber='+jobnumber+'&joDetailsID='+joDetailId+'&releaseType='+releaseType,
	datatype: 'JSON',
	mtype: 'GET',
	colNames:['Ship Date','Shipping Line ','Vendor Date','Vendor Invoice','Vendor Amount ($)','','Customer Date','Customer Amount ($)', 'ShipID', 'JoReleaseID', 'VeBillID', 'CoAccID', 'CuInvoiceID','InvoiceDate','ShiptoMode','vendorSubTotalAmt','transStatus','chkno','datepaid','vendorappliedamt','cIopenStatus','trackingURL','',''],
	colModel:[
		{name:'shipDate',index:'shipDate',align:'center',width:50},
		{name:'shippingLine',index:'shippingLine',align:'center',width:50},
	  /*{name:'vendorDate',index:'vendorDate',align:'center',width:60}, */
		{name:'veBillDate',index:'veBillDate',align:'center',width:60}, 
		{name:'vendorInvoice',index:'vendorInvoice',align:'center',width:50}, 
		{name:'vendorAmount',index:'vendorAmount',align:'right',width:60, formatter:customCurrencyFormatter}, 
		{name:'customerDate',index:'customerDate',align:'center',width:70, hidden: true},
		{name:'invoiceDate',index:'invoiceDate',align:'center',width:70},
		{name:'customerAmount',index:'customerAmount',align:'right',width:60, formatter:customCurrencyFormatter},
		{name:'veShipViaID',index:'veShipViaID',align:'right',width:60, hidden: true},//hidden: true changed to false for testing
		{name:'joReleaseDetailID',index:'joReleaseDetailID',align:'right',width:60, hidden: true},
		{name:'veBillID',index:'veBillID',align:'right',width:60, hidden: true},
		{name:'coAccountID',index:'coAccountID',align:'right',width:60, hidden: true},
		{name:'cuInvoiceID',index:'cuInvoiceID',align:'right',width:60, hidden: true},
		{name:'invoiceDate',index:'invoiceDate',align:'center',width:50, hidden: true},
		{name:'shiptoMode',index:'shiptoMode',align:'right',width:60, hidden: true},
		{name:'vendorsubtotalAmt',index:'vendorsubtotalAmt',align:'right',width:60, hidden: true},
		{name:'transactionStatus',index:'transactionStatus',align:'right',width:60, hidden: true},
		{name:'vechkNo',index:'vechkNo',align:'right',width:60, hidden: true,formatter:displayChkNo},
		{name:'vedatePaid',index:'vedatePaid',align:'right',width:60, hidden: true,formatter:dateformatter},
		{name:'vendorAppliedAmt',index:'vendorAppliedAmt',align:'right',width:60, hidden: true},
		{name:'cIopenStatus',index:'cIopenStatus',align:'right',width:60, hidden: true},
		{name:'webSight',index:'webSight',align:'right',width:60, hidden: true},
		{name:'trackingNumber',index:'trackingNumber',align:'right',width:60, hidden: true},
		{name:'',index:'',align:'center',width:50, hidden: false,formatter:buttonAddCostfn}
		],
		rowNum: 0,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		sortname: 'shipDate', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
		height:86,	width: 1080, altRows: true, altclass:'myAltRowClass',rownumbers:true,	
		loadBeforeSend: function(xhr) {
			posit_job_shiping= jQuery("#shiping").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		loadComplete: function(data) {
			$("#financial").trigger("reloadGrid");
			$('#shiping').trigger('reloadGrid');
			//$("#shiping").setSelection(1, true);
			},
		gridComplete: function () {
	        jQuery("#shiping").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_shiping);
	        posit_job_shiping=0;
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
				
				var rowData = jQuery(this).getRowData(id); 
				var cusoInvId = rowData["cuInvoiceID"];

				
				$('#cuinvoiceIDhidden').val(cusoInvId);
				CheckCustomerInvoiceSave();
				/*var rowData = jQuery(this).getRowData(id);
				var aShipViaId = rowData["veShipViaID"];
				var aVendorInvoice = rowData["vendorInvoice"];
				var aVendorDate = rowData["vendorDate"];
				var aShipDate = rowData["shipDate"];
				$("#shipViaSelectID option[value=" + aShipViaId + "]").attr("selected", true);
				$("#vendorInvoiceNum").val("");
				$("#vendorInvoiceNum").val(aVendorInvoice);
				$("#vendorDateID").val("");
				$("#vendorDateID").val(aVendorDate);
				$("#shipDateID").val("");
				$("#shipDateID").val(aShipDate);*/
			}
		});
 }catch(err){
	 alert(err.message);
 }
  }
 
 
 function addShipDetails(){
	 var aTodayDate = new Date();
	 var curr_date = aTodayDate.getDate();
	 var curr_month = aTodayDate.getMonth()+1;
	 var curr_year = aTodayDate.getFullYear();
	 
	 	var releasesGrid = $("#release");
		var selectedRelease = releasesGrid.jqGrid('getGridParam', 'selrow');
		var dueondaysforrxMasterid =releasesGrid.jqGrid('getCell', selectedRelease, 'rxMasterId'); 
	 
	 if(jQuery("#shiping").getGridParam("records") === 0){
		 var errorText = "Invoice not found, do you wish to create a new vendor invoice for this release?";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					"Yes": function(){
						jQuery(this).dialog("close");
						 $("#vendorDateID").val("");
						 $("#vendorDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
						 //document.getElementById('datedID').disabled = false;
						 $("#datedID").val("");
						// $("#datedID").val(curr_month + "/" + curr_date + "/" + curr_year);
						getDueonDayswithDate(dueondaysforrxMasterid);
						 $("#dueDateID").val("");
						 $("#shipDateID").val("");
						 $("#shipDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
						 $("#postDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
						 $('#openvendorinvoice').dialog('option', 'title', '');
						 $('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
						 jQuery("#openvendorinvoice").dialog("open");
					},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
		}}).dialog("open");
	 }else{
		 $("#vendorDateID").val("");
		 $("#vendorDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
		 //document.getElementById('datedID').disabled = false;
		 $("#datedID").val("");
		// $("#datedID").val(curr_month + "/" + curr_date + "/" + curr_year);
		getDueonDayswithDate(dueondaysforrxMasterid);
		 $("#dueDateID").val("");
		 $("#shipDateID").val("");
		 $("#shipDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
		 $("#postDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
		 $('#openvendorinvoice').dialog('option', 'title', '');
		 $('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
		 jQuery("#openvendorinvoice").dialog("open");
	 }
	 return true;
 }
 
 function deleteShipDetails(){
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Shipping Record?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					var aVeShipViaID = grid.jqGrid('getCell', rowId, 'veShipViaID');
					var aJoReleaseDetailsID = grid.jqGrid('getCell', rowId, 'joReleaseDetailID');
					var aCuInvoiceID = grid.jqGrid('getCell', rowId, 'cuInvoiceID');
					var aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');
					deleteShip(aVeShipViaID, aJoReleaseDetailsID, aVeBillID, aCuInvoiceID);
					setTimeout(function(){
						$("#release").trigger("reloadGrid");					
						},200);
					
					jQuery(this).dialog("close");
					
				},
				Cancel: function ()	{jQuery(this).dialog("close");}
				}}).dialog("open");
			return true;
		}else{
			var errorText = "Please click one of the Shipping record to Delete.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
	
 }
 
 function deleteShip(aVeShipViaID, aJoReleaseDetailsID, aVeBillID, aCuInvoiceID){
	
	 $.ajax({
		url: "./jobtabs5/deleteShipDetails",
		type: "GET",
		data : {"shipDetailsID" : aVeShipViaID, "releaseID" : aJoReleaseDetailsID , "billID" : aVeBillID, 'cuInvoiceID' : aCuInvoiceID },
		success: function(data) {
			var errorText = "Shipping record deleted successfully.";
			 $("#shiping").trigger("reloadGrid");
			 var validateMsg = "<b style='color:Green; align:right;'>"+errorText+"</b>";	
				$("#shipperMsg").css("display", "block");
				$('#shipperMsg').html(validateMsg);
				setTimeout(function(){
				$('#shipperMsg').html("");						
				},2000);
		}
	});
	
 }
 
 function changeBillingDetails(){
	var billAmount = $("#billing").is(':checked');
	if(billAmount === false){
		billAmountCalUnchk();
	}else{
		billAmountCal();
	}
 }

 function billAmountCal(){
	 $('#release').jqGrid('getGridParam', 'userData');
	var allRowsInGrid = $('#release').jqGrid('getRowData');
	var allocateamount=$("#allocate").text();
	var unAllocatedAmount = $("#unAllocated").text().replace(/[^0-9\.-]+/g,"");
	var aVal = new Array();
	var sum = 0;
	$.each(allRowsInGrid, function(index, value) { 
		aVal[index] = value.estimatedBilling;
		sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.-]+/g,""));
	});
	if(typeof estimatedamountRelease != 'undefined' && estimatedamountRelease !== '' &&  estimatedamountRelease!== null){
		estimatedamountRelease=estimatedamountRelease.replace(/[^0-9\.-]+/g,"");
		if(estimatedamountRelease=='' || estimatedamountRelease==null){
			estimatedamountRelease = 0;
		}
		$("#estimated").text(formatCurrency(estimatedamountRelease));
		$('#allocate').empty();
		if(sum===null || sum===''){
			sum=0;
		}
		$("#allocate").text(formatCurrency(sum));
		$('#unAllocated').text(formatCurrency(estimatedamountRelease-sum));
	}
	var sumTotal = '';
	var allocatedamount=allocateamount.replace(/[^0-9\.-]+/g,"");
	if(unAllocatedAmount > allocatedamount){
		sumTotal = Number(allocatedamount) - Number(unAllocatedAmount);
	}if(allocatedamount > unAllocatedAmount){
		sumTotal = Number(unAllocatedAmount) - Number(allocatedamount);
	}
//	$("#estimate").empty();
//	$("#estimate").text(formatCurrency(sumTotal));
 }
  
 function changeordclosebillamount(estimatedamount){
	try {
		var allocatext = $("#allocate").text();
		/* --Start-- Added By jenith on 09/09/2015*/
		$('#release').jqGrid('getGridParam', 'userData');
		var allRowsInGrid = $('#release').jqGrid('getRowData');
		var aVal = new Array();
		var sum = 0;
		$.each(allRowsInGrid, function(index, value) { 
			aVal[index] = value.estimatedBilling;
			sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.-]+/g,""));
		});
		/* --End-- Added By jenith on 09/09/2015*/
		if (typeof estimatedamount != 'undefined' && estimatedamount !== ''
				&& estimatedamount !== null) {
			allocatext = allocatext.replace(/[^0-9\.-]+/g, "");
			
			if(allocatext=='0.00'){
				allocatext = sum;
			}
			$("#estimate").text(formatCurrency(estimatedamount));
			$('#allocate').empty();
			if(allocatext==''||allocatext ==null){
				allocatext = 0;
			}
			$("#allocate").text(formatCurrency(allocatext));
			var unallocated=Number(estimatedamount) - Number(allocatext);
			$('#unAllocated').empty();
			if(unallocated==null || unallocated==''){
				unallocated=0;
			}
			$('#unAllocated').append(formatCurrency(unallocated));
		}
	} catch (e) {
		// TODO: handle exception
		alert(e.message);
	}
	
 }
 function billAmountCalUnchk(){
	 $('#release').jqGrid('getGridParam', 'userData');
	var allRowsInGrid = $('#release').jqGrid('getRowData');
	var aVal = new Array();
	var sum = 0;
	$.each(allRowsInGrid, function(index, value) { 
		aVal[index] = value.estimatedBilling;
		sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.-]+/g,""));
	});
	var billAmount = $("#estimatedCost").val();
	if(billAmount <sum ){
		jQuery(newDialogDiv).html('<span><b style="color:Green;">Allocated billing exceeds contract amount.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 	$('#estimated').empty();	//$("#unAllocated").empty();
								$('#allocate').empty();
								$("#estimated").text(formatCurrency(sum)); 
								if(sum=='' || sum==null){
									sum=0;
								}
								$("#allocate").text(formatCurrency(sum)); }}]}).dialog("open");
		return false;
	}else{
		$('#estimated').text(formatCurrency(0));	
		$('#allocate').text(formatCurrency(0));
		//$("#unAllocated").empty();
		/*var unAllocated = Number(billAmount) - Number(sum);
		unAllocated =unAllocated.replace(/[^0-9\.]+/g,""); */
		var estimatedcost=$("#estimatedCost").val();
		estimatedcost=estimatedcost.replace(/[^0-9\.-]+/g,"");
		$("#estimated").text(formatCurrency(estimatedcost));
		if(sum=='' && sum==null){
			sum=0;
		}
		$("#allocate").text(formatCurrency(sum));
//		$("#unAllocated").text(formatCurrency(unAllocated));
	}
	return true;
 }
 
 /** formatter for release grid **/
function typeFormatter(cellvalue, options, rowObject) {
	if(cellvalue === null){
		return "";
	}else if(cellvalue === -1){
		return "";
	}else if(cellvalue === 1) {
		return "Drop Ship";
	} else if(cellvalue === 2) {
		return "Stock Order";
	} else if(cellvalue === 3) {
		return "Bill Only";
	} else if(cellvalue === 4) {
		return "Commission";
	} else if(cellvalue === 5) {
		return "Service";
	}
}
		
/** hide/remove column function **/
function isExistInArray(array, value) {
	if(jQuery.inArray(value, array) === -1){
		return false;
	} else {
		return true;
	}
}

/** remove column function **/
function removeA(arr){
    var what, a = arguments, L = a.length, ax;
    while(L> 1 && arr.length){
        what = a[--L];
        while((ax= arr.indexOf(what)) != -1){
            arr.splice(ax, 1);
        }
    }
    return arr;
}

/** Dialog box for Add/Edit Release **/
jQuery(function(){
	jQuery("#openReleaseDig").dialog({
		autoOpen:false,
		width:470,
		title:"Add/Edit Release",
		modal:true,
		buttons:{	},
		open: function(){
			$('#loadingopenReleaseDlg').css({"visibility": "hidden","z-Index":"1234","display":"none"});
		},
		closeOnEscape: false,
		close:function(){$('#openReleaseDigForm').validationEngine('hideAll'); return true;}	
	});
}); 

/*$("#ReleasesManuID").keypress(function() {
	$(function() { var cache = {}; var lastXhr='';
	$( "#ReleasesManuID" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; $("#ManufacturerId").val(id);},
		source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
		lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
			cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
			error: function (result) {
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	if($("ul.ui-autocomplete").is(":visible")){
	} else {
		$(".ui-autocomplete").show();
	}
	$(".ui-autocomplete").show();
});*/

/** add Function for Release **/
function addRelease(){
	var Allowcreditlimitradiobutton=false;
	var settingscheckedornot=getSysvariableStatusBasedOnVariableName("UseCustomersCreditLimitwhencreatingJobs");
	if(settingscheckedornot!=null && settingscheckedornot[0].valueLong==1){
		Allowcreditlimitradiobutton=true;
	}
	var check = chechjobcustomerisonhold();
	if(Allowcreditlimitradiobutton&&check){
		jQuery(newDialogDiv).html('<span><b>Customer is on Hold.You must take off Hold in order to proceed.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
								buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var isFinalWaiver=$("#isFinalWaiver").prop('checked');
	if(isFinalWaiver==undefined){
		isFinalWaiver=$("#credittabfinwaiverchck").val();
		if(isFinalWaiver=="true"){
			isFinalWaiver=true;
		}else{
			isFinalWaiver=false;
		}
	}
	if(!AllowreleasesifFinalWaiverchecked && isFinalWaiver){
		jQuery(newDialogDiv).html('<span><b>Final waiver has been issued,no additional releases can be created for this job<br> please see a Manager additional releases are required.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:340, height:180, title:"Information", 
								buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	
	
	Releasetypeselectboxrefresh();
	$('#ReleaseTable tr:nth-child(7)').show();
	$('#ManufacturerId').val('');
	$('#ReleasesManuID').val('');
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus.indexOf("Booked")>-1){ 
	var releaseCount = $("#release").getGridParam("reccount");
	if(releaseCount === 0) {
		var validNewRelease = validateNewRelease();
		if(!validNewRelease) {
			jQuery(newDialogDiv).html('<span><b style="color:red;">Credit approval required.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
     		}
    	}
	}
	else {
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	aReleaseDialogVar = "add";
	document.getElementById("openReleaseDigForm").reset();
	var aPOAmount = $("#poAmountID").is(':checked');
	var aInvoiceAmount = $("#invoiceAmountID").is(':checked');
	if(aPOAmount === true){
		$("#poAmountFieldID").show();
	}else{
		$("#poAmountFieldID").css("display", "none");
	}
	if(aInvoiceAmount === true){
		$("#invoiceAmountFieldID").show();
	}else{
		$("#invoiceAmountFieldID").css("display", "none");
	}
	 createtpusage('job-Releases Tab','Add Releases','Info','job-Releases Tab,Adding Releases,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	// 07/29/2013
	var date = new Date();
    var curr_date = date.getDate();
    var curr_month = date.getMonth() + 1; //Months are zero based
    var curr_year = date.getFullYear();
    var formattedDate = curr_month + "/" + curr_date + "/" + curr_year;
	$("#ReleasesID").val(formattedDate);
	var aCustomerPO = $("#customerpodetails").val();
	if($('#releasesTypeID').val() == 2){
		$('#ReleaseTable tr:nth-child(7)').hide();
	}
	else
	{
		$('#ReleaseTable tr:nth-child(7)').show();
	}
	if(aCustomerPO !== ''){
		//$("#customerPONumberSelectID option[value=-1]").attr("selected", true);
		$("#customerPONumberSelectID").val(" - Select - ");
	}else{
		//$("#customerPONumberSelectID option[value=-1]").attr("selected", true);
		$("#customerPONumberSelectID").val(" - Select - ");
	}
	
	jQuery("#openReleaseDig").dialog("open");
	return true;
}
function loadBillingEntireJob(){
	var aJobNumber = $("#jobNumber_ID").text();
	var getestimatedamountdata = "&jobNumber="+aJobNumber;
	$.ajax({
		url: "./jobtabs5/getestimatedamount",
		type: "POST",
		data : {"jobNumber": aJobNumber,"joMasterID": $("#joMaster_ID").text()},
		success: function(result) {
			changeordclosebillamount(result);
		}
   });
	
}
function validateNewRelease(){
	var isValidToAddRelease = false;
			var aJoMasterID = $("#joMaster_ID").text();
			var aJobNumber = $("#jobNumber_ID").text();
			var releaseValidateQryStr = "&joMasterID="+aJoMasterID+"&jobNumber="+aJobNumber;
			$.ajax({
				async:false,
				url: "./jobtabs5/validatefirstrelease",
				type: "GET",
				data: releaseValidateQryStr,
				success: function(result) {
					isValidToAddRelease = result;
					return true;
				}
		   });
	return isValidToAddRelease;
}
/** Auto suggest for Manufacturer list **/
$(function() { var cache = {}; var lastXhr='';
$( "#ReleasesManuID" ).autocomplete({ minLength: 2, select: function( event, ui ) { var id = ui.item.id; $("#ManufacturerId").val(id);},
	source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
	lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
		cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
		error: function (result) {
			$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

/** Save Function for Release **/
function saveRelease(){
	var release_cuSOID="";
	var checklineitemvalidation = false;
	if(!$('#openReleaseDigForm').validationEngine('validate')) {
		return false;
	}
	var aReleaseValues = $("#openReleaseDigForm").serialize();
	if(aReleaseDialogVar == "edit")
	{
		var releasesGrids = $("#release");
			var selectedReleaserow = releasesGrids.jqGrid('getGridParam', 'selrow');
			var getrelesetype =releasesGrids.jqGrid('getCell', selectedReleaserow, 'type');
			var release_vePoId =releasesGrids.jqGrid('getCell', selectedReleaserow, 'vePoId');
			 release_cuSOID =releasesGrids.jqGrid('getCell', selectedReleaserow, 'cuSOID');
		if(getrelesetype == "Drop Ship" && $('#releasesTypeID').val()!= 1) {
			checklineitemvalidation = checklineitemisthere(1,release_vePoId);
		} else if(getrelesetype == "Stock Order" && $('#releasesTypeID').val()!= 2) {
			checklineitemvalidation = checklineitemisthere(2,release_cuSOID);
		} else if(getrelesetype == "Commission" && $('#releasesTypeID').val()!= 4) {
			checklineitemvalidation = checklineitemisthere(4,release_vePoId);
		} else if(getrelesetype == "Service" && $('#releasesTypeID').val()!= 5 ) {
			checklineitemvalidation = checklineitemisthere(5,release_cuSOID);
		}	
	}
	
	console.log("Release Tab--->"+aReleaseValues);
	var aJoMasterID = $("#joMaster_ID").text();
	var aJobNumberBefore = $("#jobNumber_ID").text();
	var aRxMasterID = $("#rxCustomer_ID").text();
	var manufacturerId = $("#ManufacturerId").val();
	var releaseDate = $('#ReleasesID').val();
	var releaseType = $('#releasesTypeID').val();
	/**Dev: Leo   Date: 04/02/2015 
	 * BugID: 278
	 * Changes: releaseType ==1
	 * Description: Billonly,Service,Commission getting alert message*/
	 if(releaseType==-1)
	{
		$("#message1").html('<span style="color:red;">Alert: Please select release type.</span>');
		$("#message1").show().delay(4000).fadeOut();
		return false; 
	}
	else if (releaseType==1 && manufacturerId === ""){
		$("#message1").html('<span style="color:red;">Alert: Please provide a valid Manufacturer.</span>');
		$("#message1").show().delay(4000).fadeOut();
		return false;
	}

	
	var aJobNumber = aJobNumberBefore;
	
	/**Dev: Leo   Date: 04/02/2015 
	 * BugID: 273
	 * Description: customer PO number doesn't allow special characters*/
	
	//alert(PurchaseServiceImpl);
	
	var custPONumber = $("#customerPONumberSelectID option:selected").text();
	var start = custPONumber.indexOf("(");
	if(custPONumber.indexOf("(")!=-1)
		custPONumber  = custPONumber .substring(0, start );
	
	if(custPONumber.indexOf("elect") > -1)
		custPONumber = "";
	
	//alert(custPONumber)
	boolean = false;
	
	var aReleaseFormValues = aReleaseValues+"&joMasterID="+aJoMasterID+"&oper=" +aReleaseDialogVar+"&jobNumber="+aJobNumber+"&rxMasterID="+aRxMasterID+"&custPONumber="+encodeURIComponent(custPONumber)+"&cuSOID="+release_cuSOID;
	console.log('AddRelease');
	console.log("Add/Edit Release popup values==>"+aReleaseFormValues);
	$('#loadingopenReleaseDlg').css({"visibility": "visible","z-Index":"1234","display":"block"});
	if(!checklineitemvalidation)
	{
	$.ajax({
		url: "./jobtabs5/addRelease",
		type: "POST",
		async:false,
		data : aReleaseFormValues,
		success: function(data) {
			createtpusage('job-Release Tab','Save Release','Info','Job,Release Tab,Saving Release,JobNumber:'+aJobNumber);
			$('#openReleaseDigForm').validationEngine('hideAll');
			jQuery("#openReleaseDig").dialog("close");
			$('#loadingopenReleaseDlg').css({"visibility": "hidden","z-Index":"1234","display":"none"});
			$("#release").trigger("reloadGrid");
			if(aReleaseDialogVar === "add"){ 
				/*<div id="releaseMessage" class="warningMsg"></div>*/
				/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Releases Details Added Sucessfully.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");*/
				$("#releaseMessage").html('<span style="color:green;">Success: Release Details Added Sucessfully</span>');
				$("#releaseMessage").show().delay(5000).fadeOut();
				var gridRows = $('#release').getRowData();
				var rowid = parseInt(gridRows.length) +1;
				boolean = true;
			}else{ 
				/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Releases Details Updated Sucessfully.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");*/
				$("#releaseMessage").html('<span style="color:green;">Success: Release Details Updated Sucessfully</span>');
				$("#releaseMessage").show().delay(5000).fadeOut();
				boolean = false;
			}
		}
   });
	
	}
	else
	{
		$('#openReleaseDigForm').validationEngine('hideAll');
		jQuery("#openReleaseDig").dialog("close");
		$('#loadingopenReleaseDlg').css({"visibility": "hidden","z-Index":"1234","display":"none"});
		
		errorText = "Dependency exists. You cannot change the release.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	}
	return true;
}

/** Check Validations */

function checklineitemisthere(type,valueID)
{
	var res;
	$.ajax({
		url: "./jobtabs5/Checklineitemvalidation",
		type: "POST",
		async:false,
		data : {"type":type,"valeID":valueID},
		success: function(data) {
			res = data;
			//alert(res);
		},
	});
	return res;	
}


/** Edit Function for Release **/
function editRelease(){
	createtpusage('job-Releases Tab','Edit Releases','Info','job-Releases Tab,Editing Releases,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus==''){
		jobStatus='Booked';
	}
	if(jobStatus.indexOf("Booked")>-1){
	aReleaseDialogVar = "edit";
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select the Release from the Grid to Edit.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		}
	}
	else{
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
//	loadCustomerPONumber();
	var aVepoID = grid.jqGrid('getCell', rowId, 'vePoId');
	var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
	var aJoReleasedDate = grid.jqGrid('getCell', rowId, 'released');
	var aJoReleaseType = grid.jqGrid('getCell', rowId, 'type');
	if(aJoReleaseType === "Drop Ship") {
		aJoReleaseType = 1;
	} else if(aJoReleaseType ===  "Stock Order") {
		aJoReleaseType = 2;
	} else if(aJoReleaseType === "Bill Only") {
		aJoReleaseType = 3;
	} else if(aJoReleaseType === "Commission") {
		aJoReleaseType = 4;
	} else if(aJoReleaseType === "Service") {
		aJoReleaseType = 5;
	}
	if($('#releasesTypeID').val() == 2||$('#releasesTypeID').val() == 5||$('#releasesTypeID').val() == 3){
		$('#ReleaseTable tr:nth-child(7)').hide();
	}
	else
	{
		$('#ReleaseTable tr:nth-child(7)').show();
	}
	var aManufacturerId = grid.jqGrid('getCell',rowId,'rxMasterId');
	var aFactoryId = grid.jqGrid('getCell',rowId,'manufacturerId');
	var aManufacturer = grid.jqGrid('getCell', rowId, 'manufacturer');
	var aReleaseNote = grid.jqGrid('getCell', rowId, 'note');
	var aEstimateAllocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
	var aJoReleaseDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
	var aReleasePoAmount = grid.jqGrid('getCell', rowId, 'po');
	var aReleaseInvoiceAmount = grid.jqGrid('getCell', rowId, 'invoiceAmount');
	//var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
	var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
	var costAllocated = aEstimateAllocated.replace(/[^0-9\.-]+/g,"");
	costAllocated = Number(costAllocated);
	var aPOAmount = $("#poAmountID").is(':checked');
	var aInvoiceAmount = $("#invoiceAmountID").is(':checked');
	if(aPOAmount === true){
		$("#poAmountFieldID").show();
		var costPoAmount = aReleasePoAmount.replace(/[^0-9\.-]+/g,"");
		costPoAmount = Number(costPoAmount);
		$("#POAmountID").val(costPoAmount);
		
	}else{
		$("#poAmountFieldID").css("display", "none");
	}
	if(aInvoiceAmount === true){
		$("#invoiceAmountFieldID").show();
		var costInvoiceAmount = aReleaseInvoiceAmount.replace(/[^0-9\.-]+/g,"");
		costInvoiceAmount = Number(costInvoiceAmount);
		$("#ReleasesInvoiceID").val(costInvoiceAmount);
	}else{
		$("#invoiceAmountFieldID").css("display", "none");
	}
	$("#vePoId").val(aVepoID);
	$("#ReleasesID").val(aJoReleasedDate);
	$("#ReleasesTypeID").val(aJoReleaseType);
	$("#ManufacturerId").val(aManufacturerId);
	$("#ReleasesManuID").val(aManufacturer);
	$("#NoteID").val(aReleaseNote);
	$("#AllocatedID").val(costAllocated);
	$("#joReleaseDetailId").val(aJoReleaseDetailId);
	$("#joReleaseId").val(aJoReleaseId);
	$("#veFactoryId").val(aFactoryId);
	//alert(aCustomerPONumber);
	
//	$("#customerPONumberSelectID option[value=" + aCustomerPONumber + "]").attr("selected", true);
	
//	alert(aCustomerPONumber);
	$("#customerPONumberSelectID").val(aCustomerPONumber+"()");
	jQuery("#openReleaseDig").dialog("open");
	return true;
}

/** Delete Function for Release **/
function deleteRelease(){ 
	
	
	jQuery(newDialogDiv).attr("id","msgDlg");
	jQuery(newDialogDiv).html('<span><b style="color:red;">'+"You Can't  Delete the Release"+'</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Warning",
		buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	/*aReleaseDialogVar = "del";
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var newDialogDiv = jQuery(document.createElement('div'));
	if(rowId === null){
		var errorText = "Please select the Release from the Grid to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	jQuery(newDialogDiv).html('<span><b style="color: red;">Do You Want to Delete the Release Record?</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Confirm Delete", 
		buttons:{
			"Delete": function(){
				var grid = $("#release");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var aVepoID = grid.jqGrid('getCell', rowId, 'vePoId');
				var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
				var aJoReleaseDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
				var cuSOID =	$('#Cuso_ID').text();
				var aReleaseFormValues = "&vePoName="+aVepoID+"&joReleaseName=" +aJoReleaseId+"&joReleaseDetailName=" +aJoReleaseDetailId+"&oper=" +aReleaseDialogVar+"&cuSOID="+cuSOID;
				jQuery(this).dialog("close");
				$.ajax({
					url: "./jobtabs5/addRelease",
					type: "POST",
					data : aReleaseFormValues,
					success: function(data) {
						createtpusage('job-Releases Tab','Delete Releases','Info','job-Releases Tab,Deleting Releases,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val()+',JoReleaseId:'+aJoReleaseId);
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Releases Details Delete Sucessfully.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
						$("#releaseMessage").html('<span style="color:green;">Success: Releases Details Deleted Sucessfully</span>');
						$("#releaseMessage").show().delay(5000).fadeOut();
						$("#release").trigger("reloadGrid");
					}
				});
			},
			Cancel : function ()	{
				jQuery(this).dialog("close");} }
	}).dialog("open");
	return true;*/
}

/** Cancel Function for Release **/
function cancelRelease(){
	createtpusage('job-Release Tab','Cancel Release','Info','Job-Release Tab,Cancel Release,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	$('#openReleaseDigForm').validationEngine('hideAll');
	jQuery("#openReleaseDig").dialog("close");
	return true;
}

/** Release tab Save **/
function release_save(){
	var releaseNotes = $("#releaseNote").val();
	var aJoMasterID = $("#joMaster_ID").text();
	var description = getUrlVars()["jobName"];
	var job_Number = getUrlVars()["jobNumber"];
	var bidDate = $("#bidDate_Date").val();
	var noticeId=$("#noticeId").val();
	var contactId=$("#contactId_Release").val();
	var notice=$("#notice").val();
	var noticeName=$("#noticeNameID").val();
	//var PONumberID=$("#PONumberID").text();
	var PONumberID="";
	//if(typeof(noticeName) !== null && noticeName !=='')
	if (document.getElementById('noticeContactID').checked)
	{
		contactId='-1';
	}
	
	var otherContact=0;
	if (document.getElementById('noticeContactID').checked){
		otherContact=1;
	}else{
		otherContact=0;
	}
	console.log("NoticeName:"+aJoMasterID+"&releaseNote=" +releaseNotes+"&jobName=" +description+"&jobNumber=" +job_Number+"&joBidDate=" +bidDate
		+"&noticeId="+noticeId+"&contactId="+contactId+"&notice="+notice+"&noticeName="+noticeName+"&otherContact="+otherContact);
	$.ajax({
		url: "./jobtabs5/updateReleaseNote",
		type: "POST",
		data : "&joMasterID="+aJoMasterID+"&releaseNote=" +releaseNotes+"&jobName=" +description+"&jobNumber=" +job_Number+"&joBidDate=" +bidDate
		+"&noticeId="+noticeId+"&contactId="+contactId+"&notice="+notice+"&noticeName="+noticeName+"&otherContact="+otherContact+"&PONumberID="+PONumberID,
		success: function(data) {
			var errorText = "Release Details Successfully Updated.";
			var validateMsg = "<b style='color:Green; align:right;'>"+errorText+"</b>";	
			$("#saveReleaseMsg").css("display", "block");
			$('#saveReleaseMsg').html(validateMsg);
			setTimeout(function(){
			$('#saveReleaseMsg').html("");						
			},2000);
			return false;
		}
   });
}

/** Bill Note Dialog Box **/
function openBillDialog(){
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select the Release from the Grid to Add/Edit Bill Note.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var billNotes = grid.jqGrid('getCell', rowId, 'billNote');
		if(billNotes==null){
			billNotes="";
		}	
		$("#billNote").val(billNotes);
		
		//$('#openBillNoteDialog').find(".nicEdit-main").html(billNotes);
		CKEDITOR.instances['billNote'].setData(billNotes);
		jQuery("#openBillNoteDialog").dialog("open");
		return true;
	}
}

jQuery(function(){
	jQuery("#openBillNoteDialog").dialog({
		autoOpen:false,
		width:625,
		title:"Bill Note",
		modal:true,
		buttons:{	},
		close:function(){ return true;}	
	});
}); 


function saveBillNote(){
	//var abillNote = $("#billNote").val().trim();
/*	var abillNote=$('#openBillNoteDialog').find('.nicEdit-main').html();
	if($('#openBillNoteDialog').find('.nicEdit-main').text().trim() ==""){
		abillNote = "";
	}
	*/
	var abillNote=CKEDITOR.instances["billNote"].getData();
	
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
	var aJoMasterID = $("#joMaster_ID").text();
	var aJoReleasedDate = grid.jqGrid('getCell', rowId, 'released');
	var aJoReleaseType = grid.jqGrid('getCell', rowId, 'type');
	if(aJoReleaseType === 'Drop Ship')
		aJoReleaseType = 1;
	else if(aJoReleaseType === 'Stock Order')
		aJoReleaseType = 2;
	else if(aJoReleaseType === 'Bill Only')
		aJoReleaseType = 3;
	else if(aJoReleaseType === 'Commission')
		aJoReleaseType = 4;
	else if(aJoReleaseType === 'Service')
		aJoReleaseType = 5;
	var aReleaseNote = grid.jqGrid('getCell', rowId, 'note');
	var aEstimateAllocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
	var costAllocated = aEstimateAllocated.replace(/[^0-9\.-]+/g,"");
	var aCostAllocated = Number(costAllocated);
	/*alert($('.nicEdit-main').text().trim()+"{&}"+abillNote +"{&}"+$('#billNote').val()+"{Replace}"+abillNote.replace('&nbsp;','ED'));*/
	//http://localhost:8080/turbotracker/turbo/jobtabs5/billNote?&joReleaseId=66153&billNote=This%20is%20Test&joMasterID=38191&joReleaseDate=03/03/2014%20&joReleaseType=Drop%20Ship&ReleaseNote=fans&EstimatedBilling=2688
	//joReleaseId=66153&billNote=This%20is%20Test&joMasterID=38191&joReleaseDate=03/03/2014%20&joReleaseType=Drop%20Ship&ReleaseNote=fans&EstimatedBilling=2688
	$.ajax({
		url: "./jobtabs5/billNote",
		type: "GET",
		data :{"joReleaseId" : aJoReleaseId,"billNote":abillNote,"joMasterID":aJoMasterID,"joReleaseDate":aJoReleasedDate,
				"joReleaseType":aJoReleaseType,"ReleaseNote":aReleaseNote,"EstimatedBilling":aCostAllocated},
//		data : "&joReleaseId="+aJoReleaseId+"&billNote=" +abillNote+"&joMasterID=" +aJoMasterID+"&joReleaseDate=" +aJoReleasedDate+
//				"&joReleaseType=" +aJoReleaseType+"&ReleaseNote=" +aReleaseNote+"&EstimatedBilling=" +aCostAllocated,
		success: function(data) {
			createtpusage('job-Release Tab','Save Bill Note','Info','Job,Release Tab,Saving Bill Note,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()+'abillNote:'+abillNote); 
			jQuery("#openBillNoteDialog").dialog("close");
			$("#release").trigger("reloadGrid");
			var errorText = "Bill Note Successfully Updated.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
			
			return false;
		}
	});
}


function cancelBillNote(){
	createtpusage('job-Release Tab','Cancel Bill Note','Info','Job,Release Tab,Cancelling Bill Note,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()); 
	jQuery("#openBillNoteDialog").dialog("close");
}

function openReleaseTrack(){
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var awebSight= grid.jqGrid('getCell', rowId, 'webSight'); 
	var atrackingnumber= grid.jqGrid('getCell', rowId, 'trackingNumber'); 
	if(awebSight === null || awebSight === ''){
		var errorText = "Tracking Web Site is not there.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	}else{

		
		var $temp = $("<input>");
		  $("body").append($temp);
		  $temp.val(atrackingnumber).select();
		  document.execCommand("copy");
		  $temp.remove();

		 var webSpilt = awebSight.split(":");
		///var webPage= "http://"+webSight;
		if(webSpilt[0] === "http"){
			window.open(awebSight);
		}else{
			window.open("http://"+awebSight);
		}
	}
}

/** Edit Purchase Order in release tab**/
function editreleasedialog(rowID) {
	//alert('editreleasedialog');
	var aManufacture = $("#rxCustomer_ID").text();
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowID){
		rowId=rowID;
	}
	var isSalesOrder = grid.jqGrid('getCell',rowId,'type');
	var creditTypeID = false;
	//document.getElementById("sogeneralForm").reset();
	//$("#sogeneralForm")[0].reset();
	resetSOGeneralForm();
	
	$.ajax({
		url: "./jobtabs3/getHoldCredit",
		type: "GET",
		data : {"customerID" : aManufacture},
		success: function(data) {
			creditTypeID = data.creditHold;
		}
	});
	
	
	
	//console.log("oooo", $('.ui-tabs-panel:not(.ui-tabs-hide)').index());
	var jobStatus = getUrlVars()["jobStatus"];
/*	if(jobStatus === "Closed" || jobStatus === "Bid" || jobStatus === "Planning" || jobStatus === "Budget" || jobStatus === "Quote" ||
			jobStatus === "Submitted" ||  jobStatus === "Closed" || jobStatus === "Lost" ||
			jobStatus === "Abandoned" || jobStatus === "Rejected" || jobStatus === "Over Budget"){*/
	if(jobStatus === "Bid" || jobStatus === "Planning" || jobStatus === "Budget" || jobStatus === "Quote" ||
			jobStatus === "Submitted" || jobStatus === "Lost" ||
			jobStatus === "Abandoned" || jobStatus === "Rejected" || jobStatus === "Over Budget"){
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		
	}else if(jobStatus.indexOf("Booked")>-1 || jobStatus.indexOf("Closed")>-1){
		if(creditTypeID === true){
			errorText = "This job is on Pending.  Approval required.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); var checkpermission=getGrantpermissionprivilage('Customers',0);
					    		if(checkpermission){document.location.href = "./customerdetails?rolodexNumber="+$("#rxCustomer_ID").text()+"&name="+'`'+$("#jobCustomerName_ID").text()+'`&creditHold=false';} }}]}).dialog("open");
			return false;
		} else if(rowId === null){
			errorText = "Please click one of the Order to Edit Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		} else {
		//alert('else---');
			var manufacturer = grid.jqGrid('getCell', rowId, 'rxMasterId');
			var manufacturerName =  grid.jqGrid('getCell', rowId, 'manufacturer');
			var vePoId = grid.jqGrid('getCell', rowId, 'vePoId');
			var addressID =  grid.jqGrid('getCell', rowId, 'rxAddressID');
			var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
			var rxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterId');
			var date = grid.jqGrid('getCell', rowId, 'released');
			var joReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
			var ponumAlphabet = grid.jqGrid('getCell', rowId, 'ponumber');
			$("#poDate_ID").text(date);
			$("#dateOfcustomerGeneral").val(date);
			$('#order_ID').text(rxMasterID);
			var cusoid=$('#Cuso_ID').text();
			var x = $('#rxCustomer_ID').text();
			$('#rxmasterId').text(x);
/*			$.ajax({ 
				url: "./jobtabs5/getAddressAddressInformation",
				mType: "GET",
				data : { 'rxAddressID' : addressID },
				success: function(data){
					$("#vendorAddress1").val(data.address1); $("#vendorAddress2").val(data.address2); $("#vendorAddressCity").val(data.city);
					$("#vendorAddressState").val(data.state); $("#vendorAddressZip").val(data.zip);
				}
			});
			
*/			
			if(isSalesOrder!=='Stock Order' && isSalesOrder!=='Service' && isSalesOrder!=='Bill Only'){
				console.log('Release not having Vepo Entry::'+vePoId);
				if ((vePoId === null || vePoId ==='') || vePoId === 'undefined') {
					console.log('Release not having Vepo Entry');
					editRelease();
					//loadDropshipRelease(joReleaseId,isSalesOrder);
				}else{
					loadPORelease(manufacturer,vePoId,manufacturerName, aCustomerPONumber, isSalesOrder);
				}
			} 
			else if(isSalesOrder == 'Commission'){
				loadPORelease(manufacturer,vePoId,manufacturerName, aCustomerPONumber, isSalesOrder);
			}
			else if(isSalesOrder=='Bill Only'){
				chkSplitCommissionBOValidation();
				boolean = false;
				var validationText = "Would you like to add split commission to this release?";
				if(commissionBORequired==1){
					validationText = "You must add split commission for this release. Do you want to continue?"
				}
				errorText = "Sales order not needed for bill only releases, just create the invoice. <br><br> "+validationText;
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:310, height:200, title:"Information", 
				buttons: [{height:35,text: "Yes",click: function() {
					$(this).dialog("close"); 
					calculateSplit(joReleaseId);
					addempinsplitcomrelease(joReleaseId,ponumAlphabet);
					}},{height:35,text: "No",click: function() { 
						$(this).dialog("close"); 
					}}]}).dialog("open");
				return false;
			}else{
				$('#splitDivs').show();
				//$("#splitDivs :input").prop("disabled", true);
				$("#splitDivs").children().removeAttr("disabled");
				$("#OriginalData").children().attr("disabled","disabled");
				$('#OriginalData').hide();
				
				console.log('Test jobWizardRelease.js in Stock Order');
				//var rowDatas = jQuery(this).getRowData(rowID);
				//var cuSOID = rowDatas["cuSOID"];
				if(isSalesOrder=='Service'){
					$("#salesrelease").dialog('option', 'title', 'Service Order');
				}else{
					$("#salesrelease").dialog('option', 'title', 'Sales Order');
				}
				var cuSOID = grid.jqGrid('getCell', rowId, 'cuSOID');
				$("#salesrelease").dialog("open");
				$("#lineItemGrid").trigger("reloadGrid");
				$("#Ack").trigger("reloadGrid");
				$('#saleslineitems').removeClass('ui-tabs-selected').removeClass('ui-state-active');
				$('#salesgeneral').addClass('ui-tabs-selected').addClass('ui-state-active');
				$('#ui-tabs-11').removeClass('ui-tabs-hide');
				$('#ui-tabs-12').addClass('ui-tabs-hide'); 
				
				console.log('Called from jobwizardRelease.js');
				var rowData = jQuery("#release").getRowData(rowId); 
				var acuSoid = rowData['cuSOID'];
				var aMasterID = rowData['rxMasterId'];
				$('#rxCustomer_ID').text(aMasterID);
				$('#Cuso_ID').text(acuSoid);
				$('#operation').val('update');
				loadEmailList(rxMasterID);
				PreloadSOGeneralData(cuSOID);
				invoicethereornotforsalesorder(cuSOID);
			}
		}
	 } 
	
	return true;
	 
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
	
}

function loadcusoID(joReleaseId){
	$.ajax({
		url: "./salesOrderController/getCuSOID",
		type: "POST",
		data : {"jobNumber" : joReleaseId},
		success: function(data) {
			$('#Cuso_ID').text(data);
		}
	});	
}
function disableoption(option){
	console.log(option);
	//$("#releasesTypeID").val(2);
	return true;
}

function sendPOEmail(poGeneralKey){
	var bidderGrid = $("#release");
	var aQuotePDF = "purchase";
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePoId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aContactID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxVendorContactID');
	var errorText = '';
	var cusotmerPONumber = $("#ourPoId").val();
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
	if(bidderGridRowId === null){
		errorText = "Please click one of the Order to Email Purchase Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aContactID === null && aContactID === '' && aContactID === '-1'){
		
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
				var aEmail;
				
				if($('#emailListCU').text() != null)
					aEmail=$("#emailListCU option:selected").text();
				else
					aEmail= data.email;
				var arxContact = aFirstname + ' '+aLastname;
				if(aEmail === ''){
					errorText = "Are you sure you want to send this PO to"+ arxContact +"?";
				}else{
					errorText = "Are you sure you want to send this PO to "+ arxContact +"("+ aEmail +")?";
				}
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
				buttons:{
					"Send": function(){
						$('#loadingPODiv').css({"visibility": "visible"});
						viewPOPDF(aQuotePDF);
						sentMailPOFunctionfromrelease(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId);
						jQuery(this).dialog("close");
					},
					Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
			}
		});
		}
	return true;
	}
}

function sentMailPOFunctionfromrelease(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId){
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
						url: "./sendMailServer/sendPOMail",
						mType: "POST",
						data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
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
								if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
								if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
								$.ajax({ 
									url: "./jobtabs3/updateEmailStampValue",
									mType: "GET",
									data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
									success: function(data){ $("#emailTimeStamp").empty(); 
									$("#emailTimeStamp").append(today); }
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
				url: "./sendMailServer/sendPOMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
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
		}else{
			$.ajax({ 
				url: "./sendMailServer/sendPOMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
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

function viewPOPDF(aPDFType){
	createtpusage('job-Release Tab','Print PDF','Info','Job-Release Tab,Viewing PDF,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	if($('#POtransactionStatus').val() == '-1'){
		errorText = "You can not View PDF, \nTransaction Status is 'Void' \nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","jen");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									}}]}).dialog("open");
	return false;
	}
 else if($('#POtransactionStatus').val() == '0'){
	 	errorText = "You can not View PDF, \nTransaction Status is 'Hold' \nChange Status to Open.";
	 	jQuery(newDialogDiv).attr("id","test");	
	 	jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									}}]}).dialog("open");
	return false;
 }
 else if($('#POtransactionStatus').val() == '2'){
	 	errorText = "You can not View PDF, \nTransaction Status is 'Close' \nChange Status to Open.";
	 	jQuery(newDialogDiv).attr("id","t2");
	 	jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									}}]}).dialog("open");
	return false;
 }
 else{
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var releaseType = bidderGrid.jqGrid('getCell',bidderGridRowId,'type');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var joMasterID= $("#joMaster_ID").text();
	var rxMasterID = $("#rxCustomer_ID").text();
	var cusoID =  $('#Cuso_ID').text();
	var check = document.getElementById("withPrice").checked;
	if(check){
		check='Checked';
	}else{
		check='NotChecked';
	}
	if(aPDFType === 'purchase'){
		
			$.ajax({
				type : "GET",
				url : "./purchasePDFController/viewPDFLineItemForm",
				data : { 'joMasterID':joMasterID,'vePOID' : vePOID, 'puchaseOrder' : aPDFType, 'jobNumber' :  jobNumber, 'rxMasterID' : rxMasterID, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :aInteger },
				documenttype: "application\pdf",
				async: false,
				cache: false,
				success : function (msg) {
				},
				error : function (msg) {}
			});
	} else if(releaseType==='Bill Only'){
		
		
	}else {
		if(bidderGridRowId === null){
			errorText = "Please click one of the Order to View Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		aPDFType = "Po";
		if (releaseType=='Drop Ship') {				/*Check wether the user print a Purchase order report.*/
			window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToModeId+"&releaseType=Drop Ship"+"&joMasterID="+joMasterID);
			return true;
		}
		else if(releaseType=='Commission'){
			window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToModeId+"&releaseType=Commission"+"&joMasterID="+joMasterID);
			return true;
		}
		else {									/* check wether the user prints a sales order report.*/
			if (cusoID == null || cusoID == 'undefined') {
				errorText = "Please select a sales order from release grid.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			window.open("./salesOrderController/printSalesOrderReport?cusoID="+cusoID+"&price="+check);
			return true;
		}
	}
	 
}
}


function viewPOPDFVoid(aPDFType){
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var releaseType = bidderGrid.jqGrid('getCell',bidderGridRowId,'type');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	var cusoID =  $('#Cuso_ID').text();
	var joMasterID= $("#joMaster_ID").text();
	if(aPDFType === 'purchase'){
		
			$.ajax({
				type : "GET",
				url : "./purchasePDFController/viewPDFLineItemFormVoid",
				data : { 'joMasterID':joMasterID,'vePOID' : vePOID, 'puchaseOrder' : aPDFType, 'jobNumber' :  jobNumber, 'rxMasterID' : rxMasterID, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :aInteger },
				documenttype: "application\pdf",
				async: false,
				cache: false,
				success : function (msg) {
				},
				error : function (msg) {}
			});
	} else {
		if(bidderGridRowId === null){
			errorText = "Please click one of the Order to View Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		aPDFType = "Po";
		if (releaseType=='Drop Ship') {				/*Check wether the user print a Purchase order report.*/
			window.open("./purchasePDFController/viewPDFLineItemFormVoid?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToModeId+"&joMasterID="+joMasterID);
			return true;
		} else {									/* check wether the user prints a sales order report.*/
			if (cusoID == null || cusoID == 'undefined') {
				errorText = "Please select a sales order from release grid.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			window.open("./salesOrderController/printSalesOrderReport?cusoID="+cusoID);
			return true;
		}
	}
}

/*function viewCuInvoicePDF(aPDFType){
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var releaseType = bidderGrid.jqGrid('getCell',bidderGridRowId,'type');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	var cusoID =  $('#Cuso_ID').text();
	
	if(aPDFType === 'purchase'){
		
			$.ajax({
				type : "GET",
				url : "./purchasePDFController/viewPDFLineItemForm",
				data : { 'vePOID' : vePOID, 'puchaseOrder' : aPDFType, 'jobNumber' :  jobNumber, 'rxMasterID' : rxMasterID, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :aInteger },
				documenttype: "application\pdf",
				async: false,
				cache: false,
				success : function (msg) {
				},
				error : function (msg) {}
			});
	} else {
		if(bidderGridRowId === null){
			errorText = "Please click one of the Order to View Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		aPDFType = "Po";
		if (releaseType=='Drop Ship') {				Check wether the user print a Purchase order report.
			window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToModeId);
			return true;
		} else {									 check wether the user prints a sales order report.
			if (cusoID == null || cusoID == 'undefined') {
				errorText = "Please select a sales order from release grid.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			window.open("./salesOrderController/printSalesOrderReport?cusoID="+cusoID);
			return true;
		}
	}
	 
}*/

function viewCuInvoicePDF(){
	var CuInvoice = $('#cuinvoiceIDhidden').val();
	if(CuInvoice != '' && CuInvoice != undefined){
		window.open("./salesOrderController/printinsidejobCuInvoiceReport?CuInvoice="+CuInvoice);
		$.ajax({ 
			url: "./jobtabs5/addInvoiceLog",
			type: "POST",
			data : {'cuInvoiceID' : CuInvoice, 'action' : 2},
			success: function(data){
				// action = 1 view action=2 pdf view action=3 mail sent
			}
		});
	}
	else
		{
		/*var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:Green;">Please Save Customer Invoice.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
								buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close");  }}]}).dialog("open");*/
		$('#showMessageCuInvoice').html("Please Save Customer Invoice.");
		$('#showMessageCuInvoiceLine').html("Please Save Customer Invoice.");
		
		setTimeout(function(){
			$('#showMessageCuInvoice').html("");
			$('#showMessageCuInvoiceLine').html("");
			},3000);
		}	
	
	return false;
}
                      /**-------------------------------          Vendor Invoice Functions              ------------------------------------------- **/

/** open dialog for Vendor Invoice **/

function setvendorinvoicetotal(){
	 var allocated1= $('#subtotal_ID').val().replace(/[^0-9-\.]+/g,"");
	 var frieght= $('#freight_ID').val().replace(/[^0-9-\.]+/g,"");
	 var taxValue = $('#tax_ID').val().replace(/[^0-9-\.]+/g,"");	
	 if(frieght==null || frieght=="" || frieght==undefined){
		 frieght=0.00;
	 }
	 if(allocated1==null || allocated1=="" || allocated1==undefined){
		 allocated1=0.00;
	 }
	 if(taxValue==null || taxValue=="" || taxValue==undefined){
		 taxValue=0.00;
	 }
	 var total=parseFloat(allocated1)+parseFloat(frieght)+parseFloat(taxValue);
	 $('#total_ID').val(formatCurrency(total));
	 $('#bal_ID').val(formatCurrency(total));
	 
}
function redirecttovieworaddvendorinvoice(){
	$("#datedID").val(currenDate);
	
	$("#vendorinvoice1").jqGrid('GridUnload');
	var release_grid = $("#release").jqGrid('getGridParam', 'selrow');
	var releasetype= $("#release").jqGrid('getCell', release_grid, 'type');
	
	if(releasetype != "Commission")
	{
		if(releasetype=='Bill Only'){
			jQuery(newDialogDiv).html('<span><b style="color:red;">Vendor Invoice is not applicable for Bill Only Release</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}else if(releasetype=='Stock Order'){
			jQuery(newDialogDiv).html('<span><b style="color:red;">Vendor Invoice is not applicable for Stock Order Release</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
			
		}else if(releasetype=='Service'){
			jQuery(newDialogDiv).html('<span><b style="color:red;">Vendor Invoice is not applicable for Service Order Release</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
			
		}
		$("#vendorinvoiceidbutton").css("display", "inline-block");
		$("#vendorinvoiceidclosebutton").css("display", "none");
		
		var shiping_grid_forreset = $("#shiping").jqGrid('getGridParam', 'selrow');
		var shiping_grid_cuInvoiceID= $("#shiping").jqGrid('getCell', shiping_grid_forreset, 'cuInvoiceID');
		var shiping_grid_veBillID= $("#shiping").jqGrid('getCell', shiping_grid_forreset, 'veBillID');
		
		if((shiping_grid_veBillID!="" && shiping_grid_veBillID!="0" && shiping_grid_veBillID!=null))
			$("#shiping").jqGrid("resetSelection");
		
		openvendorinvoicedialog();
	}
	else if(releasetype === "Commission")
	{
		openCommissionDialog('new');
	}
}

jQuery(function() {
	jQuery("#commissionDialogBox").dialog({
		autoOpen : false,
		modal : true,
		title : "Vendor Commission Details",
		width : 520,
		left : 300,
		top : 290,
		buttons : {},
		close : function() {
			$('#commissionDialogForm').validationEngine('hideAll');
			return true;
		}
	});
});

$("#expCommissionID").keyup(function(e) {
	
	var rowId = $("#release").jqGrid('getGridParam', 'selrow');
	var id = $("#release").jqGrid('getCell', rowId, 'joReleaseId');
	
	var commissionData = "releaseId="+id+"&expectedCommission="+$(this).val();
	if (e.keyCode != 46) {
//	alert(rowId+" || "+commissionData);
	$.ajax({
		url: "./jobtabs5/saveCommissionAmount",
		type: "POST",
		async:false,
		data : commissionData,
		success: function(data){
			//alert(data);
		}
   });
	var CommissionAmount = $(this).val();
	var balanceAmount = 0.00;
	var receivedAmount = 0.00;
	var sum=0.00;
	 /*$.ajax({
			url: "./jobtabs5/getBilledUnbilledAmount",
			type: "POST",
			async:false,
			data : {"joReleaseId" : id},
			success: function(data) {
				sum = data.billed;
				console.log('Received Amount:: '+sum);
			}
		});*/
	
	
	 $("#commissionReleaseGrid").setSelection(1, true);
	 var allRowsInCommissionGrid = $('#commissionReleaseGrid').jqGrid('getRowData');
		var aVal = new Array(); 
		$.each(allRowsInCommissionGrid, function(index, value) {
			aVal[index] = value.customerAmount;
			var number1 = aVal[index];
			var number2 = number1.replace(".00", "");
			var number3 = number2.replace("$", "");
			var number4 = number3.replace(",", "");
			var number5 = number4.replace(",", "");
			var number6 = number5.replace(",", "");
			sum = parseFloat(sum) + parseFloat(number6); 
		});
	
	 
	 /*var rowid=$("#commissionReleaseGrid").jqGrid('getGridParam', 'selrow');
		if(rowid!=null&& rowid!="null"){
			sum=$("#commissionReleaseGrid").jqGrid('getCell', rowid, 'customerAmount');
		}
		*/
		if(sum==undefined && sum =='NaN'){
			sum=0.00;
		}
	
	receivedAmount = parseFloat(sum);
	balanceAmount = CommissionAmount-receivedAmount;
	var number1 = CommissionAmount.replace("$", "");
	var number2 = number1.replace(".00", "");
	$("#expCommissionID").val(number2);
	$("#CommissionReceivedAmount").text(formatCurrency(receivedAmount));
	$("#commissionBalanceAmount").text(formatCurrency(balanceAmount));
	}
	
});

function getCommissionAmount(){
	
	var rowId = $("#release").jqGrid('getGridParam', 'selrow');
	var id = $("#release").jqGrid('getCell', rowId, 'joReleaseId');
	var CommissionAmount = 0.00;
	var balanceAmount = 0.00;
	var receivedAmount =0.00;
	var commissionReceived= false;
	var commissionData = "releaseId="+id;
	var sum =0.00;
	$.ajax({
		url: "./jobtabs5/getCommissionAmount",
		type: "POST",
		async:false,
		data : commissionData,
		success: function(data){
			CommissionAmount = data.commissionAmount;
			commissionReceived = data.commissionReceived;
		}
   });
	if (typeof(CommissionAmount) == "undefined" && CommissionAmount == null){
		CommissionAmount=0.00;
	}
/*	 $.ajax({
			url: "./jobtabs5/getBilledUnbilledAmount",
			type: "POST",
			async:false,
			data : {"joReleaseId" : id},
			success: function(data) {
				sum = data.billed;
				console.log('Received Amount:: '+sum);
			}
		});*/

	$("#commissionReleaseGrid").setSelection(1, true);
	 var allRowsInCommissionGrid = $('#commissionReleaseGrid').jqGrid('getRowData');
		var aVal = new Array(); 
		$.each(allRowsInCommissionGrid, function(index, value) {
			aVal[index] = value.customerAmount;
			var number1 = aVal[index];
			var number2 = number1.replace(".00", "");
			var number3 = number2.replace("$", "");
			var number4 = number3.replace(",", "");
			var number5 = number4.replace(",", "");
			var number6 = number5.replace(",", "");
			sum = parseFloat(sum) + parseFloat(number6); 
		});
	/* var rowid=$("#commissionReleaseGrid").jqGrid('getGridParam', 'selrow');
		if(rowid!=null&& rowid!="null"){
			sum=$("#commissionReleaseGrid").jqGrid('getCell', rowid, 'customerAmount');
		}
	*/
	
	if(sum!=undefined && sum !=null){
		/*if(sum.indexOf('.') > -1){
			sum = sum.replace(".00", "");
		}
		if(sum.indexOf('$') > -1){
			sum = sum.replace(/[^0-9\.-]+/g,"");
		}*/
	}else{
		sum =0.00;
	}
	receivedAmount = parseFloat(sum);
	balanceAmount = CommissionAmount-receivedAmount;
	$("#expCommissionID").val(formatCurrency(CommissionAmount));
	$("#CommissionReceivedAmount").text(formatCurrency(receivedAmount));
	$("#commissionBalanceAmount").text(formatCurrency(balanceAmount));
	
	var allRowsInGrid = $('#commissionReleaseGrid').jqGrid('getRowData');
	var gridLength = allRowsInGrid.length;
	if(gridLength>0){
		if(balanceAmount==0 || commissionReceived==true){
			$("#commissionClosedID").prop("checked", true);
		}else{
			$("#commissionClosedID").prop("checked", false);
		}
	}
	else{
		if( commissionReceived==true){
			$("#commissionClosedID").prop("checked", true);
		}else{
			$("#commissionClosedID").prop("checked", false);
		}
	}
}

function editvendorinvoice(){
	
	var releasesGrid = $("#release");
	var selectedRelease = releasesGrid.jqGrid('getGridParam', 'selrow');
	var aVePOID = releasesGrid.jqGrid('getCell', selectedRelease, 'vePoId');
	var aJoReleaseDetailsID = releasesGrid.jqGrid('getCell', selectedRelease, 'joReleaseDetailid');
	var releaseTypes = releasesGrid.jqGrid('getCell', selectedRelease, 'type');
	if(releaseTypes=='Commission'){
		var comRowid=$("#commissionReleaseGrid").jqGrid('getGridParam', 'selrow');
		if(comRowid!=null&& comRowid!="null"){
			var commDetailID = $("#veCommDetailIdVal").val();
			loadCommissionInvoiceDetails(commDetailID);
			openCommissionDialog('edit');
		}else{
			jQuery(newDialogDiv).html('<span><b style="color:red;">For creating Vendor Invoice .Please Select a + icon.or View existing Invoice please select any one Invoice</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		}
		
	}
	else{
	var rowid=$("#shiping").jqGrid('getGridParam', 'selrow');
	if(rowid!=null&& rowid!="null"){
		var veBillID=$("#shiping").jqGrid('getCell', rowid, 'veBillID');
		if(veBillID!=0 && veBillID!="0" && veBillID>0){
			var invoiceType = 'existing';
			loadVendorInvoiceLineItems(veBillID, aJoReleaseDetailsID, aVePOID, invoiceType);
			/*$("#postDateID").val('');
			$("#postDateID").val(currenDate);
			$("#dueDateID").val('');
			$("#dueDateID").val(currenDate);
			$("#shipDateID").val('');
			$("#shipDateID").val(currenDate);
			$("#vendorDateID").val('');
			$("#vendorDateID").val(currenDate);*/
			$('#openvendorinvoice').dialog('option', 'title', '');
			$('#openvendorinvoice').dialog('option', 'title', 'View Vendor Invoice');
			
			var transStatus=$("#shiping").jqGrid('getCell', rowid, 'transactionStatus');
			
			if(transStatus == 2)
			{
				$("#jobpaidStatus").css("display","inline");
				$("#jobchk_nofmPO").text($("#shiping").jqGrid('getCell', rowid, 'vechkNo'));
				$("#jobdate_paidfmPO").text($("#shiping").jqGrid('getCell', rowid, 'vedatePaid'));
				
			$("#vendorinvoiceidbutton").css("display", "none");
			$(".ui-pg-button").css("visibility","hidden");
			
			}
			else
			{
				$("#jobpaidStatus").css("display","none");
			$("#vendorinvoiceidbutton").css("display", "inline-block");	
			}
			$("#vendorinvoiceidclosebutton").css("display", "inline-block");
			
			jQuery("#openvendorinvoice").dialog("open");
			//getVendorDetails(aJoReleaseDetailsID, aVePOID);
			loadvebilldetails(aJoReleaseDetailsID,veBillID);

		}else{
		
			jQuery(newDialogDiv).html('<span><b style="color:red;">There is no vendor invoice for selected record.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		}
	}else{
		jQuery(newDialogDiv).html('<span><b style="color:red;">For creating Vendor Invoice .Please Select a + icon.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	}
}
}
function loadvebilldetails(aJoReleaseDetailsID,veBillID){
	$.ajax({
		url: "./jobtabs5/getVeBillDetails",
		type: "POST",
		data : {'releaseDetailID' : aJoReleaseDetailsID, 'veBillID' : veBillID},
		success: function(data) {
			if(data !== ''){
				var postdate = new Date(data.postDate);
				var dueDate = new Date(data.dueDate);
				var shipDate = new Date(data.shipDate);
				var receiveDate = new Date(data.receiveDate);
				var billDate = new Date(data.billDate);
				
				/*var aPostDate = postdate.getUTCMonth()+1+"/"+ postdate.getUTCDate()+"/"+postdate.getUTCFullYear();
				var aDueDate = dueDate.getUTCMonth()+1+"/"+ dueDate.getUTCDate()+"/"+dueDate.getUTCFullYear();
				var aShipDate = shipDate.getUTCMonth()+1+"/"+ shipDate.getUTCDate()+"/"+shipDate.getUTCFullYear();
				var aReceiveDate = receiveDate.getUTCMonth()+1+"/"+ receiveDate.getUTCDate()+"/"+receiveDate.getUTCFullYear();*/
				
				var aPostDate = getFormattedDate(postdate);
				var aDueDate = getFormattedDate(dueDate);
				var aShipDate = getFormattedDate(shipDate);
				var aReceiveDate = getFormattedDate(receiveDate);
				var aBillDate =  getFormattedDate(billDate);
				$("#rxMasterID").val(data.rxMasterId);
				
				$.ajax({
				url: './jobtabs5/rxAddressInfo?rxMasterID='+data.rxMasterId,
		        type: 'GET',       
		        success: function (data) {
		        	$("#manufacurterNameID").val(data.name);
		        	$("#addressNameID").text(data.address1+"\n"+data.address2+"\n"+data.city+"\n"+data.state+data.zip);
		        }
				});
				
				//document.getElementById('datedID').disabled = true;
				$("#datedID").attr("readonly", "true"); 
				$("#datedID").val(aBillDate); 
				$("#postDateID").val('');
				$("#proNumberID").val(data.trackingNumber);
				$("#postDateID").val('');
				$("#postDateID").val(aPostDate);
				$("#dueDateID").val('');
				$("#dueDateID").val(aDueDate);
				$("#shipDateID").val('');
				$("#shipDateID").val(aShipDate);
				$("#vendorDateID").val('');
				$("#vendorDateID").val(aReceiveDate);
				invoiceFreightAmount = data.freightAmount;
				//$("#freight_ID").val(formatCurrency(data.freightAmount));
				$("#freight_ID").val(formatCurrency(data.freightAmount));
				var subtotal=Number(floorFigureoverall(data.billAmount,2))-Number(floorFigureoverall(data.taxAmount,2))-Number(floorFigureoverall(data.freightAmount,2));
				$("#subtotal_ID").val(formatCurrency(subtotal));
				$("#total_ID").val(formatCurrency(data.billAmount));
				$("#tax_ID").val(formatCurrency(data.taxAmount));
				$("#bal_ID").val(formatCurrency(data.billAmount));
				$("#vendorInvoiceNum").val(data.invoiceNumber);
				$("#veBill_ID").val(data.veBillId);
				$("#invreasonfmjob").val(data.reason);
				
				//data.apaccountId
				
				$("#coAccountID option[value=" + data.apaccountId + "]").attr("selected", true);
				$("#shipViaSelectID option[value=" + data.veShipViaId + "]").attr("selected", true);
//				alert("shipViaSelectID :: "+data.veShipViaId);
//				alert("shipViaSelectID :: 1"+shipViaInvoiceID);
				if(data.veShipViaId==null || data.veShipViaId=='null'){
				$("#shipViaSelectID option[value=" + shipViaInvoiceID + "]").attr("selected", true);
				}
				try{
					var grid = $("#shiping");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var aVendorDate = grid.jqGrid('getCell', rowId, 'vendorDate');
					if(aVendorDate==''){
						$("#shipViaSelectID option[value=" + shipViaInvoiceID + "]").attr("selected", true);
					}	
				}catch(err){
					
				}
				
				var aPostChk = data.usePostDate;
				if(aPostChk === true){
					$("#postDateChkID").prop("checked", true);
					$("#postDateID").show(); 
				}
			}
			//setvendorinvoicetotal();
			_globalvarvenodorinvoiceform = $("#openvendorinvoiceFormID").serialize();
			
		},
		error: function(jqXHR, textStatus, errorThrown){
//			var errorText = $(jqXHR.responseText).find('u').html();
//			jQuery(newDialogDiv).html('<span><b style="color:red;">Error on modifying the data: '+errorText+'</b></span>');
//			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error.", 
//							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
	});
}

function clearvendorinvoiceformdata(){
	$("#proNumberID").val('');
	$("#vendorInvoiceNum").val("");
	//$("#shipViaSelectID").val("");
}
function openvendorinvoicedialog() {
	$("#vendorinvoice1").jqGrid('GridUnload');
	boolean = false;
	console.log("test vendorinvoice");
	$("#vendorinvoiceidclosebutton").css("display", "inline-block");
	 if(jQuery("#release").getGridParam("records") == '0'){
		 
		jQuery(newDialogDiv).html('<span><b style="color:red;">Please add one release in "Release Grid".</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	 
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	
	var aVendorDate = grid.jqGrid('getCell', rowId, 'vendorDate');
	var aVeBillId = grid.jqGrid('getCell', rowId, 'veBillID');
	
	var releasesGrid = $("#release");
	var selectedRelease = releasesGrid.jqGrid('getGridParam', 'selrow');
	var aJoReleaseDetailsID = releasesGrid.jqGrid('getCell', selectedRelease, 'joReleaseDetailid');
	var aVePOID = releasesGrid.jqGrid('getCell', selectedRelease, 'vePoId');
	var openorcloseven_inv=releasesGrid.jqGrid('getCell', selectedRelease, 'checkcloseoropenhidden');
	var dueondaysforrxMasterid =releasesGrid.jqGrid('getCell', selectedRelease, 'rxMasterId'); 
	
	if(!aVeBillId){
	
		aVeBillId = 0;
	}
	var accountId=$("#hiddenaccountId").val();
	$("#coAccountID option[value=" + accountId + "]").attr("selected", true);
	//alert("release detail ID: "+aJoReleaseDetailsID +"\naVePOID: "+aVePOID);
	//loadVendorInvoice(aVePOID);
	if(jQuery("#shiping").getGridParam("records") != 0 && aVeBillId>0){
		var releaseRowId = $("#release").jqGrid('getGridParam', 'selrow');
		var vepoID = grid.jqGrid('getCell', rowId, 'vePoId');
		if(vepoID.length!=0 && vepoID!=false){
			var DialogDiv = jQuery(document.createElement('div'));
			jQuery(DialogDiv).html('<span><b style="color:red;">PO must be created before entering vendor Invoice</b></span>');
			jQuery(DialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
						buttons:{

						"Ok": function(){
							jQuery(this).dialog("close");
						}
					}}).dialog("open");
			return false;
		}
		
		
		if(aVendorDate !== ''){
			var invoiceType = 'existing';
			loadPOGeneralDetails (aVePOID);
			loadVendorInvoice(aVePOID);
			//loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType);
			getVendorDetails(aJoReleaseDetailsID, aVePOID);
			/*Added by Jenith for BID#1021
			if(aVeBillId==0){*/
				$("#datedID").attr("readonly", "true"); 
				$("#shipDateID").val('');
				$("#shipDateID").val(currenDate);
				$("#vendorDateID").val('');
				$("#vendorDateID").val(currenDate);
				$("#datedID").val('');
				$("#datedID").val(currenDate);
				getDueonDayswithDate(dueondaysforrxMasterid);
			/*}*/
			$('#openvendorinvoice').dialog('option', 'title', '');
			if(aVendorDate==false){
				$('#openvendorinvoice').dialog('option', 'title', "Partial Vendor Invoice");
			}else{
				//$('#openvendorinvoice').dialog('option', 'title', aVendorDate);
				$('#openvendorinvoice').dialog('option', 'title', "Partial Vendor Invoice");
			}
			if(openorcloseven_inv=="true"){
				jQuery(newDialogDiv).html('<span><b style="color:red;">All items on this purchase order have been invoiced.Would you like to '+
						'open the Purchase Order for additional invoicing.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:350, height:160, title:"Information.", 
									buttons: [{height:35,text: "Yes",click: function() {
										$(this).dialog("close"); 
										jQuery("#openvendorinvoice").dialog("open");
										}},{height:35,text: "No",click: function() { 
											$(this).dialog("close"); 
										}}]}).dialog("open");
			}else{
				jQuery("#openvendorinvoice").dialog("open");
			}
			clearvendorinvoiceformdata();
			
		 }else{
			 var releaseRowId = $("#release").jqGrid('getGridParam', 'selrow');
				var vepoID = grid.jqGrid('getCell', rowId, 'vePoId');
				if(vepoID.length!=0 && vepoID!=false){
					var DialogDiv = jQuery(document.createElement('div'));
					jQuery(DialogDiv).html('<span><b style="color:red;">PO must be created before entering vendor Invoice</b></span>');
					jQuery(DialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
								buttons:{
									
								"Ok": function(){
									jQuery(this).dialog("close");
								}
							}}).dialog("open");
					return false;
				}
			 errorText = "Invoice not found, do you wish to create a new vendor invoice for this release?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					
				"Yes": function(){
					loadPOGeneralDetails (aVePOID);
					var invoiceType = 'new';
					loadVendorInvoice(aVePOID);
					//loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType);
					jQuery(this).dialog("close");
					//getVendorDetails(aJoReleaseDetailsID, aVePOID);
					$("#postDateID").val('');
					$("#postDateID").val(currenDate);
					$("#dueDateID").val('');
					getDueonDayswithDate(dueondaysforrxMasterid);
					$("#shipDateID").val('');
					$("#shipDateID").val(currenDate);
					$("#vendorDateID").val('');
					$("#vendorDateID").val(currenDate);
					$('#openvendorinvoice').dialog('option', 'title', '');
					$('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
					clearvendorinvoiceformdata();
					$("#jobpaidStatus").css("display","none");
					jQuery("#openvendorinvoice").dialog("open");
					
				},
				"No": function ()	{
					
					jQuery(this).dialog("close");
				}
			}}).dialog("open");
		 }
	}else{
		var releaseRowId = $("#release").jqGrid('getGridParam', 'selrow');
		var vepoID = grid.jqGrid('getCell', rowId, 'vePoId');
		if(vepoID.length!=0 && vepoID!=false){
			var DialogDiv = jQuery(document.createElement('div'));
			jQuery(DialogDiv).html('<span><b style="color:red;">PO must be created before entering vendor Invoice</b></span>');
			jQuery(DialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
						buttons:{
							
						"Ok": function(){
							jQuery(this).dialog("close");
						}
					}}).dialog("open");
			return false;
		}
		errorText = "Invoice not found, do you wish to create a new vendor invoice for this release?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
		buttons:{
			"Yes": function(){
				var invoiceType = 'new';
				
				loadVendorInvoice(aVePOID);
				loadPOGeneralDetails (aVePOID);
				//loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType);
				jQuery(this).dialog("close");
				//getVendorDetails(aJoReleaseDetailsID, aVePOID);
				$("#postDateID").val('');
				$("#postDateID").val(currenDate);
				$("#dueDateID").val('');
				getDueonDayswithDate(dueondaysforrxMasterid);
				$("#shipDateID").val('');
				$("#shipDateID").val(currenDate);
				$("#vendorDateID").val('');
				$("#vendorDateID").val(currenDate);
				$('#openvendorinvoice').dialog('option', 'title', '');
				$('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
				clearvendorinvoiceformdata();
				$("#jobpaidStatus").css("display","none");
				jQuery("#openvendorinvoice").dialog("open");
			},
			"No": function ()	{
		
				jQuery(this).dialog("close");
			}
		}}).dialog("open");
	}
	 return true;
}


function getDueonDayswithDate(dueondaysforrxMasterid)
{
	var d = new Date($("#datedID").val());
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
				
				console.log("-------->"+Number(data.dueonDaysPO));
				$("#dueDateID").val(today);
				
        }
	
	});

}






/*
function loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType){
	$("#vendorinvoice1").jqGrid('GridUnload');
	$("#vendorinvoice1").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager:jQuery('#vendorinvoicepager'),
		url:'./veInvoiceBillController/getBillLineitemsList',
		postData: {vePoId: function() { return aVePOID;}, veBillId: aVeBillId, aJoReleaseDetailsID: aJoReleaseDetailsID, invoiceType: invoiceType},
		colNames:['veBillDetailId','Product No', 'Description','Qty','Cost Ea', 'Mult.', 'Amount', 'VeBillId', 'prMasterID' , 'vePodetailID'],
		colModel :[
			{name:'veBillDetailId', index:'veBillDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prItemCode',index:'prItemCode',align:'left',width:70,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				dataInit: function (elem) {
					$(elem).autocomplete({
						source: 'jobtabs3/productCodeWithNameList',
						minLength: 1,
						select: function( event, ui ){ 
							var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} 
						},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
					}); 
				}
				},
				editrules:{edithidden:false,required: true}},
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityBilled', index:'quantityBilled', align:'center', width:15,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true,required: false}},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}, formatter:customCurrencyFormatter},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, editrules:{edithidden:true}},
			{name:'amount', index:'quantityBilled', align:'right', width:50,hidden:false, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}}
		],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'veBillDetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 820, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Items',
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
			//$("#vendorinvoice1").setSelection(1, true);
			var allRowsInGrid = $('#vendorinvoice1').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.amount;
				var number1 = aVal[index].replace(/[^0-9\.]+/g,"");
				sum = Number(sum) + Number(number1); 
			});
			$('#subtotal_ID').val(formatCurrency(sum));
			
			var taxValue = $('#tax_ID').val().replace(/[^0-9\.]+/g,"");
			
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace(/[^0-9\.]+/g,"");
					taxAmount = taxAmount + Number(number1)*(taxValue/100);
				}
			});
			
			var freightLineVal= $('#freight_ID').val();
			//alert($('#freight_ID').val());
			var freightAmount = '';
			if(freightLineVal !== ''){
				freightAmount = freightLineVal.replace(/[^0-9\.]+/g,"");
			}
			
			aTotal = aTotal + sum + Number(taxValue) + invoiceFreightAmount;
			//alert(aTotal+" :: sum:"+sum+"taxAmount:: "+taxAmount+"Number(freightAmount) "+Number(freightAmount)+" :: "+$('#freight_ID').val());
			$('#total_ID').val(formatCurrency(aTotal));
			*//***var txtBalanceDue = Dollar(veBill.Recordset("BillAmount")) - Dollar(veBill.Recordset("AppliedAmount"));*//*
			//var aBal =  aTotal-(sum+7+taxValue);
			//alert(aBal);
			$("#bal_ID").val(formatCurrency(aTotal));
			$("#vendorinvoice1").trigger("reload");
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		//editurl:"./veInvoiceBillController/manipulateBillLineitems"
	});.navGrid('#vendorinvoicepager',
			{add:true, edit:true,del:true,refresh:true,search:false}, //options
		//-----------------------edit options----------------------//
		{
			width:450, left:400, top: 300,
			closeAfterEdit:true, reloadAfterSubmit:true,
			modal:true, jqModel:false,
			editCaption: "Edit Product",
			beforeShowForm: function (form) {
				var unitcost = $("#unitCost").val();
				unitcost = unitcost.replace(/[^0-9\.]+/g,"");
				$("#unitCost").val(unitcost);
			},
			afterShowForm: function($form) {
				$(function() { var cache = {}; var lastXhr=''; $("input#prItemCode .FormElement").autocomplete({minLength: 1,timeout :1000,
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
				$("#prItemCode").autocomplete("destroy");
				$(".ui-menu-item").hide();
				var aPrMasterID = $('#prMasterId').val();
				if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
				return [true, ""];
			},
			onclickSubmit: function(params){
				var unitcost = $("#unitCost").val();
				var prItemCode= $("#prItemCode").val();
				var description= $("#description").val();
				var quantityBilled= $("#quantityBilled").val();
				var unitCost= $("#unitCost").val();
				var priceMultiplier= $("#priceMultiplier").val();
				var prMasterId=$("#prMasterId").val();
				var grid = $("#shiping");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var grid1 = $("#vendorinvoice1");
				var rowId1 = grid1.jqGrid('getGridParam', 'selrow');
				var veBillDetailId = grid1.jqGrid('getCell', rowId1, 'veBillDetailId');
				var aVeBillId = grid.jqGrid('getCell', rowId, 'veBillID');
				var avePodetailId = grid1.jqGrid('getCell', rowId1, 'vePodetailId');
				
				alert(avePodetailId+"::"+veBillDetailId);
				
				//alert("veBillDetailId"+veBillDetailId+"veBillId"+aVeBillId+"prMasterId"+prMasterId+"prItemCode"+prItemCode+"description"+description+"quantityBilled"+quantityBilled+"unitCost"+unitCost+"priceMultiplier"+priceMultiplier);
				return {'veBillDetailId':veBillDetailId,'veBillId' : aVeBillId,'description' : description,'prItemCode' : prItemCode,'prMasterId' : prMasterId,'priceMultiplier' : priceMultiplier,'quantityOrdered' : quantityBilled,'unitCost' : unitCost,'oper' : 'edit','vePODetailId':avePodetailId};
				
			},
			afterSubmit:function(response,postData){
				$("#vendorinvoice1").trigger("reloadGrid");
				return true;
			}
		},
		//-----------------------add options----------------------//
		{
			width:515, left:400, top: 300,
			closeAfterAdd:true,	reloadAfterSubmit:true,
			modal:true, jqModel:true,
			
			addCaption: "Add Product",
			beforeShowForm: function (form){
				var grid = $("#shiping");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var aVeBillId = grid.jqGrid('getCell', rowId, 'veBillID');
				$("#veBillId").val(aVeBillId);
				$(function() { var cache = {}; var lastXhr=''; $("input#prItemCode .FormElement").autocomplete({minLength: 1,timeout :1000,
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
						lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
					select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
						if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
					error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
					}); 
				return;
				});
			},
			afterShowForm: function($form) {
				$(function() { var cache = {}; var lastXhr=''; $("input#prItemCode .FormElement").autocomplete({minLength: 1,timeout :1000,
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
				$("#prItemCode").autocomplete("destroy");
				$(".ui-menu-item").hide();
				var aPrMasterID = $('#prMasterId').val();
				if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
				return [true, ""];
			},
			onclickSubmit: function(params){
				var unitcost = $("#unitCost").val();
				var prItemCode= $("#prItemCode").val();
				var description= $("#description").val();
				var quantityBilled= $("#quantityBilled").val();
				var unitCost= $("#unitCost").val();
				var priceMultiplier= $("#priceMultiplier").val();
				var prMasterId=$("#prMasterId").val();
				var grid = $("#shiping");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var aVeBillId = grid.jqGrid('getCell', rowId, 'veBillID');
				//alert("veBillId"+aVeBillId+"prMasterId"+prMasterId+"prItemCode"+prItemCode+"description"+description+"quantityBilled"+quantityBilled+"unitCost"+unitCost+"priceMultiplier"+priceMultiplier);
				return {'veBillId' : aVeBillId,'description' : description,'prItemCode' : prItemCode,'prMasterId' : prMasterId,'priceMultiplier' : priceMultiplier,'quantityOrdered' : quantityBilled,'unitCost' : unitCost,'oper' : 'add'};
				
			},
			afterSubmit:function(response,postData){
				$("#vendorinvoice1").trigger("reloadGrid");
				return true;
			}
		},
		//-----------------------Delete options----------------------//
		{	
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
			caption: "Delete Product",
			msg: 'Delete the Product Record?',

			onclickSubmit: function(params){
				var grid1 = $("#vendorinvoice1");
				var rowId1 = grid1.jqGrid('getGridParam', 'selrow');
				var veBillDetailId = grid1.jqGrid('getCell', rowId1, 'veBillDetailId');
				return { 'veBillDetailId' : veBillDetailId,'oper':'del'};
			}
		},
		//-----------------------search options----------------------//
		{	});
	loadPOGeneralDetails (aVePOID);
}

*/
var posit_job_vendorInvoice2=0;
function loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType){
	
	$("#vendorinvoice1").jqGrid('GridUnload');
	$("#vendorinvoice1").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager:jQuery('#vendorinvoicepager'),
		url:'./veInvoiceBillController/getBillLineitemsList',

		postData: {vePoId :function(){
			var vePOID1=vePOID;
			if(vePOID1==null || vePOID1==''){
				vePOID1=0;
				}
			return vePOID1;
			}, veBillId: aVeBillId, aJoReleaseDetailsID: aJoReleaseDetailsID, invoiceType: invoiceType},
		colNames:['Product No','','veBillDetailId', 'Description','Qty','Cost Ea', 'Mult.','Tax', 'Amount', 'VeBillId', 'prMasterID' , 'vePodetailID','remainQuantity','<img src="./../resources/images/delete.png" style="vertical-align: middle;">',''],
		colModel :[
	/*		{name:'prItemCode',index:'prItemCode',align:'left',width:70,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				dataInit: function (elem) {
					$(elem).autocomplete({
						source: 'jobtabs3/productCodeWithNameList',
						minLength: 1,
						select: function( event, ui ){ 
							var rowId = $("#release").jqGrid('getGridParam', 'selrow');
							var vepoid=$("#release").jqGrid('getCell', rowId, 'vePoId');
							$("#"+vendorinvoice1rowid+"_vePoid").val(vepoid);
							var ID = ui.item.id; var product = ui.item.label; $("#"+vendorinvoice1rowid+"_prMasterId").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} 
						},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
					}); 
				}
				},
				editrules:{edithidden:false,required: true}},*/
				
				{name:'prItemCode',index:'prItemCode',align:'left',width:50,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
					dataEvents: [
		   		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
					       				var rowobji=$(e.target).closest('tr.jqgrow');
			  	  	   		       		var textboxid=rowobji.attr('id');
			  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
			  		   		       				  e.target.select();
					       			   } },
		   		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
					       				var rowobji=$(e.target).closest('tr.jqgrow');
			  	  	   		       		var textboxid=rowobji.attr('id');
			  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
			  		   		       				  e.target.select();
					       			   } },
		   		    			 { type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
		   		    			  ],
					dataInit: function (elem) {
						 	//var aSelectedRowId = $("#vendorinvoice1").jqGrid('getGridParam', 'selrow');
				            $(elem).autocomplete({
				                source: 'jobtabs3/productCodeWithNameList',
				                minLength: 1,autoFocus: true,
				                select: function (event, ui) {
				                	var aSelectedRowId =elem.closest('tr').id;
				                	var IncTaxOnPOAndInvoices=getSysvariableStatusBasedOnVariableName('IncTaxOnPOAndInvoices');
				                	var id = ui.item.id;
				                	var product = ui.item.label;
				                	$("#"+aSelectedRowId+"_prMasterId").val(id);
				                	$("#new_row_prMasterId").val(id);
				                	
				                	/*var myGrid = $('#vePOID'),
				                    celValue = myGrid.val();*/
//				                	alert(celValue);
				                	
//				                	alert(" >>>>>>>> "+$("#new_row_vePoid").val()+ " || "+$("#"+aSelectedRowId+"_vePoid").val());
				                	//alert(id+" || "+product+" || "+aSelectedRowId+"_prMasterId = "+$("#"+aSelectedRowId+"_prMasterId").val()+" || new_row_prMasterId = "+$("#new_row_prMasterId").val());
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
															/*$("#new_row_sopopup").val(value.sopopup);
															$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
															$("#new_row_vePoid").val(celValue);
															$("#"+aSelectedRowId+"_vePoid").val(celValue);*/
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
					}, editrules:{edithidden:true,required: true}},
			{name:'inLineNoteImage', index:'inLineNoteImage', align:'right', width:25,hidden:false, editable:false, formatter:veInvinlineNoteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			{name:'veBillDetailId', index:'veBillDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,
           		dataEvents: [
      		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
      		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
      		    			{ type: 'keypress', data: { i: 7 }, fn: function(e) {
	   		    				var key = e.which;
	                    		 if(key == 13)  // the enter key code
	                    		  {
	                    			 $("#vendorinvoice1_ilsave").trigger("click");
//		                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
		                    		    return false;  
	                    		  }} 
	   		    			 }
      		    			  ]
           	},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:15,hidden:false, editable:true, editoptions:{size:5, alignText:'left',
					 dataEvents: [
					     			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
						       				var rowobji=$(e.target).closest('tr.jqgrow');
				  	  	   		       		var textboxid=rowobji.attr('id');
				  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
				  		   		       				  e.target.select();
						       			   } },
					  			  { type: 'click', data: { i: 7 }, fn: function(e) { 
					       				var rowobji=$(e.target).closest('tr.jqgrow');
			  	  	   		       		var textboxid=rowobji.attr('id');
			  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
			  		   		       				  e.target.select();
					       			   } },
			                        {
			                         type: 'change',
			                         fn: function(e) {
			                        	 Calculategrideditrowvalues(vendorinvoice1rowid);
			                        	 /*var quantityordered=$(this).val();
			                        	 var unitCost=$("#vendorinvoice1").jqGrid('getCell', vendorinvoice1rowid, 'unitCost');
			                        	 unitCost=unitCost.replace(/[^0-9\.-]+/g,"");
			                        	 var multiplier=$("#vendorinvoice1").jqGrid('getCell', vendorinvoice1rowid, 'priceMultiplier');
			                        	 var quantity=0;
			                        	 if(quantityordered.length>0){
			                        		 quantity=$(this).val();
			                        	 }
			                        	 var amount=parseFloat(unitCost)*parseFloat(quantity);
			                        	 if(multiplier>0){
			                        		 amount=parseFloat(multiplier)*parseFloat(amount);
			                        	 }
			                        	 var rowobj=jQuery("#vendorinvoice1").jqGrid('setCell', vendorinvoice1rowid, 'amount', amount);
			                              */ 
			                        	// setgridtotal1();
			                         }
			                        },
					  			{ type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
					  			  /*,
			                        
			                        {
	                                    type: 'change',
	                                    fn: function (e) {
	                                        var row = $(e.target).closest('tr.jqgrow');
	                                        var rowId = row.attr('id');
	                                        jQuery("#vendorinvoice1").saveRow(rowId, false, 'clientArray');
	                                        setgridtotal1();
	                                        
	                                    }
	                                }*/
			                        
			                       ]	
				
				},
				editrules:{custom: true,edithidden:false,custom_func:check_serialNo,required: true}	
			},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true,editoptions:{size:15, alignText:'right',
				dataEvents: [
				             { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
				  			  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
		                        {
		                         type: 'change',
		                         fn: function(e) {
		                               Calculategrideditrowvalues(vendorinvoice1rowid);
		                        	// setgridtotal();
		                         }
		                        },
		                        { type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
		                    ]		
			
			},editrules:{custom:true,custom_func:check_costnegative,required: true}, formatter:customCurrencyFormatter},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false,editable:true, editoptions:{size:15, alignText:'right',
				dataEvents: [
				             { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
				  			  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			  } },
		                        {
		                         type: 'change',
		                         fn: function(e) {
		                        	 
		                               Calculategrideditrowvalues(vendorinvoice1rowid);
		                        	// setgridtotal();
		                         }
		                        },
		                        { type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
		                     ]		
			}, formatter:'number', formatoptions:{decimalPlaces: 4}, editrules:{edithidden:true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right',
				dataEvents: [
     		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
     		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
     		    			 { type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
     		    			  ]
			},editrules:{edithidden:true}},
			{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{custom:true,custom_func:check_productNo}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'remainQuatity', index:'remainQuatity', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'canDoVI', index:'canDoVI', align:'center',  width:20, hidden:false, editable:false, formatter:canDocheckboxFormatterVIPO,   editrules:{edithidden:true}},
			{name:'note', index:'note', align:'right', width:10,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}}
			],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:482.5,	width: 840, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Items',
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
		onSelectRow: function(rowId){
			vendorinvoice1rowid=rowId;
			posit_job_vendorInvoice2= jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		loadBeforeSend: function(xhr) {
			posit_job_vendorInvoice2= jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop();
			if(aVePOID!=null || aVePOID!=''){
			gettaxpercentagefromvePO(aVePOID);
			}
		},
		loadComplete: function(data) {
			$("#vendorinvoice1").setSelection(1, true);
			var allRowsInGrid = $('#vendorinvoice1').jqGrid('getRowData');

			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace(/[^0-9\.-]+/g,"");
				sum = Number(sum) + Number(number1); 
			});
			$('#subtotal_ID').val(formatCurrency(sum));
			var taxValue = $('#tax_ID').val().replace(/[^0-9\.-]+/g,"");
			var taxpercent=$('#dropshipTaxID_release').val();
			
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace(/[^0-9\.-]+/g,"");
					taxAmount = taxAmount + Number(number1)*(taxpercent/100);
				}
			});
			var freightLineVal= $('#freight_ID').val();
			var number1 = '';
			if(freightLineVal !== ''){
				number1 = freightLineVal.replace(/[^0-9\.-]+/g,"");
			}
			$( "#vendorinvoice1_iladd" ).trigger( "click" );
			var allRowsInGridwithnewrow = $('#vendorinvoice1').jqGrid('getRowData');
			_globalvarvenodorinvoicegrid = JSON.stringify(allRowsInGridwithnewrow);
			console.log("_globalvarvenodorinvoicegrid=="+_globalvarvenodorinvoicegrid);
			//aTotal = parseFloat(aTotal) + parseFloat(sum) + parseFloat(taxAmount) + Number(number1);
			//$('#total_ID').val(formatCurrency(aTotal));
			/*var aBal =  aTotal-(sum+7+taxValue);
			$("#bal_ID").val(aBal);*/
			//$("#vendorinvoice1").trigger("reload");
		},
		gridComplete:function(data){
			jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_vendorInvoice2);
            //posit_job_vendorInvoice2=0;
			setgridtotal();
		},
		ondblClickRow: function(rowid) {
			if(rowid=="new_row"){
				 
			 }else{
				 posit_job_vendorInvoice2= jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop();
				 $("#vendorinvoice1_ilcancel").trigger("click");
			     $("#vendorinvoice1_iledit").trigger("click");
			 }
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		//editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
		cellsubmit: 'clientArray',
		editurl: 'clientArray',
	});
	$("#vendorinvoice1").jqGrid("navGrid","#vendorinvoicepager",  {
		add : false,
		edit : false,
		del : false,
		alertzIndex : 1234,
		search : false,
		refresh : false,
		pager : true,
		alertcap : "Warning",
		alerttext : 'Please select a Product'
	},
	// -----------------------edit// options----------------------//
	{},
	// -----------------------add options-------------------------//
	{},
	// -----------------------Delete options----------------------//
	{}
	);
	$("#vendorinvoice1").jqGrid("inlineNav","#vendorinvoicepager", {
		add : true,
		edit : true,
		refresh : false,
		cloneToTop : true,
		alertzIndex : 1234,
		addParams: {
	       // position: "afterSelected",
			position: "last",
	        addRowParams: {
				keys : false,
				
				oneditfunc : function(rowid) {
					$("#info_dialog").css("z-index", "10000");
				},
				successfunc : function(response) {
					$("#info_dialog").css("z-index", "12345");
						return true;
				},
				aftersavefunc : function(response) {
					$("#info_dialog").css("z-index", "12345");
					var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
					var veaccrrowid;
					if(ids.length==1){
						veaccrrowid = 0;
					}else{
						var idd = jQuery("#vendorinvoice1 tr").length;
						for(var i=0;i<ids.length;i++){
							if(idd<ids[i]){
								idd=ids[i];
							}
						}
						 veaccrrowid= idd;
					}
					if(vendorinvoice1rowid=="new_row"){
					$("#" + vendorinvoice1rowid).attr("id", Number(veaccrrowid)+1);
					var candoidrownum=Number(veaccrrowid)+1;
					$("#canDoID_new_row").attr("id", "canDoID_"+candoidrownum);
					$("#canDoVIID_new_row").attr("id","canDoVIID_"+candoidrownum);
					$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+candoidrownum);
					$("#canDoVIID_"+candoidrownum).attr("onclick","deleteRowFromJqGrid('"+candoidrownum+"');");
					$("#noteImageIcon_"+candoidrownum).attr("onclick","ShowNote('"+candoidrownum+"');");
					}
					
					
					setgridtotal();
					//alert("insidee");
					setTimeout(function(){
					 $("#vendorinvoice1").jqGrid('resetSelection');
					 var grid=$("#vendorinvoice1");
						grid.jqGrid('resetSelection');
					    var dataids = grid.getDataIDs();
					    for (var i=0, il=dataids.length; i < il; i++) {
					        grid.jqGrid('setSelection',dataids[i], false);
					    }
					    $( "#vendorinvoice1_iladd" ).trigger( "click" );
					 $("#vendorinvoice1").jqGrid('setSelection','new_row', true);
					},300);
					 
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
	    	keys : false,
	        oneditfunc: function (rowid) {
	        	$("#info_dialog").css("z-index", "15000");
	           // alert("row with rowid=" + rowid + " is editing.");
	        },
	   	successfunc : function(response) {
	   		$("#info_dialog").css("z-index", "12345");
	   		//alert("successfunc");
				return true;
		},
		aftersavefunc : function(response) {


			
			$("#info_dialog").css("z-index", "15000");
			
			var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
			var veaccrrowid;
			if(ids.length==1){
				veaccrrowid = 0;
			}else{
				var idd = jQuery("#vendorinvoice1 tr").length;
				for(var i=0;i<ids.length;i++){
					if(idd<ids[i]){
						idd=ids[i];
					}
				}
				 veaccrrowid= idd;
			}
			if(vendorinvoice1rowid=="new_row"){
				$("#" + vendorinvoice1rowid).attr("id", Number(veaccrrowid)+1);
				var candoidrownum=Number(veaccrrowid)+1;
				$("#canDoID_new_row").attr("id", "canDoID_"+candoidrownum);
				$("#canDoVIID_new_row").attr("id","canDoVIID_"+candoidrownum);
				$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+candoidrownum);
				$("#canDoVIID_"+candoidrownum).attr("onclick","deleteRowFromJqGrid('"+candoidrownum+"');");
				$("#noteImageIcon_"+candoidrownum).attr("onclick","ShowNote('"+candoidrownum+"');");
			}
			
			setgridtotal();
			setTimeout(function(){
			 $("#vendorinvoice1").jqGrid('resetSelection');
			 var grid=$("#vendorinvoice1");
				grid.jqGrid('resetSelection');
			    var dataids = grid.getDataIDs();
			    for (var i=0, il=dataids.length; i < il; i++) {
			        grid.jqGrid('setSelection',dataids[i], false);
			    }
			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			 $("#vendorinvoice1").jqGrid('setSelection','new_row', true);
			},300);
			//$("#vendorinvoice1").trigger("reload");
			
		},
		errorfunc : function(rowid, response) {
			$("#info_dialog").css("z-index", "12345");
			return false;
		},
		afterrestorefunc : function(rowid) {
			$("#info_dialog").css("z-index", "12345");
		}
	    
	    },restoreAfterSelect :false
		}
	);
	$("#vendorinvoice1_ilsave").click(function() {
		setTimeout(function(){$("#info_dialog").css("z-index", "12345");
		},100);
		});
}

var posit_job_vendorInvoice1=0;
/** vendor Invoice Grid **/
function loadVendorInvoice(vePOID){
	$("#vendorinvoice1").jqGrid('GridUnload');
	$("#vendorinvoice1").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager:jQuery('#vendorinvoicepager'),
		url:'./jobtabs5/jobReleasevendorinvoice',
		postData: {vePoId :function(){
			var vePOID1=vePOID;
			if(vePOID1==null || vePOID1==''){
				vePOID1=0;
				}
			return vePOID1;
			} },
		colNames:['Product No','','id', 'Description','Qty','Cost Ea', 'Mult.', 'Tax', 'Amount',  'prMasterID' , 'vePodetailID','Posiion', 'Move','','','','<img src="./../resources/images/delete.png" style="vertical-align: middle;">'],
		colModel :[
					{name:'itemCode',index:'itemCode',align:'left',width:50,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
						dataEvents: [
					       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
					       				var rowobji=$(e.target).closest('tr.jqgrow');
			  	  	   		       		var textboxid=rowobji.attr('id');
			  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
			  		   		       				  e.target.select();
					       			  } },
					    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
						       				var rowobji=$(e.target).closest('tr.jqgrow');
				  	  	   		       		var textboxid=rowobji.attr('id');
				  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
				  		   		       				  e.target.select();
						       			   } }
					    			  ],
						dataInit: function (elem) {
							 	//var aSelectedRowId = $("#vendorinvoice1").jqGrid('getGridParam', 'selrow');
					            $(elem).autocomplete({
					                source: 'jobtabs3/productCodeWithNameList',
					                minLength: 1,autoFocus: true,
					                select: function (event, ui) {
					                	var aSelectedRowId =elem.closest('tr').id;
					                	var IncTaxOnPOAndInvoices=getSysvariableStatusBasedOnVariableName('IncTaxOnPOAndInvoices');
					                	var id = ui.item.id;
					                	var product = ui.item.label;
					                	$("#"+aSelectedRowId+"_prMasterId").val(id);
					                	$("#new_row_prMasterId").val(id);
					                	
					                	var myGrid = $('#vePOID'),
					                    celValue = myGrid.val();
//					                	alert(celValue);
					                	
//					                	alert(" >>>>>>>> "+$("#new_row_vePoid").val()+ " || "+$("#"+aSelectedRowId+"_vePoid").val());
					                	//alert(id+" || "+product+" || "+aSelectedRowId+"_prMasterId = "+$("#"+aSelectedRowId+"_prMasterId").val()+" || new_row_prMasterId = "+$("#new_row_prMasterId").val());
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
																$("#new_row_sopopup").val(value.sopopup);
																$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
																$("#new_row_vePoid").val(celValue);
																$("#"+aSelectedRowId+"_vePoid").val(celValue);
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
						}, editrules:{edithidden:true,required: true}},
			{name:'inLineNoteImage', index:'inLineNoteImage', align:'right', width:25,hidden:false, editable:false, formatter:veInvinlineNoteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},		
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,
           		dataEvents: [
   		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
			       				var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();
			       			   } },
   		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
			       				var rowobji=$(e.target).closest('tr.jqgrow');
	  	  	   		       		var textboxid=rowobji.attr('id');
	  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
	  		   		       				  e.target.select();
			       			   } },
				  			{ type: 'keypress', data: { i: 7 }, fn: function(e) {
	   		    				var key = e.which;
	                    		 if(key == 13)  // the enter key code
	                    		  {
	                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
	                    			 $("#vendorinvoice1_ilsave").trigger("click");
//		                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
		                    		    return false;  
	                    		  }} 
	   		    			 }
   		    			  ]
           	},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:15,hidden:false, editable:true, editoptions:{size:5, alignText:'left',decimalPlaces: 2,
				 dataEvents: [
				  			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
							  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			  } },
		                        {
		                         type: 'change',
		                         fn: function(e) {
		                               Calculategrideditrowvalues(vendorinvoice1rowid);
		                               
		                        	// setgridtotal();
		                         }
		                        },
					  			{ type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
							  /*,
		                        
		                        {
                                    type: 'change',
                                    fn: function (e) {
                                        var row = $(e.target).closest('tr.jqgrow');
                                        var rowId = row.attr('id');
                                        jQuery("#vendorinvoice1").saveRow(rowId, false, 'clientArray');
                                        setgridtotal();
                                        
                                    }
                                }*/
		                        
		                        
		                        /*,
		                        {  type: 'change',
		                            fn: function(e) {
		                            	alert("inside change");
		                            }
		                        }*/
		                       ]	
			
			},
			editrules:{custom: true,edithidden:false,custom_func:check_serialNo,required: true}
			},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true,editoptions:{size:15, alignText:'right',decimalPlaces: 2,
				dataEvents: [
				             { type: 'focus', data: { i: 7 }, fn: function(e) {
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
							  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
		                        {
		                         type: 'change',
		                         fn: function(e) {
		                               Calculategrideditrowvalues(vendorinvoice1rowid);
		                        	// setgridtotal();
		                         }
		                        },
		                        { type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }
							  ]	
			},editrules:{edithidden:true}
			,editrules:{custom:true,custom_func:check_costnegative,required: true}
			, formatter:customCurrencyFormatter},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',decimalPlaces: 4,
				dataEvents: [
				             { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
							  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
		                        {
		                         type: 'change',
		                         fn: function(e) {
		                               Calculategrideditrowvalues(vendorinvoice1rowid);
		                        	// setgridtotal();
		                         }
		                        },
		                        { type: 'keypress', data: { i: 7 }, fn: function(e) {
		   		    				var key = e.which;
		                    		 if(key == 13)  // the enter key code
		                    		  {
		                    			 Calculategrideditrowvalues(vendorinvoice1rowid);
		                    			 $("#vendorinvoice1_ilsave").trigger("click");
//			                 			    $( "#vendorinvoice1_iladd" ).trigger( "click" );
			                    		    return false;  
		                    		  }} 
		   		    			 }]		
			}, formatter:'number', formatoptions:{decimalPlaces: 4}, editrules:{edithidden:true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right',
				dataEvents: [
	   		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } },
	   		    			  { type: 'click', data: { i: 7 }, fn: function(e) { 
				       				var rowobji=$(e.target).closest('tr.jqgrow');
		  	  	   		       		var textboxid=rowobji.attr('id');
		  		   		       		jQuery("#vendorinvoice1").jqGrid('setSelection',textboxid, true);
		  		   		       				  e.target.select();
				       			   } }
	   		    			  ]
			},editrules:{edithidden:true}},
			{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{custom:true,custom_func:check_productNo}},
			{name:'posistion',index:'posistion',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
			{name:'upAndDown',index:'upAndDown',align:'left',width:40, hidden: true,editable:true},
			{name:'validatequantity', index:'validatequantity', align:'center', width:15,hidden:true,editable:true},
			{name:'subtractedquantity', index:'subtractedquantity', align:'center', width:15,hidden:true,editable:true},
			{name:'note',index:'note',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
			{name:'canDoVI', index:'canDoVI', align:'center',  width:20, hidden:false, editable:false, formatter:canDocheckboxFormatterVIPO,   editrules:{edithidden:true}}
			],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:482.5,	width: 840, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Items',
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
		onSelectRow: function(rowId){
			vendorinvoice1rowid=rowId;
			posit_job_vendorInvoice1= jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop();
		},
		loadBeforeSend: function(xhr) {
			posit_job_vendorInvoice1= jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop();
			if(vePOID!=null || vePOID!=''){
				gettaxpercentagefromvePO(vePOID);
				}
		},
		loadComplete: function(data) {
			$("#vendorinvoice1").setSelection(1, true);
			var allRowsInGrid = $('#vendorinvoice1').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace(/[^0-9\.-]+/g,"");
				sum = Number(sum) + Number(number1); 
			});
			//alert(sum);
			$('#subtotal_ID').val(formatCurrency(sum));
			var taxValue = $('#tax_ID').val().replace(/[^0-9\.-]+/g,"");
			var taxpercent=$('#dropshipTaxID_release').val();
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace(/[^0-9\.-]+/g,"");
					taxAmount = taxAmount + Number(number1)*(taxpercent/100);
				}
			});
			var freightLineVal= $('#freight_ID').val();
			var number1 = '';
			if(freightLineVal !== ''){
				number1 = freightLineVal.replace(/[^0-9\.-]+/g,"");
			}
			$( "#vendorinvoice1_iladd" ).trigger( "click" );
			var allrow_withnewrow = $('#vendorinvoice1').jqGrid('getRowData');
			_globalvarvenodorinvoicegrid = JSON.stringify(allrow_withnewrow);
			console.log("_globalvarvenodorinvoicegrid=="+_globalvarvenodorinvoicegrid);
		},
		gridComplete:function(data){
			jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_vendorInvoice1);
            //posit_job_vendorInvoice1=0;
			setgridtotal();
		},
		ondblClickRow: function(rowid) {
			if(rowid=="new_row"){
				 
			 }else{
				 posit_job_vendorInvoice1= jQuery("#vendorinvoice1").closest(".ui-jqgrid-bdiv").scrollTop();
				 $("#vendorinvoice1_ilcancel").trigger("click");
			     $("#vendorinvoice1_iledit").trigger("click");
			 }
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		//editurl:"./jobtabs5/manpulateporeleaselineitem"
		cellsubmit: 'clientArray',
		editurl: 'clientArray',
	});
	$("#vendorinvoice1").jqGrid("navGrid","#vendorinvoicepager", {
		add : false,
		edit : false,
		del : false,
		search:false,refresh:false}
	);
	$("#vendorinvoice1").jqGrid("inlineNav","#vendorinvoicepager", {
		add : true,
		addtext:"Add",
		edit : true,
		edittext:"Edit",
		save: true,
		savetext:"Save",
		cancel : true,
		canceltext:"Cancel",
		addParams: {
	       // position: "afterSelected",
			position: "last",
	        addRowParams: {
				keys : false,
				oneditfunc : function(rowid) {
					$("#info_dialog").css("z-index", "10000");
				},
				successfunc : function(response) {
					$("#info_dialog").css("z-index", "12345");
						return true;
				},
				aftersavefunc : function(response) {
					$("#info_dialog").css("z-index", "12345");
						var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
						var veaccrrowid;
						if(ids.length==1){
							veaccrrowid = 0;
						}else{
							var idd = jQuery("#vendorinvoice1 tr").length;
							for(var i=0;i<ids.length;i++){
								if(idd<ids[i]){
									idd=ids[i];
								}
							}
							 veaccrrowid= idd;
						}
						if(vendorinvoice1rowid=="new_row"){
						$("#" + vendorinvoice1rowid).attr("id", Number(veaccrrowid)+1);
						var candoidrownum=Number(veaccrrowid)+1;
						$("#canDoID_new_row").attr("id", "canDoID_"+candoidrownum);
						$("#canDoVIID_new_row").attr("id","canDoVIID_"+candoidrownum);
						$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+candoidrownum);
						$("#canDoVIID_"+candoidrownum).attr("onclick","deleteRowFromJqGrid("+candoidrownum+");");
						$("#noteImageIcon_"+candoidrownum).attr("onclick","ShowNote('"+candoidrownum+"');");
						}
						setgridtotal();
						setTimeout(function(){
							 $("#vendorinvoice1").jqGrid('resetSelection');
							 var grid=$("#vendorinvoice1");
								grid.jqGrid('resetSelection');
							    var dataids = grid.getDataIDs();
							    for (var i=0, il=dataids.length; i < il; i++) {
							        grid.jqGrid('setSelection',dataids[i], false);
							    }
							    $( "#vendorinvoice1_iladd" ).trigger( "click" );
							 $("#vendorinvoice1").jqGrid('setSelection','new_row', true);
							},300);
//					alert("insidee");
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
	        	//.replace(/[^0-9\.]+/g,"");
	        	var unitCost=$("#"+rowid+"_unitCost").val();
	        	$("#"+rowid+"_unitCost").val(unitCost.replace(/[^0-9-\.]+/g,""));
	        	$("#info_dialog").css("z-index", "10000");
	           // alert("row with rowid=" + rowid + " is editing.");
	        },
	   	successfunc : function(response) {
	   		$("#info_dialog").css("z-index", "12345");
	   		//alert("successfunc");
				return true;
		},
		aftersavefunc : function(response) {
			$("#info_dialog").css("z-index", "10000");
			
			var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
			var veaccrrowid;
			if(ids.length==1){
				veaccrrowid = 0;
			}else{
				var idd = jQuery("#vendorinvoice1 tr").length;
				for(var i=0;i<ids.length;i++){
					if(idd<ids[i]){
						idd=ids[i];
					}
				}
				 veaccrrowid= idd;
			}
			if(vendorinvoice1rowid=="new_row"){
				$("#" + vendorinvoice1rowid).attr("id", Number(veaccrrowid)+1);
				var candoidrownum=Number(veaccrrowid)+1;
				$("#canDoID_new_row").attr("id", "canDoID_"+candoidrownum);
				$("#canDoVIID_new_row").attr("id","canDoVIID_"+candoidrownum);
				$("#noteImageIcon_new_row").attr("id","noteImageIcon_"+candoidrownum);
				$("#canDoVIID_"+candoidrownum).attr("onclick","deleteRowFromJqGrid('"+candoidrownum+"');");
				$("#noteImageIcon_"+candoidrownum).attr("onclick","ShowNote('"+candoidrownum+"');");
				
			}
			
			setgridtotal();
			setTimeout(function(){
				 $("#vendorinvoice1").jqGrid('resetSelection');
				 var grid=$("#vendorinvoice1");
					grid.jqGrid('resetSelection');
				    var dataids = grid.getDataIDs();
				    for (var i=0, il=dataids.length; i < il; i++) {
				        grid.jqGrid('setSelection',dataids[i], false);
				    }
				    $( "#vendorinvoice1_iladd" ).trigger( "click" );
				 $("#vendorinvoice1").jqGrid('setSelection','new_row', true);
				},300);
		},
		errorfunc : function(rowid, response) {
			$("#info_dialog").css("z-index", "12345");
			return false;
		},
		afterrestorefunc : function(rowid) {
			$("#info_dialog").css("z-index", "12345");
		}
	    
	    }
	    ,restoreAfterSelect :false
	});
	
	$("#vendorinvoice1_ilsave").click(function() {
		setTimeout(function(){$("#info_dialog").css("z-index", "12345");
		},100);
		});
	/*var custombuttonthereornot=document.getElementById("deleteveinvoicecustombutton");
    if(custombuttonthereornot == null){
		$("#vendorinvoice1").navButtonAdd('#vendorinvoicepager',
				{ 	caption:"", 
			 		id:"deleteveinvoicecustombutton",
					buttonicon:"ui-icon-trash", 
					onClickButton: deleteglgrid,
					position: "last", 
					title:"Delete", 
					cursor: "pointer"
				} 
			);

		custombutton=true;
		
	}*/
    
   /* $("#vendorinvoice1_iladd").click(function() {
    	var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
    	$('#vendorinvoiceidbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		$('#vendorinvoiceidclosebutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		document.getElementById("vendorinvoiceidbutton").disabled = true;
		document.getElementById("vendorinvoiceidclosebutton").disabled = true;
    });
    $("#vendorinvoice1_iledit").click(function() {
    	var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
    	$('#vendorinvoiceidbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		$('#vendorinvoiceidclosebutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		document.getElementById("vendorinvoiceidbutton").disabled = true;
		document.getElementById("vendorinvoiceidclosebutton").disabled = true;
    });
    
    $("#vendorinvoice1_ilsave").click(function() {
    	$("#info_dialog").css("z-index", "12345");
    	
        if($("#info_head").text()!="Error")
        {
    	document.getElementById("vendorinvoiceidbutton").disabled = false;
		document.getElementById("vendorinvoiceidclosebutton").disabled = false;
		$('#vendorinvoiceidbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		$('#vendorinvoiceidclosebutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
    	}
   
      });*/
    
   /* $("#deleteveinvoicecustombutton").click(function() {
    	document.getElementById("vendorinvoiceidbutton").disabled = false;
		document.getElementById("vendorinvoiceidclosebutton").disabled = false;
		$('#vendorinvoiceidbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		$('#vendorinvoiceidclosebutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
    });*/
   /* $("#vendorinvoice1_ilcancel").click(function() {
    	document.getElementById("vendorinvoiceidbutton").disabled = false;
		document.getElementById("vendorinvoiceidclosebutton").disabled = false;
		$('#vendorinvoiceidbutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		$('#vendorinvoiceidclosebutton').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
    });*/
}



/*$("#vendorinvoice1_ilsave").click(function() {
	var ids = $("#vendorinvoice1").jqGrid('getDataIDs');
	var veaccrrowid;
	if(ids.length==1){
		veaccrrowid = 0;
	}else{
		var idd = jQuery("#vendorinvoice1 tr").length;
		for(var i=0;i<ids.length;i++){
			if(idd<ids[i]){
				idd=ids[i];
			}
		}
		 veaccrrowid= idd;
	}
	$("#" + vendorinvoice1rowid).attr("id", Number(veaccrrowid)+1);
	setgridtotal();
 
});*/

/*
 * Added By Aravind 
 * ID 604 Inline Notes From Purchase Order Should Be Included
 */
function veInvinlineNoteImage(cellValue, options, rowObject){
	var element = '';
	var id="noteImageIcon_"+options.rowId;
	var test=""+options.rowId;
   if(cellValue !== '' && cellValue !== null && cellValue != undefined){
	   element = "<div><div align='center'><img src='./../resources/images/inline_jqGrid1.png' style='vertical-align: middle;' id='"+id+"' onclick=\"ShowNote('"+test+"')\"/></div></div>";	   
   }else{
	   element = "<div><div align='center'><img src='./../resources/images/inline_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick=\"ShowNote('"+test+"')\"/></div></div>";
   }
   return element;
} 

function ShowNote(row){
	/*try{
		*/
		var jobStatus=$('#jobStatusList').val();
		console.log("JobStatus"+jobStatus);
		/*if(typeof(jobStatus) != "undefined")
		{*/
		CKEDITOR.replace('lineItemNoteID', ckEditorconfigforinline);
		/*}
		else
		{
		CKEDITOR.replace('lineItemNoteID', ckEditorconfig);
		}*/
		
		//var rows = jQuery("#SOlineItemGrid").getDataIDs();
		//var id = jQuery("#SOlineItemGrid").jqGrid('getGridParam',row);
		$("#SaveInlineNoteID").attr("onclick","SaveVeInvLineItemNote('"+row+"');");
		var notes = jQuery("#vendorinvoice1").jqGrid ('getCell', row, 'note');	  
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
		  	$("#vendorinvoice1").jqGrid('setCell',row,'note', inlineText);  
			  $("#vendorinvoice1").jqGrid('setCell',row,'inLineNoteImage', image);
			  
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



function check_serialNo( value, colname ) {
	 var validateqty =$("#vendorinvoice1").jqGrid ('getCell', vendorinvoice1rowid, 'validatequantity');
	 var result = null;
	if(value.length==0){
		/*jQuery(newDialogDiv).html('<span><b>Qty field is required</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");*/
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
				
				result = [false, 'Qty field is required'];
				globalcheckvalidation=false;
				//return [false, ''];
			/*}
		}
		});*/
	}else if(isNaN(value)){
		/*jQuery(newDialogDiv).html('<span><b>Numeric field is required</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");*/
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
				result = [false, 'Numeric field is required'];
				globalcheckvalidation=false;
				//return [false, ''];
		/*	}
		}
		});*/
	}/*else if(Number(value)<0){ 
		jQuery(newDialogDiv).html('<span><b>Numeric value should not less than 0</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
		result = [false, 'Quantity should not less than 0'];
		globalcheckvalidation=false;
				//return [false, ''];
			}
		}
		});
	}*//*else if(Number(value)>Number(validateqty)){
	
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
		result = [false, 'Quantity should not greater than Ordered quantity'];
		globalcheckvalidation=false;
				//return [false, ''];

			}
		}
		});
	}*/else{
		/*$("#vendorinvoice1_iladd").removeClass("ui-state-disabled");
		$("#vendorinvoice1_iledit").removeClass("ui-state-disabled");
		$("#vendorinvoice1_ilsave").addClass("ui-state-disabled");
		$("#vendorinvoice1_ilcancel").addClass("ui-state-disabled");*/
		 result = [true,""];
		 globalcheckvalidation=true;
	}
	return result;
}



function check_productNo( value, colname ) {
	 var result = null;
	if(value.length==0){
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");}, 200);
	
				result = [false, 'Invalid Product No. Please select from dropdown list.'];
				
				/*$("#vendorinvoice1_iladd").addClass("ui-state-disabled");
				$("#vendorinvoice1_iledit").addClass("ui-state-disabled");
				$("#vendorinvoice1_ilsave").removeClass("ui-state-disabled");
				$("#vendorinvoice1_ilcancel").removeClass("ui-state-disabled");*/
				
				globalcheckvalidation=false;
			
	}
	else{
		/*$("#vendorinvoice1_iladd").removeClass("ui-state-disabled");
		$("#vendorinvoice1_iledit").removeClass("ui-state-disabled");
		$("#vendorinvoice1_ilsave").addClass("ui-state-disabled");
		$("#vendorinvoice1_ilcancel").addClass("ui-state-disabled");*/
		 result = [true,""];
		 globalcheckvalidation=true;
	}
	return result;
}


function check_serialNoveBillDetail( value, colname ) {
	 var validateqty =$("#vendorinvoice1").jqGrid ('getCell', vendorinvoice1rowid, 'remainQuatity');
	 var result = null;
	if(value.length==0){
		/*jQuery(newDialogDiv).html('<span><b>Qty field is required</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");*/
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
				
				result = [false, 'Qty field is required'];
				globalcheckvalidation=false;
				//return [false, ''];
			/*}
		}
		});*/
	}else if(isNaN(value)){
		/*jQuery(newDialogDiv).html('<span><b>Numeric field is required</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");*/
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
				result = [false, 'Numeric field is required'];
				globalcheckvalidation=false;
				//return [false, ''];
		/*	}
		}
		});*/
	}else if(Number(value)<0){
		/*jQuery(newDialogDiv).html('<span><b>Numeric value should not less than 0</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");*/
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
		result = [false, 'Quantity should not less than 0'];
		globalcheckvalidation=false;
				//return [false, ''];
			/*}
		}
		});*/
	}else if(Number(value)>Number(validateqty)){
		/*jQuery(newDialogDiv).html('<span><b>Ordered quantity does not match</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");*/
	
		 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
		result = [false, 'Quantity should not greater than Ordered quantity'];
		globalcheckvalidation=false;
				//return [false, ''];
			/*}
		}
		});*/
	}else{
//		$("#vendorinvoice1_iladd").removeClass("ui-state-disabled");
//		$("#vendorinvoice1_iledit").removeClass("ui-state-disabled");
//		$("#vendorinvoice1_ilsave").addClass("ui-state-disabled");
//		$("#vendorinvoice1_ilcancel").addClass("ui-state-disabled");
		 result = [true,""];
		 globalcheckvalidation=true;
	}
	return result;
}





function deleteglgrid(){
	$('#vendorinvoice1').jqGrid('delRowData',vendorinvoice1rowid);
	
	
	/*var ids = $("#vendorinvoice1").jqGrid('getDataIDs'); 
	 for(var i=0;i<ids.length;i++){
		 var id=ids[i];
		 var rowid=i+1;
		 rowid="delglacc_"+rowid;
		 //alert(id);
		 $("#" + id).attr("id", rowid);
		// $("#"+id).attr("id", i+1);
	 }
	 $("#glAccountsGrid_iladd").removeClass("ui-state-disabled");
	 $("#glAccountsGrid_iledit").removeClass("ui-state-disabled");
	 $("#glAccountsGrid_ilsave").addClass("ui-state-disabled");
	 $("#glAccountsGrid_ilcancel").addClass("ui-state-disabled");*/
	 $("#vendorinvoice1").jqGrid('resetSelection');
	 setgridtotal();
}


/**  View Vendor Invoice Details **/
function viewInvoiceDetails(){
	var grid = $("#vendorinvoice1");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aVepoID = grid.jqGrid('getCell', rowId, 'prMasterId');
	var aItemCode = grid.jqGrid('getCell', rowId, 'note');
	document.location.href="./inventoryDetails?token=view&inventoryId="+aVepoID+"&itemCode="+aItemCode+"";
	return true;
}

/** Get Vendor Details **/
function getVendorDetails(joDetailId, vePOID){
	
	if(joDetailId === "" && vePOID === ""){
		return false;
	}
	
	try{
	
	$.ajax({
		url: "./jobtabs5/getVendorInvoiceDetails",
		async:false,
		type: "POST",
		data : {'releaseDetailID' : joDetailId, 'vePODetailId' : vePOID},
		success: function(data) {
			if(data !== ''){
				var postdate = new Date(data.postDate);
				var dueDate = new Date(data.dueDate);
				var shipDate = new Date(data.shipDate);
				var receiveDate = new Date(data.receiveDate);
				var dated = new Date(data.billDate);
			/*	var aPostDate = postdate.getUTCMonth()+1+"/"+ postdate.getUTCDate()+"/"+postdate.getUTCFullYear();
				var aDueDate = dueDate.getUTCMonth()+1+"/"+ dueDate.getUTCDate()+"/"+dueDate.getUTCFullYear();
				var aShipDate = shipDate.getUTCMonth()+1+"/"+ shipDate.getUTCDate()+"/"+shipDate.getUTCFullYear();
				var aReceiveDate = receiveDate.getUTCMonth()+1+"/"+ receiveDate.getUTCDate()+"/"+receiveDate.getUTCFullYear();*/
				
				var aPostDate = getFormattedDate(postdate);
				var aDueDate = getFormattedDate(dueDate);
				var aShipDate = getFormattedDate(shipDate);
				var aReceiveDate = getFormattedDate(receiveDate);
				var billDate = getFormattedDate(dated);
				
				
			 	if(data.veInvmandatory == 1)
	        	{
	        	$('#veInvnomandatoryfromjob').css({"display":"inherit"})
	        	$('#veInvnomandatoryfromjob').attr("data-manvalue",data.veInvmandatory);
	        	}
	        	else
	        	{
	        	$('#veInvnomandatoryfromjob').css({"display":"none"})
	        	$('#veInvnomandatoryfromjob').attr("data-manvalue",data.veInvmandatory);
	        	}
				
				
				//document.getElementById('datedID').disabled = true;
				$("#datedID").attr("readonly", "true"); 
				$("#postDateID").val('');
				$("#proNumberID").val(data.trackingNumber);
				$("#postDateID").val('');
				$("#postDateID").val(aPostDate);
				$("#dueDateID").val('');
				$("#dueDateID").val(aDueDate);
				$("#shipDateID").val('');
				$("#shipDateID").val(aShipDate);
				$("#vendorDateID").val('');
				$("#vendorDateID").val(aReceiveDate);
				if(data.billDate!=null){
					$("#datedID").val('');
					$("#datedID").val(billDate);
					}
				
				invoiceFreightAmount = data.freightAmount;
				//$("#freight_ID").val(formatCurrency(data.freightAmount));
				$("#freight_ID").val(formatCurrency(0));
				$("#tax_ID").val(formatCurrency(data.taxAmount));
				$("#bal_ID").val("$0.00");
				$("#vendorInvoiceNum").val(data.invoiceNumber);
				$("#veBill_ID").val(data.veBillId);
				//data.apaccountId
				
				$("#coAccountID option[value=" + data.apaccountId + "]").attr("selected", true);
				$("#shipViaSelectID option[value=" + data.veShipViaId + "]").attr("selected", true);
//				alert("shipViaSelectID :: "+data.veShipViaId);
//				alert("shipViaSelectID :: 1"+shipViaInvoiceID);
				if(data.veShipViaId==null || data.veShipViaId=='null'){
					$("#shipViaSelectID option[value=" + shipViaInvoiceID + "]").attr("selected", true);
				}
				try{
					var grid = $("#shiping");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var aVendorDate = grid.jqGrid('getCell', rowId, 'vendorDate');
					if(aVendorDate==''){
						$("#shipViaSelectID option[value=" + shipViaInvoiceID + "]").attr("selected", true);
					}	
				}catch(err){
					
				}
				
				var aPostChk = data.usePostDate;
				if(aPostChk === true){
					$("#postDateChkID").prop("checked", true);
					$("#postDateID").show(); 
				}
			}
			setvendorinvoicetotal();
		},
		error: function(jqXHR, textStatus, errorThrown){
//			var errorText = $(jqXHR.responseText).find('u').html();
//			jQuery(newDialogDiv).html('<span><b style="color:red;">Error on modifying the data: '+errorText+'</b></span>');
//			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error.", 
//							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
	});
}catch(err){
	alert(err.message);
}
	return true;
}

/** Post Date Show and hidden **/
function postDateChk(){
	if ($('#postDateChkID').is(':checked')) { 
		$("#postDateID").show(); 
 	} else {
		$("#postDateID").hide();
 	} 
}

/** Save invoice Details **/
function savevendorinvoice(_param,_reason){
	jQuery( "#jobinvreasondialog" ).dialog("close");
	
	//alert(_reason);
	
	var aInvoiceDetails = $("#openvendorinvoiceFormID").serialize();
	/*var aSubTotal = $("#subtotal_ID").val().replace(/\$/g, '');
	var aFreight = $("#freight_ID").val().replace(/\$/g, '');
	var aTax = $("#tax_ID").val().replace(/\$/g, '');
	var aTotal = $("#total_ID").val().replace(/\$/g, '');
	var aBalance = $("#bal_ID").val().replace(/\$/g, '');*/
	
	var aSubTotal = $("#subtotal_ID").val().replace(/[^0-9-\.]+/g,"");
	var aFreight = $("#freight_ID").val().replace(/[^0-9-\.]+/g,"");
	var aTax = $("#tax_ID").val().replace(/[^0-9-\.]+/g,"");
	var aTotal = $("#total_ID").val().replace(/[^0-9-\.]+/g,"");
	var aBalance = $("#bal_ID").val().replace(/[^0-9-\.]+/g,"");
	
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var aVeBillID =  grid.jqGrid('getCell', rowId, 'veBillID');
	//var joReleaseID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'joReleaseDetailid');
	var joReleaseID="";
	if(rowId!=null && rowId!="" && rowId!=false){
		joReleaseID = grid.jqGrid('getCell', rowId, 'joReleaseDetailID');	
	}
	var vePoID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	//var rxMasterID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxMasterId');
	
	//$("#rxMasterID").val(rxMasterID);

	
	var	aAddOREdit = _param;

	var vendorSubTot = grid.jqGrid('getCell', rowId, 'vendorsubtotalAmt');
	if(aVeBillID==false)
		aVeBillID = 0;
	
	var aVendorInvoiceDetails = aInvoiceDetails+"&subtotal_Name="+aSubTotal+"&freight_Name="+aFreight+"&tax_Name="+aTax+"&total_Name="+aTotal+"&bal_Name="+aBalance+
																		"&oper="+aAddOREdit+"&joReleaseDetailsID="+joReleaseID+"&veBill_Name="+aVeBillID+"&vePOID="+vePoID+"&rxMasterID="+$("#rxMasterID").val()+"&invReasonfmjob="+_reason;
	 var Totalcheck = 0;
	 var totalamount=0;
	 var ids = $("#shiping").jqGrid('getDataIDs'); 
					if(aAddOREdit == "add")	
						{
						
						
						
							 for(var i=0;i<ids.length;i++){
								 var selectedRowId=ids[i];
								 cellValue =$("#shiping").jqGrid ('getCell', selectedRowId, 'vendorsubtotalAmt');
								 totalamount=Number(totalamount)+Number(cellValue.replace(/[^0-9-\.]+/g,""));
							 }
							 if(isNaN(totalamount)){
								 totalamount=0;
							 }
							 Totalcheck=parseFloat(aSubTotal)+totalamount;
						}
					else
						{
							 for(var i=0;i<ids.length;i++){
								 var selectedRowId=ids[i];
								 cellValue =$("#shiping").jqGrid ('getCell', selectedRowId, 'vendorsubtotalAmt');
								 totalamount=Number(totalamount)+Number(cellValue.replace(/[^0-9-\.]+/g,""));
							 }
							 if(isNaN(totalamount)){
								 totalamount=0;
							 }
							 Totalcheck=parseFloat(aSubTotal)+totalamount-parseFloat(vendorSubTot);
						
						}
					
	
					
	 if(globalcheckvalidation){
		 
		 
		if($("#veInvnomandatoryfromjob").data("manvalue")==1 && $("#vendorInvoiceNum").val() =="" && operation !='close')
		{
		$('#mandveinvno').show();
		setTimeout(function(){
			$('#mandveinvno').hide();
			}, 2000);
		}
		else
		{		 
			 
		$.ajax({
			url: "./veInvoiceBillController/getPoTotal?vePoID="+vePoID+"&invNo="+$("#vendorInvoiceNum").val(),
			type: "POST",
			//data : aVendorInvoiceDetails,
			success: function(data) {
				
				var invStatus = data.split("-*-");
				 createtpusage('job-Release Tab','Vendor Invoice Save','Info','Job,Release Tab,Saving Vendor Invoice,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()+',PO ID:'+vePoID+',Invoice No:'+$("#vendorInvoiceNum").val()); 
				if(invStatus[1]=="true" && aVeBillID==0)
					{
					jQuery(newDialogDiv).html('<span><b style="color:Green;">This invoice number already exist for this vendor. <br>Are you sure you want to enter this bill?</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Question?", 
					buttons:{
						"OK": function(){
							jQuery(this).dialog("close");
							saveVendorInvoicebasedonpopup(invStatus[0],Totalcheck,aVendorInvoiceDetails);						
						    return true;
						},
						Cancel: function ()	{
							jQuery(this).dialog("close");
							return false;	
						}}}).dialog("open");
					
					}
				else
					{
					saveVendorInvoicebasedonpopup(invStatus[0],Totalcheck,aVendorInvoiceDetails);
					}
			
			}
		});
		}
		console.log(aVendorInvoiceDetails);
	 }
}


function saveVendorInvoicebasedonpopup(subTotalValue,Totalcheck,aVendorInvoiceDetails)
{
	Totalcheck = Totalcheck.toFixed(2);

	if(parseFloat(subTotalValue)<= parseFloat(Totalcheck)){
		jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the PO transaction Status?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
		buttons:{
			"OK": function(){
				jQuery(this).dialog("close");
				updateVendorInvoiceDetails(aVendorInvoiceDetails+"&updatePO=yes");
			    
			    return true;
			},
			Cancel: function ()	{
				jQuery(this).dialog("close");
				updateVendorInvoiceDetails(aVendorInvoiceDetails+"&updatePO=no");
			//	jQuery("#openvendorinvoice").dialog("close");
				
			return false;	
			}}}).dialog("open");
	}else{
		updateVendorInvoiceDetails(aVendorInvoiceDetails+"&updatePO=no");
	  //  jQuery("#openvendorinvoice").dialog("close");
		

	}



}
	/*
	 * Commented By Jenith on 2014-09-10
	 * for Invoice on 
		updateVendorInvoiceDetails(aVendorInvoiceDetails);
	
	*/
	/*$.ajax({
		url: "./veInvoiceBillController/updateVendorInvoiceDetails",
		type: "POST",
		data : aVendorInvoiceDetails,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Vendor Invoice Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); jQuery("#openvendorinvoice").dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});*/

	//jQuery("#openvendorinvoice").dialog("close");
//}

var updateVendorInvoiceDetails = function(aVendorInvoiceDetails){
	
	/*var rows = jQuery("#vendorinvoice1").getDataIDs();
	var deleteinvoiceDetailId=new Array();
		 for(var a=0;a<rows.length;a++)
		 {
		    row=jQuery("#vendorinvoice1").getRowData(rows[a]);
		   var id="#canDoVIID_"+rows[a];
		   var canDo=$(id).is(':checked');
			   if(canDo){
				  var veBillDetailId=row['veBillDetailId'];
				  if(veBillDetailId!=undefined && veBillDetailId!=null && veBillDetailId!="" && veBillDetailId!=0){
				 		deleteinvoiceDetailId.push(veBillDetailId);
				 	}
				 $('#vendorinvoice1').jqGrid('delRowData',rows[a]);
			   }
		   }*/
	
	;
		 
	var gridRows = $('#vendorinvoice1').getRowData();
	var rowData = new Array();
	var rowId = $("#release").jqGrid('getGridParam', 'selrow');
	var joReleaseId = $("#release").jqGrid('getCell', rowId,
			'joReleaseId');
	  
	  
	var releasrowid =  $("#release").jqGrid('getGridParam', 'selrow');
	
	
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	
	var dataToSend = JSON.stringify(gridRows);
	//dataToSend = dataToSend.replace(/&/g, "\\&")
	console.log(dataToSend);
	
	///alert(dataToSend.replace(/:/g, "888"));
	
	var count = $("#shiping").getGridParam("reccount");
	aVendorInvoiceDetails=aVendorInvoiceDetails+"&joReleaseID="+joReleaseId+"&shippingCount="+count;
	var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
	$.ajax({
		url: "./checkAccountingCyclePeriods",
		data:{"datetoCheck":$("#datedID").val(),"UserStatus":checkpermission},
		type: "POST",
		success: function(data) { 

			if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
			{
				periodid=data.cofiscalperiod.coFiscalPeriodId;
				yearid = data.cofiscalperiod.coFiscalYearId;
					$.ajax({
						url: "./veInvoiceBillController/updateVendorInvoiceDetails?"+aVendorInvoiceDetails+"&coFiscalPeriodId="+periodid+"&coFiscalYearId="+yearid,
						type: "POST",
						data : {'gridData':dataToSend,'delData':deleteveBillDetailIDDetailId},
						success: function(data) {
							$("#release").trigger("reloadGrid");	
							deleteveBillDetailIDDetailId=new Array();
						}
					});
					
					jQuery("#openvendorinvoice").dialog("close");
			
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
			else(data.AuthStatus == "denied")
			{
				showDeniedPopup();
			}
			}
			$("#release").trigger("reloadGrid");	
			
  	},
		error:function(data){
			console.log('error');
			}
		});
	
	
};

 			/** ----------------------------------------------------              Vendor Invoice              ---------------------------------------------------- **/
 
 jQuery( function(){
		jQuery("#cusinvoicetab").dialog({
			autoOpen:false,
			closeOnEscape: false,
			width:850,
			title:"Customer Invoice",
			modal:true,
			open: function(){
				 loadshiptostateautocmpte("#CI_Shipto");
				//GlobalPage_Validation=3;
			},
			close:function(){
				return true;
			}
		});
	});
 
 function invoiceshipToAddress(){
	var rxMasterId = $("#rxCustomer_ID").text();
	operationVar = "ship";
	 $.ajax({
		url: "./jobtabs3/getBilltoAddress",
		type: "GET",
		data : {"customerID" : rxMasterId,"oper" : operationVar},
		success: function(data) {
			var locationName = $("#jobCustomerName_ID").text();
			
			
			if(locationName == null || locationName == "" || typeof (locationName) =="undefined" )
				locationName = data.name;
			
			
			var rxAddressId = data.rxAddressId;
			var locationAddress1 = data.address1;
			var locationAddress2 = data.address2;
			var locationCity = data.city;
			var locationState = data.state;
			var locationZip = data.zip;
//			$("#rxAddressShipID").val(rxAddressId); 
//			$("#customerShipToAddressID").val(locationName); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
//			$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
//			document.getElementById('customerShipToAddressID').disabled=true;
//			document.getElementById('customerShipToAddressID1').disabled=true;
//			document.getElementById('customerShipToAddressID2').disabled=true;
//			document.getElementById('customerShipToCity').disabled=true;
//			document.getElementById('customerShipToState').disabled=true;
//			document.getElementById('customerShipToZipID').disabled=true;
			}
		});
 }
 
function invoicebillToAddress(){
	var rxMasterId = $("#rxCustomer_ID").text();
	
	operationVar = "bill";
 $.ajax({
	url: "./jobtabs3/getBilltoAddress",
	type: "GET",
	data : {"customerID" : rxMasterId,"oper" : operationVar},
	success: function(data) {
		
		//var locationName = $("#jobCustomerName_ID").text();
		var locationName = $("#customerInvoice_customerInvoiceID").val();
		
		
		var rxAddressId = data.rxAddressId;
		var locationAddress1 = data.address1;
		var locationAddress2 = data.address2;
		var locationCity = data.city;
		var locationState = data.state;
		var locationZip = data.zip;
		$("#rxAddressBillID").val(rxAddressId); 
		$("#customerbillToAddressIDcuInvoice").val(locationName);
		$("#customerbillToAddressID1").val(locationAddress1); $("#customerbillToAddressID2").val(locationAddress2); $("#customerbillToCity").val(locationCity);
		$("#customerbillToState").val(locationState); $("#customerbillToZipID").val(locationZip);
		
		$('#customerbillToAddressID1cuInvoice').val(data.address1);
		//alert($('#customerbillToAddressID1cuInvoice').val());
		$('#customerbillToAddressID2cuInvoice').val(data.address2);
		$('#customerbillToCitycuInvoice').val(data.city);
		$('#customerbillToStatecuInvoice').val(data.state);
		$('#customerbillToZipIDcuInvoice').val(data.zip);
		$('#customerbillToAddressID1cuInvoiceRebuild').val(data.address1);
		$('#customerbillToAddressID2cuInvoiceRebuild').val(data.address2);
		$('#customerbillToCitycuInvoiceRebuild').val(data.city);
		$('#customerbillToStatecuInvoiceRebuild').val(data.state);
		$('#customerbillToZipIDcuInvoiceRebuild').val(data.zip);
		
		/*document.getElementById('customerbillToAddressID').disabled=true;
		document.getElementById('customerbillToAddressID1').disabled=true;
		document.getElementById('customerbillToAddressID2').disabled=true;
		document.getElementById('customerbillToCity').disabled=true;
		document.getElementById('customerbillToState').disabled=true;
		document.getElementById('customerbillToZipID').disabled=true;*/
		}
	});
}
var transaction = "";
 function savecustomerinvoice(operation) {
	 var customerInvoice_TaxTerritory=$("#customerInvoice_TaxTerritory").val();
	 
	 if(operation!='closedialog' && (customerInvoice_TaxTerritory==null || customerInvoice_TaxTerritory=="" ||customerInvoice_TaxTerritory.length==0)){

			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">please set Tax Territory in Customer->Roldex->Financial tab</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
			buttons:{
				"OK": function(){
					$(this).dialog("close");
				    return false;
				}
				}}).dialog("open");
			return false;
	 }
	 
	console.log('Inside jobwizardRelease.js - savecustomerinvoice');
	var cuinvId = $('#cuinvoiceIDhidden').val();
	var aVeBillID = '';
	var aAddOREdit = '';
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var ponumberAlphabet = bidderGrid.jqGrid('getCell', bidderGridRowId, 'ponumber');
	var InvoiceNumbers = $('#customerInvoice_invoiceNumberId').val();
	if(InvoiceNumbers==''){
		$('#customerInvoice_invoiceNumberId').val($("#jobNumber_ID").text().trim()+''+ponumberAlphabet);
	}
	var customerID = $('#rxCustomer_ID').text();
	var cusoId =  $('#Cuso_ID').text();
	var aInvoiceDetails = $("#custoemrInvoiceFormID").serialize();
	console.log("=======================================================================");
	console.log(_globaloldcustomerInvoiceform+" ========== "+aInvoiceDetails);
	console.log("=======================================================================");
	var gridRows = $('#customerInvoice_lineitems').getRowData();
 	var invoiceGridDetails =  JSON.stringify(gridRows)+$("#customerInvoice_invoiceDateID").val();
 	console.log(_globaloldcustomerInvoicegrid+" ========== "+invoiceGridDetails);
	console.log("=======================================================================");
	
	var aSubTotal = $("#customerInvoice_subTotalID").val().replace(/[^0-9\.-]+/g,"");
	var aFreight = $("#customerInvoice_frightIDcu").val().replace(/[^0-9\.-]+/g,"");
	var aTax = $("#customerInvoice_taxIdcu").val().replace(/[^0-9\.-]+/g,"");
	var aTotal = $("#customerInvoice_totalID").val().replace(/[^0-9\.-]+/g,"");
	var title = $('#cusinvoicetab').dialog('option', 'title');
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var cIopenStatus =  $('#ciOpenStatusID').val();
	var poNumber = $('#customerInvoice_proNumberID').val().replace(/[^0-9\.-]+/g,"");
	var taxrate = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.-]+/g,"");
	var InvoiceNo = $('#customerInvoice_invoiceNumberId').val();
	var shipDate = $('#customerInvoice_shipDateID').val();
	var joReleaseDetailID ='';
	if(rowId!=null){
	joReleaseDetailID = grid.jqGrid('getCell', rowId, 'joReleaseDetailID');
	}
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var joReleaseID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'joReleaseId');
	var type_release= bidderGrid.jqGrid('getCell', bidderGridRowId, 'type');
	
	if(cuinvId == null || cuinvId == '')
	{
		aAddOREdit = 'add';
		transaction = 'close';
		$('#operationID').val('add');
		cuinvId = $('#cuInvoiceID').text();
		$('#cuinvoiceIDhidden').val(cuinvId);
	}else{
		aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');
		aAddOREdit = 'edit';
		if(transaction==''){
			transaction = 'add';
		}else{
			transaction = 'close';
		}
		
		$('#operationID').val('edit');
	}
	
	/**
	 * Added for tax calculation
	 * 2014-09-05
	 * 
	 */
	aTax = $("#customerInvoice_generaltaxId").val().replace(/[^0-9\.-]+/g,"");
	if(parseFloat(aTax)>0){
		taxrate = aTax;
	}
	var taxAmountnew = ((aSubTotal*taxrate)/100);
	taxTotalAmt = taxAmountnew;
	
	var taxAmountField = $('#customerInvoice_taxIdcu').val();
	var releaseType = 2;
	if(vePOID != ''|| type_release=='Drop Ship'){
		releaseType = 1;
		cusoId = vePOID;
	}
	
	
	var grids = $("#release");
	var rowsId = grids.jqGrid('getGridParam', 'selrow');
	var releasType = grids.jqGrid('getCell', rowsId, 'type');
	var shipToAddressIDs = $('#prShiptowarehouseID').val();
	var shipToModeIn = $('#shiptoModeID').val();
	var customerInvoie_doNotMailID=0;
	if($("#customerInvoie_doNotMailID").is(":checked")){
		customerInvoie_doNotMailID=1;
	}
	
	var taxfreight=$('#CI_taxfreight').val();
	console.log('Address Detais: \n'+add1+"\n"+add2+'\n'+add3+'\n'+city+'\n'+state+'\n'+zip);
	
	/*var  aCustomerInvoiceDetails = aInvoiceDetails+"&customerInvoice_subTotalName="+aSubTotal+"&customerInvoice_frightname="+aFreight+"&customerInvoice_taxName="+taxAmountnew+"&customerInvoice_totalName="+aTotal+"&oper="+aAddOREdit+"&joReleaseDetailsID="+joReleaseDetailID+'&cuSOID='+cusoId+'&poNumber='+poNumber
	+'&InvoiceNo='+InvoiceNo+'&shipDate='+shipDate+'&taxRate='+taxrate+'&cuInvHiddenId='+cuinvId+"&releaseType="+releaseType+'&customerID='+customerID+'&shipToCustomerAddressID='+$('#shipToCustomerAddressID').val()+"&from=job&joReleaseID="+joReleaseID
	+"&transaction="+transaction+"&rxShiptoAddressID="+shipToAddressIDs+"&shipToMode="+shipToModeIn+"&customerInvoiceShipToAddressName="+add1+"&customerInvoiceShipToAddress1="+add2+"&customerInvoiceShipToAddress2="+add3+"&customerInvoiceShipToCity="+city
	+"&customerInvoiceShipToState="+state+"&customerInvoiceShipToZip="+zip+"&customerInvoie_doNotMailID="+customerInvoie_doNotMailID+"&customerInvoice_taxIdcuname="+taxAmountField;*/
	
	var  aCustomerInvoiceDetails = aInvoiceDetails+"&customerInvoice_subTotalName="+aSubTotal+"&customerInvoice_frightname="+aFreight+"&customerInvoice_taxName="+taxAmountnew+"&customerInvoice_totalName="+aTotal+"&oper="+aAddOREdit+"&joReleaseDetailsID="+joReleaseDetailID+'&cuSOID='+cusoId+'&poNumber='+poNumber
	+'&InvoiceNo='+InvoiceNo+'&shipDate='+shipDate+'&taxRate='+taxrate+'&cuInvHiddenId='+cuinvId+"&releaseType="+releaseType+'&customerID='+customerID+'&shipToCustomerAddressID='+$('#shipToCustomerAddressID').val()+"&from=job&joReleaseID="+joReleaseID
	+"&transaction="+transaction+"&customerInvoie_doNotMailID="+customerInvoie_doNotMailID+"&customerInvoice_taxIdcuname="+taxAmountField+"&CIjoMasterID="+$("#joMasterHiddenID").val()
	+"&CIrxShiptoid="+ $("#CI_Shipto").contents().find("#shiptoaddrhiddenfromuiid").val()+"&CIrxShiptomodevalue="+ $("#CI_Shipto").contents().find("#shiptomoderhiddenid").val()+"&taxfreight="+taxfreight;
	//"&rxShiptoAddressID="+shipToAddressIDs+"&shipToMode="+shipToModeIn+
/*
	if(operation=='closedialog'){
		cancelcustomerinvoice();
	}
	 */
	if(operation != "closedialog"){
		$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
		document.getElementById("CuInvoiceSaveID").disabled = true;
		document.getElementById("CuInvoiceSaveCloseID").disabled = true;
	}
	if(aAddOREdit == 'edit' && cIopenStatus == 'false')
		{
		$('#invreasondialog').data('cusoId', cusoId);
		$('#invreasondialog').data('transaction', "close");
		$('#invreasondialog').data('releasType', releasType);
		$('#invreasondialog').data('title', title);
		$('#invreasondialog').data('joReleaseID', joReleaseID);
		
		/*if(_globalold_cIlineitemform == "{}")
		_globalold_cIlineitemform = _globalold_cIlineitemform.replace('{}', '[]');*/
		//var aInvoiceDetailsTotal=$("#custoemrInvoiceFormTotalID").serialize();
		var aInvoiceDetailsTotal=generatecustoemrInvoiceFormTotalIDSeriallize();
		var generalTabFormval= CIGeneralTabSeriallize();
		//if(_globaloldcustomerInvoiceform != aInvoiceDetails ){
			console.log("_globaloldcustomerInvoiceform=="+_globaloldcustomerInvoiceform);
			console.log("_globalnewcustomerInvoiceform=="+generalTabFormval);
		//}
		//if(_globaloldcustomerInvoicegrid != invoiceGridDetails){
			console.log("_globaloldcustomerInvoicegrid=="+_globaloldcustomerInvoicegrid);
			console.log("invoiceGridDetails=="+invoiceGridDetails);
		//}
		//if(_globaloldcustomerInvoiceformTotal != aInvoiceDetailsTotal){
			console.log("_globaloldcustomerInvoiceformTotal=="+_globaloldcustomerInvoiceformTotal);
			console.log("aInvoiceDetailsTotal=="+aInvoiceDetailsTotal);
			//	}
			
		if(_globaloldcustomerInvoiceform != generalTabFormval || _globaloldcustomerInvoicegrid != invoiceGridDetails || _globaloldcustomerInvoiceformTotal != aInvoiceDetailsTotal)
		{
			transaction="close";
			var  aCustomerInvoiceDetails = aInvoiceDetails+"&customerInvoice_subTotalName="+aSubTotal+"&customerInvoice_frightname="+aFreight+"&customerInvoice_taxName="+taxAmountnew+"&customerInvoice_totalName="+aTotal+"&oper="+aAddOREdit+"&joReleaseDetailsID="+joReleaseDetailID+'&cuSOID='+cusoId+'&poNumber='+poNumber
			+'&InvoiceNo='+InvoiceNo+'&shipDate='+shipDate+'&taxRate='+taxrate+'&cuInvHiddenId='+cuinvId+"&releaseType="+releaseType+'&customerID='+customerID+"&from=job&joReleaseID="+joReleaseID
			+"&transaction="+transaction+"&customerInvoie_doNotMailID="+customerInvoie_doNotMailID+"&customerInvoice_taxIdcuname="+taxAmountField
			+"&CIrxShiptoid="+ $("#CI_Shipto").contents().find("#shiptoaddrhiddenfromuiid").val()+"&CIrxShiptomodevalue="+ $("#CI_Shipto").contents().find("#shiptomoderhiddenid").val()+"&CIjoMasterID="+$("#joMasterHiddenID").val()+"&taxfreight="+taxfreight;
			//+'&shipToCustomerAddressID='+$('#shipToCustomerAddressID').val()
			$('#invreasondialog').data('aCustomerInvoiceDetails', aCustomerInvoiceDetails);
			
			
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">You have made changes,would you like to save?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
			closeOnEscape: false,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			buttons:{
				"Yes": function(){
					jQuery(this).dialog("close");
					jQuery( "#invreasondialog" ).dialog("open");
				    return false;
				},
				"No": function ()	{
					document.getElementById("CuInvoiceSaveID").disabled = false;
					document.getElementById("CuInvoiceSaveCloseID").disabled = false;
					$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
					$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
					$('#transactionID').val('add');
					jQuery(this).dialog("close");
					if(operation=='closedialog'){
						cancelcustomerinvoice();
					}
					 
				return false;	
				}}}).dialog("open");
		}
		else
		{
			transaction="";
			if(operation =="closedialog"){
				cancelcustomerinvoice();
			}
			if(operation=='close'){
				 
				if(cusoId!==null && cusoId !==""){
				if(releasType=='Stock Order' || releasType=='Service'){
				var rowId = $("#release").jqGrid('getGridParam', 'selrow');
				var transStatus = $("#release").jqGrid('getCell', rowId,'transactionStatus');
				console.log('Transacion Status before Update :-:'+transStatus);
				if(transStatus!==2){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the SO transaction Status?</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
				buttons:{
					"OK": function(){
						
							$.ajax({
								url: "./salesOrderController/setSalesOrderStatus",
								type: "POST",
								async:false,
								data :{cusoID:cusoId,status:2},
								success: function(data) {
								}
							});
						
						$(this).dialog("close");
					    return false;
					},
					Cancel: function ()	{
						jQuery(this).dialog("close");
					return false;	
					}}}).dialog("open");
				}
				}
				}
			}
			
			
			$('#transactionID').val('add');
			$('#showMessageCuInvoice').css("margin-left", "1666%");
			$('#showMessageCuInvoiceLine').css("margin-left", "1666%");
			$('#showMessageCuInvoice').html("Saved");
			$('#showMessageCuInvoiceLine').html("Saved");
			
			$('#imgInvoicePDF').empty();
			$('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

			$('#imgInvoiceEmail').empty();
			$('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');

			document.getElementById("CuInvoiceSaveID").disabled = false;
			document.getElementById("CuInvoiceSaveCloseID").disabled = false;
			$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
			
			setTimeout(function(){
				$('#showMessageCuInvoice').html("");
				$('#showMessageCuInvoiceLine').html("");
				},3000);
		}
		}
	else
		{
		if(operation =="closedialog"){

			 
			if(cusoId!==null && cusoId !==""){
			if(releasType=='Stock Order'||releasType=='Bill Only'||releasType=='Service'){
			var rowId = $("#release").jqGrid('getGridParam', 'selrow');
			var transStatus = $("#release").jqGrid('getCell', rowId,'transactionStatus');
			console.log('Transacion Status before Update :-:'+transStatus);
			if(transStatus!==2){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the SO transaction Status?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
			buttons:{
				"OK": function(){
					if(releasType=='Bill Only'){
						$.ajax({
							url: "./salesOrderController/setBillOnlyStatus",
							type: "POST",
							async:false,
							data :{'joReleaseID':joReleaseID,'status':2},
							success: function(data) {
								jQuery(this).dialog("close");
								cancelcustomerinvoice();
							}
						});
					}else{
						$.ajax({
							url: "./salesOrderController/setSalesOrderStatus",
							type: "POST",
							async:false,
							data :{cusoID:cusoId,status:2},
							success: function(data) {
								jQuery(this).dialog("close");
								cancelcustomerinvoice();
							}
						});
					}
					
					jQuery(this).dialog("close");
				    return false;
				},
				Cancel: function ()	{
					jQuery(this).dialog("close");
					cancelcustomerinvoice();
				return false;	
				}}}).dialog("open");
			}else{
				cancelcustomerinvoice();
			}
			}else{
				cancelcustomerinvoice();
			}
			}
			return true;
		}
		var add1 = $("#CI_Shipto").contents().find('#shipToName').val();
		var add2 = $("#CI_Shipto").contents().find('#shipToAddress1').val();
		var add3 = $("#CI_Shipto").contents().find('#shipToAddress2').val();
		var city = $("#CI_Shipto").contents().find('#shipToCity').val();
		var state =$("#CI_Shipto").contents().find('#shipToState').val();
		var zip = $("#CI_Shipto").contents().find('#shipToZip').val();	
		var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
		$.ajax({
			url: "./checkAccountingCyclePeriods",
			data:{"datetoCheck":$('#customerInvoice_invoiceDateID').val(),"UserStatus":checkpermission},
			type: "POST",
			success: function(data) { 
				
				  createtpusage('job-Release Tab','Customer Invoice Save','Info','Job,Release Tab,Saving Customer Invoice,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()+',PO ID:'+vePOID+',Invoice No:'+InvoiceNo);

				if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
				{
					periodid=data.cofiscalperiod.coFiscalPeriodId;
					yearid = data.cofiscalperiod.coFiscalYearId;
					
					var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
					deleteinvoiceDetailId=new Array();
		 			 for(var a=0;a<rows.length;a++)
		 			 {
		 			    row=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
		 			   var id="#canDoID_"+rows[a];
		 			   var canDo=$(id).is(':checked');
			 			   if(canDo){
			 				  var cuInvoiceDetailId=row['cuInvoiceDetailId'];
			 				  if(cuInvoiceDetailId!=undefined && cuInvoiceDetailId!=null && cuInvoiceDetailId!="" && cuInvoiceDetailId!=0){
			 				 		deleteinvoiceDetailId.push(cuInvoiceDetailId);
			 				 	}
			 				 $('#customerInvoice_lineitems').jqGrid('delRowData',rows[a]);
			 			   }
		 			   }
		 			 console.log("I am in create=="+aCustomerInvoiceDetails);
		 			var gridRows = $('#customerInvoice_lineitems').getRowData();
					var dataToSend = JSON.stringify(gridRows);
						$.ajax({
							url: "./jobtabs5/updateCustomerInvoiceDetails?"+aCustomerInvoiceDetails,
							type: "POST",
							data : {'customerInvoiceShipToAddressName':add1,'customerInvoiceShipToAddress1':add2,
								'customerInvoiceShipToAddress2':add3,'customerInvoiceShipToCity':city,
								'customerInvoiceShipToState':state,'customerInvoiceShipToZip':zip,
								'coFiscalPeriodId':periodid,'coFiscalYearId':yearid,'gridData':dataToSend,'delData':deleteinvoiceDetailId},
							success: function(data) {
								transaction="";
								$('#operationID').val('edit');
								$('#ciOpenStatusID').val(data.cIopenStatus);
								$('#imgInvoicePDF').empty();
						 		$('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

								$('#imgInvoiceEmail').empty();
								$('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');
								if(operation=='closedialog'){
									 
									//alert('#'+cusoId+'#');
									if(cusoId!==null && cusoId !==""){
									if(releasType=='Stock Order'||releasType=='Bill Only'||releasType=='Service'){
									var rowId = $("#release").jqGrid('getGridParam', 'selrow');
									var transStatus = $("#release").jqGrid('getCell', rowId,'transactionStatus');
									console.log('Transacion Status before Update :-:'+transStatus);
									if(transStatus!==2){
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the SO transaction Status?</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons:{
										"OK": function(){
											if(releasType=='Bill Only'){
												$.ajax({
													url: "./salesOrderController/setBillOnlyStatus",
													type: "POST",
													async:false,
													data :{'joReleaseID':joReleaseID,'status':2},
													success: function(data) {
														$("#release").trigger("reloadGrid");
													}
												});
											}else{
												$.ajax({
													url: "./salesOrderController/setSalesOrderStatus",
													type: "POST",
													async:false,
													data :{cusoID:cusoId,status:2},
													success: function(data) {
														$("#release").trigger("reloadGrid");
													}
												});
											}
											
											$(this).dialog("close");
										    return false;
										},
										Cancel: function ()	{
											jQuery(this).dialog("close");
											$("#release").trigger("reloadGrid");
										return false;	
										}}}).dialog("open");
									}
									}
									}
									jQuery('#shiping').jqGrid('clearGridData')
									 loadShipingGrid(joReleaseID,"");
								}
								
								document.getElementById("CuInvoiceSaveID").disabled = false;
								document.getElementById("CuInvoiceSaveCloseID").disabled = false;
								$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
								$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
								
								$('#cuInvoiceID').text(data.cuInvoiceId);
								$('#cuinvoiceIDhidden').val(data.cuInvoiceId);
								
								 if(data.shipToMode == 0)
							         $("#CI_Shipto").contents().find('#shiptoaddrhiddenfromuiid').val(data.prToWarehouseId);
							     else if(data.shipToMode == 1)
							         $("#CI_Shipto").contents().find('#shiptoaddrhiddenfromuiid').val(data.rxShipToId);
							     else if(data.shipToMode == 2)
							         $("#CI_Shipto").contents().find('#shiptoaddrhiddenfromuiid').val(data.rxShipToId);
							     else if(data.shipToMode == 3)
							         $("#CI_Shipto").contents().find('#shiptoaddrhiddenfromuiid').val(data.rxShipToAddressId);
								
								$("#customerInvoice_lineitems").jqGrid('GridUnload');
								loadCustomerInvoice();
								$("#customerInvoice_lineitems").trigger("reloadGrid");
								$('#showMessageCuInvoice').css("margin-left", "1666%");
								$('#showMessageCuInvoiceLine').css("margin-left", "1666%");
								deleteinvoiceDetailId=new Array();
								$('#showMessageCuInvoice').html("Saved");
								$('#showMessageCuInvoiceLine').html("Saved");
								
								$('#imgInvoicePDF').empty();
								$('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

								$('#imgInvoiceEmail').empty();
								$('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');

								
								$("#cusinvoicetab").tabs({
								       disabled : false
								      });
								$('#prWareHouseSelectlineID').val($('#prWareHouseSelectID').val());
								$('#shipViaCustomerSelectlineID').val($('#shipViaCustomerSelectID').val());
								$('#customerInvoice_lineshipDateID').val($('#customerInvoice_shipDateID').val());
								$('#customerInvoice_lineproNumberID').val($('#customerInvoice_proNumberID').val());
								
								if(title === 'New Customer Invoice' && transaction == 'save'){
									$('#cusinvoicetab').dialog('option', 'title','Customer Invoice');	
								}
								
								setTimeout(function(){
									//updateTaxableLines();
									$('#showMessageCuInvoice').html("");
									$('#showMessageCuInvoiceLine').html("");
									//_globaloldcustomerInvoiceformTotal= $("#custoemrInvoiceFormTotalID").serialize();
									_globaloldcustomerInvoiceformTotal=generatecustoemrInvoiceFormTotalIDSeriallize();
									_globaloldcustomerInvoiceform =  $("#custoemrInvoiceFormID").serialize();
								 	var gridRows = $('#customerInvoice_lineitems').getRowData();
								 	_globaloldcustomerInvoicegrid =  JSON.stringify(gridRows)+$("#customerInvoice_invoiceDateID").val();
									},3000);
								getCustomerInvoiceDetailsforpopup(data.cuInvoiceId);
								
								
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
											buttons: [{text: "OK",click: function(){
												document.getElementById("CuInvoiceSaveID").disabled = false;
												document.getElementById("CuInvoiceSaveCloseID").disabled = false;
												$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
												$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
												$(this).dialog("close"); }}]
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

 function settotal(){
	 
	 setTaxTotal_CI();
	 /*var allocated1= $('#customerInvoice_subTotalID').val().replace(/[^0-9\.-]+/g,"");
	 var frieght= $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.-]+/g,"");
	 var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.-]+/g,"");
	 var taxRate = $('#customerInvoice_generaltaxId').val().replace(/[^0-9\.-]+/g,"");
	 
	 if(frieght==null || frieght=="" || frieght==undefined){
		 frieght=0.00;
	 }
	 if(allocated1==null || allocated1=="" || allocated1==undefined){
		 allocated1=0.00;
	 }
	 if(taxValue==null || taxValue=="" || taxValue==undefined){
		 taxValue=0.00;
	 }
	 var allowfreightinTax=false;
	 var allowreqcheckfreightintax=getSysvariableStatusBasedOnVariableName("RequireFreightwhencalculatingTaxonCustomerInvoices");
		if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax[0].valueLong==1){
			allowfreightinTax=true;
		}
	 if(allowfreightinTax){
		 taxValue = (parseFloat(allocated1)+parseFloat(frieght))*Number(taxRate)/100;
	 }else{
		 taxValue = parseFloat(allocated1)*Number(taxRate)/100;
	 }
	 
	 var total=parseFloat(allocated1)+parseFloat(frieght)+parseFloat(taxValue);
	 $('#customerInvoice_taxIdcu').val(formatCurrency(taxValue));
	 $('#customerInvoice_totalID').val(formatCurrency(total));*/
	 
 }
 
 function showPaidCommissions(){
	  jQuery("#openPaidCommissionDialog").dialog({title:"Paid Commissions Details"});
	  jQuery("#openPaidCommissionDialog").dialog("open");
}
 
 
 /** open dialog for Customer Invoice **/
 /*Edit by velmurugan*/
 function opencustomerinvoicedialog(){
	 	clearInvoiceDetailsBeforeOpen();
	 	var CIdivFlag="#CI_Shipto";
	 	document.getElementById("CuInvoiceSaveID").disabled = false;
		document.getElementById("CuInvoiceSaveCloseID").disabled = false;
		$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
		
	 	$('#paidCommissionID').css('display','none');
	 	$('#paymentDate').text('');
	 	$('#checkRefs').css('display','none');
	 	$('#dollarImage').html('');
		 if(jQuery("#release").getGridParam("records") === '0'){
			jQuery(newDialogDiv).html('<span><b style="color:red;">Please add one release in "Release Grid".</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		 }
		 
		 var ids = $("#shiping").jqGrid('getDataIDs'); 
		 /* Commented by Jenith
		  * $("#shiping").jqGrid("setSelection", ids[ids.length-1]);
		 var rowsId = $("#shiping").jqGrid('getGridParam', 'selrow');
		 
		 if($("#shiping").jqGrid('getCell', rowsId, 'vendorDate')==='' && $("#shiping").jqGrid ('getCell', rowsId, 'customerDate')!='' ){
			// alert('Jenith'+aVendorDate);
			 $("#shiping").jqGrid('resetSelection');
		 }*/
		 
		 var grid = $("#release");
		 var rowId = grid.jqGrid('getGridParam', 'selrow');
		 var releaseid=grid.jqGrid('getCell', rowId, 'joReleaseId');
		 var releaseType = grid.jqGrid('getCell', rowId, 'type');
		 whseID = grid.jqGrid('getCell', rowId, 'type');
		// allocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
		 allocated = $("#unbilledamount").text();
		 allocated = allocated.replace(/[^0-9\.-]+/g,"");
		 if(Number(allocated) < 0)
			 allocated = 0;
		 
		 var grid = $("#shiping");
		 var rowId = grid.jqGrid('getGridParam', 'selrow');
		 var aCusotmerDate = grid.jqGrid('getCell', rowId, 'customerDate');
		 var aInvoiceDate = grid.jqGrid('getCell', rowId, 'invoiceDate');
		 var aShiptoMode = grid.jqGrid('getCell', rowId, 'shiptoMode');
			 
		 var joDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
		 var shipViaID = grid.jqGrid('getCell', rowId, 'veShipViaID');
		
		 var aVendorDate = grid.jqGrid('getCell', rowId, 'vendorDate');
		 
		 
		 var aCusotmerDate='';
		 var joDetailId='';
		 var shipViaID='';
		 var cusoInvId ='';
		 var count = $("#shiping").getGridParam("reccount");
		 
		 if(count>0 && rowId!=null){
			 aCusotmerDate =$("#shiping").jqGrid ('getCell', rowId, 'customerDate');
			 joDetailId = $("#shiping").jqGrid('getCell', rowId, 'joReleaseDetailid');
			 shipViaID = $("#shiping").jqGrid('getCell', rowId, 'veShipViaID');
			 cusoInvId = $("#shiping").jqGrid('getCell', rowId, 'cuInvoiceID');
			 aInvoiceDate = $("#shiping").jqGrid('getCell', rowId, 'invoiceDate');
			 aShiptoMode = $("#shiping").jqGrid('getCell', rowId, 'shiptoMode');
		 }
		 
		 
		 $('#cuinvoiceIDhidden').val(cusoInvId);
		 getCustomerInvoiceDetailsforpopup(cusoInvId);
		 	 if(aCusotmerDate === ''){
		 		$('#imgInvoicePDF').empty();
		 		$('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new_disabled.png" title="View CuInvoice" return false;" style="background: #EEDEBC;cursor:default;">');

				$('#imgInvoiceEmail').empty();
				$('#imgInvoiceEmail').append('<input id="contactEmailID" type="image" src="./../resources/Icons/mail_new_disabled.png" title="Email Customer Invoice" style="background: #EEDEBC;cursor:default;" return false;">');
		 		console.log("1");
				 	errorText = "Invoice not found, do you wish to create a new customer invoice for this release?";
				 	if(count>0 && rowId==null){
				 		errorText ="Found Invoice(s), do you wish to create a new customer invoice for this release? or open existing Invoice click 'No' and  please select a row!";
				 	}
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:350, height:180, title:"Information.",
					buttons:{
						"Yes": function(){
							
							jQuery(this).dialog("close");
							$("#customerInvoie_doNotMailID").prop('checked',false);
							var billnoterowid=$("#release").jqGrid('getGridParam', 'selrow');
							var billNotes = $("#release").jqGrid('getCell', billnoterowid, 'billNote');
							if(billNotes.length>0){
								
							jQuery(newDialogDiv).attr("id","msgDlg");
							jQuery(newDialogDiv).html('<span>'+billNotes+'</span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Billing Instructions",
							buttons:{
								"OK": function(){
									console.log('With in BillNote Condition')
									
									jQuery(this).dialog("close");
									 $(".ui-dialog-titlebar-close ui-corner-all").css("display", "none");
									
									var aCustomerName = $(".customerNameField").val();
									var aCustomerID = $("#JobCustomerId").val();
									PreloadDataInvoice("CI_Shipto");
									$("#customerInvoice_customerInvoiceID").val(aCustomerName);
									$("#customerInvoice_customerHiddnID").val(aCustomerID);
									$("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
									$("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
									$("#customerInvoice_invoiceDateID").val('');
									$("#customerInvoice_invoiceDateID").val(currenDate);
									
									$("#customerInvoice_dueDateID").val('');
									$("#customerInvoice_dueDateID").val(currenDate);
									$("#customerInvoice_lineshipDateID").val('');
									$("#customerInvoice_lineshipDateID").val(currenDate);
									$("#customerInvoice_lineinvoiceDateID").val('');
									$("#customerInvoice_lineinvoiceDateID").val(currenDate);
									
									$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
//									$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
									console.log("aCusotmerDate Empty  ::: "+$('#rxCustomer_ID').text());
									 loadEmailList($('#rxCustomer_ID').text());
									 
									// invoiceshipToAddress();
									 invoicebillToAddress();
									// addressToShipCustomerInvoice()
									 $("#lineshipTo1").hide();
									 $('#cusinvoicetab').dialog('option', 'title', '');
									 $('#cusinvoicetab').dialog('option', 'title', 'New Customer Invoice');
									 jQuery("#cusinvoicetab").dialog("open");
									 $("a.ui-dialog-titlebar-close.ui-corner-all").css("display", "none");
									 $( "#cusinvoicetab ul li" ).first().addClass( "ui-tabs-selected ui-state-active" );
									 $( "#cusinvoicetab ul li:nth-child(2)" ).addClass("ui-state-default ui-corner-top ui-state-disabled");
									 
									 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
									 
									  if(releaseType=='Bill Only'){/*
										  alert('tests Typw##-70'+releaseType);
									 $('#customerInvoice_frightIDcu').val(formatCurrency(0));
									//2015-07-27 $('#customerInvoice_linefrightID').val(formatCurrency(0));
									 var customerInvoice_generaltaxId=$('#customerInvoice_generaltaxId').val();
									 var taxamount=(parseFloat(allocated)*parseFloat(customerInvoice_generaltaxId)/100);
									 console.log('tax & value in Billnotes if:'+customerInvoice_generaltaxId+' '+taxamount);
									 $('#customerInvoice_taxIdcu').val(formatCurrency(taxamount));
									// var total=taxamount+parseFloat(allocated);
									// $('#customerInvoice_totalID').val(formatCurrency(total));
									 boolean = false;
									 */

									 $('#customerInvoice_frightIDcu').val(formatCurrency(0));
									//2015-07-27  $('#customerInvoice_linefrightID').val(formatCurrency(0));
									 var customerInvoice_generaltaxId=$('#customerInvoice_generaltaxId').val();
									 var taxamount=(parseFloat(allocated)*parseFloat(customerInvoice_generaltaxId)/100);
									 $('#customerInvoice_taxIdcu').val(formatCurrency(taxamount));
									 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.-]+/g,"");
									 
									 var taxpercent=$('#customerInvoice_generaltaxId').val().replace(/[^0-9\.-]+/g,"");
									 if(taxpercent==null || taxpercent=="" ||taxpercent==undefined){
										 taxpercent=0.00;
									 }
									 if(allocated==null || allocated=="" || allocated==undefined){
										 allocated=0.00;
									 }
									 taxpercent=parseFloat(allocated)*parseFloat(taxpercent)/100;
									 var total=parseFloat(allocated)+parseFloat(freight)+parseFloat(taxpercent);
									 $('#customerInvoice_totalID').val(formatCurrency(total));
									//2015-07-27  $('#customerInvoice_linetotalID').val(formatCurrency(total));
									 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
									//2015-07-27 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
									 loadotherDetails();
									 //jobsiteinvoiceShiptoAddress();
									 $(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val("");
									 $(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val("");
									 $(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val(2);
									 preloadShiptoAddress("#CI_Shipto",'',null,'2','0',$("#jobCustomerName_ID").text(),"");
									 $("#CI_Shipto").contents().find("#shiptomoderhiddenid").val('2');
									 boolean = false;
										   
									  }
									}
							}}).dialog("open");
							
							}else{
								
								
 								 var aCustomerName = $(".customerNameField").val();
								 var aCustomerID = $("#JobCustomerId").val();
								 PreloadDataInvoice("CI_Shipto");
								 $("#customerInvoice_customerInvoiceID").val(aCustomerName);
								 $("#customerInvoice_customerHiddnID").val(aCustomerID);
								 $("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
								 $("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
								 $("#customerInvoice_invoiceDateID").val('');
								 $("#customerInvoice_invoiceDateID").val(currenDate);
//								 $("#customerInvoice_shipDateID").val('');
//								 $("#customerInvoice_shipDateID").val(currenDate);
								 $("#customerInvoice_dueDateID").val('');
								 $("#customerInvoice_dueDateID").val(currenDate);
								 $("#customerInvoice_lineshipDateID").val('');
								 $("#customerInvoice_lineshipDateID").val(currenDate);
								 $("#customerInvoice_lineinvoiceDateID").val('');
								 $("#customerInvoice_lineinvoiceDateID").val(currenDate);
								 $("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
								 $("#lineshipTo1").hide();
								 $('#cusinvoicetab').dialog('option', 'title', '');
								 $('#cusinvoicetab').dialog('option', 'title', 'New Customer Invoice');
								 jQuery("#cusinvoicetab").dialog("open");
							/*	 $('#customerbillToAddressID1cuInvoice').val("tsdsdf");
									//	alert($('#customerbillToAddressID1cuInvoice').val());
										$('#customerbillToAddressID2cuInvoice').val("sdafasdf");
										$('#customerbillToCitycuInvoice').val("werwe");
										$('#customerbillToStatecuInvoice').val("wersdf");
										$('#customerbillToZipIDcuInvoice').val("werxxsd");*/
								 $("a.ui-dialog-titlebar-close.ui-corner-all").css("display", "none");
								 $( "#cusinvoicetab ul li" ).first().addClass( "ui-tabs-selected ui-state-active" );
								 $( "#cusinvoicetab ul li:nth-child(2)" ).addClass("ui-state-default ui-corner-top ui-state-disabled");
								 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
								 loadEmailList($('#rxCustomer_ID').text());
								// invoiceshipToAddress();
								 invoicebillToAddress();
								 
								 //The below code for only billonly type
								 if(releaseType=='Bill Only'){
									 $('#customerInvoice_frightIDcu').val(formatCurrency(0));
									//2015-07-27  $('#customerInvoice_linefrightID').val(formatCurrency(0));
									 var customerInvoice_generaltaxId=$('#customerInvoice_generaltaxId').val();
									 var taxamount=(parseFloat(allocated)*parseFloat(customerInvoice_generaltaxId)/100);
									 $('#customerInvoice_taxIdcu').val(formatCurrency(taxamount));
									 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.-]+/g,"");
									 
									 var taxpercent=$('#customerInvoice_generaltaxId').val().replace(/[^0-9\.-]+/g,"");
									 if(taxpercent==null || taxpercent=="" ||taxpercent==undefined){
										 taxpercent=0.00;
									 }
									 if(allocated==null || allocated=="" || allocated==undefined){
										 allocated=0.00;
									 }
									 taxpercent=parseFloat(allocated)*parseFloat(taxpercent)/100;
									 var total=parseFloat(allocated)+parseFloat(freight)+parseFloat(taxpercent);
									 $('#customerInvoice_totalID').val(formatCurrency(total));
									//2015-07-27  $('#customerInvoice_linetotalID').val(formatCurrency(total));
									 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
									//2015-07-27 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
									 loadotherDetails();
									 //jobsiteinvoiceShiptoAddress();
									 $(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val("");
									 $(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val("");
									 $(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val(2);
									 preloadShiptoAddress("#CI_Shipto",'',null,'2','0',$("#jobCustomerName_ID").text(),"");
									 $("#CI_Shipto").contents().find("#shiptomoderhiddenid").val('2');
									 boolean = false;
								 }else{
									 $('#customerInvoice_frightIDcu').val(formatCurrency(0));
									//2015-07-27  $('#customerInvoice_linefrightID').val(formatCurrency(0));
									 console.log('CUI:'+$('#customerInvoice_generaltaxId').val());
									 var customerInvoice_generaltaxId=$('#customerInvoice_generaltaxId').val();
									 var taxamount=(parseFloat(allocated)*parseFloat(customerInvoice_generaltaxId)/100);
									 $('#customerInvoice_taxIdcu').val(formatCurrency(taxamount));
									 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.-]+/g,"");
									 
									 var taxpercent=$('#customerInvoice_generaltaxId').val().replace(/[^0-9\.-]+/g,"");
									 if(taxpercent==null || taxpercent=="" ||taxpercent==undefined){
										 taxpercent=0.00;
									 }
									 if(allocated==null || allocated=="" || allocated==undefined){
										 allocated=0.00;
									 }
									 taxpercent=parseFloat(allocated)*parseFloat(taxpercent)/100;
									 var total=parseFloat(allocated)+parseFloat(freight)+parseFloat(taxpercent);
									 console.log('tax & value else Billnots and billonly:'+customerInvoice_generaltaxId+' '+taxamount);
									 if(count<=0){
									 $('#customerInvoice_totalID').val(formatCurrency(total));
									 //2015-07-27 $('#customerInvoice_linetotalID').val(formatCurrency(total));
									 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
									 //2015-07-27 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
									 }
									 else if(count==1 && ($("#shiping").jqGrid ('getCell', rowId, 'veBillID'))>0){
										 $('#customerInvoice_totalID').val(formatCurrency(total));
										//2015-07-27 $('#customerInvoice_linetotalID').val(formatCurrency(total));
										 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
										//2015-07-27 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
									 }
									else{
										 console.log('Shiping Grid size > 0');
										 $('#customerInvoice_totalID').val(formatCurrency(0));
										//2015-07-27 $('#customerInvoice_linetotalID').val(formatCurrency(0));
										 $('#customerInvoice_subTotalID').val(formatCurrency(0));
										//2015-07-27 $('#customerInvoice_linesubTotalID').val(formatCurrency(0));
									 }
									 
									 invoiceshipToAddress();
									 
									
								 }
							}
							
						},
					"No": function ()	{
						jQuery(this).dialog("close");
					}
				}}).dialog("open");
			}else{
				$('#imgInvoicePDF').empty();
		 		$('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

				$('#imgInvoiceEmail').empty();
				$('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');
				
				
				
				var flag=0;
				var duplicate=0;
				var commissionDate='';
				var invoicePaidDate = '';
				var commissionPaid ="\n";
				var joReleaseDetailID=0;
				var cuInvoiceID=0;
				var commPaidArray = [];
				var commPaidDetail = [];
				joReleaseDetailID = $("#shiping").jqGrid('getCell', rowId, 'joReleaseDetailID');
				cuInvoiceID = $("#shiping").jqGrid('getCell', rowId, 'cuInvoiceID');
				$.ajax({
					url: "./jobtabs5/getCommissionPaidDetails",
					type: "POST",
					data : {"joReleaseDetailID": joReleaseDetailID,"joCuInvoiceID":cuInvoiceID},
					success: function(data) {
						/*$.each(data, function(index, value){
							console.log('EcStatementID:'+value.ecStatementId);
							if(value.ecStatementId!=null){
								flag=1;
								$('#paidCommissionID').css('display','block');
								commPaidArray.push({repsName: value.repName});
								commissionDate = value.calculatedDate;
							}
						});
					/*	for(var i=0;i<commPaidArray.length;i++) {
							for(var j=0;j<commPaidArray.length;j++) {
								if(commPaidArray[i].repsName===commPaidArray[j].repsName){
									commissionPaid=commissionPaid+commPaidArray[i].repsName+"<br/>";
								}else{
									commissionPaid=commissionPaid+commPaidArray[i].repsName+"<br/>";
								}
							}
						}*/
						if(data.indexOf("#")==-1){
						if(data=='YY'){
							$('#commissionMsg').text("Invoice has a balance due.");
							$('#paidCommissionID').css('display','block');
						}
						if(data=='NN'){
							$('#commissionMsg').text("Invoice has no open balance.");
							$('#paidCommissionID').css('display','block');
							$('#commissionLabel').text('Commission Pending');
						}
						if(data.indexOf('CP') > -1){
							$('#commissionMsg').text("Commissions Paid");
							$('#paidCommissionID').css('display','block');
							$('#commissionLabel').text('CommissionPaid');
						}
						if(data=='CPN'){
							$('#commissionMsg').text("Commissions have not been paid.");
							$('#paidCommissionID').css('display','block');
							$('#commissionLabel').text('CommissionNotPaid');
						}
						}
						if(data.indexOf("#") > -1){
							commPaidArray = data.split('#');
							for(var i=0;i<commPaidArray.length;i++) {
								if(commPaidArray[i].indexOf('-')>-1){
									$('#commissionLabel').text('CommissionPaid');
									$('#commissionMsg').text(commPaidArray[i]);
								}
								if(commPaidArray[i].indexOf('PN')>-1){
									$('#paidCommissionID').css('display','block');
									$('#commissionLabel').text('Commission Pending');
									$('#commissionMsg').text("Commissions have not been paid.");
								}
								if(commPaidArray[i].indexOf('@')>-1){
									commPaidDetail = commPaidArray[i].split('@');
									for(var j=0;j<commPaidDetail.length;j++) {
										if(commPaidDetail[j].indexOf('CP')>-1){
											$('#paidCommissionID').css('display','block');
											$('#commissionLabel').text('CommissionPaid');
											$('#commissionMsg').text("Commissions paid as of :");
										}
										if(commPaidDetail[j].indexOf('-')>-1){
											$('#commissionPaidDate').text(formatDatez(commPaidDetail[j]));
										}
										$('#commissionPaidLabel').text('Commission Paid to:'+(commPaidDetail[commPaidDetail.length-1]));
									}
									
								}
								//$('#commissionMsg').text("Commissions have not been paid.");
								//$('#commissionPaidLabel').text('Commission Paid to:');
								//$('#commissionPaidDate').text(commissionDate);
								$('#commissionPaid').html(commissionPaid);
							}
						}
					}
				});
				// var grids = $("#shiping");
				// var rowIds = grids.jqGrid('getGridParam', 'selrow');
				// joReleaseDetailID = grids.jqGrid('getCell', rowIds, 'joReleaseDetailID');
				
				$.ajax({
					url: "./jobtabs5/getInvoicePaymentDate",
					type: "POST",
					data : {"joReleaseDetailID": joReleaseDetailID},
					success: function(data) {
					//	alert(data);
						if(data!="")
						{
						console.log('Incvoice Date:'+data);
							$('#invoicePaidLabel').text('Invoice Paid as of :');
							$('#invoicePaidDate').text(data);							
							$('#dollarImage').html('<img alt="search" src="./../resources/Icons/dollar.png">');
							$('#paymentDate').text(data+ ' (Invoice Paid)');
						}
							
					}
				});
				$.ajax({
					url: "./jobtabs5/getInvoiceCheckNos",
					type: "POST",
					data : {"cuInvoiceID": cuInvoiceID},
					success: function(data) {
						//alert('checkno'+data);
							if((data!=null && data!='') && data!='$$'){
								$('#checkRefs').css('display','inline');
								$('#checkRefs').text('Check#'+ data+' ');
							}
					}
				});
				
				console.log("Existed Invoice");
				 var aCustomerName = $(".customerNameField").val();
				 var aCustomerID = $("#JobCustomerId").val();
				 $("#customerInvoice_customerInvoiceID").val(aCustomerName);
				 $("#customerInvoice_customerHiddnID").val(aCustomerID);
				 $("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
				 $("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
				/* $("#customerInvoice_invoiceDateID").val('');
				$("#customerInvoice_invoiceDateID").val(currenDate);
				$("#customerInvoice_shipDateID").val('');
				$("#customerInvoice_shipDateID").val(currenDate);
				$("#customerInvoice_dueDateID").val('');
				$("#customerInvoice_dueDateID").val(currenDate);
				$("#customerInvoice_lineshipDateID").val('');
				$("#customerInvoice_lineshipDateID").val(currenDate);
				$("#customerInvoice_lineinvoiceDateID").val('');
				$("#customerInvoice_lineinvoiceDateID").val(currenDate);*/
				console.log("else part aCusotmer Invoice Date not Empty  : "+$('#rxCustomer_ID').text());
				//alert("--->"+$('#rxCustomer_ID').text());
				loadEmailList($('#rxCustomer_ID').text());
				console.log("shipViaID for Invoiced PO--->"+shipViaID);
			    //customerinvoiceShiptoAddress();
				
				
				/*Commented by velmurugan
				 * Ship to Implementation new Code
				 * 20-10-2015
				 * */
				/*jobsiteinvoiceShiptoAddress();
				if(aShiptoMode == 0)
				{
				
				console.log('Ship to Mode inside IF: '+aShiptoMode);
				usinvoiceShiptoAddress(cusoInvId,'cuinvoice');
				
				 $("#CiShiptoradio1").attr("Checked","Checked");
				 $('#CiShiptolabel1').css("font-weight","bold");
				 $('#CiShiptolabel2').css("font-weight","normal");
				 $('#CiShiptolabel3').css("font-weight","normal");
				 $('#CiShiptolabel4').css("font-weight","normal");
				 $('#CiShiptolabel1').addClass("ui-state-active");
				 $('#CiShiptolabel2').removeClass("ui-state-active");
				 $('#CiShiptolabel3').removeClass("ui-state-active");
				 $('#CiShiptolabel4').removeClass("ui-state-active");
				 
				}
				else if(aShiptoMode == 1)
				{
				customerinvoiceShiptoAddress(cusoInvId,'cuinvoice');
				}
				else if(aShiptoMode == 3)
				{
			//	$("#shipToCustomerAddressID").val()
				otherinvoiceShiptoAddress(cusoInvId,'cuinvoice')
				}
				else if(aShiptoMode == 2)
				{
				jobsiteinvoiceShiptoAddress(cusoInvId,'cuinvoice')
				}*/
				
				/*New Code Implementation*/
				/*New Customer Invoice Ship To Code Starts*/
				loadCUInvoice_ShipTO("#CI_Shipto",cusoInvId);
				
				
				$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
				$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
				 $("#lineshipTo1").hide();
				 $('#cusinvoicetab').dialog('option', 'title', '');
				 $('#cusinvoicetab').dialog('option', 'title', aInvoiceDate);
				 $('#ciOpenStatusID').val(false);
				/**
				 * Dev : Leo  Date:04/02/2015
				 * BugID: 281
				 * Description: After saving customer invoice, the invoice amount will be getting from shiping grid
				 * */
				 
				var cuInvoiceamtfmgrid = $("#shiping").jqGrid('getCell', rowId, 'customerAmount');
				 console.log("Allocated Amount is --->"+cuInvoiceamtfmgrid);
				 cuInvoiceamtfmgrid = cuInvoiceamtfmgrid.replace(/[^0-9\.]+/g,"");
				 
				 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.-]+/g,"");
				 
				 var taxpercent=$('#customerInvoice_generaltaxId').val().replace(/[^0-9\.-]+/g,"");
				 if(taxpercent==null || taxpercent=="" ||taxpercent==undefined){
					 taxpercent=0.00;
				 }
				 if(cuInvoiceamtfmgrid==null || cuInvoiceamtfmgrid=="" || cuInvoiceamtfmgrid==undefined){
					 cuInvoiceamtfmgrid=0.00;
				 }
				 taxpercent=parseFloat(cuInvoiceamtfmgrid)*parseFloat(taxpercent)/100;
				 $('#customerInvoice_taxIdcu').val(formatCurrency(taxpercent));
				 var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.-]+/g,"");			 
				 if(freight==null || freight=="" || freight==undefined){
					 freight=0.00;
				 }
				 if(cuInvoiceamtfmgrid==null || cuInvoiceamtfmgrid=="" || cuInvoiceamtfmgrid==undefined){
					 cuInvoiceamtfmgrid=0.00;
				 }
				 if(taxValue==null || taxValue=="" || taxValue==undefined){
					 taxValue=0.00;
				 }
				 var total=parseFloat(cuInvoiceamtfmgrid)+parseFloat(freight)+parseFloat(taxValue);
				 $('#customerInvoice_totalID').val(formatCurrency(total));
				//2015-07-27  $('#customerInvoice_linetotalID').val(formatCurrency(total));
				 
				 $('#customerInvoice_subTotalID').val(formatCurrency(cuInvoiceamtfmgrid));
				 $('#customerInvoice_linesubTotalID').val(formatCurrency(cuInvoiceamtfmgrid));
				 
				 iFlag = 2;
				 //var invoicenumber=$("#customerInvoice_lineinvoiceNumberId").val();
				 //$("#customerInvoice_invoiceNumberId").val(invoicenumber);
				 jQuery("#cusinvoicetab").dialog("open");
				 $("a.ui-dialog-titlebar-close.ui-corner-all").css("display", "none");
				 $( "#cusinvoicetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
				 
				 
				 setTimeout(function(){
					 	//_globaloldcustomerInvoiceformTotal=$("#custoemrInvoiceFormTotalID").serialize();
					 _globaloldcustomerInvoiceformTotal=generatecustoemrInvoiceFormTotalIDSeriallize();
					 	_globaloldcustomerInvoiceform = CIGeneralTabSeriallize();
					 	var gridRows = $('#customerInvoice_lineitems').getRowData();
					 	_globaloldcustomerInvoicegrid =  JSON.stringify(gridRows)+$("#customerInvoice_invoiceDateID").val();
						},3000);
				
			}
		 	 
		 	 
		 	 
		 return true;
	 }
 
 function formatDatez(createdDate){
		/*2003-02-18 00:00:00.0 ----------- YYYY-mm-dd*/
		if(createdDate === ""){
			return "";
		}
		var arr1 = createdDate.split(" ");
		var arr2 = arr1[0].split("-");
		var newDate = arr2[1] + "/" + arr2[2] + "/" + arr2[0];
		return newDate;
	}
 
 /*function opencustomerinvoicedialog(){
	// $('#createRebuildButton').css({"display": "block"});
	 
	// $('#createRebuildButton').css({"display": "block"});
	 if(jQuery("#release").getGridParam("records") === '0'){
		jQuery(newDialogDiv).html('<span><b style="color:red;">Please add one release in "Release Grid".</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	 } 
	 var grid = $("#release");
	 var rowId = grid.jqGrid('getGridParam', 'selrow');
	 var releaseType = grid.jqGrid('getCell', rowId, 'type');
	 whseID = grid.jqGrid('getCell', rowId, 'type');
	 allocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
	 allocated = allocated.replace(/[^0-9\.]+/g,"");
	 var grid = $("#shiping");
	 var rowId = grid.jqGrid('getGridParam', 'selrow');
	 var aCusotmerDate = grid.jqGrid('getCell', rowId, 'customerDate');
	 var joDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
	 var shipViaID = grid.jqGrid('getCell', rowId, 'veShipViaID');
	
	 var aCusotmerDate='';
	 var joDetailId='';
	 var shipViaID='';
	 for(var i=0;i<ids.length;i++){
		 rowId=ids[i];
		 aCusotmerDate =$("#shiping").jqGrid ('getCell', rowId, 'customerDate');
		 joDetailId = $("#shiping").jqGrid('getCell', rowId, 'joReleaseDetailid');
		 shipViaID = $("#shiping").jqGrid('getCell', rowId, 'veShipViaID');
		 if(aCusotmerDate!=''){
			break; 
		 }
	 }
	 
	 console.log("shipViaID from shipping Grid--->"+shipViaID+' :: Allocated AMT : '+allocated);
	 if(jQuery("#shiping").getGridParam("records") !== 0){
		 if(aCusotmerDate === ''){
			 console.log("1");
			 	errorText = "Invoice not found, do you wish to create a new customer invoice for this release?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					"Yes": function(){
						jQuery(this).dialog("close");
						
						var billnoterowid=$("#release").jqGrid('getGridParam', 'selrow');
						var billNotes = $("#release").jqGrid('getCell', billnoterowid, 'billNote');
						if(billNotes.length>0){
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b>'+billNotes+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Billing Instructions",
						buttons:{
							"OK": function(){
								jQuery(this).dialog("close");
								var aCustomerName = $(".customerNameField").val();
								 var aCustomerID = $("#JobCustomerId").val();
								 PreloadDataInvoice();
								 $("#customerInvoice_customerInvoiceID").val(aCustomerName);
								 $("#customerInvoice_customerHiddnID").val(aCustomerID);
								 $("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
								 $("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
								 $("#customerInvoice_invoiceDateID").val('');
								$("#customerInvoice_invoiceDateID").val(currenDate);
								$("#customerInvoice_shipDateID").val('');
								$("#customerInvoice_shipDateID").val(currenDate);
								$("#customerInvoice_dueDateID").val('');
								$("#customerInvoice_dueDateID").val(currenDate);
								$("#customerInvoice_lineshipDateID").val('');
								$("#customerInvoice_lineshipDateID").val(currenDate);
								$("#customerInvoice_lineinvoiceDateID").val('');
								$("#customerInvoice_lineinvoiceDateID").val(currenDate);
								$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
//								$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
								console.log("aCusotmerDate Empty  ::: "+$('#rxCustomer_ID').text());
								 loadEmailList($('#rxCustomer_ID').text());
								 invoiceshipToAddress();
								 invoicebillToAddress();
								 $("#lineshipTo1").hide();
								 $('#cusinvoicetab').dialog('option', 'title', '');
								 $('#cusinvoicetab').dialog('option', 'title', 'New Customer Invoice');
								 jQuery("#cusinvoicetab").dialog("open");
								 $( "#cusinvoicetab ul li" ).first().addClass( "ui-tabs-selected ui-state-active" );
								 $( "#cusinvoicetab ul li:nth-child(2)" ).removeClass().addClass("ui-state-default ui-corner-top");
								}
						}}).dialog("open");
						
						}
						
					},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
			}}).dialog("open");
		}else{
			console.log("3");
			 var aCustomerName = $(".customerNameField").val();
			 var aCustomerID = $("#JobCustomerId").val();
			 $("#customerInvoice_customerInvoiceID").val(aCustomerName);
			 $("#customerInvoice_customerHiddnID").val(aCustomerID);
			 $("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
			 $("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
			 $("#customerInvoice_invoiceDateID").val('');
			$("#customerInvoice_invoiceDateID").val(currenDate);
			$("#customerInvoice_shipDateID").val('');
			$("#customerInvoice_shipDateID").val(currenDate);
			$("#customerInvoice_dueDateID").val('');
			$("#customerInvoice_dueDateID").val(currenDate);
			$("#customerInvoice_lineshipDateID").val('');
			$("#customerInvoice_lineshipDateID").val(currenDate);
			$("#customerInvoice_lineinvoiceDateID").val('');
			$("#customerInvoice_lineinvoiceDateID").val(currenDate);
			console.log("else part aCusotmerDate Empty  : "+$('#rxCustomer_ID').text());
			loadEmailList($('#rxCustomer_ID').text());
			console.log("shipViaID for Invoiced PO--->"+shipViaID);
			customerinvoiceShiptoAddress();
			$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
			$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
			 $("#lineshipTo1").hide();
			 $('#cusinvoicetab').dialog('option', 'title', '');
			 $('#cusinvoicetab').dialog('option', 'title', aCusotmerDate);
			 console.log("Allocated Amount is --->"+allocated);
			 
			 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.]+/g,"");
			 
			 var taxpercent=$('#customerInvoice_generaltaxId').val().replace(/[^0-9\.]+/g,"");
			 if(taxpercent==null || taxpercent=="" ||taxpercent==undefined){
				 taxpercent=0.00;
			 }
			 if(allocated==null || allocated=="" || allocated==undefined){
				 allocated=0.00;
			 }
			 
			 
			 taxpercent=parseFloat(allocated)*parseFloat(taxpercent)/100;
			 $('#customerInvoice_taxIdcu').val(formatCurrency(taxpercent));
			 var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.]+/g,"");			 
			 if(freight==null || freight=="" || freight==undefined){
				 freight=0.00;
			 }
			 if(allocated==null || allocated=="" || allocated==undefined){
				 allocated=0.00;
			 }
			 if(taxValue==null || taxValue=="" || taxValue==undefined){
				 taxValue=0.00;
			 }
			 var total=parseFloat(allocated)+parseFloat(freight)+parseFloat(taxValue);
			 $('#customerInvoice_totalID').val(formatCurrency(total));
			 $('#customerInvoice_linetotalID').val(formatCurrency(allocated+freight+taxValue));
			 
			 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
			 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
			 iFlag = 2;
			
			 jQuery("#cusinvoicetab").dialog("open");
		}}else{
			console.log("2");
			 var errorText = "Invoice not found, do you wish to create a new customer invoice for this release?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					"Yes": function(){
						jQuery(this).dialog("close");
						var billnoterowid=$("#release").jqGrid('getGridParam', 'selrow');
						var billNotes = $("#release").jqGrid('getCell', billnoterowid, 'billNote');
						if(billNotes.length>0){
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b>'+billNotes+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Billing Instructions",
						buttons:{
							"OK": function(){
								jQuery(this).dialog("close");
								PreloadDataInvoice();
								jQuery(this).dialog("close");
								var aCustomerName = $(".customerNameField").val();
								var aCustomerID = $("#JobCustomerId").val();
								$("#customerInvoice_customerInvoiceID").val(aCustomerName);
								$("#customerInvoice_customerHiddnID").val(aCustomerID);
								$("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
								$("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
								$("#customerInvoice_invoiceDateID").val('');
								$("#customerInvoice_invoiceDateID").val(currenDate);
								$("#customerInvoice_shipDateID").val('');
								$("#customerInvoice_shipDateID").val(currenDate);
								$("#customerInvoice_dueDateID").val('');
								$("#customerInvoice_dueDateID").val(currenDate);
								$("#customerInvoice_lineshipDateID").val('');
								$("#customerInvoice_lineshipDateID").val(currenDate);
								$("#customerInvoice_lineinvoiceDateID").val('');
								$("#customerInvoice_lineinvoiceDateID").val(currenDate);
								$("#shipViaCustomerSelectlineID option[value="+ shipViaID + "]").attr("selected", true);
								$("#shipViaCustomerSelectID option[value="+ shipViaID + "]").attr("selected", true);
								$("#lineshipTo1").hide();
								$('#cusinvoicetab').dialog('option','title', '');
								$('#cusinvoicetab').dialog('option','title', 'New Customer Invoice');
								console.log("Allocated Amount is --->"+allocated);
								 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.]+/g,"");
								 var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.]+/g,"");			 
								 
								 $('#customerInvoice_totalID').val(formatCurrency(allocated+freight+taxValue));
								 
								 $('#customerInvoice_linetotalID').val(formatCurrency(allocated+freight+taxValue));
								 
								 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
								 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
								 iFlag = 2;
								jQuery("#cusinvoicetab").dialog("open");
							}
						}}).dialog("open");
						}else{
							PreloadDataInvoice();
							jQuery(this).dialog("close");
							var aCustomerName = $(".customerNameField").val();
							var aCustomerID = $("#JobCustomerId").val();
							$("#customerInvoice_customerInvoiceID").val(aCustomerName);
							$("#customerInvoice_customerHiddnID").val(aCustomerID);
							$("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
							$("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
							$("#customerInvoice_invoiceDateID").val('');
							$("#customerInvoice_invoiceDateID").val(currenDate);
							$("#customerInvoice_shipDateID").val('');
							$("#customerInvoice_shipDateID").val(currenDate);
							$("#customerInvoice_dueDateID").val('');
							$("#customerInvoice_dueDateID").val(currenDate);
							$("#customerInvoice_lineshipDateID").val('');
							$("#customerInvoice_lineshipDateID").val(currenDate);
							$("#customerInvoice_lineinvoiceDateID").val('');
							$("#customerInvoice_lineinvoiceDateID").val(currenDate);
							$("#shipViaCustomerSelectlineID option[value="+ shipViaID + "]").attr("selected", true);
							$("#shipViaCustomerSelectID option[value="+ shipViaID + "]").attr("selected", true);
							$("#lineshipTo1").hide();
							$('#cusinvoicetab').dialog('option','title', '');
							$('#cusinvoicetab').dialog('option','title', 'New Customer Invoice');
							console.log("Allocated Amount is --->"+allocated);
							 var freight = $('#customerInvoice_frightIDcu').val().replace(/[^0-9\.]+/g,"");
							 var taxValue = $('#customerInvoice_taxIdcu').val().replace(/[^0-9\.]+/g,"");			 
							 
							 $('#customerInvoice_totalID').val(formatCurrency(allocated+freight+taxValue));
							 
							 $('#customerInvoice_linetotalID').val(formatCurrency(allocated+freight+taxValue));
							 
							 $('#customerInvoice_subTotalID').val(formatCurrency(allocated));
							 $('#customerInvoice_linesubTotalID').val(formatCurrency(allocated));
							 iFlag = 2;
							jQuery("#cusinvoicetab").dialog("open");
						}
					},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
		}}).dialog("open");
	 }
	 
	 return true;
 }*/
 
 $(function(){
	 $("span#myCloseIcon").click(function() {
			
	        $( "#cusinvoicetab" ).dialog( "close" );
	    }); 
 });
function cancelcustomerinvoice(){
	var releasrowid =  $("#release").jqGrid('getGridParam', 'selrow');
	$("#release").trigger("reloadGrid");
	//$("#release").jqGrid('setSelection', releasrowid, true);
	//$("#shiping").trigger("reloadGrid");
	var rowDataHiddenID = $("#release").jqGrid('getCell',releasrowid, 'joReleaseId');
	 jQuery('#shiping').jqGrid('clearGridData')
	 loadShipingGrid(rowDataHiddenID,"");
	jQuery("#cusinvoicetab").dialog("close");
}

function customershipBackWard(){
	$("#lineshipTo1").hide();
	$("#lineshipTo").show();
}

function customershipForWard(){
	$("#lineshipTo1").show();
	$("#lineshipTo").hide();
}

function usinvoiceShiptoAddress(dataID,tablename){
	console.log('inside jobwizardRelease.js usinvoiceShiptoAddress() '+dataID+" Table Name:"+tablename);
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	
	 var rxMasterId = $("#shipToCustomerAddressID").val();
		console.log('Jenith rxMasterID-1:'+rxMasterId);
		if((rxMasterId === null || rxMasterId === '') || typeof rxMasterId == "undefined")
			 {
			 rxMasterId = $('#rxCustomer_ID').text();
			 console.log('Jenith rxMasterID-2:'+rxMasterId);
			 }
		 if(rxMasterId===null || rxMasterId===""){
			 rxMasterId = $("#customerInvoice_customerHiddnID").val();
			 console.log('Jenith rxMasterID-3:'+rxMasterId);
			}
		 if(rxMasterId===null || rxMasterId===""){
			 rxMasterId = $("#rxMasterIDfrompage").val();
			 console.log('Jenith rxMasterID-4:'+rxMasterId);
			}
		 if(rxMasterId=== null || rxMasterId===""){
			 rxMasterId = $('#rxCustomerID').val();
			 console.log('Jenith rxMasterID-5:'+rxMasterId);
		 }
		 if(rxMasterId=== null || rxMasterId===""){
				rxMasterId=0;
				 console.log('Jenith rxMasterID-6:'+rxMasterId);
			}
		console.log('Jenith rxMasterID=7:'+rxMasterId);
			 $.ajax({
					url: "./salesOrderController/getCustomerDetails",
					type: "GET",
					data : {"customerID" : rxMasterId},
					success: function(data) {
						console.log('Jenith CD:'+data.name);
						customerName=data.name;
						}
				});
	 
	 if(tablename=='cuinvoice'){
		 $.ajax({
				url: "./salesOrderController/getPreInvoiceData",
				type: "POST",
				data : "&cuInvoiceId="+dataID+"&rxMasterID=0",
				success: function(data) {
					console.log('Jenith Your Data: '+data.cuInvoice.rxShipToAddressId);
					 loadShipToAddressing(data.cuInvoice.rxShipToAddressId);
					 if(data.cuInvoice!=null && data.cuInvoice.coTaxTerritoryId!=null && data.cuInvoice.coTaxTerritoryId!="" && data.cuInvoice.coTaxTerritoryId>0){
							loadTaxTerritoryRateforinsideJob(data.cuInvoice.coTaxTerritoryId);
						}
					}
				});
		 
		}
	if(tablename=='cuso'){
	 $.ajax({
		 url: "./salesOrderController/getPreLoadData",
			type: "POST",
			data : "&cuSOID="+dataID+"&rxMasterID=0&joMasterID="+$("#joMaster_ID").text(),
			//data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
			success: function(data) {
				console.log('Jenith Your Data: '+data.Cuso.prToWarehouseId);
				 loadShipToAddressing(data.Cuso.prToWarehouseId);
				}
			});
	}
	else if(tablename=='vepo'){
		 
		 $.ajax({
			 url: "./salesOrderController/getPreLoadData",
				type: "POST",
				data : "&vePOID="+dataID+"&rxMasterID=0&joMasterID="+$("#joMaster_ID").text(),
				//data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
				success: function(data) {
					console.log('Jenith Your Data: '+data.vepo.prWarehouseId);
					 loadShipToAddressing(data.vepo.prWarehouseId);
					}
				});
		}
	else{
		 loadShipToAddressing('');
	}
	$('#shiptoModeID').val(0);
	 $("#CiShiptoradio1").attr("Checked","Checked");
	 $('#CiShiptolabel1').css("font-weight","bold");
	 $('#CiShiptolabel2').css("font-weight","normal");
	 $('#CiShiptolabel3').css("font-weight","normal");
	 $('#CiShiptolabel4').css("font-weight","normal");
	 $('#CiShiptolabel1').addClass("ui-state-active");
	 $('#CiShiptolabel2').removeClass("ui-state-active");
	 $('#CiShiptolabel3').removeClass("ui-state-active");
	 $('#CiShiptolabel4').removeClass("ui-state-active");
	 return true;
}

function customerinvoiceShiptoAddress(ids,type){
	
	var urlValue = "";
	var rxMasterId = $("#shipToCustomerAddressID").val();
	
	if(type == "cuinvoice")
	{
			if($("#shipToCustomerAddressID").val()== $('#rxCustomer_ID').text() )
			{
			urlValue = "./jobtabs3/getBilltoAddress?customerID="+$('#rxCustomer_ID').text()+"&oper=ship";
			rxMasterId = $('#rxCustomer_ID').text();
			}
			else
			{
			urlValue = "./salesOrderController/getCustomerShipToAddressforSO?customerID="+$("#shipToCustomerAddressID").val();
			 rxMasterId = $("#shipToCustomerAddressID").val();
			}
	}
	else
	  {
		var gridrel = $("#release");
		var rowIdrel = gridrel.jqGrid('getGridParam', 'selrow');
		var rel_type = gridrel.jqGrid('getCell', rowIdrel, 'type');
		
			if(rel_type == "Drop Ship" || rel_type == "Commission")
			{
			
				if($("#prShiptowarehouseID").val()!=null)
				{
					if($('#rxCustomer_ID').text() ==  $('#prShiptowarehouseID').val())
					{
					urlValue = "./jobtabs3/getBilltoAddress?customerID="+$('#rxCustomer_ID').text()+"&oper=ship";
					rxMasterId = $('#rxCustomer_ID').text();
					}
					else
					{
					urlValue = "./salesOrderController/getCustomerShipToAddressforSO?customerID="+$('#prShiptowarehouseID').val();
					rxMasterId = $('#prShiptowarehouseID').val();
					}
				}	
				else
				{
					urlValue = "./jobtabs3/getBilltoAddress?customerID="+$('#rxCustomer_ID').text()+"&oper=ship";
					rxMasterId = $('#rxCustomer_ID').text();	
				}
			
			
			}
			else
			{
				if(rxMasterId == null || rxMasterId == '')
				 {
					urlValue = "./jobtabs3/getBilltoAddress?customerID="+$('#rxCustomer_ID').text()+"&oper=ship";
					rxMasterId = $('#rxCustomer_ID').text();
				 }
			 	 else
				 {
		 		 	if($("#shipToCustomerAddressID").val()== $('#rxCustomer_ID').text() )
		 			{
		 			urlValue = "./jobtabs3/getBilltoAddress?customerID="+$('#rxCustomer_ID').text()+"&oper=ship";
					rxMasterId = $('#rxCustomer_ID').text();
		 			}
		 			else
		 			{
		 			urlValue = "./salesOrderController/getCustomerShipToAddressforSO?customerID="+$("#shipToCustomerAddressID").val();
		 			rxMasterId = $("#shipToCustomerAddressID").val();
		 			}
				 }
			}
		
	  }
	
		
	console.log('Jenith rxMasterID=7:'+rxMasterId);
		 $.ajax({
				url: "./salesOrderController/getCustomerDetails",
				type: "GET",
				data : {"customerID" : rxMasterId},
				success: function(data) {
					console.log('Jenith CD:'+data.name);
					customerName=data.name;
					}
			});
	//alert("in Release:"+ids+"  "+type);
	console.log('inside jobwizardRelease.js customerinvoiceShiptoAddress');
	$("#cuinvoiceUs").hide();
	$('#shiptoModeID').val(1);
	//console.log('customerinvoiceShiptoAddress()');
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 
	 
	 $.ajax({
			url: urlValue,
			type: "GET",
			//data : {"customerID" : rxMasterId},
			success: function(data) {
					var locationName = $("#jobCustomerName_ID").text();
					var rxAddressId = data.rxAddressId;
					var locationAddress1 = data.address1;
					var locationAddress2 = data.address2;
					var locationCity = data.city;
					var locationState = data.state;
					var locationZip = data.zip;
					var name = data.name;
					var coTaxID = data.coTaxTerritoryId;
					//alert(coTaxID);
					loadTaxTerritoryRateforinsideJob(coTaxID);
					console.log(rxAddressId+" :: "+locationAddress1+" :: "+locationAddress2+" :: "+locationCity+" :: "+locationState+" :: "+locationZip+" :: "+name);
					$("#prShiptowarehouseID").val(rxAddressId); 
					console.log('addressID:'+$("#prShiptowarehouseID").val());
					$("#rxAddressShipID").val(rxAddressId); 
					$("#customerShipToAddressID").val(name); $("#customerShipToAddressID1").val(locationAddress1); 
					$("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
					$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
					document.getElementById('customerShipToAddressID').disabled=false;
					document.getElementById('customerShipToAddressID1').disabled=true;
					document.getElementById('customerShipToAddressID2').disabled=true;
					document.getElementById('customerShipToCity').disabled=true;
					document.getElementById('customerShipToState').disabled=true;
					document.getElementById('customerShipToZipID').disabled=true;
					}
				});
		 $("#CiShiptoradio2").attr("Checked","Checked");
		 $('#CiShiptolabel2').css("font-weight","bold");
		 $('#CiShiptolabel1').css("font-weight","normal");
		 $('#CiShiptolabel3').css("font-weight","normal");
		 $('#CiShiptolabel4').css("font-weight","normal");
		 $('#CiShiptolabel2').addClass("ui-state-active");
		 $('#CiShiptolabel1').removeClass("ui-state-active");
		 $('#CiShiptolabel3').removeClass("ui-state-active");
		 $('#CiShiptolabel4').removeClass("ui-state-active");
		 
		 if(ids!=null){
			 if(type=='cuinvoice')
			 {
				 $.ajax({
						url : "./salesOrderController/getPreInvoiceData",
						type : "POST",
						data : "&cuInvoiceId=" + ids + "&rxMasterID=0",
						success : function(data) {
							
							if(data.cuInvoice!=null && data.cuInvoice.coTaxTerritoryId!=null && data.cuInvoice.coTaxTerritoryId!="" && data.cuInvoice.coTaxTerritoryId>0){
								//alert(data.cuInvoice.coTaxTerritoryId);
								loadTaxTerritoryRateforinsideJob(data.cuInvoice.coTaxTerritoryId);
							}
						}
					});
				 
			 }
		 }
		 
		 
		 return true;
}
function jobsiteinvoiceShiptoAddress(joReleaseID,type){
	
	
	var rxMasterId = $("#shipToCustomerAddressID").val();
	console.log('Jenith rxMasterID-1:'+rxMasterId);
	if((rxMasterId === null || rxMasterId === '') || typeof rxMasterId == "undefined")
		 {
		 rxMasterId = $('#rxCustomer_ID').text();
		 console.log('Jenith rxMasterID-2:'+rxMasterId);
		 }
	 if(rxMasterId===null || rxMasterId===""){
		 rxMasterId = $("#customerInvoice_customerHiddnID").val();
		 console.log('Jenith rxMasterID-3:'+rxMasterId);
		}
	 if(rxMasterId===null || rxMasterId===""){
		 rxMasterId = $("#rxMasterIDfrompage").val();
		 console.log('Jenith rxMasterID-4:'+rxMasterId);
		}
	 if(rxMasterId=== null || rxMasterId===""){
		 rxMasterId = $('#rxCustomerID').val();
		 console.log('Jenith rxMasterID-5:'+rxMasterId);
	 }
	 if(rxMasterId=== null || rxMasterId===""){
			rxMasterId=0;
			 console.log('Jenith rxMasterID-6:'+rxMasterId);
		}
	console.log('Jenith rxMasterID=7:'+rxMasterId);
		 $.ajax({
				url: "./salesOrderController/getCustomerDetails",
				type: "GET",
				data : {"customerID" : rxMasterId},
				success: function(data) {
					console.log('Jenith CD:'+data.name);
					customerName=data.name;
					}
			});
	console.log('inside jobwizardRelease.js jobsiteinvoiceShiptoAddress');
	$("#cuinvoiceUs").hide();
	$('#shiptoModeID').val(2);
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite_select.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 
	 if(type=='cuinvoice'){
		$.ajax({
					url : "./salesOrderController/getPreInvoiceData",
					type : "POST",
					data : "&cuInvoiceId=" + joReleaseID + "&rxMasterID=0",
					success : function(data) {
						console.log('Jenith joReleaseDetailID from cuInvoice: '
								+ data.cuInvoice.joReleaseDetailId);
						//hello i am changes
						
						$.ajax({
									url : "./salesOrderController/getJobDetailsFromReleaseDetail",
									type : "POST",
									async:false,
									data : {
										"joReleasedetailID" : data.cuInvoice.joReleaseDetailId
									},
									success : function(data) {
										
									
										$("#customerShipToAddressID").val(CustomName);
										$("#customerShipToAddressID1").val(data.locationAddress1);
										$("#customerShipToAddressID2").val(data.locationAddress2);
										$("#customerShipToCity").val(data.locationCity);
										$("#customerShipToState").val(data.locationState);
										$("#customerShipToZipID").val(data.locationZip);
									}
								});
						if(data.cuInvoice!=null && data.cuInvoice.coTaxTerritoryId!=null && data.cuInvoice.coTaxTerritoryId!="" && data.cuInvoice.coTaxTerritoryId>0){
							loadTaxTerritoryRateforinsideJob(data.cuInvoice.coTaxTerritoryId);
						}
					}
				});
				}
		 else if(type=='vepo' || type=='cuso'){
			 $.ajax({
					url: "./salesOrderController/getJobDetailsFromRelease",
					type: "POST",
					data : {"joReleaseID" : joReleaseID,"joMasterID":$("#joMaster_ID").text()},
					success: function(data) {	
						 $("#customerShipToAddressID").val(CustomName); 
						 $("#customerShipToAddressID1").val(data.locationAddress1); 
						 $("#customerShipToAddressID2").val(data.locationAddress2); 
						 $("#customerShipToCity").val(data.locationCity);
						 $("#customerShipToState").val(data.locationState); 
						 $("#customerShipToZipID").val(data.locationZip);
					}
		 });
		 }
		 else{	 
			 $("#customerShipToAddressID").val(CustomName); $("#customerShipToAddressID1").val(jobLocation1); $("#customerShipToAddressID2").val(jobLocation2); $("#customerShipToCity").val(jobCity);
			 $("#customerShipToState").val(jobState); $("#customerShipToZipID").val(jobZip);
		 }
	    loadTaxTerritoryRateforinsideJob($("#TaxTerritory").val());
		document.getElementById('customerShipToAddressID').disabled=true;
		document.getElementById('customerShipToAddressID1').disabled=true;
		document.getElementById('customerShipToAddressID2').disabled=true;
		document.getElementById('customerShipToCity').disabled=true;
		document.getElementById('customerShipToState').disabled=true;
		document.getElementById('customerShipToZipID').disabled=true;
		 $("#CiShiptoradio3").attr("Checked","Checked");
		 $('#CiShiptolabel3').css("font-weight","bold");
		 $('#CiShiptolabel2').css("font-weight","normal");
		 $('#CiShiptolabel1').css("font-weight","normal");
		 $('#CiShiptolabel4').css("font-weight","normal");
		 $('#CiShiptolabel3').addClass("ui-state-active");
		 $('#CiShiptolabel2').removeClass("ui-state-active");
		 $('#CiShiptolabel1').removeClass("ui-state-active");
		 $('#CiShiptolabel4').removeClass("ui-state-active");
		return true;
}
function otherinvoiceShiptoAddress(ids,type){
	
	
	var rxMasterId = $("#shipToCustomerAddressID").val();
	console.log('Jenith rxMasterID-1:'+rxMasterId);
	if((rxMasterId === null || rxMasterId === '') || typeof rxMasterId == "undefined")
		 {
		 rxMasterId = $('#rxCustomer_ID').text();
		 console.log('Jenith rxMasterID-2:'+rxMasterId);
		 }
	 if(rxMasterId===null || rxMasterId===""){
		 rxMasterId = $("#customerInvoice_customerHiddnID").val();
		 console.log('Jenith rxMasterID-3:'+rxMasterId);
		}
	 if(rxMasterId===null || rxMasterId===""){
		 rxMasterId = $("#rxMasterIDfrompage").val();
		 console.log('Jenith rxMasterID-4:'+rxMasterId);
		}
	 if(rxMasterId=== null || rxMasterId===""){
		 rxMasterId = $('#rxCustomerID').val();
		 console.log('Jenith rxMasterID-5:'+rxMasterId);
	 }
	 if(rxMasterId=== null || rxMasterId===""){
			rxMasterId=0;
			 console.log('Jenith rxMasterID-6:'+rxMasterId);
		}
	console.log('Jenith rxMasterID=7:'+rxMasterId);
		 $.ajax({
				url: "./salesOrderController/getCustomerDetails",
				type: "GET",
				data : {"customerID" : rxMasterId},
				success: function(data) {
					console.log('Jenith CD:'+data.name);
					customerName=data.name;
					}
			});
		 console.log('Type:'+type+' ids:'+ids)
		 if(type==='cuso'){
			 $.ajax({
				 url: "./salesOrderController/getPreLoadData",
					type: "POST",
					data : "&cuSOID="+ids+"&rxMasterID=0&joMasterID="+$("#joMaster_ID").text(),
					//data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
					success: function(data) {
						console.log('Jenith Your Data: '+data.Cuso.rxShipToAddressId);
						var rxAddressIds = data.Cuso.rxShipToAddressId;
						$("#prShipToOtherAddressID").val(data.Cuso.rxShipToAddressId)
						
						;
						operationVar = "shipToOther";
						 $.ajax({
								url: "./salesOrderController/getShipToOtherAddress",
								type: "GET",
								data : {"addressID" : data.Cuso.rxShipToAddressId},
								success: function(data) {
									var locationName = $("#jobCustomerName_ID").text();
									var rxAddressId = data.rxAddressId;
									var locationAddress1 = data.address1;
									var locationAddress2 = data.address2;
									var locationCity = data.city;
									var locationState = data.state;
									var locationZip = data.zip;
									var name = data.name;
									console.log(rxAddressId+" :: "+locationAddress1+" :: "+locationAddress2+" :: "+locationCity+" :: "+locationState+" :: "+locationZip+" :: "+name);
									//$("#prShiptowarehouseID").val(rxAddressId);
									$("#prShiptowarehouseID").val(rxAddressIds); 
									console.log('addressID in otherinvoiceShiptoAddress:'+$("#prShiptowarehouseID").val());
									$("#rxAddressShipID").val(rxAddressId); 
									$("#customerShipToAddressID").val(name); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
									$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
									
									}
								});
						}
					});
			}
			else if(type=='vepo'){
				
				
				 $.ajax({
					 url: "./salesOrderController/getPreLoadData",
						type: "POST",
						data : "&vePOID="+ids+"&rxMasterID=0&joMasterID="+$("#joMaster_ID").text(),
						//data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID+'&vePOID='+vePOID+'&jobNumber='+jobNumber+"&joReleaseDetailID="+joReleaseDetailID,
						success: function(data) {
							
							var rxAddressIds = data.vepo.rxShipToOtherAddressID;
							 $("#prShipToOtherAddressID").val(data.vepo.rxShipToOtherAddressID);
							 operationVar = "shipToOther";
							 console.log('Jenith Your Data: '+data.vepo.rxShipToOtherAddressID+"==="+operationVar);
							 $.ajax({
									url: "./jobtabs3/getBilltoAddress",
									type: "GET",
									data : {"customerID" : data.vepo.rxShipToOtherAddressID,"oper" : operationVar},
									success: function(data) {
										var locationName = $("#jobCustomerName_ID").text();
										var rxAddressId = data.rxAddressId;
										var locationAddress1 = data.address1;
										var locationAddress2 = data.address2;
										var locationCity = data.city;
										var locationState = data.state;
										var locationZip = data.zip;
										var name = data.name;
										console.log("===>"+rxAddressId+" :: "+locationAddress1+" :: "+locationAddress2+" :: "+locationCity+" :: "+locationState+" :: "+locationZip+" :: "+name);
										$("#prShiptowarehouseID").val(rxAddressIds); 
										console.log('addressID in otherinvoiceShiptoAddress :'+$("#prShiptowarehouseID").val());
										$("#rxAddressShipID").val(rxAddressId); 
										$("#customerShipToAddressID").val(name); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
										$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
										
										}
									});
							}
						});
				}
			else if(type==='cuinvoice'){
				//loadTaxTerritoryRateforinsideJob(null);
				var rxAddressIds = $("#prShiptowarehouseID").val();
				operationVar = "shipToOther";
				 $.ajax({
						url: "./salesOrderController/getPreInvoiceData",
						type: "POST",
						data : {"cuInvoiceId" : ids,"rxMasterID" : ''},
						success: function(data) {
							 console.log('Jenith Your Data: '+data.cuInvoice.rxShipToAddressId);
							 $("#prShipToOtherAddressID").val(data.cuInvoice.rxShipToAddressId);
							 $("#prShiptowarehouseID").val(data.cuInvoice.rxShipToAddressId);
							 console.log('This is the data in Prshipto'+$("#prShiptowarehouseID").val());
							 $("#prShipToOtherAddressID").val(data.cuInvoice.rxShipToAddressId);
							 
				 $.ajax({
						url: "./salesOrderController/getShipToOtherAddress",
						type: "GET",
						data : {"addressID" :  data.cuInvoice.rxShipToAddressId},
						success: function(data) {
							var locationName = $("#jobCustomerName_ID").text();
							var rxAddressId = data.rxAddressId;
							var locationAddress1 = data.address1;
							var locationAddress2 = data.address2;
							var locationCity = data.city;
							var locationState = data.state;
							var locationZip = data.zip;
							var name = data.name;
							console.log(rxAddressId+" :: "+locationAddress1+" :: "+locationAddress2+" :: "+locationCity+" :: "+locationState+" :: "+locationZip+" :: "+name);
							//$("#prShiptowarehouseID").val(rxAddressIds); 
							console.log('addressID in otherinvoiceShiptoAddress :'+$("#prShiptowarehouseID").val());
							$("#rxAddressShipID").val(rxAddressId); 
							$("#customerShipToAddressID").val(name); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
							$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
							
							}
						});
//				 alert(data.cuInvoice.coTaxTerritoryId);
				 if(data.cuInvoice!=null && data.cuInvoice.coTaxTerritoryId!=null && data.cuInvoice.coTaxTerritoryId!="" && data.cuInvoice.coTaxTerritoryId>0){
					globalTaxTerritory=data.cuInvoice.coTaxTerritoryId;	
					 loadTaxTerritoryRateforinsideJob(data.cuInvoice.coTaxTerritoryId);
					}
				 
						}
				 });
			}
			else{
				var title = $('#cuinvoiceIDhidden').val();
				if(title === ""){
					$('#prShiptowarehouseID').val(0);
					loadTaxTerritoryRateforinsideJob(null);
					 $("#prShipToOtherAddressID").val(0);
				}else{
					loadTaxTerritoryRateforinsideJob(globalTaxTerritory);
				}
				
				console.log('Its in Else: '+$("#prShipToOtherAddressID").val());
				operationVar = "shipToOther";
				$.ajax({
					url: "./jobtabs3/getBilltoAddress",
					type: "GET",
					data : {"customerID" : $("#prShipToOtherAddressID").val(),"oper" : operationVar},
					success: function(data) {
						 $("#prShiptowarehouseID").val($("#prShipToOtherAddressID").val());
						var locationName = $("#jobCustomerName_ID").text();
						var rxAddressId = data.rxAddressId;
						var locationAddress1 = data.address1;
						var locationAddress2 = data.address2;
						var locationCity = data.city;
						var locationState = data.state;
						var locationZip = data.zip;
						var name = data.name;
						console.log(rxAddressId+" :: "+locationAddress1+" :: "+locationAddress2+" :: "+locationCity+" :: "+locationState+" :: "+locationZip+" :: "+name);
						//$("#prShiptowarehouseID").val(rxAddressIds); 
						console.log('addressID in otherinvoiceShiptoAddress Else:'+$("#prShipToOtherAddressID").val());
						$("#rxAddressShipID").val(rxAddressId); 
						$("#customerShipToAddressID").val(name); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
						$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
						
						}
					});
			}
	console.log('inside jobwizardRelease.js otherinvoiceShiptoAddress');
	$('#shiptoModeID').val(3);
	$("#cuinvoiceUs").hide();
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px" });
		document.getElementById('customerShipToAddressID').disabled=false;
		document.getElementById('customerShipToAddressID1').disabled=false;
		document.getElementById('customerShipToAddressID2').disabled=false;
		document.getElementById('customerShipToCity').disabled=false;
		document.getElementById('customerShipToState').disabled=false;
		document.getElementById('customerShipToZipID').disabled=false;
		 $("#CiShiptoradio4").attr("Checked","Checked");
		 $('#CiShiptolabel4').css("font-weight","bold");
		 $('#CiShiptolabel2').css("font-weight","normal");
		 $('#CiShiptolabel3').css("font-weight","normal");
		 $('#CiShiptolabel1').css("font-weight","normal");
		 $('#CiShiptolabel4').addClass("ui-state-active");
		 $('#CiShiptolabel2').removeClass("ui-state-active");
		 $('#CiShiptolabel3').removeClass("ui-state-active");
		 $('#CiShiptolabel1').removeClass("ui-state-active");
		return true;
}

$(function() { var cache = {}; var lastXhr='';
$( "#customerinvoice_paymentTerms" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerinvoicepaymentId").val(id);
	paymentTermsDue(id);
	},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#customerInvoice_TaxTerritory" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { 
		var oldtaxrate=$("#customerInvoice_generaltaxId").val();
		var taxtotal_CU=$("#customerInvoice_taxIdcu").val();	
		/*var CI_taxsubtotal=parseFloat(parseFloat(taxtotal_CU*100)/oldtaxrate);
		var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
		if(rows!=null && rows.length>0){
			var taxsubtotal = 0;
			 for(a=0;a<rows.length;a++)
			 {
			    row=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
			    var total=row['amount'].replace(/[^0-9\-.]+/g,"");
			    var taxable=row['taxable'];
			    var id="#canDoID_"+rows[a];
			    var canDo=$(id).is(':checked');
			    var taxid="#taxableID_"+rows[a];
			      if(!isNaN(total)&& !canDo && $(taxid).is(':checked')){
			    	  taxsubtotal=Number(taxsubtotal)+Number(floorFigureoverall(total,2));
			    	}
			      }
			 CI_taxsubtotal=taxsubtotal;
		}*/
		//$("#CI_taxsubtotal").val(CI_taxsubtotal);
		$('#CI_taxfreight').val(ui.item.taxfreight);
		var id = ui.item.id; $("#customerTaxTerritory").val(id); 
		var tax = ui.item.taxValue; $("#customer_TaxTextValue").val(tax);
		$('#customer_TaxTextValue').empty();
		$("#customer_TaxTextValue").append(tax+"%");
		if(tax!=null){
			$("#customerInvoice_generaltaxId").val(tax);
		}else{
			$("#customerInvoice_generaltaxId").val("0.00");
			
		}
		setTaxTotal_CI();
		
		//loadTaxTerritoryRateforinsideJob(id);
	},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#customerInvoice_salesRepsList" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_salesRepId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_CSRList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_CSRId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_SalesMgrList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_salesMgrId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_EngineersList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_engineerId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_PrjMgrList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_prjMgrId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

function getCustomerInvoiceDetails(joDetailId, CustomerID){
	
	//alert("hi");
	
	console.log('getCustomerInvoiceDetails');
	$.ajax({
		url: "./jobtabs5/getCustomerInvoiceDetails",
		type: "POST",
		data : {'releaseDetailID' : joDetailId, 'customerID' : CustomerID },
		success: function(data) {
			if(data !== ''){
				var dueDate = new Date(data.dueDate);
				var receiveDate = new Date(data.invoiceDate);
				var shipDate;
				if (typeof (data.shipDate) != 'undefined' && (data.shipDate)!= null ){
					shipDate = new Date(data.shipDate);
				}else{
					shipDate = new Date();
				}
				var invoiceNumber = data.invoiceNumber;
				var customerPO = data.customerPonumber;
				var fright = data.freight;
				var costTotal = data.costTotal;
				var subTotal = data.subtotal;
//				var taxTotal = data.taxTotal;
				var taxrate = data.taxRate;
//				var rxBillId = data.rxBillToId;
//				var rxBillAddressID = data.rxBillToAddressId;
//				var shipID = data.rxShipToId;
//				var shipAddressId  = data.rxShipToAddressId;	 
				var shipViaID = data.veShipViaId;
//				var prFromWareHouse = data.prFromWarehouseId;
				var prWarehouse = data.prFromWarehouseId;
//				var  taxTeritory = data.coTaxTerritoryId;
				var divisionID = data.coDivisionId;
				var proNumber = data.trackingNumber;
				var doNotMAil = data.doNotMail;
				var cuInvoiceID = data.cuInvoiceId;
				var  assign0 = Number(data.cuAssignmentId0);
				var  assign1 = Number(data.cuAssignmentId1);
				var  assign2 = Number(data.cuAssignmentId2);
				var  assign3 = Number(data.cuAssignmentId3);
				var  assign4 = Number(data.cuAssignmentId4);
				$("#customerInvoice_salesRepId").val(assign0);
				$("#customerInvoice_CSRId").val(assign1);
				$("#customerInvoice_salesMgrId").val(assign2);
				$("#customerInvoice_engineerId").val(assign3);
				$("#customerInvoice_prjMgrId").val(assign4);
			/*	
				var aDueDate = dueDate.getUTCMonth()+1+"/"+ dueDate.getUTCDate()+"/"+dueDate.getUTCFullYear();
				var aInvoiceDate = receiveDate.getUTCMonth()+1+"/"+ receiveDate.getUTCDate()+"/"+receiveDate.getUTCFullYear();
				var aShipDate = shipDate.getUTCMonth()+1+"/"+ shipDate.getUTCDate()+"/"+shipDate.getUTCFullYear();*/
				
				var aDueDate = getFormattedDate(dueDate);
				var aInvoiceDate = getFormattedDate(receiveDate);
				var aShipDate = getFormattedDate(shipDate);
				
				
				$("#customerInvoice_dueDateID").val(aDueDate);
				$("#customerInvoice_invoiceDateID").val(aInvoiceDate);
				$("#customerInvoice_lineinvoiceDateID").val(aInvoiceDate);
				$("#customerInvoice_invoiceNumberId").val(invoiceNumber);
				console.log("Invoice Number is ----->"+invoiceNumber);
				$("#customerInvoice_lineinvoiceNumberId").val(invoiceNumber);
				$("#customerInvoice_shipDateID").val(aShipDate);
				$("#customerInvoice_lineshipDateID").val(aShipDate);
				$("#customerInvoice_proNumberID").val(proNumber);
				$("#customerInvoice_lineproNumberID").val(proNumber);
				
				$("#customerInvoice_subTotalID").val(formatCurrency(subTotal));
				$("#customerInvoice_frightID").val(formatCurrency(fright));
				$("#customerInvoice_taxId").val(formatCurrency(taxrate));
				$("#customerInvoice_totalID").val(formatCurrency(subTotal+fright+taxrate));
				
				//2015-07-27 $("#customerInvoice_linesubTotalID").val(formatCurrency(subTotal));
//				$("#customerInvoice_linefrightID").val(formatCurrency(fright));
				//2015-07-27 $("#customerInvoice_linetaxId").val(formatCurrency(taxrate));
				//2015-07-27 $("#customerInvoice_linetotalID").val(formatCurrency(subTotal+fright+taxrate));
				$("#customerInvoice_ID").val(cuInvoiceID);
				$("#shipToCustomerAddressID").val(data.rxShipToId)
				
				if(doNotMAil == 1){
					$("#customerInvoie_doNotMailID").attr("checked", true);
				}
				
				if(prWarehouse == null && data.cuInvoiceId >0)
				{
				console.log("yes====getCustomerInvoiceDetails=====");
				
				$('#prWareHouseSelectID').val(-1);
				$('#prWareHouseSelectlineID').val(-1);
				
				var x=document.getElementById("prWareHouseSelectID")
			    x.options[0].text="Drop Ship";
			    $("#prWareHouseSelectID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectID").prop('disabled', true);
		        
		        var xL=document.getElementById("prWareHouseSelectlineID")
			    xL.options[0].text="Drop Ship";
			    $("#prWareHouseSelectlineID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectlineID").prop('disabled', true);
				
				}
			else
				{
					 var x=document.getElementById("prWareHouseSelectID")
					 x.options[0].text="- Select -";
					 var xL=document.getElementById("prWareHouseSelectlineID")
				     xL.options[0].text="- Select -";
					 $("#prWareHouseSelectID").prop('disabled', false);
					 $('#prWareHouseSelectID').val(prWarehouse);
					 $('#prWareHouseSelectlineID').val(prWarehouse);
				}
				
				
				$("#customer_Divisions option[value=" + divisionID + "]").attr("selected", true);
				/*$("#prWareHouseSelectID option[value=" + prWarehouse + "]").attr("selected", true);*/
				$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
				/*$("#prWareHouseSelectlineID option[value=" + prWarehouse + "]").attr("selected", true);*/
				$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
				var aPostChk = data.usePostDate;
				if(aPostChk === true){
					$("#postDateChkID").prop("checked", true);
					$("#postDateID").show(); 
				}
				$.ajax({
					url: "./jobtabs5/getAssigneeDetails",
					type: "POST",
					data : {'assignee0' : assign0,  'assignee1' : assign1, 'assignee2' : assign2, 'assignee3' : assign3, 'assignee4' : assign4},
					success: function(data) {
						$("#customerInvoice_salesRepsList").val(data.cuAssignmentId0);
						$("#customerInvoice_CSRList").val(data.cuAssignmentId1);
						$("#customerInvoice_SalesMgrList").val(data.cuAssignmentId2);
						$("#customerInvoice_EngineersList").val(data.cuAssignmentId3);
						$("#customerInvoice_PrjMgrList").val(data.cuAssignmentId4);
					}
				});
			}
		}
	});
	return true;
}

/** Save Customer PO Number **/

function saveCustomerPONumber(){
	if(!$('#customerPOFormID').validationEngine('validate')) {
		return false;
	}
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aReleaseDetailID = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
	var aJoMasterID = $("#joMaster_ID").text();
	var customerPO = $("#customerPONumberID").val().replace("$ ", "");
	$.ajax({
		url: "./jobtabs5/saveCustomerPONumber",
		type: "GET",
		data : {"customerPONumber" : customerPO, 'releaseDetail' : aReleaseDetailID, 'joMasterID' : aJoMasterID },
		success: function(data) {
			var errorText = "Customer PO Number Saved Successfully.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color: green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
	return false;
}

function removeDollorSym(){
	var customerPO = $("#customerPONumberID").val().replace("$ ", "");
	$("#customerPONumberID").val(customerPO);
	return false;
}

function loadCustomerPONumber() {
	var joMasterID = $("#joMaster_ID").text();
	var CO1 = '';
	var CO2 = '';
	var CO3 = '';
	var CO4 = '';
	var CO5 = '';
	var select ="";
	$.ajax({
				url : "./jobtabs5/getCustomerPO",
				type : "POST",
				data : {
					'joMasterId' : joMasterID
				},
				success : function(data) {
					//select = '<option value="-1">-Select-</option><option value="'+$("#jobmain_ponumber").val()+'">'+$("#jobmain_ponumber").val()+'()</option>';
					select = '<option value="-1">-Select-</option><option>'+$("#jobmain_ponumber").val()+'()</option>';
					var aJoMasterCO = $("#customerpodetails").val();
					/*if (aJoMasterCO !== "")
						select += '<option value=CustomerPONumber1>'
								+ aJoMasterCO + '</option>';*/
					$.each(data, function(index, value) {
						CO1 = value.customerPonumber1;
						if (CO1 !== null && CO1 !== "" )
				//	select += '<option value="'+CO1+'">' + CO1
					select += '<option>' + CO1
							+ '()</option>';//'+value.podesc1+'
					
						CO2 = value.customerPonumber2;
						if (CO2 !== null && CO2 !== "" )
					//select += '<option value="'+CO2+'">' + CO2
							select += '<option>' + CO2		
							+ '()</option>';//'+value.podesc2+'
					
						CO3 = value.customerPonumber3;
						if (CO3 !== null && CO3 !== "" )
					//select += '<option value="'+CO3+'">' + CO3
							select += '<option>' + CO3
							+ '()</option>';//'+value.podesc3+'
					
						CO4 = value.customerPonumber4;
						if (CO4 !== null && CO4 !== "" )
					//select += '<option value="'+CO4+'">' + CO4
							select += '<option>' + CO4
							+ '()</option>';//'+value.podesc4+'
					
						CO5 = value.customerPonumber5;
						if (CO5 !== null && CO5 !== "" )
					//select += '<option value="'+CO5+'">' + CO5
							select += '<option>' + CO5
							+ '()</option>';//'+value.podesc5+'
					});
					var gridRowsgg = $('#changeOrderGrid').getRowData();
					var rowDatagg = new Array();
					for (var i = 0; i < gridRowsgg.length; i++) {
						var rowgg = gridRowsgg[i];
						//select +="<option value='"+ rowgg['customerPonumber']+ "'>"+ rowgg['customerPonumber']+ "("+rowgg['changeReason']+")</option>";
						select +="<option >"+ rowgg['customerPonumber']+ "("+rowgg['changeReason']+")</option>";
					}
					
					$('#customerPONumberSelectID').empty();
					$('#customerPONumberSelectID').html(select);
				},
				 error: function(XMLHttpRequest, textStatus, errorThrown) { 
					 var gridRowsgg = $('#changeOrderGrid').getRowData();
						var rowDatagg = new Array();
						for (var i = 0; i < gridRowsgg.length; i++) {
							var rowgg = gridRowsgg[i];
						//	select +="<option value='"+ rowgg['customerPonumber']+ "'>"+ rowgg['customerPonumber']+ "("+rowgg['changeReason']+")</option>";
							select +="<option >"+ rowgg['customerPonumber']+ "("+rowgg['changeReason']+")</option>";
						}
						
						$('#customerPONumberSelectID').empty();
						$('#customerPONumberSelectID').html(select);
	                } 
			});
}

function loadPONumbers() {
	
	
	var joMasterID = $("#joMaster_ID").text();
	var CO1 = '';
	var CO2 = '';
	var CO3 = '';
	var CO4 = '';
	var CO5 = '';
	var select = "";
	$.ajax({
				url : "./jobtabs5/getCustomerPO",
				type : "POST",
				data : {
					'joMasterId' : joMasterID
				},
				success : function(data) {
					//select = '<option value="-1">-Select-</option><option value="'+$("#jobmain_ponumber").val()+'">'+$("#jobmain_ponumber").val()+'</option>';
					select = '<option value="-1">-Select-</option><option>'+$("#jobmain_ponumber").val()+'</option>';
					var aJoMasterCO = $("#customerpodetails").val();
					/*if (aJoMasterCO !== "")
						select += '<option value=CustomerPONumber1>'
								+ aJoMasterCO + '</option>';*/
					$.each(data, function(index, value) {
							CO1 = value.customerPonumber1;
							if (CO1 !== null && CO1 !== "" )
						//select += '<option value="'+CO1+'">' + CO1
								select += '<option>' + CO1
								+ '</option>';
						
							CO2 = value.customerPonumber2;
							if (CO2 !== null && CO2 !== "" )
						//select += '<option value="'+CO2+'">' + CO2
								select += '<option>' + CO2
								+ '</option>';
						
							CO3 = value.customerPonumber3;
							if (CO3 !== null && CO3 !== "" )
						//select += '<option value="'+CO3+'">' + CO3
								select += '<option>' + CO3
								+ '</option>';
						
							CO4 = value.customerPonumber4;
							if (CO4 !== null && CO4 !== "" )
						//select += '<option value="'+CO4+'">' + CO4
								select += '<option>' + CO4
								+ '</option>';
						
							CO5 = value.customerPonumber5;
							if (CO5 !== null && CO5 !== "" )
						//select += '<option value="'+CO5+'">' + CO5
								select += '<option>' + CO5
								+ '</option>';
					});
					var gridRowsgg = $('#changeOrderGrid').getRowData();
					var rowDatagg = new Array();
					for (var i = 0; i < gridRowsgg.length; i++) {
						var rowgg = gridRowsgg[i];
						//select +="<option value='"+ rowgg['customerPonumber']+ "'>"+ rowgg['customerPonumber']+ "</option>";
						select +="<option>"+ rowgg['customerPonumber']+ "</option>";
					}
					
					$('#PONumberID').empty();
					$('#PONumberID').html(select);
				},
				 error: function(XMLHttpRequest, textStatus, errorThrown) { 
					 var gridRowsgg = $('#changeOrderGrid').getRowData();
						var rowDatagg = new Array();
						for (var i = 0; i < gridRowsgg.length; i++) {
							var rowgg = gridRowsgg[i];
						//	select +="<option value='"+ rowgg['customerPonumber']+ "'>"+ rowgg['customerPonumber']+ "</option>";
						select +="<option>"+ rowgg['customerPonumber']+ "</option>";
						}
						
						$('#PONumberID').empty();
						$('#PONumberID').html(select);
	                } 
			});
	
	
}

/**
 * method to get the purchase order general detail to load in vendor invoice details
 * @param vepoID
 */
function loadPOGeneralDetails (vepoID) {
	$.ajax({
		type : "GET",
		url : "./jobtabs5/getPOGeneralDetails",
		data : { 'vepoID' : vepoID},
		async: false,
		success : function (data) {

        	var shipviaData='0';
        	var checkRefs="<option value='-1'>-Select-</option>";
			$.each(data, function(key, valueMap) {
				
				if("vepo" == key)
				{
					$.each(valueMap, function(index, value){
					$('#freight_ID').val(formatCurrency(valueMap.freight));
					$('#tax_ID').val(formatCurrency(valueMap.taxTotal));
					$('#dropshipTaxID_release').val(valueMap.taxRate);
					shipviaData = valueMap.veShipViaId
					});
					/*$.each(valueMap, function(index, value){
						console.log('New Key');
						$('#checkNumberID').val(value);
					}); */
				}
				
				if("shipViaList" == key)
				{
					console.log('ShipVia List Loading');
					$.each(valueMap, function(index, value){
							//checkRefs+='<option value='+value.veShipViaId+'>'+value.description+'</option>';
						if(value.veShipViaId===shipviaData){
							//console.log('ShipVia List Loading: '+value.veShipViaId+" "+shipviaData);
							//$("#shipViaSelectID").val(value.veShipViaId).change();
							//checkRefs+="<option value="+value.veShipViaId+" selected='selected'>"+value.description+"</option>";
							 changeShipVia(shipviaData);
							// $('#shipViaSelectID option[value="' + value.veShipViaId + '"]').prop('selected', true);
						}
							//else{
							//	checkRefs+='<option value='+value.veShipViaId+'>'+value.description+'</option>';
							//}
					
					});
				}
				
				console.log(key);
				if("veInvnoandatory" == key)
					{
					if(valueMap == 1)
		        	{
		        	$('#veInvnomandatoryfromjob').css({"display":"inherit"})
		        	$('#veInvnomandatoryfromjob').attr("data-manvalue",valueMap);
		        	}
		        	else
		        	{
		        	$('#veInvnomandatoryfromjob').css({"display":"none"})
		        	$('#veInvnomandatoryfromjob').attr("data-manvalue",valueMap);
		        	}
					}
					
					
			});		
			//$('#shipViaSelectID').html(checkRefs);
			
			//$("#shipViaSelectID").val(data.veShipViaId);
			try{
///				$("#shipViaSelectID option:contains(" + data.veShipViaId + ")").attr('selected', true);
				
//				$("#shipViaSelectID value:contains("+data.veShipViaId+")").attr('selected', true);
				//shipViaInvoiceID = data.veShipViaId;
				//alert('Jenith:'+shipViaInvoiceID);
				//$("#shipViaSelectID option[value=" + shipViaInvoiceID + "]").attr("selected", true);
				//document.getElementById('shipViaSelectID').value=shipViaInvoiceID;
			}catch(err){
				
			}
			
			
		},
		error : function (msg) {}
	});
}

function addempinsplitcomrelease(returnvalue,rowalphabetic){
	//  alert('on click');
	//'JoMasterId':document.getElementById("joMasterHiddenID").value,'JoReleaseId':$("#jobreleasehiddenId").val()
	  try{
		  var jomasterid=document.getElementById("joMasterHiddenID").value;
		  $("#releaseCommissionSplitsGrid").setGridParam({ postData: {'JoMasterId':jomasterid,'JoReleaseId':returnvalue,'tabpage':'JoRelease'} });
		  jQuery("#openSplitcommreleaseDia").dialog({title:"Commission Split for Release '"+rowalphabetic+"'"});
		  jQuery("#openSplitcommreleaseDia").dialog("open");
	/*	  jQuery("#openSplitcommreleaseDia").dialog({
		    open: function() {
		    	jQuery(this).dialog("option", "title", "My new title");
		    }
		});
	*/  $("#releaseCommissionSplitsGrid").trigger("reloadGrid");
	  }catch(err){
		  
	  }
	  return true;
	}
$(function() { var cache = {}, lastXhr;
$( "#customerShipToAddressID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function (event, ui) {
		var aValue = ui.item.value;
		var valuesArray = new Array();
		valuesArray = aValue.split("|");
		
		var id = valuesArray[0];
		var code = valuesArray[2];				
		$('#shipToCustomerAddressID').val(aValue);
		 $.ajax({
		        url: './getCustomerAddress?rxMasterId='+aValue,
		        type: 'POST',       
		        success: function (data) {

					$.each(data, function(key, valueMap) {
						
						if("customerAddress"==key)
						{				
							//shipToCustomer = valueMap;										
							$.each(valueMap, function(index, value){
								$("#prShiptowarehouseID").val(value.rxAddressId);
								//$('#rxShiptoAddressID').val(value.rxAddressId);
								$('#customerShipToAddressID').val(value.name);
								$('#customerShipToAddressID1').val(value.address1);
								$('#customerShipToAddressID2').val(value.address2);
								$('#customerShipToCity').val(value.city);
								$('#customerShipToState').val(value.state);
								$('#customerShipToZipID').val(value.zip);
							});
						}
						/* if("taxRateforCity" == key)
						{
							$.each(valueMap, function(index, value){
								$('#taxGeneralId').val(formatCurrencynodollar(value));
								taxRateShipTo = value; 
							
							}); 
						} */
					});
		        }
		    }); 
	},
	source: function( request, response ) { var term = request.term;
	//if($('#shipToMode').val() !== '4')
		//{
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "search/customerAddress?rxMasterId=0", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
		//}
		
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });



/*function loadEmailList(rxMasterID)
{
	
	console.log('Inside loadEmailList');
	$.ajax({ 
		url: "./rxdetailedviewtabs/getJobCUInvoiceEmailList",
		mType: "GET",
		data : {'rxMasterID' : rxMasterID},
		success: function(data){
			sEmail = "";
			$.each(data, function(key, valueMap) {
			if("emailList" == key)
			{
				console.log("Email List key is : "+key);
				$.each(valueMap, function(index, value){
					console.log("Email List Value is : "+value);
					if(value.imText != null && value.imText.trim() != '')
					sEmail+='<option value='+value.cuMasterId+'>'+value.imText+'</option>';
				
				}); 
			//	alert(sEmail);
				$('#emailListCU').html(sEmail);
				
				//alert("tt");
			} 
			});
		}
	});
	
}
*/


function loadEmailList(rxMasterID)
{
	//alert("hoi");
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
				$('#emailListCU').html(sEmail);
			} 
			});
		}
	});
	
}

function sendPOEmailfromrelease(poGeneralKey){
	
var grid = $("#release");
var rowId = grid.jqGrid('getGridParam', 'selrow');
var vePoId = grid.jqGrid('getCell', rowId, 'vePoId');
var cuSOID = grid.jqGrid('getCell', rowId, 'cuSOID');
$("#etoaddr").val("");
createtpusage('job-Release Tab','Email PDF','Info','Job-Release Tab,Mailing PDF,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val()+',vePoId:'+vePoId+',cuSOID:'+cuSOID);

if(vePoId)
	getVepoorCusoDetails(vePoId,"vepo",$('#contactId').val());
else
	getVepoorCusoDetails(cuSOID,"cuso",$('#contactId').val());

}


function sendPOEmailfromrelease1(poGeneralKey){
	
	//alert($('#POtransactionStatus').val());
	
	if($('#POtransactionStatus').val() == '-1'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not Send E-Mail, \nTransaction Status is 'Void' \nChange Status to Open.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		console.log('Here');
		jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message",
		buttons:{
			"OK": function(){
			    jQuery(this).dialog("close");
			},
			Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
		return false;
	
	}
 else if($('#POtransactionStatus').val() == '0'){
	 var newDialogDiv = jQuery(document.createElement('div'));
	 	errorText = "You can not Send E-Mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
	 	jQuery(newDialogDiv).attr("id","messageDlg");	
	 	jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	
 }
 else if($('#POtransactionStatus').val() == '2'){
	 var newDialogDiv = jQuery(document.createElement('div'));
	 	errorText = "You can not Send E-Mail, \nTransaction Status is 'Close' \nChange Status to Open.";
	 	jQuery(newDialogDiv).attr("id","messageDlg");
	 	jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	
 }
 else{	
	console.log('Email form jobWizardRelease.js--->'+$('#emailListCU').text()+'  :::  poGeneralKey : '+poGeneralKey);
	//callEmailPopup("","", "", "", "");
	//outsidepoEmailButtonAction()
	
	try{
		if(poGeneralKey=='poGeneral'){
			var newDialogDiv = jQuery(document.createElement('div'));
			var bidderGrid = $("#salesrelease");
			var aQuotePDF = "purchase";
			var rxMasterID = $('#rxCustomer_ID').text();
			var rxContactID=$('#contactId').val();
			var cusotmerPONumber = $("#SOnumberGeneral").val();	
				$.ajax({ 
					url: "./vendorscontroller/GetContactDetails",
					mType: "GET",
					data : { 'rxContactID' : rxContactID},
					success: function(data){
						var aFirstname = data.firstName; 
						var aLastname = data.lastName;
						var aEmail= data.email;
						
						var arxContactid=rxContactID;
						
						var arxContact = aFirstname + ' '+aLastname;
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
								viewPOPDF(aQuotePDF);
								sentMailPOFunctionfromrelease(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, '');
							    jQuery(this).dialog("close");
							},
							Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
					}
				});
		
		}else{
			
			var grid = $("#release");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var vePoId = grid.jqGrid('getCell', rowId, 'vePoId');
			var cuSOID = grid.jqGrid('getCell', rowId, 'cuSOID');
			
			
			if(vePoId)
				getVepoorCusoDetails(vePoId,"vepo");
			else
				getVepoorCusoDetails(cuSOID,"cuso");
			
			
			
			/*var newDialogDiv = jQuery(document.createElement('div'));
			var bidderGrid = $("#release");
			var aQuotePDF = "purchase";
			var rxMasterID = $('#rxCustomer_ID').text();
			var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
			var rxContactID= bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxVendorContactID');
			var cusotmerPONumber = $("#SOnumberGeneral").val();
				$.ajax({ 
					url: "./vendorscontroller/GetContactDetails",
					mType: "GET",
					data : { 'rxContactID' : rxContactID},
					success: function(data){
						var aFirstname = data.firstName; 
						var aLastname = data.lastName;
						var aEmail= data.email;
						
						var arxContactid=rxContactID;
						
						var arxContact = aFirstname + ' '+aLastname;
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
								viewPOPDF(aQuotePDF);
								sentMailPOFunctionfromrelease(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, '');
							    jQuery(this).dialog("close");
							},
							Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
					}
				});*/
		
		}
		
	
		
	}catch(err){
		//alert(err.message);
		}
	return true; 
}
}

function calculatePrice(){
//	alert($('#estimate').val());
//	var gridData = jQuery("#changeOrderGrid").getRowData();
//	var totalPrice = 0;
//	for(var index = 0; index < gridData.length; index++){
//		var rowData = gridData[index];
//		var price = rowData["changeAmount"].replace(/[^0-9\.]+/g,"");
//		totalPrice = totalPrice + Number(price);
//	}
//	alert('inside change order js'+totalPrice );
}


function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
	return cellValue;
}

function customTotalFomatter(cellValue, options, rowObject) {
	var total =0;
//	try{
		console.log("test");
		var multiplier= rowObject['priceMultiplier'];
		var q= rowObject['quantityBilled'];
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

String.prototype.contains = function(it) { return this.indexOf(it) != -1; };
function changeDropDown(toid,fromid){
	var value = $('#'+fromid).val();
	$('#'+toid).val(value);
}

function noteImage(cellValue, options, rowObject){
	var element = '';
   if(cellValue !== '' && cellValue !== null && cellValue != undefined){
	   element = "<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
   }
   return element;
}




function ShowInvoiceNote(){
	try{
		var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
		var id = jQuery("#customerInvoice_lineitems").jqGrid('getGridParam','selrow');
		row=jQuery("#customerInvoice_lineitems").getRowData(id);
		  var notes = row['note'];
		  console.log('notes::]'+notes+'[::');
		  var cuInvoiceDetailId = row['cuInvoiceDetailId'];
		  var lineITemNotes = notes;
		  
		  CKEDITOR.instances['InvoiceLineItemNoteID'].setData(lineITemNotes);
		  jQuery("#InvoiceLineItemNote").dialog("open");
		  
	/*	  if(notes==undefined && notes==null && notes==""){
			  lineITemNotes = notes;
		  }else{
			  lineITemNotes = lineITemNotes.replace("&And", "'");
		  }*/
		/*   areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('InvoiceLineItemNoteID');
		   $('#InvoiceLineItemNote').find(".nicEdit-main").empty();
		   $('#InvoiceLineItemNote').find(".nicEdit-main").append(lineITemNotes);*/
	//		$('#InvoiceLineItemNote').find(".nicEdit-main").focus();
			return true;
		}catch(err){
			alert(err.message);
		}
	}

	/*function SaveInvoiceLineItemNote(){
		var inlineText= $('#InvoiceLineItemNoteForm').find('.nicEdit-main').html();
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
		areaLine.removeInstance('InvoiceLineItemNoteID');
		jQuery("#InvoiceLineItemNote").dialog("close");
		return false;
	}*/

	jQuery(function(){
		jQuery("#InvoiceLineItemNote").dialog({
				autoOpen : false,
				modal : true,
				title:"InLine Note",
				height: 390,
				width: 635,
				buttons : {  },
				close:function(){
					//areaLine.removeInstance('InvoiceLineItemNoteID');
					return true;
				}	
		});
	});


function billedUnbilled(joReleaseId){
	 $.ajax({
			url: "./jobtabs5/getBilledUnbilledAmount",
			type: "POST",
			data : {"joReleaseId" : joReleaseId},
			success: function(data) {
				$('#billedamount').text(formatCurrency(data.billed));
				$('#unbilledamount').text(formatCurrency(data.unbilled));
				$('#freightBillAmount').text(formatCurrency(data.freightAmt));
				
				if(data.unbilled<0){
					$('#unbilledamount').css("color","red");
				}else{
					$('#unbilledamount').css("color","black");
				}
				console.log('UnBilled:/:Billed amount '+data.unbilled+' :/: '+data.billed);
				
				return data.unbilled;
				
			}
			
		});
		
	 
	}
function paymentTermsDue(cuTermsId){
	if(cuTermsId==null && cuTermsId==undefined && cuTermsId==0){
		return false;
	}
	try{
	$.ajax({
			url: './getPaymentTermsDueDate?cuTermsId='+cuTermsId,
			type: 'GET',
			async: false,
			success: function (data) {
			var invoiceDate = new Date($('#customerInvoice_invoiceDateID').val());
			console.log('invoiceDate  :: '+invoiceDate );
			var dd = invoiceDate.getDate();
			console.log('dd :: '+dd );
			if(data.dueondays==0){
				console.log('inside data.dueondays:: '+data.dueondays);
				var date = (Number(invoiceDate.getDate()) + Number(data.duedate));
			invoiceDate.setDate(date );
			dd = invoiceDate.getDate();
			console.log('inside data.dueondays:: '+dd);
			}else{
			invoiceDate.setDate(invoiceDate.getDate() + 32);
			dd = data.duedate;
			}
			console.log('invoiceDate data.dueondays:: '+invoiceDate);
			
			var mm = invoiceDate.getMonth() + 1;
			var y = invoiceDate.getFullYear();
			var someFormattedDate = mm + '/'+ dd+ '/'+ y;
			$('#customerInvoice_dueDateID').val(someFormattedDate);
			}});
	}catch(err){
		console.log('error on change due date'+err.message);
	}
	}
/** load release grid with Data **/
function loadReleaseGrid() {
	release_grid (arrColNamesRelease, arrColModelRelease);
	return true;
}

function loadTaxTerritoryRateforinsideJob(coTaxTerritoryId)
{
	if(coTaxTerritoryId != null && coTaxTerritoryId != '')
	{
		$.ajax({
			url: "./salesOrderController/taxRateTerritory",
			type: "POST",
			data : {"coTaxTerritoryId" : coTaxTerritoryId},
			success: function(data) {
				
				if(data.taxRate!=null)
				{
					var oldtaxrate=$('#customerInvoice_generaltaxId').val();
				$('#customerInvoice_TaxTerritory').val(data.county);
				$('#customerInvoice_generaltaxId').val(Number(floorFigureoverall(data.taxRate, 2)));
				//$('#customerInvoice_linetaxId').val(formatCurrencynodollar(data.taxRate));
				$('#customerTaxTerritory').val(data.coTaxTerritoryId);
			
				var subtot = $("#customerInvoice_subTotalID").val().replace(/[^0-9\.-]+/g,"");
				var frieght = $("#customerInvoice_frightIDcu").val().replace(/[^0-9\.-]+/g,"");
				
				if(subtot == null || subtot==undefined || subtot=="NaN" || subtot==""){
					subtot = 0.00;
				}
				
				if(frieght == null || frieght==undefined || frieght=="NaN" || frieght==""){
					frieght = 0.00;
				}
				var sum = 0;
				var taxAmt = 0;
				var allowfreightinTax=false;
				 var allowreqcheckfreightintax=$('#CI_taxfreight').val();
					if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax==1){
						allowfreightinTax=true;
					}
				 var CI_taxsubtotal=$("#CI_taxsubtotal").val();
				 if(allowfreightinTax){
					 taxAmt = (parseFloat(CI_taxsubtotal)+parseFloat(frieght))*Number(data.taxRate)/100;
				 }else{
					 taxAmt = parseFloat(CI_taxsubtotal)*Number(data.taxRate)/100;
				 }
				 
				 sum = Number(subtot) + taxAmt + Number(frieght);
				 $("#customerInvoice_taxIdcu").val(Number(floorFigureoverall(taxAmt, 2)));
				 $("#customerInvoice_totalID").val(Number(floorFigureoverall(sum, 2)));
				}
				
			}
	});
	}else{
		$('#customerInvoice_TaxTerritory').val("");
		$('#customerInvoice_generaltaxId').val(Number(floorFigureoverall("0.00", 2)));
		//$('#customerInvoice_linetaxId').val(formatCurrencynodollar("0.00"));
		$('#customerTaxTerritory').val(0);
		var subtot = $("#customerInvoice_subTotalID").val().replace(/[^0-9\.-]+/g,"");
		var frieght = $("#customerInvoice_frightIDcu").val().replace(/[^0-9\.-]+/g,"");
		if(subtot == null || subtot==undefined || subtot=="NaN" || subtot==""){
			subtot = 0.00;
		}
		
		if(frieght == null || frieght==undefined || frieght=="NaN" || frieght==""){
			frieght = 0.00;
		}
		var sum = 0;
		var taxAmt =0.00;
		sum = Number(subtot) + taxAmt + Number(frieght);
		
		
		 $("#customerInvoice_taxIdcu").val(Number(floorFigureoverall(taxAmt, 2)));
		 $("#customerInvoice_totalID").val(Number(floorFigureoverall(sum, 2)));
	}
	
}
function loadReleasecontactId(selectedvalue){
	var customerID=$("#JobCustomerId").val();
	if(customerID==null||customerID==""){
		customerID=0;
	}
	 $.ajax({
		       url: "./jobtabs5/getContacts",
		       type: 'GET',
		       data:{'customerID':customerID},
		        success: function(data){
			    var ob = $("#contactId_Release");
				ob.empty();
				ob.append("<option value='-1'>--Select--</option>");
				for (var i = 0; i < data.length; i++) {
				     var val = data[i].rxContactId;
				     var text =data[i].firstName+' '+data[i].lastName;
				     if(val==selectedvalue){
				    	 ob.append("<option value="+val +" selected='selected'>" + text + "</option>");
				     }else{
				    	 ob.append("<option value="+val +">" + text + "</option>");
				     }
							        	
				}
		   }
		  }); 
}
function clearInvoiceDetailsBeforeOpen(){
	$("#customerInvoice_invoiceNumberId").val("");
	$("#customerInvoice_proNumberID").val("");
	$("#customerInvoie_PONoID").val("");
	$("#customerInvoice_TaxTerritory").val("");
	$("#customerinvoice_paymentTerms").val("");
	$("#customerinvoicepaymentId").val("0");
	$("#customerTaxTerritory").val("0");
	$("#shipViaCustomerSelectID").val(-1);
	$("#customer_Divisions").val(-1);
	$('#prShiptowarehouseID').val(0);
	$('mailTimestampGeneral').empty();
	$('mailTimestampGeneral').append('');
}
function Releasetypeselectboxrefresh(){
	$("#releasesTypeID").empty();
	$("#releasesTypeID").append("<option value='-1'>--Select--</option>");
	$("#releasesTypeID").append("<option value='1'>Drop Ship</option>");
	$("#releasesTypeID").append("<option value='2'>Stock Order</option>");
	$("#releasesTypeID").append("<option value='3'>Bill Only</option>");
	$("#releasesTypeID").append("<option value='4'>Commission</option>");
	$("#releasesTypeID").append("<option value='5'>Service</option>");
}

function getCustomerInvoiceDetailsforpopup(InvoiceID){
	if(InvoiceID==null){
		InvoiceID=0;
	}
	console.log('getCustomerInvoiceDetailsforpopup');
	$.ajax({
		url: "./jobtabs5/getCustomerInvoiceDetailsForPopup",
		type: "POST",
		data : {'customerinvoiceID' : InvoiceID },
		success: function(data) {
			if(data !== ''){
				var dueDate = new Date(data.dueDate);
				var receiveDate = new Date(data.invoiceDate);
				var shipDate = data.shipDate;
				var invoiceNumber = data.invoiceNumber;
				var customerPO = data.customerPonumber;
				var fright = data.freight;
				var costTotal = data.costTotal;
				var subTotal = data.subtotal;
				var taxrate = data.taxRate;
				var shipViaID = data.veShipViaId;
				var prWarehouse = data.prFromWarehouseId;
				var divisionID = data.coDivisionId;
				var proNumber = data.trackingNumber;
				var doNotMAil = data.doNotMail;
				var cuInvoiceID = data.cuInvoiceId;
				var aEmailCU =  data.rxContactId;
				var  assign0 = Number(data.cuAssignmentId0);
				var  assign1 = Number(data.cuAssignmentId1);
				var  assign2 = Number(data.cuAssignmentId2);
				var  assign3 = Number(data.cuAssignmentId3);
				var  assign4 = Number(data.cuAssignmentId4);
				$("#customerInvoice_salesRepId").val(assign0);
				$("#customerInvoice_CSRId").val(assign1);
				$("#customerInvoice_salesMgrId").val(assign2);
				$("#customerInvoice_engineerId").val(assign3);
				$("#customerInvoice_prjMgrId").val(assign4);
				console.log('Mail Timestamp::'+data.printDate);
				if(data.printDate != null){
				console.log('Mail Timestamp::'+data.printDate);
				var today = new Date(data.printDate);
				var dd = today.getDate();
				var mm = today.getMonth()+1; 
				var yyyy = today.getFullYear().toString().substr(0,4);
				var hours = today.getHours();
				var minutes = today.getMinutes();
				var ampm = hours >= 12 ? 'PM' : 'AM';
				hours = hours % 12;
				hours = hours ? hours : 12;
				if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
				if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
				$("#mailTimestampLines").empty();
				$("#mailTimestampLines").append(today)
				$("#mailTimestampLines").show();
				$("#mailTimestampGeneral").empty();
				$("#mailTimestampGeneral").append(today)
				$("#mailTimestampGeneral").show();
				}
			/*	var aDueDate = dueDate.getUTCMonth()+1+"/"+ dueDate.getUTCDate()+"/"+dueDate.getUTCFullYear();
				var aInvoiceDate = receiveDate.getUTCMonth()+1+"/"+ receiveDate.getUTCDate()+"/"+receiveDate.getUTCFullYear();
				var aShipDate = shipDate.getUTCMonth()+1+"/"+ shipDate.getUTCDate()+"/"+shipDate.getUTCFullYear();*/
				
				
				var aDueDate = getFormattedDate(dueDate);
				var aInvoiceDate = getFormattedDate(receiveDate);
				
				if (typeof (shipDate) != 'undefined' && shipDate!= null ){
					shipDate = new Date(shipDate);
				}else{
					shipDate = new Date();
				}
				var aShipDate = getFormattedDate(shipDate);
				$("#customerInvoice_dueDateID").val(aDueDate);
				$("#customerInvoice_invoiceDateID").val(aInvoiceDate);
				$("#customerInvoice_lineinvoiceDateID").val(aInvoiceDate);
				$("#customerInvoice_invoiceNumberId").val(invoiceNumber);
				console.log("Invoice Number is ----->"+invoiceNumber);
				$("#customerInvoice_lineinvoiceNumberId").val(invoiceNumber);
				
				$("#customerInvoice_shipDateID").val(aShipDate);
				$("#customerInvoice_lineshipDateID").val(aShipDate);
				$("#customerInvoice_proNumberID").val(proNumber);
				$("#customerInvoice_lineproNumberID").val(proNumber);
				$("#customerInvoie_PONoID").val(customerPO);
				$("#customerInvoice_TaxTerritory").val(data.cotaxdescription);
	        	$("#customerTaxTerritory").val(data.coTaxTerritoryId);
	        	$("#customerinvoice_paymentTerms").val(data.description);
	        	$("#customerinvoicepaymentId").val(data.cuTermsId);
				$("#customerInvoice_subTotalID").val(formatCurrency(subTotal));
				$("#customerInvoice_frightID").val(formatCurrency(fright));
				$("#emailListCU").val(aEmailCU);
				
				if(typeof (aEmailCU) != "undefined")
				{
					$("#emailListCU option[value='" +aEmailCU+ "']").attr("selected", true);
				}
				
				/*var taxAmt=0;
				var totalAmt=0;
				var allowfreightinTax=false;
				 var allowreqcheckfreightintax=getSysvariableStatusBasedOnVariableName("RequireFreightwhencalculatingTaxonCustomerInvoices");
					if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax[0].valueLong==1){
						allowfreightinTax=true;
					}
				 if(allowfreightinTax){
					 taxAmt = (parseFloat(subTotal)+parseFloat(fright))*Number(taxrate)/100;
				 }else{
					 taxAmt = parseFloat(subTotal)*Number(taxrate)/100;
				 }
				 
				 totalAmt = Number(subtot) + taxAmt + Number(frieght);*/
				
				$("#cuGeneral_taxvalue").val(formatCurrency(data.taxAmount));
				$("#customerInvoice_totalID").val(formatCurrency(parseFloat(subTotal)+parseFloat(fright)+parseFloat(data.taxAmount)));
				
				/*Commented by Zenith on 2015-07-27 18:30
				 * $("#customerInvoice_linesubTotalID").val(formatCurrency(subTotal));
				$("#customerInvoice_linefrightID").val(formatCurrency(fright));
				$("#customerInvoice_frightIDcu").val(formatCurrency(fright));
				$("#customerInvoice_linetaxId").val(data.taxRate);
				$("#customerInvoice_linetotalID").val(formatCurrency(parseFloat(subTotal)+parseFloat(fright)+parseFloat(data.taxAmount)));*/
				
				$("#customerInvoice_ID").val(cuInvoiceID);
				$("#shipToCustomerAddressID").val(data.rxShipToId)
				$("#customerInvoice_taxIdcu").val(formatCurrency(data.taxAmount));
				$("#cuGeneral_taxvalue").val(formatCurrency(data.taxAmount));
				$('#prShiptowarehouseID').val(data.rxShipToAddressId);
				$("#prShipToOtherAddressID").val(data.rxShipToAddressId);
				
				if(doNotMAil == 1){	
					$("#customerInvoie_doNotMailID").attr("checked", true);
				}
				if(prWarehouse == null && InvoiceID != 0)
				{
				console.log("yes=====================");
				
				$('#prWareHouseSelectID').val(-1);
				$('#prWareHouseSelectlineID').val(-1);
				
				var x=document.getElementById("prWareHouseSelectID")
			    x.options[0].text="Drop Ship"
			    $("#prWareHouseSelectID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectID").prop('disabled', true);
		        
		        var xL=document.getElementById("prWareHouseSelectlineID")
			    xL.options[0].text="Drop Ship"
			    $("#prWareHouseSelectlineID option[value=-1]").attr("selected", true);
		        $("#prWareHouseSelectlineID").prop('disabled', true);
				
				}
			else
				{
					var x=document.getElementById("prWareHouseSelectID")
					x.options[0].text="- Select -";
					var xL=document.getElementById("prWareHouseSelectlineID")
				    xL.options[0].text="- Select -";
				 	$("#prWareHouseSelectID").prop('disabled', false);
				 	$('#prWareHouseSelectID').val(prWarehouse);
				 	$('#prWareHouseSelectlineID').val(prWarehouse);
				}
				
				
				$("#customer_Divisions option[value=" + divisionID + "]").attr("selected", true);
				/*$("#prWareHouseSelectID option[value=" + prWarehouse + "]").attr("selected", true);*/
				$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
				/*$("#prWareHouseSelectlineID option[value=" + prWarehouse + "]").attr("selected", true);*/
				$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
				var aPostChk = data.usePostDate;
				if(aPostChk === true){
					$("#postDateChkID").prop("checked", true);
					$("#postDateID").show(); 
				}
				$.ajax({
					url: "./jobtabs5/getAssigneeDetails",
					type: "POST",
					data : {'assignee0' : assign0,  'assignee1' : assign1, 'assignee2' : assign2, 'assignee3' : assign3, 'assignee4' : assign4},
					success: function(data) {
						$("#customerInvoice_salesRepsList").val(data.cuAssignmentId0);
						$("#customerInvoice_CSRList").val(data.cuAssignmentId1);
						$("#customerInvoice_SalesMgrList").val(data.cuAssignmentId2);
						$("#customerInvoice_EngineersList").val(data.cuAssignmentId3);
						$("#customerInvoice_PrjMgrList").val(data.cuAssignmentId4);
					}
				});
			}
		}
	});
return true;
}

function jobshowPaymentDetailsDialog()
{
	
/*	 var selrowid = $("#shiping").jqGrid('getGridParam','selrow');
	 var bilAmt= $("#shiping").jqGrid('getCell', selrowid, 'vendorAmount');
	 var appAmt= $("#shiping").jqGrid('getCell', selrowid, 'vendorAppliedAmt');
	 
	 $("#jobAmtinvoice").text($("#shiping").jqGrid('getCell', selrowid, 'vendorAmount'));
	 $("#jobcheckNo").text($("#shiping").jqGrid('getCell', selrowid, 'vechkNo'));
	 $("#jobchekDate").text($("#shiping").jqGrid('getCell', selrowid, 'vedatePaid'));
	 $("#jobcheckAmt").text(formatCurrency($("#shiping").jqGrid('getCell', selrowid, 'vendorAppliedAmt')));
	 $("#jobamtBalance").text(formatCurrency(parseFloat(bilAmt.replace(/[^0-9-.]/g, ''))-parseFloat(appAmt.replace(/[^0-9-.]/g, ''))));
	 jQuery( "#jobshowInvoiceInfoDialog" ).dialog("open");
		return true;
	*/

		 var selrowid = $("#shiping").jqGrid('getGridParam','selrow');
		 var bilAmt= $("#shiping").jqGrid('getCell', selrowid, 'vendorAmount');
		 var appAmt= $("#shiping").jqGrid('getCell', selrowid, 'vendorAppliedAmt');
		 var chkNo = $("#shiping").jqGrid('getCell', selrowid, 'vechkNo');
		 var chkDate = $("#shiping").jqGrid('getCell', selrowid, 'vedatePaid');
		 var veBillID = $("#shiping").jqGrid('getCell', selrowid, 'veBillID');
		 
		 //alert(bilAmt+appAmt+chkNo+chkDate+veBillID)
		 $("#invoiceDetailspopup").html("");
		 if(chkNo!="Multiple")
			 {
				 $("#invoiceDetailspopup").append("<tr><td><label style='Padding:10px; line-height:2'>Invoice</label></td><td></td><td><span id='Amtinvoice' style='Padding:10px; float:right'>"+bilAmt+"</span></td></tr>" +
				 		"<tr><td width='35%'><label style='Padding:10px;line-height:2'>Check #<span id='checkNo'>"+chkNo+"</span></label></td> <td width='35%'> <span id='chekDate' style='Padding:10px;'>"+chkDate+"</span></td><td width='30%'><span id='checkAmt' style='Padding:10px;float:right'>$"+Number(floorFigureoverall(appAmt, 2))+"</span></td></tr>" +
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
				 				   html = html+"<tr><td width='35%'><label style='Padding:10px;line-height:2'>Check #<span id='checkNo'>"+data[i].checkNo+"</span></label></td> <td width='35%'> <span id='chekDate' style='Padding:10px;'>"+chkDate+"</span></td><td width='30%'><span id='checkAmt' style='Padding:10px;float:right'>$"+formatCurrencynodollar(data[i].amountVal)+"</span></td></tr>";
				 				  AmtVal = AmtVal+Number(data[i].amountVal);
				 			
				 				   }
				 			  console.log("AmtVal::"+(bilAmt.replace(/[^0-9-.]/g, '')))
				 			$("#invoiceDetailspopup").append(html).append("<tr><td colspan='2'><label style='Padding:10px;line-height:2'>Balance Due</label></td> <td style='border-style:dashed; border-width: 1px;border-bottom:none;border-left:none;border-right:none;'><span id='amtBalance' style='Padding:10px;float:right; '>$"+formatCurrencynodollar(Number(bilAmt.replace(/[^0-9-.]/g, '')) - Number(AmtVal))+"</span></td>");	 
				 		}		 
				 });	
			 }
		 
		 jQuery( "#jobshowInvoiceInfoDialog" ).dialog("open");
			return true;
		
	
		
		
		
}

$(function(){
	
		jQuery( "#jobshowInvoiceInfoDialog" ).dialog({
				autoOpen: false,
				width: 430,
				height: 240,
				title:"Payment Details",
				modal: true,
				buttons:{
					ok:function(){
						
						jQuery(this).dialog("close");
					}
				}
			});
	
	
		 jQuery( "#jobinvreasondialog" ).dialog({
				autoOpen: false,
				width: 400,
				title:"Reason",
				modal: true,
				 closeOnEscape: false,
                 open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
				buttons:{
					ok:function(){
						if($("#jobinvreasonttextid").val()==""){
							$("#jobinverrordivreason").empty();
							$("#jobinverrordivreason").append("Reason required");
						}else{
							
							$("#jobinverrordivreason").empty();
							aAddOREdit = 'edit';
							savevendorinvoice(aAddOREdit,$("#jobinvreasonttextid").val());
							 
						}
						
					}
				},
				close: function () {
					$('#jobinverrordivreason').validationEngine('hideAll');
					return true;
				}
			});
	
});
function vendorinvoiceOk(){

	if($("#jobinvreasonttextid").val()==""){
		$("#jobinverrordivreason").empty();
		$("#jobinverrordivreason").append("Reason required");
	}else{
		
		$("#jobinverrordivreason").empty();
		aAddOREdit = 'edit';
		savevendorinvoice(aAddOREdit,$("#jobinvreasonttextid").val());
		 
	}
	

}

function check_costnegative(value,colname){

	 var result = null;
              if(Number(value)<0){ 
            	  
			 setTimeout(function(){$("#info_dialog").css("z-index", "12345");$("#info_dialog").css("top", "879px");$("#info_dialog").css("left", "441px");}, 200);
			result = [false, 'Quantity should not less than 0'];
			globalcheckvalidation=false;
				
		}else{
			/*$("#vendorinvoice1_iladd").removeClass("ui-state-disabled");
			$("#vendorinvoice1_iledit").removeClass("ui-state-disabled");
			$("#vendorinvoice1_ilsave").addClass("ui-state-disabled");
			$("#vendorinvoice1_ilcancel").addClass("ui-state-disabled");*/
			 result = [true,""];
			 globalcheckvalidation=true;
		}
		return result;
}

function Calculategrideditrowvalues(editrowid){
   var unitCost=$("#"+editrowid+"_unitCost" ).val();
	 unitCost=unitCost.replace(/[^0-9\.-]+/g,"");
	 
	 if(unitCost==""){
		 unitCost=0;
	 }else if(unitCost<0){
		  
		/*jQuery(newDialogDiv).html('<span><b>Cost cant be negative</b></span>');
			jQuery(newDialogDiv).dialog({modal: false, width:300, height:150, title:"Error", 
			buttons:{
				"Close": function(){*/
					 $("#"+editrowid+"_unitCost").val('');
					 $("#"+editrowid+"quantityBilled").val('0.00');
					 $("#"+editrowid+"_unitCost").focus();
					 /*	jQuery(this).dialog("close");
		
				}
			}
			});
			
			 $("#"+editrowid+"_unitCost").val('');
			 $("#"+editrowid+"quantityBilled").val('0.00');
			 $("#"+editrowid+"_unitCost").focus();*/
		  
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
    	 
}

function loadotherDetails(){
	var jomasterid=$("#joMasterID").val();
	//var customerId=$("#JobCustomerId").val();
	 $.ajax({
	        url: "./jobtabs5/getCustomerinvoicejobDetail",
	        data: {jomasterid:jomasterid},
	        type: 'GET',
	        success: function(data){
	        	$("#customerInvoice_salesRepsList").val(data.cuAssignmentName0);
	        	$("#customerInvoice_salesRepId").val(data.cuAssignmentId0);
	        	$("#customerInvoice_CSRList").val(data.cuAssignmentName1);
	        	$("#customerInvoice_CSRId").val(data.cuAssignmentId1);
	        	$("#customerInvoice_SalesMgrList").val(data.cuAssignmentName2);
	        	$("#customerInvoice_salesMgrId").val(data.cuAssignmentId2);
	        	$("#customerInvoice_EngineersList").val(data.cuAssignmentName3);
	        	$("#customerInvoice_engineerId").val(data.cuAssignmentId3);
	        	$("#customerInvoice_PrjMgrList").val(data.cuAssignmentName4);
	        	$("#customerInvoice_prjMgrId").val(data.cuAssignmentId4);
	        	$("#customer_Divisions").val(data.coDivisionId);
	        	$("#customerInvoice_TaxTerritory").val(data.cotaxdescription);
	        	$("#customerTaxTerritory").val(data.coTaxTerritoryId);
	        	$("#customerinvoice_paymentTerms").val(data.description);
	        	$("#customerinvoicepaymentId").val(data.cuTermsId);
	        	paymentTermsDue(data.cuTermsId);

	        }
	   }); 
}
function chechjobcustomerisonhold(){
	var returnvalue=false;
	var customerid=$("#JobCustomerId").val();
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
function callvendorinvoicesave()
{
	var aInvoiceDetails = $("#openvendorinvoiceFormID").serialize();
	var title = $('#openvendorinvoice').dialog('option', 'title');
	
	var gridRows = $('#vendorinvoice1').getRowData();
	var dataToSend1 = JSON.stringify(gridRows);
	
	
	var itemCode=$("#new_row_prMasterId").val();
	if(itemCode!=undefined){
		
	if(title === 'New Vendor Invoice' || title === 'Partial Vendor Invoice'){
		aAddOREdit = 'add';
		var vendorID=$("#rxMasterID").val();
		var invnum=$("#vendorInvoiceNum").val();
		var checkinvoicenumber=CheckinvoiceNumberavlforvendor(vendorID,invnum);
		if(checkinvoicenumber==true){
			var newDialogDiv2 = jQuery(document.createElement('div'));
			var errorText = "The Invoice Number already exists for this vendor.Are you sure you want to enter this bill?";
			jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
			jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Information", 
			closeOnEscape: false,
            open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
				buttons : [ {
									height : 35,
									text : "Yes",
									click : function() {
										$(this).dialog("close");
										savevendorinvoice(aAddOREdit,'');
										return true;
							}
						},
						
						{
							height : 35,
							text : "No",
							click : function() {
								$(this).dialog("close");
								return false;
						}
				}
						]
							}).dialog("open");
		}else{
			savevendorinvoice(aAddOREdit,'');
		}
		
		
	}else{
		
		aAddOREdit = 'edit';
		
		if(_globalvarvenodorinvoiceform != aInvoiceDetails || _globalvarvenodorinvoicegrid != dataToSend1)
			{
			$("#jobinvreasonttextid").val("");
			jQuery( "#jobinvreasondialog" ).dialog("open");
			}
		else
			{
			savevendorinvoice(aAddOREdit,'');
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

function noticeContact(){
	//if()
	if (document.getElementById('noticeContactID').checked){
		$('#contactId_Release').hide();
		$('#noticeNameID').show();
	}else{
		$('#contactId_Release').show();
		$('#noticeNameID').hide();
	}
	  
}

function getFormattedDate(date) {
	  var year = date.getFullYear();
	  var month = (1 + date.getMonth()).toString();
	  month = month.length > 1 ? month : '0' + month;
	  var day = date.getDate().toString();
	  day = day.length > 1 ? day : '0' + day;
	  return  month+ '/' + day + '/' + year;
}


jQuery( "#invreasondialog" ).dialog({
	autoOpen: false,
	width: 400,
	title:"Reason",
	modal: true,
	closeOnEscape: false, 
     open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
	buttons:{
		ok:function(){
			 var $cudlgObj = $("#invreasondialog");
			
			if($("#invreasonttextid").val()==""){
				$("#inverrordivreason").empty();
				$("#inverrordivreason").append("Reason required");
			}else{
				$("#inverrordivreason").empty();
				Savecustomerinvoicewithreasonboxfminsidejob($cudlgObj.data('aCustomerInvoiceDetails'),$cudlgObj.data('transaction'),$("#invreasonttextid").val(),$cudlgObj.data('cusoId'),$cudlgObj.data('releasType'),$cudlgObj.data('title'),$cudlgObj.data('joReleaseID'));
				setTimeout(function(){
					loadShipingGrid($cudlgObj.data('joReleaseID'),$cudlgObj.data('releasType'))
					},300);
				jQuery(this).dialog("close");
				$("#invreasonttextid").val('');
				$("#shiping").trigger("reloadGrid");
			}
			
		}
	},
	close: function () {
		$("#invreasonttextid").val('');
		$('#invreasondialog').validationEngine('hideAll');
		return true;
	}
});
function customerInvoicereasonokButton(){

	 var $cudlgObj = $("#invreasondialog");
	
	if($("#invreasonttextid").val()==""){
		$("#inverrordivreason").empty();
		$("#inverrordivreason").append("Reason required");
	}else{
		$("#inverrordivreason").empty();
		Savecustomerinvoicewithreasonboxfminsidejob($cudlgObj.data('aCustomerInvoiceDetails'),$cudlgObj.data('transaction'),$("#invreasonttextid").val(),$cudlgObj.data('cusoId'),$cudlgObj.data('releasType'),$cudlgObj.data('title'),$cudlgObj.data('joReleaseID'));
		setTimeout(function(){
			loadShipingGrid($cudlgObj.data('joReleaseID'),$cudlgObj.data('releasType'))
			},300);
		jQuery("#invreasondialog").dialog("close");
		$("#invreasonttextid").val('');
		$("#shiping").trigger("reloadGrid");
	}
	

}

function Savecustomerinvoicewithreasonboxfminsidejob(aCustomerInvoiceDetails,operation,reason,cusoId,releasType,title,joReleaseID)
{
	var add1 = $("#CI_Shipto").contents().find('#shipToName').val();
	var add2 = $("#CI_Shipto").contents().find('#shipToAddress1').val();
	var add3 = $("#CI_Shipto").contents().find('#shipToAddress2').val();
	var city = $("#CI_Shipto").contents().find('#shipToCity').val();
	var state =$("#CI_Shipto").contents().find('#shipToState').val();
	var zip = $("#CI_Shipto").contents().find('#shipToZip').val();	
	var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
	$.ajax({
		url: "./checkAccountingCyclePeriods",
		data:{"datetoCheck":$('#customerInvoice_invoiceDateID').val(),"UserStatus":checkpermission},
		type: "POST",
		success: function(data) { 
			transaction="";
			if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
			{
				periodid=data.cofiscalperiod.coFiscalPeriodId;
				yearid = data.cofiscalperiod.coFiscalYearId;

				
				var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
				deleteinvoiceDetailId=new Array();
	 			 for(var a=0;a<rows.length;a++)
	 			 {
	 			    row=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
	 			   var id="#canDoID_"+rows[a];
	 			   var canDo=$(id).is(':checked');
		 			   if(canDo){
		 				  var cuInvoiceDetailId=row['cuInvoiceDetailId'];
		 				  if(cuInvoiceDetailId!=undefined && cuInvoiceDetailId!=null && cuInvoiceDetailId!="" && cuInvoiceDetailId!=0){
		 				 		deleteinvoiceDetailId.push(cuInvoiceDetailId);
		 				 	}
		 				 $('#customerInvoice_lineitems').jqGrid('delRowData',rows[a]);
		 			   }
	 			   }
	 			var gridRows = $('#customerInvoice_lineitems').getRowData();
				var dataToSend = JSON.stringify(gridRows);
				
				
				console.log("Customer Invoice===="+aCustomerInvoiceDetails+"&reason="+reason);
					$.ajax({
						url: "./jobtabs5/updateCustomerInvoiceDetails?"+aCustomerInvoiceDetails+"&reason="+reason,
						type: "POST",
						data : {'customerInvoiceShipToAddressName':add1,'customerInvoiceShipToAddress1':add2,
							'customerInvoiceShipToAddress2':add3,'customerInvoiceShipToCity':city,
							'customerInvoiceShipToState':state,'customerInvoiceShipToZip':zip,'coFiscalPeriodId':periodid,'coFiscalYearId':yearid,'gridData':dataToSend,'delData':deleteinvoiceDetailId},
						success: function(data) {
							
							if(operation=='close'){
								//alert('#'+cusoId+'#');
								if(cusoId!==null && cusoId !==""){
									if(releasType=='Stock Order'||releasType=='Bill Only'||releasType=='Service'){
								var rowId = $("#release").jqGrid('getGridParam', 'selrow');
								var transStatus = $("#release").jqGrid('getCell', rowId,'transactionStatus');
								console.log('Transacion Status before Update -2 :'+transStatus);
								if(transStatus!= '2'){
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:Green;">Do You want to close the SO transaction Status?</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
								buttons:{
									"OK": function(){
										if(releasType=='Bill Only'){
											$.ajax({
												url: "./salesOrderController/setBillOnlyStatus",
												type: "POST",
												data :{'joReleaseID':joReleaseID,'status':2},
												success: function(data) {
													$("#release").trigger("reloadGrid");
												}
											});
										}else{
											$.ajax({
												url: "./salesOrderController/setSalesOrderStatus",
												type: "POST",
												data :{cusoID:cusoId,status:2},
												success: function(data) {
													$("#release").trigger("reloadGrid");
												}
											});
										}
										jQuery(this).dialog("close");
										$('#cusinvoicetab').dialog("close");
									    return true;
									},
									Cancel: function ()	{
										jQuery(this).dialog("close");
										$("#release").trigger("reloadGrid");
									return false;	
									}}}).dialog("open");
								}
								}
								}
								
							}
							jQuery('#shiping').jqGrid('clearGridData')
							 loadShipingGrid(joReleaseID,"");
							$('#cuInvoiceID').text(data.cuInvoiceId);
							$('#cuinvoiceIDhidden').val(data.cuInvoiceId);
							$("#customerInvoice_lineitems").jqGrid('GridUnload');
							loadCustomerInvoice();
							$("#customerInvoice_lineitems").trigger("reloadGrid");
							$('#showMessageCuInvoice').css("margin-left", "1666%");
							$('#showMessageCuInvoiceLine').css("margin-left", "1666%");
							$('#showMessageCuInvoice').html("Saved");
							$('#showMessageCuInvoiceLine').html("Saved");
							$('#imgInvoicePDF').empty();
							$('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new.png" title="View CuInvoice" onclick="viewCuInvoicePDF(); return false;"	style="background: #EEDEBC;">');

							$('#imgInvoiceEmail').empty();
							$('#imgInvoiceEmail').append(' <input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Customer Invoice" style="background:#EEDEBC;" onclick="sendPOEmail(\'CuInvoice\');return false;">');

							$("#cusinvoicetab").tabs({
							       disabled : false
							      });
							$('#prWareHouseSelectlineID').val($('#prWareHouseSelectID').val());
							$('#shipViaCustomerSelectlineID').val($('#shipViaCustomerSelectID').val());
							$('#customerInvoice_lineshipDateID').val($('#customerInvoice_shipDateID').val());
							$('#customerInvoice_lineproNumberID').val($('#customerInvoice_proNumberID').val());
							
							if(title === 'New Customer Invoice' && transaction == 'save'){
								$('#cusinvoicetab').dialog('option', 'title','Customer Invoice');	
							}
							setTimeout(function(){
								//updateTaxableLines();
								$('#showMessageCuInvoice').html("");
								$('#showMessageCuInvoiceLine').html("");
								},3000);
							getCustomerInvoiceDetailsforpopup(data.cuInvoiceId);
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
	$('#cusinvoicetab').dialog("close");
	
}

function buttonAddCostfn(cellvalue, options, rowObject){
	var JoReleaseDetaiID = rowObject.joReleaseDetailID;
	var cuInvoiceID = rowObject.cuInvoiceID;
	var veBillID = rowObject.veBillID;
	//alert(JoReleaseDetaiID);
	var button =""
		/*if(cuInvoiceID!=null){*/
			button='<input type="button" class="changeOrder turbo-tan" value="Add Cost" style="width:100px" onclick="applyAddCost('+JoReleaseDetaiID+','+cuInvoiceID+','+veBillID+');">';
		/*}*/
	cellvalue = button;
	return button;

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

function setvendorFullAddress(rxMasterID){
	$.ajax({
		url: './jobtabs5/rxAddressInfo?rxMasterID='+rxMasterID,
        type: 'GET',       
        success: function (data) {
        	$("#addressNameID").text(data.address1+"\n"+data.address2+"\n"+data.city+"\n"+data.state+data.zip);
        }
    });
}


function CheckinvoiceNumberavlforvendor(vendorID,Invnumber){
	var  returnvalue=false;
	$.ajax({
        url: "./jobtabs5/CheckinvoiceNumberavlforvendor",
        data: {"vendorID":vendorID,"Invnumber":Invnumber},
        type: 'GET',
        async:false,
        success: function(data){
        	returnvalue=data;
        	
        }
   }); 
 return returnvalue;
}

function checkhowmanydecimal(figure){
	var x_str = figure.toString();
	var decimal_digits = x_str.length - x_str.lastIndexOf('.') - 1;
	console.log(x_str.length+"DEc"+decimal_digits);
	if(decimal_digits <=2){
		return false;
	} else{
		return true;
	}
	 
}
function removedollarsymbol(value,id){
	if(value!=null && value!=""){
		value=value.replace("$","");
		$("#"+id).val(value);
	}
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

function DeleteImageFormatter(cellValue, options, rowObject){
	var cuInvoiceDetailId=rowObject['cuInvoiceDetailId'];
	var element = "<img src='./../resources/images/delete.png' style='vertical-align: middle;' onclick='deleteInvoiceDetail("+cuInvoiceDetailId+")'>";
return element;
}
var deleteinvoiceDetailId=new Array();
/*function deleteInvoiceDetail(cuInvoiceDetailId){
	var rowid=$("#customerInvoice_lineitems").jqGrid('getGridParam', 'selrow');
	$('#customerInvoice_lineitems').jqGrid('delRowData',rowid);
	if(cuInvoiceDetailId!=undefined && cuInvoiceDetailId!=null && cuInvoiceDetailId!="" && cuInvoiceDetailId!=0){
		deleteinvoiceDetailId.push(cuInvoiceDetailId);
	}
	setoverallcustomerinvoicetotal();
}*/

function setcustomerInvoicelineitemtotal(selrowid){
	var unitCost=$("#"+selrowid+"_unitCost").val();
	var pmult=$("#"+selrowid+"_priceMultiplier").val();
	var quantityBilled=$("#"+selrowid+"_quantityBilled").val();
	unitCost=unitCost.replace(/[^0-9\.-]+/g,"");
	if(unitCost==undefined ||unitCost==""||unitCost==null){
		unitCost=0.00;
	}
	if(pmult==undefined ||pmult==""||pmult==null){
		pmult=0;
	}
	if(quantityBilled==undefined ||quantityBilled==""||quantityBilled==null){
		quantityBilled=0;
	}
	if(pmult==undefined ||pmult==null||pmult==0||pmult==""){
		pmult=1;
	}
	unitCost=Number(floorFigureoverall(unitCost,2));
	quantityBilled=Number(floorFigureoverall(quantityBilled,2));
	pmult=Round_priceMultiplier(pmult);
	
	var amount=Number(quantityBilled)*Number(pmult);
	amount=Number(amount)*Number(unitCost);
	amount=Number(floorFigureoverall(amount,2));
	
	 $("#"+selrowid+"_quantityBilled" ).val(/*formatCurrency(*/quantity/*)*/);
	 $("#"+selrowid+"_unitCost").val(/*formatCurrency(*/unitCost/*)*/);
	 $("#"+selrowid+"_priceMultiplier" ).val(/*formatCurrency(*/pmult/*)*/);
	$("#"+selrowid+"_amount").val(formatCurrency(amount));
	
}

function setoverallcustomerinvoicetotal(){
	var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
		var grandTotal = 0;
		var taxsubtotal=0;
		 for(a=0;a<rows.length;a++)
		 {
		    row=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
//		    var quantityBilled = row['quantityBilled'];
//		    var unitCost = row['unitCost'].replace(/[^0-9\.]+/g,"");
//		    var priceMultiplier = row['priceMultiplier'];
//		    if(priceMultiplier==='0'){
//		    	priceMultiplier=1;
//		    }
		    var id="#canDoID_"+rows[a];
		    var canDo=$(id).is(':checked');
		    var total=row['amount'].replace(/[^0-9\-.]+/g,"");
		      if(!isNaN(total) && !canDo){
		    	grandTotal+=Number(floorFigureoverall(total,2));
		    	}
		      var taxid=row["taxable"];
			     if(!isNaN(total)&& !canDo && taxid==1){
			    	taxsubtotal=Number(taxsubtotal)+Number(floorFigureoverall(total,2));
	 			    	}
		      
		      }
		 var freight = $('#customerInvoice_frightIDcu').val().replace('$','');
		 freight =freight.replace(',','');
	     var taxrate = $('#customerInvoice_taxIdcu').val().replace('$','');
		 taxrate =taxrate.replace(',','');
		 
		 if(isNaN(freight)){
			 freight = 0;
			 }
		 if(isNaN(taxrate )){ 
			 taxrate = 0;
			 }
		
		 
		 var count = $("#customerInvoice_lineitems").getGridParam("reccount");
		 if(count!=null && count>0){
		  
		  var taxrate = 0;
		  
		  var allowfreightinTax=false;
			 var allowreqcheckfreightintax=$('#CI_taxfreight').val();
				if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax==1){
					allowfreightinTax=true;
				}
			 if(allowfreightinTax){
				 taxrate = (taxsubtotal+parseFloat(freight))*Number($('#customerInvoice_generaltaxId').val())/100;
			 }else{
				 taxrate = taxsubtotal*Number($('#customerInvoice_generaltaxId').val())/100;
			 }
			 
			 
		 var total = (grandTotal+Number(freight)+Number(floorFigureoverall(taxrate,2)));
		// $('#customerInvoice_linesubTotalID').val(formatCurrency(grandTotal));
		 $('#customerInvoice_subTotalID').val(formatCurrency(grandTotal));	
		 
		// $('#cuGeneral_taxvalue').val(formatCurrency(taxrate));
		 $('#customerInvoice_taxIdcu').val(formatCurrency(taxrate));
		 
		// $('#customerInvoice_linetotalID').val(formatCurrency(total));
		 $('#customerInvoice_totalID').val(formatCurrency(total));
		 }else{
			 
			 //$('#customerInvoice_linesubTotalID').val(formatCurrency(grandTotal));
			 $('#customerInvoice_subTotalID').val(formatCurrency(grandTotal));	
			 
			 var allowfreightinTax=false;
			 var allowreqcheckfreightintax=$('#CI_taxfreight').val();
				if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax==1){
					allowfreightinTax=true;
				}
			 if(allowfreightinTax){
				 taxrate = freight*Number($('#customerInvoice_generaltaxId').val())/100;
			 }
			 var total = (Number(grandTotal)+Number(freight)+Number(floorFigureoverall(taxrate,2)));
			// $('#cuGeneral_taxvalue').val(formatCurrency(taxrate));
			 $('#customerInvoice_taxIdcu').val(formatCurrency(taxrate));
			 
			// $('#customerInvoice_linetotalID').val(formatCurrency(total));
			 $('#customerInvoice_totalID').val(formatCurrency(total));
		 }
}

function canDocheckboxFormatter(cellValue, options, rowObject){
	var id="canDoID_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"' onclick='setoverallcustomerinvoicetotal();clickcheckboxChanges(this.id)'>";
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

var posit_job_customerInvoice=0;
var CuInvoiceDetailrowid;

function loadCustomerInvoice(){
	$("#customerInvoice_lineitems").jqGrid('GridUnload');
	var id = $('#cuinvoiceIDhidden').val();
	

	
	
	$("#customerInvoice_lineitems").jqGrid({
		datatype: 'JSON',
		postData: {'cuInvoiceID':function () { return id; }},
		mtype: 'POST',
		url: './salesOrderController/cuInvlineitemGrid',
		pager:jQuery('#customerInvoice_lineitemspager'),

		colNames:['Product No','', 'Description','Qty','Price Each', 'Mult.', 'Tax', 'Amount','Notes', 'Manu. ID','cuSodetailId', 'prMasterID','WhCost','<img src="./../resources/images/delete.png" style="vertical-align: middle;">'],
	   	colModel:[{name:'itemCode',index:'itemCode',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:17,
	   		dataEvents: [
 		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
 		    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
 		    			  ],
				dataInit : function(elem) {
						$(elem).autocomplete(
									{
										source : 'jobtabs3/productCodeWithNameList',
										minLength : 1,
										select : function(event, ui) {
											var selrowid=$("#customerInvoice_lineitems").jqGrid('getGridParam', 'selrow');
											 var ID = ui.item.id; var product = ui.item.label; $("#"+selrowid+"_prMasterId").val(ID);
												if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#"+selrowid+"_description").val(pro2);} 
												$.ajax({
										        url: './jobtabs5/getInvoiceLineItems?prMasterId='+$("#"+selrowid+"_prMasterId").val(),
										        type: 'POST',       
										        success: function (data) {
										        	$.each(data, function(key, valueMap) {										
										        		if("lineItems"==key)
														{				
															$.each(valueMap, function(index, value){						
																
																	$("#"+selrowid+"_description").val(value.description);
																	$("#"+selrowid+"_unitCost").val(value.salesPrice00);
																	$("#"+selrowid+"_priceMultiplier").val(value.pomult);
																	$("#"+selrowid+"_amount").val(formatCurrency(0));
																	if(value.isTaxable == 1)
																	{
																		$("#"+selrowid+"_taxable").prop("checked",true);
																	}
																	else
																		$("#"+selrowid+"_taxable").prop("checked",false);
															});
															//$("#new_row_description").focus();
															$("#"+selrowid+"_description").focus();
															setproductWareHouseCost(selrowid,ID);
															var productCost=$("#"+selrowid+"_whseCost").val();
															if(productCost==null || productCost=="" || productCost==undefined || productCost==0.00){
																productCost=0.00;
															}
															$("#salesorder_cost").val(formatCurrency(productCost));
															var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
															var grandTotal = 0;
															 for(a=0;a<rows.length;a++)
															 {
															    rowdata=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
															    var eachproductCost=rowdata['whseCost'];
															    if(eachproductCost==null || eachproductCost=="" || eachproductCost==undefined){
															    	eachproductCost=0.00;
																}
															    var quantity=rowdata['quantityBilled'];
															    if(quantity==null || quantity=="" || quantity==undefined){
															    	quantity=0.00;
																}
															    var warehsecost=Number(eachproductCost)*Number(quantity);
															    grandTotal=grandTotal+parseFloat(warehsecost);
															      }
															 $("#salesorder_order").val(formatCurrency(grandTotal));
															 var subTotalIDLine=$("#customerInvoice_subTotalID").val();
															 subTotalIDLine=subTotalIDLine.replace(/[^0-9-.]/g, '');
															 if(subTotalIDLine==null || subTotalIDLine=="" || subTotalIDLine==undefined){
																 subTotalIDLine=0.00;
															 }
															 var margintotal=Number(subTotalIDLine)-Number(grandTotal);
															 $("#salesorder_total").val(formatCurrency(margintotal));
														}								
													});
										        }
										    });
											
										}
									});
				}
	   		
	   	},editrules:{edithidden:false,required: true}},
	   	   	{name:'noteImage',index:'noteImage', align:'right', width:10,hidden:false, editable:false, formatter:noteImage, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
	   		{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:28,
	   			dataEvents: [
     		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
     		    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
     		    			  ]
	   		},editrules:{edithidden:false},  
	   			cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
	   		{name:'quantityBilled', index:'quantityBilled', align:'center', width:15,hidden:false, editable:true, editoptions:{size:17, alignText:'left'
	   			, dataEvents: [
    			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },
	                        {
	                         type: 'keyup',
	                         fn: function(e) {
	                        	 var selrowid=$("#customerInvoice_lineitems").jqGrid('getGridParam', 'selrow');
	                        	setcustomerInvoicelineitemtotal(selrowid);
	                         }
	                        }]	
	   		},editrules:{edithidden:true,required: false}},
	   		{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:17, alignText:'right'
	   			, dataEvents: [
								{ type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
								{ type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },
		                        {
		                         type: 'keyup',
		                         fn: function(e) {
		                        	 var selrowid=$("#customerInvoice_lineitems").jqGrid('getGridParam', 'selrow');
		                        	setcustomerInvoicelineitemtotal(selrowid);
		                         }
		                        }]	
	   		},editrules:{edithidden:true}},
	   		{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:17, alignText:'right'
	   			, dataEvents: [
								{ type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
								{ type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },
		                        {
		                         type: 'keyup',
		                         fn: function(e) {
		                        	 var selrowid=$("#customerInvoice_lineitems").jqGrid('getGridParam', 'selrow');
		                        	setcustomerInvoicelineitemtotal(selrowid);
		                         }
		                        }]		
	   		}/*, formatter:customCurrencyFormatterWithoutDollar*/, editrules:{edithidden:true}},
	   		{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox',editoptions:{value:'1:0'},editrules:{edithidden:true}},
	   		{name:'amount', index:'amount', align:'right', width:50,hidden:false, editable:true,editoptions:{size:15, alignText:'right',readonly: 'readonly',
	   			dataEvents: [
    		       			  { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
    		    			  { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }
    		    			  ]
	   		},editrules:{edithidden:true},formatter:customTotalFomatter},
	   		{name:'note', index:'note', align:'right', width:10,hidden:true, editable:false, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
	   		{name:'cuInvoiceId', index:'cuInvoiceId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
	   		{name:'cuInvoiceDetailId', index:'cuInvoiceDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
	   		{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:17, alignText:'right'},editrules:{edithidden:false}},
	   		{name:'whseCost',index:'whseCost',align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
	   		{name:'canDo', index:'canDo', align:'center',  width:20, hidden:false, editable:false, formatter:canDocheckboxFormatter,   editrules:{edithidden:true}}
	   		//{name:'',index:'', width:10,editable:false, hidden:false,formatter:DeleteImageFormatter,editrules:{required:false}, editoptions:{size:10}},
	   		],
		 altRows:true,
		 altclass:'myAltRowClass',
		 cellsubmit: 'clientArray',
		 editurl: 'clientArray',
		 height:200,
		 imgpath:'themes/basic/images',
		 rowNum: 0,
		 sortname:' ',
		 sortorder:"asc",
		 pgbuttons: false,
		 recordtext:'',
		 rowList:[],
		 pgtext: null,
		 rownumbers:true,
	 	 width:840,
	 	 //footerrow: true,
	    // userDataOnFooter : true,
	     viewrecords: false,
	     loadonce: false,
	     cellEdit: false,
	     cmTemplate: {sortable:false},
	     gridComplete: function () {
	    	 jQuery("#customerInvoice_lineitems").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_customerInvoice);
	            posit_job_customerInvoice=0;
	            var gridRows = $('#customerInvoice_lineitems').getRowData();
			 	_globaloldcustomerInvoicegrid =  JSON.stringify(gridRows)+$("#customerInvoice_invoiceDateID").val();
	     },
			loadBeforeSend: function(xhr) {
				posit_job_customerInvoice= jQuery("#customerInvoice_lineitems").closest(".ui-jqgrid-bdiv").scrollTop();
			},
	     loadComplete: function () 
	    	 {
	    			
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
	 			
	 			var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
	 			var grandTotal = 0;
	 			var taxsubtotal=0;
	 			 for(a=0;a<rows.length;a++)
	 			 {
	 			    row=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
	 			    var total=row['amount'].replace(/[^0-9\-.]+/g,"");
	 			    var id="#canDoID_"+rows[a];
	 			    var canDo=$(id).is(':checked');
	 			      if(!isNaN(total)&& !canDo){
	 			    	grandTotal=Number(grandTotal)+Number(floorFigureoverall(total,2));
	 			    	}
	 			      
	 			     var taxid=row['taxable'];
	 			     if(!isNaN(total)&& !canDo && taxid==1){
	 			    	taxsubtotal=Number(taxsubtotal)+Number(floorFigureoverall(total,2));
		 			    	}
	 			     
	 			      }
	 			 var freight = $('#customerInvoice_frightIDcu').val().replace('$','');
	 			 freight =freight.replace(',','');
	 		     var taxrate = $('#customerInvoice_taxIdcu').val().replace('$','');
	 			 taxrate =taxrate.replace(',','');
	 			 
	 			 if(isNaN(freight)){
	 				 freight = 0;
	 				 }
	 			 if(isNaN(taxrate )){ 
	 				 taxrate = 0;
	 				 }
	 			 var count = $("#customerInvoice_lineitems").getGridParam("reccount");
	 			 
	 			 if(count!=null && count>0){
	 			  grandTotal=Number(floorFigureoverall(grandTotal,2));
	 			  
	 			  var taxrate = 0;
	 			 var allowfreightinTax=false; 
	 			 var allowreqcheckfreightintax=$('#CI_taxfreight').val();
	 			if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax==1){
	 						allowfreightinTax=true;
	 					}
	 				 if(allowfreightinTax){
	 					 taxrate = (Number(floorFigureoverall(taxsubtotal,2))+parseFloat(freight))*Number($('#customerInvoice_generaltaxId').val())/100;
	 				 }else{
	 					 taxrate = Number(floorFigureoverall(taxsubtotal,2))*Number($('#customerInvoice_generaltaxId').val())/100;
	 				 }
	 				 
	 				 
	 			 var total = (Number(floorFigureoverall(grandTotal,2))+Number(freight)+Number(floorFigureoverall(taxrate,2)));
	 			// $('#customerInvoice_linesubTotalID').val(formatCurrency(grandTotal));
	 			 $('#customerInvoice_subTotalID').val(formatCurrency(grandTotal));	
	 			// $('#cuGeneral_taxvalue').val(formatCurrency(taxrate));
	 			 $('#customerInvoice_taxIdcu').val(formatCurrency(taxrate));
	 			 
	 			// $('#customerInvoice_linetotalID').val(formatCurrency(total));
	 			 $('#customerInvoice_totalID').val(formatCurrency(total));
	 			 }
	 			 var cuinvoiceid = $('#cuinvoiceIDhidden').val();
	 			 $("#customerInvoice_lineitems").setSelection(1, true);
	 			 
	 			
	    	 
	     },
		onSelectRow : function(id) {
			CuInvoiceDetailrowid=id;
			$("#salesorder_cost").val("$0.00");
			$("#salesorder_order").val("$0.00");
			$("#salesorder_total").val("$0.00");
			setshowWHCost(id);
		},
		jsonReader:{
			root:"rows",
			page:"page",
			total:"total",
			records:"records",
			repeatitems:false,
			cell:"cell",
			id:"id",
			userdata:"userdata"
    	}
	}).navGrid("#customerInvoice_lineitemspager", {
		add : false,
		edit : false,
		del : false,
		search : false,
		refresh : false,
		pager : false,
	});
	$("#customerInvoice_lineitems").jqGrid(
			'inlineNav',
			'#customerInvoice_lineitemspager',{
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
					addRowParams : {
								keys : false,
								oneditfunc : function() {
									$("#new_row_amount").val(formatCurrency(0));
									  $('#imgInvoicePDF').empty();
									  $('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new_disabled.png" title="View CuInvoice" return false;" style="background: #EEDEBC;cursor:default;">');

									  $('#imgInvoiceEmail').empty();
									  $('#imgInvoiceEmail').append('<input id="contactEmailID" type="image" src="./../resources/Icons/mail_new_disabled.png" title="Email Customer Invoice" style="background: #EEDEBC;cursor:default;" return false;">');

								},
								successfunc : function(response) {
									console.log(response);
									return true;
								},
								aftersavefunc : function(response) {

									var ids = $("#customerInvoice_lineitems").jqGrid('getDataIDs');
									var cuinvrowid;
									if(ids.length==1){
										cuinvrowid = 0;
									}else{
										var idd = jQuery("#customerInvoice_lineitems tr").length;
										for(var i=0;i<ids.length;i++){
											if(idd<ids[i]){
												idd=ids[i];
											}
										}
										cuinvrowid= idd;
									}
									if(CuInvoiceDetailrowid=="new_row"){
										var newid=Number(cuinvrowid)+1;
									$("#" + CuInvoiceDetailrowid).attr("id", newid);
									//$("#taxableID_new_row").attr("id", "taxableID_"+newid);
									}
									
									var grid=$("#customerInvoice_lineitems");
									grid.jqGrid('resetSelection');
								    var dataids = grid.getDataIDs();
								    for (var i=0, il=dataids.length; i < il; i++) {
								        grid.jqGrid('setSelection',dataids[i], false);
								    }
								    
								    
									//formatCurrency(sum)
								
									setoverallcustomerinvoicetotal();
								},
								
								errorfunc : function(rowid, response) {
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


						var ids = $("#customerInvoice_lineitems").jqGrid('getDataIDs');
						var cuinvrowid;
						if(ids.length==1){
							cuinvrowid = 0;
						}else{
							var idd = jQuery("#customerInvoice_lineitems tr").length;
							for(var i=0;i<ids.length;i++){
								if(idd<ids[i]){
									idd=ids[i];
								}
							}
							cuinvrowid= idd;
						}
						if(CuInvoiceDetailrowid=="new_row"){
							var newid=Number(cuinvrowid)+1;
							$("#" + CuInvoiceDetailrowid).attr("id", newid);
							//$("#taxableID_new_row").attr("id", "taxableID_"+newid);
						}
						
						var grid=$("#customerInvoice_lineitems");
						grid.jqGrid('resetSelection');
					    var dataids = grid.getDataIDs();
					    for (var i=0, il=dataids.length; i < il; i++) {
					        grid.jqGrid('setSelection',dataids[i], false);
					    }
					    
						//formatCurrency(sum)
					
						setoverallcustomerinvoicetotal();
					
					},
					errorfunc : function(rowid, response) {
						console.log('EditParams ErrorFunc');
						return false;

					},

					oneditfunc : function(id) {
						 $('#imgInvoicePDF').empty();
						  $('#imgInvoicePDF').append('<input type="image" src="./../resources/Icons/PDF_new_disabled.png" title="View CuInvoice" return false;" style="background: #EEDEBC;cursor:default;">');

						  $('#imgInvoiceEmail').empty();
						  $('#imgInvoiceEmail').append('<input id="contactEmailID" type="image" src="./../resources/Icons/mail_new_disabled.png" title="Email Customer Invoice" style="background: #EEDEBC;cursor:default;" return false;">');

						console.log('OnEditfunc'+id);
						var unitcost=$("#"+id+"_unitCost").val();
						unitcost=unitcost.replace(/[^0-9\-.]+/g,"");
						if(unitcost==undefined ||unitcost==""||unitcost==null){
							unitcost=0.00;
						}
						$("#"+id+"_unitCost").val(unitcost);
					}
					
				}
				
			});	
	$('#customerInvoice_lineitems').jqGrid('navButtonAdd',"#customerInvoice_lineitemspager",{ caption:"", buttonicon:"ui-icon-calculator", onClickButton:ShowInvoiceNote, position: "last", title:"Edit note for line item", cursor: "pointer"});
	//commented By Aravind 
	//to hide ^ 
	//$('#customerInvoice_lineitems').jqGrid('navButtonAdd',"#customerInvoice_lineitemspager",{ caption:"", buttonicon:"", onClickButton:showHiddenCost, position: "last", title:"Show/Hide Cost", cursor: "pointer"});
}
var flag=true;
function showHiddenCost(){
	console.log(type);
	if(flag==true){
		if((type=='Bill Only') || (type=='Stock Order') || (type=='Service')){
		$("#costToDisplay").show();
		flag=false;
		}else{
		}
	}else{
		$("#costToDisplay").hide();
		flag=true;
	}
}
function setProductTaxable(cellValue, options, rowObject){
		var element = '';
		//var taxValue =  $('#customerInvoice_TaxTerritory').val();
		var id="taxableID_"+options.rowId;
	   if((cellValue !== null && (cellValue=='Yes' || cellValue==1)) ){
		   element = "<input type='checkbox' id='"+id+"' checked='checked' disabled>";
	   }else{
		   element = "<input type='checkbox' id='"+id+"'  disabled>";
	   }
	   return element;

}

function SaveInvoiceLineItemNote(){
	//var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
//	row=jQuery("#customerInvoice_lineitems").getRowData(rows[id-1]);
//	  var notes = row['note'];
	
   	  var inlineText=  CKEDITOR.instances["InvoiceLineItemNoteID"].getData(); 
	  var id = jQuery("#customerInvoice_lineitems").jqGrid('getGridParam','selrow');
	
	  $("#customerInvoice_lineitems").jqGrid('setCell',id,'note', inlineText);
	  var image="<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
	  if(inlineText==null || inlineText==undefined || inlineText==""){
		  image="";
	  }
	  $("#customerInvoice_lineitems").jqGrid('setCell',id,'noteImage', image);
	  jQuery("#InvoiceLineItemNote").dialog("close");
	  
	  
	  //var cuInvoiceDetailId = row['cuInvoiceDetailId'];
	/*$.ajax({
		url: "./salesOrderController/saveInvoiceLineItemNote",
		type: "POST",
		data : "cuInvoiceDetailId="+cuInvoiceDetailId+"&note="+inlineText,
		success: function(data) {
			jQuery("#InvoiceLineItemNote").dialog("close");
			$("#customerInvoice_lineitems").trigger("reloadGrid");
		}
		});*/
}

function CancelInvoiceInLineNote(){
	//areaLine.removeInstance('InvoiceLineItemNoteID');
	jQuery("#InvoiceLineItemNote").dialog("close");
	return false;
}
function generatecustoemrInvoiceFormTotalIDSeriallize(){
	var customerInvoice_subTotalID=$("#customerInvoice_subTotalID").val();
	var customerInvoice_frightIDcu=$("#customerInvoice_frightIDcu").val();
	var customerInvoice_taxIdcu=$("#customerInvoice_taxIdcu").val();
	var customerInvoice_totalID=$("#customerInvoice_totalID").val()+"";
	if(customerInvoice_totalID!=null && customerInvoice_totalID!=undefined && customerInvoice_totalID!=""){
		customerInvoice_totalID=customerInvoice_totalID.replace(/[^0-9\.-]+/g,"");
		customerInvoice_totalID=customerInvoice_totalID.replace(",","");
	}
		return customerInvoice_totalID;
}

function canDocheckboxFormatterVIPO(cellValue, options, rowObject){
	var id="canDoVIID_"+options.rowId;
	//clickVIcheckboxChanges(this.id)
	var element = "<img src='./../resources/images/delete_jqGrid.png' style='vertical-align: middle;' id='"+id+"' onclick='deleteRowFromJqGrid("+options.rowId+");'>";
return element;
}


function clickVIcheckboxChanges(id){
	id="#"+id;
	console.log(id);
    var canDo=$(id).is(':checked');
    if(canDo){
    	$(id).val("true");
    }else{
    	$(id).val("false");
    }
}

function canDeleteCheckboxFormatter(cellValue, options, rowObject){
	var id="canDeleteJorID_"+options.rowId;
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

function validateReleaseSplitCommissionTotals(id){
	var rows = jQuery("#releaseCommissionSplitsGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {
		    row=jQuery("#releaseCommissionSplitsGrid").getRowData(rows[a]);
		    total=row['allocated'];
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
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should below 100'+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
					// jQuery("#releaseCommissionSplitsGrid").jqGrid('restoreRow',id);
					 jQuery("#releaseCommissionSplitsGrid").jqGrid('setSelection',id, true);
					 $('#releaseCommissionSplitsGrid_iledit').trigger('click');
					}}]}).dialog("open");
				return false;
		 }
}
function validateReleaseSplitCommissionTotalsSaveRelease(){
	var rows = jQuery("#releaseCommissionSplitsGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {
		    row=jQuery("#releaseCommissionSplitsGrid").getRowData(rows[a]);
		    total=row['allocated'];
		   // total+=parseInt(total);
		    grandTotal = Number(grandTotal) + Number(total); 
		 }
	var splitCommGridDatas = $('#releaseCommissionSplitsGrid').getRowData();
	var splitCommGridDataLocal = JSON.stringify(splitCommGridDatas);
		 
	if(parseInt(grandTotal)<100 && rows.length!=0){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should be 100 </b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");
				return false;
		 }else{
			 return true;
		 }
}
function loadCUInvoice_ShipTO(CIdivFlag,InvoiceID){
	$.ajax({
		url : "./salesOrderController/getcuInvoice",
		type : "POST",
		data : {"cuInvoiceId" : InvoiceID},
		success:function(data) {
			var shiptomode = data.shipToMode;
			var checkshiptoid;
			$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val("");
			$(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val("");
			loadshiptostateautocmpte(CIdivFlag);
			if(shiptomode == "0")
			{
				checkshiptoid = data.prToWarehouseId;
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
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.prToWarehouseId);
				}
				else if(data.shipToMode == 1)
				{
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.rxShipToId);
				}
				else if(data.shipToMode == 2)
				{
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.rxShipToId);
				}
				else
				{
					$(CIdivFlag).contents().find("#shiptoaddrhiddenfromdbid").val(data.rxShipToAddressId);
				}
				$(CIdivFlag).contents().find("#shiptomodehiddenfromdbid").val(data.shipToMode);
				preloadShiptoAddress(CIdivFlag,data.cuInvoiceId,checkshiptoid,data.shipToMode,'0',$("#jobCustomerName_ID").text(),data.coTaxTerritoryId);
				$(CIdivFlag).contents().find("#shiptomoderhiddenid").val(data.shipToMode);
			}
		}
	});
}

function global_override_taxIDBasedOnCustomer(){
	var customerID=$("#JobCustomerId").val();
	$.ajax({
		url : "./salesOrderController/getoverride_taxterritory",
		type : "POST",
		async:false,
		data : {"customerID" : customerID},
		success:function(data) {
			if(data!=null && data!="" && data!=undefined){
				_global_override_taxIDBasedOnCustomer=data.coTaxTerritoryId;
			}else{
				_global_override_taxIDBasedOnCustomer=null;
			}
		}
	});
}
function UpdateCommissionRecieveornot(){
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var joReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
	var releaseType = grid.jqGrid('getCell', rowId, 'type');
	var commissionClosedID=$("#commissionClosedID").is(':checked');
    if(commissionClosedID==undefined){
    	commissionClosedID=false;
    }
	
	if(releaseType==='Commission'){
		$.ajax({
			url : "./jobtabs5/updateJoReleaseCommRecieve",
			type : "GET",
			async:false,
			data : {"joReleaseId" : joReleaseId,'commissionClosedID':commissionClosedID},
			success:function(data) {
			}
		});
	}
	
}

function updateTaxableLines(){
	var OperationVar = 0;
	var taxName = $('#customerInvoice_TaxTerritory').val();
	
	if(taxName.toLowerCase().indexOf("exempt") > -1){
		OperationVar = 0;
	}else{
		OperationVar = 1;
	}
	
	var cuInvoiceID = $('#cuinvoiceIDhidden').val();
	if(cuInvoiceID!=null){
	$.ajax({
		url: "./salesOrderController/updateTaxableOnInventory",
		type: "POST",
		data : {"cuInvoiceID" : cuInvoiceID,"operation" : OperationVar},
		success: function(data) {
			
		}
	});
	}
	return true;
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
					$('#dropshipTaxID_release').val(valueMap.taxRate);
					});
				}
			});		
		}
	});
}
function setshowWHCost(id){
	var row=jQuery("#customerInvoice_lineitems").getRowData(id);
	var productCost=row['whseCost'];
	if(productCost==null || productCost=="" || productCost==undefined || productCost==0.00){
		productCost=0.00;
	}
	$("#salesorder_cost").val(formatCurrency(productCost));
	var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
	var grandTotal = 0;
	 for(a=0;a<rows.length;a++)
	 {
	    rowdata=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
	    var eachproductCost=rowdata['whseCost'];
	    if(eachproductCost==null || eachproductCost=="" || eachproductCost==undefined){
	    	eachproductCost=0.00;
		}
	    var quantity=rowdata['quantityBilled'];
	    if(quantity==null || quantity=="" || quantity==undefined){
	    	quantity=0.00;
		}
	    var warehsecost=Number(eachproductCost)*Number(quantity);
	    grandTotal=grandTotal+parseFloat(warehsecost);
	      }
	 $("#salesorder_order").val(formatCurrency(grandTotal));
	 var subTotalIDLine=$("#customerInvoice_subTotalID").val();
	 subTotalIDLine=subTotalIDLine.replace(/[^0-9-.]/g, '');
	 if(subTotalIDLine==null || subTotalIDLine=="" || subTotalIDLine==undefined){
		 subTotalIDLine=0.00;
	 }
	 var margintotal=Number(subTotalIDLine)-Number(grandTotal);
	 $("#salesorder_total").val(formatCurrency(margintotal));
}
function setproductWareHouseCost(selrowId,prMasterID){
	$.ajax({
		url: "./salesOrderController/getproductWareHouseCost",
		type: "POST",
		async:false,
		data :{"prMasterID":prMasterID},
		success: function(data) {
			$("#"+selrowId+"_whseCost").val(data);
		}
	});
}
jQuery("#costToDisplay").hide();

function deleteRowFromJqGrid(jqGridRowId)
{
	 var veBillDetailId = jQuery("#vendorinvoice1").jqGrid ('getCell', jqGridRowId, 'veBillDetailId');
	 if(veBillDetailId!=undefined && veBillDetailId!=null && veBillDetailId!="" && veBillDetailId!=0){
		 deleteveBillDetailIDDetailId.push(veBillDetailId);
		 console.log(deleteinvoiceDetailId.length+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			 $('#vendorinvoice1').jqGrid('delRowData',jqGridRowId);
	 	}else{
	 		$('#vendorinvoice1').jqGrid('delRowData',jqGridRowId);
	 	}
	 //$( "#vendorinvoice1_iladd" ).trigger( "click" );
	 jQuery("#vendorinvoice1").jqGrid("setSelection", "new_row");
	 jQuery("#new_row_prItemCode").focus();
	 setgridtotal();
}