#!/bin/bash

#if [ "$SERVER_HOME" = '.' ]; then
#   SERVER_HOME=$(echo `pwd` | sed 's/\/bin//')
#else
#   SERVER_HOME=$(echo $SERVER_HOME | sed 's/\/bin//')
#fi

PRGDIR=`dirname $0`
SERVER_HOME=$(echo `readlink -f $PRGDIR` | sed 's/\/bin//')
cd $SERVER_HOME
svn up
ant compile
exit 0
