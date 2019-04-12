:: Removes all *.class files, then removes the assets directory
del /S a3\*.class
del /S myGameEngine\*.class
echo y | rmdir /S assets
