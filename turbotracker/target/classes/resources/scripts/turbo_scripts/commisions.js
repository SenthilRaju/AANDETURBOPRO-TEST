var currentPeriodID=0;

jQuery(function () {
	//$("#searchJob").prop('id', 'searchProduct');
	//$("#searchJob").removeClass("ui-autocomplete-input");
	//$("#searchJob").removeClass("ui-autocomplete-loading");
	jQuery( "#employeeCommissionsPopupDialog" ).dialog({
		autoOpen: false,
		height: 390,
		width: 605,
		//top: 55,
		left: 185,
		title:"Commissions",
		modal: true,
		buttons:{		},
		close: function () {
			return true;
		}
	});
	return true;	
});

jQuery(document).ready(function(){
		/*$("#LoadingDialog").hide();*/
		$("#LoadingDialog").css({visibility:"hidden"});
	    loadCommissionsList(-1);
	    $("#search").hide();
	    $('#endperioddate').datepicker();
	    $('#newperioddate').datepicker();
		var date = $.datepicker.formatDate('mm/dd/yy', new Date());
		
		loadCurperiodID();
		loadAllDates();
		
		
		jQuery("#NewPeriodDialog").dialog({
			autoOpen:false,
			width:500,
			title:"",
			modal:true,
			buttons:{
				"Begin New Period":function(){
					
					$.ajax({
						url: "./employeeCrud/checkPeriodExists",
						type: "POST",
						data : { 'newPeriodDate' : $('#newperioddate').val()},
						success: function(data){
							console.log('Date Exists:'+data);
							createtpusage('Company-Employee-Employee Commission','New Period','Info','Company-Employee-Employee Commission,Begin New Period,newPeriodDate:'+$('#newperioddate').val());
							if(data==1){
								var errorText = "<b style='color:red; align:right;'>New period should be greater than previous periods date.</b>";
								$("#newDateErrorMsg").css("display", "block");
								$('#newDateErrorMsg').html(errorText);
								setTimeout(function() {
									$('#newDateErrorMsg').html("");
								}, 2000);
							}else{
								/*$("#LoadingDialog").show();*/
								$("#LoadingDialog").css({visibility:"visible"});
								$('#NewPeriodDialog').dialog("close");
								$.ajax({
									url: "./employeeCrud/commisionsNewPeriod",
									type: "POST",
									timeout: (20*60*1000),
									data : { 'newPeriodDate' : $('#newperioddate').val()},
									success: function(data){
										loadCurperiodID();
										loadAllDates();
										/*$("#LoadingDialog").hide();*/
										$("#LoadingDialog").css({visibility:"hidden"});
										$("#commissionsGridList").jqGrid('GridUnload');
										loadCommissionsList(-1);
										$("#commissionsGridList").trigger("reloadGrid");
									},
									 error: function( xhr, status, errorThrown ) {
										/* $("#LoadingDialog").hide();*/
										    $("#LoadingDialog").css({visibility:"hidden"});
								        	$("#commissionsGridList").jqGrid('GridUnload');
											loadCommissionsList(-1);
											$("#commissionsGridList").trigger("reloadGrid");
											
											if(status=='timeout'){
												console.log( "Sorry, there was a "+status+" problem! in Create New Period" );
											}else{
												console.log( "Sorry, there was a "+status+" problem! in Create New Period" );
											}
									        console.log( "Error: " + errorThrown );
									        console.log( "Status: " + status );
									        console.dir( xhr );
									    },
									    complete: function( xhr, status ) {
									    	/*$("#LoadingDialog").hide();*/
									    	$("#LoadingDialog").css({visibility:"hidden"});
								        	$("#commissionsGridList").jqGrid('GridUnload');
											loadCommissionsList(-1);
											$("#commissionsGridList").trigger("reloadGrid");
									        console.log( "The request is complete!" +status);
									    }
							 	});
								
							}
						}
				 	});
					
					
					},"Cancel":function(){
						$(this).dialog("close");
					}
					},
			close:function(){
			return true;
			}	
		});
		
		
		jQuery("#ReCalculateDialog").dialog({
			autoOpen:false,
			width:500,
			title:"? Question",
			modal:true,
			buttons:{
				"Yes":function(){
					$("#ReCalculateDialog").dialog("close");
					//callLoading();
					console.log('EndDate ON REcal:'+$('#endperioddate').val());
					$("#LoadingDialog").css({visibility:"visible"});
					/*$("#LoadingDialog").show();*/
					$.ajax({
				        url: './employeeCrud/reCalculateCommission?endDate='+$('#endperioddate').val()+'&calculatePayment=yes',
				        type: 'POST',
				        timeout: (20*60*1000),
				        success: function (data) {
				        	console.log('Insert Success /employeeCrud/reCalculateCommission :'+data);
				        	//$("#LoadingDialog").dialog("close");
				        	createtpusage('Company-Employee-Employee Commission','Recalculate-Yes','Info','Company-Employee-Employee Commission,Recalculate-Yes,endDate:'+$('#endperioddate').val());
				        	$("#LoadingDialog").css({visibility:"hidden"});
				        	/*$("#LoadingDialog").hide();*/
				        	$("#commissionsGridList").jqGrid('GridUnload');
							loadCommissionsList(-1);
							$("#commissionsGridList").trigger("reloadGrid");
				        },
				        error: function( xhr, status, errorThrown ) {
				        	$("#LoadingDialog").css({visibility:"hidden"});
				        	/*$("#LoadingDialog").hide();*/
				        	$("#commissionsGridList").jqGrid('GridUnload');
							loadCommissionsList(-1);
							$("#commissionsGridList").trigger("reloadGrid");
							if(status=='timeout'){
								console.log( "Sorry, there was a "+status+" problem in /employeeCrud/reCalculateCommission!" );
							}else{
								console.log( "Sorry, there was a "+status+" problem in /employeeCrud/reCalculateCommission!" );
							}
					        console.log( "Error: " + errorThrown );
					        console.log( "Status: " + status );
					        console.dir( xhr );
					    },
					    complete: function( xhr, status ) {
					    	$("#LoadingDialog").css({visibility:"hidden"});
					    	/*$("#LoadingDialog").hide();*/
				        	$("#commissionsGridList").jqGrid('GridUnload');
							loadCommissionsList(-1);
							$("#commissionsGridList").trigger("reloadGrid");
					        console.log( "The request is complete!" );
					        console.log(status);
					    }
				    });
					},"No":function(){

						$("#ReCalculateDialog").dialog("close");
						//callLoading();
						console.log('EndDate ON REcal:'+$('#endperioddate').val());
						/*$("#LoadingDialog").show();*/
						$("#LoadingDialog").css({visibility:"visible"});
						$.ajax({
					        url: './employeeCrud/reCalculateCommission?endDate='+$('#endperioddate').val()+'&calculatePayment=no',
					        type: 'POST',       
					        success: function (data) {
					        	console.log('Insert Success:'+data);
					        	//$("#LoadingDialog").dialog("close");
					        	createtpusage('Company-Employee-Employee Commission','Recalculate-No','Info','Company-Employee-Employee Commission,Recalculate-No,endDate:'+$('#endperioddate').val());
					        	/*$("#LoadingDialog").hide();*/
					        	$("#LoadingDialog").css({visibility:"hidden"});
					        	$("#commissionsGridList").jqGrid('GridUnload');
								loadCommissionsList(-1);
								$("#commissionsGridList").trigger("reloadGrid");
					        }
					    });
						
					},"Cancel":function(){
						$(this).dialog("close");
					}
					},
			close:function(){
			return true;
			}	
		});
		
		jQuery("#ReversePeriodDialog").dialog({
			autoOpen:false,
			width:500,
			title:"Please Confirm",
			modal:true,
			buttons:{
				"OK":function(){
					var accText = $("#rpAcceptTextID").val();
					accText = accText.toUpperCase();
					if(accText==='AGREE'){
						/*$("#LoadingDialog").show();*/
						$("#LoadingDialog").css({visibility:"visible"});
						$("#ReversePeriodDialog").dialog("close");
						$.ajax({
					        url: './employeeCrud/reverseCommissionPeriod',
					        type: 'POST',       
					        success: function (data) {
					        	console.log('Insert Success:'+data);
					        	//$("#LoadingDialog").dialog("close");
					        	$("#LoadingDialog").css({visibility:"hidden"});
					        	createtpusage('Company-Employee-Employee Commission','Reverse Period','Info','Company-Employee-Employee Commission,Reverse Period');
					        	/*$("#LoadingDialog").hide();*/
					        	document.location.href ="./employeeCommissions";
					        }
					    });
					}
					else{
						var errorText = "<b style='color:red; align:right;'>Please Type 'agree' for Reverse to the previous period</b>";
						$("#reversePeriodErrorMsg").css("display", "block");
						$('#reversePeriodErrorMsg').html(errorText);
						setTimeout(function() {
							$('#reversePeriodErrorMsg').html("");
						}, 2000);
					}
					},"Cancel":function(){
						$(this).dialog("close");
					}
					},
			close:function(){
			return true;
			}	
		});
		
		function callLoading(){
			jQuery("#LoadingDialog").dialog({
				autoOpen:false,
				width:500,
				//title:"Please Confirm",
				modal:true
			});
		}
		
});

