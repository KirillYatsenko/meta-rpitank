#!/bin/sh

function motion_running() {
	[ -f /var/run/motion.pid ]
}

function do_start() {
	if motion_running; then
		echo "motion is already running"
	else
		motion
		echo "started"
	fi
}

function do_stop() {
	killall motion
	echo "stopped"
}

function do_status() {
	if motion_running; then
		echo -n "running"
	else
		echo -n "stopped"
	fi
}

function main() {
	# QUERY_STRING is environment variable created by lighttpd server
	action=${QUERY_STRING}

	case $action in
		start)
			do_start
			;;

		stop)
			do_stop
			;;

		status)
			do_status
			;;
		*)
			echo "Invalid argument"
			exit 1
			;;
	esac

	return 0
}

main
