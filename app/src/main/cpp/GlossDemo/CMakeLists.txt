cmake_minimum_required(VERSION 3.19)

project(GlossDemo C CXX)

# look for all c++ file
function(get_all_files DIR)
    file(GLOB_RECURSE all_files "${DIR}/*.c*" "${DIR}/*.h*")
    set(${ARGN} ${all_files} PARENT_SCOPE)
endfunction()

get_all_files(${CMAKE_CURRENT_SOURCE_DIR} SOURCE_FILES)

add_library(
        GlossDemo
        SHARED
        ${SOURCE_FILES}
)

# set GlossHook library path
set(glosshook-path "${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI}/libGlossHook.a")
add_library(glosshook STATIC IMPORTED)
set_target_properties(glosshook PROPERTIES IMPORTED_LOCATION ${glosshook-path})

target_compile_options(${PROJECT_NAME} PRIVATE
        -w
        -s
        -pthread
        -fpack-struct=1
        -O2
        -std=c++11
        -fexceptions
        -frtti
        -fstrict-aliasing
        -fno-omit-frame-pointer
        -fstack-protector
        -fno-short-enums
        -fvisibility=default
)

target_compile_definitions(${PROJECT_NAME} PRIVATE RAKSAMP_CLIENT)


target_include_directories(${PROJECT_NAME} INTERFACE
        "${CMAKE_CURRENT_SOURCE_DIR}/include"
)

target_link_libraries(
        ${PROJECT_NAME}
        log
        z
        android
        OpenSLES
        glosshook
)