<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div id="changeorder" align="left">
		<div>
			<table id="changeOrderGrid" style="width:50px"></table>
			<table style="background:#EEDEBC;width:951px;height:48px">
				<tr>
					<td colspan="2" align="center"><div id="changeOrderMsg" style="display: none"></div></td>
				</tr>
				<tr>
					<td style="width: 60px;padding: 0px 1px;">
						<fieldset style="padding-bottom: 0px;width: 60px;background: #EEDEBC;border: 0px;box-shadow: 0px 0px 5px #C2A472;border-radius: 5px;">
    						<table>
    							<tr>
					      			<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Order" style="background: #EEDEBC; border: 0px;" onclick="addOrder()"></td>
					    		  	<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit Order" style="background: #EEDEBC; border: 0px;" onclick="editOrder()"></td>
					   			   	<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Order" style="background: #EEDEBC; border: 0px;" onclick="deleteOrder()"></td>
	    						   
	    						</tr>
    						</table>
   						</fieldset>
  					</td>
  					<td><input type="button" value="Close" class="savehoverbutton turbo-tan" id="closeID" onclick="changeOrderClose()" style="width:80px;margin-left:758px;"></td>				
				</tr>
   			</table>
	    </div>	
	</div>
	
<div id="changeOrderDialog">
	<form action="" id="changeOrderFormID">
		<div id="message" class="warningMsg"></div>
		<table>
			<tr>
				<td><label id="releasedLableID">Date:<span style="color:red;"> </span> </label></td>
				<td><input type="text" id="releaseddate" name="orderdate" style="width: 180px;" ></td>
			</tr>
			<tr>
				<td ><label>PO Number:<span style="color:red;"></span></label></td>
				<td><input type="text" id="PONumber" name="orderponumber" style="width: 200px;"></td>
			</tr>
			<tr>
				<td><label>Entered By: </label></td>
				<td><input type="text" id="EnteredBy" name="orderentered" style="width: 200px;text-transform: uppercase;" placeholder="Minimum 2 characters required"/>
				<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
			</tr>
			<tr>
				<td><label>Reason For Change: </label></td>
				<td><input type="text" id="ReasonForChange" name="orderchange" style="width: 200px;"/>
			</tr>
			<tr>
				<td><label>Amount: </label></td>
				<td><input type="text" id="Amount" name="orderamount" style="width: 200px;" class="validate[custom[number]]"/>
			</tr>
			<tr>
				<td><label>Cost: </label></td>
				<td><input type="text" id="cost_id" name="cost_name" style="width: 200px;" class="validate[custom[number]]"/>
			</tr>
			<tr  style="display: none;">
				<td ><label>joMasterID:<span style="color:red;display:none"></span></label></td>
				<td><input type="text" id="joMasterID" name="orderjomaster" style="width: 200px;"></td>
			</tr>
			<tr style="display: none;">
				<td ><label>joChangeID:<span style="color:red;display:none"></span></label></td>
				<td><input type="text" id="joChangeID" name="orderjoChangeID" style="width: 200px;"></td>
			</tr>
			<tr style="display: none;">
				<td ><label>ChangeById:<span style="color:red;display:none"></span></label></td>
				<td><input type="text" id="ChangeById" name="orderChangeById" style="width: 200px;"></td>
			</tr>
		</table>
		<table align="right">
			<tr>
				<td><input type="button" id="saveReleaseID" class="savehoverbutton turbo-tan" name="saveOrdernam" value="Save" onclick="saveOrder()" style="width: 90px;"></td>
				<td><input type="button" id="cancelReleaseID" class="savehoverbutton turbo-tan" name="cancelOrdernam" value="Cancel" onclick="cancelOrder()" style="width: 90px;"></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/changeOrder.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.ui.datetimepicker.min.js"></script>
