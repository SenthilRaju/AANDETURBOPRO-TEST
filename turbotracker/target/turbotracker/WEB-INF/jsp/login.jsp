<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
</head>
<body onLoad="document.getElementById('uname').focus();">
	<div id="welcomepage">
	<div id="header">
		<div class="logo">
			<img src="./../resources/styles/welcomeCSS/welcomeImages/turbopro_logo_new.png" />
			<label class="versionField">Version 2.0.3</label>
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
					<td colspan="2"><input type="text" id="uname" name="userName" class="validate[required] login_field" placeholder="Your Login Name"/></td>
				</tr>
				<tr>
					<td colspan="2"><h3>Password:</h3></td>
				</tr>
				<tr>
					<td colspan="2"><input type="password" id="pwd" name="password" class="validate[required] login_field" placeholder="Your Password"/></td>
				</tr>
				<tr>
					<td><a href="#">Forgot Password?</a></td>
					<td><input type="submit" value="Login" onclick="" class="login_submit"/></td>
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

<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/login.js"></script>
</body>
</html>