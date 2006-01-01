@echo off
REM startDemo.bat
REM
REM Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
REM
REM This software is the proprietary information of Stand By Soft, Ltd.  
REM Use is subject to license terms.

SET LOCALCLASSPATH=%CLASSPATH%
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/javadatepicker.jar
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/javadatepicker-i18n.jar
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/javadatepicker-binding.jar
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/javadatepicker-demo.jar
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/looks-1.3b1.jar
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/jlfgr-1_0.jar
SET LOCALCLASSPATH=%LOCALCLASSPATH%;lib/binding.jar

java -cp %LOCALCLASSPATH% com.standbysoft.demo.date.Main