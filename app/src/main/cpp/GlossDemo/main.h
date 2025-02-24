#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <algorithm>
#include <list>
#include <string>
#include <fstream>
#include <vector>
#include <sys/mman.h>
#include <android/log.h>
#include <jni.h>

#define LOG_TAG "Dyso_G"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define VERSION "1.0"

#ifndef PAGESIZE
#define PAGESIZE PAGE_SIZE
#endif




