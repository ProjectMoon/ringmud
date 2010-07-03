#!/bin/bash
JARDIR=`dirname $0`/jar
PYDIR=`dirname $0`/python/*
LIBDIR=`dirname $0`/../lib

AGENT=$LIBDIR/aspectjweaver.jar

java -cp "${LIBDIR}/*:${JARDIR}/*:${PYDIR}" "-javaagent:$AGENT" ring.main.RingMain $*
