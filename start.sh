#!/bin/bash

while ! curl http://api:9000/ 2> /dev/null; do
	echo Waiting for api:9000 to come up...
	sleep 5
done

echo api:9000 is UP

sbt run
