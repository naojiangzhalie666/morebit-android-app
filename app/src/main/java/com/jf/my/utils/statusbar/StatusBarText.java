package com.jf.my.utils.statusbar;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jf.my.utils.statusbar.phone.OSUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏字体颜色(分类 小米，魅族，6.0以上)
 * @author liys
 * @version 1.0  2018/10/12
 */
public class StatusBarText {

    /**
     * 改变状态栏颜色
     * @param activity
     * @param darkmode true黑色字体  false白色字体
     */
    public static void setStatusBarDarkMode(Activity activity, boolean darkmode){
        OSUtils.ROM_TYPE romType = OSUtils.getRomType();
        if(romType == OSUtils.ROM_TYPE.MIUI){ //小米
            setMiuiStatusBarDarkMode(activity, darkmode);
        }else if(romType == OSUtils.ROM_TYPE.FLYME){ //魅族
            setFlymeStatusBarDarkIcon(activity, darkmode);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //6.0以上
                if(darkmode){
//                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }else{
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        }
    }

    //小米
    private static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //魅族
    private static boolean setFlymeStatusBarDarkIcon(Activity activity, boolean dark) {
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
}
