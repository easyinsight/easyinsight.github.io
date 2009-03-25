@echo off
set CLASSPATH=
for %%i in (..\lib\*.jar) do call eicpappend.bat %%i
set CLASSPATH=%JARS%

"%JAVA_HOME%"\bin\java -classpath "%CLASSPATH%" com.easyinsight.dbservice.Configuration