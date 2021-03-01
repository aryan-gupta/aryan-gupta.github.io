---
title: "About Me"
permalink: /about-me/
layout: single
classes: wide
author_profile: true
---

Hey, My name is Aryan Gupta. This website started back in high school and became popular in my AP Computer Science class since it helped many people since it was their first time programming. Ever since then, the name has stuck and has become part of my brand. The humorous nature of me being the "emperor" sometimes becomes a nice ice breaker and topic of conversation. Enough about the empire, let’s talk about the emperor. 

I have had a passion for computers as far back as I can remember. I've loved everything about computers, from the low-level VLSI system design of ASICs to the high-level Flask web servers written in python. The majority of what I know, I've learned from YouTube, Google, and other like-minded people. I love tinkering with electronics and writing code to help me live a better life.

## Education
I currently hold a Bachelor of Science in Computer Engineering and currently pursuing a Master's degree in Electrical Engineering; however, I plan to switch over to Computer Engineering as soon as the program opens up at my university. I have taken many fun classes at my university and it has taught me intricacies of topics of which I only had surface-level knowledge. The last year of my undergrad and the current classes I'm taking as a graduate has been the most interesting as its been new information that I genuinely want to learn about. 

I typically don't like to go into details about projects I have done on my "about me" page but this project was one of the biggest ones I’ve done and really warrants a paragraph. My Senior Design Project was [NASA's USLI Project](https://www.nasa.gov/stem/studentlaunch/home/index.html). Even though we were unable to launch our rocket, we still won first place for our payload and second place overall. The competition was to design a rocket that housed the payload capable of launching to a predetermined altitude, deploying the payload, and retrieving a sample of simulated lunar ice from various areas around the launch site. Our design was a quadcopter unmanned ariel vehicle that would fold up into the launch vehicle and wait for deployment. The deployment could happen after the launch vehicle has safely parachuted on the ground or during its descent. The quadcopter has dual cameras for stereovision capable of autonomously flying to the location of the sample recovery area with an optional manual override in case of safety issues. The competition also allowed an optional secondary payload. Our secondary payload design was a 360 camera system designed around the launch vehicle that would attempt to locate the sample recovery area during its accent. More details about the project, and in a more jargony way, can be viewed [here](/projects/nasa-usli).

<!--- **Computer Architecture** taught me micro-architecture and logic level design of processors and other computational devices. We even implemented a few branch predictors and designed our own. More details can be found at [this](https://github.com/aryan-gupta/ECGR4181/tree/master/FP) repo. The paper we wrote can be accessed [here](https://github.com/aryan-gupta/ECGR4181/blob/master/FP/report/Final%20Paper.pdf). 

**Intro to Computer Networks** and **Internet of Things** taught me about computer networking. Our final project for Internet of Things was to create a home security system that would monitor various sensors on a Raspberry Pi and relay them to a central python server in the cloud. The python server would update a backend database to keep the values of the sensors and relay back any changes the user wanted back to the Raspberry Pi via a socket. The cloud server also hosted a PHP-based API endpoint that allowed an Android app to pull the sensor data and update the sensors. A use case for this could be a user wanting to decrease the thermostat if they forgot to do so before leaving for work. The Android app would contact the PHP API and update the database with the new thermostat value and signal to the python server to send a notification to the thermostat to update the temperature. The python server and raspberry pi code utilized multi-threading to fully utilized the server and Pi's processing power. The full code of this project can be viewed [here](https://github.com/aryan-gupta/ECGR5090-IoT/tree/main/fp). Note that the Android app was not completed due to time constraints and because of a global health emergency.  -->

## Professional 
I currently work at Mosaic, which is a computing environment for the College of Engineering at UNC Charlotte. We maintain a large number of Windows and Linux machines and servers to serve the need of the college. While completing my undergraduate I applied to Mosaic as a part-time helpdesk worker. I quickly learned the ropes and was able to assist many students, faculty, and staff with their computer needs. I also managed two redundant servers used by the part-timers to store software packages and other tools for our job. These servers were left in a poor shape and were having many issues. I reverse engineered how they were set up and improved them with improved hardware and a more robust system. After a bit more than a year and a half, I was promoted to a Lead TA and put in a management position. Along with another colleague, I managed the other TAs and hired new ones when we were starting to become understaffed.

During my last semester as an undergrad, a full-time position opened up at Mosaic. And was interviewed and hired for that position. This made me extend my graduation by one semester, but considering that they would pay for that last semester and my master's degree, I took the position. I am currently still working at that position as a full-time System Administrator. I offered Tier 3 support for the college and its staff. I was the "go-to guy" for any computer issues or requests that the TAs could not resolve. I also packaged engineering software so the installation could be automatically deployed to out over 4000+ computers. I also offered support with various computing needs. 

## Extracurricular
Much of my knowledge and expertise I have gained because computers are a hobby and passion. Work and classes have more-or-less cemented the knowledge and gave me an output to make money or to invest in myself. I love working on projects in my off-time to keep me entertained and competitive in the industry.

One of my projects you are looking at now is my Website. I created this as a portfolio of all my life's accomplishments and as an outlet to share with the world interesting things I have found. This "empire" started out in high school and because of a website for my AP Computer Science class so I could help them out with their homework and other assignments. The teacher did find out about it and shout out to Ms. Robinson for letting me keep the site live ;). Ever since then the name has grown on me and becomes part of my brand. Occasionally, it becomes a topic of conversation and allows me to make light-hearted jokes about my Indian lineage. 

My other projects can be viewed [here](/projects/)

## Boy Scouts, Outdoors, and Becoming an Eagle Scout
Growing up, Boy Scouts made a large impact on who I am today. It taught me friendship, outdoor skills, life skills, and leadership. I enjoyed the outdoor trips and to this day go on frequently go backpacking and camping alone and with friends. I am proud of my troop and because of that, I am still very active as an adult. 

Philmont was a major part of my life. Our whole troop went on a 10-day hike across the Rocky Mountains. I was the team leader of about 10 people and had to make sure each person had proper nutrition and did their campsite duties properly, even after a 10-mile day. I had a lot of fun climbing Mt. Baldy which is currently the highest elevation I have climbed to. Pictures of my trip will be up later.

My Eagle Scout project was to create a set of 10 benches for the tennis team. The tennis team had a problem where they would have to sit on the ground in between their sets. I designed and lead a few scouts from my troop in building the benches. More details of this project are in my [projects portfolio](/projects/)

## Resume
Click [here](https://github.com/aryan-gupta/Resume/raw/master/Resume.pdf) for a direct pdf download and [here](https://github.com/aryan-gupta/Resume) for the full repository. 
<script>
    window.onload = function() {
        width = document.getElementById("pdf-resume-container").offsetWidth
        new_height = (width * 11 / 8.5) + 1;
        document.getElementById("pdf-resume-container").style.height = new_height + "px";
        console.log(new_height)
    };
</script>
<div style="border:none;width:100%;height:1000px" id="pdf-resume-container">
    <iframe style="border:none;width:100%;height:100%" src="https://render.githubusercontent.com/view/pdf?color_mode=dark&commit=7665e4c252fbd83d6e41c98ee7d94d0d5dceb784&enc_url=68747470733a2f2f7261772e67697468756275736572636f6e74656e742e636f6d2f617279616e2d67757074612f526573756d652f373636356534633235326662643833643665343163393865653764393464306435646365623738342f526573756d652e706466&nwo=aryan-gupta%2FResume&path=Resume.pdf&repository_id=173609328&repository_type=Repository#2a189aef-f0c3-4445-94f7-062c78df871a"></iframe>
</div>