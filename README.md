# GlossHook Demo

> GlossHook usage demo (hook and patch examples not included yet)

use: https://github.com/XMDS/GlossHook.git

This is a Gradle-based project for compiling both the GTA:SA APK and JNI components directly within Android Studio. A clean CMake environment has been set up, and GlossHook is already integrated.

Supports both v2.00 and v2.10 of GTA:SA, you can change the librarys in `jniLibs` folder

As requested by [XMDS](https://github.com/XMDS), the author of GlossHook. I upload this demo stripped from my SAMP project.
### 💡What can you do with this demo
1. You can develop and debug your c++ plugins and dynamic librarys with one of the best java,kt&cpp IDEs and real time android logcat panel in Android Studio without compiling and pushing your .so manually to mobile phone or emulator, inserting them in GTASA apk, signing the apk(if you don't use AML), and checking logs in your logs.txt
2. You can do some nice UI in java and call them with C++ codes like input text, dialog or any other widgets which are hard to impl with imgui
3. This project aim to show the usages of GlossHook functions, so that you can feel it's simple, thread-safe and powerful hook and memory functions in patching game
