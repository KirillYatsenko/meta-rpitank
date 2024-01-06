DESCRIPTION = "Simple audio-server example which listens for websocket connections and spans ffplay"
LICENSE = "CLOSED"

S = "${WORKDIR}"

DEPENDS = "ws"

SRC_URI = "\
  file://audio-server.c \
  file://audio-server.sh \
  file://asound.conf \
"

do_compile() {
	${CC} ${LDFLAGS} audio-server.c -o audio-server -lws
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 audio-server ${D}${bindir}

	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/asound.conf ${D}${sysconfdir}

	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/audio-server.sh ${D}${sysconfdir}/init.d/audio-server

	install -d ${D}${sysconfdir}/rc5.d/
	ln -sf ../init.d/audio-server ${D}${sysconfdir}/rc5.d/S90audio-server
	ln -sf ../init.d/audio-server ${D}${sysconfdir}/rc5.d/K90audio-server
}

FILES:${PN} = "${sysconfdir} ${bindir}"
