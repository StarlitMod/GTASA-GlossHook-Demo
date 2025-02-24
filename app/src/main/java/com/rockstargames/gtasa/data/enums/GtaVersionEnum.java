package com.rockstargames.gtasa.data.enums;

import java.util.ArrayList;
import java.util.List;

public enum GtaVersionEnum {

    GTASA_2_00("libs/GTASA_2_00/", "2.00"),
    GTASA_2_10_32("libs/GTASA_2_10_32/", "2.10_32"),
    GTASA_2_10_64("libs/GTASA_2_10_64/", "2.10_64");

    public final String path;
    public final String version;
    GtaVersionEnum(String path, String version) {
        this.path = path;
        this.version = version;
    }

    public static GtaVersionEnum getDefault() {
        return GTASA_2_00;
    }

    public static String getPathByName(String name) {
        GtaVersionEnum[] list = GtaVersionEnum.values();
        for (int i = 0; i < list.length; i++) {
            if (list[i].name().equals(name)) {
                return list[i].path;
            }
        }
        return getDefault().path;
    }

    public static int getIndexByName(String name) {
        GtaVersionEnum[] list = GtaVersionEnum.values();
        for (int i = 0; i < list.length; i++) {
            if (list[i].name().equals(name)) {
                return i;
            }
        }
        return 0;
    }

    public static List<String> getVersionList() {
        GtaVersionEnum[] list = GtaVersionEnum.values();
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            newList.add(list[i].version);
        }
        return newList;
    }

}