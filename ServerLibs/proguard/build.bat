

@echo off

set proguard_path=E:\proguard\lib\proguard.jar


echo ---���ڻ���---
java -jar %proguard_path% @proguard.pro
echo ---

pause