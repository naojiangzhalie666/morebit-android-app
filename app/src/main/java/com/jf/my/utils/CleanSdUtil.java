package com.jf.my.utils;

/**
 * Created by Administrator on 2017/11/1.
 */

import android.content.Context;
import android.os.Environment;

import com.jf.my.contact.SdDirPath;

import java.io.File;
import java.math.BigDecimal;

/**
 * 清除缓存
 */
public class CleanSdUtil {

    /**
     * @param context
     * @return
     * @throws Exception 获取当前缓存
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * @param context 删除缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 删除文件夹里面所有和本身文件夹
     *
     * @param dir
     */
    public static boolean deleteDirALL(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                int size = 0;
                if (children != null) {
                    size = children.length;
                    for (int i = 0; i < size; i++) {
                        boolean success = deleteDirALL(new File(dir, children[i]));
                        if (!success) {
                            return false;
                        }
                    }
                }

            }
            if (dir == null) {
                return true;
            } else {

                return dir.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件夹里面所有
     *
     * @param dir
     */
    public static void deleteDir(File dir) {
        try {

            if (dir == null || !dir.exists() || !dir.isDirectory())
                return;
            for (File file : dir.listFiles()) {
                if (file.isFile())
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    deleteDir(file); // 递规的方式删除文件夹
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/您的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/您的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            int size2 = 0;
            if (fileList != null) {
                size2 = fileList.length;
                for (int i = 0; i < size2; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * 计算缓存的大小
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 初始化文件目录
     */
    public static void initCacheDirs() {
        try {
            File file = new File(SdDirPath.APK_CACHE_PATH);
            if (!file.isDirectory()) {
                file.delete();
            }
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    SdDirPath.BasePath = Environment.getExternalStorageDirectory().getPath();
                    if (!file.exists()) {
                        boolean mkdirs1 = file.mkdirs();
                        if (!mkdirs1) {
                            SdDirPath.BasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}