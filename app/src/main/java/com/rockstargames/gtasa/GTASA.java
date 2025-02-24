package com.rockstargames.gtasa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.pika.sillyboy.DynamicSoLauncher;
import com.pika.sillyboy.DysoApplication;
import com.pika.sillyboy.util.LoadLibUtils;
import com.rockstargames.gtasa.data.SettingPref;
import com.rockstargames.gtasa.data.enums.GtaVersionEnum;
import com.wardrumstudios.utils.WarMedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;
import kotlin.jvm.functions.Function1;

public class GTASA extends WarMedia {
    private static final String TAG = "GTASA";

    public static GTASA gtasaSelf = null;
    static String vmVersion;

    boolean UseExpansionPack = true;

    static {
        vmVersion = null;
        Log.i(TAG, "**** Loading SO's static");

        try {
            vmVersion = System.getProperty("java.vm.version");
            Log.i(TAG, "vmVersion " + vmVersion);

            // We don't load librarys in static block for selecting sa versions

            // System.loadLibrary("ImmEmulatorJ");
            // DysoApplication.loadLibs();
        }
        catch (ExceptionInInitializerError | UnsatisfiedLinkError e) {
            Log.e(TAG, e.getMessage());
        }

        // System.loadLibrary("SAMP");
        // System.loadLibrary("SCAnd");
        // System.loadLibrary("GTASA");
        // System.loadLibrary("AML");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "**** onCreate");

        SettingPref pref = SettingPref.getInstance(this);
        String path = GtaVersionEnum.getPathByName(pref.getGTASAVersion());

        try {
            vmVersion = System.getProperty("java.vm.version");
            Log.i(TAG, "vmVersion " + vmVersion);

            LoadLibUtils.fromAssets(this, path.concat("libImmEmulatorJ.so"), getFilesDir().getAbsolutePath());
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            Log.i(TAG, "Loading GTA libraries...");
            // 在这里修改动态库加载顺序，以及加载新的动态库
            // 动态库丢到 assets/libs 里对应的版本文件夹里，如果使用 dlopen，请把目标动态库放到 jniLibs 对应架构文件夹中
            // edit libs loading order or add new lib here
            // put libs into assets/libs/gtasa_version or jniLibs/your_arch folder, if using dlopen, please put dlopen target lib into jniLibs/your_arch folder
            // LoadLibUtils.fromAssets(this, path.concat("libXXX.so"), getFilesDir().getAbsolutePath());
            LoadLibUtils.fromAssets(this, path.concat("libSCAnd.so"), getFilesDir().getAbsolutePath());
            LoadLibUtils.fromAssets(this, path.concat("libGTASA.so"), getFilesDir().getAbsolutePath());

        } catch (Throwable e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }

        gtasaSelf = this;
        wantsMultitouch = true;

        expansionFileName = "main.8." + getPackageName() + ".obb";
        patchFileName = "patch.8." + getPackageName() + ".obb";
        apkFileName = GetPackageName(getPackageName());
        Log.i(TAG, "apkFileName " + apkFileName);

        baseDirectory = GetGameBaseDirectory();
        AllowLongPressForExit = true;

        UseExpansionPack = true;

        if (UseExpansionPack) {
            xAPKS = new XAPKFile[2];
            xAPKS[0] = new XAPKFile(true, 8, 1967561852);
            xAPKS[1] = new XAPKFile(false, 8, 625313014);
        }

        wantsAccelerometer = true;

        //RestoreCurrentLanguage();

        super.onCreate(savedInstanceState);
        SetReportPS3As360(true);
    }

    @Override
    public boolean ServiceAppCommand(String cmd, String args) {
        if (cmd.equalsIgnoreCase("SetLocale")) {
            SetLocale(args);
            return false;
        }

        return false;
    }

    @Override
    public int ServiceAppCommandValue(String cmd, String args) {
        if (cmd.equalsIgnoreCase("GetDownloadBytes")) {
            return 0;
        }

        if (cmd.equalsIgnoreCase("GetDownloadState")) {
            return 4;
        }

        return (!cmd.equalsIgnoreCase("GetNetworkState") || !isNetworkAvailable()) ? 0 : 1;
    }

    @Override
    public void onStart() {
        Log.i(TAG, "**** onStart");
        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.i(TAG, "**** onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "**** onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "**** onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "**** onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "**** onDestroy");
        super.onDestroy();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

//    @Override
//    public boolean CustomLoadFunction() {
//        return CheckIfNeedsReadPermission(gtasaSelf);
//    }

    public static void staticEnterSocialClub() {
        gtasaSelf.EnterSocialClub();
    }

    public static void staticExitSocialClub() {
        gtasaSelf.ExitSocialClub();
    }

    public void EnterSocialClub() {
        Log.i(TAG, "**** EnterSocialClub");
    }

    public void ExitSocialClub() {
        Log.i(TAG, "**** ExitSocialClub");
    }
}