function loadCurperiodID(){
	$.ajax({
		url: "./employeeCrud/getEndingPeriod",
		type: "POST",
		success: function(data){
			currentPeriodID =data;
			console.log('Current PeriodID:'+currentPeriodID);
		}
 	});
}

function loadAllDates(){
	
	$.ajax({
        url: './employeeCrud/getEndingPeriodList',
        type: 'POST',       
        success: function (data) {
        	console.log("all--->"+data);
        	checkRefs="<option value='0'>-Select-</option>";
			$.each(data, function(key, valueMap) {								
					if("endingPeriodList" == key)
					{
						
						$.each(valueMap, function(index, value){
							if(value.ecPeriodId != '')
								if(value.ecPeriodId==currentPeriodID){
									checkRefs+='<option value="'+value.ecPeriodId+'" selected="selected">'+value.endDate+'</option>';
									
								}else{
									checkRefs+='<option value='+value.ecPeriodId+'>'+value.endDate+'</option>';
								}
						
						}); 
					}
											
			});
			//$("#LoadingImage").hide();
			$('#endperioddate').html(checkRefs);
        }
    });
}

function loadCommissionsList(periodID){
	
	$.ajax({
		
        url: './employeeCrud/reCalculateStatus?endDate='+periodID,
        type: 'POST',       
        success: function (data) {
        	if(data==1){
        		$("#recalculateButton" ).removeClass( "savehoverbutton turbo-blue" );
        		$("#recalculateButton" ).addClass( "savehoverbutton turbo-grey" );
        		$("#recalculateButton").prop("onclick", null);
        		$("#recalculateButton").css("cursor", "default");
        		
        		$("#reversePeriodButton" ).removeClass( "savehoverbutton turbo-blue" );
        		$("#reversePeriodButton" ).addClass( "savehoverbutton turbo-grey" );
        		$("#reversePeriodButton").prop("onclick", null);
        		$("#reversePeriodButton").css("cursor", "default");
        		 
        		 
        	} else {
        		$("#recalculateButton" ).removeClass( "savehoverbutton turbo-grey" );
        		$("#recalculateButton" ).addClass( "savehoverbutton turbo-blue" );
        		$('#recalculateButton').attr('onclick', 'clickRecalculate()');
        		$("#recalculateButton").css("cursor", "pointer");
        		
        		$("#reversePeriodButton").removeClass( "savehoverbutton turbo-grey" );
        		$("#reversePeriodButton").addClass( "savehoverbutton turbo-blue" );
        		$('#reversePeriodButton').attr('onclick', 'clickReversePeriod()');
        		$("#reversePeriodButton").css("cursor", "pointer");
        	}
        	console.log('Insert Success:'+data);
        }
    });
	
	$("#commissionsGridList").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		url:'./employeeCrud/employeeCommisions',
		postData: {'endPeriodID':periodID },
		//pager: jQuery('#commissionsGridPager'),
		colNames:['User Id', 'Full Name', 'Commissions', 'Adjusments', 'Payments','','','','', 'EcStatement'],
	   	colModel:[
			{name:'userLoginId',index:'userLoginId', width:50,editable:true, hidden: true, editrules:{required:true}, editoptions:{size:10}},
			{name:'webName',index:'webName', width:200,editable:true,align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'jobCommissions',index:'jobCommissions',align:'right', width:150,editable:true, editrules:{required:true}, formatter:customCurrencyFormatter, editoptions:{size:10}},
			{name:'adjustments',index:'adjustments',  align:'right', width:150,editable:true, editrules:{required:true}, formatter:customCurrencyFormatter, editoptions:{size:10}},
			{name:'payment',index:'payment',align:'right', width:150,editable:true, editrules:{required:true}, formatter:customCurrencyFormatter, editoptions:{size:10}},
			{name:'statement',index:'statement',align:'right',width:40,editable:true,hidden: false, edittype:'text', formatter:imgSearchFmatter,  editoptions:{size:20},editrules:{edithidden: true,required:false}},
			{name:'adjustment',index:'adjustment',align:'right',width:40,editable:true,hidden: false, edittype:'text', formatter:imgCurrencyFmatter,  editoptions:{size:20},editrules:{edithidden: true,required:false}},
			{name:'csv',index:'csv',align:'right',width:40,editable:true,hidden: false, edittype:'text', formatter:imgCsvFmatter,  editoptions:{size:20},editrules:{edithidden: true,required:false}},
			{name:'emailstatement',index:'emailstatement',align:'right',width:40,editable:true,hidden: false, edittype:'text', formatter:imgEmailFmatter,  editoptions:{size:20},editrules:{edithidden: true,required:false}},
			{name:'ecStatementID',index:'ecStatementID', width:50,editable:true, hidden: true, editrules:{required:true}, editoptions:{size:10}}],
	   	rowNum: 0,
		pgbuttons: false,
		altRows: true,
		altclass:'myAltRowClass',	
		recordtext: '',
		viewrecords: true,
		//pager: '#commissionsGridPager',
		sortname: '', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Commissions',
		height:550,	width: 1150,/*scrollOffset:0,*/ rownumbers:true,
		loadComplete: function(data) { },
		loadError : function (jqXHR, textStatus, errorThrown) {
			var errorText = $(jqXHR.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:RED;">Error: ' + errorText + '</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");}}]}).dialog("open");
			return false;
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
    	ondblClickRow: function(rowid) { }
	}).navGrid('#commissionsGridPager',
			{add:false,edit:false,del:false,refresh:false,search:false});
}

