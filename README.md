# meta-rpitank
This is yocto layer, which you can use to build the rpitank's software.  

The rpitank is the robot that can be controlled through the public DNS name
without having public ip.  
It's achieved with the help of the pagekite.net service, you can read more: https://pagekite.net

The rpitank can stream the video signal captured from the USB camera and is also able to play the audio
recorded from the client's microphone.

#### ToDo: insert demo video here

### Layer dependencies
- meta-openembedded/meta-oe
- meta-openembedded/meta-python
- meta-openembedded/meta-networking
- meta-raspberrypi

### Hardware
- Raspberry Pi 4B
- Micro SD card
- Lenovo Essential FHD Webcam
- Some no-name AliExpress-type USB speaker
- TB6612FNG motor's driver
- 3x18650 batteries
- 5v power bank
- [Tank chassis + 2x12v motors](https://www.amazon.com/Platform-Raspberry-Programmable-Learning-Assemble/dp/B09TFN2Z56)

### Software
- motion - used for streaming the video signal
- lighttpd - light http web server
- ffmpeg - used for playing audio
- pagekite - exposing local ports to the internet
- ws - websockets's library for receiving audio frames from the web client and playing it through ffmpeg

## Instructions:
  ### Software building & flashing
0. Go through yocto quick build instructions https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html in order to get the
   required tools for successful bitbake building.
1. Download all the layers dependencies which are listed above.
2. Inside the meta-rpitank a few variables should be updated, you can easily grep them by the names:
  - LIGHTTPD_USERNAME - it's a http server username
  - LIGHTTPD_PASSWORD - a http server password
  - PAGEKITE_DNS - dns name for your server. You should sign-up in pagekite.net service and create the "kite"
  - PAGEKITE_SECRET - it can obtained from the pagekite.net account page
  - WIFI_SSID - SSID of your local network
  - WIFI_PSK - it can be generated with: wpa_passphrase <ssid> <password>
3. Update the recipes-connectivity/audio-server/files/asound.conf to point to your USB speaker name.
   Check the name by plugging the speaker to your PC and executing: `aplay -l | awk -F \: '/,/{print $2}' | awk '{print $1}' | uniq`
4. Inside the poky repository, run: `source oe-init-build-env ../build`
5. Add all the dependencies layers to the conf/bblayers.conf file. You can use: `bitbake-layers add-layer <layer_path>`
6. Set MACHINE ?= "raspberrypi4-64" inside conf/local.conf
7. Add LICENSE_FLAGS_ACCEPTED:append = " commercial synaptics-killswitch" inside conf/local.conf - it's needed for propietary wifi drivers compilation.
8. Run: `bitbake rpitank-image`
9. Inside the repository, there is deploy.sh helper script which can be used to flash the image to the sd card. Copy this script to the build directory.
   The script is dependent on bmap-tools, you can read about how to install it here: https://docs.yoctoproject.org/dev-manual/bmaptool.html.  
10. Run `./deploy.sh` from the build directory, it will unmount the sd card and flash the image.


The output of the build process is the minimalistic custom linux distribution.

### Hardware hookup
![image](https://github.com/KirillYatsenko/meta-rpitank/blob/kirkstone/assets/diagram.png)  
  
The diagram is pretty simple. The +12V is a 3x18650 batteries connected in series.  
  
The raspbbery pi is powered separately from the 10k mAh powerbank connected via type c connector.
  
The `motor #XX` are the motors' terminals, it doesn't really matter how you connect them, you always can update the motors.cgi script's gpios configuaration. 

Also, there is no camera or speaker on the diagram. You need to connect them to the Pi's usb ports.

### Possible improvements
One of the biggest improvement is to power the raspbbery pi through the same power source as the DC motors. 
However to achieve this the clear, noise-free power DC voltage should be outputed from the step down regulator.
I couldn't make it work by connecting the step down output to the Pi's +5V pin. However it may be possible to supply
the power through the type C connector as it may have the voltage regulator which may reduce noise.

### Known issues
Sometimes for the uknown reasons, the speaker volume resets to the default 30% comparing to what it's being set during the boot process (90%).
If this happens, the volume is not particullary loud and hard to hear.
