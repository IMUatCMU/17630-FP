@echo off

REM -----------------------------------------
REM %~dp0 - drive and path to the script
REM %* - all the arguments
REM     http://stackoverflow.com/questions/357315/get-list-of-passed-arguments-in-windows-batch-script-bat
REM     Documentation also in "call /?"
REM Using double quotes for the jar to accommodate for
REM     spaces in the file path.
REM -----------------------------------------

java -jar "%~dp0/rudi-final.jar" %*
