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

# Username: root, password: root
INHERIT += "extrausers"
EXTRA_USERS_PARAMS = "usermod -p '\$6\$11223344\$osLpSEFZUThpEY0LTgeIRs6W.RrMbBdd4TtQRi4ZDTr147/6Om3Gjvcqwvdy55BVWDiKytLN7sobTyQlpQkin1' root;"

INIT_MANAGER = "sysvinit"

inherit core-image
