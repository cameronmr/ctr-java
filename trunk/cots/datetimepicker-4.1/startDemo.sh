#!/bin/sh
# startDemo.sh
#
# Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
#
# This software is the proprietary information of Stand By Soft, Ltd.
# Use is subject to license terms.

LOCALCLASSPATH="$CLASSPATH"
LOCALCLASSPATH="lib/javadatepicker.jar":"$LOCALCLASSPATH"
LOCALCLASSPATH="lib/javadatepicker-i18n.jar":"$LOCALCLASSPATH"
LOCALCLASSPATH="lib/javadatepicker-binding.jar":"$LOCALCLASSPATH"
LOCALCLASSPATH="lib/javadatepicker-demo.jar":"$LOCALCLASSPATH"
LOCALCLASSPATH="lib/looks-1.3b1.jar":"$LOCALCLASSPATH"
LOCALCLASSPATH="lib/binding.jar":"$LOCALCLASSPATH"
LOCALCLASSPATH="lib/jlfgr-1_0.jar":"$LOCALCLASSPATH"

java -cp $LOCALCLASSPATH com.standbysoft.demo.date.Main