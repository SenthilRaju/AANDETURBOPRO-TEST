var message = $("#homeHiddenUseNameID").val();
var withorwithoutcreditamt=false;
$('#search').hide();
var jobCustomer="";
var status="";

var errormessage="invalid search";

$('#home_down_image').vTicker({
	speed: 500,
	pause: 3000,
	showItems: 3,
	animation: 'fade',
	mousePause: true,
	height: 180,
	direction: 'up'
});

$(document).ready(function(){
	var Divisionsysvariable=getSysvariableStatusBasedOnVariableName('RequireaDivisionwhencreatingJobs');
	if(Divisionsysvariable!=null && Divisionsysvariable[0].valueLong==1){
		$("#HomeDivisionSpanID").html("*");
		}else{
		$("#HomeDivisionSpanID").html("");
		}
	recentlyOpenedJob();
	getWeatherForecast();
	updateSysteminfodetails();
	
	
	//Openquickbook popup
	jQuery("#QuickbookpopupDiv").dialog({
		autoOpen:false,
		width:900,
		title:"Quick Book",
		modal:true,
		/*buttons:{
			"Save":function(){
				$('.ui-dialog-buttonpane').find('button:first').css('visibility','hidden');
				formsubmitSave();
				//$('#QuickbookpopupDiv').dialog("close");
				 
				},
			"Cancel":function(){
					$('#QuickbookpopupDiv').dialog("close");
					}
				
				},*/
		close:function(){$('#QuickbookpopupForm').validationEngine('hideAll');
		return true;}	
	});
	
	jQuery("#QuickbookpopupAlertDiv").dialog({
		autoOpen:false,
		width:445,
		title:"Quick Book",
		left: 440,
		modal:true,
		buttons:{
			"Ok":function(){
				$('#QuickbookpopupAlertDiv').dialog("close");
				$('#QuickbookpopupDiv').dialog("close");
			}
//			"Cancel":function(){
//					$('#QuickbookpopupAlertDiv').dialog("close");
//					$('#QuickbookpopupDiv').dialog("close");
//			}
			},
			close:function(){ return true;
			return true;}
	});
	
	$("#Quickbookdate").datepicker('setDate', new Date());
	
	var CustomerPOsysvariable=getSysvariableStatusBasedOnVariableName('CustomerPOReqYN');
	if(CustomerPOsysvariable!=null && CustomerPOsysvariable[0].valueLong==1){
		$("#jobCPORequiredSpanID").html("*");
		}else{
		$("#jobCPORequiredSpanID").html("");
		}
	
 });


function SaveQuickBook(){
	formsubmitSave();
}

function closeQuickBook(){
	$('#QuickbookpopupDiv').dialog("close");
}

function recentlyOpenedJob(){
	$("#lastOpenedJobs").jqGrid({
		url:'./job_controller/jobHistory',
		datatype:'JSON',
		mtype:'POST',
		pager:'',
		colNames:['Job #','joMasterID', 'Job', 'Date' ,'Bid Date'],
		colModel:[
		           	{name:'jobNumber', index:'jobNumber', align:'left', width:20, editable:true,hidden:true},
		           	{name:'joMasterID', index:'joMasterID', align:'left', width:20, editable:true,hidden:true},
					{name:'jobName',index:'jobName',align:'left',width:230,formatter:formatter,cellattr:function(rowId, tv, rawObject, cm, rdata){return 'style="white-space:normal"';}},
					{name:'jobOpenedDateStr', index:'jobOpenedDateStr', width:80, align:'center',formatter: dateFormatter,hidden:true},
					{name:'jobBidDateStr', index:'jobBidDateStr', width:80, align:'center',formatter: dateFormatter,hidden:false}
				 ],
		rowNum:13,
		pgbuttons:true,
		recordtext:'',
		viewrecords:true,
		pager:'',
		hidegrid:false,
		//shrinkToFit:true,
		loadtext : 'loading..',
		sortname:'', sortorder:"asc", imgpath:'themes/basic/images', caption:'Recently Opened Jobs',
		height: 'auto', draggable: true, position: 'fixed',  modal: true,	width: 320,/* scrollOffset:0, *//* rownumbers:true,*/ altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) { },
		loadError : function (jqXHR, textStatus, errorThrown) {	},
		ondblClickRow: function(rowId) {
			/*try{
				//var pr = java.lang.Runtime.getRuntime().exec("outlook");	
				return false;
			}catch(err){
				alert(err.message);
				return false;
			}*/
					
			
			var checkpermission=getGrantpermissionprivilage('Main',0);
			if(checkpermission){
			var rowData = jQuery(this).getRowData(rowId); 
			var jobNumber = rowData['jobNumber'];
			var joMasterID=rowData['joMasterID'];
			var jobName = $.trim(rowData['jobName']);
			var jobStatus = "";
			$.ajax({
				url: "./job_controller/jobStatusHome",  
				mType: "GET", 
				data : { 'jobNumber' : jobNumber,'joMasterID':joMasterID },
				success: function(data){ 
					jobStatus = data;
					var urijobname=encodeBigurl(jobName);
					var aQryStr = "./jobflow?token=view&jobNumber="+jobNumber +"&jobName="+urijobname + "&jobStatus="+ jobStatus+"&joMasterID="+joMasterID;
					createtpusage('job-Main Tab','view','Info','Job,Main Tab,View,JobNumber:'+jobNumber); 	
					var checkpermission=getGrantpermissionprivilage('Main',0);
					if(checkpermission){
					document.location.href = aQryStr;
					}
				}
		 	});
			}
		},
		onLoadComplete: function(){
			if (ts.p.shrinkToFit === false) {initwidth += brd*cl;}
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
	});
}

