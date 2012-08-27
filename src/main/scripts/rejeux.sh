#!/bin/sh

DESC="Rejeux"
NAME=rejeux
PIDFILE=/var/run/$NAME.pid
RUN_AS=admin
REJEUX_HOME=/opt/rejeux/rejeux
TRACE_FILE=trace_vms_fmc.log
SOURCE_HOSTNAME=hermelles.dsi.damgm.i2
DESTINATION_HOSTNAME=localhost:8080
READ_DELAY=300000
MESSAGE_DELAY=1000

d_start() {
	start-stop-daemon --start -q --background -m --pidfile $PIDFILE --chuid $RUN_AS --chdir $REJEUX_HOME --exec $JAVA_HOME/bin/java -- -Xmx128m -jar $REJEUX_HOME/rejeux.jar -i $REJEUX_HOME/input -f $TRACE_FILE -s $SOURCE_HOSTNAME -h $DESTINATION_HOSTNAME -rt $READ_DELAY -t $MESSAGE_DELAY
}

d_stop() {
	start-stop-daemon --stop --quiet --pidfile $PIDFILE
	if [ -e $PIDFILE ]
		then rm $PIDFILE
	fi
}

case $1 in
	start)
	echo -n "Starting $DESC: $NAME"
	d_start
	echo "."
	;;
	stop)
	echo -n "Stopping $DESC: $NAME"
	d_stop
	echo "."
	;;
	restart)
	echo -n "Restarting $DESC: $NAME"
	d_stop
	sleep 1
	d_start
	echo "."
	;;
	*)
	echo "usage: $NAME {start|stop|restart}"
	exit 1
	;;
esac

exit 0
