jQuery(document).ready(function() {
	getUrlVars();
});


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


