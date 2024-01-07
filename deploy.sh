#!/bin/bash

set -x

sudo umount /dev/sda1 /dev/sda2
sudo chmod 666 /dev/sda

oe-run-native bmap-tools-native bmaptool copy tmp/deploy/images/raspberrypi4-64/rpitank-image-raspberrypi4-64.wic.bz2 /dev/sda
