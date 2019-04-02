del a3\*.class
del a3\network\api\*.class
del a3\network\api\messages\*.class
del a3\network\api\messages\impl\*.class
del a3\network\client\*.class
del a3\network\logging\*.class
del a3\network\server\*.class
del a3\network\server\impl\*.class
del myGameEngine\asset\*.class
del myGameEngine\controller\*.class
del myGameEngine\controller\controls\*.class
del myGameEngine\mesh\*.class

javac a3\*.java
javac a3\network\server\*.java

xcopy /e /v /y ..\assets assets

pause
