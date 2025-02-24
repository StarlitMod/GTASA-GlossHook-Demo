#include "main.h"
#include "jni.h"
#include <pthread.h>
#include <dlfcn.h>

JavaVM* g_java_vm = nullptr;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("GlossDemo loaded! (" __DATE__ ")");

    JNIEnv* env;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    if (env == nullptr) {
        return JNI_ERR;
    }

    g_java_vm = vm;

    LOGI(VERSION " Inited!");
    return JNI_VERSION_1_4;
}