function imgSearchFmatter(cellValue, options, rowObject){
	var element = "<div><a onclick='viewStatement("+rowObject['ecStatementID']+")'><img src='./../resources/images/search.png' title='View Statement' align='middle' style='padding: 2px 20px;'></a></div>";
  	 return element;
}

function generateEmployeeCommissionsXl(ecStatementID) {
	var endperioddate=$('#endperioddate option:selected').text();
	if($('#endperioddate').val()=="-1"){
		endperioddate="";
	}
	window.open('./employeeCrud/generateEmployeeCommissionsCSV?endperioddate='+endperioddate+'&ecStatementID='+ecStatementID);
	return false;
}

function imgCsvFmatter(cellValue, options, rowObject){
	var element = "<div><a onclick='generateEmployeeCommissionsXl("+rowObject['ecStatementID']+")'>"+
		"<img src='./../resources/images/csv.png' title='CSV Export' align='middle' style='padding: 2px 20px;'></a></div>";
  	 return element;
}
function imgEmailFmatter(cellValue, options, rowObject){
	var element = "<div><a onclick='emailEcStatement()'>"+
		"<img src='./../resources/images/email.png' title='Email Statement' align='middle' style='padding: 2px 20px;'></a></div>";
  	 return element;
}
function imgCurrencyFmatter(cellValue, options, rowObject){
	var element = "<div><a onclick='employeeCommissionsPopupDialog("+rowObject['userLoginId']+", "+rowObject['ecStatementID']+")'>"+
		"<img src='./../resources/images/dollar.png' title='Adjustments to Payments' align='middle' style='padding: 2px 20px;'></a></div>";
  	 return element;
}

