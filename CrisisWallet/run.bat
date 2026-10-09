@echo off
rem Compile and run CrisisWallet (needs JDK installed)
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
if errorlevel 1 pause & exit /b 1
java -cp out crisiswallet.Main
