package com.pika.sillyboy.util;

import android.content.Context;
import android.util.Log;

import com.pika.sillyboy.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class DefaultSettingUtil {

    private static final String TAG = "DefaultSettingUtil";


    /**
     * 生成默认总控json文件
     *
     **/
    private static String getDefaultGeneralSettings() throws JSONException {
        JSONObject settingsObject = new JSONObject();
        settingsObject.put("architecture", ""); // 留空则使用系统默认
        settingsObject.put("debugMode", false); // 调试模式

        JSONObject logObj = new JSONObject(); // 日志相关
        logObj.put("enable", false); // 是否启用日志
        logObj.put("path", ""); // 日志路径
        logObj.put("clearOnBoot", true); // 启动时是否清空日志
        logObj.put("maxLogSize", 5); // 大小，默认最大存储5mb

        settingsObject.put("log", logObj);
        return settingsObject.toString(4);
    }


    /**
     * 生成默认的各架构的dyso设置json文件
     * @return String
     * @throws JSONException
     */
    private static String getDefaultDysoSettings() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject settingsJson = new JSONObject();
        settingsJson.put("libPath", "./");

        JSONArray libsArray = new JSONArray();

        for(int i = 0; i < 1; i++) { // 还没想如何获取调用次数，但是先做了
            JSONArray eachCallLibsArray = new JSONArray();
            for (String lib: Constant.librarys) {
                JSONObject libJson = new JSONObject();
                libJson.put("name", lib);
                libJson.put("delayTimeMillis", 0); // 延迟时间(单位毫秒)
                eachCallLibsArray.put(libJson);
            }
            libsArray.put(eachCallLibsArray);
        }

        JSONArray libsInfoArray = new JSONArray(); // so加载的时候才获取，这里的所有参数都无需读取，只需在每次获取到的时候动态写入即可
        JSONObject infoJson = new JSONObject();
        infoJson.put("libInfos", libsInfoArray);

        jsonObject.put("settings", settingsJson);
        jsonObject.put("libs", libsArray);
        jsonObject.put("infos", infoJson);

        return jsonObject.toString(4);
    }


    public static String readGeneralDysoSettings(Context context) {
        String configPath = context.getExternalFilesDir(null).getParentFile().getAbsolutePath().concat("/libs/GeneralSettings.json");
        File configFile = new File(configPath);
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        if (!configFile.exists()) {
            try {
                FileUtil.writeFile(configPath, getDefaultGeneralSettings());
            } catch (IOException e) {
                Log.e(TAG, "Write dyso settings file failed: ", e);
            } catch (JSONException e) {
                Log.e(TAG, "Write dyso settings Json failed: ", e);
            }
        }
        try {
            String dysoJsonStr = FileUtil.readFile(configPath);
            return dysoJsonStr;
        } catch (IOException e) {
            Log.e(TAG, "Read dyso setting file failed: ", e);
        }
        return null;
    }

    public static String readDysoSettings(Context context, String arch) {
        String configPath = context.getExternalFilesDir(null).getParentFile().getAbsolutePath().concat("/libs/").concat(arch).concat("/DysoSettings.json");
        File configFile = new File(configPath);
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        if (!configFile.exists()) {
            try {
                FileUtil.writeFile(configPath, getDefaultDysoSettings());
            } catch (IOException e) {
                Log.e(TAG, "Write dyso settings file failed: ", e);
            } catch (JSONException e) {
                Log.e(TAG, "Write dyso settings Json failed: ", e);
            }
        }
        try {
            String dysoJsonStr = FileUtil.readFile(configPath);
            return dysoJsonStr;
        } catch (IOException e) {
            Log.e(TAG, "Read dyso setting file failed: ", e);
        }
        return null;
    }
}
