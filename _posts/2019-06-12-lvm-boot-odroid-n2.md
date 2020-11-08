---
title: LVM Boot on ODroid N2
tags:
  - odroid
  - lvm
---

**Note:** I wrote this blog post before my actual website went up. I cannot account for the correctness and the use of proper english on tis post. Please read with a grain of salt. I will remove this warning when I have had a chance to read over it and make sure its complete.
{: .notice--warning}

# LVM booting on the ODroid-N2
I recently accurired a ODroid-N2 (refered as ODroid) so I could move my home server from the Intel Stick to something a lot more beefy. The ODroid is a 4GB ARM hexa-core Single Board Computer (SBC) with gigabit ethernet, HDMI and 4 USB 3.0 ports with many other features. The only problem I had with this machine is that its ISA is ARM based and Arch Linux doesn't officially suport ARM. There is however, a port of the distribution for ARM and SBC's like this one. However the default configuration is very barebones and considering that this was the only ARM based machine I had, I ran into many difficulties. But with every obstacle comes a learning opportunity. 


## Prepping the SD Card (Attempt 1)
While doing research on the board, I realized that the way the Linux kernel boots, setting up LVM on the root partition should work. A linux bootloader the kernel into the memory and runs it. The issue with this is that none of the filesystems may be initialized. For example, with an LVM setup, the root lvm partition may not be available in the early stages of the kernel initialization. For this reason, the kernel uses whats called an initramfs. Because the LVM partitions are mounted after the kernel boots, it should be possible to setup LVM even though U-boot doesnt support LVM as long as the kernel is in a suuported U-boot partition.


### Setting up LVM on SD Card
Setting up LVM was very similar to setting it up on my Arch Linux laptop. First, the partition table should be destroyed on the SD Card. The SD card showed up as `/dev/sda` on my system. 

``` shell
$ sudo dd if=/dev/zero of=/dev/sda bs=1M count=8
```

Then using fdisk, a new partition table should be set up. The table looked very simple. One partition for the `boot/` partiton, holding the Linue kernel and initramfs and another partition for the actual LVM volume. 

```
```

After fdisk syncs the disks with the kernel, the partition table should look like this. 

```
```

To setup the actual LVM, first a phyical volume must be created. Use `lvmdiskscan` to check which devices can be used with LVM. These devices can be any block device such as a partition or a drive. In this case we must use the partition we setup in the previous step. 

``` shell
$ sudo pvcreate /dev/sda2
```

You can use `pvdisplay` to see the new volume created. Then you want to create a Volume Group. YOu can name it anything you want, but I typically name it `vg{num XX}-{hostname}` to keep the names unique. You want to also specify where to create the volume group. Check if it was created with `vgdisplay`

``` shell
$ sudo vgcreate vg01-photon /dev/sda2
```

Now that we have our Volume Group, we can finally start creating our LVM partitions. I will create 3 logical volumes. One for the root fs, one for the `/home/` folder, and one for the `/svr/`. I typically like to use the `/svr/` directory so I can easily do backups and the like. Note that for `100%FREE` the argument is a lowercase l where for the others its an uppercase L.

``` shell
$ sudo lvcreate -L 32G -n root vg01-photon
$ sudo lvcreate -L 8G -n home vg01-photon
$ sudo lvcreate -l 100%FREE -n svr vg01-photon
```

This will create 3 volumes with the names root, home, and svr. You can view the volumes with `lvdisplay`. Format the partitions and now we can now mount the partitions in the appropriate places and apply the image.

``` shell
$ sudo mkfs.vfat /dev/sda1
$ sudo mkfs.ext4 /dev/vg01-photon/root
$ sudo mkfs.ext4 /dev/vg01-photon/home
$ sudo mkfs.ext4 /dev/vg01-photon/svr
```

Make a root directory and mount the root partitions on the mount point. 

``` shell
$ mkdir root
$ sudo mount /dev/vg01-photon/root root

```

Mount the root lvm partition on the mount point and create the rest of the directories.

``` shell
$ mkdir root/home root/svr root/boot
$ cd root
$ sudo mount /dev/vg01-photon/home home
$ sudo mount /dev/vg01-photon/svr svr
$ sudo mount /dev/sda1 boot
$ cd ..
```

Then download the image and extract it onto the new root folder

``` shell
$ wget http://os.archlinuxarm.org/os/ArchLinuxARM-odroid-n2-latest.tar.gz
$ sudo bsdtar -xpf ArchLinuxARM-odroid-n2-latest.tar.gz -C root
```

Burn the u-boot bootloader onto the SD card

```
$ sudo dd if=boot/u-boot.bin of=/dev/sda  conv=fsync,notrunc bs=512 seek=1
```

Change the `/etc/fstab` file to reflect the new changes so the kernel can mount the proper devices on the proper locations. 


