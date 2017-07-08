@echo off
color 2
cd %~dp0

echo carnetapp_setting.apk     signapk starting.....
java -jar signapk.jar platform.x509.pem platform.pk8 ./bin/carnetapp_ipod(S21A).apk   ./output/carnetapp_ipod.apk
echo carnetapp_ipod(S21A).apk signapk finish........

echo carnetapp_ipod(S21A).apk installing........
::adb install -r ./output/carnetapp_speech.apk
::adb shell am start -n carnetapp.settings/carnetapp.settings.Main

pause
