# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=985eca66feaf0170850231d4616b3074"

SRC_URI = "git://github.com/Theldus/wsServer;protocol=https;branch=master"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "fbc1547a1a361594c72dd0cf897c937c905032f6"

S = "${WORKDIR}/git"
PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev"

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""

do_compile() {
	oe_runmake -C ${S}
}

do_install() {
    # oe_runmake install -C ${S} 'DESTDIR=${D}'
    install -d ${D}${libdir}
    install -d ${D}/usr/include

    install -m 0644 ${S}/include/ws.h ${D}/usr/include
    install -m 0644 ${S}/libws.a ${D}${libdir}
}

FILES:${PN} = "/usr/include/ws.h"
FILES:${PN}-staticdev = "/usr/lib/libws.a"
