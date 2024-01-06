FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Enable the ffplay dependencies
PACKAGECONFIG:append = " avcodec avformat swscale swresample sdl2"
