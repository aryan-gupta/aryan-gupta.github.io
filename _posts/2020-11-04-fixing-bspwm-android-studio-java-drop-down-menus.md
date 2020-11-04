---
title: Fixing BSPWM Java Drop Down Centered Menus in Android Studio
tags:
  - bspwm
  - intellij
  - ide
  - android studio
  - java
---

For a uni assignment, we have to create an Android App from scratch. One of the largest annoyances of using Android Studio on BSPWM is the fact that all the drop down menus are centered on the screen. 

<iframe width="1892" height="1600" src="https://www.youtube.com/embed/Y-hmHUwO0C0?autoplay=1&showinfo=0&controls=1&loop=1&modestbranding=1" frameborder="0" allow="accelerometer; autoplay; clipboard-write; gyroscope; picture-in-picture" allowfullscreen></iframe><br/>


The [Arch Linux wiki](https://wiki.archlinux.org/index.php/bspwm#Problems_with_Java_applications) had the resolution to this issue to run this command in your `bspwmrc` file, however this did not fix the issue for me. 

``` shell
wmname LG3D &
```

Another idea was to set an Java env variable before running Android Studio. This also did not work for me. 

``` shell
_JAVA_AWT_WM_NONREPARENTING=1
```


After some more research on Google, I found [this](https://www.reddit.com/r/bspwm/comments/87j7gf/floating_dropdowns_with_intellij_and_bspwm/) Reddit comment from [u/CodyBonney](https://www.reddit.com/user/CodyBonney/). The solution they had was to create an explicit rule to unmanage the child windows of Java.

<a class="embedly-card" data-embed-live="false" href="https://www.reddit.com/r/bspwm/comments/87j7gf/floating_dropdowns_with_intellij_and_bspwm/dwvz0v4">Card</a>
<script async src="//embed.redditmedia.com/widgets/platform.js" charset="UTF-8"></script>

Post is copied here:
> Alright, I think I have a workaround!
>
> First, I installed [xtitle](https://www.cs.indiana.edu/~kinzler/xtitle/)
>
> Then, I created a new [external_rules](https://gist.github.com/codybonney/3fcbbc450f6ce1e22b2851f973c7537a) file:
> ``` shell
> #! /bin/sh
> 
> wid=$1
> class=$2
> 
> # fix context menus in IntelliJ IDEA 2018.1
> [[ "$class" = "jetbrains-idea" ]] && [[ $(xtitle "$wid") =~ > ^win[0-9]*$ ]] && echo "manage=off";
> ```
> Then, I made it executable: `sudo chmod +x external_rules`
>
> Then, I added the following to my `bspwmrc` file:
>
> `bspc config external_rules_command "/home/cody/.config/bspwm/external_rules"`

I had to change the `"jetbrains-idea"` in the script to `"jetbrains-studio"` because the window class was different since I was using Android Studio and not IntelliJ.

``` shell
#! /bin/sh

wid=$1
class=$2

[[ "$class" = "jetbrains-studio" ]] && [[ $(xtitle "$wid") =~ ^win[0-9]*$ ]] && echo "manage=off";
```