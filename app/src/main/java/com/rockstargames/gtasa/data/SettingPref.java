package com.rockstargames.gtasa.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.rockstargames.gtasa.data.enums.GtaVersionEnum;

public class SettingPref {

    private static SettingPref instance;
    private static SharedPreferences sp;
    private final String PREF_NAME = "settings";

    private SettingPref(Context context) {
        this.sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SettingPref getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingPref.class) {
                if (instance == null) {
                    instance = new SettingPref(context);
                }
            }
        }
        return instance;
    }

    public void setGTASAVersion(String version) {
        sp.edit().putString("gtasa_version", version).apply();
    }
    public String getGTASAVersion() {
        return sp.getString("gtasa_version", GtaVersionEnum.GTASA_2_00.name());
    }
}
