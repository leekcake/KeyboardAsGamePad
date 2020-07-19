# KeyboardAsGamePad
A simple tool for remap (desktop keyboard) as (android gamepad)
## Purpose of development
for control 'joy con droid' with desktop.
## Archive Reason
joy con droid or android system hangs on many input. So i doesn't need to develop it anymore.
## Typical checklist
-   I do not guarantee that this will work normally.
-   Because my English is not good, overall word selection and long sentence completion may be inadequate.
## How to launch this program
- Add 'adb' to your $PATH so called on everywhere
- run 'gradlew app:assembleDebug' or same action on your IDE.
- Copy (Project Root)/app/build/outputs/apk/debug/app-debug.apk to (Project Root)/kagp.jar
- connect your device, just only one listed on adb.
- run 'moe.leekcake.keyboardasgamepad.KAGPApp' on your IDE.
