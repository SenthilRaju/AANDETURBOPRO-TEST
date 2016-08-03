<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - ArchitectDetails</title>
		<link type="text/css" href="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/themes/custom-theme/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
		<link type="text/css" href="./../resources/styles/turbo-css/turbo.css" rel="stylesheet" />
		<link type="text/css" href="./../resources/web-plugins/Validation_Engine/validationEngine.jquery.css" rel="stylesheet" media="screen">
		<link rel="SHORTCUT ICON" href="./../resources/Icons/TurboRepIcon.png">
		<style type="text/css">
			input#customerNameHeader {width:250px;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;border:1px solid #D3D3D3;padding:3px;}
			input#customerNameHeader:focus{border:1px solid #637C92;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;}
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{ background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			#architectPhoneName a{padding:0px 0px 0px 5px; color:#FFF;}
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
		</style>
	</head>
	<body>
		<div  style="background-color: #FAFAFA">
		<div>
			 <jsp:include page="./headermenu.jsp"></jsp:include> 
		</div>
<div id="general_journal">
	<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA">
		<ul>
			<li><a href="architectContacts">Contacts</a></li>
			<li><a href="rolodex/rolodexjournal">Journal</a></li>
			<li><a href="rxdetailedviewtabs/architect">Architect</a></li>
			<li id="engineer"><a href="rxdetailedviewtabs/engineer">Engineer</a></li>
			<li id="customer"><a href="rxdetailedviewtabs/rolodexCustomer">Customer</a></li>
			<li id="vendor"><a href="#vendortab">Vendor</a></li>
			<li id="employee"><a href="employe">Employee</a></li>
			<li style="float: right; padding-right: 10px; paddind-top:3px;">
				<label id="customerName" style="color: white; vertical-align: middle;">${requestScope.name}</label>
				<label id="phoneCenter" style="color: white;vertical-align: middle;"></label><label id="phoneName" style="color: white;vertical-align: middle;"></label><label id="phone_secondField" style="color: white;vertical-align: middle;"></label>
				<label id="architectId" style="display: none;">${requestScope.rolodexNumber}</label>
			</li>
		</ul>
		<jsp:include page="vendor.jsp"></jsp:include>
	</div>
</div>
<div style="padding-top: 26px; background-color: #FAFAFA">
	<table id="footer">
	<tr>
		<td colspan="2">
			<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
		</td>
	</tr>
	</table>
</div>
</div>
<script type="text/javascript">
	jQuery(document).ready(function() {
		var archTech = "${requestScope.rxMasterDetails.isCategory1}";
		var Engg = "${requestScope.rxMasterDetails.isCategory2}";
		//var generalContract = "${requestScope.rxMasterDetails.isCategory3}";
		var customer = "${requestScope.rxMasterDetails.isCustomer}"; 
		var vendor = "${requestScope.rxMasterDetails.isVendor}";
	    var Emp = "${requestScope.rxMasterDetails.isEmployee}";
		if(Engg !== 'true'){
			$("#engineer").css("display", "none");
		}
		if(customer !== 'true'){
			$("#customer").css("display", "none");
		}
		if(vendor !== 'true'){
			$("#vendor").css("display", "none");
		}
		if(Emp !== 'true'){ 
			$("#employee").css("display", "none");
		}
		if(archTech !== 'true'){
			$("#architect").css("display", "none");
		}
		$(".tabs_main").tabs({
			cache: true,
			ajaxOptions: {
				data: {rolodexID: $("#architectId").text() },
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
					$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA;'><img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
				}
			}
		});
		$(".datepicker").datepicker();
		var name = "${requestScope.name}";
		var sting = name.replace('`', ' ');
		var sting1 = sting.replace('`', ' ');
		var archName = sting1.replace('and', '&');
		var archName1 = archName.replace('and', '&');
		var phone = "${requestScope.phone1}";
		$('#customerName').empty();	
		$('#customerName').append(archName1);
		if(phone !== ''){
			var phoneSpilt = phone.split("-");
			$('#phoneCenter').empty();
			$('#phoneCenter').append(" | ");
			$('#phoneName').empty();
			$('#phoneName').append(phoneSpilt[0]);
			$('#phone_secondField').empty();
			$('#phone_secondField').append("-"+phoneSpilt[1]);
		}
	});

	function architect() {
		if ($('#architectche').is(':checked')) {
			$("#architect").show();
		} else {
			$("#architect").hide();
		}
	}

	function engineer() {
		if ($('#engineerche').is(':checked')) {
			$("#engineer").show();
		} else {
			$("#engineer").hide();
		}
	}

	function vendor() {
		if ($('#vendorcheck').is(':checked')) {
			$("#vendor").show();
		} else {
			$("#vendor").hide();
		}
	}

	function employee() {
		if ($('#employeeche').is(':checked')) {
			$("#employee").show();
		} else {
			$("#employee").hide();
		}
	}

	function customer() {
		if ($('#customerchek').is(':checked')) {
			$("#customer").show();
			jQuery("#VendorDetailsGrid").setGridHeight(200);
			$("#customerQuickQuote").show();
			$("#customerChecked").show();
		} else {
			$("#customer").hide();
			jQuery("#VendorDetailsGrid").setGridHeight(400);
			$("#customerChecked").css("display", "none");
		}
	}
	
	function formatCurrency(strValue)
	{
		if(strValue === "" || strValue === null){
			return "$0.00";
		}
		strValue = strValue.toString().replace(/\$|\,/g,'');
		dblValue = parseFloat(strValue);

		blnSign = (dblValue === (dblValue = Math.abs(dblValue)));
		dblValue = Math.floor(dblValue*100+0.50000000001);
		intCents = dblValue%100;
		strCents = intCents.toString();
		dblValue = Math.floor(dblValue/100).toString();
		if(intCents<10){
			strCents = "0" + strCents;}
		for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
			dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
			dblValue.substring(dblValue.length-(4*i+3));}
		return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
	}

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

	$(function() { var cache = {}, lastXhr;
	$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
		select: function (event, ui) {
			var name = ui.item.value;
			$.ajax({
				url: "./search/searchrolodex",
				mType: "GET",
				data : {'rolodex': name},
				success: function(data){
					 var entityValue="";
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
			lastXhr = $.getJSON( "search/searchArchitectList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

	function updateArchitect()
	{
		if(!$('#architectFormID').validationEngine('validate')) {
			return false;
		}
		var customerName = $('#customerNameHeader').val();
		var customerId = $('#architectId').text();
		$.ajax({
			url: "./customerList/updateCustomerName",
			mType: "GET",
			data : { 'customer' : customerName, 'customerId' :customerId },
			success: function(data) {
				document.location.href = "./customerdetails?rolodexNumber="+customerId+"&name="+customerName+"";
				//window.location.reload();
			}
		});
	}
</script>
</body>
</html>