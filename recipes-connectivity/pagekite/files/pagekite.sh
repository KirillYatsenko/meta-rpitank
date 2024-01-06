#!/bin/sh

# description: Pagekite allows exposing localhost services to the world

wlan0_up() {
	local ip=$(ifconfig wlan0 | grep netmask | awk '{print $2}')
	[ -z "$ip" ]
}

wait_wlan0_up() {
	# Wait not more then 60 second to connect to wifi
	indx=0
	while [ "$indx" -lt "60" ]; do
		if ! wlan0_up; then
			return 0
		fi

		echo "pagekite: waiting for the wlan0 to connect: $indx/60"

		indx=$(($indx+1))
		sleep 1
	done

	return 1
}

start() {
	echo "Starting pagekite service"

	if ! wait_wlan0_up; then
		echo "Wlan0 is not up, exiting..."
		exit 1
	fi

	pagekite.py --optfile /etc/pagekite.rc --daemonize \
				--logfile=/var/log/pagekite.log

}
stop() {
	echo "Stopping pagekite service"
	killall pagekite.py
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

