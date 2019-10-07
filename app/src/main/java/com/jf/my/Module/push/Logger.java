package com.jf.my.Module.push;

import android.util.Log;

import com.jf.my.BuildConfig;

/**
 * Created by efan on 2017/4/13.
 */

public class Logger {

    //设为false关闭日志
    private static final boolean LOG_ENABLE = BuildConfig.DEBUG;
    private static final String TAG = "打印信息 蜜源---->>>";

    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.e(tag, msg);
        }
    }

    /*************默认TAG Log*****************/
    public static void i(String msg) {
        if (LOG_ENABLE) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (LOG_ENABLE) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (LOG_ENABLE) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (LOG_ENABLE) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (LOG_ENABLE) {
            Log.e(TAG, msg);
        }
    }

}
