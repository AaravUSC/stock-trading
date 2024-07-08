$(document).ready(function() {

	if (localStorage.getItem("loggedIn") !== null) {
  		$("#pnav").html("Portfolio");
  		
  		$("#logout").html("Logout");
	}
	else{
		$("#login").html("Login / Sign Up");
	}
});

function logout(){
	localStorage.removeItem("loggedIn");
	localStorage.removeItem("userID");
	
}

function loadStockInfo() {
	var tick = $("#search").val();
	console.log("testing...");
	
	$.ajax({
		 url: "fetch",
		 type: "GET",
		 data: {
		 	ticker:tick
		 },
		 success: function( result ) {
			 var res = JSON.parse(result);
			 
			 if(res.c == 0){
				 alert("Invalid stock ticker");
			 }
			 
			 else{
				var price = res.c;
				var change = res.d;
				var pchange = res.dp;
				var high = res.h;
				var low = res.l;
				var open = res.o;
				var pc = res.pc;
				var ipo = res.ipo;
				var mcap = res.marketCapitalization;
				var so = res.shareOutstanding;
				var web = res.weburl;
				var phone = res.phone;
				var ticker = res.ticker;
				var name = res.name;
				var ex = res.exchange;
				var curr_ts = new Date();
				var hrs = curr_ts.getHours;
				var mkt = true;
				if(hrs<9 || hrs>17){
					mkt = false;
				}
				var Mktopn = "Market Open";
				
				if(change > 0){
					$(".price").html(price);
					$(".price").css("color","green");
					$(".change").html("&#x25B2 "+String(change) + "("+pchange+"%)");
					$(".change").css("color","green");
					$(".curr_ts").css("color", "green");
					
				}
				else{
					$(".price").html(price);
					$(".price").css("color","red");
					$(".change").html("&#x25BC "+ String(-1*change) + "("+pchange+"%)");
					$(".change").css("color","red");
					$(".curr_ts").css("color", "red");
					
				}
				var date = new Date(); 
				var dt = (date.getMonth()+1)  + "-" + date.getDate() + "-"+ date.getFullYear() + " " + date.getHours() + ":"  + date.getMinutes() + ":" + date.getSeconds();
				
				$(".name").html(name);
				$(".ticker").html(ticker);
				$(".exchange").html(ex);
				$(".curr_ts").html(dt);
				$(".high").html("<strong> High Price: "+String(high)+"</strong>");
				$(".low").html("<strong> Low Price: "+String(low)+"</strong>");
				$(".open").html("<strong> Open Price: "+String(open)+"</strong>");
				$(".close").html("<strong> Close Price: "+String(pc)+"</strong>");
				$(".IPO").html("<strong>IPO Date:</strong>" + ipo);
				$(".mcap").html("<strong>Market Cap (SM): </strong>" + mcap);
				$(".phone").html("<strong>Phone: </strong>" + phone);
				$(".so").html("<strong>Share Outstanding</strong>: " + so);
				$(".web").html("<strong> Website: </strong> <a href="+web+">"+web+"</a>");
				$(".buy").css("display", "block");
				$(".price-info").css("display", "block");
				$(".market-details").css("display", "block");
				$(".company-info").css("display", "block");
				$(".search").css("display", "none");
				
				if(localStorage.getItem("loggedIn")===null){
					$(".price-info").css("display", "none");
					$(".buy").css("text-align", "center");
					$(".buy").css("left", "570px");
					$(".market-details").css("top", "200px");
					$(".company-info").css("top", "420");
					
					$(".buyinp").css("display", "none");
					$(".buy-btn").css("display", "none");
					
					$(".high").html("High Price:"+String(high));
					$(".low").html("Low Price:"+String(low));
					$(".open").html("Open Price:"+String(open));
					$(".close").html("Close Price:"+String(pc));
				}
				else{
					$(".mktopen").css("display", "block");
				}
				
				localStorage.setItem("currTick", ticker);
				localStorage.setItem("currName", name);
				
				
				
				
			}
		}
	});
}

function makeTrade(){
	var tick = localStorage.getItem("currTick");
	var name = localStorage.getItem("currName");
	var quantity = $(".qtyinp").val();
	if(quantity < 1){
		alert("Please enter a valid number of stocks");
		return;
	}
	var userID = localStorage.getItem("userID");
	$.ajax({
		 url: "makeTrade",
		 type: "GET",
		 data: {
		 	ticker:tick,
		 	quantity:quantity,
		 	userID:userID,
		 	name:name
		 	
		 },
		 success: function( result ) {
			 var res = JSON.parse(result);
			 if(res.success){
				 alert("Bought "+String(res.quantity)+" shares of " + tick + " for $" + String(res.quantity*res.price));
			 }
			 else{
				alert("FAILED: Purchase could not be completed");	
			}
		}
	})
}
