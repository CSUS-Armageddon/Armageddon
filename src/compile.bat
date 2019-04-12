del /S a3\*.class
del /S myGameEngine\*.class

javac a3\*.java
javac a3\network\server\*.java

echo d | xcopy /e /v /y ..\assets assets

pause
