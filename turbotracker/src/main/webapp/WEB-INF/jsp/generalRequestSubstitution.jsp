<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="requestSubstitutiongeneral">
<table><tr><td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Project</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobHeader_JobName" class="projectClass">${requestScope.joMasterDescription}</label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Location</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="JoblocationID"></label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 106px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Job Number</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobHeader_JobNumber" class="projectClass">${sessionScope.jobGridObj.jobNumber}</label></td> 
				</tr>
			</table>
	</fieldset></td>
	</tr></table>
	<table><tr><td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Architect</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobMain_architectsList" class="projectClass">${requestScope.joMasterDetails.rxCategory1}</label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Engineer</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobMain_EngineersList" class="projectClass">${requestScope.joMasterDetails.cuAssignmentId3}</label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 106px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Bid Date</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="bidDate_Date" class="projectClass"></label></td> 
				</tr>
			</table>
	</fieldset></td>
	</tr></table>
	<table><tr><td>
	<fieldset class="custom_fieldset" style="width: 150px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Submittal By</b></label>
		</legend>
			<table>
				<tr>
					<td><input type="text" id="job_Submittal" style="width: 100px;"title="Must be atleast 2 characters long">
					<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long"></td>
					<td><input type="hidden" id="submittal" style="width: 15px;">
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 150px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Date</b></label>
		</legend>
			<table>
				<tr>
					<td><input type="date" id="dateId" class="datepicker" value="" style="width: 120px;">
				</tr>
			</table>
	</fieldset></td>
	<td><label id="jbgfjnb"></label></td>
	</tr></table>
	<table>
		<tr>
			<td height="10px;"></td>
		</tr>
	</table>
	<fieldset class="custom_fieldset" style="width: 782px;">
		<legend class="custom_legend">
			<label><b>Opening Text</b></label>
		</legend>
			<table>
				<tr>
					<td><textarea class="textarea" rows="4" cols="93" id=" "></textarea></td>
				</tr>
			</table>
	</fieldset>
	<table>
		<tr>
			<td height="10px;"></td>
		</tr>
	</table>
	<fieldset class="custom_fieldset" style="width: 782px;">
		<legend class="custom_legend">
			<label><b>Close Text</b></label>
		</legend>
			<table>
				<tr>
					<td><textarea class="textarea" rows="4" cols="93" id=" "></textarea></td>
				</tr>
			</table>
	</fieldset>
</div>

<script type="text/javascript">
jQuery(document).ready(function(){
	var bidDate = customFormatDate("${requestScope.joMasterDetails.bidDate}");
	$('#bidDate_Date').empty();	
	$('#bidDate_Date').text(bidDate);
	
	$(function() { var cache = {}, lastXhr;
	$( "#job_Submittal" ).autocomplete({ minLength: 1,timeout :1000, select: function( event, ui ) { var id = ui.item.id; $("#submittal").val(id);},
	source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/userInitials", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });
});

	date = new Date();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var year = date.getFullYear();
	if (document.getElementById('dateId').value == ''){
	document.getElementById('dateId').value = month+'/'+day+'/' + year;
	}

</script>

