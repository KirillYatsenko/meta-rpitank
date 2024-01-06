# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   doc/copyright
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "GPL-2.0-only & Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://doc/copyright;md5=c0a142b4943af82ce229cf6b4660284e"

SRC_URI = "git://github.com/Motion-Project/motion;protocol=https;branch=master \
           file://motion.conf"

DEPENDS = "jpeg ffmpeg libmicrohttpd"

# Modify these as desired
PV = "test+git${SRCPV}"
SRCREV = "b7c8afb4755842bd89f20f3d7dfc082b453e328e"

S = "${WORKDIR}/git"

# NOTE: the following prog dependencies are unknown, ignoring: kill

# NOTE: if this software is not capable of being built in a separate build directory
# from the source, you should replace autotools with autotools-brokensep in the
# inherit line
inherit pkgconfig gettext autotools

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = ""

do_install:append() {
	install -d ${D}${sysconfdir}/motion
	install -m 0644 ${WORKDIR}/motion.conf ${D}${sysconfdir}/motion/motion.conf
}

FILES:${PN} += "${sysconfdir}"
