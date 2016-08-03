/**
 * Created by: Leo    Date:Oct 12 2015
 * Purpose : ship to address
 */

var globalshiptoid;
$(document).ready(function() {
	
	$("#shipToRadioButtonSet").buttonset();
	GLB_joMasterID=$('#joMasterHiddenID').val();
	if(GLB_joMasterID==undefined||GLB_joMasterID==null){
		GLB_joMasterID="";
	}
});



//Us - Shipto
function shiptoaddressforUSbutton()
{
console.log("US");	
$(toggledivflag).contents().find("#frandbw").css('display','inline');
globalshiptoid = $(toggledivflag).contents().find("#shiptohiddenid").val();
var warehouseID = -1;

preloadShiptoAddress(toggledivflag,'','','0','0',$("#jobCustomerName_ID").text());

$(toggledivflag).contents().find("#shiptomoderhiddenid").val("0");

var imgUrlDisBackwards = "./../resources/images/Arrowleft.png";
$('#backWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
$('#backWard').css('background-position','center');

var imgUrlforwards = "./../resources/images/Arrowright.png";
$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
$('#forWard').css('background-position','center');

}

//Customer - Shipto
function shiptoaddressforCustomerbutton()
{
console.log("Customer");	
$(toggledivflag).contents().find("#frandbw").css('display','inline');
clearshiptoAddress();
preloadShiptoAddress(toggledivflag,'','','1','0',$("#jobCustomerName_ID").text());
//console.log($("#shipToRadioButtonSet input[type='radio']:checked").val())
$(toggledivflag).contents().find("#shiptomoderhiddenid").val("1");

var imgUrlDisBackwards = "./../resources/images/Arrowleft.png";
$('#backWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
$('#backWard').css('background-position','center');

var imgUrlforwards = "./../resources/images/Arrowright.png";
$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
$('#forWard').css('background-position','center');
}

//Jobsite - Shipto
function shiptoaddressforJobsitebutton()
{
console.log("Jobsite");	
$(toggledivflag).contents().find("#frandbw").css('display','none');
clearshiptoAddress();

if(toggledivflag=="#CI_Shipto"){
	preloadShiptoAddress(toggledivflag,'',GLB_joMasterID,'2','0',$("#customerInvoice_customerInvoiceID").text());
}
if(toggledivflag=="#SO_Shipto"){
	preloadShiptoAddress(toggledivflag,'',GLB_joMasterID,'2','0',$("#jobCustomerName_ID").text());
}
else{
	preloadShiptoAddress(toggledivflag,'','','2','0',$("#jobCustomerName_ID").text());
}
	


$(toggledivflag).contents().find("#shiptomoderhiddenid").val("2");

var imgUrlDisBackwards = "./../resources/images/Arrowleft.png";
$('#backWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
$('#backWard').css('background-position','center');

var imgUrlforwards = "./../resources/images/Arrowright.png";
$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
$('#forWard').css('background-position','center');

}

//Other - Shipto
function shiptoaddressforOtherbutton()
{
console.log("Other");	
$(toggledivflag).contents().find("#frandbw").css('display','none');
clearshiptoAddress();
preloadShiptoAddress(toggledivflag,'','','3','0',$("#jobCustomerName_ID").text());
$(toggledivflag).contents().find("#shiptomoderhiddenid").val("3");

var imgUrlDisBackwards = "./../resources/images/Arrowleft.png";
$('#backWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
$('#backWard').css('background-position','center');

var imgUrlforwards = "./../resources/images/Arrowright.png";
$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
$('#forWard').css('background-position','center');

}



function clearshiptoAddress()
{
	$(toggledivflag).contents().find("#shipToName").val(""); 
	$(toggledivflag).contents().find("#shipToAddress1").val(""); 
	$(toggledivflag).contents().find("#shipToAddress2").val(""); 
	$(toggledivflag).contents().find("#shipToCity").val(""); 
	$(toggledivflag).contents().find("#shipToState").val("");
	$(toggledivflag).contents().find("#shipToZip").val("");
	$(toggledivflag).contents().find("#shiptoaddrhiddenfromuiid").val("");
}

function loadshiptostateautocmpte(divflag)
{
	 var cache = {}; var lastXhr=''; $(divflag).contents().find( "#shipToCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $(divflag).contents().find("#shipToState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); 
}



function loadCustomerAddress(CustomerID){
	
	$.ajax({
		url: "./jobtabs5/RxaddressBasedOnCustomerID",
		type: "POST",
		async:false,
		data :{"cuMasterID":CustomerID},
		success: function(data) {
			rxAddressarr=new Array();
			for(var i=0;i<data.length;i++){
				rxAddressarr.push(data[i]);
			}
			rxAddressesize=data.length;
			//alert(rxAddressesize)
		}
		});
}