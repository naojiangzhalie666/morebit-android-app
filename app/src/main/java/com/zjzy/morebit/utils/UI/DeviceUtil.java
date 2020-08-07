package com.zjzy.morebit.utils.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.graphics.ColorUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LongSparseArray;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by wzm on 2019/9/20.
 * <p>
 * 屏幕及设备参数获取工具类
 */
public final class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();

    private static String PHONE_IMEI = null;
    /**
     * 默认无效的 mac 地址列表
     * 02:00:00:00:00:02
     * 00:90:4C:11:22:33
     */
    public static final String[] DEFAULT_INVALID_MAC = new String[]{"02:00:00:00:00:02", "00:90:4C:11:22:33"};

    public static boolean isNeedCreateShortcut() {
        return !getNotNeedCreateShortcutDeviceList().contains(Build.MODEL);
    }

    public static boolean isAfterAndroidO() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean isAfterAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 获取不需要创建快捷方式的手机
     *
     * @return
     */
    private static HashSet<String> getNotNeedCreateShortcutDeviceList() {
        final HashSet<String> set = new HashSet<String>();
        set.add("ZTE N939Sc");
        set.add("Coolpad 9976D");
        set.add("vivo X20");
        return set;
    }

    /**
     * 得到栈顶activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Activity context) {
        try {
            final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

            if (runningTaskInfos != null) {
                return (runningTaskInfos.get(0).topActivity).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    /**
     * 计算颜色是否亮色
     *
     * @return
     */
    public static boolean calculateColorLight(int color) {
        // int color= Color.parseColor("#f0f0f0");
        double v = ColorUtils.calculateLuminance(color);
        if (v >= 0.5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 魅族 状态栏文字颜色
     *
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 小米状态栏文字颜色
     *
     * @param activity
     * @param dark
     * @return
     */
    private static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 谷歌原生方式改变状态栏文字颜色
     *
     * @param activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     */
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 6.0
     * 设置 手机状态栏的文字颜色为黑的
     */
//    public static void setStatusBarWordsColor(Activity activity, boolean dark) {
//        OSUtils.ROM romType = OSUtils.getRomType();
//        switch (romType) {
//            case MIUI:
//                //  LZLog.i(TAG, "setStatusBarWordsColor MIUI");
//                DeviceUtil.MIUISetStatusBarLightMode(activity, dark);
//                break;
//            case Flyme:
//                //    LZLog.i(TAG, "setStatusBarWordsColor Flyme");
//                DeviceUtil.setFlymeLightStatusBar(activity, dark);
//                break;
//            default:
//                //  LZLog.i(TAG, "setStatusBarWordsColor default");
//                DeviceUtil.setAndroidNativeLightStatusBar(activity, dark);
//                break;
//        }
//    }

    public static void fullScreen(Activity activity) {
        fullScreen(activity, false);
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public static void fullScreen(Activity activity, boolean dark) {
        // activity.getWindow().setBackgroundDrawable(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
      /*          window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);*/
                //设置状态栏字体颜色黑白
              //  setStatusBarWordsColor(activity, dark);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //状态栏颜色透明,会导致  StatusBarUtils.setColor 无效 需要使用 StatusBarUtils.addTranslucentColorView
                //window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.WHITE);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 沉浸式状态栏 设置
     *
     * @param activity
     */
    public static void statusBarScreen(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//隐藏状态栏 显示全屏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//去除状态栏，导航栏标记
                //绘画模式
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //去除半透明遮罩
                window.setStatusBarColor(Color.TRANSPARENT);//状态栏颜色透明
                window.setNavigationBarColor(Color.WHITE);//修改导航栏颜色 白色
            } else {
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


    /**
     * 获取屏幕宽度
     *
     * @return px
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return px
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static void copyString(Context context, String msg) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(msg + "");
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号 如vivo X20
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商 如Xiaomi
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }


    /**
     * 获取手机imei
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        String imei = "imei000";
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = mTelephonyManager.getImei();
                } else {
                    imei = mTelephonyManager.getDeviceId();
                }
                if (TextUtils.isEmpty(imei)) {
                    imei = getSimImei();
                }
            }
            if (imei != null) {
                imei = imei.toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    @SuppressLint("MissingPermission")
    public static String getSimImei() {
        Class<?> clazz;
        try {
            clazz = Class.forName("android.telephony.TelephonyManager");
            Method method = clazz.getDeclaredMethod("getSecondary", Object.class);
            method.setAccessible(true);
            TelephonyManager telManager = (TelephonyManager) method.invoke(clazz);
            if (telManager != null) {
                return telManager.getDeviceId();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 手机系统的版本号
     *
     * @return
     */
    public static String getDeviceVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取版本Code
     * 得到的是100121017 即1.0.0版12月10日17点
     * 可以用getVersionName 代替
     *
     * @param context
     * @return
     */
    public static long getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        long versionCode = 0;
        if (info != null) {
            versionCode = info.versionCode;
        }

        return versionCode;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }

        String versionName = "0";
        if (info != null) {
            versionName = info.versionName;
        }

        return versionName;
    }

    public static String getCurrentProcessName(final Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        final int myPid = android.os.Process.myPid();

        String processName = null;
        if (processes != null) {
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                if (myPid == process.pid) {
                    processName = process.processName;
                    break;
                }
            }
        }

        return processName;
    }

    public static boolean isMainProcess(final Context context) {
        final String processName = getCurrentProcessName(context);
        return TextUtils.equals(context.getPackageName(), processName) || TextUtils.isEmpty(processName);
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    public static String getAndroidId(Context context) {
        if (context != null) {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return "";
    }

    /**
     * 设置系统剪贴板信息
     *
     * @param context
     * @param content
     */
    public static void putClipboardText(final Context context, final CharSequence content) {
        final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    /**
     * 获取剪切板内容
     *
     * @param context
     * @return
     */
    public static CharSequence getClipboardText(final Context context) {
        final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            final ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                final ClipData.Item item = clipData.getItemAt(0);
                if (item != null) {
                    return item.getText();
                }
            }
        }
        return null;
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        // LZLog.d(TAG, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext() + " dest_context=" + destContext);
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context activity) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 获取虚拟按键的高度
     */
    public static int getNavigationBarHeight(Activity activity) {
        if (!checkDeviceHasNavigationBar(activity)) {
            return 0;
        }
        if (!isNavigationBarShow(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取状态栏 高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置View 为状态栏高度，适配沉浸式状态栏
     */
    public static void setStatusBarHeight(Activity activity, View status_bar) {
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup.LayoutParams layoutParams = status_bar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        status_bar.setLayoutParams(layoutParams);
    }

    public static void clearPreloadedDrawables() {
        // 只能在主线程调用
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }

        try {
            final Field field = Resources.class.getDeclaredField("sPreloadedDrawables");

            if (field != null) {
                field.setAccessible(true);

                final Object object = field.get(Resources.class);

                if (object != null) {
                    if (object instanceof LongSparseArray) {
                        final LongSparseArray<?> sPreloadedDrawables = (LongSparseArray<?>) object;

                        if (sPreloadedDrawables != null) {
                            sPreloadedDrawables.clear();
                        }
                    } else if (object instanceof LongSparseArray[]) {
                        final LongSparseArray<?>[] sPreloadedDrawables =
                                (LongSparseArray<?>[]) object;

                        if (sPreloadedDrawables != null && sPreloadedDrawables.length > 0) {
                            final int count = sPreloadedDrawables.length;

                            for (int i = 0; i < count; ++i) {
                                sPreloadedDrawables[i].clear();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Nothing to do
        }
    }

    public static void showInputMethod(final View view, final int flags) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, flags);
    }

    /**
     * 弹出输入法
     *
     * @param view
     */
    public static void showInputMethod(final View view) {
        showInputMethod(view, InputMethodManager.SHOW_FORCED);
    }

//    public static void showInputMethodDelay(final View view, int delay) {
//        // 显示输入法
//        ThreadUtils.runOnUiThreadDelay(() -> {
//            if (view != null && !ActivityStack.isActivityDestoryed(view.getContext())) {
//                DeviceUtil.showInputMethod(view);
//            }
//        }, delay);
//    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public static void hideInputMethod(final View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), /*InputMethodManager.HIDE_NOT_ALWAYS*/0);
    }


    public static void setTaskDescription(final Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
        }
    }

    public static boolean isSystemApp(final PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     * 检测某个service服务是否在运行
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.isEmpty())
            return false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) && TextUtils.equals(
                    serviceList.get(i).service.getPackageName(), context.getPackageName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 获取签名的SHA1
     *
     * @param context
     * @return
     */
    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isHonor10() {
        return Build.MODEL.contains("COL-AL10");
    }

    public static boolean isVivoX20() {
        return Build.MODEL.contains("vivo X20");
    }

    public static boolean isOPPO() {
        return Build.MODEL.contains("OPPO");
    }

    public static String getIPAddress(Context context) {
        @SuppressLint("MissingPermission")
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


//    public static String getMeid(Context context) {
//        if (PermissionsUtil.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String DEVICE_ID = tm.getDeviceId();
//            return DEVICE_ID;
//        } else {
//            return "";
//        }
//    }

    /**
     * 判断虚拟按键是否显示
     */
    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * 获取mac地址
     * 可设置过滤一些无效的 MAC 地址，比如 02:00:00:00:00:02
     *
     * @param context    context
     * @param filterList 指定要过滤的 MAC 地址
     * @return
     */
    public static String getMac(Context context, String... filterList) {
        String strMac = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            strMac = getMacAPI22(context);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            strMac = getMacAPI23(context);
        } else {
            strMac = getMacAPI24(context);
        }
        if (!TextUtils.isEmpty(strMac) && filterList != null && filterList.length > 0) {
            for (String filter : filterList) {
                if (TextUtils.isEmpty(filter)) {
                    continue;
                }
                if (TextUtils.equals(filter.toUpperCase(), strMac.toUpperCase())) {
                    return "";
                }
            }
        }
        return strMac;
    }

    public static String getMacAPI22(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    public static String getBluetoothMacAddress(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }

            if (TextUtils.isEmpty(bluetoothMacAddress)) {
                bluetoothMacAddress = Settings.Secure.getString(context.getContentResolver(),
                        "bluetooth_address");
            }
        } else {
            if (null != bluetoothAdapter) {
                bluetoothMacAddress = bluetoothAdapter.getAddress();
            }
        }
        return bluetoothMacAddress;
    }

    public static String getMacAPI23(Context context) {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return macSerial;
    }

    public static String getMacAPI24(Context context) {
        String strMacAddr = "";
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            strMacAddr = "";
        }
        return strMacAddr;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    public static String getUniqueId(Context context) {
        try {
            String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String id = androidID + Build.SERIAL;

            if (isValidUUID(id)) {
                return "AD" + toMD5(id);
            }
            id = getImei(context);
            if (isValidUUID(id)) {
                return "IE" + toMD5(id);
            }

            String macAddr = getMacAddress();
            if (!TextUtils.isEmpty(macAddr)) {
                macAddr = macAddr.replace(":", "");
                if (isValidUUID(macAddr)) {
                    id = macAddr;
                    return "MC" + toMD5(id);
                }
            }

            if (TextUtils.isEmpty(id)) {
                id = Build.SERIAL;
                return "EA" + toMD5(id);
            }
            return "FF" + toMD5("FFFFFFFF");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "00";
    }

    /**
     * 校验，非空，非“0000”， 非null
     *
     * @param uuid
     * @return
     */
    private static boolean isValidUUID(String uuid) {
        String checkuuid = uuid.replace("0", "");
        return !TextUtils.isEmpty(uuid) && !TextUtils.isEmpty(checkuuid) && !"null".equals(uuid);
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }

    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void gotoNotificationSetting(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }

        if (TextUtils.equals(Build.BRAND, "Xiaomi")) {//小米手机无法跳转至打开通知栏页面，参考友商的方式，跳转至app信息页
            toSelfSetting(activity);
            return;
        }

        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                activity.startActivityForResult(intent, requestCode);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, requestCode);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + pkg));
                activity.startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            // LZLog.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 查询手机内非系统应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }
}

//    /**
//     * 获取当前的运营商
//     *
//     * @param context
//     * @return 运营商名字
//     */
//    public static String getOperator(Context context) {
//        if (PermissionsUtil.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
//            String ProvidersName = "";
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String IMSI = telephonyManager.getSubscriberId();
//            LZLog.i(TAG, "运营商代码" + IMSI);
//            if (IMSI != null) {
//                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
//                    ProvidersName = "中国移动";
//                } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
//                    ProvidersName = "中国联通";
//                } else if (IMSI.startsWith("46003")) {
//                    ProvidersName = "中国电信";
//                }
//                return ProvidersName;
//            } else {
//                return "";
//            }
//        }
//        return "";
//    }
//}