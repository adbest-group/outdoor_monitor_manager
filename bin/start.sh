#!/bin/bash

PRGDIR=`dirname $0`
SERVER_HOME=$(echo `readlink -f $PRGDIR` | sed 's/\/bin//')
JAVA_HOME="/opt/software/jdk1.8.0_171"
. "$SERVER_HOME/bin/env.sh"
if [ -n "$PROID" ]; then
	echo "${MODULE} is running."
else
	
	if [ ! -d "$SERVER_HOME/logs/pids.log" ] ; then
		mkdir -p $SERVER_HOME/logs/pids.log
	fi
	if [ ! -d "$SERVER_HOME/tmp/run.log" ] ; then
		mkdir -p $SERVER_HOME/tmp/run.log
	fi
	echo "Starting ${MODULE}..."

	export CLASSPATH=$CLASSPATH
	nohup $JAVA_HOME/bin/java -server $JAVA_OPTS ${main} ${COMMAND} > $SERVER_HOME/logs/catalina.log 2>&1 &
	echo $!> $SERVER_HOME/logs/pids.log/run.pid
fi
