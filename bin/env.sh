#!/bin/bash

#FINDNAME=$0
#while [ -h $FINDNAME ] ; do FINDNAME=`ls -ld $FINDNAME | awk '{print $NF}'` ; done
#SERVER_HOME=`echo $FINDNAME | sed -e 's@/[^/]*$@@'`
#unset FINDNAME

#if [ "$SERVER_HOME" = '.' ]; then
#   SERVER_HOME=$(echo `pwd` | sed 's/\/bin//')
#else
#   SERVER_HOME=$(echo $SERVER_HOME | sed 's/\/bin//')
#fi

export LANG=zh_CN
export LC_ALL=zh_CN.UTF-8
export JAVA_HOME=$JAVA_HOME

CLASSPATH=`echo $JAVA_HOME/lib/*.jar | tr ' ' ':'`
CLASSPATH=$CLASSPATH:`echo $SERVER_HOME/target/lib/compile/*.jar | tr ' ' ':'`
CLASSPATH=$CLASSPATH:$SERVER_HOME/conf
if [ -d "$SERVER_HOME/target/src/classes" ] ; then
	CLASSPATH=$SERVER_HOME/target/src/classes:$CLASSPATH
fi

. ${PRGDIR}/startup.properties

if [ "${env_heap}" != '' ]; then
	HEAP_MEMORY=${env_heap}
else
	HEAP_MEMORY=512m
fi

if [ "${env_perm}" != '' ]; then
	PERM_MEMORY=${env_perm}
else
	PERM_MEMORY=64m
fi

if [ "${env_debug_port}" != '' ]; then
	DEBUG_ADDRESS=${env_debug_port}
fi

if [ "${module}" != '' ]; then
	MODULE=${module}
fi

if [ "${env_jmx_port}" != '' ]; then
	JMX_PORT=${env_jmx_port}
fi

if [ "${env_rmi_server}" != '' ]; then
	RMI_SERVER=${env_rmi_server}
fi

if [ "${env_http_port}" != '' ]; then
	HTTP_PORT=${env_http_port}
fi

if [ "${env_http_host}" != '' ]; then
	HTTP_HOST=${env_http_host}
fi

if [ "${env_young_heap}" != '' ]; then
	YOUNG_HEAP=${env_young_heap}
fi 

if [ "${env_gc_millis}" != '' ]; then
	GC_MILLIS=${env_gc_millis}
fi 

if [ "${env_gc_threads}" != '' ]; then
	GC_THREADS=${env_gc_threads}
fi

if [ "${env_gc_type}" != '' ]; then
	GC_TYPE=${env_gc_type}
fi

if [ "${env_passwd_file}" != '' ]; then
	PASSWD_FILE=${env_passwd_file}
fi

if [ "${env_access_file}" != '' ]; then
	ACCESS_FILE=${env_access_file}
fi


    JAVA_OPTS="-Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=24 -XX:+AggressiveOpts -XX:+HeapDumpOnOutOfMemoryError -XX:+UseFastAccessorMethods"
    
    JAVA_OPTS="${JAVA_OPTS} -Xms$HEAP_MEMORY -Xmx$HEAP_MEMORY -XX:PermSize=$PERM_MEMORY -XX:MaxPermSize=$PERM_MEMORY -Duser.dir=$SERVER_HOME -Dbase=$SERVER_HOME -Djava.io.tmpdir=$SERVER_HOME/tmp/run.log -Dlog4j.config=$SERVER_HOME/conf/log4j.properties"
	
    #JAVA_OPTS="${JAVA_OPTS} -Xms$HEAP_MEMORY -Xmx$HEAP_MEMORY -XX:PermSize=$PERM_MEMORY -XX:MaxPermSize=$PERM_MEMORY -Duser.dir=$SERVER_HOME"
    
    if [ "$JMX_PORT" != '' ]; then
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=${JMX_PORT}"
    fi

    if [ "$RMI_SERVER" != '' ]; then
        JAVA_OPTS="${JAVA_OPTS} -Djava.rmi.server.hostname=${RMI_SERVER}"
    fi
    
    if [ "$GC_TYPE" == 'parallel' ]; then
        JAVA_OPTS="${JAVA_OPTS} -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy "
        if [ "$GC_THREADS" != '' ]; then
        	JAVA_OPTS="${JAVA_OPTS} -XX:ParallelGCThreads=${GC_THREADS} "
    	fi
    fi
    
    if [ "$GC_TYPE" == 'cms' ]; then
        JAVA_OPTS="${JAVA_OPTS} -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=80 "
        if [ "$GC_THREADS" != '' ]; then
        	JAVA_OPTS="${JAVA_OPTS} -XX:ParallelCMSThreads=${GC_THREADS} "
    	fi
    	
    	if [ "$YOUNG_HEAP" != '' ]; then
        	JAVA_OPTS="${JAVA_OPTS} -Xmn${YOUNG_HEAP} "
    	fi
    fi
    
    #if [ "$GC_MILLIS" != '' ]; then
    #    JAVA_OPTS="${JAVA_OPTS} -XX:MaxGCPauseMillis=${GC_MILLIS}"
    #fi

    if [ "$DEBUG_ADDRESS" != '' ]; then
        JAVA_OPTS="${JAVA_OPTS} -Xdebug -Xrunjdwp:transport=dt_socket,address=$DEBUG_ADDRESS,server=y,suspend=n"
    fi

    if [ "$PASSWD_FILE" != '' ]; then
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.password.file=${PASSWD_FILE}"
    fi

    if [ "$ACCESS_FILE" != '' ]; then
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.access.file=${ACCESS_FILE}"
    fi 
    
    #COMMAND="nohup ${JAVA_HOME}/bin/java $JAVA_OPTS  ${main}"
    COMMAND=''
    
    if [ "$HTTP_PORT" != '' ]; then
        COMMAND=" -httpPort=${HTTP_PORT}"
    fi 
    
    if [ "$HTTP_HOST" != '' ]; then
        COMMAND="${COMMAND} -hostname=${HTTP_HOST}"
    fi 
    
    #export CLASSPATH=$CLASSPATH
    #echo $JAVA_OPTS
    #echo  "Starting ${MODULE} ...,logs redirect to ${SERVER_HOME}/logs/catalina.log"
    
	#nohup ${JAVA_HOME}/bin/java -server com.adtime.dsp.Booter >${SERVER_HOME}/logs/catalina.log 2>&1 &
    
    #echo STARTED
    
    PROID=`ps -ef|grep ${MODULE}|grep -v grep|awk '{print $2}'`
    
    #echo $PROID
    #echo $MODULE
