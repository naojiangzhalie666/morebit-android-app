package com.jf.my.utils.fire;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.io.File;

/**
 * Created by 周开平 on 2017/4/2 22:43.
 * qq 275557625@qq.com
 * 作用：解决Android 6.0以上系统的权限问题
 */

public class PermissionUtils {
    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;
    static {
        //存储android个别权限当前版本是否存在
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
    }

    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int storagePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);

            if (storagePermission != PackageManager.PERMISSION_GRANTED ||
                    cameraPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(PERMISSIONS_CAMERA_AND_STORAGE, requestCode);
                return false;
            }
        }
        return true;
    }

    public static boolean sdCardIsAvailable() {
        //首先判断外部存储是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            Log.e("qq", "sd = " + sd);//sd = /storage/emulated/0
            return sd.canWrite();
        } else {
            return false;
        }
        }

    /**
     * 检测是否需要请求权限
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(Context context, String... permissions) {
        for (String permission : permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测某个权限是否已经授权；如果已授权则返回true，如果未授权则返回false
     * @param context
     * @param permission
     * @return
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    /**
     * 检测当前版本权限是否存在
     * @param permission
     * @return
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }
}