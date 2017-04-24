$(document).ready(function() {
	
	// ["Label" , "website link" , "bar color" , "bar image"]
	var social = [
	 ["email", 		"mailto:me@theguptaempire.net", 								"rgba(0, 0, 0, .75)", "/m/img/email.png"],
	 ["skype", 		"skype:techno_geek_?chat", 										"rgba(0, 0, 0, .75)", "/m/img/skype.png"],
	 ["facebook", 	"https://www.facebook.com/gupta.aryan", 						"rgba(0, 0, 0, .75)", "/m/img/facebook.png"],
	 ["steam", 		"http://steamcommunity.com/profiles/76561198160569556", 		"rgba(0, 0, 0, .75)", "/m/img/steam.png"],
	 ["google+", 	"https://plus.google.com/102946444940955527748", 				"rgba(0, 0, 0, .75)", "/m/img/google_plus.png"],
	 ["github", 	"https://github.com/Kuantum-Freak", 							"rgba(0, 0, 0, .75)", "/m/img/github.png"],
	 ["twitter", 	"https://twitter.com/theguptaemperor/", 						"rgba(0, 0, 0, .75)", "/m/img/twitter.png"],
	 ["youtube", 	"https://www.youtube.com/channel/UCoQMZGWePyOXmHqVrZBBJWg", 	"rgba(0, 0, 0, .75)", "/m/img/youtube.png"],
	 ["linkedin", 	"https://www.linkedin.com/in/aryan-gupta",						"rgba(0, 0, 0, .75)", "/m/img/linkedin.png"],
	 ];
//["sms/mms", 	"sms:704-877-3778", 											"rgba(0, 0, 0, .75)", "/m/img/sms.png"],
//["phone", 		"tel:704-877-3778", 											"rgba(0, 0, 0, .75)", "/m/img/phone.png"],
////////////////////////////////////////////////	
//// DO NOT EDIT ANYTHING BELOW THIS LINE! /////
////////////////////////////////////////////////
		
	$("#socialside").append('<ul class="mainul"></ul>');
	
	/// generating bars
	for(var i=0;i<social.length;i++){
	$(".mainul").append("<li>" + '<ul class="scli" style="background-color:' + social[i][2] + '">' +
						'<li>' + social[i][0] + '<img src="' + social[i][3] + '"/></li></ul></li>');
	 				}
	
	/// bar click event
	$(".scli").click(function(){
		var link = $(this).text();		
		for(var i=0;i<social.length;i++){
			if(social[i][0] == link){
				window.open(social[i][1]);
			}
		}		
	});
	
	/// mouse hover event
	$(".scli").mouseenter(function() {	
		$(this).stop(true);	
		$(this).clearQueue();
			$(this).animate({
				left : "140px"
			}, 300);
				
	});

	/// mouse out event
	$(".scli").mouseleave(function(){
		$(this).animate({
			left:"0px"
		},300);
	});

});
