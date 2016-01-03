#!/bin/bash

if [ -z "$JAVA_7_HOME" ]; then
    echo "Need to set JAVA_7_HOME"
    exit 1
fi

if [ -z "$1" ]; then
    echo "No Main class provided"
    exit 1
fi

java -cp javaJvm.jar:lib/ -Xmx1024m -Djava.jvm.home=$JAVA_7_HOME -Djvm.classpath=jvm-cp/ com.mcintyret.jvm.Jvm $1