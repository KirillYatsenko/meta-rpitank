#!/bin/sh

# 19 = STBY ; Activate high

# 13 = PWMA;
# 21 = AIN2;
# 16 = AIN1;

# 12 = PWMB;
# 26 = BIN1;
# 20 = BIN2;

function do_stop() {
	gpioset 0 19=0 12=0 13=0 21=0 16=0 26=0 20=0
}

function go_front() {
	gpioset 0 19=1 12=1 13=1 21=1 16=0 26=1 20=0
}

function go_back() {
	gpioset 0 19=1 12=1 13=1 21=0 16=1 26=0 20=1
}

function go_left() {
	gpioset 0 19=1 12=1 13=1 21=0 16=1 26=1 20=0
	usleep 100000 # 100ms
	do_stop
}

function go_right() {
	gpioset 0 19=1 12=1 13=1 21=1 16=0 26=0 20=1
	usleep 100000 # 100ms
	do_stop
}

function check_lock() {
	LOCKFILE="`basename $0`.lock"
	touch $LOCKFILE
	exec {FD}<>$LOCKFILE

	if ! flock -x -w 10 $FD; then
		echo "Some action is already running"
		exit 1
	fi
}

function main() {
	check_lock

	# QUERY_STRING is environment variable created by lighttpd server
	action=${QUERY_STRING}

	case $action in
		front)
			echo "Front"
			go_front
			;;

		back)
			echo "Back"
			go_back
			;;

		right)
			echo "Right"
			go_right
			;;
		left)
			echo "Left"
			go_left
			;;
		stop)
			echo "Stop"
			do_stop
			;;
		*)
			echo "Invalid argument"
			exit 1
			;;
	esac

	return 0
}

main
