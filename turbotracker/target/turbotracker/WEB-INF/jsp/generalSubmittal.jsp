<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="generalSubmittal"> 
	<div id="generalSubmittalTabs" style="padding-left: 0px;">
		<ul>
              	<li id="submittalgeneral"><a href="submittal_General">General</a></li>
              	<li style="float: right;display: none;">
              	<label id="jobNumber_ID" >${requestScope.jobNumber}</label>
				<label id="jobName_ID">${requestScope.jobName}</label>
				<label id="jobCustomerName_ID">${requestScope.jobCustomer}</label>
				<label id="detailsHeader_ID"></label>
				<label id="productName_ID"></label>
              	</li>
              </ul>
	</div>
 </div>
<script type="text/javascript">
jQuery(document).ready(function() {
	$("#generalSubmittalTabs").tabs({
		cache: true,
		ajaxOptions: {
			data:{
				jobNumber: $("#jobNumber_ID").text(),
				jobName:"'" + $("#jobName_ID").text() + "'",
				jobCustomer:$("#jobCustomerName_ID").text()
			},
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 60px;padding-top: 30px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			var $panel = $(ui.panel);
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;'><img src='./../resources/scripts/jquery-autocomplete/loading11.gif'></div>");
			}
		}
	});
}); 

jQuery(function () {
	jQuery( "#generalSubmittal" ).dialog({
				autoOpen: false,
				height: 870,
				width: 897,
				title:"General Submittal",
				modal: true,
				buttons:{		},
				close: function () {
					return true;
				}
			});
	 });
</script>