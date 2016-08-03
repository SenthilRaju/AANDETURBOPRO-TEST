/** Document ready function **/
jQuery(document).ready(function() {
var aHoldVal = getUrlVars()["creditHold"];
if(aHoldVal === ''){
}
var archTech = $("#isCategory1").val();
var Engg = $("#isCategory2").val();
//var generalContract = "${requestScope.rxMasterDetails.isCategory3}";
var customer = $("#isCustomer").val(); 
var vendor = $("#isVendor").val();
var Emp = $("#isEmployee").val();

if(Engg !== 'true'){
	$("#engineer").css("display", "none");
}if(customer !== 'true'){
	$("#customer").css("display", "none");
}if(vendor !== 'true'){
	$("#vendor").css("display", "none");
}if(Emp !== 'true'){ 
	$("#employee").css("display", "none");
}if(archTech !== 'true'){
	$("#architect").css("display", "none");
}
/** Load Tabs **/
$(".tabs_main").tabs({
	cache: true,
	ajaxOptions: {
		data: {rolodexID: $("#CustomerId").text() },
		error: function(xhr, status, index, anchor) {
			$(anchor.hash).html("<div align='center' style='height: 386px;padding-top: 200px;'>"+
					"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
					"</label></div>");
		}
	},
	load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
	select: function (e, ui) {
		//window.location.hash = ui.tab.hash;
		var $panel = $(ui.panel);
		if ($panel.is(":empty")) {
			$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'><img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
		}
	}
});
/** Load TAb's Label values **/
$(".datepicker").datepicker();
var name = $("#lableName").val();
var phone =$("#labelPhone").val();
var sting = name.replace('`', ' ');
var sting1 = sting.replace('`', ' ');
var custName = sting1.replace('and', '&');
var custName1 = custName.replace('and', '&');
$('#customerName').empty();	
$('#customerName').append(sting1);
	if(phone !== ''){
		$('#phoneCenter').empty();
		$('#phoneCenter').append(" | ");
		$('#phoneName').empty();
		$('#phoneName').append(formatPhone(phone));
	}
});


/**  *  Format phone numbers */ 
function formatPhone(phonenum) {
	if(contains(phonenum, "Ext")) {
		var phNoArray = new Array();
		phNoArray = phonenum.split("Ext");
		phonenum = $.trim(phNoArray[0]);
	}
	var regexObj = /^(?:\+?1[-. ]?)?(?:\(?([0-9]{3})\)?[-. ]?)?([0-9]{3})[-. ]?([0-9]{4})$/;    
	if (regexObj.test(phonenum)) {
         var parts = phonenum.match(regexObj);
         var phone = "";
         if (parts[1]) { phone += "(" + parts[1] + ") "; }
        phone += parts[2] + "-" + parts[3];
        return phone;
	}else{
        //invalid phone number
         return phonenum;
	}
}

/** contains for phone numbers **/
function contains(str, text) {
	return str.indexOf(text) >= 0;
}
	
/** update Customer **/
function updateCustomer(){
	if(!$('#customerFromID').validationEngine('validate')) {
		return false;
	}
	var customerName = $('#customerNameHeader').val();
	var customerId = $('#CustomerId').text();
	$.ajax({
		url: "./customerList/updateCustomerName",
		mType: "GET",
		data : { 'customer' : customerName, 'customerId' :customerId },
		success: function(data) {
			var checkpermission=getGrantpermissionprivilage('Customers',0);
    		if(checkpermission){
			document.location.href = "./customerdetails?rolodexNumber="+customerId+"&name="+customerName+"";
    		}
		}
	});
}
	
/** Architect tab hide and show **/
function architect() {
	if ($('#architectche').is(':checked')) {
		$("#architect").show();
	} else {
		$("#architect").hide();
	}
}

/** Engineer tab hide and show **/
function engineer() {
	if ($('#engineerche').is(':checked')) {
		$("#engineer").show();
	} else {
		$("#engineer").hide();
	}
}

/** Vendor tab hide and show **/
function vendor() {
	if ($('#vendorcheck').is(':checked')) {
		$("#vendor").show();
	} else {
		$("#vendor").hide();
	}
}

/** Customer tab hide and show **/
function customer() {
	if ($('#customerchek').is(':checked')) {
		$("#customer").show();
		jQuery("#EmployeeDetailsGrid").setGridHeight(200);
		$("#customerQuickQuote").show();
		$("#customerChecked").show();
	} else {
		$("#customer").hide();
		jQuery("#EmployeeDetailsGrid").setGridHeight(400);
		$("#customerChecked").css("display", "none");
	}
}
	
/** Employee tab hide and show **/
function employee() {
	if ($('#employeeche').is(':checked')) {
		$("#employee").show();
	} else {
		$("#employee").hide();
	}
}

/** Format Currency **/
function formatCurrency(strValue){
	if(strValue == "" || strValue == null){
		return "$0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10)
		strCents = "0" + strCents;
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++)
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
}

/** Customer Format Date **/
function customFormatDate(date) {
	/*2003-02-18 00:00:00.0 ----------- YYYY-mm-dd*/
	if(date === ""){
		return "";
	}
	var arr1 = date.split(" ");
	var arr2 = arr1[0].split("-");
	var newDate = arr2[1] + "/" + arr2[2] + "/" + arr2[0];
	return newDate;
}

/** Search Job Auto-Complete **/
$(function() { var cache = {}; var lastXhr='';
$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
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
		}
	});
},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/searchCustomerList", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });