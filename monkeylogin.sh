#! /bin/bash

echo ""
echo "[Waiting for launcher to start]"
LAUNCHER_READY=
while [[ -z ${LAUNCHER_READY} ]]; do
    UI_FOCUS=`adb shell "dumpsys window | grep mCurrentFocus"`
    echo "(DEBUG) Current focus: ${UI_FOCUS}"

    case $UI_FOCUS in
    *"Launcher"*)
        LAUNCHER_READY=true
    ;;
    "")
        echo "Waiting for window service..."
        sleep 3
    ;;
    *"Not Responding"*)
        echo "Detected an ANR! Dismissing..."
        adb shell input keyevent KEYCODE_DPAD_DOWN
        adb shell input keyevent KEYCODE_DPAD_DOWN
        adb shell input keyevent KEYCODE_ENTER
    ;;
    *)
        echo "Waiting for launcher..."
        sleep 3
    ;;
    esac
done
# install debug version on device
./gradlew installDebug


echo "Launcher is ready :-)"





adb shell monkey -p powerball.apps.jacs.powerball --ignore-security-exceptions --ignore-native-crashes -v 25000