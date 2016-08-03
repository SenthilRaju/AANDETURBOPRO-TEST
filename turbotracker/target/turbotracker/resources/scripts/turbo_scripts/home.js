var message = $("#homeHiddenUseNameID").val();
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
	recentlyOpenedJob();
	getWeatherForecast();
 });

function recentlyOpenedJob(){
	$("#lastOpenedJobs").jqGrid({
		url:'./job_controller/jobHistory',
		datatype:'JSON',
		mtype:'POST',
		pager:'',
		colNames:['Job #', 'Job', 'Date' ,'Bid Date'],
		colModel:[
		           	{name:'jobNumber', index:'jobNumber', align:'left', width:20, editable:true,hidden:true},
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
			var rowData = jQuery(this).getRowData(rowId); 
			var jobNumber = rowData['jobNumber'];
			var jobName = $.trim(rowData['jobName']);
			var jobStatus = "";
			$.ajax({
				url: "./job_controller/jobStatusHome", 
				mType: "GET", 
				data : { 'jobNumber' : jobNumber },
				success: function(data){ 
					jobStatus = data;
					jobName = jobName.replace(/&/g, "``");
					jobName = jobName.replace(/#/, "__");
					var aQryStr = "./jobflow?token=view&jobNumber="+jobNumber +"&jobName="+jobName + "&jobStatus="+ jobStatus;
					document.location.href = aQryStr;
				}
		 	});
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
			var value = name.replace("[","`");
			var job = value.split("`");
			var jobName = job[0];
			jobName = "`"+jobName+"`";
			var number = job[1];
			var jobNumber = number.replace("]"," ");
			$.ajax({
				url:"./search/searchjobcustomer",mType:"GET",data:{'jobnumber':jobNumber,'jobname':jobName},
				success: function(data) {
					$.each(data, function(index, value){
						status = value.jobStatus;
						jobCustomer = value.customerName;
					});
					if(jobCustomer === null) {
						jobCustomer = "";
					}
					updateJobstatus(jobNumber,status);
					jobName=jobName.replace("`", "");
					jobName = jobName.replace("`", "");
					window.location.href = "./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+jobName+"&jobCustomer="+jobCustomer+"&jobStatus="+status+"";
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
			$.ajax({
				url: "./search/searchrolodex",
				mType: "GET",
				data : {'rolodex': name},
				success: function(data){
					 var rxId="";
					$.each(data, function(index, value){
						entityValue = value.entity;
						rxId =value.pk_fields; 
					});
					var value = name.split(": ");
					var entity = value[0];
					var text = value[1];
					var text1 = text.split(",  ");
					var searchText = text1[0];
					var search = searchText.replace('&','and');
					var search1= search.replace('&','and');
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
					location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+'`'+search1+'`'+"";
				},
				error: function(Xhr) {
					var errorText = $(Xhr.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				}
			});
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
				var aPoId = ui.item.id;
				
				//var aQryStr = "aVePOId=" + aValue;
				location.href = "./editpurchaseorder?token=view&aVePOId="+aPoId;
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
	document.location.href = "./jobflow?token=new";
}
function showSearchError(){
	
	$('#errormsg').html('<b>'+errormessage+'</b>');$("#errormsg").fadeIn();$("#errormsg").delay(1000).fadeOut();
}	
function getJobs() {
	var jobSearch = $('#jobsearch').val();
	if(jobSearch === ""){
		errormessage="please select a job to search";
		showSearchError();
		return false;
	}
	$.ajax({
		url: "./search/searchjob",
		mType: "GET",timeout: 1000,
		data : {'jobsearch': $('#jobsearch').val()},
		success: function(data){
			var searchArray = new Array();
			$.each(data, function(index, value) { 
				searchArray[index] = value.searchText;
			});
			if(searchArray.length==0){
				errormessage="Please provide a valid job from the suggested list.";
				showSearchError();
			}
				
			var job = searchArray.toString();
			var number = job.split("|");
			var jobNumber = number[0];
			var jobName = number[1];
			jobName = jobName.replace(/&/g, "``");
			jobName = jobName.replace(/#/, "__");
			$.ajax({
				url: "./search/searchjobcustomer",
				mType: "GET",
				data : {'jobnumber': jobNumber, 'jobname': jobName},
				success: function(data){
					$.each(data, function(index, value){
						status = value.jobStatus;
						jobCustomer = value.customerName;
					});
					
					if(jobCustomer === null) {
						jobCustomer = "";
					}
					document.location.href="./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+jobName+"&jobCustomer="+jobCustomer+"&jobStatus="+status+"";
				},
				error: function(Xhr) {
				}
			});
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
	return true;
	
}

function getPO(){
	var poSearch = $('#posearch').val();
	if(poSearch === ""){
		
		errormessage="Please select a PO to search";
		showSearchError();
		return false;
		
	}
	return true;
}