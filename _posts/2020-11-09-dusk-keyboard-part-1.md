---
title: Dusk Keyboard (part 1)
tags:
  - keyboard
  - mechanical keyboard
  - diy keyboard
  - macropad
  - diy
sidebar:
  title: Parts
  nav: disk-keyboard-part-nav
---

Recently, I have been interested in creating my own keyboard. This would allow me to position various keys in the way I like them and give me a learning opportunity for circuit design, KiCAD, and the like.

The design that really got me hooked was the [Sofle Keyboard](https://josefadamcik.github.io/SofleKeyboard/). I really liked the ergonomics of a split keyboard design, the OLED panel for simple data, and the potentiometer. 

![Sofle Keyboard](https://josefadamcik.github.io/SofleKeyboard/images/sofle_keyboard.jpg)


I saw this design from a [Reddit post](https://www.reddit.com/r/MechanicalKeyboards/comments/j2tv4u/hello_lads_im_back_with_an_update_on_my/) by [u/apprecihate](https://www.reddit.com/user/apprecihate/) and fell in love.

<div style="background-color:white;">
<blockquote class="reddit-card"><a href="https://www.reddit.com/r/MechanicalKeyboards/comments/j2tv4u/hello_lads_im_back_with_an_update_on_my/">Hello lads, I’m back with an update on my Cyberpunk2077 inspired build.</a> from <a href="http://www.reddit.com/r/MechanicalKeyboards">r/MechanicalKeyboards</a></blockquote>
<script async src="//embed.redditmedia.com/widgets/platform.js" charset="UTF-8"></script>
</div>

Since then, I have been doing research on designing a custom keyboard and found some more interesting designs to reference. This design is called the [ErgoDash](https://github.com/omkbd/ErgoDash) and really like the key placement for the `ctrl` and `alt` keys. 

![ErgoDash Keyboard](https://raw.githubusercontent.com/omkbd/picture/master/Ergodash.jpg)

More keyboard designs can be found [here](https://awesomeopensource.com/project/help-14/mechanical-keyboard).

### My design
Even though I really liked these two they each had one thing or another that I did not like. For the Sofle Keyboard, I really disliked that the `ctrl` and `alt` keys were unaccessible from my pinky in the usual location. And with the ErgoDash, I did not like that it did not have the OLED panel and the rotary encoder button. After giving it some thought, I came up with a set of requirements that I would like my new keyboard to have. I also decided to call my new keyboard **Dusk Keyboard** because I wanted to have a purple color scheme (If its not very apparent, purple is my favorite color). Also will name the respective macropad: **Dusk Macropad**. Since a macropad/numpad is simply a Ten-key "keyboard" I will refer to it interchangeably depending on the context. 

#### Dusk Keyboard
 - `ctrl`, `alt` key must be in the pinky cluster
 - No `caps` key. Replace with `esc` key 
 - `win`/`super` key must be be in the thumb cluster
 - Contain rotary dial for analog control
   - Can be reassigned in software to volume, etc
 - 3 layers
 - OLED Panel
 - Must contain all the keys in a TKL (Ten-key less) keyboard
   - Design a separate macropad/numpad
  - Cherry MX keys
    - Must be hot-swappable
  - Must look aesthetic (dusk/purple color scheme)
  - Use [QMK](https://qmk.fm/#/)
    - Too much of a hassle to write custom firmware

And with that, I will be posting follow up posts about the development of this keyboard and macropad. I will be posting a final post once everything is finished and working as a one stop read all guide to the design.

The other parts to this series can be found on the left sidebar