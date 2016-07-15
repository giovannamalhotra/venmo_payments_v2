#!/usr/bin/env bash

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - — - - - - - - - - -
# Script for running the rolling_median calculation program, written in scala.
# This script should be run with the "bash" prefix. Example: "bash  run.sh" 

# MDPATH is set to where these files are:
# run.sh   venmo_input/venmo-trans.txt   venmo_output/output.txt
# 
# src/
# insight_2016_intellij_2.11-1.0.jar
# 
# src/lib/
# joda-convert-1.2.jar		json4s-core_2.11-3.3.0.jar	nscala-time_2.11-2.12.0.jar	scala-xml_2.11-1.0.5.jar
# joda-time-2.9.3.jar		json4s-native_2.11-3.3.0.jar	paranamer-2.8.jar
# json4s-ast_2.11-3.3.0.jar	json4s-scalap_2.11-3.3.0.jar	scala-library-2.11.8.jar
#
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - — - - - - - - - - -


MDPATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JAR=$MDPATH/src/lib
java -cp $JAR/joda-convert-1.2.jar:$JAR/joda-time-2.9.3.jar:$JAR/json4s-ast_2.11-3.3.0.jar:$JAR/json4s-core_2.11-3.3.0.jar:$JAR/json4s-native_2.11-3.3.0.jar:$JAR/json4s-scalap_2.11-3.3.0.jar:$JAR/nscala-time_2.11-2.12.0.jar:$JAR/paranamer-2.8.jar:$JAR/scala-library-2.11.8.jar:$JAR/scala-xml_2.11-1.0.5.jar:$MDPATH/src/insight_2016_intellij_2.11-1.0.jar media_degree $MDPATH/venmo_input/venmo-trans.txt $MDPATH/venmo_output/output.txt
