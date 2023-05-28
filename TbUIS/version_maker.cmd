:: Script for automatic creation of error versions

::Configuration ========================================
@echo off

::Target directory for generated files
set outDir=.\release_archive\generated_versions
::Nave of directory with example seeds.
set seedDir=.\version_seeds
::Name of output filename of maven compilation process
set mavenOutFileName=UIS-1.7.2.war

:: =====================================================
:: Generating versions

::Backup current seed.xml
del .\src\main\resources\seed.bck
move .\src\main\resources\seed.xml .\src\main\resources\seed.bck

::Create output directory for error versions

rmdir /S /Q %outDir%
mkdir %outDir%
del %outDir%\mvn.log

::Creating loop
for /f %%f in ('dir /b %seedDir%') do (
 @echo off
 :: for every seed process build
 echo =======================================================================================
 echo =========
 echo Creating new defect version: %%f
 echo =========
 echo =========
 
 echo prepare seed.xml
 copy /Y .\%seedDir%\%%f .\src\main\resources\seed.xml
 
 echo mvn clean
 mvn clean >> %outDir%\mvn.log
 echo Run mvn package 
 mvn package >> %outDir%\mvn.log
 if errorlevel 1 (
   echo Target mvn package fails see log in %outDir%\mvn.log
   exit /b %errorlevel%
 )
 
 echo Create output war
 move /Y .\target\%mavenOutFileName% %outDir%\%%~nf.war
 
 echo Done version
 echo =======================================================================================
)

::Clean maven project after building
mvn clean >> %outDir%\mvn.log

::Delete tmp seed file and return backup file
del .\src\main\resources\seed.xml
move .\src\main\resources\seed.bck .\src\main\resources\seed.xml

echo Done generating
exit
