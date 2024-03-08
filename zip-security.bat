@echo off

REM Folder to zip
set FOLDER_TO_ZIP1=src\main\java\dat3\security
set FOLDER_TO_ZIP2=src\test\java\dat3\security

REM Output folder for the zipped file
set OUTPUT_FOLDER=security-zipped

REM Name of the zip file
set ZIP_NAME1=security.zip
set ZIP_NAME2=security-test2.zip

rmdir /s /q "%OUTPUT_FOLDER%"
REM Create the output folder if it doesn't exist
if not exist "%OUTPUT_FOLDER%" mkdir "%OUTPUT_FOLDER%"

REM Zip the folder using PowerShell
powershell -Command "Add-Type -A 'System.IO.Compression.FileSystem'; [IO.Compression.ZipFile]::CreateFromDirectory('%FOLDER_TO_ZIP1%', '%OUTPUT_FOLDER%\%ZIP_NAME1%')"
powershell -Command "Add-Type -A 'System.IO.Compression.FileSystem'; [IO.Compression.ZipFile]::CreateFromDirectory('%FOLDER_TO_ZIP2%', '%OUTPUT_FOLDER%\%ZIP_NAME2%')"
