<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - UserDetails</title>
		<style type="text/css">
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
		</style>
	</head>
	<body>
	<div  style="background-color: #FAFAFA">
	<div>
		 <jsp:include page="./headermenu.jsp"></jsp:include> 
	</div>
	<div id="userDetailsTab">
		<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA;height: 950px;">
			<ul>
				<li id="userDetailsDiv"><a href="userDetailsMainFrom">User Details</a></li>
				<li style="float: right; padding-right: 10px; padding-top:3px;">
					<label id="userName" style="color: white;vertical-align: middle;"></label>
				</li>
			</ul>
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
	<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			$(".datepicker").datepicker();
			var aUserloginID = getUrlVars()["userLoginId"];
			var aLoginName = getUrlVars()["loginName"];
			$("#userName").text(aLoginName);
			$(".tabs_main").tabs({
				cache: true,
				ajaxOptions: {
					data: { userLoginId  : aUserloginID },
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
						$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"+
								"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
					}
				}
			});
		});
	
		function formatCurrency(strValue)
		{
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

		/** User Search List **/
		$(function() { var cache = {}; var lastXhr='';
		$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
			select: function (event, ui) {
				var aUserLoginID = "${requestScope.userSysAdmin}";
				if(aUserLoginID !== '1'){
	    			var aInfo = true;
	    			if(aInfo){
	    				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
	    				var newDialogDiv = jQuery(document.createElement('div'));
	    				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
	    				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
	    										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	    				return true;
	    			}
		    	}else{
					var userID = ui.item.id;
					var name = ui.item.value;
					var splitName = name.split("[");
					var loginName = splitName[0];
					document.location.href = "./userDetails?userLoginId="+userID+"&loginName="+loginName;
				}
			},
			open: function(){ 
				$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./userlist" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New User</a></b></div>');
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				 },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "search/searchUserList", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	</script>
</body>
</html>