---
title: Giving up my VFIO/PCI Passthrough setup (for now)
tags:
  - vfio
  - pci passthrough
  - gpu passthrough
  - vm
---

One ot the main issues I had with my VFIO/PCI Passthrough setup was the broken audio from HDMI/DP connector. I didn't mind it however since I could just use my headphones. When I would boot up my machine, I would reconfigure pulseaudio to just turn off that particular sink and all my applications would use the on-board sound card. However, recently Chrome has been acting up recently and refuses to play any videos (It seems to be a hardware bug on Haswell processors). Disabling intel IOMMU module from my kernel parameters (removing the `intel_iommu=on` and `iommu=pt` params) fixes my issues. I have been recently debating on permanently moving away from this vm setup anyways because I plan on doing a full system upgrade using an AMD Ryzen processor that does not have an iGPU. This would mean I would have to buy 2 different GPUs and make the system more complicated than its worth. There exists guides and methods to do single GPU passthrough but that doesn't solve the issue that PCI pass through solves for me: switching between Windows and Linux without much downtime. I will have to play around with this concept to see how long it takes to switch when using a single GPU setup vs simply dual-booting my machine. More to come on this when I get my hands on the new hardware.


### Disabling IOMMU and VFIO
 1. Remove kernel parameter: `intel_iommu=on iommu=pt`
 2. Disabling `VT-d` in BIOS
 3. Setting dGPU as primary in BIOS
 4. Unbinding dGPU from VFIO module
   - Remove line from `/etc/modprobe.d/vfio.conf`
    ```
    options vfio-pci ids=8086:0c01,1002:6798,1002:aaa0
    ```
 5. Whitelist dGPU kernel modules/drivers
   - Remove lines from `/etc/modprobe.d/blacklist.conf`
    ```
    blacklist amdgpu
    blacklist radeon
    ```
 6. Add kernel parameters for dGPU
   - Add lines to `/etc/modprobe.d/amdgpu.conf`
    ```
    options radeon si_support=0
    options amdgpu si_support=1
    ```
 6. Remove VFIO kernem module from init ramdisk
   - Update `/etc/mkinitcpio.conf`<sup>**</sup>
    ``` diff
     --- mkinitcpio.conf	    2020-11-15 11:12:55.473397038 -0500
     +++ /etc/mkinitcpio.conf	2020-11-15 11:13:05.629972293 -0500
     @@ -4,7 +4,7 @@
      # run.  Advanced users may wish to specify all system modules
      # in this array.  For instance:
      #     MODULES=(piix ide_disk reiserfs)
     -MODULES=(vfio_pci vfio vfio_iommu_type1 vfio_virqfd i915)
     +MODULES=(amdgpu radeon)
       
      # BINARIES
      # This setting includes any additional binaries a given user may
    ```

 7. Rerun `mkinitcpio -p linux`
 8. Reboot


After that all I had left to do was update my drivers for an AMD card using `sudo pacman -S xf86-video-amdgpu` and then changemy dotfiles for the display name changes. 

<sup>**</sup>: Please note that line 9 (the "empty" line) in the diff has an extra `U+A0` character. The backend website was having issues rendering code blocks with empty lines in lists. If you do copy paste, make sure to delete that entire line from the file. 