#!/bin/bash

JARS_LOCATION="build/libs-all"
MAIN_CLASS="com.beanit.asn1bean.compiler.Compiler"
SYSPROPS=""
PARAMS=""

# Attempt to set APP_HOME (from Gradle start script)
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/.." >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

CLASSPATH=$(JARS=("$APP_HOME"/"$JARS_LOCATION"/*.jar); IFS=:; echo "${JARS[*]}")

PARAMS=( )
SYSPROPS=( )
for i in "$@"; do
    if [[ $i == -D* ]]; then
	    SYSPROPS+=( "$i" )
    else
	    PARAMS+=( "$i" )
    fi
done

java "${SYSPROPS[@]}" -cp "$CLASSPATH" $MAIN_CLASS "${PARAMS[@]}"
