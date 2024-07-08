function registerUser() {
	var username = $("#rusername").val();
	var email = $("#email").val();
	var password = $("#rpassword").val();
	var confirm = $("#confirm").val();
	console.log(email);
	
	$.ajax({
		 url: "registerUser",
		 
		 data: {
		 	username: username,
		 	email: email,
		 	password: password,
		 	confirm: confirm
		 },
		 success: function( result ) {
			 
			 if(result == -1){
				 alert("email already taken");
			 }
			 else if(result == -2){
				 alert("username already taken");
			 }
			 else if(result == -3){
				 alert("passwords must match");
			 }
			 else{
				 console.log("success");
				 localStorage.setItem("loggedIn", "true")
				 localStorage.setItem("userID", result)
				 window.location.href = "search.html";
			 }
		 	
		 }
	});
}

function loginUser() {
	var username = $("#lusername").val();
	
	var password = $("#lpassword").val();
	
	
	$.ajax({
		 url: "loginUser",
		 data: {
		 	username: username,
		 	password: password,
		 	
		 },
		 success: function( result ) {
			 console.log(result);
			 if(result == -1){
				 alert("Invalid Username");
			 }
			 else if(result == -2){
				 alert.html("Invalid Password");
			 }
			 else if(result == -3){
				 alert.html("Username or Password field cannot be blank");
			 }
			 else{
				 localStorage.setItem("loggedIn", "true")
				 localStorage.setItem("userID", result)
				 window.location.href = "search.html";
			 }
		 	
		 }
	});
}


/**
 * 
 */