#!/bin/sh

# description: Audio server waits for websocket connections and spawns ffplay

start() {
	echo "Starting audio-server service"
	amixer set 'PCM' 90%
	start-stop-daemon -S -b audio-server
}

stop() {
	echo "Stopping audio-server service"
	start-stop-daemon -K --name audio-server
}

case "$1" in
	start)
		start
		;;
	stop)
		stop
		;;
	restart)
		stop
		start
		;;
	status)
		# code to check status of app comes here
		# example: status program_name
		;;
	*)
		echo "Usage: $0 {start|stop|status|restart}"
esac

exit 0
