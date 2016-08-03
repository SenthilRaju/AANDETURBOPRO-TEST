<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro - Rolodex Details</title>
		<style type="text/css">
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{ background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
		</style>
	</head>
	<body>
	<div style="background-color: #FAFAFA">
		<div>
			 <jsp:include page="./../headermenu.jsp"></jsp:include> 
		</div>
		<div id="">
			<div id="rolodexdetailstab" class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA">
				<ul>
					<li><a href="rolodexdetails/rolodex_contacts">Contacts</a></li>
					<li><a href="rolodex/rolodexjournal">Journal</a></li>
					<li id="customer"><a href="rxdetailedviewtabs/rolodexCustomer">Financial</a></li>
					<li id="vendor"><a href="rolodexforms/vendorform" >Vendor</a></li>
					<li id="employee"><a href="employe">Employee</a></li>
					<li id="engineer"><a href="rxdetailedviewtabs/engineer"><c:out value='${requestScope.Category1Desc}'/><!-- Engineer --></a></li>
					<li id="architect"><a href="rxdetailedviewtabs/architect"><c:out value='${requestScope.Category2Desc}'/><!-- Architect --></a></li>
					<li style="float: right; padding-right: 10px; padding-top:3px;">
					<label id="customerName" style="color: white;vertical-align: middle;">${requestScope.name}</label>
					<label id="phoneCenter" style="color: white;vertical-align: middle;"></label><label id="phoneName" style="color: white;vertical-align: middle;"></label><label id="phone_secondField" style="color: white;vertical-align: middle;"></label>
					<label id="architectId" style="display: none;"></label>
				</ul>
			</div>
		</div>
		<div style="padding-top: 26px; background-color: #FAFAFA;">
			<table id="footer">
			<tr>
				<td colspan="2">
					<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
				</td>
			</tr>
			</table>
		</div>
	<div style="display: none;">
	<input type="text" id="lableName" value="${requestScope.name}">
	<input type="text" id="labelPhone" value="${requestScope.phone}">
	<input type="text" id="Category1Descset" value="${requestScope.Category1Desc}">
	<input type="text" id="Category2Descset" value="${requestScope.Category2Desc}">
	<input type="text" id="Category3Descset" value="${requestScope.Category3Desc}">
	<input type="text" id="Category4Descset" value="${requestScope.Category4Desc}">
	<input type="text" id="Category5Descset" value="${requestScope.Category5Desc}">
	<input type="text" id="Category6Descset" value="${requestScope.Category6Desc}">
	<input type="text" id="Category7Descset" value="${requestScope.Category7Desc}">
	<input type="text" id="Category8Descset" value="${requestScope.Category8Desc}">
	
	<input type="text" id="CustomerCategory1hidden" name="CustomerCategory1hidden" value="${requestScope.CustomerCategory1}">
	<input type="text" id="CustomerCategory2hidden" name="CustomerCategory2hidden" value="${requestScope.CustomerCategory2}">
	<input type="text" id="CustomerCategory3hidden" name="CustomerCategory3hidden" value="${requestScope.CustomerCategory3}">
	<input type="text" id="CustomerCategory4hidden" name="CustomerCategory4hidden" value="${requestScope.CustomerCategory4}">
	<input type="text" id="CustomerCategory5hidden" name="CustomerCategory5hidden" value="${requestScope.CustomerCategory5}">
	</div>
	</div>
	
	
		<script type="text/javascript">
			jQuery(document).ready(function() {

				var rxMasterID = getUrlVars()["rolodexNumber"];
				$("#architectId").val(rxMasterID);
				var Engg = "${requestScope.rxMasterDetails.isCategory1}";
				var archTech = "${requestScope.rxMasterDetails.isCategory2}";
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
						data: {rolodexID: rxMasterID },
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
				}});
				$("#tabs_sub").tabs();
				$(".datepicker").datepicker();
				var name = "${requestScope.name}";
				var sting = name.replace('(', ' ');
				var sting1 = sting.replace(')', ' ');
				//var rolodexName = sting1.replace('and', '&');
				var rolodexName = sting1;
			    var rolodexName1 = rolodexName;
				var phone = "${requestScope.phone}";
				$('#customerName').empty();	 
				$('#customerName').append(rolodexName1);
				if(phone !== ''){
					var phoneSpilt = phone.split("-");
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
			                }  else {
			                            //invalid phone number
			                             return phonenum;
			                }
			}

			function contains(str, text) {
				return str.indexOf(text) >= 0;
			}
			
			function vendor() {
				if ($("#vendorcheck").is(':checked')) {
					$("#vendor").show();
				} else {
					$("#vendor").hide();
				}
			}
			
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
	
			function employee() {
				if ($('#employeeche').is(':checked')) {
					$("#employee").show();
				} else {
					$("#employee").hide();
				}
			}

			$(function() { var cache = {}; var lastXhr='';
			$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
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
							}if(entity == "VEND") {
								searchlist = entity.replace("VEND","vendordetails");
								checkpermission=getGrantpermissionprivilage('Vendors',0);
							}if(entity == "CUST") {
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
				} }); });

			function formatCurrency(strValue)
			{
				if(strValue === "" || strValue === null){
					return "$0.00";
				}
				strValue = strValue.toString().replace(/\$|\,/g,'');
				dblValue = parseFloat(strValue);
		
				blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
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
		</script>
	</body>
</html>