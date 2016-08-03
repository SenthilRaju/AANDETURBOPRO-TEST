<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div id="addCost" align="left">
		<div>
		<div id="gridDatas">
			<table id="addCostGrid"></table>
			<div id="addCostPager"></div>
		</div>
			<table style="background:#EEDEBC;width:951px;height:48px">
				<tr>
					<td colspan="2" align="center"><div id="addCostMsg"  style="display: none"></div></td>
				</tr>
				<tr>
					<td style="width: 60px;padding: 0px 1px;">
					
						<%-- <fieldset style="padding-bottom: 0px;width: 60px;background: #EEDEBC;border: 0px;box-shadow: 0px 0px 5px #C2A472;border-radius: 5px;">
    						<table>
    							<tr>
					      			<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Order" style="background: #EEDEBC; border: 0px;"></td>
					    		  	<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit Order" style="background: #EEDEBC; border: 0px;"></td>
					   			   	<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Order" style="background: #EEDEBC; border: 0px;"></td>
	    						</tr>
    						</table>
   						</fieldset> --%>
  					</td>
  					<td><input type="button" value="Save & Close" class="savehoverbutton turbo-tan" id="closeID" onclick="addCostClose()" style="width:100px;margin-left:758px;"></td>				
				</tr>
   			</table>
	    </div>	
	</div>

<script type="text/javascript" src="./../resources/scripts/turbo_scripts/addCost.js"></script> 
<script type="text/javascript" src="./../resources/web-plugins/jquery.ui.datetimepicker.min.js"></script>
