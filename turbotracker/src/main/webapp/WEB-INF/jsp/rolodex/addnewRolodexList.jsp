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
							<table width="100%">
								<tr align="center">
									<td width="25%">
									<label  >Customer</label><br/>
										<input type="checkbox" id="customerCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this);showaddressSettings();">

									</td>
									<td width="25%">
									<label>Vendor</label><br/>
										<input type="checkbox" id="vendorCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this);showaddressSettings();">
									</td>
									<td width="25%">
										<label >Employee</label><br/>
										<input type="checkbox" id="employeeCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</td>
									<td width="25%">
									<span  id="category1Td" style="display: none;">
									<label id="category1">Architect</label><br/>
										<input type="checkbox" id="architectCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
								</tr>
								<tr align="center">
									<td width="26%">
									<span  id="category2Td" style="display: none;">
									<label id="category2">Engineer</label><br/>
										<input type="checkbox" id="engineerCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
									<td width="25%">
									<span  id="category3Td" style="display: none;">
									<label id="category3">G.C</label><br/>
										<input type="checkbox" id="gcCheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
									<td width="25%">
									<span  id="category4Td" style="display: none;">
									<label id="category4">G.Const MGR</label><br/>
										<input type="checkbox" id="category4CheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
									<td width="25%">
									<span  id="category5Td" style="display: none;">
									<label id="category5">category5</label><br/>
										<input type="checkbox" id="category5CheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
									
								</tr>
								<tr align="center">
									<td width="25%">
									<span  id="category6Td" style="display: none;">
									<label id="category6">category6</label><br/>
										<input type="checkbox" id="category6CheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
									<td width="25%">
									<span  id="category7Td" style="display: none;">
									<label id="category7">category7</label><br/>
										<input type="checkbox" id="category7CheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
									<td width="25%">
									<span  id="category8Td" style="display: none;">
									<label id="category8">category8</label><br/>
										<input type="checkbox" id="category8CheckID" name="customerCheckName" style="vertical-align: middle;" class="validate[minCheckbox[1]]" onclick="checkbox_validation(this)">
									</span>
									</td>
								</tr>
							</table>
					</fieldset>
					</td></tr>
			</table>
		</div>
		<div id="rolodexLineitem" style = "border-top:1.5px #00377a solid;margin-top: 10px;width:770px; margin-bottom: -10px;">
			<div id="addRolodexAddress" style = "margin-top: 5px;">
			<%@ include file="rxaddress_details.jsp" %>
			</div>
		</div>
		 <br>
		<table align="center" style="width:745px;margin-top:-10px;">
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

			$.ajax({
				url: "./rolodex/getRolodexCategory",
				mType: "GET",
				data : {'userInfoID' : 1},
				success: function(data) {
				console.log('RxCategories category1:'+data.rxMasterCategory1desc);
				if(data.rxMasterCategory1desc!=null && data.rxMasterCategory1desc!=''){
					$("#category1Td").css('display','block');
					$("#category1").text(data.rxMasterCategory1desc);
				}
				if(data.rxMasterCategory2desc!=null && data.rxMasterCategory2desc!=''){
					$("#category2Td").css('display','block');
					$("#category2").text(data.rxMasterCategory2desc);
				}
				if(data.rxMasterCategory3desc!=null && data.rxMasterCategory3desc!=''){
					$("#category3Td").css('display','block');
					$("#category3").text(data.rxMasterCategory3desc);
				}
				if(data.rxMasterCategory4desc!=null && data.rxMasterCategory4desc!=''){
					$("#category4Td").css('display','block');
					$("#category4").text(data.rxMasterCategory4desc);
				}
				if(data.rxMasterCategory5desc!=null && data.rxMasterCategory5desc!=''){
					$("#category5Td").css('display','block');
					$("#category5").text(data.rxMasterCategory5desc);
				}
				if(data.rxMasterCategory6desc!=null && data.rxMasterCategory6desc!=''){
					$("#category6Td").css('display','block');
					$("#category6").text(data.rxMasterCategory6desc);
				}
				if(data.rxMasterCategory7desc!=null && data.rxMasterCategory7desc!=''){
					$("#category7Td").css('display','block');
					$("#category7").text(data.rxMasterCategory7desc);
				}
				if(data.rxMasterCategory8desc!=null && data.rxMasterCategory8desc!=''){
					$("#category8Td").css('display','block');
					$("#category8").text(data.rxMasterCategory8desc);
				}
				}
		   });
			
		});
		jQuery( function(){
			jQuery("#addNewRolodex").dialog({
				autoOpen:false,
				height:450,
				width:810,
				title:"Add New Rolodex",
				modal:true,
				open:function(){$("#dialogboxaddreditButtons").hide();
				
			if($('#customercheckbox').is(':checked'))
				{
				$("#customerCheckID").attr("checked",true);
				$("#addressSetting").css({"display":"inline"});}
			else
				{
				$("#customerCheckID").removeAttr("checked");
				$("#addressSetting").css({"display":"none"});}
			
			if ($('#vendorcheckbox').is(':checked'))
				{$("#vendorCheckID").attr("checked",true);
				$("#remitaddress").css({"display":"inline"});}
			else
				{$("#vendorCheckID").removeAttr("checked");
				$("#remitaddress").css({"display":"none"});}
			
			if($('#employeecheckbox').is(':checked'))
				$("#employeeCheckID").attr("checked",true);
			else
				$("#employeeCheckID").removeAttr("checked");
			
			if ($('#architectcheckbox').is(':checked'))
				$("#architectCheckID").attr("checked",true);
			else
				$("#architectCheckID").removeAttr("checked");
			
			if($('engineercheckbox').is(':checked'))
				$("#engineerCheckID").attr("checked",true);
			else
				$("#engineerCheckID").removeAttr("checked");
			
			if($('#gccheckbox').is(':checked'))
				{$("#gcCheckID").attr("checked",true);}
			else
				{$("#gcCheckID").removeAttr("checked");}
			
			if ($('#ownercheckbox').is(':checked'))
				$("#category4CheckID").attr("checked",true);
			else
				$("#category4CheckID").removeAttr("checked");
			
			if($('#ListCategory5Desc').is(':checked'))
				$("#category5CheckID").attr("checked",true);
			else
				$("#category5CheckID").removeAttr("checked");
			
			if ($('#ListCategory6Desc').is(':checked'))
				$("#category6CheckID").attr("checked",true);
			else
				$("#category6CheckID").removeAttr("checked");
			
			if ($('#ListCategory7Desc').is(':checked'))
				$("#category7CheckID").attr("checked",true);
			else
				$("#category7CheckID").removeAttr("checked");
			
			if ($('#ListCategory8Desc').is(':checked'))
				$("#category8CheckID").attr("checked",true);
			else
				$("#category8CheckID").removeAttr("checked");
				},
				close:function(){ $('#addNewRolodexForm').validationEngine('hideAll');
				return true; }
			});

			
		});

		function showaddressSettings()
		{
			if($('#customerCheckID').is(':checked'))
				$("#addressSetting").css({"display":"inline"});
			else 
				$("#addressSetting").css({"display":"none"});
			
			if ($('#vendorCheckID').is(':checked'))
				$("#remitaddress").css({"display":"inline"});
			else 
				$("#remitaddress").css({"display":"none"});
		}

	
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
		var category4CheckID=document.getElementById('category4CheckID').checked;
		var category5CheckID=document.getElementById('category5CheckID').checked;
		var category6CheckID=document.getElementById('category6CheckID').checked;
		var category7CheckID=document.getElementById('category7CheckID').checked;
		var category8CheckID=document.getElementById('category8CheckID').checked;
		
		var areaCode=$("#areaId").val();
		var exchangeCode = $("#exchangeId").val();
		var subscriberNumber = $("#subscriberId").val();
		var contact1='';
		if(areaCode !== ''){
		contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
		}
		var areaCode1=$("#areaId1").val();
		var exchangeCode1 = $("#exchangeId1").val();
		var subscriberNumber1 = $("#subscriberId1").val();
		var contact2='';
		if(areaCode1 !== ''){
			contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
		}
		var areaCode2=$("#areaId2").val();
		var exchangeCode2 = $("#exchangeId2").val();
		var subscriberNumber2 = $("#subscriberId2").val();
		var fax='';
		if(areaCode2 !== ''){
			fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
		}
		
		var newRolodexValues = $("#addNewRolodexForm").serialize();
		$.ajax({
			url: "rolodex/addNewRolodexList",
			mType: "GET",
			data : newRolodexValues+"&customerValue="+customerCheck+"&vendorValue="+vendorCheck+"&employeeValue="+employeeCheck+ "&USPhoneNumber1="+ contact1 +"&USPhoneNumber2="+ contact2 + "&fax=" +fax+"&architectValue="+architectCheck+"&engineerValue="+engineerCheck+"&gcValue="+gcCheck+"&category4CheckID="+category4CheckID+"&category5CheckID="+category5CheckID+"&category6CheckID="+category6CheckID+"&category7CheckID="+category7CheckID+"&category8CheckID="+category8CheckID,
			success: function(data) {
				var rxMasterId=data.rxMasterId;
				var searchText = $("#rolodexID").val();
				var search = searchText.replace('&','and');
				var vendorName= search.replace('&','and');
				createtpusage('Company-Rolodex','Save Rolodex','Info','Company-Employee,Saving Rolodex,rxMasterId:'+rxMasterId+',vendorName:'+vendorName);
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
