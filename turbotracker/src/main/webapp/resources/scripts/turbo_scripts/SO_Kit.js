customerInvoice_subTotalID;
jQuery(document).ready(function() {
PreloadData();
});
function PreloadData(){
	var cuSOID = $('#Cuso_ID').text();
	var rxMasterID = $('#rxCustomer_ID').text();
	if(cuSOID!=null && typeof(cuSOID)!='undefined'){
	$.ajax({
		url: "./salesOrderController/getPreLoadData",
		type: "POST",
		data : "&cuSOID="+cuSOID+"&rxMasterID="+rxMasterID,
		success: function(data) {
			$("#SO_numberKit").val(data.Cuso.sonumber);
			$("#CustomerNameKit").val(data.CustomerName);
			$('#dateOfSoKit').val($("#poDate_ID").text());
			$('#customerInvoice_subTotalID').val(formatCurrency(data.Cusodetail.taxTotal));
			$('#customerInvoice_totalID').val(formatCurrency(data.Cusodetail.taxTotal));
		}
	});
	}
	}