#!/bin/sh
ROOT_DIR=`dirname $0`/../
cd $ROOT_DIR/server/target
java -jar server-1.0-SNAPSHOT-jar-with-dependencies.jar
