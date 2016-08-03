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
									<td><input type="text" name="rolodexName" id="VendorID" style="width:270px" class="validate[required] validate[maxSize[40]]"></td></tr>
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
		<div id="vendorLineitem" style = "border-top:1.5px #00377a solid;margin-top: 10px;width:770px; margin-bottom: -10px;">
		<div id="addRolodexAddress" style = "margin-top: 5px;">
			<%@ include file="rolodex/rxaddress_details.jsp" %>
		</div>
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
				height:345,
				width:800,
				title:"Add New Vendor",
				modal:true,
				open:function(){
					$("#dialogboxaddreditButtons").hide();
					$("#vendorCheckID").attr("checked",true);
					$("#remitaddress").css({"display":"inline"});
				},
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
		var vendorCheck = document.getElementById('vendorCheckID').checked;
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

			var newVendorValues = $("#addNewVendorForm").serialize();
			
			$.ajax({
				url: "rolodex/addNewRolodexList",
				mType: "GET",
				data : newVendorValues+"&vendorValue="+vendorCheck+ "&USPhoneNumber1="+ contact1 +"&USPhoneNumber2="+ contact2 + "&fax=" +fax,
				success: function(data) {
					var rxMasterId=data.rxMasterId;
					var searchText = $("#VendorID").val();
					var search = searchText.replace('&','and');
					var vendorName= search.replace('&','and');
					createtpusage('Company-Vendors','Add New Vendor','Info','Company-Vendors,Saving Vendor,rxMasterId:'+rxMasterId+',vendorName:'+vendorName);
					document.location.href = "./vendordetails?rolodexNumber="+rxMasterId+"&name="+vendorName+"";
				}
		   });
		$(".requiredSave").show();
		return true;
	}
 
</script>
