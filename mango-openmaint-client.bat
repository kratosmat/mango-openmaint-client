@ECHO OFF

rem =============================================================================
rem
rem Mango OpenMaint Client process Launch Command 
rem
rem =============================================================================
rem
rem Building the classpath inside the variable CLASSPATH1 of the application
rem 

@for %%i in (lib\*.jar) do call lib\addclp.bat %%i

TITLE Mango OpenMaint Client

@ECHO ON
java -cp src\main\resources;%CLASSPATH1%  it.storelink.openmaintmango.AllineatoreMain 1>> log\mango-openmaint-client.log 2>>&1
@ECHO OFF

goto END

:END