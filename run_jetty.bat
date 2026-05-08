@echo off
cd /d "%~dp0"
echo ============================================
echo   AppRating System - Starting with Jetty
echo ============================================
echo.
set JAVA_HOME=C:\Program Files\Java\jdk-26
set MAVEN_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\plugins\maven\lib\maven3
set PATH=%MAVEN_HOME%\bin;%PATH%
echo.
echo Building and starting on http://localhost:8080
echo (First run will download Jetty plugin - may take 1-2 min)
echo.
call mvn jetty:run-war
pause
