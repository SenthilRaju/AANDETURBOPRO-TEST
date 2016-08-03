<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Print Checks</title>
<style type="text/css">
table,th
{
border:1px solid grey;
}
th{
background-color:#C5C5C5; 
}
</style>
</head>
<body>
<input type="text" id="rxMasterID" style="display: none;" />
<div id="printBilldiv">
<table id="billListTable">
	<tr style="border: solid grey 1px;">
		<th>PO#</th>
		<th>Invoice Date</th>
		<th>Invoice Number</th>
		<th>Invoice Amount</th>
		<th>Payment Amount</th>
		<th>Discount Amount</th>
		<th style="border: none;">Invoice Balance</th>
	</tr>
</table>
<br>
<table>
	<tr>
		<td style="width: 306px;border: none;" >
			<b>Paying <span id="noOfBills"></span> Items, Totals:</b>	 
		</td>
		<td>
			<table>
				<tr>
					<td style="border-right: solid grey 1px;width: 142px;" ><span id="invoiceTotal">$</span></td>
					<td style="border-right: solid grey 1px;width: 155px;"><span id="paymentTotal">$</span></td>
					<td style="border-right: solid grey 1px;width: 160px;"><span id="discountTotal">$</span></td>
					<td style="width: 136px;">$<span id="invoiceBalance"></span></td>
				</tr>
			</table>
		</td>
</table>
<br><br><br><br><br><br><br><br>

<table style="border: none;">
	<tr>
	<td style="border: none;width: 650px;">
			<label ><span id="totalInWords"></span></label>
		</td>
		<td style="border: none;margin-left: 30px;">
			<label id="currentDate" style="margin-right: 75	px;"></label>&nbsp;
		</td>
		<td style="float: right;">
			&nbsp;<label id="TotalAmount">******$</label>
			<input id="hiddenInput" style="display: none;" onclick="numinwrd()">
		</td>
	</tr>
</table>
</div>
<hr>
<input style="margin-left: 880px;" type="button" onclick="printBill()" value="Print"/>
<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
﻿// Create a jquery plugin that prints the given element.
jQuery.fn.print = function() {
    // NOTE: We are trimming the jQuery collection down to the
    // first element in the collection.
    if (this.size() > 1) {
        this.eq(0).print();
        return;
    } else if (!this.size()) {
        return;
    }

    // ASSERT: At this point, we know that the current jQuery
    // collection (as defined by THIS), contains only one
    // printable element.

    // Create a random name for the print frame.
    var strFrameName = ("printer-" + (new Date()).getTime());

    // Create an iFrame with the new name.
    var jFrame = $("<iframe name='" + strFrameName + "'>");

    // Hide the frame (sort of) and attach to the body.
    jFrame
    .css("width", "1px")
    .css("height", "1px")
    .css("position", "absolute")
    .css("left", "-9999px")
    .appendTo($("body:first"))
    ;

    // Get a FRAMES reference to the new frame.
    var objFrame = window.frames[strFrameName];

    // Get a reference to the DOM in the new frame.
    var objDoc = objFrame.document;

    // Grab all the style tags and copy to the new
    // document so that we capture look and feel of
    // the current document.

    // Create a temp document DIV to hold the style tags.
    // This is the only way I could find to get the style
    // tags into IE.
    var jStyleDiv = $("<div>").append(
    $("style").clone()
    );

    // Write the HTML for the document. In this, we will
    // write out the HTML of the current element.
    objDoc.open();
    objDoc.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    objDoc.write("<html>");
    objDoc.write("<body>");
    objDoc.write("<head>");
    objDoc.write("<title>");
    objDoc.write(document.title);
    objDoc.write("</title>");
    objDoc.write(jStyleDiv.html());
    objDoc.write("</head>");
    objDoc.write(this.html());
    objDoc.write("</body>");
    objDoc.write("</html>");
    objDoc.close();

    // Print the document.
    objFrame.focus();
    objFrame.print();

    // Have the frame remove itself in about a minute so that
    // we don't build up too many of these frames.
    setTimeout(
    function() {
        jFrame.remove();
    },
    (60 * 1000)
    );
}</script>
<script>
jQuery(document).ready(function(){
	billsToPrint();
});
function billsToPrint(){
	var invoiceTotal = 0;
	var paymentTotal = 0;
	var discTotal = 0;
	var invoiceBal = 0;
	
	$.ajax({
		url:'./banking/printCheckDetails',
		type:'GET',
		data:{},
			success:function(data){
				$('#noOfBills').text(data.length);
				$('#currentDate').text(data[0].transactionDate);
				for(var j =0; j<data.length;j++){
				var datacol = data[j];
				$('#rxMasterID').val(datacol.rxMasterID);
				invoiceTotal = invoiceTotal + datacol.amount;
				paymentTotal = paymentTotal +  datacol.amountPaid;
				discTotal = discTotal +  datacol.discount;
				invoiceBal = invoiceBal +  (datacol.billAmount-datacol.amountPaid);
				var nextRow = '<tr><td style="border-right: solid grey 1px;">'+datacol.ponumber+'</td>'
								  +'<td style="border-right: solid grey 1px;">'+datacol.transactionDate+'</td>'
								  +'<td style="border-right: solid grey 1px;">'+datacol.invoiceNo+'</td>'
								  +'<td style="border-right: solid grey 1px;">$'+datacol.billAmount+'</td>'
								  +'<td style="border-right: solid grey 1px;">$'+datacol.amountPaid+'</td>'
								  +'<td style="border-right: solid grey 1px;">$'+datacol.discount+'</td>'
								  +'<td>$'+(datacol.billAmount-datacol.amountPaid)+'</td>';
				$('#billListTable').append(nextRow);	
				}
				$('#invoiceTotal').append(invoiceTotal);
				$('#paymentTotal').append(paymentTotal);
				$('#discountTotal').append(discTotal);
				$('#invoiceBalance').append(invoiceBal);
				$('#TotalAmount').append(Math.round(paymentTotal * 100) / 100);
				$('#hiddenInput').append(Math.round(paymentTotal * 100) / 100);
				$('#hiddenInput').trigger('click');
			},
			error:function(data){
				console.log("Failed");
				console.log(data);
			}
		});
}
function printBill(){
$('#printBilldiv').print();
}
</script>
<script type="text/javascript">

