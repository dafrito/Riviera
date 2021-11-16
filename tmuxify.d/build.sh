#!/bin/bash
while true; do
    make clean
    make
    inotifywait -r -e MODIFY src Makefile
done
