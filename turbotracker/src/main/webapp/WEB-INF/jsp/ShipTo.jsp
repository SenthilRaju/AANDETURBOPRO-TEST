<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<div>
			
				<div style="display: none">
				<input type="text" id="shiptoaddrhiddenfromdbid" style="width:50px;">
				<input type="text" id="shiptomodehiddenfromdbid" style="width:50px;">
				<input type="text" id="shiptoaddrhiddenfromuiid" style="width:50px;">
				<input type="text" id="shiptomoderhiddenid" style="width:50px;" >
				<input type="text" id="shiptoindexhiddenid" value="0" style="width:50px;" >
				<input type="text" id="shiptocusindexhiddenid" value="0" style="width:50px;" ></div>
				</label>
				</legend>
					<table  id="shipToAddress">
					<tr><td><input type="text" id="shipToName" name="shipToName" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" onchange="validationMethodSplit();"></td></tr>
	 				<tr><td><input type="text" id="shipToAddress1" name="shipToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" onchange="validationMethodSplit();"></td></tr>
	 				<tr><td><input type="text" id="shipToAddress2" name="shipToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled" onchange="validationMethodSplit();"></td></tr>
						<tr>
							<td>
		 						<input type="text" id="shipToCity" name="shipToCity" style="width: 100px;" disabled="disabled" onchange="validationMethodSplit();">
								<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								
									<input type="text" id="shipToState" name="shipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled" onchange="validationMethodSplit();">
									<label>Zip: </label><input type="text" id="shipToZip" name="shipToZip" style="width: 75px;" disabled="disabled" onchange="validationMethodSplit();">
									
								<span id="frandbw" >
									<input type="button" id="backWard" value="" onclick="shiptoaddBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
									<input type="button" id="forWard" value="" onclick="shiptoaddForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
								</span>
							</td>
						</tr> 
					</table>
					<table>
						<tr align="center">
						<td id="radioSetaddr" style="vertical-align: bottom;padding-left: 20px; height: 10px">
							<div id="shipToRadioButtonSet">
								<input type="radio" id="shiptoaddradio1" name="radio" value="0" onchange="validationMethodSplit();"/><label for="shiptoaddradio1" id="shiptoaddlabel1" onclick="shiptoaddressforUSbutton();validationMethodSplit();" style="width: 63px; margin-right: -6px;" >Us</label>
								<input type="radio" id="shiptoaddradio2" name="radio" value="1" onchange="validationMethodSplit();"/><label for="shiptoaddradio2" id="shiptoaddlabel2" onclick="shiptoaddressforCustomerbutton();validationMethodSplit();" style="margin-right: -6px;" >Customer</label>
								<input type="radio" id="shiptoaddradio3" name="radio" value="2" onchange="validationMethodSplit();"/><label for="shiptoaddradio3" id="shiptoaddlabel3" onclick="shiptoaddressforJobsitebutton();validationMethodSplit();" style="margin-right: -6px;" >Job Site</label>
								<input type="radio" id="shiptoaddradio4" name="radio" value="3" onchange="validationMethodSplit();"/><label for="shiptoaddradio4" id="shiptoaddlabel4" onclick="shiptoaddressforOtherbutton();validationMethodSplit();" >Other</label>
								</div>
							</td>
						</tr>
	 				</table>
			
	</div>
	
	<script type="text/javascript">
	//var GlobalPage_Validation=0;
	var GLB_joMasterID=0;
	var override_taxIDBasedOnCustomer;


