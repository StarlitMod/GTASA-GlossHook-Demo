package com.pika.sillyboy.util;

import android.content.Context;
import android.util.Log;

import com.pika.sillyboy.DynamicSoLauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoadLibUtils {
    private static final String TAG = "LoadLibUtils";

    public static void fromAssets(Context context, String assetPath, String targetPath) {

        File soFile = new File(targetPath, assetPath);
        File targetFolder = soFile.getParentFile();
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }
        if (soFile.exists()) {
            soFile.setReadable(true);
            soFile.setWritable(true);
            soFile.setExecutable(true);
            soFile.delete();
        }
        FileUtil.copyAssetsFile(context, assetPath, targetPath);

        Log.d(TAG, "fromAssets path:" + assetPath
                + "\nsoFile:" + soFile.getPath());

        soFile.setReadable(true);
        // targetSdk 33 and above require read only
        soFile.setWritable(false);
        soFile.setExecutable(false);

        DynamicSoLauncher.INSTANCE.initDynamicSoConfig(context, targetFolder.getPath() + "/", s -> {
            Log.d(TAG, "init DynamicSoConfig");
            return true;
        });

        String libname = soFile.getName().substring(3, soFile.getName().length() - 3);
        Log.d(TAG, "fromAssets: libName:" + libname);
        DynamicSoLauncher.INSTANCE.loadSoDynamically(soFile);
    }

    public static boolean isLibLoaded(String libName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/self/maps"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(libName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "isLibLoaded: ", e);
        }
        return false;
    }
}
