FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

do_install:append() {
    # Overwrite default configuration
	install -m 600 ${WORKDIR}/wpa_supplicant.conf ${D}${sysconfdir}/wpa_supplicant.conf
}
