#!/bin/bash
JARDIR=`dirname $0`
ENDORSEDDIR=$JARDIR/lib/endorsed
java -Djava.endorsed.dirs=$ENDORSEDDIR -jar $JARDIR/RingMUD.jar $*
