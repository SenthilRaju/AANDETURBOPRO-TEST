<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<table>
	<tr><td>
			<table style="padding-left:15px">
				<tr><td><label><b>Payable To</b></label></td></tr>
				<tr><td><select style="width:180px"><option></option></select></td></tr>
				<tr><td><textarea rows="4" cols="25"></textarea>
			</table>
		</td>
		<td style="vertical-align:top; padding-left:100px">
		
				<table>
					<tr><td><label><b>Invoice/Bill</b></label></td></tr>
					<tr><td><label>Received</label></td><td><input type="text" style="width:75px" id="datepicker"></td>
						<td><label>Vendor Inv.#</label></td><td><input type="text" style="width:75px"></td>
					</tr>
					<tr><td><label>Dated</label></td><td><input type="text" style="width:75px" id="datepicker"></td>
						<td><label>A/p Acct</label></td><td><select style="width:75px"><option></option></select></td>
					</tr>
					<tr><td><label>Due</label></td><td><input type="text" style="width:75px" id="datepicker"></td>
						<td><label>Post Date</label></td><td><input type="checkbox"></td>
					</tr>
				
					<tr><td colspan="2"><label>PO #</label></td><td><label>Ship:</label></td><td><input type="text" style="width:75px" id="datepicker"></td></tr>
					<tr><td><label>Ship Via</label></td><td><select style="width:75px"><option></option></select></td>
						<td><label>Pro #</label></td><td><input type="text" style="width:75px"></td>
					</tr>
				</table>
		</td>
		</tr>
	</table>
			<br>
			<table style="padding-left:5px">
				<tr><td><table id="vendorinvoice1" ></table><div id="vendorinvoicepager"></div></td></tr>
			</table>
			<br>
			<table style="padding-left:20px">
				<tr><td><label><b>Totals</b></label></td></tr>
				<tr><td><label>Subtotal</label><td><input type="text" style="width:75px"></td><td><label>Frieght</label></td><td><input type="text" style="width:75px"></td>
			    	<td><label>Tax</label></td><td><input type="text" style="width:75px"></td><td><label>Bal Due</label></td><td><input type="text" style="width:75px"></td></tr>
			</table>
			<br>

		
		
		
	<script type="text/javascript">
		 $(document).ready(function(){
			$(".datepicker").datepicker();
		 });
	</script>