<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
  <div id= "addNewRolodex">
	<form action="" id="addNewRolodexForm">
		<div id="vendorGeneral">
			<table>
				<tr><td>
					<fieldset class= " ui-widget-content ui-corner-all" style="width:350px">
						<legend><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
									<td style=" width : 50px;"><label>Name:</label></td>
									<td>
										<input type="text" name="rolodexName" id="rolodexID" style="width:270px" class="validate[required] validate[maxSize[40]]" onfocus="validation_message()">
									</td>
								</tr>
							</table>
					</fieldset>
				</td>
				<td style="vertical-align:top">
					<fieldset class= " ui-widget-content ui-corner-all" style="width:360px;">
						<legend><label><b>Categories</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr align="center">
									<td><label>Customer</label>
										<input type="checkbox" id="customerCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
									<td><label>Vendor</label>
										<input type="checkbox" id="vendorCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
									<td><label>Employee</label>
										<input type="checkbox" id="employeeCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
									<td><label>Architect</label>
										<input type="checkbox" id="architectCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
									<td><label>Engineer</label>
										<input type="checkbox" id="engineerCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
									<td><label>G.C</label>
										<input type="checkbox" id="gcCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
								</tr>
							</table>
					</fieldset>
					</td></tr>
			</table>
		</div>
		<div id="rolodexLineitem">
			<table>
			<tr><td colspan="2"><hr ></td></tr>
			<tr><td style="vertical-align:top">
			  <fieldset style="width:350px; height:130px" class= " ui-widget-content ui-corner-all">
				<legend><label><b>Address</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
						<label>Address:</label></td>
					<td>
						<input type="text" style="width: 261px;" id="address1ID" name="address1Name" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;"></td>
					<td>
						<input type="text" style="width: 261px;" id="address2ID" name="address2Name" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>City:</label>
						</td>
						<td>
							<input type="text" style="width: 160px;" id="cityNameListID" placeholder="Minimum 2 characters required" name="cityNameListName" value="">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
						<label>State:</label>
							<input type="text" style="width: 25px;;text-transform: uppercase" id="stateCodeID" name="stateCodeName" value="" maxlength="2">
						</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
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
				<fieldset style="width:360px; height: 130px;" class= " ui-widget-content ui-corner-all">
				<legend><label><b>Phone</b></label></legend>
				<table>
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
			 	<td></td>
			 	<td align="right" style="padding-right:1px;">
					<input type="button" class="savehoverbutton turbo-blue" value="Save & Close" onclick="saveNewRolodex()" style=" width:120px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelRolodex()" style="width:80px;">  
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
			$("#addNewRolodex").tabs();
		});
		jQuery( function(){
			jQuery("#addNewRolodex").dialog({
				autoOpen:false,
				height:350,
				width:820,
				title:"Add New Rolodex",
				modal:true,
				close:function(){ $('#addNewRolodexForm').validationEngine('hideAll');
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
	
	function cancelRolodex(){
		$('#addNewRolodexForm').validationEngine('hideAll');
		$("#addNewRolodex").dialog("close");
		return true;
	}
	
	function saveNewRolodex() {
		if(!$('#addNewRolodexForm').validationEngine('validate')) {
			return false;
		}
		var customerCheck = document.getElementById('customerCheckID').checked;
		var vendorCheck = document.getElementById('vendorCheckID').checked;
		var employeeCheck = document.getElementById('employeeCheckID').checked; 
		var architectCheck=document.getElementById('architectCheckID').checked;
		var engineerCheck=document.getElementById('engineerCheckID').checked;
		var gcCheck=document.getElementById('gcCheckID').checked;
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
		var newRolodexValues = $("#addNewRolodexForm").serialize();
		$.ajax({
			url: "rolodex/addNewRolodexList",
			mType: "GET",
			data : newRolodexValues+"&customerValue="+customerCheck+"&vendorValue="+vendorCheck+"&employeeValue="+employeeCheck+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 + "&fax=" +fax+"&architectValue="+architectCheck+"&engineerValue="+engineerCheck+"&gcValue="+gcCheck,
			success: function(data) {
				var rxMasterId=data.rxMasterId;
				var searchText = $("#rolodexID").val();
				var search = searchText.replace('&','and');
				var vendorName= search.replace('&','and');
				document.location.href = "./rolodexdetails?rolodexNumber="+rxMasterId+"&name="+vendorName+"";
			}
	   });
		$(".requiredSave").show();
		return true;
	}

	function validation_message(){
		$('#rolodexID').validationEngine('hide');
	}

	function checkbox_validation(rolodexcheckbox){
		if(rolodexcheckbox.checked === true){
			$('#customerCheckID').validationEngine('hide');
		}
	}
	
</script>
