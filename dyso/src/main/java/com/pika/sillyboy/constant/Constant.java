package com.pika.sillyboy.constant;

import com.pika.sillyboy.BuildConfig;

import java.util.ArrayList;

public class Constant {

//    private static final String LIBRARIES = "libXLog.so,libAML.so,libUE4.so,libc++_shared.so";
//    private static final String LIBRARIES = "libImmEmulatorJ.so,libSCAnd.so,libGTASA.so,libthreadfix.so,libAML.so";
    private static final String LIBRARIES = "libSCAnd.so,libGTASA.so";

    public final static ArrayList<String> librarys = new ArrayList<>();

    public static final String VERSION = "1.2";

    public static final String BUILD_TIME = BuildConfig.BUILD_TIME;

    public static final String AUTHOR = "DeepDC";

    public static final String LOG_NAME = "DysoLog.txt";

    static {
        for (String lib: LIBRARIES.split(",")) {
            librarys.add(lib);
        }
    }
}
