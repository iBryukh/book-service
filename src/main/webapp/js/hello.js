function init() {
	
	// You need to pass the root path when you load your API
	// otherwise calls to execute the API run into a problem
	
	// rootpath will evaulate to either of these, depending on where the app is running:
	// //localhost:8080/_ah/api
	// //your-app-id/_ah/api

	var rootpath = "//" + window.location.host + "/_ah/api";
	
	// Load the helloworldendpoints API
	// If loading completes successfully, call loadCallback function
	gapi.client.load('booksapi', 'v1', mes, rootpath);
}

function mes(){
	var request 	= gapi.client.booksapi.message();
	request.execute(sayHelloCallback);
}



/*
 * When helloworldendpoints API has loaded, this callback is called.
 * 
 * We need to wait until the helloworldendpoints API has loaded to
 * enable the actions for the buttons in index.html,
 * because the buttons call functions in the helloworldendpoints API
 */
function loadCallback () {	
	enableButtons();
}

function enableButtons () {
	var btn;
	{
		btn = document.getElementById("input_greet_generically");
		btn.onclick= function(){mes();};
		btn.value="Click me for a generic greeting";
	}
	{
		btn = document.getElementById("input_greet_by_name");
		btn.onclick=function(){greetByName();};
		btn.value="Click me for a personal greeting";
	}
	{
		btn = document.getElementById("input_greet_by_name_and_period");
		btn.onclick=function(){greetByPeriod();};
		btn.value="Click me for a personal greeting with period";
	}
	{
		btn = document.getElementById("input_greet_by_name_and_period_with_time");
		btn.onclick=function(){greetByPeriodWithTime();};
		btn.value="Click me for a personal greeting with period and current time(nothing useful)";
	}
}

function greetGenerically () {
	var request 	= gapi.client.helloworldendpoints.justHello();
	
	request.execute(sayHelloCallback);
}

function greetByName () {
	var name 		= document.getElementById("name_field1").value;
	var request 	= gapi.client.helloworldendpoints.sayHelloByName({'name': name});
	request.execute(sayHelloCallback);
}

function greetByPeriod(){
	var name 		= document.getElementById("name_field2").value;
	var period 		= document.getElementById("period2").value; 
	var request 	= gapi.client.helloworldendpoints.greetByPeriod({'name': name, 'period':period});
	request.execute(sayHelloCallback);
}

function greetByPeriodWithTime(){
	var name 		= document.getElementById("name_field3").value;
	var period 		= document.getElementById("period3").value;
	var currentdate = new Date(); 
	var time = 	currentdate.getDate() + "/"
                + (currentdate.getMonth()+1)  + "/" 
                + currentdate.getFullYear() + " - "  
                + currentdate.getHours() + ":"  
                + currentdate.getMinutes() + ":" 
                + currentdate.getSeconds();
	var request 	= gapi.client.helloworldendpoints.greetByPeriodWithTime({'name': name, 'period': period, 'time': time});
	request.execute(sayHelloCallback);
}

function sayHelloCallback (response){
	alert(response);	
}