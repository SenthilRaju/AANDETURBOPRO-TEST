<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- <script type="text/javascript" src="./../resources/js/jquery/jquery-1.4.4.min.js"></script>
	<script type="text/javascript">
	    var jq = jQuery.noConflict();
	</script>
	<script type="text/javascript" src="./../resources/js/jquery/jquery-ui-1.8.6.custom.min.js"></script> 
	<script type="text/javascript" src="./../resources/js/jqgrid/grid.locale-en.js" ></script>
	<script type="text/javascript" src="./../resources/js/jqgrid/jquery.jqGrid.min.js"></script> -->
<div>
	<form id="vendorheaderForm">
		<table>
			<tr>
				<td style="width:70px"><b><label>Name</label></b><span style="color:red;"> *</span></td>
					<td colspan="2"><input type="text" name="custName" style="width:300px" id="cusName" class="validate[required]" value=" ">
				</td>
			</tr>
		</table>
	</form>
</div>
	<script type="text/javascript">
	jQuery(document).ready(function(){
		var name =$("#customerName").text(); /*""+ getUrlVars()["name"]; 
		var arr = name.split("%20");
		name = "";
		for(var index = 0; index < arr.length; index++){
			name = name + arr[index] + " ";
		}
		//name = name.replace("%20", " "); */
		name = name.replace('(', ' ');
		name = name.replace(')', ' ');
		$("#cusName").val('');
		$("#cusName").val(name);
	});
	</script>