function generateEmployeeCommissionsCSV()
{
	var endperioddate=$('#endperioddate option:selected').text();
	if($('#endperioddate').val()=="-1"){
		endperioddate="";
	}
	window.open('./employeeCrud/generateEmployeeCommissionsCSV?endperioddate='+endperioddate+'&ecStatementID='+ecStatementID);
	return false;
}

function generateEmployeeCommissionsSheet()
{
	var endperioddate=$('#endperioddate option:selected').text();
	if($('#endperioddate').val()=="-1"){
		endperioddate="";
	}
	window.open('./employeeCrud/generateEmployeeCommissionsSheet?endperioddate='+endperioddate+'&periodID='+$('#endperioddate').val());
	return false;
}

function viewStatement(ecStatementID){
	var endperioddate=$('#endperioddate option:selected').text();
	if($('#endperioddate').val()=="-1"){
		endperioddate="";
	}
	var url="./employeeCrud/viewEmployeeCommissionStatement?endperioddate="+endperioddate+"&ecStatementID="+ecStatementID;
	window.open(url);
	return false;
	
}

function employeeCommissionsPopupDialog(userLoginId, ecStatementID) {
	$.ajax({
		url: './employeeCrud/getEmployeeAdjustmentToPayment?ecStatementID='+ecStatementID+'&endPeriodID='+$('#endperioddate').val()+'&userLoginID='+userLoginId,
		type: 'GET',
		success: function(data) {
			var d = data.split(",");
			$("#employeeCommissionLabel1").text(d[0]);
			$("#employeeCommissionLabel2").text(d[1]);
			$("#employeeCommissionLabel3").text(d[2]);
			$("#employeeCommissionLabel4").text(d[3]);
			$("#userLoginID").val(userLoginId);
			$("#ecStatementID").val(ecStatementID);
			
			if(d[4] != null)
				$('#txt1InputBx').val(d[4].slice(0,-2));
			else
				$('#txt1InputBx').val('0.00');
			
			if(d[5] != null)
				$('#txt2InputBx').val(d[5].slice(0,-2));
			else
				$('#txt12nputBx').val('0.00');
			
			if(d[6] != null)
				$('#txt3InputBx').val(d[6].slice(0,-2));
			else
				$('#txt3InputBx').val('0.00');
			
			if(d[7] != null)
				$('#txt4InputBx').val(d[7].slice(0,-2));
			else
				$('#txt4InputBx').val('0.00');
			
			if(d[8] != null)
				$('#txt5InputBx').val(d[8].slice(0,-2));
			else
				$('#txt5InputBx').val('0.00');
			
			if(d[9] != null && d[9] != "null")
				$('#commentTxt').val(d[9].slice(0,-2));
			else
				$('#commentTxt').val('');
			
			jQuery( "#employeeCommissionsPopupDialog" ).dialog("open");
		}
	});	
}

