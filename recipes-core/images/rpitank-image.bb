DESCRIPTION = "Rpi tank image"

IMAGE_FEATURES += "splash ssh-server-openssh"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    kernel-modules \
    motion \
    rpitank-time-sync \
    wpa-supplicant \
    avahi-daemon \
    avahi-dnsconfd \
    pagekite \
    audio-server \
    python3 \
    lighttpd \
    ffmpeg \
    alsa-utils \
    lighttpd-module-cgi \
    lighttpd-module-alias \
    lighttpd-module-proxy \
    lighttpd-module-auth \
    lighttpd-module-authn-file \
    ca-certificates \
    linux-firmware-rpidistro-bcm43455 \
    wireless-regdb-static \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

COMPATIBLE_MACHINE = "^rpi$"

LICENSE = "CLOSED"

INIT_MANAGER = "sysvinit"

inherit core-image
