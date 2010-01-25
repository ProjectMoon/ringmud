#!/bin/bash
JARDIR=`dirname $0`/jar
LIBDIR=`dirname $0`/../lib
#export CLASSPATH=$JARDIR/ring-persistence.jar:$JARDIR/ring-core.jar:$JARDIR/ring-legacy.jar:$JARDIR/ring-commands.jar:$JARDIR/ring-server.jar:$JARDIR/ring-nrapi.jar:$JARDIR/ringmud.jar
#echo $CLASSPATH
#ENDORSEDDIR=$JARDIR/lib/endorsed
java -cp "${LIBDIR}/*:jar/*" ring.main.RingMain $*