``` fstab

```

Now unmount the root partition and the Linux kernal should boot, right?

``` shell
$ sudo umount root -R
```

### First Boot
When booting, u-boot was detected by the ODroid and the kernel started, but nothing happened. No network and no display. Upon checking the serial output, the last few lines gave us our error.

```
:: running early hook [udev]
Warning: /lib/modules/4.9.177-3-ARCH/modules.devname not found - ignoring
Starting version 242.29-1-arch
:: running hook [udev]
:: Triggering uevents..
[    5.336091@5] usb 2-1: new SuperSpeed USB device number 2 using xhci-hcd
[    5.364813@5] hub 2-1:1.0: USB hub found
[    5.365191@5] hub 2-1:1.0: 4 ports detected
:: performing fsck on '/dev/mmcblk1p2'
:: mounting '/dev/mmcblk1p2' on real root
mount: /new_root: unknown filesystem type 'LVM2_member'.
You are now being dropped into an emergency shell.
```

mounting `/dev/mmcblk1p2`, wait what? Why is the kernel mounting partition 2 as real root? Should it not be mounting `/dev/vg01-photon/root` as real root? After some Googling, I figured out the real issue. 


## Prepping the SD Card (Attempt 1)
As previously stated, the initramfs is a barebones filesystem used by the kernel before it can look for the real filesystem. This initial RAM filesystem must include the barebone modules used to finish initializing the kernel. In the bare Arch ARM initramfs, the module for loading LVM is missing. By digging in to the filesystem, we can dertemine what modules are loaded into the kernel when it boots. 

```
$ sudo cat /etc/mkinitcpio.conf | grep 'HOOKS='
# HOOKS
```

The last line shows us our issue, the kernel does not load the lvm module so it doesnt know what an LVM volume is. But can't you just add the module to the list and call it a day? That is where the pain starts. Once we modify the script settings, we have to recreate the initramfs, but we cant do that unless we have an ARM specific ramfs creator or create the new ramfs on the board. After more reseach I came to 2 ways we can do it.

 1. Use dracut as an alternative to mkinitcpio
 2. Boot from a USB Drive and directally manipulate the SD Card to setup the LVM

The USB method seemed the more difficult option so I chose to try that first. The method burns the u-boot bootloader on to the SD Card, but rather than using the rootfs as the 2nd partition on the SD Card, we use the USB drive as the root drive. This allows us to create our ramfs on an ARM processor for the ARM processor. 

In order to boot into a alternative media, we can image an SD card for the initial kernel and uBoot device and use the flashdrive as the rootfs. Because after the boot process we don't need to access the `/boot/` folder (holding the 'EFI' partition), we can remove the SD Card to create the new SD Card (You can use the same SD card, but I had 2 cards so I could swap them out while I was trying to figure this out). Create the image as shown above on both the USB drive and on the SD Card. On the SD Card edit the boot.ini file to use the partition 2 of the USB drive as the rootfs. Mount the boot partition of the SD card and edit the `boot.ini` file. Make sure to only modify the root parameter and not touch the rest of the line

```
# Change this line 
setenv bootargs "root=/dev/mmcblk${devno}p2 ... "
# to this
setenv bootargs "root=/dev/sda2 ... "
# You can also choose to use UUID (get the UUID from running blkid)
setenv bootargs "root=UUID=82e84fcf-23be-4498-907e-586568c7d5d4 ... "
```

Remove the boot mount point from the `fstab` folder

```
$ sudo nano sdcard/etc/fstab
```






Once you have your SD card prepared, we need to add the lvm hook in the initramfs. Edit the config file and add lvm2 to the list of hooks before the filesystem hook

``` shell
$ sudo nano /etc/mk

```

```
mkinitcpio -p linux-odroid-n2
mkimage -A arm -O linux -T ramdisk -C none -a 0 -e 0 -n "uInitrd $(uname -r)" -d /boot/initrd.img /tmp/uInitrd
cp /tmp/uInitrd /boot/initramfs-linux.uimg
```

--

```
nano /tmp/boot/boot.ini

root=/dev/mapper/vg01--photon-root
lvmwait=/dev/mapper/vg01--photon-root

lvcreate -L 32G -n root vg01-photon; lvcreate -L 8G -n home vg01-photon; lvcreate -l 100%FREE -n svr vg01-photon

mkfs.ext4 /dev/vg01-photon/root; mkfs.ext4 /dev/vg01-photon/home; mkfs.ext4 /dev/vg01-photon/svr

dd if=root/boot/u-boot.bin of=/dev/mmcblk1 conv=fsync,notrunc bs=512 seek=1

mkimage -A arm -O linux -T ramdisk -C none -a 0 -e 0 -n "Ramdisk Image" -d /boot/initramfs-linux.img /boot/initramfs-linux.uimg
```