DESCRIPTION = "Ntp time sync"
LICENSE = "CLOSED"

SRC_URI = " \
       file://time-sync.sh \
       "

RDEPENDS:${PN} = "ntpdate"

do_install() {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/time-sync.sh ${D}${sysconfdir}/init.d/time-sync.sh

	install -d ${D}${sysconfdir}/rc5.d/
	ln -sf ../init.d/time-sync.sh ${D}${sysconfdir}/rc5.d/S90time-sync.sh
}

FILES:${PN} += "${sysconfdir}/init.d"
