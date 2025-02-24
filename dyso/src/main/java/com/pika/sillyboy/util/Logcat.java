package com.pika.sillyboy.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Logcat {
    private static final String TAG = "Dylog";

    File logParentFile;
    String fileName;
    Context context;
    int maxLogSize = 5;

    public Logcat(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        logParentFile = new File(context.getExternalFilesDir(null).getParentFile(), "libs");
        Log.d(TAG, "default log path: " + logParentFile.getAbsolutePath());
    }

    public void setParentPath(String path) {
        logParentFile = new File(path);
    }

    public Process saveLog() {
        File log_file = new File(logParentFile, fileName);

        Log.d(TAG, "save log to: " + log_file.getAbsolutePath());
        if (!logParentFile.exists()) {
            logParentFile.mkdirs();
        }

        if (log_file.exists() && log_file.length() > (long) maxLogSize * 1024 * 1000) {
             log_file.delete(); // 日志太大时候删除日志
        }

        Process process = null;

        try {
            process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -f " + log_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;
    }


    public void clearLog() {
//        File log_dir = new File(context.getExternalFilesDir(null).getParentFile(), "libs");
        File log_file = new File(logParentFile, fileName);

        if (log_file.exists()) {
            log_file.delete();
        }
    }


    public void setMaxLogSize(int maxLogSize) {
        this.maxLogSize = maxLogSize;
    }

}