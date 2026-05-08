@echo off
cd /d "%~dp0"
echo ============================================
echo   AppRating System - Starting...
echo ============================================
echo.
echo Checking Maven...
set MAVEN_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\plugins\maven\lib\maven3
set PATH=%MAVEN_HOME%\bin;%PATH%
echo.
echo Building and starting Jetty server on http://localhost:8080
echo Press Ctrl+C to stop.
echo.
call mvn jetty:run
pause
