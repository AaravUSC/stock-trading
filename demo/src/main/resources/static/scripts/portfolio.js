/**
 * 
 */ 

function test() {
	var userID = localStorage.getItem("userID");
	$.ajax({
		 url: "portfolio",
		 type: "GET",
		 data: {
		 	userID:userID
		 },
		 success: function( result ) {
			 var res = JSON.parse(result);
			 var balance = res.bal;
			 var accval = res.accval;
			 var html_string = "";
			 for(var i = 0; i<res.ptitems.length; i++){
				 var item = res.ptitems[i];
				 var up = "green";
				 var sym = "&#x25B2";
				 var mul = 1;
				 if(res.ptitems[i].change<0){
					 up = "red";
					 sym = "&#x25BC";
					 mul = -1;
				 }
				 html_string = html_string + "<article class=\"stock\" style=\"margin-bottom:20px;\"><div style=\"background-color:rgb(244, 240, 240); text-align:left; padding:10px 20px; border:1px solid gray;\">"+item.ticker+" - <span id=\"" + item.ticker+"name"+"\">" + item.name+"</span></div><div class=\"stock-info\" style=\"display:flex; margin-top:0; border:1px solid gray; height:150px;\"><div class=\"stock-info-1\" style=\"width:50%; text-align:left; padding-left:50px;\"><p>Quantity: "+String(item.qty)+"</p><p>Avg. Cost / Share: "+String(item.avg)+"</p><p>Total Cost: "+String(item.total)+"</p></div><div class=\"stock-info-1\" style=\"text-align:left; padding-right:50px; margin-left:50px;\"><p style=\"color:"+up+"\">Change: "+sym+" "+String(mul*item.change)+"</p><p>Current Price: "+String(item.curr)+"</p><p>Market Value: "+String(item.mkt)+"</p></div></div><div class=\"actions\" style=\"background-color:rgb(244, 240, 240); border:1px solid gray; margin-top:0;\"><p style=\"text-align:center;\"><label for=\"coin-qty\">Quantity:</label><input type=\"number\" id=\""+(item.ticker)+"inp"+"\" style=\"width:30px; height:30px;\"></p><p style=\"text-align:center;\"><label><input type=\"radio\" name=\"coin-action\" value=\"buy\" id=\""+(item.ticker+"buy")+"\"> BUY</label><label><input type=\"radio\" name=\"coin-action\" value=\"sell\" id=\""+(item.ticker+"sell")+"\"> SELL</label></p><p style=\"text-align:center;\"><button onclick=\"trade(this)\" id=\""+item.ticker+"\" style=\"width:150px; border:2px solid black; padding:5px;\">Submit</button></p></div></article>";
				 
			 }
			 $(".stocks").html(html_string);
			 $(".bal").html(balance);
			 $(".accval").html(accval);
		}
	});
}



function trade(element){
	var ticker = element.id;
	
	if($("#"+ticker+"inp").val()<1){
		alert("Please enter a positive number of stocks");
	}
	else{
		if(document.getElementById(ticker+"buy").checked){
			$.ajax({
			url: "makeTrade",
			type: "GET",
			data: {
			 	userID:localStorage.getItem("userID"),
			 	ticker:ticker,
			 	quantity:$("#"+ticker+"inp").val(),
			 	name:$("#"+ticker+"name").val(),

			 	
			},
			success: function( result ) {
				var res = JSON.parse(result);
				if(res.success){
					alert("Bought "+String(res.quantity)+" shares of " + ticker + " for $" + String(res.quantity*res.price));
					location.reload();
				}
				else{
					alert("FAILED: Could not complete trade");
				}
				 
			}
			});
		}
		else if(document.getElementById(ticker+"sell").checked){
			$.ajax({
			 url: "makeTrade",
			 type: "GET",
			 data: {
			 	userID:localStorage.getItem("userID"),
			 	ticker:ticker,
			 	quantity:(-1*$("#"+ticker+"inp").val()),
				name:$("#"+ticker+"name").val(),
			 	
			 },
			 success: function( result ) {
				 var res = JSON.parse(result);
				 if(res.success){
					 alert("Sold "+String(res.quantity)+" shares of " + ticker + " for $" + String(res.quantity*res.price));
					 location.reload();
				 }
				 else{
					 alert("FAILED: Could not complete trade");
				 }
				 
			}
			});
		}
		else{
			alert("Please Select an option (buy or sell)");
		}
	}
	
}