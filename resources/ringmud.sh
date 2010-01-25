#!/bin/bash
JARDIR=`dirname $0`/jar
LIBDIR=`dirname $0`/../lib

#Commented out for now -- was used to support embedded DB instance
#ENDORSEDDIR=$JARDIR/lib/endorsed

java -cp "${LIBDIR}/*:${JARDIR}/*" ring.main.RingMain $*
