#!/bin/bash

# CROSSDATA_CONF is global enviroment variable
# JAVA_OPTS
# JSVCCMD
# JAVA_HOME
if [ -z $CROSSDATA_DRIVER_HOME ];then
	CROSSDATA_DRIVER_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi 
if [ -z $CROSSDATA_DRIVER_LOGS ];then
	CROSSDATA_DRIVER_LOGS=$(pwd)
fi 
# CROSSDATA_LIB
CROSSDATA_DRIVER_LIB="${CROSSDATA_DRIVER_HOME}/lib"

# CROSSDATA_BIN
CROSSDATA_DRIVER_BIN="${CROSSDATA_DRIVER_HOME}/bin"

# CROSSDATA_LOG_OUT
CROSSDATA_SHELL_LOG_OUT="${CROSSDATA_DRIVER_LOGS}/crossdata-shell.out"

# CROSSDATA_LOG_ERROR
CROSSDATA_SHELL_LOG_ERR="${CROSSDATA_DRIVER_LOGS}/crossdata-shell.err"

# CROSSDATA_SERVER_USER
CROSSDATA_SHELL_USER=$USER

# CROSSDATA_SERVER_PID
CROSSDATA_SHELL_PID="${CROSSDATA_HOME}/crossdata-shell.pid"

CROSSDATA_CONF="/etc/sds/crossdata-shell"

echo "CROSSDATA_DRIVER_HOME = $CROSSDATA_DRIVER_HOME"
echo "CROSSDATA_DRIVER_CONF = $CROSSDATA_CONF"
echo "CROSSDATA_DRIVER_LIB  = $CROSSDATA_DRIVER_LIB"
echo "CROSSDATA_DRIVER_BIN  = $CROSSDATA_DRIVER_BIN"
echo "CROSSDATA_DRIVER_LOG_OUT  = $CROSSDATA_SHELL_LOG_OUT"
echo "CROSSDATA_DRIVER_LOG_ERR  = $CROSSDATA_SHELL_LOG_ERR"
echo "CROSSDATA_SHEL_PID  = $CROSSDATA_SHELL_PID"

export CROSSDATA_DRIVER_LOGS
export CROSSDATA_HOME
export CROSSDATA_LIB
export CROSSDATA_BIN
export CROSSDATA_SHELL_LOG_OUT
export CROSSDATA_SHELL_LOG_ERR
export CROSSDATA_SERVER_PID

