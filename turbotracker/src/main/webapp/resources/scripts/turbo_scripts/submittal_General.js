jQuery(document).ready(function() {
	var todayDate = '';
	$(".datepicker").datepicker();
	$("#revisionlable").css("display", "none");
	$("#revisiontext").show();
	$("#architectsList").val($("#jobMain_architectsList").val());
	$("#engineersRXList").val($("#jobMain_engineersRXList").val());
	$("#salesRep").val($("#jobMain_salesRepsList").val());
	var revisionDropDown=$("#revision_ID").val();
	$("#revisionID option[value='"+revisionDropDown+"']").attr("selected", "selected");
	var planDateText = customFormatDate($("#planDate_ID").val());
	if(planDateText !== ""){
		document.getElementById("plansDate").checked=true;
		document.getElementById("datePlan").disabled = false;
	}else if(planDateText === ""){
		date = new Date();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var year = date.getFullYear();
		if (document.getElementById("datePlan").value === ''){
			 todayDate = month+'/'+day+'/'+year;
			$("#datePlan").val(todayDate);
			document.getElementById("plansDate").checked=false;
			document.getElementById("datePlan").disabled = false;
		} 
	}
	var date = customFormatDate($("#submittal_ID").val());
	var revisionLable;
	if(date === ""){
		revisionLable = "Created"+" "+$("#submittalName_ID").val()+" "+todayDate;
	}else{
		revisionLable = "Created"+" "+$("#submittalName_ID").val()+" "+date;
	}
	$("#submittalDate").val(date);
	$("#revisiontext").text(revisionLable);
	$("#datePlan").val(planDateText);
});
		
function plansDateShow(){
	if ($('#plansDate').is(':checked')) { 
		document.getElementById("datePlan").disabled = true;
	}else{
		document.getElementById("datePlan").disabled = false;
	}
}

