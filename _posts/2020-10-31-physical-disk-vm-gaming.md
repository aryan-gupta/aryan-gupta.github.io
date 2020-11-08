---
title: Physical Disk boot for QEMU/KVM VM
tags:
  - vm
  - gaming
---

Yesterday, I accidentally nuked my gaming VM's hard drive that held all the Steam game data. I've had a plan to change the way my gaming vm is setup for while now, so I decided to use this chance to test out a few things. 

I have this Samsung 860 EVO 500 GB that I used to keep a Windows ToGo image. I would attach it to my laptop using a SATA-to-USB C adaptor and boot into it to play games. Because of this pandemic, I have not had the need to play games on my laptop so its been sitting in my backpack for a while. 

I have been using my PCI Passthrough system for the past 6 months. I have come to like it very much. I can quickly jump into my Windows gaming OS and quickly jump out if I need to run something on my Linux system. My only gripe I have is that some games with anti-cheat or other VM checks will refuse to run if it detects its inside a VM. And so I have been thinking of a better way to do this. 

The way I want to setup my system is that my Windows OS is on an external HDD, and is a ToGo drive so it can more easily jump from computer to computer. There will be two ways to boot into the WIndows OS, I can either attach it to a KVM VM and pass my dedicated graphics card into it, or I can restart my machine and directly run Windows on the hardware. Reason being, if I wanted to quickly jump into playing games, I can spin-up the VM and start playing and quickly return to Linux incase I needed to do so but also be able to run the VM on hardware if there is a game I want to play that is being problematic. 


### The Setup
I have attached the Samsung EVO to my MOBO and instructed libvirt to pass the disk in. The `Select or create custom storage option` is `/dev/disk/by-id/ata-Samsung_SSD_860_EVO_500GB_<S/N>`. I have omitted the serial number and replaced it with `<S/N>`

![libvirt-adding-physical-disk](/assets/img/libvirt-adding-physical-disk.png)

I also changed the boot order to boot into the newly attached disk. 

After rebooting the VM, everything worked as expected. Since the OS image had never been booted as a KVM VM, I needed to download and install the kvm drivers from [here](https://docs.fedoraproject.org/en-US/quick-docs/creating-windows-virtual-machines-using-virtio-drivers/index.html#virtio-win-direct-downloads)


### Afterwards
Hopefully, when I have my new system built next year, I can replicate something like this with a macropad that automatically reboots my machine into Windows or starts up the gaming vm. More testing will be need to be done to see which is more user friendly. 