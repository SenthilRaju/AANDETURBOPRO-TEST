<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
  <div id=addnewcustab>
	<form action="" id="addNewcutomerForm">
		<div id="soreleasegeneral">
			<table>
				<tr><td>
					<fieldset class= " ui-widget-content ui-corner-all" style="width:350px">
						<legend><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
									<td style=" width : 50px;"><label>Name:</label></td>
									<td><input type="text" name="name" id="customerName" style="width:270px" class="validate[required] validate[maxSize[40]]"></td>
								</tr>
							</table>
					</fieldset>
					</td>
					<td style="vertical-align:top">
						<fieldset class= " ui-widget-content ui-corner-all" style="width:100px;">
							<legend><label><b>Categories</b></label></legend>
								<table>
									<tr align="center">
										<td style="width : 50px"><label>Customer</label></td>
										<td> <input type="checkbox" id="customer" value="1" checked="checked" disabled="disabled" style="vertical-align: middle;"></td>
									</tr>
								</table>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
		<div id="soreleaselineitem">
			<table>
				<tr>
					<td colspan="2"><hr> 
					</td>
				</tr>
				<td style="vertical-align:top">
					<fieldset style="height:130px; width:350px" class= "ui-widget-content ui-corner-all">
	 					<legend><label><b>Address</b></label></legend>
	 					<table >
		 					<tr>
		 						<td style="width: 50px"><label>Address: </label></td>
		 						<td><input type="text" id="address1" name="address1" class="validate[maxSize[100]" style="width:261px;" value=""></td>
		 					</tr>
		 					<tr>
		 						<td style="width: 50px"><label></label></td>
								<td><input type="text" id="address2" name="address2" class="validate[maxSize[40]" style="width: 261px;" value=""></td>
							</tr>
							<tr>
								<td style="width: 50px">
									<label>City:</label></td>
									<td><input type="text" style="width: 160px;" id="cityNameListID" placeholder="Minimum 2 characters required" name="cityNameListName" value="">
									<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
									<label>State:</label>
									<input type="text" style="width: 25px;text-transform: uppercase" id="stateCode" name="state" value="" maxlength="2">
								</td>
							</tr>
							<tr>
								<td style="width: 50px">
									<label>Zip: </label>
								</td>
								<td>
									<input type="text" style="width: 100px;" id="pinCode" class="validate[custom[number]]" name="pinCode" value="">
								</td>
							</tr>
						</table>
				 	</fieldset>
				</td>
				<td style="vertical-align:top">
					<fieldset style="width:350px; height: 130px;" class= " ui-widget-content ui-corner-all">
						<legend><label><b>Phone</b></label></legend>
						<table id="phoneField">
						<tr>
							<td style=" width : 50px;">
								<label>Phone1:</label>
							</td>
							<td>
								<input type="text" id="areaCode" name="contact1" style="width:50px" maxlength="3" value="">&nbsp;
								<input type="text" id="exchangeCode" name="contact1" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
								<input type="text" id="subscriberNumber" name="contact1" style="width:100px" value="">
							</td>
						</tr>
						<tr>
							<td style=" width : 50px;">
								<label>Phone2:</label>
							</td>
							<td>
								<input type="text" id="areaCode1" name="contact2" style="width:50px" maxlength="3" value="">&nbsp;
								<input type="text" id="exchangeCode1" name="contact2" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
								<input type="text" id="subscriberNumber1" name="contact2" style="width:100px" value="">
							</td>
						</tr>
						<tr>
							<td style=" width : 50px;">
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
					<input type="button" class="savehoverbutton turbo-blue" value="Save & Close" onclick="savenewcustomer()"style=" width:120px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelAddCustomer()" style="width:80px;">  
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
		$("#addnewcustab").tabs();
	});

	jQuery( function(){
		jQuery("#addnewcustab").dialog({
			autoOpen:false,
			height:335,
			width:800,
			title:"Add New Customer",
			modal:true,
		/*	buttons:{
				"submit":function() { },
				cancel:function() {
					jQuery(this).dialog("close");
					return true;
				} 
			}, */
			close:function()
			{
				$('#addNewcutomerForm').validationEngine('hideAll');
				return true;
			}
		});
	});

	 $(function() { var cache = {}, lastXhr; $( "#cityNameListID" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
			var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#stateCode").val(stateCode);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
/*	$(function() {
		var availableTags = [
			"Alabama (AL)", "Alaska (AK)", "Arizona (AZ)", "Arkansas (AR)", "California (CA)", "Colorado (CO)", "Connecticut (CT)", "Delaware (DE)", 
			"Florida (FL)", "Georgia (GA)", "Hawaii (HI)", "Idaho (ID)", "Illinois (IL)", "Indiana (IN)",  "Iowa (IA)", "Kansas (KS)", "Kentucky (KY)",  
			"Louisiana (LA)", "Maine (ME)", "Maryland (MD)", "Massachusetts (MA)", "Michigan (MI)",  "Minnesota (MN)", "Mississippi (MS)", 
			"Missouri (MO)", "Montana (MT)", "Nebraska (NE)", "Nevada (NV)",  "New Hampshire (NH)", "New Jersey (NJ)", "New Mexico (NM)",
			"New York (NY)", "North Carolina (NC)", "North Dakota (ND)", "Ohio (OH)", "Oklahoma (OK)", "Oregon (OR)", "Pennsylvania (PA)", 
			"Rhode Island (RI)", "South Carolina (SC)", "South Dakota (SD)", "Tennessee (TN)", "Texas (TX)", "Utah (UT)", "Vermont (VT)", 
			"Virginia (VA)", "Washington (WA)", "West Virginia (WV)", "Wisconsin (WI)", "Wyoming (WY)", "American Samoa (AS)", "District of Columbia (DC)", 
			"Guam (GU)", "Northern Mariana Islands (MP)", "Puerto Rico (PR)", "Virgin Islands (VI)"
		];
		$( "#state" ).autocomplete({ minLength: 2, source: availableTags,
			select: function (event, ui) {
			  var stateSelect =	ui.item.value;
			  var stateSplit = stateSelect.split(" (");
			  var stateName = stateSplit[1];
			  var stateCode = stateName.replace(")", "");
			  $("#stateCode").val(stateCode);
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} 
		});
	}); */
	
	function cancelAddCustomer(){
		$('#addNewcutomerForm').validationEngine('hideAll');
		$("#addnewcustab").dialog("close");
		return true;
	}
	
	function savenewcustomer() {
		if(!$('#addNewcutomerForm').validationEngine('validate')) {
			return false;
		}
		var name = $("#customerName").val();
		var customer = $("#customer").val();
		var address1 = $("#address1").val();
		var address2 = $("#address2").val();
		var city=$("#cityNameListID").val();
		var country = $("#stateCode").val();
		var pinCode = $("#pinCode").val();
		var areaCode=$("#areaCode").val();
		var exchangeCode = $("#exchangeCode").val();
		var subscriberNumber = $("#subscriberNumber").val();
		var contact1;
		if(areaCode !== ""){
		contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
		}
		var areaCode1=$("#areaCode1").val();
		var exchangeCode1 = $("#exchangeCode1").val();
		var subscriberNumber1 = $("#subscriberNumber1").val();
		var contact2;
		if(areaCode1 !== ""){
			contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
			}
		var areaCode2=$("#areaCode2").val();
		var exchangeCode2 = $("#exchangeCode2").val();
		var subscriberNumber2 = $("#subscriberNumber2").val();
		var fax;
		if(areaCode2 !== ""){
			fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
			}
		$.ajax({
			url: "./customerList/addNewCustomer",
			mType: "GET",
			data : { 'name' : name, 'customer' : customer, 'address1' : address1, 'address2' : address2, 'state' : country, 'zip' : pinCode, 
						'contact1' : contact1, 'contact2' : contact2, 'fax' : fax, 'cityNameListName' : city},
			success: function(data) {
				var rxMasterId=data.rxMasterId;
				document.location.href = "./customerdetails?rolodexNumber="+rxMasterId+"&name="+name+"";
			}
	   });
		$(".requiredSave").show();
		return true;
	}
 
</script>
