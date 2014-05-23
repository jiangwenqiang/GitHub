@echo off
set LOCALCLASSPATH=
for %%i in (lib\*.jar;) do call lcp.bat %%i

start javaw  -classpath "loader.jar;posv41.jar;%LOCALCLASSPATH%"  com.royalstone.pos.loader.ProgramLoader

rem jdk1.4.2_04\bin\java  -classpath "loader.jar;posv41.jar;%LOCALCLASSPATH%"  com.royalstone.pos.loader.ProgramLoader


