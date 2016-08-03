<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
  <div id=addnewcustab>
  <style>
  
  #LoadingDialog {
		  width: 100%;
		  height: 100%;
		  top: 0px;
		  left: 0px;
		  position: fixed;
		  display: block;
		  opacity: 0.7;
		  background-color: #fff;
		  z-index: 99;
		  text-align: center;
		}
		.loading {
		  display: inline-block;
		  margin: 20em;
		  border-width: 50px;
		  border-radius: 50%;
		  -webkit-animation: spin 1s linear infinite;
		     -moz-animation: spin 1s linear infinite;
		       -o-animation: spin 1s linear infinite;
		          animation: spin 1s linear infinite;
		  }
	
		.style-1 {
		  border-style: solid;
		  border-color: #444 transparent;
		  }
		
		.style-2 {
		  border-style: double;
		  border-color: #637C92 transparent;
		  }
		
		.style-3 {
		  border-style: double;
		  border-color: #444 #fff #fff;
		  }
		
		@-webkit-keyframes spin {
		  100% { -webkit-transform: rotate(359deg); }
		  }
		
		@-moz-keyframes spin {
		  100% { -moz-transform: rotate(359deg); }
		  }
		
		@-o-keyframes spin {
		  100% { -moz-transform: rotate(359deg); }
		  }
		
		@keyframes spin {
		  100% {  transform: rotate(359deg); }
		  }
		
		/* Base styles */
		/* html {
		 // background: #eee url('http://subtlepatterns.subtlepatterns.netdna-cdn.com/patterns/billie_holiday.png');
		  text-align: center;
		  }			 */
  </style>
	<form action="" id="addNewcutomerForm">
		<div id="soreleasegeneral">
			<table>
				<tr><td>
					<fieldset class= " ui-widget-content ui-corner-all" style="width:350px">
						<legend><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
									<td style=" width : 50px;"><label>Name:</label></td>
									<td><input type="text" name="rolodexName" id="customerName" style="width:270px" class="validate[required] validate[maxSize[40]]"></td>
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
		<div id="soreleaselineitem" style = "border-top:1.5px #00377a solid;margin-top: 10px;width:770px; margin-bottom: -10px;">
			<div id="addRolodexAddress" style = "margin-top: 5px;">
			<%@ include file="rolodex/rxaddress_details.jsp" %>
			</div>
		</div>
		
		 <div id="LoadingDialog">
		<span class="loading style-2"></span>
		</div> 

		<table style="width:745px">
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
		$("#LoadingDialog").css({visibility:"hidden"});
		$("#addnewcustab").tabs();
	});

	jQuery( function(){
		jQuery("#addnewcustab").dialog({
			autoOpen:false,
			height:335,
			width:800,
			title:"Add New Customer",
			modal:true,
			open:function(){
			$("#dialogboxaddreditButtons").hide();
			$("#customerCheckID").attr("checked",true);
			$("#addressSetting").css({"display":"inline"});
			},
			close:function()
			{
				$('#addNewcutomerForm').validationEngine('hideAll');
				return true;
			}
		});
	});

	
	function cancelAddCustomer(){
		$('#addNewcutomerForm').validationEngine('hideAll');
		$("#addnewcustab").dialog("close");
		return true;
	}
	
	function savenewcustomer() {
		if(!$('#addNewcutomerForm').validationEngine('validate')) {
			return false;
		}
		$("#LoadingDialog").css({visibility:"visible"});

		var customerCheck = document.getElementById('customer').checked;
	   var name = $("#customerName").val();
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

		
		var newRolodexValues = $("#addNewcutomerForm").serialize();
		
		$.ajax({
			url: "rolodex/addNewRolodexList",
			mType: "GET",
			data : newRolodexValues+"&customerValue="+customerCheck+ "&USPhoneNumber1="+ contact1 +"&USPhoneNumber2="+ contact2 + "&fax=" +fax,
			success: function(data) {
				$("#LoadingDialog").css({visibility:"hidden"});
				var rxMasterId=data.rxMasterId;
				createtpusage('Company-Customers','Save Customer','Info','Company-Customers,Save Customer,rxMasterId:'+ rxMasterId);
				var checkpermission=getGrantpermissionprivilage('Customers',0);
		    		if(checkpermission){
				document.location.href = "./customerdetails?rolodexNumber="+rxMasterId+"&name="+name+"";
		    		}
			}
	   });
		$(".requiredSave").show();
		return true;
	}
 
</script>
