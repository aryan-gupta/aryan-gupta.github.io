---
title: Changing Terminal Colors on the Fly
tags:
  - terminal
  - ricing
  - colors
---

I have been interested in [pywal](https://github.com/dylanaraps/pywal) application for a while now. It it able to do things that I have been meaning to figure out. Recently I have found that it can change all the my open terminal colors on the fly. I have been trying to figure out how and I think I have it. 

This [Reddit post](https://www.reddit.com/r/unixporn/comments/d9ai9k/alacritty_and_pywal/f1gjfz1?utm_source=share&utm_medium=web2x&context=3) states by running the command `cat ~/.cache/wal/sequences &` in an open terminal or running it in an `rc` file will change the colors. Looking into the file, it is just binary unreadable text.

This [Github Issue](https://github.com/alacritty/alacritty/issues/656) gives us a hint on what to do.


> These escape sequences manipulate the open terminal's colorscheme and allow you to change your terminal's colorscheme on the fly. These escape sequences are supported by all of the terminal emulators I've tried with the exception of konsole which ignores them.
> 
> Two of my projects use these escape sequences for their main purpose and they currently don't work in Alacritty since the sequences aren't yet supported.
> 
> https://github.com/dylanaraps/wal
> https://github.com/dylanaraps/pywal
> Issue on my repo: dylanaraps/pywal#37
> 
> I fully understand if you don't want to implement this feature, there are a lot of really weird escape sequences in the Xterm/Rxvt specs and I understand that you want to keep Alacritty simple. ^^
> 
> The sequences are as follows:
> 
> ```shell
> # Manipulate colors 0-256
> # \033]4;{index};{color}\007
> 
> # Change color 7 to #FFFFFF
> \033]4;7;#FFFFFF\007
> 
> # Change color 14 to #333333
> \033]4;14;#333333\007
> 
> # Manipulate special colors.
> # 10 = foreground, 11 = background, 12 = cursor foregound
> # 13 = mouse foreground, 708 = terminal border background
> # \033]{index};{color}\007
> 
> # Change the terminal foreground to #FFFFFF
> \033]10;#FFFFFF\007
> 
> # Change the terminal background to #000000
> \033]11;#000000\007
> 
> # Change the terminal cursor to #FFFFFF
> \033]12;#FFFFFF\007
> 
> # Change the terminal border background to #000000
> \033]708;#000000\007
> 
> ```
> Source:
>
>  - http://pod.tst.eu/http://cvs.schmorp.de/rxvt-unicode/doc/rxvt.7.pod#XTerm_Operating_System_Commands

Looking into the file [templates/colors-tty.sh](https://github.com/dylanaraps/pywal/blob/master/pywal/templates/colors-tty.sh) shows us this code:

```bash
#!/bin/sh
[ "${{TERM:-none}}" = "linux" ] && \
    printf '%b' '\e]P0{color0.strip}
                 \e]P1{color1.strip}
                 \e]P2{color2.strip}
                 \e]P3{color3.strip}
                 \e]P4{color4.strip}
                 \e]P5{color5.strip}
                 \e]P6{color6.strip}
                 \e]P7{color7.strip}
                 \e]P8{color8.strip}
                 \e]P9{color9.strip}
                 \e]PA{color10.strip}
                 \e]PB{color11.strip}
                 \e]PC{color12.strip}
                 \e]PD{color13.strip}
                 \e]PE{color14.strip}
                 \e]PF{color15.strip}
                 \ec'
```

Combining that with the code we saw earlier in the GitHub issue, we can now change our terminal colors from the console.

So, to change colors on your terminal, simply type `printf '%b' '\033]11;#FFFFFF\007'` where `11` is the color type (in this case the background) and `#FFFFF` is the new color that you want it to be. This can be set in a `rc` file and run every time you open a shell