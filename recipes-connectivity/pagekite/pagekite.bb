DESCRIPTION = "Pagekite allows exposing localhost services to the world"
LICENSE = "CLOSED"

SRC_URI = " \
       file://pagekite.py \
       file://pagekite.rc \
       file://pagekite.sh \
       "

RDEPENDS:${PN} = "python3"

INSANE_SKIP:${PN} += "file-rdeps"

do_install() {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/pagekite.sh ${D}${sysconfdir}/init.d/pagekite
    install -m 644 ${WORKDIR}/pagekite.rc ${D}${sysconfdir}

	install -d ${D}${sysconfdir}/rc5.d/
	ln -sf ../init.d/pagekite ${D}${sysconfdir}/rc5.d/S90pagekite
	ln -sf ../init.d/pagekite ${D}${sysconfdir}/rc5.d/K90pagekite

	install -d ${D}${sbindir}
	install -m 755 ${WORKDIR}/pagekite.py ${D}${sbindir}
}

FILES:${PN} += "${sysconfdir}/init.d"
