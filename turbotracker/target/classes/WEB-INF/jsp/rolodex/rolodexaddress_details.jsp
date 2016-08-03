<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	 <div id="rxAddressGrid">
		<table style="margin:0 auto">
			<tr>
				<td colspan="3">
					<div id="rxAddressGridCategories" style="padding-left: 0px;">
						<table style="padding-left: 0px" id="rxAddressGridCategoriesGrid" class="scroll"></table>
						<div id="rxAddressGridCategoriesGridpager" class="scroll" style="text-align: right;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td align="right">
					<input type="button" class="add" id="add"	value="Add" onclick="addAddressRolodexDetails()">
					<input type="button" class="cancelhoverbutton turbo-blue" id="edit" value="Edit" style="width:60px" onclick="editAddressRolodexDetails()">
					<input type="button" class="cancelhoverbutton turbo-blue" id="delete" value="Delete" style="width:60px" onclick="deleteAddressRolodexDetails()">
					<input type="button" class="cancelhoverbutton turbo-blue" id="cancel"	value="Cancel" style="width:80px" onclick="cancelAddressRolodexDetails()"></td>
				</td>
			</tr>
			<tr height="10px;"></tr>
		</table>
		
		<div id=addRolodexAddress>
			<%@ include file="rxaddress_details.jsp" %>
		</div>
		
	</div>
	 <script type="text/javascript">
	
	if(_blockaddressjsfiles)
		{
		_blockaddressjsfiles = false;
		$.getScript("./../resources/scripts/turbo_scripts/rxAddress_Details.js");
		}

	</script>	