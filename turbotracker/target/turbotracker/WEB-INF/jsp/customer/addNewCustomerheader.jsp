<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<table>
	<tr><td>
		<fieldset class= " ui-widget-content ui-corner-all" style="width:330px">
			<legend><label><b>Name</b></label></legend>
				<table>
					<tr><td><input type="text" name="name" id="name" style="width:250px"></td></tr>
				</table>
		</fieldset>
	</td>
	<td style="width:20px"></td>
	<td style="vertical-align:top">
		<fieldset class= " ui-widget-content ui-corner-all" style="width:250px;">
			<legend><label><b>Categories</b></label></legend>
				<table>
					<tr align="center"><td style="vertical-align: middle;"><label>Customer</label><input type="checkbox" id="customer" value="1" checked="checked" disabled="disabled" style="vertical-align: middle;">
				</table>
		</fieldset>
		</td></tr>
	</table>
<script type="text/javascript">
jQuery (function(){
	$('#datepicker').datepicker();
});
</script>