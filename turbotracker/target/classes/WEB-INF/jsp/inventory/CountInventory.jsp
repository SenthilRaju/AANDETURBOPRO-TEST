<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TurboPro - Counting Inventory</title>
<style type="text/css">
	#search {display: none;}
	input#searchInventory {
width: 400px;
border-top-left-radius: 5px;
border-top-right-radius: 5px;
border-bottom-right-radius: 5px;
border-bottom-left-radius: 5px;
outline: 0px;
border: 1px solid rgb(211, 211, 211);
border-image-source: initial;
border-image-slice: initial;
border-image-width: initial;
border-image-outset: initial;
border-image-repeat: initial;
padding: 3px;
}

#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
#mainmenuInventoryPage a{background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
#mainmenuInventoryPage ul li a{background: none; }
</style>
<style>
.loadingDivInventoryCount{
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background: url('../resources/scripts/jquery-autocomplete/send_mail_waiting.gif') 50% 50% no-repeat;
}

</style>
</head>
<body>
<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<div align="center" id="marginId" style="margin-bottom: 26px;">
			<form id="writeChecksFromID" action="">
				<table style="width:650px;" >
						<tr>
							<td align="center" style="" colspan="3">
								<h2  style="font-family: Verdana,Arial,sans-serif;"><label>Counting Inventory</label></h2>
							</td>
						</tr>
						<tr>
							<td>
								<label style="font-family: Verdana,Arial,sans-serif;"><b>Warehouse:</b> </label>
							</td>
							<td>
								<select id="warehouseListID" name="warehouseList" onchange="getCountInventoryGrid()" style="width: 405px; height: 25px">
									<option value="0"> -Select- </option>														
								</select>		
							</td>
						</tr>
						<tr>
							<td>
								<label style="font-family: Verdana,Arial,sans-serif;"><b>Sort:</b> </label>
							</td>
							<td>
								<select id="sortId" name="sortList" onchange="getCountInventoryGrid()" style="width: 405px; height: 25px">
									<option value="1"> Product Code </option>
									<option value="2"> Product Description </option>
									<option value="3"> Category </option>														
									<option value="4"> Primary Vendor </option>
								</select>		
							</td>
						</tr>
						<tr>
							<td>
								<label style="font-family: Verdana,Arial,sans-serif;"><b>Search:</b></label> &nbsp;
							</td>
							<td>
								<input type="text" id="searchInventory" placeholder="Minimum 3 characters required to get List"> &nbsp;
							</td>
						</tr>
				</table>
				<div>
				<table>
						<tr>
							<td>
							<div id="jqGrid">
								<table id="CountInventoryGrid"></table>
								<div id="CountInventoryGridPager"></div>
							</div>
							</td>
						</tr>
			<tr>
			<td><div id="invTypeMsg" style="display:none"></div></td>
			</tr>
			<tr>
			<td>
			<span style="float:left;">
			<div id="pdfCountInventoryButton" style="display: none;">
				<input type="button" id="getPdfButton" name="saveUserButtonName" value="pdf" class="savehoverbutton turbo-blue" style="width: 35px;" onclick="getCSV('pdf');" >
				<span id="accountsPayableImgID"><img src='./../resources/images/csv_text.png' title='Export CSV' align='middle' style='vertical-align: middle; width: 24px; height: 24px; cursor: pointer;' onclick="getCSV('csv');"/> </span>
			</div>
			</span>
			<span style="float:right;">
			<div id="saveCountInventoryButton" style="display: none;">
				<input type="button" id="saveCountInventoryButtonID" value="Save" class="turbo-blue savehoverbutton" onclick="SaveOrderPoints()" style="margin-left: 10px; font-size: 15px;" />
				<!-- <input type="button" value="Save & Close" class="turbo-blue savehoverbutton" onclick="SaveCloseOrderPoints()" style="margin-left: 10px;font-size: 15px;"/> -->
			</div>
			</span>
			</td>
			</tr>
				</table>
				</div>
			</form>
		 <div class="loadingDivInventoryCount" id="loadingDivForInventoryCount" style="display: none;opacity: 0.8;background-color: #fff;z-index: 1234;text-align: center;"> 
			</div>
			<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
			
			
			
</div>

<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inventoryCount.js"></script>
<script type="text/javascript">
jQuery(document).ready(function(){
	
	$(function() { var cache = {}; var lastXhr='';
	$( "#searchInventory" ).autocomplete({ minLength: 3,timeout :1000,
		select: function (event, ui) {
		var aValue = ui.item.value;
		var valuesArray = new Array();
		valuesArray = aValue.split("|");
		$("#inventorysearch").val(valuesArray[1]);
		var id = valuesArray[0];
		var code = valuesArray[2];
		location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
	},
	open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "search/searchinventory", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

});
</script>
</body>
</html>