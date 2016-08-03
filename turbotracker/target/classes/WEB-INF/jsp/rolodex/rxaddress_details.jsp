<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>
	<form action="" id="rolodexAddressCustomForm">
		<div id="addAddress">
		<table>
		<tr>
		<td>
		<fieldset style="width:350px; height:146px" class= " ui-widget-content ui-corner-all">
		<legend><label><b>Address</b></label></legend>
		
			<table>
				<tr>
				<td style=" width : 50px;">
					<label>Address:</label>
				</td>
				<td>
					<input type="text" style="width: 261px;" id="rolodexAddress1" name="rolodexAddress1" maxlength="40" value="">
				</td>
				</tr>
				<tr>
				<td style=" width : 50px;">
					<label></label>
				</td>
				<td>
					<input type="text" style="width: 261px;" id="rolodexAddress2" name="rolodexAddress2"  maxlength="40" value="">
				</td>
				</tr>
				<tr>
				<td style=" width : 50px;">
					<label>City:</label>
				</td>
				<td>
					<input type="text" style="width: 155px;" id="rolodexCity" placeholder="Minimum 2 characters required" name="rolodexCity" value="">
					<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
					<label>State:</label>
					<input type="text" style="width: 25px;text-transform: uppercase" id="rolodexState" name="rolodexState" value="" maxlength="2">
				</td>
				</tr>
				<tr>
				<td style=" width : 50px;">
					<label>Zip: </label>
				</td>
				<td>
					<input type="text" style="width: 155px;" id="rolodexZip" class="validate[maxSize[10]]" name="rolodexZip" value="" maxlength="10">
					<span id="remitaddress" style="display:none"><input type="checkbox" id="remitCheckbox" name = "remitCheckbox" style="vertical-align: middle;"> <label> RemitTo? </label></span>
				</td>
				</tr>
				<tr>
				<td colspan="2" >
				<div id='addressSetting' style="float:right;display:none">
					<input type="checkbox" name="mailAddress" style="vertical-align:middle;" id="mailAddress"  /><label> Mailing</label>
					<input type="checkbox" style="vertical-align:middle;" id="shipAddress" name="shipAddress"  /><label> Shipping</label>
					<input type="checkbox" style="vertical-align:middle;" id="defaultAddress" name="defaultAddress"  /><label> Default</label>
				</div>
				</td>
				</tr>
			</table>
			
		</fieldset>
		</td>
		
		<td style="vertical-align:top" id="phoneField">
		<fieldset style="width:358px; height: 146px;" class= " ui-widget-content ui-corner-all">
		<legend><label><b>Phone</b></label></legend>
						
			<table id="phoneField">
				<tr>
				<td style=" width : 50px;">
					<label>Phone1:</label>
				</td>
				<td>
					<input type="text" id="areaId" name="contact1" style="width:50px" maxlength="3" class="number input1" value="">&nbsp;
					<input type="text" id="exchangeId" name="contact1" style="width:50px" maxlength="3" class="number input1" value="">&nbsp; - &nbsp;
					<input type="text" id="subscriberId" name="contact1" style="width:100px" maxlength="4" class="number input1" value="">
				</td>
				</tr>
				<tr>
				<td style=" width : 50px;">
					<label>Phone2:</label>
				</td>
				<td>
					<input type="text" id="areaId1" name="contact2" style="width:50px" maxlength="3" class="number input2" value="">&nbsp;
					<input type="text" id="exchangeId1" name="contact2" style="width:50px" maxlength="3" class="number input2" value="">&nbsp; - &nbsp;
					<input type="text" id="subscriberId1" name="contact2" style="width:100px" maxlength="4" class="number input2" value="">
				</td>
				</tr>
				<tr>
				<td style=" width : 50px;">
					<label>Fax:</label>
				</td>
				<td>  
					<input type="text" id="areaId2" name="contact3" style="width:50px" maxlength="3" class="number input3" value="">&nbsp;
					<input type="text" id="exchangeId2" name="contact3" style="width:50px" maxlength="3" class="number input3" value="">&nbsp; - &nbsp;
					<input type="text" id="subscriberId2" name="contact3" style="width:100px" maxlength="4" class="number input4" value="">
				</td>
				</tr>
				<tr>
				<td colspan="2">
				<div id="extensionaddress" style="display:none;">
					<label>Office Extension:</label>&nbsp;&nbsp;<input type="text" id="officeextension" name="officeextension" style="width:200px" value="">
					
				</div>
				</td>
				</tr>
				</table>
		</fieldset>
		</td>
		</tr>
		</table>
		</div>
		<br>
		<table align="right">
	 	<tr>
		<td align="right" style="padding-right:1px;" id="dialogboxaddreditButtons">
			 <input type="button" class="savehoverbutton turbo-blue" value="Save & Close" onclick="savenewRolodexAddress()" style=" width:120px;">
			 <input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelAddRolodexAddress()" style="width:80px;">  
		</td>
		</tr>
		</table>
	</form>
</div> 
<script type ="text/javascript">

$(function() { var cache = {}; var lastXhr=''; $( "#rolodexCity" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
	var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#rolodexState").val(stateCode);},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  });
	
	// Numbers Only
	$('.number').keypress(function (event) {
	    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
	        event.preventDefault();
	    }
	    var text = $(this).val();
	    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
	        event.preventDefault();
	    }
	});

	  $('.input1').keyup(function(){
	        if($(this).val().length==$(this).attr("maxlength")){
	            $(this).next().focus();
	        }
	    });
	  $('.input2').keyup(function(){
	        if($(this).val().length==$(this).attr("maxlength")){
	            $(this).next().focus();
	        }
	    });
	  $('.input3').keyup(function(){
	        if($(this).val().length==$(this).attr("maxlength")){
	            $(this).next().focus();
	        }
	    });
	
});

</script>