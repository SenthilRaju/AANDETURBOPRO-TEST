<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
.no-close .ui-dialog-titlebar-close {
  display: none;
}
.loadingDivpopup{
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    height: 158%;
    width: 100%;
    background: url('../resources/scripts/jquery-autocomplete/send_mail_waiting.gif') 50% 50% no-repeat;
}
</style>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<script src="${contextPath}/resources/js/jquery.js" type="text/javascript"></script> -->
	 <link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
	<!-- The following code is Important code to get rid of the "/turbo/" part in URL -->
	<!-- <link type="text/css" href='<c:url value="/resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/themes/custom-theme/jquery-ui-1.8.16.custom.css" />' rel="stylesheet" /> -->
	<link type="text/css" href="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/themes/custom-theme/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
	<link rel="stylesheet" href="./../resources/styles/welcomeCSS/welcomeStyles.css" type="text/css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="./../resources/web-plugins/Validation_Engine/validationEngine.jquery.css">
	<link rel="shortcut icon" href="./../resources/styles/welcomeCSS/welcomeImages/turbopro_shortcut_icon.png"  type="image/x-icon"/>
	<title>Turbo Pro - Login</title>
	<style>
	.tooltip{position:absolute;z-index:1070;display:block;font-family:"Helvetica Neue",Helvetica,Arial,sans-serif;font-size:12px;font-weight:400;line-height:1.4;filter:alpha(opacity=0);opacity:0}
	.tooltip.in{filter:alpha(opacity=90);opacity:.9}
	.tooltip.top{padding:5px 0;margin-top:-3px}
	.tooltip.right{padding:0 5px;margin-left:3px}
	.tooltip.bottom{padding:5px 0;margin-top:3px}.tooltip.left{padding:0 5px;margin-left:-3px}
	.tooltip-inner{max-width:950px;padding:3px 8px;color:#fff;text-align:center;text-decoration:none;background-color:#000;border-radius:4px}
	.tooltip-arrow{position:absolute;width:0;height:0;border-color:transparent;border-style:solid}
	.tooltip.top .tooltip-arrow{bottom:0;left:50%;margin-left:-5px;border-width:5px 5px 0;border-top-color:#000}
	.tooltip.top-left .tooltip-arrow{right:5px;bottom:0;margin-bottom:-5px;border-width:5px 5px 0;border-top-color:#000}
	.tooltip.top-right .tooltip-arrow{bottom:0;left:5px;margin-bottom:-5px;border-width:5px 5px 0;border-top-color:#000}
	.tooltip.right .tooltip-arrow{top:20%;left:0;margin-top:-5px;border-width:5px 5px 5px 0;border-right-color:#000}
	.tooltip.left .tooltip-arrow{top:80%;right:0;margin-top:-5px;border-width:5px 0 5px 5px;border-left-color:#000}
	.tooltip.bottom .tooltip-arrow{top:0;left:50%;margin-left:-5px;border-width:0 5px 5px;border-bottom-color:#000}
	.tooltip.bottom-left .tooltip-arrow{top:0;right:5px;margin-top:-5px;border-width:0 5px 5px;border-bottom-color:#000}
	.tooltip.bottom-right .tooltip-arrow{top:0;left:5px;margin-top:-5px;border-width:0 5px 5px;border-bottom-color:#000}
	.fade{opacity:0;-webkit-transition:opacity .15s linear;-o-transition:opacity .15s linear;transition:opacity .15s linear}
	</style>
</head>
<body onLoad="document.getElementById('uname').focus();">
<input type="hidden" id="versionchanges" value="${requestScope.VersionChanges}" />
	<div id="welcomepage">
	<div id="header">
		<div class="logo">
			<img src="./../resources/styles/welcomeCSS/welcomeImages/turbopro_logo_new.png" />
			<label class="versionField">Version <%=ResourceBundle.getBundle("version").getString("app.jdbc.appversion") %></label>
		</div>
		<div class="header_right">
			<div class="top_navi">
				<ul>
					<li><a href="http://turborep.com/company.html" target="_blank">About Us</a></li>
					<li>|</li>
					<li><a href="http://turborep.com/index.html" target="_blank">Contact Us</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div id="login_wrapper">
	<div class="login_clear"></div>
	<div class="login_panel">
		<form action="./usercontroller" id="loginForm" method="post">
			<table cellpadding="0" cellspacing="0" border="0" class="loginTableForm">
				<tr>
					<td colspan="2">
						
					</td>
				</tr>
				<tr>
					<td colspan="2"><h3>Username:</h3></td>
				</tr>
				<tr>
					<td colspan="2"><input type="text" id="uname" name="userName" class="validate[required] login_field" value="" placeholder="Your Login Name"/></td>
				</tr>
				<tr>
					<td colspan="2"><h3>Password:</h3></td>
				</tr>
				<tr>
					<td colspan="2"><input type="password" id="pwd" name="password" class="validate[required] login_field" value="" placeholder="Your Password"/></td>
				</tr>
				<tr>
					<td><a href="#" onclick="resetpasswordfun()">Forgot Password?</a></td>
					<td><input type="submit" value="Login"  class="login_submit"/></td>
				</tr>
			</table>
		</form>
	</div>
			<div id="errormsg" name="errormsg"  style="padding-left:400px;overflow:hidden; padding-top:15px; color: red; display: none;font-size:14px;font-weight:bold;font-family:Myriad Pro, Arial, Helvetica, Tahoma, sans-serif; text-decoration:none;display:none">Invalid Username or Password.</div>
	
</div>
	<div id="footer">
		<table cellpadding="0" cellspacing="0" border="0" class="footerTable">
		<tr>
			<td class="copyRightFooter">
				<div class="foot_left">
					<h3>&copy;&nbsp;Copyright 2014 - A&E Specialties, Inc.</h3>
					<p>Main Address: 2146 Roswell Road, Suite 108-309, Marietta, GA 30062.</p>
				</div>
			</td>
			<td>
				<div class="foot_right">
					<img src="./../resources/styles/welcomeCSS/welcomeImages/logofooter.png"/>
				</div>
			</td>
		</tr>
		</table>
	</div>
	
</div>

 <div id="resetPasswordDialogBox" style="display: none;">
	<form action="" id="resetPasswordFormId">
			<table>
								<tr>			
									<td><label>Email ID</label><input type="text" id="userMailID" name="userMailID"  style="width:180px;"></td>
									<td><img alt="Enter your email Id registered with turbopro !" data-toggle="tooltip" data-placement="bottom" title="Enter your email Id registered with turbopro !" id="mailQuestionImage" src="./../resources/Icons/question-icon.png"/></td>
									
								</tr>
								<tr>
									<td colspan="2">
											<div style="color:green;width:200px;" id="emailValidMessage"></div>
									</td>
								</tr>
							</table>
							<br>
			<table>
				<tr>
				
					<td><span style="color:red;" id ="errorStatus"></span></td>
				</tr>
			</table>
	</form>
	
</div>
<div id="versionchangesDialogBox" style="display: none;">
<form  id="versionchangesDialogId">
<table><tr><td>
<span><b style="color:red;">Database is not compatible to this app version.</b></span>
</td></tr></table>
</form>
</div>
<div class="loadingDivpopup" id="loadingDivForpopup" style="display: none;opacity: 0.7;background-color: #fff;z-index: 1234;text-align: center;">
<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/login.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bootstrap-tooltip.js"></script>
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>
</body>
</html>