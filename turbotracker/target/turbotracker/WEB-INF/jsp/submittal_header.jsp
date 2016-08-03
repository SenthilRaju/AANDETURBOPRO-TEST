<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link type="text/css" href="./../resources/styles/turbo-css/job_turbo.css" rel="stylesheet" />
     <table><tr><td>
	<fieldset class="custom_fieldset" style="width: 310px;">
		<legend class="custom_legend">
			<label><b>Project</b></label>
		</legend>
			<table>
				<tr>
					<td><textarea class="JobName" style="height: 35px;width: 294px;" disabled="disabled"></textarea></td>
				</tr>
			</table>
	</fieldset></td>
	<td width="50px;"></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 310px;">
		<legend class="custom_legend">
			<label><b>Location</b></label>
		</legend>
			<table>
				<tr>
					<td><textarea class="Joblocation_ID" style="height: 35px;width: 294px;" disabled="disabled"></textarea></td>
				</tr>
			</table>
	</fieldset></td>
	<td width="50px;"></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 106px;">
		<legend class="custom_legend">
			<label><b>Job Number</b></label>
		</legend>
			<table>
				<tr>
					<td><textarea class="JobNumber" style="height: 35px;width: 100px;" disabled="disabled"></textarea></td> 
				</tr>
			</table>
	</fieldset></td>
	</tr></table>
	
<script type="text/javascript">
$(".JobNumber").val($("#jobNumber_ID").text());
$(".JobName").val($("#jobName_ID").text());

var address = "${requestScope.joMasterDetails.locationAddress1}";
var address1 = "${requestScope.joMasterDetails.locationAddress2}";
var addressmain;
if(address === null){addressmain="";	}else{addressmain = address;}

if(address1 !== null && address1 !== ""){
	addressmain = address + ", " + address1;
} else if ((address === null || address1 === "") && (address1 !== null && address1 !== "")) {
	addressmain = address1;
} else {
	addressmain = address;
}
$(".Joblocation_ID").empty();
$(".Joblocation_ID").val(addressmain);
</script>