#!/bin/bash
PRGDIR=`dirname $0`
SERVER_HOME=$(echo `readlink -f $PRGDIR` | sed 's/\/bin//')
#JAVA_HOME="/opt/jdk1.7.0_60"
. "$SERVER_HOME/bin/env.sh"
echo "Stopping ${MODULE} ... "
        if [ -n "$PROID" ]; then
                echo "Kill process id - ${PROID}"
                kill -9 ${PROID}
                echo STOPPED
        else
                echo "No process running."
fi
