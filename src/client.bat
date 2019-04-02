@echo off
echo Sever IP Address:
set /p ip=
echo Server Port:
set /p port=
java -Dsun.java2d.d3d=false a3.MyGame %ip% %port%
pause
