<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
  <div id= "addNewVendor">
	<form action="" id="addNewVendorForm">
		<div id="vendorGeneral">
			<table>
				<tr><td>
					<fieldset class= " ui-widget-content ui-corner-all" style="width:350px">
						<legend><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
								<td style=" width : 50px;"><label>Name:</label></td>
									<td><input type="text" name="VendorName" id="VendorID" style="width:270px" class="validate[required] validate[maxSize[40]]"></td></tr>
							</table>
					</fieldset>
				</td>
				<td style="vertical-align:top">
					<fieldset class= " ui-widget-content ui-corner-all" style="width:100px;">
						<legend><label><b>Categories</b></label></legend>
							<table>
								<tr align="center"><td style="width : 50px"><label>Vendor</label></td>
									<td><input type="checkbox" id="vendorCheckID" name="vendorCheckName" value="1" checked="checked" disabled="disabled" style="vertical-align: middle;">
									</td>
							</table>
					</fieldset>
					</td></tr>
			</table>
		</div>
		<div id="vendorLineitem">
			<table>
				<tr><td colspan="2"><hr></td></tr>
				<tr><td style="vertical-align:top">
					 <fieldset style="width:350px; height:130px" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Address</b></label></legend>
						<table>
						<tr>
							<td style="width: 50px">
								<label>Address:</label>
							</td>
							<td>
								<input type="text" style="width: 261px;" id="address1ID" name="address1Name" value="">
							</td>
						</tr>
						<tr>
							<td style="width: 50px">
								<label></label>
							</td>
							<td>
								<input type="text" style="width: 261px;" id="address2ID" name="address2Name" value="">
							</td>
						</tr>
						<tr>
							<td style="width: 50px">
								<label>City:</label>
							</td>
							<td>
								<input type="text" style="width: 160px;" id="cityNameListID" placeholder="Minimum 2 characters required" name="cityNameListName" value="">
								<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
								<label>State:</label>
								<input type="text" style="width: 25px;text-transform: uppercase" id="stateCodeID" name="stateCodeName" value="" maxlength="2">
							</td>
						</tr>
						<tr>
							<td style="width: 50px">
								<label>Zip: </label>
							</td>
							<td>
								<input type="text" style="width: 100px;" id="pinCodeID" class="validate[custom[number]]" name="pinCodeName" value="">
							</td>
						</tr>
						</table>
			 		</fieldset>
				</td>
				<td style="vertical-align:top">
					<fieldset style="width:350px; height: 130px;" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Phone</b></label></legend>
						<table>
							<tr>
								<td style="width: 50px">
									<label>Phone1:</label>
								</td>
								<td>
									<input type="text" id="areaCode" name="contact1" style="width:50px" maxlength="3" value="">&nbsp;
									<input type="text" id="exchangeCode" name="contact1" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
									<input type="text" id="subscriberNumber" name="contact1" style="width:100px" value="">
								</td>
							</tr>
							<tr>
								<td style="width: 50px">
									<label>Phone2:</label>
								</td>
								<td>
									<input type="text" id="areaCode1" name="contact2" style="width:50px" maxlength="3" value="">&nbsp;
									<input type="text" id="exchangeCode1" name="contact2" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
									<input type="text" id="subscriberNumber1" name="contact2" style="width:100px" value="">
								</td>
							</tr>
							<tr>
								<td style="width: 50px">
									<label>Fax:</label>
								</td>
								<td>
									<input type="text" id="areaCode2" name="contact3" style="width:50px" maxlength="3" value="">&nbsp;
									<input type="text" id="exchangeCode2" name="contact3" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
									<input type="text" id="subscriberNumber2" name="contact3" style="width:100px" value="">
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</table>
		</div>
		<br>
		<table align="center" style="width:745px">
		 	<tr>
			 	<td align="right" style="padding-right:1px;">
					<input type="button" class="savehoverbutton turbo-blue" value="Save & Close" onclick="saveNewVendor()" style=" width:120px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelVendor()" style="width:80px;">  
				</td>
			</tr>
		</table>
	</form>
</div>
<style type="text/css">
	.ui-autocomplete {cursor: default;height: 150px;overflow-y: scroll;overflow: none;}
	.ui-menu-item a:hover { background-color: #637C92; color: #DCEDF9; border-color: #637C92 }
</style>
<script type="text/javascript">
		jQuery(document).ready(function(){
			$("#addNewVendor").tabs();
		});
		jQuery( function(){
			jQuery("#addNewVendor").dialog({
				autoOpen:false,
				height:335,
				width:800,
				title:"Add New Vendor",
				modal:true,
				close:function(){ $('#addNewVendorForm').validationEngine('hideAll');
				return true; }
			});
		});

		 $(function() { var cache = {}, lastXhr; $( "#cityNameListID" ).autocomplete({ minLength: 2,timeout :1000,
				select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
				var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#stateCodeID").val(stateCode);},
				source: function( request, response ) { var term = request.term;
					if ( term in cache ) { response( cache[ term ] ); 	return; 	}
					lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
						if ( xhr === lastXhr ) { response( data ); 	} });
				},
				error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
	
	function cancelVendor(){
		$('#addNewVendorForm').validationEngine('hideAll');
		$("#addNewVendor").dialog("close");
		return true;
	}
	
	function saveNewVendor() {
		if(!$('#addNewVendorForm').validationEngine('validate')) {
			return false;
		}
		var areaCode=$("#areaCode").val();
		var exchangeCode = $("#exchangeCode").val();
		var subscriberNumber = $("#subscriberNumber").val();
		var contact1='';
		if(areaCode !== ''){
		contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
		}
		var areaCode1=$("#areaCode1").val();
		var exchangeCode1 = $("#exchangeCode1").val();
		var subscriberNumber1 = $("#subscriberNumber1").val();
		var contact2='';
		if(areaCode1 !== ''){
			contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
		}
		var areaCode2=$("#areaCode2").val();
		var exchangeCode2 = $("#exchangeCode2").val();
		var subscriberNumber2 = $("#subscriberNumber2").val();
		var fax='';
		if(areaCode2 !== ''){
			fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
		}
		var newVendorValues = $("#addNewVendorForm").serialize();
		$.ajax({
			
			url: "./vendorscontroller/addNewVendorList",
			mType: "GET",
			data : newVendorValues+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax="+ fax,
			success: function(data) {
				var rxMasterId=data.rxMasterId;
				var searchText = $("#VendorID").val();
				var search = searchText.replace('&','and');
				var vendorName= search.replace('&','and');
				document.location.href = "./vendordetails?rolodexNumber="+rxMasterId+"&name="+vendorName+"";
			}
	   });
		$(".requiredSave").show();
		return true;
	}
 
</script>
