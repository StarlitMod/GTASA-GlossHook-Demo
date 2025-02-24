package com.pika.sillyboy;

import android.content.Context;
import android.util.Log;

import com.pika.sillyboy.elf.ElfParser;
import com.pika.sillyboy.pathinsert.LoadLibraryUtils;
import com.pika.sillyboy.util.LoadLibUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


class DynamicSo {

    private static final String TAG = "DynamicSo";

    public static void loadSoDynamically(File soFIle, String path) {
        try {
            ElfParser parser = null;
            final List<String> dependencies;
            try {
                parser = new ElfParser(soFIle);
                dependencies = parser.parseNeededDependencies();
            } finally {
                if (parser != null) {
                    parser.close();
                }
            }
            //如果nativecpp3->nativecpptwo->nativecpp 则先加载 DynamicSo.loadStaticSo(nativecpptwo)，此时nativecpp作为nativecpptwo的直接依赖被加载了
            //不能直接加载nativecpp3，导致加载直接依赖nativetwo的时候nativecpp没加载导致错误。 这个可以优化，比如递归
            for (final String dependency : dependencies) {

                try {
                    File file = new File(path.concat(dependency));
                    if (file.exists()) {
                        //递归查找
                        loadSoDynamically(file, path);
                    } else {
                        // so文件不存在这个文件夹，代表是ndk中的so，如liblog.so，则直接加载
                        // 把本来lib前缀和.so后缀去掉即可
                        if (!LoadLibUtils.isLibLoaded(dependency)) { // by DeepDC Android 8 修复重复加载动态库报错问题 arm32好像有毛病？
                            String dependencySo = dependency.substring(3, dependency.length() - 3);
                            //在application已经注入了路径DynamicSo.insertPathToNativeSystem(this,file) 所以采用系统的加载就行
                            System.loadLibrary(dependencySo);
                        }
                    }


                } catch (Exception e) {
                    Log.e(TAG, "loadSoDynamically: ", e);
                }

            }
        } catch (IOException ignored) {
        }
        // 先把依赖项加载完，再加载本身
        String libName = soFIle.getName().substring(3, soFIle.getName().length() - 3);
        Log.d("", "loadSoDynamically: loading " + libName);
        System.loadLibrary(libName);
    }

    public static void insertPathToNativeSystem(Context context,File file){
        try {
            LoadLibraryUtils.installNativeLibraryPath(context.getClassLoader(), file);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
