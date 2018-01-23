#!/bin/bash

cd `dirname $0`/..
BASE=`pwd`

PROJECT_PATH=$BASE

. "$BASE/bin/setenv.sh"

if [ "$ISRUN" != "1" ]; then
	rm -rf $BASE/logs/*.log*
	rm -rf $BASE/tmp/*.log*
	
	echo "是否清除版本环境?[Y/N]"
	read YES
	if [ "x$YES" == "xY" ];then
		find -name ".svn" -exec rm -rf {} \;
		find -name "CVS" -exec rm -rf {} \;
	fi
else
	echo "本环境还在运行，请结束后再执行本命令！"
fi
