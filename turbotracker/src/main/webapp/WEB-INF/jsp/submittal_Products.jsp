<div id="submittalproducts">
	<table>
		<tr>
			<td><div><jsp:include page="submittal_header.jsp"></jsp:include></div></td>
		</tr>
		<tr>
			<td><hr width="820px"></td>
		</tr>
		<tr height="10px;"><td></td></tr>
	</table>
	<table style="display: none;">
		<tr>
			<td>
				<input type="text" id="submittal_header_ID" value="${requestScope.joSubmittalDetails.joSubmittalHeaderId}">
			</td>
		</tr>
	</table>
	<table id="product"></table>
	<div id="productpager"></div>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/submittal_Products.js"></script>	
		