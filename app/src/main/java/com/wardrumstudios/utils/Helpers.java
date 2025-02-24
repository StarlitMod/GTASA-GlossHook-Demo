package com.wardrumstudios.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");
    public static String resourceClassString = "com.wardrumstudios.utils.R";
    public static Random sRandom = new Random(SystemClock.uptimeMillis());

    private Helpers() {
    }

    static String parseContentDisposition(String contentDisposition) {
        try {
            Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
            if (m.find()) {
                return m.group(1);
            }
        } catch (IllegalStateException e) {
        }
        return null;
    }

    public static File getFilesystemRoot(String path) {
        File cache = Environment.getDownloadCacheDirectory();
        if (path.startsWith(cache.getPath())) {
            return cache;
        }
        File external = Environment.getExternalStorageDirectory();
        if (path.startsWith(external.getPath())) {
            return external;
        }
        throw new IllegalArgumentException("Cannot determine filesystem root for " + path);
    }

    public static boolean isExternalMediaMounted() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        return true;
    }

    public static long getAvailableBytes(File root) {
        StatFs stat = new StatFs(root.getPath());
        return ((long) stat.getBlockSize()) * (((long) stat.getAvailableBlocks()) - 4);
    }

    public static boolean isFilenameValid(String filename) {
        String filename2 = filename.replaceFirst("/+", "/");
        return filename2.startsWith(Environment.getDownloadCacheDirectory().toString()) || filename2.startsWith(Environment.getExternalStorageDirectory().toString());
    }

    static void deleteFile(String path) {
        try {
            new File(path).delete();
        } catch (Exception e) {
            Log.w("Helpers.TAG", "file: '" + path + "' couldn't be deleted", e);
        }
    }

    public static String getDownloadProgressString(long overallProgress, long overallTotal) {
        if (overallTotal == 0) {
            return "";
        }
        return String.format("%.2f", Float.valueOf(((float) overallProgress) / 1048576.0f)) + "MB /" + String.format("%.2f", Float.valueOf(((float) overallTotal) / 1048576.0f)) + "MB";
    }

    public static String getDownloadProgressStringNotification(long overallProgress, long overallTotal) {
        if (overallTotal == 0) {
            return "";
        }
        return getDownloadProgressString(overallProgress, overallTotal) + " (" + getDownloadProgressPercent(overallProgress, overallTotal) + ")";
    }

    public static String getDownloadProgressPercent(long overallProgress, long overallTotal) {
        if (overallTotal == 0) {
            return "";
        }
        return Long.toString((100 * overallProgress) / overallTotal) + "%";
    }

    public static String getSpeedString(float bytesPerMillisecond) {
        return String.format("%.2f", Float.valueOf((1000.0f * bytesPerMillisecond) / 1024.0f));
    }

    public static String getTimeRemaining(long durationInMilliseconds) {
        SimpleDateFormat sdf;
        if (durationInMilliseconds > 3600000) {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        }
        return sdf.format(new Date(durationInMilliseconds - ((long) TimeZone.getDefault().getRawOffset())));
    }

    public static String getExpansionAPKFileName(Context c, boolean mainFile, int versionCode) {
        return (mainFile ? "main." : "patch.") + versionCode + "." + c.getPackageName() + ".obb";
    }

    public static String generateSaveFileName(Context c, String fileName) {
        return getSaveFilePath(c) + File.separator + fileName;
    }

    public static String getSaveFilePath(Context c) {
        File root = Environment.getExternalStorageDirectory();
        String path = (File.separator + "Android" + File.separator + "obb" + File.separator);
        System.out.println("getSaveFilePath " + root.toString() + path + c.getPackageName());
        return root.toString() + path + c.getPackageName();
    }

    public static boolean doesFileExist(Context c, String fileName, long fileSize, boolean deleteFileOnMismatch) {
        File fileForNewFile = new File(generateSaveFileName(c, fileName));
        if (fileForNewFile.exists()) {
            if (fileForNewFile.length() == fileSize) {
                return true;
            }

            if (deleteFileOnMismatch) {
                fileForNewFile.delete();
            }
        }
        return false;
    }

    public static int GetResourceIdentifier(Context c, String name, String className) {
        try {
            Class[] classes = Class.forName(resourceClassString).getClasses();
            Class desireClass = null;
            int i = 0;
            while (true) {
                if (i >= classes.length) {
                    break;
                } else if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                } else {
                    i++;
                }
            }
            if (desireClass != null) {
                return desireClass.getField(name).getInt(desireClass);
            }
            return 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return 0;
        } catch (SecurityException e3) {
            e3.printStackTrace();
            return 0;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return 0;
        } catch (NoSuchFieldException e5) {
            e5.printStackTrace();
            return 0;
        }
    }

    public static int getDownloaderStringResourceIDFromState(Context c, int state) {
        switch (state) {
            case 1:
                return GetResourceIdentifier(c, "state_idle", "string");
            case 2:
                return GetResourceIdentifier(c, "state_fetching_url", "string");
            case 3:
                return GetResourceIdentifier(c, "state_connecting", "string");
            case 4:
                return GetResourceIdentifier(c, "state_downloading", "string");
            case 5:
                return GetResourceIdentifier(c, "state_completed", "string");
            case 6:
                return GetResourceIdentifier(c, "state_paused_network_unavailable", "string");
            case 7:
                return GetResourceIdentifier(c, "state_paused_by_request", "string");
            case 8:
                return GetResourceIdentifier(c, "state_paused_wifi_disabled", "string");
            case 9:
                return GetResourceIdentifier(c, "state_paused_wifi_unavailable", "string");
            case 10:
                return GetResourceIdentifier(c, "state_paused_wifi_disabled", "string");
            case 11:
                return GetResourceIdentifier(c, "state_paused_wifi_unavailable", "string");
            case 12:
                return GetResourceIdentifier(c, "state_paused_roaming", "string");
            case 13:
                return GetResourceIdentifier(c, "state_paused_network_setup_failure", "string");
            case 14:
                return GetResourceIdentifier(c, "state_paused_sdcard_unavailable", "string");
            case 15:
                return GetResourceIdentifier(c, "state_failed_unlicensed", "string");
            case 16:
                return GetResourceIdentifier(c, "state_failed_fetching_url", "string");
            case 17:
                return GetResourceIdentifier(c, "state_failed_sdcard_full", "string");
            case 18:
                return GetResourceIdentifier(c, "state_failed_cancelled", "string");
            default:
                return GetResourceIdentifier(c, "state_unknown", "string");
        }
    }
}
