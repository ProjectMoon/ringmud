#!/bin/bash
JARDIR=`dirname $0`/jar
PYDIR=`dirname $0`/python/
LIBDIR=`dirname $0`/../lib

export RING_LIB_DIR=$JARDIR

java -cp "${LIBDIR}/*:${JARDIR}/*:${PYDIR}" ring.main.RingMain $*
