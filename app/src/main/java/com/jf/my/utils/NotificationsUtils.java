package com.jf.my.utils;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: 通知栏 工具类
 * @Author: liys
 * @CreateDate: 2019/3/16 11:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/16 11:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NotificationsUtils {

    /**
     * 检查通知栏权限有没有开启
     * 参考SupportCompat包中的方法： NotificationManagerCompat.from(context).areNotificationsEnabled();
     */
    public static boolean isNotificationEnabled(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | RuntimeException | ClassNotFoundException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }


    /**
     * 打开系统设置页面
     * @param context
     */
    public static void openSystemSetting(Context context){
        String packageName = context.getApplicationContext().getPackageName();
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");

            localIntent.putExtra("com.android.settings.ApplicationPkgName",
                    packageName);
        }
        context.startActivity(localIntent);
    }

    /**
     * 通知栏设置
     * @param context
     */
    public static void openSystemNotifications(Context context){
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }else {
            ///< 4.4以下Api 19 以前是没有通知管理的，默认都是开启，不用管，可考虑跳转到应用详情页面,
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(intent);
    }
}
