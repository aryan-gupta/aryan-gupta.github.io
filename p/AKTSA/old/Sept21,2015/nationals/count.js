var args = location.search.substring(1).replace(/%20/g, " ");
args = args.replace(/%3C/g, "<");
args = args.replace(/%3E/g, ">");


//"09/16/2015 02:30 PM"
//alert(args.substring(19));
document.getElementById("Description").innerHTML = args.substring(19);
var end = new Date(args.substring(0, 19));
    var _second = 1000;
    var _minute = _second * 60;
    var _hour = _minute * 60;
    var _day = _hour * 24;
    var timer;
    var set1 = false;
    var set2 = false;

    function showRemaining() {
        var now = new Date();
        var distance = end - now;
        if (distance < 0) {

            clearInterval(timer);
            document.getElementById('countdown').innerHTML = "Let's Roll";

            return;
        }
        var days = Math.floor(distance / _day);
        var hours = Math.floor((distance % _day) / _hour);
        var minutes = Math.floor((distance % _hour) / _minute);
        var seconds = Math.floor((distance % _minute) / _second);
        
        if(days <= 0 && hours > 0 && set1 == false)
		{
			document.getElementsByClassName('Day')[0].className += ' lose1 ';
            document.getElementsByClassName('Dot3')[0].className += ' lose1 ';
            document.getElementsByClassName('Second')[0].classList.remove("lose1");
            document.getElementsByClassName('Dot1')[0].classList.remove("lose1");
            document.getElementsByClassName('Dot1')[0].className += ' lose2 ';
            document.getElementsByClassName('Second')[0].className += ' lose2 ';
            document.getElementsByClassName('Minute')[0].classList.remove("lose2");
            document.getElementsByClassName('Dot2')[0].classList.remove("lose2");
            set1 = true;
		}
        
        if(days <= 0 && hours <= 0 && set2 == false)
		{
            document.getElementsByClassName('Day')[0].className += ' lose1 ';
            document.getElementsByClassName('Dot3')[0].className += ' lose1 ';
            document.getElementsByClassName('Hour')[0].className += ' lose2 ';
            document.getElementsByClassName('Dot2')[0].className += ' lose2 ';
            document.getElementsByClassName('Second')[0].classList.remove("lose1");
            document.getElementsByClassName('Second')[0].classList.remove("lose2");
            document.getElementsByClassName('Dot1')[0].classList.remove("lose1");
            document.getElementsByClassName('Dot1')[0].classList.remove("lose2");
            document.getElementsByClassName('Minute')[0].classList.remove("lose2");
            set2 = true;
		}
        
		
		if(days < 10)
		{
			days = "0" + days;
		}
        
		if(hours < 10)
		{
			hours = "0" + hours;
		}
        
		if(minutes < 10)
		{
			minutes = "0" + minutes;
		}
		
		if(seconds < 10)
		{
			seconds = "0" + seconds;
		}
        
        document.getElementById('Days').innerHTML = days;
        document.getElementById('Hours').innerHTML = hours;
        document.getElementById('Minutes').innerHTML = minutes;
        document.getElementById('Seconds').innerHTML = seconds;
    }

    timer = setInterval(showRemaining, 1000);