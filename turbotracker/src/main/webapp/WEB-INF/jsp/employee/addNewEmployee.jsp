<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
  <div id= "addNewEmployee">
	<form action="" id="addNewEmployeeForm">
		<div id="employeeGeneral">
			<table>
				<tr><td>
					<fieldset class= " ui-widget-content ui-corner-all" style="height:70px;">
						<legend><label><b>Name</b></label></legend>
							<table>
								<tr>
									<td style=" width : 80px;"><label>Name:<span style="color:red;"> *</span></label></td>
									<td><input type="text" name="employeeName" id="employeeID" style="width:270px" class="validate[required] validate[maxSize[40]]"></td>
								</tr>
								<tr>
									<td style=" width : 80px;"><label>First Name:</label></td>
									<td><input type="text" name="employeeName1" id="employeeID1" style="width:270px"></td>
								</tr>
							</table>
					</fieldset>
					</td>
					<td style="vertical-align:top">
						<fieldset class= " ui-widget-content ui-corner-all" style="width:150px;height:70px;">
							<legend><label><b>Categories</b></label></legend>
								<table>
									<tr align="center">
										<td style="width : 50px">
											<label>Employee</label>
										</td>
										<td>
											<input type="checkbox" id="employeeCheckID" name="employeeCheckName" value="1" checked="checked" disabled="disabled" style="vertical-align: middle;">
										</td>
									</tr>
									<tr align="center">
										<td style="width : 50px">
											<label>InActive</label>
										</td>
										<td>
											<input type="checkbox" id="InactiveCheckID" name="inActiveCheckName"  style="vertical-align: middle;">
										</td>
									</tr>
								</table>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
		<div id="employeeLineitem">
			<table>
			<tr><td colspan="2"><hr></td></tr>
			<tr><td style="vertical-align:top">
			  <fieldset style="width:350px; height:130px" class= " ui-widget-content ui-corner-all">
				<legend><label><b>Address</b></label></legend>
				<table>
					<tr>
						<td style="width : 50px">
							<label>Address:</label>
						</td>
						<td>
							<input type="text" style="width: 261px;" id="address1ID" name="address1Name" value="">
						</td>
					</tr>
					<tr>
						<td style="width : 50px">
							<label></label>
						</td>
						<td>
							<input type="text" style="width: 261px;" id="address2ID" name="address2Name" value="">
						</td>
					</tr>
					<tr>
						<td style="width : 50px">
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
						<td style="width : 50px">
							<label>Zip: </label>
						</td>
						<td>
							<input type="text" style="width: 100px; " id="pinCodeID" class="validate[maxSize[10]]" name="pinCodeName" value="">
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
						<td style="width : 50px">
							<label>Phone1:</label>
						</td>
						<td>
							<input type="text" id="areaCode" name="contact1"  style="width:50px" maxlength="3" value="" class="validate[custom[number]]" >&nbsp;
							<input type="text" id="exchangeCode" name="contact1"  style="width:50px" maxlength="3" value="" class="validate[custom[number]]" >&nbsp; - &nbsp;
							<input type="text" id="subscriberNumber" name="contact1" style="width:100px" value="" class="validate[custom[number]]" >
						</td>
					</tr>
					<tr>
						<td style="width : 50px">
							<label>Phone2:</label>
						</td>
						<td>
							<input type="text" id="areaCode1" name="contact2"  style="width:50px" maxlength="3" value="" class="validate[custom[number]]">&nbsp;
							<input type="text" id="exchangeCode1" name="contact2"  style="width:50px" maxlength="3" value="" class="validate[custom[number]]">&nbsp; - &nbsp;
							<input type="text" id="subscriberNumber1" name="contact2"  style="width:100px" value="" class="validate[custom[number]]">
						</td>
					<tr>
						<td>
							<label>Fax:</label>
						</td>
						<td>
							<input type="text" id="areaCode2" name="contact3" style="width:50px" maxlength="3" value="" class="validate[custom[number]]">&nbsp;
							<input type="text" id="exchangeCode2" name="contact3"  style="width:50px" maxlength="3" value="" class="validate[custom[number]]">&nbsp; - &nbsp;
							<input type="text" id="subscriberNumber2" name="contact3"  style="width:100px" value="" class="validate[custom[number]]">
						</td>
					</tr>
					<tr>
						<td>
							<label>Office Extension:</label>
						</td>
						<td>
							<input type="text" id="officeExtesion" name="officeExt" class="validate[custom[number]]" style="width:150px" value="">
						</td>
					</tr>
				</table>
			</fieldset>
			</td>
		</table>
		</div>
		<br>
		<table align="center" style="width:745px;">
		 	<tr>
			 	<td align="right" style="padding-right:1px;">
					<input type="button" class="savehoverbutton turbo-blue" value="Save & Close" onclick="saveNewEmployee()" style=" width:120px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelEmployee()" style="width:80px;">  
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
			$("#addNewEmployee").tabs();
		});
		jQuery( function(){
			jQuery("#addNewEmployee").dialog({
				autoOpen:false,
				height:355,
				width:800,
				title:"Add New Employee",
				modal:true,
				close:function(){
					$('#addNewEmployeeForm').validationEngine('hideAll');
					 return true; }
			});
		});

		 $(function() { var cache = {}; var lastXhr=''; $( "#cityNameListID" ).autocomplete({ minLength: 2,timeout :1000,
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
	
	function cancelEmployee(){
		$('#addNewEmployeeForm').validationEngine('hideAll');
		$("#addNewEmployee").dialog("close");
		return true;
	}
	
	function saveNewEmployee() {
		if(!$('#addNewEmployeeForm').validationEngine('validate')) {
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
		var aActive = false;
		if($('#InactiveCheckID').is(':checked')){
			aActive =  true;
		}
		var newEmployeeValues = $("#addNewEmployeeForm").serialize();
		$.ajax({
			url: "employeeCrud/addNewEmployeeList",
			mType: "GET",
			data : newEmployeeValues+"&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax="+ fax +"&aActiveEmployee="+ aActive,
			success: function(data) {
				var rxMasterId=data.rxMasterId;
				var searchText = $("#employeeID").val();
				var search = searchText.replace('&','and');
				var employeeName= search.replace('&','and');
				createtpusage('Company-Employee','Save Employee','Info','Company-Employee,Saving Employee,rxMasterId:'+rxMasterId+',name:'+employeeName);
				document.location.href = "./employeedetails?rolodexNumber="+rxMasterId+"&name="+employeeName+"";
			}
	   });
		$(".requiredSave").show();
		return true;
	}
 
</script>
