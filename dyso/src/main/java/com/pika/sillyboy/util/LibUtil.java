package com.pika.sillyboy.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LibUtil {
    private static final String TAG = "LibUtil";
    
    /**
     *   获取系统可能选择的架构
    **/
    public static String getSystemSelectedArchName(Set<String> apkSupportedArch) {
    	String osArch = System.getProperty("os.arch");
        String simpleOsArch = getSimpleArchName(osArch);
        for (String arch : apkSupportedArch) {
            String simpleArch = getSimpleArchName(arch);
            if (simpleArch.equals(simpleOsArch)) { // 如果匹配到了相同架构，直接使用这个架构
            	return simpleArch;
            }
        }
        // 未匹配到相同架构
        for (String arch : apkSupportedArch) {
            String simpleArch = getSimpleArchName(arch);
            if (simpleOsArch.contains(simpleArch)) { // 如果包含相同类型架构(如arm64兼容arm)，就使用下位替代(arm)
            	return simpleArch;
            }
        }
        return null; // 记得判空
    }


     /**
         * ：不打算支持其它架构
         */
    public static String getSimpleArchName(String abi) {
        if (abi.contains("arm64") || abi.contains("aarch64")) {
            return "arm64";
        } else if (abi.contains("arm")) {
            return "arm";
        } else if (abi.contains("x86_64")) {
            return "x86_64";
        } else if (abi.contains("x86")) {
            return "x86";
        }
        return "unknown";
    }
public static String getComplexArchName(String abi) {
        if (abi.contains("arm64") || abi.contains("aarch64")) {
            return "arm64-v8a";
        } else if (abi.contains("arm")) {
            return "armeabi-v7a";
        } else if (abi.contains("x86_64")) {
            return "x86_64";
        } else if (abi.contains("x86")) {
            return "x86";
        }
        return "unknown";
    }

    // 获取 APK 动态库支持的所有架构类型和名称
    public static Set<String> getSupportedArchitectures(Context context) {
        Set<String> supportedArchitectures = new HashSet<>();

        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            String apkFilePath = applicationInfo.sourceDir;

            try (ZipFile apkFile = new ZipFile(apkFilePath)) {
                for (ZipEntry entry : Collections.list(apkFile.entries())) {
                    String entryName = entry.getName();

                    // 判断是否是动态库文件
                    if (entryName.startsWith("lib/") && !entry.isDirectory()) {
                        String[] segments = entryName.split("/");
                        if (segments.length >= 3) {
                            String architecture = segments[1];
                            supportedArchitectures.add(architecture);
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            System.out.println("获取 APK 动态库支持的架构时发生错误：" + e.getMessage());
        }

        return supportedArchitectures;
    }

    // 从 APK 文件中提取 lib 目录下的指定架构的文件，不包含文件夹
    public static void extractLibsFromApk(Context context, File outputDirectory, String arch) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            String apkFilePath = applicationInfo.sourceDir;

            // 清空输出目录
//            FileUtil.clearDirectory(new File(outputDirectory));

            try (ZipFile apkFile = new ZipFile(outputDirectory)) {
                Enumeration<? extends ZipEntry> entries = apkFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String entryName = entry.getName();

                    // 构建目标路径，仅提取指定架构的文件
                    String targetPath = "lib/" + arch + "/";
                    if (entryName.startsWith(targetPath) && !entry.isDirectory()) {
                        // 提取文件名
                        String[] segments = entryName.split("/");
                        String fileName = segments[segments.length - 1];

                        String outputFilePath = outputDirectory + File.separator + fileName;
                        File outputFile = new File(outputFilePath);

                        try (InputStream inputStream = apkFile.getInputStream(entry);
                             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
                        FileUtil.grantExecPermission(outputFile); // 给执行权限
                    }
                }
                System.out.println("lib/" + arch + " 目录下的文件已提取到：" + outputDirectory);
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            System.out.println("提取 lib/" + arch + " 目录下的文件时发生错误：" + e.getMessage());
        }
    }



    // 从 APK 文件中提取 lib 目录下的所有文件
    public static void extractLibsFromApk(Context context, String outputDirectory) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            String apkFilePath = applicationInfo.sourceDir;

            // 清空输出目录
            FileUtil.clearDirectory(new File(outputDirectory));

            try (ZipFile apkFile = new ZipFile(apkFilePath)) {
                Enumeration<? extends ZipEntry> entries = apkFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String entryName = entry.getName();

                    // 判断是否是 lib 目录下的文件
                    if (entryName.startsWith("lib/") && !entry.isDirectory()) {
                        String outputFilePath = outputDirectory + File.separator + entryName.substring("lib/".length());
                        File outputFile = new File(outputFilePath);
                        if (!outputFile.getParentFile().exists()) {
                            outputFile.getParentFile().mkdirs();
                        }

                        try (InputStream inputStream = apkFile.getInputStream(entry);
                             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
                    }
                }
                System.out.println("lib 目录下的文件已提取到：" + outputDirectory);
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            System.out.println("提取 lib 目录下的文件时发生错误：" + e.getMessage());
        }
    }

    // 检查文件头是否匹配为elf
    public static boolean isVaildLib(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] headerBytes = new byte[4];
            int bytesRead = fis.read(headerBytes);

            return bytesRead == 4 && headerBytes[0] == 0x7F && headerBytes[1] == 'E' && headerBytes[2] == 'L' && headerBytes[3] == 'F';
        }
    }


}
