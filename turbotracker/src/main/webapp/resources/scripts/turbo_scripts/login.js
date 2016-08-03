var newDialogDiv = jQuery(document.createElement('div'));
jQuery(document).ready(function() {
	jQuery("#resetPasswordDialogBox").dialog({
		autoOpen:false,
		width:350,
		height:200,
		title:"Password Reset",
		modal:true,
		buttons:{
			"Submit":function(){
				generatePassword();
				}
				},
		close:function(){
			
		return true;}	
	});
	jQuery("#versionchangesDialogBox").dialog({
		autoOpen:false,
		dialogClass: "no-close",
		width:300,
		height:200,
		title:"Warning",
		modal:true,
		buttons:{
			"OK":function(){
				$(this).dialog("close");
				window.location.href="./contactSystemAdministrator";
				}
				}
		
	});
	var value=$("#versionchanges").val();
	if(value=='true'){
		window.location.href="./contactSystemAdministrator";
		//jQuery("#versionchangesDialogBox").dialog("open");
		 return false;
	}
	getUrlVars();
});

document.onmousedown=disableclick;
status="Right Click Disabled";
function disableclick(event)
{
  if(event.button==2)
   {
     alert(status);
     return false;    
   }
}

function parseModelMap() {
	var aMessage = "${requestScope.error}";
	var usermessage = "<label style='color: red'>${requestScope.user-message}</label>"
}

function getUrlVars(){
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++) { 
	    hash = hashes[i].split('=');
	    vars.push(hash[0]);
	    vars[hash[0]] = hash[1];
	}
    if(vars[0]=='error'){
		$("#errormsg").slideDown().delay(2000).hide("clip");
    }
  
    return vars;
}
function resetpasswordfun(){
	$("#resetPasswordDialogBox").dialog("open");
}

function generatePassword(){
	  
			//emailValidMessage
	    var email = $("#userMailID").val();
	    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

	    if (!filter.test(email)) {
	    	 $("#errorStatus").text("Give proper mail id");
	    	setTimeout(function(){
	    		$("#errorStatus").text("");
			}, 1000);
	    return false;
	    }
	    
    $('#loadingDivForpopup').css({"visibility": "visible","z-Index":"1234","display":"block"});
	$.ajax({
		url: "./usercontroller/resetPassword",  
		mType: "GET", 
		data : { 'mailid' : email },
		success: function(data){ 
			 $('#loadingDivForpopup').css({
					"visibility" : "hidden","display": "none"
				});
			 if(data){
				 $("#emailValidMessage").text("Password sent successfully");
				 setTimeout(function(){
			    		$("#emailValidMessage").text("");
					}, 1000);
				 $("#resetPasswordDialogBox").dialog("close");
				 
				 jQuery(newDialogDiv).html('<span><b style="color:green;">Password reset successfully.Check your mail.</b></span>');
				 jQuery(newDialogDiv).dialog({modal: true, width:300, height:200, title:"Info.", 
						buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");
							 }}]}).dialog("open");
				 
			 }else{
				 $("#errorStatus").text("Email Id not match");
				 setTimeout(function(){
			    		$("#errorStatus").text("");
					}, 1000);
			 }
			
			},error:function(e){
				 $('#loadingDivForPO').css({
						"visibility" : "hidden","display": "none"
					}); 
				}
 	});
}