function fn_override_taxIDBasedOnCustomer(){
		
		var customerID=$("#JobCustomerId").val();

		if(customerID!=null)
			{
				$.ajax({
					url : "./salesOrderController/getoverride_taxterritory",
					type : "POST",
					async:false,
					data : {"customerID" : customerID},
					success:function(data) {
						if(data!=null && data!="" && data!=undefined){
							override_taxIDBasedOnCustomer=data.coTaxTerritoryId;
						}else{
							override_taxIDBasedOnCustomer=null;
						}
					}
				});
			}
	}
	fn_override_taxIDBasedOnCustomer();
	 /*Brace Starts script file overloading*/
	 /*If Block for script file loading*/

	if(_blockjsfiles)
		{
		_blockjsfiles = false;
		$.getScript("./../resources/scripts/turbo_scripts/shipTo.js");
		

	  var warhousearr = "${requestScope.wareHouse}";
	  var rxAddressarr = "${requestScope.theNewShiptocusaddress}";
	  var prWarehousesize = "${fn:length(wareHouse)}";
	  var rxAddressesize = "${fn:length(theNewShiptocusaddress)}";

	var toggledivflag;
		
	function preloadShiptoAddress(divflag,primaryid,addressid,mode,shipindex,customername,coTaxTerritoryID)
	{
		toggledivflag = divflag;
		//override_taxIDBasedOnCustomer(primaryid)
		/* if(divflag == "SO_Shipto")
			{
			$("#SO_Shipto").contents().find( "#shipToRadioButtonSet #shiptoaddlabel1 span" ).text("Pickup");
			} */
		
	//	alert(divflag+"=="+primaryid+"=="+addressid+"=="+mode+"=="+shipindex+"=="+customername+"=="+rxAddressesize+"=="+prWarehousesize);

		//	alert(_global_override_taxTerritory+"=="+divflag+"=="+override_taxIDBasedOnCustomer)
		
		$(divflag).contents().find("#shipToName").attr("disabled",true); 
		$(divflag).contents().find("#shipToAddress1").attr("disabled",true);  
		$(divflag).contents().find("#shipToAddress2").attr("disabled",true);  
		$(divflag).contents().find("#shipToCity").attr("disabled",true); 
		$(divflag).contents().find("#shipToState").attr("disabled",true);  
		$(divflag).contents().find("#shipToZip").attr("disabled",true); 

		
		if(mode == "0")
		{
			if(addressid!="" && addressid!=null)
			{
					getselectedshiptoaddress("warehouse",addressid,divflag,coTaxTerritoryID);
			}
			else
			{
		    var aWareHouse = "${requestScope.wareHouse}";
			if(aWareHouse !== ''){
				var reminders = []; //create a new array global
			    <c:forEach items="${requestScope.wareHouse}" var="reminder">
			        reminders.push({description: "${reminder.description}", 
				        			address1: "${reminder.address1}", 
							        address2: "${reminder.address2}", 
							        city: "${reminder.city}", 
							        state: "${reminder.state}", 
							    	warehouseid: "${reminder.prWarehouseId}", 
							        zip: "${reminder.zip}",
							        coTaxTerritoryId:"${reminder.coTaxTerritoryId}" 
				        });
			    </c:forEach>

			    if(reminders.length == 1)
				 {
			    	prWarehousetoggleassin(reminders,shipindex,divflag,coTaxTerritoryID)
					 $("#frandbw").css('display','none');
				 }
			    else
			    {
				    if(shipindex == "0")
					{
			    	prWarehousetoggleassin(reminders,shipindex,divflag,coTaxTerritoryID)
					}
				    else
					{
				    prWarehousetoggleassin(reminders,shipindex,divflag,coTaxTerritoryID)
				    }
			    }
			}
			}
			$(divflag).contents().find("#shiptoaddradio1").trigger("click");
		}
		else if(mode == "1")
		{

			if(addressid!="" && addressid!=null)
			{

					getselectedshiptoaddress("customer",addressid,divflag,coTaxTerritoryID);
			}
			else
			{
   			var rxAddress = "${fn:length(theNewShiptocusaddress)}";
   			if(rxAddress>0){
   				var rxAddressarray = [];
   			    <c:forEach items="${requestScope.theNewShiptocusaddress}" var="remindert">
   			 	rxAddressarray.push({
									 name:"${remindert.name}", 
   	   			 					 address1: "${remindert.address1}", 
				        			 address2: "${remindert.address2}", 
				       				 city: "${remindert.city}", 
				       				 state: "${remindert.state}", 
				       				 rxAddressId: "${remindert.rxAddressId}", 
				       				 zip: "${remindert.zip}",
   			 						 coTaxTerritoryId:"${remindert.coTaxTerritoryId}"});
   			    </c:forEach>
   			}else{
   				rxAddressarray=rxAddressarr;
   	   			}
				if(rxAddressarray.length==1)
				{
					 rsAddresstoggleassign(rxAddressarray,shipindex,divflag,coTaxTerritoryID)
				}
				else
				{
					if(shipindex == "0")
					{
					rsAddresstoggleassign(rxAddressarray,shipindex,divflag,coTaxTerritoryID)
					}
					else
					{
					rsAddresstoggleassign(rxAddressarray,shipindex,divflag,coTaxTerritoryID)
					}
				}
			}
			$(divflag).contents().find("#shiptoaddradio2").trigger("click");
		}
		else if(mode == "2")
		{
			$(divflag).contents().find("#frandbw").css('display','none');

			
			
			if(addressid!="" && addressid!=null)
			{
					getselectedshiptoaddress("jobsite",addressid,divflag,coTaxTerritoryID);
			}
			else
			{
				var joblocationname="${fn:replace(fn:replace(requestScope.joMasterDetails.locationName, "\\", "\\\\"), "\"", "\\\"")}";
				var name = customername;
					if(name ==null || name == "" || typeof(name) == 'undefined' )
				name = "${fn:replace(fn:replace(requestScope.joMasterDetails.description, "\\", "\\\\"), "\"", "\\\"")}";

				if(joblocationname!=null && joblocationname!=""){
					name=joblocationname;
					} 
				var address1 = "${fn:replace(fn:replace(requestScope.joMasterDetails.locationAddress1, "\\", "\\\\"), "\"", "\\\"")}";
				var address2 = "${fn:replace(fn:replace(requestScope.joMasterDetails.locationAddress2, "\\", "\\\\"), "\"", "\\\"")}";
				var city = "${fn:replace(fn:replace(requestScope.joMasterDetails.locationCity, "\\", "\\\\"), "\"", "\\\"")}";
				var state ="${fn:replace(fn:replace(requestScope.joMasterDetails.locationState, "\\", "\\\\"), "\"", "\\\"")}";
				var joMasterId = "${requestScope.joMasterDetails.joMasterId}";
				var zip = "${fn:replace(fn:replace(requestScope.joMasterDetails.locationZip, "\\", "\\\\"), "\"", "\\\"")}"; 
				var coTaxTerritoryId = "${requestScope.joMasterDetails.coTaxTerritoryId}";
	
				$(divflag).contents().find("#shipToName").val(name); 
				$(divflag).contents().find("#shipToAddress1").val(address1); 
				$(divflag).contents().find("#shipToAddress2").val(address2); 
				$(divflag).contents().find("#shipToCity").val(city);
				$(divflag).contents().find("#shipToState").val(state);
				$(divflag).contents().find("#shipToZip").val(zip); 
				$(divflag).contents().find("#shiptoaddradio3").trigger("click");
				$(divflag).contents().find("#shiptomoderhiddenid").val("2");
				$(divflag).contents().find("#shiptoaddrhiddenfromuiid").val(joMasterId);
				//loadTaxTerritoryRate_ShipTO(coTaxTerritoryId);
			
			// ID#532 - commented by leo
			//if((divflag == "#SO_Shipto" || divflag ==  "#CI_Shipto")&&_global_override_taxTerritory&& typeof (override_taxIDBasedOnCustomer) != "undefined" && override_taxIDBasedOnCustomer != null)
			
				if((divflag == "#SO_Shipto")&&_global_override_taxTerritory&& typeof (override_taxIDBasedOnCustomer) != "undefined" && override_taxIDBasedOnCustomer != null)
					   loadTaxTerritoryRate_ShipTO(override_taxIDBasedOnCustomer);
				   else				   
				       loadTaxTerritoryRate_ShipTO(coTaxTerritoryId);
			}
			 $(divflag).contents().find("#shiptoaddradio3").trigger("click");
		}
		else
		{
			$(divflag).contents().find("#frandbw").css('display','none');
			$(divflag).contents().find("#shipToName").removeAttr("disabled"); 
			$(divflag).contents().find("#shipToAddress1").removeAttr("disabled"); 
			$(divflag).contents().find("#shipToAddress2").removeAttr("disabled"); 
			$(divflag).contents().find("#shipToCity").removeAttr("disabled"); 
			$(divflag).contents().find("#shipToState").removeAttr("disabled"); 
			$(divflag).contents().find("#shipToZip").removeAttr("disabled"); 
			var coTaxTerritoryId = "${requestScope.joMasterDetails.coTaxTerritoryId}";
			 
			 if(addressid!="" && addressid!=null)
			 {
				 getselectedshiptoaddress("other",addressid,divflag,coTaxTerritoryID);
			 }
			 else if($(divflag).contents().find("#shiptoaddrhiddenfromdbid").val()!="" && $(divflag).contents().find("#shiptoaddrhiddenfromdbid").val()!=null)
			 {
				 if($(divflag).contents().find("#shiptomodehiddenfromdbid").val()==3)
				 {
				 getselectedshiptoaddress("other",$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(),divflag,coTaxTerritoryID);
				 }
				 else
				 {
					 if((divflag == "#SO_Shipto")&&_global_override_taxTerritory&& typeof (override_taxIDBasedOnCustomer) != "undefined" && override_taxIDBasedOnCustomer != null)
						   loadTaxTerritoryRate_ShipTO(override_taxIDBasedOnCustomer);
					 else				   
					       loadTaxTerritoryRate_ShipTO(coTaxTerritoryId);
				 }
			 }

			 $(divflag).contents().find("#shiptoaddradio4").trigger("click");
		}
			    
	}

	function getselectedshiptoaddress(type,addressid,divflag,coTaxTerritoryID)
	{

		$.ajax({
			   url : "./getSelectedShiptoAddress",
			   type : "POST",
			   data : { 'type': type, 'addressid': addressid},
			   success : function (data) {
				   console.log(data.name+"=="+data.address1+"=="+data.address2+"=="+data.city+"=="+data.zip);
				   $(divflag).contents().find("#shipToName").val(data.name); 
				   $(divflag).contents().find("#shipToAddress1").val(data.address1); 
				   $(divflag).contents().find("#shipToAddress2").val(data.address2); 
				   $(divflag).contents().find("#shipToCity").val(data.city); 
				   $(divflag).contents().find("#shipToState").val(data.state); 
				   $(divflag).contents().find("#shipToZip").val(data.zip); 
				   $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val(data.rxAddressId); 
				   if(coTaxTerritoryID!=undefined && coTaxTerritoryID!=""){
					   loadTaxTerritoryRate_ShipTO(coTaxTerritoryID);
				   }else{
					   loadTaxTerritoryRate_ShipTO(data.coTaxTerritoryId);
				   }
					 
				
			   },
			   error : function (msg) {}
			});
		
	}
	
	function prWarehousetoggleassin(reminders,shipindex,divflag,coTaxTerritoryID)
	{
		
		 $(divflag).contents().find("#shipToName").val(reminders[shipindex].description); 
		 $(divflag).contents().find("#shipToAddress1").val(reminders[shipindex].address1); 
		 $(divflag).contents().find("#shipToAddress2").val(reminders[shipindex].address2); 
		 $(divflag).contents().find("#shipToCity").val(reminders[shipindex].city);
		 $(divflag).contents().find("#shipToState").val(reminders[shipindex].state);
		 $(divflag).contents().find("#shipToZip").val(reminders[shipindex].zip);
		 $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val(reminders[shipindex].warehouseid);
		 if(coTaxTerritoryID!=undefined && coTaxTerritoryID!=""){
			 loadTaxTerritoryRate_ShipTO(coTaxTerritoryID);
		   }else{
			   
			   if((divflag == "#SO_Shipto")&& _global_override_taxTerritory && typeof (override_taxIDBasedOnCustomer) != "undefined" && override_taxIDBasedOnCustomer != null)
			   {
				   loadTaxTerritoryRate_ShipTO(override_taxIDBasedOnCustomer);
			   }
			   else				   
			       loadTaxTerritoryRate_ShipTO(reminders[shipindex].coTaxTerritoryId);
		   }
	   
	}
	
	function rsAddresstoggleassign(rxAddressarray,shipindex,divflag,coTaxTerritoryID)
	{
		 $(divflag).contents().find("#shipToName").val(rxAddressarray[shipindex].name); 
		 $(divflag).contents().find("#shipToAddress1").val(rxAddressarray[shipindex].address1); 
		 $(divflag).contents().find("#shipToAddress2").val(rxAddressarray[shipindex].address2); 
		 $(divflag).contents().find("#shipToCity").val(rxAddressarray[shipindex].city);
		 $(divflag).contents().find("#shipToState").val(rxAddressarray[shipindex].state);
		 $(divflag).contents().find("#shipToZip").val(rxAddressarray[shipindex].zip);
		 $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val(rxAddressarray[shipindex].rxAddressId);
		 if(coTaxTerritoryID!=undefined && coTaxTerritoryID!=""){
			 loadTaxTerritoryRate_ShipTO(coTaxTerritoryID);
		   }else{

			   if((divflag == "#SO_Shipto")&&_global_override_taxTerritory&& typeof (override_taxIDBasedOnCustomer) != "undefined" && override_taxIDBasedOnCustomer != null)
				   loadTaxTerritoryRate_ShipTO(override_taxIDBasedOnCustomer);
			   else				   
			       loadTaxTerritoryRate_ShipTO(rxAddressarray[shipindex].coTaxTerritoryId);
		   }
		 
	}
	 
	
	function shiptoaddForWard()
	{
		var frwardvalue =0;
		
		if( $(toggledivflag).contents().find("#shiptomoderhiddenid").val()=="0")
		{
			frwardvalue = $(toggledivflag).contents().find("#shiptoindexhiddenid").val();
			frwardvalue=parseInt(frwardvalue)+1;
			if(frwardvalue>=parseInt(prWarehousesize)-1)
			{
				$(toggledivflag).contents().find("#shiptoindexhiddenid").val(parseInt(prWarehousesize)-1);
				preloadShiptoAddress(toggledivflag,'','','0',parseInt(prWarehousesize)-1,$("#jobCustomerName_ID").text());
				
				var imgUrlDisBackwards = "./../resources/images/DisabledArrowright.png";
				$('#forWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
				$('#forWard').css('background-position','center');
				
				var imgUrlforwards = "./../resources/images/Arrowleft.png";
				$('#backWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#backWard').css('background-position','center');
			}
			else
			{
				$(toggledivflag).contents().find("#shiptoindexhiddenid").val(frwardvalue);
				preloadShiptoAddress(toggledivflag,'','','0',frwardvalue,$("#jobCustomerName_ID").text());
				
				var imgUrlforwards = "./../resources/images/Arrowleft.png";
				$('#backWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#backWard').css('background-position','center');
				
			} 
		}
		else
		{
			frwardvalue = $(toggledivflag).contents().find("#shiptocusindexhiddenid").val();
			frwardvalue=parseInt(frwardvalue)+1;
			if(frwardvalue>=parseInt(rxAddressesize)-1)
			{
				$(toggledivflag).contents().find("#shiptocusindexhiddenid").val(parseInt(rxAddressesize)-1);
				preloadShiptoAddress(toggledivflag,'','','1',parseInt(rxAddressesize)-1,$("#jobCustomerName_ID").text());
				
				var imgUrlDisBackwards = "./../resources/images/DisabledArrowright.png";
				$('#forWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
				$('#forWard').css('background-position','center');
				
				var imgUrlforwards = "./../resources/images/Arrowleft.png";
				$('#backWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#backWard').css('background-position','center');
				validationMethodSplit();
			}
			else
			{
				$(toggledivflag).contents().find("#shiptocusindexhiddenid").val(frwardvalue);
				preloadShiptoAddress(toggledivflag,'','','1',frwardvalue,$("#jobCustomerName_ID").text());
				
				var imgUrlforwards = "./../resources/images/Arrowleft.png";
				$('#backWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#backWard').css('background-position','center');
				
			}
		}
		validationMethodSplit();
	}


	function shiptoaddBackWard()
	{
		var backwardvalue = 0;

		if($(toggledivflag).contents().find("#shiptomoderhiddenid").val()=="0")
		{
			backwardvalue = $(toggledivflag).contents().find("#shiptoindexhiddenid").val();
			backwardvalue=parseInt(backwardvalue)-1;
			if(backwardvalue<=0)
			{
				$(toggledivflag).contents().find("#shiptoindexhiddenid").val(0);
				preloadShiptoAddress(toggledivflag,'','','0','0',$("#jobCustomerName_ID").text());
				
				var imgUrlDisBackwards = "./../resources/images/DisabledArrowleft.png";
				$('#backWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
				$('#backWard').css('background-position','center');
	
				var imgUrlforwards = "./../resources/images/Arrowright.png";
				$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#forWard').css('background-position','center');
				
								
			}
			else
			{
				$(toggledivflag).contents().find("#shiptoindexhiddenid").val(backwardvalue);
				preloadShiptoAddress(toggledivflag,'','','0',backwardvalue,$("#jobCustomerName_ID").text());
				
				var imgUrlforwards = "./../resources/images/Arrowright.png";
				$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#forWard').css('background-position','center');
				
			}
		}
		else
		{
			
			backwardvalue = $(toggledivflag).contents().find("#shiptocusindexhiddenid").val();
			backwardvalue=parseInt(backwardvalue)-1;
			if(backwardvalue<=0)
			{
				$(toggledivflag).contents().find("#shiptocusindexhiddenid").val(0);
				preloadShiptoAddress(toggledivflag,'','','1','0',$("#jobCustomerName_ID").text());
				
				var imgUrlDisBackwards = "./../resources/images/DisabledArrowleft.png";
				$('#backWard').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
				$('#backWard').css('background-position','center');
	
				var imgUrlforwards = "./../resources/images/Arrowright.png";
				$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#forWard').css('background-position','center');
				validationMethodSplit();
								
			}
			else
			{
				$(toggledivflag).contents().find("#shiptocusindexhiddenid").val(backwardvalue);
				preloadShiptoAddress(toggledivflag,'','','1',backwardvalue,$("#jobCustomerName_ID").text());
				
				var imgUrlforwards = "./../resources/images/Arrowright.png";
				$('#forWard').css('background', 'url('+imgUrlforwards+') no-repeat');
				$('#forWard').css('background-position','center');
				
			}
			
		}
		validationMethodSplit();
	}


	function loadTaxTerritoryRate_ShipTO(coTaxTerritoryId)
	{
		if(toggledivflag=="#SO_Shipto"){
			if(_global_override_taxTerritory && coTaxThereorNOt){
				if(_global_override_taxIDBasedOnCustomer!=null && _global_override_taxIDBasedOnCustomer!=0 && _global_override_taxIDBasedOnCustomer!=-1){
						coTaxTerritoryId=_global_override_taxIDBasedOnCustomer;
					}else if(_global_override_taxIDBasedOnCustomer!=null){
						var newDialogDiv2 = jQuery(document.createElement('div'));
						var errorText = "Default taxterritory Not Available";
						jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
						jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Warning", 
							buttons : [ {
												height : 35,
												text : "OK",
												click : function() {
													$(this).dialog("close");
													return false;
										}
									}]}).dialog("open");
					}
				}
			}
		if(coTaxTerritoryId != null && coTaxTerritoryId != '')
		{
			$.ajax({
				url: "./salesOrderController/taxRateTerritory",
				type: "POST",
				data : {"coTaxTerritoryId" : coTaxTerritoryId},
				success: function(data) {
					SetTaxTerritory(toggledivflag,data);
				}
		});
		}else{
			SetTaxTerritory(toggledivflag,null);
			}
	}
	
	
function SetTaxTerritory(toggledivflag,data){
	var cotaxTerritoryID=0;
	var county="";
	var taxRate=0.00;
	var taxfreight=0;
	if(data!=undefined && data!=null && data!=""){
		cotaxTerritoryID=data.coTaxTerritoryId;
		county=data.county;
		taxRate=data.taxRate;
		taxfreight=data.taxfreight;
		}
	if(toggledivflag=="#PO_Shipto")
	{
		//Inside Job
		$("#taxGeneralId").prop("disabled",false);
		$("#taxGeneralId").val(taxRate);
		$("#taxGeneralId").prop("disabled",true);
		$("#taxLineId").prop("disabled",false);
		$("#taxLineId").val(taxRate);
		$("#taxLineId").prop("disabled",true);
		setTaxTotal_PO();
		
	}else if(toggledivflag=="#PO_Shiptooutside"){
		//Outside Job
		$("#taxGeneralId").prop("disabled",false);
		$("#taxGeneralId").val(taxRate);
		$("#taxGeneralId").prop("disabled",true);
		$("#taxLineId").prop("disabled",false);
		$("#taxLineId").val(taxRate);
		$("#taxLineId").prop("disabled",true);
		setTaxTotal_POOutside();
	}
	else if(toggledivflag=="#SO_Shipto")
	{
		//Sales Order inside/Outside
		$('#taxID').val(county);
		$('#taxIDwz').val(county);
		$('#taxhiddenID').val(cotaxTerritoryID);
		$('#taxhiddenIDwz').val(cotaxTerritoryID);
		$("#SOGeneral_taxId").prop("disabled",false);
		$("#SOGeneral_taxId").val(taxRate);
		$("#SOGeneral_taxId").prop("disabled",true);
		$("#so_taxfreight").val(taxfreight);
		setTaxTotal_SO();
	}else if(toggledivflag=="#CI_Shipto")
	{
		//Customer Invoice
		$('#customerInvoice_TaxTerritory').val(county);
		$('#customerTaxTerritory').val(cotaxTerritoryID);
		//$('#customerTaxTerritory').text(taxRate);
		$('#customerInvoice_generaltaxId').val(taxRate);
		$("#CI_taxfreight").val(taxfreight);
		setTaxTotal_CI();
	}
	
}

	function validationMethodSplit(){
		
		if(toggledivflag=="#PO_Shipto"){
			//PurchaseOrder
			if(po_General_tab_form!=undefined){
				POGeneralTabformChanges();
				}
		}else if(toggledivflag=="#PO_Shiptooutside"){
			//PurchaseOrder
			if(po_General_tab_form!=undefined){
			POGeneralTabformChanges();
			}
		}else if(toggledivflag=="#SO_Shipto"){
			if(so_general_form!=undefined && so_openornot!=1){
				soGeneralformChanges();
			}else{
				so_openornot=0;
				}
		}else if(toggledivflag=="#CI_Shipto"){
			//Customer Invoice
			//CIGeneralTabSeriallize();
		}

		}


	}else{
	//	$(toggledivflag).contents().find("#shipToRadioButtonSet").buttonset();
	//	$( "#shipToRadioButtonSet" ).buttonset( "destroy" );
	//	$( "#shipToRadioButtonSet" ).buttonset();
/* 		$("#PO_Shipto").contents().find("#shipToRadioButtonSet").buttonset("destroy");
		$("#PO_Shipto").contents().find("#shipToRadioButtonSet").buttonset();
		$("#SO_Shipto").contents().find("#shipToRadioButtonSet").buttonset("destroy");
		$("#SO_Shipto").contents().find("#shipToRadioButtonSet").buttonset();
		$("#CI_Shipto").contents().find("#shipToRadioButtonSet").buttonset("destroy"); 
		$("#CI_Shipto").contents().find("#shipToRadioButtonSet").buttonset();  */
		} /*Brace ends for script file overloading*/

	
		
	</script>