var nume=document.getElementById('hiddenInput').value;

function numinwrd()
  {
     var numbr=document.getElementById('hiddenInput').value;
     var str=new String(numbr);  
     var splt=str.split("");
     var rev=splt.reverse();
     var once=['Zero', ' One', 'Two', 'Three', 'Four',  'Five', 'Six', 'Seven', 'Eight', 'Nine'];
     var twos=['Ten', ' Eleven', ' Twelve', ' Thirteen', ' Fourteen', ' Fifteen', ' Sixteen', ' Seventeen', ' Eighteen', ' Nineteen'];
     var tens=[ '', 'Ten', ' Twenty', ' Thirty', ' Forty', ' Fifty', ' Sixty', ' Seventy', ' Eighty', ' Ninety' ];
     numlen=rev.length;
     var word=new Array();
     var i=0;
      var j=0;   
     for(i=0;i<numlen;i++)
       {
          switch(i)
           {
            case 0:
                  if((rev[i]==0) || (rev[i+1]==1))
                   {
                      word[j]='';                    
                   }
                   else
                   {
                     word[j]=once[rev[i]];
                    }
                   word[j]=word[j] ;
                   
                   break;
            case 1:
                abovetens();  
                   break;
              case 2:
                if(rev[i]==0)
                {
                  word[j]='';
                } 
               else if((rev[i-1]==0) || (rev[i-2]==0) )
                {
                   word[j]=once[rev[i]]+"Hundred ";                
                }
                else 
                {
                    word[j]=once[rev[i]]+"Hundred and";
                } 
               break;
             case 3:
                    if(rev[i]==0 || rev[i+1]==1)
                   {
                      word[j]='';                    
                   } 
                   else
                   {
                     word[j]=once[rev[i]];
                   }
                if((rev[i+1]!=0) || (rev[i] > 0))
                {
	                 word[j]= word[j]+" Thousand";
	              }
                  break;  
             case 4:
                  abovetens(); 
                    break;  
           
              case 5:
                   if((rev[i]==0) || (rev[i+1]==1))
                   {
                      word[j]='';                    
                   } 
                   else
                   {
                     word[j]=once[rev[i]];
                   }
                word[j]=word[j]+"Lakhs";
                  break;  
          
           case 6:
                  abovetens(); 
                    break;
         
          case 7:
                   if((rev[i]==0) || (rev[i+1]==1))
                   {
                      word[j]='';                    
                   } 
                   else
                   {
                     word[j]=once[rev[i]];
                   }
              word[j]= word[j]+"Crore";
                    break;  
          
           case 8:
                  abovetens(); 
                    break;    
                 default:
	               break;
              }
       
          j++;  
       
       }   
  
 function abovetens()
{
     if(rev[i]==0)
        {
            word[j]='';
        } 
    else if(rev[i]==1)
        {
           word[j]=twos[rev[i-1]];
        }
       else
         {
             word[j]=tens[rev[i]];
         }
}

word.reverse();
var finalw='';
for(i=0;i<numlen;i++)
{

  finalw= finalw+word[i];

}
document.getElementById('totalInWords').innerHTML=finalw;
}
function ctck()
{
     var sds = document.getElementById("dum");
     if(sds == null){
        alert("You are using a free package.\n You are not allowed to remove the tag.\n");
     }
     var sdss = document.getElementById("dumdiv");
     if(sdss == null){
         alert("You are using a free package.\n You are not allowed to remove the tag.\n");
     }
}
document.onload ="ctck()";

</script>	
</body>
</html>