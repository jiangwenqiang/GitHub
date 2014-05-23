@echo off
set LOCALCLASSPATH=
for %%i in (lib\*.jar;) do call lcp.bat %%i

start javaw  -classpath "posv41.jar;%LOCALCLASSPATH%" com.royalstone.pos.shell.pos


