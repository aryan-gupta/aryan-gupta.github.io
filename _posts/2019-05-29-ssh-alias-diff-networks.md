---
title: SSH Alias for Different Networks
last_modified_at: 2020-05-29T09:44 #2016-03-09T16:20:02-05:00
tags: 
  - ssh
  - ssh config
---

**Note:** I wrote this blog post before my actual website went up. I cannot account for the correctness and the use of proper english on tis post. Please read with a grain of salt. I will remove this warning when I have had a chance to read over it and make sure its complete.
{: .notice--warning}

# SSH Alias for Different Networks
One of the minor inconviences I have had trying to login to my SSH box is that if I'm logging in at home I have to use `ssh internal`, but when I'm logging in from anywhere else, I need to use `ssh external`. I wanted to find a way to tell ssh: If I'm at home, use one host and if I'm away, use another. After much Googleing and trying to find the right key words, I stumbles across SSH matching. It is best described with an example. My original `~/.ssh/config` looks similar to this.


```
Host external
    HostName external.example.com

Host internal
    HostName internal.example.local
```

My updated `~/.ssh/config` looks like the one below. The first alias tests if the DNS Search of the current network is `example.local`. If it is then it skips that alias and moves to the next one. This way I can type in `ssh internal` and it will automatically connect to `internal.example.local` if I am on my home WiFi and `external.example.com` if I am traveling. The command inside the exec can be anything. So if you have another way to test which host to use, just replace that command.

```
Match originalhost internal exec "[ $(nmcli -f ip4.domain device show wlan0 | awk '{print $2}') != 'example.local' ]"
    HostName external.example.com

Host internal
    HostName internal.example.local
```

If you would like to match multiple alias to the same config, Then you can seperate them with a comma. (The rest of the match command is the same and was omitted and replaced with <...>)

```
Match originalhost internal,internal.example.local,internal.example.com exec <...>
```