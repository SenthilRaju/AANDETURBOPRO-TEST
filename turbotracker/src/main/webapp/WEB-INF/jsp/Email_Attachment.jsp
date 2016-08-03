<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.loadingDivEmailAttachment{
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background: url('../resources/scripts/jquery-autocomplete/send_mail_waiting.gif') 50% 50% no-repeat;
}
</style>
<div id="emailpopup" title="Email Compose" style="display:none">
<iframe id="uploadFrameID" name="uploadFrame1" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
		<form id="emailform" name="emailform">
			<fieldset>
				<table>
					<tr>
						<td>From </td>
						<td><input type="text" name="efromaddr" id="efromaddr" style="width: 250px; color: black; font-family: Arial" readonly="readonly"></td>
					</tr>
					<tr>
						<td>To </td>
						<td><input type="text" name="etoaddr" id="etoaddr" style="width: 250px; color: black; font-family: Arial"></td>
					</tr>
					<tr>
						<td>Cc </td>
						<td><input type="text" name="eccaddr" id="eccaddr" style="width: 250px; color: black; font-family: Arial"></td>
					</tr>
					<tr>
				
						<td>Subject </td>
						<td><input type="text" name="esubj" id="esubj" style="width:250px; color: black; font-family: Arial"></td>
					</tr>
					
					<tr>
						<td>Content </td>
						<td><textarea id="econt" name="econt" rows="3" cols="39" style="width: 400px; height:250px; value="" class="myTextEditor">
								       </textarea>
								       
											
						</td>
					</tr> 
					
					  <tr align="center">
								<td>Attachment </td>
								<td><label id="filelabelname"></label></label><input type="hidden" name="attachmentfilename"  id="attachmentfilename"  /></td>
									
					 </tr> 
					 
					 
				
				</table>
				
				</fieldset>
		</form> 
	  <table align="right">
	 <br><br>
	  <tr >
					 <td><input type="button" class="cancelhoverbutton turbo-tan"  value="Send" onclick="submitemailattachment()" style="width:80px;"/></td>
					 <td><input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="CancelDialog()" style="width:80px;"/></td>
	 </tr>
	  </table>
		<div class="loadingDivEmailAttachment" id="loadingDivForPO" style="display: none;opacity: 0.7;background-color: #fff;z-index: 1234;text-align: center;">
</div>
 </div>
<script type="text/javascript">
function CancelDialog(){
	$("#emailpopup" ).dialog("close");
} 


function clearemailattachmentForm(){
	$("#efromaddr").val("");
	$("#etoaddr").val("");
	$("#eccaddr").val("");
	$("#esubj").val("");
	$("#econt").val("");
	$(".nicEdit-main").html("");
	$("#filelabelname").text("");
	$("#attachmentfilename").val("");
	
	
}
</script>