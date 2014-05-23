@echo on
set LOCALCLASSPATH=lib\JUNIT.JAR;lib\JCL.JAR;lib\JBCL.JAR;lib\COMM.JAR;lib\xerces.jar;lib\jsearch.jar;lib\jpos17-controls.jar;lib\jh.jar;lib\jhall.jar;lib\jhbasic.jar;lib\jdom.jar
rem for %%i in (lib\*.jar;) do call lcp.bat %%i

start jdk1.4.2_04\bin\javaw  -classpath "loader.jar;posv41.jar;%LOCALCLASSPATH%"  com.royalstone.pos.invoke.UpdatePrice


rem jdk1.4.2_04\bin\java  -classpath "loader.jar;posv41.jar;%LOCALCLASSPATH%"  com.royalstone.pos.loader.ProgramLoader