function saveCommissionAdjustment() {
	var endPeriodID=$('#endperioddate').val()
	var userLoginID = $("#userLoginID").val();
	var ecStatementID = $("#ecStatementID").val();
	var input1txt = $('#txt1InputBx').val();
	var input2txt = $('#txt2InputBx').val();
	var input3txt = $('#txt3InputBx').val();
	var input4txt = $('#txt4InputBx').val();
	var input5txt = $('#txt5InputBx').val();
	if(input1txt==''||input1txt=='undefined' || input1txt==null){
		input1txt='0.00'
	}
	if(input2txt==''||input2txt=='undefined' || input2txt==null){
		input2txt='0.00'
	}
	if(input3txt==''||input3txt=='undefined' || input3txt==null){
		input3txt='0.00'
	}
	if(input4txt==''||input4txt=='undefined' || input4txt==null){
		input4txt='0.00'
	}
	if(input5txt==''||input5txt=='undefined' || input5txt==null){
		input5txt='0.00'
	}
	$.ajax({
		url: './employeeCrud/saveCommissionAdjustment?repDeduct1='+input1txt+
			'&repDeduct2='+input2txt+'&repDeduct3='+input3txt+'&repDeduct4='+input4txt+
			'&endPeriodID='+endPeriodID+'&userLoginID='+userLoginID+'&ecStatementID='+ecStatementID+'&commentTxt='+$('#commentTxt').val()+'&payment='+input5txt,
		type: 'POST',
		success: function(data) {
			if(data === "success")
			{
				$("#commissionsGridList").jqGrid('GridUnload');
				loadCommissionsList(-1);
				$("#commissionsGridList").trigger("reloadGrid");
				jQuery( "#employeeCommissionsPopupDialog" ).dialog("close");
			}
		}
	});
}

function getPrevData(){
	console.log('endperioddate'+$('#endperioddate').val());
	$("#commissionsGridList").jqGrid('GridUnload');
	loadCommissionsList($('#endperioddate').val());
	$("#commissionsGridList").trigger("reloadGrid");
}
function clicknewPeriod(){
	jQuery("#NewPeriodDialog").dialog("open");
}
function clickRecalculate(){
	console.log('Jenith');
	jQuery("#ReCalculateDialog").dialog("open");
}
function clickReversePeriod(){
	jQuery("#ReversePeriodDialog").dialog("open");
}