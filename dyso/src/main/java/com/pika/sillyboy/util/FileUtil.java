package com.pika.sillyboy.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.pika.sillyboy.enums.PathType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static String getPath(Context context, PathType type) {
        if (type == PathType.ANDROID_DATA)
            return context.getExternalFilesDir(null).getParentFile().getAbsolutePath();
        if (type == PathType.ANDROID_DATA_FILES)
            return context.getExternalFilesDir(null).getAbsolutePath();
        if (type == PathType.ANDROID_DATA_CACHE)
            return context.getExternalCacheDir().getAbsolutePath();
        if (type == PathType.DATA_DATA) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return context.getDataDir().getAbsolutePath();
            } else {
                return context.getFilesDir().getParentFile().getAbsolutePath();
            }
        }
        if (type == PathType.DATA_DATA_FILES)
            return context.getFilesDir().getAbsolutePath();
        if (type == PathType.SDCARD)
            return Environment.getExternalStorageDirectory().getAbsolutePath();

        return null;
    }

    public static File getFile(Context context, PathType type) {
        if (type == PathType.ANDROID_DATA)
            return context.getExternalFilesDir(null).getParentFile();
        if (type == PathType.ANDROID_DATA_FILES)
            return context.getExternalFilesDir(null);
        if (type == PathType.ANDROID_DATA_CACHE)
            return context.getExternalCacheDir();
        if (type == PathType.DATA_DATA) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return context.getDataDir();
            } else {
                return context.getFilesDir().getParentFile();
            }
        }
        if (type == PathType.DATA_DATA_FILES)
            return context.getFilesDir();
        if (type == PathType.SDCARD)
            return Environment.getExternalStorageDirectory();

        return null;
    }

    // 递归清空目录
    public static void clearDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearDirectory(file);
                    }
                    file.delete();
                }
            }
        }
    }

    // 获取指定文件的文件描述符
    public static FileDescriptor getFileDescriptorFromFile(String filePath) throws IOException {
        FileDescriptor fd = null;

        try (FileInputStream fis = new FileInputStream(filePath)) {
            // 从已打开的文件流中获取文件描述符
            fd = fis.getFD();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("文件未找到：" + filePath);
        }

        return fd;
    }

    // 计算文件的 MD5 值
    public static String calculateMD5(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(filePath);
             DigestInputStream dis = new DigestInputStream(fis, md)) {
            byte[] buffer = new byte[8192]; // 8 KB 缓冲区
            while (dis.read(buffer) != -1) {
                // 读取文件内容并更新摘要
            }
        }
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b & 0xff));
        }
        return hexString.toString();
    }


    public static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
        }

        return content.toString();
    }

    public static void writeFile(String filePath, String content) throws IOException {
        // 创建一个 FileWriter 对象来写入文件内容
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
    
   public static void grantExecPermission(File file) {
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);
    }

    public static void extractAssetLibFiles(Context context, ArrayList<String> librarys, File targetDir) {
        for (String lib : librarys) {
            File file = new File(targetDir, lib);
            if (file.exists()) {
                continue;
            }
            extractAssetLibFile(context, lib, targetDir);
        }
    }

    public static void extractAssetLibFile(Context context, String fileName, File targetDir) {
        AssetManager assetManager = context.getAssets();
        String assetPath = "libs/" + fileName;
        File targetFile = new File(targetDir, fileName);

        InputStream in = null;
        FileOutputStream out = null;

        try {
            // 创建目标目录
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            in = assetManager.open(assetPath);
            out = new FileOutputStream(targetFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            Log.e(TAG, "extractAssetLibFile: ", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "extractAssetLibFile: ", e);
            }
        }
    }

    /**
     * 复制文件到SD卡
     * @param context
     * @param fileName 复制的文件名
     * @param path 保存的目录路径
     */
    public static void copyAssetsFile(Context context, String fileName, String path) {
        try {
            // 打开 Assets 文件
            InputStream mInputStream = context.getAssets().open(fileName);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs(); // 创建目标目录
            }
            File mFile = new File(path, fileName);
            if (!mFile.exists()) {
                mFile.getParentFile().mkdirs(); // 补创建目录
                Log.d(TAG, "copyAssetsFile: " + mFile.getAbsolutePath());
                mFile.createNewFile(); // 创建目标文件
            }
            Log.d(TAG, "开始拷贝文件：" + fileName);

            // 拷贝文件内容
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = mInputStream.read(buffer)) > 0) {
                mFileOutputStream.write(buffer, 0, bytesRead);
            }

            // 关闭流
            mInputStream.close();
            mFileOutputStream.close();

            Log.d(TAG, "文件拷贝成功：" + mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "文件拷贝失败：" + fileName, e);
        }
    }


}
