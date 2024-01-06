FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
       file://index.html \
       file://script.js  \
       file://style.css  \
       file://motors.cgi \
       file://video.cgi  \
       file://nosignal.png  \
       file://lighttpd-plain.user \
       "

RDEPENDS:${PN}:append = " libgpiod-tools ffmpeg"

do_install:append() {
	install -d ${D}/www/pages/rpitank
	install -m 0644 ${WORKDIR}/index.html ${D}/www/pages/rpitank/index.html
	install -m 0644 ${WORKDIR}/script.js ${D}/www/pages/rpitank/script.js
	install -m 0644 ${WORKDIR}/style.css ${D}/www/pages/rpitank/style.css
	install -m 0644 ${WORKDIR}/nosignal.png ${D}/www/pages/rpitank/nosignal.png

	install -m 0755 ${WORKDIR}/motors.cgi ${D}/www/pages/rpitank/motors.cgi
	install -m 0755 ${WORKDIR}/video.cgi ${D}/www/pages/rpitank/video.cgi

	install -d ${D}${sysconfdir}/lighttpd
	install -m 0755 ${WORKDIR}/lighttpd-plain.user ${D}${sysconfdir}/lighttpd/lighttpd-plain.user
}

FILES:${PN} += " ${sysconfdir}/lighttpd"
