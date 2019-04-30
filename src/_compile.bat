:: Cleans the working files,
:: then builds both the client and server
:: finally, copies current assets directory
call _clean.bat

javac a3\*.java
javac a3\editor\*.java
javac a3\network\server\*.java
javac myGameEngine\mesh\*.java

echo d | xcopy /e /v /y ..\assets assets

pause
