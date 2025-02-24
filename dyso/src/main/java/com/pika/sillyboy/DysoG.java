package com.pika.sillyboy;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;

import com.pika.sillyboy.constant.Constant;
import com.pika.sillyboy.enums.PathType;
import com.pika.sillyboy.util.DefaultSettingUtil;
import com.pika.sillyboy.util.FileUtil;
import com.pika.sillyboy.util.LibUtil;
import com.pika.sillyboy.util.Logcat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import kotlin.jvm.functions.Function1;

@Keep
public class DysoG {
    private static final String TAG = "DysoG";

    public static ArrayList<LinkedHashMap<String, Integer>> libsList = new ArrayList<>();
    //    private static String targetLibPath;
    private static File targetLibFile;
    public static String selectedArch;
    private static boolean useCustomArch = false;

    /**
     * 加载dyso，在要加载动态库的地方调用
     *
     */
    @Keep
    public static void loadLibs() {
        /*DynamicSoLauncher.INSTANCE.initDynamicSoConfig(context, context.getFilesDir() + "/dyso/libs/", new Function1<String, Boolean>() {
            @Override
            public Boolean invoke(String s) {
                return true;
            }
        });

        DynamicSoLauncher.INSTANCE.loadSoDynamically("libSCAnd.so");
        DynamicSoLauncher.INSTANCE.loadSoDynamically("libGTASA.so");*/
        LinkedHashMap<String, Integer> libsMap = libsList.get(0); // 无参loadLibs()方法默认获取第一个lib列表
        Iterator<String> libs = libsMap.keySet().iterator();
        Iterator<Integer> delayTimes = libsMap.values().iterator();
        while (libs.hasNext()) {
            String libName = libs.next();
            int delayTime = delayTimes.next();
            File libFile = new File(targetLibFile, libName); // 暂未使用
            try {
                int finalDelayTime = delayTime;
                if (finalDelayTime > 0) {
                    Log.i(TAG, "Load ".concat(libName).concat(" after ").concat(String.valueOf((float)delayTime/1000)).concat(" sec..."));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(finalDelayTime);
                                Log.i(TAG, "Loading ".concat(libName).concat("..."));
                                DynamicSoLauncher.INSTANCE.loadSoDynamically(libName); // 这个libName是要传完整文件名的
                            } catch (Exception e) {
                                Log.e(TAG, "Dyso load ".concat(libName).concat(" failed: "), e);
                                Log.e(TAG, "Try original loadLibrary() method...");
                                try {
                                    System.loadLibrary(libName.substring(3, libName.length() - 3));  // 这个libName不需要后缀.so和前缀lib
                                } catch (ExceptionInInitializerError | UnsatisfiedLinkError err) {
                                    Log.e(TAG, "Failed to load ".concat(libName).concat(": "), err);
                                }
                            }
                        }
                    }).start();
                } else {
                    Log.i(TAG, "Loading ".concat(libName).concat("..."));
                    DynamicSoLauncher.INSTANCE.loadSoDynamically(libName); // 这个libName是要传完整文件名的
                }
            } catch (Exception e) {
                Log.e(TAG, "Dyso load ".concat(libName).concat(" failed: "), e);
                Log.e(TAG, "Try original loadLibrary() method...");
                try {
                    System.loadLibrary(libName.substring(3, libName.length() - 3));  // 这个libName不需要后缀.so和前缀lib
                } catch (ExceptionInInitializerError | UnsatisfiedLinkError err) {
                    Log.e(TAG, "Failed to load ".concat(libName).concat(": "), err);
                }
            }

        }
    }

    @Keep
    public static void init(Context context) {
        /**
         * 定义变量
         */
        File sourceLibFile = new File(FileUtil.getFile(context, PathType.ANDROID_DATA), "/libs/");
        targetLibFile = new File(FileUtil.getFile(context, PathType.DATA_DATA_FILES), "/dyso/libs/");
        String apkLibsPath = context.getFilesDir().getAbsolutePath().concat("/dyso/lib/");

        /**
         * 预创建主要文件夹
         */
        if (!sourceLibFile.exists()) {
            sourceLibFile.mkdirs();
            Log.i(TAG, "dyso libs path created: ".concat(sourceLibFile.getAbsolutePath()));
        }
        if (!targetLibFile.exists()) {
            targetLibFile.mkdirs();
            Log.i(TAG, "dyso execute path created: ".concat(targetLibFile.getAbsolutePath()));
        }

        /**
         * 创建架构文件夹
         */
        Set<String> supportedArchitectures = LibUtil.getSupportedArchitectures(context); // 获取apk内lib文件夹下的全部架构
        for (String arch : supportedArchitectures) { // 仅用于创建对应数量的子文件夹
            String simpleArch = LibUtil.getSimpleArchName(arch);
            File sourceLibArchFile = new File(sourceLibFile, simpleArch);
            if(!sourceLibArchFile.exists()) {
                sourceLibArchFile.mkdir();
                DefaultSettingUtil.readDysoSettings(context, simpleArch); // 初始化各架构的设置
            }
        }

        /**
         * 获取系统自动选择的架构
         */
        selectedArch = LibUtil.getSystemSelectedArchName(supportedArchitectures);
        if (selectedArch == null) {
            Log.e(TAG, "Unsupported architecture or no jni");
            return;
        }

        /**
         * 读取总json，并获取当前选择的架构
         */
        String generalSettingsStr = DefaultSettingUtil.readGeneralDysoSettings(context);
        if (generalSettingsStr == null) {
            Log.e(TAG, "Load general dyso settings failed");
        }
        try {
            JSONObject jsonObject = new JSONObject(generalSettingsStr);
            if (jsonObject.has("architecture")) {
                String archStr = jsonObject.getString("architecture"); // 获取配置文件的架构
                String simpleArchStr = LibUtil.getSimpleArchName(archStr);
                if(!archStr.isEmpty()) {
                    selectedArch = simpleArchStr;
                    useCustomArch = true;
                    Log.i(TAG, "Use ".concat(selectedArch).concat("."));
                }

                if (jsonObject.has("log")) {
                    JSONObject log = jsonObject.getJSONObject("log");
                    String path = log.getString("path");
                    boolean enableLog = log.getBoolean("enable");
                    boolean clearOnBoot = log.getBoolean("clearOnBoot");
                    int maxLogSize = log.getInt("maxLogSize");

                    Logcat logcat = new Logcat(context, Constant.LOG_NAME);
                    if (!path.isBlank()) {
                        Log.d(TAG, "DyLog path: " + path);
                        logcat.setParentPath(path);
                    }
                    if (clearOnBoot) {
                        Log.d(TAG, "DyLog cleaning old log...");
                        logcat.clearLog();
                    }
                    if (maxLogSize > 0) {
                        logcat.setMaxLogSize(maxLogSize);
                    }
                    if (enableLog) {
                        logcat.saveLog();
                    }

                    Log.i(TAG, "------------------\tInitializing DyLog...\t------------------");
                    Log.i(TAG, "------------------\tVersion: " + Constant.VERSION + "\t------------------");
                    Log.i(TAG, "------------------\tCompile Date: " + Constant.BUILD_TIME + "\t------------------");
                    Log.i(TAG, "------------------\tAuthor: " + Constant.AUTHOR + "\t------------------");
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, "Load general dyso settings json failed");
        }


        sourceLibFile = new File(sourceLibFile, selectedArch);

        /**
         * 将assets里内置的libs解压到目录
         */
        FileUtil.extractAssetLibFiles(context, Constant.librarys, sourceLibFile);

        /**
         * 加载设置
         */
        String settingsStr = DefaultSettingUtil.readDysoSettings(context, selectedArch);
        if (settingsStr == null) {
            Log.e(TAG, "Load dyso settings failed");
        }
        try {
            JSONObject jsonObject = new JSONObject(settingsStr);
            JSONObject settingsObject = jsonObject.getJSONObject("settings");
            if (settingsObject.has("libPath")) {
                String libPath = settingsObject.getString("libPath"); // 自定义路径
                if (!libPath.equals("./") && !libPath.isEmpty()) { // 修改了路径
                    try {
                        File libFile = new File(libPath);
                        Log.d(TAG, "libs dir exist: " + libFile.exists());
                        if (libFile.mkdirs()) {
                            Log.d(TAG, "custom lib path created: " + libPath);
                        }
                        if (libFile.exists() && libFile.isDirectory() && libFile.canRead()) {
                            sourceLibFile = libFile;
                            Log.d(TAG, "using custom libPath: " + libPath);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "set libPath failed: ", e);
                    }
                }
            }
            JSONArray libsArray = jsonObject.getJSONArray("libs");
            Log.i(TAG, "Loading DysoSettings...");
            for (int i = 0; i < libsArray.length(); i++) {
                JSONArray eachCallLibsArray = libsArray.getJSONArray(i);

                LinkedHashMap<String, Integer> libsMap = new LinkedHashMap<>();
                for(int j = 0; j < eachCallLibsArray.length(); j++) {
                    JSONObject libJson = eachCallLibsArray.getJSONObject(j);
                    String libName = libJson.getString("name");
                    int delayTime = libJson.getInt("delayTimeMillis");

                    libsMap.put(libName, delayTime); // 添加lib名称和加载延迟
                    Log.i(TAG, "lib added: ".concat("[").concat(String.valueOf(j+1)).concat("]").concat(libName));
                }
                libsList.add(libsMap);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Load dyso settings json failed");
        }


        /**
         * 复制lib到可执行路径/data/data/package_name/ 在其它路径(如/sdcard/Android/data/package_name/)无足够权限
         */
        File[] sourceLibFiles = sourceLibFile.listFiles();
        if (sourceLibFiles != null) {
            FileUtil.clearDirectory(targetLibFile);
            Log.i(TAG, "Detected " + sourceLibFiles.length + " librarys from " + sourceLibFile.getAbsolutePath());
            Log.i(TAG, "Copying dynamic libs to executable folder...");
            for (File file : sourceLibFiles) {
                String fileName = file.getName();
                if (file.isFile() && !fileName.endsWith(".json") && !fileName.endsWith(".txt")) {
                    try {
                        if (!LibUtil.isVaildLib(file.getAbsolutePath())) { // 是动态库才拷贝
                            Log.i(TAG, "Copy ".concat(fileName).concat("failed, context file is not a arm.library"));
                        } else {
//                            File targetFile = new File(targetLibPath.concat(fileName));
                            File targetFile = new File(targetLibFile, fileName);
                            FileUtil.copyFile(file, targetFile);
                            FileUtil.grantExecPermission(targetFile);
                            Log.i(TAG, "dyso lib copied: ".concat(fileName));
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "dyso lib '".concat(fileName).concat("' copy failed: "), e);
                    }
                }
            }
        } else {
            Log.e(TAG, "cannot get access from " + sourceLibFile.getAbsolutePath());
        }

        /**
         * 解压apk内lib文件夹下选定架构的动态库，优先加载此目录下的libs
         */
        if (useCustomArch) {
            LibUtil.extractLibsFromApk(context, targetLibFile, LibUtil.getComplexArchName(selectedArch));
        }

        /**
         * 往配置文件内写入apk和.so的信息
         */



        /**
         * 初始化动态库的加载路径
         */
        DynamicSoLauncher.INSTANCE.initDynamicSoConfig(context, targetLibFile.getAbsolutePath().concat(File.separator), new Function1<String, Boolean>() {
            @Override
            public Boolean invoke(String s) {
                return true;
            }
        });

        Log.i(TAG, "------------------ Dyso G Initialized ------------------");
    }
}