$(function() { var cache = {}; var lastXhr='';
	$( "#Submittal" ).autocomplete({ minLength: 1,timeout :1000, select: function( event, ui ) { var id = ui.item.id; $("#submittalhidden").val(id);},
	source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/userInitials", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#Signed" ).autocomplete({ minLength: 1,timeout :1000, select: function( event, ui ) { var id = ui.item.id; $("#signedhidden").val(id);},
	source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/userInitials", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} }); });

var revision = $("#revisiontext").val();  var thru = $("#thrutext").val();  var planDate = $("#datePlan").val();  var submittalDate = $("#submittalDate").val();  
var cpoies = $("#copiestext").val();  var submittal = $("#Submittal").val();  var signed = $("#Signed").val();   var remarks = $("#remarksSheet").val(); 
var internal = $("#internalCommand").val(); var sheet = $("#sheetremarks").val();  var comments = $("#commentsinternal").val();

/*$('#revisionID').change(function() {
	var optionTag = '';
  var aResivedID = $(this).val();
  if(aResivedID === '4'){
//	  var addONE = 1;
	  var addTwo = 2;
	  var aResived = [parseInt(revisionID.options.length)-addTwo];
	  for (var index = 0; index < aResived ; index++){
		  optionTag += '<option value="'+index+'">'+index+'</option>';
	  }
	  optionTag += '<option value="4">+ New</option><option value="5">Copy</option></select>';
	  $("#revisionID").empty(); $("#revisionID").append(optionTag); 
	  var aSelectOption = Number(revisionID.options.length)-2;
	  $("#revisionID option[value='"+aSelectOption+"']").attr("selected", "selected");
  }
});*/

/*function revisionBoxOptions(){
	return false;
}*/

function revisionBoxOptions(){
	var chosenoption = $("#revisionID").val();
	if (chosenoption === '4') {
			optionTag = '<option value="0">0</option><option value="1">1</option><option value="2">2</option>'+
	    							  '<option value="3">3</option><option value="4">Add New</option><option value="5">Copy</option></select>';
		$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='0']").attr("selected", true);
		$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
		$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
		date = new Date(); var month = date.getMonth()+1; var day = date.getDate(); var year = date.getFullYear();
		var todayDate = '';
		if (document.getElementById("datePlan").value === ''){
			todayDate = month+'/'+day+'/'+year;
			$("#datePlan").val(todayDate);
		} document.getElementById("plansDate").checked=false; document.getElementById("datePlan").disabled = false;
		 var revisionLable = "Created"+" "+$("#submittalName_ID").val()+" "+todayDate; $("#revisiontext").text(revisionLable); $("#datePlan").val(planDate);
		$("#revisiontext").val(""); $("#thrutext").val(""); $("#datePlan").val(""); $("#submittalDate").val(""); $("#signedsearchimage").val(""); $("#searchimage").val("");
		$("#copiestext").val(""); $("#Submittal").val(""); $("#Signed").val(""); $("#remarksSheet").val(""); $("#internalCommand").val(""); $("#revisionlable").hide();
		$("#sheetremarks").val("");$("#commentsinternal").val("");
	} else if (chosenoption === 5) {
		 optionTag = '<option value="4">Add New</option><option value="5">Copy</option></select>';
		$("#revisionID").empty(); $("#revisionID").append(optionTag);
		$("#revisiontext").css("display", "none"); $("#thrutext").css("display", "none"); $("#datePlan").css("display", "none"); $("#submittalDate").css("display", "none");
		$("#copiestext").css("display", "none"); $("#Submittal").css("display", "none"); $("#remarksSheet").css("display", "none"); $("#signedsearchimage").css("display", "none");
		$("#internalCommand").css("display", "none"); $("#revisionlable").show(); $("#searchimage").css("display", "none"); $("#Signed").css("display", "none");
	} else if (chosenoption === 0) {
		optionTag = '<option value="0">0</option><option value="1">1</option>'+
								'<option value="4">Add New</option><option value="5">Copy</option></select>'; 
		$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='0']").attr("selected", true);
		$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
		$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
		$("#revisiontext").val(revision);   $("#thrutext").val(thru);   $("#datePlan").val(planDate);   $("#submittalDate").val(submittalDate);  
		 $("#copiestext").val(cpoies);   $("#Submittal").val(submittal);   $("#Signed").val(signed);    $("#remarksSheet").val(remarks); 
		 $("#internalCommand").val(internal);  $("#sheetremarks").val(sheet);   $("#commentsinternal").val(comments);
	} else if (chosenoption === 1) {
		optionTag = '<option value="0">0</option><option value="1">1</option><option value="2">2</option>'+
								 '<option value="4">Add New</option><option value="5">Copy</option></select>'; 
		$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='1']").attr("selected", true);
		$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
		$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
		$("#revisiontext").val(revision);   $("#thrutext").val(thru);   $("#datePlan").val(planDate);   $("#submittalDate").val(submittalDate);  
		 $("#copiestext").val(cpoies);   $("#Submittal").val(submittal);   $("#Signed").val(signed);    $("#remarksSheet").val(remarks); 
		 $("#internalCommand").val(internal);  $("#sheetremarks").val(sheet);   $("#commentsinternal").val(comments);
	} else if (chosenoption === 2) {
		optionTag = '<option value="0">0</option><option value="1">1</option><option value="2">2</option>'+
									 '<option value="3">3</option><option value="4">Add New</option><option value="5">Copy</option></select>';
		$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='2']").attr("selected", true);
		$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
		$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
		$("#revisiontext").val(revision);   $("#thrutext").val(thru);   $("#datePlan").val(planDate);   $("#submittalDate").val(submittalDate);  
		 $("#copiestext").val(cpoies);   $("#Submittal").val(submittal);   $("#Signed").val(signed);    $("#remarksSheet").val(remarks); 
		 $("#internalCommand").val(internal);  $("#sheetremarks").val(sheet);   $("#commentsinternal").val(comments);
	}else if (chosenoption === 3) {
		optionTag = '<option value="0">0</option><option value="1">1</option><option value="2">2</option>'+
									  '<option value="3">3</option><option value="4">Add New</option><option value="5">Copy</option></select>';
		$("#revisionID").empty(); $("#revisionID").append(optionTag); $("#revisionID option[value='3']").attr("selected", true);
		$("#revisiontext").show(); $("#thrutext").show(); $("#datePlan").show(); $("#submittalDate").show(); $("#signedsearchimage").show(); $("#searchimage").show();
		$("#copiestext").show(); $("#Submittal").show(); $("#Signed").show(); $("#remarksSheet").show(); $("#internalCommand").show(); $("#revisionlable").hide();
		$("#revisiontext").val(revision);   $("#thrutext").val(thru);   $("#datePlan").val(planDate);   $("#submittalDate").val(submittalDate);  
		 $("#copiestext").val(cpoies);   $("#Submittal").val(submittal);   $("#Signed").val(signed);    $("#remarksSheet").val(remarks); 
		 $("#internalCommand").val(internal);  $("#sheetremarks").val(sheet);   $("#commentsinternal").val(comments);
	}
}
		
/*	var selectmenu=document.getElementById("revisionID");
	selectmenu.onchange=function(){ 
	 var chosenoption=this.options[this.selectedIndex];
	 if (chosenoption.text == "+ new"){
	 	jQuery("#submittal").dialog("close");
	 	jQuery("#submittalNew").dialog("open");
	}else if(chosenoption.value == 1){
		var optionTag = '<option value="1">0</option><option value="2">1</option>' +
								'<option value="3">+ new</option><option value="4">Copy</option></select>';
		$("#revisionID").empty();
		$("#revisionID").append(optionTag);
		$("#revisionID option[value='2']").attr("selected", true);
		}
	} 	*/
		
function saveSubmittal(){
	 createtpusage('job-Submittal Tab','Save Submittal','Info','job-Submittal Tab,Save Submittal,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var auserInitials= $("#userInitials").val();
	$("#Submittal").val(auserInitials);
	var selectmenu = document.getElementById("revisionID");
	var submittalGeneralFrom = $("#submittalForm").serialize();
	var jobNumber = $("#jobNumber_ID").text();
	var remarks = $('#sheetremarks').val();
	var jobSubmittalFormQryStr = "jobName=" + jobNumber + "&" + submittalGeneralFrom+"&remarks="+remarks+"&joMasterID="+ $("#joMaster_ID").text();
	if(selectmenu.value === 5){
		return false;
	}
	 var aRevision = $("#revisionID").val();
	 if((aRevision !== '5' ||aRevision !== '4') && $("#submittalMainHeaderID").val() !== ''){
		$.ajax({
			type:'POST',
			url: "./jobtabs3/updateSubmittal",
			data: jobSubmittalFormQryStr,
			success: function(data) {
				jQuery( "#generalSubmittal" ).dialog("close");
			}
	   });
	}else{
		$.ajax({
			type : 'POST',
			url : "./jobtabs3/createSubmittal",
			data : jobSubmittalFormQryStr,
			success : function(data) {
				var submitaalhaderID = data.joSubmittalHeaderId;
				$("#submittalMainHeaderID").val(submitaalhaderID);
				jQuery( "#generalSubmittal" ).dialog("close");
				$("#tabs_main_job").tabs( "load" , 2);
			}
		});
	}
	return true;
}

function cancelSubmittal(){
	 createtpusage('job-Submittal Tab','Cancel Submittal','Info','job-Submittal Tab,Cancelling Submittal,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	 jQuery("#generalSubmittal").dialog("close");
	 $("#submittalList").trigger("reloadGrid");  
	 return true;
}