function getWeatherForecast(){
	/**http://api.worldweatheronline.com/free/v1/weather.ashx?q=33915&format=json&num_of_days=5&key=4cg5md4epmkzmc52d77gqvnf*/
	var aLoggedinUserID = $("#homeHiddenUserID").val();
	var zipcode= 30060;
	var apiKeys= "4cg5md4epmkzmc52d77gqvnf";
	var forecastdays= 3;
	var theUrl= "http://api.worldweatheronline.com/free/v1/weather.ashx?q="+zipcode+"&format=JSON&num_of_days="+forecastdays+"&key="+apiKeys;
	 $.ajax({
	        type: 'GET',
	        url: theUrl,
	        async: false,
	        contentType: "application/json",
	        dataType: 'jsonp',
	        success: function(result){
	        	console.log("Webservice call --->"+result.data);
				var currentTemp = result.data.current_condition[0].temp_C;
				var currentWeatherDesc = result.data.current_condition[0].weatherDesc[0].value;
				var currentWeatherImage = result.data.current_condition[0].weatherIconUrl[0].value;
				var dialogHTML = '<table>'+
										'<tr><td colspan="2"><b><label>Current Weather: </label></b></td></tr>'+
										'<tr>'+
											'<td><label>Temperature: '+currentTemp+'<sup> o</sup></label>'+
											'<br/><label>Weather: '+currentWeatherDesc+'</label></td>'+
											'<td><img alt="" height="40" width="40" style="border-radius:9px;border:thin outset;"src="'+currentWeatherImage+'"></td>'+
										'</tr>'+
										'<tr><td colspan="2"><b><label>Forecast: </label></b></td></tr>'+
									'</table><input id="animation" type="checkbox" style="display: none;"></input><div id="scroller"><ul id="list">';
				for ( var index = 0; index < result.data.weather.length; index++) {
					console.log("Index---->"+index);
					var date = result.data.weather[index].date;
//					var maxTempCelc = result.data[index].itsMaxTempCelc;
//					var minTempCelc = result.data[index].itsMinTempCelc;
					var maxTempFH = result.data.weather[index].tempMaxF;
					var minTempFH = result.data.weather[index].tempMinC;
					var weatherDesc = result.data.weather[index].weatherDesc[0].value;
					var weatherIcon = result.data.weather[index].weatherIconUrl[0].value;
					dialogHTML = dialogHTML + '<li><table><tr><td>'+
												'<table>'+
												'<tr><td><b><label>'+date+'</label></b></td><td><img alt="" height="32" width="32"style="border-radius:9px;border:thin outset;" src="'+weatherIcon+'"/></td></tr>'+
												'<tr><td colspan=""><label>Max </label></td><td colspan=""><label>: </label></td><td><label>'+maxTempFH+'<sup> o</sup> F</label></td></tr>'+
												'<tr><td colspan=""><label>Min </label></td><td colspan=""><label>: </label></td><td><label>'+minTempFH+'<sup> o</sup> F</label></td></tr>'+
												'<tr><td colspan=""><label>Weather </label></td><td colspan=""><label>: </label></td><td><label>'+weatherDesc+'</label></td></tr>'+
												'</table>'+
												'</td></tr>'+
												'</table></li>';
				}
				dialogHTML = dialogHTML + '</ul></div>';
				console.log(dialogHTML);
				$('.weatherWidget').html(dialogHTML);
				$('.weatherWidget').append({modal: true, width:500, height:450, title:"Weather Widget", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				
			},
	        error: function (e) {
	            console.log(e.message);
	        }
	    });
	return true;
}

function zipeCode(){
	var aZipCodeID = $("#zipCodeID").val();
	if(aZipCodeID === ''){
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span style="color:red;"><b>Please provide a zipcode.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Error.", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	} else {
		$.ajax({
			type:'POST',
			url: "./homecontroller/userzipcode",
			data:{'zipCodeID':aZipCodeID},
			success: function(data){
				return true;
			},
			error: function(jqXHR, textStatus, errorThrown){
				var errorText = $(jqXHR.responseText).find('u').html();
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
		});
	}
}

function dateFormatter(cellValue, options, rowObject) {
	/**var aDate = cellValue;
	var aDateArray = aDate.split("-");//2012-05-04
	var newDate = "" + aDateArray[1] + "/" + aDateArray[2] + "/" + aDateArray[0];*/
	return cellValue;
}
function formatter(cellValue, options, rowObject) {
	var name = cellValue;
	var name1 = name.replace("`", " ");
	var name2 = name1.replace("`", " ");
	var name3 = name2.replace("`", " ");
	var jobName = name3.replace("`", " ");
	return jobName;
}
$(function() { var cache = {}; var lastXhr='';
	$( "#jobsearch" ).autocomplete({ minLength: 3,timeout :1000,sortResults: true,
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		select: function (event, ui) {
			var name = ui.item.value;
			var joMasterID=ui.item.id;
			var value = name.replace("[","`");
			var job = value.split("`");
			var jobName = job[0];
			jobName = "`"+jobName+"`";
			var number = job[1];
			var jobNumber = number.replace("]"," ");
			$.ajax({
				url:"./search/searchjobcustomer",mType:"GET",data:{'jobnumber':jobNumber,'jobname':jobName,'joMasterID':joMasterID},
				success: function(data) {
							
					$.each(data, function(index, value){
						status = value.jobStatus;
						jobCustomer = value.customerName;
					});
					if(jobCustomer === null) {
						jobCustomer = "";
					}
					updateJobstatus(jobNumber,status,joMasterID);
					var urijobname=encodeBigurl(jobName);
					var uricusname=encodeBigurl(jobCustomer);
					window.location.href = "./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+status+"&joMasterID="+joMasterID;
				},
				error: function(Xhr) {
					var errorText = $(Xhr.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.",buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				}
			});
	      },
		source: function( request, response ) { var term = request.term;
			if(term in cache) {response(cache[term] ); return;}
			lastXhr = $.getJSON( "search/searchjoblist", request, function(data, status, xhr){cache[term]=data; if(xhr===lastXhr){response(data);} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  });
	});
	$(function() { var cache = {}; var lastXhr='';
		$( "#rolodex" ).autocomplete({ minLength: 3,timeout :1000,
			open: function(){ 
				$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./rolodexList?oper=add" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Rolodex</a></b></div>');
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			},
		select: function (event, ui) {
			var name = ui.item.value;
			var rxId=ui.item.pk_field;
			/*$.ajax({
				url: "./search/searchrolodex",
				mType: "GET",
				data : {'rolodex': name},
				success: function(data){
					 var rxId="";
					$.each(data, function(index, value){
						entityValue = value.entity;
						rxId =value.pk_fields; 
					});*/
					var value = name.split(": ");
					var entity = value[0];
					var text = value[1];
					var text1 = text.split(",  ");
					var searchText = text1[0];
					var search = searchText.replace('&','and');
					var search1= search.replace('&','and');
					var searchlist = "";
					var checkpermission=false;
					if(entity == "EMP")	{
						searchlist = entity.replace("EMP","employeedetails");
						checkpermission=getGrantpermissionprivilage('Employees',0);
					}else if(entity == "VEND") {
						searchlist = entity.replace("VEND","vendordetails");
						checkpermission=getGrantpermissionprivilage('Vendors',0);
					}else if(entity == "CUST") {
						searchlist = entity.replace("CUST","customerdetails");
						checkpermission=getGrantpermissionprivilage('Customers',0);
					}else{
						searchlist = "rolodexdetails";
						checkpermission=getGrantpermissionprivilage('Rolodex',0);
					}/*if(entity == "ARCH") {
						searchlist = entity.replace("ARCH","architectDetails");
						checkpermission=getGrantpermissionprivilage('Rolodex',0);
					}if(entity == "ENGR") {
						searchlist = entity.replace("ENGR","engineerDetails");
						checkpermission=getGrantpermissionprivilage('Rolodex',0);
					}if(entity == "ARCH/ENGR"){
						searchlist = entity.replace("ARCH/ENGR","architectDetails");
						checkpermission=getGrantpermissionprivilage('Rolodex',0);
					}if(entity == "G.C") {
						searchlist = entity.replace("G.C","rolodexdetails");
						checkpermission=getGrantpermissionprivilage('Rolodex',0);
					}*/
					
					
					
					
		    		if(checkpermission){
					location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+'`'+search1+'`'+"";
		    		}else{
		    			$("#rolodex").val("");
		    		}
				/*},
				error: function(Xhr) {
					var errorText = $(Xhr.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				}
			});*/
		},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchrolodexlist", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); 
		
		/******************** Inventory Search ***********************/
		$("#inventorysearch").autocomplete({ minLength: 3,timeout :1000,
			open: function(){ 
				$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			},
		select: function (event, ui) {
			var aValue = ui.item.value;
			var valuesArray = new Array();
			valuesArray = aValue.split("|");
			$("#inventorysearch").val(valuesArray[1]);
			var id = valuesArray[0];
			var code = valuesArray[2];
			location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
		},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchinventory", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} });
		
		/******************** PO Search ***********************/
		$("#posearch").autocomplete({ minLength: 3, timeout: 1000,
			open: function(){ 
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			},
			select: function (event, ui) {
				var aValue = ui.item.value;
				var jomasterID = ui.item.id;
				var jobname=ui.item.column1;
				var jobnumber=ui.item.column2;
				var customername=ui.item.column3;
				var urijobname=encodeBigurl(jobname);
				var uricusname=encodeBigurl(customername);
				var status ="";
				
				
				$.ajax({
					url:"./search/searchjobcustomer",mType:"GET",data:{'jobnumber':jobnumber,'jobname':urijobname,'joMasterID':jomasterID},
					async:false,
					success: function(data) {
						$.each(data, function(index, value){
							status = value.jobStatus;
							jobCustomer = value.customerName;
						});
						
					},
					error: function(Xhr) {
						var errorText = $(Xhr.responseText).find('u').html();
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.",buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					}
				});
				
				
				
				document.location.href="./jobflow?token=view&jobNumber="+jobnumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&joMasterID="+jomasterID+"&jobStatus="+status;
				//var aQryStr = "aVePOId=" + aValue;
				//location.href = "./editpurchaseorder?token=view&aVePOId="+aPoId;
				//location.href="./purchaseorder?token=view&aVePOId="+aPoId;
			},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "search/search_po", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) { $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); }
		});
	});


function createNewJob(){
	var checkpermission=getGrantpermissionprivilage('New_Job',0);
	if(checkpermission){
	document.location.href = "./jobflow?token=new";
	}
}
function showSearchError(){
	
	$('#errormsg').html('<b>'+errormessage+'</b>');$("#errormsg").fadeIn();$("#errormsg").delay(1000).fadeOut();
}	
function getJobs() {
	var jobSearch = $('#jobsearch').val();
	//alert(jobSearch);
	if(jobSearch === ""){
		errormessage="please select a job to search";
		showSearchError();
		return false;
	}
	
	$.ajax({
		url: "./search/searchjob",
		mType: "GET",
		data : {'jobsearch': jobSearch},
		success: function(result){
			createtpusage('Home','Search Job','Info','Home,Searching Job,Job:'+jobSearch);
			//var searchArray = new Array();
			/*$.each(result, function(index, value) { 
				searchArray[index] = value.searchText;
			});*/
			if(result==null || result.length==0){
				errormessage="Please provide a valid job from the suggested list.";
				showSearchError();
			}
			
			if(result.length==1){
				var job = result[0].searchText;
				var number = job.split("|");
				var jobNumber = number[0];
				var jobName = number[1];
				var joMasterID=result[0].pk_fields;
				jobName = jobName.replace(/&/g, "``");
				jobName = jobName.replace(/#/, "__");
				getjobSubMethod(jobNumber,jobName,joMasterID);
			}else{
				$("#projectID").val(jobSearch);
				searchJOB();
			}
			
		},
		error: function(Xhr) {
			var errorText = $(Xhr.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.",buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		}	
	});
	return true;
}

function getjobSubMethod(jobNumber,jobName,joMasterID){
	$.ajax({
		url: "./search/searchjobcustomer",
		mType: "GET",
		data : {'jobnumber': jobNumber, 'jobname': jobName,'joMasterID':joMasterID},
		success: function(result){
			$.each(result, function(index, value){
				status = value.jobStatus;
				jobCustomer = value.customerName;
			});
			
			if(jobCustomer === null) {
				jobCustomer = "";
			}
			var urijobname=encodeBigurl(jobName);
			var uricusname=encodeBigurl(jobCustomer);
			document.location.href="./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+status+"&joMasterID="+joMasterID;
		},
		error: function(Xhr) {
		}
	});
}

function getRolodex() {
	var rolodexSearch =$('#rolodex').val();
	if(rolodexSearch === ""){
		errormessage="Please select a rolodex to search.";
		showSearchError();
		return false;
		} 
	$.ajax({
		url: "./search/searchrolodex",
		mType: "GET",timeout: 1000,
		data : {'rolodex': $('#rolodex').val()},
		success: function(data){
			
			createtpusage('Home','Search Rolodex','Info','Home,Searching Rolodex,rolodex:'+rolodexSearch);
			
			 var rxId="";
			$.each(data, function(index, value){
				entityValue = value.entity;
				rxId =value.pk_fields; 
			});
			
			var value = $('#rolodex').val().split(":");
			var entity = value[0];
			var text = value[1];
			if(text==null){
				errormessage="Please provide a valid rolodex from the suggested list.";
				showSearchError();
			}
			var text1 = text.split(",");
			var searchText = text1[0];
			if(searchText==null)
			var searchlist = "";
			if(entity == "EMP")	{
				searchlist = entity.replace("EMP","employeedetails");
			}if(entity == "VEND") {
				searchlist = entity.replace("VEND","vendordetails");
			}if(entity == "CUST") {
				searchlist = entity.replace("CUST","customerdetails");
			}if(entity == "ARCH") {
				searchlist = entity.replace("ARCH","architectDetails");
			}if(entity == "ENGR") {
				searchlist = entity.replace("ENGR","engineerDetails");
			}if(entity == "ARCH/ENGR"){
				searchlist = entity.replace("ARCH/ENGR","architectDetails");
			}if(entity == "G.C") {
				searchlist = entity.replace("G.C","rolodexdetails");
			}
			document.location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+searchText+"";
		},
		error: function(Xhr) {
			
		}
	});
	return true;
}

function getInventory(){
	var inventorySearch = $('#inventorysearch').val();
	if(inventorySearch === ""){
		errormessage="Please select an inventory to search";
		showSearchError();
		return false;
		
	}
	createtpusage('Home','Search Inventory','Info','Home,Searching Inventory,Inventory:'+inventorySearch);
	location.href="./inventory?token=view&key="+$('#inventorysearch').val();
	return true;
	
}

function getPO(){
	var poSearch = $('#posearch').val();
	if(poSearch === ""){
		
		errormessage="Please select a PO to search";
		showSearchError();
		return false;
		
	}
	createtpusage('Home','Search PO','Info','Home,Searching PO,PO:'+poSearch);
	return true;
}

function getSysPrivilegeAccess(accessPage) {
	var UserLoginID=$("#homeHiddenUserID").val();
	$.ajax({
	    url: "./getSysPrivilage",
	    data: {'accessPage':accessPage,'userGroupID':0, 'UserLoginID':UserLoginID},
	    type: 'POST',
	    success: function(data){
	    	return data.Value;
	    }, error: function(error) {
			return "denied";
		}
	});
}

function QuickbookPopup(){
//	var returnVal = getSysPrivilegeAccess('Quick_book');
//	alert(" >>>>>>>>>>> "+returnVal);
	var UserLoginID=$("#homeHiddenUserID").val();
	$.ajax({
	    url: "./getSysPrivilage",
	    data: {'accessPage':'Quick_book','userGroupID':0, 'UserLoginID':UserLoginID},
	    type: 'POST',
	    success: function(data){
	    	if(data.Value == "granted")
	    	{
	    		getJobSettingStatus("AllowbookingJobswithnoContractAmount");
	    		document.getElementById("QuickbookpopupForm").reset();
	    		if(withorwithoutcreditamt){
	    			$("#estAmt_labelid").css("display", "none");
	    			$("#conAmt_labelid").css("display", "none");
	    		}else{
	    			$("#estAmt_labelid").css("display", "inline-block");
	    			$("#conAmt_labelid").css("display", "inline-block");
	    		}
	    			var date = new Date();
				var dateVal = (date.getMonth()+1)+"/"+date.getDate()+"/"+date.getFullYear()+" "+date.getHours()+":"+date.getMinutes();
				$("#Quickbookdate").val(dateVal);
				jQuery("#QuickbookpopupDiv").dialog("open");
	    	}
	    	else
	    		showDeniedPopup();
	    }, error: function(error) {
	    	showDeniedPopup();
		}
	});
}

function openNewJob()
{
	var UserLoginID=$("#homeHiddenUserID").val();
	$.ajax({
	    url: "./getSysPrivilage",
	    data: {'accessPage':'New_Job','userGroupID':0, 'UserLoginID':UserLoginID},
	    type: 'POST',
	    success: function(data){
	    	if(data.Value == "granted")
	    	{
	    		location.href = "./jobflow?token=new";
	    	}
	    	else
	    		showDeniedPopup();
	    }, error: function(error) {
	    	showDeniedPopup();
		}
	});		
}

function showDeniedPopup()
{
	var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	return true;
}

$(function() { var cache = {}; var lastXhr=''; $( "#locationCity" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) {var id=ui.item.id; var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
	var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationState").val(stateCode);$("#locationcityid").val(id);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
	}); 

});

$(function() { var cache = {}, lastXhr;
$( "#QuickbookCustomer_name" ).autocomplete({
	minLength: 2, timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#QuickbookCustomerId").val(id);
	$.ajax({
		url: './job_controller/Customerbaseddropdowns',
		type : 'POST',
		data: {'id':id },
		success: function (data){
			if(data!=null){
				if(data.cuAssignmentId1!=null && data.cuAssignmentId1!=""){
					$("#QuickbookCSRId").val(data.cuAssignmentId1);	
					$( "#Quickbook_CSRList" ).val(data.csr);
				}
				if(data.cuAssignmentId0!=null && data.cuAssignmentId0!=""){
					$("#salesRepId").val(data.cuAssignmentId0);
					$( "#Quickbook_salesRepsList" ).val(data.salesrep);
					
				}
				if(data.creditHold !=null && data.creditHold != "")
					$("#CreditHold").val(data.creditHold);
				else
					$("#CreditHold").val(false);
//				alert(data.creditHold+" || "+$("#CreditHold").val());
				if(data.defaultTaxTerritory == 1)
				{
									$("#Quickbook_TaxTerritory").val("");
									$("#TaxTerritory").val("");
									$("#TaxValue").html("0.00%");
					if(data.coTaxID!=null && data.coTaxID != "-1")
						{
									$("#Quickbook_TaxTerritory").val(data.taxName);
									$("#TaxTerritory").val(data.coTaxID);
									$("#TaxValue").html(data.taxRate);
						}
				}
				
			}
		}
	});
	
	},
	source: function( request, response ) { var term = request.term;
		if( term in cache ){ response( cache[ term ] ); return; }
		lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});
});

$(function() { var cache = {}; var lastXhr='';
$( "#Quickbook_CSRList" ).autocomplete({ minLength: 1, timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#QuickbookCSRId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#Quickbook_TaxTerritory" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#TaxTerritory").val(id); var tax = ui.item.taxValue; $("#TaxValue").val(tax); $('#TaxValue').empty(); $("#TaxValue").append(tax+"%");},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });


$(function() { var cache = {}; var lastXhr='';
$( "#Quickbook_salesRepsList" ).autocomplete({ minLength: 1,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesRepId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#QuickBook_salesorderQuote" ).autocomplete({ minLength: 1,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#cusoId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesorderlist", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#QuickbookDivision" ).autocomplete({ minLength: 1,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#QuickbookDivisionid").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/divisionlist", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); 
});

$(".datepickerCustom").datetimepicker();


function checkcustomerisonhold(){
	var returnvalue=false;
	var customerid=$("#QuickbookCustomerId").val();
	
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
function formsubmitSave(){
	//$("#QuickbookpopupDiv").next(".ui-dialog-buttonpane button:contains('Save')").attr("disabled", true);
	console.log("Testing formsubmitsave");
	var Allowcreditlimitradiobutton=false;
	var newDialogDiv = jQuery(document.createElement('div'));
	var allowcheckcreditlimit=getSysvariableStatusBasedOnVariableName("CheckcreditlimitinQuickBook");
	if(allowcheckcreditlimit!=null && allowcheckcreditlimit[0].valueLong==1){
		Allowcreditlimitradiobutton=true;
	}
	var check=checkcustomerisonhold();
	if(Allowcreditlimitradiobutton&&check){
		jQuery(newDialogDiv).html('<span><b>Customer is on Hold.You must take off Hold in order to proceed.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information", 
								buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var valid=formvalidation();
	$('button').prop('disabled', false);
	if(valid){
		$('#QBSaveID').prop('disabled', true);
		$('#QBSaveID').css('background','#B3BDCA');
			var Quickbookprojectid=$("#Quickbookprojectid").val();
			var QuickbookCustomer=$("#QuickbookCustomer_name").val();
			var jobcontractorcustomer=$("#jobcontractorcustomer").val();
			var locationAddress1=$("#locationAddressID1").val();
			var locationAddress2=$("#locationAddressID2").val();
			var locationCity=$("#locationCity").val();
			var locationState=$("#locationState").val();
			var locationZipID=$("#locationZipID").val();
			var Quickbookdate=$("#Quickbookdate").val();
			var salesRepId=$("#salesRepId").val();
			var QuickbookCSRId=$("#QuickbookCSRId").val();
			var QuickbookDivisionid=$("#QuickbookDivisionid").val();
			var TaxTerritory=$("#TaxTerritory").val();
			var taxvalue = $('#TaxValue').text();
			taxvalue = taxvalue.replace('%','');
			//alert(taxvalue);
			var Quickbook_TaxTerritory=$("#Quickbook_TaxTerritory").val();
			//alert(Quickbook_TaxTerritory);
			var QuickbookCustomerId=$("#QuickbookCustomerId").val();
			var contractamount=$("#contractamount").val();
			if(contractamount==null||contractamount==""||contractamount.length==0){
				contractamount="0.0";
			}
			var estimatedcost=$("#estimatedcost").val();
			if(estimatedcost==null||estimatedcost==""||estimatedcost.length==0){
				estimatedcost="0.0";
			}
			var customer_PO=$("#customer_PO").val();
			var date = new Date();
		       var month = date.getMonth()+1;
		       var aDate = "";
		       aDate = aDate + month + "/" + date.getDate() + "/" + date.getFullYear();
	
		       var urlArray = new Array();
		       var urlData = "";
			//alert(Quickbookprojectid+"=="+QuickbookCustomer+"=="+locationAddress1+"=="+locationAddress2+"=="+locationCity+"=="+locationState+"=="+locationZipID+"=="+Quickbookdate+"=="+salesRepId+"=="+QuickbookCSRId+"=="+QuickbookDivisionid+"=="+TaxTerritory+"=="+taxvalue);
			$.ajax({
				url: "./jobtabs3/Quickbookjobsubmit", 
				mType: "GET", 
				data : {'Quickbookprojectid':Quickbookprojectid,'QuickbookCustomer':QuickbookCustomer,'locationAddress1':locationAddress1,
						'locationAddress2':locationAddress2,'locationCity':locationCity,'locationState':locationState,'locationZipID':locationZipID,
						'Quickbookdate':Quickbookdate,'salesRepId':salesRepId,'QuickbookCSRId':QuickbookCSRId,'QuickbookDivisionid':QuickbookDivisionid,
						'TaxTerritory':TaxTerritory,'QuickbookCustomerId':QuickbookCustomerId,'contractamount':contractamount,'jobcontractorcustomer':jobcontractorcustomer,
						'estimatedcost':estimatedcost,'customer_PO':customer_PO,'date':aDate,'Quickbook_TaxTerritory':taxvalue},
				success: function(data){
					//alert(data);
					urlData = "token=view&"; 	
					jQuery("#QuickbookpopupDiv").dialog("close");
					urlArray = data.split('&');
					console.log('ZDataSize:'+urlArray.length);
					for(var a=0;a<urlArray.length;a++){
						console.log(urlArray[a]);
						if((urlArray[a]).indexOf("jobNumber")>-1){
							console.log("jenith you r near :"+urlArray[a]);
							urlData =urlData+urlArray[a]+'&';
						}
						if((urlArray[a]).indexOf("jobName")>-1){
							console.log("jenith you r near :"+urlArray[a]);
							var urijobname=encodeBigurl(urlArray[a]);
							urlData =urlData+urijobname+'&';
						}
						if((urlArray[a]).indexOf("joMasterID")>-1){
							console.log("jenith you r near :"+urlArray[a]);
							urlData =urlData+urlArray[a]+'&';
						}
					}
					urlData = urlData+'jobStatus=Booked&jobType=quick';
					//jobflow?token=view&jobNumber=WEB004&jobName=2014-12-23_QuickJob-3&jobStatus=Booked
					var checkpermission=getGrantpermissionprivilage('Main',0);
					if(checkpermission){
					document.location.href = "./jobflow?"+urlData;
					}
				}
		 	});
	}
}


function formvalidation(){
	//Quickbookprojectid,cusoId,QuickbookDivisionid,Quickbookdate,Quickbookjobtype,locationAddressID1,locationAddressID2,locationcityid,locationState,locationZipID,QuickbookCustomerId
	//salesRepId,customer_PO,QuickbookCSRId,TaxTerritory,contractamount,estimatedcost
	var Divisionsysvariable=getSysvariableStatusBasedOnVariableName('RequireaDivisionwhencreatingJobs');
	var divisionvalidationenordis=false;
	if(Divisionsysvariable!=null && Divisionsysvariable[0].valueLong==1){
		divisionvalidationenordis=true;
	}
	
	var CustomerPOsysvariable=getSysvariableStatusBasedOnVariableName('CustomerPOReqYN');
	
	if($("#Quickbookprojectid").val()=="" || $("#Quickbookprojectid").val().length==0){
	$("#errorDiv").empty();$("#errorDiv").append("*Required project name");
	return false;	
	}else if(divisionvalidationenordis &&($("#QuickbookDivisionid").val()=="" || $("#QuickbookDivisionid").val().length==0)){
	$("#errorDiv").empty();$("#errorDiv").append("*Required Division");
	return false;
	}/*else if($("#Quickbookjobtype").val()=="" || $("#Quickbookjobtype").val().length==0){
	$("#errorDiv").empty();$("#errorDiv").append("*Required job type");	
	return false;
	}*/else if($("#QuickbookCustomer_name").val()=="" || $("#QuickbookCustomer_name").val().length==0){
	$("#errorDiv").empty();$("#errorDiv").append("*Required Customer");
	return false;
	}else if($("#Quickbook_salesRepsList").val()=="" || $("#Quickbook_salesRepsList").val().length==0){
		$("#errorDiv").empty();$("#errorDiv").append("*Required Sales Rep");
		return false;
	}else if($("#Quickbook_TaxTerritory").val()=="" || $("#Quickbook_TaxTerritory").val().length==0){
			$("#errorDiv").empty();$("#errorDiv").append("*Required TaxTerritory");
			return false;
	}else if((CustomerPOsysvariable!=null && CustomerPOsysvariable[0].valueLong==1) && ($("#customer_PO").val()=='' || $("#customer_PO").val().length==0)){
		$("#errorDiv").empty();$("#errorDiv").append("*Required Customer PO");
		return false;
	}
	
	if(!withorwithoutcreditamt){
		if($("#contractamount").val()=="" || $("#contractamount").val().length==0){
			$("#errorDiv").empty();$("#errorDiv").append("*Required Contract Amount");
			return false;
		}
		else if($("#estimatedcost").val()=="" || $("#estimatedcost").val().length==0){
			$("#errorDiv").empty();$("#errorDiv").append("*Required Estimated Cost");
			return false;
		}
		else{
			$("#errorDiv").empty();
			return true;
		}
	}else{
		$("#errorDiv").empty();
		return true;
	}
	
return false;
}
function getJobSettingStatus(Constantvariablename){
	$.ajax({
		url: 'job_controller/getJobSettingStatus',
		async:false,
		mtype : 'GET',
		data: {'Constantvariablename':Constantvariablename},
		success: function (result) {
			if(result!=null){
				
			if(Constantvariablename=='AllowbookingJobswithnoContractAmount'){
					if(result.valueLong==1){
						withorwithoutcreditamt=true;
					}else{
						withorwithoutcreditamt=false;
					}
			}
			
			}else{
				if(Constantvariablename=='AllowbookingJobswithnoContractAmount'){
				withorwithoutcreditamt=false;
				}
				}
				
			
		}
	});
}

function updateSysteminfodetails(){
	
	$.ajax({
		url: './updateSystemInfoDetails',
		type: "GET",
		success: function (Data) {
		}
	});
}



function insertintoemmaster(){
	var betweenfrom=$("#betweenfrom").val();
	var betweento=$("#betweento").val();
	alert(betweenfrom+"=="+betweento);
	$.ajax({
		//url: './requestForInsertintoemmaster',
		//url:'./convertrtftohtmlTemplate',
		url:'./convertrtftohtml',
		type: "GET",
		data: {'betweenfrom':betweenfrom,'betweento':betweento},
		success: function (data) {
			alert("completed insert");
		}
	});
}

function updateposition(){
	alert("start");
	$.ajax({
		//url: './requestForInsertintoemmaster',
		url:'./updateposition',
		type: "GET",
		success: function (data) {
			alert("completed insert");
		}
	});
}

function insertintogl()
{
	$.ajax({
		url:'./insertMissingEntriesingl',
		type: "POST",
		data:{"fromId":$("#fromid").val(),"toId":$("#toid").val()},
		success: function (data) {
			alert(data);
		}
	});
}
